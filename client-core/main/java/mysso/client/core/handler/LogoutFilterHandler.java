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
import java.io.IOException;

/**
 * Created by pengyu on 2017/9/4.
 */
public class LogoutFilterHandler implements FilterHandler {
    private Logger log = LoggerFactory.getLogger(getClass());
    private Configuration cfg = Configuration.getInstance();
    private SessionRegistry sessionRegistry;
    @Override
    public boolean handle(HttpServletRequest request, HttpServletResponse response) {
        // 判断是前端登出还是后端登出
        String servletPath = request.getServletPath();
        if (servletPath.startsWith(cfg.backChannelLogoutUri)) {
            handleBackChannelLogout(request, response);
            return false;
        } else if (servletPath.startsWith(cfg.frontChannelLogoutUri)) {
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
            logoutResultDto.setCode(Constants.SLO_CODE_TOKEN_EMPTY);
            logoutResultDto.setMessage("token is null or empty");
            PageUtil.renderJson(response, JSON.toJSONString(logoutResultDto));
            return;
        }
        final HttpSession session = sessionRegistry.getSessionByTokenId(tk);
        final Object assertionObj = session.getAttribute(cfg.assertionName);
        if (session == null || assertionObj == null) {
            log.trace("there is no session or assertion for the logout token {}", tk);
            logoutResultDto.setCode(Constants.SLO_CODE_TOKEN_NONEXISTS);
            logoutResultDto.setMessage("already logout");
            PageUtil.renderJson(response, JSON.toJSONString(logoutResultDto));
            return;
        }
        final Assertion assertion = (Assertion) assertionObj;
        if (StringUtils.equalsIgnoreCase(assertion.getToken(), tk)) {
            log.trace("the given token for logout {} not equals the token in the assertion {}",
                    tk, assertion.getToken());
            logoutResultDto.setCode(Constants.SLO_CODE_ERROR);
            logoutResultDto.setMessage("error");
            PageUtil.renderJson(response, JSON.toJSONString(logoutResultDto));
            return;
        }
        sessionRegistry.removeSessionByTokenId(tk);
        session.setAttribute(cfg.assertionName, null);
        session.invalidate();
        logoutResultDto.setCode(Constants.SLO_CODE_SUCCESS);
        logoutResultDto.setMessage("successfully logout");
        PageUtil.renderJson(response, JSON.toJSONString(logoutResultDto));
    }

    private void handleFrontChannelLogout(HttpServletRequest request, HttpServletResponse response) {
        log.trace("handling logout via front channel");
        // 前端登出
        HttpSession session = request.getSession(false);
        final Object assertionObj = session.getAttribute(cfg.assertionName);
        if (session != null && assertionObj == null) {
            final Assertion assertion = (Assertion) assertionObj;
            sessionRegistry.removeSessionByTokenId(assertion.getToken());
            session.setAttribute(cfg.assertionName, null);
            session.invalidate();
            log.trace("logout token {} successfully");
        } else {
            log.trace("there is no session or assertion for logout token current user");
        }
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {


    }

    public SessionRegistry getSessionRegistry() {
        return sessionRegistry;
    }

    public void setSessionRegistry(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }
}
