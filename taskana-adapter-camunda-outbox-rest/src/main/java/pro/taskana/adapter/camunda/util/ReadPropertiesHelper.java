package pro.taskana.adapter.camunda.util;

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
    InputStream propertiesStream =
        this.getClass().getClassLoader().getResourceAsStream(TASKANA_OUTBOX_PROPERTIES);
    try {
      outboxProperties.load(propertiesStream);
    } catch (IOException e) {
      LOGGER.warn("Caught Exception while trying to load properties", e);
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
