package io.dapr.daprdesigner.design;


import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.EStructuralFeature;


import daprdesigner.*;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
/**
 * The services class used by VSM.
 */
public class Services {
	
	int unnamedCounter = 0;
    
    /**
    * See http://help.eclipse.org/neon/index.jsp?topic=%2Forg.eclipse.sirius.doc%2Fdoc%2Findex.html&cp=24 for documentation on how to write service methods.
    */
    public EObject myService(EObject self, String arg) {
       // TODO Auto-generated code
      return self;
    }
    
    public String getUnnamed(Block self) {
    	unnamedCounter++;
    	return self.getBlockType().getName()+"_"+unnamedCounter;
    }
    
    public String getUnnamedNodeBlock(NodeBlocks self) {
    	unnamedCounter++;
    	return self.getNodeBlockType().getName()+"_"+unnamedCounter;
    }
    
    public boolean isNodeBlockType(NodeBlocks self, String nodeType) {
    	
    	System.out.println("Here "+self.getNodeBlockType().toString());
    	return self.getNodeBlockType().toString()==nodeType;
    }
    
    public String getValueAsString(EObject self, Object o) {
    	
    	return o.toString();
    }
    
   public void addAppConfiguration(App source, AppConfiguration target) {
	   
	   source.getConfigurations().add(target);
   }
   
   public void removeAppConfiguration(App source, AppConfiguration target) {
	   
	   System.out.println("Caleed ================");
	   source.getConfigurations().remove(target);
   }
   
   public void openURL(EObject self,EStructuralFeature feature) {
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
   
   public EObject addStringValue(EObject self,EStructuralFeature feature, String value)
   {
	   System.out.println("Hellloooo...." + feature.getName() + " " + value + " "+ feature.getClass().getName());
	   
	   System.out.println(self.eGet(feature).getClass().getName());
	   
	   if(self instanceof SecretsAccessList) {
		   
		   SecretsAccessList s = (SecretsAccessList)self;
		   s.getSecrets().add(value);
		   
	   }
	   if(self instanceof MetricsConfiguration) {
		   MetricsConfiguration m = (MetricsConfiguration)self;
		   m.getHttp_pathMatching().add(value);
	   }
	   
	   return self;
	   

	   
   }
   
   public EObject removeStringValue(EObject self,EStructuralFeature feature, Object value)
   {
	   System.out.println("Hellloooo remove...." + feature.getName() + " " + value + " "+ feature.getClass().getName() + " " + value.getClass().getName());
	   
	   System.out.println(self.eGet(feature).getClass().getName());
	 
	   if(self instanceof SecretsAccessList) {
		   
		   SecretsAccessList s = (SecretsAccessList)self;
		   s.getSecrets().removeAll((ArrayList)value);
		
		   
		   
	   }
	   if(self instanceof MetricsConfiguration) {
		   MetricsConfiguration m = (MetricsConfiguration)self;
		   m.getHttp_pathMatching().removeAll((ArrayList)value);
	   }
	   
	   return self;
	   

	   
   }
}
