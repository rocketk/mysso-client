package mysso.client.core.registry;

import javax.servlet.http.HttpSession;

/**
 * Created by pengyu on 2017/8/28.
 */
public interface SessionRegistry {
    void putSession(String id, HttpSession session);
    HttpSession getSession(String id);

}
