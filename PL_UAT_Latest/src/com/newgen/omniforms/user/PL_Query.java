package com.newgen.omniforms.user;

import java.util.HashMap;

import javax.faces.validator.ValidatorException;

import org.apache.log4j.Logger;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;

public class PL_Query extends PLCommon implements FormListener{

	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	Logger mLogger=PersonalLoanS.mLogger;
	public void formLoaded(FormEvent pEvent)
	{
		mLogger.info("Inside PL_Query-->FormLOaded()");
	}


	public void formPopulated(FormEvent pEvent) 
	{
		new PersonalLoanSCommonCode().setFormHeader(pEvent);
		//empty method
		
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
		mLogger.info(" In PL_Initiation eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();	

		if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Customer_Frame1", true);
			loadPicklistCustomer();

		}	

		else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Product_Frame1", true);
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct with(nolock)");
			loadPicklistProduct("Personal Loan");
		}

		else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("IncomeDetails_Frame1", true);				
			LoadPickList("cmplx_IncomeDetails_StatementCycle", "select '--Select--' union select convert(varchar, description) from NG_MASTER_StatementCycle with (nolock)");
			
		}

		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ExtLiability_Frame1", true);
			LoadPickList("Liability_New_worststatuslast24", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from ng_master_Aecb_Codes with (nolock) order by code");
			if("CAD_Analyst1".equalsIgnoreCase(formObject.getWFActivityName()) || "Cad_Analyst2".equalsIgnoreCase(formObject.getWFActivityName()))
			{
				formObject.setVisible("Liability_New_Label1",false);
				formObject.setVisible("Liability_New_MOB",false);
				formObject.setVisible("Liability_New_Label2",true);
				formObject.setVisible("Liability_New_Utilization",true);
				formObject.setVisible("Liability_New_Label3",false);
				formObject.setVisible("Liability_New_Outstanding",false);
				formObject.setVisible("Liability_New_Delinkinlast3months",false);
				formObject.setVisible("Liability_New_DPDinlast18months",false);
				formObject.setVisible("Liability_New_DPDinlast6",false);
				formObject.setVisible("Liability_New_Label4",false);
				formObject.setVisible("Liability_New_writeOfAmount",false);
				formObject.setVisible("Liability_New_Label5",false);
				formObject.setVisible("Liability_New_worstStatusInLast24",false);
				formObject.setVisible("Liability_New_Label2",false);
				formObject.setVisible("Liability_New_Utilization",false);
				formObject.setVisible("Liability_New_Label10",false);
				formObject.setVisible("Liability_New_rejApplinlast3months",false);
			}

		}

		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("EMploymentDetails_Frame1",true);
			loadPicklist4();
		}

		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
			
		}

		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("AddressDetails_Frame1", true);
		}

		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())){

			formObject.setLocked("AltContactDetails_Frame1",true);
			LoadpicklistAltcontactDetails();
			
		} 

		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())){
			Loadpicklistfatca();
			formObject.setLocked("FATCA_Frame6", true);
		}

		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())){
			loadPicklist_KYC();
			formObject.setLocked("KYC_Frame7",true);
			
		}

		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())){
			loadPickListOECD();
			formObject.setLocked("OECD_Frame8",true);
			
		}

		else if ("IncomingDoc".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setLocked("IncomingDoc_Frame1", true);
		}

		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
		/*	formObject.setLocked("DecisionHistory_Frame1", true);
			formObject.setVisible("DecisionHistory_chqbook",false);*/
			//for decision fragment made changes 8th dec 2017
			PersonalLoanS.mLogger.info("***********Inside decision history of csm");
			fragment_ALign("DecisionHistory_Label10,cmplx_Decision_New_CIFNo#DecisionHistory_Label7,cmplx_Decision_AccountNo#DecisionHistory_Label6,cmplx_Decision_IBAN#DecisionHistory_Button2#Decision_Label1,cmplx_Decision_VERIFICATIONREQUIRED#\n#Decision_Label3,cmplx_Decision_Decision#DecisionHistory_Label1,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_DecisionReasonCode#Decision_Label4,cmplx_Decision_REMARKS#DecisionHistory_Label26,cmplx_Decision_AppID#\n#Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
			formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
			formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
			//for decision fragment made changes 8th dec 2017
			
		} 	
		

	}
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException {

		switch(pEvent.getType()) {

		case FRAME_EXPANDED:        	  
			new PersonalLoanSCommonCode().FrameExpandEvent(pEvent);          	
			break;

		case FRAGMENT_LOADED:
			fragment_loaded(pEvent);
			break;

		default: break;

		}

	}	


	public void initialize() {	//empty method
	}


	public void saveFormCompleted(FormEvent pEvent) throws ValidatorException {
	//empty method	
	}


	public void saveFormStarted(FormEvent pEvent) throws ValidatorException {
	//empty method	
	}


	public void submitFormCompleted(FormEvent pEvent) throws ValidatorException {
	//empty method	
	}


	public void submitFormStarted(FormEvent pEvent) throws ValidatorException {
	//empty method	
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
