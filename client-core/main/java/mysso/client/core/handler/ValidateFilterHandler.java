package mysso.client.core.handler;

import mysso.client.core.util.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by pengyu on 2017/9/4.
 */
public class ValidateFilterHandler implements FilterHandler {
    private Configuration configuration = Configuration.getInstance();

    @Override
    public boolean handle(HttpServletRequest request, HttpServletResponse response) {
        return false;
    }

}
