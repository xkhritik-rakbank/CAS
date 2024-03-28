
/*------------------------------------------------------------------------------------------------------

                     NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                             : Application -Projects

Project/Product                                                   : Rakbank  

Application                                                       : Credit Card

Module                                                            : CSM

File Name                                                         : CC_CSM

Author                                                            : Disha 

Date (DD/MM/YYYY)                                                 : 

Description                                                       : 

-------------------------------------------------------------------------------------------------------

CHANGE HISTORY

-------------------------------------------------------------------------------------------------------

Reference No/CR No   Change Date   Changed By    Change Description
1. 					12-6-2016	 Disha		   Code commented saveIndecisionGrid() for continueExecution 
1004				17-9-2017	Saurabh			Disabling Liability fields in Liability fragment based on emp type.	
------------------------------------------------------------------------------------------------------*/
package com.newgen.omniforms.user;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.excp.CustomExceptionHandler;
import com.newgen.omniforms.util.SKLogger_CC;

public class CC_CommonCode extends CC_Common {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void FrameExpandEvent(ComponentEvent pEvent){
		try{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
		String activityName = formObject.getWFActivityName();

		SKLogger_CC.writeLog("Credit Card","Inside CC_CommonCode-->FrameExpandEvent()");
		if (pEvent.getSource().getName().equalsIgnoreCase("CustomerDetails")) {
			
			hm.put("CustomerDetails","Clicked");
			formObject.fetchFragment("CustomerDetails", "Customer", "q_Customer");
			loadPicklistCustomer();
			
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
			
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("ProductContainer")) {
			hm.put("ProductContainer","Clicked");
			formObject.fetchFragment("ProductContainer", "Product", "q_product");
			// ++ below code already present - 06-10-2017
			hide_sheet_employee();
			hide_sheet_company();
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
			LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
			LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		
		if (pEvent.getSource().getName().equalsIgnoreCase("EmploymentDetails")) {
			
			hm.put("EmploymentDetails","Clicked");
			formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmpDetails");
			// ++ below code already present - 06-10-2017
			employment_fields_hide();
			employment_fields_IM();
			//loadPicklist_Employment();
			loadPicklist4();
			if(!formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("SAL")){
				formObject.setVisible("EMploymentDetails_Label59", false);
				formObject.setVisible("cmplx_EmploymentDetails_IndusSeg", false);
			}
			else{
				formObject.setVisible("EMploymentDetails_Label59", true);
				formObject.setVisible("cmplx_EmploymentDetails_IndusSeg", true);
			}
			if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1).equalsIgnoreCase("Credit Card")){
				formObject.setVisible("EMploymentDetails_Label71", false);
				formObject.setVisible("cmplx_EmploymentDetails_EmpContractType", false);
			}
			else{
				formObject.setVisible("EMploymentDetails_Label71", true);
				formObject.setVisible("cmplx_EmploymentDetails_EmpContractType", true);
			}
			lockALOCfields();
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}	
		
		if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDEtails")) 
		{
			hm.put("Incomedetails","Clicked");
			formObject.fetchFragment("IncomeDEtails", "IncomeDetails", "q_IncomeDetails");
			
			//added 9/08/2017
			String subprod =formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2);
			String empType = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6);
			SKLogger_CC.writeLog("CC", "subprod"+subprod+",emptype:"+empType);
			if(subprod.equalsIgnoreCase("Business titanium Card")&& empType.equalsIgnoreCase("Self Employed")){
				SKLogger_CC.writeLog("CC", "inside if condition");
				formObject.setEnabled("IncomeDEtails", false);
			}
			
			//if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6).equalsIgnoreCase("Salaried")){
			if(activityName.equalsIgnoreCase("DDVT_Maker")){	
			formObject.setEnabled("IncomeDEtails",false);
			}
			//}
			//added 9/08/2017
			if(formObject.getNGValue("cmplx_IncomeDetails_Accomodation").equalsIgnoreCase("No")){
				formObject.setLocked("cmplx_IncomeDetails_CompanyAcc",true);
			}
			
			int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			for(int i=0;i<n;i++){
				SKLogger_CC.writeLog("CC", "Grid Data[1][9] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9));
				if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1).equalsIgnoreCase("Credit Card") && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")){
					SKLogger_CC.writeLog("CC @AKSHAY", "Grid Data[1][9] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9));
					formObject.setVisible("IncomeDetails_Label12", true);
					formObject.setVisible("cmplx_IncomeDetails_StatementCycle", true);	
					formObject.setVisible("IncomeDetails_Label14", true);
					formObject.setVisible("cmplx_IncomeDetails_StatementCycle2", true);	
				}	
				else{
					formObject.setVisible("IncomeDetails_Label12", false);
					formObject.setVisible("cmplx_IncomeDetails_StatementCycle", false);	
					formObject.setVisible("IncomeDetails_Label14", false);
					formObject.setVisible("cmplx_IncomeDetails_StatementCycle2", false);			
				}
			}
			
			if(n>0)
			{					
				String EmpType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6);
				SKLogger_CC.writeLog("CC", "Emp Type Value is:"+EmpType);

				if(EmpType.equalsIgnoreCase("Salaried")|| EmpType.equalsIgnoreCase("Salaried Pensioner"))
				{
					formObject.setVisible("IncomeDetails_Frame3", false);
					formObject.setHeight("Incomedetails", 640);
					formObject.setHeight("IncomeDetails_Frame1", 615);	
					if(formObject.getNGValue("cmplx_Customer_NTB")=="true"){
						formObject.setVisible("IncomeDetails_Label11", false);
						formObject.setVisible("cmplx_IncomeDetails_DurationOfBanking", false);
						formObject.setVisible("IncomeDetails_Label13", false);
						formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsRakbankStat", false);
						formObject.setVisible("IncomeDetails_Label3", false);
						formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsOtherbankStat", false);
					}	
					else{
						formObject.setVisible("IncomeDetails_Label11", true);
						formObject.setVisible("cmplx_IncomeDetails_DurationOfBanking", true);
						formObject.setVisible("IncomeDetails_Label13", true);
						formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsRakbankStat", true);
						formObject.setVisible("IncomeDetails_Label3", true);
						formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsOtherbankStat", true);
					}	
				}
					
				else if(EmpType.equalsIgnoreCase("Self Employed"))
				{							
					formObject.setVisible("IncomeDetails_Frame2", false);
					formObject.setTop("IncomeDetails_Frame3",40);
					formObject.setHeight("Incomedetails", 490);
					formObject.setHeight("IncomeDetails_Frame1", 490);
					if(formObject.getNGValue("cmplx_Customer_NTB")=="true"){
						formObject.setVisible("IncomeDetails_Label20", false);
						formObject.setVisible("cmplx_IncomeDetails_DurationOfBanking2", false);
						formObject.setVisible("IncomeDetails_Label22", false);
						formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsRakbankStat2", false);
						formObject.setVisible("IncomeDetails_Label35", false);
						formObject.setVisible("IncomeDetails_Label5", false);
						formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsOtherbankStat2", false);
						formObject.setVisible("IncomeDetails_Label36", true);
					}	
					else{
						formObject.setVisible("IncomeDetails_Label20", true);
						formObject.setVisible("cmplx_IncomeDetails_DurationOfBanking2", true);
						formObject.setVisible("IncomeDetails_Label22", true);
						formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsRakbankStat2", true);
						formObject.setVisible("IncomeDetails_Label35", true);
						formObject.setVisible("IncomeDetails_Label5", true);
						formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsOtherbankStat2", true);
						formObject.setVisible("IncomeDetails_Label36", true);
					}
					LoadPickList("BankStatFrom","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_othBankCAC where isActive='Y' order by code");
				}
			}
			
			//IMFields_Income();
			//BTCFields_Income();
			//LimitIncreaseFields_Income();
			//ProductUpgrade_Income();
		
			/*try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}*/
		//	finally{hm.clear();}					
		}					
		
		if (pEvent.getSource().getName().equalsIgnoreCase("Internal_External_Liability")) {
			hm.put("Internal_External_Liability","Clicked");
			formObject.fetchFragment("Internal_External_Liability", "Liability_New", "q_Liabilities");
					String activity = formObject.getWFActivityName();
			String empType = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6);
			/*if(empType.equalsIgnoreCase("Salaried")){
				formObject.setEnabled("ExtLiability_Frame1", false);
			}*/
			//Ref. 1004
			if(empType.equalsIgnoreCase("Salaried") || empType.equalsIgnoreCase("Salaried Pensioner")){
				SKLogger_CC.writeLog("CC Common Code", "Inside Self Employed case for liabilities");
				formObject.setVisible("Liability_New_Label6",false);
				formObject.setVisible("cmplx_Liability_New_NoofotherbankAuth",false);
				formObject.setVisible("Liability_New_Label8",false);
				formObject.setVisible("cmplx_Liability_New_NoofotherbankCompany",false);
				formObject.setVisible("Liability_New_Label7",false);
				formObject.setVisible("cmplx_Liability_New_TotcleanFundedAmt",false);
				
				formObject.setVisible("ExtLiability_Label9",true);
				formObject.setVisible("cmplx_Liability_New_DBR",true);
				formObject.setVisible("ExtLiability_Label25",true);
				formObject.setVisible("cmplx_Liability_New_TAI",true);
				formObject.setVisible("ExtLiability_Label14",true);
				formObject.setVisible("cmplx_Liability_New_DBRNet",true);
			
			}

			//Deepak 09102017- JIRA 2864 to load company details in case of Self Employed while loading employment details
				else if(empType.equalsIgnoreCase("Self Employed")){
			        formObject.fetchFragment("CompDetails", "CompanyDetails", "q_CompanyDetails");
			        formObject.fetchFragment("Auth_Sign_Details", "AuthorisedSignDetails", "q_AuthorisedSignDetails");
			        formObject.setTop("Auth_Sign_Details", formObject.getTop("CompDetails")+formObject.getHeight("CompDetails")+25);
					formObject.setTop("Partner_Details", formObject.getTop("Auth_Sign_Details")+formObject.getHeight("Auth_Sign_Details")+25);
					SKLogger_CC.writeLog("CC Common Code", "Inside Salaried case for liabilities");
					formObject.setVisible("Liability_New_Label6",true);
					formObject.setVisible("cmplx_Liability_New_NoofotherbankAuth",true);
					formObject.setVisible("Liability_New_Label8",true);
					formObject.setVisible("cmplx_Liability_New_NoofotherbankCompany",true);
					formObject.setVisible("Liability_New_Label7",true);
					formObject.setVisible("cmplx_Liability_New_TotcleanFundedAmt",true);
					formObject.setVisible("ExtLiability_Label9",false);
					formObject.setVisible("cmplx_Liability_New_DBR",false);
					formObject.setVisible("ExtLiability_Label25",false);
					formObject.setVisible("cmplx_Liability_New_TAI",false);
					formObject.setVisible("ExtLiability_Label14",false);
					formObject.setVisible("cmplx_Liability_New_DBRNet",false);
			   }
			else{
				SKLogger_CC.writeLog("CC Common Code", "Inside Salaried case for liabilities");
				formObject.setVisible("Liability_New_Label6",true);
				formObject.setVisible("cmplx_Liability_New_NoofotherbankAuth",true);
				formObject.setVisible("Liability_New_Label8",true);
				formObject.setVisible("cmplx_Liability_New_NoofotherbankCompany",true);
				formObject.setVisible("Liability_New_Label7",true);
				formObject.setVisible("cmplx_Liability_New_TotcleanFundedAmt",true);
				formObject.setVisible("ExtLiability_Label9",false);
				formObject.setVisible("cmplx_Liability_New_DBR",false);
				formObject.setVisible("ExtLiability_Label25",false);
				formObject.setVisible("cmplx_Liability_New_TAI",false);
				formObject.setVisible("ExtLiability_Label14",false);
				formObject.setVisible("cmplx_Liability_New_DBRNet",false);
				}
			
			formObject.setLocked("cmplx_Liability_New_AggrExposure",true);
			formObject.setLocked("cmplx_Liability_New_DBR",true);
			formObject.setLocked("cmplx_Liability_New_TAI",true);
			formObject.setLocked("cmplx_Liability_New_DBRNet",true);
			//Ref. 1004 end.
			
			LoadPickList("Liability_New_worstStatusInLast24", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from ng_master_Aecb_Codes with (nolock) order by code");
			if(activity.equalsIgnoreCase("CSM")){
				//++ Below Code already exists for : "17-CSM-Liability addition-" Delinquent and DPD fields which are present in the grid are missing in the liability addition fragment" : Reported By Shashank on Oct 05, 2017++
				//hideLiabilityAddFields(formObject);
				//++ Above Code already exists for : "17-CSM-Liability addition-" Delinquent and DPD fields which are present in the grid are missing in the liability addition fragment" : Reported By Shashank on Oct 05, 2017++
			}
			if(activityName.equalsIgnoreCase("DDVT_Maker")){
			formObject.setLocked("ExtLiability_Frame1", true);
			}
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}	
		
		
		if (pEvent.getSource().getName().equalsIgnoreCase("MiscFields")) {
			hm.put("MiscFields","Clicked");
			formObject.fetchFragment("MiscFields", "MiscellaneousFields", "q_MiscFields");
			
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
			
		}
//added by yash for CC FSD
		if (pEvent.getSource().getName().equalsIgnoreCase("EligibilityAndProductInformation")) {
			hm.put("EligibilityAndProductInformation","Clicked");
			// ++ below code already present - 06-10-2017 name not change from q_EligProd to q_EligAndProductInfo as in onsite thw Queue variable name is q_EligProd 
			formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligProd");
			//added by abhishek for alignment as per CC FSD
			if(activityName.equalsIgnoreCase("DDVT_Maker")){
				formObject.setTop("ELigibiltyAndProductInfo_Frame5", 5);
				formObject.setTop("ELigibiltyAndProductInfo_Frame6", 30);
				formObject.setTop("ELigibiltyAndProductInfo_Label3",60);
				formObject.setLeft("ELigibiltyAndProductInfo_Label3",16);
				formObject.setTop("cmplx_EligibilityAndProductInfo_FinalDBR", formObject.getTop("ELigibiltyAndProductInfo_Label3")+formObject.getHeight("ELigibiltyAndProductInfo_Label3"));
				formObject.setLeft("cmplx_EligibilityAndProductInfo_FinalDBR",16);
				formObject.setTop("ELigibiltyAndProductInfo_Label4", 60);
				formObject.setLeft("ELigibiltyAndProductInfo_Label4",281);
				formObject.setTop("cmplx_EligibilityAndProductInfo_FinalTai", formObject.getTop("ELigibiltyAndProductInfo_Label4")+formObject.getHeight("ELigibiltyAndProductInfo_Label4"));
				formObject.setLeft("cmplx_EligibilityAndProductInfo_FinalTai",281);
				formObject.setTop("ELigibiltyAndProductInfo_Label5", 60);
				formObject.setLeft("ELigibiltyAndProductInfo_Label5",554);
				formObject.setTop("cmplx_EligibilityAndProductInfo_FinalLimit", formObject.getTop("ELigibiltyAndProductInfo_Label5")+formObject.getHeight("ELigibiltyAndProductInfo_Label5"));
				formObject.setLeft("cmplx_EligibilityAndProductInfo_FinalLimit",554);
				formObject.setTop("ELigibiltyAndProductInfo_Button1",120);
				formObject.setLeft("ELigibiltyAndProductInfo_Button1", 16);
				formObject.setTop("ELigibiltyAndProductInfo_Save",120);
				formObject.setLeft("ELigibiltyAndProductInfo_Save",225);
				formObject.setHeight("ELigibiltyAndProductInfo_Frame1",650);
				formObject.setHeight("EligibilityAndProductInformation", 655);
			}
				//added 09/08/2017 to make eligibility disable
			  SKLogger_CC.writeLog("RLOS Initiation Inside ","credit card");
			if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1).equalsIgnoreCase("Credit Card")){
				SKLogger_CC.writeLog("RLOS Initiation Inside ","credit card inside if");
				//formObject.setEnabled("EligibilityAndProductInformation", false);
			}
			
			//++ Below Code added By Yash on Oct 14, 2017  to fix : 15-"Following fields will be enabled for the Credit analyst user " : Reported By Shashank on Oct 09, 2017++

			if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6).equalsIgnoreCase("Salaried")){
				
				//formObject.setLocked("ELigibiltyAndProductInfo_Frame1", true);
				formObject.setEnabled("cmplx_EligibilityAndProductInfo_FinalDBR", false);
				formObject.setEnabled("cmplx_EligibilityAndProductInfo_FinalTai", false);
				formObject.setEnabled("cmplx_EligibilityAndProductInfo_FinalLimit", false);
				formObject.setLocked("ELigibiltyAndProductInfo_Save", false);//added by akshay on 13/10/17
			}
			if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6).equalsIgnoreCase("Salaried") && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("Instant Money")){
				
				//formObject.setLocked("ELigibiltyAndProductInfo_Frame1", true);
				
			}
			
			//++ Above Code added By Yash on Oct 14, 2017  to fix : 15-"Following fields will be enabled for the Credit analyst user " : Reported By Shashank on Oct 09, 2017++

			//++ Below Code added By Yash on Oct 9, 2017  to fix : 24-"Save button should be enabled " : Reported By Shashank on Oct 09, 2017++
