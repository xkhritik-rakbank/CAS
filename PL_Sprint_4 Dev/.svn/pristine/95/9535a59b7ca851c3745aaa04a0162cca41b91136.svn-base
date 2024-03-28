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
import java.util.List;

import javax.faces.validator.ValidatorException;

import org.apache.log4j.Logger;

import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;


public class PL_OV extends PLCommon implements FormListener
{

	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	String queryData_load="";

	Logger mLogger=PersonalLoanS.mLogger;
	public void formLoaded(FormEvent pEvent)
	{
		FormConfig objConfig = FormContext.getCurrentInstance().getFormConfig();
        objConfig.getM_objConfigMap().put("PartialSave", "true");
        makeSheetsInvisible("Tab1", "8,9,14,16");//Changes done by shweta for jira #1768 to hide CPV tab //Hide CAD,Dispatch
        FormReference formObject = FormContext.getCurrentInstance().getFormReference();
        formObject.setSheetVisible(tabName, 15, true);	// Show OV Tab
	}


	public void formPopulated(FormEvent pEvent) 
	{
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			formObject.setNGValue("Mandatory_Frames", NGFUserResourceMgr_PL.getGlobalVar("OV_Frame_Name"));
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
		LoadView(pEvent.getSource().getName());

		formObject.setLocked("cmplx_Customer_DOb", true);
		formObject.setLocked("cmplx_Customer_IdIssueDate", true);
		formObject.setLocked("cmplx_Customer_EmirateIDExpiry", true);
		formObject.setLocked("cmplx_Customer_VisaIssueDate", true);
		formObject.setLocked("cmplx_Customer_PassportIssueDate", true);
		formObject.setLocked("cmplx_Customer_VIsaExpiry", true);
		formObject.setLocked("cmplx_Customer_PassPortExpiry", true);
		formObject.setLocked("Customer_Frame1",true);
		formObject.setLocked("cmplx_Customer_NEP",true);
		formObject.setLocked("cmplx_Customer_DOb",true);
		formObject.setLocked("cmplx_Customer_IdIssueDate",true);
		formObject.setLocked("cmplx_Customer_EmirateIDExpiry",true);
		formObject.setLocked("cmplx_Customer_VisaIssueDate",true);
		formObject.setLocked("cmplx_Customer_VIsaExpiry",true);
		formObject.setLocked("cmplx_Customer_PassportIssueDate",true);
		formObject.setLocked("cmplx_Customer_PassPortExpiry",true);
		loadPicklistCustomer();
		
	}	
	
	else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
		formObject.setLocked("Product_Frame1",true);
		LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct with(nolock)");
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
		LoadPickList("ExtLiability_contractType", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_contract_type with (nolock) where isactive='Y' order by code");
	}

	else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
		LoadView(pEvent.getSource().getName());

		formObject.setLocked("cmplx_EmploymentDetails_DOJ",true);
		formObject.setLocked("cmplx_EmploymentDetails_CntrctExpDate",true);
		formObject.setLocked("cmplx_EmploymentDetails_dateinPL",true);
		formObject.setLocked("cmplx_EmploymentDetails_dateinCC",true);
		formObject.setLocked("EMploymentDetails_Frame1",true);
		
		formObject.setLocked("cmplx_EmploymentDetails_DOJ",true);
		formObject.setLocked("cmplx_EmploymentDetails_CntrctExpDate",true);
		formObject.setLocked("cmplx_EmploymentDetails_dateinPL",true);
		formObject.setLocked("cmplx_EmploymentDetails_dateinCC",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_FinalInterestRate", false);
		loadPicklist4();
	}

	else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {

		formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);	
		loadPicklistELigibiltyAndProductInfo();

