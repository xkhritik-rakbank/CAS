
/*------------------------------------------------------------------------------------------------------

                     NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                             : Application -Projects

Project/Product                                                   : Rakbank  

Application                                                       : Credit Card

Module                                                            : Post Disbursal

File Name                                                         : CC_Post_Disbursal

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

import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;
import com.newgen.omniforms.util.SKLogger_CC;

public class CC_Post_Disbursal extends CC_Common implements FormListener
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
		
		SKLogger_CC.writeLog("CC CC_PostDisbursal", "Inside formLoaded()" + pEvent.getSource().getName());
		
		 
		makeSheetsInvisible(tabName, "7,8,9,11,12,13,14,15,16,17");
		formObject.setVisible("PostDisbursal",true);
	}
	

	public void formPopulated(FormEvent pEvent) 
	{
        try{
        	SKLogger_CC.writeLog("CC CC_PostDisbursal","Inside CC_PostDisbursal CC");
           new CC_CommonCode().setFormHeader(pEvent);
        }catch(Exception e)
        {
            SKLogger_CC.writeLog("CC CC_PostDisbursal", "Exception:"+e.getMessage());
        }
    }
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException {
		SKLogger_CC.writeLog("Inside CC_PostDisbursal eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
	  formObject =FormContext.getCurrentInstance().getFormReference();

      switch(pEvent.getType()) {

          case FRAME_EXPANDED:
				SKLogger_CC.writeLog(" In CC_PostDisbursal eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
				
				new CC_CommonCode().FrameExpandEvent(pEvent);						

        		
       break;
                
          case FRAGMENT_LOADED:
        	  SKLogger_CC.writeLog(" In CC_PostDisbursal eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			 	
				/*if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
      			
				}*/
					if (pEvent.getSource().getName().equalsIgnoreCase("Customer")) {
						formObject.setLocked("Customer_Frame1",true);
						//formObject.setLocked("Product_Frame1",true);
						/*formObject.setEnabled("Customer_save",true);
						formObject.setLocked("cmplx_Customer_ReferrorCode",false);
						formObject.setLocked("cmplx_Customer_ReferrorName",false);
						formObject.setLocked("cmplx_Customer_AppType",false);
						formObject.setLocked("cmplx_Customer_corporateCode",false);
						formObject.setLocked("cmplx_Customer_Bankingwithus",false);
						formObject.setLocked("cmplx_Customer_noofDependent",false);
						formObject.setLocked("cmplx_Customer_guardian",false);
						formObject.setLocked("cmplx_Customer_minor",false);
						formObject.setEnabled("Customer_Reference_Add",true);
						formObject.setEnabled("Customer_Reference__modify",true);
						formObject.setEnabled("Customer_Reference_delete",true);*/
						loadPicklistCustomer();
						
					}	
					
					if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
						//Code commented by deepak as we are loading desc so no need to load the picklist(grid is already having desc) - 28Sept2017
						/*
						LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
						LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
						LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
						*/
						formObject.setLocked("Product_Frame1",true);
						/*formObject.setEnabled("Product_Save",true);
						formObject.setEnabled("Product_Add",true);
						formObject.setEnabled("Product_Modify",true);
						formObject.setEnabled("Product_Delete",true);*/
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails")) {
						formObject.setLocked("IncomeDetails_Frame1",true);
						//formObject.setEnabled("IncomeDetails_Salaried_Save",true);
						loadpicklist_Income();
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")) {
						formObject.setLocked("ExtLiability_Frame1",true);
						/*formObject.setEnabled("Liability_New_AECBReport",true);
						formObject.setEnabled("Liability_New_Save",true);*/
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) {
						formObject.setLocked("EMploymentDetails_Frame1",true);
						loadPicklistEmployment();
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo")) {
						formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
						/*formObject.setEnabled("ELigibiltyAndProductInfo_Save",true);*/
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails")) {
						loadPicklist_Address();
						formObject.setLocked("AddressDetails_Frame1",true);
						//loadPicklist_Address();
						/*formObject.setEnabled("AddressDetails_Save",true);
						formObject.setEnabled("AddressDetails_addr_Add",true);
						formObject.setEnabled("AddressDetails_addr_Modify",true);
						formObject.setEnabled("AddressDetails_addr_Delete",true);*/
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails")){
						
						formObject.setLocked("AltContactDetails_Frame1",true);
						/*formObject.setEnabled("AltContactDetails_ContactDetails_Save",true);*/
					} 
					
					if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")){
						
						formObject.setLocked("FATCA_Frame1",true);
						/*formObject.setEnabled("FATCA_Save",true);*/
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("KYC")){
						
						formObject.setLocked("KYC_Frame1",true);
						/*formObject.setEnabled("KYC_Save",true);*/
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("OECD")){
						
						formObject.setLocked("OECD_Frame1",true);
						/*formObject.setEnabled("OECD_Save",true);*/
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("IncomingDocument")){
						
						formObject.setLocked("IncomingDocument_Frame",true);
						/*formObject.setEnabled("OECD_Save",true);*/
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("Reference_Details")) {
						formObject.setLocked("Reference_Details_Frame1",true);
						
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails")) {
						formObject.setLocked("SupplementCardDetails_Frame1",true);
						
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails")) {
						formObject.setLocked("CardDetails_Frame1",true);
						
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails")) {
			               formObject.setLocked("CompanyDetails_Frame1", true);
			               
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
		            	 formObject.setLocked("AuthorisedSignDetails_ShareHolding", true);
			            // formObject.setLocked("AuthorisedSignDetails_Button4", true);
		            	LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
		                LoadPickList("AuthorisedSignDetails_SignStatus", "select '--Select--' union select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");
		            }
					if (pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails")) {
						formObject.setLocked("PartnerDetails_Frame1", true);
		                LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
		            }
					if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")) {
						formObject.setLocked("ExtLiability_Frame1",true);
						
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
						
						formObject.setVisible("DecisionHistory_CheckBox1",false);
                        formObject.setVisible("DecisionHistory_Label1",false);
                        formObject.setVisible("cmplx_DEC_VerificationRequired",false);
                        formObject.setVisible("DecisionHistory_Label3",false);
                        formObject.setVisible("DecisionHistory_Combo3",false);
                        formObject.setVisible("DecisionHistory_Label6",false);
                        formObject.setVisible("DecisionHistory_Combo6",false);
                        formObject.setVisible("DecisionHistory_Decision_Label4",false);
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
                        formObject.setLocked("cmplx_DEC_ContactPointVeri",true);
                        loadPicklist3();
				 } 	
			
			
			  break;
			  
			case MOUSE_CLICKED:
				SKLogger_CC.writeLog(" In CC_PostDisbursal eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
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
					SKLogger_CC.writeLog("CC_PostDisbursal", "Inside Customer_save button: ");
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
				
				
				if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Save")){
					formObject.saveFragment("DecisionHistoryContainer");
				}
		
			
			 case VALUE_CHANGED:
					SKLogger_CC.writeLog(" In CC_PostDisbursal eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
					 if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_DEC_Decision")) {
						 if(formObject.getWFActivityName().equalsIgnoreCase("CAD_Analyst1"))	
						 {
							 formObject.setNGValue("CAD_dec", formObject.getNGValue("cmplx_DEC_Decision"));
							SKLogger_CC.writeLog(" In CC_PostDisbursal VALChanged---New Value of CAD_dec is: ", formObject.getNGValue("cmplx_DEC_Decision"));

						 }
						 
						 else{
							 
							formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
							SKLogger_CC.writeLog(" In CC_PostDisbursal VALChanged---New Value of decision is: ", formObject.getNGValue("cmplx_DEC_Decision"));
						 	  }
					 	}
          
          
          
         
                  default: break;
	     
	     }

	}	
	
	
	public void initialize() {
		SKLogger_CC.writeLog("PersonnalLoanS>  CC_PostDisbursal", "Inside PL PROCESS initialize()" );
		  formObject =FormContext.getCurrentInstance().getFormReference();

	}

	
	public void saveFormCompleted(FormEvent pEvent) throws ValidatorException {
		SKLogger_CC.writeLog("PersonnalLoanS>  CC_PostDisbursal", "Inside PL PROCESS saveFormCompleted()" + pEvent.getSource());
		  formObject =FormContext.getCurrentInstance().getFormReference();

	}

	
	public void saveFormStarted(FormEvent pEvent) throws ValidatorException {
		  formObject =FormContext.getCurrentInstance().getFormReference();
		SKLogger_CC.writeLog("PersonnalLoanS>  CC_PostDisbursal", "Inside PL PROCESS saveFormStarted()" + pEvent.getSource());
		formObject.setNGValue("get_parent_data",queryData_load);
		SKLogger_CC.writeLog("PersonnalLoanS","Final val of queryData_load:"+ formObject.getNGValue("get_parent_data"));
	}

	
	public void submitFormCompleted(FormEvent pEvent) throws ValidatorException {
		SKLogger_CC.writeLog("PersonnalLoanS>  CC_PostDisbursal", "Inside PL PROCESS submitFormCompleted()" + pEvent.getSource());
		
	}

	
	public void submitFormStarted(FormEvent pEvent) throws ValidatorException {
		SKLogger_CC.writeLog("PersonnalLoanS>  CC_PostDisbursal", "Inside PL PROCESS submitFormStarted()" + pEvent.getSource());
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

