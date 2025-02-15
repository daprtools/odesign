package io.dapr.daprdesigner.design;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.EStructuralFeature;

import daprdesigner.*;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * The services class used by VSM.
 */
public class Services {

	int unnamedCounter = 0;
	
	String pubsub = "pubsub.kafka,pubsub.in-memory,pubsub.jetstream,pubsub.kubemq,"
			+ "pubsub.mqtt3,pubsub.pulsar,pubsub.rabbitmq,pubsub.redis,pubsub.rocketmq,pubsub.solace.amqp,"
			+ "pubsub.aws.snssqs,pubsub.gcp.pubsub,pubsub.azure.eventhubs,pubsub.azure.servicebus.queues,azure.servicebus.topics";
	
	String bindings ="bindings.apns,bindings.commercetools,bindings.cron,bindings.graphql,bindings.http,bindings.huawei.obs,"
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
	
	String statestore = "state.Aerospike,state.cassandra,state.cockroachdb,state.couchbase,state.etcd,state.consul,"
			+ "state.hazelcast,state.in-memory,state.jetstream,state.memcached,state.mysql,state.mongodb,state.oracledatabase,"
			+ "state.postgresql,state.redis,state.rethinkdb,state.sqlite,state.zookeeper,state.aws.dynamodb,"
			+ "state.cloudflare.workerskv,state.gcp.firestore,state.azure.blobstorage,state.azure.cosmosdb,"
			+ "state.azure.tablestorage,state.sqlserver,state.oci.objectstorage";
	
	String secretstore ="secretstores.hashicorp.vault,secretstores.kubernetes,secretstores.local.env,"
			+ "secretstores.local.file,secretstores.alicloud.parameterstore,secretstores.aws.secretmanager,"
			+ "secretstores.aws.parameterstore,secretstores.gcp.secretmanager,secretstores.azure.keyvault";
	String configurationstore = "configuration.postgresql,configuration.redis,configuration.azure.appconfig";
	String locks ="lock.redis";
	String cryptography ="crypto.dapr.jwks,crypto.dapr.kubernetes.secrets,crypto.dapr.localstorage,crypto.azure.keyvault";
	
			

	/**
	 * See
	 * http://help.eclipse.org/neon/index.jsp?topic=%2Forg.eclipse.sirius.doc%2Fdoc%2Findex.html&cp=24
	 * for documentation on how to write service methods.
	 */
	public EObject myService(EObject self, String arg) {
		// TODO Auto-generated code
		return self;
	}

	public String getUnnamed(Block self) {
		unnamedCounter++;
		return self.getBlockType().getName() + "_" + unnamedCounter;
	}

	public String getUnnamedNodeBlock(NodeBlocks self) {
		unnamedCounter++;
		return self.getNodeBlockType().getName() + "_" + unnamedCounter;
	}

	public String getDaprNodeName(DaprNode self) {
		unnamedCounter++;
		String s = self.getClass().getCanonicalName();
		s = s.substring(s.lastIndexOf('.') + 1, s.indexOf("Impl"));
		return getAllCaps(s)+ "_" + unnamedCounter;
	}
	

	private String getAllCaps(String s) {
		StringBuffer sb = new StringBuffer();
		for( char c : s.toCharArray()) {
			if(Character.isUpperCase(c))
				sb.append(c);			
		}
		
		return sb.toString().toLowerCase();
	}

	public boolean isNodeBlockType(NodeBlocks self, String nodeType) {

		System.out.println("Here " + self.getNodeBlockType().toString());
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

		System.out.println("Caleed ================");
		source.getConfigurations().remove(target);
	}

	public void openURL(EObject self, EStructuralFeature feature) {
		String uri = self.eGet(feature).toString();
		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
			try {
				Desktop.getDesktop().browse(new URI(uri));
			} catch (IOException | URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	public ArrayList<String> getComponentValues(EObject self) {
		
		String className = self.getClass().getName();
		System.out.println(className.substring(className.lastIndexOf('.') + 1, className.indexOf("Impl")).toLowerCase());
		ArrayList<String> als = new ArrayList<String>();
		for(String s: pubsub.split(","))
			als.add(s);
		return als;
		

	}

	
	public EObject addStringValue(EObject self, EStructuralFeature feature, String value) {
		System.out.println("Hellloooo...." + feature.getName() + " " + value + " " + feature.getClass().getName());

		System.out.println(self.eGet(feature).getClass().getName());

		if (self instanceof SecretsAccessList) {

			SecretsAccessList s = (SecretsAccessList) self;
			s.getSecrets().add(value);

		}
		if (self instanceof MetricsConfiguration) {
			MetricsConfiguration m = (MetricsConfiguration) self;
			m.getHttp_pathMatching().add(value);
		}

		return self;

	}

	public EObject removeStringValue(EObject self, EStructuralFeature feature, Object value) {
		System.out.println("Hellloooo remove...." + feature.getName() + " " + value + " " + feature.getClass().getName()
				+ " " + value.getClass().getName());

		System.out.println(self.eGet(feature).getClass().getName());

		if (self instanceof SecretsAccessList) {

			SecretsAccessList s = (SecretsAccessList) self;
			s.getSecrets().removeAll((ArrayList) value);

		}
		if (self instanceof MetricsConfiguration) {
			MetricsConfiguration m = (MetricsConfiguration) self;
			m.getHttp_pathMatching().removeAll((ArrayList) value);
		}

		return self;

	}
}
