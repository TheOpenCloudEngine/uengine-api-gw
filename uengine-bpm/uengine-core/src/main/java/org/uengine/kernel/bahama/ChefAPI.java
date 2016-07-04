package org.uengine.kernel.bahama;

import org.jclouds.ContextBuilder;
import org.jclouds.chef.ChefContext;
import org.jclouds.chef.domain.*;
import org.jclouds.chef.options.CreateClientOptions;
import org.jclouds.chef.options.SearchOptions;
import org.jclouds.enterprisechef.EnterpriseChefApi;
import org.jclouds.enterprisechef.domain.Group;
import org.jclouds.enterprisechef.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.Set;

public class ChefAPI {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(ChefAPI.class);

    private EnterpriseChefApi api;

    private ChefContext context;

    private String userPemFilePath;

    private String chefServerUrl;

    private String organizationName;

    public void init() {
        try {
            File userPemFile = new File(userPemFilePath);

            FileInputStream fis = new FileInputStream(userPemFile);
            String pem = ResourceUtils.getResourceTextContents(fis);
            this.context = ContextBuilder.newBuilder("enterprisechef")
                    .endpoint(chefServerUrl + "/organizations/" + organizationName)
                    .credentials(organizationName , pem)
                    .buildView(ChefContext.class);

            this.api = context.unwrapApi(EnterpriseChefApi.class);
            fis.close();
        } catch (Exception ex) {
            logger.warn("Chef API를 초기화할 수 없습니다.", ex);
            ex.printStackTrace();
        }
    }

    public void initIfNull() {
        if (context == null) {
            this.init();
        }
    }

    public ChefAPI(String  userPemFilePath, String chefServerUrl, String organizationName) {
        this.userPemFilePath = userPemFilePath;
        this.chefServerUrl = chefServerUrl;
        this.organizationName = organizationName;

        init();
    }

