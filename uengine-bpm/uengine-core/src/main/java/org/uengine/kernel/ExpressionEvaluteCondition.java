package org.uengine.kernel;


import org.apache.bsf.BSFEngine;
import org.apache.bsf.BSFManager;
import org.uengine.kernel.bahama.GlobalAttributes;
import org.uengine.kernel.bahama.JsonUtils;

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

        try {
            String expression = getConditionExpression();
            Map expressionMap = JsonUtils.unmarshal(expression);
            String prevTaskId = (String) expressionMap.get("prevTaskId");
            String label = (String) expressionMap.get("label");
            Map jobMap = (Map) instance.get("jobMap");

            if (!jobMap.containsKey(prevTaskId)) {
                return false;
            }
            Map prevTaskResult = (Map) jobMap.get(prevTaskId);
            if (!prevTaskResult.containsKey("next")) {
                return false;
            }
            String next = (String) prevTaskResult.get("next");

            if (next.equals(label)) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            throw new RuntimeException("Exception during evaluate condition expression: " + getConditionExpression(), e);

        }
    }
}
