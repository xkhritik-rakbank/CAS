package com.newgen.omniforms.user;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.IRepeater;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.excp.CustomExceptionHandler;
import com.newgen.omniforms.listener.FormListener;
import com.newgen.omniforms.skutil.SKLogger;
import com.newgen.omniforms.skutil.AesUtil;

import javax.faces.application.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.validator.ValidatorException;
import javax.swing.JOptionPane;
@SuppressWarnings("serial")

public class RLOS_Initiation extends RLOSCommon implements FormListener
{
	HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
	boolean isSearchEmployer=false;
	String ReqProd=null;
	String CallFired="";
	
	//System.out.println("Inside initiation RLOS");
	public void formLoaded(FormEvent pEvent)
	{
		System.out.println("Inside initiation RLOS");
		SKLogger.writeLog("RLOS Initiation", "Inside formLoaded()" + pEvent.getSource().getName());

	}


	public void formPopulated(FormEvent pEvent) {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try{
			formObject.fetchFragment("CustomerDetails", "Customer", "q_Customer");	
			formObject.fetchFragment("ProductDetailsLoader", "Product", "q_Product");
			System.out.println("Inside initiation RLOS");
			SKLogger.writeLog("RLOS Initiation", "Inside formPopulated()" + pEvent.getSource());
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			String currDate = format.format(Calendar.getInstance().getTime());
			SKLogger.writeLog("RLOS Initiation", "currTime:" + currDate);
			formObject.setNGValue("Intro_Date",currDate);
			formObject.setNGValue("WIname",formObject.getWFWorkitemName());
			formObject.setNGValue("Channel_Name","Branch Initiation");

			formObject.setNGValue("Created_By",formObject.getUserName());

			// formObject.setNGValue("Intro_Date",formObject.getNGValue("Created_Date"));
			//formObject.setNGValue("initiationChannel","Branch_Init");
			String init_Channel=formObject.getNGValue("initiationChannel");
			if(init_Channel.equals(""))
				formObject.setNGValue("initiationChannel","Branch_Init");

			if(formObject.getNGValue("empType").contains("Salaried"))
				formObject.setSheetVisible("ParentTab",1, false);

			else if(formObject.getNGValue("empType").contains("Self Employed"))
				formObject.setSheetVisible("ParentTab",3, false);

			SKLogger.writeLog("RLOS","Value Of cmplx_Product_cmplx_ProductGrid:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 1));

			if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 1).equalsIgnoreCase("Personal Loan") && Integer.parseInt(formObject.getNGValue("cmplx_Customer_age"))<18)
			{
				SKLogger.writeLog("RLOS","Value Of cmplx_Product_cmplx_ProductGrid:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 1));
				formObject.setVisible("GuarantorDetails", true);
				formObject.setTop("Incomedetails",110);
				SKLogger.writeLog("RLOS","after guarantor load:");
			}

			if(formObject.getNGValue("Product_Type").contains("Credit Card") && formObject.getNGValue("priority_ProdGrid").equalsIgnoreCase("Primary"))
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
		String Gender="";
		String popupFlag="N";
		String popUpMsg="";
		String popUpControl="";
		String alert_msg="";
		String OTPStatus="";
		SKLogger.writeLog("RLOS Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

		try
		{
			switch (pEvent.getType()) 
			{		
			case FRAME_EXPANDED:
				SKLogger.writeLog("RLOS FRAG LOADED eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

				if (pEvent.getSource().getName().equalsIgnoreCase("CustomerDetails")) 
				{
					//SKLogger.writeLog("Customer Details Frame expand", "Value of Prod Grid is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 0));
					hm.put("CustomerDetails","Clicked");
					popupFlag="N";
					if(!formObject.isVisible("Customer_Frame1"))
					{
						formObject.fetchFragment("CustomerDetails", "Customer", "q_Customer");					
						//  formObject.setNGValue("cmplx_Customer_COuntryOFResidence", "United Arab Emirates-AE");
						formObject.setNGValue("ref_Relationship","FRIENDS");
						String userName = formObject.getUserName();
						if(formObject.getNGValue("processby_email")==null)
						{
							setMailId(userName);
						}
					}
					//if(formObject.getNGValue("cmplx_Customer_NTB")=="true")
					//SetEnableCustomer();



				}
				if (pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails")) 
				{
					hm.put("GuarantorDetails","Clicked");
					popupFlag="N";
					formObject.fetchFragment("GuarantorDetails", "GuarantorDetails", "q_GuarantorDetails");




				}

				if (pEvent.getSource().getName().equalsIgnoreCase("ProductDetailsLoader")) {

					SKLogger.writeLog("RLOS", "Inside ProductDetailsLoader ");
					hm.put("ProductDetailsLoader","Clicked");
					popupFlag="N";
					formObject.fetchFragment("ProductDetailsLoader", "Product", "q_Product");



				}


				if (pEvent.getSource().getName().equalsIgnoreCase("EmploymentDetails")) 
				{				
					if(!formObject.isVisible("EMploymentDetails_Frame1"))
					{							
						hm.put("EmploymentDetails","Clicked");
						popupFlag="N";
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
					
						//IMFields_Employment();
						//BTCFields_Employment();
						//LimitIncreaseFields_Employment();
						//ProductUpgrade_Employment();
						Fields_ApplicationType_Employment();

					}	

				}		 

				if (pEvent.getSource().getName().equalsIgnoreCase("Incomedetails")) 
				{
					hm.put("Incomedetails","Clicked");
					popupFlag="N";
					formObject.fetchFragment("Incomedetails", "IncomeDetails", "q_IncomeDetails");

					int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
					for(int i=0;i<n;i++){
						SKLogger.writeLog("RLOS", "Grid Data[1][9] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9));
						if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1).equalsIgnoreCase("Credit Card") && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")){
							SKLogger.writeLog("RLOS @AKSHAY", "Grid Data[1][9] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9));
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
						SKLogger.writeLog("RLOS", "Emp Type Value is:"+EmpType);

						if(EmpType.equalsIgnoreCase("Salaried")|| EmpType.equalsIgnoreCase("Salaried Pensioner"))
						{
							formObject.setVisible("IncomeDetails_Frame3", false);
							formObject.setHeight("Incomedetails", 630);
							formObject.setHeight("IncomeDetails_Frame1", 625);	
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
							// change by aman
							formObject.setHeight("Incomedetails", 350);
							formObject.setHeight("IncomeDetails_Frame1", 330);
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
						}
					}

					//IMFields_Income();
					//BTCFields_Income();
					//LimitIncreaseFields_Income();
					//ProductUpgrade_Income();


				}

				if (pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails")) {
					hm.put("ReferenceDetails","Clicked");
					popupFlag="N";
					formObject.fetchFragment("ReferenceDetails", "ReferenceDetails", "q_ReferenceDetails");
					LoadPickList("ReferenceDetails_ref_Relationship", "select '--Select--'as description,'' as code union select convert(varchar, Description),code from NG_MASTER_Relationship  order by Code");

					try
					{
						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
					}
					finally{hm.clear();}
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails")) 
				{
					SKLogger.writeLog("RLOS", " Start CompanyDetailse1:");
					formObject.fetchFragment("CompanyDetails", "CompanyDetails", "q_CompanyDetails");
					SKLogger.writeLog("RLOS", " after fetch fragment company details:");
					int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
					for(int i=0;i<n;i++){
						if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("Business titanium Card") && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")){
							SKLogger.writeLog("RLOS", "inside if CompanyDetailse1:");
							formObject.setVisible("CompanyDetails_Label8", true);
							formObject.setVisible("effecLOB", true);//Effective length of buss
							break;
						}

						else if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("Self Employed Credit Card") && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")){
							SKLogger.writeLog("RLOS", "iside else if CompanyDetailse1:");
							formObject.setVisible("CompanyDetails_Label27", true);
							formObject.setVisible("EOW", true);//Emirate of work
							formObject.setVisible("CompanyDetails_Label29", true);
							formObject.setVisible("headOffice", true);
							formObject.setVisible("CompanyDetails_Label28", true);
							formObject.setVisible("visaSponsor", true);
							break;
						}
						else{
							SKLogger.writeLog("RLOS", "iside else CompanyDetailse1:");
							formObject.setVisible("CompanyDetails_Label27", false);
							formObject.setVisible("EOW", false);//Emirate of work
							formObject.setVisible("CompanyDetails_Label29", false);
							formObject.setVisible("headOffice", false);
							formObject.setVisible("CompanyDetails_Label28", false);
							formObject.setVisible("visaSponsor", false);
							formObject.setVisible("CompanyDetails_Label8", false);
							formObject.setVisible("effecLOB", false);
						}

					}	
					SKLogger.writeLog("RLOS", "CompanyDetailse1:");
				}


				if (pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignatoryDetails")) 
				{
					hm.put("AuthorisedSignatoryDetails","Clicked");
					popupFlag="N";
					formObject.fetchFragment("AuthorisedSignatoryDetails", "AuthorisedSignDetails", "q_AuthorisedSignDetails");
					SKLogger.writeLog("RLOS", "AuthorisedSignatoryDetails:");


				}
				if (pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails")) 
				{
					hm.put("PartnerDetails","Clicked");
					popupFlag="N";
					formObject.fetchFragment("PartnerDetails", "PartnerDetails", "q_PartnerDetails");

				}

				if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistoryContainer"))
				{
					hm.put("DecisionHistoryContainer","Clicked");
					popupFlag="N";
					formObject.fetchFragment("DecisionHistoryContainer", "DecisionHistory", "q_DecisionHistory");
					SKLogger.writeLog("RLOS_initiation Decision load", "Product grid state: "+formObject.getNGFrameState("ProductDetailsLoader"));

					//By Akshay
					/*if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 1).equalsIgnoreCase("Credit Card")){
						formObject.setVisible("DecisionHistory_Button3",false );
					}
					else
					{
						formObject.setVisible("DecisionHistory_Button3",true );
					}*/


				}

				if (pEvent.getSource().getName().equalsIgnoreCase("MiscFields")) 
				{
					hm.put("MiscFields","Clicked");
					popupFlag="N";
					formObject.fetchFragment("MiscFields", "MiscellaneousFields", "q_MiscFields");

				}	

				if (pEvent.getSource().getName().equalsIgnoreCase("EligibilityAndProductInformation"))
				{

					hm.put("EligibilityAndProductInformation","Clicked");
					popupFlag="N";
					formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligAndProductInfo");
					Fields_Eligibility();
					Fields_ApplicationType_Eligibility();
					int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");

					if(n>0){
						for(int i=0;i<n;i++){

							if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 1).equalsIgnoreCase("Personal Loan") && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")){
								formObject.setVisible("ELigibiltyAndProductInfo_Frame2", true);
								formObject.setVisible("ELigibiltyAndProductInfo_Frame4", true);
								formObject.setVisible("ELigibiltyAndProductInfo_Frame5", false);
								SKLogger.writeLog("RLOS", "Funding Account Details now Visible...!!!");
								break;
							}

							else if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 1).equalsIgnoreCase("Credit Card") && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")){
								formObject.setVisible("ELigibiltyAndProductInfo_Frame2", true);
								formObject.setVisible("ELigibiltyAndProductInfo_Frame4", false);
								formObject.setVisible("ELigibiltyAndProductInfo_Frame5", true);
								SKLogger.writeLog("RLOS", "Funding Account Details now Visible...!!!");

								if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 5).toUpperCase().contains("SECURED") && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")){
									formObject.setVisible("ELigibiltyAndProductInfo_Frame3", true);
									SKLogger.writeLog("RLOS", "Lein Details now Visible...!!!");
								}
								break;
							}
						}
					}
					else{
						formObject.setVisible("ELigibiltyAndProductInfo_Frame2", false);
						formObject.setVisible("ELigibiltyAndProductInfo_Frame3", false);
					}

				}	


				if (pEvent.getSource().getName().equalsIgnoreCase("Liability_container"))
				{
					hm.put("Liability_container","Clicked");
					popupFlag="N";
					formObject.fetchFragment("Liability_container", "Liability_New", "q_LiabilityNew");
					Fields_Liabilities();
					try
					{
						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
					}
					finally{hm.clear();}						
				}


				if (pEvent.getSource().getName().equalsIgnoreCase("CC_Loan_container")) 
				{
					hm.put("CC_Loan_container","Clicked");
					popupFlag="N";
					formObject.fetchFragment("CC_Loan_container", "CC_Loan", "q_CC_Loan");
					//Fields_ServiceRequest();
					try
					{
						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
					}
					finally{hm.clear();}

				}	


				if (pEvent.getSource().getName().equalsIgnoreCase("Address_Details_container"))
				{
					hm.put("Address_Details_container","Clicked");
					popupFlag="N";
					formObject.fetchFragment("Address_Details_container", "AddressDetails", "q_AddressDetails");
					try
					{
						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
					}
					finally{hm.clear();}

				}	

				if (pEvent.getSource().getName().equalsIgnoreCase("Alt_Contact_container")) 				
				{
					hm.put("Alt_Contact_container","Clicked");
					popupFlag="N";
					formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
					LoadPickList("AlternateContactDetails_custdomicile", "select '--Select--','' as code union select convert(varchar, description),code from NG_MASTER_SOL with (nolock) order by code");
					try
					{
						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
					}
					finally{hm.clear();}					
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails")) 
				{
					hm.put("CardDetails","Clicked");
					popupFlag="N";
					formObject.fetchFragment("CardDetails", "CardDetails", "q_CardDetails");
					int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
					for(int i=0;i<n;i++){
						if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1).equalsIgnoreCase("Personal Loan") && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")){
							formObject.setVisible("CardDetails_Label7", true);
							formObject.setVisible("cmplx_CardDetails_statCycle", true);

						}	
						if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("Business titanium Card") || formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("Self Employed Credit Card") && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")){
							formObject.setVisible("CardDetails_Label3", true);
							formObject.setVisible("cmplx_CardDetails_CompanyEmbossingName", true);

						}
						if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,0).equalsIgnoreCase("Islamic") && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")){
							formObject.setVisible("CardDetails_Label2", true);
							formObject.setVisible("cmplx_CardDetails_CharityOrg", true);
							formObject.setVisible("CardDetails_Label4", true);
							formObject.setVisible("cmplx_CardDetails_CharityAmount", true);
							break;					
						}	
					}	

