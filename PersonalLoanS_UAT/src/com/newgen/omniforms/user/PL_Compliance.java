/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PL_Compliance.java
Author                                                                        : Disha
Date (DD/MM/YYYY)                                      						  : 
Description                                                                   : 

------------------------------------------------------------------------------------------------------------------------------------------------------
CHANGE HISTORY 
------------------------------------------------------------------------------------------------------------------------------------------------------

Problem No/CR No   Change Date   Changed By    Change Description
1.                 12-6-2017     Disha	       Common function for decision fragment textboxes and combo visibility
------------------------------------------------------------------------------------------------------*/
package com.newgen.omniforms.user;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;
import com.newgen.omniforms.util.PL_SKLogger;


import com.newgen.omniforms.util.PL_SKLogger;
import com.newgen.omniforms.component.IRepeater;
import com.newgen.omniforms.excp.CustomExceptionHandler;







import javax.faces.application.*;

public class PL_Compliance extends PLCommon implements FormListener
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
        try{
            new PersonalLoanSCommonCode().setFormHeader(pEvent);
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
					new PersonalLoanSCommonCode().FrameExpandEvent(pEvent);
					
					/*if (pEvent.getSource().getName().equalsIgnoreCase("CustomerDetails")) {
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
	        			
	        			formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligAndProductInfo");
	        			
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
					
					if (pEvent.getSource().getName().equalsIgnoreCase("Compliance")) {
	        			hm.put("Compliance","Clicked");
						popupFlag="N";
        			  			
	        			formObject.fetchFragment("Compliance", "Compliance", "q_compliance");

	        			try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("Part_Match")) {
						hm.put("Part_Match","Clicked");
						popupFlag="N";
						  			
						formObject.fetchFragment("Part_Match", "PartMatch", "q_PartMatch");
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRM_Incidents")){					
						hm.put("FinacleCRM_Incidents","Clicked");
						popupFlag="N";
						formObject.fetchFragment("FinacleCRM_Incidents", "FinacleCRMIncident", "q_FinIncident");
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRM_CustInfo")){ 
						hm.put("FinacleCRM_CustInfo","Clicked");
						popupFlag="N";
						formObject.fetchFragment("FinacleCRM_CustInfo", "FinacleCRMCustInfo", "q_FinCRMCustInfo");
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
						
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("Finacle_Core")){ 
						hm.put("Finacle_Core","Clicked");
						popupFlag="N";
						formObject.fetchFragment("Finacle_Core", "FinacleCore", "q_Finaclecore");
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
						
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("MOL")){ 
						hm.put("MOL","Clicked");
						popupFlag="N";
						formObject.fetchFragment("MOL", "MOL1", "q_MOL");
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
						
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("World_Check")){ 
						hm.put("World_Check","Clicked");
						popupFlag="N";
						formObject.fetchFragment("World_Check", "WorldCheck1", "q_WorldCheck");
						formObject.setLocked("WorldCheck1_age",true);
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
						
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("Reject_Enq")){ 
						hm.put("Reject_Enq","Clicked");
						popupFlag="N";
						formObject.fetchFragment("Reject_Enq", "RejectEnq", "q_RejectEnq");
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
						
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("Sal_Enq")){ 
						hm.put("Sal_Enq","Clicked");
						popupFlag="N";
						formObject.fetchFragment("Sal_Enq", "SalaryEnq", "q_SalaryEnq");
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
						
					}*/
					
					break;
					
					case FRAGMENT_LOADED:
						PL_SKLogger.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
					 	
						/*if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
		        			
						}*/
						if (pEvent.getSource().getName().equalsIgnoreCase("Customer")) {
							//setDisabled();
							formObject.setLocked("Customer_Frame1",true);
						}	

						if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
							formObject.setLocked("Product_Frame1",true);
							LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
							LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
						}

						if (pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails")) {
	
							formObject.setLocked("GuarantorDetails_Frame1",true);
						}

						if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails")) {
	
							formObject.setLocked("IncomeDetails_Frame1",true);
						} 

						if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")) {
	
							formObject.setLocked("ExtLiability_Frame1",true);
						}

						if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) {
	
							formObject.setLocked("EMploymentDetails_Frame1",true);
						}

						if (pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo")) {
	
							formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
						}

						if (pEvent.getSource().getName().equalsIgnoreCase("LoanDetails")) {
	
							formObject.setLocked("LoanDetails_Frame1",true);
						}

						if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails")) {
							loadPicklist_Address();
							formObject.setLocked("AddressDetails_Frame1",true);
						}

						if (pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails")) {
	
							formObject.setLocked("AltContactDetails_Frame1",true);
						}

						if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails")) {
	
							formObject.setLocked("CardDetails_Frame1",true);
						}

						if (pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails")) {
	
							formObject.setLocked("ReferenceDetails_Frame1",true);
						}

						if (pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails")) {
	
							formObject.setLocked("SupplementCardDetails_Frame1",true);
						}

						if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")) {
	
							formObject.setLocked("FATCA_Frame6",true);
						}

						if (pEvent.getSource().getName().equalsIgnoreCase("KYC")) {
	
							formObject.setLocked("KYC_Frame1",true);
						}

						if (pEvent.getSource().getName().equalsIgnoreCase("OECD")) {
							
							formObject.setLocked("OECD_Frame8",true);
						}
						
						if (pEvent.getSource().getName().equalsIgnoreCase("Compliance")) {	
													
							formObject.setLocked("Compliance_Frame1",true);
							 formObject.setLocked("Compliance_CompRemarks",false);			            
			                    formObject.setLocked("Compliance_Modify",false); 
			                    formObject.setLocked("Compliance_Save",false);
			                    formObject.setLocked("cmplx_Compliance_ComplianceRemarks",false);
			                    
			                    /*LoadPickList("cmplx_Compliance_EMpType", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from ng_MASTER_EmploymentType with (nolock) order by code");
			                    LoadPickList("cmplx_Compliance_CustType", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from ng_master_CustomerType with (nolock) order by code");
			                    LoadPickList("cmplx_Compliance_BussSegment", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from ng_master_BusinessSegment with (nolock) order by code");
			                    LoadPickList("cmplx_Compliance_subSegment", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from ng_master_SubSegment with (nolock) order by code");
			                    LoadPickList("cmplx_Compliance_demographic", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from ng_master_Demographics with (nolock) order by code");
			                    LoadPickList("cmplx_Compliance_industry", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from ng_master_Industry with (nolock) order by code");
			                    LoadPickList("cmplx_Compliance_nationality_companyDomicile", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from ng_master_Nationality with (nolock) order by code");
			                    LoadPickList("cmplx_Compliance_product", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from ng_master_product with (nolock) order by code");
			                    LoadPickList("cmplx_Compliance_currency", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from ng_master_Currency with (nolock) order by code");
			                    LoadPickList("cmplx_Compliance_custCategory", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from ng_master_CustomerCategory with (nolock) order by code");
			                    LoadPickList("cmplx_Compliance_ExposedPerson", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from ng_master_PoliticalExposed with (nolock) order by code");*/
			                    
			                    
							formObject.fetchFragment("World_Check", "WorldCheck1", "q_WorldCheck");
	                        
	                        formObject.setNGFrameState("World_Check",0);
	                        int n=formObject.getLVWRowCount("cmplx_WorldCheck_WorldCheck_Grid");
	              			 PL_SKLogger.writeLog("CC value of world check row","value of n "+n);
	              			 if(n>0)
	              			 { 
	              				 PL_SKLogger.writeLog("CC value of world check row grid to get values","inside world check grid--- ");
	              				String UID = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,12);
	              				// String UID =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),12);
	              				PL_SKLogger.writeLog("CC value of world check UID","UID "+UID);
	              				formObject.setNGValue("Compliance_UID",UID);
	              				
	              				String EI = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,15);
	              				// String EI =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),15);
	              				 PL_SKLogger.writeLog("CC value of world check EI ","EI "+EI);
	              				formObject.setNGValue("Compliance_EI",EI);
	              				// String Name =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),0);
	              				String Name = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,0);
	              				 PL_SKLogger.writeLog("CC value of world check Name","Name "+Name);	
	              				formObject.setNGValue("Compliance_Name",Name);
	              				 //String Dob =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),2);
	              				String Dob = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,2);
	              				 PL_SKLogger.writeLog("CC value of world check Dob","Dob "+Dob);
	              				formObject.setNGValue("Compliance_DOB",Dob);
	              				String Citizenship = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,7);
	              				// String Citizenship =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),7);
	              				 PL_SKLogger.writeLog("CC value of world check Citizenship","Citizenship"+Citizenship);	
	              				formObject.setNGValue("Compliance_Citizenship",Citizenship);
	              				String Remarks = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,16);
	              				// String Remarks =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),16);
	              				formObject.setNGValue("Compliance_Remarks",Remarks);
	              				String Id_No = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,1);
	              				formObject.setNGValue("Compliance_IdentificationNumber",Id_No);
	              				 //String Id_No =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),1);
	              				String Age = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,3);
	              				formObject.setNGValue("Compliance_Age",Age);
	              				//String Age =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),3);
	              				
	              				String Position = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,4);
	              				formObject.setNGValue("Compliance_Positon",Position);
	              				//String Position =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),4);
	              				String Deceased = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,5); 
	              				formObject.setNGValue("Compliance_Deceased",Deceased);
	              				//String Deceased =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),5);
	              				String Category = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,6);
	              				formObject.setNGValue("Compliance_Category",Category);
	              				//String Category =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),6);
	              				String Location = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,8);
	              				formObject.setNGValue("Compliance_Location",Location);
	              				 //String Location =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),8);
	              				String Identification = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,9); 
	              				formObject.setNGValue("Compliance_Identification",Identification);
	              				//String Identification =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),9);
	              				String Biography = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,10); 
	              				formObject.setNGValue("Compliance_Biography",Biography);
	              				//String Biography =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),10);
	              				String Reports = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,11); 
	              				formObject.setNGValue("Compliance_Reports",Reports);
	              				//String Reports =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),11);
	              				String Entered_Date = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,13);
	              				formObject.setNGValue("Compliance_EntertedDate",Entered_Date);
	              				//String Entered_Date =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),13);
	              				String Updated_Date = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,14);
	              				formObject.setNGValue("Compliance_UpdatedDate",Updated_Date);
	              				//String Updated_Date =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),14);
	              				//String Match_Found = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,17); 
	              				
	              				//String Match_Found =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),17);
	              				String Document = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,18);  
	              				formObject.setNGValue("Compliance_Document",Document);
	              				//String Document =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),18);
	              				String Decision = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,19);
	              				formObject.setNGValue("Compliance_Decision",Decision);
	              				//String Decision =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),19);
	              				String Match_Rank = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,20);
	              				formObject.setNGValue("Compliance_MatchRank",Match_Rank);
	              				//String Match_Rank =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),20);
	              				String Alias = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,21);
	              				formObject.setNGValue("Compliance_Alias",Alias);
	              				//String Alias =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),21);
	              				String birth_Country = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,22);
	              				formObject.setNGValue("Compliance_BirthCountry",birth_Country);
	              				//String birth_Country =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),22);
	              				String resident_Country = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,23);
	              				formObject.setNGValue("Compliance_ResidenceCountry",resident_Country);
	              				//String resident_Country =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),23);
	              				String Address = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,24);
	              				formObject.setNGValue("Compliance_Address",Address);
	              				//String Address =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),24);
	              				String Gender = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,25);
	              				formObject.setNGValue("Compliance_Gender",Gender);
	              				//String Gender =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),25);
	              				String Listed_On = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,26);
	              				formObject.setNGValue("Compliance_ListedOn",Listed_On);
	              				//String Listed_On =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),26);
	              				String Program = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,27);
	              				formObject.setNGValue("Compliance_Program",Program);
	              				//String Program =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),27);
	              				String external_ID = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,28);
	              				formObject.setNGValue("Compliance_ExternalID",external_ID);
	              				//String external_ID =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),28);
	              				String data_Source = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,29);
	              				//String data_Source =formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",formObject.getSelectedIndex("cmplx_WorldCheck_WorldCheck_Grid"),29);
	              				formObject.setNGValue("Compliance_DataSource",data_Source);
	              				
	              				formObject.setNGValue("Compliance_winame",formObject.getWFWorkitemName());
	              				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Compliance_cmplx_gr_compliance");
	          				 	 String Datechanged="";
	          				 	 String complianceRemarks="";
	            						  
	       	        		formObject.RaiseEvent("WFSave");
	              			 }
	              			formObject.setNGFrameState("World_Check",1);
						}

						if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
							loadPicklist1();
							
							formObject.setVisible("cmplx_Decision_waiveoffver",false);
							formObject.setVisible("Decision_Label1",false);
							formObject.setVisible("cmplx_Decision_VERIFICATIONREQUIRED",false);
							formObject.setVisible("DecisionHistory_chqbook",false);
							formObject.setVisible("DecisionHistory_Label1",false);
							formObject.setVisible("cmplx_Decision_refereason",false);
							formObject.setVisible("DecisionHistory_Label6",false);
							formObject.setVisible("cmplx_Decision_IBAN",false);
							formObject.setVisible("DecisionHistory_Label7",false);
							formObject.setVisible("cmplx_Decision_AccountNo",false);
							formObject.setVisible("DecisionHistory_Label8",false);
							formObject.setVisible("cmplx_Decision_ChequeBookNumber",false);
							formObject.setVisible("DecisionHistory_Label9",false);
							formObject.setVisible("cmplx_Decision_DebitcardNumber",false);
							formObject.setVisible("DecisionHistory_Label5",false);
							formObject.setVisible("cmplx_Decision_desc",false);
							formObject.setVisible("DecisionHistory_Label3",false);
							formObject.setVisible("cmplx_Decision_strength",false);
							formObject.setVisible("DecisionHistory_Label4",false);
							formObject.setVisible("cmplx_Decision_weakness",false);
							
							formObject.setTop("Decision_Label3",7);
							formObject.setTop("cmplx_Decision_Decision",22);							
							formObject.setTop("Decision_Label4",7);
							formObject.setTop("cmplx_Decision_REMARKS",22);
							//formObject.setTop("DecisionHistory_Label11",7);
							//formObject.setTop("cmplx_Decision_Decreasoncode",22);
							formObject.setTop("Decision_ListView1",96);							
							formObject.setTop("DecisionHistory_save",288);
							
							formObject.setLeft("Decision_Label4",555);
							formObject.setLeft("cmplx_Decision_REMARKS",555);	
							formObject.setLeft("DecisionHistory_Label11",297);
							formObject.setLeft("cmplx_Decision_Decreasoncode",297);
							
							//formObject.setVisible("DecisionHistory_Modify", true);
							//formObject.setVisible("cmplx_Decision_Status", true);
							//formObject.setVisible("DecisionHistory_Label2", true);
							
							//Common function for decision fragment textboxes and combo visibility
							//decisionLabelsVisibility();
						} 	
						// disha FSD
						if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails")) {
							
				 			//formObject.setLocked("NotepadDetails_Frame1",true);
				 			formObject.setVisible("NotepadDetails_Frame3",false);
							String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
							PL_SKLogger.writeLog("PL notepad ", "Activity name is:" + sActivityName);
							int user_id = formObject.getUserId();
							String user_name = formObject.getUserName();
							user_name = user_name+"-"+user_id;					
							formObject.setNGValue("NotepadDetails_insqueue",sActivityName);
							formObject.setNGValue("NotepadDetails_Actusername",user_name); 
							formObject.setNGValue("NotepadDetails_user",user_name); 
							formObject.setLocked("NotepadDetails_noteDate",true);
							formObject.setLocked("NotepadDetails_Actusername",true);
							formObject.setLocked("NotepadDetails_user",true);
							formObject.setLocked("NotepadDetails_insqueue",true);
							formObject.setLocked("NotepadDetails_Actdate",true);
							formObject.setLocked("NotepadDetails_notecode",true);
							formObject.setVisible("NotepadDetails_save",true);
							
							formObject.setHeight("NotepadDetails_Frame1",450);
							LoadPickList("NotepadDetails_notedesc", "select '--Select--' union select  description from ng_master_notedescription");
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
						// disha FSD
						if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Add")){
							formObject.setNGValue("Notepad_wi_name",formObject.getWFWorkitemName());
							formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_notegrid");
							
							String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
							PL_SKLogger.writeLog("PL notepad ", "Activity name is:" + sActivityName);
							int user_id = formObject.getUserId();
							String user_name = formObject.getUserName();
							user_name = user_name+"-"+user_id;					
							formObject.setNGValue("NotepadDetails_insqueue",sActivityName,false);
							formObject.setNGValue("NotepadDetails_Actusername",user_name,false); 
							formObject.setNGValue("NotepadDetails_user",user_name,false);
							
							DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
							 Date date = new Date();
							 formObject.setNGValue("NotepadDetails_noteDate",dateFormat.format(date),false); 
							 formObject.setNGValue("NotepadDetails_Actdate",dateFormat.format(date),false); 

						}
						
						if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Modify")){
							formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_NotepadDetails_cmplx_notegrid");
						}
						
						if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Delete")){
							formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_NotepadDetails_cmplx_notegrid");

						}
						
						if (pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_Add")){
							formObject.setNGValue("WorldCheck1_winame",formObject.getWFWorkitemName());
							PL_SKLogger.writeLog("PL", "Inside add button: "+formObject.getNGValue("WorldCheck1_winame"));
							formObject.ExecuteExternalCommand("NGAddRow", "cmplx_WorldCheck_WorldCheck_Grid");
						}
						
						if (pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_Modify")){
							formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_WorldCheck_WorldCheck_Grid");
						}
						
						if (pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_Delete")){
							formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_WorldCheck_WorldCheck_Grid");

						}
						
						if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Modify"))
						{
							formObject.ExecuteExternalCommand("NGModifyRow","cmplx_Decision_cmplx_gr_decision");
					
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("Compliance_Modify"))
						{
							formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Compliance_cmplx_gr_compliance");
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
						// disha FSd
						if(pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails_ContactDetails_Save")){
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
						
						// disha FSD
						if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Save")){
							formObject.saveFragment("DecisionHistory");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("Compliance_Save")){
							formObject.saveFragment("Compliance");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_Save")){
							formObject.saveFragment("World_Check");
						}
						// disha FSd
						if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_save")){
							formObject.saveFragment("Notepad_Values");
						}
					
					 case VALUE_CHANGED:
							PL_SKLogger.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
							 if (pEvent.getSource().getName().equalsIgnoreCase("Decision_Combo2")) {
								 if(formObject.getWFActivityName().equalsIgnoreCase("CAD_Analyst1"))	
								 {
									 formObject.setNGValue("CAD_dec", formObject.getNGValue("Decision_Combo2"));
									PL_SKLogger.writeLog(" In PL_Initiation VALChanged---New Value of CAD_dec is: ", formObject.getNGValue("Decision_Combo2"));

								 }
								 
								 else{
									 
									formObject.setNGValue("decision", formObject.getNGValue("Decision_Combo2"));
									PL_SKLogger.writeLog(" In PL_Initiation VALChanged---New Value of decision is: ", formObject.getNGValue("Decision_Combo2"));
								 	  }
							 	}
							 
							 //Added by Arun (21/09/17)
							 // disha FSD
							 if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_notedesc")){
								 String notepad_desc = formObject.getNGValue("NotepadDetails_notedesc");
								 //LoadPickList("NotepadDetails_notecode", "select '--Select--' union select convert(varchar, description) from ng_master_notedescription with (nolock)  where Description=q'["+notepad_desc+"]'","NotepadDetails_notecode");
								 String sQuery = "select code,workstep from ng_master_notedescription where Description='" +  notepad_desc + "'";
								 PL_SKLogger.writeLog(" query is ",sQuery);
								 List<List<String>> recordList = formObject.getDataFromDataSource(sQuery);
								 if(recordList.get(0).get(0)!= null && recordList.get(0)!=null && !recordList.get(0).get(0).equalsIgnoreCase("") && recordList!=null)
								 {
									 formObject.setNGValue("NotepadDetails_notecode",recordList.get(0).get(0));
									 formObject.setNGValue("NotepadDetails_workstep",recordList.get(0).get(1));
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
		 formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
		saveIndecisionGrid();
	}

}

