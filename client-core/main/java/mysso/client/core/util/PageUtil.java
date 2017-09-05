package mysso.client.core.util;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by pengyu on 2017/8/27.
 */
public class PageUtil {
    public static String warnPage(String title, String message, String url4Redirection) {
        StringBuilder page = new StringBuilder();
        page.append("<!DOCTYPE html>").append("\n");
        page.append("<html>").append("\n");
        page.append("<head>").append("\n");
        page.append("<title>").append(title).append("</title>").append("\n");
        page.append("</head>").append("\n");
        page.append("<body>").append("\n");
        page.append("<p>").append(message);
        page.append("&nbsp;<a href='").append(url4Redirection).append("'>前往认证中心</a>");
        page.append("</p>").append("<br/>").append("\n");
        page.append("</body>").append("\n");
        page.append("</html>").append("\n");
        return page.toString();
    }


    public static void render(HttpServletResponse response, String content, String contentType) throws IOException {
        response.setContentType(contentType);
        response.setHeader("Content-Type", contentType);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(content);
        response.flushBuffer();
    }

    public static void renderJson(HttpServletResponse response, String content) throws IOException {
        render(response, content, "application/json;charset=UTF-8");
    }
    public static void renderHtml(HttpServletResponse response, String content) throws IOException {
        render(response, content, "text/html;charset=UTF-8");
    }
}
