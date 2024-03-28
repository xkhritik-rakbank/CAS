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
		
	}


	public void formPopulated(FormEvent pEvent) 
	{
		try{
			new PersonalLoanSCommonCode().setFormHeader(pEvent);
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
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
		FormReference formObject= FormContext.getCurrentInstance().getFormReference();
		PersonalLoanS.mLogger.info("In PL_Initiation eventDispatched()-->EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		Common_Utils common=new Common_Utils(mLogger);


		if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Customer_Frame1",true);
			formObject.setLocked("cmplx_Customer_NEP", true);
			formObject.setLocked("cmplx_Customer_DOb", true);
			formObject.setLocked("cmplx_Customer_IdIssueDate", true);
			formObject.setLocked("cmplx_Customer_EmirateIDExpiry", true);
			formObject.setLocked("cmplx_Customer_VisaIssueDate", true);
			formObject.setLocked("cmplx_Customer_PassportIssueDate", true);
			formObject.setLocked("cmplx_Customer_VIsaExpiry", true);
			formObject.setLocked("cmplx_Customer_PassPortExpiry", true);
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

			formObject.setVisible("ExtLiability_AECBReport", false);
			formObject.setVisible("cmplx_Liability_New_overrideAECB", false);//need to check
			formObject.setVisible("cmplx_Liability_New_overrideIntLiab", false);

		}

		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("EMploymentDetails_Frame1",true);

			formObject.setVisible("EMploymentDetails_Label36",false);
			formObject.setVisible("cmplx_EmploymentDetails_channelcode",false);
			formObject.setLocked("cmplx_EmploymentDetails_DOJ",true);
			formObject.setLocked("cmplx_EmploymentDetails_CntrctExpDate",true);
			formObject.setLocked("cmplx_EmploymentDetails_dateinPL",true);
			formObject.setLocked("cmplx_EmploymentDetails_dateinCC",true);
			loadPicklist4();
		}
		else if("GuarantorDetails".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setLocked("GuarantorDetails_Frame1", true);
			formObject.setEnabled("GuarantorDetails_Frame1", false);
		}

		

		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);

			LoadPickList("cmplx_EligibilityAndProductInfo_RepayFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_frequency with (nolock)");
			LoadPickList("cmplx_EligibilityAndProductInfo_InstrumentType", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_InstrumentType with (nolock) where isactive = 'Y'  order by code");
			LoadPickList("cmplx_EligibilityAndProductInfo_InterestType", "select '--Select--' union select convert(varchar, description) from NG_MASTER_InterestType with(nolock)");

			formObject.setNGValue("cmplx_EligibilityAndProductInfo_RepayFreq","M");
			formObject.setVisible("ELigibiltyAndProductInfo_Label39",false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_instrumenttype",false);
			//formObject.setVisible("ELigibiltyAndProductInfo_Label1",false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_TakeoverAMount",false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label2",false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_takeoverBank",false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_FinalInterestRate",false);
			LoadPickList("cmplx_EligibilityAndProductInfo_takeoverBank", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_BankName with (nolock) order by code");
			loadEligibilityData();
			formObject.setVisible("ELigibiltyAndProductInfo_Refresh",false);

		}

		else if ("LoanDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist_LoanDetails();
			loanvalidate();
			formObject.setLocked("LoanDetails_Frame1",true);
			formObject.setLocked("cmplx_LoanDetails_fdisbdate",true);
			formObject.setLocked("cmplx_LoanDetails_frepdate",true);
			formObject.setLocked("cmplx_LoanDetails_maturitydate",true);
			formObject.setNGValue("cmplx_LoanDetails_trandate",common.Convert_dateFormat("", "", "dd/MM/yyyy"));//added by akshay on 5/4/18 for proc 6424
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
			loadPicklist_Address();
			formObject.setLocked("AddressDetails_Frame1",true);
		}

		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//commented by saurabh on 10th Nov 2017.

			formObject.setLocked("AltContactDetails_Frame1",true);
			LoadpicklistAltcontactDetails();
		}

		else if ("ReferenceDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ReferenceDetails_Frame1",true);
			LoadPickList("ReferenceDetails_ref_Relationship", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Relationship with (nolock) order by code");
			

		}
		else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadpicklistCardDetails();
			formObject.setLocked("CardDetails_Frame1",true);
			
		}

		else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			/*LoadPickList("nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			LoadPickList("gender", "select '--Select--' union select convert(varchar, description) from NG_MASTER_gender with (nolock)");
			LoadPickList("ResdCountry", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			LoadPickList("relationship", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Relationship with (nolock)");*/
			loadPicklist_suppCard();
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
		//Update Customer call(Tanshu Aggarwal-29/05/2017) 
		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {

			//for decision fragment made changes 8th dec 2017			
			formObject.setLocked("cmplx_Decision_waiveoffver",true);//added by akshay for proc 7607
			PersonalLoanS.mLogger.info("***********Inside decision history");
			fragment_ALign("cmplx_Decision_waiveoffver#DecisionHistory_CifLock#DecisionHistory_CifUnlock#DecisionHistory_Button3#DecisionHistory_updcust#DecisionHistory_chqbook#\n#Decision_Label1,cmplx_Decision_VERIFICATIONREQUIRED#\n#Decision_Label3,cmplx_Decision_Decision#DecisionHistory_Label1,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_DecisionReasonCode#\n#cmplx_Decision_MultipleApplicantsGrid#\n#DecisionHistory_Label6,cmplx_Decision_IBAN#DecisionHistory_Label7,cmplx_Decision_AccountNo#DecisionHistory_Label8,cmplx_Decision_ChequeBookNumber#DecisionHistory_Label9,cmplx_Decision_DebitcardNumber#\n#DecisionHistory_Label5,cmplx_Decision_desc#DecisionHistory_Label3,cmplx_Decision_strength#DecisionHistory_Label4,cmplx_Decision_weakness#Decision_Label4,cmplx_Decision_REMARKS#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
			formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
			formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
			//adjustFrameTops("Notepad_Values,DecisionHistory,ReferHistory");//added by akshay for proc 7591
			
			try{
				PersonalLoanS.mLogger.info("***********Inside after fragment alignment decision history");
				//for decision fragment made changes 8th dec 2017
				formObject.setVisible("DecisionHistory_updcust", false);
				formObject.setVisible("DecisionHistory_chqbook", false);
				formObject.setVisible("DecisionHistory_Button3", false);
				formObject.setLocked("DecisionHistory_updcust", true);
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
			}
			catch(Exception e){
				PersonalLoanS.mLogger.info( "Exception in Decision fragment load");
				printException(e);

			} 	
			//PersonalLoanS.mLogger.info("***********Inside after making field visible");
			finally{
				setDataInMultipleAppGrid();
			}
		} 	
		
		//shifted from PLCOmmonCode to here by akshay on 17/1/18
		else if ("IncomingDoc".equalsIgnoreCase(pEvent.getSource().getName())) {

			if("".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP")) && NGFUserResourceMgr_PL.getGlobalVar("PL_false").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")))
				formObject.setVisible("IncomingDoc_UploadSig",false);
			else if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP")) || NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
				formObject.setVisible("IncomingDoc_ViewSIgnature",false);
				formObject.setVisible("IncomingDoc_UploadSig",true);
			
			}
			fetchIncomingDocRepeater();
		}	
		
		else if ("ReferHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			if(formObject.getNGValue("decision").contains("Compliance")|| formObject.getNGValue("decision").contains("FCU")){
				//AddInReferGrid();
			}
		} //Added By Akshay for Multiple Refer	


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
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setNGValue("Final_Limit", formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
		formObject.setNGValue("cmplx_IncomeDetails_totSal", formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalTai"));//added for proc 9940
	}


	public void submitFormCompleted(FormEvent pEvent) throws ValidatorException {
		//empty method
		
		//++ below code added by abhishek on 04/01/2018 for EFMS refresh functionality
		
		PersonalLoanS.mLogger.info("Inside PL PROCESS ddvtchecker submitFormCompleted()" + pEvent.getSource()); 
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		List<String> objInput=new ArrayList<String>();
	
		objInput.add("Text:"+formObject.getWFWorkitemName());
		objInput.add("Text:"+"Waiting for Approval");
		PersonalLoanS.mLogger.info("objInput args are: "+objInput.get(0));

		
		List<Object> objOutput=new ArrayList<Object>();
		
		objOutput.add("Text");
	PersonalLoanS.mLogger.info("Before executing procedure ng_RLOS_CheckWriteOff");
	objOutput= formObject.getDataFromStoredProcedure("ng_EFMS_InsertData", objInput,objOutput);

			//++ above code added by abhishek on 04/01/2018 for EFMS refresh functionality
	}



	public void submitFormStarted(FormEvent arg0) throws ValidatorException {

		//changes by saurabh on 20th july 17.
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		saveIndecisionGrid();
		CustomSaveForm();
		
		try
		{
		formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
		formObject.setNGValue("ddvt_checker_dec", formObject.getNGValue("cmplx_Decision_Decision"));
		formObject.setNGValue("ReferTo", formObject.getNGValue("DecisionHistory_ReferTo"));
		formObject.setNGValue("Next_Workstep","");
		if("Approve".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")))
		  {
			 PersonalLoanS.mLogger.info("Inside  approve DDVT checker");
			  formObject.setNGValue("q_parllel_seq", 1);
			  formObject.setNGValue("q_s_count", 1);
			 
			  formObject.setNGValue("VAR_INT3", 1);			 
			  formObject.setNGValue("VAR_INT5", 1);
			  
			  PersonalLoanS.mLogger.info("Inside  approve " + formObject.getNGValue("q_parllel_seq"));
		  }
		LoadReferGrid();
		
		//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
		Custom_fragmentSave("DecisionHistory");//added by akshay on 18/9/18 to save data in multiple app grid
	//	saveIndecisionGridCSM();//Arun (01-12-17) for Decision history to appear in the grid
		
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

