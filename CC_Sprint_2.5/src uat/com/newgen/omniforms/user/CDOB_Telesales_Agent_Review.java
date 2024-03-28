
/*------------------------------------------------------------------------------------------------------

                     NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                             : Application -Projects

Project/Product                                                   : Rakbank  

Application                                                       : Credit Card

Module                                                            : Telesales Agent Review

File Name                                                         : CC_Telesales_Agent_Review

Author                                                            : Disha

Date (DD/MM/YYYY)                                                 : 

Description                                                       : 

-------------------------------------------------------------------------------------------------------

CHANGE HISTORY

-------------------------------------------------------------------------------------------------------

Problem No/CR No   Change Date   Changed By    Change Description

------------------------------------------------------------------------------------------------------*/

package com.newgen.omniforms.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;

import com.newgen.omniforms.listener.FormListener;


public class CDOB_Telesales_Agent_Review extends CDOB_Common implements FormListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	String queryData_load="";


	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : To Make Sheet Visible in DDVT Maker(8,9,11,12,13,15,16,17,18)              

	 ***********************************************************************************  */
	public void formLoaded(FormEvent pEvent)
	{
		FormConfig objConfig = FormContext.getCurrentInstance().getFormConfig();
        objConfig.getM_objConfigMap().put("PartialSave", "true");
		DigitalOnBoarding.mLogger.info( "Inside formLoaded()" + pEvent.getSource().getName());


		makeSheetsInvisible(tabName, "6,7,8,9,11,12,13,14,15,16,17");

	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : For setting the form header             

	 ***********************************************************************************  */
	public void formPopulated(FormEvent pEvent) 
	{
		try{

			new CDOB_CommonCode().setFormHeader(pEvent);
		}catch(Exception e)
		{
			DigitalOnBoarding.mLogger.info( "Exception:"+e.getMessage());
		}
	}


	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : For Fetching the Fragments/Set controls on fragment Load/Logic on  Mouseclick/valuechange            

	 ***********************************************************************************  */
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException {
		// TODO Auto-generated method stub


		//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		switch(pEvent.getType()) {

		case FRAME_EXPANDED:
			//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			new CDOB_CommonCode().FrameExpandEvent(pEvent);
			break;

		case FRAGMENT_LOADED:
			// CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			new CDOB_CommonCode().LockFragmentsOnLoad(pEvent);
			break;

		case MOUSE_CLICKED:
			//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			new CDOB_CommonCode().FrameExpandEvent(pEvent);
			break;

		case VALUE_CHANGED:
			//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			new CDOB_CommonCode().value_changed(pEvent);
			break;

		default: break;

		}

	}	


	public void initialize() {
		DigitalOnBoarding.mLogger.info( "Inside PL PROCESS initialize()" );


	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic After Workitem Save          

	 ***********************************************************************************  */
	public void saveFormCompleted(FormEvent pEvent) throws ValidatorException {
		DigitalOnBoarding.mLogger.info( "Inside PL PROCESS saveFormCompleted()" + pEvent.getSource());


	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic Before Workitem Save          

	 ***********************************************************************************  */
	public void saveFormStarted(FormEvent pEvent) throws ValidatorException {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		DigitalOnBoarding.mLogger.info( "Inside PL PROCESS saveFormStarted()" + pEvent.getSource());
		formObject.setNGValue("get_parent_data",queryData_load);
		DigitalOnBoarding.mLogger.info("Final val of queryData_load:"+ formObject.getNGValue("get_parent_data"));
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic After Workitem Done          

	 ***********************************************************************************  */
	public void submitFormCompleted(FormEvent pEvent) throws ValidatorException {
		try{
			DigitalOnBoarding.mLogger.info( "Inside PL PROCESS submitFormCompleted()" + pEvent.getSource());
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			List<String> objInput=new ArrayList<String> ();
			objInput.add("Text:"+formObject.getWFWorkitemName());
			objInput.add("Text:Source");
			DigitalOnBoarding.mLogger.info("objInput args are: "+objInput.get(0)+objInput.get(1));
			List<Object> objOutput=new ArrayList<Object>();

			objOutput.add("Text");
			objOutput= formObject.getDataFromStoredProcedure("ng_RLOS_MultipleRefer", objInput,objOutput);
		}
		catch(Exception e){
			DigitalOnBoarding.mLogger.info("Exception occured in submitFormCompleted"+e.getStackTrace());
		}
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic Before Workitem Done          

	 ***********************************************************************************  */
	public void submitFormStarted(FormEvent pEvent) throws ValidatorException {
		try{
			DigitalOnBoarding.mLogger.info( "Inside PL PROCESS submitFormStarted()" + pEvent.getSource());
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();	
			formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));

			String decisionOnForm = formObject.getNGValue("cmplx_DEC_Decision");
			if(decisionOnForm!=null && !decisionOnForm.equalsIgnoreCase("") && decisionOnForm.equalsIgnoreCase("Submit") && formObject.getNGValue("InitiationType").equalsIgnoreCase("Telesales_Init")){
				formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision")+"-Information updated");
				formObject.setNGValue("Next_Workstep", "");
			}

			if(formObject.getNGValue("Interest_Rate_App_dec").equalsIgnoreCase("Reject"))
			{
				formObject.setNGValue("Interest_Rate_App_dec", "");
			}
			if(formObject.getNGValue("IS_SALES_HEAD_DEC").equalsIgnoreCase("Reject"))
			{
				formObject.setNGValue("IS_SALES_HEAD_DEC", "");
			}

			formObject.setNGValue("Next_Workstep", "");
			saveIndecisionGrid();
			
			//formObject.setNGValue("cmplx_DEC_Remarks","");
		}
		catch(Exception e){
			DigitalOnBoarding.mLogger.info("Exception occured in submitFormStarted"+e.getStackTrace());
		}
	}


	public void continueExecution(String arg0, HashMap<String, String> arg1) {
		// TODO Auto-generated method stub
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();	
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));

	}


	public String decrypt(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}


	public String encrypt(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}


	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Set form controls on load       

	 ***********************************************************************************  */




}

