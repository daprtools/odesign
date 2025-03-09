package io.dapr.daprdesigner.design;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.EStructuralFeature;

import daprdesigner.*;
import java.awt.Desktop;

import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.NetworkChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * The services class used by VSM.
 */
public class Services {

	static int unnamedCounter = 0;

	static String pubsub = "pubsub.kafka,pubsub.in-memory,pubsub.jetstream,pubsub.kubemq,"
			+ "pubsub.mqtt3,pubsub.pulsar,pubsub.rabbitmq,pubsub.redis,pubsub.rocketmq,pubsub.solace.amqp,"
			+ "pubsub.aws.snssqs,pubsub.gcp.pubsub,pubsub.azure.eventhubs,pubsub.azure.servicebus.queues,azure.servicebus.topics";

	static String bindings = "bindings.apns,bindings.commercetools,bindings.cron,bindings.graphql,bindings.http,bindings.huawei.obs,"
			+ "bindings.influxdb,bindings.kafka,bindings.kubernetes,bindings.localstorage,bindings.mqtt3,bindings.mysql,"
			+ "bindings.postgresql,bindings.postmark,bindings.rabbitmq,bindings.redis,bindings.rethinkdb.statechange,"
			+ "bindings.twilio.sendgrid,bindings.smtp,bindings.twilio.sms,bindings.wasm,bindings.dingtalk.webhook,"
			+ "bindings.alicloud.oss,bindings.alicloud.sls,bindings.alicloud.tablestore,"
			+ "bindings.aws.dynamodb,bindings.aws.kinesis,bindings.aws.s3,bindings.aws.ses,"
			+ "bindings.aws.sns,bindings.aws.sqs,bindings.cloudflare.queues,bindings.gcp.pubsub,"
			+ "bindings.gcp.bucket,bindings.azure.blobstorage,bindings.azure.cosmosdb.gremlinapi,"
			+ "bindings.azure.cosmosdb,bindings.azure.eventgrid,bindings.azure.eventhubs,"
			+ "bindings.azure.openai,bindings.azure.servicebusqueues,"
			+ "bindings.azure.signalr,bindings.azure.storagequeues,bindings.zeebe.command,bindings.zeebe.jobworker";

	static String statestore = "state.Aerospike,state.cassandra,state.cockroachdb,state.couchbase,state.etcd,state.consul,"
			+ "state.hazelcast,state.in-memory,state.jetstream,state.memcached,state.mysql,state.mongodb,state.oracledatabase,"
			+ "state.postgresql,state.redis,state.rethinkdb,state.sqlite,state.zookeeper,state.aws.dynamodb,"
			+ "state.cloudflare.workerskv,state.gcp.firestore,state.azure.blobstorage,state.azure.cosmosdb,"
			+ "state.azure.tablestorage,state.sqlserver,state.oci.objectstorage";

	static String secretstore = "secretstores.hashicorp.vault,secretstores.kubernetes,secretstores.local.env,"
			+ "secretstores.local.file,secretstores.alicloud.parameterstore,secretstores.aws.secretmanager,"
			+ "secretstores.aws.parameterstore,secretstores.gcp.secretmanager,secretstores.azure.keyvault";

	static String configurationstore = "configuration.postgresql,configuration.redis,configuration.azure.appconfig";

	static String locks = "lock.redis";

	static String cryptography = "crypto.dapr.jwks,crypto.dapr.kubernetes.secrets,crypto.dapr.localstorage,crypto.azure.keyvault";

	static String middleware = "middleware.http.oauth2,middleware.http.oauth2clientcredentials,"
			+ "middleware.http.bearer,middleware.http.ratelimit,middleware.http.opa,"
			+ "middleware.http.routeralias,middleware.http.routerchecker,"
			+ "middleware.http.sentinel,middleware.http.uppercase,middleware.http.wasm";

	/**
	 * See
	 * http://help.eclipse.org/neon/index.jsp?topic=%2Forg.eclipse.sirius.doc%2Fdoc%2Findex.html&cp=24
	 * for documentation on how to write service methods.
	 */
	public EObject myService(EObject self, String arg) {

		return self;
	}

	public String getUnnamed(Block self) {
		unnamedCounter++;
		return self.getBlockType().getName().toLowerCase() + "_" + unnamedCounter;
	}

