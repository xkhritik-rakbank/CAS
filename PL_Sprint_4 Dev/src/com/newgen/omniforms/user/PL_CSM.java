/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PL_CSM.java
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

import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;


public class PL_CSM extends PLCommon implements FormListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	//Logger mLogger=PersonalLoanS.mLogger;

	public void formLoaded(FormEvent pEvent)
	{
		FormConfig objConfig = FormContext.getCurrentInstance().getFormConfig();
		objConfig.getM_objConfigMap().put("PartialSave", "true");
		//FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		makeSheetsInvisible("Tab1", "5,6,7,8,9,11,12,13,14,15,16");//Changes done by shweta for jira #1768 to hide CPV tab

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setSheetVisible(tabName, 6, true);	//Show services Request
	}


	public void formPopulated(FormEvent pEvent) 
	{
		FormReference formObject= FormContext.getCurrentInstance().getFormReference();


		try{
			formObject.setNGValue("Mandatory_Frames", NGFUserResourceMgr_PL.getGlobalVar("Cad1_Frame_Name"));
			new PersonalLoanSCommonCode().setFormHeader(pEvent);
			formObject.setVisible("ReferHistory",false);
			String cc_waiv_flag = formObject.getNGValue("is_cc_waiver_require");
			if(cc_waiv_flag !=null && cc_waiv_flag.equalsIgnoreCase("Y")){
				formObject.setLocked("CardDetails_Frame1", true);
			}
			//PCASI - 2694
			String sRMNameHeader = formObject.getNGValue("RM_Name");
			String cmplxRMName = formObject.getNGValue("cmplx_Customer_RM_TL_NAME");
			if("".equalsIgnoreCase(cmplxRMName) || cmplxRMName == null){
				formObject.setNGValue("cmplx_Customer_RM_TL_NAME", sRMNameHeader);
			}
			PersonalLoanS.mLogger.info("sRMNameHeader :: "+sRMNameHeader+" cmplxRMName :: "+cmplxRMName);


		}catch(Exception e)
		{
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

	public void fragment_loaded(ComponentEvent pEvent)
	{	
		FormReference formObject= FormContext.getCurrentInstance().getFormReference();

		PersonalLoanS.mLogger.info( "EventName:Aman" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());


		if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklistCustomer();

			formObject.setEnabled("Customer_save",true);
			formObject.setLocked("cmplx_Customer_ReferrorCode",false);
			formObject.setLocked("cmplx_Customer_ReferrorName",false);
			formObject.setLocked("cmplx_Customer_AppType",false);
			formObject.setVisible("cmplx_Customer_corpcode",false);
			formObject.setVisible("cmplx_Customer_bankwithus",false);
			formObject.setVisible("cmplx_Customer_noofdependent",false);
			formObject.setVisible("Customer_Label32",false);
			formObject.setVisible("Customer_Label33",false);
			formObject.setVisible("Customer_Label34",false);
			formObject.setVisible("Customer_Label44",false);
			/*formObject.setEnabled("Customer_Reference_Add",true);
			formObject.setEnabled("Customer_Reference__modify",true);
			formObject.setEnabled("Customer_Reference_delete",true);
			 */
			// Below code added by abhishek on 08/11/2017
			formObject.setVisible("Customer_Label22", false);
			formObject.setVisible("cmplx_Customer_apptype", false);
			formObject.setVisible("Customer_Label55", true);
			formObject.setVisible("cmplx_Customer_marsoomID", true);
			// Above code added by abhishek on 08/11/2017

			formObject.setLocked("FetchDetails",true);
			formObject.setLocked("Customer_Button1",true);
			PersonalLoanS.mLogger.info("Column Added in Repeater"+" for eida");
			PersonalLoanS.mLogger.info("@@@@@@@@@@@@@@@@@@@@@@@@"+formObject.getNGValue("cmplx_Customer_SecNAtionApplicable_1"));
			if(formObject.getNGValue("cmplx_Customer_SecNAtionApplicable").equalsIgnoreCase("no"))
			{
				formObject.setLocked("cmplx_Customer_SecNationality", true);
				formObject.setLocked("SecNationality_Button", true);
				PersonalLoanS.mLogger.info("second nationality disable");
			}

			if(!"".equals(formObject.getNGValue("cmplx_Customer_NEP"))&& !"--Select--".equals(formObject.getNGValue("cmplx_Customer_NEP")) && formObject.getNGValue("cmplx_Customer_NEP")!=null){
				formObject.setVisible("cmplx_Customer_EIDARegNo",true);
				formObject.setVisible("Customer_Label56",true);
				//formObject.setLocked("cmplx_Customer_EIDARegNo",true);
			}
			else {
				formObject.setVisible("Customer_Label56",false);
				formObject.setVisible("cmplx_Customer_EIDARegNo",false);
			}
			formObject.setLocked("cmplx_Customer_DOb",false);
			formObject.setLocked("cmplx_Customer_IdIssueDate", false);
			formObject.setLocked("cmplx_Customer_EmirateIDExpiry", false);
			formObject.setLocked("cmplx_Customer_VisaIssueDate", false);
			formObject.setLocked("cmplx_Customer_VIsaExpiry", false);
			formObject.setLocked("cmplx_Customer_PassportIssueDate", false);
			formObject.setLocked("cmplx_Customer_PassPortExpiry", false);
			if(isCustomerMinor(formObject)){
				formObject.setNGValue("cmplx_Customer_minor", true);

				//commented by bandana as minor not allowed for PL
				//un-commented by shweta as minors are allowed in PL
				formObject.setVisible("Customer_Label37",true);
				formObject.setVisible("Customer_Label35",true);
				formObject.setVisible("cmplx_Customer_guarname",true);
				formObject.setVisible("cmplx_Customer_guarcif",true);
			} else {
				formObject.setNGValue("cmplx_Customer_minor", false);

			}
			formObject.setLocked("cmplx_Customer_minor", true);
			formObject.setEnabled("cmplx_Customer_IsGenuine",false);
			formObject.setEnabled("cmplx_Customer_NTB",false);
			if("false".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")))
			{
				formObject.setLocked("cmplx_Customer_MobNo", true);
			}
			PersonalLoanS.mLogger.info("Column Added in Repeater"+" after eida");
			if("".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_DSA_NAme"))){
				formObject.setNGValue("cmplx_Customer_DSA_NAme", formObject.getNGValue("DSA_Name"));
				formObject.setNGValue("cmplx_Customer_DSA_Code", formObject.getNGValue("DSA_Name"));

			}
			formObject.setLocked("cmplx_Customer_DSA_NAme",false);
			formObject.setLocked("cmplx_Customer_DSA_Code",false);

			formObject.setEnabled("cmplx_Customer_DSA_NAme", true);
			formObject.setEnabled("cmplx_Customer_DSA_Code", true);
			

		}	
		else if ("GuarantorDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist_Guarantor();
			PersonalLoanS.mLogger.info("Check Id"+" GuarantorDetails");
		}

		else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct with(nolock)");
			//LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_ApplicationType where SubProdFlag = '"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2)+"'");
			LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) order by SCHEMEID");

			formObject.setEnabled("Product_Save",true);
			//	formObject.setEnabled("Product_Add",true);
			formObject.setEnabled("Product_Modify",true);
			//	formObject.setEnabled("Product_Delete",true);
		}

		else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

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
			formObject.setLocked("cmplx_IncomeDetails_RentalIncome",false);
			formObject.setEnabled("cmplx_IncomeDetails_RentalIncome",true);


			//formObject.setLocked("cmplx_IncomeDetails_Is_tenancy_contract", true);//added by akshay on 8/1/17
			formObject.setHeight("Incomedetails", 700);
			formObject.setHeight("IncomeDetails_Frame1", 720);     
			LoadPickList("cmplx_IncomeDetails_StatementCycle", "select '--Select--' union select convert(varchar, description) from NG_MASTER_StatementCycle with (nolock)");

		}

		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setVisible("cmplx_Liability_New_overrideAECB",false);//arun 20/12/17
			formObject.setVisible("cmplx_Liability_New_overrideIntLiab",false);//arun 20/12/17
			formObject.setEnabled("Liability_New_AECBReport",true);
			formObject.setEnabled("Liability_New_Save",true);//
			//formObject.setEnabled("cmplx_Liability_New_AECBconsentAvail",true);
			//added by saurabh on 11 nov 17
			formObject.setLocked("ExtLiability_Button1",true);	
			formObject.setLocked("Liability_New_Button1",true);
			formObject.setLocked("cmplx_Liability_New_overrideIntLiab", true);
			formObject.setLocked("cmplx_Liability_New_overrideAECB", true);
			String App_Type=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 4);
			if (NGFUserResourceMgr_PL.getGlobalVar("PL_RESC").equalsIgnoreCase(App_Type) || NGFUserResourceMgr_PL.getGlobalVar("PL_RESN").equalsIgnoreCase(App_Type) || NGFUserResourceMgr_PL.getGlobalVar("PL_RESR").equalsIgnoreCase(App_Type)){
				formObject.setVisible("Liability_New_Label6", true);
				formObject.setVisible("cmplx_Liability_New_noofpaidinstallments", true);
			}
			else
			{
				formObject.setVisible("Liability_New_Label6", false);
				formObject.setVisible("cmplx_Liability_New_noofpaidinstallments", false);
			}
			LoadPickList("Liability_New_worststatuslast24", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from ng_master_Aecb_Codes with (nolock) order by code");
			formObject.setVisible("ExtLiability_AECBReport",false);
			//formObject.setLocked("cmplx_Liability_New_AECBconsentAvail", true);
			//below code added by abhishek for CSM point 9 on 7/11/2017
			if(NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("ExtLiability_Takeoverindicator"))){
				formObject.setEnabled("ExtLiability_takeoverAMount", true);
			}else{
				formObject.setEnabled("ExtLiability_takeoverAMount", false);
			}
			//above code added by abhishek for CSM point 9 on 7/11/2017
			//below code added by abhishek for CSM point 8 on 7/11/2017
			if(NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("ExtLiability_CACIndicator"))){
				formObject.setEnabled("ExtLiability_QCAmt", true);
				formObject.setEnabled("ExtLiability_QCEMI", true);
			}else{
				formObject.setEnabled("ExtLiability_QCAmt", false);
				formObject.setEnabled("ExtLiability_QCEMI", false);
			}
			//above code added by abhishek for CSM point 8 on 7/11/2017

			LoadPickList("ExtLiability_contractType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_contract_type with (nolock) order by code");
			// Added by aman to load the picklist	
		}
		else if ("ReferenceDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			//formObject.setLocked("ReferenceDetails_Frame1",true);
			LoadPickList("ReferenceDetails_ref_Relationship", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Relationship with (nolock) order by code");

		}
		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {


			PersonalLoanS.mLogger.info("before disableButtonsPL"+formObject.getNGValue("cmplx_EmploymentDetails_Designation"));
			disableButtonsPL("EMploymentDetails");
			PersonalLoanS.mLogger.info("before setALOCListed"+formObject.getNGValue("cmplx_EmploymentDetails_Designation"));

			setALOCListed();
			PersonalLoanS.mLogger.info("before loadPicklistEmployment"+formObject.getNGValue("cmplx_EmploymentDetails_Designation"));

			//loadPicklistEmployment();
			PersonalLoanS.mLogger.info("after loadPicklistEmployment"+formObject.getNGValue("cmplx_EmploymentDetails_Designation"));
			loadPicklist4();
			PersonalLoanS.mLogger.info("after loadPicklist4"+formObject.getNGValue("cmplx_EmploymentDetails_Designation"));

			Fields_ApplicationType_Employment();
			PersonalLoanS.mLogger.info("after Fields_ApplicationType_Employment"+formObject.getNGValue("cmplx_EmploymentDetails_Designation"));
			formObject.setTop("EMploymentDetails_Label41",307);
			formObject.setTop("cmplx_EmploymentDetails_LengthOfBusiness",322);

			// Ref. 1003
			//formObject.setVisible("EMploymentDetails_Label7", false); EMploymentDetails_Label43

			formObject.setVisible("EMploymentDetails_Label7",false);//PCASI-1079
			formObject.setVisible("EmploymentDetails_Bank_Button",false);
			formObject.setVisible("cmplx_EmploymentDetails_OtherBankCAC",false);
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
			formObject.setVisible("EMploymentDetails_CheckBox8", false);
			formObject.setVisible("cmplx_EmploymentDetails_Remarks_PL", false);
			formObject.setVisible("cmplx_EmploymentDetails_dateinCC", false);

			formObject.setVisible("EMploymentDetails_Combo34", false);
			formObject.setVisible("EMploymentDetails_Label17", false);
			formObject.setVisible("cmplx_EmploymentDetails_authsigname", false);
			formObject.setVisible("cmplx_EmploymentDetails_highdelinq", false);
			formObject.setVisible("EMploymentDetails_Label20", false);
			formObject.setVisible("cmplx_EmploymentDetails_dateinPL", false);
			formObject.setVisible("EMploymentDetails_Label21", false);
			formObject.setVisible("cmplx_EmploymentDetails_dateinPL", false);
			formObject.setVisible("EMploymentDetails_Label15", false);
			formObject.setVisible("cmplx_EmploymentDetails_empmovemnt", false);
			formObject.setVisible("EMploymentDetails_Combo35", false);
			formObject.setVisible("EMploymentDetails_Label26", false);
			formObject.setVisible("cmplx_EmploymentDetails_remarks", false);
			formObject.setVisible("EMploymentDetails_Label27", false);
			formObject.setVisible("cmplx_EmploymentDetails_Remarks_PL", false);
			formObject.setVisible("EMploymentDetails_Label10", false);
			formObject.setVisible("cmplx_EmploymentDetails_marketcode", false);
			formObject.setVisible("EMploymentDetails_Label4", false);
			formObject.setVisible("cmplx_EmploymentDetails_PromotionCode", false);
			formObject.setVisible("EMploymentDetails_Label10", false);
			formObject.setVisible("cmplx_EmploymentDetails_marketcode", false);
			/*formObject.setTop("EMploymentDetails_Save", 360);
			formObject.setLeft("EMploymentDetails_Save", 10);*/
			formObject.setVisible("EMploymentDetails_Save", true);
			formObject.setTop("EMploymentDetails_Label71", 108);
			formObject.setTop("cmplx_EmploymentDetails_EmpContractType", 124);
			formObject.setLeft("EMploymentDetails_Label71",1066);
			formObject.setLeft("cmplx_EmploymentDetails_EmpContractType",1066);
			PersonalLoanS.mLogger.info("after all setto and get top"+formObject.getNGValue("cmplx_EmploymentDetails_Designation"));
			if("LIFESUR".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")))
			{//EMploymentDetails_Label41
				PersonalLoanS.mLogger.info("@sagarika emp");
				//EMploymentDetails_Label43
				formObject.setEnabled("cmplx_EmploymentDetails_InsuranceValue", true);
				formObject.setVisible("EMploymentDetails_Label41",false);
				formObject.setVisible("EMploymentDetails_Label43",true);
				formObject.setVisible("cmplx_EmploymentDetails_LengthOfBusiness",false);
				formObject.setEnabled("cmplx_EmploymentDetails_PremAmt",true);
				// formObject.setVisible("EMploymentDetails_Label44", true);
				// formObject.setVisible("cmplx_EmploymentDetails_PremPaid",true);
				formObject.setEnabled("cmplx_EmploymentDetails_PremPaid",true);
				// formObject.setVisible("EMploymentDetails_Label46", true);
				// formObject.setVisible("cmplx_EmploymentDetails_PremType", true);
				formObject.setEnabled("cmplx_EmploymentDetails_PremType", true);
				// formObject.setVisible("EMploymentDetails_Label47", true);
				// formObject.setVisible("cmplx_EmploymentDetails_RegPayment",true);
				formObject.setEnabled("cmplx_EmploymentDetails_RegPayment",true);
				formObject.setEnabled("cmplx_EmploymentDetails_MotorInsurance",false);
				// formObject.setVisible("EMploymentDetails_Label52", true);
				//  formObject.setVisible("cmplx_EmploymentDetails_MinimumWait", true);
				formObject.setTop("EMploymentDetails_Label42",282);
				formObject.setTop("cmplx_EmploymentDetails_InsuranceValue", 297);
				formObject.setTop("EMploymentDetails_Label43", 282);
				formObject.setTop("cmplx_EmploymentDetails_PremAmt", 297);
				formObject.setTop("EMploymentDetails_Label44", 282);//FinacleCore_TypeOfRetutn
				formObject.setTop("cmplx_EmploymentDetails_PremPaid",297);
				formObject.setTop("EMploymentDetails_Label46", 327);//cmplx_emp_ver_sp2_empstatus_remarks
				formObject.setTop("cmplx_EmploymentDetails_PremType", 342);
				formObject.setTop("EMploymentDetails_Label47", 327);
				formObject.setTop("cmplx_EmploymentDetails_RegPayment",327);
				formObject.setTop("EMploymentDetails_Label52", 327);
				formObject.setTop("cmplx_EmploymentDetails_MinimumWait", 342);
			}//Initiation Channel
			if("MOTSUR".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")))
			{//cmplx_EmploymentDetails_MotorInsurance
				PersonalLoanS.mLogger.info("@sagarika motsur");
				//EMploymentDetails_Frame2	
				// formObject.setVisible("EMploymentDetails_Label54", true);

				//  formObject.setVisible("cmplx_EmploymentDetails_MotorInsurance", true);
				formObject.setEnabled("cmplx_EmploymentDetails_MotorInsurance", true);
				formObject.setEnabled("cmplx_EmploymentDetails_InsuranceValue", false);

				formObject.setEnabled("cmplx_EmploymentDetails_PremAmt",false);
				// formObject.setVisible("EMploymentDetails_Label44", true);
				// formObject.setVisible("cmplx_EmploymentDetails_PremPaid",true);
				formObject.setEnabled("cmplx_EmploymentDetails_PremPaid",false);
				// formObject.setVisible("EMploymentDetails_Label46", true);
				// formObject.setVisible("cmplx_EmploymentDetails_PremType", true);
				formObject.setEnabled("cmplx_EmploymentDetails_PremType", false);
				// formObject.setVisible("EMploymentDetails_Label47", true);
				// formObject.setVisible("cmplx_EmploymentDetails_RegPayment",true);
				formObject.setEnabled("cmplx_EmploymentDetails_RegPayment",false);
				//    formObject.setEnabled("cmplx_EmploymentDetails_MotorInsurance",true);
				formObject.setTop("EMploymentDetails_Label54", 327);

				formObject.setTop("cmplx_EmploymentDetails_MotorInsurance",  342);

			}
			//added by akshay on 13/10/17

			formObject.setEnabled("cmplx_EmploymentDetails_Others", true);
			formObject.setLocked("cmplx_EmploymentDetails_Others",false);
			formObject.setVisible("EMploymentDetails_Label32", false);
			formObject.setVisible("cmplx_EmploymentDetails_Field_visit_done", false);
			formObject.setLocked("cmplx_EmploymentDetails_LOS",true);
			if("".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP"))){
				formObject.setLocked("cmplx_EmploymentDetails_NepType",true);
			}
			//p1-7,ALOC fields should be disabled for CSM user
			formObject.setLocked("cmplx_EmploymentDetails_Kompass",true);
			formObject.setLocked("cmplx_EmploymentDetails_IncInPL",true);
			formObject.setLocked("cmplx_EmploymentDetails_IncInPL",true);
			formObject.setLocked("cmplx_EmploymentDetails_EmpIndusSector",true);
			formObject.setLocked("cmplx_EmploymentDetails_Indus_Macro",true);
			formObject.setLocked("cmplx_EmploymentDetails_Indus_Micro",true);
			formObject.setLocked("cmplx_EmploymentDetails_FreezoneName",true);
			formObject.setLocked("cmplx_EmploymentDetails_Freezone",true); 
			formObject.setLocked("cmplx_EmploymentDetails_Kompass",true); 
			//p1-7,ALOC fields should be disabled for CSM user
			// Ref. 1003 end. EMploymentDetails_Frame2 cmplx_EmploymentDetails_InsuranceValue
			//++Below code added by nikhil 7/11/17 as per PL issues sheet
			PersonalLoanS.mLogger.info("ssss-emp");
			formObject.setTop("EMploymentDetails_Save",400);
			//   formObject.setTop("EMploymentDetails_Label54",  327);
			//Initiation Channel
			// formObject.setTop("cmplx_EmploymentDetails_MotorInsurance",  342);
			//cmplx_EmploymentDetails_IndusSeg
			formObject.setVisible("cmplx_EmploymentDetails_StaffID", false);
			formObject.setVisible("cmplx_EmploymentDetails_Dept", false);
			formObject.setVisible("EMploymentDetails_Label28", false);
			formObject.setVisible("EMploymentDetails_Label5", false);
			formObject.setVisible("cmplx_EmploymentDetails_ClassificationCode", false);
			formObject.setVisible("EMploymentDetails_Label36", false);
			formObject.setVisible("cmplx_EmploymentDetails_marketcode", false);
			formObject.setVisible("EMploymentDetails_Label10", false);
			//--Above code added by nikhil 7/11/17 as per PL issues sheet

			//Added by aman to not show collection code at CSM
			String reqProd = formObject.getNGValue("PrimaryProduct");
			String appCategory = formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg");
			String subproduct = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2);
			//change by saurabh for PCSP-23 full target seg code description not appearing on field. 7th Feb 19.
			if(NGFUserResourceMgr_PersonalLoanS.getGlobalVar("PL_PersonalLoan").equalsIgnoreCase(reqProd)){
				if(appCategory!=null && "BAU".equalsIgnoreCase(appCategory)){
					LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  description,code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and BAU='Y' and (product = 'PL' or product='B') order by code");
				}
				else if(appCategory!=null &&  "S".equalsIgnoreCase(appCategory)){
					LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  description,code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and Surrogate='Y' and (product = 'PL' or product='B') order by code");
				}
				else{
					LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  description,code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and (product = 'PL' or product='B') order by code");	
				}
			}
			else if(NGFUserResourceMgr_PersonalLoanS.getGlobalVar("PL_PersonalLoan").equalsIgnoreCase(reqProd)){
				if(appCategory!=null &&  "BAU".equalsIgnoreCase(appCategory)){
					LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  description,code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and BAU='Y' and (product = 'PL' or product='B') order by code");
				}
				else if(appCategory!=null &&  "S".equalsIgnoreCase(appCategory)){
					LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  description,code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and Surrogate='Y' and (product = 'PL' or product='B') order by code");
				}
				else{
					LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  description,code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and (product = 'PL' or product='B') order by code");	
				}
			}

			if(formObject.getNGValue("cmplx_EmploymentDetails_Others")=="true"){
				formObject.setVisible("EMploymentDetails_Label72",false);
				formObject.setVisible("cmplx_EmploymentDetails_EMpCode",false);
			}
			else 
			{
				formObject.setVisible("EMploymentDetails_Label72",true);
				formObject.setVisible("cmplx_EmploymentDetails_EMpCode",true);
			}

			PersonalLoanS.mLogger.info("end of fetchfrag of employment"+formObject.getNGValue("cmplx_EmploymentDetails_Designation"));
			formObject.setHeight("EMploymentDetails_Frame2",800);



		}

		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklistELigibiltyAndProductInfo();

			PersonalLoanS.mLogger.info("EMI value is: "+formObject.getNGValue("cmplx_EligibilityAndProductInfo_EMI"));
			String appType = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 4);
			PersonalLoanS.mLogger.info("appTyp value is: "+appType);
			PersonalLoanS.mLogger.info("cmplx_EligibilityAndProductInfo_BaseRateType cmplx_EligibilityAndProductInfo_BaseRateType"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_BaseRateType"));
			fetch_CardDetails_frag(formObject);
			String cc_waiv_flag = formObject.getNGValue("is_cc_waiver_require");
			if(cc_waiv_flag.equalsIgnoreCase("Y")){
				formObject.setLocked("CardDetails_Frame1", true);
			}



			//formObject.setLocked("ELigibiltyAndProductInfo_Frame7",true);
			//30Nov17 code was available at Offshore without any comment and the same was not present at offshore start.
			formObject.setLocked("ELigibiltyAndProductInfo_Frame7",true);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_InterestRate", false);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_Tenor", false);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_RepayFreq", true);
			formObject.setVisible("ELigibiltyAndProductInfo_Label11", true);

			//30Nov17 code was available at Offshore without any comment and the same was not present at offshore start.
			formObject.setEnabled("ELigibiltyAndProductInfo_Save",true);
			formObject.setTop("ELigibiltyAndProductInfo_Frame7",648);//Arun (24/09/17)
			formObject.setTop("ELigibiltyAndProductInfo_Save",830);//Arun (24/09/17)
			formObject.setHeight("ELigibiltyAndProductInfo_Frame1", 1350);
			//below code added by abhishek for CSM point 16 on 7/11/2017
			//changes done by saurabh on 11 nov17
			formObject.setLocked("cmplx_EligibilityAndProductInfo_RepayFreq", false);
			//formObject.setEnabled("cmplx_EligibilityAndProductInfo_InterestType",true);
			//formObject.setEnabled("cmplx_EligibilityAndProductInfo_RepayFreq",true);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_FirstRepayDate", false);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_Moratorium", false);
			formObject.setEnabled("cmplx_EligibilityAndProductInfo_Moratorium", true);
			//above code added by abhishek for CSM point 16 on 7/11/2017
			formObject.setTop("cmplx_EmploymentDetails_PremPaid",297);
			//below code added by abhishek for CSM point 15 on 7/11/2017

			//by saurabh on 11 nov 17.
			PersonalLoanS.mLogger.info("cmplx_EligibilityAndProductInfo_BaseRateType2 cmplx_EligibilityAndProductInfo_BaseRateType"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_BaseRateType"));


			PersonalLoanS.mLogger.info("cmplx_EligibilityAndProductInfo_BaseRateType3 cmplx_EligibilityAndProductInfo_BaseRateType"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_BaseRateType"));
			formObject.setVisible("ELigibiltyAndProductInfo_Label1", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_EFCHidden", false);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_EFCHidden", true);
			loadEligibilityData();


			PersonalLoanS.mLogger.info("cmplx_EligibilityAndProductInfo_BaseRateType6 cmplx_EligibilityAndProductInfo_BaseRateType"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_BaseRateType"));
			PersonalLoanS.mLogger.info("EMI value is: "+formObject.getNGValue("cmplx_EligibilityAndProductInfo_EMI"));
			//formObject.setLocked("cmplx_EligibilityAndProductInfo_NetPayout", false);
			//formObject.setEnabled("cmplx_EligibilityAndProductInfo_NetPayout", true);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_InstrumentType",false);
			formObject.setVisible("ELigibiltyAndProductInfo_Refresh",false);
			formObject.setVisible("ELigibiltyAndProductInfo_Save",true);
			formObject.setTop("ELigibiltyAndProductInfo_Save",350);
			formObject.setLeft("ELigibiltyAndProductInfo_Save",24);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_EFCHidden", true);
			formObject.setVisible("ELigibiltyAndProductInfo_Label1",true);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_EFCHidden", true);


		}

		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist_Address();

			formObject.setEnabled("AddressDetails_Save",true);
			formObject.setEnabled("AddressDetails_addr_Add",true);
			formObject.setEnabled("AddressDetails_addr_Modify",true);
			formObject.setEnabled("AddressDetails_addr_Delete",true);
		}

		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())){

			formObject.setEnabled("AltContactDetails_ContactDetails_Save",true);
			formObject.setVisible("AlternateContactDetails_AirArabiaIdentifier", false);
			formObject.setVisible("AltContactDetails_Label7", false);
			formObject.setVisible("AlternateContactDetails_EnrollRewardsIdentifier", false);
			LoadpicklistAltcontactDetails();
			//below code added by siva on 01112019 for PCAS-1401
			AirArabiaValid();
			enrollrewardvalid();
			//Code ended by siva on 01112019 for PCAS-1401
		} 

		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())){

			Loadpicklistfatca();
			formObject.setEnabled("FATCA_Save",true);
		}

		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())){

			loadPicklist_KYC();
			formObject.setEnabled("KYC_Save",true);
		}

		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())){
			loadPickListOECD();
			formObject.setEnabled("OECD_Save",true);
		}
		else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			//disableButtonsCC("SupplementCardDetails");
			//added by akshay on 9/1/18 for proc 3507
			formObject.setVisible("SupplementCardDetails_Label19",false);
			formObject.setVisible("SupplementCardDetails_Profession",false);
			formObject.setVisible("SupplementCardDetails_Label20",false);
			formObject.setVisible("SupplementCardDetails_DLNo",false);
			formObject.setVisible("SupplementCardDetails_Label27",false);
			formObject.setVisible("SupplementCardDetails_VisaNo",false);
			formObject.setVisible("SupplementCardDetails_Label11",false);
			formObject.setVisible("SupplementCardDetails_PassportExpiry",false);
			formObject.setVisible("SupplementCardDetails_Label23",false);
			formObject.setVisible("SupplementCardDetails_VisaExpiry",false);
			formObject.setVisible("SupplementCardDetails_Label17",false);
			formObject.setVisible("SupplementCardDetails_EmiratesIDExpiry",false);
			formObject.setVisible("SupplementCardDetails_Label25",false);
			formObject.setVisible("SupplementCardDetails_ReceivedDate",false);
			formObject.setVisible("SupplementCardDetails_Label29",false);
			formObject.setVisible("SupplementCardDetails_MarketingCode",false);
			formObject.setVisible("SupplementCardDetails_Label30",false);
			formObject.setVisible("SupplementCardDetails_ApprovalLevelCode",false);
			formObject.setVisible("SupplementCardDetails_Label24",false);
			formObject.setVisible("SupplementCardDetails_AppRefNo",false);
			formObject.setVisible("SupplementCardDetails_Label31",false);
			formObject.setVisible("SupplementCardDetails_FeeProfile",false);
			formObject.setVisible("SupplementCardDetails_Label10",false);
			formObject.setVisible("SupplementCardDetails_Title",false);
			formObject.setVisible("SupplementCardDetails_Label8",false);
			formObject.setVisible("SupplementCardDetails_PersonalInfo",false);
			formObject.setVisible("SupplementCardDetails_Non_Resident",false);
			//formObject.setVisible("SupplementCardDetails_Label32",false);
			formObject.setVisible("SupplementCardDetails_EmpType",false);
			//formObject.setVisible("SupplementCardDetails_Label33",false);
			//formObject.setVisible("SupplementCardDetails_IDIssueDate",false);
			//formObject.setVisible("SupplementCardDetails_Label35",false);
			//formObject.setVisible("SupplementCardDetails_PassportIssueDate",false);
			//formObject.setVisible("SupplementCardDetails_Label36",false);
			//formObject.setVisible("SupplementCardDetails_VisaIssueDate",false);
			formObject.setVisible("SupplementCardDetails_Label37",false);
			//formObject.setVisible("SupplementCardDetails_MaritalStatus",false);
			formObject.setVisible("SupplementCardDetails_referenceName",false);
			formObject.setVisible("SupplementCardDetails_Label39",false);
			formObject.setVisible("SupplementCardDetails_EMploymentType",false);
			formObject.setVisible("SupplementCardDetails_Label38",false);
			formObject.setVisible("SupplementCardDetails_referenceMobile",false);
			formObject.setVisible("SupplementCardDetails_referenceName",false);
			formObject.setVisible("SupplementCardDetails_Label39",false);
			formObject.setVisible("SupplementCardDetails_EMploymentType",false);
			formObject.setVisible("SupplementCardDetails_Label38",false);
			formObject.setVisible("SupplementCardDetails_referenceMobile",false);



			formObject.setTop("SupplementCardDetails_Add", formObject.getTop("SupplementCardDetails_MaritalStatus")+formObject.getHeight("SupplementCardDetails_MaritalStatus")+30);
			formObject.setTop("SupplementCardDetails_Modify", formObject.getTop("SupplementCardDetails_MaritalStatus")+formObject.getHeight("SupplementCardDetails_MaritalStatus")+30);
			formObject.setTop("SupplementCardDetails_Delete", formObject.getTop("SupplementCardDetails_MaritalStatus")+formObject.getHeight("SupplementCardDetails_MaritalStatus")+30);
			formObject.setTop("SupplementCardDetails_cmplx_SupplementGrid", formObject.getTop("SupplementCardDetails_Add")+formObject.getHeight("SupplementCardDetails_Add")+20);
			formObject.setTop("SupplementCardDetails_Save", formObject.getTop("SupplementCardDetails_cmplx_SupplementGrid")+formObject.getHeight("SupplementCardDetails_cmplx_SupplementGrid")+20);
			formObject.setHeight("SupplementCardDetails_Frame1", formObject.getTop("SupplementCardDetails_Save")+formObject.getHeight("SupplementCardDetails_Save")+20);
			formObject.setHeight("Supplementary_Card_Details",formObject.getHeight("SupplementCardDetails_Frame1")+20);
			//adjustFrameTops("Address_Details_container,Alt_Contact_container,Card_Details,Supplementary_Cont,FATCA,KYC,OECD,Reference_Details");//added by akshay on 9/1/17
			loadPicklist_suppCard();
			//adjustFrameTops("Card_Details,Supplementary_Cont,FATCA,KYC,OECD,Reference_Details");//added by akshay on 9/1/17

		}
		else if("CardDetails".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			disableButtonsPL("CardDetails");
			formObject.setVisible("CardDetails_BankName", false);
			formObject.setVisible("CardDetails_bankName_Button", false);
			formObject.setVisible("CardDetails_Label9", false);
			formObject.setVisible("CardDetails_ChequeNumber", false);
			formObject.setVisible("CardDetails_Label10", false);
			formObject.setVisible("CardDetails_Amount", false);
			formObject.setVisible("CardDetails_Label11", false);
			formObject.setVisible("CardDetails_Date", false);
			formObject.setVisible("CardDetails_Label13", false);
			formObject.setVisible("cmplx_CardDetails_CustClassification", false);
			formObject.setVisible("CardDetails_Button2", false);
			formObject.setVisible("CardDetails_Button3", false);
			formObject.setVisible("CardDetails_Button4", false);
			formObject.setVisible("cmplx_CardDetails_cmpmx_gr_cardDetails", false);
			formObject.setVisible("CardDetails_CardProduct", false);
			formObject.setVisible("CardDetails_Label14", false);
			formObject.setVisible("CardDetails_ECRN", false);
			formObject.setVisible("CardDetails_Label15", false);
			formObject.setVisible("CardDetails_Label16", false);
			formObject.setVisible("CardDetails_CRN", false);
			formObject.setVisible("CardDetails_TransactionFP", false);
			formObject.setVisible("CardDetails_Label17", false);
			formObject.setVisible("CardDetails_InterestFP", false);
			formObject.setVisible("CardDetails_Label18", false);
			formObject.setVisible("CardDetails_Label19", false);
			formObject.setVisible("CardDetails_FeeProfile", false);
			formObject.setVisible("CardDetails_Button1", false);
			formObject.setVisible("CardDetails_Button5", false);
			formObject.setVisible("CardDetails_Label8", false);
			formObject.setVisible("CardDetails_Label4", false);
			formObject.setVisible("CardDetails_add", false);
			formObject.setVisible("CardDetails_modify", false);
			formObject.setVisible("CardDetails_delete", false);
			formObject.setVisible("cmplx_CardDetails_charityamt", false);
			formObject.setVisible("cmplx_CardDetails_suppcardreq", true);
			formObject.setVisible("CardDetails_Label5", true);
			formObject.setVisible("cmplx_CardDetails_smsopt", false);
			//added by saurabh on 2 Sept PROC - 12759
			formObject.setVisible("cmplx_CardDetails_securitycheck",false);
			//formObject.setVisible("CardDetails_Label7", false);
			//formObject.setVisible("cmplx_CardDetails_statcycle", false);
			formObject.setVisible("cmplx_CardDetails_securitycheck", false);
			formObject.setVisible("CardDetails_Label12", false);
			formObject.setVisible("cmplx_CardDetails_MarketCode", false);
			formObject.setVisible("cmplx_CardDetails_cmplx_CardCRNDetails", false);
			formObject.setVisible("cmplx_CardDetails_charityorg", false);
			formObject.setVisible("CardDetails_Label3", false);
			formObject.setTop("CardDetails_Save", 200);
			formObject.setHeight("CardDetails_Frame1", formObject.getTop("CardDetails_Save")+formObject.getHeight("CardDetails_Save")+20);
			formObject.setHeight("Card_Details", formObject.getHeight("CardDetails_Frame1")+20);
			adjustFrameTops("Card_Details,Supplementary_Card_Details,FATCA,KYC,OECD,ReferenceDetails");
			Load_Self_Supp_Data();
		}

		//code change by bandana starts
		//++Below code added by nikhil 8/11/17 as per CC issues sheet
		else if ("CC_Loan".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			PersonalLoanS.mLogger.info( "Activity name is:" + formObject.getNGValue("cmplx_CC_Loan_DDSMode"));

			//formObject.setLocked("chequeNo",true);
			//formObject.setLocked("chequeStatus",true);
			loadPicklist_ServiceRequest();
			PersonalLoanS.mLogger.info( "Activity name is:" + formObject.getNGValue("cmplx_CC_Loan_DDSMode"));
			//below code added by nikhil 19/1/18
			String dds_mode=formObject.getNGValue("cmplx_CC_Loan_DDSMode");
			//below code also fix point "30-Service Details#Validations not as per FSD."
			if("false".equals(formObject.getNGValue("cmplx_CC_Loan_DDSFlag")))
			{
				formObject.setLocked("cmplx_CC_Loan_DDSMode",true);
				formObject.setLocked("cmplx_CC_Loan_DDSAmount",true);
				formObject.setLocked("cmplx_CC_Loan_Percentage",true);
				formObject.setLocked("cmplx_CC_Loan_DDSExecDay",true);
				formObject.setLocked("cmplx_CC_Loan_AccType",true);
				formObject.setLocked("cmplx_CC_Loan_DDSIBanNo",true);
				formObject.setLocked("cmplx_CC_Loan_DDSStartdate",true);
				formObject.setLocked("cmplx_CC_Loan_DDSBankAName",true);
				formObject.setLocked("cmplx_CC_Loan_DDSEntityNo",true);
				formObject.setLocked("DDS_save",true);
			}

			else if("F".equalsIgnoreCase(dds_mode) || "Flat".equalsIgnoreCase(dds_mode))
				//above code also fix point "30-Service Details#Validations not as per FSD."

			{

				formObject.setLocked("cmplx_CC_Loan_Percentage",true);
				formObject.setLocked("cmplx_CC_Loan_DDSAmount",false);
			}
			//below code also fix point "30-Service Details#Validations not as per FSD."
			else if("P".equalsIgnoreCase(dds_mode) || "Per".equalsIgnoreCase(dds_mode))
				//above code also fix point "30-Service Details#Validations not as per FSD."

			{
				formObject.setLocked("cmplx_CC_Loan_DDSAmount",true);
				formObject.setLocked("cmplx_CC_Loan_Percentage",false);
			}
			else
			{
				formObject.setLocked("cmplx_CC_Loan_DDSAmount",false);
				formObject.setLocked("cmplx_CC_Loan_Percentage",false);
			}

			if(formObject.getNGValue("cmplx_CC_Loan_HoldType").equalsIgnoreCase("T")){
				formObject.setLocked("cmplx_CC_Loan_HoldFrom_Date",false);
				formObject.setLocked("cmplx_CC_Loan_HOldTo_Date",false);
			}
			else{
				formObject.setLocked("cmplx_CC_Loan_HoldFrom_Date",true);
				formObject.setLocked("cmplx_CC_Loan_HOldTo_Date",true);
			}
			if(NGFUserResourceMgr_PL.getGlobalVar("PL_F").equalsIgnoreCase(formObject.getNGValue("cmplx_CC_Loan_ModeOfSI"))){
				formObject.setLocked("cmplx_CC_Loan_FlatAMount",false);
				formObject.setLocked("cmplx_CC_Loan_SI_Percentage",true);

			}
			else if(NGFUserResourceMgr_PL.getGlobalVar("PL_P").equalsIgnoreCase(formObject.getNGValue("cmplx_CC_Loan_ModeOfSI"))){
				formObject.setLocked("cmplx_CC_Loan_FlatAMount",true);
				formObject.setLocked("cmplx_CC_Loan_SI_Percentage",false);

			}
			else{
				formObject.setLocked("cmplx_CC_Loan_FlatAMount",true);
				formObject.setLocked("cmplx_CC_Loan_SI_Percentage",true);
			}
			//++ Below Code added By Nikhil on Oct 6, 2017  to fix : "12-Percnetage to be enabled when DDS mode is Percentage" : Reported By Shashank on Oct 05, 2017++
			//below code also fix point "13-Flat amount to be enabled and mandatory when DDS mode is flat amount"
			//formObject.setNGValue("cmplx_CC_Loan_DDSMode", "--Select--");
			//-- Above Code added By Nikhil on Oct 6, 2017  to fix : "12-Percnetage to be enabled when DDS mode is Percentage" : Reported By Shashank on Oct 05, 2017--


		}
		//code change by bandana ends

		/*else if ("IncomingDoc".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			fetchIncomingDocRepeater();
			formObject.setVisible("cmplx_DocName_OVRemarks", false);
			formObject.setVisible("cmplx_DocName_OVDec",false);
			formObject.setVisible("cmplx_DocName_Approvedby",false);
		}*/
		else if ("IncomingDocNew".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			fetchIncomingDocRepeaterNew();

			PersonalLoanS.mLogger.info("***********Inside IncomingDocNew of csm");
			formObject.setHeight("IncomeDetails_Frame1", 850);
			formObject.setHeight("Incomedetails", 870);
			formObject.setHeight("Inc_Doc",850);
		}
		//disha FSD
		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			//below code added by nikhil 11/11/17
			notepad_load();
			notepad_withoutTelLog();

		}

		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("DecisionHistory_DecisionReasonCode",false); // As the fields is locked not disabled
			formObject.setLocked("cmplx_Decision_ReferTo",true);
			//	formObject.setVisible("DecisionHistory_chqbook",false);
			formObject.setLocked("cmplx_Decision_New_CIFNo",true);
			formObject.setLocked("cmplx_Decision_IBAN",true);
			/*if(formObject.isVisible("IncomingDoc_Frame1")==false){
				formObject.fetchFragment("Inc_Doc", "IncomingDoc", "q_IncomingDoc");
			}*/
			//disha FSD
			//for decision fragment made changes 8th dec 2017
			PersonalLoanS.mLogger.info("***********Inside decision history of csm");
			//changes by saurabh on 19th Dec #DecisionHistory_Button2 and DecisionHistory_Label26 removed from below list
			fragment_ALign("DecisionHistory_Label10,cmplx_Decision_New_CIFNo#DecisionHistory_Label7,cmplx_Decision_AccountNo#DecisionHistory_Label6,cmplx_Decision_IBAN#Decision_Label1,cmplx_Decision_VERIFICATIONREQUIRED#\n#cmplx_Decision_MultipleApplicantsGrid#\n#Decision_Label3,cmplx_Decision_Decision#DecisionHistory_Label1,cmplx_Decision_ReferTo#DecisionHistory_Label11,DecisionHistory_DecisionReasonCode#Decision_Label4,cmplx_Decision_REMARKS#\n#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line

			formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
			formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);

			PersonalLoanS.mLogger.info("***********Inside after fragment alignment decision history of csm");

			formObject.setNGValue("cmplx_Decision_Decision", "--Select--");
			loadPicklist1();
			//formObject.setVisible("cmplx_Decision_Referralreason",false);


			PersonalLoanS.mLogger.info("***********Inside after making field visible");
			//setDataInMultipleAppGrid();

		} 	


	}

	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException {

		//	PersonalLoanS.mLogger.info("Inside PL_Initiation eventDispatched()-->EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		switch(pEvent.getType()) {

		case FRAME_EXPANDED:
			//PersonalLoanS.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			new PersonalLoanSCommonCode().FrameExpandEvent(pEvent);					
			break;

		case FRAGMENT_LOADED:
			if("Y".equalsIgnoreCase(formObject.getNGValue("IS_Approve_Cif")))
			{
				new PersonalLoanSCommonCode().LockFragmentsOnLoad(pEvent);
				Unlock_ATC_Fields();
			}
			else
			{
				fragment_loaded(pEvent);
			}
			break;

		case MOUSE_CLICKED:
			new PersonalLoanSCommonCode().mouse_Clicked(pEvent);
			break;


		case VALUE_CHANGED:
			new PersonalLoanSCommonCode().value_Change(pEvent);
			break;

		default: break;

		}

	}	


	public void initialize() {
		PersonalLoanS.mLogger.info( "Inside PL PROCESS initialize()" );

	}


	public void saveFormCompleted(FormEvent pEvent) throws ValidatorException {
		PersonalLoanS.mLogger.info( "Inside PL PROCESS saveFormCompleted()" + pEvent.getSource());
		CustomSaveForm();

	}


	public void saveFormStarted(FormEvent pEvent) throws ValidatorException {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PersonalLoanS.mLogger.info( "Inside PL PROCESS submitFormStarted()" + pEvent.getSource());
		formObject.setNGValue("Final_Limit", formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
		PersonalLoanS.mLogger.info( "Inside PL PROCESS saveFormStarted() CMS: PLHidden" + formObject.getNGValue("cmplx_EligibilityAndProductInfo_PLHidden"));
		//Deepak Changes for Loan amount
		formObject.setNGValue("Loan_Amount", formObject.getNGValue("cmplx_EligibilityAndProductInfo_PLHidden"));
		PersonalLoanS.mLogger.info( "Loan_Amount: " + formObject.getNGValue("Loan_Amount"));		
	}


	public void submitFormCompleted(FormEvent pEvent) throws ValidatorException {
		PersonalLoanS.mLogger.info( "Inside PL PROCESS submitFormCompleted()" + pEvent.getSource());
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PersonalLoanS.mLogger.info( "Base Rate Type" + formObject.getNGValue("cmplx_EligibilityAndProductInfo_BaseRateType"));
		//++ below code added by abhishek on 23/01/2018 for Reject data functionality

		PersonalLoanS.mLogger.info("Inside PL PROCESS ddvtchecker submitFormCompleted()" + pEvent.getSource()); 
		//FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		List<String> objInput=new ArrayList<String>();

		objInput.add("Text:"+formObject.getWFWorkitemName());

		PersonalLoanS.mLogger.info("objInput args are: "+objInput.get(0));


		List<Object> objOutput=new ArrayList<Object>();

		objOutput.add("Text");
		PersonalLoanS.mLogger.info("Before executing procedure ng_RLOS_CheckWriteOff");
		objOutput= formObject.getDataFromStoredProcedure("ng_CAS_RejectData", objInput,objOutput);

		//++ above code added by abhishek on 23/01/2018 for Reject data functionality

	}


	public void submitFormStarted(FormEvent pEvent) throws ValidatorException {
		try{
			PersonalLoanS.mLogger.info( "Inside PL PROCESS submitFormStarted()" + pEvent.getSource());

			FormReference formObject = FormContext.getCurrentInstance().getFormReference();

			CustomSaveForm();
			formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
			//Deepak Changes for Loan amount
			formObject.setNGValue("Loan_Amount", formObject.getNGValue("cmplx_EligibilityAndProductInfo_PLHidden"));

			//String query_scheme="select top 1 SCHEMEDESC from NG_master_Scheme where SCHEMeID = +'""'+";
			//below code by jahnavi to map SCHEM with scheme picklist
			String prodGridId = NGFUserResourceMgr_PL.getGlobalVar("PL_productgrid");
			int prd_count=formObject.getLVWRowCount(prodGridId);
			String scheme = "";
			if(prd_count>0){
				scheme = formObject.getNGValue(prodGridId,0,7);
			}
			String map_scheme="select top 1 SCHEMEDESC from NG_master_Scheme where SCHEMeID = '"+scheme+"'";
			List<List<String>> result = formObject.getDataFromDataSource(map_scheme);
			PersonalLoanS.mLogger.info("scheme query: "+map_scheme);
			if(result!=null && !result.isEmpty()){
				formObject.setNGValue("SCHEM", result.get(0).get(0));
				formObject.setNGValue("SCHEME", result.get(0).get(0));
				PersonalLoanS.mLogger.info("scheme"+result.get(0).get(0));

			}		
			if("Approve-parallel processing".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")))
			{
				PersonalLoanS.mLogger.info( "Inside IF");

				formObject.setNGValue("parallel_sequential","P");
				PersonalLoanS.mLogger.info( "Inside IF" +formObject.getNGValue("parallel_sequential"));
			}
			else
			{

				PersonalLoanS.mLogger.info( "Inside else");
				formObject.setNGValue("parallel_sequential","S");
				PersonalLoanS.mLogger.info("Inside  approve DDVT checker");
				formObject.setNGValue("q_parllel_seq", 1);
				PersonalLoanS.mLogger.info( "Inside IF" +formObject.getNGValue("parallel_sequential"));

			}

			//change by saurabh on 29th Jan
			//IncomingDoc();
			/*if("Parallel".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")))
		{
			formObject.setNGValue("parallel_sequential","P");
		}
		else
		{
			formObject.setNGValue("parallel_sequential","S");
		}*/
			String fName = formObject.getNGValue("cmplx_Customer_FIrstNAme");
			String mName = formObject.getNGValue("cmplx_Customer_MiddleName");
			String lName = formObject.getNGValue("cmplx_Customer_LAstNAme");
			String emiratesId = formObject.getNGValue("cmplx_Customer_EmiratesID");
			String cifId = formObject.getNGValue("cmplx_Customer_CIFNO");
			String fullName = ""; 
			if("".equals(mName)){
				fullName = fName+" "+lName;
			}
			else{
				fullName = fName+" "+mName+" "+lName;
			}
			PersonalLoanS.mLogger.info("Final val of fullName:"+ fullName);
			formObject.setNGValue("DecisionHistory_Customer_Name",fullName);
			formObject.setNGValue("DecisionHistory_CIFid",cifId);
			formObject.setNGValue("DecisionHistory_Emiratesid",emiratesId);
			formObject.setNGValue("CUSTOMERNAME",fullName);

			validateStatusForSupplementary();
			saveIndecisionGridCSM();//Arun (01-12-17) for Decision history to appear in the grid

			LoadReferGrid();
			//below code added by nikhil 8/12/17
			IncomingDoc();

			PersonalLoanS.mLogger.info( "Base Rate Type" + formObject.getNGValue("cmplx_EligibilityAndProductInfo_BaseRateType"));

		}
		catch(Exception e){
			PersonalLoanS.mLogger.info( "IException is submit form completedeasdasdasdasdasdas");
			printException(e);

		}
	}	


	public void continueExecution(String arg0, HashMap<String, String> arg1) {

		//empty method
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
