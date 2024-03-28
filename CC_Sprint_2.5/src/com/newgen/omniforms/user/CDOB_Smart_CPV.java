
/*------------------------------------------------------------------------------------------------------

                     NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                             : Application -Projects

Project/Product                                                   : Rakbank  

Application                                                       : Credit Card

Module                                                            : Smart CPV

File Name                                                         : Smart_CPV

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
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;
import java.text.SimpleDateFormat;


public class CDOB_Smart_CPV extends CDOB_Common implements FormListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean IsFragLoaded=false;
	String queryData_load="";
	//Mandatory_Frames
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
		
		//changes done to show FPU  tab on smart CPV
		makeSheetsInvisible(tabName, "6,10,11,12,13,14,15,16,17");//pcaf-392
		
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
         //++ below code added by abhishek as per CC FSD 2.7.3
	    	 enable_CPV();
	    	 
	    	//-- Above code added by abhishek as per CC FSD 2.7.3
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
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		
		//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		  formObject =FormContext.getCurrentInstance().getFormReference();

				switch(pEvent.getType())
				{	

					case FRAME_EXPANDED:
					//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
					new CDOB_CommonCode().FrameExpandEvent(pEvent);

				
					 		break;
					
						case FRAGMENT_LOADED:
								//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
								fragment_loaded(pEvent,formObject);
								
								
						
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
		CustomSaveForm();
		
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
	public void submitFormStarted(FormEvent arg0) throws ValidatorException {
		// TODO Auto-generated method stub
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		if(formObject.isVisible("SmartCheck")==false){ 
			formObject.fetchFragment("Smart_Check", "SmartCheck", "q_SmartCheck");
			}
			
			int n=formObject.getLVWRowCount("cmplx_SmartCheck_SmartCheckGrid");
			for(int i=0;i<n;i++){
				formObject.setNGValue("cmplx_SmartCheck_SmartCheckGrid", i, 3, "N");
				DigitalOnBoarding.mLogger.info( "Grid Data[1][6] is:"+formObject.getNGValue("cmplx_SmartCheck_SmartCheckGrid",i,1)+formObject.getNGValue("cmplx_SmartCheck_SmartCheckGrid",i,3));
			}
			formObject.saveFragment("Smart_Check");
			if("Approve".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")) || 
					"Reject".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")))
			{
				formObject.setNGValue("is_CPV_Done","Y");
				formObject.setNGValue("isCPVDone","Yes");
				formObject.setNGValue("cpv","Y");
			}
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
			//formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
			//getNGValue("cmplx_DEC_Decision")
			DigitalOnBoarding.mLogger.info( "Decision smart"+formObject.getNGValue("cmplx_DEC_Decision")); 
			formObject.setNGValue("smart",formObject.getNGValue("cmplx_DEC_Decision"));
			DigitalOnBoarding.mLogger.info( "Smart_CPV_DECsss"+formObject.getNGValue("smart")); 
			//Changed by aman for Decision Name change
			saveIndecisionGrid();
		CustomSaveForm();
	//formObject.setNGValue("cmplx_DEC_Remarks","");
		try{
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
						n=formObject.getLVWRowCount("DecisionHistory_Decision_ListView1");
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
						n=formObject.getLVWRowCount("DecisionHistory_Decision_ListView1");
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
				if(formObject.getNGValue("CUST_HOLD_REF_COUNT_CPV")==null || "".equals(formObject.getNGValue("CUST_HOLD_REF_COUNT_CPV"))){
					formObject.setNGValue("CUST_HOLD_REF_COUNT_CPV","1");
					String currDate=new SimpleDateFormat("yyyy-MM-dd").format(java.util.Calendar.getInstance().getTime());
					DigitalOnBoarding.mLogger.info("sajan test 111 "+currDate);
					formObject.setNGValue("CUST_HOLD_REF_DATE_CPV",currDate);

				}
				else if(Integer.parseInt(formObject.getNGValue("CUST_HOLD_REF_COUNT_CPV"))<3){
					formObject.setNGValue("CUST_HOLD_REF_COUNT_CPV", Integer.parseInt(formObject.getNGValue("CUST_HOLD_REF_COUNT_CPV"))+1);
					DigitalOnBoarding.mLogger.info("sajan test 222 "+formObject.getNGValue("CUST_HOLD_REF_COUNT_CPV"));
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
						formObject.setNGValue("CUST_HOLD_REF_DATE",currDate);
					}
				}
				formObject.setNGValue("CPV_User", formObject.getUserName());
			}
		}
		catch(Exception ex){
			DigitalOnBoarding.mLogger.info("Exception in submitformstarted : "+printException(ex)); 
			DigitalOnBoarding.mLogger.info("Exception in submitformstarted : "+ex.getMessage());
			throw new ValidatorException(new FacesMessage(ex.getMessage()));
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
	
	
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Set form controls on load       

	 ***********************************************************************************  */
	private void fragment_loaded(FormEvent pEvent,FormReference formObject)
	{
		/*if (pEvent.getSource().getName().equalsIgnoreCase("Product")) {
		
		}*/if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			
			// ++ below code not commented at offshore - 06-10-2017
			loadPicklistCustomer();
			//formObject.setLocked("Customer_Frame1",true);
			LoadView(pEvent.getSource().getName());
			//setDisabled();
		}	

		else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
			//Code commented by deepak as we are loading desc so no need to load the picklist(grid is already having desc) - 28Sept2017
			/*
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct");
			LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_ApplicationType with (nolock) order by code");
			LoadPickList("ReqProd", "select '--Select--' union select convert(varchar, description) from NG_MASTER_RequestedProduct with (nolock) where activityName='"+formObject.getWFActivityName()+"'");
			*/
			formObject.setLocked("Product_Frame1",true);
		}

		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("AddressDetails_Frame1",true);
			LoadView(pEvent.getSource().getName());
			loadPicklist_Address();
		}

		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
		//	formObject.setLocked("AltContactDetails_Frame1",true);
			LoadView(pEvent.getSource().getName());

		}

		else if ("WorldCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("WorldCheck1_Frame1",true);

		}

		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {
		//	formObject.setLocked("OECD_Frame8",true);
			LoadView(pEvent.getSource().getName());
			// ++ below code not commented at offshore - 06-10-2017
			loadPicklist_oecd();
		}
		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("KYC_Frame1",true);

		}
		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("FATCA_Frame6",true);
			// ++ below code not commented at offshore - 06-10-2017
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
			//added by nikhil for disbursal showstopper
			Loadpicklist_CardDetails_Frag();
			
		}
		else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("IncomeDetails_Frame1",true);
			LoadPickList("cmplx_IncomeDetails_AvgBalFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_CreditTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_AvgCredTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_AnnualRentFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
		}
		else if ("CompanyDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
               formObject.setLocked("CompanyDetails_Frame1", true);
               
				/*LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_AppType", "select '--Select--' union select convert(varchar, description) from NG_MASTER_ApplicantType with (nolock)");
                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusSector", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusMAcro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_IndusMicro", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_legalEntity", "select '--Select--' union select convert(varchar, description) from NG_MASTER_LegalEntity with (nolock)");
                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_desigVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_emirateOfWork", "select '--Select--' union select convert(varchar, description) from NG_MASTER_emirate with (nolock)");
                LoadPickList("cmplx_CompanyDetails_cmplx_CompanyGrid_headOfficeEmirate", "select '--Select--' union select convert(varchar, description) from NG_MASTER_emirate with (nolock)");*/
			loadPicklist_CompanyDet();
		}
		else if ("AuthorisedSignDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("AuthorisedSignDetails_ShareHolding", true);

			LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			LoadPickList("AuthorisedSignDetails_SignStatus", "select '--Select--' union select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");
			LoadPickList("AuthorisedSignDetails_Combo1", "select '--Select--' as Description,'' as code union select convert(varchar, description),code from NG_MASTER_Designation with (nolock) order by code");
			LoadPickList("AuthorisedSignDetails_Combo2", "select '--Select--' as Description,'' as code union select convert(varchar, description),code from NG_MASTER_Designation with (nolock) order by code");
		}
		else if ("PartnerDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("PartnerDetails_Frame1", true);
			LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
		}
		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("EMploymentDetails_Frame1", true);
			LoadView(pEvent.getSource().getName());
			loadPicklistEmployment();
			//loadPicklistEmployment();
		}
		else if ("FinacleCore".equalsIgnoreCase(pEvent.getSource().getName())) {

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
			// ++ below code not commented at offshore - 06-10-2017
			LoadPickList("FinacleCore_ChequeType", "select '--Select--' as description,'' as code union select convert(varchar, description),Code from ng_MASTER_Cheque_Type with (nolock) order by code");
			LoadPickList("FinacleCore_TypeOfRetutn", "select '--Select--' union select convert(varchar, description) from ng_MASTER_TypeOfReturn with (nolock)");

		}
		else if ("PartMatch".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setNGFrameState("ProductContainer", 0);


	//		LoadPickList("PartMatch_nationality", "select '--Select--','--Select--' union select convert(varchar, Description),code from ng_MASTER_Country with (nolock)");

			formObject.setLocked("PartMatch_Frame1", true);

		}
		else if ("Reference_Details".equalsIgnoreCase(pEvent.getSource().getName())) {
			// ++ below code already present - 06-10-2017 - Reference_Details_Frame1 to Reference_Details_Frame1
			formObject.setLocked("Reference_Details_Frame1",true);

		}
		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ExtLiability_Frame1",true);
			
		}
		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
			
		}
		// ++ below code already present - 06-10-2017
		//added by nikhil as per CC FSD
		else if("CC_Loan".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setLocked("Details", true);
			loadPicklist_ServiceRequest();
		}
		else if("ExternalBlackList".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setLocked("External_Blacklist", true);
		}
		//ended by nikhil
		// ++ above code already present - 06-10-2017
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
		
		else if ("RejectEnq".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("RejectEnq_Frame1",true);
			
		}
		else if ("CustDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadPickList("cmplx_CustDetailVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");
			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_CustDetailVerification_mobno1_ver","cmplx_CustDetailVerification_mobno2_ver","cmplx_CustDetailVerification_dob_verification","cmplx_CustDetailVerification_POBoxno_ver","cmplx_CustDetailVerification_emirates_ver","cmplx_CustDetailVerification_persorcompPOBox_ver","cmplx_CustDetailVerification_resno_ver","cmplx_CustDetailVerification_offtelno_ver","cmplx_CustDetailVerification_hcountrytelno_ver","cmplx_CustDetailVerification_hcontryaddr_ver","cmplx_CustDetailVerification_email1_ver","cmplx_CustDetailVerification_email2_ver");
			LoadPickList("cmplx_CustDetailVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");
			LoadPicklistVerification(LoadPicklist_Verification);
			formObject.setLocked("CustDetailVerification_Frame1",true);
			//below code added by nikhil 16/12/17
			enable_custVerification();
			//done by nikhil for PCAS-2358
			
		}
		else if ("BussinessVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("BussinessVerification_Frame1",true);
			//below code added by nikhil 16/12/17
			enable_busiVerification();
			
		}
		else if ("HomeCountryVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("HomeCountryVerification_Frame1",true);
			//below code added by nikhil 16/12/17
			enable_homeVerification();
			
		}
		else if ("ResidenceVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ResidenceVerification_Frame1",true);
			//below code added by nikhil 16/12/17
			enable_ResVerification();
			
		}
		else if ("ReferenceDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("ReferenceDetailVerification_Frame1",true);
			//below code added by nikhil 16/12/17
			enable_ReferenceVerification();
			//below code added by nikhil 16/12/17
			enable_officeVerification();
			
		}
		else if ("OfficeandMobileVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			//nikhil code moved from frame expand event for PCAS-2239
			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_OffVerification_fxdsal_ver","cmplx_OffVerification_accpvded_ver","cmplx_OffVerification_desig_ver","cmplx_OffVerification_doj_ver","cmplx_OffVerification_cnfminjob_ver");
			LoadPicklistVerification(LoadPicklist_Verification);
			LoadPickList("cmplx_OffVerification_colleaguenoverified", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_CPVVeri with (nolock) where Sno != 3 order by code"); //Arun modified on 14/12/17
			//changed by nikhil for CPV changes 17-04
			LoadPickList("cmplx_OffVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) where IsActive='Y' and For_custdetail_only='N' order by code");
			//below code by saurabh on 28th nov 2017.
			LoadPickList("cmplx_OffVerification_offtelnovalidtdfrom", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_offNoValidatedFrom with (nolock)");
			LoadPickList("cmplx_OffVerification_hrdnocntctd", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_HRDContacted with (nolock)");
			LoadPickList("cmplx_OffVerification_desig_upd", "select '--Select--','--Select--' union ALL select convert(varchar, Description),Description from NG_MASTER_Designation with (nolock) where IsActive='Y'");
			
			formObject.setLocked("OfficeandMobileVerification_Frame1", true);
			formObject.setEnabled("OfficeandMobileVerification_Enable", true);
			
			formObject.setVisible("OfficeandMobileVerification_Enable",true);//arun 14/12/17
			//below code added by nikhil 16/12/17
			//enable_officeVerification();
			
			
		}
		else if ("LoanandCard".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("LoanandCard_Frame1",true);
			//below code added by nikhil 16/12/17
			enable_loanCard();
			
		}
		//added by yash on 30/7/2017
		else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//++Below code commented by nikhil 13/11/2017 for Code merge
			
			/*String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
			CreditCard.mLogger.info( "Activity name is:" + sActivityName);
			formObject.setNGValue("NotepadDetails_Actusername",formObject.getNGValue("cmplx_Customer_FirstNAme") +" "+ formObject.getNGValue("cmplx_Customer_MiddleNAme") +" "+ formObject.getNGValue("cmplx_Customer_LastNAme"));
			formObject.setNGValue("NotepadDetails_user",formObject.getNGValue("cmplx_Customer_FirstNAme") +" "+ formObject.getNGValue("cmplx_Customer_MiddleNAme") +" "+ formObject.getNGValue("cmplx_Customer_LastNAme"));

			formObject.setNGValue("NotepadDetails_insqueue",sActivityName);
			formObject.setLocked("NotepadDetails_noteDate",true);
			formObject.setLocked("NotepadDetails_Actusername",true);
			formObject.setLocked("NotepadDetails_user",true);
			formObject.setLocked("NotepadDetails_insqueue",true);
			formObject.setLocked("NotepadDetails_Actdate",true);
			formObject.setVisible("NotepadDetails_Frame3",true);
			formObject.setVisible("NotepadDetails_save",false);
			formObject.setHeight("NotepadDetails_Frame3",260);*/
			
			//formObject.setLocked("NotepadDetails_Frame1",true);
		// ++ below code not commented at offshore - 06-10-2017	
			//--Above code commented by nikhil 13/11/2017 for Code merge
		notepad_load();
		formObject.setVisible("NotepadDetails_Frame3", true);
		}
		else if ("SmartCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("SmartCheck1_Frame1",true);


			/*formObject.setHeight("SmartCheck1_Label2", 16);
			formObject.setHeight("SmartCheck1_CPVRemarks", 16);*/
		}
		// ++ below code not commented at offshore - 06-10-2017
		//added by abhishek as per CC FSD
		else if ("SmartCheck".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("SmartCheck_CR_Remarks",true);
		    formObject.setLocked("SmartCheck_Add",true);//change for smart_cpv
			
			
		}
		else if ("PostDisbursal".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("PostDisbursal_Frame1",true);
			
		}
		else if ("IncomingDocument".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("IncomingDocument_Frame1",true);

		}
		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			 //for decision fragment made changes 8th dec 2017
			/*formObject.setVisible("DecisionHistory_CheckBox1",false);
            formObject.setVisible("DecisionHistory_Label1",false);
            formObject.setVisible("cmplx_DEC_VerificationRequired",false);
            formObject.setVisible("DecisionHistory_Label3",false);
            formObject.setVisible("DecisionHistory_Combo3",false);
            formObject.setVisible("DecisionHistory_Label6",false);
            formObject.setVisible("DecisionHistory_Combo6",false);
            formObject.setVisible("DecisionHistory_Decision_Label4",false);
            formObject.setVisible("cmplx_DEC_Remarks",false);
            formObject.setVisible("DecisionHistory_Label8",false);
            formObject.setVisible("DecisionHistory_Text4",false);
            formObject.setVisible("DecisionHistory_Label7",false);
            formObject.setVisible("DecisionHistory_Text3",false);
            formObject.setVisible("DecisionHistory_Label2",false);
            formObject.setVisible("DecisionHistory_Text2",false);
            formObject.setVisible("cmplx_DEC_ReferReason",false);
            formObject.setVisible("cmplx_DEC_Description",false);
            formObject.setVisible("cmplx_DEC_Strength",false);
            formObject.setVisible("cmplx_DEC_Weakness",false);
            formObject.setLocked("cmplx_DEC_ContactPointVeri",true);
		
            CreditCard.mLogger.info( "Inside Decision history Load");
            formObject.setVisible("DecisionHistory_Decision_Label1",false);
            formObject.setVisible("DecisionHistory_Label10",false);
            formObject.setVisible("cmplx_DEC_New_CIFID",false);
            formObject.setVisible("DecisionHistory_Button2",false);
            formObject.setVisible("DecisionHistory_chqbook",false);
            formObject.setVisible("DecisionHistory_Label6",false);
            formObject.setVisible("cmplx_DEC_IBAN_No",false);
            formObject.setVisible("cmplx_DEC_NewAccNo",false);
            formObject.setVisible("cmplx_DEC_ChequebookRef",false);
            formObject.setVisible("DecisionHistory_Label9",false);
            formObject.setVisible("cmplx_DEC_DCR_Refno",false);
            formObject.setVisible("DecisionHistory_Label5",false);
            formObject.setVisible("DecisionHistory_Label4",false);
            formObject.setVisible("DecisionHistory_Label27",false);
            formObject.setVisible("cmplx_DEC_Cust_Contacted",false);
            formObject.setVisible("DecisionHistory_Decision_Label4",true);
            formObject.setVisible("cmplx_DEC_Remarks",true);
            formObject.setVisible("DecisionHistory_nonContactable",true);
            formObject.setVisible("DecisionHistory_cntctEstablished",true);
            formObject.setVisible("DecisionHistory_Label11",true);
            formObject.setVisible("DecisionHistory_dec_reason_code",true);
            formObject.setVisible("DecisionHistory_Label12",true);
            formObject.setVisible("cmplx_DEC_NoofAttempts",true);*/
			 DigitalOnBoarding.mLogger.info("***********Inside decision history");
         	fragment_ALign("DecisionHistory_Decision_Label3,cmplx_DEC_Decision#DecisionHistory_Label26,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_dec_reason_code#DecisionHistory_Label12,cmplx_DEC_NoofAttempts#\n#DecisionHistory_Decision_Label4,cmplx_DEC_Remarks#DecisionHistory_nonContactable#DecisionHistory_cntctEstablished#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#DecisionHistory_Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
         	  formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
				formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
			
         	DigitalOnBoarding.mLogger.info("***********Inside after fragment alignment decision history");
         	 //for decision fragment made changes 8th dec 2017
			
            loadPicklist3();
            //below code added by nikhil PCAS-2490
       
			
            formObject.fetchFragment("Smart_Check", "SmartCheck", "q_SmartCheck");
          //  adjustFrameTops("Customer_Details_Verification,Office_Mob_Verification,Smart_Check");//  
        	formObject.fetchFragment("Office_Mob_Verification", "OfficeandMobileVerification", "q_OffVerification");
    		
			int framestate3=formObject.getNGFrameState("Customer_Details_Verification");
			
			if(framestate3 !=0){
				formObject.fetchFragment("Customer_Details_Verification", "CustDetailVerification", "q_CustDetailVeri");
	
				formObject.setNGFrameState("Customer_Details_Verification", 1);

			}
		 } 	
			
	
	}
	


}

