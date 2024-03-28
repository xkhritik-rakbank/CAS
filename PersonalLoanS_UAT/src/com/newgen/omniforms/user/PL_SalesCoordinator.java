/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PL_SalesCoordinator.java
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

public class PL_SalesCoordinator extends PLCommon implements FormListener
{
	boolean IsFragLoaded=false;
	String queryData_load="";
	  FormReference formObject = null;
	public void formLoaded(FormEvent pEvent)
	{
		System.out.println("Inside initiation PL");
		PL_SKLogger.writeLog("PL Initiation", "Inside formLoaded()" + pEvent.getSource().getName());
		
	}
	

	public void formPopulated(FormEvent pEvent) 
	{
        
        try{
            //loadPicklistCustomer();
            //formObject.fetchFragment("Part_Match", "PartMatch", "q_PartMatch");
            //formObject.setNGFrameState("Part_Match",0);
            //loadPicklist_PartMatch();
			new PersonalLoanSCommonCode().setFormHeader(pEvent);
            PL_SKLogger.writeLog("PL Part_Match","Inside Part_Match");            
            //LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
			//LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where Code='PL' and ActivityName='Branch_Init'");
			
        }catch(Exception e)
        {
            PL_SKLogger.writeLog("PL Initiation", "Exception:"+e.getMessage());
        }
    }
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException {
		// TODO Auto-generated method stub
		String popupFlag="N";
		String popUpMsg="";
		String popUpControl="";
		String alert_msg="";
		
		PL_SKLogger.writeLog("Inside PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
	  formObject =FormContext.getCurrentInstance().getFormReference();

      switch(pEvent.getType()) {

          case FRAME_EXPANDED:
        	  PL_SKLogger.writeLog(" In PL_Iniation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
				new PersonalLoanSCommonCode().FrameExpandEvent(pEvent);
					
					break;
                
          case FRAGMENT_LOADED:
        	  PL_SKLogger.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			 	
				
					if (pEvent.getSource().getName().equalsIgnoreCase("Customer")) {
						loadPicklistCustomer();
						//formObject.setLocked("Customer_Frame1",true);
						formObject.setEnabled("Customer_save",true);
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
						formObject.setEnabled("Customer_Reference_delete",true);
						
						formObject.setLocked("FetchDetails",true);
						formObject.setLocked("Customer_Button1",true);
						
					}	
					
					if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
						LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
						LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
						LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) order by SCHEMEID");
						//formObject.setLocked("Product_Frame1",true);
						formObject.setEnabled("Product_Save",true);
						formObject.setEnabled("Product_Add",true);
						formObject.setEnabled("Product_Modify",true);
						formObject.setEnabled("Product_Delete",true);
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails")) {
						//formObject.setLocked("IncomeDetails_Frame2",true);
						formObject.setEnabled("IncomeDetails_Salaried_Save",true);	
						formObject.setVisible("IncomeDetails_Frame3", false);
						formObject.setLocked("IncomeDetails_FinacialSummarySelf",true);		
						
						formObject.setLocked("cmplx_IncomeDetails_grossSal",true);
						formObject.setLocked("cmplx_IncomeDetails_totSal",true);
						formObject.setLocked("cmplx_IncomeDetails_AvgNetSal",true);
						
						formObject.setLocked("cmplx_IncomeDetails_Overtime_Avg",true);
						formObject.setLocked("cmplx_IncomeDetails_Commission_Avg",true);
						formObject.setLocked("cmplx_IncomeDetails_FoodAllow_Avg",true);
						formObject.setLocked("cmplx_IncomeDetails_PhoneAllow_Avg",true);
						formObject.setLocked("cmplx_IncomeDetails_serviceAllow_Avg",true);
						formObject.setLocked("cmplx_IncomeDetails_Bonus_Avg",true);
						formObject.setLocked("cmplx_IncomeDetails_Other_Avg",true);
                        formObject.setHeight("Incomedetails", 630);
                        formObject.setHeight("IncomeDetails_Frame1", 605);                      
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")) {
						//formObject.setLocked("Liability_New_Frame1",true);
						formObject.setEnabled("Liability_New_AECBReport",true);
						formObject.setEnabled("Liability_New_Save",true);
						formObject.setLocked("ExtLiability_Button1",true);	
						formObject.setLocked("Liability_New_Button1",true);
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) {
						//formObject.setLocked("EMploymentDetails_Frame1",true);
						//disha FSD
						String empid="AVI,MED,EDU,HOT,PROM";	
						formObject.setVisible("EMploymentDetails_Label25",false);
						formObject.setVisible("cmplx_EmploymentDetails_NepType",false);
						//formObject.setVisible("cmplx_EmploymentDetails_FreezoneName",false);
						formObject.setVisible("EMploymentDetails_Label10",false);
						formObject.setVisible("cmplx_EmploymentDetails_marketcode",false);
						//formObject.setVisible("cmplx_EmploymentDetails_FreezoneName",false);
						formObject.setVisible("cmplx_EmploymentDetails_tenancycntrctemirate",false);
						formObject.setVisible("EMploymentDetails_Label7",false);
						formObject.setVisible("cmplx_EmploymentDetails_IndusSeg",false);
						formObject.setVisible("EMploymentDetails_Label59",false);
						formObject.setVisible("EMploymentDetails_Label27",false);
						formObject.setVisible("cmplx_EmploymentDetails_MIS",false);
						formObject.setVisible("EMploymentDetails_Label28",false);
						formObject.setVisible("cmplx_EmploymentDetails_collectioncode",false);
						formObject.setVisible("EMploymentDetails_Label22",false);
						formObject.setVisible("cmplx_EmploymentDetails_PromotionCode",false);
						
						formObject.setVisible("EMploymentDetails_Label4",false);
						formObject.setVisible("cmplx_EmploymentDetails_StaffID",false);
						formObject.setVisible("EMploymentDetails_Label5",false);
						formObject.setVisible("cmplx_EmploymentDetails_Dept",false);
						formObject.setVisible("EMploymentDetails_Label6",false);
						formObject.setVisible("cmplx_EmploymentDetails_CntrctExpDate",false);
						formObject.setVisible("EMploymentDetails_Label15",false);
						formObject.setVisible("cmplx_EmploymentDetails_empmovemnt",false);
						formObject.setVisible("EMploymentDetails_Label12",false);
						formObject.setVisible("cmplx_EmploymentDetails_categexpat",false);
						formObject.setVisible("EMploymentDetails_Label13",false);
						formObject.setVisible("cmplx_EmploymentDetails_categnational",false);
						formObject.setVisible("EMploymentDetails_Label14",false);
						formObject.setVisible("cmplx_EmploymentDetails_categcards",false);
						formObject.setVisible("EMploymentDetails_Label18",false);
						formObject.setVisible("cmplx_EmploymentDetails_ownername",false);
						formObject.setVisible("EMploymentDetails_Label9",false);
						formObject.setVisible("cmplx_EmploymentDetails_NOB",false);
						formObject.setVisible("cmplx_EmploymentDetails_accpvded",false);
						formObject.setVisible("EMploymentDetails_Label17",false);
						formObject.setVisible("cmplx_EmploymentDetails_authsigname",false);
						formObject.setVisible("cmplx_EmploymentDetails_highdelinq",false);
						formObject.setVisible("EMploymentDetails_Label20",false);
						formObject.setVisible("cmplx_EmploymentDetails_dateinPL",false);
						formObject.setVisible("EMploymentDetails_Label21",false);
						formObject.setVisible("cmplx_EmploymentDetails_dateinCC",false);
						formObject.setVisible("EMploymentDetails_Label26",false);
						formObject.setVisible("cmplx_EmploymentDetails_remarks",false);
						formObject.setVisible("EMploymentDetails_Label16",false);
						formObject.setVisible("cmplx_EmploymentDetails_Remarks_PL",false);
						formObject.setVisible("EMploymentDetails_Label11",false);												
						
						formObject.setVisible("cmplx_EmploymentDetails_channelcode",false);
						formObject.setVisible("EMploymentDetails_Label36",false);
						formObject.setLocked("cmplx_EmploymentDetails_EmpName",true);
						formObject.setLocked("cmplx_EmploymentDetails_EMpCode",true);
						formObject.setLocked("cmplx_EmploymentDetails_LOS",true);
						formObject.setLocked("EMploymentDetails_Button1",true);
						
						formObject.setTop("EMploymentDetails_Frame2",54);
						formObject.setTop("EMploymentDetails_Label71",240);
						formObject.setTop("cmplx_EmploymentDetails_EmpContractType",256);
						formObject.setTop("cmplx_EmploymentDetails_Kompass",256);
						formObject.setLeft("cmplx_EmploymentDetails_Kompass",1066);								
						
						formObject.setHeight("EMploymentDetails_Frame2",382);
						formObject.setTop("EMploymentDetails_Save",400);
						formObject.setTop("EMploymentDetails_Button1",440);
						
						loadPicklist4();
						Fields_ApplicationType_Employment();

						if(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode").equalsIgnoreCase("NEP")){
							formObject.setVisible("EMploymentDetails_Label25",true);
							formObject.setVisible("cmplx_EmploymentDetails_NepType",true);
						}

						/*else if(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode").equalsIgnoreCase("FZD") || formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode").equalsIgnoreCase("FZE")){
							formObject.setVisible("cmplx_EmploymentDetails_Freezone",true);
							formObject.setVisible("EMploymentDetails_Label62",true);
							formObject.setVisible("cmplx_EmploymentDetails_FreezoneName",true);
						}*/

						else if(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode").equalsIgnoreCase("TEN"))
						{
							formObject.setVisible("cmplx_EmploymentDetails_tenancycntrctemirate",true);
							formObject.setVisible("EMploymentDetails_Label7",true);
						}

						else if(formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg").equals("Surrogate") && empid.contains(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")))
						{
							formObject.setVisible("cmplx_EmploymentDetails_IndusSeg",true);
							formObject.setVisible("EMploymentDetails_Label59",true);
							
						}

						
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo")) {
						//formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
						formObject.setLocked("ELigibiltyAndProductInfo_Frame7",true);
						formObject.setEnabled("ELigibiltyAndProductInfo_Save",true);
						
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails")) {
						loadPicklist_Address();
						//formObject.setLocked("AddressDetails_Frame1",true);
						formObject.setEnabled("AddressDetails_Save",true);
						formObject.setEnabled("AddressDetails_addr_Add",true);
						formObject.setEnabled("AddressDetails_addr_Modify",true);
						formObject.setEnabled("AddressDetails_addr_Delete",true);
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails")){
						
						//formObject.setLocked("AltContactDetails_Frame1",true);
						formObject.setEnabled("AltContactDetails_ContactDetails_Save",true);
					} 
					
					if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")){
						
						//formObject.setLocked("FATCA_Frame1",true);
						formObject.setEnabled("FATCA_Save",true);
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("KYC")){
						
						//formObject.setLocked("KYC_Frame1",true);
						formObject.setEnabled("KYC_Save",true);
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("OECD")){
						
						//formObject.setLocked("OECD_Frame1",true);
						formObject.setEnabled("OECD_Save",true);
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("IncomingDoc")){
						fetchIncomingDocRepeater();
						formObject.setVisible("cmplx_DocName_OVRemarks", false);
				        formObject.setVisible("cmplx_DocName_OVDec",false);
				        formObject.setVisible("cmplx_DocName_Approvedby",false);
					}
					//disha FSD
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
						formObject.setVisible("NotepadDetails_save",true);						
						formObject.setLocked("NotepadDetails_notecode",true);
						
