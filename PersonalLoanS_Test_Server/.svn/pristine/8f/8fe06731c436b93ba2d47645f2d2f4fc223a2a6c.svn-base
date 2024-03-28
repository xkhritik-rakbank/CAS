package com.newgen.omniforms.user;

import java.util.HashMap;

import javax.faces.validator.ValidatorException;

import org.apache.log4j.Logger;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;

public class PL_CCWaiver extends PLCommon implements FormListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	FormReference formObject = null;
	Logger mLogger=PersonalLoanS.mLogger;

	public void formLoaded(FormEvent pEvent)
	{
		PersonalLoanS.mLogger.info("PL_CCWaiver--->Inside form load");
	}


	public void formPopulated(FormEvent pEvent) 
	{

		//  below code already exist - 10-10-2017 try catch has been uncommented so that at read only mode also header is set
		try{
			
			new PersonalLoanSCommonCode().setFormHeader(pEvent);
			

		}catch(Exception e)
		{
			printException(e);        }
		//  above code already exist - 10-10-2017 try catch has been uncommented so that at read only mode also header is set
	}
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Load fragment      
	 
	***********************************************************************************  */

	public void fragment_loaded(ComponentEvent pEvent){
		PersonalLoanS.mLogger.info(" In PL_Initiation eventDispatched()-->EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		formObject =FormContext.getCurrentInstance().getFormReference();

		if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Customer_Frame1", true);
			//  below code already exist - 10-10-2017
			loadPicklistCustomer();

		}	

		else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Product_Frame1", true);
			//  below code already exist - 10-10-2017

			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
			LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_ApplicationType with (nolock) order by code");

		}

		else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("IncomeDetails_Frame1", true);							
			//  below code already exist - 10-10-2017
			formObject.setVisible("IncomeDetails_Frame3", false);
			formObject.setHeight("Incomedetails", 630);
			formObject.setHeight("IncomeDetails_Frame1", 605); 
			LoadPickList("cmplx_IncomeDetails_StatementCycle", "select '--Select--' union select convert(varchar, description) from NG_MASTER_StatementCycle with (nolock)");
			
		}

		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ExtLiability_Frame1", true);
			LoadPickList("Liability_New_worststatuslast24", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from ng_master_Aecb_Codes with (nolock) order by code");

		}

		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("EMploymentDetails_Frame1",true);
			//  below code already exist - 10-10-2017
			loadPicklist4();

		}

		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
			
			loadEligibilityData();
			
		}

		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("AddressDetails_Frame1", true);
		}

		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())){

			formObject.setLocked("AltContactDetails_Frame1",true);
			LoadpicklistAltcontactDetails();
			
		} 

		//  below code already exist - 10-10-2017
		else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("CardDetails_Frame1",true);
		}

		else if ("ReferenceDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ReferenceDetails_Frame1",true);
			LoadPickList("ReferenceDetails_ref_Relationship", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Relationship with (nolock) order by code");
			
		}
		//  above code already exist - 10-10-2017
		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())){

			formObject.setLocked("FATCA_Frame6", true);
			Loadpicklistfatca();
		}

		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())){

			formObject.setLocked("KYC_Frame1",true);
			
		}

		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())){
			loadPickListOECD();
			formObject.setLocked("OECD_Frame8",true);
			
		}

		else if ("IncomingDoc".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setLocked("IncomingDoc_Frame1", true);
			//  below code already exist - 10-10-2017
			fetchIncomingDocRepeater();
			formObject.setVisible("cmplx_DocName_OVRemarks", false);
			formObject.setVisible("cmplx_DocName_OVDec",false);
			formObject.setVisible("cmplx_DocName_Approvedby",false);
		}
		//  below code already exist - 10-10-2017
		else if ("GuarantorDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("GuarantorDetails_Frame1",true);
		}

		else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("SupplementCardDetails_Frame1",true);
		}
		else if ("CustDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.setLocked("CustDetailVerification_Frame1",true);
		}
		else if ("OfficeandMobileVerification".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setLocked("OfficeandMobileVerification_Frame1",true);
		}
		else if ("LoanandCard".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setLocked("LoanandCard_Frame1",true);
		}
		else if ("EmploymentVerification".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setLocked("EmploymentVerification_Frame1",true);
		}
		else if ("BussinessVerification1".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setLocked("BussinessVerification_Frame1",true);
			formObject.setLocked("BussinessVerification1_Frame1",true);
		}
		else if ("SmartCheck1".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			formObject.setLocked("SmartCheck1_Frame1",true);
		}
		else if ("RiskRating".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("RiskRating_Frame1",true);
		}
		else if ("PartMatch".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("PartMatch_Frame1",true);
			LoadPickList("PartMatch_nationality", "select '--Select--' union select convert(varchar, Description) from ng_MASTER_Country with (nolock)");
		}

		else if ("FinacleCRMIncident".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("FinacleCRMIncident_Frame1",true);
		}

		else if ("FinacleCRMCustInfo".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("FinacleCRMCustInfo_Frame1",true);
		}

		else if ("FinacleCore".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("FinacleCore_Frame1",true);
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

		else if ("ExternalBlackList".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("ExternalBlackList_Frame1",true);
		}

		else if ("SalaryEnq".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("SalaryEnq_Frame1",true);
		}
		else if ("ReferHistory".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setLocked("ReferHistory_Frame1",true);
		}

		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			notepad_load();
			 notepad_withoutTelLog();
				}
		//  above code already exist - 10-10-2017
		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			
			//for decision fragment made changes 8th dec 2017
			
			PersonalLoanS.mLogger.info("***********Inside decision history");
			fragment_ALign("Decision_Label1,cmplx_Decision_VERIFICATIONREQUIRED#Decision_Label3,cmplx_Decision_Decision#DecisionHistory_Label1,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_DecisionReasonCode#Decision_Label4,cmplx_Decision_REMARKS#\n#Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
			formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
			formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
			
			PersonalLoanS.mLogger.info("***********Inside after fragment alignment decision history");
	
			
			loadPicklist1();

			/*formObject.setVisible("DecisionHistory_chqbook",false);
			formObject.setNGValue("cmplx_Decision_Decision", "--Select--");

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
			//disha FSD P2 - CPV required field to be disabled at Decision tab
			formObject.setLocked("cmplx_Decision_VERIFICATIONREQUIRED",true);
			formObject.setLocked("cmplx_Decision_Decision",true);
			formObject.setLocked("cmplx_Decision_REMARKS",true);
			formObject.setLocked("DecisionHistory_Frame1",true);	*/
			
			//for decision fragment made changes 8th dec 2017
		
			
		} 



		

	}


	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException {
		

		PersonalLoanS.mLogger.info("Inside PL_Initiation eventDispatched()-->EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		formObject =FormContext.getCurrentInstance().getFormReference();

		switch(pEvent.getType()) {

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


	public void initialize() {
		//empty method
		}
	


	public void saveFormCompleted(FormEvent pEvent) throws ValidatorException {//empty method
		}
	


	public void saveFormStarted(FormEvent pEvent) throws ValidatorException {
		//empty method
	}


	public void submitFormCompleted(FormEvent pEvent) throws ValidatorException {
		
		//empty method
	}


	public void submitFormStarted(FormEvent pEvent) throws ValidatorException {
		//empty method
		//below code added by nikhil 08/12/17
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setNGValue("CC_Waiver_dec", formObject.getNGValue("cmplx_Decision_Decision"));
		}


	public void continueExecution(String arg0, HashMap<String, String> arg1) {
		
		//empty method
	}



}
