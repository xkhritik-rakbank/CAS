
package com.newgen.omniforms.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.faces.validator.ValidatorException;
import org.apache.log4j.Logger;

import com.newgen.custom.Common_Utils;
import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;


public class PL_Disbursal_Checker extends PLCommon implements FormListener
{
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	Logger mLogger=PersonalLoanS.mLogger;

	public void formLoaded(FormEvent pEvent)
	{
		FormConfig objConfig = FormContext.getCurrentInstance().getFormConfig();
        objConfig.getM_objConfigMap().put("PartialSave", "true");
        makeSheetsInvisible("Tab1", "8,9");//Changes done by shweta for jira #1768 to hide CPV tab

	}


	public void formPopulated(FormEvent pEvent) 
	{
		FormReference formObject= FormContext.getCurrentInstance().getFormReference(); 
		try{
			new PersonalLoanSCommonCode().setFormHeader(pEvent);
			//added by yash
			formObject.setNGValue("cmplx_LimitInc_CIF",formObject.getNGValue("cmplx_Customer_CIFNO"));
			formObject.setNGValue("cmplx_LimitInc_CustomerName",formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_MiddleName")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme"));
			//ended by yash
			String cc_waiv_flag = formObject.getNGValue("is_cc_waiver_require");
			if(cc_waiv_flag.equalsIgnoreCase("N")){
			formObject.setVisible("Card_Details", true);
			}
			if(!"".equals(formObject.getNGValue("cmplx_Customer_NEP"))&& !"--Select--".equals(formObject.getNGValue("cmplx_Customer_NEP")) && formObject.getNGValue("cmplx_Customer_NEP")!=null){
				formObject.setNGValue("Customer_Type","NEP new");
			}
			else if(NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
				formObject.setNGValue("Customer_Type","NTB new");
			}
			else{
				formObject.setNGValue("Customer_Type","Existing");
			}
			PersonalLoanS.mLogger.info("PL Initiation"+ "value of los:"+formObject.getNGValue("Customer_Type"));

			String Current_LOS=formObject.getNGValue("cmplx_EmploymentDetails_LOS");
			String Previous_LOS=formObject.getNGValue("cmplx_EmploymentDetails_LOSPrevious");

			String JobDuration = Current_LOS+" "+Previous_LOS; 
			formObject.setNGValue("Emp_JobDuration",JobDuration); 
			PersonalLoanS.mLogger.info("PL Initiation"+ "value of los:"+formObject.getNGValue("Emp_JobDuration"));
			
			//++Below code added by abhishek for disbursal
			 formObject.setVisible("CC_Creation", false);
			 formObject.setVisible("Limit_Inc", false);
			//--Above code added by abhishek for disbursal
				formObject.setNGValue("Mandatory_Frames", NGFUserResourceMgr_PL.getGlobalVar("PL_Disbursal_Checker"));
				new PersonalLoanSCommonCode().setFormHeader(pEvent);

		}catch(Exception e)
		{
			PersonalLoanS.mLogger.info("PL Initiation"+ "Exception:"+e.getMessage());
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
		FormReference formObject= FormContext.getCurrentInstance().getFormReference();
		PersonalLoanS.mLogger.info(" In PL_Initiation eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		Common_Utils common=new Common_Utils(mLogger);

		
		
		
	
		if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadView(pEvent.getSource().getName());
			loadPicklistCustomer();
			formObject.setLocked("Customer_Frame1",true);
			formObject.setLocked("cmplx_Customer_NEP",true);

			if(!"".equals(formObject.getNGValue("cmplx_Customer_NEP"))&& !"--Select--".equals(formObject.getNGValue("cmplx_Customer_NEP")) && formObject.getNGValue("cmplx_Customer_NEP")!=null){
				formObject.setVisible("cmplx_Customer_EIDARegNo",true);
				formObject.setVisible("Customer_Label56",true);
				//formObject.setLocked("cmplx_Customer_EIDARegNo",true);
			}
			else {
				formObject.setVisible("Customer_Label56",false);
				formObject.setVisible("cmplx_Customer_EIDARegNo",false);
			}
		}	

		else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Product_Frame1",true);
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct with(nolock)");
			LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_ApplicationType with (nolock) order by code");
			int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			if(n>0){
				for(int i=0;i<n;i++)
				{
					if(NGFUserResourceMgr_PL.getGlobalVar("PL_LimitIncrease").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2)))
					{
						
						formObject.setVisible("cmplx_LimitInc_CurrentLimit", false);
						formObject.setVisible("cmplx_LimitInc_New_Limit", false);
						formObject.setVisible("cmplx_LimitInc_LimitExpiryDate_button", false);
						

					}
					if(NGFUserResourceMgr_PL.getGlobalVar("PL_ProductUpgrade").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2)))
					{
						formObject.setVisible("cmplx_LimitInc_ExistingCardProduct", false);
						formObject.setVisible("cmplx_LimitInc_New_Limit", false);
					}
					if(NGFUserResourceMgr_PL.getGlobalVar("ProductUpgradewithLimit").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2)))
						
					{
						formObject.setVisible("cmplx_LimitInc_CurrentLimit", false);
						formObject.setVisible("cmplx_LimitInc_New_Limit", false);
						formObject.setVisible("cmplx_LimitInc_LimitExpiryDate_button", false);
						formObject.setVisible("cmplx_LimitInc_ExistingCardProduct", false);
						formObject.setVisible("cmplx_LimitInc_New_Limit", false);

					}	
				}

			}
		}
		// ended by yash

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

		}

		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadView(pEvent.getSource().getName());
			formObject.setLocked("EMploymentDetails_Frame1",true);
			loadPicklist4();
			Fields_ApplicationType_Employment();
		}

		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
			loadPicklistELigibiltyAndProductInfo();
			loadEligibilityData();

		}

		else if ("LoanDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist_LoanDetails();
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
			//formObject.setLocked("LoanDetails_Button1", false);
			formObject.setEnabled("LoanDetails_Button1", true);

			
		}
		
		else if ("Loan_Disbursal".equalsIgnoreCase(pEvent.getSource().getName())) {
			FetchingDecision();
			loadingDecision();
			formObject.setLocked("Loan_Disbursal_Frame2",true);
			//formObject.setLocked("Loan_Disbursal_save",false);
			int framestate3=formObject.getNGFrameState("Alt_Contact_container");
			if(framestate3 == 0){
				PersonalLoanS.mLogger.info("Alt_Contact_container");
			}
			else {
				formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
				formObject.setNGFrameState("Alt_Contact_container",0);
				alignDemographiTab(formObject);
				/*
				formObject.setTop("Alt_Contact_container",formObject.getTop("Address_Details_container")+formObject.getHeight("Address_Details_container")+70);
				formObject.setTop("Supplementary_Container",formObject.getTop("ReferenceDetails")+250);
				formObject.setTop("CardDetails", formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container")+20);
				formObject.setTop("FATCA", formObject.getTop("CardDetails")+30);
				formObject.setTop("KYC", formObject.getTop("FATCA")+20);
				formObject.setTop("OECD", formObject.getTop("KYC")+20);
				*/
				PersonalLoanS.mLogger.info("fetched address details");
			}
			int frameStateEmp = formObject.getNGFrameState("EmploymentDetails");
			if(frameStateEmp==0){
				PersonalLoanS.mLogger.info("fetched Employment Details");
			}
			else{
				formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmpDetails");
			}
			int frameStateLoan = formObject.getNGFrameState("LoanDetails");
			if(frameStateLoan==0){
				PersonalLoanS.mLogger.info("fetched Loan Details");
			}
			else{
				formObject.fetchFragment("LoanDetails", "LoanDetails", "q_Loan");
				formObject.setNGFrameState("LoanDetails", 0);
				adjustFrameTops("LoanDetails,Risk_Rating");
			}
			//added by akshay on 29/12/17
			int framestate2=formObject.getNGFrameState("EligibilityAndProductInformation");
			if(framestate2 != 0){
			formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligibilityProdInfo");
			}
			int frameStateLiab = formObject.getNGFrameState("InternalExternalLiability");
			if(frameStateLiab==0){
				PersonalLoanS.mLogger.info("fetched Liability Details");
			}
			else{
				formObject.fetchFragment("InternalExternalLiability", "Liability_New", "q_Liabilities");
			}
			// code added by abhishek on 8 dec as per FSD 2.7
			PersonalLoanS.mLogger.info("retain loan amount checkbox value::"+formObject.getNGValue("AlternateContactDetails_RetainAccIfLoanReq"));
			if(NGFUserResourceMgr_PL.getGlobalVar("PL_false").equalsIgnoreCase(formObject.getNGValue("AlternateContactDetails_RetainAccIfLoanReq"))){
				if("Y".equalsIgnoreCase(formObject.getNGValue("cmplx_LoanDisb_updateCustomerFlag"))){
					formObject.setEnabled("DecisionHistory_updcust_loan", true);
					formObject.setEnabled("DecisionHistory_updacct_loan", true);
					if("Y".equalsIgnoreCase(formObject.getNGValue("cmplx_LoanDisb_updateAccountFlag"))){
						formObject.setEnabled("DecisionHistory_chqbook_loan", true);
						if("Y".equalsIgnoreCase(formObject.getNGValue("cmplx_LoanDisb_ChequeBookFlag"))){
							formObject.setEnabled("DecisionHistory_chqbook_loan",false);
							if(NGFUserResourceMgr_PL.getGlobalVar("PL_Islamic").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 0))){
								if("Y".equalsIgnoreCase(formObject.getNGValue("cmplx_LoanDisb_contractCreationFlag"))){
									formObject.setLocked("Loan_Disbursal_Button1", true);
									
								}else{
									formObject.setVisible("Loan_Disbursal_Button1", true);
									formObject.setLocked("Loan_Disbursal_Button1", false);
								}
							}else{
								formObject.setLocked("Loan_Disbursal_Button1", true);
								formObject.setVisible("Loan_Disbursal_Button1", false);
							}
						}
						else{
							//added by nikhil for proc-7528
							if("Islamic".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 0)))
							{
								if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")))
										formObject.setVisible("DecisionHistory_chqbook_loan", false);
								else 
									formObject.setVisible("DecisionHistory_chqbook_loan", true);
	
								formObject.setLocked("Loan_Disbursal_Button1", false);
								formObject.setVisible("Loan_Disbursal_Button1", true);
								
							}
							else if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")))
							{
								//formObject.setVisible("DecisionHistory_chqbook_loan", true);
								formObject.setLocked("DecisionHistory_chqbook_loan", false);
								formObject.setLocked("Loan_Disbursal_Button1", true);
								formObject.setVisible("Loan_Disbursal_Button1", false);
							}
							 
							else{
								formObject.setVisible("DecisionHistory_chqbook_loan", false);
								formObject.setLocked("Loan_Disbursal_Button1", false);
								formObject.setVisible("Loan_Disbursal_Button1", false);
							}
							
						}
					}else{
						//formObject.setLocked("DecisionHistory_updacct_loan", false);
						formObject.setEnabled("DecisionHistory_chqbook_loan", false);
						formObject.setLocked("Loan_Disbursal_Button1", true);
						formObject.setVisible("Loan_Disbursal_Button1", false);
					}
				}
				else{
					formObject.setLocked("DecisionHistory_updcust_loan", false);
					formObject.setEnabled("DecisionHistory_updacct_loan", false);
					formObject.setEnabled("DecisionHistory_chqbook_loan", false);
					formObject.setLocked("Loan_Disbursal_Button1", true);
					formObject.setVisible("Loan_Disbursal_Button1", false);
				}
				
				
				
				
			}else{
				formObject.setLocked("DecisionHistory_updcust_loan", true);
				formObject.setLocked("DecisionHistory_updacct_loan", true);
				formObject.setLocked("DecisionHistory_chqbook_loan", true);
				if(NGFUserResourceMgr_PL.getGlobalVar("PL_Islamic").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 0))){
					if("Y".equalsIgnoreCase(formObject.getNGValue("cmplx_LoanDisb_contractCreationFlag"))){
						formObject.setLocked("Loan_Disbursal_Button1", true);
					}else{
						formObject.setLocked("Loan_Disbursal_Button1", false);
						formObject.setVisible("Loan_Disbursal_Button1", true);
					}
				}else{
						formObject.setLocked("Loan_Disbursal_Button1", true);
				}
					
				
			}
			formObject.setLocked("Loan_Disbursal_save",false);
			if(!"true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")))
			{
				formObject.setVisible("DecisionHistory_chqbook_loan",false);
			}
			//added for testing
			formObject.setLocked("Loan_Disbursal_save",false);
			
	
		}

		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadView(pEvent.getSource().getName());

			loadPicklist_Address();
			formObject.setLocked("AddressDetails_Frame1",true);
		}

		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadView(pEvent.getSource().getName());

			//added By Tarang as per drop 4 point 1 started on 26/02/2018
			PersonalLoanS.mLogger.info("Inside alternate contact details fragment (Disbursal) :"+formObject.getNGValue("Card_Dispatch_Option"));
			if("Branch".equalsIgnoreCase(formObject.getNGValue("Card_Dispatch_Option"))){
				formObject.setLocked("AltContactDetails_Frame1",false);
			}
			else{
				formObject.setLocked("AltContactDetails_Frame1",true);
			}
			//added By Tarang as per drop 4 point 1 ended on 26/02/2018
			LoadpicklistAltcontactDetails();
			//below code added by siva on 01112019 for PCAS-1401
			AirArabiaValid();
			enrollrewardvalid();
			//Code ended by siva on 01112019 for PCAS-1401
		}
		else if ("RiskRating".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("RiskRating_Frame1",true);
			loadPicklistRiskRating(); //jahnavi
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
			Loadpicklistfatca();
			formObject.setLocked("FATCA_Frame6",true);
			formObject.setLocked("cmplx_FATCA_SignedDate",true);
			formObject.setLocked("cmplx_FATCA_ExpiryDate",true);
		}

		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("KYC_Frame7",true);
		}

		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadView(pEvent.getSource().getName());

			loadPickListOECD();
			formObject.setLocked("OECD_Frame8",true);
		}

		else if ("PartMatch".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("PartMatch_Frame1",true);
			formObject.setLocked("PartMatch_Dob",true);
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
		}

		else if ("MOL1".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("MOL1_Frame1",true);
		}

		else if ("WorldCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("WorldCheck1_Frame1",true);
		}
		
		//code changes by bandana start
		else if ("CC_Loan".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setLocked("cmplx_CC_Loan_mchequeno",true);
			formObject.setLocked("cmplx_CC_Loan_mchequeDate",true);
			formObject.setLocked("cmplx_CC_Loan_mchequestatus",true);
			formObject.setLocked("CC_Loan_Frame1",true);
			loadPicklist_ServiceRequest();
		}	
		//code changes by bandana ends
		
		else if ("RejectEnq".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("RejectEnq_Frame1",true);
		}

		else if ("SalaryEnq".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("SalaryEnq_Frame1",true);
		}

		else if ("CustDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("CustDetailVerification_Frame1",true);
			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_CustDetailVerification_mobno1_ver","cmplx_CustDetailVerification_mobno2_ver","cmplx_CustDetailVerification_dob_ver","cmplx_CustDetailVerification_POBoxno_ver","cmplx_CustDetailVerification_emirates_ver","cmplx_CustDetailVerification_persorcompPOBox_ver","cmplx_CustDetailVerification_hcountrytelno_ver","cmplx_CustDetailVerification_hcontryaddr_ver","cmplx_CustDetailVerification_email1_ver","cmplx_CustDetailVerification_email2_ver","cmplx_CustDetailVerification_Mother_name_ver");

			LoadPicklistVerification(LoadPicklist_Verification);
			LoadPickList("cmplx_CustDetailVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");

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
		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			notepad_load();
			 notepad_withoutTelLog();
		
		}
		
		/*else if ("IncomingDoc".equalsIgnoreCase(pEvent.getSource().getName())){
			fetchIncomingDocRepeater();
			formObject.setVisible("cmplx_DocName_OVRemarks", false);
			formObject.setVisible("cmplx_DocName_OVDec",false);
			formObject.setVisible("cmplx_DocName_Approvedby",false);
		}*/

		else if ("SmartCheck".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("SmartCheck_Frame1",true);
		}

		else if ("Compliance".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("Compliance_Frame1",true);
		}

		else if ("FCU_Decision".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("FCU_Decision_Frame1",true);
		}
		else if ("ReferHistory".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setLocked("ReferHistory_Frame1",true);
		}

		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			//for decision fragment made changes 8th dec 2017
			 loadingDecision();
			formObject.setLocked("cmplx_Decision_New_CIFNo",true);
			formObject.setLocked("cmplx_Decision_IBAN",true);

			//formObject.setVisible("Gen_welc_letter", false);
			
