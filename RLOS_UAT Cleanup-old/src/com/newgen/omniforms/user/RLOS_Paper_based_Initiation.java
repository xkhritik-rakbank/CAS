package com.newgen.omniforms.user;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.excp.CustomExceptionHandler;
import com.newgen.omniforms.listener.FormListener;
import javax.faces.application.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import javax.faces.validator.ValidatorException;


public class RLOS_Paper_based_Initiation extends RLOSCommon implements FormListener
{
	public void formLoaded(FormEvent pEvent)
	{
		RLOS.mLogger.info("Inside Paperinitiation RLOS");
		RLOS.mLogger.info( "Inside formLoaded()" + pEvent.getSource().getName());
		
	}
	public void formPopulated(FormEvent pEvent) {
        FormReference formObject = FormContext.getCurrentInstance().getFormReference();
        try{
           RLOS.mLogger.info("Inside Paperinitiation RLOS");
            RLOS.mLogger.info( "Inside formPopulated()" + pEvent.getSource());
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            String currDate = format.format(Calendar.getInstance().getTime());
             RLOS.mLogger.info( "currTime:" + currDate);
           // formObject.setNGValue("Intro_Date",currDate);
            formObject.setNGValue("Intro_Date",formObject.getNGValue("Created_Date"));
            formObject.setNGValue("initiationChannel","Paperbased_Init");
            formObject.setNGValue("Channel_Name","Paper based Initiation");
            
             if(formObject.getNGValue("cmplx_Customer_FIrstNAme")==null)
            {
            	 formObject.setNGValue("cmplx_Customer_FIrstNAme","");
            }
             
             else if(formObject.getNGValue("cmplx_Customer_MiddleName")==null)
            {
            	 formObject.setNGValue("cmplx_Customer_MiddleName","");
            }
             
             else if( formObject.getNGValue("cmplx_Customer_LAstNAme")==null)
            {
            	formObject.setNGValue("cmplx_Customer_LAstNAme","");
            }
             else
                 formObject.setNGValue("Cust_Name",formObject.getNGValue("cmplx_Customer_FIrstNAme")+formObject.getNGValue("cmplx_Customer_MiddleName")+formObject.getNGValue("cmplx_Customer_LAstNAme"));

        }catch(Exception e)
        {
        	RLOS.logException(e);
            RLOS.mLogger.info( "Exception:"+e.getMessage());
        }
    }
	
	
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{
		
		try
		{
			switch (pEvent.getType()) 
			{
				case FRAME_EXPANDED:
					new RLOSCommonCode().call_Frame_Expanded(pEvent);				
					break;
					  
				case FRAGMENT_LOADED:
					new RLOS_Initiation().call_Fragement_Loaded(pEvent);				
					break;
					
				case FRAME_COLLAPSED: {
					break;
				}
				
				default:
					break;
			}
			
		}
		
		catch(Exception ex)
		{
			RLOS.logException(ex);			
		}
	}
	public void saveFormCompleted(FormEvent pEvent) {
		RLOS.mLogger.info( "Inside saveFormCompleted()" + pEvent);
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		 
		formObject.setNGValue("Created_By",formObject.getUserName());
		formObject.setNGValue("PROCESS_NAME", "RLOS");
		formObject.setNGValue("initiationChannel", "Paperbased_Init");
	}


	public void submitFormCompleted(FormEvent pEvent)
			throws ValidatorException {
		RLOS.mLogger.info( "Inside submitFormCompleted()" + pEvent);


	}

	public void continueExecution(String eventHandler, HashMap<String, String> m_mapParams) {
		RLOS.mLogger.info( "Inside continueExecution()" + eventHandler);
	}


	public void initialize() {
		RLOS.mLogger.info( "Inside initialize()");
	}
	public void saveFormStarted(FormEvent pEvent) throws ValidatorException {
		
		RLOS.mLogger.info( "Inside saveFormStarted()" + pEvent);		
	}
	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		RLOS.mLogger.info( "Inside submitFormStarted()");
		
	}
}
