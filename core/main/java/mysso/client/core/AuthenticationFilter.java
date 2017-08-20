package mysso.client.core;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by pengyu on 17-8-20.
 */
public class AuthenticationFilter implements Filter{
    private String authenticationUrl;
    private String validationUrl;
    private String spid;
    private String spkey;
    private String tokenName = "_mysso_token";
    private String principalName = "_mysso_principal";
    public void init(FilterConfig filterConfig) throws ServletException {
        this.authenticationUrl = filterConfig.getInitParameter("authenticationUrl");
        this.validationUrl = filterConfig.getInitParameter("validationUrl");
        this.spid = filterConfig.getInitParameter("spid");
        this.spkey = filterConfig.getInitParameter("spkey");
        String customTokenName = filterConfig.getInitParameter("tokenName");
        if (customTokenName != null) {
            this.tokenName = customTokenName;
        }
        String customPrincipalName = filterConfig.getInitParameter("principalName");
        if (customPrincipalName != null) {
            this.principalName = customPrincipalName;
        }
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // check token in session
        // 
        // check principal in session
        //
        // check
    }

    public void destroy() {

    }
}
