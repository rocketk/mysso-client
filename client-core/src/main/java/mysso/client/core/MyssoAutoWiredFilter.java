package mysso.client.core;

import mysso.client.core.context.BeansContext;
import mysso.client.core.context.BeansContextFactory;
import mysso.client.core.context.Configuration;
import mysso.client.core.handler.FilterHandler;
import mysso.client.core.handler.LogoutFilterHandler;
import mysso.client.core.handler.ValidateFilterHandler;
import mysso.client.core.session.SessionRegistry;
import mysso.client.core.util.ConfigUtil;
import mysso.client.core.validator.Validator;
import mysso.protocol1.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 此类中的依赖以及配置文件均会自动完成注入。
 * 需要在FilterConfig中指定myssoclient.properties和myssoclient-beans.properties这两个配置文件的地址。
 * 默认地址参见ConfigUtil.DEFAULT_CONFIG_FILE以及ConfigUtil.DEFAULT_BEANS_CONFIG_FILE
 * Created by pengyu.
 */
public class MyssoAutoWiredFilter extends MyssoFilter {
    private BeansContext beansContext;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.loadBeansContext(filterConfig);
        this.loadConfiguration(filterConfig);
        this.loadFilterHandlers();
        filterConfig.getServletContext().setAttribute("cfg", cfg);
    }

    private void loadConfiguration(FilterConfig filterConfig) {
        String configFile = filterConfig.getInitParameter("configFile");
        log.info("loading configFile from {}", configFile);
        ConfigUtil configUtil = new ConfigUtil(StringUtils.isEmpty(configFile) ? ConfigUtil.DEFAULT_CONFIG_FILE : configFile);
        cfg = beansContext.getBean(Configuration.class);
        cfg.setAssertionName(configUtil.getProperty("assertionName", "_mysso_assertion"));
        cfg.setAuthenticationUrl(configUtil.getProperty("authenticationUrl"));
        cfg.setValidationUrlPrefix(removeSlash(configUtil.getProperty("validationUrlPrefix")));
        cfg.setSpid(configUtil.getProperty("spid"));
        cfg.setSecret(configUtil.getProperty("secret"));
        cfg.setBackChannelLogoutUri(configUtil.getProperty("backChannelLogoutUri"));
        cfg.setFrontChannelLogoutUri(configUtil.getProperty("frontChannelLogoutUri"));
        cfg.setServerLogoutUrl(configUtil.getProperty("serverLogoutUrl"));
    }

    private void loadBeansContext(FilterConfig filterConfig) {
        String configFile4Beans = filterConfig.getInitParameter("configFile4Beans");
        beansContext = new BeansContextFactory(StringUtils.isEmpty(configFile4Beans) ?
                ConfigUtil.DEFAULT_BEANS_CONFIG_FILE : configFile4Beans).createInterfaceProviderContext();
    }

    private void loadFilterHandlers() {
        this.filterHandlers = new ArrayList();
        LogoutFilterHandler logoutFilterHandler = new LogoutFilterHandler();
        logoutFilterHandler.setSessionRegistry(beansContext.getBean(SessionRegistry.class));
        logoutFilterHandler.setCfg(cfg);
        this.filterHandlers.add(logoutFilterHandler);
        ValidateFilterHandler validateFilterHandler = new ValidateFilterHandler();
        validateFilterHandler.setValidator(beansContext.getBean(Validator.class));
        validateFilterHandler.setSessionRegistry(beansContext.getBean(SessionRegistry.class));
        validateFilterHandler.setCfg(cfg);
        this.filterHandlers.add(validateFilterHandler);
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
