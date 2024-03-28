/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PL_FCU.java
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


import com.newgen.omniforms.component.IRepeater;
import com.newgen.omniforms.excp.CustomExceptionHandler;













import javax.faces.application.*;

public class PL_FCU extends PLCommon implements FormListener
{
	boolean IsFragLoaded=false;
	String queryData_load="";
	 FormReference formObject = null;
	public void formLoaded(FormEvent pEvent)
	{
		System.out.println("Inside initiation PL_FCU");
		PL_SKLogger.writeLog("PL_FCU Initiation", "Inside formLoaded()" + pEvent.getSource().getName());
		
		BussVerVisible();
		
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
							
							if (pEvent.getSource().getName().equalsIgnoreCase("RiskRating")) {
								
					 			formObject.setLocked("RiskRating_Frame1",true);
							}//Arun (15/09/2017)
							
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
							
							if (pEvent.getSource().getName().equalsIgnoreCase("CustDetailVerification1")) {
								
								custdet1values();
					 			formObject.setLocked("cmplx_CustDetailverification1_MobNo1_value",true);
					 			formObject.setLocked("cmplx_CustDetailverification1_MobNo2_value",true);
					 			formObject.setLocked("cmplx_CustDetailverification1_DOB_value",true);
					 			formObject.setLocked("cmplx_CustDetailverification1_PO_Box_Value",true);
					 			formObject.setLocked("cmplx_CustDetailverification1_Emirates_value",true);
					 			formObject.setLocked("cmplx_CustDetailverification1_Off_Telephone_value",true);
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("EmploymentVerification")) {
								
								EmpVervalues();
					 			formObject.setLocked("cmplx_EmploymentVerification_fixedsal_val",true);
					 			formObject.setLocked("cmplx_EmploymentVerification_AccomProvided_val",true);
					 			formObject.setLocked("cmplx_EmploymentVerification_Desig_val",true);
					 			formObject.setLocked("cmplx_EmploymentVerification_doj_val",true);
					 			formObject.setLocked("cmplx_EmploymentVerification_confirmedinJob_val",true);
					 			formObject.setLocked("cmplx_EmploymentVerification_OffTelNo",true);//Arun (16/09/17)
					 			LoadPickList("cmplx_EmploymentVerification_salaryTransferBank", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_BankName with (nolock)");//Arun (16/09/17)
					 		
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("BussinessVerification1")) {
								
					 			formObject.setLocked("cmplx_BussVerification1_TradeLicenseName",true);
					 			formObject.setLocked("cmplx_BussVerification1_Signatory_Name",true);
					 			formObject.setLocked("cmplx_BussVerification1_ContactTelephone",true);
					 			formObject.setNGValue("cmplx_BussVerification1_ContactTelephone",formObject.getNGValue("AlternateContactDetails_OFFICENO"));
					 			LoadPickList("cmplx_BussVerification1_CompanyEmirate", "select '--Select--','--Select--' union select convert(varchar, Description),code from ng_master_emirate with (nolock)");
					 		
							}//Arun (16/09/17)
							
							if (pEvent.getSource().getName().equalsIgnoreCase("SmartCheck1")) 
							{
								//formObject.setVisible("SmartCheck1_Label1",false);//Arun (07/09/17)
								//formObject.setVisible("SmartCheck1_credrem",false);//Arun (07/09/17)
								formObject.setVisible("SmartCheck1_Label2",false);
								formObject.setVisible("SmartCheck1_CPVrem",false);
								formObject.setLocked("SmartCheck1_Modify",true);
							}
							// disha FSD
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
								formObject.setLocked("NotepadDetails_notecode",true);
								
								formObject.setHeight("NotepadDetails_Frame1",450);
								LoadPickList("NotepadDetails_notedesc", "select '--Select--' union select  description from ng_master_notedescription");
							}
							
							if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
			                	loadPicklist1();
			                	LoadpicklistFCU();
			                	formObject.setVisible("cmplx_Decision_waiveoffver", false);
			                	formObject.setVisible("Decision_Label1", false);
			                	formObject.setVisible("cmplx_Decision_VERIFICATIONREQUIRED", false);
			                	formObject.setVisible("DecisionHistory_chqbook", false);
			                	formObject.setVisible("DecisionHistory_Label1", false);
			                	formObject.setVisible("cmplx_Decision_refereason", false);
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
			                	formObject.setVisible("DecisionHistory_Button5",false);//Arun
			                	formObject.setVisible("DecisionHistory_Button6",false);//Arun
			                	
			                	formObject.setVisible("DecisionHistory_Label19",true);
			                	formObject.setVisible("cmplx_Decision_ALOCcompany",true);
			                	formObject.setVisible("DecisionHistory_Label20",true);
			                	formObject.setVisible("cmplx_Decision_ALOCType",true);
			                	formObject.setVisible("DecisionHistory_Label21",true);
			                	formObject.setVisible("cmplx_Decision_Referralreason",true);
			                	formObject.setVisible("DecisionHistory_Label22",true);
			                	formObject.setVisible("cmplx_Decision_Referralsubreason",true);
			                	formObject.setVisible("DecisionHistory_Label23",true);
			                	formObject.setVisible("cmplx_Decision_feedbackstatus",true);
			                	formObject.setVisible("DecisionHistory_Label24",true);
			                	formObject.setVisible("cmplx_Decision_subfeedback",true);
			                	formObject.setVisible("DecisionHistory_Label25",true);
			                	formObject.setVisible("cmplx_Decision_Furtherreview",true);	
			                	
			                	formObject.setNGValue("Decision_Label3","Final Decision");
			                	formObject.setNGValue("Decision_Label4","Summary");
			                	
			                	LoadPickList("cmplx_Decision_Referralreason", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_Master_Referralreason with (nolock)");
			        			LoadPickList("cmplx_Decision_Referralsubreason", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_Master_Referralsubreason with (nolock)");
			        			
			        			formObject.setHeight("DecisionHistory_Frame1",446);
			        			
			        			formObject.setTop("DecisionHistory_Label19",10);
			        			formObject.setTop("cmplx_Decision_ALOCcompany",25);
			        			formObject.setTop("DecisionHistory_Label20",10);
			        			formObject.setTop("cmplx_Decision_ALOCType",25);
			        			formObject.setTop("DecisionHistory_Label21",10);
			        			formObject.setTop("cmplx_Decision_Referralreason",50);
			        			formObject.setTop("DecisionHistory_Label22",10);
			        			formObject.setTop("cmplx_Decision_Referralsubreason",25);
			        			formObject.setTop("DecisionHistory_Label23",10);
			        			formObject.setTop("cmplx_Decision_feedbackstatus",25);			        			
			        			formObject.setTop("DecisionHistory_Label24",60);
			        			formObject.setTop("cmplx_Decision_subfeedback",76);
			        			formObject.setTop("DecisionHistory_Label25",60);
			        			formObject.setTop("cmplx_Decision_Furtherreview",76);
			        			formObject.setLeft("DecisionHistory_Label25",528);
			        			formObject.setLeft("cmplx_Decision_Furtherreview",528);
			        			formObject.setLeft("Decision_Label3",272);
			        			formObject.setLeft("cmplx_Decision_Decision",272);
			        			formObject.setLeft("Decision_Label4",850);
			        			formObject.setLeft("cmplx_Decision_REMARKS",850);
			        			formObject.setTop("Decision_Label3",60);
			        			formObject.setTop("cmplx_Decision_Decision",76);
			        			formObject.setTop("Decision_Label4",60);
			        			formObject.setTop("cmplx_Decision_REMARKS",76);
			        			
			        			formObject.setTop("DecisionHistory_save",120);
			        			formObject.setTop("cmplx_Decision_cmplx_gr_decision",168);
			                	//formObject.setVisible("DecisionHistory_Modify", true);
			                	//formObject.setVisible("cmplx_Decision_Status", true);
			                	//formObject.setVisible("DecisionHistory_Label2", true);
			                	
			                	//Common function for decision fragment textboxes and combo visibility
			                	//decisionLabelsVisibility();
						 } 	//Arun (07/09/17)
					
					
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
						
						if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetailsFCU_Add")){
							/*formObject.setNGValue("Address_wi_name",formObject.getWFWorkitemName());
							PL_SKLogger.writeLog("PL", "Inside add button: "+formObject.getNGValue("Address_wi_name"));*/
							formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NoteFCU_cmplx_gr_NoteFCU");
						}
						
						if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetailsFCU_Modify")){
							formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_NoteFCU_cmplx_gr_NoteFCU");
						}
						
