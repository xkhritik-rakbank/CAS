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

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


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
		//iAdded
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setSheetVisible(tabName, 9, true);

	}


	public void formPopulated(FormEvent pEvent) 
	{
		try{
			new PersonalLoanSCommonCode().setFormHeader(pEvent);
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			formObject.setNGValue("Mandatory_Frames", NGFUserResourceMgr_PL.getGlobalVar("Cad1_Frame_Name"));
			String cc_waiv_flag = formObject.getNGValue("is_cc_waiver_require");
			formObject.setVisible("totBalTransfer", false);
			formObject.setVisible("ReferHistory",true);
			formObject.setVisible("ReferHistory_Frame1", true);
			if(cc_waiv_flag.equalsIgnoreCase("N")){
				formObject.setVisible("Card_Details", true);
			}
			//enable_cad1();
			enable_CPV();
		}catch(Exception e)
		{
			PersonalLoanS.mLogger.info( "Exception:"+e.getMessage());
			printException(e);
		}
		CheckforRejects("CAD_Analyst1");
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
			LoadView(pEvent.getSource().getName()); 
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
//EligibilityAndProductInformation
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
			formObject.setLocked("cmplx_IncomeDetails_RentalIncome",false);
			formObject.setEnabled("cmplx_IncomeDetails_RentalIncome",true);		
			formObject.setLocked("cmplx_IncomeDetails_EducationalAllowance",false);
			formObject.setEnabled("cmplx_IncomeDetails_EducationalAllowance",true);

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
				formObject.setNGFrameState("ELigibiltyAndProductInfo_Frame6",0);
				formObject.setIFrameSrc("ELigibiltyAndProductInfo_IFrame2","/webdesktop/custom/CustomJSP/Eligibility_Card_Limit.jsp");
			}
			//formObject.setNGValue("cmplx_IncomeDetails_totSal", formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalTai"));--commented by akshay
		String	query1="select isnull(sum(convert(float,Take_Amount)),0) as takeoveramt from ng_rlos_cust_extexpo_LoanDetails  with (nolock) where child_Wi='"+formObject.getWFWorkitemName()+"' and Take_Over_Indicator='true' " ;

		String	query2="select isnull(sum(convert(float,CurrentBalance)),0) as takeoveramt from ng_rlos_cust_extexpo_CardDetails where  child_Wi='"+formObject.getWFWorkitemName()+" ' and Take_Over_Indicator='true'";

		String	query3= "select isnull(sum(convert(float,TakeOver_Amount)),0) as takeoveramt from ng_rlos_gr_LiabilityAddition where LiabilityAddition_wiName= '"+formObject.getWFWorkitemName()+"' and TakeOverIndicator='true'";
		
		List<List<String>> l1=formObject.getDataFromDataSource(query1);
		List<List<String>> l2=formObject.getDataFromDataSource(query2);
		List<List<String>> l3=formObject.getDataFromDataSource(query3);
		PersonalLoanS.mLogger.info("queriessssss"+query1+" "+query2+" "+query3);
		double s1=Double.parseDouble(l1.get(0).get(0));
		double s2=Double.parseDouble(l2.get(0).get(0));
		double s3=Double.parseDouble(l3.get(0).get(0));
		double summ=0;
		summ=s1+s2+s3;
		formObject.setNGValue("cmplx_EligibilityAndProductInfo_TakeoverAMount", summ);		
		PersonalLoanS.mLogger.info(summ);	
		
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
			formObject.setVisible("ExtLiability_Label15",false);
			//formObject.setVisible("ExtLiability_Label15",true);
			formObject.setVisible("cmplx_Liability_New_AggrExposure",false); //27/05/2021
			//commented by saurabh on 4th Dec.
			//by shweta for pcasi-994,995,996
			formObject.setVisible("ExtLiability_QCAmt", false);
			formObject.setVisible("ExtLiability_QCEMI",false);
			formObject.setVisible("ExtLiability_CACIndicator",false);
			formObject.setVisible("ExtLiability_Label20",false);
			formObject.setVisible("ExtLiability_Label23",false);
			formObject.setVisible("ExtLiability_CACIndicator",false);
			formObject.setVisible("Liability_New_Label1",false);//Arun (16/08/17)
			formObject.setVisible("Liability_New_MOB",false);//Arun (16/08/17)
			formObject.setVisible("Liability_New_Label3",false);//Arun (16/08/17)
			formObject.setVisible("Liability_New_Utilization",false);//Arun (16/08/17)
			formObject.setVisible("Liability_New_Label5",true);// hritik 4.7.21 PCASI 3523
			formObject.setVisible("Liability_New_Outstanding",true);//Arun (16/08/17)
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
			//above line commented to remove default value being getting set in the worst field
			formObject.setNGValue("cmplx_Liability_New_overrideIntLiab",true);//Arun (27/09/17)
			formObject.setNGLVWRowHeight("Liability_New_Label8", 152);
			formObject.setNGLVWRowHeight("Liability_New_Text3", 168);										
			if (NGFUserResourceMgr_PL.getGlobalVar("PL_RESC").equalsIgnoreCase(App_Type) || NGFUserResourceMgr_PL.getGlobalVar("PL_RESN").equalsIgnoreCase(App_Type) || NGFUserResourceMgr_PL.getGlobalVar("PL_RESR").equalsIgnoreCase(App_Type)){
				formObject.setVisible("Liability_New_Label6", true);
				formObject.setVisible("cmplx_Liability_New_noofpaidinstallments", true);
			}
			else
			{
				formObject.setVisible("Liability_New_Label6", false);
				formObject.setVisible("cmplx_Liability_New_noofpaidinstallments", false);
			}
			//added by saurabh on 11th Jan
			if(formObject.getNGValue("cmplx_Liability_New_overrideIntLiab").equalsIgnoreCase("true") && !formObject.isVisible("Liability_New_Overwrite")){

				formObject.setVisible("Liability_New_Overwrite", true);
			}
		}

		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setVisible("EMploymentDetails_Label64", false);
			formObject.setVisible("cmplx_EmploymentDetails_OtherDesign", false);
			formObject.setLocked("cmplx_EmploymentDetails_employer_type", true); // pcasp-1511
			formObject.setLocked("cmplx_EmploymentDetails_IncInPL",true);
			formObject.setLocked("cmplx_EmploymentDetails_IncInCC",true);
			formObject.setVisible("EMploymentDetails_Label32", true);
			formObject.setVisible("cmplx_EmploymentDetails_Field_visit_done", true);
			formObject.setLocked("cmplx_EmploymentDetails_Field_visit_done", false);
			formObject.setEnabled("cmplx_EmploymentDetails_Field_visit_done", true);
			formObject.setLocked("cmplx_EmploymentDetails_LengthOfBusiness",true);
			formObject.setEnabled("cmplx_EmploymentDetails_LengthOfBusiness",false);

			formObject.setLocked("cmplx_EmploymentDetails_actualworkemp", false);


			formObject.setVisible("EMploymentDetails_Label7", false);
			formObject.setVisible("cmplx_EmploymentDetails_OtherBankCAC", false);
			formObject.setVisible("EmploymentDetails_Bank_Button", false); 

			//below code added by nikhil 04/11/18
			formObject.setVisible("EMploymentDetails_EmploymentDetails_Bank_Button", false); 

			if("".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg")))
			{
				formObject.setVisible("cmplx_EmploymentDetails_IndusSeg",false);
				formObject.setVisible("EMploymentDetails_Label59",false);
			}
			else {
				formObject.setVisible("cmplx_EmploymentDetails_IndusSeg",true);
				formObject.setVisible("EMploymentDetails_Label59",true);
			}

			//changes done for jira: PCSP-207 by aastha
			if(!"".equals(formObject.getNGValue("cmplx_Customer_NEP"))&& !"--Select--".equals(formObject.getNGValue("cmplx_Customer_NEP")) && formObject.getNGValue("cmplx_Customer_NEP")!=null){
				PersonalLoanS.mLogger.info("inside NEP if condition");
				formObject.setLocked("CompanyDetails_Label15", false);
				formObject.setLocked("NepType", false);
			}
			else{
				PersonalLoanS.mLogger.info("inside NEP else condition");
				formObject.setLocked("CompanyDetails_Label15", true);
				formObject.setLocked("NepType", true);
			}
			//end for jira-pcsp-207
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
				formObject.setLocked("cmplx_EmploymentDetails_accpvded",true);
				formObject.setLocked("cmplx_EmploymentDetails_authsigname",false);
				
				formObject.setLocked("cmplx_EmploymentDetails_highdelinq",true);
				formObject.setLocked("cmplx_EmploymentDetails_dateinPL",true);
				formObject.setLocked("cmplx_EmploymentDetails_dateinCC",true);
				formObject.setLocked("cmplx_EmploymentDetails_remarks",true);
				formObject.setLocked("cmplx_EmploymentDetails_Remarks_PL",true);
				formObject.setEnabled("cmplx_EmploymentDetails_Remarks_PL",false);
				formObject.setEnabled("cmplx_EmploymentDetails_remarks",false);


				formObject.setLocked("cmplx_EmploymentDetails_EmpStatus",false);
				formObject.setLocked("cmplx_EmploymentDetails_Emp_Type",false);
				//formObject.setLocked("cmplx_EmploymentDetails_Designation",false);
				formObject.setLocked("EMploymentDetails_Designation_button",false);
				//formObject.setLocked("cmplx_EmploymentDetails_DesigVisa",false);
				formObject.setLocked("DesignationAsPerVisa_button",false);


				formObject.setLocked("cmplx_EmploymentDetails_EmpContractType",false);

				formObject.setLocked("cmplx_EmploymentDetails_Freezone",false);
				//formObject.setLocked("cmplx_EmploymentDetails_FreezoneName",false);
				formObject.setLocked("cmplx_EmploymentDetails_IndusSeg",false);
				formObject.setLocked("cmplx_EmploymentDetails_Kompass",false);
				formObject.setLocked("cmplx_EmploymentDetails_StaffID",false);
				formObject.setLocked("cmplx_EmploymentDetails_Dept",false);
				formObject.setLocked("cmplx_EmploymentDetails_CntrctExpDate",false);
				formObject.setVisible("cmplx_EmploymentDetails_TL_Number", false);//pcasi 3601
				formObject.setVisible("EMploymentDetails_Label63", false);// 4.7.21 
				formObject.setVisible("EMploymentDetails_Label65", false);
				formObject.setVisible("cmplx_EmploymentDetails_TL_Emirate", false);
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
				formObject.setLocked("cmplx_EmploymentDetails_authsigname",false);
				//PCASI - 3437
				formObject.setLocked("cmplx_EmploymentDetails_highdelinq",true);
				formObject.setLocked("cmplx_EmploymentDetails_dateinPL",true);
				formObject.setLocked("cmplx_EmploymentDetails_dateinCC",true);
				formObject.setLocked("cmplx_EmploymentDetails_remarks",true);
				formObject.setLocked("cmplx_EmploymentDetails_Remarks_PL",true);
				formObject.setEnabled("cmplx_EmploymentDetails_Remarks_PL",false);
				formObject.setEnabled("cmplx_EmploymentDetails_remarks",false);
				formObject.setVisible("cmplx_EmploymentDetails_TL_Number", true);
				formObject.setVisible("EMploymentDetails_Label63", true);
				formObject.setVisible("EMploymentDetails_Label65", true);
				formObject.setVisible("cmplx_EmploymentDetails_TL_Emirate", true);

				formObject.setLocked("cmplx_EmploymentDetails_EmpIndusSector",false);
				formObject.setLocked("cmplx_EmploymentDetails_Indus_Macro",false);
				formObject.setLocked("cmplx_EmploymentDetails_Indus_Micro",false);
				formObject.setLocked("cmplx_EmploymentDetails_EmpContractType",false);
				formObject.setLocked("cmplx_EmploymentDetails_Emp_Type",false);
				//formObject.setLocked("cmplx_EmploymentDetails_Designation",false);
				//formObject.setLocked("cmplx_EmploymentDetails_DesigVisa",false);
				formObject.setLocked("EMploymentDetails_Designation_button",false);
				formObject.setLocked("EMploymentDetails_DesignationAsPerVisa_button",false);
				formObject.setVisible("EMploymentDetails_Label36",true);

				formObject.setLocked("cmplx_EmploymentDetails_EmirateOfWork",false);
				formObject.setLocked("cmplx_EmploymentDetails_HeadOfficeEmirate",false);
				formObject.setLocked("cmplx_EmploymentDetails_Freezone",false);
				//formObject.setLocked("cmplx_EmploymentDetails_FreezoneName",false);
				formObject.setLocked("cmplx_EmploymentDetails_IndusSeg",false);
				formObject.setLocked("cmplx_EmploymentDetails_Kompass",false);
				formObject.setLocked("cmplx_EmploymentDetails_StaffID",false);
				formObject.setLocked("cmplx_EmploymentDetails_Dept",false);
				formObject.setLocked("cmplx_EmploymentDetails_CntrctExpDate",false);

			}
			//below code added by nikhil 1/3/18
			Fields_ApplicationType_Employment();

			formObject.setVisible("EMploymentDetails_Label31", true);
			formObject.setVisible("cmplx_EmploymentDetails_empmovemnt", false);
			formObject.setVisible("EMploymentDetails_Label15", false);
			if("".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg")))
			{
				formObject.setVisible("cmplx_EmploymentDetails_IndusSeg",false);
				formObject.setVisible("EMploymentDetails_Label59",false);
			}
			else {
				formObject.setVisible("cmplx_EmploymentDetails_IndusSeg",true);
				formObject.setVisible("EMploymentDetails_Label59",true);
			}
			if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_Freezone")))
			{
				formObject.setLocked("cmplx_EmploymentDetails_FreezoneName", false);
			}
			else
			{
				formObject.setLocked("cmplx_EmploymentDetails_FreezoneName", true);
			}

			formObject.setVisible("EMploymentDetails_Label39",true);
			formObject.setLocked("cmplx_EmploymentDetails_VisaSponser",false);

			formObject.setLocked("cmplx_EmploymentDetails_LOSPrevious", false);
			formObject.setLocked("cmplx_EmploymentDetails_DOJPrev",false);
			formObject.setEnabled("cmplx_EmploymentDetails_DOJPrev",true);
			formObject.setLocked("cmplx_EmploymentDetails_LOS",true);
			formObject.setLocked("cmplx_EmploymentDetails_DOJ",false);
			formObject.setVisible("EMploymentDetails_Label34",true);
			formObject.setVisible("cmplx_EmploymentDetails_HeadOfficeEmirate",true);
			formObject.setLocked("cmplx_EmploymentDetails_Designation",true);
			formObject.setLocked("cmplx_EmploymentDetails_DesigVisa",true);
			formObject.setLocked("cmplx_EmploymentDetails_Status_PLNational",true);//added by saurabh1 for I6
			formObject.setLocked("cmplx_EmploymentDetails_Status_PLExpact",true);//added by saurabh1 for I6
			formObject.setLocked("cmplx_EmploymentDetails_EmpName", true); // Added by Imran
			formObject.setLocked("cmplx_EmploymentDetails_EMpCode", true);	// Added by Imran
			loadPicklist4();//by shweta for NEP TYPE
			if(!"".equals(formObject.getNGValue("cmplx_Customer_NEP"))&& !"--Select--".equals(formObject.getNGValue("cmplx_Customer_NEP")) && formObject.getNGValue("cmplx_Customer_NEP")!=null){

				PersonalLoanS.mLogger.info("inside NEP if condition");
				formObject.setLocked("EMploymentDetails_Label25", false);
				formObject.setLocked("cmplx_EmploymentDetails_NepType", false);
			}
			else{
				PersonalLoanS.mLogger.info("inside NEP else condition");
				formObject.setLocked("EMploymentDetails_Label25", true);
				formObject.setLocked("cmplx_EmploymentDetails_NepType", true);
			}


		}

		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklistELigibiltyAndProductInfo();

			/*if(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("false"))
			{

				formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalLimit", true);//total final limit
				//formObject.setLocked("ELigibiltyAndProductInfo_Button1", true);//calculate re-eligibility
			} */
			formObject.setVisible("ELigibiltyAndProductInfo_Frame5",false);//

			formObject.setVisible("ELigibiltyAndProductInfo_Label23", false);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_InterestRate", true);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_promo_interest_rate", true);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_EMI", true);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_Tenor", true);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_RepayFreq", true);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_FinalInterestRate", false);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalDBR",true);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalTAI",true);
			//below added by Rajan 21/06/2021
			formObject.setLocked("cmplx_EligibilityAndProductInfo_EFCHidden",true);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalLimit", true);

			//added by akshay on 22/12/17 for fetching card details
			if(!formObject.isVisible("CardDetails_Frame1") && !"Y".equalsIgnoreCase(formObject.getNGValue("is_cc_waiver_require"))){
				fetch_CardDetails_frag(formObject);

			}
			PersonalLoanS.mLogger.info("Shweta - cmplx_EligibilityAndProductInfo_takeoverBank value 470" + formObject.getNGValue("cmplx_EligibilityAndProductInfo_takeoverBank"));

			loadEligibilityData();
			formObject.setVisible("ELigibiltyAndProductInfo_Label5", false); // hritk 25.7.21
			formObject.setVisible("ELigibiltyAndProductInfo_Label6", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_Tenor", false);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_InterestType", true);
			formObject.setNGValue("cmplx_EligibilityAndProductInfo_InterestType", "Equated");
			formObject.setTop("cmplx_EligibilityAndProductInfo_Save", formObject.getTop("ELigibiltyAndProductInfo_Button1")+50);

		}

		else if ("LoanDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist_LoanDetails();
			loanvalidate();//added by akshay for proc 8460

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
			formObject.setVisible("CC_Loan_Frame1", false);
			//PROC-7658 AMan


			//below code by jahnavi for jirs 3059
			formObject.setEnabled("cmplx_LoanDetails_inttype",false);
			formObject.setEnabled("cmplx_LoanDetails_ddastatus",false);
			formObject.setLocked("LoanDetails_Frame1", true);
			formObject.setLocked("cmplx_LoanDetails_inttype",true);
			formObject.setLocked("cmplx_LoanDetails_ddastatus",true);
			formObject.setLocked("cmplx_LoanDetails_insplan", true);
			formObject.setLocked("LoanDetails_Button1", false);
			//hritik - pCASI - 3801
			//PersonalLoanS.mLogger.info("cmplx_LoanDetails_fdisbdate:"+formObject.getNGValue("cmplx_LoanDetails_fdisbdate"));
			formObject.setNGValue("cmplx_LoanDetails_fdisbdate", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
			PersonalLoanS.mLogger.info("cmplx_LoanDetails_fdisbdate:"+formObject.getNGValue("cmplx_LoanDetails_fdisbdate"));
			formObject.setNGValue("LoanDetails_disbdate", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));			
			PersonalLoanS.mLogger.info("LoanDetails_disbdate:"+formObject.getNGValue("LoanDetails_disbdate"));
			PersonalLoanS.mLogger.info("before CAD1 Moratorium");
			Moratorium();
			PersonalLoanS.mLogger.info("cmplx_LoanDetails_fdisbdate: after moritorium at cad1"+formObject.getNGValue("cmplx_LoanDetails_fdisbdate"));
			PersonalLoanS.mLogger.info("After CAD1 Moratorium");
		}

		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist_Address();
			LoadView(pEvent.getSource().getName()); 
			formObject.setLocked("AddressDetails_Frame1",true);
		}

		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("AltContactDetails_Frame1",true);
			LoadpicklistAltcontactDetails();
			//below code added by siva on 01112019 for PCAS-1401
			AirArabiaValid();
			enrollrewardvalid();
			LoadView(pEvent.getSource().getName()); 
			//Code ended by siva on 01112019 for PCAS-1401
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
			LoadView(pEvent.getSource().getName()); 
			//formObject.setLocked("OECD_Frame8",true);
		}
		//code added by bandana start
		else if ("Details".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("CC_Loan_Frame1",true);
			formObject.setEnabled("CC_Loan_Frame1", false);
			formObject.setVisible("totBalTransfer", false);

		}

		else if ("CC_Loan".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("CC_Loan_Frame1",true);
			formObject.setEnabled("CC_Loan_Frame1", false);
			loadPicklist_ServiceRequest();
			formObject.setVisible("totBalTransfer", false);

		}
		//code added by bandana end
		else if ("PartMatch".equalsIgnoreCase(pEvent.getSource().getName())) {

			LoadView(pEvent.getSource().getName());
			formObject.setLocked("PartMatch_Frame1",true);
			formObject.setLocked("PartMatch_Dob",true);
			//LoadPickList("PartMatch_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
		}

		else if ("FinacleCRMIncident".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("FinacleCRMIncident_Frame1",true);
		}

		else if ("FinacleCRMCustInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadinFinacleCRNGrid(formObject);
			formObject.setLocked("FinacleCRMCustInfo_Frame1",true);
		}

		else if ("FinacleCore".equalsIgnoreCase(pEvent.getSource().getName())) 
		{	//added by shweta for Seq#105
			LoadPickList("FinacleCore_cheqtype", "select '--Select--' as description,'' as code union select convert(varchar, description),Code from ng_MASTER_Cheque_Type with (nolock) order by code");
			LoadPickList("FinacleCore_typeofret", "select '--Select--' union all select convert(varchar, description) from ng_MASTER_TypeOfReturn with (nolock)");

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
			formObject.setLocked("RejectEnq_Save",true);//pcasi-1002
			//empty else if
		}

		else if ("SalaryEnq".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("SalaryEnq_Frame1",true);
		}

		else if ("CustDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			//added by shweta for  PCSP-39
			//by shweta
			LoadPickList("cmplx_CustDetailVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");
			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_CustDetailVerification_mobno1_ver","cmplx_CustDetailVerification_mobno2_ver","cmplx_CustDetailVerification_dob_ver","cmplx_CustDetailVerification_POBoxno_ver","cmplx_CustDetailVerification_emirates_ver","cmplx_CustDetailVerification_persorcompPOBox_ver","cmplx_CustDetailVerification_hcountrytelno_ver","cmplx_CustDetailVerification_hcontryaddr_ver","cmplx_CustDetailVerification_email1_ver","cmplx_CustDetailVerification_email2_ver","cmplx_CustDetailVerification_Mother_name_ver");//pcasi-1003
			LoadPickList("cmplx_CustDetailVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");
			LoadPicklistVerification(LoadPicklist_Verification);
			LoadPickList("cmplx_CustDetailVerification_emirates_upd","select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_emirate with (nolock) order by Code");//PCAS-2509

			enable_custVerification();

			//formObject.setLocked("CustDetailVerification_Frame1",true);
			//formObject.setLocked("cmplx_CustDetailVerification_dob_upd",true);
			PersonalLoanS.mLogger.info("sagarika Y"+formObject.getNGValue("CPV_WAVIER"));
			if("Y".equalsIgnoreCase(formObject.getNGValue("CPV_WAVIER")))
			{
				PersonalLoanS.mLogger.info("sagarika Y");
				formObject.setNGValue("cmplx_CustDetailVerification_Decision","Not Applicable");
			}
			// to be added disableforNA();
			disableforNA();
			if("PA".equalsIgnoreCase(formObject.getNGValue("Sub_Product")))
			{
				formObject.setNGValue("cmplx_CustDetailVerification_Decision","Not Applicable");
				formObject.setLocked("CustDetailVerification_Frame1",true);
			}

			//sagarika for PCAS-2577
		}
		//hritik 29.6.21 PCASI 
		else if ("Employment_Verification".equalsIgnoreCase(pEvent.getSource().getName()) || 
				"EmploymentVerification_s2".equalsIgnoreCase(pEvent.getSource().getName())
				|| "EmploymentVerification_s2_Frame2".equalsIgnoreCase(pEvent.getSource().getName())) {
			
			//formObject.fetchFragment("Employment_Verification", "EmploymentVerification_s2", "q_empVer_sp")
			formObject.setLocked("EmploymentVerification_s2_search_TL_number",true);
			formObject.setEnabled("EmploymentVerification_s2_search_TL_number",false);
			PersonalLoanS.mLogger.info("Inside cad1 employmentverf");
		}

		else if ("BussinessVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			enable_busiVerification();
			formObject.setLocked("BussinessVerification_Frame1",true);
		}

		else if ("HomeCountryVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("HomeCountryVerification_Frame1",true);
			formObject.setVisible("HomeCountryVerification",false); // hritik 22.8.21 -3779
			//enable_homeVerification();//pcasi-1027 by shweta
		}

		else if ("ResidenceVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			enable_ResVerification();
			formObject.setLocked("ResidenceVerification_Frame1",true);
		}

		else if ("GuarantorVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("GuarantorVerification_Frame1",true);
		}

		else if ("ReferenceDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			enable_ReferenceVerification();
			formObject.setLocked("ReferenceDetailVerification_Frame1",true);
		}

		else if ("OfficeandMobileVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			//changes added by aastha for code merge on 21/11/2019
			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_OffVerification_fxdsal_ver","cmplx_OffVerification_accpvded_ver","cmplx_OffVerification_desig_ver","cmplx_OffVerification_doj_ver","cmplx_OffVerification_cnfminjob_ver");
			LoadPickList("cmplx_OffVerification_colleaguenoverified", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_CPVVeri with (nolock) where Sno != 3 order by code"); //Arun modified on 14/12/17
			//changed by nikhil for CPV changes 17-04
			LoadPickList("cmplx_OffVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) where IsActive='Y' and For_custdetail_only='N' order by code");
			//below code by saurabh on 28th nov 2017.
			LoadPickList("cmplx_OffVerification_offtelnovalidtdfrom", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_offNoValidatedFrom with (nolock)");
			LoadPickList("cmplx_OffVerification_hrdnocntctd", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_HRDContacted with (nolock)");

			//enable_officeVerification();

			formObject.setLocked("OfficeandMobileVerification_Frame1",true);
			if("Mismatch".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_fxdsal_ver")))
			{	formObject.setEnabled("OfficeandMobileVerification_save",true);
				formObject.setLocked("cmplx_OffVerification_fxdsal_override", false);
			}
			
			if("Mismatch".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_accpvded_ver")))
			{	formObject.setEnabled("OfficeandMobileVerification_save",true);
				formObject.setLocked("cmplx_OffVerification_accprovd_override", false);
			}
			 
			if("Mismatch".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_desig_ver")))
			{	formObject.setEnabled("OfficeandMobileVerification_save",true);
				formObject.setLocked("cmplx_OffVerification_desig_override", false);
			}
			
			if("Mismatch".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_doj_ver")))
			{	formObject.setEnabled("OfficeandMobileVerification_save",true);
				formObject.setLocked("cmplx_OffVerification_doj_override", false);
			}
			
		    if("Mismatch".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_cnfrminjob_upd")))
			{	formObject.setEnabled("OfficeandMobileVerification_save",true);
				formObject.setLocked("cmplx_OffVerification_cnfrmjob_override", false);
			}
		    else if((!"Mismatch".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_cnfrminjob_upd")))&&(!"Mismatch".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_doj_ver")))&&(!"Mismatch".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_desig_ver")))&&(!"Mismatch".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_accpvded_ver")))&&(!"Mismatch".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_fxdsal_ver"))))
		    {
		    	formObject.setEnabled("OfficeandMobileVerification_save",false);
		    	formObject.setLocked("OfficeandMobileVerification_save", true);
		    }
			


			//LoadPickList("cmplx_OffVerification_offtelnovalidtdfrom", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_offNoValidatedFrom with (nolock) order by code");
			//LoadPickList("cmplx_OffVerification_desig_upd", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Designation with (nolock) where isActive='Y' order by Code");
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
			//added by aastha for jira pcsp-1151 start
			formObject.setVisible("NotepadDetails_Frame3",true);
			formObject.setLocked("NotepadDetails_Frame3",false);
			//changes end by aastha
		}
		//disha FSD
		else if ("RiskRating".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("RiskRating_Frame1",true);
			loadPicklistRiskRating(); //Arun (10/10)
		}
		else if ("Details".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setVisible("totBalTransfer",false);
		}
		//aastha for code merge
		else if ("SmartCheck".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("SmartCheck_Frame1",true);
			formObject.setVisible("SmartCheck_Label1",true);
			formObject.setVisible("SmartCheck_Label2",true);
			formObject.setVisible("SmartCheck_CPVRem",true);
			formObject.setVisible("SmartCheck_Label4",false);

			formObject.setVisible("SmartCheck_FCURemarks",false);

			formObject.setLocked("SmartCheck_CPVRemarks",true);
			formObject.setLocked("SmartCheck_Add",false);
			formObject.setLocked("SmartCheck_Modify",true);
			formObject.setLocked("SmartCheck_save",false);
			//++ Above Code added By Yash on Oct 12, 2017  to fix : 33-"CPV remarks should be disabled" : Reported By Shashank on Oct 09, 2017++

			//formObject.setLocked("SmartCheck1_Modify",true);


		}
		//end changes

		//edited by bandana
		else if ("SmartCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("SmartCheck1_Frame1",true);
			//formObject.setVisible("SmartCheck1_Label2",false);
			//formObject.setVisible("SmartCheck1_CPVRem",false);
			//formObject.setVisible("SmartCheck1_Label4",false);
			//formObject.setVisible("SmartCheck1_FCURemarks",false);

			formObject.setLocked("SmartCheck1_CPVRemarks",true);
			//formObject.setVisible("SmartCheck1_Label4",false);
			formObject.setLocked("SmartCheck1_FCURemarks",true);
			formObject.setLocked("SmartCheck1_Modify",true);
		}
		/*		else if ("SmartCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setVisible("SmartCheck1_Label2",false);
			formObject.setVisible("SmartCheck1_CPVrem",false);
			formObject.setVisible("SmartCheck1_Label4",false);
			formObject.setVisible("SmartCheck1_FCUrem",false);
			formObject.setLocked("SmartCheck1_Modify",true);
		}
		 */ 
		//bandana code change for fcu
		//disha FSD
		else if ("ExternalBlackList".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setLocked("ExternalBlackList_Frame1",true);
		}

		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadInDecGrid();
			//for decision fragment made changes 8th dec 2017
			PersonalLoanS.mLogger.info("***********Inside decision history");
			//added by shweta for PCSP-319
			if(!formObject.isVisible("ExtLiability_Frame1")){
				formObject.fetchFragment("InternalExternalLiability", "Liability_New", "q_Liabilities");
			}
			//changed by akshay on 6/12/17 for decision alignment
			Decision_cadanalyst1();
			formObject.setEnabled("cmplx_Decision_Manual_Deviation", true);
			fragment_ALign("Decision_Label1,cmplx_Decision_VERIFICATIONREQUIRED#DecisionHistory_Button4#DecisionHistory_CifLock#DecisionHistory_CifUnlock#\n#DecisionHistory_LabelNewStrength,DecisionHistory_NewStrength#DecisionHistory_newStrengthAdd#DecisionHistory_Label3,cmplx_Decision_strength#\n#DecisionHistory_LabelNewWeakness,DecisionHistory_NewWeakness#DecisionHistory_newWeaknessAdd#DecisionHistory_Label4,cmplx_Decision_weakness#\n#DecisionHistory_Label13,cmplx_Decision_Deviationcode#DecisionHistory_Label14,cmplx_Decision_Dectech_decsion#DecisionHistory_Label15,cmplx_Decision_score_grade#\n#Decision_Label3,cmplx_Decision_Decision#DecisionHistory_Label1,cmplx_Decision_ReferTo#DecisionHistory_Label11,DecisionHistory_DecisionReasonCode#DecisionHistory_Label16,cmplx_Decision_Highest_delegauth#DecisionHistory_Button5#\n#cmplx_Decision_Manual_Deviation#DecisionHistory_Button6#DecisionHistory_Label18,cmplx_Decision_CADDecisiontray#DecisionHistory_Label39,cmplx_Decision_SetReminder_CA#DecisionHistory_Label38,cmplx_Decision_NoOfAttempts_CA#\n#Decision_Label4,cmplx_Decision_REMARKS#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
			formObject.setTop("DecisionHistory_Label18", formObject.getTop("DecisionHistory_DecisionReasonCode")+60);//PCASI-2862
			formObject.setTop("cmplx_Decision_CADDecisiontray", formObject.getTop("DecisionHistory_Label18")+20);

			formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
			formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
			formObject.setTop("ReferHistory", formObject.getTop("DecisionHistory")+formObject.getHeight("DecisionHistory")+20);


			//for decision fragment made changes 8th dec 2017
			PersonalLoanS.mLogger.info( "value of manual deviation is:" + formObject.getNGValue("cmplx_Decision_Manual_Deviation"));
			if ("false".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Manual_Deviation"))){
				formObject.setEnabled("DecisionHistory_Button5", false);
				formObject.setEnabled("DecisionHistory_Button6", false);
			}
			else {
				formObject.setEnabled("DecisionHistory_Button5", true);
				formObject.setEnabled("DecisionHistory_Button6", true);
			}
			formObject.setLocked("cmplx_Decision_strength",true);
			formObject.setLocked("cmplx_Decision_weakness",true);
			formObject.setLocked("cmplx_Decision_SetReminder_CA",true);
			formObject.setLocked("cmplx_Decision_NoOfAttempts_CA",true);
			formObject.setVisible("DecisionHistory_CifUnlock",false); // Added by Rajan for PCASP-1555
			formObject.setVisible("DecisionHistory_CifLock", false);// Added by Rajan for PCASP-1554


			PersonalLoanS.mLogger.info("***********Inside after fragment alignment decision history");

			loadPicklist1();
			//added by saurabh for CAM report JIRA - 9844.
			int framestateLoanDet = formObject.getNGFrameState("LoanDetails");
			if(framestateLoanDet!=0){
				formObject.fetchFragment("LoanDetails", "LoanDetails", "q_Loan");
				formObject.setNGFrameState("LoanDetails", 0);
				adjustFrameTops("LoanDetails,Risk_Rating");

				expandLoanDetails();
			}
			LoadPickList("cmplx_Decision_CADDecisiontray", "select 'Select' as Refer_Credit  union select convert(varchar, Refer_Credit)  from NG_Master_ReferCredit with (nolock) order by Refer_Credit desc");
			//condition added by saurabh on 15th Oct.
			if(!NGFUserResourceMgr_PL.getGlobalVar("PL_Approve").equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision"))){

			//	formObject.setLocked("cmplx_Decision_CADDecisiontray",false); hritik 28.6.21 PCASI 3526
			}
	
			//Common function for decision fragment textboxes and combo visibility
			if ("false".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Manual_Deviation"))){
				formObject.setLocked("cmplx_Decision_CADDecisiontray", true);
			}
			else {
				formObject.setLocked("cmplx_Decision_CADDecisiontray", false);
			}
			if(NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Manual_Deviation"))){
				formObject.setLocked("DecisionHistory_Button6",false);
				formObject.setLocked("cmplx_Decision_CADDecisiontray", false);        
			}
			else{
				formObject.setLocked("DecisionHistory_Button6",true);
				formObject.setLocked("cmplx_Decision_CADDecisiontray", true); 
				formObject.setNGValue("cmplx_Decision_CADDecisiontray", "Select");
			}
			PersonalLoanS.mLogger.info("Check for refer history grid before");
			formObject.setVisible("ReferHistory_Frame1", true);
			formObject.setVisible("ReferHistory",true);
			formObject.fetchFragment("ReferHistory", "ReferHistory", "q_ReferHistory");
			formObject.setNGFrameState("ReferHistory_Frame1",0);
			formObject.setLocked("ReferHistory_Frame1",false);
			formObject.setEnabled("ReferHistory_status",true);
			PersonalLoanS.mLogger.info("Check for refer history grid after");
		} 
		//below code added by nikhil 19/1/18
		/*else if ("IncomingDoc".equalsIgnoreCase(pEvent.getSource().getName())) {
			 fetchIncomingDocRepeater();
		 }*/
		else if ("FPU_GRID".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.fetchFragment("FPU_GRID", "Fpu_Grid", "q_FPU_Grid");
			formObject.setLocked("cmplx_FPU_Grid_Officer_Name", true);
			LoadPickList("cmplx_FPU_Grid_Officer_Name", "select ' --Select-- 'as UserName union select UserName from PDBUser where UserIndex in (select UserIndex from PDBGroupMember where GroupIndex=(select GroupIndex from PDBGroup where GroupName in ('FCU','FPU')))");
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
		PersonalLoanS.mLogger.info("Inside PL PROCESS saveFormCompleted()" ); 
		CustomSaveForm();
		PersonalLoanS.mLogger.info("Inside PL after  CustomSaveForm()" );

	}


	public void saveFormStarted(FormEvent arg0) throws ValidatorException {
		PersonalLoanS.mLogger.info("Inside PL PROCESS saveFormStarted()" ); 
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try
		{
		PersonalLoanS.mLogger.info("Before Nmber format @cad1 :");
		NumberFormat fin_lim = NumberFormat.getNumberInstance(Locale.US);
		String FL = formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit");
		double FL_1 = Double.parseDouble(FL);
		PersonalLoanS.mLogger.info("Before NUmber format : FL :"+FL_1);
		String new_format = fin_lim.format(FL_1);
		PersonalLoanS.mLogger.info("Before NUmber format new_format::"+new_format);
		formObject.setNGValue("loan_amount_1",new_format);
		PersonalLoanS.mLogger.info("After set NUmber format new_format:::"+new_format);
		}
		catch(Exception ex)
		{
			PersonalLoanS.mLogger.info(ex.getMessage());
		} //Hritik for communication
	}


	public void submitFormCompleted(FormEvent pEvent) throws ValidatorException {

		PersonalLoanS.mLogger.info("Inside PL PROCESS submitFormCompleted()" ); 
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String Decision= formObject.getNGValue("cmplx_Decision_Decision");
		//LoadReferGrid();
		List<String> objInput=new ArrayList<String> ();
		List<Object> objOutput=new ArrayList<Object>();
		//disha FSD cad delegation procedure changes
		try
		{//CustomerLabel
			//added by aastha for jira Sprint 4- WI ALlocation Logic
			//formObject.setNGValue("CAD_Username", formObject.getUserName());
			//changes end for jira sprint 4 for jira Sprint 4- WI ALlocation Logic
			objOutput.add("Text");
			objInput.add("Text:"+formObject.getWFWorkitemName());
			objInput.add("Text:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1));
			objInput.add("Text:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2));
			objInput.add("Text:"+formObject.getNGValue("cmplx_Decision_Highest_delegauth"));
			objInput.add("Text:"+formObject.getNGValue("cmplx_Decision_Decision"));
			PersonalLoanS.mLogger.info("objInput args are: "+objInput.get(0)+objInput.get(1)+objInput.get(2)+objInput.get(3)+objInput.get(4));
			//Deepak condition added to avoid multiple approval at CAD1 stage
			String cad_dec_query = "select top 2 userName,workstepName,Decision from NG_RLOS_GR_DECISION where dec_wi_name= '"+formObject.getWFWorkitemName()+"' order by dateLastChanged desc";
			List<List<String>> cad_dec_list=formObject.getNGDataFromDataCache(cad_dec_query);
			if(cad_dec_list.size()>1 && cad_dec_list.get(0).get(0).equalsIgnoreCase(cad_dec_list.get(1).get(0)) && cad_dec_list.get(0).get(1).equalsIgnoreCase(cad_dec_list.get(1).get(1)) && cad_dec_list.get(0).get(2).equalsIgnoreCase(cad_dec_list.get(1).get(2)) && "Approve".equalsIgnoreCase(cad_dec_list.get(1).get(2)))
			{
				PersonalLoanS.mLogger.info("same decision added so procedure ng_RLOS_CADLevels is not called");
			}
			else{
				//  below code already exist - 10-10-2017
				// disha CAD deligation hierarchy should not work when decision selected if 'Refer to Credit'
				if ("".equalsIgnoreCase(formObject.getNGValue("CAD_dec_tray")) || "Select".equalsIgnoreCase(formObject.getNGValue("CAD_dec_tray"))){				
					PersonalLoanS.mLogger.info("Before executing procedure ng_RLOS_CADLevels");
					objOutput= formObject.getDataFromStoredProcedure("ng_RLOS_CADLevels", objInput,objOutput);
				}
			}
			//Deepak condition added on 28th July
			if("".equalsIgnoreCase(formObject.getNGValue("CADNext"))){
				if ("".equalsIgnoreCase(formObject.getNGValue("CAD_dec_tray")) || "Select".equalsIgnoreCase(formObject.getNGValue("CAD_dec_tray"))){				
					PersonalLoanS.mLogger.info("Before executing procedure ng_RLOS_CADLevels");
					objOutput= formObject.getDataFromStoredProcedure("ng_RLOS_CADLevels", objInput,objOutput);
				}
			}

			if("Refer".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")) && "DDVT_Maker".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_ReferTo")))
			{
				formObject.setNGValue("ReferTo", formObject.getNGValue("cmplx_Decision_ReferTo"));
				formObject.setNGValue("q_hold1",1);
				formObject.setNGValue("VAR_INT4",1);
				PersonalLoanS.mLogger.info("This is refer decision check for cad1");
				formObject.setNGValue("CA_Refer_DDVT", "Y");
				PersonalLoanS.mLogger.info("This is refer decision check for cad1" + formObject.getNGValue("CA_Refer_DDVT"));
			}
			//code sync 
			if("Approve".equalsIgnoreCase(Decision)&& !("RESC".equalsIgnoreCase(formObject.getNGValue("IS_Approve_Cif"))||"RESN".equalsIgnoreCase(formObject.getNGValue("IS_Approve_Cif")))){
				objInput.clear();
				objInput.add("Text:"+formObject.getWFWorkitemName());
				objInput.add("Text:"+"Waiting for Approval");
				objOutput.clear();
				objOutput.add("Text");
				objOutput= formObject.getDataFromStoredProcedure("ng_EFMS_InsertData", objInput,objOutput);
				PersonalLoanS.mLogger.info("After ng_EFMS_InsertData: objOutput is "+objOutput);
			}
			else{
				PersonalLoanS.mLogger.info("Exclusion for EFMS as per new condition");
				formObject.setNGValue("EFMS","NA");
			}
			//Deepak Changes done for PCASI-2940
			try{
				String app_no = formObject.getWFWorkitemName().substring(formObject.getWFWorkitemName().indexOf("-")+1, formObject.getWFWorkitemName().lastIndexOf("-"));
				String EFMS_case_count = "select COUNT(WI_NAME) as pendingcase_count from NG_EFMS_CONTROLTABLE with(nolock)where WI_NAME = '"+app_no+"' and STATUS = 'Ready'";
				PersonalLoanS.mLogger.info("EFMS_case_count query: "+ EFMS_case_count);
				List<List<String>> EFMS_case_count_list=formObject.getNGDataFromDataCache(EFMS_case_count);
				if(EFMS_case_count_list.size()>0 && Integer.parseInt(EFMS_case_count_list.get(0).get(0))>0)
				{
					PersonalLoanS.mLogger.info("Inside EFMS_case_countcondition to delete the EFMS status from ext table");
					formObject.setNGValue("EFMS_IS_Alerted", "");
					formObject.setNGValue("EFMS_AlertStatusFlag", "");
				}
				
			}
			catch(Exception e){
				
			}

		}
		catch(Exception Ex)
		{
			PersonalLoanS.mLogger.info("Exception in submit form completed!"+Ex.getStackTrace());
		}



	}




	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		CC_waiver_check();
		PersonalLoanS.mLogger.info("Inside PL after  submitFormStarted()" );
		//added by isha for  for Employment Match check PCSP-459
		String cc_waiv_flag = formObject.getNGValue("is_cc_waiver_require");
		if(cc_waiv_flag.equalsIgnoreCase("N")){

			Check_EFC_Limit();
		}
		if("Approve".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")))
		{
			Employment_Match_Check();
			Capture_ATC_Fields();
		}
		PersonalLoanS.mLogger.info("Inside PL PROCESS submitFormStarted()" ); 
		try{
			CustomSaveForm();

			//Deepak Code PSCP-270 moved from CAD 1 to CAD 2
			formObject.setNGValue("CAD_Username", formObject.getUserName());

			formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
			formObject.setNGValue("CAD_ANALYST1_DECISION", formObject.getNGValue("cmplx_Decision_Decision"));
			formObject.setNGValue("Highest_Delegation_CAD", formObject.getNGValue("cmplx_Decision_Highest_delegauth")); 
			formObject.setNGValue("Final_Limit", formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
			formObject.setNGValue("CAD_dec_tray", formObject.getNGValue("cmplx_Decision_CADDecisiontray")); //Arun (07/10)


			PersonalLoanS.mLogger.info("CAD_ANALYST1_DECISION: "+formObject.getNGValue("CAD_ANALYST1_DECISION"));
			if(NGFUserResourceMgr_PL.getGlobalVar("PL_refertoSmartCPV").equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")))
			{
				saveSmartCheckGrid();
			}
			if(NGFUserResourceMgr_PL.getGlobalVar("PL_RefertoCredit").equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")))
			{
				formObject.setNGValue("Cad_Deviation_Tray",formObject.getNGValue("cmplx_Decision_CADDecisiontray")); //Arun (11/10)
				formObject.setNGValue("q_MailTo",formObject.getNGValue("cmplx_Decision_CADDecisiontray"));
			}
			//++ below code aded by disha on 27-3-2018 to set value of var_int4
			if("STP".equalsIgnoreCase(formObject.getNGValue("Highest_Delegation_CAD")))
			{
				PersonalLoanS.mLogger.info("Inside STP ");
				if("Approve".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")))
				{
					PersonalLoanS.mLogger.info("Inside STP Approve CAD1");
					formObject.setNGValue("q_hold1", 1);
					formObject.setNGValue("VAR_INT4",1);

					formObject.setNGValue("q_hold2", 1);
					formObject.setNGValue("VAR_INT7",1);
					PersonalLoanS.mLogger.info("Inside STP Approve " + formObject.getNGValue("q_hold1"));
				}
			}
			if("Refer".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")) && "DDVT_Maker".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_ReferTo")))
			{
				formObject.setNGValue("ReferTo", formObject.getNGValue("cmplx_Decision_ReferTo"));
				formObject.setNGValue("q_hold1",1);
				formObject.setNGValue("VAR_INT4",1);
				PersonalLoanS.mLogger.info("This is refer decision check for cad1");
				formObject.setNGValue("CA_Refer_DDVT", "Y");
			}
			if("Refer".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision"))  || "Reject".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")))
			{
				formObject.setNGValue("CAD_dec_tray", formObject.getNGValue("cmplx_Decision_CADDecisiontray"));
			}
			//changed by nikhil 27/11 to set header
			formObject.setNGValue("Final_Limit", formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
			formObject.setNGValue("LoanLabel",formObject.getNGValue("Final_Limit"));//09-06-2021
			//below code added by shweta for cpv changes seq# 141
			if("Approve Sub to CIF".equalsIgnoreCase(formObject.getNGValue("cmplx_CustDetailVerification_Decision")) && !"C".equalsIgnoreCase("IS_Approve_Cif") && "Approve".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")))
			{
				formObject.setNGValue("IS_Approve_Cif", "Y");
			}
			else
			{
				formObject.setNGValue("IS_Approve_Cif", "N");

			}
			if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_SetReminder_CA")) && "CA_HOLD".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision"))){
				SetReminder_CA(formObject);
			}
			//below code added by shweta for cpv changes  seq# 141

			//Below code added by siva on 24102019 for PCAS-1288
			/*if(formObject.getNGValue("cmplx_Decision_Decision").equalsIgnoreCase("Refer"))
		{
			formObject.setNGValue("CA_Refer_DDVT", "Y");
		}*/
			//ended by siva on 24102019 for PCAS-1288
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

