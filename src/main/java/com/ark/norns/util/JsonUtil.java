package com.ark.norns.util;

import com.ark.norns.application.Logging;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Iterator;

@Component
public class JsonUtil {
    protected static final ObjectMapper mapper = new ObjectMapper();

    public static String entityToJson(Iterator<Object> iterableList) {
        try {
            String json = "";
            while (iterableList.hasNext()) {
                json.concat(mapper.writeValueAsString(iterableList.next()));
                if (iterableList.hasNext()) {
                    json.concat(",");
                }
            }
            return json;
        } catch (JsonGenerationException e) {
            Logging.error.error(e.getClass().toString(), e.toString());
        } catch (JsonMappingException e) {
            Logging.error.error(e.getClass().toString(), e.toString());
        } catch (IOException e) {
            Logging.error.error(e.getClass().toString(), e.toString());
        } catch (Exception e) {
            Logging.error.error(e.getClass().toString(), e.toString());
        }
        return "{}";
    }

    public static String entityToJson(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonGenerationException e) {
            Logging.error.error(e.getClass().toString(), e.toString());
        } catch (JsonMappingException e) {
            Logging.error.error(e.getClass().toString(), e.toString());
        } catch (IOException e) {
            Logging.error.error(e.getClass().toString(), e.toString());
        } catch (Exception e) {
            Logging.error.error(e.getClass().toString(), e.toString());
        }
        return "{}";
    }
}
