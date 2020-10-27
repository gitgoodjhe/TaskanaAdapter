package pro.taskana.adapter.camunda;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pro.taskana.adapter.camunda.exceptions.SystemException;

public class TaskanaConfigurationProperties {

  private static final String TASKANA_ADAPTER_CREATE_OUTBOX_SCHEMA =
      "taskana.adapter.create_outbox_schema";

  private static final String TASKANA_OUTBOX_PROPERTIES = "taskana-outbox.properties";

  private static final String TASKANA_ADAPTER_OUTBOX_SCHEMA = "taskana.adapter.outbox.schema";

  private static final String TASKANA_ADAPTER_OUTBOX_DATASOURCE_JNDI =
      "taskana.adapter.outbox.datasource.jndi";
  private static final String TASKANA_ADAPTER_OUTBOX_DATASOURCE_DRIVER =
      "taskana.adapter.outbox.datasource.driver";
  private static final String TASKANA_ADAPTER_OUTBOX_DATASOURCE_URL =
      "taskana.adapter.outbox.datasource.url";
  private static final String TASKANA_ADAPTER_OUTBOX_DATASOURCE_USERNAME =
      "taskana.adapter.outbox.datasource.username";
  private static final String TASKANA_ADAPTER_OUTBOX_DATASOURCE_PASSWORD =
      "taskana.adapter.outbox.datasource.password";

  private static final String TASKANA_ADAPTER_OUTBOX_MAX_NUMBER_OF_EVENTS =
      "taskana.adapter.outbox.max.number.of.events";
  private static final String TASKANA_ADAPTER_OUTBOX_DURATION_BETWEEN_TASK_CREATION_RETRIES =
      "taskana.adapter.outbox.duration.between.task.creation.retries";

  private static final String TASKANA_ADAPTER_OUTBOX_INITIAL_NUMBER_OF_TASK_CREATION_RETRIES =
      "taskana.adapter.outbox.initial.number.of.task.creation.retries";

  private static final String OUTBOX_SYSTEM_PROPERTY = "taskana.outbox.properties";
  private static final String OUTBOX_SCHEMA_DEFAULT = "taskana_tables";
  private static final int MAX_NUMBER_OF_EVENTS_DEFAULT = 50;
  private static final boolean CREATE_OUTBOX_SCHEMA_DEFAULT = true;
  private static final int INITIAL_NUMBER_OF_TASK_CREATION_RETRIES_DEFAULT = 5;
  private static final Duration DURATION_BETWEEN_TASK_CREATION_RETRIES_DEFAULT =
      Duration.ofHours(1);

  private static final Logger LOGGER =
      LoggerFactory.getLogger(pro.taskana.adapter.camunda.TaskanaConfigurationProperties.class);

  private final Properties outboxProperties = new Properties();

  private TaskanaConfigurationProperties() {

    String outboxPropertiesFile = System.getProperty(OUTBOX_SYSTEM_PROPERTY);

    if (outboxPropertiesFile != null) {

      try (FileInputStream stream = new FileInputStream(outboxPropertiesFile)) {

        outboxProperties.load(stream);

        LOGGER.debug(
            String.format("Outbox properties were loaded from file %s.", outboxPropertiesFile));

      } catch (IOException e) {
        LOGGER.warn(
            String.format(
                "Caught Exception while trying to load properties from provided properties file %s",
                outboxPropertiesFile),
            e);
        throw new SystemException(
            String.format(
                "Internal System error when processing properties file %s ", outboxPropertiesFile),
            e.getCause());
      }
    } else {
      try (InputStream propertiesStream =
          this.getClass().getClassLoader().getResourceAsStream(TASKANA_OUTBOX_PROPERTIES)) {

        outboxProperties.load(propertiesStream);

        LOGGER.debug(
            String.format(
                "Outbox properties were loaded from file %s from classpath.",
                TASKANA_OUTBOX_PROPERTIES));

      } catch (IOException e) {
        LOGGER.warn(
            String.format(
                "Caught Exception while trying to load properties from file %s from classpath",
                TASKANA_OUTBOX_PROPERTIES),
            e);
        throw new SystemException(
            String.format(
                "Internal System error when processing properties file %s ",
                TASKANA_OUTBOX_PROPERTIES),
            e.getCause());
      }
    }
  }

  public static TaskanaConfigurationProperties getInstance() {
    return TaskanaConfigurationProperties.LazyHolder.INSTANCE;
  }

  public static boolean getCreateOutboxSchema() {

    String createOutboxSchemaProperty =
        getInstance().outboxProperties.getProperty(TASKANA_ADAPTER_CREATE_OUTBOX_SCHEMA);

    if (createOutboxSchemaProperty == null || createOutboxSchemaProperty.isEmpty()) {
      LOGGER.info(
          "Couldn't retrieve boolean property to create schema or not, setting to default ");
      return CREATE_OUTBOX_SCHEMA_DEFAULT;
    } else if ("false".equalsIgnoreCase(createOutboxSchemaProperty)) {
      return false;
    }
    return true;
  }

