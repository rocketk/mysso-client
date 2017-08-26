package mysso.client.core.validator;

import mysso.client.core.model.Assertion;

/**
 * Created by pengyu on 2017/8/23.
 */
public class DefaultValidatorImpl implements Validator {
    @Override
    public Assertion validateServiceTicket(String st, String spid, String spkey) {
        return null;
    }

    @Override
    public Assertion validateToken(String tk, String spid, String spkey) {
        return null;
    }
}
