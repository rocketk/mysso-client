package mysso.client.core.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by pengyu on 2017/9/4.
 */
public interface FilterHandler {
    public boolean handle(HttpServletRequest request, HttpServletResponse response);
}
