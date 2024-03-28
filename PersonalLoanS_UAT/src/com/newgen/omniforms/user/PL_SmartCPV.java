/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PL_SmartCPV.java
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

import java.util.HashMap;

import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.excp.CustomExceptionHandler;
import com.newgen.omniforms.listener.FormListener;
import com.newgen.omniforms.util.PL_SKLogger;

public class PL_SmartCPV extends PLCommon implements FormListener 
{
	boolean IsFragLoaded=false;
	String queryData_load="";
	 FormReference formObject = null;
	public void formLoaded(FormEvent pEvent)
	{
		System.out.println("Inside initiation PL");
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
		String alert_msg="";
		PL_SKLogger.writeLog("Inside PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		  formObject =FormContext.getCurrentInstance().getFormReference();

				switch(pEvent.getType())
				{	

					case FRAME_EXPANDED:
					PL_SKLogger.writeLog(" In PL_Iniation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

					new PersonalLoanSCommonCode().FrameExpandEvent(pEvent);
					
					break;
					
					case FRAGMENT_LOADED:
						PL_SKLogger.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
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
							
							if (pEvent.getSource().getName().equalsIgnoreCase("CustDetailVerification")) {
								
								formObject.setLocked("CustDetailVerification_Frame1",true);
								
								/*formObject.setLocked("cmplx_CustDetailVerification_Mob_No1_val",true);
					 			formObject.setLocked("cmplx_CustDetailVerification_Mob_No2_val",true);
					 			formObject.setLocked("cmplx_CustDetailVerification_dob_val",true);
					 			formObject.setLocked("cmplx_CustDetailVerification_POBoxNo_val",true);
					 			formObject.setLocked("cmplx_CustDetailVerification_emirates_val",true);
					 			formObject.setLocked("cmplx_CustDetailVerification_persorcompPOBox_val",true);
					 			formObject.setLocked("cmplx_CustDetailVerification_Resno_val",true);
					 			formObject.setLocked("cmplx_CustDetailVerification_Offtelno_val",true);
					 			formObject.setLocked("cmplx_CustDetailVerification_hcountrytelno_val",true);
					 			formObject.setLocked("cmplx_CustDetailVerification_hcountryaddr_val",true);
					 			formObject.setLocked("cmplx_CustDetailVerification_email1_val",true);
					 			formObject.setLocked("cmplx_CustDetailVerification_email2_val",true);*/
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("BussinessVerification")) {
								
					 			formObject.setLocked("BussinessVerification_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("HomeCountryVerification")) {
								
					 			formObject.setLocked("HomeCountryVerification_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("ResidenceVerification")) {
								
					 			formObject.setLocked("ResidenceVerification_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("GuarantorVerification")) {
								
					 			formObject.setLocked("GuarantorVerification_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetailVerification")) {
								
					 			formObject.setLocked("ReferenceDetailVerification_Frame1",true);
							}							
							
							if (pEvent.getSource().getName().equalsIgnoreCase("OfficeandMobileVerification")) {
								
								formObject.setLocked("OfficeandMobileVerification_Frame1",true);
								
								/*formObject.setLocked("cmplx_OffVerification_fxdsal_val",true);
					 			formObject.setLocked("cmplx_OffVerification_accprovd_val",true);
					 			formObject.setLocked("cmplx_OffVerification_desig_val",true);
					 			formObject.setLocked("cmplx_OffVerification_doj_val",true);
					 			formObject.setLocked("cmplx_OffVerification_cnfrminjob_val",true);*/
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("LoanandCard")) {
								
								formObject.setLocked("LoanandCard_Frame1",true);
								
								/*formObject.setLocked("cmplx_LoanandCard_loanamt_val",true);
					 			formObject.setLocked("cmplx_LoanandCard_tenor_val",true);
					 			formObject.setLocked("cmplx_LoanandCard_emi_val",true);
					 			formObject.setLocked("cmplx_LoanandCard_islorconv_val",true);
					 			formObject.setLocked("cmplx_LoanandCard_firstrepaydate_val",true);
					 			formObject.setLocked("cmplx_LoanandCard_cardtype_val",true);
					 			formObject.setLocked("cmplx_LoanandCard_cardlimit_val",true);*/
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails")) {
								
					 			formObject.setLocked("NotepadDetails_Frame1",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("SmartCheck1")) 
							{
								formObject.setVisible("SmartCheck1_Label1",false);
								formObject.setVisible("SmartCheck1_credrem",false);
								formObject.setVisible("SmartCheck1_Label4",false);
								formObject.setVisible("SmartCheck1_FCUrem",false);
								formObject.setLocked("SmartCheck1_Add",true);
								formObject.setLocked("SmartCheck1_Delete",true);
							}							
							
							if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
			                	loadPicklist1();
			                	//Common function for decision fragment textboxes and combo visibility
			                	//decisionLabelsVisibility();
						 } 	
							
							if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails")) {
								
					 			formObject.setLocked("NotepadDetails_Frame1",true);
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
						
						if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Add")){
							//formObject.setNGValue("Address_wi_name",formObject.getWFWorkitemName());
							//SKLogger.writeLog("PL", "Inside add button: "+formObject.getNGValue("Address_wi_name"));
							formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_notegrid");
						}
						
						if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Modify")){
							formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_NotepadDetails_cmplx_notegrid");
						}
						
						if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Delete")){
							formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_NotepadDetails_cmplx_notegrid");

						}
						if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Add1")){
							//formObject.setNGValue("Address_wi_name",formObject.getWFWorkitemName());
							//SKLogger.writeLog("PL", "Inside add button: "+formObject.getNGValue("Address_wi_name"));
							formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_Telloggrid");
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Clear")){
							formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_NotepadDetails_cmplx_Telloggrid");

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
						// disha FSD
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
						if(pEvent.getSource().getName().equalsIgnoreCase("CustDetailVerification_save")){
							formObject.saveFragment("Cust_Detail_verification");
							alert_msg="CustDetailVerification saved";
							throw new ValidatorException(new FacesMessage(alert_msg));
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("BussinessVerification_save")){
							formObject.saveFragment("Business_verification");
							alert_msg="Customer details saved";
							throw new ValidatorException(new FacesMessage(alert_msg));
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("HomeCountryVerification_save")){
							formObject.saveFragment("Home_country_verification");
							alert_msg="Home_country_verification saved";
							throw new ValidatorException(new FacesMessage(alert_msg));
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("ResidenceVerification_save")){
							formObject.saveFragment("Residence_verification");
							alert_msg="Residence_verification saved";
							throw new ValidatorException(new FacesMessage(alert_msg));
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("GuarantorVerification_save")){
							formObject.saveFragment("Guarantor_verification");
							alert_msg="Guarantor_verification saved";
							throw new ValidatorException(new FacesMessage(alert_msg));
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetailVerification_save")){
							formObject.saveFragment("Reference_detail_verification");
							alert_msg="Reference_detail_verification saved";
							throw new ValidatorException(new FacesMessage(alert_msg));
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("OfficeandMobileVerification_save")){
							formObject.saveFragment("Office_verification");
							alert_msg="Office_verification saved";
							throw new ValidatorException(new FacesMessage(alert_msg));
						}
						
						
						if(pEvent.getSource().getName().equalsIgnoreCase("LoanandCard_save")){
							formObject.saveFragment("Loan_card_details");
							alert_msg="Loan_card_details saved";
							throw new ValidatorException(new FacesMessage(alert_msg));
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_save")){
							formObject.saveFragment("Notepad_Values");
							alert_msg="Notepad details saved";
							throw new ValidatorException(new FacesMessage(alert_msg));
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("SmartCheck_save")){
							formObject.saveFragment("Smart_check");
							alert_msg="Smart_check saved";
							throw new ValidatorException(new FacesMessage(alert_msg));
						}
						
						// disha FSD
						if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Save")){
							formObject.saveFragment("DecisionHistory");
						}
				
						if(pEvent.getSource().getName().equalsIgnoreCase("HomeCountryVerification_Button1")){
							formObject.saveFragment("Frame7");
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("ResidenceVerification_Button1")){
							formObject.saveFragment("Frame8");
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("BussinessVerification_Button1")){
							formObject.saveFragment("Frame6");
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("OfficeandMobileVerification_Button1")){
							formObject.saveFragment("Frame11");
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("GuarantorVerification_Button1")){
							formObject.saveFragment("Frame9");
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetailVerification_Button1")){
							formObject.saveFragment("Frame10");
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("CustDetailVerification_Button1")){
							formObject.saveFragment("Frame5");
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("LoanandCard_Button1")){
							formObject.saveFragment("Frame13");
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Button3")){
							formObject.saveFragment("Frame14");
						}
						
						
						if(pEvent.getSource().getName().equalsIgnoreCase("SmartCheck1_Modify")){
							formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_SmartCheck1_SmartChkGrid_FCU");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("SmartCheck1_Add")){
							formObject.ExecuteExternalCommand("NGAddRow", "cmplx_SmartCheck1_SmartChkGrid_FCU");
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("SmartCheck1_Delete")){
							formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_SmartCheck1_SmartChkGrid_FCU");
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
		 //formObject.setNGValue("CPV_dec", formObject.getNGValue("cmplx_DecisionHistory_Decision"));
		saveIndecisionGrid();
	}

}
