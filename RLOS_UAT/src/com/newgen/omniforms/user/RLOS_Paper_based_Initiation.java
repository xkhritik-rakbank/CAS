package com.newgen.omniforms.user;

import com.newgen.omniforms.FormReference;
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

import javax.faces.validator.ValidatorException;


public class RLOS_Paper_based_Initiation extends RLOSCommon implements FormListener
{
	public void formLoaded(FormEvent pEvent)
	{
		System.out.println("Inside Paperinitiation RLOS");
		SKLogger.writeLog("RLOS PaperInitiation", "Inside formLoaded()" + pEvent.getSource().getName());
		
	}
	public void formPopulated(FormEvent pEvent) {
        FormReference formObject = FormContext.getCurrentInstance().getFormReference();
        try{
            System.out.println("Inside Paperinitiation RLOS");
            SKLogger.writeLog("RLOS PaperInitiation", "Inside formPopulated()" + pEvent.getSource());
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            String currDate = format.format(Calendar.getInstance().getTime());
             SKLogger.writeLog("RLOS PaperInitiation", "currTime:" + currDate);
           // formObject.setNGValue("Intro_Date",currDate);
            formObject.setNGValue("Intro_Date",formObject.getNGValue("Created_Date"));
            formObject.setNGValue("initiationChannel","Paperbased_Init");
            formObject.setNGValue("Channel_Name","Paper based Initiation");
            
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
            SKLogger.writeLog("RLOS PaperInitiation", "Exception:"+e.getMessage());
        }
    }
	
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
		String popupFlag="N";
		String popUpMsg="";
		String popUpControl="";
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
				if (pEvent.getSource().getName().equalsIgnoreCase("ProductDetailsLoader")) 
				{
					
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
					SKLogger.writeLog("RLOS", "Inside ProductDetailsLoader ");
					
					hm.put("EmploymentDetails","Clicked");
					popupFlag="N";
					formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmploymentDetails");
					try
					{
						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
					}
					finally{hm.clear();}	
				}
				if (pEvent.getSource().getName().equalsIgnoreCase("Incomedetails")) 
				{
					
					hm.put("Incomedetails","Clicked");
					popupFlag="N";
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
					SKLogger.writeLog("RLOS", "CompanyDetailse1:");
					try
					{
						SKLogger.writeLog("RLOS", "CompanyDetailse1222222:");
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
							formObject.setVisible("CardDetails_Text5", true);	//Statement Cycle					
						}	
						else{
							formObject.setVisible("CardDetails_Label7", false);
							formObject.setVisible("CardDetails_Text5", false);		//Statement Cycle			
						}
					}	
				
					try
					{
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
				loadPicklist3();
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
					 SKLogger.writeLog("RLOS PaperInitiation Inside ","IncomingDocuments");
			            formObject.fetchFragment("IncomingDocuments", "IncomingDoc", "q_IncomingDoc");
			          			           
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
					SKLogger.writeLog("RLOS PaperInitiation eventDispatched()","DeferralDocuments");
		            formObject.fetchFragment("DeferralDocuments", "DeferralDocName", "q_DeferralDoc");
					try
					{
						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
					}
					finally{hm.clear();}
		            
		        }
				
				  break;
				  
			case FRAGMENT_LOADED:
				
				if (pEvent.getSource().getName().equalsIgnoreCase("Customer")) {
					formObject.setEnabled("Customer_Frame1",false);
					formObject.setHeight("Customer_Frame1", 780);
					formObject.setHeight("CustomerDetails", 790);
					formObject.setEnabled("Customer_save",true);
					SKLogger.writeLog("RLOS","Value Of Initiation Channel is:"+formObject.getNGValue("initiationChannel"));
	              
					formObject.setNGValue("cmplx_Customer_EmiratesID",formObject.getNGValue("EmirateID"));
					if(!formObject.getNGValue("initiationChannel").equalsIgnoreCase("M")){
					formObject.setEnabled("Customer_Frame2",true);
					formObject.setEnabled("Customer_Frame3",true);
					formObject.setVisible("Customer_Frame2", false);
					formObject.setEnabled("cmplx_Customer_CardNotAvailable",true);
					formObject.setEnabled("ReadFromCard",true);
					formObject.setEnabled("cmplx_Customer_NEP",true);
					formObject.setEnabled("cmplx_Customer_DLNo",true);
					formObject.setEnabled("cmplx_Customer_Passport2",true);
					formObject.setEnabled("cmplx_Customer_Passport3",true);
					formObject.setEnabled("cmplx_Customer_PAssport4",true);
					loadPicklistCustomer();           		 
					} 		
				}
				
				if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
					LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
					LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
				}
				
				if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) 
				{
					SKLogger.writeLog("RLOS","Inside fragLoaded DecHistory");
					formObject.setEnabled("DecisionHistory_Text1",false);
					formObject.setEnabled("cmplx_DecisionHistory_IBAN",false);
					formObject.setEnabled("DecisionHistory_Button3",false);
					loadPicklist3();
				}
				
			case FRAME_COLLAPSED: {
				break;
			}
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
	public void saveFormCompleted(FormEvent pEvent) {
		SKLogger.writeLog("RLOS PaperInitiation", "Inside saveFormCompleted()" + pEvent);
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		 
		formObject.setNGValue("Created_By",formObject.getUserName());
		formObject.setNGValue("PROCESS_NAME", "RLOS");
		formObject.setNGValue("initiationChannel", "Paperbased_Init");
	}


	public void submitFormCompleted(FormEvent pEvent)
			throws ValidatorException {
		SKLogger.writeLog("RLOS PaperInitiation", "Inside submitFormCompleted()" + pEvent);


	}

	public void continueExecution(String eventHandler, HashMap<String, String> m_mapParams) {
		SKLogger.writeLog("RLOS PaperInitiation", "Inside continueExecution()" + eventHandler);
	}


	public void initialize() {
		SKLogger.writeLog("RLOS PaperInitiation", "Inside initialize()");
	}
	public void saveFormStarted(FormEvent pEvent) throws ValidatorException {
		// TODO Auto-generated method stub
		SKLogger.writeLog("RLOS Initiation", "Inside saveFormStarted()" + pEvent);
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		
		
	}
	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		
	}
}
