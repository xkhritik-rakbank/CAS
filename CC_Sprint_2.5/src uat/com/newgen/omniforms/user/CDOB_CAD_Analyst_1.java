


/*------------------------------------------------------------------------------------------------------

                     NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                             : Application -Projects

Project/Product                                                   : Rakbank  

Application                                                       : Credit Card

Module                                                            : Cad Analyst1

File Name                                                         : CC_CAD_Analyst1

Author                                                            : Disha 

Date (DD/MM/YYYY)                                                 : 

Description                                                       : 

-------------------------------------------------------------------------------------------------------

CHANGE HISTORY

-------------------------------------------------------------------------------------------------------

Problem No/CR No   Change Date   Changed By    Change Description

------------------------------------------------------------------------------------------------------*/




package com.newgen.omniforms.user;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;

import com.newgen.custom.Common_Utils;
import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;

public class CDOB_CAD_Analyst_1 extends CDOB_Common implements FormListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	boolean executeFlag = false;
	String queryData_load="";

	/*          Function Header:

	 **********************************************************************************
		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description    Card_Dispatch_Option                     : To Make Sheet Visible in DDVT Maker(8,9,11,12,13,15,16,17,18)              

	 ***********************************************************************************  */
	public void formLoaded(FormEvent pEvent)
	{
		FormConfig objConfig = FormContext.getCurrentInstance().getFormConfig();
		objConfig.getM_objConfigMap().put("PartialSave", "true");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		DigitalOnBoarding.mLogger.info( "Inside formLoaded()" + pEvent.getSource().getName());

		makeSheetsInvisible(tabName, "6,11,12,13,14,15,16,17");//pcasf-392

		formObject.setVisible("SmartCheck_Label2",true);
		formObject.setVisible("cmplx_SmartCheck_SmartCheckGrid_cpvremarks",true);



	}
	/*          Function Header: 57: /**q_Emailto 

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : For setting the form header        

	 ***********************************************************************************  */

	public void formPopulated(FormEvent pEvent) 
	{
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();

			//added by akshay for proc 12855
			//Deepak below commented to show CPV table in CA1 stage for all cases PCAS-2447 
			/*if("N".equalsIgnoreCase(formObject.getNGValue("CPV_REQUIRED"))){
				formObject.setSheetVisible(tabName,8,false); exception
			}*/
			
			formObject.setNGValue("Mandatory_Frames", NGFUserResourceMgr_DigitalOnBoarding.getGlobalVar("CSM_Frame_Name"));
			new CDOB_CommonCode().setFormHeader(pEvent);
			enable_cad1();
			enable_CPV();
			Common_Utils common=new Common_Utils(DigitalOnBoarding.mLogger);
			common.getAge(formObject.getNGValue("cmplx_Customer_DOb"),"cmplx_Customer_age");
			formObject.setLocked("Customer_CreateCIF", false);
			//Added by Sajan to Reset the Customer save attribute when WIs comes back to CA1
			formObject.setNGValue("is_Customer_Saved", "");
			
		}
		catch(Exception e)
		{
			DigitalOnBoarding.mLogger.info( "Exception:"+CDOB_Common.printException(e));
		}
		CheckforRejects("CAD_Analyst1");
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
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap

		DigitalOnBoarding.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		//DigitalOnBoarding.mLogger.info("This is event dispatched @sajan");
		switch(pEvent.getType())
		{	

		case FRAME_EXPANDED:
			//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			new CDOB_CommonCode().FrameExpandEvent(pEvent);						
			break;




		case FRAGMENT_LOADED:
			//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

			fragment_loaded(pEvent,hm,formObject);
			//loadPicklist1();
			break;


		case MOUSE_CLICKED:
			//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			new CDOB_CommonCode().mouse_clicked(pEvent);
			break;

			// added by abhishek for repeater end

			//ended by yash 
		case VALUE_CHANGED:
			//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			new CDOB_CommonCode().value_changed(pEvent);
			break;
		default: break;

		}
	}


	public void getDataFromALOC(FormReference formObject2, String corpName)
	{
		try{
			String query = "select INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,HIGH_DELINQUENCY_EMPLOYER,EMPLOYER_CATEGORY_PL_EXPAT from NG_RLOS_ALOC_OFFLINE_DATA with (nolock) where EMPR_NAME = '"+corpName+"'";
			List<List<String>> result = formObject2.getNGDataFromDataCache(query);
			if(result!=null && !result.isEmpty())  //if(result!=null && result.size()>0)
			{
				formObject2.setNGValue("indusSector", result.get(0).get(0));
				formObject2.setNGValue("indusMAcro", result.get(0).get(1));
				formObject2.setNGValue("indusMicro", result.get(0).get(2));
				formObject2.setNGValue("CompanyDetails_highdelin", result.get(0).get(3));
				formObject2.setNGValue("CompanyDetails_currempcateg", result.get(0).get(4));
			}
		}
		catch(Exception ex){
			DigitalOnBoarding.mLogger.info( printException(ex));
		}
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              :              
	Description                         : Product Default          

	 ***********************************************************************************  */
	public void continueExecution(String arg0, HashMap<String, String> arg1) {
		// TODO Auto-generated method stub

	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              :              
	Description                         : Product Default      

	 ***********************************************************************************  */
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
		//LoadReferGrid();
		CustomSaveForm();
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
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String prod_desc="";
		String product=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5);
		DigitalOnBoarding.mLogger.info("ssa"+product);
		String query="select description from ng_master_carddescription where code= '"+product+"'";
		DigitalOnBoarding.mLogger.info("ssa"+query);
		List<List<String>> query_card = formObject.getDataFromDataSource(query);
		if(!query_card.isEmpty()){
			prod_desc=query_card.get(0).get(0);
			formObject.setNGValue("Card_Desc",prod_desc);
			DigitalOnBoarding.mLogger.info("ss"+prod_desc);
		}
		
	}



	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic After Workitem Done          

	 ***********************************************************************************  */
	public void submitFormCompleted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		try{
			DigitalOnBoarding.mLogger.info("inside submitformcompleted: ");

			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String Decision= formObject.getNGValue("cmplx_DEC_Decision");
			DigitalOnBoarding.mLogger.info("inside submitformcompleted: Decision"+Decision);

			DigitalOnBoarding.mLogger.info("inside submitformcompleted:2 Decision"+Decision);
			List<String> objInput=new ArrayList<String>();
			//disha FSD cad delegation procedure changes
			objInput.add("Text:"+formObject.getWFWorkitemName());
			List<Object> objOutput=new ArrayList<Object>();

			objOutput.add("Text");
			//formObject.getDataFromStoredProcedure("DummyProc", objInput, objOutput);
			DigitalOnBoarding.mLogger.info("inside submitformcompleted: Decision"+formObject.getWFWorkitemName());
			objInput.add("Text:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1));
			DigitalOnBoarding.mLogger.info("inside submitformcompleted: Decision"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1));
			objInput.add("Text:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2));
			DigitalOnBoarding.mLogger.info("inside submitformcompleted: Decision"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2));
			objInput.add("Text:"+formObject.getNGValue("cmplx_DEC_HighDeligatinAuth"));
			DigitalOnBoarding.mLogger.info("inside submitformcompleted: Decision"+formObject.getNGValue("cmplx_DEC_HighDeligatinAuth"));

			//change by saurabh on 7th Dec
			objInput.add("Text:"+Decision);
			String prod_desc="";
			String product=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5);
			DigitalOnBoarding.mLogger.info("ssa"+product);
			String query="select description from ng_master_carddescription where code= '"+product+"'";
			DigitalOnBoarding.mLogger.info("ssa"+query);
			List<List<String>> query_card = formObject.getDataFromDataSource(query);
			if(!query_card.isEmpty()){
				prod_desc=query_card.get(0).get(0);
				formObject.setNGValue("Card_Desc",prod_desc);
				DigitalOnBoarding.mLogger.info("ss"+prod_desc);
			}
			DigitalOnBoarding.mLogger.info("objInput args are: "+objInput.get(0)+objInput.get(1)+objInput.get(2)+objInput.get(3)+objInput.get(4));
			if ((!"CA_Hold".equalsIgnoreCase(Decision))&&("".equalsIgnoreCase(formObject.getNGValue("CAD_dec_tray"))|| "Select".equalsIgnoreCase(formObject.getNGValue("CAD_dec_tray")))){
				objOutput = formObject.getDataFromStoredProcedure("ng_rlos_CADLevels", objInput,objOutput);
			}
			
			if("Approve".equalsIgnoreCase(Decision)){
				objInput.clear();
				objInput.add("Text:"+formObject.getWFWorkitemName());
				objInput.add("Text:"+"Waiting for Approval");
				objOutput.clear();
				objOutput.add("Text");
				objOutput= formObject.getDataFromStoredProcedure("ng_EFMS_InsertData", objInput,objOutput);
				DigitalOnBoarding.mLogger.info("After ng_EFMS_InsertData: objOutput is "+objOutput);
			}
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
	
	public void submitFormStarted(FormEvent arg0) throws ValidatorException 
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		// TODO Auto-generated method stub
		/*formObject.setNGValue("Mail_CC", formObject.getUserName());*/
		//formObject.setNGValue("VAR_STR6", formObject.getUserName());
		//change by nikhil for PCSP-472
		Check_EFC_Limit();
	String prod_desc="";
	String product=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5);
	DigitalOnBoarding.mLogger.info("ssa"+product);
	String query="select description from ng_master_carddescription where code= '"+product+"'";
	DigitalOnBoarding.mLogger.info("ssa"+query);
	List<List<String>> query_card = formObject.getDataFromDataSource(query);
	if(!query_card.isEmpty()){
		prod_desc=query_card.get(0).get(0);
		formObject.setNGValue("Card_Desc",prod_desc);
		DigitalOnBoarding.mLogger.info("ss"+prod_desc);
	}
	
		//added by nikhil for Employment Match check PCSP-459
		//formObject.setNGValue(Card_desc,formObject.getNGValueLVWAT(, ));
		if("Approve".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")))
		{
			//	DigitalOnBoarding.mLogger.info("@sag cad");
			Employment_Match_Check();
			//DigitalOnBoarding.mLogger.info("@sag cad next");
			//Capture_ATC_Fields();
			formObject.setNGValue("cadlist","");//Deepak changes to rerun delegation in case of send back
		}
		else if("Reject".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")))//To insert Rejected application entry.
		{
			RejectAppNotification();
		}
		
		if("false".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_ContactPointVeri"))){
			DigitalOnBoarding.mLogger.info("CC val change "+ "Inside Y of CPV required");
			formObject.setNGValue("CPV_REQUIRED","Y");
			formObject.setNGValue("is_cpv_done","N");

		}
		else{
			DigitalOnBoarding.mLogger.info("CC val change "+ "Inside N of CPV required");
			formObject.setNGValue("CPV_REQUIRED","N");
			formObject.setNGValue("is_cpv_done","Y");
			formObject.setNGValue("CPV_Decision","Approve");
		}
		try{
			//below code added by nikhil for PCSP-472
			CustomSaveForm();
			//Deepak Code PSCP-270 moved from CAD 1 to CAD 2
			formObject.setNGValue("CAD_Username", formObject.getUserName());

			DigitalOnBoarding.mLogger.info("inside try block of submitFormStarted at CAD 1");
			// below line commented because now we have one common decision fragment
			//formObject.setNGValue("CAD_ANALYST1_DECISION", formObject.getNGValue("cmplx_CADDecision_Decision"));
			formObject.setNGValue("CAD_ANALYST1_DECISION", formObject.getNGValue("cmplx_DEC_Decision"));
			formObject.setNGValue("Highest_Delegation_CAD", formObject.getNGValue("cmplx_DEC_HighDeligatinAuth")); 
			formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
			formObject.setNGValue("CAD_dec_tray", formObject.getNGValue("cmplx_DEC_ReferTo")); //Arun (07/10)
			DigitalOnBoarding.mLogger.info("inside try block of submitFormStarted CAD_ANALYST1_DECISION: " + formObject.getNGValue("CAD_ANALYST1_DECISION"));

			if(NGFUserResourceMgr_DigitalOnBoarding.getGlobalVar("CC_refertoSmartCPV").equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")))
			{
				saveSmartCheckGrid();
			}
			if(NGFUserResourceMgr_DigitalOnBoarding.getGlobalVar("CC_RefertoCredit").equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")))
			{
				//++ below code added 10-10-2017 - to save cad decision tray in external table
				DigitalOnBoarding.mLogger.info("inside submitFormStarted value of ReferTo : "+ formObject.getNGValue("cmplx_DEC_ReferTo")); //Arun (11/10)
				formObject.setNGValue("Cad_Deviation_Tray",formObject.getNGValue("cmplx_DEC_ReferTo")); //Arun (11/10)
				formObject.setNGValue("q_Emailto",formObject.getNGValue("cmplx_DEC_ReferTo")); //Arun (11/10)
			}



			//++ below code aded by disha on 27-3-2018 to set value of var_int3
			if("STP".equalsIgnoreCase(formObject.getNGValue("Highest_Delegation_CAD")))
			{
				DigitalOnBoarding.mLogger.info("Inside STP ");
				if("Approve".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")))
				{
					DigitalOnBoarding.mLogger.info("Inside STP Approve CAD1");
					formObject.setNGValue("q_hold1", 1);
					formObject.setNGValue("q_Hold2", 1);
					//formObject.setNGValue("VAR_INT3",1);
					DigitalOnBoarding.mLogger.info("Inside STP Approve " + formObject.getNGValue("q_hold1"));
				}
			}
			//++ above code aded by disha on 27-3-2018 to set value of var_int3
			if("Refer".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")) || "Reject".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")))
			{
				formObject.setNGValue("CAD_dec_tray", formObject.getNGValue("cmplx_DEC_ReferTo"));

			}//cmplx_DEC_ReferTo

			DigitalOnBoarding.mLogger.info("@sajan referto  "+formObject.getNGValue("DecisionHistory_ReferTo"));
			DigitalOnBoarding.mLogger.info("@sajan ref count "+(formObject.getNGValue("CUST_HOLD_REF_COUNT")));
			
			if("Refer".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")))
			{
				if( "Customer_Hold".equalsIgnoreCase(formObject.getNGValue("DecisionHistory_ReferTo"))){
					//added by saurabh1 for customer communication start
					String TypeofProduct=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0);
					String Email_sub="";
					String Email_message="";
					if("Conventional".equalsIgnoreCase(TypeofProduct)){
						Email_sub="Reminder to update your RAKBANK Credit Card Application";
						Email_message="<html><body><p style=\"font-size:10px; font-family:verdana; color:rgb(0,0,0);\">Dear Customer,<br><br> Please update the required details of your request no " +formObject.getNGValue("PORTAL_REF_NUMBER")+ " through RAKBANK portal to process your "+ prod_desc+ " application and avoid cancellation.\n<br><br>For any queries, please call us on 04 2130000.\n<br><br>"
		+"Assuring you of our best services at all times.\n<br><br>"+
		"Regards,\n<br>"+
		"RAKBANK\n<br><br>"+
		"* This is an automated email, please do not reply.</p> </body></html>";
					}
					//CreditCard.mLogger.info("Email_sub: "+ Email_sub);
	 				else if("Islamic".equalsIgnoreCase(TypeofProduct)){
						Email_sub="Reminder to update your RAKislamic Credit Card Application";
						Email_message="<html><body><p style=\"font-size:10px; font-family:verdana; color:rgb(0,0,0);\">Dear Customer,<br><br> Please update the required details of your request no " +formObject.getNGValue("PORTAL_REF_NUMBER")+ " through RAKBANK portal to process your "+prod_desc+ " application and avoid cancellation.\n<br><br>For any queries, please call us on 04 2130000. \n<br><br>"
		+"Assuring you of our best services at all times.\n<br><br>"+
		"Regards,\n<br>"+
		"RAKBANK\n<br><br>"+
		"* This is an automated email, please do not reply.</p> </body></html>";
					}//added by saurabh1 for customer communication end
					//	DigitalOnBoarding.mLogger.info("Email_sub: "+ Email_message);
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
						count=0;
						String reminderInsertQuery="insert into NG_RLOS_EmailReminder1(Email_Name,Email_Status,Email_To,Email_From,EmailSubject,EmailMessage,WI_Name,Workstep_Name,count1,setReminder)"
								+" values('CustomerHold','P','"+formObject.getNGValue("email_id")+"','applicationstatus@rakbank.ae','"+Email_sub+"','"+Email_message+"','"+formObject.getWFWorkitemName()+"','"+formObject.getWFActivityName()+"',"+count+",dateadd(Day,30,GETDATE()))";
						
						DigitalOnBoarding.mLogger.info("Email_sub: "+ reminderInsertQuery);
						formObject.saveDataIntoDataSource(reminderInsertQuery);
					}
					else{
						String reminderInsertQuery="update NG_RLOS_EmailReminder1 set Email_Status='P',count1=0 ,setReminder = dateadd(minute,10,GETDATE())  where WI_Name='"+formObject.getWFWorkitemName()+"'";
						DigitalOnBoarding.mLogger.info("Email_sub update: "+ reminderInsertQuery);
						formObject.saveDataIntoDataSource(reminderInsertQuery);
					}
					String Email_sub1="";
					String Email_message1="";
					String Pending_reasons="";
					if("Conventional".equalsIgnoreCase(TypeofProduct)){
						Email_sub1="RAKBANK Credit Card Application Pending";
						//	String query1="select DecReasonCode from ng_rlos_decisionHistory where WI_Name ='"+formObject.getWFWorkitemName()+"'";
						//	List<List<String>> result1=formObject.getDataFromDataSource(Query);
						//	if(!result.isEmpty()){ 
						int n=formObject.getLVWRowCount("DecisionHistory_Decision_ListView1");
						Pending_reasons =formObject.getNGValue("DecisionHistory_Decision_ListView1",n-1,8);//result.get(0).get(0);
						Pending_reasons=Pending_reasons.replace(';',',');
						//	}
						Email_message1="<html><body><p style=\"font-size:10px; font-family:verdana; color:rgb(0,0,0);\">Dear Customer,<br><br>  Your request no " +formObject.getNGValue("PORTAL_REF_NUMBER")+ " for a "+prod_desc+ " is pending for "+Pending_reasons+".<br><br>Kindly provide the required documentation/information through the portal by entering your mobile number. Also you can provide the requirements by clicking on the below link.<br><a href=\"https://rakbank.ae/wps/portal/retail-banking/cards/personal/emirates-skywards-world-elite-card/apply-now\">https://rakbank.ae/wps/portal/retail-banking/cards/personal/emirates-skywards-world-elite-card/apply-now</a><br><br>Please ensure that you provide the documentation/information within 7 working days to take forward your request and avoid cancellation.<br><br>For any queries, please call us on 04 2130000.\n<br><br>"
											+"Assuring you of our best services at all times.\n<br><br>"+
											"Regards,\n<br>"+
											"RAKBANK\n<br><br>"+
											"* This is an automated email, please do not reply. </p></body></html>";
					}
					else if("Islamic".equalsIgnoreCase(TypeofProduct)){
						Email_sub1="RAKislamic Credit Card Application Pending";
						//String query1="select DecReasonCode from ng_rlos_decisionHistory where WI_Name ='"+formObject.getWFWorkitemName()+"'";
						//List<List<String>> result1=formObject.getDataFromDataSource(Query);
						//if(!result.isEmpty()){
						int n=formObject.getLVWRowCount("DecisionHistory_Decision_ListView1");
						Pending_reasons =	formObject.getNGValue("DecisionHistory_Decision_ListView1",n-1,8);//result.get(0).get(0);
						Pending_reasons=Pending_reasons.replace(';',',');
						//}
						Email_message1="<html><body><p style=\"font-size:10px; font-family:verdana; color:rgb(0,0,0);\">Dear Customer,<br><br>  Your request no " +formObject.getNGValue("PORTAL_REF_NUMBER")+ " for a "+prod_desc+ " is pending for "+Pending_reasons+".<br><br>Kindly provide the required documentation/information through the portal by entering your mobile number. Also you can provide the requirements by clicking on the below link <br><a href=\"https://rakbank.ae/wps/portal/retail-banking/cards/personal/emirates-skywards-world-elite-card/apply-now\">https://rakbank.ae/wps/portal/retail-banking/cards/personal/emirates-skywards-world-elite-card/apply-now</a><br><br>Please ensure that you provide the documentation/information within 7 working days to take forward your request and avoid cancellation.<br><br>For any queries, please call us on 04 2130000.\n<br><br>"
											+"Assuring you of our best services at all times.\n<br><br>"+
											"Regards,\n<br>"+
											"RAKBANK\n<br><br>"+
											"* This is an automated email, please do not reply. </p></body></html>";
					}
					//SELECT @mobno=isnull(MobileNo_pri,'') FROM ng_RLOS_AltContactDetails with (nolock)  where wi_name=@sProcessinstanceid
					 
					String mailInsertQuery="insert into WFMAILQUEUETABLE(MAILFROM,MAILTO,MAILSUBJECT,MAILMESSAGE,MAILCONTENTTYPE,ATTACHMENTISINDEX,ATTACHMENTNAMES,ATTACHMENTEXTS,MAILPRIORITY,MAILSTATUS,STATUSCOMMENTS,LOCKEDBY,SUCCESSTIME,LASTLOCKTIME,INSERTEDBY,MAILACTIONTYPE,INSERTEDTIME,PROCESSDEFID,PROCESSINSTANCEID,WORKITEMID,ACTIVITYID,NOOFTRIALS) values('applicationstatus@rakbank.ae','"+formObject.getNGValue("email_id")+"','"+Email_sub1+"','"+Email_message1+"','text/html;charset=UTF-8',NULL,NULL,NULL,1,'N',NULL,NULL,NULL,NULL,'CUSTOM','TRIGGER',getdate(),1,'"+formObject.getWFWorkitemName()+"',1,1,0);";
                

					DigitalOnBoarding.mLogger.info("Email_sub: "+ mailInsertQuery);
					formObject.saveDataIntoDataSource(mailInsertQuery);
					//added by saurabh1 for customer communication end
				}
			}
			if("Refer".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")) && "Customer_Hold".equalsIgnoreCase(formObject.getNGValue("DecisionHistory_ReferTo"))){
				if(formObject.getNGValue("CUST_HOLD_REF_COUNT")==null || "".equals(formObject.getNGValue("CUST_HOLD_REF_COUNT"))){
					formObject.setNGValue("CUST_HOLD_REF_COUNT","1");
					String currDate=new SimpleDateFormat("yyyy-MM-dd").format(java.util.Calendar.getInstance().getTime());
					DigitalOnBoarding.mLogger.info("sajan test 111 "+currDate);
					formObject.setNGValue("CUST_HOLD_REF_DATE",currDate);

				}
				else if(Integer.parseInt(formObject.getNGValue("CUST_HOLD_REF_COUNT"))<3){
					formObject.setNGValue("CUST_HOLD_REF_COUNT", Integer.parseInt(formObject.getNGValue("CUST_HOLD_REF_COUNT"))+1);
					DigitalOnBoarding.mLogger.info("sajan test 222 "+formObject.getNGValue("CUST_HOLD_REF_COUNT"));
				}
				else if(Integer.parseInt(formObject.getNGValue("CUST_HOLD_REF_COUNT"))==3){
					SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
					String strDate=formObject.getNGValue("CUST_HOLD_REF_DATE");
					DigitalOnBoarding.mLogger.info("date in DB is "+strDate);

					String strCurrDate=formatter.format(java.util.Calendar.getInstance().getTime());
					DigitalOnBoarding.mLogger.info("Current date is "+strCurrDate);

					java.util.Date date1=formatter.parse(strDate);
					java.util.Date date2=formatter.parse(strCurrDate);
					long diff = date2.getTime() - date1.getTime();
					DigitalOnBoarding.mLogger.info("Difference n days is "+diff);
					int days=(int)(diff / (1000*60*60*24));
					String AlertMsg="Refer to Customer hold can only be done thrice a month";
					if(days<30){
						throw new ValidatorException(new FacesMessage(AlertMsg));
					}
					else{
						formObject.setNGValue("CUST_HOLD_REF_COUNT","1");
						String currDate=new SimpleDateFormat("yyyy-MM-dd").format(java.util.Calendar.getInstance().getTime());
						DigitalOnBoarding.mLogger.info("sajan test 444 "+currDate);
						formObject.setNGValue("CUST_HOLD_REF_DATE",currDate);
					}
				}
			}
			//changed by nikhil 27/11 to set header
			formObject.setNGValue("Final_Limit", formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
			//formObject.setNGValue("LoanLabel",formObject.getNGValue("Final_Limit"));
			//added by nikhil for CPV changes
			if("Approve Sub to CIF".equalsIgnoreCase(formObject.getNGValue("cmplx_CustDetailVerification_Decision")) && !"C".equalsIgnoreCase("IS_Approve_Cif") && "Approve".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision"))){
				formObject.setNGValue("IS_Approve_Cif", "Y");
			}
			else{
				formObject.setNGValue("IS_Approve_Cif", "N");
			}
		}catch(Exception ex){
			DigitalOnBoarding.mLogger.info("Exception in submitformstarted : "+printException(ex)); 
			DigitalOnBoarding.mLogger.info("Exception in submitformstarted : "+ex.getMessage());
			throw new ValidatorException(new FacesMessage(ex.getMessage()));
		}
		finally{
			try{
				DigitalOnBoarding.mLogger.info("Before setting CAD1 user");
				formObject.setNGValue("CAD1_User", formObject.getUserName());
				saveIndecisionGrid();
			}
			catch(Exception e){
				DigitalOnBoarding.mLogger.info("Exception occured in submitFormCompleted"+e.getStackTrace());
			}
		}
		////formObject.setNGValue("cmplx_DEC_Remarks","");
	}




	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Set form controls on load       

	 ***********************************************************************************  */
	private void fragment_loaded(FormEvent pEvent,HashMap<String,String> hm,FormReference formObject)
	{
		/*if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {

		}*/
		DigitalOnBoarding.mLogger.info( "EventName: before CPV tab validations");
		if ("Notepad_Details".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Notepad_Details","Clicked");
			notepad_load();
			formObject.setVisible("NotepadDetails_Frame3",true);
			formObject.setLocked("NotepadDetails_Frame3",false);
		}
		else if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadView(pEvent.getSource().getName());
			formObject.setLocked("cmplx_Customer_DOb",true);
			formObject.setLocked("cmplx_Customer_IdIssueDate",true);
			formObject.setLocked("cmplx_Customer_EmirateIDExpiry",true);
			formObject.setLocked("cmplx_Customer_VisaExpiry",false);
			formObject.setLocked("cmplx_Customer_PassportIssueDate",false);
			formObject.setLocked("cmplx_Customer_PassPortExpiry",false);
			
			//Making Visa details locked for GCC nationals
			if("AE".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_Nationality")) || 
					"BH".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_Nationality")) || 
					"IQ".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_Nationality")) || 
					"KW".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_Nationality")) || 
					"OM".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_Nationality")) || 
					"QA".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_Nationality")) || 
					"SA".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_Nationality"))){
				formObject.setLocked("cmplx_Customer_VisaIssueDate",true);
				formObject.setLocked("cmplx_Customer_VisaNo",true);
				formObject.setLocked("cmplx_Customer_VisaExpiry",true);
			}
			else{
				
				formObject.setLocked("cmplx_Customer_VisaIssueDate",false);
				formObject.setLocked("cmplx_Customer_VisaNo",false);
				formObject.setLocked("cmplx_Customer_VisaExpiry",false);
				formObject.setLocked("cmplx_Customer_EMirateOfVisa",false);
			}
			formObject.setLocked("Designation_button8_View",false);
			formObject.setEnabled("Designation_button8_View",true);
			loadPicklistCustomer();
			formObject.setLocked("Customer_save",false);
			formObject.setEnabled("Customer_save",true);
		}	

		//added by yash fro CC FSD
		else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Product_Frame1",true);
			int prd_count=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			if(prd_count>0){
				String ReqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
				loadPicklistProduct(ReqProd);
			}
			//Code commented by deepak as we are loading desc so no need to load the picklist(grid is already having desc) - 28Sept2017

			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct with (nolock)");
			LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_ApplicationType with (nolock) order by code");
			LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");

		}



		//added by yash for CC FSD
		else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			
			//formObject.setLocked("IncomeDetails_Frame1",true);
			LoadPickList("cmplx_IncomeDetails_AvgBalFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_CreditTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_AvgCredTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_AnnualRentFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			formObject.setLocked("cmplx_IncomeDetails_Overtime_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_grossSal",true);
			formObject.setLocked("cmplx_IncomeDetails_totSal",true);
			//	formObject.setLocked("cmplx_IncomeDetails_Commission_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_FoodAllow_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_PhoneAllow_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_serviceAllow_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_Bonus_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_Other_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_Flying_Avg",true);
			formObject.setNGValue("cmplx_IncomeDetails_Is_Tenancy_contract", "N");//Added by Sajan to set tnancy contact N
			formObject.setLocked("cmplx_IncomeDetails_AvgNetSal",true);
			formObject.setLocked("cmplx_IncomeDetails_DurationOfBanking",true);//by sagarika to make it non-editable
			
			//commented below code for JIRA-9853
			/*//COde added by aman To lock salary transfer to bank 15/12 
			formObject.setLocked("cmplx_IncomeDetails_SalaryXferToBank",true);
			//COde added by aman To lock salary transfer to bank 15/12*/
			//++below code added by abhishek for CAD point 1 on 7/11/2017
			formObject.setLocked("cmplx_IncomeDetails_Commission_Avg",true);
			//++above code added by abhishek for CAD point 1 on 7/11/2017
			loadpicklist_Income();
			formObject.setVisible("IncomeDetails_ListView1",true);
			formObject.setVisible("IncomeDetails_Button1",true);
			formObject.setVisible("IncomeDetails_Button2",true);
			formObject.setVisible("IncomeDetails_Button3",true);
			String EmpType = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6);
			if("Self Employed".equalsIgnoreCase(EmpType) )
			{
				formObject.setVisible("cmplx_IncomeDetails_TotalAvgOther",false);
				formObject.setVisible("IncomeDetails_Label15",false);
				fetch_Company_frag(formObject);//added by akshay on 29/12/17
			}
			if("Salaried".contains(EmpType))//sagarika change on 07/08/19
			{
				if("PA".equalsIgnoreCase(formObject.getNGValue("Sub_Product"))){
					DigitalOnBoarding.mLogger.info("@sagarika salaried");
					formObject.setVisible("IncomeDetails_Frame3",true);
					formObject.setVisible("IncomeDetails_Frame2",true);
				}
				else{
					formObject.setVisible("IncomeDetails_Frame3",false);
				}
			}

			//added by akshay on 29/12/17
			int framestate2=formObject.getNGFrameState("EligibilityAndProductInformation");
			if(framestate2 != 0){
				formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligProd");
				expandEligibility();
			}
			/*if(!NGFUserResourceMgr_CreditCard.getGlobalVar("CC_SelfEmployed").equals(formObject.getNGValue("EmploymentType"))){
					formObject.setNGValue("cmplx_IncomeDetails_totSal", formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalTai"));
			}----commented by akshay*/
			//2951
			formObject.setLocked("cmplx_IncomeDetails_CompanyAcc",true);
			formObject.setLocked("cmplx_IncomeDetails_TotalAvgOther",true);

		}
		// ended by yash for CC FSD
		else if ("CompanyDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			loadPicklist_CompanyDet();
			//loadPicklist_Address();
			//formObject.setLocked("CompanyDetails_Frame1",true);
			formObject.setVisible("CompanyDetails_Compwithoneman",false);//Arun 20/12/17
			formObject.setVisible("CompanyDetails_CheckBox1",true);
			formObject.setVisible("CompanyDetails_Label17",true);
			formObject.setVisible("CompanyDetails_highdelin",true);
			formObject.setVisible("CompanyDetails_Text1",true);
			formObject.setVisible("CompanyDetails_Label14",true);
			formObject.setVisible("CompanyDetails_currempcateg",true);
			formObject.setVisible("CompanyDetails_Text2",true);							
			formObject.setVisible("CompanyDetails_Label16",true);
			formObject.setVisible("CompanyDetails_categcards",true);
			formObject.setVisible("CompanyDetails_Text3",true);
			formObject.setVisible("CompanyDetails_Label12",true);
			formObject.setVisible("CompanyDetails_categexpat",true);
			formObject.setVisible("CompanyDetails_Text4",true);
			formObject.setVisible("CompanyDetails_Label15",true);
			formObject.setVisible("CompanyDetails_categnational",true);
			formObject.setVisible("CompanyDetails_Text5",true);							
			formObject.setLocked("CompanyDetails_Text1",false);
			formObject.setLocked("CompanyDetails_Text2",true);
			formObject.setLocked("CompanyDetails_Text3",true);
			formObject.setLocked("CompanyDetails_Text4",true);
			formObject.setLocked("CompanyDetails_Text5",true);
			formObject.setLocked("estbDate",false);
			formObject.setLocked("compName",true);
			formObject.setEnabled("CompanyDetails_DatePicker1",true);
		//	formObject.setLocked("lob", false);
			formObject.setEnabled("lob", true);
			//changed by sagarika to resolve proc 10862
			//change by saurabh on 6th Dec
			/*DigitalOnBoarding.mLogger.info("Before NEP condition"+formObject.getNGValue("cmplx_Customer_NEP"));
			if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP"))){
				DigitalOnBoarding.mLogger.info("inside NEP if condition");
				formObject.setLocked("CompanyDetails_Label15", false);
				formObject.setLocked("NepType", false);
			}
			else{
				DigitalOnBoarding.mLogger.info("inside NEP else condition");
				formObject.setLocked("CompanyDetails_Label15", true);
				formObject.setLocked("NepType", true);
			}*/
			//change by saurabh on 6th Dec
			formObject.setLocked("AuthorisedSignDetails_CIFNo", true);
			formObject.setLocked("AuthorisedSignDetails_FetchDetails", true);

			formObject.setEnabled("CompanyDetails_EffectiveLOB",true);
			formObject.setLocked("CompanyDetails_EffectiveLOB",false);

			formObject.setLocked("EmployerCategoryPL",true);
			formObject.setLocked("EmployerStatusCC",true);
			formObject.setLocked("EmployerStatusPLExpact",true);
			formObject.setLocked("EmployerStatusPLNational",true);
			/*LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--' as Description, '' as code union select convert(varchar, description),code from NG_MASTER_Country with (nolock) order by code");
			LoadPickList("AuthorisedSignDetails_Status", "select '--Select--' as Description,'' as code union select convert(varchar, description),code from NG_MASTER_SignatoryStatus with (nolock) order by code");
			LoadPickList("Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
			LoadPickList("DesignationAsPerVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
			LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			 */
		}
		
		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {

			formObject.setEnabled("ExtLiability_QCAmt",true );
			formObject.setLocked("ExtLiability_QCAmt",false );//by sagarika on 25-10-2018 for proc10872
			formObject.setLocked("Liability_New_MOB",true);
			//formObject.setVisible("Liability_New_Label2",true);
			formObject.setLocked("Liability_New_Utilization",true);
			//formObject.setVisible("Liability_New_Label3",false);
			formObject.setVisible("Liability_New_Outstanding",false);
			formObject.setLocked("Liability_New_Delinkinlast3months",true);
			formObject.setLocked("Liability_New_DPDinlast18months",true);
			formObject.setLocked("Liability_New_DPDinlast6",true);
			formObject.setLocked("Liability_New_writeOfAmount",true);
			formObject.setLocked("Liability_New_worstStatusInLast24",true);
			formObject.setLocked("Liability_New_rejApplinlast3months",true);
			formObject.setLocked("cmplx_Liability_New_ReferenceNo",true);
			formObject.setLocked("cmplx_Liability_New_ConsentDate",true);
			formObject.setLocked("cmplx_Liability_New_FTSFromDate",true);
			formObject.setLocked("cmplx_Liability_New_FTSToDate",true);
			formObject.setLocked("cmplx_Liability_New_AECBScore",true);
			formObject.setLocked("cmplx_Liability_New_Range",true);
		/*	formObject.setEnabled("cmplx_Liability_New_ReferenceNo",true);
			formObject.setEnabled("cmplx_Liability_New_AECBScore",true);
			formObject.setEnabled("cmplx_Liability_New_Range",true);*/
			LoadPickList("ExtLiability_contractType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_contract_type with (nolock) order by code");
			formObject.setLocked("cmplx_Liability_New_IbanNumber",true);			
			formObject.setNGValue("cmplx_Liability_New_overrideIntLiab", true);
			//formObject.setVisible("Liability_New_Overwrite", true);
		}
		//added by yash for CC FSD
		else if ("Compliance".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Compliance_Frame1",true);

		}
		//ended by yash for CC FSD


		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			DigitalOnBoarding.mLogger.info("This is Employment Details Frangment expand");
			loadPicklistEmployment();
			Fields_ApplicationType_Employment();
			formObject.setLocked("cmplx_EmploymentDetails_CurrEmployer",true);
			formObject.setNGValue("cmplx_EmploymentDetails_ClassificationCode", "AECB4");
			
			 formObject.setLocked("cmplx_EmploymentDetails_Indus_Macro",true);
             formObject.setLocked("cmplx_EmploymentDetails_Indus_Micro",true);
             formObject.setLocked("cmplx_EmploymentDetails_DOJ",false);
             formObject.setLocked("cmplx_EmploymentDetails_Emp_Type",true);
             
		}
		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			if(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("false"))
			{
				formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalLimit", true);//total final limit
			}
			formObject.setLocked("cmplx_EligibilityAndProductInfo_PortalApprovedLimit", true);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_MaturityDate", true);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_NetRate", true);
			LoadPickList("cmplx_EligibilityAndProductInfo_RepayFreq", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_frequency with (nolock) order by code");
			LoadPickList("cmplx_EligibilityAndProductInfo_InterestType", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_InterestType with (nolock) order by code"); //Arun (17/10)

			if(!formObject.isVisible("CardDetails_Frame1")){
				fetch_CardDetails_frag(formObject);
				adjustFrameTops("Card_Details,FATCA,KYC,OECD,Reference_Details");
			}
			formObject.setLocked("cmplx_EligibilityAndProductInfo_InterestType", true);
			formObject.setNGValue("cmplx_EligibilityAndProductInfo_InterestType", "Equated");
		}
		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist_Address();
		//	formObject.setLocked("AddressDetails_Frame1",true);
			LoadView(pEvent.getSource().getName());
			formObject.setEnabled("AddressDetails_Save",false);
			formObject.setEnabled("AddressDetails_addr_Add",false);
			formObject.setEnabled("AddressDetails_addr_Modify",false);
			formObject.setEnabled("AddressDetails_addr_Delete",false);
			formObject.setLocked("AddressDetails_Save",true);
			formObject.setLocked("AddressDetails_addr_Add",true);
			formObject.setLocked("AddressDetails_addr_Modify",true);
			formObject.setLocked("AddressDetails_addr_Delete",true);
			
			
			
			formObject.setLocked("AddressDetails_PreferredAddress", true);
			formObject.setEnabled("AddressDetails_PreferredAddress", false);
			formObject.setLocked("AddressDetails_ResidenceAddrType", true);
			formObject.setEnabled("AddressDetails_ResidenceAddrType", true);
			
			formObject.setLocked("AddressDetails_years", true);
			formObject.setEnabled("AddressDetails_years", true);
			
			
			formObject.setLocked("AddressDetails_pobox", true);
			formObject.setEnabled("AddressDetails_pobox", true);
			
			
			formObject.setLocked("AddressDetails_state", true);
			formObject.setEnabled("AddressDetails_state", false);
			formObject.setLocked("Button_State", true);
			formObject.setEnabled("Button_State", false);
			formObject.setLocked("Button_State_View", false);
			formObject.setEnabled("Button_State_View", true);
			
			formObject.setLocked("AddressDetails_country", true);
			formObject.setEnabled("AddressDetails_country", false);
			formObject.setLocked("AddressDetails_Button1", true);
			formObject.setEnabled("AddressDetails_Button1", false);
			formObject.setLocked("AddressDetails_Button1_View", false);
			formObject.setEnabled("AddressDetails_Button1_View", true);
			
			
			formObject.setLocked("AddressDetails_city", true);
			formObject.setEnabled("AddressDetails_city", false);
			formObject.setLocked("Button_City", true);
			formObject.setEnabled("Button_City", false);
			formObject.setLocked("Button_City_View", false);
			formObject.setEnabled("Button_City_View", true);
			
			formObject.setLocked("AddressDetails_landmark", true);
			formObject.setEnabled("AddressDetails_landmark", true);
			
			formObject.setLocked("AddressDetails_street", true);
			formObject.setEnabled("AddressDetails_street", true);
			
			formObject.setLocked("AddressDetails_buildname", true);
			formObject.setEnabled("AddressDetails_buildname", true);
			
			formObject.setLocked("AddressDetails_CustomerType", true);
			formObject.setEnabled("AddressDetails_CustomerType", true);
			
			formObject.setLocked("AddressDetails_addtype", true);
			formObject.setEnabled("AddressDetails_addtype", true);
			
			formObject.setLocked("AddressDetails_house",true);
			formObject.setEnabled("AddressDetails_house",true);
			formObject.setEnabled("Button_City_View",true);
			formObject.setLocked("Button_City_View",false);
		}
		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			LoadPickList("AlternateContactDetails_CustdomBranch", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock) order by code");
			//change by saurabh on 7th Dec
			//	LoadPickList("AlternateContactDetails_CardDisp", "select '--Select--' as description,'' as code union all select convert(varchar,description),code from NG_MASTER_Dispatch with (nolock) order by code ");// Load picklist added by aman to load the picklist in card dispatch to
			//change by saurabh for air arabia functionality.
			AirArabiaValid();
			//formObject.setLocked("AltContactDetails_Frame1",true);
			LoadView(pEvent.getSource().getName());

		}
		/*	else if ("ReferenceDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("ReferenceDetailVerification_Frame1",true);

		}*/
		else if ("CardCollection".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("CardDetails_Frame1",true);
		}
		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("FATCA_Frame6",true);
			formObject.setLocked("FATCA_SignedDate",true);
			formObject.setLocked("FATCA_ExpiryDate",true);
			loadPicklist_Fatca();

		}
		else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("CardDetails_Frame1",true);
			//++Below code added by nikhil 6/11/17
			//LoadPickList("cmplx_CardDetails_Statement_cycle","select '--Select--' as Description,'' as code union select convert(varchar, Description),code from NG_MASTER_StatementCycle with (nolock) where isActive='Y' order by code");
			//LoadPickList("CardDetails_BankName", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_BankName with (nolock) where IsActive = 'Y' order by code");
			formObject.setVisible("CardDetails_Label13", false);
			formObject.setVisible("cmplx_CardDetails_CustClassification", false);
			//--Above Code added by nikhil 6/11/17
			//added by nikhil for disbursal showstopper
			Loadpicklist_CardDetails_Frag();
		}

		else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address(); 
			formObject.setLocked("SupplementCardDetails_Frame1",true);
			loadPicklist_suppCard();
		}

		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("KYC_Frame1",true);

		}
		//added by yash for CC FSD
		else if ("Reference_Details".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Reference_Details_Frame1",true);
			formObject.setLocked("Reference_Details_ReferenceRelationship",true);
		}
		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			//formObject.setLocked("OECD_Frame8",true);
			LoadView(pEvent.getSource().getName());

		}

		else if ("Details".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("CC_Loan_Frame1",true);
			formObject.setEnabled("CC_Loan_Frame1", false);

		}
		else if ("PartMatch".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("PartMatch_Frame1",true);
			LoadView(pEvent.getSource().getName());
			//change by saurabh for JIRA - 2592
			//SLoadPickList("PartMatch_nationality","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_MASTER_Country with (nolock) order by code"); //Arun (10/10)

			formObject.setLocked("PartMatch_Dob", true);
			//change by saurabh for JIRA - 2592 end.
		}
		else if ("FinacleCRMIncident".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("FinacleCRMIncident_Frame1",true);

		}
		else if ("FinacleCRMCustInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadinFinacleCRNGrid(formObject);
			//change by saurabh for pcsp - 276 on 22/12
			formObject.setLocked("FinacleCRMCustInfo_Frame1", true);
			/*if("Salaried".equalsIgnoreCase(formObject.getNGValue("EmploymentType")))
			{
				formObject.addComboItem("FinacleCRMCustInfo_ApplicantType","--Select--","");
				formObject.addComboItem("FinacleCRMCustInfo_ApplicantType", "Individual", "Individual");
			}
			else
			{
				formObject.addComboItem("FinacleCRMCustInfo_ApplicantType","--Select--","");
				formObject.addComboItem("FinacleCRMCustInfo_ApplicantType", "Individual", "Individual");
				formObject.addComboItem("FinacleCRMCustInfo_ApplicantType", "Corporate", "Corporate");
			}*/
			

		}

		else if ("ExternalBlackList".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("ExternalBlackList_Frame1",true);
		}
		else if ("FinacleCore".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadPickList("FinacleCore_ChequeType", "select '--Select--' as description,'' as code union select convert(varchar, description),Code from ng_MASTER_Cheque_Type with (nolock) order by code");
			LoadPickList("FinacleCore_TypeOfRetutn", "select '--Select--' union all select convert(varchar, description) from ng_MASTER_TypeOfReturn with (nolock)");
			formObject.setLocked("FinacleCore_DDSClearing",false);
			formObject.setEnabled("FinacleCore_DDSClearing",true);//sagrika for PCAS-2953
			formObject.setLocked("FinacleCore_ReturnDate",false);
			formObject.setEnabled("FinacleCore_ReturnDate",true);
			formObject.setLocked("InwardTT_date",false);
			int framestate4=formObject.getNGFrameState("Reject_Enquiry");//758
			if(framestate4 !=0){
				formObject.fetchFragment("Reject_Enquiry", "RejectEnq", "q_RejectEnq");
			    formObject.setNGFrameState("Reject_Enquiry", 1);

			}
			formObject.setTop("Reject_Enquiry",2000);
			//InwardTT_date
			//loadPicklist_Address();
			//formObject.setLocked("FinacleCore_Frame1",true);
		}
		else if ("MOL1".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			loadPicklist_Mol();
			//Code changed by Sajan to enable MOL fields at CA 1
			//formObject.setLocked("MOL1_Frame1",true);
			//below code added by nikhil 10/1/17
			DigitalOnBoarding.mLogger.info("this is enabling fields test 222");
			formObject.setLocked("cmplx_MOL_molexp",false);
			formObject.setEnabled("cmplx_MOL_molexp",true);
			formObject.setEnabled("cmplx_MOL_molissue",true);
			formObject.setLocked("cmplx_MOL_molissue",false);
			formObject.setLocked("cmplx_MOL_ctrctstart",false);
			formObject.setEnabled("cmplx_MOL_ctrctstart",true);
			formObject.setLocked("cmplx_MOL_ctrctend",false);
			formObject.setEnabled("cmplx_MOL_ctrctend",true);
			formObject.setLocked("cmplx_MOL_nationality",false);//added by saurabh1 for pcasf-459
			formObject.setEnabled("cmplx_MOL_nationality",true);//added by saurabh1 for pcasf-459

		}
		else if ("WorldCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("WorldCheck1_Dob",false);
			formObject.setEnabled("WorldCheck1_Dob",true);
			formObject.setLocked("WorldCheck1_entdate",false);
			formObject.setEnabled("WorldCheck1_entdate",true);
			formObject.setLocked("WorldCheck1_upddate",false);
			formObject.setEnabled("WorldCheck1_upddate",true);
			
			//Commented by Sajan to enable World check at CA 1
			//loadPicklist_Address();
			/*formObject.setLocked("WorldCheck1_Frame1",true);
			formObject.setLocked("WorldCheck1_Dob",true);
			formObject.setLocked("WorldCheck1_entdate",true);
			formObject.setLocked("WorldCheck1_upddate",true);*/

		}

		else if ("SmartCheck".equalsIgnoreCase(pEvent.getSource().getName())) {
		
			formObject.setLocked("SmartCheck_Frame1",false);//jira-Editable on CAD 1
			formObject.setVisible("SmartCheck_Label2",true);
			formObject.setVisible("SmartCheck_CPVRemarks",true);
			formObject.setVisible("SmartCheck_Label4",false);
			formObject.setVisible("SmartCheck_FCURemarks",false);
			formObject.setLocked("SmartCheck_FCURemarks",true);
			formObject.setLocked("SmartCheck_CPVRemarks",true);
			formObject.setLocked("SmartCheck_Add",false);//PCAS-2772 sagarika
			formObject.setLocked("SmartCheck_Modify",true);  //PCASF-118 Sajan
			formObject.setLocked("SmartCheck_Button1",false);
			//++ Above Code added By Yash on Oct 12, 2017  to fix : 33-"CPV remarks should be disabled" : Reported By Shashank on Oct 09, 2017++

			//formObject.setLocked("SmartCheck1_Modify",true);


		}
		else if ("SmartCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("SmartCheck_Frame1",true);
			//formObject.setVisible("SmartCheck1_Label2",false);
			formObject.setLocked("SmartCheck1_CPVRemarks",true);
			//formObject.setVisible("SmartCheck1_Label4",false);
			formObject.setLocked("SmartCheck1_FCURemarks",true);
			formObject.setLocked("SmartCheck1_Modify",true);
		}
		else if ("RejectEnq".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			//formObject.setLocked("RejectEnq_Frame1",true);

		}
		else if ("SalaryEnq".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			//formObject.setLocked("SalaryEnq_Frame1",true);

		}
		else if ("PostDisbursal".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("PostDisbursal_Frame2",true);

		}
		else if ("CC_Loan".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("CC_Loan_Frame1",true);
			formObject.setEnabled("CC_Loan_Frame1", false);
			loadPicklist_ServiceRequest();

		}
		else if ("Fraud Control Unit".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadPickList("cmplx_Supervisor_SubFeedback_Status", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_Master_SubfeedbackStatus with (nolock)");
	}

		//added by abhishek for CC FSD
		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//notepad_withoutTelLog();

			//formObject.setTop("NotepadDetails_savebutton",410);
			notepad_load();
			//change done by nikhil for PCAS-2356
			formObject.setVisible("NotepadDetails_Frame3",true);
			//Deepak changes done for PCAS-2255
			formObject.setLocked("NotepadDetails_Frame3",false);
		}
		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			//below line commented by Deepak as the same is called form fram expand method.
			//loadInDecGrid();
			LoadReferGrid();
			if(!formObject.isVisible("ExtLiability_Frame1")){
				formObject.fetchFragment("Internal_External_Liability", "Liability_New", "q_Liabilities");
				//setScoreRange();
			}
			//changed by akshay on 6/12/17 for decision alignment
			Decision_cadanalyst1();
			formObject.setEnabled("cmplx_DEC_Manual_Deviation", true);
			//for decision fragment made changes 8th dec 2017
			fragment_ALign("DecisionHistory_Decision_Label1,cmplx_DEC_VerificationRequired#DecisionHistory_Button4#DecisionHistory_CifLock#DecisionHistory_CifUnlock#\n#DecisionHistory_Decision_Label3,cmplx_DEC_Decision#DecisionHistory_Label26,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_dec_reason_code#DecisionHistory_Label13,cmplx_DEC_DeviationCode#DecisionHistory_Label14,cmplx_DEC_DectechDecision#DecisionHistory_Label1,DecisionHistory_NewStrength#DecisionHistory_AddStrength#DecisionHistory_Label3,cmplx_DEC_Strength#\n#DecisionHistory_Label34,DecisionHistory_NewWeakness#DecisionHistory_AddWeakness#DecisionHistory_Label4,cmplx_DEC_Weakness#DecisionHistory_Decision_Label4,cmplx_DEC_Remarks#DecisionHistory_Label15,cmplx_DEC_ScoreGrade#DecisionHistory_Label16,cmplx_DEC_HighDeligatinAuth#DecisionHistory_calReElig#\n#cmplx_DEC_Manual_Deviation#DecisionHistory_ManualDevReason#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#DecisionHistory_Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");
			formObject.setWidth("DecisionHistory_dec_reason_code", 210);
			formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
			formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
			//for decision fragment made changes 8th dec 2017
			DigitalOnBoarding.mLogger.info( "value of manual deviation is:" + formObject.getNGValue("cmplx_DEC_Manual_Deviation"));
			if ("false".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Manual_Deviation"))){
				formObject.setEnabled("DecisionHistory_ManualDevReason", false);
				formObject.setEnabled("DecisionHistory_calReElig", false);
			}
			else {
				formObject.setEnabled("DecisionHistory_ManualDevReason", true);
				formObject.setEnabled("DecisionHistory_calReElig", true);
			}
			//below code added by nikhil for PCSP-46
			formObject.setLocked("cmplx_DEC_Strength", true);
			formObject.setLocked("cmplx_DEC_Weakness", true);
			formObject.setVisible("cmplx_DEC_ContactPointVeri", true);
			formObject.setLocked("cmplx_DEC_ContactPointVeri",false);
			formObject.setTop("cmplx_DEC_ContactPointVeri", 10);
			formObject.setLeft("cmplx_DEC_ContactPointVeri", 840);
			formObject.setVisible("DecisionHistory_CifLock", false);
			formObject.setVisible("DecisionHistory_CifUnlock", false);
		}
		//below code added by nikhil CPV_REQUIRED
		else if ("CustDetailVerification".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			//code changed by nikhil for CPV Changes 16-04-2019
			//formObject.setLocked("CustDetailVerification_Frame1",true);
			//formObject.setEnabled("CustDetailVerification_Frame1", false);
			//done by nikhil for PCAS-2358
			LoadPickList("cmplx_CustDetailVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");
			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_CustDetailVerification_mobno1_ver","cmplx_CustDetailVerification_mobno2_ver","cmplx_CustDetailVerification_dob_verification","cmplx_CustDetailVerification_POBoxno_ver","cmplx_CustDetailVerification_emirates_ver","cmplx_CustDetailVerification_persorcompPOBox_ver","cmplx_CustDetailVerification_resno_ver","cmplx_CustDetailVerification_offtelno_ver","cmplx_CustDetailVerification_hcountrytelno_ver","cmplx_CustDetailVerification_hcontryaddr_ver","cmplx_CustDetailVerification_email1_ver","cmplx_CustDetailVerification_email2_ver");
			LoadPickList("cmplx_CustDetailVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");
			LoadPicklistVerification(LoadPicklist_Verification);
			LoadPickList("cmplx_CustDetailVerification_emirates_upd","select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_emirate with (nolock) order by Code");//PCAS-2509
			enable_custVerification();
			
			//LoadPickList("cmplx_CustDetailVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");
			/*List<String> LoadPicklist_Verification= Arrays.asList("cmplx_CustDetailVerification_mobno1_ver","cmplx_CustDetailVerification_mobno2_ver","cmplx_CustDetailVerification_dob_verification","cmplx_CustDetailVerification_POBoxno_ver","cmplx_CustDetailVerification_emirates_ver","cmplx_CustDetailVerification_persorcompPOBox_ver","cmplx_CustDetailVerification_resno_ver","cmplx_CustDetailVerification_offtelno_ver","cmplx_CustDetailVerification_hcountrytelno_ver","cmplx_CustDetailVerification_hcontryaddr_ver","cmplx_CustDetailVerification_email1_ver","cmplx_CustDetailVerification_email2_ver");
			LoadPicklistVerification(LoadPicklist_Verification);*/
			//LoadPickList("cmplx_CustDetailVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");*/	
			DigitalOnBoarding.mLogger.info("sagarika Y"+formObject.getNGValue("CPV_WAVIER"));
		/*	@511if("Y".equalsIgnoreCase(formObject.getNGValue("CPV_WAVIER")))
			{
			DigitalOnBoarding.mLogger.info("sagarika Y");
			formObject.setNGValue("cmplx_CustDetailVerification_Decision","Not Applicable");
			}*/
			disableforNA();
			if("PA".equalsIgnoreCase(formObject.getNGValue("Sub_Product")))
			{
				formObject.setNGValue("cmplx_CustDetailVerification_Decision","Not Applicable");
				formObject.setLocked("CustDetailVerification_Frame1",true);
			}
			//sagarika for PCAS-2577
			
		}

		else if ("BussinessVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("BussinessVerification_Frame1",true);
			enable_busiVerification();

		}
		else if ("HomeCountryVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("HomeCountryVerification_Frame1",true);
			enable_homeVerification();

		}
		else if ("ResidenceVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ResidenceVerification_Frame1",true);
			enable_ResVerification();

		}
		else if ("ReferenceDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ReferenceDetailVerification_Frame1",true);
			enable_ReferenceVerification();

		}
		else if ("OfficeandMobileVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			
			//nikhil code moved from frame expand event for PCAS-2239AddressDetails_addr_Add
			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_OffVerification_fxdsal_ver","cmplx_OffVerification_accpvded_ver","cmplx_OffVerification_desig_ver","cmplx_OffVerification_doj_ver","cmplx_OffVerification_cnfminjob_ver");
			LoadPicklistVerification(LoadPicklist_Verification);
			LoadPickList("cmplx_OffVerification_colleaguenoverified", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_CPVVeri with (nolock) where Sno != 3 order by code"); //Arun modified on 14/12/17
			//changed by nikhil for CPV changes 17-04
			LoadPickList("cmplx_OffVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) where IsActive='Y' and For_custdetail_only='N' order by code");
			//below code by saurabh on 28th nov 2017.
			LoadPickList("cmplx_OffVerification_offtelnovalidtdfrom", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_offNoValidatedFrom with (nolock)");
			LoadPickList("cmplx_OffVerification_hrdnocntctd", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_HRDContacted with (nolock)");
			
			//formObject.setEnabled("OfficeandMobileVerification_Frame1",false);
			formObject.setLocked("OfficeandMobileVerification_Frame1",true);
			DigitalOnBoarding.mLogger.info( "set visible OfficeandMobileVerification inside condition ");

			formObject.setLocked("cmplx_OffVerification_doj_upd",true);
			//enable_officeVerification();
			// added by abhishek to disable office verification
			//formObject.setLocked("OfficeandMobileVerification_Frame1", true);
			//formObject.setEnabled("OfficeandMobileVerification_Enable", true);
			//-- Above code added by abhishek as per CC FSD 2.7.3
			//++Below code added by nikhil 13/11/2017 for Code merge
			//LoadPickList("cmplx_OffVerification_offtelnovalidtdfrom", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_offNoValidatedFrom with (nolock) order by code");
			//LoadPickList("cmplx_OffVerification_desig_upd", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Designation with (nolock) where isActive='Y' order by Code");

			//--Above code added by nikhil 13/11/2017 for Code merge
		}
		else if ("LoanandCard".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("LoanandCard_Frame1",true);
			enable_loanCard();

		}

	}
	//test
	public String decrypt(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	public String encrypt(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}



}
