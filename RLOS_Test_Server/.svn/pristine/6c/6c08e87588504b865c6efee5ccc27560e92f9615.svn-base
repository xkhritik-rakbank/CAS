//----------------------------------------------------------------------------------------------------
//		NEWGEN SOFTWARE TECHNOLOGIES LIMITED
//Group						: AP2
//Product / Project			: RLOS
//Module					: CAS
//File Name					: RLOS_Query.java
//Author					: Deepak Kumar
//Date written (DD/MM/YYYY)	: 06/08/2017
//Description				: 
//----------------------------------------------------------------------------------------------------

package com.newgen.omniforms.user;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.IRepeater;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.excp.CustomExceptionHandler;
import com.newgen.omniforms.listener.FormListener;
import javax.faces.application.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import javax.faces.validator.ValidatorException;

@SuppressWarnings("serial")

public class RLOS_Query extends RLOSCommon implements FormListener
{
	HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
	boolean isSearchEmployer=false;
	String ReqProd=null;
	//RLOS.mLogger.info("Inside initiation RLOS");
	public void formLoaded(FormEvent pEvent)
	{
		/*RLOS.mLogger.info("Inside Query RLOS");
		RLOS.mLogger.info( "Inside formLoaded()" + pEvent.getSource().getName());*/
	}

	public void formPopulated(FormEvent pEvent) {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try{
			//formObject.fetchFragment("CustomerDetails", "Customer", "q_Customer");	
			//formObject.fetchFragment("ProductDetailsLoader", "Product", "q_Product");
			//RLOS.mLogger.info("Inside Query RLOS");
			formObject.fetchFragment("CustomerDetails", "Customer", "q_Customer");
			formObject.setNGFrameState("CustomerDetails", 0);
			//RLOS.mLogger.info( "Inside formPopulated()" + pEvent.getSource());
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			String currDate = format.format(Calendar.getInstance().getTime());
			//RLOS.mLogger.info( "currTime:" + currDate);
			formObject.setNGValue("Intro_Date",currDate);
			formObject.setNGValue("WIname",formObject.getWFWorkitemName());
			formObject.setNGValue("Channel_Name","Branch Initiation");
			formObject.setNGValue("Created_By",formObject.getUserName());

			// formObject.setNGValue("Intro_Date",formObject.getNGValue("Created_Date"));
			formObject.setNGValue("initiationChannel","Branch_Init");
			String init_Channel=formObject.getNGValue("initiationChannel");
			if("".equals(init_Channel))
				formObject.setNGValue("initiationChannel","Branch_Init");
			/*if(formObject.getNGValue("empType").contains("Salaried"))
				formObject.setSheetVisible("ParentTab",1, false);
			else if(formObject.getNGValue("empType").contains(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SelfEmployed")))
				formObject.setSheetVisible("ParentTab",3, false);*/
			if(formObject.getNGValue("EmploymentType").contains("Salaried"))
				formObject.setSheetVisible("ParentTab",1, false);
			else if(formObject.getNGValue("EmploymentType").contains(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SelfEmployed")))
				formObject.setSheetVisible("ParentTab",3, false);
			//added By Akshay-23/7/2017
			//new RLOSCommonCode().CustomerFragment_Load();
			/*formObject.fetchFragment("ProductDetailsLoader", "Product", "q_Product");
			formObject.setNGFrameState("ProductDetailsLoader", 0);
			*///ended By Akshay-23/7/2017

			if(formObject.getNGValue("Product_Type").contains(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_PersonalLoan")) && Integer.parseInt(formObject.getNGValue("Age"))<18)
			{
				formObject.setVisible("GuarantorDetails", true);
				formObject.setTop("Incomedetails",110);
			}
			if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard").equals(formObject.getNGValue("PrimaryProduct")))
				formObject.setSheetVisible("ParentTab",7, true);

			//RLOS.mLogger.info("Value Of Init Channel:"+init_Channel);
			formObject.setNGValue("Channel_Name","Branch Initiation");
			if(formObject.getNGValue("cmplx_Customer_FIrstNAme")==null)
			{
				formObject.setNGValue("cmplx_Customer_FIrstNAme","");
			}
			else if(formObject.getNGValue("cmplx_Customer_MiddleName")==null)
			{
				formObject.setNGValue("cmplx_Customer_MiddleName","");
			}

			else if( formObject.getNGValue("cmplx_Customer_LAstNAme")==null)
			{
				formObject.setNGValue("cmplx_Customer_LAstNAme","");
			}
			else
				formObject.setNGValue("Cust_Name",formObject.getNGValue("cmplx_Customer_FIrstNAme")+formObject.getNGValue("cmplx_Customer_MiddleName")+formObject.getNGValue("cmplx_Customer_LAstNAme"));

		}
		catch(Exception e)
		{
			RLOS.mLogger.info( "Exception:"+e.getMessage());
			RLOS.logException(e);
		}
	}

	
	public void call_Frame_Expanded(ComponentEvent pEvent)
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//RLOS.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());


		 if ("GuarantorDetails".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.setEnabled("GuarantorDetails",false);
			formObject.fetchFragment("GuarantorDetails", "GuarantorDetails", "q_GuarantorDetails");
		}


		else if ("ProductDetailsLoader".equalsIgnoreCase(pEvent.getSource().getName())) {

			//RLOS.mLogger.info( "Inside ProductDetailsLoader ");	
			//added Tanshu Aggarwal(23/06/2017)
			formObject.fetchFragment("ProductDetailsLoader", "Product", "q_Product");
			formObject.setNGFrameState("ProductDetailsLoader", 0);
				formObject.setEnabled("Product_Frame1", false);
				//RLOS.mLogger.info( "Inside ProductDetailsLoader if ");	
			formObject.setEnabled("ProductDetailsLoader",false);

		}


		else if ("EmploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) 
		{				
			if(!formObject.isVisible("EMploymentDetails_Frame1"))
			{							
				formObject.setEnabled("EmploymentDetails",false);
				formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmploymentDetails");
				if(formObject.getNGValue("cmplx_EmploymentDetails_Others")=="true"){
					formObject.setVisible("EMploymentDetails_Label72",false);
					formObject.setVisible("cmplx_EmploymentDetails_EMpCode",false);
				}
				else 
				{
					formObject.setVisible("EMploymentDetails_Label72",true);
					formObject.setVisible("cmplx_EmploymentDetails_EMpCode",true);
				}
				if(!formObject.isVisible("Product_Frame1")){
					formObject.fetchFragment("ProductDetailsLoader", "Product", "q_Product");
					formObject.setNGFrameState("ProductDetailsLoader", 0);
					formObject.setNGFrameState("ProductDetailsLoader", 1);
				}

				//IMFields_Employment();
				//BTCFields_Employment();
				//LimitIncreaseFields_Employment();
				//ProductUpgrade_Employment();
				
			}	
			

		}		 

		else if ("Incomedetails".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.setEnabled("Incomedetails",false);
			formObject.fetchFragment("Incomedetails", "IncomeDetails", "q_IncomeDetails");

			//int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			//for(int i=0;i<n;i++){
			//RLOS.mLogger.info( "Grid Data[1][9] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9));
			if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard").equalsIgnoreCase(formObject.getNGValue("PrimaryProduct")) /*&& formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")*/){
				//RLOS.mLogger.info( "Grid Data[1][9] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9));
				formObject.setVisible("IncomeDetails_Label12", true);
				formObject.setVisible("cmplx_IncomeDetails_StatementCycle", true);	
				formObject.setVisible("IncomeDetails_Label14", true);
				formObject.setVisible("cmplx_IncomeDetails_StatementCycle2", true);	
			}	
			/*else{
				formObject.setVisible("IncomeDetails_Label12", false);
				formObject.setVisible("cmplx_IncomeDetails_StatementCycle", false);	
				formObject.setVisible("IncomeDetails_Label14", false);
				formObject.setVisible("cmplx_IncomeDetails_StatementCycle2", false);			
			}*/
			//}

			//if(n>0)
			//{					
			//String EmpType=formObject.getNGValue("empType");
			String EmpType=formObject.getNGValue("EmploymentType");
			//RLOS.mLogger.info( "Emp Type Value is:"+EmpType);

			if("Salaried".equalsIgnoreCase(EmpType)|| "Salaried Pensioner".equalsIgnoreCase(EmpType)|| "Pensioner".equalsIgnoreCase(EmpType))
			{
				formObject.setVisible("IncomeDetails_Frame3", false);
				formObject.setHeight("Incomedetails", 730);
				formObject.setHeight("IncomeDetails_Frame1", 680);	
				if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
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

			else if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SelfEmployed").equalsIgnoreCase(EmpType))
			{							
				formObject.setVisible("IncomeDetails_Frame2", false);
				formObject.setTop("IncomeDetails_Frame3",40);
				formObject.setHeight("Incomedetails", 300);
				formObject.setHeight("IncomeDetails_Frame1", 280);
				if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
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
			}
			//}

			//IMFields_Income();
			//BTCFields_Income();
			//LimitIncreaseFields_Income();
			//ProductUpgrade_Income();


		}	
		else if ("ReferenceDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("ReferenceDetails","Clicked");
			popupFlag="N";
			formObject.fetchFragment("ReferenceDetails_container", "ReferenceDetails", "q_ReferenceDetails");
			LoadPickList("ReferenceDetails_ref_Relationship", "select convert(varchar, Description),code from NG_MASTER_Relationship with(nolock) union select '--Select--','' order by Code");

			//loadPicklist3();
			//loadInDecGrid();
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}

		else if ("CompanyDetails".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.setEnabled("CompanyDetails",false);
			formObject.fetchFragment("CompanyDetails", "CompanyDetails", "q_CompanyDetails");	




			if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_BTC").equalsIgnoreCase(formObject.getNGValue("Subproduct_productGrid"))){
				formObject.setVisible("CompanyDetails_Label8", true);
				formObject.setVisible("CompanyDetails_effecLOB", true);	

			}

			else if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Se").equalsIgnoreCase(formObject.getNGValue("Subproduct_productGrid"))/* && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")*/){
				formObject.setVisible("CompanyDetails_Label27", true);
				formObject.setVisible("CompanyDetails_EOW", true);
				formObject.setVisible("CompanyDetails_Label29", true);
				formObject.setVisible("CompanyDetails_headOffice", true);
				formObject.setVisible("CompanyDetails_Label28", true);
				formObject.setVisible("CompanyDetails_visaSponsor", true);


			}
			else{
				formObject.setVisible("CompanyDetails_Label27", false);
				formObject.setVisible("CompanyDetails_EOW", false);//Emirate of work
				formObject.setVisible("CompanyDetails_Label29", false);
				formObject.setVisible("CompanyDetails_headOffice", false);
				formObject.setVisible("CompanyDetails_Label28", false);
				formObject.setVisible("CompanyDetails_visaSponsor", false);
				formObject.setVisible("CompanyDetails_Label8", false);
				formObject.setVisible("CompanyDetails_effecLOB", false);
			}



			//RLOS.mLogger.info( "CompanyDetailse1:");

		}




		/*else	if ("AuthorisedSignatoryDetails".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.setEnabled("AuthorisedSignatoryDetails",false);
			formObject.fetchFragment("AuthorisedSignatoryDetails", "AuthorisedSignDetails", "q_AuthorisedSignDetails");
			RLOS.mLogger.info( "AuthorisedSignatoryDetails:");




		}
		else if ("PartnerDetails".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			RLOS.mLogger.info( "Saurabh123456");
			formObject.setEnabled("PartnerDetails",false);
			formObject.fetchFragment("PartnerDetails", "PartnerDetails", "q_PartnerDetails");
			
			int authgridRowCount = formObject.getLVWRowCount("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails");
			if(authgridRowCount >0){
				for(int i=0;i<authgridRowCount;i++){
					String soleEmployee = formObject.getNGValue("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails", i, 10);
					if("Yes".equalsIgnoreCase(soleEmployee)){
						formObject.setEnabled("PartnerDetails",false);
					}
				}
			}
		}
*/

		//Modified by akshay for NTB/NEP case only
		else if ("DecisionHistoryContainer".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setEnabled("DecisionHistoryContainer",false);
			formObject.fetchFragment("DecisionHistoryContainer", "DecisionHistory", "q_DecisionHistory");
		
			//25/07/2017  for mandatory checks fragments should be fetched first
		
			int framestate1=formObject.getNGFrameState("Incomedetails");
			if(framestate1 == 0){
				//RLOS.mLogger.info("Incomedetails");
			}
			else {
				formObject.setEnabled("Incomedetails",false);
				formObject.fetchFragment("Incomedetails", "IncomeDetails", "q_IncomeDetails");
				//RLOS.mLogger.info("fetched income details");
				formObject.setTop("Incomedetails",formObject.getTop("ProductDetailsLoader")+formObject.getHeight("ProductDetailsLoader"));
			}
			
			int framestate2=formObject.getNGFrameState("EmploymentDetails");
			if(framestate2 == 0){
				//RLOS.mLogger.info("EmploymentDetails");
			}
			else {
				formObject.setEnabled("EmploymentDetails",false);
				formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmploymentDetails");
				//RLOS.mLogger.info("fetched employment details");
			}
			openDemographicTabs();
			//25/07/2017  for mandatory checks fragments should be fetched first
			
			//added Tanshu Aggarwal(23/06/2017)
			
			String countCurrentAccount=formObject.getNGDataFromDataCache("SELECT count(acctId) from ng_RLOS_CUSTEXPOSE_AcctDetails with(nolock) where AcctType='CURRENT ACCOUNT' AND Wi_Name='"+formObject.getWFWorkitemName()+"' ").get(0).get(0);
			//RLOS.mLogger.info(countCurrentAccount);
			formObject.setNGValue("ExistingCustomerAccount",countCurrentAccount);

			String NTB=formObject.getNGValue("NTB");
			String NEP=formObject.getNGValue("NEP");
			//RLOS.mLogger.info(NTB+","+NEP);
			//added Tanshu Aggarwal(23/06/2017)
			//if(Product.toUpperCase().contains("PERSONAL LOAN") && formObject.getNGValue("ExistingCustomerAccount").equals("0") && (formObject.getNGValue("NTB").equals("true") || formObject.getNGValue("NEP").equals("true")))
			if("0".equals(formObject.getNGValue("ExistingCustomerAccount")))
			{
				//RLOS.mLogger.info("inside visibility true");
				formObject.setVisible("DecisionHistory_Button3", true);//Create CIF/Acc
			}
			else{
				//RLOS.mLogger.info("inside visibility false");
				formObject.setVisible("DecisionHistory_Button3", false);//Create CIFAcc
			}
			
		}


		else if ("EligibilityAndProductInformation".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setEnabled("EligibilityAndProductInformation",false);
			formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligAndProductInfo");
			formObject.setNGFrameState("EligibilityAndProductInformation", 0);
			//RLOS.mLogger.info("Saurabh ELPINFO,Framexpanded");
			//Fields_Eligibility();
			//Fields_ApplicationType_Eligibility();
			formObject.setEnabled("cmplx_EligibilityAndProductInfo_FinalInterestRate", false);

			//RLOS.mLogger.info("Eligibility grid");

			if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_PersonalLoan").equalsIgnoreCase(formObject.getNGValue("PrimaryProduct"))/* && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")*/){
				formObject.setVisible("ELigibiltyAndProductInfo_Frame2", true);//Funding Account Number Grid
				formObject.setVisible("ELigibiltyAndProductInfo_Frame4", true);//Personal Loan
				formObject.setVisible("ELigibiltyAndProductInfo_Frame3", false);//Lein Details
				//RLOS.mLogger.info( "Funding Account Details now Visible...!!!");


			}

