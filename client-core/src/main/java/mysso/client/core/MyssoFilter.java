package mysso.client.core;

import mysso.client.core.context.Configuration;
import mysso.client.core.handler.FilterHandler;
import mysso.protocol1.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 此类中的属性依赖需要通过spring来完成注入（也可以手动写代码来注入），
 * 如果你的程序不洗碗使用spring，可以使用MyssoAutoWiredFilter来替代此类
 * Created by pengyu
 */
public class MyssoFilter implements Filter {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected Configuration cfg;

    protected List<FilterHandler> filterHandlers;

    public MyssoFilter() {
    }

    public MyssoFilter(Configuration cfg, List<FilterHandler> filterHandlers) {
        this.cfg = cfg;
        this.filterHandlers = filterHandlers;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        filterConfig.getServletContext().setAttribute("cfg", cfg);
        cfg.setAuthenticationUrlWithSpid(cfg.getAuthenticationUrl() + "?" + Constants.PARAM_SPID + "=" + cfg.getSpid());
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

    public Configuration getCfg() {
        return cfg;
    }

    public void setCfg(Configuration cfg) {
        this.cfg = cfg;
    }

    public List<FilterHandler> getFilterHandlers() {
        return filterHandlers;
    }

    public void setFilterHandlers(List<FilterHandler> filterHandlers) {
        this.filterHandlers = filterHandlers;
    }
}
