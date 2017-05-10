package org.opencloudengine.garuda.script;

import org.apache.velocity.app.VelocityEngine;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.tools.shell.Global;
import org.mozilla.javascript.tools.shell.Main;
import org.opencloudengine.garuda.common.exception.ServiceException;
import org.opencloudengine.garuda.util.*;
import org.springframework.context.ApplicationContext;
import org.springframework.ui.velocity.VelocityEngineUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by uengine on 2016. 6. 28..
 */
public class ScriptRequest {

    private Map<String, Object> args = new HashMap<>();

    private String script;

    private VelocityEngine velocityEngine;

    public Map<String, Object> getArgs() {
        return args;
    }

    public void setArgs(Map<String, Object> args) {
        this.args = args;
    }

    public String getScript() {
        return script;
    }

    public ScriptRequest setScript(String script) {
        this.script = script;
        return this;
    }

    public ScriptRequest embed(String name, Object object) {
        this.args.put(name, object);
        return this;
    }

    public ScriptResponse build() throws Exception {
        ApplicationContext context = ApplicationContextRegistry.getApplicationContext();
        velocityEngine = context.getBean(VelocityEngine.class);
        ScriptResponse response = this.runScript(args, script);

        if (response.getError() != null) {
            throw new Exception("Script Error", new Throwable(response.getError()));
        }
        /**
         * object, array 에 대한 json 파싱
         **/
        String type = response.getType();
        if (type.equals(ScriptResponse.OBJECT)) {
            response.setValue(JsonUtils.unmarshal((String) response.getValue()));
        } else if (type.equals(ScriptResponse.ARRAY)) {
            response.setValue(JsonUtils.unmarshalToList((String) response.getValue()));
        }
        return response;
    }

    private ScriptResponse runScript(Map<String, Object> args, String script) {
        ScriptResponse response = new ScriptResponse();
        try {
            File file = ResourceUtils.getFile("classpath:rhino");
            String path = file.getAbsolutePath();

            Context cx = Context.enter();
            Global global = new Global(cx);
            cx.setOptimizationLevel(-1);
            cx.setLanguageVersion(Context.VERSION_1_5);

            Main.processFile(cx, global, path + "/env.rhino.1.2.js");

            Map inputData = new HashMap();
            Map normal = new HashMap();
            Map json = new HashMap();
            Set<Map.Entry<String, Object>> entries = args.entrySet();
            Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> next = iterator.next();
                Object value = next.getValue();

                if (value instanceof Map) {
                    json.put(next.getKey(), JsonUtils.marshal(value));
                } else {
                    normal.put(next.getKey(), value);
                }
            }
            inputData.put("normal", normal);
            inputData.put("json", json);
            ScriptableObject.putProperty(global, "inputData", JsonUtils.marshal(inputData));

            Map model = new HashMap();
            model.put("script", script);

            String engineJs = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "rhino/engine.js", "UTF-8", model);

            cx.evaluateString(global, engineJs, "script", 1, null);
            org.mozilla.javascript.Function run = (org.mozilla.javascript.Function) global.get("run", global);
            Object result = run.call(cx, global, global, new Object[]{});

            Map map = (Map) Context.jsToJava(result, Map.class);

            response.setLog(map.get("log").toString());
            response.setValue(map.get("value"));
            response.setType(map.get("type").toString());

        } catch (Exception ex) {
            response.setLog("");
            response.setValue(null);
            response.setType(ScriptResponse.UNDEFINED);
            response.setError(ex.getMessage() + "");

            if (ex instanceof RhinoException) {
                response.setErrorLine(((RhinoException) ex).lineNumber());
                response.setErrorSource(((RhinoException) ex).lineSource());
            }

        } finally {
            Context.exit();
        }

        return response;
    }
}
