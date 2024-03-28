package com.newgen.omniforms.user;
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
import java.util.Calendar;
import java.util.HashMap;

import javax.faces.validator.ValidatorException;


public class RLOS_Telesales_Initiation extends RLOSCommon implements FormListener
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
	            System.out.println("Inside initiation RLOS");
	            SKLogger.writeLog("RLOS Initiation", "Inside formPopulated()" + pEvent.getSource());
	            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
	            String currDate = format.format(Calendar.getInstance().getTime());
	             SKLogger.writeLog("RLOS Initiation", "currTime:" + currDate);
	           // formObject.setNGValue("Intro_Date",currDate);
	            formObject.setNGValue("Intro_Date",formObject.getNGValue("Created_Date"));
	            
	            formObject.setNGValue("initiationChannel","Telesales_Init");
	            formObject.setNGValue("Channel_Name","Telesales Initiation");
	             
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
			SKLogger.writeLog("RLOS Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			
			try
			{
				switch (pEvent.getType()) 
				{		
				case FRAME_EXPANDED:
					SKLogger.writeLog("RLOS FRAG LOADED eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		
					if (pEvent.getSource().getName().equalsIgnoreCase("CustomerDetails")) 
					{
						hm.put("CustomerDetails","Clicked");
						popupFlag="N";
						if(!formObject.isVisible("Customer_Frame1"))
							formObject.fetchFragment("CustomerDetails", "Customer", "q_Customer");
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
						
						
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails")) 
					{
						hm.put("GuarantorDetails","Clicked");
						popupFlag="N";
						formObject.fetchFragment("GuarantorDetails", "GuarantorDetails", "q_GuarantorDetails");
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
						
						
		
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("ProductDetailsLoader")) {
						
							SKLogger.writeLog("RLOS", "Inside ProductDetailsLoader ");
							hm.put("ProductDetailsLoader","Clicked");
							popupFlag="N";
							formObject.fetchFragment("ProductDetailsLoader", "Product", "q_Product");

							try
							{
								throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
							}
							finally{hm.clear();}
							
					}
						
					
		
					if (pEvent.getSource().getName().equalsIgnoreCase("EmploymentDetails")) 
					{				
						if(!formObject.isVisible("EMploymentDetails_Frame1"))
						{							
							hm.put("EmploymentDetails","Clicked");
							popupFlag="N";
							formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmploymentDetails");
							
							if(!formObject.isVisible("Product_Frame1")){
								formObject.fetchFragment("ProductDetailsLoader", "Product", "q_Product");
								formObject.setNGFrameState("ProductDetailsLoader", 0);
								formObject.setNGFrameState("ProductDetailsLoader", 1);
							}
							
							
							try
							{
								throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
							}
							finally{hm.clear();
							}
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
						
						
					
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}					
					}
					
		
					if (pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails")) 
					{
						hm.put("CompanyDetails","Clicked");
						popupFlag="N";
						formObject.fetchFragment("CompanyDetails", "CompanyDetails", "q_CompanyDetails");
						
						int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
						for(int i=0;i<n;i++){
							if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("Business titanium Card") && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")){
								formObject.setVisible("CompanyDetails_Label8", true);
								formObject.setVisible("CompanyDetails_Text4", true);//Effective length of buss
								break;
							}
							
							else if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("Self Employed Credit Card") && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")){
								formObject.setVisible("CompanyDetails_Label27", true);
								formObject.setVisible("CompanyDetails_Combo13", true);//Emirate of work
								formObject.setVisible("CompanyDetails_Label29", true);
								formObject.setVisible("CompanyDetails_Combo15", true);
								formObject.setVisible("CompanyDetails_Label28", true);
								formObject.setVisible("CompanyDetails_Text5", true);
								break;
							}
							else{
								formObject.setVisible("CompanyDetails_Label27", false);
								formObject.setVisible("CompanyDetails_Combo13", false);//Emirate of work
								formObject.setVisible("CompanyDetails_Label29", false);
								formObject.setVisible("CompanyDetails_Combo15", false);
								formObject.setVisible("CompanyDetails_Label28", false);
								formObject.setVisible("CompanyDetails_Text5", false);
								formObject.setVisible("CompanyDetails_Label8", false);
								formObject.setVisible("CompanyDetails_Text4", false);
							}
							
						}	

							SKLogger.writeLog("RLOS", "CompanyDetailse1:");
						try
						{
							SKLogger.writeLog("RLOS", "CompanyDetailse1222222:");
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}					
						
					}
					 
					
					if (pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignatoryDetails")) 
					{
						hm.put("AuthorisedSignatoryDetails","Clicked");
						popupFlag="N";
						formObject.fetchFragment("AuthorisedSignatoryDetails", "AuthorisedSignDetails", "q_AuthorisedSignDetails");
						SKLogger.writeLog("RLOS", "AuthorisedSignatoryDetails:");
						try
						{
							SKLogger.writeLog("RLOS", "AuthorisedSignatoryDetails:");
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}					
						
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails")) 
					{
						hm.put("PartnerDetails","Clicked");
						popupFlag="N";
						formObject.fetchFragment("PartnerDetails", "PartnerDetails", "q_PartnerDetails");
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}				
					}
								
					if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistoryContainer"))
					{
						hm.put("DecisionHistoryContainer","Clicked");
						popupFlag="N";
						formObject.fetchFragment("DecisionHistoryContainer", "DecisionHistory", "q_DecisionHistory");
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}					
					}
		
					if (pEvent.getSource().getName().equalsIgnoreCase("MiscFields")) 
					{
						hm.put("MiscFields","Clicked");
						popupFlag="N";
						formObject.fetchFragment("MiscFields", "MiscellaneousFields", "q_MiscFields");
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
						
					}	
		
					if (pEvent.getSource().getName().equalsIgnoreCase("EligibilityAndProductInformation"))
					{
						
						hm.put("EligibilityAndProductInformation","Clicked");
						popupFlag="N";
						formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligAndProductInfo");
						Fields_Eligibility();
						int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
						
						if(n>0)
							for(int i=0;i<n;i++){
								if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 5).toUpperCase().contains("SECURED")){
									formObject.setVisible("ELigibiltyAndProductInfo_Frame3", true);
									SKLogger.writeLog("RLOS", "Lein Details now Visible...!!!");
									break;
								}
								else 
									formObject.setVisible("ELigibiltyAndProductInfo_Frame3", false);

								if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 1).equalsIgnoreCase("Personal Loan")){
									formObject.setVisible("ELigibiltyAndProductInfo_Frame2", true);
									SKLogger.writeLog("RLOS", "Funding Account Details now Visible...!!!");
									break;
								}
								else 
								formObject.setVisible("ELigibiltyAndProductInfo_Frame2", false);
							}
					
						else{
							formObject.setVisible("ELigibiltyAndProductInfo_Frame2", false);
							formObject.setVisible("ELigibiltyAndProductInfo_Frame3", false);
							}
						
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}					
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
						Fields_ServiceRequest();
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
					
					if (pEvent.getSource().getName().equalsIgnoreCase("CustomerDocumentsContainer"))
					{
						hm.put("CustomerDocumentsContainer","Clicked");
						popupFlag="N";
						formObject.fetchFragment("CustomerDocumentsContainer", "CustomerDocument", "q_CustomerDocument");
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
						
					}	
					
					if (pEvent.getSource().getName().equalsIgnoreCase("OutgoingDocumentsContainer"))
					{
						hm.put("OutgoingDocumentsContainer","Clicked");
						popupFlag="N";
						formObject.fetchFragment("OutgoingDocumentsContainer", "CorrespondenceDocument", "q_correspondenceDocument");
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
						
					}	
		
					
					if (pEvent.getSource().getName().equalsIgnoreCase("DecisioningFields"))
					{
						hm.put("DecisioningFields","Clicked");
						popupFlag="N";
						formObject.fetchFragment("DecisioningFields", "DecisionFields", "q_DecisionFields");
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}					
					}	
		
					if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistoryContainer"))
					{
						hm.put("DecisionHistoryContainer","Clicked");
						popupFlag="N";
						formObject.fetchFragment("DecisionHistoryContainer", "DecisionHistory", "q_DecisionHistory");
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}					
					}	
		
					if (pEvent.getSource().getName().equalsIgnoreCase("DeviationHistoryContainer"))
					{
						hm.put("DeviationHistoryContainer","Clicked");
						popupFlag="N";
						formObject.fetchFragment("DeviationHistoryContainer", "DeviationHistory", "q_DeviationHistory");
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
					  
				        if (pEvent.getSource().getName().equalsIgnoreCase("DeferralDocuments")) 
				        {
							hm.put("DeferralDocuments","Clicked");
							popupFlag="N";
							SKLogger.writeLog("RLOS Initiation eventDispatched()","DeferralDocuments");
				            formObject.fetchFragment("DeferralDocuments", "DeferralDocName", "q_DeferralDoc");
				            loadAllCombo_RLOS_Documents_Deferral();
				            SKLogger.writeLog("RLOS Initiation eventDispatched()","formObject.fetchFragment2");
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
					
					 if(pEvent.getSource().getName().equalsIgnoreCase("Reference_Add"))
					 {
						 formObject.setNGValue("reference_wi_name",formObject.getWFWorkitemName());
						SKLogger.writeLog("RLOS", "Inside add button: "+formObject.getNGValue("reference_wi_name"));
						formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Customer_cmplx_GR_ReferenceDetailsGrid");
					 }
					 
					 if(pEvent.getSource().getName().equalsIgnoreCase("Reference__modify"))
					 {
						 
						formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Customer_cmplx_GR_ReferenceDetailsGrid");
					 }
					 
					 if(pEvent.getSource().getName().equalsIgnoreCase("Reference_delete"))
					 {
						
						formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_Customer_cmplx_GR_ReferenceDetailsGrid");
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
								
						formObject.setNGValue("Grid_wi_name",formObject.getWFWorkitemName());
						SKLogger.writeLog("RLOS", "Inside add button: "+formObject.getNGValue("Grid_wi_name"));
						formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Product_cmplx_ProductGrid");
						
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
					}	
					
					
					if (pEvent.getSource().getName().equalsIgnoreCase("Modify")){
						
						formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Product_cmplx_ProductGrid");
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
						
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("Delete")){
						formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_Product_cmplx_ProductGrid");
						
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
					
					if (pEvent.getSource().getName().equalsIgnoreCase("Product_Add")){
						formObject.setNGValue("gr2_winame",formObject.getWFWorkitemName());	
						SKLogger.writeLog("RLOS", "Inside product_add button: "+formObject.getNGValue("gr2_winame"));
						formObject.ExecuteExternalCommand("NGAddRow", "cmplx_ExternalLiabilities_cmplx_ProdGrid");
						}
					
					
					if (pEvent.getSource().getName().equalsIgnoreCase("Product_Modify")){
						formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_ExternalLiabilities_cmplx_ProdGrid");
		
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("Product_Delete")){
						formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_ExternalLiabilities_cmplx_ProdGrid");
		
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
					
					
					if (pEvent.getSource().getName().equalsIgnoreCase("ExternalLiabilities_Button1"))
					{
					hm.put("ExternalLiabilities_Button1","Clicked");
					popupFlag="N";
					if(formObject.getNGValue("cmplx_Customer_NTB")=="true")
					{
						SKLogger.writeLog("complex customer",formObject.getNGValue("cmplx_Customer_NTB"));
						outputResponse = GenerateXML("FINANCIAL_SUMMARY","");
						ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
						SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
						ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";	
						SKLogger.writeLog("RLOS value of ReturnDesc",ReturnDesc);
						if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000") && ReturnDesc.equalsIgnoreCase("Success") || ReturnDesc.equalsIgnoreCase("Successful")){
								valueSetCustomer(outputResponse , "");
								try
								{
									String Date1=formObject.getNGValue("cmplx_Customer_DOb");
				                    SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-mm-dd");
				                    SimpleDateFormat sdf2=new SimpleDateFormat("dd/mm/yyyy");
				                    String Datechanged=sdf2.format(sdf1.parse(Date1));
				                    SKLogger.writeLog("RLOS value ofDatechanged",Datechanged);
				                    formObject.setNGValue("cmplx_Customer_DOb",Datechanged);
								}		
								catch(Exception ex){	                        
			                    }

								formObject.setNGValue("Is_Financial_Summary","Y");
							}
							else{
								formObject.setNGValue("Is_Financial_Summary","N");
							}
					}
					else if(formObject.getNGValue("cmplx_Customer_NTB")=="false"){
						outputResponse = GenerateXML("ACCOUNT_SUMMARY","");
						ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
						SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
						ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";	
						SKLogger.writeLog("RLOS value of ReturnDesc",ReturnDesc);
							if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000") && ReturnDesc.equalsIgnoreCase("Success") || ReturnDesc.equalsIgnoreCase("Successful")){
								valueSetCustomer(outputResponse , "");
								formObject.setNGValue("Is_Account_Summary","Y");
							}
							else{
								formObject.setNGValue("Is_Account_Summary","N");
							}
						SKLogger.writeLog("RLOS value of Account Summary",formObject.getNGValue("Is_Account_Summary"));
						outputResponse = GenerateXML("CUSTOMER_DETAILS","");
						ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
						SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
						ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";	
						SKLogger.writeLog("RLOS value of ReturnDesc",ReturnDesc);
						if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000") && ReturnDesc.equalsIgnoreCase("Success") || ReturnDesc.equalsIgnoreCase("Successful")){
								valueSetCustomer(outputResponse , "");
								formObject.setNGValue("Is_Customer_Details","Y");
							}
							else{
								formObject.setNGValue("Is_Customer_Details","N");
							}
						SKLogger.writeLog("RLOS value of Customer Details",formObject.getNGValue("Is_Customer_Details"));
						if(formObject.getNGValue("Is_Account_Summary").equalsIgnoreCase("Y") && formObject.getNGValue("Is_Customer_Details").equalsIgnoreCase("Y")){ 
							SKLogger.writeLog("RLOS value of Is_Customer_Details","inside if condition");
							formObject.setEnabled("ExternalLiabilities_Button1", false);	
						}
						else{
							formObject.setEnabled("ExternalLiabilities_Button1", true);
						}
					}
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
						if(formObject.getNGValue("cmplx_Customer_NTB")=="true"){
						//createCustomer();
						outputResponse = GenerateXML("NEW_ACCOUNT_REQ","");
						ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
						SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
						ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";	
						SKLogger.writeLog("RLOS value of ReturnDesc",ReturnDesc);
						if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000") && ReturnDesc.equalsIgnoreCase("Success") || ReturnDesc.equalsIgnoreCase("Successful")){
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
						ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";	
						SKLogger.writeLog("RLOS value of ReturnDesc",ReturnDesc);
						if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000") && ReturnDesc.equalsIgnoreCase("Success") || ReturnDesc.equalsIgnoreCase("Successful")){
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
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}					
				     }
					
					
					if (pEvent.getSource().getName().equalsIgnoreCase("Account")){
						outputResponse = GenerateXML("FINANCIAL_SUMMARY","");
						ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
						SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
						ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";	
						SKLogger.writeLog("RLOS value of ReturnDesc",ReturnDesc);
						if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000") && ReturnDesc.equalsIgnoreCase("Success") || ReturnDesc.equalsIgnoreCase("Successful")){
							valueSetCustomer(outputResponse , "");	
							formObject.setNGValue("Is_Financial_Summary","Y");
							}
							else{
								formObject.setNGValue("Is_Financial_Summary","N");
							}
						SKLogger.writeLog("RLOS value of Financial Summary",formObject.getNGValue("Is_Financial_Summary"));
						
						if(formObject.getNGValue("Is_Financial_Summary").equalsIgnoreCase("Y")){ 
							SKLogger.writeLog("RLOS value of Is_Financial_Summary","inside if condition");
							formObject.setEnabled("Account", false);	
						}
						else{
							formObject.setEnabled("Account", true);
						}
		            }
				   
					if(pEvent.getSource().getName().equalsIgnoreCase("ReadFromCard")){
		                   outputResponse = GenerateXML("EID_Genuine","");
		                   ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
		                   SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
		                   ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
		                   SKLogger.writeLog("RLOS value of ReturnDesc",ReturnDesc);
		                   if((ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")) && (ReturnDesc.equalsIgnoreCase("Success") || ReturnDesc.equalsIgnoreCase("Successful"))){
		                       valueSetCustomer(outputResponse , "");    
		                       SKLogger.writeLog("RLOS value of EID_Genuine","EID is generated");
		                       }
		                       else{
		                           SKLogger.writeLog("EID_Genuine","EID is not generated");
		                       }
		                }



					if(pEvent.getSource().getName().equalsIgnoreCase("FetchDetails"))
	                {
	                    
						//if(formObject.getNGValue("cmplx_Customer_CardNotAvailable")=="true")                                    
	                    hm.put("Validate_OTP_Btn","Clicked");
	                    popupFlag="N";
	                    formObject.setNGValue("cmplx_Customer_ISFetchDetails", "Y");
	                    outputResponse = GenerateXML("ENTITY_DETAILS","");
	                    ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
	                    SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
	                    ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
	                    SKLogger.writeLog("RLOS value of ReturnDesc",ReturnDesc);
	                    if((ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")) && (ReturnDesc.equalsIgnoreCase("Success") || ReturnDesc.equalsIgnoreCase("Successful"))){
	                        valueSetCustomer(outputResponse , "");    
	                        formObject.setNGValue("Is_Entity_Details","Y");
	                        }
	                        else{
	                            formObject.setNGValue("Is_Entity_Details","N");
	                        }
	                    SKLogger.writeLog("RLOS value of Entity Details",formObject.getNGValue("Is_Entity_Details"));
	                   
	                outputResponse =GenerateXML("CUSTOMER_ELIGIBILITY","");
	                SKLogger.writeLog("RLOS value of ReturnDesc","Customer Eligibility");
	                    ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
	                    SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
	                    ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
	                    SKLogger.writeLog("RLOS value of ReturnDesc",ReturnDesc);
	                    if((ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")) && (ReturnDesc.equalsIgnoreCase("Success") || ReturnDesc.equalsIgnoreCase("Successful"))){
	                        valueSetCustomer(outputResponse , "");    
	                        formObject.setNGValue("Is_Customer_Eligibility","Y");
	                        formObject.setNGValue("BlacklistFlag","Y");
	                        formObject.setNGValue("DuplicationFlag","Y");
	                        formObject.setNGValue("IsAcctCustFlag","Y");
	                        
	                        }
	                        else{
	                            formObject.setNGValue("Is_Customer_Eligibility","N");
	                        }
	                        SKLogger.writeLog("RLOS value of Customer Details",formObject.getNGValue("Is_Customer_Eligibility"));
	                        SKLogger.writeLog("RLOS value of BlacklistFlag",formObject.getNGValue("BlacklistFlag"));
	                        SKLogger.writeLog("RLOS value of DuplicationFlag",formObject.getNGValue("DuplicationFlag"));
	                        SKLogger.writeLog("RLOS value of IsAcctCustFlag",formObject.getNGValue("IsAcctCustFlag"));
	                        //added here
	                                outputResponse = GenerateXML("CUSTOMER_DETAILS","");
	                                try
	                                {
	                                    String Date1=formObject.getNGValue("cmplx_Customer_DOb");
	                                    SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-mm-dd");
	                                    SimpleDateFormat sdf2=new SimpleDateFormat("dd/mm/yyyy");
	                                    String Datechanged=sdf2.format(sdf1.parse(Date1));
	                                    SKLogger.writeLog("RLOS value ofDatechanged",Datechanged);
	                                    formObject.setNGValue("cmplx_Customer_DOb",Datechanged);
	                                }        
	                                catch(Exception ex){                            
	                                }
	 
	                        SKLogger.writeLog("RLOS value of ReturnCode","Inside Customer");
	                        Gender =  (outputResponse.contains("<Gender>")) ? outputResponse.substring(outputResponse.indexOf("<Gender>")+"</Gender>".length()-1,outputResponse.indexOf("</Gender>")):"";
	                        SKLogger.writeLog("RLOS value of Gender",Gender);
	                        if(Gender.equalsIgnoreCase("M")){
	                            formObject.setNGValue("cmplx_Customer_gender","Male");
	                        }
	                        else{
	                            formObject.setNGValue("cmplx_Customer_gender","Female");
	                        }
	                        SKLogger.writeLog("RLOS value of Gender1","outside if of gender");
	                        ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
	                        SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
	                        ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
	                        SKLogger.writeLog("RLOS value of ReturnDesc",ReturnDesc);
	                        if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000") && ReturnDesc.equalsIgnoreCase("Success") || ReturnDesc.equalsIgnoreCase("Successful")){
	                        		formObject.fetchFragment("Address_Details_Container", "AddressDetails","q_AddressDetails");    
	                        		valueSetCustomer(outputResponse , "");
	                                formObject.setNGValue("Is_Customer_Details","Y");
	                            }
	                            else{
	                                formObject.setNGValue("Is_Customer_Details","N");
	                            }
	                        SKLogger.writeLog("RLOS value of Customer Details",formObject.getNGValue("Is_Customer_Details"));
	                        
	                        //ended here
	                        if(formObject.getNGValue("Is_Customer_Eligibility").equalsIgnoreCase("Y") && formObject.getNGValue("Is_Entity_Details").equalsIgnoreCase("Y")&& formObject.getNGValue("Is_Customer_Details").equalsIgnoreCase("Y"))
	                        { 
	                            SKLogger.writeLog("RLOS value of Customer Details","inside if condition");
	                            formObject.setEnabled("FetchDetails", false);    
	                        }
	                        else{
	                            formObject.setEnabled("FetchDetails", true);
	                        }
	                    try
	                    {
	                        throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
	                    }
	                    finally{hm.clear();}
	                        
	                }
				
					if(pEvent.getSource().getName().equalsIgnoreCase("Send_OTP_Btn"))
		               {
		                   hm.put("Send_OTP_Btn","Clicked");
		                   popupFlag="N";
		                    outputResponse = GenerateXML("OTP_MANAGEMENT","");
		                       ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
		                       SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
		                       ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
		                       SKLogger.writeLog("RLOS value of ReturnDesc",ReturnDesc);
		                       if((ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")) && (ReturnDesc.equalsIgnoreCase("Success") || ReturnDesc.equalsIgnoreCase("Successful"))){
		                           valueSetCustomer(outputResponse , "");    
		                           SKLogger.writeLog("RLOS value of OTP_Generation","OTP is generated");
		                           formObject.setEnabled("OTP_No",true);
		                           formObject.setEnabled("Validate_OTP_Btn",true);
		                           }
		                           else{
		                               formObject.setNGValue("OTP_Generation","OTP is not generated");
		                               formObject.setEnabled("OTP_No",false);
		                               formObject.setEnabled("Validate_OTP_Btn",false);
		                           }
		                       SKLogger.writeLog("RLOS value of OTP_Generation","OTP generation");
		                   try
		                   {
		                       throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
		                   }
		                   finally{hm.clear();}
		               }

		             

			
				 
					  if(pEvent.getSource().getName().equalsIgnoreCase("Validate_OTP_Btn"))
		                {
		                    hm.put("Validate_OTP_Btn","Clicked");
		                    popupFlag="N";
		                     outputResponse = GenerateXML("OTP_MANAGEMENT","");
		                        ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
		                        SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
		                        ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
		                        SKLogger.writeLog("RLOS value of ReturnDesc",ReturnDesc);
		                        if((ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000")) && (ReturnDesc.equalsIgnoreCase("Success") || ReturnDesc.equalsIgnoreCase("Successful"))){
		                            valueSetCustomer(outputResponse , "");    
		                            SKLogger.writeLog("RLOS value of OTP_Generation","OTP is generated");
		                            }
		                            else{
		                                formObject.setNGValue("OTP_Generation","OTP is not generated");
		                            }
		                        SKLogger.writeLog("RLOS value of OTP_Generation","OTP generation");
		                    try
		                    {
		                        throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
		                    }
		                    finally{hm.clear();}
		                }
		        
		 

					
					
					if (pEvent.getSource().getName().equalsIgnoreCase("Button9")){
		
						if(!formObject.isVisible("IncomeDetails_Frame1")){
							formObject.fetchFragment("Incomedetails", "IncomeDetails", "q_IncomeDetails");
							formObject.setNGFrameState("Incomedetails", 0);
							formObject.setNGFrameState("Incomedetails", 1);
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
						String EmpName=formObject.getNGValue("cmplx_EmploymentDetails_EmpName");
						String EmpCode=formObject.getNGValue("cmplx_EmploymentDetails_EMpCode");
						SKLogger.writeLog("RLOS", "EMpName$"+EmpName+"$");
						String query=null;
						if(EmpName.trim().equalsIgnoreCase(""))
							query="select distinct(EmpName),EmpCode,designation,DesignationVisa,LOS,CurrEmployer from ng_RLOS_EmpDetails where EmpCode Like '%"+EmpCode+"%'";
		               
						else
							query="select distinct(EmpName),EmpCode,designation,DesignationVisa,LOS,CurrEmployer from ng_RLOS_EmpDetails where EmpName Like '%"+EmpName + "%' or EmpCode Like '%"+EmpCode+"'";
		
						 SKLogger.writeLog("RLOS", "query is: "+query);
							populatePickListWindow(query,"EMploymentDetails_Button1", "Employer Name,Employer Code,Designation,Designation Visa,LOS,Current Employer", true, 20);
			              
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
			                outputResponse = GenerateXML("ACCOUNT_SUMMARY","");
			                ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			                SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
			                ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
			                SKLogger.writeLog("RLOS value of ReturnDesc",ReturnDesc);
			                    if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000") && ReturnDesc.equalsIgnoreCase("Success") || ReturnDesc.equalsIgnoreCase("Successful")){
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
					   	if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Button3"))
			            {
			                hm.put("Liability_New_Button1","Clicked");
			                popupFlag="N";
			                //added
			                outputResponse = GenerateXML("NEW_ACCOUNT_REQ","");
			                ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			                SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
			                ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
			                SKLogger.writeLog("RLOS value of ReturnDesc",ReturnDesc);
			                if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000") && ReturnDesc.equalsIgnoreCase("Success") || ReturnDesc.equalsIgnoreCase("Successful")){
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
			                
			                
			                //for new_customer_request call
			                //started
			                  outputResponse = GenerateXML("NEW_CUSTOMER_REQ","");
			                    ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			                    SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);
			                    ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
			                    SKLogger.writeLog("RLOS value of ReturnDesc",ReturnDesc);
			                    if(ReturnCode.equalsIgnoreCase("0000") || ReturnCode.equalsIgnoreCase("000") && ReturnDesc.equalsIgnoreCase("Success") || ReturnDesc.equalsIgnoreCase("Successful")){
			                        valueSetCustomer(outputResponse , "");    
			                        SKLogger.writeLog("RLOS value of ReturnDesc","Inside if of New customer Req");
			                        }
			                        else{
			                            SKLogger.writeLog("RLOS value of ReturnDesc","Inside else of New Customer Req");
			                        }
					   try
		                {
		                    throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
		                }
		                finally{hm.clear();}
		            }
	        
						
					if(pEvent.getSource().getName().equalsIgnoreCase("Customer_save")){
						formObject.saveFragment("CustomerDetails");
					}
					
					if(pEvent.getSource().getName().equalsIgnoreCase("Product_Save")){
						formObject.saveFragment("ProductDetailsLoader");
					}
					
					if(pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails_Save")){
						formObject.saveFragment("GuarantorDetails");
					}
					
					if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_Salaried_Save")){
						formObject.saveFragment("Incomedetails");
					}
					
					if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_SelfEmployed_Save")){
						formObject.saveFragment("Incomedetails");
					}
					
					if(pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails_Save")){
						formObject.saveFragment("CompanyDetails");
					}
					
				
					if(pEvent.getSource().getName().equalsIgnoreCase("Liability_New_Save")){
						formObject.saveFragment("Liability_container");
					}
					
					if(pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails_Save")){
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
					
					if(pEvent.getSource().getName().equalsIgnoreCase("ContactDetails_Save")){
						formObject.saveFragment("Alt_Contact_container");
					}
					
					if(pEvent.getSource().getName().equalsIgnoreCase("CardDetails_save")){
						formObject.saveFragment("CardDetails");
					}
					
					if(pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails_Save")){
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
					
					if(pEvent.getSource().getName().equalsIgnoreCase("ServiceRequest_Save")){
						formObject.saveFragment("CC_Loan_container");
					}
					
					if(pEvent.getSource().getName().equalsIgnoreCase("IncomingDoc_Save")){
						SKLogger.writeLog("RLOS", "TANSHU Inside IncomingDoc_Save button!!");
						formObject.saveFragment("IncomingDocuments");
					}
					
					if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Save")){
						formObject.saveFragment("DecisionHistoryContainer");
					}
					break;	
					
					
				case TAB_CLICKED:
					
					//SKLogger.writeLog("RLOS akshay TAB NAME: ",pEvent.getSource().getName());
					SKLogger.writeLog("RLOS akshay TAB select sheet caption: ",formObject.getSheetCaption(pEvent.getSource().getName(), formObject.getSelectedSheet(pEvent.getSource().getName())));
					//SKLogger.writeLog("RLOS akshay TAB select sheet: ",formObject.getSelectedSheet(pEvent.getSource().getName())+"");
					String SheetCaption=formObject.getSheetCaption(pEvent.getSource().getName(), formObject.getSelectedSheet(pEvent.getSource().getName()));
					
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
		
					if(SheetCaption.equalsIgnoreCase("Demographics Details")){
						if(!formObject.isVisible("Product_Frame1")){
							formObject.fetchFragment("ProductDetailsLoader", "Product", "q_Product");
							formObject.setNGFrameState("ProductDetailsLoader", 0);
							formObject.setNGFrameState("ProductDetailsLoader", 1);
						}
						SKLogger.writeLog("RLOS","Inside "+SheetCaption);
						int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
						if(n>0)
						{
							for(int i=0;i<n;i++){
								if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 1).equalsIgnoreCase("Credit Card")){
									formObject.setVisible("Supplementary_Container", true);
									//formObject.setVisible("CardDetails", true);
									formObject.setTop("FATCA", 1170);
									formObject.setTop("KYC", 1300);
									formObject.setTop("OECD", 1388);
									break;
								}
								else{
									formObject.setVisible("Supplementary_Container", false);
									//formObject.setVisible("CardDetails", false);
									formObject.setTop("FATCA", formObject.getTop("Supplementary_Container"));
									formObject.setTop("KYC", formObject.getTop("FATCA")+30);
									formObject.setTop("OECD", formObject.getTop("KYC")+30);
								}
								
							}
						}
						else{
							formObject.setVisible("Supplementary_Container", false);
							//formObject.setVisible("CardDetails", false);
							formObject.setTop("FATCA", formObject.getTop("Supplementary_Container"));
							formObject.setTop("KYC", formObject.getTop("FATCA")+30);
							formObject.setTop("OECD", formObject.getTop("KYC")+30);
						}
					}
		
					break;
				
				case FRAGMENT_LOADED:
					SKLogger.writeLog(" In RLOS Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
					if (pEvent.getSource().getName().equalsIgnoreCase("Customer")) {
						formObject.setLocked("Customer_Frame1",true);
						formObject.setHeight("Customer_Frame1", 780);
						formObject.setHeight("CustomerDetails", 790);
						formObject.setEnabled("Customer_save",true);
						SKLogger.writeLog("RLOS","Value Of Initiation Channel is:"+formObject.getNGValue("initiationChannel"));
		              
						formObject.setNGValue("cmplx_Customer_EmiratesID",formObject.getNGValue("EmirateID"));
						if(!formObject.getNGValue("initiationChannel").equalsIgnoreCase("M")){
						formObject.setLocked("Customer_Frame2",false);
						formObject.setLocked("Customer_Frame3",false);
						formObject.setVisible("Customer_Frame2", false);
						formObject.setLocked("cmplx_Customer_CardNotAvailable",false);
						formObject.setEnabled("ReadFromCard",true);
						formObject.setLocked("cmplx_Customer_NEP",false);
						formObject.setLocked("cmplx_Customer_DLNo",false);
						formObject.setLocked("cmplx_Customer_Passport2",false);
						formObject.setLocked("cmplx_Customer_Passport3",false);
						formObject.setLocked("cmplx_Customer_PAssport4",false);
						loadPicklistCustomer();           		 
						} 		
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
						LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
						LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct where activityName='"+formObject.getWFActivityName()+"'");

					}
						
					if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")) {
						
						formObject.setLocked("takeoverAMount",true);
						formObject.setLocked("cmplx_Liability_New_DBR",true);
						formObject.setLocked("cmplx_Liability_New_DBRNet",true);
						formObject.setLocked("cmplx_Liability_New_AggrExposure",true);
						formObject.setLocked("cmplx_Liability_New_TAI",true);
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) {
						
						formObject.setVisible("EMploymentDetails_Label25",false);
						formObject.setVisible("cmplx_EmploymentDetails_NepType",false);
						formObject.setVisible("cmplx_EmploymentDetails_Freezone",false);
						formObject.setVisible("EMploymentDetails_Label62",false);
						formObject.setVisible("cmplx_EmploymentDetails_FreezoneName",false);
						formObject.setVisible("tenancyContract",false);
						formObject.setVisible("EMploymentDetails_Label5",false);
						formObject.setLocked("cmplx_EmploymentDetails_EmpIndus",true);
						//formObject.setEnabled("cmplx_EmploymentDetails_DOJ",false);
						//formObject.setEnabled("cmplx_EmploymentDetails_LOS",false);
						formObject.setLocked("cmplx_EmploymentDetails_IndusSeg",true);
						//formObject.setEnabled("cmplx_EmploymentDetails_Designation",false);
						//formObject.setEnabled("cmplx_EmploymentDetails_IncInPL",false);
						//formObject.setEnabled("cmplx_EmploymentDetails_LOSPrevious",false);	
						//formObject.setEnabled("cmplx_EmploymentDetails_IncInCC",false);		
						formObject.setLocked("cmplx_EmploymentDetails_Freezone",true);
						formObject.setLocked("cmplx_EmploymentDetails_Kompass",true);
						loadPicklist4();
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("MiscellaneousFields")) {
						formObject.setLocked("cmplx_MiscFields_School",true);
						formObject.setLocked("cmplx_MiscFields_PropertyType",true);
						formObject.setLocked("cmplx_MiscFields_RealEstate",true);
						formObject.setLocked("cmplx_MiscFields_FarmEmirate",true);
					}
					
					
					if (pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo")) {
						
						formObject.setLocked("ELigibiltyAndProductInfo_Text7",true);//Final TAI
						formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalDBR",true);
						formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalLimit",true);
						/*formObject.setEnabled("cmplx_EligibilityAndProductInfo_InterestRate",false);
						formObject.setEnabled("cmplx_EligibilityAndProductInfo_EMI",false);
						formObject.setEnabled("cmplx_EligibilityAndProductInfo_NetPayout",false);
						formObject.setEnabled("cmplx_EligibilityAndProductInfo_MaturityDate",false);
						formObject.setEnabled("cmplx_EligibilityAndProductInfo_InterestType",false);
						formObject.setEnabled("cmplx_EligibilityAndProductInfo_BaseRateType",false);
						formObject.setEnabled("cmplx_EligibilityAndProductInfo_BAseRate",false);	
						formObject.setEnabled("cmplx_EligibilityAndProductInfo_MArginRate",false);
						formObject.setEnabled("cmplx_EligibilityAndProductInfo_ProdPrefRate",false);	*/
					}
					
					
					if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails")) {
						loadPicklist_Address();
					}
					
					
					
					if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) 
					{
						formObject.setEnabled("DecisionHistory_Text1",false);
						formObject.setEnabled("cmplx_DecisionHistory_IBAN",false);
						formObject.setEnabled("DecisionHistory_Button3",false);
						loadPicklist3();
					}
						
		
					break;
		
		
				case VALUE_CHANGED:
					SKLogger.writeLog("RLOS VAL Change eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
					if (pEvent.getSource().getName().equalsIgnoreCase("ReqProd")){
						ReqProd=formObject.getNGValue("ReqProd");
						SKLogger.writeLog("RLOS val change ", "Value of ReqProd is:"+ReqProd);
						loadPicklistProduct(ReqProd);
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("SubProd")){
						SKLogger.writeLog("RLOS val change ", "Value of SubProd is:"+formObject.getNGValue("SubProd"));
						formObject.clear("AppType");
						formObject.setNGValue("AppType","--Select--");
						if(formObject.getNGValue("SubProd").equalsIgnoreCase("Business titanium Card"))
							LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType where subProdFlag='BTC'");
						
						else if(formObject.getNGValue("SubProd").equalsIgnoreCase("Instant Money"))
							LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType where subProdFlag='IM'");
						
						else if(formObject.getNGValue("SubProd").equalsIgnoreCase("Limit Increase"))
							LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType where subProdFlag='LI'");
						
						else if(formObject.getNGValue("SubProd").equalsIgnoreCase("Salaried Credit Card"))
							LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType where subProdFlag='SAL'");
						
						else if(formObject.getNGValue("SubProd").equalsIgnoreCase("Self Employed Credit Card"))
							LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType where subProdFlag='SE'");
						
						else if(formObject.getNGValue("SubProd").equalsIgnoreCase("Expat Personal Loans"))
							LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType where subProdFlag='EXP'");
						
						else if(formObject.getNGValue("SubProd").equalsIgnoreCase("National Personal Loans"))
							LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType where subProdFlag='NAT'");
						
						else if(formObject.getNGValue("SubProd").equalsIgnoreCase("Topup"))
							LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType WHERE Desciption NOT LIKE 'New' AND subProdFlag='EXP'");
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("Product_type")){
						
						String ProdType=formObject.getNGValue("Product_type");
						SKLogger.writeLog("RLOS Value Change Prod_Type",ProdType);
						formObject.clear("CardProd");
						formObject.setNGValue("CardProd","--Select--");
						if(ProdType.equalsIgnoreCase("Conventional"))
							LoadPickList("CardProd", "select '--Select--' union select convert(varchar,description) from ng_MASTER_CardProduct where code NOT like '%AMAL%' ORDER BY description");
						
						if(ProdType.equalsIgnoreCase("Islamic"))
							LoadPickList("CardProd", "select '--Select--' union select convert(varchar,description) from ng_MASTER_CardProduct where code like '%AMAL%' ORDER BY description");
					}
		
					if(pEvent.getSource().getName().equalsIgnoreCase("cardNo")){
						new AesUtil();
						String encryp_CardNo=AesUtil.Encrypt(formObject.getNGValue("cardNo"));
						formObject.setNGValue("encrypt_CardNo",formObject.getNGValue("cardNo"));
						SKLogger.writeLog("RLOS Value Of Encrypted Card No:",encryp_CardNo);
					}
					
					
					break;
				default:
					break;
		
				
				}
			}
			catch(Exception ex)
			{
				 if(ex instanceof ValidatorException)
					{   
						if(popupFlag.equalsIgnoreCase("Y"))
						{
							
							if(popUpControl.equals(""))
							{
								throw new ValidatorException(new FacesMessage(popUpMsg));
							}else
							{
								throw new ValidatorException(new FacesMessage(popUpMsg,popUpControl));

							}
							
						}
						else{
						HashMap<String,String> hm=new HashMap<String,String>();
						hm.put("Error","Checked");
						if(!popUpMsg.equals("")) {
							try{ throw new ValidatorException(new CustomExceptionHandler("Details Fetched", popUpMsg,"EventType", hm));}finally{hm.clear();}
							
						} else {
							try{ throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));}finally{hm.clear();}
							
						}
						
						}
					}
				else
				{
				ex.printStackTrace();
				System.out.println("exception in eventdispatched="+ ex);
				}
			}
		}	
		public void saveFormStarted(FormEvent pEvent) 
		{
			SKLogger.writeLog("RLOS Tele sales Initiation", "Inside saveFormStarted()" + pEvent);
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			formObject.setNGValue("CIFID", formObject.getNGValue("cmplx_Customer_CIFNO"));
			SKLogger.writeLog("RLOS Initiation", "CIFID is:" +  formObject.getNGValue("cmplx_Customer_CIFNO"));
			formObject.setNGValue("Created_By",formObject.getUserName());
			formObject.setNGValue("PROCESS_NAME", "RLOS");
			formObject.setNGValue("initiationChannel", "Telesales_Init");
		}


		public void submitFormStarted(FormEvent pEvent)
				throws ValidatorException {
			SKLogger.writeLog("RLOS Initiation", "Inside submitFormStarted()" + pEvent);
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			formObject.setNGValue("loan_Type", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0));
			formObject.setNGValue("email_id", formObject.getUserName()+"@rlos.com");
			ParentToChild();
			saveIndecisionGrid();
			TypeOfProduct();
			if(!formObject.isVisible("Liability_New_Frame1")){
				SKLogger.writeLog("RLOS Initiation", "Liability not visible" );
				formObject.fetchFragment("Liability_container", "Liability_New", "q_LiabilityNew");
			}
			
				//if(formObject.getLVWRowCount("cmplx_ExternalLiabilities_cmplx_CardGrid")==0 || formObject.getLVWRowCount("cmplx_ExternalLiabilities_cmplx_ProdGrid")==0)
					//throw new ValidatorException(new FacesMessage("Liability cannot be empty","cmplx_ExternalLiabilities_cmplx_CardGrid"));
				
				CIFIDCheck();
		}


		public void saveFormCompleted(FormEvent pEvent) {
			SKLogger.writeLog("RLOS Initiation", "Inside saveFormCompleted()" + pEvent);
						 
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			formObject.setNGValue("CIFID", formObject.getNGValue("cmplx_Customer_CIFNO"));
			SKLogger.writeLog("RLOS Initiation", "CIFID is:" +  formObject.getNGValue("cmplx_Customer_CIFNO"));
			formObject.setNGValue("Created_By",formObject.getUserName());
			formObject.setNGValue("PROCESS_NAME", "RLOS");
			formObject.setNGValue("initiationChannel", "Telesales_Init");
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



