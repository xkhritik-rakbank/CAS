
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


import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.excp.CustomExceptionHandler;


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

		CreditCard.mLogger.info("Inside CC_CommonCode-->FrameExpandEvent()");
		if (pEvent.getSource().getName().equalsIgnoreCase("CustomerDetails")) {
			
			hm.put("CustomerDetails","Clicked");
			formObject.fetchFragment("CustomerDetails", "Customer", "q_Customer");
			loadPicklistCustomer();
			
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();}
			
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
			finally{
				hm.clear();}
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
			finally{
				hm.clear();}
		}	
		
		if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDEtails")) 
		{
			hm.put("Incomedetails","Clicked");
			formObject.fetchFragment("IncomeDEtails", "IncomeDetails", "q_IncomeDetails");
			
			//added 9/08/2017
			String subprod =formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2);
			String empType = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6);
			CreditCard.mLogger.info( "subprod"+subprod+",emptype:"+empType);
			if(subprod.equalsIgnoreCase("Business titanium Card")&& empType.equalsIgnoreCase("Self Employed")){
				CreditCard.mLogger.info( "inside if condition");
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
				CreditCard.mLogger.info( "Grid Data[1][9] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9));
				if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1).equalsIgnoreCase("Credit Card") && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")){
					CreditCard.mLogger.info( "Grid Data[1][9] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9));
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
				CreditCard.mLogger.info( "Emp Type Value is:"+EmpType);

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
		//	finally{
			//hm.clear();}					
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
				CreditCard.mLogger.info( "Inside Self Employed case for liabilities");
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
					CreditCard.mLogger.info( "Inside Salaried case for liabilities");
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
				CreditCard.mLogger.info( "Inside Salaried case for liabilities");
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
			finally{
				hm.clear();}
		}	
		
		
		if (pEvent.getSource().getName().equalsIgnoreCase("MiscFields")) {
			hm.put("MiscFields","Clicked");
			formObject.fetchFragment("MiscFields", "MiscellaneousFields", "q_MiscFields");
			
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();}
			
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
			  CreditCard.mLogger.info("credit card");
			if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1).equalsIgnoreCase("Credit Card")){
				CreditCard.mLogger.info("credit card inside if");
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
			CreditCard.mLogger.info(subProd);
			if(!subProd.equalsIgnoreCase("SEC")){
				CreditCard.mLogger.info("inside secured card");
				formObject.setVisible("ELigibiltyAndProductInfo_Frame3", false);
			}
			Eligibilityfields();
			
			//String activityName = formObject.getWFActivityName();
			CreditCard.mLogger.info(activityName);
			if(activityName.equalsIgnoreCase("CAD_Analyst1")){
				CreditCard.mLogger.info(activityName);
				formObject.setEnabled("ELigibiltyAndProductInfo_Button1",true);
	 		}
			LoadPickList("cmplx_EligibilCreditCard.mLogger.info(ayFreq", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_frequency with (nolock) order by code");
			LoadPickList("cmplx_EligibilityAndProductInfo_InterestType", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_InterestType with (nolock) order by code"); //Arun (17/10)
			
			/*try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
			hm.clear();}*/
		}	
		
		if (pEvent.getSource().getName().equalsIgnoreCase("Address_Details_container")) {
			hm.put("Address_Details_container","Clicked");
			formObject.fetchFragment("Address_Details_container", "AddressDetails", "q_AddressDetails");
			loadPicklist_Address();
			
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();}
		}	
		
		
		if (pEvent.getSource().getName().equalsIgnoreCase("Alt_Contact_container")) {
			hm.put("Alt_Contact_container","Clicked");
			formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
			LoadPickList("AlternateContactDetails_CustdomBranch", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock) order by code");
			//p2-49,Retain account if loan rejected tickbox should not apply for cards 
			//++ Below Code added By on Oct 6, 2017  to fix : "38-Retain account if loan rejected should not be there" : Reported By Shashank on Oct 05, 2017++
			CreditCard.mLogger.info("before invisibility");
			if(activityName.equalsIgnoreCase("DDVT_maker")){
			
			 formObject.setVisible("AlternateContactDetails_RetainaccifLoanreq_cont", false);
			}
			CreditCard.mLogger.info("after invisibility");
			//++ Above Code added By on Oct 6, 2017  to fix : "38-Retain account if loan rejected should not be there" : Reported By Shashank on Oct 05, 2017++
			//p2-49,Retain account if loan rejected tickbox should not apply for cards 
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();}
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
			finally{
				hm.clear();}
			
		}	
		
		if (pEvent.getSource().getName().equalsIgnoreCase("KYC")) 
		{
			hm.put("KYC","Clicked");
			formObject.fetchFragment("KYC", "KYC", "q_KYC");
			
			
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();}
			
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
			finally{
				hm.clear();}
			
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
			finally{
				hm.clear();}
			
		}
		
		if (pEvent.getSource().getName().equalsIgnoreCase("Self_Employed")) {
			hm.put("Self_Employed","Clicked");
			formObject.fetchFragment("Self_Employed", "SelfEmployed", "q_SelfEmployed");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();}
		}
		
		if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
			hm.put("DecisionHistory","Clicked");
			formObject.fetchFragment("DecisionHistory", "DecisionHistory", "q_DecisionHistory");
			//12th sept
			String cif= formObject.getNGValue("cmplx_Customer_CIFNo");
			String Emiratesid=formObject.getNGValue("cmplx_Customer_EmiratesID");
			String Custname= formObject.getNGValue("cmplx_Customer_FirstNAme")+""+formObject.getNGValue("cmplx_Customer_MiddleNAme")+""+formObject.getNGValue("cmplx_Customer_LastNAme");
			CreditCard.mLogger.info("Custname"+Custname);
			formObject.setNGValue("DecisionHistory_Text15",cif);
			formObject.setNGValue("DecisionHistory_Text16", Emiratesid);
			formObject.setNGValue("DecisionHistory_Text17",Custname);
			CreditCard.mLogger.info("inside csm ofdecision history value of cifid cust name and emirates"+ formObject.getNGValue("DecisionHistory_Text15")+","+ formObject.getNGValue("DecisionHistory_Text16")+","+ formObject.getNGValue("DecisionHistory_Text17"));
			//12th sept
			loadPicklist3();
