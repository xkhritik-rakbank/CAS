package com.newgen.omniforms.user;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;
import com.newgen.omniforms.util.PL_SKLogger;


import com.newgen.omniforms.component.IRepeater;
import com.newgen.omniforms.excp.CustomExceptionHandler;


import javax.faces.application.*;

public class PL_Relationship_Manager extends PLCommon implements FormListener
{
	boolean IsFragLoaded=false;
	String queryData_load="";
	 FormReference formObject = null;
	public void formLoaded(FormEvent pEvent)
	{
		System.out.println("Inside initiation RLOS");
		PL_SKLogger.writeLog("RLOS Initiation", "Inside formLoaded()" + pEvent.getSource().getName());
		
	}
	

	public void formPopulated(FormEvent pEvent) 
	{
        FormReference formObject = FormContext.getCurrentInstance().getFormReference();
        try{
            System.out.println("Inside initiation RLOS");
            PL_SKLogger.writeLog("RLOS Initiation", "Inside formPopulated()" + pEvent.getSource());
            formObject.setNGValue("WiLabel",formObject.getWFWorkitemName());
            formObject.setNGValue("QueueLabel","PL_Relationship_Manager");
            formObject.setNGValue("User_Name",formObject.getUserName()); 
            formObject.setNGValue("Introduce_date",formObject.getNGValue("CreatedDate"));
			partMatchValues();
        }catch(Exception e)
        {
            PL_SKLogger.writeLog("RLOS Initiation", "Exception:"+e.getMessage());
        }
    }
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
		String popupFlag="N";
		String popUpMsg="";
		String popUpControl="";
		PL_SKLogger.writeLog("Inside PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		  formObject =FormContext.getCurrentInstance().getFormReference();

				switch(pEvent.getType())
				{	

					case FRAME_EXPANDED:
					PL_SKLogger.writeLog(" In PL_Iniation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

					if (pEvent.getSource().getName().equalsIgnoreCase("CustomerDetails")) {
	        			hm.put("CustomerDetails","Clicked");
						popupFlag="N";
	    				formObject.fetchFragment("CustomerDetails", "Customer", "q_Customer");
	    				loadPicklistCustomer();
	        			
	        			try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        			
	        		}
					if (pEvent.getSource().getName().equalsIgnoreCase("ProductContainer")) {
	        			hm.put("ProductContainer","Clicked");
						popupFlag="N";
	    				formObject.fetchFragment("ProductContainer", "Product", "q_Product");
	    				LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
						LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
	        			
	        			try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        			}
						if (pEvent.getSource().getName().equalsIgnoreCase("EmploymentDetails")) {
	        			
	        			hm.put("EmploymentDetails","Clicked");
						popupFlag="N";
	        			formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmpDetails");
	        			
	        			loadPicklist_Employment();
	        			try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}
					if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDEtails")) {
						hm.put("IncomeDEtails","Clicked");
						popupFlag="N";
						formObject.fetchFragment("IncomeDEtails", "IncomeDetails", "q_IncomeDetails");
						
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("InternalExternalLiability")) {
	        			hm.put("InternalExternalLiability","Clicked");
						popupFlag="N";
	        			
	        			formObject.fetchFragment("InternalExternalLiability", "ExternalLiabilities", "q_ExtLiabilities");
	        			
	        			try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}	
					if (pEvent.getSource().getName().equalsIgnoreCase("MiscFields")) {
	        			hm.put("MiscFields","Clicked");
						popupFlag="N";
		
	        			formObject.fetchFragment("MiscFields", "MiscellaneousFields", "q_MiscFields");
	        			
	        			try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        			
	        		}
					if (pEvent.getSource().getName().equalsIgnoreCase("EligibilityAndProductInformation")) {
	        			hm.put("EligibilityAndProductInformation","Clicked");
						popupFlag="N";
	        			
	        			formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligibilityProdInfo");
	        			
	        			try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}	
	        		
					
	        		if (pEvent.getSource().getName().equalsIgnoreCase("Address_Details_container")) {
	        			hm.put("Address_Details_container","Clicked");
						popupFlag="N";
		
	        			formObject.fetchFragment("Address_Details_container", "AddressDetails", "q_AddressDetails");
	        			loadPicklist_Address();
	        			
	        			try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}
	        		
	        		if (pEvent.getSource().getName().equalsIgnoreCase("Alt_Contact_container")) {
	        			hm.put("Alt_Contact_container","Clicked");
						popupFlag="N";
	        			
	        			formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
	        			
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
	        		
					if (pEvent.getSource().getName().equalsIgnoreCase("Self_Employed")) {
            			hm.put("Self_Employed","Clicked");
    					popupFlag="N";
            			formObject.fetchFragment("Self_Employed", "SelfEmployed", "q_SelfEmployed");
            			try
    					{
    						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
    					}
    					finally{hm.clear();}
            		}
					
					
					
					if (pEvent.getSource().getName().equalsIgnoreCase("LoanDetails")) {
            			hm.put("LoanDetails","Clicked");
    					popupFlag="N";
            			
            			formObject.fetchFragment("LoanDetails", "LoanDetails", "q_Loan");
            			
            			try
    					{
    						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
    					}
    					finally{hm.clear();}
            		}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
	        			hm.put("DecisionHistory","Clicked");
						popupFlag="N";
	        			formObject.fetchFragment("DecisionHistory", "DecisionHistory", "q_Decision");
	        			loadPicklist3();
	        			loadInDecGrid();
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}
					if (pEvent.getSource().getName().equalsIgnoreCase("Inc_Doc")) {
	        			hm.put("Inc_Doc","Clicked");
						popupFlag="N";
	        			  			
	        			formObject.fetchFragment("Inc_Doc", "IncomingDoc", "q_IncomingDoc");
	        			try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}
					
					break;
					
					case FRAGMENT_LOADED:
						PL_SKLogger.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
					 	
						/*if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
		        			
						}*/
							if (pEvent.getSource().getName().equalsIgnoreCase("Customer")) {
								//setDisabled();
							}	
							
							if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
								LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
								LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails")) {
								loadPicklist_Address();
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
			                	loadPicklist1();
						 } 	
					
					
					  break;
					  
					case MOUSE_CLICKED:
						PL_SKLogger.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
						if (pEvent.getSource().getName().equalsIgnoreCase("Customer_ReadFromCard")){
							//GenerateXML();
						}
						
						if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_addr_Add")){
							formObject.setNGValue("Address_wi_name",formObject.getWFWorkitemName());
							PL_SKLogger.writeLog("PL", "Inside add button: "+formObject.getNGValue("Address_wi_name"));
							formObject.ExecuteExternalCommand("NGAddRow", "cmplx_AddressDetails_cmplx_AddressGrid");
						}
						
