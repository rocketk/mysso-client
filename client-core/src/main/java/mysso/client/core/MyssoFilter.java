package mysso.client.core;

import mysso.client.core.context.BeansContextFactory;
import mysso.client.core.context.Configuration;
import mysso.client.core.context.BeansContext;
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
 * Created by pengyu on 17-8-20.
 */
public class MyssoFilter implements Filter {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private Configuration cfg;

    private BeansContext beansContext;

    private List<FilterHandler> filterHandlers;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        boolean needLoadConfigurationAndBeansContext = Boolean.valueOf(filterConfig.getInitParameter("needLoadConfigurationAndBeansContext"));
        if (needLoadConfigurationAndBeansContext) {
            this.loadBeansContext(filterConfig);
            this.loadConfiguration(filterConfig);
            this.loadFilterHandlers();
        }
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
        cfg.setSecret(configUtil.getProperty("passcode"));
        cfg.setBackChannelLogoutUri(configUtil.getProperty("backChannelLogoutUri"));
        cfg.setFrontChannelLogoutUri(configUtil.getProperty("frontChannelLogoutUri"));
        cfg.setServerLogoutUrl(configUtil.getProperty("serverLogoutUrl"));
        cfg.setAuthenticationUrlWithSpid(cfg.getAuthenticationUrl() + "?" + Constants.PARAM_SPID + "=" + cfg.getSpid());
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
        validateFilterHandler.setCfg(cfg);
        this.filterHandlers.add(validateFilterHandler);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        boolean continueToFilterChain = true;
        for (FilterHandler filterHandler : this.filterHandlers) {
            if (!filterHandler.handle(request, response)) {
                continueToFilterChain = false;
                break;
            }
        }
        if (continueToFilterChain) {
            filterChain.doFilter(servletRequest, servletResponse);
        }
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
