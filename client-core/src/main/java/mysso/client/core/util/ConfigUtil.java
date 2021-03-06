package mysso.client.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

/**
 * Created by pengyu on 2017/8/31.
 */
public class ConfigUtil {
    private Logger log = LoggerFactory.getLogger(ConfigUtil.class);
    private final Properties properties = new Properties();

    public static final String DEFAULT_CONFIG_FILE = "/myssoclient.properties";
    public static final String DEFAULT_BEANS_CONFIG_FILE = "/myssoclient-beans.properties";

    public ConfigUtil(String configFile) {
        log.info("loading configFile from {}", configFile);
        try {
            this.properties.load(this.getClass().getResourceAsStream(configFile));
        } catch (IOException e) {
            log.error("an exception occurred when reading config file, caused by: " + e.getMessage(), e.getCause());
            throw new RuntimeException(e);
        }
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

    public Set<String> stringPropertyNames() {
        return properties.stringPropertyNames();
    }
}
