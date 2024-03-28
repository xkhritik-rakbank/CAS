
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

import com.newgen.custom.Common_Utils;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.excp.CustomExceptionHandler;
import com.newgen.omniforms.component.IRepeater;



public class CC_CommonCode extends CC_Common {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha             
	Description                         : to expand/fetch fragment     

	 ***********************************************************************************  */
	@SuppressWarnings("finally")
	public void FrameExpandEvent(ComponentEvent pEvent){
		HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String activityName = formObject.getWFActivityName();

			CreditCard.mLogger.info("Inside CC_CommonCode-->FrameExpandEvent()");
			if ("CustomerDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

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
			if ("ProductContainer".equalsIgnoreCase(pEvent.getSource().getName())) {
				hm.put("ProductContainer","Clicked");
				formObject.fetchFragment("ProductContainer", "Product", "q_product");
				// ++ below code already present - 06-10-2017
				hide_sheet_employee();
				hide_sheet_company();
				LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct with (nolock)");
				LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_ApplicationType with (nolock) order by code");
				LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{
					hm.clear();}
			}

			if ("EmploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

				hm.put("EmploymentDetails","Clicked");
				formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmpDetails");
				// ++ below code already present - 06-10-2017
				//COndition added by aman to not call these function at CSM
				CreditCard.mLogger.info("end of fetchfrag of employment now on frame exp"+formObject.getNGValue("cmplx_EmploymentDetails_Designation"));
				
				if(!(NGFUserResourceMgr_CreditCard.getGlobalVar("CC_CSM").equalsIgnoreCase(formObject.getWFActivityName()))){	
				employment_fields_hide();
				employment_fields_IM();
				}
				//loadPicklist_Employment();
				CreditCard.mLogger.info("before again Load Pick"+formObject.getNGValue("cmplx_EmploymentDetails_Designation"));
				
				//loadPicklist4(); //commented by aman because causing issue in load Picklist (already called in all the file)
				CreditCard.mLogger.info("after again Load Pick"+formObject.getNGValue("cmplx_EmploymentDetails_Designation"));
				
				
				if(!"CAC".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode"))){
					formObject.setVisible("EMploymentDetails_Label7", false);
					formObject.setVisible("cmplx_EmploymentDetails_OtherBankCAC", false);
				}
						
				else{	
					if(!"S".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg")))
					{
						formObject.setVisible("cmplx_EmploymentDetails_IndusSeg",false);
						formObject.setVisible("EMploymentDetails_Label59",false);
					}
					formObject.setVisible("EMploymentDetails_Label7", true);
					formObject.setVisible("cmplx_EmploymentDetails_OtherBankCAC", true);
				}
				if(!	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_SAL").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2))){
					formObject.setVisible("EMploymentDetails_Label59", false);
					formObject.setVisible("cmplx_EmploymentDetails_IndusSeg", false);
				}
				else{
					formObject.setVisible("EMploymentDetails_Label59", true);
					formObject.setVisible("cmplx_EmploymentDetails_IndusSeg", true);
				}
				/*if(	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_CreditCard").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1))){
					formObject.setVisible("EMploymentDetails_Label71", false);
					formObject.setVisible("cmplx_EmploymentDetails_EmpContractType", false);
				}
				else{*/
					formObject.setVisible("EMploymentDetails_Label71", true);
					formObject.setVisible("cmplx_EmploymentDetails_EmpContractType", true);
				//}
				CreditCard.mLogger.info("before again Load lockALOCfields"+formObject.getNGValue("cmplx_EmploymentDetails_Designation"));
				
				lockALOCfields();
				CreditCard.mLogger.info("after again Load lockALOCfields"+formObject.getNGValue("cmplx_EmploymentDetails_Designation"));
				
				if(!(NGFUserResourceMgr_CreditCard.getGlobalVar("CC_CSM").equalsIgnoreCase(formObject.getWFActivityName())))
				{
					CreditCard.mLogger.info("inside employer code CSM condition");
					if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_Others")) && formObject.isLocked("cmplx_EmploymentDetails_ApplicationCateg")==false)
					{
						CreditCard.mLogger.info("inside employer code condition");
						CreditCard.mLogger.info("inside islocked value"+formObject.isLocked("EMploymentDetails_Frame1"));
						formObject.setLocked("cmplx_EmploymentDetails_EMpCode", false);
						
					}
					formObject.setVisible("cmplx_EmploymentDetails_Others", false);
				}
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{
					hm.clear();}
			}	

			if ("IncomeDEtails".equalsIgnoreCase(pEvent.getSource().getName())) 
			{
				hm.put("Incomedetails","Clicked");
				formObject.fetchFragment("IncomeDEtails", "IncomeDetails", "q_IncomeDetails");
				expandIncome();
			
			}					

			if ("Internal_External_Liability".equalsIgnoreCase(pEvent.getSource().getName())) {
				hm.put("Internal_External_Liability","Clicked");
				formObject.fetchFragment("Internal_External_Liability", "Liability_New", "q_Liabilities");
				String activity = formObject.getWFActivityName();
				String empType = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6);
				/*if(empType.equalsIgnoreCase("Salaried")){
				formObject.setEnabled("ExtLiability_Frame1", false);
			}*/
				//Ref. 1004
				if("Salaried".equalsIgnoreCase(empType) || "Salaried Pensioner".equalsIgnoreCase(empType)){
					CreditCard.mLogger.info( "Inside Self Employed case for liabilities");
					formObject.setVisible("Liability_New_Label6",false);
					formObject.setVisible("cmplx_Liability_New_NoofotherbankAuth",false);
					formObject.setVisible("Liability_New_Label8",false);
					formObject.setVisible("cmplx_Liability_New_NoofotherbankCompany",false);
					formObject.setVisible("Liability_New_Label7",false);
					formObject.setVisible("cmplx_Liability_New_TotcleanFundedAmt",false);
					formObject.setVisible("cmplx_Liability_New_AECBCompanyconsentAvail", false);


				}

				//Deepak 09102017- JIRA 2864 to load company details in case of Self Employed while loading employment details
				else if("Self Employed".equalsIgnoreCase(empType)){
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
					formObject.setVisible("cmplx_Liability_New_AECBCompanyconsentAvail", true);
					
					
				}
				else{
					CreditCard.mLogger.info( "Inside Salaried case for liabilities");
					formObject.setVisible("Liability_New_Label6",true);
					formObject.setVisible("cmplx_Liability_New_NoofotherbankAuth",true);
					formObject.setVisible("Liability_New_Label8",true);
					formObject.setVisible("cmplx_Liability_New_NoofotherbankCompany",true);
					formObject.setVisible("Liability_New_Label7",true);
					formObject.setVisible("cmplx_Liability_New_TotcleanFundedAmt",true);
					/*formObject.setVisible("ExtLiability_Label9",false);
					formObject.setVisible("cmplx_Liability_New_DBR",false);
					formObject.setVisible("ExtLiability_Label25",false);
					formObject.setVisible("cmplx_Liability_New_TAI",false);
					formObject.setVisible("ExtLiability_Label14",false);
					formObject.setVisible("cmplx_Liability_New_DBRNet",false);*/
				}

				formObject.setLocked("cmplx_Liability_New_AggrExposure",true);
				formObject.setLocked("cmplx_Liability_New_DBR",true);
				formObject.setLocked("cmplx_Liability_New_TAI",true);
				formObject.setLocked("cmplx_Liability_New_DBRNet",true);
				formObject.setLocked("Liability_New_EMI",true);//added by akshay on 28/12/17

				//Ref. 1004 end.

				LoadPickList("Liability_New_worstStatusInLast24", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from ng_master_Aecb_Codes with (nolock) order by code");
				if(NGFUserResourceMgr_CreditCard.getGlobalVar("CC_CSM").equalsIgnoreCase(activity)){
					//++ Below Code already exists for : "17-CSM-Liability addition-" Delinquent and DPD fields which are present in the grid are missing in the liability addition fragment" : Reported By Shashank on Oct 05, 2017++
					//hideLiabilityAddFields(formObject);
					//++ Above Code already exists for : "17-CSM-Liability addition-" Delinquent and DPD fields which are present in the grid are missing in the liability addition fragment" : Reported By Shashank on Oct 05, 2017++
				}
				if("DDVT_Maker".equalsIgnoreCase(activityName)){
					formObject.setLocked("ExtLiability_Frame1", true);
				}
				//change by saurabh on 20th Dec
				if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("IM")){
					formObject.setEnabled("ExtLiability_CACIndicator",false);
				}
				
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{
					hm.clear();}
			}	


			/*if ("MiscFields".equalsIgnoreCase(pEvent.getSource().getName())) {
				hm.put("MiscFields","Clicked");
				formObject.fetchFragment("MiscFields", "MiscellaneousFields", "q_MiscFields");

				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{
					hm.clear();}

			}*/
			//added by yash for CC FSD
			if ("EligibilityAndProductInformation".equalsIgnoreCase(pEvent.getSource().getName())) {
				hm.put("EligibilityAndProductInformation","Clicked");
				// ++ below code already present - 06-10-2017 name not change from q_EligProd to q_EligAndProductInfo as in onsite thw Queue variable name is q_EligProd 
				formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligProd");
				expandEligibility();
				
			}	

			if ("Address_Details_container".equalsIgnoreCase(pEvent.getSource().getName())) {
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


			if ("Alt_Contact_container".equalsIgnoreCase(pEvent.getSource().getName())) {
				hm.put("Alt_Contact_container","Clicked");
				formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
				//p2-49,Retain account if loan rejected tickbox should not apply for cards 
				//++ Below Code added By on Oct 6, 2017  to fix : "38-Retain account if loan rejected should not be there" : Reported By Shashank on Oct 05, 2017++
				CreditCard.mLogger.info("before invisibility");
				if("DDVT_maker".equalsIgnoreCase(activityName)){

					formObject.setVisible("AlternateContactDetails_RetainaccifLoanreq_cont", false);
				}
				else{
					formObject.setVisible("AlternateContactDetails_RetainaccifLoanreq_cont", true);
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

			if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) 
			{
				hm.put("FATCA","Clicked");
				formObject.fetchFragment("FATCA", "FATCA", "q_FATCA");
				//loadPicklist_Fatca();
				if(!"CAD_Analyst1".equalsIgnoreCase(activityName)){
					String usRelation = formObject.getNGValue("cmplx_FATCA_USRelation");
					if("O".equalsIgnoreCase(usRelation)){
						formObject.setLocked("FATCA_Frame6", true);
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

			if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) 
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

			if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) 
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

			if ("Dispatch_Details".equalsIgnoreCase(pEvent.getSource().getName())) 
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

			if ("Self_Employed".equalsIgnoreCase(pEvent.getSource().getName())) {
				hm.put("Self_Employed","Clicked");
				formObject.fetchFragment("Self_Employed", "SelfEmployed", "q_SelfEmployed");
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{
					hm.clear();}
			}

			
			if("Loan_Disbursal".equalsIgnoreCase(pEvent.getSource().getName())){
				hm.put("Loan_Disbursal","Clicked");
				formObject.fetchFragment("Loan_Disbursal", "Loan_Disbursal", "q_LoanDisb");
				CreditCard.mLogger.info("after loan disbursal fragment");
			}
			//added on 20th of dec 2017 as it is not getting fetched
			if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName()))
			{
				hm.put("DecisionHistory","Clicked");
				formObject.fetchFragment("DecisionHistory", "DecisionHistory", "q_DecisionHistory");
				expandDecision();
				formObject.setLocked("cmplx_DEC_ReferTo", true);
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{
					
					hm.clear();
					}
			}	
			
			
			
			if ("Frame4".equalsIgnoreCase(pEvent.getSource().getName()))
			{
				hm.put("IncomingDocuments","Clicked");
				CreditCard.mLogger.info("IncomingDocuments");
				//frame,fragment,q_variable
				formObject.fetchFragment("Frame4", "IncomingDocument", "q_IncDoc");
				CreditCard.mLogger.info("fetchIncomingDocRepeater");
				fetchIncomingDocRepeater();

			/*	String activName = formObject.getWFActivityName();
				if("DDVT_Checker".equalsIgnoreCase(activName)){
					if(	"".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP")) && 	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_false").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")))
						formObject.setVisible("IncomingDoc_UploadSig",false);
					else if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP")) || 	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
						formObject.setVisible("IncomingDoc_ViewSIgnature",false);
						formObject.setVisible("IncomingDoc_UploadSig",true);
						//formObject.setEnabled("IncomingDoc_UploadSig",false);
					}
				}
				else if("DDVT_Maker".equalsIgnoreCase(activName)){
					if(	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_false").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP")) && 	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_false").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")))
						formObject.setEnabled("IncomingDoc_ViewSIgnature",true);
					else if(	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP")) || 	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
						formObject.setEnabled("IncomingDoc_ViewSIgnature",false);
						//formObject.setVisible("IncomingDoc_UploadSig",true);
						//formObject.setEnabled("IncomingDoc_UploadSig",false);
					}
				}---shifted to workstep files by akshay on 17/1/18*/

				CreditCard.mLogger.info("formObject.fetchFragment1");
				try{

					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{
					hm.clear();
				}
			}
			if ("Reference_Details".equalsIgnoreCase(pEvent.getSource().getName())) {
				hm.put("Reference_Details","Clicked");
				formObject.fetchFragment("Reference_Details", "Reference_Details", "q_ReferenceDetails");
				LoadPickList("Reference_Details_ReferenceRelatiomship", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_Relationship with (nolock) order by code");
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
			if ("Card_Details".equalsIgnoreCase(pEvent.getSource().getName())) {
				fetch_CardDetails_frag(formObject);
				if(!"CSM".equalsIgnoreCase(activityName))
				{
					//below code added by nikhil.
					int framestate=formObject.getNGFrameState("IncomeDetails");
					if(framestate!=0)
					{
						formObject.fetchFragment("IncomeDEtails", "IncomeDetails", "q_IncomeDetails");
						expandIncome();
					}
					formObject.setVisible("cmplx_CardDetails_Statement_cycle", true);
					if(NGFUserResourceMgr_CreditCard.getGlobalVar("CC_Salaried").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6))){
						formObject.setNGValue("cmplx_CardDetails_Statement_cycle", formObject.getNGValue("cmplx_IncomeDetails_StatementCycle"));
					}
					else{
						formObject.setNGValue("cmplx_CardDetails_Statement_cycle", formObject.getNGValue("cmplx_IncomeDetails_StatementCycle2"));
					}
				}
				if("Yes".equalsIgnoreCase(formObject.getNGValue("cmplx_CardDetails_Supplementary_Card"))){
					formObject.setVisible("Supplementary_Cont",true);
				}
				
			}
			//Tanshu Aggarwal 17/08/2017 Card Details changes
			if ("Supplementary_Cont".equalsIgnoreCase(pEvent.getSource().getName())) {
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

			if ("CompDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				hm.put("CompDetails","Clicked");
				//formObject.fetchFragment("CompDetails", "CompanyDetails", "q_CompanyDetails");
				//CreditCard.mLogger.info( "Fter Load Pick Company: 4");
				fetch_Company_frag(formObject);//added by akshay on 29/12/17
				/*if(activityName.equalsIgnoreCase("DDVT_Maker")){
					formObject.setLocked("MarketingCode",false);
					formObject.setLocked("ClassificationCode",false);
					formObject.setLocked("PromotionCode",false);
				}*/
				if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
					formObject.setVisible("CompanyDetails_Label15", true);
					formObject.setVisible("NepType", true);
					formObject.setLocked("NepType", false);
					
				}
				else{
					formObject.setVisible("CompanyDetails_Label15", false);
					formObject.setVisible("NepType", false);
					formObject.setLocked("NepType", true);
					
				}

				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{
					hm.clear();}
			}


			/*if ("Auth_Sign_Details".equalsIgnoreCase(pEvent.getSource().getName())) {
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
			if ("Partner_Details".equalsIgnoreCase(pEvent.getSource().getName())) {
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
			}*/
			if ("Details".equalsIgnoreCase(pEvent.getSource().getName())) {
				hm.put("Details","Clicked");
				formObject.fetchFragment("Details", "CC_Loan", "q_CC_Loan");
				CreditCard.mLogger.info( "Activity name is:" + formObject.getNGValue("cmplx_CC_Loan_DDSMode"));
				   
				//loadPicklist_ServiceRequest();
				//++ Below Code added By Nikhil on Oct 6, 2017  to fix : "12-Percnetage to be enabled when DDS mode is Percentage" : Reported By Shashank on Oct 05, 2017++
				//below code also fix point "13-Flat amount to be enabled and mandatory when DDS mode is flat amount"
				//formObject.setNGValue("cmplx_CC_Loan_DDSMode", "--Select--");
				//-- Above Code added By Nikhil on Oct 6, 2017  to fix : "12-Percnetage to be enabled when DDS mode is Percentage" : Reported By Shashank on Oct 05, 2017--
				CreditCard.mLogger.info( "Activity name is:" + formObject.getNGValue("cmplx_CC_Loan_DDSMode"));
				   
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{
					hm.clear();}
			}
			if ("Part_Match".equalsIgnoreCase(pEvent.getSource().getName())) {
				hm.put("Part_Match","Clicked");
				formObject.fetchFragment("Part_Match", "PartMatch", "q_PartMatch");
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{
					hm.clear();}
			}
			if ("Finacle_CRM_Incidents".equalsIgnoreCase(pEvent.getSource().getName())) {
				hm.put("Finacle_CRM_Incidents","Clicked");
				formObject.fetchFragment("Finacle_CRM_Incidents", "FinacleCRMIncident", "q_FinacleCRMIncident");

				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{
					hm.clear();}
			}
			if ("Finacle_CRM_CustomerInformation".equalsIgnoreCase(pEvent.getSource().getName())) {
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
			if ("External_Blacklist".equalsIgnoreCase(pEvent.getSource().getName())) {
				/*hm.put("External_Blacklist","Clicked");
				popupFlag="N";*/
				formObject.fetchFragment("External_Blacklist", "ExternalBlackList", "q_ExternalBlackList");
				if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6).equalsIgnoreCase("Salaried")){

					formObject.setLocked("ExternalBlackList_Frame1", true);

				}
				loadInExtBlacklistGrid(formObject);//added by akshay on 18/12/17

				/*try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{
				hm.clear();}*/
			}
			if ("Finacle_Core".equalsIgnoreCase(pEvent.getSource().getName())) {
				/*hm.put("Finacle_Core","Clicked");
				popupFlag="N";*/
				formObject.fetchFragment("Finacle_Core", "FinacleCore", "q_FinacleCore");
				//++Below code added by nikhil 8/11/17 as per CC issues sheet
				if("salaried".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6)))
				{
					formObject.setVisible("FinacleCore_Frame2", false);
					formObject.setVisible("FinacleCore_Frame3", false);
					formObject.setVisible("FinacleCore_avgbal", false);
					formObject.setVisible("FinacleCore_Frame8", false);
				}
				//--Above code added by nikhil 8/11/17 as per CC issues sheet
				fetchfinacleAvgRepeater();
				fetchfinacleTORepeater();
				fetchfinacleDocRepeater();
				fetchTransumDet();
				//if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6).equalsIgnoreCase("Salaried")){
				if("DDVT_Maker".equalsIgnoreCase(activityName)){
					formObject.setLocked("FinacleCore_Frame1", true);
				}
				try{
					//below query modified by nikhil 11/12/17
					String query="select AcctType,'',AcctId,AcctNm,AccountOpenDate,AcctStat,'',AvailableBalance,EffectiveAvailableBalance,CreditGrade,'',child_wi, AcctSegment,AcctSubSegment from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
					List<List<String>> list_acc=formObject.getNGDataFromDataCache(query);
					for(List<String> mylist : list_acc)
					{
						CreditCard.mLogger.info( "Data to be added in Grid account Grid: "+mylist.toString());
						formObject.addItemFromList("cmplx_FinacleCore_FinaclecoreGrid", mylist);
					}

					query="select AcctId,LienId,LienAmount,LienRemarks,LienReasonCode,LienStartDate,LienExpDate from ng_rlos_FinancialSummary_LienDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
					//changed ended
					List<List<String>> list_lien=formObject.getNGDataFromDataCache(query);
					for(List<String> mylist : list_lien)
					{
						CreditCard.mLogger.info( "Data to be added in Grid: "+mylist.toString());
						formObject.addItemFromList("cmplx_FinacleCore_liendet_grid", mylist);
					}
					//changed here in this query
					query="select AcctId,SiAmount,SiRemarks,'',NextExecDate from ng_rlos_FinancialSummary_SiDtls with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
					List<List<String>> list_SIDet=formObject.getNGDataFromDataCache(query);
					//changed ended

					for(List<String> mylist : list_SIDet)
					{
						CreditCard.mLogger.info( "Data to be added in Grid: "+mylist.toString());

						formObject.addItemFromList("cmplx_FinacleCore_sidet_grid", mylist);
					}
					//query changed 31/1/18
					query="select (select distinct Account_Type from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where AcctId = AcctId and CifId=CifId and Child_Wi='"+formObject.getWFWorkitemName()+"' ),AcctId,returntype,returnAmount,returnNumber,returnDate,retReasonCode,instrumentdate,returnType,'','','','','','' from ng_rlos_FinancialSummary_ReturnsDtls with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
					List<List<String>> list_DDSDet=formObject.getNGDataFromDataCache(query);
					CreditCard.mLogger.info( "query to be added in list_DDSDet Grid: "+query);

					//changed ended
					formObject.clear("cmplx_FinacleCore_DDSgrid");
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
			if ("MOL".equalsIgnoreCase(pEvent.getSource().getName())) {
				/*hm.put("MOL","Clicked");
				popupFlag="N";*/
				formObject.fetchFragment("MOL", "MOL1", "q_Mol1");
				// added by abhishek as per CC FSD
				if("self Employed".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6))){
					formObject.setLocked("MOL1_Frame1", true);
				}
				
					

				/*try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{
				hm.clear();}*/
			}
			if ("World_Check".equalsIgnoreCase(pEvent.getSource().getName())) {
				/*hm.put("World_Check","Clicked");
				popupFlag="N";*/
				formObject.fetchFragment("World_Check", "WorldCheck1", "q_WorldCheck");

				//++ Below Code added By Tanshu on Oct 6, 2017  to fix : "18,19,20-DDVT-World check-Birth country incorrect masters" : Reported By Shashank on Oct 05, 2017++
				LoadPickList("WorldCheck1_ResidentCountry","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_Country with (nolock) where isActive='Y' order by code");
				LoadPickList("WorldCheck1_BirthCountry","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_Country with (nolock) where isActive='Y' order by code");
				LoadPickList("WorldCheck1_Gender","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_gender with (nolock) where isActive='Y' order by code");
				//-- Above Code added By Tanshu on Oct 6, 2017  to fix : "18,19,20-DDVT-World check-Birth country incorrect masters" : Reported By Shashank on Oct 05, 2017--
				/*try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{
				hm.clear();}*/
				//below code added by nikhil 20/12/17
				formObject.setVisible("WorldCheck1_dec",false);
			}
			if ("Reject_Enquiry".equalsIgnoreCase(pEvent.getSource().getName())) {
				/*hm.put("Reject_Enquiry","Clicked");
				popupFlag="N";*/
				formObject.fetchFragment("Reject_Enquiry", "RejectEnq", "q_RejectEnq");
				//if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6).equalsIgnoreCase("Salaried")){
				if("DDVT_Maker".equalsIgnoreCase(activityName)){
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
			if ("Salary_Enquiry".equalsIgnoreCase(pEvent.getSource().getName())) {
				/*hm.put("Salary_Enquiry","Clicked");
				popupFlag="N";*/
				formObject.fetchFragment("Salary_Enquiry", "SalaryEnq", "q_SalaryEnq");
				//if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6).equalsIgnoreCase("Salaried")){
				if("DDVT_Maker".equalsIgnoreCase(activityName)){
					formObject.setLocked("SalaryEnq_Frame1", true);
				}

				String query="select SalCreditDate,SalCreditMonth,AcctId,SalAmount,'' from ng_rlos_FinancialSummary_SalTxnDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
				CreditCard.mLogger.info( "Data to be added in account Grid account query: "+query);
				List<List<String>> list_acc=formObject.getNGDataFromDataCache(query);
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
			if ("PostDisbursal".equalsIgnoreCase(pEvent.getSource().getName())) {
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
			if ("CAD".equalsIgnoreCase(pEvent.getSource().getName())) {
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
			if ("CAD_Decision".equalsIgnoreCase(pEvent.getSource().getName())) {
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

			if ("Notepad_Values".equalsIgnoreCase(pEvent.getSource().getName())) {
				fetch_NotepadDetails();//modified by akshay
			}
			//ended by yash on 23/8/2017

			if ("Smart_Check1".equalsIgnoreCase(pEvent.getSource().getName())) {
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
			if ("CreditCard_Enq".equalsIgnoreCase(pEvent.getSource().getName())) {
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

			if ("Case_hist".equalsIgnoreCase(pEvent.getSource().getName())) {
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

			if ("LOS".equalsIgnoreCase(pEvent.getSource().getName())) {
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

			if ("Customer_Details_Verification".equalsIgnoreCase(pEvent.getSource().getName())) {
				hm.put("Customer_Details_Verification","Clicked");
				formObject.fetchFragment("Customer_Details_Verification", "CustDetailVerification", "q_CustDetailVeri");
				//loadPicklist_custverification();
				
				openDemographicFrags(formObject);
				autopopulateValues(formObject);
				//formObject.setLocked("CustDetailVerification_Frame1", true);
				//loadPicklist3();
				//loadInDecGrid();
				List<String> LoadPicklist_Verification= Arrays.asList("cmplx_CustDetailVerification_mobno1_ver","cmplx_CustDetailVerification_mobno2_ver","cmplx_CustDetailVerification_dob_verification","cmplx_CustDetailVerification_POBoxno_ver","cmplx_CustDetailVerification_emirates_ver","cmplx_CustDetailVerification_persorcompPOBox_ver","cmplx_CustDetailVerification_resno_ver","cmplx_CustDetailVerification_offtelno_ver","cmplx_CustDetailVerification_hcountrytelno_ver","cmplx_CustDetailVerification_hcontryaddr_ver","cmplx_CustDetailVerification_email1_ver","cmplx_CustDetailVerification_email2_ver");

				LoadPicklistVerification(LoadPicklist_Verification);
				LoadPickList("cmplx_CustDetailVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");

				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{
					hm.clear();}
			}

			if ("Business_Verification".equalsIgnoreCase(pEvent.getSource().getName())) {
				hm.put("Business_Verification","Clicked");
				formObject.fetchFragment("Business_Verification", "BussinessVerification", "q_BussVerification");
				LoadPickList("cmplx_BussVerification_contctTelChk", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Other' ");
				LoadPickList("cmplx_BussVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");//Arun 14/12/17 to load the decision correctly
				//formObject.setLocked("BussinessVerification_Frame1", true);
				//loadPicklist3();
				//loadInDecGrid();
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{
					hm.clear();}
			}

			if ("HomeCountry_Verification".equalsIgnoreCase(pEvent.getSource().getName())) {
				hm.put("HomeCountry_Verification","Clicked");
				formObject.fetchFragment("HomeCountry_Verification", "HomeCountryVerification", "q_HCountryVerification");
				//below code commented by nikhil
				//LoadPickList("cmplx_HCountryVerification_Hcountrytelverified", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Other' ");
				//List<String> LoadPicklist_Verification= Arrays.asList("cmplx_HCountryVerification_Hcountrytelverified");
				//LoadPicklistVerification(LoadPicklist_Verification);
				//formObject.setLocked("HomeCountryVerification_Frame1", true);
				//loadPicklist3();
				//loadInDecGrid();
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{
					hm.clear();}
			}

			if ("ResidenceVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
				hm.put("ResidenceVerification","Clicked");
				formObject.fetchFragment("ResidenceVerification", "ResidenceVerification", "q_ResiVerification");
				openDemographicFrags(formObject);
				LoadPickList("cmplx_ResiVerification_Telnoverified", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPVveri with (nolock)");
				formObject.setNGValue("cmplx_ResiVerification_cntcttelno",formObject.getNGValue("AlternateContactDetails_ResidenceNo"));
				formObject.setLocked("cmplx_ResiVerification_cntcttelno", true);
				LoadPickList("cmplx_ResiVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");
				LoadPickList("cmplx_ResiVerification_Telnoverified", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_CPVVeri with (nolock) order by code");
				
				//formObject.setLocked("ResidenceVerification_Frame1", true);
				//loadPicklist3();
				//loadInDecGrid();
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{
					hm.clear();}
			}
			if ("Guarantor_Verification".equalsIgnoreCase(pEvent.getSource().getName())) {
				hm.put("Guarantor_Verification","Clicked");
				formObject.fetchFragment("Guarantor_Verification", "GuarantorVerification", "");
				//LoadPickList("cmplx_GuarantorVerification_verdoneonmob", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Other' ");
				LoadPickList("cmplx_GuarantorVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");
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

			if ("Reference_Detail_Verification".equalsIgnoreCase(pEvent.getSource().getName())) {
				hm.put("Reference_Detail_Verification","Clicked");
				formObject.fetchFragment("Reference_Detail_Verification", "ReferenceDetailVerification", "q_RefDetailVerification");
				LoadPickList("cmplx_RefDetVerification_ref1cntctd", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Other' ");
				LoadPickList("cmplx_RefDetVerification_ref2cntctd", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Other' ");
				LoadPickList("cmplx_RefDetVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");
				//formObject.setLocked("ReferenceDetailVerification_Frame1", true);
				//loadPicklist3();
				//loadInDecGrid();
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{
					hm.clear();}
			}

			if ("Office_Mob_Verification".equalsIgnoreCase(pEvent.getSource().getName())) {
				hm.put("Office_Mob_Verification","Clicked");
				formObject.fetchFragment("Office_Mob_Verification", "OfficeandMobileVerification", "q_OffVerification");
				//loadPicklist_officeverification();
				enable_officeVerification();
				//below code modified
				List<String> LoadPicklist_Verification= Arrays.asList("cmplx_OffVerification_fxdsal_ver","cmplx_OffVerification_accpvded_ver","cmplx_OffVerification_desig_ver","cmplx_OffVerification_doj_ver","cmplx_OffVerification_cnfminjob_ver");
				LoadPicklistVerification(LoadPicklist_Verification);
				LoadPickList("cmplx_OffVerification_colleaguenoverified", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_CPVVeri with (nolock) where Sno != 3 order by code"); //Arun modified on 14/12/17
				LoadPickList("cmplx_OffVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");
				//below code by saurabh on 28th nov 2017.
				LoadPickList("cmplx_OffVerification_offtelnovalidtdfrom", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_offNoValidatedFrom with (nolock)");
				LoadPickList("cmplx_OffVerification_hrdnocntctd", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_HRDContacted with (nolock)");
				//formObject.setLocked("OfficeandMobileVerification_Frame1", true); //Arun (26/11/17) to lock only certain fields not the whole frame


				//++ below code added by abhishek as per CC FSD 2.7.3
				if("Smart_CPV".equalsIgnoreCase(activityName)){
					//below code commneted for calling procedure



					String EnableFlagValue = formObject.getNGValue("cmplx_OffVerification_EnableFlag");
					String sQuery =" SELECT ProcessInstanceID,lockStatus FROM WFINSTRUMENTTABLE WHERE activityname = 'CPV' AND ProcessInstanceID ='"+formObject.getWFWorkitemName()+"'";
					CreditCard.mLogger.info( "query is ::"+sQuery);
					List<List<String>> list=formObject.getNGDataFromDataCache(sQuery);
					CreditCard.mLogger.info( "list is ::"+list+"size is::"+list.size());

					if(list.size()==0){

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

				//-- Above code added by abhishek as per CC FSD 2.7.3
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{
					hm.clear();}
			}

			if ("LoanCard_Details_Check".equalsIgnoreCase(pEvent.getSource().getName())) {
				hm.put("LoanCard_Details_Check","Clicked");
				formObject.fetchFragment("LoanCard_Details_Check", "LoanandCard", "q_LoanandCard");
				loadPicklist_loancardverification();
				List<String> LoadPicklist_Verification= Arrays.asList("cmplx_LoanandCard_loanamt_ver","cmplx_LoanandCard_tenor_ver","cmplx_LoanandCard_emi_ver","cmplx_LoanandCard_islorconv_ver","cmplx_LoanandCard_firstrepaydate_ver","cmplx_LoanandCard_cardtype_ver","cmplx_LoanandCard_cardlimit_ver");
				LoadPicklistVerification(LoadPicklist_Verification);
				LoadPickList("cmplx_LoanandCard_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");
				//formObject.setLocked("LoanandCard_Frame1", true);
				//loadPicklist3();
				//loadInDecGrid();
				enable_loanCard();//Arun 14/12/17 new function added for CPV
				loancardvalues();//Arun 14/12/17 new function added for smart CPV
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{
					hm.clear();}
			}



			if ("Smart_Check".equalsIgnoreCase(pEvent.getSource().getName())) {
				/*hm.put("Smart_Check","Clicked");
				popupFlag="N";*/
				formObject.fetchFragment("Smart_Check", "SmartCheck", "q_SmartCheck");

			}

			if ("Original_validation".equalsIgnoreCase(pEvent.getSource().getName())) {
				/*hm.put("Original_validation","Clicked");
				popupFlag="N";*/
				formObject.fetchFragment("Original_validation", "OriginalValidation", "q_OrigVal");
				fetchoriginalDocRepeater();
			}

			if ("Compliance".equalsIgnoreCase(pEvent.getSource().getName())) {
				hm.put("Compliance","Clicked");
				formObject.fetchFragment("Compliance", "Compliance", "q_Compliance");
				CreditCard.mLogger.info("after value of n Compliance_row");
				//++below code added by nikhil to load in compliance grid
				LoadPickList("Compliance_ResidenceCountry","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_Country with (nolock) where isActive='Y' order by code");
				LoadPickList("Compliance_BirthCountry","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_Country with (nolock) where isActive='Y' order by code");
				
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{
					hm.clear();}
			}

			if ("Limit_Increase".equalsIgnoreCase(pEvent.getSource().getName())) {
				hm.put("Limit_Increase","Clicked");
				formObject.fetchFragment("DecisionHistory", "DecisionHistory", "q_DecisionHistory");
				expandDecision();
				formObject.fetchFragment("Limit_Increase", "Limit_Inc", "q_LimitInc");
				
				//loadPicklist3();
				//loadInDecGrid();
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{
					hm.clear();}
			}
			if ("CC_Creation".equalsIgnoreCase(pEvent.getSource().getName())) {
				hm.put("CC_Creation","Clicked");
				//formObject.fetchFragment("Limit_Increase", "Limit_Inc", "");
				formObject.fetchFragment("DecisionHistory", "DecisionHistory", "q_DecisionHistory");
				LoadPickList("CC_Creation_Product","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cardProduct with (nolock) order by code");
				expandDecision();
				formObject.fetchFragment("CC_Creation", "CC_Creation", "");
				//commented by saurabh on 17th Dec
/*				//Tanshu Aggarwal Disbursal work 14/08/2017
				if("Y".equalsIgnoreCase(formObject.getNGValue("Is_NEW_CREADITCARD_REQ"))){
					formObject.setEnabled("CC_Creation_Button2", false);
				}
				else{
					formObject.setEnabled("CC_Creation_Button2", true);

				}*/
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
			if ("CAD_Decision".equalsIgnoreCase(pEvent.getSource().getName())) {
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
			if ("Card_Collection_Decision".equalsIgnoreCase(pEvent.getSource().getName())) {
				hm.put("Card_Collection_Decision","Clicked");
				formObject.fetchFragment("Card_Collection_Decision", "CardCollection", "q_CardCollection");
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

			if ("Customer_Details_Verification1".equalsIgnoreCase(pEvent.getSource().getName())) {

				formObject.fetchFragment("Customer_Details_Verification1", "CustDetailVerification1", "q_CustDetailVerification1");
				//++Below code added by  nikhil 12/10/17 as per CC FSD 2.2
				List<String> LoadPicklist_Verification= Arrays.asList("cmplx_CustDetailverification1_MobNo1_veri","cmplx_CustDetailverification1_MobNo2_veri","CustDetailVerification1_Combo3","cmplx_CustDetailverification1_PO_Box_Veri","cmplx_CustDetailverification1_Emirates_veri","cmplx_CustDetailverification1_Off_Telephone_veri");
				LoadPicklistVerification(LoadPicklist_Verification);
				openDemographicFrags(formObject);
				autopopulateValuesFCU(formObject);
				String CompName="",CompCif="";
				try
				{
					if("Self Employed".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6)) && formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",0,0)!=null ){
						
					int row_count=formObject.getLVWRowCount("cmplx_CompanyDetails_cmplx_CompanyGrid");
					for(int i=0;i<row_count;i++)
					{
						if(formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",0,2).equals("Secondary") )
						{
							CompName=formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 4);
							CompCif=formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 3);
							break;
						}
					}
					}
				}
				catch(Exception ex){
					CreditCard.mLogger.info(ex.getMessage());
				}
				formObject.setNGValue("CustDetailVerification1_Compname", CompName);
				formObject.setNGValue("CustDetailVerification1_Compcode", CompCif);
				//--above code added by  nikhil 12/10/17 as per CC FSD 2.2

			}
			if ("Business_Verification1".equalsIgnoreCase(pEvent.getSource().getName())) {

				formObject.fetchFragment("Business_Verification1", "BussinessVerification1", "q_bussverification1");
				LoadPickList("cmplx_BussVerification_contctTelChk", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Other' ");
				//12th sept
				CreditCard.mLogger.info( "Inside add button: business verification2 loadpicklist");
				LoadPickList("cmplx_BussVerification1_CmpEmirate","select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_emirate with (nolock) order by Code");
				autopopulateValuesBusinessVerification(formObject);
				CreditCard.mLogger.info( "Inside add button: business verification2 after function of autopopulate");
				//12th sept

			}
			if ("Employment_Verification".equalsIgnoreCase(pEvent.getSource().getName())) {
				int framestate4=formObject.getNGFrameState("Customer_Details_Verification1");
				String fvi="";
				if(framestate4 !=0){
					formObject.fetchFragment("Customer_Details_Verification1", "CustDetailVerification1", "q_CustDetailVeriFCU");
					fvi=formObject.getNGValue("cmplx_CustDetailverification1_Filed_Visit_value");
					formObject.setNGFrameState("Customer_Details_Verification1", 1);
					
				}
				else
				{
					fvi=formObject.getNGValue("cmplx_CustDetailverification1_Filed_Visit_value");
				}

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
				autopopulateValuesEmployeesVeri(formObject,fvi);
				//formObject.setTop("Banking_Check", formObject.getTop("Employment_Verification")+25);
				//formObject.setTop("Smart_Check1", formObject.getTop("Banking_Check")+25);
				String fcu_frames="Customer_Details_Verification1,Business_Verification1,Employment_Verification,Banking_Check,Smart_Check1";
				adjustFrameTops(fcu_frames);
				//12th sept

			}

			if ("Banking_Check".equalsIgnoreCase(pEvent.getSource().getName())) {
				//12th sept q-variable name was missing
				formObject.fetchFragment("Banking_Check", "BankingCheck", "q_BankingCheck"); 
				//++Below code added by  nikhil 12/10/17 as per CC FSD 2.2
				formObject.setEnabled("cmplx_BankingCheck_BankAccDetailUpdate", true);
				formObject.setLocked("cmplx_BankingCheck_BankAccDetailUpdate", false);
				formObject.setEnabled("BankingCheck_IFrame1", true);
				//--above code added by  nikhil 12/10/17 as per CC FSD 2.2

				//12th sept q-variable name was missing
			}

			if ("supervisor_section".equalsIgnoreCase(pEvent.getSource().getName())) {

				formObject.fetchFragment("supervisor_section", "supervisorsection", "q_Supervisor");

			}
			if ("Smart_Check1".equalsIgnoreCase(pEvent.getSource().getName())) {

				formObject.fetchFragment("Smart_Check1", "Smart_Check1", "q_smartcheck1");

			}
			// fragments for fcu tab end

			// post disbursal

			if ("Post_Disbursal".equalsIgnoreCase(pEvent.getSource().getName())) {

				formObject.fetchFragment("Post_Disbursal", "PostDisbursal", "q_PostDisbursal");

			}

		}
		catch(Exception vex){
			printException(vex);

		}
		finally{
//			throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
		}
		//commented by saurabh for color functionality
		/*catch(Exception ex){
			CreditCard.mLogger.info( "Exception in frameExpanded"+printException(ex));
		}*/
	}


	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha             
	Description                         : lock ALOC fields      

	 ***********************************************************************************  */
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
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha             
	Description                         : hide Liability Add Fields       

	 ***********************************************************************************  */
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


	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha             
	Description                         : auto populate values      

	 ***********************************************************************************  */
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
			if("RESIDENCE".equalsIgnoreCase(addType)){
				poboxResidence = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,8);
			}
			else if("OFFICE".equalsIgnoreCase(addType)){
				poboxOffice = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,8);
			}
			else if("Home".equalsIgnoreCase(addType)){
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


		String query = "select Description from ng_master_Emirate with (nolock) where code = '"+formObject.getNGValue("cmplx_Customer_EmirateOfResidence")+"'";
		String emirate = "";
		try
		{
		List<List<String>> db = formObject.getNGDataFromDataCache(query);
		if(db!=null && db.get(0)!=null && db.get(0).get(0)!=null){
			emirate= db.get(0).get(0); 
		}
		}
		catch(Exception ex)
		{
			emirate="";
		}
		setValues(formObject,mobNo1,mobNo2,dob,poboxResidence,poboxOffice,resNo,officeNo,homeNo,homeadd,email1,email2,emirate);
		//String poBox = formObject.getNGValue("cmplx_CustDetailVerification_POBoxNo_val");

	}
	//++Below code added by  nikhil 12/10/17 as per CC FSD 2.2
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha             
	Description                         :auto populated values FCU       

	 ***********************************************************************************  */
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
			//below code modified by nikhil for fcu issues on 4/12/17
			//change by saurabh on 7th Feb
			if("OFFICE".equalsIgnoreCase(addType))
			{
				poboxResidence = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,8);
				emirate = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,6);
			}			
		}
		//++Below code added by  nikhil 16/10/17 as per CC FSD 2.2
		CreditCard.mLogger.info("fcu @ emirate"+emirate);
		try
		{
			//below query chenged by nikhil for fcu issued on 4/12/17
			if(!"NA".equals(emirate))
			{
			List<List<String>> db=formObject.getNGDataFromDataCache("select Description from ng_master_state with (nolock) where Code='"+emirate+"'") ;
			if(db!=null && db.get(0)!=null && db.get(0).get(0)!=null){
				emirate= db.get(0).get(0); 
				CreditCard.mLogger.info("fcu @ emirate"+emirate);
			}
			}
		}
		catch(Exception ex)
		{
			emirate="";
			CreditCard.mLogger.info("inside fcu catch");
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
	/*          Function Header:

	 **********************************************************************************

         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


Date Modified                       : 6/08/2017              
Author                              : Disha             
Description                         : auto populate Vlaues Buss Verification       

	 ***********************************************************************************  */
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
		//below try added by nikhil 5/12/17
		
		try
		{
		//below if condition changed by nikhil 05/12/17
			String comp="";
			int row_count=formObject.getLVWRowCount("cmplx_CompanyDetails_cmplx_CompanyGrid");
			for(int i=0;i<row_count;i++)
			{
				if(formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",0,2).equals("Secondary"))
				{
					comp=formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 0);
					break;
				}
			}
			//if(formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",0,0)!=null)
			formObject.setNGValue("cmplx_BussVerification1_TradeLicenseNo",comp);
			formObject.setLocked("cmplx_BussVerification1_TradeLicenseNo", true);
			
			//below if condition changed by nikhil 05/12/17
			if(formObject.getNGValue("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails",0,1) !=null){
			formObject.setNGValue("cmplx_BussVerification1_Signatory_Name",formObject.getNGValue("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails",0,1));
			formObject.setLocked("cmplx_BussVerification1_Signatory_Name", true);
			}
			//below if condition changed by nikhil 05/12/17
			if(formObject.getNGValue("AlternateContactDetails_OfficeNo")!=null)
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
		catch(Exception ex)
		{
			CreditCard.mLogger.info(ex.getMessage());
		}
		
	}
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha             
	Description                         : auto populated Employess Veri       

	 ***********************************************************************************  */
	public void autopopulateValuesEmployeesVeri(FormReference formObject,String fvi){
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
		formObject.setNGValue("cmplx_EmploymentVerification_FiledVisitedInitiated_value",fvi);
		formObject.setLocked("cmplx_EmploymentVerification_FiledVisitedInitiated_value", true);
		String ConfirmedInJob=formObject.getNGValue("cmplx_EmploymentDetails_JobConfirmed");
		if("false".equalsIgnoreCase(ConfirmedInJob)){
			formObject.setNGValue("cmplx_EmploymentVerification_confirmedinJob_val","No");
			CreditCard.mLogger.info("in false");
		}
		else{
			formObject.setNGValue("cmplx_EmploymentVerification_confirmedinJob_val","Yes");
			CreditCard.mLogger.info("in true");
		}
		formObject.setLocked("cmplx_EmploymentVerification_confirmedinJob_val", true);

		formObject.setNGFrameState("Alt_Contact_container",1);
		//formObject.setNGFrameState("Customer_Details_Verification1", 1);
		/*formObject.setHeight("Employment_Verification",formObject.getTop("EmploymentVerification_Frame1")+formObject.getHeight("EmploymentVerification_Frame1")+5);
		formObject.setTop("Banking_Check", formObject.getTop("Employment_Verification")+formObject.getHeight("Employment_Verification")+15);
		formObject.setTop("Smart_Check1", formObject.getTop("Banking_Check")+25);*/


		//--above code added by  nikhil 13/10/17 as per CC FSD 2.2
	}
	//12th sept
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha             frame
	Description                         : set values     

	 ***********************************************************************************  */
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
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha             
	Description                         : set values Fcu tab      

	 ***********************************************************************************  */
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
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha             
	Description                         : check values     

	 ***********************************************************************************  */
	public boolean checkValue(String ngValue){
		if(ngValue==null ||"".equalsIgnoreCase(ngValue) ||" ".equalsIgnoreCase(ngValue) || "--Select--".equalsIgnoreCase(ngValue) || "false".equalsIgnoreCase(ngValue)){
			return false;
		}
		return true;
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha             
	Description                         : set form header      

	 ***********************************************************************************  */
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
			if(!"AE".equalsIgnoreCase(nationality)){
				formObject.setLocked("cmplx_Customer_masroomID",true);
			}
			formObject.setNGValue("WiLabel",formObject.getWFWorkitemName());

			formObject.setNGValue("UserLabel",formObject.getUserName()); 
			//formObject.setNGValue("IntroDateLabel",formObject.getNGValue("CreatedDate"));//Tarang to be removed on friday(1/19/2018)
			formObject.setNGValue("IntroDateLabel",formObject.getNGValue("Created_Date")); 
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
					,"Compliance","CPV_Analyst","CPV","CSM_Review",NGFUserResourceMgr_CreditCard.getGlobalVar("CC_CSM"),"DDVT_Checker","Disbursal","FCU","Fulfillment_RM","CAD_Analyst1","Cad_Analyst2","DDVT_Maker");

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
			if("Disbursal".equalsIgnoreCase(activityName)){

				formObject.setVisible("Loan_Disbursal", false);
				formObject.setTop("CC_Creation", 30);
				formObject.setTop("Limit_Increase", formObject.getTop("CC_Creation")+formObject.getHeight("CC_Creation")+5);
			}
			// ++ below code already present - 06-10-2017
			//added by nikhil as per CC FSD
			if("Smart_CPV".equalsIgnoreCase(activityName))
			{
				formObject.setVisible("Notepad_Details", false);
				formObject.setTop("LoanCard_Details_Check",formObject.getTop("Business_Verification")+formObject.getHeight("Business_Verification")+10);
				//below code modified by nikhil
				formObject.setTop("Smart_Check",formObject.getTop("Office_Mob_Verification")+30);
				CreditCard.mLogger.info( "Inside SMart_CPV Set header ");
			}
			//-- Above code added by abhishek as per CC FSD 2.7.3
			//++Below code added by  nikhil 12/10/17 as per CC FSD 2.2
			if("FCU".equalsIgnoreCase(activityName))
			{
				formObject.setVisible("Notepad_Details1", false);
				formObject.setVisible("supervisor_section", false);
				formObject.setTop("Smart_Check1",formObject.getTop("Banking_Check")+formObject.getHeight("Banking_Check")+10);

			}
			fetch_NotepadDetails();//added by akshay on 11/9/17 to load Notepad on Form load

		}catch(Exception e)
		{
			CreditCard.mLogger.info( "Exception:"+e.getMessage());
		}
	}
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha             
	Description                         : check Sec Nat app       

	 ***********************************************************************************  */
	public void checkSecNatapp(FormReference formObject) {
		String secnatRadio = formObject.getNGValue("cmplx_Customer_SecNAtionApplicable");

		if("Yes".equalsIgnoreCase(secnatRadio)){
			if(formObject.isLocked("cmplx_Customer_SecNationality")){
				formObject.setLocked("cmplx_Customer_SecNationality",false);
			}
		}
		else if("No".equalsIgnoreCase(secnatRadio) &&!formObject.isLocked("cmplx_Customer_SecNationality") ){
			formObject.setLocked("cmplx_Customer_SecNationality",true);

		}

	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha             
	Description                         :open fragments on load     

	 ***********************************************************************************  */
	public void openFragmentsonLoad(FormReference formObject , String activityName){
		try{
			formObject.setNGFrameState("CustomerDetails",0);
			formObject.fetchFragment("ProductContainer", "Product", "q_product");
			formObject.setNGFrameState("ProductContainer",0);       
			formObject.setNGValue("QueueLabel","CC_"+activityName);
			formObject.setNGValue("LoanLabel",formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,3));
			formObject.setNGValue("ProductLabel",formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0));
			formObject.setNGValue("SubProductLabel",formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2));
			formObject.setNGValue("CardLabel",formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5));
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct with (nolock)");
			LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_ApplicationType with (nolock) order by code");
			LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
			if(NGFUserResourceMgr_CreditCard.getGlobalVar("CC_IM").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2))){
				formObject.setVisible("Product_Label5",true);
				formObject.setVisible("ReqTenor",true);
			}

		}catch(Exception ex){
			CreditCard.mLogger.info( "Exception:"+ex.getMessage());
		}
	}
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha             
	Description                         : set Visible CPV Frames      

	 ***********************************************************************************  */
	public void setVisible_CPV_Frames(FormReference formObject){
		try{
			//formObject.fetchFragment("ProductContainer", "Product", "q_product");
			//formObject.setNGFrameState("ProductContainer", 0);
			int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			for(int i=0;i<n;i++){
				String strSubprod = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2);
				if("Business titanium Card".equalsIgnoreCase(strSubprod)){
					formObject.setVisible("Guarantor_Verification", false);
					formObject.setVisible("Office_Mob_Verification", false);
					formObject.setVisible("Reference_Detail_Verification", false);
					formObject.setVisible("HomeCountry_Verification", false);
					formObject.setVisible("ResidenceVerification", false);
					break;
				}
				else if("Instant Money".equalsIgnoreCase(strSubprod)){
					formObject.setVisible("Business_Verification", false);
					formObject.setVisible("HomeCountry_Verification", false);
					formObject.setVisible("ResidenceVerification", false);
					formObject.setVisible("Guarantor_Verification", false);
					formObject.setVisible("Reference_Detail_Verification", false);
					break;
				}
				else if("Salaried Credit Card".equalsIgnoreCase(strSubprod) || "Self Employed Credit Card".equalsIgnoreCase(strSubprod)){
					formObject.setVisible("Guarantor_Verification", false);
					break;

				}
			}
		}
		catch(Exception ex){
			CreditCard.mLogger.info( "Exception:"+ex.getMessage());
		}
	}
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha             
	Description                         : make input for grid     

	 ***********************************************************************************  */
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
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha             
	Description                         : open demographic fragments      

	 ***********************************************************************************  */
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
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha             
	Description                         : Disable patner details      

	 ***********************************************************************************  */
	public void disablePartnerDetails(FormReference formObject){
		int authgridRowCount = formObject.getLVWRowCount("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails");
		CreditCard.mLogger.info("authgridrowcount"+authgridRowCount);
		if(authgridRowCount >0){
			boolean flag = false;
			for(int i=0;i<authgridRowCount;i++){
				String soleEmployee = formObject.getNGValue("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails", i, 10);
				CreditCard.mLogger.info("soleEmployee"+soleEmployee);
				if("Yes".equalsIgnoreCase(soleEmployee)){
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
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha             
	Description                         : check company Details       

	 ***********************************************************************************  */
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
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha             
	Description                         : Mouse Clicked event     

	 ***********************************************************************************  */
	public void mouse_clicked(FormEvent pEvent) 
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
		CC_Integration_Input genX=new CC_Integration_Input();
		String alert_msg="";
		String outputResponse = "";
		String SystemErrorCode="";

		String ReturnCode="";
		String ReturnDesc="";
		if("cmplx_Product_cmplx_ProductGrid".equalsIgnoreCase(pEvent.getSource().getName())){
			String ReqProd=formObject.getNGValue("ReqProd");
			CreditCard.mLogger.info("Value of ReqProd is:"+ReqProd);
			loadPicklistProduct(ReqProd);
			//++Below code added by nikhil 7/11/17 as per CC issues sheet
			if(formObject.getNGValue("subProd")=="IM")
			{
				formObject.setVisible("ReqTenor", true);
				formObject.setVisible("Product_Label5", true);
			}
			//--Above code added by nikhil 7/11/17 as per CC issues sheet
		}
		//added by akshay on 18/1/18 to not allow to modify primary row  in company
		else if("cmplx_CompanyDetails_cmplx_CompanyGrid".equalsIgnoreCase(pEvent.getSource().getName())){

			if(formObject.getSelectedIndex("cmplx_CompanyDetails_cmplx_CompanyGrid")==0){
				formObject.setLocked("CompanyDetails_Frame1",true);
				formObject.setLocked("CompanyDetails_Modify",false);
				formObject.setLocked("CompanyDetails_Add",false);
			}
			else{
				formObject.setLocked("CompanyDetails_Frame1",false);
				formObject.setLocked("CompanyDetails_Modify",false);
				formObject.setLocked("CompanyDetails_Add",false);
				formObject.setLocked("cif",true);	
			}	
		}
		//ended by akshay
		
		else if ("AddressDetails_addr_Add".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setNGValue("Address_wi_name",formObject.getWFWorkitemName());
			CreditCard.mLogger.info( "Inside add button: "+formObject.getNGValue("Address_wi_name"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_AddressDetails_cmplx_AddressGrid");
		}
		else if ("BT_Add".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setNGValue("CC_Loan_wi_name",formObject.getWFWorkitemName());
			CreditCard.mLogger.info(formObject.getNGValue("CC_Loan_wi_name"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_CC_Loan_cmplx_btc");//cmplx_CC_Loan_cmplx_btc
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
			formObject.setEnabled("SmartCheck_Add",true);
			formObject.setEnabled("SmartCheck_Modify",false);
			CreditCard.mLogger.info( "Inside add button33: modify of SmartCheck_Modify details");
		}
		else if ("SmartCheck_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_SmartCheck_SmartCheckGrid");
			CreditCard.mLogger.info( "Inside add button33: delete of SmartCheck_Delete details");

		}
		if("SmartCheck_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Smart_Check");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL058");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		else if ("AddressDetails_addr_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_AddressDetails_cmplx_AddressGrid");
		}

		else if ("AddressDetails_addr_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_AddressDetails_cmplx_AddressGrid");

		}
		//change by saurabh on 20th Dec
		else if("IncomeDetails_FinacialSummarySelf".equalsIgnoreCase(pEvent.getSource().getName())){
		/*	formObject.setNGValue("cmplx_IncomeDetails_AvgBalFreq", "Half Yearly");
			formObject.setNGValue("cmplx_IncomeDetails_CreditTurnoverFreq", "Half Yearly");
			formObject.setNGValue("cmplx_IncomeDetails_AvgCredTurnoverFreq", "Half Yearly");
		*/	//Commented by aman for JIra no 4131
			String query = "select ((Sum(convert(float,replace([jan],'NA','0'))))+(Sum(convert(float,replace([feb],'NA','0'))))+(Sum(convert(float,replace([mar],'NA','0'))))+(Sum(convert(float,replace([apr],'NA','0'))))+(Sum(convert(float,replace([may],'NA','0'))))+(Sum(convert(float,replace([jun],'NA','0'))))+(Sum(convert(float,replace([jul],'NA','0'))))+(Sum(convert(float,replace([aug],'NA','0'))))+(Sum(convert(float,replace([sep],'NA','0'))))+(Sum(convert(float,replace([oct],'NA','0'))))+(Sum(convert(float,replace([nov],'NA','0')))) +(Sum(convert(float,replace([dec],'NA','0')))))as AVGBALDET from ng_rlos_FinancialSummary_AvgBalanceDtls where child_Wi ='"+formObject.getWFWorkitemName()+"'";
			List<List<String>> result = formObject.getNGDataFromDataCache(query);
			if(result!=null && !result.isEmpty())  //if(result!=null && result.size()>0)
			{
				
				formObject.setNGValue("cmplx_IncomeDetails_AvgBal", result.get(0).get(0));
				
			}
			String query2 = "select isNull((Sum(convert(float,replace([TotalCrAmt],'NA','0')))),0) as TotalCreditTurnover,isNull((Sum(convert(float,replace([TotalDrAmt],'NA','0')))),0) as AverageCreditTurnover from ng_rlos_FinancialSummary_TxnSummary where child_Wi ='"+formObject.getWFWorkitemName()+"'";
			List<List<String>> result2 = formObject.getNGDataFromDataCache(query2);
			if(result2!=null && !result2.isEmpty())  //if(result!=null && result.size()>0)
			{
				formObject.setNGValue("cmplx_IncomeDetails_CredTurnover", result2.get(0).get(0));
				formObject.setNGValue("cmplx_IncomeDetails_AvgCredTurnover", result2.get(0).get(1));
				
			}
			
		}
		else if ("EMploymentDetails_Button2".equalsIgnoreCase(pEvent.getSource().getName())) {
			String EmpName=formObject.getNGValue("EMploymentDetails_Text21");
			String EmpCode=formObject.getNGValue("EMploymentDetails_Text22");
			CreditCard.mLogger.info( "EMpName$"+EmpName+"$");
			String query=null;
			//changes done to check duplicate selection compare emp code and main emp code main_employer_code column added - 06-09-2017 Disha
			if("".equalsIgnoreCase(EmpName.trim()))
				query="select distinct(EMPR_NAME),EMPLOYER_CODE,NATURE_OF_BUSINESS,EMPLOYER_CATEGORY_PL_NATIONAL,EMPLOYER_CATEGORY_PL_EXPAT,INCLUDED_IN_PL_ALOC,DATE_OF_INCLUSION_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,DATE_OF_INCLUSION_IN_CC_ALOC,NAME_OF_AUTHORIZED_PERSON_FOR_ISSUING_SC_STL_PAYSLIP,ACCOMMODATION_PROVIDED,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,OWNER_PARTNER_SIGNATORY_NAMES_AS_PER_TL,ALOC_REMARKS,HIGH_DELINQUENCY_EMPLOYER,EMPLOYER_CATEGORY_PL,COMPANY_STATUS_CC,COMPANY_STATUS_PL,MAIN_EMPLOYER_CODE from NG_RLOS_ALOC_OFFLINE_DATA where EMPLOYER_CODE Like '%"+EmpCode+"%'";
			else if("".equalsIgnoreCase(EmpCode.trim()))
				query="select distinct(EMPR_NAME),EMPLOYER_CODE,NATURE_OF_BUSINESS,EMPLOYER_CATEGORY_PL_NATIONAL,EMPLOYER_CATEGORY_PL_EXPAT,INCLUDED_IN_PL_ALOC,DATE_OF_INCLUSION_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,DATE_OF_INCLUSION_IN_CC_ALOC,NAME_OF_AUTHORIZED_PERSON_FOR_ISSUING_SC_STL_PAYSLIP,ACCOMMODATION_PROVIDED,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,OWNER_PARTNER_SIGNATORY_NAMES_AS_PER_TL,ALOC_REMARKS,HIGH_DELINQUENCY_EMPLOYER,EMPLOYER_CATEGORY_PL,COMPANY_STATUS_CC,COMPANY_STATUS_PL,MAIN_EMPLOYER_CODE from NG_RLOS_ALOC_OFFLINE_DATA where EMPLOYER_CODE Like '%"+EmpName+"%'";

			else
				query="select distinct(EMPR_NAME),EMPLOYER_CODE,NATURE_OF_BUSINESS,EMPLOYER_CATEGORY_PL_NATIONAL,EMPLOYER_CATEGORY_PL_EXPAT,INCLUDED_IN_PL_ALOC,DATE_OF_INCLUSION_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,DATE_OF_INCLUSION_IN_CC_ALOC,NAME_OF_AUTHORIZED_PERSON_FOR_ISSUING_SC_STL_PAYSLIP,ACCOMMODATION_PROVIDED,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,OWNER_PARTNER_SIGNATORY_NAMES_AS_PER_TL,ALOC_REMARKS,HIGH_DELINQUENCY_EMPLOYER,EMPLOYER_CATEGORY_PL,COMPANY_STATUS_CC,COMPANY_STATUS_PL,MAIN_EMPLOYER_CODE from NG_RLOS_ALOC_OFFLINE_DATA where EMPR_NAME Like '%"+EmpName + "%' or EMPLOYER_CODE Like '%"+EmpCode+"'";

			CreditCard.mLogger.info( "query is: "+query);
			populatePickListWindow(query,"EMploymentDetails_Button2", "Employer Name,Employer Code,Nature Of Business,EMPLOYER CATEGORY PL NATIONAL,EMPLOYER CATEGORY PL EXPAT,INCLUDED IN PL ALOC,DOI IN PL ALOC,INCLUDED IN CC ALOC,DATE OF INCLUSION IN CC ALOC,NAME OF AUTHORIZED PERSON FOR ISSUING SC/STL/PAYSLIP,ACCOMMODATION PROVIDED,INDUSTRY SECTOR,INDUSTRY MACRO,INDUSTRY MICRO,CONSTITUTION,NAME OF FREEZONE AUTHORITY,OWNER/PARTNER/SIGNATORY NAMES AS PER TL,ALOC REMARKS,HIGH DELINQUENCY EMPLOYER,Employer Category PL, Employer Status CC,Employer Status PL,MAIN EMPLOYER CODE", true, 20);
		}
		else if("cmplx_EmploymentDetails_Others".equalsIgnoreCase(pEvent.getSource().getName())){
			try{
				if(	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_true").equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_Others"))){
					LoadPickList("cmplx_EmploymentDetails_EmpStatusCC","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_EmployerStatusCC where isActive='Y' and product='Credit Card' order by code" );					
					formObject.setLocked("cmplx_EmploymentDetails_EmpStatusCC",false);
					if("IM".equals(formObject.getNGValue("Subproduct_productGrid"))){
						formObject.removeItem("cmplx_EmploymentDetails_EmpStatusCC",2);
						formObject.setNGValue("cmplx_EmploymentDetails_EmpStatusCC","CN");//was not happening from js so done from here
					}
				}
				else{
					LoadPickList("cmplx_EmploymentDetails_EmpStatusCC","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_EmployerStatusCC where isActive='Y'  order by code" );					
					formObject.setLocked("cmplx_EmploymentDetails_EmpStatusCC",true);//was not happening from js so done from here

				}
			}
			catch(Exception e)
			{
				CreditCard.mLogger.info( "Exception occurred in removing item from cmplx_EmploymentDetails_EmpStatusCC"+printException(e));}
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
			alert_msg = NGFUserResourceMgr_CreditCard.getAlert("VAL039");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if("CompanyDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("CompanyDetails");
			alert_msg = NGFUserResourceMgr_CreditCard.getAlert("VAL008");

			throw new ValidatorException(new FacesMessage(alert_msg));

		}

		else if("PartnerDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("PartnerDetails");
		}

		else if("AuthorisedSignDetails_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("AuthorisedSignDetails_ShareHolding");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL061");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if("SelfEmployed_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Liability_container");
		}
		//added by yash on 21/8/2017
		else if("SupplementCardDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Supplementary_Container");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL059");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		else if("SupplementCardDetails_Add".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			//PersonalLoanS.mLogger.info("Suplementary add");
			formObject.setNGValue("SupplementCard_WiName", formObject.getWFWorkitemName());
			formObject.ExecuteExternalCommand("NGAddRow", "SupplementCardDetails_cmplx_supplementGrid");
		}
		else if("SupplementCardDetails_Modify".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			
			formObject.ExecuteExternalCommand("NGModifyRow", "SupplementCardDetails_cmplx_supplementGrid");
		}
		else if("SupplementCardDetails_Delete".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			
			formObject.ExecuteExternalCommand("NGDeleteRow", "SupplementCardDetails_cmplx_supplementGrid");
		}
		else if("WorldCheck1_Button4".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("World_Check");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL060");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		//added by yash on 21/8/2017
		//Added to add modify delete in the Liability grid
		//++below code commented bu nikhil 11/11/17
		/*
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
		 *///Added to add modify delete in the Liability grid
		else if("PartnerDetails_add".equalsIgnoreCase(pEvent.getSource().getName()))
		{
		/*
						formObject.setNGValue("partner_winame",formObject.getWFWorkitemName());
			CreditCard.mLogger.info( "Inside add button: "+formObject.getNGValue("partner_winame"));
		*/
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

		else  if("AuthorisedSignDetails_add".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setNGValue("AuthorisedSign_wiName",formObject.getWFWorkitemName());
			CreditCard.mLogger.info( "Inside add button: "+formObject.getNGValue("AuthorisedSign_wiName"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails");
			//added by akshay on 18/1/18 for enabling fields
			formObject.setLocked("AuthorisedSignDetails_CheckBox1",false);
			//formObject.setLocked("AuthorisedSignDetails_CIFNo",false);
			formObject.setLocked("AuthorisedSignDetails_FetchDetails",false);
			formObject.setLocked("AuthorisedSignDetails_Name",false);
			formObject.setLocked("AuthorisedSignDetails_DOB",false);
			formObject.setLocked("AuthorisedSignDetails_VisaNumber",false);
			formObject.setLocked("AuthorisedSignDetails_PassportNo",false);
			formObject.setLocked("AuthorisedSignDetails_nationality",false);
			formObject.setLocked("AuthorisedSignDetails_VisaExpiryDate",false);
			formObject.setLocked("AuthorisedSignDetails_PassportExpiryDate",false);
			formObject.setLocked("AuthorisedSignDetails_Status",false);
			formObject.setLocked("AuthorisedSignDetails_delete",false);
			formObject.setNGValue("AuthorisedSignDetails_nationality", "");
			formObject.setNGValue("AuthorisedSignDetails_Status", "--Select--");
			formObject.setNGValue("AuthorisedSignDetails_SoleEmployee", "--Select--");
			formObject.setNGValue("Designation", "");
			formObject.setNGValue("DesignationAsPerVisa", "");
		}

		else  if("AuthorisedSignDetails_modify".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails");
			//added by akshay on 18/1/18 for enabling fields
			formObject.setLocked("AuthorisedSignDetails_CheckBox1",false);
			//formObject.setLocked("AuthorisedSignDetails_CIFNo",false);
			formObject.setLocked("AuthorisedSignDetails_FetchDetails",false);
			formObject.setLocked("AuthorisedSignDetails_Name",false);
			formObject.setLocked("AuthorisedSignDetails_DOB",false);
			formObject.setLocked("AuthorisedSignDetails_VisaNumber",false);
			formObject.setLocked("AuthorisedSignDetails_PassportNo",false);
			formObject.setLocked("AuthorisedSignDetails_nationality",false);
			formObject.setLocked("AuthorisedSignDetails_VisaExpiryDate",false);
			formObject.setLocked("AuthorisedSignDetails_PassportExpiryDate",false);
			formObject.setLocked("AuthorisedSignDetails_Status",false);
			formObject.setLocked("AuthorisedSignDetails_delete",false);
			formObject.setNGValue("AuthorisedSignDetails_nationality", "");
			formObject.setNGValue("AuthorisedSignDetails_Status", "--Select--");
			formObject.setNGValue("AuthorisedSignDetails_SoleEmployee", "--Select--");
			formObject.setNGValue("Designation", "");
			formObject.setNGValue("DesignationAsPerVisa", "");
		}
		else  if("AuthorisedSignDetails_delete".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails");
			formObject.setNGValue("AuthorisedSignDetails_nationality", "");
			formObject.setNGValue("AuthorisedSignDetails_Status", "--Select--");
			formObject.setNGValue("AuthorisedSignDetails_SoleEmployee", "--Select--");
			formObject.setNGValue("Designation", "");
			formObject.setNGValue("DesignationAsPerVisa", "");
		}

		//added by Akshay on 7/9/2017 for Fetch Company Details call on CAD

		else if("CompanyDetails_Button3".equalsIgnoreCase(pEvent.getSource().getName())){
			CreditCard.mLogger.info("CompanyDetails_Button3");
			outputResponse = genX.GenerateXML("CUSTOMER_DETAILS","Corporation_CIF");

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
					//change by saurabh on 29th Nov 17 for Drop2 CR.
					Common_Utils common=new Common_Utils(CreditCard.mLogger);
					common.getAge(formObject.getNGValue("estbDate"),"lob");
					//String lob = getYearsDifference(formObject,"estbDate");
					//formObject.setNGValue("lob", lob);
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
		//change by saurabh on 6th Dec
		else if("AuthorisedSignDetails_FetchDetails".equalsIgnoreCase(pEvent.getSource().getName())){

			outputResponse = genX.GenerateXML("CUSTOMER_DETAILS","Signatory_CIF");


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
		formObject.saveFragment("EligibilityAndProductInformation"); //To save the finalLimit data if changed by aman
		CreditCard.mLogger.info("cmplx_FinacleCore_avg_credit_3month++"+formObject.getNGValue("cmplx_FinacleCore_avg_credit_3month"));
		CreditCard.mLogger.info("cmplx_FinacleCore_avg_credit_6month"+formObject.getNGValue("cmplx_FinacleCore_avg_credit_6month"));
		
		String Emp_type = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 6);
		if ("Salaried".equalsIgnoreCase(Emp_type)||"Salaried Pensioner".equalsIgnoreCase(Emp_type)){
			
			int framestate2=formObject.getNGFrameState("EmploymentDetails");
			if(framestate2 == 0){
				CreditCard.mLogger.info("EmploymentDetails already fetched.");
			}
			else {
				formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmpDetails");
				CreditCard.mLogger.info("fetched employment details");
			}
		}
		else if ("Self Employed".equalsIgnoreCase(Emp_type)){
			int framestate7=formObject.getNGFrameState("CompDetails");
			if(framestate7 == 0){
				CreditCard.mLogger.info("EmploymentDetails already fetched.");
			}
			else {
				formObject.fetchFragment("CompDetails", "CompanyDetails", "q_CompanyDetails");
				formObject.setTop("Auth_Sign_Details", formObject.getTop("CompDetails")+formObject.getHeight("CompDetails")+25);


				formObject.fetchFragment("Auth_Sign_Details", "AuthorisedSignDetails", "q_AuthorisedSignDetails");
				formObject.setTop("Partner_Details", formObject.getTop("Auth_Sign_Details")+formObject.getHeight("Auth_Sign_Details")+25);

		
				
				CreditCard.mLogger.info("fetched Internal_External_Liability details");
			}
				}
		int framestate3=formObject.getNGFrameState("Internal_External_Liability");
		if(framestate3 == 0){
			CreditCard.mLogger.info("EmploymentDetails already fetched.");
		}
		else {
			formObject.fetchFragment("Internal_External_Liability", "Liability_New", "q_Liabilities");
			
			CreditCard.mLogger.info("fetched Internal_External_Liability details");
		}
		
		int framestate4=formObject.getNGFrameState("IncomeDEtails");
		if(framestate4 == 0){
			CreditCard.mLogger.info("EmploymentDetails already fetched.");
		}
		else {
			formObject.fetchFragment("IncomeDEtails", "IncomeDetails", "q_IncomeDetails");
			income();
			
			CreditCard.mLogger.info("fetched Internal_External_Liability details");
		}
		
		formObject.fetchFragment("MOL", "MOL1", "q_CC_Loan");
		formObject.fetchFragment("World_Check", "WorldCheck1", "q_WorldCheck");	
		formObject.setTop("World_Check", formObject.getTop("MOL")+formObject.getHeight("MOL")+15);
		formObject.setTop("Reject_Enquiry", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+15);
		formObject.setTop("Salary_Enquiry", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+35);
		formObject.setTop("CreditCard_Enq", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+55);
		formObject.setTop("Case_hist", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+75);
		formObject.setTop("LOS", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+95);

		
		outputResponse = genX.GenerateXML("DECTECH","");
		CreditCard.mLogger.info("$$After genX.GenerateXML for outputResponse : "+outputResponse);

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
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL021");

			//formObject.setNGFrameState("EligibilityAndProductInformation", 1);
			formObject.RaiseEvent("WFSave");
			throw new ValidatorException(new FacesMessage(alert_msg));


		}
		else{
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL012");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}



		}

		//added By Akshay on 7/9/2017 for Dectech call on CAD decision	
		else if("DecisionHistory_calReElig".equalsIgnoreCase(pEvent.getSource().getName())){	
			formObject.setNGValue("DecCallFired","Decision");

			String Emp_type = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 6);
			if ("Salaried".equalsIgnoreCase(Emp_type)){
				//formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmpDetails");
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
			outputResponse = genX.GenerateXML("DECTECH","");


			SystemErrorCode =  (outputResponse.contains("<SystemErrorCode>")) ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"";
			CreditCard.mLogger.info(SystemErrorCode);
			if("".equalsIgnoreCase(SystemErrorCode)){
				valueSetCustomer(outputResponse,"");   
				alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL021");

				formObject.RaiseEvent("WFSave");
				throw new ValidatorException(new FacesMessage(alert_msg));				
			}
			else{
				alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL012");
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
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL034");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		//++ Above Code added By Yash on Oct 9, 2017  to fix : "to save liability" : Reported By Shashank on Oct 09, 2017++
		else if("EmpDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("EmploymentDetails");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL025");

			throw new ValidatorException(new FacesMessage(alert_msg));

		}
		//++ Below Code added By Yash on Oct 10, 2017  to fix : 10-"There should be only 1 save button below Partner details fragment.Similar to how it is at initiation" : Reported By Shashank on Oct 09, 2017++

		/*else if("CompanyDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("CompDetails");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL008");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}*/
		//++ Above Code added By Yash on Oct 10, 2017  to fix : 10-"There should be only 1 save button below Partner details fragment.Similar to how it is at initiation" : Reported By Shashank on Oct 09, 2017++

		/*if("ELigibiltyAndProductInfo_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("EligibilityAndProductInformation");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL024");

			throw new ValidatorException(new FacesMessage(alert_msg));

		}*/

		if("MiscellaneousFields_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("MiscFields");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL043");

			throw new ValidatorException(new FacesMessage(alert_msg));

		}

		if("AddressDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Address_Details_container");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL001");

			throw new ValidatorException(new FacesMessage(alert_msg));

		}

		if("AltContactDetails_Saved".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Alt_Contact_container");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL009");

			throw new ValidatorException(new FacesMessage(alert_msg));

		}

		if("CardDetails_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Supplementary_Cont");
			formObject.saveFragment("Card_Details");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL007");

			throw new ValidatorException(new FacesMessage(alert_msg));

		}


		if("FATCA_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("FATCA");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL036");

			throw new ValidatorException(new FacesMessage(alert_msg));

		}

		if("KYC_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("KYC");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL041");

			throw new ValidatorException(new FacesMessage(alert_msg));

		}

		if("OECD_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("OECD");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL045");

			throw new ValidatorException(new FacesMessage(alert_msg));

		}
		//added by yash on 24/8/2017
		if("NotepadDetails_SaveButton".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Notepad_Values");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL044");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		if("NotepadDetails_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Notepad_Values");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL044");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		if("Reference_Details_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Reference_Details");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL053");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		//added by yash on 8/8/2017
		else if("SalaryEnq_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("SalaryEnq_Frame1");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL056");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		else if("FinacleCRMCustInfo_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("FinacleCRMCustInfo_Frame1");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL037");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		else if("RejectEnq_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("RejectEnq_Frame1");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL054");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		else if("ExternalBlackList_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("ExternalBlackList_Frame1");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL035");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		//ended by yash on 8/8/2017


		//added by yash on 21/8/2017
		else if("DecisionHistory_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("DecisionHistory");
		}

		else if("SmartCheck1_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			
			formObject.saveFragment("Smart_Check");
			//below code added by nikhil 04/12/17
			alert_msg="Smart Check Saved";
			throw new ValidatorException(new FacesMessage(alert_msg));
			
		}
		else if("OriginalValidation_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("OriginalValidation");
			//below code added by nikhil 04/12/17
			alert_msg="Original Validation Details Saved";
			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		if("NotepadDetails_save".equalsIgnoreCase(pEvent.getSource().getName()) || "NotepadDetails_SaveButton".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Notepad_Values");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL044");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		else if("DecisionHistory_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("DecisionHistory");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL022");
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
		else if("CompanyDetails_Add".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setNGValue("company_winame",formObject.getWFWorkitemName());
			CreditCard.mLogger.info( "Inside add button: "+formObject.getNGValue("company_winame"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_CompanyDetails_cmplx_CompanyGrid");
			//added by akshay on 18/1/18 for unlocking frame which was locked when primary row was clicked
			formObject.setLocked("CompanyDetails_Frame1",false);
			formObject.setLocked("cif",true);
			//formObject.setLocked("lob",true);
		}

		else  if("CompanyDetails_Modify".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_CompanyDetails_cmplx_CompanyGrid");
			//added by akshay on 18/1/18 for unlocking frame which was locked when primary row was clicked
			formObject.setLocked("CompanyDetails_Frame1",false);
			formObject.setLocked("cif",true);
			//formObject.setLocked("lob",true);
		}
		else if("CompanyDetails_delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_CompanyDetails_cmplx_CompanyGrid");
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
		else if("ExtLiability_Button2".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setNGValue("LiabilityAdd_winame", formObject.getWFWorkitemName());
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
		}
		else if("ExtLiability_Button3".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
		}
		else if("ExtLiability_Button4".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
		}
		//Added to add modify delete in the Liability grid

		else if("Customer_save".equalsIgnoreCase(pEvent.getSource().getName())){
			CreditCard.mLogger.info( "Inside Customer_save button: ");
			formObject.saveFragment("CustomerDetails");
		}

		/*
					if(pEvent.getSource().getName().equalsIgnoreCase("Product_Save")){
						formObject.saveFragment("ProductContainer");
					}
		 */
		else if("GuarantorDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("GuarantorDetails");
		}
		
		// added for calculation on turnover grid fragment
		else if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_ReCalculate")){
			int n = formObject.getLVWRowCount("cmplx_FinacleCore_credturn_grid");
			CreditCard.mLogger.info("turnover grid rowcount is::"+n);
			if(n>0){
				calculateTrnovrGrid();
			}else{
				alert_msg="Please select at a row from grid";

				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			
			
		}
		// added by  abhishek to modify the avg nbc repeater data 
		
		else if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_Button9")){
			int n = formObject.getLVWRowCount("cmplx_FinacleCore_credturn_grid");
			if(formObject.getNGValue("FinacleCore_Consider_obli").equalsIgnoreCase("true")){
				if(formObject.getSelectedIndex("cmplx_FinacleCore_credturn_grid")>0 || formObject.getSelectedIndex("cmplx_FinacleCore_credturn_grid")==0){
					formObject.setNGValue("cmplx_FinacleCore_credturn_grid",formObject.getSelectedIndex("cmplx_FinacleCore_credturn_grid"),4,"true");
					for(int i = 0 ; i<n ; i++){
						if(formObject.getNGValue("cmplx_FinacleCore_credturn_grid", i, 0).equals(formObject.getNGValue("cmplx_FinacleCore_credturn_grid", formObject.getSelectedIndex("cmplx_FinacleCore_credturn_grid"), 0))){
							formObject.setNGValue("cmplx_FinacleCore_credturn_grid",i,4,"true");
						}
					}
				}
				else{
					alert_msg="Please select one row and then Modify";

					throw new ValidatorException(new FacesMessage(alert_msg));
				}


			}
			else{
				/*alert_msg="Please check consider for obligation and then Modify";

							throw new ValidatorException(new FacesMessage(alert_msg));*/
				if(formObject.getSelectedIndex("cmplx_FinacleCore_credturn_grid")>0 || formObject.getSelectedIndex("cmplx_FinacleCore_credturn_grid")==0){
					formObject.setNGValue("cmplx_FinacleCore_credturn_grid",formObject.getSelectedIndex("cmplx_FinacleCore_credturn_grid"),4,"false");
					for(int i = 0 ; i<n ; i++){
						if(formObject.getNGValue("cmplx_FinacleCore_credturn_grid", i, 0).equals(formObject.getNGValue("cmplx_FinacleCore_credturn_grid", formObject.getSelectedIndex("cmplx_FinacleCore_credturn_grid"), 0))){
							formObject.setNGValue("cmplx_FinacleCore_credturn_grid",i,4,"false");
						}
					}
				}
				else{
					alert_msg="Please select one row and then Modify";

					throw new ValidatorException(new FacesMessage(alert_msg));
				}


			}
		}
		// added for calculation on turnover grid fragment

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

		else if("Liability_New_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("InternalExternalContainer");
		}

		else if("EmpDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("EmploymentDetails");
		}

	/*	else if("ELigibiltyAndProductInfo_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("EligibilityAndProductInformation");
		}*/

		else if("MiscellaneousFields_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("MiscFields");
		}

		else if("AddressDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Address_Details_container");
		}
		// ++ below code not commented at offshore - 06-10-2017
		else if("AltContactDetails_ContactDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Alt_Contact_container");
		}

		else if("CardDetails_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Supplementary_Container");
		}


		else if("FATCA_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("FATCA");
		}

		else if("KYC_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("KYC");
		}

		else if("OECD_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("OECD");
		}


		else if("Customer_Reference_Add".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setNGValue("reference_wi_name",formObject.getWFWorkitemName());
			CreditCard.mLogger.info( "Inside add button: "+formObject.getNGValue("reference_wi_name"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Customer_cmplx_ReferenceGrid");
		}
		else if("Customer_Reference__modify".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Customer_cmplx_ReferenceGrid");
		}
		else if("Customer_Reference_delete".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_Customer_cmplx_ReferenceGrid");
		}
		else if ("Add".equalsIgnoreCase(pEvent.getSource().getName())){

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
			if(NGFUserResourceMgr_CreditCard.getGlobalVar("CC_Salaried").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6))){
				formObject.setSheetVisible("Tab1", 1, false);
			}
			//09/08/2017 to make company details invisible 
		}	
		else if ("Modify".equalsIgnoreCase(pEvent.getSource().getName())){

			String cardprodval = formObject.getNGSelectedItemText("CardProd"); //Arun (12/11/17) CardProd
			formObject.setNGValue("CardLabel",cardprodval);
			
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
			if(NGFUserResourceMgr_CreditCard.getGlobalVar("CC_Salaried").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6))){
				formObject.setSheetVisible("Tab1", 1, false);
				CreditCard.mLogger.info( "Inside add button:value of emp inside if of modify");
			}
			else{
				formObject.setSheetVisible("Tab1", 1, true);
				CreditCard.mLogger.info( "Inside add button:value of emp inside else of modify");
			}
			//09/08/2017 to make company details invisible 
		}

		else if ("Delete".equalsIgnoreCase(pEvent.getSource().getName())){
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
		else if("Product_Save".equalsIgnoreCase(pEvent.getSource().getName())){

			formObject.saveFragment("ProductContainer");

			throw new ValidatorException(new CustomExceptionHandler("", "Product_Save#Product Save Successful","", hm));
		}
		/*else if("IncomeDetails_Salaried_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("IncomeDetails");
			throw new ValidatorException(new CustomExceptionHandler("", "IncomeDetails_Salaried_Save#IncomeDetails_Salaried Save Successful","", hm));
		}*/
		/*else if ("EMploymentDetails_Button2".equalsIgnoreCase(pEvent.getSource().getName())) {
			String EmpName=formObject.getNGValue("EMploymentDetails_Text21");
			String EmpCode=formObject.getNGValue("EMploymentDetails_Text22");
			CreditCard.mLogger.info( "EMpName$"+EmpName+"$");
			String query=null;
			//changes done to check duplicate selection compare emp code and main emp code main_employer_code column added - 06-09-2017
			if("".equalsIgnoreCase(EmpName.trim()))
				query="select distinct(EMPR_NAME),EMPLOYER_CODE,INCLUDED_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,MAIN_EMPLOYER_CODE from NG_RLOS_ALOC_OFFLINE_DATA where EMPLOYER_CODE Like '%"+EmpCode+"%'";


			else
				query="select distinct(EMPR_NAME),EMPLOYER_CODE,INCLUDED_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,MAIN_EMPLOYER_CODE from NG_RLOS_ALOC_OFFLINE_DATA where EMPR_NAME Like '%"+EmpName + "%' or EMPLOYER_CODE Like '%"+EmpCode+"'";


			CreditCard.mLogger.info( "query is: "+query);
			populatePickListWindow(query,"EMploymentDetails_Button2", "Employer Name,Employer Code,INCLUDED IN PL ALOC,INCLUDED IN CC ALOC,INDUSTRY SECTOR,INDUSTRY MACRO,INDUSTRY MICRO,CONSTITUTION,NAME OF FREEZONE AUTHORITY,MAIN EMPLOYER CODE", true, 20);			     
		}--commented by akshay as it is aleady present above*/

		else if("EMploymentDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("EmploymentDetails");
			throw new ValidatorException(new CustomExceptionHandler("", "EMploymentDetails_Save#EMploymentDetails Save Successful","", hm));
		}
		else if("AuthorisedSignDetails_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Auth_Sign_Details");
			throw new ValidatorException(new CustomExceptionHandler("", "AuthorisedSignDetails_Button1#AuthorisedSignDetails_Button1 Save Successful","", hm));
		}
		else if("Button6".equalsIgnoreCase(pEvent.getSource().getName())){
			CreditCard.mLogger.info(formObject.getSelectedSheet(pEvent.getSource().getName())+"");
			formObject.setSelectedSheet("ParentTab",3);		
			formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligProd");
			formObject.setNGFrameState("EligibilityAndProductInformation", 0);
			formObject.setNGFrameState("EligibilityAndProductInformation", 1);
			formObject.setNGFrameState("EligibilityAndProductInformation", 0);
		}
		else if("ELigibiltyAndProductInfo_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("EligibilityAndProductInformation");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL024");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		//Duplicate Code 
		/*else if("CompanyDetails_Add".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setNGValue("company_winame",formObject.getWFWorkitemName());
			CreditCard.mLogger.info( "Inside add button: "+formObject.getNGValue("company_winame"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_CompanyDetails_cmplx_CompanyGrid");
		}
*/
		/*else if("CompanyDetails_Modify".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_CompanyDetails_cmplx_CompanyGrid");
		}*/

	/*	else if("CompanyDetails_delete".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_CompanyDetails_cmplx_CompanyGrid");
		}*/

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

		//Tanshu Aggarwal Reference Details Add,Delete,Modify functioning (17/08/2017)
		else if("Reference_Details_Reference_Add".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setNGValue("Reference_Details_reference_wi_name",formObject.getWFWorkitemName());
			CreditCard.mLogger.info( "Inside add button: "+formObject.getNGValue("Reference_Details_reference_wi_name"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_RefDetails_cmplx_Gr_ReferenceDetails");
		}

		else if("Reference_Details_Reference__modify".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_RefDetails_cmplx_Gr_ReferenceDetails");
		}

		else if("Reference_Details_Reference_delete".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_RefDetails_cmplx_Gr_ReferenceDetails");
			String query="DELETE  FROM ng_rlos_ReferenceDetails WHERE wi_name='"+formObject.getWFWorkitemName()+"'";
			CreditCard.mLogger.info("query"+query);
			List<List<String>> list=formObject.getNGDataFromDataCache(query);
			CreditCard.mLogger.info("list"+list);
		}

		//Tanshu Aggarwal Reference Details Add,Delete,Modify functioning (17/08/2017)
		else if("CompanyDetails_Button3".equalsIgnoreCase(pEvent.getSource().getName())){
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
                       if(("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode)) && (ReturnDesc.equalsIgnoreCase("Success") || ReturnDesc.equalsIgnoreCase("Successful"))){
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
		/*else if("FinacleCore_Button1".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			CreditCard.mLogger.info("Inside Add button FinacleCore_Button1 core"+"");
			formObject.ExecuteExternalCommand("NGAddRow", "FinacleCore_ListView2");
		}*/
		
		/*else if("FinacleCore_Button2".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			CreditCard.mLogger.info("Inside modify button FinacleCore_Button2 core"+"");
			formObject.ExecuteExternalCommand("NGModifyRow", "FinacleCore_ListView2");
		}*/
		/*else if("FinacleCore_Button3".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			CreditCard.mLogger.info("Inside delete button Finacle core"+"");
			formObject.ExecuteExternalCommand("NGDeleteRow", "FinacleCore_ListView2");
		}*/
		/*else if("FinacleCore_Button4".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			CreditCard.mLogger.info("Inside Add button Finacle core"+"");
			formObject.ExecuteExternalCommand("NGAddRow", "FinacleCore_ListView8");
		}*/
		/*else if("FinacleCore_Button5".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			CreditCard.mLogger.info("Inside modify button Finacle core"+"");
			formObject.ExecuteExternalCommand("NGModifyRow", "FinacleCore_ListView8");
		}*/
		/*else if("FinacleCore_Button6".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			CreditCard.mLogger.info("Inside delete button Finacle core"+"");
			formObject.ExecuteExternalCommand("NGDeleteRow", "FinacleCore_ListView8");
		}*/
		else if("WorldCheck1_Button1".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			CreditCard.mLogger.info("Inside Add button WorldCheck1_Button1 "+"");
			formObject.setNGValue("WorldCheck1_WiName",formObject.getWFWorkitemName());
			formObject.setNGValue("Is_WorldCheckAdd","Y");

			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_WorldCheck_WorldCheck_Grid");
		}
		else if("WorldCheck1_Button2".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			CreditCard.mLogger.info("Inside modify button WorldCheck1_Button2 core"+"");
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_WorldCheck_WorldCheck_Grid");
		}
		else if("WorldCheck1_Button3".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			CreditCard.mLogger.info("Inside delete button WorldCheck1_Button3 core"+"");

			formObject.setNGValue("Is_WorldCheckAdd","N");
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_WorldCheck_WorldCheck_Grid");
		}
		else if("FinacleCRMIncident_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Finacle_CRM_Incidents");
		}
		else if("FinacleCRMCustInfo_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Finacle_CRM_CustomerInformation");
		}
		/*else if("FinacleCore_Button8".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Finacle_Core");
		}*/
		else if("MOL1_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("MOL");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL078");
			throw new ValidatorException(new FacesMessage(alert_msg));
		
		}

		else if("PartMatch_Button3".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Part_Match");
		}
		
		else if("SalaryEnq_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Salary_Enquiry");
		}
		else if("RejectEnq_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Reject_Enquiry");
		}
		else if("CompanyDetails_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("CompDetails");
		}
		else if("PartnerDetails_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("PartnerDetails");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL046");

			throw new ValidatorException(new FacesMessage(alert_msg));

		}
		//added by yash on 24/8/2017
		else if("NotepadDetails_SaveButton".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Notepad_Values");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL044");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		else if("NotepadDetails_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Notepad_Values");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL044");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		/*else if("DecisionHistory_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("DecisionHistory");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL022");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}*/
		// added by abhishek as per CC FSD 
		//++below code commented by nikhil 9/11/17 to fix "On adding one row to the grid, two rows are reflecting in the notepad grid"
		/*if("NotepadDetails_Add".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_notegrid");
			Notepad_add();
		}
		if("NotepadDetails_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_NotepadDetails_cmplx_notegrid");
			Notepad_modify();
		}
		if("NotepadDetails_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_NotepadDetails_cmplx_notegrid");
			Notepad_delete();
		}*/
		//--above code commented by nikhil 9/11/17 to fix "On adding one row to the grid, two rows are reflecting in the notepad grid"
		// added by abhishek as per CC FSD
		/*else if("cmplx_NotepadDetails_cmplx_notegrid".equalsIgnoreCase(pEvent.getSource().getName())){
			Notepad_grid();
		} */
		else if("NotepadDetails_Add1".equalsIgnoreCase(pEvent.getSource().getName())){
			//++Below code added by nikhil 13/11/2017 for Code merge
			
			//++Below code added by  nikhil 10/10/17 winame added in ngaddrow
			formObject.setNGValue("NotepadDetails_winame",formObject.getWFWorkitemName());
			//++above code added by  nikhil 10/10/17 winame added in ngaddrow
			 Calendar cal = Calendar.getInstance();
		        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		        CreditCard.mLogger.info(" inside tele calling frame in hold_CPV"+""+ sdf.format(cal.getTime()) );
		        formObject.setNGValue("NotepadDetails_username", formObject.getUserName());
		        formObject.setNGValue("NotepadDetails_time",sdf.format(cal.getTime()) );
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_Telloggrid");
			int rowCount = formObject.getLVWRowCount("cmplx_NotepadDetails_cmplx_Telloggrid");
			formObject.setNGValue("cmplx_NotepadDetails_cmplx_Telloggrid", rowCount-1, 0, String.valueOf(rowCount));
			//--above code added by  nikhil 11/10/17 as per CC FSD 2.2
			//--Above code added by nikhil 13/11/2017 for Code merge
		}
		else if("NotepadDetails_Clear".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGClear", "cmplx_NotepadDetails_cmplx_Telloggrid");
		}
		//ended by aysh on24/8/2017
		else if("DecisionHistory_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("DecisionHistory");
		}
		else if("Customer_FetchDetails".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			if(	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_false").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_CIFID_Available"))){
				outputResponse =genX.GenerateXML("CUSTOMER_ELIGIBILITY","Primary_CIF");
				CreditCard.mLogger.info("RLOS value of ReturnDesc"+"Customer Eligibility");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				CreditCard.mLogger.info("RLOS value of ReturnCode"+ReturnCode);
				if("0000".equalsIgnoreCase(ReturnCode)){
					valueSetCustomer(outputResponse,"");    
					formObject.setNGValue("Is_Customer_Eligibility","Y");
					parse_cif_eligibility(outputResponse,"Primary_CIF");
					String NTB_flag = formObject.getNGValue("cmplx_Customer_NTB");
					if("true".equalsIgnoreCase(NTB_flag)){
						CreditCard.mLogger.info( "inside Customer Eligibility to through Exception to Exit:");
						alert_msg = NGFUserResourceMgr_CreditCard.getAlert("VAL013");
					}
					else{
						alert_msg = NGFUserResourceMgr_CreditCard.getAlert("VAL033");
						String Staff_cif = "";
						Staff_cif =  (outputResponse.contains("<IsStaff>")) ? outputResponse.substring(outputResponse.indexOf("<IsStaff>")+"</IsStaff>".length()-1,outputResponse.indexOf("</IsStaff>")):"";
						if("Y".equalsIgnoreCase(Staff_cif)){
							alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL074");
						}
						else{
						outputResponse = genX.GenerateXML("CUSTOMER_DETAILS","Primary_CIF");
						CreditCard.mLogger.info("RLOS value of ReturnCode"+"Inside Customer");
						if("0000".equalsIgnoreCase(ReturnCode)){
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
							alert_msg = NGFUserResourceMgr_CreditCard.getAlert("VAL028");
							CreditCard.mLogger.info("PL DDVT Maker value of ReturnCode"+"Error in Customer Eligibility: "+ ReturnCode);
						}
						formObject.RaiseEvent("WFSave");
					}
					}
				}
				else{
					CreditCard.mLogger.info("PL DDVT Maker value of ReturnCode"+"Error in Customer Eligibility: "+ ReturnCode);
					alert_msg = NGFUserResourceMgr_CreditCard.getAlert("VAL027");
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
				if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_CIFNo")) && !" ".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_CIFNo"))){
					outputResponse = genX.GenerateXML("CUSTOMER_DETAILS","Primary_CIF");
					CreditCard.mLogger.info("RLOS value of ReturnCode"+"Inside Customer");
					if("0000".equalsIgnoreCase(ReturnCode)){
						String Staff_cif = "";
						Staff_cif =  (outputResponse.contains("<IsStaff>")) ? outputResponse.substring(outputResponse.indexOf("<IsStaff>")+"</IsStaff>".length()-1,outputResponse.indexOf("</IsStaff>")):"";
						if("Y".equalsIgnoreCase(Staff_cif)){
							alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL074");
						}
						else{
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
					}
					else{
						alert_msg = NGFUserResourceMgr_CreditCard.getAlert("VAL028");
						CreditCard.mLogger.info("PL DDVT Maker value of ReturnCode"+"Error in Customer Eligibility: "+ ReturnCode);
					}



					formObject.RaiseEvent("WFSave");
				}
				// customer details called by saurabh on 23rd Sept for Demo findings end.
			}
			//below code added by nikhil 12/12/17
			throw new ValidatorException(new FacesMessage(alert_msg));
			//added
		}
		//ended

		//added
		/*else if(formObject.getNGValue("cmplx_Customer_CIFID_Available")=="true"){
			CreditCard.mLogger.info("RLOS value of Customer Details ----1234567"+formObject.getNGValue("cmplx_Customer_CIFID_Available"));
			CreditCard.mLogger.info("RLOS value of Customer Details ----1234567"+"inside true");
			outputResponse = genX.GenerateXML("CUSTOMER_DETAILS","Primary_CIF");
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
			if("0000".equalsIgnoreCase(ReturnCode)){
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

		else if("SupplementCardDetails_FetchDetails".equalsIgnoreCase(pEvent.getSource().getName())){

			String EmiratesID = formObject.getNGValue("SupplementCardDetails_Text6");
			CreditCard.mLogger.info("CC_DDVT_MAker "+ "EID value for Entity Details for Supplementary Cards: "+EmiratesID);
			String primaryCif = null;
			boolean isEntityDetailsSuccess = false;

			if( EmiratesID!=null && !"".equalsIgnoreCase(EmiratesID)){
				outputResponse = genX.GenerateXML("ENTITY_DETAILS","Supplementary_Card_Details");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				CreditCard.mLogger.info("CC_DDVT_MAker value of ReturnCode for Supplementary Cards: "+ReturnCode);
				if("0000".equalsIgnoreCase(ReturnCode)){
					//valueSetCustomer(outputResponse , "Supplementary_Card_Details");
					primaryCif = (outputResponse.contains("<CIFID>")) ? outputResponse.substring(outputResponse.indexOf("<CIFID>")+"</CIFID>".length()-1,outputResponse.indexOf("</CIFID>")):"";
					formObject.setNGValue("Supplementary_CIFNO",primaryCif);
					isEntityDetailsSuccess = true;
					alert_msg = fetch_cust_details_supplementary();
				}

				CreditCard.mLogger.info("CC_DDVT_MAker value of Primary Cif"+primaryCif);
			}
			if(!isEntityDetailsSuccess || (primaryCif==null || "".equalsIgnoreCase(primaryCif))){
				outputResponse =genX.GenerateXML("CUSTOMER_ELIGIBILITY","Supplementary_Card_Details");
				CreditCard.mLogger.info("CC_DDVT_MAker value of ReturnDesc for Supplementary Cards: "+"Customer Eligibility");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				CreditCard.mLogger.info("CC_DDVT_MAker value of ReturnCode for Supplementary Cards: "+ReturnCode);
				if("0000".equalsIgnoreCase(ReturnCode) )
				{
					valueSetCustomer(outputResponse ,"Supplementary_Card_Details");    
					parse_cif_eligibility(outputResponse,"Supplementary_Card_Details");
					alert_msg = fetch_cust_details_supplementary();

				}
			}

		}

		else if("Customer_Button1".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			//if("Is_Entity_Details".equalsIgnoreCase("Y") && "Is_Customer_Details".equalsIgnoreCase("Y")){
			String NegatedFlag="";
			String BlacklistFlag="";
			String DuplicationFlag="";
			outputResponse =genX.GenerateXML("CUSTOMER_ELIGIBILITY","Primary_CIF");
			CreditCard.mLogger.info("RLOS value of ReturnDesc"+"Customer Eligibility");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			CreditCard.mLogger.info("RLOS value of ReturnCode"+ReturnCode);
			formObject.setNGValue("Is_Customer_Eligibility","Y");


			if("0000".equalsIgnoreCase(ReturnCode)){
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
				if("true".equalsIgnoreCase(NTB_flag)){
					CreditCard.mLogger.info( "inside Customer Eligibility to through Exception to Exit:");
					alert_msg = NGFUserResourceMgr_CreditCard.getAlert("VAL013");
				}
				else{
					alert_msg = NGFUserResourceMgr_CreditCard.getAlert("VAL014");
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

		else if ("PartMatch_Search".equalsIgnoreCase(pEvent.getSource().getName())){
			//genX.GenerateXML();
			CreditCard.mLogger.info("PL PartMatch_Search"+ "Inside PartMatch_Search button: ");

			//HashMap<String,String> hm1= new HashMap<String,String>(); // not nullable HashMap

			//hm1.put("PartMatch_Search","Clicked");
			// popupFlag="N";
			outputResponse = genX.GenerateXML("DEDUP_SUMMARY","");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			CreditCard.mLogger.info("PL value of ReturnCode"+ReturnCode);
			ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";	
			CreditCard.mLogger.info("PL value of ReturnDesc"+ReturnDesc);
			if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode) ){
				//valueSetCustomer(outputResponse);	
				formObject.clear("cmplx_PartMatch_cmplx_Partmatch_grid");
				parseDedupe_summary(outputResponse);	
				formObject.setNGValue("Is_PartMatchSearch","Y");
				formObject.setLocked("PartMatch_Blacklist", false);
				CreditCard.mLogger.info("PL value of Part Match Request"+"inside if of partmatch");
				alert_msg= NGFUserResourceMgr_CreditCard.getAlert("VAL064");
			}
			else{
				formObject.setNGValue("Is_PartMatchSearch","N");
				formObject.setLocked("PartMatch_Blacklist", false);
				CreditCard.mLogger.info("PL value of Part Match Request"+"inside else of partmatch");
				alert_msg= NGFUserResourceMgr_CreditCard.getAlert("VAL031");
			}
			formObject.RaiseEvent("WFSave");
			CreditCard.mLogger.info("PL value of Part Match Request"+formObject.getNGValue("Is_PartMatchSearch"));
			if((	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_Y").equalsIgnoreCase(formObject.getNGValue("Is_PartMatchSearch"))))
			{ 
				CreditCard.mLogger.info("RLOS value of Is_CUSTOMER_UPDATE_REQ"+"inside if condition of disabling the button");
			}
			else{
				formObject.setEnabled("PartMatch_Search", true);
				//	throw new ValidatorException(new CustomExceptionHandler("Dedupe Summary Fail!!","PartMatch_Search#Dedupe Summary Fail!!","",hm));
			}
			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		else if ("FinacleCRMCustInfo_Button2".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_FinacleCRMCustInfo_FincustGrid");
			//below code added by nikhil as per ddvt issues
			int row_count=formObject.getLVWRowCount("cmplx_FinacleCRMCustInfo_FincustGrid");
			boolean cob_flag=false;
			for(int i=0;i<row_count;i++)
			{
				if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_FinacleCRMCustInfo_FincustGrid",i,13)))
					cob_flag=true;
			}
			if(!cob_flag)
			{
				formObject.setNGValue("cmplx_Customer_NTB", "true");
				formObject.setEnabled("cmplx_Customer_CIFNo", false);
			}
			//below code modified by nikhil
			else
			{
				formObject.setEnabled("cmplx_Customer_CIFNo", true);
			}
		}
		else if ("PartMatch_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
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



			outputResponse =genX.GenerateXML("CUSTOMER_DETAILS","Primary_CIF");
			CreditCard.mLogger.info("CC value of ReturnDesc"+"Customer Details part Match");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			CreditCard.mLogger.info("CC value of ReturnCode part Match"+ReturnCode);
			if("0000".equalsIgnoreCase(ReturnCode)){

				BlacklistFlag_Part =  (outputResponse.contains("<BlackListFlag>")) ? outputResponse.substring(outputResponse.indexOf("<BlackListFlag>")+"</BlackListFlag>".length()-1,outputResponse.indexOf("</BlackListFlag>")):"";                                  
				formObject.setNGValue("Is_Customer_Details_Part","Y"); 
				BlacklistFlag_reason =  (outputResponse.contains("<BlackListReason>")) ? outputResponse.substring(outputResponse.indexOf("<BlackListReason>")+"</BlackListReason>".length()-1,outputResponse.indexOf("</BlackListReason>")):"NA";
				BlacklistFlag_code =  (outputResponse.contains("<BlackListReasonCode>")) ? outputResponse.substring(outputResponse.indexOf("<BlackListReasonCode>")+"</BlackListReasonCode>".length()-1,outputResponse.indexOf("</BlackListReasonCode>")):"NA";



				NegativeListFlag =  (outputResponse.contains("<NegativeListFlag>")) ? outputResponse.substring(outputResponse.indexOf("<NegativeListFlag>")+"</NegativeListFlag>".length()-1,outputResponse.indexOf("</NegativeListFlag>")):"NA";
				CreditCard.mLogger.info("CC value of ReturnCode part Match"+ReturnCode);
				CreditCard.mLogger.info("Value of NegativeListFlag: "+NegativeListFlag+" BlacklistFlag_code:Blacklist Reason: "+BlacklistFlag_reason+": "+BlacklistFlag_code);



				if("Y".equalsIgnoreCase(BlacklistFlag_Part))
				{

					CreditCard.mLogger.info("CC value of BlacklistFlag_Part"+"Customer is Blacklisted");    
				}
				else{
					BlacklistFlag_Part="N";


					CreditCard.mLogger.info("CC value of BlacklistFlag_Part"+"Customer is not Blacklisted");    
				}
				//added by Akshay
				if("Y".equalsIgnoreCase(NegativeListFlag))
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
				//below code added by nikhil 
				String Cust_type=formObject.getNGValue("cmplx_PartMatch_cmplx_Partmatch_grid",formObject.getSelectedIndex("cmplx_PartMatch_cmplx_Partmatch_grid"),17);
				String Applicant="";
				if("R".equalsIgnoreCase(Cust_type))
				{
					Applicant="Individual";
				}
				else
				{
					Applicant="Corporate";
				}
				list_custinfo.add(CIFID);  // cif id from partmatch
				//below code added by nikhil 11/12/17
				list_custinfo.add(Applicant);//applicant
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
				//below code modified 8/12/17 to set by default consider for obligations
				list_custinfo.add("true");
				list_custinfo.add("");

				CreditCard.mLogger.info("CC DDVT Maker"+ "list_custinfo list values"+list_custinfo);
				formObject.addItemFromList("cmplx_FinacleCRMCustInfo_FincustGrid", list_custinfo);
				//below code added by nikhil 11/12/17 for if CIF identified and added to application the ntb is unchecked
				if(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true"))
				{
				formObject.setNGValue("cmplx_Customer_NTB", "false");
				}
			}catch(Exception e){
				CreditCard.mLogger.info("PL DDVT Maker"+ "Exception while setting data in grid:"+e.getMessage());
				alert_msg= NGFUserResourceMgr_CreditCard.getAlert("VAL032");
				throw new ValidatorException(new FacesMessage(alert_msg));
			}

			formObject.RaiseEvent("WFSave");          
		}	 /*if(pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails_Button2")){

							outputResponse = genX.GenerateXML("CUSTOMER_DETAILS","Guarantor_CIF");
							ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
							SKLogger.writeLog("CC value of ReturnCode",ReturnCode);
							ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
							SKLogger.writeLog("CC value of ReturnDesc",ReturnDesc);
							if(("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode)) && (ReturnDesc.equalsIgnoreCase("Success") || ReturnDesc.equalsIgnoreCase("Successful"))){
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
		/*else if("CompanyDetails_Button3".equalsIgnoreCase(pEvent.getSource().getName())){
			CreditCard.mLogger.info("CompanyDetails_Button3");
			outputResponse = genX.GenerateXML("CUSTOMER_DETAILS","Corporation_CIF");
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
			if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode) ){
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
		}*/
		else if("AuthorisedSignDetails_Button4".equalsIgnoreCase(pEvent.getSource().getName())){

			outputResponse = genX.GenerateXML("CUSTOMER_DETAILS","Authorised_CIF");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			CreditCard.mLogger.info(ReturnCode);
			ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
			CreditCard.mLogger.info("CC value of ReturnDesc"+ReturnDesc);
			if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode)){
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

		/*else if("BTC_save".equalsIgnoreCase(pEvent.getSource().getName()) || "DDS_save".equalsIgnoreCase(pEvent.getSource().getName()) || "SI_save".equalsIgnoreCase(pEvent.getSource().getName()) || "RVC_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Details");
		}
		*/
		else if("BTC_save".equalsIgnoreCase(pEvent.getSource().getName()) ){
			formObject.saveFragment("Details");
			
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL074");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		else if("DDS_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Details");
			
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL075");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		else if("SI_save".equalsIgnoreCase(pEvent.getSource().getName()) ){
			formObject.saveFragment("Details");
			
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL076");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		else if("RVC_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Details");
			
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL077");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		
		
		else if("PartMatch_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Part_Match");
		}
		//for BlackList Call added on 3rd May 2017
		//Changes done by aman to correctly save the value in the grid
		else if ("PartMatch_Blacklist".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.clear("cmplx_PartMatch_Blacklist_Grid");
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
				outputResponse =genX.GenerateXML("BLACKLIST_DETAILS","");
				CreditCard.mLogger.info("PL value of ReturnDesc"+"Blacklist Details part Match");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				CreditCard.mLogger.info("PL value of ReturnCode part Match"+ReturnCode);
				ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
				CreditCard.mLogger.info("PL value of ReturnDesc part Match"+ReturnDesc);

				if("0000".equalsIgnoreCase(ReturnCode) ){
					//alert_msg = "BlackList Check Successfully: " ;
					formObject.setNGValue("Is_Customer_Eligibility_Part","Y");   
					CreditCard.mLogger.info("PL value of BlacklistFlag_Part Customer is Blacklisted StatusType"+formObject.getNGValue("Is_Customer_Eligibility_Part"));
					StatusType= (outputResponse.contains("<StatusType>")) ? outputResponse.substring(outputResponse.indexOf("<StatusType>")+"</StatusType>".length()-1,outputResponse.indexOf("</StatusType>")):"";
					CreditCard.mLogger.info("PL value of BlacklistFlag_Part"+"Customer is Blacklisted StatusType"+StatusType);
					outputResponse = outputResponse.substring(outputResponse.indexOf("<CustomerBlackListResponse>")+27, outputResponse.indexOf("</CustomerBlackListResponse>"));
					CreditCard.mLogger.info(outputResponse);
					outputResponse = "<?xml version=\"1.0\"?><Response>" + outputResponse;
					outputResponse = outputResponse+"</Response>";

					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = factory.newDocumentBuilder();
					InputSource is = new InputSource(new StringReader(outputResponse));

					Document doc = builder.parse(is);
					doc.getDocumentElement().normalize();

					CreditCard.mLogger.info("Root element :"+doc.getDocumentElement().getNodeName());

					NodeList nList = doc.getElementsByTagName("StatusInfo");


					for (int temp = 0; temp < nList.getLength(); temp++) {

						Node nNode = nList.item(temp);


						if (nNode.getNodeType() == Node.ELEMENT_NODE) {
							Element eElement = (Element) nNode;
							StatusType = eElement.getElementsByTagName("StatusType").item(0).getTextContent() ;
							CreditCard.mLogger.info("PL value of BlacklistFlag_PartCustomer is Blacklisted StatusType"+StatusType);
							if("Black List".equalsIgnoreCase(StatusType)){
								//	Blacklistflag= (outputResponse.contains("<StatusFlag>")) ? outputResponse.substring(outputResponse.indexOf("<StatusFlag>")+"</StatusFlag>".length()-1,outputResponse.indexOf("</StatusFlag>")):"";
								if(eElement.getElementsByTagName("StatusFlag")!=null && eElement.getElementsByTagName("StatusFlag").item(0)!=null){
								Blacklistflag = eElement.getElementsByTagName("StatusFlag").item(0).getTextContent() ;
								}
								if(eElement.getElementsByTagName("StatusReason")!=null && eElement.getElementsByTagName("StatusReason").item(0)!=null){
								BlacklistReason = eElement.getElementsByTagName("StatusReason").item(0).getTextContent() ;
								}
								if(eElement.getElementsByTagName("StatusCode")!=null && eElement.getElementsByTagName("StatusCode").item(0)!=null){
								BlacklistCode = eElement.getElementsByTagName("StatusCode").item(0).getTextContent() ;
								}
								CreditCard.mLogger.info("PL value of BlacklistFlag_Part Customer is Blacklisted StatusCode"+BlacklistCode);
							}
							if("Negative List".equalsIgnoreCase(StatusType)){
								if(eElement.getElementsByTagName("StatusFlag")!=null && eElement.getElementsByTagName("StatusFlag").item(0)!=null){
								Negatedflag = eElement.getElementsByTagName("StatusFlag").item(0).getTextContent() ;
								}
								if(eElement.getElementsByTagName("StatusReason")!=null && eElement.getElementsByTagName("StatusReason").item(0)!=null){
								NegatedReason = eElement.getElementsByTagName("StatusReason").item(0).getTextContent() ;
								}
								if(eElement.getElementsByTagName("StatusCode")!=null && eElement.getElementsByTagName("StatusCode").item(0)!=null){
								NegatedCode = eElement.getElementsByTagName("StatusCode").item(0).getTextContent() ;
								}
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
						//below code added by nikhil 12/12/17
						alert_msg = "BlackList Check Successfull";

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
				alert_msg = "BlackList Check Successfully. Blacklist Flag: "+Blacklistflag ;  //alert change to show the blacklist at the frontend

				
			}
			catch(Exception ex)
			{
				CreditCard.logException(ex);
				//below code added by nikhil 12/12/17
				alert_msg = "BlackList Check failed Please contact administrator";
			}
			throw new ValidatorException(new FacesMessage(alert_msg));
			
		}
		//Changes done by aman to correctly save the value in the grid
		//ended here for BlackList Call
		//ended here for BlackList Call
		else if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_AddToTurnover")){
			// ++ below code already present - 06-10-2017 this different at offshore
			IRepeater repObj = formObject.getRepeaterControl("FinacleCore_Frame11");
			if(!repObj.getValue(31, "cmplx_FinacleCore_cmplx_TurnoverNBC_JAN1").equals("")){
				addToTrnoverGrid(repObj);
			}
			else{
				alert_msg="Please calculate and then click on add to Turnover button";

				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			
		}
		
		else if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_AddToAverage")){
			// ++ below code already present - 06-10-2017 this different at offshore
			IRepeater repObj = formObject.getRepeaterControl("FinacleCore_Frame10");
			if(!repObj.getValue(33, "cmplx_FinacleCore_cmplx_AvgBalanceNBC_JAN").equals("")){
				addToAvgRepeater(repObj);
				
			}
			else{
				alert_msg="Please calculate and then click on add to average button";

				throw new ValidatorException(new FacesMessage(alert_msg));
			}
		}
		
		// added by  abhishek to modify the avg nbc repeater data 
					else if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_Repeater_modify")){
						boolean flag = false;
						IRepeater repObj1 = formObject.getRepeaterControl("FinacleCore_Frame9");
						String Accno = formObject.getNGValue("FinacleCore_acc_modify");
						for( int i=0 ; i<6 ; i++){
							if(repObj1.getValue(i,"cmplx_FinacleCore_cmplx_avgbalance_new_Account").equalsIgnoreCase(Accno)){
								flag=true;
							}
						}
						if(flag==true){
							ModifyAvgNBCData(Accno,repObj1);
						}
						else{
							alert_msg="Please enter Account number from Repeater";

							throw new ValidatorException(new FacesMessage(alert_msg));
						}
						
						// ++ below code not commented at offshore - 06-10-2017
						//added by nikhil for repeater to clear text
									formObject.clear("FinacleCore_accmodify");
									//added by nikhil for repeater of avg nbc last 1 year
									formObject.clear("cmplx_FinacleCore_total_avg_last13");
									formObject.clear("cmplx_FinacleCore_total_avg_last_16");
									formObject.clear("cmplx_FinacleCore_toal_accounts_last1");
									//ended by nikhil
						}
		
		
		// added by  abhishek to modify the turnover repeater data 
		else if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_TrnRepeater_modify")){
			if(formObject.getSelectedIndex("cmplx_FinacleCore_credturn_grid")>0 || formObject.getSelectedIndex("cmplx_FinacleCore_credturn_grid")==0){
				String Accno = formObject.getNGValue("cmplx_FinacleCore_credturn_grid", formObject.getSelectedIndex("cmplx_FinacleCore_credturn_grid"), 0);
				ModifyTrnOverData(Accno);
				int j = formObject.getSelectedIndex("cmplx_FinacleCore_credturn_grid");

				/// clear grid
				int n = formObject.getLVWRowCount("cmplx_FinacleCore_credturn_grid");

				int[] arrOfIndexes =  new int[n];
				int x = 0;
				CreditCard.mLogger.info("turnover grid after select query for turnover row count value is::"+n);
				for(int i=0;i<n;i++){

					if(formObject.getNGValue("cmplx_FinacleCore_credturn_grid",i,0).equalsIgnoreCase(Accno) ){
						arrOfIndexes[x] = i;
						x++;

					}
				}
				CreditCard.mLogger.info("turnover grid after select query for turnover arrray value is::"+arrOfIndexes);
				formObject.setSelectedIndices("cmplx_FinacleCore_credturn_grid", arrOfIndexes);
				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_FinacleCore_credturn_grid");
			}

			else{
				alert_msg="Please select one row and then Modify Data";

				throw new ValidatorException(new FacesMessage(alert_msg));
			}


		}
		
		else if(pEvent.getSource().getName().equalsIgnoreCase("FinacleCore_Button8")){
			 // ++ below code not commented at offshore - 06-10-2017
			//added by nikhil for repeater of avg nbc last 1 year
						formObject.clear("cmplx_FinacleCore_total_avg_last13");
						formObject.clear("cmplx_FinacleCore_total_avg_last_16");
						formObject.clear("cmplx_FinacleCore_toal_accounts_last1");
						//ended by nikhil
			try{
				CalculateAvgTotal("cmplx_FinacleCore_cmplx_avgbalance_new_JAN2");
				CalculateAvgTotal("cmplx_FinacleCore_cmplx_avgbalance_new_FEB2");
				CalculateAvgTotal("cmplx_FinacleCore_cmplx_avgbalance_new_MAR2");
				CalculateAvgTotal("cmplx_FinacleCore_cmplx_avgbalance_new_APR2");
				CalculateAvgTotal("cmplx_FinacleCore_cmplx_avgbalance_new_MAY2");
				CalculateAvgTotal("cmplx_FinacleCore_cmplx_avgbalance_new_JUN2");
				CalculateAvgTotal("cmplx_FinacleCore_cmplx_avgbalance_new_JUL2");
				CalculateAvgTotal("cmplx_FinacleCore_cmplx_avgbalance_new_AUG2");
				CalculateAvgTotal("cmplx_FinacleCore_cmplx_avgbalance_new_SEP2");
				CalculateAvgTotal("cmplx_FinacleCore_cmplx_avgbalance_new_OCT2");
				CalculateAvgTotal("cmplx_FinacleCore_cmplx_avgbalance_new_NOV2");
				CalculateAvgTotal("cmplx_FinacleCore_cmplx_avgbalance_new_DEC2");
				CalculateAvgTotalthree();
				//CalculateAvgTotalsix();
			} 
			catch (Exception e) {
				CreditCard.mLogger.info( e.toString());
			}
		}


		else if("cmplx_DEC_ContactPointVeri".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			CreditCard.mLogger.info("CC val cmplx_DEC_ContactPointVeri "+ "Value of cmplx_DEC_ContactPointVeri is:"+formObject.getNGValue("cmplx_DEC_ContactPointVeri"));
			if("".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_ContactPointVeri")) || "null".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_ContactPointVeri")) || 	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_false").equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_ContactPointVeri")) )
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
		/*else if ("FinacleCore_CalculateTotal".equalsIgnoreCase(pEvent.getSource().getName())){
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
		}*/
	/*	else if ("FinacleCore_CalculateTurnover".equalsIgnoreCase(pEvent.getSource().getName())){
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


			CalculateRepeater(,formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));	
								CalculateRepeater(,formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));	
								CalculateRepeater(,formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));	
								CalculateRepeater(,formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));	
								CalculateRepeater(,formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));	
								CalculateRepeater(,formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));	
								CalculateRepeater(,formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));	
								CalculateRepeater(,formObject.getNGValue("cmplx_FinacleCore_cmplx_AvgBalanceNBC_BS_ANALYSED"));	

		}*/

		else if("Reference_Details_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Reference_Details");
		}


		else if ("CardDetails_Button2".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setNGValue("CardDetailsGR_WiName",formObject.getWFWorkitemName());
			CreditCard.mLogger.info( "Inside add button: add of card details");
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_CardDetails_cmpmx_gr_cardDetails");
		}
		else if ("CardDetails_Button3".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_CardDetails_cmpmx_gr_cardDetails");
		}

		else if ("CardDetails_Button4".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_CardDetails_cmpmx_gr_cardDetails");
		}
		//Below code added by Prabhakar for CRN and Kalyan 
		else if ("CardDetails_Button1".equalsIgnoreCase(pEvent.getSource().getName()))
        {
          List<String> objInput = new ArrayList();
          List<Object> objOutput = new ArrayList();
          CreditCard.mLogger.info("Generate CRN Button");
          objInput.add("Text:" + formObject.getWFWorkitemName());
          objOutput.add("Text");
          CreditCard.mLogger.info("objInput args is: " + formObject.getWFWorkitemName());
          objOutput = formObject.getDataFromStoredProcedure("generateCRNANDECRN", objInput, objOutput);
          String ECRNandCRN = (String)objOutput.get(0);
          formObject.setNGValue("CardDetails_ECRN", ECRNandCRN.substring(0, 9));
          formObject.setNGValue("CardDetails_CRN", ECRNandCRN.substring(10));
          formObject.setEnabled("CardDetails_Button1",false);
          CreditCard.mLogger.info("ECRN IS: " + ((String)objOutput.get(0)).substring(0, 9) + " CRN Is: " + ((String)objOutput.get(0)).substring(10));
        }
		else if ("CardDetails_Button5".equalsIgnoreCase(pEvent.getSource().getName()))
        {
          List<String> objInput = new ArrayList();
          List<Object> objOutput = new ArrayList();
          CreditCard.mLogger.info("Generate KCRN Button");
          objInput.add("Text:" + formObject.getWFWorkitemName());
		  objInput.add("Text:" + formObject.getNGValue("CardDetails_CRN"));
          objOutput.add("Text");
          CreditCard.mLogger.info("objInput args are: " + formObject.getWFWorkitemName()+" "+formObject.getNGValue("CardDetails_CRN"));
          objOutput = formObject.getDataFromStoredProcedure("Ng_KalyanReferencceNumber", objInput, objOutput);
          String KRN = (String)objOutput.get(0);
          formObject.setNGValue("CardDetails_KRN", KRN);
          formObject.setEnabled("CardDetails_Button5",false);
          CreditCard.mLogger.info("KRN IS: " + ((String)objOutput.get(0)));
        }
		//Above code added by Prabhakar for CRN and kalyan
		/*//Below code added by Prabhakar for CRN 
		else if ("CardDetails_Button1".equalsIgnoreCase(pEvent.getSource().getName()))
        {
          List<String> objInput = new ArrayList<String>();
          List<Object> objOutput = new ArrayList<Object>();
          CreditCard.mLogger.info("Generate CRN Button");
          objInput.add("Text:" + formObject.getWFWorkitemName());
          objOutput.add("Text");
          CreditCard.mLogger.info("objInput args is: " + formObject.getWFWorkitemName());
          objOutput = formObject.getDataFromStoredProcedure("generateCRNANDECRN", objInput, objOutput);
          String ECRNandCRN = (String)objOutput.get(0);
          formObject.setNGValue("CardDetails_ECRN", ECRNandCRN.substring(0, 9));
          formObject.setNGValue("CardDetails_CRN", ECRNandCRN.substring(10));
          CreditCard.mLogger.info("ECRN IS: " + ((String)objOutput.get(0)).substring(0, 9) + " CRN Is: " + ((String)objOutput.get(0)).substring(10));
        }
		//Above code added by Prabhakar for CRN 
*/		else if ("CardDetails_add".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setNGValue("CardDetailsGR_WiName",formObject.getWFWorkitemName());
			CreditCard.mLogger.info(formObject.getNGValue("CardDetailsGR_WiName"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_CardDetails_cmplx_CardCRNDetails");
			CreditCard.mLogger.info("after adding in the grid");

		}
		else if ("CardDetails_modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_CardDetails_cmplx_CardCRNDetails");
			CreditCard.mLogger.info( "Inside add button22: modify of card details");
		}
		else if ("CardDetails_delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_CardDetails_cmplx_CardCRNDetails");
			CreditCard.mLogger.info( "Inside add button33: delete of card details");

		}

		// added by abhishek on 17 august 2017
		else if ("FATCA_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
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


		else if ("FATCA_Button2".equalsIgnoreCase(pEvent.getSource().getName())){
			CreditCard.mLogger.info( "Inside FATCA_Button2 ");
			formObject.addItem("cmplx_FATCA_listedreason", formObject.getNGItemText("cmplx_FATCA_selectedreason", formObject.getSelectedIndex("cmplx_FATCA_selectedreason")));
			try {
				formObject.removeItem("cmplx_FATCA_selectedreason", formObject.getSelectedIndex("cmplx_FATCA_selectedreason"));
				formObject.setSelectedIndex("cmplx_FATCA_selectedreason", -1);
			} catch (Exception e) {
				CreditCard.logException(e);
			}
		}

		/*else if("FATCA_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("FATCA");


			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL036");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}*/
/*
		else if("KYC_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			CreditCard.mLogger.info( "Inside KYC save button");
			formObject.saveFragment("KYC");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL041");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}*/

		/*else if("OECD_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("OECD");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL045");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}
*/
		else if ("OECD_add".equalsIgnoreCase(pEvent.getSource().getName())){					
			formObject.setNGValue("OECD_winame",formObject.getWFWorkitemName());
			CreditCard.mLogger.info( "Inside add button: "+formObject.getNGValue("OECD_winame"));
			//below code by saurabh on 22md july 17.
			formObject.setEnabled("cmplx_OECD_cmplx_GR_OecdDetails_TinReason",true);
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_OECD_cmplx_GR_OecdDetails");
		}


		else if ("OECD_modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_OECD_cmplx_GR_OecdDetails");
		}


		else if ("OECD_delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_OECD_cmplx_GR_OecdDetails");
		}
		else if("IncomeDetails_Add".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_IncomeDetails_Cmplx_gr_IncomdeDetails");
		}
		else if("IncomeDetails_Modify".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_IncomeDetails_Cmplx_gr_IncomdeDetails");
		}
		else if("IncomeDetails_Delete".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_IncomeDetails_Cmplx_gr_IncomdeDetails");
		}
		
		else if ("BT_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_CC_Loan_cmplx_btc");
		}
		else if ("BT_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_CC_Loan_cmplx_btc");
		}
		else if ("WorldCheck1_Add".equalsIgnoreCase(pEvent.getSource().getName())){

			//++Below code added by nikhil 22/11/2017 
			
			formObject.setNGValue("WorldCheck1_WiName",formObject.getWFWorkitemName());
			formObject.setNGValue("Is_WorldCheckAdd","Y");
			//--Above code added by nikhil 22/11/2017 
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_WorldCheck_WorldCheck_Grid");
		}
		else if ("WorldCheck1_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_WorldCheck_WorldCheck_Grid");
		}
		else if ("WorldCheck1_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_WorldCheck_WorldCheck_Grid");
		}
		else if ("WorldCheck1_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("WorldCheck1_Frame1");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL079");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		else if("DecisionHistory_Button2".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			getCustAddress_details();
			hm.put("Liability_New_Button1","Clicked");
			//added
			String NTB_flag = formObject.getNGValue("cmplx_Customer_NTB");
			String NEP_flag = formObject.getNGValue("cmplx_Customer_NEP");
			String CIF_no = formObject.getNGValue("cmplx_Customer_CIFNo");
			CreditCard.mLogger.info( "inside create Account/Customer NTB value: "+NTB_flag );
			if("true".equalsIgnoreCase(NTB_flag) || "true".equalsIgnoreCase(NEP_flag)||"".equalsIgnoreCase(CIF_no)){
				formObject.setNGValue("curr_user_name",formObject.getUserName());
				int prd_count=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
				//Changes Done for Create Customer self Employed Case

				String Emp_type = NGFUserResourceMgr_CreditCard.getGlobalVar("CC_SAL");
				if(prd_count>0 && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6)!=null &&( "Self Employed".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6))||"SE".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6))) ){

					Emp_type = "SE";

				}
				outputResponse = genX.GenerateXML("NEW_CUSTOMER_REQ",Emp_type);
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				CreditCard.mLogger.info(ReturnCode);
				if("0000".equalsIgnoreCase(ReturnCode)){
					valueSetCustomer(outputResponse,"");   
					formObject.setNGValue("cmplx_DEC_New_CIFID", formObject.getNGValue("cmplx_Customer_CIFNO"));
					//change by saurabh for diabling button once call is successfull
					
					formObject.setEnabled("DecisionHistory_Button2",false);
					CreditCard.mLogger.info("Inside if of New customer Req");
					formObject.RaiseEvent("WFSave");

					//outputResponse = genX.GenerateXML("NEW_ACCOUNT_REQ","");
					// ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					CreditCard.mLogger.info(ReturnCode);

					// if("0000".equalsIgnoreCase(ReturnCode) ){
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
					alert_msg = NGFUserResourceMgr_CreditCard.getAlert("VAL011");


				}
				else{
					CreditCard.mLogger.info("Inside else of New Customer Req");
					alert_msg = NGFUserResourceMgr_CreditCard.getAlert("VAL010");
				}
			}


			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		//added by yash
		else if("CC_Creation_Card_Services".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.RaiseEvent("WFSave");
			CreditCard.mLogger.info("inside button click");
			//change by saurabh on 15th Dec
			String Product_Name=formObject.getNGValue("CC_Creation_Product");
			CreditCard.mLogger.info(""+Product_Name);
			if("Limit Change Request".equalsIgnoreCase(Product_Name)){
				outputResponse = genX.GenerateXML("CARD_SERVICES_REQUEST","Limit Change Request");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				CreditCard.mLogger.info(ReturnCode);
				ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
				CreditCard.mLogger.info(ReturnDesc);
				if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode) ){
					CreditCard.mLogger.info("value of Customer_Details inside inquiry code");
					formObject.setNGValue("Is_CardServices","Y");
					CreditCard.mLogger.info(""+formObject.getNGValue("Is_CardServices"));
					CreditCard.mLogger.info("CARD SERVICES RUNNING Limit Change Request");
					outputResponse = genX.GenerateXML("CARD_NOTIFICATION","");
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					CreditCard.mLogger.info(ReturnCode);
					ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
					CreditCard.mLogger.info(ReturnDesc);
					if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode)){
						CreditCard.mLogger.info("value of Customer_Details inside lock code");
						formObject.setNGValue("Is_CardNotifiaction","Y");
						CreditCard.mLogger.info(""+formObject.getNGValue("Is_CardNotifiaction"));
					}
				}
			}
			//for another product
			if("Financial Services Request".equalsIgnoreCase(Product_Name)){
				outputResponse = genX.GenerateXML("CARD_SERVICES_REQUEST","Financial Services Request");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				CreditCard.mLogger.info(ReturnCode);
				ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
				CreditCard.mLogger.info(ReturnDesc);
				if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode)){
					CreditCard.mLogger.info("value of Customer_Details inside inquiry code");
					formObject.setNGValue("Is_CardServices","Y"); 
					CreditCard.mLogger.info(""+formObject.getNGValue("Is_CardServices"));
					CreditCard.mLogger.info("CARD SERVICES RUNNING Financial Services Request");
					outputResponse = genX.GenerateXML("CARD_NOTIFICATION","");
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					CreditCard.mLogger.info(ReturnCode);
					ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
					CreditCard.mLogger.info(ReturnDesc);
					if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode) ){
						CreditCard.mLogger.info("value of Customer_Details inside lock code");
						formObject.setNGValue("Is_CardNotifiaction","Y");
						CreditCard.mLogger.info(""+formObject.getNGValue("Is_CardNotifiaction"));
					}
				}
			}
			//for another product
			if("Product Upgrade Request".equalsIgnoreCase(Product_Name)){
				outputResponse = genX.GenerateXML("CARD_SERVICES_REQUEST","Product Upgrade Request");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				CreditCard.mLogger.info(ReturnCode);
				ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
				CreditCard.mLogger.info(ReturnDesc);
				if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode) ){
					CreditCard.mLogger.info("value of Customer_Details inside inquiry code");
					formObject.setNGValue("Is_CardServices","Y"); 
					CreditCard.mLogger.info(""+formObject.getNGValue("Is_CardServices"));
					CreditCard.mLogger.info("CARD SERVICES RUNNING Product Upgrade Request");
					outputResponse = genX.GenerateXML("CARD_NOTIFICATION","");
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					CreditCard.mLogger.info(ReturnCode);
					ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
					CreditCard.mLogger.info(ReturnDesc);
					if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode)){
						CreditCard.mLogger.info("value of Customer_Details inside lock code");
						formObject.setNGValue("Is_CardNotifiaction","Y");
						CreditCard.mLogger.info(""+formObject.getNGValue("Is_CardNotifiaction"));
					}
				}
			}
			formObject.RaiseEvent("WFSave");
		}
		// ended by yash

		else if("Loan_Disbursal_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Loan_Disbursal");
		}
		else if("CC_Creation_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("CC_Creation");
		}
		else if("Limit_Inc_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Limit_Inc");
		}
		
		else if("CC_Creation_Button2".equalsIgnoreCase(pEvent.getSource().getName())){
			CreditCard.mLogger.info("RLOS value of ReturnCode"+"inside button click");
			CreditCard.mLogger.info(""+"Card Creation Call triggered");
			outputResponse = genX.GenerateXML("NEW_CREDITCARD_REQ","");
			ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			CreditCard.mLogger.info("RLOS value of ReturnCode"+ReturnCode);
			if(NGFUserResourceMgr_CreditCard.getGlobalVar("CC_integrationsuccess").equalsIgnoreCase(ReturnCode)){
				CreditCard.mLogger.info("PLCommoncode card creation successfull");
				
				formObject.RaiseEvent("WFSave");
				throw new ValidatorException(new FacesMessage("Card Creation Successfull!!"));
			}
			else{
				throw new ValidatorException(new FacesMessage("Card Creation Failed!!"));
			}
		}

		else if("CC_Creation_Button2".equalsIgnoreCase(pEvent.getSource().getName()) || "Limit_Inc_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.RaiseEvent("WFSave");
			CreditCard.mLogger.info("inside button click");
			String Product_Name;
			if("CC_Creation_Button2".equalsIgnoreCase(pEvent.getSource().getName())){
			Product_Name=formObject.getNGValue("cmplx_CCCreation_pdt");
			}
			else{
				Product_Name=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2);
				if(Product_Name.equalsIgnoreCase("LI")){
					Product_Name = "Limit Change Request";
				}
				else if(Product_Name.equalsIgnoreCase("PU")){
					Product_Name = "Product Upgrade Request";
				}
				else if(Product_Name.equalsIgnoreCase("PULI")){
					Product_Name = "PULI Request";
				}
			}
			CreditCard.mLogger.info(""+Product_Name);
			if("Limit Change Request".equalsIgnoreCase(Product_Name)){
				outputResponse = genX.GenerateXML("CARD_SERVICES_REQUEST","Limit Change Request");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				CreditCard.mLogger.info(ReturnCode);
				ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
				CreditCard.mLogger.info(ReturnDesc);
				if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode) ){
					CreditCard.mLogger.info("value of Customer_Details inside inquiry code");
					formObject.setNGValue("Is_CardServices","Y");
					CreditCard.mLogger.info(""+formObject.getNGValue("Is_CardServices"));
					CreditCard.mLogger.info("CARD SERVICES RUNNING Limit Change Request");
					outputResponse = genX.GenerateXML("CARD_NOTIFICATION","");
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					CreditCard.mLogger.info(ReturnCode);
					ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
					CreditCard.mLogger.info(ReturnDesc);
					if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode)){
						CreditCard.mLogger.info("value of Customer_Details inside lock code");
						formObject.setNGValue("Is_CardNotifiaction","Y");
						CreditCard.mLogger.info(""+formObject.getNGValue("Is_CardNotifiaction"));
					}
				}
			}
			//change by saurabh on 31st Dec
			if("PULI Request".equalsIgnoreCase(Product_Name)){
				outputResponse = genX.GenerateXML("CARD_SERVICES_REQUEST","Limit Change Request");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				CreditCard.mLogger.info(ReturnCode);
				ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
				CreditCard.mLogger.info(ReturnDesc);
				if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode) ){
					outputResponse = genX.GenerateXML("CARD_SERVICES_REQUEST","Product Upgrade Request");
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					CreditCard.mLogger.info(ReturnCode);
					ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
					CreditCard.mLogger.info(ReturnDesc);
					if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode) ){
					CreditCard.mLogger.info("value of Customer_Details inside inquiry code");
					formObject.setNGValue("Is_CardServices","Y");
					CreditCard.mLogger.info(""+formObject.getNGValue("Is_CardServices"));
					CreditCard.mLogger.info("CARD SERVICES RUNNING Limit Change Request");
					outputResponse = genX.GenerateXML("CARD_NOTIFICATION","");
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					CreditCard.mLogger.info(ReturnCode);
					ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
					CreditCard.mLogger.info(ReturnDesc);
					if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode)){
						CreditCard.mLogger.info("value of Customer_Details inside lock code");
						formObject.setNGValue("Is_CardNotifiaction","Y");
						CreditCard.mLogger.info(""+formObject.getNGValue("Is_CardNotifiaction"));
					}
				}
			}
			}
			//for another product
			if("Financial Services Request".equalsIgnoreCase(Product_Name)){
				outputResponse = genX.GenerateXML("CARD_SERVICES_REQUEST","Financial Services Request");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				CreditCard.mLogger.info(ReturnCode);
				ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
				CreditCard.mLogger.info(ReturnDesc);
				if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode)){
					CreditCard.mLogger.info("value of Customer_Details inside inquiry code");
					formObject.setNGValue("Is_CardServices","Y"); 
					CreditCard.mLogger.info(""+formObject.getNGValue("Is_CardServices"));
					CreditCard.mLogger.info("CARD SERVICES RUNNING Financial Services Request");
					outputResponse = genX.GenerateXML("CARD_NOTIFICATION","");
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					CreditCard.mLogger.info(ReturnCode);
					ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
					CreditCard.mLogger.info(ReturnDesc);
					if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode) ){
						CreditCard.mLogger.info("value of Customer_Details inside lock code");
						formObject.setNGValue("Is_CardNotifiaction","Y");
						CreditCard.mLogger.info(""+formObject.getNGValue("Is_CardNotifiaction"));
					}
				}
			}
			//for another product
			if("Product Upgrade Request".equalsIgnoreCase(Product_Name)){
				outputResponse = genX.GenerateXML("CARD_SERVICES_REQUEST","Product Upgrade Request");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				CreditCard.mLogger.info(ReturnCode);
				ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
				CreditCard.mLogger.info(ReturnDesc);
				if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode) ){
					CreditCard.mLogger.info("value of Customer_Details inside inquiry code");
					formObject.setNGValue("Is_CardServices","Y"); 
					CreditCard.mLogger.info(""+formObject.getNGValue("Is_CardServices"));
					CreditCard.mLogger.info("CARD SERVICES RUNNING Product Upgrade Request");
					outputResponse = genX.GenerateXML("CARD_NOTIFICATION","");
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					CreditCard.mLogger.info(ReturnCode);
					ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
					CreditCard.mLogger.info(ReturnDesc);
					if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode)){
						CreditCard.mLogger.info("value of Customer_Details inside lock code");
						formObject.setNGValue("Is_CardNotifiaction","Y");
						CreditCard.mLogger.info(""+formObject.getNGValue("Is_CardNotifiaction"));
					}
				}
			}
			formObject.RaiseEvent("WFSave");
		}

		/*else  if("CC_Creation_Update_Customer".equalsIgnoreCase(pEvent.getSource().getName())){
			CreditCard.mLogger.info("inside Update_Customer");
			//change from inquire op name to CIF_ENQUIRY by saurabh on 10th Dec
			//CHANGE from cif_enquiry of customer details to cif_verify of customer_update by saurabh on 8th Feb
			outputResponse = genX.GenerateXML("CUSTOMER_UPDATE_REQ","CIF_verify");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			CreditCard.mLogger.info(ReturnCode);
			ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
			CreditCard.mLogger.info(ReturnDesc);
			if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode)){
				CreditCard.mLogger.info("value of Customer_Details inside inquiry code");
				formObject.setNGValue("Is_CustInquiry_Disbursal","Y"); 
				CreditCard.mLogger.info("Inquiry Flag Value"+formObject.getNGValue("Is_CustInquiry_Disbursal")); 
				CreditCard.mLogger.info("inside Update_Customer");  
				/*String cif_status = (outputResponse.contains("<CustomerStatus>")) ? outputResponse.substring(outputResponse.indexOf("<CustomerStatus>")+"</CustomerStatus>".length()-1,outputResponse.indexOf("</CustomerStatus>")):"";
				if("ACTVE".equalsIgnoreCase(cif_status)){
					//change from Lock op name to CIF_LOCK by saurabh on 10th Dec
					outputResponse = genX.GenerateXML("CUSTOMER_DETAILS","CIF_LOCK");
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					CreditCard.mLogger.info(ReturnCode);
					ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
					CreditCard.mLogger.info(ReturnDesc);
					if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode)){
						CreditCard.mLogger.info("value of Customer_Details inside lock code");
						formObject.setNGValue("Is_CustLock_Disbursal","Y");
						CreditCard.mLogger.info("Inquiry Flag Is_CustLock_Disbursal value"+formObject.getNGValue("Is_CustLock_Disbursal")); 

						CreditCard.mLogger.info("Customer_Details is generated");
						//    CreditCard.mLogger.info(formObject.getNGValue("Is_ACCOUNT_MAINTENANCE_REQ"));
						//change from UnLock op name to CIF_UNLOCK by saurabh on 10th Dec
						outputResponse = genX.GenerateXML("CUSTOMER_DETAILS","CIF_UNLOCK");
						ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
						CreditCard.mLogger.info("inside unlock");
						CreditCard.mLogger.info(ReturnCode);
						formObject.setNGValue("Is_CustUnLock_Disbursal","Y");
						CreditCard.mLogger.info("Inquiry Flag Is_CustUnLock_Disbursal value"+formObject.getNGValue("Is_CustUnLock_Disbursal"));
						ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
						CreditCard.mLogger.info(ReturnDesc);
						if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode)){
							//changed from blank to CIF_update by saurabh on 10th dec
							outputResponse = genX.GenerateXML("CUSTOMER_UPDATE_REQ","CIF_update");
							ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
							CreditCard.mLogger.info(ReturnCode);
							ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
							CreditCard.mLogger.info(ReturnDesc);
							//  Message =(outputResponse.contains("<Message>")) ? outputResponse.substring(outputResponse.indexOf("<Message>")+"</Message>".length()-1,outputResponse.indexOf("</Message>")):"";    
							//  CreditCard.mLogger.info(Message);

							if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode)){
								formObject.setNGValue("Is_CUSTOMER_UPDATE_REQ_Disbursal","Y");
								CreditCard.mLogger.info("value of CUSTOMER_UPDATE_REQ"+formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
								int selectedRow = formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid");
								//code by saurabh on 17th Dec
								formObject.setNGValue("cmplx_CCCreation_cmplx_CCCreationGrid", selectedRow,10,"Y");
								formObject.setEnabled("CC_Creation_Button2",true);
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
							if(("Y".equalsIgnoreCase(formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ_Disbursal"))))
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
				/*}
				else {
					CreditCard.mLogger.info( "Error in Cif Enquiry operation CIF Staus is: "+ cif_status);
					throw new ValidatorException(new  CustomExceptionHandler("Customer Is InActive!!","FetchDetails#Customer Is InActive!!","",hm));
				}
				//added one more here
			}
			else{
				CreditCard.mLogger.info("Customer_Details Inquiry is not generated");
			}
		}*/

		else if ("BussinessVerification1_Button1".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			CreditCard.mLogger.info( "Business Verification Details are saved"); 
			formObject.saveFragment("Business_Verification1");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL005");
			CreditCard.mLogger.info( "Business Verification Details are saved123"); 
			throw new ValidatorException(new FacesMessage(alert_msg));

		}
		else if ("EmploymentVerification_Button1".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			CreditCard.mLogger.info( "Employment Verification Details are saved"); 
			formObject.saveFragment("Employment_Verification");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL026");
			CreditCard.mLogger.info( "Employment Verification Details are saved123"); 
			throw new ValidatorException(new FacesMessage(alert_msg));

		}
