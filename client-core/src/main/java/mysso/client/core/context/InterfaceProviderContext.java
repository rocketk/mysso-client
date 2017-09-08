package mysso.client.core.context;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pengyu on 17-9-7.
 */
public class InterfaceProviderContext {
    private static InterfaceProviderContext instance;
    private Map<Class, Object> beans = new HashMap<>();
    private InterfaceProviderContext() {

    }
    public static InterfaceProviderContext getInstance() {
        if (instance == null) {
            instance = new InterfaceProviderContext();
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
