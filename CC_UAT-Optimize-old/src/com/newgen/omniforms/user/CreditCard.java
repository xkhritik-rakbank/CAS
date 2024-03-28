
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
public class CreditCard implements IFormListenerFactory {
	
	private static String processName = "CC";
	public static final Logger mLogger = Logger.getLogger("mLogger_"+processName.toLowerCase());
	
	public FormListener getListener() {
        
        FormConfig formobject=FormContext.getCurrentInstance().getFormConfig();
        String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
        String sProcessName = FormContext.getCurrentInstance().getFormConfig().getConfigElement("ProcessName");
       				
        createLogFile(processName);
        
        mLogger.info( "Activity name is:" + sActivityName + " ProcessName name is:" + sProcessName );
       
        mLogger.info(  "Alert:::::::::::::::::@nikhil"+NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL009") );
        
        mLogger.info("CreditCard war :: Workstep:"+sActivityName);
		
		// ++ below code not commented at offshore - 06-10-2017
		String view_Mode = formobject.getConfigElement("Mode");//  
        
        if ("CreditCard".equalsIgnoreCase(sProcessName)) 
        {
			// ++ below code not commented at offshore - 06-10-2017
			if("R".equalsIgnoreCase(view_Mode) )
			{
				return new CC_Query();
			}
        	if("Initiation".equalsIgnoreCase(sActivityName)){
        		 return new CC_Initiation();
        	}
	        else if("SalesCoordinator".equalsIgnoreCase(sActivityName)){
	       		 return new CC_SalesCoordinatorCSM();
	       	}
	        else if("CSM".equalsIgnoreCase(sActivityName)){
	        	mLogger.info( "Activity name is: ProcessName name is: inside csm workstep of cc");
	       		 return new CC_CSM();
	       	}
	        else if("DDVT_Maker".equalsIgnoreCase(sActivityName)){
	       		 return new CC_DDVT_maker();
	       	}
	        else if("DDVT_Checker".equalsIgnoreCase(sActivityName)){
	       		 return new CC_DDVT_Checker();
	       	}
	        else if("CPV".equalsIgnoreCase(sActivityName)){
	       		 return new CC_CPV();
	       	}
	        else if("Cad_Analyst2".equalsIgnoreCase(sActivityName)){
	       		 return new CC_CAD_Analyst_2();
	       	}
	        else if("CAD_Analyst1".equalsIgnoreCase(sActivityName)){
	       		 return new CC_CAD_Analyst_1();
	       	}
	        else if("Disbursal".equalsIgnoreCase(sActivityName)){
	        	 mLogger.info( "Activity name is: ProcessName name is: inside disbursal workstep of cc");
	       		 return new CC_Disbursal();
	       	}
	        else if("FCU".equalsIgnoreCase(sActivityName)){
	       		 return new CC_FCU();
	       	}
	        else if("Original_Validation".equalsIgnoreCase(sActivityName)){
	       		 return new CC_OV();
	       	}
	        else if("Compliance".equalsIgnoreCase(sActivityName)){
	       		 return new CC_Compliance();
	       	}
	        else if("Rejected_queue".equalsIgnoreCase(sActivityName)){
	       		 return new CC_Reject_Queue();
	       	}
	        else if("Rejected_Application".equalsIgnoreCase(sActivityName)){	       	
	        	return new CC_Reject_Application();
	       	}
	        else if("Fulfillment_RM".equalsIgnoreCase(sActivityName)){
	       		 return new CC_Fulfillment_RM();
	       	}
	        else if("Telesales_RM".equalsIgnoreCase(sActivityName)){
	       		 return new CC_Telesales_RM();
	       	}
	        else if("Telesales_Agent".equalsIgnoreCase(sActivityName)){
	       		 return new CC_Telesales_Agent();
	       	}
	        else if("Post_Disbursal".equalsIgnoreCase(sActivityName)){
	       		 return new CC_Post_Disbursal();
	       	}
	        else if("CSM_Review".equalsIgnoreCase(sActivityName)){
	       		 return new CC_CSM_Review();
	       	}
	        else if("HR".equalsIgnoreCase(sActivityName)){
	       		 return new CC_HR();
	       	}
	        else if("RM".equalsIgnoreCase(sActivityName)){
	       		 return new CC_Relationship_Manager();
	       	}
	        else if("DSA_CSO_Review".equalsIgnoreCase(sActivityName)){
	       		 return new CC_RM_CSO_Review();
	       	}
	        else if("CardCollection".equalsIgnoreCase(sActivityName)){
	       		 return new CC_CardCollection();
	       	}
	        else if("Sales_Approver".equalsIgnoreCase(sActivityName)){
	       		 return new CC_Sales_Approver();
	       	}
	        else if("Dispatch".equalsIgnoreCase(sActivityName)){
	       		 return new CC_Dispatch();
	       	}
	        else if("Hold_CPV".equalsIgnoreCase(sActivityName)){
	       		 return new CC_Hold_CPV();
	       	}
	        else if("Smart_CPV".equalsIgnoreCase(sActivityName)){
	       		 return new CC_Smart_CPV();
	       	}
        	//new java file for new worksteo CPV_Analyst has been added 
	        else if("CPV_Analyst".equalsIgnoreCase(sActivityName)){
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
	   	private void createLogFile(String processName)
	{		
	   		boolean file_status;
		try
		{
			Date date = new Date();
			DateFormat logDateFormat = new SimpleDateFormat("dd-MM-yyyy");
			String logFilePath=System.getProperty("user.dir") + File.separatorChar+"RLOSLogs"+File.separatorChar+logDateFormat.format(date);	
			String logFileName="CC.log";			
			String dynamicLog = logFilePath+File.separatorChar+logFileName;
			Properties p = new Properties();  
			String log4JPropertyFile = new StringBuilder().append(System.getProperty("user.dir"))
					.append(System.getProperty("file.separator")).append("CustomConfig")
					.append(System.getProperty("file.separator")).append("log4j_rakbank.properties").toString();
			
			p.load(new FileInputStream(log4JPropertyFile));			
			
			File d = new File(logFilePath);
			d.mkdirs();			
			 
			File fl = new File(dynamicLog);
			if(!fl.exists())
			{				
				if(fl.createNewFile())
					mLogger.info("Log File created succesfully ");
				else
					mLogger.info("Error in Log File creation");
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
		  final Writer result = new StringWriter();
		  final PrintWriter printWriter = new PrintWriter(result);
		  e.printStackTrace(printWriter);
		  mLogger.info("Inside exception :"+"\n"+e+" : \n"+result.toString());			  
		}
	}
	private void checkDirectory(File dir) 
	{
        if(dir!=null && !dir.isDirectory())
        {
        	mLogger.debug(dir+" Directory Not Found. Trying to create");
        	if(dir.mkdirs())
        	{
        		mLogger.debug(dir+" Directory .Created Successfully");
        	}
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
