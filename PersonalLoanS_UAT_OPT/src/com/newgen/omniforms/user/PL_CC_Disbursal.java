/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PL_Disbursal.java
Author                                                                        : Disha
Date (DD/MM/YYYY)                                      						  : 
Description                                                                   : 

------------------------------------------------------------------------------------------------------------------------------------------------------
CHANGE HISTORY 
------------------------------------------------------------------------------------------------------------------------------------------------------

Problem No/CR No   Change Date   Changed By    Change Description

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


public class PL_CC_Disbursal extends PLCommon implements FormListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	Logger mLogger=PersonalLoanS.mLogger;

	public void formLoaded(FormEvent pEvent)
	{
		mLogger.info("Inside formLoaded()" + pEvent.getSource().getName());
	}


	public void formPopulated(FormEvent pEvent) 
	{
		try{
			new PersonalLoanSCommonCode().setFormHeader(pEvent);
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			formObject.setVisible("Card_Details", true);
			
		}catch(Exception e)
		{
			PersonalLoanS.mLogger.info( "Exception:"+e.getMessage());
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

	public void fragment_loaded(ComponentEvent pEvent){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PersonalLoanS.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

		if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			
			formObject.setLocked("Customer_Frame1",true);
		}	

		else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Product_Frame1",true);
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
			LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_ApplicationType with (nolock) order by code");
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

		else if ("LoanDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist_LoanDetails();
			
			formObject.setLocked("LoanDetails_Frame1",true);
		}

		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist_Address();
			formObject.setLocked("AddressDetails_Frame1",true);
		}

		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("AltContactDetails_Frame1",true);
			LoadpicklistAltcontactDetails();
		}

		else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("CardDetails_Frame1",true);
			loadDataInCRNGrid();
		}

		else if ("ReferenceDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ReferenceDetails_Frame1",true);
			LoadPickList("ReferenceDetails_ref_Relationship", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Relationship with (nolock) order by code");
			
		}

		else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("SupplementCardDetails_Frame1",true);
		}

		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("FATCA_Frame6",true);
			Loadpicklistfatca();
		}

		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("KYC_Frame1",true);
		}

		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPickListOECD();
			formObject.setLocked("OECD_Frame8",true);
		}

		else if ("PartMatch".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("PartMatch_Frame1",true);
		}

		else if ("FinacleCRMIncident".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("FinacleCRMIncident_Frame1",true);
		}

		else if ("FinacleCRMCustInfo".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("FinacleCRMCustInfo_Frame1",true);
		}

		else if ("FinacleCore".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("FinacleCore_Frame1",true);
		}

		else if ("MOL1".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("MOL1_Frame1",true);
		}

		else if ("WorldCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("WorldCheck1_Frame1",true);
		}

		else if ("RejectEnq".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("RejectEnq_Frame1",true);
		}

		else if ("SalaryEnq".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("SalaryEnq_Frame1",true);
		}

		else if ("CustDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("CustDetailVerification_Frame1",true);
		}

		else if ("BussinessVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("BussinessVerification_Frame1",true);
		}

		else if ("HomeCountryVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("HomeCountryVerification_Frame1",true);
		}

		else if ("ResidenceVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ResidenceVerification_Frame1",true);
		}

		else if ("GuarantorVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("GuarantorVerification_Frame1",true);
		}

		else if ("ReferenceDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ReferenceDetailVerification_Frame1",true);
		}

		else if ("OfficeandMobileVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("OfficeandMobileVerification_Frame1",true);
		}

		else if ("LoanandCard".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("LoanandCard_Frame1",true);
		}
		else if ("CC_Creation".equalsIgnoreCase(pEvent.getSource().getName())) {

			//formObject.setLocked("LoanandCard_Frame1",true);
			LoadPickList("CC_Creation_Product","select '--Select--' as description,'' as code union select distinct convert(varchar, Description),code from ng_master_cardProduct with (nolock) order by code");
			loadInCCCreationGrid();
		}
		else if ("Limit_Inc".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("Limit_Inc_Frame4",false);
			loadDataInLimitInc();
		}
		

		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			notepad_load();
			 notepad_withoutTelLog();
			
		}

		else if ("SmartCheck".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("SmartCheck_Frame1",true);
		}

		else if ("Compliance".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("Compliance_Frame1",true);
		}

		else if ("FCU_Decision".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("FCU_Decision_Frame1",true);
		}

		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			if(NGFUserResourceMgr_PL.getGlobalVar("PL_false").equalsIgnoreCase(formObject.getNGValue("AlternateContactDetails_RetainAccIfLoanReq"))){
				PersonalLoanS.mLogger.info( "after making buttons visible"+formObject.getNGValue("AlternateContactDetails_RetainAccIfLoanReq"));
				formObject.setVisible("CC_Creation_Update_Customer", true);
				
				PersonalLoanS.mLogger.info( "after making buttons visible");
			}
			//for decision fragment made changes 8th dec 2017
			PersonalLoanS.mLogger.info("***********Inside decision history");
			fragment_ALign("Decision_Label1,cmplx_Decision_VERIFICATIONREQUIRED#Decision_Label3,cmplx_Decision_Decision#DecisionHistory_Label1,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_DecisionReasonCode#Decision_Label4,cmplx_Decision_REMARKS#\n#Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
			formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
			formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
			
			PersonalLoanS.mLogger.info("***********Inside after fragment alignment decision history");
	
			loadPicklist1();

		
		
			/*formObject.setNGValue("cmplx_Decision_Decision", "--Select--");
			
			formObject.setLocked("cmplx_Decision_VERIFICATIONREQUIRED",true);
		

		
			formObject.setTop("Decision_Label1", 8);
			
			formObject.setVisible("cmplx_Decision_waiveoffver",false);
			formObject.setTop("cmplx_Decision_VERIFICATIONREQUIRED", 23);*/
			
			//for decision fragment made changes 8th dec 2017
		

		}	


	}


	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{
		PersonalLoanS.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
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
		saveIndecisionGrid();
		//loadInDecGrid();
	}

}
