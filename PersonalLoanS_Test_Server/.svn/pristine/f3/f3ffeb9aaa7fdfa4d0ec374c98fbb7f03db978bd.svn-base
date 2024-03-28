package com.newgen.omniforms.user;

import java.util.HashMap;

import javax.faces.validator.ValidatorException;

import org.apache.log4j.Logger;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;

public class PL_Query extends PLCommon implements FormListener{

	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	Logger mLogger=PersonalLoanS.mLogger;
	public void formLoaded(FormEvent pEvent)
	{
		mLogger.info("Inside PL_Query-->FormLOaded()");
	}


	public void formPopulated(FormEvent pEvent) 
	{
		//empty method
		
	}
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Load fragment      
	 
	***********************************************************************************  */

	public void fragment_loaded(ComponentEvent pEvent)
	{
		mLogger.info(" In PL_Initiation eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();	

		if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Customer_Frame1", true);

		}	

		else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Product_Frame1", true);
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
			loadPicklistProduct("Personal Loan");
		}

		else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("IncomeDetails_Frame1", true);							
		}

		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ExtLiability_Frame1", true);
		}

		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("EMploymentDetails_Frame1",true);

		}

		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
			
		}

		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("AddressDetails_Frame1", true);
		}

		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())){

			formObject.setLocked("AltContactDetails_Frame1",true);
			
		} 

		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())){

			formObject.setLocked("FATCA_Frame6", true);
		}

		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())){

			formObject.setLocked("KYC_Frame1",true);
			
		}

		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())){

			formObject.setLocked("OECD_Frame8",true);
			
		}

		else if ("IncomingDoc".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setLocked("IncomingDoc_Frame1", true);
		}

		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("DecisionHistory_Frame1", true);
			formObject.setVisible("DecisionHistory_chqbook",false);
			
		} 	
		

	}
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException {

		switch(pEvent.getType()) {

		case FRAME_EXPANDED:        	  
			new PersonalLoanSCommonCode().FrameExpandEvent(pEvent);          	
			break;

		case FRAGMENT_LOADED:
			fragment_loaded(pEvent);
			break;

		default: break;

		}

	}	


	public void initialize() {	//empty method
	}


	public void saveFormCompleted(FormEvent pEvent) throws ValidatorException {
	//empty method	
	}


	public void saveFormStarted(FormEvent pEvent) throws ValidatorException {
	//empty method	
	}


	public void submitFormCompleted(FormEvent pEvent) throws ValidatorException {
	//empty method	
	}


	public void submitFormStarted(FormEvent pEvent) throws ValidatorException {
	//empty method	
	}


	public void continueExecution(String arg0, HashMap<String, String> arg1) {
		//empty method

	}



}