//12th sept
			if(activityName.equalsIgnoreCase("CSM")){
				CreditCard.mLogger.info("inside csm ofdecision history");
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
				CreditCard.mLogger.info("after setting csm ofdecision history");
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
					CreditCard.mLogger.info( "Done button click::query result is::"+list );
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
					CreditCard.mLogger.info( "cpv dec value is::"+list1 );
					listValues +="#"+list1.get(0).get(0);
					CreditCard.mLogger.info( "list value is::"+listValues );
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
				CreditCard.mLogger.info("ProductDetailsLoader");
			}
			else {
				formObject.fetchFragment("ProductContainer", "Product", "q_product");
				CreditCard.mLogger.info("fetched product details");
				
			}
			
			
				int framestate1=formObject.getNGFrameState("IncomeDEtails");
				if(framestate1 == 0){
					CreditCard.mLogger.info("Incomedetails");
				}
				else {
					formObject.fetchFragment("IncomeDEtails", "IncomeDetails", "q_IncomeDetails");
					CreditCard.mLogger.info("fetched income details");
					formObject.setTop("Incomedetails",formObject.getTop("ProductDetailsLoader")+formObject.getHeight("ProductDetailsLoader"));
				}
				
				int framestate2=formObject.getNGFrameState("EmploymentDetails");
				if(framestate2 == 0){
					CreditCard.mLogger.info("EmploymentDetails");
				}
				else {
					formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmpDetails");
					CreditCard.mLogger.info("fetched employment details");
				}
				int framestate3=formObject.getNGFrameState("Address_Details_container");
				if(framestate3 == 0){
					CreditCard.mLogger.info("Address_Details_container");
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

					CreditCard.mLogger.info("fetched address details");
				}
				
				int framestate4=formObject.getNGFrameState("ReferenceDetails");
				if(framestate4 == 0){
					CreditCard.mLogger.info("Alt_Contact_container");
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
                        
					CreditCard.mLogger.info("fetched address details");
				}
				
				
				int framestate5=formObject.getNGFrameState("Alt_Contact_container");
				if(framestate5 == 0){
					CreditCard.mLogger.info("Alt_Contact_container");
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
                        
					CreditCard.mLogger.info("fetched address details");
				}
			
			loadInDecGrid();
			String activity = formObject.getWFActivityName();
			CreditCard.mLogger.info("activity"+activity);
			if(!activity.equalsIgnoreCase("CAD_Analyst1") && !activity.equalsIgnoreCase("Cad_Analyst2")){
				CreditCard.mLogger.info("activity"+activity);
				CreditCard.mLogger.info("gettop="+formObject.getTop("cmplx_DEC_Gr_DecisonHistory"));
				formObject.setTop("cmplx_DEC_Gr_DecisonHistory",460);
				CreditCard.mLogger.info("gettop1="+formObject.getTop("cmplx_DEC_Gr_DecisonHistory"));
				formObject.setTop("DecisionHistory_save",630);
				CreditCard.mLogger.info("gettop2="+formObject.getTop("DecisionHistory_save"));
			}
			if(!activity.equalsIgnoreCase("CPV") && !activity.equalsIgnoreCase("FCU")){
			formObject.setVisible("NotepadDetails_Frame3", false);
			}
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();}
		}	
		if (pEvent.getSource().getName().equalsIgnoreCase("Frame4"))
        {
            hm.put("IncomingDocuments","Clicked");
            CreditCard.mLogger.info("IncomingDocuments");
            //frame,fragment,q_variable
            formObject.fetchFragment("Frame4", "IncomingDocument", "q_IncDoc");
            CreditCard.mLogger.info("fetchIncomingDocRepeater");
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
            
            CreditCard.mLogger.info("formObject.fetchFragment1");
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
				finally{
					hm.clear();}
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
				finally{
				hm.clear();}*/
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
				finally{
					hm.clear();}
    		}
		 
		 if (pEvent.getSource().getName().equalsIgnoreCase("CompDetails")) {
 			hm.put("CompDetails","Clicked");
				formObject.fetchFragment("CompDetails", "CompanyDetails", "q_CompanyDetails");
				
				int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
				CreditCard.mLogger.info( "Inside else if of product self employed get rowcount for company details");
				for(int i=0;i<n;i++){
					if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("Business titanium Card") && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")){
					    CreditCard.mLogger.info( "inside btc");
						formObject.setVisible("CompanyDetails_Label8", true);
						formObject.setVisible("cmplx_CompanyDetails_cmplx_CompanyGrid_EffecLOB", true);//Effective length of buss
						//added 09/08/2017 to make company details disable
						//formObject.setEnabled("CompDetails",false);
						//added 09/08/2017 to make company details disable
						break;
					}
					
					else if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("Self Employed Credit Card") && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")){
						 CreditCard.mLogger.info( "inside self emp credit card");
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
					CreditCard.mLogger.info("Grid Data is"+Executexml);
					formObject.NGAddListItem("cmplx_CompanyDetails_cmplx_CompanyGrid",Executexml);
					}
					}catch(Exception ex){
						CreditCard.mLogger.info("Exception: "+printException(ex));
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
				finally{
					hm.clear();}
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
				finally{
					hm.clear();}
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
				finally{
					hm.clear();}
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
				finally{
					hm.clear();}
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("Part_Match")) {
				hm.put("Part_Match","Clicked");
				formObject.fetchFragment("Part_Match", "PartMatch", "q_PartMatch");
				
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{
					hm.clear();}
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("Finacle_CRM_Incidents")) {
				hm.put("Finacle_CRM_Incidents","Clicked");
				formObject.fetchFragment("Finacle_CRM_Incidents", "FinacleCRMIncident", "q_FinacleCRMIncident");
				
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{
					hm.clear();}
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("Finacle_CRM_CustomerInformation")) {
				hm.put("Finacle_CRM_CustomerInformation","Clicked");
				formObject.fetchFragment("Finacle_CRM_CustomerInformation", "FinacleCRMCustInfo", "q_FinacleCRMCustInfo");
				formObject.setNGValue("FinacleCRMCustInfo_WINAME",formObject.getWFWorkitemName());
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{
					hm.clear();}
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
				finally{
				hm.clear();}*/
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
	    				CreditCard.mLogger.info( "Data to be added in Grid account Grid: "+mylist.toString());
	    				formObject.addItemFromList("cmplx_FinacleCore_FinaclecoreGrid", mylist);
	    			 }
	    			
	    			query="select AcctId,LienId,LienAmount,LienRemarks,LienReasonCode,LienStartDate,LienExpDate from ng_rlos_FinancialSummary_LienDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
					//changed ended
	    			List<List<String>> list_lien=formObject.getDataFromDataSource(query);
	    			for(List<String> mylist : list_lien)
	    			 {
	    				CreditCard.mLogger.info( "Data to be added in Grid: "+mylist.toString());
	    				formObject.addItemFromList("cmplx_FinacleCore_liendet_grid", mylist);
	    			 }
					 //changed here in this query
					query="select AcctId,'','','','' from ng_rlos_FinancialSummary_SiDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
	    			List<List<String>> list_SIDet=formObject.getDataFromDataSource(query);
					//changed ended
	    			
	    			for(List<String> mylist : list_SIDet)
	    			 {
	    				CreditCard.mLogger.info( "Data to be added in Grid: "+mylist.toString());
	    				
	    				formObject.addItemFromList("cmplx_FinacleCore_sidet_grid", mylist);
	    			 }
	    			query="select '',AcctId,returntype,returnAmount,returnNumber,returnDate,retReasonCode,instrumentdate,returnType,'','','','','' from ng_rlos_FinancialSummary_ReturnsDtls with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
	    			List<List<String>> list_DDSDet=formObject.getDataFromDataSource(query);
	    			CreditCard.mLogger.info( "query to be added in list_DDSDet Grid: "+query);
    				
	    			//changed ended
	    			
	    			for(List<String> mylist : list_DDSDet)
	    			 {
	    				CreditCard.mLogger.info( "Data to be added in list_DDSDet Grid: "+mylist.toString());
	    				
	    				formObject.addItemFromList("cmplx_FinacleCore_DDSgrid", mylist);
	    			 }
	    			
	    			
	    			}
	    			catch(Exception e){
	    				CreditCard.mLogger.info( "Exception while setting data in grid:"+e.getMessage());
	    				throw new ValidatorException(new FacesMessage("Error while setting data in account grid"));
	    			}
				//}
				
				/*try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{
				hm.clear();}*/
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
				finally{
				hm.clear();}*/
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
				finally{
				hm.clear();}*/
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
				finally{
				hm.clear();}*/
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("Salary_Enquiry")) {
				/*hm.put("Salary_Enquiry","Clicked");
				popupFlag="N";*/
				formObject.fetchFragment("Salary_Enquiry", "SalaryEnq", "q_SalaryEnq");
				//if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6).equalsIgnoreCase("Salaried")){
				if(activityName.equalsIgnoreCase("DDVT_Maker")){
					formObject.setLocked("SalaryEnq_Frame1", true);
				}
				
					String query="select SalCreditDate,SalCreditMonth,AcctId,SalAmount,'' from ng_rlos_FinancialSummary_SalTxnDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
					CreditCard.mLogger.info( "Data to be added in account Grid account query: "+query);
					List<List<String>> list_acc=formObject.getDataFromDataSource(query);
	    			for(List<String> mylist : list_acc)
	    			 {
	    				CreditCard.mLogger.info( "Data to be added in account Grid account Grid: "+mylist.toString());
	    				formObject.addItemFromList("cmplx_SalaryEnq_SalGrid", mylist);
	    			 }
	    			
				
				
				
				//}
				
				/*try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{
				hm.clear();}*/
			}
			if (pEvent.getSource().getName().equalsIgnoreCase("PostDisbursal")) {
				hm.put("PostDisbursal","Clicked");
				formObject.fetchFragment("PostDisbursal", "PostDisbursal", "q_PostDisbursal");
				
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{
					hm.clear();}
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
				finally{
					hm.clear();}
			}
			//added by yash 23/08/2017
			if (pEvent.getSource().getName().equalsIgnoreCase("CAD_Decision")) {
				hm.put("CAD_Decision","Clicked");
				formObject.fetchFragment("CAD_Decision", "CAD_Decision", "q_CadDecision");
				
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{
					hm.clear();
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
				finally{
					hm.clear();}
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
				finally{
					hm.clear();}
    		}
			if (pEvent.getSource().getName().equalsIgnoreCase("CreditCard_Enq")) {
				hm.put("CreditCard_Enq","Clicked");
				formObject.fetchFragment("CreditCard_Enq", "CreditCardEnq", "q_CCenq");
				//fetchoriginalDocRepeater();
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{
					hm.clear();}
			}
			
			if (pEvent.getSource().getName().equalsIgnoreCase("Case_hist")) {
				hm.put("Case_hist","Clicked");
				formObject.fetchFragment("Case_hist", "CaseHistoryReport", "q_CaseHist");
				//fetchoriginalDocRepeater();
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{
					hm.clear();}
			}
			
			if (pEvent.getSource().getName().equalsIgnoreCase("LOS")) {
				hm.put("LOS","Clicked");
				formObject.fetchFragment("LOS", "LOS", "q_LOS");
				//fetchoriginalDocRepeater();
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{
					hm.clear();}
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
				finally{
					hm.clear();}
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
				finally{
					hm.clear();}
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
				finally{
					hm.clear();}
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
				finally{
					hm.clear();}
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
				finally{
					hm.clear();}
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
				finally{
					hm.clear();}
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
    				
    				
    				
    				 String EnableFlagValue = formObject.getNGValue("cmplx_OffVerification_EnableFlag");
    				 String sQuery =" SELECT ProcessInstanceID,lockStatus FROM WFINSTRUMENTTABLE WHERE activityname = 'CPV' AND ProcessInstanceID ='"+formObject.getWFWorkitemName()+"'";
    				 CreditCard.mLogger.info( "query is ::"+sQuery);
    				 List<List<String>> list=formObject.getDataFromDataSource(sQuery);
    				 CreditCard.mLogger.info( "list is ::"+list+"size is::"+list.size());
    				 
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
				finally{
					hm.clear();}
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
				finally{
					hm.clear();}
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
				finally{
					hm.clear();}
    		}
			
			if (pEvent.getSource().getName().equalsIgnoreCase("Smart_Check")) {
    			/*hm.put("Smart_Check","Clicked");
				popupFlag="N";*/
    			formObject.fetchFragment("Smart_Check", "SmartCheck", "q_SmartCheck");
    		
    		}
			
			if (pEvent.getSource().getName().equalsIgnoreCase("Original_validation")) {
    			/*hm.put("Original_validation","Clicked");
				popupFlag="N";*/
    			formObject.fetchFragment("Original_validation", "OriginalValidation", "q_OrigVal");
    			
    		}
			
			if (pEvent.getSource().getName().equalsIgnoreCase("Compliance")) {
    			hm.put("Compliance","Clicked");
				formObject.fetchFragment("Compliance", "Compliance", "q_Compliance");
    			 CreditCard.mLogger.info("after value of n Compliance_row");
             	
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{
					hm.clear();}
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
					finally{
						hm.clear();}
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
					finally{
						hm.clear();}
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
					finally{
						hm.clear();}
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
					finally{
						hm.clear();}
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
    			 CreditCard.mLogger.info( "Inside add button: business verification2 loadpicklist");
    			LoadPickList("cmplx_BussVerification1_CmpEmirate","select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_emirate order by Code");
    			autopopulateValuesBusinessVerification(formObject);
    			 CreditCard.mLogger.info( "Inside add button: business verification2 after function of autopopulate");
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
     			 CreditCard.mLogger.info( "Inside add button: Employment_Verification loadpicklist");
     			LoadPickList("cmplx_EmploymentVerification_salaryTransferBank","select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_BankName with (nolock) where IsActive = 'Y' order by code");
     			 CreditCard.mLogger.info( "Inside add button: after Employment_Verification loadpicklist");
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
			CreditCard.mLogger.info( "Exception in frameExpanded"+printException(ex));
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
		CreditCard.mLogger.info( "Inside add button: business verification2 b$ function of autopopulateValuesBusinessVerification");
	
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
		CreditCard.mLogger.info( "company framestate:"+formObject.getNGFrameState("CompDetails")+ "value :"+formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",0,4));
		CreditCard.mLogger.info( "auth sign framestate:"+formObject.getNGFrameState("Auth_Sign_Details")+ "value :"+formObject.getNGValue("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails",0,1));
		CreditCard.mLogger.info( "alconct framestate:"+formObject.getNGFrameState("Alt_Contact_container")+ "value :"+formObject.getNGValue("AlternateContactDetails_OfficeNo"));
		
		CreditCard.mLogger.info( "Inside add button: business verification2 after function of autopopulateValuesBusinessVerification");
		CreditCard.mLogger.info( "Inside add button: business verification2 b$ function of autopopulateValuesBusinessVerification");
		}
	public void autopopulateValuesEmployeesVeri(FormReference formObject){
		CreditCard.mLogger.info( "Inside add button: business verification2 b$ function of autopopulateValuesBusinessVerification");
		
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
			CreditCard.mLogger.info("in false");
		}
		else{
			formObject.setNGValue("cmplx_EmploymentVerification_confirmedinJob_val","Yes");
			CreditCard.mLogger.info("in true");
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
				CreditCard.mLogger.info( "value received at index "+i+" is: "+str);
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
				CreditCard.mLogger.info(controls[i]+"::"+ str);
				
			}
			else{
				CreditCard.mLogger.info( "value received at index "+i+" is: "+str);
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
            
            CreditCard.mLogger.info( "Inside formPopulated()" + pEvent.getSource());
            formObject.fetchFragment("CustomerDetails", "Customer", "q_Customer");
			formObject.setNGFrameState("CustomerDetails",0);
			  DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

			String nationality = formObject.getNGValue("cmplx_Customer_Nationality");
			CreditCard.mLogger.info( "Nationality is$$$$: "+nationality);
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
            CreditCard.mLogger.info( "Just before calling company details function");
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
            	CreditCard.mLogger.info( "Inside SMart_CPV Set header ");
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
            CreditCard.mLogger.info( "Exception:"+e.getMessage());
        }
	}
	
	public void checkSecNatapp(FormReference formObject) {
		String secnatRadio = formObject.getNGValue("cmplx_Customer_SecNAtionApplicable");
		
		if(secnatRadio.equalsIgnoreCase("Yes")){
			if(formObject.isLocked("cmplx_Customer_SecNationality")){
				formObject.setLocked("cmplx_Customer_SecNationality",false);
			}
		}
		else if(secnatRadio.equalsIgnoreCase("No") &&!formObject.isLocked("cmplx_Customer_SecNationality") ){
							formObject.setLocked("cmplx_Customer_SecNationality",true);
			
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
			CreditCard.mLogger.info( "Exception:"+ex.getMessage());
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
			CreditCard.mLogger.info( "Exception:"+ex.getMessage());
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
		CreditCard.mLogger.info("authgridrowcount"+authgridRowCount);
		if(authgridRowCount >0){
			boolean flag = false;
			for(int i=0;i<authgridRowCount;i++){
				String soleEmployee = formObject.getNGValue("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails", i, 10);
				CreditCard.mLogger.info("soleEmployee"+soleEmployee);
				if(soleEmployee.equalsIgnoreCase("Yes")){
					CreditCard.mLogger.info("soleEmployee is Yes!!!!!");
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
        CreditCard.mLogger.info(empType );
		
		
		 if(empType.contains("Salaried")){
            CreditCard.mLogger.info(empType.contains("Salaried")+"");

        	formObject.setSheetVisible("Tab1",1,false);

        }
        
        else if(empType.contains("Self Employed")){
           CreditCard.mLogger.info(empType.contains("Self Employed")+"");

        	formObject.setSheetVisible("Tab1",3,false);
        }
	}
	public void mouse_clicked(FormEvent pEvent) 
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
		String alert_msg="";
		String outputResponse = "";
		String SystemErrorCode="";
		
		String ReturnCode="";
		String ReturnDesc="";
		if("cmplx_Product_cmplx_ProductGrid".equalsIgnoreCase(pEvent.getSource().getName())){
			String ReqProd=formObject.getNGValue("ReqProd");
			CreditCard.mLogger.info("Value of ReqProd is:"+ReqProd);
			loadPicklistProduct(ReqProd);
		}

		else if ("AddressDetails_addr_Add".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setNGValue("Address_wi_name",formObject.getWFWorkitemName());
			CreditCard.mLogger.info( "Inside add button: "+formObject.getNGValue("Address_wi_name"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_AddressDetails_cmplx_AddressGrid");
		}
		else if ("BT_Add".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setNGValue("CC_Loan_wi_name",formObject.getWFWorkitemName());
			CreditCard.mLogger.info(formObject.getNGValue("CC_Loan_wi_name"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_CC_Loan_cmplx_btc");
			CreditCard.mLogger.info( "Inside add button33: add of btc details");

		}
		else if ("BT_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_CC_Loan_cmplx_btc");
			CreditCard.mLogger.info( "Inside add button33: modify of btc details");

		}
		else if ("BT_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_CC_Loan_cmplx_btc");
			CreditCard.mLogger.info( "Inside add button33: delete of btc details");
		}
		else if ("SmartCheck_Add".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setNGValue("SmartCheck_WiName",formObject.getWFWorkitemName());
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_SmartCheck_SmartCheckGrid");
			CreditCard.mLogger.info( "Inside add button33: add of SmartCheck_Add details");
		}
		else if ("SmartCheck_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setNGValue("SmartCheck_WiName",formObject.getWFWorkitemName());
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_SmartCheck_SmartCheckGrid");
			CreditCard.mLogger.info( "Inside add button33: modify of SmartCheck_Modify details");
		}
		else if ("SmartCheck_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_SmartCheck_SmartCheckGrid");
			CreditCard.mLogger.info( "Inside add button33: delete of SmartCheck_Delete details");

		}
		if("SmartCheck_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Smart_Check");
			alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL058");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		else if ("AddressDetails_addr_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_AddressDetails_cmplx_AddressGrid");
		}

		else if ("AddressDetails_addr_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_AddressDetails_cmplx_AddressGrid");

		}
		else if ("EMploymentDetails_Button2".equalsIgnoreCase(pEvent.getSource().getName())) {
			String EmpName=formObject.getNGValue("EMploymentDetails_Text21");
			String EmpCode=formObject.getNGValue("EMploymentDetails_Text22");
			CreditCard.mLogger.info( "EMpName$"+EmpName+"$");
			String query;
			//changes done to check duplicate selection compare emp code and main emp code main_employer_code column added - 06-09-2017
			if(EmpName.trim().equalsIgnoreCase(""))
				query="select distinct(EMPR_NAME),EMPLOYER_CODE,INCLUDED_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,MAIN_EMPLOYER_CODE from NG_RLOS_ALOC_OFFLINE_DATA where EMPLOYER_CODE Like '%"+EmpCode+"%'";


			else
				query="select distinct(EMPR_NAME),EMPLOYER_CODE,INCLUDED_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,MAIN_EMPLOYER_CODE from NG_RLOS_ALOC_OFFLINE_DATA where EMPR_NAME Like '%"+EmpName + "%' or EMPLOYER_CODE Like '%"+EmpCode+"'";


			CreditCard.mLogger.info( "query is: "+query);
			populatePickListWindow(query,"EMploymentDetails_Button2", "Employer Name,Employer Code,INCLUDED IN PL ALOC,INCLUDED IN CC ALOC,INDUSTRY SECTOR,INDUSTRY MACRO,INDUSTRY MICRO,CONSTITUTION,NAME OF FREEZONE AUTHORITY,MAIN EMPLOYER CODE", true, 20);			     
		}
		else if("IncomeDetails_Add".equalsIgnoreCase(pEvent.getSource().getName()))	
	 	{
		formObject.setNGValue("Inc_winame",formObject.getWFWorkitemName());
		CreditCard.mLogger.info( "Inside add button: "+formObject.getNGValue("IncomeDetails_wi_name"));
		formObject.ExecuteExternalCommand("NGAddRow", "cmplx_IncomeDetails_Cmplx_gr_IncomdeDetails");
		}
	else 	if("IncomeDetails_Modify".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_IncomeDetails_Cmplx_gr_IncomdeDetails");
		}
	else if("IncomeDetails_Delete".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_IncomeDetails_Cmplx_gr_IncomdeDetails");
		}
		else if("Customer_save".equalsIgnoreCase(pEvent.getSource().getName())){
			CreditCard.mLogger.info( "Inside Customer_save button: ");
			formObject.saveFragment("CustomerDetails");
		}

		else if("Product_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("ProductContainer");
		}
		//added by yash
		else if("FinacleCRMCustInfo_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("FinacleCRMCustInfo_Frame1");
		}
		else if("GuarantorDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("GuarantorDetails");
		}

		else if("IncomeDetails_Salaried_Save".equalsIgnoreCase(pEvent.getSource().getName())  ||  "IncomeDetails_SelfEmployed_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("IncomeDetails");
			alert_msg = NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL039");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if("CompanyDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("CompanyDetails");
			alert_msg = NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL008");

			throw new ValidatorException(new FacesMessage(alert_msg));

		}

		else if("PartnerDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("PartnerDetails");
		}

		else if("AuthorisedSignDetails_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("AuthorisedSignDetails_ShareHolding");
			alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL061");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if("SelfEmployed_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Liability_container");
		}
		//added by yash on 21/8/2017
		else if("SupplementCardDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Supplementary_Container");
			alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL059");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		else if("WorldCheck1_Button4".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("World_Check");
			alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL060");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		//added by yash on 21/8/2017
		//Added to add modify delete in the Liability grid
		if("ExtLiability_Button2".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
		}
		if("ExtLiability_Button3".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
		}
		if("ExtLiability_Button4".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
		}
		//Added to add modify delete in the Liability grid
		else if("PartnerDetails_add".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setNGValue("PartnerDetails_partner_winame",formObject.getWFWorkitemName());
			CreditCard.mLogger.info( "Inside add button: "+formObject.getNGValue("PartnerDetails_partner_winame"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_PartnerDetails_cmplx_partnerGrid");
		}

		else if("PartnerDetails_modify".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_PartnerDetails_cmplx_partnerGrid");
		}

		else if("PartnerDetails_delete".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_PartnerDetails_cmplx_partnerGrid");
		}

		else if("AuthorisedSignDetails_add".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setNGValue("AuthorisedSignDetails_AuthorisedSign_wiName",formObject.getWFWorkitemName());
			CreditCard.mLogger.info( "Inside add button: "+formObject.getNGValue("AuthorisedSignDetails_AuthorisedSign_wiName"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails");
		}

		else if("AuthorisedSignDetails_modify".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails");
		}

		else if("AuthorisedSignDetails_delete".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails");
		}

		//added by Akshay on 7/9/2017 for Fetch Company Details call on CAD

		else if("CompanyDetails_Button3".equalsIgnoreCase(pEvent.getSource().getName())){
			CreditCard.mLogger.info("CompanyDetails_Button3");
			outputResponse = GenerateXML("CUSTOMER_DETAILS","Corporation_CIF");
			
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			CreditCard.mLogger.info(ReturnCode);
		
			
		
			String BusinessIncDate = (outputResponse.contains("<BusinessIncDate>")) ? outputResponse.substring(outputResponse.indexOf("<BusinessIncDate>")+"</BusinessIncDate>".length()-1,outputResponse.indexOf("</BusinessIncDate>")):"";    
			CreditCard.mLogger.info(BusinessIncDate);
			
			if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode) ){
				formObject.setNGValue("Is_Customer_Details","Y");

				CreditCard.mLogger.info("value of company Details corporation"+formObject.getNGValue("Is_Customer_Details"));

				valueSetCustomer(outputResponse,"Corporation_CIF");  
				
				try{

					//String Date1=BusinessIncDate;
					//Comman method written to convert date.
					formObject.setNGValue("estbDate",formatDateFromOnetoAnother(BusinessIncDate,"yyyy-mm-dd","dd/mm/yyyy"),false);
					formObject.setNGValue("CompanyDetails_DatePicker1",formatDateFromOnetoAnother(formObject.getNGValue("CompanyDetails_DatePicker1"),"yyyy-mm-dd","dd/mm/yyyy"),false);
					formObject.setNGValue("TLExpiry",formatDateFromOnetoAnother(formObject.getNGValue("TLExpiry"),"yyyy-mm-dd","dd/mm/yyyy"),false);
					//CreditCard.mLogger.info(Date1);
					// ++ below code commented at offshore - 06-10-2017 date format different
					//SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-mm-dd");
					/*SimpleDateFormat sdf1=new SimpleDateFormat("dd-mm-yyyy");
					SimpleDateFormat sdf2=new SimpleDateFormat("dd/mm/yyyy");
					String Datechanged=sdf2.format(sdf1.parse(Date1));
					CreditCard.mLogger.info(Datechanged);
					formObject.setNGValue("estbDate",Datechanged,false);*/

					/*Date1 = formObject.getNGValue("CompanyDetails_DatePicker1");
					Datechanged=sdf2.format(sdf1.parse(Date1));
					CreditCard.mLogger.info(Datechanged);
					formObject.setNGValue("CompanyDetails_DatePicker1",Datechanged,false);*/

					/*Date1 = formObject.getNGValue("TLExpiry");
					Datechanged=sdf2.format(sdf1.parse(Date1));
					CreditCard.mLogger.info(Datechanged);
					formObject.setNGValue("TLExpiry",Datechanged,false);*/
					String lob = getYearsDifference(formObject,"CompanyDetails_DatePicker1");
					formObject.setNGValue("lob", lob);
					String corpName = formObject.getNGValue("compName");
					if(corpName!=null && !"".equalsIgnoreCase(corpName) && !" ".equalsIgnoreCase(corpName)){
						getDataFromALOC(formObject,corpName);
					}
				}
				catch(Exception ex){
					CreditCard.mLogger.info(printException(ex));
				}
				CreditCard.mLogger.info("corporation cif");
				CreditCard.mLogger.info(formObject.getNGValue("Is_Customer_Details"));
			}
			else{
				CreditCard.mLogger.info("Customer Details Corporation CIF is not generated");
				formObject.setNGValue("Is_Customer_Details","N");
			}
			CreditCard.mLogger.info(formObject.getNGValue("Is_Customer_Details"));
		}

		//ended by Akshay on 7/9/2017 for Fetch Company Details call on CAD

		//added by Akshay on 7/9/2017 for Fetch Authorised Details call on CAD
		else if("AuthorisedSignDetails_Button4".equalsIgnoreCase(pEvent.getSource().getName())){

			outputResponse = GenerateXML("CUSTOMER_DETAILS","Signatory_CIF");
			
			
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			CreditCard.mLogger.info(ReturnCode);
			ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
			CreditCard.mLogger.info(ReturnDesc);
			if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode)){
				formObject.setNGValue("Is_Customer_Details","Y");
				CreditCard.mLogger.info("value of Authorised_CIF"+formObject.getNGValue("Is_Customer_Details"));
				valueSetCustomer(outputResponse,"Signatory_CIF");    
				CreditCard.mLogger.info("Authorised_CIF is generated");
				CreditCard.mLogger.info(formObject.getNGValue("Is_Customer_Details"));
				//Comman method written to convert date.
				formObject.setNGValue("AuthorisedSignDetails_DOB",formatDateFromOnetoAnother(formObject.getNGValue("AuthorisedSignDetails_DOB"),"yyyy-mm-dd","dd/mm/yyyy"),false);
				formObject.setNGValue("AuthorisedSignDetails_PassportExpiryDate",formatDateFromOnetoAnother(formObject.getNGValue("AuthorisedSignDetails_PassportExpiryDate"),"yyyy-mm-dd","dd/mm/yyyy"),false);
				formObject.setNGValue("AuthorisedSignDetails_VisaExpiryDate",formatDateFromOnetoAnother(formObject.getNGValue("AuthorisedSignDetails_VisaExpiryDate"),"yyyy-mm-dd","dd/mm/yyyy"),false);
				/*String str_dob=formObject.getNGValue("AuthorisedSignDetails_DOB");

				String str_passExpDate=formObject.getNGValue("AuthorisedSignDetails_PassportExpiryDate");
				String str_visaExpDate=formObject.getNGValue("AuthorisedSignDetails_VisaExpiryDate");
				try{
					SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-mm-dd");
					SimpleDateFormat sdf2=new SimpleDateFormat("dd/mm/yyyy");
					if(str_dob!=null&&!str_dob.equalsIgnoreCase("")){
						String n_str_dob=sdf2.format(sdf1.parse(str_dob));
						CreditCard.mLogger.info(n_str_dob+"asd");
						formObject.setNGValue("AuthorisedSignDetails_DOB",n_str_dob,false);
					}
					if(str_passExpDate!=null&&!str_passExpDate.equalsIgnoreCase("")){
						formObject.setNGValue("AuthorisedSignDetails_PassportExpiryDate",sdf2.format(sdf1.parse(str_passExpDate)),false);
					}
					if(str_visaExpDate!=null&&!str_visaExpDate.equalsIgnoreCase("")){
						formObject.setNGValue("AuthorisedSignDetails_VisaExpiryDate",sdf2.format(sdf1.parse(str_visaExpDate)),false);
					}
				}catch(Exception ex){
					CreditCard.mLogger.info(printException(ex));
				}*/
			}
			else{
				CreditCard.mLogger.info("Customer Details is not generated");
				formObject.setNGValue("Is_Customer_Details","N");
			}
			CreditCard.mLogger.info(formObject.getNGValue("Is_Customer_Details"));
		}

		//ended by Akshay on 7/9/2017 for Fetch Authorised Details call on CAD


		else if("ELigibiltyAndProductInfo_Button1".equalsIgnoreCase(pEvent.getSource().getName()))
		{		formObject.setNGValue("DecCallFired","Eligibility");
		String Emp_type = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 6);
		if ("Salaried".equalsIgnoreCase(Emp_type)){
			formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmpDetails");
			emp_details();
		}
		else if ("Self Employed".equalsIgnoreCase(Emp_type)){
			formObject.fetchFragment("CompDetails", "CompanyDetails", "q_CompanyDetails");
			formObject.setTop("Auth_Sign_Details", formObject.getTop("CompDetails")+formObject.getHeight("CompDetails")+25);
			
			
			formObject.fetchFragment("Auth_Sign_Details", "AuthorisedSignDetails", "q_AuthorisedSignDetails");
			formObject.setTop("Partner_Details", formObject.getTop("Auth_Sign_Details")+formObject.getHeight("Auth_Sign_Details")+25);
			
		}
		
		formObject.fetchFragment("IncomeDEtails", "IncomeDetails", "q_IncomeDetails");
		income();
		formObject.fetchFragment("MOL", "MOL1", "q_CC_Loan");
		formObject.fetchFragment("World_Check", "WorldCheck1", "q_WorldCheck");	
		formObject.setTop("World_Check", formObject.getTop("MOL")+formObject.getHeight("MOL")+15);
		formObject.setTop("Reject_Enquiry", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+15);
		formObject.setTop("Salary_Enquiry", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+35);
		formObject.setTop("CreditCard_Enq", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+55);
		formObject.setTop("Case_hist", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+75);
		formObject.setTop("LOS", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+95);
		
		formObject.fetchFragment("MOL", "MOL1", "q_CC_Loan");
		outputResponse = GenerateXML("DECTECH","");
		//SKLogger.writeLog("$$After Generatexml for CallFired : "+CallFired,"");
		
		try{
			double LoanAmount=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
			double tenor=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor"));
			double RateofInt=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate"));
			
			String EMI=getEMI(LoanAmount,RateofInt,tenor);
			formObject.setNGValue("cmplx_EligibilityAndProductInfo_EMI", EMI==null||EMI.equalsIgnoreCase("")?"0":EMI);
			
		}
		catch(Exception e){
			CreditCard.mLogger.info(" Exception in EMI Generation");
		}
		//EMI Calcuation logic added below 24-Sept-2017 End
		

		SystemErrorCode =  (outputResponse.contains("<SystemErrorCode>")) ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"";
		CreditCard.mLogger.info(SystemErrorCode);
		if("".equalsIgnoreCase(SystemErrorCode)){
			valueSetCustomer(outputResponse,"");   
			alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL021");
			
			//formObject.setNGFrameState("EligibilityAndProductInformation", 1);
			formObject.RaiseEvent("WFSave");
			throw new ValidatorException(new FacesMessage(alert_msg));


		}
		else{
			alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL012");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}

	

		}

		//added By Akshay on 7/9/2017 for Dectech call on CAD decision	
		else if("DecisionHistory_calReElig".equalsIgnoreCase(pEvent.getSource().getName())){	
			formObject.setNGValue("DecCallFired","Decision");
			
			String Emp_type = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 6);
			if ("Salaried".equalsIgnoreCase(Emp_type)){
				formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmpDetails");
				emp_details();
			}
			else if ("Self Employed".equalsIgnoreCase(Emp_type)){
				formObject.fetchFragment("CompDetails", "CompanyDetails", "q_CompanyDetails");
				formObject.setTop("Auth_Sign_Details", formObject.getTop("CompDetails")+formObject.getHeight("CompDetails")+25);
				
				
				formObject.fetchFragment("Auth_Sign_Details", "AuthorisedSignDetails", "q_AuthorisedSignDetails");
				formObject.setTop("Partner_Details", formObject.getTop("Auth_Sign_Details")+formObject.getHeight("Auth_Sign_Details")+25);
    			
			}
			formObject.fetchFragment("IncomeDEtails", "IncomeDetails", "q_IncomeDetails");
			income();
			formObject.fetchFragment("MOL", "MOL1", "q_CC_Loan");
			formObject.fetchFragment("World_Check", "WorldCheck1", "q_WorldCheck");	
			formObject.setTop("World_Check", formObject.getTop("MOL")+formObject.getHeight("MOL")+15);
			formObject.setTop("Reject_Enquiry", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+15);
			formObject.setTop("Salary_Enquiry", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+35);
			formObject.setTop("CreditCard_Enq", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+55);
			formObject.setTop("Case_hist", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+75);
			formObject.setTop("LOS", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+95);
			outputResponse = GenerateXML("DECTECH","");
			

			SystemErrorCode =  (outputResponse.contains("<SystemErrorCode>")) ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"";
			CreditCard.mLogger.info(SystemErrorCode);
			if("".equalsIgnoreCase(SystemErrorCode)){
				valueSetCustomer(outputResponse,"");   
				alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL021");
				
				formObject.RaiseEvent("WFSave");
				throw new ValidatorException(new FacesMessage(alert_msg));				
			}
			else{
				alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL012");
				throw new ValidatorException(new FacesMessage(alert_msg));
			}
		}	
		//ended By Akshay on 7/9/2017 for Dectech call on CAD decision	

		else if("Liability_New_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("InternalExternalContainer");
		}
		//++ Below Code added By Yash on Oct 9, 2017  to fix : "to save liability" : Reported By Shashank on Oct 09, 2017++
		else if("ExtLiability_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Internal_External_Liability");
			alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL034");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		//++ Above Code added By Yash on Oct 9, 2017  to fix : "to save liability" : Reported By Shashank on Oct 09, 2017++
		else if("EmpDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("EmploymentDetails");
			alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL025");

			throw new ValidatorException(new FacesMessage(alert_msg));

		}
		//++ Below Code added By Yash on Oct 10, 2017  to fix : 10-"There should be only 1 save button below Partner details fragment.Similar to how it is at initiation" : Reported By Shashank on Oct 09, 2017++

		else if("CompanyDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("CompDetails");
			alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL008");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		//++ Above Code added By Yash on Oct 10, 2017  to fix : 10-"There should be only 1 save button below Partner details fragment.Similar to how it is at initiation" : Reported By Shashank on Oct 09, 2017++
		
		if(pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo_Save")){
			formObject.saveFragment("EligibilityAndProductInformation");
			alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL024");

			throw new ValidatorException(new FacesMessage(alert_msg));

		}
		
		if(pEvent.getSource().getName().equalsIgnoreCase("MiscellaneousFields_Save")){
			formObject.saveFragment("MiscFields");
			alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL043");

			throw new ValidatorException(new FacesMessage(alert_msg));

		}
		
		if(pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_Save")){
			formObject.saveFragment("Address_Details_container");
			alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL001");

			throw new ValidatorException(new FacesMessage(alert_msg));

		}
		
		if(pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails_Button1")){
			formObject.saveFragment("Alt_Contact_container");
			alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL009");

			throw new ValidatorException(new FacesMessage(alert_msg));

		}
		
		if(pEvent.getSource().getName().equalsIgnoreCase("CardDetails_save")){
			formObject.saveFragment("Supplementary_Cont");
			formObject.saveFragment("Card_Details");
			alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL007");

			throw new ValidatorException(new FacesMessage(alert_msg));

		}
		
		
		if(pEvent.getSource().getName().equalsIgnoreCase("FATCA_Save")){
			formObject.saveFragment("FATCA");
			alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL036");

			throw new ValidatorException(new FacesMessage(alert_msg));

		}
		
		if(pEvent.getSource().getName().equalsIgnoreCase("KYC_Save")){
			formObject.saveFragment("KYC");
			alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL041");

			throw new ValidatorException(new FacesMessage(alert_msg));

		}
		
		if(pEvent.getSource().getName().equalsIgnoreCase("OECD_Save")){
			formObject.saveFragment("OECD");
			alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL045");

			throw new ValidatorException(new FacesMessage(alert_msg));

		}
			//added by yash on 24/8/2017
		if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_SaveButton")){
			formObject.saveFragment("Notepad_Values");
			alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL044");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_save")){
			formObject.saveFragment("Notepad_Values");
			alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL044");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		if(pEvent.getSource().getName().equalsIgnoreCase("Reference_Details_save")){
			formObject.saveFragment("Reference_Details");
			alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL053");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		//added by yash on 8/8/2017
		else if("SalaryEnq_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("SalaryEnq_Frame1");
			alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL056");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		else if("FinacleCRMCustInfo_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("FinacleCRMCustInfo_Frame1");
			alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL037");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		else if("RejectEnq_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("RejectEnq_Frame1");
			alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL054");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		else if("ExternalBlackList_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("ExternalBlackList_Frame1");
			alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL035");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		//ended by yash on 8/8/2017


		//added by yash on 21/8/2017
		else if("DecisionHistory_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("DecisionHistory");
		}

		else if("SmartCheck1_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Smart_Check");
		}

		if("NotepadDetails_save".equalsIgnoreCase(pEvent.getSource().getName()) || "NotepadDetails_SaveButton".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Notepad_Values");
			alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL044");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		//below one is in use
		/*else if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_SaveButton")){
			formObject.saveFragment("Notepad_Values");
			alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL044");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}*/
		else if("DecisionHistory_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("DecisionHistory");
			alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL022");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		else if("SmartCheck1_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_SmartCheck1_SmartChkGrid_FCU");
		}
		else if("FinacleCore_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			//++Below code added by  nikhil 10/10/17
			formObject.setNGValue("FinacleCore_DDSWiname",formObject.getWFWorkitemName());
			//-- Above code added by  nikhil 10/10/17

			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_FinacleCore_DDSgrid");
		}
		else if("FinacleCore_Button2".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_FinacleCore_DDSgrid");
		}
		else if("FinacleCore_Button3".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_FinacleCore_DDSgrid");
		}
		else if("SmartCheck1_Add".equalsIgnoreCase(pEvent.getSource().getName())){
			//++Below code added by  nikhil 10/10/17
			formObject.setNGValue("SmartCheck1_WiName",formObject.getWFWorkitemName());
			//-- Above code added by  nikhil 10/10/17

			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_SmartCheck1_SmartChkGrid_FCU");
		}
		else if("CompanyDetails_Add".equalsIgnoreCase(pEvent.getSource().getName())){
			//++Below code added by  nikhil 10/10/17
			formObject.setNGValue("company_winame",formObject.getWFWorkitemName());
			//-- Above code added by  nikhil 10/10/17

			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_CompanyDetails_cmplx_CompanyGrid");
		}
		else if("CompanyDetails_delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_CompanyDetails_cmplx_CompanyGrid");
		}
		else if("CompanyDetails_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_CompanyDetails_cmplx_CompanyGrid");
		}

		else if("NotepadDetails_Add".equalsIgnoreCase(pEvent.getSource().getName())){
			//++Below code added by  nikhil 10/10/17
			formObject.setNGValue("NotepadDetails_winame",formObject.getWFWorkitemName());
			//-- Above code added by  nikhil 10/10/17

			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_notegrid");
			Notepad_add();
		}
		else if("NotepadDetails_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_NotepadDetails_cmplx_notegrid");
			Notepad_modify();
		}
		else if("NotepadDetails_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_NotepadDetails_cmplx_notegrid");
			Notepad_delete();
		}
		// added by abhishek as per CC FSD
		else if("cmplx_NotepadDetails_cmplx_notegrid".equalsIgnoreCase(pEvent.getSource().getName())){

			Notepad_grid();
		} 
		else if("FinacleCore_Button4".equalsIgnoreCase(pEvent.getSource().getName())){
			CreditCard.mLogger.info( "Inside Customer_finacle_add button: ");
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_FinacleCore_inwardtt");
		}
		else if("FinacleCore_Button5".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_FinacleCore_inwardtt");
		}
		else if("FinacleCore_Button6".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_FinacleCore_inwardtt");
		}
		else if("FinacleCore_Button7".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGClearRow", "cmplx_FinacleCore_inwardtt");
		}
		else if("SmartCheck1_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_SmartCheck1_SmartChkGrid_FCU");


		}
		

		// added by abhishek for repeater start
		else if ("FinacleCore_CalculateTotal".equalsIgnoreCase(pEvent.getSource().getName())){
			try {
				CreditCard.mLogger.info( "Inside Customer_save button: ");
				CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_JAN");	
				CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_FEB");	
				CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_MAR");	
				CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_APR");	
				CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_MAY");	
				CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_JUN");	
				CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_JUL");	
				CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_AUG");	
				CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_SEP");	
				CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_OCT");	
				CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_NOV");	
				CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_DEC");	


			} catch (Exception e) {
				CreditCard.mLogger.info( e.toString());
			}
		}

		else if ("FinacleCore_CalculateTurnover".equalsIgnoreCase(pEvent.getSource().getName())){
			try {
				CreditCard.mLogger.info( "Inside Customer_save button: ");
				CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_JAN1");	
				CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_FEB1");	
				CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_MAR1");	
				CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_APR1");	
				CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_MAY1");	
				CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_JUN1");	
				CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_JUL1");	
				CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_AUG1");	
				CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_SEP1");	
				CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_OCT1");	
				CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_NOV1");	
				CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_DEC1");	


			} catch (Exception e) {
				CreditCard.mLogger.info( e.toString());
			}


			/*CalculateRepeater(,formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));	
						CalculateRepeater(,formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));	
						CalculateRepeater(,formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));	
						CalculateRepeater(,formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));	
						CalculateRepeater(,formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));	
						CalculateRepeater(,formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));	
						CalculateRepeater(,formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));	
						CalculateRepeater(,formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));	*/

		}
		// ended by yash

		//Added to add modify delete in the Liability grid
		if(pEvent.getSource().getName().equalsIgnoreCase("ExtLiability_Button2"))
		{
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
		}
		if(pEvent.getSource().getName().equalsIgnoreCase("ExtLiability_Button3"))
		{
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
		}
		if(pEvent.getSource().getName().equalsIgnoreCase("ExtLiability_Button4"))
		{
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
		}
		//Added to add modify delete in the Liability grid
		
		if(pEvent.getSource().getName().equalsIgnoreCase("Customer_save")){
			CreditCard.mLogger.info( "Inside Customer_save button: ");
			formObject.saveFragment("CustomerDetails");
		}

		/*
					if(pEvent.getSource().getName().equalsIgnoreCase("Product_Save")){
						formObject.saveFragment("ProductContainer");
					}
		 */
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
		// ++ below code not commented at offshore - 06-10-2017
		if(pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails_ContactDetails_Save")){
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


		if(pEvent.getSource().getName().equalsIgnoreCase("Customer_Reference_Add"))
		{
			formObject.setNGValue("reference_wi_name",formObject.getWFWorkitemName());
			CreditCard.mLogger.info( "Inside add button: "+formObject.getNGValue("reference_wi_name"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Customer_cmplx_ReferenceGrid");
		}
		if(pEvent.getSource().getName().equalsIgnoreCase("Customer_Reference__modify"))
		{

			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Customer_cmplx_ReferenceGrid");
		}
		if(pEvent.getSource().getName().equalsIgnoreCase("Customer_Reference_delete"))
		{

			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_Customer_cmplx_ReferenceGrid");
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("Add")){

			formObject.setNGValue("Grid_wi_name",formObject.getWFWorkitemName());
			CreditCard.mLogger.info( "Inside add button: "+formObject.getNGValue("Grid_wi_name"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Product_cmplx_ProductGrid");

			formObject.setNGValue("Product_type","Conventional",false);
			formObject.setNGValue("ReqProd","--Select--",false);
			formObject.setNGValue("AppType","--Select--",false);
			formObject.setNGValue("EmpType","--Select--",false);
			formObject.setNGValue("Priority","Primary",false);
			//formObject.setVisible("cmplx_Product_cmplx_ProductGrid_Priority",false); //tenor
			formObject.setVisible("Product_Label3",false); // scheme lable
			//formObject.setVisible("Product_Label6",false); // card prod
			//formObject.setVisible("Product_Label10",false);
			//formObject.setVisible("Product_Label12",false);
			//formObject.setVisible("CardProd",false);
			formObject.setVisible("Scheme",false);
			formObject.setVisible("ReqTenor",false);
			formObject.setVisible("Product_Label15",false); // type of req
			//formObject.setVisible("Product_Label17",false); 
			formObject.setVisible("Product_Label16",false); // new limit value
			formObject.setVisible("Product_Label18",false); // limit exp
			//formObject.setVisible("Product_Label21",false); 
			//formObject.setVisible("Product_Label22",false); 
			//formObject.setVisible("Product_Label23",false); 
			//formObject.setVisible("Product_Label24",false);
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
			
			//09/08/2017 to make company details invisible
			CreditCard.mLogger.info( "Inside add button:value of emp "+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6));
			if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6).equalsIgnoreCase("Salaried")){
				formObject.setSheetVisible("Tab1", 1, false);
			}
			//09/08/2017 to make company details invisible 
		}	
		if (pEvent.getSource().getName().equalsIgnoreCase("Modify")){

			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Product_cmplx_ProductGrid");
			formObject.setNGFrameState("Incomedetails", 1);
			formObject.setNGValue("Product_type","Conventional",false);
			formObject.setNGValue("ReqProd","--Select--",false);
			formObject.setNGValue("AppType","--Select--",false);
			formObject.setNGValue("EmpType","--Select--",false);
			formObject.setNGValue("Priority","Primary",false);
			formObject.setVisible("Product_Label5",false); //tenor
			formObject.setVisible("Product_Label3",false); // scheme lable
			//formObject.setVisible("Product_Label6",false); // card prod
			//formObject.setVisible("Product_Label10",false);
			//formObject.setVisible("Product_Label12",false);
			//formObject.setVisible("CardProd",false);
			formObject.setVisible("Scheme",false);
			formObject.setVisible("ReqTenor",false);
			formObject.setVisible("Product_Label15",false); // type of req
			//formObject.setVisible("Product_Label17",false); 
			formObject.setVisible("Product_Label16",false); // new limit value
			formObject.setVisible("Product_Label18",false); // limit exp
			//formObject.setVisible("Product_Label21",false); 
			//formObject.setVisible("Product_Label22",false); 
			//formObject.setVisible("Product_Label23",false); 
			//formObject.setVisible("Product_Label24",false);
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
			
			//09/08/2017 to make company details invisible
			CreditCard.mLogger.info( "Inside add button:value of emp "+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6));
			if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6).equalsIgnoreCase("Salaried")){
				formObject.setSheetVisible("Tab1", 1, false);
				CreditCard.mLogger.info( "Inside add button:value of emp inside if of modify");
			}
			else{
				formObject.setSheetVisible("Tab1", 1, true);
				CreditCard.mLogger.info( "Inside add button:value of emp inside else of modify");
			}
			//09/08/2017 to make company details invisible 
		}

		if (pEvent.getSource().getName().equalsIgnoreCase("Delete")){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_Product_cmplx_ProductGrid");

			formObject.setNGValue("Product_type","Conventional",false);
			formObject.setNGValue("ReqProd","--Select--",false);
			formObject.setNGValue("AppType","--Select--",false);
			formObject.setNGValue("EmpType","--Select--",false);
			formObject.setNGValue("Priority","Primary",false);
			formObject.setVisible("Product_Label5",false); //tenor
			formObject.setVisible("Product_Label3",false); // scheme lable
			//formObject.setVisible("Product_Label6",false); // card prod
			//formObject.setVisible("Product_Label10",false);
			//formObject.setVisible("Product_Label12",false);
			//formObject.setVisible("CardProd",false);
			formObject.setVisible("Scheme",false);
			formObject.setVisible("ReqTenor",false);
			formObject.setVisible("Product_Label15",false); // type of req
			//formObject.setVisible("Product_Label17",false); 
			formObject.setVisible("Product_Label16",false); // new limit value
			formObject.setVisible("Product_Label18",false); // limit exp
			//formObject.setVisible("Product_Label21",false); 
			//formObject.setVisible("Product_Label22",false); 
			//formObject.setVisible("Product_Label23",false); 
			//formObject.setVisible("Product_Label24",false);
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
		if(pEvent.getSource().getName().equalsIgnoreCase("Product_Save")){
			
			formObject.saveFragment("ProductContainer");
			
			throw new ValidatorException(new CustomExceptionHandler("", "Product_Save#Product Save Successful","", hm));
		}
		if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_Salaried_Save")){
			formObject.saveFragment("IncomeDetails");
			throw new ValidatorException(new CustomExceptionHandler("", "IncomeDetails_Salaried_Save#IncomeDetails_Salaried Save Successful","", hm));
		}
		if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_SelfEmployed_Save")){
			formObject.saveFragment("Incomedetails");
			throw new ValidatorException(new CustomExceptionHandler("", "IncomeDetails_SelfEmployed_Save#IncomeDetails_Salaried Save Successful","", hm));
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails_Button2")) {
			String EmpName=formObject.getNGValue("EMploymentDetails_Text21");
			String EmpCode=formObject.getNGValue("EMploymentDetails_Text22");
			CreditCard.mLogger.info( "EMpName$"+EmpName+"$");
			String query=null;
			//changes done to check duplicate selection compare emp code and main emp code main_employer_code column added - 06-09-2017
			if(EmpName.trim().equalsIgnoreCase(""))
				query="select distinct(EMPR_NAME),EMPLOYER_CODE,INCLUDED_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,MAIN_EMPLOYER_CODE from NG_RLOS_ALOC_OFFLINE_DATA where EMPLOYER_CODE Like '%"+EmpCode+"%'";


			else
				query="select distinct(EMPR_NAME),EMPLOYER_CODE,INCLUDED_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,MAIN_EMPLOYER_CODE from NG_RLOS_ALOC_OFFLINE_DATA where EMPR_NAME Like '%"+EmpName + "%' or EMPLOYER_CODE Like '%"+EmpCode+"'";


			CreditCard.mLogger.info( "query is: "+query);
			populatePickListWindow(query,"EMploymentDetails_Button2", "Employer Name,Employer Code,INCLUDED IN PL ALOC,INCLUDED IN CC ALOC,INDUSTRY SECTOR,INDUSTRY MACRO,INDUSTRY MICRO,CONSTITUTION,NAME OF FREEZONE AUTHORITY,MAIN EMPLOYER CODE", true, 20);			     
		}

		if(pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails_Save")){
			formObject.saveFragment("EmploymentDetails");
			throw new ValidatorException(new CustomExceptionHandler("", "EMploymentDetails_Save#EMploymentDetails Save Successful","", hm));
		}
		if(pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails_Button1")){
			formObject.saveFragment("Auth_Sign_Details");
			throw new ValidatorException(new CustomExceptionHandler("", "AuthorisedSignDetails_Button1#AuthorisedSignDetails_Button1 Save Successful","", hm));
		}
		if(pEvent.getSource().getName().equalsIgnoreCase("Button6")){
			CreditCard.mLogger.info(formObject.getSelectedSheet(pEvent.getSource().getName())+"");
			formObject.setSelectedSheet("ParentTab",3);		
			formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligProd");
			formObject.setNGFrameState("EligibilityAndProductInformation", 0);
			formObject.setNGFrameState("EligibilityAndProductInformation", 1);
			formObject.setNGFrameState("EligibilityAndProductInformation", 0);
		}
		if(pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo_Save")){
			formObject.saveFragment("EligibilityAndProductInformation");
			throw new ValidatorException(new CustomExceptionHandler("", "ELigibiltyAndProductInfo_Save#ELigibiltyAndProductInfo Save Successful","", hm));
		}
		if(pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo_Button1")){
			formObject.saveFragment("EligibilityAndProductInformation");
			throw new ValidatorException(new CustomExceptionHandler("", "ELigibiltyAndProductInfo_Button1#ELigibiltyAndProductInfo  Successful","", hm));
		}
		if(pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails_Add"))
		{
			formObject.setNGValue("company_winame",formObject.getWFWorkitemName());
			CreditCard.mLogger.info( "Inside add button: "+formObject.getNGValue("company_winame"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_CompanyDetails_cmplx_CompanyGrid");
		}

		if(pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails_Modify"))
		{

			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_CompanyDetails_cmplx_CompanyGrid");
		}

		if(pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails_delete"))
		{

			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_CompanyDetails_cmplx_CompanyGrid");
		}

		if(pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails_add"))
		{
			formObject.setNGValue("PartnerDetails_partner_winame",formObject.getWFWorkitemName());
			CreditCard.mLogger.info( "Inside add button: "+formObject.getNGValue("PartnerDetails_partner_winame"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_PartnerDetails_cmplx_partnerGrid");
		}

		if(pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails_modify"))
		{

			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_PartnerDetails_cmplx_partnerGrid");
		}

		if(pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails_delete"))
		{

			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_PartnerDetails_cmplx_partnerGrid");
		}

		if(pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails_add"))
		{
			formObject.setNGValue("AuthorisedSignDetails_AuthorisedSign_wiName",formObject.getWFWorkitemName());
			CreditCard.mLogger.info( "Inside add button: "+formObject.getNGValue("AuthorisedSignDetails_AuthorisedSign_wiName"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails");
		}

		if(pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails_modify"))
		{

			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails");
		}

		if(pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails_delete"))
		{

			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails");
		}
		
		//Tanshu Aggarwal Reference Details Add,Delete,Modify functioning (17/08/2017)
		if(pEvent.getSource().getName().equalsIgnoreCase("Reference_Details_Reference_Add"))
		{
			formObject.setNGValue("Reference_Details_reference_wi_name",formObject.getWFWorkitemName());
			CreditCard.mLogger.info( "Inside add button: "+formObject.getNGValue("Reference_Details_reference_wi_name"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_RefDetails_cmplx_Gr_ReferenceDetails");
		}

		if(pEvent.getSource().getName().equalsIgnoreCase("Reference_Details_Reference__modify"))
		{

			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_RefDetails_cmplx_Gr_ReferenceDetails");
		}

		if(pEvent.getSource().getName().equalsIgnoreCase("Reference_Details_Reference_delete"))
		{
			
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_RefDetails_cmplx_Gr_ReferenceDetails");
			 String query="DELETE  FROM ng_rlos_ReferenceDetails WHERE wi_name='"+formObject.getWFWorkitemName()+"'";
			 CreditCard.mLogger.info("query"+query);
			 List<List<String>> list=formObject.getDataFromDataSource(query);
			 CreditCard.mLogger.info("list"+list);
		}
		
		//Tanshu Aggarwal Reference Details Add,Delete,Modify functioning (17/08/2017)
		if(pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails_Button3")){
			CreditCard.mLogger.info("CompanyDetails_Button3");
			/* outputResponse = GenerateXML("CUSTOMER_DETAILS","Corporation_CIF");
                       ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                       SKLogger.writeLog("CC value of ReturnCode"+ReturnCode);
                       ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                       SKLogger.writeLog("CC value of ReturnDesc"+ReturnDesc);
                       String CustId = (outputResponse.contains("<CustId>")) ? outputResponse.substring(outputResponse.indexOf("<CustId>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</CustId>")):"";    
                       SKLogger.writeLog("CC value of CustId"+CustId);
                       String CorpName = (outputResponse.contains("<CorpName>")) ? outputResponse.substring(outputResponse.indexOf("<CorpName>")+"</CorpName>".length()-1,outputResponse.indexOf("</CorpName>")):"";    
                       SKLogger.writeLog("CC value of CorpName"+CorpName);
                       String BusinessIncDate = (outputResponse.contains("<BusinessIncDate>")) ? outputResponse.substring(outputResponse.indexOf("<BusinessIncDate>")+"</BusinessIncDate>".length()-1,outputResponse.indexOf("</BusinessIncDate>")):"";    
                       SKLogger.writeLog("CC value of BusinessIncDate"+BusinessIncDate);
                       String LegEnt = (outputResponse.contains("<LegEnt>")) ? outputResponse.substring(outputResponse.indexOf("<LegEnt>")+"</LegEnt>".length()-1,outputResponse.indexOf("</LegEnt>")):"";    
                       SKLogger.writeLog("CC value of LegEnt"+LegEnt);
                       if((ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")) && (ReturnDesc.equalsIgnoreCase("Success") || ReturnDesc.equalsIgnoreCase("Successful"))){
                           formObject.setNGValue("Is_Customer_Details","Y");

                           SKLogger.writeLog("CC value of EID_Genuine"+"value of company Details corporation"+formObject.getNGValue("Is_Customer_Details"));

                           valueSetCustomer(outputResponse);  
                            try{

                                String Date1=BusinessIncDate;
                                SKLogger.writeLog("CC value of Date1111"+Date1);
                                 SimpleDateFormat sdf1=new SimpleDateFormat("dd-mm-yyyy");
                                 SimpleDateFormat sdf2=new SimpleDateFormat("dd/mm/yyyy");
                                 String Datechanged=sdf2.format(sdf1.parse(Date1));
                                 SKLogger.writeLog("CC value ofDatechanged"+Datechanged);
                                 formObject.setNGValue("cmplx_CompanyDetails_DateOfEstb",Datechanged);
                                }
                                catch(Exception ex){

                                }
                           SKLogger.writeLog("CC value of Customer Details"+"corporation cif");
                           SKLogger.writeLog("CC value of Customer Details"+formObject.getNGValue("Is_Customer_Details"));
                           }
                           else{
                               SKLogger.writeLog("Customer Details"+"Customer Details Corporation CIF is not generated");
                               formObject.setNGValue("Is_Customer_Details","N");
                           }*/
			CreditCard.mLogger.info(formObject.getNGValue("Is_Customer_Details"));
		}

		/* if(pEvent.getSource().getName().equalsIgnoreCase("PartMatch_Button2"))
					 {
						 CreditCard.mLogger.info("Inside search button part match"+"");
					 }*/
		if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_Button1"))
		{
			CreditCard.mLogger.info("Inside Add button FinacleCore_Button1 core"+"");
			formObject.ExecuteExternalCommand("NGAddRow", "FinacleCore_ListView2");
		}
		if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_Button2"))
		{
			CreditCard.mLogger.info("Inside modify button FinacleCore_Button2 core"+"");
			formObject.ExecuteExternalCommand("NGModifyRow", "FinacleCore_ListView2");
		}
		if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_Button3"))
		{
			CreditCard.mLogger.info("Inside delete button Finacle core"+"");
			formObject.ExecuteExternalCommand("NGDeleteRow", "FinacleCore_ListView2");
		}
		if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_Button4"))
		{
			CreditCard.mLogger.info("Inside Add button Finacle core"+"");
			formObject.ExecuteExternalCommand("NGAddRow", "FinacleCore_ListView8");
		}
		if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_Button5"))
		{
			CreditCard.mLogger.info("Inside modify button Finacle core"+"");
			formObject.ExecuteExternalCommand("NGModifyRow", "FinacleCore_ListView8");
		}
		if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_Button6"))
		{
			CreditCard.mLogger.info("Inside delete button Finacle core"+"");
			formObject.ExecuteExternalCommand("NGDeleteRow", "FinacleCore_ListView8");
		}
		if(pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_Button1"))
		{
			CreditCard.mLogger.info("Inside Add button WorldCheck1_Button1 "+"");
			formObject.setNGValue("WorldCheck1_WiName",formObject.getWFWorkitemName());
			formObject.setNGValue("Is_WorldCheckAdd","Y");

			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_WorldCheck_WorldCheck_Grid");
		}
		if(pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_Button2"))
		{
			CreditCard.mLogger.info("Inside modify button WorldCheck1_Button2 core"+"");
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_WorldCheck_WorldCheck_Grid");
		}
		if(pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_Button3"))
		{
			CreditCard.mLogger.info("Inside delete button WorldCheck1_Button3 core"+"");

			formObject.setNGValue("Is_WorldCheckAdd","N");
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_WorldCheck_WorldCheck_Grid");
		}
		if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMIncident_Button1")){
			formObject.saveFragment("Finacle_CRM_Incidents");
		}
		if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMCustInfo_Button1")){
			formObject.saveFragment("Finacle_CRM_CustomerInformation");
		}
		if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_Button8")){
			formObject.saveFragment("Finacle_Core");
		}
		if(pEvent.getSource().getName().equalsIgnoreCase("MOL1_Button1")){
			formObject.saveFragment("MOL");
		}

		if(pEvent.getSource().getName().equalsIgnoreCase("PartMatch_Button3")){
			formObject.saveFragment("Part_Match");
		}
		if(pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_Button4")){
			formObject.saveFragment("World_Check");
		}
		if(pEvent.getSource().getName().equalsIgnoreCase("SalaryEnq_Button1")){
			formObject.saveFragment("Salary_Enquiry");
		}
		if(pEvent.getSource().getName().equalsIgnoreCase("RejectEnq_Save")){
			formObject.saveFragment("Reject_Enquiry");
		}
		if(pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails_Button1")){
			formObject.saveFragment("CompDetails");
		}
		if(pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails_Button1")){
			formObject.saveFragment("PartnerDetails");
			alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL046");

			throw new ValidatorException(new FacesMessage(alert_msg));

		}
		//added by yash on 24/8/2017
		if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_SaveButton")){
			formObject.saveFragment("Notepad_Values");
			alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL044");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_save")){
		formObject.saveFragment("Notepad_Values");
		alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL044");
		throw new ValidatorException(new FacesMessage(alert_msg));
	}
	if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_save")){
		formObject.saveFragment("DecisionHistory");
		alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL022");
		throw new ValidatorException(new FacesMessage(alert_msg));
	}
		// added by abhishek as per CC FSD 
		if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Add")){
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_notegrid");
			Notepad_add();
		}
		if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Modify")){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_NotepadDetails_cmplx_notegrid");
			Notepad_modify();
		}
		if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Delete")){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_NotepadDetails_cmplx_notegrid");
			Notepad_delete();
		}
		// added by abhishek as per CC FSD
		if(pEvent.getSource().getName().equalsIgnoreCase("cmplx_NotepadDetails_cmplx_notegrid")){
			Notepad_grid();
		} 
		if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Add1")){
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_Telloggrid");
		}
		if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Clear")){
			formObject.ExecuteExternalCommand("NGClear", "cmplx_NotepadDetails_cmplx_Telloggrid");
		}
		//ended by aysh on24/8/2017
		if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Button1")){
			formObject.saveFragment("DecisionHistory");
		}
		else if(pEvent.getSource().getName().equalsIgnoreCase("Customer_FetchDetails"))
		{
			if(formObject.getNGValue("cmplx_Customer_CIFID_Available").equalsIgnoreCase("false")){
				outputResponse =GenerateXML("CUSTOMER_ELIGIBILITY","Primary_CIF");
				CreditCard.mLogger.info("RLOS value of ReturnDesc"+"Customer Eligibility");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				CreditCard.mLogger.info("RLOS value of ReturnCode"+ReturnCode);
				if(ReturnCode.equalsIgnoreCase("0000")){
					valueSetCustomer(outputResponse,"");    
					formObject.setNGValue("Is_Customer_Eligibility","Y");
					parse_cif_eligibility(outputResponse,"Primary_CIF");
					String NTB_flag = formObject.getNGValue("cmplx_Customer_NTB");
					if(NTB_flag.equalsIgnoreCase("true")){
						CreditCard.mLogger.info( "inside Customer Eligibility to through Exception to Exit:");
						alert_msg = NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL013");
					}
					else{
						alert_msg = NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL033");
						outputResponse = GenerateXML("CUSTOMER_DETAILS","Primary_CIF");
						CreditCard.mLogger.info("RLOS value of ReturnCode"+"Inside Customer");
						if(ReturnCode.equalsIgnoreCase("0000")){
							formObject.setNGValue("Is_Customer_Details","Y");
							CreditCard.mLogger.info("RLOS value of Is_Customer_Details"+formObject.getNGValue("Is_Customer_Details"));
							formObject.setEnabled("Customer_Button1",true);
							valueSetCustomer(outputResponse,"Primary_CIF");
							formObject.setNGValue("cmplx_Customer_DOb",formatDateFromOnetoAnother(formObject.getNGValue("cmplx_Customer_DOb"),"yyyy-mm-dd","dd/mm/yyyy"),false);
						/*	try{
								String str_dob=formObject.getNGValue("cmplx_Customer_DOb");
								SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-mm-dd");
								SimpleDateFormat sdf2=new SimpleDateFormat("dd/mm/yyyy");
								if(str_dob!=null&&!str_dob.equalsIgnoreCase("")){
									String n_str_dob=sdf2.format(sdf1.parse(str_dob));
									CreditCard.mLogger.info("converting date entered"+n_str_dob+"asd");
									formObject.setNGValue("cmplx_Customer_DOb",n_str_dob,false);
								}
							}
							catch(Exception e){
								CreditCard.mLogger.info("Exception Occur while converting date"+e+"");
							} */    
						}
						else{
							alert_msg = NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL028");
							CreditCard.mLogger.info("PL DDVT Maker value of ReturnCode"+"Error in Customer Eligibility: "+ ReturnCode);
						}
						formObject.RaiseEvent("WFSave");
					}
				}
				else{
					CreditCard.mLogger.info("PL DDVT Maker value of ReturnCode"+"Error in Customer Eligibility: "+ ReturnCode);
					alert_msg = NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL027");
				}
			}
			else{
				/*formObject.setNGValue("Is_Customer_Eligibility","N");
				popupFlag="Y";
				// alert_msg = "Dedupe check failed.";
				CreditCard.mLogger.info("Dedupe check failed."+"");
				//  throw new ValidatorException(new FacesMessage("Dedupe check failed."));
				try{
					//  throw new ValidatorException(new FacesMessage(alert_msg));
				}
				finally
{
hm.clear();
}{hm.clear();}*/
				// customer details called by saurabh on 23rd Sept for Demo findings.
				if(!formObject.getNGValue("cmplx_Customer_CIFNo").equalsIgnoreCase("") && !formObject.getNGValue("cmplx_Customer_CIFNo").equalsIgnoreCase(" ")){
				outputResponse = GenerateXML("CUSTOMER_DETAILS","Primary_CIF");
				CreditCard.mLogger.info("RLOS value of ReturnCode"+"Inside Customer");
				if(ReturnCode.equalsIgnoreCase("0000")){
					formObject.setNGValue("Is_Customer_Details","Y");
					CreditCard.mLogger.info("RLOS value of Is_Customer_Details"+formObject.getNGValue("Is_Customer_Details"));
					formObject.setEnabled("Customer_Button1",true);
					valueSetCustomer(outputResponse,"Primary_CIF");
					formObject.setNGValue("cmplx_Customer_DOb",formatDateFromOnetoAnother(formObject.getNGValue("cmplx_Customer_DOb"),"yyyy-mm-dd","dd/mm/yyyy"),false);
				/*	try{
						String str_dob=formObject.getNGValue("cmplx_Customer_DOb");
						SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-mm-dd");
						SimpleDateFormat sdf2=new SimpleDateFormat("dd/mm/yyyy");
						if(str_dob!=null&&!str_dob.equalsIgnoreCase("")){
							String n_str_dob=sdf2.format(sdf1.parse(str_dob));
							CreditCard.mLogger.info("converting date entered"+n_str_dob+"asd");
							formObject.setNGValue("cmplx_Customer_DOb",n_str_dob,false);
						}
					}
					catch(Exception e){
						CreditCard.mLogger.info("Exception Occur while converting date"+e+"");
					}*/     
				}
				else{
					alert_msg = NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL028");
					CreditCard.mLogger.info("PL DDVT Maker value of ReturnCode"+"Error in Customer Eligibility: "+ ReturnCode);
				}



				formObject.RaiseEvent("WFSave");
				}
				// customer details called by saurabh on 23rd Sept for Demo findings end.
			}
			//added
		}
		//ended

		//added
		/*else if(formObject.getNGValue("cmplx_Customer_CIFID_Available")=="true"){
			CreditCard.mLogger.info("RLOS value of Customer Details ----1234567"+formObject.getNGValue("cmplx_Customer_CIFID_Available"));
			CreditCard.mLogger.info("RLOS value of Customer Details ----1234567"+"inside true");
			outputResponse = GenerateXML("CUSTOMER_DETAILS","Primary_CIF");
			try
			{
				String Date1=formObject.getNGValue("cmplx_Customer_DOb");
				SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-mm-dd");
				SimpleDateFormat sdf2=new SimpleDateFormat("dd/mm/yyyy");
				String Datechanged=sdf2.format(sdf1.parse(Date1));
				CreditCard.mLogger.info("RLOS value ofDatechanged"+Datechanged);
				formObject.setNGValue("cmplx_Customer_DOb",Datechanged);
			}        
			catch(Exception ex){                            
			}


			CreditCard.mLogger.info("RLOS value of ReturnCode"+"Inside Customer");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			CreditCard.mLogger.info("RLOS value of ReturnCode"+ReturnCode);
			if(ReturnCode.equalsIgnoreCase("0000")){
				alert_msg= "Existing Customer details fetched Sucessfully";



				formObject.setLocked("Customer_Button1", false);


				formObject.setLocked("Customer_FetchDetails", true);


				formObject.setNGValue("Is_Customer_Details","Y");
				formObject.RaiseEvent("WFSave");

				CreditCard.mLogger.info("RLOS value of Is_Customer_Details"+formObject.getNGValue("Is_Customer_Details"));


				formObject.fetchFragment("Address_Details_Container", "AddressDetails","q_AddressDetails");                          
				formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
				formObject.setTop("ReferenceDetails",formObject.getTop("Address_Details_Container")+formObject.getHeight("Address_Details_Container"));
				formObject.setTop("Alt_Contact_container",formObject.getTop("ReferenceDetails")+25);
				formObject.setTop("CardDetails", formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container"));
				formObject.setEnabled("Customer_Button1", true);	
				CreditCard.mLogger.info("year difference:"+"diffdays difference after button is enabled");
				if(formObject.isVisible("Supplementary_Cont")){
					formObject.setTop("Supplementary_Cont",formObject.getTop("Card_Details")+25);
					formObject.setTop("FATCA",formObject.getTop("Supplementary_Cont")+25);
					formObject.setTop("KYC", formObject.getTop("FATCA")+25);
					formObject.setTop("OECD", formObject.getTop("KYC")+25);
					//Java takes height of entire container in getHeight while js takes current height	i.e collapsed/expanded
				}
				else{
					formObject.setTop("FATCA",formObject.getTop("Card_Details")+25);
					formObject.setTop("KYC", formObject.getTop("FATCA")+25);
					formObject.setTop("OECD", formObject.getTop("KYC")+25);
				}
				valueSetCustomer(outputResponse,"Primary_CIF");
				//code to set Emirates of residence start.
				int n=formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
				if(n>0){
					for(int i=0;i<n;i++){
						CreditCard.mLogger.info("selecting Emirates of residence: "+formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0));
						if(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0).equalsIgnoreCase("RESIDENCE")){
							CreditCard.mLogger.info("selecting Emirates of residence: settign value: "+formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0));
							formObject.setNGValue("cmplx_Customer_EmirateOfResidence",formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 6));
						}
					}
					//code to set Emirates of residence End.
					formObject.setEnabled("Customer_FetchDetails", false); 
					throw new ValidatorException(new FacesMessage("Existing Customer Details fetched Sucessfully"));


				}
				//code change to save the date in desired format by AMAN	
				try{
					String str_dob=formObject.getNGValue("cmplx_Customer_DOb");
					CreditCard.mLogger.info("converting date entered",str_dob+"");
					String str_IDissuedate=formObject.getNGValue("cmplx_Customer_IdIssueDate");
					String str_passExpDate=formObject.getNGValue("cmplx_Customer_PassPortExpiry");
					String str_visaExpDate=formObject.getNGValue("cmplx_Customer_VIsaExpiry");

					SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-mm-dd");
					SimpleDateFormat sdf2=new SimpleDateFormat("dd/mm/yyyy");
					if(str_dob!=null&&!str_dob.equalsIgnoreCase("")){
						String n_str_dob=sdf2.format(sdf1.parse(str_dob));
						CreditCard.mLogger.info("converting date entered"+n_str_dob+"asd");

						formObject.setNGValue("cmplx_Customer_DOb",n_str_dob,false);
					}


				}
				catch(Exception e){
					CreditCard.mLogger.info("Exception Occur while converting date"+e+"");
				}     
			}
			else{
				alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL062");

			formObject.RaiseEvent("WFSave");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}*/

		else if(pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails_FetchDetails")){
			
			String EmiratesID = formObject.getNGValue("SupplementCardDetails_Text6");
			CreditCard.mLogger.info("CC_DDVT_MAker "+ "EID value for Entity Details for Supplementary Cards: "+EmiratesID);
			String primaryCif = null;
			boolean isEntityDetailsSuccess = false;
			
			if( EmiratesID!=null && !EmiratesID.equalsIgnoreCase("")){
				outputResponse = GenerateXML("ENTITY_DETAILS","Supplementary_Card_Details");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				CreditCard.mLogger.info("CC_DDVT_MAker value of ReturnCode for Supplementary Cards: "+ReturnCode);
				if(ReturnCode.equalsIgnoreCase("0000")){
					//valueSetCustomer(outputResponse , "Supplementary_Card_Details");
					primaryCif = (outputResponse.contains("<CIFID>")) ? outputResponse.substring(outputResponse.indexOf("<CIFID>")+"</CIFID>".length()-1,outputResponse.indexOf("</CIFID>")):"";
					formObject.setNGValue("Supplementary_CIFNO",primaryCif);
					isEntityDetailsSuccess = true;
					alert_msg = fetch_cust_details_supplementary();
				}

				CreditCard.mLogger.info("CC_DDVT_MAker value of Primary Cif"+primaryCif);
			}
			if(!isEntityDetailsSuccess || (primaryCif==null || primaryCif.equalsIgnoreCase(""))){
				outputResponse =GenerateXML("CUSTOMER_ELIGIBILITY","Supplementary_Card_Details");
				CreditCard.mLogger.info("CC_DDVT_MAker value of ReturnDesc for Supplementary Cards: "+"Customer Eligibility");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				CreditCard.mLogger.info("CC_DDVT_MAker value of ReturnCode for Supplementary Cards: "+ReturnCode);
				if(ReturnCode.equalsIgnoreCase("0000") )
				{
					valueSetCustomer(outputResponse ,"Supplementary_Card_Details");    
					parse_cif_eligibility(outputResponse,"Supplementary_Card_Details");
					alert_msg = fetch_cust_details_supplementary();
					
				}
			}
			
		}

		if(pEvent.getSource().getName().equalsIgnoreCase("Customer_Button1"))
		{
			//if("Is_Entity_Details".equalsIgnoreCase("Y") && "Is_Customer_Details".equalsIgnoreCase("Y")){
			String NegatedFlag="";
			String BlacklistFlag="";
			String DuplicationFlag="";
			outputResponse =GenerateXML("CUSTOMER_ELIGIBILITY","Primary_CIF");
			CreditCard.mLogger.info("RLOS value of ReturnDesc"+"Customer Eligibility");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			CreditCard.mLogger.info("RLOS value of ReturnCode"+ReturnCode);
			formObject.setNGValue("Is_Customer_Eligibility","Y");


			if(ReturnCode.equalsIgnoreCase("0000")){
				valueSetCustomer(outputResponse,""); 
				parse_cif_eligibility(outputResponse,"Primary_CIF");
				BlacklistFlag = (outputResponse.contains("<BlacklistFlag>")) ? outputResponse.substring(outputResponse.indexOf("<BlacklistFlag>")+"</BlacklistFlag>".length()-1,outputResponse.indexOf("</BlacklistFlag>")):"";
				CreditCard.mLogger.info("PL value of BlacklistFlag_Part"+"Customer is BlacklistedFlag"+BlacklistFlag);
				DuplicationFlag = (outputResponse.contains("<DuplicationFlag>")) ? outputResponse.substring(outputResponse.indexOf("<DuplicationFlag>")+"</DuplicationFlag>".length()-1,outputResponse.indexOf("</DuplicationFlag>")):"";
				CreditCard.mLogger.info("PL value of BlacklistFlag_Part"+"Customer is DuplicationFlag"+DuplicationFlag);
				NegatedFlag= (outputResponse.contains("<NegatedFlag>")) ? outputResponse.substring(outputResponse.indexOf("<NegatedFlag>")+"</NegatedFlag>".length()-1,outputResponse.indexOf("</NegatedFlag>")):"";
				CreditCard.mLogger.info("PL value of BlacklistFlag_Part"+"Customer is NegatedFlag"+NegatedFlag);
				formObject.setNGValue("Is_Customer_Eligibility","Y");
				formObject.RaiseEvent("WFSave");
				CreditCard.mLogger.info("RLOS value of ReturnDesc Is_Customer_Eligibility"+formObject.getNGValue("Is_Customer_Eligibility"));
				formObject.setNGValue("BlacklistFlag",BlacklistFlag);
				formObject.setNGValue("DuplicationFlag",DuplicationFlag);
				formObject.setNGValue("IsAcctCustFlag",NegatedFlag);
				String NTB_flag = formObject.getNGValue("cmplx_Customer_NTB");
				if(NTB_flag.equalsIgnoreCase("true")){
					CreditCard.mLogger.info( "inside Customer Eligibility to through Exception to Exit:");
					alert_msg = NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL013");
				}
				else{
					alert_msg = NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL014");
				}

			}
			else{
				formObject.setNGValue("Is_Customer_Eligibility","N");
				formObject.RaiseEvent("WFSave");
			}
			CreditCard.mLogger.info("RLOS value of Customer Details"+formObject.getNGValue("Is_Customer_Eligibility"));
			CreditCard.mLogger.info("RLOS value of BlacklistFlag"+formObject.getNGValue("BlacklistFlag"));
			CreditCard.mLogger.info("RLOS value of DuplicationFlag"+formObject.getNGValue("DuplicationFlag"));
			CreditCard.mLogger.info("RLOS value of IsAcctCustFlag"+formObject.getNGValue("IsAcctCustFlag"));
			formObject.RaiseEvent("WFSave");
			throw new ValidatorException(new FacesMessage(alert_msg));
			//}
		}

		if (pEvent.getSource().getName().equalsIgnoreCase("PartMatch_Search")){
			//GenerateXML();
			CreditCard.mLogger.info("PL PartMatch_Search"+ "Inside PartMatch_Search button: ");

			//HashMap<String,String> hm1= new HashMap<String,String>(); // not nullable HashMap

			//hm1.put("PartMatch_Search","Clicked");
			// popupFlag="N";
			outputResponse = GenerateXML("DEDUP_SUMMARY","");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			CreditCard.mLogger.info("PL value of ReturnCode"+ReturnCode);
			ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";	
			CreditCard.mLogger.info("PL value of ReturnDesc"+ReturnDesc);
			if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000") ){
				//valueSetCustomer(outputResponse);	
				formObject.clear("cmplx_PartMatch_cmplx_Partmatch_grid");
				parseDedupe_summary(outputResponse);	
				formObject.setNGValue("Is_PartMatchSearch","Y");
				CreditCard.mLogger.info("PL value of Part Match Request"+"inside if of partmatch");
				alert_msg= NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL064");
			}
			else{
				formObject.setNGValue("Is_PartMatchSearch","N");
				CreditCard.mLogger.info("PL value of Part Match Request"+"inside else of partmatch");
				alert_msg= NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL031");
			}
			formObject.RaiseEvent("WFSave");
			CreditCard.mLogger.info("PL value of Part Match Request"+formObject.getNGValue("Is_PartMatchSearch"));
			if((formObject.getNGValue("Is_PartMatchSearch").equalsIgnoreCase("Y")))
			{ 
				CreditCard.mLogger.info("RLOS value of Is_CUSTOMER_UPDATE_REQ"+"inside if condition of disabling the button");
			}
			else{
				formObject.setEnabled("PartMatch_Search", true);
				//	throw new ValidatorException(new CustomExceptionHandler("Dedupe Summary Fail!!","PartMatch_Search#Dedupe Summary Fail!!","",hm));
			}
			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMCustInfo_Button2")){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_FinacleCRMCustInfo_FincustGrid");
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("PartMatch_Button1")){
			formObject.fetchFragment("Finacle_CRM_CustomerInformation", "FinacleCRMCustInfo", "q_FinacleCRMCustInfo");
			formObject.setTop("External_Blacklist",formObject.getTop("Finacle_CRM_CustomerInformation")+formObject.getHeight("Finacle_CRM_CustomerInformation")+20);
			formObject.setTop("Finacle_Core",formObject.getTop("External_Blacklist")+30);
			formObject.setTop("MOL",formObject.getTop("Finacle_Core")+30);
			formObject.setTop("World_Check",formObject.getTop("MOL")+30);
			formObject.setTop("Reject_Enquiry",formObject.getTop("World_Check")+30);
			formObject.setTop("Salary_Enquiry",formObject.getTop("Reject_Enquiry")+30);
			//Ref. 1006
			formObject.setTop("CreditCard_Enq",formObject.getTop("Salary_Enquiry")+30);
			formObject.setTop("Case_hist",formObject.getTop("CreditCard_Enq")+30);
			formObject.setTop("LOS",formObject.getTop("Case_hist")+30);
			//Ref. 1006 end.
	
			String BlacklistFlag_Part = "";
			String BlacklistFlag_reason = "";
			String BlacklistFlag_code = "";
			String NegativeListFlag = "";



			outputResponse =GenerateXML("CUSTOMER_DETAILS","Primary_CIF");
			CreditCard.mLogger.info("CC value of ReturnDesc"+"Customer Details part Match");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			CreditCard.mLogger.info("CC value of ReturnCode part Match"+ReturnCode);
			if(ReturnCode.equalsIgnoreCase("0000")){

				BlacklistFlag_Part =  (outputResponse.contains("<BlackListFlag>")) ? outputResponse.substring(outputResponse.indexOf("<BlackListFlag>")+"</BlackListFlag>".length()-1,outputResponse.indexOf("</BlackListFlag>")):"";                                  
				formObject.setNGValue("Is_Customer_Details_Part","Y"); 
				BlacklistFlag_reason =  (outputResponse.contains("<BlackListReason>")) ? outputResponse.substring(outputResponse.indexOf("<BlackListReason>")+"</BlackListReason>".length()-1,outputResponse.indexOf("</BlackListReason>")):"NA";
				BlacklistFlag_code =  (outputResponse.contains("<BlackListReasonCode>")) ? outputResponse.substring(outputResponse.indexOf("<BlackListReasonCode>")+"</BlackListReasonCode>".length()-1,outputResponse.indexOf("</BlackListReasonCode>")):"NA";



				NegativeListFlag =  (outputResponse.contains("<NegativeListFlag>")) ? outputResponse.substring(outputResponse.indexOf("<NegativeListFlag>")+"</NegativeListFlag>".length()-1,outputResponse.indexOf("</NegativeListFlag>")):"NA";
				CreditCard.mLogger.info("CC value of ReturnCode part Match"+ReturnCode);
				CreditCard.mLogger.info("Value of NegativeListFlag: "+NegativeListFlag+" BlacklistFlag_code:Blacklist Reason: "+BlacklistFlag_reason+": "+BlacklistFlag_code);



				if(BlacklistFlag_Part.equalsIgnoreCase("Y"))
				{

					CreditCard.mLogger.info("CC value of BlacklistFlag_Part"+"Customer is Blacklisted");    
				}
				else{
					BlacklistFlag_Part="N";


					CreditCard.mLogger.info("CC value of BlacklistFlag_Part"+"Customer is not Blacklisted");    
				}
				//added by Akshay
				if(NegativeListFlag.equalsIgnoreCase("Y"))
				{
					CreditCard.mLogger.info("CC value of NegativeListFlag"+"Customer is Negative");    
				}
				else{
					NegativeListFlag="N";
					CreditCard.mLogger.info("CC value of BlacklistFlag_Part"+"Customer is not Negative");    
				}//ended By Akshay
			}
			else{


				formObject.setNGValue("Is_Customer_Details_Part","N");
			}
			CreditCard.mLogger.info("CC value of BlacklistFlag_Part flag"+BlacklistFlag_Part+"");   
			// changed by abhishek start for populating cutomer info grid 22may2017
			try{
				CreditCard.mLogger.info("CC value of BlacklistFlag_Part flag inside try"+BlacklistFlag_Part+"");    
				List<String> list_custinfo = new ArrayList<String>();
				String CIFID =formObject.getNGValue("cmplx_PartMatch_cmplx_Partmatch_grid",formObject.getSelectedIndex("cmplx_PartMatch_cmplx_Partmatch_grid"),0);
				String PASSPORTNO =formObject.getNGValue("cmplx_PartMatch_cmplx_Partmatch_grid",formObject.getSelectedIndex("cmplx_PartMatch_cmplx_Partmatch_grid"),5);

				list_custinfo.add(CIFID);  // cif id from partmatch
				list_custinfo.add("");
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
				list_custinfo.add("");
				
				CreditCard.mLogger.info("CC DDVT Maker"+ "list_custinfo list values"+list_custinfo);
				formObject.addItemFromList("cmplx_FinacleCRMCustInfo_FincustGrid", list_custinfo);
			}catch(Exception e){
				CreditCard.mLogger.info("PL DDVT Maker"+ "Exception while setting data in grid:"+e.getMessage());
				alert_msg= NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL032");
				throw new ValidatorException(new FacesMessage(alert_msg));
			}

			formObject.RaiseEvent("WFSave");          
		}	 /*if(pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails_Button2")){

							outputResponse = GenerateXML("CUSTOMER_DETAILS","Guarantor_CIF");
							ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
							SKLogger.writeLog("CC value of ReturnCode",ReturnCode);
							ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
							SKLogger.writeLog("CC value of ReturnDesc",ReturnDesc);
							if((ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")) && (ReturnDesc.equalsIgnoreCase("Success") || ReturnDesc.equalsIgnoreCase("Successful"))){
								formObject.setNGValue("Is_Customer_Details","Y");
								SKLogger.writeLog("CC value of EID_Genuine","value of Guarantor_CIF"+formObject.getNGValue("Is_Customer_Details"));
								valueSetCustomer(outputResponse);    
								SKLogger.writeLog("CC value of Customer Details","Guarantor_CIF is generated");
								SKLogger.writeLog("CC value of Customer Details",formObject.getNGValue("Is_Customer_Details"));
							}
							else{
								SKLogger.writeLog("Customer Details","Customer Details is not generated");
								formObject.setNGValue("Is_Customer_Details","N");
							}
							SKLogger.writeLog("CC value of Guarantor_CIF",formObject.getNGValue("Is_Customer_Details"));
						}*/
		if(pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails_Button3")){
			CreditCard.mLogger.info("CompanyDetails_Button3");
			outputResponse = GenerateXML("CUSTOMER_DETAILS","Corporation_CIF");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			CreditCard.mLogger.info(ReturnCode);
			ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
			CreditCard.mLogger.info("CC value of ReturnDesc"+ReturnDesc);
			String CustId = (outputResponse.contains("<CustId>")) ? outputResponse.substring(outputResponse.indexOf("<CustId>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</CustId>")):"";    
			CreditCard.mLogger.info("CC value of CustId"+CustId);
			String CorpName = (outputResponse.contains("<CorpName>")) ? outputResponse.substring(outputResponse.indexOf("<CorpName>")+"</CorpName>".length()-1,outputResponse.indexOf("</CorpName>")):"";    
			CreditCard.mLogger.info("CC value of CorpName"+CorpName);
			String BusinessIncDate = (outputResponse.contains("<BusinessIncDate>")) ? outputResponse.substring(outputResponse.indexOf("<BusinessIncDate>")+"</BusinessIncDate>".length()-1,outputResponse.indexOf("</BusinessIncDate>")):"";    
			CreditCard.mLogger.info("CC value of BusinessIncDate"+BusinessIncDate);
			String LegEnt = (outputResponse.contains("<LegEnt>")) ? outputResponse.substring(outputResponse.indexOf("<LegEnt>")+"</LegEnt>".length()-1,outputResponse.indexOf("</LegEnt>")):"";    
			CreditCard.mLogger.info("CC value of LegEnt"+LegEnt);
			if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000") ){
				formObject.setNGValue("Is_Customer_Details","Y");

				CreditCard.mLogger.info("CC value of EID_Genuine"+"value of company Details corporation"+formObject.getNGValue("Is_Customer_Details"));

				valueSetCustomer(outputResponse,"Corporation_CIF");  
				try
				{

					String Date1=BusinessIncDate;
					CreditCard.mLogger.info("CC value of Date1111"+Date1);
					SimpleDateFormat sdf1=new SimpleDateFormat("dd-mm-yyyy");
					SimpleDateFormat sdf2=new SimpleDateFormat("dd/mm/yyyy");
					String Datechanged=sdf2.format(sdf1.parse(Date1));
					CreditCard.mLogger.info("CC value ofDatechanged"+Datechanged);
					formObject.setNGValue("cmplx_CompanyDetails_DateOfEstb",Datechanged);
				}
				catch(Exception ex)
				{
					CreditCard.logException(ex);
				}
				CreditCard.mLogger.info("CC value of Customer Details"+"corporation cif");
				CreditCard.mLogger.info("CC value of Customer Details"+formObject.getNGValue("Is_Customer_Details"));
			}
			else{
				CreditCard.mLogger.info("Customer Details"+"Customer Details Corporation CIF is not generated");
				formObject.setNGValue("Is_Customer_Details","N");
			}
			CreditCard.mLogger.info(formObject.getNGValue("Is_Customer_Details"));
		}
		if(pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails_Button4")){

			outputResponse = GenerateXML("CUSTOMER_DETAILS","Authorised_CIF");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			CreditCard.mLogger.info(ReturnCode);
			ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
			CreditCard.mLogger.info("CC value of ReturnDesc"+ReturnDesc);
			if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")){
				formObject.setNGValue("Is_Customer_Details","Y");
				CreditCard.mLogger.info("CC value of EID_Genuine"+"value of Authorised_CIF"+formObject.getNGValue("Is_Customer_Details"));
				valueSetCustomer(outputResponse,"Authorised_CIF");    
				CreditCard.mLogger.info("CC value of Customer Details"+"Authorised_CIF is generated");
				CreditCard.mLogger.info("CC value of Customer Details"+formObject.getNGValue("Is_Customer_Details"));
			}
			else{
				CreditCard.mLogger.info("Customer Details"+"Customer Details is not generated");
				formObject.setNGValue("Is_Customer_Details","N");
			}
			CreditCard.mLogger.info("CC value of Authorised_CIF"+formObject.getNGValue("Is_Customer_Details"));
		}


		//tanshu ended

		if(pEvent.getSource().getName().equalsIgnoreCase("BTC_save") || pEvent.getSource().getName().equalsIgnoreCase("DDS_save") || pEvent.getSource().getName().equalsIgnoreCase("SI_save") || pEvent.getSource().getName().equalsIgnoreCase("RVC_Save")){
			formObject.saveFragment("Details");
		}
		if(pEvent.getSource().getName().equalsIgnoreCase("PartMatch_Save")){
			formObject.saveFragment("Part_Match");
		}
		//for BlackList Call added on 3rd May 2017
		//Changes done by aman to correctly save the value in the grid
		if (pEvent.getSource().getName().equalsIgnoreCase("PartMatch_Blacklist")){
			try
			{
			CreditCard.mLogger.info("PL value of ReturnDesc"+"Blacklist Details part Match1111");
			formObject.fetchFragment("FinacleCRM_CustInfo", "FinacleCRMCustInfo", "q_FinCRMCustInfo");
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
			String Wi_Name=formObject.getWFWorkitemName();
			
			//ended here

			// added on 11may 2017

			CreditCard.mLogger.info("PL value of ReturnDesc"+"Blacklist Details part Match1111 after initializing strings");
			outputResponse =GenerateXML("BLACKLIST_DETAILS","");
			CreditCard.mLogger.info("PL value of ReturnDesc"+"Blacklist Details part Match");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			CreditCard.mLogger.info("PL value of ReturnCode part Match"+ReturnCode);
			ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
			CreditCard.mLogger.info("PL value of ReturnDesc part Match"+ReturnDesc);

			if(ReturnCode.equalsIgnoreCase("0000") ){
				alert_msg = "BlackList Check Successfully: " ;
				formObject.setNGValue("Is_Customer_Eligibility_Part","Y");   
				CreditCard.mLogger.info("PL value of BlacklistFlag_Part Customer is Blacklisted StatusType"+formObject.getNGValue("Is_Customer_Eligibility_Part"));
				StatusType= (outputResponse.contains("<StatusType>")) ? outputResponse.substring(outputResponse.indexOf("<StatusType>")+"</StatusType>".length()-1,outputResponse.indexOf("</StatusType>")):"";
				CreditCard.mLogger.info("PL value of BlacklistFlag_Part"+"Customer is Blacklisted StatusType"+StatusType);
				outputResponse = outputResponse.substring(outputResponse.indexOf("<CustomerBlackListResponse>")+27, outputResponse.indexOf("</CustomerBlackListResponse>"));
				System.out.println(outputResponse);
				outputResponse = "<?xml version=\"1.0\"?><Response>" + outputResponse;
				outputResponse = outputResponse+"</Response>";
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				InputSource is = new InputSource(new StringReader(outputResponse));

				Document doc = builder.parse(is);
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
					CreditCard.mLogger.info("PL value of CustomerStatus"+"Customer is Blacklisted StatusType"+CustomerStatus);


					// Finacle_CRM.add(ExtBlackDate);
					// Finacle_CRM.add(ExtBlackReason);

					/*outputResponse = (outputResponse.contains("<StatusInfo>")) ? outputResponse.substring(outputResponse.indexOf("</StatusInfo>"),outputResponse.length()-1):"";
              // SKLogger.writeLog("PL value of BlacklistFlag_Part","Customer is BlacklistedoutputResponse111 outputResponse"+outputResponse);
               if(outputResponse.contains(StatusType)){
               //     SKLogger.writeLog("PL value of BlacklistFlag_Part","Customer is Blacklisted outputResponse"+outputResponse);
                   StatusType= (outputResponse.contains("<StatusType>")) ? outputResponse.substring(outputResponse.indexOf("<StatusType>")+"</StatusType>".length()-1,outputResponse.indexOf("</StatusType>")):"";
                   StatusFlag= (outputResponse.contains("<StatusFlag>")) ? outputResponse.substring(outputResponse.indexOf("<StatusFlag>")+"</StatusFlag>".length()-1,outputResponse.indexOf("</StatusFlag>")):"";
                   //SKLogger.writeLog("PL value of BlacklistFlag_Part","Customer is Blacklisted StatusCode");
                   PL_SKLogger.writeLog("PL value of BlacklistFlag_Part","Customer is StatusType StatusFlag"+StatusType+","+StatusFlag);
               }*/

					//ended
				}
			}		
			else{
				formObject.setNGValue("Is_Customer_Eligibility_Part","N"); 
				alert_msg = "BlackList Check failed Please contact administrator";
				formObject.setNGValue("Is_Customer_Eligibility_Part","N");    
				CreditCard.mLogger.info("PL value of BlacklistFlag_Part Customer is Blacklisted StatusType"+formObject.getNGValue("Is_Customer_Eligibility_Part"));
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
			BlacklistGrid.add(Wi_Name);
			CreditCard.mLogger.info("PL value of BlacklistFlag_Part"+"after adding in the grid");
			CreditCard.mLogger.info("RLOS Common# getOutputXMLValues()"+ "$$AKSHAY$$List to be added inFinacle CRM grid: "+ BlacklistGrid.toString());

			formObject.addItemFromList("cmplx_PartMatch_Blacklist_Grid",BlacklistGrid);
			CreditCard.mLogger.info("PL value of BlacklistFlag_Part"+"after adding in the grid11111");       
			
			throw new ValidatorException(new FacesMessage(alert_msg));
			}
			catch(Exception ex)
			{
				CreditCard.logException(ex);
			}
		}
		//Changes done by aman to correctly save the value in the grid
		//ended here for BlackList Call
		//ended here for BlackList Call
		if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_AddToAverage")){
			//addToAvgRepeater();
		}
		if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_AddToTurnover")){
			//addToTrnoverGrid();
		}


		if(pEvent.getSource().getName().equalsIgnoreCase("cmplx_DEC_ContactPointVeri"))
		{
			CreditCard.mLogger.info("CC val cmplx_DEC_ContactPointVeri "+ "Value of cmplx_DEC_ContactPointVeri is:"+formObject.getNGValue("cmplx_DEC_ContactPointVeri"));
			if(formObject.getNGValue("cmplx_DEC_ContactPointVeri").equalsIgnoreCase("") || formObject.getNGValue("cmplx_DEC_ContactPointVeri").equalsIgnoreCase("null") || formObject.getNGValue("cmplx_DEC_ContactPointVeri").equalsIgnoreCase("false") )
			{
				CreditCard.mLogger.info("CC val change "+ "Inside Y of CPV required");
				formObject.setNGValue("CPV_REQUIRED","Y");
			}
			else
			{
				CreditCard.mLogger.info("CC val change "+ "Inside N of CPV required");
				formObject.setNGValue("CPV_REQUIRED","N");
			}
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_CalculateTotal")){
			try {
				CreditCard.mLogger.info( "Inside Customer_save button: ");
				CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_JAN");	
				CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_FEB");	
				CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_MAR");	
				CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_APR");	
				CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_MAY");	
				CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_JUN");	
				CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_JUL");	
				CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_AUG");	
				CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_SEP");	
				CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_OCT");	
				CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_NOV");	
				CalculateRepeater("cmplx_FinacleCore_cmplx_AvgBalanceNBC_DEC");	


			} catch (Exception e) {
				CreditCard.mLogger.info( e.toString());
			}
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_CalculateTurnover")){
			try {
				CreditCard.mLogger.info( "Inside Customer_save button: ");
				CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_JAN1");	
				CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_FEB1");	
				CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_MAR1");	
				CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_APR1");	
				CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_MAY1");	
				CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_JUN1");	
				CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_JUL1");	
				CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_AUG1");	
				CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_SEP1");	
				CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_OCT1");	
				CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_NOV1");	
				CalculateRepeaterTrnover("cmplx_FinacleCore_cmplx_TurnoverNBC_DEC1");	


			} catch (Exception e) {
				CreditCard.mLogger.info( e.toString());
			}


			/*CalculateRepeater(,formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));	
								CalculateRepeater(,formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));	
								CalculateRepeater(,formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));	
								CalculateRepeater(,formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));	
								CalculateRepeater(,formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));	
								CalculateRepeater(,formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));	
								CalculateRepeater(,formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));	
								CalculateRepeater(,formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));	*/

		}
		
		if(pEvent.getSource().getName().equalsIgnoreCase("Reference_Details_Save")){
			formObject.saveFragment("Reference_Details");
		}
	
	
		if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails_Button2")){
			formObject.setNGValue("CardDetailsGR_WiName",formObject.getWFWorkitemName());
			CreditCard.mLogger.info( "Inside add button: add of card details");
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_CardDetails_cmpmx_gr_cardDetails");
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails_Button3")){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_CardDetails_cmpmx_gr_cardDetails");
		}

		if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails_Button4")){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_CardDetails_cmpmx_gr_cardDetails");
		}
		
		if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails_add")){
			formObject.setNGValue("CardDetailsGR_WiName",formObject.getWFWorkitemName());
			CreditCard.mLogger.info(formObject.getNGValue("CardDetailsGR_WiName"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_CardDetails_cmplx_CardCRNDetails");
			CreditCard.mLogger.info("after adding in the grid");
			
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails_modify")){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_CardDetails_cmplx_CardCRNDetails");
			CreditCard.mLogger.info( "Inside add button22: modify of card details");
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails_delete")){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_CardDetails_cmplx_CardCRNDetails");
			CreditCard.mLogger.info( "Inside add button33: delete of card details");
			
		}
		
		// added by abhishek on 17 august 2017
			if (pEvent.getSource().getName().equalsIgnoreCase("FATCA_Button1")){
				String text=formObject.getNGItemText("cmplx_FATCA_listedreason", formObject.getSelectedIndex("cmplx_FATCA_listedreason"));
				CreditCard.mLogger.info( "Inside FATCA_Button1 "+text);
				formObject.addItem("cmplx_FATCA_selectedreason", text);
				try {
					formObject.removeItem("cmplx_FATCA_listedreason", formObject.getSelectedIndex("cmplx_FATCA_listedreason"));
					formObject.setSelectedIndex("cmplx_FATCA_listedreason", -1);

				}catch (Exception e) {
					// TODO Auto-generated catch block
					CreditCard.logException(e);
				}

			}

			
			 if (pEvent.getSource().getName().equalsIgnoreCase("FATCA_Button2")){
				CreditCard.mLogger.info( "Inside FATCA_Button2 ");
				formObject.addItem("cmplx_FATCA_listedreason", formObject.getNGItemText("cmplx_FATCA_selectedreason", formObject.getSelectedIndex("cmplx_FATCA_selectedreason")));
				try {
					formObject.removeItem("cmplx_FATCA_selectedreason", formObject.getSelectedIndex("cmplx_FATCA_selectedreason"));
					formObject.setSelectedIndex("cmplx_FATCA_selectedreason", -1);
				} catch (Exception e) {
					CreditCard.logException(e);
				}
			}
			
			 if(pEvent.getSource().getName().equalsIgnoreCase("FATCA_Save")){
				formObject.saveFragment("FATCA");


				alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL036");

				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			 
			  if(pEvent.getSource().getName().equalsIgnoreCase("KYC_Save")){
					CreditCard.mLogger.info( "Inside KYC save button");
					formObject.saveFragment("KYC");
					alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL041");
					throw new ValidatorException(new FacesMessage(alert_msg));
				}
			 
			 if(pEvent.getSource().getName().equalsIgnoreCase("OECD_Save")){
					formObject.saveFragment("OECD");
					alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL045");

					throw new ValidatorException(new FacesMessage(alert_msg));
				}
				
				if (pEvent.getSource().getName().equalsIgnoreCase("OECD_add")){					
					formObject.setNGValue("OECD_winame",formObject.getWFWorkitemName());
					CreditCard.mLogger.info( "Inside add button: "+formObject.getNGValue("OECD_winame"));
					//below code by saurabh on 22md july 17.
					formObject.setEnabled("cmplx_OECD_cmplx_GR_OecdDetails_TinReason",true);
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_OECD_cmplx_GR_OecdDetails");
				}


			  if (pEvent.getSource().getName().equalsIgnoreCase("OECD_modify")){
					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_OECD_cmplx_GR_OecdDetails");
				}


			 if (pEvent.getSource().getName().equalsIgnoreCase("OECD_delete")){
					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_OECD_cmplx_GR_OecdDetails");
				}
			 if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_Add"))
				{

					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_IncomeDetails_Cmplx_gr_IncomdeDetails");
				}
				if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_Modify"))
				{

					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_IncomeDetails_Cmplx_gr_IncomdeDetails");
				}
				if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_Delete"))
				{

					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_IncomeDetails_Cmplx_gr_IncomdeDetails");
				}
				// added by abhishek as per CC FSD
				if (pEvent.getSource().getName().equalsIgnoreCase("BT_Add")){
					//formObject.setNGValue("Address_wi_name",formObject.getWFWorkitemName());
					//CreditCard.mLogger.info( "Inside add button: "+formObject.getNGValue("Address_wi_name"));
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_CC_Loan_cmplx_btc");
				}
				if (pEvent.getSource().getName().equalsIgnoreCase("BT_Modify")){
					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_CC_Loan_cmplx_btc");
				}
				if (pEvent.getSource().getName().equalsIgnoreCase("BT_Delete")){
					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_CC_Loan_cmplx_btc");
				}
				if (pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_Add")){
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_WorldCheck_WorldCheck_Grid");
				}
				if (pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_Modify")){
					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_WorldCheck_WorldCheck_Grid");
				}
				if (pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_Delete")){
					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_WorldCheck_WorldCheck_Grid");
				}
				if (pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_Save")){
					formObject.saveFragment("WorldCheck1_Frame1");
				}
	
				 if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Button2"))
		            {
					 
					 getCustAddress_details();
		                hm.put("Liability_New_Button1","Clicked");
		                //added
		                String NTB_flag = formObject.getNGValue("cmplx_Customer_NTB");
		                String NEP_flag = formObject.getNGValue("cmplx_Customer_NEP");
		                String CIF_no = formObject.getNGValue("cmplx_Customer_CIFNo");
		                CreditCard.mLogger.info( "inside create Account/Customer NTB value: "+NTB_flag );
		                if(NTB_flag.equalsIgnoreCase("true") || NEP_flag.equalsIgnoreCase("true")||CIF_no.equalsIgnoreCase("")){
		                	formObject.setNGValue("curr_user_name",formObject.getUserName());
		                	int prd_count=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		                	//Changes Done for Create Customer self Employed Case
		                	
		                	String Emp_type = "SAL";
		    				if(prd_count>0 && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6)!=null &&( formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6).equalsIgnoreCase("Self Employed")||formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6).equalsIgnoreCase("SE")) ){
		    					
		    						Emp_type = "SE";
		    					
		    				}
		                     outputResponse = GenerateXML("NEW_CUSTOMER_REQ",Emp_type);
		                     ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
		                    CreditCard.mLogger.info(ReturnCode);
		                    if(ReturnCode.equalsIgnoreCase("0000")){
		                        valueSetCustomer(outputResponse,"");   
		                        formObject.setNGValue("cmplx_DEC_New_CIFID", formObject.getNGValue("cmplx_Customer_CIFNO"));
		                        CreditCard.mLogger.info("Inside if of New customer Req");
		                        formObject.RaiseEvent("WFSave");

		                    	//outputResponse = GenerateXML("NEW_ACCOUNT_REQ","");
			                       // ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			                        CreditCard.mLogger.info(ReturnCode);
			                        
			                        // if(ReturnCode.equalsIgnoreCase("0000") ){
			                            // valueSetCustomer(outputResponse);    
			                            // formObject.setNGValue("Is_Account_Create","Y");
			                            // formObject.setNGValue("EligibilityStatus","Y");
			                            // formObject.setNGValue("EligibilityStatusCode","Y");
			                            // formObject.setNGValue("EligibilityStatusDesc","Y");
			                            // }
			                            // else{
			                                // formObject.setNGValue("Is_Account_Create","N");
			                            // }
			                        CreditCard.mLogger.info(formObject.getNGValue("Is_Account_Create"));
			                        CreditCard.mLogger.info(formObject.getNGValue("EligibilityStatus"));
			                        CreditCard.mLogger.info(formObject.getNGValue("EligibilityStatusCode"));
			                        CreditCard.mLogger.info(formObject.getNGValue("EligibilityStatusDesc"));
			                       alert_msg = NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL011");
		                    
		                        
		                        }
		                        else{
		                            CreditCard.mLogger.info("Inside else of New Customer Req");
		                            alert_msg = NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL010");
		                        }
		                }
		                
		              
		                throw new ValidatorException(new FacesMessage(alert_msg));
		        }
	
	
	//added by yash
	if(pEvent.getSource().getName().equalsIgnoreCase("CC_Creation_Card_Services"))
	{
        formObject.RaiseEvent("WFSave");
        CreditCard.mLogger.info("inside button click");
        String Product_Name=formObject.getNGValue("cmplx_CCCreation_pdt");
        CreditCard.mLogger.info(""+Product_Name);
        if(Product_Name.equalsIgnoreCase("Limit Change Request")){
            outputResponse = GenerateXML("CARD_SERVICES_REQUEST","Limit Change Request");
            ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
            CreditCard.mLogger.info(ReturnCode);
            ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
            CreditCard.mLogger.info(ReturnDesc);
            if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000") ){
                CreditCard.mLogger.info("value of Customer_Details inside inquiry code");
                formObject.setNGValue("Is_CardServices","Y");
                CreditCard.mLogger.info(""+formObject.getNGValue("Is_CardServices"));
                CreditCard.mLogger.info("CARD SERVICES RUNNING Limit Change Request");
                outputResponse = GenerateXML("CARD_NOTIFICATION","Limit Change Request");
                ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                CreditCard.mLogger.info(ReturnCode);
                ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                CreditCard.mLogger.info(ReturnDesc);
                if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")){
                    CreditCard.mLogger.info("value of Customer_Details inside lock code");
                    formObject.setNGValue("Is_CardNotifiaction","Y");
                    CreditCard.mLogger.info(""+formObject.getNGValue("Is_CardNotifiaction"));
                }
            }
        }
        //for another product
        if(Product_Name.equalsIgnoreCase("Financial Services Request")){
            outputResponse = GenerateXML("CARD_SERVICES_REQUEST","Financial Services Request");
            ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
            CreditCard.mLogger.info(ReturnCode);
            ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
            CreditCard.mLogger.info(ReturnDesc);
            if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")){
                CreditCard.mLogger.info("value of Customer_Details inside inquiry code");
                formObject.setNGValue("Is_CardServices","Y"); 
                CreditCard.mLogger.info(""+formObject.getNGValue("Is_CardServices"));
                CreditCard.mLogger.info("CARD SERVICES RUNNING Financial Services Request");
                outputResponse = GenerateXML("CARD_NOTIFICATION","Financial Services Request");
                ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                CreditCard.mLogger.info(ReturnCode);
                ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                CreditCard.mLogger.info(ReturnDesc);
                if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000") ){
                    CreditCard.mLogger.info("value of Customer_Details inside lock code");
                    formObject.setNGValue("Is_CardNotifiaction","Y");
                    CreditCard.mLogger.info(""+formObject.getNGValue("Is_CardNotifiaction"));
                }
            }
        }
        //for another product
        if(Product_Name.equalsIgnoreCase("Product Upgrade Request")){
            outputResponse = GenerateXML("CARD_SERVICES_REQUEST","Product Upgrade Request");
            ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
            CreditCard.mLogger.info(ReturnCode);
            ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
            CreditCard.mLogger.info(ReturnDesc);
            if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000") ){
                CreditCard.mLogger.info("value of Customer_Details inside inquiry code");
                formObject.setNGValue("Is_CardServices","Y"); 
                CreditCard.mLogger.info(""+formObject.getNGValue("Is_CardServices"));
                CreditCard.mLogger.info("CARD SERVICES RUNNING Product Upgrade Request");
                outputResponse = GenerateXML("CARD_NOTIFICATION","Product Upgrade Request");
                ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                CreditCard.mLogger.info(ReturnCode);
                ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                CreditCard.mLogger.info(ReturnDesc);
                if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")){
                    CreditCard.mLogger.info("value of Customer_Details inside lock code");
                    formObject.setNGValue("Is_CardNotifiaction","Y");
                    CreditCard.mLogger.info(""+formObject.getNGValue("Is_CardNotifiaction"));
                }
            }
        }
        formObject.RaiseEvent("WFSave");
    }
