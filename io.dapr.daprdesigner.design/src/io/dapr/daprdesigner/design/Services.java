package io.dapr.daprdesigner.design;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import daprdesigner.*;

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
}
