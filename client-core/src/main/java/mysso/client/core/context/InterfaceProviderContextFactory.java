package mysso.client.core.context;

import mysso.client.core.util.ConfigUtil;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by pengyu on 2017/9/8.
 */
public class InterfaceProviderContextFactory {
    private static Logger log = LoggerFactory.getLogger(InterfaceProviderContextFactory.class);
    public static InterfaceProviderContext createInterfaceProviderContext(String configFile4Beans) {
        ConfigUtil configUtil4Beans = new ConfigUtil(configFile4Beans);
        InterfaceProviderContext interfaceProviderContext = InterfaceProviderContext.getInstance();
        for (String interfaceName : configUtil4Beans.stringPropertyNames()) {
            // todo
            String providerName = configUtil4Beans.getProperty(interfaceName);
            try {
                Class interfaceClass = Class.forName(interfaceName);
                Validate.isTrue(interfaceClass.isInterface(), "the class %s is not a interface", interfaceName);
                Class providerClass = Class.forName(providerName);
                Object providerObj = providerClass.newInstance();
                Validate.isInstanceOf(interfaceClass, providerObj,
                        "the provider '%s' is not a instance of the interface '%s'", providerName, interfaceName);
                interfaceProviderContext.setBean(interfaceClass, providerObj);
            } catch (ClassNotFoundException e) {
                log.error("the interface class '{}' or provider class '{}' is not found", interfaceName, providerName);
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        return interfaceProviderContext;
    }
}
