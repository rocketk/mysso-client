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

    public String getAssertionName() {
        return assertionName;
    }

    public void setAssertionName(String assertionName) {
        this.assertionName = assertionName;
    }

    public String getAuthenticationUrl() {
        return authenticationUrl;
    }

    public void setAuthenticationUrl(String authenticationUrl) {
        this.authenticationUrl = authenticationUrl;
    }

    public String getValidationUrlPrefix() {
        return validationUrlPrefix;
    }

    public void setValidationUrlPrefix(String validationUrlPrefix) {
        this.validationUrlPrefix = validationUrlPrefix;
    }

    public String getSpid() {
        return spid;
    }

    public void setSpid(String spid) {
        this.spid = spid;
    }

    public String getSpkey() {
        return spkey;
    }

    public void setSpkey(String spkey) {
        this.spkey = spkey;
    }

    public boolean isUseHttps() {
        return useHttps;
    }

    public void setUseHttps(boolean useHttps) {
        this.useHttps = useHttps;
    }

    public String getBackChannelLogoutUri() {
        return backChannelLogoutUri;
    }

    public void setBackChannelLogoutUri(String backChannelLogoutUri) {
        this.backChannelLogoutUri = backChannelLogoutUri;
    }

    public String getFrontChannelLogoutUri() {
        return frontChannelLogoutUri;
    }

    public void setFrontChannelLogoutUri(String frontChannelLogoutUri) {
        this.frontChannelLogoutUri = frontChannelLogoutUri;
    }

    public String getServerLogoutUrl() {
        return serverLogoutUrl;
    }

    public void setServerLogoutUrl(String serverLogoutUrl) {
        this.serverLogoutUrl = serverLogoutUrl;
    }

    public String getAuthenticationUrlWithSpid() {
        return authenticationUrlWithSpid;
    }

    public void setAuthenticationUrlWithSpid(String authenticationUrlWithSpid) {
        this.authenticationUrlWithSpid = authenticationUrlWithSpid;
    }
}
