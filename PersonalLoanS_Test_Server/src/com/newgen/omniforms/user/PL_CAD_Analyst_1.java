/*------------------------------------------------------------------------------------------------------


Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PL_CAD_Analyst_1.java
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import javax.faces.validator.ValidatorException;

import org.apache.log4j.Logger;

import com.newgen.custom.Common_Utils;
import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;

public class PL_CAD_Analyst_1 extends PLCommon implements FormListener
{
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	Logger mLogger=PersonalLoanS.mLogger;

	public void formLoaded(FormEvent pEvent)
	{
		FormConfig objConfig = FormContext.getCurrentInstance().getFormConfig();
        objConfig.getM_objConfigMap().put("PartialSave", "true");

	}


	public void formPopulated(FormEvent pEvent) 
	{
		try{
			new PersonalLoanSCommonCode().setFormHeader(pEvent);
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			formObject.setNGValue("Mandatory_Frames", NGFUserResourceMgr_PL.getGlobalVar("Cad1_Frame_Name"));
			String cc_waiv_flag = formObject.getNGValue("is_cc_waiver_require");
			if(cc_waiv_flag.equalsIgnoreCase("N")){
			formObject.setVisible("Card_Details", true);
			}
		}catch(Exception e)
		{
			PersonalLoanS.mLogger.info( "Exception:"+e.getMessage());
			printException(e);
		}
	}
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Load fragment      
	 
	***********************************************************************************  */

	public void fragment_loaded(ComponentEvent pEvent){
		PersonalLoanS.mLogger.info("EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		FormReference formObject= FormContext.getCurrentInstance().getFormReference();
		Common_Utils common=new Common_Utils(mLogger);

		if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			
			loadPicklistCustomer();
			formObject.setLocked("Customer_Frame1",true);
			//change by saurabh on 7th Dec
			formObject.setLocked("cmplx_Customer_NEP",true);
			
			
			formObject.setLocked("cmplx_Customer_DOb", true);
			formObject.setLocked("cmplx_Customer_IdIssueDate", true);
			formObject.setLocked("cmplx_Customer_EmirateIDExpiry", true);
			formObject.setLocked("cmplx_Customer_VisaIssueDate", true);
			formObject.setLocked("cmplx_Customer_VIsaExpiry", true);
			formObject.setLocked("cmplx_Customer_PassportIssueDate", true);
			formObject.setLocked("cmplx_Customer_PassPortExpiry", true);
			if(!"".equals(formObject.getNGValue("cmplx_Customer_NEP"))&& !"--Select--".equals(formObject.getNGValue("cmplx_Customer_NEP")) && formObject.getNGValue("cmplx_Customer_NEP")!=null){
				formObject.setVisible("cmplx_Customer_EIDARegNo",true);
				formObject.setVisible("Customer_Label56",true);
				//formObject.setLocked("cmplx_Customer_EIDARegNo",true);
			}
			else {
				formObject.setVisible("Customer_Label56",false);
				formObject.setVisible("cmplx_Customer_EIDARegNo",false);
			}
			
			formObject.setEnabled("Customer_save",false);
		}

		else if ("CAD_Decision".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("cmplx_CADDecision_ScoreGrade",true);
			formObject.setLocked("cmplx_CADDecision_LoanApprovalAuthority",true);

			String query1="Select Product_Type,AgreementId,TotalOutstandingAmt,NextInstallmentAmt,Consider_For_Obligations,wi_name from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where wi_name='"+formObject.getWFWorkitemName()+"'";
			String query2="Select Deviation_Code_Refer,wi_name from ng_rlos_IGR_ProductEligibility with(nolock) where wi_name='"+formObject.getWFWorkitemName()+"'";
			

			List<List<String>> mylist=formObject.getNGDataFromDataCache(query1);
			PersonalLoanS.mLogger.info("Inside CAD_Decision: mylist is:"+mylist.toString());
			for(List<String> a:mylist)
				formObject.addItemFromList("cmplx_CADDecision_cmplx_gr_exposureDetails",a);

			List<List<String>> mylist1=formObject.getNGDataFromDataCache(query2);
			PersonalLoanS.mLogger.info("Inside CAD_Decision: mylist1 is:"+mylist.toString());
			for(List<String> a:mylist1)
				formObject.addItemFromList("cmplx_CADDecision_cmplx_gr_DeviationDetails",a);

			formObject.setNGValue("cmplx_CADDecision_ScoreGrade",formObject.getNGValue("Select Score_Grade,wi_name from ng_rlos_IGR_ProductEligibility with(nolock) where wi_name='"+formObject.getWFWorkitemName()+"'"));
			formObject.setNGValue("cmplx_CADDecision_LoanApprovalAuthority",formObject.getNGValue("Select Delegation_Authorithy,wi_name from ng_rlos_IGR_ProductEligibility where wi_name='"+formObject.getWFWorkitemName()+"'"));
		}
		else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Product_Frame1",true);
			LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_ApplicationType with(nolock) where SubProdFlag = '"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2)+"'");
			LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) order by SCHEMEID");
			LoadPickList("subProd", "select '--Select--' as description,'' as code union select description,code from ng_MASTER_SubProduct_PL with (nolock) order by code");
			

			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct with(nolock)");
		}

		else if ("GuarantorDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("GuarantorDetails_Frame1",true);
			formObject.setEnabled("GuarantorDetails_Frame1",false);
		}

		else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			String EmpType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5);// Changed because Emptype comes at 5
			PersonalLoanS.mLogger.info( "Emp Type Value is:"+EmpType);
			formObject.setLocked("cmplx_IncomeDetails_Totavgother",true);
			if("Salaried".equalsIgnoreCase(EmpType)||"Pensioner".equalsIgnoreCase(EmpType)|| NGFUserResourceMgr_PL.getGlobalVar("PL_SalariedPensioner").equalsIgnoreCase(EmpType))
			{
				formObject.setVisible("IncomeDetails_Frame3", false);
				formObject.setHeight("Incomedetails", 630);
				formObject.setHeight("IncomeDetails_Frame1", 605);  
			}
			else if(NGFUserResourceMgr_PL.getGlobalVar("PL_SelfEmployed").equalsIgnoreCase(EmpType))
			{                                                                                                              
				formObject.setVisible("IncomeDetails_Frame2", false);
				formObject.setTop("IncomeDetails_Frame3",40);
				formObject.setHeight("Incomedetails", 300);
				formObject.setHeight("IncomeDetails_Frame1", 280);
			}

			formObject.setVisible("IncomeDetails_Label13",false);
			formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsRakbankStat",false);
			formObject.setVisible("IncomeDetails_Label15",true); 
			formObject.setVisible("cmplx_IncomeDetails_Totavgother",true);
			formObject.setVisible("IncomeDetails_Label16",true);
			formObject.setVisible("cmplx_IncomeDetails_compaccAmt",true);
			// disha FSD	
			formObject.setEnabled("cmplx_IncomeDetails_grossSal",true);
			LoadPickList("cmplx_IncomeDetails_StatementCycle", "select '--Select--' union select convert(varchar, description) from NG_MASTER_StatementCycle with (nolock)");
			
			formObject.setLocked("cmplx_IncomeDetails_totSal",true);
			formObject.setLocked("cmplx_IncomeDetails_AvgNetSal",true);

			if(NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")))
			{
				formObject.setLocked("cmplx_IncomeDetails_DurationOfBanking",true);
				formObject.setLocked("cmplx_IncomeDetails_NoOfMonthsOtherbankStat",true);
			}
			formObject.setVisible("IncomeDetails_Frame3",false);
			//added by akshay on 29/12/17
			int framestate2=formObject.getNGFrameState("EligibilityAndProductInformation");
			if(framestate2 != 0){
			
			formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligibilityProdInfo");
			}
			//formObject.setNGValue("cmplx_IncomeDetails_totSal", formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalTai"));--commented by akshay
		
		}

		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
			String App_Type=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 4);
			formObject.setNGValue("cmplx_Liability_New_overrideIntLiab",true);
			formObject.setLocked("cmplx_Liability_New_AECBconsentAvail",true);
			LoadPickList("Liability_New_worststatuslast24", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from ng_master_Aecb_Codes with (nolock) order by code");
			 LoadPickList("ExtLiability_contractType", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_contract_type with (nolock)  where isactive='Y' order by code");

			formObject.setLocked("ExtLiability_takeoverAMount",true);//Arun (12/08/17)
			formObject.setLocked("ExtLiability_QCAmt",true);//Arun (12/08/17)
			formObject.setLocked("ExtLiability_QCEMI",true);//Arun (12/08/17)
			formObject.setLocked("cmplx_Liability_New_noofpaidinstallments", true);
			formObject.setLocked("cmplx_Liability_New_DBR",true);
			formObject.setLocked("cmplx_Liability_New_TAI",true);
			formObject.setLocked("cmplx_Liability_New_DBRNet",true);
			formObject.setLocked("cmplx_Liability_New_AggrExposure",true);
			formObject.setVisible("ExtLiability_Label15",true);
			formObject.setVisible("cmplx_Liability_New_AggrExposure",true);
			//commented by saurabh on 4th Dec.
			formObject.setVisible("Liability_New_Label1",false);//Arun (16/08/17)
				formObject.setVisible("Liability_New_MOB",false);//Arun (16/08/17)
				formObject.setVisible("Liability_New_Label3",false);//Arun (16/08/17)
				formObject.setVisible("Liability_New_Utilization",false);//Arun (16/08/17)
				formObject.setVisible("Liability_New_Label5",false);//Arun (16/08/17)
				formObject.setVisible("Liability_New_Outstanding",false);//Arun (16/08/17)
				formObject.setVisible("Liability_New_delinin3",false);//Arun (16/08/17)
				formObject.setVisible("Liability_New_DPD30inlast6",false);//Arun (16/08/17)
				formObject.setVisible("Liability_New_DPD30inlast18",false);//Arun (16/08/17)
				formObject.setVisible("Liability_New_Label2",false);//Arun (16/08/17)
				formObject.setVisible("Liability_New_writeoff",false);//Arun (16/08/17)
				formObject.setVisible("Liability_New_Label4",false);//Arun (16/08/17)
				formObject.setVisible("Liability_New_worststatuslast24",false);//Arun (16/08/17)
				formObject.setVisible("Liability_New_Label8",false);
				formObject.setVisible("Liability_New_rejAppsinlast3months",false);
				LoadPickList("Liability_New_worststatuslast24", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from ng_master_Aecb_Codes with (nolock) order by code");
	
			formObject.setNGValue("cmplx_Liability_New_overrideIntLiab",true);//Arun (27/09/17)
			formObject.setNGLVWRowHeight("Liability_New_Label8", 152);
			formObject.setNGLVWRowHeight("Liability_New_Text3", 168);										
			if (App_Type != "RESCH"){
				formObject.setVisible("Liability_New_Label6", false);
				formObject.setVisible("cmplx_Liability_New_noofpaidinstallments", false);
			}
			//added by saurabh on 11th Jan
			if(formObject.getNGValue("cmplx_Liability_New_overrideIntLiab").equalsIgnoreCase("true") && !formObject.isVisible("Liability_New_Overwrite")){
				
				formObject.setVisible("Liability_New_Overwrite", true);
			}
		}

		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			if(!"CAC".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode"))){
				formObject.setVisible("EMploymentDetails_Label30", false);
				formObject.setVisible("cmplx_EmploymentDetails_OtherBankCAC", false);
			}
			else{	
				if(!"S".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg")))
				{
					formObject.setVisible("cmplx_EmploymentDetails_IndusSeg",false);
					formObject.setVisible("EMploymentDetails_Label59",false);
				}
			}
			if("".equals(formObject.getNGValue("cmplx_Customer_NEP")) || "--Select--".equals(formObject.getNGValue("cmplx_Customer_NEP"))){
				formObject.setLocked("cmplx_EmploymentDetails_NepType",true);
			}
			if(formObject.getNGValue("cmplx_EmploymentDetails_IncInCC")=="true" || formObject.getNGValue("cmplx_EmploymentDetails_IncInPL")=="true")
			{
				formObject.setLocked("cmplx_EmploymentDetails_EmpStatusPL",true);
				formObject.setLocked("cmplx_EmploymentDetails_EmpStatusCC",true);
				formObject.setLocked("cmplx_EmploymentDetails_CurrEmployer",true);
				formObject.setLocked("cmplx_EmploymentDetails_EmpIndusSector",true);
				formObject.setLocked("cmplx_EmploymentDetails_Indus_Macro",true);
				formObject.setLocked("cmplx_EmploymentDetails_Indus_Micro",true);
				formObject.setLocked("cmplx_EmploymentDetails_categnational",true);
				formObject.setLocked("cmplx_EmploymentDetails_categexpat",true);
				formObject.setLocked("cmplx_EmploymentDetails_ownername",true);
				formObject.setLocked("cmplx_EmploymentDetails_NOB",true);
				formObject.setEnabled("cmplx_EmploymentDetails_LOSPrevious",true);//--changed by akshay for proc 12280
				formObject.setLocked("cmplx_EmploymentDetails_accpvded",true);
				formObject.setLocked("cmplx_EmploymentDetails_authsigname",true);
				formObject.setLocked("cmplx_EmploymentDetails_highdelinq",true);
				formObject.setLocked("cmplx_EmploymentDetails_dateinPL",true);
				formObject.setLocked("cmplx_EmploymentDetails_dateinCC",true);
				formObject.setLocked("cmplx_EmploymentDetails_remarks",true);
				formObject.setLocked("cmplx_EmploymentDetails_Remarks_PL",true);

				formObject.setLocked("cmplx_EmploymentDetails_EmpStatus",false);
				formObject.setLocked("cmplx_EmploymentDetails_Emp_Type",false);
				formObject.setLocked("cmplx_EmploymentDetails_Designation",false);
				formObject.setLocked("Designation_button",false);
				formObject.setLocked("cmplx_EmploymentDetails_DesigVisa",false);
				formObject.setLocked("DesignationAsPerVisa_button",false);
				formObject.setLocked("cmplx_EmploymentDetails_JobConfirmed",false);
				
				formObject.setLocked("cmplx_EmploymentDetails_LOS",false);								

				formObject.setLocked("cmplx_EmploymentDetails_EmpContractType",false);
				formObject.setLocked("cmplx_EmploymentDetails_VisaSponser",false);

				formObject.setLocked("cmplx_EmploymentDetails_Freezone",false);
				//formObject.setLocked("cmplx_EmploymentDetails_FreezoneName",false);
				formObject.setLocked("cmplx_EmploymentDetails_IndusSeg",false);
				formObject.setLocked("cmplx_EmploymentDetails_Kompass",false);
				formObject.setLocked("cmplx_EmploymentDetails_StaffID",false);
				formObject.setLocked("cmplx_EmploymentDetails_Dept",false);
				formObject.setLocked("cmplx_EmploymentDetails_CntrctExpDate",false);
			}
			if((formObject.getNGValue("cmplx_EmploymentDetails_IncInCC")=="false") && (formObject.getNGValue("cmplx_EmploymentDetails_IncInPL")=="false"))
			{
				// disha FSD
				formObject.setNGValue("cmplx_EmploymentDetails_EmpStatusPL","CNOAL");
				formObject.setNGValue("cmplx_EmploymentDetails_EmpStatusCC","CNOAL ( disabled) /Awaiting FVR");
				formObject.setLocked("cmplx_EmploymentDetails_categnational",true);
				formObject.setLocked("cmplx_EmploymentDetails_categexpat",true);
				formObject.setLocked("cmplx_EmploymentDetails_ownername",true);
				formObject.setLocked("cmplx_EmploymentDetails_NOB",true);
				formObject.setLocked("cmplx_EmploymentDetails_accpvded",true);
				formObject.setLocked("cmplx_EmploymentDetails_authsigname",true);
				formObject.setLocked("cmplx_EmploymentDetails_highdelinq",true);
				formObject.setLocked("cmplx_EmploymentDetails_dateinPL",true);
				formObject.setLocked("cmplx_EmploymentDetails_dateinCC",true);
				formObject.setLocked("cmplx_EmploymentDetails_remarks",true);
				formObject.setLocked("cmplx_EmploymentDetails_Remarks_PL",true);

				formObject.setLocked("cmplx_EmploymentDetails_EmpIndusSector",false);
				formObject.setLocked("cmplx_EmploymentDetails_Indus_Macro",false);
				formObject.setLocked("cmplx_EmploymentDetails_Indus_Micro",false);
				formObject.setLocked("cmplx_EmploymentDetails_EmpContractType",false);
				formObject.setLocked("cmplx_EmploymentDetails_Emp_Type",false);
				formObject.setLocked("cmplx_EmploymentDetails_Designation",false);
				formObject.setLocked("cmplx_EmploymentDetails_DesigVisa",false);
				formObject.setLocked("Designation_button",false);
				formObject.setLocked("DesignationAsPerVisa_button",false);
				formObject.setLocked("cmplx_EmploymentDetails_JobConfirmed",false);
				

				formObject.setLocked("cmplx_EmploymentDetails_LOS",false);
				formObject.setLocked("cmplx_EmploymentDetails_LOSPrevious",false);
				formObject.setLocked("cmplx_EmploymentDetails_EmirateOfWork",false);
				formObject.setLocked("cmplx_EmploymentDetails_HeadOfficeEmirate",false);
				formObject.setLocked("cmplx_EmploymentDetails_VisaSponser",false);
				formObject.setLocked("cmplx_EmploymentDetails_Freezone",false);
				//formObject.setLocked("cmplx_EmploymentDetails_FreezoneName",false);
				formObject.setLocked("cmplx_EmploymentDetails_IndusSeg",false);
				formObject.setLocked("cmplx_EmploymentDetails_Kompass",false);
				formObject.setLocked("cmplx_EmploymentDetails_StaffID",false);
				formObject.setLocked("cmplx_EmploymentDetails_Dept",false);
				formObject.setLocked("cmplx_EmploymentDetails_CntrctExpDate",false);
			}
			//below code added by nikhil 1/3/18
			 if(!NGFUserResourceMgr_PL.getGlobalVar("PL_RESC").equalsIgnoreCase(formObject.getNGValue("Application_Type")) && !NGFUserResourceMgr_PL.getGlobalVar("PL_RESN").equalsIgnoreCase(formObject.getNGValue("Application_Type")) && !NGFUserResourceMgr_PL.getGlobalVar("PL_RESR").equalsIgnoreCase(formObject.getNGValue("Application_Type"))){
			
				formObject.setVisible("EMploymentDetails_Label36",false);
				formObject.setVisible("cmplx_EmploymentDetails_channelcode",false);
			}
			 
			 formObject.setVisible("EMploymentDetails_Label31", true);
			 formObject.setVisible("cmplx_EmploymentDetails_Field_visit_done", true);
			 formObject.setVisible("cmplx_EmploymentDetails_empmovemnt", true);
			 formObject.setVisible("EMploymentDetails_Label15", true);
			 if(!"S".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg")))
				{
					formObject.setVisible("cmplx_EmploymentDetails_IndusSeg",false);
					formObject.setVisible("EMploymentDetails_Label59",false);
				}
			 if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_Freezone")))
			 {
				 formObject.setLocked("cmplx_EmploymentDetails_FreezoneName", false);
			 }
			 else
			 {
				 formObject.setLocked("cmplx_EmploymentDetails_FreezoneName", true);
			 }
			 formObject.setVisible("cmplx_EmploymentDetails_Field_visit_done",true);
			 formObject.setVisible("EMploymentDetails_Label32",true);
			 formObject.setVisible("EMploymentDetails_Label39",true);
			 formObject.setVisible("cmplx_EmploymentDetails_VisaSponser",true);
			 formObject.setLocked("cmplx_EmploymentDetails_LOSPrevious", false);
			 formObject.setVisible("EMploymentDetails_Label34",true);
			 formObject.setVisible("cmplx_EmploymentDetails_HeadOfficeEmirate",true);
			loadPicklist4();

		}

		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {

			
			formObject.setVisible("ELigibiltyAndProductInfo_Frame5",false);//
			formObject.setVisible("ELigibiltyAndProductInfo_Label23", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_FinalInterestRate", false);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalDBR",true);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalTAI",true);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalLimit",true);
			//added by akshay on 22/12/17 for fetching card details
			if(!formObject.isVisible("CardDetails_Frame1")){
				fetch_CardDetails_frag(formObject);
				formObject.setTop("Card_Details", formObject.getTop("ReferenceDetails")+50);
				formObject.setTop("FATCA", formObject.getTop("Card_Details")+formObject.getHeight("Card_Details")+30);
				formObject.setTop("KYC", formObject.getTop("FATCA")+30);
				formObject.setTop("OECD", formObject.getTop("KYC")+30);

			}
			
			loadEligibilityData();

		}

		else if ("LoanDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist_LoanDetails();
			loanvalidate();//added by akshay for proc 8460
			formObject.setLocked("LoanDetails_Frame1",true);
			//below code added by nikhil 18/1/18
			formObject.setEnabled("cmplx_LoanDetails_fdisbdate", false);
			formObject.setEnabled("cmplx_LoanDetails_frepdate", false);
			formObject.setEnabled("cmplx_LoanDetails_maturitydate", false);
			//below code added 12/3 proc-6149
			//PROC-7659 AMan 
			formObject.setEnabled("cmplx_LoanDetails_paidon", false);
			formObject.setEnabled("cmplx_LoanDetails_trandate", false);
			formObject.setEnabled("cmplx_LoanDetails_chqdat", false);
			//PROC-7659 AMan
			//PROC-7658 AMan 
			formObject.setEnabled("LoanDetails_duedate", false);
			formObject.setEnabled("LoanDetails_disbdate", false);
			formObject.setEnabled("LoanDetails_payreldate", false);
			//PROC-7658 AMan
			
			formObject.setLocked("LoanDetails_Button1", false);
			formObject.setNGValue("cmplx_LoanDetails_trandate",common.Convert_dateFormat("", "", "dd/MM/yyyy"));//added by akshay on 5/4/18 for proc 6424

			
			
		}

		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist_Address();
			formObject.setLocked("AddressDetails_Frame1",true);
		}

		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			
			formObject.setLocked("AltContactDetails_Frame1",true);
			LoadpicklistAltcontactDetails();
		}

		else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadpicklistCardDetails();
			formObject.setLocked("CardDetails_Frame1",true);
		}

		else if ("ReferenceDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ReferenceDetails_Frame1",true);
			LoadPickList("ReferenceDetails_ref_Relationship", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Relationship with (nolock) order by code");
			
		}

		else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("SupplementCardDetails_Frame1",true);
			formObject.setLocked("SupplementCardDetails_DOB",true);
			formObject.setLocked("SupplementCardDetails_idIssueDate",true);
			formObject.setLocked("SupplementCardDetails_VisaIssueDate",true);
			formObject.setLocked("SupplementCardDetails_PassportIssueDate",true);
			formObject.setLocked("SupplementCardDetails_VisaExpiry",true);
			formObject.setLocked("SupplementCardDetails_EmiratesIDExpiry",true);
			formObject.setLocked("SupplementCardDetails_PassportExpiry",true);
			loadPicklist_suppCard();
		}

		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("FATCA_Frame6",true);
			formObject.setLocked("cmplx_FATCA_SignedDate",true);
			formObject.setLocked("cmplx_FATCA_ExpiryDate",true);
			Loadpicklistfatca();
		}

		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("KYC_Frame7",true);
			loadPicklist_KYC();
		}

		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPickListOECD();
			formObject.setLocked("OECD_Frame8",true);
		}

		else if ("PartMatch".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("PartMatch_Frame1",true);
			formObject.setLocked("PartMatch_Dob",true);
			//LoadPickList("PartMatch_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
		}

		else if ("FinacleCRMIncident".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("FinacleCRMIncident_Frame1",true);
		}

		else if ("FinacleCRMCustInfo".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("FinacleCRMCustInfo_Frame1",true);
		}

		else if ("FinacleCore".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			//empty else if
			//LoadPickList("FinacleCore_cheqtype", "select '--Select--' as description,'' as code union select convert(varchar, description),Code from ng_MASTER_Cheque_Type with (nolock) order by code");
		}

		else if ("MOL1".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("MOL1_Frame1",true);
			formObject.setLocked("cmplx_MOL_molexp", true);
			formObject.setLocked("cmplx_MOL_molissue", true);
			formObject.setLocked("cmplx_MOL_ctrctstart", true);
			formObject.setLocked("cmplx_MOL_ctrctend", true);
		}

		else if ("WorldCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("WorldCheck1_Frame1",true);
			//below code added by nikhil 
			formObject.setLocked("WorldCheck1_Dob", true);
			formObject.setLocked("WorldCheck1_entdate", true);
			formObject.setLocked("WorldCheck1_upddate", true);
		}

		else if ("RejectEnq".equalsIgnoreCase(pEvent.getSource().getName())) {

			//empty else if
		}

		else if ("SalaryEnq".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("SalaryEnq_Frame1",true);
		}

		else if ("CustDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("CustDetailVerification_Frame1",true);
			formObject.setLocked("cmplx_CustDetailVerification_dob_upd",true);
		}

		else if ("BussinessVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("BussinessVerification_Frame1",true);
		}

		else if ("HomeCountryVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("HomeCountryVerification_Frame1",true);
		}

		else if ("ResidenceVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ResidenceVerification_Frame1",true);
		}

		else if ("GuarantorVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("GuarantorVerification_Frame1",true);
		}

		else if ("ReferenceDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ReferenceDetailVerification_Frame1",true);
		}

		else if ("OfficeandMobileVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("OfficeandMobileVerification_Frame1",true);
			formObject.setLocked("cmplx_OffVerification_doj_upd",true);
			LoadPickList("cmplx_OffVerification_desig_upd", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Designation with (nolock) where isActive='Y' order by Code");
		}

		else if ("LoanandCard".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("LoanandCard_Frame1",true);
		}


		else if ("ReferHistory".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setLocked("ReferHistory_Frame1",true);
		}
		//disha FSD
		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			notepad_load();
			 notepad_withoutTelLog();
			}
		//disha FSD
		else if ("RiskRating".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("RiskRating_Frame1",true);
			loadPicklistRiskRating(); //Arun (10/10)
		}

		else if ("SmartCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setVisible("SmartCheck1_Label2",false);
			formObject.setVisible("SmartCheck1_CPVrem",false);
			formObject.setVisible("SmartCheck1_Label4",false);
			formObject.setVisible("SmartCheck1_FCUrem",false);
			formObject.setLocked("SmartCheck1_Modify",true);
		}
		//disha FSD
		else if ("ExternalBlackList".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setLocked("ExternalBlackList_Frame1",true);
		}

		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadInDecGrid();
			//for decision fragment made changes 8th dec 2017
			PersonalLoanS.mLogger.info("***********Inside decision history");
			fragment_ALign("Decision_Label1,cmplx_Decision_VERIFICATIONREQUIRED#DecisionHistory_Button4#DecisionHistory_CifLock#DecisionHistory_CifUnlock#\n#DecisionHistory_LabelNewStrength,DecisionHistory_NewStrength#DecisionHistory_newStrengthAdd#DecisionHistory_Label3,cmplx_Decision_strength#\n#DecisionHistory_LabelNewWeakness,DecisionHistory_NewWeakness#DecisionHistory_newWeaknessAdd#DecisionHistory_Label4,cmplx_Decision_weakness#\n#DecisionHistory_Label13,cmplx_Decision_Deviationcode#DecisionHistory_Label14,cmplx_Decision_Dectech_decsion#DecisionHistory_Label15,cmplx_Decision_score_grade#\n#Decision_Label3,cmplx_Decision_Decision#DecisionHistory_Label1,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_DecisionReasonCode#DecisionHistory_Label16,cmplx_Decision_Highest_delegauth#DecisionHistory_Button5#\n#cmplx_Decision_Manual_Deviation#DecisionHistory_Button6#DecisionHistory_Label18,cmplx_Decision_CADDecisiontray#Decision_Label4,cmplx_Decision_REMARKS#\n#Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
			formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
			formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
			formObject.setLocked("cmplx_Decision_strength",true);
			formObject.setLocked("cmplx_Decision_weakness",true);

			PersonalLoanS.mLogger.info("***********Inside after fragment alignment decision history");
			
			loadPicklist1();
			//added by saurabh for CAM report JIRA - 9844.
			int framestateLoanDet = formObject.getNGFrameState("LoanDetails");
			if(framestateLoanDet!=0){
			formObject.fetchFragment("LoanDetails", "LoanDetails", "q_Loan");
			expandLoanDetails();
			}
			LoadPickList("cmplx_Decision_CADDecisiontray", "select 'Select' as Refer_Credit  union select convert(varchar, Refer_Credit)  from NG_Master_ReferCredit with (nolock) order by Refer_Credit desc");
			//condition added by saurabh on 15th Oct.
			if(!NGFUserResourceMgr_PL.getGlobalVar("PL_Approve").equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision"))){

				formObject.setLocked("cmplx_Decision_CADDecisiontray",false);
			}
			//Common function for decision fragment textboxes and combo visibility
			if ("false".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Manual_Deviation"))){
				formObject.setLocked("cmplx_Decision_CADDecisiontray", true);
			}
			else {
				formObject.setLocked("cmplx_Decision_CADDecisiontray", false);
			}
			
		} 
		//below code added by nikhil 19/1/18
		 else if ("IncomingDoc".equalsIgnoreCase(pEvent.getSource().getName())) {
			 fetchIncomingDocRepeater();
		 }
		 

	}
	
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{

		//PersonalLoanS.mLogger.info("EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		switch(pEvent.getType())
		{	
		case FRAME_EXPANDED:
			//PersonalLoanS.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			new PersonalLoanSCommonCode().FrameExpandEvent(pEvent);						
			break;

		case FRAGMENT_LOADED:
			fragment_loaded(pEvent);
		case MOUSE_CLICKED:
			new PersonalLoanSCommonCode().mouse_Clicked(pEvent);
			break;

		case VALUE_CHANGED:
			new PersonalLoanSCommonCode().value_Change(pEvent);
			break;

		default: break;

		}
	}


	public void continueExecution(String arg0, HashMap<String, String> arg1) {
		//empty method

	}


	public void initialize() {
		//empty method

	}


	public void saveFormCompleted(FormEvent arg0) throws ValidatorException {
		//empty method
		CustomSaveForm();

	}


	public void saveFormStarted(FormEvent arg0) throws ValidatorException {
		
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setNGValue("Final_Limit", formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));//added by akshay for proc 10104
		formObject.setNGValue("LoanLabel", formObject.getNGValue("Final_Limit"));//added by akshay for proc 10104

		
	}


	public void submitFormCompleted(FormEvent pEvent) throws ValidatorException {

		PersonalLoanS.mLogger.info("Inside PL PROCESS submitFormCompleted()" ); 
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		List<String> objInput=new ArrayList<String> ();
		List<Object> objOutput=new ArrayList<Object>();
		//disha FSD cad delegation procedure changes
		try
		{
			CustomSaveForm();
			objInput.add("Text:"+formObject.getWFWorkitemName());
			objInput.add("Text:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1));
			objInput.add("Text:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2));
			objInput.add("Text:"+formObject.getNGValue("cmplx_Decision_Highest_delegauth"));
			objInput.add("Text:"+formObject.getNGValue("cmplx_Decision_Decision"));
			objOutput.add("Text");
			PersonalLoanS.mLogger.info("objInput args are: "+objInput.get(0)+objInput.get(1)+objInput.get(2)+objInput.get(3)+objInput.get(4));

			//  below code already exist - 10-10-2017
			// disha CAD deligation hierarchy should not work when decision selected if 'Refer to Credit'
			if(NGFUserResourceMgr_PL.getGlobalVar("PL_Approve").equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")))
			{  
				if ("".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_CADDecisiontray")) || "Select".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_CADDecisiontray"))){
						PersonalLoanS.mLogger.info("Before executing procedure ng_RLOS_CADLevels");
						objOutput= formObject.getDataFromStoredProcedure("ng_RLOS_CADLevels", objInput,objOutput);
						PersonalLoanS.mLogger.info("after executing procedure ng_RLOS_CADLevels: " + objOutput);
				}
			}
			else
			{
				PersonalLoanS.mLogger.info("value of cmplx_Decision_CADDecisiontray" + formObject.getNGValue("cmplx_Decision_CADDecisiontray"));
				formObject.setNGValue("CADNext",formObject.getNGValue("cmplx_Decision_CADDecisiontray"));
				//formObject.setNGValue("VAR_STR10",formObject.getNGValue("cmplx_Decision_CADDecisiontray"));
				//PersonalLoanS.mLogger.info("value of q_CADNext" + formObject.getNGValue("q_CADNext"));
			}
			objInput.clear();
			objInput.add("Text:"+formObject.getWFWorkitemName());
			objInput.add("Text:"+"Waiting for Approval");
			objOutput.clear();
			objOutput.add("Text");
			objOutput= formObject.getDataFromStoredProcedure("ng_EFMS_InsertData", objInput,objOutput);
			PersonalLoanS.mLogger.info("After ng_EFMS_InsertData: objOutput is "+objOutput);
		}
		catch(Exception Ex)
		{
			PersonalLoanS.mLogger.info("Exception in submit form completed!"+Ex.getStackTrace());
		}



	}
	



	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PersonalLoanS.mLogger.info("Inside PL PROCESS submitFormStarted()" ); 
		try{
		formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
		formObject.setNGValue("CAD_ANALYST1_DECISION", formObject.getNGValue("cmplx_Decision_Decision"));
		formObject.setNGValue("Highest_Delegation_CAD", formObject.getNGValue("cmplx_Decision_Highest_delegauth")); 
		formObject.setNGValue("Final_Limit", formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
		
		PersonalLoanS.mLogger.info("CAD_ANALYST1_DECISION: "+formObject.getNGValue("CAD_ANALYST1_DECISION"));
		//disha FSD
		if(NGFUserResourceMgr_PL.getGlobalVar("PL_RefertoCredit").equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")))
		{
			formObject.setNGValue("q_MailTo",formObject.getNGValue("cmplx_Decision_ReferTo"));
		}
		if("Refer".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")))
		{
			formObject.setNGValue("ReferTo", formObject.getNGValue("DecisionHistory_ReferTo"));
		}
	
		}
		catch(Exception ex)
		{
			PersonalLoanS.mLogger.info(ex.getMessage());
		}
		finally
		{
			try{
				saveIndecisionGrid();
				LoadReferGrid();
				//++ below code aded by disha on 27-3-2018 to set value of var_int4
				if("STP".equalsIgnoreCase(formObject.getNGValue("Highest_Delegation_CAD")))
				{
					PersonalLoanS.mLogger.info("Inside STP ");
					if("Approve".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")))
					{
						PersonalLoanS.mLogger.info("Inside STP Approve CAD1");
						formObject.setNGValue("q_hold1", 1);
						formObject.setNGValue("hold2", 1);
						//formObject.setNGValue("VAR_INT4",1);
						PersonalLoanS.mLogger.info("Inside STP Approve " + formObject.getNGValue("q_hold1"));
					}
				}
				//submitFormCompleted_cust(arg0);
			}
			catch(Exception e){
				PersonalLoanS.mLogger.info("Exception occured in submit form started method" +e.getMessage());
				PersonalLoanS.mLogger.info("Exception occured in submit form started method" +e.getStackTrace());
			}
			//++ above code aded by disha on 27-3-2018 to set value of var_int4
		}
		
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