		loadEligibilityData();
	}

	else if ("LoanDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
		loadPicklist_LoanDetails();
		
		formObject.setLocked("LoanDetails_Frame1",true);
		formObject.setEnabled("cmplx_LoanDetails_fdisbdate", false);
		formObject.setEnabled("cmplx_LoanDetails_frepdate", false);
		formObject.setEnabled("cmplx_LoanDetails_maturitydate", false);
		formObject.setEnabled("LoanDetails_duedate", false);
		formObject.setEnabled("LoanDetails_disbdate", false);
		formObject.setEnabled("LoanDetails_payreldate", false);
		formObject.setEnabled("cmplx_LoanDetails_paidon", false);
		formObject.setEnabled("cmplx_LoanDetails_trandate", false);
		formObject.setEnabled("cmplx_LoanDetails_chqdat", false);
	}

	else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
		LoadView(pEvent.getSource().getName());

		loadPicklist_Address();
		formObject.setLocked("AddressDetails_Frame1",true);
	}

	else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
		LoadView(pEvent.getSource().getName());

		formObject.setLocked("AltContactDetails_Frame1",true);
		LoadpicklistAltcontactDetails();
		//below code added by siva on 01112019 for PCAS-1401
		AirArabiaValid();
		enrollrewardvalid();
		//Code ended by siva on 01112019 for PCAS-1401
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
	}

	else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) {
		Loadpicklistfatca();
		formObject.setLocked("FATCA_Frame6",true);
		formObject.setEnabled("cmplx_FATCA_SignedDate", false);
		formObject.setEnabled("cmplx_FATCA_ExpiryDate", false);
	}

	else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) {
		loadPicklist_KYC();
		formObject.setLocked("KYC_Frame7",true);
	}

	else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {
		LoadView(pEvent.getSource().getName());

		loadPickListOECD();
		formObject.setLocked("OECD_Frame8",true);
	}
	//below code added by yash on 14/12/2017 for PL_OV
	else if ("PartMatch".equalsIgnoreCase(pEvent.getSource().getName())) {
		//loadPicklist_Address();
		formObject.setLocked("PartMatch_Frame1",true);
		formObject.setEnabled("PartMatch_Dob", false);

	}
	else if ("FinacleCRMIncident".equalsIgnoreCase(pEvent.getSource().getName())) {
		
		formObject.setLocked("FinacleCRMIncident_Frame1",true);

	}
	else if ("FinacleCRMCustInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
		
		formObject.setLocked("FinacleCRMCustInfo_Frame1",true);

	}
	else if ("ExternalBlackList".equalsIgnoreCase(pEvent.getSource().getName())) {
		
		formObject.setLocked("ExternalBlackList_Frame1",true);

	}
	else if ("FinacleCore".equalsIgnoreCase(pEvent.getSource().getName())) {
		
		formObject.setLocked("FinacleCore_Frame1",true);
		LoadPickList("FinacleCore_ChequeType", "select '--Select--' as description,'' as code union select convert(varchar, description),Code from ng_MASTER_Cheque_Type with (nolock) order by code");
		LoadPickList("FinacleCore_TypeOfRetutn", "select '--Select--' union select convert(varchar, description) from ng_MASTER_TypeOfReturn with (nolock)");

	}
	else if ("MOL1".equalsIgnoreCase(pEvent.getSource().getName())) {
		PersonalLoanS.mLogger.info("@@@@"+pEvent.getSource().getName());
		formObject.setEnabled("MOL1_Frame1",false);

	}
	else if ("RejectEnq".equalsIgnoreCase(pEvent.getSource().getName())) {
		
		formObject.setLocked("RejectEnq_Frame1",true);

	}
	else if ("SalaryEnq".equalsIgnoreCase(pEvent.getSource().getName())) {
		formObject.setLocked("SalaryEnq_Frame1",true);
	}

	else if ("PostDisbursal".equalsIgnoreCase(pEvent.getSource().getName())) {

		formObject.setVisible("PostDisbursal",true);
	}
	/*else if ("IncomingDoc".equalsIgnoreCase(pEvent.getSource().getName())){
		fetchIncomingDocRepeater();
		//formObject.setLocked("IncomingDoc_Frame2",true);
	}*/
	else if ("ReferHistory".equalsIgnoreCase(pEvent.getSource().getName())){
		
		formObject.setLocked("ReferHistory_Frame1",true);
	}
	else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
		//for decision fragment made changes 8th dec 2017
		PersonalLoanS.mLogger.info("***********Inside decision history of csm");
		//added by yash on 15/12/2017 for PL FSD as per fsd 2.7
		fragment_ALign("Decision_Label3,cmplx_Decision_Decision#DecisionHistory_Label1,cmplx_Decision_ReferTo#DecisionHistory_Label11,DecisionHistory_DecisionReasonCode#DecisionHistory_Label41,DecisionHistory_DecisionSubReason#\n#Decision_Label4,cmplx_Decision_REMARKS#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
		formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
		formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
		
		loadPicklist1();
		
		/*formObject.setTop("Decision_ListView1",250);
		formObject.setTop("DecisionHistory_save",450);
		formObject.setTop("DecisionHistory_Label1",104);
		formObject.setTop("cmplx_Decision_refereason",120);
		formObject.setLeft("Decision_Label1", 297);
		formObject.setLeft("cmplx_Decision_VERIFICATIONREQUIRED", 297);
		formObject.setTop("Decision_Label1",7);
		formObject.setTop("cmplx_Decision_VERIFICATIONREQUIRED",22);*/
		
		//for decision fragment made changes 8th dec 2017
		
		loadPicklist3();
	}	
	
	//code changes by bandana starts
	//12th sept
			else if ("CC_Loan".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("CC_Loan_Frame1",true);
				formObject.setLocked("CC_Loan_Frame2",true);
				formObject.setLocked("CC_Loan_Frame3",true);
				formObject.setLocked("totBalTransfer",true);
				loadPicklist_ServiceRequest();
			}
	//code changes by bandana ends
	
	// disha FSD
	else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
		notepad_load();
		 //formObject.setLocked("NotepadDetails_Frame2",true);
		 //formObject.setLocked("NotepadDetails_Frame1",true);
		}
	else if ("RiskRating".equalsIgnoreCase(pEvent.getSource().getName())) {

		formObject.setLocked("RiskRating_Frame1",true);
		loadPicklistRiskRating();
	}
	


	}
	//above code added by yash on 14/12/2017
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{

	//	mLogger.info("Inside PL_Initiation eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

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
			mLogger.info("Inside value change for-->"+pEvent.getSource().getName());
			new PersonalLoanSCommonCode().value_Change(pEvent);

			break;	

		default: break;

		}
	}


	public void continueExecution(String arg0, HashMap<String, String> arg1) {
		// TODO Auto-generated method stub
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();	
		formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
		formObject.setNGValue("OV_dec", formObject.getNGValue("cmplx_Decision_Decision"));

	}


	public void initialize() {
			//empty method

	}


	public void saveFormCompleted(FormEvent arg0) throws ValidatorException {
		//empty method
		CustomSaveForm();
	}


	public void saveFormStarted(FormEvent arg0) throws ValidatorException {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PersonalLoanS.mLogger.info( "Inside PL PROCESS saveFormStarted()" + arg0.getSource());
		formObject.setNGValue("get_parent_data",queryData_load);
		PersonalLoanS.mLogger.info("Final val of queryData_load:"+ formObject.getNGValue("get_parent_data"));
	}


	public void submitFormCompleted(FormEvent arg0) throws ValidatorException {
		//empty method

	}


	public void submitFormStarted(FormEvent arg0) throws ValidatorException {


		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//below line commented by nikhil 7/12/17
		CustomSaveForm();
		//formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
		//try{----commented by akshay on 24/9/18 to throw validator for docs
			formObject.setNGValue("OV_dec", formObject.getNGValue("cmplx_Decision_Decision"));
			//modified by kashay on 4/1/17
			//if("Approve".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision"))){
		//	}
			if("Refer".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision"))){
				if( !"DDVT Maker".equalsIgnoreCase(formObject.getNGValue("q_MailSubject"))){					
					formObject.setNGValue("postHoldReferWS",formObject.getWFActivityName());
					formObject.setNGValue("IS_Stage_Reversal", "Y");
				}
			}
			else if("Reject".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision"))){
				if("Re-Initiate".equalsIgnoreCase(formObject.getNGValue("Reject_DeC"))){
					formObject.setNGValue("Reject_DeC","");
				}
			}else {
				formObject.setNGValue("IS_Stage_Reversal", "N");
			}
			OriginalDocs();
			saveIndecisionGrid();
			LoadReferGrid();
			String squeryrefer = "select referTo from NG_RLOS_GR_DECISION where workstepName like '%orignal%' and dec_wi_name= '" + formObject.getWFWorkitemName() + "' order by dateLastChanged desc ";
			List<List<String>> FCURefer = formObject.getNGDataFromDataCache(squeryrefer);
			//CreditCard.mLogger.info("RLOS COMMON"+ " iNSIDE prev_loan_dbr+ " + squeryloan);
			String referto="";
			if (FCURefer != null && FCURefer.size() > 0) {
				referto = FCURefer.get(0).get(0);
				}
			if("FPU".equalsIgnoreCase(referto)){		
				formObject.setNGValue("RefFrmOV", "Y");
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

