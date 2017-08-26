package mysso.client.core;

import mysso.client.core.model.Authentication;
import mysso.client.core.validator.HttpValidatorImpl;
import mysso.client.core.validator.HttpsValidatorImpl;
import mysso.client.core.validator.Validator;
import mysso.protocol1.Constants;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by pengyu on 17-8-20.
 */
public class AuthenticationFilter implements Filter {
    private String authenticationUrl;
    private String validationUrlPrefix;
    private String spid;
    private String spkey;
    private boolean useHttps;
    private String assertionName = "_mysso_assertion";
    private Validator validator;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.authenticationUrl = filterConfig.getInitParameter("authenticationUrl");
        this.validationUrlPrefix = filterConfig.getInitParameter("validationUrlPrefix");
        this.validationUrlPrefix = removeSlash(this.validationUrlPrefix);
        this.spid = filterConfig.getInitParameter("spid");
        this.spkey = filterConfig.getInitParameter("spkey");
        String customTokenName = filterConfig.getInitParameter("assertionName");
        if (customTokenName != null && !customTokenName.equals("")) {
            this.assertionName = customTokenName;
        }
        String useHttps = filterConfig.getInitParameter("useHttps");
        if (useHttps != null || "true".equals(useHttps) || "1".equals(useHttps)) {
            this.useHttps = true;
            validator = new HttpsValidatorImpl(); // todo
        } else {
            this.useHttps = false;
            validator = new HttpValidatorImpl(spid, spkey, validationUrlPrefix);
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // check token in session
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(assertionName) != null) {
            Authentication authentication = (Authentication) session.getAttribute(assertionName);
            if (System.currentTimeMillis() < authentication.getExpiredTime()) {
                // valid token
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                // todo valid but expired, send a authentication-validation request to mysso-server
            }
        } else {
            // no token, check st(service ticket)
            String st = request.getParameter(Constants.PARAM_SERVICE_TICKET);
            if (st != null && !st.isEmpty()) {
                // todo send a st-validation request to mysso-server
            } else {
                // todo redirect to mysso-server
            }
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
