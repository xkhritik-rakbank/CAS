//----------------------------------------------------------------------------------------------------
//		NEWGEN SOFTWARE TECHNOLOGIES LIMITED
//Group						: AP2
//Product / Project			: RLOS
//Module					: CAS
//File Name					: RLOSCommonCode.java
//Author					: Deepak Kumar
//Date written (DD/MM/YYYY)	: 06/08/2017
//Description				: 
//----------------------------------------------------------------------------------------------------
//--Historty--
//Deepak Code changes to calculate LPF amount and % 08-nov-2017 start
//----------------------------------------------------------------------------------------------------
package com.newgen.omniforms.user;

import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;

import com.newgen.custom.Common_Utils;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.component.IRepeater;
import java.util.List;

public class RLOSCommonCode extends RLOSCommon{

	private static final long serialVersionUID = 1L;
	FormReference formObject = FormContext.getCurrentInstance().getFormReference();

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Akshay Gupta             
	Description                         :To fetch fragments on form        

	 ***********************************************************************************  */


	public void call_Frame_Expanded(ComponentEvent pEvent)
	{
		RLOS.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		if ("GuarantorDetails".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.fetchFragment("GuarantorDetails", "GuarantorDetails", "q_GuarantorDetails");
		}

		else if ("ProductDetailsLoader".equalsIgnoreCase(pEvent.getSource().getName())) {

			RLOS.mLogger.info("Inside ProductDetailsLoader ");	
			//added Tanshu Aggarwal(23/06/2017)
			formObject.fetchFragment("ProductDetailsLoader", "Product", "q_Product");
			if	("Y".equalsIgnoreCase(formObject.getNGValue("Is_Customer_Req")) && "Y".equalsIgnoreCase(formObject.getNGValue("Is_Account_Create")))
			{
				formObject.setEnabled("ProductDetailsLoader", false);
				RLOS.mLogger.info("Inside ProductDetailsLoader if ");	
			}

		}

		else if ("EmploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) 
		{				

			RLOS.mLogger.info( "Inside EmploymentDetails before fragment load ");	

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
			RLOS.mLogger.info( "Inside EmploymentDetails after fragment load ");	

			/*if(!"SAL".equalsIgnoreCase(formObject.getNGValue("Subproduct_productGrid"))){
				formObject.setVisible("EMploymentDetails_Label59", false);
				formObject.setVisible("cmplx_EmploymentDetails_IndusSeg", false);
			}
			else{
				formObject.setVisible("EMploymentDetails_Label59", true);
				formObject.setVisible("cmplx_EmploymentDetails_IndusSeg", true);
			}---Indus Seg is only visible for surrogate case*/
			
			if("Credit Card".equalsIgnoreCase(formObject.getNGValue("PrimaryProduct"))){
				formObject.setVisible("EMploymentDetails_Label71", false);
				formObject.setVisible("cmplx_EmploymentDetails_EmpContractType", false);
			}
			else{
				formObject.setVisible("EMploymentDetails_Label71", true);
				formObject.setVisible("cmplx_EmploymentDetails_EmpContractType", true);
			}
		
		}		 

		else if ("Incomedetails".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.fetchFragment("Incomedetails", "IncomeDetails", "q_IncomeDetails");
			visibilityFrameIncomeDetails(formObject);	
		}

		else if ("ReferenceDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.fetchFragment("ReferenceDetails", "ReferenceDetails", "q_ReferenceDetails");
			LoadPickList("ReferenceDetails_ref_Relationship", "select convert(varchar, Description),code from NG_MASTER_Relationship union select '--Select--','' order by Code");


		}

		else if ("CompanyDetails".equalsIgnoreCase(pEvent.getSource().getName())) 
		{

			formObject.fetchFragment("CompanyDetails", "CompanyDetails", "q_CompanyDetails");
			//need to be checked 
			String subprod = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2);
			if(subprod!=null && "IM".equalsIgnoreCase(subprod)){
				LoadPickList("MarketingCode","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_MarketingCode where isActive='Y' and subproduct = 'IM' order by code");	
			}
			else{
				LoadPickList("MarketingCode","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_MarketingCode where isActive='Y' and subproduct = '!IM' order by code");
			}
			LoadPickList("PromotionCode","select '--Select--' as Description,'' as code union select convert(varchar, Description),code from NG_MASTER_ReferrorCode where isActive='Y' order by code");
			LoadPickList("ClassificationCode","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_ClassificationCode where isActive='Y' order by code");
			LoadPickList("NEPType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_NEPType with (nolock) order by code");
			LoadPickList("TargetSegmentCode", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_TargetSegmentCode with (nolock) order by code");
			/*	if(formObject.getNGValue("Subproduct_productGrid").equalsIgnoreCase("BTC")){
				formObject.setVisible("CompanyDetails_Label8", true);
				formObject.setVisible("CompanyDetails_effecLOB", true);	

			}--Commented on 6/10/17 as discussed with shashank-Future CR*/
			//need to be checked 

			if("Se".equalsIgnoreCase(formObject.getNGValue("Subproduct_productGrid"))){
				formObject.setVisible("CompanyDetails_Label27", true);
				formObject.setVisible("EOW", true);
				formObject.setVisible("CompanyDetails_Label29", true);
				formObject.setVisible("headOffice", true);
				formObject.setVisible("CompanyDetails_Label28", true);
				formObject.setVisible("visaSponsor", true);

			}
			else{
				formObject.setVisible("CompanyDetails_Label27", false);
				formObject.setVisible("EOW", false);//Emirate of work
				formObject.setVisible("CompanyDetails_Label29", false);
				formObject.setVisible("headOffice", false);
				formObject.setVisible("CompanyDetails_Label28", false);
				formObject.setVisible("visaSponsor", false);
				//formObject.setVisible("CompanyDetails_Label8", false);
				//formObject.setVisible("CompanyDetails_effecLOB", false);
			}

			RLOS.mLogger.info( "CompanyDetailse1:");
		}


		else if ("AuthorisedSignatoryDetails".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.fetchFragment("AuthorisedSignatoryDetails", "AuthorisedSignDetails", "q_AuthorisedSignDetails");
			RLOS.mLogger.info( "AuthorisedSignatoryDetails:");

		}
		else if ("PartnerDetails".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			RLOS.mLogger.info( "Saurabh123456");
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


		//Modified by akshay for NTB/NEP case only
		else if ("DecisionHistoryContainer".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.fetchFragment("DecisionHistoryContainer", "DecisionHistory", "q_DecisionHistory");
			//25/07/2017  for mandatory checks fragments should be fetched first
			int framestate0 = formObject.getNGFrameState("ProductDetailsLoader");
			if(framestate0 == 0)
			{
				RLOS.mLogger.info("ProductDetailsLoader");
			}
			else 
			{
				formObject.fetchFragment("ProductDetailsLoader", "Product", "q_Product");
				RLOS.mLogger.info("fetched product details");
			}

			int framestate1=formObject.getNGFrameState("Incomedetails");
			if(framestate1 == 0)
			{
				RLOS.mLogger.info("Incomedetails");
			}
			else 
			{
				formObject.fetchFragment("Incomedetails", "IncomeDetails", "q_IncomeDetails");
				visibilityFrameIncomeDetails(formObject);
				RLOS.mLogger.info(" fetched income details");
				formObject.setTop("Incomedetails",formObject.getTop("ProductDetailsLoader")+formObject.getHeight("ProductDetailsLoader"));
			}

			int framestate2=formObject.getNGFrameState("EmploymentDetails");
			if(framestate2 == 0){
				RLOS.mLogger.info(" EmploymentDetails");
			}
			else {
				formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmploymentDetails");
				//change by saurabh on 1St Dec for Tanshu points.
				formObject.setTop("Eligibility_Emp", 460);
				formObject.setTop("WorldCheck_fetch", 500);
				RLOS.mLogger.info(" fetched employment details");
			}
			int framestate3=formObject.getNGFrameState("Address_Details_container");
			if(framestate3 == 0){
				RLOS.mLogger.info(" Address_Details_container");
			}
			else {
				formObject.fetchFragment("Address_Details_container", "AddressDetails", "q_AddressDetails");				
				formObject.setTop("Alt_Contact_container",formObject.getTop("Address_Details_Container")+formObject.getHeight("Address_Details_ontainer")+30);
				formObject.setTop("ReferenceDetails",formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container")+30);
				formObject.setTop("CardDetails_container", formObject.getTop("ReferenceDetails")+formObject.getHeight("ReferenceDetails")+30);
				formObject.setTop("FATCA", formObject.getTop("CardDetails")+formObject.getHeight("CardDetails_container")+30);
				formObject.setTop("KYC", formObject.getTop("FATCA")+formObject.getHeight("FATCA")+30);
				formObject.setTop("OECD", formObject.getTop("KYC")+formObject.getHeight("KYC")+30);
				
				/*
				formObject.setTop("Alt_Contact_container",formObject.getTop("ReferenceDetails")+25);
				formObject.setTop("Supplementary_Container",formObject.getTop("ReferenceDetails")+50);
				formObject.setTop("CardDetails", formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container"));
				formObject.setTop("FATCA", formObject.getTop("CardDetails")+30);
				formObject.setTop("KYC", formObject.getTop("FATCA")+20);
				formObject.setTop("OECD", formObject.getTop("KYC")+20);
				*/
				RLOS.mLogger.info(" fetched address details");
			}
			int framestate4=formObject.getNGFrameState("Alt_Contact_container");
			if(framestate4 == 0){
				RLOS.mLogger.info("Alt_Contact_container");
			}
			else {
				formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
				formObject.setTop("Alt_Contact_container",formObject.getTop("Address_Details_Container")+formObject.getHeight("Address_Details_Container")+30);
				formObject.setTop("ReferenceDetails",formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container")+30);
				formObject.setTop("CardDetails_container", formObject.getTop("ReferenceDetails")+formObject.getHeight("ReferenceDetails")+30);
				formObject.setTop("FATCA", formObject.getTop("CardDetails")+formObject.getHeight("CardDetails_container")+30);
				formObject.setTop("KYC", formObject.getTop("FATCA")+formObject.getHeight("FATCA")+30);
				formObject.setTop("OECD", formObject.getTop("KYC")+formObject.getHeight("KYC")+30);
				
				/*
				formObject.setTop("Alt_Contact_container",formObject.getTop("Address_Details_Container")+formObject.getHeight("Address_Details_Container")+70);
				formObject.setTop("Supplementary_Container",formObject.getTop("ReferenceDetails")+250);
				formObject.setTop("CardDetails", formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container")+20);
				formObject.setTop("FATCA", formObject.getTop("CardDetails")+30);
				formObject.setTop("KYC", formObject.getTop("FATCA")+20);
				formObject.setTop("OECD", formObject.getTop("KYC")+20);
				*/
				RLOS.mLogger.info("fetched address details");
			}

			int framestate5=formObject.getNGFrameState("ReferenceDetails");
			if(framestate5 == 0)
			{
				RLOS.mLogger.info("Alt_Contact_container ");
			}
			else {
				formObject.fetchFragment("ReferenceDetails", "ReferenceDetails", "q_ReferenceDetails");
				LoadPickList("ReferenceDetails_ref_Relationship", "select convert(varchar, Description),code from NG_MASTER_Relationship union select '--Select--','' order by Code");
				
				formObject.setTop("ReferenceDetails",formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container")+30);
				formObject.setTop("CardDetails_container", formObject.getTop("ReferenceDetails")+formObject.getHeight("ReferenceDetails")+30);
				formObject.setTop("FATCA", formObject.getTop("CardDetails")+formObject.getHeight("CardDetails")+30);
				formObject.setTop("KYC", formObject.getTop("FATCA")+formObject.getHeight("FATCA")+30);
				formObject.setTop("OECD", formObject.getTop("KYC")+formObject.getHeight("KYC")+30);
				
				/*
				formObject.setTop("ReferenceDetails",formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container"));
				//formObject.setTop("Alt_Contact_container",formObject.getTop("ReferenceDetails")+formObject.getHeight("ReferenceDetails")+25);
				formObject.setTop("Supplementary_Container",formObject.getTop("ReferenceDetails")+250);
				formObject.setTop("CardDetails", formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container"));
				formObject.setTop("FATCA", formObject.getTop("CardDetails")+30);
				formObject.setTop("KYC", formObject.getTop("FATCA")+20);
				formObject.setTop("OECD", formObject.getTop("KYC")+20);
				*/
				RLOS.mLogger.info("fetched address details");
			}
			
			//below code added for card & FATCA to be fetched while opening decision fragment
			int framestate6=formObject.getNGFrameState("CardDetails_container");
			if(framestate6 == 0){
				RLOS.mLogger.info("Alt_Contact_container");
				formObject.setTop("CardDetails_container", formObject.getTop("ReferenceDetails")+formObject.getHeight("ReferenceDetails")+70);
			}
			else {
				formObject.fetchFragment("CardDetails_container", "CardDetails", "q_CardDetails");
				formObject.setTop("CardDetails_container", formObject.getTop("ReferenceDetails")+formObject.getHeight("ReferenceDetails")+70);
				formObject.setTop("FATCA", formObject.getTop("CardDetails")+formObject.getHeight("CardDetails_container")+30);
				formObject.setTop("KYC", formObject.getTop("FATCA")+formObject.getHeight("FATCA")+30);
				formObject.setTop("OECD", formObject.getTop("KYC")+formObject.getHeight("KYC")+30);
				/*
				formObject.setTop("CardDetails", formObject.getHeight("ReferenceDetails")+formObject.getTop("ReferenceDetails")+30);
				formObject.setTop("FATCA", formObject.getHeight("CardDetails")+formObject.getTop("CardDetails")+20);
				formObject.setTop("KYC", formObject.getHeight("FATCA")+formObject.getTop("FATCA")+20);
				formObject.setTop("OECD", formObject.getHeight("KYC")+formObject.getTop("KYC")+20);
				*/
				RLOS.mLogger.info("fetched card details");
			}
			
			int framestate7=formObject.getNGFrameState("FATCA");
			if(framestate7 == 0){
				RLOS.mLogger.info("Alt_Contact_container");
			}
			else {
				formObject.fetchFragment("FATCA", "FATCA", "q_FATCA");
				formObject.setTop("FATCA", formObject.getTop("CardDetails_container")+formObject.getHeight("CardDetails_container")+30);
				formObject.setTop("KYC", formObject.getTop("FATCA")+formObject.getHeight("FATCA")+30);
				formObject.setTop("OECD", formObject.getTop("KYC")+formObject.getHeight("KYC")+30);
				/*
				formObject.setTop("FATCA", formObject.getHeight("CardDetails")+formObject.getTop("CardDetails")+30);
				formObject.setTop("KYC", formObject.getHeight("FATCA")+formObject.getTop("FATCA")+20);
				formObject.setTop("OECD", formObject.getHeight("KYC")+formObject.getTop("KYC")+20);
				*/
				RLOS.mLogger.info("fetched FATCA details");
			}
			//above code added for card & FATCA to be fetched while opening decision fragment

			int framestate8=formObject.getNGFrameState("KYC");
			if(framestate8 == 0){
				RLOS.mLogger.info("Alt_Contact_container");
			}
			else {
				formObject.fetchFragment("KYC", "KYC", "q_KYC");
				formObject.setTop("KYC", formObject.getTop("FATCA")+formObject.getHeight("FATCA")+30);
				formObject.setTop("OECD", formObject.getTop("KYC")+formObject.getHeight("KYC")+30);
				/*
				formObject.setTop("KYC", formObject.getTop("FATCA")+30);
				formObject.setTop("OECD", formObject.getTop("KYC")+20);
				*/
				RLOS.mLogger.info("fetched address details");
			}

			int framestate9=formObject.getNGFrameState("OECD");
			if(framestate9 == 0){
				RLOS.mLogger.info("Alt_Contact_container");
			}
			else {
				formObject.fetchFragment("OECD", "OECD", "q_OECD");
				formObject.setTop("OECD", formObject.getTop("KYC")+formObject.getHeight("KYC")+30);
				RLOS.mLogger.info("fetched address details");
			}

			//framestate8 logic added by saurabh on 7th Oct for JIRA- 2373.
			int framestate10=formObject.getNGFrameState("IncomingDocuments");
			if(framestate10==0){
				RLOS.mLogger.info("Incoming documents already fetched.");
			}
			else{
				formObject.fetchFragment("IncomingDocuments", "IncomingDoc", "q_IncomingDoc");
				formObject.setNGFrameState("IncomingDocuments",0 );
				RLOS.mLogger.info("RLOS Initiation Inside fetchIncomingDocRepeater");
				fetchIncomingDocRepeater();
			}


			//25/07/2017  for mandatory checks fragments should be fetched first

			String countCurrentAccount=formObject.getDataFromDataSource("SELECT count(acctId) from ng_RLOS_CUSTEXPOSE_AcctDetails where AcctType='CURRENT ACCOUNT' AND Wi_Name='"+formObject.getWFWorkitemName()+"' ").get(0).get(0);
			RLOS.mLogger.info(countCurrentAccount);
			formObject.setNGValue("ExistingCustomerAccount",countCurrentAccount);

			String NTB=formObject.getNGValue("NTB");
			String NEP=formObject.getNGValue("NEP");
			RLOS.mLogger.info(NTB+","+NEP);
			//added Tanshu Aggarwal(23/06/2017)
			//if(Product.toUpperCase().contains("PERSONAL LOAN") && formObject.getNGValue("ExistingCustomerAccount").equals("0") && (formObject.getNGValue("NTB").equals("true") || formObject.getNGValue("NEP").equals("true")))
			//Deepak 13 Nov 2017 changes done for create Cif visible
			if("0".equals(countCurrentAccount))
			{
				RLOS.mLogger.info("inside visibility true");
				formObject.setVisible("DecisionHistory_Button3", true);//Create CIF/Acc
			}
			else{
				RLOS.mLogger.info("inside visibility false");
				formObject.setVisible("DecisionHistory_Button3", false);//Create CIFAcc
			}
			//added Tanshu Aggarwal(22/06/2017)
			if	("Y".equalsIgnoreCase(formObject.getNGValue("Is_Customer_Req")) && "Y".equalsIgnoreCase(formObject.getNGValue("Is_Account_Create")))
			{
				RLOS.mLogger.info( "inside customer req Y");
				formObject.setLocked("DecisionHistory_Button3", true);
			}
			else{
				RLOS.mLogger.info( "inside customer req N");
				formObject.setLocked("DecisionHistory_Button3", false);
			}
			//added Tanshu Aggarwal(22/06/2017)		
		}

		else if ("EligibilityAndProductInformation".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			fetch_EligPrd_frag();
		}	

		else if ("Liability_container".equalsIgnoreCase(pEvent.getSource().getName()))
		{                           
			formObject.fetchFragment("Liability_container", "Liability_New", "q_LiabilityNew");  
			RLOS.mLogger.info("RLOSCommon Inside Liability_container:"); 
			int framestatecomp=formObject.getNGFrameState("Alt_Contact_container");
			if(framestatecomp == 1){
				formObject.fetchFragment("CompanyDetails", "CompanyDetails", "q_CompanyDetails");
				formObject.setTop("AuthorisedSignatoryDetails", formObject.getTop("CompanyDetails")+formObject.getHeight("CompanyDetails")+25);

				if("Se".equalsIgnoreCase(formObject.getNGValue("Subproduct_productGrid"))/* && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")*/){
					formObject.setVisible("CompanyDetails_Label27", true);
					formObject.setVisible("EOW", true);//Arun (22/10)
					formObject.setVisible("CompanyDetails_Label29", true);
					formObject.setVisible("headOffice", true); //Arun (22/10)
					formObject.setVisible("CompanyDetails_Label28", true);
					formObject.setVisible("visaSponsor", true);


				}
				else{
					formObject.setVisible("CompanyDetails_Label27", false);
					formObject.setVisible("EOW", false);//Emirate of work Arun (22/10)
					formObject.setVisible("CompanyDetails_Label29", false);
					formObject.setVisible("headOffice", false);//Arun (22/10)
					formObject.setVisible("CompanyDetails_Label28", false);
					formObject.setVisible("visaSponsor", false);
					//formObject.setVisible("CompanyDetails_Label8", false);
					formObject.setVisible("CompanyDetails_effecLOB", false);
				}

			}
			int framestateauth=formObject.getNGFrameState("AuthorisedSignatoryDetails");
			if(framestateauth == 1){
				formObject.fetchFragment("AuthorisedSignatoryDetails", "AuthorisedSignDetails", "q_AuthorisedSignDetails");
				formObject.setTop("PartnerDetails", formObject.getTop("AuthorisedSignatoryDetails")+formObject.getHeight("AuthorisedSignatoryDetails")+25);

			}
			LoadPickList("Liability_New_worstStatus24Months","Select '--Select--' as Description,'' as code union select convert(varchar,Description),code from ng_master_Aecb_Codes with(nolock) order by code");		
			LoadPickList("contractType","Select '--Select--' as Description,'' as code union select convert(varchar,Description),code from ng_master_contract_type with(nolock) order by code");
			RLOS.mLogger.info("RLOSCommon OUTSIDE Liability_container:"); 
		}
		else if ("CC_Loan_container".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.fetchFragment("CC_Loan_container", "CC_Loan", "q_CC_Loan");
		}	
		else if ("Address_Details_container".equalsIgnoreCase(pEvent.getSource().getName()))
		{	
			formObject.fetchFragment("Address_Details_container", "AddressDetails", "q_AddressDetails");	
		}	
		else if ("Alt_Contact_container".equalsIgnoreCase(pEvent.getSource().getName())) 				
		{
			formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
		}
		/*else if (pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails")) {       	
			formObject.fetchFragment("ReferenceDetails", "ReferenceDetails", "q_ReferenceDetails");
		}*/
		else if ("CardDetails_container".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.fetchFragment("CardDetails_container", "CardDetails", "q_CardDetails"); //Arun (30/11/17) modified container name of card details
			//int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			//for(int i=0;i<n;i++){
			/*if("Personal Loan".equalsIgnoreCase(formObject.getNGValue("PrimaryProduct"))){
				formObject.setVisible("CardDetails_Label7", true);
				formObject.setVisible("cmplx_CardDetails_statCycle", true);
				formObject.setVisible("CardDetails_Label3", false);
				formObject.setVisible("cmplx_CardDetails_CompanyEmbossingName", false);//added by akshay on 11/9/17
				formObject.setLeft("CardDetails_Label5", 288);
				formObject.setLeft("cmplx_CardDetails_SuppCardReq", 288);
			}	
			else if("Credit Card".equalsIgnoreCase(formObject.getNGValue("PrimaryProduct"))){
				formObject.setLeft("CardDetails_Label5", 552);
				formObject.setLeft("cmplx_CardDetails_SuppCardReq", 552);
			}*/
			
			/*if("Islamic".equalsIgnoreCase(formObject.getNGValue("LoanType_Primary"))){
				formObject.setVisible("CardDetails_Label2", true);
				formObject.setVisible("cmplx_CardDetails_CharityOrg", true);
				formObject.setVisible("CardDetails_Label4", true);
				formObject.setVisible("cmplx_CardDetails_CharityAmount", true);
				formObject.setLeft("CardDetails_Label5", 1059);
				formObject.setLeft("cmplx_CardDetails_SuppCardReq", 1059);
			}*/	
		}
		else if ("Supplementary_Container".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.fetchFragment("Supplementary_Container", "SupplementCardDetails", "q_SuppCardDetails");
			if ("Salaried pensioner".equalsIgnoreCase(formObject.getNGValue("empType")) || "Salaried".equalsIgnoreCase(formObject.getNGValue("empType"))){
				formObject.setVisible("CompEmbName", false);
				formObject.setVisible("SupplementCardDetails_Label7", false);
			}
		}	
		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.fetchFragment("FATCA", "FATCA", "q_FATCA");
		}	
		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.fetchFragment("KYC", "KYC", "q_KYC");
		}	
		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.fetchFragment("OECD", "OECD", "q_OECD");
		}	
	
		else if ("IncomingDocuments".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			int framestate0 = formObject.getNGFrameState("ProductDetailsLoader");
			if(framestate0 == 0){
				RLOS.mLogger.info("ProductDetailsLoader");
			}
			else {
				formObject.fetchFragment("ProductDetailsLoader", "Product", "q_Product");
				RLOS.mLogger.info("fetched product details");
				formObject.setTop("Incomedetails",formObject.getTop("ProductDetailsLoader")+formObject.getHeight("ProductDetailsLoader"));
			}
			int framestate1=formObject.getNGFrameState("Incomedetails");
			if(framestate1 == 0){
				RLOS.mLogger.info("Incomedetails");
			}
			else {
				formObject.fetchFragment("Incomedetails", "IncomeDetails", "q_IncomeDetails");
				RLOS.mLogger.info("fetched income details");
				formObject.setTop("Incomedetails",formObject.getTop("ProductDetailsLoader")+formObject.getHeight("ProductDetailsLoader"));
				visibilityFrameIncomeDetails(formObject);
			}
			RLOS.mLogger.info("IncomingDocuments");
			formObject.fetchFragment("IncomingDocuments", "IncomingDoc", "q_IncomingDoc");
			RLOS.mLogger.info("fetchIncomingDocRepeater");
			fetchIncomingDocRepeater();
			RLOS.mLogger.info("formObject.fetchFragment1");
		}
		//added by yash on 23/8/2017
		if ("Notepad_Values".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Notepad_Values","Clicked");
			popupFlag="N";
			formObject.fetchFragment("Notepad_Values", "NotepadDetails", "q_Note");
			LoadPickList("NotepadDetails_notedesc", "select '--Select--' as description union select  description from ng_master_notedescription order by description");
			formObject.setNGValue("NotepadDetails_notedesc", "--Select--");
		}
		//ended by yash on 23/8/2017
	}

	
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Saurabh Gupta             
	Description                         :To load income Details        

	 ***********************************************************************************  */


	public void visibilityFrameIncomeDetails(FormReference formObject) {

		if("Credit Card".equalsIgnoreCase(formObject.getNGValue("PrimaryProduct")) /*&& formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")*/){
			//SKLogger.writeLog("RLOS @AKSHAY", "Grid Data[1][9] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9));
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
		String EmpType=formObject.getNGValue("empType");
		RLOS.mLogger.info("RLOS Emp Type Value is:"+EmpType);

		if("Salaried".equalsIgnoreCase(EmpType)|| "Salaried Pensioner".equalsIgnoreCase(EmpType))
		{
			formObject.setVisible("IncomeDetails_Frame3", false);
			formObject.setHeight("Incomedetails", 730);
			formObject.setHeight("IncomeDetails_Frame1", 680);	
			if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
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

		else if("Self Employed".equalsIgnoreCase(EmpType))
		{							
			formObject.setVisible("IncomeDetails_Frame2", false);
			formObject.setTop("IncomeDetails_Frame3",40);
			formObject.setHeight("Incomedetails", 430);
			formObject.setHeight("IncomeDetails_Frame1", 400);
			if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
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

	}

	/*          Function Header:

	 **********************************************************************************

         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


Date Modified                       : 6/08/2017              
Author                              : Akshay Gupta             
Description                         :To handle mouse click events        

	 ***********************************************************************************  */


	public void call_Mouse_Clicked(ComponentEvent pEvent)
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		RLOS_IntegrationInput GenXml=new RLOS_IntegrationInput();
		Common_Utils common=new Common_Utils(RLOS.mLogger);
		String outputResponse = "";
		String ReturnCode="" ;
		String alert_msg="";
		String OTPStatus;
		String SystemErrorCode;
		String BlacklistFlag;
		String DuplicationFlag;

		/*if(pEvent.getSource().getName().equalsIgnoreCase("existingOldCustomer"))

			{
				if(formObject.getNGValue("existingOldCustomer").equalsIgnoreCase("true"))
				{
					SKLogger.writeLog("RLOS", "On click existing old customer !!");
					formObject.setNGValue("NewApplicationNo", formObject.getWFFolderId());
				}

			}

			 commented by AKshay on 16/9/17 as it is not required*/


		if("ReferenceDetails_Reference_Add".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setNGValue("ReferenceDetails_reference_wi_name",formObject.getWFWorkitemName());
			RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("reference_wi_name"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_ReferencDetails_Cmplx_gr_ReferenceDetails");
		}
		else  if("ReferenceDetails_Reference__modify".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_ReferencDetails_Cmplx_gr_ReferenceDetails");
		}
		else  if("ReferenceDetails_Reference_delete".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_ReferencDetails_Cmplx_gr_ReferenceDetails");
		}
		else  if("GuarantorDetails_add".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setNGValue("guarantor_WIName",formObject.getWFWorkitemName());
			RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("guarantor_WIName"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_GuarantorDetails_cmplx_GuarantorGrid");
		}
		else  if("GuarantorDetails_modify".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_GuarantorDetails_cmplx_GuarantorGrid");
		}
		else  if("GuarantorDetails_delete".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_GuarantorDetails_cmplx_GuarantorGrid");
		}


		else if ("Add".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			RLOS.mLogger.info( "Inside add button: aman123"+formObject.getNGValue("ReqProd"));

			formObject.setNGValue("Grid_wi_name",formObject.getWFWorkitemName());
			RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("Grid_wi_name"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Product_cmplx_ProductGrid");

			//tanshu aggarwal for documents(1/06/2017)
			IRepeater repObj;
			repObj = formObject.getRepeaterControl("IncomingDoc_Frame");
			RLOS.mLogger.info("before clear");
			boolean Visibility=formObject.isVisible("IncomingDoc_Frame");
			RLOS.mLogger.info("before clear"+Visibility);
			if(Visibility == true){
				repObj.clear();
				repObj.refresh();
				//formObject.setNGFrameState("IncomingDocuments", 1);
				fetchIncomingDocRepeater();
				RLOS.mLogger.info("after doc fun");


			}

			else {
				String query = "Delete FROM ng_rlos_IncomingDoc WHERE  wi_name='"+formObject.getWFWorkitemName()+"'";
				RLOS.mLogger.info( "when row count is  zero inside else"+query);
				RLOS.mLogger.info( "when row count is  zero inside else after deleting the values");

			}
			RLOS.mLogger.info("after refresh");
			//tanshu aggarwal for documents(1/06/2017)

			AddProducts();
			AddPrimaryData();
			ParentToChild();
			formObject.setNGValue("empType", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,6));
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

			formObject.setVisible("Product_Label18",false);
			formObject.setVisible("Product_Label21",false); 
			formObject.setVisible("Product_Label22",false); 
			formObject.setVisible("Product_Label23",false); 
			formObject.setVisible("Product_Label24",false);	
			formObject.setVisible("Product_Label16",false); 
			formObject.setVisible("LimitAcc",false); 									
			formObject.setVisible("Product_Label10", false);
			formObject.setVisible("Product_Label11", false);
			formObject.setVisible("Product_Label9", false);
			formObject.setVisible("Product_Label7", false);
			formObject.setVisible("LastPermanentLimit", false);
			formObject.setVisible("LastTemporaryLimit", false);
			formObject.setVisible("ExistingTempLimit", false);
			formObject.setVisible("Limit_Expdate", false);
			formObject.setVisible("Product_Label8", false);
			formObject.setVisible("typeReq", false);
			formObject.setVisible("FDAmount", false);

			formObject.setVisible("Product_Label15", false);

			formObject.setNGFrameState("Incomedetails", 1);
			formObject.setNGFrameState("EmploymentDetails", 1);
			formObject.setNGFrameState("EligibilityAndProductInformation", 1);
			formObject.setNGFrameState("Alt_Contact_container", 1);
			formObject.setNGFrameState("CC_Loan_container", 1);
			formObject.setNGFrameState("CompanyDetails", 1);
			formObject.setNGFrameState("CardDetails_container", 1);
			formObject.setNGFrameState("Liability_container", 1);
			Fields_ApplicationType_Employment();
			loadPicklist_TargetSegmentCode();
		}	




		else if ("Modify".equalsIgnoreCase(pEvent.getSource().getName())){

			RLOS.mLogger.info( "Inside add button: aman123"+formObject.getNGValue("ReqProd"));

			if("Credit Card".equalsIgnoreCase(formObject.getNGValue("ReqProd"))){
				formObject.setNGValue("Scheme","");
			}
			else if("Personal Loan".equalsIgnoreCase(formObject.getNGValue("ReqProd"))){
				formObject.setNGValue("CardProd","");
			}

			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Product_cmplx_ProductGrid");



			//tanshu aggarwal for documents(1/06/2017)
			RLOS.mLogger.info("after creating repeater object00");
			IRepeater repObj;
			repObj = formObject.getRepeaterControl("IncomingDoc_Frame");
			boolean Visibility=formObject.isVisible("IncomingDoc_Frame");
			RLOS.mLogger.info("before clear"+Visibility);
			if(Visibility == true){
				repObj.clear();
				repObj.refresh();
				fetchIncomingDocRepeater();
				RLOS.mLogger.info("after doc fun");
				//formObject.setNGFrameState("IncomingDocuments", 1);


			}

			else {
				String query = "Delete FROM ng_rlos_IncomingDoc WHERE  wi_name='"+formObject.getWFWorkitemName()+"'";
				RLOS.mLogger.info( "when row count is  zero inside else"+query);
				formObject.getNGDataFromDataCache(query);
				RLOS.mLogger.info( "when row count is  zero inside else after deleting the values123");


			}

			RLOS.mLogger.info("after creating repeater object00");
			//tanshu aggarwal for documents(1/06/2017)


			AddProducts();
			AddPrimaryData();
			ParentToChild();
			//setting insurance fields by saurabh on 11th oct.
			//setChargesFields();//Commented by Deepak new line of code written in check re-eligibility click event.
			formObject.setNGValue("empType", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,6));
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



			formObject.setVisible("Product_Label18",false);
			formObject.setVisible("Product_Label21",false); 
			formObject.setVisible("Product_Label22",false); 
			formObject.setVisible("Product_Label23",false); 
			formObject.setVisible("Product_Label24",false);

			formObject.setVisible("Product_Label16",false); 
			formObject.setVisible("LimitAcc",false); 									
			formObject.setVisible("Product_Label10", false);
			formObject.setVisible("Product_Label11", false);
			formObject.setVisible("Product_Label9", false);
			formObject.setVisible("Product_Label7", false);
			formObject.setVisible("LastPermanentLimit", false);
			formObject.setVisible("LastTemporaryLimit", false);
			formObject.setVisible("ExistingTempLimit", false);
			formObject.setVisible("Limit_Expdate", false);
			formObject.setVisible("Product_Label8", false);
			formObject.setVisible("typeReq", false);
			formObject.setVisible("FDAmount", false);

			formObject.setVisible("Product_Label15", false);
			formObject.setNGFrameState("Incomedetails", 1);
			formObject.setNGFrameState("EmploymentDetails", 1);
			formObject.setNGFrameState("EligibilityAndProductInformation", 1);
			formObject.setNGFrameState("Alt_Contact_container", 1);
			formObject.setNGFrameState("CC_Loan_container", 1);
			formObject.setNGFrameState("CompanyDetails", 1);
			formObject.setNGFrameState("CardDetails_container", 1);
			formObject.setNGFrameState("Liability_container", 1);
			Fields_ApplicationType_Employment();
			loadPicklist_TargetSegmentCode();//added by akshay on 24/11/17

			if("Personal Loan".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 1))){
				//	Eligibility_UnHide();
				Eligibilityfields();
			}
			else{
				Eligibilityfields();
			}
			formObject.setNGFocus("Modify");//set focus to the same button which was clicked
			//code by saurabh on 8th nov 2017.		
			if(formObject.isVisible("Liability_New_Frame1")){
				if("Self Employed".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 6))){
					formObject.setVisible("cmplx_Liability_New_AECBCompanyconsentAvail", true);
				}
				else{
					formObject.setVisible("cmplx_Liability_New_AECBCompanyconsentAvail", false);
				}
			}
			//code by saurabh on 10th nov 2017.
			if(formObject.isVisible("AltContactDetails_Frame1")){
				String TypeOfProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0)==null?"":formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0);
				LoadPickList("AlternateContactDetails_custdomicile", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock)  where product_type = '"+TypeOfProd+"' order by code");
			}
		}

