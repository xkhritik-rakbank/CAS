
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


public class CDOB_RM_Review extends CDOB_Common implements FormListener
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
	public void eventDispatched(ComponentEvent pEvent) throws ValidatorException
	{
		
		//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		  
		  
				switch(pEvent.getType())
				{	

					case FRAME_EXPANDED:
					//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

					new CDOB_CommonCode().FrameExpandEvent(pEvent);						

					break;
					
					case FRAGMENT_LOADED:
						//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			        	  new CDOB_CommonCode().LockFragmentsOnLoad(pEvent);
						
						
						
					
					  break;
					  
					case MOUSE_CLICKED:
						//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
						new CDOB_CommonCode().mouse_clicked(pEvent);
						break;
					 case VALUE_CHANGED:
						//	CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
							new CDOB_CommonCode().value_changed(pEvent);
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
			DigitalOnBoarding.mLogger.info( "Inside PL PROCESS submitFormCompleted()" + arg0.getSource());


			//added by akshay on 9/12/17 for multile Refer

			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			List<String> objInput=new ArrayList<String> ();
			objInput.add("Text:"+formObject.getWFWorkitemName());
			String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
			if("RM_Review".equalsIgnoreCase(sActivityName))
				objInput.add("Text:RM_Review");
			else
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
	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		DigitalOnBoarding.mLogger.info( "Inside PL PROCESS submitFormStarted()" + arg0.getSource());
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();	
		
		formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
		String decisionOnForm = formObject.getNGValue("cmplx_DEC_Decision");
		if(decisionOnForm!=null && !decisionOnForm.equalsIgnoreCase("") && decisionOnForm.equalsIgnoreCase("Submit") && formObject.getNGValue("InitiationType").equalsIgnoreCase("Telesales_Init")){
			formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision")+"-Information updated");
			//formObject.setNGValue("Next_Workstep", "");
			}
		if( "Customer Hold".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision"))){
			String Email_sub="REMINDER- case no "+formObject.getWFWorkitemName()+" pending for the action.";
			
			//CreditCard.mLogger.info("Email_sub: "+ Email_sub);
			String Email_message="<html><body>Dear Customer, Please update the required details of your request no" +formObject.getWFWorkitemName()+ "through RAKBANK portal to process your FALCON Credit Card"+ "Application and avoid cancellation.\n For details please call us on 04 2130000 or visit our branch. \n"
+"Assuring you of our best services at all times.\n"+
"Regards,\n"+
"RAKBANK\n"+
"* This is an automated email, please do not reply. </body></html>";
			DigitalOnBoarding.mLogger.info("Email_sub: "+ Email_message);
			String Query ="Select count(*)from NG_RLOS_EmailReminder1 where WI_Name ='"+formObject.getWFWorkitemName()+"'";
			int count=0;
			//	CreditCard.mLogger.info("query name :"+Query);
				List<List<String>> result=formObject.getDataFromDataSource(Query);
				if(!result.isEmpty()){
					String count_new =result.get(0).get(0);
					count=Integer.parseInt(count_new);
					}
					if(count==0)
					{
						 count=1;
					/*String reminderInsertQuery="insert into NG_RLOS_EmailReminder1(Email_Name,Email_Status,Email_To,Email_From,EmailSubject,EmailMessage,WI_Name,Workstep_Name,count1)"
						+" values('CustomerHold','P','','test5@rakbanktst.ae','"+Email_sub+"','"+Email_message+"','"+formObject.getWFWorkitemName()+"','"+formObject.getWFActivityName()+"',"+count+")";
					*/
					String reminderInsertQuery="insert into NG_RLOS_EmailReminder1(Email_Name,Email_Status,Email_To,Email_From,EmailSubject,EmailMessage,WI_Name,Workstep_Name,count1)"
						+" values('CustomerHold','P','','applicationstatus@rakbank.ae','"+Email_sub+"','"+Email_message+"','"+formObject.getWFWorkitemName()+"','"+formObject.getWFActivityName()+"',"+count+")";
						 DigitalOnBoarding.mLogger.info("Email_sub: "+ reminderInsertQuery);
						formObject.saveDataIntoDataSource(reminderInsertQuery);
				}
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