//below code modified by nikhil 4/12/17
		else if("BankingCheck_save".equalsIgnoreCase(pEvent.getSource().getName())){

			CreditCard.mLogger.info( "Banking Check Details are saved"); 
			formObject.saveFragment("Banking_Check");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL002");
			CreditCard.mLogger.info( "Banking Check Details are saved123"); 
			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		else if("supervisorsection_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("supervisor_section");
		}
		//Below else if button functionality changed by nikhil  5/12/17 for serach of cust detail verification
		else if ("CustDetailVerification1_Button2".equalsIgnoreCase(pEvent.getSource().getName())) {
			//FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			//below code added by nikhil
			formObject.clear("CustDetailVerification1_ListView1");
			String CompName="",CompCif="";
			try
			{
				if("Self Employed".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6)) && formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",0,0)!=null ){
					
				int row_count=formObject.getLVWRowCount("cmplx_CompanyDetails_cmplx_CompanyGrid");
				for(int i=0;i<row_count;i++)
				{
					if(formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",0,2).equals("Secondary") )
					{
						CompName=formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 4);
						CompCif=formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 3);
						break;
					}
				}
				}
			}
			catch(Exception ex){
				CreditCard.mLogger.info(ex.getMessage());
			}
			
			
			
			String EmiratesID=formObject.getNGValue("cmplx_CustDetailverification1_EmiratesId");
			String CIFID=formObject.getNGValue("cmplx_CustDetailverification1_CID_ID");
			//below code commented byn nikhil  3/1/18
