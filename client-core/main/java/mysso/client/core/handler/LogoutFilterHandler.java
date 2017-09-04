package mysso.client.core.handler;

import mysso.client.core.util.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by pengyu on 2017/9/4.
 */
public class LogoutFilterHandler implements FilterHandler {
    private Configuration configuration;

    public LogoutFilterHandler() {
    }

    public LogoutFilterHandler(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public boolean handle(HttpServletRequest request, HttpServletResponse response) {
        return false;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