if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6).equalsIgnoreCase("Self Employed")){
				
	formObject.setLocked("ELigibiltyAndProductInfo_Save",false);	
			}
//++ Above Code added By Yash on Oct 9, 2017  to fix : 24-"Save button should be enabled " : Reported By Shashank on Oct 09, 2017++
			//added 09/08/2017 to make eligibility disable
			
			String subProd = formObject.getNGValue("Sub_Product");
			SKLogger_CC.writeLog("value of sub prod is ",subProd);
			if(!subProd.equalsIgnoreCase("SEC")){
				SKLogger_CC.writeLog("value of sub prod is ","inside secured card");
				formObject.setVisible("ELigibiltyAndProductInfo_Frame3", false);
			}
			Eligibilityfields();
			
			//String activityName = formObject.getWFActivityName();
			SKLogger_CC.writeLog("aman activity:",activityName);
			if(activityName.equalsIgnoreCase("CAD_Analyst1")){
				SKLogger_CC.writeLog("aman activity1:",activityName);
				formObject.setEnabled("ELigibiltyAndProductInfo_Button1",true);
	 		}
			LoadPickList("cmplx_EligibilityAndProductInfo_RepayFreq", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_frequency with (nolock) order by code");
			LoadPickList("cmplx_EligibilityAndProductInfo_InterestType", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_InterestType with (nolock) order by code"); //Arun (17/10)
			
			/*try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}*/
		}	
		
		if (pEvent.getSource().getName().equalsIgnoreCase("Address_Details_container")) {
			hm.put("Address_Details_container","Clicked");
			formObject.fetchFragment("Address_Details_container", "AddressDetails", "q_AddressDetails");
			loadPicklist_Address();
			
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}	
		
		
		if (pEvent.getSource().getName().equalsIgnoreCase("Alt_Contact_container")) {
			hm.put("Alt_Contact_container","Clicked");
			formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
			LoadPickList("AlternateContactDetails_CustdomBranch", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock) order by code");
			//p2-49,Retain account if loan rejected tickbox should not apply for cards 
			//++ Below Code added By on Oct 6, 2017  to fix : "38-Retain account if loan rejected should not be there" : Reported By Shashank on Oct 05, 2017++
			SKLogger_CC.writeLog("RLOS count of current account not NTB","before invisibility");
			if(activityName.equalsIgnoreCase("DDVT_maker")){
			
			 formObject.setVisible("AlternateContactDetails_RetainaccifLoanreq_cont", false);
			}
			SKLogger_CC.writeLog("RLOS count of current account not NTB","after invisibility");
			//++ Above Code added By on Oct 6, 2017  to fix : "38-Retain account if loan rejected should not be there" : Reported By Shashank on Oct 05, 2017++
			//p2-49,Retain account if loan rejected tickbox should not apply for cards 
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		
		if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")) 
		{
			hm.put("FATCA","Clicked");
			formObject.fetchFragment("FATCA", "FATCA", "q_FATCA");
			loadPicklist_Fatca();
			LoadPickList("cmplx_FATCA_USRelation", " select '--Select--' as description, '' as code  union select convert(varchar,description),code  from ng_master_usrelation order by code");
			if(!activityName.equalsIgnoreCase("CAD_Analyst1")){
			String usRelation = formObject.getNGValue("cmplx_FATCA_USRelation");
				if(usRelation.equalsIgnoreCase("O")){
					formObject.setEnabled("FATCA_Frame6", false);
					formObject.setEnabled("cmplx_FATCA_USRelation",true);
					formObject.setEnabled("FATCA_Save", true);
				}
			}
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
			
		}	
		
		if (pEvent.getSource().getName().equalsIgnoreCase("KYC")) 
		{
			hm.put("KYC","Clicked");
			formObject.fetchFragment("KYC", "KYC", "q_KYC");
			
			
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
			
		}	
		
		if (pEvent.getSource().getName().equalsIgnoreCase("OECD")) 
		{
			hm.put("OECD","Clicked");
			formObject.fetchFragment("OECD", "OECD", "q_OECD");
			loadPicklist_oecd();
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
			
		}	
		
		if (pEvent.getSource().getName().equalsIgnoreCase("Dispatch_Details")) 
		{
			hm.put("OECD","Clicked");
			formObject.fetchFragment("Dispatch_Details", "Dispatch", "");
			loadPicklist_oecd();
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
			
		}
		
		if (pEvent.getSource().getName().equalsIgnoreCase("Self_Employed")) {
			hm.put("Self_Employed","Clicked");
			formObject.fetchFragment("Self_Employed", "SelfEmployed", "q_SelfEmployed");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		
		if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
			hm.put("DecisionHistory","Clicked");
			formObject.fetchFragment("DecisionHistory", "DecisionHistory", "q_DecisionHistory");
			//12th sept
			String cif= formObject.getNGValue("cmplx_Customer_CIFNo");
			String Emiratesid=formObject.getNGValue("cmplx_Customer_EmiratesID");
			String Custname= formObject.getNGValue("cmplx_Customer_FirstNAme")+""+formObject.getNGValue("cmplx_Customer_MiddleNAme")+""+formObject.getNGValue("cmplx_Customer_LastNAme");
			SKLogger_CC.writeLog("RLOS count of current account not NTB","Custname"+Custname);
			formObject.setNGValue("DecisionHistory_Text15",cif);
			formObject.setNGValue("DecisionHistory_Text16", Emiratesid);
			formObject.setNGValue("DecisionHistory_Text17",Custname);
			SKLogger_CC.writeLog("RLOS count of current account not NTB","inside csm ofdecision history value of cifid cust name and emirates"+ formObject.getNGValue("DecisionHistory_Text15")+","+ formObject.getNGValue("DecisionHistory_Text16")+","+ formObject.getNGValue("DecisionHistory_Text17"));
			//12th sept
			loadPicklist3();
//12th sept
			if(activityName.equalsIgnoreCase("CSM")){
				SKLogger_CC.writeLog("RLOS count of current account not NTB","inside csm ofdecision history");
				formObject.setVisible("DecisionHistory_Label10", false);
				formObject.setVisible("cmplx_DEC_New_CIFID", false);
				formObject.setVisible("DecisionHistory_Label6",false);
				formObject.setVisible("DecisionHistory_Rejreason",true);
				formObject.setVisible("cmplx_DEC_RejectReason",true);
				formObject.setVisible("DecisionHistory_Label1",false);//dec rsn
				formObject.setVisible("cmplx_DEC_ReferReason",false);//dec ren
				formObject.setTop("DecisionHistory_save",250);
				formObject.setTop("DecisionHistory_Decision_ListView1",180);
				formObject.setTop("cmplx_DEC_Decision_Reasoncode",72);//rsn code
       		 	formObject.setTop("DecisionHistory_Label11",56);//rsn code label
       		 	formObject.setTop("DecisionHistory_Label1",56);//decrsn
       		 	formObject.setTop("cmplx_DEC_ReferReason",72);//desrsn label
       		 	formObject.setTop("DecisionHistory_Decision_Label3",56);//dec
       		 	formObject.setTop("cmplx_DEC_Decision",72);//dec field
       		 	formObject.setLeft("cmplx_DEC_Decision_Reasoncode",304);//rsn code
    		 	formObject.setLeft("DecisionHistory_Label11",304);//rsn code label
    		 	formObject.setLeft("DecisionHistory_Rejreason",560);//rej resn
    		 	formObject.setLeft("cmplx_DEC_RejectReason",560);//rej rsn field
    		 	formObject.setTop("DecisionHistory_Rejreason",56);//rej resn
    		 	formObject.setTop("cmplx_DEC_RejectReason",72);
				SKLogger_CC.writeLog("RLOS count of current account not NTB","after setting csm ofdecision history");
			}
			else if (activityName.equalsIgnoreCase("Compliance")){
				formObject.setVisible("DecisionHistory_Label10", false);
				formObject.setVisible("cmplx_DEC_New_CIFID", false);
				formObject.setTop("DecisionHistory_Decision_ListView1",180); 
				formObject.setTop("DecisionHistory_save",250);
    		 	formObject.setVisible("cmplx_DEC_RejectReason",false);
    		 	formObject.setLeft("DecisionHistory_Decision_Label4",550);
    		 	formObject.setLeft("cmplx_Dec_Remarks",550);
			}
			else if (activityName.equalsIgnoreCase("FCU"))
			{
				//++Below code added by  nikhil 12/10/17 as per CC FSD 2.2
				
				formObject.setTop("DecisionHistory_save",250);
				formObject.setHeight("DecisionHistory_Frame1",446);				        			
    			formObject.setTop("DecisionHistory_Label19",10);
    			formObject.setTop("cmplx_DEC_AlocCompany",25);
    			formObject.setTop("DecisionHistory_Label20",10);
    			formObject.setTop("cmplx_DEC_AlocType",25);
    			formObject.setTop("DecisionHistory_Label21",10);
    			formObject.setTop("cmplx_DEC_ReferralReason",25);
    			formObject.setTop("DecisionHistory_Label22",10);
    			formObject.setTop("cmplx_DEC_ReferralSubReason",25);
    			formObject.setTop("DecisionHistory_Label23",60);
    			formObject.setTop("cmplx_DEC_FeedbackStatus",76);			        			
    			formObject.setTop("DecisionHistory_Label24",60);
    			formObject.setTop("cmplx_DEC_SubFeedbackStatus",76);
    			formObject.setTop("DecisionHistory_Label25",60);
    			formObject.setTop("cmplx_DEC_FurtherReview",76);
    			formObject.setLeft("DecisionHistory_Rejreason",776);
    			formObject.setLeft("cmplx_DEC_RejectReason",776);
    			formObject.setTop("DecisionHistory_Decision_Label3",60);
    			formObject.setTop("cmplx_DEC_Decision",76);
    			formObject.setTop("DecisionHistory_Rejreason",60);
    			formObject.setTop("cmplx_DEC_RejectReason",76);
    			formObject.setTop("DecisionHistory_Label25",110);
    			formObject.setTop("cmplx_DEC_FurtherReview",126);
    			formObject.setLeft("DecisionHistory_Label25",23);
    			formObject.setLeft("cmplx_DEC_FurtherReview",23);
    			formObject.setLeft("DecisionHistory_Label23",23);
    			formObject.setLeft("cmplx_DEC_FeedbackStatus",23);	
    			formObject.setLeft("DecisionHistory_Decision_Label3",528);
    			formObject.setLeft("cmplx_DEC_Decision",528);
    			formObject.setLeft("DecisionHistory_Label24",272);
    			formObject.setLeft("cmplx_DEC_SubFeedbackStatus",272);				        			
    			//formObject.setTop("DecisionHistory_save",250);
    			formObject.setTop("DecisionHistory_Decision_ListView1",280);
    			formObject.setTop("DecisionHistory_Decision_Label4", formObject.getTop("DecisionHistory_Decision_Label3"));
    			formObject.setLeft("DecisionHistory_Decision_Label4", formObject.getLeft("DecisionHistory_Label22"));
    			formObject.setTop("cmplx_DEC_Remarks", formObject.getTop("cmplx_DEC_Decision"));
    			formObject.setLeft("cmplx_DEC_Remarks", formObject.getLeft("cmplx_DEC_ReferralSubReason"));
    			formObject.setTop("DecisionHistory_save", formObject.getTop("cmplx_DEC_FurtherReview")+formObject.getHeight("cmplx_DEC_FurtherReview")+15);
    			formObject.setLeft("DecisionHistory_save", formObject.getLeft("cmplx_DEC_FurtherReview"));
    			
    			//--above code added by  nikhil 12/10/17 as per CC FSD 2.2
    			
			}
			//12th sept
			//added by yash for CC FSD
               else if (activityName.equalsIgnoreCase("Cad_Analyst2")){
				formObject.setVisible("DecisionHistory_Label10", false);
               }
               else if (activityName.equalsIgnoreCase("Cad_Analyst1")){
   				formObject.setVisible("DecisionHistory_Label10", false);
   				formObject.setVisible("cmplx_DEC_New_CIFID", false);
   				
   			//++ below code added by abhishek as per CC FSD 2.7.3
   				
				
					String sQuery = "SELECT activityname FROM WFINSTRUMENTTABLE with (nolock) WHERE ProcessInstanceID = '"+formObject.getWFWorkitemName()+"'";
					String listValues ="";
					List<List<String>> list=formObject.getDataFromDataSource(sQuery);
					SKLogger_CC.writeLog(" In CC CAD workstep decision history expand", "Done button click::query result is::"+list );
					for(int i =0;i<list.size();i++ ){
						if(i==0){
							listValues += list.get(i).get(0);
							// values.append(list.get(i).get(0));
						}else{
							listValues += "|"+list.get(i).get(0);
							 //values.append(",'" + sProcessName + "'");
						}
						
					}
					
					sQuery = "Select cpv_decision FROM NG_CC_EXTTABLE with (nolock) WHERE cc_wi_name='"+formObject.getWFWorkitemName()+"'";
					List<List<String>> list1=formObject.getDataFromDataSource(sQuery);
					SKLogger_CC.writeLog(" In CC CAD workstep decision history expand", "cpv dec value is::"+list1 );
					listValues +="#"+list1.get(0).get(0);
					SKLogger_CC.writeLog(" In CC CAD workstep decision history expand", "list value is::"+listValues );
					formObject.setNGValue("DecisionHistory_CadTempField",listValues);
					
				
				//-- Above code added by abhishek as per CC FSD 2.7.3
                  }
               else if (activityName.equalsIgnoreCase("Telesales_Agent")){
      				formObject.setVisible("DecisionHistory_Label10", false);
      				formObject.setVisible("cmplx_DEC_New_CIFID", false);
                     }
               else if (activityName.equalsIgnoreCase("DDVT_Maker")){
     				formObject.setVisible("DecisionHistory_Label10", false);
     				formObject.setVisible("cmplx_DEC_New_CIFID", false);
                    }
               else if (activityName.equalsIgnoreCase("DDVT_checker")){
            	// hide
      			 formObject.setVisible("cmplx_DEC_ContactPointVeri",false);
      			 formObject.setVisible("DecisionHistory_chqbook",false);
      			 formObject.setVisible("DecisionHistory_Text15",false);
      			 formObject.setVisible("DecisionHistory_Text16",false);
      			 formObject.setVisible("DecisionHistory_Text17",false);
      			 formObject.setVisible("cmplx_DEC_RejectReason",false);
      			 formObject.setVisible("DecisionHistory_Label8",false);
      			 formObject.setVisible("cmplx_DEC_ChequebookRef",false);
      			 formObject.setVisible("DecisionHistory_Label9",false);
      			 formObject.setVisible("cmplx_DEC_DCR_Refno",false);
      			 formObject.setVisible("DecisionHistory_Label2",false);
      			 formObject.setVisible("DecisionHistory_Label27",false);
      			 formObject.setVisible("cmplx_DEC_Cust_Contacted",false);
      			 formObject.setVisible("DecisionHistory_Combo6",false);
      			// formObject.setVisible("DecisionHistory_Label11",false); 
       			//formObject.setVisible("cmplx_DEC_Decision_Reasoncode", false);
      			 // show
      			 formObject.setVisible("DecisionHistory_Button2",true); 
			formObject.setVisible("DecisionHistory_Label10", true);
			formObject.setVisible("cmplx_DEC_New_CIFID", true);
      		// added by abhishek as per CC FSD
     			
    			 // set fields alignment
    			 formObject.setTop("DecisionHistory_Decision_Label1", 10);
				 formObject.setLeft("DecisionHistory_Decision_Label1", 25);
				 formObject.setTop("cmplx_DEC_VerificationRequired", formObject.getTop("DecisionHistory_Decision_Label1")+formObject.getHeight("DecisionHistory_Decision_Label1"));
				 formObject.setLeft("cmplx_DEC_VerificationRequired", 25);
				 formObject.setTop("DecisionHistory_Decision_Label3", 10);
				 formObject.setLeft("DecisionHistory_Decision_Label3",  formObject.getLeft("cmplx_DEC_VerificationRequired")+275);
				 formObject.setTop("cmplx_DEC_Decision", formObject.getTop("DecisionHistory_Decision_Label3")+formObject.getHeight("DecisionHistory_Decision_Label3"));
				 formObject.setLeft("cmplx_DEC_Decision",  formObject.getLeft("cmplx_DEC_VerificationRequired")+275);
				 formObject.setTop("DecisionHistory_Label1", 10);
				 formObject.setLeft("DecisionHistory_Label1", formObject.getLeft("DecisionHistory_Decision_Label3")+275);
				 formObject.setTop("cmplx_DEC_ReferReason", formObject.getTop("DecisionHistory_Label1")+formObject.getHeight("DecisionHistory_Label1"));
				 formObject.setLeft("cmplx_DEC_ReferReason",  formObject.getLeft("DecisionHistory_Decision_Label3")+275);
				 formObject.setTop("DecisionHistory_Button2", formObject.getTop("cmplx_DEC_ReferReason"));
				 formObject.setLeft("DecisionHistory_Button2", formObject.getLeft("DecisionHistory_Label1")+275);
				 formObject.setTop("DecisionHistory_Label6", formObject.getTop("DecisionHistory_Button2")+60);
				 formObject.setLeft("DecisionHistory_Label6", 25);
				 formObject.setTop("cmplx_DEC_IBAN_No", formObject.getTop("DecisionHistory_Label6")+formObject.getHeight("DecisionHistory_Label6"));
				 formObject.setLeft("cmplx_DEC_IBAN_No",  25);
				 formObject.setTop("DecisionHistory_Label10", formObject.getTop("DecisionHistory_Label6"));
				 formObject.setLeft("DecisionHistory_Label10", formObject.getLeft("DecisionHistory_Label6")+275);
				 formObject.setTop("cmplx_DEC_New_CIFID", formObject.getTop("DecisionHistory_Label10")+formObject.getHeight("DecisionHistory_Label10"));
				 formObject.setLeft("cmplx_DEC_New_CIFID",  formObject.getLeft("DecisionHistory_Label6")+275);
				 formObject.setTop("DecisionHistory_Label7", formObject.getTop("DecisionHistory_Label6"));
				 formObject.setLeft("DecisionHistory_Label7", formObject.getLeft("DecisionHistory_Label10")+275);
				 formObject.setTop("cmplx_DEC_NewAccNo", formObject.getTop("DecisionHistory_Label7")+formObject.getHeight("DecisionHistory_Label7"));
				 formObject.setLeft("cmplx_DEC_NewAccNo",  formObject.getLeft("DecisionHistory_Label10")+275);
				 formObject.setTop("DecisionHistory_Label5", formObject.getTop("DecisionHistory_Label6") + 60);
				 formObject.setLeft("DecisionHistory_Label5", 25);
				 formObject.setTop("cmplx_DEC_Description", formObject.getTop("DecisionHistory_Label5")+formObject.getHeight("DecisionHistory_Label5"));
				 formObject.setLeft("cmplx_DEC_Description", 25);
				 formObject.setTop("DecisionHistory_Label3", formObject.getTop("DecisionHistory_Label5"));
				 formObject.setLeft("DecisionHistory_Label3", formObject.getLeft("DecisionHistory_Label5")+275);
				 formObject.setTop("cmplx_DEC_Strength", formObject.getTop("DecisionHistory_Label3")+formObject.getHeight("DecisionHistory_Label3"));
				 formObject.setLeft("cmplx_DEC_Strength", formObject.getLeft("DecisionHistory_Label3"));
				 formObject.setTop("DecisionHistory_Label4", formObject.getTop("DecisionHistory_Label5"));
				 formObject.setLeft("DecisionHistory_Label4",  formObject.getLeft("DecisionHistory_Label3")+275);
				 formObject.setTop("cmplx_DEC_Weakness", formObject.getTop("DecisionHistory_Label4")+formObject.getHeight("DecisionHistory_Label4"));
				 formObject.setLeft("cmplx_DEC_Weakness", formObject.getLeft("DecisionHistory_Label4"));
				 formObject.setTop("DecisionHistory_Decision_Label4", formObject.getTop("DecisionHistory_Label5")) ;
				 formObject.setLeft("DecisionHistory_Decision_Label4",  formObject.getLeft("DecisionHistory_Label4")+275);
				 formObject.setTop("cmplx_DEC_Remarks", formObject.getTop("DecisionHistory_Decision_Label4")+formObject.getHeight("DecisionHistory_Decision_Label4"));
				 formObject.setLeft("cmplx_DEC_Remarks",  formObject.getLeft("DecisionHistory_Decision_Label4"));
				 formObject.setTop("DecisionHistory_Decision_ListView1", formObject.getTop("DecisionHistory_Label5")+100);
				 formObject.setLeft("DecisionHistory_Decision_ListView1", 25);
				 formObject.setVisible("DecisionHistory_save", true);
				 formObject.setTop("DecisionHistory_save", formObject.getTop("DecisionHistory_Decision_ListView1")+175);
				 formObject.setLeft("DecisionHistory_save", 25);
				 formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_Decision_ListView1") +225);
				 formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1") +5);
                   }
			//ended by yash for CC FSD
			// added by abhishek as per CC FSD
               else if (activityName.equalsIgnoreCase("CPV")){
            	   formObject.setVisible("DecisionHistory_Label10", false);
   				   formObject.setVisible("cmplx_DEC_New_CIFID", false);
   				   //added by nikhil as per CC FSD
   				   formObject.setVisible("cmplx_DEC_Decision_Reasoncode", false);
   				formObject.setVisible("DecisionHistory_Label11", false);
                   }
			// added by abhishek as per CC FSD
               else if (activityName.equalsIgnoreCase("Disbursal")){
    				// hide
            	   formObject.setVisible("DecisionHistory_Decision_Label1", false);
   				   formObject.setVisible("DecisionHistory_Label10", false);
   				   formObject.setVisible("cmplx_DEC_New_CIFID", false);
				   formObject.setVisible("DecisionHistory_chqbook", false);
				   formObject.setVisible("DecisionHistory_Button4", false);
   				   formObject.setVisible("DecisionHistory_Text15", false);
   				   formObject.setVisible("DecisionHistory_Text16", false);
				   formObject.setVisible("DecisionHistory_Text17", false);
				   formObject.setVisible("cmplx_DEC_IBAN_No", false);
   				   formObject.setVisible("cmplx_DEC_NewAccNo", false);
   				   formObject.setVisible("cmplx_DEC_ChequebookRef", false);
				   formObject.setVisible("DecisionHistory_Label9", false);
				   formObject.setVisible("cmplx_DEC_DCR_Refno", false);
   				   formObject.setVisible("DecisionHistory_Label5", false);
   				   formObject.setVisible("DecisionHistory_Label4", false);
				   formObject.setVisible("DecisionHistory_Label27", false);
				   formObject.setVisible("cmplx_DEC_Cust_Contacted", false);
				   // show
				   formObject.setVisible("DecisionHistory_Label26", true);
				   formObject.setVisible("cmplx_DEC_ReferTo", true);
				   formObject.setVisible("DecisionHistory_Label11", true);
				   formObject.setVisible("cmplx_DEC_Decision_Reasoncode", true);
				   formObject.setVisible("DecisionHistory_Decision_Label4", true);
				   formObject.setVisible("cmplx_DEC_Remarks", true);
				   
				   // set fields alignment
				   
				     formObject.setTop("DecisionHistory_Decision_Label3", 10);
					 formObject.setLeft("DecisionHistory_Decision_Label3", 25);
					 formObject.setTop("cmplx_DEC_Decision", formObject.getTop("DecisionHistory_Decision_Label3")+formObject.getHeight("DecisionHistory_Decision_Label3"));
					 formObject.setLeft("cmplx_DEC_Decision", 25);
					 formObject.setTop("DecisionHistory_Label11", 10);
					 formObject.setLeft("DecisionHistory_Label11", formObject.getLeft("DecisionHistory_Decision_Label3")+275);
					 formObject.setTop("cmplx_DEC_Decision_Reasoncode", formObject.getTop("DecisionHistory_Label11")+formObject.getHeight("DecisionHistory_Label11"));
					 formObject.setLeft("cmplx_DEC_Decision_Reasoncode", formObject.getLeft("DecisionHistory_Decision_Label3")+275);
					 formObject.setTop("DecisionHistory_Label26", 10);
					 formObject.setLeft("DecisionHistory_Label26", formObject.getLeft("DecisionHistory_Label11")+275);
					 formObject.setTop("cmplx_DEC_ReferTo", formObject.getTop("DecisionHistory_Label26")+formObject.getHeight("DecisionHistory_Label26"));
					 formObject.setLeft("cmplx_DEC_ReferTo", formObject.getLeft("DecisionHistory_Label11")+275);
					 formObject.setTop("DecisionHistory_Decision_Label4", 10);
					 formObject.setLeft("DecisionHistory_Decision_Label4",formObject.getLeft("DecisionHistory_Label26")+275);
					 formObject.setTop("cmplx_DEC_Remarks", formObject.getTop("DecisionHistory_Decision_Label4")+formObject.getHeight("DecisionHistory_Decision_Label4"));
					 formObject.setLeft("cmplx_DEC_Remarks", formObject.getLeft("DecisionHistory_Label26")+275);
					 formObject.setTop("DecisionHistory_Decision_ListView1", formObject.getTop("DecisionHistory_Decision_Label3")+100);
					 formObject.setLeft("DecisionHistory_Decision_ListView1", 25);
					 formObject.setVisible("DecisionHistory_save", true);
					 formObject.setTop("DecisionHistory_save", formObject.getTop("DecisionHistory_Decision_ListView1")+175);
					 formObject.setLeft("DecisionHistory_save", 25);
					 formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_Decision_ListView1") +225);
					 formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1") +5);
                   }
               else if(activityName.equalsIgnoreCase("Original_validation")){
            	   formObject.setVisible("DecisionHistory_Label10", false);
   					formObject.setVisible("cmplx_DEC_New_CIFID", false);
   					formObject.setVisible("DecisionHistory_Decision_Label1",false);
               }
			//++Below code added by  nikhil 11/10/17 as per CC FSD 2.7.3
				// ++ below code already present - 06-10-2017
			//added by nikhil as per CC FSD
               else if( activityName.equalsIgnoreCase("Hold_CPV") || activityName.equalsIgnoreCase("CPV_Analyst")){
            	formObject.setTop("DecisionHistory_Decision_Label3", 10);
               	formObject.setTop("cmplx_DEC_Decision", formObject.getHeight("DecisionHistory_Decision_Label3")+10);
               	formObject.setTop("DecisionHistory_Label11", 10);
               	formObject.setTop("cmplx_DEC_Decision_Reasoncode", formObject.getHeight("DecisionHistory_Label11")+10);
               	formObject.setTop("DecisionHistory_Label12", 10);
               	formObject.setTop("cmplx_DEC_NoofAttempts", formObject.getHeight("DecisionHistory_Label12")+10);
               	formObject.setTop("DecisionHistory_Decision_Label4", formObject.getHeight("cmplx_DEC_Decision")+formObject.getTop("cmplx_DEC_Decision")+20);
               	formObject.setTop("cmplx_DEC_Remarks", formObject.getHeight("DecisionHistory_Label12")+formObject.getTop("DecisionHistory_Decision_Label4"));
               	formObject.setTop("DecisionHistory_nonContactable", formObject.getTop("cmplx_DEC_Remarks"));
               	formObject.setTop("DecisionHistory_cntctEstablished", formObject.getTop("cmplx_DEC_Remarks"));
               	formObject.setLeft("DecisionHistory_Decision_Label3", 10);
               	formObject.setLeft("cmplx_DEC_Decision", 10);
               	formObject.setLeft("DecisionHistory_Label11", formObject.getLeft("DecisionHistory_Decision_Label3")+275);
               	formObject.setLeft("cmplx_DEC_Decision_Reasoncode", formObject.getLeft("DecisionHistory_Decision_Label3")+275);
               	formObject.setLeft("DecisionHistory_Label12", formObject.getLeft("DecisionHistory_Label11")+275);
               	formObject.setLeft("cmplx_DEC_NoofAttempts", formObject.getLeft("DecisionHistory_Label11")+275);
               	formObject.setLeft("DecisionHistory_Decision_Label4", 10);
               	formObject.setLeft("cmplx_DEC_Remarks", 10);
               	formObject.setLeft("DecisionHistory_nonContactable",formObject.getLeft("cmplx_DEC_Remarks")+275);
               	formObject.setLeft("DecisionHistory_cntctEstablished",formObject.getLeft("DecisionHistory_nonContactable")+275);
               	formObject.setTop("DecisionHistory_Decision_ListView1", formObject.getTop("cmplx_DEC_Remarks")+formObject.getHeight("cmplx_DEC_Remarks")+10);
              // 	formObject.setTop("DecisionHistory_save", formObject.getTop("DecisionHistory_Decision_ListView1")+formObject.getHeight("DecisionHistory_Decision_ListView1")+20);
               	formObject.setTop("DecisionHistory_save", formObject.getTop("DecisionHistory_Decision_ListView1")+200);
               	formObject.setVisible("DecisionHistory_save", true);
               //	formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+formObject.getHeight("DecisionHistory_save")+50);
               	formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_Decision_ListView1")+300);
               	formObject.setHeight("DecisionHistory", formObject.getTop("DecisionHistory_Decision_ListView1")+310);
               	
                //++ Below Code added By Nikhil on Oct 10, 2017  
               	//added by nikhil as per CC FSD
               	formObject.setNGValue("cmplx_DEC_Decision_Reasoncode", "--Select--");
               	//-- Above  Code added By Nikhil on Oct 10, 2017 
               }
			//ended by nikhil
			//--above code added by  nikhil 11/10/17 as per CC FSD 2.7.3
			
			
			//++ below code added by abhishek as per CC FSD 2.7.3

			
               else if(activityName.equalsIgnoreCase("Smart_CPV")){
            	   
            	   formObject.setTop("DecisionHistory_Decision_Label3", 10);
                  	formObject.setTop("cmplx_DEC_Decision", formObject.getHeight("DecisionHistory_Decision_Label3")+10);
                  	formObject.setTop("DecisionHistory_Label11", 10);
                  	formObject.setTop("cmplx_DEC_Decision_Reasoncode", formObject.getHeight("DecisionHistory_Label11")+10);
                  	formObject.setTop("DecisionHistory_Label12", 10);
                  	formObject.setTop("cmplx_DEC_NoofAttempts", formObject.getHeight("DecisionHistory_Label12")+10);
                  	formObject.setTop("DecisionHistory_Decision_Label4", formObject.getHeight("cmplx_DEC_Decision")+formObject.getTop("cmplx_DEC_Decision")+20);
                  	formObject.setTop("cmplx_DEC_Remarks", formObject.getHeight("DecisionHistory_Label12")+formObject.getTop("DecisionHistory_Decision_Label4"));
                  	formObject.setTop("DecisionHistory_nonContactable", formObject.getTop("cmplx_DEC_Remarks"));
                  	formObject.setTop("DecisionHistory_cntctEstablished", formObject.getTop("cmplx_DEC_Remarks"));
                  	formObject.setLeft("DecisionHistory_Decision_Label3", 10);
                  	formObject.setLeft("cmplx_DEC_Decision", 10);
                  	formObject.setLeft("DecisionHistory_Label11", formObject.getLeft("DecisionHistory_Decision_Label3")+275);
                  	formObject.setLeft("cmplx_DEC_Decision_Reasoncode", formObject.getLeft("DecisionHistory_Decision_Label3")+275);
                  	formObject.setLeft("DecisionHistory_Label12", formObject.getLeft("DecisionHistory_Label11")+275);
                  	formObject.setLeft("cmplx_DEC_NoofAttempts", formObject.getLeft("DecisionHistory_Label11")+275);
                  	formObject.setLeft("DecisionHistory_Decision_Label4", 10);
                  	formObject.setLeft("cmplx_DEC_Remarks", 10);
                  	formObject.setLeft("DecisionHistory_nonContactable",formObject.getLeft("cmplx_DEC_Remarks")+275);
                  	formObject.setLeft("DecisionHistory_cntctEstablished",formObject.getLeft("DecisionHistory_nonContactable")+275);
                  	formObject.setTop("DecisionHistory_Decision_ListView1", formObject.getTop("cmplx_DEC_Remarks")+formObject.getHeight("cmplx_DEC_Remarks")+10);
                 // 	formObject.setTop("DecisionHistory_save", formObject.getTop("DecisionHistory_Decision_ListView1")+formObject.getHeight("DecisionHistory_Decision_ListView1")+20);
                  	formObject.setTop("DecisionHistory_save", formObject.getTop("DecisionHistory_Decision_ListView1")+200);
                  	formObject.setVisible("DecisionHistory_save", true);
                  //	formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+formObject.getHeight("DecisionHistory_save")+50);
                  	formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_Decision_ListView1")+300);
                  	formObject.setHeight("DecisionHistory", formObject.getTop("DecisionHistory_Decision_ListView1")+310);
                  	
                 
                  	formObject.setNGValue("cmplx_DEC_Decision_Reasoncode", "--Select--");
            	   
            	   
					if(formObject.getNGValue("cmplx_DEC_contactableFlag").equalsIgnoreCase("true")){
		        		formObject.setEnabled("DecisionHistory_nonContactable", false);
						formObject.setEnabled("NotepadDetails_Frame1", false);
						formObject.setEnabled("DecisionHistory_Frame1", false);
						formObject.setEnabled("SmartCheck_Frame1", false);
						formObject.setEnabled("OfficeandMobileVerification_Frame1", false);
						
						formObject.setEnabled("DecisionHistory_cntctEstablished", true);
						
						formObject.setNGValue("cmplx_DEC_Decision","Smart CPV Hold");
		        	}else{
		        		formObject.setEnabled("NotepadDetails_Frame1", true);
						formObject.setEnabled("DecisionHistory_Frame1", true);
						formObject.setEnabled("SmartCheck_Frame1", true);
						formObject.setEnabled("OfficeandMobileVerification_Frame1", true);
						
						formObject.setEnabled("DecisionHistory_cntctEstablished", false);
						formObject.setEnabled("DecisionHistory_nonContactable", true);
						formObject.setNGValue("cmplx_DEC_Decision","--Select--");
		        	}
        	          	
               }
			//-- Above code added by abhishek as per CC FSD 2.7.3
			
			else{
				formObject.setVisible("DecisionHistory_Label10", true);
				formObject.setVisible("cmplx_DEC_New_CIFID", true);
			formObject.setLocked("cmplx_DEC_New_CIFID",true);
			}
			int framestate0 = formObject.getNGFrameState("ProductContainer");
			if(framestate0 == 0){
				SKLogger_CC.writeLog("RLOS count of current account not NTB","ProductDetailsLoader");
			}
			else {
				formObject.fetchFragment("ProductContainer", "Product", "q_product");
				SKLogger_CC.writeLog("RLOS count of current account not NTB","fetched product details");
				
			}
			
			
				int framestate1=formObject.getNGFrameState("IncomeDEtails");
				if(framestate1 == 0){
					SKLogger_CC.writeLog("RLOS count of current account not NTB","Incomedetails");
				}
				else {
					formObject.fetchFragment("IncomeDEtails", "IncomeDetails", "q_IncomeDetails");
					SKLogger_CC.writeLog("RLOS count of current account not NTB","fetched income details");
					formObject.setTop("Incomedetails",formObject.getTop("ProductDetailsLoader")+formObject.getHeight("ProductDetailsLoader"));
				}
				
				int framestate2=formObject.getNGFrameState("EmploymentDetails");
				if(framestate2 == 0){
					SKLogger_CC.writeLog("RLOS count of current account not NTB","EmploymentDetails");
				}
				else {
					formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmpDetails");
					SKLogger_CC.writeLog("RLOS count of current account not NTB","fetched employment details");
				}
				int framestate3=formObject.getNGFrameState("Address_Details_container");
				if(framestate3 == 0){
					SKLogger_CC.writeLog("RLOS count of current account not NTB","Address_Details_container");
				}
				else {
					formObject.fetchFragment("Address_Details_container", "AddressDetails", "q_AddressDetails");
					//formObject.setTop("Alt_Contact_container",formObject.getTop("Address_Details_Container")+formObject.getHeight("Address_Details_Container"));
					 formObject.setTop("Reference_Details",formObject.getTop("Address_Details_Container")+formObject.getHeight("Address_Details_Container"));
                        formObject.setTop("Alt_Contact_container",formObject.getTop("Reference_Details")+25);
                        formObject.setTop("Supplementary_Cont",formObject.getTop("Reference_Details")+50);
                         formObject.setTop("Card_Details", formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container"));
                         formObject.setTop("FATCA", formObject.getTop("Card_Details")+30);
     					formObject.setTop("KYC", formObject.getTop("FATCA")+20);
     					formObject.setTop("OECD", formObject.getTop("KYC")+20);

					SKLogger_CC.writeLog("RLOS count of current account not NTB","fetched address details");
				}
				
				int framestate4=formObject.getNGFrameState("ReferenceDetails");
				if(framestate4 == 0){
					SKLogger_CC.writeLog("RLOS count of current account not NTB","Alt_Contact_container");
				}
				else {
					formObject.fetchFragment("Reference_Details", "Reference_Details", "q_ReferenceDetails");
					 formObject.setTop("Reference_Details",formObject.getTop("Address_Details_container")+formObject.getHeight("Address_Details_container"));
                        //formObject.setTop("Alt_Contact_container",formObject.getTop("ReferenceDetails")+formObject.getHeight("ReferenceDetails")+25);
                        formObject.setTop("Supplementary_Cont",formObject.getTop("Reference_Details")+250);
                        formObject.setTop("Card_Details", formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container"));
                        formObject.setTop("FATCA", formObject.getTop("Card_Details")+30);
    					formObject.setTop("KYC", formObject.getTop("FATCA")+20);
    					formObject.setTop("OECD", formObject.getTop("KYC")+20);
                        
					SKLogger_CC.writeLog("RLOS count of current account not NTB","fetched address details");
				}
				
				
				int framestate5=formObject.getNGFrameState("Alt_Contact_container");
				if(framestate5 == 0){
					SKLogger_CC.writeLog("RLOS count of current account not NTB","Alt_Contact_container");
				}
				else {
					formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
					 //formObject.setTop("ReferenceDetails",formObject.getTop("Address_Details_Container")+formObject.getHeight("Address_Details_Container"));
                        formObject.setTop("Alt_Contact_container",formObject.getTop("Reference_Details")+formObject.getHeight("Reference_Details")+200);
                        formObject.setTop("Supplementary_Cont",formObject.getTop("Reference_Details")+250);
                        formObject.setTop("Card_Details", formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container")+20);
                        formObject.setTop("FATCA", formObject.getTop("Card_Details")+30);
    					formObject.setTop("KYC", formObject.getTop("FATCA")+20);
    					formObject.setTop("OECD", formObject.getTop("KYC")+20);
                        
					SKLogger_CC.writeLog("RLOS count of current account not NTB","fetched address details");
				}
			
			loadInDecGrid();
			String activity = formObject.getWFActivityName();
			SKLogger_CC.writeLog("RLOS cactivity","activity"+activity);
			if(!activity.equalsIgnoreCase("CAD_Analyst1") && !activity.equalsIgnoreCase("Cad_Analyst2")){
				SKLogger_CC.writeLog("RLOS inside activity","activity"+activity);
				SKLogger_CC.writeLog("RLOS Initiation Inside ","gettop="+formObject.getTop("cmplx_DEC_Gr_DecisonHistory"));
				formObject.setTop("cmplx_DEC_Gr_DecisonHistory",460);
				SKLogger_CC.writeLog("RLOS Initiation Inside ","gettop1="+formObject.getTop("cmplx_DEC_Gr_DecisonHistory"));
				formObject.setTop("DecisionHistory_save",630);
				SKLogger_CC.writeLog("RLOS Initiation Inside ","gettop2="+formObject.getTop("DecisionHistory_save"));
			}
			if(!activity.equalsIgnoreCase("CPV") && !activity.equalsIgnoreCase("FCU")){
			formObject.setVisible("NotepadDetails_Frame3", false);
			}
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}	
		if (pEvent.getSource().getName().equalsIgnoreCase("Frame4"))
        {
            hm.put("IncomingDocuments","Clicked");
            SKLogger_CC.writeLog("RLOS Initiation Inside ","IncomingDocuments");
            //frame,fragment,q_variable
            formObject.fetchFragment("Frame4", "IncomingDocument", "q_IncDoc");
            SKLogger_CC.writeLog("RLOS Initiation Inside ","fetchIncomingDocRepeater");
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
				if(formObject.getNGValue("cmplx_Customer_NEP").equalsIgnoreCase("false") && formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("false"))
					formObject.setEnabled("IncomingDoc_ViewSIgnature",true);
				else if(formObject.getNGValue("cmplx_Customer_NEP").equalsIgnoreCase("true") || formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true")){
					formObject.setEnabled("IncomingDoc_ViewSIgnature",false);
					//formObject.setVisible("IncomingDoc_UploadSig",true);
					//formObject.setEnabled("IncomingDoc_UploadSig",false);
				}
			}
            
            SKLogger_CC.writeLog("RLOS Initiation eventDispatched()","formObject.fetchFragment1");
            try{

                throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
            }
            finally{
                hm.clear();
            }
        }
		 if (pEvent.getSource().getName().equalsIgnoreCase("Reference_Details")) {
    			hm.put("Reference_Details","Clicked");
				formObject.fetchFragment("Reference_Details", "Reference_Details", "q_ReferenceDetails");
    			//LoadPickList("Reference_Details_ReferenceRelatiomship", "select '--Select--' as description,'' as code union select convert(varchar, description) from NG_MASTER_Relationship with (nolock) order by code");
    			//loadPicklist3();
    			//loadInDecGrid();
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{hm.clear();}
    		}
		 //Tanshu Aggarwal 17/08/2017 Card Details changes
		 if (pEvent.getSource().getName().equalsIgnoreCase("Card_Details")) {
    			/*hm.put("Card_Details","Clicked");
				popupFlag="N";*/
				//throw new ValidatorException(new CustomExceptionHandler("", "Product_Save#Product Save Successful","", hm));
				int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
				if(n==0){
					formObject.setNGFrameState("Card_Details",1);
					throw new ValidatorException(new CustomExceptionHandler("","cmplx_Product_cmplx_ProductGrid#Please Add a product first","",hm));
				}
				if(n>0){
					formObject.fetchFragment("Card_Details", "CardDetails", "q_cardDetails");
					String Product_Type=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0);
					if(Product_Type.equalsIgnoreCase("Islamic")){
						formObject.setEnabled("cmplx_CardDetails_charity_amount", true);
						formObject.setEnabled("cmplx_CardDetails_Charity_org", true);
					}
					else{
						formObject.setNGValue("cmplx_CardDetails_charity_amount","");
						formObject.setNGValue("cmplx_CardDetails_Charity_org","");
						formObject.setEnabled("cmplx_CardDetails_charity_amount", false);
						formObject.setEnabled("cmplx_CardDetails_Charity_org", false);
					}
					if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6).equalsIgnoreCase("Self Employed")){
						formObject.setEnabled("cmplx_CardDetails_CompanyEmbossing_name", true);
					}
					else{
						formObject.setNGValue("cmplx_CardDetails_CompanyEmbossing_name","");
						formObject.setEnabled("cmplx_CardDetails_CompanyEmbossing_name", false);
					}
				}
			
				LoadPickList("cmplx_CardDetails_MarketingCode","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_MarketingCode where isActive='Y' order by code");
				LoadPickList("cmplx_CardDetails_CustClassification","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_ClassificationCode where isActive='Y' order by code");
				//++ Below Code added By abhishek on Oct 9, 2017  to fix : "42,43,44-Transaction fee profile masters are incorrect,intetrest fee profile masters are incorrect,fee profile masters are incorrect" : Reported By Shashank on Oct 05, 2017++
				
				LoadPickList("CardDetails_TransactionFP","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_TransactionFeeProfile where isActive='Y' order by code");
				LoadPickList("CardDetails_InterestFP","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_InterestProfile where isActive='Y' order by code");
				LoadPickList("CardDetails_FeeProfile","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_feeprofile where isActive='Y' order by code");
				
				//-- Above Code added By abhishek on Oct 9, 2017  to fix : "42,43,44-Transaction fee profile masters are incorrect,intetrest fee profile masters are incorrect,fee profile masters are incorrect" : Reported By Shashank on Oct 05, 2017--
    			
    			//loadPicklist3();
    			//loadInDecGrid();
				/*try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{hm.clear();}*/
    		}
		 //Tanshu Aggarwal 17/08/2017 Card Details changes
		 if (pEvent.getSource().getName().equalsIgnoreCase("Supplementary_Cont")) {
    			hm.put("Supplementary_Cont","Clicked");
				formObject.fetchFragment("Supplementary_Cont", "SupplementCardDetails", "q_SuppCardDetails");
    			
    			loadPicklist_suppCard();
    			
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{hm.clear();}
    		}
		 
		 if (pEvent.getSource().getName().equalsIgnoreCase("CompDetails")) {
 			hm.put("CompDetails","Clicked");
				formObject.fetchFragment("CompDetails", "CompanyDetails", "q_CompanyDetails");
				
				int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
				SKLogger_CC.writeLog("CC Initiation", "Inside else if of product self employed get rowcount for company details");
				for(int i=0;i<n;i++){
					if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("Business titanium Card") && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")){
					    SKLogger_CC.writeLog("EXCEPTION    :    ", "inside btc");
						formObject.setVisible("CompanyDetails_Label8", true);
						formObject.setVisible("cmplx_CompanyDetails_cmplx_CompanyGrid_EffecLOB", true);//Effective length of buss
						//added 09/08/2017 to make company details disable
						//formObject.setEnabled("CompDetails",false);
						//added 09/08/2017 to make company details disable
						break;
					}
					
					else if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("Self Employed Credit Card") && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")){
						 SKLogger_CC.writeLog("EXCEPTION    :    ", "inside self emp credit card");
						formObject.setVisible("CompanyDetails_Label27", true);
						formObject.setVisible("cmplx_CompanyDetails_cmplx_CompanyGrid_emirateOfWork", true);//Emirate of work
						formObject.setVisible("CompanyDetails_Label29", true);
						formObject.setVisible("cmplx_CompanyDetails_cmplx_CompanyGrid_headOfficeEmirate", true);
						formObject.setVisible("CompanyDetails_Label28", true);
						formObject.setVisible("cmplx_CompanyDetails_cmplx_CompanyGrid_VisaSponsor", true);
						//added 09/08/2017 to make company details disable
						//formObject.setEnabled("CompDetails",false);
						//added 09/08/2017 to make company details disable
						break;
					}
					else{
						formObject.setVisible("CompanyDetails_Label27", false);
						formObject.setVisible("cmplx_CompanyDetails_cmplx_CompanyGrid_emirateOfWork", false);//Emirate of work
						formObject.setVisible("CompanyDetails_Label29", false);
						formObject.setVisible("cmplx_CompanyDetails_cmplx_CompanyGrid_headOfficeEmirate", false);
						formObject.setVisible("CompanyDetails_Label28", false);
						formObject.setVisible("cmplx_CompanyDetails_cmplx_CompanyGrid_VisaSponsor", false);
						formObject.setVisible("CompanyDetails_Label8", false);
						formObject.setVisible("cmplx_CompanyDetails_cmplx_CompanyGrid_EffecLOB", false);
						//added 09/08/2017 to make company details disable
						//formObject.setEnabled("CompDetails",false);
						//added 09/08/2017 to make company details disable
					}
					
				}
				
				try{
					String customerName = formObject.getNGValue("cmplx_Customer_FirstNAme")+" "+formObject.getNGValue("cmplx_Customer_MiddleNAme")+" "+formObject.getNGValue("cmplx_Customer_LastNAme");
					if(customerName!=null && !customerName.equalsIgnoreCase("") && !customerName.equalsIgnoreCase(" ")){
					String Executexml= makeInputForGrid(customerName);
					SKLogger_CC.writeLog("RLOS","Grid Data is"+Executexml);
					formObject.NGAddListItem("cmplx_CompanyDetails_cmplx_CompanyGrid",Executexml);
					}
					}catch(Exception ex){
						SKLogger_CC.writeLog("RLOS","Exception: "+printException(ex));
					}
					
 			
 			//loadPicklist3();
 			//loadInDecGrid();
				//added by yash for CC FSD
					if(activityName.equalsIgnoreCase("DDVT_Maker")){
						formObject.setLocked("CompanyDetails_Frame1",true);
						
						
					}
					// added by abhishek as per CC FSD
					formObject.setEnabled("CompanyDetails_DatePicker1",true);
					formObject.setEnabled("lob",false);
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{hm.clear();}
 		}

		 
		 if (pEvent.getSource().getName().equalsIgnoreCase("Auth_Sign_Details")) {
    			hm.put("Auth_Sign_Details","Clicked");
				formObject.fetchFragment("Auth_Sign_Details", "AuthorisedSignDetails", "q_AuthorisedSignDetails");
    			//loadPicklist3();
    			//loadInDecGrid();
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{hm.clear();}
    		}
		 if (pEvent.getSource().getName().equalsIgnoreCase("Partner_Details")) {
    			hm.put("Partner_Details","Clicked");
				formObject.fetchFragment("Partner_Details", "PartnerDetails", "q_PartnerDetails");
    			//loadPicklist3();
    			//loadInDecGrid();
    			disablePartnerDetails(formObject);
    			try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{hm.clear();}
    		}
		 if (pEvent.getSource().getName().equalsIgnoreCase("Details")) {
				hm.put("Details","Clicked");
				formObject.fetchFragment("Details", "CC_Loan", "q_CC_Loan");
				loadPicklist_ServiceRequest();
				 //++ Below Code added By Nikhil on Oct 6, 2017  to fix : "12-Percnetage to be enabled when DDS mode is Percentage" : Reported By Shashank on Oct 05, 2017++
				//below code also fix point "13-Flat amount to be enabled and mandatory when DDS mode is flat amount"
				formObject.setNGValue("cmplx_CC_Loan_DDSMode", "--Select--");
				//-- Above Code added By Nikhil on Oct 6, 2017  to fix : "12-Percnetage to be enabled when DDS mode is Percentage" : Reported By Shashank on Oct 05, 2017--

				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{hm.clear();}
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("Part_Match")) {
				hm.put("Part_Match","Clicked");
				formObject.fetchFragment("Part_Match", "PartMatch", "q_PartMatch");
				
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{hm.clear();}
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("Finacle_CRM_Incidents")) {
				hm.put("Finacle_CRM_Incidents","Clicked");
				formObject.fetchFragment("Finacle_CRM_Incidents", "FinacleCRMIncident", "q_FinacleCRMIncident");
				
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{hm.clear();}
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("Finacle_CRM_CustomerInformation")) {
				hm.put("Finacle_CRM_CustomerInformation","Clicked");
				formObject.fetchFragment("Finacle_CRM_CustomerInformation", "FinacleCRMCustInfo", "q_FinacleCRMCustInfo");
				formObject.setNGValue("FinacleCRMCustInfo_WINAME",formObject.getWFWorkitemName());
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{hm.clear();}
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("External_Blacklist")) {
				/*hm.put("External_Blacklist","Clicked");
				popupFlag="N";*/
				formObject.fetchFragment("External_Blacklist", "ExternalBlackList", "q_ExternalBlackList");
				if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6).equalsIgnoreCase("Salaried")){
					
					formObject.setLocked("ExternalBlackList_Frame1", true);
					
				}
				
				/*try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{hm.clear();}*/
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("Finacle_Core")) {
				/*hm.put("Finacle_Core","Clicked");
				popupFlag="N";*/
				formObject.fetchFragment("Finacle_Core", "FinacleCore", "q_FinacleCore");
				fetchfinacleAvgRepeater();
				fetchfinacleTORepeater();
				fetchfinacleDocRepeater();
					//if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6).equalsIgnoreCase("Salaried")){
				if(activityName.equalsIgnoreCase("DDVT_Maker")){
					formObject.setLocked("FinacleCore_Frame1", true);
				}
				try{
	    			String query="select AcctType,'',AcctId,AcctNm,AccountOpenDate,AcctStat,'',AvailableBalance,'','','','','','' from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
	    			List<List<String>> list_acc=formObject.getDataFromDataSource(query);
	    			for(List<String> mylist : list_acc)
	    			 {
	    				SKLogger_CC.writeLog("PL DDVT Maker", "Data to be added in Grid account Grid: "+mylist.toString());
	    				formObject.addItemFromList("cmplx_FinacleCore_FinaclecoreGrid", mylist);
	    			 }
	    			
	    			query="select AcctId,LienId,LienAmount,LienRemarks,LienReasonCode,LienStartDate,LienExpDate from ng_rlos_FinancialSummary_LienDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
					//changed ended
	    			List<List<String>> list_lien=formObject.getDataFromDataSource(query);
	    			for(List<String> mylist : list_lien)
	    			 {
	    				SKLogger_CC.writeLog("PL DDVT Maker", "Data to be added in Grid: "+mylist.toString());
	    				formObject.addItemFromList("cmplx_FinacleCore_liendet_grid", mylist);
	    			 }
					 //changed here in this query
					query="select AcctId,'','','','' from ng_rlos_FinancialSummary_SiDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
	    			List<List<String>> list_SIDet=formObject.getDataFromDataSource(query);
					//changed ended
	    			
	    			for(List<String> mylist : list_SIDet)
	    			 {
	    				SKLogger_CC.writeLog("PL DDVT Maker", "Data to be added in Grid: "+mylist.toString());
	    				
	    				formObject.addItemFromList("cmplx_FinacleCore_sidet_grid", mylist);
	    			 }
	    			query="select '',AcctId,returntype,returnAmount,returnNumber,returnDate,retReasonCode,instrumentdate,returnType,'','','','','' from ng_rlos_FinancialSummary_ReturnsDtls with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
	    			List<List<String>> list_DDSDet=formObject.getDataFromDataSource(query);
	    			SKLogger_CC.writeLog("PL DDVT Maker", "query to be added in list_DDSDet Grid: "+query);
    				
	    			//changed ended
	    			
	    			for(List<String> mylist : list_DDSDet)
	    			 {
	    				SKLogger_CC.writeLog("PL DDVT Maker", "Data to be added in list_DDSDet Grid: "+mylist.toString());
	    				
	    				formObject.addItemFromList("cmplx_FinacleCore_DDSgrid", mylist);
	    			 }
	    			
	    			
	    			}
	    			catch(Exception e){
	    				SKLogger_CC.writeLog("PL DDVT Maker", "Exception while setting data in grid:"+e.getMessage());
	    				throw new ValidatorException(new FacesMessage("Error while setting data in account grid"));
	    			}
				//}
				
				/*try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{hm.clear();}*/
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("MOL")) {
				/*hm.put("MOL","Clicked");
				popupFlag="N";*/
				formObject.fetchFragment("MOL", "MOL1", "q_Mol1");
				// added by abhishek as per CC FSD
				if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6).equalsIgnoreCase("self Employed")){
					formObject.setLocked("MOL1_Frame1", true);
				}
				
				/*try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{hm.clear();}*/
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("World_Check")) {
				/*hm.put("World_Check","Clicked");
				popupFlag="N";*/
				formObject.fetchFragment("World_Check", "WorldCheck1", "q_WorldCheck");
				
				//++ Below Code added By Tanshu on Oct 6, 2017  to fix : "18,19,20-DDVT-World check-Birth country incorrect masters" : Reported By Shashank on Oct 05, 2017++
				LoadPickList("WorldCheck1_ResidentCountry","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_Country where isActive='Y' order by code");
				LoadPickList("WorldCheck1_BirthCountry","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_Country where isActive='Y' order by code");
				LoadPickList("WorldCheck1_Gender","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_gender where isActive='Y' order by code");
				//-- Above Code added By Tanshu on Oct 6, 2017  to fix : "18,19,20-DDVT-World check-Birth country incorrect masters" : Reported By Shashank on Oct 05, 2017--
				/*try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{hm.clear();}*/
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("Reject_Enquiry")) {
				/*hm.put("Reject_Enquiry","Clicked");
				popupFlag="N";*/
				formObject.fetchFragment("Reject_Enquiry", "RejectEnq", "q_RejectEnq");
					//if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6).equalsIgnoreCase("Salaried")){
				if(activityName.equalsIgnoreCase("DDVT_Maker")){
					formObject.setLocked("RejectEnq_Frame1", true);
				}
				//}
				
				/*try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{hm.clear();}*/
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("Salary_Enquiry")) {
				/*hm.put("Salary_Enquiry","Clicked");
				popupFlag="N";*/
				formObject.fetchFragment("Salary_Enquiry", "SalaryEnq", "q_SalaryEnq");
				//if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6).equalsIgnoreCase("Salaried")){
				if(activityName.equalsIgnoreCase("DDVT_Maker")){
					formObject.setLocked("SalaryEnq_Frame1", true);
				}
				try{
					String query="select SalCreditDate,SalCreditMonth,AcctId,SalAmount,'' from ng_rlos_FinancialSummary_SalTxnDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
					SKLogger_CC.writeLog("PL DDVT Maker", "Data to be added in account Grid account query: "+query);
					List<List<String>> list_acc=formObject.getDataFromDataSource(query);
	    			for(List<String> mylist : list_acc)
	    			 {
	    				SKLogger_CC.writeLog("PL DDVT Maker", "Data to be added in account Grid account Grid: "+mylist.toString());
	    				formObject.addItemFromList("cmplx_SalaryEnq_SalGrid", mylist);
	    			 }
	    			
				}
				catch(Exception e){
					
				}
				
				//}
				
				/*try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{hm.clear();}*/
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("PostDisbursal")) {
				hm.put("PostDisbursal","Clicked");
				formObject.fetchFragment("PostDisbursal", "PostDisbursal", "q_PostDisbursal");
				
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{hm.clear();}
			}
			//added by yash 23/08/2017
			if (pEvent.getSource().getName().equalsIgnoreCase("CAD")) {
				hm.put("CAD","Clicked");
				formObject.fetchFragment("CAD", "NotepadDetails", "q_NotepadDetails");
				formObject.setLocked("NotepadDetails_notecode", true);
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{hm.clear();}
			}
			//added by yash 23/08/2017
			if (pEvent.getSource().getName().equalsIgnoreCase("CAD_Decision")) {
				hm.put("CAD_Decision","Clicked");
				formObject.fetchFragment("CAD_Decision", "CAD_Decision", "q_CadDecision");
				
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{hm.clear();
				}
			}
			
