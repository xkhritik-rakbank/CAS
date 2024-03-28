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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
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
        FormReference formObject = FormContext.getCurrentInstance().getFormReference();
        formObject.setSheetVisible(tabName, 9, true);	
        makeSheetsInvisible("Tab1", "16");	//Hide Dispatch Tab
        formObject.setEnabled("FinacleCore_Combo1", false);
        
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
			enable_CPV();
			formObject.setVisible("Home_country_verification", false); // hritik 22.8.21 -3779
		}catch(Exception e)
		{
			printException(e);        
		}
		 CheckforRejects("CPV");
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
			LoadView(pEvent.getSource().getName());
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

			formObject.setVisible("cmplx_Liability_New_overrideAECB", false);//need to check
			formObject.setVisible("cmplx_Liability_New_overrideIntLiab", false);
			formObject.setVisible("ExtLiability_Label15",false);
			formObject.setVisible("cmplx_Liability_New_AggrExposure",false);
			formObject.setLocked("ExtLiability_Frame4",true);// hritik 30.6.21 PCASI3564
			formObject.setEnabled("ExtLiability_Takeoverindicator",false);// hritik 30.6.21 PCASI3564
		}
		//code added by bandana start
		else if ("CC_Loan".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("CC_Loan_Frame1",true);
			//++Below code added by  nikhil 11/10/17 as per CC FSD 2.2
			loadPicklist_ServiceRequest();
			//--above code added by  nikhil 11/10/17 as per CC FSD 2.2
			
		}
		//code added by bandana end
		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadView(pEvent.getSource().getName());
			formObject.setLocked("EMploymentDetails_Frame1",true);
			formObject.setLocked("cmplx_EmploymentDetails_DOJ",true);
			formObject.setLocked("cmplx_EmploymentDetails_CntrctExpDate",true);
			formObject.setLocked("cmplx_EmploymentDetails_dateinPL",true);
			formObject.setLocked("cmplx_EmploymentDetails_dateinCC",true);
			formObject.setVisible("cmplx_EmploymentDetails_TL_Number", false);//pcasi 3601
			formObject.setVisible("EMploymentDetails_Label63", false);// 4.7.21 
			formObject.setVisible("EMploymentDetails_Label65", false);
			formObject.setVisible("cmplx_EmploymentDetails_TL_Emirate", false);
		
			loadPicklist4();
			
		}

		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklistELigibiltyAndProductInfo();

			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);	
			loadEligibilityData();
			formObject.setVisible("cmplx_EligibilityAndProductInfo_FinalInterestRate", false);
			formObject.setLocked("ELigibiltyAndProductInfo_Frame4",true); // by jahnavi for PCASI-3142
			formObject.setEnabled("ELigibiltyAndProductInfo_Frame4",false); // by jahnavi for PCASI-3142
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
			loadPicklistRiskRating();
		}
		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadView(pEvent.getSource().getName());
			loadPicklist_Address();
		}

		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadView(pEvent.getSource().getName());

			LoadpicklistAltcontactDetails();
			//below code added by siva on 01112019 for PCAS-1401
			AirArabiaValid();

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

			formObject.setLocked("KYC_Frame7",true);
			loadPicklist_KYC();
		}
		// hritik 23.6.21 PCASI 3443
		else if ("SmartCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("SmartCheck1_CreditRemarks",true);
			formObject.setLocked("SmartCheck1_FCURemarks",true);
		}

		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadView(pEvent.getSource().getName());
			formObject.setLocked("OECD_Frame8",true);
		}
		//++Below code added by nikhil 13/11/2017 for Code merge
		//changes done by shweta for jira no 2372
		else if ("CustDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			//code changed by nikhil for CPV Changes 16-04-2019
			LoadPickList("cmplx_CustDetailVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");
			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_CustDetailVerification_mobno1_ver","cmplx_CustDetailVerification_mobno2_ver","cmplx_CustDetailVerification_dob_ver","cmplx_CustDetailVerification_POBoxno_ver","cmplx_CustDetailVerification_emirates_ver","cmplx_CustDetailVerification_persorcompPOBox_ver","cmplx_CustDetailVerification_hcountrytelno_ver","cmplx_CustDetailVerification_hcontryaddr_ver","cmplx_CustDetailVerification_email1_ver","cmplx_CustDetailVerification_email2_ver","cmplx_CustDetailVerification_Mother_name_ver");//pcasi-1003
			LoadPickList("cmplx_CustDetailVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");
			LoadPicklistVerification(LoadPicklist_Verification);
			//code changed by nikhil for CPV Changes 16-04-2019
			formObject.setLocked("CustDetailVerification_Frame1",true);

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
			formObject.setLocked("HomeCountryVerification_Frame1",true); //by jahnavi for JIRA 3063
			formObject.setVisible("HomeCountryVerification",false); // hritik 22.8.21 -3779
			// enable_homeVerification();
			
		}//by shweta
		else if ("GuarantorVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("GuarantorVerification_Frame1",true);
			
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
			//nikhil code moved from frame expand event for PCAS-2239
			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_OffVerification_fxdsal_ver","cmplx_OffVerification_accpvded_ver","cmplx_OffVerification_desig_ver","cmplx_OffVerification_doj_ver","cmplx_OffVerification_cnfminjob_ver");
			LoadPicklistVerification(LoadPicklist_Verification);
			LoadPickList("cmplx_OffVerification_colleaguenoverified", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_CPVVeri with (nolock) where Sno != 3 order by code"); //Arun modified on 14/12/17
			//changed by nikhil for CPV changes 17-04
			LoadPickList("cmplx_OffVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) where IsActive='Y' and For_custdetail_only='N' order by code");
			//below code by saurabh on 28th nov 2017.
			LoadPickList("cmplx_OffVerification_offtelnovalidtdfrom", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_offNoValidatedFrom with (nolock)");
			LoadPickList("cmplx_OffVerification_hrdnocntctd", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_HRDContacted with (nolock)");
			//cmplx_OffVerification_fxdsal_val
			//enable_officeVerification();
			// added by abhishek to disable office verification
			formObject.setLocked("OfficeandMobileVerification_Frame1", true);
			formObject.setEnabled("OfficeandMobileVerification_Enable", true);
			formObject.setLocked("cmplx_OffVerification_fxdsal_override", true);
			formObject.setLocked("cmplx_OffVerification_accprovd_override", true);
			formObject.setLocked("cmplx_OffVerification_desig_override", true);
			formObject.setLocked("cmplx_OffVerification_doj_override", true);
			formObject.setLocked("cmplx_OffVerification_colleaguenoverified", true); // hritik 24.6.21 pcasi-3442
			formObject.setLocked("cmplx_OffVerification_cnfrmjob_override", true);
			
 			//-- Above code added by abhishek to make fields enable/disable/hidden as per FSD 2.7.3
			//--Above code added by nikhil 13/11/2017 for Code merge
			//LoadPickList("cmplx_OffVerification_desig_upd", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Designation with (nolock) where isActive='Y' order by Code");
			
		}
		else if ("Employment_Verification".equalsIgnoreCase(pEvent.getSource().getName()) || 
				"EmploymentVerification_s2".equalsIgnoreCase(pEvent.getSource().getName())
				|| "EmploymentVerification_s2_Frame2".equalsIgnoreCase(pEvent.getSource().getName())) {
			
			//formObject.fetchFragment("Employment_Verification", "EmploymentVerification_s2", "q_empVer_sp")
			formObject.setLocked("EmploymentVerification_s2_search_TL_number",true);
			formObject.setEnabled("EmploymentVerification_s2_search_TL_number",false);
			PersonalLoanS.mLogger.info("Inside cad1 employmentverf");
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
			//added by rishabh
			PersonalLoanS.mLogger.info("CHECKING RISHABH");
			PersonalLoanS.mLogger.info("cpv_dec_rishabh");
			cpv_Decision();
			PersonalLoanS.mLogger.info("cpv_dec_rishabh1");
			//end
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
        	fragment_ALign("Decision_Label3,cmplx_Decision_Decision#DecisionHistory_Label1,cmplx_Decision_ReferTo#DecisionHistory_Label11,DecisionHistory_DecisionReasonCode#DecisionHistory_Label35,cmplx_Decision_SetReminder#DecisionHistory_Label12,cmplx_Decision_NoofAttempts#\n#Decision_Label4,cmplx_Decision_REMARKS#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
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
		else if ("FinacleCore".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("FinacleCore_Frame1",true);
			formObject.setEnabled("FinacleCore_save", false);
			formObject.setEnabled("FinacleCore_Combo1", false);
		}
		else if ("FinacleCRMCustInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadinFinacleCRNGrid(formObject);
			formObject.setLocked("FinacleCRMCustInfo_Frame1",true);
		}
		else if ("MOL1".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("MOL1_Frame1",true);
			formObject.setEnabled("cmplx_MOL_rem",false);
		}

		else if ("WorldCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("WorldCheck1_Frame1",true);
			formObject.setEnabled("WorldCheck1_Gender", false);
			formObject.setEnabled("WorldCheck1_rem", false);
		}
		/* else if ("IncomingDoc".equalsIgnoreCase(pEvent.getSource().getName())) {
		 fetchIncomingDocRepeater();
		 }*/
		/* else if ("Fpu_Grid".equalsIgnoreCase(pEvent.getSource().getName())) {
			 PersonalLoanS.mLogger.info("fpu grid clicked");
				
				formObject.setLocked("cmplx_FPU_Grid_Officer_Name", true);
				LoadPickList("cmplx_FPU_Grid_Officer_Name", "select UserName from PDBUser where UserIndex in (select UserIndex from PDBGroupMember where GroupIndex=(select GroupIndex from PDBGroup where GroupName in ('FCU','FPU')))");
				
		 }*/
		/* changes done by shweta to hide credit card fields from Loan/card details fragment start*/
		formObject.setVisible("cmplx_LoanandCard_cardtype_ver", false);
		formObject.setVisible("cmplx_LoanandCard_cardlimit_ver", false);
		/* changes done by shweta to hide credit card fields from Loan/card details fragment end*/

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
		//Below code added by siva on 01112019 for PCAS-1288
		FormReference formObject= FormContext.getCurrentInstance().getFormReference();
		String Query ="Select CA_Refer_DDVT from NG_PL_EXTTABLE where PL_wi_name ='"+formObject.getWFWorkitemName()+"'";
		int count=0;
		try
		{
		PersonalLoanS.mLogger.info("query name :"+Query);
		List<List<String>> result=formObject.getDataFromDataSource(Query);
		if(!result.isEmpty()){
			if("Y".equalsIgnoreCase(result.get(0).get(0)))
			{
				count++;
			}
		}
		}
		catch(Exception ex)
		{
			PersonalLoanS.mLogger.info("Exception occured in Check_CPV_Refer_DDVT" +ex.getMessage());
		}
		if(count>0)
		{
			String AlertMsg="Workitem Referred to DDVT by UW Unit!";
			throw new ValidatorException(new FacesMessage(AlertMsg));
		}
		//ended by siva on 01112019 for PCAS-1288	

	}


	public void saveFormStarted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		/*FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		if(formObject.isVisible("OfficeandMobileVerification_Frame1")==true)
		{
			if("".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_desig_upd")) )
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
		}*/
		

	}


	public void submitFormCompleted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		PersonalLoanS.mLogger.info( "PL_CPV"+"Inside submitFormCompleted()");
		
	}


	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//added by isha for PCSP-459
		//commented by nikhil for PCAS-2814
		/*if("Approve".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")))
		{
		Other_Detail_Match_check();
		}
		CustomSaveForm();*/
		
		//formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
		try
		{
			//added by rishabh 17-10-2019
			
			if( !"CPV Checker".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_ReferTo")) || !"CPV_Checker".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_ReferTo")))
			{
				formObject.setNGValue("IS_CPV", "N");
			}

			//end rishabh
			//code sync with CC
			if(formObject.isVisible("OfficeandMobileVerification_Frame1")==true && "Approve".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")))//by shweta ,corrected decision id 
			{
				PersonalLoanS.mLogger.info("Inside submitFormstarted to validate office verification");
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("Office_verification");
			}

			PersonalLoanS.mLogger.info("Inside  submitFormStarted ");
			PersonalLoanS.mLogger.info("Inside  value of decision CPV" + formObject.getNGValue("cmplx_Decision_Decision"));
			formObject.setNGValue("CPV_Limit", formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
			formObject.setNGValue("CPV_DECISION", formObject.getNGValue("cmplx_Decision_Decision"));//Done by aman 17/12
			formObject.setNGValue("Mail_Priority", formObject.getUserName());
		/*String decision = formObject.getNGValue("cmplx_Decision_Decision");
		String query = "update ng_pl_exttable set CPV_DECISION='"+decision+"' where PL_wi_name = '"+formObject.getWFWorkitemName()+"'";
		PersonalLoanS.mLogger.info("query for cpv decision "+query);
		formObject.saveDataIntoDataSource(query);*/
		
		PersonalLoanS.mLogger.info( "cmplx_Decision_Decision:" +  formObject.getNGValue("cmplx_Decision_Decision")+ "#ControlName#");

		//formObject.setNGValue("CPV_dec", formObject.getNGValue("cmplx_Decision_Decision"));


		//commented by shweta for seq no 138
		/* if("Approve Sub to CIF".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")))
		    {
			 PersonalLoanS.mLogger.info("Inside  Approve Sub to CIF ");
		        formObject.setNGValue("IS_Approve_Cif","Y");
		        PersonalLoanS.mLogger.info("update  Approve Sub to CIF " + formObject.getNGValue("cmplx_Decision_Decision"));
		    }*/
		 if("Approve".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")) || "Reject".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")))
		  {
			 PersonalLoanS.mLogger.info("Inside  Approve CPV");
			  formObject.setNGValue("q_hold1",1);
			  formObject.setNGValue("VAR_INT4",1);
			  PersonalLoanS.mLogger.info("Inside NA Approve CPV " + formObject.getNGValue("q_hold1"));
			  //Code sync for PCAS-2926
			  if("Re-Initiate".equalsIgnoreCase(formObject.getNGValue("Reject_DeC"))){
					formObject.setNGValue("Reject_DeC","");
			 }
		  }
		 	formObject.setNGValue("cpv_user", formObject.getUserName());
			//cmplx_CustDetailVerification_email1_ver
			//code sync with CC
			CustomSaveForm();
			LoadReferGrid();
			saveIndecisionGrid();
			//Done By aman for Sprint2
			String squeryrefer = "select referTo from NG_RLOS_GR_DECISION where workstepName like '%CPV%' and dec_wi_name= '" + formObject.getWFWorkitemName() + "' order by dateLastChanged desc ";
			List<List<String>> FCURefer = formObject.getNGDataFromDataCache(squeryrefer);
			PersonalLoanS.mLogger.info("RLOS COMMON"+ " iNSIDE prev_loan_dbr+ " + FCURefer);
			String referto="";
			if (FCURefer != null && FCURefer.size() > 0) {
				referto = FCURefer.get(0).get(0);
			}
			if("FPU".equalsIgnoreCase(referto)){//code sync with CC
				formObject.setNGValue("RefFrmCPV", "Y");
			}		 
		 //below code added by siva on 04112019 for PCAS-1268
		 if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_SetReminder")) && "CPV Hold".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")))
		 {	String Email_sub="REMINDER- case no "+formObject.getWFWorkitemName()+" pending for the action of "+formObject.getUserName();
			//CreditCard.mLogger.info("Email_sub: "+ Email_sub);
			String Email_message="<html><body>Dear User, \n Kindly call the HR/office back. \n \n Remarks: "+formObject.getNGValue("cmplx_OffVerification_reamrks")+". \n Decision Remarks � "+formObject.getNGValue("cmplx_Decision_REMARKS")+". </body></html>";
			//CreditCard.mLogger.info("Email_sub: "+ Email_message);
			String query = "select count(WI_Name) from NG_RLOS_EmailReminder with(nolock) where WI_Name='"+formObject.getWFWorkitemName()+"' and Email_Name='CPV'";
			List<List<String>> Emailrecords = formObject.getDataFromDataSource(query);
			String emailTo = "select Top 1 ConstantValue from CONSTANTDEFTABLE where constantName='CONST_cpv_mailTo'";
			List<List<String>> mailTo = formObject.getNGDataFromDataCache(emailTo);
			String mailto_cpv=mailTo.get(0).get(0);
			PersonalLoanS.mLogger.info("@sagarika"+ " mail to" + mailto_cpv);
			String email_From = "select Top 1 ConstantValue from CONSTANTDEFTABLE where constantName='CONST_cpv_mailFrom'";
			List<List<String>> mail_From = formObject.getNGDataFromDataCache(email_From);
			String mail_from_cpv=mail_From.get(0).get(0);
			PersonalLoanS.mLogger.info("@sagarika"+ " mail fromssss" + mail_from_cpv);
			PersonalLoanS.mLogger.info("query NG_RLOS_EmailReminder count is: "+query);
			PersonalLoanS.mLogger.info("query NG_RLOS_EmailReminder count result: "+Emailrecords);
		
			try{
				String reminderInsertQuery="";
				if(Emailrecords!=null && Emailrecords.size()>0 && Emailrecords.get(0)!=null ){
					String EmailRow_count=Emailrecords.get(0).get(0);
					PersonalLoanS.mLogger.info("query NG_RLOS_EmailReminder count EmailRow_count: "+EmailRow_count);
					if("0".equalsIgnoreCase(EmailRow_count)){
						reminderInsertQuery="insert into NG_RLOS_EmailReminder(Email_Name,Email_Status,Email_To,Email_From,EmailSubject,EmailMessage,SetReminder,WI_Name,Workstep_Name)"
								+" values('CPV','P','"+mailto_cpv+"','"+mail_from_cpv+"','"+Email_sub+"','"+Email_message+"','"+formObject.getNGValue("cmplx_Decision_SetReminder")+"','"+formObject.getWFWorkitemName()+"','"+formObject.getWFActivityName()+"')";
					}
					else{
						reminderInsertQuery="update NG_RLOS_EmailReminder set Email_Status='P' where WI_Name='"+formObject.getWFWorkitemName()+"' and Email_Name='CPV'";
					}
					PersonalLoanS.mLogger.info("Query to insert NG_RLOS_EmailReminder: "+ reminderInsertQuery);
					formObject.saveDataIntoDataSource(reminderInsertQuery);
				}
			}
			catch(Exception e){
				PersonalLoanS.mLogger.info("qException occured in CPV reminder Email insert:"+query);
			}}
		
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