		else if ("Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_Product_cmplx_ProductGrid");

			//tanshu aggarwal for documents(1/06/2017)
			RLOS.mLogger.info("before creating repeater object22");


			RLOS.mLogger.info("before creating repeater object22 after saving the fragment12");
			IRepeater repObj;
			repObj = formObject.getRepeaterControl("IncomingDoc_Frame");
			boolean Visibility=formObject.isVisible("IncomingDoc_Frame");
			RLOS.mLogger.info("before clear"+Visibility);
			if(Visibility == true){
				repObj.clear();
				repObj.refresh();
				formObject.setNGFrameState("IncomingDocuments", 1);
				RLOS.mLogger.info("after doc fun");
				RLOS.mLogger.info("before clear setframestate");
			}


			else {
				String query = "Delete FROM ng_rlos_IncomingDoc WHERE  wi_name='"+formObject.getWFWorkitemName()+"'";
				RLOS.mLogger.info( "when row count is  zero inside else"+query);
				formObject.getNGDataFromDataCache(query);
				RLOS.mLogger.info( "when row count is  zero inside else after deleting the values456");
			}
			RLOS.mLogger.info("after creating repeater object22");
			//tanshu aggarwal for documents(1/06/2017)

			AddProducts();
			AddPrimaryData();
			ParentToChild();
			formObject.setNGValue("empType", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,6));

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

			formObject.setVisible("Product_Label18",false);
			formObject.setVisible("Product_Label21",false); 
			formObject.setVisible("Product_Label22",false); 
			formObject.setVisible("Product_Label23",false); 
			formObject.setVisible("Product_Label24",false);
			formObject.setVisible("Product_Label18",false);
			formObject.setVisible("Product_Label21",false); 
			formObject.setVisible("Product_Label22",false); 
			formObject.setVisible("Product_Label23",false); 
			formObject.setVisible("Product_Label24",false);

			formObject.setVisible("Product_Label16",false); 
			formObject.setVisible("LimitAcc",false); 									
			formObject.setVisible("Product_Label10", false);
			formObject.setVisible("Product_Label11", false);
			formObject.setVisible("Product_Label9", false);
			formObject.setVisible("Product_Label7", false);
			formObject.setVisible("LastPermanentLimit", false);
			formObject.setVisible("LastTemporaryLimit", false);
			formObject.setVisible("ExistingTempLimit", false);
			formObject.setVisible("Limit_Expdate", false);
			formObject.setVisible("Product_Label8", false);
			formObject.setVisible("typeReq", false);
			formObject.setVisible("FDAmount", false);

			formObject.setNGFrameState("Incomedetails", 1);
			formObject.setNGFrameState("EmploymentDetails", 1);
			formObject.setNGFrameState("EligibilityAndProductInformation", 1);
			formObject.setNGFrameState("Alt_Contact_container", 1);
			formObject.setNGFrameState("CC_Loan_container", 1);
			formObject.setNGFrameState("CompanyDetails", 1);
			formObject.setNGFrameState("CardDetails_container", 1);
		}

		else if("IncomeDetails_Add".equalsIgnoreCase(pEvent.getSource().getName()))	
		{
			formObject.setNGValue("IncomeDetails_wi_name",formObject.getWFWorkitemName());
			RLOS.mLogger.info("RLO Inside add button: "+formObject.getNGValue("IncomeDetails_wi_name"));
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
		else if("CompanyDetails_Add".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setNGValue("company_winame",formObject.getWFWorkitemName());
			RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("company_winame"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_CompanyDetails_cmplx_CompanyGrid");

		}

		else  if("CompanyDetails_Modify".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_CompanyDetails_cmplx_CompanyGrid");

		}

		else  if("CompanyDetails_delete".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_CompanyDetails_cmplx_CompanyGrid");


		}

		else  if("PartnerDetails_add".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setNGValue("partner_winame",formObject.getWFWorkitemName());
			RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("partner_winame"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_PartnerDetails_cmplx_partnerGrid");


		}

		else  if("PartnerDetails_modify".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_PartnerDetails_cmplx_partnerGrid");

		}

		else  if("PartnerDetails_delete".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_PartnerDetails_cmplx_partnerGrid");

		}

		else  if("AuthorisedSignDetails_add".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setNGValue("AuthorisedSign_wiName",formObject.getWFWorkitemName());
			RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("AuthorisedSign_wiName"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails");

		}

		else  if("AuthorisedSignDetails_modify".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails");

		}

		else  if("AuthorisedSignDetails_delete".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails");
		}

		else if ("Liability_New_Add".equalsIgnoreCase(pEvent.getSource().getName())){					
			formObject.setNGValue("LiabilityAddition_wiName",formObject.getWFWorkitemName());
			RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("LiabilityAddition_wiName"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
		}


		else if ("Liability_New_modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
		}


		else if ("Liability_New_delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
		}	


		else if ("addr_Add".equalsIgnoreCase(pEvent.getSource().getName())  ){
			RLOS.mLogger.info( "Inside addredd grid add button");
			//	boolean flag_addressType= Address_Validate();
			//if(flag_addressType){
			formObject.setNGValue("address_Wi_name",formObject.getWFWorkitemName());
			RLOS.mLogger.info( "Inside address button: "+formObject.getNGValue("address_Wi_name"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_AddressDetails_cmplx_AddressGrid");
			//}

		}

		else if ("addr_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			boolean flag_addressType= Address_Validate();
			if(flag_addressType)
				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_AddressDetails_cmplx_AddressGrid");


		}


		else if ("addr_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_AddressDetails_cmplx_AddressGrid");


		}


		else if ("SupplementCardDetails_add".equalsIgnoreCase(pEvent.getSource().getName())){					
			formObject.setNGValue("supplement_WiName",formObject.getWFWorkitemName());
			RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("supplement_WiName"));
			formObject.ExecuteExternalCommand("NGAddRow", "SupplementCardDetails_cmplx_SupplementGrid");
		}


		else if ("SupplementCardDetails_modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "SupplementCardDetails_cmplx_SupplementGrid");
		}


		else if ("SupplementCardDetails_delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "SupplementCardDetails_cmplx_SupplementGrid");
		}	


		else if ("OECD_add".equalsIgnoreCase(pEvent.getSource().getName())){					
			formObject.setNGValue("OECD_winame",formObject.getWFWorkitemName());
			RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("OECD_winame"));
			//below code by saurabh on 22md july 17.
			formObject.setEnabled("OECD_noTinReason",true);
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_OECD_cmplx_GR_OecdDetails");
		}


		else if ("OECD_modify".equalsIgnoreCase(pEvent.getSource().getName()) || "OECD_Button2".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_OECD_cmplx_GR_OecdDetails");
		}


		else if ("OECD_delete".equalsIgnoreCase(pEvent.getSource().getName()) || "OECD_Button3".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_OECD_cmplx_GR_OecdDetails");
		}	

		//added by akshay on 15/9/17 to add data in BT Grid
		else if ("BT_Add".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setNGValue("CC_Loan_wi_name",formObject.getWFWorkitemName());
			RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("CC_Loan_wi_name"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_CC_Loan_cmplx_btc");
		}

		else if ("BT_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_CC_Loan_cmplx_btc");
		}

		else if ("BT_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_CC_Loan_cmplx_btc");
		}

		//ended by akshay on 15/9/17 to add data in BT Grid




		else if("ReadFromCard".equalsIgnoreCase(pEvent.getSource().getName())){
			popupFlag = "Y";
			outputResponse = GenXml.GenerateXML("EID_Genuine","");
			ReturnCode =  (outputResponse.contains("<ns3:ServiceStatusCode>")) ? outputResponse.substring(outputResponse.indexOf("<ns3:ServiceStatusCode>")+"</ns3:ServiceStatusCode>".length()-1,outputResponse.indexOf("</ns3:ServiceStatusCode>")):"";
			String Returndesc = (outputResponse.contains("<ns3:ServiceStatusDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ns3:ServiceStatusDesc>")+"</ns3:ServiceStatusDesc>".length()-1,outputResponse.indexOf("</ns3:ServiceStatusDesc>")):"";
			//  ReturnCode="123";
			RLOS.mLogger.info(ReturnCode);
			RLOS.mLogger.info(Returndesc);
			if("s".equalsIgnoreCase(ReturnCode) && "Valid".equalsIgnoreCase(Returndesc)){
				RLOS_IntegrationOutput.valueSetIntegration(outputResponse, "");  
				formObject.setNGValue("Is_EID_Genuine", "Y");
				formObject.setNGValue("cmplx_Customer_IsGenuine", true);
				RLOS.mLogger.info("EID is generated");
				alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL002");
				throw new ValidatorException(new FacesMessage(alert_msg));
			}   
			else{
				alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL003");
				formObject.setNGValue("Is_EID_Genuine", "N");
				RLOS.mLogger.info("EID is not generated");
			}

			RLOS.mLogger.info(formObject.getNGValue("Is_EID_Genuine"));					
		}    


		else if("FetchDetails".equalsIgnoreCase(pEvent.getSource().getName()))
		{  
			formObject = FormContext.getCurrentInstance().getFormReference();

			formObject.setNGValue("cmplx_Customer_ISFetchDetails", "Y");
			RLOS.mLogger.info( "Fetch Detail Started");
			String cif_no = formObject.getNGValue("cmplx_Customer_CIFNO");
			RLOS.mLogger.info( "Value of cif_id_avail"+cif_no);
			if("".equalsIgnoreCase(cif_no)){ 				
				RLOS.mLogger.info( "Value of cif_id_avail is false"+cif_no);
				String EID = formObject.getNGValue("cmplx_Customer_EmiratesID");
				RLOS.mLogger.info( "EID value for Entity Details: "+EID );
				String ReadFrom_card_exc = formObject.getNGValue("cmplx_Customer_readfromcardflag");
				if( EID!=null && !"".equalsIgnoreCase(EID)&& "Y".equalsIgnoreCase(ReadFrom_card_exc))
				{							
					outputResponse = GenXml.GenerateXML("ENTITY_DETAILS","Primary_CIF");
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					RLOS.mLogger.info(ReturnCode);
					if("0000".equalsIgnoreCase(ReturnCode)){
						formObject.setNGValue("Is_Entity_Details","Y");
						RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "Primary_CIF");

					}
					else{
						formObject.setNGValue("Is_Entity_Details","N");

					}
					RLOS.mLogger.info(formObject.getNGValue("Is_Entity_Details"));							
				}
				outputResponse =GenXml.GenerateXML("CUSTOMER_ELIGIBILITY","Primary_CIF");
				RLOS.mLogger.info("Customer Eligibility");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				RLOS.mLogger.info(ReturnCode);
				if("0000".equalsIgnoreCase(ReturnCode) )
				{
					setcustomer_enable();
					popupFlag="Y";
					RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "Primary_CIF");
					parse_cif_eligibility(outputResponse,"Primary_CIF");					
					formObject.setNGValue("NTB","true");//added by Akshay
					formObject.setNGValue("Is_Customer_Eligibility","Y");
					String NTB_flag = formObject.getNGValue("cmplx_Customer_NTB");
					//Customer_enable();
					// if(NTB_flag.equalsIgnoreCase("true")){
					//  Customer_enable();

					//}
					if("false".equalsIgnoreCase(NTB_flag)){
						// parse_cif_eligibility(outputResponse,"Primary_CIF");
						formObject.setHeight("Customer_Frame1", 620);
						formObject.setHeight("CustomerDetails", 700);	
						//Code change to run Customer details if customer is existing customer in Customer Eligibility start (27-sept-2017)
						String cif = formObject.getNGValue("cmplx_Customer_CIFNO");
						if(cif!=null && !"".equalsIgnoreCase(cif)){
							alert_msg =  fetch_cust_details_primary();
						}
						//Code change to run Customer details if customer is existing customer in Customer Eligibility End (27-sept-2017)
					}

					else if("true".equalsIgnoreCase(NTB_flag)){
						formObject.setVisible("Customer_Frame2", false);
						formObject.setEnabled("cmplx_Customer_VIsaExpiry", true);
						formObject.setLocked("cmplx_Customer_VisaNo", false);
						RLOS.mLogger.info( "inside Customer Eligibility to through Exception to Exit:");
						alert_msg = NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL004");
						throw new ValidatorException(new FacesMessage(alert_msg));
						//added By Akshay
					}
					
						//ended By akshay		

					/*
				else{
					alert_msg = fetch_cust_details_primary();
				}
					 */


					RLOS.mLogger.info(formObject.getNGValue("Is_Customer_Eligibility"));
					RLOS.mLogger.info(formObject.getNGValue("BlacklistFlag"));
					RLOS.mLogger.info(formObject.getNGValue("DuplicationFlag"));
					RLOS.mLogger.info(formObject.getNGValue("IsAcctCustFlag"));

					if("Y".equalsIgnoreCase(formObject.getNGValue("Is_Customer_Eligibility")) && "Y".equalsIgnoreCase(formObject.getNGValue("Is_Customer_Details")))
					{ 
						RLOS.mLogger.info("inside if condition");
						RLOS.mLogger.info( "Customer Eligibility and Customer details fetched sucessfully");
						formObject.setEnabled("FetchDetails", false); 
						formObject.setEnabled("Customer_Button1", false);
						alert_msg = NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL006");
					}
					else{
						RLOS.mLogger.info( "Customer Eligibility and Customer details failed");
						alert_msg = NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL007");
						formObject.setEnabled("FetchDetails", true);
						formObject.setEnabled("Customer_Button1", false);
					}
					RLOS.mLogger.info("");
					formObject.RaiseEvent("WFSave");

					popupFlag="Y";
					RLOS.mLogger.info( "Alert msg to be displayed on screen: "+alert_msg);
					throw new ValidatorException(new FacesMessage(alert_msg));



				}
				else{
					formObject.setNGValue("Is_Customer_Eligibility","N");
					popupFlag="Y";
					alert_msg = NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL005");
					throw new ValidatorException(new FacesMessage(alert_msg));
				}
			}


			else{
				alert_msg = fetch_cust_details_primary();
				throw new ValidatorException(new FacesMessage(alert_msg));

			}

		}


		else if("Customer_Button1".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			//if("Is_Entity_Details".equalsIgnoreCase("Y") && "Is_Customer_Details".equalsIgnoreCase("Y")){
			String NegatedFlag;
			popupFlag="Y";
			outputResponse =GenXml.GenerateXML("CUSTOMER_ELIGIBILITY","Primary_CIF");
			RLOS.mLogger.info("Customer Eligibility");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			RLOS.mLogger.info(ReturnCode);
			formObject.setNGValue("Is_Customer_Eligibility","Y");


			if("0000".equalsIgnoreCase("0000")){
				RLOS_IntegrationOutput.valueSetIntegration(outputResponse,"Primary_CIF"); 
				parse_cif_eligibility(outputResponse,"Primary_CIF");
				BlacklistFlag= (outputResponse.contains("<BlacklistFlag>")) ? outputResponse.substring(outputResponse.indexOf("<BlacklistFlag>")+"</BlacklistFlag>".length()-1,outputResponse.indexOf("</BlacklistFlag>")):"";
				RLOS.mLogger.info("Customer is BlacklistedFlag"+BlacklistFlag);
				DuplicationFlag= (outputResponse.contains("<DuplicationFlag>")) ? outputResponse.substring(outputResponse.indexOf("<DuplicationFlag>")+"</DuplicationFlag>".length()-1,outputResponse.indexOf("</DuplicationFlag>")):"";
				RLOS.mLogger.info("Customer is DuplicationFlag"+DuplicationFlag);
				NegatedFlag= (outputResponse.contains("<NegatedFlag>")) ? outputResponse.substring(outputResponse.indexOf("<NegatedFlag>")+"</NegatedFlag>".length()-1,outputResponse.indexOf("</NegatedFlag>")):"";
				RLOS.mLogger.info("Customer is NegatedFlag"+NegatedFlag);
				formObject.setNGValue("Is_Customer_Eligibility","Y");
				RLOS.mLogger.info(formObject.getNGValue("Is_Customer_Eligibility"));
				formObject.setNGValue("BlacklistFlag",BlacklistFlag);
				formObject.setNGValue("DuplicationFlag",DuplicationFlag);
				formObject.setNGValue("IsAcctCustFlag",NegatedFlag);
				String NTB_flag = formObject.getNGValue("cmplx_Customer_NTB");
				if("true".equalsIgnoreCase(NTB_flag)){
					RLOS.mLogger.info( "inside Customer Eligibility to through Exception to Exit:");
					alert_msg = NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL004");
				}
				else{
					formObject.setHeight("Customer_Frame1", 620);
					formObject.setHeight("CustomerDetails", 700);
					alert_msg = NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL008");
				}

			}
			else{
				formObject.setNGValue("Is_Customer_Eligibility","N");
				formObject.RaiseEvent("WFSave");
			}
			RLOS.mLogger.info(formObject.getNGValue("Is_Customer_Eligibility"));
			RLOS.mLogger.info(formObject.getNGValue("BlacklistFlag"));
			RLOS.mLogger.info(formObject.getNGValue("DuplicationFlag"));
			RLOS.mLogger.info(formObject.getNGValue("IsAcctCustFlag"));
			formObject.RaiseEvent("WFSave");
			throw new ValidatorException(new FacesMessage(alert_msg));
			//}
		}
		//Customer Details Call on Dedupe Summary Button as well(Tanshu Aggarwal-29/05/2017)

		//Fetch Details call in Supplementary details tab by saurabh on 11th September.
		else if("SupplementCardDetails_FetchDetails".equalsIgnoreCase(pEvent.getSource().getName())){

			String EmiratesID = formObject.getNGValue("SupplementCardDetails_Text1");
			RLOS.mLogger.info( "EID value for Entity Details for Supplementary Cards: "+EmiratesID);
			String primaryCif = null;
			boolean isEntityDetailsSuccess = false;

			if( EmiratesID!=null && !"".equalsIgnoreCase(EmiratesID)){
				outputResponse = GenXml.GenerateXML("ENTITY_DETAILS","Supplementary_Card_Details");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				RLOS.mLogger.info(ReturnCode);
				if("0000".equalsIgnoreCase(ReturnCode)){
					//RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "Supplementary_Card_Details");
					primaryCif = (outputResponse.contains("<CIFID>")) ? outputResponse.substring(outputResponse.indexOf("<CIFID>")+"</CIFID>".length()-1,outputResponse.indexOf("</CIFID>")):"";
					formObject.setNGValue("Supplementary_CIFNO",primaryCif);
					isEntityDetailsSuccess = true;
					alert_msg = fetch_cust_details_supplementary();
				}

				RLOS.mLogger.info(primaryCif);
			}
			if(!isEntityDetailsSuccess || (primaryCif==null || "".equalsIgnoreCase(primaryCif))){
				outputResponse =GenXml.GenerateXML("CUSTOMER_ELIGIBILITY","Supplementary_Card_Details");
				RLOS.mLogger.info("Customer Eligibility");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				RLOS.mLogger.info(ReturnCode);
				if("0000".equalsIgnoreCase(ReturnCode) )
				{
					RLOS_IntegrationOutput.valueSetIntegration(outputResponse ,"Supplementary_Card_Details");    
					parse_cif_eligibility(outputResponse,"Supplementary_Card_Details");
					alert_msg = fetch_cust_details_supplementary();

				}
			}

		}



		else   if("Send_OTP_Btn".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setNGValue("otp_ref_no", formObject.getWFFolderId());
			RLOS.mLogger.info( formObject.getWFFolderId()+"");
			hm.put("Send_OTP_Btn","Clicked");
			popupFlag="Y";
			outputResponse = GenXml.GenerateXML("OTP_MANAGEMENT","GenerateOTP");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			RLOS.mLogger.info(ReturnCode);
			if("0000".equalsIgnoreCase(ReturnCode) ){
				RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");    
				RLOS.mLogger.info("OTP is generated");
				formObject.setEnabled("OTP_No",true);
				formObject.setEnabled("Validate_OTP_Btn",true);
				alert_msg = NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL009");
			}
			else{
				//formObject.setNGValue("OTP_Generation","OTP is not generated");
				alert_msg = NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL010");
				formObject.setEnabled("OTP_No",false);
				formObject.setEnabled("Validate_OTP_Btn",false);

			}
			RLOS.mLogger.info("OTP generation");
			try
			{
				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			finally{hm.clear();}
		}

		else   if("AuthorisedSignDetails_FetchDetails".equalsIgnoreCase(pEvent.getSource().getName())){

			outputResponse = GenXml.GenerateXML("CUSTOMER_DETAILS","Signatory_CIF");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			RLOS.mLogger.info(ReturnCode);
			if("0000".equalsIgnoreCase(ReturnCode) ){
				formObject.setNGValue("Is_Customer_Details_AuthSign","Y");
				RLOS.mLogger.info("value of Guarantor_CIF"+formObject.getNGValue("Is_Customer_Details_AuthSign"));
				RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "Signatory_CIF");    
				RLOS.mLogger.info("Guarantor_CIF is generated");
				RLOS.mLogger.info(formObject.getNGValue("Is_Customer_Details_AuthSign"));
				//below code added by Arun (06/12/17) to make editable after fetching details successfully 
				formObject.setLocked("AuthorisedSignDetails_Name",false);
				formObject.setLocked("AuthorisedSignDetails_nationality",false);
				formObject.setLocked("AuthorisedSignDetails_DOB",false);
				formObject.setLocked("AuthorisedSignDetails_VisaNumber",false);
				formObject.setLocked("AuthorisedSignDetails_VisaExpiryDate",false);
				formObject.setLocked("AuthorisedSignDetails_Status",false);
				formObject.setLocked("AuthorisedSignDetails_PassportNo",false);
				formObject.setLocked("AuthorisedSignDetails_PassportExpiryDate",false);
				formObject.setLocked("AuthorisedSignDetails_shareholding",false);
				formObject.setLocked("AuthorisedSignDetails_SoleEmployee",false);
				//above code added by Arun (06/12/17) to make editable after fetching details successfully
			}
			else{
				RLOS.mLogger.info("Customer Details is not generated");
				formObject.setNGValue("Is_Customer_Details_AuthSign","N");
			}
			RLOS.mLogger.info(formObject.getNGValue("Is_Customer_Details_AuthSign"));
		}


		else   if("DecisionHistory_SendSMS".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			RLOS.mLogger.info( "");

			outputResponse = GenXml.GenerateXML("SEND_ADHOC_ALERT","");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			RLOS.mLogger.info(ReturnCode);
			if("0000".equalsIgnoreCase("0000") ){
				RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");    
				RLOS.mLogger.info("SMS is send");
				//formObject.setEnabled("OTP_No",true);
				//formObject.setEnabled("Validate_OTP_Btn",true);
				alert_msg = NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL011");
			}
			else{
				//formObject.setNGValue("OTP_Generation","OTP is not generated");
				alert_msg = NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL012");
				RLOS.mLogger.info("Error while sending SMS");
			}

		}




		else  if("Validate_OTP_Btn".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			outputResponse = GenXml.GenerateXML("OTP_MANAGEMENT","ValidateOTP");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			RLOS.mLogger.info(ReturnCode);
			OTPStatus=(outputResponse.contains("<OTPStatus>")) ? outputResponse.substring(outputResponse.indexOf("<OTPStatus>")+"</OTPStatus>".length()-1,outputResponse.indexOf("</OTPStatus>")):"";    
			RLOS.mLogger.info(OTPStatus);
			if("0000".equalsIgnoreCase(ReturnCode) ){
				RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");    
				formObject.setNGValue("cmplx_Customer_OTPValidation","true");

				RLOS.mLogger.info("OTP is generated");
				formObject.setNGValue("OTPStatus",OTPStatus);
				RLOS.mLogger.info(formObject.getNGValue("OTPStatus"));

				alert_msg = NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL013");

			}
			else{
				formObject.setNGValue("OTP_Generation","OTP is not generated");
				alert_msg = NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL014");
			}
			RLOS.mLogger.info("OTP generation");
			try
			{
				popupFlag="Y";
				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			finally{hm.clear();}
		}


		else  if("CompanyDetails_Button3".equalsIgnoreCase(pEvent.getSource().getName())){
			RLOS.mLogger.info("CompanyDetails_Button3");
			outputResponse = GenXml.GenerateXML("CUSTOMER_DETAILS","Corporation_CIF");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			RLOS.mLogger.info(ReturnCode);
			/*	String CustId = (outputResponse.contains("<CustId>")) ? outputResponse.substring(outputResponse.indexOf("<CustId>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</CustId>")):"";    
		//RLOS.mLogger.info(CustId);
		String CorpName = (outputResponse.contains("<CorpName>")) ? outputResponse.substring(outputResponse.indexOf("<CorpName>")+"</CorpName>".length()-1,outputResponse.indexOf("</CorpName>")):"";    
		//RLOS.mLogger.info(CorpName);
		String BusinessIncDate = (outputResponse.contains("<BusinessIncDate>")) ? outputResponse.substring(outputResponse.indexOf("<BusinessIncDate>")+"</BusinessIncDate>".length()-1,outputResponse.indexOf("</BusinessIncDate>")):"";    
		//RLOS.mLogger.info(BusinessIncDate);
		String LegEnt = (outputResponse.contains("<LegEnt>")) ? outputResponse.substring(outputResponse.indexOf("<LegEnt>")+"</LegEnt>".length()-1,outputResponse.indexOf("</LegEnt>")):"";    
		//RLOS.mLogger.info(LegEnt);
			 */
			if("0000".equalsIgnoreCase("0000") ){
				 formObject.setNGValue("Is_Customer_Details_CompanyCIF","Y");

				 RLOS.mLogger.info("value of company Details corporation"+formObject.getNGValue("Is_Customer_Details_CompanyCIF"));

				 RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "Corporation_CIF");  
				 String query="Select INDUSTRY_SECTOR,INDUSTRY_MACRO, INDUSTRY_MICRO from NG_RLOS_ALOC_OFFLINE_DATA where EMPR_NAME='"+formObject.getNGValue("compName")+"'";
				 RLOS.mLogger.info("Query to load alloc details: "+query);
				 List<List<String>> output=formObject.getNGDataFromDataCache(query);
				if(output!=null && !output.isEmpty()){
					if(output.get(0).get(0)!=null && !"".equals(output.get(0).get(0))){
						formObject.setNGValue("indusSector",output.get(0).get(0));
					}
					if(output.get(0).get(1)!=null && !"".equals(output.get(0).get(1))){
						formObject.setNGValue("indusMAcro",output.get(0).get(1));
					}
					if(output.get(0).get(0)!=null && !"".equals(output.get(0).get(0))){
						formObject.setNGValue("indusMicro",output.get(0).get(2));
					}	
				}
				
				 //	String Date1=BusinessIncDate;
				 String estbDate = formObject.getNGValue("estbDate");
				 String TLExpiry = formObject.getNGValue("TLExpiry");
				 if(estbDate!=null&&!"".equalsIgnoreCase(estbDate)){
					 formObject.setNGValue("estbDate",common.Convert_dateFormat(estbDate, "yyyy-mm-dd", "dd/mm/yyyy"));
					 RLOS.mLogger.info(formObject.getNGValue("estbDate"));
				 }	
				 if(TLExpiry!=null&&!"".equalsIgnoreCase(TLExpiry)){
					 formObject.setNGValue("TLExpiry",common.Convert_dateFormat(TLExpiry, "yyyy-mm-dd", "dd/mm/yyyy"));
					 RLOS.mLogger.info(formObject.getNGValue("TLExpiry"));
				 }
				 formObject.setLocked("appType", false);
				 formObject.setLocked("compIndus", false);
				 formObject.setLocked("TLIssueDate", false);
				 formObject.setLocked("desig", false);
				 formObject.setLocked("desigVisa", false);
				 formObject.setLocked("indusSector", false);
				 formObject.setLocked("indusMAcro", false);
				 formObject.setLocked("indusMicro", false);
				//Deepak 12Nov2017 added to disbale the Cif IF button.
				 formObject.setEnabled("CompanyDetails_Button3", false); 
				 formObject.setEnabled("cif", false); 
				 RLOS.mLogger.info("corporation cif");
				 RLOS.mLogger.info(formObject.getNGValue("Is_Customer_Details_CompanyCIF"));
			 }
			 else{
				 RLOS.mLogger.info("Customer Details Corporation CIF is not generated");
				 formObject.setNGValue("Is_Customer_Details_CompanyCIF","N");
			 }
			 RLOS.mLogger.info(formObject.getNGValue("Is_Customer_Details_CompanyCIF"));
		}



		else  if ("Button9".equalsIgnoreCase(pEvent.getSource().getName())){

			if(!formObject.isVisible("IncomeDetails_Frame1")){
				formObject.fetchFragment("Incomedetails", "IncomeDetails", "q_IncomeDetails");
				formObject.setNGFrameState("Incomedetails", 0);
				formObject.setNGFrameState("Incomedetails", 1);

				hm.put("Button9","Clicked");
				popupFlag="N";
				outputResponse = GenXml.GenerateXML("ACCOUNT_SUMMARY","");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				RLOS.mLogger.info(ReturnCode);
				if("0000".equalsIgnoreCase(ReturnCode) ){
					RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");
					formObject.setNGValue("Is_Account_Summary","Y");
				}
				else{
					formObject.setNGValue("Is_Account_Summary","N");
				}
				RLOS.mLogger.info(formObject.getNGValue("Is_Account_Summary"));
				//ended


			}

			IMFields_Income();
			BTCFields_Income();
			LimitIncreaseFields_Income();
			ProductUpgrade_Income();

			if(!formObject.isVisible("EMploymentDetails_Frame1")){
				formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmploymentDetails");
				formObject.setNGFrameState("EmploymentDetails", 0);
				formObject.setNGFrameState("EmploymentDetails", 1);


			}
			IMFields_Employment();
			BTCFields_Employment();
			LimitIncreaseFields_Employment();
			ProductUpgrade_Employment();


			if(!formObject.isVisible("Liability_New_Frame1")){
				formObject.fetchFragment("Liability_container", "Liability_New", "q_LiabilityNew");
				formObject.setNGFrameState("Liability_New_Frame1", 0);
				formObject.setNGFrameState("Liability_New_Frame1", 1);
			}

		}


		else   if ("EMploymentDetails_Button2".equalsIgnoreCase(pEvent.getSource().getName())) {
			String EmpName=formObject.getNGValue("EMploymentDetails_Text21");
			String EmpCode=formObject.getNGValue("EMploymentDetails_Text22");
			RLOS.mLogger.info( "EMpName$"+EmpName+"$");
			String query;
			if("".equalsIgnoreCase(EmpName.trim()))
				query="select distinct(EMPR_NAME),EMPLOYER_CODE,INCLUDED_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,EMPLOYER_CATEGORY_PL,COMPANY_STATUS_CC,COMPANY_STATUS_PL,MAIN_EMPLOYER_CODE from NG_RLOS_ALOC_OFFLINE_DATA where EMPLOYER_CODE Like '%"+EmpCode+"%'";

			else if("".equalsIgnoreCase(EmpCode.trim()))
				query="select distinct(EMPR_NAME),EMPLOYER_CODE,INCLUDED_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,EMPLOYER_CATEGORY_PL,COMPANY_STATUS_CC,COMPANY_STATUS_PL,MAIN_EMPLOYER_CODE from NG_RLOS_ALOC_OFFLINE_DATA where EMPR_NAME Like '%"+EmpName + "%'";

			else
				query="select distinct(EMPR_NAME),EMPLOYER_CODE,INCLUDED_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,EMPLOYER_CATEGORY_PL,COMPANY_STATUS_CC,COMPANY_STATUS_PL,MAIN_EMPLOYER_CODE from NG_RLOS_ALOC_OFFLINE_DATA where EMPR_NAME Like '%"+EmpName + "%' or EMPLOYER_CODE Like '%"+EmpCode+"%'";

			RLOS.mLogger.info( "query is: "+query);
			populatePickListWindow(query,"EMploymentDetails_Button2", "Employer Name,Employer Code,INCLUDED IN PL ALOC,INCLUDED IN CC ALOC,INDUSTRY SECTOR,INDUSTRY MACRO,INDUSTRY MICRO,CONSTITUTION,NAME OF FREEZONE AUTHORITY,Employer Category PL, Employer Status CC,Employer Status PL,MAIN EMPLOYER CODE", true, 20);			     
		}

		else if("cmplx_ExternalLiabilities_AECBConsent".equalsIgnoreCase(pEvent.getSource().getName())){
			if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_ExternalLiabilities_AECBConsent")))
				formObject.setEnabled("ExternalLiabilities_Button1",true);
			else
				formObject.setEnabled("ExternalLiabilities_Button1",false);
		}


		else if("cmplx_EmploymentDetails_Others".equalsIgnoreCase(pEvent.getSource().getName())){
			try{
				if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_Others"))){
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
				RLOS.mLogger.info( "Exception occurred in removing item from cmplx_EmploymentDetails_EmpStatusCC"+printException(e));}
		}

		else if ("SupplementCardDetails_Button2".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			formObject.setNGValue("supplement_WIname",formObject.getWFWorkitemName());	
			RLOS.mLogger.info( "Inside Supplement_add button: "+formObject.getNGValue("supplement_WIname"));
			formObject.ExecuteExternalCommand("NGAddRow", "SupplementCardDetails_cmplx_SupplementGrid");		
		}

		/*else if(pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo_Button1"))
	{
		if(formObject.getNGValue("Product_Type").contains("Personal Loan")){
			String query="Select PRIME_TYPE,LSM_PRODDIFFRATE,prime_type_rate from NG_master_Scheme where SCHEMEId='"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,8)+"'";
			RLOS.mLogger.info("Inside ELigibiltyAndProductInfo_Button1-->Query is:"+query);
			List<List<String>> marginRate=formObject.getDataFromDataSource(query);
			if(marginRate.get(0).get(0)!=null && marginRate.get(0).get(0)!="" && marginRate.size()>0){
				String baseRateType=marginRate.get(0).get(0);
				String baseRate=marginRate.get(0).get(1);
				String ProdprefRate=marginRate.get(0).get(2);
				RLOS.mLogger.info("List is:"+marginRate);
				String netRate=formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate");
				formObject.setNGValue("cmplx_EligibilityAndProductInfo_BAseRate", baseRate);
				formObject.setNGValue("cmplx_EligibilityAndProductInfo_BaseRateType", baseRateType);
				formObject.setNGValue("cmplx_EligibilityAndProductInfo_ProdPrefRate", ProdprefRate);
				formObject.setNGValue("cmplx_EligibilityAndProductInfo_FinalInterestRate",netRate);
				formObject.setNGValue("cmplx_EligibilityAndProductInfo_MArginRate", Float.parseFloat(netRate)-Float.parseFloat(baseRate)-Float.parseFloat(ProdprefRate));
			}
		}	
		//RLOS.mLogger.info("Inside ELigibiltyAndProductInfo_Button1->MarginRate:"+MarginRate);
		List objInput=new ArrayList();
		objInput.add("Text:"+formObject.getWFWorkitemName());
		objInput.add("Text:CIF_ID");
		RLOS.mLogger.info("objInput args are: "+objInput.get(0)+objInput.get(1));
		formObject.getDataFromStoredProcedure("ng_RLOS_CASProductDedupeCheck", objInput);
		RLOS.mLogger.info("ng_RLOS_CASProductDedupeCheck Procedure Executed!!");
	}*/

		//Arun (01/12/17) modified below code of control name of listed reason & selected reason
		else if ("FATCA_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			String text=formObject.getNGItemText("cmplx_FATCA_listedReason_ListedReason", formObject.getSelectedIndex("cmplx_FATCA_listedReason_ListedReason"));
			RLOS.mLogger.info( "Inside FATCA_Button1 "+text);
			formObject.addItem("cmplx_FATCA_selectedReason_SelectedReason", text);
			try {
				formObject.removeItem("cmplx_FATCA_listedReason_ListedReason", formObject.getSelectedIndex("cmplx_FATCA_listedReason_ListedReason"));
				formObject.setSelectedIndex("cmplx_FATCA_listedReason_ListedReason", -1);

			}catch (Exception e) {

				RLOS.logException(e);
			}

		}


		else if ("FATCA_Button2".equalsIgnoreCase(pEvent.getSource().getName())){
			RLOS.mLogger.info( "Inside FATCA_Button2 ");
			formObject.addItem("cmplx_FATCA_listedReason_ListedReason", formObject.getNGItemText("cmplx_FATCA_selectedReason_SelectedReason", formObject.getSelectedIndex("cmplx_FATCA_selectedReason_SelectedReason")));
			try {
				formObject.removeItem("cmplx_FATCA_selectedReason_SelectedReason", formObject.getSelectedIndex("cmplx_FATCA_selectedReason_SelectedReason"));
				formObject.setSelectedIndex("cmplx_FATCA_selectedReason_SelectedReason", -1);
			} catch (Exception e) {

				RLOS.logException(e);
			}
		}
		//Arun (01/12/17) modified above code of control name of listed reason & selected reason


		else if ("IncomingDoc_AddFromPCButton".equalsIgnoreCase(pEvent.getSource().getName())){
			IRepeater repObj;
			repObj = formObject.getRepeaterControl("IncomingDoc_Frame");
			repObj.getValue(repObj.getChangeIndex(),"IncomingDoc_Label1");
			RLOS.mLogger.info("value of repeater:"+repObj.getValue(repObj.getChangeIndex(),"IncomingDoc_Label1"));


		}    


		else if("Reject".equalsIgnoreCase(pEvent.getSource().getName())){
			RLOS.mLogger.info(formObject.getSelectedSheet(pEvent.getSource().getName())+"");
			formObject.setSelectedSheet("ParentTab",8);		
			formObject.fetchFragment("DecisionHistoryContainer", "DecisionHistory", "q_DecisionHistory");
			formObject.setNGFrameState("DecisionHistoryContainer", 0);
			formObject.setNGFrameState("DecisionHistoryContainer", 1);
			formObject.setNGFrameState("DecisionHistoryContainer", 0);
			formObject.setNGFocus("DecisionHistory_Button4");
		}


		else  if("Liability_New_Button1".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			hm.put("Liability_New_Button1","Clicked");
			popupFlag="N";


			//added

			outputResponse = GenXml.GenerateXML("AECB","");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			RLOS.mLogger.info(ReturnCode);

			if("0000".equalsIgnoreCase(ReturnCode) ){
				RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");
				formObject.setNGValue("IS_AECB","Y");
			}
			else{
				formObject.setNGValue("IS_AECB","Y");
			}
			RLOS.mLogger.info(formObject.getNGValue("IS_AECB"));
			//ended
		}

		else  if("DecisionHistory_Button3".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			RLOS_IntegrationInput.getCustAddress_details();
			String NTB_flag = formObject.getNGValue("cmplx_Customer_NTB");
			//String NEP_flag = formObject.getNGValue("cmplx_Customer_NEP");
			String CIF_no = formObject.getNGValue("cmplx_Customer_CIFNO");
			RLOS.mLogger.info( "inside create Account/Customer NTB value: "+NTB_flag );
			if("true".equalsIgnoreCase(NTB_flag) || "".equalsIgnoreCase(CIF_no)){
				formObject.setNGValue("curr_user_name",formObject.getUserName());
				outputResponse = GenXml.GenerateXML("NEW_CUSTOMER_REQ","");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				RLOS.mLogger.info(ReturnCode);
				if("0000".equalsIgnoreCase(ReturnCode)){
					formObject.setEnabled("DecisionHistory_Button3",false);
					RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");   
					formObject.setNGValue("cmplx_DecisionHistory_CifNo", formObject.getNGValue("cmplx_Customer_CIFNO"));
					RLOS.mLogger.info("Inside if of New customer Req");


					outputResponse = GenXml.GenerateXML("NEW_ACCOUNT_REQ","");
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					RLOS.mLogger.info(ReturnCode);

					if("0000".equalsIgnoreCase(ReturnCode) ){
						RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");    
						formObject.setNGValue("Is_Account_Create","Y");
						formObject.setNGValue("EligibilityStatus","Y");
						formObject.setNGValue("EligibilityStatusCode","Y");
						formObject.setNGValue("EligibilityStatusDesc","Y");
					}
					else{
						formObject.setNGValue("Is_Account_Create","N");
					}
					RLOS.mLogger.info(formObject.getNGValue("Is_Account_Create"));
					RLOS.mLogger.info(formObject.getNGValue("EligibilityStatus"));
					RLOS.mLogger.info(formObject.getNGValue("EligibilityStatusCode"));
					RLOS.mLogger.info(formObject.getNGValue("EligibilityStatusDesc"));



				}
				else{
					RLOS.mLogger.info("Inside else of New Customer Req");
				}
			}
			else
			{
				RLOS.mLogger.info("inside else condition ##AKSHAY##");
				//String wi_name= formObject.getWFWorkitemName();
				/*
			 List<List<String>> AccountCount= formObject.getDataFromDataSource("Select count from ng_RLOS_CUSTEXPOSE_AcctDetails where AcctType='CURRENT ACCOUNT' AND Wi_Name="+wi_name+";");
			int countCurrentAccount= AccountCount.size();
			RLOS.mLogger.info(countCurrentAccount+"");
				 */
				//RLOS.mLogger.info(" count of current account: "+formObject.getNGValue("AccountCount"));

				String query="Select count(*) from ng_RLOS_CUSTEXPOSE_AcctDetails where AcctType='CURRENT ACCOUNT' AND Wi_Name='"+formObject.getWFWorkitemName()+"'";
				List<List<String>> AccountCount= formObject.getDataFromDataSource(query);
				RLOS.mLogger.info( "Query is: "+query+" currValue In AccountCount is "+AccountCount);
				RLOS.mLogger.info( "NTB is:"+formObject.getNGValue("cmplx_Customer_NTB"));
				int countCurrentAccount =  Integer.parseInt(AccountCount.get(0).get(0));


				if(("false".equals(formObject.getNGValue("cmplx_Customer_NTB")) && countCurrentAccount==0)){
					RLOS.mLogger.info("NTB: "+formObject.getNGValue("cmplx_Customer_NTB")+" Account Count:"+formObject.getNGValue("AccountCount"));
					//createCustomer();
					outputResponse = GenXml.GenerateXML("NEW_ACCOUNT_REQ","");
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					RLOS.mLogger.info(ReturnCode);
					if("0000".equalsIgnoreCase(ReturnCode) ){
						RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");    
						formObject.setNGValue("Is_Account_Create","Y");
						alert_msg= NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL048");

					}
					else{
						formObject.setNGValue("Is_Account_Create","N");
					}
					RLOS.mLogger.info(formObject.getNGValue("Is_Account_Create"));
					if("Y".equalsIgnoreCase(formObject.getNGValue("Is_Account_Create"))){ 
						RLOS.mLogger.info("inside if condition");
						formObject.setEnabled("DecisionHistory_Button5", false);     
					}
					else{
						formObject.setEnabled("DecisionHistory_Button5", true);
					}
					throw new ValidatorException(new FacesMessage(alert_msg));
				}
			}
		}


		else   if("ReadFromCIF".equalsIgnoreCase(pEvent.getSource().getName())){

			outputResponse = GenXml.GenerateXML("CUSTOMER_DETAILS","Guarantor_CIF");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			RLOS.mLogger.info(ReturnCode);
			if("0000".equalsIgnoreCase(ReturnCode)){
				formObject.setNGValue("Is_Customer_Details_Guarantor","Y");
				RLOS.mLogger.info("value of Guarantor_CIF"+formObject.getNGValue("Is_Customer_Details_Guarantor"));
				RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "Guarantor_CIF");    
				RLOS.mLogger.info("Guarantor_CIF is generated");
				RLOS.mLogger.info(formObject.getNGValue("Is_Customer_Details_Guarantor"));
				formObject.setNGValue("passExpiry", Convert_dateFormat(formObject.getNGValue("passExpiry"), "yyyy-mm-dd", "dd/mm/yyyy"),false);
				formObject.setNGValue("dob", Convert_dateFormat(formObject.getNGValue("dob"), "yyyy-mm-dd", "dd/mm/yyyy"),false);
				formObject.setNGValue("eidExpiry", Convert_dateFormat(formObject.getNGValue("eidExpiry"), "yyyy-mm-dd", "dd/mm/yyyy"),false);
				formObject.setNGValue("visaExpiry", Convert_dateFormat(formObject.getNGValue("visaExpiry"), "yyyy-mm-dd", "dd/mm/yyyy"),false);
				//code added by saurabh on 22nd July 17.
				if(formObject.getNGValue("dob")!=null && !"".equalsIgnoreCase(formObject.getNGValue("dob")) && !" ".equalsIgnoreCase(formObject.getNGValue("dob"))){
					common.getAge(formObject.getNGValue("dob"), "age");	
				}
			}
			else{
				RLOS.mLogger.info("Customer Details is not generated");
				formObject.setNGValue("Is_Customer_Details_Guarantor","N");
			}
			RLOS.mLogger.info(formObject.getNGValue("Is_Customer_Details_Guarantor"));
		}
		//Added for the World check call in case of self employed  
		else if ("FetchWorldCheck_SE".equalsIgnoreCase(pEvent.getSource().getName())) {
			popupFlag="Y";		
			//	columnValues=columnValues.join(",",columnValues_arr);
			RLOS.mLogger.info("inside worldcheck"); 
			outputResponse = GenXml.GenerateXML("CUSTOMER_SEARCH_REQUEST","");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			RLOS.mLogger.info(ReturnCode);

			if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode) ){
				RLOS.mLogger.info("inside if of WORLDCHECK");
				//formObject.fetchFragment("WorldCheck", "WorldCheck1", "q_WorldCheck");	
				//formObject.setVisible("WorldCheck", false);
				RLOS_IntegrationOutput.valueSetIntegration(outputResponse,"");	
				formObject.setNGValue("IS_WORLD_CHECK","Y");
				alert_msg= NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL015");
			}
			else if("9999".equalsIgnoreCase(ReturnCode)){
				alert_msg= NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL016");
				formObject.setNGValue("IS_WORLD_CHECK","Y");
			}
			else{
				formObject.setNGValue("IS_WORLD_CHECK","N");
				RLOS.mLogger.info("inside else of WORLD CHECK");
				alert_msg= NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL017");
			}
			RLOS.mLogger.info("alert: "+ alert_msg);
			formObject.RaiseEvent("WFSave");
			throw new ValidatorException(new FacesMessage(alert_msg));

		}
		//Added for the World check call in case of self employed

		// added by Akshay for world_check on initiation  
		else if ("WorldCheck_fetch".equalsIgnoreCase(pEvent.getSource().getName())) {
			popupFlag="Y";
			//	columnValues=columnValues.join(",",columnValues_arr);
			RLOS.mLogger.info("inside worldcheck"); 
			outputResponse = GenXml.GenerateXML("CUSTOMER_SEARCH_REQUEST","");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			RLOS.mLogger.info(ReturnCode);

			if("0000".equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode) ){
				RLOS.mLogger.info("inside if of WORLDCHECK");
				formObject.fetchFragment("WorldCheck", "WorldCheck1", "q_WorldCheck");	
				formObject.setVisible("WorldCheck", false);
				RLOS_IntegrationOutput.valueSetIntegration(outputResponse,"");	
				formObject.setNGValue("IS_WORLD_CHECK","Y");
				alert_msg= NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL015");
			}
			else if("9999".equalsIgnoreCase(ReturnCode)){
				alert_msg= NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL016");
				formObject.setNGValue("IS_WORLD_CHECK","Y");
			}
			else{
				formObject.setNGValue("IS_WORLD_CHECK","N");
				RLOS.mLogger.info("inside else of WORLD CHECK");
				alert_msg= NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL017");
			}
			RLOS.mLogger.info("alert: "+ alert_msg);
			formObject.RaiseEvent("WFSave");
			//changes done to set focus on the same button.
			formObject.setNGFocus("WorldCheck_fetch");
			throw new ValidatorException(new FacesMessage(alert_msg));

		}
		// ended Akshay for world_check initiation


		else if("ELigibiltyAndProductInfo_Button1".equalsIgnoreCase(pEvent.getSource().getName()))
		{		
			popupFlag="Y";
			formObject.setNGValue("DecCallFired","Eligibility");
			String RequiredProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
			RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse is : "+outputResponse);
			formObject.fetchFragment("WorldCheck", "WorldCheck1", "q_WorldCheck");	
			formObject.setVisible("WorldCheck", false);
			int framestate1=formObject.getNGFrameState("Incomedetails");
			if(framestate1 == 0){
				RLOS.mLogger.info("RLOS count of current account not NTB Incomedetails");
			}
			else {
				formObject.fetchFragment("Incomedetails", "IncomeDetails", "q_IncomeDetails");
				RLOS.mLogger.info("RLOS count of current account not NTB fetched income details");
				formObject.setTop("Incomedetails",formObject.getTop("ProductDetailsLoader")+formObject.getHeight("ProductDetailsLoader"));
				visibilityFrameIncomeDetails(formObject);
			}
			try{
				double LoanAmount=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
				double tenor=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor"));
				double RateofInt=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate"));

				String EMI=getEMI(LoanAmount,RateofInt,tenor);
				formObject.setNGValue("cmplx_EligibilityAndProductInfo_EMI", EMI==null||EMI.equalsIgnoreCase("")?"0":EMI);

			}
			catch(Exception e){
				RLOS.logException(e);
				RLOS.mLogger.info(" Exception in EMI Generation");
			}
			//EMI Calcuation logic added below 24-Sept-2017 End
			if ((formObject.getNGValue("Winame") != null) || "".equalsIgnoreCase(formObject.getNGValue("Winame"))){
				formObject.setNGValue("Winame", formObject.getWFWorkitemName());
			}
			if ("Credit Card".equalsIgnoreCase(RequiredProd)){
				outputResponse = GenXml.GenerateXML("DECTECH","CreditCard");
				RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse is : "+outputResponse);
				RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse ASDASDASDASD : "+outputResponse);

				SystemErrorCode =  (outputResponse.contains("<SystemErrorCode>")) ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"Error";
				RLOS.mLogger.info(SystemErrorCode);
				RLOS.mLogger.info(outputResponse);

				if("".equalsIgnoreCase(SystemErrorCode)){
					RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");   
					alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL018");
					RLOS.mLogger.info("after value set customer for dectech call");
					formObject.RaiseEvent("WFSave");
				}
				else{
					alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL019");
				}
			}
			else{
				outputResponse = GenXml.GenerateXML("DECTECH","PersonalLoan");
				RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse is : "+outputResponse);
				RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse ASDASDASDASD : "+outputResponse);

				//ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				SystemErrorCode =  (outputResponse.contains("<SystemErrorCode>")) ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"Error";
				RLOS.mLogger.info(SystemErrorCode);
				RLOS.mLogger.info(outputResponse);

				if("".equalsIgnoreCase(SystemErrorCode)){
					RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");   
					alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL018");
					RLOS.mLogger.info("after value set customer for dectech call");
					formObject.RaiseEvent("WFSave");
				}
				else{
					alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL019");
				}

			}
			
			// position of EMI logic shifted to make it run for 1st time
			try{
				double LoanAmount=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
				double tenor=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor"));
				double RateofInt=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate"));

				String EMI=getEMI(LoanAmount,RateofInt,tenor);
				formObject.setNGValue("cmplx_EligibilityAndProductInfo_EMI", EMI==null||EMI.equalsIgnoreCase("")?"0":EMI);

			}
			catch(Exception e){
				RLOS.logException(e);
				RLOS.mLogger.info(" Exception in EMI Generation");
			}
			//EMI Calcuation logic added below 24-Sept-2017 End
			//deepak Code changes to calculate LPF amount and % 08-nov-2017 start
			try{
				RLOS.mLogger.info("RLOS_Common"+"Inside set event of LPF Data");
				double LPF_charge;
				List<List<String>> result=formObject.getDataFromDataSource("select CHARGERATE from NG_MASTER_Charges where ChargeID = ( select distinct LPF_ChargeID from NG_master_Scheme where schemeid = '"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,8)+"' )");
				RLOS.mLogger.info("RLOS_Common"+ "result of fetch RMname query: "+result); 
				if(result==null || result.equals("") || result.isEmpty()){
					LPF_charge=1;
				}
				else{
					LPF_charge = Double.parseDouble(result.get(0).get(0));
				}
				RLOS.mLogger.info("RLOS_Common code "+ "result LPF_charge: "+LPF_charge);
				formObject.setNGValue("cmplx_EligibilityAndProductInfo_LPF", LPF_charge);
				double LPF_amount;
				double final_Loan_amount = Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
				LPF_amount = (final_Loan_amount*LPF_charge)/100;
				RLOS.mLogger.info("RLOS_Common code "+ "result LPF_amount: "+LPF_amount);
				formObject.setNGValue("cmplx_EligibilityAndProductInfo_LPFAmount", LPF_amount);
			}
			catch(Exception e){
				RLOS.logException(e);
				RLOS.mLogger.info(" Exception in EMI Generation");
			}
			//deepak Code changes to calculate LPF amount and % 08-nov-2017 End
			//change to set focus on the same button.	
			formObject.setNGFocus("ELigibiltyAndProductInfo_Button1");
			throw new ValidatorException(new FacesMessage(alert_msg));
			//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse ALERT : "+outputResponse);
			//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse WFS : "+outputResponse);

		}
		else if("Eligibility_Emp".equalsIgnoreCase(pEvent.getSource().getName()))
		{		
			popupFlag="Y";
			formObject.setNGValue("DecCallFired","Eligibility");
			String RequiredProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
			RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse is : "+outputResponse);
			formObject.fetchFragment("WorldCheck", "WorldCheck1", "q_WorldCheck");	
			formObject.setVisible("WorldCheck", false);
			//Change Done to fetch Income Details
			int framestate1=formObject.getNGFrameState("Incomedetails");
			if(framestate1 == 0){
				RLOS.mLogger.info("RLOS count of current account not NTB Incomedetails");
			}
			else {
				formObject.fetchFragment("Incomedetails", "IncomeDetails", "q_IncomeDetails");
				RLOS.mLogger.info("RLOS count of current account not NTB fetched income details");
				formObject.setTop("Incomedetails",formObject.getTop("ProductDetailsLoader")+formObject.getHeight("ProductDetailsLoader"));
				visibilityFrameIncomeDetails(formObject);
			}
			//Change Done to fetch Income Details
			if ((formObject.getNGValue("Winame") != null) || "".equalsIgnoreCase(formObject.getNGValue("Winame"))){
				formObject.setNGValue("Winame", formObject.getWFWorkitemName());
			}
			if ("Credit Card".equalsIgnoreCase(RequiredProd)){
				outputResponse = GenXml.GenerateXML("DECTECH","CreditCard");
				RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse is : "+outputResponse);
				RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse ASDASDASDASD : "+outputResponse);

				//	ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				SystemErrorCode =  (outputResponse.contains("<SystemErrorCode>")) ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"Error";
				RLOS.mLogger.info(SystemErrorCode);
				RLOS.mLogger.info(outputResponse);

				if("".equalsIgnoreCase(SystemErrorCode)){
					RLOS.mLogger.info(formObject.getSelectedSheet(pEvent.getSource().getName())+"");
					formObject.setSelectedSheet("ParentTab",3);		
					//formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligAndProductInfo");
					//String tenure=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 7);
					//RLOS.mLogger.info( "Funding Account Details now Visible...!!!");
					//formObject.setNGValue("cmplx_EligibilityAndProductInfo_Tenor", tenure);//it is dere at fragment load
					fetch_EligPrd_frag();
					formObject.setNGFrameState("EligibilityAndProductInformation", 0);
					//formObject.setNGFrameState("EligibilityAndProductInformation", 1);
					formObject.setNGFrameState("EligibilityAndProductInformation", 0);

					//Eligibilityfields();
					RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");   
					alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL018");
					RLOS.mLogger.info("after value set customer for dectech call");
					checkforBPACase();
					formObject.RaiseEvent("WFSave");
				}
				else{
					alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL019");

				}
			}
			else{
				outputResponse = GenXml.GenerateXML("DECTECH","PersonalLoan");
				RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse is : "+outputResponse);
				RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse ASDASDASDASD : "+outputResponse);

				//ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				SystemErrorCode =  (outputResponse.contains("<SystemErrorCode>")) ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"Error";
				RLOS.mLogger.info(SystemErrorCode);
				RLOS.mLogger.info(outputResponse);

				if(("".equalsIgnoreCase(SystemErrorCode))&& !"0".equalsIgnoreCase(ReturnCode)){
					RLOS.mLogger.info(formObject.getSelectedSheet(pEvent.getSource().getName())+"");
					formObject.setSelectedSheet("ParentTab",3);		
					//formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligAndProductInfo");
					//String tenure=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 7);
					//formObject.setNGValue("cmplx_EligibilityAndProductInfo_Tenor", tenure);
					fetch_EligPrd_frag();
					formObject.setNGFrameState("EligibilityAndProductInformation", 0);
					formObject.setNGFrameState("EligibilityAndProductInformation", 1);
					formObject.setNGFrameState("EligibilityAndProductInformation", 0);
					//Eligibilityfields();
					RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");   
					alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL018");
					RLOS.mLogger.info("after value set customer for dectech call");
					formObject.RaiseEvent("WFSave");

				}
				else{
					alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL019");

				}

			}
			//change to set focus on the same button.
			try{
				double LoanAmount=Double.parseDouble(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,3)==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
				double tenor=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor"));
				double RateofInt=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate"));

				String EMI=getEMI(LoanAmount,RateofInt,tenor);
				formObject.setNGValue("cmplx_EligibilityAndProductInfo_EMI", EMI==null||EMI.equalsIgnoreCase("")?"0":EMI);

			}
			catch(Exception e){
				RLOS.mLogger.info("Exception Occured in RLOS Iniitation :  Exception in EMI Generation");
			}
			//EMI Calcuation logic added below 06-nov-2017 End
			formObject.setNGFocus("Eligibility_Emp");
			//change by saurabh on 29th nov 2017 as elig can only be run once.
			formObject.setEnabled("Eligibility_Emp",false);
			throw new ValidatorException(new FacesMessage(alert_msg));
			//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse ALERT : "+outputResponse);
			//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse WFS : "+outputResponse);

		}
		//Added for Dectech Call on Self Employed Case
		else if("CheckEligibility_SE".equalsIgnoreCase(pEvent.getSource().getName()))
		{		
			popupFlag="Y";
			formObject.setNGValue("DecCallFired","Eligibility");
			String RequiredProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
			RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse is : "+outputResponse);
			formObject.fetchFragment("CompanyDetails", "CompanyDetails", "q_CompanyDetails");
			formObject.fetchFragment("AuthorisedSignatoryDetails", "AuthorisedSignDetails", "q_AuthorisedSignDetails");
			formObject.setTop("AuthorisedSignatoryDetails", formObject.getTop("CompanyDetails")+formObject.getHeight("CompanyDetails")+25);
			formObject.setTop("PartnerDetails", formObject.getTop("AuthorisedSignatoryDetails")+formObject.getHeight("AuthorisedSignatoryDetails")+25);
			//Change Done to fetch Income Details
			int framestate1=formObject.getNGFrameState("Incomedetails");
			if(framestate1 == 0){
				RLOS.mLogger.info("RLOS count of current account not NTB Incomedetails");
			}
			else {
				formObject.fetchFragment("Incomedetails", "IncomeDetails", "q_IncomeDetails");
				RLOS.mLogger.info("RLOS count of current account not NTB fetched income details");
				formObject.setTop("Incomedetails",formObject.getTop("ProductDetailsLoader")+formObject.getHeight("ProductDetailsLoader"));
				visibilityFrameIncomeDetails(formObject);
			}
			//Change Done to fetch Income Details
			formObject.fetchFragment("WorldCheck", "WorldCheck1", "q_WorldCheck");	
			formObject.setVisible("WorldCheck", false);
			if ((formObject.getNGValue("Winame") != null) || "".equalsIgnoreCase(formObject.getNGValue("Winame"))){
				formObject.setNGValue("Winame", formObject.getWFWorkitemName());
			}
			if ("Credit Card".equalsIgnoreCase(RequiredProd)){
				outputResponse = GenXml.GenerateXML("DECTECH","CreditCard");
				RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse is : "+outputResponse);
				RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse ASDASDASDASD : "+outputResponse);

				//ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				SystemErrorCode =  (outputResponse.contains("<SystemErrorCode>")) ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"Error";
				RLOS.mLogger.info(SystemErrorCode);
				RLOS.mLogger.info(outputResponse);

				if("".equalsIgnoreCase(SystemErrorCode)){
					RLOS.mLogger.info(formObject.getSelectedSheet(pEvent.getSource().getName())+"");
					formObject.setSelectedSheet("ParentTab",3);		
					formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligAndProductInfo");
					String tenure=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 7);
					//RLOS.mLogger.info( "Funding Account Details now Visible...!!!");
					formObject.setNGValue("cmplx_EligibilityAndProductInfo_Tenor", tenure);

					formObject.setNGFrameState("EligibilityAndProductInformation", 0);
					formObject.setNGFrameState("EligibilityAndProductInformation", 1);
					formObject.setNGFrameState("EligibilityAndProductInformation", 0);

					Eligibilityfields();
					RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");   
					alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL018");
					RLOS.mLogger.info("after value set customer for dectech call");
					formObject.RaiseEvent("WFSave");



				}
				else{
					alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL019");

				}
			}
			else{
				outputResponse = GenXml.GenerateXML("DECTECH","PersonalLoan");
				RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse is : "+outputResponse);
				RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse ASDASDASDASD : "+outputResponse);

				//ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				SystemErrorCode =  (outputResponse.contains("<SystemErrorCode>")) ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"Error";
				RLOS.mLogger.info(SystemErrorCode);
				RLOS.mLogger.info(outputResponse);

				if("".equalsIgnoreCase(SystemErrorCode)){
					RLOS.mLogger.info(formObject.getSelectedSheet(pEvent.getSource().getName())+"");
					formObject.setSelectedSheet("ParentTab",3);		
					formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligAndProductInfo");
					String tenure=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 7);
					formObject.setNGValue("cmplx_EligibilityAndProductInfo_Tenor", tenure);
					formObject.setNGFrameState("EligibilityAndProductInformation", 0);
					formObject.setNGFrameState("EligibilityAndProductInformation", 1);
					formObject.setNGFrameState("EligibilityAndProductInformation", 0);
					//Eligibilityfields();
					RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");   
					alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL018");
					RLOS.mLogger.info("after value set customer for dectech call");
					formObject.RaiseEvent("WFSave");



				}
				else{
					alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL019");

				}

			}
			//change to set focus on the same button.
			formObject.setNGFocus("CheckEligibility_SE");
			throw new ValidatorException(new FacesMessage(alert_msg));
			//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse ALERT : "+outputResponse);


			//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse WFS : "+outputResponse);

		}
		//Added for Dectech Call on Self Employed Case
		else if("Button2".equalsIgnoreCase(pEvent.getSource().getName()))
		{		
			popupFlag="Y";
			formObject.setNGValue("DecCallFired","CalculateDBR");
			formObject.fetchFragment("WorldCheck", "WorldCheck1", "q_WorldCheck");	
			formObject.setVisible("WorldCheck", false);
			String RequiredProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
			RLOS.mLogger.info("$$After RequiredProd for dectech call..outputResponse is : "+RequiredProd);
			//Change Done to fetch Income Details
			int framestate1=formObject.getNGFrameState("Incomedetails");
			if(framestate1 == 0){
				RLOS.mLogger.info("RLOS count of current account not NTB Incomedetails");
			}
			else {
				formObject.fetchFragment("Incomedetails", "IncomeDetails", "q_IncomeDetails");
				RLOS.mLogger.info("RLOS count of current account not NTB fetched income details");
				formObject.setTop("Incomedetails",formObject.getTop("ProductDetailsLoader")+formObject.getHeight("ProductDetailsLoader"));
				visibilityFrameIncomeDetails(formObject);
			}
			//Change Done to fetch Income Details

			//Change Done to fetch Employment Details on Calculate DBR button
			int EmpDetailframestate=formObject.getNGFrameState("EMploymentDetails_Frame1");
			if(EmpDetailframestate == 0)
			{
				RLOS.mLogger.info("EMploymentDetails_Frame1 frame is expanded");				
			}
			else
			{
				RLOS.mLogger.info("EMploymentDetails_Frame1 frame is not expanded");
				formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmploymentDetails");
				//added by aman for the world check button
				formObject.setTop("Eligibility_Emp", 460);
				formObject.setTop("WorldCheck_fetch", 500);
				RLOS.mLogger.info(" fetched employment details");
			
			}
			if ((formObject.getNGValue("Winame") != null) || "".equalsIgnoreCase(formObject.getNGValue("Winame"))){
				formObject.setNGValue("Winame", formObject.getWFWorkitemName());
			}
			if ("Credit Card".equalsIgnoreCase(RequiredProd)){
				outputResponse = GenXml.GenerateXML("DECTECH","CreditCard");
				RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse is : "+outputResponse);
				RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse ASDASDASDASD : "+outputResponse);
				//ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				SystemErrorCode =  (outputResponse.contains("<SystemErrorCode>")) ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"Error";
				RLOS.mLogger.info(SystemErrorCode);
				RLOS.mLogger.info(outputResponse);

				if("".equalsIgnoreCase(SystemErrorCode)){
					RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");   
					alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL018");
					RLOS.mLogger.info("after value set customer for dectech call");
					formObject.RaiseEvent("WFSave");
				}
				else{
					alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL019");
				}
			}
			else{
				outputResponse = GenXml.GenerateXML("DECTECH","PersonalLoan");
				RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse is : "+outputResponse);
				RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse ASDASDASDASD : "+outputResponse);

				//ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				SystemErrorCode =  (outputResponse.contains("<SystemErrorCode>")) ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"Error";
				RLOS.mLogger.info(SystemErrorCode);
				RLOS.mLogger.info(ReturnCode);
				if("".equalsIgnoreCase(SystemErrorCode)){
					RLOS_IntegrationOutput.valueSetIntegration(outputResponse , "");   
					alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL018");
					RLOS.mLogger.info("after value set customer for dectech call");
					formObject.RaiseEvent("WFSave");
				}
				else{
					alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL019");
				}

			}
			//below code change to set focus and use single through.  
			formObject.setNGFocus("Button2");
			throw new ValidatorException(new FacesMessage(alert_msg));


			//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse ALERT : "+outputResponse);


			//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse WFS : "+outputResponse);




		}



		else if("Customer_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setNGValue("PassportNo",formObject.getNGValue("cmplx_Customer_PAssportNo"));
			formObject.setNGValue("MobileNo",formObject.getNGValue("cmplx_Customer_MobNo"));
			if("".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_MiddleName")))
			{
				formObject.setNGValue("Customer_Name",formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme"));
			}

			else{
				formObject.setNGValue("Customer_Name",formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_MiddleName")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme"));
			}
			formObject.setNGValue("Cust_Name",formObject.getNGValue("Customer_Name"));//added by akshay on 25/9/17 as per point 14 of problem sheet
			formObject.saveFragment("CustomerDetails");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL020");
			formObject.setNGValue("cmplx_Customer_IscustomerSave", "Y");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if("Product_Save".equalsIgnoreCase(pEvent.getSource().getName()))
		{			
			formObject.setNGValue("lbl_prod_val",formObject.getNGValue("PrimaryProduct"));
			formObject.setNGValue("lbl_scheme_val",formObject.getNGValue("Subproduct_productGrid"));
			formObject.setNGValue("lbl_card_prod_val",formObject.getNGValue("CardProduct_Primary"));
			formObject.setNGValue("lbl_Loan_amount_bal",formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,3));

			formObject.saveFragment("ProductDetailsLoader");
			formObject.RaiseEvent("WFSave");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL021");
			throw new ValidatorException(new FacesMessage(alert_msg));

		}


		else if("GuarantorDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("GuarantorDetails");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL022");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if("IncomeDetails_Salaried_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Incomedetails");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL023");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if("IncomeDetails_SelfEmployed_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Incomedetails");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL024");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if("CompanyDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setNGValue("Company_Name", formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",1,4));//added By Akshay on 16/9/17 to set header
			formObject.setNGValue("Tl_No", formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",1,5));//added By Akshay on 16/9/17 to set value i ext table column
			formObject.setNGValue("lbl_Comp_Name_val",formObject.getNGValue("Company_Name"));
			formObject.saveFragment("CompanyDetails");
			formObject.saveFragment("AuthorisedSignDetails");
			formObject.saveFragment("PartnerDetails");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL025");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if("Liability_New_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Liability_container");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL026");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if("EMploymentDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setNGValue("Employer_Name", formObject.getNGValue("cmplx_EmploymentDetails_EmpName"));//added By akshay on 16/9/17 to set header
			formObject.setNGValue("lbl_Comp_Name_val",formObject.getNGValue("Employer_Name"));

			formObject.saveFragment("EmploymentDetails");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL027");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if("ELigibiltyAndProductInfo_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("EligibilityAndProductInformation");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL028");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else 	if("MiscellaneousFields_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("MiscFields");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL029");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if("AddressDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Address_Details_container");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL030");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if("ContactDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Alt_Contact_container");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL031");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else 	if("CardDetails_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("CardDetails_container");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL032");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if("SupplementCardDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Supplementary_Container");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL033");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if("FATCA_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("FATCA");


			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL034");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if("KYC_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			RLOS.mLogger.info( "Inside KYC save button");
			formObject.saveFragment("KYC");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL035");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if("OECD_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("OECD");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL036");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		else if("ReferenceDetails_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("ReferenceDetails");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL037");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		else if("ServiceRequest_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("CC_Loan_container");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL038");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		else if("BTC_save".equalsIgnoreCase(pEvent.getSource().getName()) ){
			formObject.saveFragment("CC_Loan_container");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL039");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		else if("CC_Loan_DDS_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("CC_Loan_container");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL040");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		else if("CC_Loan_SI_save".equalsIgnoreCase(pEvent.getSource().getName()) ){
			formObject.saveFragment("CC_Loan_container");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL041");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		else if("CC_Loan_RVC_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("CC_Loan_container");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL042");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if("IncomingDoc_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			RLOS.mLogger.info( "TANSHU Inside IncomingDoc_Save button!!");
			formObject.saveFragment("IncomingDocuments");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL043");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if("DecisionHistory_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("DecisionHistoryContainer");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL044");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if ("OECD_Button1".equalsIgnoreCase(pEvent.getSource().getName())){					

			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_OECD_cmplx_GR_OecdDetails");
		}

		else if ("OECD_Button2".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_OECD_cmplx_GR_OecdDetails");
		}


		else if ("OECD_Button3".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_OECD_cmplx_GR_OecdDetails");
		}



		//added by yash for RLOS FSD
		else if("NotepadDetails_save".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.saveFragment("Notepad_Values");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL045");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		// added by abhishek as per rlos FSD 


		else if("NotepadDetails_Add".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setNGValue("NotepadDetails_WiNote",formObject.getWFWorkitemName());
			RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("NotepadDetails_WiNote"));					
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

		// ended by abhishek as per rlos FSD
		else if("cmplx_NotepadDetails_cmplx_notegrid".equalsIgnoreCase(pEvent.getSource().getName())){
			Notepad_grid();

		} 
		else{
			RLOS.mLogger.info( " No condition reached in mouse click event");
		}			
	}

	/*          Function Header:

	 **********************************************************************************

         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


Date Modified                       : 6/08/2017              
Author                              : Akshay Gupta             
Description                         :To handle value change events        

	 ***********************************************************************************  */


	public void call_Value_Changed(ComponentEvent pEvent)
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		RLOS.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		Common_Utils common=new Common_Utils(RLOS.mLogger);

		/*if ("cmplx_Customer_DOb".equalsIgnoreCase(pEvent.getSource().getName()))
	{
		RLOS.mLogger.info("RLOS val ..!. Value of dob is:"+formObject.getNGValue("cmplx_Customer_DOb"));
		common.getAge(formObject.getNGValue("cmplx_Customer_DOb"),"cmplx_Customer_age");
	}
			// Code added to Load targetSegmentCode on basis of code
		 */	
		if ("ApplicationCategory".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			RLOS.mLogger.info("RLOS val change Value of dob is:"+formObject.getNGValue("ApplicationCategory"));
			String subproduct = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2);
			String appCategory=formObject.getNGValue("ApplicationCategory");
			//SKLogger.writeLog("RLOS val change ", "Value of subproduct is:"+formObject.getNGValue("subproduct"));
			if(appCategory!=null &&  "BAU".equalsIgnoreCase(appCategory))
			{
				LoadPickList("TargetSegmentCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and BAU='Y' and (product = 'CC' or product='B') order by code");
			}
			else if(appCategory!=null &&  "Surrogate".equalsIgnoreCase(appCategory))
			{
				LoadPickList("TargetSegmentCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and Surrogate='Y' and (product = 'CC' or product='B') order by code");
			}
			else
			{
				LoadPickList("TargetSegmentCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and (product = 'CC' or product='B') order by code");	
			}
		}
		// Code added to Load targetSegmentCode on basis of code

		//code added for LOB
		if ("estbDate".equalsIgnoreCase(pEvent.getSource().getName())){
			//change by saurabh on 29th Nov 17 for Drop2 CR.
			RLOS.mLogger.info( "Value of estbDate is:"+formObject.getNGValue("estbDate"));
			common.getAge(formObject.getNGValue("estbDate"),"lob");
		}
		//code added for LOB
		//code by saurabh on 28th Nov 2017 for calculating EMI after change in interest rate or tenor
		else if("cmplx_EligibilityAndProductInfo_InterestRate".equalsIgnoreCase(pEvent.getSource().getName()) || "cmplx_EligibilityAndProductInfo_Tenor".equalsIgnoreCase(pEvent.getSource().getName())){
			try{
				double LoanAmount=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
				double tenor=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor"));
				double RateofInt=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate"));

				String EMI=getEMI(LoanAmount,RateofInt,tenor);
				formObject.setNGValue("cmplx_EligibilityAndProductInfo_EMI", EMI==null||EMI.equalsIgnoreCase("")?"0":EMI);

			}
			catch(Exception e){
				RLOS.logException(e);
				RLOS.mLogger.info(" Exception in EMI Generation");
			}
		}
		else if ("indusSector".equalsIgnoreCase(pEvent.getSource().getName())){					
			LoadPickList("indusMAcro", "select '--Select--' union select convert(varchar, macro) from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where IndustrySector='"+formObject.getNGValue("indusSector")+"' and IsActive='Y'");
		}

		else if ("indusMAcro".equalsIgnoreCase(pEvent.getSource().getName())){
			LoadPickList("indusMicro", "select '--Select--' union select convert(varchar, micro) from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where Macro='"+formObject.getNGValue("indusMAcro")+"' and IsActive='Y'");
		}

		/*	else if ("cmplx_EmploymentDetails_DOJ".equalsIgnoreCase(pEvent.getSource().getName())){
		RLOS.mLogger.info( "Value of dob is:"+formObject.getNGValue("cmplx_EmploymentDetails_DOJ"));
		common.getAge(formObject.getNGValue("cmplx_EmploymentDetails_DOJ"),"cmplx_EmploymentDetails_LOS");
	}*/
		else if ("cmplx_EligibilityAndProductInfo_FirstRepayDate".equalsIgnoreCase(pEvent.getSource().getName())){
			RLOS.mLogger.info( "Value of dob is:"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_MaturityDate"));
			String AgeAtMAturity=getYearsDifference(formObject,"cmplx_Customer_DOb","cmplx_EligibilityAndProductInfo_MaturityDate");
			RLOS.mLogger.info( "Value of dob is:"+AgeAtMAturity);
			formObject.setNGValue("cmplx_EligibilityAndProductInfo_ageAtMaturity", AgeAtMAturity);

		}
		/*else if ("cmplx_Customer_Nationality".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			RLOS.mLogger.info( "iNSIDE cmplx_Customer_Nationality CASE-->akShay "+formObject.getNGValue("cmplx_Customer_Nationality"));

			String  GCC="BH,IQ,KW,OM,QA,SA,AE";

			if(GCC.contains(formObject.getNGValue("cmplx_Customer_Nationality")))
			{
				RLOS.mLogger.info("Inside GCC for Nationality");
				formObject.setNGValue("cmplx_Customer_GCCNational","Y");
			}
			else
			{
				formObject.setNGValue("cmplx_Customer_GCCNational","N");
			}
		}*/
		else if("cmplx_EmploymentDetails_ApplicationCateg".equalsIgnoreCase(pEvent.getSource().getName())){
			String reqProd = formObject.getNGValue("PrimaryProduct");
			String appCategory = formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg");
			String subproduct = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2);
			if("Personal Loan".equalsIgnoreCase(reqProd)){
				if(appCategory!=null && "BAU".equalsIgnoreCase(appCategory)){
					LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and BAU='Y' and (product = 'PL' or product='B') order by code");
				}
				else if(appCategory!=null &&  "Surrogate".equalsIgnoreCase(appCategory)){
					LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and Surrogate='Y' and (product = 'PL' or product='B') order by code");
				}
				else{
					LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and (product = 'PL' or product='B') order by code");	
				}
			}
			else if("Credit Card".equalsIgnoreCase(reqProd)){
				if(appCategory!=null &&  "BAU".equalsIgnoreCase(appCategory)){
					LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and BAU='Y' and (product = 'CC' or product='B') order by code");
				}
				else if(appCategory!=null &&  "Surrogate".equalsIgnoreCase(appCategory)){
					LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and Surrogate='Y' and (product = 'CC' or product='B') order by code");
				}
				else{
					LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and (product = 'CC' or product='B') order by code");	
				}
			}
		}

		else if ("ReqProd".equalsIgnoreCase(pEvent.getSource().getName())){
			String ReqProd=formObject.getNGValue("ReqProd");
			RLOS.mLogger.info( "Value of ReqProd is:"+ReqProd);
			loadPicklistProduct(ReqProd);
			if("Credit Card".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1))){
				formObject.setVisible("AlternateContactDetails_retainAccount",false);
			}
			else{
				formObject.setVisible("AlternateContactDetails_retainAccount",true);
			}
		}


		else if ("subProd".equalsIgnoreCase(pEvent.getSource().getName())){
			RLOS.mLogger.info( "Value of SubProd is:"+formObject.getNGValue("subProd"));
			formObject.clear("AppType");
			LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_ApplicationType with (nolock) where subProdFlag='"+formObject.getNGValue("subProd")+"' order by code");
			String subprod=formObject.getNGValue("subProd");
			String VIPFlag=formObject.getNGValue("cmplx_Customer_VIPFlag").equalsIgnoreCase("true")?"Y":"N";
			String Nationality=formObject.getNGValue("cmplx_Customer_Nationality");
			String req_prod=formObject.getNGValue("ReqProd");
			String TypeOfProd=formObject.getNGValue("Product_type");
			RLOS.mLogger.info( "Value of SubProd is:"+subprod);
			RLOS.mLogger.info( "Value of vip is:"+VIPFlag);
			RLOS.mLogger.info( "Value of Nationality is:"+Nationality);
			RLOS.mLogger.info( "Value of req_prod is:"+req_prod);
			RLOS.mLogger.info( "Value of TypeOfProd is:"+TypeOfProd);

			if ("Credit Card".equalsIgnoreCase(req_prod)){
				if("PU".equalsIgnoreCase(subprod)|| "LI".equalsIgnoreCase(subprod))
				{
					formObject.setVisible("Product_Label6",false);//Arun (06/12/17) to hide this field
					formObject.setVisible("CardProd",false);//Arun (06/12/17) to hide this field
					
					formObject.setVisible("Product_Label7",true);
					formObject.setVisible("LastPermanentLimit",true);
					formObject.setVisible("Product_Label9",true);
					formObject.setVisible("LastTemporaryLimit",true);
					formObject.setVisible("Product_Label16",false);//Arun (06/12/17) to hide this field
					formObject.setVisible("LimitAcc",false);//Arun (06/12/17) to hide this field
					formObject.setVisible("CardDetails_container",false);//Arun (06/12/17) to hide this fragment
					formObject.setLocked("cmplx_CardDetails_CompanyEmbossingName",true);//Arun (06/12/17) to make noneditable
	
					formObject.clear("CardProd");
					RLOS.mLogger.info( " is not BTC ,PA :"+subprod);

					//Deepak Code change to load card product with new master.
					LoadPickList("CardProd","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cardProduct with (nolock) where ReqProduct = '"+TypeOfProd+"' order by code");
						/*if("Conventional".equalsIgnoreCase(TypeOfProd)){
					LoadPickList("CardProd","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cardProduct with (nolock) where reqproduct like '%conventional%' order by code");
					}
					else{
					LoadPickList("CardProd","select '--Select--' as description,'' as code union select convert(varchar, Description) from ng_master_cardProduct with (nolock) where reqproduct like '%Islamic%' order by code");
					}*/
				}
				
				else if("PULI".equalsIgnoreCase(subprod))
				{
					/*formObject.setVisible("Product_Label7",true);
					formObject.setVisible("LastPermanentLimit",true);
					formObject.setVisible("Product_Label9",true);
					formObject.setVisible("LastTemporaryLimit",true);*/
					
					formObject.setVisible("Product_Label6",true);//Arun (06/12/17) to show this field
					formObject.setVisible("CardProd",true);//Arun (06/12/17) to show this field
					formObject.setVisible("Product_Label16",false);//Arun (06/12/17) to hide this field
					formObject.setVisible("LimitAcc",false);//Arun (06/12/17) to hide this field
					formObject.setLocked("cmplx_CardDetails_CompanyEmbossingName",true);//Arun (06/12/17) to make noneditable
	
					formObject.clear("CardProd");
					RLOS.mLogger.info( " is not BTC ,PA :"+subprod);

					//Deepak Code change to load card product with new master.
					LoadPickList("CardProd","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cardProduct with (nolock) where ReqProduct = '"+TypeOfProd+"' order by code");
						/*if("Conventional".equalsIgnoreCase(TypeOfProd)){
					LoadPickList("CardProd","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cardProduct with (nolock) where reqproduct like '%conventional%' order by code");
					}
					else{
					LoadPickList("CardProd","select '--Select--' as description,'' as code union select convert(varchar, Description) from ng_master_cardProduct with (nolock) where reqproduct like '%Islamic%' order by code");
					}*/
				}
				
				else if("SE".equalsIgnoreCase(subprod))
				{
					formObject.setLocked("cmplx_CardDetails_CompanyEmbossingName",false);
				}//Arun (06/12/17) to make editable 
				
				else{
					//added by akshay on 20/11/17 so select sal or SALP in emptype
					RLOS.mLogger.info( "subProd is not PU or LI"); 
					if("IM".equalsIgnoreCase(subprod) || "SAL".equalsIgnoreCase(subprod) || "BPA".equalsIgnoreCase(subprod))
					{
						RLOS.mLogger.info("subProd is IM/SAL/BPA");
						formObject.setVisible("Product_Label6",true);//Arun (06/12/17) to show this field
						formObject.setVisible("CardProd",true);//Arun (06/12/17) to show this field
						formObject.clear("EmpType");
						LoadPickList("EmpType","select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_PRD_EMPTYPE where code  like 'sal%' order by code");
						formObject.setLocked("cmplx_CardDetails_CompanyEmbossingName",true);//Arun (06/12/17) to make noneditable
					}
					else{
						//LoadPickList("EmpType","select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_PRD_EMPTYPE where code not like 'sal%' order by code");
						LoadPickList("EmpType","select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_PRD_EMPTYPE  order by code"); //Arun (05/12/17) modified to load the emplyment type masters correctly
						
						formObject.setNGValue("EmpType", "Self Employed");
					}
				
					formObject.clear("CardProd");
					String query1="select '--Select--' as Description,'' as Code union select convert(varchar, Description),code from ng_master_cardProduct with (nolock) where Reqproduct='"+TypeOfProd+"' and SUBproduct = '"+subprod+"'  and VIPFlag='"+VIPFlag+"' and Nationality='AE' order by code";	
					String query2="select '--Select--' as Description,'' as Code union select convert(varchar, Description),code from ng_master_cardProduct with (nolock) where Reqproduct='"+TypeOfProd+"' and SUBproduct = '"+subprod+"'  and VIPFlag='"+VIPFlag+"' and Nationality is null order by code"; //Arun (05/12/17) modified to load the corret masters if nationality is null
					RLOS.mLogger.info( ": "+Nationality);
					RLOS.mLogger.info(query1);
					if("AE".equalsIgnoreCase(Nationality)){
						RLOS.mLogger.info(query1);
						LoadPickList("CardProd",query1);
					}
					else{
						RLOS.mLogger.info(query2);
						LoadPickList("CardProd",query2);
					}
				}
			}
		}

		else if ("AppType".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			String subprod=formObject.getNGValue("SubProd");
			String apptype=formObject.getNGValue("AppType");
			String TypeofProduct=formObject.getNGValue("Product_type");
			RLOS.mLogger.info( "Value of SubProd is:"+formObject.getNGValue("SubProd"));
			RLOS.mLogger.info( "Value of AppType is:"+formObject.getNGValue("AppType"));
			RLOS.mLogger.info( "Value of AppType is:"+formObject.getNGValue("Product_type"));

			if (("Conventional".equalsIgnoreCase(TypeofProduct))&& "LI".equalsIgnoreCase(subprod)&&("T".equalsIgnoreCase(apptype))){
				RLOS.mLogger.info( "1Value of AppType is:"+formObject.getNGValue("AppType"));
				formObject.setVisible("Product_Label9",true);
				formObject.setVisible("LastTemporaryLimit",true);
				formObject.setVisible("Product_Label7",false);
				formObject.setVisible("LastPermanentLimit",false);
			}//Arun (06/12/17) for Application type validation in product
			
			if (("Conventional".equalsIgnoreCase(TypeofProduct))&& "LI".equalsIgnoreCase(subprod)&&("P".equalsIgnoreCase(apptype))){
				RLOS.mLogger.info( "1Value of AppType is:"+formObject.getNGValue("AppType"));
				formObject.setVisible("Product_Label9",false);
				formObject.setVisible("LastTemporaryLimit",false);
				formObject.setVisible("Product_Label7",true);
				formObject.setVisible("LastPermanentLimit",true);
			} //Arun (06/12/17) for Application type validation in product
			
			
			if (("Conventional".equalsIgnoreCase(TypeofProduct))&& "EXP".equalsIgnoreCase(subprod)&&("TKOE".equalsIgnoreCase(apptype))){


				RLOS.mLogger.info( "1Value of AppType is:"+formObject.getNGValue("AppType"));


				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Take Over'  and TypeofProduct='Conventional' order by SCHEMEID");




			}
			else if (("Conventional".equalsIgnoreCase(TypeofProduct))&& "EXP".equalsIgnoreCase(subprod)&&("NEWE".equalsIgnoreCase(apptype))){
				RLOS.mLogger.info( "2Value of AppType is:"+formObject.getNGValue("AppType"));




				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'New' and TypeofProduct='Conventional' order by SCHEMEID");




			}
			else if (("Conventional".equalsIgnoreCase(TypeofProduct)&& "EXP".equalsIgnoreCase(subprod))&&("TOPE".equalsIgnoreCase(apptype))){
				RLOS.mLogger.info( "3Value of AppType is:"+formObject.getNGValue("AppType"));


				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Top up' and TypeofProduct='Conventional' order by SCHEMEID");


			}
			else if (("Conventional".equalsIgnoreCase(TypeofProduct)&& "EXP".equalsIgnoreCase(subprod))&&("RESCE".equalsIgnoreCase(apptype))){
				RLOS.mLogger.info( "4Value of AppType is:"+formObject.getNGValue("AppType"));


				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Reschedulment' and TypeofProduct='Conventional' order by SCHEMEID");


			}
			else if (("Conventional".equalsIgnoreCase(TypeofProduct)&& "NAT".equalsIgnoreCase(subprod))&&("TOPN".equalsIgnoreCase(apptype))){
				RLOS.mLogger.info( "5Value of AppType is:"+formObject.getNGValue("AppType"));
				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Top up' and TypeofProduct='Conventional' order by SCHEMEID");


			}
			else if (("Conventional".equalsIgnoreCase(TypeofProduct)&& "NAT".equalsIgnoreCase(subprod))&&("TKON".equalsIgnoreCase(apptype))){
				RLOS.mLogger.info( "6Value of AppType is:"+formObject.getNGValue("AppType"));
				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Take Over' and TypeofProduct='Conventional' order by SCHEMEID");


			}
			else if (("Conventional".equalsIgnoreCase(TypeofProduct)&& "NAT".equalsIgnoreCase(subprod))&&("NEWN".equalsIgnoreCase(apptype))){
				RLOS.mLogger.info( "7Value of AppType is:"+formObject.getNGValue("AppType"));
				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'New' and TypeofProduct='Conventional' order by SCHEMEID");


			}
			else if (("Conventional".equalsIgnoreCase(TypeofProduct)&& "NAT".equalsIgnoreCase(subprod))&&("RESCN".equalsIgnoreCase(apptype))){
				RLOS.mLogger.info( "8Value of AppType is:"+formObject.getNGValue("AppType"));
				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Reschedulment' and TypeofProduct='Conventional' order by SCHEMEID");


			}
			else if (("Islamic".equalsIgnoreCase(TypeofProduct))&& "EXP".equalsIgnoreCase(subprod) &&("TKOE".equalsIgnoreCase(apptype))){


				RLOS.mLogger.info( "1Value of AppType is:"+formObject.getNGValue("AppType"));


				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Take Over'  and TypeofProduct='Islamic' order by SCHEMEID");				
			}

			else if (("Islamic".equalsIgnoreCase(TypeofProduct))&& "EXP".equalsIgnoreCase(subprod)&&("NEWE".equalsIgnoreCase(apptype))){
				RLOS.mLogger.info( "2Value of AppType is:"+formObject.getNGValue("AppType"));
				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'New' and TypeofProduct='Islamic' order by SCHEMEID");				
			}

			else if (("Islamic".equalsIgnoreCase(TypeofProduct)&& "EXP".equalsIgnoreCase(subprod))&&("TOPE".equalsIgnoreCase(apptype))){
				RLOS.mLogger.info( "3Value of AppType is:"+formObject.getNGValue("AppType"));


				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Top up' and TypeofProduct='Islamic' order by SCHEMEID");


			}
			else if (("Islamic".equalsIgnoreCase(TypeofProduct)&& "EXP".equalsIgnoreCase(subprod))&&("RESCE".equalsIgnoreCase(apptype))){
				RLOS.mLogger.info( "4Value of AppType is:"+formObject.getNGValue("AppType"));


				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Reschedulment' and TypeofProduct='Islamic' order by SCHEMEID");


			}
			else if (("Islamic".equalsIgnoreCase(TypeofProduct)&& "NAT".equalsIgnoreCase(subprod))&&("TOPN".equalsIgnoreCase(apptype))){
				RLOS.mLogger.info( "5Value of AppType is:"+formObject.getNGValue("AppType"));
				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Top up' and TypeofProduct='Islamic' order by SCHEMEID");


			}
			else if (("Islamic".equalsIgnoreCase(TypeofProduct)&& "NAT".equalsIgnoreCase(subprod))&&("TKON".equalsIgnoreCase(apptype))){
				RLOS.mLogger.info( "6Value of AppType is:"+formObject.getNGValue("AppType"));
				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Take Over' and TypeofProduct='Islamic' order by SCHEMEID");


			}
			else if (("Islamic".equalsIgnoreCase(TypeofProduct)&& "NAT".equalsIgnoreCase(subprod))&&("NEWN".equalsIgnoreCase(apptype))){
				RLOS.mLogger.info( "7Value of AppType is:"+formObject.getNGValue("AppType"));
				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'New' and TypeofProduct='Islamic' order by SCHEMEID");


			}
			else if (("Islamic".equalsIgnoreCase(TypeofProduct)&& "NAT".equalsIgnoreCase(subprod))&&("RESCN".equalsIgnoreCase(apptype))){
				RLOS.mLogger.info( "8Value of AppType is:"+formObject.getNGValue("AppType"));
				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Reschedulment' and TypeofProduct='Islamic' order by SCHEMEID");

			}
		}

		//changed by akshay on 8th nov 2017 for loadpicklist.
		else if ("cmplx_EmploymentDetails_EmpIndusSector".equalsIgnoreCase(pEvent.getSource().getName()) && "true".equals(formObject.getNGValue("cmplx_EmploymentDetails_Others"))){

			RLOS.mLogger.info( "$Indus Sector$:" +formObject.getNGValue("cmplx_EmploymentDetails_EmpIndusSector"));
			formObject.setLocked("cmplx_EmploymentDetails_Indus_Macro", false);//it is unlocked from js but its instance state is saved as locked as it was locked on fragment load
			LoadPickList("cmplx_EmploymentDetails_Indus_Macro", "Select macro from (select '--Select--' as macro union select macro from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where IndustrySector='"+formObject.getNGValue("cmplx_EmploymentDetails_EmpIndusSector")+"' and IsActive='Y') new_table order by case  when macro ='--Select--' then 0 else 1 end");
		}

		else if ("cmplx_EmploymentDetails_Indus_Macro".equalsIgnoreCase(pEvent.getSource().getName()) && "true".equals(formObject.getNGValue("cmplx_EmploymentDetails_Others"))){
			RLOS.mLogger.info( "$Indus Macro$:" +formObject.getNGValue("cmplx_EmploymentDetails_Indus_Macro"));
			formObject.setLocked("cmplx_EmploymentDetails_Indus_Micro", false);
			LoadPickList("cmplx_EmploymentDetails_Indus_Micro", "Select micro from (select '--Select--' as micro union select  micro from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where Macro='"+formObject.getNGValue("cmplx_EmploymentDetails_Indus_Macro")+"' and IndustrySector='"+formObject.getNGValue("cmplx_EmploymentDetails_EmpIndusSector")+"' and IsActive='Y' ) new_table order by case  when micro ='--Select--' then 0 else 1 end");
		}
		//commented by saurabh on 10th nov 2017.
		/*	else if ("AlternateContactDetails_carddispatch".equalsIgnoreCase(pEvent.getSource().getName())){
		String cardDisp=formObject.getNGSelectedItemText("AlternateContactDetails_carddispatch");
		RLOS.mLogger.info(cardDisp);
		//changes by saurabh on 10th nov 2017.
		if (!"COURIER".equalsIgnoreCase(cardDisp)){
			formObject.setVisible("AltContactDetails_Label14",true);
			formObject.setVisible("AlternateContactDetails_custdomicile",true);
			//Deepak Code change to add requested product filter

			String TypeOfProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0)==null?"":formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0);
			LoadPickList("AlternateContactDetails_custdomicile", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock)  where product_type = '"+TypeOfProd+"' order by code");					
		}

	}*/
		else if ("Product_type".equalsIgnoreCase(pEvent.getSource().getName())){

			String ProdType=formObject.getNGValue("Product_type");
			RLOS.mLogger.info(ProdType);
			formObject.clear("CardProd");
			formObject.setNGValue("CardProd","--Select--");
			//Deepak Code change to load card product with new master.
			LoadPickList("CardProd","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cardProduct with (nolock) where ReqProduct = '"+ProdType+"' order by code");
			/*if("Conventional".equalsIgnoreCase(ProdType))
			 * LoadPickList("CardProd", "select '--Select--','' as code union select convert(varchar,description),code from ng_MASTER_CardProduct with (nolock) where code NOT like '%AMAL%' order by code");
		else if("Islamic".equalsIgnoreCase(ProdType))
			LoadPickList("CardProd", "select '--Select--','' as code union select convert(varchar,description),code from ng_MASTER_CardProduct with (nolock) where code like '%AMAL%' order by code");*/
		}

		// added by abhishek point 44
		else if("cmplx_EmploymentDetails_Others".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_Others")))
			{
				formObject.setLocked("EMploymentDetails_Text21", true);
				formObject.setLocked("EMploymentDetails_Text22", true);

			}
			else
			{
				formObject.setLocked("EMploymentDetails_Text21", false);
				formObject.setLocked("EMploymentDetails_Text22", false);
			}
		}

		//added by yash on 30/8/2017
		else if ("NotepadDetails_notedesc".equalsIgnoreCase(pEvent.getSource().getName())){
			String notepad_desc = formObject.getNGValue("NotepadDetails_notedesc");

			String sQuery = "select code,workstep from ng_master_notedescription where description='" +  notepad_desc + "'";


			List<List<String>> recordList = formObject.getDataFromDataSource(sQuery);
			if(recordList.get(0).get(0)!= null && recordList.get(0)!=null && !"".equalsIgnoreCase(recordList.get(0).get(0)) && recordList!=null)
			{
				formObject.setNGValue("NotepadDetails_notecode",recordList.get(0).get(0));
				formObject.setNGValue("NotepadDetails_WorkstepName",recordList.get(0).get(1));		        

			}



		}
	}

	/*          Function Header:

	 **********************************************************************************

         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


Date Modified                       : 6/08/2017              
Author                              : Akshay Gupta             
Description                         :To make fragments locked on load       

	 ***********************************************************************************  */


	public void DisableFragmentsOnLoad(ComponentEvent pEvent)//Except Decision history
	{
		RLOS.mLogger.info("Inside RLOSCommoncode-->DisableFragmentsOnLoad()");
		if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.setLocked("Customer_Frame1",true);
			loadPicklistCustomer();
		}
		else if ("GuarantorDetails".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.setLocked("GuarantorDetails_Frame1",true);
		}
		else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			String ReqProd=formObject.getNGValue("ReqProd");
			loadPicklistProduct(ReqProd);
			formObject.setLocked("Product_Frame1",true);
		}
		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.setLocked("EMploymentDetails_Frame1",true);
			loadPicklist4();
		}
		else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.setLocked("IncomeDetails_Frame1",true);			
		}
		else if ("CompanyDetails".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.setLocked("CompanyDetails_Frame1",true);
		}
		else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.setLocked("CardDetails_Frame1",true);
		}
		else if ("PartnerDetails".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.setLocked("PartnerDetails_Frame1",true);
		}
		else if ("AuthorisedSignDetails".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			loadPicklist_AuthSign();
			formObject.setLocked("AuthorisedSignDetails_Frame1", true);
		}
		else if ("MiscellaneousFields".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.setLocked("MiscellaneousFields_Frame1",true);

		}
		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
		}
		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.setLocked("Liability_New_Frame1",true);
		}
		else if ("CC_Loan".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.setLocked("CC_Loan_Frame1",true);
			loadPicklist_ServiceRequest();
		}
		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.setLocked("AddressDetails_Frame1",true);
			loadPicklist_Address();
		}
		else if ("ReferenceDetails".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.setLocked("ReferenceDetails_Frame1",true);
		}
		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.setLocked("AltContactDetails_Frame1",true);
		}
		else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.setLocked("SupplementCardDetails_Frame1",true);
		}
		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			//++ below code changed by abhishek on 13/11/2017
			formObject.setLocked("FATCA_Frame6",true);
			//++ Above code changed by abhishek on 13/11/2017
		}
		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			//++ below code changed by abhishek on 13/11/2017
			formObject.setLocked("KYC_Frame7",true);
			//++ Above code changed by abhishek on 13/11/2017
		}
		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			//++ below code changed by abhishek on 13/11/2017
			formObject.setLocked("OECD_Frame8",true);
			//++ Above code changed by abhishek on 13/11/2017
		}
		else if ("IncomingDoc".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setLocked("IncomingDoc_Frame1",true);

		}		
		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.setLocked("DecisionHistory_Frame1", true);
			formObject.setLocked("cmplx_DecisionHistory_Decision",false);
			formObject.setLocked("cmplx_DecisionHistory_Remarks",false);
			formObject.setLocked("DecisionHistory_Save",false);
			formObject.setVisible("DecisionHistory_SendSMS",false);
			loadPicklist3();
			//++ below code added by abhishek on 13/11/2017
			formObject.setVisible("Decision_ListView1", true);
			formObject.setTop("Decision_ListView1", formObject.getTop("DecisionHistory_Save")+40);
			formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("Decision_ListView1") + formObject.getHeight("Decision_ListView1") + 20);
			formObject.setHeight("DecisionHistoryContainer",formObject.getHeight("DecisionHistory_Frame1") + 10 );
			//++ Above code added by abhishek on 13/11/2017
			
		}	
		//++ below code changed by abhishek on 13/11/2017
		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setLocked("NotepadDetails_Frame1",true);

		}	
		//++ Above code changed by abhishek on 13/11/2017
		
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Akshay Gupta             
	Description                         :To fetch Customer fragment       

	 ***********************************************************************************  */


	public void CustomerFragment_Load()
	{		
		//changed done to handel Exception in this method, Only try catch block is added along with some null pointer check
		try{
			formObject.fetchFragment("CustomerDetails", "Customer", "q_Customer");
			formObject.setNGFrameState("CustomerDetails", 0);

			String cardnotavailable=formObject.getNGValue("cmplx_Customer_CardNotAvailable");
			String MobNo=formObject.getNGValue("cmplx_Customer_MobNo");
			RLOS.mLogger.info("MobNo"+MobNo);
			String OTPMobNo=formObject.getNGValue("OTP_Mobile_NO");
			RLOS.mLogger.info("OTPMobNo"+OTPMobNo);
			if ("".equalsIgnoreCase(OTPMobNo) && OTPMobNo!=null){
				formObject.setNGValue("OTP_Mobile_NO", MobNo);


			}

			String userName = formObject.getUserName();
			if(formObject.getNGValue("processby_email")==null && userName!=null)

			{
				setMailId(userName);

			}
			/*if (cardnotavailable.equalsIgnoreCase("true")){
				SKLogger.writeLog("RLOS", "CardNotavail"+cardnotavailable);
				formObject.setLocked("ReadFromCard", true);
			}*/
			if	("Y".equalsIgnoreCase(formObject.getNGValue("Is_Customer_Req"))){
				formObject.setLocked("cmplx_Customer_FIrstNAme", true);
				formObject.setLocked("cmplx_Customer_MiddleName", true);
				formObject.setLocked("cmplx_Customer_LAstNAme", true);
				formObject.setLocked("cmplx_Customer_Nationality", true);
				formObject.setLocked("cmplx_Customer_DOb", true);
				formObject.setLocked("cmplx_Customer_PAssportNo", true);
				formObject.setLocked("cmplx_Customer_MobNo", true);
				formObject.setLocked("cmplx_Customer_CardNotAvailable", true);
				formObject.setLocked("cmplx_Customer_NEP", true);
				formObject.setLocked("ReadFromCard", true);
				formObject.setLocked("Customer_CheckBox6", true);
				formObject.setLocked("cmplx_Customer_CIFNO", true);
				formObject.setLocked("FetchDetails", true);
				RLOS.mLogger.info("Value of customer_Req after locking all the fields");	
			}

			//String nep=formObject.getNGValue("cmplx_Customer_NEP");
			String card_not_avail=formObject.getNGValue("cmplx_Customer_card_id_available");
			if ("true".equalsIgnoreCase(card_not_avail))
			{
				formObject.setLocked("Customer_Button1", false);
			}
			else {
				formObject.setLocked("Customer_Button1", true);
			}	

		}
		catch(Exception e){
			RLOS.mLogger.info("RLOS CommonCOde CustomerFragment_Load() Exception occured: "+printException(e));
		}
	}
}


