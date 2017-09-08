package mysso.client.core.handler;

import mysso.client.core.context.Configuration;
import mysso.client.core.context.InterfaceProviderContext;
import mysso.client.core.model.Assertion;
import mysso.client.core.model.Principal;
import mysso.client.core.util.PageUtil;
import mysso.client.core.validator.Validator;
import mysso.protocol1.Constants;
import mysso.protocol1.dto.AssertionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by pengyu on 2017/9/4.
 */
public class ValidateFilterHandler implements FilterHandler {
    private Logger log = LoggerFactory.getLogger(getClass());
    private Configuration cfg = Configuration.getInstance();
    private Validator validator = InterfaceProviderContext.getInstance().getBean(Validator.class);

    @Override
    public boolean handle(HttpServletRequest request, HttpServletResponse response) {
        // check token in session
        HttpSession session = request.getSession(false);
        // 检查 session 中的token, 从而判断是否登录
        if (session != null && session.getAttribute(cfg.assertionName) != null) {
            Assertion assertion = (Assertion) session.getAttribute(cfg.assertionName);
            if (System.currentTimeMillis() < assertion.getExpiredTime()) {
                // token 正常
                log.info("user has been authenticated, principalId: {}, url: {}",
                        assertion.getPrincipal().getId(), request.getServletPath());
                return true;
            } else {
                // token 正常但过时了, 发送校验请求
                log.info("token is valid but expired, principalId: {}, url: {}",
                        assertion.getPrincipal().getId(), request.getServletPath());
                AssertionDto assertionDto = validator.validateToken(assertion.getToken());
                return handleAssertionDto(request, response, assertionDto);
            }
        } else {
            // 没有 token , 表示没有登录, 检查 service ticket
            String st = request.getParameter(Constants.PARAM_SERVICE_TICKET);
            if (st != null && !st.isEmpty()) {
                // 发送校验请求
                AssertionDto assertionDto = validator.validateServiceTicket(st);
                return handleAssertionDto(request, response, assertionDto);
            } else {
                // redirect to mysso-server
                try {
                    response.sendRedirect(cfg.authenticationUrlWithSpid);
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                    e.printStackTrace();
                }
                return false;
            }
        }
    }

    private boolean handleAssertionDto(HttpServletRequest request, HttpServletResponse response, AssertionDto assertionDto) {
        if (assertionDto != null && assertionDto.getCode() == 200) {
            // 校验成功
            Principal principal = new Principal(assertionDto.getPrincipal().getId(),
                    assertionDto.getPrincipal().getAttributes());
            Assertion assertion = new Assertion(assertionDto.getToken(),
                    assertionDto.getExpiredTime(), principal);
            request.getSession().setAttribute(cfg.assertionName, assertion);
            return true;
        } else if(assertionDto != null) {
            // 校验失败
            PageUtil.renderHtml(response, PageUtil.warnPage(
                    "校验ticket失败", assertionDto.getMessage(), cfg.authenticationUrlWithSpid));
            return false;
        } else {
            // 校验出错
            PageUtil.renderHtml(response, PageUtil.warnPage(
                    "校验ticket出错", "未知错误, 请联系管理员", cfg.authenticationUrlWithSpid));
            return false;
        }
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }
}
