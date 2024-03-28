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
        String sActivityName=formobject.getConfigElement("ActivityName");
        String sProcessName = formobject.getConfigElement("ProcessName");
        PL_SKLogger.writeLog("AKSHAY PersonnalLoanS>--- ", "Activity name is:" + sActivityName + " ProcessName name is:" + sProcessName);
        if (sActivityName.equalsIgnoreCase("Initiation") ) 
        {
            return new PL_Initiation();
        }
        if(sActivityName.equalsIgnoreCase("Relationship_Manager"))
        {
            return new PL_Relationship_Manager();
        }
        if(sActivityName.equalsIgnoreCase("Sales_coordinator"))
        {
            return new PL_Sales_coordinator();
        }
        if(sActivityName.equalsIgnoreCase("SalesCoordinatorCSM"))
        {
            return new PL_SalesCoordinatorCSM();
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
        if(sActivityName.equalsIgnoreCase("Rejected_Application"))
        {       
        	return new PL_Rejected_Application();
        }
        if(sActivityName.equalsIgnoreCase("Disbursal"))
        {       
        	return new PL_Disbursal();
        }
        
        else{
        	return null;
        }
       }
	}   

