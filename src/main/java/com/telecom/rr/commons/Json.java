package com.telecom.rr.commons;

import java.io.IOException;
import java.io.StringWriter;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Json {

    private static final Logger       LOG    = LoggerFactory.getLogger(Json.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Bean对象转换json字符串
     * @param value
     * @return
     */
    public final static String objectToJson(Object value) {
        StringWriter writer = new StringWriter();
        JsonGenerator jsonGenerator = null;
        try {
            jsonGenerator = mapper.getJsonFactory().createJsonGenerator(writer);
            mapper.writeValue(jsonGenerator, value);
        } catch (IOException e) {
            LOG.error("对象转换成json失败", e);
        } finally {
            try {
                if (jsonGenerator != null) {
                    jsonGenerator.close();
                }
            } catch (IOException e) {
                LOG.error("", e);
            }
        }
        return writer.toString();
    }

    public static String objectToJson(Object pojo, boolean prettyPrint) {
        StringWriter sw = new StringWriter();
        JsonGenerator jg = null;
        try {
            jg = mapper.getJsonFactory().createJsonGenerator(sw);
            if (prettyPrint) {
                jg.useDefaultPrettyPrinter();
            }
            mapper.writeValue(jg, pojo);
        } catch (JsonGenerationException e) {
            LOG.error("对象转换成json失败", e);
        } catch (JsonMappingException e) {
            LOG.error("对象转换成json失败", e);
        } catch (IOException e) {
            LOG.error("对象转换成json失败", e);
        } finally {
            try {
                if (jg != null) {
                    jg.close();
                }
            } catch (IOException e) {
                LOG.error("", e);
            }
        }

        return sw.toString();
    }

    public final static ObjectMapper getMapperInstance() {
        return mapper;
    }

    /**
     * json字符串转换为对应Bean对象
     * @param json
     * @param clazz
     * @return
     */
    public static <V> V jsonToObject(String json, Class<V> clazz) {
        V value = null;
        try {
            if (clazz == String.class) {
                return clazz.cast(json);
            }
            value = mapper.readValue(json, clazz);
        } catch (Exception e) {
            LOG.error("json转换成对象失败", e);
        }
        return value;
    }

}
