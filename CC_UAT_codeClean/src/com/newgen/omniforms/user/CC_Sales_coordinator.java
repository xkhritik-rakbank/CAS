
/*------------------------------------------------------------------------------------------------------

                     NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                             : Application -Projects

Project/Product                                                   : Rakbank  

Application                                                       : Credit Card

Module                                                            : CSM

File Name                                                         : CC_CSM

Author                                                            : Disha 

Date (DD/MM/YYYY)                                                 : 

Description                                                       : 

-------------------------------------------------------------------------------------------------------

CHANGE HISTORY

-------------------------------------------------------------------------------------------------------

Reference No/CR No   Change Date   Changed By    Change Description
1. 					12-6-2016	 Disha		   Code commented saveIndecisionGrid() for continueExecution
1003				17-9-2017	Saurabh			Making CSM employment Details same as initiation.
1000				17-9-2017	Saurabh			Making decision history fragment same as initiation.
1002				17-9-2017	Saurabh			Hiding DDVT related fields in CSM. 
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
import com.newgen.omniforms.listener.FormListener;
import com.newgen.omniforms.util.SKLogger_CC;

//Changes - 1. On Sales coordinator Incoming Document frame is editable 

public class CC_Sales_coordinator extends CC_Common implements FormListener
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
		
		 
		makeSheetsInvisible(tabName, "7,8,9,11,12,13,14,15,16,17");
		
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
		String alert_msg="";
		SKLogger_CC.writeLog("Inside PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		SKLogger_CC.writeLog("", "eventdispatched inside");
	  formObject =FormContext.getCurrentInstance().getFormReference();

      switch(pEvent.getType()) {

          case FRAME_EXPANDED:
				SKLogger_CC.writeLog(" In PL_Iniation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
				new CC_CommonCode().FrameExpandEvent(pEvent);
        break;
                
          case FRAGMENT_LOADED:
        	  SKLogger_CC.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			 	
				/*if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
      			
				}*/
					if (pEvent.getSource().getName().equalsIgnoreCase("Customer")) {
						SKLogger_CC.writeLog("In cust frag load", "start of customer");
						
						try{
							
							if(formObject.getNGValue("cmplx_Customer_NEP").equals("true")){
								formObject.setVisible("cmplx_Customer_EIDARegNo",true);
							}
							else {
								formObject.setVisible("cmplx_Customer_EIDARegNo",false);
							}
							if(formObject.isEnabled("cmplx_Customer_CIFNo"))
							formObject.setEnabled("cmplx_Customer_CIFNo",false);
						disableButtonsCC("Customer");
						
						loadPicklistCustomer();
						
						}
						// added for point 22
						catch(Exception ex){
							SKLogger_CC.writeLog(" In cust frag load", "Exception is: "+printException(ex));
						}
						SKLogger_CC.writeLog(" In cust frag load", "end of customer");
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
					
					if (pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails")) {
		            	 
						 
						 disableButtonsCC("AuthorisedSignDetails");
						 
		            	LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
		                LoadPickList("AuthorisedSignDetails_SignStatus", "select '--Select--' union select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");
		                LoadPickList("Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
						LoadPickList("DesignationAsPerVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
						}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails")) {
					    disableButtonsCC("PartnerDetails");
						
		                LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
		                
		                
		            }
					
					if (pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails")) {
							disableButtonsCC("CompanyDetails");
							loadPicklist_CompanyDet();
							hideAtCSMCompany();
						}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")) {
					
						disableButtonsCC("Liability_New");
					// ++ below code not commented at offshore - 06-10-2017
						formObject.setVisible("Liability_New_Label6",false);
						formObject.setVisible("cmplx_Liability_New_NoofotherbankAuth",false);
						formObject.setVisible("Liability_New_Label8",false);
						formObject.setVisible("cmplx_Liability_New_NoofotherbankCompany",false);
						//++ Below Code already exists for : "18-CSM-Liability addition-" Incorrect masters in type of contract field" : Reported By Shashank on Oct 05, 2017++
						SKLogger_CC.writeLog("CC_Common","before loadng picklist");
						  LoadPickList("ExtLiability_contractType", "select '--Select--' union select convert(varchar, description) from ng_master_contract_type with (nolock)");
						  SKLogger_CC.writeLog("CC_Common","after loadng picklist");
						//++ Above Code already exists for : "18-CSM-Liability addition-" Incorrect masters in type of contract field" : Reported By Shashank on Oct 05, 2017++
						  SKLogger_CC.writeLog("CC_Common","after liability Details of CSM456");
						formObject.setVisible("Liability_New_Delinkinlast3months",true);
						formObject.setVisible("Liability_New_DPDinlast6",true);
						formObject.setVisible("Liability_New_DPDinlast18months",true);
						formObject.setVisible("Liability_New_Label4",true);
						formObject.setVisible("Liability_New_writeOfAmount",true);
						formObject.setVisible("Liability_New_Label5",true);
						formObject.setVisible("Liability_New_worstStatusInLast24",true);
						formObject.setVisible("Liability_New_Label10",true);
						formObject.setVisible("Liability_New_Text2",true);
						 SKLogger_CC.writeLog("CC_Common","after liability Details of CSM123");						
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) {
						
						
						disableButtonsCC("EMploymentDetails");
						
						loadPicklistEmployment();
						loadPicklist4();
						Fields_ApplicationType_Employment();
						// Ref. 1003
						formObject.setVisible("EMploymentDetails_Label7", false);
						formObject.setVisible("cmplx_EmploymentDetails_tenancycntrctemirate", false);
						formObject.setVisible("EMploymentDetails_Label28", false);
						formObject.setVisible("cmplx_EmploymentDetails_StaffID", false);
						formObject.setVisible("EMploymentDetails_Label5", false);
						formObject.setVisible("cmplx_EmploymentDetails_Dept", false);
						formObject.setVisible("EMploymentDetails_Label6", false);
						formObject.setVisible("cmplx_EmploymentDetails_CntrctExpDate", false);
						formObject.setVisible("EMploymentDetails_Label29", false);
						formObject.setVisible("cmplx_EmploymentDetails_Status_PLExpact", false);
						formObject.setVisible("EMploymentDetails_Label30", false);
						formObject.setVisible("cmplx_EmploymentDetails_Status_PLNational", false);
						formObject.setVisible("EMploymentDetails_Label71", false);
						formObject.setVisible("cmplx_EmploymentDetails_EmpContractType", false);
						formObject.setVisible("EMploymentDetails_Label18", false);
						formObject.setVisible("cmplx_EmploymentDetails_ownername", false);
						formObject.setVisible("EMploymentDetails_Label9", false);
						formObject.setVisible("cmplx_EmploymentDetails_NOB", false);
						formObject.setVisible("EMploymentDetails_Label31", false);
						formObject.setVisible("EMploymentDetails_Combo34", false);
						formObject.setVisible("EMploymentDetails_Label17", false);
						formObject.setVisible("cmplx_EmploymentDetails_authsigname", false);
						formObject.setVisible("cmplx_EmploymentDetails_highdelinq", false);
						formObject.setVisible("EMploymentDetails_Label20", false);
						formObject.setVisible("cmplx_EmploymentDetails_dateinPL", false);
						formObject.setVisible("EMploymentDetails_Label21", false);
						formObject.setVisible("cmplx_EmploymentDetails_dateinCC", false);
						formObject.setVisible("EMploymentDetails_Label15", false);
						formObject.setVisible("cmplx_EmploymentDetails_empmovemnt", false);
						formObject.setVisible("EMploymentDetails_Label32", false);
						formObject.setVisible("EMploymentDetails_Combo35", false);
						formObject.setVisible("EMploymentDetails_Label26", false);
						formObject.setVisible("cmplx_EmploymentDetails_remarks", false);
						formObject.setVisible("EMploymentDetails_Label27", false);
						formObject.setVisible("cmplx_EmploymentDetails_Aloc_RemarksPL", false);
						formObject.setVisible("EMploymentDetails_Label10", false);
						formObject.setVisible("cmplx_EmploymentDetails_marketcode", false);
						formObject.setVisible("EMploymentDetails_Label4", false);
						formObject.setVisible("cmplx_EmploymentDetails_PromotionCode", false);
						formObject.setVisible("EMploymentDetails_Label10", false);
						formObject.setVisible("cmplx_EmploymentDetails_marketcode", false);
						formObject.setTop("EMploymentDetails_Save", 260);
							//p1-7,ALOC fields should be disabled for CSM user
						    formObject.setLocked("cmplx_EmploymentDetails_Kompass",true);
	                        formObject.setLocked("cmplx_EmploymentDetails_IncInPL",true);
	                        formObject.setLocked("cmplx_EmploymentDetails_IncInCC",true);
	                        formObject.setLocked("cmplx_EmploymentDetails_EmpIndusSector",true);
	                        formObject.setLocked("cmplx_EmploymentDetails_Indus_Macro",true);
	                        formObject.setLocked("cmplx_EmploymentDetails_Indus_Micro",true);
	                        formObject.setLocked("cmplx_EmploymentDetails_FreezoneName",true);
	                        formObject.setLocked("cmplx_EmploymentDetails_Freezone",true); 
	                        formObject.setLocked("cmplx_EmploymentDetails_Kompass",true); 
							//p1-7,ALOC fields should be disabled for CSM user
						// Ref. 1003 end.
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo")) {
					
						disableButtonsCC("ELigibiltyAndProductInfo");
						
						
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails")) {
					
						disableButtonsCC("AddressDetails");
						loadPicklist_Address();
						
						
						
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails")){
						
						disableButtonsCC("AltContactDetails");
						
						/*formObject.setEnabled("AltContactDetails_ContactDetails_Save",true);*/
					} 
					
					if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")){
						
						disableButtonsCC("FATCA");
						
						/*formObject.setEnabled("FATCA_Save",true);*/
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("KYC")){
						
						
					}
					
					if (pEvent.getSource().getName().equalsIgnoreCase("OECD")){
						
						disableButtonsCC("OECD");
						
						/*formObject.setEnabled("OECD_Save",true);*/
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("IncomingDocument")){
						
						disableButtonsCC("IncomingDocument");
						//++ Below Code already exists for : "31-CSM-Documents-" No documents in document list" : Reported By Shashank on Oct 05, 2017++
						SKLogger_CC.writeLog("CC_Common","before incomingdoc");

						fetchIncomingDocRepeater();
						SKLogger_CC.writeLog("CC_Common","after incomingdoc");
						//++ Above Code already exists for : "31-CSM-Documents-" No documents in document list" : Reported By Shashank on Oct 05, 2017++
						
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("Reference_Details")) {
					
						disableButtonsCC("Reference_Details");
						LoadPickList("Reference_Details_ReferenceRelatiomship", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_Relationship with (nolock) order by code");
						
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails")) {
					
						disableButtonsCC("SupplementCardDetails");
						
						
					}
					if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails")) {
					
						disableButtonsCC("CardDetails");
						//Ref. 1002
						LoadPickList("CardDetails_Combo2","select '--Select--'  as description union select convert(varchar,description) from NG_MASTER_MarketingCode with (nolock) ");
						formObject.setVisible("cmplx_CardDetails_Security_Check_Held", false);
						LoadPickList("CardDetails_FeeProfile","select '--Select--'  as description union select convert(varchar,description) from ng_master_feeProfile with (nolock) ");
						formObject.setVisible("CardDetails_Label12", false);
						LoadPickList("CardDetails_InterestFP","select '--Select--'  as description union select convert(varchar,description) from ng_master_InterestProfile with (nolock) ");
						formObject.setVisible("cmplx_CardDetails_MarketingCode", false);
						LoadPickList("CardDetails_TransactionFP","select '--Select--'  as description union select convert(varchar,description) from ng_master_TransactionFeeProfile with (nolock) ");
						formObject.setVisible("CardDetails_Label8", false);

						formObject.setVisible("cmplx_CardDetails_Bank_Name", false);
						formObject.setVisible("CardDetails_Label9", false);
						formObject.setVisible("cmplx_CardDetails_Cheque_Number", false);
						formObject.setVisible("CardDetails_Label10", false);
						formObject.setVisible("cmplx_CardDetails_Amount", false);
						formObject.setVisible("CardDetails_Label11", false);
						formObject.setVisible("cmplx_CardDetails_Date", false);
						formObject.setVisible("CardDetails_Label13", false);
						formObject.setVisible("cmplx_CardDetails_CustClassification", false);
						//Ref. 1002 end.
					}
					//added by yash on 22/8/2017
					if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails")) {
						//12th sept
						 notepad_load();
						 notepad_withoutTelLog();
						 //12th sept
						 String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
						 SKLogger_CC.writeLog("CCyash ", "Activity name is:" + sActivityName);
				        formObject.setNGValue("NotepadDetails_Actusername",formObject.getNGValue("cmplx_Customer_FirstNAme") +" "+ formObject.getNGValue("cmplx_Customer_MiddleNAme") +" "+ formObject.getNGValue("cmplx_Customer_LastNAme"));
				        formObject.setNGValue("NotepadDetails_user",formObject.getNGValue("cmplx_Customer_FirstNAme") +" "+ formObject.getNGValue("cmplx_Customer_MiddleNAme") +" "+ formObject.getNGValue("cmplx_Customer_LastNAme"));
				    	//Ref. 1001.
						formObject.setNGValue("NotepadDetails_insqueue",sActivityName);
						formObject.setLocked("NotepadDetails_noteDate",true);
						formObject.setLocked("NotepadDetails_Actusername",true);
						formObject.setLocked("NotepadDetails_user",true);
						formObject.setLocked("NotepadDetails_insqueue",true);
						formObject.setLocked("NotepadDetails_Actdate",true);
						formObject.setVisible("NotepadDetails_SaveButton",true);
						formObject.setVisible("NotepadDetails_Frame3", false);
						formObject.setTop("NotepadDetails_SaveButton",400);
						//Ref. 1001.
					}
					//ended by yash
					
					
					if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
						//Ref 1000. changes done by saurabh on 17th Sept for keeping sync in decision history of CSM and initiation.
						formObject.setVisible("DecisionHistory_CheckBox1",false);
                        formObject.setVisible("DecisionHistory_Label1",true);
                        formObject.setVisible("cmplx_DEC_VerificationRequired",true);
                        formObject.setEnabled("cmplx_DEC_VerificationRequired",false);
                        formObject.setVisible("cmplx_DEC_ContactPointVeri",false);
                        formObject.setVisible("DecisionHistory_Label3",false);
                        formObject.setVisible("DecisionHistory_Combo3",true);
                        formObject.setVisible("DecisionHistory_Label6",true);
                        formObject.setVisible("DecisionHistory_Combo6",true);
                        formObject.setVisible("DecisionHistory_Decision_Label4",true);
                        formObject.setVisible("cmplx_DEC_Remarks",true);
                        formObject.setVisible("DecisionHistory_Label8",false);
                        formObject.setVisible("DecisionHistory_Text4",true);
                        //formObject.setVisible("DecisionHistory_Label7",false);
                        formObject.setVisible("cmplx_DEC_ChequebookRef",false);
                        //12th sept
                        formObject.setVisible("DecisionHistory_Label6",false);
                        formObject.setVisible("cmplx_DEC_IBAN_No",false);
                		formObject.setVisible("DecisionHistory_Label5",false);
        				formObject.setVisible("DecisionHistory_Decision_Label1",false);
						formObject.setVisible("cmplx_DEC_VerificationRequired",false);
						formObject.setVisible("DecisionHistory_Label10",false);
						formObject.setVisible("cmplx_DEC_New_CIFID",false);
						formObject.setVisible("cmplx_DEC_NewAccNo",false);
						formObject.setVisible("DecisionHistory_chqbook",false);
						formObject.setVisible("cmplx_DEC_ChequebookRef",false);
						formObject.setVisible("DecisionHistory_Label4",false);
						formObject.setVisible("DecisionHistory_Label27",false);
						formObject.setVisible("cmplx_DEC_Cust_Contacted",false);
						formObject.setVisible("DecisionHistory_Label9",false);
						formObject.setVisible("cmplx_DEC_DCR_Refno",false);
						formObject.setVisible("DecisionHistory_Decision_Label4",false);
						formObject.setVisible("cmplx_DEC_Remarks",false);
						
						 formObject.setVisible("cmplx_DEC_Description",false);
                         formObject.setVisible("cmplx_DEC_Strength",false);
                         formObject.setVisible("cmplx_DEC_Weakness",false);
                         formObject.setVisible("DecisionHistory_Label2",false);
                         formObject.setVisible("DecisionHistory_Label7",false);
                         formObject.setVisible("DecisionHistory_Label8",false);
                         formObject.setEnabled("cmplx_DEC_VerificationRequired",false);
                         formObject.setVisible("cmplx_DEC_ContactPointVeri",false);
                         formObject.setVisible("DecisionHistory_Label3",false);
                         formObject.setVisible("DecisionHistory_CheckBox1",false);
                         formObject.setVisible("cmplx_DEC_Decision_Reasoncode",true);
                         formObject.setEnabled("cmplx_DEC_Decision_Reasoncode",true);
                		 formObject.setVisible("DecisionHistory_Label11",true);
                		 formObject.setVisible("DecisionHistory_Rejreason",true);//rej resn
         		 		 formObject.setVisible("cmplx_DEC_RejectReason",true);//rej rsn field treated as remarks
         		 		formObject.setEnabled("cmplx_DEC_RejectReason",true);
                        //12th sept
						disableButtonsCC("DecisionHistory");
						formObject.setTop("DecisionHistory_save",240);
						formObject.setTop("DecisionHistory_Decision_ListView1",300);
						//Ref 1000. changes done by saurabh on 17th Sept for keeping sync in decision history of CSM and initiation end.
						
                       
                        
                        loadPicklist3();
				 } 	
			
			
			  break;
			  
			case MOUSE_CLICKED:
				SKLogger_CC.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
				if (pEvent.getSource().getName().equalsIgnoreCase("Customer_ReadFromCard")){
					//GenerateXML();
				}
				//12th september
				if(pEvent.getSource().getName().equalsIgnoreCase("Delete"))
				{
					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_Product_cmplx_ProductGrid");
				}
				
				//12th september
				
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
				if(pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails_add"))
				{
					formObject.setNGValue("PartnerDetails_partner_winame",formObject.getWFWorkitemName());
					SKLogger_CC.writeLog("CC", "Inside add button: "+formObject.getNGValue("PartnerDetails_partner_winame"));
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_PartnerDetails_cmplx_partnerGrid");
				}

				if(pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails_Add"))
				{
					formObject.setNGValue("company_winame",formObject.getWFWorkitemName());
					SKLogger_CC.writeLog("CC", "Inside add button: "+formObject.getNGValue("company_winame"));
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_CompanyDetails_cmplx_CompanyGrid");
				}
				// ++ below code not commented at offshore - 06-10-2017
				if(pEvent.getSource().getName().equalsIgnoreCase("cmplx_OECD_cmplx_GR_OecdDetails"))
				{
					LoadPickList("OECD_CountryBirth", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_Country  with (nolock) where IsActive='Y' order by code");
					LoadPickList("OECD_townBirth", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_city with (nolock) where IsActive='Y' order by code");
					LoadPickList("OECD_CountryTaxResidence", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_Country  with (nolock) where IsActive='Y' order by code");

				}

				if(pEvent.getSource().getName().equalsIgnoreCase("ExtLiability_Button2"))
				{
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
				}
				if(pEvent.getSource().getName().equalsIgnoreCase("ExtLiability_Button3"))
				{
					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
				}
				if(pEvent.getSource().getName().equalsIgnoreCase("ExtLiability_Button4"))
				{
					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
				}
				//12th sept
				
				if(pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails_modify"))
				{

					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_PartnerDetails_cmplx_partnerGrid");
				}

				if(pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails_delete"))
				{

					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_PartnerDetails_cmplx_partnerGrid");
				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails_add"))
				{
					formObject.setNGValue("AuthorisedSignDetails_AuthorisedSign_wiName",formObject.getWFWorkitemName());
					SKLogger_CC.writeLog("CC", "Inside add button: "+formObject.getNGValue("AuthorisedSignDetails_AuthorisedSign_wiName"));
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails");
					new CC_CommonCode().disablePartnerDetails(formObject);
				}

				if(pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails_modify"))
				{

					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails");
					new CC_CommonCode().disablePartnerDetails(formObject);
				}

				if(pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails_delete"))
				{

					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails");
				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("Reference_Details_Reference_Add"))
				{

					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_RefDetails_cmplx_Gr_ReferenceDetails");
				}
				if(pEvent.getSource().getName().equalsIgnoreCase("Reference_Details_Reference__modify"))
				{

					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_RefDetails_cmplx_Gr_ReferenceDetails");
				}
				if(pEvent.getSource().getName().equalsIgnoreCase("Reference_Details_Reference_delete"))
				{

					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_RefDetails_cmplx_Gr_ReferenceDetails");
				}
				
				//code by saurabh
				if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_Add"))
				{

					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_IncomeDetails_Cmplx_gr_IncomdeDetails");
				}
				if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_Modify"))
				{

					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_IncomeDetails_Cmplx_gr_IncomdeDetails");
				}
				if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_Delete"))
				{

					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_IncomeDetails_Cmplx_gr_IncomdeDetails");
				}
				
				if (pEvent.getSource().getName().equalsIgnoreCase("FATCA_Button1")){
					String text=formObject.getNGItemText("cmplx_FATCA_listedreason", formObject.getSelectedIndex("cmplx_FATCA_listedreason"));
					SKLogger_CC.writeLog("RLOS", "Inside FATCA_Button1 "+text);
					formObject.addItem("cmplx_FATCA_selectedreason", text);
					try {
						formObject.removeItem("cmplx_FATCA_listedreason", formObject.getSelectedIndex("cmplx_FATCA_listedreason"));
						formObject.setSelectedIndex("cmplx_FATCA_listedreason", -1);

					}catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						printException(e);
					}

				}

				
				 if (pEvent.getSource().getName().equalsIgnoreCase("FATCA_Button2")){
					SKLogger_CC.writeLog("RLOS", "Inside FATCA_Button2 ");
					formObject.addItem("cmplx_FATCA_listedreason", formObject.getNGItemText("cmplx_FATCA_selectedreason", formObject.getSelectedIndex("cmplx_FATCA_selectedreason")));
					try {
						formObject.removeItem("cmplx_FATCA_selectedreason", formObject.getSelectedIndex("cmplx_FATCA_selectedreason"));
						formObject.setSelectedIndex("cmplx_FATCA_selectedreason", -1);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						printException(e);
					}
				}
				 
				 if (pEvent.getSource().getName().equalsIgnoreCase("OECD_add")){					
						formObject.setNGValue("OECD_winame",formObject.getWFWorkitemName());
						SKLogger_CC.writeLog("RLOS", "Inside add button: "+formObject.getNGValue("OECD_winame"));
						//below code by saurabh on 22md july 17.
						formObject.setEnabled("OECD_noTinReason",true);
						formObject.ExecuteExternalCommand("NGAddRow", "cmplx_OECD_cmplx_GR_OecdDetails");
					}


				  if (pEvent.getSource().getName().equalsIgnoreCase("OECD_modify")){
						formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_OECD_cmplx_GR_OecdDetails");
					}


				 if (pEvent.getSource().getName().equalsIgnoreCase("OECD_delete")){
						formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_OECD_cmplx_GR_OecdDetails");
					}
				
			
				if(pEvent.getSource().getName().equalsIgnoreCase("Customer_save")){
					SKLogger_CC.writeLog("PL_Initiation", "Inside Customer_save button: ");
					formObject.saveFragment("CustomerDetails");
					alert_msg="Customer details saved";

					throw new ValidatorException(new FacesMessage(alert_msg));

				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("Product_Save")){
					formObject.saveFragment("ProductContainer");
					alert_msg="Product details saved";

					throw new ValidatorException(new FacesMessage(alert_msg));

				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails_Save")){
					formObject.saveFragment("GuarantorDetails");
					alert_msg="Guarantor details saved";

					throw new ValidatorException(new FacesMessage(alert_msg));

				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_Salaried_Save")){
					formObject.saveFragment("IncomeDEtails");
					alert_msg="Salaried Income details saved";

					throw new ValidatorException(new FacesMessage(alert_msg));

				}
				if(pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails_Saved")){
					formObject.saveFragment("AltContactDetails");
					alert_msg="Contact details saved";

					throw new ValidatorException(new FacesMessage(alert_msg));

				}
				//product grid code
				if(pEvent.getSource().getName().equalsIgnoreCase("cmplx_Product_cmplx_ProductGrid")){
					String ReqProd=formObject.getNGValue("ReqProd");
					SKLogger_CC.writeLog("CC CSM grid row click", "Value of ReqProd is:"+ReqProd);
					loadPicklistProduct(ReqProd);
				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails_SelfEmployed_Save")){
					formObject.saveFragment("Incomedetails");
					alert_msg="Self Employed Income details saved";

					throw new ValidatorException(new FacesMessage(alert_msg));

				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails_Save")){
					formObject.saveFragment("CompanyDetails");
					alert_msg="Company details saved";

					throw new ValidatorException(new FacesMessage(alert_msg));

				}
				if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails_Button2")){
					try{
					formObject.setNGValue("CardDetailsGR_WiName",formObject.getWFWorkitemName());
					SKLogger_CC.writeLog("CC", "Inside add button: add of card details");
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_CardDetails_cmpmx_gr_cardDetails");
					}catch(Exception ex){
						SKLogger_CC.writeLog("CC", "Inside exception add of card details"+printException(ex));
					}
				}
if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails_Button3")){
					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_CardDetails_cmpmx_gr_cardDetails");
				}
				if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails_Button4")){
					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_CardDetails_cmpmx_gr_cardDetails");
					
				}
				if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails_add")){
					formObject.setNGValue("CardDetailsGR_WiName",formObject.getWFWorkitemName());
					SKLogger_CC.writeLog("CCworkitem number",formObject.getNGValue("CardDetailsGR_WiName"));
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_CardDetails_cmplx_CardCRNDetails");
					SKLogger_CC.writeLog("CC","after adding in the grid");
					
				}
				if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails_modify")){
					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_CardDetails_cmplx_CardCRNDetails");
					SKLogger_CC.writeLog("CC", "Inside add button22: modify of card details");
				}
				if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails_delete")){
					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_CardDetails_cmplx_CardCRNDetails");
					SKLogger_CC.writeLog("CC", "Inside add button33: delete of card details");
					
				}
				if (pEvent.getSource().getName().equalsIgnoreCase("BT_Add")){
					formObject.setNGValue("CC_Loan_wi_name",formObject.getWFWorkitemName());
					SKLogger_CC.writeLog("CC",formObject.getNGValue("CC_Loan_wi_name"));
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_CC_Loan_cmplx_btc");
					SKLogger_CC.writeLog("CC", "Inside add button33: add of btc details");
					
				}
				if (pEvent.getSource().getName().equalsIgnoreCase("BT_Modify")){
					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_CC_Loan_cmplx_btc");
					SKLogger_CC.writeLog("CC", "Inside add button33: modify of btc details");
					
				}
				if (pEvent.getSource().getName().equalsIgnoreCase("BT_Delete")){
					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_CC_Loan_cmplx_btc");
					SKLogger_CC.writeLog("CC", "Inside add button33: delete of btc details");
					
				}
				
				
				
				//12th sept
				if(pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails_Button1")){
					formObject.saveFragment("PartnerDetails");
					alert_msg="Partner details saved";

					throw new ValidatorException(new FacesMessage(alert_msg));

				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("SelfEmployed_Save")){
					formObject.saveFragment("Liability_container");
					alert_msg="";

					throw new ValidatorException(new FacesMessage(alert_msg));

				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("Liability_New_Save")){
					formObject.saveFragment("InternalExternalContainer");
					alert_msg="Liability details saved";

					throw new ValidatorException(new FacesMessage(alert_msg));

				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("EmpDetails_Save")){
					formObject.saveFragment("EmploymentDetails");
					alert_msg="Employment details saved";

					throw new ValidatorException(new FacesMessage(alert_msg));

				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo_Save")){
					formObject.saveFragment("EligibilityAndProductInformation");
					alert_msg="Eligibility and Product Info details saved";

					throw new ValidatorException(new FacesMessage(alert_msg));

				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("MiscellaneousFields_Save")){
					formObject.saveFragment("MiscFields");
					alert_msg="Miscellaneous Fields Saved";

					throw new ValidatorException(new FacesMessage(alert_msg));

				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("AddressDetails_Save")){
					// ++ below code already present - 06-10-2017 name change from Address_Details_container to AddressDetails
					formObject.saveFragment("AddressDetails");
					alert_msg="Address Details Saved";

					throw new ValidatorException(new FacesMessage(alert_msg));

				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails_Button1")){
					formObject.saveFragment("Alt_Contact_container");
					alert_msg="Contact Details Saved";

					throw new ValidatorException(new FacesMessage(alert_msg));

				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("CardDetails_save")){
					// ++ below code already present - 06-10-2017 - below line commented
					//formObject.saveFragment("Supplementary_Cont");
					formObject.saveFragment("Card_Details");
					alert_msg="Card Details Saved";

					throw new ValidatorException(new FacesMessage(alert_msg));

				}
				
				
				if(pEvent.getSource().getName().equalsIgnoreCase("FATCA_Save")){
					formObject.saveFragment("FATCA");
					alert_msg="FATCA Fields Saved";

					throw new ValidatorException(new FacesMessage(alert_msg));

				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("KYC_Save")){
					formObject.saveFragment("KYC");
					alert_msg="KYC Details Saved";

					throw new ValidatorException(new FacesMessage(alert_msg));

				}
				
				if(pEvent.getSource().getName().equalsIgnoreCase("OECD_Save")){
					formObject.saveFragment("OECD");
					alert_msg="OECD Details Saved";

					throw new ValidatorException(new FacesMessage(alert_msg));

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

				if(pEvent.getSource().getName().equalsIgnoreCase("Reference_Details_save")){
					formObject.saveFragment("Reference_Details");
					alert_msg="Reference Details Saved";

					throw new ValidatorException(new FacesMessage(alert_msg));
				}
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
				if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Add1")){
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_Telloggrid");
				}
				if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Clear")){
					formObject.ExecuteExternalCommand("NGClear", "cmplx_NotepadDetails_cmplx_Telloggrid");
				}
				//ended by yash on 24/8/2017
				
				
				if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Button1")){
					formObject.saveFragment("DecisionHistory");
					alert_msg="Decision Details Saved";

					throw new ValidatorException(new FacesMessage(alert_msg));

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
						//++ Below Code already exists for : "8-CSM-Company Details-" Employer industry macro micro not linked" : Reported By Shashank on Oct 05, 2017++
					 if (pEvent.getSource().getName().equalsIgnoreCase("indusSector")){
						 SKLogger_CC.writeLog(" In PL_Initiation VALChanged---New Value of decision is: ","before macro");
							LoadPickList("indusMAcro", "select '--Select--' union select  macro from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where IndustrySector='"+formObject.getNGValue("indusSector")+"' and IsActive='Y'");
							SKLogger_CC.writeLog(" In PL_Initiation VALChanged---New Value of decision is: ","after macro");	
					 }

						if (pEvent.getSource().getName().equalsIgnoreCase("indusMAcro")){
							SKLogger_CC.writeLog(" In PL_Initiation VALChanged---New Value of decision is: ", "before micro");
							LoadPickList("indusMicro", "select '--Select--' union select  micro from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where Macro='"+formObject.getNGValue("indusMAcro")+"' and IsActive='Y'");
							SKLogger_CC.writeLog(" In PL_Initiation VALChanged---New Value of decision is: ","for micro");
						}
						//++ Above Code already exists for : "8-CSM-Company Details-" Employer industry macro micro not linked" : Reported By Shashank on Oct 05, 2017++
          
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
					 //12th september
					 if (pEvent.getSource().getName().equalsIgnoreCase("ReqProd")){
							String ReqProd=formObject.getNGValue("ReqProd");
							SKLogger_CC.writeLog("RLOS val change ", "Value of ReqProd is:"+ReqProd);
							loadPicklistProduct(ReqProd);
						}
					 //12th september
					 
					 if (pEvent.getSource().getName().equalsIgnoreCase("SubProd")){
							SKLogger_CC.writeLog("RLOS val change ", "Value of SubProd is:"+formObject.getNGValue("SubProd"));
							formObject.clear("AppType");

							LoadPickList("AppType", "select convert(varchar, description),code from ng_master_ApplicationType with (nolock) where subProdFlag='"+formObject.getNGValue("SubProd")+"' order by code");
							formObject.setNGValue("AppType","--Select--");

							String subprod=formObject.getNGValue("SubProd");
							String VIPFlag=formObject.getNGValue("cmplx_Customer_VIPFlag");
							String Nationality=formObject.getNGValue("cmplx_Customer_Nationality");
							String req_prod=formObject.getNGValue("ReqProd");
							String TypeOfProd=formObject.getNGValue("Product_type");
							SKLogger_CC.writeLog("RLOS val change of card prod ", "Value of SubProd is:"+subprod);
							SKLogger_CC.writeLog("RLOS val change ", "Value of vip is:"+VIPFlag);
							SKLogger_CC.writeLog("RLOS val change ", "Value of Nationality is:"+Nationality);
							SKLogger_CC.writeLog("RLOS val change ", "Value of req_prod is:"+req_prod);
							SKLogger_CC.writeLog("RLOS val change ", "Value of TypeOfProd is:"+TypeOfProd);

							if (req_prod.equalsIgnoreCase("Credit Card")){
								if(subprod.equalsIgnoreCase("PU")||subprod.equalsIgnoreCase("PULI")||subprod.equalsIgnoreCase("LI"))
								{
									formObject.clear("CardProd");
									formObject.setNGValue("CardProd","--Select--");
									SKLogger_CC.writeLog("subprod is not ", " is not BTC ,PA :"+subprod);
									if(TypeOfProd.equalsIgnoreCase("Conventional")){
									LoadPickList("CardProd","select convert(varchar, Description) from ng_master_cardProduct with (nolock) where reqproduct like '%conventional%'");
									}
									else{
									LoadPickList("CardProd","select convert(varchar, Description) from ng_master_cardProduct with (nolock) where reqproduct like '%Islamic%'");
										
									}
								}
								else{
									formObject.clear("CardProd");
									formObject.setNGValue("CardProd","--Select--");
									String query1="select convert(varchar, Description) from ng_master_cardProduct with (nolock) where Reqproduct='"+TypeOfProd+"' and SUBproduct = '"+subprod+"'  and VIPFlag='"+VIPFlag+"' and Nationality='AE'";	
									String query2="select convert(varchar, Description) from ng_master_cardProduct with (nolock) where Reqproduct='"+TypeOfProd+"' and SUBproduct = '"+subprod+"'  and VIPFlag='"+VIPFlag+"' and Nationality!='AE'";
									SKLogger_CC.writeLog("Nationality ", ": "+Nationality);
									SKLogger_CC.writeLog("Query For Card Product Dropdown: ",query1);
									if(Nationality.equalsIgnoreCase("AE")){
										SKLogger_CC.writeLog("Query For Card Product Dropdown: ",query1);
										LoadPickList("CardProd",query1);
									}
									else{
										SKLogger_CC.writeLog("Query For Card Product Dropdown: ",query2);
										LoadPickList("CardProd",query2);
									}
								}
							}
						}
					 if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_Customer_DOb")){
							SKLogger_CC.writeLog("CC val change ", "Value of dob is:"+formObject.getNGValue("cmplx_Customer_DOb"));
							getAge(formObject.getNGValue("cmplx_Customer_DOb"),"cmplx_Customer_age");
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("cmplx_EmploymentDetails_DOJ")){
							//SKLogger.writeLog("RLOS val change ", "Value of dob is:"+formObject.getNGValue("cmplx_EmploymentDetails_DOJ"));
							getAge(formObject.getNGValue("cmplx_EmploymentDetails_DOJ"),"cmplx_EmploymentDetails_LOS");
						}
					 if (pEvent.getSource().getName().equalsIgnoreCase("AppType"))
						{
							String subprod=formObject.getNGValue("SubProd");
							String apptype=formObject.getNGValue("AppType");
							String TypeofProduct=formObject.getNGValue("Product_type");
							SKLogger_CC.writeLog("RLOS val change ", "Value of SubProd is:"+formObject.getNGValue("SubProd"));
							SKLogger_CC.writeLog("RLOS val change ", "Value of AppType is:"+formObject.getNGValue("AppType"));
							SKLogger_CC.writeLog("RLOS val change ", "Value of AppType is:"+formObject.getNGValue("Product_type"));


							if ((TypeofProduct.equalsIgnoreCase("Conventional"))&& subprod.equalsIgnoreCase("EXP")&&(apptype.equalsIgnoreCase("TKOE"))){


								SKLogger_CC.writeLog("RLOS val change ", "1Value of AppType is:"+formObject.getNGValue("AppType"));


								formObject.clear("Scheme");
								formObject.setNGValue("Scheme","--Select--");
								LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Take Over'  and TypeofProduct='Conventional' order by SCHEMEID");




							}
							if ((TypeofProduct.equalsIgnoreCase("Conventional"))&& subprod.equalsIgnoreCase("EXP")&&(apptype.equalsIgnoreCase("NEWE"))){
								SKLogger_CC.writeLog("RLOS val change ", "2Value of AppType is:"+formObject.getNGValue("AppType"));




								formObject.clear("Scheme");
								formObject.setNGValue("Scheme","--Select--");
								LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'New' and TypeofProduct='Conventional' order by SCHEMEID");




							}
							if ((TypeofProduct.equalsIgnoreCase("Conventional")&&subprod.equalsIgnoreCase("EXP"))&&(apptype.equalsIgnoreCase("TOPE"))){
								SKLogger_CC.writeLog("RLOS val change ", "3Value of AppType is:"+formObject.getNGValue("AppType"));


								formObject.clear("Scheme");
								formObject.setNGValue("Scheme","--Select--");
								LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Top up' and TypeofProduct='Conventional' order by SCHEMEID");


							}
							if ((TypeofProduct.equalsIgnoreCase("Conventional")&&subprod.equalsIgnoreCase("EXP"))&&(apptype.equalsIgnoreCase("RESCE"))){
								SKLogger_CC.writeLog("RLOS val change ", "4Value of AppType is:"+formObject.getNGValue("AppType"));


								formObject.clear("Scheme");
								formObject.setNGValue("Scheme","--Select--");
								LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Reschedulment' and TypeofProduct='Conventional' order by SCHEMEID");


							}
							if ((TypeofProduct.equalsIgnoreCase("Conventional")&& subprod.equalsIgnoreCase("NAT"))&&(apptype.equalsIgnoreCase("TOPN"))){
								SKLogger_CC.writeLog("RLOS val change ", "5Value of AppType is:"+formObject.getNGValue("AppType"));
								formObject.clear("Scheme");
								formObject.setNGValue("Scheme","--Select--");
								LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Top up' and TypeofProduct='Conventional' order by SCHEMEID");


							}
							if ((TypeofProduct.equalsIgnoreCase("Conventional")&&subprod.equalsIgnoreCase("NAT"))&&(apptype.equalsIgnoreCase("TKON"))){
								SKLogger_CC.writeLog("RLOS val change ", "6Value of AppType is:"+formObject.getNGValue("AppType"));
								formObject.clear("Scheme");
								formObject.setNGValue("Scheme","--Select--");
								LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Take Over' and TypeofProduct='Conventional' order by SCHEMEID");


							}
							if ((TypeofProduct.equalsIgnoreCase("Conventional")&&subprod.equalsIgnoreCase("NAT"))&&(apptype.equalsIgnoreCase("NEWN"))){
								SKLogger_CC.writeLog("RLOS val change ", "7Value of AppType is:"+formObject.getNGValue("AppType"));
								formObject.clear("Scheme");
								formObject.setNGValue("Scheme","--Select--");
								LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'New' and TypeofProduct='Conventional' order by SCHEMEID");


							}
							if ((TypeofProduct.equalsIgnoreCase("Conventional")&&subprod.equalsIgnoreCase("NAT"))&&(apptype.equalsIgnoreCase("RESCN"))){
								SKLogger_CC.writeLog("RLOS val change ", "8Value of AppType is:"+formObject.getNGValue("AppType"));
								formObject.clear("Scheme");
								formObject.setNGValue("Scheme","--Select--");
								LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Reschedulment' and TypeofProduct='Conventional' order by SCHEMEID");


							}
							if ((TypeofProduct.equalsIgnoreCase("Islamic"))&& subprod.equalsIgnoreCase("EXP")&&(apptype.equalsIgnoreCase("TKOE"))){


								SKLogger_CC.writeLog("RLOS val change ", "1Value of AppType is:"+formObject.getNGValue("AppType"));


								formObject.clear("Scheme");
								formObject.setNGValue("Scheme","--Select--");
								LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Take Over'  and TypeofProduct='Islamic' order by SCHEMEID");				
							}

							if ((TypeofProduct.equalsIgnoreCase("Islamic"))&& subprod.equalsIgnoreCase("EXP")&&(apptype.equalsIgnoreCase("NEWE"))){
								SKLogger_CC.writeLog("RLOS val change ", "2Value of AppType is:"+formObject.getNGValue("AppType"));
								formObject.clear("Scheme");
								formObject.setNGValue("Scheme","--Select--");
								LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'New' and TypeofProduct='Islamic' order by SCHEMEID");				
							}

							if ((TypeofProduct.equalsIgnoreCase("Islamic")&&subprod.equalsIgnoreCase("EXP"))&&(apptype.equalsIgnoreCase("TOPE"))){
								SKLogger_CC.writeLog("RLOS val change ", "3Value of AppType is:"+formObject.getNGValue("AppType"));


								formObject.clear("Scheme");
								formObject.setNGValue("Scheme","--Select--");
								LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Top up' and TypeofProduct='Islamic' order by SCHEMEID");


							}
							if ((TypeofProduct.equalsIgnoreCase("Islamic")&&subprod.equalsIgnoreCase("EXP"))&&(apptype.equalsIgnoreCase("RESCE"))){
								SKLogger_CC.writeLog("RLOS val change ", "4Value of AppType is:"+formObject.getNGValue("AppType"));


								formObject.clear("Scheme");
								formObject.setNGValue("Scheme","--Select--");
								LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Reschedulment' and TypeofProduct='Islamic' order by SCHEMEID");


							}
							if ((TypeofProduct.equalsIgnoreCase("Islamic")&& subprod.equalsIgnoreCase("NAT"))&&(apptype.equalsIgnoreCase("TOPN"))){
								SKLogger_CC.writeLog("RLOS val change ", "5Value of AppType is:"+formObject.getNGValue("AppType"));
								formObject.clear("Scheme");
								formObject.setNGValue("Scheme","--Select--");
								LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Top up' and TypeofProduct='Islamic' order by SCHEMEID");


							}
							if ((TypeofProduct.equalsIgnoreCase("Islamic")&&subprod.equalsIgnoreCase("NAT"))&&(apptype.equalsIgnoreCase("TKON"))){
								SKLogger_CC.writeLog("RLOS val change ", "6Value of AppType is:"+formObject.getNGValue("AppType"));
								formObject.clear("Scheme");
								formObject.setNGValue("Scheme","--Select--");
								LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Take Over' and TypeofProduct='Islamic' order by SCHEMEID");


							}
							if ((TypeofProduct.equalsIgnoreCase("Islamic")&&subprod.equalsIgnoreCase("NAT"))&&(apptype.equalsIgnoreCase("NEWN"))){
								SKLogger_CC.writeLog("RLOS val change ", "7Value of AppType is:"+formObject.getNGValue("AppType"));
								formObject.clear("Scheme");
								formObject.setNGValue("Scheme","--Select--");
								LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'New' and TypeofProduct='Islamic' order by SCHEMEID");


							}
							if ((TypeofProduct.equalsIgnoreCase("Islamic")&&subprod.equalsIgnoreCase("NAT"))&&(apptype.equalsIgnoreCase("RESCN"))){
								SKLogger_CC.writeLog("RLOS val change ", "8Value of AppType is:"+formObject.getNGValue("AppType"));
								formObject.clear("Scheme");
								formObject.setNGValue("Scheme","--Select--");
								LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Reschedulment' and TypeofProduct='Islamic' order by SCHEMEID");

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
		formObject.setNGValue("DecisionHistory_Cif_ID",formObject.getNGValue("cmplx_Customer_CIFNo"));
		formObject.setNGValue("DecisionHistory_Emirates_Id",formObject.getNGValue("cmplx_Customer_CIFNo"));
		String fName = formObject.getNGValue("cmplx_Customer_FirstNAme");
        String mName = formObject.getNGValue("cmplx_Customer_MiddleNAme");
        String lName = formObject.getNGValue("cmplx_Customer_LastNAme");
        String fullName = fName+" "+mName+" "+lName; 
        formObject.setNGValue("DecisionHistory_Customer_Name",fullName);
		saveIndecisionGridCSM();
	}


	public void continueExecution(String arg0, HashMap<String, String> arg1) {
		// TODO Auto-generated method stub
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();	
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
		//saveIndecisionGrid();
	}

}

