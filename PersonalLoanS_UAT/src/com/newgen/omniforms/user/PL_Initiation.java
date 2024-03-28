/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PL_Initiation.java
Author                                                                        : Disha
Date (DD/MM/YYYY)                                      						  : 
Description                                                                   : 

------------------------------------------------------------------------------------------------------------------------------------------------------
CHANGE HISTORY 
------------------------------------------------------------------------------------------------------------------------------------------------------

Problem No/CR No   Change Date   Changed By    Change Description

------------------------------------------------------------------------------------------------------*/
package com.newgen.omniforms.user;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.excp.CustomExceptionHandler;
import com.newgen.omniforms.listener.FormListener;
import com.newgen.omniforms.util.PL_SKLogger;

public class PL_Initiation extends PLCommon implements FormListener{

	HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
	boolean IsFragLoaded=false;
	String ReqProd=null;
	String queryData_load="";
	  FormReference formObject = null;
	public void continueExecution(String arg0, HashMap<String, String> arg1) {
		// TODO Auto-generated method stub	
	}

	public void formLoaded(FormEvent pEvent) {
		 PL_SKLogger.writeLog("PersonnalLoanS> PL_Iniation", "Inside PL PROCESS formLoaded()" + pEvent.getSource());
		  formObject =FormContext.getCurrentInstance().getFormReference();

	}


