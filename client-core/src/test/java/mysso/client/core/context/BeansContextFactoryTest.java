package mysso.client.core.context;

import mysso.client.core.security.GoogleAuthPasscodeGenerator;
import mysso.client.core.security.SecretPasscodeGenerator;
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
public class BeansContextFactoryTest {
    @Test
    public void testCreate() {
        BeansContext context = new BeansContextFactory(ConfigUtil.DEFAULT_BEANS_CONFIG_FILE).createInterfaceProviderContext();
        assertNotNull("context is null", context);
        // Configuratrion

        // Validator
        Validator validator = context.getBean(Validator.class);
        assertNotNull("validator is null", validator);
        assertTrue("validator should be an instance of HttpValidatorImpl", validator instanceof HttpValidatorImpl);
        // SessionRegistry
        SessionRegistry sessionRegistry = context.getBean(SessionRegistry.class);
        assertNotNull("sessionRegistry is null", sessionRegistry);
        assertTrue("sessionRegistry should be an instance of InMemorySessionRegistry", sessionRegistry instanceof InMemorySessionRegistry);
        // SecretPasscodeGenerator
        SecretPasscodeGenerator secretPasscodeGenerator = context.getBean(SecretPasscodeGenerator.class);
        assertNotNull("secretPasscodeGenerator is null", secretPasscodeGenerator);
        assertTrue("secretPasscodeGenerator should be an instance of GoogleAuthPasscodeGenerator", secretPasscodeGenerator instanceof GoogleAuthPasscodeGenerator);

        // dependencies
        HttpValidatorImpl httpValidator = (HttpValidatorImpl) validator;
        assertNotNull("cfg of httpValidator is null", httpValidator.getCfg());
        assertNotNull("secretPasscodeGenerator of httpValidator is null", httpValidator.getSecretPasscodeGenerator());

    }
}
