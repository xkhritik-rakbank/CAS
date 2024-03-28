package com.newgen.omniforms.user;

import java.util.HashMap;

import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.excp.CustomExceptionHandler;
import com.newgen.omniforms.skutil.SKLogger;

public class RLOSCommonCode extends RLOSCommon{
	FormReference formObject = FormContext.getCurrentInstance().getFormReference();

	public void FrameExpandEvent(ComponentEvent pEvent){
		SKLogger.writeLog("RLOS","Inside RLOSCommoncode-->FrameExpandEvent()");
		if (pEvent.getSource().getName().equalsIgnoreCase("CustomerDetails")) 
		{

			formObject.fetchFragment("CustomerDetails", "Customer", "q_Customer");

		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails")) 
		{

			formObject.fetchFragment("GuarantorDetails", "GuarantorDetails", "q_GuarantorDetails");


		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("ProductDetailsLoader")) 
		{

			SKLogger.writeLog("RLOS", "Inside ProductDetailsLoader ");

			formObject.fetchFragment("ProductDetailsLoader", "Product", "q_Product");

		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("EmploymentDetails")) 
		{
			SKLogger.writeLog("RLOS", "Inside ProductDetailsLoader ");

			formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmploymentDetails");

		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("Incomedetails")) 
		{

			int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			for(int i=0;i<n;i++){
				SKLogger.writeLog("RLOS", "Grid Data[1][9] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9));
				if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1).equalsIgnoreCase("Credit Card") && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")){
					formObject.setVisible("IncomeDetails_Label12", true);
					formObject.setVisible("cmplx_IncomeDetails_StatementCycle", true);	
					formObject.setVisible("IncomeDetails_Label14", true);
					formObject.setVisible("IncomeDetails_Text5", true);//Statement Cycle	
				}	
				else{
					formObject.setVisible("IncomeDetails_Label12", false);
					formObject.setVisible("cmplx_IncomeDetails_StatementCycle", false);	
					formObject.setVisible("IncomeDetails_Label14", false);
					formObject.setVisible("IncomeDetails_Text5", false);	//Statement Cycle			
				}
			}

			if(n>0)
			{					
				String EmpType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6);
				SKLogger.writeLog("RLOS", "Emp Type Value is:"+EmpType);

				if(EmpType.equalsIgnoreCase("Salaried"))
				{

					formObject.fetchFragment("Incomedetails", "IncomeDetails", "q_IncomeDetails");
					formObject.setVisible("IncomeDetails_Frame3", false);
					formObject.setHeight("Incomedetails", 630);
					formObject.setHeight("IncomeDetails_Frame1", 605);	
					if(formObject.getNGValue("cmplx_Customer_NEP")=="true"){
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
					formObject.fetchFragment("Incomedetails", "IncomeDetails", "q_IncomeDetails");
					formObject.setVisible("IncomeDetails_Frame2", false);
					formObject.setTop("IncomeDetails_Frame3",1);
					formObject.setHeight("Incomedetails", 270);
					formObject.setHeight("IncomeDetails_Frame1", 240);
					if(formObject.getNGValue("cmplx_Customer_NEP")=="true"){
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
			else
				formObject.fetchFragment("Incomedetails", "IncomeDetails", "q_IncomeDetails");

		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails")) 
		{

			formObject.fetchFragment("CompanyDetails", "CompanyDetails", "q_CompanyDetails");
			SKLogger.writeLog("RLOS", "CompanyDetailse1:");


		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails")) 
		{

			formObject.fetchFragment("CardDetails", "CardDetails", "q_CardDetails");
			int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			for(int i=0;i<n;i++){
				if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1).equalsIgnoreCase("Personal Loan") && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")){
					formObject.setVisible("CardDetails_Label7", true);
					formObject.setVisible("CardDetails_Text5", true);	//Statement Cycle					
				}	
				else{
					formObject.setVisible("CardDetails_Label7", false);
					formObject.setVisible("CardDetails_Text5", false);		//Statement Cycle			
				}
			}	


		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails")) 
		{

			formObject.fetchFragment("PartnerDetails", "PartnerDetails", "q_PartnerDetails");


		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistoryContainer"))
		{

			formObject.fetchFragment("DecisionHistoryContainer", "DecisionHistory", "q_DecisionHistory");
			loadPicklist3();
			formObject.setNGValue("cmplx_DecisionHistory_Decision", "--Select--");
			formObject.setLocked("cmplx_DecisionHistory_DecisionReasonCode",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("MiscFields")) 
		{

			formObject.fetchFragment("MiscFields", "MiscellaneousFields", "q_MiscFields");


		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("EligibilityAndProductInformation"))
		{

			formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligAndProductInfo");
		}


		else if (pEvent.getSource().getName().equalsIgnoreCase("Liability_container"))
		{

			formObject.fetchFragment("Liability_container", "Liability_New", "q_LiabilityNew");
			Fields_Liabilities();

		}


		else if (pEvent.getSource().getName().equalsIgnoreCase("CC_Loan_container")) 
		{

			formObject.fetchFragment("CC_Loan_container", "CC_Loan", "q_CC_Loan");
			//Fields_ServiceRequest();


		}	


		else if (pEvent.getSource().getName().equalsIgnoreCase("Address_Details_container"))
		{

			formObject.fetchFragment("Address_Details_container", "AddressDetails", "q_AddressDetails");

		}	

		else if (pEvent.getSource().getName().equalsIgnoreCase("Alt_Contact_container")) 				
		{

			formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");

		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("Supplementary_Container")) 
		{
			formObject.fetchFragment("Supplementary_Container", "SupplementCardDetails", "q_SuppCardDetails");

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



		else if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistoryContainer"))
		{

			formObject.fetchFragment("DecisionHistoryContainer", "DecisionHistory", "q_DecisionHistory");

		}	


		else if (pEvent.getSource().getName().equalsIgnoreCase("IncomingDocuments"))
		{

			SKLogger.writeLog("RLOS StaffPortal Inside ","IncomingDocuments");
			formObject.fetchFragment("IncomingDocuments", "IncomingDoc", "q_IncomingDoc");
		}
	}


	public void DisableFragmentsOnLoad(ComponentEvent pEvent)//Except Decision history
	{
		SKLogger.writeLog("RLOS","Inside RLOSCommoncode-->DisableFragmentsOnLoad()");
		if (pEvent.getSource().getName().equalsIgnoreCase("Customer")) 
		{
			loadPicklistCustomer();
			formObject.setLocked("Customer_Frame1",true);

		}

		if (pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails")) 
		{
			formObject.setLocked("GuarantorDetails_Frame1",true);

		}

		if (pEvent.getSource().getName().equalsIgnoreCase("Product")) 
		{
			String ReqProd=formObject.getNGValue("ReqProd");
			loadPicklistProduct(ReqProd);
			formObject.setLocked("Product_Frame1",true);

		}

		if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) 
		{
			loadPicklist4()  ;
			formObject.setLocked("EMploymentDetails_Frame1",true);

		}

		if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails")) 
		{
			formObject.setLocked("IncomeDetails_Frame1",true);

		}

		if (pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails")) 
		{
			formObject.setLocked("CompanyDetails_Frame1",true);

		}

		if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails")) 
		{
			formObject.setLocked("CardDetails_Frame1",true);

		}

		if (pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails")) 
		{
			formObject.setLocked("PartnerDetails_Frame1",true);

		}

		if (pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails")) {
			loadPicklist_AuthSign();
			formObject.setLocked("AuthorisedSignDetails_Frame1", true);
		}

		if (pEvent.getSource().getName().equalsIgnoreCase("MiscellaneousFields")) 
		{
			formObject.setLocked("MiscellaneousFields_Frame1",true);

		}

		if (pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo")) 
		{
			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);

		}

		if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")) 
		{
			formObject.setLocked("Liability_New_Frame1",true);

		}

		if (pEvent.getSource().getName().equalsIgnoreCase("CC_Loan")) 
		{
			loadPicklist_ServiceRequest();
			formObject.setLocked("CC_Loan_Frame1",true);

		}
		if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails")) 
		{
			formObject.setLocked("AddressDetails_Frame1",true);

		}

		if (pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails")) 
		{

			formObject.setLocked("ReferenceDetails_Frame1",true);

		}


		if (pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails")) 
		{
			formObject.setLocked("AltContactDetails_Frame1",true);

		}
		if (pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails")) 
		{
			formObject.setLocked("SupplementCardDetails_Frame1",true);

		}
		if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")) 
		{
			formObject.setLocked("FATCA_Frame1",true);

		}
		if (pEvent.getSource().getName().equalsIgnoreCase("KYC")) 
		{
			formObject.setLocked("KYC_Frame1",true);

		}

		if (pEvent.getSource().getName().equalsIgnoreCase("OECD")) 
		{
			formObject.setLocked("OECD_Frame1",true);

		}

		if (pEvent.getSource().getName().equalsIgnoreCase("IncomingDoc"))
		{
			formObject.setLocked("IncomingDoc_Frame1",true);

		}

		if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) 
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
		//changed done to handel Exception in this method, Only try catch block is added along with some null pointer check
		try{
			formObject.fetchFragment("CustomerDetails", "Customer", "q_Customer");
			formObject.setNGFrameState("CustomerDetails", 0);

			String cardnotavailable=formObject.getNGValue("cmplx_Customer_CardNotAvailable");
			String MobNo=formObject.getNGValue("cmplx_Customer_MobNo");
			SKLogger.writeLog("RLOS", "MobNo"+MobNo);
			String OTPMobNo=formObject.getNGValue("OTP_Mobile_NO");
			SKLogger.writeLog("RLOS", "OTPMobNo"+OTPMobNo);
			if (OTPMobNo!=null && OTPMobNo.equalsIgnoreCase("")){
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
			if	(formObject.getNGValue("Is_Customer_Req").equalsIgnoreCase("Y")){
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
				SKLogger.writeLog("RLOS", "Value of customer_Req after locking all the fields");	
			}

			String nep=formObject.getNGValue("cmplx_Customer_NEP");
			String cifno=formObject.getNGValue("cmplx_Customer_card_id_available");
			if ((cardnotavailable.equalsIgnoreCase("true")||nep.equalsIgnoreCase("true"))&&cifno.equalsIgnoreCase("true")){
				formObject.setLocked("Customer_Button1", false);
			}
			else {
				formObject.setLocked("Customer_Button1", true);
			}	

		}
		catch(Exception e){
			SKLogger.writeLog("RLOS CommonCOde CustomerFragment_Load()", " Exception occured: "+printException(e));
		}

	}
}



