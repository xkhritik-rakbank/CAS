/*------------------------------------------------------------------------------------------------------

                                                                NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                                         : Application -Projects
Project/Product                                                               : Rakbank  
Application                                                                   : RLOS
Module                                                                        : Personal Loan
File Name                                                                     : PL_RM_CSO_Review.java
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

import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;



public class PL_RM_CSO_Review extends PLCommon implements FormListener
{
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	Logger mLogger=PersonalLoanS.mLogger;
	public void formLoaded(FormEvent pEvent)
	{
		FormConfig objConfig = FormContext.getCurrentInstance().getFormConfig();
        objConfig.getM_objConfigMap().put("PartialSave", "true");
        makeSheetsInvisible("Tab1", "8,9,16");//Changes done by shweta for jira #1768 to hide CPV tab//Hide Dispatch
        FormReference formObject = FormContext.getCurrentInstance().getFormReference();
        formObject.setSheetVisible(tabName, 6, true);	// Show Services RequestTab
	}


	public void formPopulated(FormEvent pEvent) 
	{
		try{
			new PersonalLoanSCommonCode().setFormHeader(pEvent);

			//PCASI - 2694
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String sRMNameHeader = formObject.getNGValue("RM_Name");
			String cmplxRMName = formObject.getNGValue("cmplx_Customer_RM_TL_NAME");
			if("".equalsIgnoreCase(cmplxRMName) || cmplxRMName == null){
				formObject.setNGValue("cmplx_Customer_RM_TL_NAME", sRMNameHeader);
			}
			PersonalLoanS.mLogger.info("sRMNameHeader :: "+sRMNameHeader+" cmplxRMName :: "+cmplxRMName);

		}catch(Exception e)
		{
			PLCommon.printException(e);
			mLogger.info("PL_RM_CSO_Review"+ "Exception:"+e.getMessage());
		}
	}


	public void fragment_loaded(ComponentEvent pEvent){
		
		FormReference formObject=FormContext.getCurrentInstance().getFormReference();
		
		if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			//for decision fragment made changes 8th dec 2017
			PersonalLoanS.mLogger.info("***********Inside decision history");
			fragment_ALign("cmplx_Decision_waiveoffver#\n#Decision_Label1,cmplx_Decision_VERIFICATIONREQUIRED#Decision_Label3,cmplx_Decision_Decision#DecisionHistory_Label1,cmplx_Decision_ReferTo#DecisionHistory_Label11,DecisionHistory_DecisionReasonCode#\n#DecisionHistory_Label5,cmplx_Decision_desc#DecisionHistory_Label3,cmplx_Decision_strength#DecisionHistory_Label4,cmplx_Decision_weakness#Decision_Label4,cmplx_Decision_REMARKS#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
			formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
    		formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
    		
			PersonalLoanS.mLogger.info("***********Inside after fragment alignment decision history");
			
		/*	formObject.setVisible("DecisionHistory_Label8", false);
			formObject.setVisible("cmplx_Decision_ChequeBookNumber", false);
			formObject.setVisible("DecisionHistory_Label9", false);
			formObject.setVisible("cmplx_Decision_DebitcardNumber", false);
			formObject.setVisible("DecisionHistory_Label5", false);
			formObject.setVisible("cmplx_Decision_desc", false);
			formObject.setVisible("DecisionHistory_Label3", false);
			formObject.setVisible("cmplx_Decision_strength", false);
			formObject.setVisible("DecisionHistory_Label4", false);
			formObject.setVisible("cmplx_Decision_weakness", false);
			
			formObject.setVisible("DecisionHistory_chqbook", false);
			formObject.setVisible("DecisionHistory_Label6", false);
			formObject.setVisible("DecisionHistory_Label7", false);
			formObject.setVisible("DecisionHistory_Label11", true);
			formObject.setVisible("DecisionHistory_DecisionReasonCode", true);

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
			
			formObject.setTop("Decision_Label1", 10);
			formObject.setTop("cmplx_Decision_VERIFICATIONREQUIRED", 25);
			formObject.setTop("Decision_Label3", 10);
			formObject.setLeft("Decision_Label3", 350);
			formObject.setLeft("cmplx_Decision_Decision", 350);
			formObject.setTop("cmplx_Decision_Decision", 25);
			formObject.setTop("DecisionHistory_Label11", 10);
			formObject.setTop("DecisionHistory_DecisionReasonCode",25);
			formObject.setLeft("DecisionHistory_Label11", 672);
			formObject.setLeft("DecisionHistory_DecisionReasonCode", 672);
			formObject.setTop("Decision_Label4",10);
			formObject.setTop("cmplx_Decision_REMARKS", 25);
			formObject.setTop("Decision_ListView1", 100);
			formObject.setTop("DecisionHistory_save", 280);
			formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+formObject.getHeight("DecisionHistory_save")+20);
			formObject.setHeight("DecisionHistory",formObject.getHeight("DecisionHistory_Frame1")+30);
*/
			//for decision fragment made changes 8th dec 2017
		}
		else if("ReferHistory".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setLocked("ReferHistory_Frame1",true);
			formObject.setLocked("ReferHistory_save",false);
		}
		else if ("LoanDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("LoanDetails_Frame1",true);
			formObject.setLocked("LoanDetails_Frame3",true);
			formObject.setLocked("LoanDetails_Frame4",true);
			
			formObject.setLocked("LoanDeatils_calculateemi",false); //hritik
		}
		else{
			new PersonalLoanSCommonCode().LockFragmentsOnLoad(pEvent);
		}
	}
	
	
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{


	//	mLogger.info("Inside PL_Initiation eventDispatched()"+ "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

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
	//empty method

	}


	public void saveFormStarted(FormEvent arg0) throws ValidatorException {
	//empty method
	}


	public void submitFormCompleted(FormEvent pEvent) throws ValidatorException {
		//added by akshay on 9/12/17 for multiple refer
				FormReference formObject = FormContext.getCurrentInstance().getFormReference();
				PersonalLoanS.mLogger.info("Inside PL DSA_CSO_Review submitFormCompleted()" + pEvent.getSource()); 
				List<String> objInput=new ArrayList<String> ();
				objInput.add("Text:"+formObject.getWFWorkitemName());
				objInput.add("Text:Source");
				PersonalLoanS.mLogger.info("objInput args are: "+objInput.get(0)+objInput.get(1));
				List<Object> objOutput=new ArrayList<Object>();
				
				objOutput.add("Text");
			PersonalLoanS.mLogger.info("Before executing procedure ng_RLOS_CheckWriteOff");
			objOutput= formObject.getDataFromStoredProcedure("ng_RLOS_MultipleRefer", objInput,objOutput);
	}



	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
	
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try
		{
			
		formObject.setNGValue("decision", formObject.getNGValue("cmplx_Decision_Decision"));
		saveIndecisionGrid();
		formObject.setNGValue("IsSourceChild_Exists", "N");//FOR REFER CASE ROUTING
		CustomSaveForm();
		
		
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

