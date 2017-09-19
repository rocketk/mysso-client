package mysso.client.core.security;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.apache.commons.lang3.Validate;

/**
 * 使用了google authentication来实现的SecretPasscodeGenerator
 * Created by pengyu on 2017/9/19.
 */
public class GoogleAuthPasscodeGenerator implements SecretPasscodeGenerator {
    final GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
    /**
     * 采用Google Authentication的算法来动态计算出一个校验码，6位数字码
     * @param secret 秘钥
     * @return
     */
    @Override
    public String generate(String secret) {
        Validate.notNull(secret, "secret is null");
        int totpPasscode = googleAuthenticator.getTotpPassword(secret);
        return totpPasscode + "";
    }
}