//added by yash on 23/8/2017
			
			if (pEvent.getSource().getName().equalsIgnoreCase("Notepad_Values")) {
				hm.put("Notepad_Values","Clicked");
				formObject.fetchFragment("Notepad_Values", "NotepadDetails", "q_NotepadDetails");
				LoadPickList("NotepadDetails_notedesc", "select '--Select--'  union select  description from ng_master_notedescription");
				formObject.setNGValue("NotepadDetails_notedesc","--Select--");
				formObject.setLocked("NotepadDetails_notecode", true);
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{hm.clear();}
			}
			
			//ended by yash on 23/8/2017
			
			if (pEvent.getSource().getName().equalsIgnoreCase("Smart_Check1")) {
    			hm.put("Smart_Check1","Clicked");
				formObject.fetchFragment("Smart_Check1", "SmartCheck1", "q_SmartCheckFCU");
    			//loadPicklist3();
    			//loadInDecGrid();
    			//++Below code added by  nikhil 13/10/17 as per CC FSD 2.2
    			formObject.setTop("SmartCheck1_CreditRemarks", 10);
    			formObject.setTop("SmartCheck1_CPVRemarks", formObject.getTop("SmartCheck1_CreditRemarks")+65);
    			formObject.setTop("SmartCheck1_FCURemarks", formObject.getTop("SmartCheck1_CPVRemarks")+65);
    			formObject.setTop("SmartCheck1_Label1", formObject.getTop("SmartCheck1_CreditRemarks"));
    			formObject.setTop("SmartCheck1_Label2", formObject.getTop("SmartCheck1_CPVRemarks"));
    			formObject.setTop("SmartCheck1_Label4", formObject.getTop("SmartCheck1_FCURemarks"));
    			//--above code added by  nikhil 13/10/17 as per CC FSD 2.2
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{hm.clear();}
    		}
			if (pEvent.getSource().getName().equalsIgnoreCase("CreditCard_Enq")) {
				hm.put("CreditCard_Enq","Clicked");
				formObject.fetchFragment("CreditCard_Enq", "CreditCardEnq", "q_CCenq");
				//fetchoriginalDocRepeater();
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{hm.clear();}
			}
			
			if (pEvent.getSource().getName().equalsIgnoreCase("Case_hist")) {
				hm.put("Case_hist","Clicked");
				formObject.fetchFragment("Case_hist", "CaseHistoryReport", "q_CaseHist");
				//fetchoriginalDocRepeater();
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{hm.clear();}
			}
			
			if (pEvent.getSource().getName().equalsIgnoreCase("LOS")) {
				hm.put("LOS","Clicked");
				formObject.fetchFragment("LOS", "LOS", "q_LOS");
				//fetchoriginalDocRepeater();
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{hm.clear();}
			}
			
			if (pEvent.getSource().getName().equalsIgnoreCase("Customer_Details_Verification")) {
    			hm.put("Customer_Details_Verification","Clicked");
				formObject.fetchFragment("Customer_Details_Verification", "CustDetailVerification", "q_CustDetailVeri");
    			//loadPicklist_custverification();
    			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_CustDetailVerification_mobno1_ver","cmplx_CustDetailVerification_mobno2_ver","cmplx_CustDetailVerification_dob_ver","cmplx_CustDetailVerification_POBoxno_ver","cmplx_CustDetailVerification_emirates_ver","cmplx_CustDetailVerification_persorcompPOBox_ver","cmplx_CustDetailVerification_resno_ver","cmplx_CustDetailVerification_offtelno_ver","cmplx_CustDetailVerification_hcountrytelno_ver","cmplx_CustDetailVerification_hcontryaddr_ver","cmplx_CustDetailVerification_email1_ver","cmplx_CustDetailVerification_email2_ver");
    			
    			LoadPicklistVerification(LoadPicklist_Verification);
    			LoadPickList("cmplx_CustDetailVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision order by code");
    			
    			openDemographicFrags(formObject);
    			autopopulateValues(formObject);
    			formObject.setLocked("CustDetailVerification_Frame1", true);
    			//loadPicklist3();
    			//loadInDecGrid();
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{hm.clear();}
    		}
			
			if (pEvent.getSource().getName().equalsIgnoreCase("Business_Verification")) {
    			hm.put("Business_Verification","Clicked");
				formObject.fetchFragment("Business_Verification", "BussinessVerification", "q_BussVerification");
    			LoadPickList("cmplx_BussVerification_contctTelChk", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Other' ");
    			formObject.setLocked("BussinessVerification_Frame1", true);
    			//loadPicklist3();
    			//loadInDecGrid();
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{hm.clear();}
    		}
			
			if (pEvent.getSource().getName().equalsIgnoreCase("HomeCountry_Verification")) {
    			hm.put("HomeCountry_Verification","Clicked");
				formObject.fetchFragment("HomeCountry_Verification", "HomeCountryVerification", "q_HCountryVerification");
    			LoadPickList("cmplx_HCountryVerification_Hcountrytelverified", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Other' ");
    			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_HCountryVerification_Hcountrytelverified");
    			LoadPicklistVerification(LoadPicklist_Verification);
    			formObject.setLocked("HomeCountryVerification_Frame1", true);
    			//loadPicklist3();
    			//loadInDecGrid();
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{hm.clear();}
    		}
			
			if (pEvent.getSource().getName().equalsIgnoreCase("ResidenceVerification")) {
    			hm.put("ResidenceVerification","Clicked");
				formObject.fetchFragment("ResidenceVerification", "ResidenceVerification", "q_ResiVerification");
    			openDemographicFrags(formObject);
    			LoadPickList("cmplx_ResiVerification_Telnoverified", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPVveri");
    			formObject.setNGValue("cmplx_ResiVerification_cntcttelno",formObject.getNGValue("AlternateContactDetails_ResidenceNo"));
    			formObject.setLocked("cmplx_ResiVerification_cntcttelno", true);
    			LoadPickList("cmplx_ResiVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision order by code");
    			formObject.setLocked("ResidenceVerification_Frame1", true);
    			//loadPicklist3();
    			//loadInDecGrid();
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{hm.clear();}
    		}
			if (pEvent.getSource().getName().equalsIgnoreCase("Guarantor_Verification")) {
    			hm.put("Guarantor_Verification","Clicked");
				formObject.fetchFragment("Guarantor_Verification", "GuarantorVerification", "");
    			//LoadPickList("cmplx_GuarantorVerification_verdoneonmob", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Other' ");
    			LoadPickList("cmplx_GuarantorVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision order by code");
    			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_GuarantorVerification_verdoneonmob");
    			LoadPicklistVerification(LoadPicklist_Verification);
    			
    			//loadPicklist3();
    			//loadInDecGrid();
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{hm.clear();}
    		}

			if (pEvent.getSource().getName().equalsIgnoreCase("Reference_Detail_Verification")) {
    			hm.put("Reference_Detail_Verification","Clicked");
				formObject.fetchFragment("Reference_Detail_Verification", "ReferenceDetailVerification", "q_RefDetailVerification");
    			LoadPickList("cmplx_RefDetVerification_ref1cntctd", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Other' ");
    			LoadPickList("cmplx_RefDetVerification_ref2cntctd", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Other' ");
    			LoadPickList("cmplx_RefDetVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision order by code");
    			formObject.setLocked("ReferenceDetailVerification_Frame1", true);
    			//loadPicklist3();
    			//loadInDecGrid();
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{hm.clear();}
    		}
			
			if (pEvent.getSource().getName().equalsIgnoreCase("Office_Mob_Verification")) {
    			hm.put("Office_Mob_Verification","Clicked");
				formObject.fetchFragment("Office_Mob_Verification", "OfficeandMobileVerification", "q_OffVerification");
    			//loadPicklist_officeverification();
    			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_OffVerification_hrdemailverified","cmplx_OffVerification_colleaguenoverified","cmplx_OffVerification_fxdsal_ver","cmplx_OffVerification_accpvded_ver","cmplx_OffVerification_desig_ver","cmplx_OffVerification_doj_ver","cmplx_OffVerification_cnfminjob_ver");
    			LoadPicklistVerification(LoadPicklist_Verification);
    			LoadPickList("cmplx_OffVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision order by code");
    			formObject.setLocked("OfficeandMobileVerification_Frame1", true);
    			
    			
    			//++ below code added by abhishek as per CC FSD 2.7.3
    			if(activityName.equalsIgnoreCase("Smart_CPV")){
    				//below code commneted for calling procedure
    				
    				/*String outputXml = "";
	    			StringBuilder values = new StringBuilder("'" + formObject.getWFWorkitemName() + "'");
	    	      // values.append(",'null'");
	    	      //  values.append(",'" + formObject.getNGValue("TARGET_WS") + "'");
	    			FormConfig objFormConfig = FormContext.getCurrentInstance().getFormConfig( );
	    			String jtsIP = objFormConfig.getConfigElement("JTSIP");
	    			String jtsPort = objFormConfig.getConfigElement("JTSPORT");
	    			SKLogger_CC.writeLog(" Ccinside office verification load in smart cpv workstep", "JTSIP and port is ::"+jtsIP+":::"+jtsPort);
	    	        String inputXml = getAPProcedureInputXml(formObject.getWFEngineName(), values.toString(), "NG_CC_SmartCheck", formObject.getWFSessionId());
	    	        SKLogger_CC.writeLog(" Ccinside office verification load in smart cpv workstep", "input xml is ::"+inputXml);
	    	        IFormCabinetList.GetCabinetDetails(formObject.getWFEngineName());
	    	       
	    	        try {
	    	        	
	    	            outputXml = IFormCallBroker.execute(inputXml,jtsIP, Integer.parseInt(jtsPort));
	    	            SKLogger_CC.writeLog(" Ccinside office verification load in smart cpv workstep", "OUt xml is ::"+outputXml);
	    	        } catch (IOException e) {
	    	            // TODO Auto-generated catch block
	    	            e.printStackTrace();
	    	            SKLogger_CC.writeLog(" Ccinside office verification load in smart cpv workstep", printException(e));
	    	        } catch (Exception e) {
	    	            // TODO Auto-generated catch block
	    	            e.printStackTrace();
	    	        }*/
	    	       
	    	       /* String maincode =  outputXml.substring(outputXml.indexOf("<MainCode>") + 10, outputXml.indexOf("</MainCode>"));
	    	        if(maincode.equalsIgnoreCase("0")){
	    	        	String OutputValue = outputXml.substring(outputXml.indexOf("<Output>") + 10, outputXml.indexOf("</Output>"));
	    	        	 SKLogger_CC.writeLog(" Ccinside office verification load in smart cpv workstep", "Procedure output value  is ::"+OutputValue);
	    	        }*/
    				
    				 String EnableFlagValue = formObject.getNGValue("cmplx_OffVerification_EnableFlag");
    				 String sQuery =" SELECT ProcessInstanceID,lockStatus FROM WFINSTRUMENTTABLE WHERE activityname = 'CPV' AND ProcessInstanceID ='"+formObject.getWFWorkitemName()+"'";
    				 SKLogger_CC.writeLog(" Ccinside office verification load in smart cpv workstep", "query is ::"+sQuery);
    				 List<List<String>> list=formObject.getDataFromDataSource(sQuery);
    				 SKLogger_CC.writeLog(" Ccinside office verification load in smart cpv workstep", "list is ::"+list+"size is::"+list.size());
    				 
    				 if(list.size()==0){
    					
    					 formObject.setLocked("OfficeandMobileVerification_Frame1",true);
    				 }
    				 else{
    					 String lockStatus = list.get(0).get(1);
    					 if(lockStatus.equalsIgnoreCase("Y")){
    						 if(EnableFlagValue.equalsIgnoreCase("true")){
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
    			
    			//-- Above code added by abhishek as per CC FSD 2.7.3
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{hm.clear();}
    		}
			
			if (pEvent.getSource().getName().equalsIgnoreCase("LoanCard_Details_Check")) {
    			hm.put("LoanCard_Details_Check","Clicked");
				formObject.fetchFragment("LoanCard_Details_Check", "LoanandCard", "q_LoanandCard");
    			loadPicklist_loancardverification();
    			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_LoanandCard_loanamt_ver","cmplx_LoanandCard_tenor_ver","cmplx_LoanandCard_emi_ver","cmplx_LoanandCard_islorconv_ver","cmplx_LoanandCard_firstrepaydate_ver","cmplx_LoanandCard_cardtype_ver","cmplx_LoanandCard_cardlimit_ver");
    			LoadPicklistVerification(LoadPicklist_Verification);
    			LoadPickList("cmplx_LoanandCard_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision order by code");
    			formObject.setLocked("LoanandCard_Frame1", true);
    			//loadPicklist3();
    			//loadInDecGrid();
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{hm.clear();}
    		}
			
			if (pEvent.getSource().getName().equalsIgnoreCase("Notepad_Details")) {
    			hm.put("Notepad_Details","Clicked");
				formObject.fetchFragment("Notepad_Details", "NotepadDetails", "q_NotepadDetails");
    			//loadPicklist3();
    			//loadInDecGrid();
    			formObject.setLocked("NotepadDetails_Frame2", true);
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{hm.clear();}
    		}
			
			if (pEvent.getSource().getName().equalsIgnoreCase("Smart_Check")) {
    			/*hm.put("Smart_Check","Clicked");
				popupFlag="N";*/
    			formObject.fetchFragment("Smart_Check", "SmartCheck", "q_SmartCheck");
    			//loadPicklist3();
    			//loadInDecGrid();
				/*try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{hm.clear();}*/
    		}
			
			if (pEvent.getSource().getName().equalsIgnoreCase("Original_validation")) {
    			/*hm.put("Original_validation","Clicked");
				popupFlag="N";*/
    			formObject.fetchFragment("Original_validation", "OriginalValidation", "q_OrigVal");
    			//loadPicklist3();
    			//loadInDecGrid();
				/*try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{hm.clear();}*/
    		}
			
			if (pEvent.getSource().getName().equalsIgnoreCase("Compliance")) {
    			hm.put("Compliance","Clicked");
				formObject.fetchFragment("Compliance", "Compliance", "q_Compliance");
    			 SKLogger_CC.writeLog("CC value of world check row","after value of n Compliance_row");
             	/*List<String> Compliance_row = new ArrayList<String>(); 
             	Compliance_row.add("0");
             	Compliance_row.add("");
             	Compliance_row.add("");
             	//Compliance_row.add("11/11/2011");
             	Compliance_row.add("");
             	Compliance_row.add("");
             	Compliance_row.add("");
             	Compliance_row.add("");
             	Compliance_row.add("");
             	Compliance_row.add("");
             	Compliance_row.add("");
             	Compliance_row.add("");
             	Compliance_row.add("");
             	Compliance_row.add("");
             	Compliance_row.add("");
             	Compliance_row.add("");
             	Compliance_row.add("");
             	Compliance_row.add("");
             	Compliance_row.add("");
             	Compliance_row.add("");
             	Compliance_row.add("");
             	Compliance_row.add("");
             	Compliance_row.add("");
             	Compliance_row.add("");
             	Compliance_row.add("");
             	Compliance_row.add("");
             	Compliance_row.add("");
             	Compliance_row.add("");
             	Compliance_row.add("");
             	//Compliance_row.add("10/18/2019");
             	//Compliance_row.add("10/28/2019");
             	Compliance_row.add("");
             	Compliance_row.add("");
             	Compliance_row.add("Compliance_Winame");
             	formObject.addItem("cmplx_Compliance_cmplx_gr_compliance",Compliance_row);*/
            	//SKLogger_CC.writeLog("CC value of world check row","begore value of n Compliance_row"+Compliance_row.toString());
    			//loadPicklist3();
    			//loadInDecGrid();
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{hm.clear();}
    		}
			
			 if (pEvent.getSource().getName().equalsIgnoreCase("Limit_Increase")) {
     			hm.put("Limit_Increase","Clicked");
					formObject.fetchFragment("Limit_Increase", "Limit_Inc", "");
     			//loadPicklist3();
     			//loadInDecGrid();
					try
					{
						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
					}
					finally{hm.clear();}
     		}
			 if (pEvent.getSource().getName().equalsIgnoreCase("CC_Creation")) {
     			hm.put("CC_Creation","Clicked");
					formObject.fetchFragment("CC_Creation", "CC_Creation", "");
     			//Tanshu Aggarwal Disbursal work 14/08/2017
    			if(formObject.getNGValue("Is_NEW_CREADITCARD_REQ").equalsIgnoreCase("Y")){
    				formObject.setEnabled("CC_Creation_Button2", false);
    			}
    			else{
    				formObject.setEnabled("CC_Creation_Button2", true);
    				
    			}
    			//Tanshu Aggarwal Disbursal work 14/08/2017
     			//loadPicklist3();
     			//loadInDecGrid();
					try
					{
						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
					}
					finally{hm.clear();}
     		}
			 if (pEvent.getSource().getName().equalsIgnoreCase("CAD_Decision")) {
     			hm.put("CAD_Decision","Clicked");
					formObject.fetchFragment("CAD_Decision", "CAD_Decision", "q_CadDecision");
     			//loadPicklist3();
     			//loadInDecGrid();
					try
					{
						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
					}
					finally{hm.clear();}
     		}
			 //added by yash on 31/7/2017
			 if (pEvent.getSource().getName().equalsIgnoreCase("Card_Collection_Decision")) {
     			hm.put("Card_Collection_Decision","Clicked");
					formObject.fetchFragment("Card_Collection_Decision", "CardCollection", "q_CardCollection");
     			//loadPicklist3();
     			//loadInDecGrid();
					try
					{
						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
					}
					finally{hm.clear();}
     		}
			 
			 if (pEvent.getSource().getName().equalsIgnoreCase("Customer_Details_Verification1")) {
     			
     			formObject.fetchFragment("Customer_Details_Verification1", "CustDetailVerification1", "q_CustDetailVerification1");
     			//++Below code added by  nikhil 12/10/17 as per CC FSD 2.2
     			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_CustDetailverification1_MobNo1_veri","cmplx_CustDetailverification1_MobNo2_veri","CustDetailVerification1_Combo3","cmplx_CustDetailverification1_PO_Box_Veri","cmplx_CustDetailverification1_Emirates_veri","cmplx_CustDetailverification1_Off_Telephone_veri");
     	    	LoadPicklistVerification(LoadPicklist_Verification);
     			openDemographicFrags(formObject);
    			autopopulateValuesFCU(formObject);
    			//--above code added by  nikhil 12/10/17 as per CC FSD 2.2
     		
			 }
			 if (pEvent.getSource().getName().equalsIgnoreCase("Business_Verification1")) {
     			
     			formObject.fetchFragment("Business_Verification1", "BussinessVerification1", "q_bussverification1");
     			LoadPickList("cmplx_BussVerification_contctTelChk", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Other' ");
    			//12th sept
    			 SKLogger_CC.writeLog("PL", "Inside add button: business verification2 loadpicklist");
    			LoadPickList("cmplx_BussVerification1_CmpEmirate","select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_emirate order by Code");
    			autopopulateValuesBusinessVerification(formObject);
    			 SKLogger_CC.writeLog("PL", "Inside add button: business verification2 after function of autopopulate");
    			//12th sept
     			
     		}
			 if (pEvent.getSource().getName().equalsIgnoreCase("Employment_Verification")) {
     			
     			formObject.fetchFragment("Employment_Verification", "EmploymentVerification", "q_EmpVerification");
     			//++Below code added by  nikhil 12/10/17 as per CC FSD 2.2
     			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_EmploymentVerification_fixedsal_ver","cmplx_EmploymentVerification_AccomProvided_ver","cmplx_EmploymentVerification_Desig_ver","cmplx_EmploymentVerification_doj_ver","cmplx_EmploymentVerification_confirmedinJob_ver",
     			"cmplx_EmploymentVerification_LoanFromCompany_ver","cmplx_EmploymentVerification_PermanentDeductSal_ver","cmplx_EmploymentVerification_SmartCheck_ver","cmplx_EmploymentVerification_FiledVisitedInitiated_ver");
    			LoadPicklistVerification(LoadPicklist_Verification);
    			
     			//--above code added by  nikhil 12/10/17 as per CC FSD 2.2
     			//12th sept
     			 SKLogger_CC.writeLog("PL", "Inside add button: Employment_Verification loadpicklist");
     			LoadPickList("cmplx_EmploymentVerification_salaryTransferBank","select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_BankName with (nolock) where IsActive = 'Y' order by code");
     			 SKLogger_CC.writeLog("PL", "Inside add button: after Employment_Verification loadpicklist");
     			autopopulateValuesEmployeesVeri(formObject);
     			formObject.setTop("Banking_Check", formObject.getTop("Employment_Verification")+25);
    			formObject.setTop("Smart_Check1", formObject.getTop("Banking_Check")+25);
     			
     			//12th sept
     			
     		}
			 
			 if (pEvent.getSource().getName().equalsIgnoreCase("Banking_Check")) {
     			//12th sept q-variable name was missing
     			formObject.fetchFragment("Banking_Check", "BankingCheck", "q_BankingCheck"); 
     			//++Below code added by  nikhil 12/10/17 as per CC FSD 2.2
     			formObject.setEnabled("cmplx_BankingCheck_BankAccDetailUpdate", true);
     			formObject.setLocked("cmplx_BankingCheck_BankAccDetailUpdate", false);
     			formObject.setEnabled("BankingCheck_IFrame1", true);
     			//--above code added by  nikhil 12/10/17 as per CC FSD 2.2
     			
     			//12th sept q-variable name was missing
     		}
			 if (pEvent.getSource().getName().equalsIgnoreCase("Notepad_Details1")) {
     			
     			formObject.fetchFragment("Notepad_Details1", "NotepadDetails", "q_NotepadDetails");
     			
     		}
			 if (pEvent.getSource().getName().equalsIgnoreCase("supervisor_section")) {
     			
     			formObject.fetchFragment("supervisor_section", "supervisorsection", "q_Supervisor");
     			
     		}
			 if (pEvent.getSource().getName().equalsIgnoreCase("Smart_Check1")) {
     			
     			formObject.fetchFragment("Smart_Check1", "Smart_Check1", "q_smartcheck1");
     			
     		}
			 // fragments for fcu tab end
			 
			 // post disbursal
			
			 if (pEvent.getSource().getName().equalsIgnoreCase("Post_Disbursal")) {
     			
     			formObject.fetchFragment("Post_Disbursal", "PostDisbursal", "q_PostDisbursal");
     			
     		}
			
		}catch(Exception ex){
			SKLogger_CC.writeLog("CC_Common_code", "Exception in frameExpanded"+printException(ex));
		}
	}
	
	

	public void lockALOCfields() {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setLocked("cmplx_EmploymentDetails_EmpName",true);
		formObject.setLocked("cmplx_EmploymentDetails_EMpCode",true);
		formObject.setLocked("cmplx_EmploymentDetails_EmpIndusSector",true);
		formObject.setLocked("cmplx_EmploymentDetails_Indus_Macro",true);
		formObject.setLocked("cmplx_EmploymentDetails_Indus_Micro",true);
		formObject.setLocked("cmplx_EmploymentDetails_EmpStatusPL",true);
		formObject.setLocked("cmplx_EmploymentDetails_EmpStatusCC",true);
		formObject.setLocked("cmplx_EmploymentDetails_FreezoneName",true);
		formObject.setLocked("cmplx_EmploymentDetails_IncInCC",true);
		formObject.setLocked("cmplx_EmploymentDetails_IncInPL",true);
		formObject.setLocked("cmplx_EmploymentDetails_Emp_Categ_PL",true);
		formObject.setLocked("cmplx_EmploymentDetails_Status_PLNational",true);
		formObject.setLocked("cmplx_EmploymentDetails_Status_PLExpact",true);
		formObject.setLocked("cmplx_EmploymentDetails_ownername",true);
		formObject.setLocked("cmplx_EmploymentDetails_NOB",true);
		formObject.setLocked("EMploymentDetails_Combo34",true);
		formObject.setLocked("cmplx_EmploymentDetails_authsigname",true);
		formObject.setLocked("cmplx_EmploymentDetails_highdelinq",true);
		formObject.setLocked("cmplx_EmploymentDetails_dateinPL",true);
		formObject.setLocked("cmplx_EmploymentDetails_dateinCC",true);
		formObject.setLocked("cmplx_EmploymentDetails_remarks",true);
		formObject.setLocked("cmplx_EmploymentDetails_Aloc_RemarksPL",true);
	}
	public void hideLiabilityAddFields(FormReference formObject) {
		
		formObject.setVisible("Liability_New_Delinkinlast3months",false);
		formObject.setVisible("Liability_New_DPDinlast6",false);
		formObject.setVisible("Liability_New_DPDinlast18months",false);
		formObject.setVisible("Liability_New_Label4",false);
		formObject.setVisible("Liability_New_writeOfAmount",false);
		formObject.setVisible("Liability_New_Label5",false);
		formObject.setVisible("Liability_New_worstStatusInLast24",false);
		formObject.setVisible("Liability_New_Label10",false);
		formObject.setVisible("Liability_New_Text2",false);
	}



	public void autopopulateValues(FormReference formObject) {
		
		String mobNo1 = formObject.getNGValue("AlternateContactDetails_MobileNo1");
		String mobNo2 = formObject.getNGValue("AlternateContactDetails_MobNo2");
		String dob = formObject.getNGValue("cmplx_Customer_DOb");
		int adressrowcount = formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
		String poboxResidence = "";
		String poboxOffice = "";
		String homeadd = "";
		for(int i=0;i<adressrowcount;i++){
			String addType = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,0);
			if(addType.equalsIgnoreCase("RESIDENCE")){
				poboxResidence = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,8);
			}
			else if(addType.equalsIgnoreCase("OFFICE")){
				poboxOffice = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,8);
			}
			else if(addType.equalsIgnoreCase("Home")){
				String house = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,1);
				String build = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,2);
				String street = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,3);
				String landmark = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,4);
				homeadd = house+" "+build+" "+street+" "+landmark;
			}
		}
		String resNo = formObject.getNGValue("AlternateContactDetails_ResidenceNo");
		String officeNo = formObject.getNGValue("AlternateContactDetails_OfficeNo");
		String homeNo = formObject.getNGValue("AlternateContactDetails_HomeCOuntryNo");
		String email1 = formObject.getNGValue("AlternateContactDetails_Email1");
		String email2 = formObject.getNGValue("AlternateContactDetails_Email2");
		
		
		String query = "select Description from ng_master_Emirate where code = '"+formObject.getNGValue("cmplx_Customer_EmirateOfResidence")+"'";
		String emirate = "";
		List<List<String>> db = formObject.getDataFromDataSource(query);
		if(db!=null && db.get(0)!=null && db.get(0).get(0)!=null){
		 emirate= db.get(0).get(0); 
		}
		setValues(formObject,mobNo1,mobNo2,dob,poboxResidence,poboxOffice,resNo,officeNo,homeNo,homeadd,email1,email2,emirate);
		//String poBox = formObject.getNGValue("cmplx_CustDetailVerification_POBoxNo_val");
		
	}
	//++Below code added by  nikhil 12/10/17 as per CC FSD 2.2
