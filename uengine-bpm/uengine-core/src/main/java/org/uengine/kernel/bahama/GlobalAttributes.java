package org.uengine.kernel.bahama;

import org.jclouds.chef.domain.Client;
import org.jclouds.chef.domain.Node;
import org.jclouds.chef.domain.SearchResult;
import org.jclouds.chef.options.SearchOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uengine.kernel.ProcessInstance;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cloudine on 2015. 2. 26..
 */
public class GlobalAttributes {
    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(GlobalAttributes.class);

    private Map glovalSet = new HashMap();

    private Map vmTemplate;

    private ChefAPI chefAPI;

    public GlobalAttributes() {

    }

    public GlobalAttributes(Map uengineChefConf) {
        this.vmTemplate = (Map) uengineChefConf.get("template");
        String userPemFile = uengineChefConf.get("userPemFile").toString();
        String chefServerUrl = uengineChefConf.get("chefServerUrl").toString();
        String organizationName = uengineChefConf.get("organizationName").toString();

        this.chefAPI = new ChefAPI(userPemFile, chefServerUrl, organizationName);
    }

    public void registJobResultMap(ProcessInstance instance) throws Exception {
        instance.set("jobMap", new HashMap());
    }

    public Map getJobMap(ProcessInstance instance) throws Exception {
        if (instance.get("jobMap") != null) {
            return (Map) instance.get("jobMap");
        } else {
            this.registJobResultMap(instance);
            return new HashMap();
        }
    }

    public void setJobMap(ProcessInstance instance, Map jobMap) throws Exception {
        instance.set("jobMap", jobMap);
    }

    public String getResolvedJsonAttrWithoutChef(ProcessInstance instance,  Map params)throws Exception{
        //jsonattritubes
        String jsonattritubes = params.get("json").toString();
        String[] split = jsonattritubes.split("#\\{");
        String identifier = "WORKFLOW";
        for (int i = 0; i < split.length; i++) {
            if (split[i].indexOf(identifier) == 0) {
                int cut = split[i].indexOf("}");
                if (cut != -1) {
                    String key = split[i].substring(0, cut);
                    String keyForReplace = "#\\{" + key + "\\}";
                    String wf_key = key.split("::")[1];
                    String wf_value = params.get(wf_key).toString();
                    jsonattritubes = jsonattritubes.replaceAll(keyForReplace, wf_value);
                }
            }
        }
        return jsonattritubes;
    }

    public String getResolvedJsonAttr(ProcessInstance instance,  Map params) throws Exception {

        Map jobMap = this.getJobMap(instance);

        //노드 expression 템플릿
        Map templates = this.vmTemplate;

        //jsonattritubes
        String jsonattritubes = params.get("json").toString();
        String[] split = jsonattritubes.split("#\\{");
        String[] identifiers = {"CANVAS", "WORKFLOW", "NODE"};
        for (int i = 0; i < split.length; i++) {
            for (String identifier : identifiers) {
                if (split[i].indexOf(identifier) == 0) {
                    int cut = split[i].indexOf("}");
                    if (cut != -1) {
                        String key = split[i].substring(0, cut);
                        String keyForReplace = "#\\{" + key + "\\}";
                        switch (identifier) {
                            case "CANVAS":
                                String canvas_node = key.split("::")[1];
                                String canvas_key = key.split("::")[2];
                                this.hasTaskNodeInfo(instance, canvas_node );
                                String canvas_nodejson = jobMap.get(canvas_node).toString();
                                String canvas_provider = JsonUtils.getValueIgnore(canvas_nodejson, "$.normalAttributes.provider");
                                Map canvas_template = (Map) templates.get(canvas_provider);
                                String canvas_expression = canvas_template.get(canvas_key).toString();
                                String canvas_value = JsonUtils.getValueIgnore(canvas_nodejson, canvas_expression);
                                System.out.println(keyForReplace + " is Converted " + canvas_value);
                                jsonattritubes = jsonattritubes.replaceAll(keyForReplace, canvas_value);
                                break;
                            case "NODE":
                                String server_node = key.split("::")[1];
                                String server_key = key.split("::")[2];
                                this.isNodeExist(server_node);
                                Node node = chefAPI.getNode(server_node);
                                String server_nodejson = JsonUtils.marshal(node);
                                this.isNodeBootstraped(server_nodejson);

                                String server_provider = JsonUtils.getValueIgnore(server_nodejson, "$.normalAttributes.provider");
                                Map server_template = (Map) templates.get(server_provider);
                                String server_expression = server_template.get(server_key).toString();
                                String server_value = JsonUtils.getValueIgnore(server_nodejson, server_expression);
                                System.out.println(keyForReplace + " is Converted " + server_value);
                                jsonattritubes = jsonattritubes.replaceAll(keyForReplace, server_value);
                                break;
                            case "WORKFLOW":
                                String wf_key = key.split("::")[1];
                                String wf_value = params.get(wf_key).toString();
                                System.out.println(keyForReplace + " is Converted " + wf_value);
                                jsonattritubes = jsonattritubes.replaceAll(keyForReplace, wf_value);
                                break;
                        }
                    }
                }
            }
        }
        return jsonattritubes;
    }

    public void hasTaskNodeInfo(ProcessInstance instance, String targetTaskId ) throws Exception {
        Map jobMap = this.getJobMap(instance);
        if (!jobMap.containsKey(targetTaskId))

            throw new Exception("ConditionExpression 을 실행하기 위해서 " + targetTaskId + " 가(이) 선행되어야 합니다." +
                    " 워크플로우를 재검토 하여 주십시오.");
    }


    public void isNodeBootstraped(String node) throws Exception {
        Map map = (Map) JsonUtils.unmarshal(node);
        String nodename = map.get("name").toString();


        if (!((Map) map.get("normalAttributes")).containsKey("provider"))
            throw new Exception("노드 " + nodename + " 는 부트스트랩 되지 않은 노드입니다. 워크플로우를 재검토 하여 주십시오.");
    }

    public void isNodeExist(String nodename) throws Exception {
        SearchOptions searchOption = new SearchOptions().query("name:" + nodename);
        SearchResult<? extends Client> nodes = chefAPI.searchClients(searchOption);
        if (nodes.size() < 1)
            throw new Exception("Chef Server에 " + nodename + " 노드가 존재하지 않습니다.");
    }

}
