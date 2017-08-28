package mysso.client.core.validator;

import mysso.protocol1.dto.AssertionDto;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengyu on 2017/8/23.
 */
public class HttpsValidatorImpl implements Validator {
    private String spid;
    private String spkey;
    private String validationUrl;

    public HttpsValidatorImpl() {
    }

    public HttpsValidatorImpl(String spid, String spkey, String validationUrl) {
        this.spid = spid;
        this.spkey = spkey;
        this.validationUrl = validationUrl;
    }

    @Override
    public AssertionDto validateServiceTicket(String st) {
        // todo
        return null;
    }

    @Override
    public AssertionDto validateToken(String tk) {
        // todo
        return null;
    }

    public String getSpid() {
        return spid;
    }

    public void setSpid(String spid) {
        this.spid = spid;
    }

    public String getSpkey() {
        return spkey;
    }

    public void setSpkey(String spkey) {
        this.spkey = spkey;
    }

    public String getValidationUrl() {
        return validationUrl;
    }

    public void setValidationUrl(String validationUrl) {
        this.validationUrl = validationUrl;
    }
}