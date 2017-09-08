package mysso.client.core.context;

import mysso.client.core.session.InMemorySessionRegistry;
import mysso.client.core.session.SessionRegistry;
import mysso.client.core.util.ConfigUtil;
import mysso.client.core.validator.HttpValidatorImpl;
import mysso.client.core.validator.Validator;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by pengyu on 2017/9/8.
 */
public class InterfaceProviderContextFactoryTest {
    @Test
    public void testCreate() {
        InterfaceProviderContext context = InterfaceProviderContextFactory.createInterfaceProviderContext(
                ConfigUtil.DEFAULT_BEANS_CONFIG_FILE);
        assertNotNull("context is null", context);
        Validator validator = context.getBean(Validator.class);
        assertNotNull("validator is null", validator);
        assertTrue("validator should be an instance of HttpValidatorImpl", validator instanceof HttpValidatorImpl);
        SessionRegistry sessionRegistry = context.getBean(SessionRegistry.class);
        assertNotNull("sessionRegistry is null", sessionRegistry);
        assertTrue("sessionRegistry should be an instance of InMemorySessionRegistry", sessionRegistry instanceof InMemorySessionRegistry);
    }
}
