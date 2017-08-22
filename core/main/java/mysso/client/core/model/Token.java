package mysso.client.core.model;

import java.io.Serializable;

/**
 * Created by pengyu on 2017/8/21.
 */
public class Token implements Serializable{
    private String id;
    private long expiredTime;
    private Principal principal;
}