public void autopopulateValuesFCU(FormReference formObject) {
		
		String mobNo1 = formObject.getNGValue("AlternateContactDetails_MobileNo1");
		String mobNo2 = formObject.getNGValue("AlternateContactDetails_MobNo2");
		String dob = formObject.getNGValue("cmplx_Customer_DOb");
		int adressrowcount = formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
		String poboxResidence = "";
		String emirate = "";
		
		for(int i=0;i<adressrowcount;i++)
		{
			String addType = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,0);
			if(addType.equalsIgnoreCase("HOME COUNTRY"))
			{
				poboxResidence = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,8);
				emirate = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,6);
			}			
			}
		//++Below code added by  nikhil 16/10/17 as per CC FSD 2.2
		try
		{
		List<List<String>> db=formObject.getDataFromDataSource("select Description from ng_master_Emirate where Code='"+emirate+"'") ;
		if(db!=null && db.get(0)!=null && db.get(0).get(0)!=null){
			 emirate= db.get(0).get(0); 
			}
		}
		catch(Exception ex)
		{
			emirate="";
		}
		//--above code added by  nikhil 16/10/17 as per CC FSD 2.2
		
		String officeNo = formObject.getNGValue("AlternateContactDetails_OfficeNo");
		
		String emirateid=formObject.getNGValue("cmplx_Customer_EmiratesID");
		String cifid=formObject.getNGValue("cmplx_Customer_CIFNo");
		formObject.setNGValue("cmplx_CustDetailverification1_EmiratesId", emirateid);
		formObject.setNGValue("cmplx_CustDetailverification1_CID_ID", cifid);
		formObject.setLocked("cmplx_CustDetailverification1_EmiratesId", true);
		formObject.setLocked("cmplx_CustDetailverification1_CID_ID", true);
		
		setValuesFCU(formObject,mobNo1,mobNo2,dob,poboxResidence,emirate,officeNo,emirate );
		//String poBox = formObject.getNGValue("cmplx_CustDetailVerification_POBoxNo_val");
		
	}
