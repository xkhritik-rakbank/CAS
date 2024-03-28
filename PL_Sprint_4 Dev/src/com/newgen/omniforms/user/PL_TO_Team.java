/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PL_TO_Team.java
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
import java.util.Date;
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

public class PL_TO_Team extends PLCommon implements FormListener
{
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	Logger mLogger=PersonalLoanS.mLogger;
	public void formLoaded(FormEvent pEvent)
	{
		FormConfig objConfig = FormContext.getCurrentInstance().getFormConfig();
        objConfig.getM_objConfigMap().put("PartialSave", "true");
        makeSheetsInvisible("Tab1", "8,9");//Changes done by shweta for jira #1768 to hide CPV tab

	}


	public void formPopulated(FormEvent pEvent) 
	{
		try{
			new PersonalLoanSCommonCode().setFormHeader(pEvent);
		}catch(Exception e)
		{
			mLogger.info("PL TO Team"+ "Exception:"+e.getMessage());
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
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadView(pEvent.getSource().getName());
			loadPicklistCustomer();
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
		}	
		else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct with(nolock)");
			LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_ApplicationType with(nolock) where SubProdFlag = '"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2)+"'");
			LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) order by SCHEMEID");
			LoadPickList("subProd", "select '--Select--' as description,'' as code union select description,code from ng_MASTER_SubProduct_PL with (nolock) order by code");
			formObject.setLocked("Product_Frame1",true);
			
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
		else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("IncomeDetails_Frame2",true);
			LoadPickList("cmplx_IncomeDetails_StatementCycle", "select '--Select--' union select convert(varchar, description) from NG_MASTER_StatementCycle with (nolock)");
			
		}
		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("Liability_New_Frame1",true);
			formObject.setLocked("ExtLiability_Frame1",true);
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
			LoadPickList("ExtLiability_contractType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_contract_type with (nolock) order by code");
}
		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("EMploymentDetails_Frame1",true);
			LoadView(pEvent.getSource().getName());

			loadPicklist4();
			formObject.setLocked("EMploymentDetails_Frame1",true);
			formObject.setVisible("EMploymentDetails_Label31", true);
			 formObject.setVisible("cmplx_EmploymentDetails_empmovemnt", true);
			 formObject.setVisible("EMploymentDetails_Label15", true);
			 formObject.setLocked("cmplx_EmploymentDetails_DOJ", true);
			 formObject.setLocked("cmplx_EmploymentDetails_CntrctExpDate", true);
			 formObject.setLocked("cmplx_EmploymentDetails_dateinCC", true);
			 formObject.setLocked("cmplx_EmploymentDetails_dateinPL", true);
			 formObject.setLocked("cmplx_EmploymentDetails_DateOfLeaving", true);
			//below code added by nikhil 1/3/18
			/* if(!NGFUserResourceMgr_PL.getGlobalVar("PL_RESC").equalsIgnoreCase(formObject.getNGValue("Application_Type")) && !NGFUserResourceMgr_PL.getGlobalVar("PL_RESN").equalsIgnoreCase(formObject.getNGValue("Application_Type")) && !NGFUserResourceMgr_PL.getGlobalVar("PL_RESR").equalsIgnoreCase(formObject.getNGValue("Application_Type"))){
			
				formObject.setVisible("EMploymentDetails_Label33",false);
				formObject.setVisible("cmplx_EmploymentDetails_channelcode",false);
			}*/
			 formObject.setVisible("cmplx_EmploymentDetails_Field_visit_done",true);
			 formObject.setVisible("EMploymentDetails_Label32",true);
		}
		else if ("LoanDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist_LoanDetails();
			if(formObject.isVisible("ELigibiltyAndProductInfo_Frame1")==false)
			 {
					formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligibilityProdInfo");
					Eligibilityfields();
			 }
			loanvalidate();//added by saurabh for proc 10831.
			//formObject.setNGValue("cmplx_LoanDetails_fdisbdate", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
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
			
			
		}
		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
			loadPicklistELigibiltyAndProductInfo();

			loadEligibilityData();
		}
		// disha FSD
		else if ("RiskRating".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("RiskRating_Frame1",true);
			loadPicklistRiskRating();
		}
		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			LoadView(pEvent.getSource().getName());
		}
		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadView(pEvent.getSource().getName());

			formObject.setLocked("AltContactDetails_Frame1",true);
			LoadpicklistAltcontactDetails();
			//below code added by siva on 01112019 for PCAS-1401
			AirArabiaValid();
			enrollrewardvalid();
			//Code ended by siva on 01112019 for PCAS-1401
		}

		//Changes done for code optimization 25/07
		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("FATCA_Frame6",true);
			formObject.setLocked("cmplx_FATCA_SignedDate",true);
			formObject.setLocked("cmplx_FATCA_ExpiryDate",true);
			Loadpicklistfatca();
		}
		else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadpicklistCardDetails();
			formObject.setLocked("CardDetails_Frame1",true);
		}
		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("KYC_Frame7",true);
			loadPicklist_KYC();
		}
		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadView(pEvent.getSource().getName());

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

		//Changes done for code optimization 25/07

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

			formObject.setLocked("RejectEnq_Frame1",true);
		}

		else if ("ExternalBlackList".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ExternalBlackList_Frame1",true);
		}

		else if ("SalaryEnq".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("SalaryEnq_Frame1",true);
		}
		else if ("ReferenceDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ReferenceDetails_Frame1",true);
			LoadPickList("ReferenceDetails_ref_Relationship", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Relationship with (nolock) order by code");
			
		}
		else if("Loan_Disbursal".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setLocked("Loan_Disbursal_Frame2", true);
			formObject.setEnabled("Loan_Disbursal_Frame2", false);
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
		else if ("FinacleCore".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("FinacleCore_Frame1",true);
			formObject.setLocked("FinacleCore_DatePicker2", true);
			formObject.setLocked("FinacleCore_cheqretdate", true);
			LoadPickList("FinacleCore_cheqtype", "select '--Select--' as description,'' as code union select convert(varchar, description),Code from ng_MASTER_Cheque_Type with (nolock) order by code");
		}
		//added  by yash on 26/12/2017
		/*else if ("IncomingDoc".equalsIgnoreCase(pEvent.getSource().getName())){
			fetchIncomingDocRepeater();
			formObject.setVisible("cmplx_DocName_OVRemarks", false);
			formObject.setVisible("cmplx_DocName_OVDec",false);
			formObject.setVisible("cmplx_DocName_Approvedby",false);
		}*/
		// disha FSD
		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			notepad_load();
			 notepad_withoutTelLog();
			}
		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			//for decision fragment made changes 8th dec 2017
			PersonalLoanS.mLogger.info("***********Inside decision history of csm");
			fragment_ALign("DecisionHistory_Label10,cmplx_Decision_New_CIFNo#DecisionHistory_Label7,cmplx_Decision_AccountNo#DecisionHistory_Label6,cmplx_Decision_IBAN#Decision_Label1,cmplx_Decision_VERIFICATIONREQUIRED#\n#Decision_Label3,cmplx_Decision_Decision#DecisionHistory_Label1,cmplx_Decision_ReferTo#DecisionHistory_Label11,DecisionHistory_DecisionReasonCode#DecisionHistory_Label41,DecisionHistory_DecisionSubReason#\n#Decision_Label4,cmplx_Decision_REMARKS#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
			formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
			formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
			
			loadPicklist1();
			//++Below code added by yash 15/12/17 for toteam decision
			formObject.setEnabled("cmplx_Decision_New_CIFNo",false);
			formObject.setEnabled("cmplx_Decision_IBAN",false);
			//++above code added by yash on 15/12/2017 for toteam decision
			
			
			//for decision fragment made changes 8th dec 2017
			
		} 	
		//++below code changed added by yash for toteam
		else if("PostDisbursal".equalsIgnoreCase(pEvent.getSource().getName()))
				{
					//String sQuery = "Select distinct DocName FROM ng_rlos_incomingDoc WHERE  docname like 'liability_certificate%' and DocSta = 'Received' and wi_name='"+formObject.getWFWorkitemName()+"'";
					//String sQuery = "Select distinct DocName FROM ng_rlos_incomingDoc with(nolock) WHERE  docname like 'liability_certificate%' and wi_name='"+formObject.getWFWorkitemName()+"'";
					String sQuery="select distinct documentname  from ng_rlos_gr_incomingDocument with (nolock) where documentname like 'liability_certificate%' and IncomingDocGR_Winame ='"+formObject.getWFWorkitemName()+"'";
						//cmplx_LoanDetails_loanemi
					LoadPickList("PostDisbursal_Bank_Name", "select '--Select--' as description,'' as code union select  convert(varchar, description),code from ng_master_bankname with (nolock)  where isActive='Y' order by code");
					LoadPickList("PostDisbursal_BG_Bank_Name", "select '--Select--' as description,'' as code union select  convert(varchar, description),code from ng_master_bankname with (nolock)  where isActive='Y' order by code");
					LoadPickList("PostDisbursal_Emirate", "select '--Select--' as description,'' as code union select  convert(varchar, description),code from ng_master_emirate with (nolock)  where isActive='Y' order by code");
					mLogger.info("LC query before load is ::"+sQuery);
					LoadPickList("PostDisbursal_LC_LC_Doc_Name", sQuery);

					LoadPickList("PostDisbursal_MCQ_Doc_Name", sQuery);
					LoadPickList("PostDisbursal_BG_LC_Doc_Name", sQuery);
					LoadPickList("PostDisbursal_NLC_LC_Doc_name", sQuery);
					formObject.setLocked("PostDisbursal_MCQ_Doc_Name",true);

					formObject.setLocked("PostDisbursal_BG_LC_Doc_Name",true);
					formObject.setLocked("PostDisbursal_NLC_LC_Doc_name",true);
					mLogger.info("LC query after load is ::"+sQuery);
					
					int row_count=formObject.getLVWRowCount("cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate");

					mLogger.info("LC query before load is ::"+sQuery);
					LoadPickList("PostDisbursal_LC_LC_Doc_Name", sQuery);

					LoadPickList("PostDisbursal_MCQ_Doc_Name", sQuery);
					LoadPickList("PostDisbursal_BG_LC_Doc_Name", sQuery);
					LoadPickList("PostDisbursal_NLC_LC_Doc_name", sQuery);
					formObject.setLocked("PostDisbursal_MCQ_Doc_Name",true);

					formObject.setLocked("PostDisbursal_BG_LC_Doc_Name",true);
					formObject.setLocked("PostDisbursal_NLC_LC_Doc_name",true);
					mLogger.info("LC query after load is ::"+sQuery);
					//int row_count=formObject.getLVWRowCount("cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate");

					boolean active_flag=false;
					for(int i=0;i<row_count;i++)
					{
						if(formObject.getNGValue("cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate", row_count-1, 7).equalsIgnoreCase("Active"))
						{
							active_flag=true;
						}
					}
					if(!active_flag)
					{
					formObject.setLocked("PostDisbursal_Frame3", true);
					formObject.setLocked("PostDisbursal_Frame6", true);
					formObject.setLocked("PostDisbursal_Frame4", true);
					formObject.setLocked("PostDisbursal_Frame5",true);
					mLogger.info("LC query after load is ::"+active_flag);
					}
					formObject.setEnabled("PostDisbursal_STL_SMS", true);
					
					formObject.setVisible("PostDisbursal_Hld_Place_Margin",false);
					formObject.setVisible("PostDisbursal_Authorise_to_overdraw",false);
					formObject.setVisible("PostDisbursal_BG_margin_amt_AED",false);
					formObject.setVisible("PostDisbursal_ExpiryDate_for_LG_Cont",false);
					formObject.setVisible("PostDisbursal_IGT_Number",false);
					formObject.setVisible("PostDisbursal_Label26",false);
					formObject.setVisible("PostDisbursal_Label27",false);
					formObject.setVisible("PostDisbursal_Label28",false);
					formObject.setVisible("PostDisbursal_Label31",false);
					formObject.setVisible("PostDisbursal_ExpiryDate_for_LG_button",false);
					formObject.setVisible("PostDisbursal_Label30",false);
					formObject.setVisible("PostDisbursal_ExpiryDate_for_LG",false);
				}
		
				//--above code added by nikhil for toteam
	}

	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{

		//mLogger.info("Inside PL_Initiation eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

		switch(pEvent.getType())
		{	

		case FRAME_EXPANDED:
			//mLogger.info(" In PL_Iniation eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

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
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();	
		formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));


	}


	public void initialize() {
			//empty method

	}


	public void saveFormCompleted(FormEvent arg0) throws ValidatorException {
		//empty method
		CustomSaveForm();
	}


	public void saveFormStarted(FormEvent arg0) throws ValidatorException {
			//empty method

	}


	public void submitFormCompleted(FormEvent arg0) throws ValidatorException {
		//empty method

		//++ below code added by Deepak on 19/03/2018 for EFMS refresh functionality
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		List<String> objInput=new ArrayList<String>();

		String decision_str = formObject.getNGValue("cmplx_Decision_Decision");
		if(NGFUserResourceMgr_PL.getGlobalVar("PL_Submit_Desc").equalsIgnoreCase(decision_str)){
			objInput.add("Text:"+formObject.getWFWorkitemName());
			objInput.add("Text:"+"Approve");
			PersonalLoanS.mLogger.info("objInput args are: "+objInput.get(0));
			List<Object> objOutput=new ArrayList<Object>();
			
			objOutput.add("Text");
		PersonalLoanS.mLogger.info("Before executing procedure ng_RLOS_CheckWriteOff");
		objOutput= formObject.getDataFromStoredProcedure("ng_EFMS_InsertData", objInput,objOutput);
		}

		//++ above code added by Deepak on 19/03/2018 for EFMS refresh functionality	
	}



	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try{
			CustomSaveForm();
		formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
		/*if("Pending for documentation".equalsIgnoreCase( formObject.getNGValue("cmplx_Decision_Decision")))
		{
			formObject.setNGValue("IS_TakeOver_Child","Y");
		}*/
		if("Approve".equalsIgnoreCase( formObject.getNGValue("cmplx_Decision_Decision")))
		{
			formObject.setNGValue("takeover_status","Y");
		}
		else if("Refer".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision"))){
			if( !"DDVT Maker".equalsIgnoreCase(formObject.getNGValue("q_MailSubject")) &&  !"ToTeam_Hold".equalsIgnoreCase(formObject.getNGValue("q_MailSubject")) ){					
				formObject.setNGValue("postHoldReferWS",formObject.getWFActivityName());
				formObject.setNGValue("IS_Stage_Reversal", "Y");
			}
		}
		
		saveIndecisionGrid();
		loadInDecGrid();
	
		}
		catch (Exception ex)
		{
			PersonalLoanS.mLogger.info(ex.getMessage());
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

