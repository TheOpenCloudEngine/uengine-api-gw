package org.uengine.kernel.bahama;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.jclouds.domain.JsonBall;

import java.io.IOException;

import static org.apache.commons.lang.StringUtils.strip;

public class JsonBallJacksonSerializer extends StdSerializer<JsonBall> {

    protected JsonBallJacksonSerializer(Class<JsonBall> t) {
        super(t);
    }

    @Override
    public void serialize(JsonBall jsonBall, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        String json = jsonBall.toString();
        if (json.startsWith("{")) {
            jgen.writeObject(JsonUtils.unmarshal(json));
        } else if (json.startsWith("[")) {
            jgen.writeObject(JsonUtils.unmarshalToList(json));
        } else {
            // Chef의 JSON Ball에서 나타나는 특이한 증상
            if (json.startsWith("\"") && json.endsWith("\"")) {
                jgen.writeString(strip(json, "\""));
            } else {
                jgen.writeString(json);
            }
        }
    }
}