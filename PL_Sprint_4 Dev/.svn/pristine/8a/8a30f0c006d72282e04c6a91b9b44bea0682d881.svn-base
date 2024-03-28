/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PL_Telesales_Agent_Review.java
Author                                                                        : Disha
Date (DD/MM/YYYY)                                      						  : 
Description                                                                   : 

------------------------------------------------------------------------------------------------------------------------------------------------------
CHANGE HISTORY 
------------------------------------------------------------------------------------------------------------------------------------------------------

Problem No/CR No   Change Date   Changed By    Change Description
1.                 12-6-2017     Disha	       Common function for decision fragment textboxes and combo visibility
------------------------------------------------------------------------------------------------------*/
package com.newgen.omniforms.user;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.validator.ValidatorException;

import org.apache.log4j.Logger;

import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;


public class PL_Telesales_Agent_Review extends PLCommon implements FormListener
{
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	Logger mLogger=PersonalLoanS.mLogger;
	public void formLoaded(FormEvent pEvent)
	{
		FormConfig objConfig = FormContext.getCurrentInstance().getFormConfig();
        objConfig.getM_objConfigMap().put("PartialSave", "true");
        makeSheetsInvisible("Tab1", "8,9");//Changes done by shweta for jira #1768 to hide CPV tab

	}


	public void formPopulated(FormEvent pEvent) 
	{
		try{
			new PersonalLoanSCommonCode().setFormHeader(pEvent);
		}catch(Exception e)
		{
			mLogger.info("PL Telesales "+ "Exception:"+e.getMessage());
			printException(e);
		}
	}
	
	

	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{
		//mLogger.info("Inside PL_Initiation eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

		switch(pEvent.getType())
		{	

		case FRAME_EXPANDED:
			new PersonalLoanSCommonCode().FrameExpandEvent(pEvent);					
			break;

		case FRAGMENT_LOADED:
			new PersonalLoanSCommonCode().LockFragmentsOnLoad(pEvent);
			break;

		case MOUSE_CLICKED:
			new PersonalLoanSCommonCode().mouse_Clicked(pEvent);
			break;

		case VALUE_CHANGED:
			new PersonalLoanSCommonCode().value_Change(pEvent);
			break;

		default: break;

		}
	}


	public void continueExecution(String arg0, HashMap<String, String> arg1) {
			//empty method
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();	
		formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));

	}


	public void initialize() {
			//empty method

	}


	public void saveFormCompleted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub

	}


	public void saveFormStarted(FormEvent arg0) throws ValidatorException {
			//empty method

	}


	public void submitFormCompleted(FormEvent arg0) throws ValidatorException {
		try{//code sync
			PersonalLoanS.mLogger.info( "Inside PL PROCESS submitFormCompleted()" + arg0.getSource());
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			List<String> objInput=new ArrayList<String> ();
			objInput.add("Text:"+formObject.getWFWorkitemName());
			objInput.add("Text:Source");
			PersonalLoanS.mLogger.info("objInput args are: "+objInput.get(0)+objInput.get(1));
			List<Object> objOutput=new ArrayList<Object>();

			objOutput.add("Text");
			objOutput= formObject.getDataFromStoredProcedure("ng_RLOS_MultipleRefer", objInput,objOutput);
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("Exception occured in submitFormCompleted"+e.getStackTrace());
		}

	}


	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		PersonalLoanS.mLogger.info( "Inside PL PROCESS submitFormStarted()" + arg0.getSource());
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();	
		formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));

		String decisionOnForm = formObject.getNGValue("cmplx_Decision_Decision");
		if(decisionOnForm!=null && !decisionOnForm.equalsIgnoreCase("") && decisionOnForm.equalsIgnoreCase("Submit") && formObject.getNGValue("InitiationType").equalsIgnoreCase("Telesales_Init")){
			formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision")+"-Information updated");
			formObject.setNGValue("Next_Workstep", "");
		}

		if(formObject.getNGValue("Interest_Rate_App_dec").equalsIgnoreCase("Reject"))
		{
			formObject.setNGValue("Interest_Rate_App_dec", "");
		}
		if(formObject.getNGValue("IS_SALES_HEAD_DEC").equalsIgnoreCase("Reject"))
		{
			formObject.setNGValue("IS_SALES_HEAD_DEC", "");
		}

		formObject.setNGValue("Next_Workstep", "");
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

}

