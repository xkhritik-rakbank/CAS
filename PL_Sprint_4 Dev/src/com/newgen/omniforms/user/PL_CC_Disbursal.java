/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PL_Disbursal.java
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;

import org.apache.log4j.Logger;

import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;


public class PL_CC_Disbursal extends PLCommon implements FormListener
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
        PersonalLoanS.mLogger.info( "Inside formLoaded()" + pEvent.getSource().getName());		
        makeSheetsInvisible("Tab1", "8,9,12,13,14,15,16");//Changes done by shweta for jira #1768 to hide CPV tab
        FormReference formObject = FormContext.getCurrentInstance().getFormReference();
	    formObject.setNGValue("Mandatory_Frames", NGFUserResourceMgr_PL.getGlobalVar("Disbural_Frame_Name"));
	}


	public void formPopulated(FormEvent pEvent) 
	{
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			PersonalLoanS.mLogger.info("Inside CC_Disbursal PL");

			new PersonalLoanSCommonCode().setFormHeader(pEvent);
			String Product_Name=formObject.getNGValue("CC_Creation_Product");
			PersonalLoanS.mLogger.info("Inside CC_Disbursal PL : "  + Product_Name);
			//formObject.setVisible("Card_Details", true);
			if(Product_Name.equalsIgnoreCase("LI") || Product_Name.equalsIgnoreCase("PU") || Product_Name.equalsIgnoreCase("PULI")){
	    		formObject.setVisible("Limit_Increase", true);
	    		formObject.setVisible("CC_Creation", false);
	    		formObject.setTop("Limit_Increase",formObject.getTop("CC_Creation"));
	    	 }
	    	 else{
	    		 formObject.setVisible("Limit_Increase", false);
	    		 formObject.setVisible("CC_Creation", true);
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
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PersonalLoanS.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

		if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklistCustomer();
			formObject.setLocked("Customer_Frame1",true);
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
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct with(nolock)");
			LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_ApplicationType with (nolock) order by code");
		}

		else if ("GuarantorDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("GuarantorDetails_Frame1",true);
			loadPicklist_Guarantor();
		}

		else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("IncomeDetails_Frame1",true);
			LoadPickList("cmplx_IncomeDetails_StatementCycle", "select '--Select--' union select convert(varchar, description) from NG_MASTER_StatementCycle with (nolock)");
			
		}

		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ExtLiability_Frame1",true);
			LoadPickList("Liability_New_worststatuslast24", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from ng_master_Aecb_Codes with (nolock) order by code");

		}

		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist4();
			formObject.setLocked("EMploymentDetails_Frame1",true);
		}

		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklistELigibiltyAndProductInfo();

			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
			
			loadEligibilityData();
		}

		else if ("LoanDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist_LoanDetails();
			
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

		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist_Address();
			formObject.setLocked("AddressDetails_Frame1",true);
		}

		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadView(pEvent.getSource().getName());
			PersonalLoanS.mLogger.info("Inside alternate contact details fragment (Disbursal) :"+formObject.getNGValue("Card_Dispatch_Option"));
			String cardDisp=formObject.getNGSelectedItemText("AlternateContactDetails_carddispatch");
			if("International dispatch-INTL".equalsIgnoreCase(cardDisp) || "Holding WI- HOLD".equalsIgnoreCase(cardDisp) || "Card centre collection".equalsIgnoreCase(cardDisp) || "COURIER".equalsIgnoreCase(cardDisp)){
    			formObject.setLocked("CardDispatchToButton",true);
    		}
    		else
    		{
    			formObject.setLocked("CardDispatchToButton",false);
    		}
			//change by saurabh for air arabia functionality.
			AirArabiaValid();
			enrollrewardvalid();
			LoadPickList("AlternateContactDetails_CustomerDomicileBranch", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock) order by code");
			//change by saurabh on 7th Dec
			//LoadPickList("AlternateContactDetails_CardDisp", "select '--Select--' as description,'' as code union all select convert(varchar,description),code from NG_MASTER_Dispatch order by code ");// Load picklist added by aman to load the picklist in card dispatch to
			formObject.setLocked("AltContactDetails_ContactDetails_Save",false);
			formObject.setEnabled("AltContactDetails_ContactDetails_Save",true);
		}

		else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadpicklistCardDetails();
			formObject.setLocked("CardDetails_Frame1",true);
			loadDataInCRNGrid();
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
			
			loadPicklist_suppCard();//added by akshay on 12/3/18 for drop 4
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

		else if ("FinacleCore".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("FinacleCore_Frame1",true);
			formObject.setLocked("FinacleCore_DatePicker2", true);
			formObject.setLocked("FinacleCore_cheqretdate", true);
		}

		else if ("MOL1".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("MOL1_Frame1",true);
		}

		else if ("WorldCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("WorldCheck1_Frame1",true);
		}

		else if ("RejectEnq".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("RejectEnq_Frame1",true);
		}

		else if ("SalaryEnq".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("SalaryEnq_Frame1",true);
		}

		else if ("CustDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

			LoadPickList("cmplx_CustDetailVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");
			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_CustDetailVerification_mobno1_ver","cmplx_CustDetailVerification_mobno2_ver","cmplx_CustDetailVerification_dob_ver","cmplx_CustDetailVerification_POBoxno_ver","cmplx_CustDetailVerification_emirates_ver","cmplx_CustDetailVerification_persorcompPOBox_ver","cmplx_CustDetailVerification_hcountrytelno_ver","cmplx_CustDetailVerification_hcontryaddr_ver","cmplx_CustDetailVerification_email1_ver","cmplx_CustDetailVerification_email2_ver","cmplx_CustDetailVerification_Mother_name_ver");//pcasi-1003
			LoadPickList("cmplx_CustDetailVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");
			LoadPicklistVerification(LoadPicklist_Verification);
			//code changed by nikhil for CPV Changes 16-04-2019
			formObject.setLocked("CustDetailVerification_Frame1",true);
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
		}

		else if ("LoanandCard".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("LoanandCard_Frame1",true);
		}
		else if ("CC_Creation".equalsIgnoreCase(pEvent.getSource().getName())) {

			//formObject.setLocked("LoanandCard_Frame1",true);
			formObject.setLocked("CC_Creation_Frame3",true);//added by akshay for proc 7746
			formObject.setLocked("CC_Creation_Save", false);
			expandDecision();
			LoadPickList("CC_Creation_Product","select '--Select--' as description,'' as code union select distinct convert(varchar, Description),code from ng_master_cardProduct with (nolock) order by code");
			openDemographicTabs();
			loadInDecGrid();
			int framestate1=formObject.getNGFrameState("IncomeDEtails");
			if(framestate1 == 0){
				PersonalLoanS.mLogger.info("Incomedetails");
			}
			else {
				//formObject.fetchFragment("IncomeDEtails", "IncomeDetails", "q_IncomeDetails");
				PersonalLoanS.mLogger.info("fetched income details");
				//formObject.setTop("IncomeDEtails",formObject.getTop("ProductDetailsLoader")+formObject.getHeight("ProductDetailsLoader"));
				expandIncome();
			}
			int framestate2=formObject.getNGFrameState("EmploymentDetails");
			PersonalLoanS.mLogger.info("framestate for Employment is: "+framestate2);
			if(framestate2 == 0){
				PersonalLoanS.mLogger.info("EmploymentDetails");
			}
			else {
				formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmpDetails");
				PersonalLoanS.mLogger.info("fetched employment details");
			}
			
			int framestate3=formObject.getNGFrameState("EligibilityAndProductInformation");
			if(framestate3 == 0){
				PersonalLoanS.mLogger.info("EligibilityAndProductInformation");
			}
			else {
				formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligibilityProdInfo");
				PersonalLoanS.mLogger.info("fetched EligibilityAndProductInformation details");
			}
			int framestate4=formObject.getNGFrameState("Internal_External_Liability");
			if(framestate4 == 0){
				PersonalLoanS.mLogger.info("Internal_External_Liability");
			}
			else {
				formObject.fetchFragment("InternalExternalLiability", "Liability_New", "q_Liabilities");
				PersonalLoanS.mLogger.info("fetched Internal_External_Liability details");
			}
			int framestate_guatantor=formObject.getNGFrameState("GuarantorDet");
		//	PersonalLoanS.mLogger.info("framestate for GuarantorDet is: "+framestate2);
			if(framestate_guatantor != 0 && Float.parseFloat(formObject.getNGValue("cmplx_Customer_age"))<21){
				formObject.fetchFragment("GuarantorDet", "GuarantorDetails", "q_Guarantor");
				loadPicklist_Guarantor();
				formObject.setTop("IncomeDEtails",formObject.getTop("GuarantorDet")+formObject.getHeight("GuarantorDet")+15);
			//	adjustFrameTops("GuarantorDet,IncomeDEtails");
			}
			loadInCCCreationGrid();
		}
		else if ("Limit_Inc".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("Limit_Inc_Frame4",false);
			loadDataInLimitInc();
		}
		

		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			notepad_load();
			 notepad_withoutTelLog();
			
		}

		else if ("SmartCheck".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("SmartCheck_Frame1",true);
		}

		else if ("Compliance".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("Compliance_Frame1",true);
		}

		else if ("CC_Loan".equalsIgnoreCase(pEvent.getSource().getName())){
			LoadPickList("transType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_TransactionType with (nolock) where IsActive = 'Y' order by code");
			LoadPickList("transferMode", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_TransferMode with (nolock) where IsActive = 'Y' order by code");			 
			loadPicklist_ServiceRequest(); //hritik pcasi 3464
		}
		
		else if ("FCU_Decision".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("FCU_Decision_Frame1",true);
		}
		else if ("Loan_Disbursal".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Loan_Disbursal_Frame2",true);
		}		
		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			if(NGFUserResourceMgr_PL.getGlobalVar("PL_false").equalsIgnoreCase(formObject.getNGValue("AlternateContactDetails_RetainAccIfLoanReq"))){
				PersonalLoanS.mLogger.info( "after making buttons visible"+formObject.getNGValue("AlternateContactDetails_RetainAccIfLoanReq"));
				//formObject.setVisible("CC_Creation_Update_Customer", true);
				PersonalLoanS.mLogger.info( "after making buttons visible");
			}
			//for decision fragment made changes 8th dec 2017
			PersonalLoanS.mLogger.info("***********Inside decision history");
			//below line modified by akshay on 9 feb 18
			fragment_ALign("Decision_Label1,cmplx_Decision_VERIFICATIONREQUIRED#Decision_Label3,cmplx_Decision_Decision#DecisionHistory_Label1,cmplx_Decision_ReferTo#DecisionHistory_Label11,DecisionHistory_DecisionReasonCode#Decision_Label4,cmplx_Decision_REMARKS#\n#cmplx_Decision_MultipleApplicantsGrid#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
			formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
			formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
			
			PersonalLoanS.mLogger.info("***********Inside after fragment alignment decision history");
	
			loadPicklist3();
			//setDataInMultipleAppGrid();
		
		
			/*formObject.setNGValue("cmplx_Decision_Decision", "--Select--");
			
			formObject.setLocked("cmplx_Decision_VERIFICATIONREQUIRED",true);
		

		
			formObject.setTop("Decision_Label1", 8);
			
			formObject.setVisible("cmplx_Decision_waiveoffver",false);
			formObject.setTop("cmplx_Decision_VERIFICATIONREQUIRED", 23);*/
			
			//for decision fragment made changes 8th dec 2017
		

		}	


	}


	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{
		//PersonalLoanS.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		switch(pEvent.getType())
		{	

		case FRAME_EXPANDED:
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
		//empty method	

	}


	public void submitFormCompleted(FormEvent arg0) throws ValidatorException {
		try{
			saveIndecisionGrid();
			//++ below code added by Deepak on 15/07/2018 for EFMS refresh functionality
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();

			if(NGFUserResourceMgr_PL.getGlobalVar("PL_Submit_Desc").equalsIgnoreCase(formObject.getNGValue("decision"))){
				List<String> objInput=new ArrayList<String>();
				objInput.add("Text:"+formObject.getWFWorkitemName());
				objInput.add("Text:"+"Approve");
				PersonalLoanS.mLogger.info("objInput args are: "+objInput);
				List<Object> objOutput=new ArrayList<Object>();
				objOutput.add("Text");
				objOutput= formObject.getDataFromStoredProcedure("ng_EFMS_InsertData", objInput,objOutput);
			}
		} catch (Exception e){
			PersonalLoanS.mLogger.info("Exception occured in submitFormCompleted"+e.getStackTrace());
		}
	}


	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try
		{
			//Deepak 24 Dec Code moved by Deepak for PCSP 316
			if(formObject.isVisible("AltContactDetails_Frame1")==false){
				formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
				formObject.setNGFrameState("Alt_Contact_container", 0);

			}
			
			String cardDisp=formObject.getNGValue("AlternateContactDetails_carddispatch");
			if("009".equalsIgnoreCase(cardDisp) ){
				formObject.setNGValue("Card_Dispatch_Option","International dispatch-INTL");
			}
			/*else if("Holding WI- HOLD".equalsIgnoreCase(cardDisp))
    		{
    			formObject.setNGValue("Card_Dispatch_Option","Holding WI- HOLD");
    		}*/
			else if("151".equalsIgnoreCase(cardDisp))
			{
				formObject.setNGValue("Card_Dispatch_Option","Card centre collection");
			}
			else if("998".equalsIgnoreCase(cardDisp))
			{
				formObject.setNGValue("Card_Dispatch_Option","COURIER");
			}
			else
			{
				formObject.setNGValue("Card_Dispatch_Option","Branch");
			}
			//Card_Dispatch_Option used to keep track if cc_Disbursal WI is created or not 

			CustomSaveForm();
			formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));

			//Deepak 30 Dec Change done for PCSP-386
			if("Refer".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")))
			{	
			 LoadReferGrid();
			}
			
			//String loan_type=formObject.getNGValue("loan_type");
			//change by saurabh for adding submit condition
			if("Submit".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision"))  && !check_MurabhaFileConfirmed(formObject)){
				throw new ValidatorException(new FacesMessage("Please generate and confirm Murabha File!!!!"));
			}
			
		}
		catch(ValidatorException v){
			PersonalLoanS.mLogger.info("Inside validator exception before throwing again.");
			throw new ValidatorException(new FacesMessage("Please generate and confirm Murabha File!!!!"));
		}
		catch(Exception Ex)
		{
			PersonalLoanS.mLogger.info("Exception occured in submit form started method" +Ex.getMessage());
		}
		//loadInDecGrid();
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

