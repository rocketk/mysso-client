package mysso.client.core.util;

import mysso.protocol1.dto.AssertionDto;
import mysso.protocol1.dto.PrincipalDto;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by pengyu on 2017/8/27.
 */
public class FastJsonUtilTest {
    @Test
    public void testJsonString2AssertionDto() {
        String content = "{\n" +
                "    'code': 200,\n" +
                "    'expiredTime': 1503825358251,\n" +
                "    'message': 'valid st',\n" +
                "    'principal': {\n" +
                "        'attributes': {\n" +
                "            'department': 'technology',\n" +
                "            'level': 't4'\n" +
                "        },\n" +
                "        'id': 'jack'\n" +
                "    },\n" +
                "    'token': 'tk-641d57fee8c0432498bdbf11d5c92d8e'\n" +
                "}";
        try {
            AssertionDto assertionDto = FastJsonUtil.parseAssertionDto(content);
            assertEquals(200, assertionDto.getCode());
            assertEquals(1503825358251L, assertionDto.getExpiredTime());
            assertEquals("tk-641d57fee8c0432498bdbf11d5c92d8e", assertionDto.getToken());
            assertEquals("valid st", assertionDto.getMessage());
            PrincipalDto principalDto = assertionDto.getPrincipal();
            assertNotNull(principalDto);
            assertEquals("jack", principalDto.getId());
            assertNotNull(principalDto);
            assertTrue(principalDto.getAttributes() instanceof Map);
            assertEquals(2, principalDto.getAttributes().size());
            assertEquals("technology", principalDto.getAttributes().get("department"));
            assertEquals("t4", principalDto.getAttributes().get("level"));
        } catch (Exception e) {
            fail(e.getMessage());
            e.printStackTrace();
        }
    }
}
