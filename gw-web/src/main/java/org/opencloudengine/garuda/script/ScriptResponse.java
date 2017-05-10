package org.opencloudengine.garuda.script;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.opencloudengine.garuda.common.exception.ServiceException;
import org.opencloudengine.garuda.model.User;
import org.opencloudengine.garuda.util.JsonUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by uengine on 2016. 6. 28..
 */
public class ScriptResponse {

    public static String UNDEFINED = "undefined";
    public static String ARRAY = "array";
    public static String OBJECT = "object";
    public static String NUMBER = "number";
    public static String STRING = "string";


    private String log;
    private Object value;
    private String type;
    private String error;
    private int errorLine;
    private String errorSource;

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getErrorLine() {
        return errorLine;
    }

    public void setErrorLine(int errorLine) {
        this.errorLine = errorLine;
    }

    public String getErrorSource() {
        return errorSource;
    }

    public void setErrorSource(String errorSource) {
        this.errorSource = errorSource;
    }

    public <T> T castValue(Class<T> tClass) {
        if (type.equals(OBJECT)) {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.convertValue(value, tClass);
        }
        if (type.equals(ARRAY)) {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.convertValue(value, tClass);
        }
        return tClass.cast(value);
    }

    public <T> List<T> castListValue(Class<T> tClass) {
        List<T> list = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map> valueLit = objectMapper.convertValue(value, List.class);
        for (Map map : valueLit) {
            T convertValue = objectMapper.convertValue(map, tClass);
            list.add(convertValue);
        }
        return list;
    }
}
