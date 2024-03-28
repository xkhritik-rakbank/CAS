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



public class RLOS_Staff_Portal extends RLOSCommon implements FormListener

{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public void formLoaded(FormEvent pEvent)
	{
		System.out.println("Inside StaffPortal RLOS");
		SKLogger.writeLog("RLOS StaffPortal", "Inside formLoaded()" + pEvent.getSource().getName());
		
	}
	public void formPopulated(FormEvent pEvent) {
        FormReference formObject = FormContext.getCurrentInstance().getFormReference();
        try{
            System.out.println("Inside StaffPortal RLOS");
            SKLogger.writeLog("RLOS StaffPortal", "Inside formPopulated()" + pEvent.getSource());
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            String currDate = format.format(Calendar.getInstance().getTime());
             SKLogger.writeLog("RLOS StaffPortal", "currTime:" + currDate);
            //formObject.setNGValue("Intro_Date",currDate);
            
            formObject.setNGValue("Intro_Date",formObject.getNGValue("Created_Date"));
            formObject.setNGValue("initiationChannel","Staff_Init");
            formObject.setNGValue("Channel_Name","Staff Portal Initiation");
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
            SKLogger.writeLog("RLOS StaffPortal", "Exception:"+e.getMessage());
        }
    }
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
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
					HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
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
					HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
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
					HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
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
					HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
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
					HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
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
					HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
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
					HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
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
					HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
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
					HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
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
					HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
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
					
					HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
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
					HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
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
				HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
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
				HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
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
				HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
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
				HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
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
				HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
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
				HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
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
				HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
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
				HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
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
				HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
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
				HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
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
				HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
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
				HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
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
				  HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
					hm.put("IncomingDocuments","Clicked");
					popupFlag="N";
					 SKLogger.writeLog("RLOS StaffPortal Inside ","IncomingDocuments");
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
		        	HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
					hm.put("DeferralDocuments","Clicked");
					popupFlag="N";
					SKLogger.writeLog("RLOS StaffPortal eventDispatched()","DeferralDocuments");
		            formObject.fetchFragment("DeferralDocuments", "DeferralDocName", "q_DeferralDoc");
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
	public void saveFormCompleted(FormEvent pEvent) {
		SKLogger.writeLog("RLOS StaffPortal", "Inside saveFormCompleted()" + pEvent);
		FormContext.getCurrentInstance().getFormReference();
		 

	}


	public void submitFormCompleted(FormEvent pEvent)
			throws ValidatorException {
		SKLogger.writeLog("RLOS StaffPortal", "Inside submitFormCompleted()" + pEvent);
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			
		formObject.setNGValue("Created_By",formObject.getUserName());
		formObject.setNGValue("PROCESS_NAME", "RLOS");
		formObject.setNGValue("initiationChannel", "Staff_Init");

	}

	public void continueExecution(String eventHandler, HashMap<String, String> m_mapParams) {
		SKLogger.writeLog("RLOS StaffPortal", "Inside continueExecution()" + eventHandler);
	}


	public void initialize() {
		SKLogger.writeLog("RLOS StaffPortal", "Inside initialize()");
	}
	public void saveFormStarted(FormEvent pEvent) throws ValidatorException {
			
		// TODO Auto-generated method stub
		SKLogger.writeLog("RLOS Staff portal", "Inside saveFormStarted()" + pEvent);
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		
		
		formObject.setNGValue("Created_By",formObject.getUserName());
		formObject.setNGValue("PROCESS_NAME", "RLOS");
		formObject.setNGValue("initiationChannel", "Staff_Init");
		
	}
	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		
	}
}
