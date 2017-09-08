package mysso.client.core;

import mysso.client.core.context.Configuration;
import mysso.client.core.context.InterfaceProviderContext;
import mysso.client.core.context.InterfaceProviderContextFactory;
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

    public ConfigUtil configUtil;
    public ConfigUtil configUtil4Beans;

    private Configuration cfg;

    private InterfaceProviderContext interfaceProviderContext;

    private List<FilterHandler> filterHandlers;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.loadConfiguration(filterConfig);
        this.loadInterfaceProviderContext(filterConfig);
        this.loadFilterHandlers();
    }

    private void loadConfiguration(FilterConfig filterConfig) {
        String configFile = filterConfig.getInitParameter("configFile");
        log.info("loading configFile from {}", configFile);
        this.configUtil = new ConfigUtil(StringUtils.isEmpty(configFile) ? ConfigUtil.DEFAULT_CONFIG_FILE : configFile);
        cfg = Configuration.getInstance();
        cfg.setAssertionName(configUtil.getProperty("assertionName", "_mysso_assertion"));
        cfg.setAuthenticationUrl(configUtil.getProperty("authenticationUrl"));
        cfg.setValidationUrlPrefix(removeSlash(configUtil.getProperty("validationUrlPrefix")));
        cfg.setSpid(configUtil.getProperty("spid"));
        cfg.setSpkey(configUtil.getProperty("spkey"));
        cfg.setBackChannelLogoutUri(configUtil.getProperty("backChannelLogoutUri"));
        cfg.setFrontChannelLogoutUri(configUtil.getProperty("frontChannelLogoutUri"));
        cfg.setServerLogoutUrl(configUtil.getProperty("serverLogoutUrl"));
        cfg.setAuthenticationUrlWithSpid(cfg.getAuthenticationUrl() + "?" + Constants.PARAM_SPID + "=" + cfg.getSpid());
    }

    private void loadInterfaceProviderContext(FilterConfig filterConfig) {
        String configFile4Beans = filterConfig.getInitParameter("configFile4Beans");
        interfaceProviderContext = InterfaceProviderContextFactory.createInterfaceProviderContext(
                StringUtils.isEmpty(configFile4Beans) ? ConfigUtil.DEFAULT_BEANS_CONFIG_FILE : configFile4Beans);
    }

    private void loadFilterHandlers() {
        this.filterHandlers = new ArrayList();
        LogoutFilterHandler logoutFilterHandler = new LogoutFilterHandler();
        logoutFilterHandler.setSessionRegistry(interfaceProviderContext.getBean(SessionRegistry.class));
        this.filterHandlers.add(logoutFilterHandler);
        ValidateFilterHandler validateFilterHandler = new ValidateFilterHandler();
        validateFilterHandler.setValidator(interfaceProviderContext.getBean(Validator.class));
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
