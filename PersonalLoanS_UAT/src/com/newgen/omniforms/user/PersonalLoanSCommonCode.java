package com.newgen.omniforms.user;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;





import com.newgen.omniforms.util.PL_SKLogger;








import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.excp.CustomExceptionHandler;

public class PersonalLoanSCommonCode extends PLCommon{
	
	public void FrameExpandEvent(ComponentEvent pEvent){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PL_SKLogger.writeLog("PersonalLoanS","Inside PLCommoncode-->FrameExpandEvent()");
		PL_SKLogger.writeLog("PersonalLoanS","Inside CUSTOME!!!!!!!!!!!!!!!!!!!!!!!");
		HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
		String popupFlag="N";
		String popUpMsg="";
	
		if (pEvent.getSource().getName().equalsIgnoreCase("CustomerDetails")) {
			hm.put("CustomerDetails","Clicked");
			popupFlag="N";
			formObject.fetchFragment("CustomerDetails", "Customer", "q_Customer");
			loadPicklistCustomer();
					
		}
		

		/*else if (pEvent.getSource().getName().equalsIgnoreCase("ProductContainer")) {
			hm.put("ProductContainer","Clicked");
			popupFlag="N";
			formObject.fetchFragment("ProductContainer", "Product", "q_Product");
			loadProductCombo();
	
		}*/
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("EmploymentDetails")) {
			
			hm.put("EmploymentDetails","Clicked");
			popupFlag="N";
			formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmpDetails");
			
			//code added
			  /*formObject.setNGValue("cmplx_EmploymentDetails_marketcode","A50");
			   PL_SKLogger.writeLog("PL Initiation", "value of los:"+ formObject.getNGValue("cmplx_EmploymentDetails_marketcode"));*/
			
			//code ended
			//loadPicklist_Employment();
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}	
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDEtails")) {
			hm.put("IncomeDEtails","Clicked");
			popupFlag="N";
			formObject.fetchFragment("IncomeDEtails", "IncomeDetails", "q_IncomeDetails");
			income_Dectech();
			//code
			/*  formObject.setNGValue("cmplx_IncomeDetails_grossSal","34123");
	            formObject.setNGValue("cmplx_IncomeDetails_Commission_Avg","3413");
	            formObject.setNGValue("cmplx_IncomeDetails_SalaryDay","34123");
	            formObject.setNGValue("cmplx_IncomeDetails_SalaryXferToBank","Y");
	            formObject.setNGValue("cmplx_IncomeDetails_AvgBal","24432");
	            formObject.setNGValue("cmplx_IncomeDetails_AvgCredTurnover","43545");
	            formObject.setNGValue("cmplx_IncomeDetails_Other_Avg","43545");
	            PL_SKLogger.writeLog("PL Initiation", "value of los: value of dbr avgbal"+formObject.getNGValue("cmplx_IncomeDetails_Other_Avg"));
			*/
			//code
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}	
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("InternalExternalLiability")) {
			hm.put("InternalExternalLiability","Clicked");
			popupFlag="N";
			
			formObject.fetchFragment("InternalExternalLiability", "Liability_New", "q_Liabilities");
			String App_Type=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 4);
			if (App_Type != "RESCH"){
				formObject.setVisible("Liability_New_Label6", false);
				formObject.setVisible("cmplx_Liability_New_noofpaidinstallments", false);
			}
			LoadPickList("Liability_New_worststatuslast24", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from ng_master_Aecb_Codes with (nolock) order by code");
			String activity = formObject.getWFActivityName();
			if(activity.equalsIgnoreCase("CSM")){
				//hideLiabilityAddFields(formObject);
			}
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}	
		
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("MiscFields")) {
			hm.put("MiscFields","Clicked");
			popupFlag="N";

			formObject.fetchFragment("MiscFields", "MiscellaneousFields", "q_MiscFields");
			
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
			
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("EligibilityAndProductInformation")) {
			hm.put("EligibilityAndProductInformation","Clicked");
			popupFlag="N";
			
			formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligibilityProdInfo");
			LoadPickList("cmplx_EligibilityAndProductInfo_RepayFreq", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_frequency with (nolock) order by code");
			LoadPickList("cmplx_EligibilityAndProductInfo_instrumenttype", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from  NG_MASTER_instrumentType with (nolock) order by code");
			LoadPickList("cmplx_EligibilityAndProductInfo_InterestType", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_InterestType with (nolock) order by code");
			//query added by saurabh on 22nd Oct for loading base rate type.
			LoadPickList("cmplx_EligibilityAndProductInfo_BaseRateType", "select distinct PRIME_TYPE from NG_master_Scheme order by PRIME_TYPE");
			//code																						q_EligibilityProdInfo
			 /*formObject.setNGValue("cmplx_EligibilityAndProductInfo_FinalDBR","34");
	            PL_SKLogger.writeLog("PL Initiation", "value of los: value of dbr"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalDBR"));
			*/
			//code
			if(formObject.getWFActivityName().equalsIgnoreCase("CSM")){
				setLoanFieldsVisible();
			}
			Eligibilityfields();
			
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}	
		
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("Address_Details_container")) {
			hm.put("Address_Details_container","Clicked");
			popupFlag="N";

			formObject.fetchFragment("Address_Details_container", "AddressDetails", "q_AddressDetails");
			loadPicklist_Address();
			
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}	
		
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("Alt_Contact_container")) {
			hm.put("Alt_Contact_container","Clicked");
			popupFlag="N";
			
			formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
			loadAltContactDetailsCombo();
				
			LoadPickList("AlternateContactDetails_CustomerDomicileBranch", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock) order by Code");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		
		if (pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails")) {
			hm.put("ReferenceDetails","Clicked");
			popupFlag="N";
			  			
			formObject.fetchFragment("ReferenceDetails", "ReferenceDetails", "q_ReferenceDetails");
			LoadPickList("ReferenceDetails_ref_Relationship", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Relationship with (nolock) order by code");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")) 
		{
			hm.put("FATCA","Clicked");
			popupFlag="N";
			formObject.fetchFragment("FATCA", "FATCA", "q_FATCA");
			LoadPickList("cmplx_FATCA_USRelation", " select '--Select--' as description, '' as code  union select convert(varchar,description),code  from ng_master_usrelation order by code");
			LoadPickList("cmplx_FATCA_Category", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_category with (nolock) order by code");
			String usRelation = formObject.getNGValue("cmplx_FATCA_USRelation");
			if(usRelation.equalsIgnoreCase("O")){
				formObject.setEnabled("FATCA_Frame6", false);
				formObject.setEnabled("cmplx_FATCA_USRelation",true);
				formObject.setEnabled("FATCA_Save", true);
			}
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
			
		}	
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("KYC")) 
		{
			hm.put("KYC","Clicked");
			popupFlag="N";
			formObject.fetchFragment("KYC", "KYC", "q_KYC");
			
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
			
		}	
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("OECD")) 
		{
			hm.put("OECD","Clicked");
			popupFlag="N";
			formObject.fetchFragment("OECD", "OECD", "q_OECD");
			loadPickListOECD();
		
			
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
			
		}	
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("LoanDetails")) {
			hm.put("LoanDetails","Clicked");
			popupFlag="N";
			
			formObject.fetchFragment("LoanDetails", "LoanDetails", "q_Loan");
			
			formObject.setNGValue("LoanDetails_winame", formObject.getWFWorkitemName());
			loadPicklist_LoanDetails();
			//if condition added by saurabh on 16th Oct.
			if(formObject.isVisible("ELigibiltyAndProductInfo_Frame1")){
			loanvalidate();//modified by akshay on 14/10/17 for displaying values in loan details
			}
			//else condition added by saurabh to auto fetch EligProdInfo.
			else{
				formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligibilityProdInfo");
				LoadPickList("cmplx_EligibilityAndProductInfo_RepayFreq", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_frequency with (nolock) order by code");
				LoadPickList("cmplx_EligibilityAndProductInfo_instrumenttype", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from  NG_MASTER_instrumentType with (nolock) order by code");
				LoadPickList("cmplx_EligibilityAndProductInfo_InterestType", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_InterestType with (nolock) order by code");
				//code																						q_EligibilityProdInfo
				 /*formObject.setNGValue("cmplx_EligibilityAndProductInfo_FinalDBR","34");
		            PL_SKLogger.writeLog("PL Initiation", "value of los: value of dbr"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalDBR"));
				*/
				//code
				if(formObject.getWFActivityName().equalsIgnoreCase("CSM")){
					setLoanFieldsVisible();
				}
				Eligibilityfields();
				loanvalidate();//modified by akshay on 14/10/17 for displaying values in loan details
			}
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("Self_Employed")) {
			hm.put("Self_Employed","Clicked");
			popupFlag="N";
			formObject.fetchFragment("Self_Employed", "SelfEmployed", "q_SelfEmployed");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
			hm.put("DecisionHistory","Clicked");
			popupFlag="N";
			try{
			formObject.fetchFragment("DecisionHistory", "DecisionHistory", "q_Decision");
			formObject.setVisible("cmplx_Decision_Manual_deviation_reason", false);
			loadInDecGrid();
			loadPicklist3();
			if(formObject.getNGValue("cmplx_Decision_Manual_Deviation").equalsIgnoreCase("true")){
                formObject.setLocked("DecisionHistory_Button6",false);
                formObject.setEnabled("cmplx_Decision_CADDecisiontray", false);        
			}
			else{
                formObject.setLocked("DecisionHistory_Button6",true);
                formObject.setEnabled("cmplx_Decision_CADDecisiontray", true);         
			}

			//Code Change By aman to save Highest Delegation Auth
			String sQuery = "select distinct(Delegation_Authorithy) from ng_rlos_IGR_Eligibility_CardProduct where Child_Wi ='"+formObject.getWFWorkitemName()+"'";
			List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
			PL_SKLogger.writeLog("sQuery list size"+sQuery, "");
			PL_SKLogger.writeLog("cmplx_DEC_HighDeligatinAuth list size"+OutputXML.size(), "value is "+OutputXML.get(0).get(0));
			PL_SKLogger.writeLog("cmplx_DEC_HighDeligatinAuth list size"+OutputXML.size(), "value is ");
			if(!OutputXML.isEmpty()){
			String HighDel=OutputXML.get(0).get(0);
			if(OutputXML.get(0).get(0)!=null && !OutputXML.get(0).get(0).isEmpty() &&  !OutputXML.get(0).get(0).equals("") && !OutputXML.get(0).get(0).equalsIgnoreCase("null") ){
				{
					PL_SKLogger.writeLog("Inside the if dectech"+HighDel, "value is ");	
					formObject.setNGValue("cmplx_Decision_Highest_delegauth", HighDel);
				}
			}
			}	
				//Code Change By aman to save Highest Delegation Auth
			
			
			}
			catch(Exception ex){
				PL_SKLogger.writeLog("Exception in Decision load",printException(ex) );
			}
			
			//added Tanshu Aggarwal(22/06/2017)
			if	(formObject.getNGValue("Is_Account_Create").equalsIgnoreCase("Y") && formObject.getNGValue("Is_Customer_Create").equalsIgnoreCase("Y"))
			{
				PL_SKLogger.writeLog("RLOS_initiation Decision load", "inside both req Y");
				formObject.setLocked("DecisionHistory_Button2", true);
			}
			else{
				PL_SKLogger.writeLog("RLOS_initiation Decision load", "inside both req N");
				formObject.setLocked("DecisionHistory_Button2", false);
			}
			//added Tanshu Aggarwal(22/06/2017)			
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("Inc_Doc")) {
			hm.put("Inc_Doc","Clicked");
			popupFlag="N";
			  	
			formObject.fetchFragment("Inc_Doc", "IncomingDoc", "q_IncomingDoc");
			fetchIncomingDocRepeater();
			String activName = formObject.getWFActivityName();
			if(activName.equalsIgnoreCase("DDVT_Checker")){
				if(formObject.getNGValue("cmplx_Customer_NEP").equalsIgnoreCase("false") && formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("false"))
					formObject.setVisible("IncomingDoc_UploadSig",true);
				else if(formObject.getNGValue("cmplx_Customer_NEP").equalsIgnoreCase("true") || formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true")){
					formObject.setVisible("IncomingDoc_ViewSIgnature",false);
					formObject.setVisible("IncomingDoc_UploadSig",true);
					//formObject.setEnabled("IncomingDoc_UploadSig",false);
				}
			}
			else if(activName.equalsIgnoreCase("DDVT_Maker")){
				if(formObject.getNGValue("cmplx_Customer_NEP").equalsIgnoreCase("false") && formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("false")){
					formObject.setVisible("IncomingDoc_ViewSIgnature",true);
					formObject.setEnabled("IncomingDoc_ViewSIgnature",true);
				}
				else if(formObject.getNGValue("cmplx_Customer_NEP").equalsIgnoreCase("true") || formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true")){
					formObject.setEnabled("IncomingDoc_ViewSIgnature",false);
					//formObject.setVisible("IncomingDoc_UploadSig",true);
					//formObject.setEnabled("IncomingDoc_UploadSig",false);
				}
			}
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("Post_Disbursal")) 
		{
			hm.put("Post_Disbursal","Clicked");
			
			popupFlag="N";
			
			PL_SKLogger.writeLog("PersonalLoanS","Inside PostDisbursal-->FrameExpandEvent()");
			
			formObject.fetchFragment("Post_Disbursal", "PostDisbursal", "q_PostDisbursal");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		
		
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("Loan_Disbursal")) {
			hm.put("Loan_Disbursal","Clicked");
			popupFlag="N";
			  			
			formObject.fetchFragment("Loan_Disbursal", "Loan_Disbursal", "q_FinIncident");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("CC_Creation")) {
			hm.put("CC_Creation","Clicked");
			popupFlag="N";
			  			
			formObject.fetchFragment("CC_Creation", "CC_Creation", "q_FinIncident");
			//code
			  /*formObject.setNGValue("cmplx_CCCreation_CRN","34123");
	            formObject.setNGValue("cmplx_CCCreation_ECRN","3498798");
	            PL_SKLogger.writeLog("PL Initiation", "value of los: value of dbr"+formObject.getNGValue("cmplx_CCCreation_ECRN")+","+formObject.getNGValue("cmplx_CCCreation_CRN"));
			*/
			//code
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("Limit_Inc")) {
			hm.put("Limit_Inc","Clicked");
			popupFlag="N";
			  			
			formObject.fetchFragment("Limit_Inc", "Limit_Inc", "q_FinIncident");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("Cust_Det_Ver")) {
			hm.put("Cust_Det_Ver","Clicked");
			popupFlag="N";
			  			
			formObject.fetchFragment("Cust_Det_Ver", "CustDetailVerification1", "q_CustDetailVeriFCU");
			//custdet1values();
			int framestate0=formObject.getNGFrameState("Address_Details_container");
			if(framestate0 != 0){
				formObject.fetchFragment("Address_Details_container", "AddressDetails", "q_AddressDetails");
				//formObject.setTop("Alt_Contact_container",formObject.getTop("Address_Details_Container")+formObject.getHeight("Address_Details_Container"));
				formObject.setTop("Alt_Contact_container",formObject.getTop("Address_Details_Container")+formObject.getHeight("Address_Details_Container")); 
				formObject.setTop("ReferenceDetails",formObject.getTop("Alt_Contact_container")+25);
                //formObject.setTop("Supplementary_Container",formObject.getTop("ReferenceDetails")+50);
                formObject.setTop("Card_Details", formObject.getTop("ReferenceDetails")+25);
                formObject.setTop("FATCA", formObject.getTop("Card_Details")+30);
					formObject.setTop("KYC", formObject.getTop("FATCA")+20);
					formObject.setTop("OECD", formObject.getTop("KYC")+20);
			}
			int framestate1=formObject.getNGFrameState("Alt_Contact_container");
			if(framestate1 != 0){
				formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
				formObject.setTop("Alt_Contact_container",formObject.getTop("Address_Details_Container")+formObject.getHeight("Address_Details_Container"));
                //formObject.setTop("Supplementary_Container",formObject.getTop("ReferenceDetails")+250);
				formObject.setTop("ReferenceDetails",formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container"));
                formObject.setTop("Card_Details", formObject.getTop("ReferenceDetails")+20);
                formObject.setTop("FATCA", formObject.getTop("Card_Details")+30);
				formObject.setTop("KYC", formObject.getTop("FATCA")+20);
				formObject.setTop("OECD", formObject.getTop("KYC")+20);
            }
			autopopulateValues(formObject);
			
			formObject.setVisible("Credit_card_Enq", false);
			formObject.setVisible("Case_History", false);
			formObject.setVisible("LOS", false);
			
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("Business_Verif")) {
			hm.put("Business_Verif","Clicked");
			popupFlag="N";
			  			
			formObject.fetchFragment("Business_Verif", "BussinessVerification1", "q_bussverification1");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("Emp_Verification")) {
			hm.put("Emp_Verification","Clicked");
			popupFlag="N";
			  			
			formObject.fetchFragment("Emp_Verification", "EmploymentVerification", "q_EmpVerification");
			EmpVervalues();
			LoadPickList("cmplx_EmploymentVerification_OffTelnoValidatedfrom", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_OffTelnoVal with (nolock)");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("Bank_Check")) {
			hm.put("Bank_Check","Clicked");
			popupFlag="N";
			  			
			formObject.fetchFragment("Bank_Check", "BankingCheck", "q_BankingCheck");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("Note_Details")) {
			hm.put("Note_Details","Clicked");
			popupFlag="N";
			PL_SKLogger.writeLog("inside NOte_Details frame expanded event", "");  			
			formObject.fetchFragment("Note_Details", "NotepadDetailsFCU", "q_Note");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("Supervisor_section")) {
			hm.put("Supervisor_section","Clicked");
			popupFlag="N";
			  			
			formObject.fetchFragment("Supervisor_section", "FCU_Decision", "q_FcuDecision");
			LoadPickList("cmplx_FcuDecision_ReferralReason", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_Master_Referralreason with (nolock)");
			LoadPickList("cmplx_FcuDecision_ReferralSubReason", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_Master_Referralsubreason with (nolock)");
			//LoadPickList("cmplx_Supervisor_SubFeedback_Status", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_Master_SubfeedbackStatus with (nolock)");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("Smart_chk")) {
			hm.put("Smart_chk","Clicked");
			popupFlag="N";
			  			
			formObject.fetchFragment("Smart_chk", "SmartCheck1", "q_SmartCheckFCU");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("Part_Match")) {
			hm.put("Part_Match","Clicked");
			popupFlag="N";
			  			
			formObject.fetchFragment("Part_Match", "PartMatch", "q_PartMatch");
			//added by saurabh on 10th July 17 for DDVT_MAKER.
			partMatchValues();
			LoadPickList("PartMatch_nationality", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_MASTER_Country with (nolock)");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRM_Incidents")){					
			hm.put("FinacleCRM_Incidents","Clicked");
			popupFlag="N";
			formObject.fetchFragment("FinacleCRM_Incidents", "FinacleCRMIncident", "q_FinIncident");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRM_CustInfo")){ 
			hm.put("FinacleCRM_CustInfo","Clicked");
			popupFlag="N";
			formObject.fetchFragment("FinacleCRM_CustInfo", "FinacleCRMCustInfo", "q_FinCRMCustInfo");
			formObject.setNGValue("FinacleCRMCustInfo_WINAME",formObject.getWFWorkitemName());
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
			
		}
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("Finacle_Core")){ 
			hm.put("Finacle_Core","Clicked");
			popupFlag="N";
			formObject.fetchFragment("Finacle_Core", "FinacleCore", "q_Finaclecore");
			loadPicklistDDSReturn();
			try{
    			String query="select AcctType,'NA',AcctId,AcctNm,AccountOpenDate,AcctStat,'NA',AvailableBalance,'NA','NA','NA' from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
    			List<List<String>> list_acc=formObject.getDataFromDataSource(query);
    			for(List<String> mylist : list_acc)
    			 {
    				PL_SKLogger.writeLog("PL DDVT Maker", "Data to be added in cmplx_FinacleCore_FinaclecoreGrid Grid: "+mylist.toString());
    				formObject.addItemFromList("cmplx_FinacleCore_FinaclecoreGrid", mylist);
    			 }
    			
    			query="select AcctId,LienId,LienAmount,LienRemarks,LienReasonCode,LienStartDate,LienExpDate from ng_rlos_FinancialSummary_LienDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
				//changed ended
    			List<List<String>> list_lien=formObject.getDataFromDataSource(query);
    			for(List<String> mylist : list_lien)
    			 {
    				PL_SKLogger.writeLog("PL DDVT Maker", "Data to be added in Grid: "+mylist.toString());
    				formObject.addItemFromList("cmplx_FinacleCore_liendet_grid", mylist);
    			 }
				 //changed here in this query
				query="select AcctId,'','','','' from ng_rlos_FinancialSummary_SiDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
    			List<List<String>> list_SIDet=formObject.getDataFromDataSource(query);
				//changed ended
    			
    			for(List<String> mylist : list_SIDet)
    			 {
    				PL_SKLogger.writeLog("PL DDVT Maker", "Data to be added in Grid: "+mylist.toString());
    				
    				formObject.addItemFromList("cmplx_FinacleCore_sidet_grid", mylist);
    			 }
    			}
    			catch(Exception e){
    				PL_SKLogger.writeLog("PL DDVT Maker", "Exception while setting data in grid:"+e.getMessage());
    				popupFlag = "Y";
    				throw new ValidatorException(new FacesMessage("Error while setting data in account grid"));
    			}
			
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
			
		}
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("MOL")){ 
			hm.put("MOL","Clicked");
			popupFlag="N";
			formObject.fetchFragment("MOL", "MOL1", "q_MOL");
			loadPicklistMol();
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
			
		}
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("World_Check")){ 
			hm.put("World_Check","Clicked");
			popupFlag="N";
			formObject.fetchFragment("World_Check", "WorldCheck1", "q_WorldCheck");
			loadPicklist_WorldCheck();
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
			
		}
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("Reject_Enq")){ 
			hm.put("Reject_Enq","Clicked");
			popupFlag="N";
			formObject.fetchFragment("Reject_Enq", "RejectEnq", "q_RejectEnq");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
			
		}
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("Sal_Enq")){ 
			hm.put("Sal_Enq","Clicked");
			popupFlag="N";
			formObject.fetchFragment("Sal_Enq", "SalaryEnq", "q_SalaryEnq");
			try{
				String query="select SalCreditDate,SalCreditMonth,AcctId,SalAmount from ng_rlos_FinancialSummary_SalTxnDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
    			List<List<String>> list_acc=formObject.getDataFromDataSource(query);
    			for(List<String> mylist : list_acc)
    			 {
    				PL_SKLogger.writeLog("PL DDVT Maker", "Data to be added in account Grid account Grid: "+mylist.toString());
    				formObject.addItemFromList("cmplx_SalaryEnq_SalGrid", mylist);
    			 }
    			
			}
			catch(Exception e){
				
			}
			
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
			
		}				
		
	
	   /*else if (pEvent.getSource().getName().equalsIgnoreCase("Notepad_Details")) {
			hm.put("Notepad_Details","Clicked");
			popupFlag="N";
			  			
			formObject.fetchFragment("Notepad_Details", "CAD", "q_DECCAD");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		} */
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("Dec")) {
			hm.put("Dec","Clicked");
			popupFlag="N";
			  			
			formObject.fetchFragment("Dec", "CAD_Decision", "q_CadDecision");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("ReferHistory")) {
			hm.put("ReferHistory","Clicked");
			popupFlag="N";
			  			
			formObject.fetchFragment("ReferHistory", "ReferHistory", "q_ReferHistory");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("Cust_Detail_verification")) {
			hm.put("Cust_Detail_verification","Clicked");
			popupFlag="N";
			formObject.fetchFragment("Cust_Detail_verification", "CustDetailVerification", "q_CustDetVer");
			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_CustDetailVerification_mobno1_ver","cmplx_CustDetailVerification_mobno2_ver","cmplx_CustDetailVerification_dob_ver","cmplx_CustDetailVerification_POBoxno_ver","cmplx_CustDetailVerification_emirates_ver","cmplx_CustDetailVerification_persorcompPOBox_ver","cmplx_CustDetailVerification_resno_ver","cmplx_CustDetailVerification_offtelno_ver","cmplx_CustDetailVerification_hcountrytelno_ver","cmplx_CustDetailVerification_hcontryaddr_ver","cmplx_CustDetailVerification_email1_ver","cmplx_CustDetailVerification_email2_ver");
		
			LoadPicklistVerification(LoadPicklist_Verification);
			LoadPickList("cmplx_CustDetailVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision order by code");
			
			custdetvalues();
			//loadPicklist3();
			//loadInDecGrid();
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("Business_verification")) {
			hm.put("Business_verification","Clicked");
			popupFlag="N";
			  			
			formObject.fetchFragment("Business_verification", "BussinessVerification", "q_businessverification");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("Home_country_verification")) {
			hm.put("Home_country_verification","Clicked");
			popupFlag="N";
			  			
			formObject.fetchFragment("Home_country_verification", "HomeCountryVerification", "q_HomeCountryVeri");
			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_HCountryVerification_Hcountrytelverified");
			LoadPicklistVerification(LoadPicklist_Verification);
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("Residence_verification")) {
			hm.put("Residence_verification","Clicked");
			popupFlag="N";
			  			
			formObject.fetchFragment("Residence_verification", "ResidenceVerification", "q_ResiVerification");
			LoadPickList("cmplx_ResiVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision order by code");
			
			
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("Guarantor_verification")) {
			hm.put("Guarantor_verification","Clicked");
			popupFlag="N";
			  			
			formObject.fetchFragment("Guarantor_verification", "GuarantorVerification", "q_GuarantorVer");
			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_GuarantorVerification_verdoneonmob");
			LoadPicklistVerification(LoadPicklist_Verification);
			
			LoadPickList("cmplx_GuarantorVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision order by code");
			
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("Reference_detail_verification")) {
			hm.put("Reference_detail_verification","Clicked");
			popupFlag="N";
			  			
			formObject.fetchFragment("Reference_detail_verification", "ReferenceDetailVerification", "q_RefDetVer");
			LoadPickList("cmplx_RefDetVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision order by code");
			
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("Office_verification")) {
			hm.put("Office_verification","Clicked");
			popupFlag="N";
			  			
			formObject.fetchFragment("Office_verification", "OfficeandMobileVerification", "q_OffVerification");
			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_OffVerification_hrdemailverified","cmplx_OffVerification_colleaguenoverified","cmplx_OffVerification_fxdsal_ver","cmplx_OffVerification_accpvded_ver","cmplx_OffVerification_desig_ver","cmplx_OffVerification_doj_ver","cmplx_OffVerification_cnfminjob_ver");
			LoadPicklistVerification(LoadPicklist_Verification);
			OfficeVervalues();
			LoadPickList("cmplx_OffVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision order by code");
			
			LoadPickList("cmplx_OffVerification_offtelnovalidtdfrom", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_OffTelnoVal with (nolock)");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("Loan_card_details")) {
			hm.put("Loan_card_details","Clicked");
			popupFlag="N";
			  			
			formObject.fetchFragment("Loan_card_details", "LoanandCard", "q_LoanandCard");
			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_LoanandCard_loanamt_ver","cmplx_LoanandCard_tenor_ver","cmplx_LoanandCard_emi_ver","cmplx_LoanandCard_islorconv_ver","cmplx_LoanandCard_firstrepaydate_ver","cmplx_LoanandCard_cardtype_ver","cmplx_LoanandCard_cardlimit_ver");
			LoadPicklistVerification(LoadPicklist_Verification);
			LoadPickList("cmplx_LoanandCard_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision order by code");
			
			loancardvalues();
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("Notepad_details")) {
			hm.put("Notepad_details","Clicked");
			popupFlag="N";
			  			
			formObject.fetchFragment("Notepad_details", "NotepadDetails", "q_Note");
			LoadPickList("NotepadDetails_notedesc", "select '--Select--' union select  description from ng_master_notedescription");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("Smart_check")) {
			hm.put("Smart_check","Clicked");
			popupFlag="N";
			  			
			formObject.fetchFragment("Smart_check", "SmartCheck1", "q_SmartCheckFCU");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("Orig_validation")) {
			hm.put("Orig_validation","Clicked");
			popupFlag="N";
			  			
			formObject.fetchFragment("Orig_validation", "OriginalValidation", "q_OrigVal");
			fetchoriginalDocRepeater();
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		
		//New fragment added by Arun on 17/7/17 for new DDVT changes on screen by shashank 
		else if (pEvent.getSource().getName().equalsIgnoreCase("Risk_Rating")) {
			hm.put("Risk_Rating","Clicked");
			popupFlag="N";
			  			
			formObject.fetchFragment("Risk_Rating", "RiskRating", "q_riskrating");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("Notepad_Values")) {
			hm.put("Notepad_Values","Clicked");
			popupFlag="N";
			  			
			formObject.fetchFragment("Notepad_Values", "NotepadDetails", "q_Note");
			
			formObject.setVisible("NotepadDetails_Label6", true);
			formObject.setVisible("NotepadDetails_insqueue", true);
			formObject.setVisible("NotepadDetails_inscompletion", true);
			formObject.setVisible("NotepadDetails_Label10", true);
			formObject.setVisible("NotepadDetails_Actdate", true);
			formObject.setVisible("NotepadDetails_Label11", true);
			formObject.setVisible("NotepadDetails_Actusername", true);
			formObject.setVisible("NotepadDetails_Label9", true);
			formObject.setVisible("NotepadDetails_ActuserRemarks", true);
			//formObject.setHeight("NotepadDetails_Frame1", 400);//Arun (11/9/17)
			//formObject.setTop("NotepadDetails_save",420);//Arun (11/9/17)
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("Card_Details")) {
			hm.put("Card_Details","Clicked");
			popupFlag="N";
			
			formObject.fetchFragment("Card_Details", "CardDetails", "q_CardDetails");
				
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("Credit_card_Enq")) {
			hm.put("Credit_card_Enq","Clicked");
			popupFlag="N";
			
			formObject.fetchFragment("Credit_card_Enq", "CreditCardEnq", "q_CCEnq");
				
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("Case_History")) {
			hm.put("Case_History","Clicked");
			popupFlag="N";
			
			formObject.fetchFragment("Case_History", "CaseHistoryReport", "q_CaseHist");
				
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("LOS")) {
			hm.put("LOS","Clicked");
			popupFlag="N";
			
			formObject.fetchFragment("LOS", "LOS", "q_LOS");
				
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("External_Blacklist")) {
			hm.put("External_Blacklist","Clicked");
			popupFlag="N";
			
			formObject.fetchFragment("External_Blacklist", "ExternalBlackList", "q_ExternalBlackList");
				
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		
		else if (pEvent.getSource().getName().equalsIgnoreCase("Compliance")) {
			hm.put("Compliance","Clicked");
			popupFlag="N";
		  			
			formObject.fetchFragment("Compliance", "Compliance", "q_compliance");

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
	
	}
	
	public void setLoanFieldsVisible() {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setVisible("ELigibiltyAndProductInfo_Label11",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_RepayFreq",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label31",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_Moratorium",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label12",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_FirstRepayDate",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label14",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_InterestType",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label15",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_BaseRateType",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label17",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_MArginRate",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label16",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_BAseRate",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label18",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_ProdPrefRate",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label23",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_NetRate",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label13",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_MaturityDate",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label30",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_AgeAtMaturity",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label32",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_NoOfInstallments",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label19",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_LPF",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label20",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_LPFAmount",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label21",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_Insurance",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label22",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_InsuranceAmount",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label8",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_NetPayout",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label10",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Text9",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label9",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Text8",true);
	}
	public void autopopulateValues(FormReference formObject) {
		
		String mobNo1 = formObject.getNGValue("AlternateContactDetails_MobileNo1");
		String mobNo2 = formObject.getNGValue("AlternateContactDetails_MobileNo2");
		String dob = formObject.getNGValue("cmplx_Customer_DOb");
		int adressrowcount = formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
		String poboxResidence = "";
		//String poboxOffice = "";
		//String homeadd = "";
		for(int i=0;i<adressrowcount;i++){
			String addType = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,0);
			if(addType.equalsIgnoreCase("RESIDENCE")){
				poboxResidence = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,8);
			}
		}
		//String resNo = formObject.getNGValue("AlternateContactDetails_ResidenceNo");
		String officeNo = formObject.getNGValue("AlternateContactDetails_OFFICENO");
		/*String homeNo = formObject.getNGValue("AlternateContactDetails_HomeCOuntryNo");
		String email1 = formObject.getNGValue("AlternateContactDetails_Email1");
		String email2 = formObject.getNGValue("AlternateContactDetails_Email2");*/
		
		setValues(formObject,mobNo1,mobNo2,dob,poboxResidence,officeNo);
		//String poBox = formObject.getNGValue("cmplx_CustDetailVerification_POBoxNo_val");
		
	}

	public void setValues(FormReference formObject,String...values) {
		String[] controls = new String[]{"cmplx_CustDetailverification1_MobNo1_value","cmplx_CustDetailverification1_MobNo2_value","cmplx_CustDetailverification1_DOB_value",
				"cmplx_CustDetailverification1_PO_Box_Value","cmplx_CustDetailverification1_Off_Telephone_value"};
		int i=0;
		for(String str : values){
			if(checkValue(str)){
				formObject.setNGValue(controls[i], str);
				formObject.setLocked(controls[i], true);
				
			}
			else{
				PL_SKLogger.writeLog("CC Initiation", "value received at index "+i+" is: "+str);
			}
			i++;
		}
	}
	
	public boolean checkValue(String ngValue){
		if(ngValue==null ||ngValue.equalsIgnoreCase("") ||ngValue.equalsIgnoreCase(" ") || ngValue.equalsIgnoreCase("--Select--") || ngValue.equalsIgnoreCase("false")){
			return false;
		}
		return true;
	}

	public void getAgeWorldCheck(String dateBirth){
  		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
  		PL_SKLogger.writeLog("CC_Common", "Inside getAge(): "); 
  		String parts[] = dateBirth.split("/");
  		Calendar dob = Calendar.getInstance();
  	    Calendar today = Calendar.getInstance();

  	    dob.set(Integer.parseInt(parts[2]), Integer.parseInt(parts[1])-1,Integer.parseInt(parts[0])); 

  	    Integer age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

  	    if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
  	        age--; 
  	    }
  	  PL_SKLogger.writeLog("CC_Common", "Values are: "+parts[2]+parts[1]+parts[0]+age); 


  	  formObject.setNGValue("WorldCheck1_age",age.toString(),false); 
  	}
	
	public void setFormHeader(FormEvent pEvent){

        FormReference formObject = FormContext.getCurrentInstance().getFormReference();
        String activityName = formObject.getWFActivityName();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try{
        	PL_SKLogger.writeLog("PL Initiation", "Inside formPopulated()" + pEvent.getSource());
            formObject.fetchFragment("CustomerDetails", "Customer", "q_Customer");
            formObject.setNGFrameState("CustomerDetails",0);
            formObject.fetchFragment("ProductContainer", "Product", "q_Product");
            formObject.setNGFrameState("ProductContainer",0);    
            String ReqProd=formObject.getNGValue("ReqProd");
			PL_SKLogger.writeLog("PersonalLoanScommonCode ", "Value of ReqProd is:"+ReqProd);
			//changed to static string by saurabh as it was coming as --select-- as found in the logs.
			loadPicklistProduct("Personal Loan");
            // disha FSD
            formObject.setNGValue("WiLabel",formObject.getWFWorkitemName());
            formObject.setNGValue("UserLabel",formObject.getUserName()); 
            //formObject.setNGValue("IntroDateLabel",formObject.getNGValue("IntroDateTime"));
            formObject.setNGValue("InitChannelLabel",formObject.getNGValue("InitiationType"));
           
            formObject.setNGValue("CifLabel",formObject.getNGValue("cmplx_Customer_CIFNO"));
            
            formObject.setNGValue("CRNLabel",formObject.getNGValue("CRN")); 
            
            Date date = new Date();
            formObject.setNGValue("CurrentDateLabel",dateFormat.format(date)); 
            formObject.setNGValue("CreatedDateLabel", formObject.getNGValue("CreatedDate"));//modified by akshay on 14/10/17
            String nationality = formObject.getNGValue("cmplx_Customer_Nationality");
			PL_SKLogger.writeLog("CC", "Nationality is$$$$: "+nationality);
			if(!nationality.equalsIgnoreCase("AE")){
				formObject.setLocked("cmplx_Customer_marsoomID",true);
			}
            String fName = formObject.getNGValue("cmplx_Customer_FIrstNAme");
            String mName = formObject.getNGValue("cmplx_Customer_MiddleName");
            String lName = formObject.getNGValue("cmplx_Customer_LAstNAme");
            
            String fullName = fName+" "+mName+" "+lName; 
            formObject.setNGValue("CustomerLabel",fullName);     
            
            String loanAmt = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,3);
            formObject.setNGValue("LoanLabel",loanAmt);
            
            String product = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0);
            formObject.setNGValue("ProductLabel",product);
            String sub_product = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2);
            formObject.setNGValue("SubProductLabel",sub_product);
            String card_product = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,4);
            formObject.setNGValue("CardLabel",card_product);
            
            PL_SKLogger.writeLog("PL Initiation", "Itemindex is:" +  formObject.getWFFolderId());
            formObject.setNGValue("AppRefLabel", formObject.getNGValue("AppRefNo"));
            
            List<String> activity_openFragmentsonLoad = Arrays.asList("CAD_Analyst1","CAD_Analyst2","CC_Disbursal","CC_Waiver","Collection_Agent_Review","Collection_User",
            		"Compliance","CPV_Analyst","CPV","CSM_Review","CSM","DDVT_Checker","Deferral_Authority","Dispatch","FCU","Hold_CPV","HR","Interest_Rate_Approval"
            		,"Manager","Original_Validation","Post_Disbursal","Reject_Queue","Rejected_Application","Relationship_Manager"
            		,"DSA_CSO_Review","RM_Review","Smart_CPV","Telesales_Agent_Review","ToTeam","Waiver_Authority","DDVT_maker");
                    
            List<String> activity_setPartMatch = Arrays.asList("DDVT_maker");
            if(activity_openFragmentsonLoad.contains(activityName)){
            	setActivityname(formObject,activityName);
            }
            if(activity_setPartMatch.contains(activityName)){
            	setPartMatch(formObject);
            }
            
        }catch(Exception e)
        {
        	PL_SKLogger.writeLog("CC Initiation", "Exception:"+printException(e));
        }
	}
	
	public static String printException(Exception e){
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String exception = sw.toString();
		return exception;
		
	}
	
	public void setActivityname(FormReference formObject , String activityName){
		try{
			formObject.setNGValue("QueueLabel","PL_"+activityName);
		}catch(Exception ex){
			PL_SKLogger.writeLog("formHeaderPart2", "Exception:"+ex.getMessage());
		}
	}
	
	public void alignfragmentsafterfetch(FormReference formObject){
		
		//formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmploymentDetails");
        formObject.fetchFragment("Address_Details_Container", "AddressDetails","q_AddressDetails");
        if(formObject.getNGFrameState("Alt_Contact_container") == 1)
        formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
        /*if(formObject.getNGFrameState("ReferenceDetails") == 1)
        formObject.fetchFragment("ReferenceDetails", "ReferenceDetails", "q_ReferenceDetails");
        if(formObject.getNGFrameState("FATCA") == 1)
        formObject.fetchFragment("FATCA", "FATCA", "q_FATCA");
        if(formObject.getNGFrameState("KYC") == 1)
        formObject.fetchFragment("KYC", "KYC", "q_KYC");
        if(formObject.getNGFrameState("OECD") == 1)
        formObject.fetchFragment("OECD", "OECD", "q_OECD");
        if(formObject.getNGFrameState("CreditCardEnq") == 1)
        formObject.fetchFragment("CreditCardEnq", "Credit_card_Enq", "q_CCEnq");
        if(formObject.getNGFrameState("CaseHistoryReport") == 1)
        formObject.fetchFragment("CaseHistoryReport", "Case_History", "q_CaseHist");
        *///formObject.fetchFragment("OECD", "OECD", "q_LOS");
        
        formObject.setTop("Alt_Contact_container",formObject.getTop("Address_Details_Container")+formObject.getHeight("Address_Details_Container"));
        PL_SKLogger.writeLog("RLOS","height of address is: "+formObject.getTop("Address_Details_Container")+"!!!"+formObject.getHeight("Address_Details_Container"));
        //formObject.setTop("CardDetails", formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container")+10);
        formObject.setTop("ReferenceDetails", formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container")+10);
        PL_SKLogger.writeLog("RLOS","height of reference is: "+formObject.getTop("ReferenceDetails")+"!!!"+formObject.getHeight("ReferenceDetails"));
        
        formObject.setTop("FATCA", formObject.getTop("ReferenceDetails")+30);//formObject.getHeight("ReferenceDetails")+10);
        PL_SKLogger.writeLog("RLOS","height of FATCA is: "+formObject.getTop("FATCA")+"!!!"+formObject.getHeight("FATCA"));
        
        formObject.setTop("KYC", formObject.getTop("FATCA")+30);//formObject.getHeight("FATCA")+20);
        PL_SKLogger.writeLog("RLOS","height of KYC is: "+formObject.getTop("KYC")+"!!!"+formObject.getHeight("KYC"));
        
        formObject.setTop("OECD", formObject.getTop("KYC")+30);//formObject.getHeight("KYC")+20);
        PL_SKLogger.writeLog("RLOS","height of OECD is: "+formObject.getTop("OECD")+"!!!"+formObject.getHeight("OECD"));
        
        formObject.setTop("Credit_card_Enq", formObject.getTop("OECD")+30);//formObject.getHeight("OECD")+20);
        PL_SKLogger.writeLog("RLOS","height of CreditCardEnq is: "+formObject.getTop("CreditCardEnq")+"!!!"+formObject.getHeight("CreditCardEnq"));
        
        formObject.setTop("Case_History", formObject.getTop("Credit_card_Enq")+30);//formObject.getHeight("Credit_card_Enq")+20);
        PL_SKLogger.writeLog("RLOS","height of CaseHistoryReport is: "+formObject.getTop("CaseHistoryReport")+"!!!"+formObject.getHeight("CaseHistoryReport"));
        
        formObject.setTop("LOS", formObject.getTop("Case_History")+30);//formObject.getHeight("Case_History")+20);
        PL_SKLogger.writeLog("RLOS","height of LOS is: "+formObject.getTop("LOS")+"!!!"+formObject.getHeight("LOS"));
        
        /*PL_SKLogger.writeLog("RLOS","height of supplement is: "+formObject.getHeight("Supplementary_Container"));
        	//formObject.setTop("Supplementary_Container", formObject.getTop("CardDetails")+10);  
			formObject.setTop("FATCA", formObject.getTop("CardDetails")+30);
		*/	
			
			
	}
	
	public void setPartMatch(FormReference formObject){
		try{
		formObject.fetchFragment("Part_Match", "PartMatch", "q_PartMatch");
            formObject.setNGFrameState("Part_Match",0);
            //loadPicklist_PartMatch();
			PL_SKLogger.writeLog("PL Part_Match","Inside Part_Match");  
	
		}
		catch(Exception ex){
			PL_SKLogger.writeLog("formHeaderPart3", "Exception:"+ex.getMessage());
		}
	}
public void hideLiabilityAddFields(FormReference formObject) {
		
		formObject.setVisible("Liability_New_delinin3",false);
		formObject.setVisible("Liability_New_DPD30inlast6",false);
		formObject.setVisible("Liability_New_DPD30inlast18",false);
		formObject.setVisible("Liability_New_Label2",false);
		formObject.setVisible("Liability_New_writeoff",false);
		formObject.setVisible("Liability_New_Label4",false);
		formObject.setVisible("Liability_New_worststatuslast24",false);
		formObject.setVisible("Liability_New_Label8",false);
		formObject.setVisible("Liability_New_Text3",false);
	}

}