	public String getUnnamedNodeBlock(NodeBlocks self) {
		unnamedCounter++;
		return self.getNodeBlockType().getName().toLowerCase() + "_" + unnamedCounter;
	}

	public EObject getContainer(EObject self) {

		return self;
	}

	public Collection<App> getAllApps(Block block) {

		
		ArrayList<App> appList = new ArrayList<App>();
		for (DaprNode node : block.getNodes()) {
			if (node instanceof NodeBlocks) {
				NodeBlocks nb = (NodeBlocks) node;
				for (DaprNode node1 : nb.getNodes()) {
					if (node1 instanceof App) {
						appList.add((App) node1);
					}
				}
			}
		}
		return appList;
	}

	public boolean isAppConfiguration(EObject self) {
		return self instanceof AppConfiguration;
	}

	public boolean isComponent(EObject self) {
		return self instanceof Component;
	}

	public Collection<Component> getComponents(Block block) {

		ArrayList<Component> componentList = new ArrayList<Component>();
		for (DaprNode node : block.getNodes()) {
			if (node instanceof NodeBlocks) {
				NodeBlocks nb = (NodeBlocks) node;
				for (DaprNode node1 : nb.getNodes()) {
					if (node1 instanceof Component) {
						componentList.add((Component) node1);
					}
				}
			}
		}
		return componentList;
	}

	public ArrayList<Component> getConnectedComponents(App self) {

		Collection<Component> components = getAllComponents((Block) self.eContainer().eContainer().eContainer());
		ArrayList<Component> ac = new ArrayList<Component>();
		for (Component c : components)
			if (c.getScopes().contains(self))
				ac.add(c);

		return ac;
	}

	private Collection<Component> getAllComponents(Block environmentBlock) {

		TreeIterator<EObject> iterator = environmentBlock.eAllContents();
		ArrayList<Component> ac = new ArrayList<Component>();

		while (iterator.hasNext()) {
			EObject eo = iterator.next();
			if (eo instanceof Component) {
				Component c = (Component) eo;
				//System.out.println("Component name :" + c.getName());
				ac.add(c);
			}

		}

		return ac;
	}

	public Collection<EObject> getRelations(App self, boolean isRestrictedAccess) {

		// System.out.println(" Finding for " + self.getName() + " for " +
		// isRestrictedAccess);
		ArrayList<EObject> appList = new ArrayList<EObject>();
		Block b = (Block) self.eContainer().eContainer().eContainer();

		Collection<EObject> cObject = getAppsinEnvironment(b, BlockType.MICROSERVICES, NodeBlockType.APP);
		cObject.addAll(getAppsinEnvironment(b, BlockType.ACTORS, NodeBlockType.ACTOR));
		cObject.addAll(getAppsinEnvironment(b, BlockType.WORKFLOW, NodeBlockType.WORKFLOW));
		cObject.addAll(getAppsinEnvironment(b, BlockType.JOBS, NodeBlockType.JOBS));

		AppAccessControl aac = findAppAccessControl(self.getConfigurations());
		if (aac == null) {
			for (EObject eo : cObject) {
				if (eo instanceof App)// || eo instanceof Actor || eo instanceof Jobs || eo instanceof Workflow)
				{
					if (((App) eo).getName().equals(self.getName()))
						continue;
					if (!isRestrictedAccess) {
						System.out.println("Adding " + ((App) eo).getName() + " for " + self.getName() + " when "
								+ isRestrictedAccess);
						appList.add(eo);
					}
				}
			}
			return appList;
		} else {

			for (EObject eo : cObject) {
				if (eo instanceof App)// || eo instanceof Actor || eo instanceof Jobs || eo instanceof Workflow)
				{
					App app = (App) eo;
					if (app.getName().equals(self.getName()))
						continue;

					AppPolicy ap = findAppPolicy(aac, app);
					if (ap == null) {
						if (aac.getDefaultAction() == AccessAction.ALLOW)
							if (!isRestrictedAccess) {
								System.out.println("Adding " + app.getName() + " for " + self.getName() + " when "
										+ isRestrictedAccess);
								appList.add(app);
							}
					} else {

						boolean isAllowedAll = false;
						boolean isAllowedSome = false;
						boolean isDeniedSome = false;
						boolean isOperationDefinedForApp = false;

						if (ap.getDefaultAction() == AccessAction.ALLOW)
							isAllowedAll = true;

						for (Operation o : ap.getOperations()) {
							isOperationDefinedForApp = true;
							if (o.getAction() == AccessAction.ALLOW) {
								isAllowedSome = true;
							}
							if (o.getAction() == AccessAction.DENY) {
								isDeniedSome = true;
							}

						}

						if (isRestrictedAccess) {
							if ((!isAllowedAll && isAllowedSome) || (isAllowedAll && isDeniedSome)) {
								// System.out.println ("Adding "+ app.getName() + " for " + self.getName() + "
								// when " + isRestrictedAccess);
								appList.add(app);
							}
						} else {
							if ((isAllowedAll && !isOperationDefinedForApp) || (isAllowedAll && !isDeniedSome)) {
								// System.out.println ("Adding "+ app.getName() + " for " + self.getName() + "
								// when " + isRestrictedAccess);
								appList.add(app);
							}

						}

					}

				}
			}
		}

		// for (EObject app : appList)
		// System.out.print(((App) app).getName() + " ");

		//System.out.println();
		return appList;

	}

