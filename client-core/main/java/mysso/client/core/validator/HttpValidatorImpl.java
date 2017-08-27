package mysso.client.core.validator;

import mysso.protocol1.Constants;
import mysso.protocol1.dto.AssertionDto;
import org.apache.commons.io.IOUtils;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengyu on 2017/8/23.
 */
public class HttpValidatorImpl implements Validator {
    private Logger log = LoggerFactory.getLogger(getClass());
    private String spid;
    private String spkey;
    private String validationUrlPrefix;

    public HttpValidatorImpl() {
    }

    public HttpValidatorImpl(String spid, String spkey, String validationUrlPrefix) {
        this.spid = spid;
        this.spkey = spkey;
        this.validationUrlPrefix = validationUrlPrefix;
    }

    @Override
    public AssertionDto validateServiceTicket(String st) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(validationUrlPrefix + Constants.VALIDATE_ST_URI);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair(Constants.PARAM_SPID, spid));
            nvps.add(new BasicNameValuePair(Constants.PARAM_SPKEY, spkey));
            nvps.add(new BasicNameValuePair(Constants.PARAM_SERVICE_TICKET, st));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            log.trace("sending validate-st request to mysso-server, url: {}, st: {}, spid: {}",
                    validationUrlPrefix + Constants.VALIDATE_ST_URI, st, spid);
            response = httpclient.execute(httpPost);
            System.out.println(response.getStatusLine());
            log.trace("response validate-st response, status: {}", response.getStatusLine().getStatusCode());
            HttpEntity entity = response.getEntity();
            // do something useful with the response body
            // and ensure it is fully consumed
            InputStream contentStream = entity.getContent();
            String content = IOUtils.toString(contentStream, "UTF-8");
            System.out.println(content);
            EntityUtils.consume(entity);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }

        return null;
    }

    @Override
    public AssertionDto validateToken(String tk) {
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

    public String getValidationUrlPrefix() {
        return validationUrlPrefix;
    }

    public void setValidationUrlPrefix(String validationUrlPrefix) {
        this.validationUrlPrefix = validationUrlPrefix;
    }
}
