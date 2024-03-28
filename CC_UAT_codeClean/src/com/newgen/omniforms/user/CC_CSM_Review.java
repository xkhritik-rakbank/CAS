
/*------------------------------------------------------------------------------------------------------

                     NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                             : Application -Projects

Project/Product                                                   : Rakbank  

Application                                                       : Credit Card

Module                                                            : CSM Review

File Name                                                         : CC_CSM_Review

Author                                                            : Disha 

Date (DD/MM/YYYY)                                                 : 

Description                                                       : 

-------------------------------------------------------------------------------------------------------

CHANGE HISTORY

-------------------------------------------------------------------------------------------------------

Problem No/CR No   Change Date   Changed By    Change Description

------------------------------------------------------------------------------------------------------*/

package com.newgen.omniforms.user;

import java.util.HashMap;
import java.util.List;
import javax.faces.validator.ValidatorException;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;
import com.newgen.omniforms.util.SKLogger_CC;
import com.newgen.omniforms.excp.CustomExceptionHandler;
import javax.faces.application.*;

public class CC_CSM_Review extends CC_Common implements FormListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	String queryData_load="";
	  FormReference formObject = null;
	public void formLoaded(FormEvent pEvent)
	{
	
		SKLogger_CC.writeLog("CC CC_CSM_Review", "Inside formLoaded()" + pEvent.getSource().getName());
		
		 
		makeSheetsInvisible(tabName, "7,8,9,11,12,13,14,15,16,17");
		
	}
	

	public void formPopulated(FormEvent pEvent) 
	{
	     try{
	    	 SKLogger_CC.writeLog("CC_CSM_Review","Inside CC_Hold_CPV CC");
	    	 new CC_CommonCode().setFormHeader(pEvent);
	        }catch(Exception e)
	        {
	            SKLogger_CC.writeLog("RLOS Initiation", "Exception:"+e.getMessage());
	        }
	        
	    }
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException {
		// TODO Auto-generated method stub
		HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
		String alert_msg="";
		SKLogger_CC.writeLog("Inside PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
	  formObject =FormContext.getCurrentInstance().getFormReference();

      switch(pEvent.getType()) {

          case FRAME_EXPANDED:
				SKLogger_CC.writeLog(" In PL_Iniation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
				
        		if (pEvent.getSource().getName().equalsIgnoreCase("CustomerDetails")) {
        			hm.put("CustomerDetails","Clicked");
					formObject.fetchFragment("CustomerDetails", "Customer", "q_Customer");
    				
        			
        			try
					{
						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
					}
					finally{hm.clear();}
        			
        		}
        		
        		

        		if (pEvent.getSource().getName().equalsIgnoreCase("ProductContainer")) {
        			hm.put("ProductContainer","Clicked");
					formObject.fetchFragment("ProductContainer", "Product", "q_product");
    				LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
					LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
					LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
        			
        			
        			try
					{
						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
					}
					finally{hm.clear();}
        		}
        		
        		if (pEvent.getSource().getName().equalsIgnoreCase("EmploymentDetails")) {
        			
        			hm.put("EmploymentDetails","Clicked");
					formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmpDetails");
        			
        			Fields_ApplicationType_Employment();
        			
        			
        			try
					{
						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
					}
					finally{hm.clear();}
        		}	
        		
        		if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDEtails")) {
        			hm.put("IncomeDEtails","Clicked");
					formObject.fetchFragment("IncomeDEtails", "IncomeDetails", "q_IncomeDetails");
        			
        			try
					{
						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
					}
					finally{hm.clear();}
        		}	
        		
        		if (pEvent.getSource().getName().equalsIgnoreCase("Internal_External_Liability")) {
        			hm.put("Internal_External_Liability","Clicked");
					formObject.fetchFragment("Internal_External_Liability", "Liability_New", "q_Liabilities");
        			
        			try
					{
						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
					}
					finally{hm.clear();}
        		}	
        		
				
        		if (pEvent.getSource().getName().equalsIgnoreCase("MiscFields")) {
        			hm.put("MiscFields","Clicked");
					formObject.fetchFragment("MiscFields", "MiscellaneousFields", "q_MiscFields");
        			
        			try
					{
						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
					}
					finally{hm.clear();}
        			
        		}

        		if (pEvent.getSource().getName().equalsIgnoreCase("EligibilityAndProductInformation")) {
        			hm.put("EligibilityAndProductInformation","Clicked");
					formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligAndProductInfo");
        			
        			
        			try
					{
						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
					}
					finally{hm.clear();}
        		}	
        		
				
        		if (pEvent.getSource().getName().equalsIgnoreCase("Address_Details_container")) {
        			hm.put("Address_Details_container","Clicked");
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
					formObject.fetchFragment("FATCA", "FATCA", "q_FATCA");
					loadPicklist_Fatca();
       			
					try
					{
						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
					}
					finally{hm.clear();}
					
				}	
				
				if (pEvent.getSource().getName().equalsIgnoreCase("KYC")) 
				{
					hm.put("KYC","Clicked");
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
					formObject.fetchFragment("OECD", "OECD", "q_OECD");
					loadPicklist_oecd();
					try
					{
						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
					}
					finally{hm.clear();}
					
				}	
				
				
				if (pEvent.getSource().getName().equalsIgnoreCase("Self_Employed")) {
        			hm.put("Self_Employed","Clicked");
					formObject.fetchFragment("Self_Employed", "SelfEmployed", "q_SelfEmployed");
        			try
					{
						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
					}
					finally{hm.clear();}
        		}
				
        		if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
        			hm.put("DecisionHistory","Clicked");
					formObject.fetchFragment("DecisionHistory", "DecisionHistory", "q_DecisionHistory");
        			loadPicklist3();
        			loadInDecGrid();
        			try
					{
						throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
					}
					finally{hm.clear();}
        		}	
        		if (pEvent.getSource().getName().equalsIgnoreCase("Frame4"))
                {
                    hm.put("IncomingDocuments","Clicked");
                    SKLogger_CC.writeLog("RLOS Initiation Inside ","IncomingDocuments");
                    //frame,fragment,q_variable
                    formObject.fetchFragment("Frame4", "IncomingDocument", "q_IncDoc");
                    SKLogger_CC.writeLog("RLOS Initiation Inside ","fetchIncomingDocRepeater");
                    
                    fetchIncomingDocRepeater();
                    SKLogger_CC.writeLog("RLOS Initiation eventDispatched()","formObject.fetchFragment1");
                    try{

                        throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
                    }
                    finally{
                        hm.clear();
                    }
                }
        		 if (pEvent.getSource().getName().equalsIgnoreCase("Reference_Details")) {
	        			hm.put("Reference_Details","Clicked");
						formObject.fetchFragment("Reference_Details", "Reference_Details", "q_ReferenceDetails");
	        			//loadPicklist3();
	        			//loadInDecGrid();
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}
        		 if (pEvent.getSource().getName().equalsIgnoreCase("Card_Details")) {
	        			hm.put("Card_Details","Clicked");
						formObject.fetchFragment("Card_Details", "CardDetails", "");
	        			//loadPicklist3();
	        			//loadInDecGrid();
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}
        		 if (pEvent.getSource().getName().equalsIgnoreCase("Supplementary_Cont")) {
	        			hm.put("Supplementary_Cont","Clicked");
						formObject.fetchFragment("Supplementary_Cont", "SupplementCardDetails", "q_SuppCardDetails");
	        			
	        			loadPicklist_suppCard();
	        			
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}
        		 if (pEvent.getSource().getName().equalsIgnoreCase("CompDetails")) {
	        			hm.put("CompDetails","Clicked");
						formObject.fetchFragment("CompDetails", "CompanyDetails", "q_CompanyDetails");
						
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}
        		 if (pEvent.getSource().getName().equalsIgnoreCase("Auth_Sign_Details")) {
	        			hm.put("Auth_Sign_Details","Clicked");
						formObject.fetchFragment("Auth_Sign_Details", "AuthorisedSignDetails", "q_AuthorisedSignDetails");
	        			//loadPicklist3();
	        			//loadInDecGrid();
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}
        		 if (pEvent.getSource().getName().equalsIgnoreCase("Partner_Details")) {
	        			hm.put("Partner_Details","Clicked");
						formObject.fetchFragment("Partner_Details", "PartnerDetails", "q_PartnerDetails");
	        			//loadPicklist3();
	        			//loadInDecGrid();
						try
						{
							throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
						}
						finally{hm.clear();}
	        		}
        		 //added by yash for CC FSD
        		 if (pEvent.getSource().getName().equalsIgnoreCase("Notepad_Values")) {
     				hm.put("Notepad_Values","Clicked");
     				formObject.fetchFragment("Notepad_Values", "NotepadDetails", "q_NotepadDetails");
     				LoadPickList("NotepadDetails_notedesc", "select '--Select--' union select  description from ng_master_notedescription");
     				try
     				{
     					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
     				}
     				finally{hm.clear();}
     			}
        		 if (pEvent.getSource().getName().equalsIgnoreCase("Details")) {
     				hm.put("Details","Clicked");
     				formObject.fetchFragment("Details", "CC_Loan", "q_CC_Loan");
     				loadPicklist_ServiceRequest();
     				try
     				{
     					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
     				}
     				finally{hm.clear();}
     			}
        		 if (pEvent.getSource().getName().equalsIgnoreCase("Part_Match")) {
     				hm.put("Part_Match","Clicked");
     				formObject.fetchFragment("Part_Match", "PartMatch", "q_PartMatch");
     				
     				try
     				{
     					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
     				}
     				finally{hm.clear();}
     			}
     			if (pEvent.getSource().getName().equalsIgnoreCase("Finacle_CRM_Incidents")) {
     				hm.put("Finacle_CRM_Incidents","Clicked");
     				formObject.fetchFragment("Finacle_CRM_Incidents", "FinacleCRMIncident", "q_FinacleCRMIncident");
     				
     				try
     				{
     					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
     				}
     				finally{hm.clear();}
     			}
     			if (pEvent.getSource().getName().equalsIgnoreCase("Finacle_CRM_CustomerInformation")) {
     				hm.put("Finacle_CRM_CustomerInformation","Clicked");
     				formObject.fetchFragment("Finacle_CRM_CustomerInformation", "FinacleCRMCustInfo", "q_FinacleCRMCustInfo");
     				
     				try
     				{
     					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
     				}
     				finally{hm.clear();}
     			}
     			if (pEvent.getSource().getName().equalsIgnoreCase("External_Blacklist")) {
    				/*hm.put("External_Blacklist","Clicked");
    				popupFlag="N";*/
    				formObject.fetchFragment("External_Blacklist", "ExternalBlackList", "q_ExternalBlackList");
    				try
     				{
     					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
     				}
     				finally{hm.clear();}
    					
    				
    				}
    				
     			if (pEvent.getSource().getName().equalsIgnoreCase("Finacle_Core")) {
    				/*hm.put("Finacle_Core","Clicked");
    				popupFlag="N";*/
    				formObject.fetchFragment("Finacle_Core", "FinacleCore", "q_FinacleCore");
    				fetchfinacleAvgRepeater();
    				fetchfinacleTORepeater();
    				fetchfinacleDocRepeater();
    					//if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6).equalsIgnoreCase("Salaried")){
    				try
     				{
     					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
     				}
     				finally{hm.clear();}
     			}
     			if (pEvent.getSource().getName().equalsIgnoreCase("MOL")) {
    				/*hm.put("MOL","Clicked");
    				popupFlag="N";*/
    				formObject.fetchFragment("MOL", "MOL1", "q_Mol1");
    			
    				
    				try
    				{
    					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
    				}
    				finally{hm.clear();}
    			
    			}
     			if (pEvent.getSource().getName().equalsIgnoreCase("World_Check")) {
    				/*hm.put("World_Check","Clicked");
    				popupFlag="N";*/
    				formObject.fetchFragment("World_Check", "WorldCheck1", "q_WorldCheck");
    				
    				try
    				{
    					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
    				}
    				finally{hm.clear();}
    			}
    			if (pEvent.getSource().getName().equalsIgnoreCase("Reject_Enquiry")) {
    				/*hm.put("Reject_Enquiry","Clicked");
    				popupFlag="N";*/
    				formObject.fetchFragment("Reject_Enquiry", "RejectEnq", "q_RejectEnq");
    					//if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6).equalsIgnoreCase("Salaried")){
    				
    				//}
    				
    				try
    				{
    					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
    				}
    				finally{hm.clear();}
    			}
    			if (pEvent.getSource().getName().equalsIgnoreCase("Salary_Enquiry")) {
    				/*hm.put("Salary_Enquiry","Clicked");
    				popupFlag="N";*/
    				formObject.fetchFragment("Salary_Enquiry", "SalaryEnq", "q_SalaryEnq");
    				//if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6).equalsIgnoreCase("Salaried")){
    				
    				//}
    				
    				try
    				{
    					throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
    				}
    				finally{hm.clear();}
    			}
        		//ended by yash for CC FSD
       break;
                
          case FRAGMENT_LOADED:
        	  SKLogger_CC.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			 	
				/*if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
      			
				}*/
        	  
        	  //added by yash for CC FSD
					if (pEvent.getSource().getName().equalsIgnoreCase("Customer")) {
						disableButtonsCC("Customer");
						formObject.setLocked("Customer_Frame1",true);
						loadPicklistCustomer();
						
					}	
					//added by yash for CC FSD
					if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
						formObject.setLocked("Product_Frame1",true);
						LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
						LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
						LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
						disableButtonsCC("Product");
					}
					//added by yash for CC FSD
					if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails")) {
						formObject.setLocked("IncomeDetails_Frame1",true);
						disableButtonsCC("IncomeDetails");
						loadpicklist_Income();
					}
					//added by yash for CC FSD
					if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")) {
						formObject.setLocked("ExtLiability_Frame1",true);
						disableButtonsCC("Liability_New");
					}
					//added by yash for CC FSD
					if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) {
						formObject.setLocked("EMploymentDetails_Frame1", true);	
						disableButtonsCC("EMploymentDetails");
						loadPicklistEmployment();
					}
					//added by yash for CC FSD
					if (pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo")) {
						formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
						disableButtonsCC("ELigibiltyAndProductInfo");
					}
					//added by yash for CC FSD
					if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails")) {
						formObject.setLocked("AddressDetails_Frame1",true);
						loadPicklist_Address();
						disableButtonsCC("AddressDetails");
					}
					//added by yash for CC FSD
					if (pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails")){
						
						formObject.setLocked("AltContactDetails_Frame1",true);
						disableButtonsCC("AltContactDetails");
					} 
					//added by yash for CC FSD
					if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")){
						formObject.setLocked("FATCA_Frame6",true);
						//disableButtonsCC("FATCA");
					}
					//added by yash for CC FSD
					if (pEvent.getSource().getName().equalsIgnoreCase("KYC")){
						formObject.setLocked("KYC_Frame1",true);
						
					}
					//added by yash for CC FSD
					if (pEvent.getSource().getName().equalsIgnoreCase("OECD")){
						formObject.setLocked("OECD_Frame8",true);
						//disableButtonsCC("OECD");
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("IncomingDocument")){
						formObject.setLocked("IncomingDocument_Frame",true);
						disableButtonsCC("IncomingDocument");
					}
					//added by yash for CC FSD
					if (pEvent.getSource().getName().equalsIgnoreCase("Reference_Details")) {
						formObject.setLocked("Reference_Details_ReferenceRelationship",true);
						//disableButtonsCC("Reference_Details");
						
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails")) {
						formObject.setLocked("SupplementCardDetails_Frame1",true);
						disableButtonsCC("SupplementCardDetails");
						
					}
					//added by yash for CC FSD
					if (pEvent.getSource().getName().equalsIgnoreCase("Compliance")) {
						formObject.setLocked("Compliance_Frame1",true);
						//disableButtonsCC("SupplementCardDetails");
						
					}
					//ended by yash for CC FSD
					if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails")) {
						formObject.setLocked("CardDetails_Frame1",true);
						disableButtonsCC("CardDetails");
						
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails")) {
						formObject.setLocked("CompanyDetails_Frame1",true);
			               disableButtonsCC("CompanyDetails");
			               
							LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_AppType", "select '--Select--' union select convert(varchar, description) from NG_MASTER_ApplicantType with (nolock)");
			                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusSector", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
			                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusMAcro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
			                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusMicro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
			                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_legalEntity", "select '--Select--' union select convert(varchar, description) from NG_MASTER_LegalEntity with (nolock)");
			                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
			                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_desigVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
			                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_emirateOfWork", "select '--Select--' union select convert(varchar, description) from NG_MASTER_State with (nolock)");
			                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_headOfficeEmirate", "select '--Select--' union select convert(varchar, description) from NG_MASTER_State with (nolock)");
			            }
					if (pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails")) {
						formObject.setLocked("AuthorisedSignDetails_ShareHolding",true);
		            	 disableButtonsCC("AuthorisedSignDetails");
		            	LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
		                LoadPickList("AuthorisedSignDetails_SignStatus", "select '--Select--' union select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");
		                LoadPickList("Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
						LoadPickList("DesignationAsPerVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
						}
					if (pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails")) {
						formObject.setLocked("PartnerDetails_Frame1",true);
						 disableButtonsCC("PartnerDetails");
		                LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
		            }
// added by yash CC FSD
					if (pEvent.getSource().getName().equalsIgnoreCase("CC_Loan")) {
						//loadPicklist_Address();
						formObject.setLocked("CC_Loan_Frame1",true);
						
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("PartMatch")) {
						//loadPicklist_Address();
						formObject.setLocked("PartMatch_Frame1",true);
						
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMIncident")) {
						//loadPicklist_Address();
						formObject.setLocked("FinacleCRMIncident_Frame1",true);
						
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMCustInfo")) {
						//loadPicklist_Address();
						formObject.setLocked("FinacleCRMCustInfo_Frame1",true);
						
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("ExternalBlackList")) {
						//loadPicklist_Address();
						formObject.setLocked("ExternalBlackList_Frame1",true);
						
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCore")) {
						//loadPicklist_Address();
						formObject.setLocked("FinacleCore_Frame1",true);
						
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("MOL1")) {
						//loadPicklist_Address();
						formObject.setLocked("MOL1_Frame1",true);
						
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1")) {
						//loadPicklist_Address();
						formObject.setLocked("WorldCheck1_Frame1",true);
						
					}
					
					//added by yash for CC FSD
					if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails")) {
						
						notepad_withoutTelLog();
						formObject.setLocked("NotepadDetails_Frame2",true);
						formObject.setVisible("NotepadDetails_SaveButton",true);
						formObject.setTop("NotepadDetails_savebutton",410);
					}
					//added by yash for CC FSD
					if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
						
						formObject.setVisible("DecisionHistory_CheckBox1",false);
						formObject.setVisible("DecisionHistory_Label1",false);
						formObject.setVisible("cmplx_DEC_VerificationRequired",false);
						formObject.setVisible("DecisionHistory_Label3",false);
						formObject.setVisible("DecisionHistory_Combo3",false);
						formObject.setVisible("DecisionHistory_Label6",false);
						formObject.setVisible("DecisionHistory_Combo6",false);
						formObject.setVisible("Decision_Label4",false);
						formObject.setVisible("cmplx_DEC_Remarks",false);
						formObject.setVisible("DecisionHistory_Label8",false);
						formObject.setVisible("DecisionHistory_Text4",false);
						formObject.setVisible("DecisionHistory_Label7",false);
						formObject.setVisible("DecisionHistory_Text3",false);
						formObject.setVisible("DecisionHistory_Label2",false);
						formObject.setVisible("DecisionHistory_Text2",false);
						formObject.setVisible("cmplx_DEC_Description",false);
						formObject.setVisible("cmplx_DEC_Strength",false);
						formObject.setVisible("cmplx_DEC_Weakness",false);
						formObject.setVisible("cmplx_DEC_ContactPointVeri",false);  
						//added byas yash for CC FSD
						formObject.setVisible("DecisionHistory_chqbook",false);
						formObject.setVisible("cmplx_DEC_NewAccNo",false);
						formObject.setVisible("cmplx_DEC_IBAN_No",false);
						formObject.setVisible("cmplx_DEC_ChequebookRef",false);
						formObject.setVisible("DecisionHistory_Label9",false);
						formObject.setVisible("cmplx_DEC_DCR_Refno",false);
						formObject.setVisible("DecisionHistory_Text15",false);
						formObject.setVisible("DecisionHistory_Text16",false);
						formObject.setVisible("DecisionHistory_Text17",false);
						formObject.setVisible("DecisionHistory_Label27",false);
						formObject.setVisible("cmplx_DEC_Cust_Contacted",false);
						formObject.setVisible("DecisionHistory_Label5",false);
						formObject.setVisible("DecisionHistory_Decision_Label4",false);
						formObject.setVisible("DecisionHistory_Decision_Label1",false);
						formObject.setVisible("DecisionHistory_Label4",false);
						formObject.setTop("DecisionHistory_Decision_Label3",7);
						
						formObject.setTop("cmplx_DEC_Decision",23);
						formObject.setLeft("cmplx_DEC_Decision",23);
						formObject.setVisible("DecisionHistory_Label1",false);
						formObject.setVisible("DecisionHistory_Label11",true);
						formObject.setTop("DecisionHistory_Label11",7);
						formObject.setLeft("DecisionHistory_Label11",304);
						formObject.setVisible("cmplx_DEC_ReferReason",false);
						formObject.setVisible("cmplx_DEC_Decision_Reasoncode",true);
						formObject.setTop("cmplx_DEC_Decision_Reasoncode",23);
						formObject.setLeft("cmplx_DEC_Decision_Reasoncode",304);
						formObject.setVisible("DecisionHistory_Rejreason",true);
						formObject.setTop("DecisionHistory_Rejreason",7);
						formObject.setLeft("DecisionHistory_Rejreason",586);	
						formObject.setVisible("cmplx_DEC_RejectReason",true);
						formObject.setTop("cmplx_DEC_RejectReason",23);
						formObject.setLeft("cmplx_DEC_RejectReason",586);
						formObject.setLocked("cmplx_DEC_RejectReason",false);
						formObject.setVisible("DecisionHistory_Decision_ListView1",true);
						formObject.setTop("DecisionHistory_Decision_ListView1",65);
						formObject.setVisible("DecisionHistory_save",true);
						formObject.setTop("DecisionHistory_save",240);
						
						
						disableButtonsCC("DecisionHistory");
						
	                	loadPicklist3();
				 } 	
			
			
			  break;
			  
			case MOUSE_CLICKED:
				SKLogger_CC.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
				if (pEvent.getSource().getName().equalsIgnoreCase("Customer_ReadFromCard")){
					//GenerateXML();
				}
				
				if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_addr_Add")){
					formObject.setNGValue("Address_wi_name",formObject.getWFWorkitemName());
					SKLogger_CC.writeLog("PL", "Inside add button: "+formObject.getNGValue("Address_wi_name"));
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_AddressDetails_cmplx_AddressGrid");
				}
				
				if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_addr_Modify")){
					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_AddressDetails_cmplx_AddressGrid");
				}
				
				if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_addr_Delete")){
					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_AddressDetails_cmplx_AddressGrid");

				}

			
				if(pEvent.getSource().getName().equalsIgnoreCase("Customer_save")){
					SKLogger_CC.writeLog("PL_Initiation", "Inside Customer_save button: ");
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
				//added by yash on 24/8/2017
				if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_SaveButton")){
					formObject.saveFragment("Notepad_Values");
					alert_msg="Notepad Details Saved";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}
				if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_save")){
					formObject.saveFragment("Notepad_Values");
					alert_msg="Notepad Details Saved";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}
				if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_save")){
					formObject.saveFragment("DecisionHistory");
					alert_msg="Decision History Details Saved";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}
				//added by yash for CC FSD
				if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Add")){
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_notegrid");
					Notepad_add();
				}
				if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Modify")){
					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_NotepadDetails_cmplx_notegrid");
					Notepad_modify();
				}
				if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Delete")){
					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_NotepadDetails_cmplx_notegrid");
					Notepad_delete();
				}
				if(pEvent.getSource().getName().equalsIgnoreCase("cmplx_NotepadDetails_cmplx_notegrid")){
					
					Notepad_grid();
					
				} 
				//ended by yash for CC FSD
				if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Add1")){
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_Telloggrid");
				}
				if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Clear")){
					formObject.ExecuteExternalCommand("NGClear", "cmplx_NotepadDetails_cmplx_Telloggrid");
				}
				//ended by yash on 24/8/2017
				
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
				// ++ below code not commented at offshore - 06-10-2017
				if(pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails_ContactDetails_Save")){
					formObject.saveFragment("Alt_Contact_container");
				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("CardDetails_save")){
					formObject.saveFragment("Supplementary_Cont");
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
				
				
				if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Button1")){
					formObject.saveFragment("DecisionHistory");
				}
		
			
			 case VALUE_CHANGED:
					SKLogger_CC.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
					 if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_DEC_Decision")) {
						 if(formObject.getWFActivityName().equalsIgnoreCase("CAD_Analyst1"))	
						 {
							 formObject.setNGValue("CAD_dec", formObject.getNGValue("cmplx_DEC_Decision"));
							SKLogger_CC.writeLog(" In PL_Initiation VALChanged---New Value of CAD_dec is: ", formObject.getNGValue("cmplx_DEC_Decision"));

						 }
						 
						 else{
							 
							formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
							SKLogger_CC.writeLog(" In PL_Initiation VALChanged---New Value of decision is: ", formObject.getNGValue("cmplx_DEC_Decision"));
						 	  }
					 	}
          
					 //added by yash on 30/8/2017
					 if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_notedesc")){
						 String notepad_desc = formObject.getNGValue("NotepadDetails_notedesc");
						 //LoadPickList("NotepadDetails_notecode", "select '--Select--' union select convert(varchar, description) from ng_master_notedescription with (nolock)  where Description=q'["+notepad_desc+"]'","NotepadDetails_notecode");
						 String sQuery = "select code from ng_master_notedescription where Description='" +  notepad_desc + "'";
						 SKLogger_CC.writeLog(" query is ",sQuery);
						 List<List<String>> recordList = formObject.getDataFromDataSource(sQuery);
						 if(recordList.get(0).get(0)!= null && recordList.get(0)!=null && !recordList.get(0).get(0).equalsIgnoreCase("") && recordList!=null)
						 {
							 formObject.setNGValue("NotepadDetails_notecode",recordList.get(0).get(0));
						 }
						 
					
						 
					 }
          
         
                  default: break;
	     
	     }

	}	
	
	
	public void initialize() {
		SKLogger_CC.writeLog("PersonnalLoanS>  PL_Iniation", "Inside PL PROCESS initialize()" );
		  formObject =FormContext.getCurrentInstance().getFormReference();

	}

	
	public void saveFormCompleted(FormEvent pEvent) throws ValidatorException {
		SKLogger_CC.writeLog("PersonnalLoanS>  PL_Iniation", "Inside PL PROCESS saveFormCompleted()" + pEvent.getSource());
		  formObject =FormContext.getCurrentInstance().getFormReference();

	}

	
	public void saveFormStarted(FormEvent pEvent) throws ValidatorException {
		  formObject =FormContext.getCurrentInstance().getFormReference();
		SKLogger_CC.writeLog("PersonnalLoanS>  PL_Iniation", "Inside PL PROCESS saveFormStarted()" + pEvent.getSource());
		formObject.setNGValue("get_parent_data",queryData_load);
		SKLogger_CC.writeLog("PersonnalLoanS","Final val of queryData_load:"+ formObject.getNGValue("get_parent_data"));
	}

	
	public void submitFormCompleted(FormEvent pEvent) throws ValidatorException {
		SKLogger_CC.writeLog("PersonnalLoanS>  PL_Iniation", "Inside PL PROCESS submitFormCompleted()" + pEvent.getSource());
		
	}

	
	public void submitFormStarted(FormEvent pEvent) throws ValidatorException {
		SKLogger_CC.writeLog("PersonnalLoanS>  PL_Iniation", "Inside PL PROCESS submitFormStarted()" + pEvent.getSource());
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();	
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
		saveIndecisionGrid();
	}


	public void continueExecution(String arg0, HashMap<String, String> arg1) {
		// TODO Auto-generated method stub
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();	
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
		
	}

}

