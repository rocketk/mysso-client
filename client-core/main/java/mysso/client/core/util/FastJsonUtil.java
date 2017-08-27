package mysso.client.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import mysso.protocol1.dto.AssertionDto;

/**
 * Created by pengyu on 2017/8/27.
 */
public class FastJsonUtil {
    public static AssertionDto parseAssertionDto(String content) {
        AssertionDto assertionDto = JSON.parseObject(content, AssertionDto.class);
        // 由于parseObject方法无法解析JavaBean中的Map字段, 所以要通过JSON.parse的方式来获取attributes字段
        JSONObject jsonObject = (JSONObject) JSON.parse(content);
        if (assertionDto.getPrincipal() != null) {
            JSONObject principalObject = jsonObject.getJSONObject("principal");
            JSONObject attributes = principalObject.getJSONObject("attributes");
            assertionDto.getPrincipal().setAttributes(attributes);
        }
        return assertionDto;
    }
}