					try
					{
						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
					}
					finally{hm.clear();}				
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("Supplementary_Container")) 
				{
					hm.put("Supplementary_Container","Clicked");
					popupFlag="N";
					formObject.fetchFragment("Supplementary_Container", "SupplementCardDetails", "q_SuppCardDetails");
					try
					{
						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
					}
					finally{hm.clear();}

				}	

				if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")) 
				{
					hm.put("FATCA","Clicked");
					popupFlag="N";
					formObject.fetchFragment("FATCA", "FATCA", "q_FATCA");
					try
					{
						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
					}
					finally{hm.clear();}

				}	

				if (pEvent.getSource().getName().equalsIgnoreCase("KYC")) 
				{
					hm.put("KYC","Clicked");
					popupFlag="N";
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
					popupFlag="N";
					formObject.fetchFragment("OECD", "OECD", "q_OECD");
					try
					{
						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
					}
					finally{hm.clear();}

				}	

			
				

				if (pEvent.getSource().getName().equalsIgnoreCase("IncomingDocuments"))
				{
					hm.put("IncomingDocuments","Clicked");
					popupFlag="N";
					SKLogger.writeLog("RLOS Initiation Inside ","IncomingDocuments");
					formObject.fetchFragment("IncomingDocuments", "IncomingDoc", "q_IncomingDoc");
					SKLogger.writeLog("RLOS Initiation Inside ","fetchIncomingDocRepeater");
					fetchIncomingDocRepeater();
					SKLogger.writeLog("RLOS Initiation eventDispatched()","formObject.fetchFragment1");
					try{

						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
					}
					finally{
						hm.clear();
					}
				}

			
				break;

			case FRAME_COLLAPSED: {
				break;
			}

			case MOUSE_CLICKED:

				if(pEvent.getSource().getName().equalsIgnoreCase("existingOldCustomer"))
				{
					if(formObject.getNGValue("existingOldCustomer").equalsIgnoreCase("true"))
					{
						SKLogger.writeLog("RLOS", "On click existing old customer !!");
						String squery = "select VAR_REC_1 from WFINSTRUMENTTABLE with (nolock) where ProcessInstanceID ='"+formObject.getWFWorkitemName()+"' ";
						List<List<String>> outputindex = null;
						outputindex = formObject.getNGDataFromDataCache(squery);
						SKLogger.writeLog("RLOS Initiation", "outputItemindex is:" +  outputindex);
						String itemIndex =outputindex.get(0).get(0);
						formObject.setNGValue("NewApplicationNo", itemIndex);
						formObject.setNGValue("ApplicationRefNo", itemIndex);
					}
				}


				if(pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails_Reference_Add"))
				{
					formObject.setNGValue("ReferenceDetails_reference_wi_name",formObject.getWFWorkitemName());
					SKLogger.writeLog("RLOS", "Inside add button: "+formObject.getNGValue("ReferenceDetails_reference_wi_name"));
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_ReferencDetails_Cmplx_gr_ReferenceDetails");
				}

				if(pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails_Reference__modify"))
				{

					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_ReferencDetails_Cmplx_gr_ReferenceDetails");
				}

				if(pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails_Reference_delete"))
				{

					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_ReferencDetails_Cmplx_gr_ReferenceDetails");
				}


				if(pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails_add"))
				{
					formObject.setNGValue("guarantor_WIName",formObject.getWFWorkitemName());
					SKLogger.writeLog("RLOS", "Inside add button: "+formObject.getNGValue("guarantor_WIName"));
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_GuarantorDetails_cmplx_GuarantorGrid");
				}

				if(pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails_modify"))
				{

					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_GuarantorDetails_cmplx_GuarantorGrid");
				}

				if(pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails_delete"))
				{

					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_GuarantorDetails_cmplx_GuarantorGrid");
				}


				if (pEvent.getSource().getName().equalsIgnoreCase("Add")){
					SKLogger.writeLog("RLOS", "Inside add button: "+formObject.getNGValue("ReqProd"));
					/*if(formObject.getNGValue("ReqProd").equalsIgnoreCase("Credit Card")){
						SKLogger.writeLog("RLOS", "Inside add button: aman123"+formObject.getNGValue("ReqProd"));
						formObject.setVisible("DecisionHistory_Button3",false );
					}
					else{
						formObject.setVisible("DecisionHistory_Button3", true);
					} ---By akshay*/

					formObject.setNGValue("Grid_wi_name",formObject.getWFWorkitemName());
					SKLogger.writeLog("RLOS", "Inside add button: "+formObject.getNGValue("Grid_wi_name"));
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Product_cmplx_ProductGrid");



					ParentToChild();
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
					SKLogger.writeLog("RLOS", "Inside add button: aman123"+formObject.getNGValue("ReqProd"));
					/*if(formObject.getNGValue("ReqProd").equalsIgnoreCase("Credit Card")){
						SKLogger.writeLog("RLOS", "Inside add button: aman123"+formObject.getNGValue("ReqProd"));
						formObject.setVisible("DecisionHistory_Button3",false );
					}
					else{
						formObject.setVisible("DecisionHistory_Button3",true );
					}--By Akshay 17/6/2017*/

					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Product_cmplx_ProductGrid");


ParentToChild();
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
	ParentToChild();
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

				if(pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails_Add"))
				{
					formObject.setNGValue("CompanyDetails_company_winame",formObject.getWFWorkitemName());
					SKLogger.writeLog("RLOS", "Inside add button: "+formObject.getNGValue("CompanyDetails_company_winame"));
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
					formObject.setNGValue("partner_winame",formObject.getWFWorkitemName());
					SKLogger.writeLog("RLOS", "Inside add button: "+formObject.getNGValue("partner_winame"));
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
					formObject.setNGValue("AuthorisedSign_wiName",formObject.getWFWorkitemName());
					SKLogger.writeLog("RLOS", "Inside add button: "+formObject.getNGValue("AuthorisedSign_wiName"));
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

				if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New_Add")){					
					formObject.setNGValue("LiabilityAddition_wiName",formObject.getWFWorkitemName());
					SKLogger.writeLog("RLOS", "Inside add button: "+formObject.getNGValue("LiabilityAddition_wiName"));
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New_modify")){
					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New_delete")){
					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
				}	

				if (pEvent.getSource().getName().equalsIgnoreCase("addr_Add")  ){
					SKLogger.writeLog("RLOS", "Inside addredd grid add button");
					//	boolean flag_addressType= Address_Validate();
					//if(flag_addressType){
					formObject.setNGValue("address_Wi_name",formObject.getWFWorkitemName());
					SKLogger.writeLog("RLOS", "Inside address button: "+formObject.getNGValue("address_Wi_name"));
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_AddressDetails_cmplx_AddressGrid");
					//}

				}


				if (pEvent.getSource().getName().equalsIgnoreCase("addr_Modify")){
					
				    formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_AddressDetails_cmplx_AddressGrid");
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("addr_Delete")){
					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_AddressDetails_cmplx_AddressGrid");

				}

				if (pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails_add")){					
					formObject.setNGValue("supplement_WiName",formObject.getWFWorkitemName());
					SKLogger.writeLog("RLOS", "Inside add button: "+formObject.getNGValue("supplement_WiName"));
					formObject.ExecuteExternalCommand("NGAddRow", "SupplementCardDetails_cmplx_SupplementGrid");
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails_modify")){
					formObject.ExecuteExternalCommand("NGModifyRow", "SupplementCardDetails_cmplx_SupplementGrid");
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails_delete")){
					formObject.ExecuteExternalCommand("NGDeleteRow", "SupplementCardDetails_cmplx_SupplementGrid");
				}	

				if (pEvent.getSource().getName().equalsIgnoreCase("OECD_add")){					
					formObject.setNGValue("OECD_winame",formObject.getWFWorkitemName());
					SKLogger.writeLog("RLOS", "Inside add button: "+formObject.getNGValue("OECD_winame"));
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_OECD_cmplx_GR_OecdDetails");
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("OECD_modify")){
					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_OECD_cmplx_GR_OecdDetails");
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("OECD_delete")){
					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_OECD_cmplx_GR_OecdDetails");
				}	


				if (pEvent.getSource().getName().equalsIgnoreCase("ExternalLiabilities_Button1"))
				{
					hm.put("ExternalLiabilities_Button1","Clicked");
					popupFlag="N";

					try
					{
						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
					}
					finally{hm.clear();}


				}

				if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Button5"))
				{
					hm.put("DecisionHistory_Button5","Clicked");
					popupFlag="N";
					SKLogger.writeLog("submit button","inside submit button");
					String wi_name= formObject.getWFWorkitemName();
					List<List<String>> AccountCount= formObject.getDataFromDataSource("Select count from ng_RLOS_CUSTEXPOSE_AcctDetails where AcctType='CURRENT ACCOUNT' AND Wi_Name="+wi_name+";");
					String countCurrentAccount= AccountCount.get(0).get(0);
					SKLogger.writeLog("RLOS count of current account not NTB",countCurrentAccount);
					if((formObject.getNGValue("cmplx_Customer_NTB")=="false")&&countCurrentAccount!="0"){
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
					if(formObject.getNGValue("cmplx_Customer_NTB")=="true"){
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
					else if(formObject.getNGValue("cmplx_Customer_NTB")=="false"){
						outputResponse = GenerateXML("NEW_ACCOUNT_REQ","");
						ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
						SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
						if(ReturnCode.equalsIgnoreCase("0000") ){
							valueSetCustomer(outputResponse, "");	
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
					try
					{
						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
					}
					finally{hm.clear();}					
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

				if(pEvent.getSource().getName().equalsIgnoreCase("FetchDetails"))
				{
					//if(formObject.getNGValue("cmplx_Customer_CardNotAvailable")=="true")                                    
					hm.put("FetchDetails","Clicked");
					popupFlag="Y";
					formObject.setNGValue("cmplx_Customer_ISFetchDetails", "Y");
					String cif_id_avail="false";
					if(cif_id_avail.equalsIgnoreCase("false")){ 
						//Deepak Code change for Entity Details
						//if("Is_EID_Genuine".equalsIgnoreCase("Y") && "cmplx_Customer_EmiratesID" != ""){
						String EID = formObject.getNGValue("cmplx_Customer_EmiratesID");
						SKLogger.writeLog("RLOS_Initiation ", "EID value for Entity Details: "+EID );
						String ReadFrom_card_exc = formObject.getNGValue("cmplx_Customer_readfromcardflag");
						if( EID!=null && !EID.equalsIgnoreCase("")&& ReadFrom_card_exc.equalsIgnoreCase("Y")){
							if( EID!=null && !EID.equalsIgnoreCase("")){
								outputResponse = GenerateXML("ENTITY_DETAILS","");
								ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
								SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
								if(ReturnCode.equalsIgnoreCase("0000")){
									formObject.setNGValue("Is_Entity_Details","Y");
									valueSetCustomer(outputResponse , "");
								}
								else{
									formObject.setNGValue("Is_Entity_Details","N");
								}
								SKLogger.writeLog("RLOS value of Entity Details",formObject.getNGValue("Is_Entity_Details"));

							}
						}
						outputResponse =GenerateXML("CUSTOMER_ELIGIBILITY","");
						SKLogger.writeLog("RLOS value of ReturnDesc","Customer Eligibility");
						ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
						SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
						int cif_row_count=formObject.getLVWRowCount("q_cif_detail");
						SKLogger.writeLog("RLOS Initiation","row count for cif details: "+ cif_row_count);
						
						try{
							for(int i=0;i<=cif_row_count;i++)
							{
								SKLogger.writeLog("RLOS Initiation","cif details deleting row no : "+ i);
								formObject.removeItem("q_cif_detail", i);
							}
						}
						catch(Exception e){
							SKLogger.writeLog("RLOs","Exception occurred:"+e.getMessage());
						}
						
						if(ReturnCode.equalsIgnoreCase("0000") )
						{
							setcustomer_enable();
							popupFlag="Y";
							valueSetCustomer(outputResponse , "");    
							formObject.setNGValue("Is_Customer_Eligibility","Y");
							parse_cif_eligibility(outputResponse);
							String NTB_flag = formObject.getNGValue("cmplx_Customer_NTB");
							//Customer_enable();
							// if(NTB_flag.equalsIgnoreCase("true")){
							//  Customer_enable();
							//}
							if(NTB_flag.equalsIgnoreCase("true")){
								formObject.setVisible("Customer_Frame2", false);
								SKLogger.writeLog("RLOS", "inside Customer Eligibility to through Exception to Exit:");
								alert_msg = "Customer is a New to Bank Customer.";

								throw new ValidatorException(new FacesMessage(alert_msg));

							}
							else{
								alert_msg = fetch_cust_details_primary();
							}




							SKLogger.writeLog("RLOS value of Customer Details",formObject.getNGValue("Is_Customer_Eligibility"));
							SKLogger.writeLog("RLOS value of BlacklistFlag",formObject.getNGValue("BlacklistFlag"));
							SKLogger.writeLog("RLOS value of DuplicationFlag",formObject.getNGValue("DuplicationFlag"));
							SKLogger.writeLog("RLOS value of IsAcctCustFlag",formObject.getNGValue("IsAcctCustFlag"));
							//added here

							//ended here
							if(formObject.getNGValue("Is_Customer_Eligibility").equalsIgnoreCase("Y") && formObject.getNGValue("Is_Customer_Details").equalsIgnoreCase("Y"))
							{ 
								SKLogger.writeLog("RLOS value of Customer Details","inside if condition");
								SKLogger.writeLog("RLOS Initiation ", "Customer Eligibility and Customer details fetched sucessfully");
								formObject.setEnabled("FetchDetails", false); 
								alert_msg = "Existing Customer Details fetched Sucessfully!";

							}
							else{
								SKLogger.writeLog("RLOS Initiation ", "Customer Eligibility and Customer details failed");
								alert_msg = "Fetch Customer Details operation failed, try after some time or contact administrator !";
								formObject.setEnabled("FetchDetails", true);
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

				if(pEvent.getSource().getName().equalsIgnoreCase("Send_OTP_Btn"))
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

				if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_SendSMS"))
				{

					SKLogger.writeLog("inside DecisionHistory_Button6", "");
					hm.put("DecisionHistory_Button6","Clicked");
					popupFlag="Y";
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
					try
					{
						throw new ValidatorException(new FacesMessage(alert_msg));
					}
					finally{hm.clear();}
				}




				if(pEvent.getSource().getName().equalsIgnoreCase("Validate_OTP_Btn"))
				{
					hm.put("Validate_OTP_Btn","Clicked");
					popupFlag="N";

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


				if(pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails_Button3")){
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
						formObject.setNGValue("Is_Customer_Details","Y");

						SKLogger.writeLog("RLOS value of EID_Genuine","value of company Details corporation"+formObject.getNGValue("Is_Customer_Details"));

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
						SKLogger.writeLog("RLOS value of Customer Details",formObject.getNGValue("Is_Customer_Details"));
					}
					else{
						SKLogger.writeLog("Customer Details","Customer Details Corporation CIF is not generated");
						formObject.setNGValue("Is_Customer_Details","N");
					}
					SKLogger.writeLog("RLOS value of  Corporation CIF",formObject.getNGValue("Is_Customer_Details"));
				}






				if (pEvent.getSource().getName().equalsIgnoreCase("Button9")){

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



					if(!formObject.isVisible("EMploymentDetails_Frame1")){
						formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmploymentDetails");
						formObject.setNGFrameState("EmploymentDetails", 0);
						formObject.setNGFrameState("EmploymentDetails", 1);

					}
				

					if(!formObject.isVisible("Liability_New_Frame1")){
						formObject.fetchFragment("Liability_container", "Liability_New", "q_LiabilityNew");
						formObject.setNGFrameState("Liability_New_Frame1", 0);
						formObject.setNGFrameState("Liability_New_Frame1", 1);
					}

				}

				if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails_Button2")) {
					String EmpName=formObject.getNGValue("EMploymentDetails_Text21");
					String EmpCode=formObject.getNGValue("EMploymentDetails_Text22");
					SKLogger.writeLog("RLOS", "EMpName$"+EmpName+"$");
					String query=null;
					if(EmpName.trim().equalsIgnoreCase(""))
						query="select distinct(EMPR_NAME),EMPLOYER_CODE,INCLUDED_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY from NG_RLOS_ALOC_OFFLINE_DATA where EMPLOYER_CODE Like '%"+EmpCode+"%'";

					else
						query="select distinct(EMPR_NAME),EMPLOYER_CODE,INCLUDED_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY from NG_RLOS_ALOC_OFFLINE_DATA where EMPR_NAME Like '%"+EmpName + "%' or EMPLOYER_CODE Like '%"+EmpCode+"'";

					SKLogger.writeLog("RLOS", "query is: "+query);
					populatePickListWindow(query,"EMploymentDetails_Button2", "Employer Name,Employer Code,INCLUDED IN PL ALOC,INCLUDED IN CC ALOC,INDUSTRY SECTOR,INDUSTRY MACRO,INDUSTRY MICRO,CONSTITUTION,NAME OF FREEZONE AUTHORITY", true, 20);			     
				}

				if(pEvent.getSource().getName().equalsIgnoreCase("cmplx_ExternalLiabilities_AECBConsent")){
					if(formObject.getNGValue("cmplx_ExternalLiabilities_AECBConsent")=="true")
						formObject.setEnabled("ExternalLiabilities_Button1",true);
					else
						formObject.setEnabled("ExternalLiabilities_Button1",false);
				}


				if(pEvent.getSource().getName().equalsIgnoreCase("Eligibility_Emp")){
					SKLogger.writeLog("Rlos Current Selected Sheet is",formObject.getSelectedSheet(pEvent.getSource().getName())+"");
					formObject.setSelectedSheet("ParentTab",3);		
					formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligAndProductInfo");
					formObject.setNGFrameState("EligibilityAndProductInformation", 0);
					formObject.setNGFrameState("EligibilityAndProductInformation", 1);
					formObject.setNGFrameState("EligibilityAndProductInformation", 0);
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails_Button2"))
				{
					hm.put("SupplementCardDetails_Button2","Clicked");
					popupFlag="N";
					formObject.setNGValue("supplement_WIname",formObject.getWFWorkitemName());	
					SKLogger.writeLog("RLOS", "Inside Supplement_add button: "+formObject.getNGValue("supplement_WIname"));
					formObject.ExecuteExternalCommand("NGAddRow", "SupplementCardDetails_cmplx_SupplementGrid");
					try
					{
						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
					}
					finally{hm.clear();}

				}

				if (pEvent.getSource().getName().equalsIgnoreCase("FATCA_Button1")){
					try{
						String text=formObject.getNGItemText("FATCA_List1", formObject.getSelectedIndex("FATCA_List1"));
						SKLogger.writeLog("RLOS", "Inside FATCA_Button1 "+text);
						formObject.addItem("FATCA_List2", text);
						formObject.removeItem("FATCA_List1", formObject.getSelectedIndex("FATCA_List1"));
					}
					catch(Exception e){SKLogger.writeLog("RLOs","Exception occurred:"+e.getMessage());}
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("FATCA_Button2")){
					try{
						SKLogger.writeLog("RLOS", "Inside FATCA_Button2 ");
						formObject.addItem("FATCA_List1", formObject.getNGItemText("FATCA_List2", formObject.getSelectedIndex("FATCA_List2")));
						formObject.removeItem("FATCA_List2", formObject.getSelectedIndex("FATCA_List2"));
					}
					catch(Exception e){SKLogger.writeLog("RLOs","Exception occurred:"+e.getMessage());}

				}

				if (pEvent.getSource().getName().equalsIgnoreCase("IncomingDoc_AddFromPCButton")){
					IRepeater repObj=null;
					repObj = formObject.getRepeaterControl("IncomingDoc_Frame");
					repObj.getValue(repObj.getChangeIndex(),"IncomingDoc_Label1");
					SKLogger.writeLog("","value of repeater:"+repObj.getValue(repObj.getChangeIndex(),"IncomingDoc_Label1"));
				}    


				if(pEvent.getSource().getName().equalsIgnoreCase("Reject")){
					SKLogger.writeLog("Rlos Current Selected Sheet is",formObject.getSelectedSheet(pEvent.getSource().getName())+"");
					formObject.setSelectedSheet("ParentTab",8);		
					formObject.fetchFragment("DecisionHistoryContainer", "DecisionHistory", "q_DecisionHistory");
					formObject.setNGFrameState("DecisionHistoryContainer", 0);
					formObject.setNGFrameState("DecisionHistoryContainer", 1);
					formObject.setNGFrameState("DecisionHistoryContainer", 0);
					formObject.setNGFocus("DecisionHistory_Button4");
				}

				if(pEvent.getSource().getName().equalsIgnoreCase("Liability_New_Button1"))
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

				if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Button3"))
				{
					getCustAddress_details();
					hm.put("Liability_New_Button1","Clicked");
					popupFlag="N";
					//added
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
								throw new ValidatorException(new FacesMessage("Account Created successfully!!"));
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
						SKLogger.writeLog("RLOS"," count of current account: "+formObject.getNGValue("AccountCount"));

						if((formObject.getNGValue("cmplx_Customer_NTB").equals("false") && Integer.parseInt(formObject.getNGValue("AccountCount"))==0)){

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


			/*	if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_FinnancialSummary") || pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_FinacialSummarySelf"))
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
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_FinancialSummarySelf"))
				{
					hm.put("IncomeDetails_FinancialSummarySelf","Clicked");
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
				}
*/
				if(pEvent.getSource().getName().equalsIgnoreCase("ReadFromCIF")){

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

				if(pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails_Button4")){

					outputResponse = GenerateXML("CUSTOMER_DETAILS","Signatory_CIF");
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
					if(ReturnCode.equalsIgnoreCase("0000") ){
						formObject.setNGValue("Is_Customer_Details","Y");
						SKLogger.writeLog("RLOS value of EID_Genuine","value of Guarantor_CIF"+formObject.getNGValue("Is_Customer_Details"));
						valueSetCustomer(outputResponse , "Signatory_CIF");    
						SKLogger.writeLog("RLOS value of Customer Details","Guarantor_CIF is generated");
						SKLogger.writeLog("RLOS value of Customer Details",formObject.getNGValue("Is_Customer_Details"));
					}
					else{
						SKLogger.writeLog("Customer Details","Customer Details is not generated");
						formObject.setNGValue("Is_Customer_Details","N");
					}
					SKLogger.writeLog("RLOS value of Signatory_CIF",formObject.getNGValue("Is_Customer_Details"));
				}

				if(pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails_Button3")){

					outputResponse = GenerateXML("CUSTOMER_DETAILS","Company_CIF");
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
					if(ReturnCode.equalsIgnoreCase("0000") ){
						formObject.setNGValue("Is_Customer_Details","Y");
						SKLogger.writeLog("RLOS value of EID_Genuine","value of Guarantor_CIF"+formObject.getNGValue("Is_Customer_Details"));
						valueSetCustomer(outputResponse , "Company_CIF");    
						SKLogger.writeLog("RLOS value of Customer Details","Company_CIF is generated");
						SKLogger.writeLog("RLOS value of Customer Details",formObject.getNGValue("Is_Customer_Details"));
					}
					else{
						SKLogger.writeLog("Customer Details","Customer Details is not generated");
						formObject.setNGValue("Is_Customer_Details","N");
					}
					SKLogger.writeLog("RLOS value of Company_CIF",formObject.getNGValue("Is_Customer_Details"));
				}


				if(pEvent.getSource().getName().equalsIgnoreCase("Customer_save")){
					formObject.saveFragment("CustomerDetails");
					popupFlag = "Y";
					alert_msg="Customer details saved";
					formObject.setNGValue("cmplx_Customer_IscustomerSave", "Y");
					throw new ValidatorException(new FacesMessage(alert_msg));
				}

				if(pEvent.getSource().getName().equalsIgnoreCase("Product_Save")){
					formObject.saveFragment("ProductDetailsLoader");
					ParentToChild();
					popupFlag = "Y";
					alert_msg="Product details Saved";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}

				if(pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails_Save")){
					formObject.saveFragment("GuarantorDetails");
					popupFlag = "Y";
					alert_msg="Guarantor Details Saved";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}

				if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_Salaried_Save")){
					formObject.saveFragment("Incomedetails");
					popupFlag = "Y";
					alert_msg="Income Details Salaried Saved";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}

				if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_SelfEmployed_Save")){
					formObject.saveFragment("Incomedetails");
					popupFlag = "Y";
					alert_msg="Income Details for SelfEmployed Saved";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}

				if(pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails_Save")){
					formObject.saveFragment("CompanyDetails");
					popupFlag = "Y";
					alert_msg="Company Details Saved";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}


				if(pEvent.getSource().getName().equalsIgnoreCase("Liability_New_Save")){
					formObject.saveFragment("Liability_container");
					popupFlag = "Y";
					alert_msg="Liability New Saved";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}

				if(pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails_Save")){
					formObject.saveFragment("EmploymentDetails");
					popupFlag = "Y";
					alert_msg="Employment Details Saved";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}

				if(pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo_Save")){
					formObject.saveFragment("EligibilityAndProductInformation");
					popupFlag = "Y";
					alert_msg="ELigibilty and Product Information Saved";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}

				if(pEvent.getSource().getName().equalsIgnoreCase("MiscellaneousFields_Save")){
					formObject.saveFragment("MiscFields");
					popupFlag = "Y";
					alert_msg="Miscellaneous Fields Saved";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}

				if(pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_Save")){
					formObject.saveFragment("Address_Details_container");
					popupFlag = "Y";
					alert_msg="Address Details Saved";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}

				if(pEvent.getSource().getName().equalsIgnoreCase("ContactDetails_Save")){
					formObject.saveFragment("Alt_Contact_container");
					popupFlag = "Y";
					alert_msg="Contact Details Saved";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}

				if(pEvent.getSource().getName().equalsIgnoreCase("CardDetails_save")){
					formObject.saveFragment("CardDetails");
					popupFlag = "Y";
					alert_msg="Card Details Saved";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}

				if(pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails_Save")){
					formObject.saveFragment("Supplementary_Container");
					popupFlag = "Y";
					alert_msg="Supplement Card Details Saved";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}

				if(pEvent.getSource().getName().equalsIgnoreCase("FATCA_Save")){
					formObject.saveFragment("FATCA");

					popupFlag = "Y";
					alert_msg="FATCA Fields Saved";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}

				if(pEvent.getSource().getName().equalsIgnoreCase("KYC_Save")){
					formObject.saveFragment("KYC");
					popupFlag = "Y";
					alert_msg="KYC Fields Saved";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}

				if(pEvent.getSource().getName().equalsIgnoreCase("OECD_Save")){
					formObject.saveFragment("OECD");
					popupFlag = "Y";
					alert_msg="OECD Fields Saved";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails_save")){
					formObject.saveFragment("ReferenceDetails");
					popupFlag = "Y";
					alert_msg="Reference Details  Saved";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}
				if(pEvent.getSource().getName().equalsIgnoreCase("ServiceRequest_Save")){
					formObject.saveFragment("CC_Loan_container");
					popupFlag = "Y";
					alert_msg="Service Request information Saved";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}

				if(pEvent.getSource().getName().equalsIgnoreCase("IncomingDoc_Save")){
					SKLogger.writeLog("RLOS", "TANSHU Inside IncomingDoc_Save button!!");
					formObject.saveFragment("IncomingDocuments");
					popupFlag = "Y";
					alert_msg="Incoming Documents Saved ";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}

				if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Save")){
					formObject.saveFragment("DecisionHistoryContainer");
					popupFlag = "Y";
					alert_msg="Decision History Saved";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}

				if(pEvent.getSource().getName().equalsIgnoreCase("ReadFromCIF"))
				{

					SKLogger.writeLog("inside ReadFromCIF button guarantor", "");
					hm.put("ReadFromCIF","Clicked");
					popupFlag="Y";
					outputResponse = GenerateXML("CUSTOMER_DETAILS","Guarantor_CIF");
					SKLogger.writeLog("RLOS value of ReturnCode","Inside Guarantor");
					Gender =  (outputResponse.contains("<Gender>")) ? outputResponse.substring(outputResponse.indexOf("<Gender>")+"</Gender>".length()-1,outputResponse.indexOf("</Gender>")):"";
					SKLogger.writeLog("RLOS value of Gender",Gender);
					String  Marital_Status =  (outputResponse.contains("<MaritialStatus>")) ? outputResponse.substring(outputResponse.indexOf("<MaritialStatus>")+"</MaritialStatus>".length()-1,outputResponse.indexOf("</MaritialStatus>")):"";
					SKLogger.writeLog("RLOS value of Marital_Status",Marital_Status);
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);

					if(ReturnCode.equalsIgnoreCase("0000") ){
						alert_msg="Existing Customer Details fetched Sucessfully";

					}
					else{
						//formObject.setNGValue("OTP_Generation","OTP is not generated");
						alert_msg = "failure while fetching details";
						SKLogger.writeLog("RLOS value of OTP_Generation","Error while sending SMS");
					}
					try
					{
						throw new ValidatorException(new FacesMessage(alert_msg));
					}
					finally{hm.clear();}
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
				
				// added for dectech call by abhishek 
				 if(pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo_Button1"))
				{		popupFlag="Y";
						formObject.setNGValue("ParentToChild","Eligibility");
						SKLogger.writeLog("$$After Generatexml for dectech call..outputResponse is : "+outputResponse,"");
						
						outputResponse = GenerateXML("DECTECH","");
						SKLogger.writeLog("$$After Generatexml for CallFired : "+CallFired,"");
						SKLogger.writeLog("$$After Generatexml for dectech call..outputResponse ASDASDASDASD : "+outputResponse,"");
						
						//ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
						//SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
						/*if(outputResponse.contains("AMAN")){
							alert_msg="Critical error occurred Please contact administrator";
							throw new ValidatorException(new FacesMessage(alert_msg));
						}
						else{*/	
							valueSetCustomer(outputResponse , "");   
							alert_msg="Decision engine integration successful";
							SKLogger.writeLog("after value set customer for dectech call","");
							formObject.RaiseEvent("WFSave");
							throw new ValidatorException(new FacesMessage(alert_msg));
					//	}
						
						//SKLogger.writeLog("$$After Generatexml for dectech call..outputResponse ALERT : "+outputResponse,"");
						
						
						//SKLogger.writeLog("$$After Generatexml for dectech call..outputResponse WFS : "+outputResponse,"");
						
						
						
						
				}
				 
				 if(pEvent.getSource().getName().equalsIgnoreCase("Button2"))
					{		popupFlag="Y";
							formObject.setNGValue("ParentToChild","CalculateDBR");
							outputResponse = GenerateXML("DECTECH","");
							SKLogger.writeLog("$$After Generatexml for dectech call..outputResponse is : "+outputResponse,"");
							SKLogger.writeLog("$$After Generatexml for dectech call..outputResponse ASDASDASDASD : "+outputResponse,"");
							
							//ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
							//SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
							/*if(outputResponse.contains("SystemErrorCode")){
								alert_msg="Critical error occurred Please contact administrator";
								throw new ValidatorException(new FacesMessage(alert_msg));
							}
							else{*/	
								valueSetCustomer(outputResponse , "");   
								alert_msg="Decision engine integration successful";
								SKLogger.writeLog("after value set customer for dectech call","");
								formObject.RaiseEvent("WFSave");
								throw new ValidatorException(new FacesMessage(alert_msg));
						//	}
							
							//SKLogger.writeLog("$$After Generatexml for dectech call..outputResponse ALERT : "+outputResponse,"");
							
							
							//SKLogger.writeLog("$$After Generatexml for dectech call..outputResponse WFS : "+outputResponse,"");
							
							
							
							
					}
				// added for dectech call by abhishek 

				break;	


			case TAB_CLICKED:

				SKLogger.writeLog("RLOS akshay TAB select sheet caption: ",formObject.getSheetCaption(pEvent.getSource().getName(), formObject.getSelectedSheet(pEvent.getSource().getName())));
				String SheetCaption=formObject.getSheetCaption(pEvent.getSource().getName(), formObject.getSelectedSheet(pEvent.getSource().getName()));
				/*
				if(SheetCaption.equalsIgnoreCase("Employment Details")){
					if(!formObject.isVisible("Product_Frame1")){
						formObject.fetchFragment("ProductDetailsLoader", "Product", "q_Product");
						formObject.setNGFrameState("ProductDetailsLoader", 0);
						formObject.setNGFrameState("ProductDetailsLoader", 1);
					}


					SKLogger.writeLog("RLOS","Inside: "+SheetCaption);
					int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
					if(n>0)
					{
						for(int i=0;i<n;i++){
							if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2).equalsIgnoreCase("RAKFINANCEPLUS-INSTALLMENT LOAN")){
								SKLogger.writeLog("RLOS Tab_CLICKED  ",formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2));
								formObject.setVisible("MiscFields", true);
								break;
							}
							else
								formObject.setVisible("MiscFields", false);

						}
					}	
					else
						SKLogger.writeLog("RLOS Tab_CLICKED  ","No row in product GRID");

				}
				 */

				if(SheetCaption.equalsIgnoreCase("Demographics Details")){
					/*
						formObject.fetchFragment("ProductDetailsLoader", "Product", "q_Product");
						formObject.setNGFrameState("ProductDetailsLoader", 0);
						formObject.setNGFrameState("ProductDetailsLoader", 1);
					 */
				}
				SKLogger.writeLog("RLOS","Inside "+SheetCaption);
				int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
				if(n>0)
				{
					if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 1).equalsIgnoreCase("Credit Card")){
						formObject.setVisible("Supplementary_Container", true);
						formObject.setTop("FATCA",formObject.getTop("Supplementary_Container")+25);
						formObject.setTop("KYC", formObject.getTop("FATCA")+25);
						formObject.setTop("OECD", formObject.getTop("KYC")+25);
						//Java takes height of entire container in getHeight while js takes current height	i.e collapsed/expanded
					}
					else{
						formObject.setVisible("Supplementary_Container", false);
						formObject.setTop("FATCA",formObject.getTop("Supplementary_Container"));
						formObject.setTop("KYC", formObject.getTop("FATCA")+25);
						formObject.setTop("OECD", formObject.getTop("KYC")+25);
					}
				}
				else{
					formObject.setVisible("Supplementary_Container", false);
					formObject.setTop("FATCA",formObject.getTop("Supplementary_Container"));
					formObject.setTop("KYC", formObject.getTop("FATCA")+25);
					formObject.setTop("OECD", formObject.getTop("KYC")+25);
				}
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
						if(formObject.getNGValue("cmplx_Customer_NEP")=="false"){
							formObject.setLocked("Customer_Frame1",true);
							formObject.setHeight("Customer_Frame1", 780);
							formObject.setHeight("CustomerDetails", 840);
							formObject.setLocked("Customer_save",false);
							//formObject.setNGValue("cmplx_Customer_EmiratesID",formObject.getNGValue("EmirateID"));
							formObject.setLocked("Customer_Frame2",false);
							formObject.setLocked("Customer_Frame3",false);
							if (formObject.getNGValue("cmplx_Customer_NTB")=="true")
								formObject.setVisible("Customer_Frame2", false);
						}
						formObject.setLocked("cmplx_Customer_CardNotAvailable",false);
						formObject.setLocked("ReadFromCard",false);
						formObject.setLocked("cmplx_Customer_NEP",false);
						formObject.setLocked("cmplx_Customer_DLNo",false);
						formObject.setLocked("cmplx_Customer_Passport2",false);
						formObject.setLocked("cmplx_Customer_Passport3",false);
						formObject.setLocked("cmplx_Customer_PAssport4",false);
						SKLogger.writeLog(" In RLOS customer in fragmnt loaded", "");
						formObject.setLocked("cmplx_Customer_SecNAtionApplicable",false);

						String EID = formObject.getNGValue("cmplx_Customer_EmiratesID");
						String NEP = formObject.getNGValue("cmplx_Customer_NEP");
						String card_not_avl = formObject.getNGValue("cmplx_Customer_CardNotAvailable");

						if(EID.equalsIgnoreCase("") && NEP.equalsIgnoreCase("false") && card_not_avl.equalsIgnoreCase("false")){
							SKLogger.writeLog(" In RLOS inside if customer in fragmnt loaded", "");
							formObject.setLocked("cmplx_Customer_SecNAtionApplicable",false);
						}
						else{
							formObject.setLocked("cmplx_Customer_FIrstNAme",false);
							formObject.setLocked("cmplx_Customer_MiddleName",false);
							formObject.setLocked("cmplx_Customer_LAstNAme",false);
							formObject.setLocked("cmplx_Customer_DOb",false);
							formObject.setLocked("cmplx_Customer_PAssportNo",false);
							formObject.setLocked("cmplx_Customer_Nationality",false);
							formObject.setLocked("cmplx_Customer_MobNo",false);
							formObject.setLocked("cmplx_Customer_DOb",false);
							formObject.setLocked("FetchDetails",true);
							formObject.setLocked("cmplx_Customer_SecNAtionApplicable",false);
							SKLogger.writeLog(" In RLOS inside else customer in fragmnt loaded", "");
						}
						formObject.setNGValue("ref_Relationship","FRIEND");
						//formObject.setNGValue("cmplx_Customer_COuntryOFResidence", "United Arab Emirates-AE");
						formObject.setNGValue("cmplx_Customer_CustomerCategory", "Normal");
						loadPicklistCustomer();
						SKLogger.writeLog("RLOS","Encrypted CIF is: "+formObject.getNGValue("encrypt_CIF"));
						//formObject.setNGValue("cmplx_Customer_CIFNO",formObject.getNGValue("encrypt_CIF"));        		 

					}

					if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
						LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct with (nolock)");
						LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
					}

					if (pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails")) {
						LoadPickList("title", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_title with (nolock) order by Code");
						LoadPickList("gender", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_gender with (nolock) order by Code");
						LoadPickList("nationality", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
					}

					if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails")) {
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
						if((formObject.getNGValue("cmplx_Customer_NTB")=="true")||(formObject.getNGValue("cmplx_Customer_NEP")=="true")){
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
						LoadPickList("cmplx_IncomeDetails_AvgBalFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
						LoadPickList("cmplx_IncomeDetails_CreditTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
						LoadPickList("cmplx_IncomeDetails_AvgCredTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
						LoadPickList("cmplx_IncomeDetails_AnnualRentFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
					}

					if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")) {

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

					if (pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails")) {
						/*formObject.setLocked("cif", true);*/
						formObject.setLocked("CompanyDetails_Button3", true);
						loadPicklist_Company();
	                SKLogger.writeLog("Inside company details fragment loaded", "Loaded Pickli" +"sest");
					}

					if (pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails")) {
						formObject.setLocked("CIFNo", true);
						formObject.setLocked("AuthorisedSignDetails_Button4", true);
						LoadPickList("nationality", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Country with (nolock)");
						LoadPickList("SignStatus", "select '--Select--' union select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");
					}

					if (pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails")) {
						LoadPickList("PartnerDetails_nationality", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Country with (nolock)");
					}


					if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) {
						loadPicklist4();
						//formObject.setVisible("cmplx_EmploymentDetails_channelcode",false);
						//formObject.setVisible("EMploymentDetails_Label36",false);
						formObject.setLocked("cmplx_EmploymentDetails_EmpName",true);
						formObject.setLocked("cmplx_EmploymentDetails_EMpCode",true);
						formObject.setLocked("cmplx_EmploymentDetails_LOS",true);
						Field_employment();
					}

					if (pEvent.getSource().getName().equalsIgnoreCase("MiscellaneousFields")) {
						formObject.setLocked("cmplx_MiscFields_School",true);
						formObject.setLocked("cmplx_MiscFields_PropertyType",true);
						formObject.setLocked("cmplx_MiscFields_RealEstate",true);
						formObject.setLocked("cmplx_MiscFields_FarmEmirate",true);
					}


					if (pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo")) {

						loadPicklistELigibiltyAndProductInfo();
						formObject.setNGValue("cmplx_EligibilityAndProductInfo_RepayFreq","Monthly");
						//formObject.setVisible("ELigibiltyAndProductInfo_Label39",false);
						//formObject.setVisible("cmplx_EligibilityAndProductInfo_instrumenttype",false);
						formObject.setVisible("ELigibiltyAndProductInfo_Label1",false);
						formObject.setVisible("cmplx_EligibilityAndProductInfo_TakeoverAMount",false);
						formObject.setVisible("ELigibiltyAndProductInfo_Label2",false);
						formObject.setVisible("cmplx_EligibilityAndProductInfo_takeoverBank",false);
						formObject.setVisible("ELigibiltyAndProductInfo_Label6",false);
						formObject.setVisible("ELigibiltyAndProductInfo_Label7",false);


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

						// temp change for new acc creation
						formObject.setNGValue("cmplx_EligibilityAndProductInfo_ProdPrefRate","12");
						formObject.setNGValue("cmplx_EligibilityAndProductInfo_FinalLimit","500");
						formObject.setNGValue("cmplx_EligibilityAndProductInfo_BAseRate","5");	
						formObject.setNGValue("cmplx_EligibilityAndProductInfo_MArginRate","5.5");
						formObject.setNGValue("cmplx_EligibilityAndProductInfo_LPF","10");
						formObject.setNGValue("cmplx_EligibilityAndProductInfo_LPFAmount","1000");
						formObject.setNGValue("cmplx_EligibilityAndProductInfo_NumberOfInstallment","10");
						formObject.setNGValue("cmplx_EligibilityAndProductInfo_Insurance","6.5");
						formObject.setNGValue("cmplx_EligibilityAndProductInfo_InsuranceAmount","2000");

					}


					if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails")) {
						loadPicklist_Address();
					}

					if (pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails")) {
						
						if(formObject.getNGValue("Product_Type").equalsIgnoreCase("Credit Card") && formObject.getNGValue("AlternateContactDetails_carddispatch").equalsIgnoreCase("Courier")){
							formObject.setVisible("AlternateContactDetails_custdomicile",false);
							formObject.setVisible("AltContactDetails_Label14",false);
						}
					}

					if (pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails")) {
						LoadPickList("nationality", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Country with (nolock)");
						LoadPickList("gender", "select '--Select--' union select convert(varchar, description) from NG_MASTER_gender with (nolock)");
						LoadPickList("ResdCountry", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Country with (nolock)");
						LoadPickList("relationship", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Relationship with (nolock)");

					}
					if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")) {
						LoadPickList("cmplx_FATCA_Category", "select '--Select--' union select convert(varchar, description) from NG_MASTER_category with (nolock)");
						LoadPickList("cmplx_FATCA_USRelation", " select '--Select--', ''  union select convert(varchar, description),code  from ng_master_usrelation order by code");
					}

					if (pEvent.getSource().getName().equalsIgnoreCase("OECD")) {
						LoadPickList("OECD_CountryBirth", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Country with (nolock)");
						LoadPickList("OECD_townBirth", "select '--Select--' union select convert(varchar, description) from NG_MASTER_city with (nolock)");
						LoadPickList("OECD_CountryTaxResidence", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Country with (nolock)");
					}


					if (pEvent.getSource().getName().equalsIgnoreCase("CC_Loan")){
						formObject.setLocked("cmplx_CC_Loan_mchequeno",true);
						formObject.setLocked("cmplx_CC_Loan_mchequeDate",true);
						formObject.setLocked("cmplx_CC_Loan_mchequestatus",true);
						loadPicklist_ServiceRequest();
					}

					if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
						
						String query="Select count(*) from ng_RLOS_CUSTEXPOSE_AcctDetails where AcctType='CURRENT ACCOUNT' AND Wi_Name='"+formObject.getWFWorkitemName()+"'";
						List<List<String>> AccountCount= formObject.getDataFromDataSource(query);
						SKLogger.writeLog("RLOS", "Query is: "+query+" Value In AccountCount is "+AccountCount);
						loadPicklist3();
						if((formObject.getNGValue("cmplx_Customer_NTB").equals("true") || AccountCount.get(0).get(0).equals("0")) && formObject.getNGValue("Product_Type").contains("Personal Loan")){
							SKLogger.writeLog("RLOS"," Product Type Is: "+formObject.getNGValue("Product_Type"));
							formObject.setVisible("DecisionHistory_Button3",true );
							formObject.setNGValue("AccountCount", AccountCount.get(0).get(0));	
						}
						formObject.setNGValue("cmplx_DecisionHistory_Decision", "--Select--");
						/*if((formObject.getNGValue("Product_Type"))=="Credit Card"){
                          formObject.setVisible("DecisionHistory_Button3",false );
                    }*/

					}
				}

				break;


			case VALUE_CHANGED:
				SKLogger.writeLog("RLOS VAL Change eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

				if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_Customer_DOb")){
					SKLogger.writeLog("RLOS val change ", "Value of dob is:"+formObject.getNGValue("cmplx_Customer_DOb"));
					getAge(formObject.getNGValue("cmplx_Customer_DOb"));
				}	


				if (pEvent.getSource().getName().equalsIgnoreCase("ReqProd")){
					ReqProd=formObject.getNGValue("ReqProd");
					SKLogger.writeLog("RLOS val change ", "Value of ReqProd is:"+ReqProd);
					loadPicklistProduct(ReqProd);
				}

				if (pEvent.getSource().getName().equalsIgnoreCase("SubProd")){
					SKLogger.writeLog("RLOS val change ", "Value of SubProd is:"+formObject.getNGValue("SubProd"));
					formObject.clear("AppType");
					formObject.setNGValue("AppType","--Select--");
					LoadPickList("AppType", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from ng_master_ApplicationType with (nolock) where subProdFlag='"+formObject.getNGValue("SubProd")+"' order by code");

					if(formObject.getNGValue("SubProd").equalsIgnoreCase("BTC"))
						formObject.setNGValue("EmpType","Self Employed");
					/*
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

					else if(formObject.getNGValue("SubProd").equalsIgnoreCase("National Personal Loans") || formObject.getNGValue("SubProd").equalsIgnoreCase("NAT"))
						LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='NAT'");

					else if(formObject.getNGValue("SubProd").equalsIgnoreCase("Pre-Approved"))
						LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='PA'");

					else if(formObject.getNGValue("SubProd").equalsIgnoreCase("Product Upgrade"))
						LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='PU'");
					 */
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

				if (pEvent.getSource().getName().equalsIgnoreCase("Product_type")){

					String ProdType=formObject.getNGValue("Product_type");
					SKLogger.writeLog("RLOS Value Change Prod_Type",ProdType);
					formObject.clear("CardProd");
					formObject.setNGValue("CardProd","--Select--");
					if(ProdType.equalsIgnoreCase("Conventional"))
						LoadPickList("CardProd", "select '--Select--','' as code union select convert(varchar,description),code from ng_MASTER_CardProduct with (nolock) where reqProduct='Conventional' order by code");

					if(ProdType.equalsIgnoreCase("Islamic"))
						LoadPickList("CardProd", "select '--Select--','' as code union select convert(varchar,description),code from ng_MASTER_CardProduct with (nolock) where reqProduct='Islamic' order by code");
				}

				if(pEvent.getSource().getName().equalsIgnoreCase("cardNo")){
					new AesUtil();
					String encryp_CardNo=AesUtil.Encrypt(formObject.getNGValue("cardNo"));
					formObject.setNGValue("encrypt_CardNo",formObject.getNGValue("cardNo"));
					SKLogger.writeLog("RLOS Value Of Encrypted Card No:",encryp_CardNo);
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
		finally{System.out.println("OK");}
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
	public void saveFormStarted(FormEvent pEvent) 
	{
		SKLogger.writeLog("RLOS Initiation", "Inside saveFormStarted()" + pEvent);
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setNGValue("CIFID", formObject.getNGValue("cmplx_Customer_CIFNO"));
		SKLogger.writeLog("RLOS Initiation", "CIFID is:" +  formObject.getNGValue("cmplx_Customer_CIFNO"));
		formObject.setNGValue("Created_By",formObject.getUserName());
		formObject.setNGValue("PROCESS_NAME", "RLOS");
		formObject.setNGValue("initiationChannel", "Branch_Init");
		ParentToChild();
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
		TypeOfProduct();
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
	public void setcustomer_enable(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setLocked("cmplx_Customer_Title", false);
		formObject.setLocked("cmplx_Customer_ResidentNonResident", false);
		formObject.setLocked("cmplx_Customer_gender", false);
		formObject.setLocked("cmplx_Customer_MotherName", false);
		formObject.setLocked("cmplx_Customer_VisaNo", false);
		formObject.setLocked("cmplx_Customer_MAritalStatus", false);
		formObject.setLocked("cmplx_Customer_COuntryOFResidence", false);
		formObject.setLocked("cmplx_Customer_SecNAtionApplicable", false);
		formObject.setLocked("cmplx_Customer_SecNationality", false);
		formObject.setLocked("cmplx_Customer_EMirateOfVisa", false);
		formObject.setLocked("cmplx_Customer_EmirateOfResidence", false);
		formObject.setLocked("cmplx_Customer_yearsInUAE", false);
		formObject.setLocked("cmplx_Customer_CustomerCategory", false);
		formObject.setLocked("cmplx_Customer_GCCNational", false);
		formObject.setEnabled("cmplx_Customer_VIPFlag", true);
		//formObject.setLocked("cmplx_Customer_PassPortExpiry", false);
		//formObject.setLocked("cmplx_Customer_VIsaExpiry", false);
	}
}

