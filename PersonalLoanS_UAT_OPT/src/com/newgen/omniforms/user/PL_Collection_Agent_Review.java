/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PL_Collection_Agent_Review.java
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

import java.util.HashMap;

import javax.faces.validator.ValidatorException;

import org.apache.log4j.Logger;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;


public class PL_Collection_Agent_Review extends PLCommon implements FormListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	FormReference formObject = null;
	Logger mLogger=PersonalLoanS.mLogger;

	public void formLoaded(FormEvent pEvent)
	{
		PersonalLoanS.mLogger.info("Inside formLoaded()" + pEvent.getSource().getName());

	}

	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to do tasks on form load   
	 
	***********************************************************************************  */

	public void formPopulated(FormEvent pEvent) 
	{
		try{
			new PersonalLoanSCommonCode().setFormHeader(pEvent);
		}catch(Exception e)
		{
			PersonalLoanS.mLogger.info("Exception:"+e.getMessage());
			printException(e);
		}
	}
	
	//Below code added by yash on 15/12/2017 as per FSD 2.7
	public void fragment_loaded(ComponentEvent pEvent)
	{		mLogger.info(" In PL_Initiation eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
	FormReference formObject = FormContext.getCurrentInstance().getFormReference();

	if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
		//setDisabled();
		formObject.setLocked("Customer_Frame1",true);
	}	

	else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
		formObject.setLocked("Product_Frame1",true);
		LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
		LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_ApplicationType with (nolock) order by code");
	}

	else if ("GuarantorDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

		formObject.setLocked("GuarantorDetails_Frame1",true);
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

		formObject.setLocked("EMploymentDetails_Frame1",true);
	}

	else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {

		formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
		
		loadEligibilityData();
	}

	else if ("LoanDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
		loadPicklist_LoanDetails();
		
		formObject.setLocked("LoanDetails_Frame1",true);
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

		formObject.setLocked("CardDetails_Frame1",true);
	}

	else if ("ReferenceDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

		formObject.setLocked("ReferenceDetails_Frame1",true);
		LoadPickList("ReferenceDetails_ref_Relationship", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Relationship with (nolock) order by code");
		
	}

	else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

		formObject.setLocked("SupplementCardDetails_Frame1",true);
	}

	else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) {

		formObject.setLocked("FATCA_Frame6",true);
		Loadpicklistfatca();
	}

	else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) {

		formObject.setLocked("KYC_Frame1",true);
	}

	else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {
		loadPickListOECD();
		formObject.setLocked("OECD_Frame8",true);
	}
	//below code added by yash on 14/12/2017 for PL_OV
	else if ("PartMatch".equalsIgnoreCase(pEvent.getSource().getName())) {
		//loadPicklist_Address();
		formObject.setLocked("PartMatch_Frame1",true);

	}
	else if ("FinacleCRMIncident".equalsIgnoreCase(pEvent.getSource().getName())) {
		
		formObject.setLocked("FinacleCRMIncident_Frame1",true);

	}
	else if ("FinacleCRMCustInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
		
		formObject.setLocked("FinacleCRMCustInfo_Frame1",true);

	}
	else if ("ExternalBlackList".equalsIgnoreCase(pEvent.getSource().getName())) {
		
		formObject.setLocked("ExternalBlackList_Frame1",true);

	}
	else if ("FinacleCore".equalsIgnoreCase(pEvent.getSource().getName())) {
		
		formObject.setLocked("FinacleCore_Frame1",true);
		LoadPickList("FinacleCore_ChequeType", "select '--Select--' union select convert(varchar, description) from ng_MASTER_Cheque_Type with (nolock)");
		LoadPickList("FinacleCore_TypeOfRetutn", "select '--Select--' union select convert(varchar, description) from ng_MASTER_TypeOfReturn with (nolock)");

	}
	else if ("MOL1".equalsIgnoreCase(pEvent.getSource().getName())) {
		
		formObject.setLocked("MOL1_Frame1",true);

	}
	else if ("RejectEnq".equalsIgnoreCase(pEvent.getSource().getName())) {
		
		formObject.setLocked("RejectEnq_Frame1",true);

	}
	else if ("SalaryEnq".equalsIgnoreCase(pEvent.getSource().getName())) {
		formObject.setLocked("SalaryEnq_Frame1",true);
	}

	else if ("PostDisbursal".equalsIgnoreCase(pEvent.getSource().getName())) {

		formObject.setLocked("PostDisbursal",true);
	}
	else if ("IncomingDoc".equalsIgnoreCase(pEvent.getSource().getName())){
		fetchIncomingDocRepeater();
		formObject.setLocked("IncomingDoc_Frame2",true);
	}
	else if ("ReferHistory".equalsIgnoreCase(pEvent.getSource().getName())){
		
		formObject.setLocked("ReferHistory_Frame1",true);
	}

	else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
		//for decision fragment made changes 8th dec 2017
		PersonalLoanS.mLogger.info("***********Inside decision history of csm");
		//added by yash on 15/12/2017 for PL FSD as per fsd 2.7
		fragment_ALign("Decision_Label3,cmplx_Decision_Decision#DecisionHistory_Label1,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_DecisionReasonCode#Decision_Label4,cmplx_Decision_REMARKS#\n#Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
		formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
		formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
		
		loadPicklist1();
		
		
		
		
		
		loadPicklist3();
	}	
	// disha FSD
	else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
		 formObject.setLocked("NotepadDetails_Frame2",true);
		 formObject.setLocked("NotepadDetails_Frame1",true);
		}


	}
	//above code added by yash on 15/12/2017 as per fsd 2.7


	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{
		PersonalLoanS.mLogger.info("Inside PL_Initiation eventDispatched()--->EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

		switch(pEvent.getType())
		{	

		case FRAME_EXPANDED:
			new PersonalLoanSCommonCode().FrameExpandEvent(pEvent);
			break;

			// Below code by yash on 15/12/2017 as per  FSD 2.7
		case FRAGMENT_LOADED:
			fragment_loaded(pEvent);
			
			//added code added by yash on 15/12/2017 as per FSD 2.7

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
	}


	public void saveFormStarted(FormEvent arg0) throws ValidatorException {
		
		//empty method
	}


	public void submitFormCompleted(FormEvent arg0) throws ValidatorException {
		
		//empty method
	}


	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		
		formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
		saveIndecisionGrid();
	}

}