	public boolean isPubSub(EObject self) {
		return self instanceof PubSub;
	}

	private AppPolicy findAppPolicy(AppAccessControl aac, App app) {

		for (AppPolicy ap : aac.getPolicies()) {
			// System.out.println("Apppolicy name " + ap.getApp().getName() + " " +
			// app.getName());
			if (ap.getApp().getName().equals(app.getName())
					&& ap.getTrustDomain().getName().equals(app.getTrustDomain().getName()))
				return ap;
		}

		return null;
	}

	private AppAccessControl findAppAccessControl(EList<AppConfiguration> configurations) {
		for (AppConfiguration ac : configurations)
			if (ac instanceof AppAccessControl)
				return (AppAccessControl) ac;

		return null;
	}

	public Collection<EObject> getAppsinEnvironment(Block block, BlockType bType, NodeBlockType nType) {

		ArrayList<EObject> appList = new ArrayList<EObject>();
		if (block.getBlockType() == BlockType.ENVIRONMENT) {

			for (Block subblock : block.getSubblocks()) {
				if (subblock.getBlockType() == bType) {

					for (DaprNode node : subblock.getNodes()) {
						if (node instanceof NodeBlocks) {
							NodeBlocks nb = (NodeBlocks) node;

							if (nb.getNodeBlockType() == nType) {

								for (DaprNode node1 : nb.getNodes()) {

									if (nType == NodeBlockType.APP) {
										if (node1 instanceof App) {
											appList.add((EObject) node1);
										}
									}
									if (nType == NodeBlockType.ACTOR) {
										if (node1 instanceof Actor) {
											appList.add((EObject) node1);
										}
									}
									if (nType == NodeBlockType.JOBS) {
										if (node1 instanceof Jobs) {
											appList.add((EObject) node1);
										}
									}
									if (nType == NodeBlockType.WORKFLOW) {
										if (node1 instanceof Workflow) {
											appList.add((EObject) node1);
										}
									}
								}
							}
						}
					}
				}
			}
		}

		return appList;
	}

	public boolean getFilters(EObject self) {

		//String s = self.eClass().getInstanceClassName();

		if(self instanceof App || self instanceof NodeBlocks) {

			return true;
		}

		return false;
	}

	public String getDaprNodeName(DaprNode self) {
		unnamedCounter++;
		String s = self.getClass().getCanonicalName();
		s = s.substring(s.lastIndexOf('.') + 1, s.indexOf("Impl"));
		return getAllCaps(s) + "_" + unnamedCounter;
	}

	private String getAllCaps(String s) {
		StringBuffer sb = new StringBuffer();
		for (char c : s.toCharArray()) {
			if (Character.isUpperCase(c))
				sb.append(c);
		}

		return sb.toString().toLowerCase();
	}

	public boolean isNodeBlockType(NodeBlocks self, String nodeType) {

		return self.getNodeBlockType().toString() == nodeType;
	}

	public String getValueAsString(EObject self, Object o) {

		return o.toString();
	}

	public void addAppConfiguration(App source, AppConfiguration target) {

		source.getConfigurations().add(target);
	}

	public void addAccessList(SecretsAccessConfiguration source, SecretsAccessList target) {

		source.getAccessList().add(target);
	}

