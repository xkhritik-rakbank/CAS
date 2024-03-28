
package com.newgen.omniforms.user;

import java.util.HashMap;



import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;


public class CC_Waiver_Authority extends CC_Common implements FormListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	String queryData_load="";
	
	public void formLoaded(FormEvent pEvent)
	{
		
		CreditCard.mLogger.info( "Inside formLoaded()" + pEvent.getSource().getName());
		
	}
	

	public void formPopulated(FormEvent pEvent) 
	{
        FormReference formObject = FormContext.getCurrentInstance().getFormReference();
        try{
          
            CreditCard.mLogger.info( "Inside formPopulated()" + pEvent.getSource());
            formObject.setNGValue("WiLabel",formObject.getWFWorkitemName());
            formObject.setNGValue("QueueLabel","CC_Waiver_Authority");
            formObject.setNGValue("User_Name",formObject.getUserName()); 
            formObject.setNGValue("Introduce_date",formObject.getNGValue("CreatedDate"));
        
        }catch(Exception e)
        {
            CreditCard.mLogger.info( "Exception:"+e.getMessage());
        }
    }
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		
		CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		  formObject =FormContext.getCurrentInstance().getFormReference();

				switch(pEvent.getType())
				{	

					case FRAME_EXPANDED:
					CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
					new CC_CommonCode().FrameExpandEvent(pEvent);						

					break;
					
					case FRAGMENT_LOADED:
						CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
						fragment_loaded(pEvent,formObject);
						
						
					
					
					  break;
					  
					case MOUSE_CLICKED:
						CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
						new CC_CommonCode().FrameExpandEvent(pEvent);
						break;
						
					 case VALUE_CHANGED:
							CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
							new CC_CommonCode().value_changed(pEvent);
							break;
					default: break;
					
				}
	}


	public void continueExecution(String arg0, HashMap<String, String> arg1) {
		// TODO Auto-generated method stub
		
	}


	public void initialize() {
		// TODO Auto-generated method stub
		
	}


	public void saveFormCompleted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		
	}


	public void saveFormStarted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		
	}


	public void submitFormCompleted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		
	}


	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();		
		formObject.setNGValue("Waiver_dec", formObject.getNGValue("cmplx_DEC_Decision"));		
		saveIndecisionGrid();
	}


	public String decrypt(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}


	public String encrypt(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	private void fragment_loaded(FormEvent pEvent,FormReference formObject)
	{
		/*if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
		
		}*/
			if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
				//setDisabled();
			}	
			
			if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
				LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
			}
			
			if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				loadPicklist_Address();
			}
			//added by yash on 24/8/2017
			if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				 String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
				 CreditCard.mLogger.info( "Activity name is:" + sActivityName);
		        formObject.setNGValue("NotepadDetails_Actusername",formObject.getNGValue("cmplx_Customer_FirstNAme") +" "+ formObject.getNGValue("cmplx_Customer_MiddleNAme") +" "+ formObject.getNGValue("cmplx_Customer_LastNAme"));
		        formObject.setNGValue("NotepadDetails_user",formObject.getNGValue("cmplx_Customer_FirstNAme") +" "+ formObject.getNGValue("cmplx_Customer_MiddleNAme") +" "+ formObject.getNGValue("cmplx_Customer_LastNAme"));
		    	
				formObject.setNGValue("NotepadDetails_insqueue",sActivityName);
				formObject.setLocked("NotepadDetails_noteDate",true);
				formObject.setLocked("NotepadDetails_Actusername",true);
				formObject.setLocked("NotepadDetails_user",true);
				formObject.setLocked("NotepadDetails_insqueue",true);
				formObject.setLocked("NotepadDetails_Actdate",true);
				formObject.setVisible("NotepadDetails_SaveButton",true);
				formObject.setTop("NotepadDetails_SaveButton",400);
			}
			
			if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
            	loadPicklist1();
		 } 	
	
	}
	


}

