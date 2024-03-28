/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PL_OV.java
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
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;


public class PL_OV extends PLCommon implements FormListener
{

	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;

	Logger mLogger=PersonalLoanS.mLogger;
	public void formLoaded(FormEvent pEvent)
	{
		mLogger.info("PL_OV Initiation"+ "Inside formLoaded()" + pEvent.getSource().getName());

	}


	public void formPopulated(FormEvent pEvent) 
	{
		try{
			new PersonalLoanSCommonCode().setFormHeader(pEvent);
		}catch(Exception e)
		{
			mLogger.info("PL_OV-->"+ "Exception:"+e.getMessage());
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

	public void fragment_loaded(ComponentEvent pEvent)
	{		mLogger.info(" In PL_Initiation eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
	FormReference formObject = FormContext.getCurrentInstance().getFormReference();

	if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
		//setDisabled();
		formObject.setLocked("Customer_Frame1",true);
	}	

	else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
		formObject.setLocked("Product_Frame1",true);
		LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
		LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
	}

	else if ("GuarantorDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

		formObject.setLocked("GuarantorDetails_Frame1",true);
	}

	else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

		formObject.setLocked("IncomeDetails_Frame1",true);
	} 

	else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {

		formObject.setLocked("ExtLiability_Frame1",true);
	}

	else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

		formObject.setLocked("EMploymentDetails_Frame1",true);
	}

	else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {

		formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
	}

	else if ("LoanDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

		formObject.setLocked("LoanDetails_Frame1",true);
	}

	else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
		loadPicklist_Address();
		formObject.setLocked("AddressDetails_Frame1",true);
	}

	else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

		formObject.setLocked("AltContactDetails_Frame1",true);
	}

	else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

		formObject.setLocked("CardDetails_Frame1",true);
	}

	else if ("ReferenceDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

		formObject.setLocked("ReferenceDetails_Frame1",true);
	}

	else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

		formObject.setLocked("SupplementCardDetails_Frame1",true);
	}

	else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) {

		formObject.setLocked("FATCA_Frame6",true);
	}

	else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) {

		formObject.setLocked("KYC_Frame1",true);
	}

	else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {

		formObject.setLocked("OECD_Frame8",true);
	}

	else if ("PostDisbursal".equalsIgnoreCase(pEvent.getSource().getName())) {

		formObject.setVisible("PostDisbursal",true);
	}

	else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
		loadPicklist1();
		
		formObject.setTop("Decision_ListView1",250);
		formObject.setTop("DecisionHistory_save",450);
		formObject.setTop("DecisionHistory_Label1",104);
		formObject.setTop("cmplx_Decision_refereason",120);
		formObject.setLeft("Decision_Label1", 297);
		formObject.setLeft("cmplx_Decision_VERIFICATIONREQUIRED", 297);
		formObject.setTop("Decision_Label1",7);
		formObject.setTop("cmplx_Decision_VERIFICATIONREQUIRED",22);
		
		loadPicklist3();
	}	
	// disha FSD
	else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
		//empty else if
		}


	}
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{

		mLogger.info("Inside PL_Initiation eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

		switch(pEvent.getType())
		{	

		case FRAME_EXPANDED:
			new PersonalLoanSCommonCode().FrameExpandEvent(pEvent);					
			break;

		case FRAGMENT_LOADED:
			fragment_loaded(pEvent);
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
		//empty method

	}


	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
	
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
		formObject.setNGValue("OV_dec", formObject.getNGValue("cmplx_Decision_Decision"));
		formObject.setNGValue("ov_validation", formObject.getNGValue("Yes"));

		saveIndecisionGrid();
	}

}

