package json.jackson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ma peiliang
 * Create Date: 2018/7/6 17:14
 * Description: ${DESCRIPTION}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VoBean {

    @JsonProperty("error_code")
    private String voName;

    private String voAge;

    public String getVoName() {
        return voName;
    }

    public void setVoName(String voName) {
        this.voName = voName;
    }

    public String getVoAge() {
        return voAge;
    }

    public void setVoAge(String voAge) {
        this.voAge = voAge;
    }
}
