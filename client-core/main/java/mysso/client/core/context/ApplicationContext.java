package mysso.client.core.context;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pengyu on 17-9-7.
 */
public class ApplicationContext {
    private static ApplicationContext instance;
    private Map<Class, Object> beans = new HashMap<>();
    private ApplicationContext() {

    }
    public static ApplicationContext getInstance() {
        if (instance == null) {
            instance = new ApplicationContext();
        }
        return instance;
    }

    public <T> T getBean(Class<T> interfaceClass) {
        return (T) beans.get(interfaceClass);
    }

    public <T> void setBean(Class<T> interfaceClass, T object) {
        beans.put(interfaceClass, object);
    }

}
