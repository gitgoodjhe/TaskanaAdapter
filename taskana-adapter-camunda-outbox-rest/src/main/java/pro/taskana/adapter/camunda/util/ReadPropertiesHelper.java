package pro.taskana.adapter.camunda.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pro.taskana.adapter.camunda.TaskanaConfigurationProperties;

public class ReadPropertiesHelper implements TaskanaConfigurationProperties {

  private static final Logger LOGGER = LoggerFactory.getLogger(ReadPropertiesHelper.class);

  private final Properties outboxProperties = new Properties();

  private ReadPropertiesHelper() {

    String outboxPropertiesFile = System.getProperty("taskana.outbox.properties");

    if (outboxPropertiesFile != null) {

      try (FileInputStream stream = new FileInputStream(outboxPropertiesFile)) {
        outboxProperties.load(stream);

        LOGGER.debug("Outbox properties were loaded from file {}.", outboxPropertiesFile);

      } catch (IOException e) {
        LOGGER.warn("Caught Exception while trying to load properties", e);
      }
    } else {
      try {
        InputStream propertiesStream =
            this.getClass().getClassLoader().getResourceAsStream(TASKANA_OUTBOX_PROPERTIES);

        outboxProperties.load(propertiesStream);

        LOGGER.debug(
            "Outbox properties were loaded from file {} from classpath.",
            TASKANA_OUTBOX_PROPERTIES);

      } catch (IOException e) {
        LOGGER.warn("Caught Exception while trying to load properties", e);
      }
    }
  }

  public static ReadPropertiesHelper getInstance() {
    return LazyHolder.INSTANCE;
  }

  public String getProperty(String key) {
    return outboxProperties.getProperty(key);
  }

  private static class LazyHolder {
    private static final ReadPropertiesHelper INSTANCE = new ReadPropertiesHelper();
  }
}
