package mysso.client.core.util;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by pengyu on 2017/8/27.
 */
public class PageUtil {
    public static String warnPage(String title, String message, String url4Redirection) {
        return warnPage(title, message, url4Redirection, "前往认证中心", true, 3);
    }

    /**
     * @param title
     * @param message          告警消息
     * @param url4Redirection  跳转链接
     * @param desc4Redirection 跳转链接的文字秒数
     * @param autoRedirection  是否自动跳转
     * @param seconds          在此秒数之后会自动跳转页面
     * @return
     */
    public static String warnPage(String title, String message, String url4Redirection,
                                  String desc4Redirection, boolean autoRedirection, int seconds) {
        StringBuilder page = new StringBuilder();
        page.append("<!DOCTYPE html>").append("\n");
        page.append("<html>").append("\n");
        page.append("<head>").append("\n");
        page.append("<title>").append(title).append("</title>").append("\n");
        if (autoRedirection) {
            page.append("<script language=\"javascript\">").append("\n");
            page.append("var secs = ").append(seconds).append(";").append("\n");
            page.append("var url='").append(autoRedirection).append("';").append("\n");
            page.append("function load(){").append("\n");
            page.append("   for(var i=secs;i>=0;i--){").append("\n");
            page.append("       window.setTimeout('doUpdate(' + i + ')', (secs-i) * 1000);").append("\n");
            page.append("   }").append("\n");
            page.append("}").append("\n");
            page.append("function doUpdate(num){").append("\n");
            page.append("   document.getElementById('showSpan').innerHTML = '页面将在'+num+'秒后自动跳转, 如果没有自动跳转, 请手动点击' ;").append("\n");
            page.append("   if(num == 0) { window.location = URL; }").append("\n");
            page.append("}").append("\n");
            page.append("</script>").append("\n");
        }
        page.append("</head>").append("\n");
        if (autoRedirection) {
            page.append("<body onload=\"load('index.asp')\">").append("\n");
        } else {
            page.append("<body>").append("\n");
        }
        page.append("<p>").append(message).append("<span id='showSpan'></span>");
        page.append("&nbsp;<a href='").append(url4Redirection).append("'>").append(desc4Redirection).append("</a>");
        page.append("</p>").append("<br/>").append("\n");
        page.append("</body>").append("\n");
        page.append("</html>").append("\n");
        return page.toString();
    }

    public static void render(HttpServletResponse response, String content, String contentType) {
        response.setContentType(contentType);
        response.setHeader("Content-Type", contentType);
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().write(content);
            response.flushBuffer();
        } catch (IOException e) {
            new RuntimeException(e);
        }
    }

    public static void renderJson(HttpServletResponse response, String content) {
        render(response, content, "application/json;charset=UTF-8");
    }

    public static void renderHtml(HttpServletResponse response, String content) {
        render(response, content, "text/html;charset=UTF-8");
    }
}
