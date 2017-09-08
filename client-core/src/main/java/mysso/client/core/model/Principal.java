package mysso.client.core.model;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by pengyu on 2017/8/21.
 */
public class Principal implements Serializable {
    private String id;
    private Map<String, Object> attributes;

    public Principal() {
    }

    public Principal(String id, Map<String, Object> attributes) {
        this.id = id;
        this.attributes = attributes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}
