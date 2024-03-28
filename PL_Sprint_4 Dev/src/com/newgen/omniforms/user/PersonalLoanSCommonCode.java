//----------------------------------------------------------------------------------------------------
//--Historty--
//Deepak Code changes to calculate LPF amount and % 08-nov-2017 start
//----------------------------------------------------------------------------------------------------


package com.newgen.omniforms.user;
//package com.newgen.generate;

import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
//import com.newgen.generate.XMLParser;
import javax.xml.parsers.ParserConfigurationException;
import java.text.NumberFormat;


//import ISPack.CPIDocumentTxn;
import ISPack.ISUtil.JPISException;
import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.newgen.custom.Common_Utils;
import com.newgen.custom.XMLParser;
import com.newgen.omni.wf.util.app.NGEjbClient;
import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.IRepeater;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.excp.CustomExceptionHandler;
import com.newgen.wfdesktop.xmlapi.WFCallBroker;
import com.newgen.wfdesktop.xmlapi.WFInputXml;

import ISPack.CPISDocumentTxn;
import ISPack.ISUtil.JPDBRecoverDocData;
import ISPack.ISUtil.JPISIsIndex;




public class PersonalLoanSCommonCode extends PLCommon implements Serializable{

	private static final long serialVersionUID = 1L;
	Common_Utils common=new Common_Utils(PersonalLoanS.mLogger);
	static String Str_NotepadDetails_noteDate;
	static String Str_NotepadDetails_user;
	static String Str_NotepadDetails_insqueue;
	static String ReferenceNo=""; 
	public static ArrayList<String> FircoGridSRNo = new ArrayList<String>();
	public static ArrayList<String> FircoGridOFACID = new ArrayList<String>();
	public static ArrayList<String> FircoGridName = new ArrayList<String>();
	public static ArrayList<String> FircoGridMatchingText = new ArrayList<String>();
	public static ArrayList<String> FircoGridOrigin = new ArrayList<String>();
	public static ArrayList<String> FircoGridDestination = new ArrayList<String>();
	public static ArrayList<String> FircoGridDOB = new ArrayList<String>();
	public static ArrayList<String> FircoGridUserData1 = new ArrayList<String>();
	public static ArrayList<String> FircoGridNationality = new ArrayList<String>();
	public static ArrayList<String> FircoGridPassport = new ArrayList<String>();
	public static ArrayList<String> FircoGridAdditionalInfo = new ArrayList<String>();
	public static ArrayList<String> FircoGridREFERENCENO = new ArrayList<String>();
	//Logger mLogger=PersonalLoanS.mLogger;

	/*          Function Header:

	 **********************************************************************************
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Description                         : Function to Expand fragments on click   

	 ***********************************************************************************  */
	public static String getFircoReferenceNumber(String workitemno)
	{
		if(!workitemno.equalsIgnoreCase(""))
		{
			workitemno = workitemno.split("-")[0]+"-"+workitemno.split("-")[1].replaceFirst("^0+(?!$)", "");
		}
		Timestamp localTimestamp = new Timestamp(System.currentTimeMillis());
		String date = Integer.toString(localTimestamp.getDate());
		if(date.length() == 1)
			date = "0"+date;
		
		int iMonth =localTimestamp.getMonth()+1;
		String month = Integer.toString(iMonth);
		if(month.length() == 1)
			month = "0"+month;
		
		int iYear = localTimestamp.getYear()+1900;
		String year = Integer.toString(iYear);

		String hour = Integer.toString(localTimestamp.getHours());
		if(hour.length() == 1)
			hour = "0"+hour;
		
		String minutes = Integer.toString(localTimestamp.getMinutes());
		if(minutes.length() == 1)
			minutes = "0"+minutes;
		
		String second = Integer.toString(localTimestamp.getSeconds());
		if(second.length() == 1)
			second = "0"+second;
		//String ReferenceNo=workitemno+"_"+System.currentTimeMillis()/1000*60;
		String ReferenceNo=workitemno+"-"+ date+month+year+hour+minutes+second;
		return ReferenceNo;	
	}
	public void FrameExpandEvent(ComponentEvent pEvent){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PersonalLoanS.mLogger.info("PersonalLoanS---Inside PLCommoncode-->FrameExpandEvent()");
		PersonalLoanS.mLogger.info("PersonalLoanS---Inside CUSTOME!!!!!!!!!!!!!!!!!!!!!!!");
		HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
		PersonalLoanS.mLogger.info("@@@@Shweta"+pEvent.getSource().getName());
		String activityName = formObject.getWFActivityName();


		//  added by Nishkarsh 24/05/2021
		if("PostDisbursal_MCQ_Sms".equalsIgnoreCase(pEvent.getSource().getName())){
			
			String email = "";

			String bankName = getBankFromTakeoverGrid(formObject);
			if("Conventional".equalsIgnoreCase(formObject.getNGValue("loan_type")))
			{
				PersonalLoanS.mLogger.info("Request is COnventional");
				email = NGFUserResourceMgr_PL.getAlert("MCQ_Email_Issuance_Conventional");
			}
		
			
			else if ("Islamic".equalsIgnoreCase(formObject.getNGValue("loan_type")))
			{
				PersonalLoanS.mLogger.info("Request is islamic");
				email = NGFUserResourceMgr_PL.getAlert("MCQ_Email_Issuance_Islamic");
			}

			PersonalLoanS.mLogger.info("email  before replacing: " + email);
			String sms = NGFUserResourceMgr_PL.getAlert("MCQ_SMS");
			email.replaceAll("replaceBank_Name", bankName);
			email.replaceAll("replaceWIName",formObject.getWFWorkitemName());
			PersonalLoanS.mLogger.info("Modified Email : " + email);
			PersonalLoanS.mLogger.info("SMS : " + sms);
			PersonalLoanS.mLogger.info("bankName  : " + bankName);
			insertMessageInQueue(formObject, "To_TeamMCQ", email, sms);
			
			
		}
		
		// added by Nishkarsh 24/05/2021
		if("PostDisbursal_LC_Sms".equalsIgnoreCase(pEvent.getSource().getName())){
			
			String bankName = getBankFromTakeoverGrid(formObject);
			if("Conventional".equalsIgnoreCase(formObject.getNGValue("loan_type")) || "Islamic".equalsIgnoreCase(formObject.getNGValue("loan_type"))){
				String email = NGFUserResourceMgr_PL.getAlert("NLC_Email");
				String sms = NGFUserResourceMgr_PL.getAlert("NLC_SMS");
				email.replaceAll("replaceBank_Name", bankName);
				email.replaceAll("replaceWIName",formObject.getWFWorkitemName());
				insertMessageInQueue(formObject, "To_TeamNLC", email, sms);
				PersonalLoanS.mLogger.info("Modified Email : " + email);
				PersonalLoanS.mLogger.info("SMS : " + sms);
			}
		}
	
		
		if("PostDisbursal_STL_SMS".equalsIgnoreCase(pEvent.getSource().getName())){
			
			String bankName = getBankFromTakeoverGrid(formObject);
			if("Conventional".equalsIgnoreCase(formObject.getNGValue("loan_type")) || "Islamic".equalsIgnoreCase(formObject.getNGValue("loan_type"))){
					String email = NGFUserResourceMgr_PL.getAlert("STL_Email");
					String sms = NGFUserResourceMgr_PL.getAlert("STL_SMS");
					email.replaceAll("replaceBank_Name", bankName);
					email.replaceAll("replaceWIName",formObject.getWFWorkitemName());
					insertMessageInQueue(formObject, "To_TeamSTL", email, sms);
					PersonalLoanS.mLogger.info("Modified Email : " + email);
					PersonalLoanS.mLogger.info("SMS : " + sms);
				}
			}
		

		// added by Rishabh 
		if ("Customer_Details_Verification1".equalsIgnoreCase(pEvent.getSource().getName())) {
			PersonalLoanS.mLogger.info("Inside Fetch Customer_Details_Verification1 sagarika");
			formObject.fetchFragment("Customer_Details_Verification1", "cust_ver_sp2", "q_custVeri");
			LoadPickList("Cust_ver_sp2_Combo9", "select convert(varchar, description),code from NG_MASTER_EmploymentStatus with (nolock)  where isActive='Y' order by code");				
            autopopulateValuesCustomerDetailVerification(formObject); 
           // LoadPickList("cmplx_cust_ver_sp2_emp_status__remarks","select convert(varchar, description),code from NG_MASTER_EmploymentStatus with (nolock)  where isActive='Y' order by code");
//			formObject.setNGValue("Cust_ver_sp2_Combo8","cmplx_cust_ver_sp2_emp_status__remarks");
//			//Loa
			formObject.setLocked("cmplx_cust_ver_sp2_application_type",true);
			if(!"FPU".equalsIgnoreCase(activityName) && !"FCU".equalsIgnoreCase(activityName))//PCASI-1006 shweta
			{
				formObject.setLocked("Customer_Details_Verification1",true);
			}

		}
		if ("Field_Visit_Initiated".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.fetchFragment("Field_Visit_Initiated", "fieldvisit_sp2", "q_field_sp");
			
				
			if("FPU".equalsIgnoreCase(activityName)   || "FCU".equalsIgnoreCase(activityName))//PCASI-1007 shweta
			{
				formObject.setLocked("Field_Visit_Initiated",false);
			}
			else
			{
				formObject.setLocked("Field_Visit_Initiated",true);
			}

		}
		if ("Employment_Verification".equalsIgnoreCase(pEvent.getSource().getName())) {
			
			formObject.fetchFragment("Employment_Verification", "EmploymentVerification_s2", "q_empVer_sp");
			LoadPickList("EmploymentVerification_s2_Combo14", "select convert(varchar, description),code from NG_MASTER_EmploymentStatus with (nolock)  where isActive='Y' order by code");				
            
						
			autopopulate_emp_verification();

			if(!"FPU".equalsIgnoreCase(activityName) && !"FCU".equalsIgnoreCase(activityName))//PCASI-1008 shweta
			{
				formObject.setLocked("Employment_Verification",true);
			}
		//fieldvisit_sp2_Button1	
		}
		if ("Customer_Info_FPU".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.fetchFragment("Customer_Info_FPU", "CustDetailVerification1", "q_CustDetailVeriFCU");
			if("FPU".equalsIgnoreCase(activityName) || "FCU".equalsIgnoreCase(activityName))
			{
				formObject.setLocked("Customer_Info_FPU",false);
			}
			else
			{
				formObject.setLocked("Customer_Info_FPU",true);
			}
			int framestate3=formObject.getNGFrameState("EmploymentDetails");
			if(framestate3 != 0){
				loadEmployment();
			}
			formObject.setNGValue("CustDetailVerification1_Compcode", formObject.getNGValue("cmplx_EmploymentDetails_EMpCode"));
			formObject.setNGValue("CustDetailVerification1_Compname",  formObject.getNGValue("cmplx_EmploymentDetails_EmpName"));
			autopopulateValuesCustInfo(formObject);


		}

		//added by rishabh
		if ("Banking_Check".equalsIgnoreCase(pEvent.getSource().getName())) {
			//12th sept q-variable name was missing
			formObject.fetchFragment("Banking_Check", "BankingCheck", "q_BankingCheck"); 
			//++Below code added by  nikhil 12/10/17 as per CC FSD 2.2
			//formObject.setEnabled("cmplx_BankingCheck_BankAccDetailUpdate", true);
			if("CAD_Analyst1".equalsIgnoreCase(activityName)){
				formObject.setEnabled("cmplx_BankingCheck_BankAccDetailUpdate", true);
				formObject.setLocked("cmplx_BankingCheck_BankAccDetailUpdate", false);
			}
			formObject.setEnabled("BankingCheck_IFrame1", true);
			//--above code added by  nikhil 12/10/17 as per CC FSD 2.2
			//12th sept q-variable name was missing
			formObject.setEnabled("BankingCheck_IFrame1", true);
			//--above code added by  nikhil 12/10/17 as per CC FSD 2.2
			if("FPU".equalsIgnoreCase(activityName))//pcasi-1009 by shweta
			{
				formObject.setLocked("Banking_Check",false);
				
			}
			else
			{
				formObject.setLocked("Banking_Check",true);
				formObject.setEnabled("Banking_Check",false);
			}

		}
		if ("CheckList".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.fetchFragment("CheckList", "checklist_ver_sp2", "q_checkList_sp");
			if(!"FPU".equalsIgnoreCase(activityName) && !"FCU".equalsIgnoreCase(formObject.getWFActivityName()))//PCASI-1009-shweta
			{
				formObject.setLocked("CheckList",true);
			}

		}
		if ("Exceptional_Case_Alert".equalsIgnoreCase(pEvent.getSource().getName())) {
			String activity = formObject.getWFActivityName();
			formObject.fetchFragment("Exceptional_Case_Alert", "exceptionalCase_sp2", "q_except_alert");
			 if("FPU".equalsIgnoreCase(activity) || "FCU".equalsIgnoreCase(activity))
             {
                 formObject.setLocked("Exceptional_Case_Alert",false);
             }
             else
             {
                 formObject.setLocked("Exceptional_Case_Alert",true);
             }
             //added by nikhil for pcas-1801 CR
             try
             {
             List<String> objInput_Liab=new ArrayList<String>();
             List<Object> objOutput_Liab=new ArrayList<Object>();
             objInput_Liab.add("Text:"+formObject.getWFWorkitemName());
             PersonalLoanS.mLogger.info("objOutput_Liab args are: "+objInput_Liab.get(0));
             objOutput_Liab.add("Text");
             objOutput_Liab=formObject.getDataFromStoredProcedure("ng_rlos_ExceptionFragmentAutomationProc", objInput_Liab,objOutput_Liab);
             PersonalLoanS.mLogger.info("output from proc ng_rlos_ExceptionFragmentAutomationProc :"+(String)objOutput_Liab.get(0));
             String[] Return=((String)objOutput_Liab.get(0)).split("-");
             int i=0;
             List<String> Exception_fields= Arrays.asList("cmplx_exceptionalCase_sp2_ch1","cmplx_exceptionalCase_sp2_ch2","cmplx_exceptionalCase_sp2_ch3","cmplx_exceptionalCase_sp2_ch4","cmplx_exceptionalCase_sp2_ch5","cmplx_exceptionalCase_sp2_ch6","cmplx_exceptionalCase_sp2_ch7","cmplx_exceptionalCase_sp2_ch8","cmplx_exceptionalCase_sp2_ch9","cmplx_exceptionalCase_sp2_ch10");
             for(String field : Exception_fields)
             {
                 formObject.setNGValue(field,Return[i]);
                 i++;
             }
             }
             catch(Exception Ex)
             {
            	 PersonalLoanS.mLogger.info("Error in ng_rlos_ExceptionFragmentAutomationProc :: "+Ex.getMessage());
             }

		}
		
		if ("FPU_GRID".equalsIgnoreCase(pEvent.getSource().getName())) {
			PersonalLoanS.mLogger.info("fpu grid clicked");
			formObject.fetchFragment("FPU_GRID", "Fpu_Grid", "q_FPU_Grid");
			if(!"FPU".equalsIgnoreCase(activityName) && !"FCU".equalsIgnoreCase(formObject.getWFActivityName()))
			{
				formObject.setLocked("Fpu_Grid_Frame1",true);
				formObject.setLocked("cmplx_FPU_Grid_Officer_Name", true);
				LoadPickList("cmplx_FPU_Grid_Officer_Name", "select ' --Select-- 'as UserName union select UserName from PDBUser where UserIndex in (select UserIndex from PDBGroupMember where GroupIndex=(select GroupIndex from PDBGroup where GroupName in ('FCU','FPU')))");
				
			}
			 String gridName="cmplx_ReferHistory_cmplx_GR_ReferHistory";
			for(int i=0;i<formObject.getLVWRowCount(gridName);i++){						
			if((formObject.getNGValue(gridName,i,5).equalsIgnoreCase("FCU")||formObject.getNGValue(gridName,i,5).equalsIgnoreCase("FPU")) && formObject.getNGValue(gridName,i,6).equalsIgnoreCase("Complete"))
						
			//if("FPU".equalsIgnoreCase(activityName) || "FCU".equalsIgnoreCase(formObject.getWFActivityName()))
			{
				formObject.setLocked("Generate_FPU_Report", false);
				formObject.setEnabled("Generate_FPU_Report",true);
			}
			}
			//cmplx_CustDetailVerification_offtelno_ver
			//formObject.setLocked("cmplx_FPU_Grid_Realloc_Analyst",true);
			//formObject.setLocked("cmplx_FPU_Grid_Realloc_recived_date",true);
			//String queryfpu="select assigneduser from wfinstrumenttable with (nolock) where activityname ='FPU' and ProcessInstanceID = '"+formObject.getWFWorkitemName()+"'";
			String readonlytest=formObject.getNGValue("readonlycheck");
			if(("FPU".equalsIgnoreCase(activityName)||"FCU".equalsIgnoreCase(activityName))&& !readonlytest.equalsIgnoreCase("R"))
			{
			PersonalLoanS.mLogger.info("flag for fpu"+formObject.getNGValue("readonlycheck"));	
			formObject.setNGValue("cmplx_FPU_Grid_Analyst_Name",formObject.getUserName());
			}
			//formObject.setNGValue("cmplx_FPU_Grid_Analyst_Name",queryfpu);
			formObject.setLocked("cmplx_FPU_Grid_Analyst_Name", true);
		
			
			
			//CustomSaveForm();
			//formObject.setNGValue("cmplx_FPU_Grid_Officer_Name",formObject.getUserName());
			String s=formObject.getWFGeneralData();
			int startPosition = s.indexOf("<EntryDateTime>") + "<EntryDateTime>".length();
			int endPosition = s.indexOf("</EntryDateTime>", startPosition);
			String subS = s.substring(startPosition, endPosition-4);
			formObject.setNGValue("cmplx_FPU_Grid_case_recived_date",subS );
			formObject.setLocked("cmplx_FPU_Grid_case_recived_date", true);
			String listValues ="";
			String WorkstepName="";
			int rowCount =formObject.getLVWRowCount(DECISION_LISTVIEW1);
			//String sQuery= "select top 1 FORMAT(datelastchanged,'dd/MM/yyyy HH:mm'),Decision from NG_RLOS_GR_DECISION where workstepName='"+formObject.getWFActivityName()+"' and dec_wi_name='"+formObject.getWFWorkitemName()+"' and (Decision= 'Positive' OR Decision= 'Negative') order by insertionOrderId desc";
			for(int i=0; i<rowCount;i++){
				WorkstepName=formObject.getNGValue("Decision_ListView1",i,2);
				if((WorkstepName.equalsIgnoreCase("FCU") || WorkstepName.equalsIgnoreCase("FPU"))&& (formObject.getNGValue("Decision_ListView1",i,3).equalsIgnoreCase("Positive")||formObject.getNGValue("Decision_ListView1",i,3).equalsIgnoreCase("Negative")))
				{
					formObject.setNGValue("cmplx_FPU_Grid_Status_Application",formObject.getNGValue("Decision_ListView1,i,3"));
					formObject.setNGValue("cmplx_FPU_Grid_CLosure_date",formObject.getNGValue("Decision_ListView1,i,1"));
					//listValues= formObject.getNGValue("Decision_ListView1",i,1);
				}
			}
			
						 
			if(!"FPU".equalsIgnoreCase(activityName) && !"FCU".equalsIgnoreCase(activityName))
			{
				formObject.setLocked("Fpu_Grid_Frame1",true);
			}


		}

		
		if ("Smart_Check1".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Smart_Check1","Clicked");
			formObject.fetchFragment("Smart_Check1", "SmartCheck1", "q_SmartCheckFCU");
			//loadPicklist3();
			//loadInDecGrid();
			//++Below code added by  nikhil 13/10/17 as per CC FSD 2.2
			formObject.setTop("SmartCheck1_CreditRemarks", 10);
			formObject.setTop("SmartCheck1_CPVRemarks", formObject.getTop("SmartCheck1_CreditRemarks")+65);
			formObject.setTop("SmartCheck1_FCURemarks", formObject.getTop("SmartCheck1_CPVRemarks")+65);
			formObject.setTop("SmartCheck1_Label1", formObject.getTop("SmartCheck1_CreditRemarks"));
			formObject.setTop("SmartCheck1_Label2", formObject.getTop("SmartCheck1_CPVRemarks"));
			formObject.setTop("SmartCheck1_Label4", formObject.getTop("SmartCheck1_FCURemarks"));
			//--above code added by  nikhil 13/10/17 as per CC FSD 2.2
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();}
		}
		if ("Smart_Check1".equalsIgnoreCase(pEvent.getSource().getName())) {
			formObject.fetchFragment("Smart_Check1", "Smart_Check1", "q_smartcheck1");
		}
		//added by akshay on 14/2/18
		if ("GuarantorDet".equalsIgnoreCase(pEvent.getSource().getName())) {
			//formObject.get
			formObject.fetchFragment("GuarantorDet", "GuarantorDetails", "q_Guarantor");
			loadPicklist_Guarantor();
			/*LoadPickList("GuarantorDetails_title", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_title with (nolock) order by code");
			LoadPickList("GuarantorDetails_gender", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_gender with (nolock) order by Code");
			LoadPickList("GuarantorDetails_nationality", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
			LoadPickList("GuarantorDetails_apptype", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_ApplicantType with (nolock) order by Code");
			LoadPickList("GuarantorDetails_MAritalStatus", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Maritalstatus with (nolock) order by Code");
			LoadPickList("GuarantorDetails_empType", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_EmploymentType with (nolock) order by Code");
			LoadPickList("GuarantorDetails_designation", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Designation with (nolock) order by Code");
		*/}	


		if ("EmploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

			hm.put("EmploymentDetails","Clicked");
			formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmpDetails");
			/*if(NGFUserResourceMgr_PL.getGlobalVar("PL_DDVT_Maker").equalsIgnoreCase(formObject.getWFActivityName())|| NGFUserResourceMgr_PL.getGlobalVar("PL_CSM").equalsIgnoreCase(formObject.getWFActivityName())){
				formObject.setNGValue("cmplx_EmploymentDetails_Designation",formObject.getNGValue("cmplx_Customer_Designation"));
			}*/ //saurabh1 for displaying designation in ddvt S4 I6
			if(!(NGFUserResourceMgr_PL.getGlobalVar("PL_CSM").equalsIgnoreCase(formObject.getWFActivityName())))
			{
				PersonalLoanS.mLogger.info("inside employer code CSM condition");
				/*if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_Others")) && formObject.isLocked("cmplx_EmploymentDetails_ApplicationCateg")==false)
				{
					PersonalLoanS.mLogger.info("inside employer code condition");
					PersonalLoanS.mLogger.info("inside islocked value"+formObject.isLocked("EMploymentDetails_Frame1"));
					//formObject.setLocked("cmplx_EmploymentDetails_EMpCode", false);

				}*/
				
				formObject.setVisible("cmplx_EmploymentDetails_Others", false);
				formObject.setVisible("EMploymentDetails_Label7", false);//PCASI-1079
				formObject.setVisible("cmplx_EmploymentDetails_OtherBankCAC", false);
				formObject.setVisible("EmploymentDetails_Bank_Button", false);
			
			
			}
			formObject.setLocked("cmplx_EmploymentDetails_CESflag",true);
			
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}	

		else if ("IncomeDEtails".equalsIgnoreCase(pEvent.getSource().getName())){
			hm.put("IncomeDEtails","Clicked");
			expandIncome();
			
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}
		
		else if("GuarantorDet".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			hm.put("GuarantorDet","Clicked");
			formObject.fetchFragment("GuarantorDet", "GuarantorDetails", "q_Guarantor");
			//income_Dectech();
			loadPicklist_Guarantor();

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}


		else if ("InternalExternalLiability".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("InternalExternalLiability","Clicked");

			formObject.fetchFragment("InternalExternalLiability", "Liability_New", "q_Liabilities");
			//chnages done by shivang for 2.1 started
			PersonalLoanS.mLogger.info("fetch_Liability_frag : Activity Name::"+formObject.getWFActivityName()+" WI No. ::"+formObject.getWFWorkitemName());
			if(formObject.getWFActivityName().equalsIgnoreCase("CAD_Analyst1")  || formObject.getWFActivityName().equalsIgnoreCase("Cad_Analyst2")){
				
				String refNo = "";
				String aecbScore = "";
				String range = "";
				try{
					//query corrected by shweta
					String query = "select top 1 ReferenceNo,AECB_Score,Range from (select ReferenceNo as ReferenceNo,AECB_Score as AECB_Score,Range as Range, case when cifId is null or cifId='' or cifId ='No Data' then '' else cifId  end as cifId from NG_rlos_custexpose_Derived with (nolock) where child_wi = '"+formObject.getWFWorkitemName()+"'  and Request_Type='ExternalExposure') as ext order by ReferenceNo desc";
					List<List<String>> list = formObject.getDataFromDataSource(query);
					PersonalLoanS.mLogger.info("Size of List:: "+list.size());
					if(list.size()>0){
						refNo= list.get(0).get(0);
						aecbScore =  list.get(0).get(1);
						range=  list.get(0).get(2);
					}
					PersonalLoanS.mLogger.info("Reference No:: "+refNo+ " AECB score ::"+aecbScore+ " Range ::"+range);
					formObject.setNGValue("cmplx_Liability_New_ReferenceNo", refNo);
					formObject.setNGValue("cmplx_Liability_New_AECBScore", aecbScore);
					formObject.setNGValue("cmplx_Liability_New_Range", range);
					
				}catch(Exception ex)
				{
					PersonalLoanS.mLogger.info("Exception occurred while fetching data"+ex.getMessage());
					printException(ex);
				}
			}
			formObject.setLocked("cmplx_Liability_New_ReferenceNo", true);
			formObject.setLocked("cmplx_Liability_New_AECBScore", true);
			formObject.setLocked("cmplx_Liability_New_Range", true);
			String App_Type=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 4);
			if (NGFUserResourceMgr_PL.getGlobalVar("PL_RESC").equalsIgnoreCase(App_Type) || NGFUserResourceMgr_PL.getGlobalVar("PL_RESN").equalsIgnoreCase(App_Type) || NGFUserResourceMgr_PL.getGlobalVar("PL_RESR").equalsIgnoreCase(App_Type)){
				formObject.setVisible("Liability_New_Label6", true);
				formObject.setVisible("cmplx_Liability_New_noofpaidinstallments", true);
			}
			else
			{
				formObject.setVisible("Liability_New_Label6", false);
				formObject.setVisible("cmplx_Liability_New_noofpaidinstallments", false);
			}
			LoadPickList("ExtLiability_contractType", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_contract_type with (nolock)  where isactive='Y' order by code");

			LoadPickList("Liability_New_worststatuslast24", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from ng_master_Aecb_Codes with (nolock) order by code");
			formObject.setLocked("Emi",true);
			
			int framestate2=formObject.getNGFrameState("EmploymentDetails");
			PersonalLoanS.mLogger.info("framestate for Employment is: "+framestate2);
			if(framestate2 == 0){
				PersonalLoanS.mLogger.info("EmploymentDetails");
			}
			else {
				loadEmployment();
				PersonalLoanS.mLogger.info("fetched employment details");
			}
			
			openDemographicTabs();
			formObject.setLocked("cmplx_Liability_New_AggrExposure",true);
			formObject.setLocked("cmplx_Liability_New_DBR",true);
			formObject.setLocked("cmplx_Liability_New_TAI",true);
			formObject.setLocked("cmplx_Liability_New_DBRNet",true);
			String activity = formObject.getWFActivityName();

			if("CSM".equalsIgnoreCase(activity)){
				//empty  if

			}
			if("CPV".equalsIgnoreCase(formObject.getWFActivityName()) || "CPV_Analyst".equalsIgnoreCase(formObject.getWFActivityName())|| "Smart_CPV".equalsIgnoreCase(formObject.getWFActivityName())
					||formObject.getWFActivityName().contains("CPV"))
			{
				//formObject.setVisible("Liability_New_Button1", true);
				formObject.setEnabled("Liability_New_Button1",false);
				formObject.setLocked("Liability_New_Button1",true);
			}
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}	




		else if ("EligibilityAndProductInformation".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("EligibilityAndProductInformation","Clicked");

			formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligibilityProdInfo");	

			//PCASI - 3524
			if("CAD_Analyst1".equalsIgnoreCase(activityName) || "CAD_Analyst2".equalsIgnoreCase(activityName) 
					|| "CPV".equalsIgnoreCase(activityName) || "Post_Disbursal".equalsIgnoreCase(activityName) 
					|| "Hold_CPV".equalsIgnoreCase(activityName) || "TO_Team".equalsIgnoreCase(activityName)
					|| "CPV_Analyst".equalsIgnoreCase(activityName) || "Disbursal_Maker".equalsIgnoreCase(activityName)
					|| "Disbursal_Checker".equalsIgnoreCase(activityName) ||  "PostDisbursal_Maker".equalsIgnoreCase(activityName)
					|| "PostDisbursal_Checker".equalsIgnoreCase(activityName) || "Takeover_Hold".equalsIgnoreCase(activityName)){
				PersonalLoanS.mLogger.info("Inside if, activityName :: "+activityName);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_InterestRate", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_EMI", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label5", false); //Interest Rate Label
				formObject.setVisible("ELigibiltyAndProductInfo_Label6", false); //EMI Label
				formObject.setVisible("cmplx_EligibilityAndProductInfo_Tenor", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label7", false);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_promo_interest_rate", true); //promorate hritik
				PersonalLoanS.mLogger.info("Hiding Interest Rate, EMI and Tenor fields and lables.");
			
			}
			else{
				PersonalLoanS.mLogger.info("Inside else, activityName :: "+activityName);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_InterestRate", true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_promo_interest_rate", true);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_EMI", true);
				formObject.setVisible("ELigibiltyAndProductInfo_Label7", true);
				PersonalLoanS.mLogger.info("Setting Interest Rate, EMI fields and Tenor lable visible true.");
				
			}
			
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
				PersonalLoanS.mLogger.info("cmplx_EligibilityAndProductInfo_BaseRateType8 cmplx_EligibilityAndProductInfo_BaseRateType"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_BaseRateType"));

			}


		}	


		else if ("Address_Details_container".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Address_Details_container","Clicked");


			formObject.fetchFragment("Address_Details_container", "AddressDetails", "q_AddressDetails");
			loadPicklist_Address();

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}	


		else if ("Alt_Contact_container".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Alt_Contact_container","Clicked");
			
			formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
			formObject.setNGFrameState("Alt_Contact_container", 0);

			if("Y".equalsIgnoreCase(formObject.getNGValue("is_CC_waiver_require")))
			{
				formObject.setLocked("AlternateContactDetails_carddispatch", true);
			}
			if("DDVT_maker".equalsIgnoreCase(formObject.getWFActivityName()))
			{
			formObject.setEnabled("AlternateContactDetails_MobileNo1", false);
			}
			if("false".equals(formObject.getNGValue("cmplx_Customer_NTB"))){
				checkforCurrentAccounts();
			}
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("ReferenceDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("ReferenceDetails","Clicked");
			formObject.fetchFragment("ReferenceDetails", "ReferenceDetails", "q_ReferenceDetails");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			hm.put("FATCA","Clicked");

			formObject.fetchFragment("FATCA", "FATCA", "q_FATCA");
			String usRelation = formObject.getNGValue("cmplx_FATCA_USRelation");
			try
			{
				if("O".equalsIgnoreCase(usRelation)){
					//changed from setenabled to setlocked by saurabh on 11 nov 17.
					formObject.setLocked("FATCA_Frame6", true);
					formObject.setLocked("cmplx_FATCA_USRelation",false);
					formObject.setEnabled("FATCA_Save", true);
					formObject.setLocked("cmplx_FATCA_SignedDate", true);
					formObject.setLocked("cmplx_FATCA_ExpiryDate", true);

				}
				//done to bypass fatca kindly remove after drop-4 fatca grid delivery
				if("O".equalsIgnoreCase(usRelation) || "C".equalsIgnoreCase(usRelation) )
				{
					formObject.removeItem("cmplx_FATCA_SelectedReason", 0);
					formObject.removeItem("cmplx_FATCA_SelectedReason", 0);
					//formObject.clear("cmplx_FATCA_SelectedReason");
					formObject.addItem("cmplx_FATCA_ListedReason", "CURRENT US PHONE NO");
					formObject.addItem("cmplx_FATCA_ListedReason", "US PLACE OF BIRTH");
				}
			}
			catch(Exception ex)
			{
				PersonalLoanSCommonCode.mLogger.info(ex.getMessage());
			}
			formObject.setNGValue("cmplx_FATCA_DocsCollec", "Documents Collected");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}

		}
		else if("Supplementary_Card_Details".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			hm.put("Supplementary_Card_Details","Clicked");
			PersonalLoanS.mLogger.info("Supplementary_Card_Details before fetch fragment");
			formObject.fetchFragment("Supplementary_Card_Details", "SupplementCardDetails", "q_SuppCardDetails");
			PersonalLoanS.mLogger.info("Supplementary_Card_Details after fetch fragment");
			loadPicklist_suppCard();

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			hm.put("KYC","Clicked");

			formObject.fetchFragment("KYC", "KYC", "q_KYC");

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}

		}	

		else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			hm.put("OECD","Clicked");

			formObject.fetchFragment("OECD", "OECD", "q_OECD");
			formObject.setNGFrameState("OECD", 0);
			formObject.setNGValue("OECD_CRSFlag","N");
			PersonalLoanS.mLogger.info("ResidentNonResident"+ formObject.getNGValue("cmplx_Customer_ResidentNonResident"));
			if("Y".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_ResidentNonResident")))
			{
				formObject.setNGValue("OECD_CountryTaxResidence","AE");
			}
			PersonalLoanS.mLogger.info("CTR"+ formObject.getNGValue("OECD_CountryTaxResidence"));

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}

		}	

		else if ("LoanDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("LoanDetails","Clicked");
			PersonalLoanS.mLogger.info("Inside Loan Details fetch fragment: ");
			if(formObject.isVisible("ELigibiltyAndProductInfo_Frame1")==false)
			 {
				PersonalLoanS.mLogger.info("Before fecthing EligProdInfo: ");
					formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligibilityProdInfo");

			 }
			formObject.fetchFragment("LoanDetails", "LoanDetails", "q_Loan");
			expandLoanDetails();
			formObject.setEnabled("cmplx_LoanDetails_ddaref", false);	//PCASI 3324
			formObject.setEnabled("cmplx_LoanDetails_ddastatus", false);	//PCASI 3324
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
			
		}

		else if ("Self_Employed".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Self_Employed","Clicked");

			formObject.fetchFragment("Self_Employed", "SelfEmployed", "q_SelfEmployed");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("DecisionHistory","Clicked");
			expandDecision();
			PersonalLoanS.mLogger.info("before Expand decision");
			PersonalLoanS.mLogger.info("After Expand decision");
			formObject.fetchFragment("DecisionHistory", "DecisionHistory", "q_DecisionHistory");
			loadInDecGrid();
			PersonalLoanS.mLogger.info("After Load in DEc frid");
			loadPicklist3();
			PersonalLoanS.mLogger.info("After Loadpicklist3");
			formObject.setLocked("cmplx_Decision_CADDecisiontray", true);
			
			PersonalLoanS.mLogger.info("Before IF");
			if("Smart_CPV".equalsIgnoreCase(formObject.getWFActivityName()))
			{
				if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_contactableFlag")))
				{
				formObject.setEnabled("DecisionHistory_nonContactable", false);
				formObject.setEnabled("NotepadDetails_Frame1", false);
				formObject.setLocked("DecisionHistory_Frame1", true);
				formObject.setEnabled("SmartCheck_Frame1", false);
				formObject.setLocked("OfficeandMobileVerification_Frame1", true);

				formObject.setEnabled("DecisionHistory_cntctEstablished", true);

				formObject.setNGValue("cmplx_Decision_Decision","Smart CPV Hold");
				//formObject.setLocked("cmplx_DEC_Decision","Smart CPV Hold");
					formObject.setLocked("cmplx_Decision_REMARKS", false);
				}
			}
			/*else if("DDVT_Hold".equalsIgnoreCase(formObject.getWFActivityName())){
				PersonalLoanS.mLogger.info("indide DDVT_Hold TRY BLOCK");
				formObject.setLocked("cmplx_Decision_New_CIFNo",true);
				formObject.setVisible("DecisionHistory_ADD", true);
				formObject.setVisible("DecisionHistory_Modify", true);
				formObject.setVisible("DecisionHistory_Delete",true);
				formObject.setTop("DecisionHistory_ADD",formObject.getTop("cmplx_Decision_REMARKS")+50);
				formObject.setLeft("DecisionHistory_ADD", formObject.getLeft("DecisionHistory_Modify"));
				formObject.setTop("DecisionHistory_Modify",formObject.getTop("cmplx_Decision_REMARKS")+50);
				formObject.setLeft("DecisionHistory_Modify", formObject.getLeft("DecisionHistory_ADD")+100);
				formObject.setTop("DecisionHistory_Delete",formObject.getTop("cmplx_Decision_REMARKS")+50);
			}*/
			formObject.setLocked("cmplx_Decision_strength",true);
			formObject.setLocked("cmplx_Decision_weakness",true);
			formObject.setLocked("cmplx_Decision_TotalOutstanding",true);
			formObject.setLocked("cmplx_Decision_TotalEMI",true);
			formObject.setLocked("cmplx_Decision_Dectech_decsion",true);
			LoadPickList("cmplx_Decision_ALOCcompany", "Select Description from ng_master_fraudsubreason with (nolock)");
			//below code added for srinidhi points 29/11/18
			formObject.setLocked("cmplx_Decision_VERIFICATIONREQUIRED", true);
			adjustFrameTops("Notepad_Values,DecisionHistory,ReferHistory");
			
			//setting contact established and non contactable buttons  along side remarks:bandana:PCASI-3455
			formObject.setLeft("DecisionHistory_nonContactable", formObject.getWidth("cmplx_Decision_REMARKS")+100);
			formObject.setTop("DecisionHistory_nonContactable",formObject.getTop("cmplx_Decision_REMARKS"));
			
			formObject.setLeft("DecisionHistory_cntctEstablished", formObject.getLeft("DecisionHistory_nonContactable")+150);
			formObject.setTop("DecisionHistory_cntctEstablished",formObject.getTop("cmplx_Decision_REMARKS"));
			formObject.setTop("Decision_ListView1",formObject.getTop("DecisionHistory_ADD")+100);
			if("DDVT_maker".equalsIgnoreCase(formObject.getWFActivityName()))
			{
				if(formObject.getNGValue("FircoStatusLabel").equalsIgnoreCase("Hit"))
				{
					PersonalLoanS.mLogger.info("checking activityname for decsion"+formObject.getWFActivityName()); 
					 formObject.setNGValue("cmplx_Decision_Decision","Refer");
					 PersonalLoanS.mLogger.info("checking why not refer"+formObject.getNGValue("cmplx_Decision_Decision")); 
						
					 
					 formObject.setEnabled("cmplx_Decision_Decision",false);
					 formObject.setEnabled("cmplx_Decision_ReferTo", false);
					 formObject.setEnabled("DecisionHistory_DecisionSubReason", false);
					 formObject.setEnabled("DecisionHistory_DecisionReasonCode", false);
				}
				else 
				{
					PersonalLoanS.mLogger.info("checking activityname for decsion"+formObject.getWFActivityName()); 
					
					 formObject.setEnabled("cmplx_Decision_Decision",true);
					 loadPicklist3();
				}
			
			}
			
			try
			{
				PersonalLoanS.mLogger.info("CHECKING RISHABH TRY BLOCK");
				//cpv_Decision();
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}

		}
		//code changed by bandana start
		/*else if ("Inc_Doc".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Inc_Doc","Clicked");
			//query changed by saurabh as part of new Incoming Document. 7th Jan.
			formObject.fetchFragment("Inc_Doc", "IncomingDoc", "q_IncomingDoc");
			//added by akshay on 9/5/18
			if(formObject.getNGValue("sig_docindex").equals("")){
				String query="select isnull(DocIndex,'') from ng_rlos_gr_incomingDocument with(nolock) where DocumentName='Signature' and IncomingDocGR_Winame = '"+formObject.getWFWorkitemName()+"'";
				List<List<String>> signature_index=formObject.getDataFromDataSource(query);
				formObject.setNGValue("sig_docindex", signature_index.get(0).get(0));
			}
			//commented by saurabh on 11th nov 17.

			String activName = formObject.getWFActivityName();
			if("DDVT_Checker".equalsIgnoreCase(activName)){
				if("".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP")) && NGFUserResourceMgr_PL.getGlobalVar("PL_false").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")))
					formObject.setVisible("IncomingDoc_UploadSig",false);
				else if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP")) || NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
					formObject.setVisible("IncomingDoc_ViewSIgnature",false);
					formObject.setVisible("IncomingDoc_UploadSig",true);

				}
			}
			else if("DDVT_Maker".equalsIgnoreCase(activName)){
				if("".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP")) && NGFUserResourceMgr_PL.getGlobalVar("PL_false").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
					formObject.setVisible("IncomingDoc_ViewSIgnature",true);
					formObject.setEnabled("IncomingDoc_ViewSIgnature",true);
				}
				else if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP")) || NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
					formObject.setEnabled("IncomingDoc_ViewSIgnature",false);

				}
			}---commented by akshay on 17/1/18 as code shifted to workstep files
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}*/
		
		if ("Inc_Doc".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			//check
			hm.put("IncomingDocuments","Clicked");
			PersonalLoanS.mLogger.info("IncomingDocuments");
			//frame,fragment,q_variable
			/*formObject.fetchFragment("Frame4", "IncomingDocument", "q_IncDoc");
			
			CreditCard.mLogger.info("fetchIncomingDocRepeater");
			fetchIncomingDocRepeater();
			*/
			formObject.setHeight("IncomeDetails_Frame1", 850);
			formObject.setHeight("Incomedetails", 870);
			formObject.setHeight("Inc_Doc",850);
			//changes for new incoming doc repeater by saurab h.
			if("Salaried".equalsIgnoreCase(formObject.getNGValue("EmploymentType"))){
				emp_details();
			}
			else{
				int framestate_CompDetails = formObject.getNGFrameState("CompDetails");
				if(framestate_CompDetails == 0){
					PersonalLoanS.mLogger.info("CompDetails already fetched");
				}
				else {
					formObject.fetchFragment("CompDetails", "CompanyDetails", "q_CompanyDetails");
					PersonalLoanS.mLogger.info("fetched CompDetails details");
				}
			}
			formObject.fetchFragment("Inc_Doc","IncomingDocNew","q_incomingDocNew");
			PersonalLoanS.mLogger.info("fetchIncomingDocRepeater");
			if(formObject.getWFActivityName().equalsIgnoreCase("DDVT_Maker") ){
				formObject.setVisible("IncomingDocNew_UploadSig",false);
				if("".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP")) && NGFUserResourceMgr_PL.getGlobalVar("PL_false").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
					formObject.setVisible("IncomingDocNew_ViewSIgnature",true);
					formObject.setEnabled("IncomingDocNew_ViewSIgnature",true);
				}
				else if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP")) || NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
					formObject.setEnabled("IncomingDocNew_ViewSIgnature",false);
					formObject.setVisible("IncomingDocNew_UploadSig",true);

					
				}
			}
			if(formObject.getWFActivityName().equalsIgnoreCase("DDVT_checker") ){
				formObject.setVisible("IncomingDocNew_UploadSig",false);
				if("".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP")) && NGFUserResourceMgr_PL.getGlobalVar("PL_false").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
					formObject.setVisible("IncomingDocNew_ViewSIgnature",true);
					formObject.setEnabled("IncomingDocNew_ViewSIgnature",true);
				}
				else if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP")) || NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
					formObject.setEnabled("IncomingDocNew_ViewSIgnature",false);
					formObject.setVisible("IncomingDocNew_UploadSig",true);
					formObject.setEnabled("IncomingDocNew_UploadSig",false);

					
				}
			}
			fetchIncomingDocRepeaterNew();
			
			//added by akshay on 9/5/18
			//query changed by saurabh as part of new Incoming Document. 7th Jan.
			if(formObject.getNGValue("sig_docindex").equals("")){
				String query="select isnull(DocIndex,'') from ng_rlos_gr_incomingDocument with(nolock) where DocumentName='Signature' and IncomingDocGR_Winame = '"+formObject.getWFWorkitemName()+"'";
				List<List<String>> signature_index=formObject.getDataFromDataSource(query);
				formObject.setNGValue("sig_docindex", signature_index.get(0).get(0));
			}

			PersonalLoanS.mLogger.info("formObject.fetchFragment1");
			try{

				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}
		
		//code changes by bandana ends
		//Below code added by nikhil 13/11/2017 for Code merge

		else if ("Post_Disbursal".equalsIgnoreCase(pEvent.getSource().getName())) 
		{
			hm.put("Post_Disbursal","Clicked");



			PersonalLoanS.mLogger.info("Inside PostDisbursal-->FrameExpandEvent()");

			formObject.fetchFragment("Post_Disbursal", "PostDisbursal", "q_PostDisbursal");
			formObject.setNGValue("PostDisbursal_Winame", formObject.getWFWorkitemName());
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}
		//++Below code added by  Yash on  2/11/17 as per CC FSD 2.7

		else if (pEvent.getSource().getName().equalsIgnoreCase("Dispatch_Details")) 
		{
			hm.put("Dispatch_Details","Clicked");

			//popupFlag="N";

			PersonalLoanS.mLogger.info("PersonalLoanS"+"Inside PostDisbursal-->FrameExpandEvent()");

			formObject.fetchFragment("Dispatch_Details", "DispatchFrag", "q_Dispatch");

			LoadPickList("DispatchFrag_Transtype", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_TransactionType with (nolock)");
			LoadPickList("DispatchFrag_Emirates", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_Emirate with (nolock)");

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}

		else if (pEvent.getSource().getName().equalsIgnoreCase("Postdisbursal_Checklist")) 
		{
			hm.put("Postdisbursal_Checklist","Clicked");



			//PL_SKLogger.writeLog("PersonalLoanS","Inside PostDisbursal-->FrameExpandEvent()");

			formObject.fetchFragment("Postdisbursal_Checklist", "PostDisbursal_Checklist", "q_PostDisbursal_Checklist");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{hm.clear();}
		}
		//++Above code added by  Yash on  2/11/17 as per CC FSD 2.7
		else if ("Loan_Disbursal".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Loan_Disbursal","Clicked");


			formObject.fetchFragment("Loan_Disbursal", "Loan_Disbursal", "q_LoanDisb");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("CC_Creation".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("CC_Creation","Clicked");

			//formObject.fetchFragment("CC_Creation", "CC_Creation", "q_FinIncident");
			LoadPickList("CC_Creation_Product","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cardProduct with (nolock) order by code");
			expandDecision();
			formObject.setLocked("cmplx_Decision_CADDecisiontray", true);
			int framestate_part_match = formObject.getNGFrameState("Part_Match");
			if(framestate_part_match == 0){
			}
			else {
				formObject.fetchFragment("Part_Match", "PartMatch", "q_PartMatch");
				formObject.setNGFrameState("Part_Match",0);				
			}
			expandFinacleCRMCustInfo();
			openDemographicTabs();
			
			fetchFinacleCoreFrag("");
			formObject.fetchFragment("Details", "CC_Loan", "q_CC_Loan");
			formObject.fetchFragment("CC_Creation", "CC_Creation", "q_CCCreation");
			formObject.setNGFocus("CC_Creation");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Limit_Inc".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Limit_Inc","Clicked");


			formObject.fetchFragment("Limit_Inc", "Limit_Inc", "q_FinIncident");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Cust_Det_Ver".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Cust_Det_Ver","Clicked");


			expandCustDetVer();
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Business_Verif".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Business_Verif","Clicked");


			formObject.fetchFragment("Business_Verif", "BussinessVerification1", "q_bussverification1");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Emp_Verification".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Emp_Verification","Clicked");


			formObject.fetchFragment("Emp_Verification", "EmploymentVerification", "q_EmpVerification");
			//Below code added by nikhil 13/11/2017 for Code merge
			//below code commented by nikhil 31/10/17
			//EmpVervalues();
			//Above code added by nikhil 13/11/2017 for Code merge
			LoadPickList("cmplx_EmploymentVerification_FiledVisitedInitiated_updates", "select description from ng_master_fieldvisitDoneupdates with (nolock) ");
			//LoadPickList("cmplx_EmploymentVerification_OffTelnoValidatedfrom", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_OffTelnoVal with (nolock)");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Bank_Check".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Bank_Check","Clicked");


			formObject.fetchFragment("Bank_Check", "BankingCheck", "q_BankingCheck");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Note_Details".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Note_Details","Clicked");

			PersonalLoanS.mLogger.info("inside NOte_Details frame expanded event");  			
			formObject.fetchFragment("Note_Details", "NotepadDetailsFCU", "q_Note");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Supervisor_section".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Supervisor_section","Clicked");


			formObject.fetchFragment("Supervisor_section", "supervisorsection", "q_Supervisor");
			LoadPickList("cmplx_FcuDecision_ReferralReason", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_Master_Referralreason with (nolock)");
			LoadPickList("cmplx_FcuDecision_ReferralSubReason", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_Master_Referralsubreason with (nolock)");

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		} 

		else if ("Smart_chk".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Smart_chk","Clicked");


			formObject.fetchFragment("Smart_chk", "SmartCheck1", "q_SmartCheckFCU");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Part_Match".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Part_Match","Clicked");


			formObject.fetchFragment("Part_Match", "PartMatch", "q_PartMatch");
			formObject.setNGFrameState("Part_Match",0);				

			//added by saurabh on 10th July 17 for DDVT_MAKER.
			partMatchValues();
			if("Original_Validation".equalsIgnoreCase(formObject.getWFActivityName())){
				formObject.setEnabled("PartMatch_Dob", true);
			}
			if("CPV".equalsIgnoreCase(formObject.getWFActivityName()))
			{
				formObject.setEnabled("PartMatch_Frame1", false);
			}
		//	LoadPickList("PartMatch_nationality", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_MASTER_Country with (nolock)");

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}

		}

		else if ("FinacleCRM_Incidents".equalsIgnoreCase(pEvent.getSource().getName())){					
			hm.put("FinacleCRM_Incidents","Clicked");

			formObject.fetchFragment("FinacleCRM_Incidents", "FinacleCRMIncident", "q_FinIncident");
			loadInAutoLoanGrid(formObject);
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("FinacleCRM_CustInfo".equalsIgnoreCase(pEvent.getSource().getName())){ 
			hm.put("FinacleCRM_CustInfo","Clicked");
			expandFinacleCRMCustInfo();	
			
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}

		}

		else if ("Finacle_Core".equalsIgnoreCase(pEvent.getSource().getName())){ 
			hm.put("Finacle_Core","Clicked");

			expandFinacleCore();

			/*try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}*/


		}

		else if ("MOL".equalsIgnoreCase(pEvent.getSource().getName())){ 
			hm.put("MOL","Clicked");

			formObject.fetchFragment("MOL", "MOL1", "q_MOL");
			loadPicklistMol();
			PersonalLoanS.mLogger.info("@@@@"+formObject.getWFActivityName());
			if("CPV".equalsIgnoreCase(formObject.getWFActivityName()) || "Document_Checker".equalsIgnoreCase(formObject.getWFActivityName()) || "Original_Validation".equalsIgnoreCase(formObject.getWFActivityName()))
			{
				formObject.setEnabled("MOL1_Frame1", false);
			}
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}

		}

		else if ("World_Check".equalsIgnoreCase(pEvent.getSource().getName())){ 
			hm.put("World_Check","Clicked");

			formObject.fetchFragment("World_Check", "WorldCheck1", "q_WorldCheck");
			loadPicklist_WorldCheck();
			if ("CPV".equalsIgnoreCase(formObject.getWFActivityName()))
			{ 
				formObject.setEnabled("WorldCheck1_Frame1", false);
			}
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}

		}

		else if ("Reject_Enq".equalsIgnoreCase(pEvent.getSource().getName())){ 
			hm.put("Reject_Enq","Clicked");

			formObject.fetchFragment("Reject_Enq", "RejectEnq", "q_RejectEnq");
			loadInRejectEnq(formObject);
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}

		}

		else if ("Sal_Enq".equalsIgnoreCase(pEvent.getSource().getName())){ 
			hm.put("Sal_Enq","Clicked");

			formObject.fetchFragment("Sal_Enq", "SalaryEnq", "q_SalaryEnq");
			int row_count=formObject.getLVWRowCount("cmplx_SalaryEnq_SalGrid");
			
			try{
				if(row_count==0)
				{
				//formObject.clear("cmplx_SalaryEnq_SalGrid");
				String query="select distinct SalCreditDate,SalCreditMonth,AcctId,SalAmount from ng_rlos_FinancialSummary_SalTxnDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
				List<List<String>> list_acc=formObject.getDataFromDataSource(query);
				for(List<String> mylist : list_acc)
				{
					PersonalLoanS.mLogger.info( "Data to be added in account Grid account Grid: "+mylist.toString());
					formObject.addItemFromList("cmplx_SalaryEnq_SalGrid", mylist);
				}

			}
			}
			finally{
				hm.clear();
			}

		}				




		else if ("Dec".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Dec","Clicked");
			formObject.fetchFragment("Dec", "CAD_Decision", "q_CadDecision");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("ReferHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("ReferHistory","Clicked");


			LoadReferGrid();
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Cust_Detail_verification".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Cust_Detail_verification","Clicked");
			//formObject.fetchFragment("Cust_Detail_verification", "CustDetailVerification", "q_CustDetVer");
			load_Customer_Details_Verification(formObject);
			//custdetvalues();
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Business_verification".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Business_verification","Clicked");
			formObject.fetchFragment("Business_verification", "BussinessVerification", "q_businessverification");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Home_country_verification".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Home_country_verification","Clicked");

			//PersonalLoanS.mLogger.info("@@@@Shweta"+pEvent.getSource().getName());
			// hritik 22.8.21 - 3779
			//formObject.fetchFragment("Home_country_verification", "HomeCountryVerification", "q_HomeCountryVeri");
			formObject.setNGFrameState("Home_country_verification",0);

			//List<String> LoadPicklist_Verification= Arrays.asList("cmplx_HCountryVerification_Hcountrytelverified");//Arun 13/12/17 this master should not load
			//LoadPicklistVerification(LoadPicklist_Verification);
			//LoadPickList("cmplx_HCountryVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code"); //Arun 13/12/17
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Residence_verification".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Residence_verification","Clicked");


			formObject.fetchFragment("Residence_verification", "ResidenceVerification", "q_ResiVerification");
			LoadPickList("cmplx_ResiVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");


			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Guarantor_verification".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Guarantor_verification","Clicked");


			formObject.fetchFragment("Guarantor_verification", "GuarantorVerification", "q_GuarantorVer");
			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_GuarantorVerification_verdoneonmob");
			LoadPicklistVerification(LoadPicklist_Verification);

			//LoadPickList("cmplx_GuarantorVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Reference_detail_verification".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Reference_detail_verification","Clicked");


			formObject.fetchFragment("Reference_detail_verification", "ReferenceDetailVerification", "q_RefDetVer");
			LoadPickList("cmplx_RefDetVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) order by code");

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Office_verification".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Office_verification","Clicked");

			load_Office_Mob_Verification(formObject);
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Loan_card_details".equalsIgnoreCase(pEvent.getSource().getName())) {
			PersonalLoanS.mLogger.info("@@@@sss");

			hm.put("Loan_card_details","Clicked");


			load_LoanCard_Details_Check(formObject);
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Notepad_details".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Notepad_details","Clicked");


			formObject.fetchFragment("Notepad_details", "NotepadDetails", "q_Note");
			//PCASI - 3154
			PersonalLoanS.mLogger.info("Notepad details change for activity name :: "+formObject.getWFActivityName());
			if("Disbursal_Maker".equalsIgnoreCase(activityName) || "Disbursal_Checker".equalsIgnoreCase(activityName))
				LoadPickList("NotepadDetails_notedesc", "select '--Select--' union select  description from ng_master_notedescription with (nolock)");
			else
				LoadPickList("NotepadDetails_notedesc", "select '--Select--' union select  description from ng_master_notedescription with (nolock) where workstep != 'NA'");

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Smart_check".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Smart_check","Clicked");

			// below code added by abhishek to load smartcheck and check for button flag as per FSD 2.7.3
			if(!NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_contactableFlag"))){
				formObject.fetchFragment("Smart_check", "SmartCheck", "q_SmartCheck");

			}
			else{
				formObject.setEnabled("SmartCheck_Frame1", false);
			}
			//below code by saurabh on 10th nov 2017.
			String activName = formObject.getWFActivityName();
			if("CAD_Analyst1".equalsIgnoreCase(activName)){
				formObject.setLocked("SmartCheck_CPVrem",true);
			}
			else if("CPV".equalsIgnoreCase(activName)){
				formObject.setLocked("SmartCheck_creditrem",true);
			}
			//-- Above code added by abhishek to load smartcheck and check for button flag as per FSD 2.7.3
			//below code added by nikhil
			if("CPV".equalsIgnoreCase(activName)){
				formObject.setLocked("SmartCheck_Frame1",true);
			}
			//added by prabhakar for making these fields non editable as per flv 6.15
			PersonalLoanS.mLogger.info("@@@@Prabhakar"+activName);
			if("Smart_CPV ".equalsIgnoreCase(activName))
			{
				formObject.setEnabled("SmartCheck_creditrem", false);
				formObject.setEnabled("SmartCheck_CPVrem", false);
			}
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Orig_validation".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Orig_validation","Clicked");

			formObject.fetchFragment("Orig_validation", "OriginalValidation", "q_OrigVal");
			fetchoriginalDocRepeater();
			//fetchIncomingDocRepeater();

		}

		//New fragment added by Arun on 17/7/17 for new DDVT changes on screen by shashank 
		else if ("Risk_Rating".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Risk_Rating","Clicked");
			

			formObject.fetchFragment("Risk_Rating", "RiskRating", "q_riskrating");
			
			//formObject.setLocked("cmplx_RiskRating_Total_riskScore",true);
			 //Added by aman to load the picklist of risk rating
			String activity=formObject.getWFActivityName();
			if("Original_Validation".equalsIgnoreCase(activity) || "CPV".equalsIgnoreCase(activity) ||  "Document_Checker".equalsIgnoreCase(activity))
			{
				formObject.setEnabled("RiskRating_Frame1",false);
			}//by shweta
			if(!"DDVT_maker".equalsIgnoreCase(activity)) {
				formObject.setEnabled("RiskRating_Frame1",false);
				
			}
			if("DDVT_maker".equalsIgnoreCase(activity)){
				if("".equalsIgnoreCase(formObject.getNGValue("cmplx_RiskRating_BusinessSeg"))){
					//cmplx_RiskRating_BusinessSeg: PERSONAL BANKING
					formObject.setNGValue("cmplx_RiskRating_BusinessSeg", "PERSONAL BANKING");
				}
				if("".equalsIgnoreCase(formObject.getNGValue("cmplx_RiskRating_SubSeg"))){
					//cmplx_RiskRating_SubSeg: PB - NORMAL
					formObject.setNGValue("cmplx_RiskRating_SubSeg", "PB - NORMAL");
				}
				if("".equalsIgnoreCase(formObject.getNGValue("cmplx_RiskRating_Industry"))||"--Select--".equalsIgnoreCase(formObject.getNGValue("cmplx_RiskRating_Industry"))){
					//cmplx_RiskRating_Industry: EMPLOYED INDIVIDUAL
					formObject.setNGValue("cmplx_RiskRating_Industry", "EMPLOYED INDIVIDUAL");
				}
				
			}
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Notepad_Values".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Notepad_Values","Clicked");
			fetch_NotepadDetails();//pcasp-1517
			if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1).equalsIgnoreCase("Personal Loan")
					&& (formObject.getWFActivityName().contains("CPV") || formObject.getWFActivityName().contains("Cad")
							|| formObject.getWFActivityName().contains("CAD") || formObject.getWFActivityName().contains("Credit"))){
					formObject.setVisible("NotepadDetails_Frame3",false);
				}
		}


		else if ("Card_Details".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Card_Details","Clicked");


			//formObject.fetchFragment("Card_Details", "CardDetails", "q_CardDetails");
			//change by akshay on 22nd Dec
			fetch_CardDetails_frag(formObject);
			if ("false".equalsIgnoreCase(formObject.getNGValue("cmplx_CardDetails_securitycheck"))){
				formObject.setLocked("CardDetails_BankName", true);
				formObject.setHeight("Card_Details", 350);
				formObject.setLocked("CardDetails_ChequeNumber", true);
				formObject.setLocked("CardDetails_Amount", true);
				formObject.setLocked("CardDetails_Date", true);
				formObject.setLocked("cmplx_CardDetails_CustClassification", true);
			}
			else {
				formObject.setLocked("CardDetails_BankName", false);
				formObject.setLocked("CardDetails_ChequeNumber", false);
				formObject.setLocked("CardDetails_Amount", false);
				formObject.setLocked("CardDetails_Date", false);
				formObject.setLocked("cmplx_CardDetails_CustClassification", false);

			}
	//		formObject.setLocked("cmplx_CardDetails_CustClassification", false); //hritik PCASI - 3495
			//LoadPickList("CardDetails_Combo5", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_BankName with (nolock)");
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Credit_card_Enq1".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Credit_card_Enq1","Clicked");


			formObject.fetchFragment("Credit_card_Enq1", "CreditCardEnq", "q_CCEnq");
			loadInCreditCardEnq(formObject);
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Case_History1".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Case_History1","Clicked");

			formObject.fetchFragment("Case_History1", "CaseHistoryReport", "q_CaseHist");
			loadInCaseHistoryReport(formObject);

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("LOS1".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("LOS1","Clicked");


			formObject.fetchFragment("LOS1", "LOS", "q_LOS");
			loadInLOS(formObject);
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		//code changes by bandana starts
		if ("Details".equalsIgnoreCase(pEvent.getSource().getName())) {
			PersonalLoanS.mLogger.info( "inside Details ggggggg");
			hm.put("Details","Clicked");
			formObject.fetchFragment("Details", "CC_Loan", "q_CC_Loan");
			//below code added by nikhil 12/3 proc-6160
			LoadPickList("bankName", "select '--Select--'as description,'' as code  union all select convert(varchar, description),code from NG_MASTER_BankName with (nolock) order by code");  
			String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
			 if ("CAD_Analyst_1".equalsIgnoreCase(sActivityName) || "CAD_Analyst_2".equalsIgnoreCase(sActivityName) )
			{
				 formObject.setVisible("totBalTransfer", false);
			}
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();}

			}		
		//code changes by bandana ends
		
		else if ("External_Blacklist".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("External_Blacklist","Clicked");


			formObject.fetchFragment("External_Blacklist", "ExternalBlackList", "q_ExternalBlackList");
			loadInExtBlacklistGrid(formObject);//changed by akshay on 10/1/18
			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

		else if ("Compliance".equalsIgnoreCase(pEvent.getSource().getName())) {
			hm.put("Compliance","Clicked");


			formObject.fetchFragment("Compliance", "Compliance", "q_compliance");

			try
			{
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally{
				hm.clear();
			}
		}

	}
	
	
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 24/05/2021              
	Author                              : Nishkarsh            
	Description                         : Function to insert mail and sms in queue tables   

	 ***********************************************************************************  */
	
	public void insertMessageInQueue(FormReference formObject, String fragName, String email, String sms)
	{
		String mobNo1 = "";
		String emailId = "";
		int framestate1=formObject.getNGFrameState("Alt_Contact_container");
		PersonalLoanS.mLogger.info( "framestate1"+framestate1);
		if(framestate1 != 0){
			PersonalLoanS.mLogger.info( "Inside if framestate1 != 0");
			formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
			formObject.setNGFrameState("Alt_Contact_container", 0);
			mobNo1 = formObject.getNGValue("AlternateContactDetails_MobileNo1");
			emailId = formObject.getNGValue("AlternateContactDetails_EMAIL1_PRI");
			PersonalLoanS.mLogger.info( "Inside if framestate1 != 0 Alt_Contact_container");
		}
		try{
				
			String smsInsertQuery = "INSERT INTO NG_RLOS_SMSQUEUETABLE(Alert_Name, Alert_Code, ALert_Status, Mobile_No, Alert_Text, WI_Name ,Workstep_Name)" 
			               + " values('Takeover','R','"+fragName+"','"+mobNo1+"','"+sms+"','"+formObject.getWFWorkitemName()+"','"+formObject.getWFActivityName()+"')";
			
			
			PersonalLoanS.mLogger.info("Query to insert SMS "+ smsInsertQuery);
			formObject.saveDataIntoDataSource(smsInsertQuery);
				
			}
		catch(Exception e){
			PersonalLoanS.mLogger.info("qException occured in Takeover SMS insert");
		}
		
		try{
			String emailSubject = "Information on Takeover";
			
			
			String mailInsertQuery="insert into WFMAILQUEUETABLE(MAILFROM,MAILTO,MAILSUBJECT,MAILMESSAGE,MAILCONTENTTYPE,ATTACHMENTISINDEX,ATTACHMENTNAMES,ATTACHMENTEXTS,"
					+ "MAILPRIORITY,MAILSTATUS,STATUSCOMMENTS,LOCKEDBY,SUCCESSTIME,LASTLOCKTIME,INSERTEDBY,MAILACTIONTYPE,INSERTEDTIME,PROCESSDEFID,PROCESSINSTANCEID,"
					+ "WORKITEMID,ACTIVITYID,NOOFTRIALS) values('applicationstatus@rakbank.ae','"+emailId+"','"+emailSubject+"','"+email+
					"','text/html;charset=UTF-8',NULL,NULL,NULL,1,'N',NULL,NULL,NULL,NULL,'CUSTOM','TRIGGER',getdate(),1,'"+formObject.getWFWorkitemName()+"',1,1,0);";

			
			PersonalLoanS.mLogger.info("Query to insert email "+ mailInsertQuery);
			formObject.saveDataIntoDataSource(mailInsertQuery);
				
			}
		catch(Exception e){
			PersonalLoanS.mLogger.info("qException occured in Takeover Email insert");
		}
	}
	
	
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 24/05/2021              
	Author                              : Nishkarsh            
	Description                         : Function to get bank name from grid  

	 ***********************************************************************************  */
	
	public String getBankFromTakeoverGrid(FormReference formObject){
		
		String bankName = "";

		PersonalLoanS.mLogger.info("bank row count" + formObject.getLVWRowCount("cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate_table"));
		if(formObject.getLVWRowCount("cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate_table")==1)
			 bankName = formObject.getNGValue("cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate_table",0, 1);
			 
	
		else if(formObject.getLVWRowCount("cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate_table")>1){

			 bankName = formObject.getNGValue("cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate_table",0, 1);
			 
			 for(int i=1;i<formObject.getLVWRowCount("cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate_table");i++)
			 {
				 bankName+=","+formObject.getNGValue("cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate_table",i, 1);
			 }
		}
		PersonalLoanS.mLogger.info("bank name in method getBankFromTakeoverGrid");
		return bankName;
		
	}
	                                                                                 
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         :Function to be called on change Event    

	 ***********************************************************************************  */

	public void value_Change(ComponentEvent pEvent){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//PersonalLoanS.mLogger.info( "EventName:" + pEvent.getType() + "#ControlName#" + pEvent.getSource().getName());
		String Workstep=formObject.getWFActivityName();
		if(Workstep.equals("Reject_Queue"))
		{
			Workstep="Rejected_queue";
		}
		//below code changed by yash on 15/12/2017
		if ("cmplx_Decision_Decision".equalsIgnoreCase(pEvent.getSource().getName())) {
			PersonalLoanS.mLogger.info("Inside change event of Decision");
			//above code changed by yash on 15/12/2017 
			if(formObject.getNGValue("cmplx_Decision_Decision").equalsIgnoreCase("REFER"))
			{
				PersonalLoanS.mLogger.info("Inside REFER decision");
				formObject.setLocked("cmplx_Decision_ReferTo", false);//cmplx_Decision_ReferTo
				//BANDANA code sync referto dropdown start
				//for PCSP-690
				formObject.setNGValue("cmplx_Decision_FeedbackStatus","");
				formObject.setNGValue("cmplx_Decision_ReferTo", "");
				//BANDANA code sync referto dropdown ends
				LoadPickList("cmplx_Decision_ReferTo", "SELECT '--Select--'  union select referTo from ng_MASTER_Decision with (nolock) where ProcessName='PersonalLoanS' and workstepName='"+Workstep+"' and decision='"+formObject.getNGValue("cmplx_Decision_Decision")+"'");
				formObject.setLocked("DecisionHistory_DecisionReasonCode", false);
				if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision"))){
					LoadPickList1("DecisionHistory_DecisionReasonCode", "select description from ng_MASTER_DecisionReason with (nolock) where workstep='"+Workstep+"' and decision='"+formObject.getNGValue("cmplx_Decision_Decision")+"' order by code");
					LoadDecisionSubReason(formObject);
				}
			}
			else if ("FCU".equalsIgnoreCase(formObject.getWFActivityName())||"FPU".equalsIgnoreCase(formObject.getWFActivityName())){
				//cmplx_EmploymentDetails_IndusSeg
				PersonalLoanS.mLogger.info("Inside change event of Decision ss");
				formObject.setNGValue("cmplx_Decision_Feedbackstatus","");
				String decision=formObject.getNGValue("cmplx_Decision_Decision");
				PersonalLoanS.mLogger.info("decision string"+decision);
				PersonalLoanS.mLogger.info("formvalue"+formObject.getNGValue("cmplx_Decision_Decision"));
				PersonalLoanS.mLogger.info("inside feedback status");
				if("Positive".equalsIgnoreCase(decision))
				{	
					PersonalLoanS.mLogger.info("inside positive decision");
					formObject.setNGValue("cmplx_Decision_Feedbackstatus","Positive");
				}
				else if("Negative".equalsIgnoreCase(decision))
				{
					PersonalLoanS.mLogger.info("inside negative decision");
					formObject.setNGValue("cmplx_Decision_Feedbackstatus", "Negative");
					formObject.setNGValue("cmplx_Decision_subfeedback","Incomplete Verification");
		
				}
				//changed by nikhil for PCAS-1815
				LoadpicklistFCU();
			}
			else if(formObject.getNGValue("cmplx_Decision_Decision").equalsIgnoreCase("REJECT") || "Pending for documentation".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")) ||  "Await pending documents".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")))
			{
				PersonalLoanS.mLogger.info("Inside REJECT or PENDING decision");
				//BANDANA code sync referto dropdown start
				formObject.clear("cmplx_Decision_ReferTo");//by sagarika for PCSP-365
				formObject.setLocked("cmplx_Decision_ReferTo", true);//added by akshay on 22/11/18
				//BANDANA code sync referto dropdown ends
				formObject.setLocked("DecisionHistory_DecisionReasonCode", false);
				if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision"))){
					LoadPickList1("DecisionHistory_DecisionReasonCode", "select description from ng_MASTER_DecisionReason with (nolock) where workstep='"+Workstep+"' and decision='"+formObject.getNGValue("cmplx_Decision_Decision")+"' order by code");
					LoadDecisionSubReason(formObject);
					//formObject.setLocked("cmplx_DEC_ReferTo", false);
				}
			}
			else if(formObject.getNGValue("cmplx_Decision_Decision").equalsIgnoreCase("Submit") && "DDVT_maker".equalsIgnoreCase(formObject.getWFActivityName()))
			{
				//BANDANA code sync referto dropdown start
				formObject.clear("cmplx_Decision_ReferTo");//by sagarika for PCSP-365
				formObject.setLocked("cmplx_Decision_ReferTo", true);//added by akshay on 22/11/18
				//BANDANA code sync referto dropdown ends
				formObject.setLocked("DecisionHistory_DecisionReasonCode", true);	
				if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision"))){
					LoadPickList1("DecisionHistory_DecisionReasonCode", "select description from ng_MASTER_DecisionReason with (nolock) where workstep='"+Workstep+"' and decision='"+formObject.getNGValue("cmplx_Decision_Decision")+"' order by code");
					LoadDecisionSubReason(formObject);

				}
				formObject.setLocked("DecisionHistory_DecisionSubReason", true);

			}


			else{
				PersonalLoanS.mLogger.info("Inside else of decision");
				formObject.setVisible("DecisionHistory_Button1", false);
				formObject.clear("cmplx_Decision_ReferTo");//by sagarika for PCSP-365
				formObject.setLocked("cmplx_Decision_ReferTo", true);
				formObject.setLocked("DecisionHistory_DecisionReasonCode", true);
				formObject.setLocked("DecisionHistory_DecisionSubReason", true);
				//formObject.setLocked("cmplx_DEC_ReferTo", true);
			}
		}
		else if("cmplx_EmploymentDetails_ApplicationCateg".equalsIgnoreCase(pEvent.getSource().getName())){
			PersonalLoanS.mLogger.info("Inside app categry ");
			String reqProd = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
			String subprod = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2);
			String appCategory = formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg");

			if("Personal Loan".equalsIgnoreCase(reqProd)){
				PersonalLoanS.mLogger.info("App cat1:"+appCategory);
				if("BAU".equalsIgnoreCase(appCategory)){
					LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subprod+"' and BAU='Y' and (product = 'PL' or product='B') order by code");
					PersonalLoanS.mLogger.info("Inside BAu ");
				}
				else if("S".equalsIgnoreCase(appCategory)){
					LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subprod+"' and Surrogate='Y' and (product = 'PL' or product='B') order by code");
					PersonalLoanS.mLogger.info("Inside S ");
				}
				else{
					LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subprod+"' and (product = 'PL' or product='B') order by code");	
				}
		}
		}
		else if("cmplx_CardDetails_suppcardreq".equalsIgnoreCase(pEvent.getSource().getName())) {
			alignDemographiTab(formObject);
		}
		else if ("cmplx_LoanDisb_AgreementID" .equalsIgnoreCase(pEvent.getSource().getName())) {
			if("Disbursal_Checker".equalsIgnoreCase(formObject.getWFActivityName()) && !"".equalsIgnoreCase(formObject.getNGValue("cmplx_LoanDisb_AgreementID")))
			{
				formObject.setLocked("PostDisbursal_Checklist_Frame1",false);
				formObject.setEnabled("PostDisbursal_Checklist_Frame1",true);
				formObject.setLocked("PostDisbursal_Checklist_Frame2",true);
				formObject.setEnabled("PostDisbursal_Checklist_Frame2",false);
				
				formObject.setLocked("PostDisbursal_Checklist_Frame3",false);
				formObject.setEnabled("PostDisbursal_Checklist_Frame3",true);
				formObject.setLocked("PostDisbursal_Checklist_Frame4",true);
				formObject.setEnabled("PostDisbursal_Checklist_Frame4",false);
			}
			else if("Disbursal_Checker".equalsIgnoreCase(formObject.getWFActivityName()) && "".equalsIgnoreCase(formObject.getNGValue("cmplx_LoanDisb_AgreementID")))
			
			{
				formObject.setLocked("PostDisbursal_Checklist_Frame2",true);
				formObject.setEnabled("PostDisbursal_Checklist_Frame2",false);
				formObject.setLocked("PostDisbursal_Checklist_Frame1",true);
				formObject.setEnabled("PostDisbursal_Checklist_Frame1",false);
				formObject.setLocked("PostDisbursal_Checklist_Frame3",true);
				formObject.setEnabled("PostDisbursal_Checklist_Frame3",false);
				formObject.setLocked("PostDisbursal_Checklist_Frame4",true);
				formObject.setEnabled("PostDisbursal_Checklist_Frame4",false);
			}
			if("PostDisbursal_Maker".equalsIgnoreCase(formObject.getWFActivityName()) && !"".equalsIgnoreCase(formObject.getNGValue("cmplx_LoanDisb_AgreementID")))
			{
				formObject.setLocked("PostDisbursal_Checklist_Frame2",true);
				formObject.setEnabled("PostDisbursal_Checklist_Frame2",false);
				formObject.setLocked("PostDisbursal_Checklist_Frame1",true);
				formObject.setEnabled("PostDisbursal_Checklist_Frame1",false);
				formObject.setLocked("PostDisbursal_Checklist_Frame3",true);
				formObject.setEnabled("PostDisbursal_Checklist_Frame3",false);
				formObject.setLocked("PostDisbursal_Checklist_Frame4",true);
				formObject.setEnabled("PostDisbursal_Checklist_Frame4",false);
			}
			else if("PostDisbursal_Maker".equalsIgnoreCase(formObject.getWFActivityName()) && "".equalsIgnoreCase(formObject.getNGValue("cmplx_LoanDisb_AgreementID")))
			
			{
				formObject.setLocked("PostDisbursal_Checklist_Frame2",true);
				formObject.setEnabled("PostDisbursal_Checklist_Frame2",false);
				formObject.setLocked("PostDisbursal_Checklist_Frame1",false);
				formObject.setEnabled("PostDisbursal_Checklist_Frame1",true);
				formObject.setLocked("PostDisbursal_Checklist_Frame3",true);
				formObject.setEnabled("PostDisbursal_Checklist_Frame3",false);
				formObject.setLocked("PostDisbursal_Checklist_Frame4",false);
				formObject.setEnabled("PostDisbursal_Checklist_Frame4",true);
			}
			if("Document_Checker".equalsIgnoreCase(formObject.getWFActivityName()) && !"".equalsIgnoreCase(formObject.getNGValue("cmplx_LoanDisb_AgreementID")))
			{
				formObject.setLocked("PostDisbursal_Checklist_Frame2",false);
				formObject.setEnabled("PostDisbursal_Checklist_Frame2",true);
				formObject.setLocked("PostDisbursal_Checklist_Frame1",false);
				formObject.setEnabled("PostDisbursal_Checklist_Frame1",true);
				formObject.setLocked("PostDisbursal_Checklist_Frame3",true);
				formObject.setEnabled("PostDisbursal_Checklist_Frame3",false);
				formObject.setLocked("PostDisbursal_Checklist_Frame4",true);
				formObject.setEnabled("PostDisbursal_Checklist_Frame4",false);
			}
			else if("Document_Checker".equalsIgnoreCase(formObject.getWFActivityName()) && "".equalsIgnoreCase(formObject.getNGValue("cmplx_LoanDisb_AgreementID")))
			
			{
				formObject.setLocked("PostDisbursal_Checklist_Frame2",true);
				formObject.setEnabled("PostDisbursal_Checklist_Frame2",false);
				formObject.setLocked("PostDisbursal_Checklist_Frame1",true);
				formObject.setEnabled("PostDisbursal_Checklist_Frame1",false);
				formObject.setLocked("PostDisbursal_Checklist_Frame3",true);
				formObject.setEnabled("PostDisbursal_Checklist_Frame3",false);
				formObject.setLocked("PostDisbursal_Checklist_Frame4",true);
				formObject.setEnabled("PostDisbursal_Checklist_Frame4",false);
			}
		
		
			}
		else if("cmplx_fieldvisit_sp2_drop1".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			if("No".equalsIgnoreCase(formObject.getNGValue("cmplx_fieldvisit_sp2_drop1")))
			{
				formObject.setNGValue("cmplx_fieldvisit_sp2_field_v_time","");
				formObject.setNGValue("cmplx_fieldvisit_sp2_drop3","");
				formObject.setNGValue("cmplx_fieldvisit_sp2_field_rep_receivedDate","");
				formObject.setNGValue("cmplx_fieldvisit_sp2_field_visit_rec_time","");
				formObject.setNGValue("cmplx_fieldvisit_sp2_field_visit_date","");
				formObject.setNGValue("cmplx_fieldvisit_sp2_drop2","");
				formObject.setEnabled("cmplx_fieldvisit_sp2_field_v_time",false);
				formObject.setEnabled("cmplx_fieldvisit_sp2_drop3",false);
				formObject.setEnabled("cmplx_fieldvisit_sp2_field_rep_receivedDate",false);
				formObject.setEnabled("cmplx_fieldvisit_sp2_field_visit_rec_time",false);
				formObject.setEnabled("cmplx_fieldvisit_sp2_field_visit_date",false);
				formObject.setEnabled("cmplx_fieldvisit_sp2_drop2",false);
				formObject.setLocked("cmplx_fieldvisit_sp2_field_v_time",true);
				formObject.setLocked("cmplx_fieldvisit_sp2_drop3",true);
				formObject.setLocked("cmplx_fieldvisit_sp2_field_rep_receivedDate",true);
				formObject.setLocked("cmplx_fieldvisit_sp2_field_visit_rec_time",true);
				formObject.setLocked("cmplx_fieldvisit_sp2_field_visit_date",true);
				formObject.setLocked("cmplx_fieldvisit_sp2_drop2",true);
				
			}
			else{
				formObject.setEnabled("cmplx_fieldvisit_sp2_field_v_time",true);
				formObject.setEnabled("cmplx_fieldvisit_sp2_drop3",true);
				formObject.setEnabled("cmplx_fieldvisit_sp2_field_rep_receivedDate",true);
				formObject.setEnabled("cmplx_fieldvisit_sp2_field_visit_rec_time",true);
				formObject.setEnabled("cmplx_fieldvisit_sp2_field_visit_date",true);
				formObject.setEnabled("cmplx_fieldvisit_sp2_drop2",true);
				formObject.setLocked("cmplx_fieldvisit_sp2_field_v_time",false);
				formObject.setLocked("cmplx_fieldvisit_sp2_drop3",false);
				formObject.setLocked("cmplx_fieldvisit_sp2_field_rep_receivedDate",false);
				formObject.setLocked("cmplx_fieldvisit_sp2_field_visit_rec_time",false);
				formObject.setLocked("cmplx_fieldvisit_sp2_field_visit_date",false);
				formObject.setLocked("cmplx_fieldvisit_sp2_drop2",false);
			}
		}
		//++below code added by bandana for Self-Supp CR" PCSP:2637
		else if("cmplx_CardDetails_SelfSupp_required".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			List<List<String>> Avl_Cards = get_Avl_Cards();
			if(Avl_Cards==null || Avl_Cards.size()==0)
			{
				if("Yes".equalsIgnoreCase(formObject.getNGValue("cmplx_CardDetails_SelfSupp_required")))
				{	
					Refresh_self_supp_data();
					formObject.setNGValue("cmplx_CardDetails_Selected_Card_Product", "--Select--");
					throw new ValidatorException(new FacesMessage(NGFUserResourceMgr_PL.getAlert("VAL113")));
				}
			}
			if(!"Yes".equalsIgnoreCase(formObject.getNGValue("cmplx_CardDetails_SelfSupp_required")))
			{
				Refresh_self_supp_data();
			}
			else
			{
				formObject.setEnabled("cmplx_CardDetails_Selected_Card_Product", true);
				formObject.setEnabled("cmplx_CardDetails_SelfSupp_Limit", true);
				formObject.setEnabled("cmplx_CardDetails_Self_card_embossing", true);
				LoadPickList("cmplx_CardDetails_Selected_Card_Product", "SELECT CASE WHEN temptble.Description!='--Select--' then concat(temptble.Description,'(',temptble.final_limit,')') else temptble.Description end,temptble.Description FROM (SELECT '--Select--' as Description,'' as final_limit union SELECT distinct card_product as Description,final_limit FROM ng_rlos_IGR_Eligibility_CardLimit WITH (nolock) WHERE Child_wi = '"+formObject.getWFWorkitemName()+"' AND cardproductselect = 'true') as temptble order by case when Description='--Select--' then 0 else 1 end");
				if("DDVT_Maker".equalsIgnoreCase(formObject.getWFActivityName()))
				{
					Remove_Self_Card(); 
				}
			}
		}
		else if ("cmplx_CardDetails_Selected_Card_Product".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			if("DDVT_Maker".equalsIgnoreCase(formObject.getWFActivityName()))
			{
				Remove_Self_Card();
				Add_selfcard_to_CRNGrid(formObject.getNGValue("cmplx_CardDetails_Selected_Card_Product"),formObject);
			}
		}
		else if("cmplx_CardDetails_SelfSupp_Limit".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			if("DDVT_Maker".equalsIgnoreCase(formObject.getWFActivityName()))
			{
				updateselfLimit(); 
			}
		}
		if("cmplx_fieldvisit_sp2_drop2".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			try{
			if(formObject.getNGValue("cmplx_fieldvisit_sp2_drop2").equalsIgnoreCase("In house"))
			{
				PersonalLoanS.mLogger.info("inhouse");
			LoadPickList("cmplx_fieldvisit_sp2_drop3","select Description from ng_master_fieldvisit with (nolock) where isActive='N' order by Description DESC");
			formObject.setLocked("cmplx_fieldvisit_sp2_drop3",false);
			}
			else if(formObject.getNGValue("cmplx_fieldvisit_sp2_drop2").equalsIgnoreCase("Agency"))
			{
				PersonalLoanS.mLogger.info("Agency");
				LoadPickList("cmplx_fieldvisit_sp2_drop3","select Description from ng_master_fieldvisit with (nolock) where isActive='Y' order by Description DESC");		
				formObject.setLocked("cmplx_fieldvisit_sp2_drop3",false);
			}
			}
			//cmplx_Customer_EMirateOfVisa
			catch(Exception e){
			//PersonalLoanS.logException(e);
			PersonalLoanS.mLogger.info(" Exception in EMI Generation");
			}
			//cmplx_Customer_Designation	
			/*	if("cmplx_Customer_Designation".equalsIgnoreCase(pEvent.getSource().getName()))
			{
			PersonalLoanS.mLogger.info("sagarika change");
			PersonalLoanS.mLogger.info(formObject.getNGValue("cmplx_Customer_Designation"));
			formObject.getNGValue("cmplx_EmploymentDetails_Designation");
			formObject.setNGValue("cmplx_EmploymentDetails_Designation",formObject.getNGValue("cmplx_Customer_Designation"));
			PersonalLoanS.mLogger.info(formObject.getNGValue("cmplx_EmploymentDetails_Designation"));
			}*/
			//cmplx_Customer_Designation			
		}

		/*if ("cmplx_Customer_DOb".equalsIgnoreCase(pEvent.getSource().getName())){//pcasi-1054

			common.getAge(formObject.getNGValue("cmplx_Customer_DOb"),"cmplx_Customer_age");
		}
		else if ("cmplx_EmploymentDetails_DOJ".equalsIgnoreCase(pEvent.getSource().getName())){

			getAge(formObject.getNGValue("cmplx_EmploymentDetails_DOJ"),"cmplx_EmploymentDetails_LOS");
		}*/             

		else if ("WorldCheck1_Dob".equalsIgnoreCase(pEvent.getSource().getName())){
			PersonalLoanS.mLogger.info("RLOS val change "+ "Value of WorldCheck1_Dob is:"+formObject.getNGValue("WorldCheck1_Dob"));
			//Changes done to auto populate age in world check fragment             
			common.getAge(formObject.getNGValue("WorldCheck1_Dob"),"WorldCheck1_age");
		}

		//code by saurabh on 28th Nov 2017 for calculating EMI after change in interest rate or tenor
		else if("cmplx_EligibilityAndProductInfo_PLHidden".equalsIgnoreCase(pEvent.getSource().getName()) || "cmplx_EligibilityAndProductInfo_InterestRate".equalsIgnoreCase(pEvent.getSource().getName()) || "cmplx_EligibilityAndProductInfo_Tenor".equalsIgnoreCase(pEvent.getSource().getName())){
			try{
				//by shweta EMI should not change at CAD WS
					//double LoanAmount=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
				
				if(!(Workstep.equalsIgnoreCase("CAD_ANALYST1") || Workstep.equalsIgnoreCase("CAD_ANALYST2")) ){	
					double LoanAmount=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_PLHidden")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_PLHidden").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_PLHidden"));
					double tenor=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor"));
					double RateofInt=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate"));

					int mono_days=Integer.parseInt(formObject.getNGValue("cmplx_LoanDetails_moratorium")==null||formObject.getNGValue("cmplx_LoanDetails_moratorium").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_LoanDetails_moratorium"));
					
				/*	String EMI=getEMI(LoanAmount,RateofInt,tenor,mono_days);
					formObject.setNGValue("cmplx_EligibilityAndProductInfo_EMI", EMI==null||EMI.equalsIgnoreCase("")?"0.00":EMI,false);
				*/
				}
			}
			catch(Exception e){
				//PersonalLoanS.logException(e);
				PersonalLoanS.mLogger.info(" Exception in EMI Generation");
			}
		}
		//code by akshay on 2/5/18 for proc 8661
		else if( "cmplx_LoanDetails_lonamt".equalsIgnoreCase(pEvent.getSource().getName())){
			PersonalLoanS.mLogger.info("On Change cmplx_LoanDetails_lonamt");
			
			try
			{
				//double 	LPF_amount;
				double LPF_amount_new = 0;
				double 	insur_amount;
				double final_Loan_amount = Double.parseDouble(formObject.getNGValue("cmplx_LoanDetails_lonamt"));
				//PersonalLoanS.mLogger.info("final_Loan_amount :: "+final_Loan_amount);
				//double lpf_percent = Double.parseDouble(formObject.getNGValue("cmplx_LoanDetails_lpf"));
				double insur_percent=Double.parseDouble(formObject.getNGValue("cmplx_LoanDetails_insur"));
				//PersonalLoanS.mLogger.info("insur_percent :: "+insur_percent);
				insur_amount=(final_Loan_amount*insur_percent)/100;
				//PersonalLoanS.mLogger.info("insur_amount 1 :: "+insur_amount);
				insur_amount = ((double)(Math.round((insur_amount*100)))/100);
				//PersonalLoanS.mLogger.info("insur_amount 2 :: "+insur_amount);
				/*LPF_amount = (final_Loan_amount*lpf_percent)/100;
				LPF_amount = ((double)(Math.round((LPF_amount*100)))/100);
				double LoanDetails_MaxLPF=Double.parseDouble(formObject.getNGValue("LoanDetails_MaxLPF"));
				double LoanDetails_MinLPF=Double.parseDouble(formObject.getNGValue("LoanDetails_MinLPF"));

				formObject.setNGValue("LoanDetails_loanamt",formObject.getNGValue("cmplx_LoanDetails_lonamt"));
				formObject.setNGValue("LoanDetails_amt", formObject.getNGValue("cmplx_LoanDetails_lonamt"));
				if(LPF_amount> LoanDetails_MaxLPF)
					LPF_amount=LoanDetails_MaxLPF;
				else if (LPF_amount<LoanDetails_MinLPF)
					LPF_amount=LoanDetails_MinLPF;*/

				//PCASI - 3647
				try{
					LPF_amount_new = 0.01 * final_Loan_amount;
					if(LPF_amount_new < 500)
						LPF_amount_new = 500;
					else if(LPF_amount_new > 2500)
						LPF_amount_new = 2500;
					PersonalLoanS.mLogger.info("LPF_amount new Loan grid :: "+LPF_amount_new);
					//formObject.setNGValue("cmplx_LoanDetails_lpfamt", LPF_amount);//PCASI - 3647
					formObject.setNGValue("cmplx_LoanDetails_lpfamt", LPF_amount_new);
					PersonalLoanS.mLogger.info("After setting value :: "+formObject.getNGValue("cmplx_LoanDetails_lpfamt"));
				}
				catch(Exception e){
					PersonalLoanS.mLogger.info("Exception in loan amount :: "+e+"\n"+e.getStackTrace());
				}
				PersonalLoanS.mLogger.info("insur_percent :: "+insur_percent);
				formObject.setNGValue("cmplx_LoanDetails_insuramt", insur_amount);


				float lpfVatAmt=0;
				float insuranceVatAmt=0;

				try{
					lpfVatAmt=(Float.parseFloat(formObject.getNGValue("cmplx_LoanDetails_LoanProcessingVATPercent"))*Float.parseFloat(formObject.getNGValue("cmplx_LoanDetails_lpfamt")))/100f;
					insuranceVatAmt=(Float.parseFloat(formObject.getNGValue("cmplx_LoanDetails_InsuranceVATPercent"))*Float.parseFloat(formObject.getNGValue("cmplx_LoanDetails_insuramt")))/100f;
					lpfVatAmt = ((float)(Math.round((lpfVatAmt*100)))/100);
					insuranceVatAmt = ((float)(Math.round((insuranceVatAmt*100)))/100);

				}catch(Exception ex){
					PersonalLoanS.mLogger.info("Exception in change event for Loan amt in Loan details tab :: "+ex);
				}


				formObject.setNGValue("cmplx_LoanDetails_LoanProcessingVat",lpfVatAmt);
				formObject.setNGValue("cmplx_LoanDetails_InsuranceVat", insuranceVatAmt);
				formObject.setNGValue("cmplx_LoanDetails_amt", LPF_amount_new+Float.parseFloat(formObject.getNGValue("cmplx_LoanDetails_insuramt"))+lpfVatAmt+insuranceVatAmt);//added by akshay for proc 9667
				double LoanAmount=Double.parseDouble(formObject.getNGValue("cmplx_LoanDetails_lonamt")==null||formObject.getNGValue("cmplx_LoanDetails_lonamt").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_LoanDetails_lonamt"));
				double tenor=Double.parseDouble(formObject.getNGValue("cmplx_LoanDetails_tenor")==null||formObject.getNGValue("cmplx_LoanDetails_tenor").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_LoanDetails_tenor"));
				double RateofInt=Double.parseDouble(formObject.getNGValue("cmplx_LoanDetails_netrate")==null||formObject.getNGValue("cmplx_LoanDetails_netrate").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_LoanDetails_netrate"));
				PersonalLoanS.mLogger.info("Calculating EMI onchange of Tenor"+LoanAmount+ "  " + tenor+"  "+"  "+RateofInt);
				int mono_days=Integer.parseInt(formObject.getNGValue("cmplx_LoanDetails_moratorium")==null||formObject.getNGValue("cmplx_LoanDetails_moratorium").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_LoanDetails_moratorium"));
				
			/*	String EMI=getEMI(LoanAmount,RateofInt,tenor,mono_days);
				formObject.setNGValue("cmplx_LoanDetails_loanemi", EMI==null||EMI.equalsIgnoreCase("")?"0.00":EMI);
				formObject.setNGValue("cmplx_EligibilityAndProductInfo_EMI", EMI==null||EMI.equalsIgnoreCase("")?"0.00":EMI);	//PCASI 3524
			*/
			}
			catch(Exception ex)
			{
				PersonalLoanS.mLogger.info(ex);
			}
			PersonalLoanS.mLogger.info("On Change end cmplx_LoanDetails_lonamt");	
		}
		else if( "cmplx_LoanDetails_netrate".equalsIgnoreCase(pEvent.getSource().getName())|| "cmplx_LoanDetails_tenor".equalsIgnoreCase(pEvent.getSource().getName()) ){
			try{
				
				double LoanAmount=Double.parseDouble(formObject.getNGValue("cmplx_LoanDetails_lonamt")==null||formObject.getNGValue("cmplx_LoanDetails_lonamt").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_LoanDetails_lonamt"));
				double tenor=Double.parseDouble(formObject.getNGValue("cmplx_LoanDetails_tenor")==null||formObject.getNGValue("cmplx_LoanDetails_tenor").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_LoanDetails_tenor"));
				double RateofInt=Double.parseDouble(formObject.getNGValue("cmplx_LoanDetails_netrate")==null||formObject.getNGValue("cmplx_LoanDetails_netrate").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_LoanDetails_netrate"));
				PersonalLoanS.mLogger.info("Calculating EMI onchange of Tenor"+LoanAmount+ "  " + tenor+"  "+"  "+RateofInt);
				int mono_days=Integer.parseInt(formObject.getNGValue("cmplx_LoanDetails_moratorium")==null||formObject.getNGValue("cmplx_LoanDetails_moratorium").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_LoanDetails_moratorium"));
				
			/*	String EMI=getEMI(LoanAmount,RateofInt,tenor,mono_days);
				formObject.setNGValue("cmplx_LoanDetails_loanemi", EMI==null||EMI.equalsIgnoreCase("")?"0.00":EMI);
				formObject.setNGValue("cmplx_EligibilityAndProductInfo_EMI", EMI==null||EMI.equalsIgnoreCase("")?"0.00":EMI);	//PCASI 3524
				*/
			}
			catch(Exception e){
				PersonalLoanS.mLogger.info(" Exception in EMI Generation");
				printException(e);
			}
		}

		else if ("ReqProd".equalsIgnoreCase(pEvent.getSource().getName())){
			int prd_count=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			if(prd_count>0){
				String ReqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
				loadPicklistProduct(ReqProd);
			}
		}

		else if ("SubProd".equalsIgnoreCase(pEvent.getSource().getName())){
			PersonalLoanS.mLogger.info("PL val change "+ "Value of SubProd is:"+formObject.getNGValue("SubProd"));
			//formObject.clear("AppType");
			//formObject.setNGValue("AppType","--Select--");--commented on 3/1/18 by akshay
			if(NGFUserResourceMgr_PL.getGlobalVar("PL_BusinessTitaniumCard").equalsIgnoreCase(formObject.getNGValue("SubProd"))){
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='BTC'");
				//formObject.setNGValue("EmpType",NGFUserResourceMgr_PL.getGlobalVar("PL_SelfEmployed"));//Tarang to be removed on friday(1/19/2018)
				formObject.setNGValue("EmploymentType",NGFUserResourceMgr_PL.getGlobalVar("PL_SelfEmployed"));
			}              
			else if(NGFUserResourceMgr_PL.getGlobalVar("PL_InstantMoney").equalsIgnoreCase(formObject.getNGValue("SubProd")))
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType  with (nolock) where subProdFlag='IM'");

			else if(NGFUserResourceMgr_PL.getGlobalVar("PL_LimitIncrease").equalsIgnoreCase(formObject.getNGValue("SubProd")))
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='LI'");

			else if(NGFUserResourceMgr_PL.getGlobalVar("PL_SalariedCreditCard").equalsIgnoreCase(formObject.getNGValue("SubProd")))
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='SAL'");

			else if(NGFUserResourceMgr_PL.getGlobalVar("PL_SelfEmployed").equalsIgnoreCase(formObject.getNGValue("SubProd")))
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='SE'");

			else if(NGFUserResourceMgr_PL.getGlobalVar("PL_ExpatPersonalLoans").equalsIgnoreCase(formObject.getNGValue("SubProd")))
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='EXP'");

			else if(NGFUserResourceMgr_PL.getGlobalVar("PL_NationalPersonalLoans").equalsIgnoreCase(formObject.getNGValue("SubProd")))
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='NAT'");

			else if(NGFUserResourceMgr_PL.getGlobalVar("PL_Pre-Approved").equalsIgnoreCase(formObject.getNGValue("SubProd")))
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='PA'");

			else if(NGFUserResourceMgr_PL.getGlobalVar("PL_ProductUpgrade").equalsIgnoreCase(formObject.getNGValue("SubProd")))
				LoadPickList("AppType", "select '--Select--' union select convert(varchar, description) from ng_master_ApplicationType with (nolock) where subProdFlag='PU'");
			formObject.setNGValue("AppType","--Select--");
		}
		//changed by akshay on 8th nov 2017 for loadpicklist.
		else if ("cmplx_EmploymentDetails_EmpIndusSector".equalsIgnoreCase(pEvent.getSource().getName()) && "true".equals(formObject.getNGValue("cmplx_EmploymentDetails_Others"))){

			PersonalLoanS.mLogger.info( "$Indus Sector$:" +formObject.getNGValue("cmplx_EmploymentDetails_EmpIndusSector"));
			formObject.setLocked("cmplx_EmploymentDetails_Indus_Macro", false);//it is unlocked from js but its instance state is saved as locked as it was locked on fragment load
			LoadPickList("cmplx_EmploymentDetails_Indus_Macro", "Select macro from (select '--Select--' as macro union select macro from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where IndustrySector='"+formObject.getNGValue("cmplx_EmploymentDetails_EmpIndusSector")+"' and IsActive='Y') new_table order by case  when macro ='--Select--' then 0 else 1 end");
		}

		else if ("cmplx_EmploymentDetails_Indus_Macro".equalsIgnoreCase(pEvent.getSource().getName()) && "true".equals(formObject.getNGValue("cmplx_EmploymentDetails_Others"))){
			PersonalLoanS.mLogger.info( "$Indus Macro$:" +formObject.getNGValue("cmplx_EmploymentDetails_Indus_Macro"));
			formObject.setLocked("cmplx_EmploymentDetails_Indus_Micro", false);
			LoadPickList("cmplx_EmploymentDetails_Indus_Micro", "Select micro from (select '--Select--' as micro union select  micro from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where Macro='"+formObject.getNGValue("cmplx_EmploymentDetails_Indus_Macro")+"' and IndustrySector='"+formObject.getNGValue("cmplx_EmploymentDetails_EmpIndusSector")+"' and IsActive='Y' ) new_table order by case  when micro ='--Select--' then 0 else 1 end");
		}

		//Added by Arun (21/09/17)
		else if ("NotepadDetails_notedesc".equalsIgnoreCase(pEvent.getSource().getName())){
			String notepad_desc = formObject.getNGValue("NotepadDetails_notedesc");
			//PCASI-3154
			if("Disbursal_Maker".equalsIgnoreCase(Workstep) || "Disbursal_Checker".equalsIgnoreCase(Workstep)){
				PersonalLoanS.mLogger.info("Inside Broker Code changes in wrkstep :: "+Workstep);
				formObject.setVisible("NotepadDetails_BrokerCode", true);
				formObject.setVisible("NotepadDetails_Label12", true);
				String sQuery = "select code,workstep,brokerCode from ng_master_notedescription with (nolock) where Description='" +  notepad_desc + "'";
				PersonalLoanS.mLogger.info(" query is "+sQuery);
				List<List<String>> recordList = formObject.getDataFromDataSource(sQuery);
				if( !recordList.isEmpty() && recordList.get(0).get(0)!= null)
				{
					PersonalLoanS.mLogger.info("inside notepad details grid :: "+recordList.get(0));
					formObject.setNGValue("NotepadDetails_notecode",recordList.get(0).get(0));
					formObject.setNGValue("NotepadDetails_Workstep",recordList.get(0).get(1));
					formObject.setNGValue("NotepadDetails_BrokerCode",recordList.get(0).get(2));
				}
			
			}
			else{
				String sQuery = "select code,workstep,brokerCode from ng_master_notedescription with (nolock) where Description='" +  notepad_desc + "'";
				PersonalLoanS.mLogger.info(" query is "+sQuery);
				List<List<String>> recordList = formObject.getDataFromDataSource(sQuery);
				//added by shweta for PCSP-857
				if( !recordList.isEmpty() && recordList.get(0).get(0)!= null)
				{
					PersonalLoanS.mLogger.info("inside notepad details grid :: "+recordList.get(0));
					formObject.setNGValue("NotepadDetails_notecode",recordList.get(0).get(0));	//3154
					formObject.setNGValue("NotepadDetails_Workstep",recordList.get(0).get(1));
					String brokerCode = recordList.get(0).get(2);
					if(brokerCode != null || !"".equalsIgnoreCase(brokerCode)){
						formObject.setNGValue("NotepadDetails_BrokerCode",recordList.get(0).get(2));
					}
				}
			}
			formObject.setNGValue("NotepadDetails_user",formObject.getUserId()+"-"+formObject.getUserName());

		}
		else if ("cmplx_Decision_Feedbackstatus".equalsIgnoreCase(pEvent.getSource().getName()) && ("FCU".equalsIgnoreCase(formObject.getWFActivityName())||"FPU".equalsIgnoreCase(formObject.getWFActivityName()))){
				String decision=formObject.getNGValue("cmplx_Decision_Decision");
				if("Positive".equalsIgnoreCase(decision))
				{
					PersonalLoanS.mLogger.info("inside positive decision");
					formObject.setNGValue("cmplx_DEC_FeedbackStatus","Positive");
				}
				else if("Negative".equalsIgnoreCase(decision))
				{
					PersonalLoanS.mLogger.info("inside negative decision");
					formObject.setNGValue("cmplx_DEC_FeedbackStatus", "Negative");
				} else{
				 LoadpicklistFCU();
				}
		}
else if ("cmplx_Decision_subfeedback".equalsIgnoreCase(pEvent.getSource().getName()) && ("FCU".equalsIgnoreCase(formObject.getWFActivityName()) ||"FPU".equalsIgnoreCase(formObject.getWFActivityName()))){
			
	PersonalLoanS.mLogger.info("inside feedback cmplx_Decision_Feedbackstatus");
			if ("Fraud".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_subfeedback"))){
				LoadPickList("cmplx_Decision_ALOCcompany", "Select Description from ng_master_fraudsubreason with (nolock) where isactive='Y'");
				formObject.setLocked("cmplx_Decision_ALOCcompany", false);

			}
			else{
				LoadPickList("cmplx_Decision_ALOCcompany", "Select Description from ng_master_fraudsubreason with (nolock) where isactive='N'");
				formObject.setLocked("cmplx_Decision_ALOCcompany", true);

			}
			PersonalLoanS.mLogger.info("inside feedback status after loadpicklist");

		}
	

		else if (pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_Modify"))
		{
			formObject.ExecuteExternalCommand("NGDeleteRow", "Decision_ListView1");
			PersonalLoanS.mLogger.info("hey this is decison modify button test");
			try{
				if("DDVT_Maker".equalsIgnoreCase(formObject.getWFActivityName())){
					ddvtmaker_submitapp();
				}
				String decision=formObject.getNGValue("cmplx_Decision_Decision");
				String ReferTo=formObject.getNGValue("cmplx_Decision_ReferTo");
				String currDate=common.Convert_dateFormat("", "","dd/MM/yyyy HH:mm");
				String s=formObject.getWFGeneralData();
				String Cifid=formObject.getNGValue("cmplx_Customer_CIFNO");
				String emiratesid=formObject.getNGValue("cmplx_Customer_EmiratesID");
				String custName=formObject.getNGValue("CustomerLabel");
				//formObject.setNGValue("ReferTo", ReferTo);
				int startPosition = s.indexOf("<EntryDateTime>") + "<EntryDateTime>".length();
				int endPosition = s.indexOf("</EntryDateTime>", startPosition);
				String subS = s.substring(startPosition, endPosition-4);
				
				List<String> DecisionCodelist=new ArrayList<String>();
				String DecisionCode=formObject.getNGValue("DecisionHistory_DecisionReasonCode");
				String DecisionSubCode=formObject.getNGValue("DecisionHistory_DecisionSubReason");

				PersonalLoanS.mLogger.info("PL_Common"+ "DecisionReasonCode: "+DecisionCode);
				DecisionCodelist.clear();
				DecisionCodelist.add(currDate);
				DecisionCodelist.add(formObject.getUserName());
				DecisionCodelist.add(formObject.getWFActivityName());
				DecisionCodelist.add(decision);
				DecisionCodelist.add(formObject.getNGValue("cmplx_Decision_REMARKS"));
				DecisionCodelist.add(formObject.getWFWorkitemName());
				DecisionCodelist.add(null==ReferTo?"":ReferTo);
				DecisionCodelist.add(subS);
				DecisionCodelist.add(DecisionCode);
				DecisionCodelist.add(Cifid);
				DecisionCodelist.add(emiratesid);
				DecisionCodelist.add(custName);
				DecisionCodelist.add("");
				DecisionCodelist.add(DecisionSubCode);
				formObject.addItemFromList("Decision_ListView1",DecisionCodelist);
					PersonalLoanS.mLogger.info("hey we testing modify on fpu"+DecisionCodelist);
				
				ReferTo = setReferto();
				if("Cad_Analyst2".equalsIgnoreCase(formObject.getWFActivityName())||"DDVT_Checker".equalsIgnoreCase(formObject.getWFActivityName())){
					String query="";
					if("Refer".equalsIgnoreCase(decision)){
						query = "update NG_PL_EXTTABLE set ReferTo = '"+ReferTo+"' where PL_Wi_Name = '"+formObject.getWFWorkitemName()+"'";
					}
					else{
						query = "update NG_PL_EXTTABLE set ReferTo = '' where PL_Wi_Name = '"+formObject.getWFWorkitemName()+"'";
					}
	
					formObject.saveDataIntoDataSource(query);
				}
			}
			catch(Exception ex)
			{//formObject.RaiseEvent("WFSave");
				PersonalLoanS.mLogger.info("PL_Common ReferList Exception is:");
				printException(ex);
			}


			
			

		}

		// Below code added by nikhil 13/11/2017 for Code merge

		else if (pEvent.getSource().getName().equalsIgnoreCase("Decision_Combo2")) {
			if(formObject.getWFActivityName().equalsIgnoreCase("CAD_Analyst1"))	
			{
				formObject.setNGValue("CAD_dec", formObject.getNGValue("Decision_Combo2"));
				PersonalLoanS.mLogger.info(" In PL_Initiation VALChanged---New Value of CAD_dec is: "+ formObject.getNGValue("Decision_Combo2"));
			}

			else{

				formObject.setNGValue("decision", formObject.getNGValue("Decision_Combo2"));
				PersonalLoanS.mLogger.info(" In PL_Initiation VALChanged---New Value of decision is: "+ formObject.getNGValue("Decision_Combo2"));
			}
		}
		//--Above code added by nikhil 13/11/2017 for Code merge
		//below code added by nikhil 08/12/17
		else if("AlternateContactDetails_carddispatch".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			if(formObject.getNGValue("AlternateContactDetails_carddispatch").equalsIgnoreCase("998"))
			{
				formObject.setNGValue("Card_Dispatch_Option","Courier");

			}
			else
			{
				formObject.setNGValue("Card_Dispatch_Option","Branch");
			}
		}
		else if("cmplx_OrigVal_ovdec".equalsIgnoreCase(pEvent.getSource().getName())){
			PersonalLoanS.mLogger.info("Inside cmplx_OrigVal_ovdec value change");
			IRepeater rObj=null;
			rObj=formObject.getRepeaterControl("OriginalValidation_Frame");
			int row=rObj.getChangeIndex();
			PersonalLoanS.mLogger.info("selected row is:"+row);
			rObj.setValue(row,6,formObject.getUserName());
		}

		/*else if("cmplx_LoanDetails_lpf".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			try
			{
				double 	LPF_amount;
				double final_Loan_amount = Double.parseDouble(formObject.getNGValue("cmplx_LoanDetails_lonamt"));
				double lpf_percent = Double.parseDouble(formObject.getNGValue("cmplx_LoanDetails_lpf"));
				double lpfVatAmt = 0.0;
				double insuranceVatAmt = 0.0;
				try{
					lpfVatAmt = Double.parseDouble(formObject.getNGValue("cmplx_LoanDetails_LoanProcessingVat"));
					insuranceVatAmt = Double.parseDouble(formObject.getNGValue("cmplx_LoanDetails_InsuranceVat"));
				}catch(Exception ex){
					PersonalLoanS.mLogger.info(ex.getStackTrace());
				}
				LPF_amount = (final_Loan_amount*lpf_percent)/100;
				PersonalLoanS.mLogger.info("RLOS_Common code "+ "result LPF_amount: "+LPF_amount);
				formObject.setNGValue("cmplx_LoanDetails_lpfamt", LPF_amount);
				formObject.setNGValue("cmplx_LoanDetails_amt", LPF_amount+Float.parseFloat(formObject.getNGValue("cmplx_LoanDetails_insuramt"))+lpfVatAmt+insuranceVatAmt);//added by akshay for proc 9667
			}
			catch(Exception ex)
			{
				PersonalLoanS.mLogger.info(ex.getStackTrace());
			}
		}*/
		else if("DecisionHistory_DecisionReasonCode".equalsIgnoreCase(pEvent.getSource().getName())){
			LoadDecisionSubReason(formObject);
		}
		else if("cmplx_Decision_ReferTo".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			//CreditCard.mLogger.info("inside mouse clicked for refr casde :::");
			String refer=formObject.getNGValue("cmplx_Decision_ReferTo");
			//formObject.setNGValue("ReferTo", refer);
			//below code for PROC - 11338
			String Work_step=formObject.getWFActivityName();
			if(Work_step.equals("Reject_Queue"))
			{
				Work_step="Rejected_queue";
			}
			//removed by shweta			
			LoadPickList1("DecisionHistory_DecisionReasonCode", "select description from ng_MASTER_DecisionReason with (nolock) where workstep='"+Work_step+"' and decision='"+formObject.getNGValue("cmplx_Decision_Decision")+"' and (ReferTo='"+refer+"' or ReferTo='') and (ProcessName='PersonalLoanS' or  ProcessName='ALL') order by code");			
			formObject.setNGValue("q_MailSubject", refer);
			LoadDecisionSubReason(formObject);

			

		}
		else if ("cmplx_EmploymentVerification_FiledVisitedInitiated_value".equalsIgnoreCase(pEvent.getSource().getName())){
			PersonalLoanS.mLogger.info( "$Indus Macro$:" +formObject.getNGValue("cmplx_EmploymentVerification_FiledVisitedInitiated_value"));
			
			LoadPickList("cmplx_EmploymentVerification_FiledVisitedInitiated_updates", "select description from ng_master_fieldvisitDoneupdates with (nolock) where fieldvisitdonevalues='"+formObject.getNGValue("cmplx_EmploymentVerification_FiledVisitedInitiated_value")+"'");
		}
		//beow code by saurabh for new incoming doc.
		else if("IncomingDocNew_DocType".equalsIgnoreCase(pEvent.getSource().getName())) {
			String docType = formObject.getNGValue("IncomingDocNew_DocType");
			String requested_product;
			String requested_subproduct;
			String application_type;
			String product_type;
			product_type= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 0);
			PersonalLoanS.mLogger.info(product_type);
			requested_product= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 1);

			PersonalLoanS.mLogger.info(requested_product);
			requested_subproduct= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2);
			PersonalLoanS.mLogger.info(requested_subproduct);
			application_type= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 4);
			PersonalLoanS.mLogger.info(application_type);
			PersonalLoanS.mLogger.info(requested_product);
			String query="";
			//changed by nikhil as loadpicklist not working
			if("Personal Loan".equalsIgnoreCase(requested_product)){

				query="SELECT DISTINCT docname FROM ng_rlos_DocTable WHERE DocType = '"+docType+"' and (ProductName = '"+requested_product+"'  or ProductName='All') and (SubProductName = '"+requested_subproduct+"'  or SubProductName='All') and Active = 'Y' and (Designation='"+formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")+"' or Designation='All') ";
				PersonalLoanS.mLogger.info( "when row count is  zero inside if"+query);
				LoadPickList("IncomingDocNew_DocName", query);

					}
			else{
				//Query corrected by Deepak.
				//change in queries by Saurabh on 4th Jan 19.
				//chnage by saurabh on 8th Jan
				String targetsegCode="";
				String empTypeDoc = "";
				if("Salaried".equalsIgnoreCase(formObject.getNGValue("EmploymentType"))){
					targetsegCode = formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode");
					empTypeDoc = "SAL";
				}
				else{
					int rowCount = formObject.getLVWRowCount("cmplx_CompanyDetails_cmplx_CompanyGrid");
					for(int i=0;i<rowCount;i++){
						if("Secondary".equalsIgnoreCase(formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",i,2))){
							targetsegCode = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",i,22);
							empTypeDoc="SE";
						}
					}
				}
				PersonalLoanS.mLogger.info("EmploymentType: "+formObject.getNGValue("EmploymentType") );
				PersonalLoanS.mLogger.info("TagertSegmentCode: "+targetsegCode );
				String nationality = formObject.getNGValue("cmplx_Customer_Nationality");

				if("CAC".equalsIgnoreCase(targetsegCode)){
					//query="SELECT docname FROM ng_rlos_DocTable WHERE DocType = '"+docType+"' and ProductName = '"+requested_product+"' and SubProductName = '"+requested_subproduct+"' and Active = 'Y' and Designation='CAC' ";
					query="SELECT docname FROM ng_rlos_DocTable WHERE DocType = '"+docType+"' and (ProductName = '"+requested_product+"'  or ProductName='All') and (SubProductName = '"+requested_subproduct+"'  or SubProductName='All') and Active = 'Y' and (Designation='CAC' or Designation='All') ";
				}
				else if("DOC".equalsIgnoreCase(targetsegCode)){
					//query="SELECT docname FROM ng_rlos_DocTable WHERE DocType = '"+docType+"' and ProductName = '"+requested_product+"' and SubProductName = '"+requested_subproduct+"' and Active = 'Y' and Designation='DOC' ";
					query="SELECT docname FROM ng_rlos_DocTable WHERE DocType = '"+docType+"' and (ProductName = '"+requested_product+"'  or ProductName='All') and (SubProductName = '"+requested_subproduct+"'  or SubProductName='All') and Active = 'Y' and (Designation='DOC' or Designation='All') ";
				}
				else if("EMPID".equalsIgnoreCase(targetsegCode)){
					//query="SELECT docname FROM ng_rlos_DocTable WHERE DocType = '"+docType+"' and ProductName = '"+requested_product+"' and SubProductName = '"+requested_subproduct+"' and Active = 'Y' and Designation='EMPID' ";
					query="SELECT docname FROM ng_rlos_DocTable WHERE DocType = '"+docType+"' and (ProductName = '"+requested_product+"'  or ProductName='All') and (SubProductName = '"+requested_subproduct+"'  or SubProductName='All') and Active = 'Y' and (Designation='EMPID' or Designation='All') ";
				}
				else if("NEPALO".equalsIgnoreCase(targetsegCode) || "NEPNAL".equalsIgnoreCase(targetsegCode)){
					//query="SELECT docname FROM ng_rlos_DocTable WHERE DocType = '"+docType+"' and ProductName = '"+requested_product+"' and SubProductName = '"+requested_subproduct+"' and Active = 'Y' and Designation='NEP' ";
					query="SELECT docname FROM ng_rlos_DocTable WHERE DocType = '"+docType+"' and (ProductName = '"+requested_product+"'  or ProductName='All') and (SubProductName = '"+requested_subproduct+"'  or SubProductName='All') and Active = 'Y' and (Designation='NEP' or Designation='All') ";
				}
				else if("VIS".equalsIgnoreCase(targetsegCode)){
					query="SELECT docname FROM ng_rlos_DocTable WHERE DocType = '"+docType+"' and (ProductName = '"+requested_product+"'  or ProductName='All') and (SubProductName = '"+requested_subproduct+"'  or SubProductName='All') and Active = 'Y' and (Designation='VC' or Designation='All') ";
				}
				else{

					query="SELECT DISTINCT docname FROM ng_rlos_DocTable WHERE DocType = '"+docType+"' and (ProductName = '"+requested_product+"'  or ProductName='All') and (SubProductName = '"+requested_subproduct+"'  or SubProductName='All') and Active = 'Y' and (Designation='"+targetsegCode+"' or Designation='"+empTypeDoc+"' or Designation='All') ";
				}


				PersonalLoanS.mLogger.info( "when row count is  zero inside else"+query);
				LoadPickList("IncomingDocNew_DocName", query);



			}


			//LoadPickList("IncomingDocNew_DocName", "SELECT docname FROM ng_rlos_DocTable WHERE DocType = '"+docType+"' and ((ProductName = '"+requested_product+"' and SubProductName = '"+requested_subproduct+"') or ProductName='All') and Active = 'Y' and (Designation='VC' or Designation='All') and (Nationality='"+nationality+"' or Nationality='All') ");
			formObject.setNGValue("IncomingDocNew_mandatory", "");
		}
		
		else if("cmplx_Customer_RM_TL_NAME".equalsIgnoreCase(pEvent.getSource().getName()) 
				&& ("DDVT_maker".equalsIgnoreCase(formObject.getWFActivityName()) || "Disbursal_Maker".equalsIgnoreCase(formObject.getWFActivityName()))){
			PersonalLoanS.mLogger.info("Inside change event for RM TL Name");
			String rmtlname = formObject.getNGValue("cmplx_Customer_RM_TL_NAME");
			if(rmtlname != ""){
				formObject.setNGValue("RM_Name", rmtlname);
				formObject.setNGValue("RmTlNameLabel", rmtlname);
			}
			PersonalLoanS.mLogger.info( "RM_Name :: "+formObject.getNGValue("RM_Name"));
		}
		else if("cmplx_Decision_Decision".equalsIgnoreCase(pEvent.getSource().getName()) && "DDVT_Checker".equalsIgnoreCase(Workstep)){
			PersonalLoanS.mLogger.info("Inside change event on DDVT Checker for decision");
			float riskRating =0;
			String queryForComplianceCount = "";
			int countFromQuery = 0;
			String ddvtc_dec = formObject.getNGValue("cmplx_Decision_Decision");

			try{
				formObject.fetchFragment("Risk_Rating", "RiskRating", "q_riskrating");
				riskRating = Float.parseFloat(formObject.getNGValue("cmplx_RiskRating_Total_riskScore"));
				queryForComplianceCount = "select count(*) from NG_RLOS_GR_DECISION where dec_wi_name = '"+formObject.getWFActivityName()+"' and workstepName = 'Compliance' and Decision = 'Approve'";
				PersonalLoanS.mLogger.info("queryForComplianceCount :: "+queryForComplianceCount);
				List<List<String>> queryResp = formObject.getDataFromDataSource(queryForComplianceCount);
				PersonalLoanS.mLogger.info("queryResp size :: "+queryResp.size());
				if(queryResp.size() != 0){
					countFromQuery = Integer.parseInt(queryResp.get(0).get(0));
					PersonalLoanS.mLogger.info("countFromQuery :: "+countFromQuery);
				}
			}
			catch(Exception e){
				PersonalLoanS.mLogger.info("Exception in high risk case, value change :: "+e);
			}

			if(countFromQuery <= 0 && riskRating >= 4 && "Approve".equalsIgnoreCase(ddvtc_dec)){
				String alert_msg=NGFUserResourceMgr_PL.getAlert("VAL1003");
				PersonalLoanS.mLogger.info("High risk case, direct to compliance :: "+alert_msg);
				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			PersonalLoanS.mLogger.info("End of value change");
		}
		
		
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to be called on Click Event     

	 ***********************************************************************************  */

	public void mouse_Clicked(ComponentEvent pEvent){

		PersonalLoanS.mLogger.info( "PersonalLoanSCommonCode--->Inside mouse_Clicked()");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String outputResponse = "";
		String ReturnCode="";
		String ReturnDesc="";
		String BlacklistFlag="";
		String DuplicationFlag="";
		String SystemErrorCode="";
		HashMap<String,String> hm= new HashMap<String,String>(); // not nullable HashMap
		PL_Integration_Input genX=new PL_Integration_Input();
		PL_Integration_Output ReadXml = new PL_Integration_Output();
		Common_Utils common=new Common_Utils(PersonalLoanS.mLogger);
		String alert_msg="";
		PersonalLoanS.mLogger.info(pEvent.getSource().getName());
		//PCASI - 2694 - start
		/*if("TLRM_Button".equalsIgnoreCase(pEvent.getSource().getName())){
			PersonalLoanS.mLogger.info("Inside TLRM Button click event");
			String query;
			query="select description,code from NG_MASTER_RM_TL with (nolock)  where isActive='Y'";
			PersonalLoanS.mLogger.info( "query is: "+query);
			populatePickListWindow(query,"cmplx_Customer_RM_TL_NAME", "Description,Code", true, 20,"RMTLName");			     

		}*/
		//PCASI - 2694 - end
		
		//added by akshay on 6/7/18 for drop 5 point
			if ("ExtLiability_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
				if(formObject.getNGValue("cmplx_Customer_NTB").equals("false")){
					List<String> objInput=new ArrayList<String>();				
					objInput.add("Text:" + formObject.getWFWorkitemName());				
					PersonalLoanS.mLogger.info("Before executing procedure ng_RLOS_CheckWriteOff");
					 List<Object> objOutput=new ArrayList<Object>();
						
						objOutput.add("Text");
					PersonalLoanS.mLogger.info("Before executing procedure ng_RLOS_CheckWriteOff");
					objOutput= formObject.getDataFromStoredProcedure("ng_RLOS_CheckWriteOff", objInput,objOutput);
				}
			}
		//disha FSD
		if ("AddressDetails_addr_Add".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setNGValue("Address_wi_name",formObject.getWFWorkitemName());
			PersonalLoanS.mLogger.info( "Inside add button: "+formObject.getNGValue("Address_wi_name"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_AddressDetails_cmplx_AddressGrid");
		}
		
		//changes done by saurabh on 7th aug for the 6 button events.
		else if("Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			//formObject.fetchFragment("Inc_Doc", "IncomingDoc", "q_IncomingDoc");
			formObject.fetchFragment("Inc_Doc","IncomingDocNew","q_incomingDocNew");// fetchfragment parameter changed by bandana
			//added by akshay on 9/5/18
			//query changed by saurabh as part of new Incoming Document. 7th Jan.
			if(formObject.getNGValue("sig_docindex").equals("")){
				String query="select isnull(DocIndex,'') from ng_rlos_gr_incomingDocument with(nolock) where DocumentName='Signature' and IncomingDocGR_Winame = '"+formObject.getWFWorkitemName()+"'";
				List<List<String>> signature_index=formObject.getDataFromDataSource(query);
				formObject.setNGValue("sig_docindex", signature_index.get(0).get(0));
			}
		}
		else if("Button2".equalsIgnoreCase(pEvent.getSource().getName())){
			expandIncome();
		}
		else if("Button3".equalsIgnoreCase(pEvent.getSource().getName())){
			expandDecision();
		}
		else if("Button4".equalsIgnoreCase(pEvent.getSource().getName())){
			expandFinacleCore();
		}
		else if("Button5".equalsIgnoreCase(pEvent.getSource().getName())){
			expandCustDetVer();
		}
		else if("Button6".equalsIgnoreCase(pEvent.getSource().getName())){
			load_LoanCard_Details_Check(formObject);
		}
		//added by rishabh
		else if ("checklist_ver_sp2_Button1".equalsIgnoreCase(pEvent.getSource().getName())) {
			 PersonalLoanS.mLogger.info( "checkList Verification Details are saved");  //Tarang 24 Dec, New method added to handle invalid session in fragment save event.
			 Custom_fragmentSave("CheckList");
			 alert_msg=NGFUserResourceMgr_PL.getAlert("VAL108");
			 PersonalLoanS.mLogger.info( "checkList Verification Details are saved123"); 
			 throw new ValidatorException(new FacesMessage(alert_msg));
		 }
		else if ("exceptionalCase_sp2_Button2".equalsIgnoreCase(pEvent.getSource().getName())) {
			 PersonalLoanS.mLogger.info( "new except Verification Details are saved"); 
			 Custom_fragmentSave("Exceptional_Case_Alert");
			 alert_msg=NGFUserResourceMgr_PL.getAlert("VAL106");
			 PersonalLoanS.mLogger.info( "new except Details are saved123"); 
			 throw new ValidatorException(new FacesMessage(alert_msg));
		 }
		 else if ("Designation_button1".equalsIgnoreCase(pEvent.getSource().getName())){
			 String query;

			 query="select description,code from NG_MASTER_Designation with (nolock)  where isActive='Y'";
			 PersonalLoanS.mLogger.info( "@bandana");
			 PersonalLoanS.mLogger.info( "query is: "+query);
			 populatePickListWindow(query,"cmplx_cust_ver_sp2_desig_remarks", "Description,Code", true, 20,"Designation");			     

		 }//Designation_button
		//saurabh1 freezone start saurabh1 I6
		 else if ("EMploymentDetails_FreeZone_Button".equalsIgnoreCase(pEvent.getSource().getName())){
			 String query;

			 query="select description,code from NG_MASTER_freezoneName with (nolock)  where isActive='Y'";
			 PersonalLoanS.mLogger.info( "@saurabh1");
			 PersonalLoanS.mLogger.info( "query is: "+query);
			 populatePickListWindow(query,"cmplx_EmploymentDetails_FreezoneName", "Description,Code", true, 20,"Freezone");			     

		 }//freezone_button saurabh1 I6
		else if ("AddressDetails_addr_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_AddressDetails_cmplx_AddressGrid");
	
		}
		else if("cmplx_EmploymentDetails_Others".equalsIgnoreCase(pEvent.getSource().getName())){
			try{
				if(	NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_Others"))){
					LoadPickList("cmplx_EmploymentDetails_ApplicationCateg", "select '--Select--' as Description,'' as code union select  convert(varchar,description),code from NG_MASTER_ApplicatonCategory with (nolock) order by code");//added By Tarang started on 22/02/2018 as per drop 4 point 20
					formObject.setNGValue("cmplx_EmploymentDetails_ApplicationCateg","CN");//added By Tarang started on 22/02/2018 as per drop 4 point 20
					formObject.setEnabled("cmplx_EmploymentDetails_ApplicationCateg",false);//added By Tarang started on 22/02/2018 as per drop 4 point 20
					LoadPickList("cmplx_EmploymentDetails_EmpStatusPL","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_EmployerStatusPL with(nolock) where isActive='Y' and product='Credit Card' order by code" );					
					formObject.setLocked("cmplx_EmploymentDetails_EmpStatusPL",false);
					if("IM".equals(formObject.getNGValue("Subproduct_productGrid"))){
						formObject.removeItem("cmplx_EmploymentDetails_EmpStatusPL",2);
						formObject.setNGValue("cmplx_EmploymentDetails_EmpStatusPL","CN");//was not happening from js so done from here
					}
				}
				else{
					LoadPickList("cmplx_EmploymentDetails_ApplicationCateg", "select '--Select--' as Description,'' as code union select  convert(varchar,description),code from NG_MASTER_ApplicatonCategory with (nolock) where code NOT LIKE '%CN%' order by code");//added By Tarang started on 22/02/2018 as per drop 4 point 20
					formObject.setEnabled("cmplx_EmploymentDetails_ApplicationCateg",true);//added By Tarang started on 22/02/2018 as per drop 4 point 20
					LoadPickList("cmplx_EmploymentDetails_EmpStatusPL","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_EmployerStatusPL with(nolock) where isActive='Y'  order by code" );					
					formObject.setLocked("cmplx_EmploymentDetails_EmpStatusPL",true);//was not happening from js so done from here

				}
			}
			catch(Exception e)
			{
				//PersonalLoanS.mLogger.info( "Exception occurred in removing item from cmplx_EmploymentDetails_EmpStatusPL"+printException(e));}
		}}
		
		if("DecisionHistory_ADD".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			PersonalLoanS.mLogger.info( "Inside ReferGrid");
			try{//code sync with CC
				if("Approve".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")) && "CAD_Analyst1".equalsIgnoreCase(formObject.getWFActivityName()))
				{
					Check_All_Limits();
				}
				if("DDVT_Maker".equalsIgnoreCase(formObject.getWFActivityName())){
					ddvtmaker_submitapp();
				}
				PersonalLoanS.mLogger.info("PL_CommonCode"+ "Inside AddInReferGrid: "); 
				
				String decision=formObject.getNGValue("cmplx_Decision_Decision");
				String ReferTo=formObject.getNGValue("cmplx_Decision_ReferTo");
				String currDate=common.Convert_dateFormat("", "","dd/MM/yyyy HH:mm");
				String s=formObject.getWFGeneralData();
				String Cifid=formObject.getNGValue("cmplx_Customer_CIFNO");
				String emiratesid=formObject.getNGValue("cmplx_Customer_EmiratesID");
				String custName=formObject.getNGValue("CustomerLabel");
				//formObject.setNGValue("ReferTo", ReferTo);
				int startPosition = s.indexOf("<EntryDateTime>") + "<EntryDateTime>".length();
				int endPosition = s.indexOf("</EntryDateTime>", startPosition);
				String subS = s.substring(startPosition, endPosition-4);
				
				List<String> DecisionCodelist=new ArrayList<String>();
				String DecisionCode=formObject.getNGValue("DecisionHistory_DecisionReasonCode");
				String DecisionSubCode=formObject.getNGValue("DecisionHistory_DecisionSubReason");

				PersonalLoanS.mLogger.info("PL_Common"+ "DecisionReasonCode: "+DecisionCode);
				DecisionCodelist.clear();
				DecisionCodelist.add(currDate);
				DecisionCodelist.add(formObject.getUserName());
				DecisionCodelist.add(formObject.getWFActivityName());
				DecisionCodelist.add(decision);
				DecisionCodelist.add(formObject.getNGValue("cmplx_Decision_REMARKS"));
				DecisionCodelist.add(formObject.getWFWorkitemName());
				DecisionCodelist.add(null==ReferTo?"":ReferTo);
				DecisionCodelist.add(subS);
				DecisionCodelist.add(DecisionCode);
				DecisionCodelist.add(Cifid);
				DecisionCodelist.add(emiratesid);
				DecisionCodelist.add(custName);
				DecisionCodelist.add("");
				DecisionCodelist.add(DecisionSubCode);
				
				/*DecisionCodelist.add(DecisionCode);
						if("CSO (for documents)".equalsIgnoreCase(DecisionCode_array[i])){
							DecisionCodelist.add("Source");
						}
						else{
							//DecisionCodelist.add(DecisionCode_array[i]);
						}
						DecisionCodelist.add("");
						DecisionCodelist.add("");
					    DecisionCodelist.add(formObject.getWFWorkitemName());*/

				formObject.addItemFromList("Decision_ListView1",DecisionCodelist);
				PersonalLoanS.mLogger.info("PL_CommonCode"+"ReferList is:"+DecisionCodelist.toString());
				//}
				PersonalLoanS.mLogger.info("PL out of refer function"); 
				//formObject.saveFragment("ReferHistory");
				//changes to save refer to for Cad_Analyst2 start
				ReferTo = setReferto();
				if(("Cad_Analyst2".equalsIgnoreCase(formObject.getWFActivityName())||"DDVT_Checker".equalsIgnoreCase(formObject.getWFActivityName())|| "Cad_Analyst1".equalsIgnoreCase(formObject.getWFActivityName())) && "Refer".equalsIgnoreCase(decision)){
					//String update_refer = null==ReferTo?"":ReferTo;
					String query = "update NG_PL_EXTTABLE set ReferTo = '"+ReferTo+"' where PL_Wi_Name = '"+formObject.getWFWorkitemName()+"'";
					PersonalLoanS.mLogger.info("Deepak Query to update ReferTo at Cad 2: "+query);
					formObject.saveDataIntoDataSource(query);
				}
				if("CSM_Review".equalsIgnoreCase(formObject.getWFActivityName())){
					String query = "update NG_PL_EXTTABLE set IS_SALES_HEAD_DEC = (select top 1 IS_SALES_HEAD_DEC from NG_RLOS_DecisionValidation with(nolock) where wi_name = '"+formObject.getWFWorkitemName()+"') where PL_Wi_Name ='"+formObject.getWFWorkitemName()+"'";
					PersonalLoanS.mLogger.info("Deepak Query to update IS_SALES_HEAD_DEC at CSM_Review: "+query);
					formObject.saveDataIntoDataSource(query);
					if("Submit".equalsIgnoreCase(decision)){
						String query_2 = "update NG_RLOS_DecisionValidation set IS_SALES_HEAD_DEC = '' where wi_name = '"+formObject.getWFWorkitemName()+"'";
						formObject.saveDataIntoDataSource(query_2);
						PersonalLoanS.mLogger.info("Deepak Query to update IS_SALES_HEAD_DEC at CSM_Review: "+query_2);
					}
				}
				//changes to save refer to for Cad_Analyst2 end
			}


			catch(Exception ex){//formObject.RaiseEvent("WFSave");
				PersonalLoanS.mLogger.info("PL_Common ReferList Exception is:");
				printException(ex);
			}


			PersonalLoanS.mLogger.info( "Out of ReferGrid");
		}
		else if ("DecisionHistory_Modify".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.ExecuteExternalCommand("NGDeleteRow", "Decision_ListView1");
			PersonalLoanS.mLogger.info("hey this is decison modify button test");
			try{
				if("DDVT_Maker".equalsIgnoreCase(formObject.getWFActivityName())){
					ddvtmaker_submitapp();
				}
				String decision=formObject.getNGValue("cmplx_Decision_Decision");
				String ReferTo=formObject.getNGValue("cmplx_Decision_ReferTo");
				String currDate=common.Convert_dateFormat("", "","dd/MM/yyyy HH:mm");
				String s=formObject.getWFGeneralData();
				String Cifid=formObject.getNGValue("cmplx_Customer_CIFNO");
				String emiratesid=formObject.getNGValue("cmplx_Customer_EmiratesID");
				String custName=formObject.getNGValue("CustomerLabel");
				//formObject.setNGValue("ReferTo", ReferTo);
				int startPosition = s.indexOf("<EntryDateTime>") + "<EntryDateTime>".length();
				int endPosition = s.indexOf("</EntryDateTime>", startPosition);
				String subS = s.substring(startPosition, endPosition-4);
				
				List<String> DecisionCodelist=new ArrayList<String>();
				String DecisionCode=formObject.getNGValue("DecisionHistory_DecisionReasonCode");
				String DecisionSubCode=formObject.getNGValue("DecisionHistory_DecisionSubReason");

				PersonalLoanS.mLogger.info("PL_Common"+ "DecisionReasonCode: "+DecisionCode);
				DecisionCodelist.clear();
				DecisionCodelist.add(currDate);
				DecisionCodelist.add(formObject.getUserName());
				DecisionCodelist.add(formObject.getWFActivityName());
				DecisionCodelist.add(decision);
				DecisionCodelist.add(formObject.getNGValue("cmplx_Decision_REMARKS"));
				DecisionCodelist.add(formObject.getWFWorkitemName());
				DecisionCodelist.add(null==ReferTo?"":ReferTo);
				DecisionCodelist.add(subS);
				DecisionCodelist.add(DecisionCode);
				DecisionCodelist.add(Cifid);
				DecisionCodelist.add(emiratesid);
				DecisionCodelist.add(custName);
				DecisionCodelist.add("");
				DecisionCodelist.add(DecisionSubCode);
				formObject.addItemFromList("Decision_ListView1",DecisionCodelist);
					PersonalLoanS.mLogger.info("hey we testing modify on fpu"+DecisionCodelist);
				
				ReferTo = setReferto();
				if("Cad_Analyst2".equalsIgnoreCase(formObject.getWFActivityName())||"DDVT_Checker".equalsIgnoreCase(formObject.getWFActivityName())){
					String query="";
					if("Refer".equalsIgnoreCase(decision)){
						query = "update NG_PL_EXTTABLE set ReferTo = '"+ReferTo+"' where PL_Wi_Name = '"+formObject.getWFWorkitemName()+"'";
					}
					else{
						query = "update NG_PL_EXTTABLE set ReferTo = '' where PL_Wi_Name = '"+formObject.getWFWorkitemName()+"'";
					}
	
					formObject.saveDataIntoDataSource(query);
				}
			}
			catch(Exception ex)
			{//formObject.RaiseEvent("WFSave");
				PersonalLoanS.mLogger.info("PL_Common ReferList Exception is:");
				printException(ex);
			}


			
			

		}


		//below code by saurabh for new Incoming Doc.
		else if("IncomingDocNew_Addbtn".equalsIgnoreCase(pEvent.getSource().getName())) {
			//Deepak Changes done to save signature document index for upload signature call.
			if("Signature".equalsIgnoreCase(formObject.getNGValue("IncomingDocNew_DocName"))){
				formObject.setNGValue("sig_docindex", formObject.getNGValue("IncomingDocNew_Docindex"));
			}
			
			formObject.setNGValue("IncomingDocNew_winame", formObject.getWFWorkitemName());
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_IncomingDocNew_IncomingDocGrid");
			//to set expiry date by default nikhil 30/1
			Date date = new Date();
			String modifiedDate= new SimpleDateFormat("dd/MM/yyyy").format(date);
			//formObject.setNGValue("IncomingDocNew_ExpiryDate",modifiedDate,false);
		}
		
		else if("IncomingDocNew_Modifybtn".equalsIgnoreCase(pEvent.getSource().getName())) {
			//Deepak Changes done to save signature document index for upload signature call.
			if("Signature".equalsIgnoreCase(formObject.getNGValue("IncomingDocNew_DocName"))){
				formObject.setNGValue("sig_docindex", formObject.getNGValue("IncomingDocNew_Docindex"));
			}
			formObject.setNGValue("IncomingDocNew_winame", formObject.getWFWorkitemName());
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_IncomingDocNew_IncomingDocGrid");
			PersonalLoanS.mLogger.info("rlos modify btn inc doc");
			PersonalLoanS.mLogger.info("formObject.isLocked(\"IncomingDocNew_DocType\")"+formObject.isLocked("IncomingDocNew_DocType"));
			PersonalLoanS.mLogger.info("formObject.isLocked(\"IncomingDocNew_DocName\")"+formObject.isLocked("IncomingDocNew_DocName"));
			if(formObject.isLocked("IncomingDocNew_DocType")) {
				PersonalLoanS.mLogger.info("document type is locked");
				formObject.setLocked("IncomingDocNew_DocType", false);
			}
			if(formObject.isLocked("IncomingDocNew_DocName")) {
				PersonalLoanS.mLogger.info("document name is locked");
				formObject.setLocked("IncomingDocNew_DocName", false);
			}
			//to set expiry date by default nikhil 30/1
			Date date = new Date();
			String modifiedDate= new SimpleDateFormat("dd/MM/yyyy").format(date);
			formObject.setNGValue("IncomingDocNew_ExpiryDate",modifiedDate,false);
		}
		else if ("fieldvisit_sp2_Button1".equalsIgnoreCase(pEvent.getSource().getName())) {
			 PersonalLoanS.mLogger.info( "new field Verification Details are saved"); 
			 Custom_fragmentSave("Field_Visit_Initiated");
			 alert_msg=NGFUserResourceMgr_PL.getAlert("VAL107");
			 PersonalLoanS.mLogger.info( "new fieldVerification Details are saved123"); 
			 throw new ValidatorException(new FacesMessage(alert_msg));
		 }
		else if ("EmploymentVerification_s2_Button1".equalsIgnoreCase(pEvent.getSource().getName())) {
			 PersonalLoanS.mLogger.info( "new EmploymentVerification Details are saved"); 
			 Custom_fragmentSave("Employment Verification");
			 alert_msg=NGFUserResourceMgr_PL.getAlert("VAL500");
			 PersonalLoanS.mLogger.info( "new employmentVerification Details are saved123"); 
			 throw new ValidatorException(new FacesMessage(alert_msg));
		 }
		
		else if("cmplx_IncomingDocNew_IncomingDocGrid".equalsIgnoreCase(pEvent.getSource().getName()))
		{
					PersonalLoanS.mLogger.info("inside doc grid");
			if(formObject.getSelectedIndex("cmplx_IncomingDocNew_IncomingDocGrid")==-1)
			{
				//to set expiry date by default nikhil 30/1
				Date date = new Date();
				String modifiedDate= new SimpleDateFormat("dd/MM/yyyy").format(date);
				formObject.setNGValue("IncomingDocNew_ExpiryDate",modifiedDate,false);
				
				
					}
			
			

		}
		 else if("IncomingDocNew_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			
			 PersonalLoanS.mLogger.info( "Inside add save button for documents:");
				
			 Custom_fragmentSave("Inc_Doc");
			 alert_msg=NGFUserResourceMgr_PL.getAlert("VAL080");//Incoming Document details savedd

			 try{
				 Custom_fragmentSave("Inc_Doc");
				 PersonalLoanS.mLogger.info( "Inside Inc SAVE, displaying alert message");
				 alert_msg=NGFUserResourceMgr_PL.getAlert("val1000");//Incoming Document details savedd
			 }
			 catch(Exception e){
				 PersonalLoanS.mLogger.info( "Exc:"+e);
			 }
			 PersonalLoanS.mLogger.info("End of elseif for Inc_Doc");

			 throw new ValidatorException(new FacesMessage(alert_msg));
		 }
		//Below code added by shweta 
			else if ("DDS_Add".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.setNGValue("CC_Loan_wi_name",formObject.getWFWorkitemName());
				PersonalLoanS.mLogger.info(formObject.getNGValue("CC_Loan_wi_name"));
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_CC_Loan_cmplx_dds");//cmplx_CC_Loan_cmplx_btc
				PersonalLoanS.mLogger.info( "Inside add button33: add of btc details");
				//formObject.setNGValue("benificiaryName", formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme"));
			}
			
			else if ("DDS_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_CC_Loan_cmplx_dds");
				PersonalLoanS.mLogger.info( "Inside add button33: modify of btc details");

			}
			else if ("DDS_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_CC_Loan_cmplx_dds");
				PersonalLoanS.mLogger.info( "Inside add button33: delete of btc details");
			}
			else if ("SI_Add".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.setNGValue("CC_Loan_wi_name",formObject.getWFWorkitemName());
				PersonalLoanS.mLogger.info(formObject.getNGValue("CC_Loan_wi_name"));
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_CC_Loan_cmplx_si");//cmplx_CC_Loan_cmplx_btc
				PersonalLoanS.mLogger.info( "Inside add button33: add of btc details");
				//formObject.setNGValue("benificiaryName", formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme"));
			}
			
			else if ("SI_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_CC_Loan_cmplx_si");
				PersonalLoanS.mLogger.info( "Inside add button33: modify of btc details");

			}
			else if ("SI_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_CC_Loan_cmplx_si");
				PersonalLoanS.mLogger.info( "Inside add button33: delete of btc details");
			}
			else if ("RVC_Add".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.setNGValue("CC_Loan_wi_name",formObject.getWFWorkitemName());
				PersonalLoanS.mLogger.info(formObject.getNGValue("CC_Loan_wi_name"));
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_CC_Loan_cmplx_rvc");//cmplx_CC_Loan_cmplx_btc
				PersonalLoanS.mLogger.info( "Inside add button33: add of btc details");
				//formObject.setNGValue("benificiaryName", formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme"));
			}
			
			else if ("RVC_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_CC_Loan_cmplx_rvc");
				PersonalLoanS.mLogger.info( "Inside add button33: modify of btc details");

			}
			else if ("RVC_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_CC_Loan_cmplx_rvc");
				PersonalLoanS.mLogger.info( "Inside add button33: delete of btc details");
			}

		
			else if ("BT_Add".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.setNGValue("CC_Loan_wi_name",formObject.getWFWorkitemName());
				PersonalLoanS.mLogger.info(formObject.getNGValue("CC_Loan_wi_name"));
				//formObject.ExecuteExternalCommand("NGAddRow", "cmplx_CC_Loan_cmplx_btc");//cmplx_CC_Loan_cmplx_btc
				PersonalLoanS.mLogger.info( "Inside add button33: add of btc details");
				//formObject.setNGValue("benificiaryName", formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme"));
				
				Double availBal = maxAmountAllowed(formObject);//PCASP-3191
				if(Double.parseDouble(formObject.getNGValue("amount"))>availBal){
					alert_msg=NGFUserResourceMgr_PersonalLoanS.getAlert("VAL123")+" "+availBal;
					throw new ValidatorException(new FacesMessage(alert_msg));
				}
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_CC_Loan_cmplx_btc");//cmplx_CC_Loan_cmplx_btc
				PersonalLoanS.mLogger.info( "Inside add button33: add of btc details");
				formObject.setNGValue("benificiaryName", formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme"));
			}
			else if ("BT_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_CC_Loan_cmplx_btc");
				PersonalLoanS.mLogger.info( "Inside add button33: modify of btc details");
				Double availBal = maxAmountAllowed(formObject);//PCASP-3191
				if(Double.parseDouble(formObject.getNGValue("amount"))>availBal){
					alert_msg=NGFUserResourceMgr_PersonalLoanS.getAlert("VAL123")+" "+availBal;
					throw new ValidatorException(new FacesMessage(alert_msg));
				}
				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_CC_Loan_cmplx_btc");
				PersonalLoanS.mLogger.info( "Inside add button33: modify of btc details");
				formObject.setLocked("transtype", false);
				formObject.setLocked("transferMode", false);    //by jahnavi for CR BT GRID

			}
			else if ("BT_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_CC_Loan_cmplx_btc");
				PersonalLoanS.mLogger.info( "Inside add button33: delete of btc details");
				formObject.setLocked("transtype", false);
				formObject.setLocked("transferMode", false);
			}
//code added by bandana ends

		else if ("AddressDetails_addr_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_AddressDetails_cmplx_AddressGrid");

		}
		else if("cmplx_CardDetails_cmplx_CardCRNDetails".equalsIgnoreCase(pEvent.getSource().getName())){
			String cardProduct = formObject.getNGValue("CardDetails_CardProduct");
			LoadPickList("CardDetails_TransactionFP","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_TransactionFeeProfile with (nolock) where isActive='Y' and Product = '"+cardProduct+"' order by code");
			LoadPickList("CardDetails_InterestFP","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_InterestProfile with (nolock) where isActive='Y' and Product = '"+cardProduct+"' order by code");
			LoadPickList("CardDetails_FeeProfile","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_feeprofile with (nolock) where isActive='Y' and Product = '"+cardProduct+"' order by code");
			int selected_row=formObject.getSelectedIndex("cmplx_CardDetails_cmplx_CardCRNDetails");
			if(selected_row==-1)
			{
				formObject.setLocked("CardDetails_Button1", true);
			}
		}
		
		else if("LoanDeatils_calculateemi".equalsIgnoreCase(pEvent.getSource().getName())){
			double loanAmount=Double.parseDouble(formObject.getNGValue("cmplx_LoanDetails_lonamt")==null||formObject.getNGValue("cmplx_LoanDetails_lonamt").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_LoanDetails_lonamt"));
			double tenureMonths=Double.parseDouble(formObject.getNGValue("cmplx_LoanDetails_tenor")==null||formObject.getNGValue("cmplx_LoanDetails_tenor").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_LoanDetails_tenor"));
			double rate=Double.parseDouble(formObject.getNGValue("cmplx_LoanDetails_netrate")==null||formObject.getNGValue("cmplx_LoanDetails_netrate").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_LoanDetails_netrate"));
			String First_emi_date = formObject.getNGValue("cmplx_LoanDetails_frepdate");
			PersonalLoanS.mLogger.info("Calculating EMI onchange of Tenor"+loanAmount+ "  " + tenureMonths+"  "+"  "+rate+"   "+First_emi_date);
		
			String EMI=getEMI_new(loanAmount,rate,tenureMonths,First_emi_date);
			formObject.setNGValue("cmplx_LoanDetails_loanemi", EMI==null||EMI.equalsIgnoreCase("")?"0.00":EMI);
			formObject.setNGValue("cmplx_EligibilityAndProductInfo_EMI", EMI==null||EMI.equalsIgnoreCase("")?"0.00":EMI);	//PCASI 3524
			PersonalLoanS.mLogger.info("cmplx_EligibilityAndProductInfo_EMI: cmplx_LoanDetails_loanemi:"+EMI);
		
		}

		//Below code added by Prabhakar for CRN and Kalyan 
		else if ("CardDetails_Button1".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			PersonalLoanS.mLogger.info("Inside CardDetails_Button1 click ");
			String alertMsg="";
		try{
				String cardProd = formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails", formObject.getSelectedIndex("cmplx_CardDetails_cmplx_CardCRNDetails"), 0);
				PersonalLoanS.mLogger.info("Inside CardDetails_Button1 : card product is : "+cardProd);
				if(null!=cardProd && !"".equals(cardProd)){
					PersonalLoanS.mLogger.info("value of checkDedupAirArabia(cardProd) is  "+checkDedupAirArabia(cardProd));
					if("Pass".equals(checkDedupAirArabia(cardProd))){
			String gridECRN="";
			int gridRowNo=formObject.getLVWRowCount("cmplx_CardDetails_cmplx_CardCRNDetails");
			List<String> objInput = new ArrayList();
			List<Object> objOutput = new ArrayList();
			PersonalLoanS.mLogger.info("Generate CRN Button");
			if(gridRowNo>1)
			{
				for(int i=0;i<=gridRowNo;i++)
				{
					gridECRN=formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",i,1);
					PersonalLoanS.mLogger.info(i+" Row ECRN is "+gridECRN);
					//formObject.getNGValueAt(arg0, arg1)
					if(gridECRN!=null && !gridECRN.equalsIgnoreCase(""))
						PersonalLoanS.mLogger.info("ECRN is already Present "+gridECRN);
					break;
				}
			}
			objInput.add("Text:" + formObject.getWFWorkitemName());
			objOutput.add("Text");
			objInput.add("Text:"+formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails", formObject.getSelectedIndex("cmplx_CardDetails_cmplx_CardCRNDetails"), 0));//Card Product
			objInput.add("Text:"+formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails", formObject.getSelectedIndex("cmplx_CardDetails_cmplx_CardCRNDetails"), 9));//Applicant type
			
			PersonalLoanS.mLogger.info("generateCRNANDECRN objInput args is: " + formObject.getWFWorkitemName());
			PersonalLoanS.mLogger.info("generateCRNANDECRN objInput cardproduct is: " + formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails", formObject.getSelectedIndex("cmplx_CardDetails_cmplx_CardCRNDetails"), 0));
			PersonalLoanS.mLogger.info("generateCRNANDECRN objInput applicanttype is: " + formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails", formObject.getSelectedIndex("cmplx_CardDetails_cmplx_CardCRNDetails"), 9));
			objOutput = formObject.getDataFromStoredProcedure("generateCRNANDECRN", objInput, objOutput);
			PersonalLoanS.mLogger.info("generateCRNANDECRN Output: "+objOutput.toString());
			if(objOutput.isEmpty()){
				alert_msg=NGFUserResourceMgr_PL.getAlert("val079");
				formObject.setEnabled("CardDetails_Button1",true);
			}
			else{
				String ECRNandCRN = (String)objOutput.get(0);
				if(ECRNandCRN.contains("Error")){
					PersonalLoanS.mLogger.info("CRN/ECRN generation Error occured in procedure");
					formObject.setEnabled("CardDetails_Button1",true);
					if(ECRNandCRN.indexOf(":")>-1 && ECRNandCRN.split(":").length>0){
						PersonalLoanS.mLogger.info("CRN/ECRN generation Error occured in procedure: Error string provided in procedure");
						alert_msg = ECRNandCRN.split(":")[1];
					}
					else{
						PersonalLoanS.mLogger.info("CRN/ECRN generation Error occured in procedure: Error string provided in procedure");
						alert_msg=NGFUserResourceMgr_PL.getAlert("val079");
					}
				}
				else{
								//Changed because in case CRN/ECRN exceeds the length(9 Character ~ was coming at frontend for CRN
								String[] ECRNCRN=ECRNandCRN.split("~");
								String ECRN= gridECRN!=null && !gridECRN.equalsIgnoreCase("") ? gridECRN :ECRNCRN[0];
					formObject.setNGValue("CardDetails_ECRN", ECRN);
								formObject.setNGValue("CardDetails_CRN", ECRNCRN[1]);
					formObject.setEnabled("CardDetails_Button1",false);
					PersonalLoanS.mLogger.info("ECRN IS: " + ((String)objOutput.get(0)).substring(0, 9) + " CRN Is: " + ((String)objOutput.get(0)).substring(10));
					String CardProduct=formObject.getNGValue("CardDetails_CardProduct");
					String KRN=formObject.getNGValue("CardDetails_KRN");
					PersonalLoanS.mLogger.info("CardProduct is: "+CardProduct+" And KRN is: "+KRN);
					PersonalLoanS.mLogger.info("KRN IS"+String.valueOf(KRN));
					alert_msg=NGFUserResourceMgr_PL.getAlert("val080");
				}
						}
			}
					else{
						if("Fail".equals(checkDedupAirArabia(cardProd))){
							alertMsg="This Air Arabia Identifier is already in use";
							throw new ValidatorException(new FacesMessage(alertMsg));
						}
						else if("Invalid".equals(checkDedupAirArabia(cardProd))){
							alertMsg="Kindly fill Air Arabia Identifier for this card";
							throw new ValidatorException(new FacesMessage(alertMsg));	
						}
					}
				}
			}
		catch(Exception ex){
				PersonalLoanS.mLogger.info("Exception in generating ECRN:");
				printException(ex);
				if(!"".equals(alertMsg)){
					throw new ValidatorException(new FacesMessage(alertMsg));
		}
			}
		/*if(CardProduct.toLowerCase().contains("kalyan") && "".equalsIgnoreCase(KRN))
      {
    	  formObject.setEnabled("CardDetails_Button5",true);
      }*/
		throw new ValidatorException(new FacesMessage(alert_msg));
	}

		else if ("CardDetails_Button5".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			List<String> objInput = new ArrayList();
			List<Object> objOutput = new ArrayList();
			PersonalLoanS.mLogger.info("Generate KCRN Button");
			objInput.add("Text:" + formObject.getWFWorkitemName());
			objInput.add("Text:" + formObject.getNGValue("CardDetails_ECRN"));
			objOutput.add("Text");
			PersonalLoanS.mLogger.info("objInput args are: " + formObject.getWFWorkitemName()+" "+formObject.getNGValue("CardDetails_CRN"));
			objOutput = formObject.getDataFromStoredProcedure("Ng_KalyanReferencceNumber", objInput, objOutput);
			String KRN = (String)objOutput.get(0);
			formObject.setNGValue("CardDetails_KRN", KRN);
			formObject.setEnabled("CardDetails_Button5",false);
			PersonalLoanS.mLogger.info("KRN IS: " + ((String)objOutput.get(0)));
			//PCASP-1436
				if(formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails", formObject.getSelectedIndex("cmplx_CardDetails_cmplx_CardCRNDetails"),0).contains("KALYAN")
					&&formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails", formObject.getSelectedIndex("cmplx_CardDetails_cmplx_CardCRNDetails"),9).equalsIgnoreCase("Primary"))
				{
					formObject.setNGValue("cmplx_CardDetails_CompanyEmbossing_name", KRN);
				}
		}
		//Above code added by Prabhakar for CRN and kalyan
		/*//Below code added by Prabhakar for CRN 
		 else if ("CardDetails_Button1".equalsIgnoreCase(pEvent.getSource().getName()))
        {
          List<String> objInput = new ArrayList();
          List<Object> objOutput = new ArrayList();
          PersonalLoanS.mLogger.info("Generate CRN Button");
          objInput.add("Text:" + formObject.getWFWorkitemName());
          objOutput.add("Text");
          PersonalLoanS.mLogger.info("objInput args is: " + formObject.getWFWorkitemName());
          objOutput = formObject.getDataFromStoredProcedure("generateCRNANDECRN", objInput, objOutput);
          String ECRNandCRN = (String)objOutput.get(0);
          formObject.setNGValue("CardDetails_ECRN", ECRNandCRN.substring(0, 9));
          formObject.setNGValue("CardDetails_CRN", ECRNandCRN.substring(10));
          PersonalLoanS.mLogger.info("ECRN IS: " + ((String)objOutput.get(0)).substring(0, 9) + " CRN Is: " + ((String)objOutput.get(0)).substring(10));
        }
		//Above code added by Prabhakar for CRN 
		 */		//added by saurabh on 4th jan

		else if ("CardDetails_modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_CardDetails_cmplx_CardCRNDetails");
			//Deepak 25March2018 Changes done to save CRN Grid data. at CRN/ECRN generation 
			//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
			
			 PersonalLoanS.mLogger.info( "Inside add button22: modify of card details");
			 //Deepak 25March2018 Changes done to save CRN Grid data. at CRN/ECRN generation 
			 //Changed by nikhil 27/11 to save ecrn on header
			 //changed by shweta
			 if("DDVT_Maker".equalsIgnoreCase(formObject.getWFActivityName()) && formObject.getLVWRowCount("cmplx_CardDetails_cmplx_CardCRNDetails")>0){
				 String CRN=formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",0, 2);
				 for(int i=1;i<formObject.getLVWRowCount("cmplx_CardDetails_cmplx_CardCRNDetails");i++)
				 {
					 CRN=CRN+","+formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",i, 2);
				 }
				 formObject.setNGValue("CRN", CRN);
				 
				 if("".equalsIgnoreCase(formObject.getNGValue("ECRN"))){
					 formObject.setNGValue("ECRN", formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",0,1));
					 formObject.setNGValue("ECRNLabel",formObject.getNGValue("ECRN")); 
					 String newCif = formObject.getNGValue("CardDetails_newPrimaryCif");
					 PersonalLoanS.mLogger.info("NewCif: "+ newCif);
					 PersonalLoanS.mLogger.info("old CIF: "+ formObject.getNGValue("cmplx_Customer_CIFNO"));
					 PersonalLoanS.mLogger.info("NTB flag: "+ formObject.getNGValue("cmplx_Customer_NTB"));
					 if((!formObject.getNGValue("cmplx_Customer_CIFNO").equalsIgnoreCase(newCif)) && (!("".equalsIgnoreCase(newCif) || newCif ==null) && "false".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))) ){
						 PersonalLoanS.mLogger.info("Inside CIF update condition: "+ newCif);
						 formObject.setNGValue("cmplx_Customer_CIFNO",newCif);
						 formObject.setNGValue("CIF_ID",newCif);
						 formObject.setNGValue("CifLabel",newCif);
					 }
					 Custom_fragmentSave("CustomerDetails");
					 formObject.RaiseEvent("WFSave");
				 }
				 Custom_fragmentSave("Alt_Contact_container");
	
			 }

			 //Tarang 24 Dec, New method added to handle invalid session in fragment save event.
			 Custom_fragmentSave("Card_Details");
		}
		else if ("CardDetails_delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_CardDetails_cmplx_CardCRNDetails");
			//Deepak 25March2018 Changes done to save CRN Grid data. at CRN/ECRN generation 
			//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
			Custom_fragmentSave("Card_Details");
		}

		//Arun added (14-jun)
		else if ("ExtLiability_Add".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setNGValue("Liability_Wi_Name",formObject.getWFWorkitemName());
			PersonalLoanS.mLogger.info( "before worst::"+formObject.getNGValue("Liability_New_worststatuslast24"));
		//	formObject.setNGValue("Liability_New_worststatuslast24",""); // PCASI - 3408
			PersonalLoanS.mLogger.info( "after worst::"+formObject.getNGValue("Liability_New_worststatuslast24"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
		}

		else if ("ExtLiability_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			PersonalLoanS.mLogger.info( "before worst::"+formObject.getNGValue("Liability_New_worststatuslast24"));
		//	formObject.setNGValue("Liability_New_worststatuslast24",""); // PCASI - 3408
			PersonalLoanS.mLogger.info( "after worst::"+formObject.getNGValue("Liability_New_worststatuslast24"));
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
		}

		else if ("ExtLiability_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
		}

		/*else if ("FATCA_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			String text=formObject.getNGItemText("cmplx_FATCA_ListedReason", formObject.getSelectedIndex("cmplx_FATCA_ListedReason"));
			PersonalLoanS.mLogger.info( "Inside FATCA_Button1 "+text);
			String hiddenText=formObject.getNGValue("cmplx_FATCA_selectedreasonhidden");
			PersonalLoanS.mLogger.info("hiddenText is: "+hiddenText);
			if(hiddenText.endsWith(",")|| hiddenText.equalsIgnoreCase(""))
			{
				hiddenText=hiddenText+text+",";
			}
			else
			{
				hiddenText=hiddenText+","+text+",";
			}
			formObject.setNGValue("cmplx_FATCA_selectedreasonhidden", hiddenText);
			PersonalLoanS.mLogger.info( "Inside FATCA_Button1 hidden text is after Concat: "+hiddenText);
			formObject.addItem("cmplx_FATCA_SelectedReason", text);
			try {
				formObject.removeItem("cmplx_FATCA_ListedReason", formObject.getSelectedIndex("cmplx_FATCA_ListedReason"));
				formObject.setSelectedIndex("cmplx_FATCA_ListedReason", -1);

			}catch (Exception e) {
				printException(e);
			}
			formObject.setLocked("cmplx_FATCA_ListedReason", false)	;	
			formObject.setLocked("cmplx_FATCA_SelectedReason", false)	;	

		}*/
		else if ("FATCA_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
			String text=formObject.getNGItemText("cmplx_FATCA_ListedReason", formObject.getSelectedIndex("cmplx_FATCA_ListedReason"));
			PersonalLoanS.mLogger.info( "Inside FATCA_Button1 text is "+text);
			//RLOS.mLogger.info( "Inside FATCA_Button1--->"+formObject.getNGValue("cmplx_FATCA_ListedReason", formObject.getSelectedIndex("cmplx_FATCA_ListedReason")));
			String hiddenText=formObject.getNGValue("cmplx_FATCA_selectedreasonhidden");
			PersonalLoanS.mLogger.info("hiddenText is: "+hiddenText);
			Map<String,String> ReasonMap =getFatcaReasons();
			if(hiddenText.endsWith(",")|| hiddenText.equalsIgnoreCase(""))
			{
				hiddenText=hiddenText+ReasonMap.get(text)+",";
			}
			else
			{
				hiddenText=hiddenText+","+ReasonMap.get(text)+",";
			}
			formObject.setNGValue("cmplx_FATCA_selectedreasonhidden", hiddenText);
			PersonalLoanS.mLogger.info( "Inside FATCA_Button1 hidden text is after Concat: "+hiddenText);

			formObject.addComboItem("cmplx_FATCA_SelectedReason",text, ReasonMap.get(text));
			try {
				formObject.removeItem("cmplx_FATCA_ListedReason", formObject.getSelectedIndex("cmplx_FATCA_ListedReason"));
				formObject.setSelectedIndex("cmplx_FATCA_ListedReason", -1);

			}catch (Exception e) {
					printException(e);
			}

		}
		else if("cmplx_CC_Loan_cmplx_btc".equalsIgnoreCase(pEvent.getSource().getName())){
			String transferMode = formObject.getNGValue("cmplx_CC_Loan_cmplx_btc", formObject.getSelectedIndex("cmplx_CC_Loan_cmplx_btc"),2);
			String transtype = formObject.getNGValue("cmplx_CC_Loan_cmplx_btc", formObject.getSelectedIndex("cmplx_CC_Loan_cmplx_btc"),0);
			
			LoadPickList("marketingCode", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_MarketingCodeService with (nolock) where TransactionType = '"+transtype+"' and IsActive = 'Y' order by code");

			if(transferMode.equalsIgnoreCase("C") && "SC".equalsIgnoreCase(transtype))
			{
				formObject.setLocked("dispatchChannel",false);
				formObject.setNGValue("dispatchChannel","998");
				formObject.setLocked("chequeNo",false);
				formObject.setLocked("chequeDate",false);
				formObject.setLocked("chequeStatus",false);
				formObject.setLocked("tenor",true);
				formObject.setLocked("creditcardNo",true);
				formObject.setVisible("CC_Loan_Label24", false);
				formObject.setVisible("Account_No_for_AT", false);
				formObject.setVisible("CC_Loan_Label23", false);
				formObject.setVisible("Account_No_for_Swift", false);
			}
			else if(transferMode.equalsIgnoreCase("S") && "SC".equalsIgnoreCase(transtype))
			{
				formObject.setLocked("chequeNo",true);
				formObject.setLocked("chequeDate",true);
				formObject.setLocked("chequeStatus",true);
				formObject.setLocked("dispatchChannel",true);
				formObject.setLocked("tenor",true);
				formObject.setLocked("creditcardNo",true);
				formObject.setVisible("CC_Loan_Label24", false);
				formObject.setVisible("Account_No_for_AT", false);
				formObject.setVisible("CC_Loan_Label23", true);
				formObject.setVisible("Account_No_for_Swift", true);
			}
			
			if(transferMode.equalsIgnoreCase("A") && transtype!="BT")
			{
				formObject.setVisible("CC_Loan_Label23", false);
				formObject.setVisible("Account_No_for_Swift", false);
				formObject.setVisible("CC_Loan_Label24", true);
				formObject.setVisible("Account_No_for_AT", true);
			}
			else if(transferMode.equalsIgnoreCase("S") && transtype!="BT")
			{
				formObject.setVisible("CC_Loan_Label24", false);
				formObject.setVisible("Account_No_for_AT", false);
				formObject.setVisible("CC_Loan_Label23", true);
				formObject.setVisible("Account_No_for_Swift", true);
			}				 
			else{
				formObject.setVisible("CC_Loan_Label23", false);
				formObject.setVisible("Account_No_for_Swift", false);
				formObject.setVisible("CC_Loan_Label24", false);
				formObject.setVisible("Account_No_for_AT", false);
				if("BT".equalsIgnoreCase(transtype)){
					formObject.setLocked("tenor",true);
					
				}
			}
		}
		else if ("SmartCheck_Add".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setNGValue("SmartCheck_WiName",formObject.getWFWorkitemName());
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_SmartCheck_SmartCheckGrid");
			PersonalLoanS.mLogger.info( "Inside add button33: add of SmartCheck_Add details");
		}
		else if ("SmartCheck_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.setNGValue("SmartCheck_WiName",formObject.getWFWorkitemName());
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_SmartCheck_SmartCheckGrid");
			formObject.setEnabled("SmartCheck_Add",true);
			formObject.setEnabled("SmartCheck_Modify",false);

			PersonalLoanS.mLogger.info( "Inside add button33: modify of SmartCheck_Modify details");
		}
		if("SmartCheck_save".equalsIgnoreCase(pEvent.getSource().getName())){
			//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
			Custom_fragmentSave("Smart_check");
			alert_msg=NGFUserResourceMgr_PL.getAlert("val015");
			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		else if ("SmartCheck_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_SmartCheck_SmartCheckGrid");
			PersonalLoanS.mLogger.info( "Inside add button33: delete of SmartCheck_Delete details");

		}
		/*else if ("FATCA_Button2".equalsIgnoreCase(pEvent.getSource().getName())){
			PersonalLoanS.mLogger.info( "Inside FATCA_Button2 ");
			formObject.addItem("cmplx_FATCA_ListedReason", formObject.getNGItemText("cmplx_FATCA_SelectedReason", formObject.getSelectedIndex("cmplx_FATCA_SelectedReason")));
			try {
				formObject.removeItem("cmplx_FATCA_SelectedReason", formObject.getSelectedIndex("cmplx_FATCA_SelectedReason"));
				formObject.setSelectedIndex("cmplx_FATCA_SelectedReason", -1);
			} catch (Exception e) {

				printException(e);
			}
			formObject.setLocked("cmplx_FATCA_ListedReason", false)	;	
			formObject.setLocked("cmplx_FATCA_SelectedReason", false)	;
		}*/
		/*else if ("FATCA_Button2".equalsIgnoreCase(pEvent.getSource().getName())){
			String hiddenText=formObject.getNGValue("cmplx_FATCA_selectedreasonhidden");
			PersonalLoanS.mLogger.info( "Inside FATCA_Button2 >>>"+hiddenText);
			String hiddenTextRemove=formObject.getNGItemText("cmplx_FATCA_SelectedReason", formObject.getSelectedIndex("cmplx_FATCA_SelectedReason"))+",";
			hiddenText=hiddenText.replace(hiddenTextRemove, "");
			PersonalLoanS.mLogger.info( "hidden text after removing"+ hiddenText);
			formObject.setNGValue("cmplx_FATCA_selectedreasonhidden", hiddenText);
			formObject.addItem("cmplx_FATCA_ListedReason", formObject.getNGItemText("cmplx_FATCA_SelectedReason", formObject.getSelectedIndex("cmplx_FATCA_SelectedReason")));
			try {
				formObject.removeItem("cmplx_FATCA_SelectedReason", formObject.getSelectedIndex("cmplx_FATCA_SelectedReason"));
				formObject.setSelectedIndex("cmplx_FATCA_SelectedReason", -1);
			} catch (Exception e) {

				printException(e);
			}
			formObject.setLocked("cmplx_FATCA_ListedReason", false)	;	
			formObject.setLocked("cmplx_FATCA_SelectedReason", false)	;
		}*/
		
		else if ("FATCA_Button2".equalsIgnoreCase(pEvent.getSource().getName())){
			try {
				PersonalLoanS.mLogger.info( "Inside FATCA_Button2 ");
			Map<String,String> ReasonMap =getFatcaReasons();
			String hiddenText=formObject.getNGValue("cmplx_FATCA_selectedreasonhidden");
			PersonalLoanS.mLogger.info( "Inside FATCA_Button1 hidden text is "+hiddenText);
			String hiddenTextRemove=ReasonMap.get(formObject.getNGItemText("cmplx_FATCA_SelectedReason", formObject.getSelectedIndex("cmplx_FATCA_SelectedReason")));
			PersonalLoanS.mLogger.info( "hiddenText.indexOf(hiddenTextRemove)+hiddenTextRemove.length(), hiddenText.length() "+hiddenText.indexOf(hiddenTextRemove)+hiddenTextRemove.length()+","+hiddenText.length());
			if((hiddenText.indexOf(hiddenTextRemove)+hiddenTextRemove.length())<hiddenText.length() && hiddenText.charAt(hiddenText.indexOf(hiddenTextRemove)+hiddenTextRemove.length())==','){
			hiddenText=hiddenText.replace(hiddenTextRemove+"," , "");
			}
			else{
				hiddenText=hiddenText.replace(hiddenTextRemove , "");
			}
			formObject.setNGValue("cmplx_FATCA_selectedreasonhidden", hiddenText);
			PersonalLoanS.mLogger.info( "Inside FATCA_Button1 hidden text is after deletion: "+hiddenText);
			formObject.addComboItem("cmplx_FATCA_ListedReason",formObject.getNGItemText("cmplx_FATCA_SelectedReason", formObject.getSelectedIndex("cmplx_FATCA_SelectedReason")),hiddenTextRemove);
			
				formObject.removeItem("cmplx_FATCA_SelectedReason", formObject.getSelectedIndex("cmplx_FATCA_SelectedReason"));
				formObject.setSelectedIndex("cmplx_FATCA_SelectedReason", -1);
			} catch (Exception e) {

				printException(e);
			}
		}
		else if("ReferenceDetails_Reference_Add".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setNGValue("ReferenceDetails_reference_wi_name",formObject.getWFWorkitemName());
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_ReferenceDetails_cmplx_ReferenceGrid");			
		}

		else if ("ReferenceDetails_Reference__modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_ReferenceDetails_cmplx_ReferenceGrid");
		}

		else if ("ReferenceDetails_Reference_delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_ReferenceDetails_cmplx_ReferenceGrid");
		}




		else if("GuarantorDetails_add".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			formObject.setNGValue("GuarantorDetails_guarantor_WIName",formObject.getWFWorkitemName());
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Guarantror_GuarantorDet");			
			loadDynamicPickList();

		}

		else if ("GuarantorDetails_modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Guarantror_GuarantorDet");
			loadDynamicPickList();
		}

		else if ("GuarantorDetails_delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_Guarantror_GuarantorDet");
		}

		else if ("IncomingDoc_AddFromPCButton".equalsIgnoreCase(pEvent.getSource().getName())){
			IRepeater repObj=null;
			repObj = formObject.getRepeaterControl("IncomingDoc_Frame2");
			repObj.getValue(repObj.getChangeIndex(),"IncomingDoc_Label1");
			PersonalLoanS.mLogger.info("value of repeater:"+repObj.getValue(repObj.getChangeIndex(),"IncomingDoc_Label1"));
		}
		//added by yash

		//added By Tarang started on 08/03/2018

		else if ("FinacleCRMCustInfo_Add".equalsIgnoreCase(pEvent.getSource().getName())){
			if(Check_ECRNGenOnChange()){
				alert_msg=NGFUserResourceMgr_PL.getAlert("VAL116");
				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			formObject.setNGValue("FinacleCRMCustInfo_WINAME",formObject.getWFWorkitemName());
			PersonalLoanS.mLogger.info(formObject.getNGValue("FinacleCRMCustInfo_WINAME"));
			if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")) && "".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_CIFNO")) && "true".equalsIgnoreCase(formObject.getNGValue("FinacleCRMCustInfo_CheckBox1"))){
				formObject.setNGValue("cmplx_Customer_NTB","false");
				formObject.setLocked("FetchDetails",false);	

				formObject.setNGValue("cmplx_Customer_CIFNO",formObject.getNGValue("FinacleCRMCustInfo_Text1"));
				formObject.setNGValue("CifLabel",formObject.getNGValue("cmplx_Customer_CIFNO"));
				formObject.setNGValue("CIF_ID",formObject.getNGValue("FinacleCRMCustInfo_Text1"));
			}
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_FinacleCRMCustInfo_FincustGrid");
			Custom_fragmentSave("CustomerDetails");
			Custom_fragmentSave("FinacleCRMCustInfo_Frame1");//Deepak changes done for PCAS-3526
			PersonalLoanS.mLogger.info( "Inside add button33: add of FinacleCRMCustInfo details");

		}

		else if ("FinacleCRMCustInfo_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			if(Check_ECRNGenOnChange()){
				alert_msg=NGFUserResourceMgr_PL.getAlert("VAL116");
				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			if(formObject.getNGValue("FinacleCRMCustInfo_Text1").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_CIFNO"))){
				formObject.setNGValue("cmplx_Customer_NTB","true");
				formObject.setLocked("FetchDetails",true);	
				formObject.setNGValue("cmplx_Customer_CIFNO","");
				formObject.setNGValue("CifLabel","");

			}
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_FinacleCRMCustInfo_FincustGrid");
			CheckNTBOnFinCrmChange();
			formObject.setNGValue("FinacleCRMCustInfo_WINAME",formObject.getWFWorkitemName());
			PersonalLoanS.mLogger.info(formObject.getNGValue("FinacleCRMCustInfo_WINAME"));
			PersonalLoanS.mLogger.info( "Inside delete button33: delete of FinacleCRMCustInfo details");
			Custom_fragmentSave("CustomerDetails");
			Custom_fragmentSave("FinacleCRMCustInfo_Frame1");//Deepak changes done for PCAS-3526
			formObject.RaiseEvent("WFSave");
		}

		//added By Tarang ended on 08/03/2018

		else if("FetchDetails".equalsIgnoreCase(pEvent.getSource().getName()))
		{
			
			String ResideSince="";
			formObject.setNGValue("cmplx_Customer_ISFetchDetails", "Y");
			String alert_details="";
			//Deepak Code change for Entity Details
			//if("Is_EID_Genuine".equalsIgnoreCase("Y") && "cmplx_Customer_EmiratesID" != ""){
			String EID = formObject.getNGValue("cmplx_Customer_EmiratesID");
			PersonalLoanS.mLogger.info( "EID value for Entity Details: "+EID );

			if(formObject.getNGValue("cmplx_Customer_card_id_available")=="false"){
				PersonalLoanS.mLogger.info("RLOS value of Customer Details"+formObject.getNGValue("cmplx_Customer_card_id_available"));
				outputResponse =genX.GenerateXML("CUSTOMER_ELIGIBILITY","Primary_CIF");
				ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);

				if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCode)|| "000".equalsIgnoreCase(ReturnCode)){
					ReadXml.valueSetIntegration(outputResponse,""); 

					formObject.setNGValue("Is_Customer_Eligibility","Y");
					parse_cif_eligibility(outputResponse,"Primary_CIF");
					String NTB_flag = formObject.getNGValue("cmplx_Customer_NTB");

					if("true".equalsIgnoreCase(NTB_flag)){
						setcustomer_enable();
						try
						{
							PersonalLoanS.mLogger.info("inside Customer Eligibility to through Exception to Exit:");
							formObject.RaiseEvent("WFSave");
							throw new ValidatorException(new FacesMessage("Customer is a New to Bank Customer."));

						}
						finally{
							hm.clear();
						}
					}
					else{
						outputResponse = genX.GenerateXML("CUSTOMER_DETAILS","Primary_CIF");
						String  Marital_Status =  outputResponse.contains("<MaritialStatus>") ? outputResponse.substring(outputResponse.indexOf("<MaritialStatus>")+"</MaritialStatus>".length()-1,outputResponse.indexOf("</MaritialStatus>")):"";
						PersonalLoanS.mLogger.info("RLOS value of Marital_Status"+Marital_Status);
						ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
						PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);
						ResideSince =  outputResponse.contains("<ResideSince>") ? outputResponse.substring(outputResponse.indexOf("<ResideSince>")+"</ResideSince>".length()-1,outputResponse.indexOf("</ResideSince>")):"";    
						PersonalLoanS.mLogger.info("RLOS value of ResideSince"+ResideSince);



						if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCode)|| "000".equalsIgnoreCase(ReturnCode)){
							String Staff_cif = "";
							Staff_cif =  (outputResponse.contains("<IsStaff>")) ? outputResponse.substring(outputResponse.indexOf("<IsStaff>")+"</IsStaff>".length()-1,outputResponse.indexOf("</IsStaff>")):"";
							if("Y".equalsIgnoreCase(Staff_cif)){
								alert_msg=NGFUserResourceMgr_PL.getAlert("VAL051");
							}
							else{
								formObject.setNGValue("Is_Customer_Details","Y");
								formObject.RaiseEvent("WFSave");
								PersonalLoanS.mLogger.info("RLOS value of Is_Customer_Details"+formObject.getNGValue("Is_Customer_Details"));

								ReadXml.valueSetIntegration(outputResponse,"Primary_CIF");
								//code to set Emirates of residence start.
								int n=formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
								if(n>0){
									for(int i=0;i<n;i++){
										PersonalLoanS.mLogger.info("selecting Emirates of residence: "+formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid"+ i+ 0));
										if("RESIDENCE".equalsIgnoreCase(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0))){
											PersonalLoanS.mLogger.info("selecting Emirates of residence: settign value: "+formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid"+ i+ 0));
											formObject.setNGValue("cmplx_Customer_EmirateOfResidence",formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 6));
										}
									}
									//code to set Emirates of residence End.


								}
								//code change to save the date in desired format by AMAN        

								PersonalLoanS.mLogger.info("converting date entered"+formObject.getNGValue("cmplx_Customer_DOb")+"");	
								formObject.setNGValue("cmplx_Customer_DOb",formatDateFromOnetoAnother(formObject.getNGValue("cmplx_Customer_DOb"),"yyyy-mm-dd","dd/mm/yyyy"),false);

								try{
									String str_dob=formObject.getNGValue("cmplx_Customer_DOb");
									SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-mm-dd");
									SimpleDateFormat sdf2=new SimpleDateFormat("dd/mm/yyyy");
									if(str_dob!=null&&!str_dob.equalsIgnoreCase("")){
										String n_str_dob=sdf2.format(sdf1.parse(str_dob));
										PersonalLoanS.mLogger.info("converting date entered"+n_str_dob+"asd");
										formObject.setNGValue("cmplx_Customer_DOb",n_str_dob,false);
									}
								}
								catch(Exception e){
									PersonalLoanS.mLogger.info("Exception Occur while converting date"+e+"");
								}  
							}
						}
						else{
							formObject.setNGValue("Is_Customer_Details","N");
							
							alert_msg = NGFUserResourceMgr_PL.getAlert("val029");//"Error in fetch Customer details operation";
							PersonalLoanS.mLogger.info("PL DDVT Maker value of ReturnCode"+"Error in Customer Eligibility: "+ ReturnCode);
						}

						formObject.RaiseEvent("WFSave");
					}
					if(NGFUserResourceMgr_PL.getGlobalVar("PL_Y").equalsIgnoreCase(formObject.getNGValue("Is_Customer_Eligibility")) && NGFUserResourceMgr_PL.getGlobalVar("PL_Y").equalsIgnoreCase(formObject.getNGValue("Is_Customer_Details")))
					{ 
						PersonalLoanS.mLogger.info("RLOS value of Customer Details"+"inside if condition");
						formObject.setEnabled("FetchDetails", false); 
						throw new ValidatorException(new FacesMessage("Customer information fetched sucessfully"));
					}
					else{
						formObject.setEnabled("FetchDetails", true);
					}
					PersonalLoanS.mLogger.info("RLOS value of Customer Details ----1234");
					//formObject.RaiseEvent("WFSave");
					try
					{//empty try
					}
					finally{
						hm.clear();
					}

				}
				else{
					formObject.setNGValue("Is_Customer_Eligibility","N");
					alert_details="Fetch Details Call failed.";
					PersonalLoanS.mLogger.info("Dedupe check failed.");


					try{
						//empty try
					}
					finally{
						hm.clear();
					}
				}
				if(alert_details!=""){
					throw new ValidatorException(new FacesMessage(alert_details));

				} else {
					throw new ValidatorException(new FacesMessage("Existing Customer Details fetched Sucessfully"));

				}
				//added
			}

			else if(formObject.getNGValue("cmplx_Customer_card_id_available")=="true"){
				PersonalLoanS.mLogger.info("RLOS value of Customer Details ----1234567"+formObject.getNGValue("cmplx_Customer_card_id_available"));
				PersonalLoanS.mLogger.info("RLOS value of Customer Details ----1234567-->inside true");
				outputResponse = genX.GenerateXML("CUSTOMER_DETAILS","Primary_CIF");

				ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);

				ResideSince =  outputResponse.contains("<ResideSince>") ? outputResponse.substring(outputResponse.indexOf("<ResideSince>")+"</ResideSince>".length()-1,outputResponse.indexOf("</ResideSince>")):"";    
				PersonalLoanS.mLogger.info("RLOS value of ResideSince"+ResideSince);

				if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCode)|| "000".equalsIgnoreCase(ReturnCode)){
					String Staff_cif = "";
					Staff_cif =  (outputResponse.contains("<IsStaff>")) ? outputResponse.substring(outputResponse.indexOf("<IsStaff>")+"</IsStaff>".length()-1,outputResponse.indexOf("</IsStaff>")):"";
					if("Y".equalsIgnoreCase(Staff_cif)){
						alert_msg=NGFUserResourceMgr_PL.getAlert("VAL051");
					}
					else{
						formObject.setNGValue("Is_Customer_Details","Y");
						formObject.RaiseEvent("WFSave");
						PersonalLoanS.mLogger.info("RLOS value of CurrentDate Is_Customer_Details"+formObject.getNGValue("Is_Customer_Details"));
						ReadXml.valueSetIntegration(outputResponse,"Primary_CIF");
						//code added here to change the DOB in DD/MM/YYYY format     

						String DOB=formObject.getNGValue("cmplx_Customer_DOb");
						formObject.setNGValue("cmplx_Customer_DOb",formatDateFromOnetoAnother(formObject.getNGValue("cmplx_Customer_DOb"),"yyyy-mm-dd","dd/mm/yyyy"),false);
						
						/*try{
							String str_dob=formObject.getNGValue("cmplx_Customer_DOb");
							SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-mm-dd");
							SimpleDateFormat sdf2=new SimpleDateFormat("dd/mm/yyyy");
							if(str_dob!=null&&!str_dob.equalsIgnoreCase("")){
								String n_str_dob=sdf2.format(sdf1.parse(str_dob));
								PersonalLoanS.mLogger.info("converting date entered"+n_str_dob+"asd");
								formObject.setNGValue("cmplx_Customer_DOb",n_str_dob,false);
							}
						}
						catch(Exception e){
							PersonalLoanS.mLogger.info("Exception Occur while converting date"+e+"");
						}  */
						// code added here to change the DOB in DD/MM/YYYY format    
						//code to set Emirates of residence start.
						int n=formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
						if(n>0){
							for(int i=0;i<n;i++){
								PersonalLoanS.mLogger.info("selecting Emirates of residence: "+formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid"+ i+ 0));
								if(NGFUserResourceMgr_PL.getGlobalVar("PL_RESIDENCE").equalsIgnoreCase(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0))){
									PersonalLoanS.mLogger.info("selecting Emirates of residence: settign value: "+formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid"+ i+ 0));
									formObject.setNGValue("cmplx_Customer_EmirateOfResidence",formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 6));
								}
							}
							//code to set Emirates of residence End.
							formObject.setEnabled("FetchDetails", false); 
							formObject.RaiseEvent("WFSave");
							throw new ValidatorException(new FacesMessage("Existing Customer Details fetched Sucessfully"));

						}
						
					}

				}
				else{
					formObject.setNGValue("Is_Customer_Details","N");
					alert_details="Fetch Details Call failed.";
					PersonalLoanS.mLogger.info("RLOS value of Customer Details456"+formObject.getNGValue("Is_Customer_Details"));
				}
				if(alert_details!=""){
					throw new ValidatorException(new FacesMessage(alert_details));

				} else {
					throw new ValidatorException(new FacesMessage("Existing Customer Details fetched Sucessfully"));

				}
			}
			formObject.RaiseEvent("WFSave");

		}

		else if("Customer_Button1".equalsIgnoreCase(pEvent.getSource().getName()))
		{

			String NegatedFlag="";
			outputResponse =genX.GenerateXML("CUSTOMER_ELIGIBILITY","Primary_CIF");
			PersonalLoanS.mLogger.info("RLOS value of ReturnDesc"+"Customer Eligibility");
			ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";


			PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);

			formObject.setNGValue("Is_Customer_Eligibility","Y");

			if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCode)){
				ReadXml.valueSetIntegration(outputResponse,""); 
				parse_cif_eligibility(outputResponse,"Primary_CIF");
				BlacklistFlag= outputResponse.contains("<BlacklistFlag>") ? outputResponse.substring(outputResponse.indexOf("<BlacklistFlag>")+"</BlacklistFlag>".length()-1,outputResponse.indexOf("</BlacklistFlag>")):"";
				PersonalLoanS.mLogger.info("PL value of BlacklistFlag_Part"+"Customer is BlacklistedFlag"+BlacklistFlag);
				DuplicationFlag= outputResponse.contains("<DuplicationFlag>") ? outputResponse.substring(outputResponse.indexOf("<DuplicationFlag>")+"</DuplicationFlag>".length()-1,outputResponse.indexOf("</DuplicationFlag>")):"";
				PersonalLoanS.mLogger.info("PL value of BlacklistFlag_Part"+"Customer is DuplicationFlag"+DuplicationFlag);
				NegatedFlag= outputResponse.contains("<NegatedFlag>") ? outputResponse.substring(outputResponse.indexOf("<NegatedFlag>")+"</NegatedFlag>".length()-1,outputResponse.indexOf("</NegatedFlag>")):"";
				PersonalLoanS.mLogger.info("PL value of BlacklistFlag_Part"+"Customer is NegatedFlag"+NegatedFlag);
				if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCode)|| "000".equalsIgnoreCase(ReturnCode)){
					ReadXml.valueSetIntegration(outputResponse,"Primary_CIF");    
					formObject.setNGValue("Is_Customer_Eligibility","Y");
					formObject.RaiseEvent("WFSave");
					PersonalLoanS.mLogger.info("RLOS value of ReturnDesc Is_Customer_Eligibility"+formObject.getNGValue("Is_Customer_Eligibility"));
					formObject.setNGValue("BlacklistFlag",BlacklistFlag);
					formObject.setNGValue("DuplicationFlag",DuplicationFlag);
					formObject.setNGValue("IsAcctCustFlag",NegatedFlag);
					String NTB_flag = formObject.getNGValue("cmplx_Customer_NTB");
					if("true".equalsIgnoreCase(NTB_flag)){
						PersonalLoanS.mLogger.info("inside Customer Eligibility to through Exception to Exit:");
						alert_msg = NGFUserResourceMgr_PL.getAlert("val030");//"Customer is a New to Bank Customer.";
					}
					else{
						alert_msg = NGFUserResourceMgr_PL.getAlert("val031");//"Customer is an Existing Customer.";
					}

				}
				else{
					formObject.setNGValue("Is_Customer_Eligibility","N");
					formObject.RaiseEvent("WFSave");
				}
				PersonalLoanS.mLogger.info("RLOS value of Customer Details"+formObject.getNGValue("Is_Customer_Eligibility"));
				PersonalLoanS.mLogger.info("RLOS value of BlacklistFlag"+formObject.getNGValue("BlacklistFlag"));
				PersonalLoanS.mLogger.info("RLOS value of DuplicationFlag"+formObject.getNGValue("DuplicationFlag"));
				PersonalLoanS.mLogger.info("RLOS value of IsAcctCustFlag"+formObject.getNGValue("IsAcctCustFlag"));
				formObject.RaiseEvent("WFSave");
				throw new ValidatorException(new FacesMessage(alert_msg));

			}

		}
		//Customer Details Call on Dedupe Summary Button as well(Tanshu Aggarwal-29/05/2017)


		else if ("DecisionHistory_Button2".equalsIgnoreCase(pEvent.getSource().getName())) {

			if("Primary".equalsIgnoreCase(MultipleAppGridSelectedRow("MultipleApp_AppType"))){
				PersonalLoanS.mLogger.info(""+"inside create cif button");
				outputResponse = genX.GenerateXML("NEW_ACCOUNT_REQ","");
				ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);
				ReturnDesc =  outputResponse.contains("<ReturnDesc>") ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
				PersonalLoanS.mLogger.info("RLOS value of ReturnDesc"+ReturnDesc);
				String NewAcid= outputResponse.contains("<NewAcid>") ? outputResponse.substring(outputResponse.indexOf("<NewAcid>")+"</NewAcid>".length()-1,outputResponse.indexOf("</NewAcid>")):"";
				String IBANNumber= outputResponse.contains("<IBANNumber>") ? outputResponse.substring(outputResponse.indexOf("<IBANNumber>")+"</IBANNumber>".length()-1,outputResponse.indexOf("</IBANNumber>")):"";
				if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode)){
					ReadXml.valueSetIntegration(outputResponse,"");    
					PersonalLoanS.mLogger.info("RLOS value of Account Request"+"after valuesetcudtomer");
					formObject.setNGValue("Is_Account_Create","Y");
					formObject.setNGValue("EligibilityStatus","Y");
					formObject.setNGValue("EligibilityStatusCode","Y");
					formObject.setNGValue("EligibilityStatusDesc","Y");
					formObject.setNGValue("Account_Number",NewAcid);
					formObject.setNGValue("IBAN_Number",IBANNumber);

					PersonalLoanS.mLogger.info("RLOS value of Account Request NewAcid"+formObject.getNGValue("Account_Number"));
					PersonalLoanS.mLogger.info("RLOS value of Account Request IBANNumber"+formObject.getNGValue("IBAN_Number"));
					PersonalLoanS.mLogger.info("RLOS value of Account Request cmplx_Decision_IBAN"+formObject.getNGValue("cmplx_Decision_IBAN"));
					PersonalLoanS.mLogger.info("RLOS value of Account Request cmplx_Decision_AccountNo"+formObject.getNGValue("cmplx_Decision_AccountNo"));
				}
				else{
					formObject.setNGValue("Is_Account_Create","N");
				}


				outputResponse = genX.GenerateXML("NEW_CUSTOMER_REQ","PRIMARY_CIF");//$$ akshay 20/2/18
				ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);
				//ReturnDesc =  outputResponse.contains("<ReturnDesc>") ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
				//PersonalLoanS.mLogger.info("RLOS value of ReturnDesc"+ReturnDesc);
				if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode) ){
					ReadXml.valueSetIntegration(outputResponse,"");    
					PersonalLoanS.mLogger.info("PL value of ReturnDesc"+"Inside if of New customer Req");
					formObject.setNGValue("Is_Customer_Create","Y");
					alert_msg = NGFUserResourceMgr_PL.getAlert("val062");
				}
				else{
					PersonalLoanS.mLogger.info("PL value of ReturnDesc"+"Inside else of New Customer Req");
					formObject.setNGValue("Is_Customer_Create","N");
					alert_msg = NGFUserResourceMgr_PL.getAlert("val063");
				}
			}
			//added by akshay on 20/2/18 for drop 4
			else if("Supplement".equalsIgnoreCase(MultipleAppGridSelectedRow("MultipleApp_AppType"))){
				PersonalLoanS.mLogger.info("Select row data is: "+formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"), 0));
				outputResponse = genX.GenerateXML("NEW_CUSTOMER_REQ","SUPPLEMENT_CIF");
				ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				PersonalLoanS.mLogger.info("PL DDVT Checker"+ "Guarantor CIf ReturnCode: "+ReturnCode);
				if("0000".equalsIgnoreCase(ReturnCode))
				{
					PersonalLoanS.mLogger.info("PL DDVT Checker"+ "Guarantor CIf created sucessfully!!!");
					alert_msg = NGFUserResourceMgr_PL.getAlert("val077");
					String CIf_value=outputResponse.contains("<CIFId>") ? outputResponse.substring(outputResponse.indexOf("<CIFId>")+"</CIFId>".length()-1,outputResponse.indexOf("</CIFId>")):"";
					formObject.setNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"), 3, CIf_value);
					//Added by shivanshi for PCASI-PCASI-3427
					for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_supplementGrid");i++){
						String appName = "";
						PersonalLoanS.mLogger.info("PL Common Code"+ "inside for shivanshi");
						if("null".equalsIgnoreCase(formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,1)) && formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,1)!=null){
							appName = formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,0)+" "+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,1)+" "+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,2);
						}else{
							appName = formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,0)+" "+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,2);
						}
						String multipleAppName = formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),1);
						PersonalLoanS.mLogger.info("PL Common Code"+ "Applicant name in supplemetary grid shivanshi--"+appName);
						PersonalLoanS.mLogger.info("PL Common Code"+ "cmplx_Decision_MultipleApplicantsGridshivanshi--"+multipleAppName);
						if(multipleAppName.equalsIgnoreCase(appName)){
							PersonalLoanS.mLogger.info("PL Common Code"+ "Inside if shivanshi--"+multipleAppName);
							formObject.setNGValue("SupplementCardDetails_cmplx_supplementGrid",i,33,CIf_value);
							break;
						}
					}
					//Added by shivanshi for PCASI-PCASI-3427
					formObject.setLocked("DecisionHistory_Button2", true);
				}
				else{
					alert_msg = NGFUserResourceMgr_PL.getAlert("val078");
				}
			}

			formObject.RaiseEvent("WFSave");
			throw new ValidatorException(new FacesMessage(alert_msg));

		}

		else if("SupplementCardDetails_FetchDetails".equalsIgnoreCase(pEvent.getSource().getName())){

			String EmiratesID = formObject.getNGValue("SupplementCardDetails_Text6");
			PersonalLoanS.mLogger.info("CC_DDVT_MAker "+ "EID value for Entity Details for Supplementary Cards: "+EmiratesID);
			String primaryCif = null;
			boolean isEntityDetailsSuccess = false;

			if( EmiratesID!=null && !"".equalsIgnoreCase(EmiratesID)){
				outputResponse = genX.GenerateXML("ENTITY_DETAILS","Supplementary_Card_Details");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				PersonalLoanS.mLogger.info("CC_DDVT_MAker value of ReturnCode for Supplementary Cards: "+ReturnCode);
				if("0000".equalsIgnoreCase(ReturnCode)){
					//valueSetCustomer(outputResponse , "Supplementary_Card_Details");
					primaryCif = (outputResponse.contains("<CIFID>")) ? outputResponse.substring(outputResponse.indexOf("<CIFID>")+"</CIFID>".length()-1,outputResponse.indexOf("</CIFID>")):"";
					formObject.setNGValue("Supplementary_CIFNO",primaryCif);
					isEntityDetailsSuccess = true;
					alert_msg = fetch_cust_details_supplementary();
				}

				PersonalLoanS.mLogger.info("CC_DDVT_MAker value of Primary Cif"+primaryCif);
			}
			if(!isEntityDetailsSuccess || (primaryCif==null || "".equalsIgnoreCase(primaryCif))){
				outputResponse =genX.GenerateXML("CUSTOMER_ELIGIBILITY","Supplementary_Card_Details");
				PersonalLoanS.mLogger.info("CC_DDVT_MAker value of ReturnDesc for Supplementary Cards: "+"Customer Eligibility");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				PersonalLoanS.mLogger.info("CC_DDVT_MAker value of ReturnCode for Supplementary Cards: "+ReturnCode);
				if("0000".equalsIgnoreCase(ReturnCode) )
				{
					ReadXml.valueSetIntegration(outputResponse,"");    
					String NTBFlag=(outputResponse.contains("<DuplicationFlag>")) ? outputResponse.substring(outputResponse.indexOf("<DuplicationFlag>")+"</DuplicationFlag>".length()-1,outputResponse.indexOf("</DuplicationFlag>")):"";
					if("Y".equals(NTBFlag)){
						alert_msg = NGFUserResourceMgr_PL.getAlert("val030");	
						formObject.setLocked("SupplementCardDetails_FetchDetails", true);
					}
					else{
						alert_msg = fetch_cust_details_supplementary();
					}
					

				}
			}
			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		
		else if("DecisionHistory_CifLock".equalsIgnoreCase(pEvent.getSource().getName()) || "PartMatch_CifLock".equalsIgnoreCase(pEvent.getSource().getName())){
			outputResponse = new PL_Integration_Input().GenerateXML("CUSTOMER_DETAILS","CIF_LOCK");
			ReturnCode = outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCode)  || "CINF376001".equalsIgnoreCase(ReturnCode)) //Modified if for PCASI - 3559
			{
				PersonalLoanS.mLogger.info("PL DDVT Checker"+ "Locked P-CIF sucessfully!!!::"+ formObject.getNGValue("cmplx_Customer_CIFNO"));
				
				//formObject.setNGValue("Is_CustLock", "Y");
				//formObject.setNGValue("cmplx_Decision_MultipleApplicantsGrid", formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),6,"Y");
				//formObject.setNGValue("cmplx_PartMatch_CIF_Lock_Status","Y");
				//alert_msg = NGFUserResourceMgr_PL.getAlert("val060");
				//formObject.RaiseEvent("WFSave");
				//check minor line 4158 copy
				
				String age=formObject.getNGValue("cmplx_Customer_age");
				float age_1 = Float.parseFloat(age);
				PersonalLoanS.mLogger.info("Check minor case:"+age_1);
				PersonalLoanS.mLogger.info("Inside G lock cif");
				PersonalLoanS.mLogger.info("formObject.getNGValue minor"+formObject.getNGValue("cmplx_Customer_minor"));
				if(formObject.getNGValue("cmplx_Customer_minor").equalsIgnoreCase("true"));
				{
					PersonalLoanS.mLogger.info("Inside G lock cif!!");
					outputResponse = new PL_Integration_Input().GenerateXML("CUSTOMER_DETAILS","G_CIF_LOCK");
					ReturnCode = outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					PersonalLoanS.mLogger.info("Inside G lock cif::::"+ReturnCode);
					if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCode)  || "CINF376001".equalsIgnoreCase(ReturnCode)) //Modified if for PCASI - 3559
					{
						PersonalLoanS.mLogger.info("PL DDVT Checker"+ "Locked G-cif sucessfully!!!::"+formObject.getNGValue("cmplx_Customer_guarcif"));
						//formObject.setNGValue("Is_CustLock", "Y");
					}
					//formObject.RaiseEvent("WFSave");
				}
				formObject.setNGValue("Is_CustLock", "Y");
				formObject.setNGValue("cmplx_PartMatch_CIF_Lock_Status","Y");
				formObject.setNGValue("cmplx_Decision_MultipleApplicantsGrid", formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),6,"Y");
				alert_msg = NGFUserResourceMgr_PL.getAlert("val060");
				formObject.RaiseEvent("WFSave");
			}//3353
			else{
				alert_msg = NGFUserResourceMgr_PL.getAlert("val049");
			}			
			throw new ValidatorException(new FacesMessage(alert_msg));
		}	

		else if("DecisionHistory_CifUnlock".equalsIgnoreCase(pEvent.getSource().getName()) || "PartMatch_CifUnlock".equalsIgnoreCase(pEvent.getSource().getName())){
			outputResponse = new PL_Integration_Input().GenerateXML("CUSTOMER_DETAILS","CIF_UNLOCK");
			ReturnCode =  outputResponse.contains("<ReturnCode>")? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";

			if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCode) || "CINF376002".equalsIgnoreCase(ReturnCode))
			{
				//formObject.setNGValue("Is_CustLock", "N");
				//formObject.setNGValue("cmplx_Decision_MultipleApplicantsGrid", formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),6,"N");
				//formObject.setNGValue("cmplx_PartMatch_CIF_Lock_Status","N");
				PersonalLoanS.mLogger.info("UnLocked P cif sucessfully!!");
				//formObject.RaiseEvent("WFSave");
				//alert_msg = NGFUserResourceMgr_PL.getAlert("val061");
				
				String age=formObject.getNGValue("cmplx_Customer_age");
				double age_1 = Double.parseDouble(age);
				PersonalLoanS.mLogger.info("Check minor case:"+age_1);
				PersonalLoanS.mLogger.info("Inside G lock cif");
				PersonalLoanS.mLogger.info("formObject.getNGValue minor"+formObject.getNGValue("cmplx_Customer_minor"));
				if(formObject.getNGValue("cmplx_Customer_minor").equalsIgnoreCase("true"));
				{
					PersonalLoanS.mLogger.info("Inside G lock cif!!");
					outputResponse = new PL_Integration_Input().GenerateXML("CUSTOMER_DETAILS","G_CIF_UNLOCK");
					ReturnCode = outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					PersonalLoanS.mLogger.info("Inside G lock cif"+ReturnCode);
					if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCode)  || "CINF376001".equalsIgnoreCase(ReturnCode)) //Modified if for PCASI - 3559
					{
						PersonalLoanS.mLogger.info("PL DDVT Checker"+ "unLocked G-cif sucessfully!!!");
						//formObject.setNGValue("Is_CustLock", "Y");
					}
					//formObject.RaiseEvent("WFSave");
				}
				formObject.setNGValue("Is_CustLock", "N");
				formObject.setNGValue("cmplx_PartMatch_CIF_Lock_Status","N");
				formObject.setNGValue("cmplx_Decision_MultipleApplicantsGrid", formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),6,"N");
				alert_msg = NGFUserResourceMgr_PL.getAlert("val061");
				formObject.RaiseEvent("WFSave");
			//3353
			}
			else{
				alert_msg = NGFUserResourceMgr_PL.getAlert("val043");
			}
			throw new ValidatorException(new FacesMessage(alert_msg));
		}		


		else if ("Product_Add".equalsIgnoreCase(pEvent.getSource().getName())){

			formObject.setNGValue("Product_wi_name",formObject.getWFWorkitemName());
			PersonalLoanS.mLogger.info( "Inside add button: "+formObject.getNGValue("Product_wi_name"));
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Product_cmplx_ProductGrid");

			formObject.setNGValue("Product_type","Conventional",false);
			formObject.setNGValue("ReqProd","--Select--",false);
			formObject.setNGValue("AppType","--Select--",false);
			//formObject.setNGValue("EmpType","--Select--",false);//Tarang to be removed on friday(1/19/2018)
			formObject.setNGValue("EmploymentType","--Select--",false);
			formObject.setNGValue("Priority","Primary",false);
			formObject.setVisible("Product_Label5",false);
			formObject.setVisible("Product_Label3",false);
			formObject.setVisible("Product_Label6",false);
			formObject.setVisible("Product_Label10",false);
			formObject.setVisible("Product_Label12",false);
			formObject.setVisible("CardProd",false);
			formObject.setVisible("Scheme",false);
			formObject.setVisible("ReqTenor",false);
			formObject.setVisible("Product_Label15",false); 
			formObject.setVisible("Product_Label17",false); 
			formObject.setVisible("Product_Label16",false); 
			formObject.setVisible("Product_Label18",false);
			formObject.setVisible("Product_Label21",false); 
			formObject.setVisible("Product_Label22",false); 
			formObject.setVisible("Product_Label23",false); 
			formObject.setVisible("Product_Label24",false);
			formObject.setVisible("typeReq",false);
			formObject.setVisible("LimitAcc",false); 
			formObject.setVisible("LimitExpiryDate",false);
			formObject.setNGFrameState("Incomedetails", 1);
			formObject.setNGFrameState("EmploymentDetails", 1);
			formObject.setNGFrameState("EligibilityAndProductInformation", 1);
			formObject.setNGFrameState("Alt_Contact_container", 1);
			formObject.setNGFrameState("CC_Loan_container", 1);
			formObject.setNGFrameState("CompanyDetails", 1);
			formObject.setNGFrameState("CardDetails", 1);
		}              


		else if ("Product_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Product_cmplx_ProductGrid");
			formObject.setNGFrameState("Incomedetails", 1);
			formObject.setNGValue("Product_type","Conventional",false);
			formObject.setNGValue("ReqProd","--Select--",false);
			formObject.setNGValue("AppType","--Select--",false);
			//formObject.setNGValue("EmpType","--Select--",false);//Tarang to be removed on friday(1/19/2018)
			formObject.setNGValue("EmploymentType","--Select--",false);
			formObject.setNGValue("Priority","Primary",false);
			//formObject.setVisible("Product_Label5",false);
			//formObject.setVisible("Product_Label3",false);
			formObject.setVisible("Product_Label6",false);
			formObject.setVisible("Product_Label10",false);
			formObject.setVisible("Product_Label12",false);
			//formObject.setVisible("CardProd",false);
			//formObject.setVisible("Scheme",false);
			//formObject.setVisible("ReqTenor",false);
			formObject.setVisible("Product_Label15",false); 
			formObject.setVisible("Product_Label17",false); 
			formObject.setVisible("Product_Label16",false); 
			formObject.setVisible("Product_Label18",false);
			formObject.setVisible("Product_Label21",false); 
			formObject.setVisible("Product_Label22",false); 
			formObject.setVisible("Product_Label23",false); 
			formObject.setVisible("Product_Label24",false);
			//formObject.setVisible("typeReq",false);
			//formObject.setVisible("LimitAcc",false); 
			//formObject.setVisible("LimitExpiryDate",false);
			formObject.setNGFrameState("Incomedetails", 1);
			formObject.setNGFrameState("EmploymentDetails", 1);
			formObject.setNGFrameState("EligibilityAndProductInformation", 1);
			formObject.setNGFrameState("Alt_Contact_container", 1);
			formObject.setNGFrameState("CC_Loan_container", 1);
			formObject.setNGFrameState("CompanyDetails", 1);
			formObject.setNGFrameState("CardDetails", 1);

			//Arun (12/11/17) CardProd chages to change the header for Application Type  
			//String cardprodval = formObject.getNGSelectedItemText("CardProd"); 
			//formObject.setNGValue("CardLabel",cardprodval);
		}

		else if ("Product_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_Product_cmplx_ProductGrid");

			formObject.setNGValue("Product_type","Conventional",false);
			formObject.setNGValue("ReqProd","--Select--",false);
			formObject.setNGValue("AppType","--Select--",false);
			//formObject.setNGValue("EmpType","--Select--",false);//Tarang to be removed on friday(1/19/2018)
			formObject.setNGValue("EmploymentType","--Select--",false);
			formObject.setNGValue("Priority","Primary",false);
			formObject.setVisible("Product_Label5",false);
			formObject.setVisible("Product_Label3",false);
			formObject.setVisible("Product_Label6",false);
			formObject.setVisible("Product_Label10",false);
			formObject.setVisible("Product_Label12",false);
			formObject.setVisible("CardProd",false);
			formObject.setVisible("Scheme",false);
			formObject.setVisible("ReqTenor",false);
			formObject.setVisible("Product_Label15",false); 
			formObject.setVisible("Product_Label17",false); 
			formObject.setVisible("Product_Label16",false); 
			formObject.setVisible("Product_Label18",false);
			formObject.setVisible("Product_Label21",false); 
			formObject.setVisible("Product_Label22",false); 
			formObject.setVisible("Product_Label23",false); 
			formObject.setVisible("Product_Label24",false);
			formObject.setVisible("typeReq",false);
			formObject.setVisible("LimitAcc",false); 
			formObject.setVisible("LimitExpiryDate",false);
			formObject.setNGFrameState("Incomedetails", 1);
			formObject.setNGFrameState("EmploymentDetails", 1);
			formObject.setNGFrameState("EligibilityAndProductInformation", 1);
			formObject.setNGFrameState("Alt_Contact_container", 1);
			formObject.setNGFrameState("CC_Loan_container", 1);
			formObject.setNGFrameState("CompanyDetails", 1);
			formObject.setNGFrameState("CardDetails", 1);
		}

		else if ("PartMatch_Search".equalsIgnoreCase(pEvent.getSource().getName())){

			PersonalLoanS.mLogger.info("PL PartMatch_Search"+ "Inside PartMatch_Search button: ");

			formObject.clear("cmplx_PartMatch_cmplx_Partmatch_grid");
			outputResponse = genX.GenerateXML("DEDUP_SUMMARY","");
			ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			PersonalLoanS.mLogger.info("PL value of ReturnCode"+ReturnCode);
			ReturnDesc =  outputResponse.contains("<ReturnDesc>") ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";          
			PersonalLoanS.mLogger.info("PL value of ReturnDesc"+ReturnDesc);
			if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode) ){
				//change by saurabh on 11th July 17.
				formObject.clear("cmplx_PartMatch_cmplx_Partmatch_grid");
				PL_Integration_Output.parseDedupe_summary(outputResponse);
				formObject.setNGValue("Is_PartMatchSearch","Y");
				formObject.setLocked("PartMatch_Blacklist", false);
				PersonalLoanS.mLogger.info("PL value of Part Match Request"+"inside if of partmatch");
				alert_msg=NGFUserResourceMgr_PL.getAlert("val032");
			}
			else
			{
				formObject.setNGValue("Is_PartMatchSearch","N");
				formObject.setLocked("PartMatch_Blacklist", true);
				PersonalLoanS.mLogger.info("PL value of Part Match Request"+"inside else of partmatch");
				alert_msg= NGFUserResourceMgr_PL.getAlert("val033");//"Error while performing Part match";
			}
			// changed by abhishek start for populating cutomer info grid 22may2017
			if(formObject.isVisible("FinacleCRMCustInfo_Frame1")==false){
				expandFinacleCRMCustInfo();
				adjustFrameTops("FinacleCRM_CustInfo,External_Blacklist,Finacle_Core,MOL,World_Check,Reject_Enq,Sal_Enq,Credit_card_Enq1,LOS1,Case_History1");
			}
			formObject.RaiseEvent("WFSave");
			throw new ValidatorException(new FacesMessage(alert_msg));

			/*PersonalLoanS.mLogger.info("PL value of Part Match Request"+formObject.getNGValue("Is_PartMatchSearch"));
			if(NGFUserResourceMgr_PL.getGlobalVar("PL_Y").equalsIgnoreCase(formObject.getNGValue("Is_PartMatchSearch")))
			{ 
				PersonalLoanS.mLogger.info("PL value of Is_CUSTOMER_UPDATE_REQ"+"inside if condition of disabling the button");

			}
			else{
				formObject.setEnabled("PartMatch_Search", true);

			}
			String activityName = formObject.getWFActivityName();*/

		}

		//code changes by bandana starts
		
		else if("BTC_save".equalsIgnoreCase(pEvent.getSource().getName()) ){
			//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
			Custom_fragmentSave("Details");

			alert_msg=NGFUserResourceMgr_PL.getAlert("VAL090");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		else if("DDS_save".equalsIgnoreCase(pEvent.getSource().getName())){
			//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
			Custom_fragmentSave("Details");

			alert_msg=NGFUserResourceMgr_PL.getAlert("VAL075");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		else if("SI_save".equalsIgnoreCase(pEvent.getSource().getName()) ){
			//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
			Custom_fragmentSave("Details");

			alert_msg=NGFUserResourceMgr_PL.getAlert("VAL076");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		else if("RVC_Save".equalsIgnoreCase(pEvent.getSource().getName())){
			//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
			Custom_fragmentSave("Details");

			alert_msg=NGFUserResourceMgr_PL.getAlert("VAL077");

			throw new ValidatorException(new FacesMessage(alert_msg));
		}
		//code changes by bandana ends

		//for BlackList Call added on 3rd May 2017
		//Edited by Imran 12.30.19
		else if ("PartMatch_Blacklist".equalsIgnoreCase(pEvent.getSource().getName())){
			formObject.clear("cmplx_PartMatch_cmplx_PartBlacklistGrid");
			try{
				PersonalLoanS.mLogger.info("PL value of ReturnDesc"+"Blacklist Details part Match1111");
				formObject.fetchFragment("FinacleCRM_CustInfo", "FinacleCRMCustInfo", "q_FinCRMCustInfo");
				formObject.setNGFrameState("FinacleCRM_CustInfo",0);
				adjustFrameTops("FinacleCRM_CustInfo,External_Blacklist,Finacle_Core,MOL,World_Check,Reject_Enq,Sal_Enq,Credit_card_Enq1,LOS1,Case_History1");


				String CifID="";
				String CustomerStatus="";
				String firstName="";
				String LastName="";					
				String OldPassportNumber=""; 
				String PassportNumber="";
				String Visa="";
				String EmirateID="";
				String PhoneNo="";
				String RetailCorpFlag="";
				//added here
				String StatusType="";
				String Wi_Name=formObject.getWFWorkitemName();

				//ended here

				// added on 11may 2017

				PersonalLoanS.mLogger.info("PL value of ReturnDesc"+"Blacklist Details part Match1111 after initializing strings");
				outputResponse =genX.GenerateXML("BLACKLIST_DETAILS","");
				PersonalLoanS.mLogger.info("PL value of ReturnDesc"+"Blacklist Details part Match");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				PersonalLoanS.mLogger.info("PL value of ReturnCode part Match"+ReturnCode);
				ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
				PersonalLoanS.mLogger.info("PL value of ReturnDesc part Match"+ReturnDesc);

				if("0000".equalsIgnoreCase(ReturnCode) ){
					alert_msg = "BlackList Check Successfull." ;
					
					formObject.setNGValue("Is_Customer_Eligibility_Part","Y");   
					PersonalLoanS.mLogger.info("PL value of BlacklistFlag_Part Customer is Blacklisted StatusType"+formObject.getNGValue("Is_Customer_Eligibility_Part"));
					StatusType= (outputResponse.contains("<StatusType>")) ? outputResponse.substring(outputResponse.indexOf("<StatusType>")+"</StatusType>".length()-1,outputResponse.indexOf("</StatusType>")):"";
					PersonalLoanS.mLogger.info("PL value of BlacklistFlag_Part"+"Customer is Blacklisted StatusType"+StatusType);
					//Deepak changes done for PCAS-2458
					outputResponse = outputResponse.substring(outputResponse.indexOf("<CustomerBlackListResponse>")+27, outputResponse.lastIndexOf("</CustomerBlackListResponse>"));
					PersonalLoanS.mLogger.info(outputResponse);
					outputResponse = "<?xml version=\"1.0\"?><Response><CustomerBlackListResponse>" + outputResponse;
					outputResponse = outputResponse+"</CustomerBlackListResponse></Response>";

					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = factory.newDocumentBuilder();
					InputSource is = new InputSource(new StringReader(outputResponse));

					Document doc = builder.parse(is);
					doc.getDocumentElement().normalize();
					NodeList nListMain = doc.getElementsByTagName("CustomerBlackListResponse");
					PersonalLoanS.mLogger.info("check 3");

					CustomerStatus =  (outputResponse.contains("<CustomerStatus>")) ? outputResponse.substring(outputResponse.indexOf("<CustomerStatus>")+"</CustomerStatus>".length()-1,outputResponse.indexOf("</CustomerStatus>")):"";

					for (int temp1 = 0; temp1 < nListMain.getLength(); temp1++) {
						Node nNode_custblk = nListMain.item(temp1);
						Element CustomerBlackListElement = (Element) nNode_custblk;
						PersonalLoanS.mLogger.info("check 5");

						NodeList StatusInfoList =CustomerBlackListElement.getElementsByTagName("StatusInfo");
						//NodeList StatusInfoList = doc.getElementsByTagName("StatusInfo");
						CifID=CustomerBlackListElement.getElementsByTagName("CIFID").item(0).getTextContent();
						RetailCorpFlag=CustomerBlackListElement.getElementsByTagName("RetailCorpFlag").item(0).getTextContent();
						PersonalLoanS.mLogger.info("check 6");

						try{
							NodeList documentList =CustomerBlackListElement.getElementsByTagName("Document");
							for(int doc_count=0;doc_count<documentList.getLength();doc_count++){
								Node nNode_documentList = documentList.item(doc_count);
								Element documentElement = (Element) nNode_documentList;
								if("PPT".equalsIgnoreCase(documentElement.getElementsByTagName("DocumentType").item(0).getTextContent())){
									PassportNumber = documentElement.getElementsByTagName("DocumentRefNumber").item(0).getTextContent();
								}
								else if("VISA".equalsIgnoreCase(documentElement.getElementsByTagName("DocumentType").item(0).getTextContent())){
									Visa = documentElement.getElementsByTagName("DocumentRefNumber").item(0).getTextContent();
								}
								else if("OPPT".equalsIgnoreCase(documentElement.getElementsByTagName("DocumentType").item(0).getTextContent())){
									OldPassportNumber = documentElement.getElementsByTagName("DocumentRefNumber").item(0).getTextContent();
								}
								else if("EMID".equalsIgnoreCase(documentElement.getElementsByTagName("DocumentType").item(0).getTextContent())){
									EmirateID = documentElement.getElementsByTagName("DocumentRefNumber").item(0).getTextContent();
								}
							}
						}
						catch(Exception e){
							PersonalLoanS.mLogger.info("Exception occured while parsing document type: "+e.getMessage());
						}
						/*OldPassportNumber=formObject.getNGValue("PartMatch_oldpass");
						PassportNumber=formObject.getNGValue("PartMatch_newpass");
						Visa=formObject.getNGValue("PartMatch_visafno");
						EmirateID=formObject.getNGValue("PartMatch_EID");*/
						PhoneNo=formObject.getNGValue("PartMatch_mno1");
						PersonalLoanS.mLogger.info("check 7");
						if(StatusInfoList!=null){
							for (int temp = 0; temp < StatusInfoList.getLength(); temp++) {
								PersonalLoanS.mLogger.info("check 8");
	
								Node nNode = StatusInfoList.item(temp);
								if (nNode.getNodeType() == Node.ELEMENT_NODE) {
									Element StatusInfoElement = (Element) nNode;
									PersonalLoanS.mLogger.info("check 9");
	
									firstName=CustomerBlackListElement.getElementsByTagName("FirstName").item(0).getTextContent();
									LastName=CustomerBlackListElement.getElementsByTagName("LastName").item(0).getTextContent();
									PersonalLoanS.mLogger.info("check 10");
	
									StatusType = StatusInfoElement.getElementsByTagName("StatusType").item(0).getTextContent() ;
									PersonalLoanS.mLogger.info("StatusType: "+StatusType);
									NodeList StatusDetailsList = StatusInfoElement.getElementsByTagName("StatusDetails");
	
									if(StatusDetailsList.getLength()==0){
										String Negatedflag="";
										String Negatednote="";
										String NegatedCode="";
										String NegatedReason="";
										String Blacklistflag="";
										String Blacklistnote="";
										String BlacklistCode="";
										String BlacklistReason="";
										if("Black List".equalsIgnoreCase(StatusType)){
											if(StatusInfoElement.getElementsByTagName("StatusFlag")!=null && StatusInfoElement.getElementsByTagName("StatusFlag").item(0)!=null){
												Blacklistflag = StatusInfoElement.getElementsByTagName("StatusFlag").item(0).getTextContent() ;
											}
										}
										else{
											if(StatusInfoElement.getElementsByTagName("StatusFlag")!=null && StatusInfoElement.getElementsByTagName("StatusFlag").item(0)!=null){
												Negatedflag = StatusInfoElement.getElementsByTagName("StatusFlag").item(0).getTextContent() ;
											}
										}
										List<String> BlacklistGrid = new ArrayList<String>(); 
										BlacklistGrid.add(CifID);
										BlacklistGrid.add(CustomerStatus); 
										BlacklistGrid.add(firstName);
										BlacklistGrid.add(LastName);
										BlacklistGrid.add(Blacklistflag);
										BlacklistGrid.add(Blacklistnote);
										BlacklistGrid.add(BlacklistReason);
										BlacklistGrid.add(BlacklistCode);
										BlacklistGrid.add(Negatedflag);
										BlacklistGrid.add(Negatednote);
										BlacklistGrid.add(NegatedReason);
										BlacklistGrid.add(NegatedCode);
										BlacklistGrid.add(PassportNumber);
										BlacklistGrid.add(OldPassportNumber);
										BlacklistGrid.add(EmirateID);
										BlacklistGrid.add(Visa);
										BlacklistGrid.add(PhoneNo);
										BlacklistGrid.add(Wi_Name);
										BlacklistGrid.add("");
	
										formObject.addItemFromList("cmplx_PartMatch_cmplx_PartBlacklistGrid",BlacklistGrid);
										PersonalLoanS.mLogger.info("final list:: "+ BlacklistGrid.toString());
									}
									else{
										for (int listcount=0;listcount<StatusDetailsList.getLength();listcount++){
											String Negatedflag="";
											String Negatednote="";
											String NegatedCode="";
											String NegatedReason="";
											String Blacklistflag="";
											String Blacklistnote="";
											String BlacklistCode="";
											String BlacklistReason="";
											String Created_date="";
											Element StatusDetailsElement = (Element)StatusDetailsList.item(listcount);
											if("Black List".equalsIgnoreCase(StatusType)){
												//	Blacklistflag= (outputResponse.contains("<StatusFlag>")) ? outputResponse.substring(outputResponse.indexOf("<StatusFlag>")+"</StatusFlag>".length()-1,outputResponse.indexOf("</StatusFlag>")):"";
												if(StatusInfoElement.getElementsByTagName("StatusFlag")!=null && StatusInfoElement.getElementsByTagName("StatusFlag").item(0)!=null){
													Blacklistflag = StatusInfoElement.getElementsByTagName("StatusFlag").item(0).getTextContent() ;
												}
												if(StatusDetailsElement.getElementsByTagName("ReasonNotes")!=null && StatusDetailsElement.getElementsByTagName("ReasonNotes").item(0)!=null){
													Blacklistnote = StatusDetailsElement.getElementsByTagName("ReasonNotes").item(0).getTextContent() ;
												}
												if(StatusDetailsElement.getElementsByTagName("ReasonCodeDesc")!=null && StatusDetailsElement.getElementsByTagName("ReasonCodeDesc").item(0)!=null){
													BlacklistReason = StatusDetailsElement.getElementsByTagName("ReasonCodeDesc").item(0).getTextContent() ;
												}
												if(StatusDetailsElement.getElementsByTagName("ReasonCode")!=null && StatusDetailsElement.getElementsByTagName("ReasonCode").item(0)!=null){
													BlacklistCode = StatusDetailsElement.getElementsByTagName("ReasonCode").item(0).getTextContent() ;
												}
												if(StatusDetailsElement.getElementsByTagName("CreationDate")!=null && StatusDetailsElement.getElementsByTagName("CreationDate").item(0)!=null){
													Created_date = StatusDetailsElement.getElementsByTagName("CreationDate").item(0).getTextContent() ;
												}
												PersonalLoanS.mLogger.info("PL value of BlacklistFlag_Part Customer is Blacklisted StatusCode"+BlacklistCode);
												//change by saurabh on 29th April
												if(Blacklistflag.equalsIgnoreCase("Y")){
													Blacklistflag = "Yes";
												}
												else if(Blacklistflag==null || Blacklistflag.equalsIgnoreCase("Y") || Blacklistflag.equalsIgnoreCase("")){
													Blacklistflag = "No";
												}
												alert_msg = "BlackList Check Successfull. Blacklist Flag: "+Blacklistflag ;  //alert change to show the blacklist at the frontend
											}
											if("Negative List".equalsIgnoreCase(StatusType)){
												if(StatusInfoElement.getElementsByTagName("StatusFlag")!=null && StatusInfoElement.getElementsByTagName("StatusFlag").item(0)!=null){
													Negatedflag = StatusInfoElement.getElementsByTagName("StatusFlag").item(0).getTextContent() ;
												}
												if(StatusDetailsElement.getElementsByTagName("ReasonNotes")!=null && StatusDetailsElement.getElementsByTagName("ReasonNotes").item(0)!=null){
													Negatednote = StatusDetailsElement.getElementsByTagName("ReasonNotes").item(0).getTextContent() ;
												}
												if(StatusDetailsElement.getElementsByTagName("ReasonCodeDesc")!=null && StatusDetailsElement.getElementsByTagName("ReasonCodeDesc").item(0)!=null){
													NegatedReason = StatusDetailsElement.getElementsByTagName("ReasonCodeDesc").item(0).getTextContent() ;
												}
												if(StatusDetailsElement.getElementsByTagName("ReasonCode")!=null && StatusDetailsElement.getElementsByTagName("ReasonCode").item(0)!=null){
													NegatedCode = StatusDetailsElement.getElementsByTagName("ReasonCode").item(0).getTextContent() ;
												}
												if(StatusDetailsElement.getElementsByTagName("CreationDate")!=null && StatusDetailsElement.getElementsByTagName("CreationDate").item(0)!=null){
													Created_date = StatusDetailsElement.getElementsByTagName("CreationDate").item(0).getTextContent() ;
												}
											}
											PersonalLoanS.mLogger.info("PL value of CustomerStatus"+"Customer is Blacklisted StatusType"+CustomerStatus);
											//below code added by nikhil 12/12/17
											List<String> BlacklistGrid = new ArrayList<String>(); 
											BlacklistGrid.add(CifID);
											BlacklistGrid.add(CustomerStatus); 
											BlacklistGrid.add(firstName);
											BlacklistGrid.add(LastName);
											BlacklistGrid.add(Blacklistflag);
											BlacklistGrid.add(Blacklistnote);
											BlacklistGrid.add(BlacklistReason);
											BlacklistGrid.add(BlacklistCode);
											BlacklistGrid.add(Negatedflag);
											BlacklistGrid.add(Negatednote);
											BlacklistGrid.add(NegatedReason);
											BlacklistGrid.add(NegatedCode);
											BlacklistGrid.add(PassportNumber);
											BlacklistGrid.add(OldPassportNumber);
											BlacklistGrid.add(EmirateID);
											BlacklistGrid.add(Visa);
											BlacklistGrid.add(PhoneNo);
											BlacklistGrid.add(Wi_Name);
											BlacklistGrid.add(Created_date);
	
											formObject.addItemFromList("cmplx_PartMatch_cmplx_PartBlacklistGrid",BlacklistGrid);
											PersonalLoanS.mLogger.info("final list: "+listcount+" : "+ BlacklistGrid.toString());
										}
									}
	
								}
							}
						}
					}
				}
					else if("CINF0516".equalsIgnoreCase(ReturnCode) ){
						formObject.setNGValue("Is_Customer_Eligibility_Part","Y");
						String alert=formObject.getDataFromDataSource("select Alert from ng_MASTER_INTEGRATION_ERROR_CODE with (nolock) where Error_Code='"+ReturnCode+"' union all select Alert from ng_MASTER_INTEGRATION_ERROR_CODE with (nolock) where Error_Code='2033'").get(0).get(0);
						PersonalLoanS.mLogger.info( ReturnCode+": "+alert);
						alert_msg= alert;//NGFUserResourceMgr_CreditCard.getAlert("VAL015");
					}
					else{
						formObject.setNGValue("Is_Customer_Eligibility_Part","N");  
						alert_msg = "BlackList Check failed Please contact administrator";
						PersonalLoanS.mLogger.info("PL value of BlacklistFlag_Part Customer is Blacklisted StatusType"+formObject.getNGValue("Is_Customer_Eligibility_Part"));
					}
					
					/*List<String> BlacklistGrid = new ArrayList<String>(); 
					BlacklistGrid.add(CifID);
					BlacklistGrid.add(CustomerStatus); 
					BlacklistGrid.add(firstName);
					BlacklistGrid.add(LastName);
					BlacklistGrid.add(Blacklistflag);
					BlacklistGrid.add(Blacklistnote);
					BlacklistGrid.add(BlacklistReason);
					BlacklistGrid.add(BlacklistCode);
					BlacklistGrid.add(Negatedflag);
					BlacklistGrid.add(Negatednote);
					BlacklistGrid.add(NegatedReason);
					BlacklistGrid.add(NegatedCode);
					BlacklistGrid.add(PassportNumber);
					BlacklistGrid.add(OldPassportNumber);
					BlacklistGrid.add(EmirateID);
					BlacklistGrid.add(Visa);
					BlacklistGrid.add(PhoneNo);
					BlacklistGrid.add(formObject.getWFWorkitemName());*/
					PersonalLoanS.mLogger.info("PL value of BlacklistFlag_Part"+"after adding in the grid");
					//PersonalLoanS.mLogger.info("RLOS Common# getOutputXMLValues()"+ "$$AKSHAY$$ List to be added in Blacklist grid: "+ BlacklistGrid);
					
					//formObject.addItemFromList("cmplx_PartMatch_cmplx_PartBlacklistGrid",BlacklistGrid);
					PersonalLoanS.mLogger.info("PL value of BlacklistFlag_Part"+"after adding in the grid11111");       

				}
				catch(ValidatorException ve){
					alert_msg="BlackList Check failed Please contact administrator";
					PersonalLoanS.mLogger.info("PL DDVT Maker"+ "Inside Validator exception"+alert_msg);
					throw new ValidatorException(new FacesMessage(alert_msg));
				}catch(Exception e){
					PersonalLoanS.mLogger.info("PL DDVT Maker"+ "Exception while checking blacklist"+e.getMessage());
					PLCommon.printException(e);
					alert_msg="BlackList Check failed Please contact administrator";

				}
				throw new ValidatorException(new FacesMessage(alert_msg));
			}

			//Changes done by aman to correctly save the value in the grid


			else if ("PartMatch_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
				if(Check_ECRNGenOnChange()){
					alert_msg=NGFUserResourceMgr_PL.getAlert("VAL116");
					throw new ValidatorException(new FacesMessage(alert_msg));
				}
				String BlacklistFlag_Part = "";
				String BlacklistFlag_reason = "";
				String BlacklistFlag_code = "";
				String NegativeListFlag = "";
				String NegativeListReason="";
				String NegativeListReasonCode="";

				try{
					outputResponse =genX.GenerateXML("CUSTOMER_DETAILS","PartMatch_CIF");//changed by akshay for proc 7395 on 1/4/18
					PersonalLoanS.mLogger.info("CC value of ReturnDesc"+"Customer Details part Match");
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					PersonalLoanS.mLogger.info("CC value of ReturnCode part Match"+ReturnCode);
					if("0000".equalsIgnoreCase(ReturnCode)){

						String Staff_cif = new PL_Integration_Output().getTagValue(outputResponse, "IsStaff");  
						if("Y".equalsIgnoreCase(Staff_cif)){
							PersonalLoanS.mLogger.info("Staff CIF is Y");
							alert_msg=NGFUserResourceMgr_PL.getAlert("VAL051");
							throw new ValidatorException(new FacesMessage(alert_msg));
						}
						else{
							BlacklistFlag_Part =  (outputResponse.contains("<BlackListFlag>")) ? outputResponse.substring(outputResponse.indexOf("<BlackListFlag>")+"</BlackListFlag>".length()-1,outputResponse.indexOf("</BlackListFlag>")):"";                                  
							formObject.setNGValue("Is_Customer_Details_Part","Y");    
							BlacklistFlag_reason =  (outputResponse.contains("<BlackListReason>")) ? outputResponse.substring(outputResponse.indexOf("<BlackListReason>")+"</BlackListReason>".length()-1,outputResponse.indexOf("</BlackListReason>")):"NA";
							BlacklistFlag_code =  (outputResponse.contains("<BlackListReasonCode>")) ? outputResponse.substring(outputResponse.indexOf("<BlackListReasonCode>")+"</BlackListReasonCode>".length()-1,outputResponse.indexOf("</BlackListReasonCode>")):"NA";


							NegativeListFlag =  (outputResponse.contains("<NegativeListFlag>")) ? outputResponse.substring(outputResponse.indexOf("<NegativeListFlag>")+"</NegativeListFlag>".length()-1,outputResponse.indexOf("</NegativeListFlag>")):"NA";
							PersonalLoanS.mLogger.info("CC value of ReturnCode part Match"+ReturnCode);
							PersonalLoanS.mLogger.info("Value of NegativeListFlag: "+NegativeListFlag+" BlacklistFlag_code:Blacklist Reason: "+BlacklistFlag_reason+": "+BlacklistFlag_code);
							if("Y".equalsIgnoreCase(BlacklistFlag_Part))
							{
								PersonalLoanS.mLogger.info("PL value of BlacklistFlag_Part"+"Customer is Blacklisted");    
							}
							else{
								BlacklistFlag_Part="N";
								PersonalLoanS.mLogger.info("PL value of BlacklistFlag_Part"+"Customer is not Blacklisted");    
							}
							//added by Akshay
							if("Y".equalsIgnoreCase(NegativeListFlag))
							{
								PersonalLoanS.mLogger.info("CC value of NegativeListFlag"+"Customer is Negative"); 
								NegativeListReason = (outputResponse.contains("<NegativeListReason>")) ? outputResponse.substring(outputResponse.indexOf("<NegativeListReason>")+"</NegativeListReason>".length()-1,outputResponse.indexOf("</NegativeListReason>")):"NA";
								NegativeListReasonCode = (outputResponse.contains("<NegativeListReasonCode>")) ? outputResponse.substring(outputResponse.indexOf("<NegativeListReasonCode>")+"</NegativeListReasonCode>".length()-1,outputResponse.indexOf("</NegativeListReasonCode>")):"NA";
							
							}
							else{
								NegativeListFlag="N";
								
								PersonalLoanS.mLogger.info("CC value of BlacklistFlag_Part"+"Customer is not Negative");    
							}//ended By Akshay
						}

						PersonalLoanS.mLogger.info("CC value of BlacklistFlag_Part flag"+BlacklistFlag_Part+"");   
						// changed by abhishek start for populating cutomer info grid 22may2017
						if(formObject.isVisible("FinacleCRMCustInfo_Frame1")==false){
							expandFinacleCRMCustInfo();						
							adjustFrameTops("FinacleCRM_CustInfo,External_Blacklist,Finacle_Core,MOL,World_Check,Reject_Enq,Sal_Enq,Credit_card_Enq1,LOS1,Case_History1");
						}
						List<String> list_custinfo = new ArrayList<String>();
						String CIFID =formObject.getNGValue("cmplx_PartMatch_cmplx_Partmatch_grid",formObject.getSelectedIndex("cmplx_PartMatch_cmplx_Partmatch_grid"),0);
						String PASSPORTNO =formObject.getNGValue("cmplx_PartMatch_cmplx_Partmatch_grid",formObject.getSelectedIndex("cmplx_PartMatch_cmplx_Partmatch_grid"),5);
						//below code added by nikhil 
						String Cust_type=formObject.getNGValue("cmplx_PartMatch_cmplx_Partmatch_grid",formObject.getSelectedIndex("cmplx_PartMatch_cmplx_Partmatch_grid"),17);
						String Applicant="";
						if("R".equalsIgnoreCase(Cust_type))
						{
							Applicant="Individual";
						}
						else
						{
							Applicant="Corporate";
						}
						list_custinfo.add(CIFID);  // cif id from partmatch
						//below code added by nikhil 11/12/17
						list_custinfo.add(Applicant);//applicant
						list_custinfo.add(PASSPORTNO); // passport
						list_custinfo.add(NegativeListFlag);
						list_custinfo.add("");
						list_custinfo.add(NegativeListReasonCode);
						list_custinfo.add(NegativeListReason);
						list_custinfo.add(BlacklistFlag_Part); // blacklist flag
						list_custinfo.add("");
						list_custinfo.add(BlacklistFlag_code);
						list_custinfo.add(BlacklistFlag_reason);
						list_custinfo.add("");
						list_custinfo.add("");
						
						//below code modified 8/12/17 to set by default consider for obligations
						list_custinfo.add("true");
						list_custinfo.add(formObject.getWFWorkitemName());

						PersonalLoanS.mLogger.info("CC DDVT Maker"+ "list_custinfo list values"+list_custinfo);
						
						//formObject.addItemFromList("cmplx_FinacleCRMCustInfo_FincustGrid", list_custinfo);
						int rowCount = formObject.getLVWRowCount("cmplx_FinacleCRMCustInfo_FincustGrid");
						boolean isCifAlreadyAdded = false;
						if(rowCount>0){
							for(int i=0;i<rowCount;i++){
								if(CIFID.equalsIgnoreCase(formObject.getNGValue("cmplx_FinacleCRMCustInfo_FincustGrid",i,0))){
									isCifAlreadyAdded = true;
									break;
								}
							}
						}
						if(!isCifAlreadyAdded){
						formObject.addItemFromList("cmplx_FinacleCRMCustInfo_FincustGrid", list_custinfo);
						alert_msg="CIF Added successfully to the Application";
							if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")) && "".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_CIFNO"))){
								formObject.setNGValue("cmplx_Customer_NTB","false");
								formObject.setNGValue("cmplx_Customer_CIFNO",CIFID);
								formObject.setLocked("FetchDetails",false);	
								

							}
						}
						else{
							alert_msg="CIF already present in application!!!";
						}
					}
					//added by akshay for proc 9916
					else if("CINF0282".equalsIgnoreCase(ReturnCode) || "CINF377".equalsIgnoreCase(ReturnCode) )
					{
						String errorCodeAlert=formObject.getNGDataFromDataCache("SELECT isnull((SELECT alert FROM ng_MASTER_INTEGRATION_ERROR_CODE with (nolock) WHERE Integration_call='CUSTOMER_DETAILS' AND error_code='"+ReturnCode+"'),(SELECT alert FROM ng_MASTER_INTEGRATION_ERROR_CODE with (nolock) WHERE  Integration_call='CUSTOMER_DETAILS' AND error_code='DEFAULT'))").get(0).get(0);
						PersonalLoanS.mLogger.info(errorCodeAlert);
						alert_msg=errorCodeAlert;
						throw new ValidatorException(new FacesMessage(alert_msg));
					}
					else{
						formObject.setNGValue("Is_Customer_Details_Part","N");
						PersonalLoanS.mLogger.info("CC DDVT Maker"+ "Customer Details failed while Add to Application");
						alert_msg=NGFUserResourceMgr_PL.getAlert("VAL065");
						throw new ValidatorException(new FacesMessage(alert_msg));
					}
				}
				catch(ValidatorException ve){
					PersonalLoanS.mLogger.info("PL DDVT Maker"+ "Inside Validator exception Catch---->STAFF CIF case!!"+alert_msg);
					throw new ValidatorException(new FacesMessage(alert_msg));
				}catch(Exception e){
					PersonalLoanS.mLogger.info("PL DDVT Maker"+ "Exception while setting data in grid:"+e.getMessage());
					PLCommon.printException(e);			
					alert_msg=NGFUserResourceMgr_PL.getAlert("val034");//"Error while setting data in finacle customer info grid";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}
				//change by saurabh on 21st May for PROC-9457
				alert_msg=NGFUserResourceMgr_PL.getAlert("val081");
				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			//changes done as said by Deepak Sir To call Customer_Details call ended


			/*		else if ("PartMatch_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
		if (!formObject.isVisible("FinacleCRMCustInfo_Frame1")){
				formObject.fetchFragment("FinacleCRM_CustInfo", "FinacleCRMCustInfo", "q_FinCRMCustInfo");
			}	
			formObject.setNGValue("FinacleCRMCustInfo_WINAME",formObject.getWFWorkitemName());
			formObject.setTop("External_Blacklist",formObject.getTop("FinacleCRM_CustInfo")+formObject.getHeight("FinacleCRM_CustInfo")+20);
			formObject.setTop("Finacle_Core",formObject.getTop("External_Blacklist")+30);
			formObject.setTop("MOL",formObject.getTop("Finacle_Core")+30);
			formObject.setTop("World_Check",formObject.getTop("MOL")+30);
			formObject.setTop("Sal_Enq",formObject.getTop("World_Check")+30);
			formObject.setTop("Reject_Enq",formObject.getTop("Sal_Enq")+30);

			String BlacklistFlag_Part = "";
			String BlacklistFlag_reason = "";
			String BlacklistFlag_code = "";
			String NegativeListFlag = "";

			outputResponse =genX.GenerateXML("CUSTOMER_DETAILS","Primary_CIF");
			PersonalLoanS.mLogger.info("PL value of ReturnDesc"+"Customer Details part Match");
			ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";

			BlacklistFlag_Part =  outputResponse.contains("<BlackListFlag>") ? outputResponse.substring(outputResponse.indexOf("<BlackListFlag>")+"</BlackListFlag>".length()-1,outputResponse.indexOf("</BlackListFlag>")):"NA";

			BlacklistFlag_reason =  outputResponse.contains("<BlackListReason>") ? outputResponse.substring(outputResponse.indexOf("<BlackListReason>")+"</BlackListReason>".length()-1,outputResponse.indexOf("</BlackListReason>")):"NA";

			BlacklistFlag_code =  outputResponse.contains("<BlackListReasonCode>") ? outputResponse.substring(outputResponse.indexOf("<BlackListReasonCode>")+"</BlackListReasonCode>".length()-1,outputResponse.indexOf("</BlackListReasonCode>")):"NA";

			NegativeListFlag =  outputResponse.contains("<NegativeListFlag>") ? outputResponse.substring(outputResponse.indexOf("<NegativeListFlag>")+"</NegativeListFlag>".length()-1,outputResponse.indexOf("</NegativeListFlag>")):"NA";
			PersonalLoanS.mLogger.info("PL value of ReturnCode part Match"+ReturnCode);
			ReturnDesc =  outputResponse.contains("<ReturnDesc>") ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    

			if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode)){

				BlacklistFlag_Part =  outputResponse.contains("<BlacklistFlag>") ? outputResponse.substring(outputResponse.indexOf("<BlacklistFlag>")+"</BlacklistFlag>".length()-1,outputResponse.indexOf("</BlacklistFlag>")):"";                                  
				formObject.setNGValue("Is_Customer_Details_Part","Y");    
				formObject.setNGValue("FinacleCRMCustInfo_WINAME",formObject.getWFWorkitemName());
				formObject.setNGValue("cmplx_Customer_NTB",false);//added by akshay on 28/3/18 for proc 7224
				if("Y".equalsIgnoreCase(BlacklistFlag_Part))
				{
					PersonalLoanS.mLogger.info("PL value of BlacklistFlag_Part"+"Customer is Blacklisted");    
				}
				else
					PersonalLoanS.mLogger.info("PL value of BlacklistFlag_Part"+"Customer is not Blacklisted");    
			}
			else{
				formObject.setNGValue("Is_Customer_Details_Part","N");
			}
			try{
				PersonalLoanS.mLogger.info("CC value of BlacklistFlag_Part flag inside try"+BlacklistFlag_Part+"");    
				List<String> list_custinfo = new ArrayList<String>();
				String CIFID =formObject.getNGValue("cmplx_PartMatch_cmplx_Partmatch_grid",formObject.getSelectedIndex("cmplx_PartMatch_cmplx_Partmatch_grid"),0);
				String PASSPORTNO =formObject.getNGValue("cmplx_PartMatch_cmplx_Partmatch_grid",formObject.getSelectedIndex("cmplx_PartMatch_cmplx_Partmatch_grid"),5);

				list_custinfo.add(CIFID);  // cif id from partmatch
				list_custinfo.add("");
				list_custinfo.add(PASSPORTNO); // passport
				list_custinfo.add(NegativeListFlag);
				list_custinfo.add("");
				list_custinfo.add("");
				list_custinfo.add("");
				list_custinfo.add(BlacklistFlag_Part); // blacklist flag
				list_custinfo.add("");
				list_custinfo.add(BlacklistFlag_code);
				list_custinfo.add(BlacklistFlag_reason);
				list_custinfo.add("");
				list_custinfo.add("");
				//below code modified 12/12/17
				list_custinfo.add("false");
				list_custinfo.add(formObject.getWFWorkitemName());

				PersonalLoanS.mLogger.info("CC DDVT Maker"+ "list_custinfo list values"+list_custinfo);
				formObject.addItemFromList("cmplx_FinacleCRMCustInfo_FincustGrid", list_custinfo);
				//++below code added by nikhil as per ddvt issues 9/12/17
				alert_msg=NGFUserResourceMgr_PL.getAlert("val047");//added successfully
				throw new ValidatorException(new FacesMessage(alert_msg));
			}catch(Exception e){
				PersonalLoanS.mLogger.info("PL DDVT Maker"+ "Exception while setting data in grid:"+e.getMessage());
				PLCommon.printException(e);			
				alert_msg=NGFUserResourceMgr_PL.getAlert("val034");//"Error while setting data in finacle customer info grid";
				throw new ValidatorException(new FacesMessage(alert_msg));
			}          
		}
		//changes done as said by Deepak Sir To call Customer_Details call ended
			 */

			else if("GuarantorDetails_Button2".equalsIgnoreCase(pEvent.getSource().getName())){
				if(formObject.getNGValue("GuarantorDetails_cif")==null || formObject.getNGValue("GuarantorDetails_cif")=="" || formObject.getNGValue("GuarantorDetails_cif").equalsIgnoreCase(NGFUserResourceMgr_PL.getGlobalVar("Guarantor_DummyCIF"))){
					outputResponse =genX.GenerateXML("CUSTOMER_ELIGIBILITY","Guarantor_CIF");
					ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);
					if("0000".equalsIgnoreCase(ReturnCode) )
					{
						parse_cif_eligibility(outputResponse,"Guarantor_CIF");					
						formObject.setNGValue("Is_Customer_Eligibility_Guarantor","Y");
						PersonalLoanS.mLogger.info("Check Guarator CIF"+formObject.getNGValue("cif"));

						if(formObject.getNGValue("GuarantorDetails_cif")=="" || formObject.getNGValue("GuarantorDetails_cif").equalsIgnoreCase(NGFUserResourceMgr_PL.getGlobalVar("Guarantor_DummyCIF"))){
							try
							{
								PersonalLoanS.mLogger.info("inside Customer Eligibility to through Exception to Exit:");
								formObject.RaiseEvent("WFSave");
								throw new ValidatorException(new FacesMessage("Guarantor is a New to Bank Customer."));
							}
							finally{
								hm.clear();
							}

						}else {
							outputResponse = genX.GenerateXML("CUSTOMER_DETAILS","Guarantor_CIF");
							ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
							if("0000".equalsIgnoreCase(ReturnCode)){
								formObject.setNGValue("Is_Customer_Details_Guarantor","Y");
								formObject.setLocked("GuarantorDetails_Button2",true);
								openDemographicTabs();
								ReadXml.valueSetIntegration(outputResponse , "Guarantor_CIF");    
								formObject.setNGValue("GuarantorDetails_passExpiry", Convert_dateFormat(formObject.getNGValue("GuarantorDetails_passExpiry"), "yyyy-mm-dd", "dd/mm/yyyy"),false);
								formObject.setNGValue("GuarantorDetails_dob", Convert_dateFormat(formObject.getNGValue("GuarantorDetails_dob"), "yyyy-mm-dd", "dd/mm/yyyy"),false);
								formObject.setNGValue("GuarantorDetails_eidExpiry", Convert_dateFormat(formObject.getNGValue("GuarantorDetails_eidExpiry"), "yyyy-mm-dd", "dd/mm/yyyy"),false);
								formObject.setNGValue("GuarantorDetails_visaExpiry", Convert_dateFormat(formObject.getNGValue("GuarantorDetails_visaExpiry"), "yyyy-mm-dd", "dd/mm/yyyy"),false);
								if(formObject.getNGValue("GuarantorDetails_dob")!=null && !"".equalsIgnoreCase(formObject.getNGValue("GuarantorDetails_dob")) && !" ".equalsIgnoreCase(formObject.getNGValue("GuarantorDetails_dob"))){
									common.getAge(formObject.getNGValue("GuarantorDetails_dob"), "GuarantorDetails_age");	
								}								
								formObject.RaiseEvent("WFSave");
								
							} else {
								formObject.setNGValue("Is_Customer_Details_Guarantor","N");
								throw new ValidatorException(new FacesMessage("Error in fetch Customer details operation."));

							}
						}
						
					} else {
						formObject.setNGValue("Is_Customer_Eligibility_Guarantor","N");
					}
				}
				else {
					outputResponse = genX.GenerateXML("CUSTOMER_DETAILS","Guarantor_CIF");
					ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					PersonalLoanS.mLogger.info("PL value of ReturnCode"+ReturnCode);
					ReturnDesc =  outputResponse.contains("<ReturnDesc>") ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
					PersonalLoanS.mLogger.info("PL value of ReturnDesc"+ReturnDesc);
					formObject.setLocked("GuarantorDetails_Button2",true);

					if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode)){
						formObject.setNGValue("Is_Customer_Eligibility_Guarantor","Y");
						PersonalLoanS.mLogger.info("PL value of EID_Genuine"+"value of Guarantor_CIF"+formObject.getNGValue("Is_Customer_Details"));
						ReadXml.valueSetIntegration(outputResponse,"");  
						formObject.setNGValue("GuarantorDetails_passExpiry", Convert_dateFormat(formObject.getNGValue("GuarantorDetails_passExpiry"), "yyyy-mm-dd", "dd/mm/yyyy"),false);
						formObject.setNGValue("GuarantorDetails_dob", Convert_dateFormat(formObject.getNGValue("GuarantorDetails_dob"), "yyyy-mm-dd", "dd/mm/yyyy"),false);
						formObject.setNGValue("GuarantorDetails_eidExpiry", Convert_dateFormat(formObject.getNGValue("GuarantorDetails_eidExpiry"), "yyyy-mm-dd", "dd/mm/yyyy"),false);
						formObject.setNGValue("GuarantorDetails_visaExpiry", Convert_dateFormat(formObject.getNGValue("GuarantorDetails_visaExpiry"), "yyyy-mm-dd", "dd/mm/yyyy"),false);
						if(formObject.getNGValue("GuarantorDetails_dob")!=null && !"".equalsIgnoreCase(formObject.getNGValue("GuarantorDetails_dob")) && !" ".equalsIgnoreCase(formObject.getNGValue("GuarantorDetails_dob"))){
							common.getAge(formObject.getNGValue("GuarantorDetails_dob"), "GuarantorDetails_age");	
						}
						formObject.RaiseEvent("WFSave");
						throw new ValidatorException(new FacesMessage("Existing Customer Details fetched Sucessfully"));
					}
					else{
						PersonalLoanS.mLogger.info("Customer Details"+"Customer Details is not generated");
						formObject.setNGValue("Is_Customer_Eligibility_Guarantor","N");
						throw new ValidatorException(new FacesMessage("Error in fetch Guarantor details operation."));
					}
				}
				if(NGFUserResourceMgr_PL.getGlobalVar("PL_Y").equalsIgnoreCase(formObject.getNGValue("Is_Customer_Eligibility_Guarantor")) && NGFUserResourceMgr_PL.getGlobalVar("PL_Y").equalsIgnoreCase(formObject.getNGValue("Is_Customer_Details_Guarantor")))
				{ 
					PersonalLoanS.mLogger.info("RLOS value of Guarator Details"+"inside if condition");
					formObject.setEnabled("GuarantorDetails_Button2", false); 

					throw new ValidatorException(new FacesMessage("Customer information fetched sucessfully"));
				}
				else{
					formObject.setEnabled("GuarantorDetails_Button2", true);
					throw new ValidatorException(new FacesMessage("Error in fetch Guarantor details operation."));
				}

			}

			else if ("EMploymentDetails_Button2".equalsIgnoreCase(pEvent.getSource().getName())) {
				String EmpName=formObject.getNGValue("EMploymentDetails_Text21");
				String EmpCode=formObject.getNGValue("EMploymentDetails_Text22");
				PersonalLoanS.mLogger.info( "EMpName$"+EmpName+"$");
				String query=null;
				//changes done to check duplicate selection compare emp code and main emp code main_employer_code column added - 06-09-2017 Disha
				if("".equalsIgnoreCase(EmpName.trim()))
					query="select distinct(EMPR_NAME),EMPLOYER_CODE,NATURE_OF_BUSINESS,EMPLOYER_CATEGORY_PL_NATIONAL,EMPLOYER_CATEGORY_PL_EXPAT,INCLUDED_IN_PL_ALOC,DATE_OF_INCLUSION_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,DATE_OF_INCLUSION_IN_CC_ALOC,NAME_OF_AUTHORIZED_PERSON_FOR_ISSUING_SC_STL_PAYSLIP,ACCOMMODATION_PROVIDED,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,OWNER_PARTNER_SIGNATORY_NAMES_AS_PER_TL,ALOC_REMARKS_CC,ALOC_REMARKS_PL,HIGH_DELINQUENCY_EMPLOYER,EMPLOYER_CATEGORY_PL,COMPANY_STATUS_CC,COMPANY_STATUS_PL,MAIN_EMPLOYER_CODE,PAYROLL_FLAG,TYPE_OF_COMPANY from NG_RLOS_ALOC_OFFLINE_DATA with(nolock) where EMPLOYER_CODE Like '"+EmpCode+"%'";

				else if("".equalsIgnoreCase(EmpCode.trim()))
					query="select distinct(EMPR_NAME),EMPLOYER_CODE,NATURE_OF_BUSINESS,EMPLOYER_CATEGORY_PL_NATIONAL,EMPLOYER_CATEGORY_PL_EXPAT,INCLUDED_IN_PL_ALOC,DATE_OF_INCLUSION_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,DATE_OF_INCLUSION_IN_CC_ALOC,NAME_OF_AUTHORIZED_PERSON_FOR_ISSUING_SC_STL_PAYSLIP,ACCOMMODATION_PROVIDED,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,OWNER_PARTNER_SIGNATORY_NAMES_AS_PER_TL,ALOC_REMARKS_CC,ALOC_REMARKS_PL,HIGH_DELINQUENCY_EMPLOYER,EMPLOYER_CATEGORY_PL,COMPANY_STATUS_CC,COMPANY_STATUS_PL,MAIN_EMPLOYER_CODE,PAYROLL_FLAG,TYPE_OF_COMPANY from NG_RLOS_ALOC_OFFLINE_DATA with(nolock) where EMPR_NAME Like '"+EmpName+"%'";

				else
					query="select distinct(EMPR_NAME),EMPLOYER_CODE,NATURE_OF_BUSINESS,EMPLOYER_CATEGORY_PL_NATIONAL,EMPLOYER_CATEGORY_PL_EXPAT,INCLUDED_IN_PL_ALOC,DATE_OF_INCLUSION_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,DATE_OF_INCLUSION_IN_CC_ALOC,NAME_OF_AUTHORIZED_PERSON_FOR_ISSUING_SC_STL_PAYSLIP,ACCOMMODATION_PROVIDED,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,OWNER_PARTNER_SIGNATORY_NAMES_AS_PER_TL,ALOC_REMARKS_CC,ALOC_REMARKS_PL,HIGH_DELINQUENCY_EMPLOYER,EMPLOYER_CATEGORY_PL,COMPANY_STATUS_CC,COMPANY_STATUS_PL,MAIN_EMPLOYER_CODE,PAYROLL_FLAG,TYPE_OF_COMPANY from NG_RLOS_ALOC_OFFLINE_DATA with(nolock) where EMPR_NAME Like '"+EmpName + "%' or EMPLOYER_CODE Like '"+EmpCode+"%'";

				PersonalLoanS.mLogger.info( "query is: "+query);//by shweta
				//populatePickListWindow(query,"EMploymentDetails_Button2", "Employer Name,Employer Code,INCLUDED IN PL ALOC,INCLUDED IN CC ALOC,INDUSTRY SECTOR,INDUSTRY MACRO,INDUSTRY MICRO,CONSTITUTION,NAME OF FREEZONE AUTHORITY,Employer Category PL, Employer Status CC,Employer Status PL,MAIN EMPLOYER CODE", true, 20);
				populatePickListWindow(query,"EMploymentDetails_Button2", "Employer Name,Employer Code,Nature Of Business,EMPLOYER CATEGORY PL NATIONAL,EMPLOYER CATEGORY PL EXPAT,INCLUDED IN PL ALOC,DOI IN PL ALOC,INCLUDED IN CC ALOC,DATE OF INCLUSION IN CC ALOC,NAME OF AUTHORIZED PERSON FOR ISSUING SC/STL/PAYSLIP,ACCOMMODATION PROVIDED,INDUSTRY SECTOR,INDUSTRY MACRO,INDUSTRY MICRO,CONSTITUTION,NAME OF FREEZONE AUTHORITY,OWNER/PARTNER/SIGNATORY NAMES AS PER TL,ALOC REMARKS CC,ALOC REMARKS PL,HIGH DELINQUENCY EMPLOYER,Employer Category PL, Employer Status CC,Employer Status PL,MAIN EMPLOYER CODE,PAYROLL_FLAG,TYPE OF COMPANY", true, 20,"Employer");
			}


			else if("LoanDetails_Button3".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.setNGValue("LoanDetails_winame",formObject.getWFWorkitemName());
				PersonalLoanS.mLogger.info( "Inside add button: "+formObject.getNGValue("LoanDetails_winame"));                                                                                				
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_LoanDetails_cmplx_LoanGrid");
			}                                
			else if("LoanDetails_Button4".equalsIgnoreCase(pEvent.getSource().getName())){

				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_LoanDetails_cmplx_LoanGrid");
			}
			else if("LoanDetails_Button5".equalsIgnoreCase(pEvent.getSource().getName())){
				//Update_Disbursal_Details();
				formObject.setNGValue("LoanDetails_disbdate", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_LoanDetails_cmplx_LoanGrid");			
				//formObject.setNGFrameState("LoanDetails_Frame4", 1);


			}

			else if("Customer_save".equalsIgnoreCase(pEvent.getSource().getName())){
				PersonalLoanS.mLogger.info( "Inside Customer_save button: ");
				//PCASI - 3444
				String sRMTLName = formObject.getNGValue("cmplx_Customer_RM_TL_NAME");
				PersonalLoanS.mLogger.info("sRMTLName :: "+sRMTLName);
				if(!(sRMTLName.equalsIgnoreCase(null) || sRMTLName.isEmpty())){
				formObject.setNGValue("RM_Name", sRMTLName);
				formObject.setNGValue("RmTlNameLabel", sRMTLName);
				PersonalLoanS.mLogger.info("RMTLName :: "+formObject.getNGValue("RM_Name")+ " RmTlNameLabel :: "+formObject.getNGValue("RmTlNameLabel"));
				}
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("CustomerDetails");
				Update_Office_Address(); 
				alert_msg=NGFUserResourceMgr_PL.getAlert("val017");//"Customer Details Saved";
				throw new ValidatorException(new FacesMessage(alert_msg));
			}

			else if("Product_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("ProductContainer");
				alert_msg=NGFUserResourceMgr_PL.getAlert("val018");
				throw new ValidatorException(new FacesMessage(alert_msg));
			}

			else if("GuarantorDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("GuarantorDet");
				throw new ValidatorException(new FacesMessage("Guarantor Save Successful"));


			}

			else if("IncomeDetails_Salaried_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("IncomeDEtails");
				alert_msg=NGFUserResourceMgr_PL.getAlert("val059");
				throw new ValidatorException(new FacesMessage(alert_msg));

			}

			else if("IncomeDetails_SelfEmployed_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("Incomedetails");
			}

			else if("CompanyDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("CompanyDetails");
			}

			else if("PartnerDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("PartnerDetails");
			}

			else if("SelfEmployed_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("Liability_container");
			}
			//added By Shweta on 21/10/2019 for Dectech call on CAD decision	
			else if("DecisionHistory_Button5".equalsIgnoreCase(pEvent.getSource().getName())){	
				formObject.setNGValue("DecCallFired","Decision");
				if(formObject.getNGFrameState("IncomeDEtails") != 0){
					formObject.fetchFragment("IncomeDEtails", "IncomeDetails", "q_IncomeDetails");
				}
				expandIncome();
				if(formObject.getNGFrameState("MOL") != 0){
					formObject.fetchFragment("MOL", "MOL1", "q_MOL");
					formObject.setNGFrameState("MOL", 0);	

				}
				if(formObject.getNGFrameState("World_Check") != 0){
					formObject.fetchFragment("World_Check", "WorldCheck1", "q_WorldCheck");
					formObject.setNGFrameState("World_Check", 0);	
					
				}
				formObject.setTop("World_Check", formObject.getTop("MOL")+formObject.getHeight("MOL")+15);
				formObject.setTop("Reject_Enq", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+15);
				formObject.setTop("Sal_Enq", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+35);
				formObject.setTop("Credit_card_Enq", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+55);
				formObject.setTop("Case_hist", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+75);
				formObject.setTop("LOS", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+95);
				outputResponse = genX.GenerateXML("DECTECH","");
				SystemErrorCode =  (outputResponse.contains("<SystemErrorCode>")) ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"";
				PersonalLoanS.mLogger.info(SystemErrorCode);

				if("".equalsIgnoreCase(SystemErrorCode)){
					new PL_Integration_Output().valueSetIntegration(outputResponse,"");   
					alert_msg=NGFUserResourceMgr_PL.getAlert("VAL021");
					formObject.RaiseEvent("WFSave");
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else{
					alert_msg=NGFUserResourceMgr_PL.getAlert("VAL012");
				}
				throw new ValidatorException(new FacesMessage(alert_msg));
			}

			else if("Liability_New_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("InternalExternalContainer");
				alert_msg=NGFUserResourceMgr_PL.getAlert("val019");//"Liability Details Saved";
				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			//change by saurabh on 7th Dec
			else if("EMploymentDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.


				Custom_fragmentSave("EmploymentDetails");
				formObject.setNGValue("Employer_Name", formObject.getNGValue("cmplx_EmploymentDetails_EmpName"));
				formObject.setNGValue("Company_Name", formObject.getNGValue("cmplx_EmploymentDetails_EmpName"));
				formObject.setNGValue("Cmp_Emp_Label",formObject.getNGValue("Employer_Name"));
				Update_Office_Address();
				alert_msg=NGFUserResourceMgr_PL.getAlert("val020");//"Employment Details Saved";
				throw new ValidatorException(new FacesMessage(alert_msg));
			}



			else if("ELigibiltyAndProductInfo_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.setNGValue("Final_Limit", formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("EligibilityAndProductInformation");

				alert_msg="Eligibility Details Saved";
				throw new ValidatorException(new FacesMessage(alert_msg));
			}


			else if("LoanDetailsRepay_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("LoanDetails");
			}

			else if("AddressDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("Address_Details_container");
				
				alert_msg=NGFUserResourceMgr_PL.getAlert("val063");//"Reference Details Saved";
				throw new ValidatorException(new FacesMessage(alert_msg));

			}
			//disha FSD
			else if("AltContactDetails_ContactDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("Alt_Contact_container");
				Custom_fragmentSave("CustomerDetails");
				formObject.RaiseEvent("WFSave");
				alert_msg=NGFUserResourceMgr_PL.getAlert("val064");//"Reference Details Saved";
				throw new ValidatorException(new FacesMessage(alert_msg));
			}

			else if("ReferenceDetails_save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("ReferenceDetails");
				//below code by saurabh on 11th nov
				alert_msg=NGFUserResourceMgr_PL.getAlert("val045");//"Reference Details Saved";

				throw new ValidatorException(new FacesMessage(alert_msg));
			}

			else if("CardDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){//Details Were not saving
				//below line commneted by Deepak 23March2018 to save card details section
				try{
					if("DDVT_Maker".equalsIgnoreCase(formObject.getWFActivityName()) && formObject.getLVWRowCount("cmplx_CardDetails_cmplx_CardCRNDetails")>0){
						String CRN=formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",0, 2);
						for(int i=1;i<formObject.getLVWRowCount("cmplx_CardDetails_cmplx_CardCRNDetails");i++)
						{
							CRN=CRN+","+formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",i, 2);
						}
						formObject.setNGValue("CRN", CRN);
						formObject.setNGValue("ECRN", formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",0,1));
						formObject.setNGValue("ECRNLabel",formObject.getNGValue("ECRN")); 

					}
					//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
					Custom_fragmentSave("Card_Details");

					Custom_fragmentSave("Supplementary_Card_Details");

					//formObject.saveFragment("Supplementary_Container");
					//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
					alert_msg=NGFUserResourceMgr_PL.getAlert("val062");//"Loan Details Saved";
				}
				catch(Exception e){
					PersonalLoanS.mLogger.info("Inside CardDetails_Save button click!!!"+"Exception occurred!!");
					printException(e);
				}
				throw new ValidatorException(new FacesMessage(alert_msg));
			}


			else if("LoanDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("LoanDetails");
				alert_msg=NGFUserResourceMgr_PL.getAlert("val022");//"Loan Details Saved";

				throw new ValidatorException(new FacesMessage(alert_msg));
			}

			else if("IMD_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("LoanDetails");
				alert_msg=NGFUserResourceMgr_PL.getAlert("val054");//"Loan Details Saved";

				throw new ValidatorException(new FacesMessage(alert_msg));
			}

			else if("LoanDetaisDisburs_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("LoanDetails");
				alert_msg=NGFUserResourceMgr_PL.getAlert("val035");//"Disbursal Details Saved";

				throw new ValidatorException(new FacesMessage(alert_msg));
			}

			//Below code added by Arun (23/11/17) for IMD save button in loan details
			else if("LoanDetails_IMD_save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("LoanDetails");
				alert_msg=NGFUserResourceMgr_PL.getAlert("val046");//"Initial Money Deposit Saved";

				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			else if ("FATCA_Add".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.setNGValue("FATCA_Wi_Name",formObject.getWFWorkitemName());
				PersonalLoanS.mLogger.info( "Inside add button: "+formObject.getNGValue("FATCA_Wi_Name"));
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_FATCA_cmplx_FATCAGrid");
				//added by prabhakar
				formObject.clear("cmplx_FATCA_ListedReason");
				formObject.clear("cmplx_FATCA_SelectedReason");
				LoadPickList("cmplx_FATCA_ListedReason", "Select description,code from ng_master_fatcaReasons with(nolock)");
				PersonalLoanS.mLogger.info("FATCA_Button3");
				formObject.setNGValue("cmplx_FATCA_selectedreasonhidden", "");
			}
			//Added By Prabhakar Drop-4 point-3
			else if ("FATCA_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
	

				PersonalLoanS.mLogger.info( "Inside Fatca Modify>>>cmplx_FATCA_selectedreasonhidden: "+formObject.getNGValue("cmplx_FATCA_selectedreasonhidden"));
				String hiddenText=formObject.getNGValue("cmplx_FATCA_selectedreasonhidden");
				hiddenText=hiddenText.replace("NA,", "");
				formObject.setNGValue("cmplx_FATCA_selectedreasonhidden",hiddenText);
				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_FATCA_cmplx_FATCAGrid");
				formObject.clear("cmplx_FATCA_ListedReason");
				formObject.clear("cmplx_FATCA_SelectedReason");
				LoadPickList("cmplx_FATCA_ListedReason", "Select description,code from ng_master_fatcaReasons with (nolock)");
				formObject.setNGValue("cmplx_FATCA_selectedreasonhidden", "");
			}
			//Added By Prabhakar Drop-4 point-3
			else if ("FATCA_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_FATCA_cmplx_FATCAGrid");
				formObject.clear("cmplx_FATCA_ListedReason");
				formObject.clear("cmplx_FATCA_SelectedReason");
		
				PersonalLoanS.mLogger.info( "Inside FATCA_Delete: delete of FATCA details");
				LoadPickList("cmplx_FATCA_ListedReason", "Select description,code from ng_master_fatcaReasons with (nolock)");
				formObject.setNGValue("cmplx_FATCA_selectedreasonhidden", "");

			}
			else if("cmplx_FATCA_cmplx_FATCAGrid".equalsIgnoreCase(pEvent.getSource().getName()))
			{
					try{
					Map<String,String> ReasonMap =getFatcaReasons();
				List<String> selectedReasonList= new ArrayList<String>();
					//String selectedreasons="";
					for (String reason : ReasonMap.values()) {
						selectedReasonList.add(reason);
					}
				PersonalLoanS.mLogger.info("Inside FatcaGrid Click");
				PersonalLoanS.mLogger.info("Index is"+formObject.getSelectedIndex("cmplx_FATCA_cmplx_FATCAGrid"));
				String hiddenText="";
				if(formObject.getSelectedIndex("cmplx_FATCA_cmplx_FATCAGrid")>-1){

					hiddenText=formObject.getNGValue("cmplx_FATCA_cmplx_FATCAGrid", formObject.getSelectedIndex("cmplx_FATCA_cmplx_FATCAGrid"), 10);
					PersonalLoanS.mLogger.info("HiddenField is: "+hiddenText);
							if(hiddenText.equalsIgnoreCase("NA")){
								hiddenText="";
							}
							if(hiddenText!=null && hiddenText!="null" &&  !"".equalsIgnoreCase(hiddenText) && !"NA".equals(hiddenText)){
								List<String> listedReasonList=Arrays.asList(hiddenText.split(","));	
								PersonalLoanS.mLogger.info("HiddenField is: check-1 ");

								if(formObject.getItemCount("cmplx_FATCA_SelectedReason")==0){
									formObject.clear("cmplx_FATCA_ListedReason");
									for (String string : listedReasonList)
									{
										selectedReasonList.remove(string);
										formObject.addComboItem("cmplx_FATCA_SelectedReason",getKeyByValue(ReasonMap, string), string);
									}
									for (String string : selectedReasonList)
									{
											PersonalLoanS.mLogger.info("cmplx_FATCA_SelectedReason"+listedReasonList);
											formObject.addComboItem("cmplx_FATCA_ListedReason", getKeyByValue(ReasonMap, string),string);
									}
								}
								//formObject.setNGValue("cmplx_FATCA_selectedreasonhidden", selectedreasons);
							}
				}
					else {
						int itemCount = formObject.getItemCount("cmplx_FATCA_SelectedReason");
						for(int i=0;i<itemCount;i++){
	
								formObject.removeItem("cmplx_FATCA_SelectedReason", (itemCount-1)-i);
							
						}
					}
				}catch(Exception e){
					printException(e);	
				}
			}

		
			else if("FATCA_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.setLocked("cmplx_FATCA_ListedReason", false)	;	
				formObject.setLocked("cmplx_FATCA_SelectedReason", false)	;
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("FATCA");
				alert_msg=NGFUserResourceMgr_PL.getAlert("val023");//"FATCA Details Saved";
				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			else if("KYC_Add".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.setNGValue("KYC_WI_NAME",formObject.getWFWorkitemName());
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_KYC_cmplx_KYCGrid");	
				formObject.setNGValue("KYC_CustomerType", "--Select--");
			}
			else if("KYC_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_KYC_cmplx_KYCGrid");
				formObject.setNGValue("KYC_CustomerType", "--Select--");
			}
			else if("KYC_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_KYC_cmplx_KYCGrid");
				formObject.setNGValue("KYC_CustomerType", "--Select--");
			}
			else if("KYC_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("KYC");
				alert_msg=NGFUserResourceMgr_PL.getAlert("val024");//"KYC Details Saved";
				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			else if("OECD_add".equalsIgnoreCase(pEvent.getSource().getName()))
			{
				formObject.setNGValue("OECD_winame",formObject.getWFWorkitemName());
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_OECD_cmplx_GR_OecdDetails");	
				formObject.setNGValue("OECD_CustomerType", "--Select--");
			}

			else if ("OECD_modify".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_OECD_cmplx_GR_OecdDetails");
				formObject.setNGValue("OECD_CustomerType", "--Select--");
			}

			else if ("OECD_delete".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_OECD_cmplx_GR_OecdDetails");
				formObject.setNGValue("OECD_CustomerType", "--Select--");

			}
		
			else if("OECD_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("OECD");
				alert_msg=NGFUserResourceMgr_PL.getAlert("val016");//"OECD Details Saved";
				throw new ValidatorException(new FacesMessage(alert_msg));
			}	
			else if ("OECD_Button1".equalsIgnoreCase(pEvent.getSource().getName())){					

				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_OECD_cmplx_GR_OecdDetails");
			}

			else if ("OECD_Button2".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_OECD_cmplx_GR_OecdDetails");
			}


			else if ("OECD_Button3".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_OECD_cmplx_GR_OecdDetails");
			}
			else if("RiskRating_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
				PersonalLoanS.mLogger.info("Inside risk rating");
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("Risk_Rating");
				PersonalLoanS.mLogger.info("risk rating saved");
				alert_msg=NGFUserResourceMgr_PL.getAlert("val057");//"OECD Details Saved";
				throw new ValidatorException(new FacesMessage(alert_msg));
			}

			else if("PartMatch_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("Part_Match");
				alert_msg=NGFUserResourceMgr_PL.getAlert("val067");//"OECD Details Saved";
				throw new ValidatorException(new FacesMessage(alert_msg));
			}

			else if("FinacleCRMIncident_save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("FinacleCRM_Incidents");
			}

			else if("FinacleCRMCustInfo_save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("FinacleCRM_CustInfo");
				alert_msg=NGFUserResourceMgr_PL.getAlert("val084");
				throw new ValidatorException(new FacesMessage(alert_msg));
			}                                                                                              

			else if("FinacleCore_save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("Finacle_Core");
				alert_msg=NGFUserResourceMgr_PL.getAlert("val085");
				throw new ValidatorException(new FacesMessage(alert_msg));
	
			}

			else if("MOL1_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("MOL");
				alert_msg=NGFUserResourceMgr_PL.getAlert("val056");//"MOl Details Saved";
				throw new ValidatorException(new FacesMessage(alert_msg));

			}

			else if("WorldCheck1_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("WorldCheck1_Frame1");
				alert_msg=NGFUserResourceMgr_PL.getAlert("val058");//"World Check Details Saved";
				throw new ValidatorException(new FacesMessage(alert_msg));

			}

			else if("SalaryEnq_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("Sal_Enq");
			}

			else if("CreditCardEnq_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("Credit_card_Enq");
			}

			else if("CaseHistoryReport_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("Case_History");
			}

			else if("LOS_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("LOS");
			}

			else if("RejectEnq_Save".equalsIgnoreCase(pEvent.getSource().getName())){//pcasi-1002
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("RejectEnq_Frame1");
				alert_msg=NGFUserResourceMgr_PL.getAlert("VAL120");
				throw new ValidatorException(new FacesMessage(alert_msg));
			}                                                                                              

			else if("DecisionHistory_Save".equalsIgnoreCase(pEvent.getSource().getName())){

				//saveIndecisionGrid();//Arun (23/09/17)
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("DecisionHistory");
				alert_msg=NGFUserResourceMgr_PL.getAlert("val002");//"Decision History Details Saved";
				throw new ValidatorException(new FacesMessage(alert_msg));//Arun (23/09/17)
			}
			else if ("DecisionHistory_Delete".equalsIgnoreCase(pEvent.getSource().getName()))
			{
				formObject.ExecuteExternalCommand("NGDeleteRow", "Decision_ListView1");
				if("DDVT_Maker".equalsIgnoreCase(formObject.getWFActivityName())){
					ddvtmaker_submitapp();
				}
				//changes to save refer to for Cad_Analyst2 start
				if("Cad_Analyst2".equalsIgnoreCase(formObject.getWFActivityName())||"DDVT_Checker".equalsIgnoreCase(formObject.getWFActivityName())){
					String ReferTo = setReferto();
					String query = "update NG_PL_EXTTABLE set ReferTo = '"+ReferTo+"' where PL_Wi_Name = '"+formObject.getWFWorkitemName()+"'";
					PersonalLoanS.mLogger.info("Deepak Query to update ReferTo at Cad 2: "+query);
					formObject.saveDataIntoDataSource(query);
				}
				//changes to save refer to for Cad_Analyst2 end
			}

			else  if("NotepadDetails_save".equalsIgnoreCase(pEvent.getSource().getName())){
				//formObject.saveFragment("Notepad_Values");
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("NotepadDetails_Frame2");
				alert_msg=NGFUserResourceMgr_PL.getAlert("val003");//"Notepad Details Saved";//Arun (23/09/17)
				throw new ValidatorException(new FacesMessage(alert_msg));//Arun (23/09/17)
			}


			else if("CAD_save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("Notepad_Details");
			}

			else if("CAD_Decision_save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("Dec");
			}


			else if("Loan_Disbursal_save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("Loan_Disbursal");
				alert_msg=NGFUserResourceMgr_PL.getAlert("val083");//"Loan Disbursal saved";
				throw new ValidatorException(new FacesMessage(alert_msg));
			}

			else if("CC_Creation_save".equalsIgnoreCase(pEvent.getSource().getName())){
				 //Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				 Custom_fragmentSave("CC_Creation");
				 throw new ValidatorException(new FacesMessage("Card details Disbursal saved !"));
			}

			else if("Limit_Inc_save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("Limit_Inc");
			}
			//changes done by shweta for jira# 2372 
			else if("CustDetailVerification_save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("CustDetailVerification_Frame1");
				alert_msg=NGFUserResourceMgr_PL.getAlert("val007");//"Customer detail verification saved";
				 //required to save Cust detail fragment and flag
				 formObject.RaiseEvent("WFSave");
				 //CreditCard.mLogger.info( "Customer Details Verification Details are saved123"); 
				 throw new ValidatorException(new FacesMessage(alert_msg));
			}

			else if("BussinessVerification_save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("Business_verification");
				alert_msg=NGFUserResourceMgr_PL.getAlert("val008");//"Business detail verification saved";
				throw new ValidatorException(new FacesMessage(alert_msg));
			}

			else if("HomeCountryVerification_save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("Home_country_verification");
				alert_msg=NGFUserResourceMgr_PL.getAlert("val009");//"Home country verification saved";
				throw new ValidatorException(new FacesMessage(alert_msg));
			}

			else if("ResidenceVerification_save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("Residence_verification");
				alert_msg=NGFUserResourceMgr_PL.getAlert("val010");//"Residence verification saved";
				throw new ValidatorException(new FacesMessage(alert_msg));
			}

			else if("GuarantorVerification_save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("Guarantor_verification");
				alert_msg=NGFUserResourceMgr_PL.getAlert("val011");//"Guarantor verification   saved";
				throw new ValidatorException(new FacesMessage(alert_msg));
			}

			else if("ReferenceDetailVerification_save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("Reference_detail_verification");
				alert_msg=NGFUserResourceMgr_PL.getAlert("val012");//"Reference detail verification saved";
				throw new ValidatorException(new FacesMessage(alert_msg));
			}

			else if("OfficeandMobileVerification_save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("Office_verification");
				alert_msg=NGFUserResourceMgr_PL.getAlert("val013");//"Office verification details saved";
				throw new ValidatorException(new FacesMessage(alert_msg));
			}


			else if("LoanandCard_save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("Loan_card_details");
				alert_msg=NGFUserResourceMgr_PL.getAlert("val014");//"Loan and Card details saved";
				throw new ValidatorException(new FacesMessage(alert_msg));
			}

			else if("Compliance_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("Compliance");
				//added by nikhil 24/12/17
				alert_msg=NGFUserResourceMgr_PL.getAlert("val052");//Compliance details savedd
				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			//added by yash on  24/12/17
			else if("IncomingDoc_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("IncomingDoc_Frame2");

				alert_msg=NGFUserResourceMgr_PL.getAlert("val053");//Incoming Document details savedd
				throw new ValidatorException(new FacesMessage(alert_msg));
			}


			else if("supervisorsection_save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("Supervisor_section");
			}

			else if("ReferHistory_save".equalsIgnoreCase(pEvent.getSource().getName())){
				PersonalLoanS.mLogger.info("Inside refer hostory save event.. ");
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("ReferHistory");
				alert_msg=NGFUserResourceMgr_PL.getAlert("val066");//Incoming Document details savedd
				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			else if("NotepadDetails_Button4".equalsIgnoreCase(pEvent.getSource().getName()))
			{
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_notegrid");
			}

			else if("NotepadDetails_Button5".equalsIgnoreCase(pEvent.getSource().getName()))
			{

				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_NotepadDetails_cmplx_notegrid");
			}

			else if("NotepadDetails_Button6".equalsIgnoreCase(pEvent.getSource().getName()))
			{

				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_NotepadDetails_cmplx_notegrid");
			}

			// below code changed by abhishek on 25th oct 2017 to change button id
			else if("WorldCheck1_Add".equalsIgnoreCase(pEvent.getSource().getName()))
			{
				formObject.setNGValue("WorldCheck1_winame",formObject.getWFWorkitemName());
				formObject.setNGValue("Is_World_Check_Add","Y");//Corrected world check id
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_WorldCheck_WorldCheck_Grid");
			}
			//-- Above code changed by abhishek on 25th oct 2017 to change button id
			//Code to add the dectech call on PL
			else if("ELigibiltyAndProductInfo_Button1".equalsIgnoreCase(pEvent.getSource().getName()))
			{
				PersonalLoanS.mLogger.info("Inside button");	
				Custom_fragmentSave("EligibilityAndProductInformation");
				takeoveramt(); //hritik 3257 pcasi

				//PersonalLoanS.mLogger.info("Inside ELigibiltyAndProductInfo_Button1"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
				formObject.setNGValue("DecCallFired","Eligibility");
				try{
					//double LoanAmount=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit")==null||"".equalsIgnoreCase(formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"))?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
					double LoanAmount=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_PLHidden")==null||"".equalsIgnoreCase(formObject.getNGValue("cmplx_EligibilityAndProductInfo_PLHidden"))?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_PLHidden"));
					double tenor=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor")==null||"".equalsIgnoreCase(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor"))?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor"));
					double RateofInt=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate")==null||"".equalsIgnoreCase(formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate"))?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate"));
					int mono_days=Integer.parseInt(formObject.getNGValue("cmplx_LoanDetails_moratorium")==null||formObject.getNGValue("cmplx_LoanDetails_moratorium").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_LoanDetails_moratorium"));
					
					PersonalLoanS.mLogger.info("Inside ELigibiltyAndProductInfo_Button2"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
					PersonalLoanS.mLogger.info("Loan Amount 2"+ formObject.getNGValue("cmplx_EligibilityAndProductInfo_PLHidden"));

				/*	String EMI=getEMI(LoanAmount,RateofInt,tenor,mono_days);
					PersonalLoanS.mLogger.info("Inside ELigibiltyAndProductInfo_Button3"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));

					formObject.setNGValue("cmplx_EligibilityAndProductInfo_EMI", EMI==null||"".equalsIgnoreCase(EMI)?"0":EMI);
					*/
					//below query added by akshay for proc 9238
					String qry = "select MAXINTRATE from ng_master_scheme where schemeid='"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,7)+"'";
					PersonalLoanS.mLogger.info("query to load max interest rate is"+qry +"");
					List<List<String>> record = formObject.getNGDataFromDataCache(qry);
					PersonalLoanS.mLogger.info("Result is: "+record);
					if(!record.isEmpty()){
						formObject.setNGValue("MAXINTRATE", record.get(0).get(0));
					}
					


					int framestate2=formObject.getNGFrameState("IncomeDEtails");
					if(framestate2 != 0){
						PersonalLoanS.mLogger.info("Inside income_Dectech ");
						formObject.fetchFragment("IncomeDEtails", "IncomeDetails", "q_IncomeDetails");
						//PersonalLoanS.mLogger.info("Inside ELigibiltyAndProductInfo_Button3"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
						//PersonalLoanS.mLogger.info("Inside beforer income_Dectech ");
						income_Dectech();
						//PersonalLoanS.mLogger.info("Inside after income_Dectech ");
						//PersonalLoanS.mLogger.info("Inside ELigibiltyAndProductInfo_Button3"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));


					}
					boolean framestate3=formObject.isVisible("EMploymentDetails_Frame1");
					if(!framestate3){
						PersonalLoanS.mLogger.info("Inside Employments_Dectech ");
						//PersonalLoanS.mLogger.info("Inside ELigibiltyAndProductInfo_Buttonemdeti"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
						//formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmpDetails");
						//PersonalLoanS.mLogger.info("Inside beforer EmploymentDetails ");
						//PersonalLoanS.mLogger.info("Inside ELigibiltyAndProductInfo_Button3"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
						loadEmployment();//EMployment tab is coming different for same ws.

						//PersonalLoanS.mLogger.info("Inside after EmploymentDetails ");

					}	

					int framestate4=formObject.getNGFrameState("MOL");
					if(framestate4 != 0){
						PersonalLoanS.mLogger.info("Inside MOL_Dectech ");
						formObject.fetchFragment("MOL", "MOL1", "q_MOL");
						formObject.setNGFrameState("MOL", 0);	


					}
					int framestate5=formObject.getNGFrameState("World_Check");
					if(framestate5 != 0){
						PersonalLoanS.mLogger.info("Inside World Check_Dectech ");
						formObject.fetchFragment("World_Check", "WorldCheck1", "q_WorldCheck");	
						formObject.setNGFrameState("World_Check", 0);	

					}


					//below code added by nikhil for fragmnet alignment of system check tabvs
					//adjustFrameTops("MOL,World_Check,Sal_Enq,Reject_Enq,Credit_card_Enq1,LOS1,Case_History1");
					int framestate6=formObject.getNGFrameState("InternalExternalLiability");
					if(framestate6 != 0){
						PersonalLoanS.mLogger.info("Inside InternalExternalLiability_Dectech ");
						//PersonalLoanS.mLogger.info("Inside ELigibiltyAndProductInfo_Buttonemdeti"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
						formObject.fetchFragment("InternalExternalLiability", "Liability_New", "q_Liabilities");
						//PersonalLoanS.mLogger.info("Inside beforer EmploymentDetails ");
						//PersonalLoanS.mLogger.info("Inside ELigibiltyAndProductInfo_Button3"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));


						//PersonalLoanS.mLogger.info("Inside after EmploymentDetails ");

					}
					int framestate7=formObject.getNGFrameState("DecisionHistory");
					if(framestate7 != 0){
						PersonalLoanS.mLogger.info("Inside DecisionHistory_Dectech ");
						//PersonalLoanS.mLogger.info("Inside ELigibiltyAndProductInfo_Buttonemdeti"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
						formObject.fetchFragment("DecisionHistory", "DecisionHistory", "q_Decision");
						new PLCommon().adjustFrameTops("Notepad_Values,DecisionHistory,ReferHistory");
						//PersonalLoanS.mLogger.info("Inside beforer EmploymentDetails ");
						//PersonalLoanS.mLogger.info("Inside ELigibiltyAndProductInfo_Button3"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));


						//PersonalLoanS.mLogger.info("Inside after EmploymentDetails ");

					}
					//PersonalLoanS.mLogger.info("Inside ELigibiltyAndProductInfo_ButtonAman"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));

					outputResponse = genX.GenerateXML("DECTECH","");

					//PersonalLoanS.mLogger.info("Inside ELigibiltyAndProductInfo_Button4"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));


					SystemErrorCode =  (outputResponse.contains("<SystemErrorCode>")) ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"Error";

					if("".equalsIgnoreCase(SystemErrorCode)){
						ReadXml.valueSetIntegration(outputResponse,"");   
						alert_msg="Decision engine integration successful";
						formObject.RaiseEvent("WFSave");
					}
					else{
						alert_msg="Critical error occurred Please contact administrator";

					}

					//deepak Code changes to calculate LPF amount and % 08-nov-2017 start

					PersonalLoanS.mLogger.info("RLOS_Common"+"Inside set event of LPF Data");
					double LPF_charge=0;
					double Insur_charge = 0;

					List<List<String>> result=formObject.getNGDataFromDataCache("select distinct c.CHARGERATE,I.Insur_chargeRate from NG_MASTER_Charges c with (nolock) left join NG_master_Scheme S with (nolock) on c.ChargeID=S.LPF_ChargeID  left join NG_MASTER_InsuranceCharges I with (nolock) on I.Insur_chargeId= S.Insur_chargeID where S.SCHEMEID='"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,7)+"'");
					PersonalLoanS.mLogger.info("RLOS_Common"+ "result of fetch RMname query: "+result); 
					if(result==null  || result.isEmpty()){
						LPF_charge=1;
						Insur_charge=1;
					}
					else{
						if(result.get(0).get(0)!=null && !"".equals(result.get(0).get(0))){
							LPF_charge = Double.parseDouble(result.get(0).get(0));
						}
						if(result.get(0).get(1)!=null && !"".equals(result.get(0).get(1))){
							Insur_charge = Double.parseDouble(result.get(0).get(1));
						}
					}
					PersonalLoanS.mLogger.info("RLOS_Common code "+ "result LPF_charge: "+LPF_charge);
					formObject.setNGValue("cmplx_EligibilityAndProductInfo_LPF", LPF_charge);
					//double LPF_amount;
					//double final_Loan_amount = Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
					double final_Loan_amount = Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_PLHidden"));
					
					/*LPF_amount = (final_Loan_amount*LPF_charge)/100;
					LPF_amount = ((double)(Math.round((LPF_amount*100)))/100);
					PersonalLoanS.mLogger.info("RLOS_Common code "+ "result LPF_amount: "+LPF_amount);*/
					//added b y akshay on 15/3/18 for proc 6396
					double Insuranceamount=(Insur_charge*final_Loan_amount)/100;
					Insuranceamount = ((double)(Math.round((Insuranceamount*100)))/100);
					formObject.setNGValue("cmplx_EligibilityAndProductInfo_InsuranceAmount", Insuranceamount);
					//formObject.setNGValue("cmplx_LoanDetails_loanemi", EMI==null||"".equalsIgnoreCase(EMI)?"0":EMI);//added by akshay for proc 8460
					String qry2 = "select VATDESC,VATRATE from  ng_MASTER_LoanVAT with(nolock) union SELECT distinct mincap,maxcap FROM NG_MASTER_Charges with(nolock) WHERE CHARGEDESC='Loan Processing Fee' union select SCHEMEDESC,MAXAMTFIN from NG_master_Scheme with(nolock) where SCHEMEID='"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,8)+"'";
					PersonalLoanS.mLogger.info("query to load VAT and Max Loan AMount is"+qry2 +"");
					List<List<String>> record1 = formObject.getNGDataFromDataCache(qry2);
					PersonalLoanS.mLogger.info("Result is: "+record1);
					if( record1.get(0)!=null && !record1.isEmpty()){
						
						for(int i=0;i<record1.size();i++){
							PersonalLoanS.mLogger.info("Value of record.get(i).get(0) "+record1.get(i).get(0));
							if("Loan Processing VAT".equalsIgnoreCase(record1.get(i).get(0))){
								int loanProcPercent=Integer.parseInt(record1.get(i).get(1));
								formObject.setNGValue("cmplx_EligibilityAndProductInfo_LoanProcessingVATPercent", loanProcPercent);
							}
							else if("Insurance VAT".equalsIgnoreCase(record1.get(i).get(0))){
								int insurancePercent=Integer.parseInt(record1.get(i).get(1));
								formObject.setNGValue("cmplx_EligibilityAndProductInfo_InsuranceVATPercent", insurancePercent);
							}
							/*else if(isNumeric(record1.get(i).get(0))){
								double LoanDetails_MaxLPF=Double.parseDouble(record1.get(i).get(1));
								double LoanDetails_MinLPF=Double.parseDouble(record1.get(i).get(0));
								if(LPF_amount> LoanDetails_MaxLPF)
									LPF_amount= LoanDetails_MaxLPF;
									else if (LPF_amount<LoanDetails_MinLPF)
										LPF_amount= LoanDetails_MinLPF;
							}*/
						}
					}
					
					//PCASI - 3647
					double LPF_amount_new = 0.01 * final_Loan_amount;
					
					if(LPF_amount_new < 500)
						LPF_amount_new = 500;
					else if(LPF_amount_new > 2500)
						LPF_amount_new = 2500;
					PersonalLoanS.mLogger.info("LPF_amount_new Loan grid :: "+LPF_amount_new);
					formObject.setNGValue("cmplx_LoanDetails_lpfamt", LPF_amount_new);
					//formObject.setNGValue("cmplx_EligibilityAndProductInfo_LPFAmount", LPF_amount);

					float lpfVatAmt=0;
					float insuranceVatAmt=0;
					
					 lpfVatAmt=(Float.parseFloat(formObject.getNGValue("cmplx_EligibilityAndProductInfo_LoanProcessingVATPercent"))*Float.parseFloat(formObject.getNGValue("cmplx_EligibilityAndProductInfo_LPFAmount")))/100f;
					 insuranceVatAmt=(Float.parseFloat(formObject.getNGValue("cmplx_EligibilityAndProductInfo_InsuranceVATPercent"))*Float.parseFloat(formObject.getNGValue("cmplx_EligibilityAndProductInfo_InsuranceAmount")))/100f;
					 lpfVatAmt = ((float)(Math.round((lpfVatAmt*100)))/100);
					 insuranceVatAmt = ((float)(Math.round((insuranceVatAmt*100)))/100);
					 
					 formObject.setNGValue("cmplx_EligibilityAndProductInfo_LoanProcessingVat", lpfVatAmt);
					 formObject.setNGValue("cmplx_EligibilityAndProductInfo_InsuranceVat", insuranceVatAmt);
					 formObject.setLocked("cmplx_EligibilityAndProductInfo_LoanProcessingVat", true);
					 formObject.setLocked("cmplx_EligibilityAndProductInfo_InsuranceVat", true);
					 formObject.RaiseEvent("WFSave");
	
				}
				catch(Exception e){
					PersonalLoanS.mLogger.info(e);
					PersonalLoanS.mLogger.info(" Exception in EMI Generation");
				}
				//deepak Code changes to calculate LPF amount and % 08-nov-2017 End
				calculateNetPayout(formObject); // bandana: method called for PCASI-3093
				
				throw new ValidatorException(new FacesMessage(alert_msg));
				
			}
			
			//Code to addd dechtech call for pl end 


			else if("WorldCheck1_Button2".equalsIgnoreCase(pEvent.getSource().getName()))
			{

				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_WorldCheck_WorldCheck_Grid");
			}

			else if("WorldCheck1_Button3".equalsIgnoreCase(pEvent.getSource().getName()))
			{
				formObject.setNGValue("Is_World_Check_Add","N");
				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_WorldCheck_WorldCheck_Grid");
			}

			else if("cmplx_Decision_VERIFICATIONREQUIRED".equalsIgnoreCase(pEvent.getSource().getName()))
			{
				PersonalLoanS.mLogger.info("CC val cmplx_Decision_VERIFICATIONREQUIRED "+ "Value of cmplx_Decision_VERIFICATIONREQUIRED is:"+formObject.getNGValue("cmplx_Decision_VERIFICATIONREQUIRED"));
				if(NGFUserResourceMgr_PL.getGlobalVar("PL_Y").equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_VERIFICATIONREQUIRED")) )
				{
					PersonalLoanS.mLogger.info("CC val change "+ "Inside Y of CPV required");
					formObject.setNGValue("cpv_required","Y");
				}
				else
				{
					PersonalLoanS.mLogger.info("CC val change "+ "Inside N of CPV required");
					formObject.setNGValue("cpv_required","N");
				}
			}

			// disha FSD
			else if ("NotepadDetails_Add".equalsIgnoreCase(pEvent.getSource().getName())){
				// Below code added by  Shweta 17/10/19
				PersonalLoanS.mLogger.info( "Inside NotepadDetails_Add button: "+formObject.getNGValue("formObject.getWFWorkitemName()"));

				formObject.setNGValue("NotepadDetails_winame",formObject.getWFWorkitemName());
				PersonalLoanS.mLogger.info( "Inside NotepadDetails_Add button: "+formObject.getNGValue("formObject.getWFWorkitemName()"));

				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_notegrid");
				Notepad_add();
				// Above code added by  Shweta 17/10/19
			}
			else if ("NotepadDetails_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
				//++Below code added by  nikhil 11/11/17
				
				int rowindex = formObject.getSelectedIndex("cmplx_NotepadDetails_cmplx_notegrid");
				String Notedate=formObject.getNGValue("cmplx_NotepadDetails_cmplx_notegrid",rowindex,4);

				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_NotepadDetails_cmplx_notegrid");
				Notepad_modify(rowindex,Notedate);
				//-- Above code added by  nikhil 11/11/17
			}
			else if ("NotepadDetails_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
				//Below code added by  nikhil 11/11/17
				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_NotepadDetails_cmplx_notegrid");
				Notepad_delete();
				//-- Above code added by  nikhil 11/11/17
			}
		
			//Below code added by  nikhil 11/11/17
		
			else if("cmplx_NotepadDetails_cmplx_notegrid".equalsIgnoreCase(pEvent.getSource().getName())){

				Notepad_grid();
				// added by siva on 23102019 for PCSP-412
				int selectedRows[]=formObject.getNGLVWSelectedRows("cmplx_NotepadDetails_cmplx_notegrid");
				PersonalLoanS.mLogger.info("selectedRows.length v is: "+selectedRows.length);
				if(selectedRows.length > 0)
				{					
					 Str_NotepadDetails_noteDate = formObject.getNGValue("cmplx_NotepadDetails_cmplx_notegrid", formObject.getSelectedIndex("cmplx_NotepadDetails_cmplx_notegrid"), 4);
					PersonalLoanS.mLogger.info("Str_NotepadDetails_noteDate is: "+Str_NotepadDetails_noteDate);
					//PersonalLoanS.mLogger.info("Str_NotepadDetails_user is: "+formObject.getNGValue("cmplx_NotepadDetails_cmplx_notegrid", formObject.getSelectedIndex("cmplx_NotepadDetails_cmplx_notegrid"), 3));
					  Str_NotepadDetails_user= formObject.getNGValue("cmplx_NotepadDetails_cmplx_notegrid", formObject.getSelectedIndex("cmplx_NotepadDetails_cmplx_notegrid"), 3);
					PersonalLoanS.mLogger.info("Str_NotepadDetails_user is: "+Str_NotepadDetails_user);
					//PersonalLoanS.mLogger.info("Str_NotepadDetails_insqueue is: "+formObject.getNGValue("cmplx_NotepadDetails_cmplx_notegrid", formObject.getSelectedIndex("cmplx_NotepadDetails_cmplx_notegrid"), 5));
					 Str_NotepadDetails_insqueue= formObject.getNGValue("cmplx_NotepadDetails_cmplx_notegrid", formObject.getSelectedIndex("cmplx_NotepadDetails_cmplx_notegrid"), 5);
					PersonalLoanS.mLogger.info("Str_NotepadDetails_insqueue is: "+Str_NotepadDetails_insqueue);
				}
				else if(selectedRows.length < 1)
				{
					PersonalLoanS.mLogger.info("Str_NotepadDetails_noteDate is: " +Str_NotepadDetails_noteDate);
					formObject.setNGValue("NotepadDetails_noteDate", Str_NotepadDetails_noteDate,false);
					PersonalLoanS.mLogger.info("Str_NotepadDetails_user is: "+Str_NotepadDetails_user);
					formObject.setNGValue("NotepadDetails_user", Str_NotepadDetails_user,false);
					PersonalLoanS.mLogger.info("NotepadDetails_insqueue is: "+Str_NotepadDetails_insqueue);
					formObject.setNGValue("NotepadDetails_insqueue", Str_NotepadDetails_insqueue,false);
					formObject.setNGValue("NotepadDetails_notedesc", "--Select--",false);
				}
				// ended by siva on 23102019 for PCSP-412
			}
			//-- Above code added by  nikhil 11/11/17
			else  if ("FinacleCRMCustInfo_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
				if(Check_ECRNGenOnChange()){
					alert_msg=NGFUserResourceMgr_PL.getAlert("VAL116");
					throw new ValidatorException(new FacesMessage(alert_msg));
				}
				if(formObject.getNGValue("FinacleCRMCustInfo_Text1").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_CIFNO")) && "false".equalsIgnoreCase(formObject.getNGValue("FinacleCRMCustInfo_CheckBox1"))){
					formObject.setNGValue("cmplx_Customer_NTB","true");
					formObject.setLocked("FetchDetails",true);	

				}
				if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")) && "".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_CIFNO"))&& "true".equalsIgnoreCase(formObject.getNGValue("FinacleCRMCustInfo_CheckBox1"))){
					formObject.setNGValue("cmplx_Customer_NTB","false");
					formObject.setNGValue("cmplx_Customer_CIFNO",formObject.getNGValue("FinacleCRMCustInfo_Text1"));
					formObject.setNGValue("CifLabel",formObject.getNGValue("cmplx_Customer_CIFNO"));
					formObject.setNGValue("CIF_ID",formObject.getNGValue("cmplx_Customer_CIFNO"));
					formObject.setLocked("FetchDetails",false);	

				}
				formObject.setNGValue("FinacleCRMCustInfo_WINAME",formObject.getWFWorkitemName());
				PersonalLoanS.mLogger.info(formObject.getNGValue("FinacleCRMCustInfo_WINAME"));
				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_FinacleCRMCustInfo_FincustGrid");
				CheckNTBOnFinCrmChange();
				Custom_fragmentSave("CustomerDetails");
				Custom_fragmentSave("FinacleCRMCustInfo_Frame1");//Deepak changes done for PCAS-3526
				formObject.RaiseEvent("WFSave");
				
			}

			else if("DecisionHistory_Button3".equalsIgnoreCase(pEvent.getSource().getName()) || "DecisionHistory_updcust_loan".equalsIgnoreCase(pEvent.getSource().getName())){
				String message = CustomerUpdate();
				//++Below code added by abhishek for disbursal as per FSD
				if(message.contains("success")){
					formObject.setLocked("DecisionHistory_updacct_loan", false);
					formObject.setNGValue("cmplx_LoanDisb_updateCustomerFlag", "Y");
					//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
					Custom_fragmentSave("Loan_Disbursal_Frame2");

				}
				//--Above code added by abhishek for disbursal as per FSD
				throw new ValidatorException(new FacesMessage(message));
			}
		//added by rishabh
			 else if ("EmploymentVerification_s2_Button1".equalsIgnoreCase(pEvent.getSource().getName())) {
				 //loadPicklist_Address();
				 PersonalLoanS.mLogger.info( "new Employment Verification Details are saved"); 
				 //Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				 Custom_fragmentSave("Employment Verification");
				 alert_msg=NGFUserResourceMgr_PersonalLoanS.getAlert("VAL026");
				 PersonalLoanS.mLogger.info( "new Employment Verification Details are saved123"); 
				 throw new ValidatorException(new FacesMessage(alert_msg));

			 }
			//tanshu startedDecisionHistory_updacct_loan
			else if("DecisionHistory_updcust".equalsIgnoreCase(pEvent.getSource().getName()) || "DecisionHistory_updacct_loan".equalsIgnoreCase(pEvent.getSource().getName())){
				try{
					PersonalLoanS.mLogger.info("inside ENTITY_MAINTENANCE_REQ is generated");
					String acc_veri= (formObject.getNGValue("Is_Acc_verified")!=null) ?formObject.getNGValue("Is_Acc_verified"):"";
					PersonalLoanS.mLogger.info("PL checker Account Update call"+ "entity_flag : "+acc_veri);
					//flag to check if there is already a current account. Then acct verification will not trigger.
					String acc_currflag = getCurrentAccountNumber("ENTITY_MAINTENANCE_REQ");
					formObject.setNGValue("cmplx_Decision_AccountNo", acc_currflag.split("!")[0]);
					if((acc_veri == null || "".equalsIgnoreCase(acc_veri)||"N".equalsIgnoreCase(acc_veri)) && !"Y".equals(acc_currflag.split("!")[1])){

						outputResponse = genX.GenerateXML("ENTITY_MAINTENANCE_REQ","AcctVerification");
						ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
						PersonalLoanS.mLogger.info("PL DDVT checker value of ReturnCode AcctVerification: "+ReturnCode);
						if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCode) || "CINF205".equalsIgnoreCase(ReturnCode)){
							formObject.setNGValue("Is_Acc_verified","Y");
							acc_veri="Y";
							PersonalLoanS.mLogger.info("account Verified successfully");
							formObject.setNGValue("LoanDetails_valstatus","Active");
							formObject.setNGValue("cmplx_LoanDetails_cmplx_LoanGrid",0,9,"Active");
							PersonalLoanS.mLogger.info("Loandetails acc status"+formObject.getNGValue("LoanDetails_valstatus"));
						}
						else{
							PersonalLoanS.mLogger.info("account Verified failed ReturnCode: "+ReturnCode );
							formObject.setNGValue("Is_Acc_verified","N");
							alert_msg= NGFUserResourceMgr_PL.getAlert("val025");//"Account Verification operation Failed, Please try after some time or contact administrator";
							//throw new ValidatorException(new FacesMessage(alert_msg));
						}
					}

					String acc_acti= formObject.getNGValue("Is_Acc_Active")!=null ? formObject.getNGValue("Is_Acc_Active"):"";
					//Deepak condition changed. As Account verification need not to done if user is already having current account.
					PersonalLoanS.mLogger.info("Existing account flag: "+acc_currflag.split("!")[1]);
					PersonalLoanS.mLogger.info("account active flag: "+acc_acti);
					PersonalLoanS.mLogger.info("account verify flag: "+acc_veri);
					if(("Y".equalsIgnoreCase(acc_veri) || !"Y".equals(acc_currflag.split("!")[1]))&&("".equalsIgnoreCase(acc_acti)||"N".equalsIgnoreCase(acc_acti))){
						outputResponse = genX.GenerateXML("ENTITY_MAINTENANCE_REQ","AcctActivation");
						ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
						PersonalLoanS.mLogger.info("PL DDVT checker value of ReturnCode for AcctActivation: "+ReturnCode);
						if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCode) || "CINF0007".equalsIgnoreCase(ReturnCode) || "E4860".equalsIgnoreCase(ReturnCode)){
							formObject.setNGValue("Is_Acc_Active","Y");
							acc_acti="Y";
							PersonalLoanS.mLogger.info("account Activation successfully");
						}
						else{
							PersonalLoanS.mLogger.info("ENTITY_MAINTENANCE_REQ is not generated ReturnCode: "+ReturnCode );
							formObject.setNGValue("Is_Acc_Active","N");
							alert_msg= NGFUserResourceMgr_PL.getAlert("val026");//"Account Activation operation Failed, Please try after some time or contact administrator";
							//throw new ValidatorException(new FacesMessage(alert_msg));
						}

					}

					if(("Y".equalsIgnoreCase(acc_veri) && "Y".equalsIgnoreCase(acc_acti))||("Y".equalsIgnoreCase(acc_currflag.split("!")[1]))) {
						outputResponse = genX.GenerateXML("ACCOUNT_MAINTENANCE_REQ","");
						
						ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
						PersonalLoanS.mLogger.info("PL DDVT checker value of ReturnCode"+ReturnCode);
						if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCode) ){
							formObject.setNGValue("Is_ACCOUNT_MAINTENANCE_REQ","Y");
							PersonalLoanS.mLogger.info("value of ACCOUNT_MAINTENANCE_REQ"+formObject.getNGValue("Is_ACCOUNT_MAINTENANCE_REQ"));
							ReadXml.valueSetIntegration(outputResponse,"");    
							PersonalLoanS.mLogger.info("ACCOUNT_MAINTENANCE_REQ is generated");
							PersonalLoanS.mLogger.info("PL DDVT checker value of Customer Details"+formObject.getNGValue("Is_ACCOUNT_MAINTENANCE_REQ"));
							formObject.RaiseEvent("WFSave");
							alert_msg= NGFUserResourceMgr_PL.getAlert("val027");//"Account Updated Successfully !";
							//++Below code added by abhishek for disbursal
							//below query added by akshay on 13/4/18 for proc 7689
							String query="Select count(AcctType) from ng_RLOS_CUSTEXPOSE_AcctDetails with(nolock) where AcctType like '%CURRENT ACCOUNT%' AND child_wi='"+formObject.getWFWorkitemName()+"'";
							List<List<String>> accountCount=formObject.getDataFromDataSource(query);
							if( Integer.parseInt(accountCount.get(0).get(0))==0){
								if("DecisionHistory_updcust".equalsIgnoreCase(pEvent.getSource().getName())){
									formObject.setVisible("DecisionHistory_chqbook", true);
								}
								else{
									formObject.setEnabled("DecisionHistory_chqbook_loan", true);
								}
							}
							//change by saurabh on 27th June.
							if( Integer.parseInt(accountCount.get(0).get(0))>0){
								formObject.setVisible("DecisionHistory_chqbook_loan", false);
								if(NGFUserResourceMgr_PL.getGlobalVar("PL_Islamic").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 0))){
									formObject.setVisible("Loan_Disbursal_Button1", true);
									formObject.setLocked("Loan_Disbursal_Button1", false);
								}

							} else {
								formObject.setVisible("DecisionHistory_chqbook_loan", true);
							}
							
							formObject.setNGValue("cmplx_LoanDisb_updateAccountFlag", "Y");
							//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
							Custom_fragmentSave("Loan_Disbursal_Frame2");
							//--Above code added by abhishek for disbursal
							//throw new ValidatorException(new FacesMessage(alert_msg));
						}
						else{
							PersonalLoanS.mLogger.info("ACCOUNT_MAINTENANCE_REQ is not generated ReturnCode: "+ReturnCode );
							formObject.setNGValue("Is_ACCOUNT_MAINTENANCE_REQ","N");
							alert_msg= NGFUserResourceMgr_PL.getAlert("val028");//"Account Update operation Failed, Please try after some time or contact administrator";

						}
					}										
				}
				catch(Exception ex){
					PersonalLoanS.mLogger.info("Exception in update account: ");
					printException(ex);
				}
				throw new ValidatorException(new FacesMessage(alert_msg));
			}

			else if("DecisionHistory_chqbook".equalsIgnoreCase(pEvent.getSource().getName()) || "DecisionHistory_chqbook_loan".equalsIgnoreCase(pEvent.getSource().getName())){
				PersonalLoanS.mLogger.info("RLOS value of CheckBook");
				if(!"Y".equalsIgnoreCase(formObject.getNGValue("Is_CHEQUE_BOOK_ELIGIBILITY"))){
					outputResponse = genX.GenerateXML("CHEQUE_BOOK_ELIGIBILITY","");
					ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);
					ReturnDesc =  outputResponse.contains("<ReturnDesc>") ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
					PersonalLoanS.mLogger.info("RLOS value of ReturnDesc"+ReturnDesc);
					String ReferenceId=outputResponse.contains("<ReferenceId>") ? outputResponse.substring(outputResponse.indexOf("<ReferenceId>")+"</ReferenceId>".length()-1,outputResponse.indexOf("</ReferenceId>")):"";    
					PersonalLoanS.mLogger.info("RLOS value of ReferenceId"+ReferenceId);
					//Modified condition Tanshu Aggarwal(7-06-2017)
					if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode)){
						//Modified condition Tanshu Aggarwal(7-06-2017)
						formObject.setNGValue("Is_CHEQUE_BOOK_ELIGIBILITY","Y");
						PersonalLoanS.mLogger.info("value of ENTITY_MAINTENANCE_REQ"+formObject.getNGValue("Is_CHEQUE_BOOK_ELIGIBILITY"));
						ReadXml.valueSetIntegration(outputResponse,"");    
						PersonalLoanS.mLogger.info("Is_CHEQUE_BOOK_ELIGIBILITY is generated");
						PersonalLoanS.mLogger.info("RLOS value of Is_CHEQUE_BOOK_ELIGIBILITY flag value"+formObject.getNGValue("Is_CHEQUE_BOOK_ELIGIBILITY"));
						formObject.setNGValue("cmplx_Decision_ChequeBookNumber",ReferenceId);
						PersonalLoanS.mLogger.info("ReferenceId"+formObject.getNGValue("cmplx_Decision_ChequeBookNumber"));
						formObject.RaiseEvent("WFSave");
					}
					else{
						PersonalLoanS.mLogger.info("Is_CHEQUE_BOOK_ELIGIBILITY is not generated");
						formObject.setNGValue("Is_CHEQUE_BOOK_ELIGIBILITY","N");
					}
				}
				if(!"Y".equalsIgnoreCase(formObject.getNGValue("Is_NEW_CARD_REQ"))){
					outputResponse = genX.GenerateXML("NEW_CARD_REQ","");
					ReturnCode =  outputResponse.contains("<ReturnCode>")? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);
					ReturnDesc =  outputResponse.contains("<ReturnDesc>") ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
					PersonalLoanS.mLogger.info("RLOS value of ReturnDesc"+ReturnDesc);
					String RequestId=outputResponse.contains("<RequestId>") ? outputResponse.substring(outputResponse.indexOf("<RequestId>")+"</RequestId>".length()-1,outputResponse.indexOf("</RequestId>")):"";    
					PersonalLoanS.mLogger.info("RLOS value of RequestId"+RequestId);
					//Modified condition Tanshu Aggarwal(7-06-2017)
					if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode)){
						//Modified condition Tanshu Aggarwal(7-06-2017)
						formObject.setNGValue("Is_NEW_CARD_REQ","Y");
						PersonalLoanS.mLogger.info("value of NEW_CARD_REQ"+formObject.getNGValue("Is_NEW_CARD_REQ"));
						ReadXml.valueSetIntegration(outputResponse,"");    
						PersonalLoanS.mLogger.info("Is_NEW_CARD_REQ is generated");
						PersonalLoanS.mLogger.info("RLOS value of Is_CHEQUE_BOOK_ELIGIBILITY flag value"+formObject.getNGValue("Is_NEW_CARD_REQ"));
						formObject.setNGValue("cmplx_Decision_DebitcardNumber",RequestId);
						PersonalLoanS.mLogger.info("NEW_CARD_REQ"+formObject.getNGValue("cmplx_Decision_DebitcardNumber"));
						//++Below code added by abhishek for disbursal
						formObject.setNGValue("cmplx_LoanDisb_ChequeBookFlag", "Y");
						//formObject.saveFragment("Loan_Disbursal_Frame2");
						//--Above code added by abhishek for disbursal
						formObject.RaiseEvent("WFSave");
					}
					else{
						PersonalLoanS.mLogger.info("Is_NEW_CARD_REQ is not generated");
						formObject.setNGValue("Is_NEW_CARD_REQ","N");
					}
				}
				formObject.RaiseEvent("WFSave");
				PersonalLoanS.mLogger.info("RLOS value of Is_CHEQUE_BOOK_ELIGIBILITY flag123 value"+formObject.getNGValue("Is_CHEQUE_BOOK_ELIGIBILITY"));
				PersonalLoanS.mLogger.info("RLOS value of Is_NEW_CARD_REQ flag123 value"+formObject.getNGValue("Is_NEW_CARD_REQ"));
				String ChequeBook=formObject.getNGValue("Is_CHEQUE_BOOK_ELIGIBILITY");
				String NewCardReq=formObject.getNGValue("Is_NEW_CARD_REQ");
				if(("Y".equalsIgnoreCase(ChequeBook)) && ("Y".equalsIgnoreCase(NewCardReq)) ){
					formObject.setNGValue("cmplx_LoanDisb_ChequeBookFlag", "Y");

					PersonalLoanS.mLogger.info("RLOS value of Is_CHEQUE_BOOK_ELIGIBILITY flag123 value"+formObject.getNGValue("Is_CHEQUE_BOOK_ELIGIBILITY")+","+formObject.getNGValue("Is_NEW_CARD_REQ"));
					if(NGFUserResourceMgr_PL.getGlobalVar("PL_Islamic").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 0)))
					{
						formObject.setVisible("Loan_Disbursal_Button1", true);
						formObject.setEnabled("Loan_Disbursal_Button1", true);
					}
					alert_msg="Debit Card and Cheque Book created successfully!!";
					throw new ValidatorException(new FacesMessage(alert_msg));

				}
				else if(("N".equalsIgnoreCase(ChequeBook)) && ("Y".equalsIgnoreCase(NewCardReq))){
					formObject.setNGValue("cmplx_LoanDisb_ChequeBookFlag", "N");

					PersonalLoanS.mLogger.info("RLOS value of Is_NEW_CARD_REQ flag123 value"+formObject.getNGValue("Is_CHEQUE_BOOK_ELIGIBILITY")+","+formObject.getNGValue("Is_NEW_CARD_REQ"));
					if(formObject.isVisible("Loan_Disbursal_Button1")){
						formObject.setVisible("Loan_Disbursal_Button1", false);
					}
					alert_msg="Debit Card created successfully.But Cheque Book Request Failed.!!";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}
				else if(("Y".equalsIgnoreCase(ChequeBook)) && ("N".equalsIgnoreCase(NewCardReq))){
					formObject.setNGValue("cmplx_LoanDisb_ChequeBookFlag", "N");

					PersonalLoanS.mLogger.info("RLOS value of Is_NEW_CARD_REQ flag123 value"+formObject.getNGValue("Is_CHEQUE_BOOK_ELIGIBILITY")+","+formObject.getNGValue("Is_NEW_CARD_REQ"));
					if(formObject.isVisible("Loan_Disbursal_Button1")){
						formObject.setVisible("Loan_Disbursal_Button1", false);
					}
					alert_msg="Cheque Book created sucessfully.But Debit card Request Failed.!!";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}
				else{
					PersonalLoanS.mLogger.info("RLOS value of Is_NEW_CARD_REQ flag123 value"+formObject.getNGValue("Is_CHEQUE_BOOK_ELIGIBILITY")+","+formObject.getNGValue("Is_NEW_CARD_REQ"));
					if(formObject.isVisible("Loan_Disbursal_Button1")){
						formObject.setVisible("Loan_Disbursal_Button1", false);
					}
					alert_msg="Cheque Book and Debit Card Request Failed, Please try after time or contact administrator!!";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}
			}
			//ended merged code


			//started code merged
			else  if("CC_Creation_Update_Customer".equalsIgnoreCase(pEvent.getSource().getName())){
				/*PersonalLoanS.mLogger.info(""+"inside Update_Customer"); 
			//change from inquire op name to CIF_ENQUIRY by saurabh on 10th Dec
			outputResponse = genX.GenerateXML("CUSTOMER_DETAILS","CIF_ENQUIRY");
			ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);

			if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCode)){
				PersonalLoanS.mLogger.info("RLOS value of Customer_Details"+"value of Customer_Details inside inquiry code");
				formObject.setNGValue("Is_CustInquiry_Disbursal","Y"); 
				PersonalLoanS.mLogger.info(""+"Inquiry Flag Value"+formObject.getNGValue("Is_CustInquiry_Disbursal")); 
				PersonalLoanS.mLogger.info(""+"inside Update_Customer");  
				String cif_status = outputResponse.contains("<CustomerStatus>") ? outputResponse.substring(outputResponse.indexOf("<CustomerStatus>")+"</CustomerStatus>".length()-1,outputResponse.indexOf("</CustomerStatus>")):"";
				if("ACTVE".equalsIgnoreCase(cif_status)){
					//change from Lock op name to CIF_LOCK by saurabh on 10th Dec
					outputResponse = genX.GenerateXML("CUSTOMER_DETAILS","CIF_LOCK");
					ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);
					ReturnDesc =  outputResponse.contains("<ReturnDesc>") ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
					PersonalLoanS.mLogger.info("RLOS value of ReturnDesc"+ReturnDesc);
					if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCode)|| "000".equalsIgnoreCase(ReturnCode)){
						PersonalLoanS.mLogger.info("RLOS value of Customer_Details"+"value of Customer_Details inside lock code");
						formObject.setNGValue("Is_CustLock_Disbursal","Y");
						PersonalLoanS.mLogger.info(""+"Inquiry Flag Is_CustLock_Disbursal value"+formObject.getNGValue("Is_CustLock_Disbursal")); 

						PersonalLoanS.mLogger.info("RLOS value of Customer_Details"+"Customer_Details is generated");
						//change from UnLock op name to CIF_UNLOCK by saurabh on 10th Dec
						outputResponse = genX.GenerateXML("CUSTOMER_DETAILS","CIF_UNLOCK");
						ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
						PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+"inside unlock");
						PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);
						formObject.setNGValue("Is_CustUnLock_Disbursal","Y");
						PersonalLoanS.mLogger.info(""+"Inquiry Flag Is_CustUnLock_Disbursal value"+formObject.getNGValue("Is_CustUnLock_Disbursal"));
						ReturnDesc =  outputResponse.contains("<ReturnDesc>") ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
						PersonalLoanS.mLogger.info("RLOS value of ReturnDesc"+ReturnDesc);
						if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCode)|| "000".equalsIgnoreCase(ReturnCode)){
							//changed from blank to CIF_update by saurabh on 10th dec
							if("SUPPLEMENT".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)))
							{
								outputResponse = genX.GenerateXML("CUSTOMER_UPDATE_REQ","SUPPLEMENT_CIF_UPDATE");

							}
							else{
								outputResponse = genX.GenerateXML("CUSTOMER_UPDATE_REQ","CIF_update");
							}
							ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
							PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);
							ReturnDesc =  outputResponse.contains("<ReturnDesc>") ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
							PersonalLoanS.mLogger.info("RLOS value of ReturnDesc"+ReturnDesc);
							//  Message =outputResponse.contains("<Message>")) ? outputResponse.substring(outputResponse.indexOf("<Message>")+"</Message>".length()-1,outputResponse.indexOf("</Message>")):"";    


							if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCode)|| "000".equalsIgnoreCase(ReturnCode)){
								formObject.setNGValue("Is_CUSTOMER_UPDATE_REQ_Disbursal","Y");
								PersonalLoanS.mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+"value of CUSTOMER_UPDATE_REQ"+formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
								int selectedRow = formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid");
								formObject.setNGValue("cmplx_CCCreation_cmplx_CCCreationGrid", selectedRow,10,"Y");
								formObject.setEnabled("CC_Creation_Button2",true);
								ReadXml.valueSetIntegration(outputResponse);    
								PersonalLoanS.mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+"CUSTOMER_UPDATE_REQ is generated");
								PersonalLoanS.mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ_Disbursal"));
							}
							else{
								PersonalLoanS.mLogger.info("Customer Details"+"CUSTOMER_UPDATE_REQ is not generated");
								formObject.setNGValue("Is_CUSTOMER_UPDATE_REQ_Disbursal","N");
							}
							PersonalLoanS.mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ_Disbursal"));
							formObject.RaiseEvent("WFSave");
							PersonalLoanS.mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+"after saving the flag");
							if(NGFUserResourceMgr_PL.getGlobalVar("PL_Y").equalsIgnoreCase(formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ_Disbursal")))
							{ 
								PersonalLoanS.mLogger.info("RLOS value of Is_CUSTOMER_UPDATE_REQ"+"inside if condition");
								formObject.setEnabled("Update_Customer", false); 
								throw new ValidatorException(new CustomExceptionHandler("Customer Updated Successful!!","Update_Customer#Customer Updated Successful!!","",hm));
							}
							else{
								formObject.setEnabled("CC_Creation_Update_Customer", true);
								throw new ValidatorException(new CustomExceptionHandler("Customer Updated Fail!!","Update_Customer#Customer Updated Fail!!","",hm));
							}
						}
						else{
							PersonalLoanS.mLogger.info("Customer Details"+"Customer_Details unlock is not generated");

						}
					}
					else{
						PersonalLoanS.mLogger.info("Customer Details"+"Customer_Details lock is not generated");
					}
				}
				else {
					PersonalLoanS.mLogger.info("DDVT Checker Customer Update operation: "+ "Error in Cif Enquiry operation CIF Staus is: "+ cif_status);
					throw new ValidatorException(new  CustomExceptionHandler("Customer Is InActive!!","FetchDetails#Customer Is InActive!!","",hm));
				}
				//added one more here
			}
			else{
				PersonalLoanS.mLogger.info("Customer Details"+"Customer_Details Inquiry is not generated");
			}*/

				//change by saurabh for DROP-4
				String buttonId = pEvent.getSource().getName();
				String message = CustomerUpdatePrimarySupplementary(buttonId);
				 formObject.setNGValue("cmplx_Customer_DOb",formatDateFromOnetoAnother(formObject.getNGValue("cmplx_Customer_DOb"),"yyyy-mm-dd","dd/mm/yyyy"),false);
				throw new ValidatorException(new FacesMessage(message));
			}

			else if("CC_Creation_Card_Services".equalsIgnoreCase(pEvent.getSource().getName())){
				PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+"inside button click");
				String Product_Name=formObject.getNGValue("CC_Creation_Product");
				PersonalLoanS.mLogger.info("RLOS value of Product_Name"+""+Product_Name);
				if("Limit Change Request".equalsIgnoreCase(Product_Name)){
					outputResponse = genX.GenerateXML("CARD_SERVICES_REQUEST","Limit Change Request");
					ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);
					ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
					PersonalLoanS.mLogger.info(ReturnDesc);

					if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCode)  || "000".equalsIgnoreCase(ReturnCode)){
						PersonalLoanS.mLogger.info("RLOS value of Customer_Details"+"value of Customer_Details inside inquiry code");
						formObject.setNGValue("Is_CardServicesPL","Y");
						PersonalLoanS.mLogger.info("RLOS value of Customer_Details for limit change"+formObject.getNGValue("Is_CardServicesPL"));
						 alert_msg=Product_Name+NGFUserResourceMgr_PL.getAlert("VAL086");
					} else {
						 alert_msg=Product_Name+NGFUserResourceMgr_PL.getAlert("VAL087");
					}
				}
				else if("Financial Services Request".equalsIgnoreCase(Product_Name)){
					outputResponse = genX.GenerateXML("CARD_SERVICES_REQUEST","Financial Services Request");
					ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);
					ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
					PersonalLoanS.mLogger.info(ReturnDesc);
					if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCode) || "000".equalsIgnoreCase(ReturnCode)){
						PersonalLoanS.mLogger.info("RLOS value of Customer_Details"+"value of Customer_Details inside inquiry code");
						formObject.setNGValue("Is_CardServicesPL","Y"); 
						PersonalLoanS.mLogger.info("RLOS value of Customer_Details for financial product"+formObject.getNGValue("Is_CardServicesPL"));
						alert_msg=Product_Name+NGFUserResourceMgr_PL.getAlert("VAL086");
					} else {
						 alert_msg=Product_Name+NGFUserResourceMgr_PL.getAlert("VAL087");
					}
				}
				else if("Product Upgrade Request".equalsIgnoreCase(Product_Name)){
					outputResponse = genX.GenerateXML("CARD_SERVICES_REQUEST","Product Upgrade Request");
					ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);
					ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
					PersonalLoanS.mLogger.info(ReturnDesc);
					if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCode)  || "000".equalsIgnoreCase(ReturnCode) ){
						PersonalLoanS.mLogger.info("RLOS value of Customer_Details"+"value of Customer_Details inside inquiry code");
						formObject.setNGValue("Is_CardServicesPL","Y"); 
						PersonalLoanS.mLogger.info("RLOS value of Customer_Details for product upgrade"+formObject.getNGValue("Is_CardServicesPL"));
						 alert_msg=Product_Name+NGFUserResourceMgr_PL.getAlert("VAL086");
					} else {
						 alert_msg=Product_Name+NGFUserResourceMgr_PL.getAlert("VAL087");
					}
				}
				 formObject.RaiseEvent("WFSave");
				 throw new ValidatorException(new FacesMessage(alert_msg));
			}
			//end of entity main and account mainitainence call

			//code for New Card and Notification and New Loan Request call Tanshu Aggarwal
			else if("CC_Creation_Button2".equalsIgnoreCase(pEvent.getSource().getName())){
				PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+"inside button click");
				PersonalLoanS.mLogger.info(""+"Card Creation Call triggered");
				 //Deepak changes done for PCAS-2343
				 String islamic_complete="select COUNT(Murhabha_WIName) as cardcount from ng_rlos_Murabha_Warranty where Murhabha_WIName ='"+formObject.getWFWorkitemName()+"' and (status ='' or status is null)";
				 PersonalLoanS.mLogger.info("islamic_complete query: "+islamic_complete);
				 List<List<String>> query_result=formObject.getDataFromDataSource(islamic_complete);
				 if(!query_result.isEmpty()&& Integer.parseInt(query_result.get(0).get(0))>0){
					 throw new ValidatorException(new FacesMessage("Islamic Workflow not completed for this card. Kindly complete the it first!!"));
				 } 
				if("SUPPLEMENT".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)))
				{
					outputResponse = genX.GenerateXML("NEW_CREDITCARD_REQ","SUPPLEMENT");

				}
				else{
					outputResponse = genX.GenerateXML("NEW_CREDITCARD_REQ","PRIMARY");
				}
				ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);
				if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCode)){
					PersonalLoanS.mLogger.info("PLCommoncode card creation successfull");
					formObject.setEnabled("CC_Creation_CAPS",true);
					formObject.setEnabled("CC_Creation_Button2",false);
					formObject.setNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getNGListIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),8,"Y");
					Custom_fragmentSave("CC_Creation");
					throw new ValidatorException(new FacesMessage("Card Creation Successfull!!"));
				}
				else{
					throw new ValidatorException(new FacesMessage("Card Creation Failed!!"));
				}
			}
			//added by saurabh on 19th Dec
			else if("CC_Creation_CAPS".equalsIgnoreCase(pEvent.getSource().getName())){
				PersonalLoanS.mLogger.info(""+"CARD_NOTIFICATION call triggered.");
				//below condition added by akshay on 22/10/18
				if("SUPPLEMENT".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)))
				 {
					 outputResponse = genX.GenerateXML("CARD_NOTIFICATION","SUPPLEMENT");
				 }
				 else{
					 outputResponse = genX.GenerateXML("CARD_NOTIFICATION","PRIMARY");
				 }
				ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);
				ReturnDesc =  (outputResponse.contains("<ReturnDesc>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
				PersonalLoanS.mLogger.info(ReturnDesc);
				if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCode) ||  "000".equalsIgnoreCase(ReturnCode)){
					PersonalLoanS.mLogger.info("RLOS value of Customer_Details"+"value of Customer_Details inside lock code");
					formObject.setNGValue("Is_CardNotifiactionPL","Y");
					formObject.setNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),9,"Y");
					PersonalLoanS.mLogger.info("RLOS value of ReturnCode Is_CardNotifiactionPL"+""+formObject.getNGValue("Is_CardNotifiactionPL"));
					formObject.setLocked("CC_Creation_CAPS",true);
					Custom_fragmentSave("CC_Creation");
					throw new ValidatorException(new FacesMessage("Transfer to Caps successfull!!"));

				}
				else{
					 formObject.setNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),9,"N");
					 throw new ValidatorException(new FacesMessage("Some error occurred!!"));
				}
				
			}
			//ended    for New Card and Notification call  
			//Enroll Reward call
			else  if("CC_Creation_Validate_Skyward".equalsIgnoreCase(pEvent.getSource().getName()) ){
				String buttonId = pEvent.getSource().getName();
				String message = VaildateSkyward(buttonId);
				//Added by Rajan for PCASP-2063
				if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5).equalsIgnoreCase("EKTMC-EXPAT") || formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5).equalsIgnoreCase("EKWEC-EXPAT")
						 || formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5).toUpperCase().contains("SKYWARDS"))
				{
					formObject.setLocked("CC_Creation_Validate_Skyward",true);
				}
				else
					{
					formObject.setLocked("CC_Creation_Validate_Skyward",false);
					}
					//	 formObject.setNGValue("cmplx_Customer_DOb",formatDateFromOnetoAnother(formObject.getNGValue("cmplx_Customer_DOb"),"yyyy-mm-dd","dd/mm/yyyy"),false);
				throw new ValidatorException(new FacesMessage(message));
			}
			//loan call
			else if("Loan_Disbursal_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
				List<List<String>> output = null;
				String scheme="";
				String acc_brnch="";
				//added by abhishek started
				PersonalLoanS.mLogger.info("PL Disbursal: " +"inside Loan Creation");
				formObject.fetchFragment("ProductContainer", "Product", "q_Product");
				int n = formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
				PersonalLoanS.mLogger.info("PL Disbursal: "+ "Row count: "+n);
				for(int i=0;i<n;i++)
				{
					PersonalLoanS.mLogger.info("value of requested type is"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid"+ 0+ 1) +"");
					formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 1);
					if(NGFUserResourceMgr_PL.getGlobalVar("PL_PL").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 1)) || NGFUserResourceMgr_PL.getGlobalVar("PL_PersonalLoan").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 1))){
						scheme = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 7);
						break;                                                          
					}
				}
				if(!"".equalsIgnoreCase(scheme) && !"null".equalsIgnoreCase(scheme)){
					try{
						String Query = "select distinct S.SCHEMEID,S.SCHEMEDESC,S.LSM_PRODDIFFRATE,P.Code , P.grace_days,P.description,S.PRIME_TYPE from ng_MASTER_Scheme S with(nolock) join ng_master_product_parameter P with(nolock) on S.PRODUCTFLAG = P.CODE and S.SCHEMEID = '"+scheme+"'";
						PersonalLoanS.mLogger.info("Query to fetch from ng_master_product_parameter"+Query +"");
						output = formObject.getNGDataFromDataCache(Query);
						PersonalLoanS.mLogger.info("value ofscheme is"+scheme +"");
						if(output!=null && !output.isEmpty()){
							formObject.setNGValue("scheme_code","CNP1");
							formObject.setNGValue("Scheme_desc","PL");
							formObject.setNGValue("cmplx_LoanDisb_prod_diff_rate",output.get(0).get(2) );
							formObject.setNGValue("cmplx_LoanDisb_product_code",output.get(0).get(3) );
							formObject.setNGValue("cmplx_LoanDisb_gracedays", output.get(0).get(4));
							formObject.setNGValue("cmplx_LoanDisb_product_desc",output.get(0).get(5) );
							formObject.setNGValue("cmplx_LoanDetails_basetype",output.get(0).get(6) );

							if(formObject.isVisible("DecisionHistory_Frame1")==false){
								PersonalLoanS.mLogger.info("PL" +"Inside IsVisible check for Decision History!!");
								formObject.fetchFragment("DecisionHistory", "DecisionHistory", "q_Decision");
								new PLCommon().adjustFrameTops("Notepad_Values,DecisionHistory,ReferHistory");
							}

							String acc_no = getCurrentAccountNumber("NEW_LOAN_REQ"); 

							PersonalLoanS.mLogger.info("cmplx_Decision_AccountNo: "+acc_no +"");
							if("".equalsIgnoreCase(acc_no) || acc_no == null ){

								/*							String qry = "select AcctId from ng_RLOS_CUSTEXPOSE_AcctDetails where AcctType like '%CURRENT ACCOUNT%' and ( Child_Wi = '"+formObject.getWFWorkitemName()+"' or Wi_Name ='"+formObject.getWFWorkitemName()+"') and FundingAccount='true'";
							PersonalLoanS.mLogger.info("query is"+qry +"");
							List<List<String>> record = formObject.getDataFromDataSource(qry);
							if(record!=null && record.get(0)!=null && !record.isEmpty()){
								acc_no = record.get(0).get(0);
								//started By Akshay
								if("RAKislamic CURRENT ACCOUNT".equalsIgnoreCase(record.get(0).get(1))){
									acc_brnch=acc_no.substring(0,3);

								}
								else{
									acc_brnch=acc_no.substring(1,4);
								}
								//ended By Akshay
								PersonalLoanS.mLogger.info("value of accno is"+acc_no +"");

							}
							else{*/
								alert_msg=NGFUserResourceMgr_PL.getAlert("val036");//"Loan can't be created as No current account is maintained for this Customer";
								throw new ValidatorException(new FacesMessage(alert_msg));
								//}	
							}
							//added By Akshay-21/6/2017	
							else{
								acc_brnch=acc_no.substring(0,3);
							}
							if(acc_no!=null && !"".equalsIgnoreCase(acc_no)){
								formObject.setNGValue("Account_Number", acc_no);//added By Akshay
								formObject.setNGValue("Branch",acc_brnch);
								formObject.setNGValue("cmplx_LoanDisb_SourcingBranch",acc_no.substring(0,4));  

							}



							outputResponse = genX.GenerateXML("NEW_LOAN_REQ","");
							ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
							PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);
							ReturnDesc =  outputResponse.contains("<ReturnDesc>") ? outputResponse.substring(outputResponse.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponse.indexOf("</ReturnDesc>")):"";    
							PersonalLoanS.mLogger.info("RLOS value of ReturnDesc"+ReturnDesc);
							String ContractID =  outputResponse.contains("<contractID>") ? outputResponse.substring(outputResponse.indexOf("<contractID>")+"</contractID>".length()-1,outputResponse.indexOf("</contractID>")):"";    
							PersonalLoanS.mLogger.info("RLOS value of ContractID"+ContractID);
							String StatusCode =  outputResponse.contains("<StatusCode>") ? outputResponse.substring(outputResponse.indexOf("<StatusCode>")+"</StatusCode>".length()-1,outputResponse.indexOf("</StatusCode>")):"";    
							String StatusReason ="";    

							if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCode)){
								PersonalLoanS.mLogger.info("RLOS value of Loan Request"+"value of Loan_Req inside");

								if (StatusCode.equalsIgnoreCase("H")){
									StatusReason="Waiting for Authorization";
								}
								else if (StatusCode.equalsIgnoreCase("A")){
									StatusReason="Active";
								}
								else if (StatusCode.equalsIgnoreCase("R")){
									StatusReason="Rejected by system";
								}
								else if (StatusCode.equalsIgnoreCase("C")){
									StatusReason="Cancelled by user";
								}
								else{
									StatusReason="Reversed in Flexcube";
								}

								formObject.setNGValue("Is_LoanReq","Y");
								formObject.setNGValue("cmplx_LoanDisb_AgreementID",ContractID);
								formObject.setNGValue("cmplx_LoanDisb_IbsReturnHeaderDesc",ReturnDesc);
								formObject.setNGValue("cmplx_LoanDisb_IbsRetrunCode",ReturnCode);
								formObject.setNGValue("cmplx_LoanDisb_IbsStatus",StatusCode);
								formObject.setNGValue("cmplx_LoanDisb_IbsReason",StatusReason);



								//++Below code added by abhishek for disbursal
								formObject.setNGValue("cmplx_LoanDisb_contractCreationFlag","Y");
								//--Above code added by abhishek for disbursal
								formObject.setNGValue("Contract_id",ContractID);
								PersonalLoanS.mLogger.info("agreement id masked: "+ContractID.replaceAll(ContractID.substring(6, 12),"xxxxxx"));
								formObject.setNGValue("cmplx_LoanDisb_AgreementId_masked",ContractID.replaceAll(ContractID.substring(6, 12),"xxxxxx"));	
								alert_msg = NGFUserResourceMgr_PL.getAlert("val037")+ContractID; //"Contract created sucessfully with contract id: "+ContractID;
								PersonalLoanS.mLogger.info("PL Disbursal "+ "message need to be displayed: "+alert_msg);
								//++Below code added by abhishek for disbursal
								//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
								Custom_fragmentSave("Loan_Disbursal_Frame2");
								//--Above code added by abhishek for disbursal
							} 
							else{
								alert_msg = NGFUserResourceMgr_PL.getAlert("val038");//"Error in Contract creation operation, kindly try after some time or contact administrator.";
								PersonalLoanS.mLogger.info("PL Disbursal: "+ "Error while generating new loan: "+ ReturnCode);
							}
						}
						else{
							alert_msg = NGFUserResourceMgr_PL.getAlert("val038");//"Error in Contract creation operation, kindly try after some time or contact administrator.";
							PersonalLoanS.mLogger.info("PL Disbursal "+"ng_master_product_parameter is not miantained for scheme: "+scheme );
						}
						formObject.RaiseEvent("WFSave");
					}
					catch(Exception e){
						if(alert_msg.equalsIgnoreCase("")){
							alert_msg = NGFUserResourceMgr_PL.getAlert("val038");//"Error in Contract creation operation, kindly try after some time or contact administrator.";
						}
						PersonalLoanS.mLogger.info("PL Disbursal: "+ "Exception occured while generating new loan: "+ e.toString()+" desc: "+e.getMessage()+" stack: "+e.getStackTrace());
						printException(e);
						throw new ValidatorException(new FacesMessage(alert_msg));	
					}
					throw new ValidatorException(new FacesMessage(alert_msg));
				}
				else{
					PersonalLoanS.mLogger.info("PL Disbursal "+"Scheme code is not maintained for this case");
				}
			}  

			else if ("CAD_Add".equalsIgnoreCase(pEvent.getSource().getName())){

				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_DECCAD_cmplx_gr_DECCAD");
			}

			else if ("CAD_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_DECCAD_cmplx_gr_DECCAD");
			}

			else if ("CAD_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_DECCAD_cmplx_gr_DECCAD");

			}

			else if ("FinacleCore_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.setNGValue("Dds_wi_name",formObject.getWFWorkitemName());
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_FinacleCore_DDSgrid");
			}
			else if ("FinacleCore_Button3".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_FinacleCore_DDSgrid");

			}
			else if ("FinacleCore_Button2".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_FinacleCore_DDSgrid");
				Custom_fragmentSave("FinacleCore_Frame6");
			}


			else if ("FinacleCore_Button4".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.setNGValue("Inward_wi_name",formObject.getWFWorkitemName());
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_FinacleCore_inwardtt");
			}
			else if ("CardDetails_Button2".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.setNGValue("CardDetailsGR_WiName",formObject.getWFWorkitemName());
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_CardDetails_cmpmx_gr_cardDetails");
			}
			else if ("CardDetails_Button3".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_CardDetails_cmpmx_gr_cardDetails");
			}

			else if ("CardDetails_Button4".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_CardDetails_cmpmx_gr_cardDetails");
			}
			else if ("FinacleCore_Button6".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_FinacleCore_inwardtt");

			}
			else if ("FinacleCore_Button5".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_FinacleCore_inwardtt");
			}
			else if ("FinacleCore_Button7".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.ExecuteExternalCommand("NGClearRow", "cmplx_FinacleCore_inwardtt");
			}

			else if("SmartCheck1_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("Smart_check");
				//below code added by nikhil 04/12/17
				alert_msg="Smart Check Saved";
				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			else if("SmartCheck1_Modify".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_SmartCheck1_SmartChkGrid_FCU");
			}

			//edited by rishabh
			else if("SmartCheck1_Add".equalsIgnoreCase(pEvent.getSource().getName())){
				//++Below code added by  nikhil 10/10/17
				formObject.setNGValue("SmartCheck1_WiName",formObject.getWFWorkitemName());
				//-- Above code added by  nikhil 10/10/17
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_SmartCheck1_SmartChkGrid_FCU");
			}
//commented by rishabh		
			/*else if("SmartCheck1_Add".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_SmartCheck1_SmartChkGrid_FCU");
			}*/
			else if("SmartCheck1_Delete".equalsIgnoreCase(pEvent.getSource().getName())){
				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_SmartCheck1_SmartChkGrid_FCU");
			}

			//Code to addd dechtec call on Decision for pl start 
			else if("DecisionHistory_Button5".equalsIgnoreCase(pEvent.getSource().getName())){	
				formObject.setNGValue("DecCallFired","Decision");
				PersonalLoanS.mLogger.info("$$Before genX.GenerateXML for dectech call..outputResponse is : "+outputResponse);
				int framestate2=formObject.getNGFrameState("IncomeDEtails");
				if(framestate2 != 0){
					formObject.fetchFragment("IncomeDEtails", "IncomeDetails", "q_IncomeDetails");
					PersonalLoanS.mLogger.info("Inside beforer income_Dectech ");
					income_Dectech();
					PersonalLoanS.mLogger.info("Inside after income_Dectech ");

				}
				int framestate3=formObject.getNGFrameState("EmploymentDetails");
				if(framestate3 != 0){
					formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmpDetails");
					PersonalLoanS.mLogger.info("Inside beforer EmploymentDetails ");


					PersonalLoanS.mLogger.info("Inside after EmploymentDetails ");

				}	

				int framestate4=formObject.getNGFrameState("MOL");
				if(framestate4 != 0){
					formObject.fetchFragment("MOL", "MOL1", "q_MOL");
				}
				int framestate5=formObject.getNGFrameState("World_Check");
				if(framestate5 != 0){
					formObject.fetchFragment("World_Check", "WorldCheck1", "q_WorldCheck");	
					formObject.setNGFrameState("World_Check", 0);	
					formObject.setTop("World_Check", formObject.getTop("MOL")+formObject.getHeight("MOL")+15);
					formObject.setTop("Reject_Enq", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+75);
					formObject.setTop("Sal_Enq", formObject.getTop("World_Check")+formObject.getHeight("World_Check")+35);
				}
				loadPicklistMol();
				outputResponse = genX.GenerateXML("DECTECH","");
				PersonalLoanS.mLogger.info("$$After genX.GenerateXML for dectech call..outputResponse ASDASDASDASD : "+outputResponse);

				SystemErrorCode = outputResponse.contains("<SystemErrorCode>") ? outputResponse.substring(outputResponse.indexOf("<SystemErrorCode>")+"</SystemErrorCode>".length()-1,outputResponse.indexOf("</SystemErrorCode>")):"";
				PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+SystemErrorCode);
				if("".equalsIgnoreCase(SystemErrorCode)){
					ReadXml.valueSetIntegration(outputResponse,"");   
					alert_msg=NGFUserResourceMgr_PL.getAlert("val006");//"Decision engine integration successful";
					PersonalLoanS.mLogger.info("after value set customer for dectech call");
					formObject.RaiseEvent("WFSave");
					throw new ValidatorException(new FacesMessage(alert_msg));				
				}
				else{
					alert_msg=NGFUserResourceMgr_PL.getAlert("val005");//"Critical error occurred Please contact administrator";
					throw new ValidatorException(new FacesMessage(alert_msg));
				}
			}	
			//Code to addd dechtec call on Decision for pl end  



			else if("HomeCountryVerification_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("Frame7");
			}
			else if("ResidenceVerification_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("Frame8");
			}
			else if("BussinessVerification_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("Frame6");
			}
			else if("OfficeandMobileVerification_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("Frame11");
			}
			else if("GuarantorVerification_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("Frame9");
			}
			else if("ReferenceDetailVerification_Button1".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("Frame10");
			}
			else if("LoanandCard_save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("Frame13");
			}



			else if ("CustDetailVerification1_Button2".equalsIgnoreCase(pEvent.getSource().getName())) {
				//below code added by nikhil
				formObject.clear("CustDetailVerification1_ListView1");
				String EmiratesID=formObject.getNGValue("cmplx_CustDetailverification1_EmiratesId");
				String CIFID=formObject.getNGValue("CustDetailVerification1_Text8");
				String EmpName=formObject.getNGValue("CustDetailVerification1_Compname");
				String EmpCode=formObject.getNGValue("CustDetailVerification1_Compcode");
				Custom_fragmentSave("Frame14");

				PersonalLoanS.mLogger.info("PL"+ "EmiratesID$"+EmiratesID+"$");
				String query="";
				//query="SELECT  DISTINCT concat(isnull(dec.CustomerNAme,'NA'),' , ',comp.Employer_Name),concat(dec.CIF_ID,' , ',dec.dec_wi_name),dec.remarks,dec.userName,concat(dec.Decision,' , ',(select isnull(subfeedback,'NA') from ng_rlos_decisionHistory where wi_name=dec.dec_wi_name),' , ',dec.dateLastChanged) FROM NG_RLOS_GR_DECISION AS dec INNER JOIN ng_RLOS_EmpDetails AS comp ON dec.dec_wi_name=comp.wi_name WHERE (";

				//query = "select    col1,col2,remarks,userName,col5 from ( select  DISTINCT concat(isnull(gr_dec.CustomerNAme,'NA'),' , ',emp.Employer_Name) as col1,concat(ext.CIF_ID,' , ',ext.PL_wi_name) as col2,(select TOP 1 remarks from ng_rlos_gr_decision where dec_wi_name=PL_Wi_Name order by datelastchanged desc)as remarks,gr_dec.userName,isnull((select top 1 concat(Decision,',',subfeedback,',',datelastchanged) from ng_rlos_gr_decision where dec_wi_name=PL_Wi_Name and workstepName='FPU' order by dateLastChanged desc),'') as col5,ext.EmirateID,ext.CIF_ID,ext.Employer_Name,emp.EmpCode from NG_PL_EXTTABLE ext inner join ng_RLOS_EmpDetails emp on ext.PL_wi_name = emp.wi_name inner join NG_RLOS_GR_DECISION gr_dec on ext.PL_wi_name = gr_dec.dec_wi_name and gr_dec.workstepName  in ('FCU','FPU') inner join ng_rlos_decisionHistory dec_history on ext.PL_wi_name = dec_history.wi_name union all select  DISTINCT concat(isnull(gr_dec.CustomerNAme,'NA'),' , ',ext.Employer_Name) as col1,concat(ext.CIF_ID,' , ',ext.CC_Wi_Name) as col2,(select TOP 1 remarks from ng_rlos_gr_decision where dec_wi_name=CC_Wi_Name order by datelastchanged desc)as remarks,gr_dec.userName,(select top 1 concat(Decision,subfeedback) from ng_rlos_gr_decision where dec_wi_name=CC_Wi_Name and workstepName='FPU' order by dateLastChanged desc) as col5,ext.EmirateID,ext.CIF_ID,emp.Employer_Name,emp.EmpCode from NG_CC_EXTTABLE ext inner join ng_RLOS_EmpDetails emp on ext.CC_Wi_Name = emp.wi_name inner join NG_RLOS_GR_DECISION gr_dec on ext.CC_Wi_Name = gr_dec.dec_wi_name and gr_dec.workstepName  in ('FCU','FPU') inner join ng_rlos_decisionHistory dec_history on ext.CC_Wi_Name = dec_history.wi_name) as fpcu_abc where ";
				//Deepak connected for 3080
				//query="select  col1,col2,remarks,userName,col5 from (select  DISTINCT concat(isnull(gr_dec.CustomerNAme,'NA'),' , ',emp.Employer_Name) as col1,concat(ext.CIF_ID,' , ',ext.PL_wi_name) as col2,(select TOP 1 remarks from ng_rlos_gr_decision where dec_wi_name=PL_Wi_Name order by datelastchanged desc)as remarks,gr_dec.userName,isnull((select top 1 concat(Decision,',',subfeedback,',',datelastchanged) from ng_rlos_gr_decision where dec_wi_name=PL_Wi_Name and workstepName='FPU' order by dateLastChanged desc),'') as col5,ext.EmirateID,ext.CIF_ID,emp.Employer_Name,emp.EmpCode from NG_PL_EXTTABLE ext inner join ng_RLOS_EmpDetails emp on ext.PL_wi_name = emp.wi_name inner join NG_RLOS_GR_DECISION gr_dec on ext.PL_wi_name = gr_dec.dec_wi_name and gr_dec.workstepName  in ('FCU','FPU') inner join ng_rlos_decisionHistory dec_history on ext.PL_wi_name = dec_history.wi_name union all select  DISTINCT concat(isnull(gr_dec.CustomerNAme,'NA'),' , ',emp.Employer_Name) as col1,concat(ext.CIF_ID,' , ',ext.CC_Wi_Name) as col2,(select TOP 1 remarks from ng_rlos_gr_decision where dec_wi_name=CC_Wi_Name order by datelastchanged desc)as remarks,gr_dec.userName,(select top 1 concat(Decision,subfeedback) from ng_rlos_gr_decision where dec_wi_name=CC_Wi_Name and workstepName='FPU' order by dateLastChanged desc) as col5,ext.EmirateID,ext.CIF_ID,emp.Employer_Name,emp.EmpCode from NG_CC_EXTTABLE ext inner join ng_RLOS_EmpDetails emp on ext.CC_Wi_Name = emp.wi_name inner join NG_RLOS_GR_DECISION gr_dec on ext.CC_Wi_Name = gr_dec.dec_wi_name and gr_dec.workstepName  in ('FCU','FPU') inner join ng_rlos_decisionHistory dec_history on ext.CC_Wi_Name = dec_history.wi_name  where ext.employmenttype='Salaried' union all select  DISTINCT concat(isnull(gr_dec.CustomerNAme,'NA'),' , ',ext.Employer_Name) as col1,concat(ext.CIF_ID,' , ',ext.CC_Wi_Name) as col2,(select TOP 1 remarks from ng_rlos_gr_decision where dec_wi_name=CC_Wi_Name order by datelastchanged desc)as remarks,gr_dec.userName,(select top 1 concat(Decision,subfeedback) from ng_rlos_gr_decision where dec_wi_name=CC_Wi_Name and workstepName='FPU' order by dateLastChanged desc) as col5,ext.EmirateID,ext.CIF_ID,ext.Employer_Name,emp.EmpCode from NG_CC_EXTTABLE ext inner join ng_RLOS_EmpDetails emp on ext.CC_Wi_Name = emp.wi_name inner join NG_RLOS_GR_DECISION gr_dec on ext.CC_Wi_Name = gr_dec.dec_wi_name and gr_dec.workstepName  in ('FCU','FPU') inner join ng_rlos_decisionHistory dec_history on ext.CC_Wi_Name = dec_history.wi_name  where ext.employmenttype!='Salaried') as fpcu_abc where ";
				//query="select col1,col2,remarks,userName,col5 from (select  DISTINCT concat(isnull(gr_dec.CustomerNAme,'NA'),' , ',emp.Employer_Name) as col1,concat(ext.CIF_ID,' , ',ext.PL_wi_name) as col2,(select TOP 1 remarks from ng_rlos_gr_decision where dec_wi_name=PL_Wi_Name order by datelastchanged desc)as remarks,(select TOP 1 username from ng_rlos_gr_decision where dec_wi_name=PL_wi_name and workstepName='FPU' order by dateLastChanged desc) as userName,isnull((select top 1 concat(Decision,',',subfeedback,',',datelastchanged) from ng_rlos_gr_decision where dec_wi_name=PL_Wi_Name and workstepName='FPU' order by dateLastChanged desc),'') as col5,ext.EmirateID,ext.CIF_ID,emp.Employer_Name,emp.EmpCode from NG_PL_EXTTABLE ext inner join ng_RLOS_EmpDetails emp on ext.PL_wi_name = emp.wi_name inner join NG_RLOS_GR_DECISION gr_dec on ext.PL_wi_name = gr_dec.dec_wi_name and gr_dec.workstepName  in ('FCU','FPU') inner join ng_rlos_decisionHistory dec_history on ext.PL_wi_name = dec_history.wi_name union all select  DISTINCT concat(isnull(gr_dec.CustomerNAme,'NA'),' , ',emp.Employer_Name) as col1,concat(ext.CIF_ID,' , ',ext.CC_Wi_Name) as col2,(select TOP 1 remarks from ng_rlos_gr_decision where dec_wi_name=CC_Wi_Name order by datelastchanged desc)as remarks,(select TOP 1 username from ng_rlos_gr_decision where dec_wi_name=CC_Wi_Name and workstepName='FPU' order by dateLastChanged desc) as userName,isnull((select top 1 concat(Decision,',',subfeedback,',',datelastchanged) from ng_rlos_gr_decision where dec_wi_name=CC_Wi_Name and workstepName='FPU' order by dateLastChanged desc),'') as col5,ext.EmirateID,ext.CIF_ID,emp.Employer_Name,emp.EmpCode from NG_CC_EXTTABLE ext inner join ng_RLOS_EmpDetails emp on ext.CC_Wi_Name = emp.wi_name inner join NG_RLOS_GR_DECISION gr_dec on ext.CC_Wi_Name = gr_dec.dec_wi_name and gr_dec.workstepName  in ('FCU','FPU') inner join ng_rlos_decisionHistory dec_history on ext.CC_Wi_Name = dec_history.wi_name  where ext.employmenttype='Salaried' union all select  DISTINCT concat(isnull(gr_dec.CustomerNAme,'NA'),' , ',ext.Employer_Name) as col1,concat(ext.CIF_ID,' , ',ext.CC_Wi_Name) as col2,(select TOP 1 remarks from ng_rlos_gr_decision where dec_wi_name=CC_Wi_Name order by datelastchanged desc)as remarks,(select TOP 1 username from ng_rlos_gr_decision where dec_wi_name=CC_Wi_Name and workstepName='FPU' order by dateLastChanged desc) as userName,isnull((select top 1 concat(Decision,',',subfeedback,',',datelastchanged) from ng_rlos_gr_decision where dec_wi_name=CC_Wi_Name and workstepName='FPU' order by dateLastChanged desc),'') as col5,ext.EmirateID,ext.CIF_ID,ext.Employer_Name,emp.EmpCode from NG_CC_EXTTABLE ext inner join ng_RLOS_EmpDetails emp on ext.CC_Wi_Name = emp.wi_name inner join NG_RLOS_GR_DECISION gr_dec on ext.CC_Wi_Name = gr_dec.dec_wi_name and gr_dec.workstepName  in ('FCU','FPU') inner join ng_rlos_decisionHistory dec_history on ext.CC_Wi_Name = dec_history.wi_name  where ext.employmenttype!='Salaried' ) as fpcu_abc where ";
				//query="select col1,col2,remarks,userName,col5 from ( select  DISTINCT concat(isnull(gr_dec.CustomerNAme,'NA'),' , ',emp.Employer_Name) as col1,concat(ext.CIF_ID,' , ',ext.PL_wi_name) as col2,(select TOP 1 remarks from ng_rlos_gr_decision where dec_wi_name=PL_Wi_Name and workstepName='FPU' order by datelastchanged desc)as remarks,(select TOP 1 username from ng_rlos_gr_decision where dec_wi_name=PL_wi_name and workstepName='FPU' order by dateLastChanged desc) as userName,isnull((select top 1 concat(Decision,',',subfeedback,',',datelastchanged) from ng_rlos_gr_decision where dec_wi_name=PL_Wi_Name and workstepName='FPU' order by dateLastChanged desc),'') as col5,ext.EmirateID,ext.CIF_ID,emp.Employer_Name,emp.EmpCode from NG_PL_EXTTABLE ext inner join ng_RLOS_EmpDetails emp on ext.PL_wi_name = emp.wi_name inner join NG_RLOS_GR_DECISION gr_dec on ext.PL_wi_name = gr_dec.dec_wi_name and gr_dec.workstepName  in ('FCU','FPU') inner join ng_rlos_decisionHistory dec_history on ext.PL_wi_name = dec_history.wi_name union all select  DISTINCT concat(isnull(gr_dec.CustomerNAme,'NA'),' , ',emp.Employer_Name) as col1,concat(ext.CIF_ID,' , ',ext.CC_Wi_Name) as col2,(select TOP 1 remarks from ng_rlos_gr_decision where dec_wi_name=CC_Wi_Name and workstepName='FPU' order by datelastchanged desc)as remarks,(select TOP 1 username from ng_rlos_gr_decision where dec_wi_name=CC_Wi_Name and workstepName='FPU' order by dateLastChanged desc) as userName,isnull((select top 1 concat(Decision,',',subfeedback,',',datelastchanged) from ng_rlos_gr_decision where dec_wi_name=CC_Wi_Name and workstepName='FPU' order by dateLastChanged desc),'') as col5,ext.EmirateID,ext.CIF_ID,emp.Employer_Name,emp.EmpCode from NG_CC_EXTTABLE ext inner join ng_RLOS_EmpDetails emp on ext.CC_Wi_Name = emp.wi_name inner join NG_RLOS_GR_DECISION gr_dec on ext.CC_Wi_Name = gr_dec.dec_wi_name and gr_dec.workstepName  in ('FCU','FPU') inner join ng_rlos_decisionHistory dec_history on ext.CC_Wi_Name = dec_history.wi_name  where ext.employmenttype='Salaried' union all select  DISTINCT concat(isnull(gr_dec.CustomerNAme,'NA'),' , ',ext.Employer_Name) as col1,concat(ext.CIF_ID,' , ',ext.CC_Wi_Name) as col2,(select TOP 1 remarks from ng_rlos_gr_decision where dec_wi_name=CC_Wi_Name and workstepName='FPU' order by datelastchanged desc)as remarks,(select TOP 1 username from ng_rlos_gr_decision where dec_wi_name=CC_Wi_Name and workstepName='FPU' order by dateLastChanged desc) as userName,isnull((select top 1 concat(Decision,',',subfeedback,',',datelastchanged) from ng_rlos_gr_decision where dec_wi_name=CC_Wi_Name and workstepName='FPU' order by dateLastChanged desc),'') as col5,ext.EmirateID,ext.CIF_ID,ext.Employer_Name,emp.EmpCode from NG_CC_EXTTABLE ext inner join ng_RLOS_EmpDetails emp on ext.CC_Wi_Name = emp.wi_name inner join NG_RLOS_GR_DECISION gr_dec on ext.CC_Wi_Name = gr_dec.dec_wi_name and gr_dec.workstepName  in ('FCU','FPU') inner join ng_rlos_decisionHistory dec_history on ext.CC_Wi_Name = dec_history.wi_name  where ext.employmenttype!='Salaried' ) as fpcu_abc where ";
				
				//for latest fpu remarks
				query="select col1,col2,isnull(remarks,''),isnull(userName,''),col5 from ( select  DISTINCT concat(isnull(ext.CustomerNAme,'NA'),' , ',emp.Employer_Name) as col1,concat(ext.CIF_ID,' , ',ext.PL_wi_name) as col2,(select TOP 1 remarks from ng_rlos_gr_decision where dec_wi_name=PL_Wi_Name and workstepName='FPU' order by datelastchanged desc)as remarks,(select TOP 1 username from ng_rlos_gr_decision where dec_wi_name=PL_wi_name and workstepName='FPU' order by dateLastChanged desc) as userName,isnull((select top 1 concat(Decision,',',subfeedback,',',datelastchanged) from ng_rlos_gr_decision where dec_wi_name=PL_Wi_Name and workstepName='FPU' order by dateLastChanged desc),'') as col5,ext.EmirateID,ext.CIF_ID,emp.Employer_Name,emp.EmpCode from NG_PL_EXTTABLE ext inner join ng_RLOS_EmpDetails emp on ext.PL_wi_name = emp.wi_name inner join NG_RLOS_GR_DECISION gr_dec on ext.PL_wi_name = gr_dec.dec_wi_name and (gr_dec.workstepName in ('FCU','FPU') or ext.PL_wi_name in (select distinct history_wi_name from ng_rlos_gr_ReferHistory with(nolock) where ReferWorkstep ='fpu' and WorkStatus is null)) inner join ng_rlos_decisionHistory dec_history on ext.PL_wi_name = dec_history.wi_name union all select  DISTINCT concat(isnull(gr_dec.CustomerNAme,'NA'),' , ',emp.Employer_Name) as col1,concat(ext.CIF_ID,' , ',ext.CC_Wi_Name) as col2,(select TOP 1 remarks from ng_rlos_gr_decision where dec_wi_name=CC_Wi_Name and workstepName='FPU' order by datelastchanged desc)as remarks,(select TOP 1 username from ng_rlos_gr_decision where dec_wi_name=CC_Wi_Name and workstepName='FPU' order by dateLastChanged desc) as userName,isnull((select top 1 concat(Decision,',',subfeedback,',',datelastchanged) from ng_rlos_gr_decision where dec_wi_name=CC_Wi_Name and workstepName='FPU' order by dateLastChanged desc),'') as col5,ext.EmirateID,ext.CIF_ID,emp.Employer_Name,emp.EmpCode from NG_CC_EXTTABLE ext inner join ng_RLOS_EmpDetails emp on ext.CC_Wi_Name = emp.wi_name inner join NG_RLOS_GR_DECISION gr_dec on ext.CC_Wi_Name = gr_dec.dec_wi_name and (gr_dec.workstepName in ('FCU','FPU') or ext.CC_Wi_Name in (select distinct history_wi_name from ng_rlos_gr_ReferHistory with(nolock) where ReferWorkstep ='fpu' and WorkStatus is null)) inner join ng_rlos_decisionHistory dec_history on ext.CC_Wi_Name = dec_history.wi_name  where ext.employmenttype='Salaried' union all select  DISTINCT concat(isnull(gr_dec.CustomerNAme,'NA'),' , ',ext.Employer_Name) as col1,concat(ext.CIF_ID,' , ',ext.CC_Wi_Name) as col2,(select TOP 1 remarks from ng_rlos_gr_decision where dec_wi_name=CC_Wi_Name and workstepName='FPU' order by datelastchanged desc)as remarks,(select TOP 1 username from ng_rlos_gr_decision where dec_wi_name=CC_Wi_Name and workstepName='FPU' order by dateLastChanged desc) as userName,isnull((select top 1 concat(Decision,',',subfeedback,',',datelastchanged) from ng_rlos_gr_decision where dec_wi_name=CC_Wi_Name and workstepName='FPU' order by dateLastChanged desc),'') as col5,ext.EmirateID,ext.CIF_ID,ext.Employer_Name,emp.EmpCode from NG_CC_EXTTABLE ext inner join ng_RLOS_EmpDetails emp on ext.CC_Wi_Name = emp.wi_name inner join NG_RLOS_GR_DECISION gr_dec on ext.CC_Wi_Name = gr_dec.dec_wi_name and (gr_dec.workstepName in ('FCU','FPU') or ext.CC_Wi_Name in (select distinct history_wi_name from ng_rlos_gr_ReferHistory with(nolock) where ReferWorkstep ='fpu' and WorkStatus is null)) inner join ng_rlos_decisionHistory dec_history on ext.CC_Wi_Name = dec_history.wi_name  where ext.employmenttype!='Salaried' ) as fpcu_abc where ";

				//query = "select    col1,col2,remarks,userName,col5 from ( select  DISTINCT concat(isnull(gr_dec.CustomerNAme,'NA'),' , ',emp.Employer_Name) as col1,concat(ext.CIF_ID,' , ',ext.PL_wi_name) as col2,(select TOP 1 remarks from ng_rlos_gr_decision where dec_wi_name=PL_Wi_Name order by datelastchanged desc)as remarks,gr_dec.userName,isnull((select top 1 concat(Decision,',',subfeedback,',',datelastchanged) from ng_rlos_gr_decision where dec_wi_name=PL_Wi_Name and workstepName='FPU' order by dateLastChanged desc),'') as col5,ext.EmirateID,ext.CIF_ID,ext.Employer_Name,emp.EmpCode from NG_PL_EXTTABLE ext inner join ng_RLOS_EmpDetails emp on ext.PL_wi_name = emp.wi_name inner join NG_RLOS_GR_DECISION gr_dec on ext.PL_wi_name = gr_dec.dec_wi_name and gr_dec.workstepName  in ('FCU','FPU') inner join ng_rlos_decisionHistory dec_history on ext.PL_wi_name = dec_history.wi_name union all select  DISTINCT concat(isnull(gr_dec.CustomerNAme,'NA'),' , ',ext.Employer_Name) as col1,concat(ext.CIF_ID,' , ',ext.CC_Wi_Name) as col2,(select TOP 1 remarks from ng_rlos_gr_decision where dec_wi_name=CC_Wi_Name order by datelastchanged desc)as remarks,gr_dec.userName,(select top 1 concat(Decision,subfeedback) from ng_rlos_gr_decision where dec_wi_name=CC_Wi_Name and workstepName='FPU' order by dateLastChanged desc) as col5,ext.EmirateID,ext.CIF_ID,emp.Employer_Name,emp.EmpCode from NG_CC_EXTTABLE ext inner join ng_RLOS_EmpDetails emp on ext.CC_Wi_Name = emp.wi_name inner join NG_RLOS_GR_DECISION gr_dec on ext.CC_Wi_Name = gr_dec.dec_wi_name and gr_dec.workstepName  in ('FCU','FPU') inner join ng_rlos_decisionHistory dec_history on ext.CC_Wi_Name = dec_history.wi_name) as fpcu_abc where ";

				String where_clause="";
				if(!"".equalsIgnoreCase(EmiratesID.trim())){
					where_clause="EmirateID='"+EmiratesID+"'";
				}
				if(!"".equalsIgnoreCase(CIFID.trim())){
					if("".equalsIgnoreCase(where_clause)){
						where_clause="CIF_ID ='"+CIFID+"'";
					}
					else{
						where_clause+=" And CIF_ID ='"+CIFID+"'";
					}
				}
					
				if(!"".equalsIgnoreCase(EmpName)){
					if("".equalsIgnoreCase(where_clause)){
						where_clause="Employer_Name='"+EmpName+"'";
					}
					else{
						where_clause+=" And Employer_Name='"+EmpName+"'";
					}
				}
					
				if(!"".equalsIgnoreCase(EmpCode)){
					if("".equalsIgnoreCase(where_clause)){
						where_clause="EmpCode='"+EmpCode+"'";
					}
					else{
						where_clause+=" And EmpCode='"+EmpCode+"'";
					}
				}
					/*query+="EmpCode='"+EmpCode+"'";
				else
					query=query.substring(0,query.length()-3);

				query+=") and dec.workstepName in ('FCU','FPU')";*/
				query+=where_clause;
				PersonalLoanS.mLogger.info("PL"+ "query is: "+query);

				List<List<String>> list=formObject.getDataFromDataSource(query);
				int count =0;
				PersonalLoanS.mLogger.info("PL"+"##Arun");
				
				for (List<String> a : list) 
				{
					count =count+1;
					formObject.addItemFromList("CustDetailVerification1_ListView1", a);
					
				}				
			}
		
			else if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Add1")){
				//formObject.setNGValue("Address_wi_name",formObject.getWFWorkitemName());
				//SKLogger.writeLog("PL", "Inside add button: "+formObject.getNGValue("Address_wi_name"));
				//formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_Telloggrid");
				//++ below code added by abhishek to add in telecom grid as per FSD 2.7.3
				//++Below code added by nikhil 13/11/2017 for Code merge

				//++Below code added by  nikhil 10/10/17 winame added in ngaddrow
				formObject.setNGValue("NotepadDetails_winame",formObject.getWFWorkitemName());
				//++above code added by  nikhil 10/10/17 winame added in ngaddrow
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
				PersonalLoanS.mLogger.info(" inside tele calling frame in hold_CPV"+""+ sdf.format(cal.getTime()) );
				formObject.setNGValue("NotepadDetails_username", formObject.getUserName());
				formObject.setNGValue("NotepadDetails_time",sdf.format(cal.getTime()) );
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_Telloggrid");
				int rowCount = formObject.getLVWRowCount("cmplx_NotepadDetails_cmplx_Telloggrid");
				if(rowCount==1){
					formObject.setNGValue("cmplx_NotepadDetails_cmplx_Telloggrid", 0,0,"1");
					String dateinfield = formObject.getNGValue("cmplx_NotepadDetails_cmplx_Telloggrid", 0,1);
					formObject.setNGValue("cmplx_NotepadDetails_cmplx_Telloggrid", 0,1, dateinfield+" "+sdf.format(cal.getTime()));
				}
				else{
					formObject.setNGValue("cmplx_NotepadDetails_cmplx_Telloggrid", (rowCount-1),0,String.valueOf(rowCount));
					String dateinfield = formObject.getNGValue("cmplx_NotepadDetails_cmplx_Telloggrid", (rowCount-1),1);
					formObject.setNGValue("cmplx_NotepadDetails_cmplx_Telloggrid", (rowCount-1),1, dateinfield+" "+sdf.format(cal.getTime()));
				}
				//--above code added by  nikhil 11/10/17 as per CC FSD 2.2
				//--Above code added by nikhil 13/11/2017 for Code merge


				/* Calendar cal = Calendar.getInstance();
		        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		        PersonalLoanS.mLogger.info(" inside tele calling frame in CPV" );
		        formObject.setNGValue("Tele_wi_name",formObject.getWFWorkitemName());
		        formObject.setNGValue("NotepadDetails_username", formObject.getUserName());
		        formObject.setNGValue("NotepadDetails_time",sdf.format(cal.getTime()) );
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_NotepadDetails_cmplx_Telloggrid");
			//-- Above code added by abhishek to add in telecom grid as per FSD 2.7.3
				 */		}
			else if (pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Clear")){
				//++ below code changed by abhishek as per FSD 2.7.3



				formObject.ExecuteExternalCommand("NGClear", "cmplx_NotepadDetails_cmplx_Telloggrid");
				//-- Above code changed by abhishek as per FSD 2.7.3
				//++Below code added by nikhil 13/11/2017 for Code merge
				Date date = new Date();
				String modifiedDate= new SimpleDateFormat("dd/MM/yyyy").format(date);
				formObject.setNGValue("NotepadDetails_Dateofcalling", modifiedDate);
				//--Above code added by nikhil 13/11/2017 for Code merge
			}
			//++ below code added by abhishek as per FSD 2.7.3
			else if(pEvent.getSource().getName().equalsIgnoreCase("OfficeandMobileVerification_Enable")){
				String sQuery = "SELECT activityname FROM WFINSTRUMENTTABLE with (nolock) WHERE ProcessInstanceID = '"+formObject.getWFWorkitemName()+"'";
				List<List<String>> list=formObject.getDataFromDataSource(sQuery);
				PersonalLoanS.mLogger.info(" In CC CPV workstep eventDispatched()"+ "Enable button click::query result is::"+list );
				for(int i =0;i<list.size();i++ ){
					if(list.get(i).get(0).equalsIgnoreCase("Smart_CPV")){
						// formObject.setLocked("OfficeandMobileVerification_Frame1", true);
						alert_msg = "Cannot enable as one Instance is Already at Smart CPV";
						throw new ValidatorException(new FacesMessage(alert_msg));						
					}
					else{
						formObject.setLocked("OfficeandMobileVerification_Frame1", false);
						formObject.setEnabled("OfficeandMobileVerification_Enable", false);
						 //formObject.setLocked("cmplx_OffVerification_fxdsal_override", true);
						 //.setLocked("cmplx_OffVerification_accprovd_override", true);
						 //formObject.setLocked("cmplx_OffVerification_desig_override", true);
						// formObject.setLocked("cmplx_OffVerification_doj_override", true);
						// formObject.setLocked("cmplx_OffVerification_cnfrmjob_override", true);
						 formObject.setEnabled("cmplx_OffVerification_cnfminjob_ver", true);
						formObject.setLocked("cmplx_OffVerification_cnfminjob_ver", false);
						formObject.setLocked("cmplx_OffVerification_colleaguenoverified",true); // hritik 24.6.21 pcasi 3442
						//pcasi 3608
						formObject.setLocked("cmplx_OffVerification_fxdsal_upd", true);
						 formObject.setLocked("cmplx_OffVerification_accpvded_upd", true);
						 formObject.setLocked("cmplx_OffVerification_desig_upd", true);
						 formObject.setLocked("cmplx_OffVerification_doj_upd", true);
						 formObject.setLocked("cmplx_OffVerification_OthDesign_Upt", true);
						 formObject.setLocked("cmplx_OffVerification_cnfrminjob_upd", true);
						
						 formObject.setLocked("cmplx_OffVerification_cnfrminjob_val", true);
							formObject.setEnabled("cmplx_OffVerification_cnfrminjob_val", false);
							formObject.setEnabled("cmplx_OffVerification_fxdsal_override",true);
							formObject.setEnabled("cmplx_OffVerification_accprovd_override",true);
							formObject.setEnabled("cmplx_OffVerification_desig_override",true);
							formObject.setEnabled("cmplx_OffVerification_doj_override",true);
							formObject.setEnabled("cmplx_OffVerification_cnfrmjob_override", true);
					}
				}//by shweta
				 if("Yes".equalsIgnoreCase(formObject.getNGValue("'cmplx_OffVerification_offtelnocntctd")))// Added by Rajan
				 {
					 if("CPV".equalsIgnoreCase(formObject.getWFActivityName()) || "CPV_Analyst".equalsIgnoreCase(formObject.getWFActivityName()))
					 {
						 formObject.setLocked("cmplx_OffVerification_colleaguename",false);
						 formObject.setEnabled("cmplx_OffVerification_colleaguename",true);
						 formObject.setLocked("cmplx_OffVerification_colleaguedesig",false);
						 formObject.setEnabled("cmplx_OffVerification_colleaguedesig",true);
					 }
				 }
				 if("--Select--".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_hrdnocntctd"))&& "--Select--".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_colleaguenoverified")))
				 {
					 formObject.setLocked("cmplx_OffVerification_hrdnocntctd",false);
				//	 formObject.setLocked("cmplx_OffVerification_colleaguenoverified",false); hritik 24.6.21 pcasi 3442
					 formObject.setLocked("cmplx_OffVerification_hrdemailverified",false);
				 }
				 if("Yes".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_hrdnocntctd")))
				 {
					 PersonalLoanS.mLogger.info( "@sagarika new hrd");
					 
					 formObject.setLocked("cmplx_OffVerification_offtelnovalidtdfrom",true);//PCAS-2514 sagarika
					 formObject.setLocked("cmplx_OffVerification_hrdcntctname",false);//sagarika for missed out field
					 formObject.setLocked("cmplx_OffVerification_hrdcntctdesig",false);
					 formObject.setLocked("cmplx_OffVerification_hrdcntctno",false);
					 formObject.setLocked("cmplx_OffVerification_fxdsal_ver",false);
					 formObject.setEnabled("cmplx_OffVerification_fxdsal_ver",true);
					 formObject.setLocked("cmplx_OffVerification_accpvded_ver",false);
					 formObject.setEnabled("cmplx_OffVerification_accpvded_ver",true);
					 formObject.setLocked("cmplx_OffVerification_doj_ver",false);
					 formObject.setEnabled("cmplx_OffVerification_doj_ver",true);
					 formObject.setLocked("cmplx_OffVerification_desig_ver",false);
					 formObject.setEnabled("cmplx_OffVerification_desig_ver",true);
					 formObject.setEnabled("cmplx_OffVerification_doj_ver",true);
					 formObject.setLocked("cmplx_OffVerification_cnfminjob_ver",false);
					 formObject.setEnabled("cmplx_OffVerification_cnfminjob_ver",true);
					 if("NA".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_cnfminjob_ver")))
					 {
						 formObject.setLocked("cmplx_OffVerification_cnfminjob_ver",true);
						 // formObject.setNGValue("cmplx_OffVerification_cnfminjob_ver","--Select--");
					 }
					 else{
						 formObject.setLocked("cmplx_OffVerification_cnfminjob_ver",false);
						 //	 formObject.setEnabled("cmplx_OffVerification_cnfminjob_ver",true);
					 }

					 formObject.setLocked("cmplx_OffVerification_colleagueno",true);
					 formObject.setLocked("cmplx_OffVerification_colleaguename",true);
					 formObject.setLocked("cmplx_OffVerification_colleaguedesig",true);
					 formObject.setLocked("cmplx_OffVerification_hrdemailid",true);
					 // formObject.setNGValue("cmplx_OffVerification_hrdemailverified","--Select--");
					 formObject.setLocked("cmplx_OffVerification_hrdemailverified",true);
					 // formObject.setNGValue("cmplx_OffVerification_colleaguenoverified","--Select--");
					 formObject.setLocked("cmplx_OffVerification_colleaguenoverified",true);
					 formObject.setLocked("cmplx_OffVerification_colleagueno",true);
					 formObject.setNGValue("cmplx_OffVerification_colleagueno","");
					 // formObject.setNGValue("cmplx_OffVerification_colleaguenoverified","");
					 formObject.setNGValue("cmplx_OffVerification_colleaguename","");
					 formObject.setLocked("cmplx_OffVerification_colleaguename",true);
					 formObject.setLocked("cmplx_OffVerification_colleaguedesig",true);
					 formObject.setNGValue("cmplx_OffVerification_colleaguedesig","");
					 //	 formObject.setNGValue("cmplx_OffVerification_colleaguenoverified","--Select--");
					 formObject.setLocked("cmplx_OffVerification_colleaguenoverified",true);
					 formObject.setLocked("cmplx_OffVerification_colleagueno",true);
					 formObject.setEnabled("cmplx_OffVerification_fxdsal_upd", true);
					 formObject.setEnabled("cmplx_OffVerification_accpvded_upd", true);
					 formObject.setEnabled("cmplx_OffVerification_desig_upd", true);
					 formObject.setEnabled("cmplx_OffVerification_doj_upd", true);
					 formObject.setEnabled("cmplx_OffVerification_OthDesign_Upt", true);

					 formObject.setLocked("cmplx_OffVerification_fxdsal_upd", true);
					 formObject.setLocked("cmplx_OffVerification_accpvded_upd", true);
					 formObject.setLocked("cmplx_OffVerification_desig_upd", true);
					 formObject.setLocked("cmplx_OffVerification_doj_upd", true);
					 formObject.setLocked("cmplx_OffVerification_OthDesign_Upt", true);				 

				 }
				 else if("Yes".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_hrdemailverified"))){

					 formObject.setNGValue("cmplx_OffVerification_hrdnocntctd","--Select--");
					 formObject.setLocked("cmplx_OffVerification_hrdnocntctd",true);
					 formObject.setLocked("cmplx_OffVerification_hrdcntctno",true);
					 formObject.setNGValue("cmplx_OffVerification_hrdcntctno","");
					 // formObject.setNGValue("cmplx_OffVerification_hrdcntctdesig",""); 
					 formObject.setNGValue("cmplx_OffVerification_colleaguenoverified","--Select--");
					 formObject.setLocked("cmplx_OffVerification_hrdcntctname",false);
					 // formObject.setNGValue("cmplx_OffVerification_hrdcntctname","");
					 formObject.setLocked("cmplx_OffVerification_offtelnovalidtdfrom",true);
					 // formObject.setLocked("cmplx_OffVerification_hrdcntctname",false);
					 formObject.setLocked("cmplx_OffVerification_hrdemailid",false);
					 formObject.setLocked("cmplx_OffVerification_hrdcntctdesig",false);
					 //formObject.setLocked("cmplx_OffVerification_hrdemailverified",false);
					 formObject.setLocked("cmplx_OffVerification_fxdsal_ver",false);
					 formObject.setEnabled("cmplx_OffVerification_fxdsal_ver",true);
					 formObject.setLocked("cmplx_OffVerification_accpvded_ver",false);
					 formObject.setEnabled("cmplx_OffVerification_accpvded_ver",true);
					 formObject.setLocked("cmplx_OffVerification_doj_ver",false);
					 formObject.setEnabled("cmplx_OffVerification_doj_ver",true);
					 formObject.setLocked("cmplx_OffVerification_desig_ver",false);
					 formObject.setEnabled("cmplx_OffVerification_desig_ver",true);
					 formObject.setLocked("cmplx_OffVerification_cnfminjob_ver",false);
					 if("NA".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_cnfminjob_ver")))
					 {
						 formObject.setLocked("cmplx_OffVerification_cnfminjob_ver",true);
						 // formObject.setNGValue("cmplx_OffVerification_cnfminjob_ver","--Select--");
					 }
					 else{
						 formObject.setLocked("cmplx_OffVerification_cnfminjob_ver",false);
						 formObject.setEnabled("cmplx_OffVerification_cnfminjob_ver",true);
					 }
					 formObject.setLocked("cmplx_OffVerification_colleagueno",true);
					 formObject.setLocked("cmplx_OffVerification_colleaguename",true);
					 formObject.setLocked("cmplx_OffVerification_colleaguedesig",true);
					 // formObject.setNGValue("cmplx_OffVerification_colleaguenoverified","--Select--");
					 formObject.setLocked("cmplx_OffVerification_colleaguenoverified",true);
					 formObject.setLocked("cmplx_OffVerification_colleagueno",true);
					 formObject.setNGValue("cmplx_OffVerification_colleagueno","");
					 // formObject.setNGValue("cmplx_OffVerification_colleaguenoverified","");
					 formObject.setNGValue("cmplx_OffVerification_colleaguename","");
					 formObject.setLocked("cmplx_OffVerification_colleaguename",true);
					 formObject.setLocked("cmplx_OffVerification_colleaguedesig",true);
					 formObject.setNGValue("cmplx_OffVerification_colleaguedesig","");
					 formObject.setEnabled("cmplx_OffVerification_fxdsal_upd", true);
					 formObject.setEnabled("cmplx_OffVerification_accpvded_upd", true);
					 formObject.setEnabled("cmplx_OffVerification_desig_upd", true);
					 formObject.setEnabled("cmplx_OffVerification_OthDesign_Upt", true);
					 formObject.setEnabled("cmplx_OffVerification_doj_upd", true);
					 formObject.setLocked("cmplx_OffVerification_fxdsal_upd", true);
					 formObject.setLocked("cmplx_OffVerification_accpvded_upd", true);
					 formObject.setLocked("cmplx_OffVerification_desig_upd", true);
					 formObject.setLocked("cmplx_OffVerification_OthDesign_Upt", true);
					 formObject.setLocked("cmplx_OffVerification_doj_upd", true);


				 }
				 else
				 {
					 PersonalLoanS.mLogger.info( "@sagarika no");
					 formObject.setNGValue("cmplx_OffVerification_fxdsal_ver","");//sagarika for 3225
					 formObject.setNGValue("cmplx_OffVerification_offtelnovalidtdfrom","--Select--");
					 formObject.setLocked("cmplx_OffVerification_offtelnovalidtdfrom",true);
					 formObject.setNGValue("cmplx_OffVerification_hrdemailverified","--Select--");//PCAS-2514 sagarika
					 formObject.setNGValue("cmplx_OffVerification_hrdcntctno","");//PCAS-2514 sagarika
					 formObject.setNGValue("cmplx_OffVerification_hrdemailid","");//PCAS-2514 sagarika
					 formObject.setLocked("cmplx_OffVerification_hrdcntctname",true);//sagarika for missed out field
					 formObject.setLocked("cmplx_OffVerification_hrdcntctno",true);
					 formObject.setLocked("cmplx_OffVerification_hrdcntctname",true);
					 formObject.setLocked("cmplx_OffVerification_hrdemailid",true);
					 formObject.setLocked("cmplx_OffVerification_hrdcntctdesig",true);
					 formObject.setLocked("cmplx_OffVerification_hrdemailverified",false);
					 formObject.setLocked("cmplx_OffVerification_fxdsal_ver",true);
					 formObject.setLocked("cmplx_OffVerification_accpvded_ver",true);
					 formObject.setLocked("cmplx_OffVerification_desig_ver",true);
					 formObject.setLocked("cmplx_OffVerification_doj_ver",true);
					 formObject.setLocked("cmplx_OffVerification_cnfminjob_ver",true);
				//	 formObject.setLocked("cmplx_OffVerification_colleaguenoverified",false); hritik 24.6.21 pcasi 3442
					 //formObject.setNGValue("cmplx_OffVerification_colleaguenoverified","--Select--");
					 formObject.setLocked("cmplx_OffVerification_hrdnocntctd",false);
					 if(!"Yes".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_colleaguenoverified")))
					 {
						 //if(formObject.getNGValue("cmplx_OffVerification_colleaguenoverified").equ)
						 formObject.setLocked("cmplx_OffVerification_colleagueno",true);
						 formObject.setLocked("cmplx_OffVerification_colleaguename",true);
						 formObject.setLocked("cmplx_OffVerification_colleaguedesig",true);
					 }
					 else
					 {
						 formObject.setLocked("cmplx_OffVerification_colleagueno",false);
						 formObject.setLocked("cmplx_OffVerification_colleaguename",false);
						 formObject.setLocked("cmplx_OffVerification_colleaguedesig",false);
						 formObject.setLocked("cmplx_OffVerification_hrdnocntctd",true);
						 formObject.setLocked("cmplx_OffVerification_hrdemailverified",true);
					 }
				 }


				 formObject.setNGValue("cmplx_OffVerification_EnableFlag", true);
				 String los=formObject.getNGValue("cmplx_EmploymentDetails_DOJ");
				 String los_in_current=formObject.getNGValue("cmplx_EmploymentDetails_LOS");
				 formObject.setNGValue("cmplx_OffVerification_cnfrminjob_val",formObject.getNGValue("cmplx_EmploymentDetails_JobConfirmed"));
				 String curr="0.06";
				 if(los_in_current.compareTo(curr)>-1)
				 {
					 formObject.setNGValue("cmplx_OffVerification_cnfminjob_ver","NA");
					 formObject.setLocked("cmplx_OffVerification_cnfminjob_ver",true);
				 }
								 formObject.RaiseEvent("WFSave");



			}
			//-- Above code added by abhishek as per FSD 2.7.3
			else if(pEvent.getSource().getName().equalsIgnoreCase("NotepadDetails_Button3")){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("Frame14");
			}

			//++ below code added by abhishek for non contactable button as per FSD 2.7.3



			else if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_nonContactable")){
				formObject.fetchFragment("Notepad_Values", "NotepadDetails", "q_Note");
				formObject.fetchFragment("Smart_check", "SmartCheck", "q_SmartCheck");
				formObject.fetchFragment("Office_verification", "OfficeandMobileVerification", "q_OffVerification");
				formObject.setNGFrameState("Office_verification", 1);
				formObject.setNGFrameState("Smart_check", 1);
				formObject.setNGFrameState("Notepad_Values", 1);
				formObject.setEnabled("DecisionHistory_nonContactable", false);
				formObject.setEnabled("NotepadDetails_Frame1", false);
				formObject.setEnabled("DecisionHistory_Frame1", false);
				formObject.setEnabled("SmartCheck_Frame1", false);
				formObject.setEnabled("OfficeandMobileVerification_Frame1", false);

				formObject.setEnabled("DecisionHistory_cntctEstablished", true);

				formObject.setNGValue("cmplx_Decision_Decision","Smart CPV Hold");

				DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
				Date date = new Date();
				String currDate = dateFormat.format(date);
				PersonalLoanS.mLogger.info(" In CC smartCPV.... value of current date time for non contactable is :: "+ ""+currDate);



				String sQuery = "insert into ng_rlos_SmartCPV_datetime (wi_name,datetime_nonContactable) values('"+formObject.getWFWorkitemName()+"','"+currDate+"')" ;
				PersonalLoanS.mLogger.info(" In CC smartCPV.... value of query for non contactable is :: "+ ""+sQuery);
				formObject.saveDataIntoDataSource(sQuery);
				formObject.setNGValue("cmplx_Decision_contactableFlag", true);
				formObject.RaiseEvent("WFSave");

			}
			//-- Above code added by abhishek  for non contactable button  as per FSD 2.7.3

			//++ below code added by abhishek for contact established button as per FSD 2.7.3
			else if(pEvent.getSource().getName().equalsIgnoreCase("DecisionHistory_cntctEstablished")){
				formObject.fetchFragment("Notepad_Values", "NotepadDetails", "q_Note");
				formObject.fetchFragment("Smart_check", "SmartCheck", "q_SmartCheck");
				formObject.fetchFragment("Office_verification", "OfficeandMobileVerification", "q_OffVerification");
				formObject.setNGFrameState("Office_verification", 1);
				formObject.setNGFrameState("Smart_check", 1);
				formObject.setNGFrameState("Notepad_Values", 1);
				formObject.setEnabled("NotepadDetails_Frame1", true);
				formObject.setEnabled("DecisionHistory_Frame1", true);
				formObject.setEnabled("SmartCheck_Frame1", true);
				formObject.setEnabled("OfficeandMobileVerification_Frame1", true);

				formObject.setEnabled("DecisionHistory_cntctEstablished", false);
				formObject.setEnabled("DecisionHistory_nonContactable", true);
				formObject.setNGValue("cmplx_Decision_Decision","--Select--");

				DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
				Date date = new Date();
				String currDate = dateFormat.format(date);
				PersonalLoanS.mLogger.info(" In CC smartCPV.... value of current date time for contact estblished is :: "+ ""+currDate);


				String sQuery = "insert into ng_rlos_SmartCPV_datetime (wi_name,datetime_ContactEstablished) values('"+formObject.getWFWorkitemName()+"','"+currDate+"')" ;
				PersonalLoanS.mLogger.info(" In CC smartCPV.... value of query for contactable is :: "+ ""+sQuery);
				formObject.saveDataIntoDataSource(sQuery);
				formObject.setNGValue("cmplx_Decision_contactableFlag", false);
				formObject.RaiseEvent("WFSave");
			}

			//-- Above code added by abhishek for contact established button as per FSD 2.7.3
			else if(pEvent.getSource().getName().equalsIgnoreCase("CustDetailVerification1_save")){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("Cust_Det_Ver");
				alert_msg="Customer Verification details saved";

				throw new ValidatorException(new FacesMessage(alert_msg));


			}

			else if(pEvent.getSource().getName().equalsIgnoreCase("BussinessVerification1_save")){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("Business_Verif");
			}
			else if(pEvent.getSource().getName().equalsIgnoreCase("EmploymentVerification_save")){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("Emp_Verification");
				//below code added by nikhil 4/12/17
				alert_msg="Employment Verification details saved";

				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			else if(pEvent.getSource().getName().equalsIgnoreCase("SmartCheck1_save")){
				//++Below code added by  nikhil 31/10/17 as per CC FSD 2.7

				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("Smart_chk");
				//--Above code added by nikhil 31/10/17 as per CC FSD 2.7
				//below code added by nikhil 4/12/17
				alert_msg="Smart Check details saved";

				throw new ValidatorException(new FacesMessage(alert_msg));
			}

			//--Above code added by nikhil 13/11/2017 for Code merge

			//added above by Tarang against drop 4 TakeOver on 04/04/2018
			if ("PostDisbursal_LMSDocumentUpdate".equalsIgnoreCase(pEvent.getSource().getName())){
				String AgreementId="";
				PersonalLoanS.mLogger.info( "Inside LMSDocumentUpdate button: ");
				outputResponse = genX.GenerateXML("UPDATE_LOAN_DETAILS","TakeOver");
				ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);
				ReadXml.valueSetIntegration(outputResponse,"");  
				AgreementId= outputResponse.contains("<AgreementId>") ? outputResponse.substring(outputResponse.indexOf("<AgreementId>")+"</AgreementId>".length()-1,outputResponse.indexOf("</AgreementId>")):"";
			}
			//added above by Tarang against drop 4 TakeOver on 04/04/2018

			//++Below code added by nikhil for Toteam
			else if(pEvent.getSource().getName().equalsIgnoreCase("PostDisbursal_LC_Add"))
			{
				PersonalLoanS.mLogger.info("Post disbursal grid test start");
			
				String lc_doc_name=formObject.getNGValue("PostDisbursal_LC_LC_Doc_Name");
				String bank_name=formObject.getNGValue("PostDisbursal_Bank_Name");
				//added by yash on 19/12/2017 for to team
				String bank_status=formObject.getNGValue("PostDisbursal_Status");
				String bank_type=formObject.getNGValue("PostDisbursal_Bank_Type");
				String emirates=formObject.getNGValue("PostDisbursal_Emirate");
				//ended by yash 
				PersonalLoanS.mLogger.info("Post disbursal grid test"+emirates+bank_name);
//
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate");
				ToTeam_AddtoGrids(formObject,lc_doc_name,bank_name,bank_status,bank_type);

			} //PostDisbursal_LC_LC_Doc_Name
			//++below code changed by yash on 17/12/2017 for Toteam
			else if(pEvent.getSource().getName().equalsIgnoreCase("PostDisbursal_LC_Modify"))
			{	
				//loadPicklist_LiabilityCertificate();
				String lc_doc_name=formObject.getNGValue("PostDisbursal_LC_LC_Doc_Name");
				String bank_name=formObject.getNGValue("PostDisbursal_Bank_Name");
				String bank_status=formObject.getNGValue("PostDisbursal_Status");
				String bank_type=formObject.getNGValue("PostDisbursal_Bank_Type");
				
				int row_count=formObject.getLVWRowCount("cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate");
				String status=formObject.getNGValue("cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate", row_count-1, 8);
				//formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate");
				int row_count1=formObject.getLVWRowCount("cmplx_PostDisbursal_cmplx_gr_ManagersCheque");
				String status1=formObject.getNGValue("cmplx_PostDisbursal_cmplx_gr_ManagersCheque", row_count1-1, 6);
				int row_count2=formObject.getLVWRowCount("cmplx_PostDisbursal_cmplx_gr_BankGuarantee");
				String status2=formObject.getNGValue("cmplx_PostDisbursal_cmplx_gr_BankGuarantee", row_count2-1, 12);
				PersonalLoanS.mLogger.info("Post disbursal grid test for modify"+status+status1+status2+row_count1);
				if(status.equalsIgnoreCase(status1 ))
				{
					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate");
					formObject.setSelectedIndex("cmplx_PostDisbursal_cmplx_gr_ManagersCheque", row_count1-1);
					
					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_PostDisbursal_cmplx_gr_ManagersCheque");
					formObject.setLocked("PostDisbursal_MCQ_Issue_Date",false);
					formObject.setLocked("PostDisbursal_MCQ_Deposit_Date",false);
				}
				if (status.equalsIgnoreCase(status2 ))
				{
					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate");
					formObject.setSelectedIndex("cmplx_PostDisbursal_cmplx_gr_BankGuarantee", row_count2-1);
					formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_PostDisbursal_cmplx_gr_BankGuarantee");
				}

				ToTeam_AddtoGrids(formObject,lc_doc_name,bank_name,bank_status,bank_type);
			}
			else if(pEvent.getSource().getName().equalsIgnoreCase("PostDisbursal_LC_Delete"))
			{
				int row_count=formObject.getLVWRowCount("cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate");
				String status=formObject.getNGValue("cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate", row_count-1, 8);
				//formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate");
				int row_count1=formObject.getLVWRowCount("cmplx_PostDisbursal_cmplx_gr_ManagersCheque");
				String status1=formObject.getNGValue("cmplx_PostDisbursal_cmplx_gr_ManagersCheque", row_count1-1, 6);
				int row_count2=formObject.getLVWRowCount("cmplx_PostDisbursal_cmplx_gr_BankGuarantee");
				
				String status2=formObject.getNGValue("cmplx_PostDisbursal_cmplx_gr_BankGuarantee", row_count2-1, 12);
				PersonalLoanS.mLogger.info("Post disbursal grid test for modify"+status+status1+status2);
				if(status.equalsIgnoreCase(status1 ))
				{
				
					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate");
					formObject.setSelectedIndex("cmplx_PostDisbursal_cmplx_gr_ManagersCheque", row_count1-1);
					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_PostDisbursal_cmplx_gr_ManagersCheque");
				}
				if (status.equalsIgnoreCase(status2 ))
				{
					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate");
					formObject.setSelectedIndex("cmplx_PostDisbursal_cmplx_gr_BankGuarantee", row_count2-1);
					formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_PostDisbursal_cmplx_gr_BankGuarantee");
				}

			}
			//++Above code added by yash on 26/12/2017 for toteam
			else if(pEvent.getSource().getName().equalsIgnoreCase("PostDisbursal_MCQ_Add"))
			{
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_PostDisbursal_cmplx_gr_ManagersCheque");
			}
			else if(pEvent.getSource().getName().equalsIgnoreCase("PostDisbursal_MCQ_Modify"))
			{
				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_PostDisbursal_cmplx_gr_ManagersCheque");
			}
			else if(pEvent.getSource().getName().equalsIgnoreCase("PostDisbursal_MCQ_Delete"))
			{
				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_PostDisbursal_cmplx_gr_ManagersCheque");
			}
			else if(pEvent.getSource().getName().equalsIgnoreCase("PostDisbursal_BG_Add"))
			{
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_PostDisbursal_cmplx_gr_BankGuarantee");
			}
			else if(pEvent.getSource().getName().equalsIgnoreCase("PostDisbursal_BG_Modify"))
			{
				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_PostDisbursal_cmplx_gr_BankGuarantee");
			}
			else if(pEvent.getSource().getName().equalsIgnoreCase("PostDisbursal_BG_Delete"))
			{
				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_PostDisbursal_cmplx_gr_BankGuarantee");
			}
			else if(pEvent.getSource().getName().equalsIgnoreCase("PostDisbursal_NLC_Add"))
			{
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_PostDisbursal_cpmlx_gr_NLC");
			}
			//changed by yash on 22/12/2017
			else if(pEvent.getSource().getName().equalsIgnoreCase("PostDisbursal_NLC_Modify"))
			{
				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_PostDisbursal_cpmlx_gr_NLC");
				ToTeamStl(formObject);
			}
			else if(pEvent.getSource().getName().equalsIgnoreCase("PostDisbursal_NLC_Delete"))
			{
				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_PostDisbursal_cpmlx_gr_NLC");
			}
			else if("PostDisbursal_Save".equalsIgnoreCase(pEvent.getSource().getName()))
			{
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("Post_Disbursal");
				alert_msg=NGFUserResourceMgr_PL.getAlert("VAL505");
				PersonalLoanS.mLogger.info("Hey we are testing post disbursal save");
				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			//++ Below Code added By Yash on Oct 9, 2017  to fix : "to save liability" : Reported By Shashank on Oct 09, 2017++
			else if("ExtLiability_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("InternalExternalLiability");
				alert_msg=NGFUserResourceMgr_PL.getAlert("val019");

				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			//--Above code added by nikhil for Toteam

			//added by akshay on 8/12/17
			else if(pEvent.getSource().getName().equalsIgnoreCase("ReferHistory_Modify"))
			{
				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_ReferHistory_cmplx_GR_ReferHistory");
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("ReferHistory");
			}	
			//ended by akshay on 8/12/17
			else if (pEvent.getSource().getName().equalsIgnoreCase("Compliance_Modify"))
			{
				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_Compliance_cmplx_gr_compliance");
			}//changes for DROP-4 by saurabh.
			else if("SupplementCardDetails_Add".equalsIgnoreCase(pEvent.getSource().getName()))
			{
				//PersonalLoanS.mLogger.info("Suplementary add");
				formObject.setNGValue("SupplementCard_WiName", formObject.getWFWorkitemName());
				PersonalLoanS.mLogger.info("Selected Card Prod id: "+formObject.getNGSelectedItemText("SupplementCardDetails_CardProduct"));
				addDynamicInCRNGrid(formObject,formObject.getNGValue("SupplementCardDetails_passportNo"),formObject.getNGSelectedItemText("SupplementCardDetails_CardProduct"));
				formObject.ExecuteExternalCommand("NGAddRow", "SupplementCardDetails_cmplx_supplementGrid");
				AddFromHiddenList("SupplementCardDetails_AddressList","cmplx_AddressDetails_cmplx_AddressGrid");
				AddFromHiddenList("SupplementCardDetails_FatcaList","cmplx_FATCA_cmplx_FATCAGrid");
				AddFromHiddenList("SupplementCardDetails_KYCList","cmplx_KYC_cmplx_KYCGrid");
				AddFromHiddenList("SupplementCardDetails_OecdList","cmplx_OECD_cmplx_GR_OecdDetails");
				
				//setDataInMultipleAppGrid();
				loadDynamicPickList();
			}
			else if("SupplementCardDetails_Modify".equalsIgnoreCase(pEvent.getSource().getName()))
			{
				modifyDynamicInCRNGrid(formObject,formObject.getNGValue("SupplementCardDetails_passportNo"),formObject.getNGSelectedItemText("SupplementCardDetails_CardProduct"));
				formObject.ExecuteExternalCommand("NGModifyRow", "SupplementCardDetails_cmplx_supplementGrid");
				AddFromHiddenList("SupplementCardDetails_AddressList","cmplx_AddressDetails_cmplx_AddressGrid");
				AddFromHiddenList("SupplementCardDetails_FatcaList","cmplx_FATCA_cmplx_FATCAGrid");
				AddFromHiddenList("SupplementCardDetails_KYCList","cmplx_KYC_cmplx_KYCGrid");
				AddFromHiddenList("SupplementCardDetails_OecdList","cmplx_OECD_cmplx_GR_OecdDetails");

				//setDataInMultipleAppGrid();
				loadDynamicPickList();
			}
			else if("SupplementCardDetails_Delete".equalsIgnoreCase(pEvent.getSource().getName()))
			{
				
				formObject.ExecuteExternalCommand("NGDeleteRow", "SupplementCardDetails_cmplx_supplementGrid");
				loadDynamicPickList();

			}
			else if("SupplementCardDetails_Save".equalsIgnoreCase(pEvent.getSource().getName())){
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("Supplementary_Card_Details");
				alert_msg=NGFUserResourceMgr_PL.getAlert("val055");
				loadDataInCRNGrid();

				throw new ValidatorException(new FacesMessage(alert_msg));
			}

			//++ below code added by abhishek on 04/01/2018 for EFMS refresh functionality


			else if("ELigibiltyAndProductInfo_Refresh".equalsIgnoreCase(pEvent.getSource().getName()) || "DecisionHistory_EFMS_Status".equalsIgnoreCase(pEvent.getSource().getName()))
			{
				PersonalLoanS.mLogger.info("Inside refresh button code::");
				//Changes done by Deepak 16 Dec to check EFMS alert status based on application no Start.
				String app_no = formObject.getWFWorkitemName().substring(formObject.getWFWorkitemName().indexOf("-")+1, formObject.getWFWorkitemName().lastIndexOf("-"));
				 String sQuery = "Select top 1 Application_Status,isnull(REPORT_GEN_DATETIME,'') as REPORT_GEN_DATETIME,isnull(CASE_OWNER,'') as CASE_OWNER,isnull(CASE_STATUS,'') as CASE_STATUS,isnull(CLOSE_DATETIME,'') as CLOSE_DATETIME from NG_EFMS_RESPONSE with (nolock) where Application_number = '"+app_no+"'  order by SNO desc";
				 //Changes done by Deepak 16 Dec to check EFMS alert status based on application no End.
				PersonalLoanS.mLogger.info("EFMS sQuery:: "+sQuery);
				List<List<String>> list = formObject.getDataFromDataSource(sQuery);
				PersonalLoanS.mLogger.info("EFMS list:: "+list);
				 //Deepak changes done for 3076
				 String sAlertQuery="select concat(descrip,' : ', APPLICATION_STATUS) from (select 'Initial status 'as descrip ,APPLICATION_STATUS from (select top 1 APPLICATION_STATUS from NG_EFMS_RESPONSE with(nolock) where APPLICATION_NUMBER='"+app_no+"' order by  APPLICATION_STATUS) as EFMS_APPLICATION_STATUS  union all select 'Final Status ' as decrip,CASE_STATUS from (select top 1 case when CASE_STATUS='Confirmed Fraud' then 'Negative case' else isnull(CASE_STATUS,'') end as CASE_STATUS from NG_EFMS_RESPONSE with(nolock) where APPLICATION_NUMBER='"+app_no+"' order by  CASE_STATUS desc) as EFMS_CASE_STATUS) as EFMS_CaseDump";
				 PersonalLoanS.mLogger.info("EFMS sAlertQuery:: "+sAlertQuery);
				 String alertstatus="";
				 try{
					 List<List<String>> list_alertstatus = formObject.getDataFromDataSource(sAlertQuery);
					 PersonalLoanS.mLogger.info("EFMS list 2::"+list_alertstatus);
					 alertstatus = list_alertstatus.get(0).get(0)+" || "+list_alertstatus.get(1).get(0);
				 }
				 catch(Exception e){
					 PersonalLoanS.mLogger.info("Exception occured while geting alertstatus"+e.getMessage());

				 }
				if(!list.isEmpty()){
					//Deepak 22 Dec Code changes to as per doc EFMS Doc 2.0
					String Alertflag = list.get(0).get(0).toLowerCase();
					String REPORT_GEN_DATETIME = list.get(0).get(1).toLowerCase();
					String CASE_OWNER = list.get(0).get(2);
					String CASE_STATUS = list.get(0).get(3);
					String CLOSE_DATETIME = list.get(0).get(4);
					PersonalLoanS.mLogger.info("EFMS Alertflag::"+Alertflag  +"     Report Generated On : "+REPORT_GEN_DATETIME+ "  User Id is : "+CASE_OWNER);

					// set ext table flag here



					if(!"".equals(Alertflag) && !"null".equalsIgnoreCase(Alertflag)){
						 if(!"".equalsIgnoreCase(alertstatus)){
							 if ("".equalsIgnoreCase(CASE_STATUS)){
								 alert_msg = "Application status : "+alertstatus+", Report Generated on: "+ REPORT_GEN_DATETIME;
							 }
							 else{
								 alert_msg = "Application status : "+alertstatus+", User Id: " +CASE_OWNER+", Report Generated on: "+ CLOSE_DATETIME;
							 }
						 }
						 else{
							 if ("".equalsIgnoreCase(CASE_STATUS)){
								 alert_msg = "Application status : "+Alertflag+", Report Generated on: "+ REPORT_GEN_DATETIME;
							 }
							 else{
								 alert_msg = "Application status : "+CASE_STATUS+", User Id: " +CASE_OWNER+", Report Generated on: "+ CLOSE_DATETIME;
							 }
						 }
						//alert_msg = "Application status is "+Alertflag;
						//Siva Changes done for Fraud Negative Case of EFMS PCSP 2471
						//deepak Changes done to save EFMS staus in ext table for CAM report - PCSP 456
						 if(!"".equalsIgnoreCase(Alertflag)){
							 formObject.setNGValue("EFMS_IS_Alerted", Alertflag);
						 }if(!"".equalsIgnoreCase(CASE_STATUS)){
							 formObject.setNGValue("EFMS_AlertStatusFlag", CASE_STATUS);
						 }
					}else{
						alert_msg = "EFMS alert status not updated. Try again Later";
					}
 					setEfms();
					formObject.RaiseEvent("WFSave");

				}
				else{
					alert_msg = "EFMS alert status not updated. Try again Later";
				}
				throw new ValidatorException(new FacesMessage(alert_msg));
			}
			//below code added by nikhil 15/1/17
		

			else if (pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_Modify")){
				formObject.ExecuteExternalCommand("NGModifyRow", "cmplx_WorldCheck_WorldCheck_Grid");
			}

			else if (pEvent.getSource().getName().equalsIgnoreCase("WorldCheck1_Delete")){
				formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_WorldCheck_WorldCheck_Grid");

			}
			
			else if ("Nationality_Button".equalsIgnoreCase(pEvent.getSource().getName())
					||"SecNationality_Button".equalsIgnoreCase(pEvent.getSource().getName())
				    ||"Third_Nationality_Button".equalsIgnoreCase(pEvent.getSource().getName())
					||"BirthCountry_Button".equalsIgnoreCase(pEvent.getSource().getName())
					||"ResidentCountry_Button".equalsIgnoreCase(pEvent.getSource().getName())
					||"AddressDetails_Button1".equalsIgnoreCase(pEvent.getSource().getName())
					||"Nationality_ButtonPartMatch".equalsIgnoreCase(pEvent.getSource().getName())
					||"MOL_Nationality_Button".equalsIgnoreCase(pEvent.getSource().getName())
					||"CardDetails_bankName_Button".equalsIgnoreCase(pEvent.getSource().getName())
					||"EmploymentDetails_Bank_Button".equalsIgnoreCase(pEvent.getSource().getName())
					||"DesignationAsPerVisa_button".equalsIgnoreCase(pEvent.getSource().getName())
					||"EMploymentDetails_FreeZone_Button".equalsIgnoreCase(pEvent.getSource().getName())
					||"Designation_button".equalsIgnoreCase(pEvent.getSource().getName())
					||"EMploymentDetails_Designation_button".equalsIgnoreCase(pEvent.getSource().getName())//added by saurabh1 for S4 I6
					||"EMploymentDetails_DesignationAsPerVisa_button".equalsIgnoreCase(pEvent.getSource().getName())//added by saurabh1 for S4 I6
					||"CardDispatchToButton".equalsIgnoreCase(pEvent.getSource().getName())
					||"TLRM_Button".equalsIgnoreCase(pEvent.getSource().getName())
					||"Sourcing_Branch_Code_button".equalsIgnoreCase(pEvent.getSource().getName())
					||"Customer_Button2".equalsIgnoreCase(pEvent.getSource().getName())){
					//||"EmploymentVerification_s2_Designation_button2".equalsIgnoreCase(pEvent.getSource().getName())
					//||"EmploymentVerification_s2_desig_visa_Select".equalsIgnoreCase(pEvent.getSource().getName()))
				
				pickListMasterLoad(pEvent.getSource().getName());

			}
			else if("EmploymentVerification_s2_Designation_button2".equalsIgnoreCase(pEvent.getSource().getName())
					||"EmploymentVerification_s2_desig_visa_Select".equalsIgnoreCase(pEvent.getSource().getName()))
			{
				String sfieldName = "";
				if("EmploymentVerification_s2_Designation_button2".equalsIgnoreCase(pEvent.getSource().getName()))
				{
					sfieldName = "cmplx_emp_ver_sp2_desig_remarks";
				}
				else
				{
					sfieldName = "cmplx_emp_ver_sp2_desig_as_visa_update";
				}
				String query="select Code,Description from NG_MASTER_Designation with (nolock)  where isActive='Y'";
				populatePickListWindow(query,sfieldName,"Code,Description", true, 20,"Designation");
			}

			else if ("Button_State".equalsIgnoreCase(pEvent.getSource().getName())){
				String query;

				query="select description,code from NG_MASTER_state with (nolock)  where isActive='Y'";

				PersonalLoanS.mLogger.info( "query is: "+query);
				populatePickListWindow(query,"AddressDetails_state", "Description,Code", true, 20,"State");			     

			}
			else if ("Button_City".equalsIgnoreCase(pEvent.getSource().getName())){
				String query;

				query="select description,code from NG_MASTER_city with (nolock)  where isActive='Y'";

				PersonalLoanS.mLogger.info( "query is: "+query);
				populatePickListWindow(query,"AddressDetails_city", "Description,Code", true, 20,"City");			     

			}

			else if ("ButtonLoan_City".equalsIgnoreCase(pEvent.getSource().getName())){
				String query;

				query="select description,code from NG_MASTER_city with (nolock)  where isActive='Y'";

				PersonalLoanS.mLogger.info( "query is: "+query);
				populatePickListWindow(query,"cmplx_LoanDetails_city", "Description,Code", true, 20,"City");			     

			}

			else if ("ButtonOECD_State".equalsIgnoreCase(pEvent.getSource().getName())){
				String query;
				String filter_value="";
				filter_value = formObject.getNGValue("OECD_townBirth");
				if(!"".equalsIgnoreCase(filter_value)){
					query="select description,code from NG_MASTER_state with (nolock)  where isActive='Y' and code like'%"  + filter_value + "%' or Description Like'%" + filter_value + "%'";	
				}
				query="select description,code from NG_MASTER_city with (nolock)  where isActive='Y'";

				PersonalLoanS.mLogger.info( "query is: "+query);
				populatePickListWindow(query,"OECD_townBirth", "Description,Code", true, 20,"Town");			     

			}
			/*else if ("Customer_Button2".equalsIgnoreCase(pEvent.getSource().getName())){
				String query;
				String filter_value="";
				filter_value = formObject.getNGValue("Customer_Button2");
				if(!"".equalsIgnoreCase(filter_value)){
					query="select UserId, UserName from NG_MASTER_SourceCode with (nolock) and UserId like'%"  + filter_value + "%' or UserName Like'%" + filter_value + "%'";	
				}
				query="select UserId , UserId from NG_MASTER_SourceCode with (nolock)";
				PersonalLoanS.mLogger.info( "query is for dsa code button: "+query);
				populatePickListWindow(query,"Customer_Button2", "UserId,UserName", true, 20,"DSA");			     

			}*/
			else if("OriginalValidation_Save".equalsIgnoreCase(pEvent.getSource().getName()))
			{
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("Orig_validation");
				if("Document_Checker".equalsIgnoreCase(formObject.getWFActivityName())){
					throw new ValidatorException (new FacesMessage("Documnet Checker Decision Saved"));
				}
				else 
					throw new ValidatorException(new FacesMessage("Original validation Saved"));

			}
			else if("ExternalBlackList_Button1".equalsIgnoreCase(pEvent.getSource().getName()))
			{
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("OriginalValidation_Frame");
				throw new ValidatorException(new FacesMessage("External BlackList Saved"));
			}
			
			// code by bandana starts for fcu
			else if("DesignationAsPerVisa_button_View".equalsIgnoreCase(pEvent.getSource().getName())
					||"EMploymentDetails_DesignationAsPerVisa_button_View".equalsIgnoreCase(pEvent.getSource().getName())//added by saurabh1 for S4 I6
					||"EMploymentDetails_Designation_button_View".equalsIgnoreCase(pEvent.getSource().getName())// added by saurabh1 for S4 I6
					||"Designation_button_View".equalsIgnoreCase(pEvent.getSource().getName())
					||"Designation_button8_View".equalsIgnoreCase(pEvent.getSource().getName())
					||"Designation_button3_View".equalsIgnoreCase(pEvent.getSource().getName())
					||"Designation_button4_View".equalsIgnoreCase(pEvent.getSource().getName())
					||"Designation_button5_View".equalsIgnoreCase(pEvent.getSource().getName())
					||"Designation_button6_View".equalsIgnoreCase(pEvent.getSource().getName())
					||"CardDispatchToButton_View".equalsIgnoreCase(pEvent.getSource().getName())
					||"ButtonOECD_State_View".equalsIgnoreCase(pEvent.getSource().getName())
					||"Nationality_Button1_View".equalsIgnoreCase(pEvent.getSource().getName())
					||"Cust_ver_sp2_Designation_button3_View".equalsIgnoreCase(pEvent.getSource().getName())
					||"Cust_ver_sp2_Designation_button5_View".equalsIgnoreCase(pEvent.getSource().getName())
					||"EmploymentVerification_s2_Designation_button4_View".equalsIgnoreCase(pEvent.getSource().getName())
					||"EmploymentVerification_s2_Designation_button6_View".equalsIgnoreCase(pEvent.getSource().getName())
					||"AddressDetails_Button1_View".equalsIgnoreCase(pEvent.getSource().getName())	
					||"CustDetailVerification1_nationality_View".equalsIgnoreCase(pEvent.getSource().getName())//PCASI-1086
					||"Nationality_Button_View".equalsIgnoreCase(pEvent.getSource().getName())//PCASI-1086
					||"SecNationality_Button_View".equalsIgnoreCase(pEvent.getSource().getName())
					||"Third_Nationality_Button_View".equalsIgnoreCase(pEvent.getSource().getName())//By Alok for Third Nationality
					){
				PersonalLoanS.mLogger.info( "click on view button");
				LoadViewButton(pEvent.getSource().getName());
				}
			else if("EmploymentVerification_s2_Designation_button4_View".equalsIgnoreCase(pEvent.getSource().getName())
					|| "EmploymentVerification_s2_desig_visa_view".equalsIgnoreCase(pEvent.getSource().getName())
					|| "EmploymentVerification_s2_Designation_button6_View".equalsIgnoreCase(pEvent.getSource().getName())
							|| "EmploymentVerification_s2_desig_visa_update_view".equalsIgnoreCase(pEvent.getSource().getName()))
			{
				String alert="";
				String sfieldName = "";
				if ("EmploymentVerification_s2_Designation_button4_View".equalsIgnoreCase(pEvent.getSource().getName()))
				{
					sfieldName = "EmploymentVerification_s2_Text1";
				}
				else if  ("EmploymentVerification_s2_desig_visa_view".equalsIgnoreCase(pEvent.getSource().getName()))
				{
					sfieldName = "EmploymentVerification_s2_desig_visa";
				}
				else if  ("EmploymentVerification_s2_Designation_button6_View".equalsIgnoreCase(pEvent.getSource().getName()))
				{
					sfieldName = "cmplx_emp_ver_sp2_desig_remarks";
				}
				else if  ("EmploymentVerification_s2_desig_visa_update_view".equalsIgnoreCase(pEvent.getSource().getName()))
				{
					sfieldName = "cmplx_emp_ver_sp2_desig_as_visa_update";
				}
				try{
					
					String query="select Description from NG_MASTER_Designation with (nolock) where code='"+formObject.getNGValue(sfieldName)+"' AND isActive='Y'";
					PersonalLoanS.mLogger.info("query name :"+query);
					List<List<String>> result=formObject.getDataFromDataSource(query);
					if(!result.isEmpty()){
					if(result.get(0).get(0)==null || result.get(0).get(0).equals("")){

					}
					else{
					alert=result.get(0).get(0);

					}
					}
					PersonalLoanS.mLogger.info("alert name :"+alert);

					throw new ValidatorException(new FacesMessage(alert));
					}
					catch(ValidatorException ve){
					throw new ValidatorException(new FacesMessage(alert));
					}
					catch(Exception e){
						PersonalLoanS.mLogger.info("PLCommon Exception Occurred genericMaster :"+e.getMessage());
					printException(e);

					}
			}

			else if ("Cust_ver_sp2_Button1".equalsIgnoreCase(pEvent.getSource().getName())) {
				 PersonalLoanS.mLogger.info( "new cust Verification Details are saved"); 
				 Custom_fragmentSave("Customer_Details_Verification1");
				 alert_msg=NGFUserResourceMgr_PersonalLoanS.getAlert("VAL105");
				 PersonalLoanS.mLogger.info( "s Verification Details are saved123"); 
				 throw new ValidatorException(new FacesMessage(alert_msg));

			 }
			 else if ("Fpu_Grid_Button1".equalsIgnoreCase(pEvent.getSource().getName())) {
				 //CreditCard.mLogger.info( "new cust Verification Details are saved"); 
				 Custom_fragmentSave("Fpu_Grid");
				 formObject.RaiseEvent("WFSave");
				 alert_msg="FPU Grid Details Saved";
				 //CreditCard.mLogger.info( "s Verification Details are saved123"); 
				 throw new ValidatorException(new FacesMessage(alert_msg));

			 }
			else  if (pEvent.getSource().getName().equalsIgnoreCase("CustDetailVerification1_Button1")) {
				 //loadPicklist_Address();
				 //Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				 Custom_fragmentSave("Customer_Info_FPU");
				 //below code added by nikhil 4/12/17
				 alert_msg="Customer Verification details saved";
				 throw new ValidatorException(new FacesMessage(alert_msg));
			 }
			 else if ("EmploymentVerification_s2_Button1".equalsIgnoreCase(pEvent.getSource().getName())) {
				 //loadPicklist_Address();
				 PersonalLoanS.mLogger.info( "new Employment Verification Details are saved"); 
				 //Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				 Custom_fragmentSave("Employment_Verification");
				 alert_msg=NGFUserResourceMgr_PL.getAlert("VAL500");
				 PersonalLoanS.mLogger.info( "new Employment Verification Details are saved123"); 
				 throw new ValidatorException(new FacesMessage(alert_msg));

			 }
			 else if("EmploymentVerification_s2_comp_save".equalsIgnoreCase(pEvent.getSource().getName()))
					 {
				 PersonalLoanS.mLogger.info( "new Company mismatch Details are saved"); 
				 //Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				 Custom_fragmentSave("Employment_Verification");
				 alert_msg=NGFUserResourceMgr_PL.getAlert("VAL506");
				 PersonalLoanS.mLogger.info( "new Company Mismatch Details are saved123"); 
				 throw new ValidatorException(new FacesMessage(alert_msg));
					 }
			 else if ("EmploymentVerification_s2_search_TL_number".equalsIgnoreCase(pEvent.getSource().getName()))
			 {//check
				 try
				 {
					 String query="";
					 formObject.clear("cmplx_emp_ver_sp2_cmplx_comp_mismatch");
					 //Deepak Query changed for PCAS-1260
					 //query="select Product,(select TL_Number from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),(select TL_Emirate from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),PL_Wi_Name,CRN,CIF_ID,CUSTOMERNAME,Employer_Name,(select EmirateVisa from ng_RLOS_Customer where wi_name=PL_Wi_Name),(select OfficeNo from ng_RLOS_AltContactDetails where wi_name=PL_Wi_Name),(select LOS from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),(select Nationality from ng_RLOS_Customer where wi_name=PL_Wi_Name),(select yearsINUAE from ng_RLOS_Customer where wi_name=PL_Wi_Name),DSA_Name,RM_Name,(select designation from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),(select top 1 userName from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),(select top 1 remarks from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),(select top 1 Decision from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),(select subfeedback from ng_rlos_decisionHistory where wi_name=PL_Wi_Name),(select top 1 dateLastChanged from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),(select top 1 Decision from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='Cad_Analyst2' order by dateLastChanged),(select top 1 Decision from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='CPV' order by dateLastChanged),'"+formObject.getWFWorkitemName()+"'  from NG_PL_EXTTABLE where PL_Wi_Name in (select wi_name from ng_RLOS_EmpDetails where TL_Number='"+formObject.getNGValue("EmploymentVerification_s2_comp_tl_no")+"' )";
					 //below query by jahnavi to remove current wi 
					 //query="select Product,(select TL_Number from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),isnull((select TL_Emirate from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),''),PL_Wi_Name,isnull((select AgreementID from ng_rlos_LoanDisbursal where wi_name=PL_Wi_Name),''),isnull(CIF_ID,''),CUSTOMERNAME,Employer_Name,isnull((select case when EmirateVisa='--Select--' then '' else EmirateVisa end  from ng_RLOS_Customer where wi_name=PL_Wi_Name),''),isnull((select OfficeNo from ng_RLOS_AltContactDetails where wi_name=PL_Wi_Name),''),isnull((select LOS from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),''),isnull((select Nationality from ng_RLOS_Customer where wi_name=PL_Wi_Name),''),isnull((select yearsINUAE from ng_RLOS_Customer where wi_name=PL_Wi_Name),''),isnull(DSA_Name,''),isnull(RM_Name,''),isnull((select isnull(desigstatus,'') from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),''),isnull((select top 1 isnull(userName,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select top 1 isnull(remarks,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select isnull(subfeedback,'') from ng_rlos_decisionHistory where wi_name=PL_Wi_Name),''),isnull((select convert(varchar,dateLastChanged,120)  from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='Cad_Analyst2' order by dateLastChanged),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='CPV' order by dateLastChanged),''),'"+formObject.getWFWorkitemName()+"'  from NG_PL_EXTTABLE where PL_Wi_Name in (select wi_name from ng_RLOS_EmpDetails where TL_Number='"+formObject.getNGValue("EmploymentVerification_s2_comp_tl_no")+"' ) and pl_wi_name!='"+formObject.getWFWorkitemName()+"'";
					 query="select Product,(select TL_Number from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),isnull((select TL_Emirate from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),''),PL_Wi_Name,isnull((select AgreementID from ng_rlos_LoanDisbursal where wi_name=PL_Wi_Name),''),isnull(CIF_ID,''),CUSTOMERNAME,employer_name,isnull((select case when EmirateVisa='--Select--' then '' else EmirateVisa end  from ng_RLOS_Customer where wi_name=PL_Wi_Name),''),isnull((select OfficeNo from ng_RLOS_AltContactDetails where wi_name=PL_Wi_Name),''),isnull((select LOS from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),''),isnull((select description from ng_master_country where code=(select Nationality from ng_RLOS_Customer where wi_name=PL_Wi_Name)),''),isnull((select yearsINUAE from ng_RLOS_Customer where wi_name=PL_Wi_Name),''),isnull(DSA_Name,''),isnull(RM_Name,''),isnull((select description from ng_master_designation where code= (select desigstatus from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name)),''),isnull((select top 1 isnull(userName,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select top 1 isnull(remarks,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select isnull(subfeedback,'') from ng_rlos_decisionHistory where wi_name=PL_Wi_Name),''),isnull((select convert(varchar,dateLastChanged,120)  from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='Cad_Analyst2' order by dateLastChanged),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='CPV' order by dateLastChanged),''),'"+formObject.getWFWorkitemName()+"'  from NG_PL_EXTTABLE where PL_Wi_Name in (select wi_name from ng_RLOS_EmpDetails where TL_Number='"+formObject.getNGValue("EmploymentVerification_s2_comp_tl_no")+"' ) and pl_wi_name!='"+formObject.getWFWorkitemName()+"'";
					 List<List<String>> list_acc=formObject.getDataFromDataSource(query);
						PersonalLoanS.mLogger.info("Squery for data :: "+query);
						int count =0;
					 for(List<String> mylist : list_acc){
						 PersonalLoanS.mLogger.info( "Data to be added in Grid account Grid: "+mylist.toString());
						 formObject.addItemFromList("cmplx_emp_ver_sp2_cmplx_comp_mismatch", mylist);
						 count++;
						 if(count ==10)
						 {
							 break;
						 }
					 }
				}catch(Exception ex){
					 printException(ex);
				}
			}
// EmploymentVerification_approved_report
			 else if ("EmploymentVerification_s2_download_comp_report".equalsIgnoreCase(pEvent.getSource().getName()))
			 {
					try
					{
						String Product= "";   String TL_Number = "";   String TL_Emirate= "";   String PL_Wi_Name= "";   String CRN = "";   String CIF_ID= "";   String CUSTOMERNAME= "";   String Employer_Name = "";   String EmirateVisa= "";   String OfficeNo= "";   String LOS = "";   String Nationality= "";   String yearsINUAE= "";   String DSA_Name = "";   String RM_Name= "";   String designation= "";   String userName = "";   String remarks= "";   String DecisionFPU= "";   String subfeedback = "";   String dateLastChanged= "";   String DecisionCAD2= "";   String DecisionCPV = "";   

						String query="";

						 //query="select Product,(select TL_Number from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),(select TL_Emirate from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),PL_Wi_Name,CRN,CIF_ID,CUSTOMERNAME,Employer_Name,(select EmirateVisa from ng_RLOS_Customer where wi_name=PL_Wi_Name),(select OfficeNo from ng_RLOS_AltContactDetails where wi_name=PL_Wi_Name),(select LOS from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),(select Nationality from ng_RLOS_Customer where wi_name=PL_Wi_Name),(select yearsINUAE from ng_RLOS_Customer where wi_name=PL_Wi_Name),DSA_Name,RM_Name,(select designation from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),(select top 1 userName from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),(select top 1 remarks from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),(select top 1 Decision from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),(select subfeedback from ng_rlos_decisionHistory where wi_name=PL_Wi_Name),(select top 1 dateLastChanged from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),(select top 1 Decision from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='Cad_Analyst2' order by dateLastChanged),(select top 1 Decision from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='CPV' order by dateLastChanged),'"+formObject.getWFWorkitemName()+"'  from NG_PL_EXTTABLE where PL_Wi_Name in (select wi_name from ng_RLOS_EmpDetails where TL_Number='"+formObject.getNGValue("EmploymentVerification_s2_comp_tl_no")+"' )";	
						 query="select Product,(select TL_Number from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),isnull((select TL_Emirate from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),''),PL_Wi_Name,isnull((select AgreementID from ng_rlos_LoanDisbursal where wi_name=PL_Wi_Name),''),isnull(CIF_ID,''),CUSTOMERNAME,Employer_Name,isnull((select case when EmirateVisa='--Select--' then '' else EmirateVisa end  from ng_RLOS_Customer where wi_name=PL_Wi_Name),''),isnull((select OfficeNo from ng_RLOS_AltContactDetails where wi_name=PL_Wi_Name),''),isnull((select LOS from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),''),isnull((select description from ng_master_country where code=(select Nationality from ng_RLOS_Customer where wi_name=PL_Wi_Name)),''),isnull((select yearsINUAE from ng_RLOS_Customer where wi_name=PL_Wi_Name),''),isnull(DSA_Name,''),isnull(RM_Name,''),isnull((select description from ng_master_designation where code= (select desigstatus from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name)),''),isnull((select top 1 isnull(userName,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select top 1 isnull(remarks,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select isnull(subfeedback,'') from ng_rlos_decisionHistory where wi_name=PL_Wi_Name),''),isnull((select convert(varchar,dateLastChanged,120)  from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='Cad_Analyst2' order by dateLastChanged),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='CPV' order by dateLastChanged),''),'"+formObject.getWFWorkitemName()+"'  from NG_PL_EXTTABLE where PL_Wi_Name in (select wi_name from ng_RLOS_EmpDetails where TL_Number='"+formObject.getNGValue("EmploymentVerification_s2_comp_tl_no")+"' ) and pl_wi_name!='"+formObject.getWFWorkitemName()+"'";
								
								PersonalLoanS.mLogger.info("Squery for data :: "+query);
								
								List<List<String>> list = formObject.getDataFromDataSource(query);
								
								PersonalLoanS.mLogger.info("Size of List:: "+list.size());
								
							List<String> data = new ArrayList<String>();
							
						if(list.size()>0){
							for(int i =0;i<list.size();i++ ){
							 //refNo= list.get(i).get(i);
							 //aecbScore =  list.get(i).get(1);
							 //range=  list.get(i).get(2);
								
							     Product		= list.get(i).get(0);
								 TL_Number	    = list.get(i).get(1);
								 TL_Emirate		= list.get(i).get(2);
								 PL_Wi_Name		= list.get(i).get(3);
								 CRN 			= list.get(i).get(4);
								 CIF_ID			= list.get(i).get(5);
								 CUSTOMERNAME	= list.get(i).get(6);
								 Employer_Name  = list.get(i).get(7);
								 EmirateVisa	= list.get(i).get(8);
								 OfficeNo		= list.get(i).get(9);
								 LOS		    = list.get(i).get(10);
								 Nationality	= list.get(i).get(11);
								 yearsINUAE		= list.get(i).get(12);
								 DSA_Name 		= list.get(i).get(13);
								 RM_Name		= list.get(i).get(14);
								 designation	= list.get(i).get(15);
								 userName 		= list.get(i).get(16);
								 remarks		= list.get(i).get(17);
								 DecisionFPU	= list.get(i).get(18);
								 subfeedback	= list.get(i).get(19);
								 dateLastChanged= list.get(i).get(20);
								 DecisionCAD2	= list.get(i).get(21);
								 DecisionCPV 	= list.get(i).get(22);
								 if(i==0){
									
									data.add("Employer_Name"  +","+  formObject.getNGValue("EmploymentVerification_s2_comp_emp_name") );
									data.add("TL_Number"  +","+  formObject.getNGValue("EmploymentVerification_s2_comp_tl_no") );
									data.add("TL_Emirate"  +","+ formObject.getNGValue("EmploymetlntVerification_s2_comp_emirate")  );
									data.add("");
									data.add("");
									data.add("Type of Product"  +","+   "TL Number"  +","+   "TL emirate"  +","+   "WI number"  +","+   "Agreement Number/CRN"  +","+   "CIF"  +","+   "Customer Name"  +","+   "Employer Name"  +","+   "Company/Visa Emirate"  +","+   "Office Telephone"  +","+   "LOS"  +","+   "Nationality"  +","+   "Years in UAE"  +","+   "RO name"  +","+   "RM Name"  +","+   "Designation"  +","+   "FPU analyst Name"  +","+   "FPU Remarks"  +","+   "FPU Decision"  +","+   "FPU  Sub feedback"  +","+   "FPU Decision Date"  +","+   "CAD decision"  +","+   "CPV  Decision");								
								
								 }
									data.add(Product  +","+   TL_Number  +","+    TL_Emirate  +","+   PL_Wi_Name  +","+   CRN  +","+   CIF_ID  +","+   CUSTOMERNAME  +","+   Employer_Name  +","+    EmirateVisa  +","+   OfficeNo  +","+   LOS  +","+   Nationality  +","+   yearsINUAE  +","+   DSA_Name  +","+    RM_Name  +","+   designation  +","+   userName  +","+    remarks  +","+   DecisionFPU  +","+   subfeedback  +","+   dateLastChanged  +","+   DecisionCAD2  +","+   DecisionCPV);
							}
						}
						
						String OutPutFileName = "";
					  
						String logFilePath = System.getProperty("user.dir") + File.separatorChar + "FPU_Reports" + File.separatorChar +formObject.getWFWorkitemName();
						OutPutFileName = logFilePath + File.separatorChar + "COMPANY_MISMATCH_PL" +".csv";
						String FileName = "COMPANY_MISMATCH_PL.csv";	
						writeToFile(data,OutPutFileName,FileName);
						PersonalLoanS.mLogger.info("File Created");
						
						}
						catch(Exception ex)
						{
							PersonalLoanS.mLogger.info("error in attach : "+ex.getMessage());
							printException(ex);
						}
				}
			 else if ("EmploymentVerification_approved_report".equalsIgnoreCase(pEvent.getSource().getName()) ) {
					try
					{

						String Time_Frame = "";String Amount_disbursed_Approved_limit = "" ; String Disbursal_Approval_date = "" ; String Total_number_of_active_loans_from_Co = "" ; String Total_number_of_active_CCs_from_Co = "" ; String Product= "";   String TL_Number = "";   String TL_Emirate= "";   String PL_Wi_Name= "";   String CRN = "";   String CIF_ID= "";   String CUSTOMERNAME= "";   String Employer_Name = "";   String EmirateVisa= "";   String OfficeNo= "";   String LOS = "";   String Nationality= "";   String yearsINUAE= "";   String DSA_Name = "";   String RM_Name= "";   String designation= "";   String userName = "";   String remarks= "";   String DecisionFPU= "";   String subfeedback = "";   String dateLastChanged= "";   String DisbursalDate= "";   String DisbursalAmount = "";   
						Total_number_of_active_loans_from_Co = formObject.getNGValue("cmplx_emp_ver_sp2_num_active_loan");
						Total_number_of_active_CCs_from_Co = formObject.getNGValue("cmplx_emp_ver_sp2_num_active_CC");				//courier_auto change by jahnavi
							//int appCount = formObject.getLVWRowCount("cmplx_emp_ver_sp2_cmplx_loan_disb");
						//String query1="select  Product, case when DATEDIFF(MONTH,( (select top 1 dateLastChanged from NG_RLOS_GR_DECISION with(nolock) where dec_wi_name=PL_Wi_Name order by dateLastChanged)),GETDATE())< 6 then '0-6 month' when DATEDIFF(MONTH,( (select top 1 dateLastChanged from NG_RLOS_GR_DECISION with(nolock) where dec_wi_name=PL_Wi_Name order by dateLastChanged)),GETDATE())<12 then'6-12 month' else '12-24 month' end  as month_diff,PL_Wi_Name,isnull((select AgreementID from ng_rlos_LoanDisbursal where wi_name=PL_Wi_Name),''),CIF_ID,CUSTOMERNAME,Employer_Name,isnull((select EmirateVisa from ng_RLOS_Customer where wi_name=PL_Wi_Name),''),isnull((select TL_Number from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),''),(select OfficeNo from ng_RLOS_AltContactDetails where wi_name=PL_Wi_Name),(select LOS from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),(select Nationality from ng_RLOS_Customer where wi_name=PL_Wi_Name),(select yearsINUAE from ng_RLOS_Customer where wi_name=PL_Wi_Name),DSA_Name,RM_Name,(select isnull(desigstatus,'') from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),isnull((select top 1 isnull(userName,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select top 1 isnull(remarks,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select isnull(subfeedback,'') from ng_rlos_decisionHistory where wi_name=PL_Wi_Name),''),(select top 1 isnull(dateLastChanged,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),isnull((select top 1 isnull(dateLastChanged,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name order by dateLastChanged),'') as disb_Date,Final_Limit,'"+formObject.getWFWorkitemName()+"'  from NG_PL_EXTTABLE where CURR_WSNAME= 'Disbursed' and PL_Wi_Name in (select wi_name from ng_RLOS_EmpDetails where EmpCode='"+formObject.getNGValue("cmplx_EmploymentDetails_EMpCode")+"') and CREATED_DATE between GETDATE()-730 and GETDATE() union all select  Product, case when DATEDIFF(MONTH,( (select top 1 isnull(dateLastChanged,'') from NG_RLOS_GR_DECISION with(nolock) where dec_wi_name=CC_Wi_Name order by dateLastChanged)),GETDATE())< 6 then '0-6 month' when DATEDIFF(MONTH,( (select top 1 isnull(dateLastChanged,'') from NG_RLOS_GR_DECISION with(nolock) where dec_wi_name=cc_Wi_name order by dateLastChanged)),GETDATE())<12 then'6-12 month' else '12-24 month' end  as month_diff,CC_Wi_Name,isnull(CRN,''),CIF_ID,CUSTOMERNAME,Employer_Name,isnull((select EmirateVisa from ng_RLOS_Customer where wi_name=cc_Wi_name),''),isnull((select TL_Number from ng_RLOS_EmpDetails where wi_name=cc_Wi_name),''),(select OfficeNo from ng_RLOS_AltContactDetails where wi_name=cc_Wi_name),(select LOS from ng_RLOS_EmpDetails where wi_name=cc_Wi_name),(select Nationality from ng_RLOS_Customer where wi_name=cc_Wi_name),isnull((select yearsINUAE from ng_RLOS_Customer where wi_name=cc_Wi_name),''),DSA_Name,RM_Name,(select isnull(desigstatus,'') from ng_RLOS_EmpDetails where wi_name=cc_Wi_name),isnull((select top 1 isnull(userName,'') from NG_RLOS_GR_DECISION where dec_wi_name=cc_Wi_name and workstepName='FPU'),''),isnull((select top 1 isnull(remarks,'') from NG_RLOS_GR_DECISION where dec_wi_name=cc_Wi_name and workstepName='FPU'),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=cc_Wi_name and workstepName='FPU'),''),isnull((select isnull(subfeedback,'') from ng_rlos_decisionHistory where wi_name=cc_Wi_name),''),(select top 1 isnull(dateLastChanged,'') from NG_RLOS_GR_DECISION where dec_wi_name=cc_Wi_name and workstepName='FPU'),(select top 1 isnull(dateLastChanged,'') from NG_RLOS_GR_DECISION where dec_wi_name=cc_Wi_name order by dateLastChanged) as disb_Date,Final_Limit,'"+formObject.getWFWorkitemName()+"'  from NG_CC_EXTTABLE where CURR_WSNAME= 'Courier_Auto' and cc_Wi_name in (select wi_name from ng_RLOS_EmpDetails where EmpCode='"+formObject.getNGValue("cmplx_EmploymentDetails_EMpCode")+"') and CREATED_DATE between GETDATE()-730 and GETDATE()";

			String query1="select  Product, case when DATEDIFF(MONTH,( (select top 1  convert(varchar,dateLastChanged,120)  from NG_RLOS_GR_DECISION with(nolock) where dec_wi_name=PL_Wi_Name order by dateLastChanged desc)),GETDATE())< 6 then '0-6 month' when DATEDIFF(MONTH,( (select top 1  convert(varchar,dateLastChanged,120)  from NG_RLOS_GR_DECISION with(nolock) where dec_wi_name=PL_Wi_Name order by dateLastChanged desc)),GETDATE())<12 then'6-12 month' else '12-24 month' end  as month_diff,PL_Wi_Name,isnull((select AgreementID from ng_rlos_LoanDisbursal where wi_name=PL_Wi_Name),''),isnull(CIF_ID,''),CUSTOMERNAME,(select employer_name from ng_RLOS_empdetails where wi_name=PL_Wi_Name),isnull((select case when EmirateVisa='--Select--' then '' else EmirateVisa end  from ng_RLOS_Customer where wi_name=PL_Wi_Name),''),isnull((select TL_Number from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),''),(select OfficeNo from ng_RLOS_AltContactDetails where wi_name=PL_Wi_Name),isnull((select LOS from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),''),isnull((select top 1 description from ng_master_country where code=( select Nationality from ng_RLOS_Customer where wi_name=PL_Wi_Name)),''),isnull((select yearsINUAE from ng_RLOS_Customer where wi_name=PL_Wi_Name),''),isnull(DSA_Name,''),isnull(RM_Name,''),(select case when  (select top 1 description from ng_master_designation where code=(select desigstatus from ng_rlos_empdetails where wi_name=PL_Wi_Name)) is not null then (select top 1 description from ng_master_designation where code=(select desigstatus from ng_rlos_empdetails where wi_name = PL_Wi_Name))  else (select desigstatus from ng_rlos_Empdetails where wi_name = PL_Wi_Name) end),isnull((select top 1 isnull(userName,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select top 1 isnull(remarks,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select top 1 isnull(subfeedback,'') from ng_rlos_decisionHistory where wi_name=PL_Wi_Name),''),isnull((select top 1 convert(varchar,dateLastChanged,120)  from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select top 1  convert(varchar,dateLastChanged,120)  from NG_RLOS_GR_DECISION where workstepname='Disbursal_Checker' and decision in ('Approve and Exit','Approve and Post Disbursal','Approve and Takeover') and dec_wi_name=PL_Wi_Name order by dateLastChanged desc),'') as disb_Date,Final_Limit,'"+formObject.getWFWorkitemName()+"'  from NG_PL_EXTTABLE where CURR_WSNAME= 'Disbursed' and PL_Wi_Name in (select wi_name from ng_RLOS_EmpDetails where employer_name='"+formObject.getNGValue("EmploymentVerification_s2_loan_emp_name")+"') and CREATED_DATE between GETDATE()-730 and GETDATE()  union all select  Product, case when DATEDIFF(MONTH,( (select top 1  convert(varchar,dateLastChanged,120)  from NG_RLOS_GR_DECISION with(nolock) where workstepname='Disbursal' and decision ='Submit' and dec_wi_name=CC_Wi_Name order by dateLastChanged desc)),GETDATE())< 6 then '0-6 month' when DATEDIFF(MONTH,( (select top 1  convert(varchar,dateLastChanged,120)  from NG_RLOS_GR_DECISION with(nolock) where dec_wi_name=cc_Wi_name order by dateLastChanged desc)),GETDATE())<12 then'6-12 month' else '12-24 month' end  as month_diff,CC_Wi_Name,isnull(CRN,''),isnull(CIF_ID,''),CUSTOMERNAME,(select Employer_Name from ng_RLOS_EmpDetails where  wi_name=CC_Wi_Name),isnull((select case when EmirateVisa='--Select--' then '' else EmirateVisa end  from ng_RLOS_Customer where wi_name=cc_Wi_name),''),isnull((select TL_Number from ng_RLOS_EmpDetails where wi_name=cc_Wi_name),''),isnull((select OfficeNo from ng_RLOS_AltContactDetails where wi_name=cc_Wi_name),''),isnull((select LOS from ng_RLOS_EmpDetails where wi_name=cc_Wi_name),''),isnull((select top 1 description from ng_master_country where code=(select Nationality from ng_RLOS_Customer where wi_name=cc_Wi_name)),''),isnull((select yearsINUAE from ng_RLOS_Customer where wi_name=cc_Wi_name),''),isnull(DSA_Name,''),isnull(RM_Name,''),(select case when  (select top 1 description from ng_master_designation where code=(select desigstatus from ng_rlos_empdetails where wi_name=CC_Wi_Name)) is not null then (select top 1 description from ng_master_designation where code=(select desigstatus from ng_rlos_empdetails where wi_name = CC_Wi_Name))  else (select desigstatus from ng_rlos_Empdetails where wi_name = CC_Wi_Name) end),isnull((select top 1 isnull(userName,'') from NG_RLOS_GR_DECISION where dec_wi_name=cc_Wi_name and workstepName='FPU'),''),isnull((select top 1 isnull(remarks,'') from NG_RLOS_GR_DECISION where dec_wi_name=cc_Wi_name and workstepName='FPU'),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=cc_Wi_name and workstepName='FPU'),''),isnull((select top 1 isnull(subfeedback,'') from ng_rlos_decisionHistory where wi_name=cc_Wi_name),''),isnull((select  top 1  convert(varchar,dateLastChanged,120) from NG_RLOS_GR_DECISION where dec_wi_name=cc_Wi_name and workstepName='FPU'),''),isnull((select top 1  convert(varchar,dateLastChanged,120)  from NG_RLOS_GR_DECISION where dec_wi_name=cc_Wi_name order by dateLastChanged desc),'') as disb_Date,Final_Limit,'"+formObject.getWFWorkitemName()+"'  from NG_CC_EXTTABLE where CURR_WSNAME= 'Courier_Auto' and Employer_name='"+formObject.getNGValue("EmploymentVerification_s2_loan_emp_name")+"' and employmenttype='Salaried' and cc_Wi_name in (select wi_name from ng_RLOS_EmpDetails where Employer_name='"+formObject.getNGValue("EmploymentVerification_s2_loan_emp_name")+"') and CREATED_DATE between GETDATE()-730 and GETDATE()  union all select  Product, case when DATEDIFF(MONTH,( (select top 1  convert(varchar,dateLastChanged,120)  from NG_RLOS_GR_DECISION with(nolock) where workstepname='Disbursal' and decision ='Submit' and dec_wi_name=CC_Wi_Name order by dateLastChanged desc)),GETDATE())< 6 then '0-6 month' when DATEDIFF(MONTH,( (select top 1 isnull(dateLastChanged,'') from NG_RLOS_GR_DECISION with(nolock) where dec_wi_name=cc_Wi_name order by dateLastChanged desc)),GETDATE())<12 then'6-12 month' else '12-24 month' end  as month_diff,CC_Wi_Name,isnull(CRN,''),isnull(CIF_ID,''),CUSTOMERNAME,Employer_Name,isnull((select case when EmirateVisa='--Select--' then '' else EmirateVisa end  from ng_RLOS_Customer where wi_name=cc_Wi_name),''),isnull((select top 1 tradeliceneceNo from NG_RLOS_GR_CompanyDetails where comp_winame =cc_Wi_name and application_type='Secondary'),''),isnull((select OfficeNo from ng_RLOS_AltContactDetails where wi_name=cc_Wi_name),''),isnull((select LOS from ng_RLOS_EmpDetails where wi_name=cc_Wi_name),''),isnull((select top 1 description from ng_master_country where code=(select Nationality from ng_RLOS_Customer where wi_name=cc_Wi_name)),''),isnull((select yearsINUAE from ng_RLOS_Customer where wi_name=cc_Wi_name),''),isnull(DSA_Name,''),isnull(RM_Name,''),(select case when  (select top 1 description from ng_master_designation where code=(select desigstatus from ng_rlos_empdetails where wi_name=CC_Wi_Name)) is not null then (select top 1 description from ng_master_designation where code=(select desigstatus from ng_rlos_empdetails where wi_name = CC_Wi_Name))  else (select desigstatus from ng_rlos_Empdetails where wi_name = CC_Wi_Name) end),isnull((select top 1 isnull(userName,'') from NG_RLOS_GR_DECISION where dec_wi_name=cc_Wi_name and workstepName='FPU'),''),isnull((select top 1 isnull(remarks,'') from NG_RLOS_GR_DECISION where dec_wi_name=cc_Wi_name and workstepName='FPU'),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=cc_Wi_name and workstepName='FPU'),''),isnull((select top 1  isnull(subfeedback,'') from ng_rlos_decisionHistory where wi_name=cc_Wi_name),''),isnull((select top 1 convert(varchar,dateLastChanged,120) from NG_RLOS_GR_DECISION where dec_wi_name=cc_Wi_name and workstepName='FPU'),''),isnull((select top 1  convert(varchar,dateLastChanged,120)  from NG_RLOS_GR_DECISION where dec_wi_name=cc_Wi_name order by dateLastChanged desc),'') as disb_Date,Final_Limit,'"+formObject.getWFWorkitemName()+"'  from NG_CC_EXTTABLE where CURR_WSNAME= 'Courier_Auto' and Employer_name='"+formObject.getNGValue("EmploymentVerification_s2_loan_emp_name")+"' and employmenttype!='Salaried' and  cc_Wi_name in (select wi_name from ng_RLOS_EmpDetails ) and CREATED_DATE between GETDATE()-730 and GETDATE() order by disb_date desc";
						PersonalLoanS.mLogger.info( "Data to be added in Grid account Grid query 1: "+query1);
						
						List<List<String>> list_acc=formObject.getDataFromDataSource(query1);
						int appCount = list_acc.size();
							List<String> data_loandisb = new ArrayList<String>();
							if(appCount>0){
								for(int i=0;i<appCount;i++){
									Product		= list_acc.get(i).get(0);
									Time_Frame	    = list_acc.get(i).get(1);
									PL_Wi_Name		= list_acc.get(i).get(2);
									CRN 			= list_acc.get(i).get(3).replaceAll(",", ";");
									CIF_ID			= list_acc.get(i).get(4);
									CUSTOMERNAME	= list_acc.get(i).get(5);
									Employer_Name  = list_acc.get(i).get(6);
									EmirateVisa	= list_acc.get(i).get(7);
									TL_Number	    = list_acc.get(i).get(8);
									OfficeNo		= list_acc.get(i).get(9);
									LOS		    = list_acc.get(i).get(10);
									Nationality	= list_acc.get(i).get(11);
									yearsINUAE		= list_acc.get(i).get(12);
									DSA_Name 		= list_acc.get(i).get(13);
									RM_Name		= list_acc.get(i).get(14);
									designation	= list_acc.get(i).get(15);
									userName 		= list_acc.get(i).get(16);
									remarks		= list_acc.get(i).get(17);
									DecisionFPU	= list_acc.get(i).get(18);
									subfeedback	= list_acc.get(i).get(19);
									dateLastChanged= list_acc.get(i).get(20);
									DisbursalDate	= list_acc.get(i).get(21);
									DisbursalAmount 	= list_acc.get(i).get(22);
								 
							 if(i==0){
							data_loandisb.add("Employer_Name"  +","+  formObject.getNGValue("EmploymentVerification_s2_loan_emp_name") );
							data_loandisb.add("Total number of active loans from Co"  +","+  Total_number_of_active_loans_from_Co );
							data_loandisb.add("Total number of active CCs from Co"  +","+  Total_number_of_active_CCs_from_Co );
							data_loandisb.add("");
							data_loandisb.add("");
							data_loandisb.add("Type of Product"  +","+   "Time Frame"   +","+   "WI number"  +","+   "Agreement Number/CRN"  +","+   "CIF"  +","+   "Customer Name"  +","+   "Employer Name"  +","+   "Company/Visa Emirate"  +","+ "TL Number" +","+   "Office Telephone"  +","+   "LOS"  +","+   "Nationality"  +","+   "Years in UAE"  +","+   "RO name"  +","+   "RM Name"  +","+   "Designation"  +","+   "FPU analyst Name"  +","+   "FPU Remarks"  +","+   "FPU Decision"  +","+   "FPU  Sub feedback"  +","+   "FPU Decision Date"    +","+   "Disbursal/Approval date"  +"," + "Amount disbursed/Approved limit");

							 }						
							 data_loandisb.add(Product  +","+   Time_Frame  +","+   PL_Wi_Name  +","+   CRN  +","+   CIF_ID  +","+   CUSTOMERNAME  +","+   Employer_Name  +","+    EmirateVisa +","+ TL_Number +","+   OfficeNo  +","+   LOS  +","+   Nationality  +","+   yearsINUAE  +","+   DSA_Name  +","+    RM_Name  +","+   designation  +","+   userName  +","+    remarks  +","+   DecisionFPU  +","+   subfeedback  +","+   dateLastChanged  +","+   DisbursalDate  +","+   DisbursalAmount);
								
						}
					}
						
						String OutPutFileName = "";
						String logFilePath = System.getProperty("user.dir") + File.separatorChar + "FPU_Reports" + File.separatorChar + formObject.getWFWorkitemName();
						OutPutFileName = logFilePath + File.separatorChar + "LOAN_APPROVED_PL" +".csv";
												
			
						String FileName = "LOAN_APPROVED_PL.csv";
						//Hritik 20.6.21 PCASI 3083
						PersonalLoanS.mLogger.info("data_loandisb"+data_loandisb);
						PersonalLoanS.mLogger.info("OutPutFileName"+OutPutFileName);
						PersonalLoanS.mLogger.info("FileName"+FileName);
						writeToFile(data_loandisb,OutPutFileName,FileName);
						}
						catch(Exception ex)
						{
							printException(ex);
						}
						}

			 

		/*	 else if("Generate_FPU_Report".equalsIgnoreCase(pEvent.getSource().getName()) )

			 {
				 List<String> doctypecapturerej = new ArrayList<String>();
				 List<String> doctypecaptureloan = new ArrayList<String>();
				 List<String> doctypecapturecomp = new ArrayList<String>();
				 Map<String,List<String>> finaldoctype= new HashMap<String,List<String>>();
				 List<List<String>> alldoclist=new  ArrayList <List<String>>();
				 PersonalLoanS.mLogger.info("data"+"inside try for fpu report");
				 try
					{
					
						//int appCount = formObject.getLVWRowCount("cmplx_emp_ver_sp2_cmplx_loan_rej");
						String Rejected_date = "" ;String Time_Frame =""; String Product= "";   String TL_Number = "";   String TL_Emirate= "";   String PL_Wi_Name= "";   String CRN = "";   String CIF_ID= "";   String CUSTOMERNAME= "";   String Employer_Name = "";   String EmirateVisa= "";   String OfficeNo= "";   String LOS = "";   String Nationality= "";   String yearsINUAE= "";   String DSA_Name = "";   String RM_Name= "";   String designation= "";   String userName = "";   String remarks= "";   String DecisionFPU= "";   String subfeedback = "";   String dateLastChanged= "";   String DecisionCAD2= "";   String DecisionCPV = "";   
						//String query2="select Product,'6 Months',CC_Wi_Name,isnull(CRN,''),isnull(CIF_ID,''),CUSTOMERNAME,Employer_Name,(select EmirateVisa from ng_RLOS_Customer where wi_name=cc_Wi_name),isnull((select TL_Number from ng_RLOS_EmpDetails where wi_name=cc_Wi_name),''),(select OfficeNo from ng_RLOS_AltContactDetails where wi_name=cc_Wi_name),isnull((select LOS from ng_RLOS_EmpDetails where wi_name=cc_Wi_name),''),(select Nationality from ng_RLOS_Customer where wi_name=cc_Wi_name),(select yearsINUAE from ng_RLOS_Customer where wi_name=cc_Wi_name),DSA_Name,RM_Name,(select isnull(designation,'') from ng_RLOS_EmpDetails where wi_name=cc_Wi_name),isnull((select top 1 isnull(userName,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='FPU'),''),isnull((select top 1 isnull(remarks,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='FPU'),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='FPU'),''),(select isnull(subfeedback,'') from ng_rlos_decisionHistory where wi_name=CC_Wi_Name),(select top 1 isnull(dateLastChanged,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='FPU'),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='Cad_Analyst2' order by dateLastChanged),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='CPV' order by dateLastChanged),''),(select top 1 isnull(dateLastChanged,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name  order by dateLastChanged) as rej_date,'"+formObject.getWFWorkitemName()+"'  from NG_CC_EXTTABLE where CURR_WSNAME='Rejected_Application' and CC_Wi_Name in (select wi_name from ng_RLOS_EmpDetails where EmpCode='"+formObject.getNGValue("cmplx_EmploymentDetails_EMpCode")+"' ) and CREATED_DATE between GETDATE()-180 and GETDATE() union all select Product,'6 Months',PL_Wi_Name,isnull((select AgreementID from ng_rlos_LoanDisbursal where wi_name=PL_Wi_Name),''),CIF_ID,CUSTOMERNAME,Employer_Name,(select EmirateVisa from ng_RLOS_Customer where wi_name=PL_Wi_Name),(select TL_Number from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),(select OfficeNo from ng_RLOS_AltContactDetails where wi_name=PL_Wi_Name),(select LOS from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),(select Nationality from ng_RLOS_Customer where wi_name=PL_Wi_Name),(select yearsINUAE from ng_RLOS_Customer where wi_name=PL_Wi_Name),DSA_Name,RM_Name,(select isnull(designation,'')from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),(select top 1 isnull(userName,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),(select top 1 isnull(remarks,'')from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),(select top 1 isnull(Decision,'' )from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),(select isnull(subfeedback,'')from ng_rlos_decisionHistory where wi_name=PL_Wi_Name),(select top 1 isnull(dateLastChanged,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),(select top 1 isnull(Decision,'')from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='Cad_Analyst2' order by dateLastChanged),(select top 1 isnull(Decision,'' )from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='CPV' order by dateLastChanged),(select top 1 isnull(dateLastChanged,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name order by dateLastChanged) as rej_dec,'"+formObject.getWFWorkitemName()+"'  from NG_PL_EXTTABLE where CURR_WSNAME='Reject' and PL_Wi_Name in (select wi_name from ng_RLOS_EmpDetails where EmpCode='"+formObject.getNGValue("cmplx_EmploymentDetails_EMpCode")+"') and CREATED_DATE between GETDATE()-180 and GETDATE() ";
						String query2="select Product,'6 Months',CC_Wi_Name,isnull(CRN,''),isnull(CIF_ID,''),CUSTOMERNAME,Employer_Name,isnull((select  case when EmirateVisa='--Select--' then '' else EmirateVisa end   from ng_RLOS_Customer where wi_name=cc_Wi_name),''),isnull((select TL_Number from ng_RLOS_EmpDetails where wi_name=cc_Wi_name),''),(select OfficeNo from ng_RLOS_AltContactDetails where wi_name=cc_Wi_name),isnull((select LOS from ng_RLOS_EmpDetails where wi_name=cc_Wi_name),''),(select Nationality from ng_RLOS_Customer where wi_name=cc_Wi_name),(select yearsINUAE from ng_RLOS_Customer where wi_name=cc_Wi_name),isnull(DSA_Name,''),isnull(RM_Name,''),(select description from ng_master_designation where code =(select isnull(desigstatus,'') from ng_RLOS_EmpDetails where wi_name=cc_Wi_name)),isnull((select top 1 isnull(userName,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='FPU'),''),isnull((select top 1 isnull(remarks,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='FPU'),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='FPU'),''),isnull((select isnull(subfeedback,'') from ng_rlos_decisionHistory where wi_name=CC_Wi_Name),''),isnull(( select   convert(varchar,dateLastChanged,120)  from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName in('FPU','FPU')),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='Cad_Analyst2' order by dateLastChanged),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='CPV' order by dateLastChanged),''),(select top 1 isnull(dateLastChanged,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and decision='Reject' order by dateLastChanged) as rej_date,'"+formObject.getWFWorkitemName()+"'  from NG_CC_EXTTABLE where CURR_WSNAME='Rejected_Application' and CC_Wi_Name in (select wi_name from ng_RLOS_EmpDetails where EmpCode='"+formObject.getNGValue("cmplx_EmploymentDetails_EMpCode")+"' ) and CREATED_DATE between GETDATE()-180 and GETDATE() union all select Product,'6 Months',PL_Wi_Name,isnull(CRN,''),isnull(CIF_ID,''),CUSTOMERNAME,Employer_Name,(select case when EmirateVisa='--Select--' then '' else EmirateVisa end   from ng_RLOS_Customer where wi_name=PL_Wi_Name),isnull((select TL_Number from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),''),isnull((select OfficeNo from ng_RLOS_AltContactDetails where wi_name=PL_Wi_Name),''),isnull((select LOS from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),''),isnull((select Nationality from ng_RLOS_Customer where wi_name=PL_Wi_Name),''),isnull((select yearsINUAE from ng_RLOS_Customer where wi_name=PL_Wi_Name),''),isnull(DSA_Name,''),isnull(RM_Name,''),isnull((select description from ng_master_designation where code=(select isnull(desigstatus,'')from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name)),''),isnull((select top 1 userName from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select top 1 isnull(remarks,'')from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select top 1  Decision from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select subfeedback from ng_rlos_decisionHistory where wi_name=PL_Wi_Name),''),isnull((select  convert(varchar,dateLastChanged,120) from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName IN('FPU','FCU')),''),isnull((select top 1 Decision from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='Cad_Analyst2' order by dateLastChanged),'Reject'),isnull((select top 1 Decision from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='CPV' order by dateLastChanged),'Reject'),isnull((select top 1 dateLastChanged from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and decision='Reject' order by dateLastChanged),'') as rej_dec,'"+formObject.getWFWorkitemName()+"'  from NG_PL_EXTTABLE where CURR_WSNAME='Reject' and PL_Wi_Name in (select wi_name from ng_RLOS_EmpDetails where EmpCode='"+formObject.getNGValue("cmplx_EmploymentDetails_EMpCode")+"') and CREATED_DATE between GETDATE()-180 and GETDATE() order by rej_date desc";
				
						PersonalLoanS.mLogger.info( "Data to be added in Grid account Grid query 1: "+query2);
						doctypecapturerej.clear();
						List<List<String>> list_acc_rej=formObject.getDataFromDataSource(query2);
						List<String> data = new ArrayList<String>();
						int appCount = list_acc_rej.size();
						if(appCount>0){
							for(int i=0;i<appCount;i++){
								 Product		= list_acc_rej.get(i).get(0);
								 Time_Frame	    = list_acc_rej.get(i).get(1);
								 PL_Wi_Name		= list_acc_rej.get(i).get(2);
								 CRN 			= list_acc_rej.get(i).get(3).replaceAll(",", ";");
								 CIF_ID			= list_acc_rej.get(i).get(4);
								 CUSTOMERNAME	= list_acc_rej.get(i).get(5);
								 Employer_Name  = list_acc_rej.get(i).get(6);
								 EmirateVisa	= list_acc_rej.get(i).get(7);
								 OfficeNo		= list_acc_rej.get(i).get(9);
								 LOS		    = list_acc_rej.get(i).get(10);
								 Nationality	= list_acc_rej.get(i).get(11);
								 yearsINUAE		= list_acc_rej.get(i).get(12);
								 DSA_Name 		= list_acc_rej.get(i).get(13);
								 RM_Name		= list_acc_rej.get(i).get(14);
								 designation	= list_acc_rej.get(i).get(15);
								 userName 		= list_acc_rej.get(i).get(16);
								 remarks		= list_acc_rej.get(i).get(17);
								 DecisionFPU	= list_acc_rej.get(i).get(18);
								 subfeedback	= list_acc_rej.get(i).get(19);
								 dateLastChanged= list_acc_rej.get(i).get(20);
								 DecisionCAD2	= list_acc_rej.get(i).get(21);
								 DecisionCPV 	= list_acc_rej.get(i).get(22);
								 Rejected_date 	= list_acc_rej.get(i).get(23);
							     if(i==0){
									 data.add("Type of Product"  +","+   "Time Frame"   +","+   "WI number"  +","+   "Agreement Number/CRN"  +","+   "CIF"  +","+   "Customer Name"  +","+   "Employer Name"  +","+   "Company/Visa Emirate"  +","+   "Office Telephone"  +","+   "LOS"  +","+   "Nationality"  +","+   "Years in UAE"  +","+   "RO name"  +","+   "RM Name"  +","+   "Designation"  +","+   "FPU analyst Name"  +","+   "FPU Remarks"  +","+   "FPU Decision"  +","+   "FPU  Sub feedback"  +","+   "FPU Decision Date"  +","+   "CAD decision"  +","+   "CPV  Decision"  +"," + "Rejected date");
								 }
							     PersonalLoanS.mLogger.info("test for fpu date"+dateLastChanged+Rejected_date);
								data.add(Product  +","+   Time_Frame   +","+   PL_Wi_Name  +","+   CRN  +","+   CIF_ID  +","+   CUSTOMERNAME  +","+   Employer_Name  +","+    EmirateVisa  +","+   OfficeNo  +","+   LOS  +","+   Nationality  +","+   yearsINUAE  +","+   DSA_Name  +","+    RM_Name  +","+   designation  +","+   userName  +","+    remarks  +","+   DecisionFPU  +","+   subfeedback  +","+   dateLastChanged  +","+   DecisionCAD2  +","+   DecisionCPV+","+ Rejected_date);

							}
						}
						String logFilePath = System.getProperty("user.dir") + File.separatorChar + "FPU_Reports" + File.separatorChar + formObject.getWFWorkitemName();
						String OutPutFileName="";
						
						OutPutFileName = logFilePath + File.separatorChar + "REJECTED_LOAN_PL" +".csv";
						PersonalLoanS.mLogger.info("data"+data);
						PersonalLoanS.mLogger.info("OutPutFileName"+OutPutFileName);
						PersonalLoanS.mLogger.info("WI fetched successfully for SI");
						String FileName = "REJECTED_LOAN_PL";
						alldoclist.add(data);
						//doctypecapturerej.add(data.toString());
						doctypecapturerej.add(OutPutFileName);
						doctypecapturerej.add(FileName);
						finaldoctype.put(FileName,doctypecapturerej);
						PersonalLoanS.mLogger.info(finaldoctype+" "+"for reject"+doctypecapturerej);
						}
						catch(Exception ex)
						{
							printException(ex);
						}
try
					{

						String Time_Frame = "";String Amount_disbursed_Approved_limit = "" ; String Disbursal_Approval_date = "" ; String Total_number_of_active_loans_from_Co = "" ; String Total_number_of_active_CCs_from_Co = "" ; String Product= "";   String TL_Number = "";   String TL_Emirate= "";   String PL_Wi_Name= "";   String CRN = "";   String CIF_ID= "";   String CUSTOMERNAME= "";   String Employer_Name = "";   String EmirateVisa= "";   String OfficeNo= "";   String LOS = "";   String Nationality= "";   String yearsINUAE= "";   String DSA_Name = "";   String RM_Name= "";   String designation= "";   String userName = "";   String remarks= "";   String DecisionFPU= "";   String subfeedback = "";   String dateLastChanged= "";   String DisbursalDate= "";   String DisbursalAmount = "";   
						Total_number_of_active_loans_from_Co = formObject.getNGValue("cmplx_emp_ver_sp2_num_active_loan");
						Total_number_of_active_CCs_from_Co = formObject.getNGValue("cmplx_emp_ver_sp2_num_active_CC");				//courier_auto change by jahnavi
							//int appCount = formObject.getLVWRowCount("cmplx_emp_ver_sp2_cmplx_loan_disb");
						//String query1="select  Product, case when DATEDIFF(MONTH,( (select top 1 dateLastChanged from NG_RLOS_GR_DECISION with(nolock) where dec_wi_name=PL_Wi_Name order by dateLastChanged)),GETDATE())< 6 then '0-6 month' when DATEDIFF(MONTH,( (select top 1 dateLastChanged from NG_RLOS_GR_DECISION with(nolock) where dec_wi_name=PL_Wi_Name order by dateLastChanged)),GETDATE())<12 then'6-12 month' else '12-24 month' end  as month_diff,PL_Wi_Name,isnull((select AgreementID from ng_rlos_LoanDisbursal where wi_name=PL_Wi_Name),''),CIF_ID,CUSTOMERNAME,Employer_Name,isnull((select EmirateVisa from ng_RLOS_Customer where wi_name=PL_Wi_Name),''),isnull((select TL_Number from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),''),(select OfficeNo from ng_RLOS_AltContactDetails where wi_name=PL_Wi_Name),(select LOS from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),(select Nationality from ng_RLOS_Customer where wi_name=PL_Wi_Name),(select yearsINUAE from ng_RLOS_Customer where wi_name=PL_Wi_Name),DSA_Name,RM_Name,(select isnull(desigstatus,'') from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),isnull((select top 1 isnull(userName,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select top 1 isnull(remarks,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select isnull(subfeedback,'') from ng_rlos_decisionHistory where wi_name=PL_Wi_Name),''),(select top 1 isnull(dateLastChanged,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),isnull((select top 1 isnull(dateLastChanged,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name order by dateLastChanged),'') as disb_Date,Final_Limit,'"+formObject.getWFWorkitemName()+"'  from NG_PL_EXTTABLE where CURR_WSNAME= 'Disbursed' and PL_Wi_Name in (select wi_name from ng_RLOS_EmpDetails where EmpCode='"+formObject.getNGValue("cmplx_EmploymentDetails_EMpCode")+"') and CREATED_DATE between GETDATE()-730 and GETDATE() union all select  Product, case when DATEDIFF(MONTH,( (select top 1 isnull(dateLastChanged,'') from NG_RLOS_GR_DECISION with(nolock) where dec_wi_name=CC_Wi_Name order by dateLastChanged)),GETDATE())< 6 then '0-6 month' when DATEDIFF(MONTH,( (select top 1 isnull(dateLastChanged,'') from NG_RLOS_GR_DECISION with(nolock) where dec_wi_name=cc_Wi_name order by dateLastChanged)),GETDATE())<12 then'6-12 month' else '12-24 month' end  as month_diff,CC_Wi_Name,isnull(CRN,''),CIF_ID,CUSTOMERNAME,Employer_Name,isnull((select EmirateVisa from ng_RLOS_Customer where wi_name=cc_Wi_name),''),isnull((select TL_Number from ng_RLOS_EmpDetails where wi_name=cc_Wi_name),''),(select OfficeNo from ng_RLOS_AltContactDetails where wi_name=cc_Wi_name),(select LOS from ng_RLOS_EmpDetails where wi_name=cc_Wi_name),(select Nationality from ng_RLOS_Customer where wi_name=cc_Wi_name),isnull((select yearsINUAE from ng_RLOS_Customer where wi_name=cc_Wi_name),''),DSA_Name,RM_Name,(select isnull(desigstatus,'') from ng_RLOS_EmpDetails where wi_name=cc_Wi_name),isnull((select top 1 isnull(userName,'') from NG_RLOS_GR_DECISION where dec_wi_name=cc_Wi_name and workstepName='FPU'),''),isnull((select top 1 isnull(remarks,'') from NG_RLOS_GR_DECISION where dec_wi_name=cc_Wi_name and workstepName='FPU'),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=cc_Wi_name and workstepName='FPU'),''),isnull((select isnull(subfeedback,'') from ng_rlos_decisionHistory where wi_name=cc_Wi_name),''),(select top 1 isnull(dateLastChanged,'') from NG_RLOS_GR_DECISION where dec_wi_name=cc_Wi_name and workstepName='FPU'),(select top 1 isnull(dateLastChanged,'') from NG_RLOS_GR_DECISION where dec_wi_name=cc_Wi_name order by dateLastChanged) as disb_Date,Final_Limit,'"+formObject.getWFWorkitemName()+"'  from NG_CC_EXTTABLE where CURR_WSNAME= 'Courier_Auto' and cc_Wi_name in (select wi_name from ng_RLOS_EmpDetails where EmpCode='"+formObject.getNGValue("cmplx_EmploymentDetails_EMpCode")+"') and CREATED_DATE between GETDATE()-730 and GETDATE()";
						String query1="select  Product, case when DATEDIFF(MONTH,( (select top 1 dateLastChanged from NG_RLOS_GR_DECISION with(nolock) where dec_wi_name=PL_Wi_Name order by dateLastChanged)),GETDATE())< 6 then '0-6 month' when DATEDIFF(MONTH,( (select top 1 dateLastChanged from NG_RLOS_GR_DECISION with(nolock) where dec_wi_name=PL_Wi_Name order by dateLastChanged)),GETDATE())<12 then'6-12 month' else '12-24 month' end  as month_diff,PL_Wi_Name,isnull((select AgreementID from ng_rlos_LoanDisbursal where wi_name=PL_Wi_Name),''),isnull(CIF_ID,''),CUSTOMERNAME,Employer_Name,isnull((select case when EmirateVisa='--Select--' then '' else EmirateVisa end  from ng_RLOS_Customer where wi_name=PL_Wi_Name),''),isnull((select TL_Number from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),''),(select OfficeNo from ng_RLOS_AltContactDetails where wi_name=PL_Wi_Name),isnull((select LOS from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),''),isnull((select Nationality from ng_RLOS_Customer where wi_name=PL_Wi_Name),''),isnull((select yearsINUAE from ng_RLOS_Customer where wi_name=PL_Wi_Name),''),isnull(DSA_Name,''),isnull(RM_Name,''),isnull((select description from ng_master_designation where code=(select desigstatus from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name)),''),isnull((select top 1 isnull(userName,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select top 1 isnull(remarks,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select isnull(subfeedback,'') from ng_rlos_decisionHistory where wi_name=PL_Wi_Name),''),isnull((select convert(varchar,dateLastChanged,120)  from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select top 1 isnull(dateLastChanged,'') from NG_RLOS_GR_DECISION where workstepname='Disbursal_Checker' and decision in ('Approve and Exit','Approve and Post Disbursal','Approve and Takeover') and dec_wi_name=PL_Wi_Name order by dateLastChanged),'') as disb_Date,Final_Limit,'"+formObject.getWFWorkitemName()+"'  from NG_PL_EXTTABLE where CURR_WSNAME= 'Disbursed' and PL_Wi_Name in (select wi_name from ng_RLOS_EmpDetails where EmpCode='"+formObject.getNGValue("cmplx_EmploymentDetails_EMpCode")+"') and CREATED_DATE between GETDATE()-730 and GETDATE() union all select  Product, case when DATEDIFF(MONTH,( (select top 1 isnull(dateLastChanged,'') from NG_RLOS_GR_DECISION with(nolock) where workstepname='Disbursal' and decision ='Submit' and dec_wi_name=CC_Wi_Name order by dateLastChanged)),GETDATE())< 6 then '0-6 month' when DATEDIFF(MONTH,( (select top 1 isnull(dateLastChanged,'') from NG_RLOS_GR_DECISION with(nolock) where dec_wi_name=cc_Wi_name order by dateLastChanged)),GETDATE())<12 then'6-12 month' else '12-24 month' end  as month_diff,CC_Wi_Name,isnull(CRN,''),isnull(CIF_ID,''),CUSTOMERNAME,Employer_Name,isnull((select case when EmirateVisa='--Select--' then '' else EmirateVisa end  from ng_RLOS_Customer where wi_name=cc_Wi_name),''),isnull((select TL_Number from ng_RLOS_EmpDetails where wi_name=cc_Wi_name),''),isnull((select OfficeNo from ng_RLOS_AltContactDetails where wi_name=cc_Wi_name),''),isnull((select LOS from ng_RLOS_EmpDetails where wi_name=cc_Wi_name),''),isnull((select Nationality from ng_RLOS_Customer where wi_name=cc_Wi_name),''),isnull((select yearsINUAE from ng_RLOS_Customer where wi_name=cc_Wi_name),''),isnull(DSA_Name,''),isnull(RM_Name,''),isnull((select description from ng_master_designation where code=(select isnull(desigstatus,'') from ng_RLOS_EmpDetails where wi_name=cc_Wi_name)),''),isnull((select top 1 isnull(userName,'') from NG_RLOS_GR_DECISION where dec_wi_name=cc_Wi_name and workstepName='FPU'),''),isnull((select top 1 isnull(remarks,'') from NG_RLOS_GR_DECISION where dec_wi_name=cc_Wi_name and workstepName='FPU'),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=cc_Wi_name and workstepName='FPU'),''),isnull((select isnull(subfeedback,'') from ng_rlos_decisionHistory where wi_name=cc_Wi_name),''),isnull((select   convert(varchar,dateLastChanged,120) from NG_RLOS_GR_DECISION where dec_wi_name=cc_Wi_name and workstepName='FPU'),''),isnull((select top 1 isnull(dateLastChanged,'') from NG_RLOS_GR_DECISION where dec_wi_name=cc_Wi_name order by dateLastChanged),'') as disb_Date,Final_Limit,'"+formObject.getWFWorkitemName()+"'  from NG_CC_EXTTABLE where CURR_WSNAME= 'Courier_Auto' and cc_Wi_name in (select wi_name from ng_RLOS_EmpDetails where EmpCode='"+formObject.getNGValue("cmplx_EmploymentDetails_EMpCode")+"') and CREATED_DATE between GETDATE()-730 and GETDATE()";
						doctypecaptureloan.clear();
						PersonalLoanS.mLogger.info( "Data to be added in Grid account Grid query 1: "+query1);
						
						List<List<String>> list_acc=formObject.getDataFromDataSource(query1);
						int appCount = list_acc.size();
							List<String> data_loandisb = new ArrayList<String>();
							if(appCount>0){
								for(int i=0;i<appCount;i++){
									Product		= list_acc.get(i).get(0);
									Time_Frame	    = list_acc.get(i).get(1);
									PL_Wi_Name		= list_acc.get(i).get(2);
									CRN 			= list_acc.get(i).get(3).replaceAll(",", ";");
									CIF_ID			= list_acc.get(i).get(4);
									CUSTOMERNAME	= list_acc.get(i).get(5);
									Employer_Name  = list_acc.get(i).get(6);
									EmirateVisa	= list_acc.get(i).get(7);
									TL_Number	    = list_acc.get(i).get(8);
									OfficeNo		= list_acc.get(i).get(9);
									LOS		    = list_acc.get(i).get(10);
									Nationality	= list_acc.get(i).get(11);
									yearsINUAE		= list_acc.get(i).get(12);
									DSA_Name 		= list_acc.get(i).get(13);
									RM_Name		= list_acc.get(i).get(14);
									designation	= list_acc.get(i).get(15);
									userName 		= list_acc.get(i).get(16);
									remarks		= list_acc.get(i).get(17);
									DecisionFPU	= list_acc.get(i).get(18);
									subfeedback	= list_acc.get(i).get(19);
									dateLastChanged= list_acc.get(i).get(20);
									DisbursalDate	= list_acc.get(i).get(21);
									DisbursalAmount 	= list_acc.get(i).get(22);
								 
							 if(i==0){
							data_loandisb.add("Employer_Name"  +","+  Employer_Name );
							data_loandisb.add("Total number of active loans from Co"  +","+  Total_number_of_active_loans_from_Co );
							data_loandisb.add("Total number of active CCs from Co"  +","+  Total_number_of_active_CCs_from_Co );
							data_loandisb.add("");
							data_loandisb.add("");
							data_loandisb.add("Type of Product"  +","+   "Time Frame"   +","+   "WI number"  +","+   "Agreement Number/CRN"  +","+   "CIF"  +","+   "Customer Name"  +","+   "Employer Name"  +","+   "Company/Visa Emirate"  +","+   "Office Telephone"  +","+   "LOS"  +","+   "Nationality"  +","+   "Years in UAE"  +","+   "RO name"  +","+   "RM Name"  +","+   "Designation"  +","+   "FPU analyst Name"  +","+   "FPU Remarks"  +","+   "FPU Decision"  +","+   "FPU  Sub feedback"  +","+   "FPU Decision Date"    +","+   "Disbursal/Approval date"  +"," + "Amount disbursed/Approved limit");

							 }						
							 data_loandisb.add(Product  +","+   Time_Frame  +","+   PL_Wi_Name  +","+   CRN  +","+   CIF_ID  +","+   CUSTOMERNAME  +","+   Employer_Name  +","+    EmirateVisa  +","+   OfficeNo  +","+   LOS  +","+   Nationality  +","+   yearsINUAE  +","+   DSA_Name  +","+    RM_Name  +","+   designation  +","+   userName  +","+    remarks  +","+   DecisionFPU  +","+   subfeedback  +","+   dateLastChanged  +","+   DisbursalDate  +","+   DisbursalAmount);
								
						}
					}
						
						String OutPutFileName = "";
						String logFilePath = System.getProperty("user.dir") + File.separatorChar + "FPU_Reports" + File.separatorChar + formObject.getWFWorkitemName();
						OutPutFileName = logFilePath + File.separatorChar + "LOAN_APPROVED_PL" +".csv";
						String FileNameloan = "LOAN_APPROVED_PL";
						alldoclist.add(data_loandisb);
						//doctypecaptureloan.add(data_loandisb.toString());
						doctypecaptureloan.add(OutPutFileName);
						doctypecaptureloan.add(FileNameloan);
						finaldoctype.put(FileNameloan,doctypecaptureloan);						
						PersonalLoanS.mLogger.info(finaldoctype+" "+"for loan"+doctypecaptureloan);
						
						//Hritik 20.6.21 PCASI 3083
						PersonalLoanS.mLogger.info("data_loandisb"+data_loandisb);
						PersonalLoanS.mLogger.info("OutPutFileName"+OutPutFileName);
						PersonalLoanS.mLogger.info("FileName"+FileNameloan);
						//writeToFilefpu(data_loandisb,OutPutFileName,FileName);
						}
						catch(Exception ex)
						{
							printException(ex);
						}
						try
						{
							String Product= "";   String TL_Number = "";   String TL_Emirate= "";   String PL_Wi_Name= "";   String CRN = "";   String CIF_ID= "";   String CUSTOMERNAME= "";   String Employer_Name = "";   String EmirateVisa= "";   String OfficeNo= "";   String LOS = "";   String Nationality= "";   String yearsINUAE= "";   String DSA_Name = "";   String RM_Name= "";   String designation= "";   String userName = "";   String remarks= "";   String DecisionFPU= "";   String subfeedback = "";   String dateLastChanged= "";   String DecisionCAD2= "";   String DecisionCPV = "";   

							String query="";

							 //query="select Product,(select TL_Number from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),(select TL_Emirate from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),PL_Wi_Name,CRN,CIF_ID,CUSTOMERNAME,Employer_Name,(select EmirateVisa from ng_RLOS_Customer where wi_name=PL_Wi_Name),(select OfficeNo from ng_RLOS_AltContactDetails where wi_name=PL_Wi_Name),(select LOS from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),(select Nationality from ng_RLOS_Customer where wi_name=PL_Wi_Name),(select yearsINUAE from ng_RLOS_Customer where wi_name=PL_Wi_Name),DSA_Name,RM_Name,(select designation from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),(select top 1 userName from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),(select top 1 remarks from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),(select top 1 Decision from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),(select subfeedback from ng_rlos_decisionHistory where wi_name=PL_Wi_Name),(select top 1 dateLastChanged from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),(select top 1 Decision from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='Cad_Analyst2' order by dateLastChanged),(select top 1 Decision from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='CPV' order by dateLastChanged),'"+formObject.getWFWorkitemName()+"'  from NG_PL_EXTTABLE where PL_Wi_Name in (select wi_name from ng_RLOS_EmpDetails where TL_Number='"+formObject.getNGValue("EmploymentVerification_s2_comp_tl_no")+"' )";	
							 query="select Product,(select TL_Number from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),isnull((select TL_Emirate from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),''),PL_Wi_Name,isnull((select AgreementID from ng_rlos_LoanDisbursal where wi_name=PL_Wi_Name),''),isnull(CIF_ID,''),CUSTOMERNAME,Employer_Name,isnull((select case when EmirateVisa='--Select--' then '' else EmirateVisa end  from ng_RLOS_Customer where wi_name=PL_Wi_Name),''),isnull((select OfficeNo from ng_RLOS_AltContactDetails where wi_name=PL_Wi_Name),''),isnull((select LOS from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),''),isnull((select Nationality from ng_RLOS_Customer where wi_name=PL_Wi_Name),''),isnull((select yearsINUAE from ng_RLOS_Customer where wi_name=PL_Wi_Name),''),isnull(DSA_Name,''),isnull(RM_Name,''),isnull((select isnull(desigstatus,'') from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),''),isnull((select top 1 isnull(userName,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select top 1 isnull(remarks,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select isnull(subfeedback,'') from ng_rlos_decisionHistory where wi_name=PL_Wi_Name),''),isnull((select convert(varchar,dateLastChanged,120)  from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='Cad_Analyst2' order by dateLastChanged),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='CPV' order by dateLastChanged),''),'"+formObject.getWFWorkitemName()+"'  from NG_PL_EXTTABLE where PL_Wi_Name in (select wi_name from ng_RLOS_EmpDetails where TL_Number='"+formObject.getNGValue("EmploymentVerification_s2_comp_tl_no")+"' ) and pl_wi_name!='"+formObject.getWFWorkitemName()+"'";
									
									PersonalLoanS.mLogger.info("Squery for data :: "+query);
									
									List<List<String>> list = formObject.getDataFromDataSource(query);
									
									PersonalLoanS.mLogger.info("Size of List:: "+list.size());
									
								List<String> data = new ArrayList<String>();
								
							if(list.size()>0){
								for(int i =0;i<list.size();i++ ){
								 //refNo= list.get(i).get(i);
								 //aecbScore =  list.get(i).get(1);
								 //range=  list.get(i).get(2);
									
								     Product		= list.get(i).get(0);
									 TL_Number	    = list.get(i).get(1);
									 TL_Emirate		= list.get(i).get(2);
									 PL_Wi_Name		= list.get(i).get(3);
									 CRN 			= list.get(i).get(4);
									 CIF_ID			= list.get(i).get(5);
									 CUSTOMERNAME	= list.get(i).get(6);
									 Employer_Name  = list.get(i).get(7);
									 EmirateVisa	= list.get(i).get(8);
									 OfficeNo		= list.get(i).get(9);
									 LOS		    = list.get(i).get(10);
									 Nationality	= list.get(i).get(11);
									 yearsINUAE		= list.get(i).get(12);
									 DSA_Name 		= list.get(i).get(13);
									 RM_Name		= list.get(i).get(14);
									 designation	= list.get(i).get(15);
									 userName 		= list.get(i).get(16);
									 remarks		= list.get(i).get(17);
									 DecisionFPU	= list.get(i).get(18);
									 subfeedback	= list.get(i).get(19);
									 dateLastChanged= list.get(i).get(20);
									 DecisionCAD2	= list.get(i).get(21);
									 DecisionCPV 	= list.get(i).get(22);
									 if(i==0){
										data.add("Employer_Name"  +","+  Employer_Name );
										data.add("TL_Number"  +","+  TL_Number );
										data.add("TL_Emirate"  +","+  TL_Emirate );
										data.add("");
										data.add("");
										data.add("Type of Product"  +","+   "TL Number"  +","+   "TL emirate"  +","+   "WI number"  +","+   "Agreement Number/CRN"  +","+   "CIF"  +","+   "Customer Name"  +","+   "Employer Name"  +","+   "Company/Visa Emirate"  +","+   "Office Telephone"  +","+   "LOS"  +","+   "Nationality"  +","+   "Years in UAE"  +","+   "RO name"  +","+   "RM Name"  +","+   "Designation"  +","+   "FPU analyst Name"  +","+   "FPU Remarks"  +","+   "FPU Decision"  +","+   "FPU  Sub feedback"  +","+   "FPU Decision Date"  +","+   "CAD decision"  +","+   "CPV  Decision");								
									
									 }
										data.add(Product  +","+   TL_Number  +","+    TL_Emirate  +","+   PL_Wi_Name  +","+   CRN  +","+   CIF_ID  +","+   CUSTOMERNAME  +","+   Employer_Name  +","+    EmirateVisa  +","+   OfficeNo  +","+   LOS  +","+   Nationality  +","+   yearsINUAE  +","+   DSA_Name  +","+    RM_Name  +","+   designation  +","+   userName  +","+    remarks  +","+   DecisionFPU  +","+   subfeedback  +","+   dateLastChanged  +","+   DecisionCAD2  +","+   DecisionCPV);
								}
							}
							doctypecapturecomp.clear();
							String OutPutFileName = "";
						  
							String logFilePath = System.getProperty("user.dir") + File.separatorChar + "FPU_Reports" + File.separatorChar +formObject.getWFWorkitemName();
							OutPutFileName = logFilePath + File.separatorChar + "COMPANY_MISMATCH_PL" +".csv";
							String FileNamecomp = "COMPANY_MISMATCH_PL";
							alldoclist.add(data);
							//doctypecapturecomp.add(data.toString());
							doctypecapturecomp.add(OutPutFileName);
							doctypecapturecomp.add(FileNamecomp);
							finaldoctype.put(FileNamecomp,doctypecapturecomp);
							PersonalLoanS.mLogger.info(finaldoctype+" "+"for comp"+doctypecapturecomp);
							//writeToFilefpu(data,OutPutFileName,FileName);
							PersonalLoanS.mLogger.info("File Created");
							
							}
							catch(Exception ex)
							{
								PersonalLoanS.mLogger.info("error in attach : "+ex.getMessage());
								printException(ex);
							}
							PersonalLoanS.mLogger.info("printing finaldoctype by jahnav"+finaldoctype);
							writeToFilefpu(finaldoctype,alldoclist);

<<<<<<< .mine
			 }*/

			 

			 else if ("EmploymentVerification_s2_approved_report".equalsIgnoreCase(pEvent.getSource().getName()) ) {
					try
					{
						//int appCount = formObject.getLVWRowCount("cmplx_emp_ver_sp2_cmplx_loan_rej");
						String Rejected_date = "" ;String Time_Frame =""; String Product= "";   String TL_Number = "";   String TL_Emirate= "";   String PL_Wi_Name= "";   String CRN = "";   String CIF_ID= "";   String CUSTOMERNAME= "";   String Employer_Name = "";   String EmirateVisa= "";   String OfficeNo= "";   String LOS = "";   String Nationality= "";   String yearsINUAE= "";   String DSA_Name = "";   String RM_Name= "";   String designation= "";   String userName = "";   String remarks= "";   String DecisionFPU= "";   String subfeedback = "";   String dateLastChanged= "";   String DecisionCAD2= "";   String DecisionCPV = "";   
						//String query2="select Product,'6 Months',CC_Wi_Name,isnull(CRN,''),isnull(CIF_ID,''),CUSTOMERNAME,Employer_Name,(select EmirateVisa from ng_RLOS_Customer where wi_name=cc_Wi_name),isnull((select TL_Number from ng_RLOS_EmpDetails where wi_name=cc_Wi_name),''),(select OfficeNo from ng_RLOS_AltContactDetails where wi_name=cc_Wi_name),isnull((select LOS from ng_RLOS_EmpDetails where wi_name=cc_Wi_name),''),(select Nationality from ng_RLOS_Customer where wi_name=cc_Wi_name),(select yearsINUAE from ng_RLOS_Customer where wi_name=cc_Wi_name),DSA_Name,RM_Name,(select isnull(designation,'') from ng_RLOS_EmpDetails where wi_name=cc_Wi_name),isnull((select top 1 isnull(userName,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='FPU'),''),isnull((select top 1 isnull(remarks,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='FPU'),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='FPU'),''),(select isnull(subfeedback,'') from ng_rlos_decisionHistory where wi_name=CC_Wi_Name),(select top 1 isnull(dateLastChanged,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='FPU'),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='Cad_Analyst2' order by dateLastChanged),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='CPV' order by dateLastChanged),''),(select top 1 isnull(dateLastChanged,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name  order by dateLastChanged) as rej_date,'"+formObject.getWFWorkitemName()+"'  from NG_CC_EXTTABLE where CURR_WSNAME='Rejected_Application' and CC_Wi_Name in (select wi_name from ng_RLOS_EmpDetails where EmpCode='"+formObject.getNGValue("cmplx_EmploymentDetails_EMpCode")+"' ) and CREATED_DATE between GETDATE()-180 and GETDATE() union all select Product,'6 Months',PL_Wi_Name,isnull((select AgreementID from ng_rlos_LoanDisbursal where wi_name=PL_Wi_Name),''),CIF_ID,CUSTOMERNAME,Employer_Name,(select EmirateVisa from ng_RLOS_Customer where wi_name=PL_Wi_Name),(select TL_Number from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),(select OfficeNo from ng_RLOS_AltContactDetails where wi_name=PL_Wi_Name),(select LOS from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),(select Nationality from ng_RLOS_Customer where wi_name=PL_Wi_Name),(select yearsINUAE from ng_RLOS_Customer where wi_name=PL_Wi_Name),DSA_Name,RM_Name,(select isnull(designation,'')from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),(select top 1 isnull(userName,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),(select top 1 isnull(remarks,'')from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),(select top 1 isnull(Decision,'' )from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),(select isnull(subfeedback,'')from ng_rlos_decisionHistory where wi_name=PL_Wi_Name),(select top 1 isnull(dateLastChanged,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),(select top 1 isnull(Decision,'')from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='Cad_Analyst2' order by dateLastChanged),(select top 1 isnull(Decision,'' )from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='CPV' order by dateLastChanged),(select top 1 isnull(dateLastChanged,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name order by dateLastChanged) as rej_dec,'"+formObject.getWFWorkitemName()+"'  from NG_PL_EXTTABLE where CURR_WSNAME='Reject' and PL_Wi_Name in (select wi_name from ng_RLOS_EmpDetails where EmpCode='"+formObject.getNGValue("cmplx_EmploymentDetails_EMpCode")+"') and CREATED_DATE between GETDATE()-180 and GETDATE() ";


						String query2="select Product,'6 Months',CC_Wi_Name,isnull(CRN,''),isnull(CIF_ID,''),CUSTOMERNAME,(select Employer_Name from ng_RLOS_EmpDetails where  wi_name=CC_Wi_Name),isnull((select  case when EmirateVisa='--Select--' then '' else EmirateVisa end   from ng_RLOS_Customer where wi_name=cc_Wi_name),''),isnull((select TL_Number from ng_RLOS_EmpDetails where wi_name=cc_Wi_name),''),(select OfficeNo from ng_RLOS_AltContactDetails where wi_name=cc_Wi_name),isnull((select LOS from ng_RLOS_EmpDetails where wi_name=cc_Wi_name),''),(select description from ng_master_country where code=( select  Nationality from ng_RLOS_Customer where wi_name=cc_Wi_name)),(select yearsINUAE from ng_RLOS_Customer where wi_name=cc_Wi_name),isnull(DSA_Name,''),isnull(RM_Name,''),(select case when  (select description from ng_master_designation where code=(select desigstatus from ng_rlos_empdetails where wi_name=CC_Wi_Name)) is not null then (select description from ng_master_designation where code=(select desigstatus from ng_rlos_empdetails where wi_name = CC_Wi_Name))  else (select desigstatus from ng_rlos_Empdetails where wi_name = CC_Wi_Name) end),isnull((select top 1 isnull(userName,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='FPU'),''),isnull((select top 1 isnull(remarks,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='FPU'),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='FPU'),''),isnull((select isnull(subfeedback,'') from ng_rlos_decisionHistory where wi_name=CC_Wi_Name),''),isnull(( select   convert(varchar,dateLastChanged,120)  from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName in('FPU','FPU')),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='Cad_Analyst2' order by dateLastChanged desc),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='CPV' order by dateLastChanged desc),''),(select top 1 isnull(dateLastChanged,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and decision='Reject' order by dateLastChanged desc) as rej_date,'"+formObject.getWFWorkitemName()+"'  from NG_CC_EXTTABLE where CURR_WSNAME in('Rejected_Application')  and employmenttype='Salaried' and CC_Wi_Name in (select wi_name from ng_RLOS_EmpDetails where Employer_name='"+formObject.getNGValue("EmploymentVerification_s2_loan_emp_name")+"' ) and CREATED_DATE between GETDATE()-180 and GETDATE() union all select Product,'6 Months',PL_Wi_Name,isnull(CRN,''),isnull(CIF_ID,''),CUSTOMERNAME,(select employer_name from ng_RLOS_empdetails where wi_name=PL_Wi_Name),(select case when EmirateVisa='--Select--' then '' else EmirateVisa end   from ng_RLOS_Customer where wi_name=PL_Wi_Name),isnull((select TL_Number from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),''),isnull((select OfficeNo from ng_RLOS_AltContactDetails where wi_name=PL_Wi_Name),''),isnull((select LOS from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),''),isnull((select description from ng_master_country where code=( select Nationality from ng_RLOS_Customer where wi_name=PL_Wi_Name)),''),isnull((select yearsINUAE from ng_RLOS_Customer where wi_name=PL_Wi_Name),''),isnull(DSA_Name,''),isnull(RM_Name,''),(select case when  (select description from ng_master_designation where code=(select desigstatus from ng_rlos_empdetails where wi_name=PL_Wi_Name)) is not null then (select description from ng_master_designation where code=(select desigstatus from ng_rlos_empdetails where wi_name = PL_Wi_Name))  else (select desigstatus from ng_rlos_Empdetails where wi_name = PL_Wi_Name) end),isnull((select top 1 userName from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select top 1 isnull(remarks,'')from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select top 1  Decision from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select subfeedback from ng_rlos_decisionHistory where wi_name=PL_Wi_Name),''),isnull((select  convert(varchar,dateLastChanged,120) from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName IN('FPU','FCU')),''),isnull((select top 1 Decision from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='Cad_Analyst2' order by dateLastChanged desc),''),isnull((select top 1 Decision from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='CPV' order by dateLastChanged desc),''),isnull((select top 1 convert(varchar,dateLastChanged,120) from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and decision='Reject' order by dateLastChanged desc),'') as rej_dec,'"+formObject.getWFWorkitemName()+"'  from NG_PL_EXTTABLE where CURR_WSNAME in ('Rejected_Application') and PL_Wi_Name in (select wi_name from ng_RLOS_EmpDetails where Employer_name='"+formObject.getNGValue("EmploymentVerification_s2_loan_emp_name")+"') and CREATED_DATE between GETDATE()-180 and GETDATE() union all select Product,'6 Months',CC_Wi_Name,isnull(CRN,''),isnull(CIF_ID,''),CUSTOMERNAME,Employer_Name,isnull((select  case when EmirateVisa='--Select--' then '' else EmirateVisa end   from ng_RLOS_Customer where wi_name=cc_Wi_name),''),isnull((select top 1 tradeliceneceNo from NG_RLOS_GR_CompanyDetails where comp_winame =cc_Wi_name and application_type='Secondary'),''),(select OfficeNo from ng_RLOS_AltContactDetails where wi_name=cc_Wi_name),isnull((select LOS from ng_RLOS_EmpDetails where wi_name=cc_Wi_name),''),(select description from ng_master_country where code=( select  Nationality from ng_RLOS_Customer where wi_name=cc_Wi_name)),(select yearsINUAE from ng_RLOS_Customer where wi_name=cc_Wi_name),isnull(DSA_Name,''),isnull(RM_Name,''),(select case when  (select description from ng_master_designation where code=(select desigstatus from ng_rlos_empdetails where wi_name=CC_Wi_Name)) is not null then (select description from ng_master_designation where code=(select desigstatus from ng_rlos_empdetails where wi_name = CC_Wi_Name))  else (select desigstatus from ng_rlos_Empdetails where wi_name = CC_Wi_Name) end),isnull((select top 1 isnull(userName,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='FPU'),''),isnull((select top 1 isnull(remarks,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='FPU'),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='FPU'),''),isnull((select isnull(subfeedback,'') from ng_rlos_decisionHistory where wi_name=CC_Wi_Name),''),isnull(( select   convert(varchar,dateLastChanged,120)  from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName in('FPU','FPU')),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='Cad_Analyst2' order by dateLastChanged desc),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='CPV' order by dateLastChanged desc),''),(select top 1 convert(varchar,dateLastChanged,120) from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and decision='Reject' order by dateLastChanged desc) as rej_date,'"+formObject.getWFWorkitemName()+"'  from NG_CC_EXTTABLE where CURR_WSNAME in ('Rejected_Application') and employmenttype!='Salaried' and  Employer_name='"+formObject.getNGValue("EmploymentVerification_s2_loan_emp_name")+"' and  CC_Wi_Name in (select wi_name from ng_RLOS_EmpDetails ) and CREATED_DATE between GETDATE()-180 and GETDATE() order by rej_date desc";


				
						PersonalLoanS.mLogger.info( "Data to be added in Grid account Grid query 1: "+query2);

						List<List<String>> list_acc_rej=formObject.getDataFromDataSource(query2);
						List<String> data = new ArrayList<String>();
						int appCount = list_acc_rej.size();
						if(appCount>0){
							for(int i=0;i<appCount;i++){
								 Product		= list_acc_rej.get(i).get(0);
								 Time_Frame	    = list_acc_rej.get(i).get(1);
								 PL_Wi_Name		= list_acc_rej.get(i).get(2);
								 CRN 			= list_acc_rej.get(i).get(3).replaceAll(",", ";");
								 CIF_ID			= list_acc_rej.get(i).get(4);
								 CUSTOMERNAME	= list_acc_rej.get(i).get(5);
								 Employer_Name  = list_acc_rej.get(i).get(6);
								 EmirateVisa	= list_acc_rej.get(i).get(7);
								 TL_Number		=list_acc_rej.get(i).get(8);
								 OfficeNo		= list_acc_rej.get(i).get(9);
								 LOS		    = list_acc_rej.get(i).get(10);
								 Nationality	= list_acc_rej.get(i).get(11);
								 yearsINUAE		= list_acc_rej.get(i).get(12);
								 DSA_Name 		= list_acc_rej.get(i).get(13);
								 RM_Name		= list_acc_rej.get(i).get(14);
								 designation	= list_acc_rej.get(i).get(15);
								 userName 		= list_acc_rej.get(i).get(16);
								 remarks		= list_acc_rej.get(i).get(17);
								 DecisionFPU	= list_acc_rej.get(i).get(18);
								 subfeedback	= list_acc_rej.get(i).get(19);
								 dateLastChanged= list_acc_rej.get(i).get(20);
								 DecisionCAD2	= list_acc_rej.get(i).get(21);
								 DecisionCPV 	= list_acc_rej.get(i).get(22);
								 Rejected_date 	= list_acc_rej.get(i).get(23);
							     if(i==0){
									 data.add("Type of Product"  +","+   "Time Frame"   +","+   "WI number"  +","+   "Agreement Number/CRN"  +","+   "CIF"  +","+   "Customer Name"  +","+   "Employer Name"  +","+   "Company/Visa Emirate" +","+ "TL Number" +","+   "Office Telephone"  +","+   "LOS"  +","+   "Nationality"  +","+   "Years in UAE"  +","+   "RO name"  +","+   "RM Name"  +","+   "Designation"  +","+   "FPU analyst Name"  +","+   "FPU Remarks"  +","+   "FPU Decision"  +","+   "FPU  Sub feedback"  +","+   "FPU Decision Date"  +","+   "CAD decision"  +","+   "CPV  Decision"  +"," + "Rejected date");
								 }
							     PersonalLoanS.mLogger.info("test for fpu date"+dateLastChanged+Rejected_date);
								data.add(Product  +","+   Time_Frame   +","+   PL_Wi_Name  +","+   CRN  +","+   CIF_ID  +","+   CUSTOMERNAME  +","+   Employer_Name  +","+    EmirateVisa  +","+ TL_Number   +","+ OfficeNo +","+   LOS  +","+   Nationality  +","+   yearsINUAE  +","+   DSA_Name  +","+    RM_Name  +","+   designation  +","+   userName  +","+    remarks  +","+   DecisionFPU  +","+   subfeedback  +","+   dateLastChanged  +","+   DecisionCAD2  +","+   DecisionCPV+","+ Rejected_date);

							}
						}
						String logFilePath = System.getProperty("user.dir") + File.separatorChar + "FPU_Reports" + File.separatorChar + formObject.getWFWorkitemName();
						String OutPutFileName="";
						
						OutPutFileName = logFilePath + File.separatorChar + "REJECTED_LOAN_PL" +".csv";
						PersonalLoanS.mLogger.info("data"+data);
						PersonalLoanS.mLogger.info("OutPutFileName"+OutPutFileName);
						PersonalLoanS.mLogger.info("WI fetched successfully for SI");
						
						
						String FileName = "REJECTED_LOAN_PL.csv";
						writeToFile(data,OutPutFileName,FileName);
						}
						catch(Exception ex)
						{
							printException(ex);
						}
						}
				 


			 else if ("exceptionalCase_sp2_Button2".equalsIgnoreCase(pEvent.getSource().getName())) {
				 PersonalLoanS.mLogger.info( "new except Verification Details are saved"); 
				 Custom_fragmentSave("Exceptional_Case_Alert");
				 alert_msg=NGFUserResourceMgr_PL.getAlert("VAL106");
				 PersonalLoanS.mLogger.info( "new except Details are saved123"); 
				 throw new ValidatorException(new FacesMessage(alert_msg));
			 }//cmplx_fieldvisit_sp2_drop2
			 else if ("fieldvisit_sp2_Button1".equalsIgnoreCase(pEvent.getSource().getName())) {
				 PersonalLoanS.mLogger.info( "new field Verification Details are saved"); 
				 Custom_fragmentSave("Field_Visit_Initiated");
				 alert_msg=NGFUserResourceMgr_PL.getAlert("VAL107");
				 PersonalLoanS.mLogger.info( "new fieldVerification Details are saved123"); 
				 throw new ValidatorException(new FacesMessage(alert_msg));
			 }//EMploymentDetails_Label43
			 else if ("checklist_ver_sp2_Button1".equalsIgnoreCase(pEvent.getSource().getName())) {
				 PersonalLoanS.mLogger.info( "checkList Verification Details are saved");  //Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				 Custom_fragmentSave("CheckList");
				 alert_msg=NGFUserResourceMgr_PL.getAlert("VAL108");
				 PersonalLoanS.mLogger.info( "checkList Verification Details are saved123"); 
				 throw new ValidatorException(new FacesMessage(alert_msg));
			 }//by shweta
			 else if ("RiskRating_CalButton".equalsIgnoreCase(pEvent.getSource().getName())) {
				 outputResponse =genX.GenerateXML("RISK_SCORE_DETAILS","");
				 PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+outputResponse);
				 ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				 PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);
				 if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCode)|| "000".equalsIgnoreCase(ReturnCode)){
					 String totRiskRating = "";
					 totRiskRating =  (outputResponse.contains("<TotalRiskScore>")) ? outputResponse.substring(outputResponse.indexOf("<TotalRiskScore>")+"</TotalRiskScore>".length()-1,outputResponse.indexOf("</TotalRiskScore>")):"";
					 alert_msg=NGFUserResourceMgr_PL.getAlert("VAL520") ;
					 PersonalLoanS.mLogger.info("total risk rating "+totRiskRating);
					 formObject.setNGValue("cmplx_RiskRating_Total_riskScore",totRiskRating);
					 formObject.RaiseEvent("WFSave");
				 } else {//VAL521
					 alert_msg=NGFUserResourceMgr_PL.getAlert("VAL521");

				 }
				 throw new ValidatorException(new FacesMessage(alert_msg));
			 }
			 else if("BankingCheck_save".equalsIgnoreCase(pEvent.getSource().getName())){

				 PersonalLoanS.mLogger.info( "Banking Check Details are saved"); 
				 Custom_fragmentSave("Banking_Check");
				 alert_msg=NGFUserResourceMgr_PL.getAlert("VAL501");
				 PersonalLoanS.mLogger.info( "Banking Check Details are saved123"); 
				 throw new ValidatorException(new FacesMessage(alert_msg));
			 }
			 else if("CardDetails_Self_add".equalsIgnoreCase(pEvent.getSource().getName()))
			 {
				 /*
				 String Selected_Cards=formObject.getNGValue("cmplx_CardDetails_Selected_Card_Product");
				 if(formObject.getSelectedIndex("CardDetails_Avl_Card_Product")==-1)
				 {
					 throw new ValidatorException(new FacesMessage(NGFUserResourceMgr_CreditCard.getAlert("VAL111")));
				 }
				 else
				 {
					 String Curr_Card=formObject.getNGValue("CardDetails_Avl_Card_Product");
					 if(Selected_Cards.contains(Curr_Card))
					 {
						 throw new ValidatorException(new FacesMessage(NGFUserResourceMgr_CreditCard.getAlert("VAL112")));
					 }
					 if("".equalsIgnoreCase(Selected_Cards))
					 {
						 Selected_Cards=Curr_Card;					
					 }
					 else
					 {
						 Selected_Cards+=","+Curr_Card;					
					 }
					 Selected_Cards=Selected_Cards.replace(",,",",");
					 formObject.setNGValue("cmplx_CardDetails_Selected_Card_Product", Selected_Cards);
					 formObject.addItem("CardDetails_Sel_Card_Product", Curr_Card);
					 formObject.setSelectedIndex("CardDetails_Avl_Card_Product", -1);
					 if("DDVT_Maker".equalsIgnoreCase(formObject.getWFActivityName()))
					 {
						 Add_selfcard_to_CRNGrid(Curr_Card,formObject);
					 }
				 }
			  */}
			 else if("CardDetails_Self_remove".equalsIgnoreCase(pEvent.getSource().getName()))
			 {/*
				 String Selected_Cards=formObject.getNGValue("cmplx_CardDetails_Selected_Card_Product");
				 if(formObject.getSelectedIndex("CardDetails_Sel_Card_Product")==-1)
				 {
					 throw new ValidatorException(new FacesMessage(NGFUserResourceMgr_CreditCard.getAlert("VAL111")));
				 }
				 else
				 {
					 String Curr_Card=formObject.getNGValue("CardDetails_Sel_Card_Product");
					 try {
						 CreditCard.mLogger.info("selected card::"+Selected_Cards);
						 CreditCard.mLogger.info("Curr_Card card::"+Curr_Card);
						 formObject.removeItem("CardDetails_Sel_Card_Product", formObject.getSelectedIndex("CardDetails_Sel_Card_Product"));
						 CreditCard.mLogger.info("item removed::");
						 Selected_Cards=Selected_Cards.replace(Curr_Card, "");
						 CreditCard.mLogger.info("Selected_Cards::"+Selected_Cards);
						 Selected_Cards=Selected_Cards.replace(",,",",");
						 if(",".equalsIgnoreCase(Selected_Cards))
						 {
							 Selected_Cards="";
						 }
						 CreditCard.mLogger.info("Selected_Cards::"+Selected_Cards);
						 formObject.setNGValue("cmplx_CardDetails_Selected_Card_Product", Selected_Cards);
						 formObject.setSelectedIndex("CardDetails_Sel_Card_Product", -1);
						 if("DDVT_Maker".equalsIgnoreCase(formObject.getWFActivityName()))
						 {
							 Remove_Self_Card(Curr_Card);
						 }

					 } catch (Exception e) {
						 CreditCard.mLogger.info("Exception in remove"+e.getMessage());

					 }

				 }
			  */}


			//++ above code added by abhishek on 04/01/2018 for EFMS refresh functionality
			// Below code added by Imran @ 3/1/19 
			 else if("cmplx_FinacleCore_DDSgrid".equalsIgnoreCase(pEvent.getSource().getName()) && "CAD_Analyst1".equalsIgnoreCase(formObject.getWFActivityName()))
			 {
				 String Grid="cmplx_FinacleCore_DDSgrid";
				 int selected_index=formObject.getSelectedIndex(Grid);
				 if(selected_index>-1)
				 {
					String Source=formObject.getNGValue("cmplx_FinacleCore_DDSgrid",selected_index,15);
					if("External".equalsIgnoreCase(Source))
					{
						 formObject.setLocked("FinacleCore_Frame6", true);
					}
					else if("Internal".equalsIgnoreCase(Source))
					{
						formObject.setLocked("FinacleCore_Frame6", true);
						formObject.setLocked("FinacleCore_CheckBox1", false);
						 formObject.setLocked("FinacleCore_Button2", false);
					}
					else if("".equalsIgnoreCase(Source)) {
						 formObject.setLocked("FinacleCore_Frame6", false);
						 formObject.setLocked("FinacleCore_ReturnIn3Months", true);
						 formObject.setLocked("FinacleCore_ReturnIn6Months", true);
						 formObject.setLocked("FinacleCore_ReturnIn9Months", true);
						 formObject.setLocked("FinacleCore_ReturnIn12Months", true);
					 }
				 }
				 else if(selected_index==-1)
				 {
					 formObject.setLocked("FinacleCore_Frame6", false);
					 formObject.setLocked("FinacleCore_ReturnIn3Months", true);
					 formObject.setLocked("FinacleCore_ReturnIn6Months", true);
					 formObject.setLocked("FinacleCore_ReturnIn9Months", true);
					 formObject.setLocked("FinacleCore_ReturnIn12Months", true);
				 }
					 

			 }//added by shweta
			 else if ("Customer_FircoStatus".equalsIgnoreCase(pEvent.getSource().getName()) ) {
				 String AlertMsg="";
				 String Status=" ";
				 String NewgenStatus=" ";
				 String New_status=" "; 
			 try
				{
				String query ="select top 1 StatusBehavior,Newgen_status from NG_RLOS_FIRCO where  Workitem_no='"+formObject.getWFWorkitemName()+"' and Call_type='FIRCO' and call_valid='Y'";
				PersonalLoanS.mLogger.info("Query for eststus is: "+query);
				List<List<String>> record = formObject.getDataFromDataSource(query);
				PersonalLoanS.mLogger.info("Query data is: "+record);
			    if(record !=null && record.size()>0 && record.get(0)!=null)   //if(record !=null && record.size()>0 && record.get(0)!=null)
				{
					Status=record.get(0).get(0);
					NewgenStatus=record.get(0).get(1);
					if(("0".equalsIgnoreCase(Status)||" ".equalsIgnoreCase(Status)) || "FFF_OK".equalsIgnoreCase(Status) || "0000".equalsIgnoreCase(Status))
					{
						New_status="No Hit";
					}
					else if("2".equalsIgnoreCase(Status))
					{
						New_status="Hit";
					}
					else
					{
						New_status="Pending";
					}
					/*if(!New_status.equalsIgnoreCase(NewgenStatus))
					{
						String Squery="update NG_RLOS_FIRCO set Newgen_status='"+New_status+"' where  Workitem_no='"+formObject.getWFWorkitemName()+"' and Call_type='FIRCO' and call_valid='Y'";
						formObject.saveDataIntoDataSource(Squery);

					}*/
					formObject.setNGValue("FIRCO_Dec", New_status);
					formObject.setNGValue("FircoStatusLabel", formObject.getNGValue("FIRCO_Dec"));
					AlertMsg+=" || CallType: Primary|| Firco Status:"+New_status;
			      }
				
					if(AlertMsg.length()>0)	
						{
							AlertMsg=AlertMsg;
						}
					}
					catch(Exception ex)
					{
						PersonalLoanS.mLogger.info("Error In EFirco status" + ex.getStackTrace());
					}
					if(!AlertMsg.equalsIgnoreCase(""))
					{
						throw new ValidatorException(new FacesMessage(AlertMsg));
					} else {
						AlertMsg ="Please Refresh Firco Details";
						throw new ValidatorException(new FacesMessage(AlertMsg));
					}
			}//DecisionHistory_updcust
			 else if ("Customer_FetchFirco".equalsIgnoreCase(pEvent.getSource().getName())){	

				 String outputResponseFIRCO="";
				 String ReturnCodeFIRCO="";
				 String ReturnDescFIRCO = "";
				 String requestDate=new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
				 String processInstanceID=formObject.getWFWorkitemName();
				 ReferenceNo = getFircoReferenceNumber(formObject.getWFWorkitemName());
				 outputResponseFIRCO=  genX.GenerateXML("COMPLIANCE_CHECK","Primary");
				 PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+outputResponseFIRCO);

				 ReturnCodeFIRCO = outputResponseFIRCO.contains("<ReturnCode>") ? outputResponseFIRCO.substring(outputResponseFIRCO.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponseFIRCO.indexOf("</ReturnCode>")):"";

				 PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCodeFIRCO);
				 ReturnDescFIRCO =  outputResponseFIRCO.contains("<ReturnDesc>") ? outputResponseFIRCO.substring(outputResponseFIRCO.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponseFIRCO.indexOf("</ReturnDesc>")):"";    
				 PersonalLoanS.mLogger.info("RLOS value of ReturnDesc"+ReturnDescFIRCO);
				 String SystemID =  outputResponseFIRCO.contains("<SystemID>") ? outputResponseFIRCO.substring(outputResponseFIRCO.indexOf("<SystemID>")+"</SystemID>".length()-1,outputResponseFIRCO.indexOf("</SystemID>")):"";    
				 PersonalLoanS.mLogger.info("RLOS value of ContractID"+SystemID);					

				 //String FilterationDate =  outputResponseFIRCO.contains("<FilterationDate>") ? outputResponseFIRCO.substring(outputResponseFIRCO.indexOf("<FilterationDate>")+"</FilterationDate>".length()-1,outputResponseFIRCO.indexOf("</FilterationDate>")):"";    
				 String StatusBehavior =  outputResponseFIRCO.contains("<StatusBehavior>") ? outputResponseFIRCO.substring(outputResponseFIRCO.indexOf("<StatusBehavior>")+"</StatusBehavior>".length()-1,outputResponseFIRCO.indexOf("</StatusBehavior>")):"";    
				 String StatusName =  outputResponseFIRCO.contains("<StatusName>") ? outputResponseFIRCO.substring(outputResponseFIRCO.indexOf("<StatusName>")+"</StatusName>".length()-1,outputResponseFIRCO.indexOf("</StatusName>")):"";    
				 String AlertDetails =  outputResponseFIRCO.contains("<AlertDetails>") ? outputResponseFIRCO.substring(outputResponseFIRCO.indexOf("<AlertDetails>")+"</AlertDetails>".length()-1,outputResponseFIRCO.indexOf("</AlertDetails>")):"";    
				 PersonalLoanS.mLogger.info("RLOS value of ContractID"+SystemID);					
				 String rowVal = outputResponseFIRCO.substring(outputResponseFIRCO.indexOf("<AlertDetails>")+"</AlertDetails>".length()-1,outputResponseFIRCO.indexOf("</AlertDetails>"));
				 String NewgenStatus="Pending";

				 String UpdatedDateAndTime=new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());


				 PersonalLoanS.mLogger.info("RLOS value of ReturnCodeFIRCO"+ReturnCodeFIRCO);					
				 if(ReturnCodeFIRCO!=""&&  NGFUserResourceMgr_PL.getGlobalVar("PL_Firco").contains(ReturnCodeFIRCO))
				 {
					 try{
						 PersonalLoanS.mLogger.info("inside firco sucessfull");
						 //commented by Deepak to save Hit & nohit bases on AlertDetails rather than StatusBehavior
						 /*
					if (StatusBehavior.equalsIgnoreCase("1") || StatusBehavior.equalsIgnoreCase("")){	
						alert_msg=NGFUserResourceMgr_PL.getAlert("VAL119");
						NewgenStatus="Done";	
					} else if(StatusBehavior.equalsIgnoreCase("2")){
						NewgenStatus="Rejected";
					} else {
						NewgenStatus="Pending";
					}
						  */
						 Custom_fragmentSave("CustomerDetails");
						 String 	PassportNumber =formObject.getNGValue("cmplx_Customer_PAssportNo");
						 alert_msg=NGFUserResourceMgr_PL.getAlert("VAL118");
						 /*-------------by jahnavi for firco insert data----------------*/
						 String[] arrOfStr1 = null; 
						 if (rowVal.contains("Suspect detected #1"))
						 {
							 NewgenStatus="Hit";
							 
							 alert_msg=NGFUserResourceMgr_PL.getAlert("VAL118")  + " Firco Status: Hit";;
							 formObject.setNGValue("FircoStatusLabel", "Hit");
							 formObject.setNGValue("cmplx_Decision_Decision","Refer");
							 PersonalLoanS.mLogger.info("checking why not refer"+formObject.getNGValue("cmplx_Decision_Decision")); 
								
							 
							 formObject.setEnabled("cmplx_Decision_Decision",false);
							 formObject.setEnabled("cmplx_Decision_ReferTo", false);
							 formObject.setEnabled("DecisionHistory_DecisionSubReason", false);
							 formObject.setEnabled("DecisionHistory_DecisionReasonCode", false);
							 formObject.setNGValue("firco_flag", "H");
							 arrOfStr1 = rowVal.split("=============================");
							 if(arrOfStr1.length==2)
							 {
								 //LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "No Records Found : ");
								 //objRespBean.setFircosoft_Details("Record Not Found"); 
								 PersonalLoanS.mLogger.info("RLOS value of ReturnCodeFIRCO"+ReturnCodeFIRCO);
								 //return "Record Not Found";
							 }
							 else if(arrOfStr1.length>2)
							 {
								 String rlos_firco_update="update NG_RLOS_FIRCO set call_valid = 'N' where Workitem_no='"+processInstanceID+"' and Call_type='FIRCO'";
								 formObject.saveDataIntoDataSource(rlos_firco_update);
								 String firDelete="delete from ng_rlos_gr_firco_grid_dtls where wi_name='"+processInstanceID+"' and RELATEDPARTYID ='Primary'";
								 formObject.saveDataIntoDataSource(firDelete);
								 try {
									 int FIRCOSOFTGridsize = 0;
									 String colNames = "";
									 String colValues = "";
									 Map<String,String> Columnvalues = new HashMap<String,String>(); 
									 for(int j=1;j<arrOfStr1.length-1;j++)
									 {
										 String sRecords=arrOfStr1[j].replace(": \n", ":"); 
										 sRecords=sRecords.replace(":\n", ":");
										 //LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"Firco sRecords:"+sRecords);

										 BufferedReader bufReader = new BufferedReader(new StringReader(sRecords));
										 String line=null;
										 while( (line=bufReader.readLine()) != null )
										 {
											 String[] PDFColumns = {"OFAC ID", "NAME", "MATCHINGTEXT", "ORIGIN", "DESIGNATION", "DATE OF BIRTH", "USER DATA 1", "NATIONALITY", "PASSPORT", "ADDITIONAL INFOS"};
											 for(int k=0;k<PDFColumns.length;k++)
											 {
												 if(line.contains(PDFColumns[k]+":"))
												 {
													 String colData = "";
													 String [] tmp = line.split(":");
													 //iRBLSysCheckIntegrationLog.iRBLSysCheckIntegrationLogger.debug("tmp.length : "+tmp.length+", line : "+line);

													 //********below loop added for handling hardcoded Fircosoft XML in offshore dev server
													 if(tmp.length == 1)
														 colData="";//***************************
													 else if(tmp[1].trim().equalsIgnoreCase("Synonyms") || tmp[1].trim().equalsIgnoreCase("none") || tmp[1].trim().equalsIgnoreCase(""))
														 colData="";
													 else
													 {
														 //colData=tmp[1].trim();
														 for(int m=1; m<tmp.length; m++)
														 {
															 colData=colData+" "+tmp[m].trim();
														 }
													 }

													 if("DATE OF BIRTH".equalsIgnoreCase(PDFColumns[k].trim()))
													 {
														 colData=colData.trim();
														 if(colData.length()==4)
															 colData="01-01-"+colData;
														 else if(colData.length()>10)
															 colData = colData.substring(0,10);
													 }	

													 Columnvalues.put(PDFColumns[k],colData);

												 }
											 }
										 }

										 	FircoGridSRNo.add(Integer.toString(j));
											FircoGridOFACID.add(Columnvalues.get("OFAC ID"));
											FircoGridName.add(Columnvalues.get("NAME"));
											FircoGridMatchingText.add(Columnvalues.get("MATCHINGTEXT"));
											FircoGridOrigin.add(Columnvalues.get("ORIGIN"));
											FircoGridDestination.add(Columnvalues.get("DESIGNATION"));
											FircoGridDOB.add(Columnvalues.get("DATE OF BIRTH"));
											FircoGridUserData1.add(Columnvalues.get("User Data 1"));
											FircoGridNationality.add(Columnvalues.get("NATIONALITY"));
											FircoGridPassport.add(Columnvalues.get("PASSPORT"));
											FircoGridAdditionalInfo.add(Columnvalues.get("ADDITIONAL INFOS"));
											FircoGridREFERENCENO.add(ReferenceNo);
										
										 String firInsert = "Insert into ng_rlos_gr_firco_grid_dtls (WI_NAME,SRNumber,OFAC_ID,ADDITIONALINFO,NAME,"
												 + "DATEOFBIRTH,DESIGNATION,MATCHINGTEXT,ORIGIN,NATIONALITY,PASSPORT,USERDATA1,MATCH_STATUS,"
												 + "REFERENCE_NO,DETAILS_FOR,RELATEDPARTYID) values ('"+processInstanceID+"','"+FIRCOSOFTGridsize+"','"+Columnvalues.get("OFAC ID").toString().trim()+"',"
												 + "'"+Columnvalues.get("ADDITIONAL INFOS")+"','"+Columnvalues.get("NAME")+"','"+Columnvalues.get("DATE OF BIRTH")+"',"
												 + "'"+Columnvalues.get("DESIGNATION")+"','"+Columnvalues.get("MATCHINGTEXT")+"','"+Columnvalues.get("ORIGIN")+"',"
												 + "'"+Columnvalues.get("NATIONALITY")+"','"+Columnvalues.get("PASSPORT")+"','"+ Columnvalues.get("USER DATA 1")+"',"
												 + "'','"+ReferenceNo+"','"+"','Primary')";
										 PersonalLoanS.mLogger.info("rlos_firco_query : "+firInsert);
										 formObject.saveDataIntoDataSource(firInsert);
									 }
									 try{
										 String rlos_firco_query="INSERT INTO NG_RLOS_FIRCO(Process_name,Workitem_no,Firco_ID,Request_datatime,Workstep_name,Newgen_status,StatusBehavior,StatusName,AlertDetails,Updated_Datetime,Call_type,call_valid,passport) VALUES('"+formObject.getWFProcessName()+"','"+ formObject.getWFWorkitemName()+"','"+ReferenceNo+"','"+requestDate+"','"+formObject.getWFActivityName()+"','Pending','"+StatusBehavior+"','"+StatusName+"','"+AlertDetails+"','"+UpdatedDateAndTime+"',"+"'FIRCO','Y','"+Columnvalues.get("PASSPORT")+"')";
										 PersonalLoanS.mLogger.info("rlos_firco_query : "+rlos_firco_query);
										 formObject.saveDataIntoDataSource(rlos_firco_query);
										//Clearing data in Arraylist
										 	String DocName = "Fircosoft_ForIndividual.pdf";
										 	HashMap<String, String> ExtTabDataMap = GetCustomer_Details();
											String returnValue = PL_GeneratePDF.PDFTemplate(false,processInstanceID,processInstanceID,"COMPLIANCE_CHECK",DocName,ExtTabDataMap,formObject);
											
											FircoGridSRNo.clear();
											FircoGridOFACID.clear();
											FircoGridName.clear();
											FircoGridMatchingText.clear();
											FircoGridOrigin.clear();
											FircoGridDestination.clear();
											FircoGridDOB.clear();
											FircoGridUserData1.clear();
											FircoGridNationality.clear();
											FircoGridPassport.clear();
											FircoGridAdditionalInfo.clear();
											FircoGridREFERENCENO.clear();
									 }catch(Exception e){
										 e.printStackTrace();
										 PersonalLoanS.mLogger.info(" Exceptio After ng_FIRCO_InsertData: "+e.getMessage());
									 }

								 }catch(Exception e){
									 e.printStackTrace();
									 PersonalLoanS.mLogger.info(" Exceptio After ng_FIRCO_InsertData: "+e.getMessage());
								 }
							 }
						 }
						 else
						 {	NewgenStatus="No Hit";
						 formObject.setNGValue("FircoStatusLabel", "No Hit");
						
						 formObject.setNGValue("firco_flag", "NH");
						 alert_msg=NGFUserResourceMgr_PL.getAlert("VAL118") + " Firco Status: No Hit";
						 }
						 try {
							 String query="INSERT INTO NG_RLOS_FIRCO_Online_Response (Process_name,Workitem_no,Firco_ID,Request_datatime,Workstep_name,Newgen_status,StatusBehavior,StatusName,AlertDetails,Updated_Datetime,call_type,call_valid,passport)"
									 + "VALUES('"+formObject.getWFProcessName()+"','"+ formObject.getWFWorkitemName()+"','"+ReferenceNo+"','"+requestDate+"','"+formObject.getWFActivityName()+"','"+NewgenStatus+"','"+StatusBehavior+"','"+StatusName+"','"+AlertDetails+"','"+UpdatedDateAndTime+"',"+"'Primary','Y','"+PassportNumber+"')";
							 formObject.saveDataIntoDataSource(query);

							 //formObject.setNGValue("FircoStatusLabel",NewgenStatus);
							 formObject.setNGValue("FIRCO_dec",NewgenStatus);
							 List<String> objInput=new ArrayList<String> ();
							 List<Object> objOutput=new ArrayList<Object>();

							 objInput.add("Text:"+formObject.getWFWorkitemName());
							 objInput.add("Text:"+"Primary");
							 objInput.add("Text:"+PassportNumber);
							 objOutput.clear();
							 objOutput.add("Text");
							 //objOutput= formObject.getDataFromStoredProcedure("ng_FIRCO_InsertData", objInput,objOutput);
							 PersonalLoanS.mLogger.info("After ng_FIRCO_InsertData: objOutput is "+objOutput);
						 } catch (Exception e) {
							 e.printStackTrace();
							 PersonalLoanS.mLogger.info(" Exceptio After ng_FIRCO_InsertData: "+e.getMessage());
						 }
					 }
					 catch (Exception e) {
						 e.printStackTrace();
						 PersonalLoanS.mLogger.info(" Exceptio After ng_FIRCO_InsertData: "+e.getMessage());
					 }
					 //product 
					 formObject.RaiseEvent("WFSave");
					 throw new ValidatorException(new FacesMessage(alert_msg));

				 }  else {
					 NewgenStatus="Pending";
					 formObject.setNGValue("FIRCO_dec",NewgenStatus);
					 alert_msg=NGFUserResourceMgr_PL.getAlert("VAL117");
					 throw new ValidatorException(new FacesMessage(alert_msg));

				 }


			 }
			 else if ("SupplementCardDetails_Button90".equalsIgnoreCase(pEvent.getSource().getName())){
					
				  String outputResponseFIRCO="";
				  String ReturnCodeFIRCO="";
				  String ReturnDescFIRCO = "";
				  String requestDate=new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
				  outputResponseFIRCO=  genX.GenerateXML("COMPLIANCE_CHECK","SUPPLEMENT");
				  ReturnCodeFIRCO = outputResponseFIRCO.contains("<ReturnCode>") ? outputResponseFIRCO.substring(outputResponseFIRCO.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponseFIRCO.indexOf("</ReturnCode>")):"";
				PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+outputResponseFIRCO);
				ReturnDescFIRCO =  outputResponseFIRCO.contains("<ReturnDesc>") ? outputResponseFIRCO.substring(outputResponseFIRCO.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponseFIRCO.indexOf("</ReturnDesc>")):"";    
				PersonalLoanS.mLogger.info("RLOS value of ReturnDesc"+ReturnDescFIRCO);
				String SystemID =  outputResponseFIRCO.contains("<SystemID>") ? outputResponseFIRCO.substring(outputResponseFIRCO.indexOf("<SystemID>")+"</SystemID>".length()-1,outputResponseFIRCO.indexOf("</SystemID>")):"";    
				PersonalLoanS.mLogger.info("RLOS value of ContractID"+SystemID);					

				String FilterationDate =  outputResponseFIRCO.contains("<FilterationDate>") ? outputResponseFIRCO.substring(outputResponseFIRCO.indexOf("<FilterationDate>")+"</FilterationDate>".length()-1,outputResponseFIRCO.indexOf("</FilterationDate>")):"";    
				String StatusBehavior =  outputResponseFIRCO.contains("<StatusBehavior>") ? outputResponseFIRCO.substring(outputResponseFIRCO.indexOf("<StatusBehavior>")+"</StatusBehavior>".length()-1,outputResponseFIRCO.indexOf("</StatusBehavior>")):"";    
				String StatusName =  outputResponseFIRCO.contains("<StatusName>") ? outputResponseFIRCO.substring(outputResponseFIRCO.indexOf("<StatusName>")+"</StatusName>".length()-1,outputResponseFIRCO.indexOf("</StatusName>")):"";    
				String AlertDetails =  outputResponseFIRCO.contains("<AlertDetails>") ? outputResponseFIRCO.substring(outputResponseFIRCO.indexOf("<AlertDetails>")+"</AlertDetails>".length()-1,outputResponseFIRCO.indexOf("</AlertDetails>")):"";    
				String NewgenStatus="Pending";
				PersonalLoanS.mLogger.info("RLOS value of ContractID"+AlertDetails);					

				String UpdatedDateAndTime=new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
				
				String Passport =formObject.getNGValue("SupplementCardDetails_passportNo");
				/*String query1="INSERT INTO NG_RLOS_FIRCO_Online_Response (workitem_no,Firco_ID,Request_datatime,Workstep_name,StatusBehavior,StatusName,AlertDetails,Updated_Datetime,Call_type)"
					+ "VALUES('"+ formObject.getWFWorkitemName()+"','"+SystemID+"','"+requestDate+"','"+formObject.getWFActivityName()+"','"+StatusBehavior+"','"+StatusName+"','"+AlertDetails+"','"+UpdatedDateAndTime+"','SUPPLEMENT')";
				formObject.saveDataIntoDataSource(query1);*/
				PersonalLoanS.mLogger.info("RLOS value of ReturnCodeFIRCO"+ReturnCodeFIRCO);					

				if(ReturnCodeFIRCO!="" &&   NGFUserResourceMgr_PL.getGlobalVar("PL_Firco").contains(ReturnCodeFIRCO))
				{
					alert_msg=NGFUserResourceMgr_PL.getAlert("VAL118");
					if (StatusBehavior.equalsIgnoreCase("1") || StatusBehavior.equalsIgnoreCase("")){
						alert_msg=NGFUserResourceMgr_PL.getAlert("VAL119");
						NewgenStatus="Done";	
					} else if(StatusBehavior.equalsIgnoreCase("2")){
						NewgenStatus="Rejected";
					} else {
						NewgenStatus="Pending";
					}
					try {
					String query="INSERT INTO NG_RLOS_FIRCO_Online_Response (Process_name,Workitem_no,Firco_ID,Request_datatime,Workstep_name,Newgen_status,StatusBehavior,StatusName,AlertDetails,Updated_Datetime,call_type,call_valid,passport)"
						+ "VALUES('"+formObject.getWFProcessName()+"','"+ formObject.getWFWorkitemName()+"','"+SystemID+"','"+requestDate+"','"+formObject.getWFActivityName()+"','"+NewgenStatus+"','"+StatusBehavior+"','"+StatusName+"','"+AlertDetails+"','"+UpdatedDateAndTime+"',"+"'SUPPLEMENT','Y','"+Passport+"')";
					formObject.saveDataIntoDataSource(query);
					PersonalLoanS.mLogger.info("RLOS value of InsertQuery "+query);	
					
					List<String> objInput=new ArrayList<String> ();
					List<Object> objOutput=new ArrayList<Object>();
					
					objInput.add("Text:"+formObject.getWFWorkitemName());
					objInput.add("Text:"+"SUPPLEMENT");
					objInput.add("Text:"+Passport);
					objOutput.clear();
					objOutput.add("Text");
					objOutput= formObject.getDataFromStoredProcedure("ng_FIRCO_InsertData", objInput,objOutput);
					PersonalLoanS.mLogger.info("After ng_FIRCO_InsertData: objOutput is "+objOutput);
					} catch (Exception e) {
						e.printStackTrace();
						PersonalLoanS.mLogger.info(" Exceptio After ng_FIRCO_InsertData: "+e.getMessage());

					}
	
					throw new ValidatorException(new FacesMessage(alert_msg));
		
				} else {
					alert_msg=NGFUserResourceMgr_PL.getAlert("VAL117");
					throw new ValidatorException(new FacesMessage(alert_msg));

				}
				  
			 

			 }
				else if ("FetchWorldCheck_SE".equalsIgnoreCase(pEvent.getSource().getName()) ||  "WorldCheck_fetch".equalsIgnoreCase(pEvent.getSource().getName())) {
					String outputResponseFIRCO="";
					String ReturnCodeFIRCO="";
					String ReturnDescFIRCO = "";
					String requestDate=new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
					  
					outputResponseFIRCO=  genX.GenerateXML("COMPLIANCE_CHECK","");
					PersonalLoanS.mLogger.info("FIRCO Call Outputs @@@@-" +outputResponseFIRCO);
					outputResponseFIRCO="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"  encoding=\"utf-8\"?> <EE_EAI_MESSAGE> <EE_EAI_HEADER> <MsgFormat>COMPLIANCE_CHECK</MsgFormat> <MsgVersion>0001</MsgVersion> <RequestorChannelId>CAS</RequestorChannelId> <RequestorUserId>RAKUSER</RequestorUserId> <RequestorLanguage>E</RequestorLanguage> <RequestorSecurityInfo>secure</RequestorSecurityInfo> <ReturnCode>0000</ReturnCode> <ReturnDesc>Success</ReturnDesc> <MessageId>123123453</MessageId><Extra1>REP||SHELL.JOHN</Extra1> <Extra2>YYYY-MM-DDThh:mm:ss.mmm+hh:mm</Extra2> </EE_EAI_HEADER> <ComplianceCheckResponse> <SystemID>String</SystemID><FilterationDate>2018/09/11 22:17:56</FilterationDate><StatusBehavior>1</StatusBehavior><StatusName>FALSE</StatusName><AlertDetails><![CDATA[Suspect(s) detected by OFAC-Agent:3SystemId: Associate: =============================Suspect detected #1OFAC ID:AS04979205MATCH: 0.00TAG: NAMMATCHINGTEXT: MANOJ KUMAR, RESULT: (0)NAME: KUMAR, MONOJ Synonyms: noneADDRESS: Synonyms: noneCITY: https://accuity.worldcompliance.com/signin.aspx?ent=158f588d-2b86-41e8-8432-5b7663dbf90aTYS: 1ISN: 0=============================   *** INTERNAL OFAC DETAILS ***HasSndRcvInLimited: 0|AS04979205|0.00|NAM|3|13|-1|-1|-1|-1|-1|-1|-1|-1||AS08151740|0.00|NAM|3|13|-1|-1|-1|-1|-1|-1|-1|-1||AS05752051|0.00|NAM|3|13|-1|-1|-1|-1|-1|-1|-1|-1|]]></AlertDetails></ComplianceCheckResponse> </EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";

					ReturnCodeFIRCO =  outputResponseFIRCO.contains("<ReturnCode>") ? outputResponseFIRCO.substring(outputResponseFIRCO.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponseFIRCO.indexOf("</ReturnCode>")):"";
					PersonalLoanS.mLogger.info("FIRCO value of ReturnCode"+ReturnCodeFIRCO);
					ReturnDescFIRCO =  outputResponseFIRCO.contains("<ReturnDesc>") ? outputResponseFIRCO.substring(outputResponseFIRCO.indexOf("<ReturnDesc>")+"</ReturnDesc>".length()-1,outputResponseFIRCO.indexOf("</ReturnDesc>")):"";    
					PersonalLoanS.mLogger.info("FIRCO value of ReturnDesc"+ReturnDescFIRCO);
					if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCodeFIRCO) || "FFF002".equalsIgnoreCase(ReturnCodeFIRCO)){
						String SystemID =  outputResponseFIRCO.contains("<SystemID>") ? outputResponseFIRCO.substring(outputResponseFIRCO.indexOf("<SystemID>")+"</SystemID>".length()-1,outputResponseFIRCO.indexOf("</SystemID>")):"";    
						PersonalLoanS.mLogger.info("RLOS value of ContractID"+SystemID);					
						String FilterationDate =  outputResponseFIRCO.contains("<FilterationDate>") ? outputResponseFIRCO.substring(outputResponseFIRCO.indexOf("<FilterationDate>")+"</FilterationDate>".length()-1,outputResponseFIRCO.indexOf("</FilterationDate>")):"";    
						String StatusBehavior =  outputResponseFIRCO.contains("<StatusBehavior>") ? outputResponseFIRCO.substring(outputResponseFIRCO.indexOf("<StatusBehavior>")+"</StatusBehavior>".length()-1,outputResponseFIRCO.indexOf("</StatusBehavior>")):"";    
						String StatusName =  outputResponseFIRCO.contains("<StatusName>") ? outputResponseFIRCO.substring(outputResponseFIRCO.indexOf("<StatusName>")+"</StatusName>".length()-1,outputResponseFIRCO.indexOf("</StatusName>")):"";    
						String AlertDetails =  outputResponseFIRCO.contains("<AlertDetails>") ? outputResponseFIRCO.substring(outputResponseFIRCO.indexOf("<AlertDetails>")+"</AlertDetails>".length()-1,outputResponseFIRCO.indexOf("</AlertDetails>")):"";    
						String NewgenStatus="Pending";
						if (StatusBehavior.equalsIgnoreCase("4")  || StatusBehavior.equalsIgnoreCase("0")){					
							NewgenStatus="Pending";
						} else {
							NewgenStatus="Done";
						}
						//check if WI is present in table
						String updateDateAndTime=new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());

						String query1="INSERT INTO NG_RLOS_FIRCO_Online_Response (workitem_no,child_wi,Firco_ID,Request_datatime,Workstep_name,Newgen_status,StatusBehavior,StatusName,AlertDetails,Updated_Datetime) "
								+ "VALUES('"+formObject.getNGValue("Parent_WIName")+"','"+ formObject.getWFWorkitemName()+"','"+SystemID+"','"+requestDate+"','"+formObject.getWFActivityName()+"','"+NewgenStatus+"','"+StatusBehavior+"','"+StatusName+"','"+AlertDetails+"','"+updateDateAndTime+"')";
						formObject.saveDataIntoDataSource(query1);
						PersonalLoanS.mLogger.info("RLOS value of query1 "+query1);	

						//(Process_name,Firco_ID,Workstep_name,Newgen_status,StatusBehavior,StatusName,AlertDetails,Updated_Datetime,child_wi) 
						String CountQuery="Select count(Newgen_status) from  NG_RLOS_FIRCO with (nolock) where child_wi='"+formObject.getWFWorkitemName()+"'";
						List<List<String>> result = formObject.getDataFromDataSource(CountQuery);
						PersonalLoanS.mLogger.info("RLOS value of CountQuery "+CountQuery );	
						if( Integer.parseInt(result.get(0).get(0))!=0) 
						{
							String query="update NG_RLOS_FIRCO  set Process_name='"+formObject.getWFProcessName()+"', Firco_ID='"+SystemID+"',Workstep_name='"+formObject.getWFActivityName()+"',Newgen_status='"+NewgenStatus+"',StatusBehavior='"+StatusBehavior+"',StatusName='"+StatusName+"',AlertDetails='"+AlertDetails+"',Updated_Datetime='"+updateDateAndTime+"',child_wi='"+formObject.getWFWorkitemName()+"' where workitem_no='"+ formObject.getNGValue("Parent_WIName")+"'";
							formObject.saveDataIntoDataSource(query);
							PersonalLoanS.mLogger.info("RLOS value of query1 "+query);
						
						} else {
							String query="INSERT INTO NG_RLOS_FIRCO (Process_name,Workitem_no,child_wi,Firco_ID,Request_datatime,Workstep_name,Newgen_status,StatusBehavior,StatusName,AlertDetails,Updated_Datetime) "
									+ "VALUES('"+formObject.getWFProcessName()+"','"+formObject.getNGValue("Parent_WIName")+"','"+ formObject.getWFWorkitemName()+"','"+SystemID+"','"+requestDate+"','"+formObject.getWFActivityName()+"','"+NewgenStatus+"','"+StatusBehavior+"','"+StatusName+"','"+AlertDetails+"','"+updateDateAndTime+"')";
							PersonalLoanS.mLogger.info("RLOS value of InsertQuery "+query);	
							formObject.saveDataIntoDataSource(query);											
						}
						formObject.setNGValue("FIRCO_dec", NewgenStatus);
					//	formObject.setNGValue("FircoStatusLabel", NewgenStatus);

						if(StatusBehavior.equalsIgnoreCase("2")){
							formObject.setNGValue("IS_WORLD_CHECK","Y");
							alert_msg= NGFUserResourceMgr_PL.getAlert("VAL119");
						}else {
							alert_msg= NGFUserResourceMgr_PL.getAlert("VAL117");
							formObject.setNGValue("IS_WORLD_CHECK","YES");
						}
						
					} else {
						formObject.setNGValue("IS_WORLD_CHECK","N");
						alert_msg= NGFUserResourceMgr_PL.getAlert("VAL118");					
					}
					throw new ValidatorException(new FacesMessage(alert_msg));	
				}
			//  added by Nishkarsh 24/05/2021
				else if("PostDisbursal_MCQ_Sms".equalsIgnoreCase(pEvent.getSource().getName())){
					
					String email = "";

					String bankName = getBankFromTakeoverGrid(formObject);
					if("Conventional".equalsIgnoreCase(formObject.getNGValue("loan_type")))
					{
						PersonalLoanS.mLogger.info("Request is COnventional");
						email = NGFUserResourceMgr_PL.getAlert("MCQ_Email_Issuance_Conventional");
					}
				
					
					else if ("Islamic".equalsIgnoreCase(formObject.getNGValue("loan_type")))
					{
						PersonalLoanS.mLogger.info("Request is islamic");
						email = NGFUserResourceMgr_PL.getAlert("MCQ_Email_Issuance_Islamic");
					}

					PersonalLoanS.mLogger.info("email  before replacing: " + email);
					String sms = NGFUserResourceMgr_PL.getAlert("MCQ_SMS");
					email.replace("replaceBank_Name", bankName);
					email.replace("replaceWIName",formObject.getWFWorkitemName());
					PersonalLoanS.mLogger.info("Modified Email : " + email);
					PersonalLoanS.mLogger.info("SMS : " + sms);
					PersonalLoanS.mLogger.info("bankName  : " + bankName);
					insertMessageInQueue(formObject, "To_Team_MCQ", email, sms);
					
					
				}
				
				// added by Nishkarsh 24/05/2021
				else if("PostDisbursal_LC_Sms".equalsIgnoreCase(pEvent.getSource().getName())){
					
					String bankName = getBankFromTakeoverGrid(formObject);
					if("Conventional".equalsIgnoreCase(formObject.getNGValue("loan_type")) || "Islamic".equalsIgnoreCase(formObject.getNGValue("loan_type"))){
						String email = NGFUserResourceMgr_PL.getAlert("NLC_Email");
						String sms = NGFUserResourceMgr_PL.getAlert("NLC_SMS");
						email.replace("replaceBank_Name", bankName);
						email.replace("replaceWIName",formObject.getWFWorkitemName());
						insertMessageInQueue(formObject, "To_Team_NLC", email, sms);
						PersonalLoanS.mLogger.info("Modified Email : " + email);
						PersonalLoanS.mLogger.info("SMS : " + sms);
					}
				}
			
				
				else if("PostDisbursal_STL_SMS".equalsIgnoreCase(pEvent.getSource().getName())){
					
					String bankName = getBankFromTakeoverGrid(formObject);
					if("Conventional".equalsIgnoreCase(formObject.getNGValue("loan_type")) || "Islamic".equalsIgnoreCase(formObject.getNGValue("loan_type"))){
							String email = NGFUserResourceMgr_PL.getAlert("STL_Email");
							String sms = NGFUserResourceMgr_PL.getAlert("STL_SMS");
							email.replace("replaceBank_Name", bankName);
							email.replace("replaceWIName",formObject.getWFWorkitemName());
							insertMessageInQueue(formObject, "To_Team_STL", email, sms);
							PersonalLoanS.mLogger.info("Modified Email : " + email);
							PersonalLoanS.mLogger.info("SMS : " + sms);
						}
					}
			//PCASI - 2694
				else if("TLRM_Button".equalsIgnoreCase(pEvent.getSource().getName()) 
						&& ("DDVT_maker".equalsIgnoreCase(formObject.getWFActivityName()) || "Disbursal_Maker".equalsIgnoreCase(formObject.getWFActivityName()))){
					PersonalLoanS.mLogger.info("Inside change event for RM TL Name");
					String rmtlname = formObject.getNGValue("cmplx_Customer_RM_TL_NAME");
					if(rmtlname != ""){
						formObject.setNGValue("RM_Name", rmtlname);
						formObject.setNGValue("RmTlNameLabel", rmtlname);
					}
					PersonalLoanS.mLogger.info( "RM_Name :: "+formObject.getNGValue("RM_Name"));
				}
				else  if("CardDetails_Reset".equalsIgnoreCase(pEvent.getSource().getName()) ){
					String UpdateQuery ="update ng_cc_exttable set crn='',ecrn='' where cc_wi_name='"+formObject.getWFWorkitemName()+"'";
					//CreditCard.mLogger.info("query for deletion of repeater value in 3rd table is:"+UpdateQuery);
					formObject.saveDataIntoDataSource(UpdateQuery);

					String delQuery="delete from ng_rlos_gr_cardDetailsCRN where CRN_winame='"+formObject.getWFWorkitemName()+"'";
							formObject.saveDataIntoDataSource(delQuery);
					String delQuery_CCCreation="delete from NG_RLOS_gr_CCCreation where CreationGrid_winame='"+formObject.getWFWorkitemName()+"'";
							formObject.saveDataIntoDataSource(delQuery_CCCreation);
							for(int i=0;i<formObject.getLVWRowCount("ng_rlos_gr_cardDetailsCRN");i++)
							{
								formObject.setNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",0,2,"");
								formObject.setNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",0,1,"");
							}
							formObject.clear("cmplx_CardDetails_cmplx_CardCRNDetails");
							PersonalLoanS.mLogger.info("load data in crn");
							formObject.setNGValue("ECRNLabel", "");
							formObject.setNGValue("ECRN", "");
							formObject.clear("cmplx_CardDetails_cmplx_CardCRNDetails");
							loadDataInCRNGrid();
				}
				

		}



		public boolean checkValue(String ngValue){
			if(ngValue==null ||"".equalsIgnoreCase(ngValue) ||" ".equalsIgnoreCase(ngValue) || "--Select--".equalsIgnoreCase(ngValue) || "false".equalsIgnoreCase(ngValue)){
				return false;
			}
			return true;
		}

		/*          Function Header:

		 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to populate header Fields  

		 ***********************************************************************************  */


		public void setFormHeader(FormEvent pEvent){

			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			Common_Utils common=new Common_Utils(PersonalLoanS.mLogger);
			String activityName = formObject.getWFActivityName();

			try{
				PersonalLoanS.mLogger.info("Inside formPopulated()" + pEvent.getSource());
				formObject.fetchFragment("CustomerDetails", "Customer", "q_Customer");
				formObject.setNGFrameState("CustomerDetails",0);
				formObject.fetchFragment("ProductContainer", "Product", "q_Product");
				formObject.setNGFrameState("ProductContainer",0);

				if("N".equalsIgnoreCase(formObject.getNGValue("is_cc_waiver_require")) || "CSM".equalsIgnoreCase(formObject.getWFWorkitemName()))
				{
					formObject.setVisible("Card_Details", true);
				}
				if("Y".equalsIgnoreCase(formObject.getNGValue("is_cc_waiver_require"))){
					formObject.setLocked("CardDetails_Frame1",true);//added by shweta
				}
				FormConfig formobject1=FormContext.getCurrentInstance().getFormConfig();
				if("R".equalsIgnoreCase(formobject1.getConfigElement("Mode")))
	            	formObject.setNGValue("readonlycheck", "R");
				// disha FSD
				fetch_NotepadDetails();//added by akshay on 11/9/17 for fetching Notepad on form load(for validation on done click)	
				formObject.setNGValue("WiLabel",formObject.getWFWorkitemName());
				formObject.setNGValue("UserLabel",formObject.getUserName()); 
				formObject.setNGValue("FircoStatusLabel",formObject.getNGValue("FIRCO_dec"));
				//formObject.setNGValue("IntroDateLabel",formObject.getNGValue("IntroDateTime"));//Tarang to be removed on friday(1/19/2018)
				formObject.setNGValue("IntroDateLabel",formObject.getNGValue("INTRODUCTION_DATE"));
				formObject.setNGValue("CreatedDateLabel",formObject.getNGValue("CREATED_DATE"));
				if("M".equalsIgnoreCase(formObject.getNGValue("InitiationType")))
				{
					formObject.setNGValue("InitChannelLabel","Tablet"); 
				}
				else{
					formObject.setNGValue("InitChannelLabel",formObject.getNGValue("InitiationType")); 
				}

				formObject.setNGValue("CifLabel",formObject.getNGValue("cmplx_Customer_CIFNO"));//changed from ciflabel to cif_id by akshay on 26/5/18

				formObject.setNGValue("ECRNLabel",formObject.getNGValue("ECRN")); 
				//formObject.setNGValue("EFMS_Status",formObject.getNGValue("EFMS"));
				//Done by deepak
				setEfms();
				formObject.setNGValue("CurrentDateLabel",common.Convert_dateFormat("", "", "dd/MM/yyyy")); 

				String nationality = formObject.getNGValue("cmplx_Customer_Nationality");
				PersonalLoanS.mLogger.info("Nationality is: "+nationality);
				if(!"AE".equalsIgnoreCase(nationality)){
					formObject.setLocked("cmplx_Customer_marsoomID",true);
				}
				String fName = formObject.getNGValue("cmplx_Customer_FIrstNAme");
				String mName = formObject.getNGValue("cmplx_Customer_MiddleName");
				String lName = formObject.getNGValue("cmplx_Customer_LAstNAme");

				String fullName = fName+" "+mName+" "+lName; 
				formObject.setNGValue("CustomerLabel",fullName);     
				//++Below code added by  nikhil 31/10/17 as per CC FSD 2.7  hritik 6.7.21 PCASI 3543
				try
				{	//formObject.setNGValue("Final_Limit", formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
					formObject.setNGValue("LoanLabel",formObject.getNGValue("Loan_Amount"));
				}
				catch(Exception ex)
				{
					PersonalLoanS.mLogger.info(ex);
				}
				//--added by akshay on 6/12/17 for null pointer excep
				if(formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid")>0){
					String product = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0);
					formObject.setNGValue("ProductLabel",product);
					String sub_product = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2);
					formObject.setNGValue("SubProductLabel",sub_product);
					String card_product = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,4);
					formObject.setNGValue("CardLabel",card_product);
				}
				PersonalLoanS.mLogger.info("Itemindex is:" +  formObject.getWFFolderId());
				formObject.setNGValue("AppRefLabel", formObject.getNGValue("AppRefNo"));

				//  below code already exist - 10-10-2017
				//disha FSD P1 - Top section is blank partly
				formObject.setNGValue("RmTlNameLabel", formObject.getNGValue("RM_Name")); 
				formObject.setNGValue("Cmp_Emp_Label",formObject.getNGValue("Employer_Name"));
				List<String> activity_openFragmentsonLoad = Arrays.asList("CAD_Analyst1","CAD_Analyst2","CC_Disbursal","CC_Waiver","Collection_Agent_Review","Collection_User",
						"Compliance","CPV_Analyst","CPV","CSM_Review","CSM","DDVT_Checker","Deferral_Authority","Dispatch","FCU","FPU","Hold_CPV","HR","Interest_Rate_Approval"
						,"Manager","Orig_Validation","Post_Disbursal","Reject_Queue","Rejected_Application","Relationship_Manager"
						,"DSA_CSO_Review","RM_Review","Smart_CPV","Telesales_Agent_Review","ToTeam","Waiver_Authority","DDVT_maker","Credit_Analyst_Hold"
						,"Document_Checker","Disbursal_Maker","Disbursal_Checker","PostDisbursal_Maker","PostDisbursal_Checker","DDVT_Hold","CustomerHold");

				List<String> activity_setPartMatch = Arrays.asList("DDVT_maker");
				if(activity_openFragmentsonLoad.contains(activityName)){
					setActivityname(formObject,activityName);
				}
				if(activity_setPartMatch.contains(activityName)){
					setPartMatch(formObject);
				}
				/*if("".equals(formObject.getNGValue("cmplx_Customer_NEP"))||"--Select--".equals(formObject.getNGValue("cmplx_Customer_NEP"))){
				formObject.setVisible("cmplx_Customer_NEP",true);
				formObject.setVisible("Customer_Label43",true);
				formObject.setLocked("cmplx_Customer_EIDARegNo",true);
			}
			else {
				formObject.setVisible("cmplx_Customer_NEP",false);
				formObject.setVisible("Customer_Label43",false);
			}---commented by akshay on 23/1/18*/
				String sQuery_CAD_dec = "select top 1 decision from NG_RLOS_GR_DECISION with (nolock) where dec_wi_name='"+formObject.getWFWorkitemName()+"' and workstepName='CAD_Analyst1' order by dateLastChanged desc";
				//formObject.setNGValue("Label_introdate", formObject.getNGValue("Created_Date"));
				formObject.setNGValue("CAD_decision", formObject.getNGValue("CAD_ANALYST1_DECISION")); 
				List<List<String>> CAD_dec = formObject.getDataFromDataSource(sQuery_CAD_dec);
				if(!CAD_dec.isEmpty() && CAD_dec.size()>0){
					formObject.setNGValue("CAD_decision", CAD_dec.get(0).get(0)); 
				}
				formObject.setNGValue("RmTlNameLabel", formObject.getNGValue("RM_Name")); 
				String dectech_dec ="select top 1 Decision from ng_rlos_IGR_Eligibility_PersonalLoan where Child_Wi='"+formObject.getWFWorkitemName()+"'";
				List<List<String>> dectech = formObject.getDataFromDataSource(dectech_dec);
				if(!dectech.isEmpty()){
					formObject.setNGValue("dectech_label",dectech.get(0).get(0));
				}	
				String sQuery_AecbHistory = "select isnull(max(AECBHistMonthCnt),0) as AECBHistMonthCnt from ( select MAX(cast(isnull(AECBHistMonthCnt,'0') as int)) as AECBHistMonthCnt  from ng_rlos_cust_extexpo_CardDetails with (nolock) where  Child_wi  = '"+formObject.getWFWorkitemName()+"' union select Max(cast(isnull(AECBHistMonthCnt,'0') as int)) as AECBHistMonthCnt from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Child_wi  = '"+formObject.getWFWorkitemName()+"') as ext_expo";
				//formObject.setNGValue("Label_introdate", formObject.getNGValue("Created_Date"));
				List<List<String>> aecb = formObject.getDataFromDataSource(sQuery_AecbHistory);
				if(!aecb.isEmpty()){
					formObject.setNGValue("AECB_History",aecb.get(0).get(0));
				}
				//added by rishabh
				if("FPU".equalsIgnoreCase(activityName) || "FCU".equalsIgnoreCase(activityName))
				{
					formObject.setVisible("Notepad_Details1", false);
					formObject.setVisible("supervisor_section", false);
					formObject.setTop("Smart_Check1",formObject.getTop("Banking_Check")+formObject.getHeight("Banking_Check")+10);
				}
			}catch(Exception e)
			{
				printException(e);
			}
			finally
			{
				//++Below code added by  nikhil 31/10/17 as per CC FSD 2.7

				if("FPU".equalsIgnoreCase(activityName) || activityName.equalsIgnoreCase("FCU"))
				{
					formObject.setVisible("Supervisor_section", false);
					formObject.setVisible("Postdisbursal_Checklist", false);
				}
				//++below code added by yash on 15/12/2017
				else if(activityName.equalsIgnoreCase("ToTeam"))
				{
					formObject.setVisible("Post_Disbursal", true);
					PersonalLoanS.mLogger.info("In TO Team Post disbursal");
				}
				else if(activityName.equalsIgnoreCase("PostDisbursal_Maker") || activityName.equalsIgnoreCase("PostDisbursal_Checker") )
				{
					formObject.setVisible("Postdisbursal_Checklist", true);
					formObject.setVisible("Post_Disbursal", false);
					PersonalLoanS.mLogger.info("In TO Team Post disbursal");
				}
				else if(activityName.equalsIgnoreCase("Disbursal_Checker") )
				{
					formObject.setVisible("Postdisbursal_Checklist", true);
					formObject.setVisible("Post_Disbursal", false);
					PersonalLoanS.mLogger.info("disbursal checker it is");
				}
				else if(activityName.equalsIgnoreCase("ddvt_maker") || activityName.equalsIgnoreCase("cad_analyst1")){
					fetch_NotepadDetails();
					
					
				}
				else
				{
					formObject.setVisible("Post_Disbursal", false);
					formObject.setVisible("Postdisbursal_Checklist", false);
					formObject.setVisible("ReferHistory_Frame1",true);
					formObject.setVisible("ReferHistory",true);
					//--Below code added by yash 15/12/17 as per CC FSD 2.7
					PersonalLoanS.mLogger.info("In other than TO Team Post disbursal");
				}
				//--Above code added by nikhil 31/10/17 as per CC FSD 2.7


			}
			
			//PCASI - 3524
			if("CAD_Analyst1".equalsIgnoreCase(activityName) || "CAD_Analyst2".equalsIgnoreCase(activityName) 
					|| "CPV".equalsIgnoreCase(activityName) || "Post_Disbursal".equalsIgnoreCase(activityName) 
					|| "Hold_CPV".equalsIgnoreCase(activityName) || "TO_Team".equalsIgnoreCase(activityName)
					|| "CPV_Analyst".equalsIgnoreCase(activityName) || "Disbursal_Maker".equalsIgnoreCase(activityName)
					|| "Disbursal_Checker".equalsIgnoreCase(activityName) ||  "PostDisbursal_Maker".equalsIgnoreCase(activityName)
					|| "PostDisbursal_Checker".equalsIgnoreCase(activityName) || "Takeover_Hold".equalsIgnoreCase(activityName)){
				PersonalLoanS.mLogger.info("Inside if, activityName :: "+activityName);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_InterestRate", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_EMI", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label5", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label6", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_Tenor", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label7", false);
				PersonalLoanS.mLogger.info("Hiding Interest Rate, EMI and Tenor fields and lables.");
			}
			else{
				PersonalLoanS.mLogger.info("Inside else, activityName :: "+activityName);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_InterestRate", true);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_EMI", true);
				formObject.setVisible("ELigibiltyAndProductInfo_Label7", true);
				PersonalLoanS.mLogger.info("Setting Interest Rate, EMI fields and Tenor lable visible true.");
			}
		}

		/*          Function Header:

		 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to set activity name      

		 ***********************************************************************************  */
					public void setActivityname(FormReference formObject , String activityName){
			try{
				formObject.setNGValue("QueueLabel","PL_"+activityName);
			}catch(Exception ex){
				PersonalLoanS.mLogger.info("Exception:"+ex.getMessage());
				PLCommon.printException(ex);			
			}
		}

		/*          Function Header:

		 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to align the Fragments after Fetch   

		 ***********************************************************************************  */

		public void alignfragmentsafterfetch(FormReference formObject){


			formObject.fetchFragment("Address_Details_container", "AddressDetails","q_AddressDetails");
			if(formObject.getNGFrameState("Alt_Contact_container") == 1) {
				formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
				formObject.setNGFrameState("Alt_Contact_container", 0);
	
			}


			formObject.setTop("Alt_Contact_container",formObject.getTop("Address_Details_container")+formObject.getHeight("Address_Details_container"));
			PersonalLoanS.mLogger.info("height of address is: "+formObject.getTop("Address_Details_container")+"!!!"+formObject.getHeight("Address_Details_container"));

			formObject.setTop("ReferenceDetails", formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container")+10);
			PersonalLoanS.mLogger.info("height of reference is: "+formObject.getTop("ReferenceDetails")+"!!!"+formObject.getHeight("ReferenceDetails"));

			formObject.setTop("FATCA", formObject.getTop("ReferenceDetails")+30);
			PersonalLoanS.mLogger.info("height of FATCA is: "+formObject.getTop("FATCA")+"!!!"+formObject.getHeight("FATCA"));

			formObject.setTop("KYC", formObject.getTop("FATCA")+30);
			PersonalLoanS.mLogger.info("height of KYC is: "+formObject.getTop("KYC")+"!!!"+formObject.getHeight("KYC"));

			formObject.setTop("OECD", formObject.getTop("KYC")+30);
			PersonalLoanS.mLogger.info("height of OECD is: "+formObject.getTop("OECD")+"!!!"+formObject.getHeight("OECD"));

			formObject.setTop("Credit_card_Enq", formObject.getTop("OECD")+30);
			PersonalLoanS.mLogger.info("height of CreditCardEnq is: "+formObject.getTop("CreditCardEnq")+"!!!"+formObject.getHeight("CreditCardEnq"));

			formObject.setTop("Case_History", formObject.getTop("Credit_card_Enq")+30);
			PersonalLoanS.mLogger.info("height of CaseHistoryReport is: "+formObject.getTop("CaseHistoryReport")+"!!!"+formObject.getHeight("CaseHistoryReport"));

			formObject.setTop("LOS", formObject.getTop("Case_History")+30);//formObject.getHeight("Case_History")+20);
			PersonalLoanS.mLogger.info("height of LOS is: "+formObject.getTop("LOS")+"!!!"+formObject.getHeight("LOS"));

		}

		/*          Function Header:

		 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to set PartMatch     

		 ***********************************************************************************  */

		public void setPartMatch(FormReference formObject){
			try{
				int framestate_part_match = formObject.getNGFrameState("Part_Match");
				if(framestate_part_match == 0){
				}
				else {
					formObject.fetchFragment("Part_Match", "PartMatch", "q_PartMatch");
					formObject.setNGFrameState("Part_Match",0);	
					
				}
				
				int framestate_Alt_Contact = formObject.getNGFrameState("Alt_Contact_container");
				if(framestate_Alt_Contact == 0){
					//PersonalLoanS.mLogger.info("Alternate details alread fetched");
				}
				else {
					formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
					formObject.setNGFrameState("Alt_Contact_container",0);
					alignDemographiTab(formObject);
				}
				PersonalLoanS.mLogger.info("Inside Part_Match");  

			}
			catch(Exception ex){
				PersonalLoanS.mLogger.info( "Exception:"+ex.getMessage());
				PLCommon.printException(ex);			
			}
		}
		
		

		/*          Function Header:

		 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Hide Liability Fields     

		 ***********************************************************************************  */

		public void hideLiabilityAddFields(FormReference formObject) {

			formObject.setVisible("Liability_New_delinin3",false);
			formObject.setVisible("Liability_New_DPD30inlast6",false);
			formObject.setVisible("Liability_New_DPD30inlast18",false);
			formObject.setVisible("Liability_New_Label2",false);
			formObject.setVisible("Liability_New_writeoff",false);
			formObject.setVisible("Liability_New_Label4",false);
			formObject.setVisible("Liability_New_worststatuslast24",false);
			formObject.setVisible("Liability_New_Label8",false);
			formObject.setVisible("Liability_New_Text3",false);
		}


		public void fetch_NotepadDetails()
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			formObject.fetchFragment("Notepad_Values", "NotepadDetails", "q_Note");
			
			

			if(!NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_contactableFlag"))){
				formObject.setVisible("NotepadDetails_Label6", true);
				formObject.setVisible("NotepadDetails_insqueue", true);
				formObject.setVisible("NotepadDetails_inscompletion", true);
				formObject.setVisible("NotepadDetails_Label10", true);
				formObject.setVisible("NotepadDetails_Actdate", true);
				formObject.setVisible("NotepadDetails_Label11", true);
				formObject.setVisible("NotepadDetails_Actusername", true);
				formObject.setVisible("NotepadDetails_Label9", true);
				formObject.setVisible("NotepadDetails_ActuserRemarks", true);
				//formObject.setHeight("NotepadDetails_Frame1", 400);//Arun (11/9/17)
				//formObject.setTop("NotepadDetails_save",420);//Arun (11/9/17)
				//LoadPickList("NotepadDetails_notedesc", "select '--Select--'  union select  description from ng_master_notedescription with(nolock)");
				
				//PCASI - 3154
				PersonalLoanS.mLogger.info("Notepad details change for activity name :: "+formObject.getWFActivityName());
				if("Disbursal_Maker".equalsIgnoreCase(formObject.getWFActivityName()) || "Disbursal_Checker".equalsIgnoreCase(formObject.getWFActivityName()))
					LoadPickList("NotepadDetails_notedesc", "select '--Select--' union select  description from ng_master_notedescription with (nolock)");
				else
					LoadPickList("NotepadDetails_notedesc", "select '--Select--' union select  description from ng_master_notedescription with (nolock) where workstep != 'NA'");

				
				formObject.setNGValue("NotepadDetails_notedesc","--Select--");
				//formObject.setLocked("NotepadDetails_notecode", true);

			}else{
				formObject.setEnabled("NotepadDetails_Frame1", false);
			}
		}
		public void autopopulateValuesEmployeesVeri(){
			FormReference formObject=FormContext.getCurrentInstance().getFormReference();
			PersonalLoanS.mLogger.info("PL"+ "Inside add button: business verification2 b$ function of autopopulateValuesBusinessVerification");
			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_EmploymentVerification_fixedsal_ver","cmplx_EmploymentVerification_AccomProvided_ver","cmplx_EmploymentVerification_Desig_ver","cmplx_EmploymentVerification_doj_ver","cmplx_EmploymentVerification_confirmedinJob_ver","cmplx_EmploymentVerification_LoanFromCompany_ver","cmplx_EmploymentVerification_PermanentDeductSal_ver","cmplx_EmploymentVerification_SmartCheck_ver","cmplx_EmploymentVerification_FiledVisitedInitiated_ver");
			LoadPicklistVerification(LoadPicklist_Verification);
			int framestate1=formObject.getNGFrameState("Alt_Contact_container");
			if(framestate1 != 0){
				formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
			}
			int framestate2=formObject.getNGFrameState("IncomeDEtails");
			if(framestate2 != 0){
				formObject.fetchFragment("IncomeDEtails", "IncomeDetails", "q_IncomeDetails");
				expandIncome();
			}
			int framestate3=formObject.getNGFrameState("EmploymentDetails");
			if(framestate3 != 0){
				//formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmpDetails");
				loadEmployment();
			}
			if("Y".equalsIgnoreCase(formObject.getNGValue("cmplx_IncomeDetails_SalaryXferToBank")))
			{//Generate_Template
				PersonalLoanS.mLogger.info( "Inside value in customer detail verification salary");
				formObject.setNGValue("EmploymentVerification_s2_Text29",formObject.getNGValue("cmplx_Customer_CIFNo"));
				formObject.setLocked("EmploymentVerification_s2_Text29", true);
			}
			else
			{
				PersonalLoanS.mLogger.info( "Inside value in customer detail verification salary");
				formObject.setNGValue("EmploymentVerification_s2_Text29","NA-No salary transfer with Rakbank");
				formObject.setLocked("EmploymentVerification_s2_Text29", true);
				PersonalLoanS.mLogger.info("value of emp status");

			}
			int framestate4=formObject.getNGFrameState("Cust_Det_Ver");
			String fvi="";
			if(framestate4 !=0){
				formObject.fetchFragment("Customer_Details_Verification1", "CustDetailVerification1", "q_CustDetailVeriFCU");
				fvi=formObject.getNGValue("cmplx_CustDetailverification1_Filed_Visit_value");
				formObject.setNGFrameState("Customer_Details_Verification1", 1);

			}
			else
			{
				fvi=formObject.getNGValue("cmplx_CustDetailverification1_Filed_Visit_value");
			}
			formObject.setNGValue("cmplx_EmploymentVerification_OffTelNo",formObject.getNGValue("AlternateContactDetails_OFFICENO"));
			formObject.setLocked("cmplx_EmploymentVerification_OffTelNo", true);
			formObject.setNGValue("cmplx_EmploymentVerification_fixedsal_val",formObject.getNGValue("cmplx_IncomeDetails_netSal1"));
			formObject.setLocked("cmplx_EmploymentVerification_fixedsal_val", true);
			formObject.setNGValue("cmplx_EmploymentVerification_AccomProvided_val",formObject.getNGValue("cmplx_IncomeDetails_Accomodation"));
			formObject.setLocked("cmplx_EmploymentVerification_AccomProvided_val", true);
			formObject.setNGValue("cmplx_EmploymentVerification_doj_val",formObject.getNGValue("cmplx_EmploymentDetails_DOJ"));
			formObject.setLocked("cmplx_EmploymentVerification_doj_val", true);
			formObject.setNGValue("cmplx_EmploymentVerification_Desig_val",formObject.getNGValue("cmplx_EmploymentDetails_Designation"));
			formObject.setLocked("cmplx_EmploymentVerification_Desig_val", true);
			formObject.setNGValue("cmplx_EmploymentVerification_FiledVisitedInitiated_value",fvi);
			formObject.setLocked("cmplx_EmploymentVerification_FiledVisitedInitiated_value", true);
			formObject.setNGValue("cmplx_EmploymentVerification_confirmedinJob_val",formObject.getNGValue("cmplx_EmploymentDetails_JobConfirmed"));
			PersonalLoanS.mLogger.info("PL"+ formObject.getNGValue("cmplx_EmploymentDetails_JobConfirmed"));
			
			formObject.setLocked("cmplx_EmploymentVerification_confirmedinJob_val", true);
			
		}

		public void LockFragmentsOnLoad(ComponentEvent pEvent)
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			PersonalLoanS.mLogger.info("Inside LockFragmentsOnLoad()");
			if ("Customer".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("Customer_Frame1",true);
				loadPicklistCustomer();
			}	

			else if ("Product".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("Product_Frame1",true);
				LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct with(nolock)");
				LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_ApplicationType with (nolock) order by code");
			}

			else if ("GuarantorDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

				formObject.setLocked("GuarantorDetails_Frame1",true);
			}

			else if ("IncomeDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				LoadPickList("cmplx_IncomeDetails_StatementCycle", "select '--Select--' union select convert(varchar, description) from NG_MASTER_StatementCycle with (nolock)");

				formObject.setLocked("IncomeDetails_Frame1",true);
			} 

			else if ("Liability_New".equalsIgnoreCase(pEvent.getSource().getName())) {

				formObject.setLocked("ExtLiability_Frame1",true);
			}

			else if ("EMploymentDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				loadPicklist4();
				formObject.setLocked("EMploymentDetails_Frame1",true);
			}

			else if ("ELigibiltyAndProductInfo".equalsIgnoreCase(pEvent.getSource().getName())) {

				formObject.setLocked("ELigibiltyAndProductInfo_Frame1",true);
				PersonalLoanS.mLogger.info("Shweta - cmplx_EligibilityAndProductInfo_takeoverBank value 7419" + formObject.getNGValue("cmplx_EligibilityAndProductInfo_takeoverBank"));


			}

			else if ("LoanDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

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
				LoadpicklistCardDetails();
				Load_Self_Supp_Data();	
				formObject.setLocked("CardDetails_Frame1",true);
				String Activityname=formObject.getWFActivityName();
				if(Activityname.equalsIgnoreCase("DSA_CSO_Review")){
					disableButtonsPL("CardDetails");
					formObject.setVisible("CardDetails_Label7", true);
					formObject.setVisible("cmplx_CardDetails_statcycle", true);
					formObject.setVisible("CardDetails_Label3", false);
					formObject.setVisible("cmplx_CardDetails_CompanyEmbossing_name", false);//added by akshay on 11/9/17
					formObject.setLeft("CardDetails_Label5", 288);
					formObject.setLeft("cmplx_CardDetails_suppcardreq", 288);
					if("Islamic".equalsIgnoreCase(formObject.getNGValue("LoanType_Primary"))){
						formObject.setVisible("CardDetails_Label2", true);
						formObject.setVisible("cmplx_CardDetails_charityorg", true);
						formObject.setVisible("CardDetails_Label4", true);
						formObject.setVisible("cmplx_CardDetails_charityAmt", true);
						formObject.setLeft("CardDetails_Label5", 1059);
						formObject.setLeft("cmplx_CardDetails_suppcardreq", 1059);
					}

				}

			}

			else if ("ReferenceDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

				formObject.setLocked("ReferenceDetails_Frame1",true);
			}

			else if ("SupplementCardDetails".equalsIgnoreCase(pEvent.getSource().getName())) {

				formObject.setLocked("SupplementCardDetails_Frame1",true);
			}

			else if ("FATCA".equalsIgnoreCase(pEvent.getSource().getName())) {

				formObject.setLocked("FATCA_Frame6",true);

				Loadpicklistfatca();
			}

			else if ("KYC".equalsIgnoreCase(pEvent.getSource().getName())) {
				//changed by nkhil 5/11/18
				formObject.setLocked("KYC_Frame7",true);
			}

			else if ("OECD".equalsIgnoreCase(pEvent.getSource().getName())) {

				formObject.setLocked("OECD_Frame8",true);
			}
			
			//code added by bandana starts
			else if ("CC_Loan".equalsIgnoreCase(pEvent.getSource().getName()))
			{			      
				formObject.setLocked("CC_Loan_Frame1",true);
				loadPicklist_ServiceRequest();				   
			}
			//code added by bandana end

			else if ("PartMatch".equalsIgnoreCase(pEvent.getSource().getName())) {

				formObject.setLocked("PartMatch_Frame1",true);
			}

			else if ("FinacleCRMIncident".equalsIgnoreCase(pEvent.getSource().getName())) {

				formObject.setLocked("FinacleCRMIncident_Frame1",true);
			}

			else if ("FinacleCRMCustInfo".equalsIgnoreCase(pEvent.getSource().getName())) {

				formObject.setLocked("FinacleCRMCustInfo_Frame1",true);
			}

			else if ("FinacleCore".equalsIgnoreCase(pEvent.getSource().getName())) {

				formObject.setLocked("FinacleCore_Frame1",true);
				//formObject.setEnabled("FinacleCore_save", false);
				//formObject.setEnabled("FinacleCore_Combo1", false);
			}

			else if ("MOL1".equalsIgnoreCase(pEvent.getSource().getName())) {

				formObject.setLocked("MOL1_Frame1",true);
				//formObject.setEnabled("cmplx_MOL_rem",false);
			}

			else if ("WorldCheck1".equalsIgnoreCase(pEvent.getSource().getName())) {

				formObject.setLocked("WorldCheck1_Frame1",true);
				//formObject.setEnabled("WorldCheck1_Gender", false);
				//formObject.setEnabled("WorldCheck1_rem", false);
			}

			else if ("RejectEnq".equalsIgnoreCase(pEvent.getSource().getName())) {

				formObject.setLocked("RejectEnq_Frame1",true);
			}

			else if ("SalaryEnq".equalsIgnoreCase(pEvent.getSource().getName())) {

				formObject.setLocked("SalaryEnq_Frame1",true);
			}

			else if ("CustDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("CustDetailVerification_Frame1",true);//By shweta for jira# PCAS-2372
			}

			else if ("BussinessVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

				formObject.setLocked("BussinessVerification_Frame1",true);
			}

			else if ("HomeCountryVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

				formObject.setLocked("HomeCountryVerification_Frame1",true);
			}

			else if ("ResidenceVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

				formObject.setLocked("ResidenceVerification_Frame1",true);
			}

			else if ("GuarantorVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

				formObject.setLocked("GuarantorVerification_Frame1",true);
			}

			else if ("ReferenceDetailVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

				formObject.setLocked("ReferenceDetailVerification_Frame1",true);
			}

			else if ("OfficeandMobileVerification".equalsIgnoreCase(pEvent.getSource().getName())) {

				formObject.setLocked("OfficeandMobileVerification_Frame1",true);
			}

			else if ("LoanandCard".equalsIgnoreCase(pEvent.getSource().getName())) {

				formObject.setLocked("LoanandCard_Frame1",true);
			}

			else if ("Compliance".equalsIgnoreCase(pEvent.getSource().getName())) {	

				formObject.setLocked("Compliance_Frame1",true);
				formObject.setLocked("Compliance_CompRemarks",false);			            
				formObject.setLocked("Compliance_Modify",false); 
				formObject.setLocked("Compliance_Save",false);
				formObject.setLocked("cmplx_Compliance_ComplianceRemarks",false);



				formObject.fetchFragment("World_Check", "WorldCheck1", "q_WorldCheck");

				formObject.setNGFrameState("World_Check",0);
				int n=formObject.getLVWRowCount("cmplx_WorldCheck_WorldCheck_Grid");
				PersonalLoanS.mLogger.info("CC value of world check row--->value of n "+n);
				if(n>0)
				{ 
					String UID = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,12);
					PersonalLoanS.mLogger.info("CC value of world check UID"+UID);
					formObject.setNGValue("Compliance_UID",UID);
					String EI = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,15);
					PersonalLoanS.mLogger.info("CC value of world check EI "+EI);
					formObject.setNGValue("Compliance_EI",EI);
					String Name = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,0);
					PersonalLoanS.mLogger.info("CC value of world check Name"+Name);	
					formObject.setNGValue("Compliance_Name",Name);
					String Dob = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,2);
					PersonalLoanS.mLogger.info("CC value of world check Dob"+Dob);
					formObject.setNGValue("Compliance_DOB",Dob);
					String Citizenship = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,7);
					PersonalLoanS.mLogger.info("Citizenship"+Citizenship);	
					formObject.setNGValue("Compliance_Citizenship",Citizenship);
					String Remarks = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,16);
					formObject.setNGValue("Compliance_Remarks",Remarks);
					String Id_No = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,1);
					formObject.setNGValue("Compliance_IdentificationNumber",Id_No);
					String Age = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,3);
					formObject.setNGValue("Compliance_Age",Age);
					String Position = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,4);
					formObject.setNGValue("Compliance_Positon",Position);
					String Deceased = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,5); 
					formObject.setNGValue("Compliance_Deceased",Deceased);
					String Category = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,6);
					formObject.setNGValue("Compliance_Category",Category);
					String Location = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,8);
					formObject.setNGValue("Compliance_Location",Location);
					String Identification = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,9); 
					formObject.setNGValue("Compliance_Identification",Identification);
					String Biography = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,10); 
					formObject.setNGValue("Compliance_Biography",Biography);
					String Reports = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,11); 
					formObject.setNGValue("Compliance_Reports",Reports);
					String Entered_Date = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,13);
					formObject.setNGValue("Compliance_EntertedDate",Entered_Date);
					String Updated_Date = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,14);
					formObject.setNGValue("Compliance_UpdatedDate",Updated_Date);
					String Document = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,18);  
					formObject.setNGValue("Compliance_Document",Document);
					String Decision = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,19);
					formObject.setNGValue("Compliance_Decision",Decision);
					String Match_Rank = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,20);
					formObject.setNGValue("Compliance_MatchRank",Match_Rank);
					String Alias = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,21);
					formObject.setNGValue("Compliance_Alias",Alias);
					String birth_Country = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,22);
					formObject.setNGValue("Compliance_BirthCountry",birth_Country);
					String resident_Country = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,23);
					formObject.setNGValue("Compliance_ResidenceCountry",resident_Country);
					String Address = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,24);
					formObject.setNGValue("Compliance_Address",Address);
					String Gender = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,25);
					formObject.setNGValue("Compliance_Gender",Gender);
					String Listed_On = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,26);
					formObject.setNGValue("Compliance_ListedOn",Listed_On);
					String Program = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,27);
					formObject.setNGValue("Compliance_Program",Program);
					String external_ID = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,28);
					formObject.setNGValue("Compliance_ExternalID",external_ID);
					String data_Source = formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",0,29);
					formObject.setNGValue("Compliance_DataSource",data_Source);
					formObject.setNGValue("Compliance_winame",formObject.getWFWorkitemName());
					formObject.ExecuteExternalCommand("NGAddRow", "cmplx_Compliance_cmplx_gr_compliance");
					formObject.RaiseEvent("WFSave");
				}
				formObject.setNGFrameState("World_Check",1);
			}

			else if ("SmartCheck".equalsIgnoreCase(pEvent.getSource().getName())) {

				formObject.setLocked("SmartCheck_Frame1",true);
			}
			//below code added by yash for PL - POST DISBURSAL on 27/11/2017
			else if ("DispatchFrag".equalsIgnoreCase(pEvent.getSource().getName())) {

				formObject.setLocked("DispatchFrag_Frame1",true);
			}
			else if ("RiskRating".equalsIgnoreCase(pEvent.getSource().getName())) {

				formObject.setLocked("RiskRating_Frame1",true);
			}




			else if ("FCU_Decision".equalsIgnoreCase(pEvent.getSource().getName())) {

				formObject.setLocked("FCU_Decision_Frame1",true);
			}

			// disha FSD
			else if ("NotepadDetails".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("NotepadDetails_Frame1",true);
				notepad_load();
				notepad_withoutTelLog();
			}

			else if ("DecisionHistory".equalsIgnoreCase(pEvent.getSource().getName())) {

				//for decision fragment made changes 8th dec 2017
				String Activityname=formObject.getWFActivityName();
				PersonalLoanS.mLogger.info("***********Inside decision history of csm");
				if(Activityname.equalsIgnoreCase("DSA_CSO_Review")){
					PersonalLoanS.mLogger.info( "inside dsa review");
					fragment_ALign("Decision_Label1,cmplx_Decision_VERIFICATIONREQUIRED#Decision_Label3,cmplx_Decision_Decision#DecisionHistory_Label1,cmplx_Decision_ReferTo#DecisionHistory_Label11,DecisionHistory_DecisionReasonCode#\n#Decision_Label4,cmplx_Decision_REMARKS#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line	
				
				}
				else if("DDVT_maker".equalsIgnoreCase(formObject.getWFActivityName())){
					fragment_ALign("Decision_Label3,cmplx_Decision_Decision#DecisionHistory_Label1,cmplx_Decision_ReferTo#DecisionHistory_Label11,DecisionHistory_DecisionReasonCode#DecisionHistory_Label41,DecisionHistory_DecisionSubReason#\n#DecisionHistory_CifLock#DecisionHistory_CifUnlock#\n#DecisionHistory_Label5,cmplx_Decision_desc#DecisionHistory_Label3,cmplx_Decision_strength#DecisionHistory_Label4,cmplx_Decision_weakness#Decision_Label4,cmplx_Decision_REMARKS#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line

				}
				else if("DDVT_checker".equalsIgnoreCase(formObject.getWFActivityName())){
					fragment_ALign("cmplx_Decision_waiveoffver#DecisionHistory_CifLock#DecisionHistory_CifUnlock#DecisionHistory_Button3#DecisionHistory_updcust#DecisionHistory_chqbook#\n#Decision_Label3,cmplx_Decision_Decision#DecisionHistory_Label1,cmplx_Decision_ReferTo#DecisionHistory_Label11,DecisionHistory_DecisionReasonCode#DecisionHistory_Label41,DecisionHistory_DecisionSubReason#\n#cmplx_Decision_MultipleApplicantsGrid#\n#DecisionHistory_Label6,cmplx_Decision_IBAN#DecisionHistory_Label7,cmplx_Decision_AccountNo#DecisionHistory_Label8,cmplx_Decision_ChequeBookNumber#DecisionHistory_Label9,cmplx_Decision_DebitcardNumber#\n#DecisionHistory_Label5,cmplx_Decision_desc#DecisionHistory_Label3,cmplx_Decision_strength#DecisionHistory_Label4,cmplx_Decision_weakness#Decision_Label4,cmplx_Decision_REMARKS#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line

				}
				else if("Reject_queue".equalsIgnoreCase(formObject.getWFActivityName())){
					fragment_ALign("Decision_Label1,cmplx_Decision_VERIFICATIONREQUIRED#Decision_Label3,cmplx_Decision_Decision#DecisionHistory_Label1,cmplx_Decision_ReferTo#DecisionHistory_Label11,DecisionHistory_DecisionReasonCode#\n#Decision_Label4,cmplx_Decision_REMARKS#\n#cmplx_Decision_MultipleApplicantsGrid#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line	

				} 
				else
				{
					fragment_ALign("Decision_Label1,cmplx_Decision_VERIFICATIONREQUIRED#Decision_Label3,cmplx_Decision_Decision#DecisionHistory_Label1,cmplx_Decision_ReferTo#DecisionHistory_Label11,DecisionHistory_DecisionReasonCode#\n#Decision_Label4,cmplx_Decision_REMARKS#\n#DecisionHistory_ADD#DecisionHistory_Modify#DecisionHistory_Delete#\n#Decision_ListView1#\n#DecisionHistory_save","DecisionHistory");//\n for new line	

				}

				
				loadPicklist3();
				
				formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save")+ formObject.getHeight("DecisionHistory_save")+20);
				formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1")+20);
				formObject.setTop("ReferHistory",formObject.getTop("DecisionHistory")+formObject.getHeight("DecisionHistory")+20);

			}
			
			
			else if ("ReferHistory".equalsIgnoreCase(pEvent.getSource().getName())) {
				formObject.setLocked("ReferHistory_Frame1",true);
			}
			/*else if ("IncomingDoc".equalsIgnoreCase(pEvent.getSource().getName())) {
				fetchIncomingDocRepeater();
			}*/
		}
		
		public String setReferto(){
			String ReferTo="";
			try{
				FormReference formObject = FormContext.getCurrentInstance().getFormReference();
				String listView = "Decision_ListView1";
				int rowcount = formObject.getLVWRowCount(listView);
				
				for (int i=0;i<rowcount;i++){
					if("".equalsIgnoreCase(formObject.getNGValue(listView,i,12))){
						String refer_temp = formObject.getNGValue(listView,i,6);
						if("".equalsIgnoreCase(refer_temp)){
							ReferTo = refer_temp;
						}else{
							ReferTo = ReferTo+","+refer_temp;
						}
					}
				}
			}
			catch(Exception e){
				PersonalLoanS.mLogger.info( "Exception occured in setReferto: "+e.getMessage());
			}
			return ReferTo;
		}
		public void autopopulateValuesCustomerDetailVerification(FormReference formObject)
		{
			PersonalLoanS.mLogger.info( "Inside value in customer detail verification");
			int framestate1=formObject.getNGFrameState("Alt_Contact_container");
			PersonalLoanS.mLogger.info( "framestate1"+framestate1);
			if(framestate1 != 0){
				PersonalLoanS.mLogger.info( "Inside if framestate1 != 0");
				formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
				formObject.setNGFrameState("Alt_Contact_container", 0);

				PersonalLoanS.mLogger.info( "Inside if framestate1 != 0 Alt_Contact_container");
			}
			PersonalLoanS.mLogger.info( "outside if Alt_Contact_container");
			int framestate2=formObject.getNGFrameState("IncomeDEtails");
			PersonalLoanS.mLogger.info( "framestate2"+framestate2);
			if(framestate2 != 0){
				PersonalLoanS.mLogger.info( "Inside if framestate2 IncomeDEtails");
				expandIncome();
			}
			int framestate3=formObject.getNGFrameState("EmploymentDetails");
			PersonalLoanS.mLogger.info( "framestate3 "+framestate3);
			if(framestate3 != 0){
				PersonalLoanS.mLogger.info( "inside if framestate3");
				try{
					loadEmployment();	
				}
				catch(Exception e)
				{
					PersonalLoanS.mLogger.info( "inside if framestate3"+e.getMessage());
				}
			}
			PersonalLoanS.mLogger.info( "all frames sss");
			if("Y".equalsIgnoreCase(formObject.getNGValue("cmplx_IncomeDetails_SalaryXferToBank"))){
				PersonalLoanS.mLogger.info( "Inside value in customer detail verification salary");
				formObject.setNGValue("Cust_ver_sp2_Text20",formObject.getNGValue("cmplx_Customer_CIFNO"));
				formObject.setLocked("Cust_ver_sp2_Text20", true);
			} else {
				PersonalLoanS.mLogger.info( "Inside value in customer detail verification salary");
				formObject.setNGValue("Cust_ver_sp2_Text20","NA-No salary transfer with RAKBank");
				formObject.setLocked("Cust_ver_sp2_Text20", true);
			}
			//cmplx_fieldvisit_sp2_drop2Salaried
			formObject.setNGValue("cmplx_cust_ver_sp2_application_type","Salaried");
			formObject.setLocked("cmplx_cust_ver_sp2_application_type",true);
			String activityName = formObject.getWFActivityName();
			if(activityName.equalsIgnoreCase("FPU"))
			formObject.setLocked("cmplx_cust_ver_sp2_ContactedDate",false); //bu jahnavi for 3041
			else 
				formObject.setLocked("cmplx_cust_ver_sp2_ContactedDate",true);
			formObject.setNGValue("Cust_ver_sp2_Combo9",formObject.getNGValue("cmplx_EmploymentDetails_EmpStatus"));
			formObject.setLocked("Cust_ver_sp2_Combo9", true);

			
			formObject.setNGValue("Cust_ver_sp2_Text6",formObject.getNGValue("AlternateContactDetails_MobileNo1"));
			formObject.setLocked("Cust_ver_sp2_Text6", true);
			formObject.setNGValue("Cust_ver_sp2_Text8",formObject.getNGValue("AlternateContactDetails_MobileNo2"));
			formObject.setLocked("Cust_ver_sp2_Text8", true);
			
			formObject.setNGValue("Cust_ver_sp2_Text16",formObject.getNGValue("cmplx_IncomeDetails_netSal1"));
			formObject.setLocked("Cust_ver_sp2_Text16", true);
			formObject.setNGValue("Cust_ver_sp2_Text18",formObject.getNGValue("cmplx_Customer_yearsInUAE"));
			formObject.setLocked("Cust_ver_sp2_Text18", true);
			formObject.setNGValue("Cust_ver_sp2_Text14",formObject.getNGValue("cmplx_EmploymentDetails_DOJ"));
			formObject.setLocked("Cust_ver_sp2_Text14", true);
			formObject.setNGValue("Cust_ver_sp2_Text13",formObject.getNGValue("cmplx_EmploymentDetails_Designation"));
			formObject.setLocked("Cust_ver_sp2_Text13", true);
			
			formObject.setNGValue("Cust_ver_sp2_Text4",formObject.getNGValue("cmplx_EmploymentDetails_LOSPrevious"));
			formObject.setLocked("Cust_ver_sp2_Text4", true);
//			LoadPickList("cmplx_cust_ver_sp2_emp_status__remarks","select convert(varchar, description),code from NG_MASTER_EmploymentStatus with (nolock)  where isActive='Y' order by code");
//			formObject.setNGValue("Cust_ver_sp2_Combo8","cmplx_cust_ver_sp2_emp_status__remarks");
//			//LoadPickList("cmplx_cust_ver_sp2_emp_status__remarks","select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_EmploymentStatus with (nolock) where isActive='Y' order by Code");
			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_cust_ver_sp2_desig_drop","cmplx_cust_ver_sp2_desig_drop","cmplx_cust_ver_sp2_doj_drop","cmplx_cust_ver_sp2_emp_status_drop","cmplx_cust_ver_sp2_saalry_drop","cmplx_cust_ver_sp2_salary_payment_drop","cmplx_cust_ver_sp2_years_drop","cmplx_cust_ver_sp2_ContactedOn","cmplx_cust_ver_sp2_prevemp_los_drop");
			List<String> LoadPicklist_update= Arrays.asList("Designation_button1","Designation_button5_View","cmplx_cust_ver_sp2_doj_remarks","cmplx_cust_ver_sp2_emp_status__remarks","cmplx_cust_ver_sp2_salary_remarks","cmplx_cust_ver_sp2_salary_payment_remarks","cmplx_cust_ver_sp2_years_remarks","cmplx_cust_ver_sp2_Mobile","cmplx_cust_ver_sp2_prevemp_los_remark");
			mismatchFieldProperty(formObject,LoadPicklist_Verification,LoadPicklist_update);
			PersonalLoanS.mLogger.info( "all values");
		}
		//Cust_ver_sp2_Text8
		public void autopopulateValuesEmployeesVeri(FormReference formObject,String fvi){
			PersonalLoanS.mLogger.info( "Inside add button: business verification2 b$ function of autopopulateValuesBusinessVerification");
			int framestate1=formObject.getNGFrameState("Alt_Contact_container");
			if(framestate1 != 0){
				formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
				formObject.setNGFrameState("Alt_Contact_container", 0);

			}
			int framestate2=formObject.getNGFrameState("IncomeDEtails");
			if(framestate2 != 0){
				formObject.fetchFragment("IncomeDEtails", "IncomeDetails", "q_IncomeDetails");
				expandIncome();
			}
			int framestate3=formObject.getNGFrameState("EmploymentDetails");
			if(framestate3 != 0){
				/*formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmpDetails");*/
				loadEmployment();
			}//Designation_button1
			if("Y".equalsIgnoreCase(formObject.getNGValue("cmplx_IncomeDetails_SalaryXferToBank")))
			{//Generate_Template
		PersonalLoanS.mLogger.info( "Inside value in customer detail verification salary");
		formObject.setNGValue("EmploymentVerification_s2_Text29",formObject.getNGValue("cmplx_Customer_CIFNO"));
		formObject.setLocked("EmploymentVerification_s2_Text29", true);
			}
	else
	{
		PersonalLoanS.mLogger.info( "Inside value in customer detail verification salary");
		formObject.setNGValue("EmploymentVerification_s2_Text29","NA-No salary transfer with Rakbank");
		formObject.setLocked("EmploymentVerification_s2_Text29", true);
		PersonalLoanS.mLogger.info("value of emp status");
	}
			formObject.setNGValue("cmplx_emp_ver_sp2_application_type","Salaried");
			formObject.setEnabled("cmplx_emp_ver_sp2_application_type", false);
			
		formObject.setVisible("EmploymentVerification_s2_Frame2",true);
			PersonalLoanS.mLogger.info("value of emp status"+formObject.getNGValue("cmplx_EmploymentDetails_EmpStatus"));
			LoadPickList("EmploymentVerification_s2_Combo14", "select description from NG_MASTER_EmploymentStatus with (nolock)  where isActive='Y' and Code='"+formObject.getNGValue("cmplx_EmploymentDetails_EmpStatus")+"'");
			String query = "select description from NG_MASTER_EmploymentStatus with (nolock)  where isActive='Y' and Code='"+formObject.getNGValue("cmplx_EmploymentDetails_EmpStatus")+"'";
			
			List<List<String>> result = formObject.getDataFromDataSource(query);
			if(!result.isEmpty()){
				formObject.setNGValue("EmploymentVerification_s2_Combo14",result.get(0).get(0));
			}
			formObject.setEnabled("EmploymentVerification_s2_Combo14", false);
			formObject.setNGValue("EmploymentVerification_s2_Combo14", formObject.getNGValue("cmplx_EmploymentDetails_EmpStatus"));
			
			formObject.setNGValue("EmploymentVerification_s2_Text23",formObject.getNGValue("AlternateContactDetails_OFFICENO"));
			formObject.setLocked("EmploymentVerification_s2_Text23", true);
		formObject.setLocked("cmplx_emp_ver_sp2_salary_break_remarks", true);
			formObject.setNGValue("EmploymentVerification_s2_Text5","");
			formObject.setLocked("EmploymentVerification_s2_Text5", true);
			//formObject.setNGValue("EmploymentVerification_s2_Text33",formObject.getNGValue("cmplx_EmploymentDetails_EmirateOfWork"));
			///formObject.setLocked("EmploymentVerification_s2_Text33", true);
			 LoadPickList("EmploymentVerification_s2_Combo15", "select description from NG_MASTER_emirate with (nolock) where Code='"+formObject.getNGValue("cmplx_Customer_EMirateOfVisa")+"'");
				
			formObject.setEnabled("EmploymentVerification_s2_Combo15", false);
			LoadPickList("cmplx_emp_ver_sp2_emirate_remarks", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_emirate with (nolock) where isActive='Y' order by Code");
			//LoadPickList("cmplx_emp_ver_sp2_empstatus_remarks", "select convert(varchar, description),code from NG_MASTER_EmploymentStatus with (nolock)  where isActive='Y' order by code");
			formObject.setNGValue("EmploymentVerification_s2_Text7",formObject.getNGValue("cmplx_IncomeDetails_netSal1"));
			formObject.setLocked("EmploymentVerification_s2_Text7", true);
			formObject.setNGValue("EmploymentVerification_s2_Text4",formObject.getNGValue("cmplx_EmploymentDetails_DOJ"));
			formObject.setLocked("EmploymentVerification_s2_Text4", true);
			formObject.setNGValue("EmploymentVerification_s2_Text1",formObject.getNGValue("cmplx_EmploymentDetails_Designation"));
			formObject.setLocked("EmploymentVerification_s2_Text1", true);
			// Below added by Rajan for FCU implementation of Self Employed frame 
			formObject.setNGValue("EmploymentVerification_s2_Text13", formObject.getNGValue("cmplx_Customer_CIFNo"));
			formObject.setNGValue("EmploymentVerification_s2_Text14", formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",1,15));
			formObject.setNGValue("EmploymentVerification_s2_Text15",formObject.getNGValue("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails",1,9));
			formObject.setNGValue("EmploymentVerification_s2_Text11", formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",1,4));
			formObject.setLocked("EmploymentVerification_s2_Text11", true);  //JIRA 3033
			/*formObject.setNGValue("EmploymentVerification_s2_Text38", formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",1,8));
			formObject.setLocked("EmploymentVerification_s2_Text38", true);
			formObject.setNGValue("EmploymentVerification_s2_Text36", formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",1,9));
			formObject.setLocked("EmploymentVerification_s2_Text36", true);
			formObject.setNGValue("EmploymentVerification_s2_Text37",  formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",1,10));
			formObject.setLocked("EmploymentVerification_s2_Text37", true);
			formObject.setNGValue("EmploymentVerification_s2_Text39", formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",1,14));
			formObject.setLocked("EmploymentVerification_s2_Text39", true);*/
			//Deepak 26 Dec code commented for PCSP-283
			/*formObject.setNGValue("cmplx_EmploymentVerification_FiledVisitedInitiated_value",fvi);
			formObject.setLocked("cmplx_EmploymentVerification_FiledVisitedInitiated_value", true);*/
			//formObject.setNGFrameState("Customer_Details_Verification1", 1);
			/*formObject.setHeight("Employment_Verification",formObject.getTop("EmploymentVerification_Frame1")+formObject.getHeight("EmploymentVerification_Frame1")+5);
			formObject.setTop("Banking_Check", formObject.getTop("Employment_Verification")+formObject.getHeight("Employment_Verification")+15);
			formObject.setTop("Smart_Check1", formObject.getTop("Banking_Check")+25);*/
			//--above code added by  nikhil 13/10/17 as per CC FSD 2.2
		}
		public void ddvtmaker_submitapp(){
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			PersonalLoanS.mLogger.info("ddvtmaker_submitapp method start:: Winame: "+ formObject.getWFWorkitemName()+" user name: "+ formObject.getUserName());
			try{
				String CRN=formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",0, 2);
				for(int i=1;i<formObject.getLVWRowCount("cmplx_CardDetails_cmplx_CardCRNDetails");i++)
				{
					CRN=CRN+","+formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",i, 2);
				}
				formObject.setNGValue("CRN", CRN);
				formObject.setNGValue("ECRN", formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",0,1));
				//changed by nikhil 27/11 to set header
				formObject.setNGValue("Final_Limit", formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
				formObject.setNGValue("LoanLabel",formObject.getNGValue("Final_Limit"));
				//hritik -> communication
				PersonalLoanS.mLogger.info("Before NUmber format @PLComCode in ddvtMsubmitapp :");
				NumberFormat fin_lim = NumberFormat.getNumberInstance(Locale.US);
				String FL = formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit");
				double FL_1 = Double.parseDouble(FL);
				PersonalLoanS.mLogger.info("Before NUmber format : FL :"+FL_1);
				String new_format = fin_lim.format(FL_1);
				PersonalLoanS.mLogger.info("Before NUmber format new_format::"+new_format);
				formObject.setNGValue("loan_amount_1",new_format);
				
				//added by nikhil to set new Email ID
				formObject.setNGValue("email_id",formObject.getNGValue("AlternateContactDetails_EMAIL1_PRI"));
				PersonalLoanS.mLogger.info("CRN: "+formObject.getNGValue("CRN")+" ECRN: "+formObject.getNGValue("ECRN") + " Final_Limit: "+formObject.getNGValue("Final_Limit")+" email_id: "+formObject.getNGValue("email_id"));
			}
			catch(Exception e){
				PersonalLoanS.mLogger.info("Exception occured in ddvtmaker_submitapp to set ext table data: "+e.getMessage());
			}

			try{
				String dec = formObject.getNGValue("cmplx_Decision_Decision");
				if(dec!=null && !"".equals(dec) && "Submit".equalsIgnoreCase(dec)){
					formObject.setNGValue("decision","Approve" );
				}
				else{
					formObject.setNGValue("decision",dec);
				}
				if("".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_VERIFICATIONREQUIRED")) || "null".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_VERIFICATIONREQUIRED")) || "N".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_VERIFICATIONREQUIRED")) || "false".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_VERIFICATIONREQUIRED")) )
				{
					PersonalLoanS.mLogger.info("PL val change "+ "Inside Y of CPV required");
					formObject.setNGValue("CPV_REQUIRED","N");
				}
				else
				{
					PersonalLoanS.mLogger.info("CC val change "+ "Inside N of CPV required");
					formObject.setNGValue("CPV_REQUIRED","Y");
				}
			}
			catch(Exception e){
				PersonalLoanS.mLogger.info("Exception occured in ddvtmaker_submitapp to set decision+fragment data save table data: "+e.getMessage());
			}
			formObject.RaiseEvent("WFSave");
			CustomSaveForm();
			PersonalLoanS.mLogger.info("ddvtmaker_submitapp method end:: Winame: "+ formObject.getWFWorkitemName()+" user name: "+ formObject.getUserName());
		}
		//removed extra code 
		
		
		public void autopopulateValuesCustInfo(FormReference formObject){
			String emirateid=formObject.getNGValue("cmplx_Customer_EmiratesID");
			String cifid=formObject.getNGValue("cmplx_Customer_CIFNO");
			formObject.setNGValue("cmplx_CustDetailverification1_EmiratesId", emirateid);
			formObject.setNGValue("cmplx_CustDetailverification1_DOB_value", formObject.getNGValue("cmplx_Customer_DOb"));
			formObject.setNGValue("cmplx_CustDetailverification1_MobNo1_value", formObject.getNGValue("cmplx_Customer_MobNo"));
			formObject.setNGValue("CustDetailVerification1_ApplicationNo", formObject.getWFWorkitemName());

			String customerName = formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_MiddleName")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme");

			formObject.setNGValue("CustDetailVerification1_Text8", cifid);
			formObject.setNGValue("CustDetailVerification1_Text5", emirateid);
			formObject.setNGValue("CustDetailVerification1_CifId", cifid);
			formObject.setNGValue("CustDetailVerification1_EmiratesID", emirateid);
			formObject.setNGValue("CustDetailVerification1_CustName", customerName);
			formObject.setNGValue("CustDetailVerification1_nationality",formObject.getNGValue("cmplx_Customer_Nationality"));
			formObject.setNGValue("CustDetailVerification1_VisaNo",formObject.getNGValue("cmplx_Customer_VisaNo"));
			formObject.setNGValue("CustDetailVerification1_VisaIssueDt", formObject.getNGValue("cmplx_Customer_VisaIssueDate"));
			formObject.setNGValue("CustDetailVerification1_VisaExpDt", formObject.getNGValue("cmplx_Customer_VisaExpiry"));
			formObject.setNGValue("CustDetailVerification1_EmirIDExpDt",formObject.getNGValue("cmplx_Customer_EmirateIDExpiry"));
			formObject.setNGValue("CustDetailVerification1_PassNo",formObject.getNGValue("cmplx_Customer_PAssportNo"));
			formObject.setNGValue("cmplx_CustDetailverification1_EID_ID", formObject.getNGValue("cmplx_Customer_EMirateOfVisa"));
			formObject.setLocked("CustDetailVerification1_CifId",true);
			formObject.setLocked("CustDetailVerification1_EmiratesID",true);
			formObject.setLocked("CustDetailVerification1_CustName",true);
			formObject.setLocked("CustDetailVerification1_nationality",true);
			formObject.setLocked("CustDetailVerification1_VisaExpDt",true);
			formObject.setLocked("CustDetailVerification1_EmirIDExpDt",true);
			formObject.setLocked("CustDetailVerification1_PassNo",true);
			formObject.setLocked("CustDetailVerification1_VisaIssueDt",true);
			formObject.setLocked("CustDetailVerification1_VisaNo",true);
			formObject.setLocked("CustDetailVerification1_ApplicationNo",true);
			formObject.setLocked("cmplx_CustDetailverification1_MobNo1_value",true);
			formObject.setLocked("cmplx_CustDetailverification1_DOB_value",true);
			formObject.setLocked("cmplx_CustDetailverification1_EID_ID",true);

		}
	
		private  String writeToFile(List<String> list, String path,String FileName)  {
			BufferedWriter out = null;   
			BufferedReader reader = null;
			StringBuilder Newpath = new StringBuilder(path);
			String WriteFlag = "true";
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String PL_Wi_Name = formObject.getWFWorkitemName();
			String logFilePath=System.getProperty("user.dir")+File.separator+"FPU_Reports"+File.separator+PL_Wi_Name;				
			try {
				PersonalLoanS.mLogger.info("path writeToFile= " + path);
				File d = new File(logFilePath);
					if (!d.exists()) 
					{
						d.mkdir();
					}
					PersonalLoanS.mLogger.info("File exist " +d.exists()  );		

					File file = new File(Newpath.toString());
					if(!file.exists()){
						file.createNewFile();
						PersonalLoanS.mLogger.info("File exist " );		

					}
					
					PrintWriter writer = new PrintWriter(file);	
					writer.print("");
					writer.close();

				
					reader = new BufferedReader(new FileReader(file)); 
					out = new BufferedWriter(new FileWriter(file,true));
					//String line;
					PersonalLoanS.mLogger.info("Before if of write to file function " );		
					PersonalLoanS.mLogger.info("Inside if of write to file function " );
					for (Object s : list) {
						out.write(s.toString());
						out.newLine();
						out.flush();
					}	
					out.close();
					reader.close();

					String DocName = FileName ;	
					String jtsIP = FormContext.getCurrentInstance().getFormConfig().getConfigElement("JTSIP");
					String jtsPort = "3333"; 
					String cabinetName=FormContext.getCurrentInstance().getFormConfig().getConfigElement("EngineName"); 
					String volumeID = FormContext.getCurrentInstance().getFormConfig().getConfigElement("VolumeId"); 
					String Name = formObject.getUserName(); String password = ""; 
					String sessionID = formObject.getWFSessionId(); 
					String serverPort = "2809";
					String status=attachDocument(path,PL_Wi_Name,DocName,jtsIP,jtsPort,cabinetName,volumeID,Name,password,sessionID,serverPort);
					String[] statusAttachArr =status.split("~"); 
					String docStatus="";String outputDoc="";String docIndex="";String strDocISIndex="";
					PersonalLoanS.mLogger.info("check for docname by jahnavi"+DocName);
					for(int k=0;k<statusAttachArr.length;k++)
					{
						if(k==0)
							docStatus=statusAttachArr[k];
						if(k==1)
							outputDoc=statusAttachArr[k];
						if(k==2)
							docIndex=statusAttachArr[k];
						if(k==3)
							strDocISIndex=statusAttachArr[k];
					 }
					formObject.setNGValue("cmplx_emp_ver_sp2_inputxml","");
					//formObject.setNGValue("cmplx_emp_ver_sp2_inputxml_reject","");
					//formObject.setNGValue("cmplx_emp_ver_sp2_inputxml_comp","");
					formObject.setNGValue("cmplx_emp_ver_sp2_docsize","");
					
					if(docStatus.equalsIgnoreCase("Y") ){
						formObject.setNGValue("cmplx_emp_ver_sp2_inputxml",outputDoc);
						// docIndex=formObject.getNGValue("cmplx_emp_ver_sp2_docsize")+","+docIndex;
						formObject.setNGValue("cmplx_emp_ver_sp2_docsize",docIndex );
					}
					/*if(docStatus.equalsIgnoreCase("Y") && "LOAN_APPROVED_PL".equalsIgnoreCase(DocName)){
						formObject.setNGValue("cmplx_emp_ver_sp2_inputxml",outputDoc);
						// docIndex=formObject.getNGValue("cmplx_emp_ver_sp2_docsize")+","+docIndex;
						formObject.setNGValue("cmplx_emp_ver_sp2_docsize",docIndex );
					}
					else if(docStatus.equalsIgnoreCase("Y") && "REJECTED_LOAN_PL_".equalsIgnoreCase(DocName)){
						formObject.setNGValue("cmplx_emp_ver_sp2_inputxml_reject",outputDoc);
						formObject.setNGValue("cmplx_emp_ver_sp2_docsize_reject",docIndex );
					}
					else if(docStatus.equalsIgnoreCase("Y") && "COMPANY_MISMATCH_PL".equalsIgnoreCase(DocName)){
						formObject.setNGValue("cmplx_emp_ver_sp2_inputxml_comp",outputDoc);
						 //docIndex=formObject.getNGValue("cmplx_emp_ver_sp2_docsize")+","+docIndex;
						formObject.setNGValue("cmplx_emp_ver_sp2_docsize",docIndex );
					}*/
					
					Custom_fragmentSave("Employment_Verification");
					

					
					
					PersonalLoanS.mLogger.info("Inside if of write to file function "+docStatus +"    "+outputDoc+"    "+docIndex+"    "+ strDocISIndex);
					File lobjFileTemp = new File(Newpath.toString());
					File deleteFolder = new File(logFilePath);
					if (lobjFileTemp.exists()) 
					{
						if (!lobjFileTemp.isDirectory()) 
						{
							boolean test2=lobjFileTemp.delete();
							deleteDir(deleteFolder);
						}
					}else 
					{
						lobjFileTemp = null;
					}
						
			}
			catch (Exception ex) 
			{
				PersonalLoanS.mLogger.info("Error in reading file = " + ex);
			}
			finally{
				try {
					reader.close();
					PersonalLoanS.mLogger.info("Closing reader stream");
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				try {
					out.close();
					PersonalLoanS.mLogger.info("Closing writer stream " );
					PersonalLoanS.mLogger.info("WriteFlag :: @nikhil" +  WriteFlag);
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
			try {
			}catch(Exception e){
				e.printStackTrace();
			}
			return WriteFlag;
			
		}
		//below function by jahnavi for downloading three reports on fpu button
		private  String writeToFilefpu(Map<String,List<String>> finaldoctype,List<List<String>> alldoclist)  {
			PersonalLoanS.mLogger.info("inside writetofilefpu");
			PersonalLoanS.mLogger.info("inside writetofilefpu"+finaldoctype);
			
			List<String> rej_list= new ArrayList<String>();
			List<String> loan_list= new ArrayList<String>();
			List<String> comp_list= new ArrayList<String>();
			rej_list=finaldoctype.get("REJECTED_LOAN_PL");
			loan_list=finaldoctype.get("LOAN_APPROVED_PL");
			comp_list=finaldoctype.get("COMPANY_MISMATCH_PL");
			PersonalLoanS.mLogger.info("now check for all object jahnavi"+"rej_list"+rej_list+"loan_list"+loan_list+"commppp"+comp_list);
			// fetching path
			String path_rej="";
			String path_loan="";
			String path_comp="";
			ArrayList<String> arrrej=new ArrayList<String>();
			ArrayList<String> arrloan=new ArrayList<String>();
			ArrayList<String> arrcomp=new ArrayList<String>();
			String rej_file="";
			String comp_file="";
			String loan_file="";
			try{
			PersonalLoanS.mLogger.info("size of list"+rej_list.size());
			PersonalLoanS.mLogger.info("value of list"+rej_list.get(0));
			 path_rej=rej_list.get(0);
			 path_loan=loan_list.get(0);
			 path_comp=comp_list.get(0);
			PersonalLoanS.mLogger.info("now check for all path jahnavi"+path_rej+path_loan+path_comp);
			
			
			
			//fetching filename
			 rej_file=rej_list.get(1);
			 comp_file=comp_list.get(1);
			 loan_file=loan_list.get(1);
			PersonalLoanS.mLogger.info("now check for all filename jahnavi"+rej_file+comp_file+loan_file);
			//fetching data
			
			PersonalLoanS.mLogger.info("alldoclist"+alldoclist+" "+alldoclist.size()+"size"+alldoclist.get(0).get(0)+" "+alldoclist.get(0).size());
			arrrej.addAll( alldoclist.get(0));
			arrloan.addAll( alldoclist.get(1));
			arrcomp.addAll( alldoclist.get(2));
			}
			catch(Exception e)
			{
				PersonalLoanS.mLogger.info("exception"+e);
			}
			PersonalLoanS.mLogger.info("now check for all arraylist"+arrrej+" "+arrloan+" "+arrcomp);
			String path="";
			List<String> list= new ArrayList<String>();
			String FileName="";
			BufferedWriter out = null;   
			BufferedReader reader = null;
			StringBuilder Newpath = new StringBuilder(path);
			String WriteFlag = "true";
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String PL_Wi_Name = formObject.getWFWorkitemName();
			String logFilePath=System.getProperty("user.dir")+File.separator+"FPU_Reports"+File.separator+PL_Wi_Name;				
			StringBuffer sb=new StringBuffer();
			StringBuffer sbdoc=new StringBuffer();
			String docStatus="";String outputDoc="";String docIndex="";String strDocISIndex="";
			try {
				for(int j=0;j<3;j++)
				{	
					list.clear();
					if(j==0)
					{
						path=path_rej;
						FileName=rej_file;
						list=arrrej;
					}
					else	if(j==1)
					{
						path=path_loan;
						FileName=loan_file;
						list=arrloan;
					}
					else	if(j==2)
					{
						path=path_comp;
						FileName=comp_file;
						list=arrcomp;
					}
				PersonalLoanS.mLogger.info("path writeToFile= " + path+FileName+list);
				File d = new File(logFilePath);
					if (!d.exists()) 
					{
						d.mkdir();
					}
					PersonalLoanS.mLogger.info("File exist " +d.exists()  );		

					File file = new File(Newpath.toString());
					if(!file.exists()){
						file.createNewFile();
						PersonalLoanS.mLogger.info("File exist " );		

					}
					
					PrintWriter writer = new PrintWriter(file);	
					writer.print("");
					writer.close();

				
					reader = new BufferedReader(new FileReader(file)); 
					out = new BufferedWriter(new FileWriter(file,true));
					//String line;
					PersonalLoanS.mLogger.info("Before if of write to file function " );		
					PersonalLoanS.mLogger.info("Inside if of write to file function " );
					for (Object s : list) {
						out.write(s.toString());
						out.newLine();
						out.flush();
					}	
					out.close();
					reader.close();

					String DocName = FileName ;	
					//String jtsIP = "10.15.11.147"; String jtsPort = "3333"; String cabinetName= "cas"; String volumeID = "1"; String Name = formObject.getUserName(); String password = ""; String sessionID = formObject.getWFSessionId(); String serverPort = "2809";
					String jtsIP = FormContext.getCurrentInstance().getFormConfig().getConfigElement("JTSIP");
					String jtsPort = "3333"; 
					String cabinetName=FormContext.getCurrentInstance().getFormConfig().getConfigElement("EngineName"); 
					String volumeID = FormContext.getCurrentInstance().getFormConfig().getConfigElement("VolumeId"); 
					String Name = formObject.getUserName(); String password = ""; 
					String sessionID = formObject.getWFSessionId(); 
					String serverPort = "2809";
					String status=attachDocument(path,PL_Wi_Name,DocName,jtsIP,jtsPort,cabinetName,volumeID,Name,password,sessionID,serverPort);
					String[] statusAttachArr =status.split("~"); 
					
					PersonalLoanS.mLogger.info("check for docname by jahnavi"+DocName);
					
					for(int k=0;k<statusAttachArr.length;k++)
					{
						if(k==0)
						{
							docStatus=statusAttachArr[k];
							PersonalLoanS.mLogger.info("check for docStatus by jahnavi"+docStatus);
						}
						if(k==1)
						{
							outputDoc=statusAttachArr[k];
							sbdoc.append(outputDoc);
							sbdoc.append("$");
						}
						if(k==2)
						{
							docIndex=statusAttachArr[k];
							sb.append(docIndex);   // for appending all indices 
							sb.append(",");
						}
						if(k==3)
							strDocISIndex=statusAttachArr[k];
					 }
				}
					formObject.setNGValue("cmplx_emp_ver_sp2_inputxml","");
					
					formObject.setNGValue("cmplx_emp_ver_sp2_docsize","");
					
					PersonalLoanS.mLogger.info("docstatus"+docStatus);
					 if(docStatus.equalsIgnoreCase("Y")){
							formObject.setNGValue("cmplx_emp_ver_sp2_inputxml",sbdoc.toString());
						
							formObject.setNGValue("cmplx_emp_ver_sp2_docsize",sb.toString() );
							PersonalLoanS.mLogger.info(sb.toString()+"this is for docindex all"+sbdoc.toString());
						}
					 /*  if(docStatus.equalsIgnoreCase("Y") && "LOAN_APPROVED_PL".equalsIgnoreCase(DocName)){
						formObject.setNGValue("cmplx_emp_ver_sp2_inputxml",outputDoc);
						// docIndex=formObject.getNGValue("cmplx_emp_ver_sp2_docsize")+","+docIndex;
						formObject.setNGValue("cmplx_emp_ver_sp2_docsize", sb.toString());
						PersonalLoanS.mLogger.info(sb.toString()+"this is for docindex loan");
					}
					
					 if(docStatus.equalsIgnoreCase("Y") && "COMPANY_MISMATCH_PL".equalsIgnoreCase(DocName)){
						formObject.setNGValue("cmplx_emp_ver_sp2_inputxml_comp",outputDoc);
						 //docIndex=formObject.getNGValue("cmplx_emp_ver_sp2_docsize")+","+docIndex;
						formObject.setNGValue("cmplx_emp_ver_sp2_docsize",sb.toString() );
						PersonalLoanS.mLogger.info(sb.toString()+"this is for docindex company");
					}*/
					PersonalLoanS.mLogger.info(sb.toString()+"this is for docindex");
					Custom_fragmentSave("Employment_Verification");
					

					
					
					PersonalLoanS.mLogger.info("Inside if of write to file function "+docStatus +"    "+outputDoc+"    "+docIndex+"    "+ strDocISIndex);
					File lobjFileTemp = new File(Newpath.toString());
					File deleteFolder = new File(logFilePath);
					if (lobjFileTemp.exists()) 
					{
						if (!lobjFileTemp.isDirectory()) 
						{
							boolean test2=lobjFileTemp.delete();
							deleteDir(deleteFolder);
						}
					}else 
					{
						lobjFileTemp = null;
					}
						
			}
			catch (Exception ex) 
			{
				PersonalLoanS.mLogger.info("Error in reading file = " + ex);
			}
			finally{
				try {
					reader.close();
					PersonalLoanS.mLogger.info("Closing reader stream");
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				try {
					out.close();
					PersonalLoanS.mLogger.info("Closing writer stream " );
					PersonalLoanS.mLogger.info("WriteFlag :: @nikhil" +  WriteFlag);
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
			try {
			}catch(Exception e){
				e.printStackTrace();
			}
			return WriteFlag;
			
		}

 public static boolean deleteDir(File dir)
 {
        if (dir.isDirectory()) 
        {
            String[] lstrChildren = dir.list();
            for (int i = 0; i < lstrChildren.length; i++) 
            {
                boolean success = deleteDir(new File(dir, lstrChildren[i]));
                if (!success) 
                {
                    return false;
                }
            }
        }
        return dir.delete();
 }
		  

public static String attachDocument(String filetobeaddedpath,String workItemName,String DocName,String jtsIP, String jtsPort, String cabinetName,  String volumeID, String userName, String password, String sessionID, String serverPort)
{
	try
	{	  PersonalLoanS.mLogger.info("Inside Attach Document " );
		  int sessionCheckInt=0;
		  int loopCount=50;
		  int waitLoop=50;
		String wiAddDocumentOutputXML="";
		String docIndex="";
		if(sessionID==null)
		{
			//sessionID=getSessionIDFirstTime(cabinetName,jtsIP, jtsPort,userName, password);
			return  "N";
		}
		
		if("".equalsIgnoreCase(filetobeaddedpath)||"".equalsIgnoreCase(workItemName)||"".equalsIgnoreCase(DocName))
		{
			return  "N";
		}
		XMLParser objXMLParser = new XMLParser();
		Integer intISIndex;
		int intVolumeId;
		JPISIsIndex IsIndex = new JPISIsIndex();
		JPDBRecoverDocData JPISDEC = new JPDBRecoverDocData();
		JPISDEC.m_cDocumentType = 'N';
		JPISDEC.m_sVolumeId = Short.parseShort(volumeID);
		File fppp = new File(filetobeaddedpath);
		String DocAttach="Y";
		
		
		
		String strDocISIndex = null;
		if(fppp.exists())
		{
			PersonalLoanS.mLogger.info("fpp exists : "+fppp.getPath());
		}
		else
		{
			PersonalLoanS.mLogger.info("fpp does not exists");
			DocAttach="N";
		}
		if(!"N".equalsIgnoreCase(DocAttach))
		{
			if(fppp.isFile())
			{
				PersonalLoanS.mLogger.info("fpp is file");
			}
			else
			{
				PersonalLoanS.mLogger.info("fpp is not file");
				DocAttach="N";
			}
		}
		if(!"N".equalsIgnoreCase(DocAttach))
		{
			PersonalLoanS.mLogger.info("Before AddDocument_MT Completion");
			try
			{
				//PersonalLoanS.mLogger.info("aaaaaaaaa : "+jtsIP);
				//PersonalLoanS.mLogger.info("aaaaaaaaa : "+jtsPort);
				//PersonalLoanS.mLogger.info("aaaaaaaaa : "+cabinetName);
				PersonalLoanS.mLogger.info("AddDocument_MT check 1");

				if(jtsPort.startsWith("33"))
				{
					CPISDocumentTxn.AddDocument_MT(null,jtsIP,(short)(Integer.parseInt(jtsPort)),cabinetName,JPISDEC.m_sVolumeId, fppp.getPath(), JPISDEC, "",IsIndex);
				}	
				else
				{
					CPISDocumentTxn.AddDocument_MT(null,jtsIP,(short)(Integer.parseInt(jtsPort)),cabinetName,JPISDEC.m_sVolumeId, fppp.getPath(), JPISDEC, "","JNDI" ,IsIndex);
				}
				PersonalLoanS.mLogger.info("AddDocument_MT check 3");

				strDocISIndex = String.valueOf(IsIndex.m_nDocIndex) + "#" + String.valueOf(IsIndex.m_sVolumeId);
				PersonalLoanS.mLogger.info("Document added in SMS: strDocISIndex: "+ strDocISIndex);
				PersonalLoanS.mLogger.info("AddDocument_MT Completed successfully");
			}
			catch(Exception e)
			{
				PersonalLoanS.mLogger.info("Exception in CPISDocumentTxn");
				DocAttach="N";
			}
			catch(JPISException e)
			{
				PersonalLoanS.mLogger.info("Exception in CPISDocumentTxn : "+e);
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				PersonalLoanS.mLogger.info("Exception in CPISDocumentTxn 2 : "+sw);
				DocAttach="N";
			}
		}
		if(!"N".equalsIgnoreCase(DocAttach))
		{	PersonalLoanS.mLogger.info("Inside if else : ");
			intISIndex=(int)IsIndex.m_nDocIndex;
			intVolumeId=(int)IsIndex.m_sVolumeId;

			String sQueryFoldIndex = "SELECT FOLDERINDEX FROM PDBFOLDER with(nolock) WHERE NAME ='" + workItemName + "'";
			String APSelectOutputFoldIndex="";
			sessionCheckInt=0;
			while(sessionCheckInt<loopCount)
			{
				String APSelectInputFoldIndex = ExecuteQuery_APSelect(sQueryFoldIndex,cabinetName,sessionID);					
				
				try
				{
					APSelectOutputFoldIndex=WFNGExecute(APSelectInputFoldIndex,jtsIP,serverPort,1);
					PersonalLoanS.mLogger.info("APSelectOutputFoldIndex : " + APSelectOutputFoldIndex);
				}
				catch(Exception e)
				{
					PersonalLoanS.mLogger.info("Exception in Execute : " + e);
					sessionCheckInt++;
					waiteloopExecute(waitLoop);
					
					continue;
				}
				
				sessionCheckInt++;
				if ("11".equalsIgnoreCase(getTagValue(APSelectOutputFoldIndex,"MainCode")))
				{
					
					PersonalLoanS.mLogger.info("Session id blank in sQueryFoldIndex call : " );
				}
				else
				{
					sessionCheckInt++;
					break;
				}
			}	
			objXMLParser.setInputXML(APSelectOutputFoldIndex);
			String mainCodeAPSelectFoldIndex = "";
			mainCodeAPSelectFoldIndex=objXMLParser.getValueOf("MainCode");
			if (!"0".equalsIgnoreCase(mainCodeAPSelectFoldIndex))
			{
				PersonalLoanS.mLogger.info("Problem in APSELECT mainCodeAPSelectFoldIndex");
				DocAttach="N";
			}
			if(!"N".equalsIgnoreCase(DocAttach))
			{
				String folderIndex=objXMLParser.getValueOf("td");
				//PersonalLoanS.mLogger.info("folderIndex : "+folderIndex);
				String APSelectOutputDocSize="";
				sessionCheckInt=0;
				while(sessionCheckInt<loopCount)
				{
					String sQueryDocSize = "select DOCSIZE from isdoc with (nolock) WHERE docindex ='" + intISIndex + "' and volumeid = '"+volumeID+"' order by docsize desc";
					String APSelectInputDocSize = ExecuteQuery_APSelect(sQueryDocSize,cabinetName,sessionID);
					
					
					try
					{
						APSelectOutputDocSize=WFNGExecute(APSelectInputDocSize,jtsIP,serverPort,1);
						PersonalLoanS.mLogger.info("APSelectOutputDocSize : " + APSelectOutputDocSize);

					}
					catch(Exception e)
					{
						PersonalLoanS.mLogger.info("Exception in Execute : " + e);
						sessionCheckInt++;
						waiteloopExecute(waitLoop);
						
						continue;
					}					
					
					sessionCheckInt++;
					if ("11".equalsIgnoreCase(getTagValue(APSelectOutputDocSize,"MainCode")))
					{
						
						PersonalLoanS.mLogger.info("Session id blank in sQueryDocSize call : " );
					}
					else
					{
						sessionCheckInt++;
						break;
					}
				}
				objXMLParser.setInputXML(APSelectOutputDocSize);
				String mainCodeAPSelectDocSize = "";
				mainCodeAPSelectDocSize=objXMLParser.getValueOf("MainCode");
				if (!"0".equalsIgnoreCase(mainCodeAPSelectDocSize))
				{
					PersonalLoanS.mLogger.info("Problem in APSELECT mainCodeAPSelectDocSize");					
					DocAttach="N";
				}
				if(!"N".equalsIgnoreCase(DocAttach))
				{
					String docSize="";
					
					docSize=objXMLParser.getValueOf("td");

					//PersonalLoanS.mLogger.info("docSize : "+docSize);

					String strISIndex =String.valueOf(JPISDEC.m_nDocIndex)+ "#"+ String.valueOf(JPISDEC.m_sVolumeId)+ "#" ;
					//PersonalLoanS.mLogger.info("strISIndex "+strISIndex);
					String intISIndexVolId = intISIndex.toString() +"#"+intVolumeId+"#";
					//PersonalLoanS.mLogger.info("intISIndexVolId "+intISIndexVolId);
					
					sessionCheckInt=0;
					while(sessionCheckInt<loopCount)
					{
						String wiAddDocumentInputXML = get_NGOAddDocument_Input(cabinetName,sessionID,folderIndex,docSize,DocName,intISIndexVolId,volumeID, fppp.getPath());

						
						
						try
						{
							wiAddDocumentOutputXML = WFNGExecute(wiAddDocumentInputXML,jtsIP,serverPort,1);
							PersonalLoanS.mLogger.info("wiAddDocumentOutputXML: " + wiAddDocumentOutputXML );

							
						}
						catch(Exception e)
						{
							PersonalLoanS.mLogger.info("Exception in Execute : " + e);
							sessionCheckInt++;
							waiteloopExecute(waitLoop);
							
							continue;
						}					
						
						sessionCheckInt++;
						if ("11".equalsIgnoreCase(getTagValue(wiAddDocumentOutputXML,"MainCode")))
						{
							
							PersonalLoanS.mLogger.info("Session id blank in get_NGOAddDocument_Input call : " );
						}
						else
						{
							sessionCheckInt++;
							break;
						}
					}	
					objXMLParser.setInputXML(wiAddDocumentOutputXML);
					String mainCodeforCreateWI = "";
					mainCodeforCreateWI=objXMLParser.getValueOf("Status");
					if (!"0".equalsIgnoreCase(mainCodeforCreateWI))
					{
						DocAttach="N";						
					}
					else
					{
						docIndex=objXMLParser.getValueOf("DocumentIndex");
						DocAttach="Y";
						DocAttach = DocAttach+"~"+wiAddDocumentOutputXML+"~"+docIndex+"~"+strDocISIndex;						 
						
					}
				}
			}
		}
		PersonalLoanS.mLogger.info("intISIndexVolId "+DocAttach);

		return DocAttach;
	}
	catch(Exception e)
	{
		return  "N";
	}
}

public static String ExecuteQuery_APSelect(String sQuery,String sEngineName,String sSessionId)
{
	WFInputXml wfInputXml = new WFInputXml();

	wfInputXml.appendStartCallName("APSelect", "Input");
	wfInputXml.appendTagAndValue("Query",sQuery);
	wfInputXml.appendTagAndValue("EngineName",sEngineName);
	wfInputXml.appendTagAndValue("SessionId",sSessionId);
	wfInputXml.appendEndCallName("APSelect","Input");
	return wfInputXml.toString();
}

public static String get_NGOAddDocument_Input(String cabinetName, String sessionID, String folderIndex,String docSize,String DocumentName,String strISIndex, String volumeID, String filePath)
{
	String noOfPage = "1";
	int ext_index = DocumentName.lastIndexOf('.');
	String File_ext = DocumentName.substring(ext_index+1);
	DocumentName = DocumentName.substring(0, ext_index);
			
	String xml = "<?xml version=\"1.0\"?>" + "\n" +
	"<NGOAddDocument_Input>" + "\n" +
	"<Option>NGOAddDocument</Option>" + "\n" +
	"<CabinetName>" + cabinetName + "</CabinetName>" + "\n" +
	"<UserDBId>" + sessionID + "</UserDBId>" + "\n" +
	"<GroupIndex>0</GroupIndex>" + "\n" +
	"<ParentFolderIndex>" + folderIndex + "</ParentFolderIndex>" + "\n" +
	"<DocumentName>" + DocumentName + "</DocumentName>" + "\n" +
	"<CreatedByAppName>"+File_ext+"</CreatedByAppName>" + "\n" +
	"<Comment></Comment>" + "\n" +
	"<VersionComment></VersionComment>" + "\n" +
	"<VolumeIndex>" + volumeID + "</VolumeIndex>" + "\n" +
	"<FilePath>" + filePath + "</FilePath>" + "\n" +
	"<DataDefinition></DataDefinition>" + "\n" +
	"<ISIndex>" + strISIndex + "</ISIndex>" + "\n" +
	"<NoOfPages>"+noOfPage+"</NoOfPages>" + "\n" +
	"<DocumentType>N</DocumentType>" + "\n" +
	"<DocumentSize>" + docSize + "</DocumentSize>" + "\n" +
	"</NGOAddDocument_Input>";
	   
	return xml;   
}



private static String WFNGExecute(String ipXML, String jtsServerIP, String serverPort,int flag) throws IOException
{
	
	try{
		
	if(serverPort.startsWith("33"))
		return WFCallBroker.execute(ipXML,jtsServerIP,Integer.parseInt(serverPort),1);
	else
	{
		return NGEjbClient.getSharedInstance().makeCall(jtsServerIP,serverPort,"WebSphere",ipXML); 
	}
		//
	}
	catch(Exception e){
		PersonalLoanS.mLogger.info("Exception Occured in WF NG Execute : "+e.getMessage());	
		return "Error";
	}
	
}


private static void waiteloopExecute(long wtime)
{
    try
	{
        for (int i = 0; i < 10; i++)
		{
            Thread.yield();
            Thread.sleep(wtime / 10);
        }
    }
	catch (InterruptedException e)
	{
		PersonalLoanS.mLogger.info(e.toString());
	   Thread.currentThread().interrupt();
    }
}

public static String getTagValue(String xml,String tag) throws ParserConfigurationException, SAXException, IOException
{
	Document doc=getDocument(xml);
	NodeList nodeList = doc.getElementsByTagName(tag);

	int length = nodeList.getLength();

	if (length > 0)
	{
		Node node =  nodeList.item(0);
		
		if (node.getNodeType() == Node.ELEMENT_NODE)
		{
			NodeList childNodes = node.getChildNodes();
			String value = "";
			int count = childNodes.getLength();
			for (int i = 0; i < count; i++)
			{
				Node item = childNodes.item(i);
				if (item.getNodeType() == Node.TEXT_NODE)
				{
					value += item.getNodeValue();
				}
			}
			return value;
		}
		else if (node.getNodeType() == Node.TEXT_NODE)
		{
			return node.getNodeValue();
		}
	}
	return "";
}

public static Document getDocument(String xml) throws ParserConfigurationException, SAXException, IOException
{
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	DocumentBuilder db = dbf.newDocumentBuilder();
	Document doc = db.parse(new InputSource(new StringReader(xml)));
	return doc;
}

private String VaildateSkyward(String buttonId) {
	FormReference formObject = FormContext.getCurrentInstance().getFormReference();
	String alert_msg = "";
	String outputResponse = "";
	String   ReturnCode="";
	PL_Integration_Input genX=new PL_Integration_Input();
	String Customer_type = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12);
	if("PRIMARY".equalsIgnoreCase(Customer_type) || buttonId.contains("Limit_Inc"))
	{
		outputResponse = genX.GenerateXML("ENROLL_REWARDS","PRIMARY");
	}
	else {
		outputResponse = genX.GenerateXML("ENROLL_REWARDS","SUPPLEMENT");
	} 
	ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
	if("0000".equalsIgnoreCase(ReturnCode))
	{
		if("PRIMARY".equalsIgnoreCase(Customer_type)){
			String MembershipNumber =  (outputResponse.contains("<MembershipNumber>")) ? outputResponse.substring(outputResponse.indexOf("<MembershipNumber>")+"</MembershipNumber>".length()-1,outputResponse.indexOf("</MembershipNumber>")):"";
			formObject.setNGValue("AlternateContactDetails_EnrollRewardsIdentifier", MembershipNumber);
			formObject.setNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),17,"Y");
			Custom_fragmentSave("Alt_Contact_container");
			alert_msg="Validation Successfull";
		}
		else{
			String MembershipNumber =  (outputResponse.contains("<MembershipNumber>")) ? outputResponse.substring(outputResponse.indexOf("<MembershipNumber>")+"</MembershipNumber>".length()-1,outputResponse.indexOf("</MembershipNumber>")):"";
			for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_supplementGrid");i++){
				if(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13).equals(formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,3))){
					formObject.setNGValue("SupplementCardDetails_cmplx_supplementGrid",i,42,MembershipNumber);
					break;
				}
			}
			formObject.setNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),17,"Y");
			Custom_fragmentSave("Supplementary_Cont");
			alert_msg="Validation Successfull";
		}
		
	}
	else
	{
		formObject.setNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),17,"N");
		alert_msg="Validation Failed";
	}
	Custom_fragmentSave("CC_Creation");
	Custom_fragmentSave("Alt_Contact_container");
	return alert_msg;
}

public HashMap<String, String> GetCustomer_Details(){
	HashMap<String, String> GridDataMap = new HashMap<String, String>();
	try{
		GridDataMap.put("COMPANYFLAG", "N");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		GridDataMap.put("FIRSTNAME",formObject.getNGValue("cmplx_Customer_FIrstNAme"));
		GridDataMap.put("MIDDLENAME",formObject.getNGValue("cmplx_Customer_MiddleName"));
		GridDataMap.put("LASTNAME",formObject.getNGValue("cmplx_Customer_LAstNAme"));
		GridDataMap.put("GENDER",formObject.getNGValue("cmplx_Customer_gender"));
		GridDataMap.put("DATEOFBIRTH",formObject.getNGValue("cmplx_Customer_DOb"));
		GridDataMap.put("COUNTRYOFRESIDENCE",formObject.getNGValue("cmplx_Customer_COuntryOFResidence"));
		GridDataMap.put("PASSPORTNUMBER",formObject.getNGValue("cmplx_Customer_PAssportNo"));
		GridDataMap.put("NATIONALITY",formObject.getNGValue("cmplx_Customer_Nationality"));							
		GridDataMap.put("CIF",formObject.getNGValue("cmplx_Customer_CIFNO"));
	}
	catch(Exception e){
		PersonalLoanS.mLogger.info("Exception Occured in GetCustomer_Details: "+e.getMessage());
	}
	return GridDataMap;
}

private Double maxAmountAllowed(FormReference formObject) {
	String typeOfProd = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0);
	String transtype= formObject.getNGValue("transtype");
	String paypurpose = formObject.getNGValue("PaymentPurpose");
	String cardProduct = formObject.getNGValue("cmplx_CC_Loan_BTC_CardProduct");
	String query;
	Double balTaken=0.0,availBal=0.0;
	int rowSelected = formObject.getSelectedIndex("cmplx_CC_Loan_cmplx_btc");
	try{
	if(!("Conventional".equalsIgnoreCase(typeOfProd)&&"CCC".equalsIgnoreCase(transtype))){
		paypurpose="NA";
	}
	
	if("Conventional".equalsIgnoreCase(typeOfProd)){
		query="select top 1 cast(cast(Final_Limit As float)*(select Percentage from NG_MASTER_ServiceMaxAmount where CardProduct='"+typeOfProd+"' and TransactionType='"+transtype+"' and PaymentPurpose='"+paypurpose+"')/100 AS float)from ng_rlos_IGR_Eligibility_CardLimit where Child_Wi='"+formObject.getWFWorkitemName()+"' and Cardproductselect='true'";		
		for(int i=0;i<formObject.getLVWRowCount("cmplx_CC_Loan_cmplx_btc");i++){
			if(rowSelected!=i)
			balTaken += Double.parseDouble(formObject.getNGValue("cmplx_CC_Loan_cmplx_btc", i,6));
		}
	}
	else{
		query="select top 1 cast(cast(Final_Limit As float)*(select Percentage from NG_MASTER_ServiceMaxAmount where CardProduct='"+typeOfProd+"' and TransactionType='"+transtype+"' and PaymentPurpose='"+paypurpose+"')/100 AS float)from ng_rlos_IGR_Eligibility_CardLimit where Child_Wi='"+formObject.getWFWorkitemName()+"' and Card_Product='"+cardProduct+"' and Cardproductselect='true'";
		for(int i=0;i<formObject.getLVWRowCount("cmplx_CC_Loan_cmplx_btc");i++){
			if(formObject.getNGValue("cmplx_CC_Loan_cmplx_btc", i,19).equalsIgnoreCase(cardProduct)&& rowSelected!=i)
			balTaken += Double.parseDouble(formObject.getNGValue("cmplx_CC_Loan_cmplx_btc", i,6));
		}
	}
	//CreditCard.mLogger.info("Query to Calculate Amount:"+query);
	List<List<String>> result =	formObject.getDataFromDataSource(query);
	availBal = Double.parseDouble(result.get(0).get(0))-balTaken; 
	}catch (Exception e) {
		PersonalLoanS.mLogger.info("Available Balance:"+e.getMessage());
	}
	PersonalLoanS.mLogger.info("Available Balance:"+availBal);
	return availBal;	




}
	/*public void generateRiskRatingReport(FormReference formObject){
		Document document = new Document();
		OutputStream out = new FileOutputStream(new File(""));
		PdfWriter.getInstance(document , out);
		document.open();
		
		PdfPTable pdfPTable = new PdfPTable(2);
		for(int i=0; i< 20 ; i++){
			PdfPCell pdfCell1 = new PDFCell(new Paragraph(""));
			PdfPCell pdfCell2 = new PDFCell(new Paragraph(""));
			
			pdfPTable.addCell(pdfCell1);
			pdfPTable.addCell(pdfCell2);
		}
		document.add(pdfPTable);
		document.close();
		out.close();
		
	}*/
		
	}