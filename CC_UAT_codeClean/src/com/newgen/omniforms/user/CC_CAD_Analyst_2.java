
/*------------------------------------------------------------------------------------------------------

                     NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                             : Application -Projects

Project/Product                                                   : Rakbank  

Application                                                       : Credit Card

Module                                                            : Cad Analyst2

File Name                                                         : CC_CAD_Analyst2

Author                                                            : Disha 

Date (DD/MM/YYYY)                                                 : 

Description                                                       : 

-------------------------------------------------------------------------------------------------------

CHANGE HISTORY

-------------------------------------------------------------------------------------------------------

Problem No/CR No   Change Date   Changed By    Change Description

------------------------------------------------------------------------------------------------------*/

package com.newgen.omniforms.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;

import com.newgen.omniforms.util.SKLogger_CC;


import javax.faces.application.*;

public class CC_CAD_Analyst_2 extends CC_Common implements FormListener
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
		SKLogger_CC.writeLog("RLOS Initiation", "Inside formLoaded()" + pEvent.getSource().getName());
		
		makeSheetsInvisible(tabName, "9,11,12,14,15,16,17");
		formObject.setVisible("SmartCheck_Label2",true);
		formObject.setVisible("cmplx_SmartCheck_SmartCheckGrid_cpvremarks",true);
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
	
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String alert_msg="";
		SKLogger_CC.writeLog("Inside PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		  formObject =FormContext.getCurrentInstance().getFormReference();

				switch(pEvent.getType())
				{	

				case FRAME_EXPANDED:
					SKLogger_CC.writeLog(" In PL_Iniation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
					new CC_CommonCode().FrameExpandEvent(pEvent);						
					break;
					
					  
					case FRAGMENT_LOADED:
						SKLogger_CC.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			 	
				/*if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
        			
				}*/
						//added by yash for CC FSD
						if (pEvent.getSource().getName().equalsIgnoreCase("Customer")) {
							formObject.setLocked("Customer_Frame1",true);
							loadPicklistCustomer();
						
						}	
						//added by yash for  CC FSD
						if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
							formObject.setLocked("Product_Frame1",true);
							int prd_count=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
							if(prd_count>0){
								String ReqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
							loadPicklistProduct(ReqProd);
							}
							//Code commented by deepak as we are loading desc so no need to load the picklist(grid is already having desc) - 28Sept2017
							/*
							LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
							LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");
							LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
							
							//change by saurabh on 7tb oct for JIRA- 2561. If condition added.
							if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("IM")){
							formObject.setVisible("Product_Label5",true);
							formObject.setVisible("ReqTenor",true);
							}
							formObject.setLocked("Product_Frame1",true);
							formObject.setLocked("Add",true);
							formObject.setLocked("Modify",true);
							formObject.setLocked("Delete",true);
							formObject.setLocked("Product_Save",true);
							formObject.setLocked("cmplx_Product_cmplx_ProductGrid_table",true);
							formObject.setLocked("Customer_save",true);
							formObject.setLocked("Customer_save",true);*/
							
						}
						//added by yash for CC FSD
						if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails")) {
							formObject.setLocked("IncomeDetails_Frame1",true);
							LoadPickList("cmplx_IncomeDetails_AvgBalFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
							LoadPickList("cmplx_IncomeDetails_CreditTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
							LoadPickList("cmplx_IncomeDetails_AvgCredTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
							LoadPickList("cmplx_IncomeDetails_AnnualRentFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
							
							formObject.setLocked("IncomeDetails_Text2",true);
							formObject.setVisible("IncomeDetails_Label22",false);
							formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsRakbankStat2",false);
							/*formObject.setVisible("IncomeDetails_ListView1",true);
							formObject.setVisible("IncomeDetails_Button1",true);
							formObject.setVisible("IncomeDetails_Button2",true);
							formObject.setVisible("IncomeDetails_Button3",true);*/
						}
						//added by yash for CC FSD
						if (pEvent.getSource().getName().equalsIgnoreCase("CompanyDetails")) {
							loadPicklist_CompanyDet();
							formObject.setLocked("CompanyDetails_Frame1",true);
							//loadPicklist_Address();
							//formObject.setLocked("CompanyDetails_Frame1",true);
							formObject.setVisible("CompanyDetails_CheckBox1",true);
							formObject.setVisible("CompanyDetails_Label17",true);
							formObject.setVisible("CompanyDetails_highdelin",true);
							formObject.setVisible("CompanyDetails_Text1",true);
							formObject.setVisible("CompanyDetails_Label14",true);
							formObject.setVisible("CompanyDetails_currempcateg",true);
							formObject.setVisible("CompanyDetails_Text2",true);							
							formObject.setVisible("CompanyDetails_Label16",true);
							formObject.setVisible("CompanyDetails_categcards",true);
							formObject.setVisible("CompanyDetails_Text3",true);
							formObject.setVisible("CompanyDetails_Label12",true);
							formObject.setVisible("CompanyDetails_categexpat",true);
							formObject.setVisible("CompanyDetails_Text4",true);
							formObject.setVisible("CompanyDetails_Label15",true);
							formObject.setVisible("CompanyDetails_categnational",true);
							formObject.setVisible("CompanyDetails_Text5",true);
							formObject.setLocked("CompanyDetails_Text1",false);
							formObject.setLocked("CompanyDetails_Text2",true);
							formObject.setLocked("CompanyDetails_Text3",true);
							formObject.setLocked("CompanyDetails_Text4",true);
							formObject.setLocked("CompanyDetails_Text5",true);
							//formObject.setVisible("CompanyDetails_Label8",false);
							formObject.setLocked("estbDate",false);
							formObject.setEnabled("CompanyDetails_DatePicker1",true);
						}
						//added by yash fro CC FSD
						if (pEvent.getSource().getName().equalsIgnoreCase("AuthorisedSignDetails")) {
							//loadPicklist_Address();
							formObject.setLocked("AuthorisedSignDetails_ShareHolding", true);
				            
							LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--' as Description, '' as code union select convert(varchar, description),code from NG_MASTER_Country with (nolock) order by code");
			                LoadPickList("AuthorisedSignDetails_SignStatus", "select '--Select--' as Description,'' as code union select convert(varchar, description),code from NG_MASTER_SignatoryStatus with (nolock) order by code");
			                LoadPickList("Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
							LoadPickList("DesignationAsPerVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
							}
						//added by yash for CC FSD
						if (pEvent.getSource().getName().equalsIgnoreCase("PartnerDetails")) {
							//loadPicklist_Address();
							 LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
							formObject.setLocked("PartnerDetails_Frame1",true);
							
						}
						//added by yash for CC FSD
						if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")) {
							//loadPicklist_Address();
							formObject.setLocked("ExtLiability_Frame1",true);
							formObject.setVisible("Liability_New_Label1",true);
							formObject.setVisible("Liability_New_Text1",true);
							formObject.setVisible("Liability_New_Label2",true);
							formObject.setVisible("Liability_New_Text3",true);
							formObject.setVisible("Liability_New_Label3",true);
							formObject.setVisible("Liability_New_Text2",true);
							formObject.setVisible("Liability_New_CheckBox1",true);
							formObject.setVisible("Liability_New_CheckBox2",true);
							formObject.setVisible("Liability_New_CheckBox3",true);
							formObject.setVisible("Liability_New_Label4",true);
							formObject.setVisible("Liability_New_Text4",true);
							formObject.setVisible("Liability_New_Label5",true);
							formObject.setVisible("Liability_New_Combo1",true);
							
							formObject.setVisible("ExtLiability_Label15",true);
							formObject.setVisible("cmplx_Liability_New_AggrExposure",true);
							formObject.setVisible("Liability_New_Label6",true);
							formObject.setVisible("Liability_New_Text5",true);
							formObject.setVisible("Liability_New_Label8",true);
							formObject.setVisible("Liability_New_Text6",true);
							formObject.setVisible("Liability_New_Label7",true);
							formObject.setVisible("Liability_New_Text7",true);
							formObject.setVisible("cmplx_Liability_New_NoofotherbankAuth",true);
							formObject.setVisible("cmplx_Liability_New_NoofotherbankCompany",true);
							formObject.setVisible("cmplx_Liability_New_TotcleanFundedAmt",true);
						
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("Internal Liabilities")) {
							//loadPicklist_Address();
							formObject.setLocked("ExtLiability_Frame2",true);
							
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("External Liabilities")) {
							//loadPicklist_Address();
							formObject.setLocked("ExtLiability_Frame3",true);
							
							
						}
						
						if (pEvent.getSource().getName().equalsIgnoreCase("Liability Addition")) {
							//loadPicklist_Address();
							formObject.setLocked("ExtLiability_Frame4",true);		
						}
						//added by yash for CC FSD
						if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) {
							formObject.setLocked("EMploymentDetails_Frame1", true);							
							//loadPicklist4();
							formObject.setVisible("EMploymentDetails_Label19",false);
							formObject.setVisible("cmplx_EmploymentDetails_funded_faci",false);
							formObject.setVisible("EMploymentDetails_Label16",false);
							formObject.setVisible("cmplx_EmploymentDetails_borrowing_rel",false);
							formObject.setVisible("EMploymentDetails_Label11",false);
							formObject.setVisible("cmplx_EmploymentDetails_head_office",false);
							
							
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo")) {
							//added 6th sept for proc 1926
							// ++ below code not commented at offshore - 06-10-2017
							formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
							if(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("false"))
							{
							formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalLimit", false);//total final limit
							formObject.setLocked("ELigibiltyAndProductInfo_Button1", false);//calculate re-eligibilit
							//loadPicklist_Address();
							//formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
							}
							//ended 6th sept for proc 1926
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails")) {
							//loadPicklist_Address();
							formObject.setLocked("AddressDetails_Frame1",true);
				
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails")) {
							//loadPicklist_Address();
							formObject.setLocked("AltContactDetails_Frame1",true);
						
						}
						//added by yash for CC FSD
						if (pEvent.getSource().getName().equalsIgnoreCase("Reference_Details")) {
							//loadPicklist_Address();
							formObject.setLocked("Reference_Details_ReferenceRelationship",true);
							
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("CardCollection")) {
							//loadPicklist_Address();
							formObject.setLocked("CardDetails_Frame1",true);
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")) {
							//loadPicklist_Address();
							formObject.setLocked("FATCA_Frame6",true);
							
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails")) {
							//loadPicklist_Address();
							formObject.setLocked("CardDetails_Frame1",true);
							
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("KYC")) {
							//loadPicklist_Address();
							formObject.setLocked("KYC_Frame1",true);
							
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("OECD")) {
							//loadPicklist_Address();
							formObject.setLocked("OECD_Frame8",true);
							
						}
						// ++ below code already present - 06-10-2017 name change from Details to CC_Loan
						if (pEvent.getSource().getName().equalsIgnoreCase("Details")) {
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
							//added by saurabh on13th Oct for JIRA-2517
							formObject.setLocked("FinacleCore_Frame9", true);
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
						if (pEvent.getSource().getName().equalsIgnoreCase("SmartCheck1")) {
							formObject.setLocked("SmartCheck_Frame1",true);
							 formObject.setVisible("SmartCheck1_Label2",false);
							 formObject.setVisible("SmartCheck1_CPVRemarks",false);
							 formObject.setVisible("SmartCheck1_Label4",false);
							 formObject.setVisible("SmartCheck1_FCURemarks",false);
							 
							 //formObject.setLocked("SmartCheck1_Modify",true);
							 
							
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("RejectEnq")) {
							//loadPicklist_Address();
							formObject.setLocked("RejectEnq_Frame1",true);
							
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("SalaryEnq")) {
							//loadPicklist_Address();
							formObject.setLocked("SalaryEnq_Frame1",true);
							
						}
						/*if (pEvent.getSource().getName().equalsIgnoreCase("External_Blacklist")) {
							//loadPicklist_Address();
							formObject.setLocked("ExternalBlackList_Frame1",true);
							
						}*/
						if (pEvent.getSource().getName().equalsIgnoreCase("IncomingDocument")) {
							//loadPicklist_Address();
							formObject.setLocked("IncomingDocument_Frame",true);
							
						}
						if (pEvent.getSource().getName().equalsIgnoreCase("PostDisbursal")) {
							//loadPicklist_Address();
							formObject.setLocked("PostDisbursal_Frame2",true);
							
						}
						//added by yash for CC FSD
						if (pEvent.getSource().getName().equalsIgnoreCase("Compliance")) {
							formObject.setLocked("Compliance_Frame1",true);
						}
						//ended by yash for CC FSD
						if (pEvent.getSource().getName().equalsIgnoreCase("CC_Loan")) {
							//loadPicklist_Address();
							formObject.setLocked("CC_Loan_Frame1",true);
							
						}
						//added by yash for CC FSD
						if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails")) {
							notepad_withoutTelLog();
							notepad_load();
					        
							formObject.setTop("NotepadDetails_savebutton",410);
						}
						//ended by yash
						if (pEvent.getSource().getName().equalsIgnoreCase("Fraud Control Unit")) {
						LoadPickList("cmplx_Supervisor_SubFeedback_Status", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_Master_SubfeedbackStatus with (nolock)");
						}
		//added by yash for CC FSD
							if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {
								try{
								formObject.setTop("DecisionHistory_Frame2",6);
								formObject.setTop("DecisionHistory_Frame3",174);
								formObject.setTop("DecisionHistory_Decision_ListView1",515);
								formObject.setTop("DecisionHistory_save",690);
								formObject.setVisible("cmplx_DEC_ReferReason",false);
								formObject.setVisible("cmplx_DEC_Decision_Reasoncode",true);
								formObject.setTop("cmplx_DEC_Decision_Reasoncode",411);
								formObject.setLeft("cmplx_DEC_Decision_Reasoncode",330);
								/*formObject.setVisible("DecisionHistory_CheckBox1",false);
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
		                        formObject.setVisible("DecisionHistory_Text3",false);*/
		                        //formObject.setVisible("DecisionHistory_Label2",false);
		                        /*formObject.setVisible("DecisionHistory_Text2",false);
		                        formObject.setVisible("cmplx_DEC_ReferReason",false);*/
		                        //formObject.setVisible("cmplx_DEC_Description",false);
		                        /*formObject.setVisible("cmplx_DEC_Strength",false);
		                        formObject.setVisible("cmplx_DEC_Weakness",false);
		                        formObject.setLocked("cmplx_DEC_ContactPointVeri",true);
		                        formObject.setVisible("cmplx_DEC_ContactPointVeri",false);*/
								formObject.setVisible("DecisionHistory_Label15",true);
		                        formObject.setTop("DecisionHistory_Label15",350);
		                        formObject.setLeft("DecisionHistory_Label15",24);
		                        formObject.setLeft("DecisionHistory_Label5",24);
		                        formObject.setVisible("DecisionHistory_Label5",true);
		                        formObject.setVisible("cmplx_DEC_ScoreGrade",true);
		                        formObject.setTop("DecisionHistory_Label5",350);
		                        formObject.setTop("cmplx_DEC_ScoreGrade",366);
		                        formObject.setLeft("DecisionHistory_Label5",24);
		                        formObject.setLeft("cmplx_DEC_ScoreGrade",24);
		                        formObject.setTop("DecisionHistory_Label17",350);
		                        formObject.setTop("cmplx_DEC_LoanApprovalAuth",366);
		                        formObject.setLeft("DecisionHistory_Label17",330);
		                        formObject.setLeft("cmplx_DEC_LoanApprovalAuth",330);
		                        formObject.setTop("DecisionHistory_Button4",350);
		                        formObject.setLeft("DecisionHistory_Button4",650);
		                        formObject.setVisible("DecisionHistory_Decision_Label3",true);
		                        formObject.setVisible("cmplx_DEC_Decision",true);
		                        formObject.setTop("DecisionHistory_Decision_Label3",394);
		                        formObject.setLeft("DecisionHistory_Decision_Label3",24);
		                        formObject.setTop("cmplx_DEC_Decision",411);
		                        formObject.setLeft("cmplx_DEC_Decision",24);
		                        formObject.setVisible("DecisionHistory_Label1",false);
		                        formObject.setTop("DecisionHistory_Label11",394);
		                        formObject.setLeft("DecisionHistory_Label11",330);
		                        formObject.setVisible("DecisionHistory_Label3",true);
		                        formObject.setTop("DecisionHistory_Label3",440);
		                        formObject.setLeft("DecisionHistory_Label3",24);
		                        formObject.setVisible("cmplx_DEC_Strength",true);
		                        formObject.setTop("cmplx_DEC_Strength",457);
		                        formObject.setLeft("cmplx_DEC_Strength",24);
		                        formObject.setVisible("DecisionHistory_Label4",true);
		                        formObject.setTop("DecisionHistory_Label4",440);
		                        formObject.setLeft("DecisionHistory_Label4",330);
		                        formObject.setVisible("cmplx_DEC_Weakness",true);
		                        formObject.setTop("cmplx_DEC_Weakness",457);
		                        formObject.setLeft("cmplx_DEC_Weakness",330);
		                        formObject.setVisible("DecisionHistory_Decision_Label4",true);
		                        formObject.setTop("DecisionHistory_Decision_Label4",440);
		                        formObject.setLeft("DecisionHistory_Decision_Label4",650);
		                        formObject.setVisible("cmplx_DEC_Remarks",true);
		                        formObject.setTop("cmplx_DEC_Remarks",457);
		                        formObject.setLeft("cmplx_DEC_Remarks",650);
		                        formObject.setVisible("DecisionHistory_Rejreason",false);
		                        formObject.setTop("DecisionHistory_Rejreason",394);
		                        formObject.setLeft("DecisionHistory_Rejreason",650);
		                        formObject.setVisible("cmplx_DEC_RejectReason",false);
		                        formObject.setTop("cmplx_DEC_RejectReason",411);
		                        formObject.setLeft("cmplx_DEC_RejectReason",650);
		                        formObject.setVisible("DecisionHistory_Combo4",true);
		                        formObject.setVisible("DecisionHistory_Label10",true);
		                        formObject.setVisible("DecisionHistory_Text6",true);
		                        
		                       // formObject.setVisible("DecisionHistory_Combo5",true);
		                        
		                        formObject.setVisible("DecisionHistory_Button4",true);
		                        formObject.setVisible("DecisionHistory_Frame2",true);
		                        formObject.setVisible("DecisionHistory_Frame3",true);
		                        formObject.setVisible("DecisionHistory_Label17",true);
		                        formObject.setVisible("cmplx_DEC_LoanApprovalAuth",true);
		                        formObject.setVisible("DecisionHistory_Label11",true);
		                        formObject.setVisible("cmplx_DEC_Decision_Reasoncode",true);
		                        formObject.setVisible("cmplx_DEC_Cust_Contacted",false);
		                        formObject.setVisible("DecisionHistory_Label27",false);
		                        formObject.setVisible("cmplx_DEC_ContactPointVeri",false);
		                        formObject.setVisible("DecisionHistory_Decision_Label1",false);
		                        formObject.setVisible("cmplx_DEC_VerificationRequired",false);
		                        formObject.setVisible("DecisionHistory_Label6",false);
		                        formObject.setVisible("cmplx_DEC_IBAN_No",false);
		                        formObject.setVisible("DecisionHistory_Label7",false);
		                        formObject.setVisible("cmplx_DEC_NewAccNo",false);
		                        formObject.setVisible("DecisionHistory_Label8",false);
		                        formObject.setVisible("cmplx_DEC_ChequebookRef",false);
		                        formObject.setVisible("DecisionHistory_Label9",false);
		                        formObject.setVisible("cmplx_DEC_DCR_Refno",false);
		                        formObject.setVisible("DecisionHistory_Label5",false);
		                        formObject.setVisible("cmplx_DEC_Description",false);
		                        formObject.setVisible("DecisionHistory_Label5",false);
		                        formObject.setVisible("cmplx_DEC_Description",false);
		                        formObject.setVisible("DecisionHistory_Label27",false);
		                        formObject.setVisible("cmplx_DEC_Cust_Contacted",false);
		                        formObject.setVisible("DecisionHistory_Label10",false);
		                        formObject.setVisible("cmplx_DEC_New_CIFID",false);
		                        formObject.setVisible("DecisionHistory_Decision_Label1",false);
		                        formObject.setVisible("cmplx_DEC_VerificationRequired",false);
		                   //     formObject.setEnabled("cmplx_DEC_ScoreGrade",true);
		                        
		                        
		                        loadPicklist3();
		                        int rowsExposure = formObject.getLVWRowCount("cmplx_DEC_cmplx_gr_ExpoDetails");
		                        int rowsDeviation = formObject.getLVWRowCount("cmplx_DEC_cmplx_gr_DeviaitonDetails");
		                        if(rowsExposure!=0){
		                        formObject.clear("cmplx_DEC_cmplx_gr_ExpoDetails");
		                        }
		                        setExposureGridData();
		                        if(rowsDeviation!=0){
		                        formObject.clear("cmplx_DEC_cmplx_gr_DeviaitonDetails");
		                        }
		                        setDeviationGridData();
		                      //Code Change By aman to save Highest Delegation Auth
		                		String sQuery = "select distinct(Delegation_Authorithy) from ng_rlos_IGR_Eligibility_CardProduct where Child_Wi ='"+formObject.getWFWorkitemName()+"'";
		                		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		                		SKLogger_CC.writeLog("sQuery list size"+sQuery, "");
		                		SKLogger_CC.writeLog("cmplx_DEC_LoanApprovalAuth list size"+OutputXML.size(), "value is "+OutputXML.get(0).get(0));
		                		SKLogger_CC.writeLog("cmplx_DEC_LoanApprovalAuth list size"+OutputXML.size(), "value is ");
		                		if(!OutputXML.isEmpty()){
		                			String HighDel=OutputXML.get(0).get(0);
		                			if(OutputXML.get(0).get(0)!=null && !OutputXML.get(0).get(0).isEmpty() &&  !OutputXML.get(0).get(0).equals("") && !OutputXML.get(0).get(0).equalsIgnoreCase("null") ){
		                				{
		                					SKLogger_CC.writeLog("Inside the if dectech"+HighDel, "value is ");	
		                					formObject.setNGValue("cmplx_DEC_LoanApprovalAuth", HighDel);
		                				}
		                			}
		                		}	
								}//Code Change By aman to save Highest Delegation Auth
		                        catch(Exception ex){
		                        	SKLogger_CC.writeLog("Exception in Decision history load",printException(ex));
		                        }
						 }//loadPicklist1();
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
						// ++ below code not commented at offshore - 06-10-2017
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
						
						
						if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Button1")){
							formObject.saveFragment("DecisionHistory");
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
						// added by abhishek as per CC FSD 
						if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Add")){
							//++Below code added by  nikhil 10/10/17
							formObject.setNGValue("NotepadDetails_winame",formObject.getWFWorkitemName());
							//-- Above code added by  nikhil 10/10/17
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
						// added by abhishek as per CC FSD
						if(pEvent.getSource().getName().equalsIgnoreCase("cmplx_NotepadDetails_cmplx_notegrid")){
							Notepad_grid();
						} 
						if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Add1")){
							// ++ below code not commented at offshore - 06-10-2017
							formObject.setNGValue("NotepadDetails_winame",formObject.getWFWorkitemName());
							formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_Telloggrid");
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Clear")){
							formObject.ExecuteExternalCommand("NGClear", "cmplx_NotepadDetails_cmplx_Telloggrid");
						}
						//ended by yash on 24/8/2017
						
						if(pEvent.getSource().getName().equalsIgnoreCase("SmartCheck1_Save")){
							formObject.saveFragment("Smart_Check");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("SmartCheck1_Modify")){
							formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_SmartCheck1_SmartChkGrid_FCU");
						}
						
						if(pEvent.getSource().getName().equalsIgnoreCase("SmartCheck1_Add")){
							//++Below code added by  nikhil 10/10/17
							formObject.setNGValue("SmartCheck1_WiName",formObject.getWFWorkitemName());
							//-- Above code added by  nikhil 10/10/17

							formObject.ExecuteExternalCommand("NGAddRow", "cmplx_SmartCheck1_SmartChkGrid_FCU");
						}
						if(pEvent.getSource().getName().equalsIgnoreCase("SmartCheck1_Delete")){
							formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_SmartCheck1_SmartChkGrid_FCU");
						}
				
					
					 case VALUE_CHANGED:
							SKLogger_CC.writeLog(" In PL_Initiation eventDispatched()", "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
							 if (pEvent.getSource().getName().equalsIgnoreCase("Decision_Combo2")) {
								 if(formObject.getWFActivityName().equalsIgnoreCase("CAD_Analyst1"))	
								 {
									 formObject.setNGValue("CAD_dec", formObject.getNGValue("Decision_Combo2"));
									SKLogger_CC.writeLog(" In PL_Initiation VALChanged---New Value of CAD_dec is: ", formObject.getNGValue("Decision_Combo2"));

								 }
								 
								 else{
									 
									formObject.setNGValue("decision", formObject.getNGValue("Decision_Combo2"));
									SKLogger_CC.writeLog(" In PL_Initiation VALChanged---New Value of decision is: ", formObject.getNGValue("Decision_Combo2"));
								 	  }
							 	}
							 //added by yash on 30/8/2017
							// Code added to Load targetSegmentCode on basis of code
								if (pEvent.getSource().getName().equalsIgnoreCase("ApplicationCategory")){
									SKLogger_CC.writeLog("RLOS val change ", "Value of dob is:"+formObject.getNGValue("ApplicationCategory"));
									String subproduct = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2);
									String appCategory=formObject.getNGValue("ApplicationCategory");
									SKLogger_CC.writeLog("RLOS val change ", "Value of subproduct is:"+formObject.getNGValue("subproduct"));
									if(appCategory!=null &&  appCategory.equalsIgnoreCase("BAU")){
										LoadPickList("TargetSegmentCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and BAU='Y' and (product = 'CC' or product='B') order by code");
									}
									else if(appCategory!=null &&  appCategory.equalsIgnoreCase("Surrogate")){
										LoadPickList("TargetSegmentCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and Surrogate='Y' and (product = 'CC' or product='B') order by code");
									}
									else{
									LoadPickList("TargetSegmentCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and (product = 'CC' or product='B') order by code");	
									}
								}
								// Code added to Load targetSegmentCode on basis of code
							 
							 if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_notedesc")){
								 String notepad_desc = formObject.getNGValue("NotepadDetails_notedesc");
								 //LoadPickList("NotepadDetails_notecode", "select '--Select--' union select convert(varchar, description) from ng_master_notedescription with (nolock)  where Description=q'["+notepad_desc+"]'","NotepadDetails_notecode");
								 String sQuery = "select code from ng_master_notedescription where Description='" +  notepad_desc + "'";
								 SKLogger_CC.writeLog(" query is ",sQuery);
								 List<List<String>> recordList = formObject.getDataFromDataSource(sQuery);
								 if(recordList.get(0).get(0)!= null && recordList.get(0)!=null && !recordList.get(0).get(0).equalsIgnoreCase("") && recordList!=null)
								 {
									 formObject.setNGValue("NotepadDetails_notecode",recordList.get(0).get(0));
									 // ++ below code not commented at offshore - 06-10-2017
									formObject.setNGValue("NotepadDetails_workstep",recordList.get(0).get(1),false);
								 }
								 
							
								 
							 }
					default: break;
					
				}
	}


	public void setDeviationGridData() {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String wi_name = formObject.getWFWorkitemName();
		//String query = "select Deviation_Code_Refer from ng_rlos_IGR_Eligibility_CardProduct where Deviation_Code_Refer is not null and Child_Wi like '"+wi_name+"'";
		String query="select case when Decision='Declined' then Declined_Reason  when Decision='Refer' then Deviation_Code_Refer when Decision='Approve' then Deviation_Code_Refer end as Deviation,Decision from ng_rlos_IGR_Eligibility_CardProduct where  Child_Wi ='"+wi_name+"'";
		List<List<String>> record = formObject.getDataFromDataSource(query);
		SKLogger_CC.writeLog("CC","Deviation query formulated is: "+query);
		if(record !=null && record.get(0)!=null && record.size()>0){
			for(List<String> row:record){
				List<String> temp = new ArrayList<String>();
				int separatorIndex = row.get(0).lastIndexOf("-");
				temp.add(row.get(0).substring(0, separatorIndex));
				SKLogger_CC.writeLog("CC","Description is: "+row.get(0).substring(0, separatorIndex));
				temp.add(row.get(0).substring(separatorIndex+1, row.get(0).length()));
				SKLogger_CC.writeLog("CC","Code is: "+row.get(0).substring(separatorIndex+1, row.get(0).length()));
				temp.add(formObject.getWFWorkitemName());
				formObject.addItemFromList("cmplx_DEC_cmplx_gr_DeviaitonDetails", temp);//
			}
		}
	}


	public void setExposureGridData() {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String wi_name = formObject.getWFWorkitemName();
		String query = "select LoanType as Scheme,Liability_type,AgreementId as Agreement,OutstandingAmt as OutBal,PaymentsAmt as EMI,Consider_For_Obligations  from ng_RLOS_CUSTEXPOSE_LoanDetails where Child_Wi = '"+wi_name+"' union select CardType as Scheme,Liability_type,CardEmbossNum as Agreement,OutstandingAmt as OutBal,PaymentsAmount as EMI,Consider_For_Obligations  from ng_RLOS_CUSTEXPOSE_CardDetails where Child_Wi = '"+wi_name+"' union select LoanType as Scheme,Liability_type,AgreementId as Agreement,OutstandingAmt as OutBal,PaymentsAmt as EMI,Consider_For_Obligations from ng_rlos_cust_extexpo_LoanDetails where Child_Wi = '"+wi_name+"' and LoanStat!='Pipeline'  union select CardType as Scheme,Liability_type,CardEmbossNum as Agreement,CurrentBalance as OutBal,PaymentsAmount as EMI,Consider_For_Obligations from ng_rlos_cust_extexpo_CardDetails where Child_Wi like '"+wi_name+"' and CardStatus!='Pipeline'";
		SKLogger_CC.writeLog("CC","Query for exposure grid is: "+query);
		List<List<String>> record = formObject.getDataFromDataSource(query);
		if(record !=null && record.size()>0 && record.get(0)!=null){
			for(List<String> row:record){
				SKLogger_CC.writeLog("CC","CFO received is: "+row.get(5));
				if( row.get(5) == null ||row.get(5).equalsIgnoreCase("null")|| row.get(5).equalsIgnoreCase("true") || row.get(5).equalsIgnoreCase(null) ||row.get(5).equalsIgnoreCase("")){
					SKLogger_CC.writeLog("CC","row.get(5) is: "+row.get(5));
					row.set(5, "Yes");
					SKLogger_CC.writeLog("CC","row.get(5) is: "+row.get(5));
				}
				else if(row.get(5).equalsIgnoreCase("false")){
					SKLogger_CC.writeLog("CC","row.get(5) is: "+row.get(5));
					row.set(5, "No");
					SKLogger_CC.writeLog("CC","row.get(5) is: "+row.get(5));
				}
				row.add(formObject.getWFWorkitemName());
				formObject.addItemFromList("cmplx_DEC_cmplx_gr_ExpoDetails", row);
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


	public void submitFormCompleted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		SKLogger_CC.writeLog("CC","inside submitformcompleted: ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		 String Decision= formObject.getNGValue("cmplx_DEC_Decision");
		 if (Decision.equalsIgnoreCase("Approve")){
			 List<String> objInput=new ArrayList<String>();
			 //disha FSD cad delegation procedure changes
			 objInput.add("Text:"+formObject.getWFWorkitemName());
			 objInput.add("Text:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1));
			 objInput.add("Text:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2));
			 objInput.add("Text:"+formObject.getNGValue("cmplx_DEC_HighDeligatinAuth"));
			 SKLogger_CC.writeLog("PL","objInput args are: "+objInput.get(0)+objInput.get(1)+objInput.get(2)+objInput.get(3));
		   	 formObject.getDataFromStoredProcedure("ng_rlos_CADLevels", objInput);
  		}
	}


	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
		
		if(formObject.getNGValue("cmplx_DEC_Decision").equalsIgnoreCase("Refer to Smart CPV"))
		{
			saveSmartCheckGrid();
		}
		saveIndecisionGrid();
	}

}