						formObject.setHeight("NotepadDetails_Frame1",450);
						formObject.setTop("NotepadDetails_save",400);
						LoadPickList("NotepadDetails_notedesc", "select '--Select--' union select  description from ng_master_notedescription");
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
						
					    formObject.setVisible("DecisionHistory_chqbook",false);
	                	loadPicklist1();
	                	//disha FSD
	                	formObject.setVisible("DecisionHistory_Label8", false);
	        			formObject.setVisible("cmplx_Decision_ChequeBookNumber", false);
	        			formObject.setVisible("DecisionHistory_Label9", false);
	        			formObject.setVisible("cmplx_Decision_DebitcardNumber", false);
	        			formObject.setVisible("DecisionHistory_Label5", false);
	        			formObject.setVisible("cmplx_Decision_desc", false);
	        			formObject.setVisible("DecisionHistory_Label3", false);
	        			formObject.setVisible("cmplx_Decision_strength", false);
	        			formObject.setVisible("DecisionHistory_Label4", false);
	        			formObject.setVisible("cmplx_Decision_weakness", false);
	        			
	                	formObject.setVisible("DecisionHistory_Button4",false);
						formObject.setVisible("cmplx_Decision_Deviationcode",false);
						formObject.setVisible("DecisionHistory_Label14",false);
						formObject.setVisible("cmplx_Decision_Dectech_decsion",false);
						formObject.setVisible("DecisionHistory_Label15",false);
						formObject.setVisible("cmplx_Decision_score_grade",false);
						formObject.setVisible("DecisionHistory_Label16",false);
						formObject.setVisible("cmplx_Decision_Highest_delegauth",false);
						formObject.setVisible("cmplx_Decision_Manual_Deviation",false);
						formObject.setVisible("DecisionHistory_Button6",false);
						formObject.setVisible("cmplx_Decision_Manual_deviation_reason",false);
						formObject.setVisible("cmplx_Decision_waiveoffver",false);
						formObject.setVisible("cmplx_Decision_refereason",false);
						formObject.setVisible("DecisionHistory_Label1",false);
						formObject.setVisible("DecisionHistory_Label26",true);
						formObject.setVisible("cmplx_Decision_AppID",true);
						