// ended by yash
	
	if(pEvent.getSource().getName().equalsIgnoreCase("Loan_Disbursal_Save")){
		formObject.saveFragment("Loan_Disbursal");
	}
	if(pEvent.getSource().getName().equalsIgnoreCase("CC_Creation_Save")){
		formObject.saveFragment("CC_Creation");
	}
	if(pEvent.getSource().getName().equalsIgnoreCase("Limit_Inc_Save")){
		formObject.saveFragment("Limit_Inc");
	}
	
	if(pEvent.getSource().getName().equalsIgnoreCase("CC_Creation_Button2")){
        formObject.RaiseEvent("WFSave");
        CreditCard.mLogger.info("inside button click");
        String Product_Name=formObject.getNGValue("cmplx_CCCreation_pdt");
        CreditCard.mLogger.info(""+Product_Name);
        if(Product_Name.equalsIgnoreCase("Limit Change Request")){
            outputResponse = GenerateXML("CARD_SERVICES_REQUEST","Limit Change Request");
            ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
            CreditCard.mLogger.info(ReturnCode);
            ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
            CreditCard.mLogger.info(ReturnDesc);
            if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000") ){
                CreditCard.mLogger.info("value of Customer_Details inside inquiry code");
                formObject.setNGValue("Is_CardServices","Y");
                CreditCard.mLogger.info(""+formObject.getNGValue("Is_CardServices"));
                CreditCard.mLogger.info("CARD SERVICES RUNNING Limit Change Request");
                outputResponse = GenerateXML("CARD_NOTIFICATION","Limit Change Request");
                ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                CreditCard.mLogger.info(ReturnCode);
                ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                CreditCard.mLogger.info(ReturnDesc);
                if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")){
                    CreditCard.mLogger.info("value of Customer_Details inside lock code");
                    formObject.setNGValue("Is_CardNotifiaction","Y");
                    CreditCard.mLogger.info(""+formObject.getNGValue("Is_CardNotifiaction"));
                }
            }
        }
        //for another product
        if(Product_Name.equalsIgnoreCase("Financial Services Request")){
            outputResponse = GenerateXML("CARD_SERVICES_REQUEST","Financial Services Request");
            ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
            CreditCard.mLogger.info(ReturnCode);
            ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
            CreditCard.mLogger.info(ReturnDesc);
            if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")){
                CreditCard.mLogger.info("value of Customer_Details inside inquiry code");
                formObject.setNGValue("Is_CardServices","Y"); 
                CreditCard.mLogger.info(""+formObject.getNGValue("Is_CardServices"));
                CreditCard.mLogger.info("CARD SERVICES RUNNING Financial Services Request");
                outputResponse = GenerateXML("CARD_NOTIFICATION","Financial Services Request");
                ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                CreditCard.mLogger.info(ReturnCode);
                ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                CreditCard.mLogger.info(ReturnDesc);
                if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000") ){
                    CreditCard.mLogger.info("value of Customer_Details inside lock code");
                    formObject.setNGValue("Is_CardNotifiaction","Y");
                    CreditCard.mLogger.info(""+formObject.getNGValue("Is_CardNotifiaction"));
                }
            }
        }
        //for another product
        if(Product_Name.equalsIgnoreCase("Product Upgrade Request")){
            outputResponse = GenerateXML("CARD_SERVICES_REQUEST","Product Upgrade Request");
            ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
            CreditCard.mLogger.info(ReturnCode);
            ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
            CreditCard.mLogger.info(ReturnDesc);
            if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000") ){
                CreditCard.mLogger.info("value of Customer_Details inside inquiry code");
                formObject.setNGValue("Is_CardServices","Y"); 
                CreditCard.mLogger.info(""+formObject.getNGValue("Is_CardServices"));
                CreditCard.mLogger.info("CARD SERVICES RUNNING Product Upgrade Request");
                outputResponse = GenerateXML("CARD_NOTIFICATION","Product Upgrade Request");
                ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                CreditCard.mLogger.info(ReturnCode);
                ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                CreditCard.mLogger.info(ReturnDesc);
                if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")){
                    CreditCard.mLogger.info("value of Customer_Details inside lock code");
                    formObject.setNGValue("Is_CardNotifiaction","Y");
                    CreditCard.mLogger.info(""+formObject.getNGValue("Is_CardNotifiaction"));
                }
            }
        }
        formObject.RaiseEvent("WFSave");
    }

	if(pEvent.getSource().getName().equalsIgnoreCase("CC_Creation_Update_Customer")){
        CreditCard.mLogger.info("inside Update_Customer");  
        outputResponse = GenerateXML("CUSTOMER_DETAILS","Inquire");
        ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
       CreditCard.mLogger.info(ReturnCode);
       ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
       CreditCard.mLogger.info(ReturnDesc);
       if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")){
           CreditCard.mLogger.info("value of Customer_Details inside inquiry code");
          formObject.setNGValue("Is_CustInquiry_Disbursal","Y"); 
          CreditCard.mLogger.info("Inquiry Flag Value"+formObject.getNGValue("Is_CustInquiry_Disbursal")); 
           CreditCard.mLogger.info("inside Update_Customer");  
           String cif_status = (outputResponse.contains("<CustomerStatus>")) ? outputResponse.substring(outputResponse.indexOf("<CustomerStatus>")+"</CustomerStatus>".length()-1,outputResponse.indexOf("</CustomerStatus>")):"";
           if(cif_status.equalsIgnoreCase("ACTVE")){
        outputResponse = GenerateXML("CUSTOMER_DETAILS","Lock");
        ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
       CreditCard.mLogger.info(ReturnCode);
       ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
       CreditCard.mLogger.info(ReturnDesc);
       if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")){
       CreditCard.mLogger.info("value of Customer_Details inside lock code");
         formObject.setNGValue("Is_CustLock_Disbursal","Y");
         CreditCard.mLogger.info("Inquiry Flag Is_CustLock_Disbursal value"+formObject.getNGValue("Is_CustLock_Disbursal")); 
           
           CreditCard.mLogger.info("Customer_Details is generated");
       //    CreditCard.mLogger.info(formObject.getNGValue("Is_ACCOUNT_MAINTENANCE_REQ"));
            outputResponse = GenerateXML("CUSTOMER_DETAILS","UnLock");
            ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
               CreditCard.mLogger.info("inside unlock");
               CreditCard.mLogger.info(ReturnCode);
               formObject.setNGValue("Is_CustUnLock_Disbursal","Y");
               CreditCard.mLogger.info("Inquiry Flag Is_CustUnLock_Disbursal value"+formObject.getNGValue("Is_CustUnLock_Disbursal"));
               ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
               CreditCard.mLogger.info(ReturnDesc);
               if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")){
                    outputResponse = GenerateXML("CUSTOMER_UPDATE_REQ","");
                    ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
                       CreditCard.mLogger.info(ReturnCode);
                       ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
                       CreditCard.mLogger.info(ReturnDesc);
                     //  Message =(outputResponse.contains("<Message>")) ? outputResponse.substring(outputResponse.indexOf("<Message>")+"</Message>".length()-1,outputResponse.indexOf("</Message>")):"";    
                     //  CreditCard.mLogger.info(Message);
                       
                       if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")){
                           formObject.setNGValue("Is_CUSTOMER_UPDATE_REQ_Disbursal","Y");
                           CreditCard.mLogger.info("value of CUSTOMER_UPDATE_REQ"+formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
                           valueSetCustomer(outputResponse,"");    
                           CreditCard.mLogger.info("CUSTOMER_UPDATE_REQ is generated");
                           CreditCard.mLogger.info(formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ_Disbursal"));
                       }
                       else{
                           CreditCard.mLogger.info("CUSTOMER_UPDATE_REQ is not generated");
                           formObject.setNGValue("Is_CUSTOMER_UPDATE_REQ_Disbursal","N");
                       }
                       CreditCard.mLogger.info(formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ_Disbursal"));
                       formObject.RaiseEvent("WFSave");
                       CreditCard.mLogger.info("after saving the flag");
                       if((formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ_Disbursal").equalsIgnoreCase("Y")))
                       { 
                           CreditCard.mLogger.info("inside if condition");
                           formObject.setEnabled("Update_Customer", false); 
                           throw new ValidatorException(new CustomExceptionHandler("Customer Updated Successful!!","Update_Customer#Customer Updated Successful!!","",hm));
                       }
                       else{
                           formObject.setEnabled("DecisionHistory_Button3", true);
                           throw new ValidatorException(new CustomExceptionHandler("Customer Updated Fail!!","Update_Customer#Customer Updated Fail!!","",hm));
                       }
               }
               else{
                   CreditCard.mLogger.info("Customer_Details unlock is not generated");
                   
               }
               }
       else{
           CreditCard.mLogger.info("Customer_Details lock is not generated");
           }
   }
           else {
             CreditCard.mLogger.info( "Error in Cif Enquiry operation CIF Staus is: "+ cif_status);
             throw new ValidatorException(new  CustomExceptionHandler("Customer Is InActive!!","FetchDetails#Customer Is InActive!!","",hm));
         }
       //added one more here
}
else{
       CreditCard.mLogger.info("Customer_Details Inquiry is not generated");
}
}
	
	if (pEvent.getSource().getName().equalsIgnoreCase("BussinessVerification1_Button1")) {
		//loadPicklist_Address();
		CreditCard.mLogger.info( "Business Verification Details are saved"); 
		formObject.saveFragment("Business_Verification1");
		alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL005");
		CreditCard.mLogger.info( "Business Verification Details are saved123"); 
		throw new ValidatorException(new FacesMessage(alert_msg));
		
	}
	if (pEvent.getSource().getName().equalsIgnoreCase("EmploymentVerification_Button1")) {
		//loadPicklist_Address();
		CreditCard.mLogger.info( "Employment Verification Details are saved"); 
		formObject.saveFragment("Employment_Verification");
		alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL026");
		CreditCard.mLogger.info( "Employment Verification Details are saved123"); 
		throw new ValidatorException(new FacesMessage(alert_msg));
		
	}
	
	if(pEvent.getSource().getName().equalsIgnoreCase("BankingCheck_Button1")){
		
		CreditCard.mLogger.info( "Banking Check Details are saved"); 
		formObject.saveFragment("Banking_Check");
		alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL002");
		CreditCard.mLogger.info( "Banking Check Details are saved123"); 
		throw new ValidatorException(new FacesMessage(alert_msg));
	}
	
	if(pEvent.getSource().getName().equalsIgnoreCase("supervisorsection_Button1")){
		formObject.saveFragment("supervisor_section");
	}
	
	else if (pEvent.getSource().getName().equalsIgnoreCase("CustDetailVerification1_Button2")) {
		//FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String EmiratesID=formObject.getNGValue("cmplx_CustDetailverification1_EmiratesId");
		String CIFID=formObject.getNGValue("cmplx_CustDetailverification1_CID_ID");
		CreditCard.mLogger.info( "EmiratesID$"+EmiratesID+"$");
		String query=null;
		if(EmiratesID.trim().equalsIgnoreCase(""))
			//12th sept
		//	query="select distinct(Emirates_ID),Customer_Name,Cif_ID,FCU_Remarks,FCU_Analyst_Name,FCU_Decision from NG_RLOS_FCU_Decision where Cif_ID Like '%"+CIFID+"%'";
			query="select CustName,CifId,remarks,userName,Decisiom from ng_rlos_gr_decision where CifId Like '%"+CIFID+"%'";
		//12th sept
		else
			//12th sept
			//query="select distinct(Emirates_ID),Customer_Name,Cif_ID,FCU_Remarks,FCU_Analyst_Name,FCU_Decision from NG_RLOS_FCU_Decision where Emirates_ID Like '%"+EmiratesID + "%' or Cif_ID Like '%"+CIFID+"'";
			query="select CustName,CifId,remarks,userName,Decisiom from ng_rlos_gr_decision where EmiratesID Like '%"+EmiratesID + "%' or CifId Like '%"+CIFID+"'";
		//12th sept
		CreditCard.mLogger.info( "query is: "+query);
			//populatePickListCustdetailWindow(query,"CustDetailVerification1_Button2", "Emirates ID, Customer Name,Cif ID,Fcu Remarks,FCU Analyst Name,FCU Decision", true, 20);
	            List<List<String>> list=formObject.getDataFromDataSource(query);
	            //m_objPickList.populateData(query);
	            CreditCard.mLogger.info("##Arun");
	            for (List<String> a : list) 
				{
					
					formObject.addItemFromList("CustDetailVerification1_ListView1", a);
				}
	                //SKLogger.writeLog("PL","Column "+i+"is: "+ m_objPickList.getSelectedValue().get(i));
	        

	}
	
	if(pEvent.getSource().getName().equalsIgnoreCase("HomeCountryVerification_Button1")){
		formObject.saveFragment("HomeCountry_Verification");
	}
	if(pEvent.getSource().getName().equalsIgnoreCase("CustDetailVerification_Button1")){
		formObject.saveFragment("Customer_Details_Verification");
	}
	if(pEvent.getSource().getName().equalsIgnoreCase("BussinessVerification_Button1")){
		formObject.saveFragment("Business_Verification");
	}
	if(pEvent.getSource().getName().equalsIgnoreCase("ResidenceVerification_Button1")){
		formObject.saveFragment("ResidenceVerification");
	}
	if(pEvent.getSource().getName().equalsIgnoreCase("GuarantorVerification_Button1")){
		formObject.saveFragment("Guarantor_Verification");
	}
	if(pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetailVerification_Button1")){
		formObject.saveFragment("Reference_Detail_Verification");
	}
	if(pEvent.getSource().getName().equalsIgnoreCase("OfficeandMobileVerification_Button1")){
		formObject.saveFragment("Office_Mob_Verification");
	}
	
	if(pEvent.getSource().getName().equalsIgnoreCase("LoanandCard_Button1")){
		formObject.saveFragment("LoanCard_Details_Check");
	}
	
	if(pEvent.getSource().getName().equalsIgnoreCase("OfficeandMobileVerification_Enable")){
		String sQuery = "SELECT activityname FROM WFINSTRUMENTTABLE with (nolock) WHERE ProcessInstanceID = '"+formObject.getWFWorkitemName()+"'";
		List<List<String>> list=formObject.getDataFromDataSource(sQuery);
		CreditCard.mLogger.info( "Enable button click::query result is::"+list );
		for(int i =0;i<list.size();i++ ){
			if(list.get(i).get(0).equalsIgnoreCase("Smart_CPV")){
				formObject.setLocked("OfficeandMobileVerification_Frame1", true);
				alert_msg = NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL006");
				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			else{
				formObject.setLocked("OfficeandMobileVerification_Frame1", false);
				formObject.setEnabled("OfficeandMobileVerification_Enable", false);
			}
		}
		//disable values fields
		formObject.setEnabled("cmplx_OffVerification_offtelno",false);
		formObject.setEnabled("cmplx_OffVerification_fxdsal_val",false);
		formObject.setEnabled("cmplx_OffVerification_accprovd_val",false);
		formObject.setEnabled("cmplx_OffVerification_desig_val",false);
		formObject.setEnabled("cmplx_OffVerification_doj_val",false);
		formObject.setEnabled("cmplx_OffVerification_cnfrminjob_val",false);
		
		formObject.setNGValue("cmplx_OffVerification_EnableFlag", true);
		formObject.RaiseEvent("WFSave");
		
	}
	//-- Above code added by abhishek as per CC FSD 2.7.3

	if (pEvent.getSource().getName().equalsIgnoreCase("Compliance_Button2"))
	{
		formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Compliance_cmplx_gr_compliance");
	}
	if(pEvent.getSource().getName().equalsIgnoreCase("Compliance_Save")){
		formObject.saveFragment("Compliance");
	}
	if("DecisionHistory_nonContactable".equalsIgnoreCase(pEvent.getSource().getName())){
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		String currDate = dateFormat.format(cal);
		CreditCard.mLogger.info( ""+currDate);
		
		formObject.setEnabled("DecisionHistory_nonContactable", false);
		formObject.setEnabled("NotepadDetails_Frame1", false);
		formObject.setEnabled("DecisionHistory_Frame1", false);
		formObject.setEnabled("SmartCheck_Frame1", false);
		formObject.setEnabled("OfficeandMobileVerification_Frame1", false);
		
		formObject.setEnabled("DecisionHistory_cntctEstablished", true);
		
		formObject.setNGValue("cmplx_DEC_Decision","Smart CPV Hold");
		
		String sQuery = "insert into ng_rlos_SmartCPV_datetime (wi_name,datetime_nonContactable) values('"+formObject.getWFWorkitemName()+"','"+currDate+"')" ;
		formObject.saveDataIntoDataSource(sQuery);
		formObject.setNGValue("cmplx_DEC_contactableFlag", true);
		formObject.RaiseEvent("WFSave");
		
	}

	// added by abhishek as per CC FSD 
	if("DecisionHistory_cntctEstablished".equalsIgnoreCase(pEvent.getSource().getName())){
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		String currDate = dateFormat.format(cal);
		CreditCard.mLogger.info( ""+currDate);
		
		formObject.setEnabled("NotepadDetails_Frame1", true);
		formObject.setEnabled("DecisionHistory_Frame1", true);
		formObject.setEnabled("SmartCheck_Frame1", true);
		formObject.setEnabled("OfficeandMobileVerification_Frame1", true);
		
		formObject.setEnabled("DecisionHistory_cntctEstablished", false);
		formObject.setEnabled("DecisionHistory_nonContactable", true);
		formObject.setNGValue("cmplx_DEC_Decision","--Select--");
		String sQuery = "insert into ng_rlos_SmartCPV_datetime (wi_name,datetime_ContactEstablished) values('"+formObject.getWFWorkitemName()+"','"+currDate+"')" ;
		formObject.saveDataIntoDataSource(sQuery);
		formObject.setNGValue("cmplx_DEC_contactableFlag", false);
		formObject.RaiseEvent("WFSave");
	}
	// added by abhishek as per CC FSD 
	if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Button4")){
		String message = CustomerUpdate();
        throw new ValidatorException(new FacesMessage(message));
	}
	if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetailsFCU_Button1")){
		//++Below code added by  nikhil 10/10/17
		formObject.setNGValue("NotepadDetailsFCU_TellogWiname",formObject.getWFWorkitemName());
		//-- Above code added by  nikhil 10/10/17
		formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NoteFCU_cmplx_gr_TellogFCU");
	}
	if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetailsFCU_Button2")){
		formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_NoteFCU_cmplx_gr_TellogFCU");
	}
	
	if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetailsFCU_Button4")){
		
		//++Below code added by  nikhil 10/10/17
		formObject.setNGValue("NotepadDetailsFCU_NotepadWiname",formObject.getWFWorkitemName());
		//-- Above code added by  nikhil 10/10/17
		formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NoteFCU_cmplx_gr_NoteFCU");
	}
	if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetailsFCU_Button6")){
		formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_NoteFCU_cmplx_gr_NoteFCU");
	}
	if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetailsFCU_Button5")){
		formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_NoteFCU_cmplx_gr_NoteFCU");
	}
	if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetailsFCU_Button3")){
		formObject.saveFragment("Notepad_Details1");
	}
	if ("NotepadDetails_Button7".equalsIgnoreCase(pEvent.getSource().getName()))
	{
	Date dNow = new Date( );

	SimpleDateFormat ft = new SimpleDateFormat ("E  hh:mm:ss a zzz");
	formObject.setNGValue("NotepadDetails_Text5",ft.format(dNow));
	}
	
}
	
	public void getDataFromALOC(FormReference formObject2, String corpName) {
		try{
			String query = "select INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,HIGH_DELINQUENCY_EMPLOYER,EMPLOYER_CATEGORY_PL_EXPAT from NG_RLOS_ALOC_OFFLINE_DATA where EMPR_NAME = '"+corpName+"'";
			List<List<String>> result = formObject2.getDataFromDataSource(query);
			if(result!=null && !result.isEmpty())  //if(result!=null && result.size()>0)
			{
				formObject2.setNGValue("indusSector", result.get(0).get(0));
				formObject2.setNGValue("indusMAcro", result.get(0).get(1));
				formObject2.setNGValue("indusMicro", result.get(0).get(2));
				formObject2.setNGValue("CompanyDetails_highdelin", result.get(0).get(3));
				formObject2.setNGValue("CompanyDetails_currempcateg", result.get(0).get(4));
			}
		}
		catch(Exception ex){
			CreditCard.mLogger.info( printException(ex));
		}
	}
	
	public void value_changed(FormEvent pEvent)
	{
		String alert_msg="";
		FormReference formObject=FormContext.getCurrentInstance().getFormReference();
		String ReqProd="";
		if (pEvent.getSource().getName().equalsIgnoreCase("Decision_Combo2")) {
			if(formObject.getWFActivityName().equalsIgnoreCase("CAD_Analyst1"))	
			{
				formObject.setNGValue("CAD_dec", formObject.getNGValue("Decision_Combo2"));
				CreditCard.mLogger.info(" In PL_Initiation VALChanged---New Value of CAD_dec is: "+ formObject.getNGValue("Decision_Combo2"));

			}

			else{

				formObject.setNGValue("decision", formObject.getNGValue("Decision_Combo2"));
				CreditCard.mLogger.info(" In PL_Initiation VALChanged---New Value of decision is: "+ formObject.getNGValue("Decision_Combo2"));
			}
		}
		 //added by yash on 30/8/2017
		// Code added to Load targetSegmentCode on basis of code
		if (pEvent.getSource().getName().equalsIgnoreCase("ApplicationCategory")){
			CreditCard.mLogger.info("RLOS val change "+ "Value of dob is:"+formObject.getNGValue("ApplicationCategory"));
			String subproduct = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2);
			String appCategory=formObject.getNGValue("ApplicationCategory");
			CreditCard.mLogger.info("RLOS val change "+ "Value of subproduct is:"+formObject.getNGValue("subproduct"));
			if(appCategory!=null &&  appCategory.equalsIgnoreCase("BAU")){
				LoadPickList("TargetSegmentCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and BAU='Y' and (product = 'CC' or product='B') order by code");
			}
			else if(appCategory!=null &&  appCategory.equalsIgnoreCase("Surrogate")){
				LoadPickList("TargetSegmentCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and Surrogate='Y' and (product = 'CC' or product='B') order by code");
			}
			else{
			LoadPickList("TargetSegmentCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and (product = 'CC' or product='B') order by code");	
			}
		}
		// Code added to Load targetSegmentCode on basis of code
	 
		
		if (pEvent.getSource().getName().equalsIgnoreCase("ReqProd")){
			ReqProd=formObject.getNGValue("ReqProd");
			CreditCard.mLogger.info("CC val change "+ "Value of ReqProd is:"+ReqProd);
			loadPicklistProduct(ReqProd);
		}

		if (pEvent.getSource().getName().equalsIgnoreCase("subProd")){
			CreditCard.mLogger.info("CC val change "+ "Value of SubProd is:"+formObject.getNGValue("subProd"));
			formObject.clear("AppType");
			formObject.setNGValue("AppType","--Select--");
			if(formObject.getNGValue("subProd").equalsIgnoreCase("Business titanium Card")){
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='BTC'");
				formObject.setNGValue("EmpType","Self Employed");
			}	
			else if(formObject.getNGValue("subProd").equalsIgnoreCase("Instant Money")){
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType  with (nolock) where subProdFlag='IM'");
				formObject.setNGValue("EmpType","Salaried");
			}

			else if(formObject.getNGValue("subProd").equalsIgnoreCase("Limit Increase"))
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='LI'");

			else if(formObject.getNGValue("subProd").equalsIgnoreCase("Salaried Credit Card"))
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='SAL'");

			else if(formObject.getNGValue("subProd").equalsIgnoreCase("Self Employed Credit Card"))
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='SE'");

			else if(formObject.getNGValue("subProd").equalsIgnoreCase("Expat Personal Loans"))
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='EXP'");

			else if(formObject.getNGValue("subProd").equalsIgnoreCase("National Personal Loans"))
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='NAT'");

			else if(formObject.getNGValue("subProd").equalsIgnoreCase("Pre-Approved"))
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='PA'");

			else if(formObject.getNGValue("subProd").equalsIgnoreCase("Product Upgrade"))
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='PU'");
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("Product_type")){

			String ProdType=formObject.getNGValue("Product_type");
			CreditCard.mLogger.info("CC Value Change Prod_Type"+ProdType);
			formObject.clear("CardProd");
			formObject.setNGValue("CardProd","--Select--");
			if(ProdType.equalsIgnoreCase("Conventional"))
				LoadPickList("CardProd", "select '--Select--' union select convert(varchar,description) from ng_MASTER_CardProduct with (nolock) where code NOT like '%AMAL%' ORDER BY description");

			if(ProdType.equalsIgnoreCase("Islamic"))
				LoadPickList("CardProd", "select '--Select--' union select convert(varchar,description) from ng_MASTER_CardProduct with (nolock) where code like '%AMAL%' ORDER BY description");
		}
		
		if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_WorldCheck_WorldCheck_Grid_dob")){
			CreditCard.mLogger.info("RLOS val change "+ "Value of cmplx_WorldCheck_WorldCheck_Grid_dob is:"+formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid_dob"));
			getAgeWorldCheck(formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid_dob"));
		}
		
		if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_Liability_New_overrideIntLiab")){
			if(formObject.getNGValue("cmplx_Liability_New_overrideIntLiab").equalsIgnoreCase("true")){

				formObject.setVisible("Liability_New_Overwrite", true);

			}
			else{
				formObject.setVisible("Liability_New_Overwrite", false);
			}
		}
	
		//code added for LOB
				if ("TLIssueDate".equalsIgnoreCase(pEvent.getSource().getName())){
					CreditCard.mLogger.info( "Value of TLIssueDate is:"+formObject.getNGValue("TLIssueDate"));
					getAge(formObject.getNGValue("TLIssueDate"),"lob");
				}
				//code added for LOB
				if ("indusSector".equalsIgnoreCase(pEvent.getSource().getName())){
					
					LoadPickList("indusMAcro", "select '--Select--' union select convert(varchar, macro) from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where IndustrySector='"+formObject.getNGValue("indusSector")+"' and IsActive='Y'");
				}

				if ("indusMAcro".equalsIgnoreCase(pEvent.getSource().getName())){
					LoadPickList("indusMicro", "select '--Select--' union select convert(varchar, micro) from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where Macro='"+formObject.getNGValue("indusMAcro")+"' and IsActive='Y'");
				}

				else if ("cmplx_Customer_DOb".equalsIgnoreCase(pEvent.getSource().getName())){
					CreditCard.mLogger.info( "Value of dob is:"+formObject.getNGValue("cmplx_Customer_DOb"));
					getAge(formObject.getNGValue("cmplx_Customer_DOb"),"cmplx_Customer_age");
				}
				else if ("cmplx_EmploymentDetails_DOJ".equalsIgnoreCase(pEvent.getSource().getName())){
					//SKLogger.writeLog("RLOS val change ", "Value of dob is:"+formObject.getNGValue("cmplx_EmploymentDetails_DOJ"));
					getAge(formObject.getNGValue("cmplx_EmploymentDetails_DOJ"),"cmplx_EmploymentDetails_LOS");
				}
				else if ("cmplx_EmploymentDetails_EmpIndusSector".equalsIgnoreCase(pEvent.getSource().getName())){

					LoadPickList("cmplx_EmploymentDetails_Indus_Macro", "select '--Select--' union select convert(varchar, macro) from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where IndustrySector='"+formObject.getNGValue("cmplx_EmploymentDetails_EmpIndusSector")+"' and IsActive='Y'");
				}
				//added by yashfor CC FSD
				else if ("NotepadDetails_notedesc".equalsIgnoreCase(pEvent.getSource().getName())){
					String notepad_desc = formObject.getNGValue("NotepadDetails_notedesc");
					 //LoadPickList("NotepadDetails_notecode", "select '--Select--' union select convert(varchar, description) from ng_master_notedescription with (nolock)  where Description=q'["+notepad_desc+"]'","NotepadDetails_notecode");
					 String sQuery = "select code,workstep from ng_master_notedescription where Description='" +  notepad_desc + "'";
					 CreditCard.mLogger.info(sQuery);
					 List<List<String>> recordList = formObject.getDataFromDataSource(sQuery);
					 //if(recordList.get(0).get(0)!= null && recordList.get(0)!=null && !recordList.get(0).get(0).equalsIgnoreCase("") && recordList!=null)
					 if(recordList.get(0).get(0)!= null && !recordList.isEmpty())
					 { CreditCard.mLogger.info(recordList.get(0).get(0));
						 formObject.setNGValue("NotepadDetails_notecode",recordList.get(0).get(0));
						 CreditCard.mLogger.info(recordList.get(0).get(1));
						 formObject.setNGValue("NotepadDetails_workstep",recordList.get(0).get(1),false);
					 }
					//LoadPickList("NotepadDetails_notecode", "select '--Select--' union select convert(varchar, description) from ng_master_notedescription with (nolock)  where Description=q'["+notepad_desc+"]'","NotepadDetails_notecode");
					 sQuery = "select code,workstep from ng_master_notedescription where Code='" +  notepad_desc + "'";
					CreditCard.mLogger.info(sQuery);
					 recordList = formObject.getDataFromDataSource(sQuery);
					if(recordList!=null && recordList.get(0)!=null && recordList.get(0).get(0)!= null && !recordList.get(0).get(0).equalsIgnoreCase("") )
					{
						formObject.setNGValue("NotepadDetails_notecode",recordList.get(0).get(0));
						CreditCard.mLogger.info(recordList.get(0).get(1));
						formObject.setNGValue("NotepadDetails_workstep",recordList.get(0).get(1),false);
					}
				//	formObject.setNGValue("NotepadDetails_notecode",notepad_desc);
				}
				else if ("cmplx_EmploymentDetails_Indus_Macro".equalsIgnoreCase(pEvent.getSource().getName())){
					LoadPickList("cmplx_EmploymentDetails_Indus_Micro", "select '--Select--' union select convert(varchar, micro) from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where Macro='"+formObject.getNGValue("cmplx_EmploymentDetails_Indus_Macro")+"' and IsActive='Y'");
				} 
				else if ("cmplx_FinacleCore_total_deduction_3month".equalsIgnoreCase(pEvent.getSource().getName())){
					float totalCredit = Float.parseFloat(formObject.getNGValue("cmplx_FinacleCore_total_credit_3month"));
					float deduction = Float.parseFloat(formObject.getNGValue("cmplx_FinacleCore_total_deduction_3month"));
					if(totalCredit>deduction){
					float avgCredit = (totalCredit - deduction)/3;

						formObject.setNGValue("cmplx_FinacleCore_avg_credit_3month",convertFloatToString(avgCredit));
				} 
					else{
						alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL023");
						throw new ValidatorException(new FacesMessage(alert_msg));
					}
				} 
				else if ("cmplx_FinacleCore_total_deduction_6month".equalsIgnoreCase(pEvent.getSource().getName())){
					float totalCredit = Float.parseFloat(formObject.getNGValue("cmplx_FinacleCore_total_credit_6month"));
					float deduction = Float.parseFloat(formObject.getNGValue("cmplx_FinacleCore_total_deduction_6month"));
					if(totalCredit>deduction){
					float avgCredit = (totalCredit - deduction)/6;

						formObject.setNGValue("cmplx_FinacleCore_avg_credit_6month",convertFloatToString(avgCredit));
					}
					else{
						alert_msg=NGFUserResourceMgr_CreditCard.getResourceString_ccValidations("VAL023");
						throw new ValidatorException(new FacesMessage(alert_msg));
					}
				}
				else{
					CreditCard.mLogger.info(" ");
				}
				if ("cmplx_DEC_Decision".equalsIgnoreCase(pEvent.getSource().getName())) {
					 if("CAD_Analyst1".equalsIgnoreCase(formObject.getWFActivityName()))	
					 {
						 formObject.setNGValue("CAD_dec", formObject.getNGValue("cmplx_DEC_Decision"));
						CreditCard.mLogger.info( formObject.getNGValue("cmplx_DEC_Decision"));

					 }
					 
					 else{
						 
						formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
						CreditCard.mLogger.info( formObject.getNGValue("cmplx_DEC_Decision"));
					 	  }
				 	}
				 if (pEvent.getSource().getName().equalsIgnoreCase("AppType"))
					{
						String subprod=formObject.getNGValue("SubProd");
						String apptype=formObject.getNGValue("AppType");
						String TypeofProduct=formObject.getNGValue("Product_type");
						CreditCard.mLogger.info( "Value of SubProd is:"+formObject.getNGValue("SubProd"));
						CreditCard.mLogger.info( "Value of AppType is:"+formObject.getNGValue("AppType"));
						CreditCard.mLogger.info( "Value of AppType is:"+formObject.getNGValue("Product_type"));


						if ((TypeofProduct.equalsIgnoreCase("Conventional"))&& subprod.equalsIgnoreCase("EXP")&&(apptype.equalsIgnoreCase("TKOE"))){


							CreditCard.mLogger.info( "1Value of AppType is:"+formObject.getNGValue("AppType"));


							formObject.clear("Scheme");
							formObject.setNGValue("Scheme","--Select--");
							LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Take Over'  and TypeofProduct='Conventional' order by SCHEMEID");




						}
						if ((TypeofProduct.equalsIgnoreCase("Conventional"))&& subprod.equalsIgnoreCase("EXP")&&(apptype.equalsIgnoreCase("NEWE"))){
							CreditCard.mLogger.info( "2Value of AppType is:"+formObject.getNGValue("AppType"));




							formObject.clear("Scheme");
							formObject.setNGValue("Scheme","--Select--");
							LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'New' and TypeofProduct='Conventional' order by SCHEMEID");




						}
						if ((TypeofProduct.equalsIgnoreCase("Conventional")&&subprod.equalsIgnoreCase("EXP"))&&(apptype.equalsIgnoreCase("TOPE"))){
							CreditCard.mLogger.info( "3Value of AppType is:"+formObject.getNGValue("AppType"));


							formObject.clear("Scheme");
							formObject.setNGValue("Scheme","--Select--");
							LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Top up' and TypeofProduct='Conventional' order by SCHEMEID");


						}
						if ((TypeofProduct.equalsIgnoreCase("Conventional")&&subprod.equalsIgnoreCase("EXP"))&&(apptype.equalsIgnoreCase("RESCE"))){
							CreditCard.mLogger.info( "4Value of AppType is:"+formObject.getNGValue("AppType"));


							formObject.clear("Scheme");
							formObject.setNGValue("Scheme","--Select--");
							LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Reschedulment' and TypeofProduct='Conventional' order by SCHEMEID");


						}
						if ((TypeofProduct.equalsIgnoreCase("Conventional")&& subprod.equalsIgnoreCase("NAT"))&&(apptype.equalsIgnoreCase("TOPN"))){
							CreditCard.mLogger.info( "5Value of AppType is:"+formObject.getNGValue("AppType"));
							formObject.clear("Scheme");
							formObject.setNGValue("Scheme","--Select--");
							LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Top up' and TypeofProduct='Conventional' order by SCHEMEID");


						}
						if ((TypeofProduct.equalsIgnoreCase("Conventional")&&subprod.equalsIgnoreCase("NAT"))&&(apptype.equalsIgnoreCase("TKON"))){
							CreditCard.mLogger.info( "6Value of AppType is:"+formObject.getNGValue("AppType"));
							formObject.clear("Scheme");
							formObject.setNGValue("Scheme","--Select--");
							LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Take Over' and TypeofProduct='Conventional' order by SCHEMEID");


						}
						if ((TypeofProduct.equalsIgnoreCase("Conventional")&&subprod.equalsIgnoreCase("NAT"))&&(apptype.equalsIgnoreCase("NEWN"))){
							CreditCard.mLogger.info( "7Value of AppType is:"+formObject.getNGValue("AppType"));
							formObject.clear("Scheme");
							formObject.setNGValue("Scheme","--Select--");
							LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'New' and TypeofProduct='Conventional' order by SCHEMEID");


						}
						if ((TypeofProduct.equalsIgnoreCase("Conventional")&&subprod.equalsIgnoreCase("NAT"))&&(apptype.equalsIgnoreCase("RESCN"))){
							CreditCard.mLogger.info( "8Value of AppType is:"+formObject.getNGValue("AppType"));
							formObject.clear("Scheme");
							formObject.setNGValue("Scheme","--Select--");
							LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Reschedulment' and TypeofProduct='Conventional' order by SCHEMEID");


						}
						if ((TypeofProduct.equalsIgnoreCase("Islamic"))&& subprod.equalsIgnoreCase("EXP")&&(apptype.equalsIgnoreCase("TKOE"))){


							CreditCard.mLogger.info( "1Value of AppType is:"+formObject.getNGValue("AppType"));


							formObject.clear("Scheme");
							formObject.setNGValue("Scheme","--Select--");
							LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Take Over'  and TypeofProduct='Islamic' order by SCHEMEID");				
						}

						if ((TypeofProduct.equalsIgnoreCase("Islamic"))&& subprod.equalsIgnoreCase("EXP")&&(apptype.equalsIgnoreCase("NEWE"))){
							CreditCard.mLogger.info( "2Value of AppType is:"+formObject.getNGValue("AppType"));
							formObject.clear("Scheme");
							formObject.setNGValue("Scheme","--Select--");
							LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'New' and TypeofProduct='Islamic' order by SCHEMEID");				
						}

						if ((TypeofProduct.equalsIgnoreCase("Islamic")&&subprod.equalsIgnoreCase("EXP"))&&(apptype.equalsIgnoreCase("TOPE"))){
							CreditCard.mLogger.info( "3Value of AppType is:"+formObject.getNGValue("AppType"));


							formObject.clear("Scheme");
							formObject.setNGValue("Scheme","--Select--");
							LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Top up' and TypeofProduct='Islamic' order by SCHEMEID");


						}
						if ((TypeofProduct.equalsIgnoreCase("Islamic")&&subprod.equalsIgnoreCase("EXP"))&&(apptype.equalsIgnoreCase("RESCE"))){
							CreditCard.mLogger.info( "4Value of AppType is:"+formObject.getNGValue("AppType"));


							formObject.clear("Scheme");
							formObject.setNGValue("Scheme","--Select--");
							LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Reschedulment' and TypeofProduct='Islamic' order by SCHEMEID");


						}
						if ((TypeofProduct.equalsIgnoreCase("Islamic")&& subprod.equalsIgnoreCase("NAT"))&&(apptype.equalsIgnoreCase("TOPN"))){
							CreditCard.mLogger.info( "5Value of AppType is:"+formObject.getNGValue("AppType"));
							formObject.clear("Scheme");
							formObject.setNGValue("Scheme","--Select--");
							LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Top up' and TypeofProduct='Islamic' order by SCHEMEID");


						}
						if ((TypeofProduct.equalsIgnoreCase("Islamic")&&subprod.equalsIgnoreCase("NAT"))&&(apptype.equalsIgnoreCase("TKON"))){
							CreditCard.mLogger.info( "6Value of AppType is:"+formObject.getNGValue("AppType"));
							formObject.clear("Scheme");
							formObject.setNGValue("Scheme","--Select--");
							LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Take Over' and TypeofProduct='Islamic' order by SCHEMEID");


						}
						if ((TypeofProduct.equalsIgnoreCase("Islamic")&&subprod.equalsIgnoreCase("NAT"))&&(apptype.equalsIgnoreCase("NEWN"))){
							CreditCard.mLogger.info( "7Value of AppType is:"+formObject.getNGValue("AppType"));
							formObject.clear("Scheme");
							formObject.setNGValue("Scheme","--Select--");
							LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'New' and TypeofProduct='Islamic' order by SCHEMEID");


						}
						if ((TypeofProduct.equalsIgnoreCase("Islamic")&&subprod.equalsIgnoreCase("NAT"))&&(apptype.equalsIgnoreCase("RESCN"))){
							CreditCard.mLogger.info( "8Value of AppType is:"+formObject.getNGValue("AppType"));
							formObject.clear("Scheme");
							formObject.setNGValue("Scheme","--Select--");
							LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Reschedulment' and TypeofProduct='Islamic' order by SCHEMEID");

						}
					}
				 if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_DEC_FeedbackStatus") && formObject.getWFActivityName().equalsIgnoreCase("FCU")){
						
					 CreditCard.mLogger.info("inside feedback status");
					 LoadpicklistFCU();
					 CreditCard.mLogger.info("inside feedback status after loadpicklist");
					 
				 }

	}
	
	
}
