package mysso.client.core;

import com.alibaba.fastjson.JSON;
import mysso.client.core.model.Assertion;
import mysso.client.core.model.Principal;
import mysso.client.core.util.ConfigUtil;
import mysso.client.core.util.Configuration;
import mysso.client.core.util.PageUtil;
import mysso.client.core.validator.HttpValidatorImpl;
import mysso.client.core.validator.HttpsValidatorImpl;
import mysso.client.core.validator.Validator;
import mysso.protocol1.Constants;
import mysso.protocol1.dto.AssertionDto;
import mysso.protocol1.dto.LogoutResultDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by pengyu on 17-8-20.
 */
public class MyssoFilter implements Filter {
    private final Logger log = LoggerFactory.getLogger(getClass());

    public ConfigUtil configUtil;

    private Configuration cfg;

    private Validator validator;

    private String authenticationUrlWithSpid;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String configFile = filterConfig.getInitParameter("configFile");
        log.info("loading configFile from {}", configFile);
        this.configUtil = ConfigUtil.getInstance(configFile);
        cfg = Configuration.getInstance();
        cfg.assertionName = configUtil.getProperty("assertionName", "_mysso_assertion");
        cfg.authenticationUrl = configUtil.getProperty("authenticationUrl");
        cfg.validationUrlPrefix = configUtil.getProperty("validationUrlPrefix");
        cfg.validationUrlPrefix = removeSlash(cfg.validationUrlPrefix);
        cfg.spid = configUtil.getProperty("spid");
        cfg.spkey = configUtil.getProperty("spkey");
        cfg.localLogoutUri = configUtil.getProperty("localLogoutUri");
        cfg.serverLogoutUrl = configUtil.getProperty("serverLogoutUrl");
        String useHttps = configUtil.getProperty("useHttps");
        if (useHttps != null && ("true".equals(useHttps) || "1".equals(useHttps))) {
            cfg.useHttps = true;
            validator = new HttpsValidatorImpl(); // todo
        } else {
            cfg.useHttps = false;
            validator = new HttpValidatorImpl(cfg.spid, cfg.spkey, cfg.validationUrlPrefix);
        }
        this.authenticationUrlWithSpid = cfg.authenticationUrl + "?" + Constants.PARAM_SPID + "=" + cfg.spid;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // check logout request
        if (request.getServletPath().equals(cfg.localLogoutUri)) {
            handleLogout(request, response);
            return;
        }
        // check token in session
        HttpSession session = request.getSession(false);
        // 检查 session 中的token, 从而判断是否登录
        if (session != null && session.getAttribute(cfg.assertionName) != null) {
            Assertion assertion = (Assertion) session.getAttribute(cfg.assertionName);
            if (System.currentTimeMillis() < assertion.getExpiredTime()) {
                // token 正常
                log.info("user has been authenticated, principalId: {}, url: {}",
                        assertion.getPrincipal().getId(), request.getServletPath());
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                // token 正常但过时了, 发送校验请求
                log.info("token is valid but expired, principalId: {}, url: {}",
                        assertion.getPrincipal().getId(), request.getServletPath());
                AssertionDto assertionDto = validator.validateToken(assertion.getToken());
                handleAssertionDto(request, response, filterChain, assertionDto);
                return;
            }
        } else {
            // 没有 token , 表示没有登录, 检查 service ticket
            String st = request.getParameter(Constants.PARAM_SERVICE_TICKET);
            if (st != null && !st.isEmpty()) {
                // 发送校验请求
                AssertionDto assertionDto = validator.validateServiceTicket(st);
                handleAssertionDto(request, response, filterChain, assertionDto);
                return;
            } else {
                // redirect to mysso-server
                response.sendRedirect(authenticationUrlWithSpid);
                return;
            }
        }
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LogoutResultDto logoutResultDto = new LogoutResultDto();
        // todo 判断前端登出还是后端登出，如果是后端登出，要从SessionRegistry中取得session
        String tk = request.getParameter(Constants.SLO_PARAM_TOKEN);
        if (StringUtils.isEmpty(tk)) {
            logoutResultDto.setCode(Constants.SLO_CODE_TOKEN_EMPTY);
            logoutResultDto.setMessage("token is null or empty");
            renderJson(response, JSON.toJSONString(logoutResultDto));
            return;
        }
        // 前端登出
        try{
            HttpSession session = request.getSession(false);
            String principalId = null;
            if (session != null) {
                Object assertionNameObj = session.getAttribute(cfg.assertionName);
                if (assertionNameObj != null) {
                    Assertion assertion = (Assertion) assertionNameObj;
                    if (assertion != null && assertion.getPrincipal() != null) {
                        principalId = assertion.getPrincipal().getId();
                    }
                }
                session.invalidate();
            }
            if (principalId != null) {
                logoutResultDto.setCode(Constants.SLO_CODE_SUCCESS);
                logoutResultDto.setMessage("handled logout request successfully");
                log.info("handled logout request, principal.id: {}", principalId);
            } else {
                logoutResultDto.setCode(Constants.SLO_CODE_TOKEN_NONEXISTS);
                logoutResultDto.setMessage("handled logout request successfully");
                log.info("handled logout request, there is no principal, the token {} does not exists", tk);
            }
            renderJson(response, JSON.toJSONString(logoutResultDto));
        } catch (Exception e) {
            logoutResultDto.setCode(Constants.SLO_CODE_ERROR);
            logoutResultDto.setMessage("an exception occurred when handling logout request");
            renderJson(response, JSON.toJSONString(logoutResultDto));
        }
    }

    private void handleAssertionDto(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                    AssertionDto assertionDto) throws IOException, ServletException {
        if (assertionDto != null && assertionDto.getCode() == 200) {
            // 校验成功
            Principal principal = new Principal(assertionDto.getPrincipal().getId(),
                    assertionDto.getPrincipal().getAttributes());
            Assertion assertion = new Assertion(assertionDto.getToken(),
                    assertionDto.getExpiredTime(), principal);
            request.getSession().setAttribute(cfg.assertionName, assertion);
            chain.doFilter(request, response);
            return;
        } else if(assertionDto != null) {
            // 校验失败
            renderHtml(response, PageUtil.warnPage(
                    "校验ticket失败", assertionDto.getMessage(), authenticationUrlWithSpid));
            return;
        } else {
            // 校验出错
            renderHtml(response, PageUtil.warnPage("校验ticket出错", "未知错误, 请联系管理员", authenticationUrlWithSpid));
            return;
        }
    }

    private void render(HttpServletResponse response, String content, String contentType) throws IOException {
        response.setContentType(contentType);
        response.setHeader("Content-Type", contentType);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(content);
        response.flushBuffer();
    }

    private void renderJson(HttpServletResponse response, String content) throws IOException {
        render(response, content, "application/json;charset=UTF-8");
    }
    private void renderHtml(HttpServletResponse response, String content) throws IOException {
        render(response, content, "text/html;charset=UTF-8");
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