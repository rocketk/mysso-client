package mysso.client.core.security;

/**
 * Created by pengyu on 2017/9/19.
 */
public interface SecretPasscodeGenerator {
    String generate(String secret);
}
