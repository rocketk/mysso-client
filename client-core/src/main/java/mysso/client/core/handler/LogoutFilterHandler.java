package mysso.client.core.handler;

import com.alibaba.fastjson.JSON;
import mysso.client.core.context.Configuration;
import mysso.client.core.model.Assertion;
import mysso.client.core.session.SessionRegistry;
import mysso.client.core.util.PageUtil;
import mysso.protocol1.Constants;
import mysso.protocol1.dto.LogoutResultDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by pengyu on 2017/9/4.
 */
public class LogoutFilterHandler implements FilterHandler {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private Configuration cfg;
    private SessionRegistry sessionRegistry;

    public LogoutFilterHandler() {
    }

    public LogoutFilterHandler(Configuration cfg, SessionRegistry sessionRegistry) {
        this.cfg = cfg;
        this.sessionRegistry = sessionRegistry;
    }

    @Override
    public boolean handle(HttpServletRequest request, HttpServletResponse response) {
        // 判断是前端登出还是后端登出
        String servletPath = request.getServletPath();
        if (servletPath.startsWith(cfg.getBackChannelLogoutUri())) {
            handleBackChannelLogout(request, response);
            return false;
        } else if (servletPath.startsWith(cfg.getFrontChannelLogoutUri())) {
            handleFrontChannelLogout(request, response);
            return false;
        }
        // 不是登出操作，过滤器继续向下执行
        return true;
    }

    /**
     * todo
     * @param request
     * @param response
     */
    private void handleBackChannelLogout(HttpServletRequest request, HttpServletResponse response) {
        LogoutResultDto logoutResultDto = new LogoutResultDto();
        String tk = request.getParameter(Constants.SLO_PARAM_TOKEN);
        log.trace("handling logout via back channel, token: {}", tk);
        if (StringUtils.isEmpty(tk)) {
            log.trace("token {} is empty", tk);
            logoutResultDto.setCode(Constants.SLO_CODE_TOKEN_EMPTY);
            logoutResultDto.setMessage("token is null or empty");
            PageUtil.renderJson(response, JSON.toJSONString(logoutResultDto));
            return;
        }
        final HttpSession session = sessionRegistry.getSessionByTokenId(tk);
        if (session == null || session.getAttribute(cfg.getAssertionName()) == null) {
            log.trace("there is no session or assertion for the logout token {}", tk);
            logoutResultDto.setCode(Constants.SLO_CODE_TOKEN_NONEXISTS);
            logoutResultDto.setMessage("already logout");
            PageUtil.renderJson(response, JSON.toJSONString(logoutResultDto));
            return;
        }
        final Object assertionObj = session.getAttribute(cfg.getAssertionName());
        final Assertion assertion = (Assertion) assertionObj;
        if (!StringUtils.equalsIgnoreCase(assertion.getToken(), tk)) {
            log.error("the given token for logout {} not equals the token in the assertion {}",
                    tk, assertion.getToken());
            logoutResultDto.setCode(Constants.SLO_CODE_ERROR);
            logoutResultDto.setMessage("invalid token");
            PageUtil.renderJson(response, JSON.toJSONString(logoutResultDto));
            return;
        }
        sessionRegistry.removeSessionByTokenId(tk);
        session.setAttribute(cfg.getAssertionName(), null);
        session.invalidate();
        log.trace("handled logout via back channel successfully, tk: {}", tk);
        logoutResultDto.setCode(Constants.SLO_CODE_SUCCESS);
        logoutResultDto.setMessage("successfully logout");
        PageUtil.renderJson(response, JSON.toJSONString(logoutResultDto));
    }

    private void handleFrontChannelLogout(HttpServletRequest request, HttpServletResponse response) {
        log.trace("handling logout via front channel");
        // 前端登出
        HttpSession session = request.getSession(false);
        if (session != null ) {
            final Object assertionObj = session.getAttribute(cfg.getAssertionName());
            if (assertionObj != null) {
                final Assertion assertion = (Assertion) assertionObj;
                sessionRegistry.removeSessionByTokenId(assertion.getToken());
                session.setAttribute(cfg.getAssertionName(), null);
                session.invalidate();
                log.trace("logout token {} via frontChannel successfully");
                PageUtil.renderHtml(response, PageUtil.warnPage("前端登出成功",
                        "前端登出成功，但是仍需前往服务端登出",
                        cfg.getServerLogoutUrl(),
                        "服务端登出", true, 3));
                return;
            }
        }
        log.trace("there is no session or assertion for logout token current user");
        PageUtil.renderHtml(response, PageUtil.warnPage("前端登出成功",
                "前端登出成功，但是仍需前往服务端登出",
                cfg.getServerLogoutUrl(),
                "服务端登出", true, 3));
    }

    public void setCfg(Configuration cfg) {
        this.cfg = cfg;
    }

    public void setSessionRegistry(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }
}
