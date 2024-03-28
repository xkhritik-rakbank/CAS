/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PL_RM_Review.java
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


public class PL_RM_Review extends PLCommon implements FormListener
{
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	Logger mLogger=PersonalLoanS.mLogger;
	public void formLoaded(FormEvent pEvent)
	{
		FormConfig objConfig = FormContext.getCurrentInstance().getFormConfig();
        objConfig.getM_objConfigMap().put("PartialSave", "true");

	}


	public void formPopulated(FormEvent pEvent) 
	{
		try{
			new PersonalLoanSCommonCode().setFormHeader(pEvent);
		}catch(Exception e)
		{
			mLogger.info("PL_RM_Review"+ "Exception:"+e.getMessage());
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

		//mLogger.info("Inside PL_Initiation eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

		switch(pEvent.getType())
		{	
		case FRAME_EXPANDED:
			new PersonalLoanSCommonCode().FrameExpandEvent(pEvent);					
			break;

		case FRAGMENT_LOADED:
			new PL_RM_CSO_Review().fragment_loaded(pEvent);
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
		//empty method

	}


	public void saveFormStarted(FormEvent arg0) throws ValidatorException {
		//empty method

	}


	public void submitFormCompleted(FormEvent arg0) throws ValidatorException {
		//added by akshay on 9/12/17 for multiple refer
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PersonalLoanS.mLogger.info("Inside PL DSA_CSO_Review submitFormCompleted()" + arg0.getSource()); 
		List<String> objInput=new ArrayList<String> ();
		objInput.add("Text:"+formObject.getWFWorkitemName());
		objInput.add("Text:RM_Review");
		PersonalLoanS.mLogger.info("objInput args are: "+objInput.get(0)+objInput.get(1));
		List<Object> objOutput=new ArrayList<Object>();
		
		objOutput.add("Text");
	PersonalLoanS.mLogger.info("Before executing procedure ng_RLOS_CheckWriteOff");
	objOutput= formObject.getDataFromStoredProcedure("ng_RLOS_MultipleRefer", objInput,objOutput);
}


	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try{
			CustomSaveForm();
		formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
		saveIndecisionGrid();
		loadInDecGrid();
		}
		catch (Exception ex)
		{
			PersonalLoanS.mLogger.info(ex.getMessage());
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

