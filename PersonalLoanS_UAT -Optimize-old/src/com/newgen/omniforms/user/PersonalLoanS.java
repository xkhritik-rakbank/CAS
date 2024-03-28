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
		String sActivityName=formobject.getConfigElement("ActivityName");
		String sProcessName = formobject.getConfigElement("ProcessName");
		createLogFile(processName);
		mLogger.info("PersonnalLoanS>---Activity name is:" + sActivityName + " ProcessName name is:" + sProcessName);
		String view_Mode = formobject.getConfigElement("Mode");//  
		if(view_Mode.equalsIgnoreCase("R") )
		{
			return new PL_Query();
		}

		else if (sActivityName.equalsIgnoreCase("Initiation") ) 
		{
			return new PL_Initiation();
		}
		else if(sActivityName.equalsIgnoreCase("Relationship_Manager"))
		{
			return new PL_Relationship_Manager();
		}
		else if(sActivityName.equalsIgnoreCase("SalesCoordinator"))
		{
			return new PL_SalesCoordinator();
		}
		else if(sActivityName.equalsIgnoreCase("CSM"))
		{       
			return new PL_CSM();
		}
		else if(sActivityName.equalsIgnoreCase("Manager"))
		{       
			return new PL_Manager();
		}
		else if(sActivityName.equalsIgnoreCase("DSA_CSO_Review"))
		{       
			return new PL_RM_CSO_Review();
		}
		else if(sActivityName.equalsIgnoreCase("CSM_Review"))
		{       
			return new PL_CSM_Review();
		}
		else if(sActivityName.equalsIgnoreCase("RM_Review"))
		{       
			return new PL_RM_Review();
		}
		else if(sActivityName.equalsIgnoreCase("Collection_Agent_Review"))
		{       
			return new PL_Collection_Agent_Review();
		}
		else if(sActivityName.equalsIgnoreCase("Telesales_Agent_Review"))
		{       
			return new PL_Telesales_Agent_Review();
		}
		else if(sActivityName.equalsIgnoreCase("Waiver_Authority"))
		{       
			return new PL_Waiver_Authority();
		}
		else if(sActivityName.equalsIgnoreCase("Deferral_Authority"))
		{       
			return new PL_Deferral_Authority();
		}
		else if(sActivityName.equalsIgnoreCase("DDVT_maker"))
		{       
			return new PL_DDVT_maker();
		}
		else if(sActivityName.equalsIgnoreCase("DDVT_Checker"))
		{       
			return new PL_DDVT_Checker();
		}
		else if(sActivityName.equalsIgnoreCase("HR"))
		{       
			return new PL_HR();
		}
		else if(sActivityName.equalsIgnoreCase("Interest_Rate_Approval"))
		{       
			return new PL_Interest_Rate_Approval();
		}
		else if(sActivityName.equalsIgnoreCase("Original_Validation"))
		{       
			return new PL_OV();
		}
		else if(sActivityName.equalsIgnoreCase("CAD_Analyst1"))
		{       
			return new PL_CAD_Analyst_1();
		}
		else if(sActivityName.equalsIgnoreCase("CAD_Analyst2"))
		{       
			return new PL_CAD_Analyst_2();
		}
		else if(sActivityName.equalsIgnoreCase("CPV"))
		{       
			return new PL_CPV();
		}
		else if(sActivityName.equalsIgnoreCase("FCU"))
		{       
			return new PL_FCU();
		}
		else if(sActivityName.equalsIgnoreCase("Compliance"))
		{       
			return new PL_Compliance();
		}
		else if(sActivityName.equalsIgnoreCase("Post_Disbursal"))
		{       
			return new PL_Post_Disbursal();
		}
		else if(sActivityName.equalsIgnoreCase("Dispatch"))
		{       
			return new PL_Dispatch();
		}
		else if(sActivityName.equalsIgnoreCase("ToTeam"))
		{       
			return new PL_TO_Team();
		} 
		else if(sActivityName.equalsIgnoreCase("Reject_Queue"))
		{       
			return new PL_Reject_Queue();
		}
		else if(sActivityName.equalsIgnoreCase("Rejected_Application"))
		{       
			return new PL_Rejected_Application();
		}
		else if(sActivityName.equalsIgnoreCase("Disbursal"))
		{       
			return new PL_Disbursal();
		}
		else if(sActivityName.equalsIgnoreCase("Smart_CPV"))
		{       
			return new PL_SmartCPV();
		}
		else if(sActivityName.equalsIgnoreCase("CC_Waiver"))
		{       
			return new PL_CCWaiver();
		}
		else if(sActivityName.equalsIgnoreCase("Collection_User"))
		{       
			return new PL_Collection_User();
		}
		else if(sActivityName.equalsIgnoreCase("Hold_CPV"))
		{       
			return new PL_Hold_CPV();
		}
		//new java file for new worksteo CPV_Analyst and CC_Disbursal has been added
		else if(sActivityName.equalsIgnoreCase("CPV_Analyst"))
		{       
			return new PL_CPV_Analyst();
		}
		else if(sActivityName.equalsIgnoreCase("CC_Disbursal"))
		{       
			return new PL_CC_Disbursal();
		}
		else if(sActivityName.contains("Hold"))

		{
			mLogger.info("RLOS for Distribute-->inside Distribute or query");
			return new PL_Query();
		}

		else{
			return null;
		}
	}

	private void createLogFile(String processName)
	{		 
		try
		{
			Date date = new Date();
			DateFormat logDateFormat = new SimpleDateFormat("dd-MM-yyyy");
			String logFilePath=System.getProperty("user.dir") + File.separatorChar+"RLOSLogs"+File.separatorChar+logDateFormat.format(date);	
			String logFileName="PL.log";			
			String dynamicLog = logFilePath+File.separatorChar+logFileName;
			Properties p = new Properties();  
			String log4JPropertyFile = new StringBuilder().append(System.getProperty("user.dir"))
			.append(System.getProperty("file.separator")).append("CustomConfig")
			.append(System.getProperty("file.separator")).append("log4j_rakbank.properties").toString();

			p.load(new FileInputStream(log4JPropertyFile));			

			File d = new File(logFilePath);
			d.mkdirs();			

			File fl = new File(dynamicLog);

			if(fl.createNewFile()){
				mLogger.info("Log file created successfully");
			}
			else{
				mLogger.info("Error occurred while creating the log file");
			}

			p.put( "log4j.category.mLogger_"+processName.toLowerCase(), p.getProperty("log4j.category.mLogger").replaceAll("mLogger", "mLogger_"+processName.toLowerCase()));
			p.put( "log4j.additivity.mLogger_"+processName.toLowerCase(), p.getProperty("log4j.additivity.mLogger"));			
			p.put( "log4j.appender.mLogger_"+processName.toLowerCase(), p.getProperty("log4j.appender.mLogger"));
			p.put( "log4j.appender.mLogger_"+processName.toLowerCase()+".File", dynamicLog);
			p.put( "log4j.appender.mLogger_"+processName.toLowerCase()+".MaxFileSize", p.getProperty("log4j.appender.mLogger.MaxFileSize"));
			p.put( "log4j.appender.mLogger_"+processName.toLowerCase()+".MaxFileSize", p.getProperty("log4j.appender.mLogger.MaxFileSize"));
			p.put( "log4j.appender.mLogger_"+processName.toLowerCase()+".MaxBackupIndex", p.getProperty("log4j.appender.mLogger.MaxBackupIndex"));
			p.put( "log4j.appender.mLogger_"+processName.toLowerCase()+".layout", p.getProperty("log4j.appender.mLogger.layout"));
			p.put( "log4j.appender.mLogger_"+processName.toLowerCase()+".layout.ConversionPattern", p.getProperty("log4j.appender.mLogger.layout.ConversionPattern"));

			PropertyConfigurator.configure( p );			

			// CODE for deletion of log files :: To be configured later according to requirement

			/*
			int logsBackupDays=20;
			File logFileFolder = new File(System.getProperty("user.dir") + File.separatorChar+"RLOSLogs");			
			checkDirectory(logFileFolder);				
			File[] list = logFileFolder.listFiles();
			Calendar lCal = Calendar.getInstance(); // initializes with today
			lCal.add(Calendar.DATE, (-1)*logsBackupDays); // Captures a date which is 10 days before today

			for(int i=0;i<list.length;i++)
			{				
				File backupFile=new File(logFileFolder+File.separator+list[i].getName());
				Date fileModDate=new Date(backupFile.lastModified());
				if(fileModDate.before(lCal.getTime()))
				{								
					FileUtils.forceDelete(backupFile);
				}		
			}
			 */
		}
		catch(Exception e)
		{
			printException(e);			  
		}
	}


}   

