package com.newgen.omniforms.user;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.IRepeater;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.excp.CustomExceptionHandler;

public class PersonalLoanSCommonCode extends PLCommon implements Serializable{

	private static final long serialVersionUID = 1L;
	Logger mLogger=PersonalLoanS.mLogger;
	

	public void FrameExpandEvent(ComponentEvent pEvent){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		mLogger.info("PersonalLoanS---Inside PLCommoncode-->FrameExpandEvent()");
		mLogger.info("PersonalLoanS---Inside CUSTOME!!!!!!!!!!!!!!!!!!!!!!!");
		HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap

		/*if ("CustomerDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("CustomerDetails","Clicked");
			formObject.fetchFragment("CustomerDetails", "Customer", "q_Customer");
			loadPicklistCustomer();

		}----This code is never called...as customer is fetched on form load*/

		if (pEvent.getSource().getName().equalsIgnoreCase("EmploymentDetails")) {

			hm.put("EmploymentDetails","Clicked");
			formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmpDetails");

			//code added
			/*formObject.setNGValue("cmplx_EmploymentDetails_marketcode","A50");
			   mLogger.info("PL Initiation", "value of los:"+ formObject.getNGValue("cmplx_EmploymentDetails_marketcode"));*/

			//code ended
			//loadPicklist_Employment();
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}	

		else if ("IncomeDEtails".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("IncomeDEtails","Clicked");
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
	            mLogger.info("PL Initiation", "value of los: value of dbr avgbal"+formObject.getNGValue("cmplx_IncomeDetails_Other_Avg"));
			 */
			//code
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}	

		else if (pEvent.getSource().getName().equalsIgnoreCase("InternalExternalLiability")) {
			hm.put("InternalExternalLiability","Clicked");

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
			finally{
				hm.clear();
			}
		}	


		else if ("MiscFields".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("MiscFields","Clicked");

			formObject.fetchFragment("MiscFields", "MiscellaneousFields", "q_MiscFields");

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}

		}

		else if ("EligibilityAndProductInformation".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("EligibilityAndProductInformation","Clicked");
			formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligibilityProdInfo");
			LoadPickList("cmplx_EligibilityAndProductInfo_RepayFreq", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_frequency with (nolock) order by code");
			LoadPickList("cmplx_EligibilityAndProductInfo_instrumenttype", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from  NG_MASTER_instrumentType with (nolock) order by code");
			LoadPickList("cmplx_EligibilityAndProductInfo_InterestType", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_InterestType with (nolock) order by code");
			//query added by saurabh on 22nd Oct for loading base rate type.
			LoadPickList("cmplx_EligibilityAndProductInfo_BaseRateType", "select distinct PRIME_TYPE from NG_master_Scheme order by PRIME_TYPE");
			//code																						q_EligibilityProdInfo
			/*formObject.setNGValue("cmplx_EligibilityAndProductInfo_FinalDBR","34");
	            mLogger.info("PL Initiation", "value of los: value of dbr"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalDBR"));
			 */
			if("CSM".equalsIgnoreCase(formObject.getWFActivityName())){
				setLoanFieldsVisible();
			}
			Eligibilityfields();

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}	


		else if ("Address_Details_container".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Address_Details_container","Clicked");


			formObject.fetchFragment("Address_Details_container", "AddressDetails", "q_AddressDetails");
			loadPicklist_Address();

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}	


		else if ("Alt_Contact_container".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Alt_Contact_container","Clicked");
			formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
			//loadAltContactDetailsCombo();
			LoadPickList("AlternateContactDetails_CustomerDomicileBranch", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock) order by Code");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		if ("ReferenceDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("ReferenceDetails","Clicked");


			formObject.fetchFragment("ReferenceDetails", "ReferenceDetails", "q_ReferenceDetails");
			LoadPickList("ReferenceDetails_ref_Relationship", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Relationship with (nolock) order by code");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			hm.put("FATCA","Clicked");

			formObject.fetchFragment("FATCA", "FATCA", "q_FATCA");
			LoadPickList("cmplx_FATCA_USRelation", " select '--Select--' as description, '' as code  union select convert(varchar,description),code  from ng_master_usrelation order by code");
			LoadPickList("cmplx_FATCA_Category", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_category with (nolock) order by code");
			String usRelation = formObject.getNGValue("cmplx_FATCA_USRelation");
			if("O".equalsIgnoreCase(usRelation)){
				formObject.setEnabled("FATCA_Frame6", false);
				formObject.setEnabled("cmplx_FATCA_USRelation",true);
				formObject.setEnabled("FATCA_Save", true);
			}
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}

		}	

		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			hm.put("KYC","Clicked");

			formObject.fetchFragment("KYC", "KYC", "q_KYC");

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}

		}	

		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			hm.put("OECD","Clicked");

			formObject.fetchFragment("OECD", "OECD", "q_OECD");
			loadPickListOECD();


			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}

		}	

