
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

import com.newgen.custom.Common_Utils;
import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.listener.FormListener;


import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


@SuppressWarnings("unused")
public class DigitalOnBoarding implements IFormListenerFactory {

	private static String processName = "CDOB";
	public static final Logger mLogger = Logger.getLogger("mLogger_"+processName.toLowerCase());

	public FormListener getListener() {
		Common_Utils Common=new Common_Utils(mLogger);
		FormConfig formobject=FormContext.getCurrentInstance().getFormConfig();
		String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
		String sProcessName = FormContext.getCurrentInstance().getFormConfig().getConfigElement("ProcessName");

		Common.createLogFile(processName, "DigitalOnBoarding");

		mLogger.info( "Activity name is @aman:" + sActivityName + " ProcessName name is:" + sProcessName );

		mLogger.info(  "Alert:::::::::::::::::@nikhil"+NGFUserResourceMgr_DigitalOnBoarding.getAlert("VAL009") );

		mLogger.info("CreditCard war :: Workstep:"+sActivityName);

		// ++ below code not commented at offshore - 06-10-2017
		String view_Mode = formobject.getConfigElement("Mode");// 
		mLogger.info( "@Sajan Activity Name is "+sActivityName+" and process Name is "+sProcessName );
		if ("DigitalOnBoarding".equalsIgnoreCase(sProcessName)) 
		{
			// ++ below code not commented at offshore - 06-10-2017
			if("R".equalsIgnoreCase(view_Mode) )
			{
				return new CDOB_Query();
			}
			if("Initiation".equalsIgnoreCase(sActivityName)){
				return new CDOB_Initiation();
			}
			else if("SalesCoordinator".equalsIgnoreCase(sActivityName)){
				// return new CC_SalesCoordinatorCSM();
				//return new CC_Sales_coordinator();
				return new CDOB_CSM();
			}
			else if(NGFUserResourceMgr_DigitalOnBoarding.getGlobalVar("CC_CSM").equalsIgnoreCase(sActivityName)){
				mLogger.info( "Activity name is: ProcessName name is: inside csm workstep of cc");
				return new CDOB_CSM();
			}
			else if("DDVT_Maker".equalsIgnoreCase(sActivityName)){
				return new CDOB_DDVT_maker();
			}
			else if("DDVT_Checker".equalsIgnoreCase(sActivityName)){
				return new CDOB_DDVT_Checker();
			}
			else if("CPV".equalsIgnoreCase(sActivityName)){
				return new CDOB_CPV();
			}
			else if("Cad_Analyst2".equalsIgnoreCase(sActivityName)){
				return new CDOB_CAD_Analyst_2();
			}
			/*else if("RM_Review".equalsIgnoreCase(sActivityName)){
				
				return new CC_RM_Review();
			}*/
			else if("CAD_Analyst1".equalsIgnoreCase(sActivityName)){
				mLogger.info( "AmanAMan AMan name is: ProcessName name is: inside disbursal workstep of cc");
				return new CDOB_CAD_Analyst_1();
			}
			else if("Disbursal".equalsIgnoreCase(sActivityName)){
				mLogger.info( "Activity name is: ProcessName name is: inside disbursal workstep of cc");
				return new CDOB_Disbursal();
			}
			else if("FPU".equalsIgnoreCase(sActivityName)){
				return new CDOB_FPU();
			}
			else if("OV".equalsIgnoreCase(sActivityName)){//Original_Validation changes to OV by saurabh1
				return new CDOB_OV();
			}
			else if("Compliance".equalsIgnoreCase(sActivityName)){
				DigitalOnBoarding.mLogger.info("Inside Compliance call from Creditcard");
				return new CDOB_Compliance();
			}
			else if("Rejected_queue".equalsIgnoreCase(sActivityName)){
				return new CDOB_Reject_Queue();
			}
			else if("Rejected_Application".equalsIgnoreCase(sActivityName)){	       	
				return new CDOB_Reject_Application();
			}
			else if("Fulfillment_RM".equalsIgnoreCase(sActivityName)){
				return new CDOB_Fulfillment_RM();
			}
			else if("Telesales_RM".equalsIgnoreCase(sActivityName)){
				return new CDOB_Telesales_RM();
			}
			else if("Telesales_Agent".equalsIgnoreCase(sActivityName)){
				return new CDOB_Telesales_Agent();
			}
			else if("Post_Disbursal".equalsIgnoreCase(sActivityName)){
				return new CDOB_Post_Disbursal();
			}
			else if("CSM_Review".equalsIgnoreCase(sActivityName)){
				return new CDOB_CSM_Review();
			}
			else if("HR".equalsIgnoreCase(sActivityName)){
				return new CDOB_HR();
			}
			else if("RM".equalsIgnoreCase(sActivityName)){
				return new CDOB_Relationship_Manager();
			}
			else if("DSA_CSO_Review".equalsIgnoreCase(sActivityName) || "RM_Review".equalsIgnoreCase(sActivityName)){
				mLogger.info("Inside Credit card.java for RM_Review"+sActivityName);
				return new CDOB_RM_CSO_Review();
			}
			else if("CardCollection".equalsIgnoreCase(sActivityName)){
				return new CDOB_CardCollection();
			}
			else if("Sales_Approver".equalsIgnoreCase(sActivityName)){
				return new CDOB_Sales_Approver();
			}
			else if("Dispatch".equalsIgnoreCase(sActivityName)){
				return new CDOB_Dispatch();
			}
			else if("Hold_CPV".equalsIgnoreCase(sActivityName)){
				return new CDOB_Hold_CPV();
			}
			else if("Smart_CPV".equalsIgnoreCase(sActivityName)){
				return new CDOB_Smart_CPV();
			}
			else if("CAD1_Hold".equalsIgnoreCase(sActivityName)){
				return new CDOB_CAD1_Hold();
			}
			//new java file for new worksteo CPV_Analyst has been added 
			else if("CPV_Analyst".equalsIgnoreCase(sActivityName)){
				return new CDOB_CPV_Analyst();
			}   
			//added 11th dec 2017
			else if("CustomerHold".equalsIgnoreCase(sActivityName)){
				return new CDOB_Customer_Hold();
			}
			//added 11th dec 2017

			else{
				return null;
			}

		}

		else{
			return null;
		}
	}

	public static void logException(Exception excp)
	{
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		excp.printStackTrace(printWriter);
		mLogger.info("Inside exception :"+"\n"+excp+" : \n"+result.toString());
	}

}   
