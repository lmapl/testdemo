package json.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by ma peiliang
 * Create Date: 2018/7/6 17:14
 * Description: ${DESCRIPTION}
 */
public class JackJsonMain {

    public static void main(String[] args) throws IOException {

        VoBean voBean = new VoBean();
        voBean.setVoName("helloWorld");
        voBean.setVoAge("12138");

        System.out.println(voBean.toString());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(voBean);
        System.out.println(json);


        VoBean user = objectMapper.readValue(json, VoBean.class);
        System.out.println(user);

    }
}
