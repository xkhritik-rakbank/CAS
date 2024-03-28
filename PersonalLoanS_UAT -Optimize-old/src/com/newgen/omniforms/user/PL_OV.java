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

	public void fragment_loaded(ComponentEvent pEvent)
	{		mLogger.info(" In PL_Initiation eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
	FormReference formObject = FormContext.getCurrentInstance().getFormReference();

	if (pEvent.getSource().getName().equalsIgnoreCase("Customer")) {
		//setDisabled();
		formObject.setLocked("Customer_Frame1",true);
	}	

	else if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
		formObject.setLocked("Product_Frame1",true);
		LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
		LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
	}

	else if (pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails")) {

		formObject.setLocked("GuarantorDetails_Frame1",true);
	}

	else if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails")) {

		formObject.setLocked("IncomeDetails_Frame1",true);
	} 

	else if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")) {

		formObject.setLocked("ExtLiability_Frame1",true);
	}

	else if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) {

		formObject.setLocked("EMploymentDetails_Frame1",true);
	}

	else if (pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo")) {

		formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
	}

	else if (pEvent.getSource().getName().equalsIgnoreCase("LoanDetails")) {

		formObject.setLocked("LoanDetails_Frame1",true);
	}

	else if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails")) {
		loadPicklist_Address();
		formObject.setLocked("AddressDetails_Frame1",true);
	}

	else if (pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails")) {

		formObject.setLocked("AltContactDetails_Frame1",true);
	}

	else if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails")) {

		formObject.setLocked("CardDetails_Frame1",true);
	}

	else if (pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails")) {

		formObject.setLocked("ReferenceDetails_Frame1",true);
	}

	else if (pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails")) {

		formObject.setLocked("SupplementCardDetails_Frame1",true);
	}

	else if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")) {

		formObject.setLocked("FATCA_Frame6",true);
	}

	else if (pEvent.getSource().getName().equalsIgnoreCase("KYC")) {

		formObject.setLocked("KYC_Frame1",true);
	}

	else if (pEvent.getSource().getName().equalsIgnoreCase("OECD")) {

		formObject.setLocked("OECD_Frame8",true);
	}

	else if (pEvent.getSource().getName().equalsIgnoreCase("PostDisbursal")) {

		formObject.setVisible("PostDisbursal",true);
	}

	else if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
		loadPicklist1();
		//formObject.setVisible("DecisionHistory_Modify", true);
		//formObject.setVisible("cmplx_Decision_Status", true);
		//formObject.setVisible("DecisionHistory_Label2", true);
		formObject.setTop("Decision_ListView1",250);
		formObject.setTop("DecisionHistory_save",450);
		formObject.setTop("DecisionHistory_Label1",104);
		formObject.setTop("cmplx_Decision_refereason",120);
		formObject.setLeft("Decision_Label1", 297);
		formObject.setLeft("cmplx_Decision_VERIFICATIONREQUIRED", 297);
		formObject.setTop("Decision_Label1",7);
		formObject.setTop("cmplx_Decision_VERIFICATIONREQUIRED",22);
		//Common function for decision fragment textboxes and combo visibility
		//decisionLabelsVisibility();
		loadPicklist3();
	}	
	// disha FSD
	else if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails")) {

		formObject.setLocked("NotepadDetails_Frame1",true);
		formObject.setVisible("NotepadDetails_Frame3",false);
		String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
		mLogger.info("PL notepad "+ "Activity name is:" + sActivityName);
		int user_id = formObject.getUserId();
		String user_name = formObject.getUserName();
		user_name = user_name+"-"+user_id;					
		formObject.setNGValue("NotepadDetails_insqueue",sActivityName);
		formObject.setNGValue("NotepadDetails_Actusername",user_name); 
		formObject.setNGValue("NotepadDetails_user",user_name); 
		formObject.setLocked("NotepadDetails_noteDate",true);
		formObject.setLocked("NotepadDetails_Actusername",true);
		formObject.setLocked("NotepadDetails_user",true);
		formObject.setLocked("NotepadDetails_insqueue",true);
		formObject.setLocked("NotepadDetails_Actdate",true);
		formObject.setLocked("NotepadDetails_notecode",true);
		formObject.setVisible("NotepadDetails_save",true);

		formObject.setHeight("NotepadDetails_Frame1",450);
		LoadPickList("NotepadDetails_notedesc", "select '--Select--' union select  description from ng_master_notedescription");
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
		formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
		formObject.setNGValue("OV_dec", formObject.getNGValue("cmplx_Decision_Decision"));
		formObject.setNGValue("ov_validation", formObject.getNGValue("Yes"));

		saveIndecisionGrid();
	}

}

