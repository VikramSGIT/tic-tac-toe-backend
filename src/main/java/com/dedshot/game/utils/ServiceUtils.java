package com.dedshot.game.utils;

import java.util.HashMap;
import java.util.Map;
import org.springframework.util.MultiValueMap;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class ServiceUtils {
    protected final MultiValueMap<String, String> headers;
    protected final ObjectMapper om;

    static {
        Map<String, String> map = new HashMap<>();
        map.put("Content-Type", "application/json");
        headers = MultiValueMap.fromSingleValue(map);

        om = new ObjectMapper();
    }

    public MultiValueMap<String, String> getHeaders() { return headers; }

    public String toJSONString(Object obj) {
        try {
            return om.writeValueAsString(obj); 
        } catch (JsonProcessingException e) {
            log.error("Error while converting object to JSON.");
            e.printStackTrace();
        }
        return null;
    }

    public <T> T toObject(String json, Class<T> type) {
        try {
            return om.readValue(json, type);
        } catch(JsonProcessingException e) {
            log.error("Error occured while converting json to {}", type.toString());
            e.printStackTrace();
        }
        return null;
    }
}
