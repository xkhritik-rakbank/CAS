/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PL_CSM_Review.java
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


import java.util.HashMap;

import javax.faces.validator.ValidatorException;

import org.apache.log4j.Logger;

import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;


public class PL_CSM_Review extends PLCommon implements FormListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	Logger mLogger=PersonalLoanS.mLogger;

	public void formLoaded(FormEvent pEvent)
	{
		FormConfig objConfig = FormContext.getCurrentInstance().getFormConfig();
        objConfig.getM_objConfigMap().put("PartialSave", "true");
        makeSheetsInvisible("Tab1", "8,9,16");//Changes done by shweta for jira #1768 to hide CPV tab//Hide Dispatch Tab
        FormReference formObject = FormContext.getCurrentInstance().getFormReference();
        formObject.setSheetVisible(tabName, 4, true);	//Demographics Tab
        formObject.setSheetVisible(tabName, 6, true);	//Services Request Tab
	}


	public void formPopulated(FormEvent pEvent) 
	{
		try{
			new PersonalLoanSCommonCode().setFormHeader(pEvent);

			//PCASI - 2694
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String sRMNameHeader = formObject.getNGValue("RM_Name");
			String cmplxRMName = formObject.getNGValue("cmplx_Customer_RM_TL_NAME");
			if("".equalsIgnoreCase(cmplxRMName) || cmplxRMName == null){
				formObject.setNGValue("cmplx_Customer_RM_TL_NAME", sRMNameHeader);
			}
			PersonalLoanS.mLogger.info("sRMNameHeader :: "+sRMNameHeader+" cmplxRMName :: "+cmplxRMName);
		}catch(Exception e)
		{
			PersonalLoanS.mLogger.info("Exception:"+e.getMessage());
			printException(e);
		}
	}
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Load fragment      
	 
	***********************************************************************************  */


	


	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{

		//PersonalLoanS.mLogger.info("Inside PL_Initiation eventDispatched()-->EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

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
	}


	public void initialize() {
		
		//empty method
	}


	public void saveFormCompleted(FormEvent arg0) throws ValidatorException {
		CustomSaveForm();
		//empty method
	}


	public void saveFormStarted(FormEvent arg0) throws ValidatorException {
		//empty method

	}


	public void submitFormCompleted(FormEvent arg0) throws ValidatorException {
		//empty method

	}


	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//temporary changes by saurabh on 22nd april.
		try{
			
		String decisionOnForm = formObject.getNGValue("cmplx_Decision_Decision");
		formObject.setNGValue("Next_Workstep", "");
		if(decisionOnForm!=null && !decisionOnForm.equalsIgnoreCase("") && decisionOnForm.equalsIgnoreCase("Submit") && formObject.getNGValue("InitiationType").equalsIgnoreCase("Telesales_Init")){
		formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision")+"-Information updated");
		formObject.setNGValue("Next_Workstep", "");
		}
		
		

		else{
			formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
		}
		}catch(Exception ex){
			formObject.setNGValue("decision", "Submit-Information updated");
		}
		finally{
		saveIndecisionGrid();
		}
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

