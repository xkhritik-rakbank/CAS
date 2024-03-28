
/*------------------------------------------------------------------------------------------------------

                     NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                             : Application -Projects
Project/Product                                                   : Rakbank  
Application                                                       : Credit Card
Module                                                            : CreditCard
File Name                                                         : CreditCard
Author                                                            : Disha
Date (DD/MM/YYYY)                                                 : 
Description                                                       : 
-------------------------------------------------------------------------------------------------------

CHANGE HISTORY

-------------------------------------------------------------------------------------------------------

Problem No/CR No   Change Date   Changed By    Change Description
1.				   9-6-2017		 Disha		   new java file for new worksteo CPV_Analyst has been added
------------------------------------------------------------------------------------------------------*/

package com.newgen.omniforms.user;

import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.listener.FormListener;
import com.newgen.omniforms.util.SKLogger_CC;


@SuppressWarnings("unused")
public class CreditCard implements IFormListenerFactory {
	public FormListener getListener() {
        
        FormConfig formobject=FormContext.getCurrentInstance().getFormConfig();
        String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
        String sProcessName = FormContext.getCurrentInstance().getFormConfig().getConfigElement("ProcessName");
        SKLogger_CC.writeLog("CCyash ", "Activity name is:" + sActivityName + " ProcessName name is:" + sProcessName );
		// ++ below code not commented at offshore - 06-10-2017
		String view_Mode = formobject.getConfigElement("Mode");//  
        
        if (sProcessName.equalsIgnoreCase("CreditCard")) 
        {
			// ++ below code not commented at offshore - 06-10-2017
			if(view_Mode.equalsIgnoreCase("R") )
			{
				return new CC_Query();
			}
        	if(sActivityName.equalsIgnoreCase("Initiation")){
        		 return new CC_Initiation();
        	}
	        else if(sActivityName.equalsIgnoreCase("SalesCoordinator")){
	       		 return new CC_SalesCoordinatorCSM();
	       	}
	        else if(sActivityName.equalsIgnoreCase("CSM")){
	        	SKLogger_CC.writeLog("CCyash ", "Activity name is: ProcessName name is: inside csm workstep of cc");
	       		 return new CC_CSM();
	       	}
	        else if(sActivityName.equalsIgnoreCase("DDVT_Maker")){
	        	SKLogger_CC.writeLog("CreditCard ", "Inside DDVT_Maker if condition.");
	       		 return new CC_DDVT_maker();
	       	}
	        else if(sActivityName.equalsIgnoreCase("DDVT_Checker")){
	       		 return new CC_DDVT_Checker();
	       	}
	        else if(sActivityName.equalsIgnoreCase("CPV")){
	       		 return new CC_CPV();
	       	}
	        else if(sActivityName.equalsIgnoreCase("Cad_Analyst2")){
	       		 return new CC_CAD_Analyst_2();
	       	}
	        else if(sActivityName.equalsIgnoreCase("CAD_Analyst1")){
	       		 return new CC_CAD_Analyst_1();
	       	}
	        else if(sActivityName.equalsIgnoreCase("Disbursal")){
	        	 SKLogger_CC.writeLog("CCyash ", "Activity name is: ProcessName name is: inside disbursal workstep of cc");
	       		 return new CC_Disbursal();
	       	}
	        else if(sActivityName.equalsIgnoreCase("FCU")){
	       		 return new CC_FCU();
	       	}
	        else if(sActivityName.equalsIgnoreCase("Original_Validation")){
	       		 return new CC_OV();
	       	}
	        else if(sActivityName.equalsIgnoreCase("Compliance")){
	       		 return new CC_Compliance();
	       	}
	        else if(sActivityName.equalsIgnoreCase("Rejected_queue")){
	       		 return new CC_Reject_Queue();
	       	}
	        else if(sActivityName.equalsIgnoreCase("Rejected_Application")){	       	
	        	return new CC_Reject_Application();
	       	}
	        else if(sActivityName.equalsIgnoreCase("Fulfillment_RM")){
	       		 return new CC_Fulfillment_RM();
	       	}
	        else if(sActivityName.equalsIgnoreCase("Telesales_RM")){
	       		 return new CC_Telesales_RM();
	       	}
	        else if(sActivityName.equalsIgnoreCase("Telesales_Agent")){
	       		 return new CC_Telesales_Agent();
	       	}
	        else if(sActivityName.equalsIgnoreCase("Post_Disbursal")){
	       		 return new CC_Post_Disbursal();
	       	}
	        else if(sActivityName.equalsIgnoreCase("CSM_Review")){
	       		 return new CC_CSM_Review();
	       	}
	        else if(sActivityName.equalsIgnoreCase("HR")){
	       		 return new CC_HR();
	       	}
	        else if(sActivityName.equalsIgnoreCase("RM")){
	       		 return new CC_Relationship_Manager();
	       	}
	        else if(sActivityName.equalsIgnoreCase("DSA_CSO_Review")){
	       		 return new CC_RM_CSO_Review();
	       	}
	        else if(sActivityName.equalsIgnoreCase("CardCollection")){
	       		 return new CC_CardCollection();
	       	}
	        else if(sActivityName.equalsIgnoreCase("Sales_Approver")){
	       		 return new CC_Sales_Approver();
	       	}
	        else if(sActivityName.equalsIgnoreCase("Dispatch")){
	       		 return new CC_Dispatch();
	       	}
	        else if(sActivityName.equalsIgnoreCase("Hold_CPV")){
	       		 return new CC_Hold_CPV();
	       	}
	        else if(sActivityName.equalsIgnoreCase("Smart_CPV")){
	       		 return new CC_Smart_CPV();
	       	}
        	//new java file for new worksteo CPV_Analyst has been added 
	        else if(sActivityName.equalsIgnoreCase("CPV_Analyst")){
	       		 return new CC_CPV_Analyst();
	       	}        	
        	
        	else{
        		return null;
        	}
           
        }
        
        else{
        	return null;
        }
       }
	}   
