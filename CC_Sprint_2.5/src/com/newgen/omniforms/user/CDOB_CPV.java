
/*------------------------------------------------------------------------------------------------------

                     NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                             : Application -Projects

Project/Product                                                   : Rakbank  

Application                                                       : Credit Card

Module                                                            : CPV

File Name                                                         : CC_CPV

Author                                                            : Disha 

Date (DD/MM/YYYY)                                                 : 

Description                                                       : 

-------------------------------------------------------------------------------------------------------

CHANGE HISTORY

-------------------------------------------------------------------------------------------------------

Problem No/CR No   Change Date   Changed By    Change Description

------------------------------------------------------------------------------------------------------*/

package com.newgen.omniforms.user;

import java.util.Arrays;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;


public class CDOB_CPV extends CDOB_Common implements FormListener
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
				
		makeSheetsInvisible(tabName, "6,11,12,13,14,15,16,17");//pcaf-392
		
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
	    	 DigitalOnBoarding.mLogger.info("Inside CC_Hold_CPV CC");
	    	 new CDOB_CommonCode().setFormHeader(pEvent);
	    	 enable_CPV();
	    	 FormReference formObject= FormContext.getCurrentInstance().getFormReference();
	    	// load_Office_Mob_Verification(formObject);
	        }catch(Exception e)
	        {
	            DigitalOnBoarding.mLogger.info( "Exception:"+e.getMessage());
	        }
	     CheckforRejects("CPV");
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
		  //formObject =FormContext.getCurrentInstance().getFormReference();

				switch(pEvent.getType())
				{	

					case FRAME_EXPANDED:
					//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

					new CDOB_CommonCode().FrameExpandEvent(pEvent);
									
					 		break;
					
						case FRAGMENT_LOADED:
								//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
								fragment_loaded(pEvent);
								
								
						
							
					
					  break;
					  
					case MOUSE_CLICKED:
						//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
						
						new CDOB_CommonCode().mouse_clicked(pEvent);
						break;
						
						
				
					
					 case VALUE_CHANGED:
							//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
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
		CustomSaveForm();
		FormReference formObject= FormContext.getCurrentInstance().getFormReference();
		String Query ="Select CA_Refer_DDVT from NG_CC_EXTTABLE where CC_Wi_Name ='"+formObject.getWFWorkitemName()+"'";
		int count=0;
		try
		{
		DigitalOnBoarding.mLogger.info("query name :"+Query);
		List<List<String>> result=formObject.getDataFromDataSource(Query);
		if(!result.isEmpty()){
			if("Y".equalsIgnoreCase(result.get(0).get(0)))
			{
				count++;
			}
		}
		}
		catch(Exception ex)
		{
			DigitalOnBoarding.mLogger.info("Exception occured in Check_CPV_Refer_DDVT" +ex.getMessage());
		}
		if(count>0)
		{
		String AlertMsg="Workitem Referred to DDVT by UW Unit!";
		throw new ValidatorException(new FacesMessage(AlertMsg));
		}
		
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
		// TODO Auto-generated method stub
	}


	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Logic Before Workitem Done          

	 ***********************************************************************************  */
	@SuppressWarnings("finally")
	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//PCSP-459
		//commented by nikhil for PCAS-2814
		/*if("Approve".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")))
		{
		Other_Detail_Match_check();
		}*/
		try
		{
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
			//PCSP-459
			if( !"CPV Checker".equalsIgnoreCase(formObject.getNGValue("DecisionHistory_ReferTo")))
			{
				formObject.setNGValue("IS_CPV", "N");
			}
			formObject.setNGValue("CPV_Limit", formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
			//code changed for for PCSP - 78 - deepak
			if(formObject.isVisible("OfficeandMobileVerification_Frame1")==true && "Approve".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")))
			{
				DigitalOnBoarding.mLogger.info("Inside submitFormstarted to validate office verification");

				/*if("".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_desig_upd")) )
				{
					formObject.setNGValue("cmplx_OffVerification_desig_upd", formObject.getNGValue("cmplx_EmploymentDetails_Designation"));
				}
				//commeted as the same is not in use any more.
				if("".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_doj_upd")) || "--Select--".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_doj_upd")))
			{
				formObject.setNGValue("cmplx_OffVerification_doj_upd", formObject.getNGValue("cmplx_EmploymentDetails_DOJ"));
			}
				if("".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_cnfrminjob_upd")) || "--Select--".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_cnfrminjob_upd")))
				{
					formObject.setNGValue("cmplx_OffVerification_cnfrminjob_upd", formObject.getNGValue("cmplx_OffVerification_cnfrminjob_val"));
				}*/
				//added by nikhil top save office verification
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("Office_Mob_Verification");
			}

			formObject.setNGValue("CPV_DECISION", formObject.getNGValue("cmplx_DEC_Decision"));
			formObject.setNGValue("Mail_Priority", formObject.getUserName());
			//formObject.setNGValue("VAR_STR4", formObject.getUserName());
			//commented by nikhil for CPV changes 17-04
			/*if("Approve Sub to CIF".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")))
			{
				formObject.setNGValue("IS_Approve_Cif","Y");
			}*/
			if("Approve".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")) || "Reject".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")))
			{
				DigitalOnBoarding.mLogger.info("Inside  Approve CPV");
				formObject.setNGValue("q_Hold1",1);
				formObject.setNGValue("VAR_INT3",1);
				DigitalOnBoarding.mLogger.info("Inside NA Approve CPV " + formObject.getNGValue("q_hold1"));
				//Deepak Changes done for PCAS-2926
				if("Re-Initiate".equalsIgnoreCase(formObject.getNGValue("Reject_DeC"))){
					formObject.setNGValue("Reject_DeC","");
				}
				formObject.setNGValue("is_CPV_Done","Y");
				formObject.setNGValue("isCPVDone","Yes");
				formObject.setNGValue("cpv","Y");
				DigitalOnBoarding.mLogger.info("cpvsss"+formObject.getNGValue("cpv"));
				
				DigitalOnBoarding.mLogger.info("This is CPV approve reject sajan test 111 "+formObject.getNGValue("is_CPV_Done")+" and "+formObject.getNGValue("isCPVDone"));
			}
			if("Approve".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")) || 
					"Reject".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")) ||
					"Approve and Send Back".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision"))){
				formObject.setNGValue("is_CPV_Done","Y");
				formObject.setNGValue("isCPVDone","Yes");
				formObject.setNGValue("cpv","Y");
			}
			
			if("Reject".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision"))){
				formObject.setNGValue("cpv","Y");
				RejectAppNotification();//Deepak To insert Rejected application entry.
			}
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
								+" values('CustomerHold','P','"+formObject.getNGValue("email_id")+"','applicationstatus@rakbank.ae','"+Email_sub+"','"+Email_message+"','"+formObject.getWFWorkitemName()+"','"+formObject.getWFActivityName()+"',"+count+",dateadd(Day,2,GETDATE()))";
						
						DigitalOnBoarding.mLogger.info("Email_sub: "+ reminderInsertQuery);
						formObject.saveDataIntoDataSource(reminderInsertQuery);
					}
					else{
						String reminderInsertQuery="update NG_RLOS_EmailReminder1 set Email_Status='P',count1=0 ,setReminder = dateadd(Day,2,GETDATE())  where WI_Name='"+formObject.getWFWorkitemName()+"'";
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
			DigitalOnBoarding.mLogger.info("sagarika 11 before"+formObject.getNGValue("cmplx_DEC_Decision"));
			DigitalOnBoarding.mLogger.info("sagarika 11 before"+formObject.getNGValue("DecisionHistory_ReferTo"));
			if("Refer".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")) && "Customer_Hold".equalsIgnoreCase(formObject.getNGValue("DecisionHistory_ReferTo"))){
				if(formObject.getNGValue("CUST_HOLD_REF_COUNT_CPV")==null || "".equals(formObject.getNGValue("CUST_HOLD_REF_COUNT_CPV"))){
					formObject.setNGValue("CUST_HOLD_REF_COUNT_CPV","1");
					DigitalOnBoarding.mLogger.info("sagarika 11 "+formObject.getNGValue("CUST_HOLD_REF_COUNT_CPV"));
					String currDate=new SimpleDateFormat("yyyy-MM-dd").format(java.util.Calendar.getInstance().getTime());
					DigitalOnBoarding.mLogger.info("sajan test 111 "+currDate);
					
					formObject.setNGValue("CUST_HOLD_REF_DATE_CPV",currDate);
					DigitalOnBoarding.mLogger.info("sagarika 11 "+formObject.getNGValue("CUST_HOLD_REF_COUNT_CPV"));

				}
				else if(Integer.parseInt(formObject.getNGValue("CUST_HOLD_REF_COUNT_CPV"))<3){
					formObject.setNGValue("CUST_HOLD_REF_COUNT_CPV", Integer.parseInt(formObject.getNGValue("CUST_HOLD_REF_COUNT_CPV"))+1);
					DigitalOnBoarding.mLogger.info("sajan test 222 "+formObject.getNGValue("CUST_HOLD_REF_COUNT"));
				}
				else if(Integer.parseInt(formObject.getNGValue("CUST_HOLD_REF_COUNT_CPV"))==3){
					SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
					String strDate=formObject.getNGValue("CUST_HOLD_REF_DATE_CPV");
					DigitalOnBoarding.mLogger.info("date in DB is "+strDate);

					String strCurrDate=formatter.format(java.util.Calendar.getInstance().getTime());
					DigitalOnBoarding.mLogger.info("Current date is "+strCurrDate);

					java.util.Date date1=formatter.parse(strDate);
					java.util.Date date2=formatter.parse(strCurrDate);
					long diff = date2.getTime() - date1.getTime();
					DigitalOnBoarding.mLogger.info("Difference n days is "+diff);
					int days=(int)(diff / (1000*60*60*24));
					String AlertMsg="Refer to Customer hold can only be done thrice a month";
					DigitalOnBoarding.mLogger.info("Difference n days is@ "+days);
					if(days<30){
						try{
							DigitalOnBoarding.mLogger.info("Difference n days is sagarika "+days);
						throw new ValidatorException(new FacesMessage(AlertMsg));
						}
						catch(Exception exc){
							
							DigitalOnBoarding.mLogger.info("Exception in submitformstarted : "+exc.getMessage());
							throw new ValidatorException(new FacesMessage(exc.getMessage()));
							//DigitalOnBoarding.mLogger.info("Exception in submitformstarted : "+ex.getMessage());
							
						}
						finally
						{
							DigitalOnBoarding.mLogger.info("Inside final condition");
							throw new ValidatorException(new FacesMessage(AlertMsg));
						}
					}
					
					else{
						formObject.setNGValue("CUST_HOLD_REF_COUNT_CPV","1");
						String currDate=new SimpleDateFormat("yyyy-MM-dd").format(java.util.Calendar.getInstance().getTime());
						DigitalOnBoarding.mLogger.info("sajan test 444 "+currDate);
						formObject.setNGValue("CUST_HOLD_REF_DATE_CPV",currDate);
					}
				}
			}
			
			
			/*if("Approve".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")) || "Approve".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision"))){
				formObject.setNGValue("is_CPV_Done","Y");
			}*/
			formObject.setNGValue("CPV_User", formObject.getUserName());
			
			//cmplx_CustDetailVerification_email1_ver
			
			CustomSaveForm();
			LoadReferGrid();
			saveIndecisionGrid();
			//Done By aman for Sprint2
			String squeryrefer = "select referTo from NG_RLOS_GR_DECISION where workstepName like '%CPV%' and dec_wi_name= '" + formObject.getWFWorkitemName() + "' order by dateLastChanged desc ";
			List<List<String>> FCURefer = formObject.getNGDataFromDataCache(squeryrefer);
			DigitalOnBoarding.mLogger.info("RLOS COMMON"+ " iNSIDE prev_loan_dbr+ " + FCURefer);
			String referto="";
			if (FCURefer != null && FCURefer.size() > 0) {
				referto = FCURefer.get(0).get(0);
				}
			if("FPU".equalsIgnoreCase(referto)){		
				formObject.setNGValue("RefFrmCPV", "Y");
			}
			
			if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_SetReminder")) && "CPV Hold".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision"))){
				String Email_sub="REMINDER- case no "+formObject.getWFWorkitemName()+" pending for the action of "+formObject.getUserName();
				//CreditCard.mLogger.info("Email_sub: "+ Email_sub);
				String Email_message="<html><body>Dear User, \n Kindly call the HR/office back. \n \n Remarks: "+formObject.getNGValue("cmplx_OffVerification_reamrks")+". \n Decision Remarks � "+formObject.getNGValue("cmplx_DEC_Remarks")+". </body></html>";
				//CreditCard.mLogger.info("Email_sub: "+ Email_message);
			//For UAT 
				/*String reminderInsertQuery="insert into NG_RLOS_EmailReminder(Email_Name,Email_Status,Email_To,Email_From,EmailSubject,EmailMessage,SetReminder,WI_Name,Workstep_Name)"
				+" values('CPV','P','','test5@rakbanktst.ae','"+Email_sub+"','"+Email_message+"','"+formObject.getNGValue("cmplx_DEC_SetReminder")+"','"+formObject.getWFWorkitemName()+"','"+formObject.getWFActivityName()+"')";
				*/
			//For Production	
				String reminderInsertQuery="insert into NG_RLOS_EmailReminder(Email_Name,Email_Status,Email_To,Email_From,EmailSubject,EmailMessage,SetReminder,WI_Name,Workstep_Name)"
						+" values('CPV','P','','applicationstatus@rakbank.ae','"+Email_sub+"','"+Email_message+"','"+formObject.getNGValue("cmplx_DEC_SetReminder")+"','"+formObject.getWFWorkitemName()+"','"+formObject.getWFActivityName()+"')";
				DigitalOnBoarding.mLogger.info("Email_sub: "+ reminderInsertQuery);
				formObject.saveDataIntoDataSource(reminderInsertQuery);
			}
			
		}
		catch(Exception ex)
		{
			DigitalOnBoarding.mLogger.info("Submit form Started exception CPV"+ex.getMessage());
			throw new ValidatorException(new FacesMessage(ex.getMessage()));
		}
	}
	
	
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Set form controls on load       

	 ***********************************************************************************  */
	
	private void fragment_loaded(FormEvent pEvent)
	{
		
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		/*else if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
		
		}*/
		if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadView(pEvent.getSource().getName());
		//	formObject.setLocked("Customer_Frame1",true);
			formObject.setLocked("cmplx_Customer_DOb", true);
			formObject.setLocked("cmplx_Customer_IdIssueDate", true);
			formObject.setLocked("cmplx_Customer_EmirateIDExpiry", true);
			formObject.setLocked("cmplx_Customer_VisaIssueDate", true);
			formObject.setLocked("cmplx_Customer_PassportIssueDate", true);
			formObject.setLocked("cmplx_Customer_VisaExpiry", true);
			formObject.setLocked("cmplx_Customer_PassPortExpiry", true);
			loadPicklistCustomer();
			//Below line is added by Sajan to enable CPV to view the designation pick list
			formObject.setLocked("Designation_button7", false); 
			formObject.setLocked("Designation_button8_View",false);
			formObject.setEnabled("Designation_button8_View",true);
			//setDisabled();
		}	
		
		if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
			//Code commented by deepak as we are loading desc so no need to load the picklist(grid is already having desc) - 28Sept2017
			/*
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
			LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_ApplicationType with (nolock) order by code");
			LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
			*/
			//code changed to load master's of product based on requested product.
			formObject.setLocked("Product_Frame1",true);
			int prd_count=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			if(prd_count>0){
				String ReqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
				loadPicklistProduct(ReqProd);
			}
		}
		
		else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("IncomeDetails_Frame1",true);
			loadpicklist_Income();}
		else if ("CompanyDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
               formObject.setLocked("CompanyDetails_Frame1", true);
               formObject.setLocked("CompanyDetails_Frame2", true);
               formObject.setLocked("CompanyDetails_Frame3", true);
               formObject.setLocked("TLIssueDate", true);
               formObject.setLocked("TLExpiry", true);
               formObject.setLocked("estbDate", true);
               formObject.setLocked("AuthorisedSignDetails_DOB", true);
               formObject.setLocked("AuthorisedSignDetails_VisaExpiryDate", true);
               formObject.setLocked("AuthorisedSignDetails_PassportExpiryDate", true);
               
               
               formObject.setLocked("PartnerDetails_Dob",true);
         
/*				               LoadPickList("appType", "select '--Select--' union select convert(varchar, description) from NG_MASTER_ApplicantType with (nolock)");
               LoadPickList("indusSector", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
               LoadPickList("indusMAcro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_EmpIndusMacro with (nolock)");
               LoadPickList("indusMicro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_EmpIndusMicro with (nolock)");
               LoadPickList("legalEntity", "select '--Select--' union select convert(varchar, description) from NG_MASTER_LegalEntity with (nolock)");
               LoadPickList("desig", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
               LoadPickList("desigVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
               LoadPickList("EOW", "select '--Select--' union select convert(varchar, description) from NG_MASTER_emirate with (nolock)");
               LoadPickList("headOffice", "select '--Select--' union select convert(varchar, description) from NG_MASTER_emirate with (nolock)");
*/								loadPicklist_CompanyDet();
        }
		
		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadView(pEvent.getSource().getName());
			//formObject.setLocked("AddressDetails_Frame1",true);
			loadPicklist_Address();
		}
		
		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadView(pEvent.getSource().getName());
			//formObject.setLocked("AltContactDetails_Frame1",true);
			LoadPickList("AlternateContactDetails_CustdomBranch", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock) order by code");
			//change by saurabh on 7th Dec
			//LoadPickList("AlternateContactDetails_CardDisp", "select '--Select--' as description,'' as code union all select convert(varchar,description),code from NG_MASTER_Dispatch order by code ");// Load picklist added by aman to load the picklist in card dispatch to
			//change by saurabh for air arabia functionality.
			AirArabiaValid();
		}
		
		
		
		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadView(pEvent.getSource().getName());
			formObject.setLocked("OECD_Frame8",true);
			
		}
		else if ("Reference_Details".equalsIgnoreCase(pEvent.getSource().getName())) {
			//12thsept
			//formObject.setLocked("Reference_Details_Frame1",true);
			formObject.setLocked("Reference_Details_ReferenceRelationship",true);
			//12th sept
			
		}
		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("KYC_Frame1",true);
			
		}
		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("FATCA_Frame6",true);
			formObject.setLocked("FATCA_SignedDate",true);
			formObject.setLocked("FATCA_ExpiryDate",true);
			loadPicklist_Fatca();
			
		}
		else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("SupplementCardDetails_Frame1",true);
			
		}
		else if ("CardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//++below code added by nikhil for Self-Supp CR
			Load_Self_Supp_Data();
			//--above code added by nikhil for Self-Supp CR
			formObject.setLocked("CardDetails_Frame1",true);
			Loadpicklist_CardDetails_Frag();
		}
		else if ("WorldCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("WorldCheck1_Frame1",true);
			
		}

		
		/*else if ("AuthorisedSignDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
        	 formObject.setLocked("AuthorisedSignDetails_ShareHolding", true);
            
        	LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
            LoadPickList("AuthorisedSignDetails_SignStatus", "select '--Select--' union select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");
            LoadPickList("Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
			LoadPickList("DesignationAsPerVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
			}
		
		else if ("PartnerDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("PartnerDetails_Frame1", true);
            LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
        }*/
		
		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("EMploymentDetails_Frame1", true);
			
			formObject.setEnabled("EMploymentDetails_Frame1", false);
			LoadView(pEvent.getSource().getName());
			loadPicklistEmployment();
			
		//loadPicklistEmployment();
		}
		
		if ("FinacleCore".equalsIgnoreCase(pEvent.getSource().getName())) {
           
         if(	NGFUserResourceMgr_DigitalOnBoarding.getGlobalVar("CC_BusinessTitaniumCard").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2)) || NGFUserResourceMgr_DigitalOnBoarding.getGlobalVar("CC_SelfEmployedCreditCard").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2))){
			if(	NGFUserResourceMgr_DigitalOnBoarding.getGlobalVar("CC_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
				formObject.setVisible("FinacleCore_avgbal", true);
				DigitalOnBoarding.mLogger.info( "set visible FinacleCore_Frame8 else if condition ");
				formObject.setVisible("FinacleCore_Frame8", true);
				DigitalOnBoarding.mLogger.info( "after set visible FinacleCore_Frame8 else if condition ");
				formObject.setVisible("FinacleCore_Frame2", false);
				formObject.setVisible("FinacleCore_Frame3", false);
				DigitalOnBoarding.mLogger.info( "Inside fianacle core fragment else if condition ");
				
				}
			else{
					formObject.setVisible("FinacleCore_Frame2", true);
					formObject.setVisible("FinacleCore_Frame3", true);
					formObject.setVisible("FinacleCore_avgbal", false);
					DigitalOnBoarding.mLogger.info( "ELSE set visible FinacleCore_Frame8 ELSE condition ");
					formObject.setVisible("FinacleCore_Frame8", false);
					DigitalOnBoarding.mLogger.info( "AFTER  Inside fianacle core fragment else condition ");
				}
			
			}
         		formObject.setLocked("FinacleCore_Frame1", true);
			
        }
		else if ("PartMatch".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setNGFrameState("ProductContainer", 0);
			
			
			//SLoadPickList("PartMatch_nationality", "select '--Select--','--Select--' union select convert(varchar, Description),code from ng_MASTER_Country with (nolock)");
			
			formObject.setLocked("PartMatch_Frame1", true);
        								
        }
		else if ("Reference_Details".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Reference_Details_Frame1",true);
			
		}
		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ExtLiability_Frame1",true);
			LoadPickList("ExtLiability_contractType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_contract_type with (nolock) order by code");

		}
		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
			
			
		}
		else if ("FinacleCRMIncident".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("FinacleCRMIncident_Frame1",true);
			
		}
		else if ("FinacleCRMCustInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("FinacleCRMCustInfo_Frame1",true);
			
		}
		else if ("MOL1".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("MOL1_Frame1",true);
			loadPicklist_Mol();
		}
		else if ("WorldCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("WorldCheck1_Frame1",true);
			
		}
		else if ("RejectEnq".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("RejectEnq_Frame1",true);
			
		}
		//++Below code added by nikhil 13/11/2017 for Code merge
		
		else if ("CC_Loan".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("CC_Loan_Frame1",true);
			//++Below code added by  nikhil 11/10/17 as per CC FSD 2.2
			loadPicklist_ServiceRequest();
			//--above code added by  nikhil 11/10/17 as per CC FSD 2.2
			
		}
		//--Above code added by nikhil 13/11/2017 for Code merge
		// added by abhishek as per CC FSD
		else if ("CustDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			//done by nikhil for PCAS-2358
			LoadPickList("cmplx_CustDetailVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");
			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_CustDetailVerification_mobno1_ver","cmplx_CustDetailVerification_mobno2_ver","cmplx_CustDetailVerification_dob_verification","cmplx_CustDetailVerification_POBoxno_ver","cmplx_CustDetailVerification_emirates_ver","cmplx_CustDetailVerification_persorcompPOBox_ver","cmplx_CustDetailVerification_resno_ver","cmplx_CustDetailVerification_offtelno_ver","cmplx_CustDetailVerification_hcountrytelno_ver","cmplx_CustDetailVerification_hcontryaddr_ver","cmplx_CustDetailVerification_email1_ver","cmplx_CustDetailVerification_email2_ver");
			LoadPickList("cmplx_CustDetailVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");
			LoadPicklistVerification(LoadPicklist_Verification);
			//code changed by nikhil for CPV Changes 16-04-2019
			formObject.setLocked("CustDetailVerification_Frame1",true);
			//enable_custVerification();

			//LoadPicklistVerification(LoadPicklist_Verification);
			//LoadPickList("cmplx_CustDetailVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");
			enable_custVerification();
			
		/*	if("Y".equalsIgnoreCase(formObject.getNGValue("is_cc_waiver_require")))
			{
				formObject.setNGValue("cmplx_CustDetailVerification_Decision","Not Applicable");
			}*///sagarika for PCAS-2577
		}
		else if ("BussinessVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("BussinessVerification_Frame1",true);
			enable_busiVerification();
			
		}
		else if ("HomeCountryVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("HomeCountryVerification_Frame1",true);
			enable_homeVerification();
			
		}
		else if ("ResidenceVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("ResidenceVerification_Frame1",true);
			enable_ResVerification();
			
		}
		else if ("ReferenceDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("ReferenceDetailVerification_Frame1",true);
			enable_ReferenceVerification();
			
		}
		else if ("OfficeandMobileVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("OfficeandMobileVerification_Frame1",true);
			DigitalOnBoarding.mLogger.info( "set visible OfficeandMobileVerification inside condition ");
			//nikhil code moved from frame expand event for PCAS-2239
			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_OffVerification_fxdsal_ver","cmplx_OffVerification_accpvded_ver","cmplx_OffVerification_desig_ver","cmplx_OffVerification_doj_ver","cmplx_OffVerification_cnfminjob_ver");
			LoadPicklistVerification(LoadPicklist_Verification);
			LoadPickList("cmplx_OffVerification_colleaguenoverified", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_CPVVeri with (nolock) where Sno != 3 order by code"); //Arun modified on 14/12/17
			//changed by nikhil for CPV changes 17-04
			LoadPickList("cmplx_OffVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) where IsActive='Y' and For_custdetail_only='N' order by code");
			//below code by saurabh on 28th nov 2017.
			LoadPickList("cmplx_OffVerification_offtelnovalidtdfrom", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_offNoValidatedFrom with (nolock)");
			LoadPickList("cmplx_OffVerification_hrdnocntctd", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_HRDContacted with (nolock)");
			LoadPickList("cmplx_OffVerification_desig_upd", "select '--Select--','--Select--' as description  union ALL select convert(varchar, Description),Description from NG_MASTER_Designation with (nolock) where IsActive='Y'");
		
			
			//cmplx_OffVerification_fxdsal_val
			//enable_officeVerification();
			// added by abhishek to disable office verification
			formObject.setLocked("OfficeandMobileVerification_Frame1", true);
			formObject.setEnabled("OfficeandMobileVerification_Enable", true);
		//	load_Office_Mob_Verification(formObject);
		//	formObject.setLocked("cmplx_OffVerification_doj_ver", true);
			if(!"Mismatch".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_desig_ver")))
			 {
		 DigitalOnBoarding.mLogger.info( "@@@sss");
		 formObject.setNGValue("cmplx_OffVerification_desig_upd","--Select--");
			 }
		}
		else if ("LoanandCard".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("LoanandCard_Frame1",true);
			enable_loanCard();
			
		}
		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			notepad_load();
	    	
			//formObject.setLocked("NotepadDetails_Frame1",true);
			
			// added by abhishek as per CC FSD
			formObject.setVisible("NotepadDetails_Frame3",true);
			
			//formObject.setLocked("NotepadDetails_Frame1",true);
			
		}
		else if ("SmartCheck".equalsIgnoreCase(pEvent.getSource().getName())) {
			//++ below code added by abhishek as per CC FSD 2.7.3
			
			formObject.setLocked("SmartCheck_Frame1",true);
			//-- Above code added by abhishek as per CC FSD 2.7.3
			
			
		}
		else if ("PostDisbursal".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("PostDisbursal_Frame1",true);
			
		}
		else if ("IncomingDocument".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("IncomingDocument_Frame1",true);
			
		}
		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			
            cpv_Decision();
            loadPicklist3();
            openCPVtabs(formObject);
            DigitalOnBoarding.mLogger.info("@sagarika load");
            //load_Office_Mob_Verification(formObject);
            DigitalOnBoarding.mLogger.info("@sagarika loading");
          //  load_Office_Mob_Verification(formObject);
          //for decision fragment made changes 8th dec 2017
            DigitalOnBoarding.mLogger.info("***********Inside decision history");
        	fragment_ALign("DecisionHistory_Decision_Label1,cmplx_DEC_VerificationRequired#DecisionHistory_Decision_Label3,cmplx_DEC_Decision#DecisionHistory_Label26,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_dec_reason_code#DecisionHistory_Label41,cmplx_DEC_NoofAttempts#DecisionHistory_Label12,cmplx_DEC_SetReminder#\n#DecisionHistory_Decision_Label4,cmplx_DEC_Remarks#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#DecisionHistory_Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
        	
        	//below code added by nikhil for PCSP-232
        	formObject.setWidth("DecisionHistory_dec_reason_code", 210);
        	formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
			formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
			
        	DigitalOnBoarding.mLogger.info("***********Inside after fragment alignment decision history");
			
        	//for decision fragment made changes 8th dec 2017
        	formObject.setLocked("cmplx_DEC_VerificationRequired", true);
        	//formObject.fetchFragment("Office_Mob_Verification", "OfficeandMobileVerification", "q_OffVerification");
    		
            	

            	//loadPicklist1();
		 } 	
		else if ("SmartCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("SmartCheck_Frame1",true);
			//formObject.setVisible("SmartCheck1_Label1",false);
			formObject.setLocked("SmartCheck1_CreditRemarks",true);
			//formObject.setVisible("SmartCheck1_Label4",false);
			formObject.setLocked("SmartCheck1_FCURemarks",true);
			formObject.setLocked("SmartCheck1_Modify",true);
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

