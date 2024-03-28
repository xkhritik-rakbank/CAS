/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PL_Post_Disbursal.java
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



public class PL_Post_Disbursal extends PLCommon implements FormListener
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
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			new PersonalLoanSCommonCode().setFormHeader(pEvent);
			formObject.setVisible("CC_Creation", false);
			 formObject.setVisible("Limit_Inc", false);
			 formObject.setVisible("Post_Disbursal", false);
		}catch(Exception e)
		{
			mLogger.info("PL_POST_Disbursal"+ "Exception:"+e.getMessage());
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
		mLogger.info(" In PL_Initiation eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			
			formObject.setLocked("Customer_Frame1",true);
			formObject.setLocked("cmplx_Customer_DOb", true);
			formObject.setLocked("cmplx_Customer_IdIssueDate", true);
			formObject.setLocked("cmplx_Customer_EmirateIDExpiry", true);
			formObject.setLocked("cmplx_Customer_VisaIssueDate", true);
			formObject.setLocked("cmplx_Customer_PassportIssueDate", true);
			formObject.setLocked("cmplx_Customer_VIsaExpiry", true);
			formObject.setLocked("cmplx_Customer_PassPortExpiry", true);
			
		
			loadPicklistCustomer();
			
		}	

		else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Product_Frame1",true);
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct with(nolock)");
			LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_ApplicationType with(nolock) where SubProdFlag = '"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2)+"'");
			LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) order by SCHEMEID");
			LoadPickList("subProd", "select '--Select--' as description,'' as code union select description,code from ng_MASTER_SubProduct_PL with (nolock) order by code");
		}

		else if ("GuarantorDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("GuarantorDetails_Frame1",true);
			loadPicklist_Guarantor();
		}

		else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("IncomeDetails_Frame1",true);
			LoadPickList("cmplx_IncomeDetails_StatementCycle", "select '--Select--' union select convert(varchar, description) from NG_MASTER_StatementCycle with (nolock)");
			
		}

		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ExtLiability_Frame1",true);
			LoadPickList("Liability_New_worststatuslast24", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from ng_master_Aecb_Codes with (nolock) order by code");
			LoadPickList("ExtLiability_contractType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_contract_type with (nolock) order by code");

		}

		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("EMploymentDetails_Frame1",true);
			loadPicklist4();
			 if(!NGFUserResourceMgr_PL.getGlobalVar("PL_RESC").equalsIgnoreCase(formObject.getNGValue("Application_Type")) && !NGFUserResourceMgr_PL.getGlobalVar("PL_RESN").equalsIgnoreCase(formObject.getNGValue("Application_Type")) && !NGFUserResourceMgr_PL.getGlobalVar("PL_RESR").equalsIgnoreCase(formObject.getNGValue("Application_Type"))){
				 formObject.setVisible("cmplx_EmploymentDetails_channelcode",false);
				 formObject.setVisible("EMploymentDetails_Label36",false);
			 }
		}

		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);	
			loadEligibilityData();
		}

		else if ("LoanDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist_LoanDetails();
			
			formObject.setLocked("LoanDetails_Frame1",true);
		}
		else if("Loan_Disbursal".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setLocked("Loan_Disbursal_Frame2", true);
			formObject.setEnabled("Loan_Disbursal_Frame2", false);
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
			LoadpicklistCardDetails();
			formObject.setLocked("CardDetails_Frame1",true);
		}

		else if ("ReferenceDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ReferenceDetails_Frame1",true);
			LoadPickList("ReferenceDetails_ref_Relationship", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Relationship with (nolock) order by code");
			
		}

		else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("SupplementCardDetails_Frame1",true);
			loadPicklist_suppCard();
			formObject.setLocked("SupplementCardDetails_DOB",true);
			formObject.setLocked("SupplementCardDetails_idIssueDate",true);
			formObject.setLocked("SupplementCardDetails_VisaIssueDate",true);
			formObject.setLocked("SupplementCardDetails_PassportIssueDate",true);
			formObject.setLocked("SupplementCardDetails_VisaExpiry",true);
			formObject.setLocked("SupplementCardDetails_EmiratesIDExpiry",true);
			formObject.setLocked("SupplementCardDetails_PassportExpiry",true);
			
		}

		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) {
			Loadpicklistfatca();
			formObject.setLocked("FATCA_Frame6",true);
		}

		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist_KYC();
			formObject.setLocked("KYC_Frame7",true);
		}

		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPickListOECD();
			formObject.setLocked("OECD_Frame8",true);
		}

		else if ("PartMatch".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("PartMatch_Frame1",true);
			formObject.setLocked("PartMatch_Dob",true);
			//LoadPickList("PartMatch_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			
		}

		else if ("FinacleCRMIncident".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("FinacleCRMIncident_Frame1",true);
		}

		else if ("FinacleCRMCustInfo".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("FinacleCRMCustInfo_Frame1",true);
		}

		else if ("FinacleCore".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("FinacleCore_Frame1",true);
			formObject.setLocked("FinacleCore_DatePicker2", true);
			formObject.setLocked("FinacleCore_cheqretdate", true);
			LoadPickList("FinacleCore_cheqtype", "select '--Select--' as description,'' as code union select convert(varchar, description),Code from ng_MASTER_Cheque_Type with (nolock) order by code");
			
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
			formObject.setLocked("cmplx_CustDetailVerification_dob_upd",true);
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
			formObject.setLocked("cmplx_OffVerification_doj_upd",true);
		}

		else if ("LoanandCard".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("LoanandCard_Frame1",true);
		}
		// disha FSD
		
		//++below code added by yash on 16/12/017 for post_disbursal
		else if ("NotepadDetails" .equalsIgnoreCase(pEvent.getSource().getName())) {
			notepad_load();
			 notepad_withoutTelLog();
	}
		//++above code added by yash on 16/12/2017
		else if ("PostDisbursal_Checklist" .equalsIgnoreCase(pEvent.getSource().getName())) {
			//empty else if
		}
		else if("PostDisbursal".equalsIgnoreCase(pEvent.getSource().getName())) {
				LoadPickList("PostDisbursal_NLC_BankName", "select '--Select--' as description ,'' as code union select description,code from ng_master_bankName with(nolock) where isactive='y' order by code");
				LoadPickList("PostDisbursal_Bank_Name", "select '--Select--' as description ,'' as code union select description,code from ng_master_bankName with(nolock) where isactive='y' order by code");				
}
		
		else if ("SmartCheck".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("SmartCheck_Frame1",true);
		}
		//Changes done for code optimization 25/07

		else if ("Compliance".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("Compliance_Frame1",true);
		}

		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			//for decision fragment made changes 8th dec 2017
			PersonalLoanS.mLogger.info("***********Inside decision history of csm");
			//code by saurabh on 18th Oct.
			formObject.setVisible("Gen_welc_letter", true);
			//changed by yash on 16/12/2017 for post_disbursal
			fragment_ALign("Decision_Label3,cmplx_Decision_Decision#DecisionHistory_Label1,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_DecisionReasonCode#Decision_Label4,cmplx_Decision_REMARKS#DecisionHistory_Label26,#DecisionHistory_Button4,#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#Decision_ListView1#\n#DecisionHistory_save#Gen_welc_letter","DecisionHistory");//\n for new line
			formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
			formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
			//for decision fragment made changes 8th dec 2017
			loadPicklist1();
			//Common function for decision fragment textboxes and combo visibility
			//decisionLabelsVisibility();
		} 	
		else if ("IncomingDoc".equalsIgnoreCase(pEvent.getSource().getName())) {
			 fetchIncomingDocRepeater();
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
		CustomSaveForm();
	}


	public void saveFormStarted(FormEvent arg0) throws ValidatorException {
	//empty method

	}


	public void submitFormCompleted(FormEvent arg0) throws ValidatorException {
		//empty method
		
		//++ below code added by Deepak on 19/03/2018 for EFMS refresh functionality
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		List<String> objInput=new ArrayList<String>();

		String decision_str = formObject.getNGValue("cmplx_Decision_Decision");
		if(NGFUserResourceMgr_PL.getGlobalVar("PL_Submit_Desc").equalsIgnoreCase(decision_str)){
			objInput.add("Text:"+formObject.getWFWorkitemName());
			objInput.add("Text:"+"Approve");
			PersonalLoanS.mLogger.info("objInput args are: "+objInput.get(0));
			List<Object> objOutput=new ArrayList<Object>();
			
			objOutput.add("Text");
		PersonalLoanS.mLogger.info("Before executing procedure ng_RLOS_CheckWriteOff");
		objOutput= formObject.getDataFromStoredProcedure("ng_EFMS_InsertData", objInput,objOutput);
		}
		//++ above code added by Deepak on 19/03/2018 for EFMS refresh functionality	

	}



	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
	
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try
		{
			CustomSaveForm();
		formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
		saveIndecisionGrid();
		
		}
		catch(Exception e)
		{
			PersonalLoanS.mLogger.info(e.getMessage());
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

