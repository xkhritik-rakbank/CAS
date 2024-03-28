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
        SKLogger.writeLog(sProcessName,"RLOS war: Workstep Name12345:"+sActivityName);
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
        else
        {
            return null;
        } 
        
    }

}
