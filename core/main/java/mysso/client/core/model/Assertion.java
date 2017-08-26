package mysso.client.core.model;

import mysso.client.core.model.Principal;

/**
 * Created by pengyu on 2017/8/22.
 */
public class Assertion {
    /**
     * 表示校验结果的数值，取值范围参见 mysso.util.Constants
     */
    private int code;
    private String message;
    /**
     * 需要派发新的token时（例如st校验通过后或者tk超时需要更换），此字段不为null。否则为null。
     * 此字段实际上对应的是Token对象的id字段
     */
    private String token; // tokenId
    private long expiredTime;
    private Principal principal;
}
