package mysso.client.core.model;

import java.io.Serializable;

/**
 * Created by pengyu on 2017/8/21.
 */
public class Token implements Serializable{
    private String id;
    private long expiredTime;
    private Principal principal;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
