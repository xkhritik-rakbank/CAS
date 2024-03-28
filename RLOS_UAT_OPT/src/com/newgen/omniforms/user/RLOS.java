//----------------------------------------------------------------------------------------------------
//		NEWGEN SOFTWARE TECHNOLOGIES LIMITED
//Group						: AP2
//Product / Project			: RLOS
//Module					: CAS
//File Name					: RLOS.java
//Author					: Deepak Kumar
//Date written (DD/MM/YYYY)	: 06/08/2017
//Description				: 
//----------------------------------------------------------------------------------------------------

package com.newgen.omniforms.user;

import java.io.*;
import java.text.*;
import java.util.*;


import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.newgen.custom.Common_Utils;
import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.listener.FormListener;



@SuppressWarnings("unused")
public class RLOS implements IFormListenerFactory 
{
	private static String processName = "RLOS";
	public static final Logger mLogger = Logger.getLogger("mLogger_"+processName.toLowerCase());
	
	public FormListener getListener() 
	{        
        FormConfig formobject=FormContext.getCurrentInstance().getFormConfig();
        String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
         Common_Utils common=new Common_Utils(mLogger);     			
         common.createLogFile(processName,"cas");        
        mLogger.info("RLOS war123 :::: Workstep:"+sActivityName);      
        
        String view_Mode = formobject.getConfigElement("Mode");  
        if("R".equalsIgnoreCase(view_Mode) )
        {
        	return new RLOS_Query();
        }
        else if("Branch_Init".equalsIgnoreCase(sActivityName) || "Initiation".equalsIgnoreCase(sActivityName))
        {
            return new RLOS_Initiation();
        }
        else if("Paperbased_Init".equalsIgnoreCase(sActivityName))
        {
            return new RLOS_Paper_based_Initiation();
        }
        
        else if("Telesales_Init".equalsIgnoreCase(sActivityName))
        {
            return new RLOS_Telesales_Initiation();
        }
        
        else if("Staff_Init".equalsIgnoreCase(sActivityName))
        {
            return new RLOS_Staff_Portal();
        }
             
        else if("Online_Init".equalsIgnoreCase(sActivityName))
        {
            return new RLOS_OnlineAcquisitionInitiation();
        }
        else  if("Operation_Maker".equalsIgnoreCase(sActivityName))
        {
            return new RLOS_Operation_Maker();
        }
        else if("Operation_checker".equalsIgnoreCase(sActivityName))
        {
            return new RLOS_Operation_checker();
        }
       /* if("Mobility_Init".equalsIgnoreCase(sActivityName))
        {
            return new RLOS_Mobility();
        }*/
        else if("Re_Initiate".equalsIgnoreCase(sActivityName))
        {
            return new RLOS_ReInitiate();
        }
        else  if("Reject".equalsIgnoreCase(sActivityName))
        {
            return new RLOS_Reject();
        } 
      //added 11th dec 2017
        else if("CustomerHold".equalsIgnoreCase(sActivityName)){
        	return new RLOS_Customer_Hold();
        }
    	//added 11th dec 2017
        else
        {
            return null;
        } 
        
    }
	
/*	private void createLogFile(String processName)
	{		 
		try
		{
			Date date = new Date();
			DateFormat logDateFormat = new SimpleDateFormat("dd-MM-yyyy");
			String logFilePath=System.getProperty("user.dir") + File.separatorChar+"RLOSLogs"+File.separatorChar+logDateFormat.format(date);	
			String logFileName="ngrlos.log";			
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
					mLogger.info("Log file crested successfully");
				else
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
			
		}
		catch(Exception e)
		{
		  logException(e);			  
		}
	}
	
	public void checkDirectory(File dir) 
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
	*/
	public static void logException(Exception excp)
	{
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		//excp.printStackTrace(printWriter);
		mLogger.info("Inside exception :"+"\n"+excp+" : \n"+result.toString());	
		
	}

}