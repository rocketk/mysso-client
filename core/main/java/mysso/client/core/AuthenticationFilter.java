package mysso.client.core;

import mysso.client.core.model.Token;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
    private String PARAM_ST = "st";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.authenticationUrl = filterConfig.getInitParameter("authenticationUrl");
        this.validationUrl = filterConfig.getInitParameter("validationUrl");
        this.spid = filterConfig.getInitParameter("spid");
        this.spkey = filterConfig.getInitParameter("spkey");
        String customTokenName = filterConfig.getInitParameter("tokenName");
        if (customTokenName != null && !customTokenName.equals("")) {
            this.tokenName = customTokenName;
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // check token in session
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(tokenName) != null) {
            Token token = (Token) session.getAttribute(tokenName);
            if (System.currentTimeMillis() < token.getExpiredTime()) {
                // valid token
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                // todo valid but expired, send a token-validation request to mysso-server
            }
        } else {
            // no token, check st(service ticket)
            String st = request.getParameter(PARAM_ST);
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

}
