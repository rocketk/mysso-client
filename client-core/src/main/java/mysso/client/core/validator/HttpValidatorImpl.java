package mysso.client.core.validator;

import com.alibaba.fastjson.JSON;
import mysso.client.core.context.Configuration;
import mysso.client.core.security.SecretPasscodeGenerator;
import mysso.protocol1.Constants;
import mysso.protocol1.dto.AssertionDto;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengyu on 2017/8/23.
 */
public class HttpValidatorImpl implements Validator {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final CloseableHttpClient httpclient = HttpClients.createDefault();
    private SecretPasscodeGenerator secretPasscodeGenerator;
    private Configuration cfg;

    public HttpValidatorImpl() {
    }

    public HttpValidatorImpl(SecretPasscodeGenerator secretPasscodeGenerator, Configuration cfg) {
        this.secretPasscodeGenerator = secretPasscodeGenerator;
        this.cfg = cfg;
    }

    @Override
    public AssertionDto validateServiceTicket(String st) {
        return validateTicket(st, Constants.PARAM_SERVICE_TICKET, cfg.getValidationUrlPrefix() + Constants.VALIDATE_ST_URI);
    }

    @Override
    public AssertionDto validateToken(String tk) {
        return validateTicket(tk, Constants.PARAM_TOKEN, cfg.getValidationUrlPrefix() + Constants.VALIDATE_TK_URI);
    }

    private AssertionDto validateTicket(String ticket, String paramName, String validateUrl) {
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(validateUrl);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair(Constants.PARAM_SPID, cfg.getSpid()));
            nvps.add(new BasicNameValuePair(Constants.PARAM_SECRET_PASSCODE, secretPasscodeGenerator.generate(cfg.getSecret())));
            nvps.add(new BasicNameValuePair(paramName, ticket));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            log.trace("sending ticket validation request to mysso-server, url: {}, ticket: {}, spid: {}, validateUrl: {}",
                    cfg.getValidationUrlPrefix() + Constants.VALIDATE_ST_URI, ticket, cfg.getSpid(), validateUrl);
            response = httpclient.execute(httpPost);
            log.trace("received ticket validation response, status: {}", response.getStatusLine().getStatusCode());
            HttpEntity entity = response.getEntity();
            InputStream contentStream = entity.getContent();
            log.trace("reading input stream of response to a string");
            String content = IOUtils.toString(contentStream, "UTF-8");
            log.trace("response content: " + content);
            log.trace("parsing the string content to AssertionDto object");
            AssertionDto assertionDto = JSON.parseObject(content, AssertionDto.class);
            EntityUtils.consume(entity);
            return assertionDto;
        } catch (Exception e) {
            log.error("an exception occurred when sending validation request, caused by: " + e.getMessage(), e);
//            e.printStackTrace();
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

    public CloseableHttpClient getHttpclient() {
        return httpclient;
    }

    public SecretPasscodeGenerator getSecretPasscodeGenerator() {
        return secretPasscodeGenerator;
    }

    public void setSecretPasscodeGenerator(SecretPasscodeGenerator secretPasscodeGenerator) {
        this.secretPasscodeGenerator = secretPasscodeGenerator;
    }

    public Configuration getCfg() {
        return cfg;
    }

    public void setCfg(Configuration cfg) {
        this.cfg = cfg;
    }
}
