package com.newgen.omniforms.user;

import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.listener.FormListener;
import com.newgen.omniforms.skutil.SKLogger;



@SuppressWarnings("unused")
public class RLOS implements IFormListenerFactory {
	public FormListener getListener() {

		FormConfig formobject=FormContext.getCurrentInstance().getFormConfig();
		String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
        String sProcessName = FormContext.getCurrentInstance().getFormConfig().getConfigElement("ProcessName");

		
		


		String view_Mode = formobject.getConfigElement("Mode");  

		
		SKLogger.writeLog(sProcessName,"RLOS war: Activity name: "+sActivityName);
		SKLogger.writeLog(sProcessName,"RLOS war: Process name: "+sProcessName);
		SKLogger.writeLog(sProcessName,"RLOS war: view mode: "+view_Mode);
		
		if(view_Mode.equalsIgnoreCase("R") )
		{
			return new RLOS_Query();
		}
		if(sActivityName.equalsIgnoreCase("Branch_Init") || sActivityName.equalsIgnoreCase("Initiation"))
		{
			return new RLOS_Initiation();
		}


		if(sActivityName.equalsIgnoreCase("Paperbased_Init"))
		{
			return new RLOS_Paper_based_Initiation();
		}

		if(sActivityName.equalsIgnoreCase("Telesales_Init"))
		{
			return new RLOS_Telesales_Initiation();
		}

		if(sActivityName.equalsIgnoreCase("Staff_Init"))
		{
			return new RLOS_Staff_Portal();
		}

		if(sActivityName.equalsIgnoreCase("Online_Init"))
		{
			return new RLOS_OnlineAcquisitionInitiation();
		}
		if(sActivityName.equalsIgnoreCase("Operation_Maker"))
		{
			return new RLOS_Operation_Maker();
		}
		if(sActivityName.equalsIgnoreCase("Operation_checker"))
		{
			return new RLOS_Operation_checker();
		}
		if(sActivityName.equalsIgnoreCase("Mobility_Init"))
		{
			return new RLOS_Mobility();
		}
		if(sActivityName.equalsIgnoreCase("Distribute") || sActivityName.equalsIgnoreCase("Query"))

		{
			SKLogger.writeLog("RLOS for Distribute ", "inside Distribute or query");
			return new RLOS_Query();
		} 
		if(sActivityName.equalsIgnoreCase("Re_Initiate"))
		{
			return new RLOS_ReInitiate();
		}
		if(sActivityName.equalsIgnoreCase("Reject"))
		{
			return new RLOS_Reject();
		}
		else
		{
			return null;
		} 

	}

}
