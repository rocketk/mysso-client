package mysso.client.core.util;

/**
 * Created by pengyu on 2017/9/4.
 */
public class Configuration {
    private static Configuration instance;
    public String assertionName;
    public String authenticationUrl;
    public String validationUrlPrefix;
    public String spid;
    public String spkey;
    public boolean useHttps;
    public String localLogoutUri;
    public String serverLogoutUrl;

    private Configuration() {

    }

    public static Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }
}
