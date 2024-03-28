package com.newgen.omniforms.user;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.IRepeater;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.excp.CustomExceptionHandler;
import com.newgen.omniforms.listener.FormListener;
import com.newgen.omniforms.skutil.SKLogger;

import javax.faces.application.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import javax.faces.validator.ValidatorException;
import javax.print.PrintException;
@SuppressWarnings("serial")

public class RLOS_Query extends RLOSCommon implements FormListener
{
	HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
	boolean isSearchEmployer=false;
	String ReqProd=null;
	//System.out.println("Inside initiation RLOS");
	public void formLoaded(FormEvent pEvent)
	{
		System.out.println("Inside Query RLOS");
		SKLogger.writeLog("RLOS Query", "Inside formLoaded()" + pEvent.getSource().getName());
	}

	public void formPopulated(FormEvent pEvent) {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try{
			//formObject.fetchFragment("CustomerDetails", "Customer", "q_Customer");	
			//formObject.fetchFragment("ProductDetailsLoader", "Product", "q_Product");
			System.out.println("Inside Query RLOS");
			formObject.fetchFragment("CustomerDetails", "Customer", "q_Customer");
			formObject.setNGFrameState("CustomerDetails", 0);
			SKLogger.writeLog("RLOS Initiation", "Inside formPopulated()" + pEvent.getSource());
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			String currDate = format.format(Calendar.getInstance().getTime());
			SKLogger.writeLog("RLOS Query", "currTime:" + currDate);
			formObject.setNGValue("Intro_Date",currDate);
			formObject.setNGValue("WIname",formObject.getWFWorkitemName());
			formObject.setNGValue("Channel_Name","Branch Initiation");
			formObject.setNGValue("Created_By",formObject.getUserName());

			// formObject.setNGValue("Intro_Date",formObject.getNGValue("Created_Date"));
			formObject.setNGValue("initiationChannel","Branch_Init");
			String init_Channel=formObject.getNGValue("initiationChannel");
			if(init_Channel.equals(""))
				formObject.setNGValue("initiationChannel","Branch_Init");
			if(formObject.getNGValue("empType").contains("Salaried"))
				formObject.setSheetVisible("ParentTab",1, false);
			else if(formObject.getNGValue("empType").contains("Self Employed"))
				formObject.setSheetVisible("ParentTab",3, false);
			//added By Akshay-23/7/2017
			//new RLOSCommonCode().CustomerFragment_Load();
			/*formObject.fetchFragment("ProductDetailsLoader", "Product", "q_Product");
			formObject.setNGFrameState("ProductDetailsLoader", 0);
			*///ended By Akshay-23/7/2017

			if(formObject.getNGValue("Product_Type").contains("Personal Loan") && Integer.parseInt(formObject.getNGValue("Age"))<18)
			{
				formObject.setVisible("GuarantorDetails", true);
				formObject.setTop("Incomedetails",110);
			}
			if(formObject.getNGValue("PrimaryProduct").equals("Credit Card"))
				formObject.setSheetVisible("ParentTab",7, true);

			SKLogger.writeLog("RLOS","Value Of Init Channel:"+init_Channel);
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
					formObject.setEnabled("GuarantorDetails",false);
					formObject.fetchFragment("GuarantorDetails", "GuarantorDetails", "q_GuarantorDetails");
				}


				else if (pEvent.getSource().getName().equalsIgnoreCase("ProductDetailsLoader")) {

					SKLogger.writeLog("RLOS", "Inside ProductDetailsLoader ");	
					//added Tanshu Aggarwal(23/06/2017)
					formObject.fetchFragment("ProductDetailsLoader", "Product", "q_Product");
					formObject.setNGFrameState("ProductDetailsLoader", 0);
						formObject.setEnabled("Product_Frame1", false);
						SKLogger.writeLog("RLOS", "Inside ProductDetailsLoader if ");	
					formObject.setEnabled("ProductDetailsLoader",false);

				}


				else if (pEvent.getSource().getName().equalsIgnoreCase("EmploymentDetails")) 
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

				else if (pEvent.getSource().getName().equalsIgnoreCase("Incomedetails")) 
				{
					formObject.setEnabled("Incomedetails",false);
					formObject.fetchFragment("Incomedetails", "IncomeDetails", "q_IncomeDetails");

					//int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
					//for(int i=0;i<n;i++){
					//SKLogger.writeLog("RLOS", "Grid Data[1][9] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9));
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
					//}

					//IMFields_Income();
					//BTCFields_Income();
					//LimitIncreaseFields_Income();
					//ProductUpgrade_Income();


				}				else if (pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails")) {
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
					formObject.setEnabled("CompanyDetails",false);
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
					formObject.setEnabled("AuthorisedSignatoryDetails",false);
					formObject.fetchFragment("AuthorisedSignatoryDetails", "AuthorisedSignDetails", "q_AuthorisedSignDetails");
					SKLogger.writeLog("RLOS", "AuthorisedSignatoryDetails:");




				}
				else if (pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails")) 
				{
					SKLogger.writeLog("RLOS", "Saurabh123456");
					formObject.setEnabled("PartnerDetails",false);
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
					formObject.setEnabled("DecisionHistoryContainer",false);
					formObject.fetchFragment("DecisionHistoryContainer", "DecisionHistory", "q_DecisionHistory");
				
					//25/07/2017  for mandatory checks fragments should be fetched first
					
					int framestate0 = formObject.getNGFrameState("ProductDetailsLoader");
				if(framestate0 == 0){
					SKLogger.writeLog("RLOS count of current account not NTB","ProductDetailsLoader");
				}
				else {
					formObject.setEnabled("ProductDetailsLoader",false);
					formObject.fetchFragment("ProductDetailsLoader", "Product", "q_Product");
					SKLogger.writeLog("RLOS count of current account not NTB","fetched product details");
					
				}
				
				
					int framestate1=formObject.getNGFrameState("Incomedetails");
					if(framestate1 == 0){
						SKLogger.writeLog("RLOS count of current account not NTB","Incomedetails");
					}
					else {
						formObject.setEnabled("Incomedetails",false);
						formObject.fetchFragment("Incomedetails", "IncomeDetails", "q_IncomeDetails");
						SKLogger.writeLog("RLOS count of current account not NTB","fetched income details");
						formObject.setTop("Incomedetails",formObject.getTop("ProductDetailsLoader")+formObject.getHeight("ProductDetailsLoader"));
					}
					
					int framestate2=formObject.getNGFrameState("EmploymentDetails");
					if(framestate2 == 0){
						SKLogger.writeLog("RLOS count of current account not NTB","EmploymentDetails");
					}
					else {
						formObject.setEnabled("EmploymentDetails",false);
						formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmploymentDetails");
						SKLogger.writeLog("RLOS count of current account not NTB","fetched employment details");
					}
					int framestate3=formObject.getNGFrameState("Address_Details_container");
					if(framestate3 == 0){
						SKLogger.writeLog("RLOS count of current account not NTB","Address_Details_container");
					}
					else {
						formObject.setEnabled("Address_Details_container",false);
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
						formObject.setEnabled("ReferenceDetails",false);
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
					formObject.setEnabled("Alt_Contact_container",false);
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
						formObject.setEnabled("KYC",false);
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
						formObject.setEnabled("OECD",false);
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
					
				}


				else 	if (pEvent.getSource().getName().equalsIgnoreCase("MiscFields")) 
				{
					formObject.setEnabled("MiscFields",false);
					formObject.fetchFragment("MiscFields", "MiscellaneousFields", "q_MiscFields");	


				}	


				else if (pEvent.getSource().getName().equalsIgnoreCase("EligibilityAndProductInformation"))
				{
					formObject.setEnabled("EligibilityAndProductInformation",false);
					formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligAndProductInfo");
					formObject.setNGFrameState("EligibilityAndProductInformation", 0);
					SKLogger.writeLog("RLOS value of Reason","Saurabh ELPINFO,Framexpanded");
					//Fields_Eligibility();
					//Fields_ApplicationType_Eligibility();
					formObject.setEnabled("cmplx_EligibilityAndProductInfo_FinalInterestRate", false);

					SKLogger.writeLog("RLOS value of Reason","Eligibility grid");

					if(formObject.getNGValue("PrimaryProduct").equalsIgnoreCase("Personal Loan")/* && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")*/){
						formObject.setVisible("ELigibiltyAndProductInfo_Frame2", true);//Funding Account Number Grid
						formObject.setVisible("ELigibiltyAndProductInfo_Frame4", true);//Personal Loan
						formObject.setVisible("ELigibiltyAndProductInfo_Frame3", false);//Lein Details
						SKLogger.writeLog("RLOS", "Funding Account Details now Visible...!!!");


					}

					else if(formObject.getNGValue("PrimaryProduct").equalsIgnoreCase("Credit Card")/* && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")*/){

						formObject.setVisible("ELigibiltyAndProductInfo_Frame2", false);//Funding Account Number Grid
						formObject.setVisible("ELigibiltyAndProductInfo_Frame4", false);//Personal Loan
						formObject.setVisible("ELigibiltyAndProductInfo_Frame3", false);//Lein Details
						formObject.setTop("ELigibiltyAndProductInfo_Frame5",formObject.getTop("ELigibiltyAndProductInfo_Frame1")+5);//CC
						formObject.setTop("ELigibiltyAndProductInfo_Frame6",formObject.getTop("ELigibiltyAndProductInfo_Frame5")+formObject.getHeight("ELigibiltyAndProductInfo_Frame5")+10);//Eligible For Card

						if(formObject.getNGValue("CardProduct_Primary").toUpperCase().contains("-SEC") /*&& formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")*/){
							formObject.setVisible("ELigibiltyAndProductInfo_Frame3", true);//Lein Details
							formObject.setTop("ELigibiltyAndProductInfo_Frame3",formObject.getTop("ELigibiltyAndProductInfo_Frame6")+formObject.getHeight("ELigibiltyAndProductInfo_Frame6")+10);
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
					formObject.setEnabled("Liability_container",false);				
                    formObject.fetchFragment("Liability_container", "Liability_New", "q_LiabilityNew");              
                }


				else if (pEvent.getSource().getName().equalsIgnoreCase("CC_Loan_container")) 
				{
				formObject.setEnabled("CC_Loan_container",false);	
					formObject.fetchFragment("CC_Loan_container", "CC_Loan", "q_CC_Loan");
				}	


				else if (pEvent.getSource().getName().equalsIgnoreCase("Address_Details_container"))

				{	
				formObject.setEnabled("Address_Details_container",false);	
					formObject.fetchFragment("Address_Details_container", "AddressDetails", "q_AddressDetails");	
				}	


				else if (pEvent.getSource().getName().equalsIgnoreCase("Alt_Contact_container")) 				
				{

					formObject.setEnabled("Alt_Contact_container",false);	
					formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");


				}



				/*else if (pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails")) {       	


					formObject.fetchFragment("ReferenceDetails", "ReferenceDetails", "q_ReferenceDetails");
					
				}*/


				else if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails")) 
				{


					formObject.setEnabled("CardDetails",false);	
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

					formObject.setEnabled("Supplementary_Container",false);	
					formObject.fetchFragment("Supplementary_Container", "SupplementCardDetails", "q_SuppCardDetails");


					if (formObject.getNGValue("empType").equalsIgnoreCase("Salaried pensioner")){
						formObject.setVisible("CompEmbName", false);
						formObject.setVisible("SupplementCardDetails_Label7", false);
					}
				}	


				else if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")) 
				{

					formObject.setEnabled("FATCA",false);	
					formObject.fetchFragment("FATCA", "FATCA", "q_FATCA");


				}	


				else if (pEvent.getSource().getName().equalsIgnoreCase("KYC")) 
				{

					formObject.setEnabled("KYC",false);
					formObject.fetchFragment("KYC", "KYC", "q_KYC");


				}	


				else if (pEvent.getSource().getName().equalsIgnoreCase("OECD")) 
				{

					formObject.setEnabled("OECD",false);
					formObject.fetchFragment("OECD", "OECD", "q_OECD");

				}	






				else if (pEvent.getSource().getName().equalsIgnoreCase("CustomerDocumentsContainer"))
				{
					formObject.setEnabled("CustomerDocumentsContainer",false);
					formObject.fetchFragment("CustomerDocumentsContainer", "CustomerDocument", "q_CustomerDocument");

				}	


				else if (pEvent.getSource().getName().equalsIgnoreCase("OutgoingDocumentsContainer"))
				{

					formObject.setEnabled("OutgoingDocumentsContainer",false);
					formObject.fetchFragment("OutgoingDocumentsContainer", "CorrespondenceDocument", "q_correspondenceDocument");
				}	




				else if (pEvent.getSource().getName().equalsIgnoreCase("DecisioningFields"))
				{
					formObject.setEnabled("DecisioningFields",false);
					formObject.fetchFragment("DecisioningFields", "DecisionFields", "q_DecisionFields");


				}	



				else if (pEvent.getSource().getName().equalsIgnoreCase("DeviationHistoryContainer"))
				{
					formObject.setEnabled("DeviationHistoryContainer",false);
					formObject.fetchFragment("DeviationHistoryContainer", "DeviationHistory", "q_DeviationHistory");


				}	


				else  if (pEvent.getSource().getName().equalsIgnoreCase("IncomingDocuments"))

				{
					formObject.setEnabled("IncomingDocuments",false);
					int framestate0 = formObject.getNGFrameState("ProductDetailsLoader");
					if(framestate0 == 0){
						SKLogger.writeLog("RLOS count of current account not NTB","ProductDetailsLoader");
					}
					else {
					formObject.setEnabled("ProductDetailsLoader",false);
						formObject.fetchFragment("ProductDetailsLoader", "Product", "q_Product");
						SKLogger.writeLog("RLOS count of current account not NTB","fetched product details");
						formObject.setTop("Incomedetails",formObject.getTop("ProductDetailsLoader")+formObject.getHeight("ProductDetailsLoader"));
					}
					
					
						int framestate1=formObject.getNGFrameState("Incomedetails");
						if(framestate1 == 0){
							SKLogger.writeLog("RLOS count of current account not NTB","Incomedetails");
						}
						else {
						formObject.setEnabled("Incomedetails",false);
							formObject.fetchFragment("Incomedetails", "IncomeDetails", "q_IncomeDetails");
							SKLogger.writeLog("RLOS count of current account not NTB","fetched income details");
							formObject.setTop("Incomedetails",formObject.getTop("ProductDetailsLoader")+formObject.getHeight("ProductDetailsLoader"));
						}
					
					

					SKLogger.writeLog("RLOS Initiation Inside ","IncomingDocuments");
					formObject.setEnabled("IncomingDocuments",false);
					formObject.fetchFragment("IncomingDocuments", "IncomingDoc", "q_IncomingDoc");
					SKLogger.writeLog("RLOS Initiation Inside ","fetchIncomingDocRepeater");
					fetchIncomingDocRepeater();
					SKLogger.writeLog("RLOS Initiation eventDispatched()","formObject.fetchFragment1");
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("DeferralDocuments")) 

				{
					formObject.setEnabled("DeferralDocuments",false);
					SKLogger.writeLog("RLOS Initiation eventDispatched()","DeferralDocuments");
					formObject.fetchFragment("DeferralDocuments", "DeferralDocName", "q_DeferralDoc");
					loadAllCombo_RLOS_Documents_Deferral();
					SKLogger.writeLog("RLOS Initiation eventDispatched()","formObject.fetchFragment2");



				}

				break;


			case FRAME_COLLAPSED: {
				break;
			}

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

						 //change by saurabh on 9th July 17.
	                    if(formObject.getNGValue("cmplx_Liability_New_AECBconsentAvail").equalsIgnoreCase("true")){
	                          formObject.setLocked("Liability_New_fetchLiabilities",false);
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

						formObject.setLocked("Liability_New_fetchLiabilities",true);
						formObject.setLocked("takeoverAMount",true);
						formObject.setLocked("cmplx_Liability_New_DBR",true);
						formObject.setLocked("cmplx_Liability_New_DBRNet",true);
						formObject.setLocked("cmplx_Liability_New_AggrExposure",true);
						formObject.setLocked("cmplx_Liability_New_TAI",true);
					}


					else if (pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails")) {
						SKLogger.writeLog("RLOS","AKSHAY COMPANY FRAGMENT LOADED");
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

					else if (pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails")) {
						formObject.setLocked("CIFNo", true);
						formObject.setLocked("AuthorisedSignDetails_Button4", true);
						formObject.setNGValue("SignStatus", "--Select--");
						LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_Country with (nolock) order by code");
						formObject.setNGValue("AuthorisedSignDetails_nationality", "");
						LoadPickList("SignStatus", "select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");
					}

					else if (pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails")) {
						formObject.setNGValue("PartnerDetails_nationality", "--Select--");
						LoadPickList("PartnerDetails_nationality", "select convert(varchar, description) from NG_MASTER_Country with (nolock)");
					}


					else if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) {

						String empid="AVI,MED,EDU,HOT,PROM";	
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
						loadPicklist4();
						Fields_ApplicationType_Employment();

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
						formObject.setVisible("AlternateContactDetails_custdomicile",false);
						formObject.setVisible("AltContactDetails_Label14",false);
						SKLogger.writeLog("RLOS","AMasd COMPANY FRAGMENT LOADED"+formObject.getNGValue("Product_Type"));
						if(formObject.getNGValue("Product_Type").equalsIgnoreCase("Credit Card")){
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
					}

					if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
						loadPicklist3();
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

						formObject.setNGValue("cmplx_DecisionHistory_Decision", "--Select--");
						
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
				}


				if (pEvent.getSource().getName().equalsIgnoreCase("SubProd")){
					SKLogger.writeLog("RLOS val change ", "Value of SubProd is:"+formObject.getNGValue("SubProd"));
					formObject.clear("AppType");

					LoadPickList("AppType", "select convert(text, description),code from ng_master_ApplicationType with (nolock) where subProdFlag='"+formObject.getNGValue("SubProd")+"' order by code");
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
						if(subprod.equalsIgnoreCase("PU")||subprod.equalsIgnoreCase("IM")||subprod.equalsIgnoreCase("PULI")||subprod.equalsIgnoreCase("LI"))
						{
							formObject.clear("CardProd");
							formObject.setNGValue("CardProd","--Select--");
							SKLogger.writeLog("subprod is not ", " is not BTC ,PA :"+subprod);
							LoadPickList("CardProd","select convert(varchar, Description) from ng_MASTER_CardProduct with (nolock) where code like '%AMAL%'");

						}
						else{
							formObject.clear("CardProd");
							formObject.setNGValue("CardProd","--Select--");
							String query1="select convert(varchar, Description) from ng_MASTER_CardProduct with (nolock) where Reqproduct='"+TypeOfProd+"' and SUBproduct = '"+subprod+"'  and VIPFlag='"+VIPFlag+"' and Nationality='AE'";	
							String query2="select convert(varchar, Description) from ng_MASTER_CardProduct with (nolock) where Reqproduct='"+TypeOfProd+"' and SUBproduct = '"+subprod+"'  and VIPFlag='"+VIPFlag+"' and Nationality!='AE'";
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
						LoadPickList("Scheme", "select convert(varchar, SCHEMEDESC) from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Take Over'  and TypeofProduct='Conventional'");




					}
					if ((TypeofProduct.equalsIgnoreCase("Conventional"))&& subprod.equalsIgnoreCase("EXP")&&(apptype.equalsIgnoreCase("NEWE"))){
						SKLogger.writeLog("RLOS val change ", "2Value of AppType is:"+formObject.getNGValue("AppType"));




						formObject.clear("Scheme");
						formObject.setNGValue("Scheme","--Select--");
						LoadPickList("Scheme", "select convert(varchar, SCHEMEDESC) from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'New' and TypeofProduct='Conventional'");




					}
					if ((TypeofProduct.equalsIgnoreCase("Conventional")&&subprod.equalsIgnoreCase("EXP"))&&(apptype.equalsIgnoreCase("TOPE"))){
						SKLogger.writeLog("RLOS val change ", "3Value of AppType is:"+formObject.getNGValue("AppType"));


						formObject.clear("Scheme");
						formObject.setNGValue("Scheme","--Select--");
						LoadPickList("Scheme", "select convert(varchar, SCHEMEDESC) from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Top up' and TypeofProduct='Conventional'");


					}
					if ((TypeofProduct.equalsIgnoreCase("Conventional")&&subprod.equalsIgnoreCase("EXP"))&&(apptype.equalsIgnoreCase("RESCE"))){
						SKLogger.writeLog("RLOS val change ", "4Value of AppType is:"+formObject.getNGValue("AppType"));


						formObject.clear("Scheme");
						formObject.setNGValue("Scheme","--Select--");
						LoadPickList("Scheme", "select convert(varchar, SCHEMEDESC) from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Reschedulment' and TypeofProduct='Conventional'");


					}
					if ((TypeofProduct.equalsIgnoreCase("Conventional")&& subprod.equalsIgnoreCase("NAT"))&&(apptype.equalsIgnoreCase("TOPN"))){
						SKLogger.writeLog("RLOS val change ", "5Value of AppType is:"+formObject.getNGValue("AppType"));
						formObject.clear("Scheme");
						formObject.setNGValue("Scheme","--Select--");
						LoadPickList("Scheme", "select convert(varchar, SCHEMEDESC) from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Top up' and TypeofProduct='Conventional'");


					}
					if ((TypeofProduct.equalsIgnoreCase("Conventional")&&subprod.equalsIgnoreCase("NAT"))&&(apptype.equalsIgnoreCase("TKON"))){
						SKLogger.writeLog("RLOS val change ", "6Value of AppType is:"+formObject.getNGValue("AppType"));
						formObject.clear("Scheme");
						formObject.setNGValue("Scheme","--Select--");
						LoadPickList("Scheme", "select convert(varchar, SCHEMEDESC) from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Take Over' and TypeofProduct='Conventional'");


					}
					if ((TypeofProduct.equalsIgnoreCase("Conventional")&&subprod.equalsIgnoreCase("NAT"))&&(apptype.equalsIgnoreCase("NEWN"))){
						SKLogger.writeLog("RLOS val change ", "7Value of AppType is:"+formObject.getNGValue("AppType"));
						formObject.clear("Scheme");
						formObject.setNGValue("Scheme","--Select--");
						LoadPickList("Scheme", "select convert(varchar, SCHEMEDESC) from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'New' and TypeofProduct='Conventional'");


					}
					if ((TypeofProduct.equalsIgnoreCase("Conventional")&&subprod.equalsIgnoreCase("NAT"))&&(apptype.equalsIgnoreCase("RESCN"))){
						SKLogger.writeLog("RLOS val change ", "8Value of AppType is:"+formObject.getNGValue("AppType"));
						formObject.clear("Scheme");
						formObject.setNGValue("Scheme","--Select--");
						LoadPickList("Scheme", "select convert(varchar, SCHEMEDESC) from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Reschedulment' and TypeofProduct='Conventional'");


					}
					if ((TypeofProduct.equalsIgnoreCase("Islamic"))&& subprod.equalsIgnoreCase("EXP")&&(apptype.equalsIgnoreCase("TKOE"))){


						SKLogger.writeLog("RLOS val change ", "1Value of AppType is:"+formObject.getNGValue("AppType"));


						formObject.clear("Scheme");
						formObject.setNGValue("Scheme","--Select--");
						LoadPickList("Scheme", "select convert(varchar, SCHEMEDESC) from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Take Over'  and TypeofProduct='Islamic'");				
					}

					if ((TypeofProduct.equalsIgnoreCase("Islamic"))&& subprod.equalsIgnoreCase("EXP")&&(apptype.equalsIgnoreCase("NEWE"))){
						SKLogger.writeLog("RLOS val change ", "2Value of AppType is:"+formObject.getNGValue("AppType"));
						formObject.clear("Scheme");
						formObject.setNGValue("Scheme","--Select--");
						LoadPickList("Scheme", "select convert(varchar, SCHEMEDESC) from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'New' and TypeofProduct='Islamic'");				
					}

					if ((TypeofProduct.equalsIgnoreCase("Islamic")&&subprod.equalsIgnoreCase("EXP"))&&(apptype.equalsIgnoreCase("TOPE"))){
						SKLogger.writeLog("RLOS val change ", "3Value of AppType is:"+formObject.getNGValue("AppType"));


						formObject.clear("Scheme");
						formObject.setNGValue("Scheme","--Select--");
						LoadPickList("Scheme", "select convert(varchar, SCHEMEDESC) from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Top up' and TypeofProduct='Islamic'");


					}
					if ((TypeofProduct.equalsIgnoreCase("Islamic")&&subprod.equalsIgnoreCase("EXP"))&&(apptype.equalsIgnoreCase("RESCE"))){
						SKLogger.writeLog("RLOS val change ", "4Value of AppType is:"+formObject.getNGValue("AppType"));


						formObject.clear("Scheme");
						formObject.setNGValue("Scheme","--Select--");
						LoadPickList("Scheme", "select convert(varchar, SCHEMEDESC) from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Reschedulment' and TypeofProduct='Islamic'");


					}
					if ((TypeofProduct.equalsIgnoreCase("Islamic")&& subprod.equalsIgnoreCase("NAT"))&&(apptype.equalsIgnoreCase("TOPN"))){
						SKLogger.writeLog("RLOS val change ", "5Value of AppType is:"+formObject.getNGValue("AppType"));
						formObject.clear("Scheme");
						formObject.setNGValue("Scheme","--Select--");
						LoadPickList("Scheme", "select convert(varchar, SCHEMEDESC) from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Top up' and TypeofProduct='Islamic'");


					}
					if ((TypeofProduct.equalsIgnoreCase("Islamic")&&subprod.equalsIgnoreCase("NAT"))&&(apptype.equalsIgnoreCase("TKON"))){
						SKLogger.writeLog("RLOS val change ", "6Value of AppType is:"+formObject.getNGValue("AppType"));
						formObject.clear("Scheme");
						formObject.setNGValue("Scheme","--Select--");
						LoadPickList("Scheme", "select convert(varchar, SCHEMEDESC) from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Take Over' and TypeofProduct='Islamic'");


					}
					if ((TypeofProduct.equalsIgnoreCase("Islamic")&&subprod.equalsIgnoreCase("NAT"))&&(apptype.equalsIgnoreCase("NEWN"))){
						SKLogger.writeLog("RLOS val change ", "7Value of AppType is:"+formObject.getNGValue("AppType"));
						formObject.clear("Scheme");
						formObject.setNGValue("Scheme","--Select--");
						LoadPickList("Scheme", "select convert(varchar, SCHEMEDESC) from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'New' and TypeofProduct='Islamic'");


					}
					if ((TypeofProduct.equalsIgnoreCase("Islamic")&&subprod.equalsIgnoreCase("NAT"))&&(apptype.equalsIgnoreCase("RESCN"))){
						SKLogger.writeLog("RLOS val change ", "8Value of AppType is:"+formObject.getNGValue("AppType"));
						formObject.clear("Scheme");
						formObject.setNGValue("Scheme","--Select--");
						LoadPickList("Scheme", "select convert(varchar, SCHEMEDESC) from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Reschedulment' and TypeofProduct='Islamic'");

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
						LoadPickList("CardProd", "select '--Select--','' as code union select convert(varchar,description),code from ng_MASTER_CardProduct with (nolock) where code not like '%AMAL%' order by code");


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
		
		finally{SKLogger.writeLog("RLOS", "Exception occurred");}
		/*catch(Exception ex)
		{
			SKLogger.writeLog("RLOS Initiation","Inside Exception to show msg at front end");
			HashMap<String,String> hm1=new HashMap<String,String>();
			hm1.put("Error","Checked");
			if(ex instanceof ValidatorException)
			{   SKLogger.writeLog("RLOS Initiation","popupFlag value: "+ popupFlag);
			if(popupFlag.equalsIgnoreCase("Y"))

			{
				SKLogger.writeLog("RLOS Initiation","Inside popup msg through Exception "+ popupFlag);
				if(popUpControl.equals(""))

				{
					SKLogger.writeLog("PL DDVY maker","Before show Exception at front End "+ popupFlag);
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
	public String Convert_dateFormat(String date,String Old_Format,String new_Format)
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
	}

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


		formObject.setNGValue("Age",formObject.getNGValue("cmplx_Customer_age"));

		String squery = "select VAR_REC_1 from WFINSTRUMENTTABLE with (nolock) where ProcessInstanceID ='"+formObject.getWFWorkitemName()+"' ";
		List<List<String>> outputindex = null;
		outputindex = formObject.getNGDataFromDataCache(squery);
		SKLogger.writeLog("RLOS Initiation", "outputItemindex is:" +  outputindex);
		String itemIndex =outputindex.get(0).get(0);
		formObject.setNGValue("NewApplicationNo", itemIndex);
		formObject.setNGValue("ApplicationRefNo", itemIndex);
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

		String squery = "select VAR_REC_1 from WFINSTRUMENTTABLE with (nolock) where ProcessInstanceID ='"+formObject.getWFWorkitemName()+"' ";
		List<List<String>> outputindex = null;
		List<List<String>> secondquery=null;
		outputindex = formObject.getNGDataFromDataCache(squery);
		SKLogger.writeLog("RLOS Initiation", "outputItemindex is:" +  outputindex);
		String itemIndex =outputindex.get(0).get(0);
		formObject.setNGValue("NewApplicationNo", itemIndex);
		formObject.setNGValue("ApplicationRefNo", itemIndex);




		SKLogger.writeLog("RLOS Initiation", "Value Of decision is:"+formObject.getNGValue("cmplx_DecisionHistory_Decision"));
		
		saveIndecisionGrid();
		TypeOfProduct();//Loan type
		//CIFIDCheck();  
		//tanshu started
		HashMap<String,String> hm1= new HashMap<String,String>();
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

	public String decrypt(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String encrypt(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}

