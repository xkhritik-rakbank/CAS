/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PL_DDVT_Checker.java
Author                                                                        : Disha
Date (DD/MM/YYYY)                                      						  : 
Description                                                                   : 

------------------------------------------------------------------------------------------------------------------------------------------------------
CHANGE HISTORY 
------------------------------------------------------------------------------------------------------------------------------------------------------

Problem No/CR No   Change Date   Changed By    Change Description

------------------------------------------------------------------------------------------------------*/
package com.newgen.omniforms.user;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;
import org.apache.log4j.Logger;

import com.newgen.custom.Common_Utils;
import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;

public class PL_DDVT_Checker extends PLCommon implements FormListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	Logger mLogger=PersonalLoanS.mLogger;
	
	public void formLoaded(FormEvent pEvent)
	{
		FormConfig objConfig = FormContext.getCurrentInstance().getFormConfig();
        objConfig.getM_objConfigMap().put("PartialSave", "true");
        makeSheetsInvisible("Tab1", "8,9,16");//Changes done by shweta for jira #1768 to hide CPV tab//HIde Dispatch
        FormReference formObject = FormContext.getCurrentInstance().getFormReference();
        formObject.setSheetVisible(tabName, 10, true); //Show Document Tab
	}


	public void formPopulated(FormEvent pEvent) 
	{	
		String alert_msg ="";
		int count=0;
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try{
			new PersonalLoanSCommonCode().setFormHeader(pEvent);

			//by Shweta
			formObject.setNGValue("Mandatory_Frames", NGFUserResourceMgr_PL.getGlobalVar("DDVT_Frame_Name"));
			if (formObject.getNGValue("RefFrmDDVT").equals("Y") ){
				formObject.setSheetVisible(tabName,8,true);
			}
			if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")))
			{
				formObject.setNGValue("Mandatory_Frames", NGFUserResourceMgr_PL.getGlobalVar("DDVT_Frame_Name"));
			}
			String cc_waiv_flag = formObject.getNGValue("is_cc_waiver_require");
			if(cc_waiv_flag.equalsIgnoreCase("N")){
				formObject.setVisible("Card_Details", true);
			}
			PersonalLoanS.mLogger.info("inside form populated outside wrldcheck at ddvtC");


			String activityName = formObject.getWFActivityName();
			PersonalLoanS.mLogger.info("activity"+activityName);
			if("DDVT_Checker".equalsIgnoreCase(activityName))
			{
				formObject.fetchFragment("World_Check", "WorldCheck1", "q_WorldCheck");
				formObject.setNGFrameState("World_Check",0);
				//formObject.setNGFrameState("WorldCheck1_Frame1",0);
				PersonalLoanS.mLogger.info("Inside 1st IF");
				count = formObject.getLVWRowCount("cmplx_WorldCheck_WorldCheck_Grid");
				PersonalLoanS.mLogger.info("Count"+count);
			}
			PersonalLoanS.mLogger.info("Alert"+alert_msg);

		}catch(Exception e)
		{
			PersonalLoanS.mLogger.info( "Exception:"+e.getMessage());
			printException(e);
		}
		if(count>0)
		{
			PersonalLoanS.mLogger.info("after count");	
			alert_msg=NGFUserResourceMgr_PL.getAlert("VAL1001");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		//PCASI-3719
		float riskRating=0;
		try{
			formObject.fetchFragment("Risk_Rating", "RiskRating", "q_riskrating");
			PersonalLoanS.mLogger.info("Checking risk rating :: "+formObject.getNGValue("cmplx_RiskRating_Total_riskScore"));
			riskRating = Float.parseFloat(formObject.getNGValue("cmplx_RiskRating_Total_riskScore"));
			PersonalLoanS.mLogger.info("riskRating :: "+riskRating);
		}catch(Exception e){
			PersonalLoanS.mLogger.info("Exception in form populated risk check :: "+e);
		}
		if(riskRating >= 4){
			PersonalLoanS.mLogger.info("High Risk case");
			alert_msg=NGFUserResourceMgr_PL.getAlert("VAL1002");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		PersonalLoanS.mLogger.info("End of form populated");

	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Load fragment      

	 ***********************************************************************************  */

	public void fragment_loaded(ComponentEvent pEvent){
		FormReference formObject= FormContext.getCurrentInstance().getFormReference();
		PersonalLoanS.mLogger.info("In PL_Initiation eventDispatched()-->EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		Common_Utils common=new Common_Utils(mLogger);


		if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadView(pEvent.getSource().getName());
			formObject.setLocked("Customer_Frame1",true);
			formObject.setLocked("cmplx_Customer_NEP", true);
			formObject.setLocked("cmplx_Customer_DOb", true);
			formObject.setLocked("cmplx_Customer_IdIssueDate", true);
			formObject.setLocked("cmplx_Customer_EmirateIDExpiry", true);
			formObject.setLocked("cmplx_Customer_VisaIssueDate", true);
			formObject.setLocked("cmplx_Customer_PassportIssueDate", true);
			formObject.setLocked("cmplx_Customer_VIsaExpiry", true);
			formObject.setLocked("cmplx_Customer_PassPortExpiry", true);
			formObject.setVisible("Customer_Label32",false);
			formObject.setVisible("cmplx_Customer_corpcode",false); // pcasi3592
			loadPicklistCustomer();		
			if(!"".equals(formObject.getNGValue("cmplx_Customer_NEP"))&& !"--Select--".equals(formObject.getNGValue("cmplx_Customer_NEP")) && formObject.getNGValue("cmplx_Customer_NEP")!=null){
				formObject.setVisible("cmplx_Customer_EIDARegNo",true);
				formObject.setVisible("Customer_Label56",true);
				//formObject.setLocked("cmplx_Customer_EIDARegNo",true);
			}
			else {
				formObject.setVisible("Customer_Label56",false);
				formObject.setVisible("cmplx_Customer_EIDARegNo",false);
			}
		}	

		else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Product_Frame1",true);
			loadPicklistProduct("Personal Loan");
		}
		
		//Changes done for code optimization 25/07(else if removed because duplicate)
		
		else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("IncomeDetails_Frame1",true);
			LoadPickList("cmplx_IncomeDetails_StatementCycle", "select '--Select--' union select convert(varchar, description) from NG_MASTER_StatementCycle with (nolock)");
			
			LoadPickList("cmplx_IncomeDetails_AvgBalFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_CreditTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_AvgCredTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_AnnualRentFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
		}

		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ExtLiability_Frame1",true);
			LoadPickList("Liability_New_worststatuslast24", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from ng_master_Aecb_Codes with (nolock) order by code");
			LoadPickList("ExtLiability_contractType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_contract_type with (nolock) order by code");

			formObject.setVisible("cmplx_Liability_New_overrideAECB", false);//need to check
			formObject.setVisible("cmplx_Liability_New_overrideIntLiab", false);

		}

		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadView(pEvent.getSource().getName());

			formObject.setLocked("EMploymentDetails_Frame1",true);

			/*formObject.setVisible("EMploymentDetails_Label33",false);
			formObject.setVisible("cmplx_EmploymentDetails_channelcode",false);*/
			formObject.setLocked("cmplx_EmploymentDetails_DOJ",true);
			formObject.setVisible("EMploymentDetails_Label36",true);
			formObject.setLocked("cmplx_EmploymentDetails_CntrctExpDate",true);
			formObject.setLocked("cmplx_EmploymentDetails_dateinPL",true);
			formObject.setLocked("cmplx_EmploymentDetails_dateinCC",true);
			formObject.setLocked("cmplx_EmploymentDetails_actualworkemp", false);

			//formObject.setVisible("WorldCheck_status",true);
			//formObject.setVisible("WorldCheck_fetch",true);
			loadPicklist4();
		}
		else if("GuarantorDetails".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setLocked("GuarantorDetails_Frame1", true);
			formObject.setEnabled("GuarantorDetails_Frame1", false);
		}

		

		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);

			loadPicklistELigibiltyAndProductInfo();

			formObject.setNGValue("cmplx_EligibilityAndProductInfo_RepayFreq","Monthly");
			//formObject.setVisible("ELigibiltyAndProductInfo_Label1",false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_TakeoverAMount",false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label2",false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_takeoverBank",true);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_FinalInterestRate",false);
			loadEligibilityData();
			formObject.setVisible("ELigibiltyAndProductInfo_Refresh",false);

		}

		else if ("LoanDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist_LoanDetails();
			//loanvalidate();
			formObject.setLocked("LoanDetails_Frame1",true);
			formObject.setLocked("cmplx_LoanDetails_fdisbdate",true);
			formObject.setLocked("cmplx_LoanDetails_frepdate",true);
			formObject.setLocked("cmplx_LoanDetails_maturitydate",true);
			formObject.setEnabled("LoanDetails_duedate", false);
			formObject.setEnabled("LoanDetails_disbdate", false);
			formObject.setEnabled("LoanDetails_payreldate", false);
			formObject.setEnabled("cmplx_LoanDetails_paidon", false);
			formObject.setEnabled("cmplx_LoanDetails_trandate", false);
			formObject.setEnabled("cmplx_LoanDetails_chqdat", false);
			//understanding gap JIRA.
			formObject.setLocked("LoanDetails_Button1",false);
			
		}
		else if ("RiskRating".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("RiskRating_Frame1",true);
			loadPicklistRiskRating();
		}
		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadView(pEvent.getSource().getName());
			loadPicklist_Address();
			formObject.setLocked("AddressDetails_Frame1",true);
		}

		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//commented by saurabh on 10th Nov 2017.
			LoadView(pEvent.getSource().getName());
			formObject.setLocked("AltContactDetails_Frame1",true);
			LoadpicklistAltcontactDetails();
			//below code added by siva on 01112019 for PCAS-1401
			AirArabiaValid();
			enrollrewardvalid();
			//Code ended by siva on 01112019 for PCAS-1401
		}

		else if ("ReferenceDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ReferenceDetails_Frame1",true);
			LoadPickList("ReferenceDetails_ref_Relationship", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Relationship with (nolock) order by code");
			

		}
		else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadpicklistCardDetails();
			Load_Self_Supp_Data();
			formObject.setLocked("CardDetails_Frame1",true);
			
		}

		else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			/*LoadPickList("nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			LoadPickList("gender", "select '--Select--' union select convert(varchar, description) from NG_MASTER_gender with (nolock)");
			LoadPickList("ResdCountry", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			LoadPickList("relationship", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Relationship with (nolock)");*/
			loadPicklist_suppCard();
			formObject.setVisible("SupplementCardDetails_Button1",true);
			formObject.setEnabled("SupplementCardDetails_Button1",true);
			formObject.setLocked("SupplementCardDetails_Button1",false);
		
		}

		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setEnabled("FATCA_Frame6",false);
			Loadpicklistfatca();	
			formObject.setEnabled("cmplx_FATCA_SignedDate", false);
			formObject.setEnabled("cmplx_FATCA_ExpiryDate", false);
			}

		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("KYC_Frame7",true);
			loadPicklist_KYC();
		}
		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadView(pEvent.getSource().getName());
			formObject.setLocked("OECD_Frame8",true);
			loadPickListOECD();
		}
		// disha FSD
		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			notepad_load();
			notepad_withoutTelLog();


		}
		else if ("PartMatch".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setEnabled("PartMatch_Frame1",false);
			formObject.setLocked("PartMatch_Dob",true);
		}
		else if ("MOL1".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("MOL1_Frame1", true);
			formObject.setLocked("cmplx_MOL_molexp", true);
			formObject.setLocked("cmplx_MOL_molissue", true);
			formObject.setLocked("cmplx_MOL_ctrctstart", true);
			formObject.setLocked("cmplx_MOL_ctrctend", true);
			
		}
		else if ("WorldCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {
			// added by abhishek as per CC FSD
			formObject.setLocked("WorldCheck1_Frame1",true);
			formObject.setLocked("WorldCheck1_Dob",true);
			formObject.setLocked("WorldCheck1_entdate",true);
			formObject.setLocked("WorldCheck1_upddate",true);
			//formObject.setLocked("WorldCheck1_Frame1",false);
			}
		
		//code changes by bandana starts
		else if ("CC_Loan".equalsIgnoreCase(pEvent.getSource().getName())){
			loadPicklist_ServiceRequest();
			formObject.setLocked("CC_Loan_Frame5",true);
			formObject.setLocked("CC_Loan_Frame4",true);
			formObject.setLocked("CC_Loan_Frame2",true);
			formObject.setLocked("CC_Loan_Frame3",true);
			// added by abhishek as per CC FSD
			formObject.setLocked("CC_Loan_Frame1",true);
			//loadPicklist_ServiceRequest();
		}
		else if ("ExternalBlackList".equalsIgnoreCase(pEvent.getSource().getName())) {
			PersonalLoanS.mLogger.info("inside locking of External Blacklist");
			formObject.setLocked("ExternalBlackList_Frame1",true);

			
		}
		else if ("LOS".equalsIgnoreCase(pEvent.getSource().getName())) {
			PersonalLoanS.mLogger.info("inside locking of LOS_Frame1");
			formObject.setLocked("LOS_Frame1",true);
			
		}
		else if ("FinacleCRMIncident".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("FinacleCRMIncident_Frame1", true);
		}
		else if ("FinacleCore".equalsIgnoreCase(pEvent.getSource().getName())){
			LoadPickList("FinacleCore_cheqtype", "select '--Select--' as description,'' as code union select convert(varchar, description),Code from ng_MASTER_Cheque_Type with (nolock) order by code");
			LoadPickList("FinacleCore_typeofret", "select '--Select--' union all select convert(varchar, description) from ng_MASTER_TypeOfReturn with (nolock)");

			formObject.setLocked("FinacleCore_Frame1",true);
			/*formObject.setEnabled("OECD_Save",true);*/
		}
		//FinacleCRM_CustInfo
		else if ("FinacleCRMCustInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("FinacleCRMCustInfo_Frame1", true);
		}
		//code changes by bandana ends
		 
		//Update Customer call(Tanshu Aggarwal-29/05/2017) 
		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {

			//for decision fragment made changes 8th dec 2017			
			formObject.setLocked("cmplx_Decision_waiveoffver",true);//added by akshay for proc 7607
			PersonalLoanS.mLogger.info("***********Inside decision history");
			//fragment_ALign("cmplx_Decision_waiveoffver#DecisionHistory_CifLock#DecisionHistory_CifUnlock#DecisionHistory_Button3#DecisionHistory_updcust#DecisionHistory_chqbook#\n#Decision_Label1,cmplx_Decision_VERIFICATIONREQUIRED#\n#Decision_Label3,cmplx_Decision_Decision#DecisionHistory_Label1,cmplx_Decision_ReferTo#DecisionHistory_Label11,DecisionHistory_DecisionReasonCode#\n#cmplx_Decision_MultipleApplicantsGrid#\n#DecisionHistory_Label6,cmplx_Decision_IBAN#DecisionHistory_Label7,cmplx_Decision_AccountNo#DecisionHistory_Label8,cmplx_Decision_ChequeBookNumber#DecisionHistory_Label9,cmplx_Decision_DebitcardNumber#\n#DecisionHistory_Label5,cmplx_Decision_desc#DecisionHistory_Label3,cmplx_Decision_strength#DecisionHistory_Label4,cmplx_Decision_weakness#Decision_Label4,cmplx_Decision_REMARKS#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
			fragment_ALign("cmplx_Decision_waiveoffver#DecisionHistory_CifLock#DecisionHistory_CifUnlock#DecisionHistory_Button3#DecisionHistory_updcust#DecisionHistory_chqbook#\n#Decision_Label3,cmplx_Decision_Decision#DecisionHistory_Label1,cmplx_Decision_ReferTo#DecisionHistory_Label11,DecisionHistory_DecisionReasonCode#DecisionHistory_Label41,DecisionHistory_DecisionSubReason#\n#cmplx_Decision_MultipleApplicantsGrid#\n#DecisionHistory_Label6,cmplx_Decision_IBAN#DecisionHistory_Label7,cmplx_Decision_AccountNo#DecisionHistory_Label8,cmplx_Decision_ChequeBookNumber#DecisionHistory_Label9,cmplx_Decision_DebitcardNumber#\n#DecisionHistory_Label5,cmplx_Decision_desc#DecisionHistory_Label3,cmplx_Decision_strength#DecisionHistory_Label4,cmplx_Decision_weakness#Decision_Label4,cmplx_Decision_REMARKS#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line

			formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
			formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
			//adjustFrameTops("Notepad_Values,DecisionHistory,ReferHistory");//added by akshay for proc 7591
			
			try{
				PersonalLoanS.mLogger.info("***********Inside after fragment alignment decision history");
				//for decision fragment made changes 8th dec 2017
				formObject.setVisible("DecisionHistory_updcust", true);
				//formObject.setVisible("DecisionHistory_Button3", false); - PCASI - 3615
				formObject.setVisible("DecisionHistory_Button3", true);	//PCASI - 3615
				formObject.setLocked("DecisionHistory_updcust", true);
				PersonalLoanS.mLogger.info("Customer update, Account Update button visible");
				
				//added by Vaishvi for PCASI - 3433 - start
				PersonalLoanS.mLogger.info("value of retain checkbox: "+formObject.getNGValue("AlternateContactDetails_RetainAccIfLoanReq")+" , value of NTB checkbox: "+formObject.getNGValue("cmplx_Customer_NTB"));
				PersonalLoanS.mLogger.info("PL_true :: "+NGFUserResourceMgr_PL.getGlobalVar("PL_true"));
				if((NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))) && 
						(NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("AlternateContactDetails_RetainAccIfLoanReq"))))
				{
					PersonalLoanS.mLogger.info("Setting checkbook button visible.");
					formObject.setVisible("DecisionHistory_chqbook",true);
				}
				else{
					formObject.setVisible("DecisionHistory_chqbook", false);
				}
				//added by Vaishvi for PCASI - 3433 - end
				
				/*if((NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))) && "".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_New_CIFNo"))) 
				{
					formObject.setVisible("DecisionHistory_Button2",false);
					formObject.setLeft("DecisionHistory_Button2",1000);
					//below code added by nikhil
					formObject.setVisible("DecisionHistory_Button3",true);
					formObject.setVisible("DecisionHistory_updcust",true);
					formObject.setVisible("DecisionHistory_chqbook",true);
				}
				//below code added by nikhil
				if((NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))))
				{
					formObject.setVisible("DecisionHistory_Button3",true);
					formObject.setVisible("DecisionHistory_updcust",true);
					formObject.setVisible("DecisionHistory_chqbook",true);
				}
				else
				{
					formObject.setVisible("DecisionHistory_Button3",false);
					formObject.setVisible("DecisionHistory_updcust",false);
					formObject.setVisible("DecisionHistory_chqbook",false);
					formObject.setVisible("DecisionHistory_Button2",false);
				}---commented by akshay on 1/5/18 for proc 8923---code shifted to js on row click*/


				String retainCheckboxValue = formObject.getNGValue("AlternateContactDetails_RetainAccIfLoanReq");
				PersonalLoanS.mLogger.info("value of retain checkbox before if is: "+retainCheckboxValue);
				if(retainCheckboxValue != null){
					//Fragment not opened.
					PersonalLoanS.mLogger.info("inside else of retain checkbox");
				}
				else{

					PersonalLoanS.mLogger.info("inside if of retain checkbbox");
					int framestate=formObject.getNGFrameState("Alt_Contact_container");
					PersonalLoanS.mLogger.info("framestate is: "+framestate);
					if(framestate == 1){
						PersonalLoanS.mLogger.info("PL_DDVT_CHECKER alternate contact details framestate is 1");

						new PersonalLoanSCommonCode().alignfragmentsafterfetch(formObject);

					}

				}
				/*if(NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("AlternateContactDetails_RetainAccIfLoanReq"))){
					PersonalLoanS.mLogger.info( "after making buttons visible"+formObject.getNGValue("AlternateContactDetails_RetainAccIfLoanReq"));
					formObject.setVisible("DecisionHistory_updcust",true);
					formObject.setVisible("DecisionHistory_chqbook",true);
					PersonalLoanS.mLogger.info( "after making buttons visible");
				}
				else
				{
					//formObject.setVisible("DecisionHistory_Button3",false);
					formObject.setVisible("DecisionHistory_updcust",false);
					formObject.setVisible("DecisionHistory_chqbook",false);
					PersonalLoanS.mLogger.info( "after making buttons invisible");
				}
---commented by akshay on 1/5/18---code shifted to js on row click*/
				if(NGFUserResourceMgr_PL.getGlobalVar("PL_Reschedulment").equalsIgnoreCase(formObject.getNGValue("InitiationType")))
				{

					loadPicklist1();
				}
				else
				{
					loadPicklistChecker();
				}
				
				PersonalLoanS.mLogger.info("Before adding in Multiple applicants Grid!!!");

				try{
						String grid_applicantType="";
						List<List<String>> mylist=new ArrayList<List<String>>();
						List<String> subList=new ArrayList<String>();
						//Deepak condition added for PCAS-2473
						if("Primary".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",0,0))
								&& "".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",0,3))
										&& (!"".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_CIFNo")))
										&& "false".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
							formObject.setNGValue("cmplx_DEC_MultipleApplicantsGrid",0,3,formObject.getNGValue("cmplx_Customer_CIFNo"));
						}
						
						for(int i=0;i<formObject.getLVWRowCount("cmplx_Decision_MultipleApplicantsGrid");i++){
							grid_applicantType+=formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",i,0)+",";
						}
						PersonalLoanS.mLogger.info("Applicant types in Grid is: "+grid_applicantType);

					
						if(!grid_applicantType.contains("Supplement")){
							int n=formObject.getLVWRowCount("SupplementCardDetails_cmplx_supplementGrid");
							
								List<String> suppPassAdded = new ArrayList<String>();
								
								PersonalLoanS.mLogger.info("supplementary grid row count: "+n);
							for(int i=0;i<n;i++){
								subList.clear();
								Map<String,String> suppGridColumnsToValues = initializeGridMap("SupplementCardDetails_cmplx_supplementGrid",i);
								String statusSupp = suppGridColumnsToValues.get(getValueOfConstant("Supplement_Status"));//getValueOfConstant(constName)//formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,35);//.equals("Active");
								String passSupp = suppGridColumnsToValues.get(getValueOfConstant("Supplement_Passport_No"));//formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,3);
								//String cardProdSupp = formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,30);
								
								if(!suppPassAdded.contains(passSupp) && "Active".equals(statusSupp)){
								subList.add("Supplement");
								subList.add(suppGridColumnsToValues.get(getValueOfConstant("Supplement_First_Name"))+" "+suppGridColumnsToValues.get(getValueOfConstant("Supplement_Last_Name")));
								subList.add(suppGridColumnsToValues.get(getValueOfConstant("Supplement_Passport_No")));
								subList.add(suppGridColumnsToValues.get(getValueOfConstant("Supplement_CIF_ID")));
								
								
								subList.add("");//is_cif_created
								subList.add("");//cif verified
								subList.add("");//cif locked
								subList.add("");//is_cif_updated
								subList.add(formObject.getWFWorkitemName());
								mylist.add(new ArrayList<String>(subList));//each time adding we need to give a new list reference or it will pick up the old one and uon clear it will get deleted as well
								suppPassAdded.add(passSupp);
							}
						  }
						}
						PersonalLoanS.mLogger.info("mylist 3: "+mylist);
						if(mylist.size()>0){
							for(List<String> temp:mylist){
								formObject.addItemFromList("cmplx_Decision_MultipleApplicantsGrid", temp);
							}
						}
					//}		
				}catch(Exception e){
					PersonalLoanS.mLogger.info("Exception while adding data in mutiple app grid: "+ e.getMessage());
					printException(e);
				}
				
				
			}
			catch(Exception e){
				PersonalLoanS.mLogger.info( "Exception in Decision fragment load");
				printException(e);

			} 	
			//PersonalLoanS.mLogger.info("***********Inside after making field visible");
			finally{
				//setDataInMultipleAppGrid();
			}
		} 	
		
		//shifted from PLCOmmonCode to here by akshay on 17/1/18
		/*else if ("IncomingDoc".equalsIgnoreCase(pEvent.getSource().getName())) {

			if("".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP")) && NGFUserResourceMgr_PL.getGlobalVar("PL_false").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")))
				formObject.setVisible("IncomingDoc_UploadSig",false);
			else if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP")) || NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
				formObject.setVisible("IncomingDoc_ViewSIgnature",false);
				formObject.setVisible("IncomingDoc_UploadSig",true);
			
			}
			fetchIncomingDocRepeater();
		}	
		
*/		else if ("ReferHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadReferGrid();
	}


	}

	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{

		//PersonalLoanS.mLogger.info("Inside PL_Initiation eventDispatched()-->EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());



		switch(pEvent.getType())
		{	

		case FRAME_EXPANDED:
			//PersonalLoanS.mLogger.info(" In PL_DDVT Checker eventDispatched()-->EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			new PersonalLoanSCommonCode().FrameExpandEvent(pEvent);
			break;

		case FRAGMENT_LOADED:
			fragment_loaded(pEvent);
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




	public void continueExecution(String arg0, HashMap<String, String> arg1) {
		//empty method

	}


	public void initialize() {
		//empty method

	}


	public void saveFormCompleted(FormEvent arg0) throws ValidatorException {
		CustomSaveForm();
		//empty method
	}


	public void saveFormStarted(FormEvent arg0) throws ValidatorException {
		try{//code sync with CC by shweta
			CustomSaveForm();
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			if("".equalsIgnoreCase(formObject.getNGValue("CUSTOMERNAME"))){
				if("".equals(formObject.getNGValue("cmplx_Customer_MiddleNAme")))
				{
					formObject.setNGValue("CUSTOMERNAME",formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme"));
					//changed from customer_name to customerName by akshay on 17/4/18
				}
	
				else{
					formObject.setNGValue("CUSTOMERNAME",formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_MiddleName")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme"));
					//changed from customer_name to customerName by akshay on 17/4/18
				}
			}
			if("".equalsIgnoreCase(formObject.getNGValue("CIF_ID"))){
				formObject.setNGValue("CIF_ID",formObject.getNGValue("cmplx_Customer_CIFNO"));
			}
			formObject.setNGValue("Final_Limit", formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
			formObject.setNGValue("cmplx_IncomeDetails_totSal", formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalTai"));//added for proc 9940
		} catch(Exception e){
			PersonalLoanS.mLogger.info("Exception occured while setting Customer name & CIf in ext table at DDVT checker"+ e.getMessage());
		}
		
	}


	public void submitFormCompleted(FormEvent pEvent) throws ValidatorException {
		//empty method
		
		//++ below code added by abhishek on 04/01/2018 for EFMS refresh functionality
		
		PersonalLoanS.mLogger.info("Inside PL PROCESS ddvtchecker submitFormCompleted()" + pEvent.getSource()); 
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//Deepak changes done for PCAS-2982 # code sync
		String dec = formObject.getNGValue("cmplx_Decision_Decision");
		if("Approve".equalsIgnoreCase(dec)){
			String check_app = "select COUNT(dec_wi_name) as app_count from NG_RLOS_GR_DECISION where dec_wi_name='"+formObject.getWFWorkitemName()+"' and workstepName = 'DDVT_Checker' and Decision = 'Approve'";
			List<List<String>> check_appCount=formObject.getDataFromDataSource(check_app);
			if( Integer.parseInt(check_appCount.get(0).get(0))<2){
				List<String> objInput=new ArrayList<String>();
				objInput.add("Text:"+formObject.getWFWorkitemName());
				//Deepak Chnages for PCAS-2983
				if("Y".equalsIgnoreCase(formObject.getNGValue("IS_Approve_Cif"))){
					objInput.add("Text:"+"ASTC");
				}
				else{
					objInput.add("Text:"+"Waiting for Approval");
				}
				
				PersonalLoanS.mLogger.info("objInput args are: "+objInput.get(0)+"::: "+objInput.get(1));
				List<Object> objOutput=new ArrayList<Object>();
					objOutput.add("Text");
					objOutput= formObject.getDataFromStoredProcedure("ng_EFMS_InsertData", objInput,objOutput);
					//CreditCard.mLogger.info("objOutput args are: "+objOutput.toString());
					//++ above code added by abhishek on 04/01/2018 for EFMS refresh functionality
			
			}
					
		}
		else{
			PersonalLoanS.mLogger.info("Exclusion for EFMS as per new condition");
			formObject.setNGValue("EFMS","NA");
		}
		//++ above code added by abhishek on 04/01/2018 for EFMS refresh functionality
	}



	public void submitFormStarted(FormEvent arg0) throws ValidatorException {

		//changes by saurabh on 20th july 17.
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		saveIndecisionGrid();
		CC_waiver_check();
		CustomSaveForm();
		
		try
		{
			formObject.setNGValue("EMPLOYMENT_DETAILS_MATCH","Yes");
			formObject.setNGValue("employ_detail_match","Yes");//mapping correction
			//shweta Changes done to handle Approve subject to Cif case if CAD case already processed: start
			if("Y".equalsIgnoreCase(formObject.getNGValue("IS_Approve_Cif"))&& "Approve".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision"))){
				PersonalLoanS.mLogger.info("Submit form started of DDVT checker case of Approve Sub to CIF :: Winame: "+ formObject.getWFWorkitemName()+" user name: "+ formObject.getUserName());
				formObject.setNGValue("q_hold2", 1);
				formObject.setNGValue("hold2", 1);
				//formObject.setNGValue("VAR_INT4",1);
				formObject.setNGValue("q_PrevHold", "Hold2");
				formObject.setNGValue("VAR_STR20", "Hold2");
			}
			
			//shweta Changes done to handle Approve subject to Cif case if CAD case already processed: End
			formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
			formObject.setNGValue("ddvt_checker_dec", formObject.getNGValue("cmplx_Decision_Decision"));
			formObject.setNGValue("CA_Refer_DDVT", "N");
			formObject.setNGValue("IS_CPV", "N");
			//formObject.setNGValue("ReferTo", formObject.getNGValue("cmplx_Decision_ReferTo"));
			formObject.setNGValue("CIF_ID",formObject.getNGValue("cmplx_Customer_CIFNO"));
			
			if(Check_CPV_Refer_DDVT())
			{
				formObject.setNGValue("IS_CPV", "Y");
			}
			
			//++below code added by nikhil for PCAS-2140 CR
			if(!"true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")))
			{
				List<String> objInput_Liab=new ArrayList<String>();
				List<Object> objOutput_Liab=new ArrayList<Object>();
				objInput_Liab.add("Text:"+formObject.getNGValue("parent_WIName"));
				PersonalLoanS.mLogger.info("objOutput_Liab args are: "+objInput_Liab.get(0));
				objOutput_Liab.add("Text");
				objOutput_Liab=formObject.getDataFromStoredProcedure("ng_RLOS_ClearLiability", objInput_Liab,objOutput_Liab);
			}
			if(!"Approve".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")) )
			formObject.setNGValue("Next_Workstep","");
			LoadReferGrid();
			if("Approve".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")) ) {
				 PersonalLoanS.mLogger.info("Inside  approve DDVT checker");
				  formObject.setNGValue("q_parllel_seq", 1);
				  formObject.setNGValue("q_s_count", 1);			  
				  PersonalLoanS.mLogger.info("Inside  approve " + formObject.getNGValue("q_parllel_seq"));
			 } 
			//removed 
			//	saveIndecisionGridCSM();//Arun (01-12-17) for Decision history to appear in the grid
			String squeryrefer = "select referTo from NG_RLOS_GR_DECISION where workstepName like '%DDVT_Checker%' and dec_wi_name= '" + formObject.getWFWorkitemName() + "' order by dateLastChanged desc ";
			List<List<String>> FCURefer = formObject.getNGDataFromDataCache(squeryrefer);
			PersonalLoanS.mLogger.info("RLOS COMMON"+ " iNSIDE prev_loan_dbr+ " + FCURefer);
			
			//CreditCard.mLogger.info("RLOS COMMON"+ " iNSIDE prev_loan_dbr+ " + squeryloan);
			String referto="";
			if (FCURefer != null && FCURefer.size() > 0) {
				referto = FCURefer.get(0).get(0);
				}
			if("FPU".equalsIgnoreCase(referto)){		
				formObject.setNGValue("RefFrmDDVT", "Y");
			}
		
		}
		catch(Exception e)
		{
			PersonalLoanS.mLogger.info("Exception occured in submit form started method" +e.getMessage());
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