		else if ("LoanDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("LoanDetails","Clicked");
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
		            mLogger.info("PL Initiation", "value of los: value of dbr"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalDBR"));
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

		else if ("Self_Employed".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Self_Employed","Clicked");

			formObject.fetchFragment("Self_Employed", "SelfEmployed", "q_SelfEmployed");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("DecisionHistory","Clicked");

			try{
				formObject.fetchFragment("DecisionHistory", "DecisionHistory", "q_Decision");
				formObject.setVisible("cmplx_Decision_Manual_deviation_reason", false);
				loadInDecGrid();
				loadPicklist3();
				if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Manual_Deviation"))){
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
				mLogger.info("sQuery list size"+sQuery);
				//mLogger.info("cmplx_DEC_HighDeligatinAuth list size"+OutputXML.size(), "value is "+OutputXML.get(0).get(0));
				//mLogger.info("cmplx_DEC_HighDeligatinAuth list size"+OutputXML.size(), "value is ");
				if(!OutputXML.isEmpty()){
					String HighDel=OutputXML.get(0).get(0);
					if(OutputXML.get(0).get(0)!=null && !OutputXML.get(0).get(0).isEmpty() &&  !OutputXML.get(0).get(0).equals("") && !OutputXML.get(0).get(0).equalsIgnoreCase("null") ){

						mLogger.info("Inside the if dectech"+HighDel+" value is ");	
						formObject.setNGValue("cmplx_Decision_Highest_delegauth", HighDel);

					}
				}	
				//Code Change By aman to save Highest Delegation Auth


			}
			catch(Exception ex){
				printException(ex);
			}

			//added Tanshu Aggarwal(22/06/2017)
			if	("Y".equalsIgnoreCase(formObject.getNGValue("Is_Account_Create")) && "Y".equalsIgnoreCase(formObject.getNGValue("Is_Customer_Create")))
			{
				mLogger.info("inside both req Y");
				formObject.setLocked("DecisionHistory_Button2", true);
			}
			else{
				mLogger.info( "inside both req N");
				formObject.setLocked("DecisionHistory_Button2", false);
			}
			//added Tanshu Aggarwal(22/06/2017)			
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Inc_Doc".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Inc_Doc","Clicked");


			formObject.fetchFragment("Inc_Doc", "IncomingDoc", "q_IncomingDoc");
			fetchIncomingDocRepeater();
			String activName = formObject.getWFActivityName();
			if("DDVT_Checker".equalsIgnoreCase(activName)){
				if("false".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP")) && "false".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")))
					formObject.setVisible("IncomingDoc_UploadSig",true);
				else if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP")) || "true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
					formObject.setVisible("IncomingDoc_ViewSIgnature",false);
					formObject.setVisible("IncomingDoc_UploadSig",true);
					//formObject.setEnabled("IncomingDoc_UploadSig",false);
				}
			}
			else if("DDVT_Maker".equalsIgnoreCase(activName)){
				if("false".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP")) && "false".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
					formObject.setVisible("IncomingDoc_ViewSIgnature",true);
					formObject.setEnabled("IncomingDoc_ViewSIgnature",true);
				}
				else if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP")) || "true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
					formObject.setEnabled("IncomingDoc_ViewSIgnature",false);
					//formObject.setVisible("IncomingDoc_UploadSig",true);
					//formObject.setEnabled("IncomingDoc_UploadSig",false);
				}
			}
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Post_Disbursal".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			hm.put("Post_Disbursal","Clicked");



			mLogger.info("Inside PostDisbursal-->FrameExpandEvent()");

			formObject.fetchFragment("Post_Disbursal", "PostDisbursal", "q_PostDisbursal");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}



		else if ("Loan_Disbursal".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Loan_Disbursal","Clicked");


			formObject.fetchFragment("Loan_Disbursal", "Loan_Disbursal", "q_FinIncident");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("CC_Creation".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("CC_Creation","Clicked");


			formObject.fetchFragment("CC_Creation", "CC_Creation", "q_FinIncident");
			//code
			/*formObject.setNGValue("cmplx_CCCreation_CRN","34123");
	            formObject.setNGValue("cmplx_CCCreation_ECRN","3498798");
	            mLogger.info("PL Initiation", "value of los: value of dbr"+formObject.getNGValue("cmplx_CCCreation_ECRN")+","+formObject.getNGValue("cmplx_CCCreation_CRN"));
			 */
			//code
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Limit_Inc".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Limit_Inc","Clicked");


			formObject.fetchFragment("Limit_Inc", "Limit_Inc", "q_FinIncident");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Cust_Det_Ver".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Cust_Det_Ver","Clicked");


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
			finally{
				hm.clear();
			}
		}

		else if ("Business_Verif".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Business_Verif","Clicked");


			formObject.fetchFragment("Business_Verif", "BussinessVerification1", "q_bussverification1");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Emp_Verification".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Emp_Verification","Clicked");


			formObject.fetchFragment("Emp_Verification", "EmploymentVerification", "q_EmpVerification");
			EmpVervalues();
			LoadPickList("cmplx_EmploymentVerification_OffTelnoValidatedfrom", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_OffTelnoVal with (nolock)");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Bank_Check".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Bank_Check","Clicked");


			formObject.fetchFragment("Bank_Check", "BankingCheck", "q_BankingCheck");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Note_Details".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Note_Details","Clicked");

			mLogger.info("inside NOte_Details frame expanded event");  			
			formObject.fetchFragment("Note_Details", "NotepadDetailsFCU", "q_Note");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Supervisor_section".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Supervisor_section","Clicked");


			formObject.fetchFragment("Supervisor_section", "supervisorsection", "q_Supervisor");
			LoadPickList("cmplx_FcuDecision_ReferralReason", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_Master_Referralreason with (nolock)");
			LoadPickList("cmplx_FcuDecision_ReferralSubReason", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_Master_Referralsubreason with (nolock)");

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		} 

		else if ("Smart_chk".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Smart_chk","Clicked");


			formObject.fetchFragment("Smart_chk", "SmartCheck1", "q_SmartCheckFCU");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Part_Match".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Part_Match","Clicked");


			formObject.fetchFragment("Part_Match", "PartMatch", "q_PartMatch");
			//added by saurabh on 10th July 17 for DDVT_MAKER.
			partMatchValues();
			LoadPickList("PartMatch_nationality", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_MASTER_Country with (nolock)");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("FinacleCRM_Incidents".equalsIgnoreCase(pEvent.getSource().getName())){					
			hm.put("FinacleCRM_Incidents","Clicked");

			formObject.fetchFragment("FinacleCRM_Incidents", "FinacleCRMIncident", "q_FinIncident");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("FinacleCRM_CustInfo".equalsIgnoreCase(pEvent.getSource().getName())){ 
			hm.put("FinacleCRM_CustInfo","Clicked");

			formObject.fetchFragment("FinacleCRM_CustInfo", "FinacleCRMCustInfo", "q_FinCRMCustInfo");
			formObject.setNGValue("FinacleCRMCustInfo_WINAME",formObject.getWFWorkitemName());
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}

		}

		else if ("Finacle_Core".equalsIgnoreCase(pEvent.getSource().getName())){ 
			hm.put("Finacle_Core","Clicked");
			formObject.fetchFragment("Finacle_Core", "FinacleCore", "q_Finaclecore");
			loadPicklistDDSReturn();
			try{
				String query="select AcctType,'NA',AcctId,AcctNm,AccountOpenDate,AcctStat,'NA',AvailableBalance,'NA','NA','NA' from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
				List<List<String>> list_acc=formObject.getDataFromDataSource(query);
				for(List<String> mylist : list_acc)
				{
					mLogger.info( "Data to be added in cmplx_FinacleCore_FinaclecoreGrid Grid: "+mylist.toString());
					formObject.addItemFromList("cmplx_FinacleCore_FinaclecoreGrid", mylist);
				}

				query="select AcctId,LienId,LienAmount,LienRemarks,LienReasonCode,LienStartDate,LienExpDate from ng_rlos_FinancialSummary_LienDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
				//changed ended
				List<List<String>> list_lien=formObject.getDataFromDataSource(query);
				for(List<String> mylist : list_lien)
				{
					mLogger.info( "Data to be added in Grid: "+mylist.toString());
					formObject.addItemFromList("cmplx_FinacleCore_liendet_grid", mylist);
				}
				//changed here in this query
				query="select AcctId,'','','','' from ng_rlos_FinancialSummary_SiDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
				List<List<String>> list_SIDet=formObject.getDataFromDataSource(query);
				//changed ended

				for(List<String> mylist : list_SIDet)
				{
					mLogger.info("Data to be added in Grid: "+mylist.toString());

					formObject.addItemFromList("cmplx_FinacleCore_sidet_grid", mylist);
				}
			}
			catch(Exception e){
				mLogger.info("Exception while setting data in grid:"+e.getMessage());
				throw new ValidatorException(new FacesMessage("Error while setting data in account grid"));
			}

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}


		}

		else if ("MOL".equalsIgnoreCase(pEvent.getSource().getName())){ 
			hm.put("MOL","Clicked");

			formObject.fetchFragment("MOL", "MOL1", "q_MOL");
			loadPicklistMol();
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}

		}

		else if ("World_Check".equalsIgnoreCase(pEvent.getSource().getName())){ 
			hm.put("World_Check","Clicked");

			formObject.fetchFragment("World_Check", "WorldCheck1", "q_WorldCheck");
			loadPicklist_WorldCheck();
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}

		}

		else if ("Reject_Enq".equalsIgnoreCase(pEvent.getSource().getName())){ 
			hm.put("Reject_Enq","Clicked");

			formObject.fetchFragment("Reject_Enq", "RejectEnq", "q_RejectEnq");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}

		}

		else if ("Sal_Enq".equalsIgnoreCase(pEvent.getSource().getName())){ 
			hm.put("Sal_Enq","Clicked");

			formObject.fetchFragment("Sal_Enq", "SalaryEnq", "q_SalaryEnq");
			try{
				String query="select SalCreditDate,SalCreditMonth,AcctId,SalAmount from ng_rlos_FinancialSummary_SalTxnDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
				List<List<String>> list_acc=formObject.getDataFromDataSource(query);
				for(List<String> mylist : list_acc)
				{
					mLogger.info( "Data to be added in account Grid account Grid: "+mylist.toString());
					formObject.addItemFromList("cmplx_SalaryEnq_SalGrid", mylist);
				}

			}
			finally{
				hm.clear();
			}

		}				


		/*else if (.equalsIgnoreCase(pEvent.getSource().getName()"Notepad_Details")) {
			hm.put("Notepad_Details","Clicked");


			formObject.fetchFragment("Notepad_Details", "CAD", "q_DECCAD");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
 hm.clear();
}
		} */

		else if ("Dec".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Dec","Clicked");


			formObject.fetchFragment("Dec", "CAD_Decision", "q_CadDecision");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("ReferHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("ReferHistory","Clicked");


			formObject.fetchFragment("ReferHistory", "ReferHistory", "q_ReferHistory");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Cust_Detail_verification".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Cust_Detail_verification","Clicked");

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
			finally{
				hm.clear();
			}
		}

		else if ("Business_verification".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Business_verification","Clicked");


			formObject.fetchFragment("Business_verification", "BussinessVerification", "q_businessverification");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Home_country_verification".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Home_country_verification","Clicked");


			formObject.fetchFragment("Home_country_verification", "HomeCountryVerification", "q_HomeCountryVeri");
			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_HCountryVerification_Hcountrytelverified");
			LoadPicklistVerification(LoadPicklist_Verification);
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Residence_verification".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Residence_verification","Clicked");


			formObject.fetchFragment("Residence_verification", "ResidenceVerification", "q_ResiVerification");
			LoadPickList("cmplx_ResiVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision order by code");


			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Guarantor_verification".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Guarantor_verification","Clicked");


			formObject.fetchFragment("Guarantor_verification", "GuarantorVerification", "q_GuarantorVer");
			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_GuarantorVerification_verdoneonmob");
			LoadPicklistVerification(LoadPicklist_Verification);

			LoadPickList("cmplx_GuarantorVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision order by code");

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Reference_detail_verification".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Reference_detail_verification","Clicked");


			formObject.fetchFragment("Reference_detail_verification", "ReferenceDetailVerification", "q_RefDetVer");
			LoadPickList("cmplx_RefDetVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision order by code");

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Office_verification".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Office_verification","Clicked");

			if(!"true".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_contactableFlag"))){
				formObject.fetchFragment("Office_verification", "OfficeandMobileVerification", "q_OffVerification");
				List<String> LoadPicklist_Verification= Arrays.asList("cmplx_OffVerification_hrdemailverified","cmplx_OffVerification_colleaguenoverified","cmplx_OffVerification_fxdsal_ver","cmplx_OffVerification_accpvded_ver","cmplx_OffVerification_desig_ver","cmplx_OffVerification_doj_ver","cmplx_OffVerification_cnfminjob_ver");
				LoadPicklistVerification(LoadPicklist_Verification);
				OfficeVervalues();
				LoadPickList("cmplx_OffVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision order by code");

				LoadPickList("cmplx_OffVerification_offtelnovalidtdfrom", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_OffTelnoVal with (nolock)");

				//++ below code added by abhishek to check for enable/disable of offverification frag as per FSD 2.7.3
				if("Smart_CPV".equalsIgnoreCase(formObject.getWFActivityName())){


					String EnableFlagValue = formObject.getNGValue("cmplx_OffVerification_EnableFlag");
					String sQuery =" SELECT ProcessInstanceID,lockStatus FROM WFINSTRUMENTTABLE WHERE activityname = 'CPV' AND ProcessInstanceID ='"+formObject.getWFWorkitemName()+"'";
					mLogger.info("Ccinside office verification load in smart cpv workstep-->query is ::"+sQuery);
					List<List<String>> list=formObject.getDataFromDataSource(sQuery);
					mLogger.info("Ccinside office verification load in smart cpv workstep-->list is ::"+list+"size is::"+list.size());

					if(list.isEmpty()){

						formObject.setLocked("OfficeandMobileVerification_Frame1",true);
					}
					else{
						String lockStatus = list.get(0).get(1);
						if("Y".equalsIgnoreCase(lockStatus)){
							if("true".equalsIgnoreCase(EnableFlagValue)){
								formObject.setLocked("OfficeandMobileVerification_Frame1",true);
							}else{
								formObject.setLocked("OfficeandMobileVerification_Frame1",false);
								formObject.setEnabled("OfficeandMobileVerification_Enable", false);
								formObject.setEnabled("cmplx_OffVerification_offtelno",false);
								formObject.setEnabled("cmplx_OffVerification_fxdsal_val",false);
								formObject.setEnabled("cmplx_OffVerification_accprovd_val",false);
								formObject.setEnabled("cmplx_OffVerification_desig_val",false);
								formObject.setEnabled("cmplx_OffVerification_doj_val",false);
								formObject.setEnabled("cmplx_OffVerification_cnfrminjob_val",false);

							}
						}else{
							formObject.setLocked("OfficeandMobileVerification_Frame1",false);
							formObject.setEnabled("OfficeandMobileVerification_Enable", false);
							formObject.setEnabled("cmplx_OffVerification_offtelno",false);
							formObject.setEnabled("cmplx_OffVerification_fxdsal_val",false);
							formObject.setEnabled("cmplx_OffVerification_accprovd_val",false);
							formObject.setEnabled("cmplx_OffVerification_desig_val",false);
							formObject.setEnabled("cmplx_OffVerification_doj_val",false);
							formObject.setEnabled("cmplx_OffVerification_cnfrminjob_val",false);
						}
					}


				}
			}else{
				formObject.setEnabled("OfficeandMobileVerification_Frame1", false);
			}

			//-- Above code added by abhishek to check for enable/disable of offverification frag as per FSD 2.7.3
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Loan_card_details".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Loan_card_details","Clicked");


			formObject.fetchFragment("Loan_card_details", "LoanandCard", "q_LoanandCard");
			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_LoanandCard_loanamt_ver","cmplx_LoanandCard_tenor_ver","cmplx_LoanandCard_emi_ver","cmplx_LoanandCard_islorconv_ver","cmplx_LoanandCard_firstrepaydate_ver","cmplx_LoanandCard_cardtype_ver","cmplx_LoanandCard_cardlimit_ver");
			LoadPicklistVerification(LoadPicklist_Verification);
			LoadPickList("cmplx_LoanandCard_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision order by code");

			loancardvalues();
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Notepad_details".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Notepad_details","Clicked");


			formObject.fetchFragment("Notepad_details", "NotepadDetails", "q_Note");
			LoadPickList("NotepadDetails_notedesc", "select '--Select--' union select  description from ng_master_notedescription");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Smart_check".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Smart_check","Clicked");

			//++ below code added by abhishek to load smartcheck and check for button flag as per FSD 2.7.3
			if(!"true".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_contactableFlag"))){
				formObject.fetchFragment("Smart_check", "SmartCheck", "q_SmartCheck");

			}
			else{
				formObject.setEnabled("SmartCheck_Frame1", false);
			}
			//-- Above code added by abhishek to load smartcheck and check for button flag as per FSD 2.7.3
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Orig_validation".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Orig_validation","Clicked");


			formObject.fetchFragment("Orig_validation", "OriginalValidation", "q_OrigVal");
			fetchoriginalDocRepeater();
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		//New fragment added by Arun on 17/7/17 for new DDVT changes on screen by shashank 
		else if ("Risk_Rating".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Risk_Rating","Clicked");


			formObject.fetchFragment("Risk_Rating", "RiskRating", "q_riskrating");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Notepad_Values".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Notepad_Values","Clicked");

			//++ below code added by abhishek to load notepad description  as per FSD 2.7.3
			if(!"true".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_contactableFlag"))){
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


				LoadPickList("NotepadDetails_notedesc", "select '--Select--'  union select  description from ng_master_notedescription");
				formObject.setNGValue("NotepadDetails_notedesc","--Select--");
				formObject.setLocked("NotepadDetails_notecode", true);

			}else{
				formObject.setEnabled("NotepadDetails_Frame1", false);
			}
			//-- Above code added by abhishek to load notepad description as per FSD 2.7.3
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Card_Details".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Card_Details","Clicked");


			formObject.fetchFragment("Card_Details", "CardDetails", "q_CardDetails");

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Credit_card_Enq".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Credit_card_Enq","Clicked");


			formObject.fetchFragment("Credit_card_Enq", "CreditCardEnq", "q_CCEnq");

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Case_History".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Case_History","Clicked");


			formObject.fetchFragment("Case_History", "CaseHistoryReport", "q_CaseHist");

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("LOS".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("LOS","Clicked");


			formObject.fetchFragment("LOS", "LOS", "q_LOS");

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("External_Blacklist".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("External_Blacklist","Clicked");


			formObject.fetchFragment("External_Blacklist", "ExternalBlackList", "q_ExternalBlackList");

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Compliance".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Compliance","Clicked");


			formObject.fetchFragment("Compliance", "Compliance", "q_compliance");

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}


	}

	public void value_Change(ComponentEvent pEvent){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

		if ("cmplx_Decision_Decision".equalsIgnoreCase(pEvent.getSource().getName())) {
			if(formObject.getWFActivityName().equalsIgnoreCase("CAD_Analyst1"))	
			{
				formObject.setNGValue("CAD_dec", formObject.getNGValue("cmplx_Decision_Decision"));
				mLogger.info(" In PL_Initiation VALChanged---New Value of CAD_dec is: "+ formObject.getNGValue("Decision_Combo2"));

			}

			else{				 
				formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
				mLogger.info(" In PL_Initiation VALChanged---New Value of decision is: "+ formObject.getNGValue("Decision_Combo2"));
			}
		}

		if ("cmplx_Customer_DOb".equalsIgnoreCase(pEvent.getSource().getName())){
			//SKLogger_CC.writeLog("CC val change ", "Value of dob is:"+formObject.getNGValue("cmplx_Customer_DOb"));
			getAge(formObject.getNGValue("cmplx_Customer_DOb"),"cmplx_Customer_age");
		}
		if ("cmplx_EmploymentDetails_DOJ".equalsIgnoreCase(pEvent.getSource().getName())){
			//SKLogger.writeLog("RLOS val change ", "Value of dob is:"+formObject.getNGValue("cmplx_EmploymentDetails_DOJ"));
			getAge(formObject.getNGValue("cmplx_EmploymentDetails_DOJ"),"cmplx_EmploymentDetails_LOS");
		}             

		if ("WorldCheck1_Dob".equalsIgnoreCase(pEvent.getSource().getName())){
			mLogger.info("RLOS val change "+ "Value of WorldCheck1_Dob is:"+formObject.getNGValue("WorldCheck1_Dob"));
			//Changes done to auto populate age in world check fragment             
			getAge(formObject.getNGValue("WorldCheck1_Dob"),"WorldCheck1_age");
		}


		if ("ReqProd".equalsIgnoreCase(pEvent.getSource().getName())){
			int prd_count=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			if(prd_count>0){
				String ReqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
				loadPicklistProduct(ReqProd);
			}
		}

		else if ("SubProd".equalsIgnoreCase(pEvent.getSource().getName())){
			mLogger.info("PL val change "+ "Value of SubProd is:"+formObject.getNGValue("SubProd"));
			formObject.clear("AppType");
			formObject.setNGValue("AppType","--Select--");
			if("Business titanium Card".equalsIgnoreCase(formObject.getNGValue("SubProd"))){
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='BTC'");
				formObject.setNGValue("EmpType","Self Employed");
			}              
			else if("Instant Money".equalsIgnoreCase(formObject.getNGValue("SubProd")))
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType  with (nolock) where subProdFlag='IM'");

			else if("Limit Increase".equalsIgnoreCase(formObject.getNGValue("SubProd")))
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='LI'");

			else if("Salaried Credit Card".equalsIgnoreCase(formObject.getNGValue("SubProd")))
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='SAL'");

			else if("Self Employed Credit Card".equalsIgnoreCase(formObject.getNGValue("SubProd")))
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='SE'");

			else if("Expat Personal Loans".equalsIgnoreCase(formObject.getNGValue("SubProd")))
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='EXP'");

			else if("National Personal Loans".equalsIgnoreCase(formObject.getNGValue("SubProd")))
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='NAT'");

			else if("Pre-Approved".equalsIgnoreCase(formObject.getNGValue("SubProd")))
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='PA'");

			else if("Product Upgrade".equalsIgnoreCase(formObject.getNGValue("SubProd")))
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='PU'");
		}

		if ("cmplx_EmploymentDetails_EmpIndusSector".equalsIgnoreCase(pEvent.getSource().getName())){

			LoadPickList("cmplx_EmploymentDetails_Indus_Macro", "select '--Select--' union select convert(varchar, macro) from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where IndustrySector='"+formObject.getNGValue("cmplx_EmploymentDetails_EmpIndusSector")+"' and IsActive='Y'");
		}

		if ("cmplx_EmploymentDetails_Indus_Macro".equalsIgnoreCase(pEvent.getSource().getName())){
			LoadPickList("cmplx_EmploymentDetails_Indus_Micro", "select '--Select--' union select convert(varchar, micro) from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where Macro='"+formObject.getNGValue("cmplx_EmploymentDetails_Indus_Macro")+"' and IsActive='Y'");
		}

		//Added by Arun (21/09/17)
		if ("NotepadDetails_notedesc".equalsIgnoreCase(pEvent.getSource().getName())){
			String notepad_desc = formObject.getNGValue("NotepadDetails_notedesc");
			//LoadPickList("NotepadDetails_notecode", "select '--Select--' union select convert(varchar, description) from ng_master_notedescription with (nolock)  where Description=q'["+notepad_desc+"]'","NotepadDetails_notecode");
			String sQuery = "select code,workstep from ng_master_notedescription where Description='" +  notepad_desc + "'";
			mLogger.info(" query is "+sQuery);
			List<List<String>> recordList = formObject.getDataFromDataSource(sQuery);
			if(recordList.get(0).get(0)!= null && recordList.get(0)!=null && !"".equalsIgnoreCase(recordList.get(0).get(0)) && recordList!=null)
			{
				formObject.setNGValue("NotepadDetails_notecode",recordList.get(0).get(0));
				formObject.setNGValue("NotepadDetails_Workstep",recordList.get(0).get(1));
			}

		}

		if("cmplx_Decision_Feedbackstatus".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			if(formObject.getWFActivityName().equalsIgnoreCase("FCU"))
			{
				mLogger.info(" queryarun is "+"inside feedback status");
				LoadpicklistFCU();
				mLogger.info(" queryarun is "+"inside feedback status after loadpicklist");
			} 
		}

	}

	public void mouse_Clicked(ComponentEvent pEvent){

		mLogger.info( "PersonalLoanSCommonCode--->Inside mouse_Clicked()");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String outputResponse = "";
		String ReturnCode="";
		String ReturnDesc="";
		String BlacklistFlag="";
		String DuplicationFlag="";
		String SystemErrorCode="";
		HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
		PL_Integration_Input genX=new PL_Integration_Input();
		String alert_msg="";

		//disha FSD
		if ("AddressDetails_addr_Add".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setNGValue("Address_wi_name",formObject.getWFWorkitemName());
			mLogger.info( "Inside add button: "+formObject.getNGValue("Address_wi_name"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_AddressDetails_cmplx_AddressGrid");
		}

		if ("AddressDetails_addr_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_AddressDetails_cmplx_AddressGrid");
		}

		if ("AddressDetails_addr_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_AddressDetails_cmplx_AddressGrid");

		}

		//Arun added (14-jun)
		if ("ExtLiability_Add".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setNGValue("Liability_Wi_Name",formObject.getWFWorkitemName());
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
		}

		if ("ExtLiability_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
		}

		if ("ExtLiability_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
		}

		if ("FATCA_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			String text=formObject.getNGItemText("cmplx_FATCA_listedreason", formObject.getSelectedIndex("cmplx_FATCA_listedreason"));
			mLogger.info( "Inside FATCA_Button1 "+text);
			formObject.addItem("cmplx_FATCA_selectedreason", text);
			try {
				formObject.removeItem("cmplx_FATCA_listedreason", formObject.getSelectedIndex("cmplx_FATCA_listedreason"));
				formObject.setSelectedIndex("cmplx_FATCA_listedreason", -1);

			}catch (Exception e) {
				// TODO Auto-generated catch block
				printException(e);
			}

		}

		if ("FATCA_Button2".equalsIgnoreCase(pEvent.getSource().getName())){
			mLogger.info( "Inside FATCA_Button2 ");
			formObject.addItem("cmplx_FATCA_listedreason", formObject.getNGItemText("cmplx_FATCA_selectedreason", formObject.getSelectedIndex("cmplx_FATCA_selectedreason")));
			try {
				formObject.removeItem("cmplx_FATCA_selectedreason", formObject.getSelectedIndex("cmplx_FATCA_selectedreason"));
				formObject.setSelectedIndex("cmplx_FATCA_selectedreason", -1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				printException(e);
			}
		}
		if("ReferenceDetails_Reference_Add".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setNGValue("ReferenceDetails_reference_wi_name",formObject.getWFWorkitemName());
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_ReferenceDetails_cmplx_ReferenceGrid");			
		}

		if ("ReferenceDetails_Reference__modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_ReferenceDetails_cmplx_ReferenceGrid");
		}

		if ("ReferenceDetails_Reference_delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_ReferenceDetails_cmplx_ReferenceGrid");
		}

		if("OECD_add".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setNGValue("OECD_winame",formObject.getWFWorkitemName());
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_OECD_cmplx_GR_OecdDetails");			
		}

		if ("OECD_modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_OECD_cmplx_GR_OecdDetails");
		}

		if ("OECD_delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_OECD_cmplx_GR_OecdDetails");
		}

		if ("Product_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Product_cmplx_ProductGrid");
		}

		if("GuarantorDetails_add".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setNGValue("GuarantorDetails_guarantor_WIName",formObject.getWFWorkitemName());
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Guarantror_GuarantorDet");			
		}

		if ("GuarantorDetails_modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Guarantror_GuarantorDet");
		}

		if ("GuarantorDetails_delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_Guarantror_GuarantorDet");
		}
		//disha FSD
		if ("NotepadDetails_Add".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setNGValue("Notepad_wi_name",formObject.getWFWorkitemName());
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_notegrid");

			String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
			mLogger.info( "sActivityName after add row $ "+sActivityName+"$");
			int user_id = formObject.getUserId();
			String user_name = formObject.getUserName();
			user_name = user_name+"-"+user_id;		

			mLogger.info( "user_name after add row $ "+user_name+"$");
			formObject.setNGValue("NotepadDetails_insqueue",sActivityName,false);
			formObject.setNGValue("NotepadDetails_Actusername",user_name,false); 
			formObject.setNGValue("NotepadDetails_user",user_name,false);

			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date date = new Date();
			formObject.setNGValue("NotepadDetails_noteDate",dateFormat.format(date),false); 
			formObject.setNGValue("NotepadDetails_Actdate",dateFormat.format(date),false); 
		}
		if ("NotepadDetails_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_NotepadDetails_cmplx_notegrid");
		}
		if ("NotepadDetails_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_NotepadDetails_cmplx_notegrid");

		}




		if ("IncomingDoc_AddFromPCButton".equalsIgnoreCase(pEvent.getSource().getName())){
			IRepeater repObj=null;
			repObj = formObject.getRepeaterControl("IncomingDoc_Frame2");
			repObj.getValue(repObj.getChangeIndex(),"IncomingDoc_Label1");
			mLogger.info("value of repeater:"+repObj.getValue(repObj.getChangeIndex(),"IncomingDoc_Label1"));
		}

		if("FetchDetails".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			String ResideSince="";
			//String Gender="";

			//if(formObject.getNGValue("cmplx_Customer_CardNotAvailable")=="true")                                    

			formObject.setNGValue("cmplx_Customer_ISFetchDetails", "Y");

			//Deepak Code change for Entity Details
			//if("Is_EID_Genuine".equalsIgnoreCase("Y") && "cmplx_Customer_EmiratesID" != ""){
			String EID = formObject.getNGValue("cmplx_Customer_EmiratesID");
			mLogger.info( "EID value for Entity Details: "+EID );

			if(formObject.getNGValue("cmplx_Customer_card_id_available")=="false"){
				mLogger.info("RLOS value of Customer Details"+formObject.getNGValue("cmplx_Customer_card_id_available"));
				outputResponse =genX.GenerateXML("CUSTOMER_ELIGIBILITY","");
				ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				mLogger.info("RLOS value of ReturnCode"+ReturnCode);

				if("0000".equalsIgnoreCase(ReturnCode)|| "000".equalsIgnoreCase(ReturnCode)){
					PL_Integration_Output.valueSetIntegration(outputResponse);    
					formObject.setNGValue("Is_Customer_Eligibility","Y");
					parse_cif_eligibility(outputResponse);
					String NTB_flag = formObject.getNGValue("cmplx_Customer_NTB");
					//Customer_enable();
					// if(NTB_flag.equalsIgnoreCase("true")){
					//  Customer_enable();
					//}
					if("true".equalsIgnoreCase(NTB_flag)){
						setcustomer_enable();
						try
						{
							mLogger.info("inside Customer Eligibility to through Exception to Exit:");
							throw new ValidatorException(new FacesMessage("Customer is a New to Bank Customer."));

						}
						finally{
							hm.clear();
						}
					}
					else{
						outputResponse = genX.GenerateXML("CUSTOMER_DETAILS","Primary_CIF");
						try
						{
							String Date1=formObject.getNGValue("cmplx_Customer_DOb");
							SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-mm-dd");
							SimpleDateFormat sdf2=new SimpleDateFormat("dd/mm/yyyy");
							String Datechanged=sdf2.format(sdf1.parse(Date1));
							mLogger.info("RLOS value ofDatechanged"+Datechanged);
							formObject.setNGValue("cmplx_Customer_DOb",Datechanged);
						}        
						catch(Exception ex){                            
							PLCommon.printException(ex);
						}

						//mLogger.info("RLOS value of Gender"+Gender);
						String  Marital_Status =  outputResponse.contains("<MaritialStatus>") ? outputResponse.substring(outputResponse.indexOf("<MaritialStatus>")+"</MaritialStatus>".length()-1,outputResponse.indexOf("</MaritialStatus>")):"";
						mLogger.info("RLOS value of Marital_Status"+Marital_Status);
						ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
						mLogger.info("RLOS value of ReturnCode"+ReturnCode);
						ResideSince =  outputResponse.contains("<ResideSince>") ? outputResponse.substring(outputResponse.indexOf("<ResideSince>")+"</ResideSince>".length()-1,outputResponse.indexOf("</ResideSince>")):"";    
						mLogger.info("RLOS value of ResideSince"+ResideSince);



						if("0000".equalsIgnoreCase(ReturnCode)|| "000".equalsIgnoreCase(ReturnCode)){
							formObject.setNGValue("Is_Customer_Details","Y");
							formObject.RaiseEvent("WFSave");
							mLogger.info("RLOS value of Is_Customer_Details"+formObject.getNGValue("Is_Customer_Details"));


							formObject.fetchFragment("Address_Details_Container", "AddressDetails","q_AddressDetails");                          
							formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
							formObject.setTop("ReferenceDetails",formObject.getTop("Address_Details_Container")+formObject.getHeight("Address_Details_Container"));
							formObject.setTop("Alt_Contact_container",formObject.getTop("ReferenceDetails")+25);
							formObject.setTop("CardDetails", formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container"));
							if(formObject.isVisible("Supplementary_Container")){
								formObject.setTop("Supplementary_Container",formObject.getTop("CardDetails")+25);
								formObject.setTop("FATCA",formObject.getTop("Supplementary_Container")+25);
								formObject.setTop("KYC", formObject.getTop("FATCA")+25);
								formObject.setTop("OECD", formObject.getTop("KYC")+25);
								//Java takes height of entire container in getHeight while js takes current height         i.e collapsed/expanded
							}
							else{
								formObject.setTop("FATCA",formObject.getTop("CardDetails")+25);
								formObject.setTop("KYC", formObject.getTop("FATCA")+25);
								formObject.setTop("OECD", formObject.getTop("KYC")+25);
							}
							PL_Integration_Output.valueSetIntegration(outputResponse);
							//code to set Emirates of residence start.
							int n=formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
							if(n>0){
								for(int i=0;i<n;i++){
									mLogger.info("selecting Emirates of residence: "+formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid"+ i+ 0));
									if(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0).equalsIgnoreCase("RESIDENCE")){
										mLogger.info("selecting Emirates of residence: settign value: "+formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid"+ i+ 0));
										formObject.setNGValue("cmplx_Customer_EmirateOfResidence",formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 6));
									}
								}
								//code to set Emirates of residence End.

								throw new ValidatorException(new FacesMessage("Existing Customer Details fetched Sucessfully"));
							}
							//code change to save the date in desired format by AMAN        
							try{
								mLogger.info("converting date entered"+formObject.getNGValue("cmplx_Customer_DOb")+"");
								mLogger.info("converting date enteredID"+formObject.getNGValue("cmplx_Customer_IdIssueDate")+"");
								mLogger.info("converting date enteredPASS"+formObject.getNGValue("cmplx_Customer_PassPortExpiry")+"");
								mLogger.info("converting date enteredVISA"+formObject.getNGValue("cmplx_Customer_VIsaExpiry")+"");

								String str_dob=formObject.getNGValue("cmplx_Customer_DOb");
								SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-mm-dd");
								SimpleDateFormat sdf2=new SimpleDateFormat("dd/mm/yyyy");

								if(str_dob!=null&&!str_dob.equalsIgnoreCase("")){
									String n_str_dob=sdf2.format(sdf1.parse(str_dob));

									mLogger.info("converting date entered"+n_str_dob+"asd");
									formObject.setNGValue("cmplx_Customer_DOb",n_str_dob,false);

								}

							}
							catch(Exception e){
								mLogger.info("Exception Occur while converting date"+e+"");

							}              
						}
						else{
							formObject.setNGValue("Is_Customer_Details","N");

							alert_msg = NGFUserResourceMgr_PL.getResourceString_plValidations("val029");//"Error in fetch Customer details operation";
							mLogger.info("PL DDVT Maker value of ReturnCode"+"Error in Customer Eligibility: "+ ReturnCode);
						}

						formObject.RaiseEvent("WFSave");
					}
					if(formObject.getNGValue("Is_Customer_Eligibility").equalsIgnoreCase("Y") && formObject.getNGValue("Is_Customer_Details").equalsIgnoreCase("Y"))
					{ 
						mLogger.info("RLOS value of Customer Details"+"inside if condition");
						formObject.setEnabled("FetchDetails", false); 
						throw new ValidatorException(new FacesMessage("Customer information fetched sucessfully"));
					}
					else{
						formObject.setEnabled("FetchDetails", true);
					}
					mLogger.info("RLOS value of Customer Details ----1234");
					//formObject.RaiseEvent("WFSave");
					try
					{
						// throw new ValidatorException(new FacesMessage(alert_msg));
						// throw new ValidatorException(new FacesMessage("You have not attached Mandatory Documents: "+mandatoryDocName.toString()));
					}
					finally{
						hm.clear();
					}

				}
				else{
					formObject.setNGValue("Is_Customer_Eligibility","N");
					// alert_msg = "Dedupe check failed.";
					mLogger.info("Dedupe check failed.");
					//  throw new ValidatorException(new FacesMessage("Dedupe check failed."));
					try{
						//  throw new ValidatorException(new FacesMessage(alert_msg));
					}
					finally{
						hm.clear();
					}
				}
				//added
			}
			//ended

			//added
			else if(formObject.getNGValue("cmplx_Customer_card_id_available")=="true"){
				mLogger.info("RLOS value of Customer Details ----1234567"+formObject.getNGValue("cmplx_Customer_card_id_available"));
				mLogger.info("RLOS value of Customer Details ----1234567-->inside true");
				outputResponse = genX.GenerateXML("CUSTOMER_DETAILS","Primary_CIF");
				/*         try
                                               {
                                                               String Date1=formObject.getNGValue("cmplx_Customer_DOb");
                                                               SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-mm-dd");
                                                               SimpleDateFormat sdf2=new SimpleDateFormat("dd/mm/yyyy");
                                                               String Datechanged=sdf2.format(sdf1.parse(Date1));
                                                               mLogger.info("RLOS value ofDatechanged"+Datechanged);
                                                               formObject.setNGValue("cmplx_Customer_DOb",Datechanged);
                                               }        
                                                catch(Exception ex){                            
                                                }
				 */  //Code commented because here it is of no use
				ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				mLogger.info("RLOS value of ReturnCode"+ReturnCode);

				ResideSince =  outputResponse.contains("<ResideSince>") ? outputResponse.substring(outputResponse.indexOf("<ResideSince>")+"</ResideSince>".length()-1,outputResponse.indexOf("</ResideSince>")):"";    
				mLogger.info("RLOS value of ResideSince"+ResideSince);

				if(ReturnCode.equalsIgnoreCase("0000")|| ReturnCode.equalsIgnoreCase("000")){

					formObject.setNGValue("Is_Customer_Details","Y");
					formObject.RaiseEvent("WFSave");
					mLogger.info("RLOS value of CurrentDate Is_Customer_Details"+formObject.getNGValue("Is_Customer_Details"));


					formObject.fetchFragment("Address_Details_Container", "AddressDetails","q_AddressDetails");                          
					formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
					formObject.setTop("ReferenceDetails",formObject.getTop("Address_Details_Container")+formObject.getHeight("Address_Details_Container"));
					formObject.setTop("Alt_Contact_container",formObject.getTop("ReferenceDetails")+25);
					formObject.setTop("CardDetails", formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container"));
					formObject.setEnabled("Customer_Button1", true);      
					mLogger.info("year difference:"+"diffdays difference after button is enabled");
					if(formObject.isVisible("Supplementary_Container")){
						formObject.setTop("Supplementary_Container",formObject.getTop("CardDetails")+25);
						formObject.setTop("FATCA",formObject.getTop("Supplementary_Container")+25);
						formObject.setTop("KYC", formObject.getTop("FATCA")+25);
						formObject.setTop("OECD", formObject.getTop("KYC")+25);
						//Java takes height of entire container in getHeight while js takes current height  i.e collapsed/expanded
					}
					else{
						formObject.setTop("FATCA",formObject.getTop("CardDetails")+25);
						formObject.setTop("KYC", formObject.getTop("FATCA")+25);
						formObject.setTop("OECD", formObject.getTop("KYC")+25);
					}
					PL_Integration_Output.valueSetIntegration(outputResponse);
					//code added here to change the DOB in DD/MM/YYYY format     
					try
					{
						String Date1=formObject.getNGValue("cmplx_Customer_DOb");
						SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-mm-dd");
						SimpleDateFormat sdf2=new SimpleDateFormat("dd/mm/yyyy");
						String Datechanged=sdf2.format(sdf1.parse(Date1));
						mLogger.info("RLOS value ofDatechanged"+Datechanged);
						formObject.setNGValue("cmplx_Customer_DOb",Datechanged);
					}        
					catch(Exception ex){                            
						PLCommon.printException(ex);			
					}
					//         code added here to change the DOB in DD/MM/YYYY format    
					//code to set Emirates of residence start.
					int n=formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
					if(n>0){
						for(int i=0;i<n;i++){
							mLogger.info("selecting Emirates of residence: "+formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid"+ i+ 0));
							if(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0).equalsIgnoreCase("RESIDENCE")){
								mLogger.info("selecting Emirates of residence: settign value: "+formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid"+ i+ 0));
								formObject.setNGValue("cmplx_Customer_EmirateOfResidence",formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 6));
							}
						}
						//code to set Emirates of residence End.
						formObject.setEnabled("FetchDetails", false); 
						throw new ValidatorException(new FacesMessage("Existing Customer Details fetched Sucessfully"));

					}
					//code change to save the date in desired format by AMAN        
					try{
						mLogger.info("converting date entered"+formObject.getNGValue("cmplx_Customer_DOb")+"");
						mLogger.info("converting date enteredID"+formObject.getNGValue("cmplx_Customer_IdIssueDate")+"");
						mLogger.info("converting date enteredPASS"+formObject.getNGValue("cmplx_Customer_PassPortExpiry")+"");
						mLogger.info("converting date enteredVISA"+formObject.getNGValue("cmplx_Customer_VIsaExpiry")+"");

						String str_dob=formObject.getNGValue("cmplx_Customer_DOb");
						mLogger.info("converting date entered"+str_dob+"");
						/*String str_IDissuedate=formObject.getNGValue("cmplx_Customer_IdIssueDate");
						String str_passExpDate=formObject.getNGValue("cmplx_Customer_PassPortExpiry");
						String str_visaExpDate=formObject.getNGValue("cmplx_Customer_VIsaExpiry");
						SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-mm-dd");
						SimpleDateFormat sdf2=new SimpleDateFormat("dd/MM/yyyy");

						 --Unused Code--*/
					}
					catch(Exception e){
						mLogger.info("Exception Occur while converting date"+e+"");
					}
					//code change end to save the date in desired format by AMAN             



				}
				else{
					formObject.setNGValue("Is_Customer_Details","N");
					formObject.RaiseEvent("WFSave");
					mLogger.info("RLOS value of Customer Details456"+formObject.getNGValue("Is_Customer_Details"));
				}
				mLogger.info("RLOS value of Customer Details789"+formObject.getNGValue("Is_Customer_Details"));
			}
			formObject.RaiseEvent("WFSave");
			//ended
		}

		else if("Customer_Button1".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			//if("Is_Entity_Details".equalsIgnoreCase("Y") && "Is_Customer_Details".equalsIgnoreCase("Y")){
			String NegatedFlag="";
			outputResponse =genX.GenerateXML("CUSTOMER_ELIGIBILITY","");
			mLogger.info("RLOS value of ReturnDesc"+"Customer Eligibility");
			ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";


			mLogger.info("RLOS value of ReturnCode"+ReturnCode);

			formObject.setNGValue("Is_Customer_Eligibility","Y");

			if("0000".equalsIgnoreCase(ReturnCode)){
				PL_Integration_Output.valueSetIntegration(outputResponse); 
				parse_cif_eligibility(outputResponse);
				BlacklistFlag= outputResponse.contains("<BlacklistFlag>") ? outputResponse.substring(outputResponse.indexOf("<BlacklistFlag>")+"</BlacklistFlag>".length()-1,outputResponse.indexOf("</BlacklistFlag>")):"";
				mLogger.info("PL value of BlacklistFlag_Part"+"Customer is BlacklistedFlag"+BlacklistFlag);
				DuplicationFlag= outputResponse.contains("<DuplicationFlag>") ? outputResponse.substring(outputResponse.indexOf("<DuplicationFlag>")+"</DuplicationFlag>".length()-1,outputResponse.indexOf("</DuplicationFlag>")):"";
				mLogger.info("PL value of BlacklistFlag_Part"+"Customer is DuplicationFlag"+DuplicationFlag);
				NegatedFlag= outputResponse.contains("<NegatedFlag>") ? outputResponse.substring(outputResponse.indexOf("<NegatedFlag>")+"</NegatedFlag>".length()-1,outputResponse.indexOf("</NegatedFlag>")):"";
				mLogger.info("PL value of BlacklistFlag_Part"+"Customer is NegatedFlag"+NegatedFlag);
				if(ReturnCode.equalsIgnoreCase("0000")|| ReturnCode.equalsIgnoreCase("000")){
					PL_Integration_Output.valueSetIntegration(outputResponse);    
					formObject.setNGValue("Is_Customer_Eligibility","Y");
					formObject.RaiseEvent("WFSave");
					mLogger.info("RLOS value of ReturnDesc Is_Customer_Eligibility"+formObject.getNGValue("Is_Customer_Eligibility"));
					formObject.setNGValue("BlacklistFlag",BlacklistFlag);
					formObject.setNGValue("DuplicationFlag",DuplicationFlag);
					formObject.setNGValue("IsAcctCustFlag",NegatedFlag);
					String NTB_flag = formObject.getNGValue("cmplx_Customer_NTB");
					if("true".equalsIgnoreCase(NTB_flag)){
						mLogger.info("inside Customer Eligibility to through Exception to Exit:");
						alert_msg = NGFUserResourceMgr_PL.getResourceString_plValidations("val030");//"Customer is a New to Bank Customer.";
					}
					else{
						alert_msg = NGFUserResourceMgr_PL.getResourceString_plValidations("val031");//"Customer is an Existing Customer.";
					}

				}
				else{
					formObject.setNGValue("Is_Customer_Eligibility","N");
					formObject.RaiseEvent("WFSave");
				}
				mLogger.info("RLOS value of Customer Details"+formObject.getNGValue("Is_Customer_Eligibility"));
				mLogger.info("RLOS value of BlacklistFlag"+formObject.getNGValue("BlacklistFlag"));
				mLogger.info("RLOS value of DuplicationFlag"+formObject.getNGValue("DuplicationFlag"));
				mLogger.info("RLOS value of IsAcctCustFlag"+formObject.getNGValue("IsAcctCustFlag"));
				formObject.RaiseEvent("WFSave");
				throw new ValidatorException(new FacesMessage(alert_msg));
				//}
			}

		}
		//Customer Details Call on Dedupe Summary Button as well(Tanshu Aggarwal-29/05/2017)


		if ("DecisionHistory_Button2".equalsIgnoreCase(pEvent.getSource().getName())) {

			mLogger.info(""+"inside create cif button");
			outputResponse = genX.GenerateXML("NEW_ACCOUNT_REQ","");
			ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			mLogger.info("RLOS value of ReturnCode"+ReturnCode);
			ReturnDesc =  outputResponse.contains("<ReturnDesc>") ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
			mLogger.info("RLOS value of ReturnDesc"+ReturnDesc);
			String NewAcid= outputResponse.contains("<NewAcid>") ? outputResponse.substring(outputResponse.indexOf("<NewAcid>")+"</NewAcid>".length()-1,outputResponse.indexOf("</NewAcid>")):"";
			String IBANNumber= outputResponse.contains("<IBANNumber>") ? outputResponse.substring(outputResponse.indexOf("<IBANNumber>")+"</IBANNumber>".length()-1,outputResponse.indexOf("</IBANNumber>")):"";
			if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode)){
				PL_Integration_Output.valueSetIntegration(outputResponse);    
				mLogger.info("RLOS value of Account Request"+"after valuesetcudtomer");
				formObject.setNGValue("Is_Account_Create","Y");
				formObject.setNGValue("EligibilityStatus","Y");
				formObject.setNGValue("EligibilityStatusCode","Y");
				formObject.setNGValue("EligibilityStatusDesc","Y");
				formObject.setNGValue("Account_Number",NewAcid);
				formObject.setNGValue("IBAN_Number",IBANNumber);
				//formObject.setNGValue("cmplx_Decision_IBAN",IBANNumber);
				//formObject.setNGValue("cmplx_Decision_AccountNo",NewAcid);
				mLogger.info("RLOS value of Account Request NewAcid"+formObject.getNGValue("Account_Number"));
				mLogger.info("RLOS value of Account Request IBANNumber"+formObject.getNGValue("IBAN_Number"));
				mLogger.info("RLOS value of Account Request cmplx_Decision_IBAN"+formObject.getNGValue("cmplx_Decision_IBAN"));
				mLogger.info("RLOS value of Account Request cmplx_Decision_AccountNo"+formObject.getNGValue("cmplx_Decision_AccountNo"));
			}
			else{
				formObject.setNGValue("Is_Account_Create","N");
			}

			mLogger.info("RLOS value of Account Request"+formObject.getNGValue("Is_Account_Create"));
			mLogger.info("RLOS value of EligibilityStatus"+formObject.getNGValue("EligibilityStatus"));
			mLogger.info("RLOS value of EligibilityStatusCode"+formObject.getNGValue("EligibilityStatusCode"));
			mLogger.info("RLOS value of EligibilityStatusDesc"+formObject.getNGValue("EligibilityStatusDesc"));
			mLogger.info("RLOS value of Account Request NewAcid111"+formObject.getNGValue("Account_Number"));
			mLogger.info("RLOS value of Account Request IBANNumber111"+formObject.getNGValue("IBAN_Number"));

			outputResponse = genX.GenerateXML("NEW_CUSTOMER_REQ","");
			ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			mLogger.info("RLOS value of ReturnCode"+ReturnCode);
			ReturnDesc =  outputResponse.contains("<ReturnDesc>") ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
			mLogger.info("RLOS value of ReturnDesc"+ReturnDesc);
			if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode) ){
				PL_Integration_Output.valueSetIntegration(outputResponse);    
				mLogger.info("PL value of ReturnDesc"+"Inside if of New customer Req");
				formObject.setNGValue("Is_Customer_Create","Y");

			}
			else{
				mLogger.info("PL value of ReturnDesc"+"Inside else of New Customer Req");
				formObject.setNGValue("Is_Customer_Create","N");
			}
			formObject.RaiseEvent("WFSave");

		}


		if ("Product_Add".equalsIgnoreCase(pEvent.getSource().getName())){

			formObject.setNGValue("Product_wi_name",formObject.getWFWorkitemName());
			mLogger.info( "Inside add button: "+formObject.getNGValue("Product_wi_name"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Product_cmplx_ProductGrid");

			formObject.setNGValue("Product_type","Conventional",false);
			formObject.setNGValue("ReqProd","--Select--",false);
			formObject.setNGValue("AppType","--Select--",false);
			formObject.setNGValue("EmpType","--Select--",false);
			formObject.setNGValue("Priority","Primary",false);
			formObject.setVisible("Product_Label5",false);
			formObject.setVisible("Product_Label3",false);
			formObject.setVisible("Product_Label6",false);
			formObject.setVisible("Product_Label10",false);
			formObject.setVisible("Product_Label12",false);
			formObject.setVisible("CardProd",false);
			formObject.setVisible("Scheme",false);
			formObject.setVisible("ReqTenor",false);
			formObject.setVisible("Product_Label15",false); 
			formObject.setVisible("Product_Label17",false); 
			formObject.setVisible("Product_Label16",false); 
			formObject.setVisible("Product_Label18",false);
			formObject.setVisible("Product_Label21",false); 
			formObject.setVisible("Product_Label22",false); 
			formObject.setVisible("Product_Label23",false); 
			formObject.setVisible("Product_Label24",false);
			formObject.setVisible("typeReq",false);
			formObject.setVisible("LimitAcc",false); 
			formObject.setVisible("LimitExpiryDate",false);
			formObject.setNGFrameState("Incomedetails", 1);
			formObject.setNGFrameState("EmploymentDetails", 1);
			formObject.setNGFrameState("EligibilityAndProductInformation", 1);
			formObject.setNGFrameState("Alt_Contact_container", 1);
			formObject.setNGFrameState("CC_Loan_container", 1);
			formObject.setNGFrameState("CompanyDetails", 1);
			formObject.setNGFrameState("CardDetails", 1);
		}              


		else if ("Product_Modify".equalsIgnoreCase(pEvent.getSource().getName())){

			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Product_cmplx_ProductGrid");
			formObject.setNGFrameState("Incomedetails", 1);
			formObject.setNGValue("Product_type","Conventional",false);
			formObject.setNGValue("ReqProd","--Select--",false);
			formObject.setNGValue("AppType","--Select--",false);
			formObject.setNGValue("EmpType","--Select--",false);
			formObject.setNGValue("Priority","Primary",false);
			formObject.setVisible("Product_Label5",false);
			formObject.setVisible("Product_Label3",false);
			formObject.setVisible("Product_Label6",false);
			formObject.setVisible("Product_Label10",false);
			formObject.setVisible("Product_Label12",false);
			formObject.setVisible("CardProd",false);
			formObject.setVisible("Scheme",false);
			formObject.setVisible("ReqTenor",false);
			formObject.setVisible("Product_Label15",false); 
			formObject.setVisible("Product_Label17",false); 
			formObject.setVisible("Product_Label16",false); 
			formObject.setVisible("Product_Label18",false);
			formObject.setVisible("Product_Label21",false); 
			formObject.setVisible("Product_Label22",false); 
			formObject.setVisible("Product_Label23",false); 
			formObject.setVisible("Product_Label24",false);
			formObject.setVisible("typeReq",false);
			formObject.setVisible("LimitAcc",false); 
			formObject.setVisible("LimitExpiryDate",false);
			formObject.setNGFrameState("Incomedetails", 1);
			formObject.setNGFrameState("EmploymentDetails", 1);
			formObject.setNGFrameState("EligibilityAndProductInformation", 1);
			formObject.setNGFrameState("Alt_Contact_container", 1);
			formObject.setNGFrameState("CC_Loan_container", 1);
			formObject.setNGFrameState("CompanyDetails", 1);
			formObject.setNGFrameState("CardDetails", 1);
		}

		else if ("Product_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_Product_cmplx_ProductGrid");

			formObject.setNGValue("Product_type","Conventional",false);
			formObject.setNGValue("ReqProd","--Select--",false);
			formObject.setNGValue("AppType","--Select--",false);
			formObject.setNGValue("EmpType","--Select--",false);
			formObject.setNGValue("Priority","Primary",false);
			formObject.setVisible("Product_Label5",false);
			formObject.setVisible("Product_Label3",false);
			formObject.setVisible("Product_Label6",false);
			formObject.setVisible("Product_Label10",false);
			formObject.setVisible("Product_Label12",false);
			formObject.setVisible("CardProd",false);
			formObject.setVisible("Scheme",false);
			formObject.setVisible("ReqTenor",false);
			formObject.setVisible("Product_Label15",false); 
			formObject.setVisible("Product_Label17",false); 
			formObject.setVisible("Product_Label16",false); 
			formObject.setVisible("Product_Label18",false);
			formObject.setVisible("Product_Label21",false); 
			formObject.setVisible("Product_Label22",false); 
			formObject.setVisible("Product_Label23",false); 
			formObject.setVisible("Product_Label24",false);
			formObject.setVisible("typeReq",false);
			formObject.setVisible("LimitAcc",false); 
			formObject.setVisible("LimitExpiryDate",false);
			formObject.setNGFrameState("Incomedetails", 1);
			formObject.setNGFrameState("EmploymentDetails", 1);
			formObject.setNGFrameState("EligibilityAndProductInformation", 1);
			formObject.setNGFrameState("Alt_Contact_container", 1);
			formObject.setNGFrameState("CC_Loan_container", 1);
			formObject.setNGFrameState("CompanyDetails", 1);
			formObject.setNGFrameState("CardDetails", 1);
		}

		else if ("PartMatch_Search".equalsIgnoreCase(pEvent.getSource().getName())){
			//genX.GenerateXML();
			mLogger.info("PL PartMatch_Search"+ "Inside PartMatch_Search button: ");

			//HashMap<String,String> hm1= new HashMap<String,String>(); // not nullable HashMap

			//hm1.put("PartMatch_Search","Clicked");
			// popupFlag="N";
			formObject.clear("cmplx_PartMatch_cmplx_Partmatch_grid");
			outputResponse = genX.GenerateXML("DEDUP_SUMMARY","");
			ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			mLogger.info("PL value of ReturnCode"+ReturnCode);
			ReturnDesc =  outputResponse.contains("<ReturnDesc>") ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";          
			mLogger.info("PL value of ReturnDesc"+ReturnDesc);
			if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode) ){
				//valueSetIntegration(outputResponse);
				//change by saurabh on 11th July 17.
				PL_Integration_Output.parseDedupe_summary(outputResponse);
				formObject.setNGValue("Is_PartMatchSearch","Y");
				mLogger.info("PL value of Part Match Request"+"inside if of partmatch");
				alert_msg= NGFUserResourceMgr_PL.getResourceString_plValidations("val0032");//"Part match sucessfull";
			}
			else{
				formObject.setNGValue("Is_PartMatchSearch","N");
				mLogger.info("PL value of Part Match Request"+"inside else of partmatch");
				alert_msg= NGFUserResourceMgr_PL.getResourceString_plValidations("val033");//"Error while performing Part match";
			}
			formObject.RaiseEvent("WFSave");
			mLogger.info("PL value of Part Match Request"+formObject.getNGValue("Is_PartMatchSearch"));
			if("Y".equalsIgnoreCase(formObject.getNGValue("Is_PartMatchSearch")))
			{ 
				mLogger.info("PL value of Is_CUSTOMER_UPDATE_REQ"+"inside if condition of disabling the button");

			}
			else{
				formObject.setEnabled("PartMatch_Search", true);
				//            throw new ValidatorException(new CustomExceptionHandler("Dedupe Summary Fail!!","PartMatch_Search#Dedupe Summary Fail!!","",hm));
			}
			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		//for BlackList Call added on 3rd May 2017
		else if ("PartMatch_Blacklist".equalsIgnoreCase(pEvent.getSource().getName())){
			mLogger.info("PL value of ReturnDesc"+"Blacklist Details part Match1111");
			//     formObject.fetchFragment("FinacleCRM_CustInfo", "FinacleCRMCustInfo", "q_FinCRMCustInfo");
			String CifID="";
			String CustomerStatus="";
			String firstName="";
			String LastName="";
			String Negatedflag="";
			String Negatednote="";
			String NegatedCode="";
			String NegatedReason="";
			String Blacklistflag="";
			String Blacklistnote="";
			String BlacklistCode="";
			String BlacklistReason="";
			String OldPassportNumber=""; 
			String PassportNumber="";
			String Visa="";
			String EmirateID="";
			String PhoneNo="";
			//added here
			String StatusType="";
			//ended here
			mLogger.info("Blacklist Details part Match1111 after initializing strings");
			outputResponse =genX.GenerateXML("BLACKLIST_DETAILS","");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			mLogger.info("PL value of ReturnCode part Match"+ReturnCode);
			ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
			mLogger.info("PL value of ReturnDesc part Match"+ReturnDesc);

			if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode)){

				formObject.setNGValue("Is_Customer_Eligibility_Part","Y");   
				alert_msg="Blacklist check successfull";
				mLogger.info("PL value of BlacklistFlag_Part Customer is Blacklisted StatusType"+formObject.getNGValue("Is_Customer_Eligibility_Part"));
				StatusType= (outputResponse.contains("<StatusType>")) ? outputResponse.substring(outputResponse.indexOf("<StatusType>")+"</StatusType>".length()-1,outputResponse.indexOf("</StatusType>")):"";
				mLogger.info("PL value of BlacklistFlag_Part"+"Customer is Blacklisted StatusType"+StatusType);
				outputResponse = outputResponse.substring(outputResponse.indexOf("<CustomerBlackListResponse>")+27, outputResponse.indexOf("</CustomerBlackListResponse>"));
				System.out.println(outputResponse);
				outputResponse = "<?xml version=\"1.0\"?><Response>" + outputResponse;
				outputResponse = outputResponse+"</Response>";

				Document doc = PL_Integration_Output.getDocument(outputResponse);
				doc.getDocumentElement().normalize();

				System.out.println("Root element :"+doc.getDocumentElement().getNodeName());

				NodeList nList = doc.getElementsByTagName("StatusInfo");


				for (int temp = 0; temp < nList.getLength(); temp++) {

					Node nNode = nList.item(temp);


					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						StatusType = eElement.getElementsByTagName("StatusType").item(0).getTextContent() ;
						System.out.println("PL value of BlacklistFlag_PartCustomer is Blacklisted StatusType"+StatusType);
						if(StatusType.equalsIgnoreCase("Black List")){
							//	Blacklistflag= (outputResponse.contains("<StatusFlag>")) ? outputResponse.substring(outputResponse.indexOf("<StatusFlag>")+"</StatusFlag>".length()-1,outputResponse.indexOf("</StatusFlag>")):"";
							Blacklistflag = eElement.getElementsByTagName("StatusFlag").item(0).getTextContent() ;
							BlacklistReason = eElement.getElementsByTagName("StatusReason").item(0).getTextContent() ;
							BlacklistCode = eElement.getElementsByTagName("StatusCode").item(0).getTextContent() ;

							System.out.println("PL value of BlacklistFlag_Part Customer is Blacklisted StatusCode"+BlacklistCode);
						}
						if(StatusType.equalsIgnoreCase("Negative List")){
							Negatedflag = eElement.getElementsByTagName("StatusFlag").item(0).getTextContent() ;
							NegatedReason = eElement.getElementsByTagName("StatusReason").item(0).getTextContent() ;
							NegatedCode = eElement.getElementsByTagName("StatusCode").item(0).getTextContent() ;
						}
					}
					//added
					CustomerStatus =  (outputResponse.contains("<CustomerStatus>")) ? outputResponse.substring(outputResponse.indexOf("<CustomerStatus>")+"</CustomerStatus>".length()-1,outputResponse.indexOf("</CustomerStatus>")):"";
					CifID=formObject.getNGValue("PartMatch_CIFID");
					firstName=formObject.getNGValue("PartMatch_fname");
					LastName=formObject.getNGValue("PartMatch_lname");
					OldPassportNumber=formObject.getNGValue("PartMatch_oldpass");
					PassportNumber=formObject.getNGValue("PartMatch_newpass");
					Visa=formObject.getNGValue("PartMatch_visafno");
					EmirateID=formObject.getNGValue("PartMatch_EID");
					PhoneNo=formObject.getNGValue("PartMatch_mno1");
					mLogger.info("PL value of CustomerStatus"+"Customer is Blacklisted StatusType"+CustomerStatus);

				}
			}		
			else{
				formObject.setNGValue("Is_Customer_Eligibility_Part","N");    
				mLogger.info("PL value of BlacklistFlag_Part Customer is Blacklisted StatusType"+formObject.getNGValue("Is_Customer_Eligibility_Part"));
			}
			
			List<String> BlacklistGrid = new ArrayList<String>(); 
			BlacklistGrid.add(CifID);
			BlacklistGrid.add(CustomerStatus); 
			BlacklistGrid.add(firstName);
			BlacklistGrid.add(LastName);
			BlacklistGrid.add(Blacklistflag);
			BlacklistGrid.add(Blacklistnote);
			BlacklistGrid.add(BlacklistReason);
			BlacklistGrid.add(BlacklistCode);
			BlacklistGrid.add(Negatedflag);
			BlacklistGrid.add(Negatednote);
			BlacklistGrid.add(NegatedReason);
			BlacklistGrid.add(NegatedCode);
			BlacklistGrid.add(PassportNumber);
			BlacklistGrid.add(OldPassportNumber);
			BlacklistGrid.add(EmirateID);
			BlacklistGrid.add(Visa);
			BlacklistGrid.add(PhoneNo);
			mLogger.info("PL value of BlacklistFlag_Part"+"after adding in the grid");
			mLogger.info("RLOS Common# getOutputXMLValues()"+ "$$AKSHAY$$List to be added inFinacle CRM grid: "+ BlacklistGrid.toString());

			formObject.addItemFromList("cmplx_PartMatch_cmplx_PartBlacklistGrid",BlacklistGrid);
			mLogger.info("PL value of BlacklistFlag_Part"+"after adding in the grid11111");       
			formObject.RaiseEvent("WFSave");  

			throw new ValidatorException(new FacesMessage(alert_msg));

		}
		//Changes done by aman to correctly save the value in the grid


		else if ("PartMatch_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.fetchFragment("FinacleCRM_CustInfo", "FinacleCRMCustInfo", "q_FinCRMCustInfo");
			formObject.setNGValue("FinacleCRMCustInfo_WINAME",formObject.getWFWorkitemName());
			formObject.setTop("External_Blacklist",formObject.getTop("FinacleCRM_CustInfo")+formObject.getHeight("FinacleCRM_CustInfo")+20);
			formObject.setTop("Finacle_Core",formObject.getTop("External_Blacklist")+30);
			formObject.setTop("MOL",formObject.getTop("Finacle_Core")+30);
			formObject.setTop("World_Check",formObject.getTop("MOL")+30);
			formObject.setTop("Sal_Enq",formObject.getTop("World_Check")+30);
			formObject.setTop("Reject_Enq",formObject.getTop("Sal_Enq")+30);

			String BlacklistFlag_Part = "";
			String BlacklistFlag_reason = "";
			String BlacklistFlag_code = "";
			String NegativeListFlag = "";
			//String ReturnDesc = "";
			outputResponse =genX.GenerateXML("CUSTOMER_DETAILS","Primary_CIF");
			mLogger.info("PL value of ReturnDesc"+"Customer Details part Match");
			ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";

			BlacklistFlag_Part =  outputResponse.contains("<BlackListFlag>") ? outputResponse.substring(outputResponse.indexOf("<BlackListFlag>")+"</BlackListFlag>".length()-1,outputResponse.indexOf("</BlackListFlag>")):"NA";



			BlacklistFlag_reason =  outputResponse.contains("<BlackListReason>") ? outputResponse.substring(outputResponse.indexOf("<BlackListReason>")+"</BlackListReason>".length()-1,outputResponse.indexOf("</BlackListReason>")):"NA";


			BlacklistFlag_code =  outputResponse.contains("<BlackListReasonCode>") ? outputResponse.substring(outputResponse.indexOf("<BlackListReasonCode>")+"</BlackListReasonCode>".length()-1,outputResponse.indexOf("</BlackListReasonCode>")):"NA";

			NegativeListFlag =  outputResponse.contains("<NegativeListFlag>") ? outputResponse.substring(outputResponse.indexOf("<NegativeListFlag>")+"</NegativeListFlag>".length()-1,outputResponse.indexOf("</NegativeListFlag>")):"NA";
			mLogger.info("PL value of ReturnCode part Match"+ReturnCode);
			ReturnDesc =  outputResponse.contains("<ReturnDesc>") ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
			//mLogger.info("PL value of ReturnDesc part Match"+ReturnDesc);
			if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")){

				BlacklistFlag_Part =  outputResponse.contains("<BlacklistFlag>") ? outputResponse.substring(outputResponse.indexOf("<BlacklistFlag>")+"</BlacklistFlag>".length()-1,outputResponse.indexOf("</BlacklistFlag>")):"";                                  
				formObject.setNGValue("Is_Customer_Details_Part","Y");    
				formObject.setNGValue("FinacleCRMCustInfo_WINAME",formObject.getWFWorkitemName());
				if("Y".equalsIgnoreCase(BlacklistFlag_Part))
				{
					mLogger.info("PL value of BlacklistFlag_Part"+"Customer is Blacklisted");    
				}
				else
					mLogger.info("PL value of BlacklistFlag_Part"+"Customer is not Blacklisted");    
			}
			else{
				formObject.setNGValue("Is_Customer_Details_Part","N");
			}
			try{
				mLogger.info("CC value of BlacklistFlag_Part flag inside try"+BlacklistFlag_Part+"");    
				List<String> list_custinfo = new ArrayList<String>();
				String CIFID =formObject.getNGValue("cmplx_PartMatch_cmplx_Partmatch_grid",formObject.getSelectedIndex("cmplx_PartMatch_cmplx_Partmatch_grid"),0);
				String PASSPORTNO =formObject.getNGValue("cmplx_PartMatch_cmplx_Partmatch_grid",formObject.getSelectedIndex("cmplx_PartMatch_cmplx_Partmatch_grid"),5);

				list_custinfo.add(CIFID);  // cif id from partmatch
				list_custinfo.add("Individual");
				list_custinfo.add(PASSPORTNO); // passport
				list_custinfo.add(NegativeListFlag);
				list_custinfo.add("");
				list_custinfo.add("");
				list_custinfo.add("");
				list_custinfo.add(BlacklistFlag_Part); // blacklist flag
				list_custinfo.add("");
				list_custinfo.add(BlacklistFlag_code);
				list_custinfo.add(BlacklistFlag_reason);
				list_custinfo.add("");
				list_custinfo.add("");
				list_custinfo.add("");
				list_custinfo.add(formObject.getWFWorkitemName());

				mLogger.info("CC DDVT Maker"+ "list_custinfo list values"+list_custinfo);
				formObject.addItemFromList("cmplx_FinacleCRMCustInfo_FincustGrid", list_custinfo);
			}catch(Exception e){
				mLogger.info("PL DDVT Maker"+ "Exception while setting data in grid:"+e.getMessage());
				PLCommon.printException(e);			
				alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val034");//"Error while setting data in finacle customer info grid";
				throw new ValidatorException(new FacesMessage(alert_msg));
			}

			formObject.RaiseEvent("WFSave");          
		}
		//changes done as said by Deepak Sir To call Customer_Details call ended


		else if("GuarantorDetails_Button2".equalsIgnoreCase(pEvent.getSource().getName())){

			outputResponse = genX.GenerateXML("CUSTOMER_DETAILS","Guarantor_CIF");
			ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			mLogger.info("PL value of ReturnCode"+ReturnCode);
			ReturnDesc =  outputResponse.contains("<ReturnDesc>") ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
			mLogger.info("PL value of ReturnDesc"+ReturnDesc);
			if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode)){
				formObject.setNGValue("Is_Customer_Details","Y");
				mLogger.info("PL value of EID_Genuine"+"value of Guarantor_CIF"+formObject.getNGValue("Is_Customer_Details"));
				PL_Integration_Output.valueSetIntegration(outputResponse);    
				mLogger.info("PL value of Customer Details"+"Guarantor_CIF is generated");
				mLogger.info("PL value of Customer Details"+formObject.getNGValue("Is_Customer_Details"));
			}
			else{
				mLogger.info("Customer Details"+"Customer Details is not generated");
				formObject.setNGValue("Is_Customer_Details","N");
			}
			mLogger.info("PL value of Guarantor_CIF"+formObject.getNGValue("Is_Customer_Details"));
			formObject.RaiseEvent("WFSave");
		}

		if ("EMploymentDetails_Button2".equalsIgnoreCase(pEvent.getSource().getName())) {
			String EmpName=formObject.getNGValue("EMploymentDetails_Text21");
			String EmpCode=formObject.getNGValue("EMploymentDetails_Text22");
			mLogger.info( "EMpName$"+EmpName+"$");
			String query=null;
			//changes done to check duplicate selection compare emp code and main emp code main_employer_code column added - 06-09-2017 Disha
			if("".equalsIgnoreCase(EmpName.trim()))
				query="select distinct(EMPR_NAME),EMPLOYER_CODE,NATURE_OF_BUSINESS,EMPLOYER_CATEGORY_PL_NATIONAL,EMPLOYER_CATEGORY_CARDS,EMPLOYER_CATEGORY_PL_EXPAT,INCLUDED_IN_PL_ALOC,DOI_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,DATE_OF_INCLUSION_IN_CC_ALOC,NAME_OF_AUTHORIZED_PERSON_FOR_ISSUING_SC_STL_PAYSLIP,ACCOMMODATION_PROVIDED,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,OWNER_PARTNER_SIGNATORY_NAMES_AS_PER_TL,ALOC_REMARKS,HIGH_DELINQUENCY_EMPLOYER,MAIN_EMPLOYER_CODE from NG_RLOS_ALOC_OFFLINE_DATA where EMPLOYER_CODE Like '%"+EmpCode+"%'";

			else
				query="select distinct(EMPR_NAME),EMPLOYER_CODE,NATURE_OF_BUSINESS,EMPLOYER_CATEGORY_PL_NATIONAL,EMPLOYER_CATEGORY_CARDS,EMPLOYER_CATEGORY_PL_EXPAT,INCLUDED_IN_PL_ALOC,DOI_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,DATE_OF_INCLUSION_IN_CC_ALOC,NAME_OF_AUTHORIZED_PERSON_FOR_ISSUING_SC_STL_PAYSLIP,ACCOMMODATION_PROVIDED,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,OWNER_PARTNER_SIGNATORY_NAMES_AS_PER_TL,ALOC_REMARKS,HIGH_DELINQUENCY_EMPLOYER,MAIN_EMPLOYER_CODE from NG_RLOS_ALOC_OFFLINE_DATA where EMPR_NAME Like '%"+EmpName + "%' or EMPLOYER_CODE Like '%"+EmpCode+"'";

			mLogger.info( "query is: "+query);
			populatePickListWindow(query,"EMploymentDetails_Button2", "Employer Name,Employer Code,Nature Of Business,EMPLOYER CATEGORY PL NATIONAL,EMPLOYER CATEGORY CARDS,EMPLOYER CATEGORY PL EXPAT,INCLUDED IN PL ALOC,DOI IN PL ALOC,INCLUDED IN CC ALOC,DATE OF INCLUSION IN CC ALOC,NAME OF AUTHORIZED PERSON FOR ISSUING SC/STL/PAYSLIP,ACCOMMODATION PROVIDED,INDUSTRY SECTOR,INDUSTRY MACRO,INDUSTRY MICRO,CONSTITUTION,NAME OF FREEZONE AUTHORITY,OWNER/PARTNER/SIGNATORY NAMES AS PER TL,ALOC REMARKS,HIGH DELINQUENCY EMPLOYER,MAIN EMPLOYER CODE", true, 20);

		}


		else if("LoanDetails_Button3".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setNGValue("LoanDetails_winame",formObject.getWFWorkitemName());
			mLogger.info( "Inside add button: "+formObject.getNGValue("LoanDetails_winame"));                                                                                				
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_LoanDetails_cmplx_LoanGrid");
		}                                
		else if("LoanDetails_Button4".equalsIgnoreCase(pEvent.getSource().getName())){

			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_LoanDetails_cmplx_LoanGrid");
		}
		else if(pEvent.getSource().getName().equalsIgnoreCase("LoanDetails_Button5")){

			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_LoanDetails_cmplx_LoanGrid");
		}

		if ("FATCA_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			String text=formObject.getNGItemText("cmplx_FATCA_listedreason", formObject.getSelectedIndex("cmplx_FATCA_listedreason"));
			mLogger.info("RLOS"+ "Inside FATCA_Button1 "+text);
			formObject.addItem("cmplx_FATCA_selectedreason", text);
			try {
				formObject.removeItem("cmplx_FATCA_listedreason", formObject.getSelectedIndex("cmplx_FATCA_listedreason"));
				formObject.setSelectedIndex("cmplx_FATCA_listedreason", -1);

			}catch (Exception e) {
				// TODO Auto-generated catch block
				printException(e);
			}

		}


		if ("FATCA_Button2".equalsIgnoreCase(pEvent.getSource().getName())){
			mLogger.info("RLOS"+ "Inside FATCA_Button2 ");
			formObject.addItem("cmplx_FATCA_listedreason", formObject.getNGItemText("cmplx_FATCA_selectedreason", formObject.getSelectedIndex("cmplx_FATCA_selectedreason")));
			try {
				formObject.removeItem("cmplx_FATCA_selectedreason", formObject.getSelectedIndex("cmplx_FATCA_selectedreason"));
				formObject.setSelectedIndex("cmplx_FATCA_selectedreason", -1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				printException(e);
			}
		}


		else if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_addr_Add")){
			formObject.setNGValue("Address_wi_name",formObject.getWFWorkitemName());
			mLogger.info( "Inside add button: "+formObject.getNGValue("Address_wi_name"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_AddressDetails_cmplx_AddressGrid");
		}

		else if ("AddressDetails_addr_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_AddressDetails_cmplx_AddressGrid");
		}

		else if ("AddressDetails_addr_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_AddressDetails_cmplx_AddressGrid");

		}


		if("Customer_save".equalsIgnoreCase(pEvent.getSource().getName())){
			mLogger.info( "Inside Customer_save button: ");
			formObject.saveFragment("CustomerDetails");
			alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val017");//"Customer Details Saved";
			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		if("Product_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("ProductContainer");
			alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val018");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		else if("GuarantorDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("GuarantorDetails");
			throw new ValidatorException(new FacesMessage("Guarantor Save Successful"));


		}

		else if("IncomeDetails_Salaried_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("IncomeDEtails");

		}

		else if("IncomeDetails_SelfEmployed_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Incomedetails");
		}

		else if("CompanyDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("CompanyDetails");
		}

		else if("PartnerDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("PartnerDetails");
		}

		else if("SelfEmployed_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Liability_container");
		}

		if("Liability_New_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("InternalExternalContainer");
			alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val019");//"Liability Details Saved";
			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		if("EmpDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("EmploymentDetails");
			alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val020");//"Employment Details Saved";
			throw new ValidatorException(new FacesMessage(alert_msg));
		}



		if("ELigibiltyAndProductInfo_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("EligibilityAndProductInformation");
			alert_msg="Eligibility Details Saved";
			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		if("LoanDetailsRepay_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("LoanDetails");
		}

		if("AddressDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Address_Details_container");
		}
		//disha FSD
		if("AltContactDetails_ContactDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Alt_Contact_container");
		}

		if("ReferenceDetails_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("ReferenceDetails");
		}

		if("CardDetails_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Supplementary_Container");
		}


		else if("LoanDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("LoanDetails");
			alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val022");//"Loan Details Saved";

			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		else if("LoanDetaisDisburs_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("LoanDetails");
			alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val035");//"Disbursal Details Saved";

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		if("FATCA_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("FATCA");
			alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val023");//"FATCA Details Saved";
			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		if("KYC_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("KYC");
			alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val024");//"KYC Details Saved";
			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		if("OECD_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("OECD");
			alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val016");//"OECD Details Saved";
			throw new ValidatorException(new FacesMessage(alert_msg));
		}								

		else if("PartMatch_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Part_Match");
		}

		else if("FinacleCRMIncident_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("FinacleCRM_Incidents");
		}

		else if("FinacleCRMCustInfo_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("FinacleCRM_CustInfo");
		}                                                                                              

		else if("FinacleCore_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Finacle_Core");
		}

		else if("MOL1_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("MOL");
		}

		else if("WorldCheck1_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("World_Check");
		}

		else if("SalaryEnq_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Sal_Enq");
		}

		else if("CreditCardEnq_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Credit_card_Enq");
		}

		else if("CaseHistoryReport_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Case_History");
		}

		else if("LOS_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("LOS");
		}

		else if("RejectEnq_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Reject_Enq");
		}                                                                                              

		else if("DecisionHistory_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			//formObject.saveFragment("DecisionHistoryContainer");
			saveIndecisionGrid();//Arun (23/09/17)
			formObject.saveFragment("DecisionHistory");
			alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val002");//"Decision History Details Saved";
			throw new ValidatorException(new FacesMessage(alert_msg));//Arun (23/09/17)
		}

		else  if("NotepadDetails_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Notepad_Values");
			alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val003");//"Notepad Details Saved";//Arun (23/09/17)
			throw new ValidatorException(new FacesMessage(alert_msg));//Arun (23/09/17)
		}


		else if("CAD_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Notepad_Details");
		}

		else if("CAD_Decision_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Dec");
		}


		else if("Loan_Disbursal_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Loan_Disbursal");
		}

		else if("CC_Creation_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("CC_Creation");
		}

		else if("Limit_Inc_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Limit_Inc");
		}

		if("CustDetailVerification_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Cust_Detail_verification");
			alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val007");//"Customer detail verification saved";
			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		if("BussinessVerification_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Business_verification");
			alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val008");//"Business detail verification saved";
			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		if("HomeCountryVerification_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Home_country_verification");
			alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val009");//"Home country verification saved";
			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		if("ResidenceVerification_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Residence_verification");
			alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val010");//"Residence verification saved";
			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		if("GuarantorVerification_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Guarantor_verification");
			alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val011");//"Guarantor verification   saved";
			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		if("ReferenceDetailVerification_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Reference_detail_verification");
			alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val012");//"Reference detail verification saved";
			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		if("OfficeandMobileVerification_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Office_verification");
			alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val013");//"Office verification details saved";
			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		if("LoanandCard_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Loan_card_details");
			alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val014");//"Loan and Card details saved";
			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		if("Compliance_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Compliance");
		}

		if("WorldCheck1_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("World_Check");
		}

		if("supervisorsection_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Supervisor_section");
		}

		else if("NotepadDetails_Button4".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_notegrid");
		}

		else if("NotepadDetails_Button5".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_NotepadDetails_cmplx_notegrid");
		}

		else if("NotepadDetails_Button6".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_NotepadDetails_cmplx_notegrid");
		}

		//++ below code changed by abhishek on 25th oct 2017 to change button id
		else if(pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_Add"))
		{
			formObject.setNGValue("WorldCheck1_winame",formObject.getWFWorkitemName());
			formObject.setNGValue("Is_WorldCheckAdd","Y");
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_WorldCheck_WorldCheck_Grid");
		}
		//-- Above code changed by abhishek on 25th oct 2017 to change button id


		else if("WorldCheck1_Button2".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_WorldCheck_WorldCheck_Grid");
		}

		else if("WorldCheck1_Button3".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setNGValue("Is_World_Check_Add","N");
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_WorldCheck_WorldCheck_Grid");
		}

		else if("cmplx_Decision_VERIFICATIONREQUIRED".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			mLogger.info("CC val cmplx_Decision_VERIFICATIONREQUIRED "+ "Value of cmplx_Decision_VERIFICATIONREQUIRED is:"+formObject.getNGValue("cmplx_Decision_VERIFICATIONREQUIRED"));
			if(formObject.getNGValue("cmplx_Decision_VERIFICATIONREQUIRED").equalsIgnoreCase("Yes") )
			{
				mLogger.info("CC val change "+ "Inside Y of CPV required");
				formObject.setNGValue("cpv_required","Y");
			}
			else
			{
				mLogger.info("CC val change "+ "Inside N of CPV required");
				formObject.setNGValue("cpv_required","N");
			}
		}
		// disha FSD
		else if ("NotepadDetails_Add".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setNGValue("Notepad_wi_name",formObject.getWFWorkitemName());
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_notegrid");

			String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
			mLogger.info("PL notepad "+ "Activity name is:" + sActivityName);
			int user_id = formObject.getUserId();
			String user_name = formObject.getUserName();
			user_name = user_name+"-"+user_id;					
			formObject.setNGValue("NotepadDetails_insqueue",sActivityName,false);
			formObject.setNGValue("NotepadDetails_Actusername",user_name,false); 
			formObject.setNGValue("NotepadDetails_user",user_name,false);

			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date date = new Date();
			formObject.setNGValue("NotepadDetails_noteDate",dateFormat.format(date),false); 
			formObject.setNGValue("NotepadDetails_Actdate",dateFormat.format(date),false); 

		}
		else if ("NotepadDetails_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_NotepadDetails_cmplx_notegrid");
		}
		else if ("NotepadDetails_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_NotepadDetails_cmplx_notegrid");

		}
		else  if ("FinacleCRMCustInfo_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			//genX.GenerateXML();
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_FinacleCRMCustInfo_FincustGrid");
		}

		else if("DecisionHistory_Button3".equalsIgnoreCase(pEvent.getSource().getName())){
			String message = CustomerUpdate();
			throw new ValidatorException(new FacesMessage(message));
		}

		//tanshu started
		else if("DecisionHistory_updcust".equalsIgnoreCase(pEvent.getSource().getName())){
			try{
				mLogger.info("inside ENTITY_MAINTENANCE_REQ is generated");
				String acc_veri= (formObject.getNGValue("Is_Acc_verified")!=null) ?formObject.getNGValue("Is_Acc_verified"):"";
				mLogger.info("PL checker Account Update call"+ "entity_flag : "+acc_veri);

				if(acc_veri == null || acc_veri.equalsIgnoreCase("")||acc_veri.equalsIgnoreCase("N")){
					outputResponse = genX.GenerateXML("ENTITY_MAINTENANCE_REQ","AcctVerification");
					ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					mLogger.info("PL DDVT checker value of ReturnCode AcctVerification: "+ReturnCode);
					if(ReturnCode.equalsIgnoreCase("0000")){
						formObject.setNGValue("Is_Acc_verified","Y");
						acc_veri="Y";
						mLogger.info("account Verified successfully");
					}
					else{
						mLogger.info("account Verified failed ReturnCode: "+ReturnCode );
						formObject.setNGValue("Is_Acc_verified","N");
						alert_msg= NGFUserResourceMgr_PL.getResourceString_plValidations("val025");//"Account Verification operation Failed, Please try after some time or contact administrator";
						throw new ValidatorException(new FacesMessage(alert_msg));
					}
				}

				String acc_acti= formObject.getNGValue("Is_Acc_Active")!=null ? formObject.getNGValue("Is_Acc_Active"):"";
				if(acc_veri.equalsIgnoreCase("Y")&&(acc_acti.equalsIgnoreCase("")||acc_acti.equalsIgnoreCase("N"))){
					outputResponse = genX.GenerateXML("ENTITY_MAINTENANCE_REQ","AcctActivation");
					ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					mLogger.info("PL DDVT checker value of ReturnCode for AcctActivation: "+ReturnCode);
					if(ReturnCode.equalsIgnoreCase("0000")){
						formObject.setNGValue("Is_Acc_Active","Y");
						acc_acti="Y";
						mLogger.info("account Activation successfully");
					}
					else{
						mLogger.info("ENTITY_MAINTENANCE_REQ is not generated ReturnCode: "+ReturnCode );
						formObject.setNGValue("Is_Acc_Active","N");
						alert_msg= NGFUserResourceMgr_PL.getResourceString_plValidations("val026");//"Account Activation operation Failed, Please try after some time or contact administrator";
						throw new ValidatorException(new FacesMessage(alert_msg));
					}

				}

				if(acc_veri.equalsIgnoreCase("Y")&&acc_acti.equalsIgnoreCase("Y")) {
					outputResponse = genX.GenerateXML("ACCOUNT_MAINTENANCE_REQ","");
					ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					mLogger.info("PL DDVT checker value of ReturnCode"+ReturnCode);
					if(ReturnCode.equalsIgnoreCase("0000") ){
						formObject.setNGValue("Is_ACCOUNT_MAINTENANCE_REQ","Y");
						mLogger.info("value of ACCOUNT_MAINTENANCE_REQ"+formObject.getNGValue("Is_ACCOUNT_MAINTENANCE_REQ"));
						PL_Integration_Output.valueSetIntegration(outputResponse);    
						mLogger.info("ACCOUNT_MAINTENANCE_REQ is generated");
						mLogger.info("PL DDVT checker value of Customer Details"+formObject.getNGValue("Is_ACCOUNT_MAINTENANCE_REQ"));
						formObject.RaiseEvent("WFSave");
						alert_msg= NGFUserResourceMgr_PL.getResourceString_plValidations("val027");//"Account Updated Successfully !";
						throw new ValidatorException(new FacesMessage(alert_msg));
					}
					else{
						mLogger.info("ACCOUNT_MAINTENANCE_REQ is not generated ReturnCode: "+ReturnCode );
						formObject.setNGValue("Is_ACCOUNT_MAINTENANCE_REQ","N");
						alert_msg= NGFUserResourceMgr_PL.getResourceString_plValidations("val028");//"Account Update operation Failed, Please try after some time or contact administrator";
						throw new ValidatorException(new FacesMessage(alert_msg));
					}
				}										
			}
			catch(Exception ex){
				mLogger.info("Exception in update account: ");
				printException(ex);
			}
		}

		if("DecisionHistory_chqbook".equalsIgnoreCase(pEvent.getSource().getName())){
			mLogger.info("RLOS value of CheckBook");
			outputResponse = genX.GenerateXML("CHEQUE_BOOK_ELIGIBILITY","");
			ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			mLogger.info("RLOS value of ReturnCode"+ReturnCode);
			ReturnDesc =  outputResponse.contains("<ReturnDesc>") ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
			mLogger.info("RLOS value of ReturnDesc"+ReturnDesc);
			String ReferenceId=outputResponse.contains("<ReferenceId>") ? outputResponse.substring(outputResponse.indexOf("<ReferenceId>")+"</ReferenceId>".length()-1,outputResponse.indexOf("</ReferenceId>")):"";    
			mLogger.info("RLOS value of ReferenceId"+ReferenceId);
			//Modified condition Tanshu Aggarwal(7-06-2017)
			if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")){
				//Modified condition Tanshu Aggarwal(7-06-2017)
				formObject.setNGValue("Is_CHEQUE_BOOK_ELIGIBILITY","Y");
				mLogger.info("value of ENTITY_MAINTENANCE_REQ"+formObject.getNGValue("Is_CHEQUE_BOOK_ELIGIBILITY"));
				PL_Integration_Output.valueSetIntegration(outputResponse);    
				mLogger.info("Is_CHEQUE_BOOK_ELIGIBILITY is generated");
				mLogger.info("RLOS value of Is_CHEQUE_BOOK_ELIGIBILITY flag value"+formObject.getNGValue("Is_CHEQUE_BOOK_ELIGIBILITY"));
				formObject.setNGValue("cmplx_Decision_ChequeBookNumber",ReferenceId);
				mLogger.info("ReferenceId"+formObject.getNGValue("cmplx_Decision_ChequeBookNumber"));
				formObject.RaiseEvent("WFSave");
			}
			else{
				mLogger.info("Is_CHEQUE_BOOK_ELIGIBILITY is not generated");
				formObject.setNGValue("Is_CHEQUE_BOOK_ELIGIBILITY","N");
			}
			outputResponse = genX.GenerateXML("NEW_CARD_REQ","");
			ReturnCode =  outputResponse.contains("<ReturnCode>")? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			mLogger.info("RLOS value of ReturnCode"+ReturnCode);
			ReturnDesc =  outputResponse.contains("<ReturnDesc>") ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
			mLogger.info("RLOS value of ReturnDesc"+ReturnDesc);
			String RequestId=outputResponse.contains("<RequestId>") ? outputResponse.substring(outputResponse.indexOf("<RequestId>")+"</RequestId>".length()-1,outputResponse.indexOf("</RequestId>")):"";    
			mLogger.info("RLOS value of RequestId"+RequestId);
			//Modified condition Tanshu Aggarwal(7-06-2017)
			if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")){
				//Modified condition Tanshu Aggarwal(7-06-2017)
				formObject.setNGValue("Is_NEW_CARD_REQ","Y");
				mLogger.info("value of NEW_CARD_REQ"+formObject.getNGValue("Is_NEW_CARD_REQ"));
				PL_Integration_Output.valueSetIntegration(outputResponse);    
				mLogger.info("Is_NEW_CARD_REQ is generated");
				mLogger.info("RLOS value of Is_CHEQUE_BOOK_ELIGIBILITY flag value"+formObject.getNGValue("Is_NEW_CARD_REQ"));
				formObject.setNGValue("cmplx_Decision_DebitcardNumber",RequestId);
				mLogger.info("NEW_CARD_REQ"+formObject.getNGValue("cmplx_Decision_DebitcardNumber"));
				formObject.RaiseEvent("WFSave");
			}
			else{
				mLogger.info("Is_NEW_CARD_REQ is not generated");
				formObject.setNGValue("Is_NEW_CARD_REQ","N");
			}

			formObject.RaiseEvent("WFSave");
			mLogger.info("RLOS value of Is_CHEQUE_BOOK_ELIGIBILITY flag123 value"+formObject.getNGValue("Is_CHEQUE_BOOK_ELIGIBILITY"));
			mLogger.info("RLOS value of Is_NEW_CARD_REQ flag123 value"+formObject.getNGValue("Is_NEW_CARD_REQ"));
			String ChequeBook=formObject.getNGValue("Is_CHEQUE_BOOK_ELIGIBILITY");
			String NewCardReq=formObject.getNGValue("Is_NEW_CARD_REQ");
			if((ChequeBook.equalsIgnoreCase("Y")) && (NewCardReq.equalsIgnoreCase("Y"))){
				mLogger.info("RLOS value of Is_CHEQUE_BOOK_ELIGIBILITY flag123 value"+formObject.getNGValue("Is_CHEQUE_BOOK_ELIGIBILITY")+","+formObject.getNGValue("Is_NEW_CARD_REQ"));

				throw new ValidatorException(new CustomExceptionHandler("Debit Card and Cheque Book created successfully!!","DecisionHistory_chqbook#Debit Card and Cheque Book created successfully","",hm));
			}
			else if((ChequeBook.equalsIgnoreCase("N")) && (NewCardReq.equalsIgnoreCase("Y"))){
				mLogger.info("RLOS value of Is_NEW_CARD_REQ flag123 value"+formObject.getNGValue("Is_CHEQUE_BOOK_ELIGIBILITY")+","+formObject.getNGValue("Is_NEW_CARD_REQ"));
				throw new ValidatorException(new CustomExceptionHandler("Debit Card created sucessfully, But Cheque Book created Failed.!!","DecisionHistory_chqbook#Debit Card created sucessfully, But Cheque Book created Failed.","",hm));
			}
			else if((ChequeBook.equalsIgnoreCase("Y")) && (NewCardReq.equalsIgnoreCase("N"))){
				mLogger.info("RLOS value of Is_NEW_CARD_REQ flag123 value"+formObject.getNGValue("Is_CHEQUE_BOOK_ELIGIBILITY")+","+formObject.getNGValue("Is_NEW_CARD_REQ"));
				throw new ValidatorException(new CustomExceptionHandler("Cheque Book created sucessfully, But Debit Card created Failed.!!","DecisionHistory_chqbook#Cheque Book created sucessfully, But Debit Card created Failed.","",hm));
			}
			else{
				mLogger.info("RLOS value of Is_NEW_CARD_REQ flag123 value"+formObject.getNGValue("Is_CHEQUE_BOOK_ELIGIBILITY")+","+formObject.getNGValue("Is_NEW_CARD_REQ"));
				throw new ValidatorException(new CustomExceptionHandler("Cheque Book created and Debit Card created Failed, Please try after time or contact administrator!!","DecisionHistory_chqbook#Cheque Book created and Debit Card created Failed, Please try after time or contact administrator.","",hm));
			}
		}
		//ended merged code


		//started code merged
		else  if("CC_Creation_Update_Customer".equalsIgnoreCase(pEvent.getSource().getName())){
			mLogger.info(""+"inside Update_Customer");  
			outputResponse = genX.GenerateXML("CUSTOMER_DETAILS","Inquire");
			ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			mLogger.info("RLOS value of ReturnCode"+ReturnCode);

			if(ReturnCode.equalsIgnoreCase("0000")){
				mLogger.info("RLOS value of Customer_Details"+"value of Customer_Details inside inquiry code");
				formObject.setNGValue("Is_CustInquiry_Disbursal","Y"); 
				mLogger.info(""+"Inquiry Flag Value"+formObject.getNGValue("Is_CustInquiry_Disbursal")); 
				mLogger.info(""+"inside Update_Customer");  
				String cif_status = outputResponse.contains("<CustomerStatus>") ? outputResponse.substring(outputResponse.indexOf("<CustomerStatus>")+"</CustomerStatus>".length()-1,outputResponse.indexOf("</CustomerStatus>")):"";
				if(cif_status.equalsIgnoreCase("ACTVE")){
					outputResponse = genX.GenerateXML("CUSTOMER_DETAILS","Lock");
					ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					mLogger.info("RLOS value of ReturnCode"+ReturnCode);
					ReturnDesc =  outputResponse.contains("<ReturnDesc>") ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
					mLogger.info("RLOS value of ReturnDesc"+ReturnDesc);
					if(ReturnCode.equalsIgnoreCase("0000")|| ReturnCode.equalsIgnoreCase("000")){
						mLogger.info("RLOS value of Customer_Details"+"value of Customer_Details inside lock code");
						formObject.setNGValue("Is_CustLock_Disbursal","Y");
						mLogger.info(""+"Inquiry Flag Is_CustLock_Disbursal value"+formObject.getNGValue("Is_CustLock_Disbursal")); 

						mLogger.info("RLOS value of Customer_Details"+"Customer_Details is generated");
						//    SKLogger.writeLog("RLOS value of Customer Details",formObject.getNGValue("Is_ACCOUNT_MAINTENANCE_REQ"));
						outputResponse = genX.GenerateXML("CUSTOMER_DETAILS","UnLock");
						ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
						mLogger.info("RLOS value of ReturnCode"+"inside unlock");
						mLogger.info("RLOS value of ReturnCode"+ReturnCode);
						formObject.setNGValue("Is_CustUnLock_Disbursal","Y");
						mLogger.info(""+"Inquiry Flag Is_CustUnLock_Disbursal value"+formObject.getNGValue("Is_CustUnLock_Disbursal"));
						ReturnDesc =  outputResponse.contains("<ReturnDesc>") ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
						mLogger.info("RLOS value of ReturnDesc"+ReturnDesc);
						if(ReturnCode.equalsIgnoreCase("0000")|| ReturnCode.equalsIgnoreCase("000")){
							outputResponse = genX.GenerateXML("CUSTOMER_UPDATE_REQ","");
							ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
							mLogger.info("RLOS value of ReturnCode"+ReturnCode);
							ReturnDesc =  outputResponse.contains("<ReturnDesc>") ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
							mLogger.info("RLOS value of ReturnDesc"+ReturnDesc);
							//  Message =outputResponse.contains("<Message>")) ? outputResponse.substring(outputResponse.indexOf("<Message>")+"</Message>".length()-1,outputResponse.indexOf("</Message>")):"";    
							//  SKLogger.writeLog("RLOS value of Message",Message);

							if(ReturnCode.equalsIgnoreCase("0000")|| ReturnCode.equalsIgnoreCase("000")){
								formObject.setNGValue("Is_CUSTOMER_UPDATE_REQ_Disbursal","Y");
								mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+"value of CUSTOMER_UPDATE_REQ"+formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
								PL_Integration_Output.valueSetIntegration(outputResponse);    
								mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+"CUSTOMER_UPDATE_REQ is generated");
								mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ_Disbursal"));
							}
							else{
								mLogger.info("Customer Details"+"CUSTOMER_UPDATE_REQ is not generated");
								formObject.setNGValue("Is_CUSTOMER_UPDATE_REQ_Disbursal","N");
							}
							mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ_Disbursal"));
							formObject.RaiseEvent("WFSave");
							mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+"after saving the flag");
							if(formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ_Disbursal").equalsIgnoreCase("Y"))
							{ 
								mLogger.info("RLOS value of Is_CUSTOMER_UPDATE_REQ"+"inside if condition");
								formObject.setEnabled("Update_Customer", false); 
								throw new ValidatorException(new CustomExceptionHandler("Customer Updated Successful!!","Update_Customer#Customer Updated Successful!!","",hm));
							}
							else{
								formObject.setEnabled("CC_Creation_Update_Customer", true);
								throw new ValidatorException(new CustomExceptionHandler("Customer Updated Fail!!","Update_Customer#Customer Updated Fail!!","",hm));
							}
						}
						else{
							mLogger.info("Customer Details"+"Customer_Details unlock is not generated");

						}
					}
					else{
						mLogger.info("Customer Details"+"Customer_Details lock is not generated");
					}
				}
				else {
					mLogger.info("DDVT Checker Customer Update operation: "+ "Error in Cif Enquiry operation CIF Staus is: "+ cif_status);
					throw new ValidatorException(new  CustomExceptionHandler("Customer Is InActive!!","FetchDetails#Customer Is InActive!!","",hm));
				}
				//added one more here
			}
			else{
				mLogger.info("Customer Details"+"Customer_Details Inquiry is not generated");
			}
		}

		else if("CC_Creation_Card_Services".equalsIgnoreCase(pEvent.getSource().getName())){

			mLogger.info("RLOS value of ReturnCode"+"inside button click");
			String Product_Name=formObject.getNGValue("cmplx_CCCreation_pdt");
			mLogger.info("RLOS value of Product_Name"+""+Product_Name);
			if(Product_Name.equalsIgnoreCase("Limit Change Request")){
				outputResponse = genX.GenerateXML("CARD_SERVICES_REQUEST","Limit Change Request");
				ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				mLogger.info("RLOS value of ReturnCode"+ReturnCode);

				if(ReturnCode.equalsIgnoreCase("0000")){
					mLogger.info("RLOS value of Customer_Details"+"value of Customer_Details inside inquiry code");
					formObject.setNGValue("Is_CardServicesPL","Y");
					mLogger.info("RLOS value of Customer_Details for limit change"+formObject.getNGValue("Is_CardServicesPL"));
				}
			}
			else if(Product_Name.equalsIgnoreCase("Financial Services Request")){
				outputResponse = genX.GenerateXML("CARD_SERVICES_REQUEST","Financial Services Request");
				ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				mLogger.info("RLOS value of ReturnCode"+ReturnCode);
				if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")){
					mLogger.info("RLOS value of Customer_Details"+"value of Customer_Details inside inquiry code");
					formObject.setNGValue("Is_CardServicesPL","Y"); 
					mLogger.info("RLOS value of Customer_Details for financial product"+formObject.getNGValue("Is_CardServicesPL"));
				}
			}
			else if(Product_Name.equalsIgnoreCase("Product Upgrade Request")){
				outputResponse = genX.GenerateXML("CARD_SERVICES_REQUEST","Product Upgrade Request");
				ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				mLogger.info("RLOS value of ReturnCode"+ReturnCode);
				if(ReturnCode.equalsIgnoreCase("0000")){
					mLogger.info("RLOS value of Customer_Details"+"value of Customer_Details inside inquiry code");
					formObject.setNGValue("Is_CardServicesPL","Y"); 
					mLogger.info("RLOS value of Customer_Details for product upgrade"+formObject.getNGValue("Is_CardServicesPL"));
				}
			}
		}
		//end of entity main and account mainitainence call

		//code for New Card and Notification and New Loan Request call Tanshu Aggarwal
		else if("CC_Creation_Button2".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.RaiseEvent("WFSave");
			mLogger.info("RLOS value of ReturnCode"+"inside button click");
			String Product_Name=formObject.getNGValue("cmplx_CCCreation_pdt");
			mLogger.info("RLOS value of Product_Name"+""+Product_Name);
			if(Product_Name.equalsIgnoreCase("Limit Change Request")){
				outputResponse = genX.GenerateXML("CARD_SERVICES_REQUEST","Limit Change Request");
				ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				mLogger.info("RLOS value of ReturnCode"+ReturnCode);
				if(ReturnCode.equalsIgnoreCase("0000")){
					mLogger.info("RLOS value of Customer_Details"+"value of Customer_Details inside inquiry code");
					formObject.setNGValue("Is_CardServicesPL","Y");
					mLogger.info("RLOS value of ReturnCode Is_CardServices"+""+formObject.getNGValue("Is_CardServicesPL"));
					mLogger.info(""+"CARD SERVICES RUNNING Limit Change Request");
					outputResponse = genX.GenerateXML("CARD_NOTIFICATION","");
					ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					mLogger.info("RLOS value of ReturnCode"+ReturnCode);
					if(ReturnCode.equalsIgnoreCase("0000")){
						mLogger.info("RLOS value of Customer_Details"+"value of Customer_Details inside lock code");
						formObject.setNGValue("Is_CardNotifiactionPL","Y");
						mLogger.info("RLOS value of ReturnCode Is_CardNotifiactionPL"+""+formObject.getNGValue("Is_CardNotifiactionPL"));
					}


					formObject.RaiseEvent("WFSave");
				} 
			}
		}
		//ended    for New Card and Notification call    

		//loan call
		else if("Loan_Disbursal_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			List<List<String>> output = null;
			String scheme="";
			String acc_brnch="";
			//added by abhishek started
			mLogger.info("PL Disbursal: " +"inside Loan Creation");
			formObject.fetchFragment("ProductContainer", "Product", "q_Product");
			int n = formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			mLogger.info("PL Disbursal: "+ "Row count: "+n);
			for(int i=0;i<n;i++)
			{
				mLogger.info("value of requested type is"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid"+ 0+ 1) +"");
				formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 1);
				if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 1).equalsIgnoreCase("PL") || formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 1).equalsIgnoreCase("Personal Loan")){
					scheme = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 7);
					break;                                                          
				}
			}
			if(!scheme.equalsIgnoreCase("") && !scheme.equalsIgnoreCase("null")){
				try{
					String Query = "select S.SCHEMEID,S.SCHEMEDESC,S.LSM_PRODDIFFRATE,P.Code , P.grace_days,P.description,S.PRIME_TYPE from ng_MASTER_Scheme S join ng_master_product_parameter P on S.PRODUCTFLAG = P.CODE and S.SCHEMEDESC = '"+scheme+"'";
					mLogger.info("Query to fetch from ng_master_product_parameter"+Query +"");
					output = formObject.getNGDataFromDataCache(Query);
					mLogger.info("value ofscheme is"+scheme +"");
					if(output!=null && !output.isEmpty()){
						formObject.setNGValue("scheme_code","CNP1");
						formObject.setNGValue("Scheme_desc","PL");
						formObject.setNGValue("cmplx_LoanDisb_prod_diff_rate",output.get(0).get(2) );
						formObject.setNGValue("cmplx_LoanDisb_product_code",output.get(0).get(3) );
						formObject.setNGValue("cmplx_LoanDisb_gracedays", output.get(0).get(4));
						formObject.setNGValue("cmplx_LoanDisb_product_desc",output.get(0).get(5) );
						formObject.setNGValue("cmplx_LoanDetails_basetype",output.get(0).get(6) );

						if(formObject.isVisible("DecisionHistory_Frame1")==false){
							mLogger.info("PL" +"Inside IsVisible check for Decision History!!");
							formObject.fetchFragment("DecisionHistory", "DecisionHistory", "q_Decision");
						}

						String acc_no = formObject.getNGValue("cmplx_Decision_AccountNo");
						mLogger.info("cmplx_Decision_AccountNo: "+acc_no +"");
						if(acc_no.equalsIgnoreCase("") || acc_no == null ){

							String qry = "select AcctId from ng_RLOS_CUSTEXPOSE_AcctDetails where AcctType = 'CURRENT ACCOUNT' and ( Child_Wi = '"+formObject.getWFWorkitemName()+"' or Wi_Name ='"+formObject.getWFWorkitemName()+"')";
							mLogger.info("query is"+qry +"");
							List<List<String>> record = formObject.getDataFromDataSource(qry);
							if(record!=null && record.get(0)!=null && !record.isEmpty()){
								acc_no = record.get(0).get(0);
								//started By Akshay
								if(record.get(0).get(1).equalsIgnoreCase("RAKislamic CURRENT ACCOUNT")){
									acc_brnch=acc_no.substring(0,3);

								}
								else{
									acc_brnch=acc_no.substring(1,4);
								}
								//ended By Akshay
								mLogger.info("value of accno is"+acc_no +"");
								//formObject.setNGValue("Account_Number", record.get(0).get(0));
							}
							else{
								alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val036");//"Loan can't be created as No current account is maintained for this Customer";
								throw new ValidatorException(new FacesMessage(alert_msg));
							}	
						}
						//added By Akshay-21/6/2017	
						else{
							acc_brnch=acc_no.substring(0,3);
						}
						if(acc_no!=null && !acc_no.equalsIgnoreCase("")){
							formObject.setNGValue("Account_Number", acc_no);//added By Akshay
							formObject.setNGValue("acct_brnch",acc_brnch);
							formObject.setNGValue("Loan_Disbursal_SourcingBranch",acc_no.substring(0,4));  

						}



						outputResponse = genX.GenerateXML("NEW_LOAN_REQ","");
						ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
						mLogger.info("RLOS value of ReturnCode"+ReturnCode);
						ReturnDesc =  outputResponse.contains("<ReturnDesc>") ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
						mLogger.info("RLOS value of ReturnDesc"+ReturnDesc);
						String ContractID =  outputResponse.contains("<contractID>") ? outputResponse.substring(outputResponse.indexOf("<contractID>")+"</contractID>".length()-1,outputResponse.indexOf("</contractID>")):"";    
						mLogger.info("RLOS value of ContractID"+ContractID);
						if(ReturnCode.equalsIgnoreCase("0000")){
							mLogger.info("RLOS value of Loan Request"+"value of Loan_Req inside");
							//PL_Integration_Output.valueSetIntegration(outputResponse);  
							formObject.setNGValue("Is_LoanReq","Y");
							formObject.setNGValue("Contract_id",ContractID);
							alert_msg = NGFUserResourceMgr_PL.getResourceString_plValidations("val037")+ContractID; //"Contract created sucessfully with contract id: "+ContractID;
							mLogger.info("PL Disbursal "+ "message need to be displayed: "+alert_msg);
						} 
						else{
							alert_msg = NGFUserResourceMgr_PL.getResourceString_plValidations("val038");//"Error in Contract creation operation, kindly try after some time or contact administrator.";
							mLogger.info("PL Disbursal: "+ "Error while generating new loan: "+ ReturnCode);
						}
					}
					else{
						alert_msg = NGFUserResourceMgr_PL.getResourceString_plValidations("val038");//"Error in Contract creation operation, kindly try after some time or contact administrator.";
						mLogger.info("PL Disbursal "+"ng_master_product_parameter is not miantained for scheme: "+scheme );
					}
					formObject.RaiseEvent("WFSave");
				}
				catch(Exception e){
					if(alert_msg.equalsIgnoreCase("")){
						alert_msg = NGFUserResourceMgr_PL.getResourceString_plValidations("val038");//"Error in Contract creation operation, kindly try after some time or contact administrator.";
					}
					mLogger.info("PL Disbursal: "+ "Exception occured while generating new loan: "+ e.toString()+" desc: "+e.getMessage()+" stack: "+e.getStackTrace());
					printException(e);
					throw new ValidatorException(new FacesMessage(alert_msg));	
				}
				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			else{
				mLogger.info("PL Disbursal "+"Scheme code is not maintained for this case");
			}
		}  

		else if ("CAD_Add".equalsIgnoreCase(pEvent.getSource().getName())){
			//formObject.setNGValue("Address_wi_name",formObject.getWFWorkitemName());
			//SKLogger.writeLog("PL", "Inside add button: "+formObject.getNGValue("Address_wi_name"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_DECCAD_cmplx_gr_DECCAD");
		}

		else if ("CAD_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_DECCAD_cmplx_gr_DECCAD");
		}

		else if ("CAD_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_DECCAD_cmplx_gr_DECCAD");

		}

		else if ("FinacleCore_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setNGValue("Dds_wi_name",formObject.getWFWorkitemName());
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_FinacleCore_DDSgrid");
		}
		else if ("FinacleCore_Button3".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_FinacleCore_DDSgrid");

		}
		else if ("FinacleCore_Button2".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_FinacleCore_DDSgrid");
		}


		else if ("FinacleCore_Button4".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setNGValue("Inward_wi_name",formObject.getWFWorkitemName());
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_FinacleCore_inwardtt");
		}

		else if ("FinacleCore_Button6".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_FinacleCore_inwardtt");

		}
		else if ("FinacleCore_Button5".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_FinacleCore_inwardtt");
		}
		else if ("FinacleCore_Button7".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGClearRow", "cmplx_FinacleCore_inwardtt");
		}


		else if("SmartCheck1_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_SmartCheck1_SmartChkGrid_FCU");
		}

		else if("SmartCheck1_Add".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_SmartCheck1_SmartChkGrid_FCU");
		}

		else if("SmartCheck1_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_SmartCheck1_SmartChkGrid_FCU");
		}



		if("SmartCheck1_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_SmartCheck1_SmartChkGrid_FCU");
		}

		if("SmartCheck1_Add".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_SmartCheck1_SmartChkGrid_FCU");
		}
		if("SmartCheck1_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_SmartCheck1_SmartChkGrid_FCU");
		}

		//Code to addd dechtec call on Decision for pl start 
		if("DecisionHistory_Button5".equalsIgnoreCase(pEvent.getSource().getName())){	
			formObject.setNGValue("DecCallFired","Decision");
			mLogger.info("$$Before genX.GenerateXML for dectech call..outputResponse is : "+outputResponse);
			formObject.fetchFragment("IncomeDEtails", "IncomeDetails", "q_IncomeDetails");
			income_Dectech();
			formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmpDetails");
			employment_dectech();
			formObject.fetchFragment("MOL", "MOL1", "q_MOL");
			formObject.fetchFragment("World_Check", "WorldCheck1", "q_WorldCheck");	
			formObject.setTop("World_Check", formObject.getTop("MOL")+formObject.getHeight("MOL")+15);
			formObject.setTop("Reject_Enquiry", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+15);
			formObject.setTop("Salary_Enquiry", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+35);
			loadPicklistMol();
			outputResponse = genX.GenerateXML("DECTECH","");
			mLogger.info("$$After genX.GenerateXML for dectech call..outputResponse ASDASDASDASD : "+outputResponse);

			SystemErrorCode = outputResponse.contains("<SystemErrorCode>") ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"";
			mLogger.info("RLOS value of ReturnCode"+SystemErrorCode);
			if(SystemErrorCode.equalsIgnoreCase("")){
				PL_Integration_Output.valueSetIntegration(outputResponse);   
				alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val006");//"Decision engine integration successful";
				mLogger.info("after value set customer for dectech call");
				formObject.RaiseEvent("WFSave");
				throw new ValidatorException(new FacesMessage(alert_msg));				
			}
			else{
				alert_msg=NGFUserResourceMgr_PL.getResourceString_plValidations("val005");//"Critical error occurred Please contact administrator";
				throw new ValidatorException(new FacesMessage(alert_msg));
			}
		}	
		//Code to addd dechtec call on Decision for pl end  



		if("HomeCountryVerification_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Frame7");
		}
		if("ResidenceVerification_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Frame8");
		}
		if("BussinessVerification_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Frame6");
		}
		if("OfficeandMobileVerification_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Frame11");
		}
		if("GuarantorVerification_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Frame9");
		}
		if("ReferenceDetailVerification_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Frame10");
		}
		if("CustDetailVerification_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Frame5");
		}
		if("LoanandCard_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Frame13");
		}



		else if ("CustDetailVerification1_Button2".equalsIgnoreCase(pEvent.getSource().getName())) {
			String EmiratesID=formObject.getNGValue("cmplx_CustDetailverification1_EmiratesId");
			String CIFID=formObject.getNGValue("cmplx_CustDetailverification1_CIF_ID");
			mLogger.info("PL"+ "EmiratesID$"+EmiratesID+"$");
			String query=null;
			if("".equalsIgnoreCase(EmiratesID.trim()))
				//query="select distinct(EmiratesId),CustName,CifId,remarks,userName,Decisiom from NG_RLOS_GR_DECISION where CifId Like '%"+CIFID+"%'";
				query="select CustName,CifId,remarks,userName,Decisiom from ng_rlos_gr_decision where CifId Like '%"+CIFID+"%'";

			else
				//query="select distinct(EmiratesId),CustName,CifId,remarks,userName,Decisiom from NG_RLOS_GR_DECISION where EmiratesId Like '%"+EmiratesID + "%' or CifId Like '%"+CIFID+"'";
				query="select CustName,CifId,remarks,userName,Decisiom from ng_rlos_gr_decision where EmiratesID Like '%"+EmiratesID + "%' or CifId Like '%"+CIFID+"'";

			mLogger.info("PL"+ "query is: "+query);
			//populatePickListCustdetailWindow(query,"CustDetailVerification1_Button2", "Emirates ID, Customer Name,Cif ID,Fcu Remarks,FCU Analyst Name,FCU Decision", true, 20);
			List<List<String>> list=formObject.getDataFromDataSource(query);
			//m_objPickList.populateData(query);
			mLogger.info("PL"+"##Arun");
			for (List<String> a : list) 
			{

				formObject.addItemFromList("cmplx_CustDetailverification1_CustDetVer_GRID", a);
			}


		}//Arun (15/09/2017)

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
				mLogger.info("value received at index "+i+" is: "+str);
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


	public void setFormHeader(FormEvent pEvent){

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String activityName = formObject.getWFActivityName();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		try{
			mLogger.info("Inside formPopulated()" + pEvent.getSource());
			formObject.fetchFragment("CustomerDetails", "Customer", "q_Customer");
			formObject.setNGFrameState("CustomerDetails",0);
			formObject.fetchFragment("ProductContainer", "Product", "q_Product");
			formObject.setNGFrameState("ProductContainer",0);    
			// disha FSD
			formObject.setNGValue("WiLabel",formObject.getWFWorkitemName());
			formObject.setNGValue("UserLabel",formObject.getUserName()); 

			formObject.setNGValue("IntroDateLabel",formObject.getNGValue("IntroDateTime"));
			formObject.setNGValue("InitChannelLabel",formObject.getNGValue("InitiationType"));

			formObject.setNGValue("CifLabel",formObject.getNGValue("cmplx_Customer_CIFNO"));

			formObject.setNGValue("CRNLabel",formObject.getNGValue("CRN")); 

			Date date = new Date();
			formObject.setNGValue("CurrentDateLabel",dateFormat.format(date)); 

			String nationality = formObject.getNGValue("cmplx_Customer_Nationality");
			mLogger.info("Nationality is$$$$: "+nationality);
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

			mLogger.info("Itemindex is:" +  formObject.getWFFolderId());
			formObject.setNGValue("AppRefLabel", formObject.getNGValue("AppRefNo"));

			// ++ below code already exist - 10-10-2017
			//disha FSD P1 - Top section is blank partly
			formObject.setNGValue("RmTlNameLabel", formObject.getNGValue("RM_Name")); 
			formObject.setNGValue("Cmp_Emp_Label",formObject.getNGValue("Employer_Name"));
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
			printException(e);
		}
	}

	public void setActivityname(FormReference formObject , String activityName){
		try{
			formObject.setNGValue("QueueLabel","PL_"+activityName);
		}catch(Exception ex){
			mLogger.info("Exception:"+ex.getMessage());
			PLCommon.printException(ex);			
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
		mLogger.info("height of address is: "+formObject.getTop("Address_Details_Container")+"!!!"+formObject.getHeight("Address_Details_Container"));
		//formObject.setTop("CardDetails", formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container")+10);
		formObject.setTop("ReferenceDetails", formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container")+10);
		mLogger.info("height of reference is: "+formObject.getTop("ReferenceDetails")+"!!!"+formObject.getHeight("ReferenceDetails"));

		formObject.setTop("FATCA", formObject.getTop("ReferenceDetails")+30);//formObject.getHeight("ReferenceDetails")+10);
		mLogger.info("height of FATCA is: "+formObject.getTop("FATCA")+"!!!"+formObject.getHeight("FATCA"));

		formObject.setTop("KYC", formObject.getTop("FATCA")+30);//formObject.getHeight("FATCA")+20);
		mLogger.info("height of KYC is: "+formObject.getTop("KYC")+"!!!"+formObject.getHeight("KYC"));

		formObject.setTop("OECD", formObject.getTop("KYC")+30);//formObject.getHeight("KYC")+20);
		mLogger.info("height of OECD is: "+formObject.getTop("OECD")+"!!!"+formObject.getHeight("OECD"));

		formObject.setTop("Credit_card_Enq", formObject.getTop("OECD")+30);//formObject.getHeight("OECD")+20);
		mLogger.info("height of CreditCardEnq is: "+formObject.getTop("CreditCardEnq")+"!!!"+formObject.getHeight("CreditCardEnq"));

		formObject.setTop("Case_History", formObject.getTop("Credit_card_Enq")+30);//formObject.getHeight("Credit_card_Enq")+20);
		mLogger.info("height of CaseHistoryReport is: "+formObject.getTop("CaseHistoryReport")+"!!!"+formObject.getHeight("CaseHistoryReport"));

		formObject.setTop("LOS", formObject.getTop("Case_History")+30);//formObject.getHeight("Case_History")+20);
		mLogger.info("height of LOS is: "+formObject.getTop("LOS")+"!!!"+formObject.getHeight("LOS"));

		/*mLogger.info("RLOS","height of supplement is: "+formObject.getHeight("Supplementary_Container"));
        	//formObject.setTop("Supplementary_Container", formObject.getTop("CardDetails")+10);  
			formObject.setTop("FATCA", formObject.getTop("CardDetails")+30);
		 */	


	}

	public void setPartMatch(FormReference formObject){
		try{
			formObject.fetchFragment("Part_Match", "PartMatch", "q_PartMatch");
			formObject.setNGFrameState("Part_Match",0);
			//loadPicklist_PartMatch();
			mLogger.info("Inside Part_Match");  

		}
		catch(Exception ex){
			mLogger.info( "Exception:"+ex.getMessage());
			PLCommon.printException(ex);			
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