//			fragment_ALign("Decision_Label3,cmplx_Decision_Decision#DecisionHistory_Label1,cmplx_Decision_ReferTo#DecisionHistory_Label11,DecisionHistory_DecisionReasonCode#DecisionHistory_Label41,DecisionHistory_DecisionSubReason#\n#Decision_Label4,cmplx_Decision_REMARKS#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#Decision_ListView1#\n#DecisionHistory_save#Gen_welc_letter","DecisionHistory");//\n for new line
//			formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
//			formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
//			formObject.setTop("ReferHistory", formObject.getTop("DecisionHistory")+formObject.getHeight("DecisionHistory")+20);
//			formObject.setTop("Postdisbursal_Checklist", formObject.getTop("ReferHistory")+formObject.getHeight("ReferHistory")+60);
//			formObject.setVisible("Postdisbursal_Checklist", true);
//			//for decision fragment made changes 8th dec 2017
			loadPicklist1();

		} 
		else if ("PostDisbursal_Checklist" .equalsIgnoreCase(pEvent.getSource().getName())) {
			if( !"".equalsIgnoreCase(formObject.getNGValue("cmplx_LoanDisb_AgreementID")))
			{
				
				
				formObject.setLocked("PostDisbursal_Checklist_Frame1",false);
				formObject.setEnabled("PostDisbursal_Checklist_Frame1",true);
				formObject.setLocked("PostDisbursal_Checklist_Frame2",true);
				formObject.setEnabled("PostDisbursal_Checklist_Frame2",false);
				formObject.setLocked("PostDisbursal_Checklist_Frame3",false);
				formObject.setEnabled("PostDisbursal_Checklist_Frame3",true);
				formObject.setLocked("PostDisbursal_Checklist_Frame4",true);
				formObject.setEnabled("PostDisbursal_Checklist_Frame4",false);
				PersonalLoanS.mLogger.info("with agreement id");
			}
			else if("".equalsIgnoreCase(formObject.getNGValue("cmplx_LoanDisb_AgreementID")))
			
			{
				formObject.setLocked("PostDisbursal_Checklist_Frame2",true);
				formObject.setEnabled("PostDisbursal_Checklist_Frame2",false);
				formObject.setLocked("PostDisbursal_Checklist_Frame1",true);
				formObject.setEnabled("PostDisbursal_Checklist_Frame1",false);
				formObject.setLocked("PostDisbursal_Checklist_Frame3",true);
				formObject.setEnabled("PostDisbursal_Checklist_Frame3",false);
				formObject.setLocked("PostDisbursal_Checklist_Frame4",true);
				formObject.setEnabled("PostDisbursal_Checklist_Frame4",false);
				PersonalLoanS.mLogger.info("without agreement id");
			}
		
		
			}
		
		
		// fcu fragments start
		else if ("CustDetailVerification1".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("CustDetailVerification1_Frame1",true);
			
		}
		else if ("BussinessVerification1".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("BussinessVerification1_Frame1",true);
		}
		else if ("Fpu_Grid".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("cmplx_FPU_Grid_Officer_Name", true);
			LoadPickList("cmplx_FPU_Grid_Officer_Name", "select ' --Select-- 'as UserName union select UserName from PDBUser where UserIndex in (select UserIndex from PDBGroupMember where GroupIndex=(select GroupIndex from PDBGroup where GroupName in ('FCU','FPU')))");
		}
	}
	
	public void loadingDecision(){
		FormReference formObject= FormContext.getCurrentInstance().getFormReference();
		PersonalLoanS.mLogger.info("***********Inside decision history of csm");
		fragment_ALign("DecisionHistory_Label10,cmplx_Decision_New_CIFNo#DecisionHistory_Label7,cmplx_Decision_AccountNo#DecisionHistory_Label6,cmplx_Decision_IBAN#Decision_Label1,cmplx_Decision_VERIFICATIONREQUIRED#\n#Decision_Label3,cmplx_Decision_Decision#DecisionHistory_Label40,cmplx_Decision_DecDisbChecker#DecisionHistory_Label1,cmplx_Decision_ReferTo#DecisionHistory_Label11,DecisionHistory_DecisionReasonCode#\n#DecisionHistory_Label41,DecisionHistory_DecisionSubReason#Decision_Label4,cmplx_Decision_REMARKS#\n#cmplx_Decision_MultipleApplicantsGrid#\n#DecisionHistory_Button1#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line

		formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
		formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
		formObject.setTop("ReferHistory", formObject.getTop("DecisionHistory")+formObject.getHeight("DecisionHistory")+20);
		formObject.setTop("Postdisbursal_Checklist", formObject.getTop("ReferHistory")+formObject.getHeight("ReferHistory")+60);
		formObject.setVisible("Postdisbursal_Checklist", true);
		
		//for decision fragment made changes 8th dec 2017
		loadPicklist1();
		/*String appType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,4);
		if(appType.contains("TKO")){
			formObject.setLocked("cmplx_Decision_DecDisbChecker",false);
		}*/
		if(NGFUserResourceMgr_PL.getGlobalVar("PL_false").equalsIgnoreCase(formObject.getNGValue("AlternateContactDetails_RetainAccIfLoanReq"))){
			formObject.setVisible("DecisionHistory_Button3", false);
			formObject.setVisible("DecisionHistory_updcust", false);
			formObject.setVisible("DecisionHistory_chqbook", false);
		}
		else{
			formObject.setVisible("DecisionHistory_chqbook", false);
		}
		String newCif = formObject.getNGValue("cmplx_Decision_New_CIFNo");
		String newAcc = formObject.getNGValue("cmplx_Decision_AccountNo");
		if(!"".equalsIgnoreCase(newCif)&& !"".equalsIgnoreCase(newAcc)){
			formObject.setVisible("DecisionHistory_Button2", false);
		}
	}

	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{
		//PersonalLoanS.mLogger.info("Inside PL_Initiation eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		switch(pEvent.getType())
		{	

		case FRAME_EXPANDED:
			//PersonalLoanS.mLogger.info(" In PL_Iniation eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

			new PersonalLoanSCommonCode().FrameExpandEvent(pEvent);					
			break;

		case FRAGMENT_LOADED:						
			fragment_loaded(pEvent);
			break;

		case MOUSE_CLICKED:						
			new PersonalLoanSCommonCode().mouse_Clicked(pEvent);
			break;
			//ended code merged

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
		CustomSaveForm();
		//empty method
	}


	public void saveFormStarted(FormEvent arg0) throws ValidatorException {
		//empty method
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
if(formObject.getNGValue("Account_Number").equalsIgnoreCase(""))
			
		{
			String query="select  New_Account_Number from ng_rlos_decisionHistory where wi_name ='"+formObject.getWFWorkitemName()+"'";
			List<List<String>> acc_num=formObject.getDataFromDataSource(query);
			formObject.setNGValue("Account_Number",acc_num.get(0).get(0));
		}
		if(formObject.getNGValue("Contract_ID").equalsIgnoreCase(""))
			
		{
			String query="select AgreementID,* from ng_rlos_LoanDisbursal where wi_name ='"+formObject.getWFWorkitemName()+"'";
			List<List<String>> acc_num=formObject.getDataFromDataSource(query);
			formObject.setNGValue("Contract_ID",acc_num.get(0).get(0));
		}
		if(formObject.getNGValue("Branch").equalsIgnoreCase(""))
			
		{
			String query="select CustomerDomicileBranch,* from ng_RLOS_AltContactDetails  where wi_name ='"+formObject.getWFWorkitemName()+"'";
			List<List<String>> acc_num=formObject.getDataFromDataSource(query);
			formObject.setNGValue("Branch",acc_num.get(0).get(0));
		}
		PersonalLoanS.mLogger.info(formObject.getNGValue("Account_Number")+formObject.getNGValue("")+formObject.getNGValue("Branch"));
	}


	public void submitFormCompleted(FormEvent arg0) throws ValidatorException {
		//empty method

	}


	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try
		{
			CustomSaveForm();
			formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
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
			} 
			LoadReferGrid();//added by akshay on 6/4/18 for proc 7641
			saveIndecisionGrid();
			//below code added by nikhil to 
			if(formObject.getNGValue("AlternateContactDetails_carddispatch").equalsIgnoreCase("998"))
			{
				formObject.setNGValue("Card_Dispatch_Option","Courier");

			}
			else if(!formObject.getNGValue("AlternateContactDetails_carddispatch").equalsIgnoreCase(""))
			{
				formObject.setNGValue("Card_Dispatch_Option","Branch");
			}

			//++ below code added by Deepak on 10/07/21 for PCASI-3656 EFMS data push  functionality
			List<String> objInput=new ArrayList<String>();

			String decision_str = formObject.getNGValue("cmplx_Decision_Decision");
			if("Approve and Exit".equalsIgnoreCase(decision_str)){
				objInput.add("Text:"+formObject.getWFWorkitemName());
				objInput.add("Text:"+"Approve");
				PersonalLoanS.mLogger.info("objInput args are: "+objInput.get(0));
				List<Object> objOutput=new ArrayList<Object>();

				objOutput.add("Text");
				PersonalLoanS.mLogger.info("Before executing procedure ng_RLOS_CheckWriteOff");
				objOutput= formObject.getDataFromStoredProcedure("ng_EFMS_InsertData", objInput,objOutput);
			}
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

