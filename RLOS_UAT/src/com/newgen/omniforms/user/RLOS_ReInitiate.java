package com.newgen.omniforms.user;

//import com.newgen.omni.wf.util.excp.NGException;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.IRepeater;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.excp.CustomExceptionHandler;
import com.newgen.omniforms.listener.FormListener;
import com.newgen.omniforms.skutil.AesUtil;
import com.newgen.omniforms.skutil.SKLogger;


import javax.faces.application.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.faces.validator.ValidatorException;
@SuppressWarnings("serial")


public class RLOS_ReInitiate extends RLOSCommon implements FormListener
{

	HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
	boolean isSearchEmployer=false;
	String ReqProd=null;
	//System.out.println("Inside initiation RLOS");
	public void formLoaded(FormEvent pEvent)
	{
		System.out.println("Inside initiation RLOS");
		SKLogger.writeLog("RLOS Initiation", "Inside formLoaded()" + pEvent.getSource().getName());
	}

	public void formPopulated(FormEvent pEvent) {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try{
			formObject.fetchFragment("ProductDetailsLoader", "Product", "q_Product");
			formObject.setNGFrameState("ProductDetailsLoader", 0);

			SKLogger.writeLog("RLOS Initiation", "Inside formPopulated()" + pEvent.getSource());
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			String currDate = format.format(Calendar.getInstance().getTime());
			SKLogger.writeLog("RLOS Initiation", "currTime:" + currDate);
			formObject.setNGValue("Intro_Date",formObject.getNGValue("CreatedDate"));
			formObject.setNGValue("WIname",formObject.getWFWorkitemName());
			formObject.setNGValue("Channel_Name","Re Initiation");
			formObject.setNGValue("Created_By",formObject.getUserName());
			
			formObject.setNGValue("lbl_init_channel_val","Re_Initiate");
			
			formObject.setNGValue("lbl_curr_date_val",currDate);
			formObject.setNGValue("ApplicationRefNo", formObject.getWFFolderId());
			formObject.setNGValue("lbl_user_name_val",formObject.getUserName());
			formObject.setNGValue("lbl_prod_val",formObject.getNGValue("PrimaryProduct"));
			formObject.setNGValue("lbl_scheme_val",formObject.getNGValue("Subproduct_productGrid"));
			formObject.setNGValue("lbl_card_prod_val",formObject.getNGValue("CardProduct_Primary"));
			formObject.setNGValue("lbl_Loan_amount_bal",formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,3));
						
			
			String init_Channel=formObject.getNGValue("initiationChannel");
			if(init_Channel.equals(""))
				formObject.setNGValue("initiationChannel","Branch_Init");
			if(formObject.getNGValue("empType").contains("Salaried")){
				formObject.setNGValue("lbl_Comp_Name_val",formObject.getNGValue("Employer_Name"));
				formObject.setSheetVisible("ParentTab",1, false);
			}
			
			else if(formObject.getNGValue("empType").contains("Self Employed")){
				formObject.setNGValue("lbl_Comp_Name_val",formObject.getNGValue("Company_Name"));
				formObject.setSheetVisible("ParentTab",3, false);
			}
			//added By Akshay-23/7/2017
			new RLOSCommonCode().CustomerFragment_Load();
			
			//ended By Akshay-23/7/2017

			if(formObject.getNGValue("Product_Type").contains("Personal Loan") && Integer.parseInt(formObject.getNGValue("Age"))<18)
			{
				formObject.setVisible("GuarantorDetails", true);
				formObject.setTop("Incomedetails",110);
			}
			if(formObject.getNGValue("PrimaryProduct").equals("Credit Card"))
				formObject.setSheetVisible("ParentTab",7, true);

			SKLogger.writeLog("RLOS","Value Of Init Channel:"+init_Channel);
		
			if(formObject.getNGValue("cmplx_Customer_MiddleName").equals(""))
			{
				formObject.setNGValue("Cust_Name",formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme"));
			}

			else{
				formObject.setNGValue("Cust_Name",formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_MiddleName")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme"));
			}
		}catch(Exception e)

