package mysso.client.core.context;

import mysso.client.core.util.ConfigUtil;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by pengyu on 2017/9/8.
 */
public class BeansContextFactory {
    private Logger log = LoggerFactory.getLogger(BeansContextFactory.class);
    private final String configFile4Beans;

    public BeansContextFactory(String configFile4Beans) {
        this.configFile4Beans = configFile4Beans;
    }

    public BeansContext createInterfaceProviderContext() {
        ConfigUtil configUtil4Beans = new ConfigUtil(configFile4Beans);
        BeansContext context = BeansContext.getInstance();
        for (String interfaceName : configUtil4Beans.stringPropertyNames()) {
            String providerName = configUtil4Beans.getProperty(interfaceName);
            try {
                Class interfaceClass = Class.forName(interfaceName);
//                Validate.isTrue(interfaceClass.isInterface(), "the class %s is not a interface", interfaceName);
                Class providerClass = Class.forName(providerName);
                Object providerObj = providerClass.newInstance();
                Validate.isInstanceOf(interfaceClass, providerObj,
                        "the provider '%s' is not a instance of the interface '%s'", providerName, interfaceName);
                context.setBean(interfaceClass, providerObj);
            } catch (ClassNotFoundException e) {
                log.error("the interface class '{}' or provider class '{}' is not found", interfaceName, providerName);
            } catch (IllegalAccessException e) {
                log.error(e.getMessage(), e);
            } catch (InstantiationException e) {
                log.error(e.getMessage(), e);
            }
        }
        try {
            injectDependencies(context);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
        }
        return context;
    }

    private void injectDependencies(BeansContext context) throws IllegalAccessException {
        Set failed = new HashSet();
        do {
            for (Object providerObj : context.getBeans()) {
                Class providerClass = providerObj.getClass();
                for (Field field : providerClass.getDeclaredFields()) {
                    if (field.getAnnotation(Bean.class) != null) {
                        Object bean = context.getBean(field.getDeclaringClass());
                        if (bean != null) {
                            field.set(providerObj, bean);
                            failed.remove(providerObj);
                        } else {
                            failed.add(providerObj);
                        }
                    }
                }
            }
        } while (failed.size() > 0);
    }
}
