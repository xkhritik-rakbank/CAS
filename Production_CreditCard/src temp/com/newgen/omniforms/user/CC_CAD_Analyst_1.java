


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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.faces.validator.ValidatorException;

import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;


public class CC_CAD_Analyst_1 extends CC_Common implements FormListener
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
		CreditCard.mLogger.info( "Inside formLoaded()" + pEvent.getSource().getName());

		makeSheetsInvisible(tabName, "11,12,13,14,15,16,17");

		formObject.setVisible("SmartCheck_Label2",true);
		formObject.setVisible("cmplx_SmartCheck_SmartCheckGrid_cpvremarks",true);



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
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();

			//added by akshay for proc 12855
			//Deepak below commented to show CPV table in CA1 stage for all cases PCAS-2447 
			/*if("N".equalsIgnoreCase(formObject.getNGValue("CPV_REQUIRED"))){
				formObject.setSheetVisible(tabName,8,false);
			}*/
			formObject.setNGValue("Mandatory_Frames", NGFUserResourceMgr_CreditCard.getGlobalVar("CSM_Frame_Name"));
			new CC_CommonCode().setFormHeader(pEvent);
			enable_cad1();
			enable_CPV();
			
		}
		catch(Exception e)
		{
			CreditCard.mLogger.info( "Exception:"+CC_Common.printException(e));
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

		//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

		switch(pEvent.getType())
		{	

		case FRAME_EXPANDED:
			//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			new CC_CommonCode().FrameExpandEvent(pEvent);						
			break;




		case FRAGMENT_LOADED:
			//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());

			fragment_loaded(pEvent,hm,formObject);
			//loadPicklist1();
			break;


		case MOUSE_CLICKED:
			//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			new CC_CommonCode().mouse_clicked(pEvent);



			break;

			// added by abhishek for repeater end

			//ended by yash hold_alternate
		case VALUE_CHANGED:
			//CreditCard.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
			new CC_CommonCode().value_changed(pEvent);
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
			CreditCard.mLogger.info( printException(ex));
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
			CreditCard.mLogger.info("inside submitformcompleted: ");

			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String Decision= formObject.getNGValue("cmplx_DEC_Decision");
			CreditCard.mLogger.info("inside submitformcompleted: Decision"+Decision);

			CreditCard.mLogger.info("inside submitformcompleted:2 Decision"+Decision);
			List<String> objInput=new ArrayList<String>();
			//disha FSD cad delegation procedure changes
			objInput.add("Text:"+formObject.getWFWorkitemName());
			List<Object> objOutput=new ArrayList<Object>();

			objOutput.add("Text");
			//formObject.getDataFromStoredProcedure("DummyProc", objInput, objOutput);
			CreditCard.mLogger.info("inside submitformcompleted: Decision"+formObject.getWFWorkitemName());
			objInput.add("Text:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1));
			CreditCard.mLogger.info("inside submitformcompleted: Decision"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1));
			objInput.add("Text:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2));
			CreditCard.mLogger.info("inside submitformcompleted: Decision"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2));
			objInput.add("Text:"+formObject.getNGValue("cmplx_DEC_HighDeligatinAuth"));
			CreditCard.mLogger.info("inside submitformcompleted: Decision"+formObject.getNGValue("cmplx_DEC_HighDeligatinAuth"));

			//change by saurabh on 7th Dec
			objInput.add("Text:"+Decision);
			CreditCard.mLogger.info("objInput args are: "+objInput.get(0)+objInput.get(1)+objInput.get(2)+objInput.get(3)+objInput.get(4));
			if ("".equalsIgnoreCase(formObject.getNGValue("CAD_dec_tray"))|| "Select".equalsIgnoreCase(formObject.getNGValue("CAD_dec_tray"))){
				objOutput = formObject.getDataFromStoredProcedure("ng_rlos_CADLevels", objInput,objOutput);
			}
			
			if("Approve".equalsIgnoreCase(Decision)){
				objInput.clear();
				objInput.add("Text:"+formObject.getWFWorkitemName());
				objInput.add("Text:"+"Waiting for Approval");
				objOutput.clear();
				objOutput.add("Text");
				objOutput= formObject.getDataFromStoredProcedure("ng_EFMS_InsertData", objInput,objOutput);
				CreditCard.mLogger.info("After ng_EFMS_InsertData: objOutput is "+objOutput);
			}
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
	public void submitFormStarted(FormEvent arg0) throws ValidatorException 
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		// TODO Auto-generated method stub
		/*formObject.setNGValue("Mail_CC", formObject.getUserName());*/
		//formObject.setNGValue("VAR_STR6", formObject.getUserName());
		//change by nikhil for PCSP-472
		
		Check_EFC_Limit();
		
		//added by nikhil for Employment Match check PCSP-459
		if("Approve".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")) && !"Waiver".equalsIgnoreCase(formObject.getNGValue("CPV_Analyst_dec")))
		{
		Employment_Match_Check();
		Capture_ATC_Fields();
		}
		try{
			
			
			//below code added by nikhil for PCSP-472
			
			CustomSaveForm();
			//Deepak Code PSCP-270 moved from CAD 1 to CAD 2
				formObject.setNGValue("CAD_Username", formObject.getUserName());
				
			CreditCard.mLogger.info("inside try block of submitFormStarted at CAD 1");
			// below line commented because now we have one common decision fragment
			//formObject.setNGValue("CAD_ANALYST1_DECISION", formObject.getNGValue("cmplx_CADDecision_Decision"));
			formObject.setNGValue("CAD_ANALYST1_DECISION", formObject.getNGValue("cmplx_DEC_Decision"));
			formObject.setNGValue("Highest_Delegation_CAD", formObject.getNGValue("cmplx_DEC_HighDeligatinAuth")); 
			formObject.setNGValue("Decision", formObject.getNGValue("cmplx_DEC_Decision"));
			formObject.setNGValue("CAD_dec_tray", formObject.getNGValue("cmplx_DEC_ReferTo")); //Arun (07/10)
			CreditCard.mLogger.info("inside try block of submitFormStarted CAD_ANALYST1_DECISION: " + formObject.getNGValue("CAD_ANALYST1_DECISION"));

			if(NGFUserResourceMgr_CreditCard.getGlobalVar("CC_refertoSmartCPV").equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")))
			{
				saveSmartCheckGrid();
			}
			if(NGFUserResourceMgr_CreditCard.getGlobalVar("CC_RefertoCredit").equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")))
			{
				//++ below code added 10-10-2017 - to save cad decision tray in external table
				CreditCard.mLogger.info("inside submitFormStarted value of ReferTo : "+ formObject.getNGValue("cmplx_DEC_ReferTo")); //Arun (11/10)
				formObject.setNGValue("Cad_Deviation_Tray",formObject.getNGValue("cmplx_DEC_ReferTo")); //Arun (11/10)
				formObject.setNGValue("q_Emailto",formObject.getNGValue("cmplx_DEC_ReferTo")); //Arun (11/10)
			}

			//++ below code aded by disha on 27-3-2018 to set value of var_int3
			if("STP".equalsIgnoreCase(formObject.getNGValue("Highest_Delegation_CAD")))
			{
				CreditCard.mLogger.info("Inside STP ");
				if("Approve".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")))
				{
					CreditCard.mLogger.info("Inside STP Approve CAD1");
					formObject.setNGValue("q_hold1", 1);
					formObject.setNGValue("q_Hold2", 1);
					//formObject.setNGValue("VAR_INT3",1);
					CreditCard.mLogger.info("Inside STP Approve " + formObject.getNGValue("q_hold1"));
				}
			}
			//++ above code aded by disha on 27-3-2018 to set value of var_int3
			if("Refer".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")) || "Reject".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")))
			{
				formObject.setNGValue("CAD_dec_tray", formObject.getNGValue("cmplx_DEC_ReferTo"));
			}
			//changed by nikhil 27/11 to set header
			formObject.setNGValue("Final_Limit", formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
			formObject.setNGValue("LoanLabel",formObject.getNGValue("Final_Limit"));
			//added by nikhil for CPV changes
			if("Approve Sub to CIF".equalsIgnoreCase(formObject.getNGValue("cmplx_CustDetailVerification_Decision")) && !"C".equalsIgnoreCase("IS_Approve_Cif") && "Approve".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision")))
			{
				formObject.setNGValue("IS_Approve_Cif", "Y");
			}
			else
			{
				formObject.setNGValue("IS_Approve_Cif", "NA");
			}
			if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_SetReminder_CA")) && "CA_HOLD".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision"))){
				SetReminder_CA(formObject);
			}
		}catch(Exception ex){
			CreditCard.mLogger.info("Exception in submitformstarted : "+printException(ex)); 
		}
		finally{
			try{
				//commented by deepak as the same is creating issue for refer. 
				//LoadReferGrid();
				saveIndecisionGrid();
			}
			catch(Exception e){
				CreditCard.mLogger.info("Exception occured in submitFormCompleted"+e.getStackTrace());
			}
		}
		////formObject.setNGValue("cmplx_DEC_Remarks","");cmplx_EmploymentDetails_targetSegCode
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
		CreditCard.mLogger.info( "EventName: before CPV tab validations");
		/*if ("Business_Verification".equalsIgnoreCase(pEvent.getSource().getName())) {
		hm.put("Business_Verification","Clicked");
		formObject.fetchFragment("Business_Verification", "BussinessVerification", "q_BussVerification");
		LoadPickList("cmplx_BussVerification_contctTelChk", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Other' ");
		formObject.setLocked("BussinessVerification_Frame1", true);
		//++ Below Code added By Yash on Oct 12, 2017  to fix : 36-"Fields are not populated from the company details fragment " : Reported By Shashank on Oct 09, 2017++

		enable_busiVerification();
		//++ Above Code added By Yash on Oct 12, 2017  to fix : 36-"Fields are not populated from the company details fragment" : Reported By Shashank on Oct 09, 2017++


		}
		else if ("HomeCountry_Verification".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("HomeCountry_Verification","Clicked");
			formObject.fetchFragment("HomeCountry_Verification", "HomeCountryVerification", "q_HCountryVerification");
			LoadPickList("cmplx_HCountryVerification_Hcountrytelverified", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Other' ");
			formObject.setLocked("HomeCountryVerification_Frame1", true);
		}
		else if ("ResidenceVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("ResidenceVerification","Clicked");
			formObject.fetchFragment("ResidenceVerification", "ResidenceVerification", "q_ResiVerification");
			LoadPickList("cmplx_ResiVerification_Telnoverified", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Other' ");
			formObject.setLocked("ResidenceVerification_Frame1", true);	
		}
		else if ("Reference_Detail_Verification".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Reference_Detail_Verification","Clicked");
			formObject.fetchFragment("Reference_Detail_Verification", "ReferenceDetailVerification", "q_RefDetailVerification");
			LoadPickList("cmplx_RefDetVerification_ref1cntctd", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Other' ");
			LoadPickList("cmplx_RefDetVerification_ref2cntctd", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Other' ");

	}

		else if ("LoanCard_Details_Check".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("LoanCard_Details_Check","Clicked");
			formObject.fetchFragment("LoanCard_Details_Check", "LoanandCard", "q_LoanandCard");
			loadPicklist_loancardverification();
			formObject.setLocked("LoanandCard_Frame1", true);
		}*/
		if ("Notepad_Details".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Notepad_Details","Clicked");
			//formObject.fetchFragment("Notepad_Details", "NotepadDetails", "q_NotepadDetails");
			notepad_load();
			//formObject.setVisible("NotepadDetails_Frame1", false);
			//change done by nikhil for PCAS-2356
			formObject.setVisible("NotepadDetails_Frame3",true);
			//Deepak changes done for PCAS-2255
			formObject.setLocked("NotepadDetails_Frame3",false);
			/*String notepad_desc = formObject.getNGValue("NotepadDetails_notedesc");
		formObject.setNGValue("NotepadDetails_notecode",notepad_desc);
			 */}
		/*else if ("Smart_Check".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Smart_Check","Clicked");
			formObject.fetchFragment("Smart_Check", "SmartCheck", "q_SmartCheck");
			formObject.setLocked("SmartCheck_Frame1", true);

		}*/

		else if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.setLocked("Customer_Frame1",true);
			LoadView(pEvent.getSource().getName());
			formObject.setLocked("cmplx_Customer_DOb",true);
			formObject.setLocked("cmplx_Customer_IdIssueDate",true);
			formObject.setLocked("cmplx_Customer_EmirateIDExpiry",true);
			formObject.setLocked("cmplx_Customer_VisaExpiry",true);
			formObject.setLocked("cmplx_Customer_PassportIssueDate",true);
			formObject.setLocked("cmplx_Customer_PassPortExpiry",true);
			formObject.setLocked("cmplx_Customer_VisaIssueDate",true);
			loadPicklistCustomer();
			/*formObject.setLocked("Customer_FetchDetails",true);
					formObject.setLocked("Customer_save",true);*/

			//setDisabled();
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

			//formObject.setLocked("Product_Frame1",true);
			/*formObject.setLocked("Add",true);
					formObject.setLocked("Modify",true);
					formObject.setLocked("Delete",true);
					formObject.setLocked("Product_Save",true);
					formObject.setLocked("cmplx_Product_cmplx_ProductGrid_table",true);
					formObject.setLocked("Customer_save",true);
					formObject.setLocked("Customer_save",true);*/
		}



		//added by yash for CC FSD
		else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			
			//formObject.setLocked("IncomeDetails_Frame1",true);
			LoadPickList("cmplx_IncomeDetails_AvgBalFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_CreditTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_AvgCredTurnoverFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			LoadPickList("cmplx_IncomeDetails_AnnualRentFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_Frequency with (nolock)");
			/*LoadPickList("cmplx_IncomeDetails_Account_num_sal", "select Acctid as Account_ID from  ng_RLOS_CUSTEXPOSE_AcctDetails where child_wi ='"+formObject.getWFWorkitemName()+"' and CifId='"+formObject.getNGValue("cmplx_Customer_CIFNO")+"' order by case when accttype='CURRENT ACCOUNT' then '1'  when accttype='FAST SAVER' then '2' else accttype end asc");
			LoadPickList("cmplx_IncomeDetails_Account_self_num", "select Acctid as Account_ID from  ng_RLOS_CUSTEXPOSE_AcctDetails where  child_wi ='"+formObject.getWFWorkitemName()+"' and CifId='"+formObject.getNGValue("cmplx_Customer_CIFNO")+"' order by case when accttype='CURRENT ACCOUNT' then '1'  when accttype='FAST SAVER' then '2' else accttype end asc");
			*/formObject.setLocked("cmplx_IncomeDetails_Overtime_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_grossSal",true);
			formObject.setLocked("cmplx_IncomeDetails_totSal",true);
			//	formObject.setLocked("cmplx_IncomeDetails_Commission_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_FoodAllow_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_PhoneAllow_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_serviceAllow_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_Bonus_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_Other_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_Flying_Avg",true);
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
					//CreditCard.mLogger.info("@sagarika salaried");
					formObject.setVisible("IncomeDetails_Frame3",true);
					formObject.setVisible("IncomeDetails_Frame2",true);
				}
				else{
					formObject.setVisible("IncomeDetails_Frame3",false);
				}
			}
	/// Added by Rajan for PCASP-2123
			if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("BPA") && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6).equalsIgnoreCase("Salaried") )
			{
				formObject.setVisible("IncomeDetails_Label22", false);
				formObject.setVisible("cmplx_IncomeDetails_Account_num_sal",false);
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
			
			if("Self Employed".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6))||"Salaried".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6))){
			String query="select AccountOpenDate from ng_RLOS_CUSTEXPOSE_AcctDetails where Child_Wi='"+formObject.getWFWorkitemName()+"' and AcctStat in ('ACTIVE','A') and CifId='"+formObject.getNGValue("cmplx_Customer_CIFNo")+"' order by AccountOpenDate asc";
			 List<List<String>> result = formObject.getDataFromDataSource(query);
			 if(result!=null && result.size()>0){
				 formObject.setNGValue("cmplx_IncomeDetails_DurationOfBanking2", result.get(0).get(0));
				 formObject.setNGValue("cmplx_IncomeDetails_DurationOfBanking", result.get(0).get(0));
			 }
			}
		}
		// ended by yash for CC FSD
		else if ("CompanyDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			String subProduct = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2);
			
			//For PCASP-2036
			
			
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
			//formObject.setVisible("CompanyDetails_Label15",true);
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
			formObject.setEnabled("CompanyDetails_Button3", false); // Added by Rajan for PCASP- 1609
			formObject.setEnabled("AuthorisedSignDetails_CheckBox1", false);
			formObject.setEnabled("CompanyDetails_CheckBox4", false);
			
			//for PCASP-2722
			formObject.setVisible("CompanyDetails_PartnerDetails_Label2", false);
			formObject.setVisible("PartnerDetails_authsigname", false);
		//	formObject.setLocked("lob", false);
			formObject.setEnabled("lob", true);
			//changed by sagarika to resolve proc 10862
			//change by saurabh on 6th Dec
			//CreditCard.mLogger.info("Before NEP condition"+formObject.getNGValue("cmplx_Customer_NEP"));
			if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP"))){
				CreditCard.mLogger.info("inside NEP if condition");
				formObject.setLocked("CompanyDetails_Label15", false);
				formObject.setLocked("NepType", false);
			}
			else{
				CreditCard.mLogger.info("inside NEP else condition");
				formObject.setLocked("CompanyDetails_Label15", true);
				formObject.setLocked("NepType", true);
			}
			//change by saurabh on 6th Dec
			formObject.setLocked("AuthorisedSignDetails_CIFNo", true);
			formObject.setLocked("AuthorisedSignDetails_FetchDetails", true);

			formObject.setEnabled("CompanyDetails_EffectiveLOB",true);
			formObject.setLocked("CompanyDetails_EffectiveLOB",false);

			formObject.setLocked("EmployerCategoryPL",true);
			formObject.setLocked("EmployerStatusCC",true);
			formObject.setLocked("EmployerStatusPLExpact",true);
			formObject.setLocked("EmployerStatusPLNational",true);
			//Added by shivang for PCASP-1807
			formObject.setLocked("AuthorisedSignDetails_Name",true);
			formObject.setLocked("AuthorisedSignDetails_nationality",true);
			formObject.setLocked("AuthorisedSignDetails_VisaNumber",true);
			formObject.setLocked("AuthorisedSignDetails_Status",true);
			formObject.setLocked("AuthorisedSignDetails_PassportNo",true);
			//Added by Shivang for PCASP-1714
			formObject.setLocked("compIndus",true);
			formObject.setLocked("indusSector",true);
			formObject.setLocked("indusMAcro",true);
			formObject.setLocked("indusMicro",true);
			formObject.setLocked("legalEntity",true);
			formObject.setLocked("cif", true);
			//Added by shivang
			AECBHistMonthCnt();
			formObject.setLocked("CompanyDetails_Frame2", true);
			/*LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--' as Description, '' as code union select convert(varchar, description),code from NG_MASTER_Country with (nolock) order by code");
			LoadPickList("AuthorisedSignDetails_Status", "select '--Select--' as Description,'' as code union select convert(varchar, description),code from NG_MASTER_SignatoryStatus with (nolock) order by code");
			LoadPickList("Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
			LoadPickList("DesignationAsPerVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
			LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			 */
			/* PCASP-3002
			   String customerName = formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_MiddleName")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme");
	        	new CC_CommonCode().AddInAuthorisedGrid(formObject,customerName);
	    		new CC_CommonCode().AddInCompanyGrid(formObject, customerName);
	    		*/
    		//PCASP-2847 by shweta,PCASP-2915
			String appCatg = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",1,21);
			if(formObject.getNGValue("Sub_Product").equalsIgnoreCase("SE") && "BAU".equalsIgnoreCase(appCatg)
					|| formObject.getNGValue("Sub_Product").equalsIgnoreCase("BPA")
					|| formObject.getNGValue("Sub_Product").equalsIgnoreCase("SEC")){
				formObject.setVisible("CompanyDetails_Label29",false);
				formObject.setVisible("headOffice", false);
				formObject.setVisible("CompanyDetails_Label15", false);
				formObject.setVisible("NepType", false);
				formObject.setVisible("CompanyDetails_Label5", false);
				formObject.setVisible("PromotionCode", false);//PCASP-2915
			} 
			//pcasp-2856 cmplx_EmploymentDetails_Indus_Macro
			if(subProduct.equalsIgnoreCase("SE")){
				formObject.setVisible("CompanyDetails_Label5", false);
				formObject.setVisible("PromotionCode", false);
				formObject.setVisible("CompanyDetails_Label15", false);
				formObject.setVisible("NepType", false);
				formObject.setVisible("CompanyDetails_Label29",false);// Added by Rajan for PCASP-2124
				formObject.setVisible("headOffice", false);
			}
			else if(subProduct.equalsIgnoreCase("BTC")){
				formObject.setVisible("CompanyDetails_Label5", false);
				formObject.setVisible("PromotionCode", false);
				formObject.setVisible("CompanyDetails_Label29",false);
				formObject.setVisible("headOffice", false);
				formObject.setVisible("CompanyDetails_Label15", false);
				formObject.setVisible("NepType", false);
			}else if(subProduct.equalsIgnoreCase("PA")){
				formObject.setVisible("CompanyDetails_Label5", false);
				formObject.setVisible("PromotionCode", false);
				formObject.setVisible("CompanyDetails_Label15", false);
				formObject.setVisible("NepType", false);
				formObject.setVisible("CompanyDetails_Label29",false);
				formObject.setVisible("headOffice", false);
				formObject.setVisible("CompanyDetails_Label20", false);
				formObject.setVisible("ClassificationCode", false);
			} 
	    	
		}
		/*	else if ("AuthorisedSignDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			//commented by Akshay on 7/9/2017 for firing this call on CAD
			//formObject.setLocked("AuthorisedSignDetails_ShareHolding", true);
			formObject.setLocked("AuthorisedSignDetails_CIFNo", true);
			formObject.setLocked("AuthorisedSignDetails_Button4", true);
			LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--' as Description, '' as code union select convert(varchar, description),code from NG_MASTER_Country with (nolock) order by code");
			LoadPickList("AuthorisedSignDetails_SignStatus", "select '--Select--' as Description,'' as code union select convert(varchar, description),code from NG_MASTER_SignatoryStatus with (nolock) order by code");
			LoadPickList("Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
			LoadPickList("DesignationAsPerVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");

		}
		//added by yash for CC FSD
		else if ("PartnerDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			LoadPickList("PartnerDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
			formObject.setEnabled("PartnerDetails_Dob",true);
			//loadPicklist_Address();
			//formObject.setLocked("PartnerDetails_Frame1",true);

		}*/
		else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {
			if(("IM".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2))))
			{
		formObject.setVisible("ExtLiability_Takeoverindicator", false); //Added by Rajan
		formObject.setVisible("ExtLiability_takeoverAMount", false);//Added by Rajan
		formObject.setVisible("ExtLiability_QCAmt", false);//Added by Rajan
		formObject.setVisible("ExtLiability_QCEMI",false);//Added by Rajan
		formObject.setVisible("ExtLiability_CACIndicator",false);//Added by Rajan
		formObject.setVisible("ExtLiability_Label23",false);//Added by Rajan
		formObject.setVisible("ExtLiability_Label26" , false); //Added by Rajan
		formObject.setVisible("ExtLiability_Label20",false);// Added by Rajan
		formObject.setVisible("Liability_New_Utilization",false);// Added by Rajan
		formObject.setVisible("Liability_New_Label1",false);//Added by Rajan
		formObject.setVisible("Liability_New_MOB", false);//Added by Rajan
		formObject.setVisible("Liability_New_Label2", false); //Added by Rajan
		formObject.setVisible("Liability_New_Label3", false);//Added by Rajan
		formObject.setVisible("Liability_New_Label5", false);// Added by Rajan for PCASP-1522
		formObject.setVisible("Liability_New_worstStatusInLast24", false); //Added by Rajan for PCASP-1522 
		formObject.setVisible("Liability_New_Label10", false);// Added by Rajan for PCASP-1523
		formObject.setVisible("Liability_New_rejApplinlast3months", false);//  Added by Rajan for PCASP-1523
		//Added by shivang for pcasp-1515
		formObject.setVisible("ExtLiability_Label15",true);
		formObject.setVisible("cmplx_Liability_New_AggrExposure",true);
		formObject.setLocked("ExtLiability_Label15",true);
		formObject.setLocked("cmplx_Liability_New_AggrExposure",true);
			}
			//loadPicklist_Address();
			//formObject.setLocked("ExtLiability_Frame1",true);
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
			//formObject.setVisible("Liability_New_Label4",false);
			formObject.setLocked("Liability_New_writeOfAmount",true);
			//formObject.setVisible("Liability_New_Label5",false);
			formObject.setLocked("Liability_New_worstStatusInLast24",true);
			//formObject.setVisible("Liability_New_Label2",false);
			//formObject.setVisible("Liability_New_Label10",false);
			formObject.setLocked("Liability_New_rejApplinlast3months",true);
			//Changed by aman for onsite verificatiobn sheet 17/12
			LoadPickList("ExtLiability_contractType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_contract_type with (nolock) order by code");

			//Below code commented by shivang for hiding Aggregate Exposure
			//formObject.setVisible("ExtLiability_Label15",true);
			//formObject.setVisible("cmplx_Liability_New_AggrExposure",true);
			
			
			//below commented for Nikhil For PCSP-138
			/*if((formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode").equals("CAC") || formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",1,22).equals("CAC" ) )){
				formObject.setVisible("Liability_New_Label12",true);
				formObject.setVisible("cmplx_Liability_New_CACBankName",true);
			}*/

			if("Self Employed".equalsIgnoreCase( formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6)))
			{
				formObject.setVisible("cmplx_Liability_New_DBR",false);
				formObject.setVisible("ExtLiability_Label9",false);
				formObject.setVisible("cmplx_Liability_New_TAI",false);
				formObject.setVisible("ExtLiability_Label25",false);
				formObject.setVisible("cmplx_Liability_New_DBRNet",false);
				formObject.setVisible("ExtLiability_Label14",false);
				formObject.setVisible("Liability_New_Label6",true);
				formObject.setVisible("cmplx_Liability_New_NoofotherbankAuth",true);
				formObject.setVisible("Liability_New_Label8",true);
				formObject.setVisible("cmplx_Liability_New_NoofotherbankCompany",true);
				formObject.setVisible("Liability_New_Label7",true);
				formObject.setVisible("cmplx_Liability_New_TotcleanFundedAmt",true);
				//below code added by nikhil for PCSP-138
				if(formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",1,22).equals("CAC" ) ){
					formObject.setVisible("Liability_New_Label12",true);
					formObject.setVisible("cmplx_Liability_New_CACBankName",true);
				}
				formObject.setVisible("Liability_New_Label14",true);
				formObject.setVisible("cmplx_Liability_New_Aecb_Score_Company",true);
				formObject.setVisible("Liability_New_Label15",true);
				formObject.setVisible("cmplx_Liability_New_Aecb_range_Company",true);
			}
			formObject.setNGValue("cmplx_Liability_New_overrideIntLiab", true);
			formObject.setVisible("Liability_New_Overwrite", true);
			Aecb_checks(formObject.getNGValue("Sub_Product"),formObject.getNGValue("EmploymentType"),formObject);
			if("N".equalsIgnoreCase(formObject.getNGValue("Corporate_Aecb")	))
			{
				formObject.setLocked("Liability_New_Button1", true);
			}
			else if ("Y".equalsIgnoreCase(formObject.getNGValue("Corporate_Aecb")	))
			{
				formObject.setLocked("Liability_New_Button1", false);
				formObject.setEnabled("Liability_New_Button1", true);
			}
		/*	if("BPA".equalsIgnoreCase(formObject.getNGValue("Sub_Product")))
			{
				formObject.setLocked("ExtLiability_AECBReport",true);
				formObject.setLocked("Liability_New_Button1",true);
			}*/
		}
		//added by yash for CC FSD ExtLiability_AECBReport
		else if ("Compliance".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("Compliance_Frame1",true);

		}
		//ended by yash for CC FSD ELigibiltyAndProductInfo_Button1


		else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//Added by shivang for PCASP-887
			if(!"SAL".equalsIgnoreCase(formObject.getNGValue("Sub_Product")))//sagarika for PCASP-2348
			{
				formObject.setVisible("EMploymentDetails_Label64", false);
				formObject.setVisible("cmplx_EmploymentDetails_OtherDesign",false);
			}//sagarika for PCASP-2348
			else
			{
			formObject.setVisible("EMploymentDetails_Label64", true);
			formObject.setVisible("cmplx_EmploymentDetails_OtherDesign", true);
			}
			//formObject.setLocked("EMploymentDetails_Frame1", true);
			//++Below code added by nikhil 6/11/17
			//loadPicklistEmployment();
			loadPicklist4();
			Fields_ApplicationType_Employment();
			//--Above code added by nikhil 6/11/17
			//loadPicklist4();
			//++below code added by abhishek for CAD point 3 on 7/11/2017
			//Deepak 10 Aug 2019 - PCAS-2484
			formObject.setLocked("cmplx_EmploymentDetails_CurrEmployer",true);
			//done by sagarika for CR
			/*String emp_CurrEmp =formObject.getNGValue("cmplx_EmploymentDetails_CurrEmployer");
			if("CN".equalsIgnoreCase(emp_CurrEmp))
			{

				formObject.setEnabled("cmplx_EmploymentDetails_Indus_Micro",true);
				formObject.setEnabled("cmplx_EmploymentDetails_Indus_Macro",true);
			}*/
			//Done by aman as per verification sheet 17/12
			//done by sagarika for PCSP-207
			if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP")))
			{
				CreditCard.mLogger.info("inside NEP if condition");
				formObject.setLocked("EMploymentDetails_Label25", false);
				formObject.setLocked("cmplx_EmploymentDetails_NepType", false);
			}
			else{
				CreditCard.mLogger.info("inside NEP else condition");
				formObject.setLocked("EMploymentDetails_Label25", true);
				formObject.setLocked("cmplx_EmploymentDetails_NepType", true);
			}
			
			//formObject.setLocked("cmplx_EmploymentDetails_NepType", true);
			formObject.setLocked("cmplx_EmploymentDetails_FieldVisitDone",false);
			//Done by aman as per verification sheet 17/12

			//++above code added by abhishek for CAD point 3 on 7/11/2017
			//++Below code added by nikhil 8/11/17 as per CC issues sheet
			//change by saurabh on 30th Nov
			/*formObject.setVisible("EMploymentDetails_Label32", true);
			formObject.setVisible("cmplx_EmploymentDetails_FieldVisitDone",true);*/

			//--Above code added by nikhil 8/11/17 as per CC issues sheet
			if("IM".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2)))
			{
				formObject.setVisible("cmplx_EmploymentDetails_empmovemnt", true);
				formObject.setVisible("EMploymentDetails_Label15", true);
				formObject.setVisible("EMploymentDetails_Label59", false);//Added by Rajan
				formObject.setVisible("cmplx_EmploymentDetails_IndusSeg", false);// Added by Rajan
				//changed by shivang for PCASP-1511
				formObject.setLocked("cmplx_EmploymentDetails_EmployerType", true); // Added by Rajan
	
				formObject.setLocked("cmplx_EmploymentDetails_Indus_Macro",true);
				formObject.setLocked("cmplx_EmploymentDetails_Indus_Micro",true);
				//formObject.setEnabled("cmplx_EmploymentDetails_EmployerType", true);// Added by Rajan
				//formObject.setLocked("cmplx_EmploymentDetails_EmployerType", true);// Added by Rajan
				//changed by shivang for PCASP-1533
				/*if(formObject.getNGValue("cmplx_EmploymentDetails_IncInCC").equalsIgnoreCase("false") && formObject.getNGValue("cmplx_EmploymentDetails_IncInPL").equalsIgnoreCase("false")){
					formObject.setVisible("EMploymentDetails_Label41", true);
					formObject.setVisible("cmplx_EmploymentDetails_LengthOfBusiness", true);
					formObject.setLocked("cmplx_EmploymentDetails_LengthOfBusiness", true);
				}else{
					formObject.setVisible("EMploymentDetails_Label41", false);
					formObject.setVisible("cmplx_EmploymentDetails_LengthOfBusiness", false);
					
				}*/

			}
			else
			{
				formObject.setVisible("cmplx_EmploymentDetails_empmovemnt", false);
				formObject.setVisible("EMploymentDetails_Label15", false);
			}
			//Added by shivang for PCASP-1823
			CreditCard.mLogger.info("@Shivang ::Employment type :: "+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6));
			String subProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2);
			if(("Salaried".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6))||"SAL".equals(subProd)) && 
					!subProd.equalsIgnoreCase("SEC") && !subProd.equalsIgnoreCase("BPA") && !subProd.equalsIgnoreCase("PA") && !subProd.equalsIgnoreCase("IM")){
				if((formObject.getNGValue("cmplx_EmploymentDetails_IncInCC").equalsIgnoreCase("false") && formObject.getNGValue("cmplx_EmploymentDetails_IncInPL").equalsIgnoreCase("false"))
						|| (formObject.getNGValue("cmplx_EmploymentDetails_CurrEmployer").equalsIgnoreCase("CN")
								&& (formObject.getNGValue("cmplx_EmploymentDetails_EmpStatusPL").equalsIgnoreCase("OP")||formObject.getNGValue("cmplx_EmploymentDetails_EmpStatusPL").equalsIgnoreCase("RS"))
								&& (formObject.getNGValue("cmplx_EmploymentDetails_EmpStatusCC").equalsIgnoreCase("CN")||formObject.getNGValue("cmplx_EmploymentDetails_EmpStatusCC").equalsIgnoreCase("")||formObject.getNGValue("cmplx_EmploymentDetails_EmpStatusCC").equalsIgnoreCase("--Select--")))
							||(subProd.equalsIgnoreCase("SAL") && formObject.getNGValue("cmplx_EmploymentDetails_CurrEmployer").equalsIgnoreCase("CN") && 
								((formObject.getNGValue("cmplx_EmploymentDetails_EmpStatusPL").equalsIgnoreCase("NM")||formObject.getNGValue("cmplx_EmploymentDetails_EmpStatusPL").equalsIgnoreCase("CN")||formObject.getNGValue("cmplx_EmploymentDetails_EmpStatusPL").equalsIgnoreCase("")) 
								&& (formObject.getNGValue("cmplx_EmploymentDetails_EmpStatusCC").equalsIgnoreCase("NM")||formObject.getNGValue("cmplx_EmploymentDetails_EmpStatusCC").equalsIgnoreCase("CN")||formObject.getNGValue("cmplx_EmploymentDetails_EmpStatusCC").equalsIgnoreCase("")||formObject.getNGValue("cmplx_EmploymentDetails_EmpStatusCC").equalsIgnoreCase("--Select--"))))){
					
					CreditCard.mLogger.info("@Shivang ::Employment type :: "+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6));
					formObject.setVisible("EMploymentDetails_Label41", true);
					formObject.setVisible("cmplx_EmploymentDetails_LengthOfBusiness", true);
					formObject.setLocked("cmplx_EmploymentDetails_LengthOfBusiness", false);
				}else{
					formObject.setVisible("EMploymentDetails_Label41", true);
					formObject.setVisible("cmplx_EmploymentDetails_LengthOfBusiness", true);
					formObject.setNGValue("cmplx_EmploymentDetails_LengthOfBusiness", "");
					formObject.setLocked("cmplx_EmploymentDetails_LengthOfBusiness", true);
				}
			}
			if(subProd.equalsIgnoreCase("IM")){
				if(formObject.getNGValue("cmplx_EmploymentDetails_IncInCC").equalsIgnoreCase("false") && formObject.getNGValue("cmplx_EmploymentDetails_IncInPL").equalsIgnoreCase("false"))
					{					
					CreditCard.mLogger.info("@Shivang ::Employment type :: "+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6));
					formObject.setVisible("EMploymentDetails_Label41", true);
					formObject.setVisible("cmplx_EmploymentDetails_LengthOfBusiness", true);
					formObject.setLocked("cmplx_EmploymentDetails_LengthOfBusiness", true);
				}else{
					formObject.setVisible("EMploymentDetails_Label41", false);
					formObject.setVisible("cmplx_EmploymentDetails_LengthOfBusiness", false);
				}
			}
			String appCategory = formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg");
			String targetSeg = formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode");
			if(appCategory!=null &&  "S".equalsIgnoreCase(appCategory)){
				if("EKTMC-EXPAT".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 5))||"EKTMC-UAE".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 5))||"EKWEC-EXPAT".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 5))||"EKWEC-UAE".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 5))||"EKWEI-EXPAT".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 5)))
				{
					CreditCard.mLogger.info("sagarika_TargetSegmentCode()");
					String subproduct = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2);
					if(targetSeg!=null && targetSeg.equalsIgnoreCase("AMS"))
					{
						formObject.setVisible("EMploymentDetails_Label63", true);
						formObject.setVisible("cmplx_EmploymentDetails_tierno", true);
					}
					else{
						formObject.setNGValue("cmplx_EmploymentDetails_tierno", "");
						formObject.setVisible("EMploymentDetails_Label63", false);
						formObject.setVisible("cmplx_EmploymentDetails_tierno", false);
					}
					LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  description,code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and Surrogate='Y' and (product = 'CDOB') order by code");
					
				}
			}
			formObject.setLocked("cmplx_EmploymentDetails_Indus_Macro",true);
			formObject.setLocked("cmplx_EmploymentDetails_Indus_Micro",true);
			formObject.setLocked("cmplx_EmploymentDetails_DOJ",false);
			if("BPA".equalsIgnoreCase(formObject.getNGValue("Sub_Product")))
				{
			          formObject.setLocked("cmplx_EmploymentDetails_LOSPrevious",true);//PCASP-2479
			          formObject.setLocked("cmplx_EmploymentDetails_targetSegCode", true);//for PCASP-2627
			          formObject.setLocked("cmplx_EmploymentDetails_ApplicationCateg", true);//for PCASP-2627
				}
			if("PA".equalsIgnoreCase(formObject.getNGValue("Sub_Product")))
			{
				 formObject.setLocked("cmplx_EmploymentDetails_targetSegCode", true);//for PCASP-2627
		          formObject.setLocked("cmplx_EmploymentDetails_ApplicationCateg", true);//for PCASP-2627
			}
			formObject.setLocked("cmplx_EmploymentDetails_payroll_flag", true);

		}
		else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {
			//added 6th sept for proc 1926
			//fetchFinacleCoreFrag();
			if(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("false"))
			{

				formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalLimit", true);//total final limit
				//formObject.setLocked("ELigibiltyAndProductInfo_Button1", true);//calculate re-eligibility
			}
			//ended 6th sept for proc 1926

			formObject.setLocked("cmplx_EligibilityAndProductInfo_MaturityDate", true);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_NetRate", true);
			//Changes done by Shivang for PCASP-1536,1537,1538
			formObject.setLocked("cmplx_EligibilityAndProductInfo_InterestRate", true);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_EMI", true);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_Tenor", true);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_RepayFreq", true);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_FinalInterestRate", true);
			
			LoadPickList("cmplx_EligibilityAndProductInfo_RepayFreq", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_frequency with (nolock) order by code");

			LoadPickList("cmplx_EligibilityAndProductInfo_InterestType", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_InterestType with (nolock) order by code"); //Arun (17/10)

			//added by akshay on 22/12/17 for fetching card details
			if(!formObject.isVisible("CardDetails_Frame1")){
				fetch_CardDetails_frag(formObject);
				//change by Saurabh for PCSP-197. 
				adjustFrameTops("Card_Details,FATCA,KYC,OECD,Reference_Details");
				/*formObject.setTop("Card_Details", formObject.getTop("Reference_Details")+20);
				formObject.setTop("FATCA", formObject.getTop("Card_Details")+formObject.getHeight("Card_Details")+30);
				formObject.setTop("KYC", formObject.getTop("FATCA")+30);
				formObject.setTop("OECD", formObject.getTop("KYC")+30);*/

			}
			formObject.setLocked("cmplx_EligibilityAndProductInfo_InterestType", true);
			formObject.setNGValue("cmplx_EligibilityAndProductInfo_InterestType", "Equated");
			if("IM".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2)))
			{
				formObject.setVisible("ELigibiltyAndProductInfo_Label9", true);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_Max_threshold_exposure", true);
			}
		}
		else if ("AddressDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			loadPicklist_Address();
		//	formObject.setLocked("AddressDetails_Frame1",true);
			LoadView(pEvent.getSource().getName());

		}
		else if ("AltContactDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			LoadPickList("AlternateContactDetails_CustdomBranch", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock) order by code");
			//change by saurabh on 7th Dec
			//	LoadPickList("AlternateContactDetails_CardDisp", "select '--Select--' as description,'' as code union all select convert(varchar,description),code from NG_MASTER_Dispatch with (nolock) order by code ");// Load picklist added by aman to load the picklist in card dispatch to
			//change by saurabh for air arabia functionality.
			AirArabiaValid();
			enrollrewardvalid();//added by saurabh1 for enroll
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
			formObject.setLocked("FinacleCore_DDSClearing",false);//sagrika for PCAS-2953
			formObject.setLocked("FinacleCore_ReturnDate",false);
			formObject.setLocked("InwardTT_date",false);
			formObject.setNGValue("cmplx_FinacleCore_avg_credit_6month", "-");
			formObject.setNGValue("cmplx_FinacleCore_total_avg_last_16", "-");
			//InwardTT_date
			//loadPicklist_Address();
			//formObject.setLocked("FinacleCore_Frame1",true);
			formObject.setTop("FinacleCore_save", 3150);
			if("SEC".equalsIgnoreCase(formObject.getNGValue("Sub_Product")) || ("BTC".equalsIgnoreCase(formObject.getNGValue("Sub_Product")) && "SMES".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,4))))
					{
				formObject.setVisible("FinacleCore_Label31", true);
				formObject.setVisible("cmplx_FinacleCore_Account_FD", true);
				//formObject.setVisible("FinacleCore_Cal_FD_Amount", true);
				formObject.setVisible("FinacleCore_Label27", true);
				formObject.setVisible("cmplx_FinacleCore_Final_FD_Amount", true);
				String Lien_Grid = "cmplx_FinacleCore_liendet_grid";
				for(int i=0;i<formObject.getLVWRowCount(Lien_Grid);i++)
				{
					if("true".equals(formObject.getNGValue(Lien_Grid,i,7)))
					{
						formObject.setNGValue("cmplx_FinacleCore_Account_FD", formObject.getNGValue(Lien_Grid,i,0));
						formObject.setNGValue("cmplx_FinacleCore_Final_FD_Amount", formObject.getNGValue(Lien_Grid,i,2));
						break;
					}
				}
						}
			if("PA".equals(formObject.getNGValue("Sub_Product")) && ("RELT".equals(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,4 )) || "RELTN".equals(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,4 )))) 
			{
				formObject.setVisible("FinacleCore_Frame13",true);
			}
			else
			{
				formObject.setVisible("FinacleCore_Frame13",false);
			}
					
		}
		else if ("MOL1".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			loadPicklist_Mol();
			formObject.setLocked("MOL1_Frame1",true);
			//below code added by nikhil 10/1/17
			formObject.setLocked("cmplx_MOL_molexp",true);
			formObject.setEnabled("cmplx_MOL_molissue",false);
			formObject.setLocked("cmplx_MOL_ctrctstart",true);
			formObject.setLocked("cmplx_MOL_ctrctend",true);

		}
		else if ("WorldCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			formObject.setLocked("WorldCheck1_Frame1",true);
			formObject.setLocked("WorldCheck1_Dob",true);
			formObject.setLocked("WorldCheck1_entdate",true);
			formObject.setLocked("WorldCheck1_upddate",true);

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
			formObject.setLocked("SmartCheck_Modify",true);
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
			//Added by Shivang for PCASP-1390
			formObject.setLocked("RejectEnq_Save",true);

		}
		else if ("SalaryEnq".equalsIgnoreCase(pEvent.getSource().getName())) {
			//loadPicklist_Address();
			//formObject.setLocked("SalaryEnq_Frame1",true);
			//Added by Shivang for PCASP-1390
			formObject.setLocked("SalaryEnq_Button1",true);

		}
		/*if (pEvent.getSource().getName().equalsIgnoreCase("External_Blacklist")) {
					//loadPicklist_Address();
					formObject.setLocked("ExternalBlackList_Frame1",true);

				}*/
		/*if (pEvent.getSource().getName().equalsIgnoreCase("IncomingDocument")) {
					//loadPicklist_Address();
					formObject.setLocked("IncomingDocument_Frame",true);

				}*/
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
		/*else if ("CustDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("CustDetailVerification_Frame1", true);
			formObject.setLocked("cmplx_CustDetailVerification_mobno1_ver", true);
			formObject.setLocked("cmplx_CustDetailVerification_mobno2_ver", true);
			formObject.setLocked("cmplx_CustDetailVerification_dob_verification", true);
			formObject.setLocked("cmplx_CustDetailVerification_POBoxno_ver", true);
			formObject.setLocked("cmplx_CustDetailVerification_emirates_ver", true);
			formObject.setLocked("cmplx_CustDetailVerification_persorcompPOBox_ver", true);
			formObject.setLocked("cmplx_CustDetailVerification_resno_ver", true);
			formObject.setLocked("cmplx_CustDetailVerification_offtelno_ver", true);
			formObject.setLocked("cmplx_CustDetailVerification_hcountrytelno_ver", true);
			formObject.setLocked("cmplx_CustDetailVerification_hcontryaddr_ver", true);
			formObject.setLocked("cmplx_CustDetailVerification_email1_ver", true);
			formObject.setLocked("cmplx_CustDetailVerification_email2_ver", true);
			formObject.setLocked("cmplx_CustDetailVerification_Decision", true);
			formObject.setLocked("cmplx_CustDetailVerification_dob_upd", true);
		}
		//ended by yash
		 */
		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			//below line commented by Deepak as the same is called form fram expand method.
			//loadInDecGrid();
			LoadReferGrid();
			if(!formObject.isVisible("ExtLiability_Frame1")){
				formObject.fetchFragment("Internal_External_Liability", "Liability_New", "q_Liabilities");
			}

			//changed by akshay on 6/12/17 for decision alignment
			Decision_cadanalyst1();
			formObject.setEnabled("cmplx_DEC_Manual_Deviation", true);
			//for decision fragment made changes 8th dec 2017
			//changes done by shivang start PCASP-2003-reverting changes as this is wrong jira.
			//fragment_ALign("DecisionHistory_Frame2#\n#DecisionHistory_Label33,cmplx_DEC_TotalOutstanding#DecisionHistory_Label32,cmplx_DEC_TotalEMI#\n#DecisionHistory_Frame3#\n#DecisionHistory_Label17,cmplx_DEC_LoanApprovalAuth#DecisionHistory_Button4#\n#DecisionHistory_Decision_Label3,cmplx_DEC_Decision#DecisionHistory_Label26,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_dec_reason_code#DecisionHistory_Label14,cmplx_DEC_DectechDecision#\n#DecisionHistory_Label1,DecisionHistory_NewStrength#DecisionHistory_AddStrength#DecisionHistory_Label3,cmplx_DEC_Strength#\n#DecisionHistory_Label34,DecisionHistory_NewWeakness#DecisionHistory_AddWeakness#DecisionHistory_Label4,cmplx_DEC_Weakness#\n#DecisionHistory_Decision_Label4,cmplx_DEC_Remarks#DecisionHistory_Label42,cmplx_DEC_SetReminder_CA#DecisionHistory_Label43,cmplx_DEC_NoOfAttempts_CA#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#DecisionHistory_Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line
			fragment_ALign("DecisionHistory_Decision_Label1,cmplx_DEC_VerificationRequired#DecisionHistory_Button4#DecisionHistory_CifLock#DecisionHistory_CifUnlock#\n#DecisionHistory_Decision_Label3,cmplx_DEC_Decision#DecisionHistory_Label26,DecisionHistory_ReferTo#DecisionHistory_Label11,DecisionHistory_dec_reason_code#DecisionHistory_Label13,cmplx_DEC_DeviationCode#DecisionHistory_Label14,cmplx_DEC_DectechDecision#DecisionHistory_Label1,DecisionHistory_NewStrength#DecisionHistory_AddStrength#DecisionHistory_Label3,cmplx_DEC_Strength#\n#DecisionHistory_Label34,DecisionHistory_NewWeakness#DecisionHistory_AddWeakness#DecisionHistory_Label4,cmplx_DEC_Weakness#DecisionHistory_Decision_Label4,cmplx_DEC_Remarks#DecisionHistory_Label15,cmplx_DEC_ScoreGrade#DecisionHistory_Label16,cmplx_DEC_HighDeligatinAuth#DecisionHistory_Label18,cmplx_DEC_ReferTo#DecisionHistory_calReElig#\n#cmplx_DEC_Manual_Deviation#DecisionHistory_ManualDevReason#DecisionHistory_Label42,cmplx_DEC_SetReminder_CA#DecisionHistory_Label43,cmplx_DEC_NoOfAttempts_CA#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#DecisionHistory_Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");
			formObject.setWidth("DecisionHistory_dec_reason_code", 210);
			formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
			formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
			
			/*int rowsExposure = formObject.getLVWRowCount("cmplx_DEC_cmplx_gr_ExpoDetails");
            //int rowsDeviation = formObject.getLVWRowCount("cmplx_DEC_cmplx_gr_DeviaitonDetails");
            if(rowsExposure!=0){
            formObject.clear("cmplx_DEC_cmplx_gr_ExpoDetails");
            }
            setExposureGridData();
            if(rowsDeviation!=0){
            formObject.clear("cmplx_DEC_cmplx_gr_DeviaitonDetails");
            }*/
          //changes done by shivang start PCASP-2003
			//for decision fragment made changes 8th dec 2017
			CreditCard.mLogger.info( "value of manual deviation is:" + formObject.getNGValue("cmplx_DEC_Manual_Deviation"));
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
			formObject.setLocked("cmplx_DEC_SetReminder_CA",true);
			formObject.setLocked("cmplx_DEC_NoOfAttempts_CA",true);
			formObject.setVisible("DecisionHistory_CifUnlock",false); // Added by Rajan for PCASP-1555
			formObject.setVisible("DecisionHistory_CifLock", false);// Added by Rajan for PCASP-1554
			formObject.setTop("Finacle_CRM_Incidents", formObject.getTop("Part_Match")+formObject.getHeight("Part_Match")+50);
			formObject.setTop("External_Blacklist", formObject.getTop("Finacle_CRM_Incidents")+formObject.getHeight("Finacle_CRM_Incidents")+50);
			formObject.setTop("MOL", formObject.getTop("External_Blacklist")+formObject.getHeight("External_Blacklist")+30);
			if(!("SEC".equalsIgnoreCase(formObject.getNGValue("Sub_Product")) || ("BTC".equalsIgnoreCase(formObject.getNGValue("Sub_Product")) && "SMES".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,4))) || "BPA".equalsIgnoreCase(formObject.getNGValue("Sub_Product"))
					 || "SAL".equalsIgnoreCase(formObject.getNGValue("Sub_Product"))))
			{
			formObject.setTop("BussinessVerification", formObject.getTop("Customer_Details_Verification")+formObject.getHeight("Customer_Details_Verification")+30);
			formObject.setTop("Smart_Check", formObject.getTop("BussinessVerification")+formObject.getHeight("BussinessVerification")+30);
			
		//	adjustFrameTops("Customer_Details_Verification,BussinessVerification,Smart_Check");
			int framestate4=formObject.getNGFrameState("Business_Verification");//758
			if(framestate4 !=0){
				formObject.fetchFragment("Business_Verification", "BussinessVerification", "q_BussVerification");
			    formObject.setNGFrameState("Business_Verification", 1);

			}
			formObject.setTop("Business_Verification",1200);	
			int framestate3=formObject.getNGFrameState("Smart_Check");
			if(framestate3 !=0){
				formObject.fetchFragment("Smart_Check", "Smart_Check", "Smart_Check");
			    formObject.setNGFrameState("Smart_Check", 1);

			}
			formObject.setTop("Smart_Check",1400);	
			}
			else if( "SAL".equalsIgnoreCase(formObject.getNGValue("Sub_Product"))){
				
				int framestate4=formObject.getNGFrameState("Office_Mob_Verification");//758
				if(framestate4 !=0){
					formObject.fetchFragment("Office_Mob_Verification", "OfficeandMobileVerification", "q_OffVerification");
				    formObject.setNGFrameState("Office_Mob_Verification", 1);

				}
				formObject.setTop("Office_Mob_Verification",1200);	
				int framestate3=formObject.getNGFrameState("Smart_Check");
				if(framestate3 !=0){
					formObject.fetchFragment("Smart_Check", "Smart_Check", "Smart_Check");
				    formObject.setNGFrameState("Smart_Check", 1);

				}
				formObject.setTop("Smart_Check",1300);	
			}
			if( "IM".equalsIgnoreCase(formObject.getNGValue("Sub_Product"))){
				formObject.setVisible("Office_Mob_Verification",false);	
				formObject.setVisible("Business_Verification",false);
				
			}
		}
		//below code added by nikhil CPV_REQUIRED
		else if ("CustDetailVerification".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			//code changed by nikhil for CPV Changes 16-04-2019
			//formObject.setLocked("CustDetailVerification_Frame1",true);
			//formObject.setEnabled("CustDetailVerification_Frame1", false);
			//done by nikhil for PCAS-2358
			LoadPickList("cmplx_CustDetailVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");
			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_CustDetailVerification_mobno1_ver","cmplx_CustDetailVerification_mobno2_ver","cmplx_CustDetailVerification_dob_verification","cmplx_CustDetailVerification_POBoxno_ver","cmplx_CustDetailVerification_emirates_ver","cmplx_CustDetailVerification_persorcompPOBox_ver","cmplx_CustDetailVerification_resno_ver","cmplx_CustDetailVerification_offtelno_ver","cmplx_CustDetailVerification_hcountrytelno_ver","cmplx_CustDetailVerification_hcontryaddr_ver","cmplx_CustDetailVerification_email1_ver","cmplx_CustDetailVerification_email2_ver","cmplx_CustDetailVerification_Mother_name_ver");
			LoadPickList("cmplx_CustDetailVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");
			LoadPicklistVerification(LoadPicklist_Verification);
			LoadPickList("cmplx_CustDetailVerification_emirates_upd","select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_emirate with (nolock) order by Code");//PCAS-2509
			enable_custVerification();
			formObject.setVisible("CustDetailVerification_Label10", false);//Added by Rajan
            formObject.setVisible("cmplx_CustDetailVerification_hcountryaddr_val", false);//Added by Rajan
            formObject.setVisible("cmplx_CustDetailVerification_hcontryaddr_ver", false);//Added by Rajan
            formObject.setVisible("cmplx_CustDetailVerification_hcountryaddr_upd", false);  //Added by Rajan 
			
			//LoadPickList("cmplx_CustDetailVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");
			/*List<String> LoadPicklist_Verification= Arrays.asList("cmplx_CustDetailVerification_mobno1_ver","cmplx_CustDetailVerification_mobno2_ver","cmplx_CustDetailVerification_dob_verification","cmplx_CustDetailVerification_POBoxno_ver","cmplx_CustDetailVerification_emirates_ver","cmplx_CustDetailVerification_persorcompPOBox_ver","cmplx_CustDetailVerification_resno_ver","cmplx_CustDetailVerification_offtelno_ver","cmplx_CustDetailVerification_hcountrytelno_ver","cmplx_CustDetailVerification_hcontryaddr_ver","cmplx_CustDetailVerification_email1_ver","cmplx_CustDetailVerification_email2_ver");
			LoadPicklistVerification(LoadPicklist_Verification);*/
			//LoadPickList("cmplx_CustDetailVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");*/	
			CreditCard.mLogger.info("sagarika Y"+formObject.getNGValue("CPV_WAVIER"));
			if("Y".equalsIgnoreCase(formObject.getNGValue("CPV_WAVIER")))
			{
			CreditCard.mLogger.info("sagarika Y");
			formObject.setNGValue("cmplx_CustDetailVerification_Decision","Not Applicable");
			}
			disableforNA();
			if( ("PA".equalsIgnoreCase(formObject.getNGValue("Sub_Product"))) || ("BPA".equalsIgnoreCase(formObject.getNGValue("Sub_Product")) && "Self Employed".equalsIgnoreCase(formObject.getNGValue("EmploymentType")) && ( "ALBUN".equalsIgnoreCase(formObject.getNGValue("Application_Type")) ||  "MLBUN".equalsIgnoreCase(formObject.getNGValue("Application_Type")))) )
			{
				formObject.setNGValue("cmplx_CustDetailVerification_Decision","Not Applicable");
				formObject.setLocked("CustDetailVerification_Frame1",true);
			}
			//sagarika for PCAS-2577
			if("IM".equalsIgnoreCase(formObject.getNGValue("Sub_Product")))
			{
			formObject.setVisible("CustDetailVerification_Label7", false);// Added by Rajan
			formObject.setVisible("cmplx_CustDetailVerification_persorcompPOBox_val",false);//Added by Rajan
			formObject.setVisible("cmplx_CustDetailVerification_persorcompPOBox_ver", false);//Added by Rajan
			formObject.setVisible("cmplx_CustDetailVerification_persorcompPOBox_upd", false);//Added by Rajan
			formObject.setVisible("CustDetailVerification_Label8", false);//Added by Rajan
			formObject.setVisible("cmplx_CustDetailVerification_Offtelno_val", false);//Added by Rajan
			formObject.setVisible("cmplx_CustDetailVerification_offtelno_ver", false);//Added by Rajan
			formObject.setVisible("cmplx_CustDetailVerification_offtelno_upd", false);//Added by Rajan
			formObject.setVisible("CustDetailVerification_Label19", true);
			formObject.setVisible("cmplx_CustDetailVerification_mother_name_val", true);
			formObject.setVisible("cmplx_CustDetailVerification_mother_name_ver", true);
			formObject.setVisible("cmplx_CustDetailVerification_mother_name_upd", true);
			formObject.setVisible("CustDetailVerification_Label12", false);//Added by Rajan
			formObject.setVisible("cmplx_CustDetailVerification_email2_val", false);//Added by Rajan
			formObject.setVisible("cmplx_CustDetailVerification_email2_ver", false);//Added by Rajan
			formObject.setVisible("cmplx_CustDetailVerification_email2_upd", false);//Added by Rajan
			  				
			}
			
			if("SE".equalsIgnoreCase(formObject.getNGValue("Sub_Product"))||"BTC".equalsIgnoreCase(formObject.getNGValue("Sub_Product"))||"LI".equalsIgnoreCase(formObject.getNGValue("Sub_Product"))||"PU".equalsIgnoreCase(formObject.getNGValue("Sub_Product"))||"PULI".equalsIgnoreCase(formObject.getNGValue("Sub_Product"))||"SEC".equalsIgnoreCase(formObject.getNGValue("Sub_Product")))
			{
				formObject.setVisible("CustDetailVerification_Label3",false);//Added by Rajan
				formObject.setVisible("cmplx_CustDetailVerification_Mob_No2_val", false);//Added by Rajan
				formObject.setVisible("cmplx_CustDetailVerification_mobno2_ver", false);//Added by Rajan
				formObject.setVisible("cmplx_CustDetailVerification_mobno2_upd", false);//Added by Rajan
				formObject.setVisible("CustDetailVerification_Label12", false);//Added by Rajan
				formObject.setVisible("cmplx_CustDetailVerification_email2_val", false);//Added by Rajan
				formObject.setVisible("cmplx_CustDetailVerification_email2_ver", false);//Added by Rajan
				formObject.setVisible("cmplx_CustDetailVerification_email2_upd", false);//Added by Rajan
				formObject.setVisible("CustDetailVerification_Label17", false);//Added by Rajan
                formObject.setVisible("cmplx_CustDetailVerification_Resno_val", false);//Added by Rajan
                formObject.setVisible("cmplx_CustDetailVerification_resno_ver", false);//Added by Rajan
                formObject.setVisible("cmplx_CustDetailVerification_resno_upd", false);//Added by Rajan
                formObject.setVisible("CustDetailVerification_Label9", false);//Added by Rajan
                formObject.setVisible("cmplx_CustDetailVerification_hcountrytelno_val", false);//Added by Rajan
                formObject.setVisible("cmplx_CustDetailVerification_hcountrytelno_ver", false);//Added by Rajan
                formObject.setVisible("cmplx_CustDetailVerification_hcountrytelno_upd", false);//Added by Rajan
                formObject.setVisible("CustDetailVerification_Label10", false);//Added by Rajan
                formObject.setVisible("cmplx_CustDetailVerification_hcountryaddr_val", false);//Added by Rajan
                formObject.setVisible("cmplx_CustDetailVerification_hcontryaddr_ver", false);//Added by Rajan
                formObject.setVisible("cmplx_CustDetailVerification_hcountryaddr_upd", false);   //Added by Rajan        
                
			}
			//2481 jira
			formObject.setNGValue("cmplx_CustDetailVerification_POBoxNo_val", "NA");
			formObject.setNGValue("cmplx_CustDetailVerification_emirates_val", "NA");
			formObject.setNGValue("cmplx_CustDetailVerification_persorcompPOBox_val", "NA");
			if("SE".equalsIgnoreCase(formObject.getNGValue("Sub_Product")) ||"SAL".equalsIgnoreCase(formObject.getNGValue("Sub_Product"))|| "BTC".equalsIgnoreCase(formObject.getNGValue("Sub_Product")))
			{
				formObject.setNGValue("cmplx_CustDetailVerification_POBoxno_ver", "NA");// Added by Rajan
				formObject.setNGValue("cmplx_CustDetailVerification_persorcompPOBox_ver", "NA");// Added by Rajan
				formObject.setNGValue("cmplx_CustDetailVerification_emirates_ver", "NA");// Added by Rajan
}
			else
			{
				formObject.setNGValue("cmplx_CustDetailVerification_POBoxno_ver", "--Select--");// Added by Rajan
				formObject.setNGValue("cmplx_CustDetailVerification_persorcompPOBox_ver", "--Select--");// Added by Rajan
				formObject.setNGValue("cmplx_CustDetailVerification_emirates_ver", "--Select--");// Added by Rajan
			}
		}

		else if ("BussinessVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("BussinessVerification_Frame1",true);
			enable_busiVerification();
			if(("BTC".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2))))// Added by Rajan
			{
				formObject.setVisible("cmplx_BussVerification_Office_Extension", false);// Added by Rajan
				formObject.setVisible("BussinessVerification_Label5", false); // Added by Rajan
			}	

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
			
			//nikhil code moved from frame expand event for PCAS-2239
			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_OffVerification_fxdsal_ver","cmplx_OffVerification_accpvded_ver","cmplx_OffVerification_desig_ver","cmplx_OffVerification_doj_ver","cmplx_OffVerification_cnfminjob_ver");
			LoadPicklistVerification(LoadPicklist_Verification);
			LoadPickList("cmplx_OffVerification_colleaguenoverified", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_CPVVeri with (nolock) where Sno != 3 order by code"); //Arun modified on 14/12/17
			//changed by nikhil for CPV changes 17-04
			LoadPickList("cmplx_OffVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) where IsActive='Y' and For_custdetail_only='N' order by code");
			//below code by saurabh on 28th nov 2017.
			LoadPickList("cmplx_OffVerification_offtelnovalidtdfrom", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_offNoValidatedFrom with (nolock)");
			LoadPickList("cmplx_OffVerification_hrdnocntctd", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_HRDContacted with (nolock)");
			LoadPickList("cmplx_OffVerification_desig_upd", "select '--Select--'  as description,'' as code  union select convert(varchar, description),code from NG_MASTER_designation with (nolock) order by code");
			//formObject.setEnabled("OfficeandMobileVerification_Frame1",false);
			formObject.setLocked("OfficeandMobileVerification_Frame1",true);
			CreditCard.mLogger.info( "set visible OfficeandMobileVerification inside condition ");
			
			formObject.setLocked("cmplx_OffVerification_doj_upd",true);
				if("SE".equalsIgnoreCase(formObject.getNGValue("Sub_Product")))
							{
			//Added by Rajan for SE CPV FLV
			formObject.setLocked("cmplx_OffVerification_fxdsal_override", true);
			formObject.setLocked("cmplx_OffVerification_accprovd_override", true);
			formObject.setLocked("cmplx_OffVerification_desig_override", true);
			formObject.setLocked("cmplx_OffVerification_doj_override", true);
			formObject.setLocked("cmplx_OffVerification_cnfrmjob_override", true);
			formObject.setVisible("OfficeandMobileVerification_Label12", false);
            formObject.setVisible("cmplx_OffVerification_offtelnovalidtdfrom", false);
            formObject.setVisible("OfficeandMobileVerification_Label2", false);
            formObject.setVisible("cmplx_OffVerification_hrdnocntctd", false);
            formObject.setVisible("OfficeandMobileVerification_Label7",false);
            formObject.setVisible("cmplx_OffVerification_hrdcntctno", false);
            formObject.setVisible("OfficeandMobileVerification_Label25", false);
            formObject.setVisible("cmplx_OffVerification_Office_Extension", false);
            formObject.setVisible("OfficeandMobileVerification_Label3", false);
            formObject.setVisible("cmplx_OffVerification_hrdcntctname", false);
            formObject.setVisible("OfficeandMobileVerification_Label13", false);
            formObject.setVisible("cmplx_OffVerification_hrdcntctdesig", false);
            formObject.setVisible("OfficeandMobileVerification_Label4", false);
            formObject.setVisible("cmplx_OffVerification_hrdemailverified", false);
            formObject.setVisible("OfficeandMobileVerification_Label8", false);
            formObject.setVisible("cmplx_OffVerification_hrdemailid", false);
            formObject.setVisible("OfficeandMobileVerification_Label5", false);
            formObject.setVisible("cmplx_OffVerification_colleaguenoverified", false);
            formObject.setVisible("OfficeandMobileVerification_Label9", false);
            formObject.setVisible("cmplx_OffVerification_colleagueno", false);
            formObject.setVisible("OfficeandMobileVerification_Label15", false);
            formObject.setVisible("OfficeandMobileVerification_Label21", false);
            formObject.setVisible("OfficeandMobileVerification_Label22", false);
            formObject.setVisible("OfficeandMobileVerification_Label23", false);
            formObject.setVisible("OfficeandMobileVerification_Label26", false);
            formObject.setVisible("OfficeandMobileVerification_Label16", false);
            formObject.setVisible("cmplx_OffVerification_fxdsal_val", false);
            formObject.setVisible("cmplx_OffVerification_fxdsal_ver", false);									
            formObject.setVisible("cmplx_OffVerification_fxdsal_upd", false);	
            formObject.setVisible("cmplx_OffVerification_fxdsal_override", false);	
            formObject.setVisible("OfficeandMobileVerification_Label17", false);	
            formObject.setVisible("cmplx_OffVerification_accprovd_val", false);	
            formObject.setVisible("cmplx_OffVerification_accprovd_ver", false);	
            formObject.setVisible("cmplx_OffVerification_accprovd_upd", false);	
            formObject.setVisible("cmplx_OffVerification_accprovd_override", false);	
            formObject.setVisible("OfficeandMobileVerification_Label18", false);	
            formObject.setVisible("cmplx_OffVerification_desig_val", false);	
            formObject.setVisible("cmplx_OffVerification_desig_ver", false);	
            formObject.setVisible("cmplx_OffVerification_desig_upd", false);
            formObject.setVisible("cmplx_OffVerification_OthDesign_Upt", false);
            formObject.setVisible("cmplx_OffVerification_desig_override", false);	
            formObject.setVisible("OfficeandMobileVerification_Label19", false);	
            formObject.setVisible("cmplx_OffVerification_doj_val", false);	
            formObject.setVisible("cmplx_OffVerification_doj_ver", false);
            formObject.setVisible("cmplx_OffVerification_doj_upd", false);
            formObject.setVisible("cmplx_OffVerification_doj_override", false);
            formObject.setVisible("OfficeandMobileVerification_Label20", false);
            formObject.setVisible("cmplx_OffVerification_cnfminjob_val", false);
            formObject.setVisible("cmplx_OffVerification_cnfminjob_ver", false);
            formObject.setVisible("cmplx_OffVerification_cnfminjob_upd", false);
            formObject.setVisible("cmplx_OffVerification_cnfminjob_override", false);
            formObject.setVisible("cmplx_OffVerification_accpvded_ver", false);
            formObject.setVisible("cmplx_OffVerification_accpvded_upd", false);
            formObject.setVisible("cmplx_OffVerification_cnfrminjob_upd", false);
            formObject.setVisible("cmplx_OffVerification_cnfrminjob_val", false);
            formObject.setVisible("cmplx_OffVerification_cnfrmjob_override", false);
		}
			//enable_officeVerification();
			// added by abhishek to disable office verification
			//formObject.setLocked("OfficeandMobileVerification_Frame1", true);
			//formObject.setEnabled("OfficeandMobileVerification_Enable", true);
			//-- Above code added by abhishek as per CC FSD 2.7.3
			//++Below code added by nikhil 13/11/2017 for Code merge
			//LoadPickList("cmplx_OffVerification_offtelnovalidtdfrom", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_offNoValidatedFrom with (nolock) order by code");
			//LoadPickList("cmplx_OffVerification_desig_upd", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Designation with (nolock) where isActive='Y' order by Code");

			//--Above code added by nikhil 13/11/2017 for Code merge
			if("Mismatch".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_fxdsal_ver")))
			{
				formObject.setLocked("cmplx_OffVerification_fxdsal_override", false);
				formObject.setLocked("OfficeandMobileVerification_Button1", false);
				formObject.setEnabled("OfficeandMobileVerification_Button1", true);
			}
			if("Mismatch".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_accpvded_ver")))
			{
				formObject.setLocked("cmplx_OffVerification_accprovd_override", false);
				formObject.setLocked("OfficeandMobileVerification_Button1", false);
				formObject.setEnabled("OfficeandMobileVerification_Button1", true);
			}
			if("Mismatch".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_desig_ver")))
			{
				formObject.setLocked("cmplx_OffVerification_desig_override", false);
				formObject.setLocked("OfficeandMobileVerification_Button1", false);
				formObject.setEnabled("OfficeandMobileVerification_Button1", true);
			}
			if("Mismatch".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_doj_ver")))
			{
				formObject.setLocked("cmplx_OffVerification_doj_override", false);
				formObject.setLocked("OfficeandMobileVerification_Button1", false);
				formObject.setEnabled("OfficeandMobileVerification_Button1", true);
			}
			if("Mismatch".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_cnfminjob_ver")))
			{
				formObject.setLocked("cmplx_OffVerification_cnfrmjob_override", false);
				formObject.setLocked("OfficeandMobileVerification_Button1", false);
				formObject.setEnabled("OfficeandMobileVerification_Button1", true);
			}
			
		
		}
		else if ("LoanandCard".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("LoanandCard_Frame1",true);
			enable_loanCard();

		}
		else if ("Fpu_Grid".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.setLocked("cmplx_FPU_Grid_Officer_Name", true);
			LoadPickList("cmplx_FPU_Grid_Officer_Name", "select UserName from PDBUser where UserIndex in (select UserIndex from PDBGroupMember where GroupIndex=(select GroupIndex from PDBGroup where GroupName='FPU'))");
		}
	}
	public void setExposureGridData() {

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String wi_name = formObject.getWFWorkitemName();
		float totalOut = 0;
		float totalEMI = 0;
		String query =	"select LoanType as Scheme,Liability_type,AgreementId as Agreement,isnull(TotalOutstandingAmt,isnull(OutstandingAmt,0)) as OutBal,isnull(PaymentsAmt,0) as EMI,Consider_For_Obligations  from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where Child_Wi = '"+wi_name+"' and LoanStat not in ('Pipeline','CAS-Pipeline','C') union select CardType as Scheme,Liability_type,CardEmbossNum as Agreement,isnull(TotalOutstandingAmt,isnull(OutstandingAmt,0)) as OutBal,isnull(PaymentsAmount,0) as EMI,Consider_For_Obligations  from ng_RLOS_CUSTEXPOSE_CardDetails with (nolock) where Child_Wi = '"+wi_name+"' and CardStatus not in ('Pipeline','CAS-Pipeline','C') and custroleType!='Secondary'  union select LoanType as Scheme,Liability_type,AgreementId as Agreement,isnull(OutstandingAmt,'0') as OutBal,isnull(PaymentsAmt,'0') as EMI,Consider_For_Obligations from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Child_Wi = '"+wi_name+"' and ProviderNo != 'B01' and LoanStat not in  ('Pipeline','Closed') union select CardType as Scheme,Liability_type,CardEmbossNum as Agreement,isnull(CurrentBalance,'0') as OutBal,isnull(PaymentsAmount,0) as EMI,Consider_For_Obligations from ng_rlos_cust_extexpo_CardDetails with (nolock) where Child_Wi = '"+wi_name+"' and ProviderNo != 'B01' and CardStatus not in  ('Pipeline','Closed') and custRoleType!='Secondary' union select (select Description from NG_MASTER_contract_type with (nolock) where code = TypeOfContract) as Scheme,'' as Liability_type,'' as Agreement,isnull(OutstandingAmt,0) as OutBal,isnull(cast(EMI as float),0),Consider_For_Obligations  from ng_rlos_gr_LiabilityAddition with (nolock) where LiabilityAddition_wiName = '"+wi_name+"' union select accttype as scheme,acctId,providerNo,isnull(outstandingbalance,0),isnull(paymentsamount,0),Consider_For_Obligations from ng_rlos_cust_extexpo_AccountDetails with (nolock) where  child_wi = '"+wi_name+"' and ProviderNo != 'B01' and AcctStat not in  ('Pipeline','Closed') union select 'Overdraft' as scheme,account_type,AcctId,isnull(sanctionlimit,0),0,consider_for_obligations from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where Child_Wi= '"+wi_name+"' and  ODType != '' and CustRoleType='Main'";
		//CreditCard.mLogger.info("Query for exposure grid is: "+query);
		List<List<String>> record = formObject.getDataFromDataSource(query);
		//CreditCard.mLogger.info("Query data is: "+record);

		if(record !=null && record.size()>0 && record.get(0)!=null)   //if(record !=null && record.size()>0 && record.get(0)!=null)
		{
			for(List<String> row:record){
				//CreditCard.mLogger.info("CFO received is: "+row.get(5));
				if( row.get(5) == null ||"null".equalsIgnoreCase(row.get(5))|| "true".equalsIgnoreCase(row.get(5)) || row.get(5).equalsIgnoreCase(null) ||"".equalsIgnoreCase(row.get(5))){
					//CreditCard.mLogger.info("row.get(5) is: "+row.get(5));
					row.set(5, "Yes");
					//CreditCard.mLogger.info("row.get(5) is: "+row.get(5));
				}
				else if("false".equalsIgnoreCase(row.get(5))){
					//CreditCard.mLogger.info("row.get(5) is: "+row.get(5));
					row.set(5, "No");
					//CreditCard.mLogger.info("row.get(5) is: "+row.get(5));
				}
				//CreditCard.mLogger.info("row.get(3) is: "+row.get(3));
				//CreditCard.mLogger.info("row.get(4) is: "+row.get(4));
				if(row.get(3)!=null && !row.get(3).equalsIgnoreCase("null") && !"".equals(row.get(3))){
				totalOut += Float.parseFloat(row.get(3));
				}
				if(row.get(4)!=null && !row.get(4).equalsIgnoreCase("null") && !"".equals(row.get(3))){
				totalEMI += Float.parseFloat(row.get(4));
				}
				row.add(formObject.getWFWorkitemName());
				formObject.addItemFromList("cmplx_DEC_cmplx_gr_ExpoDetails", row);
			}
		}
		/*if(formObject.isVisible("ExtLiability_Frame4")){
		if(formObject.getLVWRowCount("cmplx_Liability_New_cmplx_LiabilityAdditionGrid")>0){
		for(int i=0;i<formObject.getLVWRowCount("cmplx_Liability_New_cmplx_LiabilityAdditionGrid");i++){
			totalOut+=Float.parseFloat(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",i, 12));
			totalEMI+=Float.parseFloat(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",i, 2));
		}
		}
		}*/
		CreditCard.mLogger.info("Total Out is: "+String.valueOf(totalOut));
		CreditCard.mLogger.info("Total EMI is: "+String.valueOf(totalEMI));
		formObject.setNGValue("cmplx_DEC_TotalOutstanding","-");
		formObject.setNGValue("cmplx_DEC_TotalEMI", "-");
	
		
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
