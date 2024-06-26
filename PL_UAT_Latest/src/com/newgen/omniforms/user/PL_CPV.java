/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PL_CPV.java
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


public class PL_CPV extends PLCommon implements FormListener
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

	}


	public void formPopulated(FormEvent pEvent) 
	{
		try{
			new PersonalLoanSCommonCode().setFormHeader(pEvent);
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String cc_waiv_flag = formObject.getNGValue("is_cc_waiver_require");
			if(cc_waiv_flag.equalsIgnoreCase("N")){
			formObject.setVisible("Card_Details", true);
			}
		}catch(Exception e)
		{
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
		FormReference formObject= FormContext.getCurrentInstance().getFormReference();
		PersonalLoanS.mLogger.info(" In PL_Initiation eventDispatched()-->EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			
			formObject.setLocked("Customer_Frame1",true);
			formObject.setLocked("cmplx_Customer_NEP", true);
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
			LoadPickList("ExtLiability_contractType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_contract_type with (nolock) order by code");

			formObject.setVisible("ExtLiability_AECBReport", false);
			formObject.setVisible("cmplx_Liability_New_overrideAECB", false);//need to check
			formObject.setVisible("cmplx_Liability_New_overrideIntLiab", false);
		}

		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("EMploymentDetails_Frame1",true);
			formObject.setLocked("cmplx_EmploymentDetails_DOJ",true);
			formObject.setLocked("cmplx_EmploymentDetails_CntrctExpDate",true);
			formObject.setLocked("cmplx_EmploymentDetails_dateinPL",true);
			formObject.setLocked("cmplx_EmploymentDetails_dateinCC",true);
		
			loadPicklist4();
			
		}

		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);	
			loadEligibilityData();
			formObject.setVisible("cmplx_EligibilityAndProductInfo_FinalInterestRate", false);
		}

		else if ("LoanDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist_LoanDetails();
			if(!formObject.isVisible("ELigibiltyAndProductInfo_Frame1")){
				formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligibilityProdInfo");
				
			}
			loanvalidate();
			formObject.setLocked("LoanDetails_Frame1",true);
			formObject.setLocked("cmplx_LoanDetails_fdisbdate",true);
			formObject.setLocked("cmplx_LoanDetails_frepdate",true);
			formObject.setLocked("cmplx_LoanDetails_maturitydate",true);
			formObject.setEnabled("LoanDetails_duedate", false);
			formObject.setEnabled("LoanDetails_disbdate", false);
			formObject.setEnabled("LoanDetails_payreldate", false);
			formObject.setEnabled("cmplx_LoanDetails_paidon", false);
			formObject.setEnabled("cmplx_LoanDetails_trandate", false);
			formObject.setEnabled("cmplx_LoanDetails_chqdat", false);
		
		}
		else if ("RiskRating".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("RiskRating_Frame1",true);
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
			formObject.setLocked("SupplementCardDetails_DOB",true);
			formObject.setLocked("SupplementCardDetails_idIssueDate",true);
			formObject.setLocked("SupplementCardDetails_VisaIssueDate",true);
			formObject.setLocked("SupplementCardDetails_PassportIssueDate",true);
			formObject.setLocked("SupplementCardDetails_VisaExpiry",true);
			formObject.setLocked("SupplementCardDetails_EmiratesIDExpiry",true);
			formObject.setLocked("SupplementCardDetails_PassportExpiry",true);
			
		}

		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("FATCA_Frame6",true);
			formObject.setEnabled("cmplx_FATCA_SignedDate", false);
			formObject.setEnabled("cmplx_FATCA_ExpiryDate", false);
			Loadpicklistfatca();
		}

		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("KYC_Frame1",true);
			loadPicklist_KYC();
		}

		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPickListOECD();
			formObject.setLocked("OECD_Frame8",true);
		}
		//++Below code added by nikhil 13/11/2017 for Code merge
		else if ("CustDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

		/*	formObject.setLocked("cmplx_CustDetailVerification_Mob_No1_val",true);
			formObject.setLocked("cmplx_CustDetailVerification_Mob_No2_val",true);
			formObject.setLocked("cmplx_CustDetailVerification_dob_val",true);
			formObject.setLocked("cmplx_CustDetailVerification_POBoxNo_val",true);
			formObject.setLocked("cmplx_CustDetailVerification_emirates_val",true);
			formObject.setLocked("cmplx_CustDetailVerification_persorcompPOBox_val",true);
			formObject.setLocked("cmplx_CustDetailVerification_Resno_val",true);
			formObject.setLocked("cmplx_CustDetailVerification_Offtelno_val",true);
			formObject.setLocked("cmplx_CustDetailVerification_hcountrytelno_val",true);
			formObject.setLocked("cmplx_CustDetailVerification_hcountryaddr_val",true);
			formObject.setLocked("cmplx_CustDetailVerification_email1_val",true);
			formObject.setLocked("cmplx_CustDetailVerification_email2_val",true);*/
			//++ below code added by abhishek to make fields enable/disable/hidden as per FSD 2.7.3
			enable_custVerification();
			//-- Above code added by abhishek to make fields enable/disable/hidden as per FSD 2.7.3
		}
		
		
		//++ below code added by abhishek to make fields enable/disable/hidden as per FSD 2.7.3
		if (pEvent.getSource().getName().equalsIgnoreCase("BussinessVerification")) {
			//formObject.setLocked("BussinessVerification_Frame1",true);
			enable_busiVerification();
			
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("HomeCountryVerification")) {
			//formObject.setLocked("HomeCountryVerification_Frame1",true);
			enable_homeVerification();
			
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("ResidenceVerification")) {
			//formObject.setLocked("ResidenceVerification_Frame1",true);
			enable_ResVerification();
			
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetailVerification")) {
			//formObject.setLocked("ReferenceDetailVerification_Frame1",true);
			enable_ReferenceVerification();
			
		}
		//-- Above code added by abhishek to make fields enable/disable/hidden as per FSD 2.7.3
		//--Above code added by nikhil 13/11/2017 for Code merge

		else if ("OfficeandMobileVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

			/*formObject.setLocked("cmplx_OffVerification_fxdsal_val",true);
			formObject.setLocked("cmplx_OffVerification_accprovd_val",true);
			formObject.setLocked("cmplx_OffVerification_desig_val",true);
			formObject.setLocked("cmplx_OffVerification_doj_val",true);
			formObject.setLocked("cmplx_OffVerification_cnfrminjob_val",true);*/
			//++Below code added by nikhil 13/11/2017 for Code merge
			//++ below code added by abhishek to make fields enable/disable/hidden as per FSD 2.7.3
			enable_officeVerification();
			formObject.setLocked("OfficeandMobileVerification_Frame1", true);
			formObject.setLocked("cmplx_OffVerification_doj_upd",true);
			formObject.setEnabled("OfficeandMobileVerification_Enable", true);
 			//-- Above code added by abhishek to make fields enable/disable/hidden as per FSD 2.7.3
			//--Above code added by nikhil 13/11/2017 for Code merge
			LoadPickList("cmplx_OffVerification_desig_upd", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Designation with (nolock) where isActive='Y' order by Code");
			
		}
		

		else if ("LoanandCard".equalsIgnoreCase(pEvent.getSource().getName())) {

			/*formObject.setLocked("cmplx_LoanandCard_loanamt_val",true);
			formObject.setLocked("cmplx_LoanandCard_tenor_val",true);
			formObject.setLocked("cmplx_LoanandCard_emi_val",true);
			formObject.setLocked("cmplx_LoanandCard_islorconv_val",true);
			formObject.setLocked("cmplx_LoanandCard_firstrepaydate_val",true);
			formObject.setLocked("cmplx_LoanandCard_cardtype_val",true);
			formObject.setLocked("cmplx_LoanandCard_cardlimit_val",true);*/
			//++Below code added by nikhil 13/11/2017 for Code merge
			//++ below code added by abhishek to make fields enable/disable/hidden as per FSD 2.7.3
			enable_loanCard();
			//-- Above code added by abhishek to make fields enable/disable/hidden as per FSD 2.7.3
			//--Above code added by nikhil 13/11/2017 for Code merge
		}
		
		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			
			//for decision fragment made changes 8th dec 2017
			//++Below code added by nikhil 13/11/2017 for Code merge
        	loadPicklist1();
        	openCPVtabs(formObject);
        	// ++ below code already exist - 10-10-2017
        	 /*	formObject.setNGValue("cmplx_Decision_Decision", "--Select--");
        	
        	// disha Decision waiver made invisible
        	formObject.setVisible("cmplx_Decision_waiveoffver",false);
			// ++ above code already exist - 10-10-2017
        	formObject.setVisible("Decision_Label1",false);
        	formObject.setVisible("cmplx_Decision_VERIFICATIONREQUIRED",false);
        	formObject.setVisible("DecisionHistory_chqbook",false);
        	formObject.setVisible("DecisionHistory_Label1",false);
        	formObject.setVisible("cmplx_Decision_refereason",false);
        	formObject.setVisible("DecisionHistory_Rejreason",false);
        	formObject.setVisible("cmplx_Decision_rejreason",false);
        	formObject.setVisible("DecisionHistory_Button1",false);
        	formObject.setVisible("DecisionHistory_Label5",false);
        	formObject.setVisible("cmplx_Decision_desc",false);
        	formObject.setVisible("DecisionHistory_Label3",false);
        	formObject.setVisible("cmplx_Decision_strength",false);
        	formObject.setVisible("DecisionHistory_Label4",false);
        	formObject.setVisible("cmplx_Decision_weakness",false);			                	
        	formObject.setVisible("DecisionHistory_Label11",true);
        	formObject.setVisible("cmplx_Decision_Decreasoncode",true);
        	formObject.setVisible("DecisionHistory_Label12",true);
        	formObject.setVisible("cmplx_Decision_NoofAttempts",true);
        	
       	formObject.setTop("Decision_Label3",10);
        	formObject.setTop("cmplx_Decision_Decision",24);
        	formObject.setTop("DecisionHistory_Label11",10);
        	formObject.setTop("cmplx_Decision_Decreasoncode",24);
        	formObject.setTop("DecisionHistory_Label12",10);
        	formObject.setTop("cmplx_Decision_NoofAttempts",24);
        	formObject.setTop("Decision_Label4",58);
        	formObject.setTop("cmplx_Decision_REMARKS",72);			                	
        	formObject.setTop("Decision_ListView1",200);
        	formObject.setTop("DecisionHistory_save",400);
        	
        	formObject.setLeft("Decision_Label4",24);
        	formObject.setLeft("cmplx_Decision_REMARKS",24);
        	formObject.setLeft("Decision_Label3",24);
        	formObject.setLeft("cmplx_Decision_Decision",24);
        	formObject.setLeft("DecisionHistory_Label11",297);
        	formObject.setLeft("DecisionHistory_DecisionReasonCode",297);
        	formObject.setLeft("DecisionHistory_Label12",555);
        	formObject.setLeft("cmplx_Decision_NoofAttempts",555);*/		                	
			// ++ below code already exist - 10-10-2017
			// disha cpv required position corrected
        	/*formObject.setTop("Decision_Label1", 8);
        	formObject.setTop("cmplx_Decision_VERIFICATIONREQUIRED", 23);*/
        	
        	PersonalLoanS.mLogger.info("***********Inside decision history");
        	fragment_ALign("Decision_Label3,cmplx_Decision_Decision#DecisionHistory_Label1,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_DecisionReasonCode#DecisionHistory_Label12,cmplx_Decision_NoofAttempts#\n#Decision_Label4,cmplx_Decision_REMARKS#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
        	formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
			formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
			
        	PersonalLoanS.mLogger.info("***********Inside after fragment alignment decision history");
			formObject.setVisible("cmplx_Decision_Referralreason", false);
        	//disha FSD P1 - CPV required field to be disabled at Decision tab
        	formObject.setLocked("cmplx_Decision_VERIFICATIONREQUIRED",true);
    		//for decision fragment made changes 8th dec 2017
        	formObject.setNGValue("CPV_DECISION", formObject.getNGValue("cmplx_Decision_Decision"));//Done by aman 17/12
    		
        
			// ++ above code already exist - 10-10-2017
        	
        	//Common function for decision fragment textboxes and combo visibility
        	//decisionLabelsVisibility();
        	//--Above code added by nikhil 13/11/2017 for Code merge
        	
	 } 	
		// disha FSD
		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//++Below code added by nikhil 13/11/2017 for Code merge
			//++ below code added by abhishek to load notepad details as per FSD 2.7.3
			notepad_load();
			formObject.setVisible("NotepadDetails_Frame3",true);
			//-- Above code added by abhishek to load notepad details as per FSD 2.7.3;
			//--Above code added by nikhil 13/11/2017 for Code merge
			
		
		}
		 else if ("IncomingDoc".equalsIgnoreCase(pEvent.getSource().getName())) {
		 fetchIncomingDocRepeater();
		 }

	}

	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{

		//PersonalLoanS.mLogger.info("Inside PL_Initiation eventDispatched()-->EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

		switch(pEvent.getType())
		{	

		case FRAME_EXPANDED:
			//PersonalLoanS.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
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
		CustomSaveForm();
		

	}


	public void saveFormStarted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		if(formObject.isVisible("OfficeandMobileVerification_Frame1")==true)
		{
		if("".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_desig_upd")) || "--Select--".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_desig_upd")))
		{
			formObject.setNGValue("cmplx_OffVerification_desig_upd", formObject.getNGValue("cmplx_EmploymentDetails_Designation"));
		}
		if("".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_doj_upd")) || "--Select--".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_doj_upd")))
		{
			formObject.setNGValue("cmplx_OffVerification_doj_upd", formObject.getNGValue("cmplx_OffVerification_doj_val"));
		}
		if("".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_cnfrminjob_upd")) || "--Select--".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_cnfrminjob_upd")))
		{
			formObject.setNGValue("cmplx_OffVerification_cnfrminjob_upd", formObject.getNGValue("cmplx_OffVerification_cnfrminjob_val"));
		}
		}
		

	}


	public void submitFormCompleted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		PersonalLoanS.mLogger.info( "PL_CPV"+"Inside submitFormCompleted()");
	}


	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		CustomSaveForm();
		//below code added by nikhil 7/12/17
		//formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
		try
		{
		PersonalLoanS.mLogger.info("Inside  submitFormStarted ");
		PersonalLoanS.mLogger.info("Inside  value of decision CPV" + formObject.getNGValue("cmplx_Decision_Decision"));
		formObject.setNGValue("CPV_DECISION", formObject.getNGValue("cmplx_Decision_Decision"));//Done by aman 17/12
		
		/*String decision = formObject.getNGValue("cmplx_Decision_Decision");
		String query = "update ng_pl_exttable set CPV_DECISION='"+decision+"' where PL_wi_name = '"+formObject.getWFWorkitemName()+"'";
		PersonalLoanS.mLogger.info("query for cpv decision "+query);
		formObject.saveDataIntoDataSource(query);*/
		
		PersonalLoanS.mLogger.info( "cmplx_Decision_Decision:" +  formObject.getNGValue("cmplx_Decision_Decision")+ "#ControlName#");

		//formObject.setNGValue("CPV_dec", formObject.getNGValue("cmplx_Decision_Decision"));



		 if("Approve Sub to CIF".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")))
		    {
			 PersonalLoanS.mLogger.info("Inside  Approve Sub to CIF ");
		        formObject.setNGValue("IS_Approve_Cif","Y");
		        PersonalLoanS.mLogger.info("update  Approve Sub to CIF " + formObject.getNGValue("cmplx_Decision_Decision"));
		    }
		 if("Approve".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")) || "Reject".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")))
		  {
			 PersonalLoanS.mLogger.info("Inside  Approve CPV");
			  formObject.setNGValue("q_hold1",1);
		
			  PersonalLoanS.mLogger.info("Inside NA Approve CPV " + formObject.getNGValue("q_hold1"));
		  }
		saveIndecisionGrid();
		LoadReferGrid();
		}
		catch(Exception e)
		{
			PersonalLoanS.mLogger.info("Exception occured in submit form started method" +e.getMessage());
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