						if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetailsFCU_Delete")){
							formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_NoteFCU_cmplx_gr_NoteFCU");

						}
						
						if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetailsFCU_add")){
							/*formObject.setNGValue("Address_wi_name",formObject.getWFWorkitemName());
							PL_SKLogger.writeLog("PL", "Inside add button: "+formObject.getNGValue("Address_wi_name"));*/
							formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NoteFCU_cmplx_gr_TellogFCU");
						}
						
						if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetailsFCU_Clear")){
							formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_NoteFCU_cmplx_gr_TellogFCU");

						}
						
						if (pEvent.getSource().getName().equalsIgnoreCase("SmartCheck1_Add")){
							/*formObject.setNGValue("Address_wi_name",formObject.getWFWorkitemName());
							PL_SKLogger.writeLog("PL", "Inside add button: "+formObject.getNGValue("Address_wi_name"));*/
							formObject.ExecuteExternalCommand("NGAddRow", "cmplx_SmartCheck1_SmartChkGrid_FCU");
						}
						
						if (pEvent.getSource().getName().equalsIgnoreCase("SmartCheck1_Modify")){
							formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_SmartCheck1_SmartChkGrid_FCU");
						}
						
						if (pEvent.getSource().getName().equalsIgnoreCase("SmartCheck1_Delete")){
							formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_SmartCheck1_SmartChkGrid_FCU");

						}
						
						if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Modify"))
							formObject.ExecuteExternalCommand("NGModifyRow","cmplx_Decision_cmplx_gr_decision");
						
					
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
						
						if(pEvent.getSource().getName().equalsIgnoreCase("CustDetailVerification1_save")){
							formObject.saveFragment("Cust_Det_Ver");
							String alert_msg="Customer Verification details saved";

							throw new ValidatorException(new FacesMessage(alert_msg));

							
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("BussinessVerification1_save")){
							formObject.saveFragment("Business_Verif");
						}	
						
						if(pEvent.getSource().getName().equalsIgnoreCase("EmploymentVerification_save")){
							formObject.saveFragment("Emp_Verification");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("BankingCheck_save")){
							formObject.saveFragment("Bank_Check");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetailsFCU_save")){
							formObject.saveFragment("Note_Details");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("SmartCheck1_save")){
							formObject.saveFragment("Smart_chk");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("supervisorsection_save")){
							formObject.saveFragment("Supervisor_section");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_save")){
							formObject.saveFragment("DecisionHistory");
						}
						// disha FSD
						if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_save")){
							formObject.saveFragment("Notepad_Values");
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
						
						else if (pEvent.getSource().getName().equalsIgnoreCase("CustDetailVerification1_Button2")) {
							String EmiratesID=formObject.getNGValue("cmplx_CustDetailverification1_EmiratesId");
							String CIFID=formObject.getNGValue("cmplx_CustDetailverification1_CIF_ID");
							PL_SKLogger.writeLog("PL", "EmiratesID$"+EmiratesID+"$");
							String query=null;
							if(EmiratesID.trim().equalsIgnoreCase(""))
								//query="select distinct(EmiratesId),CustName,CifId,remarks,userName,Decisiom from NG_RLOS_GR_DECISION where CifId Like '%"+CIFID+"%'";
							query="select CustName,CifId,remarks,userName,Decisiom from ng_rlos_gr_decision where CifId Like '%"+CIFID+"%'";
			               
							else
								//query="select distinct(EmiratesId),CustName,CifId,remarks,userName,Decisiom from NG_RLOS_GR_DECISION where EmiratesId Like '%"+EmiratesID + "%' or CifId Like '%"+CIFID+"'";
							query="select CustName,CifId,remarks,userName,Decisiom from ng_rlos_gr_decision where EmiratesID Like '%"+EmiratesID + "%' or CifId Like '%"+CIFID+"'";
			
							 PL_SKLogger.writeLog("PL", "query is: "+query);
								//populatePickListCustdetailWindow(query,"CustDetailVerification1_Button2", "Emirates ID, Customer Name,Cif ID,Fcu Remarks,FCU Analyst Name,FCU Decision", true, 20);
						            List<List<String>> list=formObject.getDataFromDataSource(query);
						            //m_objPickList.populateData(query);
						            PL_SKLogger.writeLog("PL","##Arun");
						            for (List<String> a : list) 
									{
										
										formObject.addItemFromList("cmplx_CustDetailverification1_CustDetVer_GRID", a);
									}
						                //SKLogger.writeLog("PL","Column "+i+"is: "+ m_objPickList.getSelectedValue().get(i));
						        
					
						}//Arun (15/09/2017)
				
					
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
							 
							 /*else if (pEvent.getSource().getName().equalsIgnoreCase("FeedbackStatus")){
								 	String FeedbackStatus = formObject.getNGValue("cmplx_FcuDecision_FeedBackStatus");
									SKLogger.writeLog("RLOS val change ", "Value of FeedBackStatus is:"+FeedbackStatus);
		
								}*/
							 if(pEvent.getSource().getName().equalsIgnoreCase("cmplx_Decision_Feedbackstatus"))
							 {
								 if(formObject.getWFActivityName().equalsIgnoreCase("FCU"))
								 {
									 PL_SKLogger.writeLog(" queryarun is ","inside feedback status");
									 LoadpicklistFCU();
									 PL_SKLogger.writeLog(" queryarun is ","inside feedback status after loadpicklist");
								 } 
							 }
							 // disha FSD
							 if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_notedesc")){
								 String notepad_desc = formObject.getNGValue("NotepadDetails_notedesc");
								 //LoadPickList("NotepadDetails_notecode", "select '--Select--' union select convert(varchar, description) from ng_master_notedescription with (nolock)  where Description=q'["+notepad_desc+"]'","NotepadDetails_notecode");
								 String sQuery = "select code from ng_master_notedescription where Description='" +  notepad_desc + "'";
								 PL_SKLogger.writeLog(" query is  NotepadDetails_notedesc ",sQuery);
								 List<List<String>> recordList = formObject.getDataFromDataSource(sQuery);
								 PL_SKLogger.writeLog(" query is  recordList 12345 ",recordList.get(0).get(0));
								 
									 formObject.setNGValue("NotepadDetails_notecode",recordList.get(0).get(0));
									 formObject.setNGValue("NotepadDetails_Workstep",recordList.get(0).get(1));
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

