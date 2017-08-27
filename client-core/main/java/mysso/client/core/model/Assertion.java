package mysso.client.core.model;

import java.io.Serializable;

/**
 * Created by pengyu on 2017/8/21.
 */
public class Assertion implements Serializable {
    private String token;
    private long expiredTime;
    private Principal principal;

    public Assertion() {
    }

    public Assertion(String token, long expiredTime, Principal principal) {
        this.token = token;
        this.expiredTime = expiredTime;
        this.principal = principal;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(long expiredTime) {
        this.expiredTime = expiredTime;
    }

    public Principal getPrincipal() {
        return principal;
    }

    public void setPrincipal(Principal principal) {
        this.principal = principal;
    }
}