						formObject.setLocked("cmplx_Decision_IBAN",true);
						formObject.setLocked("cmplx_Decision_AppID",true);
						formObject.setLocked("cmplx_Decision_AccountNo",true);
						formObject.setTop("DecisionHistory_Label7", 8);
						formObject.setTop("cmplx_Decision_AccountNo", 23);
						formObject.setTop("Decision_Label3", 56);
						formObject.setTop("cmplx_Decision_Decision", 72);
						formObject.setTop("Decision_Label4", 56);
						formObject.setTop("cmplx_Decision_REMARKS", 72);
						
						formObject.setTop("Decision_Label4", 104);
						formObject.setTop("cmplx_Decision_REMARKS", 120);
						
						formObject.setTop("DecisionHistory_Label6", 8);
						formObject.setTop("cmplx_Decision_IBAN", 23);						
						formObject.setTop("DecisionHistory_Label26", 56);
						formObject.setTop("cmplx_Decision_AppID", 92);				
						formObject.setTop("cmplx_Decision_cmplx_gr_decision", 226);		
						formObject.setTop("DecisionHistory_save", 400);	
						
						formObject.setTop("Decision_Label1", 8);
						formObject.setTop("cmplx_Decision_VERIFICATIONREQUIRED", 23);
						
						formObject.setLeft("DecisionHistory_Label26", 1000);
						formObject.setLeft("cmplx_Decision_AppID", 1000);
						formObject.setLeft("Decision_Label4", 352);
						formObject.setLeft("cmplx_Decision_REMARKS", 352);
						formObject.setLeft("Decision_Label4", 672);
						formObject.setLeft("cmplx_Decision_REMARKS", 672);					
						
