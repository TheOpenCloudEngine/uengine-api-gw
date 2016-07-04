package org.uengine.kernel;


import org.apache.bsf.BSFEngine;
import org.apache.bsf.BSFManager;
import org.uengine.kernel.bahama.GlobalAttributes;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.HashMap;
import java.util.Map;

public class ExpressionEvaluteCondition extends Condition {

    String conditionExpression;

    public String getConditionExpression() {
        return conditionExpression;
    }

    public void setConditionExpression(String conditionExpression) {
        this.conditionExpression = conditionExpression;
    }


    public ExpressionEvaluteCondition(String conditionExpression) {
        super();
        setConditionExpression(conditionExpression
        );
    }

    @Override
    public boolean isMet(ProcessInstance instance, String scope) throws Exception {


        //오리지널 코드
//        try {
//            BSFManager manager = new BSFManager();
//            manager.setClassLoader(this.getClass().getClassLoader());
//
//
//            if (instance != null)
//                manager.declareBean("instance", instance, ProcessInstance.class);
//
//            manager.declareBean("activity", this, Activity.class);
//            manager.declareBean("definition", instance.getProcessDefinition(), ProcessDefinition.class);
//            manager.declareBean("util", new ScriptUtil(), ScriptUtil.class);
//
//            BSFEngine engine = manager.loadScriptingEngine("javascript");
//
//            Object result = engine.eval("my_class.my_generated_method", 0, 0, "function getVal(){\nimportPackage(java.lang); \n " + getConditionExpression() + "}\n getVal();");
//
//            if (result instanceof Boolean) {
//                return (Boolean) result;
//            } else {
//                throw new RuntimeException("Not a boolean return value by the condition expression: " + getConditionExpression() + ". Returned is " + result);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("Exception during evaluate condition expression: " + getConditionExpression(), e);
//
//        }

        try {
            String script = getConditionExpression();

            //스크립트중에서 익스프레션 발류를 실제값으로 치환하는 작업이 필요.



            Map params = new HashMap();
            Map vars = (Map) instance.get("vars");
            Map global = (Map) vars.get("global");
            params.putAll(global);
            params.put("json", script);


            GlobalAttributes globalAttributes = null;
            String resolvedJsonAttr = null;
            if(instance.get("uengineChefConf") != null){
                Map uengineChefConf = (Map) instance.get("uengineChefConf");
                globalAttributes = new GlobalAttributes(uengineChefConf);
                resolvedJsonAttr = globalAttributes.getResolvedJsonAttr(instance, params);
            }else{
                globalAttributes = new GlobalAttributes();
                resolvedJsonAttr = globalAttributes.getResolvedJsonAttrWithoutChef(instance, params);
            }


            System.out.println("resolvedScript : " + resolvedJsonAttr);

            //치환되어 완성된 스크립트를 실행한다.
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");

            engine.eval(resolvedJsonAttr);
            Invocable inv = (Invocable) engine;
            Object result = inv.invokeFunction("run");

            if (result instanceof Boolean) {
                return (Boolean) result;
            } else {
                throw new RuntimeException("Not a boolean return value by the condition expression: " + getConditionExpression() + ". Returned is " + result);
            }
        } catch (Exception e) {
            throw new RuntimeException("Exception during evaluate condition expression: " + getConditionExpression(), e);

        }
    }
}
