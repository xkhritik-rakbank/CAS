
/*------------------------------------------------------------------------------------------------------

                     NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                             : Application -Projects

Project/Product                                                   : Rakbank  

Application                                                       : Credit Card

Module                                                            : Telesales Agent Review

File Name                                                         : CC_Telesales_Agent_Review

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

import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.excp.CustomExceptionHandler;
import com.newgen.omniforms.listener.FormListener;
import com.newgen.omniforms.util.SKLogger_CC;

public class CC_Telesales_Agent_Review extends CC_Common implements FormListener
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
		
		SKLogger_CC.writeLog("CC Initiation", "Inside formLoaded()" + pEvent.getSource().getName());
		
		 
		makeSheetsInvisible(tabName, "6,7,8,9,11,12,13,14,15,16,17");
		
	}
	

	public void formPopulated(FormEvent pEvent) 
	{
        try{
        
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
					formObject.fetchFragment("DecisionHistory", "DecisionHistory", "q_DecisonHistory");
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
        		
       break;
                
          case FRAGMENT_LOADED:
        	  SKLogger_CC.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			 	
        	  if (pEvent.getSource().getName().equalsIgnoreCase("Customer")) {
					disableButtonsCC("Customer");
					loadPicklistCustomer();
					
				}	
				
				if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
					LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
					LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
					LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
					disableButtonsCC("Product");
				}
				
				if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails")) {
					disableButtonsCC("IncomeDetails");
					loadpicklist_Income();
				}
				
				if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")) {
					disableButtonsCC("Liability_New");
				}
				
				if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) {
					disableButtonsCC("EMploymentDetails");
					loadPicklistEmployment();
				}
				
				if (pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo")) {
					disableButtonsCC("ELigibiltyAndProductInfo");
				}
				
				if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails")) {
					loadPicklist_Address();
					disableButtonsCC("AddressDetails");
				}
				
				if (pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails")){
					
					disableButtonsCC("AltContactDetails");
				} 
				
				if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")){
					
					disableButtonsCC("FATCA");
				}
				
				if (pEvent.getSource().getName().equalsIgnoreCase("KYC")){
					
					
				}
				
				if (pEvent.getSource().getName().equalsIgnoreCase("OECD")){
					
					disableButtonsCC("OECD");
				}
				if (pEvent.getSource().getName().equalsIgnoreCase("IncomingDocument")){
					
					disableButtonsCC("IncomingDocument");
				}
				if (pEvent.getSource().getName().equalsIgnoreCase("Reference_Details")) {
					disableButtonsCC("Reference_Details");
					
				}
				if (pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails")) {
					disableButtonsCC("SupplementCardDetails");
					
				}
				if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails")) {
					disableButtonsCC("CardDetails");
					
				}
				if (pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails")) {
		               disableButtonsCC("CompanyDetails");
		               
						/*LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_AppType", "select '--Select--' union select convert(varchar, description) from NG_MASTER_ApplicantType with (nolock)");
		                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusSector", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
		                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusMAcro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
		                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusMicro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
		                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_legalEntity", "select '--Select--' union select convert(varchar, description) from NG_MASTER_LegalEntity with (nolock)");
		                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
		                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_desigVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
		                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_emirateOfWork", "select '--Select--' union select convert(varchar, description) from NG_MASTER_State with (nolock)");
		                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_headOfficeEmirate", "select '--Select--' union select convert(varchar, description) from NG_MASTER_State with (nolock)");*/
		               loadPicklist_CompanyDet();
		            }
				if (pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails")) {
	            	 disableButtonsCC("AuthorisedSignDetails");
	            	LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
	                LoadPickList("AuthorisedSignDetails_SignStatus", "select '--Select--' union select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");
	            }
				if (pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails")) {
					 disableButtonsCC("PartnerDetails");
	                LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
	            }
				
				if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails")) {
					 String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
					 SKLogger_CC.writeLog("CCyash ", "Activity name is:" + sActivityName);
			        formObject.setNGValue("NotepadDetails_Actusername",formObject.getNGValue("cmplx_Customer_FirstNAme") +" "+ formObject.getNGValue("cmplx_Customer_MiddleNAme") +" "+ formObject.getNGValue("cmplx_Customer_LastNAme"));
			        formObject.setNGValue("NotepadDetails_user",formObject.getNGValue("cmplx_Customer_FirstNAme") +" "+ formObject.getNGValue("cmplx_Customer_MiddleNAme") +" "+ formObject.getNGValue("cmplx_Customer_LastNAme"));
			    	
					formObject.setNGValue("NotepadDetails_insqueue",sActivityName);
					formObject.setLocked("NotepadDetails_noteDate",true);
					formObject.setLocked("NotepadDetails_Actusername",true);
					formObject.setLocked("NotepadDetails_user",true);
					formObject.setLocked("NotepadDetails_insqueue",true);
					formObject.setLocked("NotepadDetails_Actdate",true);
					formObject.setVisible("NotepadDetails_Frame3",true);
					formObject.setVisible("NotepadDetails_save",false);
					formObject.setHeight("NotepadDetails_Frame3",260);
					formObject.setVisible("NotepadDetails_SaveButton",true);
					formObject.setTop("NotepadDetails_SaveButton",400);
					//formObject.setLocked("NotepadDetails_Frame1",true);
					
				
				
			}
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
					formObject.setVisible("cmplx_DEC_ReferReason",false);
					formObject.setVisible("cmplx_DEC_Description",false);
					formObject.setVisible("cmplx_DEC_Strength",false);
					formObject.setVisible("cmplx_DEC_Weakness",false);
					formObject.setVisible("cmplx_DEC_ContactPointVeri",false);   
					
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
					formObject.saveFragment("Supplementary_Cont");
				}
				
				
				if(pEvent.getSource().getName().equalsIgnoreCase("FATCA_Save")){
					formObject.saveFragment("FATCA");
				}
				//added by yash on 22/8/2017
				if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Button1")){
					formObject.saveFragment("DecisionHistory");
				}
				if(pEvent.getSource().getName().equalsIgnoreCase("KYC_Save")){
					formObject.saveFragment("KYC");
				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("OECD_Save")){
					formObject.saveFragment("OECD");
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
				//added by yash on 24/8/2017
				if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Add")){
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_notegrid");
				}
				if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Modify")){
					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_NotepadDetails_cmplx_notegrid");
				}
				if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Delete")){
					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_NotepadDetails_cmplx_notegrid");
				}
				if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Add1")){
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_Telloggrid");
				}
				if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Clear")){
					formObject.ExecuteExternalCommand("NGClear", "cmplx_NotepadDetails_cmplx_Telloggrid");
				}
				//ended by yash on 24/8/2017
				
			
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
						 SKLogger_CC.writeLog(" queryyash is ",sQuery);
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


	public String decrypt(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}


	public String encrypt(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}