  public static String getOutboxSchema() {

    String outboxSchema = getInstance().outboxProperties.getProperty(TASKANA_ADAPTER_OUTBOX_SCHEMA);

    if (outboxSchema == null || outboxSchema.isEmpty()) {
      LOGGER.info("Couldn't retrieve property entry for outbox schema, setting to default ");
      return OUTBOX_SCHEMA_DEFAULT;

    } else {
      return outboxSchema;
    }
  }

  public static String getOutboxDatasourceJndi() {
    return getInstance().outboxProperties.getProperty(TASKANA_ADAPTER_OUTBOX_DATASOURCE_JNDI);
  }

  public static String getOutboxDatasourceDriver() {
    return getInstance().outboxProperties.getProperty(TASKANA_ADAPTER_OUTBOX_DATASOURCE_DRIVER);
  }

  public static String getOutboxDatasourceUrl() {
    return getInstance().outboxProperties.getProperty(TASKANA_ADAPTER_OUTBOX_DATASOURCE_URL);
  }

  public static String getOutboxDatasourceUsername() {
    return getInstance().outboxProperties.getProperty(TASKANA_ADAPTER_OUTBOX_DATASOURCE_USERNAME);
  }

  public static String getOutboxDatasourcePassword() {
    return getInstance().outboxProperties.getProperty(TASKANA_ADAPTER_OUTBOX_DATASOURCE_PASSWORD);
  }

  public static int getOutboxMaxNumberOfEvents() {

    int maxNumberOfEventsReturned = 0;

    String maxNumberOfEventsString =
        getInstance().outboxProperties.getProperty(TASKANA_ADAPTER_OUTBOX_MAX_NUMBER_OF_EVENTS);

    if (maxNumberOfEventsString == null || maxNumberOfEventsString.isEmpty()) {
      LOGGER.info(
          "Couldn't retrieve property entry for max number of events to be returned, "
              + "setting to default ");
      maxNumberOfEventsReturned = MAX_NUMBER_OF_EVENTS_DEFAULT;

    } else {
      try {
        maxNumberOfEventsReturned = Integer.parseInt(maxNumberOfEventsString);
      } catch (NumberFormatException e) {
        maxNumberOfEventsReturned = MAX_NUMBER_OF_EVENTS_DEFAULT;
        LOGGER.warn(
            String.format(
                "Attempted to retrieve max number of events to be returned and caught Exception. "
                    + "Setting default for max number of events to be returned to %d  ",
                maxNumberOfEventsReturned),
            e);
      }
    }
    return maxNumberOfEventsReturned;
  }

  public static Duration getDurationBetweenTaskCreationRetries() {

    String durationBetweentaskCreationRetriesProperty =
        getInstance()
            .outboxProperties
            .getProperty(TASKANA_ADAPTER_OUTBOX_DURATION_BETWEEN_TASK_CREATION_RETRIES);

    if (durationBetweentaskCreationRetriesProperty == null
        || durationBetweentaskCreationRetriesProperty.isEmpty()) {
      LOGGER.info(
          "Couldn't retrieve property entry for duration between task creation retries, "
              + "setting to default ");
      return DURATION_BETWEEN_TASK_CREATION_RETRIES_DEFAULT;
    } else {
      try {
        return Duration.parse(durationBetweentaskCreationRetriesProperty);
      } catch (Exception e) {
        LOGGER.warn(
            String.format(
                "Attempted to retrieve duration between task creation retries and caught Exception."
                    + "Setting default to %s ",
                durationBetweentaskCreationRetriesProperty.toString()),
            e);

        return DURATION_BETWEEN_TASK_CREATION_RETRIES_DEFAULT;
      }
    }
  }

  public static int getInitialNumberOfTaskCreationRetries() {
    int initialNumberOfTaskCreationRetries = 0;

    String maxNumberOfEventsString =
        getInstance()
            .outboxProperties
            .getProperty(TASKANA_ADAPTER_OUTBOX_INITIAL_NUMBER_OF_TASK_CREATION_RETRIES);

    if (maxNumberOfEventsString == null || maxNumberOfEventsString.isEmpty()) {
      LOGGER.info(
          "Couldn't retrieve property entry for initial number of task creation retries, "
              + "setting to default ");
      initialNumberOfTaskCreationRetries = INITIAL_NUMBER_OF_TASK_CREATION_RETRIES_DEFAULT;

    } else {
      try {
        initialNumberOfTaskCreationRetries = Integer.parseInt(maxNumberOfEventsString);
      } catch (NumberFormatException e) {
        initialNumberOfTaskCreationRetries = INITIAL_NUMBER_OF_TASK_CREATION_RETRIES_DEFAULT;
        LOGGER.warn(
            String.format(
                "Attempted to retrieve initial number of task creation retries and caught "
                    + "Exception. Setting default for initial number of "
                    + "task creation retries to %d ",
                initialNumberOfTaskCreationRetries),
            e);
      }
    }
    return initialNumberOfTaskCreationRetries;
  }

  private static class LazyHolder {
    private static final TaskanaConfigurationProperties INSTANCE =
        new TaskanaConfigurationProperties();
  }
}
