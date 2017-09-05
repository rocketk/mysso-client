package mysso.client.core.handler;

import com.alibaba.fastjson.JSON;
import mysso.client.core.model.Assertion;
import mysso.client.core.util.Configuration;
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

    private void handleBackChannelLogout(HttpServletRequest request, HttpServletResponse response) {
        LogoutResultDto logoutResultDto = new LogoutResultDto();
        // todo 判断前端登出还是后端登出，如果是后端登出，要从SessionRegistry中取得session
        String tk = request.getParameter(Constants.SLO_PARAM_TOKEN);
        if (StringUtils.isEmpty(tk)) {
            logoutResultDto.setCode(Constants.SLO_CODE_TOKEN_EMPTY);
            logoutResultDto.setMessage("token is null or empty");
            try {
                PageUtil.renderJson(response, JSON.toJSONString(logoutResultDto));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        String principalId = null;

    }
    private void handleFrontChannelLogout(HttpServletRequest request, HttpServletResponse response) {
        // 前端登出
//        try{
//            HttpSession session = request.getSession(false);
//            String principalId = null;
//            if (session != null) {
//                Object assertionNameObj = session.getAttribute(cfg.assertionName);
//                if (assertionNameObj != null) {
//                    Assertion assertion = (Assertion) assertionNameObj;
//                    if (assertion != null && assertion.getPrincipal() != null) {
//                        principalId = assertion.getPrincipal().getId();
//                    }
//                }
//                session.invalidate();
//            }
//            if (principalId != null) {
//                logoutResultDto.setCode(Constants.SLO_CODE_SUCCESS);
//                logoutResultDto.setMessage("handled logout request successfully");
//                log.info("handled logout request, principal.id: {}", principalId);
//            } else {
//                logoutResultDto.setCode(Constants.SLO_CODE_TOKEN_NONEXISTS);
//                logoutResultDto.setMessage("handled logout request successfully");
//                log.info("handled logout request, there is no principal, the token {} does not exists", tk);
//            }
//            PageUtil.renderJson(response, JSON.toJSONString(logoutResultDto));
//        } catch (Exception e) {
//            logoutResultDto.setCode(Constants.SLO_CODE_ERROR);
//            logoutResultDto.setMessage("an exception occurred when handling logout request");
//            PageUtil.renderJson(response, JSON.toJSONString(logoutResultDto));
//        }
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {


    }

}
