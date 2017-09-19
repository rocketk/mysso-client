package mysso.client.core.security;

import org.apache.commons.lang3.Validate;

/**
 * SecretPasscodeGenerator的简单实现，生产环境中不应当使用此类，应使用GoogleAuthPasscodeGenerator替代此类
 * Created by pengyu on 2017/9/19.
 */
public class PlainTextSecretPasscodeGenerator implements SecretPasscodeGenerator {
    /**
     * 使用secret直接作为返回值
     * @param secret
     * @return
     */
    @Override
    public String generate(String secret) {
        Validate.notNull(secret, "secret is null");
        return secret;
    }
}
