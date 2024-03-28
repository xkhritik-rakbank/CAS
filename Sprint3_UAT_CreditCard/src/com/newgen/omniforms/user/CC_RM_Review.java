
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


public class CC_RM_Review extends CC_Common implements FormListener
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
		CreditCard.mLogger.info( "Inside formLoaded()" + pEvent.getSource().getName());
		
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
        FormReference formObject = FormContext.getCurrentInstance().getFormReference();
        try{

			new CC_CommonCode().setFormHeader(pEvent);
		}catch(Exception e)
		{
			CreditCard.mLogger.info( "Exception:"+e.getMessage());
		}
    }
	
	
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : For Fetching the Fragments/Set controls on fragment Load/Logic on  Mouseclick/valuechange            

	 ***********************************************************************************  */
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{
		
		//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		  
		  
				switch(pEvent.getType())
				{	

					case FRAME_EXPANDED:
					//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

					new CC_CommonCode().FrameExpandEvent(pEvent);						

					break;
					
					case FRAGMENT_LOADED:
						//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			        	  new CC_CommonCode().LockFragmentsOnLoad(pEvent);
						
						
						
					
					  break;
					  
					case MOUSE_CLICKED:
						//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
						new CC_CommonCode().mouse_clicked(pEvent);
						break;
					 case VALUE_CHANGED:
						//	CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
							new CC_CommonCode().value_changed(pEvent);
							break;
					default: break;
					
				}
	}


	public void continueExecution(String arg0, HashMap<String, String> arg1) {
		// TODO Auto-generated method stub
		
	}


	public void initialize() {
		// TODO Auto-generated method stub
		
	}


	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic After Workitem Save          

	 ***********************************************************************************  */
	public void saveFormCompleted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic Before Workitem Save          

	 ***********************************************************************************  */
	public void saveFormStarted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic After Workitem Done          

	 ***********************************************************************************  */
	public void submitFormCompleted(FormEvent arg0) throws ValidatorException {
		try{
			CreditCard.mLogger.info( "Inside PL PROCESS submitFormCompleted()" + arg0.getSource());


			//added by akshay on 9/12/17 for multile Refer

			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			List<String> objInput=new ArrayList<String> ();
			objInput.add("Text:"+formObject.getWFWorkitemName());
			String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
			
			
			if("RM_Review".equalsIgnoreCase(sActivityName))
				objInput.add("Text:RM_Review");
			else
				objInput.add("Text:Source");
			CreditCard.mLogger.info("objInput args are: "+objInput.get(0)+objInput.get(1));
			List<Object> objOutput=new ArrayList<Object>();

			objOutput.add("Text");
			objOutput= formObject.getDataFromStoredProcedure("ng_RLOS_MultipleRefer", objInput,objOutput);
		}
		catch(Exception e){
			CreditCard.mLogger.info("Exception occured in submitFormCompleted"+e.getStackTrace());
		}
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic Before Workitem Done          

	 ***********************************************************************************  */
	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		CreditCard.mLogger.info( "Inside PL PROCESS submitFormStarted()" + arg0.getSource());
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();	
		
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
		String decisionOnForm = formObject.getNGValue("cmplx_DEC_Decision");
		if(decisionOnForm!=null && !decisionOnForm.equalsIgnoreCase("") && decisionOnForm.equalsIgnoreCase("Submit") && formObject.getNGValue("InitiationType").equalsIgnoreCase("Telesales_Init")){
			formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision")+"-Information updated");
			//formObject.setNGValue("Next_Workstep", "");
			}
		if(decisionOnForm.equalsIgnoreCase("Reject"))
		{
			String query="UPDATE WFINSTRUMENTTABLE  SET ValidTill=DATEADD(Day,90, getdate()) WHERE ProcessInstanceID='"+formObject.getWFWorkitemName()+"' AND activity='Refer_Hold'";
			formObject.saveDataIntoDataSource(query);
			//CreditCard.mLogger.info( "Inside reject @sag" + query);
			//select wi from ng_reject_source where reject_Flag='R'";
		}
		
		/*if(formObject.getNGValue("Interest_Rate_App_dec").equalsIgnoreCase("Reject"))
		{
			formObject.setNGValue("Interest_Rate_App_dec", "");
		}
		if(formObject.getNGValue("IS_SALES_HEAD_DEC").equalsIgnoreCase("Reject"))
		{
			formObject.setNGValue("IS_SALES_HEAD_DEC", "");
		}
		
		formObject.setNGValue("Next_Workstep", "");*/
		saveIndecisionGrid();
		CustomSaveForm();
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