						formObject.setLeft("Decision_Label1", 1000);
						formObject.setLeft("cmplx_Decision_VERIFICATIONREQUIRED", 1000);
						
						formObject.setLeft("Decision_Label4", 352);
						formObject.setLeft("cmplx_Decision_REMARKS", 352);
						
						formObject.setTop("Decision_Label4", 56);
						formObject.setTop("cmplx_Decision_REMARKS", 72);
	                	//Common function for decision fragment textboxes and combo visibility
	                	//decisionLabelsVisibility();
				 } 	
					//code by saurabh gupta on 27th June 17.
					/*String hiddenFieldString = formObject.getNGValue("fields_string");
					if(hiddenFieldString!=null && !hiddenFieldString.equalsIgnoreCase("") && !hiddenFieldString.equalsIgnoreCase(" ")){
						String[] fieldNames = hiddenFieldString.split(",");
						setChangedFieldsColor(fieldNames);
					}*/
			
			  break;
			  
			case MOUSE_CLICKED:
				PL_SKLogger.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
				if (pEvent.getSource().getName().equalsIgnoreCase("Customer_ReadFromCard")){
					//GenerateXML();
				}
				//disha FSD
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
				
				//Arun added (14-jun)
				if (pEvent.getSource().getName().equalsIgnoreCase("ExtLiability_Add")){
					formObject.setNGValue("Liability_Wi_Name",formObject.getWFWorkitemName());
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
				}
				
				if (pEvent.getSource().getName().equalsIgnoreCase("ExtLiability_Modify")){
					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
				}
				
				if (pEvent.getSource().getName().equalsIgnoreCase("ExtLiability_Delete")){
					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
				}
				
				if (pEvent.getSource().getName().equalsIgnoreCase("FATCA_Button1")){
					String text=formObject.getNGItemText("cmplx_FATCA_listedreason", formObject.getSelectedIndex("cmplx_FATCA_listedreason"));
					PL_SKLogger.writeLog("RLOS", "Inside FATCA_Button1 "+text);
					formObject.addItem("cmplx_FATCA_selectedreason", text);
					try {
						formObject.removeItem("cmplx_FATCA_listedreason", formObject.getSelectedIndex("cmplx_FATCA_listedreason"));
						formObject.setSelectedIndex("cmplx_FATCA_listedreason", -1);

					}catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				if (pEvent.getSource().getName().equalsIgnoreCase("FATCA_Button2")){
					PL_SKLogger.writeLog("RLOS", "Inside FATCA_Button2 ");
					formObject.addItem("cmplx_FATCA_listedreason", formObject.getNGItemText("cmplx_FATCA_selectedreason", formObject.getSelectedIndex("cmplx_FATCA_selectedreason")));
					try {
						formObject.removeItem("cmplx_FATCA_selectedreason", formObject.getSelectedIndex("cmplx_FATCA_selectedreason"));
						formObject.setSelectedIndex("cmplx_FATCA_selectedreason", -1);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails_Reference_Add"))
				{
					formObject.setNGValue("ReferenceDetails_reference_wi_name",formObject.getWFWorkitemName());
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_ReferenceDetails_cmplx_ReferenceGrid");			
				}
				
				if (pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails_Reference__modify")){
					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_ReferenceDetails_cmplx_ReferenceGrid");
				}
				
				if (pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails_Reference_delete")){
					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_ReferenceDetails_cmplx_ReferenceGrid");
				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("OECD_add"))
				{
					formObject.setNGValue("OECD_winame",formObject.getWFWorkitemName());
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_OECD_cmplx_GR_OecdDetails");			
				}
				
				if (pEvent.getSource().getName().equalsIgnoreCase("OECD_modify")){
					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_OECD_cmplx_GR_OecdDetails");
				}
				
				if (pEvent.getSource().getName().equalsIgnoreCase("OECD_delete")){
					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_OECD_cmplx_GR_OecdDetails");
				}
				
				if (pEvent.getSource().getName().equalsIgnoreCase("Product_Modify")){
					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Product_cmplx_ProductGrid");
				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails_add"))
				{
					formObject.setNGValue("GuarantorDetails_guarantor_WIName",formObject.getWFWorkitemName());
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Guarantror_GuarantorDet");			
				}
				
				if (pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails_modify")){
					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Guarantror_GuarantorDet");
				}
				
				if (pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails_delete")){
					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_Guarantror_GuarantorDet");
				}
				//disha FSD
				if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Add")){
					formObject.setNGValue("Notepad_wi_name",formObject.getWFWorkitemName());
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_notegrid");
					
					String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
					PL_SKLogger.writeLog("PL", "sActivityName after add row $ "+sActivityName+"$");
					int user_id = formObject.getUserId();
					String user_name = formObject.getUserName();
					user_name = user_name+"-"+user_id;		
					
					PL_SKLogger.writeLog("PL", "user_name after add row $ "+user_name+"$");
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
				
				// Save button code 
				if(pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails_save")){
					formObject.saveFragment("ReferenceDetails");
				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("OECD_Save")){
					formObject.saveFragment("OECD");
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
					formObject.saveFragment("IncomeDEtails");
				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_SelfEmployed_Save")){
					formObject.saveFragment("IncomeDEtails");
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
				
				if(pEvent.getSource().getName().equalsIgnoreCase("ExtLiability_Save")){
					formObject.saveFragment("InternalExternalLiability");
				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("Liability_New_Save")){
					formObject.saveFragment("InternalExternalContainer");
				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("EmpDetails_Save")){
					formObject.saveFragment("EmploymentDetails");
				}
				
				if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails_Button2")) {
                    String EmpName=formObject.getNGValue("EMploymentDetails_Text21");
                    String EmpCode=formObject.getNGValue("EMploymentDetails_Text22");
                    PL_SKLogger.writeLog("PL", "EMpName$"+EmpName+"$");
                    String query=null;
					//changes done to check duplicate selection compare emp code and main emp code main_employer_code column added - 06-09-2017 Disha
                    if(EmpName.trim().equalsIgnoreCase(""))
                                    query="select distinct(EMPR_NAME),EMPLOYER_CODE,NATURE_OF_BUSINESS,EMPLOYER_CATEGORY_PL_NATIONAL,EMPLOYER_CATEGORY_CARDS,EMPLOYER_CATEGORY_PL_EXPAT,INCLUDED_IN_PL_ALOC,DOI_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,DATE_OF_INCLUSION_IN_CC_ALOC,NAME_OF_AUTHORIZED_PERSON_FOR_ISSUING_SC_STL_PAYSLIP,ACCOMMODATION_PROVIDED,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,OWNER_PARTNER_SIGNATORY_NAMES_AS_PER_TL,ALOC_REMARKS,HIGH_DELINQUENCY_EMPLOYER,MAIN_EMPLOYER_CODE from NG_RLOS_ALOC_OFFLINE_DATA where EMPLOYER_CODE Like '%"+EmpCode+"%'";

                    else
                                    query="select distinct(EMPR_NAME),EMPLOYER_CODE,NATURE_OF_BUSINESS,EMPLOYER_CATEGORY_PL_NATIONAL,EMPLOYER_CATEGORY_CARDS,EMPLOYER_CATEGORY_PL_EXPAT,INCLUDED_IN_PL_ALOC,DOI_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,DATE_OF_INCLUSION_IN_CC_ALOC,NAME_OF_AUTHORIZED_PERSON_FOR_ISSUING_SC_STL_PAYSLIP,ACCOMMODATION_PROVIDED,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,OWNER_PARTNER_SIGNATORY_NAMES_AS_PER_TL,ALOC_REMARKS,HIGH_DELINQUENCY_EMPLOYER,MAIN_EMPLOYER_CODE from NG_RLOS_ALOC_OFFLINE_DATA where EMPR_NAME Like '%"+EmpName + "%' or EMPLOYER_CODE Like '%"+EmpCode+"'";

                    PL_SKLogger.writeLog("PL", "query is: "+query);
                    populatePickListWindow(query,"EMploymentDetails_Button2", "Employer Name,Employer Code,Nature Of Business,EMPLOYER CATEGORY PL NATIONAL,EMPLOYER CATEGORY CARDS,EMPLOYER CATEGORY PL EXPAT,INCLUDED IN PL ALOC,DOI IN PL ALOC,INCLUDED IN CC ALOC,DATE OF INCLUSION IN CC ALOC,NAME OF AUTHORIZED PERSON FOR ISSUING SC/STL/PAYSLIP,ACCOMMODATION PROVIDED,INDUSTRY SECTOR,INDUSTRY MACRO,INDUSTRY MICRO,CONSTITUTION,NAME OF FREEZONE AUTHORITY,OWNER/PARTNER/SIGNATORY NAMES AS PER TL,ALOC REMARKS,HIGH DELINQUENCY EMPLOYER,MAIN EMPLOYER CODE", true, 20);

				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo_Save")){
					formObject.saveFragment("EligibilityAndProductInformation");
				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("LoanDetails_Save")){
					formObject.saveFragment("LoanDetails");
					popupFlag = "Y";
					alert_msg="Loan Details Saved";

					throw new ValidatorException(new FacesMessage(alert_msg));
				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("LoanDetaisDisburs_Save")){
					formObject.saveFragment("LoanDetails");
				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("LoanDetailsRepay_Save")){
					formObject.saveFragment("LoanDetails");
				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("MiscellaneousFields_Save")){
					formObject.saveFragment("MiscFields");
				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_Save")){
					formObject.saveFragment("Address_Details_container");
				}
				//disha FSD
				if(pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails_ContactDetails_Save")){
					formObject.saveFragment("Alt_Contact_container");
				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails_save")){
					formObject.saveFragment("ReferenceDetails");
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
				//disha FSD
				if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Save")){
					formObject.saveFragment("DecisionHistory");
				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_save")){
					formObject.saveFragment("Notepad_Values");
				}
				
				if (pEvent.getSource().getName().equalsIgnoreCase("IncomingDoc_AddFromPCButton")){
					IRepeater repObj=null;
					repObj = formObject.getRepeaterControl("IncomingDoc_Frame2");
					repObj.getValue(repObj.getChangeIndex(),"IncomingDoc_Label1");
					PL_SKLogger.writeLog("","value of repeater:"+repObj.getValue(repObj.getChangeIndex(),"IncomingDoc_Label1"));
					}
				
				
				
				
				
		
			
			 case VALUE_CHANGED:
					PL_SKLogger.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
					 if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_Decision_Decision")) {
						 if(formObject.getWFActivityName().equalsIgnoreCase("CAD_Analyst1"))	
						 {
							 formObject.setNGValue("CAD_dec", formObject.getNGValue("cmplx_Decision_Decision"));
							PL_SKLogger.writeLog(" In PL_Initiation VALChanged---New Value of CAD_dec is: ", formObject.getNGValue("Decision_Combo2"));

						 }
						 
						 else{
							 
							formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
							PL_SKLogger.writeLog(" In PL_Initiation VALChanged---New Value of decision is: ", formObject.getNGValue("Decision_Combo2"));
						 	  }
					 	}
					 //disha FSD
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
	
	
	public void initialize() {
		PL_SKLogger.writeLog("PersonnalLoanS>  PL_Iniation", "Inside PL PROCESS initialize()" );
		  formObject =FormContext.getCurrentInstance().getFormReference();

	}

	
	public void saveFormCompleted(FormEvent pEvent) throws ValidatorException {
		PL_SKLogger.writeLog("PersonnalLoanS>  PL_Iniation", "Inside PL PROCESS saveFormCompleted()" + pEvent.getSource());
		  formObject =FormContext.getCurrentInstance().getFormReference();

	}

	
	public void saveFormStarted(FormEvent pEvent) throws ValidatorException {
		  formObject =FormContext.getCurrentInstance().getFormReference();
		PL_SKLogger.writeLog("PersonnalLoanS>  PL_Iniation", "Inside PL PROCESS saveFormStarted()" + pEvent.getSource());
		formObject.setNGValue("get_parent_data",queryData_load);
		PL_SKLogger.writeLog("PersonnalLoanS","Final val of queryData_load:"+ formObject.getNGValue("get_parent_data"));
	}

	
	public void submitFormCompleted(FormEvent pEvent) throws ValidatorException {
		PL_SKLogger.writeLog("PersonnalLoanS>  PL_Iniation", "Inside PL PROCESS submitFormCompleted()" + pEvent.getSource());
		
	}

	
	public void submitFormStarted(FormEvent pEvent) throws ValidatorException {
		PL_SKLogger.writeLog("PersonnalLoanS>  PL_Iniation", "Inside PL PROCESS submitFormStarted()" + pEvent.getSource());
		
		 FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		 formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
		 
		 if(formObject.getNGValue("cmplx_Decision_Decision").equalsIgnoreCase("Parallel"))
		 {
			 formObject.setNGValue("parallel_sequential","P");
		 }
		 else
		 {
			 formObject.setNGValue("parallel_sequential","S");
		 }
		saveIndecisionGrid();
	}


	public void continueExecution(String arg0, HashMap<String, String> arg1) {
		// TODO Auto-generated method stub
		
	}

}

