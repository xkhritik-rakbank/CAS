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

import java.io.File;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import com.newgen.custom.Common_Utils;
import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.listener.FormListener;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;



@SuppressWarnings("unused")
public class PersonalLoanS extends PLCommon implements IFormListenerFactory {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String processName = "PersonalLoan";
	public static final Logger mLogger = Logger.getLogger("mLogger_"+processName.toLowerCase());
	
	public FormListener getListener() {
		FormConfig formobject=FormContext.getCurrentInstance().getFormConfig();
		Common_Utils c=new Common_Utils(mLogger);
		String sActivityName=formobject.getConfigElement("ActivityName");
		String sProcessName = formobject.getConfigElement("ProcessName");
	    c.createLogFile(processName,"PL");
		mLogger.info("PersonnalLoanS>---Activity name is:" + sActivityName + " ProcessName name is:" + sProcessName);
		String view_Mode = formobject.getConfigElement("Mode");//  
		if("R".equalsIgnoreCase(view_Mode) )
		{
			return new PL_Query();
		}

		else if ("Initiation".equalsIgnoreCase(sActivityName) ) 
		{
			return new PL_Initiation();
		}
		else if("Relationship_Manager".equalsIgnoreCase(sActivityName))
		{
			return new PL_Relationship_Manager();
		}
		else if("SalesCoordinator".equalsIgnoreCase(sActivityName))
		{
			return new PL_SalesCoordinator();
		}
		else if("CSM".equalsIgnoreCase(sActivityName))
		{       
			return new PL_CSM();
		}
		else if("Manager".equalsIgnoreCase(sActivityName))
		{       
			return new PL_Manager();
		}
		else if("DSA_CSO_Review".equalsIgnoreCase(sActivityName))
		{       
			return new PL_RM_CSO_Review();
		}
		else if("CSM_Review".equalsIgnoreCase(sActivityName))
		{       
			return new PL_CSM_Review();
		}
		else if("RM_Review".equalsIgnoreCase(sActivityName))
		{       
			return new PL_RM_Review();
		}
		else if("Collection_Agent_Review".equalsIgnoreCase(sActivityName))
		{       
			return new PL_Collection_Agent_Review();
		}
		else if("Telesales_Agent_Review".equalsIgnoreCase(sActivityName))
		{       
			return new PL_Telesales_Agent_Review();
		}
		else if("Waiver_Authority".equalsIgnoreCase(sActivityName))
		{       
			return new PL_Waiver_Authority();
		}
		else if("Deferral_Authority".equalsIgnoreCase(sActivityName))
		{       
			return new PL_Deferral_Authority();
		}
		else if("DDVT_maker".equalsIgnoreCase(sActivityName))
		{       
			return new PL_DDVT_maker();
		}
		else if("DDVT_Checker".equalsIgnoreCase(sActivityName))
		{       
			return new PL_DDVT_Checker();
		}
		else if("HR".equalsIgnoreCase(sActivityName))
		{       
			return new PL_HR();
		}
		else if("Interest_Rate_Approval".equalsIgnoreCase(sActivityName))
		{       
			return new PL_Interest_Rate_Approval();
		}
		else if("Original_Validation".equalsIgnoreCase(sActivityName))
		{       
			return new PL_OV();
		}
		else if("CAD_Analyst1".equalsIgnoreCase(sActivityName))
		{       
			return new PL_CAD_Analyst_1();
		}
		else if("CAD_Analyst2".equalsIgnoreCase(sActivityName))
		{       
			return new PL_CAD_Analyst_2();
		}
		else if("CPV".equalsIgnoreCase(sActivityName))
		{       
			return new PL_CPV();
		}
		else if("FCU".equalsIgnoreCase(sActivityName))
		{       
			return new PL_FCU();
		}
		else if("Compliance".equalsIgnoreCase(sActivityName))
		{       
			return new PL_Compliance();
		}
		else if("Post_Disbursal".equalsIgnoreCase(sActivityName))
		{       
			return new PL_Post_Disbursal();
		}
		else if("Dispatch".equalsIgnoreCase(sActivityName))
		{       
			return new PL_Dispatch();
		}
		else if("ToTeam".equalsIgnoreCase(sActivityName))
		{       
			return new PL_TO_Team();
		} 
		else if("Reject_Queue".equalsIgnoreCase(sActivityName))
		{       
			return new PL_Reject_Queue();
		}
		else if("Rejected_Application".equalsIgnoreCase(sActivityName))
		{       
			return new PL_Rejected_Application();
		}
		else if("Disbursal".equalsIgnoreCase(sActivityName))
		{       
			return new PL_Disbursal();
		}
		else if("Smart_CPV".equalsIgnoreCase(sActivityName))
		{       
			return new PL_SmartCPV();
		}
		else if("CC_Waiver".equalsIgnoreCase(sActivityName))
		{       
			return new PL_CCWaiver();
		}
		else if("Collection_User".equalsIgnoreCase(sActivityName))
		{       
			return new PL_Collection_User();
		}
		else if("Hold_CPV".equalsIgnoreCase(sActivityName))
		{       
			return new PL_Hold_CPV();
		}
		//new java file for new worksteo CPV_Analyst and CC_Disbursal has been added
		else if("CPV_Analyst".equalsIgnoreCase(sActivityName))
		{       
			return new PL_CPV_Analyst();
		}
		else if("CC_Disbursal".equalsIgnoreCase(sActivityName))
		{       
			return new PL_CC_Disbursal();
		}
		else if(sActivityName.contains("Hold"))

		{
			mLogger.info("RLOS for Distribute-->inside Distribute or query");
			return new PL_Query();
		}
		//added 11th dec 2017
        /*else if("CustomerHold".equalsIgnoreCase(sActivityName)){
        	return new PL_Customer_Hold();
        }*/
    	//added 11th dec 2017

		else{
			return null;
		}
	}

	


}   

