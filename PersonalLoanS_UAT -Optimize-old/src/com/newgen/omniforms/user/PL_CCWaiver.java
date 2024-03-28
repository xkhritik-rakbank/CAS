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
		mLogger.info("PL_CCWaiver--->Inside form load");
	}


	public void formPopulated(FormEvent pEvent) 
	{

		// ++ below code already exist - 10-10-2017 try catch has been uncommented so that at read only mode also header is set
		try{
			//loadPicklistCustomer();
			//formObject.fetchFragment("Part_Match", "PartMatch", "q_PartMatch");
			//formObject.setNGFrameState("Part_Match",0);
			//loadPicklist_PartMatch();
			new PersonalLoanSCommonCode().setFormHeader(pEvent);
			//LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
			//LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where Code='PL' and ActivityName='Branch_Init'");

		}catch(Exception e)
		{
			printException(e);        }
		// ++ above code already exist - 10-10-2017 try catch has been uncommented so that at read only mode also header is set
	}

	public void fragment_loaded(ComponentEvent pEvent){
		mLogger.info(" In PL_Initiation eventDispatched()-->EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		formObject =FormContext.getCurrentInstance().getFormReference();

		if (pEvent.getSource().getName().equalsIgnoreCase("Customer")) {
			formObject.setLocked("Customer_Frame1", true);
			// ++ below code already exist - 10-10-2017
			loadPicklistCustomer();

		}	

		else if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
			formObject.setLocked("Product_Frame1", true);
			// ++ below code already exist - 10-10-2017

			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
			LoadPickList("AppType", "select '--Select--' union select convert(varchar, desciption) from ng_master_ApplicationType");

		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("IncomeDetails")) {
			formObject.setLocked("IncomeDetails_Frame1", true);							
			// ++ below code already exist - 10-10-2017
			formObject.setVisible("IncomeDetails_Frame3", false);
			formObject.setHeight("Incomedetails", 630);
			formObject.setHeight("IncomeDetails_Frame1", 605); 
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("Liability_New")) {
			formObject.setLocked("ExtLiability_Frame1", true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("EMploymentDetails")) {
			formObject.setLocked("EMploymentDetails_Frame1",true);
			// ++ below code already exist - 10-10-2017
			loadPicklist4();

		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("ELigibiltyAndProductInfo")) {
			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
			//	formObject.setEnabled("ELigibiltyAndProductInfo_Save",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("AddressDetails")) {
			formObject.setLocked("AddressDetails_Frame1", true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("AltContactDetails")){

			formObject.setLocked("AltContactDetails_Frame1",true);
			//	formObject.setEnabled("AltContactDetails_ContactDetails_Save",true);
		} 

		// ++ below code already exist - 10-10-2017
		else if (pEvent.getSource().getName().equalsIgnoreCase("CardDetails")) {

			formObject.setLocked("CardDetails_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("ReferenceDetails")) {

			formObject.setLocked("ReferenceDetails_Frame1",true);
		}
		// ++ above code already exist - 10-10-2017
		else if (pEvent.getSource().getName().equalsIgnoreCase("FATCA")){

			formObject.setLocked("FATCA_Frame6", true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("KYC")){

			formObject.setLocked("KYC_Frame1",true);
			//	formObject.setEnabled("KYC_Save",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("OECD")){

			formObject.setLocked("OECD_Frame8",true);
			//	formObject.setEnabled("OECD_Save",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("IncomingDoc")){
			formObject.setLocked("IncomingDoc_Frame1", true);
			// ++ below code already exist - 10-10-2017
			fetchIncomingDocRepeater();
			formObject.setVisible("cmplx_DocName_OVRemarks", false);
			formObject.setVisible("cmplx_DocName_OVDec",false);
			formObject.setVisible("cmplx_DocName_Approvedby",false);
		}
		// ++ below code already exist - 10-10-2017
		else if (pEvent.getSource().getName().equalsIgnoreCase("GuarantorDetails")) {

			formObject.setLocked("GuarantorDetails_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("SupplementCardDetails")) {

			formObject.setLocked("SupplementCardDetails_Frame1",true);
		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("CustDetailVerification")) 
		{
			formObject.setLocked("CustDetailVerification_Frame1",true);
		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("OfficeandMobileVerification"))
		{
			formObject.setLocked("OfficeandMobileVerification_Frame1",true);
		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("LoanandCard"))
		{
			formObject.setLocked("LoanandCard_Frame1",true);
		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("EmploymentVerification"))
		{
			formObject.setLocked("EmploymentVerification_Frame1",true);
		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("BussinessVerification1"))
		{
			formObject.setLocked("BussinessVerification_Frame1",true);
			formObject.setLocked("BussinessVerification1_Frame1",true);
		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("SmartCheck1")) 
		{
			formObject.setLocked("SmartCheck1_Frame1",true);
		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("RiskRating")) {

			formObject.setLocked("RiskRating_Frame1",true);
		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("PartMatch")) {

			formObject.setLocked("PartMatch_Frame1",true);
			LoadPickList("PartMatch_nationality", "select '--Select--' union select convert(varchar, Description) from ng_MASTER_Country with (nolock)");
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMIncident")) {

			formObject.setLocked("FinacleCRMIncident_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCRMCustInfo")) {

			formObject.setLocked("FinacleCRMCustInfo_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("FinacleCore")) {

			formObject.setLocked("FinacleCore_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("MOL1")) {

			formObject.setLocked("MOL1_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1")) {

			formObject.setLocked("WorldCheck1_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("RejectEnq")) {

			formObject.setLocked("RejectEnq_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("ExternalBlackList")) {

			formObject.setLocked("ExternalBlackList_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("SalaryEnq")) {

			formObject.setLocked("SalaryEnq_Frame1",true);
		}
		else if (pEvent.getSource().getName().equalsIgnoreCase("ReferHistory"))
		{
			formObject.setLocked("ReferHistory_Frame1",true);
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails"))
		{
			formObject.setLocked("NotepadDetails_Frame1",true);
			formObject.setLocked("NotepadDetails_Frame2",true);
			formObject.setVisible("NotepadDetails_Frame3",false);
			String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
			mLogger.info("Activity name is:" + sActivityName);
			int user_id = formObject.getUserId();
			String user_name = formObject.getUserName();
			user_name = user_name+"-"+user_id;					
			formObject.setNGValue("NotepadDetails_insqueue",sActivityName);
			formObject.setNGValue("NotepadDetails_Actusername",user_name); 
			formObject.setNGValue("NotepadDetails_user",user_name); 
			formObject.setLocked("NotepadDetails_noteDate",true);
			formObject.setLocked("NotepadDetails_Actusername",true);
			formObject.setLocked("NotepadDetails_user",true);
			formObject.setLocked("NotepadDetails_insqueue",true);
			formObject.setLocked("NotepadDetails_Actdate",true);
			formObject.setVisible("NotepadDetails_save",true);
			formObject.setLocked("NotepadDetails_notecode",true);

			//formObject.setHeight("NotepadDetails_Frame1",450);//Arun (23/09/17)
			formObject.setTop("NotepadDetails_save",440);//Arun (23/09/17)
			LoadPickList("NotepadDetails_notedesc", "select '--Select--' union select  description from ng_master_notedescription");
		}
		// ++ above code already exist - 10-10-2017
		else if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory")) {

			// ++ below code already exist - 10-10-2017		
			formObject.setVisible("DecisionHistory_chqbook",false);
			loadPicklist1();

			//disha FSD --Select-- come at top of drop down
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
			formObject.setLocked("DecisionHistory_Frame1",true);	
			// ++ above code already exist - 10-10-2017
			//	loadPicklist1();
			//Common function for decision fragment textboxes and combo visibility
			//decisionLabelsVisibility();
		} 



		//code by saurabh gupta on 27th June 17.
		/*String hiddenFieldString = formObject.getNGValue("fields_string");
			if(hiddenFieldString!=null && !hiddenFieldString.equalsIgnoreCase("") && !hiddenFieldString.equalsIgnoreCase(" ")){
				String[] fieldNames = hiddenFieldString.split(",");
				setChangedFieldsColor(fieldNames);
			}*/

	}


	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException {
		// TODO Auto-generated method stub

		mLogger.info("Inside PL_Initiation eventDispatched()-->EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
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


	public void initialize() {/*
		PL_SKLogger.writeLog("PersonnalLoanS>  PL_Iniation", "Inside PL PROCESS initialize()" );
		  formObject =FormContext.getCurrentInstance().getFormReference();

	 */}


	public void saveFormCompleted(FormEvent pEvent) throws ValidatorException {/*
		PL_SKLogger.writeLog("PersonnalLoanS>  PL_Iniation", "Inside PL PROCESS saveFormCompleted()" + pEvent.getSource());
		  formObject =FormContext.getCurrentInstance().getFormReference();

	 */}


	public void saveFormStarted(FormEvent pEvent) throws ValidatorException {/*
		  formObject =FormContext.getCurrentInstance().getFormReference();
		PL_SKLogger.writeLog("PersonnalLoanS>  PL_Iniation", "Inside PL PROCESS saveFormStarted()" + pEvent.getSource());
		formObject.setNGValue("get_parent_data",queryData_load);
		PL_SKLogger.writeLog("PersonnalLoanS","Final val of queryData_load:"+ formObject.getNGValue("get_parent_data"));
	 */}


	public void submitFormCompleted(FormEvent pEvent) throws ValidatorException {/*
		PL_SKLogger.writeLog("PersonnalLoanS>  PL_Iniation", "Inside PL PROCESS submitFormCompleted()" + pEvent.getSource());

	 */}


	public void submitFormStarted(FormEvent pEvent) throws ValidatorException {/*
		PL_SKLogger.writeLog("PersonnalLoanS>  PL_Iniation", "Inside PL PROCESS submitFormStarted()" + pEvent.getSource());

		 FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		 formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));

		 if(formObject.getNGValue("cmplx_Decision_Decision").equalsIgnoreCase("Parallel"))
		 {
			 formObject.setNGValue("parallel_sequential","P");
		 }
		 else
		 {
			 formObject.setNGValue("parallel_sequential","S");
		 }
		saveIndecisionGrid();
	 */}


	public void continueExecution(String arg0, HashMap<String, String> arg1) {
		// TODO Auto-generated method stub

	}



}
