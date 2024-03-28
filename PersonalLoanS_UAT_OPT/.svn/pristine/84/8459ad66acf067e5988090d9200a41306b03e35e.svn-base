/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PL_Waiver_Authority.java
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
public class PL_Waiver_Authority extends PLCommon implements FormListener
{
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	Logger mLogger=PersonalLoanS.mLogger;
	public void formLoaded(FormEvent pEvent)
	{
		mLogger.info("PL Initiation"+ "Inside formLoaded()" + pEvent.getSource().getName());

	}


	public void formPopulated(FormEvent pEvent) 
	{
		try{
			new PersonalLoanSCommonCode().setFormHeader(pEvent);
		}catch(Exception e)
		{
			mLogger.info("RLOS Initiation"+ "Exception:"+e.getMessage());
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
	{
		mLogger.info(" In PL_Initiation eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Customer_Frame1",true);
		}	
		else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
			LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_ApplicationType with (nolock) order by code");
			formObject.setLocked("Product_Frame1",true);
		}
		else if ("GuarantorDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("GuarantorDetails_Frame1",true);
		}
		else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("IncomeDetails_Frame1",true);
			LoadPickList("cmplx_IncomeDetails_StatementCycle", "select '--Select--' union select convert(varchar, description) from NG_MASTER_StatementCycle with (nolock)");
			
						
		}

		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ExtLiability_Frame1",true);
			LoadPickList("Liability_New_worststatuslast24", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from ng_master_Aecb_Codes with (nolock) order by code");

		
		}
		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("EMploymentDetails_Frame1",true);
			
		}
		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);	
			loadEligibilityData();
			
		}
		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist_Address();
			formObject.setLocked("AddressDetails_Frame1",true);
			
		}
		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setLocked("AltContactDetails_Frame1",true);
			LoadpicklistAltcontactDetails();
			
		} 
		else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("CardDetails_Frame1",true);
		}
		else if ("ReferenceDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ReferenceDetails_Frame1",true);
			LoadPickList("ReferenceDetails_ref_Relationship", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Relationship with (nolock) order by code");
			
		}
		else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("SupplementCardDetails_Frame1",true);
		}
		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setLocked("FATCA_Frame6",true);
			Loadpicklistfatca();
		
		}
		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setLocked("KYC_Frame1",true);
		
		}
		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setLocked("OECD_Frame8",true);
			loadPickListOECD();
		}
		
		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			notepad_load();
			 notepad_withoutTelLog();
		}

		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			//for decision fragment made changes 8th dec 2017
			PersonalLoanS.mLogger.info("***********Inside decision history of csm");
			fragment_ALign("DecisionHistory_Label10,cmplx_Decision_New_CIFNo#DecisionHistory_Label7,cmplx_Decision_AccountNo#DecisionHistory_Label6,cmplx_Decision_IBAN#DecisionHistory_Button2#Decision_Label1,cmplx_Decision_VERIFICATIONREQUIRED#\n#Decision_Label3,cmplx_Decision_Decision#DecisionHistory_Label1,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_DecisionReasonCode#Decision_Label4,cmplx_Decision_REMARKS#DecisionHistory_Label26,cmplx_Decision_AppID#\n#Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
			formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
			formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
			//for decision fragment made changes 8th dec 2017
			loadPicklist1();
			
		} 	
	}

	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{

		mLogger.info("Inside PL_Initiation eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

		switch(pEvent.getType()) {

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
		formObject.setNGValue("Waiver_dec", formObject.getNGValue("cmplx_Decision_Decision"));
		//below code commented by nikhil 08/12/17
		//formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
		saveIndecisionGrid();
		//loadInDecGrid();
	}

}