		{
			SKLogger.writeLog("RLOS Initiation", "Exception:"+e.getMessage());

		}
	}

	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String outputResponse = "";
		String	ReturnCode="";
		String	ReturnDesc="";
		String Gender="";
		String popupFlag="N";
		String popUpMsg="";
		String popUpControl="";
		String buttonClickFlag="";
		String alert_msg="";
		String OTPStatus="";
		String SystemErrorCode="";

		String BlacklistFlag="";
		String DuplicationFlag="";


		SKLogger.writeLog("RLOS Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

		try
		{
			switch (pEvent.getType()) 
			{		
			case FRAME_EXPANDED:
				SKLogger.writeLog("RLOS FRAG LOADED eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());


				if (pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails")) 
				{
					formObject.fetchFragment("GuarantorDetails", "GuarantorDetails", "q_GuarantorDetails");
				}


				else if (pEvent.getSource().getName().equalsIgnoreCase("ProductDetailsLoader")) {

					SKLogger.writeLog("RLOS", "Inside ProductDetailsLoader ");	
					//added Tanshu Aggarwal(23/06/2017)
					formObject.fetchFragment("ProductDetailsLoader", "Product", "q_Product");
					if	(formObject.getNGValue("Is_Customer_Req").equalsIgnoreCase("Y") && formObject.getNGValue("Is_Account_Create").equalsIgnoreCase("Y"))
					{
						formObject.setEnabled("ProductDetailsLoader", false);
						SKLogger.writeLog("RLOS", "Inside ProductDetailsLoader if ");	
					}

				}


				else if (pEvent.getSource().getName().equalsIgnoreCase("EmploymentDetails")) 
				{				

					SKLogger.writeLog("RLOS", "Inside EmploymentDetails before fragment load ");	

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
					SKLogger.writeLog("RLOS", "Inside EmploymentDetails after fragment load ");	


					//IMFields_Employment();
					//BTCFields_Employment();
					//LimitIncreaseFields_Employment();
					//ProductUpgrade_Employment();



				}		 

				else if (pEvent.getSource().getName().equalsIgnoreCase("Incomedetails")) 
				{

					formObject.fetchFragment("Incomedetails", "IncomeDetails", "q_IncomeDetails");

					//int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
					//for(int i=0;i<n;i++){
					//SKLogger.writeLog("RLOS", "Grid Data[1][9] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9));
					visibilityFrameIncomeDetails(formObject);
					//}

					//IMFields_Income();
					//BTCFields_Income();
					//LimitIncreaseFields_Income();
					//ProductUpgrade_Income();
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




					if(formObject.getNGValue("Subproduct_productGrid").equalsIgnoreCase("BTC")){
						formObject.setVisible("CompanyDetails_Label8", true);
						formObject.setVisible("CompanyDetails_effecLOB", true);	

					}

					else if(formObject.getNGValue("Subproduct_productGrid").equalsIgnoreCase("Se")/* && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")*/){
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



					SKLogger.writeLog("RLOS", "CompanyDetailse1:");

				}




				else	if (pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignatoryDetails")) 
				{

					formObject.fetchFragment("AuthorisedSignatoryDetails", "AuthorisedSignDetails", "q_AuthorisedSignDetails");
					SKLogger.writeLog("RLOS", "AuthorisedSignatoryDetails:");




				}
				else if (pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails")) 
				{
					SKLogger.writeLog("RLOS", "Saurabh123456");
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
					if(framestate0 == 0){
						SKLogger.writeLog("RLOS count of current account not NTB","ProductDetailsLoader");
					}
					else {
						formObject.fetchFragment("ProductDetailsLoader", "Product", "q_Product");
						SKLogger.writeLog("RLOS count of current account not NTB","fetched product details");

					}


					int framestate1=formObject.getNGFrameState("Incomedetails");
					if(framestate1 == 0){
						SKLogger.writeLog("RLOS count of current account not NTB","Incomedetails");
					}
					else {
						formObject.fetchFragment("Incomedetails", "IncomeDetails", "q_IncomeDetails");
						visibilityFrameIncomeDetails(formObject);
						SKLogger.writeLog("RLOS count of current account not NTB","fetched income details");
						formObject.setTop("Incomedetails",formObject.getTop("ProductDetailsLoader")+formObject.getHeight("ProductDetailsLoader"));
					}

					int framestate2=formObject.getNGFrameState("EmploymentDetails");
					if(framestate2 == 0){
						SKLogger.writeLog("RLOS count of current account not NTB","EmploymentDetails");
					}
					else {
						formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmploymentDetails");
						SKLogger.writeLog("RLOS count of current account not NTB","fetched employment details");
					}
					int framestate3=formObject.getNGFrameState("Address_Details_container");
					if(framestate3 == 0){
						SKLogger.writeLog("RLOS count of current account not NTB","Address_Details_container");
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

						SKLogger.writeLog("RLOS count of current account not NTB","fetched address details");
					}

					int framestate4=formObject.getNGFrameState("ReferenceDetails");
					if(framestate4 == 0){
						SKLogger.writeLog("RLOS count of current account not NTB","Alt_Contact_container");
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

						SKLogger.writeLog("RLOS count of current account not NTB","fetched address details");
					}


					int framestate5=formObject.getNGFrameState("Alt_Contact_container");
					if(framestate5 == 0){
						SKLogger.writeLog("RLOS count of current account not NTB","Alt_Contact_container");
					}
					else {
						formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
						//formObject.setTop("ReferenceDetails",formObject.getTop("Address_Details_Container")+formObject.getHeight("Address_Details_Container"));
						formObject.setTop("Alt_Contact_container",formObject.getTop("ReferenceDetails")+formObject.getHeight("ReferenceDetails")+70);
						formObject.setTop("Supplementary_Container",formObject.getTop("ReferenceDetails")+250);
						formObject.setTop("CardDetails", formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container")+20);
						formObject.setTop("FATCA", formObject.getTop("CardDetails")+30);
						formObject.setTop("KYC", formObject.getTop("FATCA")+20);
						formObject.setTop("OECD", formObject.getTop("KYC")+20);

						SKLogger.writeLog("RLOS count of current account not NTB","fetched address details");
					}

					int framestate6=formObject.getNGFrameState("KYC");
					if(framestate6 == 0){
						SKLogger.writeLog("RLOS count of current account not NTB","Alt_Contact_container");
					}
					else {
						formObject.fetchFragment("KYC", "KYC", "q_KYC");
						formObject.setTop("KYC", formObject.getTop("FATCA")+30);
						formObject.setTop("OECD", formObject.getTop("KYC")+20);
						SKLogger.writeLog("RLOS count of current account not NTB","fetched address details");
					}

					int framestate7=formObject.getNGFrameState("OECD");
					if(framestate7 == 0){
						SKLogger.writeLog("RLOS count of current account not NTB","Alt_Contact_container");
					}
					else {
						formObject.fetchFragment("OECD", "OECD", "q_OECD");
						formObject.setTop("OECD", formObject.getTop("KYC")+formObject.getHeight("KYC")+20);
						SKLogger.writeLog("RLOS count of current account not NTB","fetched address details");
					}




					//25/07/2017  for mandatory checks fragments should be fetched first

					//added Tanshu Aggarwal(23/06/2017)
					String wi_name= formObject.getWFWorkitemName();
					String countCurrentAccount=formObject.getDataFromDataSource("SELECT count(acctId) from ng_RLOS_CUSTEXPOSE_AcctDetails where AcctType='CURRENT ACCOUNT' AND Wi_Name='"+formObject.getWFWorkitemName()+"' ").get(0).get(0);
					SKLogger.writeLog("RLOS count of current account not NTB",countCurrentAccount);
					formObject.setNGValue("ExistingCustomerAccount",countCurrentAccount);

					String NTB=formObject.getNGValue("NTB");
					String NEP=formObject.getNGValue("NEP");
					SKLogger.writeLog("RLOS count of current account not NTB and NEP values",NTB+","+NEP);
					//added Tanshu Aggarwal(23/06/2017)
					//if(Product.toUpperCase().contains("PERSONAL LOAN") && formObject.getNGValue("ExistingCustomerAccount").equals("0") && (formObject.getNGValue("NTB").equals("true") || formObject.getNGValue("NEP").equals("true")))
					if(formObject.getNGValue("ExistingCustomerAccount").equals("0"))
					{
						SKLogger.writeLog("RLOS count of current account not NTB","inside visibility true");
						formObject.setVisible("DecisionHistory_Button3", true);//Create CIF/Acc
					}
					else{
						SKLogger.writeLog("RLOS count of current account not NTB","inside visibility false");
						formObject.setVisible("DecisionHistory_Button3", false);//Create CIFAcc
					}
					//added Tanshu Aggarwal(22/06/2017)
					if	(formObject.getNGValue("Is_Customer_Req").equalsIgnoreCase("Y") && formObject.getNGValue("Is_Account_Create").equalsIgnoreCase("Y"))
					{
						SKLogger.writeLog("RLOS_initiation Decision load", "inside customer req Y");
						formObject.setLocked("DecisionHistory_Button3", true);
					}
					else{
						SKLogger.writeLog("RLOS_initiation Decision load", "inside customer req N");
						formObject.setLocked("DecisionHistory_Button3", false);
					}
					//added Tanshu Aggarwal(22/06/2017)		
				}


				else 	if (pEvent.getSource().getName().equalsIgnoreCase("MiscFields")) 
				{

					formObject.fetchFragment("MiscFields", "MiscellaneousFields", "q_MiscFields");	


				}	


				else if (pEvent.getSource().getName().equalsIgnoreCase("EligibilityAndProductInformation"))
				{

					formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligAndProductInfo");
					formObject.setNGFrameState("EligibilityAndProductInformation", 0);
					SKLogger.writeLog("RLOS value of Reason","Saurabh ELPINFO,Framexpanded");
					Fields_Eligibility();
					Fields_ApplicationType_Eligibility();
					formObject.setEnabled("cmplx_EligibilityAndProductInfo_FinalInterestRate", false);

					SKLogger.writeLog("RLOS value of Reason","Eligibility grid");

					if(formObject.getNGValue("PrimaryProduct").equalsIgnoreCase("Personal Loan")/* && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")*/){
						formObject.setVisible("ELigibiltyAndProductInfo_Frame2", true);//Funding Account Number Grid
						formObject.setVisible("ELigibiltyAndProductInfo_Frame4", true);//Personal Loan
						formObject.setVisible("ELigibiltyAndProductInfo_Frame6",true);//ELigible for Card
						formObject.setVisible("ELigibiltyAndProductInfo_Frame3", false);//Lein Details
						formObject.setVisible("ELigibiltyAndProductInfo_Frame5",false);//CC
						SKLogger.writeLog("RLOS", "Funding Account Details now Visible...!!!");


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
							SKLogger.writeLog("RLOS", "Lein Details now Visible...!!!");
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


				}



				/*else if (pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails")) {       	


					formObject.fetchFragment("ReferenceDetails", "ReferenceDetails", "q_ReferenceDetails");

				}*/


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






				else if (pEvent.getSource().getName().equalsIgnoreCase("CustomerDocumentsContainer"))
				{

					formObject.fetchFragment("CustomerDocumentsContainer", "CustomerDocument", "q_CustomerDocument");

				}	


				else if (pEvent.getSource().getName().equalsIgnoreCase("OutgoingDocumentsContainer"))
				{


					formObject.fetchFragment("OutgoingDocumentsContainer", "CorrespondenceDocument", "q_correspondenceDocument");
				}	




				else if (pEvent.getSource().getName().equalsIgnoreCase("DecisioningFields"))
				{

					formObject.fetchFragment("DecisioningFields", "DecisionFields", "q_DecisionFields");


				}	



				else if (pEvent.getSource().getName().equalsIgnoreCase("DeviationHistoryContainer"))
				{

					formObject.fetchFragment("DeviationHistoryContainer", "DeviationHistory", "q_DeviationHistory");


				}	


				else  if (pEvent.getSource().getName().equalsIgnoreCase("IncomingDocuments"))

				{

					int framestate0 = formObject.getNGFrameState("ProductDetailsLoader");
					if(framestate0 == 0){
						SKLogger.writeLog("RLOS count of current account not NTB","ProductDetailsLoader");
					}
					else {
						formObject.fetchFragment("ProductDetailsLoader", "Product", "q_Product");
						SKLogger.writeLog("RLOS count of current account not NTB","fetched product details");
						formObject.setTop("Incomedetails",formObject.getTop("ProductDetailsLoader")+formObject.getHeight("ProductDetailsLoader"));
					}


					int framestate1=formObject.getNGFrameState("Incomedetails");
					if(framestate1 == 0){
						SKLogger.writeLog("RLOS count of current account not NTB","Incomedetails");
					}
					else {
						formObject.fetchFragment("Incomedetails", "IncomeDetails", "q_IncomeDetails");
						SKLogger.writeLog("RLOS count of current account not NTB","fetched income details");
						formObject.setTop("Incomedetails",formObject.getTop("ProductDetailsLoader")+formObject.getHeight("ProductDetailsLoader"));
						visibilityFrameIncomeDetails(formObject);
					}



					SKLogger.writeLog("RLOS Initiation Inside ","IncomingDocuments");
					formObject.fetchFragment("IncomingDocuments", "IncomingDoc", "q_IncomingDoc");
					SKLogger.writeLog("RLOS Initiation Inside ","fetchIncomingDocRepeater");
					fetchIncomingDocRepeater();
					SKLogger.writeLog("RLOS Initiation eventDispatched()","formObject.fetchFragment1");




				}

				if (pEvent.getSource().getName().equalsIgnoreCase("DeferralDocuments")) 

				{

					SKLogger.writeLog("RLOS Initiation eventDispatched()","DeferralDocuments");
					formObject.fetchFragment("DeferralDocuments", "DeferralDocName", "q_DeferralDoc");
					loadAllCombo_RLOS_Documents_Deferral();
					SKLogger.writeLog("RLOS Initiation eventDispatched()","formObject.fetchFragment2");

				}
				if (pEvent.getSource().getName().equalsIgnoreCase("Notepad_details")) {
					hm.put("Notepad_details","Clicked");
					popupFlag="N";

					formObject.fetchFragment("Notepad_details", "NotepadDetails", "q_Note");
					try
					{
						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
					}
					finally{hm.clear();}
				}

				break;


			case FRAME_COLLAPSED: {
				break;
			}


			case MOUSE_CLICKED:

				//SKLogger.writeLog("RLOS", "Inside mouse clicked saurabh debugging");
				/*if(pEvent.getSource().getName().equalsIgnoreCase("existingOldCustomer"))

				{
					if(formObject.getNGValue("existingOldCustomer").equalsIgnoreCase("true"))
					{
						SKLogger.writeLog("RLOS", "On click existing old customer !!");
						formObject.setNGValue("NewApplicationNo", formObject.getWFFolderId());
					}

				}

				 commented by AKshay on 16/9/17 as it is not required*/
				
				 if(pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails_Reference_Add"))

				{
					formObject.setNGValue("ReferenceDetails_reference_wi_name",formObject.getWFWorkitemName());
					SKLogger.writeLog("RLOS", "Inside add button: "+formObject.getNGValue("reference_wi_name"));
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
					SKLogger.writeLog("RLOS", "Inside add button: "+formObject.getNGValue("guarantor_WIName"));
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

					SKLogger.writeLog("RLOS", "Inside add button: aman123"+formObject.getNGValue("ReqProd"));


					formObject.setNGValue("Grid_wi_name",formObject.getWFWorkitemName());
					SKLogger.writeLog("RLOS", "Inside add button: "+formObject.getNGValue("Grid_wi_name"));
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Product_cmplx_ProductGrid");


					//tanshu aggarwal for documents(1/06/2017)
					IRepeater repObj=null;
					List<List<String>> deleteQuery = null;
					repObj = formObject.getRepeaterControl("IncomingDoc_Frame");
					SKLogger.writeLog("RLOS Inside add button: after creating repeater object00","before clear");
					boolean Visibility=formObject.isVisible("IncomingDoc_Frame");
					SKLogger.writeLog("RLOS Inside add button: after creating repeater object00","before clear"+Visibility);
					if(Visibility == true){
						repObj.clear();
						repObj.refresh();
						//formObject.setNGFrameState("IncomingDocuments", 1);
						fetchIncomingDocRepeater();
						SKLogger.writeLog("RLOS Inside add button: after creating repeater object00","after doc fun");


					}

					else {
						String query = "Delete FROM ng_rlos_IncomingDoc WHERE  wi_name='"+formObject.getWFWorkitemName()+"'";
						SKLogger.writeLog("RLOS incoming document", "when row count is  zero inside else"+query);
						deleteQuery = formObject.getNGDataFromDataCache(query);


						SKLogger.writeLog("RLOS incoming document", "when row count is  zero inside else after deleting the values");

					}
					SKLogger.writeLog("RLOS Inside add button: after creating repeater object00","after refresh");
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
					Fields_ApplicationType_Employment();
				}	




				else if (pEvent.getSource().getName().equalsIgnoreCase("Modify")){

					SKLogger.writeLog("RLOS", "Inside add button: aman123"+formObject.getNGValue("ReqProd"));

					if(formObject.getNGValue("ReqProd").equalsIgnoreCase("Credit Card")){
						formObject.setNGValue("Scheme","");
					}
					else if(formObject.getNGValue("ReqProd").equalsIgnoreCase("Personal Loan")){
						formObject.setNGValue("CardProd","");
					}

					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Product_cmplx_ProductGrid");



					//tanshu aggarwal for documents(1/06/2017)
					SKLogger.writeLog("RLOS Inside add button: ","after creating repeater object00");
					IRepeater repObj=null;
					repObj = formObject.getRepeaterControl("IncomingDoc_Frame");
					boolean Visibility=formObject.isVisible("IncomingDoc_Frame");
					SKLogger.writeLog("RLOS Inside add button: after creating repeater object00","before clear"+Visibility);
					if(Visibility == true){
						repObj.clear();
						repObj.refresh();
						fetchIncomingDocRepeater();
						SKLogger.writeLog("RLOS Inside add button: after creating repeater object00","after doc fun");
						//formObject.setNGFrameState("IncomingDocuments", 1);


					}

					else {
						String query = "Delete FROM ng_rlos_IncomingDoc WHERE  wi_name='"+formObject.getWFWorkitemName()+"'";
						SKLogger.writeLog("RLOS incoming document", "when row count is  zero inside else"+query);
						formObject.getNGDataFromDataCache(query);
						SKLogger.writeLog("RLOS incoming document", "when row count is  zero inside else after deleting the values123");


					}

					SKLogger.writeLog("RLOS Inside add button: ","after creating repeater object00");
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
					Fields_ApplicationType_Employment();

					if(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")!=null){
						String reqProd = formObject.getNGValue("PrimaryProduct");
						SKLogger.writeLog("RLOS Inside targetsegcode: ","requested Product is: "+reqProd);
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
					SKLogger.writeLog("RLOS Inside add button: ","before creating repeater object22");


					SKLogger.writeLog("RLOS Inside add button: ","before creating repeater object22 after saving the fragment12");
					IRepeater repObj=null;
					repObj = formObject.getRepeaterControl("IncomingDoc_Frame");
					boolean Visibility=formObject.isVisible("IncomingDoc_Frame");
					SKLogger.writeLog("RLOS Inside add button: after creating repeater object00","before clear"+Visibility);
					if(Visibility == true){
						repObj.clear();
						repObj.refresh();
						formObject.setNGFrameState("IncomingDocuments", 1);
						SKLogger.writeLog("RLOS Inside add button: after creating repeater object00","after doc fun");
						SKLogger.writeLog("RLOS Inside add button: after creating repeater object00","before clear setframestate");
					}


					else {
						String query = "Delete FROM ng_rlos_IncomingDoc WHERE  wi_name='"+formObject.getWFWorkitemName()+"'";
						SKLogger.writeLog("RLOS incoming document", "when row count is  zero inside else"+query);
						formObject.getNGDataFromDataCache(query);
						SKLogger.writeLog("RLOS incoming document", "when row count is  zero inside else after deleting the values456");
					}
					SKLogger.writeLog("RLOS Inside add button: ","after creating repeater object22");
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
					formObject.setNGValue("CompanyDetails_company_winame",formObject.getWFWorkitemName());
					SKLogger.writeLog("RLOS", "Inside add button: "+formObject.getNGValue("company_winame"));
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
					SKLogger.writeLog("RLOS", "Inside add button: "+formObject.getNGValue("partner_winame"));
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
					SKLogger.writeLog("RLOS", "Inside add button: "+formObject.getNGValue("AuthorisedSign_wiName"));
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
					SKLogger.writeLog("RLOS", "Inside add button: "+formObject.getNGValue("LiabilityAddition_wiName"));
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
				}


				else if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New_modify")){
					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
				}


				else if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New_delete")){
					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
				}	


				else if (pEvent.getSource().getName().equalsIgnoreCase("addr_Add")  ){
					SKLogger.writeLog("RLOS", "Inside addredd grid add button");
					//	boolean flag_addressType= Address_Validate();
					//if(flag_addressType){
					formObject.setNGValue("address_Wi_name",formObject.getWFWorkitemName());
					SKLogger.writeLog("RLOS", "Inside address button: "+formObject.getNGValue("address_Wi_name"));
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
					SKLogger.writeLog("RLOS", "Inside add button: "+formObject.getNGValue("supplement_WiName"));
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
					SKLogger.writeLog("RLOS", "Inside add button: "+formObject.getNGValue("OECD_winame"));
					//below code by saurabh on 22md july 17.
					formObject.setEnabled("OECD_noTinReason",true);
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_OECD_cmplx_GR_OecdDetails");
				}


				else if (pEvent.getSource().getName().equalsIgnoreCase("OECD_modify")){
					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_OECD_cmplx_GR_OecdDetails");
				}


				else if (pEvent.getSource().getName().equalsIgnoreCase("OECD_delete")){
					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_OECD_cmplx_GR_OecdDetails");
				}	

				//added by akshay on 15/9/17 to add data in BT Grid
				if (pEvent.getSource().getName().equalsIgnoreCase("BT_Add")){
					formObject.setNGValue("CC_Loan_wi_name",formObject.getWFWorkitemName());
					SKLogger.writeLog("CC", "Inside add button: "+formObject.getNGValue("CC_Loan_wi_name"));
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_CC_Loan_cmplx_btc");
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("BT_Modify")){
					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_CC_Loan_cmplx_btc");
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("BT_Delete")){
					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_CC_Loan_cmplx_btc");
				}

				//ended by akshay on 15/9/17 to add data in BT Grid


				else if (pEvent.getSource().getName().equalsIgnoreCase("ExternalLiabilities_Button1"))
				{
					hm.put("ExternalLiabilities_Button1","Clicked");
					popupFlag="N";

				}

				if(pEvent.getSource().getName().equalsIgnoreCase("ReadFromCard")){
					popupFlag = "Y";
					outputResponse = GenerateXML("EID_Genuine","");
					ReturnCode =  (outputResponse.contains("<ns3:ServiceStatusCode>")) ? outputResponse.substring(outputResponse.indexOf("<ns3:ServiceStatusCode>")+"</ns3:ServiceStatusCode>".length()-1,outputResponse.indexOf("</ns3:ServiceStatusCode>")):"";
					String Returndesc = (outputResponse.contains("<ns3:ServiceStatusDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ns3:ServiceStatusDesc>")+"</ns3:ServiceStatusDesc>".length()-1,outputResponse.indexOf("</ns3:ServiceStatusDesc>")):"";
					//  ReturnCode="123";
					SKLogger.writeLog("RLOS value of EIDA ReturnCode new: ",ReturnCode);
					SKLogger.writeLog("RLOS value of EIDA Returndesc new: ",Returndesc);
					if(ReturnCode.equalsIgnoreCase("s") && Returndesc.equalsIgnoreCase("Valid")){
						valueSetCustomer(outputResponse, "");  
						formObject.setNGValue("Is_EID_Genuine", "Y");
						formObject.setNGValue("cmplx_Customer_IsGenuine", true);
						SKLogger.writeLog("RLOS value of EID_Genuine","EID is generated");
						alert_msg="EID Genuine is sucessfull";
						throw new ValidatorException(new FacesMessage("EID Genuine is sucessfull"));
					}   
					else{
						alert_msg="EID Genuine failed";
						formObject.setNGValue("Is_EID_Genuine", "N");
						SKLogger.writeLog("EID_Genuine","EID is not generated");
					}

					SKLogger.writeLog("RLOS value of Entity Details",formObject.getNGValue("Is_EID_Genuine"));
					try
					{
						throw new ValidatorException(new FacesMessage(alert_msg));
					}
					finally{hm.clear();}

				}    


				else if(pEvent.getSource().getName().equalsIgnoreCase("FetchDetails"))
				{  
					formObject = FormContext.getCurrentInstance().getFormReference();

					formObject.setNGValue("cmplx_Customer_ISFetchDetails", "Y");
					SKLogger.writeLog("RLOS_Initiation ", "Fetch Detail Started");
					String cif_no = formObject.getNGValue("cmplx_Customer_CIFNO");
					SKLogger.writeLog("RLOS_Initiation ", "Value of cif_id_avail"+cif_no);
					if(cif_no.equalsIgnoreCase("")){ 
						//Deepak Code change for Entity Details
						//if("Is_EID_Genuine".equalsIgnoreCase("Y") && "cmplx_Customer_EmiratesID" != ""){
						SKLogger.writeLog("RLOS_Initiation ", "Value of cif_id_avail is false"+cif_no);
						String EID = formObject.getNGValue("cmplx_Customer_EmiratesID");
						SKLogger.writeLog("RLOS_Initiation ", "EID value for Entity Details: "+EID );
						String ReadFrom_card_exc = formObject.getNGValue("cmplx_Customer_readfromcardflag");
						if( EID!=null && !EID.equalsIgnoreCase("")&& ReadFrom_card_exc.equalsIgnoreCase("Y")){
							if( EID!=null && !EID.equalsIgnoreCase("")){
								outputResponse = GenerateXML("ENTITY_DETAILS","Primary_CIF");
								ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
								SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
								if(ReturnCode.equalsIgnoreCase("0000")){
									formObject.setNGValue("Is_Entity_Details","Y");
									valueSetCustomer(outputResponse , "Primary_CIF");

								}
								else{
									formObject.setNGValue("Is_Entity_Details","N");

								}
								SKLogger.writeLog("RLOS value of Entity Details",formObject.getNGValue("Is_Entity_Details"));



							}
						}
						outputResponse =GenerateXML("CUSTOMER_ELIGIBILITY","Primary_CIF");
						SKLogger.writeLog("RLOS value of ReturnDesc","Customer Eligibility");
						ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
						SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
						if(ReturnCode.equalsIgnoreCase("0000") )
						{
							setcustomer_enable();
							popupFlag="Y";
							valueSetCustomer(outputResponse , "Primary_CIF");    
							formObject.setNGValue("NTB","true");//added by Akshay
							formObject.setNGValue("Is_Customer_Eligibility","Y");
							parse_cif_eligibility(outputResponse,"Primary_CIF");
							String NTB_flag = formObject.getNGValue("cmplx_Customer_NTB");
							//Customer_enable();
							// if(NTB_flag.equalsIgnoreCase("true")){
							//  Customer_enable();

							//}
							if(NTB_flag.equalsIgnoreCase("true")){
								formObject.setVisible("Customer_Frame2", false);
								SKLogger.writeLog("RLOS", "inside Customer Eligibility to through Exception to Exit:");
								alert_msg = "Customer is a New to Bank Customer.";
								//added By Akshay

								String  GCC="BH,IQ,KW,OM,QA,SA,AE";
								if(GCC.contains(formObject.getNGValue("cmplx_Customer_Nationality")))
								{
									SKLogger.writeLog("RLOS val change ","Inside GCC for Nationality");
									formObject.setNGValue("cmplx_Customer_GCCNational","Y");
								}
								else
								{
									formObject.setNGValue("cmplx_Customer_GCCNational","N");
								}
								//ended By akshay		
								throw new ValidatorException(new FacesMessage(alert_msg));

							}
							else{
								alert_msg = fetch_cust_details_primary();
							}



							SKLogger.writeLog("RLOS value of Customer Details",formObject.getNGValue("Is_Customer_Eligibility"));
							SKLogger.writeLog("RLOS value of BlacklistFlag",formObject.getNGValue("BlacklistFlag"));
							SKLogger.writeLog("RLOS value of DuplicationFlag",formObject.getNGValue("DuplicationFlag"));
							SKLogger.writeLog("RLOS value of IsAcctCustFlag",formObject.getNGValue("IsAcctCustFlag"));

							if(formObject.getNGValue("Is_Customer_Eligibility").equalsIgnoreCase("Y") && formObject.getNGValue("Is_Customer_Details").equalsIgnoreCase("Y"))
							{ 
								SKLogger.writeLog("RLOS value of Customer Details","inside if condition");
								SKLogger.writeLog("RLOS Initiation ", "Customer Eligibility and Customer details fetched sucessfully");
								formObject.setEnabled("FetchDetails", false); 
								formObject.setEnabled("Customer_Button1", false);
								alert_msg = "Existing Customer Details fetched Sucessfully!";
							}
							else{
								SKLogger.writeLog("RLOS Initiation ", "Customer Eligibility and Customer details failed");
								alert_msg = "Fetch Customer Details operation failed, try after some time or contact administrator !";
								formObject.setEnabled("FetchDetails", true);
								formObject.setEnabled("Customer_Button1", false);
							}
							SKLogger.writeLog("RLOS value of Customer Details ----1234","");
							formObject.RaiseEvent("WFSave");

							popupFlag="Y";
							SKLogger.writeLog("RLOS Initiation", "Alert msg to be displayed on screen: "+alert_msg);
							throw new ValidatorException(new FacesMessage(alert_msg));



						}
						else{
							formObject.setNGValue("Is_Customer_Eligibility","N");
							popupFlag="Y";
							alert_msg = "CUSTOMER ELIGIBILITY operation failed.";
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
					String NegatedFlag="";
					popupFlag="Y";
					outputResponse =GenerateXML("CUSTOMER_ELIGIBILITY","Primary_CIF");
					SKLogger.writeLog("RLOS value of ReturnDesc","Customer Eligibility");
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
					formObject.setNGValue("Is_Customer_Eligibility","Y");


					if(ReturnCode.equalsIgnoreCase("0000")){
						valueSetCustomer(outputResponse,"Primary_CIF"); 
						parse_cif_eligibility(outputResponse,"Primary_CIF");
						BlacklistFlag= (outputResponse.contains("<BlacklistFlag>")) ? outputResponse.substring(outputResponse.indexOf("<BlacklistFlag>")+"</BlacklistFlag>".length()-1,outputResponse.indexOf("</BlacklistFlag>")):"";
						SKLogger.writeLog("PL value of BlacklistFlag_Part","Customer is BlacklistedFlag"+BlacklistFlag);
						DuplicationFlag= (outputResponse.contains("<DuplicationFlag>")) ? outputResponse.substring(outputResponse.indexOf("<DuplicationFlag>")+"</DuplicationFlag>".length()-1,outputResponse.indexOf("</DuplicationFlag>")):"";
						SKLogger.writeLog("PL value of BlacklistFlag_Part","Customer is DuplicationFlag"+DuplicationFlag);
						NegatedFlag= (outputResponse.contains("<NegatedFlag>")) ? outputResponse.substring(outputResponse.indexOf("<NegatedFlag>")+"</NegatedFlag>".length()-1,outputResponse.indexOf("</NegatedFlag>")):"";
						SKLogger.writeLog("PL value of BlacklistFlag_Part","Customer is NegatedFlag"+NegatedFlag);
						formObject.setNGValue("Is_Customer_Eligibility","Y");
						formObject.RaiseEvent("WFSave");
						SKLogger.writeLog("RLOS value of ReturnDesc Is_Customer_Eligibility",formObject.getNGValue("Is_Customer_Eligibility"));
						formObject.setNGValue("BlacklistFlag",BlacklistFlag);
						formObject.setNGValue("DuplicationFlag",DuplicationFlag);
						formObject.setNGValue("IsAcctCustFlag",NegatedFlag);
						String NTB_flag = formObject.getNGValue("cmplx_Customer_NTB");
						if(NTB_flag.equalsIgnoreCase("true")){
							SKLogger.writeLog("RLOS", "inside Customer Eligibility to through Exception to Exit:");
							alert_msg = "Customer is a New to Bank Customer.";
						}
						else{
							alert_msg = "Customer is an Existing Customer.";
						}


					}
					else{
						formObject.setNGValue("Is_Customer_Eligibility","N");
						formObject.RaiseEvent("WFSave");
					}
					SKLogger.writeLog("RLOS value of Customer Details",formObject.getNGValue("Is_Customer_Eligibility"));
					SKLogger.writeLog("RLOS value of BlacklistFlag",formObject.getNGValue("BlacklistFlag"));
					SKLogger.writeLog("RLOS value of DuplicationFlag",formObject.getNGValue("DuplicationFlag"));
					SKLogger.writeLog("RLOS value of IsAcctCustFlag",formObject.getNGValue("IsAcctCustFlag"));
					formObject.RaiseEvent("WFSave");
					throw new ValidatorException(new FacesMessage(alert_msg));
					//}
				}
				//Customer Details Call on Dedupe Summary Button as well(Tanshu Aggarwal-29/05/2017)





				else   if(pEvent.getSource().getName().equalsIgnoreCase("Send_OTP_Btn"))
				{
					formObject.setNGValue("otp_ref_no", formObject.getWFFolderId());
					SKLogger.writeLog("ref no", formObject.getWFFolderId()+"");
					hm.put("Send_OTP_Btn","Clicked");
					popupFlag="Y";
					outputResponse = GenerateXML("OTP_MANAGEMENT","GenerateOTP");
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
					if(ReturnCode.equalsIgnoreCase("0000") ){
						valueSetCustomer(outputResponse , "");    
						SKLogger.writeLog("RLOS value of OTP_Generation","OTP is generated");
						formObject.setEnabled("OTP_No",true);
						formObject.setEnabled("Validate_OTP_Btn",true);
						alert_msg = "OTP Generated Sucessfully";
					}
					else{
						//formObject.setNGValue("OTP_Generation","OTP is not generated");
						alert_msg = "OTP Generated failed";
						formObject.setEnabled("OTP_No",false);
						formObject.setEnabled("Validate_OTP_Btn",false);

					}
					SKLogger.writeLog("RLOS value of OTP_Generation","OTP generation");
					try
					{
						throw new ValidatorException(new FacesMessage(alert_msg));
					}
					finally{hm.clear();}
				}

				else   if(pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails_Button4")){

					outputResponse = GenerateXML("CUSTOMER_DETAILS","Signatory_CIF");
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
					if(ReturnCode.equalsIgnoreCase("0000") ){
						formObject.setNGValue("Is_Customer_Details_AuthSign","Y");
						SKLogger.writeLog("RLOS value of EID_Genuine","value of Guarantor_CIF"+formObject.getNGValue("Is_Customer_Details_AuthSign"));
						valueSetCustomer(outputResponse , "Signatory_CIF");    
						SKLogger.writeLog("RLOS value of Customer Details","Guarantor_CIF is generated");
						SKLogger.writeLog("RLOS value of Customer Details",formObject.getNGValue("Is_Customer_Details_AuthSign"));
					}
					else{
						SKLogger.writeLog("Customer Details","Customer Details is not generated");
						formObject.setNGValue("Is_Customer_Details_AuthSign","N");
					}
					SKLogger.writeLog("RLOS value of Signatory_CIF",formObject.getNGValue("Is_Customer_Details_AuthSign"));
				}


				else   if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_SendSMS"))
				{

					SKLogger.writeLog("inside DecisionHistory_Button6", "");

					outputResponse = GenerateXML("SEND_ADHOC_ALERT","");
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					SKLogger.writeLog("RLOS value of ReturnCode of SEND SMS",ReturnCode);
					if(ReturnCode.equalsIgnoreCase("0000") ){
						valueSetCustomer(outputResponse , "");    
						SKLogger.writeLog("RLOS value of OTP_Generation","SMS is send");
						//formObject.setEnabled("OTP_No",true);
						//formObject.setEnabled("Validate_OTP_Btn",true);
						alert_msg = "SMS Sent Sucessfully";
					}
					else{
						//formObject.setNGValue("OTP_Generation","OTP is not generated");
						alert_msg = "failur while sending SMS";
						SKLogger.writeLog("RLOS value of OTP_Generation","Error while sending SMS");
					}

				}




				else  if(pEvent.getSource().getName().equalsIgnoreCase("Validate_OTP_Btn"))
				{

					outputResponse = GenerateXML("OTP_MANAGEMENT","ValidateOTP");
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
					OTPStatus=(outputResponse.contains("<OTPStatus>")) ? outputResponse.substring(outputResponse.indexOf("<OTPStatus>")+"</OTPStatus>".length()-1,outputResponse.indexOf("</OTPStatus>")):"";    
					SKLogger.writeLog("RLOS value of OTPStatus",OTPStatus);
					if(ReturnCode.equalsIgnoreCase("0000") ){
						valueSetCustomer(outputResponse , "");    
						formObject.setNGValue("cmplx_Customer_OTPValidation","true");

						SKLogger.writeLog("RLOS value of OTP_Generation","OTP is generated");
						formObject.setNGValue("OTPStatus",OTPStatus);
						SKLogger.writeLog("OTPStatus getNGValue",formObject.getNGValue("OTPStatus"));

						alert_msg = "OTP validation Sucessfully";

					}
					else{
						formObject.setNGValue("OTP_Generation","OTP is not generated");
						alert_msg = "OTP validation failed";
					}
					SKLogger.writeLog("RLOS value of OTP_Generation","OTP generation");
					try
					{
						popupFlag="Y";
						throw new ValidatorException(new FacesMessage(alert_msg));
					}
					finally{hm.clear();}
				}


				else  if(pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails_Button3")){
					SKLogger.writeLog("RLOS value of ReturnCode","CompanyDetails_Button3");
					outputResponse = GenerateXML("CUSTOMER_DETAILS","Corporation_CIF");
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
					String CustId = (outputResponse.contains("<CustId>")) ? outputResponse.substring(outputResponse.indexOf("<CustId>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</CustId>")):"";    
					SKLogger.writeLog("RLOS value of CustId",CustId);
					String CorpName = (outputResponse.contains("<CorpName>")) ? outputResponse.substring(outputResponse.indexOf("<CorpName>")+"</CorpName>".length()-1,outputResponse.indexOf("</CorpName>")):"";    
					SKLogger.writeLog("RLOS value of CorpName",CorpName);
					String BusinessIncDate = (outputResponse.contains("<BusinessIncDate>")) ? outputResponse.substring(outputResponse.indexOf("<BusinessIncDate>")+"</BusinessIncDate>".length()-1,outputResponse.indexOf("</BusinessIncDate>")):"";    
					SKLogger.writeLog("RLOS value of BusinessIncDate",BusinessIncDate);
					String LegEnt = (outputResponse.contains("<LegEnt>")) ? outputResponse.substring(outputResponse.indexOf("<LegEnt>")+"</LegEnt>".length()-1,outputResponse.indexOf("</LegEnt>")):"";    
					SKLogger.writeLog("RLOS value of LegEnt",LegEnt);
					if(ReturnCode.equalsIgnoreCase("0000") ){
						formObject.setNGValue("Is_Customer_Details_CompanyCIF","Y");

						SKLogger.writeLog("RLOS value of EID_Genuine","value of company Details corporation"+formObject.getNGValue("Is_Customer_Details_CompanyCIF"));

						valueSetCustomer(outputResponse , "");  
						try{

							String Date1=BusinessIncDate;
							SKLogger.writeLog("RLOS value of Date1111",Date1);
							SimpleDateFormat sdf1=new SimpleDateFormat("dd-mm-yyyy");
							SimpleDateFormat sdf2=new SimpleDateFormat("dd/mm/yyyy");
							String Datechanged=sdf2.format(sdf1.parse(Date1));
							SKLogger.writeLog("RLOS value ofDatechanged",Datechanged);
							formObject.setNGValue("cmplx_CompanyDetails_DateOfEstb",Datechanged);
						}
						catch(Exception ex){

						}
						SKLogger.writeLog("RLOS value of Customer Details","corporation cif");
						SKLogger.writeLog("RLOS value of Customer Details",formObject.getNGValue("Is_Customer_Details_CompanyCIF"));
					}
					else{
						SKLogger.writeLog("Customer Details","Customer Details Corporation CIF is not generated");
						formObject.setNGValue("Is_Customer_Details_CompanyCIF","N");
					}
					SKLogger.writeLog("RLOS value of  Corporation CIF",formObject.getNGValue("Is_Customer_Details_CompanyCIF"));
				}



				else  if (pEvent.getSource().getName().equalsIgnoreCase("Button9")){

					if(!formObject.isVisible("IncomeDetails_Frame1")){
						formObject.fetchFragment("Incomedetails", "IncomeDetails", "q_IncomeDetails");
						formObject.setNGFrameState("Incomedetails", 0);
						formObject.setNGFrameState("Incomedetails", 1);

						hm.put("Button9","Clicked");
						popupFlag="N";
						outputResponse = GenerateXML("ACCOUNT_SUMMARY","");
						ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
						SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
						if(ReturnCode.equalsIgnoreCase("0000") ){
							valueSetCustomer(outputResponse , "");
							formObject.setNGValue("Is_Account_Summary","Y");
						}
						else{
							formObject.setNGValue("Is_Account_Summary","N");
						}
						SKLogger.writeLog("RLOS value of Account Summary",formObject.getNGValue("Is_Account_Summary"));
						//ended
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}

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


				else   if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails_Button2")) {
					String EmpName=formObject.getNGValue("EMploymentDetails_Text21");
					String EmpCode=formObject.getNGValue("EMploymentDetails_Text22");
					SKLogger.writeLog("RLOS", "EMpName$"+EmpName+"$");
					String query=null;
					if(EmpName.trim().equalsIgnoreCase(""))
						query="select distinct(EMPR_NAME),EMPLOYER_CODE,INCLUDED_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY from NG_RLOS_ALOC_OFFLINE_DATA where EMPLOYER_CODE Like '%"+EmpCode+"%'";

					else if(EmpCode.trim().equalsIgnoreCase(""))
						query="select distinct(EMPR_NAME),EMPLOYER_CODE,INCLUDED_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY from NG_RLOS_ALOC_OFFLINE_DATA where EMPR_NAME Like '%"+EmpName + "%'";

					else
						query="select distinct(EMPR_NAME),EMPLOYER_CODE,INCLUDED_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY from NG_RLOS_ALOC_OFFLINE_DATA where EMPR_NAME Like '%"+EmpName + "%' or EMPLOYER_CODE Like '%"+EmpCode+"%'";

					SKLogger.writeLog("RLOS", "query is: "+query);
					populatePickListWindow(query,"EMploymentDetails_Button2", "Employer Name,Employer Code,INCLUDED IN PL ALOC,INCLUDED IN CC ALOC,INDUSTRY SECTOR,INDUSTRY MACRO,INDUSTRY MICRO,CONSTITUTION,NAME OF FREEZONE AUTHORITY", true, 20);			     
				}

				else if(pEvent.getSource().getName().equalsIgnoreCase("cmplx_ExternalLiabilities_AECBConsent")){
					if(formObject.getNGValue("cmplx_ExternalLiabilities_AECBConsent").equals("true"))
						formObject.setEnabled("ExternalLiabilities_Button1",true);
					else
						formObject.setEnabled("ExternalLiabilities_Button1",false);
				}



				else if(pEvent.getSource().getName().equalsIgnoreCase("Eligibility_Emp")){
					SKLogger.writeLog("Rlos Current Selected Sheet is",formObject.getSelectedSheet(pEvent.getSource().getName())+"");
					formObject.setSelectedSheet("ParentTab",3);		
					formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligAndProductInfo");
					formObject.setNGFrameState("EligibilityAndProductInformation", 0);
					formObject.setNGFrameState("EligibilityAndProductInformation", 1);
					formObject.setNGFrameState("EligibilityAndProductInformation", 0);
				}


				else if (pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails_Button2"))
				{

					formObject.setNGValue("supplement_WIname",formObject.getWFWorkitemName());	
					SKLogger.writeLog("RLOS", "Inside Supplement_add button: "+formObject.getNGValue("supplement_WIname"));
					formObject.ExecuteExternalCommand("NGAddRow", "SupplementCardDetails_cmplx_SupplementGrid");		
				}

				else if(pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo_Button1"))
				{
					if(formObject.getNGValue("Product_Type").contains("Personal Loan")){
						String query="Select PRIME_TYPE,LSM_PRODDIFFRATE,prime_type_rate from NG_master_Scheme where SCHEMEId='"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,8)+"'";
						SKLogger.writeLog("RLOS","Inside ELigibiltyAndProductInfo_Button1-->Query is:"+query);
						List<List<String>> marginRate=formObject.getDataFromDataSource(query);
						if(marginRate.get(0).get(0)!=null && marginRate.get(0).get(0)!="" && marginRate.size()>0){
							String baseRateType=marginRate.get(0).get(0);
							String baseRate=marginRate.get(0).get(1);
							String ProdprefRate=marginRate.get(0).get(2);
							SKLogger.writeLog("RLOS","List is:"+marginRate);
							String netRate=formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate");
							formObject.setNGValue("cmplx_EligibilityAndProductInfo_BAseRate", baseRate);
							formObject.setNGValue("cmplx_EligibilityAndProductInfo_BaseRateType", baseRateType);
							formObject.setNGValue("cmplx_EligibilityAndProductInfo_ProdPrefRate", ProdprefRate);
							formObject.setNGValue("cmplx_EligibilityAndProductInfo_FinalInterestRate",netRate);
							formObject.setNGValue("cmplx_EligibilityAndProductInfo_MArginRate", Float.parseFloat(netRate)-Float.parseFloat(baseRate)-Float.parseFloat(ProdprefRate));
						}
					}	
					//SKLogger.writeLog("RLOS","Inside ELigibiltyAndProductInfo_Button1->MarginRate:"+MarginRate);
					List objInput=new ArrayList();
					objInput.add("Text:"+formObject.getWFWorkitemName());
					objInput.add("Text:CIF_ID");
					SKLogger.writeLog("RLOS","objInput args are: "+objInput.get(0)+objInput.get(1));
					formObject.getDataFromStoredProcedure("ng_RLOS_CASProductDedupeCheck", objInput);
					SKLogger.writeLog("RLOS","ng_RLOS_CASProductDedupeCheck Procedure Executed!!");
				}


				else if (pEvent.getSource().getName().equalsIgnoreCase("FATCA_Button1")){
					String text=formObject.getNGItemText("cmplx_FATCA_listedreason", formObject.getSelectedIndex("cmplx_FATCA_listedreason"));
					SKLogger.writeLog("RLOS", "Inside FATCA_Button1 "+text);
					formObject.addItem("cmplx_FATCA_selectedreason", text);
					try {
						formObject.removeItem("cmplx_FATCA_listedreason", formObject.getSelectedIndex("cmplx_FATCA_listedreason"));
						formObject.setSelectedIndex("cmplx_FATCA_listedreason", -1);

					}catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						printException(e);
					}

				}


				else if (pEvent.getSource().getName().equalsIgnoreCase("FATCA_Button2")){
					SKLogger.writeLog("RLOS", "Inside FATCA_Button2 ");
					formObject.addItem("cmplx_FATCA_listedreason", formObject.getNGItemText("cmplx_FATCA_selectedreason", formObject.getSelectedIndex("cmplx_FATCA_selectedreason")));
					try {
						formObject.removeItem("cmplx_FATCA_selectedreason", formObject.getSelectedIndex("cmplx_FATCA_selectedreason"));
						formObject.setSelectedIndex("cmplx_FATCA_selectedreason", -1);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						printException(e);
					}
				}


				else if (pEvent.getSource().getName().equalsIgnoreCase("IncomingDoc_AddFromPCButton")){
					IRepeater repObj=null;
					repObj = formObject.getRepeaterControl("IncomingDoc_Frame");
					repObj.getValue(repObj.getChangeIndex(),"IncomingDoc_Label1");
					SKLogger.writeLog("","value of repeater:"+repObj.getValue(repObj.getChangeIndex(),"IncomingDoc_Label1"));


				}    


				else if(pEvent.getSource().getName().equalsIgnoreCase("Reject")){
					SKLogger.writeLog("Rlos Current Selected Sheet is",formObject.getSelectedSheet(pEvent.getSource().getName())+"");
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

					outputResponse = GenerateXML("AECB","");
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);

					if(ReturnCode.equalsIgnoreCase("0000") ){
						valueSetCustomer(outputResponse , "");


						formObject.setNGValue("IS_AECB","Y");

					}
					else{
						formObject.setNGValue("IS_AECB","Y");
					}
					SKLogger.writeLog("RLOS value of IS_AECB",formObject.getNGValue("IS_AECB"));
					//ended
					try

					{
						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));

					}
					finally{hm.clear();}


				}

				else  if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Button3"))
				{
					getCustAddress_details();

					String NTB_flag = formObject.getNGValue("cmplx_Customer_NTB");
					String NEP_flag = formObject.getNGValue("cmplx_Customer_NEP");
					String CIF_no = formObject.getNGValue("cmplx_Customer_CIFNO");
					SKLogger.writeLog("RLOS_Initiation: ", "inside create Account/Customer NTB value: "+NTB_flag );
					if(NTB_flag.equalsIgnoreCase("true") || NEP_flag.equalsIgnoreCase("true")||CIF_no.equalsIgnoreCase("")){
						formObject.setNGValue("curr_user_name",formObject.getUserName());
						outputResponse = GenerateXML("NEW_CUSTOMER_REQ","");
						ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
						SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
						if(ReturnCode.equalsIgnoreCase("0000")){
							formObject.setEnabled("DecisionHistory_Button3",false);
							valueSetCustomer(outputResponse , "");   
							formObject.setNGValue("cmplx_DecisionHistory_CifNo", formObject.getNGValue("cmplx_Customer_CIFNO"));
							SKLogger.writeLog("RLOS value of ReturnDesc","Inside if of New customer Req");


							outputResponse = GenerateXML("NEW_ACCOUNT_REQ","");
							ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
							SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);

							if(ReturnCode.equalsIgnoreCase("0000") ){
								valueSetCustomer(outputResponse , "");    
								formObject.setNGValue("Is_Account_Create","Y");
								formObject.setNGValue("EligibilityStatus","Y");
								formObject.setNGValue("EligibilityStatusCode","Y");
								formObject.setNGValue("EligibilityStatusDesc","Y");
							}
							else{
								formObject.setNGValue("Is_Account_Create","N");
							}
							SKLogger.writeLog("RLOS value of Account Request",formObject.getNGValue("Is_Account_Create"));
							SKLogger.writeLog("RLOS value of EligibilityStatus",formObject.getNGValue("EligibilityStatus"));
							SKLogger.writeLog("RLOS value of EligibilityStatusCode",formObject.getNGValue("EligibilityStatusCode"));
							SKLogger.writeLog("RLOS value of EligibilityStatusDesc",formObject.getNGValue("EligibilityStatusDesc"));



						}
						else{
							SKLogger.writeLog("RLOS value of ReturnDesc","Inside else of New Customer Req");
						}
					}
					else
					{
						SKLogger.writeLog("submit button","inside else condition ##AKSHAY##");
						//String wi_name= formObject.getWFWorkitemName();
						/*
						 List<List<String>> AccountCount= formObject.getDataFromDataSource("Select count from ng_RLOS_CUSTEXPOSE_AcctDetails where AcctType='CURRENT ACCOUNT' AND Wi_Name="+wi_name+";");
						int countCurrentAccount= AccountCount.size();
						SKLogger.writeLog("RLOS count of current account not NTB",countCurrentAccount+"");
						 */
						//SKLogger.writeLog("RLOS"," count of current account: "+formObject.getNGValue("AccountCount"));

						String query="Select count(*) from ng_RLOS_CUSTEXPOSE_AcctDetails where AcctType='CURRENT ACCOUNT' AND Wi_Name='"+formObject.getWFWorkitemName()+"'";
						List<List<String>> AccountCount= formObject.getDataFromDataSource(query);
						SKLogger.writeLog("RLOS", "Query is: "+query+" currValue In AccountCount is "+AccountCount);
						SKLogger.writeLog("RLOS", "NTB is:"+formObject.getNGValue("cmplx_Customer_NTB"));
						int countCurrentAccount =  Integer.parseInt(AccountCount.get(0).get(0));


						if((formObject.getNGValue("cmplx_Customer_NTB").equals("false") && countCurrentAccount==0)){
							SKLogger.writeLog("RLOS","NTB: "+formObject.getNGValue("cmplx_Customer_NTB")+" Account Count:"+formObject.getNGValue("AccountCount"));
							//createCustomer();
							outputResponse = GenerateXML("NEW_ACCOUNT_REQ","");
							ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
							SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
							if(ReturnCode.equalsIgnoreCase("0000") ){
								valueSetCustomer(outputResponse , "");    
								formObject.setNGValue("Is_Account_Create","Y");
							}
							else{
								formObject.setNGValue("Is_Account_Create","N");
							}
							SKLogger.writeLog("RLOS value of Account Request",formObject.getNGValue("Is_Account_Create"));
							if(formObject.getNGValue("Is_Account_Create").equalsIgnoreCase("Y")){ 
								SKLogger.writeLog("RLOS value of Is_Account_Create","inside if condition");
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
					SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
					if(ReturnCode.equalsIgnoreCase("0000") ){
						valueSetCustomer(outputResponse , "");


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
					SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
					if(ReturnCode.equalsIgnoreCase("0000") ){
						formObject.setNGValue("Is_Customer_Details","Y");
						SKLogger.writeLog("RLOS value of EID_Genuine","value of Guarantor_CIF"+formObject.getNGValue("Is_Customer_Details"));
						valueSetCustomer(outputResponse , "Guarantor_CIF");    
						SKLogger.writeLog("RLOS value of Customer Details","Guarantor_CIF is generated");
						SKLogger.writeLog("RLOS value of Customer Details",formObject.getNGValue("Is_Customer_Details"));
					}
					else{
						SKLogger.writeLog("Customer Details","Customer Details is not generated");
						formObject.setNGValue("Is_Customer_Details","N");
					}
					SKLogger.writeLog("RLOS value of Guarantor_CIF",formObject.getNGValue("Is_Customer_Details"));
				}
				 */
				else   if(pEvent.getSource().getName().equalsIgnoreCase("ReadFromCIF")){

					outputResponse = GenerateXML("CUSTOMER_DETAILS","Guarantor_CIF");
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
					if(ReturnCode.equalsIgnoreCase("0000")){
						formObject.setNGValue("Is_Customer_Details_Guarantor","Y");
						SKLogger.writeLog("RLOS value of EID_Genuine","value of Guarantor_CIF"+formObject.getNGValue("Is_Customer_Details_Guarantor"));
						valueSetCustomer(outputResponse , "Guarantor_CIF");    
						SKLogger.writeLog("RLOS value of Customer Details","Guarantor_CIF is generated");
						SKLogger.writeLog("RLOS value of Customer Details",formObject.getNGValue("Is_Customer_Details_Guarantor"));
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
						SKLogger.writeLog("Customer Details","Customer Details is not generated");
						formObject.setNGValue("Is_Customer_Details_Guarantor","N");
					}
					SKLogger.writeLog("RLOS value of Guarantor_CIF",formObject.getNGValue("Is_Customer_Details_Guarantor"));
				}


				// added by Akshay for world_check on initiation
				if (pEvent.getSource().getName().equalsIgnoreCase("WorldCheck_fetch")) {
					popupFlag="Y";
					String columnValues="";
					String[] columnValues_arr;
					//	columnValues=columnValues.join(",",columnValues_arr);
					SKLogger.writeLog("","inside worldcheck"); 
					outputResponse = GenerateXML("CUSTOMER_SEARCH_REQUEST","");
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);

					if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000") ){
						SKLogger.writeLog("PL value of WORLD CHECKt","inside if of WORLDCHECK");
						formObject.fetchFragment("WorldCheck", "WorldCheck1", "q_WorldCheck");	
						formObject.setVisible("WorldCheck", false);
						valueSetCustomer(outputResponse,"");	
						formObject.setNGValue("IS_WORLD_CHECK","Y");
						alert_msg= "WORLD CHECK sucessfull";
					}
					else if(ReturnCode.equalsIgnoreCase("9999")){
						alert_msg= "WORLD CHECK sucessfull....No records found!!!";
						formObject.setNGValue("IS_WORLD_CHECK","Y");
					}
					else{
						formObject.setNGValue("IS_WORLD_CHECK","N");
						SKLogger.writeLog("PL value of WORLD CHECK","inside else of WORLD CHECK");
						alert_msg= "Error while performing WORLD CHECK";
					}
					SKLogger.writeLog("PL value of WORLD CHECKt","alert: "+ alert_msg);
					formObject.RaiseEvent("WFSave");
					throw new ValidatorException(new FacesMessage(alert_msg));

				}
				// ended Akshay for world_check initiation


				if(pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo_Button1"))
				{		popupFlag="Y";
				formObject.setNGValue("DecCallFired","Eligibility");
				SKLogger.writeLog("$$After Generatexml for dectech call..outputResponse is : "+outputResponse,"");
				formObject.fetchFragment("WorldCheck", "WorldCheck1", "q_WorldCheck");	

				outputResponse = GenerateXML("DECTECH","");
				//SKLogger.writeLog("$$After Generatexml for CallFired : "+CallFired,"");
				SKLogger.writeLog("$$After Generatexml for dectech call..outputResponse ASDASDASDASD : "+outputResponse,"");


				SystemErrorCode =  (outputResponse.contains("<SystemErrorCode>")) ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"";
				SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
				if(SystemErrorCode.equalsIgnoreCase("")){
					valueSetCustomer(outputResponse , "");   
					alert_msg="Decision engine integration successful";
					SKLogger.writeLog("after value set customer for dectech call","");
					formObject.RaiseEvent("WFSave");
					throw new ValidatorException(new FacesMessage(alert_msg));


				}
				else{
					alert_msg="Critical error occurred Please contact administrator";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}

				//SKLogger.writeLog("$$After Generatexml for dectech call..outputResponse ALERT : "+outputResponse,"");


				//SKLogger.writeLog("$$After Generatexml for dectech call..outputResponse WFS : "+outputResponse,"");




				}
				if(pEvent.getSource().getName().equalsIgnoreCase("Eligibility_Emp"))
				{		popupFlag="Y";
				formObject.setNGValue("DecCallFired","Eligibility");
				SKLogger.writeLog("$$After Generatexml for dectech call..outputResponse is : "+outputResponse,"");
				formObject.fetchFragment("WorldCheck", "WorldCheck1", "q_WorldCheck");	

				outputResponse = GenerateXML("DECTECH","");
				//		SKLogger.writeLog("$$After Generatexml for CallFired : "+CallFired,"");
				SKLogger.writeLog("$$After Generatexml for dectech call..outputResponse ASDASDASDASD : "+outputResponse,"");


				SystemErrorCode =  (outputResponse.contains("<SystemErrorCode>")) ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"";
				SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
				if(SystemErrorCode.equalsIgnoreCase("")){
					valueSetCustomer(outputResponse , "");   
					alert_msg="Decision engine integration successful";
					SKLogger.writeLog("after value set customer for dectech call","");
					formObject.RaiseEvent("WFSave");
					throw new ValidatorException(new FacesMessage(alert_msg));


				}
				else{
					alert_msg="Critical error occurred Please contact administrator";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}

				//SKLogger.writeLog("$$After Generatexml for dectech call..outputResponse ALERT : "+outputResponse,"");


				//SKLogger.writeLog("$$After Generatexml for dectech call..outputResponse WFS : "+outputResponse,"");




				}

				if(pEvent.getSource().getName().equalsIgnoreCase("Button2"))
				{		popupFlag="Y";
				formObject.setNGValue("DecCallFired","CalculateDBR");
				formObject.fetchFragment("WorldCheck", "WorldCheck1", "q_WorldCheck");	

				outputResponse = GenerateXML("DECTECH","");
				SKLogger.writeLog("$$After Generatexml for dectech call..outputResponse is : "+outputResponse,"");
				SKLogger.writeLog("$$After Generatexml for dectech call..outputResponse ASDASDASDASD : "+outputResponse,"");

				SystemErrorCode =  (outputResponse.contains("<SystemErrorCode>")) ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"";
				SKLogger.writeLog("RLOS value of ReturnCode",SystemErrorCode);
				SKLogger.writeLog("RLOS value of outputreas",outputResponse);

				if((SystemErrorCode.equalsIgnoreCase(""))&& !outputResponse.equalsIgnoreCase("0")){
					valueSetCustomer(outputResponse , "");   
					alert_msg="Decision engine integration successful";
					SKLogger.writeLog("after value set customer for dectech call","");
					formObject.RaiseEvent("WFSave");
					throw new ValidatorException(new FacesMessage(alert_msg));


				}
				else{
					alert_msg="Critical error occurred Please contact administrator";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}



				//SKLogger.writeLog("$$After Generatexml for dectech call..outputResponse ALERT : "+outputResponse,"");


				//SKLogger.writeLog("$$After Generatexml for dectech call..outputResponse WFS : "+outputResponse,"");




				}



				else if(pEvent.getSource().getName().equalsIgnoreCase("Customer_save")){
					formObject.saveFragment("CustomerDetails");
					popupFlag = "Y";
					alert_msg="Customer details saved";
					formObject.setNGValue("cmplx_Customer_IscustomerSave", "Y");
					throw new ValidatorException(new FacesMessage(alert_msg));
				}


				else if(pEvent.getSource().getName().equalsIgnoreCase("Product_Save")){
					formObject.setNGValue("lbl_prod_val",formObject.getNGValue("PrimaryProduct"));
					formObject.setNGValue("lbl_scheme_val",formObject.getNGValue("Subproduct_productGrid"));
					formObject.setNGValue("lbl_card_prod_val",formObject.getNGValue("CardProduct_Primary"));
					formObject.setNGValue("lbl_Loan_amount_bal",formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,3));
								
					formObject.saveFragment("ProductDetailsLoader");
					formObject.RaiseEvent("WFSave");
					popupFlag = "Y";
					alert_msg="Product details Saved";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}


				else 	if(pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails_Save")){
					formObject.saveFragment("GuarantorDetails");
					popupFlag = "Y";
					alert_msg="Guarantor Details Saved";

					throw new ValidatorException(new FacesMessage(alert_msg));
				}


				else if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_Salaried_Save")){
					formObject.saveFragment("Incomedetails");
					popupFlag = "Y";
					alert_msg="Income Details Salaried Saved";

					throw new ValidatorException(new FacesMessage(alert_msg));
				}


				else if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_SelfEmployed_Save")){
					formObject.saveFragment("Incomedetails");
					popupFlag = "Y";
					alert_msg="Income Details for SelfEmployed Saved";

					throw new ValidatorException(new FacesMessage(alert_msg));
				}


				else if(pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails_Save")){
					formObject.setNGValue("Company_Name", formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",1,4));//added By Akshay on 16/9/17 to set header
					formObject.setNGValue("lbl_Comp_Name_val",formObject.getNGValue("Company_Name"));
					formObject.saveFragment("CompanyDetails");
					formObject.saveFragment("AuthorisedSignDetails");
					formObject.saveFragment("PartnerDetails");
					popupFlag = "Y";
					alert_msg="Company Details Saved";

					throw new ValidatorException(new FacesMessage(alert_msg));
				}


				else if(pEvent.getSource().getName().equalsIgnoreCase("Liability_New_Save")){
					formObject.saveFragment("Liability_container");
					popupFlag = "Y";
					alert_msg="Liability New Saved";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}


				else if(pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails_Save")){
					formObject.setNGValue("Employer_Name", formObject.getNGValue("cmplx_EmploymentDetails_EmpName"));//added By akshay on 16/9/17 to set header
					formObject.setNGValue("lbl_Comp_Name_val",formObject.getNGValue("Employer_Name"));

					formObject.saveFragment("EmploymentDetails");
					popupFlag = "Y";
					alert_msg="Employment Details Saved";

					throw new ValidatorException(new FacesMessage(alert_msg));
				}


				else if(pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo_Save")){
					formObject.saveFragment("EligibilityAndProductInformation");
					popupFlag = "Y";
					alert_msg="ELigibilty and Product Information Saved";

					throw new ValidatorException(new FacesMessage(alert_msg));
				}


				else 	if(pEvent.getSource().getName().equalsIgnoreCase("MiscellaneousFields_Save")){
					formObject.saveFragment("MiscFields");
					popupFlag = "Y";
					alert_msg="Miscellaneous Fields Saved";

					throw new ValidatorException(new FacesMessage(alert_msg));
				}


				else if(pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_Save")){
					formObject.saveFragment("Address_Details_container");
					popupFlag = "Y";
					alert_msg="Address Details Saved";

					throw new ValidatorException(new FacesMessage(alert_msg));
				}


				else if(pEvent.getSource().getName().equalsIgnoreCase("ContactDetails_Save")){
					formObject.saveFragment("Alt_Contact_container");
					popupFlag = "Y";
					alert_msg="Contact Details Saved";

					throw new ValidatorException(new FacesMessage(alert_msg));
				}


				else 	if(pEvent.getSource().getName().equalsIgnoreCase("CardDetails_save")){
					formObject.saveFragment("CardDetails");
					popupFlag = "Y";
					alert_msg="Card Details Saved";

					throw new ValidatorException(new FacesMessage(alert_msg));
				}


				else if(pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails_Save")){
					formObject.saveFragment("Supplementary_Container");
					popupFlag = "Y";
					alert_msg="Supplement Card Details Saved";

					throw new ValidatorException(new FacesMessage(alert_msg));
				}


				else if(pEvent.getSource().getName().equalsIgnoreCase("FATCA_Save")){
					formObject.saveFragment("FATCA");


					popupFlag = "Y";
					alert_msg="FATCA Fields Saved";

					throw new ValidatorException(new FacesMessage(alert_msg));
				}


				else if(pEvent.getSource().getName().equalsIgnoreCase("KYC_Save")){
					SKLogger.writeLog("RLOs", "Inside KYC save button");
					formObject.saveFragment("KYC");
					popupFlag = "Y";
					alert_msg="KYC Fields Saved";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}


				else if(pEvent.getSource().getName().equalsIgnoreCase("OECD_Save")){
					formObject.saveFragment("OECD");
					popupFlag = "Y";
					alert_msg="OECD Fields Saved";

					throw new ValidatorException(new FacesMessage(alert_msg));
				}

				else if(pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails_save")){
					formObject.saveFragment("ReferenceDetails");
					popupFlag = "Y";
					alert_msg="Reference Details Saved";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}

				else if(pEvent.getSource().getName().equalsIgnoreCase("ServiceRequest_Save")){
					formObject.saveFragment("CC_Loan_container");
					popupFlag = "Y";
					alert_msg="Service Request information Saved";

					throw new ValidatorException(new FacesMessage(alert_msg));
				}

				else if(pEvent.getSource().getName().equalsIgnoreCase("BTC_save") ){
					formObject.saveFragment("CC_Loan_container");
					popupFlag = "Y";
					alert_msg="BTC information Saved";

					throw new ValidatorException(new FacesMessage(alert_msg));
				}
				else if(pEvent.getSource().getName().equalsIgnoreCase("DDS_save")){
					formObject.saveFragment("CC_Loan_container");
					popupFlag = "Y";
					alert_msg="DDS information Saved";

					throw new ValidatorException(new FacesMessage(alert_msg));
				}
				else if(pEvent.getSource().getName().equalsIgnoreCase("SI_save") ){
					formObject.saveFragment("CC_Loan_container");
					popupFlag = "Y";
					alert_msg="SI information Saved";

					throw new ValidatorException(new FacesMessage(alert_msg));
				}
				else if(pEvent.getSource().getName().equalsIgnoreCase("RVC_Save")){
					formObject.saveFragment("CC_Loan_container");
					popupFlag = "Y";
					alert_msg="RVC information Saved";

					throw new ValidatorException(new FacesMessage(alert_msg));
				}


				else if(pEvent.getSource().getName().equalsIgnoreCase("IncomingDoc_Save")){
					SKLogger.writeLog("RLOS", "TANSHU Inside IncomingDoc_Save button!!");
					formObject.saveFragment("IncomingDocuments");
					popupFlag = "Y";
					alert_msg="Incoming Documents Saved ";

					throw new ValidatorException(new FacesMessage(alert_msg));
				}


				else if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Save")){
					formObject.saveFragment("DecisionHistoryContainer");
					popupFlag = "Y";
					alert_msg="Decision History Saved";

					throw new ValidatorException(new FacesMessage(alert_msg));
				}


				else if (pEvent.getSource().getName().equalsIgnoreCase("OECD_Button1")){					

					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_OECD_cmplx_GR_OecdDetails");
				}
				else if (pEvent.getSource().getName().equalsIgnoreCase("OECD_Button2")){
					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_OECD_cmplx_GR_OecdDetails");
				}


				else if (pEvent.getSource().getName().equalsIgnoreCase("OECD_Button3")){
					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_OECD_cmplx_GR_OecdDetails");
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Add")){
					formObject.setNGValue("NotepadDetails_WiNote",formObject.getWFWorkitemName());
					SKLogger.writeLog("RLOS", "Inside add button: "+formObject.getNGValue("NotepadDetails_WiNote"));
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_notegrid");
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Modify")){
					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_NotepadDetails_cmplx_notegrid");
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Delete")){
					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_NotepadDetails_cmplx_notegrid");

				}
				if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Add1")){
					formObject.setNGValue("NotepadDetails_WiTele",formObject.getWFWorkitemName());
					SKLogger.writeLog("RLOS", "Inside add button: "+formObject.getNGValue("NotepadDetails_WiTele"));
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_Telloggrid");
				}
				if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Clear")){
					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_NotepadDetails_cmplx_Telloggrid");

				}


				break;	




			case TAB_CLICKED:


				SKLogger.writeLog("RLOS akshay TAB select sheet caption: ",formObject.getSheetCaption(pEvent.getSource().getName(), formObject.getSelectedSheet(pEvent.getSource().getName())));
				String SheetCaption=formObject.getSheetCaption(pEvent.getSource().getName(), formObject.getSelectedSheet(pEvent.getSource().getName()));

				//Java takes height of entire container in getHeight while js takes current height	i.e collapsed/expanded


				break;


			case FRAGMENT_LOADED:
				SKLogger.writeLog(" In RLOS Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
				SKLogger.writeLog("RLOS","Value Of init type is:"+formObject.getNGValue("initiationChannel")); 
				if(formObject.getNGValue("initiationChannel").equalsIgnoreCase("M") ){
					SKLogger.writeLog("RLOS","Value Of init type is:"+formObject.getNGValue("initiationChannel")); 
					new RLOSCommonCode().DisableFragmentsOnLoad(pEvent);
				}
				else{
					if (pEvent.getSource().getName().equalsIgnoreCase("Customer")) {
						if(formObject.getNGValue("cmplx_Customer_NEP").equals("false")){
							formObject.setLocked("Customer_Frame1",true);
							//formObject.setEnabled("Customer_Frame1",false);
							formObject.setHeight("Customer_Frame1", 510);
							formObject.setHeight("CustomerDetails", 530);
							formObject.setLocked("Customer_save",false);

							formObject.setLocked("Customer_Frame2",false);
							formObject.setLocked("Customer_Frame3",false);
							if (formObject.getNGValue("cmplx_Customer_NTB").equals("true")){

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
						if (!formObject.getNGValue("cmplx_Customer_card_id_available").equals("true")){

							formObject.setLocked("ReadFromCard",false);
						}

						formObject.setLocked("cmplx_Customer_NEP",false);
						formObject.setLocked("cmplx_Customer_DLNo",false);
						formObject.setLocked("cmplx_Customer_Passport2",false);
						formObject.setLocked("cmplx_Customer_Passport3",false);
						formObject.setLocked("cmplx_Customer_PAssport4",false);
						SKLogger.writeLog(" In RLOS customer in fragmnt loaded", "");
						//formObject.setLocked("cmplx_Customer_SecNAtionApplicable",false);


						String EID = formObject.getNGValue("cmplx_Customer_EmiratesID");
						String NEP = formObject.getNGValue("cmplx_Customer_NEP");
						String card_not_avl = formObject.getNGValue("cmplx_Customer_CardNotAvailable");
						LoadPickList("cmplx_Customer_CustomerCategory", "select convert(varchar, Description) from NG_MASTER_CustomerCategory with (nolock)");
						formObject.setNGValue("cmplx_Customer_CustomerCategory","--Select--" );
						if(EID.equalsIgnoreCase("") && NEP.equalsIgnoreCase("false") && card_not_avl.equalsIgnoreCase("false")){
							SKLogger.writeLog(" In RLOS inside if customer in fragmnt loaded", "");
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
								SKLogger.writeLog(" In RLOS inside else customer in fragmnt loaded", "");
							}
						}
						formObject.setNGValue("ref_Relationship","FRIEND");
						//formObject.setNGValue("cmplx_Customer_COuntryOFResidence", "United Arab Emirates-AE");
						formObject.setNGValue("cmplx_Customer_CustomerCategory", "Normal");
						formObject.setEnabled("cmplx_Customer_OTPValidation", false);
						loadPicklistCustomer();
						SKLogger.writeLog("RLOS","Encrypted CIF is: "+formObject.getNGValue("encrypt_CIF"));
						//formObject.setNGValue("cmplx_Customer_CIFNO",formObject.getNGValue("encrypt_CIF"));
						try{
							String if_cif_available = formObject.getNGValue("cmplx_Customer_CIFNO");
							SKLogger.writeLog("Check for Cif","CIF is: "+if_cif_available);
							if(if_cif_available !=null && !if_cif_available.equalsIgnoreCase("") && !if_cif_available.equalsIgnoreCase(" ")){
								setcustomer_enable();
								SKLogger.writeLog("Check for Cif","Saurabh12345");
							}
							if(formObject.getNGValue("cmplx_Customer_card_id_available").equalsIgnoreCase("true") && "".equalsIgnoreCase(formObject.getNGValue("Is_Customer_Eligibility"))){
								formObject.setLocked("Customer_Button1", false);
							}
						}catch(Exception ex){
							SKLogger.writeLog("Saurabh exception", printException(ex));
						}

					}

					else if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
						LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct with (nolock)");
						LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
						formObject.setNGValue("ReqProd","--Select--");
					}

					else if (pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails")) {

						formObject.setNGValue("gender", "");
						formObject.setNGValue("nationality", "--Select--");
						// formObject.setNGValue("SignStatus", "--Select--");
						LoadPickList("title", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_title with (nolock) order by code");
						formObject.setNGValue("title", "");
						//LoadPickList("gender", "select convert(varchar, description) from NG_MASTER_gender with (nolock)");
						LoadPickList("gender", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_gender with (nolock) order by Code");
						LoadPickList("nationality", "select convert(varchar, description) from NG_MASTER_country with (nolock)");
					}

					else if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails")) {
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

					else if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")) {

						formObject.setLocked("QCAmount", true);//added by Akshay on 9/9/17 as per FSD
						formObject.setLocked("QCEMi", true);//added by Akshay on 9/9/17 as per FSD

						//change by saurabh on 9th July 17.
						if(formObject.getNGValue("cmplx_Liability_New_AECBconsentAvail").equalsIgnoreCase("true")){
							formObject.setLocked("Liability_New_fetchLiabilities",false);
						}
						else{
							formObject.setLocked("Liability_New_fetchLiabilities",true);
						}

						//added by abhishek
						int count = formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
						for(int i =0;i<count;i++){
							if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2).equalsIgnoreCase("BTC") || formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2).equalsIgnoreCase("SE")){
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

						//formObject.setLocked("Liability_New_fetchLiabilities",true);
						formObject.setLocked("takeoverAMount",true);
						formObject.setLocked("cmplx_Liability_New_DBR",true);
						formObject.setLocked("cmplx_Liability_New_DBRNet",true);
						formObject.setLocked("cmplx_Liability_New_AggrExposure",true);
						formObject.setLocked("cmplx_Liability_New_TAI",true);
						if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 4).equalsIgnoreCase("RESCE")){
							formObject.setVisible("Liability_New_Label25", true);
							formObject.setVisible("cmplx_Liability_New_paid_installments", true);
						}
						else{
							formObject.setVisible("Liability_New_Label25", false);
							formObject.setVisible("cmplx_Liability_New_paid_installments", false);
						}
					}


					else if (pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails")) {
						SKLogger.writeLog("RLOS","AKSHAY COMPANY FRAGMENT LOADED");
						formObject.setLocked("cif", true);
						formObject.setLocked("CompanyDetails_Button3", true);
						LoadPickList("appType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_ApplicantType with (nolock) order by code");
						LoadPickList("indusSector", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_IndustrySector with (nolock) order by code");
						LoadPickList("indusMAcro", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_EmpIndusMacro with (nolock) order by code");
						LoadPickList("indusMicro", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_EmpIndusMicro with (nolock) order by code");
						LoadPickList("legalEntity", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_LegalEntity with (nolock) order by code");
						LoadPickList("desig", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
						LoadPickList("desigVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
						LoadPickList("EOW", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_emirate with (nolock) order by code");
						LoadPickList("headOffice", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_emirate with (nolock) order by code");
						try{
							String customerName = formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_MiddleName")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme");
							if(customerName!=null && !customerName.equalsIgnoreCase("") && !customerName.equalsIgnoreCase(" ")){
								String Executexml= makeInputForGrid(customerName);
								SKLogger.writeLog("RLOS","Grid Data is"+Executexml);
								formObject.NGAddListItem("cmplx_CompanyDetails_cmplx_CompanyGrid",Executexml);
							}
						}catch(Exception ex){
							SKLogger.writeLog("RLOS","Exception: "+printException(ex));
						}
					}

					else if (pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails")) {
						formObject.setLocked("CIFNo", true);
						formObject.setLocked("AuthorisedSignDetails_Button4", true);
						formObject.setNGValue("SignStatus", "--Select--");
						LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_Country with (nolock) order by code");
						formObject.setNGValue("AuthorisedSignDetails_nationality", "");
						LoadPickList("SignStatus", "select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");

						String cif = formObject.getNGValue("cmplx_Customer_CIFNO");
						String name = formObject.getNGValue("cmplx_Customer_FIrstNAme");
						String nationality = formObject.getNGValue("cmplx_Customer_Nationality");
						String dob = formObject.getNGValue("cmplx_Customer_DOb");
						String visanum = formObject.getNGValue("cmplx_Customer_VisaNo");
						String visaexp = formObject.getNGValue("cmplx_Customer_VIsaExpiry");
						String pass = formObject.getNGValue("cmplx_Customer_PAssportNo");
						String passexp = formObject.getNGValue("cmplx_Customer_PassPortExpiry");

						String Executexml= "<ListItem><SubItem>"+cif+"</SubItem>" +
								"<SubItem>"+name+"</SubItem>"+
								"<SubItem>"+nationality+"</SubItem>"+
								"<SubItem>"+dob+"</SubItem>"+
								"<SubItem>"+visanum+"</SubItem>"+
								"<SubItem>"+visaexp+"</SubItem>"+
								"<SubItem></SubItem>"+
								"<SubItem>"+pass+"</SubItem>"+
								"<SubItem>"+passexp+"</SubItem>"+
								"<SubItem></SubItem>"+
								"<SubItem></SubItem>"+
								"<SubItem></SubItem></ListItem>";
						SKLogger.writeLog("RLOS","Grid Data is"+Executexml);
						formObject.NGAddListItem("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails",Executexml);
					}

					else if (pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails")) {
						LoadPickList("PartnerDetails_nationality", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
					}


					else if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) {

						String empid="AVI,MED,EDU,HOT,PROM";	
						if(formObject.getNGValue("cmplx_Customer_NEP").equals("false")){
							formObject.setLocked("EMploymentDetails_Label25",true);//Added by Akshay on 9/9/17 for enabling NEP type as per FSD
							formObject.setLocked("cmplx_EmploymentDetails_NepType",true);
						}
						setALOCListed();
						formObject.setVisible("cmplx_EmploymentDetails_tenancntrct",false);
						formObject.setVisible("EMploymentDetails_Label5",false);
						
						formObject.setVisible("cmplx_EmploymentDetails_channelcode",false);
						formObject.setVisible("EMploymentDetails_Label36",false);
						formObject.setLocked("cmplx_EmploymentDetails_EmpName",true);
						formObject.setLocked("cmplx_EmploymentDetails_EMpCode",true);
						formObject.setLocked("cmplx_EmploymentDetails_LOS",true);
						loadPicklist4();
						Fields_ApplicationType_Employment();
						//added By Akshay on 12/9/2017 to set this field as CAC if CAC is true
						if(formObject.getNGValue("cmplx_Liability_IS_CAC").equals("Y"))
						{
							formObject.setNGValue("cmplx_EmploymentDetails_targetSegCode", "CAC");
						}
						//ended By Akshay on 12/9/2017 to set this field as CAC if CAC is true

						if(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode").equalsIgnoreCase("NEP")){
							formObject.setVisible("EMploymentDetails_Label25",true);
							formObject.setVisible("cmplx_EmploymentDetails_NepType",true);
						}

						else if(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode").equalsIgnoreCase("FZD") || formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode").equalsIgnoreCase("FZE")){
							formObject.setVisible("cmplx_EmploymentDetails_Freezone",true);
							formObject.setVisible("EMploymentDetails_Label62",true);
							formObject.setVisible("cmplx_EmploymentDetails_FreezoneName",true);
						}

						else if(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode").equalsIgnoreCase("TEN"))
						{
							formObject.setVisible("cmplx_EmploymentDetails_tenancntrct",true);
							formObject.setVisible("EMploymentDetails_Label5",true);
						}

						else if(formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg").equals("Surrogate") && empid.contains(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")))
						{
							formObject.setVisible("cmplx_EmploymentDetails_IndusSeg",true);
							formObject.setVisible("EMploymentDetails_Label59",true);

						}

					}

					else if (pEvent.getSource().getName().equalsIgnoreCase("MiscellaneousFields")) {
						formObject.setLocked("cmplx_MiscFields_School",true);
						formObject.setLocked("cmplx_MiscFields_PropertyType",true);
						formObject.setLocked("cmplx_MiscFields_RealEstate",true);
						formObject.setLocked("cmplx_MiscFields_FarmEmirate",true);
					}

					else if (pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo")) {
						SKLogger.writeLog("RLOS value of Reason","Saurabh ELPINFO,Fragment Loaded");
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
						formObject.setLocked("cmplx_EligibilityAndProductInfo_LPFAmount",true);
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


					else if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails")) {

						loadPicklist_Address();
					}

					else if (pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails")) {
						if(!formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1).equalsIgnoreCase("Personal Loan")){
							formObject.setVisible("AlternateContactDetails_custdomicile",false);
							formObject.setVisible("AltContactDetails_Label14",false);	
						}
						SKLogger.writeLog("RLOS","Saurabh COMPANY FRAGMENT LOADED"+formObject.getNGValue("Product_Type"));
						if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1).equalsIgnoreCase("Credit Card")){
							formObject.setVisible("AlternateContactDetails_retainAccount",false);
						}
						else{
							formObject.setVisible("AlternateContactDetails_retainAccount",true);
						}	
					}

					else if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails")) {
						formObject.setLeft("CardDetails_Label5",288);
						formObject.setLeft("cmplx_CardDetails_SuppCardReq",288);
					}

					else if (pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails")) {
						LoadPickList("ResdCountry", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by code");
						LoadPickList("relationship", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_Relationship with (nolock) order by code");
						LoadPickList("nationality", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
						LoadPickList("gender", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_gender with (nolock) order by Code");
					}
					else if (pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails")) {
						LoadPickList("ReferenceDetails_ref_Relationship", "select '--Select--' as description,'' as code union select convert(varchar, description) from NG_MASTER_Relationship with (nolock) order by code");
						formObject.setNGValue("ReferenceDetails_ref_Relationship", "FRE");
					}
					else if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")) {
						LoadPickList("cmplx_FATCA_Category", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_category with (nolock) order by code");
						//LoadPickList("cmplx_FATCA_Category", "select '--Select--' union select convert(varchar, description) from NG_MASTER_category with (nolock)");
						LoadPickList("cmplx_FATCA_USRelation", " select '--Select--' as description, '' as code  union select convert(varchar,description),code  from ng_master_usrelation order by code");

						String usRelation = formObject.getNGValue("cmplx_FATCA_USRelation");
						if(usRelation.equalsIgnoreCase("O")){
							formObject.setEnabled("FATCA_Frame6", false);
							formObject.setEnabled("cmplx_FATCA_USRelation",true);
							formObject.setEnabled("FATCA_Save", true);
						}
					}

					else if (pEvent.getSource().getName().equalsIgnoreCase("OECD")) {
						LoadPickList("OECD_CountryBirth", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_Country with (nolock) order by code");
						LoadPickList("OECD_townBirth", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_city with (nolock) order by code");
						LoadPickList("OECD_CountryTaxResidence", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_Country with (nolock) order by code");
						LoadPickList("OECD_CRSFlagReason", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_CRSReason with (nolock) order by code");
						formObject.setLocked("OECD_CRSFlagReason",true);
					}

					else if (pEvent.getSource().getName().equalsIgnoreCase("CC_Loan")){
						formObject.setLocked("cmplx_CC_Loan_mchequeno",true);
						formObject.setLocked("cmplx_CC_Loan_mchequeDate",true);
						formObject.setLocked("cmplx_CC_Loan_mchequestatus",true);
						loadPicklist_ServiceRequest();
						//added By Akshay on 15/9/17 for disabling DDS tab if DDSFlag is false
						if(formObject.getNGValue("cmplx_CC_Loan_DDSFlag").equals("false"))
						{
							formObject.setLocked("cmplx_CC_Loan_DDSMode",true);
							formObject.setLocked("cmplx_CC_Loan_DDSAmount",true);
							formObject.setLocked("cmplx_CC_Loan_Percentage",true);
							formObject.setLocked("cmplx_CC_Loan_DDSExecDay",true);
							formObject.setLocked("cmplx_CC_Loan_AccType",true);
							formObject.setLocked("cmplx_CC_Loan_DDSIBanNo",true);
							formObject.setLocked("cmplx_CC_Loan_DDSStartdate",true);
							formObject.setLocked("cmplx_CC_Loan_DDSBankAName",true);
							formObject.setLocked("cmplx_CC_Loan_DDSEntityNo",true);
						}
						else if(formObject.getNGValue("cmplx_CC_Loan_DDSMode").equals("flat"))
						{
							formObject.setLocked("cmplx_CC_Loan_DDSAmount",false);

						}
						
						else if(formObject.getNGValue("cmplx_CC_Loan_DDSMode").equals("Per"))
						{
							formObject.setLocked("cmplx_CC_Loan_Percentage",false);

						}
						else{
							formObject.setLocked("cmplx_CC_Loan_DDSAmount",true);
							formObject.setLocked("cmplx_CC_Loan_Percentage",true);
						}
						//ended By Akshay on 15/9/17 for disabling DDS tab if DDSFlag is false

					}

					if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
						loadPicklist3();
						formObject.setNGValue("cmplx_DecisionHistory_Decision", "--Select--");
						formObject.setLocked("cmplx_DecisionHistory_DecisionReasonCode",true);
						String query="Select count(*) from ng_RLOS_CUSTEXPOSE_AcctDetails where AcctType='CURRENT ACCOUNT' AND Wi_Name='"+formObject.getWFWorkitemName()+"'";
						List<List<String>> AccountCount= formObject.getDataFromDataSource(query);
						SKLogger.writeLog("RLOS", "Query is: "+query+" Value In AccountCount is "+AccountCount);
						SKLogger.writeLog("RLOS", "NTB is:"+formObject.getNGValue("cmplx_Customer_NTB"));
						formObject.setNGValue("AccountCount", AccountCount.get(0).get(0));
						if((formObject.getNGValue("cmplx_Customer_NTB").equals("true") || AccountCount.get(0).get(0).equals("0")) && formObject.getNGValue("Product_Type").equals("Personal Loan")){
							SKLogger.writeLog("RLOS"," Product Type Is: "+formObject.getNGValue("Product_Type"));
							formObject.setVisible("DecisionHistory_Button3",true);
						}

						else
						{
							if (formObject.getNGValue("Product_Type").equalsIgnoreCase("Credit Card")){
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


					}
				}


				break;


			case VALUE_CHANGED:
				SKLogger.writeLog("RLOS VAL Change eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());


				if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_Customer_DOb")){
					SKLogger.writeLog("RLOS val change ", "Value of dob is:"+formObject.getNGValue("cmplx_Customer_DOb"));
					getAge(formObject.getNGValue("cmplx_Customer_DOb"),"cmplx_Customer_age");
				}	


				if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_Customer_Nationality"))
				{

					SKLogger.writeLog("RLOS val change ", "iNSIDE cmplx_Customer_Nationality CASE-->akShay "+formObject.getNGValue("cmplx_Customer_Nationality"));

					String  GCC="BH,IQ,KW,OM,QA,SA,AE";

					if(GCC.contains(formObject.getNGValue("cmplx_Customer_Nationality")))
					{
						SKLogger.writeLog("RLOS val change ","Inside GCC for Nationality");
						formObject.setNGValue("cmplx_Customer_GCCNational","Y");
					}
					else
					{
						formObject.setNGValue("cmplx_Customer_GCCNational","N");
					}
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("ReqProd")){
					ReqProd=formObject.getNGValue("ReqProd");
					SKLogger.writeLog("RLOS val change ", "Value of ReqProd is:"+ReqProd);
					loadPicklistProduct(ReqProd);
					if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1).equalsIgnoreCase("Credit Card")){
						formObject.setVisible("AlternateContactDetails_retainAccount",false);
					}
					else{
						formObject.setVisible("AlternateContactDetails_retainAccount",true);
					}
				}


				if (pEvent.getSource().getName().equalsIgnoreCase("SubProd")){
					SKLogger.writeLog("RLOS val change ", "Value of SubProd is:"+formObject.getNGValue("SubProd"));
					formObject.clear("AppType");

					LoadPickList("AppType", "select convert(varchar, description),code from ng_master_ApplicationType with (nolock) where subProdFlag='"+formObject.getNGValue("SubProd")+"' order by code");
					formObject.setNGValue("AppType","--Select--");

					String subprod=formObject.getNGValue("SubProd");
					String VIPFlag=formObject.getNGValue("cmplx_Customer_VIPFlag");
					String Nationality=formObject.getNGValue("cmplx_Customer_Nationality");
					String req_prod=formObject.getNGValue("ReqProd");
					String TypeOfProd=formObject.getNGValue("Product_type");
					SKLogger.writeLog("RLOS val change of card prod ", "Value of SubProd is:"+subprod);
					SKLogger.writeLog("RLOS val change ", "Value of vip is:"+VIPFlag);
					SKLogger.writeLog("RLOS val change ", "Value of Nationality is:"+Nationality);
					SKLogger.writeLog("RLOS val change ", "Value of req_prod is:"+req_prod);
					SKLogger.writeLog("RLOS val change ", "Value of TypeOfProd is:"+TypeOfProd);

					if (req_prod.equalsIgnoreCase("Credit Card")){
						if(subprod.equalsIgnoreCase("PU")||subprod.equalsIgnoreCase("PULI")||subprod.equalsIgnoreCase("LI"))
						{
							formObject.clear("CardProd");
							formObject.setNGValue("CardProd","--Select--");
							SKLogger.writeLog("subprod is not ", " is not BTC ,PA :"+subprod);
							if(TypeOfProd.equalsIgnoreCase("Conventional")){
								LoadPickList("CardProd","select convert(varchar, Description) from ng_master_cardProduct with (nolock) where reqproduct like '%conventional%'");
							}
							else{
								LoadPickList("CardProd","select convert(varchar, Description) from ng_master_cardProduct with (nolock) where reqproduct like '%Islamic%'");

							}
						}
						else{
							formObject.clear("CardProd");
							formObject.setNGValue("CardProd","--Select--");
							String query1="select convert(varchar, Description) from ng_master_cardProduct with (nolock) where Reqproduct='"+TypeOfProd+"' and SUBproduct = '"+subprod+"'  and VIPFlag='"+VIPFlag+"' and Nationality='AE'";	
							String query2="select convert(varchar, Description) from ng_master_cardProduct with (nolock) where Reqproduct='"+TypeOfProd+"' and SUBproduct = '"+subprod+"'  and VIPFlag='"+VIPFlag+"' and Nationality!='AE'";
							SKLogger.writeLog("Nationality ", ": "+Nationality);
							SKLogger.writeLog("Query For Card Product Dropdown: ",query1);
							if(Nationality.equalsIgnoreCase("AE")){
								SKLogger.writeLog("Query For Card Product Dropdown: ",query1);
								LoadPickList("CardProd",query1);
							}
							else{
								SKLogger.writeLog("Query For Card Product Dropdown: ",query2);
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
					SKLogger.writeLog("RLOS val change ", "Value of SubProd is:"+formObject.getNGValue("SubProd"));
					SKLogger.writeLog("RLOS val change ", "Value of AppType is:"+formObject.getNGValue("AppType"));
					SKLogger.writeLog("RLOS val change ", "Value of AppType is:"+formObject.getNGValue("Product_type"));


					if ((TypeofProduct.equalsIgnoreCase("Conventional"))&& subprod.equalsIgnoreCase("EXP")&&(apptype.equalsIgnoreCase("TKOE"))){


						SKLogger.writeLog("RLOS val change ", "1Value of AppType is:"+formObject.getNGValue("AppType"));


						formObject.clear("Scheme");
						formObject.setNGValue("Scheme","--Select--");
						LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Take Over'  and TypeofProduct='Conventional' order by SCHEMEID");




					}
					if ((TypeofProduct.equalsIgnoreCase("Conventional"))&& subprod.equalsIgnoreCase("EXP")&&(apptype.equalsIgnoreCase("NEWE"))){
						SKLogger.writeLog("RLOS val change ", "2Value of AppType is:"+formObject.getNGValue("AppType"));




						formObject.clear("Scheme");
						formObject.setNGValue("Scheme","--Select--");
						LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'New' and TypeofProduct='Conventional' order by SCHEMEID");




					}
					if ((TypeofProduct.equalsIgnoreCase("Conventional")&&subprod.equalsIgnoreCase("EXP"))&&(apptype.equalsIgnoreCase("TOPE"))){
						SKLogger.writeLog("RLOS val change ", "3Value of AppType is:"+formObject.getNGValue("AppType"));


						formObject.clear("Scheme");
						formObject.setNGValue("Scheme","--Select--");
						LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Top up' and TypeofProduct='Conventional' order by SCHEMEID");


					}
					if ((TypeofProduct.equalsIgnoreCase("Conventional")&&subprod.equalsIgnoreCase("EXP"))&&(apptype.equalsIgnoreCase("RESCE"))){
						SKLogger.writeLog("RLOS val change ", "4Value of AppType is:"+formObject.getNGValue("AppType"));


						formObject.clear("Scheme");
						formObject.setNGValue("Scheme","--Select--");
						LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Reschedulment' and TypeofProduct='Conventional' order by SCHEMEID");


					}
					if ((TypeofProduct.equalsIgnoreCase("Conventional")&& subprod.equalsIgnoreCase("NAT"))&&(apptype.equalsIgnoreCase("TOPN"))){
						SKLogger.writeLog("RLOS val change ", "5Value of AppType is:"+formObject.getNGValue("AppType"));
						formObject.clear("Scheme");
						formObject.setNGValue("Scheme","--Select--");
						LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Top up' and TypeofProduct='Conventional' order by SCHEMEID");


					}
					if ((TypeofProduct.equalsIgnoreCase("Conventional")&&subprod.equalsIgnoreCase("NAT"))&&(apptype.equalsIgnoreCase("TKON"))){
						SKLogger.writeLog("RLOS val change ", "6Value of AppType is:"+formObject.getNGValue("AppType"));
						formObject.clear("Scheme");
						formObject.setNGValue("Scheme","--Select--");
						LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Take Over' and TypeofProduct='Conventional' order by SCHEMEID");


					}
					if ((TypeofProduct.equalsIgnoreCase("Conventional")&&subprod.equalsIgnoreCase("NAT"))&&(apptype.equalsIgnoreCase("NEWN"))){
						SKLogger.writeLog("RLOS val change ", "7Value of AppType is:"+formObject.getNGValue("AppType"));
						formObject.clear("Scheme");
						formObject.setNGValue("Scheme","--Select--");
						LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'New' and TypeofProduct='Conventional' order by SCHEMEID");


					}
					if ((TypeofProduct.equalsIgnoreCase("Conventional")&&subprod.equalsIgnoreCase("NAT"))&&(apptype.equalsIgnoreCase("RESCN"))){
						SKLogger.writeLog("RLOS val change ", "8Value of AppType is:"+formObject.getNGValue("AppType"));
						formObject.clear("Scheme");
						formObject.setNGValue("Scheme","--Select--");
						LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Reschedulment' and TypeofProduct='Conventional' order by SCHEMEID");


					}
					if ((TypeofProduct.equalsIgnoreCase("Islamic"))&& subprod.equalsIgnoreCase("EXP")&&(apptype.equalsIgnoreCase("TKOE"))){


						SKLogger.writeLog("RLOS val change ", "1Value of AppType is:"+formObject.getNGValue("AppType"));


						formObject.clear("Scheme");
						formObject.setNGValue("Scheme","--Select--");
						LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Take Over'  and TypeofProduct='Islamic' order by SCHEMEID");				
					}

					if ((TypeofProduct.equalsIgnoreCase("Islamic"))&& subprod.equalsIgnoreCase("EXP")&&(apptype.equalsIgnoreCase("NEWE"))){
						SKLogger.writeLog("RLOS val change ", "2Value of AppType is:"+formObject.getNGValue("AppType"));
						formObject.clear("Scheme");
						formObject.setNGValue("Scheme","--Select--");
						LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'New' and TypeofProduct='Islamic' order by SCHEMEID");				
					}

					if ((TypeofProduct.equalsIgnoreCase("Islamic")&&subprod.equalsIgnoreCase("EXP"))&&(apptype.equalsIgnoreCase("TOPE"))){
						SKLogger.writeLog("RLOS val change ", "3Value of AppType is:"+formObject.getNGValue("AppType"));


						formObject.clear("Scheme");
						formObject.setNGValue("Scheme","--Select--");
						LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Top up' and TypeofProduct='Islamic' order by SCHEMEID");


					}
					if ((TypeofProduct.equalsIgnoreCase("Islamic")&&subprod.equalsIgnoreCase("EXP"))&&(apptype.equalsIgnoreCase("RESCE"))){
						SKLogger.writeLog("RLOS val change ", "4Value of AppType is:"+formObject.getNGValue("AppType"));


						formObject.clear("Scheme");
						formObject.setNGValue("Scheme","--Select--");
						LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Reschedulment' and TypeofProduct='Islamic' order by SCHEMEID");


					}
					if ((TypeofProduct.equalsIgnoreCase("Islamic")&& subprod.equalsIgnoreCase("NAT"))&&(apptype.equalsIgnoreCase("TOPN"))){
						SKLogger.writeLog("RLOS val change ", "5Value of AppType is:"+formObject.getNGValue("AppType"));
						formObject.clear("Scheme");
						formObject.setNGValue("Scheme","--Select--");
						LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Top up' and TypeofProduct='Islamic' order by SCHEMEID");


					}
					if ((TypeofProduct.equalsIgnoreCase("Islamic")&&subprod.equalsIgnoreCase("NAT"))&&(apptype.equalsIgnoreCase("TKON"))){
						SKLogger.writeLog("RLOS val change ", "6Value of AppType is:"+formObject.getNGValue("AppType"));
						formObject.clear("Scheme");
						formObject.setNGValue("Scheme","--Select--");
						LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Take Over' and TypeofProduct='Islamic' order by SCHEMEID");


					}
					if ((TypeofProduct.equalsIgnoreCase("Islamic")&&subprod.equalsIgnoreCase("NAT"))&&(apptype.equalsIgnoreCase("NEWN"))){
						SKLogger.writeLog("RLOS val change ", "7Value of AppType is:"+formObject.getNGValue("AppType"));
						formObject.clear("Scheme");
						formObject.setNGValue("Scheme","--Select--");
						LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'New' and TypeofProduct='Islamic' order by SCHEMEID");


					}
					if ((TypeofProduct.equalsIgnoreCase("Islamic")&&subprod.equalsIgnoreCase("NAT"))&&(apptype.equalsIgnoreCase("RESCN"))){
						SKLogger.writeLog("RLOS val change ", "8Value of AppType is:"+formObject.getNGValue("AppType"));
						formObject.clear("Scheme");
						formObject.setNGValue("Scheme","--Select--");
						LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Reschedulment' and TypeofProduct='Islamic' order by SCHEMEID");

					}
				}


				if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_EmploymentDetails_EmpIndusSector")){

					LoadPickList("cmplx_EmploymentDetails_Indus_Macro", "select '--Select--' union select convert(varchar, macro) from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where IndustrySector='"+formObject.getNGValue("cmplx_EmploymentDetails_EmpIndusSector")+"' and IsActive='Y'");
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_EmploymentDetails_Indus_Macro")){
					LoadPickList("cmplx_EmploymentDetails_Indus_Micro", "select '--Select--' union select convert(varchar, micro) from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where Macro='"+formObject.getNGValue("cmplx_EmploymentDetails_Indus_Macro")+"' and IsActive='Y'");
				}


				if (pEvent.getSource().getName().equalsIgnoreCase("AlternateContactDetails_carddispatch")){
					String cardDisp=formObject.getNGValue("AlternateContactDetails_carddispatch");
					SKLogger.writeLog("RLOS Value Change card disp",cardDisp);

					if (cardDisp.equalsIgnoreCase("Branch")){
						formObject.setVisible("AlternateContactDetails_custdomicile",true);
						LoadPickList("AlternateContactDetails_custdomicile", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock) order by code");					
					}

				}
				if (pEvent.getSource().getName().equalsIgnoreCase("Product_type")){

					String ProdType=formObject.getNGValue("Product_type");
					SKLogger.writeLog("RLOS Value Change Prod_Type",ProdType);
					formObject.clear("CardProd");
					formObject.setNGValue("CardProd","--Select--");
					if(ProdType.equalsIgnoreCase("Conventional"))
						LoadPickList("CardProd", "select '--Select--','' as code union select convert(varchar,description),code from ng_MASTER_CardProduct with (nolock) where code NOT like '%AMAL%' order by code");


					if(ProdType.equalsIgnoreCase("Islamic"))
						LoadPickList("CardProd", "select '--Select--','' as code union select convert(varchar,description),code from ng_MASTER_CardProduct with (nolock) where code like '%AMAL%' order by code");
				}

				// added by abhishek point 44
				if(pEvent.getSource().getName().equalsIgnoreCase("cmplx_EmploymentDetails_Others")){
					if(formObject.getNGValue("cmplx_EmploymentDetails_Others").equalsIgnoreCase("true")){
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
						SKLogger.writeLog("RLOS inside transfer mode change","");
						formObject.setLocked("cmplx_CC_Loan_mchequeno", false);
						formObject.setLocked("cmplx_CC_Loan_mchequeDate", false);
						formObject.setLocked("cmplx_CC_Loan_mchequestatus", false);
					}


				}*/

				break;
			default:
				break;

			}
		}

		finally{SKLogger.writeLog("RLOS", "Inside finally after try catch");}
	
	}	
	public void visibilityFrameIncomeDetails(FormReference formObject) {

		if(formObject.getNGValue("PrimaryProduct").equalsIgnoreCase("Credit Card") /*&& formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")*/){
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
		SKLogger.writeLog("RLOS", "Emp Type Value is:"+EmpType);

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
			formObject.setHeight("Incomedetails", 300);
			formObject.setHeight("IncomeDetails_Frame1", 280);
			if(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true") || formObject.getNGValue("cmplx_Customer_NEP").equals("true")){
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

	public String makeInputForGrid(String customerName) {
		String temp = "<ListItem><SubItem>"+customerName+"</SubItem>"
				+"<SubItem>Authorised signatory</SubItem>"
				+"<SubItem>Primary</SubItem>";

		for(int i=0;i<21;i++){
			temp+= "<SubItem></SubItem>";
		}

		temp+="</ListItem>";

		return temp;
	}

	/*public String Convert_dateFormat(String date,String Old_Format,String new_Format)
	{
		SKLogger.writeLog("RLOS Common ", "Inside Convert_dateFormat()"+date);
		String new_date="";
		if(date.equals(null) || date.equals(""))
		{
			return new_date;
		}

		try{
		SimpleDateFormat sdf_old=new SimpleDateFormat(Old_Format);
		SimpleDateFormat sdf_new=new SimpleDateFormat(new_Format);
		new_date=sdf_new.format(sdf_old.parse(date));
		}
		catch(Exception e)
		{
			SKLogger.writeLog("RLOS Common ", "Exception occurred in parsing date:"+printException(e));
		}
		return new_date;
	}*/

	public void saveFormStarted(FormEvent pEvent) 
	{
		SKLogger.writeLog("RLOS Initiation", "Inside saveFormStarted()" + pEvent);
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setNGValue("CIFID", formObject.getNGValue("cmplx_Customer_CIFNO"));
		SKLogger.writeLog("RLOS Initiation", "CIFID is:" +  formObject.getNGValue("cmplx_Customer_CIFNO"));
		formObject.setNGValue("Created_By",formObject.getUserName());
		formObject.setNGValue("PROCESS_NAME", "RLOS");
		formObject.setNGValue("initiationChannel", "Branch_Init");

		formObject.setNGValue("empType", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,6));
		formObject.setNGValue("priority_ProdGrid", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,9));
		formObject.setNGValue("Subproduct_productGrid", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,2));
		formObject.setNGValue("Employer_Name", formObject.getNGValue("cmplx_EmploymentDetails_EmpName"));//added By Akshay on 16/9/17 to set header
		formObject.setNGValue("Company_Name", formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",1,4));//added By Akshay on 16/9/17 to set header


		formObject.setNGValue("Age",formObject.getNGValue("cmplx_Customer_age"));

		
		formObject.setNGValue("NewApplicationNo", formObject.getWFFolderId());
	}

	public void saveFormCompleted(FormEvent pEvent) {
		SKLogger.writeLog("RLOS Initiation", "Inside saveFormCompleted()" + pEvent);


		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setNGValue("CIFID", formObject.getNGValue("cmplx_Customer_CIFNO"));
		SKLogger.writeLog("RLOS Initiation", "CIFID is:" +  formObject.getNGValue("cmplx_Customer_CIFNO"));
		formObject.setNGValue("Created_By",formObject.getUserName());
		formObject.setNGValue("PROCESS_NAME", "RLOS");
		formObject.setNGValue("initiationChannel", "Branch_Init");


		String squery = "select VAR_REC_1 from WFINSTRUMENTTABLE with (nolock) where ProcessInstanceID ='"+formObject.getWFWorkitemName()+"' ";
		List<List<String>> outputindex = null;
		outputindex = formObject.getNGDataFromDataCache(squery);
		SKLogger.writeLog("RLOS Initiation", "outputItemindex is:" +  outputindex);
		String itemIndex =outputindex.get(0).get(0);
		formObject.setNGValue("NewApplicationNo", itemIndex);
		formObject.setNGValue("ApplicationRefNo", itemIndex);


	}



	public void submitFormStarted(FormEvent pEvent)
			throws ValidatorException {
		SKLogger.writeLog("RLOS Initiation", "Inside submitFormStarted()" + pEvent);
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sessionID = FormContext.getCurrentInstance().getFormConfig().getConfigElement("DMSSessionId");
		String cabinetName = FormContext.getCurrentInstance().getFormConfig().getConfigElement("EngineName");

		formObject.setNGValue("loan_Type", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0));
		formObject.setNGValue("email_id", formObject.getUserName()+"@rlos.com");
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DecisionHistory_Decision"));

		/*	String squery = "select VAR_REC_1 from WFINSTRUMENTTABLE with (nolock) where ProcessInstanceID ='"+formObject.getWFWorkitemName()+"' ";
		List<List<String>> outputindex = null;
		List<List<String>> secondquery=null;
		outputindex = formObject.getNGDataFromDataCache(squery);
		SKLogger.writeLog("RLOS Initiation", "outputItemindex is:" +  outputindex);
		String itemIndex =outputindex.get(0).get(0);*/
		//added for document function
		int itemIndex=formObject.getWFFolderId();
		formObject.setNGValue("NewApplicationNo", itemIndex);
		formObject.setNGValue("ApplicationRefNo", itemIndex);
		IncomingDoc();
		SKLogger.writeLog("RLOS Initiation", "after calling incomingdoc function");
		//added for document function

		SKLogger.writeLog("RLOS Initiation", "Value Of decision is:"+formObject.getNGValue("cmplx_DecisionHistory_Decision"));

		saveIndecisionGrid();
		TypeOfProduct();//Loan type
		//CIFIDCheck();  
		//tanshu started
		/*		HashMap<String,String> hm1= new HashMap<String,String>();
		String requested_product="";
		String requested_subproduct="";
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		SKLogger.writeLog("INSIDE INCOMING DOCUMENT value_doc_name:" ,"valu of row count"+n);
		if(n>0)
		{
			for(int i=0;i<n;i++)
			{
				requested_product= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 1);
				SKLogger.writeLog("INSIDE INCOMING DOCUMENT requested_product:" ,requested_product);
				requested_subproduct= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2);
				SKLogger.writeLog("INSIDE INCOMING DOCUMENT requested_subproduct:" ,requested_subproduct);

			}
		}
		String sQuery="SELECT Name FROM PDBDocument with(nolock) WHERE DocumentIndex IN (SELECT DocumentIndex FROM PDBDocumentContent with(nolock) WHERE ParentFolderIndex= '"+itemIndex+"')";
		outputindex = null;
		SKLogger.writeLog("RLOS Initiation", "sQuery for document name is:" +  sQuery);
		outputindex = formObject.getNGDataFromDataCache(sQuery);
		SKLogger.writeLog("RLOS Initiation", "outputItemindex is:" +  outputindex);






		if(outputindex==null || outputindex.size()==0) {
			SKLogger.writeLog("","output index is blank");
			String  query = "SELECT distinct DocName,Mandatory FROM ng_rlos_DocTable WHERE ProductName='"+requested_product+"' and SubProductName='"+requested_subproduct+"' and ProcessName='RLOS'";
			SKLogger.writeLog("RLOS Initiation", "sQuery for document name is:" +  query);
			secondquery = formObject.getNGDataFromDataCache(query);
			for(int j = 0; j < secondquery.size(); j++) {
				if("Y".equalsIgnoreCase(secondquery.get(j).get(1))) {
					//throw new ValidatorException(new FacesMessage("You have not attached Mandatory Documents"));
				}
			}
		}



		String Document_Name =outputindex.get(0).get(0);
		SKLogger.writeLog("RLOS Initiation", "Document_Index Document_Name is:" + Document_Name);
		String[] arrval=new String[outputindex.size()];
		if(outputindex != null && outputindex.size() != 0)
		{
			System.out.println("Staff List "+outputindex);
			for(int i = 0; i < outputindex.size(); i++)
			{
				arrval[i]=outputindex.get(i).get(0);
				//str.append(outputindex.get(i).get(0));
				//str.append(",");
			}
		}
		//SKLogger.writeLog("RLOS Initiation", " sMap is:" +  str.toString());
		//String arr=str.substring(0, str.length()-1);
		//SKLogger.writeLog("RLOS Initiation", " arr is:" +  arr);

		//String[] arrval = arr.split(",");
		for(int k=0;k<arrval.length;k++)
		{
			SKLogger.writeLog("RLOS Initiation", " arrval is:" +  arrval[k]);
		}


		SKLogger.writeLog("INSIDE INCOMING DOCUMENT value_doc_name outsid for:" ,requested_product);
		String  query = "SELECT distinct DocName,Mandatory FROM ng_rlos_DocTable WHERE ProductName='"+requested_product+"' and SubProductName='"+requested_subproduct+"' and ProcessName='RLOS'";
		outputindex = null;
		SKLogger.writeLog("RLOS Initiation", "sQuery for document name is:" +  query);
		outputindex = formObject.getNGDataFromDataCache(query);
		SKLogger.writeLog("RLOS Initiation", "outputItemindex is:" +  outputindex+"::Size::"+outputindex.size());
		IRepeater repObj=null;
		repObj = formObject.getRepeaterControl("IncomingDoc_Frame");
		SKLogger.writeLog("RLOS Initiation","repObj::"+repObj+"row::+"+repObj.getRepeaterRowCount());
		String [] misseddoc=new String[outputindex.size()];
		for(int j = 0; j < outputindex.size(); j++)
		{
			String DocName =outputindex.get(j).get(0);
			String Mandatory =outputindex.get(j).get(1);
			SKLogger.writeLog("RLOS Initiation", "Document_Index Document_Name is:" + DocName+","+Mandatory);

			if (repObj.getRepeaterRowCount() != 0) {
				if(Mandatory.equalsIgnoreCase("Y")){

					int l=0;
					while(l<arrval.length)
					{
						SKLogger.writeLog("","DocName::"+DocName+":str:"+arrval[l]);

						if(arrval[l].equalsIgnoreCase(DocName))
						{
							SKLogger.writeLog("","document is present in the list");
							String StatusValue=repObj.getValue(j, "cmplx_DocName_Status");
							SKLogger.writeLog("","StatusValue::"+StatusValue);
							if(!StatusValue.equalsIgnoreCase("Recieved")){
								repObj.setValue(j, "cmplx_DocName_Status", "Recieved");
								repObj.setEditable(j, "cmplx_DocName_Status", false);
								SKLogger.writeLog("","StatusValue::123final"+StatusValue);

							}

							break;
						}
						else{
							SKLogger.writeLog("","Document is not present in the list");
							misseddoc[j]=DocName;
							l++;
							SKLogger.writeLog("RLOS Initiation", " misseddoc is in j is:" +  misseddoc[j]);

							String StatusValue=repObj.getValue(j, "cmplx_DocName_Status");
							SKLogger.writeLog("","StatusValue::"+StatusValue);
							String Remarks=repObj.getValue(j, "cmplx_DocName_Remarks");
							SKLogger.writeLog("","Remarks::"+Remarks);
							if(!(StatusValue.equalsIgnoreCase("Recieved")||StatusValue.equalsIgnoreCase("Deferred"))){
								if(Remarks.equalsIgnoreCase("")||Remarks.equalsIgnoreCase(null)||Remarks.equalsIgnoreCase("null")){
									SKLogger.writeLog("It is Mandatory to fill Remarks","As you have not attached the Mandatory Document fill the Remarks");
									throw new ValidatorException(new FacesMessage("As you have not attached the Mandatory Document fill the Remarks"));

								}
								else if(!Remarks.equalsIgnoreCase("")||Remarks.equalsIgnoreCase(null)||Remarks.equalsIgnoreCase("null")){
									SKLogger.writeLog("You may proceed further","Proceed further");


								}
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
			SKLogger.writeLog("RLOS Initiation", "misseddoc is:" +misseddoc[k]);
		}
		mandatoryDocName.setLength(Math.max(mandatoryDocName.length()-1,0));
		SKLogger.writeLog("RLOS Initiation", "misseddoc is:" +mandatoryDocName.toString());
		//throw new ValidatorException(new FacesMessage("You have not attached Mandatory Documents: "+mandatoryDocName.toString()));

		//tanshu ended
		 */


	}


	public void submitFormCompleted(FormEvent pEvent)
			throws ValidatorException {
		SKLogger.writeLog("RLOS Initiation", "Inside submitFormCompleted()" + pEvent);

	}

	public void continueExecution(String eventHandler, HashMap<String, String> m_mapParams) {
		SKLogger.writeLog("RLOS Initiation", "Inside continueExecution()" + eventHandler);
	}


	public void initialize() {
		SKLogger.writeLog("RLOS Initiation", "Inside initialize()");
	}

}

