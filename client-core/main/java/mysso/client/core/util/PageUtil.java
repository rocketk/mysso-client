package mysso.client.core.util;

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
}