			else if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard").equalsIgnoreCase(formObject.getNGValue("PrimaryProduct"))/* && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")*/){

				formObject.setVisible("ELigibiltyAndProductInfo_Frame2", false);//Funding Account Number Grid
				formObject.setVisible("ELigibiltyAndProductInfo_Frame4", false);//Personal Loan
				formObject.setVisible("ELigibiltyAndProductInfo_Frame3", false);//Lein Details
				formObject.setTop("ELigibiltyAndProductInfo_Frame5",formObject.getTop("ELigibiltyAndProductInfo_Frame1")+5);//CC
				formObject.setTop("ELigibiltyAndProductInfo_Frame6",formObject.getTop("ELigibiltyAndProductInfo_Frame5")+formObject.getHeight("ELigibiltyAndProductInfo_Frame5")+10);//Eligible For Card

				if(formObject.getNGValue("CardProduct_Primary").toUpperCase().contains("-SEC") /*&& formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")*/){
					formObject.setVisible("ELigibiltyAndProductInfo_Frame3", true);//Lein Details
					formObject.setTop("ELigibiltyAndProductInfo_Frame3",formObject.getTop("ELigibiltyAndProductInfo_Frame6")+formObject.getHeight("ELigibiltyAndProductInfo_Frame6")+10);
					//RLOS.mLogger.info( "Lein Details now Visible...!!!");
				}
				//break;
			}
			//}
			//}
			else{
				formObject.setVisible("ELigibiltyAndProductInfo_Frame2", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Frame3", false);
			}

		}	

		else if ("Liability_container".equalsIgnoreCase(pEvent.getSource().getName()))
       {        
			formObject.setEnabled("Liability_container",false);				
           formObject.fetchFragment("Liability_container", "Liability_New", "q_LiabilityNew");              
       }


		else if ("CC_Loan_container".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
		formObject.setEnabled("CC_Loan_container",false);	
			formObject.fetchFragment("CC_Loan_container", "CC_Loan", "q_CC_Loan");
		}	


		else if ("Address_Details_container".equalsIgnoreCase(pEvent.getSource().getName()))

		{	
		formObject.setEnabled("Address_Details_container",false);	
			formObject.fetchFragment("Address_Details_container", "AddressDetails", "q_AddressDetails");	
		}	


		else if ("Alt_Contact_container".equalsIgnoreCase(pEvent.getSource().getName())) 				
		{

			formObject.setEnabled("Alt_Contact_container",false);	
			formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");


		}



		/*else if (pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails")) {       	


			formObject.fetchFragment("ReferenceDetails", "ReferenceDetails", "q_ReferenceDetails");
			
		}*/


		else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) 
		{


			formObject.setEnabled("CardDetails",false);	
			formObject.fetchFragment("CardDetails", "CardDetails", "q_CardDetails");
			//int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			//for(int i=0;i<n;i++){
			if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_PersonalLoan").equalsIgnoreCase(formObject.getNGValue("PrimaryProduct"))){
				formObject.setVisible("CardDetails_Label7", true);
				formObject.setVisible("cmplx_CardDetails_statCycle", true);

			}	
			if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_BTC").equalsIgnoreCase(formObject.getNGValue("Subproduct_productGrid")) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Se").equalsIgnoreCase(formObject.getNGValue("Subproduct_productGrid"))){
				formObject.setVisible("CardDetails_Label3", true);
				formObject.setVisible("cmplx_CardDetails_CompanyEmbossingName", true);
				formObject.setLeft("CardDetails_Label5", 552);
				formObject.setLeft("cmplx_CardDetails_SuppCardReq", 552);



			}
			if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic").equalsIgnoreCase(formObject.getNGValue("LoanType_Primary"))){
				formObject.setVisible("CardDetails_Label2", true);
				formObject.setVisible("cmplx_CardDetails_CharityOrg", true);
				formObject.setVisible("CardDetails_Label4", true);
				formObject.setVisible("cmplx_CardDetails_CharityAmount", true);


				formObject.setLeft("CardDetails_Label5", 1059);
				formObject.setLeft("cmplx_CardDetails_SuppCardReq", 1059);


			}	

		}


		else if ("Supplementary_Container".equalsIgnoreCase(pEvent.getSource().getName())) 
		{

			formObject.setEnabled("Supplementary_Container",false);	
			formObject.fetchFragment("Supplementary_Container", "SupplementCardDetails", "q_SuppCardDetails");


			//if (NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Salariedpensioner").equalsIgnoreCase(formObject.getNGValue("empType"))){
			if (NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Salariedpensioner").equalsIgnoreCase(formObject.getNGValue("EmploymentType")) || ("Pensioner").equalsIgnoreCase(formObject.getNGValue("EmploymentType"))){
				formObject.setVisible("CompEmbName", false);
				formObject.setVisible("SupplementCardDetails_Label7", false);
			}
		}	


		else if ("FATCA_container".equalsIgnoreCase(pEvent.getSource().getName())) 
		{

			formObject.setEnabled("FATCA",false);	
			formObject.fetchFragment("FATCA_container", "FATCA", "q_FATCA");


		}	


		else if ("KYC_container".equalsIgnoreCase(pEvent.getSource().getName())) 
		{

			formObject.setEnabled("KYC",false);
			formObject.fetchFragment("KYC_container", "KYC", "q_KYC");


		}	


		else if ("OECD_container".equalsIgnoreCase(pEvent.getSource().getName())) 
		{

			formObject.setEnabled("OECD",false);
			formObject.fetchFragment("OECD_container", "OECD", "q_OECD");

		}	


		else  if ("IncomingDocuments".equalsIgnoreCase(pEvent.getSource().getName()))

		{
			formObject.setEnabled("IncomingDocuments",false);
			int framestate0 = formObject.getNGFrameState("ProductDetailsLoader");
			if(framestate0 == 0){
				//RLOS.mLogger.info("ProductDetailsLoader");
			}
			else {
			formObject.setEnabled("ProductDetailsLoader",false);
				formObject.fetchFragment("ProductDetailsLoader", "Product", "q_Product");
				//RLOS.mLogger.info("fetched product details");
				formObject.setTop("Incomedetails",formObject.getTop("ProductDetailsLoader")+formObject.getHeight("ProductDetailsLoader"));
			}
			
			
				int framestate1=formObject.getNGFrameState("Incomedetails");
				if(framestate1 == 0){
					//RLOS.mLogger.info("Incomedetails");
				}
				else {
				formObject.setEnabled("Incomedetails",false);
					formObject.fetchFragment("Incomedetails", "IncomeDetails", "q_IncomeDetails");
					//RLOS.mLogger.info("fetched income details");
					formObject.setTop("Incomedetails",formObject.getTop("ProductDetailsLoader")+formObject.getHeight("ProductDetailsLoader"));
				}
			
			

			//RLOS.mLogger.info("IncomingDocuments");
			formObject.setEnabled("IncomingDocuments",false);
			formObject.fetchFragment("IncomingDocuments", "IncomingDoc", "q_IncomingDoc");
			//RLOS.mLogger.info("fetchIncomingDocRepeater");
			fetchIncomingDocRepeater();
			//RLOS.mLogger.info("formObject.fetchFragment1");
		}

		
	}
	
	public void call_Fragement_Loaded(ComponentEvent pEvent)
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//RLOS.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		//RLOS.mLogger.info("Value Of init type is:"+formObject.getNGValue("initiationChannel")); 
		if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_M").equalsIgnoreCase(formObject.getNGValue("initiationChannel")) ){
			//RLOS.mLogger.info("Value Of init type is:"+formObject.getNGValue("initiationChannel")); 
			new RLOSCommonCode().DisableFragmentsOnLoad(pEvent);
		}
		else{
			if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
				if("".equals(formObject.getNGValue("cmplx_Customer_NEP"))){
					formObject.setLocked("Customer_Frame1",true);
					//formObject.setEnabled("Customer_Frame1",false);
					formObject.setHeight("Customer_Frame1", 510);
					formObject.setHeight("CustomerDetails", 530);
					formObject.setLocked("Customer_save",false);

					formObject.setLocked("Customer_Frame2",false);
					formObject.setLocked("Customer_Frame3",false);
					if ("true".equals(formObject.getNGValue("cmplx_Customer_NTB"))){
						
						formObject.setVisible("Customer_Frame2", false);
					}
					else {
						formObject.setHeight("Customer_Frame1", 610);
						formObject.setHeight("CustomerDetails", 700);
						//formObject.setLocked("Customer_save",false);
						formObject.setVisible("Customer_Frame2", true);
					}

				}
				formObject.setLocked("cmplx_Customer_CardNotAvailable",false);
				formObject.setEnabled("cmplx_Customer_card_id_available",true);
				if (!"true".equals(formObject.getNGValue("cmplx_Customer_card_id_available"))){
					
					formObject.setLocked("ReadFromCard",false);
				}
				
				formObject.setLocked("cmplx_Customer_NEP",false);
				formObject.setLocked("cmplx_Customer_DLNo",false);
				formObject.setLocked("cmplx_Customer_Passport2",false);
				formObject.setLocked("cmplx_Customer_Passport3",false);
				formObject.setLocked("cmplx_Customer_PAssport4",false);
				//RLOS.mLogger.info( "");
				//formObject.setLocked("cmplx_Customer_SecNAtionApplicable",false);


				String EID = formObject.getNGValue("cmplx_Customer_EmiratesID");
				String NEP = formObject.getNGValue("cmplx_Customer_NEP");
				String card_not_avl = formObject.getNGValue("cmplx_Customer_CardNotAvailable");


				if("".equalsIgnoreCase(EID) && "false".equalsIgnoreCase(NEP) && "false".equalsIgnoreCase(card_not_avl)){
					//RLOS.mLogger.info( "");
					//formObject.setLocked("cmplx_Customer_SecNAtionApplicable",false);
				}
				else{
					String is_fetched_details = formObject.getNGValue("cmplx_Customer_ISFetchDetails");
					if(!"Y".equalsIgnoreCase(is_fetched_details)){
					formObject.setLocked("cmplx_Customer_FIrstNAme",false);
					formObject.setLocked("cmplx_Customer_MiddleName",false);
					formObject.setLocked("cmplx_Customer_LAstNAme",false);
					formObject.setEnabled("cmplx_Customer_DOb",true);
					formObject.setLocked("cmplx_Customer_PAssportNo",false);
					formObject.setLocked("cmplx_Customer_Nationality",false);
					formObject.setLocked("cmplx_Customer_MobNo",false);
					formObject.setLocked("FetchDetails",true);
					formObject.setVisible("Customer_Label56", true);
					formObject.setVisible("cmplx_Customer_EIDARegNo", true);
					//		formObject.setLocked("cmplx_Customer_SecNAtionApplicable",false);
					//RLOS.mLogger.info( "");
					}
				}
				formObject.setNGValue("ref_Relationship","FRIEND");
				//formObject.setNGValue("cmplx_Customer_COuntryOFResidence", "United Arab Emirates-AE");
				formObject.setNGValue("cmplx_Customer_CustomerCategory", "Normal");
				loadPicklistCustomer();
				//RLOS.mLogger.info("Encrypted CIF is: "+formObject.getNGValue("encrypt_CIF"));
				//formObject.setNGValue("cmplx_Customer_CIFNO",formObject.getNGValue("encrypt_CIF"));
				try{
				String if_cif_available = formObject.getNGValue("cmplx_Customer_CIFNO");
				//RLOS.mLogger.info("CIF is: "+if_cif_available);
				if(if_cif_available !=null && !"".equalsIgnoreCase(if_cif_available) && !" ".equalsIgnoreCase(if_cif_available)){
					setcustomer_enable();
					//RLOS.mLogger.info("Saurabh12345");
				}
				if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_card_id_available")) && "".equalsIgnoreCase(formObject.getNGValue("Is_Customer_Eligibility"))){
					formObject.setLocked("Customer_Button1", false);
				}
				}catch(Exception ex){
					RLOS.mLogger.info( printException(ex));
				}
			}

			else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
				LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct with (nolock)");
				LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
			}

			else if ("GuarantorDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				
				formObject.setNGValue("gender", "");
				formObject.setNGValue("nationality", "--Select--");
				// formObject.setNGValue("SignStatus", "--Select--");
				LoadPickList("title", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_title with (nolock) order by code");
				formObject.setNGValue("title", "");
				//LoadPickList("gender", "select convert(varchar, description) from NG_MASTER_gender with (nolock)");
				LoadPickList("gender", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_gender with (nolock) order by Code");
				LoadPickList("nationality", "select convert(varchar, description) from NG_MASTER_country with (nolock)");
			}

			else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("cmplx_IncomeDetails_grossSal", true);
				formObject.setLocked("cmplx_IncomeDetails_TotSal", true);
				formObject.setLocked("cmplx_IncomeDetails_netSal1", false);
				formObject.setLocked("cmplx_IncomeDetails_netSal2", false);
				formObject.setLocked("cmplx_IncomeDetails_netSal3", false);
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

			else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {

				 //change by saurabh on 9th July 17.
                if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Liability_New_AECBconsentAvail"))){
                      formObject.setLocked("Liability_New_fetchLiabilities",false);
                }
                
				//added by abhishek
				int count = formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
				for(int i =0;i<count;i++){
					if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_BTC").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2)) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SE").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2))){
						formObject.setVisible("Label9", false);
						formObject.setVisible("cmplx_Liability_New_DBR", false);
						formObject.setVisible("Label15", false);
						formObject.setVisible("cmplx_Liability_New_TAI", false);
						formObject.setVisible("Liability_New_Label14", false);
						formObject.setVisible("cmplx_Liability_New_DBRNet", false);
						formObject.setVisible("Button2", false);
						formObject.setLeft("Liability_New_Label15", 7);
						formObject.setLeft("cmplx_Liability_New_AggrExposure", 7);

					}

				}

				formObject.setLocked("Liability_New_fetchLiabilities",true);
				formObject.setLocked("takeoverAMount",true);
				formObject.setLocked("cmplx_Liability_New_DBR",true);
				formObject.setLocked("cmplx_Liability_New_DBRNet",true);
				formObject.setLocked("cmplx_Liability_New_AggrExposure",true);
				formObject.setLocked("cmplx_Liability_New_TAI",true);
			}


			else if ("CompanyDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				//RLOS.mLogger.info("AKSHAY COMPANY FRAGMENT LOADED");
				formObject.setLocked("cif", true);
				formObject.setLocked("CompanyDetails_Button3", true);
				LoadPickList("CompanyDetails_appType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_ApplicantType with (nolock) order by code");
				LoadPickList("CompanyDetails_indusSector", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_IndustrySector with (nolock) order by code");
				LoadPickList("CompanyDetails_indusMAcro", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_EmpIndusMacro with (nolock) order by code");
				LoadPickList("CompanyDetails_indusMicro", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_EmpIndusMicro with (nolock) order by code");
				LoadPickList("CompanyDetails_legalEntity", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_LegalEntity with (nolock) order by code");
				LoadPickList("CompanyDetails_desig", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
				LoadPickList("CompanyDetails_desigVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
				LoadPickList("CompanyDetails_EOW", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_emirate with (nolock) order by code");
				LoadPickList("CompanyDetails_headOffice", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_emirate with (nolock) order by code");
			}

			else if ("AuthorisedSignDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("CIFNo", true);
				formObject.setLocked("AuthorisedSignDetails_Button4", true);
				formObject.setNGValue("SignStatus", "--Select--");
				LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_Country with (nolock) order by code");
				formObject.setNGValue("AuthorisedSignDetails_nationality", "");
				LoadPickList("SignStatus", "select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");
			}

			else if ("PartnerDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setNGValue("PartnerDetails_nationality", "--Select--");
				LoadPickList("PartnerDetails_nationality", "select convert(varchar, description) from NG_MASTER_Country with (nolock)");
			}


			else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

				String empid="AVI,MED,EDU,HOT,PROM";	
				formObject.setVisible("EMploymentDetails_Label25",false);
				formObject.setVisible("cmplx_EmploymentDetails_NepType",false);
				formObject.setVisible("cmplx_EmploymentDetails_Freezone",false);
				formObject.setVisible("EMploymentDetails_Label62",false);
				formObject.setVisible("cmplx_EmploymentDetails_FreezoneName",false);
				formObject.setVisible("FreeZone_Button",false);
				formObject.setVisible("cmplx_EmploymentDetails_tenancntrct",false);
				formObject.setVisible("EMploymentDetails_Label5",false);
				formObject.setVisible("cmplx_EmploymentDetails_IndusSeg",false);
				formObject.setVisible("EMploymentDetails_Label59",false);
				formObject.setVisible("cmplx_EmploymentDetails_channelcode",false);
				formObject.setVisible("EMploymentDetails_Label36",false);
				formObject.setLocked("cmplx_EmploymentDetails_EmpName",true);
				formObject.setLocked("cmplx_EmploymentDetails_EMpCode",true);
				formObject.setLocked("cmplx_EmploymentDetails_LOS",true);
				loadPicklist4();
				Fields_ApplicationType_Employment();

				if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_NEP").equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode"))){
					formObject.setVisible("EMploymentDetails_Label25",true);
					formObject.setVisible("cmplx_EmploymentDetails_NepType",true);
				}

				else if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_FZD").equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_FZE").equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode"))){
					formObject.setVisible("cmplx_EmploymentDetails_Freezone",true);
					formObject.setVisible("EMploymentDetails_Label62",true);
					formObject.setVisible("cmplx_EmploymentDetails_FreezoneName",true);
					formObject.setVisible("FreeZone_Button",false);
				}

				else if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_TEN").equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")))
				{
					formObject.setVisible("cmplx_EmploymentDetails_tenancntrct",true);
					formObject.setVisible("EMploymentDetails_Label5",true);
				}

				else if("S".equals(formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg")) && empid.contains(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")))
				{
					formObject.setVisible("cmplx_EmploymentDetails_IndusSeg",true);
					formObject.setVisible("EMploymentDetails_Label59",true);
					
				}
					
			}

			else if ("MiscellaneousFields".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("cmplx_MiscFields_School",true);
				formObject.setLocked("cmplx_MiscFields_PropertyType",true);
				formObject.setLocked("cmplx_MiscFields_RealEstate",true);
				formObject.setLocked("cmplx_MiscFields_FarmEmirate",true);
			}

			else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
				//RLOS.mLogger.info("Saurabh ELPINFO,Fragment Loaded");
				LoadPickList("cmplx_EligibilityAndProductInfo_RepayFreq", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_frequency with (nolock) order by code");
				LoadPickList("cmplx_EligibilityAndProductInfo_instrumenttype", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from  NG_MASTER_instrumentType with (nolock) order by code");
				LoadPickList("cmplx_EligibilityAndProductInfo_InterestType", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_InterestType with (nolock) order by code");

				formObject.setNGValue("cmplx_EligibilityAndProductInfo_RepayFreq","Monthly");
				//formObject.setVisible("ELigibiltyAndProductInfo_Label39",false);
				//formObject.setVisible("cmplx_EligibilityAndProductInfo_instrumenttype",false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label1",false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_TakeoverAMount",false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label2",false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_takeoverBank",false);
			
				formObject.setLocked("cmplx_EligibilityAndProductInfo_LPF",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_ageAtMaturity",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_LPFAmount",false); //Drop 4 point 21 changed by Tarang on 29/01/2018 
				formObject.setLocked("cmplx_EligibilityAndProductInfo_InsuranceAmount",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_Insurance",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_NumberOfInstallment",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalDBR",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalTAI",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalLimit",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_BAseRate",true);	
				//formObject.setLocked("cmplx_EligibilityAndProductInfo_MArginRate",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_ProdPrefRate",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_MaturityDate",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_InterestType",true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_BaseRateType",true);

			}


			else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

				loadPicklist_Address();
			}

			else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setVisible("AlternateContactDetails_custdomicile",false);
				formObject.setVisible("AltContactDetails_Label14",false);
				//RLOS.mLogger.info("AMasd COMPANY FRAGMENT LOADED"+formObject.getNGValue("Product_Type"));
				if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard").equalsIgnoreCase(formObject.getNGValue("Product_Type"))){
					formObject.setVisible("AlternateContactDetails_retainAccount",false);
				}
				else{
					formObject.setVisible("AlternateContactDetails_retainAccount",true);
				}	
				}

			else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLeft("CardDetails_Label5",288);
				formObject.setLeft("cmplx_CardDetails_SuppCardReq",288);
			}

			else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				LoadPickList("ResdCountry", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by code");
				LoadPickList("relationship", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_Relationship with (nolock) order by code");
				LoadPickList("nationality", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
				LoadPickList("gender", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_gender with (nolock) order by Code");
			}
			else if ("ReferenceDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				LoadPickList("ReferenceDetails_ref_Relationship", "select '--Select--' as description,'' as code union select convert(varchar, description) from NG_MASTER_Relationship with (nolock) order by code");
				formObject.setNGValue("ReferenceDetails_ref_Relationship", "FRE");
			}
			else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) {
				LoadPickList("cmplx_FATCA_Category", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_category with (nolock) order by code");
				//LoadPickList("cmplx_FATCA_Category", "select '--Select--' union select convert(varchar, description) from NG_MASTER_category with (nolock)");
				LoadPickList("cmplx_FATCA_USRelation", " select '--Select--' as description, '' as code  union select convert(varchar,description),code  from ng_master_usrelation with(nolock) order by code");
				
				String usRelation = formObject.getNGValue("cmplx_FATCA_USRelation");
				if("O".equalsIgnoreCase(usRelation)){
					formObject.setEnabled("FATCA_Frame6", false);
					formObject.setEnabled("cmplx_FATCA_USRelation",true);
				}
			}

			else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {
				LoadPickList("OECD_CountryBirth", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_Country with (nolock) order by code");
				//LoadPickList("OECD_townBirth", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_city with (nolock) order by code");
				LoadPickList("OECD_CountryTaxResidence", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_Country with (nolock) order by code");
				LoadPickList("OECD_CRSFlagReason", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_CRSReason with (nolock) order by code");
				formObject.setLocked("OECD_CRSFlagReason",true);
			}

			else if ("CC_Loan".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.setLocked("cmplx_CC_Loan_mchequeno",true);
				formObject.setLocked("cmplx_CC_Loan_mchequeDate",true);
				formObject.setLocked("cmplx_CC_Loan_mchequestatus",true);
				loadPicklist_ServiceRequest();
			}

			if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
				loadPicklist3();
				String query="Select count(*) from ng_RLOS_CUSTEXPOSE_AcctDetails with(nolock) where AcctType='CURRENT ACCOUNT' AND Wi_Name='"+formObject.getWFWorkitemName()+"'";
				List<List<String>> AccountCount= formObject.getNGDataFromDataCache(query);
				//RLOS.mLogger.info( "Query is: "+query+" Value In AccountCount is "+AccountCount);
				//RLOS.mLogger.info( "NTB is:"+formObject.getNGValue("cmplx_Customer_NTB"));
				formObject.setNGValue("AccountCount", AccountCount.get(0).get(0));
				if(("true".equals(formObject.getNGValue("cmplx_Customer_NTB")) || "0".equals(AccountCount.get(0).get(0))) && NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_PersonalLoan").equals(formObject.getNGValue("Product_Type"))){
					//RLOS.mLogger.info(" Product Type Is: "+formObject.getNGValue("Product_Type"));
					formObject.setVisible("DecisionHistory_Button3",true);
				}
				
				else
				{
					if (NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard").equalsIgnoreCase(formObject.getNGValue("Product_Type"))){
						formObject.setVisible("cmplx_DecisionHistory_AccountNumber",false );
						formObject.setVisible("cmplx_DecisionHistory_IBAN",false );
						formObject.setVisible("DecisionHistory_Label3",false );
						formObject.setVisible("AccountNum",false );
						
					}
					else {
						formObject.setVisible("cmplx_DecisionHistory_AccountNumber",true );
						formObject.setVisible("cmplx_DecisionHistory_IBAN",true );
						formObject.setVisible("DecisionHistory_Label3",true );
						formObject.setVisible("AccountNum",true );
						
					}
					formObject.setVisible("DecisionHistory_Button3",false);//create CIF Acc
				}

				formObject.setNGValue("cmplx_DecisionHistory_Decision", "--Select--");
				
			}
		}
	}
	
	public void call_Value_Changed(ComponentEvent pEvent)
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//RLOS.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());


		if ("cmplx_Customer_DOb".equalsIgnoreCase(pEvent.getSource().getName())){
			//RLOS.mLogger.info( "Value of dob is:"+formObject.getNGValue("cmplx_Customer_DOb"));
			//getAge(formObject.getNGValue("cmplx_Customer_DOb"),"cmplx_Customer_age");
		}	


		if ("cmplx_Customer_Nationality".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			//RLOS.mLogger.info( "iNSIDE cmplx_Customer_Nationality CASE-->akShay "+formObject.getNGValue("cmplx_Customer_Nationality"));

			String  GCC="BH,IQ,KW,OM,QA,SA,AE";

			if(GCC.contains(formObject.getNGValue("cmplx_Customer_Nationality")))
			{
				//RLOS.mLogger.info("Inside GCC for Nationality");
				formObject.setNGValue("cmplx_Customer_GCCNational","Y");
			}
			else
			{
				formObject.setNGValue("cmplx_Customer_GCCNational","N");
			}
		}

		if ("ReqProd".equalsIgnoreCase(pEvent.getSource().getName())){
			ReqProd=formObject.getNGValue("ReqProd");
			//RLOS.mLogger.info( "Value of ReqProd is:"+ReqProd);
			loadPicklistProduct(ReqProd);
		}


		if ("SubProd".equalsIgnoreCase(pEvent.getSource().getName())){
			RLOS.mLogger.info( "Value of SubProd is:"+formObject.getNGValue("SubProd"));
			formObject.clear("AppType");

			LoadPickList("AppType", "select convert(text, description),code from ng_master_ApplicationType with (nolock) where subProdFlag='"+formObject.getNGValue("SubProd")+"' order by code");
			formObject.setNGValue("AppType","--Select--");

			String subprod=formObject.getNGValue("SubProd");
			String VIPFlag=formObject.getNGValue("cmplx_Customer_VIPFlag");
			String Nationality=formObject.getNGValue("cmplx_Customer_Nationality");
			String req_prod=formObject.getNGValue("ReqProd");
			String TypeOfProd=formObject.getNGValue("Product_type");
			/*RLOS.mLogger.info( "Value of SubProd is:"+subprod);
			RLOS.mLogger.info( "Value of vip is:"+VIPFlag);
			RLOS.mLogger.info( "Value of Nationality is:"+Nationality);
			RLOS.mLogger.info( "Value of req_prod is:"+req_prod);
			RLOS.mLogger.info( "Value of TypeOfProd is:"+TypeOfProd);*/

			if (NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard").equalsIgnoreCase(req_prod)){
				if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_ProductUpgrade").equalsIgnoreCase(subprod)||NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_InstantMoney").equalsIgnoreCase(subprod)||NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_ProductUpgrade_LimitIncrease").equalsIgnoreCase(subprod)||NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_LimitIncrease").equalsIgnoreCase(subprod))
				{
					formObject.clear("CardProd");
					formObject.setNGValue("CardProd","--Select--");
					//RLOS.mLogger.info( " is not BTC ,PA :"+subprod);
					String ProdType=formObject.getNGValue("Product_type");
					LoadPickList("CardProd","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cardProduct with (nolock) where ReqProduct = '"+ProdType+"' order by code");

				}
				else{
					formObject.clear("CardProd");
					formObject.setNGValue("CardProd","--Select--");
					String query1="select convert(varchar, Description) from ng_MASTER_CardProduct with (nolock) where Reqproduct='"+TypeOfProd+"' and SUBproduct = '"+subprod+"'  and VIPFlag='"+VIPFlag+"' and Nationality='"+Nationality+"'";	
					String query2="select convert(varchar, Description) from ng_MASTER_CardProduct with (nolock) where Reqproduct='"+TypeOfProd+"' and SUBproduct = '"+subprod+"'  and VIPFlag='"+VIPFlag+"' and Nationality!='"+Nationality+"'";
					/*RLOS.mLogger.info( ": "+Nationality);
					RLOS.mLogger.info(query1);*/
					if("AE".equalsIgnoreCase(Nationality)){
						//RLOS.mLogger.info(query1);
						LoadPickList("CardProd",query1);
					}
					else{
						//RLOS.mLogger.info(query2);
						LoadPickList("CardProd",query2);
					}
				}
			}
		}

		if ("AppType".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			String subprod=formObject.getNGValue("SubProd");
			String apptype=formObject.getNGValue("AppType");
			String TypeofProduct=formObject.getNGValue("Product_type");
			/*RLOS.mLogger.info( "Value of SubProd is:"+formObject.getNGValue("SubProd"));
			RLOS.mLogger.info( "Value of AppType is:"+formObject.getNGValue("AppType"));
			RLOS.mLogger.info( "Value of AppType is:"+formObject.getNGValue("Product_type"));*/


			if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Conventional").equalsIgnoreCase(TypeofProduct))&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_EXP").equalsIgnoreCase(subprod)&&("TKOE".equalsIgnoreCase(apptype))){


				/*RLOS.mLogger.info( "1Value of AppType is:"+formObject.getNGValue("AppType"));*/


				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select convert(varchar, SCHEMEDESC) from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Take Over'  and TypeofProduct='Conventional'");




			}
			if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Conventional").equalsIgnoreCase(TypeofProduct))&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_EXP").equalsIgnoreCase(subprod)&&("NEWE".equalsIgnoreCase(apptype))){
				//RLOS.mLogger.info( "2Value of AppType is:"+formObject.getNGValue("AppType"));




				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select convert(varchar, SCHEMEDESC) from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'New' and TypeofProduct='Conventional'");




			}
			if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Conventional").equalsIgnoreCase(TypeofProduct)&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_EXP").equalsIgnoreCase(subprod))&&("TOPE".equalsIgnoreCase(apptype))){
				//RLOS.mLogger.info( "3Value of AppType is:"+formObject.getNGValue("AppType"));


				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select convert(varchar, SCHEMEDESC) from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Top up' and TypeofProduct='Conventional'");


			}
			if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Conventional").equalsIgnoreCase(TypeofProduct)&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_EXP").equalsIgnoreCase(subprod))&&(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESC").equalsIgnoreCase(apptype) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESN").equalsIgnoreCase(apptype) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESR").equalsIgnoreCase(apptype))){
				//RLOS.mLogger.info( "4Value of AppType is:"+formObject.getNGValue("AppType"));


				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select convert(varchar, SCHEMEDESC) from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Reschedulment' and TypeofProduct='Conventional'");


			}
			if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Conventional").equalsIgnoreCase(TypeofProduct)&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_NAT").equalsIgnoreCase(subprod))&&("TOPN".equalsIgnoreCase(apptype))){
			//	RLOS.mLogger.info( "5Value of AppType is:"+formObject.getNGValue("AppType"));
				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select convert(varchar, SCHEMEDESC) from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Top up' and TypeofProduct='Conventional'");


			}
			if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Conventional").equalsIgnoreCase(TypeofProduct)&&NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_NAT").equalsIgnoreCase(subprod))&&(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_TKON").equalsIgnoreCase(apptype))){
				//RLOS.mLogger.info( "6Value of AppType is:"+formObject.getNGValue("AppType"));
				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select convert(varchar, SCHEMEDESC) from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Take Over' and TypeofProduct='Conventional'");


			}
			if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Conventional").equalsIgnoreCase(TypeofProduct)&&NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_NAT").equalsIgnoreCase(subprod))&&("NEWN".equalsIgnoreCase(apptype))){
			//	RLOS.mLogger.info( "7Value of AppType is:"+formObject.getNGValue("AppType"));
				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select convert(varchar, SCHEMEDESC) from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'New' and TypeofProduct='Conventional'");


			}
			if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Conventional").equalsIgnoreCase(TypeofProduct)&&NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_NAT").equalsIgnoreCase(subprod))&&(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESC").equalsIgnoreCase(apptype) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESN").equalsIgnoreCase(apptype) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESR").equalsIgnoreCase(apptype))){
			//	RLOS.mLogger.info( "8Value of AppType is:"+formObject.getNGValue("AppType"));
				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select convert(varchar, SCHEMEDESC) from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Reschedulment' and TypeofProduct='Conventional'");


			}
			if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic").equalsIgnoreCase(TypeofProduct))&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_EXP").equalsIgnoreCase(subprod)&&("TKOE".equalsIgnoreCase(apptype))){


			//	RLOS.mLogger.info( "1Value of AppType is:"+formObject.getNGValue("AppType"));


				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select convert(varchar, SCHEMEDESC) from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Take Over'  and TypeofProduct='"+NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic")+"'");				
			}

			if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic").equalsIgnoreCase(TypeofProduct))&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_EXP").equalsIgnoreCase(subprod)&&("NEWE".equalsIgnoreCase(apptype))){
			//	RLOS.mLogger.info( "2Value of AppType is:"+formObject.getNGValue("AppType"));
				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select convert(varchar, SCHEMEDESC) from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'New' and TypeofProduct='"+NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic")+"'");				
			}

			if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic").equalsIgnoreCase(TypeofProduct)&&NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_EXP").equalsIgnoreCase(subprod))&&("TOPE".equalsIgnoreCase(apptype))){
			//	RLOS.mLogger.info( "3Value of AppType is:"+formObject.getNGValue("AppType"));


				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select convert(varchar, SCHEMEDESC) from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Top up' and TypeofProduct='"+NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic")+"'");


			}
			if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic").equalsIgnoreCase(TypeofProduct)&&NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_EXP").equalsIgnoreCase(subprod))&&(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESC").equalsIgnoreCase(apptype) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESN").equalsIgnoreCase(apptype) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESR").equalsIgnoreCase(apptype))){
			//	RLOS.mLogger.info( "4Value of AppType is:"+formObject.getNGValue("AppType"));


				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select convert(varchar, SCHEMEDESC) from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Reschedulment' and TypeofProduct='"+NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic")+"'");


			}
			if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic").equalsIgnoreCase(TypeofProduct)&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_NAT").equalsIgnoreCase(subprod))&&("TOPN".equalsIgnoreCase(apptype))){
			//	RLOS.mLogger.info( "5Value of AppType is:"+formObject.getNGValue("AppType"));
				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select convert(varchar, SCHEMEDESC) from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Top up' and TypeofProduct='"+NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic")+"'");


			}
			if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic").equalsIgnoreCase(TypeofProduct)&&NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_NAT").equalsIgnoreCase(subprod))&&(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_TKON").equalsIgnoreCase(apptype))){
			//	RLOS.mLogger.info( "6Value of AppType is:"+formObject.getNGValue("AppType"));
				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select convert(varchar, SCHEMEDESC) from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Take Over' and TypeofProduct='"+NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic")+"'");


			}
			if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic").equalsIgnoreCase(TypeofProduct)&&NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_NAT").equalsIgnoreCase(subprod))&&("NEWN".equalsIgnoreCase(apptype))){
			//	RLOS.mLogger.info( "7Value of AppType is:"+formObject.getNGValue("AppType"));
				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select convert(varchar, SCHEMEDESC) from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'New' and TypeofProduct='"+NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic")+"'");


			}
			if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic").equalsIgnoreCase(TypeofProduct)&&NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_NAT").equalsIgnoreCase(subprod))&&(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESC").equalsIgnoreCase(apptype) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESN").equalsIgnoreCase(apptype) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESR").equalsIgnoreCase(apptype))){
			//	RLOS.mLogger.info( "8Value of AppType is:"+formObject.getNGValue("AppType"));
				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select convert(varchar, SCHEMEDESC) from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Reschedulment' and TypeofProduct='"+NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic")+"'");

			}
		}


		if ("cmplx_EmploymentDetails_EmpIndusSector".equalsIgnoreCase(pEvent.getSource().getName())){
		
			LoadPickList("cmplx_EmploymentDetails_Indus_Macro", "select '--Select--' union select macro from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where IndustrySector='"+formObject.getNGValue("cmplx_EmploymentDetails_EmpIndusSector")+"' and IsActive='Y'");
		}

		if ("cmplx_EmploymentDetails_Indus_Macro".equalsIgnoreCase(pEvent.getSource().getName())){
			LoadPickList("cmplx_EmploymentDetails_Indus_Micro", "select '--Select--' union select convert(varchar, micro) from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where Macro='"+formObject.getNGValue("cmplx_EmploymentDetails_Indus_Macro")+"' and IsActive='Y'");
		}


		if ("AlternateContactDetails_carddispatch".equalsIgnoreCase(pEvent.getSource().getName())){
			String cardDisp=formObject.getNGValue("AlternateContactDetails_carddispatch");
			//RLOS.mLogger.info(cardDisp);

			if ("Branch".equalsIgnoreCase(cardDisp)){
				formObject.setVisible("AlternateContactDetails_custdomicile",true);
				//Deepak Code change to add requested product filter
				String TypeOfProd=formObject.getNGValue("Product_type");
				LoadPickList("AlternateContactDetails_custdomicile", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock)  where product_type = '"+TypeOfProd+"' order by code");					
			}

		}
		if ("Product_type".equalsIgnoreCase(pEvent.getSource().getName())){

			String ProdType=formObject.getNGValue("Product_type");
			//RLOS.mLogger.info(ProdType);
			formObject.clear("CardProd");
			formObject.setNGValue("CardProd","--Select--");
			if("Conventional".equalsIgnoreCase(ProdType))
				LoadPickList("CardProd", "select '--Select--','' as code union select convert(varchar,description),code from ng_MASTER_CardProduct with (nolock) where code not like '%AMAL%' order by code");


			if("Islamic".equalsIgnoreCase(ProdType))
				LoadPickList("CardProd", "select '--Select--','' as code union select convert(varchar,description),code from ng_MASTER_CardProduct with (nolock) where code like '%AMAL%' order by code");
		}

		// added by abhishek point 44
		if("cmplx_EmploymentDetails_Others".equalsIgnoreCase(pEvent.getSource().getName())){
			if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_true").equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_Others"))){
				formObject.setLocked("EMploymentDetails_Text21", true);
				formObject.setLocked("EMploymentDetails_Text22", true);

			}
			else{
				formObject.setLocked("EMploymentDetails_Text21", false);
				formObject.setLocked("EMploymentDetails_Text22", false);
			}
		}

		//added by abhishek point 64
		/*if(pEvent.getSource().getName().equalsIgnoreCase("cmplx_CC_Loan_TransMode")){


			if(formObject.getNGValue("cmplx_CC_Loan_TransMode").equalsIgnoreCase("chq")){
				RLOS.mLogger.info("");
				formObject.setLocked("cmplx_CC_Loan_mchequeno", false);
				formObject.setLocked("cmplx_CC_Loan_mchequeDate", false);
				formObject.setLocked("cmplx_CC_Loan_mchequestatus", false);
			}


		}*/
	}
	
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		
		//RLOS.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

		try
		{
			switch (pEvent.getType()) 
			{		
			case FRAME_EXPANDED:
				call_Frame_Expanded(pEvent);
				break;

			case FRAME_COLLAPSED: {
				break;
			}

			case TAB_CLICKED:
				RLOS.mLogger.info(formObject.getSheetCaption(pEvent.getSource().getName(), formObject.getSelectedSheet(pEvent.getSource().getName())));
				//Java takes height of entire container in getHeight while js takes current height	i.e collapsed/expanded

				break;

			case FRAGMENT_LOADED:
				call_Fragement_Loaded(pEvent);
				break;

			case VALUE_CHANGED:
				call_Value_Changed(pEvent);
				

				break;
			default:
				break;

			}
		}
		
		finally
		{
			RLOS.mLogger.info( "Exception occurred");
		}
		/*catch(Exception ex)
		{
			RLOS.mLogger.info("Inside Exception to show msg at front end");
			HashMap<String,String> hm1=new HashMap<String,String>();
			hm1.put("Error","Checked");
			if(ex instanceof ValidatorException)
			{   RLOS.mLogger.info("popupFlag value: "+ popupFlag);
			if(popupFlag.equalsIgnoreCase("Y"))

			{
				RLOS.mLogger.info("Inside popup msg through Exception "+ popupFlag);
				if(popUpControl.equals(""))

				{
					RLOS.mLogger.info("Before show Exception at front End "+ popupFlag);
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
				RLOS.mLogger.info("exception in eventdispatched="+ ex);
			}
		}*/
	}	
	public String Convert_dateFormat(String date,String OldFormat,String newFormat)
	{
		//RLOS.mLogger.info( "Inside Convert_dateFormat() method "+date);
		String new_date="";
		if("".equals(date))
		{
			return new_date;
		}
		
		try
		{
			SimpleDateFormat sdf_old=new SimpleDateFormat(OldFormat);
			SimpleDateFormat sdf_new=new SimpleDateFormat(newFormat);
			new_date=sdf_new.format(sdf_old.parse(date));
		}
		catch(Exception e)
		{
			RLOS.mLogger.info( "Exception occurred in Convert_dateFormat method:"+printException(e));
		}
		return new_date;
	}

	public void saveFormStarted(FormEvent pEvent) 
	{
		//RLOS.mLogger.info( "Inside saveFormStarted()" + pEvent);
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setNGValue("CIFID", formObject.getNGValue("cmplx_Customer_CIFNO"));
		//RLOS.mLogger.info( "CIFID is:" +  formObject.getNGValue("cmplx_Customer_CIFNO"));
		formObject.setNGValue("Created_By",formObject.getUserName());
		formObject.setNGValue("PROCESS_NAME", "RLOS");
		formObject.setNGValue("initiationChannel", "Branch_Init");
		
		//formObject.setNGValue("empType", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,6));
		formObject.setNGValue("EmploymentType", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,6));
		formObject.setNGValue("priority_ProdGrid", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,9));
		formObject.setNGValue("Subproduct_productGrid", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,2));


		formObject.setNGValue("Age",formObject.getNGValue("cmplx_Customer_age"));

		String squery = "select VAR_REC_1 from WFINSTRUMENTTABLE with (nolock) where ProcessInstanceID ='"+formObject.getWFWorkitemName()+"' ";
		List<List<String>> outputindex ;
		outputindex = formObject.getNGDataFromDataCache(squery);
		//RLOS.mLogger.info( "outputItemindex is:" +  outputindex);
		String itemIndex =outputindex.get(0).get(0);
		formObject.setNGValue("NewApplicationNo", itemIndex);
		formObject.setNGValue("ApplicationRefNo", itemIndex);
	}

	public void saveFormCompleted(FormEvent pEvent) {
		//RLOS.mLogger.info( "Inside saveFormCompleted()" + pEvent);


		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setNGValue("CIFID", formObject.getNGValue("cmplx_Customer_CIFNO"));
		//RLOS.mLogger.info( "CIFID is:" +  formObject.getNGValue("cmplx_Customer_CIFNO"));
		formObject.setNGValue("Created_By",formObject.getUserName());
		formObject.setNGValue("PROCESS_NAME", "RLOS");
		formObject.setNGValue("initiationChannel", "Branch_Init");


		String squery = "select VAR_REC_1 from WFINSTRUMENTTABLE with (nolock) where ProcessInstanceID ='"+formObject.getWFWorkitemName()+"' ";
		List<List<String>> outputindex ;
		outputindex = formObject.getNGDataFromDataCache(squery);
		//RLOS.mLogger.info( "outputItemindex is:" +  outputindex);
		String itemIndex =outputindex.get(0).get(0);
		formObject.setNGValue("NewApplicationNo", itemIndex);
		formObject.setNGValue("ApplicationRefNo", itemIndex);


	}



	public void submitFormStarted(FormEvent pEvent)
	throws ValidatorException {
		//RLOS.mLogger.info( "Inside submitFormStarted()" + pEvent);
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		
		formObject.setNGValue("loan_Type", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0));
		formObject.setNGValue("email_id", formObject.getUserName()+"@rlos.com");
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DecisionHistory_Decision"));

		String squery = "select VAR_REC_1 from WFINSTRUMENTTABLE with (nolock) where ProcessInstanceID ='"+formObject.getWFWorkitemName()+"' ";
		List<List<String>> outputindex;
		List<List<String>> secondquery;
		outputindex = formObject.getNGDataFromDataCache(squery);
		//RLOS.mLogger.info( "outputItemindex is:" +  outputindex);
		String itemIndex =outputindex.get(0).get(0);
		formObject.setNGValue("NewApplicationNo", itemIndex);
		formObject.setNGValue("ApplicationRefNo", itemIndex);




		//RLOS.mLogger.info( "Value Of decision is:"+formObject.getNGValue("cmplx_DecisionHistory_Decision"));
		
		saveIndecisionGrid();
		TypeOfProduct();//Loan type
		//CIFIDCheck();  
		//tanshu started
		@SuppressWarnings("unused")
		HashMap<String,String> hm1= new HashMap<String,String>();
		String requested_product="";
		String requested_subproduct="";
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		//RLOS.mLogger.info("valu of row count"+n);
		if(n>0)
		{
			for(int i=0;i<n;i++)
			{
				requested_product= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 1);
				//RLOS.mLogger.info(requested_product);
				requested_subproduct= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2);
				//RLOS.mLogger.info(requested_subproduct);

			}
		}
		String sQuery="SELECT Name FROM PDBDocument with(nolock) with(nolock) WHERE DocumentIndex IN (SELECT DocumentIndex FROM PDBDocumentContent with(nolock) WHERE ParentFolderIndex= '"+itemIndex+"')";
		outputindex = null;
		RLOS.mLogger.info( "sQuery for document name is:" +  sQuery);
		outputindex = formObject.getNGDataFromDataCache(sQuery);
		//RLOS.mLogger.info( "outputItemindex is:" +  outputindex);






		if(outputindex==null || !outputindex.isEmpty()) 
		{
			//RLOS.mLogger.info("output index is blank");
			String  query = "SELECT distinct DocName,Mandatory FROM ng_rlos_DocTable with(nolock) WHERE ProductName='"+requested_product+"' and SubProductName='"+requested_subproduct+"' and ProcessName='RLOS'";
			//RLOS.mLogger.info( "sQuery for document name is:" +  query);
			secondquery = formObject.getNGDataFromDataCache(query);
			for(int j = 0; j < secondquery.size(); j++) {
				if("Y".equalsIgnoreCase(secondquery.get(j).get(1))) {
					//throw new ValidatorException(new FacesMessage("You have not attached Mandatory Documents"));
				}
			}
		}



		String Document_Name;
		if(outputindex!=null && !outputindex.isEmpty())
				Document_Name = outputindex.get(0).get(0);
		else
			Document_Name="";
		
		//RLOS.mLogger.info( "Document_Index Document_Name is:" + Document_Name);
		String[] arrval=new String[outputindex.size()];
		if(outputindex != null && !outputindex.isEmpty())
		{
			RLOS.mLogger.info("Staff List "+outputindex);
			for(int i = 0; i < outputindex.size(); i++)
			{
				arrval[i]=outputindex.get(i).get(0);
				//str.append(outputindex.get(i).get(0));
				//str.append(",");
			}
		}
		//RLOS.mLogger.info( " sMap is:" +  str.toString());
		//String arr=str.substring(0, str.length()-1);
		//RLOS.mLogger.info( " arr is:" +  arr);

		//String[] arrval = arr.split(",");
		for(int k=0;k<arrval.length;k++)
		{
			//RLOS.mLogger.info( " arrval is:" +  arrval[k]);
		}


		//RLOS.mLogger.info(requested_product);
		String  query = "SELECT distinct DocName,Mandatory FROM ng_rlos_DocTable with(nolock) WHERE ProductName='"+requested_product+"' and SubProductName='"+requested_subproduct+"' and ProcessName='RLOS'";
		outputindex = null;
		//RLOS.mLogger.info( "sQuery for document name is:" +  query);
		outputindex = formObject.getNGDataFromDataCache(query);
	//	RLOS.mLogger.info( "outputItemindex is:" +  outputindex+"::Size::"+outputindex.size());
		IRepeater repObj;
		repObj = formObject.getRepeaterControl("IncomingDoc_Frame");
		//RLOS.mLogger.info("repObj::"+repObj+"row::+"+repObj.getRepeaterRowCount());
		String [] misseddoc=new String[outputindex.size()];
		for(int j = 0; j < outputindex.size(); j++)
		{
			String DocName =outputindex.get(j).get(0);
			String Mandatory =outputindex.get(j).get(1);
			//RLOS.mLogger.info( "Document_Index Document_Name is:" + DocName+","+Mandatory);

			if (repObj.getRepeaterRowCount() != 0 && "Y".equalsIgnoreCase(Mandatory)) 
			{
				int l=0;
				while(l<arrval.length)
				{
					//RLOS.mLogger.info("DocName::"+DocName+":str:"+arrval[l]);

					if(arrval[l].equalsIgnoreCase(DocName))
					{
						//RLOS.mLogger.info("document is present in the list");
						String StatusValue=repObj.getValue(j, "cmplx_DocName_Status");
						//RLOS.mLogger.info("StatusValue::"+StatusValue);
						if(!"Recieved".equalsIgnoreCase(StatusValue))
						{
							repObj.setValue(j, "cmplx_DocName_Status", "Recieved");
							repObj.setEditable(j, "cmplx_DocName_Status", false);
							//RLOS.mLogger.info("StatusValue::123final"+StatusValue);
						}

						break;
					}
					else
					{
					//	RLOS.mLogger.info("Document is not present in the list");
						misseddoc[j]=DocName;
						l++;
					//	RLOS.mLogger.info( " misseddoc is in j is:" +  misseddoc[j]);

						String StatusValue=repObj.getValue(j, "cmplx_DocName_Status");
						//RLOS.mLogger.info("StatusValue::"+StatusValue);
						String Remarks=repObj.getValue(j, "cmplx_DocName_Remarks");
						//RLOS.mLogger.info("Remarks::"+Remarks);
						if(!("Recieved".equalsIgnoreCase(StatusValue)||"Deferred".equalsIgnoreCase(StatusValue))){
							if("".equalsIgnoreCase(Remarks)||Remarks.equalsIgnoreCase(null)||"null".equalsIgnoreCase(Remarks)){
								RLOS.mLogger.info("As you have not attached the Mandatory Document fill the Remarks");
								throw new ValidatorException(new FacesMessage("As you have not attached the Mandatory Document fill the Remarks"));

							}
							else if(!"".equalsIgnoreCase(Remarks)||Remarks.equalsIgnoreCase(null)||"null".equalsIgnoreCase(Remarks)){
								RLOS.mLogger.info("Proceed further");


							}
						}
					}					
				}
			}
		}
		StringBuilder mandatoryDocName = new StringBuilder("");
		for(int k=0;k<misseddoc.length;k++)
		{
			if(null != misseddoc[k]) {
				mandatoryDocName.append(misseddoc[k]).append(",");
			}
			//RLOS.mLogger.info( "misseddoc is:" +misseddoc[k]);
		}
		mandatoryDocName.setLength(Math.max(mandatoryDocName.length()-1,0));
	//	RLOS.mLogger.info( "misseddoc is:" +mandatoryDocName.toString());
		//throw new ValidatorException(new FacesMessage("You have not attached Mandatory Documents: "+mandatoryDocName.toString()));

		//tanshu ended

	}


	public void submitFormCompleted(FormEvent pEvent)
	throws ValidatorException {
		//RLOS.mLogger.info( "Inside submitFormCompleted()" + pEvent);

	}

	public void continueExecution(String eventHandler, HashMap<String, String> m_mapParams) {
		//RLOS.mLogger.info( "Inside continueExecution()" + eventHandler);
	}


	public void initialize() {
		//RLOS.mLogger.info( "Inside initialize()");
	}

	public String decrypt(String arg0) {
		
		return null;
	}

	public String encrypt(String arg0) {
		
		return null;
	}

}

