package mysso.client.core.context;

import java.util.*;

/**
 * Created by pengyu on 17-9-7.
 */
public class BeansContext {
    private static BeansContext instance;
    private Map<Class, Object> beans = new HashMap<>();
    private BeansContext() {

    }
    public static BeansContext getInstance() {
        if (instance == null) {
            instance = new BeansContext();
        }
        return instance;
    }

    public <T> T getBean(Class<T> interfaceClass) {
        return (T) beans.get(interfaceClass);
    }

    public Collection<Object> getBeans() {
        return beans.values();
    }

    public <T> void setBean(Class<T> interfaceClass, T object) {
        beans.put(interfaceClass, object);
    }

}
