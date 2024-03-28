/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PL_Hold_CPV.java
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


public class PL_CPV_Analyst extends PLCommon implements FormListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	Logger mLogger=PersonalLoanS.mLogger;

	public void formLoaded(FormEvent pEvent)
	{
		mLogger.info("PL_CPV_Analyst--->Inside formLoaded()" + pEvent.getSource().getName());

	}


	public void formPopulated(FormEvent pEvent) 
	{
		try{
			new PersonalLoanSCommonCode().setFormHeader(pEvent);
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

		PersonalLoanS.mLogger.info(" In PL_Initiation eventDispatched()-->EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			
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

		else if ("CustDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("cmplx_CustDetailVerification_Mob_No1_val",true);
			formObject.setLocked("cmplx_CustDetailVerification_Mob_No2_val",true);
			formObject.setLocked("cmplx_CustDetailVerification_dob_val",true);
			formObject.setLocked("cmplx_CustDetailVerification_POBoxNo_val",true);
			formObject.setLocked("cmplx_CustDetailVerification_emirates_val",true);
			formObject.setLocked("cmplx_CustDetailVerification_persorcompPOBox_val",true);
			formObject.setLocked("cmplx_CustDetailVerification_Resno_val",true);
			formObject.setLocked("cmplx_CustDetailVerification_Offtelno_val",true);
			formObject.setLocked("cmplx_CustDetailVerification_hcountrytelno_val",true);
			formObject.setLocked("cmplx_CustDetailVerification_hcountryaddr_val",true);
			formObject.setLocked("cmplx_CustDetailVerification_email1_val",true);
			formObject.setLocked("cmplx_CustDetailVerification_email2_val",true);
		}

		else if ("OfficeandMobileVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("cmplx_OffVerification_fxdsal_val",true);
			formObject.setLocked("cmplx_OffVerification_accprovd_val",true);
			formObject.setLocked("cmplx_OffVerification_desig_val",true);
			formObject.setLocked("cmplx_OffVerification_doj_val",true);
			formObject.setLocked("cmplx_OffVerification_cnfrminjob_val",true);
		}

		else if ("LoanandCard".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setLocked("cmplx_LoanandCard_loanamt_val",true);
			formObject.setLocked("cmplx_LoanandCard_tenor_val",true);
			formObject.setLocked("cmplx_LoanandCard_emi_val",true);
			formObject.setLocked("cmplx_LoanandCard_islorconv_val",true);
			formObject.setLocked("cmplx_LoanandCard_firstrepaydate_val",true);
			formObject.setLocked("cmplx_LoanandCard_cardtype_val",true);
			formObject.setLocked("cmplx_LoanandCard_cardlimit_val",true);
		}

		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			
			//for decision fragment made changes 8th dec 2017
			loadPicklist1();
								// ++ below code already exist - 10-10-2017
								formObject.setNGValue("cmplx_Decision_Decision", "--Select--");
								
								PersonalLoanS.mLogger.info("***********Inside checker decision history");
					         	fragment_ALign("Decision_Label3,cmplx_Decision_Decision#DecisionHistory_Label1,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_DecisionReasonCode#DecisionHistory_Label12,cmplx_Decision_NoofAttempts#\n#Decision_Label4,cmplx_Decision_REMARKS#\n#Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
					 			PersonalLoanS.mLogger.info("***********Inside checker after fragment alignment decision history");
					 			formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
								formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
								
								
			                
			                	/*formObject.setLocked("cmplx_Decision_VERIFICATIONREQUIRED",true);
			                	
			                	formObject.setVisible("cmplx_Decision_waiveoffver",false);

			                	
			                	formObject.setTop("Decision_Label1", 8);
			                	formObject.setTop("cmplx_Decision_VERIFICATIONREQUIRED", 23);


			                	formObject.setTop("cmplx_Decision_cmplx_gr_decision", 226);		
			                	formObject.setTop("DecisionHistory_save", 400);	*/
								
								//for decision fragment made changes 8th dec 2017
								
			                	//Common function for decision fragment textboxes and combo visibility
			                	//decisionLabelsVisibility();
		} 	
		// disha FSD
		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			notepad_load();
			formObject.setVisible("NotepadDetails_Frame3",true);
			
		}

	}

	
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{

		PersonalLoanS.mLogger.info("Inside PL_Initiation eventDispatched()--->EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

		switch(pEvent.getType())
		{	

		case FRAME_EXPANDED:
			PersonalLoanS.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
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
		
		//empty method
	}


	public void saveFormStarted(FormEvent arg0) throws ValidatorException {
		//empty method

	}


	public void submitFormCompleted(FormEvent arg0) throws ValidatorException {
		
		//empty method
	}


	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//below code added by nikhil 7/12/17
		//formObject.setNGValue("CPV_dec", formObject.getNGValue("cmplx_Decision_Decision"));

		formObject.setNGValue("CPV_Analyst_dec", formObject.getNGValue("cmplx_Decision_Decision"));//Done by aman 17/12
		PersonalLoanS.mLogger.info( "cmplx_Decision_Decision:" +  formObject.getNGValue("cmplx_Decision_Decision")+ "#ControlName#");
		

		//formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
		if("Approve Sub to CIF".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")))
	    {
	        formObject.setNGValue("IS_Approve_Cif","Y");
	    }

		saveIndecisionGrid();
	}

}