    public User getUser(String var1) {
        initIfNull();
        try {
            return api.getUser(var1);
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public Set<String> listGroups() {
        initIfNull();
        try {
            return api.listGroups();
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public Group getGroup(String var1) {
        initIfNull();
        try {
            return api.getGroup(var1);
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public void createGroup(String var1) {
        initIfNull();
        try {
            api.createGroup(var1);
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public void updateGroup(Group var1) {
        initIfNull();
        try {
            api.updateGroup(var1);
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public void deleteGroup(String var1) {
        initIfNull();
        try {
            api.deleteGroup(var1);
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }


    public Set<String> listClients() {
        initIfNull();
        try {
            return api.listClients();
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public Client getClient(String clientName) {
        initIfNull();
        try {
            return api.getClient(clientName);
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public Client createClient(String clientName) {
        initIfNull();
        try {
            return api.createClient(clientName);
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public Client createClient(String clientName, CreateClientOptions options) {
        initIfNull();
        try {
            return api.createClient(clientName, options);
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public Client generateKeyForClient(String clientName) {
        initIfNull();
        try {
            return api.generateKeyForClient(clientName);
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public Client deleteClient(String clientName) {
        initIfNull();
        try {
            return api.deleteClient(clientName);
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public Set<String> listCookbooks() {
        initIfNull();
        try {
            return api.listCookbooks();
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public Set<CookbookDefinition> listCookbooksInEnvironment(String environmentName) {
        initIfNull();
        try {
            return api.listCookbooksInEnvironment(environmentName);
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public Set<CookbookDefinition> listCookbooksInEnvironment(String environmentName, String numVersions) {
        initIfNull();
        try {
            return api.listCookbooksInEnvironment(environmentName, numVersions);
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public Set<String> listVersionsOfCookbook(String cookbookName) {
        initIfNull();
        try {
            return api.listVersionsOfCookbook(cookbookName);
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public CookbookVersion getCookbook(String cookbookName, String version) {
        initIfNull();
        try {
            return api.getCookbook(cookbookName, version);
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public CookbookDefinition getCookbookInEnvironment(String environmentName, String cookbookName) {
        initIfNull();
        try {
            return api.getCookbookInEnvironment(environmentName, cookbookName);
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public CookbookDefinition getCookbookInEnvironment(String environmentName, String cookbookName, String numVersions) {
        initIfNull();
        try {
            return api.getCookbookInEnvironment(environmentName, cookbookName, numVersions);
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public Set<String> listRecipesInEnvironment(String environmentName) {
        initIfNull();
        try {
            return api.listRecipesInEnvironment(environmentName);
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public CookbookVersion updateCookbook(String cookbookName, String version, CookbookVersion cookbook) {
        initIfNull();
        try {
            return api.updateCookbook(cookbookName, version, cookbook);
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public CookbookVersion deleteCookbook(String cookbookName, String version) {
        initIfNull();
        try {
            return api.deleteCookbook(cookbookName, version);
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public Set<String> listDatabags() {
        initIfNull();
        try {
            return api.listDatabags();
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public void createDatabag(String databagName) {
        initIfNull();
        try {
            api.createDatabag(databagName);
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public void deleteDatabag(String databagName) {
        initIfNull();
        try {
            api.deleteDatabag(databagName);
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public Set<String> listDatabagItems(String databagName) {
        initIfNull();
        try {
            return api.listDatabagItems(databagName);
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public DatabagItem getDatabagItem(String databagName, String databagItemId) {
        initIfNull();
        try {
            return api.getDatabagItem(databagName, databagItemId);
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public DatabagItem createDatabagItem(String databagName, DatabagItem databagItem) {
        initIfNull();
        try {
            return api.createDatabagItem(databagName, databagItem);
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public DatabagItem updateDatabagItem(String databagName, DatabagItem item) {
        initIfNull();
        try {
            return api.updateDatabagItem(databagName, item);
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public DatabagItem deleteDatabagItem(String databagName, String databagItemId) {
        initIfNull();
        try {
            return api.deleteDatabagItem(databagName, databagItemId);
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public Set<String> listEnvironments() {
        initIfNull();
        try {
            return api.listEnvironments();
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public Environment getEnvironment(String environmentName) {
        initIfNull();
        try {
            return api.getEnvironment(environmentName);
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public void createEnvironment(Environment environment) {
        initIfNull();
        try {
            api.createEnvironment(environment);
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public Environment updateEnvironment(Environment environment) {
        initIfNull();
        try {
            return api.updateEnvironment(environment);
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public Environment deleteEnvironment(String environmentName) {
        initIfNull();
        try {
            return api.deleteEnvironment(environmentName);
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public Set<String> listNodes() {
        initIfNull();
        try {
            return api.listNodes();
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public Set<String> listNodesInEnvironment(String environmentName) {
        initIfNull();
        try {
            return api.listNodesInEnvironment(environmentName);
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public Node getNode(String nodeName) {
        initIfNull();
        try {
            return api.getNode(nodeName);
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public void createNode(Node node) {
        initIfNull();
        try {
            api.createNode(node);
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public Node updateNode(Node node) {
        initIfNull();
        try {
            return api.updateNode(node);
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public Node deleteNode(String nodeName) {
        initIfNull();
        try {
            return api.deleteNode(nodeName);
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public Set<String> listRoles() {
        initIfNull();
        try {
            return api.listRoles();
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public Role getRole(String roleName) {
        initIfNull();
        try {
            return api.getRole(roleName);
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public void createRole(Role role) {
        initIfNull();
        try {
            api.createRole(role);
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public Role updateRole(Role role) {
        initIfNull();
        try {
            return api.updateRole(role);
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public Role deleteRole(String roleName) {
        initIfNull();
        try {
            return api.deleteRole(roleName);
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

    public SearchResult<? extends Client> searchClients(SearchOptions options) {
        initIfNull();
        try {
            return api.searchClients(options);
        } finally {
            try {
                context.close();
                context = null;
                api = null;
            } catch (Exception ex) {
            }
        }
    }

}