	public void formPopulated(FormEvent pEvent) {
		  formObject =FormContext.getCurrentInstance().getFormReference();

		PL_SKLogger.writeLog("PersonnalLoanS>  PL_Iniation", "Inside PL PROCESS formPopulated()" + pEvent.getSource());
			 formObject.setNGValue("Text7", formObject.getWFWorkitemName());
			 formObject.setNGValue("Text1", formObject.getWFActivityName());
			 
			 formObject.setNGValue("WiLabel",formObject.getWFWorkitemName());
        	 formObject.setNGValue("QueueLabel","PL_Initiation");
        	 formObject.setNGValue("User_Name",formObject.getUserName()); 
	         queryData_load=formObject.getNGValue("get_parent_data");
			 PL_SKLogger.writeLog("Inside PL_Iniation ","Value of querydata_load:"+queryData_load);
			 partMatchValues();
	}

	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException {
		// TODO Auto-generated method stub
		HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
		String popupFlag="N";
		String popUpMsg="";
		String popUpControl="";
		PL_SKLogger.writeLog("Inside PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
	  formObject =FormContext.getCurrentInstance().getFormReference();

      switch(pEvent.getType()) {

          case FRAME_EXPANDED:
				PL_SKLogger.writeLog(" In PL_Iniation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
				new PersonalLoanSCommonCode().FrameExpandEvent(pEvent);
                 break;
                
          case FRAGMENT_LOADED:
        	  PL_SKLogger.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			 	
        	  if (pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails")) {
					LoadPickList("title", "select '--Select--' union select convert(varchar, description) from NG_MASTER_title with (nolock)");
					LoadPickList("gender", "select '--Select--' union select convert(varchar, description) from NG_MASTER_gender with (nolock)");
					LoadPickList("nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
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
				
				if (pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails")) {
	               formObject.setLocked("cif", true);
	               formObject.setLocked("CompanyDetails_Button3", true);
					LoadPickList("appType", "select '--Select--' union select convert(varchar, description) from NG_MASTER_ApplicantType with (nolock)");
	                LoadPickList("indusSector", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
	                LoadPickList("indusMAcro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
	                LoadPickList("indusMicro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
	                LoadPickList("legalEntity", "select '--Select--' union select convert(varchar, description) from NG_MASTER_LegalEntity with (nolock)");
	                LoadPickList("desig", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Designation with (nolock)");
	                LoadPickList("desigVisa", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Designation with (nolock)");
	                LoadPickList("EOW", "select '--Select--' union select convert(varchar, description) from NG_MASTER_State with (nolock)");
	                LoadPickList("headOffice", "select '--Select--' union select convert(varchar, description) from NG_MASTER_State with (nolock)");
	            }
				
	            if (pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails")) {
	            	 formObject.setLocked("CIFNo", true);
		             formObject.setLocked("AuthorisedSignDetails_Button4", true);
	            	LoadPickList("nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
	                LoadPickList("SignStatus", "select '--Select--' union select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");
	            }
	            
	            if (pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails")) {
	                LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
	            }

	            
				if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) {
					
					formObject.setVisible("EMploymentDetails_Label25",false);
					formObject.setVisible("cmplx_EmploymentDetails_NepType",false);
					formObject.setVisible("cmplx_EmploymentDetails_Freezone",false);
					formObject.setVisible("EMploymentDetails_Label62",false);
					formObject.setVisible("cmplx_EmploymentDetails_FreezoneName",false);
					formObject.setVisible("cmplx_EmploymentDetails_tenancntrct",false);
					formObject.setVisible("EMploymentDetails_Label5",false);
					formObject.setVisible("cmplx_EmploymentDetails_IndusSeg",false);
					formObject.setVisible("EMploymentDetails_Label59",false);
					formObject.setVisible("cmplx_EmploymentDetails_channelcode",false);
					formObject.setVisible("EMploymentDetails_Label36",false);
					formObject.setLocked("cmplx_EmploymentDetails_EmpName",true);
					formObject.setLocked("cmplx_EmploymentDetails_EMpCode",true);
					formObject.setLocked("cmplx_EmploymentDetails_LOS",true);
					formObject.setLocked("cmplx_EmploymentDetails_LOSPrevious",true);
					loadPicklist4();
				}
				
				if (pEvent.getSource().getName().equalsIgnoreCase("MiscellaneousFields")) {
					formObject.setLocked("cmplx_MiscFields_School",true);
					formObject.setLocked("cmplx_MiscFields_PropertyType",true);
					formObject.setLocked("cmplx_MiscFields_RealEstate",true);
					formObject.setLocked("cmplx_MiscFields_FarmEmirate",true);
				}
				
				
				if (pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo")) {
					
					 LoadPickList("cmplx_EligibilityAndProductInfo_RepayFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_frequency with (nolock)");
					 LoadPickList("cmplx_EligibilityAndProductInfo_instrumenttype", "select '--Select--' union select convert(varchar, description) from NG_MASTER_instrumentType");
					 LoadPickList("cmplx_EligibilityAndProductInfo_InterestType", "select '--Select--' union select convert(varchar, description) from NG_MASTER_InterestType");
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
					 LoadPickList("nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
		             LoadPickList("gender", "select '--Select--' union select convert(varchar, description) from NG_MASTER_gender with (nolock)");
					 LoadPickList("ResdCountry", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
					LoadPickList("relationship", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Relationship with (nolock)");

				}
				if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")) {
					LoadPickList("cmplx_FATCA_Category", "select '--Select--' union select convert(varchar, description) from NG_MASTER_category with (nolock)");
				}
					
				if (pEvent.getSource().getName().equalsIgnoreCase("OECD")) {
					 LoadPickList("OECD_CountryBirth", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
		                LoadPickList("OECD_townBirth", "select '--Select--' union select convert(varchar, description) from NG_MASTER_city with (nolock)");
						 LoadPickList("OECD_CountryTaxResidence", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
				}
				
					if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
	                	loadPicklist1();
				 } 	
			
			
			  break;
			  
			case MOUSE_CLICKED:
				PL_SKLogger.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
				if (pEvent.getSource().getName().equalsIgnoreCase("Customer_ReadFromCard")){
					//GenerateXML();
				}
				
				
				if (pEvent.getSource().getName().equalsIgnoreCase("Add")){
					
					formObject.setNGValue("Grid_wi_name",formObject.getWFWorkitemName());
					PL_SKLogger.writeLog("RLOS", "Inside add button: "+formObject.getNGValue("Grid_wi_name"));
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
				
				
				if (pEvent.getSource().getName().equalsIgnoreCase("Modify")){
					
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
				
				if (pEvent.getSource().getName().equalsIgnoreCase("Delete")){
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
				
				if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_addr_Add")){
					formObject.setNGValue("Address_wi_name",formObject.getWFWorkitemName());
					PL_SKLogger.writeLog("PL", "Inside add button: "+formObject.getNGValue("Address_wi_name"));
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_AddressDetails_cmplx_AddressGrid");
				}
				
				if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_addr_Modify")){
					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_AddressDetails_cmplx_AddressGrid");
				}
				
				if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_addr_Delete")){
					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_AddressDetails_cmplx_AddressGrid");

				}

			
				if(pEvent.getSource().getName().equalsIgnoreCase("Customer_save")){
					PL_SKLogger.writeLog("PL_Initiation", "Inside Customer_save button: ");
					formObject.saveFragment("CustomerDetails");
				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("Product_Save")){
					formObject.saveFragment("ProductContainer");
				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails_Save")){
					formObject.saveFragment("GuarantorDetails");
				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_Salaried_Save")){
					formObject.saveFragment("IncomeDEtails");
				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_SelfEmployed_Save")){
					formObject.saveFragment("Incomedetails");
				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails_Save")){
					formObject.saveFragment("CompanyDetails");
				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails_Save")){
					formObject.saveFragment("PartnerDetails");
				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("SelfEmployed_Save")){
					formObject.saveFragment("Liability_container");
				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("Liability_New_Save")){
					formObject.saveFragment("InternalExternalContainer");
				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("EmpDetails_Save")){
					formObject.saveFragment("EmploymentDetails");
				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo_Save")){
					formObject.saveFragment("EligibilityAndProductInformation");
				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("MiscellaneousFields_Save")){
					formObject.saveFragment("MiscFields");
				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_Save")){
					formObject.saveFragment("Address_Details_container");
				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("14.AltContactDetails_ContactDetails_Save")){
					formObject.saveFragment("Alt_Contact_container");
				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("CardDetails_save")){
					formObject.saveFragment("Supplementary_Container");
				}
				
				
				if(pEvent.getSource().getName().equalsIgnoreCase("FATCA_Save")){
					formObject.saveFragment("FATCA");
				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("KYC_Save")){
					formObject.saveFragment("KYC");
				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("OECD_Save")){
					formObject.saveFragment("OECD");
				}
				
				
				if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Save")){
					formObject.saveFragment("DecisionHistoryContainer");
				}
		
			
			 case VALUE_CHANGED:
					PL_SKLogger.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
					
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
					
					
					if (pEvent.getSource().getName().equalsIgnoreCase("Decision_Combo2")) {
						 if(formObject.getWFActivityName().equalsIgnoreCase("CAD_Analyst1"))	
						 {
							 formObject.setNGValue("CAD_dec", formObject.getNGValue("Decision_Combo2"));
							PL_SKLogger.writeLog(" In PL_Initiation VALChanged---New Value of CAD_dec is: ", formObject.getNGValue("Decision_Combo2"));

						 }
						 
						 else{
							 
							formObject.setNGValue("decision", formObject.getNGValue("Decision_Combo2"));
							PL_SKLogger.writeLog(" In PL_Initiation VALChanged---New Value of decision is: ", formObject.getNGValue("Decision_Combo2"));
						 	  }
					 	}
          
          default: break;
	     
	     }

	}	
	
	
	public void initialize() {
		PL_SKLogger.writeLog("PersonnalLoanS>  PL_Iniation", "Inside PL PROCESS initialize()" );
		  formObject =FormContext.getCurrentInstance().getFormReference();

	}

	
	public void saveFormCompleted(FormEvent pEvent) throws ValidatorException {
		PL_SKLogger.writeLog("PersonnalLoanS>  PL_Iniation", "Inside PL PROCESS saveFormCompleted()" + pEvent.getSource());
		  formObject =FormContext.getCurrentInstance().getFormReference();

	}

	
	public void saveFormStarted(FormEvent pEvent) throws ValidatorException {
		  formObject =FormContext.getCurrentInstance().getFormReference();
		PL_SKLogger.writeLog("PersonnalLoanS>  PL_Iniation", "Inside PL PROCESS saveFormStarted()" + pEvent.getSource());
		formObject.setNGValue("get_parent_data",queryData_load);
		PL_SKLogger.writeLog("PersonnalLoanS","Final val of queryData_load:"+ formObject.getNGValue("get_parent_data"));
	}

	
	public void submitFormCompleted(FormEvent pEvent) throws ValidatorException {
		PL_SKLogger.writeLog("PersonnalLoanS>  PL_Iniation", "Inside PL PROCESS submitFormCompleted()" + pEvent.getSource());
		
	}

	
	public void submitFormStarted(FormEvent pEvent) throws ValidatorException {
		PL_SKLogger.writeLog("PersonnalLoanS>  PL_Iniation", "Inside PL PROCESS submitFormStarted()" + pEvent.getSource());
		saveIndecisionGrid();
	}

}
