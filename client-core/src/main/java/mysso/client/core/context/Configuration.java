package mysso.client.core.context;

import mysso.protocol1.Constants;

/**
 * Created by pengyu on 2017/9/4.
 */
public class Configuration {
//    private static Configuration instance;
    private String assertionName;
    private String authenticationUrl;
    private String validationUrlPrefix;
    private String spid;
    private String secret;
//    private boolean useHttps;
    private String backChannelLogoutUri;
    private String frontChannelLogoutUri;
    private String serverLogoutUrl;
    private String authenticationUrlWithSpid;

//    private Configuration() {
//
//    }
//
//    public static Configuration getInstance() {
//        if (instance == null) {
//            instance = new Configuration();
//        }
//        return instance;
//    }

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
        setAuthenticationUrlWithSpid(getAuthenticationUrl() + "?" + Constants.PARAM_SPID + "=" + getSpid());
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

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

//    public boolean isUseHttps() {
//        return useHttps;
//    }
//
//    public void setUseHttps(boolean useHttps) {
//        this.useHttps = useHttps;
//    }

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
