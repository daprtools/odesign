package io.dapr.daprdesigner.design;

import org.eclipse.emf.ecore.EObject;

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
    
    public String getUnnamed(EObject self) {
    	unnamedCounter++;
    	return "Unnamed_"+unnamedCounter;
    }
}
