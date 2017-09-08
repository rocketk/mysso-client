package mysso.client.core.context;

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
    public String backChannelLogoutUri;
    public String frontChannelLogoutUri;
    public String serverLogoutUrl;
    public String authenticationUrlWithSpid;

    private Configuration() {

    }

    public static Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }
}