//--above code added by  nikhil 12/10/17 as per CC FSD 2.2
//12th sept
	public void autopopulateValuesBusinessVerification(FormReference formObject) {
		SKLogger_CC.writeLog("PL", "Inside add button: business verification2 b$ function of autopopulateValuesBusinessVerification");
	
		int framestate1=formObject.getNGFrameState("Alt_Contact_container");
		if(framestate1 != 0){
			formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
		}
		int framestate2=formObject.getNGFrameState("CompDetails");
		if(framestate2 !=0){
			formObject.fetchFragment("CompDetails", "CompanyDetails", "q_CompanyDetails");	
		}
		int framestate3=formObject.getNGFrameState("Auth_Sign_Details");
		if(framestate3 !=0){
			formObject.fetchFragment("Auth_Sign_Details", "AuthorisedSignDetails", "q_AuthorisedSignDetails");
		}
		//++Below code added by  nikhil 12/10/17 as per CC FSD 2.2
		if(formObject.getNGFrameState("CompDetails") == 1)
		{
		formObject.setNGValue("cmplx_BussVerification1_TradeLicenseNo",formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",0,0));
		formObject.setLocked("cmplx_BussVerification1_TradeLicenseNo", true);
		}
		if(formObject.getNGFrameState("Auth_Sign_Details") == 1)
		{
		formObject.setNGValue("cmplx_BussVerification1_Signatory_Name",formObject.getNGValue("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails",0,1));
		formObject.setLocked("cmplx_BussVerification1_Signatory_Name", true);
		}
		if(formObject.getNGFrameState("Alt_Contact_container") == 1)
		{
		formObject.setNGValue("cmplx_BussVerification1_ContactTelephone",formObject.getNGValue("AlternateContactDetails_OfficeNo"));
		formObject.setLocked("cmplx_BussVerification1_ContactTelephone", true);
		}
		//--above code added by  nikhil 12/10/17 as per CC FSD 2.2
		SKLogger_CC.writeLog("bussverfivation@nikhil", "company framestate:"+formObject.getNGFrameState("CompDetails")+ "value :"+formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",0,4));
		SKLogger_CC.writeLog("bussverfivation@nikhil", "auth sign framestate:"+formObject.getNGFrameState("Auth_Sign_Details")+ "value :"+formObject.getNGValue("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails",0,1));
		SKLogger_CC.writeLog("bussverfivation@nikhil", "alconct framestate:"+formObject.getNGFrameState("Alt_Contact_container")+ "value :"+formObject.getNGValue("AlternateContactDetails_OfficeNo"));
		
		SKLogger_CC.writeLog("PL", "Inside add button: business verification2 after function of autopopulateValuesBusinessVerification");
		SKLogger_CC.writeLog("PL", "Inside add button: business verification2 b$ function of autopopulateValuesBusinessVerification");
		}
	public void autopopulateValuesEmployeesVeri(FormReference formObject){
		SKLogger_CC.writeLog("PL", "Inside add button: business verification2 b$ function of autopopulateValuesBusinessVerification");
		
		int framestate1=formObject.getNGFrameState("Alt_Contact_container");
		if(framestate1 != 0){
			formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
		}
		int framestate2=formObject.getNGFrameState("IncomeDEtails");
		if(framestate2 != 0){
			formObject.fetchFragment("IncomeDEtails", "IncomeDetails", "q_IncomeDetails");
		}
		int framestate3=formObject.getNGFrameState("EmploymentDetails");
		if(framestate3 != 0){
			formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmpDetails");
		}
		int framestate4=formObject.getNGFrameState("Customer_Details_Verification1");
		if(framestate4 !=0){
			formObject.fetchFragment("Customer_Details_Verification1", "CustDetailVerification1", "q_CustDetailVeriFCU");
		}
		//++Below code added by  nikhil 13/10/17 as per CC FSD 2.2
		formObject.setNGValue("cmplx_EmploymentVerification_OffTelNo",formObject.getNGValue("AlternateContactDetails_OfficeNo"));
		formObject.setLocked("cmplx_EmploymentVerification_OffTelNo", true);
		formObject.setNGValue("cmplx_EmploymentVerification_fixedsal_val",formObject.getNGValue("cmplx_IncomeDetails_netSal1"));
		formObject.setLocked("cmplx_EmploymentVerification_fixedsal_val", true);
		formObject.setNGValue("cmplx_EmploymentVerification_AccomProvided_val",formObject.getNGValue("cmplx_IncomeDetails_Accomodation"));
		formObject.setLocked("cmplx_EmploymentVerification_AccomProvided_val", true);
		formObject.setNGValue("cmplx_EmploymentVerification_doj_val",formObject.getNGValue("cmplx_EmploymentDetails_DOJ"));
		formObject.setLocked("cmplx_EmploymentVerification_doj_val", true);
		formObject.setNGValue("cmplx_EmploymentVerification_Desig_val",formObject.getNGValue("cmplx_EmploymentDetails_Designation"));
		formObject.setLocked("cmplx_EmploymentVerification_Desig_val", true);
		formObject.setNGValue("cmplx_EmploymentVerification_FiledVisitedInitiated_value",formObject.getNGValue("cmplx_CustDetailverification1_Filed_Visit_value"));
		formObject.setLocked("cmplx_EmploymentVerification_FiledVisitedInitiated_value", true);
		String ConfirmedInJob=formObject.getNGValue("cmplx_EmploymentDetails_JobConfirmed");
		if(ConfirmedInJob.equalsIgnoreCase("false")){
			formObject.setNGValue("cmplx_EmploymentVerification_confirmedinJob_val","No");
			SKLogger_CC.writeLog("PL","in false");
		}
		else{
			formObject.setNGValue("cmplx_EmploymentVerification_confirmedinJob_val","Yes");
			SKLogger_CC.writeLog("PL","in true");
		}
		formObject.setLocked("cmplx_EmploymentVerification_confirmedinJob_val", true);
		
		formObject.setNGFrameState("Alt_Contact_container",1);
		formObject.setNGFrameState("Customer_Details_Verification1", 1);
		/*formObject.setHeight("Employment_Verification",formObject.getTop("EmploymentVerification_Frame1")+formObject.getHeight("EmploymentVerification_Frame1")+5);
		formObject.setTop("Banking_Check", formObject.getTop("Employment_Verification")+formObject.getHeight("Employment_Verification")+15);
		formObject.setTop("Smart_Check1", formObject.getTop("Banking_Check")+25);*/
		
		
		//--above code added by  nikhil 13/10/17 as per CC FSD 2.2
	}
	//12th sept

	public void setValues(FormReference formObject,String...values) {
		String[] controls = new String[]{"cmplx_CustDetailVerification_Mob_No1_val","cmplx_CustDetailVerification_Mob_No2_val","cmplx_CustDetailVerification_dob_val",
				"cmplx_CustDetailVerification_POBoxNo_val","cmplx_CustDetailVerification_persorcompPOBox_val","cmplx_CustDetailVerification_Resno_val","cmplx_CustDetailVerification_Offtelno_val",
				"cmplx_CustDetailVerification_hcountrytelno_val","cmplx_CustDetailVerification_hcountryaddr_val","cmplx_CustDetailVerification_email1_val","cmplx_CustDetailVerification_email2_val"
				,"cmplx_CustDetailVerification_emirates_val"};
		int i=0;
		for(String str : values){
			if(checkValue(str)){
				formObject.setNGValue(controls[i], str);
				formObject.setLocked(controls[i], true);
				
			}
			else{
				SKLogger_CC.writeLog("CC Initiation", "value received at index "+i+" is: "+str);
			}
			i++;
		}
	}
	//++Below code added by  nikhil 12/10/17 as per CC FSD 2.2
	public void setValuesFCU(FormReference formObject,String...values) {
		String[] controls = new String[]{"cmplx_CustDetailverification1_MobNo1_value","cmplx_CustDetailverification1_MobNo2_value","cmplx_CustDetailverification1_DOB_value",
				"cmplx_CustDetailverification1_PO_Box_Value","cmplx_CustDetailverification1_Emirates_value","cmplx_CustDetailverification1_Off_Telephone_value"
				};
		int i=0;
		for(String str : values){
			if(checkValue(str)){
				formObject.setNGValue(controls[i], str);
				//formObject.setLocked(controls[i], true);
				SKLogger_CC.writeLog("FCU@nikhil",controls[i]+"::"+ str);
				
			}
			else{
				SKLogger_CC.writeLog("CC Initiation", "value received at index "+i+" is: "+str);
			}
			formObject.setLocked(controls[i], true);
			i++;
		}
	}
	//--above code added by  nikhil 12/10/17 as per CC FSD 2.2
	
	public boolean checkValue(String ngValue){
		if(ngValue==null ||ngValue.equalsIgnoreCase("") ||ngValue.equalsIgnoreCase(" ") || ngValue.equalsIgnoreCase("--Select--") || ngValue.equalsIgnoreCase("false")){
			return false;
		}
		return true;
	}


	public void setFormHeader(FormEvent pEvent){

        FormReference formObject = FormContext.getCurrentInstance().getFormReference();
        String activityName = formObject.getWFActivityName();
        try{
            
            SKLogger_CC.writeLog("CC Initiation", "Inside formPopulated()" + pEvent.getSource());
            formObject.fetchFragment("CustomerDetails", "Customer", "q_Customer");
			formObject.setNGFrameState("CustomerDetails",0);
			  DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

			String nationality = formObject.getNGValue("cmplx_Customer_Nationality");
			SKLogger_CC.writeLog("CC", "Nationality is$$$$: "+nationality);
			if(!nationality.equalsIgnoreCase("AE")){
				formObject.setLocked("cmplx_Customer_masroomID",true);
			}
            formObject.setNGValue("WiLabel",formObject.getWFWorkitemName());
            
            formObject.setNGValue("UserLabel",formObject.getUserName()); 
            formObject.setNGValue("IntroDateLabel",formObject.getNGValue("CreatedDate"));   
            formObject.setNGValue("InitChannelLabel",formObject.getNGValue("InitiationType")); 
            formObject.setNGValue("CC_Wi_Name",formObject.getWFWorkitemName());
            formObject.setNGValue("MobileLabel",formObject.getNGValue("cmplx_Customer_MobileNo")); 
            formObject.setNGValue("CifLabel",formObject.getNGValue("cmplx_Customer_CIFNo")); 	
			formObject.setNGValue("CRNLabel",formObject.getNGValue("CRN")); 
			/*if (formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 6).equalsIgnoreCase("Self Employed")){
				formObject.setNGValue("Cmp_Emp_Label",formObject.getNGValue("compName"));//Arun (11/10)
			}
			else {
				formObject.setNGValue("Cmp_Emp_Label",formObject.getNGValue("compName"));//Arun (11/10)
			}*/
			formObject.setNGValue("AppRefLabel",formObject.getNGValue("AppRefNo"));//Arun (11/10)
            Date date = new Date();
            formObject.setNGValue("CurrentDateLabel",dateFormat.format(date)); 
			
			formObject.setNGValue("RmTlNameLabel", formObject.getNGValue("RM_Name")); 
          //  formObject.setNGValue("Cmp_Emp_Label",formObject.getNGValue("Employer_Name"));
            // ++ above code already present - 06-10-2017
            String fName = formObject.getNGValue("cmplx_Customer_FirstNAme");
            String mName = formObject.getNGValue("cmplx_Customer_MiddleNAme");
            String lName = formObject.getNGValue("cmplx_Customer_LastNAme");
            
            String fullName = fName+" "+mName+" "+lName; 
            formObject.setNGValue("CustomerLabel",fullName);
            List<String> activity_openFragmentsonLoad = Arrays.asList("Hold_CPV","HR","Interest_Rate_Approval","Manager","Original_Validation","Post_Disbursal","Rejected_Application"
            ,"Rejected_queue","RM","DSA_CSO_Review","Sales_Approver","SalesCoordinator","Smart_CPV","Telesales_Agent_Review","Telesales_Agent","Telesales_RM","TO_Team","CardCollection","Collection_Agent_Review"
            ,"Compliance","CPV_Analyst","CPV","CSM_Review","CSM","DDVT_Checker","Disbursal","FCU","Fulfillment_RM","CAD_Analyst1","Cad_Analyst2","DDVT_Maker");
            
            List<String> activity_setVisible_CPV_Frames = Arrays.asList("Hold_CPV","Smart_CPV","CPV_Analyst","CPV");
            if(activity_openFragmentsonLoad.contains(activityName)){
            	openFragmentsonLoad(formObject,activityName);
            }
            if(activity_setVisible_CPV_Frames.contains(activityName)){
            	setVisible_CPV_Frames(formObject);
            }
            SKLogger_CC.writeLog("RLOS", "Just before calling company details function");
            checkComanyDetailsTabVisibility(formObject);
            checkSecNatapp(formObject);
          //added by abhishek as per CC FSD
            if(activityName.equalsIgnoreCase("Disbursal")){
            	 
   	    	 formObject.setVisible("Loan_Disbursal", false);
   	    	 formObject.setTop("CC_Creation", 30);
   	    	 formObject.setTop("Limit_Increase", formObject.getTop("CC_Creation")+formObject.getHeight("CC_Creation")+5);
            }
				// ++ below code already present - 06-10-2017
			//added by nikhil as per CC FSD
            if(activityName.equalsIgnoreCase("Smart_CPV"))
            {
            	formObject.setVisible("Notepad_Details", false);
            	formObject.setTop("LoanCard_Details_Check",formObject.getTop("Business_Verification")+formObject.getHeight("Business_Verification")+10);
            	formObject.setTop("Smart_Check",formObject.getTop("LoanCard_Details_Check")+30);
            	SKLogger_CC.writeLog("Cc@nikhil", "Inside SMart_CPV Set header ");
            }
          //-- Above code added by abhishek as per CC FSD 2.7.3
            //++Below code added by  nikhil 12/10/17 as per CC FSD 2.2
            if(activityName.equalsIgnoreCase("FCU"))
            {
            	formObject.setVisible("Notepad_Details1", false);
            	formObject.setVisible("supervisor_section", false);
            	formObject.setTop("Smart_Check1",formObject.getTop("Banking_Check")+formObject.getHeight("Banking_Check")+10);
            	
            }
        }catch(Exception e)
        {
            SKLogger_CC.writeLog("CC Initiation", "Exception:"+e.getMessage());
        }
	}
	
	public void checkSecNatapp(FormReference formObject) {
		String secnatRadio = formObject.getNGValue("cmplx_Customer_SecNAtionApplicable");
		
		if(secnatRadio.equalsIgnoreCase("Yes")){
			if(formObject.isLocked("cmplx_Customer_SecNationality")){
				formObject.setLocked("cmplx_Customer_SecNationality",false);
			}
		}
		else if(secnatRadio.equalsIgnoreCase("No")){
			if(!formObject.isLocked("cmplx_Customer_SecNationality")){
				formObject.setLocked("cmplx_Customer_SecNationality",true);
			}
		}
		
	}

	
	public void openFragmentsonLoad(FormReference formObject , String activityName){
		try{
			formObject.setNGFrameState("CustomerDetails",0);
			formObject.fetchFragment("ProductContainer", "Product", "q_product");
			formObject.setNGFrameState("ProductContainer",0);       
			formObject.setNGValue("QueueLabel","CC_"+activityName);
			formObject.setNGValue("LoanLabel",formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,3));
			formObject.setNGValue("ProductLabel",formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0));
			formObject.setNGValue("SubProductLabel",formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2));
			formObject.setNGValue("CardLabel",formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,4));
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
			LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
			LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
			if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("IM")){
                formObject.setVisible("Product_Label5",true);
                formObject.setVisible("ReqTenor",true);
                }

		}catch(Exception ex){
			SKLogger_CC.writeLog("formHeaderPart2", "Exception:"+ex.getMessage());
		}
	}
	
	public void setVisible_CPV_Frames(FormReference formObject){
		try{
		//formObject.fetchFragment("ProductContainer", "Product", "q_product");
        //formObject.setNGFrameState("ProductContainer", 0);
        int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		for(int i=0;i<n;i++){
        String strSubprod = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2);
	        if(strSubprod.equalsIgnoreCase("Business titanium Card")){
	        	formObject.setVisible("Guarantor_Verification", false);
	        	formObject.setVisible("Office_Mob_Verification", false);
	        	formObject.setVisible("Reference_Detail_Verification", false);
	        	formObject.setVisible("HomeCountry_Verification", false);
	        	formObject.setVisible("ResidenceVerification", false);
	        	break;
	        }
	        else if(strSubprod.equalsIgnoreCase("Instant Money")){
	        	formObject.setVisible("Business_Verification", false);
	        	formObject.setVisible("HomeCountry_Verification", false);
	        	formObject.setVisible("ResidenceVerification", false);
	        	formObject.setVisible("Guarantor_Verification", false);
	        	formObject.setVisible("Reference_Detail_Verification", false);
	        	break;
	        }
	        else if(strSubprod.equalsIgnoreCase("Salaried Credit Card") || strSubprod.equalsIgnoreCase("Self Employed Credit Card")){
	        	formObject.setVisible("Guarantor_Verification", false);
	        	break;
	        	
	        }
		}
		}
		catch(Exception ex){
			SKLogger_CC.writeLog("formHeaderPart3", "Exception:"+ex.getMessage());
		}
	}
	
	public String makeInputForGrid(String customerName) {
		String temp = "<ListItem><SubItem>"+customerName+"</SubItem>"
		+"<SubItem>Authorised signatory</SubItem>"
		+"<SubItem>Primary</SubItem>";
		
		for(int i=0;i<28;i++){
			temp+= "<SubItem></SubItem>";
		}
		
		temp+="</ListItem>";
		
		return temp;
	}
	
	public void openDemographicFrags(FormReference formObject){
		
		int framestate0=formObject.getNGFrameState("Address_Details_container");
		if(framestate0 != 0){
			formObject.fetchFragment("Address_Details_container", "AddressDetails", "q_AddressDetails");
			//formObject.setTop("Alt_Contact_container",formObject.getTop("Address_Details_Container")+formObject.getHeight("Address_Details_Container"));
			formObject.setTop("Alt_Contact_container",formObject.getTop("Address_Details_Container")+formObject.getHeight("Address_Details_Container")); 
			formObject.setTop("Reference_Details",formObject.getTop("Alt_Contact_container")+25);
            //formObject.setTop("Supplementary_Container",formObject.getTop("ReferenceDetails")+50);
            formObject.setTop("Card_Details", formObject.getTop("Reference_Details")+25);
            formObject.setTop("FATCA", formObject.getTop("Card_Details")+30);
				formObject.setTop("KYC", formObject.getTop("FATCA")+20);
				formObject.setTop("OECD", formObject.getTop("KYC")+20);
		}
		int framestate1=formObject.getNGFrameState("Alt_Contact_container");
		if(framestate1 != 0){
			formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
			formObject.setTop("Alt_Contact_container",formObject.getTop("Address_Details_Container")+formObject.getHeight("Address_Details_Container"));
            //formObject.setTop("Supplementary_Container",formObject.getTop("ReferenceDetails")+250);
			formObject.setTop("Reference_Details",formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container"));
            formObject.setTop("Card_Details", formObject.getTop("Reference_Details")+20);
            formObject.setTop("FATCA", formObject.getTop("Card_Details")+30);
			formObject.setTop("KYC", formObject.getTop("FATCA")+20);
			formObject.setTop("OECD", formObject.getTop("KYC")+20);
        }
		//++Below code added by  nikhil 12/10/17 as per CC FSD 2.2
		int framestate4=formObject.getNGFrameState("CustomerDetails");
		if(framestate4 != 0)
		{
			formObject.fetchFragment("CustomerDetails", "Customer", "q_Customer");
		}
		//--above code added by  nikhil 12/10/17 as per CC FSD 2.2
	}
	
	public void disablePartnerDetails(FormReference formObject){
		int authgridRowCount = formObject.getLVWRowCount("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails");
		SKLogger_CC.writeLog("inside partner details","authgridrowcount"+authgridRowCount);
		if(authgridRowCount >0){
			boolean flag = false;
			for(int i=0;i<authgridRowCount;i++){
				String soleEmployee = formObject.getNGValue("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails", i, 10);
				SKLogger_CC.writeLog("inside partner details","soleEmployee"+soleEmployee);
				if(soleEmployee.equalsIgnoreCase("Yes")){
					SKLogger_CC.writeLog("inside partner details","soleEmployee is Yes!!!!!");
					flag = true;
				}
			}
			if(flag){
				formObject.setEnabled("Partner_Details",false);
			}
			else{
				formObject.setEnabled("Partner_Details",true);
			}
		}
	}
	
	public void checkComanyDetailsTabVisibility(FormReference formObject){
		
		//formObject.fetchFragment("ProductContainer", "Product", "q_product");
        //formObject.setNGFrameState("ProductContainer", 0);
        String empType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 6);
        SKLogger_CC.writeLog("CC_UAT Value Of empType is: ",empType );
		
		
		 if(empType.contains("Salaried")){
            SKLogger_CC.writeLog("CC_UAT when empType is SAL",empType.contains("Salaried")+"");

        	formObject.setSheetVisible("Tab1",1,false);

        }
        
        else if(empType.contains("Self Employed")){
            SKLogger_CC.writeLog("CC_UAT when empType is SE",empType.contains("Self Employed")+"");

        	formObject.setSheetVisible("Tab1",3,false);
        }
	}
	
}
