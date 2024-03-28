/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PersonalLoanS.java
Author                                                                        : Disha
Date (DD/MM/YYYY)                                      						  : 
Description                                                                   : 

------------------------------------------------------------------------------------------------------------------------------------------------------
CHANGE HISTORY 
------------------------------------------------------------------------------------------------------------------------------------------------------

Problem No/CR No   Change Date   Changed By    Change Description
1.				   9.6.2017      Disha			new java file for new worksteo CPV_Analyst and CC_Disbursal has been added
------------------------------------------------------------------------------------------------------*/

package com.newgen.omniforms.user;

import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.listener.FormListener;
import com.newgen.omniforms.util.PL_SKLogger;









@SuppressWarnings("unused")
public class PersonalLoanS implements IFormListenerFactory {
	public FormListener getListener() {
        
        FormConfig formobject=FormContext.getCurrentInstance().getFormConfig();
        //FormReference formObject = FormContext.getCurrentInstance().getFormReference();
        
        String sActivityName=formobject.getConfigElement("ActivityName");
        String sProcessName = formobject.getConfigElement("ProcessName");
        PL_SKLogger.writeLog("AKSHAY PersonnalLoanS>--- ", "Activity name is:" + sActivityName + " ProcessName name is:" + sProcessName);
        //formObject.setNGValue("workitem_name",formObject.getWFWorkitemName());
        String view_Mode = formobject.getConfigElement("Mode");//  
        if(view_Mode.equalsIgnoreCase("R") )
        {
        	return new PL_Query();
        }
        
        if (sActivityName.equalsIgnoreCase("Initiation") ) 
        {
            return new PL_Initiation();
        }
        if(sActivityName.equalsIgnoreCase("Relationship_Manager"))
        {
            return new PL_Relationship_Manager();
        }
        if(sActivityName.equalsIgnoreCase("SalesCoordinator"))
        {
            return new PL_SalesCoordinator();
        }
        if(sActivityName.equalsIgnoreCase("CSM"))
        {       
        	return new PL_CSM();
        }
        if(sActivityName.equalsIgnoreCase("Manager"))
        {       
        	return new PL_Manager();
        }
        if(sActivityName.equalsIgnoreCase("DSA_CSO_Review"))
        {       
        	return new PL_RM_CSO_Review();
        }
        if(sActivityName.equalsIgnoreCase("CSM_Review"))
        {       
        	return new PL_CSM_Review();
        }
        if(sActivityName.equalsIgnoreCase("RM_Review"))
        {       
        	return new PL_RM_Review();
        }
        if(sActivityName.equalsIgnoreCase("Collection_Agent_Review"))
        {       
        	return new PL_Collection_Agent_Review();
        }
        if(sActivityName.equalsIgnoreCase("Telesales_Agent_Review"))
        {       
        	return new PL_Telesales_Agent_Review();
        }
        if(sActivityName.equalsIgnoreCase("Waiver_Authority"))
        {       
        	return new PL_Waiver_Authority();
        }
        if(sActivityName.equalsIgnoreCase("Deferral_Authority"))
        {       
        	return new PL_Deferral_Authority();
        }
        if(sActivityName.equalsIgnoreCase("DDVT_maker"))
        {       
        	return new PL_DDVT_maker();
        }
        if(sActivityName.equalsIgnoreCase("DDVT_Checker"))
        {       
        	return new PL_DDVT_Checker();
        }
        if(sActivityName.equalsIgnoreCase("HR"))
        {       
        	return new PL_HR();
        }
        if(sActivityName.equalsIgnoreCase("Interest_Rate_Approval"))
        {       
        	return new PL_Interest_Rate_Approval();
        }
        if(sActivityName.equalsIgnoreCase("Original_Validation"))
        {       
        	return new PL_OV();
        }
        if(sActivityName.equalsIgnoreCase("CAD_Analyst1"))
        {       
        	return new PL_CAD_Analyst_1();
        }
        if(sActivityName.equalsIgnoreCase("CAD_Analyst2"))
        {       
        	return new PL_CAD_Analyst_2();
        }
        if(sActivityName.equalsIgnoreCase("CPV"))
        {       
        	return new PL_CPV();
        }
        if(sActivityName.equalsIgnoreCase("FCU"))
        {       
        	return new PL_FCU();
        }
        if(sActivityName.equalsIgnoreCase("Compliance"))
        {       
        	return new PL_Compliance();
        }
        if(sActivityName.equalsIgnoreCase("Post_Disbursal"))
        {       
        	return new PL_Post_Disbursal();
        }
        if(sActivityName.equalsIgnoreCase("Dispatch"))
        {       
        	return new PL_Dispatch();
        }
        if(sActivityName.equalsIgnoreCase("ToTeam"))
        {       
        	return new PL_TO_Team();
        } 
        if(sActivityName.equalsIgnoreCase("Reject_Queue"))
        {       
        	return new PL_Reject_Queue();
        }
        if(sActivityName.equalsIgnoreCase("Rejected_Application"))
        {       
        	return new PL_Rejected_Application();
        }
        if(sActivityName.equalsIgnoreCase("Disbursal"))
        {       
        	return new PL_Disbursal();
        }
        if(sActivityName.equalsIgnoreCase("Smart_CPV"))
        {       
        	return new PL_SmartCPV();
        }
        if(sActivityName.equalsIgnoreCase("CC_Waiver"))
        {       
        	return new PL_CCWaiver();
        }
        if(sActivityName.equalsIgnoreCase("Collection_User"))
        {       
        	return new PL_Collection_User();
        }
        if(sActivityName.equalsIgnoreCase("Hold_CPV"))
        {       
        	return new PL_Hold_CPV();
        }
      //new java file for new worksteo CPV_Analyst and CC_Disbursal has been added
        if(sActivityName.equalsIgnoreCase("CPV_Analyst"))
        {       
        	return new PL_CPV_Analyst();
        }
        if(sActivityName.equalsIgnoreCase("CC_Disbursal"))
        {       
        	return new PL_CC_Disbursal();
        }
        
        if(sActivityName.contains("Hold"))
        	
        {
        	 PL_SKLogger.writeLog("RLOS for Distribute ", "inside Distribute or query");
            return new PL_Query();
        }
        
        else{
        	return null;
        }
       }
	}   

