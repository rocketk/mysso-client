package mysso.client.core.session;

import javax.servlet.http.HttpSession;

/**
 * Created by pengyu on 2017/8/28.
 */
public interface SessionRegistry {
    void putSessionByTokenId(String tokenId, HttpSession session);
    HttpSession getSessionByTokenId(String tokenId);

    HttpSession removeSessionByTokenId(String tokenId);
}
