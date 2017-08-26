package mysso.client.core.validator;

import mysso.protocol1.dto.AssertionDto;

/**
 * Created by pengyu on 2017/8/22.
 */
public interface Validator {
    AssertionDto validateServiceTicket(String st);
    AssertionDto validateToken(String tk);
}