						if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_addr_Modify")){
							formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_AddressDetails_cmplx_AddressGrid");
						}
						
						if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_addr_Delete")){
							formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_AddressDetails_cmplx_AddressGrid");

						}

					
						if(pEvent.getSource().getName().equalsIgnoreCase("Customer_save")){
							PL_SKLogger.writeLog("PL_Initiation", "Inside Customer_save button: ");
							formObject.saveFragment("CustomerDetails");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("Product_Save")){
							formObject.saveFragment("ProductContainer");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails_Save")){
							formObject.saveFragment("GuarantorDetails");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_Salaried_Save")){
							formObject.saveFragment("IncomeDEtails");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_SelfEmployed_Save")){
							formObject.saveFragment("Incomedetails");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails_Save")){
							formObject.saveFragment("CompanyDetails");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails_Save")){
							formObject.saveFragment("PartnerDetails");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("SelfEmployed_Save")){
							formObject.saveFragment("Liability_container");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("Liability_New_Save")){
							formObject.saveFragment("InternalExternalContainer");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("EmpDetails_Save")){
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
						
						if(pEvent.getSource().getName().equalsIgnoreCase("14.AltContactDetails_ContactDetails_Save")){
							formObject.saveFragment("Alt_Contact_container");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("CardDetails_save")){
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
						
						
						if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Save")){
							formObject.saveFragment("DecisionHistoryContainer");
						}
				
					
					 case VALUE_CHANGED:
							PL_SKLogger.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
							 if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_Decision_Decision")) {
								 if(formObject.getWFActivityName().equalsIgnoreCase("CAD_Analyst1"))	
								 {
									 formObject.setNGValue("CAD_dec", formObject.getNGValue("cmplx_Decision_Decision"));
									PL_SKLogger.writeLog(" In PL_Initiation VALChanged---New Value of CAD_dec is: ", formObject.getNGValue("cmplx_Decision_Decision"));

								 }
								 
								 else{
									 
									formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
									PL_SKLogger.writeLog(" In PL_Initiation VALChanged---New Value of decision is: ", formObject.getNGValue("cmplx_Decision_Decision"));
								 	  }
							 	}
					default: break;
					
				}
	}


	public void continueExecution(String arg0, HashMap<String, String> arg1) {
		// TODO Auto-generated method stub
		
	}


	public void initialize() {
		// TODO Auto-generated method stub
		
	}


	public void saveFormCompleted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		
	}


	public void saveFormStarted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		
	}


	public void submitFormCompleted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		
	}


	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		 FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		
		saveIndecisionGrid();
	}

}

