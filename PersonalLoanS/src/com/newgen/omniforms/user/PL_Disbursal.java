
package com.newgen.omniforms.user;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;
import com.newgen.omniforms.util.PL_SKLogger;


import com.newgen.omniforms.excp.CustomExceptionHandler;

public class PL_Disbursal extends PLCommon implements FormListener
{
	boolean IsFragLoaded=false;
	String queryData_load="";
	FormReference formObject = null;
	public void formLoaded(FormEvent pEvent)
	{
		System.out.println("Inside initiation PL");
		PL_SKLogger.writeLog("PL Initiation", "Inside formLoaded()" + pEvent.getSource().getName());

	}


	public void formPopulated(FormEvent pEvent) 
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try{
			System.out.println("Inside initiation PL");
			PL_SKLogger.writeLog("PL Initiation", "Inside formPopulated()" + pEvent.getSource());
			formObject.setNGValue("WiLabel",formObject.getWFWorkitemName());
			formObject.setNGValue("QueueLabel","PL_Disbursal");
			formObject.setNGValue("User_Name",formObject.getUserName()); 
			formObject.setNGValue("Introduce_date",formObject.getNGValue("CreatedDate"));
			partMatchValues();
		}catch(Exception e)
		{
			PL_SKLogger.writeLog("PL Initiation", "Exception:"+e.getMessage());
		}
	}
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
		String popupFlag="N";
		String popUpMsg="";
		String popUpControl="";
		String outputResponse="";
		String ReturnCode="";
		String buttonClickFlag="";
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
					//formObject.setNGValue("cmplx_LoanDetails_inttype", "E");
					loadPicklist_LoanDetails();
					//formObject.setNGValue("cmplx_LoanDetails_basetype", "2");

					try
					{
						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
					}
					finally{hm.clear();}
				}

				else if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
					hm.put("DecisionHistory","Clicked");
					popupFlag="N";
					formObject.fetchFragment("DecisionHistory", "DecisionHistory", "q_Decision");

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
					try
					{
						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
					}
					finally{hm.clear();}
				}

				else if (pEvent.getSource().getName().equalsIgnoreCase("Loan_Disbursal")) {
					hm.put("Loan_Disbursal","Clicked");
					popupFlag="N";

					formObject.fetchFragment("Loan_Disbursal", "Loan_Disbursal", "q_LoanDisb");
					formObject.setHeight("Loan_Disbursal_Frame2", 200);
					try
					{
						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
					}
					finally{hm.clear();}
				}

				else if (pEvent.getSource().getName().equalsIgnoreCase("CC_Creation")) {
					PL_SKLogger.writeLog(" In CC_Creation","Start" );
					hm.put("CC_Creation","Clicked");
					popupFlag="N";

					formObject.fetchFragment("CC_Creation", "CC_Creation", "q_CCCreation");
					PL_SKLogger.writeLog(" In CC_Creation","end" );

					try
					{
						PL_SKLogger.writeLog(" In CC_Creation","Error" );
						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
					}
					finally{hm.clear();}
				}

				else if (pEvent.getSource().getName().equalsIgnoreCase("Limit_Inc")) {
					hm.put("Limit_Inc","Clicked");
					popupFlag="N";

					formObject.fetchFragment("Limit_Inc", "Limit_Inc", "q_LimitInc");

					try
					{
						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
					}
					finally{hm.clear();}
				}

				break;

			case FRAGMENT_LOADED:
				PL_SKLogger.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

				/*if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {

						}*/
				if (pEvent.getSource().getName().equalsIgnoreCase("Customer")) {
					//setDisabled();
					formObject.setLocked("Customer_Frame1",true);
				}	

				if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
					formObject.setLocked("Product_Frame1",true);
					LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
					LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails")) {

					formObject.setLocked("GuarantorDetails_Frame1",true);
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails")) {

					formObject.setLocked("IncomeDetails_Frame1",true);
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")) {

					formObject.setLocked("ExtLiability_Frame1",true);
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) {
					Field_employment();
					//formObject.setLocked("EMploymentDetails_Frame1",true);
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo")) {

					formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
					loadPicklistELigibiltyAndProductInfo();
					//formObject.setNGValue("cmplx_EligibilityAndProductInfo_InstrumentType", "Salary");
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("LoanDetails")) {

					formObject.setLocked("LoanDetails_Frame1",true);
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails")) {
					loadPicklist_Address();
					formObject.setLocked("AddressDetails_Frame1",true);
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails")) {

					formObject.setLocked("AltContactDetails_Frame1",true);
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails")) {

					formObject.setLocked("CardDetails_Frame1",true);
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails")) {

					formObject.setLocked("ReferenceDetails_Frame1",true);
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails")) {

					formObject.setLocked("SupplementCardDetails_Frame1",true);
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")) {

					formObject.setLocked("FATCA_Frame6",true);
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("KYC")) {

					formObject.setLocked("KYC_Frame1",true);
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("OECD")) {

					formObject.setLocked("OECD_Frame8",true);
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("PartMatch")) {

					formObject.setLocked("PartMatch_Frame1",true);
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMIncident")) {

					formObject.setLocked("FinacleCRMIncident_Frame1",true);
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMCustInfo")) {

					formObject.setLocked("FinacleCRMCustInfo_Frame1",true);
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCore")) {

					formObject.setLocked("FinacleCore_Frame1",true);
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("MOL1")) {

					formObject.setLocked("MOL1_Frame1",true);
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1")) {

					formObject.setLocked("WorldCheck1_Frame1",true);
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("RejectEnq")) {

					formObject.setLocked("RejectEnq_Frame1",true);
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("SalaryEnq")) {

					formObject.setLocked("SalaryEnq_Frame1",true);
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("CustDetailVerification")) {

					formObject.setLocked("CustDetailVerification_Frame1",true);
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("BussinessVerification")) {

					formObject.setLocked("BussinessVerification_Frame1",true);
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("HomeCountryVerification")) {

					formObject.setLocked("HomeCountryVerification_Frame1",true);
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("ResidenceVerification")) {

					formObject.setLocked("ResidenceVerification_Frame1",true);
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("GuarantorVerification")) {

					formObject.setLocked("GuarantorVerification_Frame1",true);
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetailVerification")) {

					formObject.setLocked("ReferenceDetailVerification_Frame1",true);
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("OfficeandMobileVerification")) {

					formObject.setLocked("OfficeandMobileVerification_Frame1",true);
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("LoanandCard")) {

					formObject.setLocked("LoanandCard_Frame1",true);
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails")) {

					formObject.setLocked("NotepadDetails_Frame1",true);
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("SmartCheck")) {

					formObject.setLocked("SmartCheck_Frame1",true);
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("Compliance")) {

					formObject.setLocked("Compliance_Frame1",true);
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("FCU_Decision")) {

					formObject.setLocked("FCU_Decision_Frame1",true);
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
					if(formObject.isVisible("AlternateContactDetails_Frame1")==false){
						formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
						formObject.setNGFrameState("AltContactDetails", 0);
						formObject.setTop("ReferenceDetails", formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container")+10);
						if(formObject.getNGFrameState("ReferenceDetails")==0){
							formObject.setTop("FATCA", formObject.getTop("ReferenceDetails")+formObject.getHeight("ReferenceDetails")+10);
						}
						else{
							formObject.setTop("FATCA", formObject.getTop("ReferenceDetails")+30);
						}
						if(formObject.getNGFrameState("FATCA")==0){
							formObject.setTop("KYC", formObject.getTop("FATCA")+formObject.getHeight("FATCA")+10);
						}else{
							formObject.setTop("KYC", formObject.getTop("FATCA")+30);
						}
						if(formObject.getNGFrameState("KYC")==0){
							formObject.setTop("OECD", formObject.getTop("KYC")+formObject.getHeight("KYC")+10);
						}else{
							formObject.setTop("OECD", formObject.getTop("KYC")+30);
						}
						PL_SKLogger.writeLog("PL", "Inside decision fragment load: Contact Details not Visible!!");
					}	
					loadPicklist1();
					if(formObject.getNGValue("AlternateContactDetails_RetainAccIfLoanReq").equalsIgnoreCase("false")){
						formObject.setVisible("DecisionHistory_Button3", true);
						formObject.setVisible("DecisionHistory_updcust", true);
						formObject.setVisible("DecisionHistory_chqbook", false);
					}
					else{
						formObject.setVisible("DecisionHistory_chqbook", false);
					}
				} 	


				break;

			case MOUSE_CLICKED:
				PL_SKLogger.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
				if (pEvent.getSource().getName().equalsIgnoreCase("Customer_ReadFromCard")){
					//GenerateXML();
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

				else if(pEvent.getSource().getName().equalsIgnoreCase("Loan_Disbursal_save")){
					formObject.saveFragment("Loan_Disbursal");
				}

				else if(pEvent.getSource().getName().equalsIgnoreCase("CC_Creation_save")){
					formObject.saveFragment("CC_Creation");
				}

				else if(pEvent.getSource().getName().equalsIgnoreCase("Limit_Inc_save")){
					formObject.saveFragment("Limit_Inc");
				}

				//started code merged
				else  if(pEvent.getSource().getName().equalsIgnoreCase("CC_Creation_Update_Customer")){
					PL_SKLogger.writeLog("","inside Update_Customer");  
					outputResponse = GenerateXML("CUSTOMER_DETAILS","Inquire");
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);

					if(ReturnCode.equalsIgnoreCase("0000")){
						PL_SKLogger.writeLog("RLOS value of Customer_Details","value of Customer_Details inside inquiry code");
						formObject.setNGValue("Is_CustInquiry_Disbursal","Y"); 
						PL_SKLogger.writeLog("","Inquiry Flag Value"+formObject.getNGValue("Is_CustInquiry_Disbursal")); 
						PL_SKLogger.writeLog("","inside Update_Customer");  
						String cif_status = (outputResponse.contains("<CustomerStatus>")) ? outputResponse.substring(outputResponse.indexOf("<CustomerStatus>")+"</CustomerStatus>".length()-1,outputResponse.indexOf("</CustomerStatus>")):"";
						if(cif_status.equalsIgnoreCase("ACTVE")){
							outputResponse = GenerateXML("CUSTOMER_DETAILS","Lock");
							ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
							PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
							if(ReturnCode.equalsIgnoreCase("0000")){
								PL_SKLogger.writeLog("RLOS value of Customer_Details","value of Customer_Details inside lock code");
								formObject.setNGValue("Is_CustLock_Disbursal","Y");
								PL_SKLogger.writeLog("","Inquiry Flag Is_CustLock_Disbursal value"+formObject.getNGValue("Is_CustLock_Disbursal")); 

								PL_SKLogger.writeLog("RLOS value of Customer_Details","Customer_Details is generated");
								//    SKLogger.writeLog("RLOS value of Customer Details",formObject.getNGValue("Is_ACCOUNT_MAINTENANCE_REQ"));
								outputResponse = GenerateXML("CUSTOMER_DETAILS","UnLock");
								ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
								PL_SKLogger.writeLog("RLOS value of ReturnCode","inside unlock");
								PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
								formObject.setNGValue("Is_CustUnLock_Disbursal","Y");
								PL_SKLogger.writeLog("","Inquiry Flag Is_CustUnLock_Disbursal value"+formObject.getNGValue("Is_CustUnLock_Disbursal"));
								if(ReturnCode.equalsIgnoreCase("0000")){
									outputResponse = GenerateXML("CUSTOMER_UPDATE_REQ","");
									ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
									PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
									//  Message =(outputResponse.contains("<Message>")) ? outputResponse.substring(outputResponse.indexOf("<Message>")+"</Message>".length()-1,outputResponse.indexOf("</Message>")):"";    
									//  SKLogger.writeLog("RLOS value of Message",Message);

									if(ReturnCode.equalsIgnoreCase("0000")){
										formObject.setNGValue("Is_CUSTOMER_UPDATE_REQ_Disbursal","Y");
										PL_SKLogger.writeLog("RLOS value of CUSTOMER_UPDATE_REQ","value of CUSTOMER_UPDATE_REQ"+formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
										valueSetCustomer(outputResponse);    
										PL_SKLogger.writeLog("RLOS value of CUSTOMER_UPDATE_REQ","CUSTOMER_UPDATE_REQ is generated");
										PL_SKLogger.writeLog("RLOS value of CUSTOMER_UPDATE_REQ",formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ_Disbursal"));
									}
									else{
										PL_SKLogger.writeLog("Customer Details","CUSTOMER_UPDATE_REQ is not generated");
										formObject.setNGValue("Is_CUSTOMER_UPDATE_REQ_Disbursal","N");
									}
									PL_SKLogger.writeLog("RLOS value of CUSTOMER_UPDATE_REQ",formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ_Disbursal"));
									formObject.RaiseEvent("WFSave");
									PL_SKLogger.writeLog("RLOS value of CUSTOMER_UPDATE_REQ","after saving the flag");
									if((formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ_Disbursal").equalsIgnoreCase("Y")))
									{ 
										PL_SKLogger.writeLog("RLOS value of Is_CUSTOMER_UPDATE_REQ","inside if condition");
										formObject.setEnabled("Update_Customer", false); 
										buttonClickFlag="CC_Creation_Update_Customer";
										throw new ValidatorException(new CustomExceptionHandler("Customer Updated Successful!!","Update_Customer#Customer Updated Successful!!","",hm));
									}
									else{
										formObject.setEnabled("DecisionHistory_Button3", true);
										throw new ValidatorException(new CustomExceptionHandler("Customer Updated Fail!!","Update_Customer#Customer Updated Fail!!","",hm));
									}
								}
								else{
									PL_SKLogger.writeLog("Customer Details","Customer_Details unlock is not generated");

								}
							}
							else{
								PL_SKLogger.writeLog("Customer Details","Customer_Details lock is not generated");
							}
						}
						else {
							PL_SKLogger.writeLog("DDVT Checker Customer Update operation: ", "Error in Cif Enquiry operation CIF Staus is: "+ cif_status);
							popupFlag = "Y";
							throw new ValidatorException(new  CustomExceptionHandler("Customer Is InActive!!","FetchDetails#Customer Is InActive!!","",hm));
						}
						//added one more here
					}
					else{
						PL_SKLogger.writeLog("Customer Details","Customer_Details Inquiry is not generated");
					}
				}
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
							alert_msg= "Account Updated Successfully !";
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

				else if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Button3")){
					popupFlag = "Y";
					String message = CustomerUpdate();
					throw new ValidatorException(new FacesMessage(message));
				}
				//code added for card_services call(Tanshu Aggarwal-15/06/2017)
				else if(pEvent.getSource().getName().equalsIgnoreCase("CC_Creation_Card_Services")){

					PL_SKLogger.writeLog("RLOS value of ReturnCode","inside button click");
					String Product_Name=formObject.getNGValue("cmplx_CCCreation_pdt");
					PL_SKLogger.writeLog("RLOS value of Product_Name",""+Product_Name);
					if(Product_Name.equalsIgnoreCase("Limit Change Request")){
						outputResponse = GenerateXML("CARD_SERVICES_REQUEST","Limit Change Request");
						ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
						PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);

						if(ReturnCode.equalsIgnoreCase("0000")){
							PL_SKLogger.writeLog("RLOS value of Customer_Details","value of Customer_Details inside inquiry code");
							formObject.setNGValue("Is_CardServicesPL","Y");
							PL_SKLogger.writeLog("RLOS value of Customer_Details for limit change",formObject.getNGValue("Is_CardServicesPL"));
						}
					}
					else if(Product_Name.equalsIgnoreCase("Financial Services Request")){
						outputResponse = GenerateXML("CARD_SERVICES_REQUEST","Financial Services Request");
						ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
						PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
						if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")){
							PL_SKLogger.writeLog("RLOS value of Customer_Details","value of Customer_Details inside inquiry code");
							formObject.setNGValue("Is_CardServicesPL","Y"); 
							PL_SKLogger.writeLog("RLOS value of Customer_Details for financial product",formObject.getNGValue("Is_CardServicesPL"));
						}
					}
					else if(Product_Name.equalsIgnoreCase("Product Upgrade Request")){
						outputResponse = GenerateXML("CARD_SERVICES_REQUEST","Product Upgrade Request");
						ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
						PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
						if(ReturnCode.equalsIgnoreCase("0000")){
							PL_SKLogger.writeLog("RLOS value of Customer_Details","value of Customer_Details inside inquiry code");
							formObject.setNGValue("Is_CardServicesPL","Y"); 
							PL_SKLogger.writeLog("RLOS value of Customer_Details for product upgrade",formObject.getNGValue("Is_CardServicesPL"));
						}
					}
				}
				//end of entity main and account mainitainence call
				//code added for card_services call(Tanshu Aggarwal-15/06/2017)
				//code for New Card and Notification and New Loan Request call Tanshu Aggarwal
				else if(pEvent.getSource().getName().equalsIgnoreCase("CC_Creation_Contract_Creation")){
					//31/07/2017 code added
					PL_SKLogger.writeLog("iNSIDE CC_Creation_Contract_Creation ","1");
					outputResponse = GenerateXML("NEW_CREDITCARD_REQ","");
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					PL_SKLogger.writeLog("RLOS value of ReturnCode NEW_CREADITCARD_REQ",ReturnCode);
					if(ReturnCode.equalsIgnoreCase("0000")){
						PL_SKLogger.writeLog("RLOS value of Loan Request","value of Loan_Req inside");
						valueSetCustomer(outputResponse);  
						formObject.setNGValue("Is_NEW_CREADITCARD_REQ","Y");

						alert_msg = "New Card Request has been Successfully created";
						PL_SKLogger.writeLog("PL Disbursal ", "message need to be displayed: "+alert_msg);
					} 
					else{
						formObject.setNGValue("Is_NEW_CREADITCARD_REQ","N");
						alert_msg = "Error in  creation of the New Card , kindly try after some time or contact administrator.";
						PL_SKLogger.writeLog("PL Disbursal: ", "Error while generating New Request: "+ ReturnCode);
					}
					//31/07/2017 code ended


				}	

				else if(pEvent.getSource().getName().equalsIgnoreCase("CC_Creation_Button2")){//Code for CARDNOTIFICATION done by aman	
					formObject.RaiseEvent("WFSave");
					PL_SKLogger.writeLog("RLOS value of ReturnCode","inside button click");
					String Product_Name=formObject.getNGValue("cmplx_CCCreation_pdt");
					PL_SKLogger.writeLog("RLOS value of Product_Name",""+Product_Name);
					outputResponse = GenerateXML("CARD_NOTIFICATION","");
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
					formObject.RaiseEvent("WFSave");
					if(ReturnCode.equalsIgnoreCase("0000")){
						PL_SKLogger.writeLog("RLOS value of Customer_Details","value of Customer_Details inside lock code");
						formObject.setNGValue("Is_CardNotifiactionPL","Y");
						PL_SKLogger.writeLog("RLOS value of ReturnCode Is_CardNotifiactionPL",""+formObject.getNGValue("Is_CardNotifiactionPL"));
						alert_msg= "Card Notification is successful !";
						popupFlag = "Y";
						throw new ValidatorException(new FacesMessage(alert_msg));

					}
					else
					{
						PL_SKLogger.writeLog("PL DDVT CHECKER :","ACCOUNT_MAINTENANCE_REQ is not generated ReturnCode: "+ReturnCode );
						formObject.setNGValue("Is_ACCOUNT_MAINTENANCE_REQ","N");
						alert_msg= "Card Notification is failed";
						popupFlag = "Y";
						throw new ValidatorException(new FacesMessage(alert_msg));
					}

					/*  if(Product_Name.equalsIgnoreCase("Limit Change Request")){
	                             outputResponse = GenerateXML("CARD_SERVICES_REQUEST","Limit Change Request");
	                             ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
	                             PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
	                             if(ReturnCode.equalsIgnoreCase("0000")){
	                                 PL_SKLogger.writeLog("RLOS value of Customer_Details","value of Customer_Details inside inquiry code");
	                                 formObject.setNGValue("Is_CardServicesPL","Y");
	                                 PL_SKLogger.writeLog("RLOS value of ReturnCode Is_CardServices",""+formObject.getNGValue("Is_CardServicesPL"));
	                                 PL_SKLogger.writeLog("","CARD SERVICES RUNNING Limit Change Request");
	                                 outputResponse = GenerateXML("CARD_NOTIFICATION","Limit Change Request");
	                                 ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
	                                 PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
	                                 if(ReturnCode.equalsIgnoreCase("0000")){
	                                     PL_SKLogger.writeLog("RLOS value of Customer_Details","value of Customer_Details inside lock code");
	                                     formObject.setNGValue("Is_CardNotifiactionPL","Y");
	                                     PL_SKLogger.writeLog("RLOS value of ReturnCode Is_CardNotifiactionPL",""+formObject.getNGValue("Is_CardNotifiactionPL"));
	                                 }
	                             }
	                         }
	                         //for another product
	                         if(Product_Name.equalsIgnoreCase("Financial Services Request")){
	                             outputResponse = GenerateXML("CARD_SERVICES_REQUEST","Financial Services Request");
	                             ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
	                             PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
	                             if(ReturnCode.equalsIgnoreCase("0000")){
	                                 PL_SKLogger.writeLog("RLOS value of Customer_Details","value of Customer_Details inside inquiry code");
	                                 formObject.setNGValue("Is_CardServicesPL","Y"); 
	                                 PL_SKLogger.writeLog("RLOS value of ReturnCode Is_CardServices",""+formObject.getNGValue("Is_CardServicesPL"));
	                                 PL_SKLogger.writeLog("","CARD SERVICES RUNNING Financial Services Request");
	                                 outputResponse = GenerateXML("CARD_NOTIFICATION","Financial Services Request");
	                                 ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
	                                 PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
	                                 if(ReturnCode.equalsIgnoreCase("0000")){
	                                     PL_SKLogger.writeLog("RLOS value of Customer_Details","value of Customer_Details inside lock code");
	                                     formObject.setNGValue("Is_CardNotifiactionPL","Y");
	                                     PL_SKLogger.writeLog("RLOS value of ReturnCode Is_CardNotifiactionPL",""+formObject.getNGValue("Is_CardNotifiactionPL"));
	                                 }
	                             }
	                         }
	                         //for another product
	                         if(Product_Name.equalsIgnoreCase("Product Upgrade Request")){
	                             outputResponse = GenerateXML("CARD_SERVICES_REQUEST","Product Upgrade Request");
	                             ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
	                             PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
	                             if(ReturnCode.equalsIgnoreCase("0000")){
	                                 PL_SKLogger.writeLog("RLOS value of Customer_Details","value of Customer_Details inside inquiry code");
	                                 formObject.setNGValue("Is_CardServicesPL","Y"); 
	                                 PL_SKLogger.writeLog("RLOS value of ReturnCode Is_CardServices",""+formObject.getNGValue("Is_CardServicesPL"));
	                                 PL_SKLogger.writeLog("","CARD SERVICES RUNNING Product Upgrade Request");
	                                 outputResponse = GenerateXML("CARD_NOTIFICATION","Product Upgrade Request");
	                                 ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
	                                 PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
	                                 if(ReturnCode.equalsIgnoreCase("0000")){
	                                     PL_SKLogger.writeLog("RLOS value of Customer_Details","value of Customer_Details inside lock code");
	                                     formObject.setNGValue("Is_CardNotifiactionPL","Y");
	                                     PL_SKLogger.writeLog("RLOS value of ReturnCode Is_CardNotifiactionPL",""+formObject.getNGValue("Is_CardNotifiactionPL"));
	                                 }
	                             }
	                         }*/

				}    
				//ended    for New Card and Notification call    

				//loan call
				else if(pEvent.getSource().getName().equalsIgnoreCase("Loan_Disbursal_Button1")){
					List<List<String>> output = null;
					String scheme="";
					String acc_brnch="";
					//added by abhishek started
					PL_SKLogger.writeLog("PL Disbursal: " ,"inside Loan Creation");
					formObject.fetchFragment("ProductContainer", "Product", "q_Product");
					int n = formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
					PL_SKLogger.writeLog("PL Disbursal: ", "Row count: "+n);
					for(int i=0;i<n;i++)
					{
						PL_SKLogger.writeLog("value of requested type is"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 1) ,"");
						formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 1);
						if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 1).equalsIgnoreCase("PL") || formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 1).equalsIgnoreCase("Personal Loan")){
							scheme = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 7);
							break;                                                          
						}
					}
					if(!scheme.equalsIgnoreCase("") && !scheme.equalsIgnoreCase("null")){
						try{
							String Query = "select S.SCHEMEID,S.SCHEMEDESC,S.LSM_PRODDIFFRATE,P.Code , P.grace_days,P.description,S.PRIME_TYPE from ng_MASTER_Scheme S join ng_master_product_parameter P on S.PRODUCTFLAG = P.CODE and S.SCHEMEDESC = '"+scheme+"'";
							PL_SKLogger.writeLog("Query to fetch from ng_master_product_parameter"+Query ,"");
							output = formObject.getDataFromDataSource(Query);
							PL_SKLogger.writeLog("value ofscheme is"+scheme ,"");
							if(output!=null && !output.isEmpty()){
								formObject.setNGValue("scheme_code","CNP2");
								formObject.setNGValue("Scheme_desc","PL");
								formObject.setNGValue("cmplx_LoanDisb_prod_diff_rate",output.get(0).get(2) );
								formObject.setNGValue("cmplx_LoanDisb_product_code",output.get(0).get(3) );
								formObject.setNGValue("cmplx_LoanDisb_gracedays", output.get(0).get(4));
								formObject.setNGValue("cmplx_LoanDisb_product_desc",output.get(0).get(5) );
								formObject.setNGValue("cmplx_LoanDetails_basetype",output.get(0).get(6) );

								if(formObject.isVisible("DecisionHistory_Frame1")==false){
									PL_SKLogger.writeLog("PL" ,"Inside IsVisible check for Decision History!!");
									formObject.fetchFragment("DecisionHistory", "DecisionHistory", "q_Decision");
								}

								String acc_no = formObject.getNGValue("cmplx_Decision_AccountNo");
								PL_SKLogger.writeLog("cmplx_Decision_AccountNo: "+acc_no ,"");
								if(acc_no.equalsIgnoreCase("") || acc_no == null ){

									String qry = "select AcctId,AcctType from ng_RLOS_CUSTEXPOSE_AcctDetails where AcctType like '%CURRENT ACCOUNT%' and ( Child_Wi = '"+formObject.getWFWorkitemName()+"' or Wi_Name ='"+formObject.getWFWorkitemName()+"')";
									PL_SKLogger.writeLog("query is"+qry ,"");
									List<List<String>> record =null; 
									record=formObject.getDataFromDataSource(qry);
									PL_SKLogger.writeLog("record is: "+record ,"");

									if(record!=null && record.size()>0 &&  record.get(0)!=null ){
										acc_no = record.get(0).get(0);
										//started By Akshay
										if(record.get(0).get(1).equalsIgnoreCase("RAKislamic CURRENT ACCOUNT")){
											acc_brnch=acc_no.substring(0,3);

										}
										else{
											acc_brnch=acc_no.substring(0,3);//acc_brnch=acc_no.substring(1,4);//Changed by Rachit on 30/07/2017
										}
										//ended By Akshay
										PL_SKLogger.writeLog("value of accno is"+acc_no ,"");
										//formObject.setNGValue("Account_Number", record.get(0).get(0));
									}

									else{
										alert_msg="Loan can't be created as No current account is maintained for this Customer";
										//throw new ValidatorException(new FacesMessage(alert_msg));
									}	
								}
								//added By Akshay-21/6/2017	
								else{
									acc_brnch=acc_no.substring(0,3);
								}
								//ended By Akshay-21/6/2017
								if(acc_no!=null && !acc_no.equalsIgnoreCase("")){
									formObject.setNGValue("Account_Number", acc_no);//added By Akshay
									formObject.setNGValue("acct_brnch",acc_brnch);
									formObject.setNGValue("Loan_Disbursal_SourcingBranch",acc_no.substring(0,4));  
								}



								outputResponse = GenerateXML("NEW_LOAN_REQ","");
								ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
								PL_SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
								String ContractID =  (outputResponse.contains("<contractID>")) ? outputResponse.substring(outputResponse.indexOf("<contractID>")+"</contractID>".length()-1,outputResponse.indexOf("</contractID>")):"";    
								String StatusDescription= (outputResponse.contains("<Description>")) ? outputResponse.substring(outputResponse.indexOf("<Description>")+"</Description>".length()-1,outputResponse.indexOf("</Description>")):"";
								String returnCode= (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
								String returnDesc= (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";
								String StatusCode= (outputResponse.contains("<StatusCode>")) ? outputResponse.substring(outputResponse.indexOf("<StatusCode>")+"</StatusCode>".length()-1,outputResponse.indexOf("</StatusCode>")):"";

								PL_SKLogger.writeLog("RLOS value of ContractID",ContractID);
								if(ReturnCode.equalsIgnoreCase("0000")){
									PL_SKLogger.writeLog("RLOS value of Loan Request","value of Loan_Req inside");
									//valueSetCustomer(outputResponse);  
									formObject.setNGValue("Is_LoanReq","Y");
									formObject.setNGValue("cmplx_LoanDisb_AgreementID",ContractID);
									formObject.setNGValue("cmplx_LoanDisb_IbsRetrunCode",returnCode);
									formObject.setNGValue("cmplx_LoanDisb_IbsStatus",StatusCode);
									formObject.setNGValue("cmplx_LoanDisb_IbsReason",StatusDescription);
									formObject.setNGValue("cmplx_LoanDisb_IbsReturnHeaderDesc",returnDesc);

									alert_msg = "Contract created sucessfully with contract id: "+ContractID;
									PL_SKLogger.writeLog("PL Disbursal ", "message need to be displayed: "+alert_msg);
								} 
								else{
									alert_msg = "Error in Contract creation operation, kindly try after some time or contact administrator.";
									PL_SKLogger.writeLog("PL Disbursal: ", "Error while generating new loan: "+ ReturnCode);
								}
							}
							else{
								alert_msg = "Error in Contract creation operation, kindly try after some time or contact administrator.";
								PL_SKLogger.writeLog("PL Disbursal ","ng_master_product_parameter is not miantained for scheme: "+scheme );
							}
							formObject.RaiseEvent("WFSave");
							throw new ValidatorException(new FacesMessage(alert_msg));

						}
						catch(Exception e){
							popupFlag = "Y";
							if(e instanceof ValidatorException){
								PL_SKLogger.writeLog("PL Disbursal: ", "Inside instance of validator exception");
								throw new ValidatorException(new FacesMessage(alert_msg));
							}
							else{
								alert_msg = "Error in Contract creation operation, kindly try after some time or contact administrator.";
								PL_SKLogger.writeLog("PL Disbursal: ", "Exception occured while generating new loan: "+ e.toString()+" stack trace: "+printException(e));
								throw new ValidatorException(new FacesMessage(alert_msg));
							} 

						}                                            		
					}
					else{
						PL_SKLogger.writeLog("PL Disbursal ","Scheme code is not maintained for this case");
					}
				}  

				//ended code merged

			case VALUE_CHANGED:
				PL_SKLogger.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
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

		finally{System.out.print("OK");}
		/*	  catch(Exception ex)
			{
				 PL_SKLogger.writeLog("PL Disbursal","Inside Exception to show msg at front end");
				 HashMap<String,String> hm1=new HashMap<String,String>();
					hm1.put("Error","Checked");
				 if(ex instanceof ValidatorException)
					{   PL_SKLogger.writeLog("PL Disbursal","popupFlag value: "+ popupFlag);
						if(popupFlag.equalsIgnoreCase("Y"))
						{
							PL_SKLogger.writeLog("PL Disbursal","Inside popup msg through Exception "+ popupFlag);
							if(popUpControl.equals(""))
							{
								PL_SKLogger.writeLog("PL Disbursal","Before show Exception at front End "+ popupFlag);
								throw new ValidatorException(new FacesMessage(alert_msg));
								//try{ throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm1));}finally{hm1.clear();}
							}else
							{
								throw new ValidatorException(new FacesMessage(popUpMsg,popUpControl));

							}

						}
						else{

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

		saveIndecisionGrid();
	}

}

