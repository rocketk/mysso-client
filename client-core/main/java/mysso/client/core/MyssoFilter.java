package mysso.client.core;

import mysso.client.core.context.ApplicationContext;
import mysso.client.core.context.Configuration;
import mysso.client.core.util.ConfigUtil;
import mysso.protocol1.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by pengyu on 17-8-20.
 */
public class MyssoFilter implements Filter {
    private final Logger log = LoggerFactory.getLogger(getClass());

    public ConfigUtil configUtil;
    public ConfigUtil configUtil4Beans;

    private Configuration cfg;

    private ApplicationContext applicationContext;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String configFile = filterConfig.getInitParameter("configFile");
        log.info("loading configFile from {}", configFile);
        this.configUtil = new ConfigUtil(configFile);
        cfg = Configuration.getInstance();
        cfg.assertionName = configUtil.getProperty("assertionName", "_mysso_assertion");
        cfg.authenticationUrl = configUtil.getProperty("authenticationUrl");
        cfg.validationUrlPrefix = configUtil.getProperty("validationUrlPrefix");
        cfg.validationUrlPrefix = removeSlash(cfg.validationUrlPrefix);
        cfg.spid = configUtil.getProperty("spid");
        cfg.spkey = configUtil.getProperty("spkey");
        cfg.backChannelLogoutUri = configUtil.getProperty("backChannelLogoutUri");
        cfg.frontChannelLogoutUri = configUtil.getProperty("frontChannelLogoutUri");
        cfg.serverLogoutUrl = configUtil.getProperty("serverLogoutUrl");
        cfg.authenticationUrlWithSpid = cfg.authenticationUrl + "?" + Constants.PARAM_SPID + "=" + cfg.spid;

        this.applicationContext = ApplicationContext.getInstance();
        for (String interfaceName : configUtil4Beans.stringPropertyNames()) {
            // todo
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // check logout request


    }



    @Override
    public void destroy() {

    }

    private String removeSlash(String uri) {
        if (uri != null) {
            while (uri.endsWith("/")) {
                uri = uri.substring(0, uri.length() - 1);
            }
        }
        return uri;
    }
}
