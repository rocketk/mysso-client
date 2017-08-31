package mysso.client.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by pengyu on 2017/8/31.
 */
public class ConfigUtil {
    private static Logger log = LoggerFactory.getLogger(ConfigUtil.class);
    private String configFile;
    private final Properties properties = new Properties();
    private static ConfigUtil instance;

    private static final String DEFAULT_CONFIG_FILE = "myssoclient.properties";

    private ConfigUtil(String configFile) {
        this.configFile = configFile;
        log.info("loading configFile from {}", configFile);
        try {
            instance.properties.load(instance.getClass().getResourceAsStream(configFile));
        } catch (IOException e) {
            log.error("an exception occurred when reading config file, caused by: " + e.getMessage(), e.getCause());
            throw new RuntimeException(e);
        }
    }

    public static ConfigUtil getInstance(String configFile) {
        if (instance != null) {
            return instance;
        }
        instance = new ConfigUtil(configFile == null || configFile.isEmpty() ? DEFAULT_CONFIG_FILE : configFile);
        return instance;
    }

    public static ConfigUtil getInstance() {
        if (instance != null) {
            return instance;
        }
        return getInstance(null);
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public boolean contains(Object value) {
        return properties.contains(value);
    }

    public Object get(Object key) {
        return properties.get(key);
    }
}
