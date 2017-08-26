package mysso.client.core.validator;

import mysso.client.core.model.Assertion;

/**
 * Created by pengyu on 2017/8/22.
 */
public interface Validator {
    Assertion validateServiceTicket(String st, String spid, String spkey);
    Assertion validateToken(String tk, String spid, String spkey);
}
