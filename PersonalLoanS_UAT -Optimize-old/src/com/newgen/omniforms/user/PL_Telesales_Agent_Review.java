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


import java.util.HashMap;

import javax.faces.validator.ValidatorException;

import org.apache.log4j.Logger;
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
		mLogger.info("PL_Telesales_Agent_Review Initiation"+ "Inside formLoaded()" + pEvent.getSource().getName());

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

	public void fragment_loaded(ComponentEvent pEvent)
	{
		mLogger.info(" In PL_Initiation eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		if (pEvent.getSource().getName().equalsIgnoreCase("Customer")) {
			formObject.setLocked("Customer_Frame1",true);
			formObject.setEnabled("Customer_save",true);
			formObject.setLocked("cmplx_Customer_ReferrorCode",false);
			formObject.setLocked("cmplx_Customer_ReferrorName",false);
			formObject.setLocked("cmplx_Customer_AppType",false);
			formObject.setLocked("cmplx_Customer_corporateCode",false);
			formObject.setLocked("cmplx_Customer_Bankingwithus",false);
			formObject.setLocked("cmplx_Customer_noofDependent",false);
			formObject.setLocked("cmplx_Customer_guardian",false);
			formObject.setLocked("cmplx_Customer_minor",false);
			formObject.setEnabled("Customer_Reference_Add",true);
			formObject.setEnabled("Customer_Reference__modify",true);
			formObject.setEnabled("Customer_Reference_delete",true);

		}	

		else if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
			LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
			formObject.setLocked("Product_Frame1",true);
			formObject.setEnabled("Product_Save",true);
			formObject.setEnabled("Product_Add",true);
			formObject.setEnabled("Product_Modify",true);
			formObject.setEnabled("Product_Delete",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails")) {
			formObject.setLocked("IncomeDetails_Frame2",true);
			formObject.setEnabled("IncomeDetails_Salaried_Save",true);						
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")) {
			formObject.setLocked("Liability_New_Frame1",true);
			formObject.setEnabled("Liability_New_AECBReport",true);
			formObject.setEnabled("Liability_New_Save",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) {
			formObject.setLocked("EMploymentDetails_Frame1",true);
			formObject.setLocked("cmplx_EmploymentDetails_StaffID",false);
			formObject.setLocked("cmplx_EmploymentDetails_Dept",false);
			formObject.setLocked("cmplx_EmploymentDetails_CntrctExpDate",false);
			formObject.setLocked("cmplx_EmploymentDetails_yrsinprevjobinUAE",false);
			formObject.setEnabled("EMploymentDetails_Save",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo")) {
			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
			formObject.setEnabled("ELigibiltyAndProductInfo_Save",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails")) {
			loadPicklist_Address();
			formObject.setLocked("AddressDetails_Frame1",true);
			formObject.setEnabled("AddressDetails_Save",true);
			formObject.setEnabled("AddressDetails_addr_Add",true);
			formObject.setEnabled("AddressDetails_addr_Modify",true);
			formObject.setEnabled("AddressDetails_addr_Delete",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails")){

			formObject.setLocked("AltContactDetails_Frame1",true);
			formObject.setEnabled("AltContactDetails_ContactDetails_Save",true);
		} 

		else if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")){

			formObject.setLocked("FATCA_Frame1",true);
			formObject.setEnabled("FATCA_Save",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("KYC")){

			formObject.setLocked("KYC_Frame1",true);
			formObject.setEnabled("KYC_Save",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("OECD")){

			formObject.setLocked("OECD_Frame1",true);
			formObject.setEnabled("OECD_Save",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
			loadPicklist1();
			//Common function for decision fragment textboxes and combo visibility
			//decisionLabelsVisibility();
		} 	
		// dishs FSD
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
		saveIndecisionGrid();
	}

}

