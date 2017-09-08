package mysso.client.core.session;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pengyu on 2017/9/5.
 */
public class InMemorySessionRegistry implements SessionRegistry {
    private Map<String, HttpSession> sessionMapByTokenId = new HashMap<>();
    @Override
    public void putSessionByTokenId(String tokenId, HttpSession session) {
        sessionMapByTokenId.put(tokenId, session);
    }

    @Override
    public HttpSession getSessionByTokenId(String tokenId) {
        return sessionMapByTokenId.get(tokenId);
    }

    @Override
    public HttpSession removeSessionByTokenId(String tokenId) {
        return sessionMapByTokenId.remove(tokenId);
    }

}