	public void addAPI(APIAccessControl source, API target) {

		source.getApiList().add(target);
	}

	public void addAppPolicy(AppAccessControl source, AppPolicy target) {

		source.getPolicies().add(target);

	}

	public void addHttpHandler(MiddlewareConfiguration source, HttpHandler target) {

		source.getHttpHandlers().add(target);

	}

	public void addOperation(AppPolicy source, Operation target) {

		source.getOperations().add(target);

	}

	public void addRouteRules(SubscriptionConfiguration source, RouteRules target) {

		source.getRouteRules().add(target);

	}

	public void setResiliencyPolicy(ResiliencyConfiguration source, ResiliencyPolicy target) {

		source.setPolicy(target);

	}

	public void addRetryPolicy(ResiliencyPolicy source, RetryPolicy target) {

		source.getRetries().add(target);

	}

	public void addCircuitBreakerPolicy(ResiliencyPolicy source, CircuitBreakerPolicy target) {

		source.getCircuitBreakers().add(target);

	}

	public void addResiliencyTimeouts(ResiliencyPolicy source, ResiliencyTimeout target) {

		source.getTimeoutDefinitions().add(target);

	}

	public void addResiliencyTargets(ResiliencyConfiguration source, ResiliencyTarget target) {

		source.getResiliencyTargets().add(target);

	}

	public void addSpecMetadatHeadersToHTTPEndPoint(HTTPEndPoint source, SpecMetadata target) {

		source.getSpecHeaders().add(target);

	}

	public void addSpecMetadaToComponent(Component source, SpecMetadata target) {

		source.getSpecMetadata().add(target);

	}

	public void removeAppConfiguration(App source, AppConfiguration target) {

		source.getConfigurations().remove(target);
	}

	public void openURL(EObject self, EStructuralFeature feature) {
		String uri = self.eGet(feature).toString();
		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
			try {
				Desktop.getDesktop().browse(new URI(uri));
			} catch (IOException | URISyntaxException e) {

				e.printStackTrace();
			}
		}

	}

	public List<String> getComponentValues(EObject self) {

		String className = self.getClass().getName();
		String classNameLowerCase = className.substring(className.lastIndexOf('.') + 1, className.indexOf("Impl"))
				.toLowerCase();

		switch (classNameLowerCase) {
		case "pubsub":
			return (List<String>) Arrays.asList(pubsub.split(","));
		case "middleware":
			return (List<String>) Arrays.asList(middleware.split(","));
		case "bindings":
			return (List<String>) Arrays.asList(bindings.split(","));
		case "secretstore":
			return (List<String>) Arrays.asList(secretstore.split(","));
		case "cryptography":
			return (List<String>) Arrays.asList(cryptography.split(","));
		case "statestore":
			return (List<String>) Arrays.asList(statestore.split(","));
		case "locks":
			return (List<String>) Arrays.asList(locks.split(","));
		case "configurationstore":
			return (List<String>) Arrays.asList(configurationstore.split(","));

		}

		return new ArrayList<String>();

	}

	public EObject addStringValue(EObject self, EStructuralFeature feature, String value) {

		if (self instanceof SecretsAccessList) {

			SecretsAccessList s = (SecretsAccessList) self;
			if (feature.getName().equals("allowedSecrets"))
				s.getAllowedSecrets().add(value);
			if (feature.getName().equals("deniedSecrets"))
				s.getDeniedSecrets().add(value);

		}
		if (self instanceof MetricsConfiguration) {
			MetricsConfiguration m = (MetricsConfiguration) self;
			m.getHttp_pathMatching().add(value);
		}

		return self;

	}

	public EObject removeStringValue(EObject self, EStructuralFeature feature, Object value) {

		if (self instanceof SecretsAccessList) {

			SecretsAccessList s = (SecretsAccessList) self;
			if (feature.getName().equals("allowedSecrets"))
				s.getAllowedSecrets().removeAll((ArrayList) value);
			if (feature.getName().equals("deniedSecrets"))
				s.getDeniedSecrets().removeAll((ArrayList) value);

		}
		if (self instanceof MetricsConfiguration) {
			MetricsConfiguration m = (MetricsConfiguration) self;
			m.getHttp_pathMatching().removeAll((ArrayList) value);
		}

		return self;

	}
}
