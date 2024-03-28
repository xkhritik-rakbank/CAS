package com.newgen.omniforms.user;

import java.text.SimpleDateFormat;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.IRepeater;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.excp.CustomExceptionHandler;

public class RLOSCommonCode extends RLOSCommon{
	FormReference formObject = FormContext.getCurrentInstance().getFormReference();

	public void call_Frame_Expanded(ComponentEvent pEvent)
	{
		RLOS.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		if (pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails")) 
		{
			formObject.fetchFragment("GuarantorDetails", "GuarantorDetails", "q_GuarantorDetails");
		}


		else if (pEvent.getSource().getName().equalsIgnoreCase("EmploymentDetails")) 
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

				if(!formObject.getNGValue("Subproduct_productGrid").equalsIgnoreCase("SAL")){
					formObject.setVisible("EMploymentDetails_Label59", false);
					formObject.setVisible("cmplx_EmploymentDetails_IndusSeg", false);
				}
				else{
					formObject.setVisible("EMploymentDetails_Label59", true);
					formObject.setVisible("cmplx_EmploymentDetails_IndusSeg", true);
				}
				if(formObject.getNGValue("PrimaryProduct").equalsIgnoreCase("Credit Card")){
					formObject.setVisible("EMploymentDetails_Label71", false);
					formObject.setVisible("cmplx_EmploymentDetails_EmpContractType", false);
				}
				else{
					formObject.setVisible("EMploymentDetails_Label71", true);
					formObject.setVisible("cmplx_EmploymentDetails_EmpContractType", true);
				}
				//IMFields_Employment();
				//BTCFields_Employment();
				//LimitIncreaseFields_Employment();
				//ProductUpgrade_Employment();
		
		}		 

		else if (pEvent.getSource().getName().equalsIgnoreCase("Incomedetails")) 
		{
			formObject.fetchFragment("Incomedetails", "IncomeDetails", "q_IncomeDetails");
			visibilityFrameIncomeDetails(formObject);	
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails")) {
			hm.put("ReferenceDetails","Clicked");
			popupFlag="N";
			formObject.fetchFragment("ReferenceDetails", "ReferenceDetails", "q_ReferenceDetails");
			LoadPickList("ReferenceDetails_ref_Relationship", "select convert(varchar, Description),code from NG_MASTER_Relationship union select '--Select--','' order by Code");

			//loadPicklist3();
			//loadInDecGrid();
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails")) 
		{

			formObject.fetchFragment("CompanyDetails", "CompanyDetails", "q_CompanyDetails");
			String subprod = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2);
			if(subprod!=null && subprod.equalsIgnoreCase("IM")){
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
				formObject.setVisible("effecLOB", true);	

			}--Commented on 6/10/17 as discussed with shashank-Future CR*/
		
		
			 if(formObject.getNGValue("Subproduct_productGrid").equalsIgnoreCase("Se")){
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
				//formObject.setVisible("CompanyDetails_Label8", false);
				//formObject.setVisible("CompanyDetails_effecLOB", false);
			}

			RLOS.mLogger.info( "CompanyDetailse1:");
		}


		else if (pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignatoryDetails")) 
		{
			formObject.fetchFragment("AuthorisedSignatoryDetails", "AuthorisedSignDetails", "q_AuthorisedSignDetails");
			RLOS.mLogger.info( "AuthorisedSignatoryDetails:");

		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails")) 
		{
			RLOS.mLogger.info( "Saurabh123456");
			formObject.fetchFragment("PartnerDetails", "PartnerDetails", "q_PartnerDetails");

			int authgridRowCount = formObject.getLVWRowCount("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails");
			if(authgridRowCount >0){
				for(int i=0;i<authgridRowCount;i++){
					String soleEmployee = formObject.getNGValue("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails", i, 10);
					if(soleEmployee.equalsIgnoreCase("Yes")){
						formObject.setEnabled("PartnerDetails",false);
					}
				}
			}
		}


		//Modified by akshay for NTB/NEP case only
		else if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistoryContainer"))
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
				RLOS.mLogger.info(" fetched employment details");
			}
			int framestate3=formObject.getNGFrameState("Address_Details_container");
			if(framestate3 == 0){
				RLOS.mLogger.info(" Address_Details_container");
			}
			else {
				formObject.fetchFragment("Address_Details_container", "AddressDetails", "q_AddressDetails");
				//formObject.setTop("Alt_Contact_container",formObject.getTop("Address_Details_Container")+formObject.getHeight("Address_Details_Container"));
				formObject.setTop("ReferenceDetails",formObject.getTop("Address_Details_Container")+formObject.getHeight("Address_Details_Container"));
				formObject.setTop("Alt_Contact_container",formObject.getTop("ReferenceDetails")+25);
				formObject.setTop("Supplementary_Container",formObject.getTop("ReferenceDetails")+50);
				formObject.setTop("CardDetails", formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container"));
				formObject.setTop("FATCA", formObject.getTop("CardDetails")+30);
				formObject.setTop("KYC", formObject.getTop("FATCA")+20);
				formObject.setTop("OECD", formObject.getTop("KYC")+20);

				RLOS.mLogger.info(" fetched address details");
			}

			int framestate4=formObject.getNGFrameState("ReferenceDetails");
			if(framestate4 == 0)
			{
				RLOS.mLogger.info("Alt_Contact_container ");
			}
			else {
				formObject.fetchFragment("ReferenceDetails", "ReferenceDetails", "q_ReferenceDetails");
				formObject.setTop("ReferenceDetails",formObject.getTop("Address_Details_Container")+formObject.getHeight("Address_Details_Container"));
				//formObject.setTop("Alt_Contact_container",formObject.getTop("ReferenceDetails")+formObject.getHeight("ReferenceDetails")+25);
				formObject.setTop("Supplementary_Container",formObject.getTop("ReferenceDetails")+250);
				formObject.setTop("CardDetails", formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container"));
				formObject.setTop("FATCA", formObject.getTop("CardDetails")+30);
				formObject.setTop("KYC", formObject.getTop("FATCA")+20);
				formObject.setTop("OECD", formObject.getTop("KYC")+20);

				RLOS.mLogger.info("fetched address details");
			}


			int framestate5=formObject.getNGFrameState("Alt_Contact_container");
			if(framestate5 == 0){
				RLOS.mLogger.info("Alt_Contact_container");
			}
			else {
				formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
				formObject.setTop("Alt_Contact_container",formObject.getTop("ReferenceDetails")+formObject.getHeight("ReferenceDetails")+70);
				formObject.setTop("Supplementary_Container",formObject.getTop("ReferenceDetails")+250);
				formObject.setTop("CardDetails", formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container")+20);
				formObject.setTop("FATCA", formObject.getTop("CardDetails")+30);
				formObject.setTop("KYC", formObject.getTop("FATCA")+20);
				formObject.setTop("OECD", formObject.getTop("KYC")+20);

				RLOS.mLogger.info("fetched address details");
			}

			int framestate6=formObject.getNGFrameState("KYC");
			if(framestate6 == 0){
				RLOS.mLogger.info("Alt_Contact_container");
			}
			else {
				formObject.fetchFragment("KYC", "KYC", "q_KYC");
				formObject.setTop("KYC", formObject.getTop("FATCA")+30);
				formObject.setTop("OECD", formObject.getTop("KYC")+20);
				RLOS.mLogger.info("fetched address details");
			}

			int framestate7=formObject.getNGFrameState("OECD");
			if(framestate7 == 0){
				RLOS.mLogger.info("Alt_Contact_container");
			}
			else {
				formObject.fetchFragment("OECD", "OECD", "q_OECD");
				formObject.setTop("OECD", formObject.getTop("KYC")+formObject.getHeight("KYC")+20);
				RLOS.mLogger.info("fetched address details");
			}
			//25/07/2017  for mandatory checks fragments should be fetched first

			
			//added Tanshu Aggarwal(23/06/2017)
			
			String countCurrentAccount=formObject.getDataFromDataSource("SELECT count(acctId) from ng_RLOS_CUSTEXPOSE_AcctDetails where AcctType='CURRENT ACCOUNT' AND Wi_Name='"+formObject.getWFWorkitemName()+"' ").get(0).get(0);
			RLOS.mLogger.info(countCurrentAccount);
			formObject.setNGValue("ExistingCustomerAccount",countCurrentAccount);

			String NTB=formObject.getNGValue("NTB");
			String NEP=formObject.getNGValue("NEP");
			RLOS.mLogger.info(NTB+","+NEP);
			//added Tanshu Aggarwal(23/06/2017)
			//if(Product.toUpperCase().contains("PERSONAL LOAN") && formObject.getNGValue("ExistingCustomerAccount").equals("0") && (formObject.getNGValue("NTB").equals("true") || formObject.getNGValue("NEP").equals("true")))
			if(formObject.getNGValue("ExistingCustomerAccount").equals("0"))
			{
				RLOS.mLogger.info("inside visibility true");
				formObject.setVisible("DecisionHistory_Button3", true);//Create CIF/Acc
			}
			else{
				RLOS.mLogger.info("inside visibility false");
				formObject.setVisible("DecisionHistory_Button3", false);//Create CIFAcc
			}
			//added Tanshu Aggarwal(22/06/2017)
			if	(formObject.getNGValue("Is_Customer_Req").equalsIgnoreCase("Y") && formObject.getNGValue("Is_Account_Create").equalsIgnoreCase("Y"))
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



		else if (pEvent.getSource().getName().equalsIgnoreCase("EligibilityAndProductInformation"))
		{
			formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligAndProductInfo");
			formObject.setNGFrameState("EligibilityAndProductInformation", 0);
			RLOS.mLogger.info("Saurabh ELPINFO,Framexpanded");
			Fields_ApplicationType_Eligibility();
			Fields_Eligibility();
			formObject.setEnabled("cmplx_EligibilityAndProductInfo_FinalInterestRate", false);

			RLOS.mLogger.info("Eligibility grid");

			if(formObject.getNGValue("PrimaryProduct").equalsIgnoreCase("Personal Loan")/* && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")*/){
				formObject.setVisible("ELigibiltyAndProductInfo_Frame2", true);//Funding Account Number Grid
				formObject.setVisible("ELigibiltyAndProductInfo_Frame4", true);//Personal Loan
				formObject.setVisible("ELigibiltyAndProductInfo_Frame6",true);//ELigible for Card
				formObject.setVisible("ELigibiltyAndProductInfo_Frame3", false);//Lein Details
				formObject.setVisible("ELigibiltyAndProductInfo_Frame5",false);//CC
				//added by akshay on 29/9/17 for improving visibility and order of fields
				formObject.setTop("ELigibiltyAndProductInfo_Frame6",formObject.getTop("ELigibiltyAndProductInfo_Frame4")+25);
				formObject.setTop("ELigibiltyAndProductInfo_Frame2",formObject.getTop("ELigibiltyAndProductInfo_Frame6")+25);
				//ended by akshay on 29/9/17 for improving visibility and order of fields

				RLOS.mLogger.info( "Funding Account Details now Visible...!!!");
			}

			else if(formObject.getNGValue("PrimaryProduct").equalsIgnoreCase("Credit Card")/* && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")*/){
				formObject.setVisible("ELigibiltyAndProductInfo_Frame5",true);//CC
				formObject.setVisible("ELigibiltyAndProductInfo_Frame6",true);//ELigible for Card
				formObject.setVisible("ELigibiltyAndProductInfo_Frame2", false);//Funding Account Number Grid
				formObject.setVisible("ELigibiltyAndProductInfo_Frame4", false);//Personal Loan
				formObject.setVisible("ELigibiltyAndProductInfo_Frame3", false);//Lein Details
				formObject.setTop("ELigibiltyAndProductInfo_Frame5",5);//CC
				formObject.setTop("ELigibiltyAndProductInfo_Frame6",formObject.getTop("ELigibiltyAndProductInfo_Frame5")+25);//Eligible For Card

				if(formObject.getNGValue("CardProduct_Primary").toUpperCase().contains("-SEC") /*&& formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")*/){
					formObject.setVisible("ELigibiltyAndProductInfo_Frame3", true);//Lein Details
					formObject.setTop("ELigibiltyAndProductInfo_Frame3",formObject.getTop("ELigibiltyAndProductInfo_Frame6")+25);
					RLOS.mLogger.info( "Lein Details now Visible...!!!");
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

		else if (pEvent.getSource().getName().equalsIgnoreCase("Liability_container"))
        {                           
            formObject.fetchFragment("Liability_container", "Liability_New", "q_LiabilityNew");  
            RLOS.mLogger.info( "Inside Liability_container:"); 
    		
           LoadPickList("contractType","Select '--Select--' as Description,'' as code union select convert(varchar,Description),code from ng_master_contract_type with(nolock) order by code");
           LoadPickList("Liability_New_worstStatus24Months","Select '--Select--' as Description,'' as code union select convert(varchar,Description),code from ng_master_Aecb_Codes with(nolock) order by code");

        }


		else if (pEvent.getSource().getName().equalsIgnoreCase("CC_Loan_container")) 
		{
			formObject.fetchFragment("CC_Loan_container", "CC_Loan", "q_CC_Loan");
		}	


		else if (pEvent.getSource().getName().equalsIgnoreCase("Address_Details_container"))

		{	
			formObject.fetchFragment("Address_Details_container", "AddressDetails", "q_AddressDetails");	
		}	


		else if (pEvent.getSource().getName().equalsIgnoreCase("Alt_Contact_container")) 				
		{

			formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
			LoadPickList("AlternateContactDetails_custdomicile", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock) order by code");					

		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails")) 
		{


			formObject.fetchFragment("CardDetails", "CardDetails", "q_CardDetails");
			//int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			//for(int i=0;i<n;i++){
			if(formObject.getNGValue("PrimaryProduct").equalsIgnoreCase("Personal Loan")){
				formObject.setVisible("CardDetails_Label7", true);
				formObject.setVisible("cmplx_CardDetails_statCycle", true);

			}	
			if(formObject.getNGValue("Subproduct_productGrid").equalsIgnoreCase("BTC") || formObject.getNGValue("Subproduct_productGrid").equalsIgnoreCase("Se")){
				formObject.setVisible("CardDetails_Label3", true);
				formObject.setVisible("cmplx_CardDetails_CompanyEmbossingName", true);
				formObject.setLeft("CardDetails_Label5", 552);
				formObject.setLeft("cmplx_CardDetails_SuppCardReq", 552);



			}
			if(formObject.getNGValue("LoanType_Primary").equalsIgnoreCase("Islamic")){
				formObject.setVisible("CardDetails_Label2", true);
				formObject.setVisible("cmplx_CardDetails_CharityOrg", true);
				formObject.setVisible("CardDetails_Label4", true);
				formObject.setVisible("cmplx_CardDetails_CharityAmount", true);


				formObject.setLeft("CardDetails_Label5", 1059);
				formObject.setLeft("cmplx_CardDetails_SuppCardReq", 1059);


			}	

		}


		else if (pEvent.getSource().getName().equalsIgnoreCase("Supplementary_Container")) 
		{


			formObject.fetchFragment("Supplementary_Container", "SupplementCardDetails", "q_SuppCardDetails");


			if (formObject.getNGValue("empType").equalsIgnoreCase("Salaried pensioner") || formObject.getNGValue("empType").equalsIgnoreCase("Salaried")){
				formObject.setVisible("CompEmbName", false);
				formObject.setVisible("SupplementCardDetails_Label7", false);
			}
		}	


		else if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")) 
		{


			formObject.fetchFragment("FATCA", "FATCA", "q_FATCA");


		}	


		else if (pEvent.getSource().getName().equalsIgnoreCase("KYC")) 
		{


			formObject.fetchFragment("KYC", "KYC", "q_KYC");


		}	


		else if (pEvent.getSource().getName().equalsIgnoreCase("OECD")) 
		{


			formObject.fetchFragment("OECD", "OECD", "q_OECD");

		}	

		else  if (pEvent.getSource().getName().equalsIgnoreCase("IncomingDocuments"))

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

		if (pEvent.getSource().getName().equalsIgnoreCase("Notepad_Values")) {
			hm.put("Notepad_Values","Clicked");
			popupFlag="N";
			formObject.fetchFragment("Notepad_Values", "NotepadDetails", "q_Note");
			LoadPickList("NotepadDetails_notedesc", "select '--Select--' as description union select  description from ng_master_notedescription order by description");
			formObject.setNGValue("NotepadDetails_notedesc", "--Select--");
		}

		//ended by yash on 23/8/2017
	}

	public void call_Mouse_Clicked(ComponentEvent pEvent)
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		RLOS_Integration_Input GenXml=new RLOS_Integration_Input();
		String outputResponse = "";
		String ReturnCode="" ;
		String alert_msg="";
		String OTPStatus;
		String SystemErrorCode;
		String BlacklistFlag;
		String DuplicationFlag;
		

		if(pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails_Reference_Add"))
		{
			formObject.setNGValue("ReferenceDetails_reference_wi_name",formObject.getWFWorkitemName());
			RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("reference_wi_name"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_ReferencDetails_Cmplx_gr_ReferenceDetails");
		}
		else  if(pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails_Reference__modify"))
		{
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_ReferencDetails_Cmplx_gr_ReferenceDetails");
		}
		else  if(pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails_Reference_delete"))
		{
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_ReferencDetails_Cmplx_gr_ReferenceDetails");
		}
		else  if(pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails_add"))
		{
			formObject.setNGValue("guarantor_WIName",formObject.getWFWorkitemName());
			RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("guarantor_WIName"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_GuarantorDetails_cmplx_GuarantorGrid");
		}
		else  if(pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails_modify"))
		{
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_GuarantorDetails_cmplx_GuarantorGrid");
		}
		else  if(pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails_delete"))


		{

			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_GuarantorDetails_cmplx_GuarantorGrid");



		}


		else 	if (pEvent.getSource().getName().equalsIgnoreCase("Add")){

			RLOS.mLogger.info( "Inside add button: aman123"+formObject.getNGValue("ReqProd"));


			formObject.setNGValue("Grid_wi_name",formObject.getWFWorkitemName());
			RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("Grid_wi_name"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Product_cmplx_ProductGrid");


			//tanshu aggarwal for documents(1/06/2017)
			IRepeater repObj;
			List<List<String>> deleteQuery ;
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
				deleteQuery = formObject.getNGDataFromDataCache(query);


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
			formObject.setNGFrameState("CardDetails", 1);
			formObject.setNGFrameState("Liability_container", 1);
			Fields_ApplicationType_Employment();
		}	




		else if (pEvent.getSource().getName().equalsIgnoreCase("Modify")){

			RLOS.mLogger.info( "Inside add button: aman123"+formObject.getNGValue("ReqProd"));

			if(formObject.getNGValue("ReqProd").equalsIgnoreCase("Credit Card")){
				formObject.setNGValue("Scheme","");
			}
			else if(formObject.getNGValue("ReqProd").equalsIgnoreCase("Personal Loan")){
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
			formObject.setNGFrameState("CardDetails", 1);
			formObject.setNGFrameState("Liability_container", 1);
			Fields_ApplicationType_Employment();

			if(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")!=null){
				String reqProd = formObject.getNGValue("PrimaryProduct");
				RLOS.mLogger.info("requested Product is: "+reqProd);
				//LoadPickList("cmplx_EmploymentDetails_ApplicationCateg", "select '--Select--' union select  convert(varchar,description) from NG_MASTER_ApplicatonCategory with (nolock)");

				if(reqProd.equalsIgnoreCase("Personal Loan")){
					LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and (product = 'PL' or product='B') order by code");
				}
				else if(reqProd.equalsIgnoreCase("Credit Card")){
					LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and (product = 'CC' or product='B') order by code");	
				}
			}
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("Delete")){
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
			formObject.setNGFrameState("CardDetails", 1);
		}


		else 	 if(pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails_Add"))

		{
			formObject.setNGValue("company_winame",formObject.getWFWorkitemName());
			RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("company_winame"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_CompanyDetails_cmplx_CompanyGrid");


		}

		else  if(pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails_Modify"))


		{

			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_CompanyDetails_cmplx_CompanyGrid");


		}

		else  if(pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails_delete"))


		{

			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_CompanyDetails_cmplx_CompanyGrid");


		}

		else  if(pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails_add"))

		{
			formObject.setNGValue("partner_winame",formObject.getWFWorkitemName());
			RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("partner_winame"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_PartnerDetails_cmplx_partnerGrid");


		}

		else  if(pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails_modify"))


		{

			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_PartnerDetails_cmplx_partnerGrid");


		}

		else  if(pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails_delete"))


		{

			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_PartnerDetails_cmplx_partnerGrid");


		}

		else  if(pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails_add"))

		{
			formObject.setNGValue("AuthorisedSign_wiName",formObject.getWFWorkitemName());
			RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("AuthorisedSign_wiName"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails");


		}

		else  if(pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails_modify"))


		{

			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails");


		}

		else  if(pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails_delete"))


		{

			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails");


		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New_Add")){					
			formObject.setNGValue("LiabilityAddition_wiName",formObject.getWFWorkitemName());
			RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("LiabilityAddition_wiName"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
		}


		else if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New_modify")){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
		}


		else if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New_delete")){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
		}	


		else if (pEvent.getSource().getName().equalsIgnoreCase("addr_Add")  ){
			RLOS.mLogger.info( "Inside addredd grid add button");
			//	boolean flag_addressType= Address_Validate();
			//if(flag_addressType){
			formObject.setNGValue("address_Wi_name",formObject.getWFWorkitemName());
			RLOS.mLogger.info( "Inside address button: "+formObject.getNGValue("address_Wi_name"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_AddressDetails_cmplx_AddressGrid");
			//}


		}




		else if (pEvent.getSource().getName().equalsIgnoreCase("addr_Modify")){
			boolean flag_addressType= Address_Validate();
			if(flag_addressType)
				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_AddressDetails_cmplx_AddressGrid");


		}


		else if (pEvent.getSource().getName().equalsIgnoreCase("addr_Delete")){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_AddressDetails_cmplx_AddressGrid");


		}


		else if (pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails_add")){					
			formObject.setNGValue("supplement_WiName",formObject.getWFWorkitemName());
			RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("supplement_WiName"));
			formObject.ExecuteExternalCommand("NGAddRow", "SupplementCardDetails_cmplx_SupplementGrid");
		}


		else if (pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails_modify")){
			formObject.ExecuteExternalCommand("NGModifyRow", "SupplementCardDetails_cmplx_SupplementGrid");
		}


		else if (pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails_delete")){
			formObject.ExecuteExternalCommand("NGDeleteRow", "SupplementCardDetails_cmplx_SupplementGrid");
		}	


		else if (pEvent.getSource().getName().equalsIgnoreCase("OECD_add")){					
			formObject.setNGValue("OECD_winame",formObject.getWFWorkitemName());
			RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("OECD_winame"));
			//below code by saurabh on 22md july 17.
			formObject.setEnabled("OECD_noTinReason",true);
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_OECD_cmplx_GR_OecdDetails");
		}


		else if (pEvent.getSource().getName().equalsIgnoreCase("OECD_modify") || pEvent.getSource().getName().equalsIgnoreCase("OECD_Button2")){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_OECD_cmplx_GR_OecdDetails");
		}


		else if (pEvent.getSource().getName().equalsIgnoreCase("OECD_delete") || pEvent.getSource().getName().equalsIgnoreCase("OECD_Button3")){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_OECD_cmplx_GR_OecdDetails");
		}	

		//added by akshay on 15/9/17 to add data in BT Grid
		else if (pEvent.getSource().getName().equalsIgnoreCase("BT_Add")){
			formObject.setNGValue("CC_Loan_wi_name",formObject.getWFWorkitemName());
			RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("CC_Loan_wi_name"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_CC_Loan_cmplx_btc");
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("BT_Modify")){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_CC_Loan_cmplx_btc");
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("BT_Delete")){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_CC_Loan_cmplx_btc");
		}

		//ended by akshay on 15/9/17 to add data in BT Grid


	

		else if(pEvent.getSource().getName().equalsIgnoreCase("ReadFromCard")){
			popupFlag = "Y";
			outputResponse = GenXml.GenerateXML("EID_Genuine","");
			ReturnCode =  (outputResponse.contains("<ns3:ServiceStatusCode>")) ? outputResponse.substring(outputResponse.indexOf("<ns3:ServiceStatusCode>")+"</ns3:ServiceStatusCode>".length()-1,outputResponse.indexOf("</ns3:ServiceStatusCode>")):"";
			String Returndesc = (outputResponse.contains("<ns3:ServiceStatusDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ns3:ServiceStatusDesc>")+"</ns3:ServiceStatusDesc>".length()-1,outputResponse.indexOf("</ns3:ServiceStatusDesc>")):"";
			//  ReturnCode="123";
			RLOS.mLogger.info(ReturnCode);
			RLOS.mLogger.info(Returndesc);
			if(ReturnCode.equalsIgnoreCase("s") && Returndesc.equalsIgnoreCase("Valid")){
				RLOS_Integration_Output.valueSetCustomer(outputResponse, "");  
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


		else if(pEvent.getSource().getName().equalsIgnoreCase("FetchDetails"))
		{  
			formObject = FormContext.getCurrentInstance().getFormReference();

			formObject.setNGValue("cmplx_Customer_ISFetchDetails", "Y");
			RLOS.mLogger.info( "Fetch Detail Started");
			String cif_no = formObject.getNGValue("cmplx_Customer_CIFNO");
			RLOS.mLogger.info( "Value of cif_id_avail"+cif_no);
			if(cif_no.equalsIgnoreCase("")){ 				
				RLOS.mLogger.info( "Value of cif_id_avail is false"+cif_no);
				String EID = formObject.getNGValue("cmplx_Customer_EmiratesID");
				RLOS.mLogger.info( "EID value for Entity Details: "+EID );
				String ReadFrom_card_exc = formObject.getNGValue("cmplx_Customer_readfromcardflag");
				if( EID!=null && !EID.equalsIgnoreCase("")&& ReadFrom_card_exc.equalsIgnoreCase("Y"))
				{							
					outputResponse = GenXml.GenerateXML("ENTITY_DETAILS","Primary_CIF");
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					RLOS.mLogger.info(ReturnCode);
					if(ReturnCode.equalsIgnoreCase("0000")){
						formObject.setNGValue("Is_Entity_Details","Y");
						RLOS_Integration_Output.valueSetCustomer(outputResponse , "Primary_CIF");

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
				if(ReturnCode.equalsIgnoreCase("0000") )
				{
					setcustomer_enable();
					popupFlag="Y";
					RLOS_Integration_Output.valueSetCustomer(outputResponse , "Primary_CIF");    
					formObject.setNGValue("NTB","true");//added by Akshay
					formObject.setNGValue("Is_Customer_Eligibility","Y");
					String NTB_flag = formObject.getNGValue("cmplx_Customer_NTB");
					//Customer_enable();
					// if(NTB_flag.equalsIgnoreCase("true")){
					//  Customer_enable();

					//}
					 if(NTB_flag.equalsIgnoreCase("false")){
						 parse_cif_eligibility(outputResponse,"Primary_CIF");
						 formObject.setHeight("Customer_Frame1", 620);
						 formObject.setHeight("CustomerDetails", 700);	
						 //Code change to run Customer details if customer is existing customer in Customer Eligibility start (27-sept-2017)
						 String cif = formObject.getNGValue("cmplx_Customer_CIFNO");
						 if(cif!=null && !cif.equalsIgnoreCase("")){
							 alert_msg =  fetch_cust_details_primary();
						 }
						 //Code change to run Customer details if customer is existing customer in Customer Eligibility End (27-sept-2017)
					}
					
					else if(NTB_flag.equalsIgnoreCase("true")){
						formObject.setVisible("Customer_Frame2", false);
						formObject.setEnabled("cmplx_Customer_VIsaExpiry", true);
						formObject.setLocked("cmplx_Customer_VisaNo", false);
						RLOS.mLogger.info( "inside Customer Eligibility to through Exception to Exit:");
						alert_msg = NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL004");
						//added By Akshay

						String  GCC="BH,IQ,KW,OM,QA,SA,AE";
						if(GCC.contains(formObject.getNGValue("cmplx_Customer_Nationality")))
						{
							RLOS.mLogger.info("Inside GCC for Nationality");
							formObject.setNGValue("cmplx_Customer_GCCNational","Yes");
						}
						else
						{
							formObject.setNGValue("cmplx_Customer_GCCNational","No");
						}
						//ended By akshay		
						throw new ValidatorException(new FacesMessage(alert_msg));

					}/*
					else{
						alert_msg = fetch_cust_details_primary();
					}
*/


					RLOS.mLogger.info(formObject.getNGValue("Is_Customer_Eligibility"));
					RLOS.mLogger.info(formObject.getNGValue("BlacklistFlag"));
					RLOS.mLogger.info(formObject.getNGValue("DuplicationFlag"));
					RLOS.mLogger.info(formObject.getNGValue("IsAcctCustFlag"));

					if(formObject.getNGValue("Is_Customer_Eligibility").equalsIgnoreCase("Y") && formObject.getNGValue("Is_Customer_Details").equalsIgnoreCase("Y"))
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

		
		else if(pEvent.getSource().getName().equalsIgnoreCase("Customer_Button1"))
		{
			//if("Is_Entity_Details".equalsIgnoreCase("Y") && "Is_Customer_Details".equalsIgnoreCase("Y")){
			String NegatedFlag;
			popupFlag="Y";
			outputResponse =GenXml.GenerateXML("CUSTOMER_ELIGIBILITY","Primary_CIF");
			RLOS.mLogger.info("Customer Eligibility");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			RLOS.mLogger.info(ReturnCode);
			formObject.setNGValue("Is_Customer_Eligibility","Y");


			if(ReturnCode.equalsIgnoreCase("0000")){
				RLOS_Integration_Output.valueSetCustomer(outputResponse,"Primary_CIF"); 
				parse_cif_eligibility(outputResponse,"Primary_CIF");
				BlacklistFlag= (outputResponse.contains("<BlacklistFlag>")) ? outputResponse.substring(outputResponse.indexOf("<BlacklistFlag>")+"</BlacklistFlag>".length()-1,outputResponse.indexOf("</BlacklistFlag>")):"";
				RLOS.mLogger.info("Customer is BlacklistedFlag"+BlacklistFlag);
				DuplicationFlag= (outputResponse.contains("<DuplicationFlag>")) ? outputResponse.substring(outputResponse.indexOf("<DuplicationFlag>")+"</DuplicationFlag>".length()-1,outputResponse.indexOf("</DuplicationFlag>")):"";
				RLOS.mLogger.info("Customer is DuplicationFlag"+DuplicationFlag);
				NegatedFlag= (outputResponse.contains("<NegatedFlag>")) ? outputResponse.substring(outputResponse.indexOf("<NegatedFlag>")+"</NegatedFlag>".length()-1,outputResponse.indexOf("</NegatedFlag>")):"";
				RLOS.mLogger.info("Customer is NegatedFlag"+NegatedFlag);
				formObject.setNGValue("Is_Customer_Eligibility","Y");
				formObject.RaiseEvent("WFSave");
				RLOS.mLogger.info(formObject.getNGValue("Is_Customer_Eligibility"));
				formObject.setNGValue("BlacklistFlag",BlacklistFlag);
				formObject.setNGValue("DuplicationFlag",DuplicationFlag);
				formObject.setNGValue("IsAcctCustFlag",NegatedFlag);
				String NTB_flag = formObject.getNGValue("cmplx_Customer_NTB");
				if(NTB_flag.equalsIgnoreCase("true")){
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
		else if(pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails_FetchDetails")){
			
			String EmiratesID = formObject.getNGValue("SupplementCardDetails_Text1");
			RLOS.mLogger.info( "EID value for Entity Details for Supplementary Cards: "+EmiratesID);
			String primaryCif = null;
			boolean isEntityDetailsSuccess = false;
			
			if( EmiratesID!=null && !EmiratesID.equalsIgnoreCase("")){
				outputResponse = GenXml.GenerateXML("ENTITY_DETAILS","Supplementary_Card_Details");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				RLOS.mLogger.info(ReturnCode);
				if(ReturnCode.equalsIgnoreCase("0000")){
					//RLOS_Integration_Output.valueSetCustomer(outputResponse , "Supplementary_Card_Details");
					primaryCif = (outputResponse.contains("<CIFID>")) ? outputResponse.substring(outputResponse.indexOf("<CIFID>")+"</CIFID>".length()-1,outputResponse.indexOf("</CIFID>")):"";
					formObject.setNGValue("Supplementary_CIFNO",primaryCif);
					isEntityDetailsSuccess = true;
					alert_msg = fetch_cust_details_supplementary();
				}

				RLOS.mLogger.info(primaryCif);
			}
			if(!isEntityDetailsSuccess || (primaryCif==null || primaryCif.equalsIgnoreCase(""))){
				outputResponse =GenXml.GenerateXML("CUSTOMER_ELIGIBILITY","Supplementary_Card_Details");
				RLOS.mLogger.info("Customer Eligibility");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				RLOS.mLogger.info(ReturnCode);
				if(ReturnCode.equalsIgnoreCase("0000") )
				{
					RLOS_Integration_Output.valueSetCustomer(outputResponse ,"Supplementary_Card_Details");    
					parse_cif_eligibility(outputResponse,"Supplementary_Card_Details");
					alert_msg = fetch_cust_details_supplementary();
					
				}
			}
			
		}



		else   if(pEvent.getSource().getName().equalsIgnoreCase("Send_OTP_Btn"))
		{
			formObject.setNGValue("otp_ref_no", formObject.getWFFolderId());
			RLOS.mLogger.info( formObject.getWFFolderId()+"");
			hm.put("Send_OTP_Btn","Clicked");
			popupFlag="Y";
			outputResponse = GenXml.GenerateXML("OTP_MANAGEMENT","GenerateOTP");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			RLOS.mLogger.info(ReturnCode);
			if(ReturnCode.equalsIgnoreCase("0000") ){
				RLOS_Integration_Output.valueSetCustomer(outputResponse , "");    
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

		else   if(pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails_Button4")){

			outputResponse = GenXml.GenerateXML("CUSTOMER_DETAILS","Signatory_CIF");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			RLOS.mLogger.info(ReturnCode);
			if(ReturnCode.equalsIgnoreCase("0000") ){
				formObject.setNGValue("Is_Customer_Details_AuthSign","Y");
				RLOS.mLogger.info("value of Guarantor_CIF"+formObject.getNGValue("Is_Customer_Details_AuthSign"));
				RLOS_Integration_Output.valueSetCustomer(outputResponse , "Signatory_CIF");    
				RLOS.mLogger.info("Guarantor_CIF is generated");
				RLOS.mLogger.info(formObject.getNGValue("Is_Customer_Details_AuthSign"));
			}
			else{
				RLOS.mLogger.info("Customer Details is not generated");
				formObject.setNGValue("Is_Customer_Details_AuthSign","N");
			}
			RLOS.mLogger.info(formObject.getNGValue("Is_Customer_Details_AuthSign"));
		}


		else   if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_SendSMS"))
		{

			RLOS.mLogger.info( "");

			outputResponse = GenXml.GenerateXML("SEND_ADHOC_ALERT","");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			RLOS.mLogger.info(ReturnCode);
			if(ReturnCode.equalsIgnoreCase("0000") ){
				RLOS_Integration_Output.valueSetCustomer(outputResponse , "");    
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




		else  if(pEvent.getSource().getName().equalsIgnoreCase("Validate_OTP_Btn"))
		{

			outputResponse = GenXml.GenerateXML("OTP_MANAGEMENT","ValidateOTP");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			RLOS.mLogger.info(ReturnCode);
			OTPStatus=(outputResponse.contains("<OTPStatus>")) ? outputResponse.substring(outputResponse.indexOf("<OTPStatus>")+"</OTPStatus>".length()-1,outputResponse.indexOf("</OTPStatus>")):"";    
			RLOS.mLogger.info(OTPStatus);
			if(ReturnCode.equalsIgnoreCase("0000") ){
				RLOS_Integration_Output.valueSetCustomer(outputResponse , "");    
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


		else  if(pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails_Button3")){
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
*/					if(ReturnCode.equalsIgnoreCase("0000") ){
				formObject.setNGValue("Is_Customer_Details_CompanyCIF","Y");

				RLOS.mLogger.info("value of company Details corporation"+formObject.getNGValue("Is_Customer_Details_CompanyCIF"));

				RLOS_Integration_Output.valueSetCustomer(outputResponse , "Corporation_CIF");  
				try{

				//	String Date1=BusinessIncDate;
					SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-mm-dd");
					SimpleDateFormat sdf2=new SimpleDateFormat("dd/mm/yyyy");
					String estbDate = formObject.getNGValue("estbDate");
					String TLExpiry = formObject.getNGValue("TLExpiry");
					if(estbDate!=null&&!estbDate.equalsIgnoreCase("")){
						formObject.setNGValue("estbDate",sdf2.format(sdf1.parse(estbDate)),false);
						RLOS.mLogger.info(formObject.getNGValue("estbDate"));
					}
					if(TLExpiry!=null&&!TLExpiry.equalsIgnoreCase("")){
						formObject.setNGValue("TLExpiry",sdf2.format(sdf1.parse(TLExpiry)),false);
						RLOS.mLogger.info(formObject.getNGValue("TLExpiry"));
					}
				}
				catch(Exception ex){
					RLOS.logException(ex);
				}
				RLOS.mLogger.info("corporation cif");
				RLOS.mLogger.info(formObject.getNGValue("Is_Customer_Details_CompanyCIF"));
			}
			else{
				RLOS.mLogger.info("Customer Details Corporation CIF is not generated");
				formObject.setNGValue("Is_Customer_Details_CompanyCIF","N");
			}
			RLOS.mLogger.info(formObject.getNGValue("Is_Customer_Details_CompanyCIF"));
		}



		else  if (pEvent.getSource().getName().equalsIgnoreCase("Button9")){

			if(!formObject.isVisible("IncomeDetails_Frame1")){
				formObject.fetchFragment("Incomedetails", "IncomeDetails", "q_IncomeDetails");
				formObject.setNGFrameState("Incomedetails", 0);
				formObject.setNGFrameState("Incomedetails", 1);

				hm.put("Button9","Clicked");
				popupFlag="N";
				outputResponse = GenXml.GenerateXML("ACCOUNT_SUMMARY","");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				RLOS.mLogger.info(ReturnCode);
				if(ReturnCode.equalsIgnoreCase("0000") ){
					RLOS_Integration_Output.valueSetCustomer(outputResponse , "");
					formObject.setNGValue("Is_Account_Summary","Y");
				}
				else{
					formObject.setNGValue("Is_Account_Summary","N");
				}
				RLOS.mLogger.info(formObject.getNGValue("Is_Account_Summary"));
				//ended
				try
				{
					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
				}
				finally{hm.clear();}

			}

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


		else   if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails_Button2")) {
			String EmpName=formObject.getNGValue("EMploymentDetails_Text21");
			String EmpCode=formObject.getNGValue("EMploymentDetails_Text22");
			RLOS.mLogger.info( "EMpName$"+EmpName+"$");
			String query;
			if(EmpName.trim().equalsIgnoreCase(""))
				query="select distinct(EMPR_NAME),EMPLOYER_CODE,INCLUDED_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,EMPLOYER_CATEGORY_PL,COMPANY_STATUS_CC,COMPANY_STATUS_PL from NG_RLOS_ALOC_OFFLINE_DATA where EMPLOYER_CODE Like '%"+EmpCode+"%'";

			else if(EmpCode.trim().equalsIgnoreCase(""))
				query="select distinct(EMPR_NAME),EMPLOYER_CODE,INCLUDED_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,EMPLOYER_CATEGORY_PL,COMPANY_STATUS_CC,COMPANY_STATUS_PL from NG_RLOS_ALOC_OFFLINE_DATA where EMPR_NAME Like '%"+EmpName + "%'";

			else
				query="select distinct(EMPR_NAME),EMPLOYER_CODE,INCLUDED_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,EMPLOYER_CATEGORY_PL,COMPANY_STATUS_CC,COMPANY_STATUS_PL from NG_RLOS_ALOC_OFFLINE_DATA where EMPR_NAME Like '%"+EmpName + "%' or EMPLOYER_CODE Like '%"+EmpCode+"%'";

			RLOS.mLogger.info( "query is: "+query);
			populatePickListWindow(query,"EMploymentDetails_Button2", "Employer Name,Employer Code,INCLUDED IN PL ALOC,INCLUDED IN CC ALOC,INDUSTRY SECTOR,INDUSTRY MACRO,INDUSTRY MICRO,CONSTITUTION,NAME OF FREEZONE AUTHORITY,Employer Category PL, Employer Status CC,Employer Status PL", true, 20);			     
		}

		else if(pEvent.getSource().getName().equalsIgnoreCase("cmplx_ExternalLiabilities_AECBConsent")){
			if(formObject.getNGValue("cmplx_ExternalLiabilities_AECBConsent").equals("true"))
				formObject.setEnabled("ExternalLiabilities_Button1",true);
			else
				formObject.setEnabled("ExternalLiabilities_Button1",false);
		}


		else if(pEvent.getSource().getName().equalsIgnoreCase("cmplx_EmploymentDetails_Others")){
			try{
			if(formObject.getNGValue("cmplx_EmploymentDetails_Others").equalsIgnoreCase("true")){
				LoadPickList("cmplx_EmploymentDetails_EmpStatusCC","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_EmployerStatusCC where isActive='Y' and product='Credit Card' order by code" );					
				formObject.setLocked("cmplx_EmploymentDetails_EmpStatusCC",false);
				if(formObject.getNGValue("Subproduct_productGrid").equals("IM")){
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
	
		else if (pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails_Button2"))
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


		else if (pEvent.getSource().getName().equalsIgnoreCase("FATCA_Button1")){
			String text=formObject.getNGItemText("cmplx_FATCA_listedreason", formObject.getSelectedIndex("cmplx_FATCA_listedreason"));
			RLOS.mLogger.info( "Inside FATCA_Button1 "+text);
			formObject.addItem("cmplx_FATCA_selectedreason", text);
			try {
				formObject.removeItem("cmplx_FATCA_listedreason", formObject.getSelectedIndex("cmplx_FATCA_listedreason"));
				formObject.setSelectedIndex("cmplx_FATCA_listedreason", -1);

			}catch (Exception e) {
				
				RLOS.logException(e);
			}

		}


		else if (pEvent.getSource().getName().equalsIgnoreCase("FATCA_Button2")){
			RLOS.mLogger.info( "Inside FATCA_Button2 ");
			formObject.addItem("cmplx_FATCA_listedreason", formObject.getNGItemText("cmplx_FATCA_selectedreason", formObject.getSelectedIndex("cmplx_FATCA_selectedreason")));
			try {
				formObject.removeItem("cmplx_FATCA_selectedreason", formObject.getSelectedIndex("cmplx_FATCA_selectedreason"));
				formObject.setSelectedIndex("cmplx_FATCA_selectedreason", -1);
			} catch (Exception e) {
				
				RLOS.logException(e);
			}
		}


		else if (pEvent.getSource().getName().equalsIgnoreCase("IncomingDoc_AddFromPCButton")){
			IRepeater repObj;
			repObj = formObject.getRepeaterControl("IncomingDoc_Frame");
			repObj.getValue(repObj.getChangeIndex(),"IncomingDoc_Label1");
			RLOS.mLogger.info("value of repeater:"+repObj.getValue(repObj.getChangeIndex(),"IncomingDoc_Label1"));


		}    


		else if(pEvent.getSource().getName().equalsIgnoreCase("Reject")){
			RLOS.mLogger.info(formObject.getSelectedSheet(pEvent.getSource().getName())+"");
			formObject.setSelectedSheet("ParentTab",8);		
			formObject.fetchFragment("DecisionHistoryContainer", "DecisionHistory", "q_DecisionHistory");
			formObject.setNGFrameState("DecisionHistoryContainer", 0);
			formObject.setNGFrameState("DecisionHistoryContainer", 1);
			formObject.setNGFrameState("DecisionHistoryContainer", 0);
			formObject.setNGFocus("DecisionHistory_Button4");
		}


		else  if(pEvent.getSource().getName().equalsIgnoreCase("Liability_New_Button1"))

		{
			hm.put("Liability_New_Button1","Clicked");
			popupFlag="N";


			//added

			outputResponse = GenXml.GenerateXML("AECB","");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			RLOS.mLogger.info(ReturnCode);

			if(ReturnCode.equalsIgnoreCase("0000") ){
				RLOS_Integration_Output.valueSetCustomer(outputResponse , "");


				formObject.setNGValue("IS_AECB","Y");

			}
			else{
				formObject.setNGValue("IS_AECB","Y");
			}
			RLOS.mLogger.info(formObject.getNGValue("IS_AECB"));
			//ended
			try

			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));

			}
			finally{hm.clear();}


		}

		else  if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Button3"))
		{
			RLOS_Integration_Output.getCustAddress_details();

			String NTB_flag = formObject.getNGValue("cmplx_Customer_NTB");
			String NEP_flag = formObject.getNGValue("cmplx_Customer_NEP");
			String CIF_no = formObject.getNGValue("cmplx_Customer_CIFNO");
			RLOS.mLogger.info( "inside create Account/Customer NTB value: "+NTB_flag );
			if(NTB_flag.equalsIgnoreCase("true") || NEP_flag.equalsIgnoreCase("true")||CIF_no.equalsIgnoreCase("")){
				formObject.setNGValue("curr_user_name",formObject.getUserName());
				outputResponse = GenXml.GenerateXML("NEW_CUSTOMER_REQ","");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				RLOS.mLogger.info(ReturnCode);
				if(ReturnCode.equalsIgnoreCase("0000")){
					formObject.setEnabled("DecisionHistory_Button3",false);
					RLOS_Integration_Output.valueSetCustomer(outputResponse , "");   
					formObject.setNGValue("cmplx_DecisionHistory_CifNo", formObject.getNGValue("cmplx_Customer_CIFNO"));
					RLOS.mLogger.info("Inside if of New customer Req");


					outputResponse = GenXml.GenerateXML("NEW_ACCOUNT_REQ","");
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					RLOS.mLogger.info(ReturnCode);

					if(ReturnCode.equalsIgnoreCase("0000") ){
						RLOS_Integration_Output.valueSetCustomer(outputResponse , "");    
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


				if((formObject.getNGValue("cmplx_Customer_NTB").equals("false") && countCurrentAccount==0)){
					RLOS.mLogger.info("NTB: "+formObject.getNGValue("cmplx_Customer_NTB")+" Account Count:"+formObject.getNGValue("AccountCount"));
					//createCustomer();
					outputResponse = GenXml.GenerateXML("NEW_ACCOUNT_REQ","");
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					RLOS.mLogger.info(ReturnCode);
					if(ReturnCode.equalsIgnoreCase("0000") ){
						RLOS_Integration_Output.valueSetCustomer(outputResponse , "");    
						formObject.setNGValue("Is_Account_Create","Y");
					}
					else{
						formObject.setNGValue("Is_Account_Create","N");
					}
					RLOS.mLogger.info(formObject.getNGValue("Is_Account_Create"));
					if(formObject.getNGValue("Is_Account_Create").equalsIgnoreCase("Y")){ 
						RLOS.mLogger.info("inside if condition");
						formObject.setEnabled("DecisionHistory_Button5", false);     
					}
					else{
						formObject.setEnabled("DecisionHistory_Button5", true);
					}
				}
			}
		}

		/*else    if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_FinnancialSummary") || pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_FinacialSummarySelf"))
		{
			hm.put("IncomeDetails_FinancialSummary","Clicked");
			popupFlag="N";
			//    if(formObject.getNGValue("cmplx_Customer_NTB")=="true")
			//{
			outputResponse = GenerateXML("FINANCIAL_SUMMARY","");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			RLOS.mLogger.info(ReturnCode);
			if(ReturnCode.equalsIgnoreCase("0000") ){
				RLOS_Integration_Output.valueSetCustomer(outputResponse , "");


				formObject.setNGValue("Is_Financial_Summary","Y");
			}
			else{
				formObject.setNGValue("Is_Financial_Summary","N");
			}
			//    }

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}*/





		/*else   if(pEvent.getSource().getName().equalsIgnoreCase("ReadFromCIF")){

			outputResponse = GenerateXML("CUSTOMER_DETAILS","Guarantor_CIF");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			RLOS.mLogger.info(ReturnCode);
			if(ReturnCode.equalsIgnoreCase("0000") ){
				formObject.setNGValue("Is_Customer_Details","Y");
				RLOS.mLogger.info("value of Guarantor_CIF"+formObject.getNGValue("Is_Customer_Details"));
				RLOS_Integration_Output.valueSetCustomer(outputResponse , "Guarantor_CIF");    
				RLOS.mLogger.info("Guarantor_CIF is generated");
				RLOS.mLogger.info(formObject.getNGValue("Is_Customer_Details"));
			}
			else{
				RLOS.mLogger.info("Customer Details is not generated");
				formObject.setNGValue("Is_Customer_Details","N");
			}
			RLOS.mLogger.info(formObject.getNGValue("Is_Customer_Details"));
		}
		 */
		else   if(pEvent.getSource().getName().equalsIgnoreCase("ReadFromCIF")){

			outputResponse = GenXml.GenerateXML("CUSTOMER_DETAILS","Guarantor_CIF");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			RLOS.mLogger.info(ReturnCode);
			if(ReturnCode.equalsIgnoreCase("0000")){
				formObject.setNGValue("Is_Customer_Details_Guarantor","Y");
				RLOS.mLogger.info("value of Guarantor_CIF"+formObject.getNGValue("Is_Customer_Details_Guarantor"));
				RLOS_Integration_Output.valueSetCustomer(outputResponse , "Guarantor_CIF");    
				RLOS.mLogger.info("Guarantor_CIF is generated");
				RLOS.mLogger.info(formObject.getNGValue("Is_Customer_Details_Guarantor"));
				formObject.setNGValue("passExpiry", Convert_dateFormat(formObject.getNGValue("passExpiry"), "yyyy-mm-dd", "dd/mm/yyyy"),false);
				formObject.setNGValue("dob", Convert_dateFormat(formObject.getNGValue("dob"), "yyyy-mm-dd", "dd/mm/yyyy"),false);
				formObject.setNGValue("eidExpiry", Convert_dateFormat(formObject.getNGValue("eidExpiry"), "yyyy-mm-dd", "dd/mm/yyyy"),false);
				formObject.setNGValue("visaExpiry", Convert_dateFormat(formObject.getNGValue("visaExpiry"), "yyyy-mm-dd", "dd/mm/yyyy"),false);
				//code added by saurabh on 22nd July 17.
				if(formObject.getNGValue("dob")!=null && !formObject.getNGValue("dob").equalsIgnoreCase("") && !formObject.getNGValue("dob").equalsIgnoreCase(" ")){
					getAge(formObject.getNGValue("dob"), "age");	
				}
			}
			else{
				RLOS.mLogger.info("Customer Details is not generated");
				formObject.setNGValue("Is_Customer_Details_Guarantor","N");
			}
			RLOS.mLogger.info(formObject.getNGValue("Is_Customer_Details_Guarantor"));
		}
		//Added for the World check call in case of self employed
		else if (pEvent.getSource().getName().equalsIgnoreCase("FetchWorldCheck_SE")) {
			popupFlag="Y";			
		//	columnValues=columnValues.join(",",columnValues_arr);
			RLOS.mLogger.info("inside worldcheck"); 
			outputResponse = GenXml.GenerateXML("CUSTOMER_SEARCH_REQUEST","");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			RLOS.mLogger.info(ReturnCode);

			if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000") ){
				RLOS.mLogger.info("inside if of WORLDCHECK");
				//formObject.fetchFragment("WorldCheck", "WorldCheck1", "q_WorldCheck");	
				//formObject.setVisible("WorldCheck", false);
				RLOS_Integration_Output.valueSetCustomer(outputResponse,"");	
				formObject.setNGValue("IS_WORLD_CHECK","Y");
				alert_msg= NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL015");
			}
			else if(ReturnCode.equalsIgnoreCase("9999")){
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
		else if (pEvent.getSource().getName().equalsIgnoreCase("WorldCheck_fetch")) {
			popupFlag="Y";
			//columnValues=columnValues.join(",",columnValues_arr);
			RLOS.mLogger.info("inside worldcheck"); 
			outputResponse = GenXml.GenerateXML("CUSTOMER_SEARCH_REQUEST","");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			RLOS.mLogger.info(ReturnCode);

			if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000") ){
				RLOS.mLogger.info("inside if of WORLDCHECK");
				formObject.fetchFragment("WorldCheck", "WorldCheck1", "q_WorldCheck");	
				formObject.setVisible("WorldCheck", false);
				RLOS_Integration_Output.valueSetCustomer(outputResponse,"");	
				formObject.setNGValue("IS_WORLD_CHECK","Y");
				alert_msg= NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL015");
			}
			else if(ReturnCode.equalsIgnoreCase("9999")){
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
		
		
		else if(pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo_Button1"))
		{		popupFlag="Y";
		formObject.setNGValue("DecCallFired","Eligibility");
		String RequiredProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
		RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse is : "+outputResponse);
		formObject.fetchFragment("WorldCheck", "WorldCheck1", "q_WorldCheck");	
		formObject.setVisible("WorldCheck", false);
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
		if ((formObject.getNGValue("Winame") != null) || formObject.getNGValue("Winame").equalsIgnoreCase("")){
			formObject.setNGValue("Winame", formObject.getWFWorkitemName());
		}
		if (RequiredProd.equalsIgnoreCase("Credit Card")){
			outputResponse = GenXml.GenerateXML("DECTECH","CreditCard");
			RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse is : "+outputResponse);
			RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse ASDASDASDASD : "+outputResponse);
			
			SystemErrorCode =  (outputResponse.contains("<SystemErrorCode>")) ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"Error";
			RLOS.mLogger.info(SystemErrorCode);
			RLOS.mLogger.info(outputResponse);

			if(SystemErrorCode.equalsIgnoreCase("")){
				RLOS_Integration_Output.valueSetCustomer(outputResponse , "");   
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

			if(SystemErrorCode.equalsIgnoreCase("")){
				RLOS_Integration_Output.valueSetCustomer(outputResponse , "");   
				alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL018");
				RLOS.mLogger.info("after value set customer for dectech call");
				formObject.RaiseEvent("WFSave");
			}
			else{
				alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL019");
		        }
		
		}
		
		//change to set focus on the same button.	
		formObject.setNGFocus("ELigibiltyAndProductInfo_Button1");
		throw new ValidatorException(new FacesMessage(alert_msg));
		//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse ALERT : "+outputResponse);
		//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse WFS : "+outputResponse);
		
}
		else if(pEvent.getSource().getName().equalsIgnoreCase("Eligibility_Emp"))
			{		popupFlag="Y";
			formObject.setNGValue("DecCallFired","Eligibility");
			String RequiredProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
			RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse is : "+outputResponse);
			formObject.fetchFragment("WorldCheck", "WorldCheck1", "q_WorldCheck");	
			formObject.setVisible("WorldCheck", false);
			if ((formObject.getNGValue("Winame") != null) || formObject.getNGValue("Winame").equalsIgnoreCase("")){
				formObject.setNGValue("Winame", formObject.getWFWorkitemName());
			}
			if (RequiredProd.equalsIgnoreCase("Credit Card")){
				outputResponse = GenXml.GenerateXML("DECTECH","CreditCard");
				RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse is : "+outputResponse);
				RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse ASDASDASDASD : "+outputResponse);
				
			//	ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				SystemErrorCode =  (outputResponse.contains("<SystemErrorCode>")) ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"Error";
				RLOS.mLogger.info(SystemErrorCode);
				RLOS.mLogger.info(outputResponse);
	
				if(SystemErrorCode.equalsIgnoreCase("")){
					RLOS.mLogger.info(formObject.getSelectedSheet(pEvent.getSource().getName())+"");
					formObject.setSelectedSheet("ParentTab",3);		
					formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligAndProductInfo");
					String tenure=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 7);
					//RLOS.mLogger.info( "Funding Account Details now Visible...!!!");
					formObject.setNGValue("cmplx_EligibilityAndProductInfo_Tenor", tenure);

					formObject.setNGFrameState("EligibilityAndProductInformation", 0);
					formObject.setNGFrameState("EligibilityAndProductInformation", 1);
					formObject.setNGFrameState("EligibilityAndProductInformation", 0);
					
					//Eligibilityfields();
					RLOS_Integration_Output.valueSetCustomer(outputResponse , "");   
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
	
				if((SystemErrorCode.equalsIgnoreCase(""))&& !ReturnCode.equalsIgnoreCase("0")){
					RLOS.mLogger.info(formObject.getSelectedSheet(pEvent.getSource().getName())+"");
					formObject.setSelectedSheet("ParentTab",3);		
					formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligAndProductInfo");
					String tenure=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 7);
					formObject.setNGValue("cmplx_EligibilityAndProductInfo_Tenor", tenure);
					formObject.setNGFrameState("EligibilityAndProductInformation", 0);
					formObject.setNGFrameState("EligibilityAndProductInformation", 1);
					formObject.setNGFrameState("EligibilityAndProductInformation", 0);
					//Eligibilityfields();
					RLOS_Integration_Output.valueSetCustomer(outputResponse , "");   
					alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL018");
					RLOS.mLogger.info("after value set customer for dectech call");
					formObject.RaiseEvent("WFSave");
							
				}
				else{
					alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL019");
					
			        }
			
			}
			//change to set focus on the same button.
			formObject.setNGFocus("Eligibility_Emp");
			throw new ValidatorException(new FacesMessage(alert_msg));
			//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse ALERT : "+outputResponse);
			//RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse WFS : "+outputResponse);
			
	}
		 //Added for Dectech Call on Self Employed Case
		else if(pEvent.getSource().getName().equalsIgnoreCase("CheckEligibility_SE"))
			{		popupFlag="Y";
			formObject.setNGValue("DecCallFired","Eligibility");
			String RequiredProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
			RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse is : "+outputResponse);
			formObject.fetchFragment("WorldCheck", "WorldCheck1", "q_WorldCheck");	
			formObject.setVisible("WorldCheck", false);
			if ((formObject.getNGValue("Winame") != null) || formObject.getNGValue("Winame").equalsIgnoreCase("")){
				formObject.setNGValue("Winame", formObject.getWFWorkitemName());
			}
			if (RequiredProd.equalsIgnoreCase("Credit Card")){
				outputResponse = GenXml.GenerateXML("DECTECH","CreditCard");
				RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse is : "+outputResponse);
				RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse ASDASDASDASD : "+outputResponse);
				
				//ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				SystemErrorCode =  (outputResponse.contains("<SystemErrorCode>")) ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"Error";
				RLOS.mLogger.info(SystemErrorCode);
				RLOS.mLogger.info(outputResponse);
	
				if(SystemErrorCode.equalsIgnoreCase("")){
					RLOS.mLogger.info(formObject.getSelectedSheet(pEvent.getSource().getName())+"");
					formObject.setSelectedSheet("ParentTab",3);		
					formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligAndProductInfo");
					String tenure=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 7);
					//RLOS.mLogger.info( "Funding Account Details now Visible...!!!");
					formObject.setNGValue("cmplx_EligibilityAndProductInfo_Tenor", tenure);

					formObject.setNGFrameState("EligibilityAndProductInformation", 0);
					formObject.setNGFrameState("EligibilityAndProductInformation", 1);
					formObject.setNGFrameState("EligibilityAndProductInformation", 0);
					
					//Eligibilityfields();
					RLOS_Integration_Output.valueSetCustomer(outputResponse , "");   
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
	
				if(SystemErrorCode.equalsIgnoreCase("")){
					RLOS.mLogger.info(formObject.getSelectedSheet(pEvent.getSource().getName())+"");
					formObject.setSelectedSheet("ParentTab",3);		
					formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligAndProductInfo");
					String tenure=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 7);
					formObject.setNGValue("cmplx_EligibilityAndProductInfo_Tenor", tenure);
					formObject.setNGFrameState("EligibilityAndProductInformation", 0);
					formObject.setNGFrameState("EligibilityAndProductInformation", 1);
					formObject.setNGFrameState("EligibilityAndProductInformation", 0);
					//Eligibilityfields();
					RLOS_Integration_Output.valueSetCustomer(outputResponse , "");   
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
		else if(pEvent.getSource().getName().equalsIgnoreCase("Button2"))
			{		popupFlag="Y";
			formObject.setNGValue("DecCallFired","CalculateDBR");
			formObject.fetchFragment("WorldCheck", "WorldCheck1", "q_WorldCheck");	
			formObject.setVisible("WorldCheck", false);
			String RequiredProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
			RLOS.mLogger.info("$$After RequiredProd for dectech call..outputResponse is : "+RequiredProd);
			if ((formObject.getNGValue("Winame") != null) || formObject.getNGValue("Winame").equalsIgnoreCase("")){
				formObject.setNGValue("Winame", formObject.getWFWorkitemName());
			}
			if (RequiredProd.equalsIgnoreCase("Credit Card")){
				outputResponse = GenXml.GenerateXML("DECTECH","CreditCard");
				RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse is : "+outputResponse);
				RLOS.mLogger.info("$$After Generatexml for dectech call..outputResponse ASDASDASDASD : "+outputResponse);
				//ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				SystemErrorCode =  (outputResponse.contains("<SystemErrorCode>")) ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"Error";
				RLOS.mLogger.info(SystemErrorCode);
				RLOS.mLogger.info(outputResponse);
	
				if(SystemErrorCode.equalsIgnoreCase("")){
					RLOS_Integration_Output.valueSetCustomer(outputResponse , "");   
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
				if(SystemErrorCode.equalsIgnoreCase("")){
					RLOS_Integration_Output.valueSetCustomer(outputResponse , "");   
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
		
		

		else if(pEvent.getSource().getName().equalsIgnoreCase("Customer_save")){
			formObject.setNGValue("PassportNo",formObject.getNGValue("cmplx_Customer_PAssportNo"));
			formObject.setNGValue("MobileNo",formObject.getNGValue("cmplx_Customer_MobNo"));
			if(formObject.getNGValue("cmplx_Customer_MiddleName").equals(""))
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


		else if(pEvent.getSource().getName().equalsIgnoreCase("Product_Save"))
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


		else 	if(pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails_Save")){
			formObject.saveFragment("GuarantorDetails");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL022");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_Salaried_Save")){
			formObject.saveFragment("Incomedetails");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL023");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_SelfEmployed_Save")){
			formObject.saveFragment("Incomedetails");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL024");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if(pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails_Save")){
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


		else if(pEvent.getSource().getName().equalsIgnoreCase("Liability_New_Save")){
			formObject.saveFragment("Liability_container");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL026");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if(pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails_Save")){
			formObject.setNGValue("Employer_Name", formObject.getNGValue("cmplx_EmploymentDetails_EmpName"));//added By akshay on 16/9/17 to set header
			formObject.setNGValue("lbl_Comp_Name_val",formObject.getNGValue("Employer_Name"));

			formObject.saveFragment("EmploymentDetails");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL027");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if(pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo_Save")){
			formObject.saveFragment("EligibilityAndProductInformation");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL028");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else 	if(pEvent.getSource().getName().equalsIgnoreCase("MiscellaneousFields_Save")){
			formObject.saveFragment("MiscFields");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL029");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if(pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_Save")){
			formObject.saveFragment("Address_Details_container");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL030");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if(pEvent.getSource().getName().equalsIgnoreCase("ContactDetails_Save")){
			formObject.saveFragment("Alt_Contact_container");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL031");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else 	if(pEvent.getSource().getName().equalsIgnoreCase("CardDetails_save")){
			formObject.saveFragment("CardDetails");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL032");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if(pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails_Save")){
			formObject.saveFragment("Supplementary_Container");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL033");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if(pEvent.getSource().getName().equalsIgnoreCase("FATCA_Save")){
			formObject.saveFragment("FATCA");


			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL034");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if(pEvent.getSource().getName().equalsIgnoreCase("KYC_Save")){
			RLOS.mLogger.info( "Inside KYC save button");
			formObject.saveFragment("KYC");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL035");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if(pEvent.getSource().getName().equalsIgnoreCase("OECD_Save")){
			formObject.saveFragment("OECD");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL036");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		else if(pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails_save")){
			formObject.saveFragment("ReferenceDetails");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL037");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		else if(pEvent.getSource().getName().equalsIgnoreCase("ServiceRequest_Save")){
			formObject.saveFragment("CC_Loan_container");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL038");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}

		else if(pEvent.getSource().getName().equalsIgnoreCase("BTC_save") ){
			formObject.saveFragment("CC_Loan_container");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL039");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		else if(pEvent.getSource().getName().equalsIgnoreCase("DDS_save")){
			formObject.saveFragment("CC_Loan_container");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL040");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		else if(pEvent.getSource().getName().equalsIgnoreCase("SI_save") ){
			formObject.saveFragment("CC_Loan_container");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL041");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		else if(pEvent.getSource().getName().equalsIgnoreCase("RVC_Save")){
			formObject.saveFragment("CC_Loan_container");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL042");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if(pEvent.getSource().getName().equalsIgnoreCase("IncomingDoc_Save")){
			RLOS.mLogger.info( "TANSHU Inside IncomingDoc_Save button!!");
			formObject.saveFragment("IncomingDocuments");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL043");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Save")){
			formObject.saveFragment("DecisionHistoryContainer");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL044");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}


		else if (pEvent.getSource().getName().equalsIgnoreCase("OECD_Button1")){					

			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_OECD_cmplx_GR_OecdDetails");
		}
		/*
		else if (pEvent.getSource().getName().equalsIgnoreCase("OECD_Button2")){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_OECD_cmplx_GR_OecdDetails");
		}


		else if (pEvent.getSource().getName().equalsIgnoreCase("OECD_Button3")){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_OECD_cmplx_GR_OecdDetails");
		}
		*/


		//added by yash for RLOS FSD
		else if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_save")){
			formObject.saveFragment("Notepad_Values");
			popupFlag = "Y";
			alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL045");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		// added by abhishek as per rlos FSD 
		
		
		else if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Add")){
			formObject.setNGValue("NotepadDetails_WiNote",formObject.getWFWorkitemName());
			RLOS.mLogger.info( "Inside add button: "+formObject.getNGValue("NotepadDetails_WiNote"));					
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_notegrid");
			Notepad_add();
		}
		else if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Modify")){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_NotepadDetails_cmplx_notegrid");
			Notepad_modify();
		}
		else if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Delete")){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_NotepadDetails_cmplx_notegrid");
			Notepad_delete();
		}

		// ended by abhishek as per rlos FSD
		else if(pEvent.getSource().getName().equalsIgnoreCase("cmplx_NotepadDetails_cmplx_notegrid")){
		 Notepad_grid();
			
		} 
		else{
			RLOS.mLogger.info( " No condition reached in mouse click event");
		}			
	}

	public void call_Value_Changed(ComponentEvent pEvent)
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		RLOS.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		String ReqProd;

		/*if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_Customer_DOb")){
			RLOS.mLogger.info( "Value of dob is:"+formObject.getNGValue("cmplx_Customer_DOb"));
			getAge(formObject.getNGValue("cmplx_Customer_DOb"),"cmplx_Customer_age");
		}*/
		
	
		//code added for LOB
		if (pEvent.getSource().getName().equalsIgnoreCase("TLIssueDate")){
			RLOS.mLogger.info( "Value of TLIssueDate is:"+formObject.getNGValue("TLIssueDate"));
			getAge(formObject.getNGValue("TLIssueDate"),"lob");
		}
		//code added for LOB
		
		if (pEvent.getSource().getName().equalsIgnoreCase("indusSector")){					
			LoadPickList("indusMAcro", "select '--Select--' union select convert(varchar, macro) from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where IndustrySector='"+formObject.getNGValue("indusSector")+"' and IsActive='Y'");
		}

		if (pEvent.getSource().getName().equalsIgnoreCase("indusMAcro")){
			LoadPickList("indusMicro", "select '--Select--' union select convert(varchar, micro) from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where Macro='"+formObject.getNGValue("indusMAcro")+"' and IsActive='Y'");
		}
		
		if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_EmploymentDetails_DOJ")){
			RLOS.mLogger.info( "Value of dob is:"+formObject.getNGValue("cmplx_EmploymentDetails_DOJ"));
			getAge(formObject.getNGValue("cmplx_EmploymentDetails_DOJ"),"cmplx_EmploymentDetails_LOS");
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_EligibilityAndProductInfo_FirstRepayDate")){
			RLOS.mLogger.info( "Value of dob is:"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_MaturityDate"));
			String AgeAtMAturity=getYearsDifference(formObject,"cmplx_Customer_DOb","cmplx_EligibilityAndProductInfo_MaturityDate");
			RLOS.mLogger.info( "Value of dob is:"+AgeAtMAturity);
			formObject.setNGValue("cmplx_EligibilityAndProductInfo_ageAtMaturity", AgeAtMAturity);
			
		}
		if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_Customer_Nationality"))
		{

			RLOS.mLogger.info( "iNSIDE cmplx_Customer_Nationality CASE-->akShay "+formObject.getNGValue("cmplx_Customer_Nationality"));

			String  GCC="BH,IQ,KW,OM,QA,SA,AE";

			if(GCC.contains(formObject.getNGValue("cmplx_Customer_Nationality")))
			{
				RLOS.mLogger.info("Inside GCC for Nationality");
				formObject.setNGValue("cmplx_Customer_GCCNational","Yes");
			}
			else
			{
				formObject.setNGValue("cmplx_Customer_GCCNational","No");
			}
		}
		if(pEvent.getSource().getName().equalsIgnoreCase("cmplx_EmploymentDetails_ApplicationCateg")){
			String reqProd = formObject.getNGValue("PrimaryProduct");
			String appCategory = formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg");
			String subproduct = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2);
			if(reqProd.equalsIgnoreCase("Personal Loan")){
				if(appCategory!=null && appCategory.equalsIgnoreCase("BAU")){
					LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and BAU='Y' and (product = 'PL' or product='B') order by code");
				}
				else if(appCategory!=null &&  appCategory.equalsIgnoreCase("Surrogate")){
					LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and Surrogate='Y' and (product = 'PL' or product='B') order by code");
				}
				else{
					LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and (product = 'PL' or product='B') order by code");	
				}
			}
			else if(reqProd.equalsIgnoreCase("Credit Card")){
				if(appCategory!=null &&  appCategory.equalsIgnoreCase("BAU")){
					LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and BAU='Y' and (product = 'CC' or product='B') order by code");
				}
				else if(appCategory!=null &&  appCategory.equalsIgnoreCase("Surrogate")){
					LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and Surrogate='Y' and (product = 'CC' or product='B') order by code");
				}
				else{
					LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and (product = 'CC' or product='B') order by code");	
				}
			}
		}

		if (pEvent.getSource().getName().equalsIgnoreCase("ReqProd")){
			ReqProd=formObject.getNGValue("ReqProd");
			RLOS.mLogger.info( "Value of ReqProd is:"+ReqProd);
			loadPicklistProduct(ReqProd);
			if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1).equalsIgnoreCase("Credit Card")){
				formObject.setVisible("AlternateContactDetails_retainAccount",false);
			}
			else{
				formObject.setVisible("AlternateContactDetails_retainAccount",true);
			}
		}


		if (pEvent.getSource().getName().equalsIgnoreCase("SubProd")){
			RLOS.mLogger.info( "Value of SubProd is:"+formObject.getNGValue("SubProd"));
			//formObject.clear("AppType");

			LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_ApplicationType with (nolock) where subProdFlag='"+formObject.getNGValue("SubProd")+"' order by code");
            //formObject.setNGValue("AppType","--Select--");

			String subprod=formObject.getNGValue("SubProd");
			String VIPFlag=formObject.getNGValue("cmplx_Customer_VIPFlag");
			String Nationality=formObject.getNGValue("cmplx_Customer_Nationality");
			String req_prod=formObject.getNGValue("ReqProd");
			String TypeOfProd=formObject.getNGValue("Product_type");
			RLOS.mLogger.info( "Value of SubProd is:"+subprod);
			RLOS.mLogger.info( "Value of vip is:"+VIPFlag);
			RLOS.mLogger.info( "Value of Nationality is:"+Nationality);
			RLOS.mLogger.info( "Value of req_prod is:"+req_prod);
			RLOS.mLogger.info( "Value of TypeOfProd is:"+TypeOfProd);

			if (req_prod.equalsIgnoreCase("Credit Card")){
				if(subprod.equalsIgnoreCase("PU")||subprod.equalsIgnoreCase("PULI")||subprod.equalsIgnoreCase("LI"))
				{
					formObject.clear("CardProd");
					formObject.setNGValue("CardProd","--Select--");
					RLOS.mLogger.info( " is not BTC ,PA :"+subprod);
					if(TypeOfProd.equalsIgnoreCase("Conventional")){
					LoadPickList("CardProd","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cardProduct with (nolock) where reqproduct like '%conventional%' order by code");
					}
					else{
					LoadPickList("CardProd","select '--Select--' as description,'' as code union select convert(varchar, Description) from ng_master_cardProduct with (nolock) where reqproduct like '%Islamic%' order by code");
						
					}
				}
				else{
					formObject.clear("CardProd");
					formObject.setNGValue("CardProd","--Select--");
					String query1="select '--Select--' as Description,'' as Code union select convert(varchar, Description),code from ng_master_cardProduct with (nolock) where Reqproduct='"+TypeOfProd+"' and SUBproduct = '"+subprod+"'  and VIPFlag='"+VIPFlag+"' and Nationality='AE' order by code";	
					String query2="select '--Select--' as Description,'' as Code union select convert(varchar, Description),code from ng_master_cardProduct with (nolock) where Reqproduct='"+TypeOfProd+"' and SUBproduct = '"+subprod+"'  and VIPFlag='"+VIPFlag+"' and Nationality!='AE' order by code";
					RLOS.mLogger.info( ": "+Nationality);
					RLOS.mLogger.info(query1);
					if(Nationality.equalsIgnoreCase("AE")){
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

		if (pEvent.getSource().getName().equalsIgnoreCase("AppType"))
		{
			String subprod=formObject.getNGValue("SubProd");
			String apptype=formObject.getNGValue("AppType");
			String TypeofProduct=formObject.getNGValue("Product_type");
			RLOS.mLogger.info( "Value of SubProd is:"+formObject.getNGValue("SubProd"));
			RLOS.mLogger.info( "Value of AppType is:"+formObject.getNGValue("AppType"));
			RLOS.mLogger.info( "Value of AppType is:"+formObject.getNGValue("Product_type"));


			if ((TypeofProduct.equalsIgnoreCase("Conventional"))&& subprod.equalsIgnoreCase("EXP")&&(apptype.equalsIgnoreCase("TKOE"))){


				RLOS.mLogger.info( "1Value of AppType is:"+formObject.getNGValue("AppType"));


				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Take Over'  and TypeofProduct='Conventional' order by SCHEMEID");




			}
			if ((TypeofProduct.equalsIgnoreCase("Conventional"))&& subprod.equalsIgnoreCase("EXP")&&(apptype.equalsIgnoreCase("NEWE"))){
				RLOS.mLogger.info( "2Value of AppType is:"+formObject.getNGValue("AppType"));




				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'New' and TypeofProduct='Conventional' order by SCHEMEID");




			}
			if ((TypeofProduct.equalsIgnoreCase("Conventional")&&subprod.equalsIgnoreCase("EXP"))&&(apptype.equalsIgnoreCase("TOPE"))){
				RLOS.mLogger.info( "3Value of AppType is:"+formObject.getNGValue("AppType"));


				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Top up' and TypeofProduct='Conventional' order by SCHEMEID");


			}
			if ((TypeofProduct.equalsIgnoreCase("Conventional")&&subprod.equalsIgnoreCase("EXP"))&&(apptype.equalsIgnoreCase("RESCE"))){
				RLOS.mLogger.info( "4Value of AppType is:"+formObject.getNGValue("AppType"));


				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Reschedulment' and TypeofProduct='Conventional' order by SCHEMEID");


			}
			if ((TypeofProduct.equalsIgnoreCase("Conventional")&& subprod.equalsIgnoreCase("NAT"))&&(apptype.equalsIgnoreCase("TOPN"))){
				RLOS.mLogger.info( "5Value of AppType is:"+formObject.getNGValue("AppType"));
				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Top up' and TypeofProduct='Conventional' order by SCHEMEID");


			}
			if ((TypeofProduct.equalsIgnoreCase("Conventional")&&subprod.equalsIgnoreCase("NAT"))&&(apptype.equalsIgnoreCase("TKON"))){
				RLOS.mLogger.info( "6Value of AppType is:"+formObject.getNGValue("AppType"));
				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Take Over' and TypeofProduct='Conventional' order by SCHEMEID");


			}
			if ((TypeofProduct.equalsIgnoreCase("Conventional")&&subprod.equalsIgnoreCase("NAT"))&&(apptype.equalsIgnoreCase("NEWN"))){
				RLOS.mLogger.info( "7Value of AppType is:"+formObject.getNGValue("AppType"));
				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'New' and TypeofProduct='Conventional' order by SCHEMEID");


			}
			if ((TypeofProduct.equalsIgnoreCase("Conventional")&&subprod.equalsIgnoreCase("NAT"))&&(apptype.equalsIgnoreCase("RESCN"))){
				RLOS.mLogger.info( "8Value of AppType is:"+formObject.getNGValue("AppType"));
				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Reschedulment' and TypeofProduct='Conventional' order by SCHEMEID");


			}
			if ((TypeofProduct.equalsIgnoreCase("Islamic"))&& subprod.equalsIgnoreCase("EXP")&&(apptype.equalsIgnoreCase("TKOE"))){


				RLOS.mLogger.info( "1Value of AppType is:"+formObject.getNGValue("AppType"));


				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Take Over'  and TypeofProduct='Islamic' order by SCHEMEID");				
			}

			if ((TypeofProduct.equalsIgnoreCase("Islamic"))&& subprod.equalsIgnoreCase("EXP")&&(apptype.equalsIgnoreCase("NEWE"))){
				RLOS.mLogger.info( "2Value of AppType is:"+formObject.getNGValue("AppType"));
				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'New' and TypeofProduct='Islamic' order by SCHEMEID");				
			}

			if ((TypeofProduct.equalsIgnoreCase("Islamic")&&subprod.equalsIgnoreCase("EXP"))&&(apptype.equalsIgnoreCase("TOPE"))){
				RLOS.mLogger.info( "3Value of AppType is:"+formObject.getNGValue("AppType"));


				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Top up' and TypeofProduct='Islamic' order by SCHEMEID");


			}
			if ((TypeofProduct.equalsIgnoreCase("Islamic")&&subprod.equalsIgnoreCase("EXP"))&&(apptype.equalsIgnoreCase("RESCE"))){
				RLOS.mLogger.info( "4Value of AppType is:"+formObject.getNGValue("AppType"));


				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Reschedulment' and TypeofProduct='Islamic' order by SCHEMEID");


			}
			if ((TypeofProduct.equalsIgnoreCase("Islamic")&& subprod.equalsIgnoreCase("NAT"))&&(apptype.equalsIgnoreCase("TOPN"))){
				RLOS.mLogger.info( "5Value of AppType is:"+formObject.getNGValue("AppType"));
				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Top up' and TypeofProduct='Islamic' order by SCHEMEID");


			}
			if ((TypeofProduct.equalsIgnoreCase("Islamic")&&subprod.equalsIgnoreCase("NAT"))&&(apptype.equalsIgnoreCase("TKON"))){
				RLOS.mLogger.info( "6Value of AppType is:"+formObject.getNGValue("AppType"));
				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Take Over' and TypeofProduct='Islamic' order by SCHEMEID");


			}
			if ((TypeofProduct.equalsIgnoreCase("Islamic")&&subprod.equalsIgnoreCase("NAT"))&&(apptype.equalsIgnoreCase("NEWN"))){
				RLOS.mLogger.info( "7Value of AppType is:"+formObject.getNGValue("AppType"));
				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'New' and TypeofProduct='Islamic' order by SCHEMEID");


			}
			if ((TypeofProduct.equalsIgnoreCase("Islamic")&&subprod.equalsIgnoreCase("NAT"))&&(apptype.equalsIgnoreCase("RESCN"))){
				RLOS.mLogger.info( "8Value of AppType is:"+formObject.getNGValue("AppType"));
				formObject.clear("Scheme");
				formObject.setNGValue("Scheme","--Select--");
				LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Reschedulment' and TypeofProduct='Islamic' order by SCHEMEID");

			}
		}


		if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_EmploymentDetails_EmpIndusSector")){

			LoadPickList("cmplx_EmploymentDetails_Indus_Macro", "select '--Select--' union select  macro from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where IndustrySector='"+formObject.getNGValue("cmplx_EmploymentDetails_EmpIndusSector")+"' and IsActive='Y'");
		}

		if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_EmploymentDetails_Indus_Macro")){
			LoadPickList("cmplx_EmploymentDetails_Indus_Micro", "select '--Select--' union select  micro from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where Macro='"+formObject.getNGValue("cmplx_EmploymentDetails_Indus_Macro")+"' and IsActive='Y'");
		}


		if (pEvent.getSource().getName().equalsIgnoreCase("AlternateContactDetails_carddispatch")){
			String cardDisp=formObject.getNGValue("AlternateContactDetails_carddispatch");
			RLOS.mLogger.info(cardDisp);

			if (cardDisp.equalsIgnoreCase("Branch")){
				formObject.setVisible("AlternateContactDetails_custdomicile",true);
			}

		}
		if (pEvent.getSource().getName().equalsIgnoreCase("Product_type")){

			String ProdType=formObject.getNGValue("Product_type");
			RLOS.mLogger.info(ProdType);
			formObject.clear("CardProd");
			formObject.setNGValue("CardProd","--Select--");
			if(ProdType.equalsIgnoreCase("Conventional"))
				LoadPickList("CardProd", "select '--Select--','' as code union select convert(varchar,description),code from ng_MASTER_CardProduct with (nolock) where code NOT like '%AMAL%' order by code");


			if(ProdType.equalsIgnoreCase("Islamic"))
				LoadPickList("CardProd", "select '--Select--','' as code union select convert(varchar,description),code from ng_MASTER_CardProduct with (nolock) where code like '%AMAL%' order by code");
		}



		//added by yash on 30/8/2017
		if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_notedesc")){
			String notepad_desc = formObject.getNGValue("NotepadDetails_notedesc");

			String sQuery = "select code,workstep from ng_master_notedescription where description='" +  notepad_desc + "'";


			List<List<String>> recordList = formObject.getDataFromDataSource(sQuery);
			if(recordList.get(0).get(0)!= null && recordList.get(0)!=null && !recordList.get(0).get(0).equalsIgnoreCase("") && recordList!=null)
			{
				formObject.setNGValue("NotepadDetails_notecode",recordList.get(0).get(0));
				formObject.setNGValue("NotepadDetails_WorkstepName",recordList.get(0).get(1));		        

			}

		}
	}

	
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
			formObject.setLocked("AuthorisedSignDetails_Frame1", true);
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
			formObject.setLocked("FATCA_Frame1",true);
		}
		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.setLocked("KYC_Frame1",true);
		}
		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.setLocked("OECD_Frame1",true);
		}
		else if ("IncomingDoc".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setLocked("IncomingDoc_Frame1",true);
			fetchIncomingDocRepeater();
		}		
		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.setLocked("DecisionHistory_Frame1", true);
			formObject.setLocked("cmplx_DecisionHistory_Decision",false);
			formObject.setLocked("cmplx_DecisionHistory_Remarks",false);
			formObject.setLocked("DecisionHistory_Save",false);
			formObject.setVisible("DecisionHistory_SendSMS",false);
			loadPicklist3();
		}		
	}
	
	public void CustomerFragment_Load()
	{		
		formObject.fetchFragment("CustomerDetails", "Customer", "q_Customer");
		formObject.setNGFrameState("CustomerDetails", 0);

		String cardnotavailable=formObject.getNGValue("cmplx_Customer_CardNotAvailable");
		String MobNo=formObject.getNGValue("cmplx_Customer_MobNo");
		RLOS.mLogger.info("MobNo"+MobNo);
		String OTPMobNo=formObject.getNGValue("OTP_Mobile_NO");
		RLOS.mLogger.info("OTPMobNo"+OTPMobNo);
		if ("".equalsIgnoreCase(OTPMobNo)){
			formObject.setNGValue("OTP_Mobile_NO", MobNo);
		}
				
		String userName = formObject.getUserName();
		if(formObject.getNGValue("processby_email")==null)
		{
			setMailId(userName);
		}
		/*if (cardnotavailable.equalsIgnoreCase("true")){
			RLOS.mLogger.info("RLOS", "CardNotavail"+cardnotavailable);
			formObject.setLocked("ReadFromCard", true);
		}*/
		if	("Y".equalsIgnoreCase(formObject.getNGValue("Is_Customer_Req")))
		{
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
								
		String nep=formObject.getNGValue("cmplx_Customer_NEP");
		String cifno=formObject.getNGValue("cmplx_Customer_card_id_available");
		if (("true".equalsIgnoreCase(cardnotavailable) || "true".equalsIgnoreCase(nep)) && "true".equalsIgnoreCase(cifno))
		{
			formObject.setLocked("Customer_Button1", false);
		}
		else {
			formObject.setLocked("Customer_Button1", true);
		}	
	
	}
	
	public static void visibilityFrameIncomeDetails(FormReference formObject) {

		if(formObject.getNGValue("PrimaryProduct").equalsIgnoreCase("Credit Card") /*&& formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")*/){
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
		String EmpType=formObject.getNGValue("empType");
		RLOS.mLogger.info( "Emp Type Value is:"+EmpType);

		if(EmpType.equalsIgnoreCase("Salaried")|| EmpType.equalsIgnoreCase("Salaried Pensioner"))
		{
			formObject.setVisible("IncomeDetails_Frame3", false);
			formObject.setHeight("Incomedetails", 730);
			formObject.setHeight("IncomeDetails_Frame1", 680);	
			if(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true") || formObject.getNGValue("cmplx_Customer_NEP").equals("true")){
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
			formObject.setHeight("Incomedetails", 430);
			formObject.setHeight("IncomeDetails_Frame1", 400);
			if(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true") || formObject.getNGValue("cmplx_Customer_NEP").equalsIgnoreCase("true")){
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

	public static String makeInputForGrid(String customerName) {
		
		String temp = "<ListItem><SubItem>"+customerName+"</SubItem>"
				+"<SubItem>Authorised signatory</SubItem>"
				+"<SubItem>Primary</SubItem>";

		for(int i=0;i<26;i++){
			temp+= "<SubItem></SubItem>";
			//company_data.add("");
		}

		temp+="</ListItem>";
		//formObject.addItemFromList("cmplx_CompanyDetails_cmplx_CompanyGrid",company_data);

		return temp;
	}

}



