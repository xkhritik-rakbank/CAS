
package com.newgen.omniforms.user;

import java.util.HashMap;

import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;
import com.newgen.omniforms.util.PL_SKLogger;


import com.newgen.omniforms.excp.CustomExceptionHandler;


import javax.faces.application.*;

public class PL_DDVT_Checker extends PLCommon implements FormListener
{
	boolean IsFragLoaded=false;
	String queryData_load="";
	String ReqProd=null;
	FormReference formObject = null;
	public void formLoaded(FormEvent pEvent)
	{
		System.out.println("Inside initiation RLOS");
		PL_SKLogger.writeLog("RLOS Initiation", "Inside formLoaded()" + pEvent.getSource().getName());

	}


	public void formPopulated(FormEvent pEvent) 
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try{
			System.out.println("Inside initiation RLOS");
			PL_SKLogger.writeLog("RLOS Initiation", "Inside formPopulated()" + pEvent.getSource());
			formObject.setNGValue("WiLabel",formObject.getWFWorkitemName());
			formObject.setNGValue("QueueLabel","PL_DDVT_Checker");
			formObject.setNGValue("User_Name",formObject.getUserName()); 
			formObject.setNGValue("Introduce_date",formObject.getNGValue("CreatedDate"));
			partMatchValues();
		}catch(Exception e)
		{
			PL_SKLogger.writeLog("RLOS Initiation", "Exception:"+e.getMessage());
		}
	}
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
		String popupFlag="N";
		String popUpMsg="";
		String popUpControl="";
		String outputResponse = "";
		String	ReturnCode="";
		String Message="";
		String alert_msg="";
		PL_SKLogger.writeLog("Inside PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		formObject =FormContext.getCurrentInstance().getFormReference();

		try{


			switch(pEvent.getType())
			{	

			case FRAME_EXPANDED:
				PL_SKLogger.writeLog(" In PL_Iniation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

				if (pEvent.getSource().getName().equalsIgnoreCase("CustomerDetails")) {
					hm.put("CustomerDetails","Clicked");
					popupFlag="N";
					formObject.fetchFragment("CustomerDetails", "Customer", "q_Customer");

					if (formObject.getNGValue("cmplx_Customer_NEP").equalsIgnoreCase("true")){
						formObject.setNGValue("cmplx_Customer_EmirateIDExpiry","");	
						formObject.setNGValue("cmplx_Customer_IdIssueDate","");
						formObject.setEnabled("cmplx_Customer_IdIssueDate",false);
						formObject.setEnabled("cmplx_Customer_EmirateIDExpiry",false);
					}
					loadPicklistCustomer();
					try
					{
						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
					}
					finally{hm.clear();}

				}
				else if (pEvent.getSource().getName().equalsIgnoreCase("ProductContainer")) {
					hm.put("ProductContainer","Clicked");
					popupFlag="N";
					formObject.fetchFragment("ProductContainer", "Product", "q_Product");
					LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
					LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");

					try
					{
						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
					}
					finally{hm.clear();}
				}

				else if (pEvent.getSource().getName().equalsIgnoreCase("GuarantorDet")) {
					hm.put("GuarantorDet","Clicked");
					popupFlag="N";
					formObject.fetchFragment("GuarantorDet", "GuarantorDetails", "q_Guarantor");
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

				else if (pEvent.getSource().getName().equalsIgnoreCase("LoanDetails")) {
					hm.put("LoanDetails","Clicked");
					popupFlag="N";
					formObject.fetchFragment("LoanDetails", "LoanDetails", "q_Loan");
					loadPicklist_LoanDetails();
					try
					{
						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
					}
					finally{hm.clear();}
				}

				else if (pEvent.getSource().getName().equalsIgnoreCase("EmploymentDetails")) {

					hm.put("EmploymentDetails","Clicked");
					popupFlag="N";
					formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmpDetails");

					loadPicklist_Employment();
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

					try
					{
						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
					}
					finally{hm.clear();}

				}
				
				else if (pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails")) 
				{
				
					formObject.fetchFragment("ReferenceDetails", "ReferenceDetails", "q_ReferenceDetails");
				
				}
				
				else if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
					hm.put("DecisionHistory","Clicked");
					popupFlag="N";
					formObject.fetchFragment("DecisionHistory", "DecisionHistory", "q_Decision");
					formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
					formObject.setNGFrameState("AltContactDetails", 1);
					loadPicklist3();
					loadInDecGrid();
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
					if(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true")||formObject.getNGValue("cmplx_Customer_NEP").equalsIgnoreCase("true")){
						formObject.setVisible("IncomingDoc_UploadSig", true);
					}
					else{
						formObject.setVisible("IncomingDoc_UploadSig", false);

					}
					try
					{
						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
					}
					finally{hm.clear();}
				}

				else if (pEvent.getSource().getName().equalsIgnoreCase("Part_Match")){
					formObject.fetchFragment("Part_Match", "PartMatch", "q_PartMatch");
					loadPicklist_PartMatch();
				}

				else if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRM_Incidents"))
					formObject.fetchFragment("FinacleCRM_Incidents", "FinacleCRMIncident", "");

				else if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRM_CustInfo"))
					formObject.fetchFragment("FinacleCRM_CustInfo", "FinacleCRMCustInfo", "");

				else if (pEvent.getSource().getName().equalsIgnoreCase("Finacle_Core"))
					formObject.fetchFragment("Finacle_Core", "FinacleCore", "");

				else if (pEvent.getSource().getName().equalsIgnoreCase("MOL"))
					formObject.fetchFragment("MOL", "MOL1", "");

				else if (pEvent.getSource().getName().equalsIgnoreCase("World_Check"))
					formObject.fetchFragment("World_Check", "WorldCheck1", "");

				else if (pEvent.getSource().getName().equalsIgnoreCase("Reject_Enq"))
					formObject.fetchFragment("Reject_Enq", "RejectEnq", "");

				else if (pEvent.getSource().getName().equalsIgnoreCase("Sal_Enq"))
					formObject.fetchFragment("Sal_Enq", "SalaryEnq", "");

				break;

			case FRAGMENT_LOADED:
				PL_SKLogger.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

				/*if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {

						}*/
				if (pEvent.getSource().getName().equalsIgnoreCase("Customer")) {
					//setDisabled();

					/*if(formObject.getNGValue("cmplx_Customer_NEP")=="false"){


									formObject.setLocked("Customer_Frame1",true);
									formObject.setHeight("Customer_Frame1", 640);
									formObject.setHeight("CustomerDetails", 650);

									formObject.setLocked("Customer_save",false);
									formObject.setLocked("FetchDetails",false);
									}
									if(formObject.getNGValue("cmplx_Customer_NEP")=="true"){
										formObject.setVisible("cmplx_Customer_EIDARegNo",true);
										formObject.setVisible("Customer_Label56", true);
									}*/
					formObject.setVisible("cmplx_Customer_EIDARegNo",true);
					formObject.setVisible("Customer_Label56", true);
					//Code change Deepak for NEP
					//formObject.setLocked("cmplx_Customer_CardNotAvailable",false);
					formObject.setLocked("ReadFromCard",false);
					//formObject.setLocked("cmplx_Customer_NEP",false);
					formObject.setLocked("Customer_CheckBox6",false);//CIF ID available
					formObject.setLocked("cmplx_Customer_referrorcode",false);
					formObject.setLocked("cmplx_Customer_referrorname",true);
					formObject.setLocked("cmplx_Customer_apptype",false);
					formObject.setLocked("cmplx_Customer_corpcode",false);
					formObject.setLocked("cmplx_Customer_bankwithus",false);
					formObject.setLocked("cmplx_Customer_noofdependent",false);
					formObject.setLocked("cmplx_Customer_minor",false);
					formObject.setLocked("cmplx_Customer_guarname",false);
					formObject.setLocked("cmplx_Customer_guarcif",false);
					loadPicklistCustomer();

					formObject.setEnabled("Customer_ReadFromCard",true);
					formObject.setEnabled("Customer_save",true);
					formObject.setEnabled("Customer_Reference_Add",true);
					formObject.setEnabled("Customer_Reference_modify",true);
					formObject.setEnabled("Customer_Reference_delete",true);
					//formObject.setLocked("cmplx_Customer_CARDNOTAVAIL",false);
					//formObject.setLocked("cmplx_Customer_NEP",false);
					formObject.setLocked("cmplx_Customer_ReferrorCode",false);
					formObject.setLocked("cmplx_Customer_ReferrorName",false);
					formObject.setLocked("cmplx_Customer_AppType",false);
					formObject.setLocked("cmplx_Customer_corporateCode",false);
					formObject.setLocked("cmplx_Customer_Bankingwithus",false);
					formObject.setLocked("cmplx_Customer_noofDependent",false);
					formObject.setLocked("cmplx_Customer_guardian",false);
					formObject.setLocked("cmplx_Customer_minor",false);
					formObject.setLocked("cmplx_Customer_DLNo", false);
					formObject.setLocked("cmplx_Customer_PAssport2", false);
					formObject.setLocked("cmplx_Customer_Passport3", false);
					formObject.setLocked("cmplx_Customer_PASSPORT4", false);


				}	

				if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
					LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
					LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
				}
				if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails")) {
					formObject.setLocked("cmplx_IncomeDetails_grossSal", true);
					formObject.setLocked("cmplx_IncomeDetails_TotSal", true);
					//formObject.setLocked("cmplx_IncomeDetails_netSal1", true);
					//formObject.setLocked("cmplx_IncomeDetails_netSal2", true);
					//formObject.setLocked("cmplx_IncomeDetails_netSal3", true);
					formObject.setLocked("cmplx_IncomeDetails_AvgNetSal", true);
					formObject.setLocked("cmplx_IncomeDetails_Overtime_Avg", true);
					formObject.setLocked("cmplx_IncomeDetails_Commission_Avg", true);
					formObject.setLocked("cmplx_IncomeDetails_FoodAllow_Avg", true);
					formObject.setLocked("cmplx_IncomeDetails_PhoneAllow_Avg", true);
					formObject.setLocked("cmplx_IncomeDetails_serviceAllow_Avg", true);
					formObject.setLocked("cmplx_IncomeDetails_Bonus_Avg", true);
					formObject.setLocked("cmplx_IncomeDetails_Other_Avg", true);
					formObject.setLocked("cmplx_IncomeDetails_Flying_Avg", true);
					LoadPickList("cmplx_IncomeDetails_AvgBalFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
					LoadPickList("cmplx_IncomeDetails_CreditTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
					LoadPickList("cmplx_IncomeDetails_AvgCredTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
					LoadPickList("cmplx_IncomeDetails_AnnualRentFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")) {

					formObject.setLocked("Liability_New_fetchLiabilities",true);
					formObject.setLocked("takeoverAMount",true);
					formObject.setLocked("cmplx_Liability_New_DBR",true);
					formObject.setLocked("cmplx_Liability_New_DBRNet",true);
					formObject.setLocked("cmplx_Liability_New_AggrExposure",true);
					formObject.setLocked("cmplx_Liability_New_TAI",true);
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) {

					Field_employment();
					formObject.setVisible("cmplx_EmploymentDetails_channelcode",false);
					formObject.setVisible("EMploymentDetails_Label36",false);
					formObject.setLocked("cmplx_EmploymentDetails_EmpName",true);
					formObject.setLocked("cmplx_EmploymentDetails_EMpCode",true);
					formObject.setLocked("cmplx_EmploymentDetails_LOS",true);
					formObject.setLocked("cmplx_EmploymentDetails_LOSPrevious",true);
					loadPicklist_Employment();
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("MiscellaneousFields")) {
					formObject.setLocked("cmplx_MiscFields_School",true);
					formObject.setLocked("cmplx_MiscFields_PropertyType",true);
					formObject.setLocked("cmplx_MiscFields_RealEstate",true);
					formObject.setLocked("cmplx_MiscFields_FarmEmirate",true);
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo")) {
					loadPicklistELigibiltyAndProductInfo();
					formObject.setNGValue("cmplx_EligibilityAndProductInfo_RepayFreq","Monthly");
					formObject.setVisible("ELigibiltyAndProductInfo_Label39",false);
					formObject.setVisible("cmplx_EligibilityAndProductInfo_instrumenttype",false);
					formObject.setVisible("ELigibiltyAndProductInfo_Label1",false);
					formObject.setVisible("cmplx_EligibilityAndProductInfo_TakeoverAMount",false);
					formObject.setVisible("ELigibiltyAndProductInfo_Label2",false);
					formObject.setVisible("cmplx_EligibilityAndProductInfo_takeoverBank",false);

					formObject.setLocked("cmplx_EligibilityAndProductInfo_LPF",true);
					formObject.setLocked("cmplx_EligibilityAndProductInfo_ageAtMaturity",true);
					formObject.setLocked("cmplx_EligibilityAndProductInfo_LPFAmount",true);
					formObject.setLocked("cmplx_EligibilityAndProductInfo_InsuranceAmount",true);
					formObject.setLocked("cmplx_EligibilityAndProductInfo_Insurance",true);
					formObject.setLocked("cmplx_EligibilityAndProductInfo_NumberOfInstallment",true);
					formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalDBR",true);
					formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalTAI",true);
					formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalLimit",true);
					formObject.setLocked("cmplx_EligibilityAndProductInfo_BAseRate",true);	
					formObject.setLocked("cmplx_EligibilityAndProductInfo_MArginRate",true);
					formObject.setLocked("cmplx_EligibilityAndProductInfo_ProdPrefRate",true);
					formObject.setLocked("cmplx_EligibilityAndProductInfo_MaturityDate",true);
					formObject.setLocked("cmplx_EligibilityAndProductInfo_InterestType",true);
					formObject.setLocked("cmplx_EligibilityAndProductInfo_BaseRateType",true);

				}
				if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails")) {
					loadPicklist_Address();
				}
				if (pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails")) {
					formObject.setVisible("AlternateContactDetails_custdomicile",false);
					formObject.setVisible("AltContactDetails_Label14",false);
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails")) {
					LoadPickList("nationality", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Country with (nolock)");
					LoadPickList("gender", "select '--Select--' union select convert(varchar, description) from NG_MASTER_gender with (nolock)");
					LoadPickList("ResdCountry", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Country with (nolock)");
					LoadPickList("relationship", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Relationship with (nolock)");

				}

				if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")) {
					LoadPickList("cmplx_FATCA_Category", "select '--Select--' union select convert(varchar, description) from NG_MASTER_category with (nolock)");
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("OECD")) {
					LoadPickList("OECD_CountryBirth", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Country with (nolock)");
					LoadPickList("OECD_townBirth", "select '--Select--' union select convert(varchar, description) from NG_MASTER_city with (nolock)");
					LoadPickList("OECD_CountryTaxResidence", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Country with (nolock)");
				}
				if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
					loadPicklist1();
					if(formObject.getNGValue("AlternateContactDetails_RetainAccIfLoanReq").equalsIgnoreCase("true")){
						formObject.setVisible("DecisionHistory_Button3", true);
						formObject.setVisible("DecisionHistory_updcust", true);
						formObject.setVisible("DecisionHistory_chqbook", true);
					}
				} 	


				break;

			case MOUSE_CLICKED:
				PL_SKLogger.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
				if (pEvent.getSource().getName().equalsIgnoreCase("Customer_ReadFromCard")){
					//GenerateXML();
				}

				else if (pEvent.getSource().getName().equalsIgnoreCase("Product_Add")){

					formObject.setNGValue("Product_wi_name",formObject.getWFWorkitemName());
					PL_SKLogger.writeLog("RLOS", "Inside add button: "+formObject.getNGValue("Product_wi_name"));
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


				else if (pEvent.getSource().getName().equalsIgnoreCase("Product_Modify")){

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

				else if (pEvent.getSource().getName().equalsIgnoreCase("Product_Delete")){
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
				else if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_addr_Add")){
					formObject.setNGValue("Address_wi_name",formObject.getWFWorkitemName());
					PL_SKLogger.writeLog("PL", "Inside add button: "+formObject.getNGValue("Address_wi_name"));
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_AddressDetails_cmplx_AddressGrid");
				}

				else if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_addr_Modify")){
					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_AddressDetails_cmplx_AddressGrid");
				}

				else if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_addr_Delete")){
					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_AddressDetails_cmplx_AddressGrid");

				}


				else if(pEvent.getSource().getName().equalsIgnoreCase("Customer_save")){
					PL_SKLogger.writeLog("PL_Initiation", "Inside Customer_save button: ");
					formObject.saveFragment("CustomerDetails");
				}

				else if(pEvent.getSource().getName().equalsIgnoreCase("Product_Save")){
					formObject.saveFragment("ProductContainer");
				}

				else if(pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails_Save")){
					formObject.saveFragment("GuarantorDetails");
				}

				else if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_Salaried_Save")){
					formObject.saveFragment("IncomeDEtails");
				}

				else if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_SelfEmployed_Save")){
					formObject.saveFragment("Incomedetails");
				}

				else if(pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails_Save")){
					formObject.saveFragment("CompanyDetails");
				}

				else if(pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails_Save")){
					formObject.saveFragment("PartnerDetails");
				}

				else if(pEvent.getSource().getName().equalsIgnoreCase("SelfEmployed_Save")){
					formObject.saveFragment("Liability_container");
				}

				else if(pEvent.getSource().getName().equalsIgnoreCase("Liability_New_Save")){
					formObject.saveFragment("InternalExternalContainer");
				}

				else if(pEvent.getSource().getName().equalsIgnoreCase("EmpDetails_Save")){
					formObject.saveFragment("EmploymentDetails");
				}

				else if(pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo_Save")){
					formObject.saveFragment("EligibilityAndProductInformation");
				}

				else if(pEvent.getSource().getName().equalsIgnoreCase("MiscellaneousFields_Save")){
					formObject.saveFragment("MiscFields");
				}

				else if(pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_Save")){
					formObject.saveFragment("Address_Details_container");
				}

				else if(pEvent.getSource().getName().equalsIgnoreCase("14.AltContactDetails_ContactDetails_Save")){
					formObject.saveFragment("Alt_Contact_container");
				}

				else if(pEvent.getSource().getName().equalsIgnoreCase("CardDetails_save")){
					formObject.saveFragment("Supplementary_Container");
				}

				else if(pEvent.getSource().getName().equalsIgnoreCase("FATCA_Save")){
					formObject.saveFragment("FATCA");
				}

				else if(pEvent.getSource().getName().equalsIgnoreCase("KYC_Save")){
					formObject.saveFragment("KYC");
				}

				else if(pEvent.getSource().getName().equalsIgnoreCase("OECD_Save")){
					formObject.saveFragment("OECD");
				}


				else if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Save")){
					formObject.saveFragment("DecisionHistoryContainer");
				}
				//tanshu started
				else if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_updcust")){

					PL_SKLogger.writeLog("RLOS value of ENTITY_MAINTENANCE_REQ","inside ENTITY_MAINTENANCE_REQ is generated");
					String acc_veri= (formObject.getNGValue("Is_Acc_verified")!=null) ?formObject.getNGValue("Is_Acc_verified"):"";
					String acc_mant_flag = formObject.getNGValue("Is_ACCOUNT_MAINTENANCE_REQ");
					PL_SKLogger.writeLog("PL checker Account Update call", "entity_flag : "+acc_veri);

					if(acc_veri == null || acc_veri.equalsIgnoreCase("")||acc_veri.equalsIgnoreCase("N")){
						outputResponse = GenerateXML("ENTITY_MAINTENANCE_REQ","AcctVerification");
						ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
						PL_SKLogger.writeLog("PL DDVT checker value of ReturnCode AcctVerification: ",ReturnCode);
						if(ReturnCode.equalsIgnoreCase("0000")){
							formObject.setNGValue("Is_Acc_verified","Y");
							acc_veri="Y";
							PL_SKLogger.writeLog("PL DDVT checker","account Verified successfully");
						}
						else{
							PL_SKLogger.writeLog("PL DDVT CHECKER : ","account Verified failed ReturnCode: "+ReturnCode );
							formObject.setNGValue("Is_Acc_verified","N");
							alert_msg= "Account Verification operation Failed, Please try after some time or contact administrator";
							popupFlag = "Y";
							throw new ValidatorException(new FacesMessage(alert_msg));
						}
					}

					String acc_acti= (formObject.getNGValue("Is_Acc_Active")!=null) ? formObject.getNGValue("Is_Acc_Active"):"";
					if(acc_veri.equalsIgnoreCase("Y")&&(acc_acti.equalsIgnoreCase("")||acc_acti.equalsIgnoreCase("N"))){
						outputResponse = GenerateXML("ENTITY_MAINTENANCE_REQ","AcctActivation");
						ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
						PL_SKLogger.writeLog("PL DDVT checker value of ReturnCode for AcctActivation: ",ReturnCode);
						if(ReturnCode.equalsIgnoreCase("0000")){
							formObject.setNGValue("Is_Acc_Active","Y");
							acc_acti="Y";
							PL_SKLogger.writeLog("PL DDVT checker","account Activation successfully");
						}
						else{
							PL_SKLogger.writeLog("PL DDVT CHECKER : ","ENTITY_MAINTENANCE_REQ is not generated ReturnCode: "+ReturnCode );
							formObject.setNGValue("Is_Acc_Active","N");
							alert_msg= "Account Activation operation Failed, Please try after some time or contact administrator";
							popupFlag = "Y";
							throw new ValidatorException(new FacesMessage(alert_msg));
						}

					}

					if(acc_veri.equalsIgnoreCase("Y")&&acc_acti.equalsIgnoreCase("Y")) {
						outputResponse = GenerateXML("ACCOUNT_MAINTENANCE_REQ","");
						ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
						PL_SKLogger.writeLog("PL DDVT checker value of ReturnCode",ReturnCode);
						if(ReturnCode.equalsIgnoreCase("0000") ){
							formObject.setNGValue("Is_ACCOUNT_MAINTENANCE_REQ","Y");
							PL_SKLogger.writeLog("PL DDVT checker value of ENTITY_MAINTENANCE_REQ","value of ACCOUNT_MAINTENANCE_REQ"+formObject.getNGValue("Is_ACCOUNT_MAINTENANCE_REQ"));
							valueSetCustomer(outputResponse);    
							PL_SKLogger.writeLog("PL DDVT checker value of ACCOUNT_MAINTENANCE_REQ","ACCOUNT_MAINTENANCE_REQ is generated");
							PL_SKLogger.writeLog("PL DDVT checker value of Customer Details",formObject.getNGValue("Is_ACCOUNT_MAINTENANCE_REQ"));
							formObject.RaiseEvent("WFSave");
							alert_msg= "Account Updated Successfully!!";
							popupFlag = "Y";
							throw new ValidatorException(new FacesMessage(alert_msg));
						}
						else{
							PL_SKLogger.writeLog("PL DDVT CHECKER :","ACCOUNT_MAINTENANCE_REQ is not generated ReturnCode: "+ReturnCode );
							formObject.setNGValue("Is_ACCOUNT_MAINTENANCE_REQ","N");
							alert_msg= "Account Update operation Failed, Please try after some time or contact administrator";
							popupFlag = "Y";
							throw new ValidatorException(new FacesMessage(alert_msg));
						}
					}										
				}
				
				else if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Button4"))
				{
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_notegrid");
				}

				else if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Button5"))
				{

					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_NotepadDetails_cmplx_notegrid");
				}
				else if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Button6"))
				{

					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_NotepadDetails_cmplx_notegrid");
				}

				else if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Button3")){

					popupFlag = "Y";
					String message = CustomerUpdate();
					throw new ValidatorException(new FacesMessage(message));

				}
				else if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_chqbook")){
					PL_SKLogger.writeLog("DDVT Checker ", "Inside Check Book and DDS request");
					outputResponse = GenerateXML("NEW_CARD_REQ","");
					String DCR_flag = "N";
					String CBS_flag = "N";
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					if(ReturnCode.equalsIgnoreCase("0000")){
						DCR_flag="Y";
					}
					else{
						PL_SKLogger.writeLog("DDVT Checker DCR operation: ", "Error in DDS Request operation Return code is: "+ReturnCode);
					}

					outputResponse = GenerateXML("CHEQUE_BOOK_ELIGIBILITY","");

					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					if(ReturnCode.equalsIgnoreCase("0000")){
						CBS_flag="Y";
					}
					else{
						PL_SKLogger.writeLog("DDVT Checker CBS operation: ", "Error in CBS Request operation Return code is: "+ReturnCode);
					}

					if(DCR_flag.equalsIgnoreCase("Y")&&CBS_flag.equalsIgnoreCase("Y")){
						alert_msg= "Debit Card and Cheque Book created successfully ";
						popupFlag = "Y";
						throw new ValidatorException(new FacesMessage(alert_msg));
					}
					else if(DCR_flag.equalsIgnoreCase("Y")&&CBS_flag.equalsIgnoreCase("N")){
						alert_msg= "Debit Card created sucessfully, But Cheque Book created Failed.";
						popupFlag = "Y";
						throw new ValidatorException(new FacesMessage(alert_msg));
					}
					else if(DCR_flag.equalsIgnoreCase("N")&&CBS_flag.equalsIgnoreCase("Y")){
						alert_msg= "Cheque Book created sucessfully, But Debit Card created Failed.";
						popupFlag = "Y";
						throw new ValidatorException(new FacesMessage(alert_msg));
					}
					else{
						alert_msg= "Cheque Book Request and Debit Card request Failed, Please try after time or contact administrator";
						popupFlag = "Y";
						throw new ValidatorException(new FacesMessage(alert_msg));
					}
				}
				else if(pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_Button1"))
				{
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_WorldCheck_WorldCheck_Grid");
				}

				else if(pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_Button2"))
				{

					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_WorldCheck_WorldCheck_Grid");
				}

				else if(pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_Button3"))
				{
					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_WorldCheck_WorldCheck_Grid");
				}
				else{
					PL_SKLogger.writeLog("PL DDVT Checker", "This control is not maintained in code control name:"+pEvent.getSource().getName());
				}

				//tanshu ended
				break;

			case VALUE_CHANGED:
				PL_SKLogger.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

				if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_Customer_DOb")){
					PL_SKLogger.writeLog("RLOS val change ", "Value of dob is:"+formObject.getNGValue("cmplx_Customer_DOb"));
					getAge(formObject.getNGValue("cmplx_Customer_DOb"));
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("ReqProd")){
					ReqProd=formObject.getNGValue("ReqProd");
					PL_SKLogger.writeLog("RLOS val change ", "Value of ReqProd is:"+ReqProd);
					loadPicklistProduct(ReqProd);
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("SubProd")){
					PL_SKLogger.writeLog("RLOS val change ", "Value of SubProd is:"+formObject.getNGValue("SubProd"));
					formObject.clear("AppType");
					formObject.setNGValue("AppType","--Select--");
					if(formObject.getNGValue("SubProd").equalsIgnoreCase("Business titanium Card")){
						LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='BTC'");
						formObject.setNGValue("EmpType","Self Employed");
					}	
					else if(formObject.getNGValue("SubProd").equalsIgnoreCase("Instant Money"))
						LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType  with (nolock) where subProdFlag='IM'");

					else if(formObject.getNGValue("SubProd").equalsIgnoreCase("Limit Increase"))
						LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='LI'");

					else if(formObject.getNGValue("SubProd").equalsIgnoreCase("Salaried Credit Card"))
						LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='SAL'");

					else if(formObject.getNGValue("SubProd").equalsIgnoreCase("Self Employed Credit Card"))
						LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='SE'");

					else if(formObject.getNGValue("SubProd").equalsIgnoreCase("Expat Personal Loans"))
						LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='EXP'");

					else if(formObject.getNGValue("SubProd").equalsIgnoreCase("National Personal Loans"))
						LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='NAT'");

					else if(formObject.getNGValue("SubProd").equalsIgnoreCase("Pre-Approved"))
						LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='PA'");

					else if(formObject.getNGValue("SubProd").equalsIgnoreCase("Product Upgrade"))
						LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='PU'");
				}
				if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_Decision_Decision")) {
					if(formObject.getWFActivityName().equalsIgnoreCase("CAD_Analyst1"))	
					{
						formObject.setNGValue("CAD_dec", formObject.getNGValue("cmplx_Decision_Decision"));
						PL_SKLogger.writeLog(" In PL_Initiation VALChanged---New Value of CAD_dec is: ", formObject.getNGValue("cmplx_Decision_Decision"));

					}

					else{

						formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
						PL_SKLogger.writeLog(" In PL_Initiation VALChanged---New Value of decision is: ", formObject.getNGValue("cmplx_Decision_Decision"));
					}
				}
			default: break;

			}
		}
		
		finally{PL_SKLogger.writeLog("PL","Inside finally!!!");}
	/*	catch(Exception ex)
		{
			PL_SKLogger.writeLog("PL DDVY maker","Inside Exception to show msg at front end");
			if(ex instanceof ValidatorException)
			{   PL_SKLogger.writeLog("PL DDVY maker","popupFlag value: "+ popupFlag);
			if(popupFlag.equalsIgnoreCase("Y"))
			{
				PL_SKLogger.writeLog("PL DDVY maker","Inside popup msg through Exception "+ popupFlag);
				if(popUpControl.equals(""))
				{
					PL_SKLogger.writeLog("PL DDVY maker","Before show Exception at front End "+ popupFlag);
					throw new ValidatorException(new FacesMessage(alert_msg));
				}else
				{
					throw new ValidatorException(new FacesMessage(popUpMsg,popUpControl));

				}

			}
			else{
				HashMap<String,String> hm1=new HashMap<String,String>();
				hm1.put("Error","Checked");
				if(!popUpMsg.equals("")) {
					try{ throw new ValidatorException(new CustomExceptionHandler("Details Fetched", popUpMsg,"EventType", hm1));}finally{hm1.clear();}

				}
				else {
					try{ throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm1));}finally{hm1.clear();}

				}

			}
			}
			else
			{
				ex.printStackTrace();
				System.out.println("exception in eventdispatched="+ ex);
			}
		}*/
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
		formObject.setNGValue("ddvt_checker_dec", formObject.getNGValue("cmplx_DecisionHistory_Decision"));
		saveIndecisionGrid();
	}

}

