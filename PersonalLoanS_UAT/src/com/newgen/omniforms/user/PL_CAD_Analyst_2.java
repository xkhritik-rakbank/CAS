/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PL_CAD_Analyst_2.java
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import java.text.DateFormat;
import java.util.Date;

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

public class PL_CAD_Analyst_2 extends PLCommon implements FormListener
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
					
					break;
					
					case FRAGMENT_LOADED:
						PL_SKLogger.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
					 	
						/*if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
		        			
						}*/
					if (pEvent.getSource().getName().equalsIgnoreCase("Customer")) {
						//setDisabled();
						formObject.setLocked("Customer_Frame1",true);
						loadPicklistCustomer();
					}	

					if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
						formObject.setLocked("Product_Frame1",true);
						loadPicklistProduct("Personal Loan");
						//loadProductCombo();
						LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
						//LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
						/*String ReqProd=formObject.getNGValue("ReqProd");
						PL_SKLogger.writeLog("RLOS val change ", "Value of ReqProd is:"+ReqProd);
						loadPicklistProduct(ReqProd);*/
					}

					if (pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails")) {
	
						formObject.setLocked("GuarantorDetails_Frame1",true);
					}

					if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDEtails")) {
						/*
						String EmpType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6);
                        PL_SKLogger.writeLog("PL", "Emp Type Value is:"+EmpType);
               
						if(EmpType.equalsIgnoreCase("Salaried")|| EmpType.equalsIgnoreCase("Salaried Pensioner"))
                        {
                        	formObject.setVisible("IncomeDetails_Frame3", false);
                            formObject.setHeight("Incomedetails", 630);
                            formObject.setHeight("IncomeDetails_Frame1", 605);  
                        }
                        else if(EmpType.equalsIgnoreCase("Self Employed"))
                        {                                                                                                              
	                        formObject.setVisible("IncomeDetails_Frame2", false);
	                        formObject.setTop("IncomeDetails_Frame3",40);
	                        formObject.setHeight("Incomedetails", 300);
	                        formObject.setHeight("IncomeDetails_Frame1", 280);
                        }
						
						formObject.setLocked("IncomeDetails_Frame1",true);
						formObject.setLocked("IncomeDetails_Frame2",true);
						formObject.setVisible("IncomeDetail_Label13",false);
						formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsRakbankStat",false);
						formObject.setVisible("IncomeDetail_Label15",true); 
						formObject.setVisible("cmplx_IncomeDetails_Totavgother",true);
						formObject.setVisible("IncomeDetail_Label16",true);
						formObject.setVisible("cmplx_IncomeDetails_compaccAmt",true);*/
						
						formObject.setLocked("IncomeDetails_Frame1",true);
						formObject.setVisible("IncomeDetails_Frame3", false);
                        formObject.setHeight("Incomedetails", 630);
                        formObject.setHeight("IncomeDetails_Frame1", 605);  
                        formObject.setVisible("IncomeDetails_Label15",true); 
						formObject.setVisible("cmplx_IncomeDetails_Totavgother",true);
						formObject.setEnabled("cmplx_IncomeDetails_Totavgother",false);
						formObject.setVisible("IncomeDetails_Label16",true);
						formObject.setVisible("cmplx_IncomeDetails_compaccAmt",true);
						formObject.setEnabled("cmplx_IncomeDetails_compaccAmt",false);
						
					}

					if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")) {
						
						formObject.setLocked("ExtLiability_Frame1",true);
					}

					if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) {
	
						formObject.setLocked("EMploymentDetails_Frame1",true);
						loadPicklist4();
					}

					if (pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo")) {
	
						formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
					}

					if (pEvent.getSource().getName().equalsIgnoreCase("LoanDetails")) {
	
						formObject.setLocked("LoanDetails_Frame1",true);
						LoadPickList("cmplx_LoanDetails_insplan", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_Master_InstallmentPlan with (nolock) order by Code");
		                LoadPickList("cmplx_LoanDetails_collecbranch", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_COLLECTIONBRANCH with (nolock) order by code");
		                LoadPickList("cmplx_LoanDetails_ddastatus", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_DDASTATUS with (nolock) order by code");
		                LoadPickList("LoanDetails_modeofdisb", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_ModeofDisbursal with (nolock) order by code");
		                LoadPickList("LoanDetails_disbto", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_BankName with (nolock)");
		                LoadPickList("LoanDetails_holdcode", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_HoldCode with (nolock) order by code");
		                LoadPickList("cmplx_LoanDetails_paymode", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_PAYMENTMODE with (nolock)");
		                LoadPickList("cmplx_LoanDetails_status", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_STATUS with (nolock)");
		                LoadPickList("cmplx_LoanDetails_bankdeal", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_BankName with (nolock)");
		                LoadPickList("cmplx_LoanDetails_city", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_city with (nolock) order by code");
		                
					}
					// disha FSD
					if (pEvent.getSource().getName().equalsIgnoreCase("RiskRating")) {
						
						formObject.setLocked("RiskRating_Frame1",true);
					}
					
					
					if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails"))
					{
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
						formObject.setTop("NotepadDetails_save",400);
						LoadPickList("NotepadDetails_notedesc", "select '--Select--' union select  description from ng_master_notedescription");
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

					if (pEvent.getSource().getName().equalsIgnoreCase("PartMatch")) {
	
						formObject.setLocked("PartMatch_Frame1",true);
						LoadPickList("PartMatch_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
					}

					if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMIncident")) {
	
						formObject.setLocked("FinacleCRMIncident_Frame1",true);
					}

					if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMCustInfo")) {
	
						formObject.setLocked("FinacleCRMCustInfo_Frame1",true);
					}

					if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCore")) {
	
						formObject.setLocked("FinacleCore_Frame1",true);
					}

					if (pEvent.getSource().getName().equalsIgnoreCase("MOL1")) {
	
						formObject.setLocked("MOL1_Frame1",true);
					}

					if (pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1")) {
	
						formObject.setLocked("WorldCheck1_Frame1",true);
					}

					if (pEvent.getSource().getName().equalsIgnoreCase("RejectEnq")) {
	
						formObject.setLocked("RejectEnq_Frame1",true);
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("ExternalBlackList")) {
						
						formObject.setLocked("ExternalBlackList_Frame1",true);
					}

					if (pEvent.getSource().getName().equalsIgnoreCase("SalaryEnq")) {

						formObject.setLocked("SalaryEnq_Frame1",true);
					}

					if (pEvent.getSource().getName().equalsIgnoreCase("CustDetailVerification")) {
	
						formObject.setLocked("CustDetailVerification_Frame1",true);
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
					}

					if (pEvent.getSource().getName().equalsIgnoreCase("LoanandCard")) {
	
						formObject.setLocked("LoanandCard_Frame1",true);
					}

					if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails")) {
	
						//formObject.setLocked("NotepadDetails_Frame1",true);
					}

					if (pEvent.getSource().getName().equalsIgnoreCase("SmartCheck")) {

						//formObject.setLocked("SmartCheck_Frame1",true);
						
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails")) 
					{
						formObject.setLocked("AddressDetails_Frame1",true);
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("ReferHistory"))
					{
						formObject.setLocked("ReferHistory_Frame1",true);
					}
					// disha FSD
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
	                	formObject.setVisible("DecisionHistory_Label3",true);
	                	formObject.setVisible("cmplx_Decision_strength",true);
	                	formObject.setVisible("DecisionHistory_Label4",true);
	                	formObject.setVisible("cmplx_Decision_weakness",true);
	                	
	                	formObject.setVisible("DecisionHistory_Label15",true);
	                	formObject.setVisible("cmplx_Decision_score_grade",true);
	                	formObject.setVisible("DecisionHistory_Label17",true);
	                	formObject.setVisible("cmplx_Decision_LoanApprovalAuthority",true);			                	
	                	formObject.setVisible("DecisionHistory_Button4",true);			                	
	                	formObject.setVisible("DecisionHistory_Label18",true);
	                	formObject.setVisible("cmplx_Decision_CADDecisiontray",true);
	                	//formObject.setVisible("cmplx_Decision_ReferTo",true);
	                	formObject.setVisible("DecisionHistory_Label11",true);
	                	formObject.setVisible("cmplx_Decision_DecReasonCode",true);
	                	formObject.setVisible("DecisionHistory_Frame2",true);
	                	formObject.setVisible("DecisionHistory_Frame3",true);
	                	
	                	formObject.setVisible("cmplx_Decision_LoanApprovalAuthority",true);
	                	
	                	formObject.setTop("DecisionHistory_Label15",10);
	                	formObject.setTop("cmplx_Decision_score_grade",25);
	                	formObject.setLeft("DecisionHistory_Label15",272);
	                	formObject.setLeft("cmplx_Decision_score_grade",272);
	                	formObject.setTop("DecisionHistory_Label17",10);
	                	formObject.setTop("cmplx_Decision_LoanApprovalAuthority",25);
	                	
	                	formObject.setTop("DecisionHistory_Button4",10);
	                	formObject.setLeft("DecisionHistory_Button4",528);
	                	
	                	formObject.setTop("Decision_Label3",56);
	                	formObject.setTop("cmplx_Decision_Decision",72);
	                	formObject.setTop("DecisionHistory_Label18",56);
	                	formObject.setTop("cmplx_Decision_CADDecisiontray",92);
	                	
	                	//formObject.setTop("cmplx_Decision_ReferTo",72);
	                	formObject.setTop("DecisionHistory_Label11",56);
	                	formObject.setTop("cmplx_Decision_DecReasonCode",72);
	                	formObject.setLeft("DecisionHistory_Label11",528);
	                	formObject.setLeft("cmplx_Decision_DecReasonCode",528);
	                	formObject.setTop("Decision_Label4",118);
	                	formObject.setTop("cmplx_Decision_REMARKS",134);
	                	formObject.setLeft("Decision_Label4",776);
	                	formObject.setLeft("cmplx_Decision_REMARKS",776);
	                	
	                	formObject.setLeft("DecisionHistory_Label3",24);
	                	formObject.setLeft("cmplx_Decision_strength",24);
	                	
	                	formObject.setLeft("DecisionHistory_Label4",325);
	                	formObject.setLeft("cmplx_Decision_weakness",325);
	                	
	                	
	                	formObject.setTop("DecisionHistory_Label3",118);
	                	formObject.setTop("cmplx_Decision_strength",134);
	                	formObject.setTop("DecisionHistory_Label4",118);
	                	formObject.setTop("cmplx_Decision_weakness",134);
	                		                	
	                	
	                	formObject.setTop("DecisionHistory_Frame2",190);
	                	formObject.setTop("DecisionHistory_Frame3",375);
	                	formObject.setTop("DecisionHistory_Modify",458);
	                	formObject.setTop("Decision_ListView1",575);
	                	formObject.setTop("DecisionHistory_save",740);
	                	
	                	formObject.setNGValue("cmplx_Decision_LoanApprovalAuthority",formObject.getNGValue("cmplx_Decision_Highest_delegauth")==null?"":formObject.getNGValue("cmplx_Decision_Highest_delegauth") );
	                	
	                	formObject.setHeight("DecisionHistory_Frame1",1000);
	                	
	                	LoadPickList("cmplx_Decision_CADDecisiontray", "select '--Select--' union select convert(varchar, Refer_Credit) from ng_master_ReferCredit");
	                	int rowsExposure = formObject.getLVWRowCount("cmplx_Decision_ExposureDetails_grid");
                        int rowsDeviation = formObject.getLVWRowCount("cmplx_Decision_DeviationDetails_grid");
                        if(rowsExposure!=0){
                        formObject.clear("cmplx_Decision_ExposureDetails_grid");
                        }
	                	setExposureGridData();
                        if(rowsDeviation!=0){
                        formObject.clear("cmplx_Decision_DeviationDetails_grid");
                        }
                        setDeviationGridData();
                        
						//Common function for decision fragment textboxes and combo visibility
						//decisionLabelsVisibility();
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
						else if(pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails_Save")){
							formObject.saveFragment("EmploymentDetails");
							popupFlag = "Y";
                		String	alert_msg="Employment Details Saved";
                			throw new ValidatorException(new FacesMessage(alert_msg));
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

					
						if(pEvent.getSource().getName().equalsIgnoreCase("Customer_save")){
							PL_SKLogger.writeLog("PL_Initiation", "Inside Customer_save button: ");
							formObject.saveFragment("CustomerDetails");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("Product_Save")){
							formObject.saveFragment("ProductContainer");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails_Save")){
							formObject.saveFragment("GuarantorDet");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_Salaried_Save")){
							formObject.saveFragment("IncomeDetails");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_SelfEmployed_Save")){
							formObject.saveFragment("IncomeDetails");
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
						
						if(pEvent.getSource().getName().equalsIgnoreCase("CreditCardEnq_Save")){
							formObject.saveFragment("Credit_card_Enq");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("CaseHistoryReport_Save")){
							formObject.saveFragment("Case_History");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("LOS_Save")){
							formObject.saveFragment("LOS");
						}
						
						// disha FSD
						if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Save")){
							formObject.saveFragment("DecisionHistory");
						}
						
						
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
				if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_Decision_Decision")){
							 if(formObject.getNGValue("cmplx_Decision_Decision").equalsIgnoreCase("Approve"))
							 {
								 formObject.setLocked("cmplx_Decision_CADDecisiontray",true);
							 }
							 else
							 {
								 formObject.setLocked("cmplx_Decision_CADDecisiontray",false);
							 }
						}	 
					default: break;
					
				}
	}


	public void setDeviationGridData() {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String wi_name = formObject.getWFWorkitemName();
		//query changed by saurabh on 9th Oct. Added cses.
		String query = "select case when Decision='Declined' then DeclinedReasonCode when Decision='Refer' then ReferReasoncode when Decision='Approve' then ReferReasoncode end  from ng_rlos_IGR_Eligibility_PersonalLoan where Child_Wi = '"+wi_name+"'";
		List<List<String>> record = formObject.getDataFromDataSource(query);
		if(record !=null && record.get(0)!=null && record.size()>0){
			for(List<String> row:record){
				List<String> temp = new ArrayList<String>();
				int separatorIndex = row.get(0).lastIndexOf("-");
				temp.add(row.get(0).substring(0, separatorIndex));
				temp.add(row.get(0).substring(separatorIndex+1, row.get(0).length()));
				formObject.addItemFromList("cmplx_Decision_DeviationDetails_grid", temp);
			}
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


	public void submitFormCompleted(FormEvent pEvent) throws ValidatorException {
        // TODO Auto-generated method stub
        PL_SKLogger.writeLog("PersonnalLoanS>  PL_Iniation", "Inside PL PROCESS submitFormCompleted()" + pEvent.getSource()); 
        FormReference formObject = FormContext.getCurrentInstance().getFormReference();
            List objInput=new ArrayList();
            objInput.add("Text:"+formObject.getWFWorkitemName());
            objInput.add("Text:CAD_Analyst2");
            PL_SKLogger.writeLog("PL","objInput args are: "+objInput.get(0)+objInput.get(1));
            formObject.getDataFromStoredProcedure("ng_rlos_CADLevels", objInput);
        
    }
	public void setExposureGridData() {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String wi_name = formObject.getWFWorkitemName();
		//query changed by saurabh on 9th Oct.
		String query = "select LoanType as Scheme,Liability_type,AgreementId as Agreement,OutstandingAmt as OutBal,PaymentsAmt as EMI,Consider_For_Obligations  from ng_RLOS_CUSTEXPOSE_LoanDetails where Child_Wi like '"+wi_name+"' union select CardType as Scheme,Liability_type,CardEmbossNum as Agreement,OutstandingAmt as OutBal,PaymentsAmount as EMI,Consider_For_Obligations  from ng_RLOS_CUSTEXPOSE_CardDetails where Child_Wi like '"+wi_name+"'  union select LoanType as Scheme,Liability_type,AgreementId as Agreement,OutstandingAmt as OutBal,PaymentsAmt as EMI,Consider_For_Obligations from ng_rlos_cust_extexpo_LoanDetails where Child_Wi like '"+wi_name+"' union select CardType as Scheme,Liability_type,CardEmbossNum as Agreement,CurrentBalance as OutBal,PaymentsAmount as EMI,Consider_For_Obligations from ng_rlos_cust_extexpo_CardDetails where Child_Wi like '"+wi_name+"'";
		List<List<String>> record = formObject.getDataFromDataSource(query);
		if(record !=null && record.get(0)!=null && record.size()>0){
			for(List<String> row:record){
				formObject.addItemFromList("cmplx_Decision_ExposureDetails_grid", row);
			}
		}
	}


	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		 FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		 formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
		 
		saveIndecisionGrid();
	}

}