/*			if("Self Employed".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6)) && formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",0,0)!=null ){
				CompName=formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",0,0);
				if(formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",0,0)!=null)
				{
				CompCif=formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",0,0);
				}
			}*/
			//below query changed by nikhil 3/1/18
			String query="SELECT DISTINCT isnull(dec.CustomerNAme,'NA'),dec.CIF_ID,dec.remarks,dec.userName,dec.Decision FROM NG_RLOS_GR_DECISION AS dec LEFT JOIN NG_RLOS_GR_CompanyDetails AS comp ON dec.dec_wi_name=comp.comp_winame WHERE ";
			if(!"".equalsIgnoreCase(EmiratesID.trim()))
				query+="dec.EmirateID='"+EmiratesID+"' OR ";
			if(!"".equalsIgnoreCase(CIFID.trim()))
				query+="dec.CIF_ID ='"+CIFID+"' OR";
			if(!"".equalsIgnoreCase(CompName))
				query+="comp.CompanyName='"+CompName+"' OR ";
			if(!"".equalsIgnoreCase(CompCif))
				query+="comp.CompanyCIF='"+CompCif+"'";
			else
				query=query.substring(0,query.length()-3);
					
			CreditCard.mLogger.info( "query is: of cust detail search "+query);
			List<List<String>> list=formObject.getNGDataFromDataCache(query);
			CreditCard.mLogger.info("##Arun");
			for (List<String> a : list) 
			{
				formObject.addItemFromList("CustDetailVerification1_ListView1", a);
			}
		}

		else if("HomeCountryVerification_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("HomeCountry_Verification");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL066");
			CreditCard.mLogger.info( "Customer Details Verification Details are saved123"); 
			throw new ValidatorException(new FacesMessage(alert_msg));

		}
		else if("CustDetailVerification_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Customer_Details_Verification");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL065");
			CreditCard.mLogger.info( "Customer Details Verification Details are saved123"); 
			throw new ValidatorException(new FacesMessage(alert_msg));

		}
		else if("BussinessVerification_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Business_Verification");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL067");
			CreditCard.mLogger.info( "Customer Details Verification Details are saved123"); 
			throw new ValidatorException(new FacesMessage(alert_msg));

		}
		else if("ResidenceVerification_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("ResidenceVerification");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL068");
			CreditCard.mLogger.info( "Customer Details Verification Details are saved123"); 
			throw new ValidatorException(new FacesMessage(alert_msg));

		}
		else if("GuarantorVerification_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Guarantor_Verification");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL069");
			CreditCard.mLogger.info( "Customer Details Verification Details are saved123"); 
			throw new ValidatorException(new FacesMessage(alert_msg));

		}
		else if("ReferenceDetailVerification_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Reference_Detail_Verification");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL070");
			CreditCard.mLogger.info( "Customer Details Verification Details are saved123"); 
			throw new ValidatorException(new FacesMessage(alert_msg));

		}
		else if("OfficeandMobileVerification_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Office_Mob_Verification");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL071");
			CreditCard.mLogger.info( "Customer Details Verification Details are saved123"); 
			throw new ValidatorException(new FacesMessage(alert_msg));

		}

		else if("LoanandCard_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("LoanCard_Details_Check");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL072");
			CreditCard.mLogger.info( "Customer Details Verification Details are saved123"); 
			throw new ValidatorException(new FacesMessage(alert_msg));

		}

		else if("OfficeandMobileVerification_Enable".equalsIgnoreCase(pEvent.getSource().getName())){
			String sQuery = "SELECT activityname FROM WFINSTRUMENTTABLE with (nolock) WHERE ProcessInstanceID = '"+formObject.getWFWorkitemName()+"'";
			List<List<String>> list=formObject.getNGDataFromDataCache(sQuery);
			CreditCard.mLogger.info( "Enable button click::query result is::"+list );
			for(int i =0;i<list.size();i++ ){
				if("Smart_CPV".equalsIgnoreCase(list.get(i).get(0))){
					formObject.setLocked("OfficeandMobileVerification_Frame1", true);
					alert_msg = NGFUserResourceMgr_CreditCard.getAlert("VAL006");
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

		else if ("Compliance_Button2".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			 CreditCard.mLogger.info( "inside Compliance_Button2 click before call::");
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Compliance_cmplx_gr_compliance");
			CreditCard.mLogger.info( "inside Compliance_Button2 click after  call::");
		}
		else if("Compliance_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Compliance");
		}
		else if("DecisionHistory_nonContactable".equalsIgnoreCase(pEvent.getSource().getName())){
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			Date date = new Date();
			String currDate = dateFormat.format(date);
			CreditCard.mLogger.info( "currDate::"+currDate);

			formObject.setEnabled("DecisionHistory_nonContactable", false);
			formObject.setEnabled("NotepadDetails_Frame1", false);
			formObject.setLocked("DecisionHistory_Frame1", true);
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
		else  if("DecisionHistory_cntctEstablished".equalsIgnoreCase(pEvent.getSource().getName())){
			 CreditCard.mLogger.info( "inside contact established click::");
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			Date date = new Date();
			String currDate = dateFormat.format(date);
			CreditCard.mLogger.info( "currDate::"+currDate);

			formObject.setEnabled("NotepadDetails_Frame1", true);
			formObject.setLocked("DecisionHistory_Frame1", false);
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
		// changed by saurabh on 8th Feb
		else  if("CC_Creation_Update_Customer".equalsIgnoreCase(pEvent.getSource().getName())){
			String message = CustomerUpdate();
			throw new ValidatorException(new FacesMessage(message));
		}
		else if("NotepadDetailsFCU_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			//++Below code added by  nikhil 10/10/17
			formObject.setNGValue("NotepadDetailsFCU_TellogWiname",formObject.getWFWorkitemName());
			//-- Above code added by  nikhil 10/10/17
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NoteFCU_cmplx_gr_TellogFCU");
		}
		else if("NotepadDetailsFCU_Button2".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_NoteFCU_cmplx_gr_TellogFCU");
		}

		else  if("NotepadDetailsFCU_Button4".equalsIgnoreCase(pEvent.getSource().getName())){

			//++Below code added by  nikhil 10/10/17
			formObject.setNGValue("NotepadDetailsFCU_NotepadWiname",formObject.getWFWorkitemName());
			//-- Above code added by  nikhil 10/10/17
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NoteFCU_cmplx_gr_NoteFCU");
		}
		else  if("NotepadDetailsFCU_Button6".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_NoteFCU_cmplx_gr_NoteFCU");
		}
		else if("NotepadDetailsFCU_Button5".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_NoteFCU_cmplx_gr_NoteFCU");
		}
		else if("NotepadDetailsFCU_Button3".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Notepad_Details1");
		}
		else  if ("NotepadDetails_Button7".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			Date dNow = new Date( );

			SimpleDateFormat ft = new SimpleDateFormat ("E  hh:mm:ss a zzz");
			formObject.setNGValue("NotepadDetails_Text5",ft.format(dNow));
		}
		//++Below code added by nikhil 13/11/2017 for Code merge
		
		else  if (pEvent.getSource().getName().equalsIgnoreCase("CustDetailVerification1_Button1")) {
			//loadPicklist_Address();
			formObject.saveFragment("Customer_Details_Verification1");
			//below code added by nikhil 4/12/17
			alert_msg="Customer Verification details saved";

			throw new ValidatorException(new FacesMessage(alert_msg));
			
		}
		else  if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Button3")){
			formObject.saveFragment("Notepad_Details");
		}
		//--Above code added by nikhil 13/11/2017 for Code merge
		
		
		//++ below code added by abhishek on 04/01/2018 for EFMS refresh functionality
		
		else if("IncomingDocument_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("IncomingDocument_Frame");
			
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL080");//Incoming Document details savedd
			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		else if("ELigibiltyAndProductInfo_Refresh".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			CreditCard.mLogger.info("Inside refresh button code::");
			String sQuery = "Select Application_Status from NG_EFMS_RESPONSE with (nolock) where Application_number = '"+ formObject.getWFWorkitemName() +"'";
			CreditCard.mLogger.info("EFMS sQuery::"+sQuery);
			List<List<String>> list = formObject.getNGDataFromDataCache(sQuery);
			CreditCard.mLogger.info("EFMS list::"+list);
			if(!list.isEmpty()){
				
				String Alertflag = list.get(0).get(0).toLowerCase();
				CreditCard.mLogger.info("EFMS Alertflag::"+Alertflag);
				
				// set ext table flag here
				
			
				
				if(!"".equals(Alertflag) && !"null".equalsIgnoreCase(Alertflag)){
					alert_msg = "Application status is "+Alertflag;
					formObject.setNGValue("EFMS_IS_Alerted", Alertflag);
					
				}else{
					alert_msg = "EFMS alert status not updated. Try again Later";
				}
				formObject.RaiseEvent("WFSave");
				
			}
			else{
				alert_msg = "EFMS alert status not updated. Try again Later";
			}
			throw new ValidatorException(new FacesMessage(alert_msg));
		}

//++ above code added by abhishek on 04/01/2018 for EFMS refresh functionality
		else if("DecisionHistory_ReferTo".equalsIgnoreCase(pEvent.getSource().getName())){
			CreditCard.mLogger.info("inside mouse clicked for refr casde :::");
			String refer=formObject.getNGItemText("DecisionHistory_ReferTo", formObject.getSelectedIndex("DecisionHistory_ReferTo"));
			CreditCard.mLogger.info("inside mouse clicked for refr casde :::"+refer);
				LoadPickList("DecisionHistory_dec_reason_code", "SELECT '--Select--' as description,'' as code union select description,code from ng_MASTER_DecisionReason where workstep='"+formObject.getWFActivityName()+"' and decision='"+formObject.getNGValue("cmplx_DEC_Decision")+"'and( ReferTo='"+refer+"'  or ReferTo='All') order by code");
			
		}
		
		else if("OriginalValidation_Save".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.saveFragment("Original_validation");
			alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL081");//Incoming Document details savedd
			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		

	}
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha             
	Description                         : get data from aloc       

	 ***********************************************************************************  */
	public void getDataFromALOC(FormReference formObject2, String corpName) {
		try{
			String query = "select INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,HIGH_DELINQUENCY_EMPLOYER,EMPLOYER_CATEGORY_PL_EXPAT from NG_RLOS_ALOC_OFFLINE_DATA with (nolock) where EMPR_NAME = '"+corpName+"'";
			List<List<String>> result = formObject2.getNGDataFromDataCache(query);
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
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha             
	Description                         : value changed event       

	 ***********************************************************************************  */
	

		public void value_changed(FormEvent pEvent)
		{
			String alert_msg="";
			FormReference formObject=FormContext.getCurrentInstance().getFormReference();
			String ReqProd="";
			Common_Utils common=new Common_Utils(CreditCard.mLogger);

			if ("cmplx_DEC_Decision".equalsIgnoreCase(pEvent.getSource().getName())) {
				CreditCard.mLogger.info("Inside Decision value change");
				//modified by akshay on 5/1/18
				if(formObject.getNGValue("cmplx_DEC_Decision").equalsIgnoreCase("REFER"))
		        {
					formObject.setLocked("DecisionHistory_ReferTo", false);
					//query modified by saurabh on 25th Jan
					LoadPickList("DecisionHistory_ReferTo", "select referTo from (SELECT '--Select--' as referTo union select referTo from ng_MASTER_Decision with (nolock) where WorkstepName='"+formObject.getWFActivityName()+"' and decision='"+formObject.getNGValue("cmplx_DEC_Decision")+"') as tempTable order by case when referTo='--Select--' then 0 else 1 end");
		        }
				
				if("REJECT".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")) || "REFER".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")))
				{
					formObject.setLocked("DecisionHistory_dec_reason_code", false);
					formObject.clear("DecisionHistory_dec_reason_code");
					LoadPickList("DecisionHistory_dec_reason_code", "SELECT '--Select--' as description,'' as code union select description,code from ng_MASTER_DecisionReason where workstep='"+formObject.getWFActivityName()+"' and decision='"+formObject.getNGValue("cmplx_DEC_Decision")+"' order by code");
					//formObject.setLocked("cmplx_DEC_ReferTo", false);--field not present in decision now--Akshay
				}
				
				
				else{
					formObject.setVisible("DecisionHistory_Button1", false);
					formObject.setLocked("DecisionHistory_ReferTo", true);
					formObject.setLocked("DecisionHistory_dec_reason_code", true);
					//formObject.setLocked("cmplx_DEC_ReferTo", true);

			        }
			}
			//changed by akshay on 5/12/17





		// Code added to Load targetSegmentCode on basis of code
		if ("ApplicationCategory".equalsIgnoreCase(pEvent.getSource().getName())){
			CreditCard.mLogger.info("RLOS val change "+ "Value of dob is:"+formObject.getNGValue("ApplicationCategory"));
			String subproduct = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2);
			String appCategory=formObject.getNGValue("ApplicationCategory");
			CreditCard.mLogger.info("RLOS val change "+ "Value of subproduct is:"+formObject.getNGValue("subproduct"));
			if(appCategory!=null &&  "BAU".equalsIgnoreCase(appCategory)){
				LoadPickList("TargetSegmentCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and BAU='Y' and (product = 'CC' or product='B') order by code");
			}
			else if(appCategory!=null &&  "S".equalsIgnoreCase(appCategory)){
				LoadPickList("TargetSegmentCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and Surrogate='Y' and (product = 'CC' or product='B') order by code");
			}
			else{
				LoadPickList("TargetSegmentCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and (product = 'CC' or product='B') order by code");	
			}
		}
		// Code added to Load targetSegmentCode on basis of code
		
		if ("cmplx_EmploymentDetails_ApplicationCateg".equalsIgnoreCase(pEvent.getSource().getName())){
			CreditCard.mLogger.info("RLOS val change "+ "Value of dob is:"+formObject.getNGValue("ApplicationCategory"));
			String subprod = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2);
			String appCategory = formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg");
			CreditCard.mLogger.info("RLOS val change "+ "Value of subproduct is:"+formObject.getNGValue("subproduct"));
			if(appCategory!=null &&  "BAU".equalsIgnoreCase(appCategory)){
				LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subprod+"' and BAU='Y' and (product = 'CC' or product='B') order by code");
			}
			else if(appCategory!=null &&  "S".equalsIgnoreCase(appCategory)){
				LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subprod+"' and Surrogate='Y' and (product = 'CC' or product='B') order by code");
			}
			else{
				LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subprod+"' and (product = 'CC' or product='B') order by code");	
			}
		}

		if ("ReqProd".equalsIgnoreCase(pEvent.getSource().getName())){
			ReqProd=formObject.getNGValue("ReqProd");
			CreditCard.mLogger.info("CC val change "+ "Value of ReqProd is:"+ReqProd);
			loadPicklistProduct(ReqProd);
		}

		if ("subProd".equalsIgnoreCase(pEvent.getSource().getName())){
			CreditCard.mLogger.info("CC val change "+ "Value of SubProd is:"+formObject.getNGValue("subProd"));
			//formObject.clear("AppType");
			if(	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_BusinessTitaniumCard").equalsIgnoreCase(formObject.getNGValue("subProd"))){
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='"+NGFUserResourceMgr_CreditCard.getGlobalVar("CC_BTC")+"'");
				formObject.setNGValue("EmpType","Self Employed");
			}	
			else if(NGFUserResourceMgr_CreditCard.getGlobalVar("CC_InstantMoney").equalsIgnoreCase(formObject.getNGValue("subProd"))){
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType  with (nolock) where subProdFlag='"+NGFUserResourceMgr_CreditCard.getGlobalVar("CC_IM")+"'");
				formObject.setNGValue("EmpType","Salaried");
			}

			else if(	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_LimitIncrease").equalsIgnoreCase(formObject.getNGValue("subProd")))
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='"+NGFUserResourceMgr_CreditCard.getGlobalVar("CC_LI")+"'");

			else if(NGFUserResourceMgr_CreditCard.getGlobalVar("CC_SalariedCreditCard").equalsIgnoreCase(formObject.getNGValue("subProd")))
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='"+NGFUserResourceMgr_CreditCard.getGlobalVar("CC_SAL")+"'");

			else if(NGFUserResourceMgr_CreditCard.getGlobalVar("CC_SelfEmployedCreditCard").equalsIgnoreCase(formObject.getNGValue("subProd")))
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='"+NGFUserResourceMgr_CreditCard.getGlobalVar("CC_SE")+"'");

			else if(	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_ExpatPersonalLoans").equalsIgnoreCase(formObject.getNGValue("subProd")))
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='"+NGFUserResourceMgr_CreditCard.getGlobalVar("CC_EXP")+"'");

			else if(	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_NationalPersonal").equalsIgnoreCase(formObject.getNGValue("subProd")))
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='"+NGFUserResourceMgr_CreditCard.getGlobalVar("CC_NAT")+"'");

			else if(	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_Pre-Approved").equalsIgnoreCase(formObject.getNGValue("subProd")))
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='"+NGFUserResourceMgr_CreditCard.getGlobalVar("CC_PA")+"'");

			else if(	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_ProductUpgrade").equalsIgnoreCase(formObject.getNGValue("subProd")))
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='"+NGFUserResourceMgr_CreditCard.getGlobalVar("CC_PU")+"'");
			formObject.setNGValue("AppType","--Select--");

		}
		if ("Product_type".equalsIgnoreCase(pEvent.getSource().getName())){

			String ProdType=formObject.getNGValue("Product_type");
			CreditCard.mLogger.info("CC Value Change Prod_Type"+ProdType);
			formObject.clear("CardProd");
			formObject.setNGValue("CardProd","--Select--");
			//Deepak Code change to load card product with new master.
			LoadPickList("CardProd","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cardProduct with (nolock) where ReqProduct = '"+ProdType+"' order by code");

			/*			if("Conventional".equalsIgnoreCase(ProdType))
				LoadPickList("CardProd", "select '--Select--' union select convert(varchar,description) from ng_MASTER_CardProduct with (nolock) where code NOT like '%AMAL%' ORDER BY description");

			if("Islamic".equalsIgnoreCase(ProdType))
				LoadPickList("CardProd", "select '--Select--' union select convert(varchar,description) from ng_MASTER_CardProduct with (nolock) where code like '%AMAL%' ORDER BY description");*/
		}

		if ("cmplx_WorldCheck_WorldCheck_Grid_dob".equalsIgnoreCase(pEvent.getSource().getName())){
			CreditCard.mLogger.info("RLOS val change "+ "Value of cmplx_WorldCheck_WorldCheck_Grid_dob is:"+formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid_dob"));
			getAgeWorldCheck(formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid_dob"));
		}

		if ("cmplx_Liability_New_overrideIntLiab".equalsIgnoreCase(pEvent.getSource().getName())){
			if(	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Liability_New_overrideIntLiab"))){

				formObject.setVisible("Liability_New_Overwrite", true);

			}
			else{
				formObject.setVisible("Liability_New_Overwrite", false);
			}
		}

		//code added for LOB
		if ("estbDate".equalsIgnoreCase(pEvent.getSource().getName())){
			//change by saurabh on 29th Nov 17 for Drop2 CR.
			CreditCard.mLogger.info( "Value of estbDate is:"+formObject.getNGValue("estbDate"));
			common.getAge(formObject.getNGValue("estbDate"),"lob");
		}
		//code added for LOB
		if ("indusSector".equalsIgnoreCase(pEvent.getSource().getName())){

			LoadPickList("indusMAcro", "select '--Select--' union select macro from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where IndustrySector='"+formObject.getNGValue("indusSector")+"' and IsActive='Y'");
		}

		if ("indusMAcro".equalsIgnoreCase(pEvent.getSource().getName())){
			LoadPickList("indusMicro", "select '--Select--' union select convert(varchar, micro) from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where Macro='"+formObject.getNGValue("indusMAcro")+"' and IsActive='Y'");
		}

		else if ("cmplx_Customer_DOb".equalsIgnoreCase(pEvent.getSource().getName())){
			CreditCard.mLogger.info( "Value of dob is:"+formObject.getNGValue("cmplx_Customer_DOb"));
			common.getAge(formObject.getNGValue("cmplx_Customer_DOb"),"cmplx_Customer_age");
		}
		else if ("cmplx_EmploymentDetails_DOJ".equalsIgnoreCase(pEvent.getSource().getName())){
			//SKLogger.writeLog("RLOS val change ", "Value of dob is:"+formObject.getNGValue("cmplx_EmploymentDetails_DOJ"));
			common.getAge(formObject.getNGValue("cmplx_EmploymentDetails_DOJ"),"cmplx_EmploymentDetails_LOS");
		}
		//changed by akshay on 8th nov 2017 for loadpicklist.
		else if ("cmplx_EmploymentDetails_EmpIndusSector".equalsIgnoreCase(pEvent.getSource().getName()) && "true".equals(formObject.getNGValue("cmplx_EmploymentDetails_Others"))){

			CreditCard.mLogger.info( "$Indus Sector$:" +formObject.getNGValue("cmplx_EmploymentDetails_EmpIndusSector"));
			formObject.setLocked("cmplx_EmploymentDetails_Indus_Macro", false);//it is unlocked from js but its instance state is saved as locked as it was locked on fragment load
			LoadPickList("cmplx_EmploymentDetails_Indus_Macro", "Select macro from (select '--Select--' as macro union select macro from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where IndustrySector='"+formObject.getNGValue("cmplx_EmploymentDetails_EmpIndusSector")+"' and IsActive='Y') new_table order by case  when macro ='--Select--' then 0 else 1 end");
		}

		else if ("cmplx_EmploymentDetails_Indus_Macro".equalsIgnoreCase(pEvent.getSource().getName()) && "true".equals(formObject.getNGValue("cmplx_EmploymentDetails_Others"))){
			CreditCard.mLogger.info( "$Indus Macro$:" +formObject.getNGValue("cmplx_EmploymentDetails_Indus_Macro"));
			formObject.setLocked("cmplx_EmploymentDetails_Indus_Micro", false);
			LoadPickList("cmplx_EmploymentDetails_Indus_Micro", "Select micro from (select '--Select--' as micro union select  micro from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where Macro='"+formObject.getNGValue("cmplx_EmploymentDetails_Indus_Macro")+"' and IndustrySector='"+formObject.getNGValue("cmplx_EmploymentDetails_EmpIndusSector")+"' and IsActive='Y' ) new_table order by case  when micro ='--Select--' then 0 else 1 end");
		}
		//added by yashfor CC FSD
		else if ("NotepadDetails_notedesc".equalsIgnoreCase(pEvent.getSource().getName())){
			String notepad_desc = formObject.getNGValue("NotepadDetails_notedesc");
			//LoadPickList("NotepadDetails_notecode", "select '--Select--' union select convert(varchar, description) from ng_master_notedescription with (nolock)  where Description=q'["+notepad_desc+"]'","NotepadDetails_notecode");
			String sQuery = "select code,workstep from ng_master_notedescription with (nolock) where Description='" +  notepad_desc + "'";
			CreditCard.mLogger.info(sQuery);
			List<List<String>> recordList = formObject.getNGDataFromDataCache(sQuery);
			//if(recordList.get(0).get(0)!= null && recordList.get(0)!=null && !recordList.get(0).get(0).equalsIgnoreCase("") && recordList!=null)
			if(recordList.get(0).get(0)!= null && !recordList.isEmpty())
			{ CreditCard.mLogger.info(recordList.get(0).get(0));
			formObject.setNGValue("NotepadDetails_notecode",recordList.get(0).get(0));
			CreditCard.mLogger.info(recordList.get(0).get(1));
			formObject.setNGValue("NotepadDetails_workstep",recordList.get(0).get(1),false);
			}
			//LoadPickList("NotepadDetails_notecode", "select '--Select--' union select convert(varchar, description) from ng_master_notedescription with (nolock)  where Description=q'["+notepad_desc+"]'","NotepadDetails_notecode");
			sQuery = "select code,workstep from ng_master_notedescription with (nolock) where Code='" +  notepad_desc + "'";
			CreditCard.mLogger.info(sQuery);
			recordList = formObject.getNGDataFromDataCache(sQuery);
			if(recordList!=null && recordList.get(0)!=null && recordList.get(0).get(0)!= null && !"".equalsIgnoreCase(recordList.get(0).get(0)) )
			{
				formObject.setNGValue("NotepadDetails_notecode",recordList.get(0).get(0));
				CreditCard.mLogger.info(recordList.get(0).get(1));
				formObject.setNGValue("NotepadDetails_workstep",recordList.get(0).get(1),false);
			}
			//	formObject.setNGValue("NotepadDetails_notecode",notepad_desc);
		}
		
		else if ("cmplx_FinacleCore_total_deduction_3month".equalsIgnoreCase(pEvent.getSource().getName())){
			float totalCredit = Float.parseFloat(formObject.getNGValue("cmplx_FinacleCore_total_credit_3month"));
			float deduction = Float.parseFloat(formObject.getNGValue("cmplx_FinacleCore_total_deduction_3month"));
			if(totalCredit>deduction){
				float avgCredit = (totalCredit - deduction)/3;

				formObject.setNGValue("cmplx_FinacleCore_avg_credit_3month",convertFloatToString(avgCredit));
			} 
			else{
				alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL023");
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
				alert_msg=NGFUserResourceMgr_CreditCard.getAlert("VAL023");
				throw new ValidatorException(new FacesMessage(alert_msg));
			}
		}
		/*else{
			CreditCard.mLogger.info(" ");
		}*/
		else if ("cmplx_DEC_Decision".equalsIgnoreCase(pEvent.getSource().getName())) {
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
		else if ("AppType".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			String subprod=formObject.getNGValue("SubProd");
			String apptype=formObject.getNGValue("AppType");
			String TypeofProduct=formObject.getNGValue("Product_type");
			CreditCard.mLogger.info( "Value of SubProd is:"+formObject.getNGValue("SubProd"));
			CreditCard.mLogger.info( "Value of AppType is:"+formObject.getNGValue("AppType"));
			CreditCard.mLogger.info( "Value of AppType is:"+formObject.getNGValue("Product_type"));


			if (("Conventional".equalsIgnoreCase(TypeofProduct))&& NGFUserResourceMgr_CreditCard.getGlobalVar("CC_EXP").equalsIgnoreCase(subprod)&&("TKOE".equalsIgnoreCase(apptype))){


				CreditCard.mLogger.info( "1Value of AppType is:"+formObject.getNGValue("AppType"));


				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Take Over'  and TypeofProduct='Conventional' order by SCHEMEID");




			}
			if (("Conventional".equalsIgnoreCase(TypeofProduct))&& NGFUserResourceMgr_CreditCard.getGlobalVar("CC_EXP").equalsIgnoreCase(subprod)&&("NEWE".equalsIgnoreCase(apptype))){
				CreditCard.mLogger.info( "2Value of AppType is:"+formObject.getNGValue("AppType"));




				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'New' and TypeofProduct='Conventional' order by SCHEMEID");




			}
			if (("Conventional".equalsIgnoreCase(TypeofProduct)&&NGFUserResourceMgr_CreditCard.getGlobalVar("CC_EXP").equalsIgnoreCase(subprod))&&("TOPE".equalsIgnoreCase(apptype))){
				CreditCard.mLogger.info( "3Value of AppType is:"+formObject.getNGValue("AppType"));


				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Top up' and TypeofProduct='Conventional' order by SCHEMEID");


			}
			if (("Conventional".equalsIgnoreCase(TypeofProduct)&&NGFUserResourceMgr_CreditCard.getGlobalVar("CC_EXP").equalsIgnoreCase(subprod))&&("RESCE".equalsIgnoreCase(apptype))){
				CreditCard.mLogger.info( "4Value of AppType is:"+formObject.getNGValue("AppType"));


				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Reschedulment' and TypeofProduct='Conventional' order by SCHEMEID");


			}
			if (("Conventional".equalsIgnoreCase(TypeofProduct)&& NGFUserResourceMgr_CreditCard.getGlobalVar("CC_NAT").equalsIgnoreCase(subprod))&&("TOPN".equalsIgnoreCase(apptype))){
				CreditCard.mLogger.info( "5Value of AppType is:"+formObject.getNGValue("AppType"));
				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Top up' and TypeofProduct='Conventional' order by SCHEMEID");


			}
			if (("Conventional".equalsIgnoreCase(TypeofProduct)&&NGFUserResourceMgr_CreditCard.getGlobalVar("CC_NAT").equalsIgnoreCase(subprod))&&("TKON".equalsIgnoreCase(apptype))){
				CreditCard.mLogger.info( "6Value of AppType is:"+formObject.getNGValue("AppType"));
				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Take Over' and TypeofProduct='Conventional' order by SCHEMEID");


			}
			if (("Conventional".equalsIgnoreCase(TypeofProduct)&&NGFUserResourceMgr_CreditCard.getGlobalVar("CC_NAT").equalsIgnoreCase(subprod))&&("NEWN".equalsIgnoreCase(apptype))){
				CreditCard.mLogger.info( "7Value of AppType is:"+formObject.getNGValue("AppType"));
				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'New' and TypeofProduct='Conventional' order by SCHEMEID");


			}
			if (("Conventional".equalsIgnoreCase(TypeofProduct)&&NGFUserResourceMgr_CreditCard.getGlobalVar("CC_NAT").equalsIgnoreCase(subprod))&&("RESCN".equalsIgnoreCase(apptype))){
				CreditCard.mLogger.info( "8Value of AppType is:"+formObject.getNGValue("AppType"));
				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Reschedulment' and TypeofProduct='Conventional' order by SCHEMEID");


			}
			if (("Islamic".equalsIgnoreCase(TypeofProduct))&& NGFUserResourceMgr_CreditCard.getGlobalVar("CC_EXP").equalsIgnoreCase(subprod)&&("TKOE".equalsIgnoreCase(apptype))){


				CreditCard.mLogger.info( "1Value of AppType is:"+formObject.getNGValue("AppType"));


				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Take Over'  and TypeofProduct='Islamic' order by SCHEMEID");				
			}

			if (("Islamic".equalsIgnoreCase(TypeofProduct))&& NGFUserResourceMgr_CreditCard.getGlobalVar("CC_EXP").equalsIgnoreCase(subprod)&&("NEWE".equalsIgnoreCase(apptype))){
				CreditCard.mLogger.info( "2Value of AppType is:"+formObject.getNGValue("AppType"));
				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'New' and TypeofProduct='Islamic' order by SCHEMEID");				
			}

			if (("Islamic".equalsIgnoreCase(TypeofProduct)&&NGFUserResourceMgr_CreditCard.getGlobalVar("CC_EXP").equalsIgnoreCase(subprod))&&("TOPE".equalsIgnoreCase(apptype))){
				CreditCard.mLogger.info( "3Value of AppType is:"+formObject.getNGValue("AppType"));


				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Top up' and TypeofProduct='Islamic' order by SCHEMEID");


			}
			if (("Islamic".equalsIgnoreCase(TypeofProduct)&&NGFUserResourceMgr_CreditCard.getGlobalVar("CC_EXP").equalsIgnoreCase(subprod))&&("RESCE".equalsIgnoreCase(apptype))){
				CreditCard.mLogger.info( "4Value of AppType is:"+formObject.getNGValue("AppType"));


				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Reschedulment' and TypeofProduct='Islamic' order by SCHEMEID");


			}
			if (("Islamic".equalsIgnoreCase(TypeofProduct)&& NGFUserResourceMgr_CreditCard.getGlobalVar("CC_NAT").equalsIgnoreCase(subprod))&&("TOPN".equalsIgnoreCase(apptype))){
				CreditCard.mLogger.info( "5Value of AppType is:"+formObject.getNGValue("AppType"));
				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Top up' and TypeofProduct='Islamic' order by SCHEMEID");


			}
			if (("Islamic".equalsIgnoreCase(TypeofProduct)&&NGFUserResourceMgr_CreditCard.getGlobalVar("CC_NAT").equalsIgnoreCase(subprod))&&("TKON".equalsIgnoreCase(apptype))){
				CreditCard.mLogger.info( "6Value of AppType is:"+formObject.getNGValue("AppType"));
				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Take Over' and TypeofProduct='Islamic' order by SCHEMEID");


			}
			if (("Islamic".equalsIgnoreCase(TypeofProduct)&&NGFUserResourceMgr_CreditCard.getGlobalVar("CC_NAT").equalsIgnoreCase(subprod))&&("NEWN".equalsIgnoreCase(apptype))){
				CreditCard.mLogger.info( "7Value of AppType is:"+formObject.getNGValue("AppType"));
				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'New' and TypeofProduct='Islamic' order by SCHEMEID");


			}
			if (("Islamic".equalsIgnoreCase(TypeofProduct)&&NGFUserResourceMgr_CreditCard.getGlobalVar("CC_NAT").equalsIgnoreCase(subprod))&&("RESCN".equalsIgnoreCase(apptype))){
				CreditCard.mLogger.info( "8Value of AppType is:"+formObject.getNGValue("AppType"));
				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Reschedulment' and TypeofProduct='Islamic' order by SCHEMEID");

			}
		}
		else if ("cmplx_DEC_FeedbackStatus".equalsIgnoreCase(pEvent.getSource().getName()) && "FCU".equalsIgnoreCase(formObject.getWFActivityName())){

			CreditCard.mLogger.info("inside feedback status");
			LoadpicklistFCU();
			CreditCard.mLogger.info("inside feedback status after loadpicklist");

		}
		else if("DecisionHistory_ReferTo".equalsIgnoreCase(pEvent.getSource().getName())){
			CreditCard.mLogger.info("inside mouse clicked for refr casde :::");
			String refer=formObject.getNGItemText("DecisionHistory_ReferTo", formObject.getSelectedIndex("DecisionHistory_ReferTo"));
			CreditCard.mLogger.info("inside mouse clicked for refr casde :::"+refer);
				LoadPickList("DecisionHistory_dec_reason_code", "SELECT '--Select--' as description,'' as code union select description,code from ng_MASTER_DecisionReason where workstep='"+formObject.getWFActivityName()+"' and decision='"+formObject.getNGValue("cmplx_DEC_Decision")+"'and( ReferTo='"+refer+"'  or ReferTo='All') order by code");
				formObject.setNGValue("ReferTo",refer);
				
			
		}

	}
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha             
	Description                         : fetch notepad details      

	 ***********************************************************************************  */
	public void fetch_NotepadDetails()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.fetchFragment("Notepad_Values", "NotepadDetails", "q_NotepadDetails");
		LoadPickList("NotepadDetails_notedesc", "select '--Select--'  union select  description from ng_master_notedescription with (nolock)");
		formObject.setNGValue("NotepadDetails_notedesc","--Select--");
		formObject.setLocked("NotepadDetails_notecode", true);
	}




	public void LockFragmentsOnLoad(ComponentEvent pEvent)
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		CreditCard.mLogger.info("Inside LockFragmentsOnLoad()");
		if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Customer_Frame1",true);
			loadPicklistCustomer();
		}	

		else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Product_Frame1",true);
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct with (nolock)");
			LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_ApplicationType with (nolock) order by code with (nolock)");
		}


		else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("IncomeDetails_Frame1",true);
			loadpicklist_Income();
		} 

		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
			  LoadPickList("ExtLiability_contractType", "select '--Select--' union select convert(varchar, description) from ng_master_contract_type with (nolock)");
			formObject.setLocked("ExtLiability_Frame1",true);
		}

		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("EMploymentDetails_Frame1",true);
			loadPicklistEmployment();
			loadPicklist4();
		}

		else if ("CompanyDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
            formObject.setLocked("CompanyDetails_Frame1", true);
            formObject.setLocked("CompanyDetails_Frame2", true);
            formObject.setLocked("CompanyDetails_Frame3", true);
            loadPicklist_CompanyDet();
         }
		
		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
		}

		else if ("LoanDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("LoanDetails_Frame1",true);
		}

		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist_Address();
			formObject.setLocked("AddressDetails_Frame1",true);
		}

		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadPickList("AlternateContactDetails_CustdomBranch", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock) order by code");
			//change by saurabh on 7th Dec
			LoadPickList("AlternateContactDetails_CardDisp", "select '--Select--' as description,'' as code union all select convert(varchar,description),code from NG_MASTER_Dispatch with (nolock) order by code ");// Load picklist added by aman to load the picklist in card dispatch to
			
			formObject.setLocked("AltContactDetails_Frame1",true);
		}

		else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("CardDetails_Frame1",true);
		}

		else if ("ReferenceDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadPickList("Reference_Details_ReferenceRelatiomship", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_Relationship with (nolock) order by code");

			formObject.setLocked("ReferenceDetails_Frame1",true);
		}

		else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("SupplementCardDetails_Frame1",true);
		}

		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist_Fatca();

			formObject.setLocked("FATCA_Frame6",true);
		}

		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("KYC_Frame1",true);
		}

		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist_oecd();

			formObject.setLocked("OECD_Frame8",true);
		}
		
		else if ("IncomingDocument".equalsIgnoreCase(pEvent.getSource().getName())){
			String Activityname=formObject.getWFActivityName();
			CreditCard.mLogger.info("Activityname  Activityname  Activityname  Activityname"+Activityname);
			
			if(!Activityname.equalsIgnoreCase("DDVT_Maker")){
			formObject.setLocked("IncomingDocument_Frame",true);
			}
			/*formObject.setEnabled("OECD_Save",true);*/
		}

		else if ("CC_Loan".equalsIgnoreCase(pEvent.getSource().getName()))
		{			      
			formObject.setLocked("CC_Loan_Frame1",true);
			loadPicklist_ServiceRequest();				   
		}
		
		
		else if ("PartMatch".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("PartMatch_Frame1",true);
		}

		else if ("FinacleCRMIncident".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("FinacleCRMIncident_Frame1",true);
		}

		else if ("FinacleCRMCustInfo".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("FinacleCRMCustInfo_Frame1",true);
		}

		else if ("FinacleCore".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("FinacleCore_Frame1",true);
		}

		else if ("MOL1".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("MOL1_Frame1",true);
		}

		else if ("WorldCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("WorldCheck1_Frame1",true);
		}

		else if ("RejectEnq".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("RejectEnq_Frame1",true);
		}
		
		else if ("SalaryEnq".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("SalaryEnq_Frame1",true);
		}

		else if ("CustDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("CustDetailVerification_Frame1",true);
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
		}

		else if ("LoanandCard".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("LoanandCard_Frame1",true);
		}
		
		else if ("Compliance".equalsIgnoreCase(pEvent.getSource().getName())) {	

			formObject.setLocked("Compliance_Frame1",true);
			formObject.setLocked("Compliance_CompRemarks",false);			            
			formObject.setLocked("Compliance_Modify",false); 
			formObject.setLocked("Compliance_Save",false);
			formObject.setLocked("cmplx_Compliance_ComplianceRemarks",false);



			formObject.fetchFragment("World_Check", "WorldCheck1", "q_WorldCheck");

			formObject.setNGFrameState("World_Check",0);
			int n=formObject.getLVWRowCount("cmplx_WorldCheck_WorldCheck_Grid");
			CreditCard.mLogger.info("CC value of world check row--->value of n "+n);
			if(n>0)
			{ 
				String UID = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,12);
				CreditCard.mLogger.info("CC value of world check UID"+UID);
				formObject.setNGValue("Compliance_UID",UID);
				String EI = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,15);
				CreditCard.mLogger.info("CC value of world check EI "+EI);
				formObject.setNGValue("Compliance_EI",EI);
				String Name = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,0);
				CreditCard.mLogger.info("CC value of world check Name"+Name);	
				formObject.setNGValue("Compliance_Name",Name);
				String Dob = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,2);
				CreditCard.mLogger.info("CC value of world check Dob"+Dob);
				formObject.setNGValue("Compliance_DOB",Dob);
				String Citizenship = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,7);
				CreditCard.mLogger.info("Citizenship"+Citizenship);	
				formObject.setNGValue("Compliance_Citizenship",Citizenship);
				String Remarks = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,16);
				formObject.setNGValue("Compliance_Remarks",Remarks);
				String Id_No = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,1);
				formObject.setNGValue("Compliance_IdentificationNumber",Id_No);
				String Age = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,3);
				formObject.setNGValue("Compliance_Age",Age);
				String Position = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,4);
				formObject.setNGValue("Compliance_Positon",Position);
				String Deceased = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,5); 
				formObject.setNGValue("Compliance_Deceased",Deceased);
				String Category = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,6);
				formObject.setNGValue("Compliance_Category",Category);
				String Location = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,8);
				formObject.setNGValue("Compliance_Location",Location);
				String Identification = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,9); 
				formObject.setNGValue("Compliance_Identification",Identification);
				String Biography = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,10); 
				formObject.setNGValue("Compliance_Biography",Biography);
				String Reports = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,11); 
				formObject.setNGValue("Compliance_Reports",Reports);
				String Entered_Date = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,13);
				formObject.setNGValue("Compliance_EntertedDate",Entered_Date);
				String Updated_Date = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,14);
				formObject.setNGValue("Compliance_UpdatedDate",Updated_Date);
				String Document = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,18);  
				formObject.setNGValue("Compliance_Document",Document);
				String Decision = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,19);
				formObject.setNGValue("Compliance_Decision",Decision);
				String Match_Rank = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,20);
				formObject.setNGValue("Compliance_MatchRank",Match_Rank);
				String Alias = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,21);
				formObject.setNGValue("Compliance_Alias",Alias);
				String birth_Country = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,22);
				formObject.setNGValue("Compliance_BirthCountry",birth_Country);
				String resident_Country = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,23);
				formObject.setNGValue("Compliance_ResidenceCountry",resident_Country);
				String Address = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,24);
				formObject.setNGValue("Compliance_Address",Address);
				String Gender = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,25);
				formObject.setNGValue("Compliance_Gender",Gender);
				String Listed_On = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,26);
				formObject.setNGValue("Compliance_ListedOn",Listed_On);
				String Program = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,27);
				formObject.setNGValue("Compliance_Program",Program);
				String external_ID = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,28);
				formObject.setNGValue("Compliance_ExternalID",external_ID);
				String data_Source = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,29);
				formObject.setNGValue("Compliance_DataSource",data_Source);
				formObject.setNGValue("Compliance_winame",formObject.getWFWorkitemName());
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Compliance_cmplx_gr_compliance");
				formObject.RaiseEvent("WFSave");
			}
			formObject.setNGFrameState("World_Check",1);
		}
		
		else if ("SmartCheck".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("SmartCheck_Frame1",true);
		}

		/*else if ("Compliance".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("Compliance_Frame1",true);
		}
*/
		else if ("FCU_Decision".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("FCU_Decision_Frame1",true);
		}
		
		// disha FSD
		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("NotepadDetails_Frame1",true);
			notepad_load();
			notepad_withoutTelLog();
		}
		
		if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			//changed by akshay on 6/12/17 for decision alignment
			//for decision fragment made changes 8th dec 2017
			String Activityname=formObject.getWFActivityName();
			 CreditCard.mLogger.info( "Exception:inside dsa review"+Activityname);
			
			if(Activityname.equalsIgnoreCase("DSA_CSO_Review")){
				 CreditCard.mLogger.info( "inside dsa review");
				fragment_ALign("cmplx_DEC_ContactPointVeri#\n#DecisionHistory_Decision_Label1,cmplx_DEC_VerificationRequired#DecisionHistory_Decision_Label3,cmplx_DEC_Decision#DecisionHistory_Label26,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_dec_reason_code#\n#DecisionHistory_Label5,cmplx_DEC_Description#DecisionHistory_Label3,cmplx_DEC_Strength#DecisionHistory_Label4,cmplx_DEC_Weakness#DecisionHistory_Decision_Label4,cmplx_DEC_Remarks#\n#DecisionHistory_Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line	
			}
			
			else{
		 	fragment_ALign("DecisionHistory_Decision_Label1,cmplx_DEC_VerificationRequired#DecisionHistory_Decision_Label3,cmplx_DEC_Decision#DecisionHistory_Label26,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_dec_reason_code#DecisionHistory_Decision_Label4,cmplx_DEC_Remarks#\n#DecisionHistory_Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
		 	//changed by akshay on 6/12/17 for decision alignment
			}
            loadPicklist3();
            formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
			formObject.setHeight("DecisionHistory",formObject.getHeight("DecisionHistory_Frame1")+30);
			
			//for decision fragment made changes 8th dec 2017
		}
		
		else if ("ReferHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ReferHistory_Frame1",true);
		}
	}
	
	

}