package com.newgen.omniforms.user;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;


import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.validator.ValidatorException;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.newgen.custom.Common_Utils;
import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.Column;
import com.newgen.omniforms.component.IRepeater;
import com.newgen.omniforms.component.ListView;
import com.newgen.omniforms.component.PickList;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.util.Common;
import com.newgen.omniforms.util.PL_EventListenerHandler;



public class PLCommon extends Common implements PLConstant{
	private static final long serialVersionUID = 1L;
	static Logger mLogger=PersonalLoanS.mLogger;
	final static double epsilon=0.0000001;
	List<String> Supplementary = null;
	String tabName = "Tab1";
	/*          Function Header:

	 **********************************************************************************
DecisionHistory_CifLock
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to calculate Age from DOB

	 ***********************************************************************************  */

	/*public void getAge(String dateBirth,String controlName){
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			PersonalLoanS.mLogger.info("RLOS_Common123"+ "Inside getAge(): "); 
			Calendar dob = Calendar.getInstance();
			Calendar today = Calendar.getInstance();
			String[] parts;
			if (dateBirth.contains("/")){
				parts= dateBirth.split("/");
				dob.set(Integer.parseInt(parts[2]), Integer.parseInt(parts[1])-1,Integer.parseInt(parts[0])); 
			}

			else if(dateBirth.contains("-")) {
				 parts = dateBirth.split("-");
				dob.set(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])-1,Integer.parseInt(parts[2])); 
			}
				Integer age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
				PersonalLoanS.mLogger.info("age=="+age);
				int month=today.get(Calendar.MONTH) - dob.get(Calendar.MONTH);
				PersonalLoanS.mLogger.info("month=="+month);
				if (month<0){
					age--; 
					month= 12-dob.get(Calendar.MONTH) + today.get(Calendar.MONTH);
					if(today.get(Calendar.DATE) < dob.get(Calendar.DATE)){
						month=month-1;
					}
				}
				else if(month == 0 && today.get(Calendar.DATE) < dob.get(Calendar.DATE)) {
					age--;
					month= 11-dob.get(Calendar.MONTH) + today.get(Calendar.MONTH);//If month is same as current no need to count it
				}
				else if(1<=month && month<=11 && today.get(Calendar.DATE) < dob.get(Calendar.DATE)){
					month=month-1;
				}

				String month1=Integer.toString(month);
				if (month1.length()==1){
					month1="0"+month1;
					if (month1.equalsIgnoreCase("00")){
						month1="01";
					}
				}
				PersonalLoanS.mLogger.info("Age is====== "+age+"."+month);

				formObject.setNGValue(controlName,(age+"."+month1).toString(),false); 


		}

		catch(Exception e){
			PersonalLoanS.mLogger.info("Exception:"+e.getMessage());
			printException(e);
		}
}
	commented by akshay on 10/7/18*/
	//SVN Commit test
	
	
	//added by rishabh
	public void cpv_Decision(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PersonalLoanS.mLogger.info("1234");
		formObject.setTop("Decision_Label3", 10);
		
		formObject.setLeft("Decision_Label3", 25);
		formObject.setVisible("Decision_Label3", true);
		PersonalLoanS.mLogger.info("@@@1234");
		formObject.setTop("cmplx_Decision_Decision", formObject.getTop("Decision_Label3")+formObject.getHeight("Decision_Label3"));
		formObject.setLeft("cmplx_Decision_Decision", 25);

		formObject.setTop("DecisionHistory_Label11", 10);
		formObject.setLeft("DecisionHistory_Label11", formObject.getLeft("Decision_Label1")+275);
		formObject.setTop("DecisionHistory_DecisionReasonCode",formObject.getTop("DecisionHistory_Label11")+formObject.getHeight("DecisionHistory_Label11"));
		formObject.setLeft("DecisionHistory_DecisionReasonCode",formObject.getLeft("Decision_Label1")+275);

		formObject.setTop("DecisionHistory_Label35",10);
		formObject.setLeft("DecisionHistory_Label35",formObject.getLeft("DecisionHistory_Label11")+275);
		/*formObject.setTop("cmplx_DEC_NoofAttempts", formObject.getTop("DecisionHistory_Label12")+formObject.getHeight("DecisionHistory_Label12"));
		formObject.setLeft("cmplx_DEC_NoofAttempts", formObject.getLeft("DecisionHistory_Label11")+275);
		 */
		
		//asked shweta for this
		formObject.setTop("Decision_Label4",10);
		formObject.setLeft("Decision_Label4",formObject.getLeft("DecisionHistory_Label35")+275);
		formObject.setTop("cmplx_Decision_RejectReason", formObject.getTop("Decision_Label4")+formObject.getHeight("Decision_Label4"));
		formObject.setLeft("cmplx_Decision_RejectReason",formObject.getLeft("DecisionHistory_Label35")+275);

		formObject.setTop("Decision_ListView1", formObject.getTop("Decision_Label4") +60 );
		formObject.setLeft("Decision_ListView1", 25);
		PersonalLoanS.mLogger.info("asdfadsf"+formObject.getTop("Decision_ListView1"));
		formObject.setVisible("DecisionHistory_save", true);
		formObject.setTop("DecisionHistory_save", formObject.getTop("Decision_ListView1")+formObject.getHeight("Decision_ListView1")+20);
		formObject.setLeft("DecisionHistory_save", 25);

		formObject.setHeight("DecisionHistory_Frame1", formObject.getTop("DecisionHistory_save") +70);
		formObject.setHeight("DecisionHistory", formObject.getHeight("DecisionHistory_Frame1") +5);
	}
	//end rishabh

	
	//bandana test for svn
	

	public String MultipleAppGridSelectedRow(String ColumnName){
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String column = getValueOfConstant(ColumnName);
			int colIndex = Integer.parseInt(column.split(":")[1]);
			String returnVal = formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),colIndex);
			return returnVal;
		}
		catch(Exception ex){
			PersonalLoanS.mLogger.info("Exception in MultipleAppGridSelectedRow() is: ");
			printException(ex);
			return "";
		}
	}

	public String getGridColumnValue(String columnName,String listviewName,int rowIndex){
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String column = getValueOfConstant(columnName);
			List<String> GridColumns = new ArrayList<String>();
			String returnVal ="";
			if(listviewName.contains("Supplement")){
				if(Supplementary==null){
					UIComponent pComp = formObject.getComponent(listviewName);
					ListView objListView = ( ListView )pComp;
					int columns = objListView.getChildCount();
					for(int j=0;j<columns;j++){
						GridColumns.add(((Column)(pComp.getChildren().get(j))).getName());
					}
					Supplementary = GridColumns;
				}
			}
			else{
				UIComponent pComp = formObject.getComponent(listviewName);
				ListView objListView = ( ListView )pComp;
				int columns = objListView.getChildCount();
				for(int j=0;j<columns;j++){
					GridColumns.add(((Column)(pComp.getChildren().get(j))).getName());
				}
			}

			PersonalLoanS.mLogger.info("GridColumns is: "+GridColumns);
			//int colIndex = Integer.parseInt(column.split(":")[1]);
			if(listviewName.contains("Supplement")){
				returnVal= formObject.getNGValue(listviewName,rowIndex,Supplementary.indexOf(column));	
			}
			else{
				returnVal= formObject.getNGValue(listviewName,rowIndex,GridColumns.indexOf(column));
			}

			PersonalLoanS.mLogger.info("returnVal for columnName:"+columnName+" is: "+returnVal);
			return returnVal;
		}
		catch(Exception ex){
			PersonalLoanS.mLogger.info("Exception in MultipleAppGridSelectedRow() is: ");
			printException(ex);
			return "";
		}
	}

	/*public int  getAge(String dateBirth){
		Integer age = null;
		String final_month = null;
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			mLogger.info("RLOS_Common123"+ "Inside getAge(): "); 
			Calendar dob = Calendar.getInstance();
			Calendar today = Calendar.getInstance();
			String[] parts;
			if (dateBirth.contains("/")){
				parts= dateBirth.split("/");
				dob.set(Integer.parseInt(parts[2]), Integer.parseInt(parts[1])-1,Integer.parseInt(parts[0])); 
			}

			else if(dateBirth.contains("-")) {
				 parts = dateBirth.split("-");
				dob.set(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])-1,Integer.parseInt(parts[2])); 
			}
				age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
				mLogger.info("age=="+age);
				int month=today.get(Calendar.MONTH) - dob.get(Calendar.MONTH);
				mLogger.info("month=="+month);
				if (month<0){
					age--; 
					month= 12-dob.get(Calendar.MONTH) + today.get(Calendar.MONTH);
					if(today.get(Calendar.DATE) < dob.get(Calendar.DATE)){
						month=month-1;
					}
				}
				else if(month == 0 && today.get(Calendar.DATE) < dob.get(Calendar.DATE)) {
					age--;
					month= 11-dob.get(Calendar.MONTH) + today.get(Calendar.MONTH);//If month is same as current no need to count it
				}
				else if(1<=month && month<=11 && today.get(Calendar.DATE) < dob.get(Calendar.DATE)){
					month=month-1;
				}
				 final_month=Integer.toString(month);
				if(final_month.length()<2){
					final_month="0"+final_month;
				}
				mLogger.info("Age is====== "+age+"."+final_month);
				return (age+"."+final_month).toString();
	}catch(Exception e){
		mLogger.info("Exception:"+e.getMessage());
		printException(e);
		return "";
	}


		String[] arr = dateBirth.split("/");
		String year = arr[2];
		String month = arr[1];
		String day = arr[0];
		LocalDate dob = new LocalDate(Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(day));
		LocalDate date = new LocalDate();
		Period period = new Period(dob,date,PeriodType.yearMonthDay());
		PersonalLoanS.mLogger.info("returning from getAge method for date"+dateBirth+": "+period.getYears());
		return period.getYears();
	}*/

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 29/05/2017)             
	Author                              : Tanshu Aggarwal              
	Description                         : Function added with respect to change in Customer_Details call     

	 ***********************************************************************************  */



	public void setcustomer_enable(){
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			formObject.setLocked("cmplx_Customer_Title", false);
			formObject.setLocked("cmplx_Customer_ResidentNonResident", false);
			formObject.setLocked("cmplx_Customer_gender", false);
			formObject.setLocked("cmplx_Customer_MotherName", false);
			formObject.setLocked("cmplx_Customer_VisaNo", false);
			formObject.setLocked("cmplx_Customer_MAritalStatus", false);
			formObject.setLocked("cmplx_Customer_COuntryOFResidence", false);
			formObject.setLocked("cmplx_Customer_SecNAtionApplicable", false);
			
			if("No".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_SecNAtionApplicable"))){
				formObject.setLocked("SecNationality_Button",true);
			}
			else{
				formObject.setLocked("SecNationality_Button",true);
			}
			formObject.setLocked("cmplx_Customer_Third_Nationaity_Applicable", false);
			//formObject.setLocked("Third_Nationality_Button", false);
			if("No".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_Third_Nationaity_Applicable"))){
				formObject.setLocked("Third_Nationality_Button",true);
			}
			else{
				formObject.setLocked("Third_Nationality_Button",false);
			}
			formObject.setLocked("cmplx_Customer_EMirateOfVisa", false);
			formObject.setLocked("cmplx_Customer_EmirateOfResidence", false);
			formObject.setLocked("cmplx_Customer_yearsInUAE", false);
			formObject.setLocked("cmplx_Customer_CustomerCategory", false);
			formObject.setLocked("cmplx_Customer_GCCNational", false);
			formObject.setLocked("cmplx_Customer_VIPFlag", false);
			formObject.setLocked("cmplx_Customer_Constitution", false);

		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("Exception occured while parsing Customer Eligibility : "+ e.getMessage());
			printException(e);
		}


	}

	/*public void loadDataInBlacklistgrid(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int rowCount = formObject.getLVWRowCount("cmplx_ExtBlackList_cmplx_gr_ExtBlackList");
		if(rowCount==0){
			String query = "select CONCAT(cust.FirstName,' ',cust.MiddleName,' ',cust.LAstName) as Fullname,cust.PassportNo,cif.CustId,cif.BlacklistFlag,cif.BlacklistDate,cif.BlacklistReasonCode from ng_rlos_cif_detail cif,ng_RLOS_Customer cust where cif_wi_name ='"+formObject.getNGValue("Parent_WIName")+"' and cif_wi_name=wi_name and cif_SearchType = 'External'";
			PersonalLoanS.mLogger.info("Query for External Blacklist is : "+query );
			List<List<String>> records = formObject.getNGDataFromDataCache(query);
			for(List<String> record : records){
				formObject.addItemFromList("cmplx_ExtBlackList_cmplx_gr_ExtBlackList", record);
			}
		}

	}*/

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 07/05/2017           
	Author                              : Disha           
	Description                         : Function to load combos on FCU Fragment    

	 ***********************************************************************************  */
	public void LoadpicklistAltcontactDetails()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PersonalLoanS.mLogger.info("PL_Common"+ "Inside loadPicklist");
		try{	
			//LoadPickList("AlternateContactDetails_carddispatch", "select '--Select--' as description,'' as code union all select convert(varchar,description),code from NG_MASTER_Dispatch order by code ");
			String TypeOfProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0)==null?"":formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0);
			LoadPickList("AlternateContactDetails_CustomerDomicileBranch", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock)  where product_type = '"+TypeOfProd+"' order by code");
		}catch(Exception ex){
			PersonalLoanS.mLogger.info("Exception occured in LoadpicklistAltcontactDetails(): for WI: "+ formObject.getWFWorkitemName());
			printException(ex);
		}
	}

	public void LoadpicklistCardDetails()
	{	//by shweta- corrected statementcycle id
		//LoadPickList("cmplx_CardDetails_Statement_cycle", "select '--Select--' as Description , '' as code union select convert(varchar, Description),code from NG_MASTER_StatementCycle with (nolock)");
		LoadPickList("cmplx_CardDetails_statcycle","select '--Select--'  as Description , '' as code union select convert(varchar, description),code from NG_MASTER_StatementCycle with (nolock)");
		
		LoadPickList("cmplx_CardDetails_MarketCode", " select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_MarketingCode where isActive='Y'  order by code");
		
		LoadPickList("cmplx_CardDetails_CustClassification","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_ClassificationCode with(nolock) where isActive='Y' order by code");
		//LoadPickList("CardDetails_BankName", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_BankName with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("CardDetails_TransactionFP","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_TransactionFeeProfile with(nolock) where isActive='Y' order by code");
		LoadPickList("CardDetails_InterestFP","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_InterestProfile with(nolock) where isActive='Y' order by code");
		LoadPickList("CardDetails_FeeProfile","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_feeprofile with(nolock) where isActive='Y' order by code");
		//LoadPickList("cmplx_CardDetails_MarketingCode","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_MarketingCode where isActive='Y' order by code");
		//++ Below Code added By abhishek on Oct 9, 2017  to fix : "42,43,44-Transaction fee profile masters are incorrect,intetrest fee profile masters are incorrect,fee profile masters are incorrect" : Reported By Shashank on Oct 05, 2017++

	}

	public void Loadpicklistfatca()
	{
		LoadPickList("FATCA_USRelaton", " select '--Select--' as description, '' as code  union select convert(varchar,description),code  from ng_master_usrelation with(nolock) order by code");
		LoadPickList("cmplx_FATCA_Category", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_category with (nolock) order by code");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		LoadPickList("cmplx_FATCA_CustomerType","SELECT * FROM (SELECT '--Select--' AS customername UNION ALL  SELECT 'P-'+FirstName+' '+LAstName  FROM ng_rlos_customer with(nolock) where wi_name='"+formObject.getWFWorkitemName()+"'   UNION ALL SELECT 'G-'+firstname+' '+lastname as customername FROM ng_RLOS_GR_GuarantorDetails with(nolock) where guarandet_wi_name='"+formObject.getWFWorkitemName()+"' and firstname IS NOT NULL  union ALL SELECT 'S-'+FistName+' '+lastname+'-'+passportNo as customername FROM ng_RLOS_GR_SupplementCardDetails with(nolock) WHERE  supplement_WIname='"+formObject.getWFWorkitemName()+"') as u where customername is not null");
		Map<String,String> ReasonMap =getFatcaReasons();
		List<String> selectedReasonList= new ArrayList<String>();
		for (String reason : ReasonMap.keySet()) {
			selectedReasonList.add(reason);
		}
		formObject.addItemFromList("cmplx_FATCA_ListedReason", selectedReasonList);

	}
	public void LoadpicklistFCU()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PersonalLoanS.mLogger.info("PL_Common"+ "Inside loadPicklist");
		String Feedback=formObject.getNGValue("cmplx_Decision_feedbackstatus");
		PersonalLoanS.mLogger.info("PL_Common"+ "Inside loadPicklist::"+Feedback);

		if("Positive".equalsIgnoreCase(Feedback))
		{
			
			formObject.setEnabled("cmplx_Decision_subfeedback", true);
			formObject.setNGValue("cmplx_Decision_subfeedback", "");
			formObject.setLocked("cmplx_Decision_ALOCtype", true);
			LoadPickList("cmplx_Decision_subfeedback", "select '--Select--' union all select convert(varchar, Description) from NG_Master_Possubfeedbackstatus with (nolock) where IsActive='Y'");
		}
		else if("Negative".equalsIgnoreCase(Feedback))
		{
			LoadPickList("cmplx_DEC_SubFeedbackStatus", "select '--Select--' union all select convert(varchar, Description) from NG_Master_Possubfeedbackstatus with (nolock) where IsActive='N'");
			//LoadPickList("cmplx_DEC_SubFeedbackStatus", "select '--Select--' union all select convert(varchar, Description) from NG_Master_subfeedbackstatus with (nolock) where IsActive='Y'");
			
			
			formObject.setEnabled("cmplx_Decision_subfeedback", true);
			formObject.setNGValue("cmplx_Decision_subfeedback", "");
			formObject.setLocked("cmplx_Decision_ALOCtype", true);
			LoadPickList("cmplx_Decision_subfeedback", "select '--Select--' union all select convert(varchar, Description) from NG_Master_subfeedbackstatus with (nolock) where IsActive='Y'");
			LoadPickList("cmplx_Decision_ALOCtype", "select Description from NG_MASTER_fraudsubreason ");

		}

		else{

			formObject.setEnabled("cmplx_Decision_subfeedback", false);
			formObject.setNGValue("cmplx_Decision_subfeedback", "");
			formObject.setLocked("cmplx_Decision_ALOCtype", true);
			formObject.RaiseEvent("WFSave");// by jahnavi for 3080
			//LoadPickList("cmplx_Decision_subfeedback", "select '--Select--' union all select convert(varchar, Description) from NG_Master_subfeedbackstatus with (nolock) where IsActive='Y'");
		}
	}//Arun (07/09/17)

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 07/05/2017           
	Author                              : Disha           
	Description                         : Function to Hide/Unhide Employment fields   

	 ***********************************************************************************  */

	public void Field_employment()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		String empid="AVI,MED,EDU,HOT,PROM";
		if(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode").equalsIgnoreCase(NGFUserResourceMgr_PL.getGlobalVar("PL_NEP"))){

			formObject.setVisible("EMploymentDetails_Label25",true);
			formObject.setVisible("cmplx_EmploymentDetails_NepType",true);
			formObject.setVisible("cmplx_EmploymentDetails_Freezone",false);
			formObject.setVisible("EMploymentDetails_Label62",false);
			formObject.setVisible("cmplx_EmploymentDetails_FreezoneName",false);
			formObject.setVisible("cmplx_EmploymentDetails_tenancntrct",false);
			formObject.setVisible("EMploymentDetails_Label5",false);
			formObject.setVisible("cmplx_EmploymentDetails_IndusSeg",false);
			formObject.setVisible("EMploymentDetails_Label59",false);
		}

		else if(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")==NGFUserResourceMgr_PL.getGlobalVar("PL_FZD") ||formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")==NGFUserResourceMgr_PL.getGlobalVar("PL_FZE")){
			formObject.setVisible("cmplx_EmploymentDetails_Freezone",true);
			formObject.setVisible("EMploymentDetails_Label62",true);
			formObject.setVisible("cmplx_EmploymentDetails_FreezoneName",true);
			formObject.setVisible("EMploymentDetails_Label25",false);
			formObject.setVisible("cmplx_EmploymentDetails_NepType",false);
			formObject.setVisible("cmplx_EmploymentDetails_tenancntrct",false);
			formObject.setVisible("EMploymentDetails_Label5",false);
			formObject.setVisible("cmplx_EmploymentDetails_IndusSeg",false);
			formObject.setVisible("EMploymentDetails_Label59",false);
		}

		else if(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")==NGFUserResourceMgr_PL.getGlobalVar("PL_TEN"))
		{
			formObject.setVisible("cmplx_EmploymentDetails_tenancntrct",true);
			formObject.setVisible("EMploymentDetails_Label5",true);
			formObject.setVisible("EMploymentDetails_Label25",false);
			formObject.setVisible("cmplx_EmploymentDetails_NepType",false);
			formObject.setVisible("cmplx_EmploymentDetails_Freezone",false);
			formObject.setVisible("EMploymentDetails_Label62",false);
			formObject.setVisible("cmplx_EmploymentDetails_FreezoneName",false);
			formObject.setVisible("cmplx_EmploymentDetails_IndusSeg",false);
			formObject.setVisible("EMploymentDetails_Label59",false);
		}

		else if(formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg")=="S" && empid.indexOf(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode"))>-1)
		{
			formObject.setVisible("cmplx_EmploymentDetails_IndusSeg",true);
			formObject.setVisible("EMploymentDetails_Label59",true);
			formObject.setVisible("EMploymentDetails_Label25",false);
			formObject.setVisible("cmplx_EmploymentDetails_NepType",false);
			formObject.setVisible("cmplx_EmploymentDetails_Freezone",false);
			formObject.setVisible("EMploymentDetails_Label62",false);
			formObject.setVisible("cmplx_EmploymentDetails_FreezoneName",false);
			formObject.setVisible("cmplx_EmploymentDetails_tenancntrct",false);
			formObject.setVisible("EMploymentDetails_Label5",false);
		}
		else{
			formObject.setVisible("EMploymentDetails_Label25",false);
			formObject.setVisible("cmplx_EmploymentDetails_NepType",false);
			formObject.setVisible("cmplx_EmploymentDetails_Freezone",false);
			formObject.setVisible("EMploymentDetails_Label62",false);
			formObject.setVisible("cmplx_EmploymentDetails_FreezoneName",false);
			formObject.setVisible("cmplx_EmploymentDetails_tenancntrct",false);
			formObject.setVisible("EMploymentDetails_Label5",false);
			formObject.setVisible("cmplx_EmploymentDetails_IndusSeg",false);
			formObject.setVisible("EMploymentDetails_Label59",false);
		}
				
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 07/05/2017           
	Author                              : Disha           
	Description                         : Function to make business verification visible   

	 ***********************************************************************************  */

	/*public void BussVerVisible()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		if(formObject.getNGValue("EmpType").equalsIgnoreCase(NGFUserResourceMgr_PL.getGlobalVar("PL_SelfEmployed"))){

			formObject.setVisible("Business_Verif",true);

		}
	}
*/
	public static String plusyear(String currentDate,int i,int j,int k){
		DateTime dt = new DateTime();
		DateTimeFormatter fp = DateTimeFormat.forPattern("yyyy-MM-dd");
		DateTime cd = dt.parse(currentDate,fp).plusYears(i).plusMonths(j).plusDays(k); 
		String close_date = cd.getYear()+"-"+(cd.getMonthOfYear()<10?"0"+cd.getMonthOfYear():cd.getMonthOfYear())+"-"+(cd.getDayOfMonth()<10?"0"+cd.getDayOfMonth():cd.getDayOfMonth());
		return close_date;
	}


	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 07/05/2017           
	Author                              : Disha           
	Description                         : Function to save CIF DATA

	 ***********************************************************************************  */

	public void save_cif_data(Map<Integer, HashMap<String, String>> cusDetails ,int prim_cif){
		try{
			PersonalLoanS.mLogger.info("RLSOCommon"+ "inside save_cif_data methos");
			FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 


			String WI_Name = formObject.getWFWorkitemName();
			PersonalLoanS.mLogger.info("RLSOCommon"+ "inside save_cif_data methos wi_name: "+WI_Name );
			Map<String, String> curr_entry;
			Iterator<Map.Entry<Integer, HashMap<String, String>>> itr = cusDetails.entrySet().iterator();

			while(itr.hasNext()){
				List<String> Cif_data=new ArrayList<String>();
				Map.Entry<Integer, HashMap<String, String>> entry =  itr.next();
				curr_entry = entry.getValue();

				Cif_data.add(curr_entry.get("SearchType")!=null?curr_entry.get("SearchType"):"");
				Cif_data.add(curr_entry.get("CustId")!=null?curr_entry.get("CustId"):"");
				Cif_data.add(curr_entry.get("PassportNum")!=null?curr_entry.get("PassportNum"):"");
				Cif_data.add(curr_entry.get("BlacklistFlag")!=null?curr_entry.get("BlacklistFlag"):"");
				Cif_data.add(curr_entry.get("DuplicationFlag")!=null?curr_entry.get("DuplicationFlag"):"");
				Cif_data.add(curr_entry.get("NegatedFlag")!=null?curr_entry.get("NegatedFlag"):"");
				Cif_data.add(curr_entry.get("Products")!=null?curr_entry.get("Products"):"");
				/*//Chnages done to add blacklist reasone code, data Negated Reasoncode and date start
				Cif_data.add(curr_entry.get("BlacklistReasonCode")!=null?curr_entry.get("BlacklistReasonCode"):"");
				Cif_data.add(curr_entry.get("BlacklistDate")!=null?curr_entry.get("BlacklistDate"):"");
				Cif_data.add(curr_entry.get("NegatedReasonCode")!=null?curr_entry.get("NegatedReasonCode"):"");
				Cif_data.add(curr_entry.get("NegatedDate")!=null?curr_entry.get("NegatedDate"):"");
				//Chnages done to add blacklist reasone code, data Negated Reasoncode and date end
				 */	
				
if(curr_entry.get("CustId").equalsIgnoreCase(Integer.toString(prim_cif))){//curr_entry.get("CustId").equalsIgnoreCase(prim_cif+"")

					 Cif_data.add("Y");
				 }
				 else{
					 Cif_data.add("N");	
				 }
				 Cif_data.add(WI_Name);
				 //Id changed by saurabh on 27th May.
				 formObject.addItemFromList("q_CIFDetail", Cif_data);
				 curr_entry=null;
					Cif_data=null;
				 PersonalLoanS.mLogger.info("RLSOCommon"+ "data to dave in cif details grid: "+ Cif_data);

			}
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("Exception occured while saving data for Customer Eligibility : "+ e.getMessage());
			printException(e);
		}


	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 07/05/2017           
	Author                              : Disha           
	Description                         : Function to set non Primary Passport 

	 ***********************************************************************************  */

	public void set_nonprimaryPassport(String cif_id, String pass_list){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try{
			formObject.setNGValue("cmplx_Customer_CIFNO",cif_id);
			if(pass_list.contains(",")){
				String[] pass_list_arr = pass_list.split(",");
				for(int i= 0; i<pass_list_arr.length && i<4 ; i++){
					formObject.setNGValue("cmplx_Customer_Passport"+(i+2),pass_list_arr[i]);
				}
			}

		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("Exception occured while seting non primary CIF: "+ e.getMessage());
			printException(e);
		}

	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 07/05/2017           
	Author                              : Disha           
	Description                         : Function for Application Type Employment    

	 ***********************************************************************************  */

	public void Fields_ApplicationType_Employment()
	{
		PersonalLoanS.mLogger.info("RLOSCommon"+ "Inside Fields_ApplicationType_Employment:"); 

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		if(NGFUserResourceMgr_PL.getGlobalVar("PL_RESC").equalsIgnoreCase(formObject.getNGValue("Application_Type")) || NGFUserResourceMgr_PL.getGlobalVar("PL_RESN").equalsIgnoreCase(formObject.getNGValue("Application_Type")) || NGFUserResourceMgr_PL.getGlobalVar("PL_RESR").equalsIgnoreCase(formObject.getNGValue("Application_Type"))){
			//formObject.setVisible("EMploymentDetails_Label33",true); 
			//formObject.setVisible("cmplx_EmploymentDetails_channelcode",true); 
			formObject.setVisible("EMploymentDetails_Label37", true);
			formObject.setVisible("cmplx_EmploymentDetails_Collectioncode", true);
		}
		else{
			formObject.setVisible("EMploymentDetails_Label37", false);
			formObject.setVisible("cmplx_EmploymentDetails_Collectioncode", false);
			//formObject.setVisible("EMploymentDetails_Label33",false); 
			//formObject.setVisible("cmplx_EmploymentDetails_channelcode",false); 
		}
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 07/05/2017           
	Author                              : Disha           
	Description                         : Function to parse cif eligibility  

	 ***********************************************************************************  */

	public void parse_cif_eligibility(String output,String operation_name){

		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			output=output.substring(output.indexOf("<MQ_RESPONSE_XML>")+17,output.indexOf("</MQ_RESPONSE_XML>"));
			PersonalLoanS.mLogger.info("inside parse_cif_eligibility"+"");
			Map<Integer, HashMap<String, String> > Cus_details = new HashMap<Integer, HashMap<String, String>>();
			String passport_list = "";

			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(output)));
			PersonalLoanS.mLogger.info("name is doc : "+ doc+"");
			NodeList nl = doc.getElementsByTagName("*");

			for (int nodelen=0; nodelen<nl.getLength();nodelen++){
				Map<String, String> cif_details = new HashMap<String, String>();
				nl.item(nodelen);
				if("CustomerDetails".equalsIgnoreCase(nl.item(nodelen).getNodeName()))//nl.item(nodelen).getNodeName().equalsIgnoreCase("CustomerDetails")
				{
					int no_of_product = 0;
					NodeList childnode  = nl.item(nodelen).getChildNodes();
					for (int childnodelen= 0;childnodelen<childnode.getLength();childnodelen++){
						String tag_name = childnode.item(childnodelen).getNodeName();
						String tag_value=childnode.item(childnodelen).getTextContent();
						if(tag_name!=null && tag_value!=null){
							if("Products".equalsIgnoreCase(tag_name)){
								++no_of_product;
								cif_details.put(tag_name, Integer.toString(no_of_product)+"");
							}else{
								if("PassportNum".equalsIgnoreCase(tag_name))
									passport_list = tag_value+ ","+passport_list;
								cif_details.put(tag_name, tag_value);
							}

						}
						else{
							PersonalLoanS.mLogger.info("tag name and tag value can not be null:: tag name: "+tag_name+ " tag value: " +tag_value);
						}

					}
					//changes done to remove internal blacklist without cif
					if(cif_details.containsKey("CustId")){
						Cus_details.put(Integer.parseInt(cif_details.get("CustId")), (HashMap<String, String>) cif_details) ;
					}
				}
			}
			int Prim_cif = primary_cif_identify(Cus_details);
			if("Supplementary_Card_Details".equalsIgnoreCase(operation_name)){
				Map<String, String> Supplementary = new HashMap<String, String>();
				Supplementary = Cus_details.get(Prim_cif);
				formObject.setNGValue("Supplementary_CIFNO",Supplementary.get("CustId")==null?"":Supplementary.get("CustId"));
			}
			else if("Primary_CIF".equalsIgnoreCase(operation_name)){
				formObject.clear("q_CIFDetail");
				save_cif_data(Cus_details,Prim_cif);
				if(Prim_cif!=0){
					PersonalLoanS.mLogger.info("parse_cif_eligibility Primary CIF: "+Prim_cif+"");
					Map<String, String> prim_entry;
					prim_entry = Cus_details.get(Prim_cif);
					String primary_pass = prim_entry.get("PassportNum");
					passport_list = passport_list.replace(primary_pass, "");
					PersonalLoanS.mLogger.info("Prim_cif: "+prim_entry.get("CustId"));


					set_nonprimaryPassport(prim_entry.get("CustId")==null?"":prim_entry.get("CustId"),passport_list);//changes done to save CIF without 0.
				}
			}
			else if ("Guarantor_CIF".equalsIgnoreCase(operation_name)){
				Map<String, String> Guarantor ;
				Guarantor = Cus_details.get(Prim_cif);
				formObject.setNGValue("GuarantorDetails_cif",Guarantor.get("CustId")==null?"":Guarantor.get("CustId"));

			}
			PersonalLoanS.mLogger.info("Prim_cif: "+ Prim_cif+"");

		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("Exception occured while parsing Customer Eligibility : "+ e.getMessage());
			printException(e);
		}

	}
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Load Customer Picklist      

	 ***********************************************************************************  */


	public void loadPicklistCustomer()  
	{
		// disha FSD
		PersonalLoanS.mLogger.info("RLOS_Common"+ "Inside loadPicklistCustomer: ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		//LoadPickList("cmplx_Customer_Nationality", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
		LoadPickList("cmplx_Customer_Title", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_title with (nolock) order by code");
		//LoadPickList("cmplx_Customer_SecNationality", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
		//LoadPickList("cmplx_Customer_Third_Nationaity", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
		LoadPickList("cmplx_Customer_COuntryOFResidence", "select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from ng_MASTER_Country  with (nolock) order by Code");	
		LoadPickList("cmplx_Customer_MAritalStatus", "select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_Maritalstatus  with (nolock) order by Code");
		//LoadPickList("ref_Relationship", "select convert(varchar, Description),code from NG_MASTER_Relationship union select '--Select--','' order by Code");
		LoadPickList("cmplx_Customer_EmirateOfResidence", "select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_emirate  with (nolock)  order by Code");
		LoadPickList("cmplx_Customer_EMirateOfVisa", "select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_emirate with (nolock)  order by Code");
		LoadPickList("cmplx_Customer_referrorcode", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_ReferrorCode with (nolock)"); //Arun
		LoadPickList("cmplx_Customer_referrorname", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_ReferrorCode with (nolock)"); //Arun
		LoadPickList("cmplx_Customer_NEP", " select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from ng_MASTER_NEPType  where isActive='Y'  order by code " );//by shweta PCASI-1049
		
		//change by saurabh on 7th Dec
		//LoadPickList("cmplx_Customer_NEP","select '--Select--' as Description,'' as code union select convert(varchar, Description),code from NG_MASTER_NEPType with (nolock)  where isActive='Y' order by code");
		LoadPickList("cmplx_Customer_corpcode", "select '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_CollectionCode with (nolock)"); //Arun
		LoadPickList("cmplx_Customer_CustomerCategory", " select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_CustomerCategory with (nolock)  order by Code");
		//LoadPickList("cmplx_Customer_Dsa_NAme", "select '--Select--' as UserName union all select UserName from PDBUser where UserIndex in (select UserIndex from PDBGroupMember where GroupIndex=(select GroupIndex from PDBGroup where GroupName='DSA'))");
		//LoadPickList("cmplx_Customer_DSA_Code", "select UserId , UserId from NG_MASTER_SourceCode with (nolock)");
		//LoadPickList("cmplx_Customer_DSA_Code", "select UserId , UserName from NG_MASTER_SourceCode with (nolock)");
		LoadPickList("cmplx_Customer_Constitution","select '--Select--' as Description,'' as code  union select convert(varchar, Description),code from NG_MASTER_Constitution with (nolock)");
		
		String activity = formObject.getWFActivityName();
		if("DDVT_maker".equalsIgnoreCase(activity)){
			
			if("".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_Constitution"))||"--Select--".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_Constitution")) || null == (formObject.getNGValue("cmplx_Customer_Constitution")) ){
				//cmplx_Customer_Constitution: Individuals - Residents
				formObject.setNGValue("cmplx_Customer_Constitution", "Individuals - Residents");
			}//By Alok for Constitution on 28/10/21

		}
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Load RiskRating Picklist 

	 ***********************************************************************************  */

	public void loadPicklistRiskRating()
	{
		PersonalLoanS.mLogger.info("RLOS_Common"+ "Inside loadPicklistRiskRating: ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		LoadPickList("cmplx_RiskRating_BusinessSeg", "select '--Select--'as description,'' as code  union all select convert(varchar, description),code from NG_MASTER_Business_Segment with (nolock) order by Code");
		LoadPickList("cmplx_RiskRating_SubSeg", "select '--Select--'as description,'' as code  union all select convert(varchar, description),code from NG_MASTER_Business_SubSegment with (nolock) order by Code");
		LoadPickList("cmplx_RiskRating_Demographics", "select demographic from (select '--Select--'as demographic,'0' as sno  union all select demographic,Sno from NG_MASTER_Demographics  with (nolock) ) as q order by Sno");//Load Picklist changed by aman in risk fragment
		LoadPickList("cmplx_RiskRating_Industry","select Industry from (select '--Select--'as Industry,'' as sno  union all  select Industry,Sno from NG_MASTER_Risk_Industry  with (nolock) ) as q order by Sno"); //Load Picklist added by aman in risk fragment
		PersonalLoanS.mLogger.info("query to set EMPLOYED INDIVIDUAL as a default value:::");
		PersonalLoanS.mLogger.info("RiskRating_Industry"+formObject.getNGValue("cmplx_RiskRating_Industry"));
		if("--Select--".equalsIgnoreCase(formObject.getNGValue("cmplx_RiskRating_Industry")) || "".equalsIgnoreCase(formObject.getNGValue("cmplx_RiskRating_Industry")) || null == (formObject.getNGValue("cmplx_RiskRating_Industry")) )
		{
			String Q = "select Industry from NG_MASTER_Risk_Industry where Industry='EMPLOYED INDIVIDUAL'";
			List<List<String>> res = formObject.getNGDataFromDataCache(Q);
			PersonalLoanS.mLogger.info("res:"+res);
			if(res!=null && !res.isEmpty())
			 {
				 PersonalLoanS.mLogger.info("inside 1st if::");
				 if("EMPLOYED INDIVIDUAL".equalsIgnoreCase(res.get(0).get(0)))
				 {
					 PersonalLoanS.mLogger.info("inside 2nd if::"+res.get(0).get(0));
					 formObject.setNGValue("cmplx_RiskRating_Industry",res.get(0).get(0));
				 }
			 }
			
		} //alok
		if("--Select--".equalsIgnoreCase(formObject.getNGValue("cmplx_RiskRating_BusinessSeg")) || "".equalsIgnoreCase(formObject.getNGValue("cmplx_RiskRating_BusinessSeg")) || null == (formObject.getNGValue("cmplx_RiskRating_BusinessSeg")) )
		{
			PersonalLoanS.mLogger.info("Inside 2nd if of cmplx_RiskRating_BusinessSeg");
		formObject.setNGValue("cmplx_RiskRating_BusinessSeg","PERSONAL BANKING");
		}
		if("--Select--".equalsIgnoreCase(formObject.getNGValue("cmplx_RiskRating_SubSeg")) || "".equalsIgnoreCase(formObject.getNGValue("cmplx_RiskRating_SubSeg")) || null == (formObject.getNGValue("cmplx_RiskRating_SubSeg")) )
		{
			PersonalLoanS.mLogger.info("Inside 2nd if of cmplx_RiskRating_SubSeg");
			formObject.setNGValue("cmplx_RiskRating_SubSeg","PB - NORMAL");
		}
		PersonalLoanS.mLogger.info("set value of industry: "+formObject.getNGValue("cmplx_RiskRating_Industry"));
		PersonalLoanS.mLogger.info("set value of BusinessSeg: "+formObject.getNGValue("cmplx_RiskRating_BusinessSeg"));
		PersonalLoanS.mLogger.info("set value of cmplx_RiskRating_SubSeg: "+formObject.getNGValue("cmplx_RiskRating_SubSeg"));
	}//Arun (10/10)

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Validate Loan   

	 ***********************************************************************************  */

	public void loanvalidate()
	{
		//below try catch added by nikhil 9/12/17
		try
		{

			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			PersonalLoanS.mLogger.info(""+"cmplx_LoanDetails_moratorium"+formObject.getNGValue("cmplx_LoanDetails_moratorium"));
			PersonalLoanS.mLogger.info("Checkk"+"cmplx_LoanDetails_lonamt"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_PLHidden"));
			PersonalLoanS.mLogger.info("Checkk--1"+"cmplx_LoanDetails_emi"+formObject.getNGValue("cmplx_LoanDetails_lonamt"));
			
			PersonalLoanS.mLogger.info("Checkk--2 cmplx_LoanDetails_emi"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_EMI"));
			
			if(!"".equals(formObject.getNGValue("cmplx_EligibilityAndProductInfo_EMI")) && ("".equals(formObject.getNGValue("cmplx_LoanDetails_loanemi")) || "0".equals(formObject.getNGValue("cmplx_LoanDetails_loanemi")))){
				PersonalLoanS.mLogger.info("Checkk--3 Inside to set EMI");
				formObject.setNGValue("cmplx_LoanDetails_loanemi",formObject.getNGValue("cmplx_EligibilityAndProductInfo_EMI"));
			}
			
			//above by hritik for loamemi InternalExternalLiability_Header
			
			//below code modified by nikhil and if conditions  changed 06/12/17
			//if added by nikhil
			if(!"".equals(formObject.getNGValue("cmplx_EligibilityAndProductInfo_PLHidden")) && "".equals(formObject.getNGValue("cmplx_LoanDetails_lonamt")))
				formObject.setNGValue("cmplx_LoanDetails_lonamt",formObject.getNGValue("cmplx_EligibilityAndProductInfo_PLHidden"));
			//if condition changed
			if(!"".equals(formObject.getNGValue("cmplx_EligibilityAndProductInfo_BaseRateType")) && "".equals(formObject.getNGValue("cmplx_LoanDetails_basetype")))
				formObject.setNGValue("cmplx_LoanDetails_basetype",formObject.getNGValue("cmplx_EligibilityAndProductInfo_BaseRateType"));
			//if condition change
			if(!"".equals(formObject.getNGValue("cmplx_EligibilityAndProductInfo_BAseRate")) && "".equals(formObject.getNGValue("cmplx_LoanDetails_baserate")))			
				formObject.setNGValue("cmplx_LoanDetails_baserate",formObject.getNGValue("cmplx_EligibilityAndProductInfo_BAseRate"));
			//if condition changed
			if(!"".equals(formObject.getNGValue("cmplx_EligibilityAndProductInfo_MArginRate")) && "".equals(formObject.getNGValue("cmplx_LoanDetails_marginrate")))
				formObject.setNGValue("cmplx_LoanDetails_marginrate",formObject.getNGValue("cmplx_EligibilityAndProductInfo_MArginRate"));
			//if condition changed
			if(!"".equals(formObject.getNGValue("cmplx_EligibilityAndProductInfo_ProdPrefRate")) && "".equals(formObject.getNGValue("cmplx_LoanDetails_pdtpref")))
				formObject.setNGValue("cmplx_LoanDetails_pdtpref",formObject.getNGValue("cmplx_EligibilityAndProductInfo_ProdPrefRate"));
			//if condition changed
			//change by saurabh on 2nd Jan
			if(!"".equals(formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalInterestRate")) && "".equals(formObject.getNGValue("cmplx_LoanDetails_netrate")))
				formObject.setNGValue("cmplx_LoanDetails_netrate",formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalInterestRate"));
			//if condition changed
			if(!"".equals(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor")) && "".equals(formObject.getNGValue("cmplx_LoanDetails_tenor")))
				formObject.setNGValue("cmplx_LoanDetails_tenor",formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor"),false);
			//if condition changed
			/*if(!"".equals(formObject.getNGValue("cmplx_EligibilityAndProductInfo_RepayFreq")) && "".equals(formObject.getNGValue("cmplx_LoanDetails_repfreq")))
			formObject.setNGValue("cmplx_LoanDetails_repfreq",formObject.getNGValue("cmplx_EligibilityAndProductInfo_RepayFreq"));
		//if condition changed
----commented by akshay on 15/4/18 for proc 2180*/		
			if(!"".equals(formObject.getNGValue("cmplx_EligibilityAndProductInfo_NumberOfInstallment")) && "".equals(formObject.getNGValue("cmplx_LoanDetails_insplan")))
				formObject.setNGValue("cmplx_LoanDetails_insplan",formObject.getNGValue("cmplx_EligibilityAndProductInfo_NumberOfInstallment"));

			//if condition changed
			PersonalLoanS.mLogger.info(""+"cmplx_LoanDetails_moratorium"+formObject.getNGValue("cmplx_LoanDetails_moratorium"));

			if(!"".equals(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Moratorium")) && "".equals(formObject.getNGValue("cmplx_LoanDetails_moratorium"))){
				PersonalLoanS.mLogger.info(""+"cmplx_LoanDetails_moratorium"+formObject.getNGValue("cmplx_LoanDetails_moratorium"));

				//formObject.setNGValue("cmplx_LoanDetails_moratorium",formObject.getNGValue("cmplx_EligibilityAndProductInfo_Moratorium"),false); //PCASI - 3590
				formObject.setNGValue("cmplx_LoanDetails_moratorium",formObject.getNGValue("cmplx_EligibilityAndProductInfo_Moratorium")); //PCASI - 3590
			}
			//if condition changed
			if(!"".equals(formObject.getNGValue("cmplx_EligibilityAndProductInfo_FirstRepayDate")) && "".equals(formObject.getNGValue("cmplx_LoanDetails_frepdate")))
				formObject.setNGValue("cmplx_LoanDetails_frepdate",formObject.getNGValue("cmplx_EligibilityAndProductInfo_FirstRepayDate"),false);
			//if condition changed
			if(!"".equals(formObject.getNGValue("cmplx_EligibilityAndProductInfo_MaturityDate"))  && "".equals(formObject.getNGValue("cmplx_LoanDetails_maturitydate")))
				formObject.setNGValue("cmplx_LoanDetails_maturitydate",formObject.getNGValue("cmplx_EligibilityAndProductInfo_MaturityDate"),false);
			//if condition changed
			if(!"".equals(formObject.getNGValue("cmplx_EligibilityAndProductInfo_AgeAtMaturity")) && "".equals(formObject.getNGValue("cmplx_LoanDetails_ageatmaturity")))
				formObject.setNGValue("cmplx_LoanDetails_ageatmaturity",formObject.getNGValue("cmplx_EligibilityAndProductInfo_AgeAtMaturity"));
			//if condition changed
			if(!"".equals(formObject.getNGValue("cmplx_EligibilityAndProductInfo_LPF")) && "".equals(formObject.getNGValue("cmplx_LoanDetails_lpf")))
				formObject.setNGValue("cmplx_LoanDetails_lpf",formObject.getNGValue("cmplx_EligibilityAndProductInfo_LPF"));
			//if condition changed
			if(!"".equals(formObject.getNGValue("cmplx_EligibilityAndProductInfo_LPFAmount")) && "".equals(formObject.getNGValue("cmplx_LoanDetails_lpfamt")))
				formObject.setNGValue("cmplx_LoanDetails_lpfamt",formObject.getNGValue("cmplx_EligibilityAndProductInfo_LPFAmount"));
			//if condition changed
			if(!"".equals(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Insurance")) && "".equals(formObject.getNGValue("cmplx_LoanDetails_insur")))
				formObject.setNGValue("cmplx_LoanDetails_insur",formObject.getNGValue("cmplx_EligibilityAndProductInfo_Insurance"));
			//if condition changed
			if(!"".equals(formObject.getNGValue("cmplx_EligibilityAndProductInfo_InsuranceAmount")) && "".equals(formObject.getNGValue("cmplx_LoanDetails_insuramt")))
				formObject.setNGValue("cmplx_LoanDetails_insuramt",formObject.getNGValue("cmplx_EligibilityAndProductInfo_InsuranceAmount"));
			//if condition changed
			/*if(!"".equals(formObject.getNGValue("cmplx_EligibilityAndProductInfo_FirstRepayDate")) && "".equals(formObject.getNGValue("LoanDetails_duedate")))
			formObject.setNGValue("LoanDetails_duedate",formObject.getNGValue("cmplx_EligibilityAndProductInfo_FirstRepayDate"));
		//if condition changed
			 */		if(!"".equals(formObject.getNGValue("cmplx_LoanDetails_lonamt")))
			 {
				 formObject.setNGValue("LoanDetails_loanamt",formObject.getNGValue("cmplx_LoanDetails_lonamt"));
				 formObject.setNGValue("LoanDetails_amt", formObject.getNGValue("cmplx_LoanDetails_lonamt"));
			 }
			 formObject.setLocked("cmplx_LoanDetails_moratorium",true);
			// if(!"".equals(formObject.getNGValue("cmplx_EligibilityAndProductInfo_EMI")))//to always populate emi from eligibility in case dectech is run//proc 8460
			//	{
				//	double LoanAmount=Double.parseDouble(formObject.getNGValue("cmplx_LoanDetails_lonamt")==null||formObject.getNGValue("cmplx_LoanDetails_lonamt").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_LoanDetails_lonamt"));
				//	double tenor=Double.parseDouble(formObject.getNGValue("cmplx_LoanDetails_tenor")==null||formObject.getNGValue("cmplx_LoanDetails_tenor").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_LoanDetails_tenor"));
				//	double RateofInt=Double.parseDouble(formObject.getNGValue("cmplx_LoanDetails_netrate")==null||formObject.getNGValue("cmplx_LoanDetails_netrate").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_LoanDetails_netrate"));
				//	PersonalLoanS.mLogger.info("Calculating EMI onchange of Tenor"+LoanAmount+ "  " + tenor+"  "+"  "+RateofInt);
				//	int mono_days=Integer.parseInt(formObject.getNGValue("cmplx_LoanDetails_moratorium")==null||formObject.getNGValue("cmplx_LoanDetails_moratorium").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_LoanDetails_moratorium"));
					
				/*	String EMI=getEMI(LoanAmount,RateofInt,tenor,mono_days);
					formObject.setNGValue("cmplx_LoanDetails_loanemi", EMI==null||EMI.equalsIgnoreCase("")?formObject.getNGValue("cmplx_EligibilityAndProductInfo_EMI"):EMI);
					formObject.setNGValue("cmplx_EligibilityAndProductInfo_EMI", EMI==null||EMI.equalsIgnoreCase("")?"0.00":EMI);	//PCASI 3524
				*/	
				/*	Double emi=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_EMI"));
					PersonalLoanS.mLogger.info("Emi double is: "+emi);
					formObject.setNGValue("cmplx_LoanDetails_loanemi",Math.round(emi));*/ 
			
		//}
			 float lpfVatAmt=0;
			 float insuranceVatAmt=0;
			 PersonalLoanS.mLogger.info("IMD cal");
			 try
			 {
				 //added By Tarang for drop 4 point 8 started on 08/03/2018
				 PersonalLoanS.mLogger.info("IMD cal try start:");
				 lpfVatAmt=(Float.parseFloat(formObject.getNGValue("cmplx_LoanDetails_LoanProcessingVATPercent"))*Float.parseFloat(formObject.getNGValue("cmplx_LoanDetails_lpfamt")))/100f;
				 insuranceVatAmt=(Float.parseFloat(formObject.getNGValue("cmplx_LoanDetails_InsuranceVATPercent"))*Float.parseFloat(formObject.getNGValue("cmplx_LoanDetails_insuramt")))/100f;
				 lpfVatAmt = ((float)(Math.round((lpfVatAmt*100)))/100);
				 insuranceVatAmt = ((float)(Math.round((insuranceVatAmt*100)))/100);
				 PersonalLoanS.mLogger.info("IMD cal try end:lpfVatAmt"+lpfVatAmt);
				 PersonalLoanS.mLogger.info("IMD cal try end:insuranceVatAmt"+insuranceVatAmt);
			 }
			 catch(Exception ex)
			 {

			 }
			 finally
			 {
				 formObject.setNGValue("cmplx_LoanDetails_LoanProcessingVat", lpfVatAmt);
				 formObject.setNGValue("cmplx_LoanDetails_InsuranceVat", insuranceVatAmt);
				 PersonalLoanS.mLogger.info("IMD cal final start:lpfVatAmt"+lpfVatAmt);
				 PersonalLoanS.mLogger.info("IMD cal final start:insuranceVatAmt"+insuranceVatAmt);
				 formObject.setLocked("cmplx_LoanDetails_LoanProcessingVat", true);
				 formObject.setLocked("cmplx_LoanDetails_InsuranceVat", true);
				 //added By Tarang for drop 4 point 8 started on 08/03/2018
				 String lpf = formObject.getNGValue("cmplx_LoanDetails_lpfamt");
				 String insurance = formObject.getNGValue("cmplx_LoanDetails_insuramt");
				 PersonalLoanS.mLogger.info("IMD cal final :lpf"+lpf);
				 PersonalLoanS.mLogger.info("IMD cal final :insurance"+insurance);
				 //change by saurabh for UG points 17/6
				 float Amount = Float.parseFloat(lpf) + Float.parseFloat(insurance)+lpfVatAmt+insuranceVatAmt;		
				 formObject.setNGValue("cmplx_LoanDetails_amt",Amount);
				 PersonalLoanS.mLogger.info("IMD cal final :Amount"+Amount);
				 //below code added by nikhil 
				 formObject.setLocked("cmplx_LoanDetails_amt", true);
				 formObject.setLocked("LoanDetails_duedate", true);
				 /*String LoanAmt=formObject.getNGValue("cmplx_LoanDetails_lonamt");
		String lpfpercent=formObject.getNGValue("cmplx_LoanDetails_lpf");
		String inspercent=formObject.getNGValue("cmplx_LoanDetails_insur");
		double lpfamt= (Double.parseDouble(lpfpercent)*0.01)*;*/
				 formObject.setLocked("cmplx_LoanDetails_lpfamt", true);
				 formObject.setLocked("cmplx_LoanDetails_lpf", true);
				 formObject.setLocked("cmplx_LoanDetails_insur", true);

				 //formObject.setLocked("cmplx_LoanDetails_inttype", true);
				 formObject.setNGValue("cmplx_LoanDetails_currency","AED");
				 formObject.setLocked("cmplx_LoanDetails_currency",true);

				 formObject.setNGValue("cmplx_LoanDetails_favourof","RAK");
				 formObject.setLocked("cmplx_LoanDetails_favourof",true);
				 if("".equals(formObject.getNGValue("cmplx_LoanDetails_fdisbdate")))
				 {
					 formObject.setNGValue("cmplx_LoanDetails_fdisbdate", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
				 }
				 if("".equals(formObject.getNGValue("cmplx_LoanDetails_trandate")))
				 {
					 formObject.setNGValue("cmplx_LoanDetails_trandate", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
				 }

				 //id changes by nikhil 9/12/17
				 if(!"".equals(formObject.getNGValue("LoanDetails_disbdate")))
					 formObject.setNGValue("cmplx_LoanDetails_paidon",formObject.getNGValue("LoanDetails_disbdate"));
				 //++below code added by nikhil 9/12/17
				 if(!"".equals(formObject.getNGValue("cmplx_LoanDetails_frepdate")))
				 {
					 formObject.setNGValue("LoanDetails_duedate", formObject.getNGValue("cmplx_LoanDetails_frepdate"));
				 }
				 if("".equals(formObject.getNGValue("cmplx_LoanDetails_paidon")))
				 {

					 formObject.setNGValue("cmplx_LoanDetails_paidon",formObject.getNGValue("cmplx_LoanDetails_cmplx_LoanGrid",0,2));
					 //as per PROC-7103

					 formObject.setLocked("cmplx_LoanDetails_basetype", true);
				 }

			 }
			 String currVal = formObject.getNGValue("FrameName");
			 if(currVal!=null && currVal!=""){
				 formObject.setNGValue("FrameName",currVal+"LoanDetails,");
			}else {
				formObject.setNGValue("FrameName","LoanDetails,");

				
			}
		}

		catch(Exception ex)
		{
			PersonalLoanS.mLogger.info(ex.getMessage());
			printException(ex);
		}


	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to loadPicklist for ELigibiltyAndProductInfo    

	 ***********************************************************************************  */

	public void loadPicklistELigibiltyAndProductInfo()  
	{
		LoadPickList("cmplx_EligibilityAndProductInfo_takeoverBank", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_BankName with (nolock) order by code");
		LoadPickList("cmplx_EligibilityAndProductInfo_RepayFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_frequency with (nolock)");
		LoadPickList("cmplx_EligibilityAndProductInfo_InterestType", "select '--Select--' union select convert(varchar, description) from NG_MASTER_InterestType  with (nolock) ");
		LoadPickList("cmplx_EligibilityAndProductInfo_InstrumentType", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_InstrumentType with (nolock) where isactive = 'Y'  order by code");
		//query added by saurabh on 22nd Oct for loading base rate type.
		LoadPickList("cmplx_EligibilityAndProductInfo_BaseRateType", "select distinct PRIME_TYPE from NG_master_Scheme  with (nolock)  order by PRIME_TYPE");
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to loadPicklist for Product    

	 ***********************************************************************************  */


	public void loadPicklistProduct(String ReqProd)
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PersonalLoanS.mLogger.info("RLOS_Common"+ "Inside loadPicklistProduct$"+ReqProd+"$");


		if(ReqProd!=null && "Personal Loan".equalsIgnoreCase(ReqProd)){
			PersonalLoanS.mLogger.info("RLOS_Common"+ "Inside equalsIgnoreCase()"); 
			formObject.setVisible("Scheme", true);
			formObject.setLeft("Scheme", 555);
			formObject.clear("SubProd");
			String TypeofProduct=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",formObject.getSelectedIndex("cmplx_Product_cmplx_ProductGrid"),0);
			String subprod=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",formObject.getSelectedIndex("cmplx_Product_cmplx_ProductGrid"),2);
			formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",formObject.getSelectedIndex("cmplx_Product_cmplx_ProductGrid"),4);
			//formObject.clear("Scheme");
			//formObject.setNGValue("Scheme", "--Select--");
			LoadPickList("Product_type", "select convert(varchar, Type) from NG_MASTER_TypeOfProduct with (nolock)");
			LoadPickList("SubProd", "select '--select--',''as 'code' union select convert(varchar(50),description),code  from ng_MASTER_SubProduct_PL  with (nolock) order by code");
			//LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar,SchemeDesc),SCHEMEID from ng_MASTER_Scheme with (nolock) order by SCHEMEID");
			//added by abhishek
			LoadPickList("AppType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_ApplicationType with (nolock)  where SubProdFlag = '"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2)+"' order by code");
			LoadPickList("EmpType", " select '--Select--' union select convert(varchar, description) from ng_MASTER_PRD_EMPTYPE with (nolock)  where SubProduct = '"+ReqProd+"'");
			String sub_prod="EXP".equalsIgnoreCase(getSubproduct())?"Expat":"National";
			//LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='"+sub_prod+"'   and TypeofProduct='"+getTypeProd()+"' order by SCHEMEID");
			LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) order by SCHEMEID");
			formObject.setLocked("Scheme",true);
		}
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Load Incoming Document Repeater  

	 ***********************************************************************************  */

	public String getTypeProd() {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String prodGridId = NGFUserResourceMgr_PL.getGlobalVar("PL_productgrid");
		//Deepak 06june2018 below code changed because of null pointer exception in sys logs
		int prd_count=formObject.getLVWRowCount(prodGridId);
		String typeProd = "";
		if(prd_count>0){
			typeProd = formObject.getNGValue(prodGridId,formObject.getSelectedIndex(prodGridId),0);
		}
		//String typeProd = formObject.getNGValue(prodGridId,formObject.getSelectedIndex(prodGridId),0);

		return typeProd;
	}

	public String getApplicationType() {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String prodGridId = NGFUserResourceMgr_PL.getGlobalVar("PL_productgrid");
		//Deepak 06june2018 below code changed because of null pointer exception in sys logs
		int prd_count=formObject.getLVWRowCount(prodGridId);
		String apptype = "";
		if(prd_count>0){
			apptype = formObject.getNGValue(prodGridId,formObject.getSelectedIndex(prodGridId),4);
		}
		//String apptype = formObject.getNGValue(prodGridId,formObject.getSelectedIndex(prodGridId),4);
		if(apptype.contains("TOP")){
			return "Top up";
		}
		else if(apptype.contains("TKO")){
			return "Take Over";
		}
		else if(apptype.contains("NEW")){
			return "New";
		}
		else if(apptype.contains("RES")){
			return "Reschedulment";
		}
		return "";
	}

	public String getSubproduct() {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String prodGridId = NGFUserResourceMgr_PL.getGlobalVar("PL_productgrid");
		//Deepak 06june2018 below code changed because of null pointer exception in sys logs
		int prd_count=formObject.getLVWRowCount(prodGridId);
		String subprod = "";
		if(prd_count>0){
			subprod = formObject.getNGValue(prodGridId,formObject.getSelectedIndex(prodGridId),2);
		}

		//String subprod = formObject.getNGValue(prodGridId,formObject.getSelectedIndex(prodGridId),2);

		return subprod;
	}

	public void IncomingDoc()
	{
		PersonalLoanS.mLogger.info("Inside IncomingDoc() method ...");
		try
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
			IRepeater repObj;
			repObj = formObject.getRepeaterControl("IncomingDoc_Frame2");


			String [] finalmisseddoc=new String[70];
			if(null!=repObj)
			{
				int rowRowcount=repObj.getRepeaterRowCount();
				PersonalLoanS.mLogger.info("RLOS Initiation"+ "sQuery for document name is: rowRowcount" +  rowRowcount);
				if (repObj.getRepeaterRowCount() != 0) {

					for(int j = 0; j < rowRowcount; j++)
					{
						String DocName=repObj.getValue(j, "cmplx_DocName_DocName");
						PersonalLoanS.mLogger.info("RLOS Initiation"+ "sQuery for document name is: DocName" +  DocName);

						String Mandatory=repObj.getValue(j, "cmplx_DocName_Mandatory");
						PersonalLoanS.mLogger.info("RLOS Initiation"+ "sQuery for document name is: Mandatory" +  Mandatory);
						String DocIndex=repObj.getValue(j,"cmplx_DocName_DocIndex")==null?"":repObj.getValue(j,"cmplx_DocName_DocIndex");

						//Belwo code added by Deepak to save document index of signature to use the same in upload signature call.--start 09Jan2018
						if("Signature".equalsIgnoreCase(DocName)){
							formObject.setNGValue("sig_docindex", DocIndex);
						}
						//Belwo code added by Deepak to save document index of signature to use the same in upload signature call.--end
						if("Y".equalsIgnoreCase(Mandatory))
						{

							PersonalLoanS.mLogger.info(""+"DocIndex"+DocIndex);
							String StatusValue=repObj.getValue(j,"cmplx_DocName_Doc_Status")==null?"":repObj.getValue(j,"cmplx_DocName_Doc_Status");
							PersonalLoanS.mLogger.info(""+"StatusValue"+StatusValue);
							String Remarks=repObj.getValue(j, "cmplx_DocName_Remarks")==null?"":repObj.getValue(j, "cmplx_DocName_Remarks");
							PersonalLoanS.mLogger.info(""+"Remarks"+Remarks);

							if(DocIndex==null || "".equalsIgnoreCase(DocIndex)){
								PersonalLoanS.mLogger.info(""+"StatusValue inside DocIndex"+DocIndex);
								if("Received".equalsIgnoreCase(StatusValue)){
									PersonalLoanS.mLogger.info(""+"StatusValue inside DocIndex recieved");
									//below line commented as this mandatory document is already received. 
									// finalmisseddoc[j]=DocName;
								}

								else if("Deferred".equalsIgnoreCase(StatusValue)){
									formObject.setNGValue("Deferral_req","Y");
									formObject.RaiseEvent("WFSave");
									PersonalLoanS.mLogger.info("Deferred flag value inside no document"+formObject.getNGValue("is_deferral_approval_require"));
									if("".equalsIgnoreCase(Remarks)){
										PersonalLoanS.mLogger.info("It is Mandatory to fill Remarks"+"As you have not attached the Mandatory Document and the status is Deferred");
										throw new ValidatorException(new FacesMessage("As you have Deferred "+DocName+" Document,So kindly fill the Remarks"));
									}
									else if(!"".equalsIgnoreCase(Remarks)|| Remarks==null){
										PersonalLoanS.mLogger.info("You may proceed further"+"Proceed further");
									}
								}
								else if("Waived".equalsIgnoreCase(StatusValue)){
									formObject.setNGValue("Waiver_req","Y");
									formObject.RaiseEvent("WFSave");
									PersonalLoanS.mLogger.info("waived flag value inside no document"+formObject.getNGValue("is_waiver_approval_require"));
									if("".equalsIgnoreCase(Remarks)){
										PersonalLoanS.mLogger.info("It is Mandatory to fill Remarks"+"As you have not attached the Mandatory Document and the status is Waived");
										throw new ValidatorException(new FacesMessage("As you have Waived "+DocName+" Document,So kindly fill the Remarks"));
									}
									else if(!"".equalsIgnoreCase(Remarks)||Remarks.equalsIgnoreCase(null)||"null".equalsIgnoreCase(Remarks)){
										PersonalLoanS.mLogger.info("You may proceed further"+"Proceed further");
									}
								}
								else if("--Select--".equalsIgnoreCase(StatusValue)||("".equalsIgnoreCase(StatusValue))){
									PersonalLoanS.mLogger.info(""+"StatusValue inside DocIndex is blank");
									finalmisseddoc[j]=DocName;
								}
								else if("Pending".equalsIgnoreCase(StatusValue)){
									PersonalLoanS.mLogger.info(""+"StatusValue of doc is Pending");

								}
							}
							else{
								if(!("".equalsIgnoreCase(DocIndex))){
									if(!"Received".equalsIgnoreCase(StatusValue)){
										repObj.setValue(j,"cmplx_DocName_Status","Received");
										repObj.setEditable(j, "cmplx_DocName_Status", false);
										PersonalLoanS.mLogger.info(""+"StatusValue::123final"+StatusValue);
									}
									else {

										PersonalLoanS.mLogger.info(""+"StatusValue::123final status is already received");
									}
								}
							}

						}
					}
				}
			}	

			StringBuilder mandatoryDocName = new StringBuilder("");

			PersonalLoanS.mLogger.info(""+"length of missed document"+finalmisseddoc.length);
			PersonalLoanS.mLogger.info(""+"length of missed document mandatoryDocName.length"+mandatoryDocName.length());

			for(int k=0;k<finalmisseddoc.length;k++)
			{
				if("AECBconsentform".equalsIgnoreCase(finalmisseddoc[k]) && "true".equals(formObject.getNGValue("cmplx_Liability_New_AECBconsentAvail"))){
					k++;
				}//added by akshay on 16/1/18 for proc 3667
				else if(null != finalmisseddoc[k]) {
					mandatoryDocName.append(finalmisseddoc[k]).append(",");
				}
				PersonalLoanS.mLogger.info("RLOS Initiation"+ "finalmisseddoc is:" +finalmisseddoc[k]);
				PersonalLoanS.mLogger.info(""+"length of missed document mandatoryDocName.length1111"+mandatoryDocName.length());
			}
			mandatoryDocName.setLength(Math.max(mandatoryDocName.length()-1,0));
			PersonalLoanS.mLogger.info("RLOS Initiation"+ "misseddoc is:" +mandatoryDocName.toString());

			if(mandatoryDocName.length()<=0){

				PersonalLoanS.mLogger.info("RLOS Initiation"+ "misseddoc is: inside if condition");

			}
			else{
				PersonalLoanS.mLogger.info("RLOS Initiation"+ "misseddoc is: inside if condition");
				PersonalLoanS.mLogger.info(""+"length of missed document mandatoryDocName.length1111222"+mandatoryDocName.length());
				throw new ValidatorException(new FacesMessage("You have not attached Mandatory Documents: "+mandatoryDocName.toString()));
			}

		}
		catch(Exception excp)//applied temprary for testing in UAT as suggested by projects--to be removed later for alerts
		{
			PersonalLoanS.mLogger.info("Exception occurred in IncomingDoc method :"+excp);
			PersonalLoanS.printException(excp);
		}
	}



	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to loadPicklist for OECD    

	 ***********************************************************************************  */

	public void loadPickListOECD()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		LoadPickList("OECD_CRSFlagReason", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_CRSReason with (nolock) order by Code");
		LoadPickList("OECD_CountryBirth", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
		//LoadPickList("OECD_townBirth", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_city with (nolock) order by Code");
		LoadPickList("OECD_CountryTaxResidence", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
		//added by prabhakar
		LoadPickList("OECD_CustomerType","SELECT * FROM (SELECT '--Select--' AS customername UNION ALL  SELECT 'P-'+FirstName+' '+LAstName  FROM ng_rlos_customer with (nolock) where wi_name='"+formObject.getWFWorkitemName()+"'   UNION ALL SELECT 'G-'+firstname+' '+lastname as customername FROM ng_RLOS_GR_GuarantorDetails with (nolock) where guarandet_wi_name='"+formObject.getWFWorkitemName()+"' and firstname IS NOT NULL  union ALL SELECT 'S-'+FistName+' '+lastname+'-'+passportNo as customername FROM ng_RLOS_GR_SupplementCardDetails with (nolock) WHERE  supplement_WIname='"+formObject.getWFWorkitemName()+"') as u where customername is not null");
		//OECD_noTinReason
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Disable Customer Fragment fields  

	 ***********************************************************************************  */


	public void setDisabled()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		PersonalLoanS.mLogger.info("PersonnalLoanS> "+"Inside PLCommon ->setDisabled()");
		String fields="cmplx_Customer_FirstNAme,cmplx_Customer_MiddleNAme,cmplx_Customer_LastNAme,cmplx_Customer_age,cmplx_Customer_DOb,cmplx_Customer_gender,cmplx_Customer_EmiratesID,cmplx_Customer_IdIssueDate,cmplx_Customer_EmirateIDExpiry,cmplx_Customer_MotherName,cmplx_Customer_PAssportNo,cmplx_Customer_PassPortExpiry,cmplx_Customer_VisaNo,cmplx_Customer_VIsaExpiry,cmplx_Customer_SecNAtionApplicable,cmplx_Customer_Third_Nationaity_Applicable,cmplx_Customer_MobNo,cmplx_Customer_CIFNO,cmplx_Customer_Constitution";
		String[] array=fields.split(",");
		for(int i=0;array[i]!=null;i++)
		{
			PersonalLoanS.mLogger.info("PersonnalLoanS> "+"Inside PLCommon ->setDisabled()"+array[i]);

			formObject.setEnabled(array[i], false);

		}
	}

	public void expandFinacleCore(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.fetchFragment("Finacle_Core", "FinacleCore", "q_Finaclecore");
		formObject.setNGFrameState("Finacle_Core", 0);
		loadPicklistDDSReturn();
		try{
			String query="";
			//below query changed by nikhil 
			if(formObject.getLVWRowCount("cmplx_FinacleCore_FinaclecoreGrid")==0){

				//Deepak changed for PCASI-3092
				//query="select AcctType,CustRoleType,AcctId,AcctNm,isnull(format(convert(DATETIME,AccountOpenDate),'dd/MM/yyyy'),AcctStat,ClearBalanceAmount,AvailableBalance,EffectiveAvailableBalance,CreditGrade,'NA',AcctSegment,AcctSubSegment from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
				query="select  distinct AcctType,CustRoleType,AcctId,CIfID,AcctNm,AccountOpenDate,AcctStat,'-' as ClearBal,AvailableBalance,EffectiveAvailableBalance,CreditGrade,'',child_wi, AcctSegment,AcctSubSegment from ng_RLOS_CUSTEXPOSE_AcctDetails where Child_Wi = '"+formObject.getWFWorkitemName()+"' and CustRoleType='Main' and acctstat<>'CLOSED' and cifId in (select CIF_ID from ng_RLOS_GR_FinacleCRMCustInfo with (nolock) where ChildMapping = (select ParentMapping from ng_RLOS_FinacleCRMCustInfo with (nolock) where wi_name   = '"+formObject.getWFWorkitemName()+"') and Consider_For_Obligations = 'true' )";
				List<List<String>> list_acc=formObject.getDataFromDataSource(query);
				for(List<String> mylist : list_acc)
				{
					PersonalLoanS.mLogger.info( "Data to be added in cmplx_FinacleCore_FinaclecoreGrid Grid: "+mylist.toString());
					formObject.addItemFromList("cmplx_FinacleCore_FinaclecoreGrid", mylist);
				}		
			}

			if(formObject.getLVWRowCount("cmplx_FinacleCore_liendet_grid")==0){

				query="select distinct AcctId,LienId,LienAmount,LienRemarks,isnull(LienReasonCode,''),isnull(LienStartDate,''),isnull(LienExpDate,'') from ng_rlos_FinancialSummary_LienDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"' ORDER BY LienExpDate desc";
				//changed ended
				List<List<String>> list_lien=formObject.getDataFromDataSource(query);
				for(List<String> mylist : list_lien)
				{
					PersonalLoanS.mLogger.info( "Data to be added in Grid: "+mylist.toString());
					formObject.addItemFromList("cmplx_FinacleCore_liendet_grid", mylist);
				}
			}
			if(formObject.getLVWRowCount("cmplx_FinacleCore_sidet_grid")==0){

				//changed here in this query
				query="select AcctId,SiAmount,SiRemarks,'',isnull(format(convert(DATETIME,NextExecDate),'dd/MM/yyyy'),'') from ng_rlos_FinancialSummary_SiDtls with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"' ORDER BY NextExecDate desc";
				List<List<String>> list_SIDet=formObject.getDataFromDataSource(query);
				//changed ended

				for(List<String> mylist : list_SIDet)
				{
					PersonalLoanS.mLogger.info("Data to be added in Grid: "+mylist.toString());

					formObject.addItemFromList("cmplx_FinacleCore_sidet_grid", mylist);
				}
			}
			if(formObject.getLVWRowCount("cmplx_FinacleCore_DDSgrid")==0){

				/*query="select  isnull(AccountType,''),isnull(Account_Number,''),chqtype,chqamt,ChequeNumber,isnull(format(convert(datetime,chqretdate),'dd/MM/yyyy'),'')as returnDate,chqretcode,isnull(ddsclear,''),typeofret,'','','','',DDS_wi_name,Consider_For_Obligations,source from NG_RLOS_GR_DDSreturn where DDS_wi_name ='"+formObject.getWFWorkitemName()+"' union all select  case when (select count(AcctId) from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where AcctId = AcctId and CifId=CifId and Child_Wi='"+formObject.getWFWorkitemName()+"')>1 then 'Individual_CIF' else (select distinct(Account_Type) from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where AcctId = AcctId and CifId=CifId and Child_Wi='"+formObject.getWFWorkitemName()+"') end ,AcctId,returntype,returnAmount,returnNumber,isnull(format(convert(datetime,returnDate),'dd/MM/yyyy'),'')as returnDate,isnull(retReasonCode,''),isnull(instrumentdate,''),returnType,'','','','','"+formObject.getWFWorkitemName()+"','true','Internal' from ng_rlos_FinancialSummary_ReturnsDtls with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"' and returnNumber not in (select ChequeNumber from NG_RLOS_GR_DDSreturn where DDS_wi_name='"+formObject.getWFWorkitemName()+"') union all  select '-','-',ChqType,Amount,Number,isnull(format(convert(datetime,ReturnDate),'dd/MM/yyyy'),'')as returnDate,ReasonCode,'',ProviderNo,'','','','',Child_wi,'true','External' from ng_rlos_cust_extexpo_ChequeDetails where Child_wi ='"+formObject.getWFWorkitemName()+"' and (Number<>'' OR Number is null) and Number not in (select isnull(ChequeNumber,'') from NG_RLOS_GR_DDSreturn where DDS_wi_name='"+formObject.getWFWorkitemName()+"') ORDER BY returnDate DESC";
				List<List<String>> list_DDSDet=formObject.getDataFromDataSource(query);
				PersonalLoanS.mLogger.info( "query to be added in list_DDSDet Grid: "+query);

				//changed ended

				for(List<String> mylist : list_DDSDet)
				{
					PersonalLoanS.mLogger.info( "Data to be added in list_DDSDet Grid: "+mylist.toString());

					formObject.addItemFromList("cmplx_FinacleCore_DDSgrid", mylist);
				}*/
				query="select  isnull(AccountType,''),isnull(Account_Number,''),chqtype,chqamt,ChequeNumber,isnull(format(convert(datetime,chqretdate),'dd/MM/yyyy'),'')as returnDate,chqretcode,isnull(ddsclear,''),typeofret,'','','','',DDS_wi_name,Consider_For_Obligations,source from NG_RLOS_GR_DDSreturn where DDS_wi_name ='"+formObject.getWFWorkitemName()+"' union all select  case when (select count(AcctId) from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where AcctId = AcctId and CifId=CifId and Child_Wi='"+formObject.getWFWorkitemName()+"')>1 then 'Individual_CIF' else (select distinct(Account_Type) from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where AcctId = AcctId and CifId=CifId and Child_Wi='"+formObject.getWFWorkitemName()+"') end ,AcctId,returntype,returnAmount,returnNumber,isnull(format(convert(datetime,returnDate),'dd/MM/yyyy'),'')as returnDate,isnull(retReasonCode,''),isnull(instrumentdate,''),returnType,'','','','','"+formObject.getWFWorkitemName()+"','true','Internal' from ng_rlos_FinancialSummary_ReturnsDtls with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"' and returnNumber not in (select ChequeNumber from NG_RLOS_GR_DDSreturn where DDS_wi_name='"+formObject.getWFWorkitemName()+"') union all  select '-','-',ChqType,Amount,Number,isnull(format(convert(datetime,ReturnDate),'dd/MM/yyyy'),'')as returnDate,ReasonCode,'',ProviderNo,'','','','',Child_wi,'true','External' from ng_rlos_cust_extexpo_ChequeDetails where Child_wi ='"+formObject.getWFWorkitemName()+"' and (Number<>'' OR Number is null) and Number not in (select ChequeNumber from NG_RLOS_GR_DDSreturn where DDS_wi_name='"+formObject.getWFWorkitemName()+"') ORDER BY returnDate DESC";
				List<List<String>> list_DDSDet=formObject.getDataFromDataSource(query);
				PersonalLoanS.mLogger.info( "query to be added in list_DDSDet Grid: "+query);
				PersonalLoanS.mLogger.info( "data to be added in list_DDSDet Grid: "+list_DDSDet);
				String return_date="";
				try
				{
					int count=list_DDSDet.size();

					for(int i=0;i<count;i++)
					{
						return_date=list_DDSDet.get(i).get(5);
						String[] date=return_date.split("-");
						return_date=date[2]+"/"+date[1]+"/"+date[0];
						list_DDSDet.get(i).set(5, return_date);
					}
				}
				catch(Exception ex)
				{
					PersonalLoanS.mLogger.info( "Error parsing date ::"+return_date);
				}

				//changed ended
				formObject.clear("cmplx_FinacleCore_DDSgrid");
				for(List<String> mylist : list_DDSDet)
				{
					//CreditCard.mLogger.info( "Data to be added in list_DDSDet Grid: "+mylist.toString());
					formObject.addItemFromList("cmplx_FinacleCore_DDSgrid", mylist);
				}

			}
			if("cpv".equalsIgnoreCase(formObject.getWFActivityName())|| "DDVT_Checker".equalsIgnoreCase(formObject.getWFActivityName()) || "DDVT_Maker".equalsIgnoreCase(formObject.getWFActivityName()))
			{
				formObject.setEnabled("FinacleCore_Frame5", false);
				formObject.setLocked("FinacleCore_DatePicker2", true);
				formObject.setLocked("FinacleCore_Frame6", true);
				formObject.setLocked("FinacleCore_cheqretdate", true);
				formObject.setEnabled("FinacleCore_Combo1", false);
			}
			if("Original_Validation".equalsIgnoreCase(formObject.getWFActivityName()))
			{
				formObject.setEnabled("FinacleCore_Frame5", false);
				formObject.setLocked("FinacleCore_DatePicker2", true);
				//formObject.setEnabled("FinacleCore_Frame6", false);
			}
			if("cad_analyst1".equalsIgnoreCase(formObject.getWFActivityName())){
				formObject.setLocked("FinacleCore_DatePicker2", false);
			}
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("Exception while setting data in grid:"+e.getMessage()+" "+e);
			//throw new ValidatorException(new FacesMessage("Error while setting data in account grid"));
		}
	}

	public void expandCustDetVer(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.fetchFragment("Cust_Det_Ver", "CustDetailVerification1", "q_CustDetailVeriFCU");
		//30Nov17 code was available at Offshore without any comment and the same was not present at offshore start.
		int framestate0=formObject.getNGFrameState("Address_Details_container");
		if(framestate0 != 0){
			formObject.fetchFragment("Address_Details_container", "AddressDetails", "q_AddressDetails");
			formObject.setNGFrameState("Address_Details_container",0);
			alignDemographiTab(formObject);
			/*//formObject.setTop("Alt_Contact_container",formObject.getTop("Address_Details_container")+formObject.getHeight("Address_Details_Container"));
			formObject.setTop("Alt_Contact_container",formObject.getTop("Address_Details_container")+formObject.getHeight("Address_Details_container")); 
			formObject.setTop("ReferenceDetails",formObject.getTop("Alt_Contact_container")+25);
			//formObject.setTop("Supplementary_Container",formObject.getTop("ReferenceDetails")+50);
			formObject.setTop("Card_Details", formObject.getTop("ReferenceDetails")+25);
			formObject.setTop("FATCA", formObject.getTop("Card_Details")+30);
			formObject.setTop("KYC", formObject.getTop("FATCA")+20);
			formObject.setTop("OECD", formObject.getTop("KYC")+20);*/
		}
		int framestate1=formObject.getNGFrameState("Alt_Contact_container");
		if(framestate1 != 0){
			formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
			formObject.setNGFrameState("Alt_Contact_container",0);
			alignDemographiTab(formObject);
			/*formObject.setTop("Alt_Contact_container",formObject.getTop("Address_Details_container")+formObject.getHeight("Address_Details_container"));
			//formObject.setTop("Supplementary_Container",formObject.getTop("ReferenceDetails")+250);
			formObject.setTop("ReferenceDetails",formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container"));
			formObject.setTop("Card_Details", formObject.getTop("ReferenceDetails")+20);
			formObject.setTop("FATCA", formObject.getTop("Card_Details")+30);
			formObject.setTop("KYC", formObject.getTop("FATCA")+20);
			formObject.setTop("OECD", formObject.getTop("KYC")+20);*/
		}

		//below code commented by nikhil
		//autopopulateValues(formObject);
		//30Nov17 code was available at Offshore without any comment and the same was not present at offshore start.
		formObject.setVisible("Credit_card_Enq", false);
		formObject.setVisible("Case_History", false);
		formObject.setVisible("LOS", false);

		LoadPickList("cmplx_CustDetailVerification_emirates_upd","select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_emirate with (nolock) order by Code");

	}

	public void expandDecision(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try{

			//change by saurabh on 10th Dec
			/*openDemographicTabs();

			int framestate2=formObject.getNGFrameState("EmploymentDetails");
			PersonalLoanS.mLogger.info("framestate for Employment is: "+framestate2);
			if(framestate2 == 0){
				PersonalLoanS.mLogger.info("EmploymentDetails");
			}
			else {
				formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmpDetails");
				PersonalLoanS.mLogger.info("fetched employment details");
			}*/
			PersonalLoanS.mLogger.info("Before guarantor fetch");
			int framestate_guatantor=formObject.getNGFrameState("GuarantorDet");
			//	PersonalLoanS.mLogger.info("framestate for GuarantorDet is: "+framestate2);
			if(framestate_guatantor != 0 && isCustomerMinor(formObject)){
				formObject.fetchFragment("GuarantorDet", "GuarantorDetails", "q_Guarantor");
				loadPicklist_Guarantor();
				formObject.setTop("IncomeDEtails",formObject.getTop("GuarantorDet")+formObject.getHeight("GuarantorDet")+15);
				//	adjustFrameTops("GuarantorDet,IncomeDEtails");
			}
			PersonalLoanS.mLogger.info("After guarantor fetch");

			/*int framestate1=formObject.getNGFrameState("IncomeDEtails");
			if(framestate1 == 0){
				PersonalLoanS.mLogger.info("Incomedetails");
			}
			else {
				//formObject.fetchFragment("IncomeDEtails", "IncomeDetails", "q_IncomeDetails");
				PersonalLoanS.mLogger.info("fetched income details");
				//formObject.setTop("IncomeDEtails",formObject.getTop("ProductDetailsLoader")+formObject.getHeight("ProductDetailsLoader"));
				expandIncome();
			}*/
			/*int framestate3=formObject.getNGFrameState("EligibilityAndProductInformation");
			if(framestate3 == 0){
				PersonalLoanS.mLogger.info("EligibilityAndProductInformation");
			}
			else {
				formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligibilityProdInfo");

				PersonalLoanS.mLogger.info("fetched EligibilityAndProductInformation details");
			}*/
			PersonalLoanS.mLogger.info("Before decision fetch call");
			formObject.fetchFragment("DecisionHistory", "DecisionHistory", "q_Decision");
			new PLCommon().adjustFrameTops("Notepad_Values,DecisionHistory,ReferHistory");
			PersonalLoanS.mLogger.info("after  decision fetch call");
			formObject.setVisible("cmplx_Decision_Manual_deviation_reason", false);
			//added by akshay on 12/12/17
			PersonalLoanS.mLogger.info("Before refer history");
			List<String> referWorksteps_List=Arrays.asList("CSM","DDVT_maker","DDVT_Checker","RM_Review","CAD_Analyst1","CAD_Analyst2","CPV","CPV_Analyst","FCU","FPU","Compliance","DSA_CSO_Review","Orig_Validation","Disbursal","SalesCoordinator","Original_Validation","Document_Checker","Documnet_Checker","Disbursal_Maker","Disbursal_Checker","PostDisbursal_Maker","PostDisbursal_Maker","ToTeam","DDVT_Hold","CustomerHold");
			if(referWorksteps_List.contains(formObject.getWFActivityName())){
				if(!formObject.isVisible("ReferHistory_Frame1")){
					formObject.fetchFragment("ReferHistory", "ReferHistory", "q_ReferHistory");
					formObject.setNGFrameState("ReferHistory", 0);
				}
			}
			PersonalLoanS.mLogger.info("After refer history");
			//ended by akshay on 12/12/17

			loadInDecGrid();
			PersonalLoanS.mLogger.info("after load in dec grid");
			//saveIndecisionGrid();//arun 25/12/17
			loadPicklist3();
			//below code added by yash on 15/12/2017 for toteam 
			PersonalLoanS.mLogger.info("To team code");
			if(!NGFUserResourceMgr_PL.getGlobalVar("PL_Reject").equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")) && !"REFER".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")) && !"Pending for documentation".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")))
			{
				formObject.setLocked("DecisionHistory_DecisionReasonCode",true);
				formObject.setLocked("DecisionHistory_DecisionSubReason",true);

			}
			//above code added by yash on 15/12/2017 for toteam
			if(NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Manual_Deviation"))){
				formObject.setLocked("DecisionHistory_Button6",false);
				formObject.setLocked("cmplx_Decision_CADDecisiontray", false);        
			}
			else{
				formObject.setLocked("DecisionHistory_Button6",true);
				formObject.setLocked("cmplx_Decision_CADDecisiontray", true); 
				formObject.setNGValue("cmplx_Decision_CADDecisiontray", "Select");
			}
			
			
			//code change by bandana start
			int framestate4=formObject.getNGFrameState("Details");
			if(framestate4 == 0){
				PersonalLoanS.mLogger.info("Details");
			}
			else {
				
				formObject.fetchFragment("Details", "CC_Loan", "q_CC_Loan");
				loadPicklist_ServiceRequest();
				PersonalLoanS.mLogger.info("fetched servicerequest details");
				
			}
			//code change by bandana ends
			
			//below code added by shweta for CPV chnages 17-10 seq#138//by shweta
/*			if(formObject.isVisible("CustDetailVerification_Frame1")==false && ("CAD_Analyst1".equalsIgnoreCase(formObject.getWFActivityName()) || "Cad_Analyst2".equalsIgnoreCase(formObject.getWFActivityName())))
			{
				load_Customer_Details_Verification(formObject);//by shweta
				adjustFrameTops("Cust_Detail_verification,Home_country_verification,Office_verification,Smart_check");
			}  */ // hritik 27.6.21 
			//above code added by shweta for CPV chnages 17-10  seq#138
		}
		catch(Exception ex){
			printException(ex);
		}
		PersonalLoanS.mLogger.info("Before account createy");
		//added Tanshu Aggarwal(22/06/2017)
		if	(NGFUserResourceMgr_PL.getGlobalVar("PL_Y").equalsIgnoreCase(formObject.getNGValue("Is_Account_Create")) && NGFUserResourceMgr_PL.getGlobalVar("PL_Y").equalsIgnoreCase(formObject.getNGValue("Is_Customer_Create")))
		{
			PersonalLoanS.mLogger.info("inside both req Y");
			formObject.setLocked("DecisionHistory_Button2", true);
		}
		else{
			PersonalLoanS.mLogger.info( "inside both req N");
			formObject.setLocked("DecisionHistory_Button2", false);
		}
		//added Tanshu Aggarwal(22/06/2017)	
		//++Below code added by nikhil 13/11/2017 for Code merge

		//++ below code added by abhishek  to check workitem on CPV as per FSD 2.7.3

		PersonalLoanS.mLogger.info("Before WFinstrument code");
		if (formObject.getWFActivityName().equalsIgnoreCase("Cad_Analyst1")){
			//for decision fragment made changes 8th dec 2017
			/*formObject.setVisible("DecisionHistory_Label10", false);
				formObject.setVisible("cmplx_DEC_New_CIFID", false);*/
			//for decision fragment made changes 8th dec 2017




			String sQuery = "SELECT activityname FROM WFINSTRUMENTTABLE with (nolock) WHERE ProcessInstanceID = '"+formObject.getWFWorkitemName()+"'";
			boolean flag = false;
			/*StringBuilder values = new StringBuilder();
		        values.append(",'" + sProcessName + "'");*/

			String listValues ="";
			List<List<String>> list=formObject.getDataFromDataSource(sQuery);
			PersonalLoanS.mLogger.info(" In CC CAD workstep decision history expand"+ "Done button click::query result is::"+list );
			for(int i =0;i<list.size();i++ ){
				if(i==0){
					listValues += list.get(i).get(0);
					// values.append(list.get(i).get(0));
				}else{
					listValues += "|"+list.get(i).get(0);
					//values.append(",'" + sProcessName + "'");
				}

			}

			sQuery = "Select cpv_decision FROM NG_PL_EXTTABLE with (nolock) WHERE PL_wi_name='"+formObject.getWFWorkitemName()+"'";
			List<List<String>> list1=formObject.getDataFromDataSource(sQuery);
			PersonalLoanS.mLogger.info(" In CC CAD workstep decision history expand"+ "cpv dec value is::"+list1 );
			listValues +="#"+list1.get(0).get(0);
			PersonalLoanS.mLogger.info(" In CC CAD workstep decision history expand"+ "list value is::"+listValues );
			formObject.setNGValue("DecisionHistory_CadTempField",listValues);


			PersonalLoanS.mLogger.info("After WFinstrument code");

		}
		//for decision fragment made changes 8th dec 2017

		//for decision fragment made changes 8th dec 2017
		else if(formObject.getWFActivityName().equalsIgnoreCase("Smart_CPV")){

			//for decision fragment made changes 8th dec 2017
			if(formObject.getNGValue("cmplx_Decision_contactableFlag").equalsIgnoreCase("true")){
				formObject.fetchFragment("Notepad_Values", "NotepadDetails", "q_Note");
				formObject.setVisible("NotepadDetails_Label6", true);
				formObject.setVisible("NotepadDetails_insqueue", true);
				formObject.setVisible("NotepadDetails_inscompletion", true);
				formObject.setVisible("NotepadDetails_Label10", true);
				formObject.setVisible("NotepadDetails_Actdate", true);
				formObject.setVisible("NotepadDetails_Label11", true);
				formObject.setVisible("NotepadDetails_Actusername", true);
				formObject.setVisible("NotepadDetails_Label9", true);
				formObject.setVisible("NotepadDetails_ActuserRemarks", true);
				LoadPickList("NotepadDetails_notedesc", "select '--Select--'  union select  description from ng_master_notedescription  with (nolock) where workstep != 'NA'");	//PCASI-3154
				formObject.setNGValue("NotepadDetails_notedesc","--Select--");

				formObject.fetchFragment("Smart_check", "SmartCheck", "q_SmartCheck");
				formObject.fetchFragment("Office_verification", "OfficeandMobileVerification", "q_OffVerification");

				//	formObject.setEnabled("DecisionHistory_nonContactable", false);
				formObject.setEnabled("NotepadDetails_Frame1", false);
				//	formObject.setEnabled("DecisionHistory_Frame1", false);
				formObject.setEnabled("SmartCheck_Frame1", false);
				formObject.setEnabled("OfficeandMobileVerification_Frame1", false);

				//	formObject.setEnabled("DecisionHistory_cntctEstablished", true);

				formObject.setNGValue("cmplx_Decision_Decision","Smart CPV Hold");

				formObject.setNGFrameState("Office_verification", 1);
				formObject.setNGFrameState("Smart_check", 1);
				formObject.setNGFrameState("Notepad_Values", 1);
			}else{
				//for decision fragment made changes 8th dec 2017

				/*formObject.setEnabled("DecisionHistory_Frame1", true);
     				formObject.setEnabled("DecisionHistory_cntctEstablished", false);
     				formObject.setEnabled("DecisionHistory_nonContactable", true);
     				formObject.setNGValue("cmplx_Decision_Decision","--Select--");*/
				//for decision fragment made changes 8th dec 2017


			}

		}
		
		//formObject.setTop("ReferHistory", formObject.getTop("DecisionHistory")+formObject.getHeight("DecisionHistory_Frame1")+20);

		//for FCU autopopulation aloc type and company
		PersonalLoanS.mLogger.info("After Smart CPV code");
		
		String activity = formObject.getWFActivityName();
		
	//below code added by aastha for jira pcsp-1151
		PersonalLoanS.mLogger.info("activity"+activity);
		if(!"CPV".equalsIgnoreCase(activity) && !"FCU".equalsIgnoreCase(activity) && !"FPU".equalsIgnoreCase(activity) && !"CAD_Analyst1".equalsIgnoreCase(activity) && !"Cad_Analyst2".equalsIgnoreCase(activity)){
			formObject.setVisible("NotepadDetails_Frame3", false);
		}
		//changes done for jira-1151

	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to loadPicklist for Decision

	 ***********************************************************************************  */
	public void expandIncome(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.fetchFragment("IncomeDEtails", "IncomeDetails", "q_IncomeDetails");
		income_Dectech();
		String EmploymentType = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 6);

		if (EmploymentType.equalsIgnoreCase("Salaried")||EmploymentType.equalsIgnoreCase("Salaried Pensioner")){
			formObject.setEnabled("cmplx_IncomeDetails_RentalIncome", true);
			formObject.setEnabled("cmplx_IncomeDetails_EducationalAllowance", true);

		}
	}

	public void loadPicklist3()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		try
		{
			PersonalLoanS.mLogger.info("RLOSCommon"+ "Inside loadpicklist3:");
			String activityName = formObject.getWFActivityName();
			//LoadPickList("cmplx_Decision_refereason", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_RejectReasons with (nolock) order by code");
			formObject.clear("cmplx_Decision_Decision");

			String query2="select description FROM  ng_MASTER_RejectReasons with (nolock) where isActive='Y' order by description";
			LoadPickList1("DecisionHistory_DecisionReasonCode", query2);
			String Query = "select * from (select '--Select--' as decision union select decision from NG_MASTER_Decision with (nolock)  where ProcessName='PersonalLoanS' and workstepname='"+formObject.getWFActivityName()+"')t order by case when decision = '--Select--' then 0 else 1 end";
			PersonalLoanS.mLogger.info("RLOSCommon Load desison Drop down: "+Query );
			LoadDecisionSubReason(formObject);

			LoadPickList("cmplx_Decision_Decision", Query);
			LoadPickList("cmplx_Decision_CADDecisiontray", "select 'Select' as Refer_Credit  union select convert(varchar, Refer_Credit)  from NG_Master_ReferCredit with (nolock) order by Refer_Credit desc"); //Arun (12/10)			
			//LoadPickList("DecisionHistory_DecisionReasonCode", "select * from (select '--Select--' as description union select description from ng_MASTER_RejectReasons  with (nolock) where IsActive='Y')t order by case when description = '--Select--' then 0 else 1 end");
			//LoadPickList("DecisionHistory_DecisionReasonCode", "SELECT '--Select--' as description,'' as code union select description,code from ng_MASTER_DecisionReason where workstep='"+formObject.getWFActivityName()+"' and decision='"+formObject.getNGValue("cmplx_Decision_Decision")+"' order by code");
			if("CAD_Analyst1".equalsIgnoreCase(activityName)){
				LoadPickList("cmplx_Decision_Decision", Query);
			}
			else{
				LoadPickList("cmplx_Decision_Decision", Query);	
			}
		}
		catch(Exception e){ 
			PersonalLoanS.mLogger.info("PLCommon"+"Exception Occurred loadPicklist3 :"+e.getMessage());
			printException(e);
		}
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to loadPicklist for Address

	 ***********************************************************************************  */


	public void loadPicklist_Address()
	{
		try
		{
			//AddressDetails_city
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			PersonalLoanS.mLogger.info("RLOS_Common"+ "Inside loadPicklist_Address: "); 
			LoadPickList("AddressDetails_addtype", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_AddressType with (nolock) order by code");
			//LoadPickList("AddressDetails_city", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_city with (nolock) order by code");
			//LoadPickList("AddressDetails_state", "select '--Select--'as description,'' as code union select convert(varchar, description),code from NG_MASTER_state with (nolock) order by code");
			//LoadPickList("AddressDetails_country", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
			LoadPickList("AddressDetails_ResidenceAddrType", "select '--Select--' as description,'' as code union select  convert(varchar, description),code from NG_MASTER_ResidAddressType with (nolock)  where isActive='Y' order by code");
			LoadPickList("AddressDetails_CustomerType","SELECT * FROM (SELECT '--Select--' AS customername UNION ALL  SELECT 'P-'+FirstName+' '+LAstName  FROM ng_rlos_customer where wi_name='"+formObject.getWFWorkitemName()+"'   UNION ALL SELECT 'G-'+firstname+' '+lastname as customername FROM ng_RLOS_GR_GuarantorDetails where guarandet_wi_name='"+formObject.getWFWorkitemName()+"' and firstname IS NOT NULL  union ALL SELECT 'S-'+FistName+' '+lastname+'-'+passportNo as customername FROM ng_RLOS_GR_SupplementCardDetails WHERE  supplement_WIname='"+formObject.getWFWorkitemName()+"') as u where customername is not null");
		}	
		catch(Exception e){ 
			PersonalLoanS.mLogger.info("PLCommon"+"Exception Occurred loadPicklist_Address :"+e.getMessage());
			printException(e);
		}
	}

	/*public void loadPicklist_FATCA()
	{
		try
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			LoadPickList("cmplx_FATCA_CustomerType","SELECT * FROM (SELECT '--Select--' AS customername UNION ALL  SELECT 'P-'+CAST(customername AS VARCHAR(200))  FROM NG_PL_EXTTABLE where PL_wi_name='"+formObject.getWFWorkitemName()+"'   UNION ALL SELECT 'G-'+firstname+' '+lastname as customername FROM ng_RLOS_GR_GuarantorDetails where guarandet_wi_name='"+formObject.getWFWorkitemName()+"' and firstname IS NOT NULL  union ALL SELECT 'S-'+FistName+' '+lastname as customername FROM ng_RLOS_GR_SupplementCardDetails WHERE  supplement_WIname='"+formObject.getWFWorkitemName()+"') as u where customername is not null");
			LoadPickList("FATCA_USRelaton", " select '--Select--' as description, '' as code  union select convert(varchar,description),code  from ng_master_usrelation with (nolock) order by code");
			PersonalLoanS.mLogger.info("FATCA LoadPickList");
			//FATCA_USRelaton
		}
		catch (Exception e)
		{
			PersonalLoanS.mLogger.info("Fatca LoadPickList"+e);
		}
	}*/
	//added by prabhakar
	public void loadPicklist_KYC()
	{
		try
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			LoadPickList("KYC_CustomerType","SELECT * FROM (SELECT '--Select--' AS customername UNION ALL  SELECT 'P-'+FirstName+' '+LAstName  FROM ng_rlos_customer where wi_name='"+formObject.getWFWorkitemName()+"'   UNION ALL SELECT 'G-'+firstname+' '+lastname as customername FROM ng_RLOS_GR_GuarantorDetails where guarandet_wi_name='"+formObject.getWFWorkitemName()+"' and firstname IS NOT NULL  union ALL SELECT 'S-'+FistName+' '+lastname+'-'+passportNo as customername FROM ng_RLOS_GR_SupplementCardDetails WHERE  supplement_WIname='"+formObject.getWFWorkitemName()+"') as u where customername is not null");
			PersonalLoanS.mLogger.info("Start load kyc");
			//PCASI - 3721
			formObject.fetchFragment("Risk_Rating", "RiskRating", "q_riskrating");
			//float riskRate = Float.parseFloat(formObject.getNGValue("cmplx_RiskRating_Total_riskScore"));
			//formObject.setNGValue("KYC_Combo1", "N"); //hritik 3721
			if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
				PersonalLoanS.mLogger.info("inside if ntb: kyc");
				formObject.setNGValue("KYC_Combo1", "N");
				//formObject.setLocked("KYC_CustomerType", true);
				//formObject.setLocked("KYC_Combo2", true);
				formObject.setLocked("KYC_DatePicker1", true);
			}
			PersonalLoanS.mLogger.info("KYC_Combo1 :: "+formObject.getNGValue("KYC_Combo1") +" , NTB case :: "+formObject.getNGValue("cmplx_Customer_NTB"));

			PersonalLoanS.mLogger.info("KYC LoadPickList");
		}
		catch (Exception e)
		{
			PersonalLoanS.mLogger.info("KYC LoadPickList"+e);
		}
	}


	public void loadPicklist_Guarantor()
	{

		LoadPickList("GuarantorDetails_title", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_title with (nolock) order by code");
		LoadPickList("GuarantorDetails_gender", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_gender with (nolock) order by Code");
		LoadPickList("GuarantorDetails_nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
		LoadPickList("GuarantorDetails_designation", "select '--Select--' as description,'' as code union select  convert(varchar, description),code from NG_MASTER_Designation with (nolock)  where isActive='Y' order by code");
		LoadPickList("GuarantorDetails_empType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_EmploymentType with (nolock)  where isActive='Y' order by code");
		LoadPickList("GuarantorDetails_employmentStatus", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_EmploymentStatus with (nolock)  where isActive='Y' order by code");
		LoadPickList("GuarantorDetails_MAritalStatus", "select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_Maritalstatus  with (nolock)  order by Code");
		
	}
	//added by prabhakar
	/*public void loadPicklist_OECD()
		{
			try
			{
				PersonalLoanS.mLogger.info("OECD LoadPickList");
				FormReference formObject = FormContext.getCurrentInstance().getFormReference();
				LoadPickList("OECD_CustomerType","SELECT * FROM (SELECT '--Select--' AS customername UNION ALL  SELECT 'P-'+CAST(customername AS VARCHAR(200))  FROM NG_PL_EXTTABLE where PL_wi_name='"+formObject.getWFWorkitemName()+"'   UNION ALL SELECT 'G-'+firstname+' '+lastname as customername FROM ng_RLOS_GR_GuarantorDetails where guarandet_wi_name='"+formObject.getWFWorkitemName()+"' and firstname IS NOT NULL  union ALL SELECT 'S-'+FistName+' '+lastname as customername FROM ng_RLOS_GR_SupplementCardDetails WHERE  supplement_WIname='"+formObject.getWFWorkitemName()+"') as u where customername is not null");
			}
			catch (Exception e)
			{
				PersonalLoanS.mLogger.info("OECD LoadPickList"+e);
			}
		}*/
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to loadPicklist for Loan Details  

	 ***********************************************************************************  */

	public void loadPicklist_LoanDetails()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		LoadPickList("cmplx_LoanDetails_repfreq", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Frequency with (nolock) order by Code");
		LoadPickList("cmplx_LoanDetails_insplan", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_InstallmentType with (nolock) order by Code");
		LoadPickList("cmplx_LoanDetails_collecbranch", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_COLLECTION_BRANCH with (nolock)");
		LoadPickList("cmplx_LoanDetails_ddastatus", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_DDASTATUS with (nolock)");
		LoadPickList("LoanDetails_modeofdisb", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_ModeOfDisbursal with (nolock)");
		//LoadPickList("LoanDetails_disbto", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_BankName with (nolock)");
		LoadPickList("LoanDetails_holdcode", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_HoldCode with (nolock) order by code");
		LoadPickList("cmplx_LoanDetails_paymode", "select '--Select--' as description ,'' as code union select convert(varchar, Description),code from NG_MASTER_PAYMENTMODE with (nolock) order by code");
		LoadPickList("cmplx_LoanDetails_status", "select '--Select--','' union select convert(varchar, Description),code from NG_MASTER_STATUS with (nolock)");
		LoadPickList("cmplx_LoanDetails_bankdeal", "select '--Select--','' as code union select Description,code from NG_MASTER_Dealing_Bank with(nolock) order by code");
		//By shweta, added fast saver acct type 
		LoadPickList("LoanDetails_accno","SELECT acctid FROM ng_RLOS_CUSTEXPOSE_AcctDetails WHERE AcctStat='Active' AND (Accttype LIKE '%SAVINGS ACCOUNT%' OR Accttype LIKE '%CURRENT ACCOUNT%' OR Accttype like '%FAST SAVER%') and child_wi='"+formObject.getWFWorkitemName()+"' union select New_Account_Number from ng_rlos_decisionHistory where wi_name='"+formObject.getWFWorkitemName()+"' and New_Account_Number is not null");//Changed by aman 2210
		LoadPickList("cmplx_LoanDetails_drawnon","SELECT acctid FROM ng_RLOS_CUSTEXPOSE_AcctDetails WHERE AcctStat='Active' AND (Accttype LIKE '%SAVINGS ACCOUNT%' OR Accttype LIKE '%CURRENT ACCOUNT%' OR Accttype like '%FAST SAVER%') and child_wi='"+formObject.getWFWorkitemName()+"' union select New_Account_Number from ng_rlos_decisionHistory where wi_name='"+formObject.getWFWorkitemName()+"' and New_Account_Number is not null");//Changed by aman 2210

	}
	public void loadPicklist_LiabilityCertificate()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery="select distinct documentname  from ng_rlos_gr_incomingDocument with (nolock) where documentname like 'liability_certificate%' and IncomingDocGR_Winame ='"+formObject.getWFWorkitemName()+"'";
		//cmplx_LoanDetails_loanemi
	LoadPickList("PostDisbursal_Bank_Name", "select '--Select--' as description,'' as code union select  convert(varchar, description),code from ng_master_bankname with (nolock)  where isActive='Y' order by code");
	LoadPickList("PostDisbursal_BG_Bank_Name", "select '--Select--' as description,'' as code union select  convert(varchar, description),code from ng_master_bankname with (nolock)  where isActive='Y' order by code");
	LoadPickList("PostDisbursal_Emirate", "select '--Select--' as description,'' as code union select  convert(varchar, description),code from ng_master_emirate with (nolock)  where isActive='Y' order by code");
	mLogger.info("LC query before load is ::"+sQuery);
	LoadPickList("PostDisbursal_LC_LC_Doc_Name", sQuery);

	LoadPickList("PostDisbursal_MCQ_Doc_Name", sQuery);
	LoadPickList("PostDisbursal_BG_LC_Doc_Name", sQuery);
	LoadPickList("PostDisbursal_NLC_LC_Doc_name", sQuery);
	formObject.setLocked("PostDisbursal_MCQ_Doc_Name",true);

	formObject.setLocked("PostDisbursal_BG_LC_Doc_Name",true);
	formObject.setLocked("PostDisbursal_NLC_LC_Doc_name",true);
	mLogger.info("LC query after load is ::"+sQuery);
	
	int row_count=formObject.getLVWRowCount("cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate");

	mLogger.info("LC query before load is ::"+sQuery);
	LoadPickList("PostDisbursal_LC_LC_Doc_Name", sQuery);

	LoadPickList("PostDisbursal_MCQ_Doc_Name", sQuery);
	LoadPickList("PostDisbursal_BG_LC_Doc_Name", sQuery);
	LoadPickList("PostDisbursal_NLC_LC_Doc_name", sQuery);
	formObject.setLocked("PostDisbursal_MCQ_Doc_Name",true);

	formObject.setLocked("PostDisbursal_BG_LC_Doc_Name",true);
	formObject.setLocked("PostDisbursal_NLC_LC_Doc_name",true);
	mLogger.info("LC query after load is ::"+sQuery);
	//int row_count=formObject.getLVWRowCount("cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate");
	
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to loadPicklist for Part Match 

	 ***********************************************************************************  */

	public void loadPicklist_PartMatch()
	{
		PersonalLoanS.mLogger.info("RLOS_Common"+ "Inside loadPicklist_PartMatch: ");
		//LoadPickList("PartMatch_nationality", "select '--Select--','--Select--' union select convert(varchar, Description),code from ng_MASTER_Country with (nolock)");
	}



	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Populate the Incoming Document Repeater   

	 ***********************************************************************************  */

	/*public void fetchIncomingDocRepeater(){

		PersonalLoanS.mLogger.info(" Inside loadAllCombo_LeadManagement_Documents_Deferral"+ "inside fetchIncomingDocRepeater");
		FormConfig formobject=FormContext.getCurrentInstance().getFormConfig();
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sActivityName = formObject.getWFActivityName();
		PersonalLoanS.mLogger.info("INSIDE INCOMING DOCUMENT:" +"");
		String Username=formObject.getUserName();//Arun (22/09/17)
		String requested_product ="";
		String requested_subproduct;
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		PersonalLoanS.mLogger.info("INSIDE INCOMING DOCUMENT value_doc_name:" +"valu of row count"+n);
		if(n>0)
		{
			for(int i=0;i<n;i++)
			{
				requested_product= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 1);
				PersonalLoanS.mLogger.info("INSIDE INCOMING DOCUMENT value_doc_name:" +requested_product);
				requested_subproduct= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2);
				PersonalLoanS.mLogger.info("INSIDE INCOMING DOCUMENT value_doc_name:" +requested_subproduct);

			}    
		}
		PersonalLoanS.mLogger.info("INSIDE INCOMING DOCUMENT value_doc_name outsid for:" +requested_product);

		//Disha started (10/6/17)

		List<String> repeaterHeaders = new ArrayList<String>();
		PersonalLoanS.mLogger.info("INSIDE INCOMING DOCUMENT value sActivityName for:" +sActivityName);
		if ("Original_Validation".equalsIgnoreCase(sActivityName) || "Dispatch".equalsIgnoreCase(sActivityName) || "Post_Disbursal".equalsIgnoreCase(sActivityName) || "Disbursal".equalsIgnoreCase(sActivityName) || "CC_Disbursal".equalsIgnoreCase(sActivityName) || "Collection_User".equalsIgnoreCase(sActivityName))
		{
			PersonalLoanS.mLogger.info("INSIDE INCOMING DOCUMENT value sActivityName for:" +"OV activity");
			repeaterHeaders.add("Document Name");

			repeaterHeaders.add("Expiry Date");
			repeaterHeaders.add("Mandatory");
			repeaterHeaders.add("Status");
			repeaterHeaders.add("Remarks");

			repeaterHeaders.add("Add from DMS");

			repeaterHeaders.add("Add from PC");

			repeaterHeaders.add("Scan");
			repeaterHeaders.add("View");

			repeaterHeaders.add("Print");

			repeaterHeaders.add("Download");
			repeaterHeaders.add("DocIndex");

			/*repeaterHeaders.add("OV Remarks");
			repeaterHeaders.add("OV Decision");
			repeaterHeaders.add("Approved By");*#/
			//added by yash on 19/12/2017
			repeaterHeaders.add("Deferred Until");
		}
		else 
		{
			PersonalLoanS.mLogger.info("INSIDE other INCOMING DOCUMENT value sActivityName for:" +" non OV activity");
			repeaterHeaders.add("Document Name");

			repeaterHeaders.add("Expiry Date");
			repeaterHeaders.add("Mandatory");
			repeaterHeaders.add("Status");
			repeaterHeaders.add("Remarks");

			repeaterHeaders.add("Add from DMS");

			repeaterHeaders.add("Add from PC");

			repeaterHeaders.add("Scan");
			repeaterHeaders.add("View");

			repeaterHeaders.add("Print");

			repeaterHeaders.add("Download");
			repeaterHeaders.add("DocIndex");
			/*repeaterHeaders.add("OV Remarks");
			repeaterHeaders.add("OV Decision");
			repeaterHeaders.add("Approved By");*#/
			//added by yash on 19/12/2017
			repeaterHeaders.add("Deferred Until");


		}
		PersonalLoanS.mLogger.info("INSIDE INCOMING DOCUMENT:" +"after making headers");

		FormContext.getCurrentInstance().getFormConfig().getConfigElement("ProcessName");

		List<List<String>> docName ;
		String documentName = null;
		String documentNameMandatory=null;
		String statusValue = null;
		String expiryDate = null;
		String Remarks= null;
		String DocInd= null;

		String query ;

		IRepeater repObj;
		PersonalLoanS.mLogger.info("INSIDE INCOMING DOCUMENT:" +"after creating the object for repeater");

		int repRowCount = 0;

		repObj = formObject.getRepeaterControl("IncomingDoc_Frame2");

		PersonalLoanS.mLogger.info("INSIDE INCOMING DOCUMENT:" +""+repObj.toString());



		query = "SELECT distinct DocName,ExpiryDate,Mandatory,DocSta,Remarks,DocInd,DeferredUntilDate FROM ng_rlos_incomingDoc with (nolock)  WHERE  wi_name='"+formObject.getWFWorkitemName()+"' order by Mandatory desc";
		PersonalLoanS.mLogger.info("query INSIDE INCOMING DOCUMENT:"+query);
		docName = formObject.getDataFromDataSource(query);
		PersonalLoanS.mLogger.info("Docname:"+docName);
		try{
			repObj.setRepeaterHeaders(repeaterHeaders);

			/*if ("Original_Validation".equalsIgnoreCase(sActivityName) )
			{
				PersonalLoanS.mLogger.info("Repeater Row Count to add row ov"+ "add row ov ");
				for(int i=0;i<docName.size();i++ )
				{
					//repObj.addRow();                    

					documentName = docName.get(i).get(0);
					documentNameMandatory = docName.get(i).get(2);
					statusValue = docName.get(i).get(3);
					expiryDate = Convert_dateFormat(docName.get(i).get(1), "yyyy-mm-dd", "dd/mm/yyyy") ;//Arun (22/09/17)
					Remarks = docName.get(i).get(4);//Arun (22/09/17)
					DocInd = docName.get(i).get(5);//Arun (22/09/17)

					PersonalLoanS.mLogger.info("Column Added in Repeater Username"+" "+ Username);//Arun (22/09/17)
					PersonalLoanS.mLogger.info("Column Added in Repeater documentName"+" "+ documentName);
					PersonalLoanS.mLogger.info("Column Added in Repeater documentNameMandatory"+" "+ documentNameMandatory);
					PersonalLoanS.mLogger.info("Column Added in Repeater expiryDate"+" "+ expiryDate);//Arun (22/09/17)
					PersonalLoanS.mLogger.info("Column Added in Repeater Status"+" "+ statusValue);//Arun (22/09/17)
					PersonalLoanS.mLogger.info("Column Added in Repeater Remarks"+" "+ Remarks);//Arun (22/09/17)
					PersonalLoanS.mLogger.info("Column Added in Repeater DocInd"+" "+ DocInd);//Arun (22/09/17)

					repObj.setValue(i, 0, documentName);
					repObj.setValue(i, 2, documentNameMandatory);
					repObj.setValue(i,1,expiryDate);
					repObj.setValue(i,3,statusValue);
					repObj.setValue(i,4,Remarks);
					repObj.setValue(i,11,DocInd);
					repObj.setColumnDisabled(0, true);
					repObj.setColumnDisabled(2, true);
					repObj.setColumnDisabled(1, true);
					repObj.setColumnDisabled(3, true);
					repObj.setColumnDisabled(14, true);
					//below code modified by nikhil
					if("Received".equalsIgnoreCase(statusValue))
					{
						repObj.setEditable(i,"cmplx_DocName_OVRemarks" , true);
						repObj.setEditable(i,"cmplx_DocName_OVDec" , true);
						repObj.setEditable(i,"cmplx_DocName_Approvedby" , true);
					}
					else
					{
						repObj.setEditable(i,"cmplx_DocName_OVRemarks" , false);
						repObj.setEditable(i,"cmplx_DocName_OVDec" , false);
						repObj.setEditable(i,"cmplx_DocName_Approvedby" , false);
					}
					repRowCount = repObj.getRepeaterRowCount();

					PersonalLoanS.mLogger.info("Repeater Row Count "+ " " + repRowCount);
				}
				repObj.setColumnVisible(11, false);
				repObj.setColumnDisabled(0, true);

			}*#/
			repObj.setColumnVisible(12, false);
			repObj.setColumnVisible(13, false);
			repObj.setColumnVisible(14, false);


			//added by yash on 26/12/2017 or making documents non-editable
			if ("ToTeam".equalsIgnoreCase(sActivityName) )
			{
				PersonalLoanS.mLogger.info("Repeater Row Count to add row ov"+ "add row ov ");
				for(int i=0;i<docName.size();i++ )
				{
					//repObj.addRow();                    

					documentName = docName.get(i).get(0);
					PersonalLoanS.mLogger.info("Documents name are"+" "+ documentName);

					documentNameMandatory = docName.get(i).get(2);
					statusValue = docName.get(i).get(3);
					expiryDate = Convert_dateFormat(docName.get(i).get(1), "yyyy-mm-dd", "dd/mm/yyyy") ;//Arun (22/09/17)
					Remarks = docName.get(i).get(4);//Arun (22/09/17)
					DocInd = docName.get(i).get(5);//Arun (22/09/17)

					PersonalLoanS.mLogger.info("Column Added in Repeater Username"+" "+ Username);//Arun (22/09/17)
					PersonalLoanS.mLogger.info("Column Added in Repeater documentName"+" "+ documentName);
					PersonalLoanS.mLogger.info("Column Added in Repeater documentNameMandatory"+" "+ documentNameMandatory);
					PersonalLoanS.mLogger.info("Column Added in Repeater expiryDate"+" "+ expiryDate);//Arun (22/09/17)
					PersonalLoanS.mLogger.info("Column Added in Repeater Status"+" "+ statusValue);//Arun (22/09/17)
					PersonalLoanS.mLogger.info("Column Added in Repeater Remarks"+" "+ Remarks);//Arun (22/09/17)
					PersonalLoanS.mLogger.info("Column Added in Repeater DocInd"+" "+ DocInd);//Arun (22/09/17)

					repObj.setValue(i, 0, documentName);
					repObj.setValue(i, 2, documentNameMandatory);
					repObj.setValue(i,1,expiryDate);
					repObj.setValue(i,3,statusValue);
					repObj.setValue(i,4,Remarks);
					repObj.setValue(i,11,DocInd);
					repObj.setColumnDisabled(0, false);
					repObj.setColumnDisabled(2, false);
					repObj.setColumnDisabled(1, false);
					repObj.setColumnDisabled(3, false);
					//repObj.setColumnDisabled(14, false);
					if("LIABILITY_CERTIFICATE".contains(documentName)||("LIABILITY_CERTIFICATE4".contains(documentName))||"LIABILITY_CERTIFICATE1".contains(documentName)||"LIABILITY_CERTIFICATE2".contains(documentName)||"LIABILITY_CERTIFICATE3".contains(documentName)
							||"LIABILITY_CERTIFICATE5".contains(documentName)||"NO_LIABILITY_CERTIFICATE".contains(documentName)||"SALARY_TRANSFER_LETTER".contains(documentName))
					{


						repObj.setRowEditable(i, true);

					}
					else
					{
						repObj.setRowEditable(i, false);
					}
					repRowCount = repObj.getRepeaterRowCount();

					PersonalLoanS.mLogger.info("Repeater Row Count "+ " " + repRowCount);
				}
				repObj.setColumnVisible(11, false);
				repObj.setColumnDisabled(0, true);
				//repObj.setColumnVisible(12, false);
				//repObj.setColumnVisible(13, false);
				//repObj.setColumnVisible(14, false);

			}
			//added by yash on 21/12/2017
			else if ( "Dispatch".equalsIgnoreCase(sActivityName) || "Post_Disbursal".equalsIgnoreCase(sActivityName) || "Disbursal".equalsIgnoreCase(sActivityName) || "CC_Disbursal".equalsIgnoreCase(sActivityName) || "Collection_User".equalsIgnoreCase(sActivityName))
			{
				PersonalLoanS.mLogger.info("Repeater Row Count to add row ov"+ "add row ov ");
				for(int i=0;i<docName.size();i++ )
				{
					//repObj.addRow();                    

					documentName = docName.get(i).get(0);
					documentNameMandatory = docName.get(i).get(2);	          
					statusValue = docName.get(i).get(3);
					expiryDate = Convert_dateFormat(docName.get(i).get(1), "yyyy-mm-dd", "dd/mm/yyyy") ;
					Remarks = docName.get(i).get(4);
					DocInd = docName.get(i).get(5);

					PersonalLoanS.mLogger.info("Column Added in Repeater documentName"+" "+ documentName);
					PersonalLoanS.mLogger.info("Column Added in Repeater documentNameMandatory"+" "+ documentNameMandatory);
					PersonalLoanS.mLogger.info("Column Added in Repeater expiryDate"+" "+ expiryDate);
					PersonalLoanS.mLogger.info("Column Added in Repeater Status"+" "+ statusValue);
					PersonalLoanS.mLogger.info("Column Added in Repeater Remarks"+" "+ Remarks);
					PersonalLoanS.mLogger.info("Column Added in Repeater DocInd"+" "+ DocInd);

					repObj.setValue(i, 0, documentName);
					repObj.setValue(i, 2, documentNameMandatory);
					repObj.setValue(i,1,expiryDate);
					repObj.setValue(i,3,statusValue);
					repObj.setValue(i,4,Remarks);
					repObj.setValue(i,11,DocInd);
					repObj.setColumnDisabled(0, true);
					repObj.setColumnDisabled(2, true);

					repRowCount = repObj.getRepeaterRowCount();

					PersonalLoanS.mLogger.info("Repeater Row Count "+ " " + repRowCount);
				}
				repObj.setColumnVisible(11, false);
				repObj.setColumnDisabled(0, true);
				//repObj.setColumnVisible(12, false);
				//repObj.setColumnVisible(13, false);
				//repObj.setColumnVisible(14, false);
			}
			//below code added by nikhil for ddvt
			else if ("DDVT_maker".equalsIgnoreCase(sActivityName) || "DDVT_Checker".equalsIgnoreCase(sActivityName) )
			{
				PersonalLoanS.mLogger.info("Repeater Row Count to add row ov"+ "add row ov ");
				PersonalLoanS.mLogger.info("Repeater Headers list: "+repeaterHeaders);

				for(int i=0;i<docName.size();i++ )
				{
					//repObj.addRow();                    

					documentName = docName.get(i).get(0);
					PersonalLoanS.mLogger.info("Documents name are"+" "+ documentName);

					documentNameMandatory = docName.get(i).get(2);
					statusValue = docName.get(i).get(3);
					expiryDate = Convert_dateFormat(docName.get(i).get(1), "yyyy-mm-dd", "dd/mm/yyyy") ;//Arun (22/09/17)
					Remarks = docName.get(i).get(4);//Arun (22/09/17)
					DocInd = docName.get(i).get(5);//Arun (22/09/17)

					PersonalLoanS.mLogger.info("Column Added in Repeater Username"+" "+ Username);//Arun (22/09/17)
					PersonalLoanS.mLogger.info("Column Added in Repeater documentName"+" "+ documentName);
					PersonalLoanS.mLogger.info("Column Added in Repeater documentNameMandatory"+" "+ documentNameMandatory);
					PersonalLoanS.mLogger.info("Column Added in Repeater expiryDate"+" "+ expiryDate);//Arun (22/09/17)
					PersonalLoanS.mLogger.info("Column Added in Repeater Status"+" "+ statusValue);//Arun (22/09/17)
					PersonalLoanS.mLogger.info("Column Added in Repeater Remarks"+" "+ Remarks);//Arun (22/09/17)
					PersonalLoanS.mLogger.info("Column Added in Repeater DocInd"+" "+ DocInd);//Arun (22/09/17)

					repObj.setValue(i, 0, documentName);
					if(documentName.equalsIgnoreCase("signature") && formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("false"))
					{
						repObj.setValue(i, 2,"N");
					}
					else
					{						
						repObj.setValue(i, 2, documentNameMandatory);
					}
					repObj.setValue(i,1,expiryDate);
					repObj.setValue(i,3,statusValue);
					repObj.setValue(i,4,Remarks);
					repObj.setValue(i,11,DocInd);
					//repObj.setColumnDisabled(0, true);
					repObj.setColumnDisabled(2, false);
					repObj.setColumnDisabled(1, false);
					repObj.setColumnDisabled(3, false);
					//repObj.setColumnDisabled(14, false);
					repObj.setColumnDisabled(0, true);

					if("DDVT_maker".equalsIgnoreCase(sActivityName) || "Other1".equalsIgnoreCase(documentName) || ("Other2".equalsIgnoreCase(documentName)) || ("Other3".equalsIgnoreCase(documentName))
							|| ("Other4".equalsIgnoreCase(documentName)) || ("Other5".equalsIgnoreCase(documentName)))

					{
						repObj.setRowEditable(i, true);
						repObj.setEditable(i, "IncomingDoc_AddFromDMSButton", true);
						repObj.setEditable(i, "IncomingDoc_AddFromPCButton", true);
						repObj.setEditable(i, "IncomingDoc_ScanButton", true);
						repObj.setEditable(i, "IncomingDoc_ViewButton", true);
						repObj.setEditable(i, "IncomingDoc_PrintButton", true);
					}
					else if("DDVT_Checker".equalsIgnoreCase(sActivityName))
					{
						repObj.setRowEditable(i, false);
						repObj.setEditable(i, "IncomingDoc_AddFromDMSButton", false);
						repObj.setEditable(i, "IncomingDoc_AddFromPCButton", false);
						repObj.setEditable(i, "IncomingDoc_ScanButton", false);
						repObj.setEditable(i, "IncomingDoc_ViewButton", false);
						repObj.setEditable(i, "IncomingDoc_PrintButton", false);			
					}




					repRowCount = repObj.getRepeaterRowCount();

					PersonalLoanS.mLogger.info("Repeater Row Count "+ " " + repRowCount);
				}
				/*repObj.setColumnDisabled(0, true);
				repObj.setColumnDisabled(5, true);
				repObj.setColumnDisabled(6, true);
				repObj.setColumnDisabled(7, true);
				repObj.setColumnDisabled(8, true);
				repObj.setColumnDisabled(9, true);*#/
				repObj.setColumnVisible(11, false);
				repObj.setColumnVisible(12, false);
				repObj.setColumnDisabled(0, true);
				//repObj.setColumnVisible(12, false);
				//repObj.setColumnVisible(13, false);
				//repObj.setColumnVisible(14, false);
				//below cpde added y nikhil 29/1/18
				repObj.setColumnDisabled(2, true);

			}
			//added by nikhil for fcu 9/2/18

			if ("FPU".equalsIgnoreCase(sActivityName) || "FCU".equalsIgnoreCase(sActivityName)|| "CAD_Analyst2".equalsIgnoreCase(sActivityName) )
			{
				PersonalLoanS.mLogger.info("Repeater Row Count to add row ov"+ "add row ov ");
				for(int i=0;i<docName.size();i++ )
				{
					//repObj.addRow();                    

					documentName = docName.get(i).get(0);
					PersonalLoanS.mLogger.info("Documents name are"+" "+ documentName);

					documentNameMandatory = docName.get(i).get(2);
					statusValue = docName.get(i).get(3);
					expiryDate = Convert_dateFormat(docName.get(i).get(1), "yyyy-mm-dd", "dd/mm/yyyy") ;//Arun (22/09/17)
					Remarks = docName.get(i).get(4);//Arun (22/09/17)
					DocInd = docName.get(i).get(5);//Arun (22/09/17)

					PersonalLoanS.mLogger.info("Column Added in Repeater Username"+" "+ Username);//Arun (22/09/17)
					PersonalLoanS.mLogger.info("Column Added in Repeater documentName"+" "+ documentName);
					PersonalLoanS.mLogger.info("Column Added in Repeater documentNameMandatory"+" "+ documentNameMandatory);
					PersonalLoanS.mLogger.info("Column Added in Repeater expiryDate"+" "+ expiryDate);//Arun (22/09/17)
					PersonalLoanS.mLogger.info("Column Added in Repeater Status"+" "+ statusValue);//Arun (22/09/17)
					PersonalLoanS.mLogger.info("Column Added in Repeater Remarks"+" "+ Remarks);//Arun (22/09/17)
					PersonalLoanS.mLogger.info("Column Added in Repeater DocInd"+" "+ DocInd);//Arun (22/09/17)

					repObj.setValue(i, 0, documentName);
					repObj.setValue(i, 2, documentNameMandatory);
					repObj.setValue(i,1,expiryDate);
					repObj.setValue(i,3,statusValue);
					repObj.setValue(i,4,Remarks);
					repObj.setRowEditable(i, true);
					repRowCount = repObj.getRepeaterRowCount();

					PersonalLoanS.mLogger.info("Repeater Row Count "+ " " + repRowCount);
				}

				repObj.setColumnVisible(11, false);
				repObj.setColumnVisible(12, true);
				repObj.setColumnDisabled(12, true);
				repObj.setColumnEditable(12, false);
				repObj.setColumnDisabled(2, true);
				repObj.setColumnDisabled(1, true);
				repObj.setColumnEditable(1, false);
				repObj.setColumnDisabled(3, true);
				repObj.setColumnDisabled(4, true);
				repObj.setColumnDisabled(0, true);


			}
			else if ("CAD_Analyst1".equalsIgnoreCase(sActivityName)   )
			{
				PersonalLoanS.mLogger.info("Repeater Row Count to add row ov"+ "add row ov ");
				for(int i=0;i<docName.size();i++ )
				{
					//repObj.addRow();                    

					documentName = docName.get(i).get(0);
					PersonalLoanS.mLogger.info("Documents name are"+" "+ documentName);

					documentNameMandatory = docName.get(i).get(2);
					statusValue = docName.get(i).get(3);
					expiryDate = Convert_dateFormat(docName.get(i).get(1), "yyyy-mm-dd", "dd/mm/yyyy") ;//Arun (22/09/17)
					Remarks = docName.get(i).get(4);//Arun (22/09/17)
					DocInd = docName.get(i).get(5);//Arun (22/09/17)

					PersonalLoanS.mLogger.info("Column Added in Repeater Username"+" "+ Username);//Arun (22/09/17)
					PersonalLoanS.mLogger.info("Column Added in Repeater documentName"+" "+ documentName);
					PersonalLoanS.mLogger.info("Column Added in Repeater documentNameMandatory"+" "+ documentNameMandatory);
					PersonalLoanS.mLogger.info("Column Added in Repeater expiryDate"+" "+ expiryDate);//Arun (22/09/17)
					PersonalLoanS.mLogger.info("Column Added in Repeater Status"+" "+ statusValue);//Arun (22/09/17)
					PersonalLoanS.mLogger.info("Column Added in Repeater Remarks"+" "+ Remarks);//Arun (22/09/17)
					PersonalLoanS.mLogger.info("Column Added in Repeater DocInd"+" "+ DocInd);//Arun (22/09/17)

					repObj.setValue(i, 0, documentName);
					repObj.setValue(i, 2, documentNameMandatory);
					repObj.setValue(i,1,expiryDate);
					repObj.setValue(i,3,statusValue);
					repObj.setValue(i,4,Remarks);
					repObj.setValue(i,11,DocInd);
					//repObj.setColumnDisabled(0, true);
					repObj.setColumnDisabled(2, true);
					repObj.setColumnDisabled(1, false);
					repObj.setColumnDisabled(3, false);
					//repObj.setColumnDisabled(14, false);
					repObj.setColumnDisabled(0, true);
					/*repObj.setColumnDisabled(5, true);
					repObj.setColumnDisabled(6, true);
					repObj.setColumnDisabled(7, true);
					repObj.setColumnDisabled(8, true);
					repObj.setColumnDisabled(9, true);*#/

					repObj.setRowEditable(i, true);


					repRowCount = repObj.getRepeaterRowCount();

					PersonalLoanS.mLogger.info("Repeater Row Count "+ " " + repRowCount);
				}
				/*repObj.setColumnDisabled(0, true);
				repObj.setColumnDisabled(5, true);
				repObj.setColumnDisabled(6, true);
				repObj.setColumnDisabled(7, true);
				repObj.setColumnDisabled(8, true);
				repObj.setColumnDisabled(9, true);*#/
				repObj.setColumnDisabled(0, true);
				repObj.setColumnVisible(11, false);
				repObj.setColumnVisible(12, true);
				//repObj.setColumnVisible(13, false);
				//repObj.setColumnVisible(14, false);

			}
			//added by yash on 21/12/2017
			else if ( "Dispatch".equalsIgnoreCase(sActivityName) || "Post_Disbursal".equalsIgnoreCase(sActivityName) || "Disbursal".equalsIgnoreCase(sActivityName) || "CC_Disbursal".equalsIgnoreCase(sActivityName) || "Collection_User".equalsIgnoreCase(sActivityName))
			{
				PersonalLoanS.mLogger.info("Repeater Row Count to add row ov"+ "add row ov ");
				for(int i=0;i<docName.size();i++ )
				{
					//repObj.addRow();                    

					documentName = docName.get(i).get(0);
					documentNameMandatory = docName.get(i).get(2);	          
					statusValue = docName.get(i).get(3);
					expiryDate = Convert_dateFormat(docName.get(i).get(1), "yyyy-mm-dd", "dd/mm/yyyy") ;
					Remarks = docName.get(i).get(4);
					DocInd = docName.get(i).get(5);

					PersonalLoanS.mLogger.info("Column Added in Repeater documentName"+" "+ documentName);
					PersonalLoanS.mLogger.info("Column Added in Repeater documentNameMandatory"+" "+ documentNameMandatory);
					PersonalLoanS.mLogger.info("Column Added in Repeater expiryDate"+" "+ expiryDate);
					PersonalLoanS.mLogger.info("Column Added in Repeater Status"+" "+ statusValue);
					PersonalLoanS.mLogger.info("Column Added in Repeater Remarks"+" "+ Remarks);
					PersonalLoanS.mLogger.info("Column Added in Repeater DocInd"+" "+ DocInd);

					repObj.setValue(i, 0, documentName);
					repObj.setValue(i, 2, documentNameMandatory);
					repObj.setValue(i,1,expiryDate);
					repObj.setValue(i,3,statusValue);
					repObj.setValue(i,4,Remarks);
					repObj.setValue(i,11,DocInd);
					repObj.setColumnDisabled(0, true);
					repObj.setColumnDisabled(2, true);

					repRowCount = repObj.getRepeaterRowCount();

					PersonalLoanS.mLogger.info("Repeater Row Count "+ " " + repRowCount);
				}
				repObj.setColumnVisible(11, false);
				repObj.setColumnDisabled(0, true);
				//repObj.setColumnVisible(12, false);
				//repObj.setColumnVisible(13, false);
				//repObj.setColumnVisible(14, false);
			}
			else
			{
				PersonalLoanS.mLogger.info("Repeater Row Count to add row maker csm"+ "add row CSm maker ");


				for(int i=0;i<docName.size();i++ )
				{
					//repObj.addRow();

					//repObj.setColumnVisible(12, false);
					//repObj.setColumnVisible(13, false);
					//repObj.setColumnVisible(14, false);

					documentName = docName.get(i).get(0);
					documentNameMandatory = docName.get(i).get(2);
					expiryDate = Convert_dateFormat(docName.get(i).get(1), "yyyy-mm-dd", "dd/mm/yyyy") ;

					statusValue = docName.get(i).get(3);
					Remarks = docName.get(i).get(4);
					DocInd = docName.get(i).get(5);
					PersonalLoanS.mLogger.info("Column Added in Repeater documentName"+" "+ documentName);
					PersonalLoanS.mLogger.info("Column Added in Repeater documentNameMandatory"+" "+ documentNameMandatory);
					PersonalLoanS.mLogger.info("Column Added in Repeater expiryDate"+" "+ expiryDate);
					PersonalLoanS.mLogger.info("Column Added in Repeater Status"+" "+ statusValue);
					PersonalLoanS.mLogger.info("Column Added in Repeater Remarks"+" "+ Remarks);
					PersonalLoanS.mLogger.info("Column Added in Repeater DocInd"+" "+ DocInd);

					repObj.setValue(i, 0, documentName);
					repObj.setValue(i, 2, documentNameMandatory); 	     
					repObj.setValue(i,1,expiryDate);
					/*repObj.setValue(i,3,statusValue);
					repObj.setValue(i,4,Remarks);*#/
					repObj.setValue(i,11,DocInd);
					repObj.setColumnDisabled(0, true);
					repObj.setColumnDisabled(2, true);
					repObj.setDisabled(i, "cmplx_DocName_DocName", true);
					formObject.setLocked("cmplx_DocName_ExpiryDate",true);
					formObject.setLocked("cmplx_DocName_Deferred_Until",true);



					if(statusValue == null){
						statusValue = "--Select--"; 
					}
					repObj.setValue(i, 3, statusValue);
					repObj.setValue(i, 4, Remarks);
					/*List<String> man=new ArrayList<String>(Arrays.asList("--Select--", "Pending", "Received", "Deferred", "Waived"));
					List<String> nonman=new ArrayList<String>(Arrays.asList("--Select--", "Pending", "Received")); 
					if(documentNameMandatory.equalsIgnoreCase("Y"))
					{
						formObject.addItem("cmplx_DocName_Doc_Status", man);
					}
					else
					{
						formObject.addItem("cmplx_DocName_Doc_Status", nonman);
					}*/
					/*repRowCount = repObj.getRepeaterRowCount();

					PersonalLoanS.mLogger.info("Repeater Row Count "+ " " + repRowCount);*#/

				} 
				repObj.setColumnVisible(11, false);
				repObj.setColumnDisabled(0, true);
				repObj.setColumnVisible(12,true);
				repObj.setColumnDisabled(12, true);
				repObj.setColumnEditable(12,false);
				repObj.setColumnDisabled(2, true);
				repObj.setColumnEditable(2,false);

				if("CPV".equalsIgnoreCase(sActivityName) ){
					repObj.setColumnDisabled(1, true);
					repObj.setColumnEditable(1,false);
					repObj.setColumnDisabled(3, true);
					repObj.setColumnEditable(3,false);
					repObj.setColumnDisabled(4, true);

				}

				if("CPV_Analyst".equalsIgnoreCase(sActivityName)){
					repObj.setColumnDisabled(1, true);

					repObj.setColumnDisabled(3, true);
					repObj.setColumnDisabled(4, true);

				}
				//repObj.setColumnVisible(12, false);
				//repObj.setColumnVisible(13, false);
				//repObj.setColumnVisible(14, false);
				if("Original_Validation".equalsIgnoreCase(sActivityName))
				{
					repObj.setColumnDisabled(1, true);
					repObj.setColumnEditable(1, false);
					repObj.setColumnDisabled(2, true);
					repObj.setColumnEditable(2, false);
					repObj.setColumnDisabled(4,true);
					repObj.setColumnDisabled(3, true);
					repObj.setColumnEditable(3, false);

				}
			}
		}
		catch (Exception e) {

			PersonalLoanS.mLogger.info("EXCEPTION    :    "+ " " + e.toString());
			printException(e);

		} 
	}*/

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Populate Existing Data in Decision Grid

	 ***********************************************************************************  */

	public void loadInDecGrid()
	{
		PersonalLoanS.mLogger.info("Inside loadInDecGrid() method to load WI history");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		try{
			String ParentWI_Name = formObject.getNGValue("parent_WIName");
			formObject.clear(DECISION_LISTVIEW1);
			String query="";
			if(formObject.getLVWRowCount(DECISION_LISTVIEW1)==0){
				if(NGFUserResourceMgr_PL.getGlobalVar("FCU_remarks_View").contains(formObject.getWFActivityName())  && !"R".equalsIgnoreCase(formObject.getFormConfig().getConfigElement("Mode"))){
					query="select FORMAT(datelastchanged,'dd/MM/yyyy HH:mm'),userName,workstepName,isnull(Decision,'NA'),isnull(remarks,'NA'),dec_wi_name,isnull(referto,''),FORMAT(entry_date,'dd/MM/yyyy HH:mm'),decisionreasoncode from ng_rlos_gr_Decision with (nolock) where (dec_wi_name='"+ParentWI_Name+"' or dec_wi_name='"+formObject.getWFWorkitemName()+"') and workstepName is not null order by datelastchanged asc";
				}else{
					String user=formObject.getUserName();
					user=user.toLowerCase();
					if(user.contains("fpu"))
					{
					query="select FORMAT(datelastchanged,'dd/MM/yyyy HH:mm'),userName,workstepName,isnull(Decision,'NA'),isnull(remarks,'NA'),dec_wi_name,referto,FORMAT(entry_date,'dd/MM/yyyy HH:mm'),decisionreasoncode from ng_rlos_gr_Decision with (nolock) where (dec_wi_name='"+ParentWI_Name+"' or dec_wi_name='"+formObject.getWFWorkitemName()+"') and workstepName is not null order by datelastchanged asc";
				
					PersonalLoanS.mLogger.info("Isss"+query);
					}
					else
					{
						query="select FORMAT(datelastchanged,'dd/MM/yyyy HH:mm'),userName,workstepName,isnull(Decision,'NA'),case when workstepName in ('FCU','FPU') then 'XXXXXXXXXX' else isnull(remarks,'NA') end,dec_wi_name,isNull(referto,''),FORMAT(entry_date,'dd/MM/yyyy HH:mm'),decisionreasoncode from ng_rlos_gr_Decision with (nolock) where (dec_wi_name='"+ParentWI_Name+"' or dec_wi_name='"+formObject.getWFWorkitemName()+"') and workstepName is not null order by datelastchanged asc";
						PersonalLoanS.mLogger.info("Isss1"+query);
					}
				}
			PersonalLoanS.mLogger.info("Inside PLCommon ->loadInDecGrid()"+query);
			List<List<String>> list=formObject.getDataFromDataSource(query);
			PersonalLoanS.mLogger.info("Inside PLCommon ->loadInDecGrid()"+list);
			UIComponent pComp = formObject.getComponent(DECISION_LISTVIEW1);
			ListView objListView = ( ListView )pComp;
			//List<String> myList = new ArrayList<String>();
			int columns = objListView.getChildCount();
			PersonalLoanS.mLogger.info("Inside CCCommon ->columns"+columns);
			for (List<String> a : list) 
			{
				//List<String> mylist=new ArrayList<String>();
				
	                 int temp = a.size();
	             if(columns>temp){
	            	 PersonalLoanS.mLogger.info("Inside CCCommon ->loadInDecGrid() temp value"+temp);
	            	 while(columns>temp){
	            		 PersonalLoanS.mLogger.info(" in loaddEcGrid column name"+((Column)(pComp.getChildren().get(temp))).getName());
	            		 if((((Column)(pComp.getChildren().get(temp))).getName()).equals("ExistingRowFlag")){
	            			 a.add("Y"); 
	            		 }
	            		 else{
	            		 a.add("");
	            		 }
	            		 temp++;
	            	 }
	             }
	             PersonalLoanS.mLogger.info("Inside CCCommon ->loadInDecGrid() loop"+a);
			formObject.addItemFromList(DECISION_LISTVIEW1, a);
			}
		}
		}catch(Exception e){ 
			PersonalLoanS.mLogger.info("PLCommon"+"Exception Occurred loadInDecGrid :"+e.getMessage());
			printException(e);
		}     
	}



	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to load data in Refer grid

	 ***********************************************************************************  */


	/*public void loadInReferGrid()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		String query="select FORMAT(datelastchanged,'dd/MM/yyyy hh:mm'),userName,workstepName,Decisiom,remarks,TargetDecision,workitemID,history_wi_nAme from ng_rlos_gr_ReferHistory with (nolock) where  history_wi_nAme='"+formObject.getWFWorkitemName()+"'";
		PersonalLoanS.mLogger.info("PersonnalLoanS> "+"Inside PLCommon ->loadInReferGrid()"+query);
		List<List<String>> list=formObject.getNGDataFromDataCache(query);
		PersonalLoanS.mLogger.info("PersonnalLoanS> "+"Inside PLCommon ->Query Result is: "+list.toString());
		for (List<String> a : list) 
		{

			formObject.addItemFromList("cmplx_ReferHistory_cmplx_GR_ReferHistory", a);
		}

	}*/

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to loadPicklist for Employment Details    

	 ***********************************************************************************  */

	public void loadPicklist4()    
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PersonalLoanS.mLogger.info("RLOSCommon"+ "Inside loadpicklist4:"); 
		String reqProd = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
		String subprod = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2);
		String appCategory = formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg");
		LoadPickList("cmplx_EmploymentDetails_ApplicationCateg", "select '--Select--' as Description,'' as code union select  convert(varchar,description),code from NG_MASTER_ApplicatonCategory with (nolock) order by code");

		if("Personal Loan".equalsIgnoreCase(reqProd)){
			PersonalLoanS.mLogger.info("App cat1:"+appCategory);
			if("BAU".equalsIgnoreCase(appCategory)){
				LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subprod+"' and BAU='Y' and (product = 'PL' or product='B') order by code");
			}
			else if("S".equalsIgnoreCase(appCategory)){
				LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subprod+"' and Surrogate='Y' and (product = 'PL' or product='B') order by code");
			}
			else{
				LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subprod+"' and (product = 'PL' or product='B') order by code");	
			}
		}
		PersonalLoanS.mLogger.info("outside if:");
		if("Personal Loan".equalsIgnoreCase(reqProd)){
			PersonalLoanS.mLogger.info("App cat2:"+appCategory);
			if("BAU".equalsIgnoreCase(appCategory)){
				PersonalLoanS.mLogger.info("Inside if");
				LoadPickList("cmplx_EmploymentDetails_IndusSeg","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_IndustrySegment with (nolock)  where isActive='Y' and code in('H3','H4')  order by code");
			}
			else{
				PersonalLoanS.mLogger.info("else");
				LoadPickList("cmplx_EmploymentDetails_IndusSeg", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_IndustrySegment with (nolock)   where isActive='Y'  order by code");
			}
				
		}
		//LoadPickList("cmplx_EmploymentDetails_DesigVisa", "select '--Select--' as description,'' as code union select  convert(varchar, description),code from NG_MASTER_Designation with (nolock)  where isActive='Y' order by code"); commented by saurabh1
		LoadPickList("cmplx_EmploymentDetails_Emp_Type", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_EmploymentType with (nolock)  where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_EmpStatus", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_EmploymentStatus with (nolock)  where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_EmirateOfWork", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_emirate with (nolock)  where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_HeadOfficeEmirate", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_emirate with (nolock)  where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_tenancycntrctemirate", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_emirate with (nolock) order by code");
		LoadPickList("cmplx_EmploymentDetails_EmpIndusSector", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_IndustrySector with (nolock)  where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_EmpContractType", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_EmpContractType with (nolock)  where isActive='Y' order by code");
		
		LoadPickList("cmplx_EmploymentDetails_channelcode","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_ChannelCode with (nolock)  where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_EmpStatusPL","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_EmployerStatusPL with (nolock)  where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_EmpStatusCC","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_EmployerStatusCC with (nolock)  where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_Emp_Categ_PL","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_EmployerCategory_PL with (nolock)  where isActive='Y'  order by code");
		//LoadPickList("cmplx_EmploymentDetails_FreezoneName","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_freezoneName with (nolock)  where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_Status_PLExpact","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_EmployerStatusPL where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_Status_PLNational","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_EmployerStatusPL where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_categexpat","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_EmployerStatusPL with (nolock)  where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_categnational","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_EmployerStatusPL with (nolock)  where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_OtherBankCAC","select '--Select--' union select convert(varchar, Description) from NG_MASTER_othBankCAC where isActive='Y'");
		LoadPickList("cmplx_EmploymentDetails_Indus_Macro", " Select macro from (select '--Select--' as macro union select macro from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where  IsActive='Y') new_table order by case  when macro ='--Select--' then 0 else 1 end");
		LoadPickList("cmplx_EmploymentDetails_Indus_Micro", "Select micro from (select '--Select--' as micro union select micro from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where  IsActive='Y') new_table order by case  when micro ='--Select--' then 0 else 1 end");
		LoadPickList("cmplx_EmploymentDetails_EmpContractType","select '--Select--' union select convert(varchar, Description) from NG_MASTER_EmpContractType where isActive='Y'");
		LoadPickList("cmplx_EmploymentDetails_PromotionCode","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_ReferrorCode where isActive='Y' and product='PL'  order by code");//pcasi-998 by shweta
		LoadPickList("cmplx_EmploymentDetails_collectioncode","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_CollectionCode where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_CurrEmployer","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_EmployerCategory_PL where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_employer_type","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_EmployerType where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_empmovemnt", "select '--Select--' union all select convert(varchar, Description) from NG_MASTER_EMPLOYERMOVEMENT with (nolock)");
		//LoadPickList("cmplx_EmploymentDetails_Field_visit_done", "select '--Select--' union all select convert(varchar, Description) from ng_master_fieldvisitDone with (nolock)");
		
		LoadPickList("cmplx_EmploymentDetails_marketcode","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_MarketingCode with (nolock)  where isActive='Y' and subproduct ='PL' order by code");//table changed to NG_MASTER_ClassificationCode to NG_MASTER_MarketingCode by bandana //Changed for PCASI-2878
		LoadPickList("cmplx_EmploymentDetails_Field_visit_done","select '--Select--' as Description,'' as code union select convert(varchar, Description),code from ng_master_fieldVisitdone with (nolock) where isActive='Y' order by code");
		
		//added by aastha for jira PCSP-207
		LoadPickList("cmplx_EmploymentDetails_NepType","select '--Select--' as Description,'' as code union select convert(varchar, Description),code from NG_MASTER_NEPType with (nolock) where isActive='Y' order by code");
		
		//end change
		//picklist classification code loaded from master bandana
		LoadPickList("cmplx_EmploymentDetails_ClassificationCode","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_ClassificationCode with (nolock)  where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_TL_Emirate", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_emirate with (nolock)  where isActive='Y' order by code");
	}
	//
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Hide/Unhide fields for ELigibiltyAndProductInfo    

	 ***********************************************************************************  */

	public void Eligibilityfields()
	{

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PersonalLoanS.mLogger.info("RLOS_Common"+ "Inside loadPicklist_Address: "); 



		//PersonalLoanS.mLogger.info("CC"+ "Grid Data[1][6] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid"+0+1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid"+0+6));
		if((NGFUserResourceMgr_PL.getGlobalVar("PL_Salaried").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5)) || NGFUserResourceMgr_PL.getGlobalVar("PL_SalariedPensioner").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5)) ) && !NGFUserResourceMgr_PL.getGlobalVar("PL_IM").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2))){
			formObject.setVisible("ELigibiltyAndProductInfo_Label8", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_InterestRate", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label7", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_EMI", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label11", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_RepayFreq", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label13", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_MaturityDate", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label15", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_BaseRateType",false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label12", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_FirstRepayDate", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label14", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_InterestType", false);
			/*formObject.setVisible("ELigibiltyAndProductInfo_Label1", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_EFCHidden", false);*/
			//formObject.setLocked("cmplx_EligibilityAndProductInfo_EFCHidden", true);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_FinalInterestRate", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label23", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_NetRate", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label8", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_NetPayout", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label10", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_takeoverBank", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label9", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_TakeoverAMount", false);


		}
		String appType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,4);

		if(formObject.getNGValue("Application_Type").contains("TOP")){
			PersonalLoanS.mLogger.info("inside top condition");
			formObject.setVisible("ELigibiltyAndProductInfo_Label8", true);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_NetPayout", true);
			formObject.setLeft("ELigibiltyAndProductInfo_Label8", formObject.getLeft("ELigibiltyAndProductInfo_Label4")+260);
			formObject.setTop("ELigibiltyAndProductInfo_Label8", formObject.getTop("ELigibiltyAndProductInfo_Label39"));
			formObject.setLeft("cmplx_EligibilityAndProductInfo_NetPayout", formObject.getLeft("ELigibiltyAndProductInfo_Label4")+260);
			formObject.setTop("cmplx_EligibilityAndProductInfo_NetPayout", formObject.getTop("cmplx_EligibilityAndProductInfo_InstrumentType"));
		}
		else if(formObject.getNGValue("Application_Type").contains("TKO")){
			formObject.setVisible("ELigibiltyAndProductInfo_Label10", true);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_takeoverBank", true);
			formObject.setVisible("ELigibiltyAndProductInfo_Label9", true);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_TakeoverAMount", true);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_TakeoverAMount", true);	
			formObject.setLocked("cmplx_EligibilityAndProductInfo_takeoverBank", true);
			

			formObject.setLeft("ELigibiltyAndProductInfo_Label10", formObject.getLeft("ELigibiltyAndProductInfo_Label5"));
			formObject.setTop("ELigibiltyAndProductInfo_Label10", formObject.getTop("ELigibiltyAndProductInfo_Label39"));
			formObject.setLeft("cmplx_EligibilityAndProductInfo_takeoverBank", formObject.getLeft("ELigibiltyAndProductInfo_Label10"));
			formObject.setTop("cmplx_EligibilityAndProductInfo_takeoverBank", formObject.getTop("cmplx_EligibilityAndProductInfo_InstrumentType"));
			formObject.setLeft("ELigibiltyAndProductInfo_Label9", formObject.getLeft("ELigibiltyAndProductInfo_Label6"));
			formObject.setTop("ELigibiltyAndProductInfo_Label9", formObject.getTop("ELigibiltyAndProductInfo_Label39"));
			formObject.setLeft("cmplx_EligibilityAndProductInfo_TakeoverAMount", formObject.getLeft("ELigibiltyAndProductInfo_Label9"));
			formObject.setTop("cmplx_EligibilityAndProductInfo_TakeoverAMount", formObject.getTop("cmplx_EligibilityAndProductInfo_InstrumentType"));
		
		}
		else{
			PersonalLoanS.mLogger.info("inside else top condition");
			formObject.setVisible("ELigibiltyAndProductInfo_Label8", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_NetPayout", false);
		}
		if("CAD_Analyst1".equalsIgnoreCase(formObject.getWFActivityName()) || "CAD_Analyst2".equalsIgnoreCase(formObject.getWFActivityName()) ){
			//by shweta
			int framestate1=formObject.getNGFrameState("MOL");
			if(framestate1!= 0){//PCASI-1064
				formObject.fetchFragment("MOL", "MOL1", "q_MOL");
				formObject.setNGFrameState("MOL", 0);
				adjustFrameTops("MOL,World_Check,Reject_Enq,Sal_Enq,Credit_card_Enq1,LOS1,Case_History1");	
				

			}
			formObject.setEnabled("cmplx_EligibilityAndProductInfo_InterestRate", false);
			formObject.setLocked("cmplx_EligibilityAndProductInfo_InterestRate", true);
			formObject.setEnabled("cmplx_EligibilityAndProductInfo_InterestRate", false);

			formObject.setLocked("cmplx_EligibilityAndProductInfo_EMI", true);
			if(appType.contains("TKO")){
			
				if(!"CAD_Analyst2".equalsIgnoreCase(formObject.getWFActivityName())){
					//formObject.setEnabled("cmplx_EligibilityAndProductInfo_takeoverBank", true);
					formObject.setEnabled("cmplx_EligibilityAndProductInfo_TakeoverAMount", true);
					formObject.setLocked("cmplx_EligibilityAndProductInfo_TakeoverAMount", false);
					if("CAD_Analyst1".equalsIgnoreCase(formObject.getWFActivityName())){
						formObject.setLocked("cmplx_EligibilityAndProductInfo_takeoverBank", false);
						formObject.setEnabled("cmplx_EligibilityAndProductInfo_takeoverBank", true);
					}
				} else {
					formObject.setLocked("cmplx_EligibilityAndProductInfo_TakeoverAMount", true);
					formObject.setLocked("cmplx_EligibilityAndProductInfo_takeoverBank", true);//PCASI-1089,1090
				}

			}
		}
		

		if("CSM".equalsIgnoreCase(formObject.getWFActivityName())){
			setLoanFieldsVisible();
			if(appType.contains("TKO")){				
				//formObject.setEnabled("cmplx_EligibilityAndProductInfo_takeoverBank", true);//pcasi-1089,1090
				formObject.setEnabled("cmplx_EligibilityAndProductInfo_TakeoverAMount", true);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_TakeoverAMount", false);
				formObject.setLocked("cmplx_EligibilityAndProductInfo_takeoverBank", true);
			}

		}
		PersonalLoanS.mLogger.info("Shweta - cmplx_EligibilityAndProductInfo_takeoverBank value 3" + formObject.getNGValue("cmplx_EligibilityAndProductInfo_takeoverBank"));


	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Save Data in Decision Grid   

	 ***********************************************************************************  */


	public void saveIndecisionGrid(){

		PersonalLoanS.mLogger.info("PL_Common"+ "Inside saveIndecisionGrid: "); 
		Common_Utils common=new Common_Utils(PersonalLoanS.mLogger);
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String query="";
		@SuppressWarnings("unused")
		String entrydate = "";
		String Cad_level="";
		//String entrydate = formObject.getNGValue("Entry_date_time"); //Tarang to be removed on friday(1/19/2018)
		try{
		//String[] parts = EntrydateTime.split("/"); 
		//entrydate =common.Convert_dateFormat(entrydate, "dd/MM/yyyy","MM/dd/yyyy");
		//PersonalLoanS.mLogger.info("PL_Common"+ "Inside saveIndecisionGrid:EntrydateTime "+entrydate); 
		String sQuery1 = "Select distinct cadNext FROM NG_PL_EXTTABLE  with (nolock) WHERE PL_wi_name='"+formObject.getWFWorkitemName()+"'";
		PersonalLoanS.mLogger.info( "box value:"+sQuery1); 
		List<List<String>> csmNext_Result = formObject.getNGDataFromDataCache(sQuery1);
		if(csmNext_Result==null && !csmNext_Result.isEmpty())
		{
		formObject.setNGValue("cmplx_Decision_csmNext",csmNext_Result.get(0).get(0));
		}
		String currDate=common.Convert_dateFormat("", "","MM/dd/yyyy HH:mm");
		PersonalLoanS.mLogger.info("PL_Common"+ "Inside saveIndecisionGrid...currDate "+currDate);
		String CIF_id=formObject.getNGValue("cmplx_Customer_CIFNO");
		String Emirate_id=formObject.getNGValue("cmplx_Customer_EmiratesID");
		PersonalLoanS.mLogger.info("PL_Common"+ "Inside  saveIndecisionGrid entrydate @@nikhil"+formObject.getWFGeneralData());
		//below code added by nikhil
		String s=formObject.getWFGeneralData();
		int startPosition = s.indexOf("<EntryDateTime>") + "<EntryDateTime>".length();
		int endPosition = s.indexOf("</EntryDateTime>", startPosition);
		String subS = s.substring(startPosition, endPosition);
		String decisionreason="";
		String decision="";
		String remarks="";
		String ReferTo="";
		
		
		//boolean NewRowFlag=false;
		for(int i=0;i<formObject.getLVWRowCount("Decision_ListView1");i++){
			if(formObject.getNGValue("Decision_ListView1", i, 12).equals("")){
				//NewRowFlag=true;
				entrydate = formObject.getNGValue("Decision_ListView1",i,7);
				PersonalLoanS.mLogger.info("PL_Common"+ "Inside saveIndecisionGrid:EntrydateTime "+entrydate); 
				currDate=common.Convert_dateFormat(formObject.getNGValue("Decision_ListView1",i,0), "dd/MM/yyyy HH:mm","MM/dd/yyyy HH:mm");
				PersonalLoanS.mLogger.info("PL_Common"+ "Inside saveIndecisionGrid...currDate "+currDate);
				decision=formObject.getNGValue("Decision_ListView1",i,3);
				decisionreason=formObject.getNGValue("Decision_ListView1",i,8);
				remarks=formObject.getNGValue("Decision_ListView1",i,4);
				ReferTo=formObject.getNGValue("Decision_ListView1",i,6);
				query="insert into ng_rlos_gr_Decision(dateLastChanged, userName, workstepName, Decision, remarks, dec_wi_name,Entry_Date,DecisionReasonCode,CIF_ID,EmirateID,csmNext,referTo) values('"+currDate+"','"+formObject.getUserName()+"','"+formObject.getWFActivityName()+"','"+decision+"','"+remarks+"','"+formObject.getWFWorkitemName()+"',convert(varchar(30),cast('"+subS+"' as datetime),121),'"+decisionreason+"','"+CIF_id+"','"+Emirate_id+"','"+formObject.getNGValue("CADNext")+"','"+ReferTo+"')";
				if("FCU".equals(formObject.getWFActivityName()) || "FPU".equals(formObject.getWFActivityName()))
				{
					query="insert into ng_rlos_gr_Decision(dateLastChanged, userName, workstepName, Decision, remarks, dec_wi_name,Entry_Date,DecisionReasonCode,CIF_ID,EmirateID,csmNext,referTo,CustomerNAme) values('"+currDate+"','"+formObject.getUserName()+"','"+formObject.getWFActivityName()+"','"+decision+"','"+remarks+"','"+formObject.getWFWorkitemName()+"',convert(varchar(30),cast('"+subS+"' as datetime),121),'"+decisionreason+"','"+CIF_id+"','"+Emirate_id+"','"+formObject.getNGValue("cmplx_Decision_csmNext")+"','"+ReferTo+"','"+formObject.getNGValue("CustomerLabel")+"')";
				}
				PersonalLoanS.mLogger.info("PL_Common"+"Query is"+query);
				formObject.saveDataIntoDataSource(query);
			}
		}
		}catch(Exception e){
			printException(e);
		}
		
	}	

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Save Data in Decision grid for CSM

	 ***********************************************************************************  */

	public void saveIndecisionGridCSM(){
		try
		{
		PersonalLoanS.mLogger.info("PL_Common"+ "Inside saveIndecisionGrid: "); 
		//String entrydate="" ; 
		Common_Utils common=new Common_Utils(PersonalLoanS.mLogger);
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String Cifid=formObject.getNGValue("DecisionHistory_CIFid");
		String emiratesid=formObject.getNGValue("DecisionHistory_Emiratesid");
		String custName=formObject.getNGValue("DecisionHistory_Customer_Name");
		PersonalLoanS.mLogger.info("PersonnalLoanS"+"Final val of custName:"+ custName);
		//String EntrydateTime = formObject.getNGValue("Entry_date_time");//Tarang to be removed on friday(1/19/2018)
		String entrydate = formObject.getNGValue("q_EntryDate");
		//String[] parts = EntrydateTime.split("/"); 
		entrydate =common.Convert_dateFormat(entrydate, "dd/MM/yyyy","MM/dd/yyyy");
		PersonalLoanS.mLogger.info("PL_Common"+ "Inside saveIndecisionGrid:EntrydateTime "+entrydate); 
		PersonalLoanS.mLogger.info("PL_Common"+ "Inside saveIndecisionGrid:EntrydateTime "+entrydate); 
		PersonalLoanS.mLogger.info("PL_Common"+ "Inside  saveIndecisionGrid entrydate "+entrydate); 
		String currDate=new SimpleDateFormat("MM/dd/yyyy HH:mm").format(Calendar.getInstance().getTime());
		PersonalLoanS.mLogger.info("PL_Common"+ "Inside saveIndecisionGrid...currDate "+currDate);
		//formObject.getNGValue("q_EntryDate");
		PersonalLoanS.mLogger.info("PL_Common"+ "Inside  saveIndecisionGrid entrydate @@nikhil"+formObject.getWFGeneralData());
		//below code added by nikhil
		String s=formObject.getWFGeneralData();
		int startPosition = s.indexOf("<EntryDateTime>") + "<EntryDateTime>".length();
		int endPosition = s.indexOf("</EntryDateTime>", startPosition);
		String subS = s.substring(startPosition, endPosition);


		//below query changed by nikhil 20/12/17
		String query="insert into ng_rlos_gr_Decision(dateLastChanged, userName, workstepName, Decision, remarks, dec_wi_name,Entry_Date,CIF_ID,EmirateID,CustomerNAme,DecisionReasonCode,DecisionSubReason) values('"+currDate+"','"+formObject.getUserName()+"','"+formObject.getWFActivityName()+"','"+formObject.getNGValue("cmplx_Decision_Decision")+"','"+formObject.getNGValue("cmplx_Decision_REMARKS")+"','"+formObject.getWFWorkitemName()+"',convert(varchar(30),cast('"+subS+"' as datetime),121),'"+Cifid+"','"+emiratesid+"','"+custName+"','"+formObject.getNGValue("DecisionHistory_DecisionReasonCode")+"','"+formObject.getNGValue("DecisionHistory_DecisionSubReason")+"')";
		PersonalLoanS.mLogger.info("PL_Common"+"Query is"+query);
		formObject.saveDataIntoDataSource(query);
		}
		catch( Exception e)
		{
			System.out.print("exception in csm decison load"+e);
		}
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              fetch
	Author                              : Disha              
	Description                         : Function to Save data in Refer grid   

	 ***********************************************************************************  */
	//changed by akshay on 23/11/18
	public void LoadReferGrid()
	{
		try{
			PersonalLoanS.mLogger.info("PL_Common"+ "Inside AddInReferGrid: "); 
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			/*	if(!formObject.isVisible("ReferHistory_Frame1")){
			formObject.fetchFragment("ReferHistory", "ReferHistory", "q_ReferHistory");
			formObject.setNGFrameState("ReferHistory", 0);
		}*/
			Common_Utils common=new Common_Utils(PersonalLoanS.mLogger);
			//String currDate=new SimpleDateFormat("MM/dd/yyyy HH:mm").format(Calendar.getInstance().getTime());
			String decision=formObject.getNGValue("cmplx_Decision_Decision");
			String currDate=common.Convert_dateFormat("", "","MM/dd/yyyy HH:mm");
			String ReferTo="";
			PersonalLoanS.mLogger.info("Inside Load refer grid decision "+decision);
			if("Refer".equalsIgnoreCase(decision)){
				List<String> Referlist=new ArrayList<String>();
				for(int i=0;i<formObject.getLVWRowCount("Decision_ListView1");i++){
					if(formObject.getNGValue("Decision_ListView1",i,12).equals("")){
						ReferTo=formObject.getNGValue("Decision_ListView1",i,6);
						PersonalLoanS.mLogger.info("Inside Load refer grid Refer to "+ReferTo+" Activity Name: "+formObject.getWFActivityName());
					if(!"DDVT_Maker".equalsIgnoreCase(ReferTo) && !(NGFUserResourceMgr_PL.getGlobalVar("PL_Stage_Reversal_WS").contains(formObject.getWFActivityName())))
					{
						//String ReferTo=formObject.getNGValue("cmplx_Decision_ReferTo");
						PersonalLoanS.mLogger.info("PL_Common"+ "ReferTo: "+ReferTo); 
						//String[] ReferTo_array=ReferTo.split(";");
						//for(int i=0;i<ReferTo_array.length;i++)
						//{//
							Referlist.clear();
							Referlist.add(currDate);
							Referlist.add(formObject.getUserName());
							Referlist.add(formObject.getWFActivityName());
							Referlist.add(decision);
							Referlist.add(formObject.getNGValue("cmplx_Decision_REMARKS"));
							if(("Source".equalsIgnoreCase(ReferTo) || "CSO (for documents)".equalsIgnoreCase(ReferTo)  || "RM (for documents)".equalsIgnoreCase(ReferTo) ) && formObject.getNGValue("InitiationType").equalsIgnoreCase("M"))
							{
								Referlist.add("RM_Review");
							}
							else if("CSO (for documents)".equalsIgnoreCase(ReferTo) && !formObject.getNGValue("InitiationType").equalsIgnoreCase("M"))
							{
								Referlist.add("Source");
							}
							else if("FPU".equals(ReferTo))
							{
								Referlist.add("FPU");
							}
							else{
								Referlist.add(ReferTo);
							}
							Referlist.add("");
							Referlist.add("");
							Referlist.add(formObject.getWFWorkitemName());
							formObject.addItemFromList("cmplx_ReferHistory_cmplx_GR_ReferHistory",Referlist);
					}
				}
				}
				//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
				Custom_fragmentSave("ReferHistory");
				PersonalLoanS.mLogger.info("PL_Common"+"ReferList is:"+Referlist.toString());
			}
		}
		catch(Exception ex){//formObject.RaiseEvent("WFSave");
			PersonalLoanS.mLogger.info("PL_Common ReferList Exception is:");
			printException(ex);
		}
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to loadPicklist for Decision Fields  

	 ***********************************************************************************  */

	public void  loadPicklist1()
	{
		try
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
			String ActivityName=formObject.getWFActivityName();
			PersonalLoanS.mLogger.info("PL"+"Inside PLCommon ->loadPicklist1()"+ActivityName);

			//LoadPickList("cmplx_Decision_Decision", "select '--Select--' union select  convert(varchar,Decision) from NG_MASTER_Decision with (nolock) where ProcessName='PersonalLoanS' and WorkstepName='"+ActivityName+"'");
			LoadPickList("cmplx_Decision_Decision", "select * from (select '--Select--' as decision union select decision from NG_MASTER_Decision with (nolock)  where ProcessName='PersonalLoanS' and workstepname='"+ActivityName+"'  )t order by case when decision = '--Select--' then 0 else 1 end");
			//LoadPickList("cmplx_Decision_Decision", "select '--Select--' union select  convert(varchar,Decision) from NG_MASTER_Decision with (nolock) where ProcessName='PersonalLoanS' and WorkstepName='"+ActivityName+"'");

			LoadPickList("cmplx_Decision_CADDecisiontray", "select 'Select' as Refer_Credit  union select convert(varchar, Refer_Credit)  from NG_Master_ReferCredit with (nolock) order by Refer_Credit desc"); //Arun (12/10)
		}
		catch(Exception e){ 
			PersonalLoanS.mLogger.info("PLCommon"+"Exception Occurred loadPicklist1 :"+e.getMessage());
			printException(e);
		}	
	}
	//CHanges done by aman
	public void pickListMasterLoad(String buttonName){
		try{
			String val=NGFUserResourceMgr_PL.getMasterManager(buttonName);
			String[] value= val.split(":");
			String query;
			String stableName="";
			String sColumnName="";
			String sfieldName="";
			String sHeaderName="";
			PersonalLoanS.mLogger.info("Invalid entry maintained for :"+val+"asd"+value.length);
			if(value.length==4){
				stableName=value[0];
				sColumnName=value[1];
				sfieldName=value[2];
				sHeaderName=value[3];
				if(!buttonName.equalsIgnoreCase("Customer_Button2"))
				{
				query="select "+sColumnName+" from "+stableName+" with (nolock)  where isActive='Y'";
				
				populatePickListWindow(query,sfieldName,sColumnName, true, 20,sHeaderName);	
				}
				else
					
					{
					query="select "+sColumnName+" from "+stableName+" with (nolock) ";
					
					populatePickListWindow(query,sfieldName,sColumnName, true, 20,sHeaderName);	
					}
			}
			else{
				PersonalLoanS.mLogger.info("Invalid entry maintained for :"+buttonName);
			}



		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("PLCommon Exception Occurred genericMaster :"+e.getMessage());
			printException(e);
		}

	}

	//CHanges done by aman
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to populate PickList Window 

	 ***********************************************************************************  */


	public void populatePickListWindow(String sQuery, String sControlName,String sColName, boolean bBatchingRequired, int iBatchSize,String Header)
	{

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PickList objPickList = formObject.getNGPickList(sControlName, sColName, bBatchingRequired, iBatchSize);


		objPickList.setWindowTitle("Search "+Header);


		/*		List<List<String>> result=formObject.getNGDataFromDataCache(sQuery);
		if(!result.isEmpty()){//result.size()>0
			objPickList.setHeight(600);
			objPickList.setWidth(1000);
			objPickList.setVisible(true);
			objPickList.setSearchEnabled(true);
			objPickList.addPickListListener(new PL_EventListenerHandler(objPickList.getClientId()));
			PersonalLoanS.mLogger.info("EventListenerHandler: Result Of Query:"+result.toString());   
			objPickList.populateData(result);

		}
		else{
			throw new ValidatorException(new FacesMessage("No Data Found"));
		}*/

		List<List<String>> result=formObject.getDataFromDataSource(sQuery);
		if (sControlName.equalsIgnoreCase("EMploymentDetails_Button2")){
			if(result.isEmpty())

			{
				throw new ValidatorException(new FacesMessage("No Data Found"));
			}
			else
			{
				objPickList.setHeight(600);
				objPickList.setWidth(800);
				objPickList.setVisible(true);
				objPickList.setSearchEnabled(true);
				objPickList.addPickListListener(new PL_EventListenerHandler(objPickList.getClientId()));

				PersonalLoanS.mLogger.info(result.toString());   

				objPickList.populateData(result);			
			}
		}	
		else{
			if(result.isEmpty())

			{
				throw new ValidatorException(new FacesMessage("No Data Found"));
			}
			else
			{
				objPickList.setHeight(400);
				objPickList.setWidth(400);
				objPickList.setVisible(true);
				objPickList.setSearchEnabled(true);
				objPickList.addPickListListener(new PL_EventListenerHandler(objPickList.getClientId()));

				PersonalLoanS.mLogger.info("Aman "+result.toString());   

				objPickList.populateData(result);			
			}
		}	
	}       


	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Set Values on Part Match Fragment

	 ***********************************************************************************  */




	public void partMatchValues()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		String fName = formObject.getNGValue("cmplx_Customer_FIrstNAme");
		String mName = formObject.getNGValue("cmplx_Customer_MiddleName");
		String lName = formObject.getNGValue("cmplx_Customer_LAstNAme");

		String fullName = fName+" "+mName+" "+lName; 
		formObject.setNGValue("PartMatch_funame",fullName);

		formObject.setNGValue("PartMatch_CIFID",formObject.getNGValue("cmplx_Customer_CIFNO"));
		formObject.setNGValue("PartMatch_fname",formObject.getNGValue("cmplx_Customer_FIrstNAme"));
		formObject.setNGValue("PartMatch_lname",formObject.getNGValue("cmplx_Customer_LAstNAme"));
		formObject.setNGValue("PartMatch_newpass",formObject.getNGValue("cmplx_Customer_PAssportNo"));
		//below code added by nikhil 04/01/17
		formObject.setNGValue("PartMatch_oldpass",formObject.getNGValue("cmplx_Customer_Passport2"));

		formObject.setNGValue("PartMatch_visafno",formObject.getNGValue("cmplx_Customer_VisaNo"));
		formObject.setNGValue("PartMatch_mno1",formObject.getNGValue("AlternateContactDetails_MobileNo1"));//modified by nikhil
		formObject.setNGValue("PartMatch_mno2",formObject.getNGValue("AlternateContactDetails_MobileNo2"));//Arun (23/10) to autopopulate values here
		formObject.setNGValue("PartMatch_Dob",formObject.getNGValue("cmplx_Customer_DOb"));
		formObject.setNGValue("PartMatch_EID",formObject.getNGValue("cmplx_Customer_EmiratesID"));
		formObject.setNGValue("PartMatch_nationality",formObject.getNGValue("cmplx_Customer_Nationality"));
		formObject.setNGValue("PartMatch_drno",formObject.getNGValue("cmplx_Customer_DLNo"));
		//formObject.setNGValue("PartMatch_oldpass",formObject.getNGValue("cmplx_Customer_PAssportNo")); //Arun (07/09/17)

	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to set Customer Detail Verification Values

	 ***********************************************************************************  */


	//++Below code added by nikhil 13/11/2017 for Code merge

	public void custdet1values()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		openFrags(formObject);
		autopopulateValuesFCU(formObject);
	}


	

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Load Picklist for Verification

	 ***********************************************************************************  */

	public void LoadPicklistVerification(List<String> mylist){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PersonalLoanS.mLogger.info("RLOS_Common"+ "Inside LoadPicklistVerification(): "+mylist);

		for(String control_name:mylist)
		{
			formObject.setVisible(control_name, true);
			LoadPickList(control_name, "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_CPVVeri with (nolock)  order by code");
		}
	}


	
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Load picklist for worldcheck

	 ***********************************************************************************  */

	public void loadPicklist_WorldCheck()
	{
		LoadPickList("WorldCheck1_BirthCountry", "select '--Select--','--Select--' union select convert(varchar, Description),code from ng_MASTER_Country with (nolock)");
		LoadPickList("WorldCheck1_ResidentCountry", "select '--Select--','--Select--' union select convert(varchar, Description),code from ng_MASTER_Country with (nolock)");
		//below code added by niukhil 20/12/17
		LoadPickList("WorldCheck1_Gender","select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_master_gender with (nolock) order by Code");
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Load MOL Picklist

	 ***********************************************************************************  */

	public void loadPicklistMol()
	{
		//LoadPickList("cmplx_MOL_nationality", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Load Picklist for DDS Return

	 ***********************************************************************************  */


	public void loadPicklistDDSReturn()
	{
		LoadPickList("FinacleCore_cheqtype", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Cheque_Type with (nolock) order by Code");
		LoadPickList("FinacleCore_typeofret", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_TypeOfReturn with (nolock) order by Code");
		LoadPickList("FinacleCore_acctype", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_AccountType with (nolock) order by Code");
		LoadPickList("FinacleCore_cheqretres", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Cheque_Reason with (nolock) order by Code");
	}

	

	

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to set Loan Card Verification Values

	 ***********************************************************************************  */

	public void loancardvalues()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//below code added by nikhil
		int framestate3=formObject.getNGFrameState("EligibilityAndProductInformation");
		if(framestate3 == 0){
			PersonalLoanS.mLogger.info("EligibilityAndProductInformation");
		}
		else {
			formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligibilityProdInfo");
			PersonalLoanS.mLogger.info("fetched EligibilityAndProductInformation details");
		}

		//Changes done for code optimization 25/07
		PersonalLoanS.mLogger.info( "Loan & card"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
		formObject.setNGValue("cmplx_LoanandCard_loanamt_val",formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
		PersonalLoanS.mLogger.info( "Loan & card"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor"));
		formObject.setNGValue("cmplx_LoanandCard_tenor_val",formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor"));
		PersonalLoanS.mLogger.info( "Loan & card"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_EMI"));
		formObject.setNGValue("cmplx_LoanandCard_emi_val",formObject.getNGValue("cmplx_EligibilityAndProductInfo_EMI"));
		formObject.setNGValue("cmplx_LoanandCard_islorconv_val",formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0));
		formObject.setNGValue("cmplx_LoanandCard_firstrepaydate_val",formObject.getNGValue("cmplx_EligibilityAndProductInfo_FirstRepayDate"));
		String CardType="";
		String CardLimit="";
//commented by shweta, not required in PL
		/*String CardTypeandLimit="select Card_Product,Final_Limit from ng_rlos_IGR_Eligibility_CardLimit with (nolock)  where Child_Wi='"+formObject.getWFWorkitemName()+"' and Cardproductselect='True'";
		List<List<String>> CardTypeandLimitXML = formObject.getNGDataFromDataCache(CardTypeandLimit);
		if (CardTypeandLimitXML != null){
			for (int i = 0; i<CardTypeandLimitXML.size();i++){
				if (i == 0){
					CardType=CardTypeandLimitXML.get(i).get(0);
					CardLimit=CardTypeandLimitXML.get(i).get(1);

				}
				else {
					CardType=CardType+","+CardTypeandLimitXML.get(i).get(0);
					CardLimit=CardLimit+","+CardTypeandLimitXML.get(i).get(1);
				}
			}
		}
		formObject.setNGValue("cmplx_LoanandCard_cardtype_val",CardType);
		formObject.setNGValue("cmplx_LoanandCard_cardlimit_val",CardLimit);*/
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Load Picklist checker

	 ***********************************************************************************  */


	public void  loadPicklistChecker()
	{
		try
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
			String ActivityName=formObject.getWFActivityName();
			PersonalLoanS.mLogger.info("PL"+"Inside PLCommon ->loadPicklistChecker()"+ActivityName);
			LoadPickList("cmplx_Decision_Decision", "select * from (select '--Select--' as decision union select decision from NG_MASTER_Decision with (nolock)  where ProcessName='PersonalLoanS' and workstepname='"+ActivityName+"' and Initiation_Type NOT LIKE  '%Reschedulment%' )t order by case when decision = '--Select--' then 0 else 1 end");
		}
		catch(Exception e){ 
			PersonalLoanS.mLogger.info("PLCommon"+"Exception Occurred loadPicklistChecker :"+e.getMessage());
			printException(e);
		}	
	}	

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function for primary cif Identify

	 ***********************************************************************************  */

	public int primary_cif_identify(Map<Integer, HashMap<String, String>> cusDetails )
	{
		int primary_cif = 0;
		try{
			Map<String, String> prim_entry ;
			Map<String, String> curr_entry ;


			Iterator<Map.Entry<Integer, HashMap<String, String>>> itr = cusDetails.entrySet().iterator();
			while(itr.hasNext()){
				Map.Entry<Integer, HashMap<String, String>> entry =  itr.next();

				curr_entry = entry.getValue();
				if("Internal".equalsIgnoreCase(curr_entry.get("SearchType")))
				{
					if(primary_cif==0 && curr_entry.containsKey("Products")){
						primary_cif = entry.getKey();
					}
					else if(curr_entry.containsKey("Products"))
					{
						prim_entry = cusDetails.get(Integer.toString(primary_cif)+"");
						int prim_entry_prod_no = Integer.parseInt(prim_entry.get("Products"));
						int curr_entry_prod_no = Integer.parseInt(curr_entry.get("Products"));

						if(curr_entry_prod_no>prim_entry_prod_no){
							primary_cif = entry.getKey();
						}
						else if(curr_entry_prod_no==prim_entry_prod_no)
						{
							int prim_cif_no = Integer.parseInt(prim_entry.get("CustId"));
							int curr_cif_no = Integer.parseInt(curr_entry.get("CustId"));
							if(curr_cif_no>prim_cif_no)
								primary_cif = curr_cif_no;

						}

					}
				}

			}

		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("Exception occured while parsing Customer Eligibility : "+ e.getMessage());
			printException(e);
		}

		return primary_cif;
	}


	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function for Customer Update

	 ***********************************************************************************  */

	public String CustomerUpdate(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String alert_msg = "";
		String outputResponse;
		String cif_verf_status;
		String   ReturnCode;
		PL_Integration_Output PL_Integ_out  = new PL_Integration_Output();

		if("PRIMARY".equalsIgnoreCase(MultipleAppGridSelectedRow("MultipleApp_AppType")) && "false".equals(formObject.getNGValue("cmplx_Customer_NTB"))){
			cif_verf_status="Y";
		}
		else{
			cif_verf_status = MultipleAppGridSelectedRow("MultipleApp_AppCIF_Verified");//formObject.getNGValue("is_cust_verified");
		}
		String Existingcust = formObject.getNGValue("cmplx_Customer_NTB");
		PersonalLoanS.mLogger.info("PL DDVT Vhecker"+ "EXISTING CUST : "+ Existingcust);

		String Cif_lock_status = MultipleAppGridSelectedRow("MultipleApp_AppCIF_Locked");
		PersonalLoanS.mLogger.info("PL DDVT Vhecker"+ "cif_verf_status : "+ cif_verf_status);
		PersonalLoanS.mLogger.info("PL DDVT Vhecker"+ "cif_Lock_status : "+ Cif_lock_status);
		if ("".equalsIgnoreCase(cif_verf_status)||"N".equalsIgnoreCase(cif_verf_status)){
			outputResponse = new PL_Integration_Input().GenerateXML("CUSTOMER_UPDATE_REQ","CIF_verify");

			ReturnCode = outputResponse.contains("<ReturnCode>")? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCode) ||  "MSGEXC50107".equalsIgnoreCase(ReturnCode)){
				//below line added by akshay on 6/3/18 for drop 4
				if("PRIMARY".equalsIgnoreCase(MultipleAppGridSelectedRow("MultipleApp_AppType")))
				{
					formObject.setNGValue("is_cust_verified", "Y");
					//formObject.setNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),5,"Y");
				}
				formObject.setNGValue("cmplx_Decision_MultipleApplicantsGrid", formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),5,"Y");
				cif_verf_status="Y";
				alert_msg="Customer verified Successfully";
				formObject.RaiseEvent("WFSave");
			}
			else{
				if("PRIMARY".equalsIgnoreCase(MultipleAppGridSelectedRow("MultipleApp_AppType")))
				{
					formObject.setNGValue("is_cust_verified", "N");
				}
				formObject.setNGValue("cmplx_Decision_MultipleApplicantsGrid", formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),5,"N");
				PersonalLoanS.mLogger.info("DDVT Checker Customer Update operation: "+ "Error in Cif Enquiry operation Return code is: "+ReturnCode);
				alert_msg= "Customer status Enquiry Failed, Please try after some time or contact administrator";
				formObject.RaiseEvent("WFSave");

			}
		}

		if ("Y".equalsIgnoreCase(cif_verf_status)&&("".equalsIgnoreCase(Cif_lock_status)||"N".equalsIgnoreCase(Cif_lock_status)))
		{
			PersonalLoanS.mLogger.info("PL DDVT Checker"+ "Inside Lock and Update Customer");
			outputResponse = new PL_Integration_Input().GenerateXML("CUSTOMER_DETAILS","CIF_LOCK");
			ReturnCode = outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCode) || ("CINF376001".equalsIgnoreCase(ReturnCode)))
			{
				Cif_lock_status="Y";
				PersonalLoanS.mLogger.info("PL DDVT Checker"+ "Locked sucessfully and now Unlock and update customer");
				if("PRIMARY".equalsIgnoreCase(MultipleAppGridSelectedRow("MultipleApp_AppType")))
				{
					formObject.setNGValue("Is_CustLock", "Y");
				}
				formObject.setNGValue("cmplx_Decision_MultipleApplicantsGrid", formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),6,"Y");
				outputResponse = new PL_Integration_Input().GenerateXML("CUSTOMER_DETAILS","CIF_UNLOCK");
				ReturnCode =  outputResponse.contains("<ReturnCode>")? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";

				if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCode))
				{
					PersonalLoanS.mLogger.info("DDVT Checker Customer Update operation: "+ "Cif UnLock sucessfull");
					formObject.setNGValue("cmplx_Decision_MultipleApplicantsGrid", formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),6,"N");
					//below if else added by akshay on 6/3/18 for drop 4
					if("GUARANTOR".equalsIgnoreCase(MultipleAppGridSelectedRow("MultipleApp_AppType")))
					{
						outputResponse = new PL_Integration_Input().GenerateXML("CUSTOMER_UPDATE_REQ","SUPPLEMENT_CIF_UPDATE");
					}
					else{
						formObject.setNGValue("Is_CustLock", "N");
						Cif_lock_status="N";
						outputResponse = new PL_Integration_Input().GenerateXML("CUSTOMER_UPDATE_REQ","CIF_update");
					}

					ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);

					if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCode)){
						if("PRIMARY".equalsIgnoreCase(MultipleAppGridSelectedRow("MultipleApp_AppType")))
						{
							formObject.setNGValue("CUSTOMER_UPDATE_REQ","Y");
						}
						//below line added by akshay on 6/3/18 for drop 4
						formObject.setNGValue("cmplx_Decision_MultipleApplicantsGrid", formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),7,"Y");
						PersonalLoanS.mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+"value of CUSTOMER_UPDATE_REQ"+formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
						PL_Integ_out.valueSetIntegration(outputResponse,"");    
						formObject.setEnabled("DecisionHistory_updcust", true);//update account button
						PersonalLoanS.mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+"CUSTOMER_UPDATE_REQ is generated");
						PersonalLoanS.mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
						alert_msg = "Customer Updated Successful!";
						formObject.setEnabled("DecisionHistory_updacct_loan", true);
						formObject.setNGValue("cmplx_LoanDisb_updateCustomerFlag", "Y");
						//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
						Custom_fragmentSave("Loan_Disbursal_Frame2");
					}
					else{
						alert_msg= "Customer Update operation failed, Please try after some time or contact administrator";
						formObject.setEnabled("DecisionHistory_Button3", true);
						formObject.setLocked("DecisionHistory_updcust", true);//update account button
						PersonalLoanS.mLogger.info("Customer Details"+"Customer Update operation Failed");
						if("PRIMARY".equalsIgnoreCase(MultipleAppGridSelectedRow("MultipleApp_AppType")))
						{
							formObject.setNGValue("Is_CUSTOMER_UPDATE_REQ","N");
						}
						formObject.setNGValue("cmplx_Decision_MultipleApplicantsGrid", formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),7,"N");

					}
					PersonalLoanS.mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
					formObject.RaiseEvent("WFSave");
					PersonalLoanS.mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+"after saving the flag");

				}
				else{
					PersonalLoanS.mLogger.info("DDVT Checker Customer Update operation: "+ "Cif UnLock failed return code: "+ReturnCode);
					alert_msg= "Customer UnLock operation failed, Please try after some time or contact administrator";
				}

			}
			else{
				if("PRIMARY".equalsIgnoreCase(MultipleAppGridSelectedRow("MultipleApp_AppType")))
				{
					formObject.setNGValue("Is_CustLock", "N");
				}
				//formObject.setNGValue("cmplx_Decision_MultipleApplicantsGrid", formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),6,"Y");
				PersonalLoanS.mLogger.info("DDVT Checker Customer Update operation: "+ "Error in Cif Lock operation Return code is: "+ReturnCode);
				alert_msg= "Customer status Enquiry Failed, Please try after some time or contact administrator";
			}

		}
		else if("Y".equalsIgnoreCase(cif_verf_status)&& "Y".equalsIgnoreCase(Cif_lock_status))
		{
			outputResponse = new PL_Integration_Input().GenerateXML("CUSTOMER_DETAILS","CIF_UNLOCK");
			ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCode))
			{

				PersonalLoanS.mLogger.info("DDVT Checker Customer Update operation: "+ "Cif UnLock sucessfull");
				formObject.setNGValue("cmplx_Decision_MultipleApplicantsGrid", formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),6,"N");

				//below if else added by akshay on 6/3/18 for drop 4
				if("GUARANTOR".equalsIgnoreCase(MultipleAppGridSelectedRow("MultipleApp_AppType")))
				{
					outputResponse = new PL_Integration_Input().GenerateXML("CUSTOMER_UPDATE_REQ","SUPPLEMENT_CIF_UPDATE");
				}
				else{
					formObject.setNGValue("Is_CustLock", "N");
					Cif_lock_status="N";
					outputResponse = new PL_Integration_Input().GenerateXML("CUSTOMER_UPDATE_REQ","CIF_update");
				}
				ReturnCode =  outputResponse.contains("<ReturnCode>") ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				PersonalLoanS.mLogger.info("RLOS value of ReturnCode"+ReturnCode);

				if(NGFUserResourceMgr_PL.getGlobalVar("PL_IntegrationSuccess").equalsIgnoreCase(ReturnCode)){
					if("PRIMARY".equalsIgnoreCase(MultipleAppGridSelectedRow("MultipleApp_AppType")))
					{
						formObject.setNGValue("CUSTOMER_UPDATE_REQ","Y");
					}
					//below line added by akshay on 6/3/18 for drop 4
					formObject.setNGValue("cmplx_Decision_MultipleApplicantsGrid", formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),7,"Y");
					PersonalLoanS.mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+"value of CUSTOMER_UPDATE_REQ"+formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
					PL_Integ_out.valueSetIntegration(outputResponse,"");    
					PersonalLoanS.mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+"CUSTOMER_UPDATE_REQ is generated");
					PersonalLoanS.mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
					alert_msg = "Customer Updated Successful!";
				}
				else{
					PersonalLoanS.mLogger.info("Customer Details"+"ACCOUNT_MAINTENANCE_REQ is not generated");
					if("PRIMARY".equalsIgnoreCase(MultipleAppGridSelectedRow("MultipleApp_AppType")))
					{
						formObject.setNGValue("Is_CUSTOMER_UPDATE_REQ","N");
					}
					formObject.setNGValue("cmplx_Decision_MultipleApplicantsGrid", formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),7,"N");
					alert_msg= "Customer status Enquiry Failed, Please try after some time or contact administrator";


				}
				PersonalLoanS.mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
				formObject.RaiseEvent("WFSave");
				PersonalLoanS.mLogger.info("RLOS value of CUSTOMER_UPDATE_REQ"+"after saving the flag");
				if(NGFUserResourceMgr_PL.getGlobalVar("PL_Y").equalsIgnoreCase(formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ")))
				{ 
					PersonalLoanS.mLogger.info("RLOS value of Is_CUSTOMER_UPDATE_REQ"+"inside if condition");
					formObject.setEnabled("DecisionHistory_Button3", false); 
				}
				else{
					formObject.setEnabled("DecisionHistory_Button3", true);
				}

			}
			else{
				PersonalLoanS.mLogger.info("DDVT Checker Customer Update operation: "+ "Cif UnLock failed return code: "+ReturnCode);
				alert_msg= "Customer UnLock operation failed, Please try after some time or contact administrator";

			}
		}
		return alert_msg;
	}
	
	//updated with CC disbursal code
	public String CustomerUpdatePrimarySupplementary(String buttonId){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String alert_msg = "";
		String outputResponse = "";
		String   ReturnCode="";
		String cif_verf_status ="";// getCIFStatus("verification",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"));//formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),14);//formObject.getNGValue("is_cust_verified");
		
		if(buttonId.contains("Limit_Inc") || buttonId.contains("DecisionHistory_Button3")){
			String pass = formObject.getNGValue("cmplx_Customer_PAssportNo");
			String cifId = formObject.getNGValue("cmplx_Customer_CIFNO");
			//getCIFStatus("verification","cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"));
			cif_verf_status=getCIFStatus("verification",pass,cifId);
		}
		
		else{
			String pass = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13);
			String cifId = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),3);
			cif_verf_status=getCIFStatus("verification",pass,cifId);
		}
		
		String Existingcust = ""; 
		if(buttonId.contains("Limit_Inc") || buttonId.contains("DecisionHistory_Button3")){
			Existingcust=formObject.getNGValue("cmplx_Customer_NTB");
		}
		else{
			Existingcust=checkSuppPrimaryNTB();	
		}	
		
		PL_Integration_Input genX=new PL_Integration_Input();

		if("false".equalsIgnoreCase(Existingcust)){
			cif_verf_status = "Y";
		}
		String Cif_lock_status = "";
		if(buttonId.contains("Limit_Inc") || buttonId.contains("DecisionHistory_Button3")){
			String pass = formObject.getNGValue("cmplx_Customer_PAssportNo");
			String cifId = formObject.getNGValue("cmplx_Customer_CIFNO");
			//getCIFStatus("verification","cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"));
			Cif_lock_status=getCIFStatus("lock",pass,cifId);
		}
		else{
			String pass = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13);
			String cifId = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),3);
			getCIFStatus("lock",pass,cifId);//formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),14);//formObject.getNGValue("is_cust_verified");	
		}	

		//String Cif_unlock_status = formObject.getNGValue("is_cust_verified");
		PersonalLoanS.mLogger.info( "cif_verf_status : "+ cif_verf_status);
		PersonalLoanS.mLogger.info( "cif_Lock_status : "+ Cif_lock_status);
		if ("".equalsIgnoreCase(cif_verf_status)||"N".equalsIgnoreCase(cif_verf_status)){
			outputResponse = genX.GenerateXML("CUSTOMER_UPDATE_REQ","CIF_verify");

			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";

			if("0000".equalsIgnoreCase(ReturnCode) || "MSGEXC50107".equalsIgnoreCase(ReturnCode)){
				/*if("PRIMARY".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)))
				{*/
				setCifVerifyLockstatus("Y",5);
				formObject.RaiseEvent("WFSave");
				//formObject.setNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13,"");
				//}
				cif_verf_status="Y";
				alert_msg=NGFUserResourceMgr_PL.getAlert("VAL019");
			}
			else{
				/*if("PRIMARY".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)))
				{*/
				/*String cif = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),3);
				String currstatus = formObject.getNGValue("is_cust_verified");*/
				setCifVerifyLockstatus("N",5);
				//formObject.setNGValue("is_cust_verified", "N");
				//}
				String alert=formObject.getNGDataFromDataCache("select Alert from ng_MASTER_INTEGRATION_ERROR_CODE with (nolock)  where  Error_Code='2033'").get(0).get(0);
				PersonalLoanS.mLogger.info( ReturnCode+": "+alert);
				alert_msg= alert;//NGFUserResourceMgr_PersonalLoanS.getAlert("VAL015");
			}
			Custom_fragmentSave("DecisionHistory");
			formObject.RaiseEvent("WFSave");
		}

		if ("Y".equalsIgnoreCase(cif_verf_status)&&("".equalsIgnoreCase(Cif_lock_status)||"N".equalsIgnoreCase(Cif_lock_status)))
		{
			PersonalLoanS.mLogger.info( "Inside Lock and Update Customer");
			outputResponse = genX.GenerateXML("CUSTOMER_DETAILS","CIF_LOCK");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			if("0000".equalsIgnoreCase(ReturnCode))
			{
				Cif_lock_status="Y";
				PersonalLoanS.mLogger.info( "Locked Successfully and now Unlock and update customer");
				/*if("PRIMARY".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)))
				{*/
				//formObject.setNGValue("Is_CustLock", "Y");
				setCifVerifyLockstatus("Y", 6);
				//}
				outputResponse = genX.GenerateXML("CUSTOMER_DETAILS","CIF_UNLOCK");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";

				if("0000".equalsIgnoreCase(ReturnCode))
				{

					Cif_lock_status="N";
					PersonalLoanS.mLogger.info( "Cif UnLock sucessfull");
					setCifVerifyLockstatus("N", 6);
					if("PRIMARY".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)))
					{
						//formObject.setNGValue("Is_CustLock", "N");

						outputResponse = genX.GenerateXML("CUSTOMER_UPDATE_REQ","CIF_update");
					}
					else if("SUPPLEMENT".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)))
					{
						outputResponse = genX.GenerateXML("CUSTOMER_UPDATE_REQ","SUPPLEMENT_CIF_UPDATE");
					}
					ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
					PersonalLoanS.mLogger.info(ReturnCode);

					if("0000".equalsIgnoreCase(ReturnCode)){
						if("PRIMARY".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)) || buttonId.contains("Limit_Inc"))
						{
							formObject.setNGValue("CUSTOMER_UPDATE_REQ","Y");
						}
						PersonalLoanS.mLogger.info("value of CUSTOMER_UPDATE_REQ"+formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
						new PL_Integration_Output().valueSetIntegration(outputResponse,""); 
						//valueSetCustomer(outputResponse,""); 
						if(buttonId.contains("CC_Creation")){
							if(!"Y".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getNGListIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),8))){
								formObject.setEnabled("CC_Creation_Button2", true);
							}
							formObject.setNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getNGListIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),10,"Y");							
						}
						else if(buttonId.contains("Limit_Inc")){
							formObject.setEnabled("Limit_Inc_Button1",true);
							formObject.setNGValue("cmplx_LimitInc_UpdateCustFlag", "Y");
						}
						PersonalLoanS.mLogger.info("CUSTOMER_UPDATE_REQ is generated");
						PersonalLoanS.mLogger.info(formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
						alert_msg = NGFUserResourceMgr_PL.getAlert("VAL018");
					}
					else{
						alert_msg= NGFUserResourceMgr_PL.getAlert("VAL017");
						formObject.setEnabled("DecisionHistory_Button3", true);
						PersonalLoanS.mLogger.info("Customer Update operation Failed");
						if("PRIMARY".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)) || buttonId.contains("Limit_Inc"))
						{
							formObject.setNGValue("Is_CUSTOMER_UPDATE_REQ","N");
						}
					}
					PersonalLoanS.mLogger.info(formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
					formObject.RaiseEvent("WFSave");
					PersonalLoanS.mLogger.info("after saving the flag");
				}
				else{
					PersonalLoanS.mLogger.info( "Cif UnLock failed return code: "+ReturnCode);
					alert_msg= NGFUserResourceMgr_PL.getAlert("VAL016");
				}

			}
			else{

				//formObject.setNGValue("Is_CustLock", "N");
				setCifVerifyLockstatus("N", 6);
				PersonalLoanS.mLogger.info( "Error in Cif Lock operation Return code is: "+ReturnCode);
				alert_msg= NGFUserResourceMgr_PL.getAlert("VAL015");
			}

		}
		else if("Y".equalsIgnoreCase(cif_verf_status)&& "Y".equalsIgnoreCase(Cif_lock_status))
		{
			outputResponse = genX.GenerateXML("CUSTOMER_DETAILS","CIF_UNLOCK");
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			if("0000".equalsIgnoreCase(ReturnCode))
			{
				/*	if("PRIMARY".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)))
				{*/
				//formObject.setNGValue("Is_CustLock", "N");
				setCifVerifyLockstatus("N", 6);
				//}
				Cif_lock_status="N";
				PersonalLoanS.mLogger.info( "Cif UnLock sucessfull");

				outputResponse = genX.GenerateXML("CUSTOMER_UPDATE_REQ","CIF_update");
				ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
				PersonalLoanS.mLogger.info(ReturnCode);

				if("0000".equalsIgnoreCase(ReturnCode)){
					if("PRIMARY".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12))|| buttonId.contains("Limit_Inc"))
					{
						formObject.setNGValue("CUSTOMER_UPDATE_REQ","Y");
					}
					PersonalLoanS.mLogger.info("value of CUSTOMER_UPDATE_REQ"+formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
					new PL_Integration_Output().valueSetIntegration(outputResponse,""); 
					//valueSetCustomer(outputResponse,""); 
					if(buttonId.contains("CC_Creation")){
						formObject.setEnabled("CC_Creation_Button2", true);
						formObject.setNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getNGListIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),10,"Y");
					}
					else if(buttonId.contains("Limit_Inc")){
						formObject.setEnabled("Limit_Inc_Button1",true);
						formObject.setNGValue("cmplx_LimitInc_UpdateCustFlag", "Y");
					}
					PersonalLoanS.mLogger.info("CUSTOMER_UPDATE_REQ is generated");
					PersonalLoanS.mLogger.info(formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
				}
				else{
					PersonalLoanS.mLogger.info("ACCOUNT_MAINTENANCE_REQ is not generated");
					if("PRIMARY".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)) || buttonId.contains("Limit_Inc"))
					{
						formObject.setNGValue("Is_CUSTOMER_UPDATE_REQ","N");
					}
					alert_msg = NGFUserResourceMgr_PL.getAlert("VAL017");
				}
				PersonalLoanS.mLogger.info(formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ"));
				formObject.RaiseEvent("WFSave");
				PersonalLoanS.mLogger.info("after saving the flag");
				if(	NGFUserResourceMgr_PL.getGlobalVar("PL_Y").equalsIgnoreCase(formObject.getNGValue("Is_CUSTOMER_UPDATE_REQ")))
				{ 
					PersonalLoanS.mLogger.info("inside if condition");
					formObject.setEnabled("DecisionHistory_Button3", false); 
					//throw new ValidatorException(new CustomExceptionHandler("Customer Updated Successful!!","FetchDetails#Customer Updated Successful!!","",hm));
				}
				else{
					formObject.setEnabled("DecisionHistory_Button3", true);
					//throw new ValidatorException(new CustomExceptionHandler("Customer Updated Fail!!","FetchDetails#Customer Updated Fail!!","",hm));
				}

			}
			else{
				PersonalLoanS.mLogger.info( "Cif UnLock failed return code: "+ReturnCode);
				alert_msg= NGFUserResourceMgr_PL.getAlert("VAL016");
			}
		}
		return alert_msg;
	}
	
	public String fetchIsNTBval(String applicantType) {
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();	
			String ntbVal = "-1";
			String listView = "cmplx_CCCreation_cmplx_CCCreationGrid";
			PersonalLoanS.mLogger.info("Inside fetchNTBVal function ");
			
			//Deepak 20 Dec PCSP 142 Changes to send NTB customer as existing for second card if his first card got created successfully start 
			String selectPassport = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13);
			String selectCIF = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),2);
			int rowcount = formObject.getLVWRowCount(listView);
			int createdCards = 0;
			for(int i=0;i<rowcount;i++){
				String gridPassprtValue = formObject.getNGValue(listView,i,13);
				String gridCifValue = formObject.getNGValue(listView,i,2);
				if(formObject.getNGValue(listView, i, 7).equals("Y") && gridPassprtValue.equalsIgnoreCase(selectPassport) && gridCifValue.equalsIgnoreCase(selectCIF)){
					createdCards++;
					break;
				}
			}
			if(createdCards>0){
				ntbVal="0";
				return ntbVal;
			}
			//Deepak 20 Dec PCSP 142 Changes to send NTB customer as existing for second card if his first card got created successfully END
			
			
			if(applicantType.equalsIgnoreCase("Primary")){
				PersonalLoanS.mLogger.info("Inside fetchNTBVal function primary");
				String query = "select count(distinct ECRN) from ng_RLOS_CUSTEXPOSE_CardDetails with (nolock) where Child_Wi = '"+formObject.getWFWorkitemName()+"' and ecrn !='' and ecrn is not null";
				PersonalLoanS.mLogger.info("CC Integration query for ECRN "+query);
				List<List<String>> count = formObject.getNGDataFromDataCache(query);

				if(count!=null && count.size()>0 && count.get(0)!=null){
					if(Integer.parseInt(count.get(0).get(0))>0){
						ntbVal="0";
					}
					else{
						//int rowcount = formObject.getLVWRowCount(listView);
						 createdCards = 0;
						for(int i=0;i<rowcount;i++){
							if(formObject.getNGValue(listView, i, 8).equals("Y")){
								createdCards++;
								break;
							}
						}
						if(createdCards>0){
							ntbVal="0";
						}
					}
				}
			}
			else if (applicantType.equalsIgnoreCase("Supplement")){
				PersonalLoanS.mLogger.info("Inside fetchNTBVal function supplement");
				String ntb = checkSuppPrimaryNTB();
				PersonalLoanS.mLogger.info("Inside fetchNTBVal function supplement ntb value is: "+ntb);
				if("false".equalsIgnoreCase(ntb)){
					//this is ntb case.
					//Deepak 20 Dec PCSP 142 Changes below code moved to above & NTB true condition added.
					ntbVal="0";
				}
				else {
					//Deepak 20 Dec PCSP-254 changes done as in this condition supplimentry will be NTB & for NTB -1 flag need to send in condition.
					ntbVal = "-1";
				}
				PersonalLoanS.mLogger.info("Inside fetchNTBVal function supplement value is: "+ntbVal);
			}
			PersonalLoanS.mLogger.info("Inside fetchNTBVal function before return is: "+ntbVal);
			return ntbVal;
		}catch(Exception ex){
			PersonalLoanS.mLogger.info("CC_INtegration Exception in CARD services custom function:  ");
			printException(ex);
			return "";
		}
	}
	public String getCIFStatus(String col,String passport,String cifId){
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String listViewName = "cmplx_Decision_MultipleApplicantsGrid";
			String flagVal = "";
			int rowCount  = formObject.getLVWRowCount(listViewName);
			String passPort = passport; //formObject.getNGValue(lvName,ccCreationRowIndex,13);
			String cif = cifId;//formObject.getNGValue(lvName,ccCreationRowIndex,3);
			for(int i=0;i<rowCount;i++){
				if(passPort.equals(formObject.getNGValue(listViewName,i,2)) && cif.equals(formObject.getNGValue(listViewName,i,3))){
					if(col.equals("verification")){
						flagVal = formObject.getNGValue(listViewName,i,5);
					}
					else if(col.equals("lock")){
						flagVal = formObject.getNGValue(listViewName,i,6);
					}
				}
			}
			return flagVal;
		}catch(Exception ex){
			PersonalLoanS.mLogger.info("CC Common Exception Inside loadInCCCreationGrid()");
			printException(ex);
			return "";
		}
	}
	public String checkSuppPrimaryNTB() {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//String cif = formObject.getNGValue("CC_Creation_CIF");
		String ntb = "false";
		String passport = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13);
		String cardprod = formObject.getNGValue("CC_Creation_Product");
		String appType = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12);
		PersonalLoanS.mLogger.info("Inside CCCommon ->passport()"+passport);
		PersonalLoanS.mLogger.info("Inside CCCommon ->cardprod()"+cardprod);
		PersonalLoanS.mLogger.info("Inside CCCommon ->appType()"+appType);

		if(appType.equalsIgnoreCase("Primary")){
			ntb = formObject.getNGValue("cmplx_Customer_NTB");
			PersonalLoanS.mLogger.info("Inside CCCommon ->Primary()"+ntb);
		}
		else if(appType.equalsIgnoreCase("Supplement")){
			int suppgrirowCount = formObject.getLVWRowCount("SupplementCardDetails_cmplx_supplementGrid");
			PersonalLoanS.mLogger.info("Inside CCCommon ->suppgrirowCount()"+suppgrirowCount);
			for(int i=0;i<suppgrirowCount;i++){
				String pass = formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,3);
				PersonalLoanS.mLogger.info("Inside CCCommon ->pass()"+pass);
				String cardProd = formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,30);
				PersonalLoanS.mLogger.info("Inside CCCommon ->cardProd()"+cardProd);
				String cifId = formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,33);
				PersonalLoanS.mLogger.info("Inside CCCommon ->cifId()"+cifId);
				if(cardProd.equalsIgnoreCase(cardprod) && pass.equalsIgnoreCase(passport)){
					if(cifId.equalsIgnoreCase("")){//changed from !cifId to cifId by akshay on 22/5/18
						ntb = "true"; 
					}
					break;
				}
			}
		}
		return ntb;
	}
	public void setCifVerifyLockstatus(String status,int index) {
		// TODO Auto-generated method stub
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String List_view = "";
		String cif="";
		if("Reject_Queue".equalsIgnoreCase(formObject.getWFActivityName())){
			PersonalLoanS.mLogger.info("Inside setCifVerifyLockstatus ->in Reject_queue");
			List_view="cmplx_Decision_MultipleApplicantsGrid";
			int selected_index=formObject.getSelectedIndex(List_view);
			cif = formObject.getNGValue(List_view,selected_index,3);
			formObject.setNGValue("cmplx_Decision_MultipleApplicantsGrid",selected_index,index,status);
		} else {
			List_view="cmplx_CCCreation_cmplx_CCCreationGrid";
			PersonalLoanS.mLogger.info("Inside PLCommon ->before selected index");
			int selected_index=formObject.getSelectedIndex(List_view);
			PersonalLoanS.mLogger.info("Inside PLCommon ->before selected index::"+selected_index);
			cif = formObject.getNGValue(List_view,selected_index,3);
			PersonalLoanS.mLogger.info("Inside PLCommon ->cif()"+cif);
			String passPort = formObject.getNGValue(List_view,selected_index,13);
			PersonalLoanS.mLogger.info("Inside PLCommon ->passPort()"+passPort);
			
			for(int i=0;i<formObject.getLVWRowCount("cmplx_Decision_MultipleApplicantsGrid");i++){
				PersonalLoanS.mLogger.info("Inside for loop @shweta");
				if(formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",i,3).equals(cif))
				{
					PersonalLoanS.mLogger.info("Inside PLCommon -> iteration to set status: "+status+" index: "+index+" is: "+i);
					formObject.setNGValue("cmplx_Decision_MultipleApplicantsGrid",i,index,status);
					break;
				}
			}  
		}
	}


	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to fetch Document Repeater

	 ***********************************************************************************  */


	public void fetchoriginalDocRepeater(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		PersonalLoanS.mLogger.info("INSIDE OriginalDocument:" +"");
		String query = "SELECT distinct DocumentName,DocStatus,'','','',Mandatory FROM NG_RLOS_GR_incomingDocument with (nolock) WHERE  incomingDOCGR_Winame='"+formObject.getWFWorkitemName()+"' order by Mandatory desc,DocumentName asc";
		List<String> repeaterHeaders = new ArrayList<String>();
		PersonalLoanS.mLogger.info("query"+""+ query);
		repeaterHeaders.add("Document List");

		repeaterHeaders.add("Status");
		repeaterHeaders.add("Received Date");
		repeaterHeaders.add("Expected Date");
		repeaterHeaders.add("Remarks");
		repeaterHeaders.add("OV Decision");
		repeaterHeaders.add("Approved By");
		repeaterHeaders.add("DC Decision");
		repeaterHeaders.add("DC Remarks");

		PersonalLoanS.mLogger.info("INSIDE Original Validation:" +"after making headers");
		FormContext.getCurrentInstance().getFormConfig().getConfigElement("ProcessName");
		List<List<String>> documents = formObject.getNGDataFromDataCache(query);
		String documentName = null;
		IRepeater repObj;
		int repRowCount = 0;
		repObj = formObject.getRepeaterControl("OriginalValidation_Frame");		
		repRowCount = repObj.getRepeaterRowCount();

		try{
			repObj.setRepeaterHeaders(repeaterHeaders);
			if (documents.size()> 0 && repRowCount==0) {
				PersonalLoanS.mLogger.info("RLOS Original Validation 1 "+ repObj);

				repObj.clear();
				PersonalLoanS.mLogger.info("RLOS Original Validation 2 "+ "CLeared repeater object");
				repObj.setRepeaterHeaders(repeaterHeaders);
				PersonalLoanS.mLogger.info("RLOS Original Validation"+ repObj);
				PersonalLoanS.mLogger.info("RLOS Original Validation"+ "CLeared repeater object");
				
				for(int i=0;i<documents.size();i++){
					repObj.addRow();
					PersonalLoanS.mLogger.info("document Added in Repeater"+" "+ documents.get(i).get(0));
					repObj.setValue(i, 0, documents.get(i).get(0));
					repObj.setValue(i, 1, documents.get(i).get(1));
					repObj.setValue(i, 4, documents.get(i).get(2));
					PersonalLoanS.mLogger.info("Repeater Row Count "+ " " + repRowCount);
				}
				PersonalLoanS.mLogger.info("document Added in Repeater"+" "+ repObj.getColumnDataList(0));

				PersonalLoanS.mLogger.info("document Added in Repeater"+" "+ formObject.getWFActivityName());
				
			}
			if("Document_Checker".equalsIgnoreCase(formObject.getWFActivityName())){
				PersonalLoanS.mLogger.info("document Added in Repeater"+" "+ repObj.getRepeaterHeaders());
				repObj.setColumnEditable(0, false);
				repObj.setColumnEditable(1, false);
				repObj.setColumnEditable(2, false);
				repObj.setColumnEditable(3, false);
				repObj.setColumnEditable(4, false);
				repObj.setColumnEditable(5, false);
				repObj.setColumnEditable(6, false);
				repObj.setColumnVisible(7,true);
				repObj.setColumnVisible(8,true);
				PersonalLoanS.mLogger.info("document Added in Repeater"+" "+ repObj.getRepeaterHeaders());

			} else if ("Original_Validation".equalsIgnoreCase(formObject.getWFActivityName())){
				repObj.setColumnEditable(0, false);
				repObj.setColumnEditable(1, false);
				repObj.setColumnEditable(6, false);
				repObj.setColumnVisible(7,false);
				repObj.setColumnVisible(8,false);
			} else if (formObject.getWFActivityName().contains("PostDisbursal")){
				repObj.setColumnEditable(0, false);
				repObj.setColumnEditable(1, true);
				repObj.setColumnEditable(2, false);
				repObj.setColumnEditable(3, false);
				repObj.setColumnEditable(4, false);
				repObj.setColumnEditable(5, false);
				repObj.setColumnEditable(6, false);
				repObj.setColumnVisible(7,true);
				repObj.setColumnVisible(8,true);
				repObj.setColumnEditable(7, false);
				repObj.setColumnEditable(8, false);
			}
			
		}
		catch (Exception e) {
			PersonalLoanS.mLogger.info("EXCEPTION    :    "+ " " + e.toString());
			printException(e);
		} 

	}
/*	public  String getEMI(double loanAmount,double rate,double tenureMonths)
	{   
		String loanAmt_DaysDiff="";
		try{
			if(Math.abs(loanAmount)<epsilon ||Math.abs(rate)<epsilon || Math.abs(tenureMonths)<epsilon){
				return "0";
			}
			BigDecimal B_intrate=  BigDecimal.valueOf(rate);
			BigDecimal B_tenure=  BigDecimal.valueOf(tenureMonths);
			BigDecimal B_loamamount= BigDecimal.valueOf(loanAmount);
			loanAmt_DaysDiff=calcgoalseekEMI(B_intrate,B_tenure,B_loamamount);

		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("RLOSCommon"+ "Exception occured in getEMI() : ");
			printException(e);
			loanAmt_DaysDiff = "0";
		}
		return loanAmt_DaysDiff;
	} */

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Calculate EMI

	 ***********************************************************************************  */

/*	public  String getEMI(double loanAmount,double rate,double tenureMonths,int mono_days)
	{   
		try{
			PersonalLoanS.mLogger.info("In new EMI Mono_day: "+ mono_days);
			if(mono_days>30){
				PersonalLoanS.mLogger.info("In new EMI Mono_day: "+ mono_days);
				mono_days = mono_days-30;
				PersonalLoanS.mLogger.info("In new EMI Mono_day to be considered: "+ mono_days);
				Double mono_intrest = ((Double.parseDouble(getEMI(loanAmount,rate,1))-loanAmount)/30)*mono_days;
				PersonalLoanS.mLogger.info("Exception in new EMI amount to be added: "+ mono_intrest);
				loanAmount = loanAmount+mono_intrest;
				PersonalLoanS.mLogger.info("In new loan amount: "+ loanAmount);
			}
			else if(mono_days<30){
				PersonalLoanS.mLogger.info("In new EMI Mono_day: "+ mono_days);
				mono_days = 30-mono_days;
				PersonalLoanS.mLogger.info("In new EMI Mono_day to be considered: "+ mono_days);
				Double mono_intrest = ((Double.parseDouble(getEMI(loanAmount,rate,1))-loanAmount)/30)*mono_days;
				PersonalLoanS.mLogger.info("Exception in new EMI amount to be added: "+ mono_intrest);
				loanAmount = loanAmount-mono_intrest;
				PersonalLoanS.mLogger.info("In new loan amount: "+ loanAmount);
			} 
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("Exception in new EMI logic: "+ e.getMessage());
		}
		
		String loanAmt_DaysDiff="";
		try{
			if(Math.abs(loanAmount)<epsilon ||Math.abs(rate)<epsilon || Math.abs(tenureMonths)<epsilon){
				return "0";
			}
			BigDecimal B_intrate=  BigDecimal.valueOf(rate);
			BigDecimal B_tenure=  BigDecimal.valueOf(tenureMonths);
			BigDecimal B_loamamount= BigDecimal.valueOf(loanAmount);
			loanAmt_DaysDiff=calcgoalseekEMI(B_intrate,B_tenure,B_loamamount);

		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("RLOSCommon"+ "Exception occured in getEMI() : ");
			printException(e);
			loanAmt_DaysDiff = "0";
		}
		return loanAmt_DaysDiff;
	} */
	
	public String getEMI_new(double loanAmount,double rate,double tenureMonths, String First_emi_date)
	{      
		String EMI_val="";
		try{
			PersonalLoanS.mLogger.info( "inside getEMI_new");

			if(loanAmount < 0 ||rate < 0 ||tenureMonths < 0 ){
				return "0";
			}
			
			String Disb_date= new SimpleDateFormat("dd-MM-yyyy").format(new Date());;
			First_emi_date=Convert_dateFormat(First_emi_date,"dd/MM/yyyy","dd-MM-yyyy");
			String prop_file_loc=System.getProperty("user.dir") + File.separatorChar+"CustomConfig"+File.separatorChar+"ServerConfig.properties";
		    File file = new File(prop_file_loc);
            FileInputStream fileInput = new FileInputStream(file);
            Properties properties = new Properties();
            properties.load(fileInput);
            fileInput.close();
		
			String mqIP= properties.getProperty("mqIP");
			String mqPort_str = properties.getProperty("mqPort");
			int mqPort = Integer.parseInt(mqPort_str);
			PersonalLoanS.mLogger.info( "mqIP: "+ mqIP+ " mqPort_str: "+ mqPort_str+" mqPort: " + mqPort);
			
			
			PersonalLoanS.mLogger.info( "before makeSocketCall_EMI");
			EMI_val = makeSocketCall_EMI(loanAmount,rate,(int)Math.round(tenureMonths),Disb_date,First_emi_date,mqIP,mqPort);
			PersonalLoanS.mLogger.info( "after makeSocketCall_EMI EMI_val: "+EMI_val);
			String[] EMI_val_str = EMI_val.split("~");
			EMI_val =EMI_val_str[0];
			BigDecimal EMI_val_d = new BigDecimal(EMI_val).setScale(0, RoundingMode.CEILING);
			EMI_val=EMI_val_d.toString();
		}
		catch(Exception e)
		{
			PersonalLoanS.logException(e);
			EMI_val = "0";
		}
		return EMI_val;
	}	
	public String makeSocketCall_EMI(double loanAmount,double rate,int months,String disbursement_Date,String firstEMI_Date, String mqIP, int mqPort )
	{
			String socketParams=loanAmount+"~"+rate+"~"+months+"~"+disbursement_Date+"~"+firstEMI_Date;
			
			PersonalLoanS.mLogger.info( "inside makeSocketCall_EMI socketParams: "+socketParams);
			
			Socket socket = null;
			DataOutputStream dout=null;
			DataInputStream in = null;
			String result="";
			try {
				socket  = new Socket(mqIP, mqPort);
				dout=new DataOutputStream(socket.getOutputStream());
				if (socketParams != null && socketParams.length() > 0) 
				{		dout.write(socketParams.getBytes("UTF-16LE"));
				dout.flush();
				} else {
					notify();
				}
				socket.setSoTimeout(60*1000);
				in = new DataInputStream (new BufferedInputStream(socket.getInputStream()));
				byte[] readBuffer = new byte[3500];
				int num = in.read(readBuffer);
				if (num > 0) {
					byte[] arrayBytes = new byte[num];
					System.arraycopy(readBuffer, 0, arrayBytes, 0, num);
					result = new String(arrayBytes, "UTF-16LE");
					//System.out.println("Result:::"+result);
				}        
			} 
			catch (SocketException se) {
				se.printStackTrace();
			} 
			catch (IOException i) {	
				i.printStackTrace();
			}
			finally	
			{
				try{
					if(socket!=null)
					{
						if(!socket.isClosed()){
							socket.close();
						}
						socket=null;
					}
					if(dout!=null)
					{
						dout.close();
						dout=null;
					}
					if(in!=null)
					{
						in.close();
						in=null;
					}
				}
				catch (Exception e)
				{
				}
			}
			return result;
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Calculate EMI

	 ***********************************************************************************  */

	public  BigDecimal calcEMI(BigDecimal P, BigDecimal N, BigDecimal ROI) {
		BigDecimal emi = new BigDecimal(0) ;
		try{
			MathContext mc = MathContext.DECIMAL128;     
			BigDecimal R = ROI.divide(new BigDecimal(1200),mc);
			BigDecimal nemi1 = P.multiply(R,mc);
			BigDecimal npower1 = (BigDecimal.ONE.add(R)).pow(N.intValue(),mc);
			BigDecimal dpower1 = (BigDecimal.ONE.add(R)).pow(N.intValue(),mc);
			BigDecimal denominator = dpower1.subtract(BigDecimal.ONE);
			BigDecimal numerator = nemi1.multiply(npower1);
			emi = numerator.divide(denominator,0,RoundingMode.UP);//pcasi-999
			
			//emi = numerator.divide(denominator,0);
			

		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("RLOSCommon"+ "Exception occured in calcEMI() : ");
			printException(e);
		}
		return emi;
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Calculate goalseekEMI

	 ***********************************************************************************  */

	public  String calcgoalseekEMI(BigDecimal B_intrate,BigDecimal B_tenure,BigDecimal B_loamamount) {
		String loanAmt_DaysDiff="0";
		try{
			BigDecimal PMTEMI=calcEMI(B_loamamount,B_tenure,B_intrate);
			PMTEMI = PMTEMI.setScale(2,BigDecimal.ROUND_HALF_EVEN);

			//RLOS.mLogger.info("PMTEMI  **************"+PMTEMI);
			double seedvalue=PMTEMI.doubleValue();


			loanAmt_DaysDiff=Double.toString(seedvalue);
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("RLOSCommon"+ "Exception occured in calcgoalseekEMI() : ");
			printException(e);
			loanAmt_DaysDiff="0";
		}

		return loanAmt_DaysDiff;
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to print Exception


	 ***********************************************************************************  */


	public  static void printException(Exception e){
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String exception = sw.toString();
		PersonalLoanS.mLogger.info("Inside exception :"+"\n"+e.getMessage()+" : \n"+exception);	

	}
	public  static String returnException(Exception e){
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String exception = sw.toString();
		return exception;

	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Convert Date Format 


	 ***********************************************************************************  */

	public static String Convert_dateFormat(String date,String Old_Format,String new_Format)
	{
		PersonalLoanS.mLogger.info("RLOS Common "+ "Inside Convert_dateFormat()"+date);
		String new_date="";
		if(date==null || "".equals(date))
		{
			return new_date;
		}
		try{
			SimpleDateFormat sdf_old=new SimpleDateFormat(Old_Format);
			SimpleDateFormat sdf_new=new SimpleDateFormat(new_Format);
			new_date=sdf_new.format(sdf_old.parse(date));
		}
		catch(Exception e)
		{
			PersonalLoanS.mLogger.info("RLOS Common "+ "Exception occurred in parsing date:"+e.getMessage());
			printException(e);
		}
		return new_date;
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function For Dectech Income


	 ***********************************************************************************  */

	public void income_Dectech(){
		try{
			PersonalLoanS.mLogger.info("PL"+ "Emp Type Value is:inside income");
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();


			String EmpType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5);
			PersonalLoanS.mLogger.info("PL"+ "Emp Type Value is:"+EmpType);

			if("Salaried Pensioner".contains(EmpType))
			{
				formObject.setVisible("IncomeDetails_Frame3", false);
				formObject.setHeight("Incomedetails", 700);
				formObject.setHeight("IncomeDetails_Frame1", 720);  
			}
			/*else if(NGFUserResourceMgr_PL.getGlobalVar("PL_SelfEmployed").equalsIgnoreCase(EmpType))
			{                                                                                                              
				formObject.setVisible("IncomeDetails_Frame2", false);
				formObject.setTop("IncomeDetails_Frame3",40);
				formObject.setHeight("Incomedetails", 300);
				formObject.setHeight("IncomeDetails_Frame1", 280);
			}*/

			formObject.setVisible("IncomeDetails_Label13",false);
			formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsRakbankStat",false);
			if(!"CSM".equals(formObject.getWFActivityName())){
				formObject.setVisible("IncomeDetails_Label15",true); 
				formObject.setVisible("cmplx_IncomeDetails_Totavgother",true);
				formObject.setVisible("IncomeDetails_Label16",true);
				formObject.setVisible("cmplx_IncomeDetails_compaccAmt",true);
			}
			else{		
				formObject.setVisible("IncomeDetails_Label15",false); 
				formObject.setVisible("cmplx_IncomeDetails_Totavgother",false);
				formObject.setVisible("IncomeDetails_Label16",false);
				formObject.setVisible("cmplx_IncomeDetails_compaccAmt",false);	
			}
			if("CSM".equals(formObject.getWFActivityName()) || "CAD_Analyst1".equals(formObject.getWFActivityName())){
				formObject.setLocked("cmplx_IncomeDetails_RentalIncome",false);
				formObject.setEnabled("cmplx_IncomeDetails_RentalIncome",true);
				formObject.setLocked("cmplx_IncomeDetails_EducationalAllowance",false);
				formObject.setEnabled("cmplx_IncomeDetails_EducationalAllowance",true);
			}
			formObject.setLocked("cmplx_IncomeDetails_grossSal",true);
			formObject.setLocked("cmplx_IncomeDetails_Overtime_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_Commission_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_FoodAllow_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_PhoneAllow_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_serviceAllow_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_Bonus_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_Other_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_Flying_Avg",true);
			formObject.setLocked("cmplx_IncomeDetails_totSal",true);
			formObject.setLocked("cmplx_IncomeDetails_AvgNetSal",true);
			//Code added by aman to load the picklist at CAD
			if(NGFUserResourceMgr_PL.getGlobalVar("PL_false").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")))
			{
				formObject.setVisible("cmplx_IncomeDetails_DurationOfBanking",true);
				formObject.setVisible("cmplx_IncomeDetails_NoOfMonthsOtherbankStat",true);
				formObject.setVisible("IncomeDetails_Label11",true);
				formObject.setVisible("IncomeDetails_Label3",true);
			}
			formObject.setVisible("IncomeDetails_Frame3",false);
		}
		catch(Exception e){
			System.out.println("Exceptiion in income"+e.getMessage());
			PersonalLoanS.mLogger.info("PL"+ "Emp Type Value is:inside income"+e.getMessage());
		}	
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function for Dectech Employment


	 ***********************************************************************************  */

	public void employment_dectech(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		if(formObject.getNGValue("cmplx_EmploymentDetails_IncInCC")=="true" || formObject.getNGValue("cmplx_EmploymentDetails_IncInPL")=="true")
		{
			formObject.setLocked("cmplx_EmploymentDetails_EmpStatusPL",true);
			formObject.setLocked("cmplx_EmploymentDetails_EmpStatusCC",true);
			formObject.setLocked("cmplx_EmploymentDetails_CurrEmployer",true);
			formObject.setLocked("cmplx_EmploymentDetails_EmpIndusSector",true);
			formObject.setLocked("cmplx_EmploymentDetails_Indus_Macro",true);
			formObject.setLocked("cmplx_EmploymentDetails_Indus_Micro",true);
			formObject.setLocked("cmplx_EmploymentDetails_categnational",true);
			formObject.setLocked("cmplx_EmploymentDetails_categexpat",true);
			formObject.setLocked("cmplx_EmploymentDetails_ownername",true);
			formObject.setLocked("cmplx_EmploymentDetails_NOB",true);
			formObject.setLocked("cmplx_EmploymentDetails_LOSPrevious",true);
			formObject.setLocked("cmplx_EmploymentDetails_accpvded",true);
			String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
			
			if("DDVT_maker".equalsIgnoreCase(sActivityName)||"CAD_Analyst1".equalsIgnoreCase(sActivityName))
				
			formObject.setLocked("cmplx_EmploymentDetails_authsigname",false);
			formObject.setLocked("cmplx_EmploymentDetails_highdelinq",true);
			formObject.setLocked("cmplx_EmploymentDetails_dateinPL",true);
			formObject.setLocked("cmplx_EmploymentDetails_dateinCC",true);
			formObject.setLocked("cmplx_EmploymentDetails_remarks",true);
			formObject.setLocked("cmplx_EmploymentDetails_Remarks_PL",true);
			formObject.setEnabled("cmplx_EmploymentDetails_Remarks_PL",false);
			formObject.setEnabled("cmplx_EmploymentDetails_remarks",false);



			formObject.setLocked("cmplx_EmploymentDetails_EmpStatus",false);
			formObject.setLocked("cmplx_EmploymentDetails_Emp_Type",false);
			formObject.setLocked("cmplx_EmploymentDetails_Designation",false);
			formObject.setLocked("cmplx_EmploymentDetails_DesigVisa",false);
			formObject.setLocked("DesignationAsPerVisa_button",false);
			formObject.setLocked("EMploymentDetails_Designation_button",false);//corected button id


			formObject.setLocked("cmplx_EmploymentDetails_EmpContractType",false);
			formObject.setLocked("cmplx_EmploymentDetails_VisaSponser",false);

			formObject.setLocked("cmplx_EmploymentDetails_Freezone",false);
			formObject.setLocked("cmplx_EmploymentDetails_FreezoneName",false);
			formObject.setLocked("cmplx_EmploymentDetails_IndusSeg",false);
			formObject.setLocked("cmplx_EmploymentDetails_Kompass",false);
			formObject.setLocked("cmplx_EmploymentDetails_StaffID",false);
			formObject.setLocked("cmplx_EmploymentDetails_Dept",false);
			formObject.setLocked("cmplx_EmploymentDetails_CntrctExpDate",false);
			formObject.setVisible("cmplx_EmploymentDetails_TL_Number", false);
			formObject.setVisible("EMploymentDetails_Label63", false);
			formObject.setVisible("EMploymentDetails_Label65", false);
			formObject.setVisible("cmplx_EmploymentDetails_TL_Emirate", false);
		}
		if((formObject.getNGValue("cmplx_EmploymentDetails_IncInCC")=="false") && (formObject.getNGValue("cmplx_EmploymentDetails_IncInPL")=="false"))
		{
			formObject.setVisible("cmplx_EmploymentDetails_TL_Number", true);
			formObject.setVisible("EMploymentDetails_Label63", true);
			formObject.setVisible("EMploymentDetails_Label65", true);
			formObject.setVisible("cmplx_EmploymentDetails_TL_Emirate", true);
			formObject.setNGValue("cmplx_EmploymentDetails_EmpStatusPL","CNOAL");
			formObject.setNGValue("cmplx_EmploymentDetails_EmpStatusCC","CNOAL ( disabled) /Awaiting FVR");
			formObject.setLocked("cmplx_EmploymentDetails_categnational",true);
			formObject.setLocked("cmplx_EmploymentDetails_categexpat",true);
			formObject.setLocked("cmplx_EmploymentDetails_ownername",true);
			formObject.setLocked("cmplx_EmploymentDetails_NOB",true);
			formObject.setLocked("cmplx_EmploymentDetails_accpvded",true);
			String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
			
			if("DDVT_maker".equalsIgnoreCase(sActivityName)||"CAD_Analyst1".equalsIgnoreCase(sActivityName))
			formObject.setLocked("cmplx_EmploymentDetails_authsigname",false);
			formObject.setLocked("cmplx_EmploymentDetails_highdelinq",true);
			formObject.setLocked("cmplx_EmploymentDetails_dateinPL",true);
			formObject.setLocked("cmplx_EmploymentDetails_dateinCC",true);
			formObject.setLocked("cmplx_EmploymentDetails_remarks",true);
			formObject.setLocked("cmplx_EmploymentDetails_Remarks_PL",true);
			formObject.setEnabled("cmplx_EmploymentDetails_Remarks_PL",false);
			formObject.setEnabled("cmplx_EmploymentDetails_remarks",false);



			formObject.setLocked("cmplx_EmploymentDetails_EmpIndusSector",false);
			formObject.setLocked("cmplx_EmploymentDetails_Indus_Macro",false);
			formObject.setLocked("cmplx_EmploymentDetails_Indus_Micro",false);
			formObject.setLocked("cmplx_EmploymentDetails_EmpContractType",false);
			formObject.setLocked("cmplx_EmploymentDetails_Emp_Type",false);
			formObject.setLocked("cmplx_EmploymentDetails_Designation",false);
			formObject.setLocked("cmplx_EmploymentDetails_DesigVisa",false);
			formObject.setLocked("DesignationAsPerVisa_button",false);
			formObject.setLocked("EMploymentDetails_Designation_button",false);

			formObject.setLocked("cmplx_EmploymentDetails_EmirateOfWork",false);
			formObject.setLocked("cmplx_EmploymentDetails_HeadOfficeEmirate",false);
			formObject.setLocked("cmplx_EmploymentDetails_VisaSponser",false);
			formObject.setLocked("cmplx_EmploymentDetails_Freezone",false);
			formObject.setLocked("cmplx_EmploymentDetails_FreezoneName",false);
			formObject.setLocked("cmplx_EmploymentDetails_IndusSeg",false);
			formObject.setLocked("cmplx_EmploymentDetails_Kompass",false);
			formObject.setLocked("cmplx_EmploymentDetails_StaffID",false);
			formObject.setLocked("cmplx_EmploymentDetails_Dept",false);
			formObject.setLocked("cmplx_EmploymentDetails_CntrctExpDate",false);
		}
		loadPicklist4();

	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to HIde/Unhide Decision Fields


	 ***********************************************************************************  */

	public void decisionLabelsVisibility()	
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();


		formObject.setVisible("cmplx_Decision_VERIFICATIONREQUIRED",false);
		formObject.setVisible("Decision_Label1",false);
		formObject.setVisible("DecisionHistory_Label1",false);
		formObject.setVisible("cmplx_Decision_ReferTo", false); //Arun (01-12-17) to hide this combo
		formObject.setVisible("cmplx_Decision_refereason",false);
		formObject.setVisible("cmplx_Decision_IBAN",false);
		formObject.setVisible("DecisionHistory_Label6",false);
		formObject.setVisible("cmplx_Decision_AccountNo",false);
		formObject.setVisible("DecisionHistory_Label7",false);
		formObject.setVisible("cmplx_Decision_ChequeBookNumber",false);
		formObject.setVisible("DecisionHistory_Label8",false);
		formObject.setVisible("cmplx_Decision_DebitcardNumber",false);
		formObject.setVisible("DecisionHistory_Label9",false);
		formObject.setVisible("cmplx_Decision_desc",false);
		formObject.setVisible("DecisionHistory_Label5",false);
		formObject.setVisible("cmplx_Decision_strength",false);
		formObject.setVisible("DecisionHistory_Label3",false);
		formObject.setVisible("cmplx_Decision_weakness",false);
		formObject.setVisible("DecisionHistory_Label4",false);
		formObject.setVisible("cmplx_Decision_REMARKS",false);
		formObject.setVisible("DecisionHistory_chqbook",false);

		formObject.setVisible("DecisionHistory_Label2", false);
		formObject.setVisible("cmplx_Decision_Status", false);
	}
	//below code added by nikhil 11/11/17
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha             
	Description                         : Notepad Load      

	 ***********************************************************************************  */
	public void notepad_load(){
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
			Date date = new Date();
			String modifiedDate= new SimpleDateFormat("dd/MM/yyyy").format(date);
			PersonalLoanS.mLogger.info( "Activity name is:" + sActivityName);
			//formObject.setNGValue("NotepadDetails_Actusername",formObject.getUserId()+"-"+formObject.getUserName());
			formObject.setNGValue("NotepadDetails_user",formObject.getUserId()+"-"+formObject.getUserName());
			formObject.setNGValue("NotepadDetails_noteDate",modifiedDate);
			//formObject.setNGValue("NotepadDetails_Actdate",modifiedDate);
			formObject.setNGValue("NotepadDetails_insqueue",sActivityName);
			formObject.setLocked("NotepadDetails_noteDate",true);
			formObject.setLocked("NotepadDetails_Actusername",true);
			formObject.setLocked("NotepadDetails_user",true);
			formObject.setLocked("NotepadDetails_insqueue",true);
			formObject.setLocked("NotepadDetails_Actdate",true);
			formObject.setEnabled("NotepadDetails_inscompletion",false);
			formObject.setLocked("NotepadDetails_ActuserRemarks",true);
			//formObject.setTop("NotepadDetails_save",200); commented by shweta 
		}
		catch(Exception ex){
			PersonalLoanS.mLogger.info("Exception in notepad load is :");
			printException(ex);
		}


	}
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha             
	Description                         : Notepad Addition     

	 ***********************************************************************************  */

	public void Notepad_add(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");

		PersonalLoanS.mLogger.info(""+formObject.getUserId()+"-"+formObject.getUserName());

		formObject.setNGValue("NotepadDetails_user",formObject.getUserId()+"-"+formObject.getUserName(),false);

		formObject.setNGValue("NotepadDetails_insqueue",sActivityName,false);
		// added by abhishek as per CC FSD
		Date date = new Date();
		String modifiedDate1= new SimpleDateFormat("dd/MM/yyyy").format(date);
		String modifiedDate2= new SimpleDateFormat("dd/MM/yyyy HH:mm").format(date);

		formObject.setNGValue("NotepadDetails_noteDate",modifiedDate1,false);

		int rowCount = formObject.getLVWRowCount("cmplx_NotepadDetails_cmplx_notegrid");
		PersonalLoanS.mLogger.info( "Notepad_add rowCount is:" + rowCount);
		//String time = date.getHours()+":"+date.getMinutes();
		PersonalLoanS.mLogger.info( "Notepad_add value to set is:" +  (modifiedDate2));
		formObject.setNGValue("cmplx_NotepadDetails_cmplx_notegrid",rowCount-1,4, (modifiedDate2));

		// to make frame in Add state
		formObject.setLocked("NotepadDetails_noteDate",true);
		formObject.setLocked("NotepadDetails_Actusername",true);
		formObject.setLocked("NotepadDetails_user",true);
		formObject.setLocked("NotepadDetails_insqueue",true);
		formObject.setLocked("NotepadDetails_Actdate",true);
		formObject.setLocked("NotepadDetails_Actdate",true);
		formObject.setEnabled("NotepadDetails_inscompletion",false);
		formObject.setLocked("NotepadDetails_ActuserRemarks",true);

		formObject.setLocked("NotepadDetails_notedesc",false);
		//formObject.setLocked("NotepadDetails_notecode",true);
		formObject.setLocked("NotepadDetails_notedetails",false);
		formObject.setNGValue("NotepadDetails_notedesc", "--Select--",false);
		PersonalLoanS.mLogger.info( "Notepad_add value to set is:" + formObject.getWFActivityName());
		/*if (formObject.getWFActivityName().equalsIgnoreCase("Disbursal_Maker") || formObject.getWFActivityName().equalsIgnoreCase("Disbursal_Checker")){
			formObject.setNGValue("IS_Stage_Reversal", "Y");
		}
		PersonalLoanS.mLogger.info( "Notepad_add value to set is:" + formObject.getNGValue("IS_Stage_Reversal"));
	*/}
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha             
	Description                         : Notepad Modify      

	 ***********************************************************************************  */
	
	//changed by Shweta for PCSP-701
	public void Notepad_modify(int rowindex,String Notedate){
		// added by abhishek as per CC FSD
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
		Date date = new Date();
		String modifiedDate1= new SimpleDateFormat("dd/MM/yyyy").format(date);
		String modifiedDate2= new SimpleDateFormat("dd/MM/yyyy HH:mm").format(date);

		PersonalLoanS.mLogger.info( "Activity name is:" + sActivityName);

		//String time = date.getHours()+":"+date.getMinutes();
		//below code done by nikhil for PCSP-701
		formObject.setNGValue("cmplx_NotepadDetails_cmplx_notegrid",rowindex,4, Notedate);

		formObject.setNGValue("NotepadDetails_user",formObject.getUserId()+"-"+formObject.getUserName(),false);
		formObject.setNGValue("NotepadDetails_noteDate",modifiedDate1,false);

		formObject.setNGValue("NotepadDetails_insqueue",sActivityName,false);

		formObject.setLocked("NotepadDetails_noteDate",true);
		formObject.setLocked("NotepadDetails_Actusername",true);
		formObject.setLocked("NotepadDetails_user",true);
		formObject.setLocked("NotepadDetails_insqueue",true);
		formObject.setLocked("NotepadDetails_Actdate",true);
		formObject.setLocked("NotepadDetails_Actdate",true);
		formObject.setEnabled("NotepadDetails_inscompletion",false);
		formObject.setLocked("NotepadDetails_ActuserRemarks",true);

		formObject.setLocked("NotepadDetails_notedesc",false);
		//formObject.setLocked("NotepadDetails_notecode",true);
		formObject.setLocked("NotepadDetails_notedetails",false);
		//below code added by abhishek on 10/11/2017
		formObject.setEnabled("NotepadDetails_Add", true);
		formObject.setEnabled("NotepadDetails_Delete", true);
		formObject.setNGValue("NotepadDetails_notedesc", "--Select--",false);
		//String gridValue = formObject.getNGValue("cmplx_NotepadDetails_cmplx_notegrid",rowindex, 5);
		String targetWorkstep =  formObject.getNGValue("cmplx_NotepadDetails_cmplx_notegrid",rowindex, 11);
		if(targetWorkstep.equalsIgnoreCase(formObject.getWFActivityName()))
		{
			formObject.setNGValue("cmplx_NotepadDetails_cmplx_notegrid",rowindex,7, modifiedDate2);
			formObject.setNGValue("NotepadDetails_Actdate",modifiedDate1,false);
		}
		PersonalLoanS.mLogger.info( "Notepad_add value to set is:" + formObject.getWFActivityName());
		/*if (formObject.getWFActivityName().equalsIgnoreCase("Disbursal_Maker") || formObject.getWFActivityName().equalsIgnoreCase("Disbursal_Checker")){
			formObject.setNGValue("IS_Stage_Reversal", "Y");
		}
		PersonalLoanS.mLogger.info( "Notepad_add value to set is:" + formObject.getNGValue("IS_Stage_Reversal"));
	*/
		//--Above code added by abhishek on 10/11/2017
	}
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha             
	Description                         : Notepad Grid        

	 ***********************************************************************************  */
	public void Notepad_grid(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
		PersonalLoanS.mLogger.info("Inside notepad grid function");
		Date date = new Date();
		String modifiedDate= new SimpleDateFormat("dd/MM/yyyy").format(date);
		//++below code added by abhishek on 10/11/2017
		//below code changed by nikhil system hang 6/12/18
		String gridValue="";
		String targetWorkstep ="";
		if(formObject.getSelectedIndex("cmplx_NotepadDetails_cmplx_notegrid")!=-1) {
			//Bandana: below column number changed from 5 to 6 for PCASI-3494
			gridValue = formObject.getNGValue("cmplx_NotepadDetails_cmplx_notegrid", formObject.getSelectedIndex("cmplx_NotepadDetails_cmplx_notegrid"), 6);
			//Bandana: below column number changed from 11 to 12 for PCASI-3494
			targetWorkstep =  formObject.getNGValue("cmplx_NotepadDetails_cmplx_notegrid", formObject.getSelectedIndex("cmplx_NotepadDetails_cmplx_notegrid"), 12);
			PersonalLoanS.mLogger.info("gridValue is: "+gridValue);
			PersonalLoanS.mLogger.info("sActivityName is: "+sActivityName);
			PersonalLoanS.mLogger.info("targetWorkstep is: "+targetWorkstep);
		}
		
		if(formObject.getSelectedIndex("cmplx_NotepadDetails_cmplx_notegrid")==-1)
		{
			formObject.setNGValue("NotepadDetails_noteDate",modifiedDate,false);
			PersonalLoanS.mLogger.info("Inside notepad grid function condition 3");
			//formObject.setEnabled("NotepadDetails_Add", true);
			formObject.setEnabled("NotepadDetails_Delete", true);
			formObject.setEnabled("NotepadDetails_Modify", true);
			formObject.setLocked("NotepadDetails_notedesc", false);
			formObject.setEnabled("NotepadDetails_notedetails", true);
			formObject.setNGValue("NotepadDetails_user",formObject.getUserId()+"-"+formObject.getUserName(),false);
			formObject.setNGValue("NotepadDetails_insqueue",sActivityName,false);
			//String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
			//Date date = new Date();
			//String modifiedDate= new SimpleDateFormat("dd/MM/yyyy").format(date);
			//formObject.setNGValue("NotepadDetails_Actusername",formObject.getUserId()+"-"+formObject.getUserName());
			formObject.setNGValue("NotepadDetails_user",formObject.getUserId()+"-"+formObject.getUserName());
			//formObject.setNGValue("NotepadDetails_noteDate",modifiedDate);
			//formObject.setNGValue("NotepadDetails_Actdate",modifiedDate);
			formObject.setNGValue("NotepadDetails_insqueue",sActivityName);

		} 
		else {
			if(sActivityName.equalsIgnoreCase(gridValue)){
				PersonalLoanS.mLogger.info("Inside notepad grid function condition 1");
				//formObject.setEnabled("NotepadDetails_Add", false);
				formObject.setEnabled("NotepadDetails_Delete", true);
				formObject.setLocked("NotepadDetails_notedesc", false);
				formObject.setEnabled("NotepadDetails_notedetails", true);
				formObject.setEnabled("NotepadDetails_inscompletion",false);
				formObject.setLocked("NotepadDetails_ActuserRemarks",true);			
			}
			//added by akshay on 15/1/18 for proc 3450
			else if(sActivityName.equalsIgnoreCase(targetWorkstep)){
				
				PersonalLoanS.mLogger.info("Inside notepad grid function condition 2");
				//formObject.setEnabled("NotepadDetails_Add", false);
				formObject.setEnabled("NotepadDetails_Modify", true);
				formObject.setEnabled("NotepadDetails_Delete", false);
				formObject.setLocked("NotepadDetails_notedesc", true);
				formObject.setEnabled("NotepadDetails_notedetails", true);
				//formObject.setEnabled("NotepadDetails_inscompletion",true);
				formObject.setLocked("NotepadDetails_ActuserRemarks",false);
				formObject.setNGValue("NotepadDetails_Actusername",formObject.getUserId()+"-"+formObject.getUserName(),false);
				//CreditCard.mLogger.info("user ID & name Deepak: "+formObject.getUserId()+"-"+formObject.getUserName());
				formObject.setNGValue("NotepadDetails_Actdate",modifiedDate,false);
				formObject.setEnabled("NotepadDetails_inscompletion",true);
				//formObject.setEnabled("NotepadDetails_inscompletion",true);
				//formObject.setLocked("NotepadDetails_inscompletion", false);	
			}
			else{
				PersonalLoanS.mLogger.info("Inside notepad grid function condition 4");
				//formObject.setEnabled("NotepadDetails_Add", false);
				formObject.setEnabled("NotepadDetails_Delete", false);
				formObject.setEnabled("NotepadDetails_Modify", false);
				formObject.setLocked("NotepadDetails_notedesc", false);
				formObject.setEnabled("NotepadDetails_notedetails", false);
				//formObject.setNGValue("NotepadDetails_insqueue",sActivityName,false);
				formObject.setEnabled("NotepadDetails_inscompletion",false);
				formObject.setLocked("NotepadDetails_ActuserRemarks",true);				
			}
		//ended by akshay on 15/1/18 for proc 3450
		
		}
		//formObject.setLocked("NotepadDetails_notecode",true);

		formObject.setLocked("NotepadDetails_noteDate",true);
		formObject.setLocked("NotepadDetails_Actusername",true);
		formObject.setLocked("NotepadDetails_user",true);
		formObject.setLocked("NotepadDetails_insqueue",true);
		formObject.setLocked("NotepadDetails_Actdate",true);

		//--Above code added by abhishek on 10/11/2017
	}
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha             
	Description                         : Notepad Delete

	 ***********************************************************************************  */
	public void Notepad_delete(){
		// added by abhishek as per CC FSD
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
		Date date = new Date();
		String modifiedDate= new SimpleDateFormat("dd/MM/yyyy").format(date);
		PersonalLoanS.mLogger.info( "Activity name is:" + sActivityName);

		formObject.setNGValue("NotepadDetails_user",formObject.getUserId()+"-"+formObject.getUserName(),false);


		formObject.setNGValue("NotepadDetails_insqueue",sActivityName,false);
		formObject.setNGValue("NotepadDetails_noteDate",modifiedDate,false);


		formObject.setLocked("NotepadDetails_noteDate",true);
		formObject.setLocked("NotepadDetails_Actusername",true);
		formObject.setLocked("NotepadDetails_user",true);
		formObject.setLocked("NotepadDetails_insqueue",true);
		formObject.setLocked("NotepadDetails_Actdate",true);
		formObject.setLocked("NotepadDetails_Actdate",true);
		formObject.setEnabled("NotepadDetails_inscompletion",false);
		formObject.setLocked("NotepadDetails_ActuserRemarks",true);

		formObject.setLocked("NotepadDetails_notedesc",false);
		//formObject.setLocked("NotepadDetails_notecode",true);
		formObject.setLocked("NotepadDetails_notedetails",false);
		
		formObject.setEnabled("NotepadDetails_Add", true);
		formObject.setEnabled("NotepadDetails_Delete", true);
		formObject.setNGValue("NotepadDetails_notedesc", "--Select--",false);
	}
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha             
	Description                         : Notepad Details without telloging frame        

	 ***********************************************************************************  */
	public void notepad_withoutTelLog(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setVisible("NotepadDetails_Frame3", false);
		formObject.setTop("NotepadDetails_save", formObject.getHeight("NotepadDetails_Frame2")+50);
		formObject.setHeight("NotepadDetails_Frame1", formObject.getTop("NotepadDetails_save")+100);
		formObject.setHeight("Notepad_Values", formObject.getHeight("NotepadDetails_Frame1")+5);
		formObject.setTop("DecisionHistory", formObject.getHeight("Notepad_Values")+20);

	}
	//--above code added by nikhil 11/11/17
	//++Below code added by nikhil 13/11/2017 for Code merge

	public void enable_busiVerification(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();





		formObject.fetchFragment("CompDetails", "CompanyDetails", "q_CompanyDetails");

		formObject.setNGValue("cmplx_BussVerification_TradeLicName",formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",0,6));
		formObject.setNGValue("cmplx_BussVerification_signame",formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",0,0));


		// disable fields

		formObject.setEnabled("cmplx_BussVerification_TradeLicName",false);
		formObject.setEnabled("cmplx_BussVerification_signame",false);




	}

	public void enable_homeVerification(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
		formObject.setNGFrameState("Alt_Contact_container",0);
		//below id changed by nikhil 10/12/17
		formObject.setNGValue("cmplx_HCountryVerification_hcountrytel",formObject.getNGValue("AlternateContactDetails_HOMECOUNTRYNO"));

		formObject.setEnabled("cmplx_HCountryVerification_Hcountrytelverified",true);
		//formObject.setEnabled("cmplx_HCountryVerification_hcountrytel",false);
		formObject.setEnabled("cmplx_HCountryVerification_personcontctd",true);
		formObject.setEnabled("cmplx_HCountryVerification_Relwithpersoncntcted",true);
		formObject.setEnabled("cmplx_HCountryVerification_Remarks",true);
		formObject.setEnabled("cmplx_HCountryVerification_Decision",true);

	}

	public void enable_ResVerification(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
		formObject.setNGFrameState("Alt_Contact_container",0);
		formObject.setNGValue("cmplx_ResiVerification_cntcttelno",formObject.getNGValue("AlternateContactDetails_RESIDENCENO"));

		formObject.setEnabled("cmplx_ResiVerification_Telnoverified",true);
		formObject.setEnabled("cmplx_ResiVerification_cntcttelno",false);
		formObject.setEnabled("cmplx_ResiVerification_Remarks",true);
		formObject.setEnabled("cmplx_ResiVerification_Decision",true);

	}


	public void enable_ReferenceVerification(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.fetchFragment("ReferenceDetails", "ReferenceDetails", "q_ReferenceDetails");
		//below code added by nikhil 10/12/17
		if(formObject.getLVWRowCount("cmplx_ReferenceDetails_cmplx_ReferenceGrid")>0)
		{
			//below id changed by nikhil 10/12/17
			formObject.setNGValue("cmplx_RefDetVerification_ref1cntctno",formObject.getNGValue("cmplx_ReferenceDetails_cmplx_ReferenceGrid",0,1));
			formObject.setNGValue("cmplx_RefDetVerification_ref1name",formObject.getNGValue("cmplx_ReferenceDetails_cmplx_ReferenceGrid",0,0));
			formObject.setNGValue("cmplx_RefDetVerification_ref1phone",formObject.getNGValue("cmplx_ReferenceDetails_cmplx_ReferenceGrid",0,2));
		}
		formObject.setLocked("cmplx_RefDetVerification_ref1cntctno",true);
		formObject.setLocked("cmplx_RefDetVerification_ref1name",true);
		formObject.setLocked("cmplx_RefDetVerification_ref1phone",true);



	}
	public void enable_officeVerification(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
	//by shweta
		int framestate2=formObject.getNGFrameState("EmploymentDetails");
		if(framestate2 == 0){
			PersonalLoanS.mLogger.info("EmploymentDetails");
		}
		else {
			
			PersonalLoanS.mLogger.info("fetched employment details");
			//below code added by nikhil for PCSP-22
			loadEmployment();
			
		}
		int framestateIncome=formObject.getNGFrameState("IncomeDEtails");
		if(framestateIncome == 0){
			PersonalLoanS.mLogger.info("IncomeDEtails already fetched");
		}
		else {
			formObject.fetchFragment("IncomeDEtails", "IncomeDetails", "q_IncomeDetails");
			expandIncome();
			PersonalLoanS.mLogger.info("fetched IncomeDEtails details");
		}
		
		formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
		formObject.setNGFrameState("Alt_Contact_container",0);

		if(formObject.getNGValue("cmplx_OffVerification_offtelno").equalsIgnoreCase("") || formObject.getNGValue("cmplx_OffVerification_offtelno")==null){
			formObject.setNGValue("cmplx_OffVerification_offtelno",formObject.getNGValue("AlternateContactDetails_OFFICENO"));
		}
		if("".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_fxdsal_val")))
		{
		formObject.setNGValue("cmplx_OffVerification_fxdsal_val",formObject.getNGValue("cmplx_IncomeDetails_grossSal"));
		}
		//formObject.setNGValue("cmplx_OffVerification_fxdsal_val",formObject.getNGValue("cmplx_IncomeDetails_AvgNetSal"));
		if("".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_accprovd_val")))
		{
		formObject.setNGValue("cmplx_OffVerification_accprovd_val",formObject.getNGValue("cmplx_IncomeDetails_Accomodation"));
		}
		//formObject.setNGValue("cmplx_OffVerification_accprovd_val",formObject.getNGValue("cmplx_IncomeDetails_Accomodation"));
		//below code commented
		//formObject.setNGValue("cmplx_OffVerification_desig_val",formObject.getNGSelectedItemText("cmplx_EmploymentDetails_Designation"));
		if("".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_doj_val")))
		{
		formObject.setNGValue("cmplx_OffVerification_doj_val",formObject.getNGValue("cmplx_EmploymentDetails_DOJ"));
		}
		//formObject.setNGValue("cmplx_OffVerification_cnfrminjob_val",formObject.getNGValue("cmplx_EmploymentDetails_JobConfirmed"));
		String desig=formObject.getNGValue("cmplx_EmploymentDetails_Designation");
		//below code added by nikhil
		try
		{
			//below query changed by nikhil for cpv issued on 19/12/17
			List<List<String>> db=formObject.getNGDataFromDataCache("select Description from NG_MASTER_Designation with (nolock)  where Code='"+desig+"'") ;
			if(db!=null && db.get(0)!=null && db.get(0).get(0)!=null){
				desig=db.get(0).get(0); 
				PersonalLoanS.mLogger.info("fcu @ emirate"+desig);
			}
		}
		catch(Exception ex)
		{
			desig="";
		}
		if("".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_desig_val")))
		{
		formObject.setNGValue("cmplx_OffVerification_desig_val",desig);
		}
		formObject.setEnabled("cmplx_OffVerification_offtelno",false);
		formObject.setEnabled("cmplx_OffVerification_fxdsal_val",false);
		formObject.setEnabled("cmplx_OffVerification_accprovd_val",false);
		formObject.setEnabled("cmplx_OffVerification_desig_val",false);
		formObject.setEnabled("cmplx_OffVerification_doj_val",false);
		formObject.setVisible("EMploymentDetails_Label45", true);
		formObject.setEnabled("cmplx_OffVerification_cnfrminjob_val",false);




	}
	public void enable_loanCard(){


		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		if(NGFUserResourceMgr_PL.getGlobalVar("PL_IM").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2)) || 
				NGFUserResourceMgr_PL.getGlobalVar("PL_InstantMoney").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2)))
		{

			formObject.setEnabled("cmplx_LoanandCard_loanamt_val",false);
			formObject.setEnabled("cmplx_LoanandCard_tenor_val",false);
			formObject.setEnabled("cmplx_LoanandCard_emi_val",false);
			formObject.setEnabled("cmplx_LoanandCard_islorconv_val",false);
			formObject.setEnabled("cmplx_LoanandCard_firstrepaydate_val",false);
			// hide
			formObject.setVisible("LoanandCard_Label6", false);
			formObject.setVisible("cmplx_LoanandCard_firstrepaydate_val", false);
			formObject.setVisible("cmplx_LoanandCard_firstrepaydate_ver", false);
			formObject.setVisible("cmplx_LoanandCard_firstrepaydate_upd", false);

			formObject.setVisible("LoanandCard_Label17", false);
			formObject.setTop("LoanandCard_Label18",8);
			formObject.setVisible("LoanandCard_Label10", false);
			formObject.setVisible("LoanandCard_Label13", false);
			formObject.setVisible("LoanandCard_Label14", false);
			formObject.setVisible("LoanandCard_Label15", false);

			formObject.setVisible("LoanandCard_Label11", false);
			formObject.setVisible("cmplx_LoanandCard_cardtype_val", false);
			formObject.setVisible("cmplx_LoanandCard_cardtype_ver", false);
			formObject.setVisible("cmplx_LoanandCard_cardtype_upd", false);

			formObject.setVisible("LoanandCard_Label12", false);
			formObject.setVisible("cmplx_LoanandCard_cardlimit_val", false);
			formObject.setVisible("cmplx_LoanandCard_cardlimit_ver", false);
			formObject.setVisible("cmplx_LoanandCard_cardlimit_upd", false);

			//++Below code added by nikhil 13/11/2017 for Code merge
			formObject.setLeft("cmplx_LoanandCard_tenor_ver", 555);
			formObject.setLeft("cmplx_LoanandCard_cardtype_ver", 300);
			formObject.setLeft("cmplx_LoanandCard_cardlimit_ver", 300);
			formObject.setLeft("cmplx_LoanandCard_firstrepaydate_ver", 300);
			formObject.setTop("cmplx_LoanandCard_firstrepaydate_ver", formObject.getTop("cmplx_LoanandCard_cardlimit_ver"));

			formObject.setTop("LoanandCard_Label16", 278);
			formObject.setTop("cmplx_LoanandCard_remarks", 278);
			formObject.setHeight("cmplx_LoanandCard_remarks", 56);

			//--Above code added by nikhil 13/11/2017 for Code merge
		}
		else {		
			formObject.setEnabled("cmplx_LoanandCard_loanamt_val",false);
			formObject.setEnabled("cmplx_LoanandCard_tenor_val",false);
			formObject.setEnabled("cmplx_LoanandCard_emi_val",false);
			formObject.setEnabled("cmplx_LoanandCard_islorconv_val",false);
			formObject.setEnabled("cmplx_LoanandCard_firstrepaydate_val",false);
			formObject.setEnabled("cmplx_LoanandCard_cardtype_val",false);
			formObject.setEnabled("cmplx_LoanandCard_cardlimit_val",false);
			
			/* changes done by shweta to hide credit card fields from Loan/card details fragment start*/
			formObject.setVisible("LoanandCard_Label18", false);
			formObject.setVisible("LoanandCard_Label10", false);
			formObject.setVisible("LoanandCard_Label13", false);
			formObject.setVisible("LoanandCard_Label14", false);
			formObject.setVisible("LoanandCard_Label15", false);
			
			formObject.setVisible("LoanandCard_Label11", false);
			formObject.setVisible("cmplx_LoanandCard_cardtype_val",false);
			formObject.setVisible("cmplx_LoanandCard_cardtype_ver", false);
			formObject.setVisible("cmplx_LoanandCard_cardtype_upd", false);
			
			formObject.setVisible("LoanandCard_Label12", false);
			formObject.setVisible("cmplx_LoanandCard_cardlimit_val",false);
			formObject.setVisible("cmplx_LoanandCard_cardlimit_ver", false);
			formObject.setVisible("cmplx_LoanandCard_cardlimit_upd", false);
			/* changes done by shweta to hide credit card fields from Loan/card details fragment end*/			
			//++Below code added by nikhil 13/11/2017 for Code merge

			formObject.setTop("LoanandCard_Label16", 278);
			formObject.setTop("cmplx_LoanandCard_remarks", 278);
			formObject.setHeight("cmplx_LoanandCard_remarks", 56);
			
			formObject.setTop("LoanandCard_Label19", 350);
			formObject.setTop("cmplx_LoanandCard_Decision", 370);
			formObject.setHeight("cmplx_LoanandCard_Decision", 25);
			formObject.setTop("LoanandCard_save", 420);
			formObject.setHeight("cmplx_LoanandCard_Decision", 25);

			//--Above code added by nikhil 13/11/2017 for Code merge
			}

	}
	
		
	public void loadInComplianceGrid()
	{

		// FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		PersonalLoanS.mLogger.info("CC_Common"+"Query is loadInWorldGrid ");
		//Below code modified by nikhil 07/12/17
		LoadPickList("Compliance_BirthCountry","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_Country with (nolock)  where isActive='Y' order by code");
		LoadPickList("Compliance_ResidenceCountry","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_Country with (nolock)  where isActive='Y' order by code");

	}
	public void openFrags(FormReference formObject){

		int framestate0=formObject.getNGFrameState("Address_Details_container");
		if(framestate0 != 0){
			formObject.fetchFragment("Address_Details_container", "AddressDetails", "q_AddressDetails");
			formObject.setNGFrameState("Address_Details_container",0);

		}
		int framestate1=formObject.getNGFrameState("Alt_Contact_container");
		if(framestate1 != 0){
			formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
			formObject.setNGFrameState("Alt_Contact_container",0);

		}
		int framestate4=formObject.getNGFrameState("CustomerDetails");
		if(framestate4 != 0)
		{
			formObject.fetchFragment("CustomerDetails", "Customer", "q_Customer");
			formObject.setNGFrameState("CustomerDetails",0);

		}
		int framestate2=formObject.getNGFrameState("EmploymentDetails");
		PersonalLoanS.mLogger.info("framestate for Employment is: "+framestate2);
		if(framestate2 == 0){
			PersonalLoanS.mLogger.info("EmploymentDetails");
		}
		else {
			formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmpDetails");
			formObject.setNGFrameState("EmploymentDetails",0);
			PersonalLoanS.mLogger.info("fetched employment details");
		}

	}
	//function by saurabh on 10th dec
	public void openDemographicTabs(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//below code added by Arun (27/11/17) to fetch the fragments when Decision fragment is fetched.
		int framestate3=formObject.getNGFrameState("Address_Details_container");
		if(framestate3 == 0){
			PersonalLoanS.mLogger.info("Address_Details_container");
		}
		else {
			formObject.fetchFragment("Address_Details_container", "AddressDetails", "q_AddressDetails");
			formObject.setNGFrameState("Address_Details_container", 0);

			PersonalLoanS.mLogger.info("fetched address details");
		}
		
		int framestate5=formObject.getNGFrameState("Alt_Contact_container");
		if(framestate5 == 0){
			PersonalLoanS.mLogger.info("Alt_Contact_container");
		}
		else {
			formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
			formObject.setNGFrameState("Alt_Contact_container", 0);

			PersonalLoanS.mLogger.info("fetched address details");
		}
		
		int framestate6=formObject.getNGFrameState("ReferenceDetails");
		if(framestate6==0){
			PersonalLoanS.mLogger.info("expanded reference details");

		} else {
			formObject.fetchFragment("ReferenceDetails", "ReferenceDetails", "q_ReferenceDetails");
			formObject.setNGFrameState("ReferenceDetails", 0);
			PersonalLoanS.mLogger.info("fetched reference details");
		}
		int framestate7=formObject.getNGFrameState("Card_Details");

		if(framestate7!=0  && (!"Y".equalsIgnoreCase(formObject.getNGValue("is_cc_waiver_require")) || "CSM".equalsIgnoreCase(formObject.getWFActivityName())) ){
			/*formObject.fetchFragment("Card_Details", "CardDetails", "q_CardDetails");
				formObject.setNGFrameState("Card_Details", 0);*/
			fetch_CardDetails_frag(formObject);
			formObject.setNGFrameState("Card_Details", 0);
			PersonalLoanS.mLogger.info("fetched card details");
		} else {
			PersonalLoanS.mLogger.info("expanded card details");
		}
		int framestate8=formObject.getNGFrameState("Supplementary_Card_Details");
		if(framestate8 !=0 && "Yes".equals(formObject.getNGValue("cmplx_CardDetails_suppcardreq")) && !"Y".equalsIgnoreCase(formObject.getNGValue("is_cc_waiver_require"))){
			formObject.setVisible("Supplementary_Card_Details",true);
			formObject.fetchFragment("Supplementary_Card_Details", "SupplementCardDetails", "q_SuppCardDetails");//q_SuppCardDetails
			formObject.setNGFrameState("Supplementary_Card_Details", 0);
			PersonalLoanS.mLogger.info("fetched Supplementary details");
			loadPicklist_suppCard();
			
			if("KALYAN-EXPAT".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 5)) ||
					"KALYAN-PRIORITY".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 5)) ||
					"KALYAN-SEC".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 5)) ||
					"KALYAN-STAFF".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 5)) ||
					"KALYAN-UAE".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 5)) ||
					"KALYAN-VVIPS".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 5)) || "Self Employed".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 5)))
				{
				formObject.setVisible("SupplementCardDetails_Label7", true);
				formObject.setVisible("SupplementCardDetails_CompEmbName", true);
				}
			else
			{
				formObject.setVisible("SupplementCardDetails_Label7", false);
				formObject.setVisible("SupplementCardDetails_CompEmbName", false);
			}
		}

		int framestate9=formObject.getNGFrameState("FATCA");

		if(framestate9!=0){
			formObject.fetchFragment("FATCA", "FATCA", "q_FATCA");
			formObject.setNGFrameState("FATCA", 0);			
		}
		int framestate10=formObject.getNGFrameState("KYC");


		if(framestate10!=0){
			formObject.fetchFragment("KYC", "KYC", "q_KYC");
			formObject.setNGFrameState("KYC", 0);
			loadPicklist_KYC();
			
			PersonalLoanS.mLogger.info("KYC LoadPickList in open demograp");
			if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))){
				PersonalLoanS.mLogger.info("inside if ntb: kyc-opendemo");
				formObject.setNGValue("KYC_Combo1", "N");
				//formObject.setLocked("KYC_CustomerType", true);
				//formObject.setLocked("KYC_Combo2", true);
				formObject.setLocked("KYC_DatePicker1", true);
			}
			PersonalLoanS.mLogger.info("KYC_Combo1 :: "+formObject.getNGValue("KYC_Combo1") +" , NTB case :: "+formObject.getNGValue("cmplx_Customer_NTB"));

			PersonalLoanS.mLogger.info("after kyc fetch");
			PersonalLoanS.mLogger.info("fetched address details");
		}

		int framestate111=formObject.getNGFrameState("OECD");

		if(framestate111!=0){
			formObject.fetchFragment("OECD", "OECD", "q_OECD");
			formObject.setNGFrameState("OECD", 0);
			loadPickListOECD();
			formObject.setNGValue("OECD_CRSFlag","N"); //hritik 3709
			PersonalLoanS.mLogger.info("OECD_CRSFlag: "+formObject.getNGValue("OECD_CRSFlag"));
			//formObject.setTop("OECD", formObject.getTop("KYC")+formObject.getHeight("KYC")+30);
			PersonalLoanS.mLogger.info("fetched address details");
		}
		alignDemographiTab(formObject);
		loadDemographicsData();

	}
	public void FetchingDecision(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try{
			PersonalLoanS.mLogger.info("%%%%%%%%%%%%%%% Inside FetchingDecision PL Common @@@@@@@@@@@@@@@@@@@@@@@@@@@@ ");
			formObject.fetchFragment("DecisionHistory", "DecisionHistory", "q_Decision");
			new PLCommon().adjustFrameTops("Notepad_Values,DecisionHistory,ReferHistory");
			//change by saurabh on 10th Dec
			openDemographicTabs();
			if(!"Disbursal".equalsIgnoreCase(formObject.getWFActivityName())){
				//formObject.fetchFragment("Inc_Doc", "IncomingDoc", "q_IncomingDoc");//added by akshay on 17/1/18
			}
			formObject.setVisible("cmplx_Decision_Manual_deviation_reason", false);
			//added by akshay on 12/12/17
			List<String> referWorksteps_List=Arrays.asList("PostDisbursal_Checker","Disbursal_Checker","Document_Checker","CSM","DDVT_maker","RM_Review","DDVT_Checker","Original_Validation","CAD_Analyst1","CAD_Analyst2","CPV","CPV_Analyst","FCU","FPU","Compliance","DSA_CSO_Review","Disbursal","SalesCoordinator","Telesales_Agent","CustomerHold");
			if(referWorksteps_List.contains(formObject.getWFActivityName())){
				if(!formObject.isVisible("ReferHistory_Frame1")){
					formObject.fetchFragment("ReferHistory", "ReferHistory", "q_ReferHistory");
					formObject.setNGFrameState("ReferHistory", 0);
				}
			}
			//ended by akshay on 12/12/17

			loadInDecGrid();
			//saveIndecisionGrid();//arun 25/12/17
			loadPicklist3();
			//below code added by yash on 15/12/2017 for toteam 
			if(!NGFUserResourceMgr_PL.getGlobalVar("PL_Reject").equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")) && !"REFER".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")) && !"Pending for documentation".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")))
			{
				formObject.setLocked("DecisionHistory_DecisionReasonCode",true);
				formObject.setLocked("DecisionHistory_DecisionSubReason",true);

			}
			//above code added by yash on 15/12/2017 for toteam
			if(NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Manual_Deviation"))){
				formObject.setLocked("DecisionHistory_Button6",false);
				formObject.setLocked("cmplx_Decision_CADDecisiontray", false);        
			}
			else{
				formObject.setLocked("DecisionHistory_Button6",true);
				formObject.setLocked("cmplx_Decision_CADDecisiontray", true);         
			}

			/*String sQuery = "select distinct(Delegation_Authorithy) from ng_rlos_IGR_Eligibility_CardProduct with (nolock)  where Child_Wi ='"+formObject.getWFWorkitemName()+"'";
			List<List<String>> OutputXML = formObject.getNGDataFromDataCache(sQuery);
			PersonalLoanS.mLogger.info("sQuery list size"+sQuery);

			if(!OutputXML.isEmpty()){
				String HighDel=OutputXML.get(0).get(0);
				if(OutputXML.get(0).get(0)!=null && !OutputXML.get(0).get(0).isEmpty() &&  !OutputXML.get(0).get(0).equals("") && !OutputXML.get(0).get(0).equalsIgnoreCase("null") ){

					PersonalLoanS.mLogger.info("Inside the if dectech"+HighDel+" value is ");	
					formObject.setNGValue("cmplx_Decision_Highest_delegauth", HighDel);

				}
			}	*/
			//Code Change By aman to save Highest Delegation Auth


		}
		catch(Exception ex){
			printException(ex);
		}

		//added Tanshu Aggarwal(22/06/2017)
		if	(NGFUserResourceMgr_PL.getGlobalVar("PL_Y").equalsIgnoreCase(formObject.getNGValue("Is_Account_Create")) && NGFUserResourceMgr_PL.getGlobalVar("PL_Y").equalsIgnoreCase(formObject.getNGValue("Is_Customer_Create")))
		{
			PersonalLoanS.mLogger.info("inside both req Y");
			formObject.setLocked("DecisionHistory_Button2", true);
		}
		else{
			PersonalLoanS.mLogger.info( "inside both req N");
			formObject.setLocked("DecisionHistory_Button2", false);
		}
		//added Tanshu Aggarwal(22/06/2017)	
		//++Below code added by nikhil 13/11/2017 for Code merge

		//++ below code added by abhishek  to check workitem on CPV as per FSD 2.7.3


		if (formObject.getWFActivityName().equalsIgnoreCase("Cad_Analyst1")){
			//for decision fragment made changes 8th dec 2017
			/*formObject.setVisible("DecisionHistory_Label10", false);
 				formObject.setVisible("cmplx_DEC_New_CIFID", false);*/
			//for decision fragment made changes 8th dec 2017




			String sQuery = "SELECT activityname FROM WFINSTRUMENTTABLE with (nolock) WHERE ProcessInstanceID = '"+formObject.getWFWorkitemName()+"'";
			boolean flag = false;
			/*StringBuilder values = new StringBuilder();
			        values.append(",'" + sProcessName + "'");*/

			String listValues ="";
			List<List<String>> list=formObject.getNGDataFromDataCache(sQuery);
			PersonalLoanS.mLogger.info(" In CC CAD workstep decision history expand"+ "Done button click::query result is::"+list );
			for(int i =0;i<list.size();i++ ){
				if(i==0){
					listValues += list.get(i).get(0);
					// values.append(list.get(i).get(0));
				}else{
					listValues += "|"+list.get(i).get(0);
					//values.append(",'" + sProcessName + "'");
				}

			}

			sQuery = "Select cpv_decision FROM NG_PL_EXTTABLE with (nolock) WHERE PL_wi_name='"+formObject.getWFWorkitemName()+"'";
			List<List<String>> list1=formObject.getNGDataFromDataCache(sQuery);
			PersonalLoanS.mLogger.info(" In CC CAD workstep decision history expand"+ "cpv dec value is::"+list1 );
			listValues +="#"+list1.get(0).get(0);
			PersonalLoanS.mLogger.info(" In CC CAD workstep decision history expand"+ "list value is::"+listValues );
			formObject.setNGValue("DecisionHistory_CadTempField",listValues);




		}
		//for decision fragment made changes 8th dec 2017

		//for decision fragment made changes 8th dec 2017
		else if(formObject.getWFActivityName().equalsIgnoreCase("Smart_CPV")){

			//for decision fragment made changes 8th dec 2017
			if(formObject.getNGValue("cmplx_Decision_contactableFlag").equalsIgnoreCase("true")){
				formObject.fetchFragment("Notepad_Values", "NotepadDetails", "q_Note");
				formObject.setVisible("NotepadDetails_Label6", true);
				formObject.setVisible("NotepadDetails_insqueue", true);
				formObject.setVisible("NotepadDetails_inscompletion", true);
				formObject.setVisible("NotepadDetails_Label10", true);
				formObject.setVisible("NotepadDetails_Actdate", true);
				formObject.setVisible("NotepadDetails_Label11", true);
				formObject.setVisible("NotepadDetails_Actusername", true);
				formObject.setVisible("NotepadDetails_Label9", true);
				formObject.setVisible("NotepadDetails_ActuserRemarks", true);
				LoadPickList("NotepadDetails_notedesc", "select '--Select--'  union select  description from ng_master_notedescription  with (nolock) where workstep != 'NA'");	//PCASI-3154
				formObject.setNGValue("NotepadDetails_notedesc","--Select--");

				formObject.fetchFragment("Smart_check", "SmartCheck", "q_SmartCheck");
				formObject.fetchFragment("Office_verification", "OfficeandMobileVerification", "q_OffVerification");

				//	formObject.setEnabled("DecisionHistory_nonContactable", false);
				formObject.setEnabled("NotepadDetails_Frame1", false);
				//	formObject.setEnabled("DecisionHistory_Frame1", false);
				formObject.setEnabled("SmartCheck_Frame1", false);
				formObject.setEnabled("OfficeandMobileVerification_Frame1", false);

				//	formObject.setEnabled("DecisionHistory_cntctEstablished", true);

				formObject.setNGValue("cmplx_Decision_Decision","Smart CPV Hold");

				formObject.setNGFrameState("Office_verification", 1);
				formObject.setNGFrameState("Smart_check", 1);
				formObject.setNGFrameState("Notepad_Values", 1);
			}else{
				//for decision fragment made changes 8th dec 2017

				/*formObject.setEnabled("DecisionHistory_Frame1", true);
       				formObject.setEnabled("DecisionHistory_cntctEstablished", false);
       				formObject.setEnabled("DecisionHistory_nonContactable", true);
       				formObject.setNGValue("cmplx_Decision_Decision","--Select--");*/
				//for decision fragment made changes 8th dec 2017


			}

		}
	}

	public void expandLoanDetails(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//if(!"DDVT_Maker".equalsIgnoreCase(formObject.getWFActivityName()))
		formObject.setLocked("cmplx_LoanDetails_loanemi",true);

		formObject.setNGValue("LoanDetails_winame", formObject.getWFWorkitemName());
		if(formObject.getNGValue("cmplx_EligibilityAndProductInfo_InstrumentType")=="--Select--")
			formObject.setNGValue("cmplx_EligibilityAndProductInfo_InstrumentType", "S");
		//pcasi3365 added ddvtm
		if("CSM".equalsIgnoreCase(formObject.getWFActivityName()) || "SalesCoordinator".equalsIgnoreCase(formObject.getWFActivityName())){
			setLoanFieldsVisible();
			formObject.setLocked("cmplx_EligibilityAndProductInfo_InstrumentType", false);

		}

		if("RM_CSO_Review".equalsIgnoreCase(formObject.getWFActivityName())||"DSA_CSO_Review".equalsIgnoreCase(formObject.getWFActivityName()) || "RM_Review".equalsIgnoreCase(formObject.getWFActivityName()))
		{	
			formObject.setVisible("LoanDeatils_calculateemi",true);
			 formObject.setLocked("LoanDeatils_calculateemi",false);
			 formObject.setEnabled("LoanDeatils_calculateemi", true);
			 formObject.setEnabled("LoanDetails_Save", true);
			 formObject.setLocked("LoanDetails_Save", false);
		}
		else
		{	
			formObject.setVisible("LoanDeatils_calculateemi",false);
			formObject.setLocked("LoanDeatils_calculateemi",false);
			 formObject.setEnabled("LoanDeatils_calculateemi", false);
		}
		//below code added by nikhil
		formObject.setNGValue("cmplx_LoanDetails_insplan", "E");
		formObject.setNGValue("cmplx_LoanDetails_repfreq", "Monthly");
		formObject.setLocked("cmplx_LoanDetails_repfreq", true);


	}
	/*public void loadEligibilityData(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String appType = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 4);
		//below code by saurabh on 11 nov 17.
		formObject.setNGValue("cmplx_EligibilityAndProductInfo_RepayFreq","Monthly");
		formObject.setNGValue("cmplx_EligibilityAndProductInfo_InstrumentType", "S");

		PersonalLoanS.mLogger.info("inside loadEligibilityData"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_BaseRateType"));
		Eligibilityfields();

		//formObject.setLocked("cmplx_EligibilityAndProductInfo_EFCHidden", true);
		
				
		loadPicklistELigibiltyAndProductInfo();

	}*/
	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : Function to make Loan fields Visible     

	 ***********************************************************************************  */


	public void setLoanFieldsVisible() {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setVisible("ELigibiltyAndProductInfo_Label11",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_RepayFreq",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label31",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_Moratorium",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label12",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_FirstRepayDate",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label14",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_InterestType",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label15",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_BaseRateType",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label17",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_MArginRate",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label16",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_BAseRate",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label18",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_ProdPrefRate",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label23",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_NetRate",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label13",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_MaturityDate",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label30",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_AgeAtMaturity",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label32",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_NoOfInstallments",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label19",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_LPF",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label20",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_LPFAmount",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label21",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_Insurance",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label22",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_InsuranceAmount",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Text9",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Text8",true);
		//added by saurabh on 2nd Jan
		formObject.setVisible("cmplx_EligibilityAndProductInfo_FinalInterestRate", true);
		
		
	}
	//function by saurabh on 10th Dec
	public void loadDemographicsData(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		if(!formObject.isVisible("AddressDetails_Frame1")){
			loadPicklist_Address();
		}
		if(!formObject.isVisible("AltContactDetails_Frame1")){
			//  LoadPickList("AlternateContactDetails_carddispatch", "select '--Select--' as description,'' as code union all select convert(varchar,description),code from NG_MASTER_Dispatch order by code ");
			String TypeOfProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0)==null?"":formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0);
			LoadPickList("AlternateContactDetails_CustomerDomicileBranch", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock)  where product_type = '"+TypeOfProd+"' order by code");
		}
		if(!formObject.isVisible("ReferenceDetails_Frame1")){
			LoadPickList("ReferenceDetails_ref_Relationship", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Relationship with (nolock) order by code");
		}
		if(!formObject.isVisible("FATCA_Frame6")){
			PersonalLoanS.mLogger.info("***********cmplx_FATCA_USRelation cmplx_FATCA_USRelation cmplx_FATCA_USRelation cmplx_FATCA_USRelation cmplx_FATCA_USRelation cmplx_FATCA_USRelation");
			LoadPickList("cmplx_FATCA_USRelation", " select '--Select--' as description, '' as code  union select convert(varchar,description),code  from ng_master_usrelation with (nolock)  order by code");
			LoadPickList("cmplx_FATCA_Category", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_category with (nolock) order by code");
		}
		String usRelation = formObject.getNGValue("cmplx_FATCA_USRelation");

		if("O".equalsIgnoreCase(usRelation)){
			//changed from setenabled to setlocked by saurabh on 11 nov 17.
			formObject.setLocked("FATCA_Frame6", true);
			formObject.setLocked("cmplx_FATCA_USRelation",false);
			formObject.setEnabled("FATCA_Save", true);
		}
		if(!formObject.isVisible("OECD_Frame8")){
			loadPickListOECD();
		}
	}

	public void setDataInMultipleAppGrid(){

		//added by akshay on 20/2/18
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			/*if(formObject.isVisible("GuarantorDetails_Frame1")==false && !"".equals(formObject.getNGValue("cmplx_Customer_age")) && Float.parseFloat(formObject.getNGValue("cmplx_Customer_age"))<18)
				{
					formObject.fetchFragment("GuarantorDet", "GuarantorDetails", "q_Guarantor");
					formObject.setNGFrameState("GuarantorDet", 0);
					adjustFrameTops("GuarantorDet,IncomeDEtails");
				}*/
			PersonalLoanS.mLogger.info("MultipleApplicantsGrid rowcount: "+formObject.getLVWRowCount("cmplx_Decision_MultipleApplicantsGrid"));
			int multipRowCount = formObject.getLVWRowCount("cmplx_Decision_MultipleApplicantsGrid");
			List<String> multipAdded = new ArrayList<String>();
			for(int i=0;i<multipRowCount;i++){
				multipAdded.add(formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",i,2));
			}
			//if(formObject.getLVWRowCount("cmplx_Decision_MultipleApplicantsGrid")==0){
			List<List<String>> mylist=new ArrayList<List<String>>();
			List<String> subList=new ArrayList<String>();

			PersonalLoanS.mLogger.info("mylist 2: "+mylist);

			int n=formObject.getLVWRowCount("SupplementCardDetails_cmplx_supplementGrid");
			PersonalLoanS.mLogger.info("rowcount supplement is: "+n);
			List<String> suppPassAdded = new ArrayList<String>();
			for(int i=0;i<n;i++){
				String statusSupp = formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,42);//.equals("Active");
				String passSupp = formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,3);
				String cardProdSupp = formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,30);
				PersonalLoanS.mLogger.info("statusSupp supplement is: "+statusSupp);
				PersonalLoanS.mLogger.info("passSupp supplement is: "+passSupp);
				PersonalLoanS.mLogger.info("cardProdSupp supplement is: "+cardProdSupp);
				if(!suppPassAdded.contains(passSupp) && "Active".equals(statusSupp) && !multipAdded.contains(formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,3))){
					subList.add("Supplement");
					subList.add(formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,0)+" "+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,2));
					subList.add(formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,3));
					subList.add(formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,33));//cif
					/*subList.add("");//CIF verified
					subList.add("");//cif locked
					subList.add("");//cif update
					 */
					subList.add("");//is_cif_created
					subList.add("");//cif verified
					subList.add("");//cif locked
					subList.add("");//is_cif_updated
					subList.add(formObject.getWFWorkitemName());

					mylist.add(new ArrayList<String>(subList));//each time adding we need to give a new list reference or it will pick up the old one and uon clear it will get deleted as well
					PersonalLoanS.mLogger.info("mylist inside loop: "+mylist);
					suppPassAdded.add(passSupp);
				}
			}

			PersonalLoanS.mLogger.info("mylist 3: "+mylist);
			if(mylist.size()>0){
				for(List<String> temp:mylist){
					formObject.addItemFromList("cmplx_Decision_MultipleApplicantsGrid", temp);
				}
			}
			//}		
		}catch(Exception e){printException(e);}
	}


	public void loadDataInCRNGrid(){

		try{FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String query = "select cardlimit.Card_Product AS cardProd,cardlimit.Final_Limit AS finalLim,'Primary' AS card_type,cust.passportNo AS passport from ng_rlos_IGR_Eligibility_CardLimit cardlimit WITH (nolock) join ng_RLOS_Customer cust WITH (nolock) on cardlimit.Child_Wi = cust.wi_name where Cardproductselect = 'true' and ( cardlimit.wi_name = '"+formObject.getNGValue("parent_WIName")+"' or cardlimit.Child_Wi = '"+formObject.getWFWorkitemName()+"' )  UNION SELECT cardProduct AS cardProd,cast(approvedlimit as nvarchar) AS finalLim,'Supplement' AS card_type,PassportNo AS passport FROM ng_RLOS_GR_SupplementCardDetails WITH (nolock) WHERE supplement_WIname = '"+formObject.getWFWorkitemName()+"' AND status1= 'Active' ORDER BY card_type";
		//query = "select cardlimit.Card_Product AS cardProd,cardlimit.Final_Limit AS finalLim,'Primary' AS card_type,cust.passportNo AS passport from ng_rlos_IGR_Eligibility_CardLimit cardlimit with (nolock) join ng_RLOS_Customer cust WITH (nolock) on cardlimit.Child_Wi = cust.wi_name where Cardproductselect = 'true' and ( cardlimit.wi_name = '"+formObject.getNGValue("parent_WIName")+"' or cardlimit.Child_Wi = '"+formObject.getWFWorkitemName()+"' ) UNION SELECT cardProduct AS cardProd,approvedlimit AS finalLim,'Supplement' AS card_type,PassportNo AS passport FROM ng_RLOS_GR_SupplementCardDetails WHERE supplement_WIname = '"+formObject.getWFWorkitemName()+"' AND status1= 'Active' ORDER BY card_type";

		PersonalLoanS.mLogger.info("query loadinCRNGrid: "+query);
		List<List<String>> records = formObject.getDataFromDataSource(query);
		PersonalLoanS.mLogger.info("records loadinCRNGrid: "+records);	
		if(records!=null && records.size()>0){
			UIComponent pComp = formObject.getComponent("cmplx_CardDetails_cmplx_CardCRNDetails");
			ListView objListView = ( ListView )pComp;
			//List<String> myList = new ArrayList<String>();

			int gridRowCount = formObject.getLVWRowCount("cmplx_CardDetails_cmplx_CardCRNDetails");
			boolean gridcontainsprimary = false;
			List<String> gridrows = new ArrayList<String>();
			List<String> crnGridColumns = new ArrayList<String>();
			if(gridRowCount>0){
				for(int i=0;i<gridRowCount;i++){
					gridrows.add(formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",i,0)+":"+formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",i,9)+":"+formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",i,10).toUpperCase());
					if("Primary".equalsIgnoreCase(formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",i,9)))
					{
						gridcontainsprimary = true;
					}
				}
			}
			int columns = objListView.getChildCount();
			for(int j=0;j<columns;j++){
				crnGridColumns.add(((Column)(pComp.getChildren().get(j))).getName());
			}
			PersonalLoanS.mLogger.info("crnGridColumns loadinCRNGrid: "+crnGridColumns);
			for(List<String> record:records){
				List<String> newRecord = new ArrayList<String>();
				String dbValue = record.get(0)+":"+record.get(2)+":"+record.get(3).toUpperCase();
				String temp_applicant = record.get(2);
				if(temp_applicant.equalsIgnoreCase("Primary") && gridcontainsprimary)
				{
					continue;
				}
				if(!gridrows.contains(dbValue)){
					newRecord.add(record.get(0));
					for(int i=1;i<columns;i++){
						PersonalLoanS.mLogger.info("column name in iteration: "+((Column)(pComp.getChildren().get(i))).getName());
						if(((Column)(pComp.getChildren().get(i))).getName().equalsIgnoreCase("wi_name")){
							newRecord.add(formObject.getWFWorkitemName());
						}
						else if(((Column)(pComp.getChildren().get(i))).getName().equalsIgnoreCase("final_limit")){
							if(record.get(1)!=null && !record.get(1).equalsIgnoreCase("null") && !record.get(1).equalsIgnoreCase("")){
								newRecord.add(record.get(1));
							}
							else{
								newRecord.add("0");
							}
						}

						/*else if(((Column)(pComp.getChildren().get(i))).getName().equalsIgnoreCase("ECRN") && NGFUserResourceMgr_PL.getGlobalVar("Services").contains(formObject.getNGValue("Sub_Product"))){
							PersonalLoanS.mLogger.info("inside ECN condition: ");
							if(record.get(3)!=null){
								newRecord.add(record.get(3));
							}
							else{
								newRecord.add("");
							}
						}
						else if(((Column)(pComp.getChildren().get(i))).getName().equalsIgnoreCase("CRN") && NGFUserResourceMgr_PL.getGlobalVar("Services").contains(formObject.getNGValue("Sub_Product"))){
							PersonalLoanS.mLogger.info("inside CRN condition: ");
							if(record.get(2)!=null){
								newRecord.add(record.get(2));
							}
							else{
								newRecord.add("");
							}
						}
						else if(((Column)(pComp.getChildren().get(i))).getName().equalsIgnoreCase("Applicant Type") && NGFUserResourceMgr_PL.getGlobalVar("Services").contains(formObject.getNGValue("Sub_Product"))){
							newRecord.add("Primary");
						}
						else if(((Column)(pComp.getChildren().get(i))).getName().equalsIgnoreCase("PassportNo") && NGFUserResourceMgr_PL.getGlobalVar("Services").contains(formObject.getNGValue("Sub_Product"))){
							newRecord.add("");
						}*/

						else if(((Column)(pComp.getChildren().get(i))).getName().equalsIgnoreCase("Applicant Type")){
							newRecord.add(record.get(2));
						}
						else if(((Column)(pComp.getChildren().get(i))).getName().equalsIgnoreCase("PassportNo")){
							newRecord.add(record.get(3));
						}
						//start by sagarika
						else if(((Column)(pComp.getChildren().get(i))).getName().equalsIgnoreCase("Transaction Fee Profile")){
							PersonalLoanS.mLogger.info("inside Transaction Fee Profile");
							String cardProduct=record.get(0);
							PersonalLoanS.mLogger.info("card product"+cardProduct);
							String query1="select TOP 1 code,convert(varchar, Description) as description from ng_master_TransactionFeeProfile with (nolock) where isActive='Y' and Product = '"+cardProduct+"'";
							List<List<String>> result = formObject.getNGDataFromDataCache(query1);
							//CreditCard.mLogger.info("card product result"+result);
							if(result!=null && !result.isEmpty())  //if(result!=null && result.size()>0)
							{
								//CreditCard.mLogger.info("result"+result.get(0).get(0));
								//cmplx_EmploymentDetails_CurrEmployer
								newRecord.add(result.get(0).get(0));
							}
							else{
								newRecord.add("");
							}
						}
						else if(((Column)(pComp.getChildren().get(i))).getName().equalsIgnoreCase("Interest Fee Profile")){
							PersonalLoanS.mLogger.info("inside Transaction Fee Profile");
							String cardProduct=record.get(0);
							PersonalLoanS.mLogger.info("card product"+cardProduct);
							String query2="select TOP 1 code,convert(varchar, Description) as description from ng_master_InterestProfile with (nolock) where isActive='Y' and Product = '"+cardProduct+"'";
							List<List<String>> result1 = formObject.getNGDataFromDataCache(query2);
							//CreditCard.mLogger.info("card product result"+result1);
							if(result1!=null && !result1.isEmpty())  //if(result!=null && result.size()>0)
							{
								newRecord.add(result1.get(0).get(0));
							}
							else{
								newRecord.add("");
							}
						}
						else if(((Column)(pComp.getChildren().get(i))).getName().equalsIgnoreCase("Fee Profile")){
							PersonalLoanS.mLogger.info("inside Transaction Fee Profile");
							String cardProduct=record.get(0);
							//CreditCard.mLogger.info("card product"+cardProduct);
							String query3="select TOP 1 code,convert(varchar, Description) as description from NG_MASTER_feeprofile with (nolock) where isActive='Y' and Product = '"+cardProduct+"'";
							List<List<String>> result3 = formObject.getNGDataFromDataCache(query3);
							//CreditCard.mLogger.info("card product result"+result3);
							if(result3!=null && !result3.isEmpty())  //if(result!=null && result.size()>0)
							{
								//CreditCard.mLogger.info("result"+result3.get(0).get(0));
								newRecord.add(result3.get(0).get(0));
							}
							else{
								newRecord.add("");
							}
						}
						else{
							newRecord.add("");
						}
					}
					//String finalLimit = formObject.getNGValue("CardDetails_FinalLimit");
					formObject.addItemFromList("cmplx_CardDetails_cmplx_CardCRNDetails", newRecord);
				}
				else{
					if(gridrows.indexOf(record.get(0))>-1){
						String finalLimitInGrid = formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",gridrows.indexOf(record.get(0)),crnGridColumns.indexOf("final_limit"));
						if(!finalLimitInGrid.equalsIgnoreCase(record.get(1))){
							formObject.setNGValue("cmplx_CardDetails_cmplx_CardCRNDetails", gridrows.indexOf(record.get(0)), crnGridColumns.indexOf("final_limit"), record.get(1));
						}
					}
					formObject.addItemFromList("cmplx_CardDetails_cmplx_CardCRNDetails", newRecord);
				}
				PersonalLoanS.mLogger.info("newRecord formed loadinCRNGrid: "+newRecord);
			}
		}
		String activityName = formObject.getWFActivityName();
		if("DDVT_Maker".equalsIgnoreCase(activityName)){
			Load_SelfSupp_CRNGrid();
		}
		}catch(Exception ex){
			PersonalLoanS.mLogger.info("CC Common Exception Inside loadDataInCRNGrid()");
			printException(ex);
		}
	}


	public void loadInCCCreationGrid(){
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			//String cif = formObject.getNGValue("cmplx_Customer_CIFNo");
			//String custname = formObject.getNGValue("cmplx_Customer_FirstNAme")+" "+formObject.getNGValue("cmplx_Customer_MiddleNAme")+" "+formObject.getNGValue("cmplx_Customer_LastNAme");
			int rowCount = formObject.getLVWRowCount("cmplx_CCCreation_cmplx_CCCreationGrid");
			List<String> currentCRN=null;

			if(rowCount>0){
				currentCRN= new ArrayList<String>();
				for(int i=0;i<rowCount;i++){
					String cardProdPassport = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid", i,5)+":"+formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid", i,13); 
					currentCRN.add(cardProdPassport);
				}
			}
			int crngridRowCount = formObject.getLVWRowCount("cmplx_CardDetails_cmplx_CardCRNDetails");//cmplx_CardDetails_cmplx_CardCRNDetails
			PersonalLoanS.mLogger.info("CC Common crngridRowCount is:"+crngridRowCount);
			PersonalLoanS.mLogger.info("CC Common rowCount is:"+rowCount);
			PersonalLoanS.mLogger.info("CC Common currentCRN is:"+currentCRN);
			if(crngridRowCount>0){
				String prod = "";
				List<List<String>> ccCreationgridRows = new ArrayList<List<String>>();
				Map<String,String> combinedlimit = setCombineLimitHashmapvalues();
				Map<String,String> crnGridColumnsToValues ;
				Map<String,String> SupplementCardMap =loadSupplementCardLimit();
				for(int i=0;i<crngridRowCount;i++){
					crnGridColumnsToValues = initializeGridMap("cmplx_CardDetails_cmplx_CardCRNDetails",i);
					String newCardProdPassport = crnGridColumnsToValues.get(getValueOfConstant("CRN_cardProduct"))+":"+crnGridColumnsToValues.get(getValueOfConstant("CRN_Passport"));
					if(currentCRN==null || !currentCRN.contains(newCardProdPassport)){
						PersonalLoanS.mLogger.info("CC Common inside for loop is:");
						String cardprod = crnGridColumnsToValues.get(getValueOfConstant("CRN_cardProduct"));
						String passport = crnGridColumnsToValues.get(getValueOfConstant("CRN_Passport"));
						String kalyanNo = crnGridColumnsToValues.get(getValueOfConstant("CRN_KalyanRefNo"));
						String applicant_type = crnGridColumnsToValues.get(getValueOfConstant("CRN_AppType"));
						List<String> gridRows = new ArrayList<String>();
						gridRows.add(formObject.getWFWorkitemName());
						gridRows.add(crnGridColumnsToValues.get(getValueOfConstant("CRN_CRN")));
						gridRows.add(crnGridColumnsToValues.get(getValueOfConstant("CRN_ECRN")));
						gridRows.add(getCustomerDetails(passport,true));
						gridRows.add(getCustomerDetails(passport,false));
						gridRows.add(cardprod);
						//gridRows.add(crnGridColumnsToValues.get(getValueOfConstant("CRN_FinalLimit")));
						if(combinedlimit==null || combinedlimit.isEmpty()){
							gridRows.add("0.0");//final limit
							gridRows.add("0.0");//combined limit
						}
						else{
							try{
							//Deepak method added to load supplymentry card limit
								if("Primary".equalsIgnoreCase(applicant_type)){
									gridRows.add(String.valueOf(Double.valueOf(combinedlimit.get(cardprod)).longValue()));//final limit	
								}
								else if("Supplement".equalsIgnoreCase(applicant_type) && formObject.getNGValue("cmplx_Customer_PAssportNo").equalsIgnoreCase(passport))
								{
									gridRows.add(String.valueOf(Double.valueOf(getFinalLimit("", formObject)).longValue()));
								}
								else{
									if(SupplementCardMap.containsKey(passport+"-"+cardprod)){
										gridRows.add(SupplementCardMap.get(passport+"-"+cardprod));
									}
									else{
										gridRows.add(String.valueOf(Double.valueOf(combinedlimit.get(cardprod)).longValue()));//final limit
									}
									
								}
								gridRows.add(String.valueOf(Double.valueOf(combinedlimit.get(cardprod)).longValue()));//combined limit	
							}catch(Exception ex){
								gridRows.add("0.0");//final limit
								gridRows.add("0.0");
							}

						}
						gridRows.add("");
						gridRows.add("");
						gridRows.add("");
						gridRows.add(crnGridColumnsToValues.get(getValueOfConstant("CRN_IntFeeProf")));
						gridRows.add(crnGridColumnsToValues.get(getValueOfConstant("CRN_AppType")));
						gridRows.add(passport);
						gridRows.add(kalyanNo);//kalyan no
						//gridRows.add("");
						int columns =16;
						/*gridRows.add("");*/
						//gridRows.add(formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",i,9));//added by akshay on 27/2/18 for drop 4
						if(i==crngridRowCount-1){
							prod+="'"+cardprod+"'";
						}
						else{
							prod+="'"+cardprod+"',";
						}
						PersonalLoanS.mLogger.info("CC Common gridRow list is:"+gridRows);
						int temp = gridRows.size();
						PersonalLoanS.mLogger.info("CC Common gridRow list is:"+temp);
						if(columns>temp){
							PersonalLoanS.mLogger.info("Inside CCCommon ->loadInDecGrid() temp value"+temp);
							while(columns>temp){
								PersonalLoanS.mLogger.info("Inside CCCommon ->loadInDecGrid() temp value"+temp);
								gridRows.add("");
								temp++;
							}
						}
						ccCreationgridRows.add(gridRows);

					}
				}
				ccCreationgridRows=addTemplateIdtoEveryRow(prod,ccCreationgridRows);
				for(List<String> row:ccCreationgridRows){
					formObject.addItemFromList("cmplx_CCCreation_cmplx_CCCreationGrid", row);
				}

			}

		}catch(Exception ex){
			PersonalLoanS.mLogger.info("CC Common Exception Inside loadInCCCreationGrid()");
			printException(ex);
		}
	}

	public  List<List<String>> addTemplateIdtoEveryRow(String prod, List<List<String>> ccCreationgridRows){
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			if(!"".equalsIgnoreCase(prod)){
				String query = "select distinct templateId,CODE from ng_master_cardProduct where CODE in ("+prod+")";  
				PersonalLoanS.mLogger.info("query for templateId is:"+query);
				List<List<String>> records = formObject.getNGDataFromDataCache(query);
				PersonalLoanS.mLogger.info("records received is:"+records);
				for(List<String> gridrow:ccCreationgridRows){
					if(records.isEmpty()){
						//blank template id added as no recode foung in ng_master_cardProduct for givent product.
						gridrow.add("");
					}
					String template_id ="";
					for(List<String> record: records){
						PersonalLoanS.mLogger.info("CC Common gridrow.get(5) is:"+gridrow.get(5));
						PersonalLoanS.mLogger.info("CC Common record.get(1) is:"+record.get(1));

						if((gridrow.get(5).toString()).equalsIgnoreCase(record.get(1).toString())){
							template_id = record.get(0);
						}
					}
					gridrow.add(template_id);
					gridrow.add("");
					gridrow.add("");
					PersonalLoanS.mLogger.info("final gridrow is:"+gridrow);
				}
			}
			return ccCreationgridRows;
		}catch(Exception ex){
			PersonalLoanS.mLogger.info("Exception in addTemplateIdtoEveryRow is: ");
			printException(ex);
			return null;
		}

	}

	public Map<String,String> setCombineLimitHashmapvalues(){
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			Map<String,String> temp = new HashMap<String,String>();
			String query = "select distinct cl.Card_Product,tempTble.limit from (select max(cast(Final_Limit as float)) as limit,max(combined_limit) as val,max(Child_Wi) as child_wi from ng_rlos_IGR_Eligibility_CardLimit with (nolock) where Child_Wi = '"+formObject.getWFWorkitemName()+"' and Cardproductselect='true'  group by case when combined_limit=1 then combined_limit else Card_Product end) as tempTble join ng_rlos_IGR_Eligibility_CardLimit cl on tempTble.val = cl.combined_limit and cl.Cardproductselect='true' and cl.Child_Wi= tempTble.child_wi";
			PersonalLoanS.mLogger.info("query for combined limit is:"+query);
			List<List<String>> records = formObject.getDataFromDataSource(query);
			PersonalLoanS.mLogger.info("query output for combined limit is:"+records);
			if(records!=null && records.size()>0){
				for(List<String> record:records){
					temp.put(record.get(0), record.get(1));
				}
			}
			return temp;
		}catch(Exception e){
			PersonalLoanS.mLogger.info("CC Common Exception Inside setCombineLimitHashmapvalues()");
			printException(e);
			return null;
		}

	}
	public Map<String,String> initializeCRNGridMap(int crnRowNum){
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			Map<String,String> temp = new HashMap<String,String>();
			UIComponent pComp = formObject.getComponent("cmplx_CardDetails_cmplx_CardCRNDetails");
			ListView objListView = ( ListView )pComp;
			int columns = objListView.getChildCount();
			for(int j=0;j<columns;j++){
				temp.put(((Column)(pComp.getChildren().get(j))).getName(),formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",crnRowNum,j));
			}
			return temp;
		}catch(Exception ex){
			PersonalLoanS.mLogger.info("Exception in initializeCRNGridMap is: ");
			printException(ex);
			return null;
		}

	}
	public Map<String,String> initializeGridMap(String lvName,int rowNum){
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			Map<String,String> temp = new HashMap<String,String>();
			UIComponent pComp = formObject.getComponent(lvName);
			ListView objListView = ( ListView )pComp;
			int columns = objListView.getChildCount();
			for(int j=0;j<columns;j++){
				temp.put(((Column)(pComp.getChildren().get(j))).getName(),formObject.getNGValue(lvName,rowNum,j));
			}
			return temp;
		}catch(Exception ex){
			PersonalLoanS.mLogger.info("Exception in initializeGridMap is: ");
			printException(ex);
			return null;
		}

	}
	public String getValueOfConstant(String constName){
		return NGFUserResourceMgr_PL.getGlobalVar(constName);
	}


	public String getCustomerDetails(String passport,boolean forCif){
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String listViewName = "cmplx_Decision_MultipleApplicantsGrid";
			String retValue = "";
			int multipleApplGirRowCount = formObject.getLVWRowCount(listViewName);
			for(int i=0;i<multipleApplGirRowCount;i++){
				if(passport.equals(formObject.getNGValue(listViewName, i, 2))){
					if(forCif){
						retValue = formObject.getNGValue(listViewName, i, 3);
						break;
					}	
					//Changes done for code optimization 25/07
					else{
						retValue = formObject.getNGValue(listViewName, i, 1);
						break;
					}
					//Changes done for code optimization 25/07
				}

			}
			return retValue;
		}catch(Exception ex){
			PersonalLoanS.mLogger.info("Exception in getCustomerDetails is: ");
			printException(ex);
			return "";
		}
	}

	public void loadDataInLimitInc(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setNGValue("cmplx_LimitInc_CIF", formObject.getNGValue("cmplx_Customer_CIFNO"));
		formObject.setNGValue("cmplx_LimitInc_CustomerName", formObject.getNGValue("cmplx_Customer_FirstNAme")+" "+formObject.getNGValue("cmplx_Customer_MiddleNAme")+" "+formObject.getNGValue("cmplx_Customer_LastNAme"));
		formObject.setNGValue("cmplx_LimitInc_Permanant",getFromproductGrid(4));
		String query = "select top 1 c.ECRN,c.General_Status,c.SchemeCardProd,d.Final_limit from ng_RLOS_CUSTEXPOSE_CardDetails c,ng_rlos_IGR_Eligibility_CardProduct d where Child_Wi = '"+formObject.getWFWorkitemName()+"' and c.Limit_Increase = 'true'";
		List<List<String>> record = formObject.getNGDataFromDataCache(query);
		String ecrn ="";
		String general_status ="";
		String cardProd ="";
		String finalLimit ="";
		if(record!=null && record.size()>0){
			ecrn = record.get(0).get(0);
			general_status = record.get(0).get(1);
			cardProd = record.get(0).get(2);
			finalLimit = record.get(0).get(3);
		}
		if("IM".equalsIgnoreCase(getFromproductGrid(2)) || "PU".equalsIgnoreCase(getFromproductGrid(2))){
			formObject.setNGValue("cmplx_LimitInc_NewCardProduct", getFromproductGrid(5));
		}
		else if("LI".equalsIgnoreCase(getFromproductGrid(2))){
			formObject.setNGValue("cmplx_LimitInc_NewCardProduct",cardProd);
		}
		formObject.setNGValue("cmplx_LimitInc_CRN", ecrn);
		formObject.setNGValue("cmplx_LimitInc_ECRN", ecrn);
		formObject.setNGValue("cmplx_LimitInc_New_Limit", finalLimit);


	}

	public String getFromproductGrid(int index){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		return formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,index);
	}



	

	public void autopopulateValues(FormReference formObject) {

		String mobNo1 = formObject.getNGValue("AlternateContactDetails_MobileNo1");
		String mobNo2 = formObject.getNGValue("AlternateContactDetails_MobileNo2");
		String dob = formObject.getNGValue("cmplx_Customer_DOb");
		int adressrowcount = formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
		String poboxResidence = "";
		String poboxOffice = "";
		String homeadd = "";
		String emirate = "";
		for(int i=0;i<adressrowcount;i++){
			String addType = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,0);
			String pref = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,10);
			if("true".equalsIgnoreCase(pref))//sagarika for CR emirate to be mapped
			{
				emirate=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,6);
			}
			//Added by aman for PCSP-162 cmplx_OffVerification_colleaguenoverified
			if("RESIDENCE".equalsIgnoreCase(addType)){
				poboxResidence = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,8);
			}
			if("OFFICE".equalsIgnoreCase(addType))
			{
				poboxOffice = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,8);
			}
			if("Home".equalsIgnoreCase(addType)){
				String house = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,1);
				String build = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,2);
				String street = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,3);
				String landmark = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,4);
				homeadd = house+" "+build+" "+street+" "+landmark;
			}
		}
		String resNo = formObject.getNGValue("AlternateContactDetails_RESIDENCENO");
		String officeNo = formObject.getNGValue("AlternateContactDetails_OFFICENO");
		String homeNo = formObject.getNGValue("AlternateContactDetails_HOMECOUNTRYNO");
		String email1 = formObject.getNGValue("AlternateContactDetails_EMAIL1_PRI");
		String email2 = formObject.getNGValue("AlternateContactDetails_EMAIL2_SEC");
        String motherName= formObject.getNGValue("cmplx_Customer_MotherName");// PCASI-1003

		//setValues(formObject,mobNo1,mobNo2,dob,poboxResidence,poboxOffice,resNo,officeNo,homeNo,homeadd,email1,email2,emirate);
		setValues(formObject,mobNo1,mobNo2,dob,poboxResidence,poboxOffice,homeNo,homeadd,email1,email2,emirate,motherName);


	}
	
	public void setValues(FormReference formObject,String...values) {
		String[] controls = new String[]{"cmplx_CustDetailVerification_Mob_No1_val","cmplx_CustDetailVerification_Mob_No2_val","cmplx_CustDetailVerification_dob_val",
				"cmplx_CustDetailVerification_POBoxNo_val","cmplx_CustDetailVerification_persorcompPOBox_val",
				"cmplx_CustDetailVerification_hcountrytelno_val","cmplx_CustDetailVerification_hcountryaddr_val","cmplx_CustDetailVerification_email1_val","cmplx_CustDetailVerification_email2_val"
				,"cmplx_CustDetailVerification_emirates_val","cmplx_CustDetailVerification_Mother_name_val"};
		int i=0;
		for(String str : values){
			if(checkValue(str)){
				formObject.setNGValue(controls[i], str);
				formObject.setLocked(controls[i], true);

			}
			else{
				PersonalLoanS.mLogger.info( "value received at index "+i+" is: "+str);
			}
			i++;
		}
	}

	public boolean checkValue(String ngValue){
		if(ngValue==null ||ngValue.equalsIgnoreCase("") ||ngValue.equalsIgnoreCase(" ") || ngValue.equalsIgnoreCase("--Select--") || ngValue.equalsIgnoreCase("false")){
			return false;
		}
		return true;
	}
	//++ below code added by abhishek as per FSD 2.7.3
	public void enable_custVerification()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		
		// auto populaate fields from prev tabs
		formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
		formObject.setNGFrameState("Alt_Contact_container", 0);

		
		//below code commented by PCSP-206
		//formObject.fetchFragment("CustomerDetails", "Customer", "q_Customer");
		formObject.setNGValue("cmplx_CustDetailVerification_Mob_No1_val", formObject.getNGValue("AlternateContactDetails_MobileNo1"));
		formObject.setNGValue("cmplx_CustDetailVerification_Mob_No2_val", formObject.getNGValue("AlternateContactDetails_MobileNo2"));
		formObject.setNGValue("cmplx_CustDetailVerification_email1_val", formObject.getNGValue("AlternateContactDetails_EMAIL1_PRI"));
		formObject.setNGValue("cmplx_CustDetailVerification_email2_val", formObject.getNGValue("AlternateContactDetails_EMAIL2_SEC"));
		formObject.setNGValue("cmplx_CustDetailVerification_dob_val", formObject.getNGValue("cmplx_Customer_DOb"));
		formObject.setNGValue("cmplx_CustDetailVerification_Resno_val", formObject.getNGValue("AlternateContactDetails_RESIDENCENO"));
		formObject.setNGValue("cmplx_CustDetailVerification_Offtelno_val", formObject.getNGValue("AlternateContactDetails_OFFICENO"));
		formObject.setNGValue("cmplx_CustDetailVerification_hcountrytelno_val", formObject.getNGValue("AlternateContactDetails_HOMECOUNTRYNO"));
		formObject.setNGValue("cmplx_CustDetailVerification_Mother_name_val", formObject.getNGValue("cmplx_Customer_MotherName"));//pcasi-1003
		// formObject.setNGValue("", formObject.getNGValue(""));
		//Below Code added for PCASI-1003
		formObject.setVisible("CustDetailVerification_Label12", false);
		formObject.setVisible("cmplx_CustDetailVerification_email2_val", false);
		formObject.setVisible("cmplx_CustDetailVerification_email2_ver", false);
		formObject.setVisible("cmplx_CustDetailVerification_email2_upd", false);
		//Above code added for PCASI
		

		if(NGFUserResourceMgr_PL.getGlobalVar("PL_IM").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2)) || 
				NGFUserResourceMgr_PL.getGlobalVar("PL_InstantMoney").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2)) ||
				NGFUserResourceMgr_PL.getGlobalVar("PL_BTC").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2)) || 
				NGFUserResourceMgr_PL.getGlobalVar("PL_BusinessTitaniumCard").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2))){

			formObject.setEnabled("cmplx_CustDetailVerification_Mob_No1_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_Mob_No2_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_email1_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_email2_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_dob_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_POBoxNo_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_emirates_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_persorcompPOBox_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_Offtelno_val",false);
			formObject.setLocked("cmplx_CustDetailVerification_dob_upd",true);
			formObject.setEnabled("cmplx_CustDetailVerification_dob_upd",false);

			// hide
			formObject.setVisible("CustDetailVerification_Label17", false);
			formObject.setVisible("cmplx_CustDetailVerification_Resno_val", false);
			formObject.setEnabled("cmplx_CustDetailVerification_resno_ver",false);

			formObject.setVisible("cmplx_CustDetailVerification_resno_ver", false);
			formObject.setVisible("cmplx_CustDetailVerification_resno_upd", false);

			formObject.setVisible("CustDetailVerification_Label9", false);
			formObject.setVisible("cmplx_CustDetailVerification_hcountrytelno_val", false);
			formObject.setVisible("cmplx_CustDetailVerification_hcountrytelno_ver", false);
			formObject.setEnabled("cmplx_CustDetailVerification_hcountrytelno_ver",false);
			formObject.setVisible("cmplx_CustDetailVerification_hcountrytelno_upd", false);

			formObject.setVisible("CustDetailVerification_Label10", false);
			formObject.setVisible("cmplx_CustDetailVerification_hcountryaddr_val", false);
			formObject.setVisible("cmplx_CustDetailVerification_hcontryaddr_ver", false);
			formObject.setEnabled("cmplx_CustDetailVerification_hcontryaddr_ver",false);
			formObject.setVisible("cmplx_CustDetailVerification_hcountryaddr_upd", false);
			//++Below code added by nikhil 13/11/2017 for Code merge
			//Below Code added for PCASI-1003
			formObject.setVisible("CustDetailVerification_Label12", false);
			formObject.setVisible("cmplx_CustDetailVerification_email2_val", false);
			formObject.setVisible("cmplx_CustDetailVerification_email2_ver", false);
			formObject.setVisible("cmplx_CustDetailVerification_email2_upd", false);
			//Above code added for PCASI

			formObject.setTop("CustDetailVerification_Label8", 248);
			formObject.setTop("cmplx_CustDetailVerification_Offtelno_val", 248);
			formObject.setTop("cmplx_CustDetailVerification_offtelno_ver", 248);
			formObject.setTop("cmplx_CustDetailVerification_offtelno_upd", 248);

			formObject.setTop("CustDetailVerification_Label11", 312);
			formObject.setTop("cmplx_CustDetailVerification_email1_val", 312);
			formObject.setTop("cmplx_CustDetailVerification_email1_ver", 312);
			formObject.setTop("cmplx_CustDetailVerification_email1_upd", 312);

			/*formObject.setTop("CustDetailVerification_Label12", 344);
			formObject.setTop("cmplx_CustDetailVerification_email2_val", 344);
			formObject.setTop("cmplx_CustDetailVerification_email2_ver", 344);
			formObject.setTop("cmplx_CustDetailVerification_email2_upd", 344);*/

			formObject.setTop("cmplx_CustDetailVerification_resno_ver", 312);



			//--Above code added by nikhil 13/11/2017 for Code merge
		}

		else  if(NGFUserResourceMgr_PL.getGlobalVar("PL_SalariedCreditCard").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2)) || 
				NGFUserResourceMgr_PL.getGlobalVar("PL_SelfEmployedCreditCard").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2)) || NGFUserResourceMgr_PL.getGlobalVar("PL_SAL").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2))){

			formObject.setEnabled("cmplx_CustDetailVerification_Mob_No1_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_Mob_No2_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_email1_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_email2_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_dob_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_POBoxNo_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_emirates_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_persorcompPOBox_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_Offtelno_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_hcountrytelno_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_hcountryaddr_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_Resno_val",false);
			//below code added by nikhil 29/12/17 for CPV CR
			formObject.setVisible("CustDetailVerification_Label17", false);
			formObject.setVisible("cmplx_CustDetailVerification_Resno_val", false);
			formObject.setEnabled("cmplx_CustDetailVerification_resno_ver",false);
			formObject.setVisible("cmplx_CustDetailVerification_resno_ver", false);
			formObject.setVisible("cmplx_CustDetailVerification_resno_upd", false);

			formObject.setVisible("CustDetailVerification_Label9", false);
			formObject.setVisible("cmplx_CustDetailVerification_hcountrytelno_val", false);
			formObject.setVisible("cmplx_CustDetailVerification_hcountrytelno_ver", false);
			formObject.setEnabled("cmplx_CustDetailVerification_hcountrytelno_ver",false);
			formObject.setVisible("cmplx_CustDetailVerification_hcountrytelno_upd", false);

			formObject.setVisible("CustDetailVerification_Label10", false);
			formObject.setVisible("cmplx_CustDetailVerification_hcountryaddr_val", false);
			formObject.setVisible("cmplx_CustDetailVerification_hcontryaddr_ver", false);
			formObject.setEnabled("cmplx_CustDetailVerification_hcontryaddr_ver",false);
			formObject.setVisible("cmplx_CustDetailVerification_hcountryaddr_upd", false);
			formObject.setTop("CustDetailVerification_Label8", 248);
			formObject.setTop("cmplx_CustDetailVerification_Offtelno_val", 248);
			formObject.setTop("cmplx_CustDetailVerification_offtelno_ver", 248);
			formObject.setTop("cmplx_CustDetailVerification_offtelno_upd", 248);

			formObject.setTop("CustDetailVerification_Label11", 312);
			formObject.setTop("cmplx_CustDetailVerification_email1_val", 312);
			formObject.setTop("cmplx_CustDetailVerification_email1_ver", 312);
			formObject.setTop("cmplx_CustDetailVerification_email1_upd", 312);

			/*formObject.setTop("CustDetailVerification_Label12", 344);
			formObject.setTop("cmplx_CustDetailVerification_email2_val", 344);
			formObject.setTop("cmplx_CustDetailVerification_email2_ver", 344);
			formObject.setTop("cmplx_CustDetailVerification_email2_upd", 344);*/


		}
		//below code added by nikhil 18/12/17
		else
		{
			formObject.setEnabled("cmplx_CustDetailVerification_Mob_No1_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_Mob_No2_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_email1_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_email2_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_dob_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_POBoxNo_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_emirates_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_persorcompPOBox_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_Offtelno_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_hcountrytelno_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_hcountryaddr_val",false);
			formObject.setEnabled("cmplx_CustDetailVerification_Resno_val",false);

		}

		if(NGFUserResourceMgr_PL.getGlobalVar("PL_SelfEmployed").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5))){
			formObject.setLocked("cmplx_CustDetailVerification_resno_ver",true);
			formObject.setLocked("cmplx_CustDetailVerification_resno_upd",true);
			formObject.setLocked("cmplx_CustDetailVerification_hcountrytelno_ver",true);
			formObject.setLocked("cmplx_CustDetailVerification_hcountrytelno_upd",true);
			formObject.setLocked("cmplx_CustDetailVerification_hcontryaddr_ver",true);
			formObject.setLocked("cmplx_CustDetailVerification_hcountryaddr_upd",true);
		}

	}

	public void autopopulate_Compliance_risk(FormReference formObject)
	{
		formObject.setNGValue("cmplx_Compliance_CustomerName", formObject.getNGValue("CustomerLabel"));
		formObject.setNGValue("cmplx_Compliance_CifId", formObject.getNGValue("CifLabel"));
		formObject.setNGValue("cmplx_Compliance_RMName", formObject.getNGValue("RmTlNameLabel"));
		formObject.setNGValue("cmplx_Compliance_EMpType", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5));
		formObject.setNGValue("cmplx_Compliance_BussSegment", formObject.getNGValue("cmplx_RiskRating_BusinessSeg"));
		formObject.setNGValue("cmplx_Compliance_subSegment", formObject.getNGValue("cmplx_RiskRating_SubSeg"));
		formObject.setNGValue("cmplx_Compliance_demographic", formObject.getNGValue("cmplx_RiskRating_Demographics"));
		formObject.setNGValue("cmplx_Compliance_industry", formObject.getNGValue("cmplx_RiskRating_Industry"));
		formObject.setNGValue("cmplx_Compliance_riskRating", formObject.getNGValue("cmplx_RiskRating_Riskrating"));
		formObject.setNGValue("cmplx_Compliance_totRiskScore", formObject.getNGValue("cmplx_RiskRating_Total_riskScore"));
		formObject.setNGValue("cmplx_Compliance_nationality_companyDomicile", formObject.getNGValue("cmplx_Customer_Nationality"));
		formObject.setNGValue("cmplx_Compliance_product", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1));
		formObject.setNGValue("cmplx_Compliance_custCategory", formObject.getNGValue("cmplx_Customer_CustomerCategory"));
		formObject.fetchFragment("KYC", "KYC", "q_KYC");
		formObject.setNGValue("cmplx_Compliance_ExposedPerson", formObject.getNGValue("cmplx_KYC_PEP"));

	}

	//added by yash on 19/12/2017 for toteam
	public void ToTeam_AddtoGrids(FormReference formObject,String lc_doc_name,String bank_name,String bank_status,String bank_type)
	{
		int row_count=formObject.getLVWRowCount("cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate");
		String status=formObject.getNGValue("cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate", row_count-1, 7);
		if("Received".equalsIgnoreCase(status))
		{
			PersonalLoanS.mLogger.info("LC Document Name"+formObject.getNGValue("PostDisbursal_LC_LC_Doc_Name"));
			formObject.setLocked("PostDisbursal_Frame3", false);
			formObject.setLocked("PostDisbursal_MCQ_Doc_Name",true);
			formObject.setLocked("PostDisbursal_Frame6", false);
			formObject.setLocked("PostDisbursal_BG_LC_Doc_Name",true);
			formObject.setLocked("PostDisbursal_Frame4", false);
			formObject.setLocked("PostDisbursal_Frame5", true);
			formObject.setNGValue("cmplx_PostDisbursal_STLReceivedDate", "");
			formObject.setNGValue("PostDisbursal_MCQ_Doc_Name", lc_doc_name);
			formObject.setNGValue("PostDisbursal_BG_LC_Doc_Name", lc_doc_name);
			formObject.setNGValue("PostDisbursal_NLC_LC_Doc_name", lc_doc_name);
			formObject.setNGValue("PostDisbursal_NLC_Bank_Name", bank_name);
			//added by yash on 19/12/2017 for to team
			formObject.setNGValue("PostDisbursal_NLC_Bank_Type", bank_type);

			formObject.setNGValue("PostDisbursal_BG_Status", bank_status);
			formObject.setNGValue("PostDisbursal_NLC_Status", "Awaited");
			//ended by yash on 19/12/2017
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_PostDisbursal_cmplx_gr_ManagersCheque");
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_PostDisbursal_cmplx_gr_BankGuarantee");
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_PostDisbursal_cpmlx_gr_NLC");
		}


	}
	//added by byash on 22/12/2017
	public void ToTeamStl(FormReference formObject)
	{
		int row_count=formObject.getLVWRowCount("cmplx_PostDisbursal_cpmlx_gr_NLC");
		String status=formObject.getNGValue("cmplx_PostDisbursal_cpmlx_gr_NLC", row_count-1, 2);

		if("Customer".equalsIgnoreCase(status))
		{
			formObject.setLocked("PostDisbursal_Frame5", false);
		}

	}

	//added by akshay on 8/12/17 for alignment
	public void fragment_ALign(String field_list,String fragment_Name)
	{
		PersonalLoanS.mLogger.info("CC_Common"+" Inside fragment_ALign(): List of fields is-->"+field_list);
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		try{
			String field_list_array[] = field_list.split("#");
			int fieldsInARow_count=0;
			int current_top = 10;
			int current_left = 10;
			int top_diff=40;
			int left_diff = 60;
			String lable_name,field_name="";
			String reasonFlag="false";
			for(int field_list_count = 0 ;field_list_count<field_list_array.length;field_list_count++ ){
				lable_name="";

				if(field_list_array[field_list_count].split(",").length>1){
					lable_name =field_list_array[field_list_count].split(",")[0];
					field_name = field_list_array[field_list_count].split(",")[1];
					formObject.setVisible(lable_name, true);
					formObject.setVisible(field_name, true);

				}
				else{
					field_name = field_list_array[field_list_count].split("#")[0];//for checkbox or button
					formObject.setVisible(field_name, true);
				}

				if("\n".equals(field_name)){
					continue;
				}
				if(!"".equals(lable_name)){//for normal case
					formObject.setTop(lable_name, current_top);
					if((current_left+formObject.getWidth(field_name))<formObject.getWidth(fragment_Name+"_Frame1")){
						formObject.setLeft(lable_name, current_left);
					}
					formObject.setTop(field_name, (current_top+formObject.getHeight(lable_name)+2));

				}
				else{//for button or chckbox or grid when there is no label
					formObject.setTop(field_name, current_top);
				}
				if((current_left+formObject.getWidth(field_name))<formObject.getWidth(fragment_Name+"_Frame1")){
					formObject.setLeft(field_name, current_left);
					if(!"".equals(lable_name) && formObject.getWidth(lable_name)>formObject.getWidth(field_name)){//if width of label is more
						current_left = formObject.getLeft(lable_name)+formObject.getWidth(lable_name)+left_diff;
					}
					else{
						current_left = formObject.getLeft(field_name)+formObject.getWidth(field_name)+left_diff;
					}
				}
				if(field_name.equalsIgnoreCase("DecisionHistory_DecisionReasonCode")){
					reasonFlag="true";
				}

				fieldsInARow_count++;
				if(field_list_count<field_list_array.length-1){//check if next element exist as checking (i+1)th element---to avoid arrayOutOfBOund exception
					if((fieldsInARow_count)/5>0 || "\n".equals(field_list_array[field_list_count+1]) || (current_left+formObject.getWidth(field_name))>formObject.getWidth("DecisionHistory_Frame1")){
						PersonalLoanS.mLogger.info("***********Inside /5 IF***********"+(fieldsInARow_count)/4);
						if(reasonFlag.equalsIgnoreCase("true"))
							current_top=formObject.getTop(field_name)+formObject.getHeight(field_name)+top_diff+5;
						else
							current_top=formObject.getTop(field_name)+formObject.getHeight(field_name)+top_diff;

						reasonFlag="false";
						current_left=10;
						fieldsInARow_count=0;
					}
				}
				PersonalLoanS.mLogger.info("**********************");

			}    
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("Exception Inside fragment_Align()");
			printException(e);

		}
	}
	//ended by akshay on 8/12/17 for alignment

	public void	loadInExtBlacklistGrid(FormReference formObject){

		//String cust_name=formObject.getNGValue("CustomerLabel");
		String query="Select a.customername,a.passportNo, b.custid,b.BlacklistFlag,b.BlacklistDate,b.BlacklistReasonCode from ng_pl_exttable a JOIN ng_rlos_cif_detail b on  a.parent_WIName=b.cif_wi_name WHERE b.cif_wi_name='"+formObject.getNGValue("parent_WIName")+"' and cif_SearchType = 'External'";

		PersonalLoanS.mLogger.info("Inside loadInExtBlacklistGrid()--Query is: "+query);
		List<List<String>> outputList=formObject.getNGDataFromDataCache(query);
		PersonalLoanS.mLogger.info("Ext Blacklist Output List is:"+outputList);
		for(List<String> a:outputList){
			formObject.addItemFromList("cmplx_ExtBlackList_cmplx_gr_ExtBlackList",a);
		}
	}

	public void fetch_CardDetails_frag(FormReference formObject){
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(n==0){
			formObject.setNGFrameState("Card_Details",1);
			throw new ValidatorException(new FacesMessage("cmplx_Product_cmplx_ProductGrid#Please Add a product first"));
		}
		if(n>0){
			formObject.fetchFragment("Card_Details", "CardDetails", "q_CardDetails");
			formObject.setNGFrameState("Card_Details", 0);
			
			IslamicFieldsvisibility();
			if(NGFUserResourceMgr_PL.getGlobalVar("PL_SelfEmployed").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5)) ){
				formObject.setEnabled("cmplx_CardDetails_compemboss", true);
			}
			else{
				formObject.setNGValue("cmplx_CardDetails_compemboss","");
				formObject.setEnabled("cmplx_CardDetails_compemboss", false);
			}
			if("Yes".equalsIgnoreCase(formObject.getNGValue("cmplx_CardDetails_suppcardreq"))){
				PersonalLoanS.mLogger.info("Is Supp req is yes: ");
				formObject.setVisible("Supplementary_Card_Details",true);
				if(formObject.getWFActivityName().equalsIgnoreCase("DDVT_Checker") || formObject.getWFActivityName().equalsIgnoreCase("Disbursal")){
					PersonalLoanS.mLogger.info("activity is checker: ");
					formObject.fetchFragment("Supplementary_Card_Details", "SupplementCardDetails", "q_SuppCardDetails");
					formObject.setNGFrameState("Supplementary_Card_Details",0);
					loadPicklist_suppCard();
					//adjustFrameTops("Address_Details_container,,ReferenceDetails,Card_Details,Supplementary_Card_Details,FATCA,KYC,OECD");
					//adjustFrameTops("Card_Details,Supplementary_Card_Details,FATCA,KYC,OECD,ReferenceDetails");
				}

			}
			alignDemographiTab(formObject);
			//cmplx_CardDetails_SelfSupp_required
		}


		//change by saurabh on 4th Jan
		//loadDataInCRNGrid();
		if(!"Y".equalsIgnoreCase(formObject.getNGValue("is_cc_waiver_require")))
		{
			formObject.setVisible("CardDetails_Label2",false);
			formObject.setVisible("cmplx_CardDetails_compemboss",false);
			formObject.setVisible("CardDetails_Label12",true);//added by akshay for proc 12174
			formObject.setVisible("cmplx_CardDetails_MarketCode",true);
		}
		//if("Islamic".equalsIgnoreCase(arg0))

	}


	public void loadPicklist_suppCard(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		LoadPickList("SupplementCardDetails_Nationality", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
		LoadPickList("SupplementCardDetails_Gender", "select '--Select--' as description,'' as code union select convert(varchar, description) as description,Code from NG_MASTER_gender with (nolock) order by code");
		LoadPickList("SupplementCardDetails_ResidentCountry", "select '--Select--'  as description,'' as code union select convert(varchar, description) ,code from NG_MASTER_Country with (nolock) order by code");
		LoadPickList("SupplementCardDetails_Relationship", "select '--Select--'  as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Relationship with (nolock) order by code");
		LoadPickList("SupplementCardDetails_MarketingCode", "select '--Select--'  as description,'' as code  union select convert(varchar, description),code from NG_MASTER_MarketingCode with (nolock) where isActive='Y'   order by code");
		LoadPickList("SupplementCardDetails_Title", "select '--Select--'  as description,'' as code  union select convert(varchar, description),code from NG_MASTER_title with (nolock) order by code");
		LoadPickList("SupplementCardDetails_Designation", "select '--Select--'  as description,'' as code  union select convert(varchar, description),code from NG_MASTER_designation with (nolock) order by code");
		//LoadPickList("SupplementCardDetails_CardProduct","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cardProduct with (nolock) order by code");
		LoadPickList("SupplementCardDetails_EMploymentType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_EmploymentType with (nolock) where isactive='Y' order by code");
		LoadPickList("SupplementCardDetails_maritalStatus", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_Maritalstatus with (nolock) where isactive='Y' order by code");
		LoadPickList("SupplementCardDetails_EmploymentStatus", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_EmploymentStatus with (nolock) order by code");
		//complex query to load supplementary card products by saurabh
		LoadPickList("SupplementCardDetails_CardProduct", "SELECT CASE WHEN temptble.Description!='--Select--' then concat(temptble.Description,'(',temptble.final_limit,')') else temptble.Description end,temptble.Description FROM (SELECT '--Select--' as Description,'' as final_limit union SELECT distinct card_product as Description,final_limit FROM ng_rlos_IGR_Eligibility_CardLimit WITH (nolock) WHERE child_wi = '"+formObject.getWFWorkitemName()+"' AND cardproductselect = 'true') as temptble order by case when Description='--Select--' then 0 else 1 end");

	}
	//added by saurabh for Active/inactive status for supplementary.
	public void validateStatusForSupplementary(){
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			if(formObject.isVisible("SupplementCardDetails_Frame1")){
				int supplementaryRows = formObject.getLVWRowCount("SupplementCardDetails_cmplx_supplementGrid");
				PersonalLoanS.mLogger.info("RLOS supplementary row count: "+supplementaryRows);
			//	Map<Integer, String> suppGridCardsSelected = new HashMap<Integer, String>();
				if(supplementaryRows>0){
					List<String>selectedCardProds = new ArrayList<String>();
					String query = "SELECT distinct card_product FROM ng_rlos_IGR_Eligibility_CardLimit WITH (nolock) WHERE child_wi='"+formObject.getWFWorkitemName()+"' AND cardproductselect = 'true'";
					PersonalLoanS.mLogger.info("RLOS supplementary query: "+query);
					List<List<String>> records = formObject.getDataFromDataSource(query);
					PersonalLoanS.mLogger.info("RLOS supplementary records: "+records);
					if(records!=null && records.size()>0){
						if(records.get(0) != null){
							for(List<String> cardProd : records){
								selectedCardProds.add(cardProd.get(0));
							}
						}
					}
					PersonalLoanS.mLogger.info("RLOS supplementary selected card prods: "+selectedCardProds);
					for(int j=0;j<supplementaryRows;j++){
						PersonalLoanS.mLogger.info("value for cardproduct for row: "+j+" is: "+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid", j, 30));
						if(selectedCardProds.contains(formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",j,30))){
							//suppGridCardsSelected.put(j, "Active");
							formObject.setNGValue("SupplementCardDetails_cmplx_supplementGrid", j, 42, "Active");
						}
						else{
							formObject.setNGValue("SupplementCardDetails_cmplx_supplementGrid", j, 42, "InActive");
						}
						PersonalLoanS.mLogger.info("value for status for row: "+j+" is: "+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid", j, 42));
					}
				}
			}
		}catch(Exception e)
		{
			PersonalLoanS.mLogger.info( "Exception occurred in validateStatusForSupplementary function:"+e.getMessage());
			printException(e);
		}
	}
	//added by akshay on 9/1/17
	public void adjustFrameTops(String frameList){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try{
			String[] framelist_array=frameList.split(",");
			if(framelist_array.length<2)
				return;

			for(int i=1;i<framelist_array.length;i++){

				PersonalLoanS.mLogger.info("Frame state for "+framelist_array[i]+": "+formObject.getNGFrameState(framelist_array[i]));
				if(formObject.getNGFrameState(framelist_array[i-1])==0 || formObject.getNGFrameState(framelist_array[i-1])==-1 ){//frame expanded
					formObject.setTop(framelist_array[i], formObject.getTop(framelist_array[i-1])+formObject.getHeight(framelist_array[i-1])+30);
				}
				else{//frame not expanded
					formObject.setTop(framelist_array[i], formObject.getTop(framelist_array[i-1])+30);
				}
			}
		}catch(Exception e){
			PersonalLoanS.mLogger.info("Exception occurred in adjustFrameTops()"+e.getMessage());
			printException(e);
		}
	}

	//added By Tarang for drop 4 point 5 started on 20/02/2018
	public void  loadPicklistDisbursal()
	{
		try
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
			String ActivityName=formObject.getWFActivityName();
			PersonalLoanS.mLogger.info("PL"+"Inside PLCommon ->loadPicklistDisbursal()"+ActivityName);

			LoadPickList("cmplx_Decision_Decision", "select * from (select '--Select--' as decision union select decision from NG_MASTER_Decision with (nolock)  where ProcessName='PersonalLoanS' and workstepname='"+ActivityName+"' and Initiation_Type is NULL )t order by case when decision = '--Select--' then 0 else 1 end");
		}
		catch(Exception e){ 
			PersonalLoanS.mLogger.info("PLCommon"+"Exception Occurred loadPicklistDisbursal :"+e.getMessage());
			printException(e);
		}    
	} 
	//added By Tarang for drop 4 point 5 ended on 20/02/2018

	public void checkForDeferredWaivedDocs(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try{
			PersonalLoanS.mLogger.info("Before Defining repeater object: ");
			IRepeater repObj= formObject.getRepeaterControl("IncomingDoc_Frame2");
			PersonalLoanS.mLogger.info("repObj: "+repObj);
			int repRows = repObj.getRepeaterRowCount();
			PersonalLoanS.mLogger.info("repRows: "+repRows);
			if(repRows>0){
				String defdocs = "";
				String waivDocs = "";
				int defdocs_count=1,waivDocs_count=1;
				boolean setWaiverExtTableFlag = false;
				boolean setDeferExtTableFlag = false;
				for(int i=0;i<repRows;i++){
					String status = repObj.getValue(i, "cmplx_DocName_Doc_Status");
					if("Deferred".equalsIgnoreCase(status)){
						/*  if(i==repRows-1){
	 				   defdocs+=defdocs_count+". "+repObj.getValue(i, "cmplx_DocName_DocName")+"<br>";
	 				   }
	 				   else{
	 					   defdocs+=repObj.getValue(i, "cmplx_DocName_DocName")+",";
	 				   }*/
						defdocs+=String.valueOf(defdocs_count++)+". "+repObj.getValue(i, "cmplx_DocName_DocName")+"<br>";
						setDeferExtTableFlag = true;
					}
					else if("Waived".equalsIgnoreCase(status)){
						/* if(i==repRows-1){
	 					   waivDocs+=repObj.getValue(i, "cmplx_DocName_DocName");
		    				}
		    				else{
		    					waivDocs+=repObj.getValue(i, "cmplx_DocName_DocName")+",";
		    				}*/
						waivDocs+=String.valueOf(waivDocs_count++)+". "+repObj.getValue(i, "cmplx_DocName_DocName")+"<br>";
						setWaiverExtTableFlag = true;
					}
				}
				formObject.setNGValue("DeferredDocsList", defdocs);
				formObject.setNGValue("waivedDocsList", waivDocs);
				if(setDeferExtTableFlag){
					formObject.setNGValue("Deferral_req", "Y");
				}
				if(setWaiverExtTableFlag){
					formObject.setNGValue("Waiver_req", "Y");
				}
			}
		}catch(Exception ex){
			PersonalLoanS.mLogger.info("Exception in checkForDeferredWaivedDocs function: ");
			printException(ex);

		}
	}

	public String getCurrentAccountNumber(String callname){
		PersonalLoanS.mLogger.info("outside current Acc");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String acc_no= formObject.getNGValue("cmplx_Decision_AccountNo");
		String currAccflag="N";
		String Account_Number=formObject.getNGValue("Account_Number");
		PersonalLoanS.mLogger.info("current Acc"+ Account_Number);
		if(acc_no==null || acc_no.equals("")){
			acc_no=Account_Number;
			currAccflag="Y";
		}
		PersonalLoanS.mLogger.info("current Acc IF accno.con"+acc_no);
		if(acc_no.contains(",")){
			
			String newFundingAccountNo=formObject.getNGValue("Funding_Account_no");
			acc_no=newFundingAccountNo;
			PersonalLoanS.mLogger.info("current Acc IF accno.con"+acc_no);
		} else if("".equalsIgnoreCase(formObject.getNGValue("Funding_Account_no")) ){
			formObject.setNGValue("Funding_Account_no",acc_no);
		}
		PersonalLoanS.mLogger.info("current Acc  ELSEIF accno.con"+acc_no);
		/*if(acc_no == null || "".equalsIgnoreCase(acc_no )){
		String qry = "select AcctId from ng_RLOS_CUSTEXPOSE_AcctDetails where AcctType like '%CURRENT ACCOUNT%' and ( Child_Wi = '"+formObject.getWFWorkitemName()+"' or Wi_Name ='"+formObject.getWFWorkitemName()+"') and FundingAccount='true'";
		PersonalLoanS.mLogger.info("query is"+qry +"");
		List<List<String>> record = formObject.getDataFromDataSource(qry);
		if(record!=null && !record.isEmpty()){
			acc_no = record.get(0).get(0);
			currAccflag="Y";
		}
		}*/
		if(callname.equals("ENTITY_MAINTENANCE_REQ")){
			PersonalLoanS.mLogger.info("IF callname"+acc_no);
			return acc_no+"!"+currAccflag;
		}
		PersonalLoanS.mLogger.info("current Acc  ELSEIF accno.con"+acc_no);
		return acc_no;

	}

	public void IslamicFieldsvisibility() {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String query = "select distinct limit.Card_Product,limit.CC_Waiver,mstr.ReqProduct from ng_rlos_IGR_Eligibility_CardLimit limit with(nolock) join ng_master_cardProduct mstr with(nolock) on limit.Card_Product=mstr.CODE  where child_wi = '"+formObject.getWFWorkitemName()+"' and Cardproductselect='true'";
		List<List<String>> records = formObject.getDataFromDataSource(query);
		PersonalLoanS.mLogger.info("query produced is:"+query);
		try{
			boolean cc_waiver = false;
			boolean islamic_visible = false;
			boolean islamic_mandatory = false;
			boolean kalyanCompany = false;
			if(records!=null && records.size()>0){
				PersonalLoanS.mLogger.info("records list is:"+records);
				for(List<String> row:records){
					PersonalLoanS.mLogger.info("row list is:"+records);
					if( row.get(2)!=null && row.get(2).equalsIgnoreCase("Islamic")  && !row.get(2).contains("LOC")){
						islamic_visible=true;
						islamic_mandatory =true;
					}
					else if( row.get(0)!=null && row.get(0).contains("KALYAN")){
						kalyanCompany = true;
					}
					if(row.get(1)!=null  && row.get(1).equalsIgnoreCase("true")){
						cc_waiver =true;
					}
				}
			}
			if(islamic_visible){
				formObject.setVisible("CardDetails_Label3", true);
				formObject.setVisible("cmplx_CardDetails_charityorg", true);
				formObject.setVisible("CardDetails_Label4", true);
				formObject.setVisible("cmplx_CardDetails_charityamt", true);
				formObject.setLeft("CardDetails_Label5", 1059);
				formObject.setLeft("cmplx_CardDetails_SuppCardReq", 1059);
			}
			else
			{
				formObject.setVisible("CardDetails_Label3", false);
				formObject.setVisible("cmplx_CardDetails_charityorg", false);
				formObject.setVisible("CardDetails_Label4", false);
				formObject.setVisible("cmplx_CardDetails_charityamt", false);

			}
			if(islamic_mandatory){
				formObject.setNGValue("CardDetails_Islamic_mandatory","Y");
			}
			if(kalyanCompany){
				formObject.setVisible("CardDetails_Label2", true);
				formObject.setVisible("cmplx_CardDetails_compemboss", true);
			}
			/*if(!cc_waiver){
				formObject.setVisible("CardDetails_Label7", true);
				formObject.setVisible("cmplx_CardDetails_statCycle", true);
			}*/
		}catch(Exception ex){
			PersonalLoanS.mLogger.info( "Exception in IslamicFieldsVisibility Function");
			printException(ex);
		}
	}

	public void  loadInRejectEnq(FormReference formObject)
	{
		try{
			if(formObject.getLVWRowCount("cmplx_RejectEnq_RejectEnqGrid")==0){
				String customer_cif=formObject.getNGValue("cmplx_Customer_CIFNO");
				String DOB=formObject.getNGValue("cmplx_Customer_DOb");
				String passport=formObject.getNGValue("cmplx_Customer_PAssportNo");
				String mobNo1 = formObject.getNGValue("AlternateContactDetails_MobileNo1");

				String queryForRejectCases="select isnull(customer_name,''),isnull(employer,''),passport_No,FORMAT(upload_date,'dd/MM/yyyy'),isnull(category,''),remarks from ng_cas_rejected_table with(nolock) where reject_system='CAS' and (cif='"+customer_cif+"'or (Passport_No='"+passport+"' and mobile='"+mobNo1+"' and FORMAT(dob,'dd/MM/yyyy')='"+DOB+"'))";
				mLogger.info("queryForRejectCases: "+queryForRejectCases);
				List<List<String>> records=formObject.getDataFromDataSource(queryForRejectCases);
				mLogger.info("Query data: "+records);

				if(records.size()>0){
					for(List<String> record:records){
						record.add(formObject.getWFWorkitemName());
						formObject.addItemFromList("cmplx_RejectEnq_RejectEnqGrid",record);
					}
				}
			}	
		}catch(Exception e)
		{
			mLogger.info(e.getMessage());
			printException(e);
		}
	}
	public void  loadInPersonalLoanSEnq(FormReference formObject)
	{
		try{
			if(formObject.getLVWRowCount("cmplx_CCenq_cmplx_GR_CCenq")==0){
				String customer_cif=formObject.getNGValue("cmplx_Customer_CIFNO");
				String DOB=formObject.getNGValue("cmplx_Customer_DOb");
				String passport=formObject.getNGValue("cmplx_Customer_PAssportNo");
				String mobNo1 = formObject.getNGValue("AlternateContactDetails_MobileNo1");

				String query=" select CRN,isnull(cif,''),Customer_ref_No,isnull(Account_No,''),isnull(Card_Product,''),Status,'',Decline_Reason,FORMAT(Declined_Date,'dd/MM/yyyy') from ng_cas_rejected_table where reject_system='CAPS' and (cif='"+customer_cif+"'or (Passport_No='"+passport+"' and mobile='"+mobNo1+"' and FORMAT(dob,'dd/MM/yyyy')='"+DOB+"'))";
				mLogger.info("queryFor cc Enquiry "+query);
				List<List<String>> records=formObject.getDataFromDataSource(query);
				mLogger.info("Query data: "+records);

				if(records.size()>0){
					for(List<String> record:records){
						record.add(formObject.getWFWorkitemName());
						formObject.addItemFromList("cmplx_CCenq_cmplx_GR_CCenq",record);
					}
				}
			}
		}catch(Exception e)
		{
			mLogger.info(e.getMessage());
			printException(e);
		}
	}

	public void  loadInLOS(FormReference formObject)
	{
		try{
			PersonalLoanS.mLogger.info("inside get LOS details function");
			PersonalLoanS.mLogger.info("formObject.getLVWRowCount('cmplx_LOS_cmplx_GR_LOS'): "+formObject.getLVWRowCount("cmplx_LOS_cmplx_GR_LOS"));
			if(formObject.getLVWRowCount("cmplx_LOS_cmplx_GR_LOS")==0){

				String customer_cif=formObject.getNGValue("cmplx_Customer_CIFNO");
				String DOB=formObject.getNGValue("cmplx_Customer_DOb");
				String passport=formObject.getNGValue("cmplx_Customer_PAssportNo");
				String mobNo1 = formObject.getNGValue("AlternateContactDetails_MobileNo1");
				String query="  select isnull(cif,''),isnull(customer_name,''),isnull(Agreement_ID,''),Decline_Reason,remarks,FORMAT(Declined_Date,'dd/MM/yyyy')  from ng_cas_rejected_table where reject_system='LOS' and (cif='"+customer_cif+"'or (Passport_No='"+passport+"' and mobile='"+mobNo1+"' and FORMAT(dob,'dd/MM/yyyy')='"+DOB+"'))";
				PersonalLoanS.mLogger.info("queryFor  LOS "+query);
				List<List<String>> records=formObject.getDataFromDataSource(query);
				PersonalLoanS.mLogger.info("Query data: "+records);

				if(records.size()>0){
					for(List<String> record:records){
						record.add(formObject.getWFWorkitemName());
						formObject.addItemFromList("cmplx_LOS_cmplx_GR_LOS",record);
					}
				}
			}
		}catch(Exception e)
		{
			PersonalLoanS.mLogger.info(e.getMessage());
			printException(e);
		}
	}

	public void  loadInCaseHistoryReport(FormReference formObject)
	{
		try{
			if(formObject.getLVWRowCount("cmplx_CaseHist_cmplx_GR_casehist")==0){

				String customer_cif=formObject.getNGValue("cmplx_Customer_CIFNO");
				String DOB=formObject.getNGValue("cmplx_Customer_DOb");
				String passport=formObject.getNGValue("cmplx_Customer_PAssportNo");
				String mobNo1 = formObject.getNGValue("AlternateContactDetails_MobileNo1");

				String query=" select '',FORMAT(Submission_Date,'dd/MM/yyyy'),RM_Name,status,isnull(Agreement_ID,''),TL_Emirate,remarks,FORMAT(Declined_Date,'dd/MM/yyyy'),Decline_Reason from ng_cas_rejected_table where reject_system='ICIS' and (cif='"+customer_cif+"'or (Passport_No='"+passport+"' and mobile='"+mobNo1+"' and FORMAT(dob,'dd/MM/yyyy')='"+DOB+"'))";
				mLogger.info("queryFor case history check "+query);
				List<List<String>> records=formObject.getDataFromDataSource(query);
				mLogger.info("Query data: "+records);

				if(records.size()>0){
					for(List<String> record:records){
						record.add(formObject.getWFWorkitemName());
						formObject.addItemFromList("cmplx_CaseHist_cmplx_GR_casehist",record);
					}
				}
			}
		}catch(Exception e)
		{
			mLogger.info(e.getMessage());
			printException(e);
		}
	}

	public void loadInAutoLoanGrid(FormReference formObject)
	{
		try{
			if(formObject.getLVWRowCount("cmplx_FinacleCRMIncident_FinCRMGrid")==0){

				String customer_cif=formObject.getNGValue("cmplx_Customer_CIFNO");

				String query="select distinct Incident_Case_ID,DSA_Name,Team_Leader,No_of_loans,Loan_Amount,Status,Pending_Remarks,Other_Remarks,Decline_Remarks from ng_RLOS_SR_IPP_OFFLINE with(nolock) where CIF_ID='"+customer_cif+"' and status='DEC'";
				mLogger.info("queryFor sr ipp AL decline cases "+query);
				List<List<String>> records=formObject.getDataFromDataSource(query);
				mLogger.info("Query data: "+records);

				if(records.size()>0){
					for(List<String> record:records){
						record.add(formObject.getWFWorkitemName());
						formObject.addItemFromList("cmplx_FinacleCRMIncident_FinCRMGrid",record);
					}
				}
			}
		}catch(Exception e)
		{
			mLogger.info(e.getMessage());
			printException(e);
		}
	}

	public String  getRejectedDetails(){
		mLogger.info( "inside getCustAddress_details : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "SELECT Rejected_cust,Rejected_Date,Rejected_reason,Rejected_product,Rejected_app_id FROM Ng_Rlos_Dectech_Rejected_App_Data  with (nolock) where Wi_Name = '"+formObject.getWFWorkitemName()+"'";
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		for (int i = 0; i<OutputXML.size();i++){
			String Rejected_cust = "";
			String Rejected_Date = "";
			String Rejected_reason = "";
			String Rejected_product = ""; 
			String Rejected_app_id = "";
			if(!(OutputXML.get(i).get(0) == null || "".equalsIgnoreCase(OutputXML.get(i).get(0))) ){
				Rejected_cust = OutputXML.get(i).get(0);
			}
			if(!(OutputXML.get(i).get(1) == null || "".equalsIgnoreCase(OutputXML.get(i).get(1))) ){
				Rejected_Date = OutputXML.get(i).get(1);
			}
			if(!(OutputXML.get(i).get(2) == null || "".equalsIgnoreCase(OutputXML.get(i).get(2))) ){
				Rejected_reason = OutputXML.get(i).get(2);
			}
			if(!(OutputXML.get(i).get(3) == null || "".equalsIgnoreCase(OutputXML.get(i).get(3))) ){
				Rejected_product = OutputXML.get(i).get(3);
			}
			if(!(OutputXML.get(i).get(4) == null || "".equalsIgnoreCase(OutputXML.get(i).get(4))) ){
				Rejected_app_id = OutputXML.get(i).get(4);
			}
			add_xml_str = add_xml_str + "<RejectedDetails><rejected_cust>"+Rejected_cust+"</rejected_cust>";
			add_xml_str = add_xml_str + "<rejected_date>"+Rejected_Date+"</rejected_date>";
			add_xml_str = add_xml_str + "<rejected_reason>"+Rejected_reason+"</rejected_reason>";
			add_xml_str = add_xml_str + "<rejected_product>"+Rejected_product+"</rejected_product>";
			add_xml_str = add_xml_str + "<rejected_app_id>"+Rejected_app_id+"</rejected_app_id></RejectedDetails>";
		}
		mLogger.info( "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}

	public String fetch_cust_details_supplementary(){
		String outputResponse="";
		String ReturnCode="";
		String alert_msg="";
		PL_Integration_Input genX=new PL_Integration_Input();
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			outputResponse = genX.GenerateXML("CUSTOMER_DETAILS","Supplementary_Card_Details");
			PersonalLoanS.mLogger.info("Inside Customer");
			/*String Gender =  (outputResponse.contains("<Gender>")) ? outputResponse.substring(outputResponse.indexOf("<Gender>")+"</Gender>".length()-1,outputResponse.indexOf("</Gender>")):"";
				SKLogger.writeLog("RLOS value of Gender",Gender);
				String  Marital_Status =  (outputResponse.contains("<MaritialStatus>")) ? outputResponse.substring(outputResponse.indexOf("<MaritialStatus>")+"</MaritialStatus>".length()-1,outputResponse.indexOf("</MaritialStatus>")):"";
				SKLogger.writeLog("RLOS value of Marital_Status",Marital_Status);*/
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			PersonalLoanS.mLogger.info(ReturnCode);

			if("0000".equalsIgnoreCase(ReturnCode) ){
				formObject.clear("SupplementCardDetails_AddressList");
				formObject.clear("SupplementCardDetails_FatcaList");
				formObject.clear("SupplementCardDetails_KYCList");
				formObject.clear("SupplementCardDetails_OecdList");
				new PL_Integration_Output().valueSetIntegration(outputResponse,"Supplementary_Card_Details");
				alert_msg=NGFUserResourceMgr_PL.getAlert("val082");
				formObject.setLocked("SupplementCardDetails_FetchDetails", true);

				String dob=formObject.getNGValue("SupplementCardDetails_DOB");
				String str_IDissuedate=formObject.getNGValue("SupplementCardDetails_IDIssueDate");
				String str_PassIssDate=formObject.getNGValue("SupplementCardDetails_PassportIssueDate");
				String str_VisaIssDate=formObject.getNGValue("SupplementCardDetails_VisaIssueDate");
				String str_passexpiry=formObject.getNGValue("SupplementCardDetails_PassportExpiry");
				String str_Visaexpiry=formObject.getNGValue("SupplementCardDetails_VisaExpiry");
				String str_EIDexpiry=formObject.getNGValue("SupplementCardDetails_EmiratesIDExpiry");

				if(dob!=null && !"".equalsIgnoreCase(dob)){
					formObject.setNGValue("SupplementCardDetails_DOB",Convert_dateFormat(dob, "yyyy-mm-dd", "dd/mm/yyyy"),false);
				}
				if(str_VisaIssDate!=null&&!"".equalsIgnoreCase(str_VisaIssDate)){
					formObject.setNGValue("SupplementCardDetails_VisaIssueDate",Convert_dateFormat(str_VisaIssDate, "yyyy-mm-dd", "dd/mm/yyyy"),false);
				}
				if(str_PassIssDate!=null&&!"".equalsIgnoreCase(str_PassIssDate)){
					formObject.setNGValue("SupplementCardDetails_PassportIssueDate",Convert_dateFormat(str_PassIssDate, "yyyy-mm-dd", "dd/mm/yyyy"),false);
				}
				if(str_IDissuedate!=null&&!"".equalsIgnoreCase(str_IDissuedate)){
					formObject.setNGValue("SupplementCardDetails_IDIssueDate",Convert_dateFormat(str_IDissuedate, "yyyy-mm-dd", "dd/mm/yyyy"),false);
				}
				if(str_passexpiry!=null&&!"".equalsIgnoreCase(str_passexpiry)){
					formObject.setNGValue("SupplementCardDetails_PassportExpiry",Convert_dateFormat(str_passexpiry, "yyyy-mm-dd", "dd/mm/yyyy"),false);
				}
				if(str_Visaexpiry!=null&&!"".equalsIgnoreCase(str_Visaexpiry)){
					formObject.setNGValue("SupplementCardDetails_VisaExpiry",Convert_dateFormat(str_Visaexpiry, "yyyy-mm-dd", "dd/mm/yyyy"),false);
				}
				if(str_EIDexpiry!=null&&!"".equalsIgnoreCase(str_EIDexpiry)){
					formObject.setNGValue("SupplementCardDetails_EmiratesIDExpiry",Convert_dateFormat(str_EIDexpiry, "yyyy-mm-dd", "dd/mm/yyyy"),false);
				}
			}
			else{
				alert_msg=NGFUserResourceMgr_PL.getAlert("val029");
			}

		}catch(Exception ex){
			printException(ex);
			if("".equalsIgnoreCase(alert_msg)){
				alert_msg=NGFUserResourceMgr_PL.getAlert("val029");
			}
		}
		return alert_msg;
	} 

	public static void clearSelectiveRows(String operationName,String grid_control){
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String firstName ;
			String lastName ;
			String match="";
			if(operationName.equalsIgnoreCase("Supplementary_Card_Details")){
				firstName=formObject.getNGValue("FirstName");
				lastName=formObject.getNGValue("lastname");
				String passport = formObject.getNGValue("passportNo");
				PersonalLoanS.mLogger.info("firstname is:"+firstName);
				PersonalLoanS.mLogger.info("lastName is:"+lastName);
				PersonalLoanS.mLogger.info("passport is:"+passport);
				match = "S-"+firstName+" "+lastName+"-"+passport;

			}
			else if(operationName.equalsIgnoreCase("Guarantor_CIF")){
				firstName=formObject.getNGValue("GuarantorDetails_Fname");
				lastName=formObject.getNGValue("GuarantorDetails_lname");
				match = "G-"+firstName+" "+lastName;
			}
			PersonalLoanS.mLogger.info("	"+match);
			UIComponent pComp =formObject.getComponent(grid_control);
			int columns=0;
			if( pComp != null && pComp instanceof ListView )
			{			
				ListView objListView = ( ListView )pComp;
				columns = objListView.getChildCount();
			}
			if(columns>0){
				int rowcount = formObject.getLVWRowCount(grid_control);
				int deleteCount = 0;
				for(int i=0;i<rowcount;i++){
					String appTypeinGrid = formObject.getNGValue(grid_control,(i-deleteCount),(columns-1));
					PersonalLoanS.mLogger.info("appTypeinGrid is:"+appTypeinGrid);
					if(match.equalsIgnoreCase(appTypeinGrid)){
						formObject.setSelectedIndex(grid_control, (i-deleteCount));
						PersonalLoanS.mLogger.info("selectedIndex is:"+formObject.getSelectedIndex(grid_control));
						formObject.ExecuteExternalCommand("NGDeleteRow",grid_control);
						deleteCount++;
						PersonalLoanS.mLogger.info("selectedIndex after deleting is:"+formObject.getSelectedIndex(grid_control));
					}
				}
			}
		}
		catch(Exception ex){
			PersonalLoanS.mLogger.info("Exception in clearSelectiveRows function");
			printException(ex);

		}
	}

	public void AddFromHiddenList(String hiddenfieldName,String GridName){
		PersonalLoanS.mLogger.info("inside AddFromHiddenList"+hiddenfieldName);
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			PersonalLoanS.mLogger.info("inside AddFromHiddenList field value:"+formObject.getNGValue(hiddenfieldName));
			String ListTobeAdded=formObject.getNGValue(hiddenfieldName);
			UIComponent pComp=formObject.getComponent(GridName);
			//mLogger.info("ListTobeAdded1: "+ListTobeAdded);
			if( pComp != null && pComp instanceof ListView )
			{
				//mLogger.info("ListTobeAdded2: "+ListTobeAdded);
				if(!ListTobeAdded.equals("") && !ListTobeAdded.equals("#")){
					//mLogger.info("ListTobeAdded3: "+ListTobeAdded);
					String[] ListTobeAdded_array=ListTobeAdded.split("#");
					for(int i=0;i<ListTobeAdded_array.length;i++){
						List<String> rowTobeAdded = Arrays.asList(ListTobeAdded_array[i].substring(0, ListTobeAdded_array[i].length()).split("~cas_sep~"));
						Clean(rowTobeAdded);
						//mLogger.info("rowTobeAdded: "+rowTobeAdded+rowTobeAdded.size());

						for(int j=0;j<rowTobeAdded.size();j++){
							if(!((Column)(pComp.getChildren().get(j))).getFormat().equals("")){
								rowTobeAdded.set(j, "");
							}

						}
						//mLogger.info("finalList: "+ rowTobeAdded);
						formObject.addItemFromList(GridName, rowTobeAdded);
					}
					formObject.clear(hiddenfieldName);
				}
			}
		}
		catch(Exception ex){
			PersonalLoanS.mLogger.info("Exception in AddFromHiddenList function"+ex.getMessage());
			printException(ex);

		}
	}

	public List<String> Clean(List<String> input){
		for(String s:input){
			String temp=s.trim();
			input.set(input.indexOf(s), temp);
		}
		return input;
	}



	public static void loadDynamicPickList()
	{
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			PersonalLoanS.mLogger.info("inside loadDynamicPickList");
			List<String> controls= Arrays.asList("AddressDetails_CustomerType","cmplx_FATCA_CustomerType","KYC_CustomerType","OECD_CustomerType");
			for(String controlName:controls){
				formObject.clear(controlName);
				formObject.addItem(controlName,"--Select--");
				String pFname = formObject.getNGValue("cmplx_Customer_FIrstNAme");
				String pLname = formObject.getNGValue("cmplx_Customer_LAstNAme");
				String pPass = formObject.getNGValue("cmplx_Customer_PAssportNo");
				String pvalue="P-"+pFname+" "+pLname;
				String sValue="";
				String gValue="";
				formObject.addItem(controlName,pvalue);
				PersonalLoanS.mLogger.info("inside loadDynamicPickList pvalue"+pvalue);
				List<String> entries = new ArrayList<String>();
				//Supplementary
				if(formObject.isVisible("SupplementCardDetails_Frame1")){
					int rowCount = formObject.getLVWRowCount("SupplementCardDetails_cmplx_supplementGrid");
					if(rowCount>0){
						for(int i=0;i<rowCount;i++){
							String sFname = formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,0);
							String sLname = formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,2);
							String sPass = formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,3);
							sValue = "S-"+sFname+" "+sLname+"-"+sPass;
							if(!entries.contains(sValue)){
								formObject.addItem(controlName,sValue);
								entries.add(sValue);
							}
						}

					}
				}
				if(formObject.isVisible("GuarantorDetails_Frame1")){
					int rowCount = formObject.getLVWRowCount("cmplx_Guarantror_GuarantorDet");
					if(rowCount>0){
						for(int i=0;i<rowCount;i++){
							String gFname = formObject.getNGValue("cmplx_Guarantror_GuarantorDet",i,7);
							String gLname = formObject.getNGValue("cmplx_Guarantror_GuarantorDet",i,9);
							//String gPass = formObject.getNGValue("cmplx_GuarantorDetails_cmplx_GuarantorGrid",i,5);
							gValue = "G-"+gFname+" "+gLname;//+"-"+gPass;
							if(!entries.contains(gValue)){
								formObject.addItem(controlName,gValue);
								entries.add(gValue);
							}
						}

					}
					String listViewName = "cmplx_DecisionHistory_MultipleApplicantsGrid";
				if(formObject.isVisible(listViewName)){
					int guarantorPresent = -1;
					int guarantorPresentrowIndex = -1;
					String name = "";
					String passport = "";
					String cif = "";
					for(int i=0;i<formObject.getLVWRowCount(listViewName);i++){
						if("Guarantor".equalsIgnoreCase(formObject.getNGValue(listViewName,i,0))){
							name = formObject.getNGValue(listViewName,i,1);
							passport = formObject.getNGValue(listViewName,i,2);
							cif = formObject.getNGValue(listViewName,i,3);
							guarantorPresent = 0;
							guarantorPresentrowIndex = i;
							//RLOS.mLogger.info("guarantor row index to be replaced guarantor: "+guarantorPresentrowIndex);
							break;
						}
					}
					if(formObject.getLVWRowCount("cmplx_Guarantror_GuarantorDet")>0){
						if(guarantorPresent==0){
							//RLOS.mLogger.info("inside condition to replace the guarantor");
							String gridguarName =  formObject.getNGValue("cmplx_Guarantror_GuarantorDet",0,7)+" "+formObject.getNGValue("cmplx_Guarantror_GuarantorDet",0,9);
							String gridguarpass = formObject.getNGValue("cmplx_Guarantror_GuarantorDet",0,5);
							String gridguarcif = formObject.getNGValue("cmplx_Guarantror_GuarantorDet",0,1);
							//RLOS.mLogger.info("inside condition to replace the guarantor gridguarName: "+gridguarName);
							//RLOS.mLogger.info("inside condition to replace the guarantor name: "+name);
							//RLOS.mLogger.info("inside condition to replace the guarantor gridguarpass: "+gridguarpass);
							//RLOS.mLogger.info("inside condition to replace the guarantor passport: "+passport);
							//RLOS.mLogger.info("inside condition to replace the guarantor gridguarcif: "+gridguarcif);
							//RLOS.mLogger.info("inside condition to replace the guarantor cif: "+cif);
							if(!gridguarName.equalsIgnoreCase(name) || !gridguarpass.equalsIgnoreCase(passport) || !gridguarcif.equalsIgnoreCase(cif)){
								formObject.setSelectedIndex("cmplx_DecisionHistory_MultipleApplicantsGrid", guarantorPresentrowIndex);
								formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_DecisionHistory_MultipleApplicantsGrid");
								List<String> subList = new ArrayList<String>();
								subList.add("Guarantor");
								subList.add(gridguarName);
								subList.add(gridguarpass);
								subList.add(gridguarcif);
								subList.add(formObject.getWFWorkitemName());
								//RLOS.mLogger.info("sublist formed is: "+subList);
								formObject.addItemFromList("cmplx_DecisionHistory_MultipleApplicantsGrid", subList);
							}
						}
						else if(guarantorPresent==-1){
							//RLOS.mLogger.info("inside condition to add the guarantor in the row");
							List<String> subList = new ArrayList<String>();
							subList.add("Guarantor");
							subList.add(formObject.getNGValue("cmplx_Guarantror_GuarantorDet",0,7)+" "+formObject.getNGValue("cmplx_Guarantror_GuarantorDet",0,9));
							subList.add(formObject.getNGValue("cmplx_Guarantror_GuarantorDet",0,5));
							subList.add(formObject.getNGValue("cmplx_Guarantror_GuarantorDet",0,1));
							subList.add(formObject.getWFWorkitemName());
							//RLOS.mLogger.info("sublist formed is: "+subList);
							formObject.addItemFromList("cmplx_DecisionHistory_MultipleApplicantsGrid", subList);
						}
					}
					else{
						//RLOS.mLogger.info("guarantor row has been deleted hence nothing to do ");
					}
				}
					
				
				}
				PersonalLoanS.mLogger.info("inside loadDynamicPickList sValue"+sValue);


				PersonalLoanS.mLogger.info("inside loadDynamicPickList gValue"+gValue);
				//PersonalLoanS.mLogger.info("@@@@@Prabhakar"+pvalue);
				//formObject.addItem("Customer_Type",pvalue);

			}
		}catch(Exception ex){
			PersonalLoanS.mLogger.info( "Exception occurred in validateStatusForSupplementary function:");
			printException(ex);
		}
	}

	public void modifyDynamicInCRNGrid(FormReference formObject,String passportNo,String CardProduct){
		try{
			String final_limit="";
			String CardProd = "";
			if(CardProduct.contains("(") && CardProduct.contains(")")){
				final_limit=CardProduct.substring(CardProduct.indexOf("(")+1, CardProduct.indexOf(")"));
				CardProd=CardProduct.split("\\(")[0];
				PersonalLoanS.mLogger.info("Inside modifyDynamicInCRNGrid->final limit is  "+final_limit);
				PersonalLoanS.mLogger.info("Inside modifyDynamicInCRNGrid ->Card Prod is  "+CardProd);
			}
			else{
				CardProd=CardProduct;
			}
			int n=formObject.getLVWRowCount("cmplx_CardDetails_cmplx_CardCRNDetails");
			for(int i=0;i<n;i++){
				if(formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",i,9).equals("Supplement") && formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",i,10).equals(passportNo)){
					formObject.setNGValue("cmplx_CardDetails_cmplx_CardCRNDetails", i,0,CardProd);
					formObject.setNGValue("cmplx_CardDetails_cmplx_CardCRNDetails", i,7,final_limit);
				}
			}
		}
		catch(Exception e){
			printException(e);
		}
	}
	public void addDynamicInCRNGrid(FormReference formObject,String passportNo,String CardProduct)
	{
		try{
			String final_limit="";
			String CardProd = "";
			/*String query = "select Card_Product AS cardProd,Final_Limit AS finalLim from ng_rlos_IGR_Eligibility_CardLimit  WITH (nolock) where Cardproductselect = 'true' and Child_Wi = '"+formObject.getWFWorkitemName()+"'";
			List<List<String>> records = formObject.getDataFromDataSource(query);
			if(records!=null && records.size()>0){
				for(List<String> selectedCards: records){
					if(selectedCards.get(0).equalsIgnoreCase(arg0))
				}
			}*/
			if(CardProduct.contains("(") && CardProduct.contains(")")){
				final_limit=CardProduct.substring(CardProduct.indexOf("(")+1, CardProduct.indexOf(")"));
				CardProd=CardProduct.split("\\(")[0];
				PersonalLoanS.mLogger.info("Inside addDynamicInCRNGrid:Final limit is  "+final_limit);
				PersonalLoanS.mLogger.info("Inside addDynamicInCRNGrid:CardProd is  "+CardProd);
			}
			UIComponent comp=formObject.getComponent("cmplx_CardDetails_cmplx_CardCRNDetails");
			List<String> CRNrow = new ArrayList<String>();
			for(int i=0;i<comp.getChildCount();i++){
				PersonalLoanS.mLogger.info(((Column)(comp.getChildren().get(i))).getName());
				if(((Column)(comp.getChildren().get(i))).getName().equals("Card Product"))
				{
					CRNrow.add(CardProd);
				}
				else if(((Column)(comp.getChildren().get(i))).getName().equals("Applicant Type"))
				{
					CRNrow.add("Supplement");
				}
				else if(((Column)(comp.getChildren().get(i))).getName().equals("final_limit"))
				{
					CRNrow.add(final_limit);
				}
				else if(((Column)(comp.getChildren().get(i))).getName().equals("PassportNo"))
				{
					CRNrow.add(passportNo);
				}
				else if(((Column)(comp.getChildren().get(i))).getName().equals("wi_name"))
				{
					CRNrow.add(formObject.getWFWorkitemName());
				}
				else{
					CRNrow.add("");
				}
			}
			formObject.addItemFromList("cmplx_CardDetails_cmplx_CardCRNDetails", CRNrow);
		}
		catch(Exception e){
			printException(e);
		}
	}
	//added by akshay on 22/6/18 for propc 9237
	public void loadinFinacleCRNGrid(FormReference formObject)
	{
		try{
			PersonalLoanS.mLogger.info( "inside loadinFinacleCRNGrid()");
			if(formObject.getLVWRowCount("cmplx_FinacleCRMCustInfo_FincustGrid")==0){
				String query="";
				if(formObject.getNGValue("cmplx_Customer_NTB").equals("false")){
					//query="select distinct custid,'' as applicant,(select passportno from ng_rlos_customer with(nolock) where wi_name='"+formObject.getWFWorkitemName()+"'),NegatedFlag,isnull(format(convert(datetime,NegatedDate),'dd/MM/yyyy'),''),isnull(NegatedReasonCode,''),'' as reason,BlacklistFlag,isnull(format(convert(datetime,NegatedDate),'dd/MM/yyyy'),''),isnull(BlacklistReasonCode,''),'' as blacklistReason,'' as alerts,'' as watchlistCode,'true' as ConsiderForObligations from ng_rlos_cif_detail where cif_wi_name='"+formObject.getNGValue("parent_WIName")+"' and is_primary_cif='Y'";
					query="select isnull(custid,''),'Individual' as applicant,(select passportno from ng_rlos_customer with (nolock) where wi_name='"+formObject.getWFWorkitemName()+"'),NegatedFlag,isnull(format(convert(datetime,NegatedDate),'dd/MM/yyyy'),''),isnull(NegatedReasonCode,''),'' as reason,BlacklistFlag,isnull(format(convert(datetime,NegatedDate),'dd/MM/yyyy'),''),isnull(BlacklistReasonCode,''),'' as blacklistReason,'' as alerts,'' as watchlistCode,'true' as ConsiderForObligations from ng_rlos_cif_detail with (nolock) where cif_wi_name='"+formObject.getNGValue("parent_WIName")+"' and CustId='"+formObject.getNGValue("cmplx_Customer_CIFNO")+"'";

				}
				else{
					//query="select cif_id,'' as applicant,passportno ,'','','','','','','','','','','true' from ng_rlos_customer with(nolock) where wi_name='"+formObject.getWFWorkitemName()+"'"; 
					//Deepak Commented for PCASI-4006 || Set true in Consider For Obligations for NTB as well because CIF is already created at Iniitation
					//query="select isnull(cif_id,''),'Individual' as applicant,passportno ,'N','','','','N','','','','','','false' from ng_rlos_customer with (nolock) where wi_name='"+formObject.getWFWorkitemName()+"'"; 
					query="select isnull(cif_id,''),'Individual' as applicant,passportno ,'N','','','','N','','','','','','true' as ConsiderForObligations from ng_rlos_customer with (nolock) where wi_name='"+formObject.getWFWorkitemName()+"'";
				}

				PersonalLoanS.mLogger.info( "query: "+ query);

				List<List<String>> result=formObject.getDataFromDataSource(query);
				PersonalLoanS.mLogger.info( "result: "+ result);

				if(result!=null && result.size()>0){
					for(List<String> rowTobeAdded:result){
						rowTobeAdded.add(formObject.getWFWorkitemName());
						PersonalLoanS.mLogger.info( "final row: "+ rowTobeAdded);
						formObject.addItemFromList("cmplx_FinacleCRMCustInfo_FincustGrid", rowTobeAdded);
					}
				}
				String currVal = formObject.getNGValue("FrameName");
				formObject.setNGValue("FrameName",currVal+"FinacleCRM_CustInfo,");
			}
		}
		catch(Exception e){
			printException(e);
		}
	}

	public static boolean isNumeric(String str)  
	{  
		try  
		{  
			double d = Double.parseDouble(str);  
		}  
		catch(NumberFormatException nfe)  
		{  
			return false;  
		}  
		return true;  
	}
	//below code added by nikhil 
	public void openCPVtabs(FormReference formObject){

/*		if(formObject.isVisible("Cust_Detail_verification") && !formObject.isVisible("CustDetailVerification_Frame1")){
			load_Customer_Details_Verification(formObject);
			formObject.setNGFrameState("Cust_Detail_verification",0);
		}      // hritik 27.6.21                  */

		if(formObject.isVisible("Office_verification") && !formObject.isVisible("OfficeandMobileVerification_Frame1")){
			load_Office_Mob_Verification(formObject);
			formObject.setNGFrameState("Office_verification",0);

		}

		if(formObject.isVisible("Loan_card_details") && !formObject.isVisible("LoanandCard_Frame1")){
			load_LoanCard_Details_Check(formObject);
			formObject.setNGFrameState("Loan_card_details",0);

		}
		
		if(formObject.isVisible("Business_Verification") && !formObject.isVisible("BussinessVerification_Frame1")){
			formObject.fetchFragment("Business_Verification", "BussinessVerification", "q_bussverification1");
			LoadPickList("cmplx_BussVerification_contctTelChk", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Other' ");
			//changed by nikhil CPV changes
			LoadPickList("cmplx_BussVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) where IsActive='Y' and For_custdetail_only='N' order by code");//Arun 14/12/17 to load the decision correctly
			formObject.setNGFrameState("Business_Verification",0);

		}
		
		if(formObject.isVisible("Smart_check") && !formObject.isVisible("SmartCheck_Frame1")){//cpv tab aligment by shweta
			formObject.fetchFragment("Smart_check", "SmartCheck", "q_SmartCheck");
			formObject.setNGFrameState("Smart_check",0);

		}

/*		if(formObject.isVisible("Home_country_verification") && !formObject.isVisible("HomeCountryVerification_Frame1")){
			formObject.fetchFragment("Home_country_verification", "HomeCountryVerification", "q_HomeCountryVeri");
			formObject.setNGFrameState("Home_country_verification",0);

		}      hritik 22.8.21 PCASi 3779        */
		if(formObject.isVisible("Residence_verification") && !formObject.isVisible("ResidenceVerification_Frame1")){
			formObject.fetchFragment("Residence_verification", "ResidenceVerification", "q_ResiVerification");
			formObject.setNGFrameState("Residence_verification",0);

		}
		if(formObject.isVisible("Guarantor_verification") && !formObject.isVisible("GuarantorVerification_Frame1")){
			formObject.fetchFragment("Guarantor_verification", "GuarantorVerification", "q_GuarantorVer");
			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_GuarantorVerification_verdoneonmob");
			LoadPicklistVerification(LoadPicklist_Verification);

			//LoadPickList("cmplx_GuarantorVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with(nolock) order by code");

			formObject.setNGFrameState("Guarantor_verification",0);

		}
		
		adjustFrameTops("Cust_Detail_verification,Home_country_verification,Office_verification,Smart_check");
	}

	public void load_LoanCard_Details_Check(FormReference formObject) {
		formObject.fetchFragment("Loan_card_details", "LoanandCard", "q_LoanandCard");
		formObject.setNGFrameState("Loan_card_details",0);

		//loadPicklist_loancardverification();

		List<String> LoadPicklist_Verification= Arrays.asList("cmplx_LoanandCard_loanamt_ver","cmplx_LoanandCard_tenor_ver","cmplx_LoanandCard_emi_ver","cmplx_LoanandCard_islorconv_ver","cmplx_LoanandCard_firstrepaydate_ver","cmplx_LoanandCard_cardtype_ver","cmplx_LoanandCard_cardlimit_ver");
		LoadPicklistVerification(LoadPicklist_Verification);
		LoadPickList("cmplx_LoanandCard_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision order by code");
		enable_loanCard();//Arun 14/12/17 new function added for CPV
		loancardvalues();		
	}
	public void loadPicklist_loancardverification(){
		LoadPickList("cmplx_LoanandCard_loanamt_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock) where field_name='Verification'");
		LoadPickList("cmplx_LoanandCard_tenor_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock) where field_name='Verification'");
		LoadPickList("cmplx_LoanandCard_emi_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock) where field_name='Verification'");
		LoadPickList("cmplx_LoanandCard_islorconv_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock) where field_name='Verification'");
		LoadPickList("cmplx_LoanandCard_firstrepaydate_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock) where field_name='Verification'");
		LoadPickList("cmplx_LoanandCard_cardtype_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock) where field_name='Verification'");
		LoadPickList("cmplx_LoanandCard_cardlimit_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock) where field_name='Verification'");
	}
	
	//by shweta
	public void load_Office_Mob_Verification(FormReference formObject) {
		formObject.fetchFragment("Office_verification", "OfficeandMobileVerification", "q_OffVerification");
		//loadPicklist_officeverification();
		formObject.setNGFrameState("Office_verification",0);

		LoadPickList("cmplx_OffVerification_desig_upd", "select '--Select--'  as description,'' as code  union select convert(varchar, description),code from NG_MASTER_designation with (nolock) order by code");

		enable_officeVerification();
		if(!"Yes".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_hrdnocntctd")) && !"Yes".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_hrdemailverified")))
		{
			formObject.setNGValue("cmplx_OffVerification_fxdsal_ver","");
			formObject.setNGValue("cmplx_OffVerification_accpvded_ver","");
			formObject.setNGValue("cmplx_OffVerification_desig_ver","");
			formObject.setNGValue("cmplx_OffVerification_doj_ver","");
			formObject.setLocked("cmplx_OffVerification_fxdsal_ver",true);
			formObject.setLocked("cmplx_OffVerification_accpvded_ver",true);
			formObject.setLocked("cmplx_OffVerification_desig_ver",true);
			formObject.setLocked("cmplx_OffVerification_doj_ver",true);
		}
		if("Yes".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_hrdnocntctd")) || "Yes".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_hrdemailverified")))
		{
			formObject.setNGValue("cmplx_OffVerification_colleaguenoverified","--Select--");
		}
		//below code modified
		//commented by nikhil for pcas-2239
		/*List<String> LoadPicklist_Verification= Arrays.asList("cmplx_OffVerification_fxdsal_ver","cmplx_OffVerification_accpvded_ver","cmplx_OffVerification_desig_ver","cmplx_OffVerification_doj_ver","cmplx_OffVerification_cnfminjob_ver");
		LoadPicklistVerification(LoadPicklist_Verification);
		LoadPickList("cmplx_OffVerification_colleaguenoverified", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_CPVVeri with (nolock) where Sno != 3 order by code"); //Arun modified on 14/12/17
		//changed by nikhil for CPV changes 17-04
		LoadPickList("cmplx_OffVerification_Decision", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cpvdecision with (nolock) where IsActive='Y' and For_custdetail_only='N' order by code");
		//below code by saurabh on 28th nov 2017.
		LoadPickList("cmplx_OffVerification_offtelnovalidtdfrom", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_offNoValidatedFrom with (nolock)");
		LoadPickList("cmplx_OffVerification_hrdnocntctd", "select '--Select--','--Select--' union select convert(varchar, Description),code from NG_MASTER_HRDContacted with (nolock)");
		*///formObject.setLocked("OfficeandMobileVerification_Frame1", true); //Arun (26/11/17) to lock only certain fields not the whole frame


		//++ below code added by abhishek as per CC FSD 2.7.3
		if("Smart_CPV".equalsIgnoreCase(formObject.getWFActivityName())){
			//below code commneted for calling procedure



			String EnableFlagValue = formObject.getNGValue("cmplx_OffVerification_EnableFlag");
			String sQuery =" SELECT ProcessInstanceID,lockStatus FROM WFINSTRUMENTTABLE with (nolock) WHERE activityname = 'CPV' AND ProcessInstanceID ='"+formObject.getWFWorkitemName()+"'";
			PersonalLoanS.mLogger.info( "query is ::"+sQuery);
			List<List<String>> list=formObject.getNGDataFromDataCache(sQuery);
			PersonalLoanS.mLogger.info( "list is ::"+list+"size is::"+list.size());

			if(list.size()==0){

				formObject.setLocked("OfficeandMobileVerification_Frame1",false);
			}
			else{
				String lockStatus = list.get(0).get(1);
				if("Y".equalsIgnoreCase(lockStatus)){
					if("true".equalsIgnoreCase(EnableFlagValue)){
						formObject.setLocked("OfficeandMobileVerification_Frame1",true);
					}else{
						formObject.setLocked("OfficeandMobileVerification_Frame1",false);
						formObject.setEnabled("OfficeandMobileVerification_Enable", false);
						formObject.setEnabled("cmplx_OffVerification_offtelno",false);
						formObject.setEnabled("cmplx_OffVerification_fxdsal_val",false);
						formObject.setEnabled("cmplx_OffVerification_accprovd_val",false);
						formObject.setEnabled("cmplx_OffVerification_desig_val",false);
						formObject.setEnabled("cmplx_OffVerification_doj_val",false);
						formObject.setVisible("EMploymentDetails_Label45", true);
						formObject.setEnabled("cmplx_OffVerification_cnfrminjob_val",false);

					}
				}else{
					formObject.setLocked("OfficeandMobileVerification_Frame1",false);
					formObject.setEnabled("OfficeandMobileVerification_Enable", false);
					formObject.setEnabled("cmplx_OffVerification_offtelno",false);
					formObject.setEnabled("cmplx_OffVerification_fxdsal_val",false);
					formObject.setEnabled("cmplx_OffVerification_accprovd_val",false);
					formObject.setEnabled("cmplx_OffVerification_desig_val",false);
					formObject.setEnabled("cmplx_OffVerification_doj_val",false);
					formObject.setVisible("EMploymentDetails_Label45", true);
					formObject.setEnabled("cmplx_OffVerification_cnfrminjob_val",false);
				}
			}


		}
		if("Smart_CPV".equalsIgnoreCase(formObject.getWFActivityName()))
		{
			 PersonalLoanS.mLogger.info( "@sagarika enable");
			 if("--Select--".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_hrdnocntctd"))&& "--Select--".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_colleaguenoverified")))
			 {
				 formObject.setLocked("cmplx_OffVerification_hrdnocntctd",false);
				 formObject.setLocked("cmplx_OffVerification_colleaguenoverified",false);
				 formObject.setLocked("cmplx_OffVerification_hrdemailverified",false);
				 


			 }

			 if("Yes".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_hrdnocntctd")))
			 {
				 PersonalLoanS.mLogger.info( "@sagarika new hrd");
				 formObject.setLocked("cmplx_OffVerification_offtelnovalidtdfrom",false);//PCAS-2514 sagarika
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
				 
				 if("NA".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_cnfminjob_ver")))
					{
					 formObject.setLocked("cmplx_OffVerification_cnfminjob_ver",true);
					// formObject.setNGValue("cmplx_OffVerification_cnfminjob_ver","--Select--");
					}
					else{
						 formObject.setLocked("cmplx_OffVerification_cnfminjob_ver",false);
						 formObject.setEnabled("cmplx_OffVerification_cnfminjob_ver", true);
					//	 formObject.setEnabled("cmplx_OffVerification_cnfminjob_ver",true);
					}
				
				 formObject.setLocked("cmplx_OffVerification_colleagueno",true);
				 formObject.setLocked("cmplx_OffVerification_colleaguename",true);
				 formObject.setLocked("cmplx_OffVerification_colleaguedesig",true);
				 formObject.setLocked("cmplx_OffVerification_hrdemailid",true);
				// formObject.setNGValue("cmplx_OffVerification_hrdemailverified","--Select--");
				 formObject.setLocked("cmplx_OffVerification_hrdemailverified",false);
				 formObject.setNGValue("cmplx_OffVerification_colleaguenoverified","--Select--");
				 formObject.setLocked("cmplx_OffVerification_colleaguenoverified",true);
				 formObject.setLocked("cmplx_OffVerification_colleagueno",true);
				 formObject.setNGValue("cmplx_OffVerification_colleagueno","");
				 // formObject.setNGValue("cmplx_OffVerification_colleaguenoverified","");
				 formObject.setNGValue("cmplx_OffVerification_colleaguename","");
				 formObject.setLocked("cmplx_OffVerification_colleaguename",true);
				 formObject.setLocked("cmplx_OffVerification_colleaguedesig",true);
				 formObject.setNGValue("cmplx_OffVerification_colleaguedesig","");
			//	 formObject.setNGValue("cmplx_OffVerification_colleaguenoverified","--Select--");
				 formObject.setLocked("cmplx_OffVerification_colleaguenoverified",false);
				 formObject.setLocked("cmplx_OffVerification_colleagueno",true);
				 disableforNA_Off();

			 }
			 else if("Yes".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_hrdemailverified")))
			 {

				 formObject.setNGValue("cmplx_OffVerification_hrdnocntctd","--Select--");
				 formObject.setLocked("cmplx_OffVerification_hrdnocntctd",false);
				 formObject.setLocked("cmplx_OffVerification_hrdcntctno",true);
				 formObject.setNGValue("cmplx_OffVerification_hrdcntctno","");
				 //formObject.setNGValue("cmplx_OffVerification_hrdcntctdesig",""); 
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
				 //formObject.setLocked("cmplx_OffVerification_cnfminjob_ver",false);
				// formObject.setEnabled("cmplx_OffVerification_cnfminjob_ver",true);
				 formObject.setEnabled("cmplx_OffVerification_desig_ver",true);
				 formObject.setLocked("cmplx_OffVerification_cnfminjob_ver",false);
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
				// formObject.setNGValue("cmplx_OffVerification_colleaguenoverified","--Select--");
				 formObject.setLocked("cmplx_OffVerification_colleaguenoverified",false);
				 formObject.setLocked("cmplx_OffVerification_colleagueno",true);
				 formObject.setNGValue("cmplx_OffVerification_colleagueno","");
				 // formObject.setNGValue("cmplx_OffVerification_colleaguenoverified","");
				 formObject.setNGValue("cmplx_OffVerification_colleaguename","");
				 formObject.setLocked("cmplx_OffVerification_colleaguename",true);
				 formObject.setLocked("cmplx_OffVerification_colleaguedesig",true);
				 formObject.setNGValue("cmplx_OffVerification_colleaguedesig","");
				 disableforNA_Off();

			 }

			 else
			 {
				 PersonalLoanS.mLogger.info( "@sagarika no");
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
				 formObject.setLocked("cmplx_OffVerification_colleaguenoverified",false);
				 formObject.setNGValue("cmplx_OffVerification_colleaguenoverified","--Select--");
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
				 }
			 }


			 PersonalLoanS.mLogger.info( "@sagarika hrd");
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
				else
				{
					formObject.setNGValue("cmplx_OffVerification_cnfminjob_ver","");
					formObject.setLocked("cmplx_OffVerification_cnfminjob_ver",false);
					formObject.setEnabled("cmplx_OffVerification_cnfminjob_ver",true);
				}
			 /*if("".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerificatio_desig_upd")) )
{
	formObject.setNGValue("cmplx_OffVerification_desig_upd", formObject.getNGValue("cmplx_EmploymentDetails_Designation"));

}
if("".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_doj_upd")) || "--Select--".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_doj_upd")))
{
	formObject.setNGValue("cmplx_OffVerification_doj_upd", formObject.getNGValue("cmplx_EmploymentDetails_DOJ"));
}
if("".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_cnfrminjob_upd")) || "--Select--".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_cnfrminjob_upd")))
{
	formObject.setNGValue("cmplx_OffVerification_cnfrminjob_upd", formObject.getNGValue("cmplx_OffVerification_cnfrminjob_val"));
}*/
		}
		

	}

	public void CustomSaveForm(){
		try{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sampleString = formObject.getNGValue("FrameName");
		PersonalLoanS.mLogger.info(sampleString + "sampleString");
		String[] items = sampleString.split(",");
		for (String item : items) {
			//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
			if("NotepadDetails_Frame1".equals(item))
	    	  {
	    		  item="Notepad_Values";
	    	  }
			//added by shweta to save finacle core on master save
	    	  if("FinacleCore_Frame1".equals(item))
	    	  {
	    		  item="Finacle_Core";
	    	  }
	    	  if("DecisionHistory".equals(item)){
	    		  Data_reset("DecisionHistory");
	    	  }
	    	  if("Fpu_Grid".equals(item)||"FPU_GRID".equals(item)||"Fpu_Grid_Frame1".equals(item)){
	    		  item="FPU_GRID";
	    	  }
	    	  
	    	  formObject.saveFragment(item);
	    	PersonalLoanS.mLogger.info(item+ " item in CustomSaveForm ");
		}
		formObject.setNGValue("FrameName","");
		//formObject.setNGValue("Loan_Amount", formObject.getNGValue("cmplx_LoanDetails_lonamt"));	//PCASI - 3444
		}catch(Exception e){
			PersonalLoanS.mLogger.info("Inside catch of  CustomSaveForm()" );
			printException(e);
		}
	}
	
	//change done by shweta for jira#2372 
	public void load_Customer_Details_Verification(FormReference formObject) {
		formObject.fetchFragment("Cust_Detail_verification", "CustDetailVerification", "q_CustDetVer");
		formObject.setNGFrameState("Cust_Detail_verification",0);
		openDemographicTabs();
		autopopulateValues(formObject);
		//formObject.setNGValue("cmplx_CustDetailVerification_offtelno_ver", "NA");
		if("Y".equalsIgnoreCase(formObject.getNGValue("CPV_WAVIER"))) {
			formObject.setNGValue("cmplx_CustDetailVerification_Decision","Not Applicable");
			PersonalLoanS.mLogger.info("detail log");
		} else {
			PersonalLoanS.mLogger.info("else detail log");

		}
	}

	public Map<String,String> getFatcaReasons(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String query="Select code,description from ng_master_fatcaReasons with(nolock)";
		List<List<String>> fatcaReasons=formObject.getNGDataFromDataCache(query);
		Map<String,String> ReasonMap=new HashMap<String,String>();
		if(fatcaReasons!=null && !fatcaReasons.isEmpty()){
			for(List<String> fatcaReason:fatcaReasons){
				ReasonMap.put(fatcaReason.get(1),fatcaReason.get(0));

			}
		}
		return ReasonMap;
	}

	public String getKeyByValue(Map hm, String value) {
		PersonalLoanS.mLogger.info("Inside getKeyByValue-->Arguement Value: "+value);

		List<String> keys=new ArrayList(hm.keySet());
		List<String> values =new ArrayList(hm.values());
		int matchingindex=-1;
		for(String iterator:values){
			if(value.equalsIgnoreCase(iterator)){
				matchingindex=values.indexOf(iterator);
				break;
			}
		}
		PersonalLoanS.mLogger.info("matchingindex, Matching key is: "+matchingindex+", "+keys.get(matchingindex));
		if(matchingindex!=-1){
			return keys.get(matchingindex);
		}
		return "";	
	}


	public void OriginalDocs(){
		//OriginalValidation_Frame

		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		IRepeater repObj=null;
		//repObj = formObject.getRepeaterControl("IncomingDoc_Frame2");
		repObj = formObject.getRepeaterControl("OriginalValidation_Frame");

		String [] finalmisseddoc=new String[70];
		int rowRowcount=repObj.getRepeaterRowCount();
		PersonalLoanS.mLogger.info( "sQuery for document name is: rowRowcount" +  rowRowcount);
		if (repObj.getRepeaterRowCount() != 0) {
			for(int j = 0; j < rowRowcount; j++)
			{
				String DocName=repObj.getValue(j, "cmplx_OrigVal_doclist");
				PersonalLoanS.mLogger.info( "sQuery for document name is: DocName" +  DocName);
				
				String decValue="";
				PersonalLoanS.mLogger.info( "Ov decision value is: " +  decValue);
				if("Document_Checker".equalsIgnoreCase(formObject.getWFActivityName())){
					decValue= repObj.getValue(j,"cmplx_OrigVal_dcdec")==null?"":repObj.getValue(j,"cmplx_OrigVal_dcdec");

				}else {
					decValue= repObj.getValue(j,"cmplx_OrigVal_ovdec")==null?"":repObj.getValue(j,"cmplx_OrigVal_ovdec");
				}

			

				if("--Select--".equalsIgnoreCase(decValue)){
					//PersonalLoanS.mLogger.info("StatusValue inside DocIndex"+DocIndex);
					finalmisseddoc[j]=DocName;
				}
			}
		}
		StringBuilder mandatoryDocName = new StringBuilder("");
		PersonalLoanS.mLogger.info("length of missed document"+finalmisseddoc.length);
		PersonalLoanS.mLogger.info("length of missed document mandatoryDocName.length"+mandatoryDocName.length());
		for(int k=0;k<finalmisseddoc.length;k++)
		{
			if(null != finalmisseddoc[k]) {
				mandatoryDocName.append(finalmisseddoc[k]).append(",");
			}
			PersonalLoanS.mLogger.info( "finalmisseddoc is:" +finalmisseddoc[k]);
			PersonalLoanS.mLogger.info("length of missed document mandatoryDocName.length1111"+mandatoryDocName.length());
		}
		mandatoryDocName.setLength(Math.max(mandatoryDocName.length()-1,0));
		PersonalLoanS.mLogger.info( "misseddoc is:" +mandatoryDocName.toString());

		if(mandatoryDocName.length()<=0){

			PersonalLoanS.mLogger.info( "misseddoc is: inside if condition");

		}
		else{
			PersonalLoanS.mLogger.info( "misseddoc is: inside if condition");
			PersonalLoanS.mLogger.info("length of missed document mandatoryDocName.length1111222"+mandatoryDocName.length());
		String ws="OV";
		
		if("Document_Checker".equalsIgnoreCase(formObject.getWFActivityName())){
			 ws="Document_Checker";
		}
			throw new ValidatorException(new FacesMessage("Please take "+ws+" Decision for the following Documents: "+mandatoryDocName.toString()));
		}

	}

	//added by akshay
		public boolean check_MurabhaFileConfirmed(FormReference formObject)
		{
			//Deepak 30 Dec Change done as for Islamic combined limit will be 0.
			try{
			String query="SELECT IS_Murhabah_Confirm  FROM ng_rlos_Murabha_Warranty warranty JOIN ng_rlos_IGR_Eligibility_CardLimit limit ON child_wi=murhabha_winame and warranty.card_product = limit.Card_Product WHERE murhabha_winame='"+formObject.getWFWorkitemName()+"' AND limit.combined_limit='0'";
			PersonalLoanS.mLogger.info("inside check_MurabhaFileConfirmed....query is :"+query);
			List<List<String>> result=formObject.getDataFromDataSource(query);
			if(!result.isEmpty()){
				if(result.get(0).get(0)==null || result.get(0).get(0).equals("")  || result.get(0).get(0).equals("N")){
					return false;
				}
				else{
					return true;//murhabha flag is Y
				}
			}
			else{
				return true;//conventional case
			}
		}
			catch(Exception e){
				printException(e);
			}
			return false;
	}
	//cmplx_emp_ver_sp2_cmplx_loan_disb
	//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
	public void Custom_fragmentSave(String fragment_name){
		PersonalLoanS.mLogger.info( "inside Custom_fragmentSave :: "+fragment_name);
		 String alert_msg="";
		 try{
			 PersonalLoanS.mLogger.info("Inside try");
			 FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			 Data_reset(fragment_name);
			 PersonalLoanS.mLogger.info("Check 1");
			 String return_Arr[] = formObject.saveFragment(fragment_name);
			 PersonalLoanS.mLogger.info("save fragment size :: "+return_Arr.length+" , "+return_Arr[0]+" , "+return_Arr[1]);
				if(return_Arr.length>0 && !"0".equalsIgnoreCase(return_Arr[0])){
					 PersonalLoanS.mLogger.info("Insie if of save fragment :: "+return_Arr[0]);
					if("11".equalsIgnoreCase(return_Arr[0])){
						alert_msg=NGFUserResourceMgr_PL.getAlert("VAL087");
					}
					else{
						PersonalLoanS.mLogger.info(" inside Custom_fragmentSave fragment_name =  "+fragment_name);
						alert_msg=NGFUserResourceMgr_PL.getAlert("VAL088");
					}
				}
				else if (null==return_Arr || return_Arr.length==0){
					 PersonalLoanS.mLogger.info(" inside Custom_fragmentSave return_Arr.length==0"+fragment_name);
					alert_msg=NGFUserResourceMgr_PL.getAlert("VAL088");
				}
				 PersonalLoanS.mLogger.info("End of try - CustomSave_fragment");
		 }
		 catch(Exception e){
			 PersonalLoanS.mLogger.info(" inside catch of Custom_fragmentSave"+e);
			 printException(e);
			 alert_msg=NGFUserResourceMgr_PL.getAlert("VAL088");
		 }
		 if(!"".equalsIgnoreCase(alert_msg)){
			 PersonalLoanS.mLogger.info("alert_msg :: "+alert_msg);
			 throw new ValidatorException(new FacesMessage(alert_msg));
		 }
		 PersonalLoanS.mLogger.info("End of Custom fragment save function");
	 }
	//new function by Saurabh
	public void fetchIncomingDocRepeaterNew_Bakcup(){
		PersonalLoanS.mLogger.info( "inside fetchIncomingDocRepeaterNew");
		try {
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			List<List<String>> docName = null;
			String documentName = "";
			String documentNameMandatory="";
			String documentNameNonMandatory="";
			String query = "";
			query = "SELECT distinct DocumentType FROM ng_rlos_gr_incomingDocument with (nolock) WHERE  IncomingDocGR_Winame='"+formObject.getWFWorkitemName()+"' order by Mandatory desc,DocName asc";
			PersonalLoanS.mLogger.info("query:"+query);
			docName = formObject.getDataFromDataSource(query);
			PersonalLoanS.mLogger.info("docName.size():"+docName.size()+" result: "+docName);
			//Changed for sonar
			if(docName.size()>0) {
				for(List<String> row: docName) {
					if("Y".equalsIgnoreCase(row.get(2))) {
						documentNameMandatory+=row.get(1)+",";
					}
					else {
						documentNameNonMandatory+=row.get(1)+",";
					}
				}

			}

			if(documentNameMandatory.endsWith(",")) {
				documentNameMandatory = documentNameMandatory.substring(0, documentNameMandatory.length()-1);
			}
			if(documentNameNonMandatory.endsWith(",")) {
				documentNameNonMandatory = documentNameNonMandatory.substring(0, documentNameNonMandatory.length()-1);
			}
			formObject.setNGValue("cmplx_IncomingDocNew_MandatoryDocument", documentNameMandatory);
			formObject.setNGValue("cmplx_IncomingDocNew_NonMandatoryDoc", documentNameNonMandatory);

			LoadPickList("IncomingDocNew_DocType", "SELECT DISTINCT documentType FROM ng_rlos_gr_incomingDocument WHERE IncomingDocGR_Winame = '"+formObject.getWFWorkitemName()+"'");
			PersonalLoanS.mLogger.info( "incomingdocname");
			LoadPickList("IncomingDocNew_DocName", "SELECT DISTINCT documentName FROM ng_rlos_gr_incomingDocument WHERE IncomingDocGR_Winame = '"+formObject.getWFWorkitemName()+"'");
			PersonalLoanS.mLogger.info( "out of incomingdocname");
		}catch(Exception ex) {
			PersonalLoanS.mLogger.info( "exception in fetchIncomingDocRepeater");
			printException(ex);
		}
	}
	
	//below method added by isha for employemt Match Check for jira PCSP-459
	public void Employment_Match_Check()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String AlertMsg="";
		String Wi_Name=formObject.getWFWorkitemName();
		List<List<String>> Emp_Income_Result=null;
		try
		{
			String final_query = "select isnull(IS_CPV,''),'','','','' from NG_PL_EXTTABLE with (nolock) where PL_Wi_Name='"+Wi_Name+"' union all select isnull(emp.DesigStatus,''),isnull(emp.ConfirmedInJob,''),isnull(inc.grossSal,''),ISNULL(inc.Accomodation,''),emp.DOJ  from ng_RLOS_EmpDetails emp with (nolock) join ng_RLOS_IncomeDetails inc with (nolock) on emp.wi_name=inc.wi_name where emp.wi_name ='"+Wi_Name+"' union all SELECT (case when Desig_upd is null or Desig_upd ='--Select--' then (select code from NG_MASTER_Designation with (nolock) where Description=Desig_val) else Desig_upd end ),(case when confirmedinJob_upd is null or confirmedinJob_upd ='--Select--' then confirmedinJob_val else confirmedinJob_upd end),(case when fixedsalupd is null or fixedsalupd ='--Select--' then fixedsal_val else fixedsalupd end),(case when AccomProvided_upd is null or AccomProvided_upd ='NA' or AccomProvided_upd ='--Select--' then AccomProvided_val else AccomProvided_upd end),(case when doj_upd is null then doj_val else doj_upd end) from NG_RLOS_OffVerification with (nolock) where wi_name='"+Wi_Name+"'";
			PersonalLoanS.mLogger.info("Inside Employment_Match_check final_query : "+ final_query);
			Emp_Income_Result=formObject.getDataFromDataSource(final_query);
			if(Emp_Income_Result.size()<3)
			{
				AlertMsg="";
			}
			else
			{
				if(!"Y".equalsIgnoreCase(Emp_Income_Result.get(0).get(0)))
				{
					if(!(Emp_Income_Result.get(2).get(0)).equalsIgnoreCase(Emp_Income_Result.get(1).get(0)) && !"true".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_desig_override")))
					{
						AlertMsg+=" || \nField:Designation | Value:"+Emp_Income_Result.get(2).get(0);
					}
					if(!(Emp_Income_Result.get(2).get(1)).equalsIgnoreCase(Emp_Income_Result.get(1).get(1)) &&  !"true".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_cnfrmjob_override")))
					{
						AlertMsg+=" || \nField:Confirmed In Job | Value:"+Emp_Income_Result.get(2).get(1);
					}
					if(!(Emp_Income_Result.get(2).get(2)).equalsIgnoreCase(Emp_Income_Result.get(1).get(2)) &&  !"true".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_fxdsal_override")))
					{
						AlertMsg+=" || \nField:Fixed Salary | Value:"+Emp_Income_Result.get(2).get(2);
					}
					if(!(Emp_Income_Result.get(2).get(3)).equalsIgnoreCase(Emp_Income_Result.get(1).get(3)) &&  !"true".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_accprovd_override")))
					{
						AlertMsg+=" || \nField:Accomodation | Value:"+Emp_Income_Result.get(2).get(3);
					}
					if(!(Emp_Income_Result.get(2).get(4)).equalsIgnoreCase(Emp_Income_Result.get(1).get(4)) &&  !"true".equalsIgnoreCase(formObject.getNGValue("cmplx_OffVerification_doj_override")))
					{
						AlertMsg+=" || \nField:DOJ | Value:"+Emp_Income_Result.get(2).get(4).substring(0,11);
					}
				}

			}
			if(AlertMsg.length()>0)	
			{
				AlertMsg=NGFUserResourceMgr_PersonalLoanS.getGlobalVar("Mismatch_message")+AlertMsg;
			}


		}
		catch(Exception ex)
		{
			PersonalLoanS.mLogger.info("Error In Employment_Match_Check" + ex.getStackTrace());
		}
		if(!AlertMsg.equalsIgnoreCase(""))
		{
			throw new ValidatorException(new FacesMessage(AlertMsg));
		}

	}
	//below method added by isha for other Match Check PCSP-459
	public void Other_Detail_Match_check()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String Wi_Name=formObject.getWFWorkitemName(),AlertMsg="";
		List<List<String>> OtherMismatch_Result=null;
		List<List<String>> Decision_Result=null;
		try
		{
			String Query_dec="select top 1 workstepName,Decision from NG_RLOS_GR_DECISION with (nolock) where dec_wi_name='"+Wi_Name+"' and workstepName in ('CAD_Analyst1','CAD_Analyst2') order by insertionOrderId desc";
			PersonalLoanS.mLogger.info("Inside Other_Match_check final_query : "+ Query_dec);
			Decision_Result=formObject.getDataFromDataSource(Query_dec);
			if("Approve".equalsIgnoreCase(Decision_Result.get(0).get(1)))
			{
				String Query="select Final_Limit from ng_rlos_EligAndProdInfo with (nolock) where wi_name='"+Wi_Name+"'";
				PersonalLoanS.mLogger.info("Inside Other_Match_check final_query : "+ Query);
				OtherMismatch_Result=formObject.getDataFromDataSource(Query);
				if(OtherMismatch_Result!=null && OtherMismatch_Result.size()>0 )
				{
					if(!formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit").equalsIgnoreCase(OtherMismatch_Result.get(0).get(0)))
					{
						AlertMsg=NGFUserResourceMgr_PersonalLoanS.getGlobalVar("Mismatch_message")+"\n Field: Final Limit | Value: "+OtherMismatch_Result.get(0).get(0);
					}
				}
			}
		}
		catch(Exception ex)
		{
			PersonalLoanS.mLogger.info("Error In Other_Match_Check" + ex.getStackTrace());
		}
		if(!AlertMsg.equalsIgnoreCase(""))
		{
			throw new ValidatorException(new FacesMessage(AlertMsg));
		}
	}//cmplx_EligibilityAndProductInfo_NetPayout
	//below code added by nikhil 06/1/2019
	public void fetchIncomingDocRepeaterNew(){
		PersonalLoanS.mLogger.info( "inside fetchIncomingDocRepeaterNew");
		try {
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();

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

			List<List<String>> docName = null;
			List<String> docType = new ArrayList<String>();
			String documentName = "";
			String documentNameMandatory="";
			String documentNameNonMandatory="";
			String query = "";
			if("Personal Loan".equalsIgnoreCase(requested_product)){
				//changes done by nikhil for PCSP-699
				String targetsegCode="";
				String empTypeDoc = "";
				if("Salaried".equalsIgnoreCase(formObject.getNGValue("EmploymentType"))){
					targetsegCode = formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode");
					empTypeDoc = "SAL";
				}
				String nationality = formObject.getNGValue("cmplx_Customer_Nationality");
				String minor_flag=""; 
				try
				{
					if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_age")))
					{
						if(isCustomerMinor(formObject)){
							minor_flag="Y";
						}
					}
					
				}
				catch(Exception ex)
				{
					
				}
				if(!"Compliance".equalsIgnoreCase(formObject.getWFActivityName()))
				{
					query="SELECT distinct doctype,mandatory FROM ng_rlos_DocTable WHERE (ProductName = '"+requested_product+"'  or ProductName='All' or ProductName='PL') and  (SubProductName= '"+requested_subproduct+"'  or SubProductName='All' ) and (Application_Type= '"+application_type+"' or Application_Type='All' or Application_Type='New')and Active = 'Y' and (Designation='"+targetsegCode+"' or Designation='"+empTypeDoc+"' or Designation='All') and (Nationality='"+nationality+"' or Nationality='All') and (minor_flag='"+minor_flag+"' or minor_flag='N') ORDER BY Mandatory desc,DocType";
					formObject.setLocked("IncomingDocNew_Frame1",false);
				}
					else if("Compliance".equalsIgnoreCase(formObject.getWFActivityName())){
					query="SELECT distinct doctype,mandatory FROM ng_rlos_DocTable WHERE ((ProductName = '"+requested_product+"' and SubProductName = '"+requested_subproduct+"') or ProductName='All') and Active = 'Y' and (Designation='' or Designation='All') and (Nationality='"+nationality+"' or Nationality='All') and (minor_flag='"+minor_flag+"' or minor_flag='N') and doctype ='Other Documents' ORDER BY Mandatory desc,DocType";
				}
				//query = "SELECT distinct doctype,mandatory FROM ng_rlos_DocTable WHERE (ProductName = '"+NGFUserResourceMgr_PL.getGlobalVar("RLOS_PersonalLoan")+"' and SubProductName = '"+requested_subproduct+"' /*and (SubProductName = '"+requested_subproduct+"' or SubProductName='All')*/ and Application_Type = '"+application_type+"' and Product_Type = '"+product_type+"' and Active = 'Y' and (Nationality='"+nationality+"' or Nationality='All') and (minor_flag='"+minor_flag+"' or minor_flag='N')) or ProductName='All' order by Mandatory desc";
				PersonalLoanS.mLogger.info( "when row count is  zero inside if"+query+formObject.getWFActivityName());
				docName = formObject.getDataFromDataSource(query);
				PersonalLoanS.mLogger.info(""+ docName);
			}
			else{
				//Query corrected by Deepak.
				//change in queries by Saurabh on 4th Jan 19.
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
							empTypeDoc = "SAL";
							break;
						}
					}
				}
				PersonalLoanS.mLogger.info("EmploymentType: "+formObject.getNGValue("EmploymentType") );
				PersonalLoanS.mLogger.info("TagertSegmentCode: "+targetsegCode );
				String nationality = formObject.getNGValue("cmplx_Customer_Nationality");
				
				//changes done by nikhil for PCSP-699
				String minor_flag=""; 
				try
				{
					if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_age")))
					{
						Double Age=Double.parseDouble(formObject.getNGValue("cmplx_Customer_age"));
						if(Age<21){
							minor_flag="Y";
						}
					}
					
				}
				catch(Exception ex)
				{
					
				}
				
				if("CAC".equalsIgnoreCase(targetsegCode)){
					query="SELECT distinct doctype,mandatory FROM ng_rlos_DocTable WHERE ((ProductName = '"+requested_product+"' and SubProductName = '"+requested_subproduct+"') or ProductName='All') and Active = 'Y' and (Designation='CAC' or Designation='All') and (Nationality='"+nationality+"' or Nationality='All') and (minor_flag='"+minor_flag+"' or minor_flag='N') ORDER BY Mandatory desc,DocType";
				}
				else if("DOC".equalsIgnoreCase(targetsegCode)){
					query="SELECT distinct doctype,mandatory FROM ng_rlos_DocTable WHERE ((ProductName = '"+requested_product+"' and SubProductName = '"+requested_subproduct+"') or ProductName='All') and Active = 'Y' and (Designation='DOC' or Designation='All') and (Nationality='"+nationality+"' or Nationality='All') and (minor_flag='"+minor_flag+"' or minor_flag='N') ORDER BY Mandatory desc,DocType";
				}
				else if("EMPID".equalsIgnoreCase(targetsegCode)){
					query="SELECT distinct doctype,mandatory FROM ng_rlos_DocTable WHERE ((ProductName = '"+requested_product+"' and SubProductName = '"+requested_subproduct+"') or ProductName='All') and Active = 'Y' and (Designation='EMPID' or Designation='All') and (Nationality='"+nationality+"' or Nationality='All') and (minor_flag='"+minor_flag+"' or minor_flag='N') ORDER BY Mandatory desc,DocType";
				}
				else if("NEPALO".equalsIgnoreCase(targetsegCode) || "NEPNAL".equalsIgnoreCase(targetsegCode)){
					query="SELECT distinct doctype,mandatory FROM ng_rlos_DocTable WHERE ((ProductName = '"+requested_product+"' and SubProductName = '"+requested_subproduct+"') or ProductName='All') and Active = 'Y' and (Designation='NEP' or Designation='All') and (Nationality='"+nationality+"' or Nationality='All') and (minor_flag='"+minor_flag+"' or minor_flag='N') ORDER BY Mandatory desc,DocType";
				}
				else if("VIS".equalsIgnoreCase(targetsegCode)){
					query="SELECT distinct doctype,mandatory FROM ng_rlos_DocTable WHERE ((ProductName = '"+requested_product+"' and SubProductName = '"+requested_subproduct+"') or ProductName='All') and Active = 'Y' and (Designation='VC' or Designation='All') and (Nationality='"+nationality+"' or Nationality='All') and (minor_flag='"+minor_flag+"' or minor_flag='N') ORDER BY Mandatory desc,DocType";
				}
				//else if("Compliance".equalsIgnoreCase(formObject.getWFActivityName())){
					//query="SELECT distinct doctype,mandatory FROM ng_rlos_DocTable WHERE ((ProductName = '"+requested_product+"' and SubProductName = '"+requested_subproduct+"') or ProductName='All') and Active = 'Y' and (Designation='' or Designation='All') and (Nationality='"+nationality+"' or Nationality='All') and (minor_flag='"+minor_flag+"' or minor_flag='N') and doctype ='Other Documents' ORDER BY Mandatory desc,DocType";
				//}
				else{
					query="SELECT distinct doctype,mandatory FROM ng_rlos_DocTable WHERE ((ProductName = '"+requested_product+"' and SubProductName = '"+requested_subproduct+"') or ProductName='All') and Active = 'Y' and (Designation='"+targetsegCode+"' or Designation='"+empTypeDoc+"' or Designation='All') and (Nationality='"+nationality+"' or Nationality='All') and (minor_flag='"+minor_flag+"' or minor_flag='N') ORDER BY Mandatory desc,DocType";
				}
			
				
				PersonalLoanS.mLogger.info( "when row count is  zero inside else"+query);
				docName = formObject.getDataFromDataSource(query);

				PersonalLoanS.mLogger.info(""+ docName);
			}

			if(null!=docName && docName.size()>0) {
				for(List<String> row: docName) {
					if("Y".equalsIgnoreCase(row.get(1))) {
						if("Security Cheque".equalsIgnoreCase(row.get(0)) && Check_Elite_Customer(formObject))
						{
							documentNameNonMandatory+=row.get(0)+",";
						}
						else
						{
							documentNameMandatory+=row.get(0)+",";
						}
						
					}
					else {
						
						documentNameNonMandatory+=row.get(0)+",";
					}
					if(null!=row.get(0) && !"".equals(row.get(0)) && !" ".equals(row.get(0)) && !"--Select--".equals(row.get(0)) && !docType.contains(row.get(0))) {
						docType.add(row.get(0));
					}
				}
			}

			//formObject.addItemFromList("cmplx_IncomingDocNew_IncomingDocGrid_Doctype", docType);
			formObject.clear("IncomingDocNew_DocType");
			
			formObject.addItem("IncomingDocNew_DocType", docType);

			if(documentNameMandatory.endsWith(",")) {
				documentNameMandatory = documentNameMandatory.substring(0, documentNameMandatory.length()-1);
			}
			if(documentNameNonMandatory.endsWith(",")) {
				documentNameNonMandatory = documentNameNonMandatory.substring(0, documentNameNonMandatory.length()-1);
			}
			formObject.setNGValue("cmplx_IncomingDocNew_MandatoryDocument", documentNameMandatory);
			formObject.setNGValue("cmplx_IncomingDocNew_NonMandatoryDoc", documentNameNonMandatory);
			//change by saurabh for Deferred Until Date.
			if(!"Deferred".equalsIgnoreCase(formObject.getNGValue("IncomingDocNew_Status"))){
				formObject.setLocked("IncomingDocNew_DeferredUntilDate",true);
			}
			
		}catch(Exception ex) {
			PersonalLoanS.mLogger.info( "exception in fetchIncomingDocRepeater :");
			printException(ex);
		}
	}
	
	public void loadPicklist_ServiceRequest()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PersonalLoanS.mLogger.info( "Inside loadPicklist_ServiceRequest: "); 
		LoadPickList("transType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_TransactionType with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("transferMode", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_TransferMode with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("DispatchChannel", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_DispatchChannel with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("TargetSegmentCode", "select '--Select--' as description,'' as code union select description,code from NG_MASTER_TargetSegmentCode with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("marketingCode", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_MarketingCode with (nolock) where IsActive = 'Y'   order by code");
		LoadPickList("sourceCode", "select Branch , SOL_ID,0 as sno from NG_MASTER_SourceCode with (nolock) where userid = '"+formObject.getNGValue("lbl_user_name_val")+"'  union select distinct Branch,SOL_ID,1 as sno from NG_MASTER_SourceCode with (nolock) where Branch !='' and SOL_ID !='' order by sno");
		LoadPickList("appstatus", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_ApplicationStatus with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("approvalcode", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_ApprovalCode with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("chequeStatus", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_MChequeStatus with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("cmplx_CC_Loan_DDSMode", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_DDSMode with (nolock) where IsActive = 'Y' order by code ");
		LoadPickList("cmplx_CC_Loan_AccType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_AccountType with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("cmplx_CC_Loan_DDSBankAName", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_BankName with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("cmplx_CC_Loan_ModeOfSI", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_ModeOfSI with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("cmplx_CC_Loan_VPSPAckage", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_VPSPackage with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("cmplx_CC_Loan_VPSSourceCode", "select '--Select--' as Branch,'' as SOL_ID union  select distinct Branch,SOL_ID from NG_MASTER_SourceCode with (nolock) where Branch !='' and SOL_ID !=''");
		LoadPickList("cmplx_CC_Loan_StartMonth", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_Month with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("cmplx_CC_Loan_HoldType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_HoldType with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("bankName", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_bankName with (nolock) where IsActive = 'Y' order by code");
		//Below Code added by Shweta
		LoadPickList("cmplx_CC_Loan_BTC_CardProduct", "SELECT CASE WHEN temptble.Description!='--Select--' then concat(temptble.Description,'(',temptble.final_limit,')') else temptble.Description end,temptble.Description FROM (SELECT '--Select--' as Description,'' as final_limit union SELECT distinct card_product as Description,final_limit FROM ng_rlos_IGR_Eligibility_CardLimit WITH (nolock) WHERE child_wi = '"+formObject.getWFWorkitemName()+"' AND cardproductselect = 'true') as temptble order by case when Description='--Select--' then 0 else 1 end");
		LoadPickList("cmplx_CC_Loan_DDS_CardProduct", "SELECT CASE WHEN temptble.Description!='--Select--' then concat(temptble.Description,'(',temptble.final_limit,')') else temptble.Description end,temptble.Description FROM (SELECT '--Select--' as Description,'' as final_limit union SELECT distinct card_product as Description,final_limit FROM ng_rlos_IGR_Eligibility_CardLimit WITH (nolock) WHERE child_wi = '"+formObject.getWFWorkitemName()+"' AND cardproductselect = 'true') as temptble order by case when Description='--Select--' then 0 else 1 end");
		LoadPickList("cmplx_CC_Loan_SI_CardProduct", "SELECT CASE WHEN temptble.Description!='--Select--' then concat(temptble.Description,'(',temptble.final_limit,')') else temptble.Description end,temptble.Description FROM (SELECT '--Select--' as Description,'' as final_limit union SELECT distinct card_product as Description,final_limit FROM ng_rlos_IGR_Eligibility_CardLimit WITH (nolock) WHERE child_wi = '"+formObject.getWFWorkitemName()+"' AND cardproductselect = 'true') as temptble order by case when Description='--Select--' then 0 else 1 end");
		LoadPickList("cmplx_CC_Loan_RVC_CardProduct", "SELECT CASE WHEN temptble.Description!='--Select--' then concat(temptble.Description,'(',temptble.final_limit,')') else temptble.Description end,temptble.Description FROM (SELECT '--Select--' as Description,'' as final_limit union SELECT distinct card_product as Description,final_limit FROM ng_rlos_IGR_Eligibility_CardLimit WITH (nolock) WHERE child_wi = '"+formObject.getWFWorkitemName()+"' AND cardproductselect = 'true') as temptble order by case when Description='--Select--' then 0 else 1 end");
		LoadPickList("Account_No_for_AT","select '--Select--' as Account_ID union select Acctid as Account_ID from  ng_RLOS_CUSTEXPOSE_AcctDetails where child_wi ='"+formObject.getWFWorkitemName()+"' and CifId='"+formObject.getNGValue("cmplx_Customer_CIFNo")+"' order by Account_ID desc");
		LoadPickList("PaymentPurpose", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_PaymentPurpose with (nolock) where IsActive = 'Y' order by code");
		//Above Code added by Shweta

		//cmplx_CC_Loan_HoldType
	}

	public void  loadInCreditCardEnq(FormReference formObject)
	{
		try{
			if(formObject.getLVWRowCount("cmplx_CCenq_cmplx_GR_CCenq")==0){
				String customer_cif=formObject.getNGValue("cmplx_Customer_CIFNO");
				String DOB=formObject.getNGValue("cmplx_Customer_DOb");
				String passport=formObject.getNGValue("cmplx_Customer_PAssportNo");
				String mobNo1 = formObject.getNGValue("AlternateContactDetails_MobileNo1");
				
				String query=" select CRN,isnull(cif,''),Customer_ref_No,isnull(Account_No,''),isnull(Card_Product,''),isnull(Status,''),'',isnull(Decline_Reason,''),FORMAT(Declined_Date,'dd/MM/yyyy') from ng_cas_rejected_table where reject_system='CAPS' and (cif='"+customer_cif+"'or (Passport_No='"+passport+"' and mobile='"+mobNo1+"' and FORMAT(dob,'dd/MM/yyyy')='"+DOB+"'))";
				PersonalLoanS.mLogger.info("queryFor cc Enquiry "+query);
				List<List<String>> records=formObject.getNGDataFromDataCache(query);
				PersonalLoanS.mLogger.info("Query data: "+records);

				if(records.size()>0){
					for(List<String> record:records){
						record.add(formObject.getWFWorkitemName());
						formObject.addItemFromList("cmplx_CCenq_cmplx_GR_CCenq",record);
					}
				}
			}
		}catch(Exception e)
		{
			PersonalLoanS.mLogger.info(e.getMessage());
			printException(e);
		}
	}
	
	public void emp_details(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		int framestate2=formObject.getNGFrameState("EmploymentDetails");
		if(framestate2 == 0){
			PersonalLoanS.mLogger.info("EmploymentDetails already fetched.");
		}
		else {
			
			PersonalLoanS.mLogger.info("fetched employment details");
			loadEmployment();
		}
		

	}

	public void loadEmployment()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmpDetails");
		formObject.setNGFrameState("EmploymentDetails", 0);

		// ++ below code already present - 06-10-2017
		//COndition added by aman to not call these function at CSM
		PersonalLoanS.mLogger.info("end of fetchfrag of employment now on frame exp"+formObject.getNGValue("cmplx_EmploymentDetails_Designation"));
		//formObject.setNGValue("cmplx_EmploymentDetails_Designation",formObject.getNGValue("cmplx_Customer_Designation"));//commented by saurabh1 for populating desination
		if(formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg")==null || formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg")=="")//PCASI-1109
			formObject.setNGValue("cmplx_EmploymentDetails_ApplicationCateg","BAU");//pcasp-1437

		if(formObject.getNGValue("cmplx_EmploymentDetails_marketcode")==null || "".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_marketcode"))
			||"--Select--".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_marketcode")))//PCASI-2617
			{
				formObject.setNGValue("cmplx_EmploymentDetails_marketcode","BAU");
			}
			
		PersonalLoanS.mLogger.info("sagarika end of fetchfrag of employment now on frame exp"+formObject.getNGValue("cmplx_EmploymentDetails_Designation"));
		PersonalLoanS.mLogger.info("sagarika end of fetchfrag of employment now on cust frame exp"+formObject.getNGValue("cmplx_Customer_Designation"));

		formObject.setLocked("EMploymentDetails_Designation_button",true);//corrected button id
		if(!(NGFUserResourceMgr_PL.getGlobalVar("PL_CSM").equalsIgnoreCase(formObject.getWFActivityName()))){	
			employment_fields_hide();
			//employment_fields_IM();//not required in PL
		} else {//CSM fields
				formObject.setVisible("EMploymentDetails_Label36", false);
				formObject.setVisible("cmplx_EmploymentDetails_ClassificationCode", false);
				formObject.setVisible("EMploymentDetails_Label10", false);
				formObject.setVisible("cmplx_EmploymentDetails_marketcode", false);
				formObject.setVisible("EMploymentDetails_Label28", false);
				formObject.setVisible("cmplx_EmploymentDetails_StaffID", false);
				formObject.setVisible("cmplx_EmploymentDetails_Dept", false);
				formObject.setVisible("EMploymentDetails_Label5", false);
				

				formObject.setVisible("EMploymentDetails_Label32", false);
				formObject.setVisible("cmplx_EmploymentDetails_Field_visit_done", false);
				formObject.setVisible("cmplx_EmploymentDetails_empmovemnt", false);
				formObject.setVisible("EMploymentDetails_Label15", false);
				formObject.setLocked("cmplx_EmploymentDetails_DOJPrev",false);
				formObject.setEnabled("cmplx_EmploymentDetails_DOJPrev",true);
		}
		//loadPicklist_Employment();
		PersonalLoanS.mLogger.info("before again Load Pick"+formObject.getNGValue("cmplx_EmploymentDetails_Designation"));

		//loadPicklist4(); //commented by aman because causing issue in load Picklist (already called in all the file)
		//cmplx_DEC_FeedbackStatus
		//RLOS val change Value of subproduct
		//CardDetails_CardProduct Designation_button
		if("LIFESUR".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")))
		{//EMploymentDetails_Label41
			PersonalLoanS.mLogger.info("@sagarika emp lifesur change");
			//formObject.setVisible("EMploymentDetails_Label42",true);
			//  formObject.setVisible("cmplx_EmploymentDetails_InsuranceValue", true);//cmplx_EmploymentDetails_InsuranceValue
			//Cust_ver_sp2_Button1
			formObject.setVisible("cmplx_EmploymentDetails_InsuranceValue", true);
			formObject.setEnabled("cmplx_EmploymentDetails_InsuranceValue", true);
			
			formObject.setVisible("EMploymentDetails_Label43", true);
			formObject.setVisible("cmplx_EmploymentDetails_PremAmt",true);
			formObject.setEnabled("cmplx_EmploymentDetails_PremAmt",true);
			formObject.setVisible("EMploymentDetails_Label44", true);
			formObject.setVisible("cmplx_EmploymentDetails_PremPaid",true);
			formObject.setEnabled("cmplx_EmploymentDetails_PremPaid",true);
			formObject.setVisible("EMploymentDetails_Label46", true);
			formObject.setVisible("EMploymentDetails_Label42",true);
			//formObject.setVisible("EMploymentDetails_Label46",false);
			//formObject.setVisible("EMploymentDetails_Label52",false);
			formObject.setVisible("cmplx_EmploymentDetails_MinimumWait",false);
			formObject.setVisible("cmplx_EmploymentDetails_PremType", true);
			formObject.setEnabled("cmplx_EmploymentDetails_PremType", true);
			formObject.setVisible("EMploymentDetails_Label47", true);
			formObject.setVisible("cmplx_EmploymentDetails_RegPayment",true);
			formObject.setEnabled("cmplx_EmploymentDetails_RegPayment",true);
			formObject.setEnabled("cmplx_EmploymentDetails_MinimumWait",true);
			formObject.setEnabled("cmplx_EmploymentDetails_InsuranceValue", false);
			formObject.setVisible("EMploymentDetails_Label52", true);
			formObject.setVisible("cmplx_EmploymentDetails_MinimumWait", true);
			formObject.setVisible("EMploymentDetails_Label54",false);

			formObject.setVisible("cmplx_EmploymentDetails_MotorInsurance", false);
			formObject.setEnabled("cmplx_EmploymentDetails_InsuranceValue",true);
			//Cust_ver_sp2_Text11
		}//sagarika empid employment change
		else{
			//formObject.setNGValue("cmplx_EmploymentDetails_InsuranceValue","");
			
			formObject.setVisible("cmplx_EmploymentDetails_InsuranceValue",false);
			formObject.setVisible("EMploymentDetails_Label43", false);
			formObject.setVisible("cmplx_EmploymentDetails_PremAmt",false);
		//	formObject.setNGValue("cmplx_EmploymentDetails_PremAmt","");
			formObject.setEnabled("cmplx_EmploymentDetails_PremAmt",false);
			formObject.setVisible("EMploymentDetails_Label44", false);
			formObject.setVisible("cmplx_EmploymentDetails_PremPaid",false);
			//formObject.setNGValue("cmplx_EmploymentDetails_PremPaid","");
			formObject.setEnabled("cmplx_EmploymentDetails_PremPaid",false);
			formObject.setVisible("EMploymentDetails_Label46", true);
			formObject.setVisible("cmplx_EmploymentDetails_PremType",false);
			formObject.setEnabled("cmplx_EmploymentDetails_PremType", false);
		//	formObject.setNGValue("cmplx_EmploymentDetails_PremType","--Select--");
			formObject.setVisible("EMploymentDetails_Label47", false);
			formObject.setVisible("cmplx_EmploymentDetails_RegPayment",false);
			formObject.setEnabled("cmplx_EmploymentDetails_RegPayment",false);
		//	formObject.setNGValue("cmplx_EmploymentDetails_RegPayment",false);
			formObject.setEnabled("cmplx_EmploymentDetails_MinimumWait",false);
		//	formObject.setNGValue("cmplx_EmploymentDetails_MinimumWait",false);
			formObject.setVisible("EMploymentDetails_Label42",false);
			formObject.setVisible("EMploymentDetails_Label46",false);
			formObject.setVisible("EMploymentDetails_Label52",false);
			formObject.setVisible("cmplx_EmploymentDetails_MinimumWait",false);
			//EMploymentDetails_Label42 EMploymentDetails_Label46 EMploymentDetails_Label52 cmplx_EmploymentDetails_MinimumWait


		}
		if("MOTSUR".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")))
		{//cmplx_EmploymentDetails_MotorInsurance

			formObject.setVisible("EMploymentDetails_Label54", true);

			formObject.setVisible("cmplx_EmploymentDetails_MotorInsurance", true);
		//	formObject.setNGValue("cmplx_EmploymentDetails_MotorInsurance","");
			formObject.setEnabled("cmplx_EmploymentDetails_MotorInsurance", true);


		}
		else
		{
			formObject.setVisible("EMploymentDetails_Label54", false);
			//formObject.setNGValue("cmplx_EmploymentDetails_MotorInsurance","");
			formObject.setVisible("cmplx_EmploymentDetails_MotorInsurance", false);
			formObject.setEnabled("cmplx_EmploymentDetails_MotorInsurance", false);
		}
		// NA in PL
		formObject.setVisible("EMploymentDetails_Label7", false);
		formObject.setVisible("cmplx_EmploymentDetails_OtherBankCAC", false);
		formObject.setVisible("EmploymentDetails_Bank_Button", false);

		
		//changed by nikhil pCSP-172
		if(!"EMPID".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")))
		{ PersonalLoanS.mLogger.info("@sagarika empid employment change");
			formObject.setVisible("cmplx_EmploymentDetails_IndusSeg",false);
			formObject.setVisible("EMploymentDetails_Label59",false);
		}
			
		/*if(!NGFUserResourceMgr_CreditCard.getGlobalVar("CC_SAL").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2))){
		formObject.setVisible("EMploymentDetails_Label59", false);
		formObject.setVisible("cmplx_EmploymentDetails_IndusSeg", false);
	}
	else{
		formObject.setVisible("EMploymentDetails_Label59", true);
		formObject.setVisible("cmplx_EmploymentDetails_IndusSeg", true);
	}---commented by akshay for proc 10396*/
		/*if(	NGFUserResourceMgr_CreditCard.getGlobalVar("CC_CreditCard").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1))){
		formObject.setVisible("EMploymentDetails_Label71", false);
		formObject.setVisible("cmplx_EmploymentDetails_EmpContractType", false);
	}
	else{*/
		//formObject.setVisible("EMploymentDetails_Label71", true);
		//formObject.setVisible("cmplx_EmploymentDetails_EmpContractType", true);
		//}
		PersonalLoanS.mLogger.info("before again Load lockALOCfields"+formObject.getNGValue("cmplx_EmploymentDetails_Designation"));

		lockALOCfields();
		PersonalLoanS.mLogger.info("after again Load lockALOCfields"+formObject.getNGValue("cmplx_EmploymentDetails_Designation"));

		if(!(NGFUserResourceMgr_PL.getGlobalVar("PL_CSM").equalsIgnoreCase(formObject.getWFActivityName())))
		{
			PersonalLoanS.mLogger.info("inside employer code CSM condition");
			formObject.setVisible("cmplx_EmploymentDetails_Others", false);
		}
		if("DDVT_Maker".equalsIgnoreCase(formObject.getWFActivityName()))
		{
			formObject.setVisible("cmplx_EmploymentDetails_StaffID", false);
			formObject.setVisible("cmplx_EmploymentDetails_Dept", false);
			formObject.setVisible("cmplx_EmploymentDetails_CntrctExpDate", false);
			formObject.setVisible("EMploymentDetails_Label28", false);
			formObject.setVisible("EMploymentDetails_Label5", false);
			formObject.setVisible("EMploymentDetails_Label6", false);
		}
		if("CAD_Analyst1".equalsIgnoreCase(formObject.getWFActivityName()))
		{
			formObject.setLocked("cmplx_EmploymentDetails_Dept",false);
			formObject.setEnabled("cmplx_EmploymentDetails_Dept", true);
			formObject.setLocked("EMploymentDetails_Designation_button", false);//added by saurabh1 S4 I6
			formObject.setLocked("cmplx_EmploymentDetails_DOJPrev",false);
			formObject.setEnabled("cmplx_EmploymentDetails_DOJPrev",true);

		}
		//changed by nikhil pCSP-172


		if(!"EMPID".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")))
		{
			formObject.setVisible("cmplx_EmploymentDetails_IndusSeg",false);
			formObject.setVisible("EMploymentDetails_Label59",false);
		}
		else
		{
			formObject.setVisible("cmplx_EmploymentDetails_IndusSeg",true);
			formObject.setVisible("EMploymentDetails_Label59",true);
		}

		//added by akshay for proc 12869
		/*if(!"EMPID".equals(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")))
		{
			formObject.setVisible("EMploymentDetails_Label41",false);
			formObject.setVisible("cmplx_EmploymentDetails_LengthOfBusiness",false);
		}*/

		if("IM".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2)))
		{
			formObject.setVisible("cmplx_EmploymentDetails_empmovemnt", true);
			formObject.setVisible("EMploymentDetails_Label15", true);	
		}
		else
		{
			formObject.setVisible("cmplx_EmploymentDetails_empmovemnt", false);
			formObject.setVisible("EMploymentDetails_Label15", false);
		}
		if("false".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_IncInCC")) && "false".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_IncInPL")))
		{
			formObject.setVisible("cmplx_EmploymentDetails_TL_Number", true);
			formObject.setVisible("EMploymentDetails_Label63", true);
			formObject.setVisible("EMploymentDetails_Label65", true);
			formObject.setVisible("cmplx_EmploymentDetails_TL_Emirate", true);
		}
		formObject.setLocked("cmplx_EmploymentDetails_CESflag",true);//Production change
	}
	
	public void  setALOCListed(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//RLOS.mLogger.info( "Inside setALOCListed()");
		String NewEmployer=formObject.getNGValue("cmplx_EmploymentDetails_Others");
		String IncInPL=formObject.getNGValue("cmplx_EmploymentDetails_IncInPL");
		String INcInPL=formObject.getNGValue("cmplx_EmploymentDetails_IncInPL");
		String subprod=formObject.getNGValue("PrimaryProduct");


		if(NGFUserResourceMgr_PL.getGlobalVar("PL_PersonalLoan").equalsIgnoreCase(subprod) && "true".equalsIgnoreCase(NewEmployer) && ("false".equalsIgnoreCase(IncInPL) || "false".equalsIgnoreCase(INcInPL)))
		{
			formObject.setNGValue("cmplx_EmploymentDetails_EmpStatusPL", "CN");
			formObject.setNGValue("cmplx_EmploymentDetails_EmpStatusPL", "CN");
			formObject.setLocked("cmplx_EmploymentDetails_EmpStatusPL", true);
		}


		else if(NGFUserResourceMgr_PL.getGlobalVar("PL_CreditCard").equalsIgnoreCase(subprod) && "true".equalsIgnoreCase(NewEmployer) && ("false".equalsIgnoreCase(IncInPL) || "false".equalsIgnoreCase(INcInPL)))
		{

			formObject.setNGValue("cmplx_EmploymentDetails_EmpStatusPL", "CN");
			formObject.setNGValue("cmplx_EmploymentDetails_EmpStatusPL", "CN");
			formObject.setLocked("cmplx_EmploymentDetails_EmpStatusPL", true);
		}


		else{
			if("true".equalsIgnoreCase(NewEmployer)){
				//formObject.setNGValue("cmplx_EmploymentDetails_EmpStatusPL", "");
				//formObject.setNGValue("cmplx_EmploymentDetails_EmpStatusPL", "");
				formObject.setLocked("cmplx_EmploymentDetails_EmpStatusPL", false);
				formObject.setLocked("cmplx_EmploymentDetails_EmpStatusPL", false);
			}


		}
	}
	
	public void employment_fields_hide()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//Arun 16Nov2017 to get the activity name 
		FormConfig formConfigObject = FormContext.getCurrentInstance().getFormConfig();
		String sActivityName = formConfigObject.getConfigElement("ActivityName");
		PersonalLoanS.mLogger.info( "Inside loadPicklist_Address: "); 
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		for(int i=0;i<n;i++){
			PersonalLoanS.mLogger.info( "Grid Data[1][6] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,5));
			if(NGFUserResourceMgr_PL.getGlobalVar("PL_Salaried").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,5)) || NGFUserResourceMgr_PL.getGlobalVar("PL_SalariedPensioner").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,5))){
				PersonalLoanS.mLogger.info( "Grid Data[1][5] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,5));
				formObject.setVisible("EMploymentDetails_Label15", true);
				formObject.setVisible("cmplx_EmploymentDetails_empmovemnt", true);
				formObject.setVisible("EMploymentDetails_Label32", true);
				formObject.setVisible("cmplx_EmploymentDetails_Field_visit_done", true);
				formObject.setLocked("cmplx_EmploymentDetails_Field_visit_done", true);//PCASI-1122

				
				formObject.setVisible("EMploymentDetails_Combo35", true);
				//++ Below Code added By Yash on Oct 10, 2017  to fix : 5-"Customer classification code field is missing" : Reported By Shashank on Oct 09, 2017++
				formObject.setVisible("EMploymentDetails_Label36", true);
				formObject.setVisible("EMploymentDetails_Combo5", true);
				//++ Below Code added By Yash on Oct 10, 2017  to fix : 5-"Customer classification code field is missing" : Reported By Shashank on Oct 09, 2017++
				//formObject.setVisible("EMploymentDetails_Label33", false);
				//formObject.setVisible("cmplx_EmploymentDetails_channelcode", false); //Arun (12/10)
				//formObject.setVisible("EMploymentDetails_Label37", false);
				//formObject.setVisible("cmplx_EmploymentDetails_Collectioncode", false);
				formObject.setVisible("EMploymentDetails_Label10", true);
				formObject.setVisible("cmplx_EmploymentDetails_marketcode", true);
				//change by saurabh on 11th Jan
				//formObject.setVisible("cmplx_EmploymentDetails_MIS", true);
				//formObject.setVisible("EMploymentDetails_Label38", true);
				//++ Below Code added By Yash on Oct 10, 2017  to fix : 4,7,8,11,13-"NEP type should be disabled ,Staff id should be disabled ,department should be disabled,LOS in current company should be non editable field" : Reported By Shashank on Oct 09, 2017++
				formObject.setVisible("cmplx_EmploymentDetails_StaffID", true);
				formObject.setVisible("EMploymentDetails_Label28", true);
				//formObject.setEnabled("cmplx_EmploymentDetails_StaffID", false); Arun (16-11-17) to hide this field in CSM
				formObject.setVisible("cmplx_EmploymentDetails_NepType", true);
				formObject.setVisible("EMploymentDetails_Label25", true);
				//below code added by nikhil for PCSP-679
				formObject.setLocked("cmplx_EmploymentDetails_NepType", true);
				formObject.setLocked("cmplx_EmploymentDetails_Field_visit_done", true);//PCASI-1122

				/*formObject.setVisible("cmplx_EmploymentDetails_Dept", true);
				formObject.setVisible("EMploymentDetails_Label5", true);*/
				Fields_ApplicationType_Employment();
				if("Cad_Analyst1".equalsIgnoreCase(formObject.getWFActivityName()))
				{//sagarika 
					if("NEPNAL".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")) || "NEPALO".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")))
					{
						 formObject.setLocked("cmplx_EmploymentDetails_NepType", false);
						 formObject.setEnabled("cmplx_EmploymentDetails_NepType", true);
					}
					else
					{
						 formObject.setLocked("cmplx_EmploymentDetails_NepType", true);
						 formObject.setEnabled("cmplx_EmploymentDetails_NepType", false);	
					}
					formObject.setVisible("EMploymentDetails_Label32", true);//pcasi-1059
					formObject.setVisible("cmplx_EmploymentDetails_Field_visit_done", true);
					formObject.setLocked("cmplx_EmploymentDetails_Field_visit_done", false);//as per FLV		
					formObject.setLocked("cmplx_EmploymentDetails_LOSPrevious", false);//as per FLV
					formObject.setLocked("cmplx_EmploymentDetails_VisaSponser",false);
				} else {
					formObject.setLocked("cmplx_EmploymentDetails_DOJPrev",true);
					formObject.setEnabled("cmplx_EmploymentDetails_DOJPrev",false);
				}
				
				formObject.setEnabled("cmplx_EmploymentDetails_Dept", false);
				formObject.setEnabled("cmplx_EmploymentDetails_tenancycntrctemirate", false);
				formObject.setLocked("cmplx_EmploymentDetails_LOS",true);
				

				//formObject.setTop("EMploymentDetails_Label10",10);
				//formObject.setTop("cmplx_EmploymentDetails_marketcode",26);

				//++ Below Code added By Yash on Oct 10, 2017  to fix : 4,7,8,11,13-"NEP type should be disabled ,Staff id should be disabled ,department should be disabled,LOS in current company should be non editable field" : Reported By Shashank on Oct 09, 2017++

				formObject.setLeft("EMploymentDetails_Label10",794);
				formObject.setLeft("cmplx_EmploymentDetails_marketcode",794);
				formObject.setLeft("EMploymentDetails_Label4",290);//PCASI-2842
				formObject.setLeft("cmplx_EmploymentDetails_PromotionCode",290);
			}
			else 
			{
				//makeSheetsVisible(tabName, "3");
			}
		}//Below code was unreachable, moved to the main method
		
		//above code added by Arun (16/11/17) to hide in CSM
	}
	
	//Below Code is not applicable in PL
	public void employment_fields_IM()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//Arun 16Nov2017 to get the activity name 
		FormConfig formConfigObject = FormContext.getCurrentInstance().getFormConfig();
		String sActivityName = formConfigObject.getConfigElement("ActivityName");
		PersonalLoanS.mLogger.info( "Inside loadPicklist_Address: "); 
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		for(int i=0;i<n;i++){
			PersonalLoanS.mLogger.info( "Grid Data[1][5] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,5));
			if(NGFUserResourceMgr_PL.getGlobalVar("PL_IM").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2))){
				PersonalLoanS.mLogger.info( "Grid Data[1][6] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,5));
				formObject.setVisible("EMploymentDetails_Label15", true);
				formObject.setVisible("cmplx_EmploymentDetails_empmovemnt", true);
				formObject.setVisible("EMploymentDetails_Label32", true);
				formObject.setVisible("cmplx_EmploymentDetails_Field_visit_done", true);
				formObject.setVisible("EMploymentDetails_Combo35", true);
				//++ Below Code added By Yash on Oct 10, 2017  to fix : 5-"Customer classification code field is missing" : Reported By Shashank on Oct 09, 2017++
				formObject.setVisible("EMploymentDetails_Label36", true);
				formObject.setVisible("EMploymentDetails_Combo5", true);
				//++ Above Code added By Yash on Oct 10, 2017  to fix : 5-"Customer classification code field is missing" : Reported By Shashank on Oct 09, 2017++
				formObject.setVisible("EMploymentDetails_Label33", false);
				formObject.setVisible("cmplx_EmploymentDetails_channelcode", false);
				formObject.setVisible("EMploymentDetails_Label37", false);
				formObject.setVisible("cmplx_EmploymentDetails_Collectioncode", false);
				formObject.setVisible("EMploymentDetails_Label10", true);
				formObject.setVisible("cmplx_EmploymentDetails_marketcode", true);
				//formObject.setVisible("cmplx_EmploymentDetails_MIS", true);
				//formObject.setVisible("EMploymentDetails_Label38", true);
				//++ Below Code added By Yash on Oct 10, 2017  to fix : 4,7,8,11,13-"NEP type should be disabled ,Staff id should be disabled ,department should be disabled,LOS in current company should be non editable field" : Reported By Shashank on Oct 09, 2017++
				formObject.setVisible("cmplx_EmploymentDetails_StaffID", true);
				formObject.setVisible("EMploymentDetails_Label28", true);
				//formObject.setEnabled("cmplx_EmploymentDetails_StaffID", false); Arun (16-11-17) to hide this field in CSM
				formObject.setVisible("cmplx_EmploymentDetails_NepType", true);
				formObject.setVisible("EMploymentDetails_Label25", true);
				formObject.setLocked("cmplx_EmploymentDetails_NepType", false);
				/*formObject.setVisible("cmplx_EmploymentDetails_Dept", true);
				formObject.setVisible("EMploymentDetails_Label5", true);*/
				formObject.setEnabled("cmplx_EmploymentDetails_Dept", false);
				formObject.setEnabled("cmplx_EmploymentDetails_tenancycntrctemirate", true);
				formObject.setLocked("cmplx_EmploymentDetails_LOS",true);
			

				//formObject.setTop("EMploymentDetails_Label10",10);
				//formObject.setTop("cmplx_EmploymentDetails_marketcode",26);

				//++ Above Code added By Yash on Oct 10, 2017  to fix : 4,7,8,11,13-"NEP type should be disabled ,Staff id should be disabled ,department should be disabled,LOS in current company should be non editable field" : Reported By Shashank on Oct 09, 2017++

				formObject.setLeft("EMploymentDetails_Label10",794);
				formObject.setLeft("cmplx_EmploymentDetails_marketcode",794);
				formObject.setLeft("EMploymentDetails_Label4",24);
				formObject.setLeft("cmplx_EmploymentDetails_PromotionCode",24);
				//below code added by Arun (16/11/17) to hide in CSM
				if (NGFUserResourceMgr_PL.getGlobalVar("PL_CSM").equalsIgnoreCase(sActivityName))
				{
					formObject.setVisible("EMploymentDetails_Label36", false); 
					formObject.setVisible("EMploymentDetails_Label10", false);
					formObject.setVisible("cmplx_EmploymentDetails_marketcode", false);
					formObject.setVisible("EMploymentDetails_Label28", false);
					formObject.setVisible("cmplx_EmploymentDetails_StaffID", false);
					formObject.setVisible("cmplx_EmploymentDetails_Dept", false);
					formObject.setVisible("EMploymentDetails_Label5", false);

					formObject.setVisible("EMploymentDetails_Label32", false);
					formObject.setVisible("cmplx_EmploymentDetails_Field_visit_done", false);
					formObject.setVisible("cmplx_EmploymentDetails_empmovemnt", false);
					formObject.setVisible("EMploymentDetails_Label15", false);

				}
				//above code added by Arun (16/11/17) to hide in CSM
			}
			else 
			{
				//makeSheetsVisible(tabName, "3");
			}
		}
	}
	
	public void lockALOCfields() {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setLocked("cmplx_EmploymentDetails_EmpName",true);
		formObject.setLocked("EMploymentDetails_Text20",true); //Imran
		formObject.setLocked("cmplx_EmploymentDetails_EmpIndusSector",true);
		formObject.setLocked("cmplx_EmploymentDetails_Indus_Macro",true);
		formObject.setLocked("cmplx_EmploymentDetails_Indus_Micro",true);
		formObject.setLocked("cmplx_EmploymentDetails_EmpStatusPL",true);
		formObject.setLocked("cmplx_EmploymentDetails_EmpStatusCC",true);
		formObject.setLocked("cmplx_EmploymentDetails_FreezoneName",true);
		formObject.setLocked("cmplx_EmploymentDetails_IncInCC",true);
		formObject.setLocked("cmplx_EmploymentDetails_IncInPL",true);
		formObject.setLocked("cmplx_EmploymentDetails_Emp_Categ_PL",true);
		formObject.setLocked("cmplx_EmploymentDetails_Status_PLNational",true);
		formObject.setLocked("cmplx_EmploymentDetails_Status_PLExpact",true);
		formObject.setLocked("cmplx_EmploymentDetails_ownername",true);
		formObject.setLocked("cmplx_EmploymentDetails_NOB",true);
		formObject.setLocked("EMploymentDetails_Combo34",true);
			//PCASI - 3715
		formObject.setLocked("cmplx_EmploymentDetails_highdelinq",true);
		formObject.setLocked("cmplx_EmploymentDetails_dateinPL",true);
		formObject.setLocked("cmplx_EmploymentDetails_dateinCC",true);
		formObject.setLocked("cmplx_EmploymentDetails_remarks",true);
		formObject.setLocked("cmplx_EmploymentDetails_Remarks_PL",true);
		formObject.setEnabled("cmplx_EmploymentDetails_Remarks_PL",false);
		formObject.setEnabled("cmplx_EmploymentDetails_remarks",false);

	}
	
	//done by sagarika for view button
	//below code changed by nikhil for View button functionality
	public void LoadView(String eventName)
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		if ("Customer".equalsIgnoreCase(eventName))
			{
			PersonalLoanS.mLogger.info( "Inside customer fragment");
			formObject.setLocked("Customer_Frame1",true);
			formObject.setLocked("Nationality_Button_View",false);
			formObject.setEnabled("Nationality_Button_View", true);
			formObject.setLocked("SecNationality_Button_View",false);
			formObject.setEnabled("SecNationality_Button_View", true);
			formObject.setLocked("Third_Nationality_Button_View",false);
			formObject.setEnabled("Third_Nationality_Button_View", true);
			}
		else if ("Cust_ver_sp2".equalsIgnoreCase(eventName))
			{
			formObject.setLocked("Customer_Details_Verification1",true);
			formObject.setLocked("Designation_button3_View", false);
			formObject.setEnabled("Designation_button3_View", true);
			formObject.setLocked("Designation_button5_View", false);
			formObject.setEnabled("Designation_button5_View",true);
			
			}
		else if("EmploymentVerification_s2".equalsIgnoreCase(eventName)||"Employment_Verification".equalsIgnoreCase(eventName))
		{
			PersonalLoanS.mLogger.info("Inside plcomm employmentverf");
			PersonalLoanS.mLogger.info( "Inside employment verification s2");
			formObject.setLocked("EmploymentVerification_s2_Frame1",true);
			formObject.setLocked("EmploymentVerification_s2_Designation_button4_View",false);
			formObject.setEnabled("EmploymentVerification_s2_Designation_button4_View", true);
			formObject.setLocked("EmploymentVerification_s2_Designation_button6_View",false);
			formObject.setEnabled("EmploymentVerification_s2_Designation_button6_View", true);
			formObject.setLocked("EmploymentVerification_s2_desig_visa_view",false);
			formObject.setEnabled("EmploymentVerification_s2_desig_visa_view", true);
			formObject.setLocked("EmploymentVerification_s2_desig_visa_update_view",false);
			formObject.setEnabled("EmploymentVerification_s2_desig_visa_update_view", true);
			formObject.setLocked("EmploymentVerification_s2_search_TL_number",true); //pcasi 3606
			formObject.setEnabled("EmploymentVerification_s2_search_TL_number",false);

		}
		
		else if("EMploymentDetails".equalsIgnoreCase(eventName))
	{
		PersonalLoanS.mLogger.info( "Inside employment fragment");
		formObject.setLocked("EMploymentDetails_Frame1",true);
		formObject.setLocked("EMploymentDetails_Designation_button_View",false);
		formObject.setEnabled("EMploymentDetails_Designation_button_View", true);
		formObject.setLocked("EMploymentDetails_DesignationAsPerVisa_button_View",false);
		formObject.setEnabled("EMploymentDetails_DesignationAsPerVisa_button_View", true);

		
	}
		else if("PartMatch".equalsIgnoreCase(eventName))
		{
			PersonalLoanS.mLogger.info( "Inside employment fragment");
			formObject.setLocked("PartMatch_Frame1",true);
			formObject.setLocked("Nationality_Button1_View",false);
			
		}
		else if("AddressDetails".equalsIgnoreCase(eventName))
	{
		PersonalLoanS.mLogger.info( "Inside address fragment");
        formObject.setLocked("AddressDetails_Frame1",true);
        formObject.setLocked("Button_City_View",false);
		formObject.setLocked("Button_State_View",false);
		formObject.setLocked("AddressDetails_Button1_View",false);
		
	}
		else if("AltContactDetails".equalsIgnoreCase(eventName))
		{
			PersonalLoanS.mLogger.info( "Inside con fragment");
	        formObject.setLocked("AltContactDetails_Frame1",true);
	        formObject.setLocked("CardDispatchToButton_View",false);
	       /* formObject.setNGValue("AlternateContactDetails_carddispatch","988");
	        formObject.setLocked("AlternateContactDetails_carddispatch",true);*/
			
		}
		else if("OECD".equalsIgnoreCase(eventName))
		{
            PersonalLoanS.mLogger.info( "Inside oecd fragment");
	        formObject.setLocked("OECD_Frame8",true);
	        formObject.setLocked("ButtonOECD_State_View",false);
			
		}
		
	}
	
	public String checkDedupAirArabia(String cardProd){
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String query = "select case when DESCRIPTION like '%Air Arabia%' then 'yes' else 'no' end from ng_master_cardproduct where code = '"+cardProd+"'";
			List<List<String>> result = formObject.getDataFromDataSource(query);
			if(null!=result && !result.isEmpty()){
				if("yes".equals(result.get(0).get(0))){
					if(null!= formObject.getNGValue("AlternateContactDetails_AirArabiaIdentifier") && !"".equals(formObject.getNGValue("AlternateContactDetails_AirArabiaIdentifier"))){
						String dedupQuery = "with temp as(select passportno,AIR_ARABIA_IDENTIFIER from ng_rlos_customer c join ng_RLOS_AltContactDetails a on c.wi_name=a.wi_name where AIR_ARABIA_IDENTIFIER is not null and c.wi_name != '"+formObject.getWFWorkitemName()+"' and c.PassportNo != '"+formObject.getNGValue("cmplx_Customer_PAssportNo")+"'),temp2 as(select distinct PassportNo from temp where AIR_ARABIA_IDENTIFIER = '"+formObject.getNGValue("AlternateContactDetails_AirArabiaIdentifier")+"')select case when (select count(*) from temp2)>0 then 'no' else 'yes' end";
						List<List<String>> dedupResult = formObject.getDataFromDataSource(dedupQuery);
						if(null != dedupResult && !dedupQuery.isEmpty()){
							if("yes".equals(dedupResult.get(0).get(0))){
								return "Pass"; // if the dedup logic logic results that air arabia identifier is unique.
							}
							else{
								return "Fail"; //if the dedup logic logic results that air arabia identifier is not unique.
							}
						}
						else{
							return "Pass";// query fail case for dedup logic.
						}
					}
					else{
						return "Invalid";// if air arabia identifier not filled in form
					}
				}
				else{
					return "Pass";//if card is not air arabia card
				}
			}
			else{
				return "Pass";//query fail case for checking if card is arabia card.
			}
		}catch(Exception ex){
			PersonalLoanS.mLogger.info( "Exception in checkDedupAirArabia Function");
			printException(ex);
			return "Pass";
		}

	}
	
	public void Unlock_ATC_Fields()		
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try
		{
			String[] Primary_fields;
			String[] Fields_Name;
			Primary_fields=NGFUserResourceMgr_PL.getGlobalVar("Primary_mismatch_fields").split(":");
			for(int i=0;i<Primary_fields.length;i++)
			{
				Fields_Name=Primary_fields[i].split("#");
				if(formObject.getNGValue("ATC_Mismatch_Fields").contains(Fields_Name[0]))
				{
					for(int j=1;j<Fields_Name.length;j++)
					{
						formObject.setLocked(Fields_Name[j], false);
						formObject.setEnabled(Fields_Name[j], true);
					}
				}
			}
		}
		catch(Exception ex)
		{
			PersonalLoanS.mLogger.info("Error In Unlock_ATC_Fields" + ex.getStackTrace());
		}
	}

	
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 3/10/2019              
	Author                              : Bandana             
	Description                         : make required sheet visible on WI         

	 ***********************************************************************************  */
	public void makeSheetsInvisible(String tabName, String sheetNo) {

		try {
			PersonalLoanS.mLogger.info( "Inside makeSheetsInvisible()" );
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String sheetArr[] = sheetNo.split(",");

			for (int i = 0; i < sheetArr.length; i++) {
				formObject.setSheetVisible(tabName, Integer.parseInt(sheetArr[i]), false);
			}
		} catch (Exception e) {
			//new CC_CommonCode();//Commented for sonar
			PersonalLoanS.mLogger.info( "Exception: ");
			PLCommon.printException(e);
		}

	}
	
	//method added by bandana for fcu
	//done by sagarika for view button
	public void LoadViewButton(String buttonName) throws ValidatorException{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String alert="";
		try{
		PersonalLoanS.mLogger.info("inside function1 is :");
		String[] button_new=buttonName.split("_View");
		String button=button_new[0];

		String val=NGFUserResourceMgr_PL.getMasterManager(button);
		String[] value= val.split(":");
		PersonalLoanS.mLogger.info("value= :"+value);
		String query;
		String stableName="";
		String sColumnName="";
		String sfieldName="";
		String sHeaderName="";

		//CreditCard.mL;ogger.info("Invalid entry value.length for :"+val);
		//CreditCard.mLogger.info("Invalid entry value.length for :"+value.length);

		stableName=value[0];
		sColumnName=value[1];
		sfieldName=value[2];
		sHeaderName=value[3];
		PersonalLoanS.mLogger.info("Invalid entry value.length for :"+stableName);
		PersonalLoanS.mLogger.info("column name  :"+sfieldName);
		PersonalLoanS.mLogger.info("column name :"+formObject.getNGValue(sfieldName));
		PersonalLoanS.mLogger.info("column name static :"+formObject.getNGValue("cmplx_Customer_Nationality"));

		query="select Description from "+stableName+" with (nolock) where code='"+formObject.getNGValue(sfieldName)+"' AND isActive='Y'";
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

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha             
	Description                         : External Bounced Cheques        

	 ***********************************************************************************  */
	public String ExternalBouncedCheques(){
		PersonalLoanS.mLogger.info("RLOSCommon java file"+ "inside ExternalBouncedCheques : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "SELECT cifid,number,amount,reasoncode,returndate,providerno FROM ng_rlos_cust_extexpo_ChequeDetails  with (nolock) where child_wi = '"+formObject.getWFWorkitemName()+"' and Request_Type = 'ExternalExposure'";
		PersonalLoanS.mLogger.info("ExternalBouncedCheques sQuery"+sQuery);
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		PersonalLoanS.mLogger.info("ExternalBouncedCheques list size"+OutputXML.size());

		for (int i = 0; i<OutputXML.size();i++){

			String CifId = formObject.getNGValue("cmplx_Customer_CIFNO");
			String chqNo = "";
			String Amount = "";
			String Reason = ""; 
			String returnDate = "";
			String providerNo = "";


			if(!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals("")) ){
				chqNo = OutputXML.get(i).get(1).toString();
			}
			if(!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2).equals("")) ){
				Amount = OutputXML.get(i).get(2).toString();
			}
			if(!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals("")) ){
				Reason = OutputXML.get(i).get(3).toString();
			}
			if(!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals("")) ){
				returnDate = OutputXML.get(i).get(4).toString();
			}
			if(!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5).equals("")) ){
				providerNo = OutputXML.get(i).get(5).toString();
			}


			add_xml_str = add_xml_str + "<ExternalBouncedCheques><applicant_id>"+CifId+"</applicant_id>";
			add_xml_str = add_xml_str + "<external_bounced_cheques_id>"+""+"</external_bounced_cheques_id>";
			add_xml_str = add_xml_str + "<bounced_cheque>"+""+"</bounced_cheque>";
			add_xml_str = add_xml_str + "<cheque_no>"+chqNo+"</cheque_no>";
			add_xml_str = add_xml_str + "<amount>"+Amount+"</amount>";
			add_xml_str = add_xml_str + "<reason>"+Reason+"</reason>";
			add_xml_str = add_xml_str + "<return_date>"+returnDate+"</return_date>"; // to be populated later
			add_xml_str = add_xml_str + "<provider_no>"+providerNo+"</provider_no><company_flag>N</company_flag></ExternalBouncedCheques>"; // to be populated later


		}
		PersonalLoanS.mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}
	
	//above method added for fcu by bandana 
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Shweta             
	Description                         : Set Cad Analsyst Decision  controls         

	 ***********************************************************************************  */
	public void Decision_cadanalyst1(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//changed by akshay on 6/12/17 for decision alignment

		/*List<String> controlList_visible= Arrays.asList("DecisionHistory_Label10","cmplx_DEC_New_CIFID","DecisionHistory_Label5","DecisionHistory_Text5","DecisionHistory_Label9",
				"DecisionHistory_Combo4","DecisionHistory_Text6","DecisionHistory_Label4","DecisionHistory_Combo5","DecisionHistory_Label16",
				"cmplx_DEC_HighDeligatinAuth","DecisionHistory_Button4","DecisionHistory_Label18","cmplx_DEC_ReferTo","DecisionHistory_Label13",
				"cmplx_DEC_DeviationCode","DecisionHistory_Label14","cmplx_DEC_DectechDecision","DecisionHistory_Label15","cmplx_DEC_ScoreGrade",
				"DecisionHistory_calReElig","DecisionHistory_ManualDevReason","cmplx_DEC_Manual_Deviation","DecisionHistory_ManualDevReason");

		List<String> controlList_hidden= Arrays.asList("cmplx_DEC_ContactPointVeri","DecisionHistory_chqbook","DecisionHistory_Label6",
				"cmplx_DEC_IBAN_No","DecisionHistory_Label7","cmplx_DEC_NewAccNo","DecisionHistory_Label8","cmplx_DEC_ChequebookRef",
				"DecisionHistory_Label9","cmplx_DEC_DCR_Refno","DecisionHistory_Label27","cmplx_DEC_Cust_Contacted","DecisionHistory_Combo5");

		List<String> controlList_setLeft= Arrays.asList("DecisionHistory_Button4","DecisionHistory_Decision_Label1","cmplx_DEC_VerificationRequired",
				"DecisionHistory_Label3","cmplx_DEC_Strength","DecisionHistory_Label4",
				"cmplx_DEC_Weakness","DecisionHistory_Decision_Label4","cmplx_DEC_Remarks","DecisionHistory_Label13","cmplx_DEC_DeviationCode",
				"DecisionHistory_Label14","cmplx_DEC_DectechDecision","DecisionHistory_Label15","cmplx_DEC_ScoreGrade","DecisionHistory_Decision_Label3",
				"cmplx_DEC_Decision","DecisionHistory_Label1","cmplx_DEC_ReferReason","DecisionHistory_Label16","cmplx_DEC_HighDeligatinAuth",
				"DecisionHistory_calReElig","cmplx_DEC_Manual_Deviation","DecisionHistory_ManualDevReason","DecisionHistory_Label18","cmplx_DEC_ReferTo");

		List<String> controlList_setTop= Arrays.asList("DecisionHistory_Button4","DecisionHistory_Decision_Label1","cmplx_DEC_VerificationRequired",
				"DecisionHistory_Label3","cmplx_DEC_Strength","DecisionHistory_Label4","cmplx_DEC_Weakness","DecisionHistory_Decision_Label4",
				"cmplx_DEC_Remarks","DecisionHistory_Label13","cmplx_DEC_DeviationCode","DecisionHistory_Label14","cmplx_DEC_DectechDecision",
				"DecisionHistory_Label15","cmplx_DEC_ScoreGrade","DecisionHistory_Decision_Label3","cmplx_DEC_Decision",
				"DecisionHistory_Label1","cmplx_DEC_ReferReason","DecisionHistory_Label16","cmplx_DEC_HighDeligatinAuth","DecisionHistory_calReElig",
				"cmplx_DEC_Manual_Deviation","DecisionHistory_ManualDevReason","DecisionHistory_Label18","cmplx_DEC_ReferTo","DecisionHistory_Decision_ListView1","DecisionHistory_save");

		List<String> setLeft_Values= Arrays.asList("590","305","305",
				"24","24","305","305","590",
				"590","24","24","305","305",
				"590","590","24","24","305",
				"305","590","590","784",
				"24","304","590","590");

		List<String> setTop_Values= Arrays.asList("8","8","24",
				"56","72","56","72","56","72","128",
				"145","128","145","128","145",
				"208","224","208","224","208",
				"224","210","256","256",
				"250","266","300","470");
		 */
		//setVisibleTrue(controlList_visible);
		//setVisibleFalse(controlList_hidden);
		//setTop(controlList_setTop,setTop_Values);
		//setLeft(controlList_setLeft,setLeft_Values);
		//formObject.setHeight("DecisionHistory_Frame1", 500);	
		//formObject.setHeight("DecisionHistory", 520);	   
		formObject.setLocked("cmplx_Decision_Dectech_decsion",true);
		formObject.setLocked("cmplx_Decision_Highest_delegauth",true);
		formObject.setLocked("cmplx_Decision_CADDecisiontray",true);
		formObject.setLocked("cmplx_Decision_Deviationcode",true);
		formObject.setLocked("cmplx_Decision_Manual_deviation_reason",true);
		formObject.setLocked("cmplx_Decision_VERIFICATIONREQUIRED",true);

		/*	//Code Change By aman to save Highest Delegation Auth
		String sQuery = "select distinct(Delegation_Authorithy) from ng_rlos_IGR_Eligibility_CardProduct where Child_Wi ='"+formObject.getWFWorkitemName()+"'";
		List<List<String>> OutputXML = formObject.getNGDataFromDataCache(sQuery);



		if(!OutputXML.isEmpty()){
			String HighDel=OutputXML.get(0).get(0);
			if(OutputXML.get(0).get(0)!=null && !OutputXML.get(0).get(0).isEmpty() &&  !OutputXML.get(0).get(0).equals("") && !OutputXML.get(0).get(0).equalsIgnoreCase("null") ){
				{

					formObject.setNGValue("cmplx_DEC_HighDeligatinAuth", HighDel);
				}
			}
		}	
		//Code Change By aman to save Highest Delegation Auth
		 */
		//ended by Akshay on 5/9/17
		loadPicklist3();

		LoadPickList("cmplx_Decision_CADDecisiontray", "select convert(varchar, Refer_Credit) as col from ng_master_ReferCredit with (nolock) order by col desc");


		//changed by akshay on 6/12/17 for decision alignment
	}
	//ELigibiltyAndProductInfo_EFMS_Status
	public void fetchFinacleCoreFrag(String Event){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.fetchFragment("Finacle_Core", "FinacleCore", "q_FinacleCore");
		formObject.setNGFrameState("Finacle_Core", 0);
		if(!"IncomeDetails_FinacialSummarySelf".equals(Event))
		{
		formObject.setNGFocus("cmplx_FinacleCore_FinaclecoreGrid");
		}
		String activityName = formObject.getWFActivityName();
		//++Below code added by nikhil 8/11/17 as per CC issues sheet
		int prd_count =formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		LoadPickList("FinacleCore_ChequeReturnCode", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_Cheque_Reason with (nolock) order by Code");
		String emp_type = "";
		if(prd_count>-1){
			 emp_type = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5);
			 PersonalLoanS.mLogger.info("fetchFinacleCoreFrag> emp type is: "+emp_type);
			 if("Salaried".equalsIgnoreCase(emp_type) || "Salaried Pensioner".equalsIgnoreCase(emp_type)||"Pensioner".equalsIgnoreCase(emp_type))
				{
					formObject.setVisible("FinacleCore_Frame2", false);  
					formObject.setVisible("FinacleCore_Frame3", false);
					formObject.setVisible("FinacleCore_avgbal", false);
					formObject.setVisible("FinacleCore_Frame8", false);
					//commented by nikhil PCAS-2250 
					//formObject.setHeight("Finacle_Core", formObject.getTop("FinacleCore_Frame5")+formObject.getHeight("FinacleCore_Frame5")+50);
					 PersonalLoanS.mLogger.info(formObject.getHeight("Finacle_Core"));

					//adjustFrameTops("Finacle_Core,MOL,World_Check,Reject_Enquiry,Salary_Enquiry,PersonalLoanS_Enq,Case_hist,LOS");
					
				}
			 else{
					//fetchfinacleAvgRepeater();
					//fetchfinacleTORepeater();
					//fetchfinacleDocRepeater();
			 }
				//--Above code added by nikhil 8/11/17 as per CC issues sheet

				//Deepak Code change need to done for salaried below is mentioned for self-employed

				//fetchTransumDet();
				//if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6).equalsIgnoreCase("Salaried")){
				if("DDVT_Maker".equalsIgnoreCase(activityName)){
					formObject.setLocked("FinacleCore_Frame1", true);
				}
				try{
					//below query modified by nikhil 11/12/17
					String query="";
					//changed by nikhil for PCAS-1260
					if("CAD_Analyst1".equalsIgnoreCase(formObject.getWFActivityName()))
					{
						PersonalLoanS.mLogger.info( "Inside fetchFinacleCore Grid for CAD_Analyst1 ");
						formObject.clear("cmplx_FinacleCore_FinaclecoreGrid");
						query="select AcctType,CustRoleType,AcctId,AcctNm,AccountOpenDate,AcctStat,ClearBalanceAmount,AvailableBalance,EffectiveAvailableBalance,CreditGrade,'',child_wi, AcctSegment,AcctSubSegment from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"' and AcctStat !='CLOSED'";
						List<List<String>> list_acc=formObject.getDataFromDataSource(query);
						for(List<String> mylist : list_acc)
						{
							PersonalLoanS.mLogger.info( "Data to be added in Grid account Grid: "+mylist.toString());
							formObject.addItemFromList("cmplx_FinacleCore_FinaclecoreGrid", mylist);
						}
						formObject.clear("cmplx_FinacleCore_liendet_grid");
						query="select AcctId,LienId,LienAmount,LienRemarks,isnull(LienReasonCode,''),isnull(LienStartDate,''),isnull(LienExpDate,'') from ng_rlos_FinancialSummary_LienDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
						//changed ended
						List<List<String>> list_lien=formObject.getDataFromDataSource(query);
						for(List<String> mylist : list_lien)
						{
							PersonalLoanS.mLogger.info( "Data to be added in Grid: "+mylist.toString());
							formObject.addItemFromList("cmplx_FinacleCore_liendet_grid", mylist);
						}
						formObject.clear("cmplx_FinacleCore_sidet_grid");
						query="select AcctId,SiAmount,SiRemarks,'',isnull(NextExecDate,'') from ng_rlos_FinancialSummary_SiDtls with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
						List<List<String>> list_SIDet=formObject.getDataFromDataSource(query);//Nikhil changes done to load dynamic data badiya
						//changed ended

						for(List<String> mylist : list_SIDet)
						{
							PersonalLoanS.mLogger.info( "Data to be added in Grid: "+mylist.toString());

							formObject.addItemFromList("cmplx_FinacleCore_sidet_grid", mylist);
						}
						
						
						//Deepak added on 22July2019 to show Cheque return details PCAS-2159. 
							//query changed 31/1/18
							String checkSubquery = "(select count(AcctId) from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where AcctId = AcctId and CifId=CifId and Child_Wi='"+formObject.getWFWorkitemName()+"')>1";
							String elseSubquery = "(select distinct(Account_Type) from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where AcctId = AcctId and CifId=CifId and Child_Wi='"+formObject.getWFWorkitemName()+"')";
							//query changed by shivang, added order by return date.
							query="select  isnull(AccountType,''),isnull(Account_Number,''),chqtype,chqamt,ChequeNumber,isnull(format(convert(datetime,chqretdate),'dd/MM/yyyy'),'')as returnDate,chqretcode,isnull(ddsclear,''),typeofret,'','','','',DDS_wi_name,Consider_For_Obligations,source from NG_RLOS_GR_DDSreturn where DDS_wi_name ='"+formObject.getWFWorkitemName()+"' union all select  case when (select count(AcctId) from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where AcctId = AcctId and CifId=CifId and Child_Wi='"+formObject.getWFWorkitemName()+"')>1 then 'Individual_CIF' else (select distinct(Account_Type) from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where AcctId = AcctId and CifId=CifId and Child_Wi='"+formObject.getWFWorkitemName()+"') end ,AcctId,returntype,returnAmount,returnNumber,isnull(format(convert(datetime,returnDate),'dd/MM/yyyy'),'')as returnDate,isnull(retReasonCode,''),isnull(instrumentdate,''),returnType,'','','','','"+formObject.getWFWorkitemName()+"','true','Internal' from ng_rlos_FinancialSummary_ReturnsDtls with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"' and returnNumber not in (select ChequeNumber from NG_RLOS_GR_DDSreturn where DDS_wi_name='"+formObject.getWFWorkitemName()+"') union all  select '-','-',ChqType,Amount,Number,isnull(format(convert(datetime,ReturnDate),'dd/MM/yyyy'),'')as returnDate,ReasonCode,'',ProviderNo,'','','','',Child_wi,'true','External' from ng_rlos_cust_extexpo_ChequeDetails where Child_wi ='"+formObject.getWFWorkitemName()+"' and (Number<>'' OR Number is null) and Number not in (select isnull(ChequeNumber,'') from NG_RLOS_GR_DDSreturn where DDS_wi_name='"+formObject.getWFWorkitemName()+"') ORDER BY returnDate DESC";
							List<List<String>> list_DDSDet=formObject.getDataFromDataSource(query);
							PersonalLoanS.mLogger.info( "query to be added in list_DDSDet Grid: "+query);
							String return_date="";
							try
							{
								int count=list_DDSDet.size();

								for(int i=0;i<count;i++)
								{
									return_date=list_DDSDet.get(i).get(5);
									String[] date=return_date.split("-");
									return_date=date[2]+"/"+date[1]+"/"+date[0];
									list_DDSDet.get(i).set(5, return_date);
								}
							}
							catch(Exception ex)
							{
								PersonalLoanS.mLogger.info( "Error parsing date ::"+return_date);
							}

							//changed ended
							formObject.clear("cmplx_FinacleCore_DDSgrid");
							for(List<String> mylist : list_DDSDet)
							{
								PersonalLoanS.mLogger.info( "Data to be added in list_DDSDet Grid: "+mylist.toString());
								formObject.addItemFromList("cmplx_FinacleCore_DDSgrid", mylist);
							}
					}
					else
					{
						if(formObject.getLVWRowCount("cmplx_FinacleCore_FinaclecoreGrid")==0)
						{

							query="select AcctType,CustRoleType,AcctId,AcctNm,AccountOpenDate,AcctStat,ClearBalanceAmount,AvailableBalance,EffectiveAvailableBalance,CreditGrade,'',child_wi, AcctSegment,AcctSubSegment from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"' and AcctStat !='CLOSED'";
							List<List<String>> list_acc=formObject.getNGDataFromDataCache(query);
							for(List<String> mylist : list_acc)
							{
								PersonalLoanS.mLogger.info( "Data to be added in Grid account Grid: "+mylist.toString());
								formObject.addItemFromList("cmplx_FinacleCore_FinaclecoreGrid", mylist);
							}
						}
					
					if(formObject.getLVWRowCount("cmplx_FinacleCore_liendet_grid")==0){

						query="select AcctId,LienId,LienAmount,LienRemarks,isnull(LienReasonCode,''),isnull(LienStartDate,''),isnull(LienExpDate,'') from ng_rlos_FinancialSummary_LienDetails with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
						//changed ended
						List<List<String>> list_lien=formObject.getNGDataFromDataCache(query);
						for(List<String> mylist : list_lien)
						{
							PersonalLoanS.mLogger.info( "Data to be added in Grid: "+mylist.toString());
							formObject.addItemFromList("cmplx_FinacleCore_liendet_grid", mylist);
						}
					}
					//changed here in this query
					if(formObject.getLVWRowCount("cmplx_FinacleCore_sidet_grid")==0){
						query="select AcctId,SiAmount,SiRemarks,'',isnull(NextExecDate,'') from ng_rlos_FinancialSummary_SiDtls with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
						List<List<String>> list_SIDet=formObject.getNGDataFromDataCache(query);
						//changed ended

						for(List<String> mylist : list_SIDet)
						{
							PersonalLoanS.mLogger.info( "Data to be added in Grid: "+mylist.toString());

							formObject.addItemFromList("cmplx_FinacleCore_sidet_grid", mylist);
						}
					}

					if(formObject.getLVWRowCount("cmplx_FinacleCore_DDSgrid")==0){

						//query changed 31/1/18
						String checkSubquery = "(select count(AcctId) from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where AcctId = AcctId and CifId=CifId and Child_Wi='"+formObject.getWFWorkitemName()+"')>1";
						String elseSubquery = "(select distinct(Account_Type) from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where AcctId = AcctId and CifId=CifId and Child_Wi='"+formObject.getWFWorkitemName()+"')";
						//query changed by Shivang, added order by Return Date
						query="select distinct case when "+checkSubquery+" then 'Individual_CIF' else "+elseSubquery+" end ,AcctId,returntype,returnAmount,returnNumber,isnull(format(convert(datetime,returnDate),'dd/MM/yyyy'),''),isnull(retReasonCode,''),isnull(instrumentdate,''),returnType,'','','','','','' from ng_rlos_FinancialSummary_ReturnsDtls with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'" +" ORDER BY returnDate DESC";
						//added by rishabh same as in cc added order by Return Date
						//query="select  AccountType,Account_Number,chqtype,chqamt,ChequeNumber,isnull(format(convert(datetime,chqretdate),'dd/MM/yyyy'),'')as returnDate,chqretcode,ddsclear,typeofret,'','','','',DDS_wi_name,Consider_For_Obligations,source from NG_RLOS_GR_DDSreturn where DDS_wi_name ='"+formObject.getWFWorkitemName()+"' union all select  case when (select count(AcctId) from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where AcctId = AcctId and CifId=CifId and Child_Wi='"+formObject.getWFWorkitemName()+"')>1 then 'Individual_CIF' else (select distinct(Account_Type) from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where AcctId = AcctId and CifId=CifId and Child_Wi='"+formObject.getWFWorkitemName()+"') end ,AcctId,returntype,returnAmount,returnNumber,isnull(format(convert(datetime,returnDate),'dd/MM/yyyy'),'')as returnDate,isnull(retReasonCode,''),isnull(instrumentdate,''),returnType,'','','','','"+formObject.getWFWorkitemName()+"','true','Internal' from ng_rlos_FinancialSummary_ReturnsDtls with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"' and returnNumber not in (select ChequeNumber from NG_RLOS_GR_DDSreturn where DDS_wi_name='"+formObject.getWFWorkitemName()+"') union all  select '','',ChqType,Amount,Number,isnull(format(convert(datetime,ReturnDate),'dd/MM/yyyy'),'')as returnDate,ReasonCode,'',ProviderNo,'','','','',Child_wi,'true','External' from ng_rlos_cust_extexpo_ChequeDetails where Child_wi ='"+formObject.getWFWorkitemName()+"' and (Number<>'' OR Number is null) and Number not in (select ChequeNumber from NG_RLOS_GR_DDSreturn where DDS_wi_name='"+formObject.getWFWorkitemName()+"') ORDER BY returnDate DESC";

						List<List<String>> list_DDSDet=formObject.getDataFromDataSource(query);
						PersonalLoanS.mLogger.info( "query to be added in list_DDSDet Grid: "+query);
						String return_date="";
						try
						{
							int count=list_DDSDet.size();

							for(int i=0;i<count;i++)
							{
								return_date=list_DDSDet.get(i).get(5);
								String[] date=return_date.split("-");
								return_date=date[2]+"/"+date[1]+"/"+date[0];
								list_DDSDet.get(i).set(5, return_date);
							}


						}
						catch(Exception ex)
						{
							PersonalLoanS.mLogger.info( "Error parsing date ::"+return_date);
						}

						//changed ended
						formObject.clear("cmplx_FinacleCore_DDSgrid");
						for(List<String> mylist : list_DDSDet)
						{
							PersonalLoanS.mLogger.info( "Data to be added in list_DDSDet Grid: "+mylist.toString());

							formObject.addItemFromList("cmplx_FinacleCore_DDSgrid", mylist);
						}

					}
					}
					adjustFrameTops("MOL,World_Check,Reject_Enq,Sal_Enq,Credit_card_Enq1,LOS1,Case_History1");

				}
				catch(Exception e){
					PersonalLoanS.mLogger.info( "Exception while setting data in grid:"+e.getMessage());
					throw new ValidatorException(new FacesMessage("Error while setting data in account grid"));
				}
		}
	}
	
	
	//below code added by Shweta to Capture ATC Mismatch Fields
	public void Capture_ATC_Fields()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try
		{
			String[] Ver_fields;
			String[] Fields_Name;
			String ATC_Fields="";
			Ver_fields=NGFUserResourceMgr_PL.getGlobalVar("CustDetail_Verification").split(":");
			for(int i=0;i<Ver_fields.length;i++)
			{
				Fields_Name=Ver_fields[i].split("#");
				if("Mismatch".equalsIgnoreCase(formObject.getNGValue(Fields_Name[0])))
				{
					ATC_Fields+=ATC_Fields.equals("")?Fields_Name[1]:(";"+Fields_Name[1]);
				}
			}
			formObject.setNGValue("ATC_Mismatch_Fields", ATC_Fields);
		}
		catch(Exception ex)
		{
			PersonalLoanS.mLogger.info("Error In Capture_ATC_Fields" + ex.getStackTrace());
		}
	}
	
	public void Data_reset(String fragment_name) {
		// TODO Auto-generated method stub
		try{
			PersonalLoanS.mLogger.info("Inside try of Data_reset :: "+fragment_name);
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			if("DecisionHistory".equalsIgnoreCase(fragment_name)){
				PersonalLoanS.mLogger.info("Inside decision fragment in data_reset");
				if(!("CAD_Analyst1".equalsIgnoreCase(formObject.getWFActivityName())||"Cad_Analyst2".equalsIgnoreCase(formObject.getWFActivityName()))){
					String Data_reset_query = "select isnull(Highest_delegation_Authority,''),isnull(strength,''),isnull(weakness,''),isnull(LoanApprovalAuth,''),isnull(Dectech_decision,''),isnull(Manual_Deviation,''),isnull(Manual_Deviation_Reason,''),isnull(Score_Grade,''),isnull(ReferTo,'') from ng_rlos_decisionHistory with(nolock) where wi_name = '"+formObject.getWFWorkitemName()+"'";
					List<List<String>> history_data = formObject.getDataFromDataSource(Data_reset_query);
					PersonalLoanS.mLogger.info("history_data query : "+Data_reset_query);
					PersonalLoanS.mLogger.info("history_data result: "+ history_data); 
					if(!history_data.isEmpty()){
						formObject.setNGValue("cmplx_Decision_Highest_delegauth", history_data.get(0).get(0));
						formObject.setNGValue("cmplx_Decision_strength", history_data.get(0).get(1));
						formObject.setNGValue("cmplx_Decision_weakness", history_data.get(0).get(2));
						formObject.setNGValue("cmplx_Decision_LoanApprovalAuthority", history_data.get(0).get(3));
						formObject.setNGValue("cmplx_Decision_Dectech_decsion", history_data.get(0).get(4));
						formObject.setNGValue("cmplx_Decision_Manual_Deviation", history_data.get(0).get(5));
						formObject.setNGValue("cmplx_Decision_Manual_deviation_reason", history_data.get(0).get(6));
						formObject.setNGValue("cmplx_Decision_score_grade", history_data.get(0).get(7));
						formObject.setNGValue("cmplx_Decision_CADDecisiontray", history_data.get(0).get(8));
					}
				}
				
			}
			PersonalLoanS.mLogger.info("End of try - Data_reset");
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("Exception occured in Data_reset : "+ e.getMessage());
		}
		
	}
	
	public void enrollrewardvalid() {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String query = "select count(*) from ng_rlos_IGR_Eligibility_CardLimit limit with (nolock) join ng_master_cardProduct mstr with (nolock) on limit.Card_Product=mstr.CODE where child_wi like '"+formObject.getWFWorkitemName()+"' and Cardproductselect='true' and mstr.DESCRIPTION like '%SKYWARDS%'";
		List<List<String>> records = formObject.getDataFromDataSource(query);
		//CreditCard.mLogger.info("query produced is:"+query);
		try{
			if(records!=null && records.size()>0 && records.get(0)!=null){
				//CreditCard.mLogger.info("records list is:"+records);
				if("0".equals(records.get(0).get(0))){
					formObject.setVisible("AltContactDetails_EnrollRewardsLabel", false);
					formObject.setVisible("AlternateContactDetails_EnrollRewardsIdentifier", false);
				}
				else{
					formObject.setVisible("AltContactDetails_EnrollRewardsLabel", true);
					formObject.setVisible("AlternateContactDetails_EnrollRewardsIdentifier", true);
				}
			}
		}catch(Exception ex){
			PersonalLoanS.mLogger.info( "Exception in AirArabiaValid Function");
			printException(ex);
		}	
	}
	 public void SupplementaryenrollrewardValid() {
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String query = "select count(*) from ng_rlos_IGR_Eligibility_CardLimit limit with (nolock) join ng_master_cardProduct mstr with (nolock) on limit.Card_Product=mstr.CODE where wi_name like '"+formObject.getWFWorkitemName()+"' and Cardproductselect='true' and mstr.DESCRIPTION like '%SKYWARDS%'";
			List<List<String>> records = formObject.getDataFromDataSource(query);
			//CreditCard.mLogger.info("squery produced is@sagarika:"+query);
			try{
				if(records!=null && records.size()>0 && records.get(0)!=null){
					PersonalLoanS.mLogger.info("records list is:"+records);
					if("0".equals(records.get(0).get(0))){
						//CreditCard.mLogger.info("Supsssagarika"+records.get(0).get(0));
						formObject.setVisible("SupplementCardDetails_Label40", false);
						formObject.setVisible("SupplementCardDetails_Text2", false);
						formObject.setLocked("SupplementCardDetails_Text2", true);
					}
					else{
						//CreditCard.mLogger.info("else sagarika"+records.get(0).get(0));
						formObject.setVisible("SupplementCardDetails_Label40", true);
						formObject.setVisible("SupplementCardDetails_Text2", true);
						formObject.setLocked("SupplementCardDetails_Text2", false);
					}
				}
			}catch(Exception ex){
				PersonalLoanS.mLogger.info( "Exception in AirArabiaValid Function");
				printException(ex);
			}
		}
	//code by saurabh for Air Arabia check if checked
	public void AirArabiaValid() {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String query = "select count(*) from ng_rlos_IGR_Eligibility_CardLimit limit with (nolock) join ng_master_cardProduct mstr with (nolock) on limit.Card_Product=mstr.CODE where child_wi like '"+formObject.getWFWorkitemName()+"' and Cardproductselect='true' and mstr.DESCRIPTION like '%Air Arabia Card%'";
		List<List<String>> records = formObject.getDataFromDataSource(query);
		PersonalLoanS.mLogger.info("query produced is:"+query);
		try{
			if(records!=null && records.size()>0 && records.get(0)!=null){
				PersonalLoanS.mLogger.info("records list is:"+records);
				if("0".equals(records.get(0).get(0))){
					formObject.setVisible("AltContactDetails_Label7", false);
					formObject.setVisible("AlternateContactDetails_AirArabiaIdentifier", false);
				}
				else{
					formObject.setVisible("AltContactDetails_Label7", true);
					formObject.setVisible("AlternateContactDetails_AirArabiaIdentifier", true);
				}
			}
		}catch(Exception ex){
			PersonalLoanS.mLogger.info( "Exception in AirArabiaValid Function");
			printException(ex);
		}
	}
	
	public void loadEligibilityData(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		String activityName = formObject.getWFActivityName();

		formObject.setLocked("ELigibiltyAndProductInfo_Frame2",true);
		if("Salaried".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5))){

			//formObject.setLocked("ELigibiltyAndProductInfo_Frame1", true);
			formObject.setEnabled("cmplx_EligibilityAndProductInfo_FinalDBR", false);
			formObject.setEnabled("cmplx_EligibilityAndProductInfo_FinalTai", false);

		}	
		Eligibilityfields();
		
		if(!"CAD_Analyst1".equalsIgnoreCase(formObject.getWFActivityName())){
			formObject.setEnabled("ELigibiltyAndProductInfo_Button1",false);
		}
				
		if("Disbursal".equalsIgnoreCase(activityName)){
			PersonalLoanS.mLogger.info(activityName);
			formObject.setEnabled("ELigibiltyAndProductInfo_Save",false);
		}
		if("Fulfillment_RM".equalsIgnoreCase(activityName))
		{
			formObject.setVisible("ELigibiltyAndProductInfo_Label14", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_InterestType", false);
		}
		//pcasi3365 added ddvtm
		if("CSM".equalsIgnoreCase(formObject.getWFActivityName()) || "SalesCoordinator".equalsIgnoreCase(formObject.getWFActivityName()) ){
			setLoanFieldsVisible();
			formObject.setLocked("cmplx_EligibilityAndProductInfo_InstrumentType", false);
		} 
		
		formObject.setLocked("cmplx_EligibilityAndProductInfo_InterestType", true);
		formObject.setNGValue("cmplx_EligibilityAndProductInfo_InterestType", "Equated");
		formObject.setNGValue("cmplx_EligibilityAndProductInfo_RepayFreq", "Monthly");
		if(formObject.getNGValue("cmplx_EligibilityAndProductInfo_InstrumentType")=="--Select--")
			formObject.setNGValue("cmplx_EligibilityAndProductInfo_InstrumentType", "S");
	}
	
	public void loadDatainLiengrid(){
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String query = "select AcctId,LienAmount,LienRemarks,'AED', isnull(format(convert(datetime,LienExpDate),'dd/MM/yyyy'),'') from ng_rlos_FinancialSummary_LienDetails with (nolock) where child_wi = '"+formObject.getWFWorkitemName()+"'";
			PersonalLoanS.mLogger.info("Value of query is: "+query);
			List<List<String>> records = formObject.getDataFromDataSource(query);
			PersonalLoanS.mLogger.info("Value of records is: "+records);
			for(List<String> record : records){
				formObject.addItemFromList("ELigibiltyAndProductInfo_ListView5", record);
			}
		}catch(Exception ex){
			PersonalLoanS.mLogger.info("Exception in loadDatainLiengrid: ");
			printException(ex);
		}
	}//Liability_New_fetchLiabilities
	public boolean Check_Elite_Customer(FormReference formObject)
	{
		String Nationality=formObject.getNGValue("cmplx_Customer_Nationality");
		String CustomerSubSeg=formObject.getNGValue("cmplx_Customer_CustomerSubSeg");
		String Salary=formObject.getNGValue("cmplx_IncomeDetails_totSal")==null?"0":formObject.getNGValue("cmplx_IncomeDetails_totSal").replaceAll(",", "");
		Float Total_Salary;
		try
		{
			Total_Salary=Float.parseFloat(Salary);
		}
		catch(Exception  Ex)
		{
			Total_Salary=0f;
		}
		PersonalLoanS.mLogger.info("Check_Elite_Customer : Nationality :"+ Nationality);
		PersonalLoanS.mLogger.info("Check_Elite_Customer: CustomerSubSeg : "+ CustomerSubSeg);
		PersonalLoanS.mLogger.info("Check_Elite_Customer : Salary :"+ Salary);
		if("AE".equals(Nationality))
		{
			return true;
		}
		else if("PAM".equalsIgnoreCase(CustomerSubSeg) && Total_Salary>=50000 )
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	public String formatDateFromOnetoAnother(String date,String givenformat,String resultformat) {
		String result = "";
		SimpleDateFormat givenDateformat;
		SimpleDateFormat resultDateformat;
		try {
			givenDateformat = new SimpleDateFormat(givenformat);
			resultDateformat = new SimpleDateFormat(resultformat);
			result = resultDateformat.format(givenDateformat.parse(date));
			PersonalLoanS.mLogger.info("Date converted: old Date: "+date+" \n new date: "+result);
		}
		catch(Exception e) {
			PersonalLoanS.mLogger.info("Exception occured while converting Date: "+e.getMessage());
			PersonalLoanS.mLogger.info("Date: "+date+"\n givenformat: "+givenformat+"\n resultformat: " +resultformat);
			return date;
		}
		finally {
			givenDateformat=null;
			resultDateformat=null;
		}
		return result;
	}

	// added by abhishek on 13 july to disable only buttons and not whole fragment
		/*          Function Header:

		 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha             
		Description                         : disable buttons CC       

		 ***********************************************************************************  */
		public void disableButtonsPL(String fragName){
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String activityName = formObject.getWFActivityName(); 
			if("customer".equalsIgnoreCase(fragName)){

				formObject.setLocked("Customer_ReadFromCard",true);
				formObject.setLocked("cmplx_Customer_CARDNOTAVAIL",true);
				formObject.setLocked("cmplx_Customer_NEP",true);
				formObject.setLocked("Customer_FetchDetails",true);
				formObject.setLocked("Customer_Button1",true);
				formObject.setLocked("cmplx_Customer_IsGenuine",true);
				formObject.setLocked("cmplx_Customer_NTB",true);
				formObject.setLocked("cmplx_Customer_card_id_available",true);
				formObject.setLocked("cmplx_Customer_VIPFlag",true);
				//++ Below Code already exists for : "1-CSM-Customer details-" Age should be non editable for the user" : Reported By Shashank on Oct 05, 2017++
				if(NGFUserResourceMgr_PL.getGlobalVar("PL_CSM").equalsIgnoreCase(activityName)){
					formObject.setLocked("cmplx_Customer_age",true);
					formObject.setLocked("cmplx_Customer_GCCNational",true);
				}
				//"R"
				//++ Above Code already exists for : "1-CSM-Customer details-" Age should be non editable for the user" : Reported By Shashank on Oct 05, 2017++


			}
			else if("Product".equalsIgnoreCase(fragName)){

				formObject.setLocked("Add",true);
				if(NGFUserResourceMgr_PL.getGlobalVar("PL_CSM").equalsIgnoreCase(activityName)){
					//formObject.setLocked("Add",false);  //Commmented by aman
					PersonalLoanS.mLogger.info("Query is inside product");
					//++Below code added by nikhil 7/11/17 as per CC issues sheet
					//formObject.setLocked("ReqProd", true);
					formObject.setEnabled("ReqProd", false);
					//--Above code added by nikhil 7/11/17 as per CC issues sheet
					formObject.setEnabled("subProd", true);
					formObject.setEnabled("AppType", true);
					formObject.setEnabled("EmpType", true);
					formObject.setLocked("subProd", true);
					formObject.setLocked("AppType", true);
					formObject.setLocked("EmpType", true);
					PersonalLoanS.mLogger.info("Query is after product");
				}
				else if(!NGFUserResourceMgr_PL.getGlobalVar("PL_CSM").equalsIgnoreCase(activityName)){
					formObject.setLocked("Modify",true);
				}
			}
			else if("IncomeDetails".equalsIgnoreCase(fragName)){
				//Code added by aman to disable the fields
				formObject.setLocked("cmplx_IncomeDetails_grossSal", true);
				formObject.setLocked("cmplx_IncomeDetails_TotSal", true);
				formObject.setLocked("cmplx_IncomeDetails_netSal1", false);
				formObject.setLocked("cmplx_IncomeDetails_netSal2", false);
				formObject.setLocked("cmplx_IncomeDetails_netSal3", false);
				formObject.setLocked("cmplx_IncomeDetails_AvgNetSal", true);
				formObject.setLocked("cmplx_IncomeDetails_Overtime_Avg", true);
				formObject.setLocked("cmplx_IncomeDetails_Commission_Avg", true);
				formObject.setLocked("cmplx_IncomeDetails_FoodAllow_Avg", true);
				formObject.setLocked("cmplx_IncomeDetails_PhoneAllow_Avg", true);
				formObject.setLocked("cmplx_IncomeDetails_serviceAllow_Avg", true);
				formObject.setLocked("cmplx_IncomeDetails_Bonus_Avg", true);
				formObject.setLocked("cmplx_IncomeDetails_Other_Avg", true);
				formObject.setLocked("cmplx_IncomeDetails_Flying_Avg", true);
				//added by akshay on 16/10/17 for point 6 in PL_NTB sheet-By default Value of accomodation should be disabled 
				if("Yes".equals(formObject.getNGValue("cmplx_IncomeDetails_Accomodation")))
					formObject.setLocked("cmplx_IncomeDetails_AccomodationValue", false);


				formObject.setLocked("IncomeDetails_Button4",true);
				formObject.setLocked("IncomeDetails_FinacialSummarySelf",true);
			}
			else if("AuthorisedSignDetails".equalsIgnoreCase(fragName)){

				formObject.setLocked("CompanyDetails_Button3", true);
				formObject.setLocked("AuthorisedSignDetails_Button4", true);
				if(!NGFUserResourceMgr_PL.getGlobalVar("PL_CSM").equalsIgnoreCase(activityName)){
					formObject.setLocked("AuthorisedSignDetails_add", true);
					formObject.setLocked("AuthorisedSignDetails_modify", true);
					formObject.setLocked("AuthorisedSignDetails_delete", true);
				}
			}
			else if("PartnerDetails".equalsIgnoreCase(fragName)){
				if(!NGFUserResourceMgr_PL.getGlobalVar("PL_CSM").equalsIgnoreCase(activityName)){
					formObject.setLocked("PartnerDetails_add", true);
					formObject.setLocked("PartnerDetails_modify", true);
					formObject.setLocked("PartnerDetails_delete", true);
				}
				//12th september
				//++ Below Code already exists for : "14-CSM-Partner Details-" first make partner details fragment enable" : Reported By Shashank on Oct 05, 2017++
				else{
					formObject.setLocked("PartnerDetails_Frame1",false);
				}
				//++ Above Code already exists for : "14-CSM-Partner Details-" first make partner details fragment enable" : Reported By Shashank on Oct 05, 2017++

				//12th september
			}
			else if("CompanyDetails".equalsIgnoreCase(fragName)){
				if(!NGFUserResourceMgr_PL.getGlobalVar("PL_CSM").equalsIgnoreCase(activityName)){
					formObject.setLocked("CompanyDetails_Add", true);
					formObject.setLocked("CompanyDetails_Modify", true);
					formObject.setLocked("CompanyDetails_delete", true);
				}

				formObject.setLocked("CompanyDetails_Button3", true);
			}
			else if("Liability_New".equalsIgnoreCase(fragName)){

				formObject.setLocked("cmplx_Liability_New_AECBconsentAvail",true);
				if(!("CAD_Analyst2").equalsIgnoreCase(activityName) || !("DDVT_maker").equalsIgnoreCase(activityName)){
					
				formObject.setLocked("ExtLiability_AECBReport",true);
				}
				formObject.setLocked("cmplx_Liability_New_overrideAECB",true);
				formObject.setLocked("cmplx_Liability_New_overrideIntLiab",true);
				formObject.setLocked("ExtLiability_Button1",true);
				formObject.setLocked("Liability_New_Overwrite",true);
				formObject.setLocked("ExtLiability_Add",true);
				formObject.setLocked("ExtLiability_Modify",true);
				formObject.setLocked("ExtLiability_Delete",true);
				//12th sept
				formObject.setLocked("cmplx_Liability_New_AECBconsentAvail",false);
				formObject.setLocked("cmplx_Liability_New_overrideAECB",false);
				formObject.setLocked("ExtLiability_Add",false);
				formObject.setLocked("ExtLiability_Modify",false);
				formObject.setLocked("ExtLiability_Delete",false);
				//12th sept
				//P2-47 Override AECB and override internal liabilities tickbox not to be enabled for CSM (Cosmetics)
				if(NGFUserResourceMgr_PL.getGlobalVar("PL_CSM").equalsIgnoreCase(activityName)){
					PersonalLoanS.mLogger.info("inside liability Details of CSM");
					formObject.setEnabled("cmplx_Liability_New_overrideAECB",false);
					PersonalLoanS.mLogger.info("after liability Details of CSM");

				}
				//P2-47 Override AECB and override internal liabilities tickbox not to be enabled for CSM (Cosmetics)
			}
			else if("EMploymentDetails".equalsIgnoreCase(fragName)){

				formObject.setLocked("EMploymentDetails_Button2",true);
				if(NGFUserResourceMgr_PL.getGlobalVar("PL_CSM").equalsIgnoreCase(activityName))
				{
					//++Below code added by nikhil 7/11/17 as per CC issues sheet
					formObject.setLocked("cmplx_EmploymentDetails_CurrEmployer",true);
					formObject.setLocked("EMploymentDetails_Button2",false);
					//--Above code added by nikhil 7/11/17 as per CC issues sheet
				}
			}
			else if("ELigibiltyAndProductInfo".equalsIgnoreCase(fragName)){
				if(!"CAD_Analyst1".equalsIgnoreCase(activityName)){
					formObject.setLocked("ELigibiltyAndProductInfo_Button1",true);
				}
			}
			else if("AddressDetails".equalsIgnoreCase(fragName)){
				//Deepak changes done for Salescordinator 09 Sept 2019
				if(!(NGFUserResourceMgr_PL.getGlobalVar("PL_CSM").equalsIgnoreCase(activityName)|| "SalesCoordinator".equalsIgnoreCase(activityName))){
					formObject.setLocked("AddressDetails_addr_Add",true);
					formObject.setLocked("AddressDetails_addr_Modify",true);
					formObject.setLocked("AddressDetails_addr_Delete",true);
				}
			}
			else if("AltContactDetails".equalsIgnoreCase(fragName)){	
				//added if-else condition for PCASI - 3544
				if("DDVT_maker".equalsIgnoreCase(activityName)){
					formObject.setLocked("AlternateContactDetails_RetainaccifLoanreq",false);
				}
				else
				formObject.setLocked("AlternateContactDetails_RetainaccifLoanreq",true);
			}
			else if("FATCA".equalsIgnoreCase(fragName)){
				if(!NGFUserResourceMgr_PL.getGlobalVar("PL_CSM").equalsIgnoreCase(activityName)){
					formObject.setLocked("FATCA_Button1",true);
					formObject.setLocked("FATCA_Button2",true);
				}
			}
			else if("OECD".equalsIgnoreCase(fragName)){
				if(!NGFUserResourceMgr_PL.getGlobalVar("PL_CSM").equalsIgnoreCase(activityName)){
					formObject.setLocked("OECD_add",true);
					formObject.setLocked("OECD_modify",true);
					formObject.setLocked("OECD_delete",true);
				}
			}
			else if("IncomingDocument".equalsIgnoreCase(fragName)){

				formObject.setLocked("IncomingDocument_Frame",false);
			}
			else if("ReferenceDetails".equalsIgnoreCase(fragName)){
				PersonalLoanS.mLogger.info("before CSM");
				if(!NGFUserResourceMgr_PL.getGlobalVar("PL_CSM").equalsIgnoreCase(activityName)){
					PersonalLoanS.mLogger.info("inside CSM");
					formObject.setLocked("ReferenceDetails_Reference_Add",true);
					formObject.setLocked("ReferenceDetails_Reference__modify",true);
					formObject.setLocked("ReferenceDetails_Reference_delete",true);
				}
				PersonalLoanS.mLogger.info("after CSM");
				//formObject.setLocked("Reference_Details_Save",true);
			}
			else if("SupplementCardDetails".equalsIgnoreCase(fragName)){

				formObject.setLocked("SupplementCardDetails_Button1",true);
				formObject.setLocked("SupplementCardDetails_Button2",true);
				formObject.setLocked("SupplementCardDetails_Button3",true);
				formObject.setLocked("SupplementCardDetails_Button4",true);
			}
			else if("CardDetails".equalsIgnoreCase(fragName)){
				if(!NGFUserResourceMgr_PL.getGlobalVar("PL_CSM").equalsIgnoreCase(activityName)){
					formObject.setLocked("CardDetails_Button2",true);
					formObject.setLocked("CardDetails_Button3",true);
					formObject.setLocked("CardDetails_Button4",true);
				}
			}
			else if("DecisionHistory".equalsIgnoreCase(fragName)){

				formObject.setLocked("DecisionHistory_Button3",true);
				formObject.setLocked("DecisionHistory_Button4",true);
				formObject.setLocked("cmplx_Decision_waiveoffver",true);
			}
		}

		//Deepak below method added to check selected card at DDVT maker
		public void Check_selectedCard()
		{
			PersonalLoanS.mLogger.info("Inside Check_EFC_Limit");
			String alerttxt = "";
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			List<List<String>> CardTypeandLimitXML=null;
			try
			{
				String query="select count(Card_Product) as card_count from ng_rlos_IGR_Eligibility_CardLimit with(nolock) where Cardproductselect='true' and Child_Wi ='"+formObject.getWFWorkitemName()+"'";
				PersonalLoanS.mLogger.info("Inside Check_selectedCard : "+ query);
				CardTypeandLimitXML=formObject.getDataFromDataSource(query);
				PersonalLoanS.mLogger.info("output CardTypeandLimitXML : "+ CardTypeandLimitXML);
				if (CardTypeandLimitXML != null){
					if("0".equalsIgnoreCase(CardTypeandLimitXML.get(0).get(0)))
					{
						alerttxt = NGFUserResourceMgr_PL.getAlert("VAL110");
					}
				}
			}
			catch(Exception ex)
			{
				PersonalLoanS.mLogger.info("Inside Check_EFC_Limit exception"+ex.getStackTrace());
			}
			if(!alerttxt.equalsIgnoreCase("")){
				throw new ValidatorException(new FacesMessage(alerttxt));
			}
			
			
		}
	
		//added by nikhil for Saving Card Desc
		public void Save_Card_desc()
		{
			
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String Desc="";
			String query="select  distinct description  from ng_master_carddescription with (nolock)where code in (select Card_Product from ng_rlos_IGR_Eligibility_CardLimit with (nolock) where Child_Wi like '"+formObject.getWFWorkitemName()+"' and Cardproductselect='true')";
			try
			{
				PersonalLoanS.mLogger.info("query name :"+query);
			List<List<String>> result=formObject.getDataFromDataSource(query);
			if(!result.isEmpty()){
				for(List<String> result_1 : result)
				{
					if("".equalsIgnoreCase(Desc))
					{
						Desc+=result_1.get(0);
					}
					else
					{
						Desc=Desc+", "+result_1.get(0);
					}
				}
			}
			formObject.setNGValue("Card_desc", Desc);
			}
			catch(Exception ex)
			{
				PersonalLoanS.mLogger.info("Exception occured in Save_Card_desc" +ex.getMessage());
			}
		
		}
		
		//Deepak New method added to update Final Cif in multiple Applicant.
		public void UpdatePrimaryCif(){
			try{
				FormReference formObject = FormContext.getCurrentInstance().getFormReference();
				String FinalPrimaryCif = formObject.getNGValue("cmplx_Customer_CIFNO");
				String FinalPrimarypass = formObject.getNGValue("cmplx_Customer_PAssportNo");
				String supdateCif_Query="update ng_rlos_gr_MultipleApplicants set applicantCIF='"+FinalPrimaryCif+"',ApplicantPassport='"+FinalPrimarypass+"' where multipleApplicants_winame='"+formObject.getWFWorkitemName()+"' and applicantType='Primary'";
				PersonalLoanS.mLogger.info(" inside UpdatePrimaryCif final update Query: "+ supdateCif_Query);
				formObject.saveDataIntoDataSource(supdateCif_Query);
			}
			catch(Exception e){
				PersonalLoanS.mLogger.info("Exception occured in UpdatePrimaryCif" +e.getMessage());
			}
		}
		
		//++below code added by nikhil for Self-Supp CR PCAS-2637
		//change in self-supp 20-08-2019
		public void Load_Self_Supp_Data()
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			if("Yes".equalsIgnoreCase(formObject.getNGValue("cmplx_CardDetails_SelfSupp_required")))
			{
				formObject.setEnabled("cmplx_CardDetails_Selected_Card_Product", true);
				formObject.setEnabled("cmplx_CardDetails_SelfSupp_Limit", true);
				formObject.setEnabled("cmplx_CardDetails_Self_card_embossing", true);
				LoadPickList("cmplx_CardDetails_Selected_Card_Product", "SELECT CASE WHEN temptble.Description!='--Select--' then concat(temptble.Description,'(',temptble.final_limit,')') else temptble.Description end,temptble.Description FROM (SELECT '--Select--' as Description,'' as final_limit union SELECT distinct card_product as Description,final_limit FROM ng_rlos_IGR_Eligibility_CardLimit WITH (nolock) WHERE Child_wi = '"+formObject.getWFWorkitemName()+"' AND cardproductselect = 'true') as temptble order by case when Description='--Select--' then 0 else 1 end");
				
			}
			else
			{
				formObject.setNGValue("cmplx_CardDetails_Selected_Card_Product", "--Select--");
				formObject.setNGValue("cmplx_CardDetails_SelfSupp_Limit", "");
				formObject.setNGValue("cmplx_CardDetails_Self_card_embossing", "");
				formObject.setEnabled("cmplx_CardDetails_Selected_Card_Product", false);
				formObject.setEnabled("cmplx_CardDetails_SelfSupp_Limit", false);
				formObject.setEnabled("cmplx_CardDetails_Self_card_embossing", false);
			}
			
		}
		@SuppressWarnings("finally")
		public List<List<String>> get_Avl_Cards()
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			List<List<String>> Avl_Cards=null;
			try
			{
			String Avl_card_Query="select Card_Product from ng_rlos_IGR_Eligibility_CardLimit where Cardproductselect='true' and Child_Wi='"+formObject.getWFWorkitemName()+"'";
			Avl_Cards = formObject.getDataFromDataSource(Avl_card_Query);
			}
			catch (Exception ex)
			{
				PersonalLoanS.mLogger.info("Exception in get_Avl_Cards data"+ex.getMessage());
			}
			finally
			{
				return Avl_Cards;
			}
			
		}
		public void Refresh_self_supp_data()
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			//formObject.clear("CardDetails_Sel_Card_Product");
			formObject.setNGValue("cmplx_CardDetails_Selected_Card_Product", "--Select--");
			formObject.setNGValue("cmplx_CardDetails_SelfSupp_Limit", "");
			formObject.setNGValue("cmplx_CardDetails_Self_card_embossing", "");
			formObject.setEnabled("cmplx_CardDetails_Selected_Card_Product", false);
			formObject.setEnabled("cmplx_CardDetails_SelfSupp_Limit", false);
			formObject.setEnabled("cmplx_CardDetails_Self_card_embossing", false);
		}
		public void Load_SelfSupp_CRNGrid()
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			if(!"--Select--".equalsIgnoreCase(formObject.getNGValue("cmplx_CardDetails_Selected_Card_Product")) && "Yes".equalsIgnoreCase(formObject.getNGValue("cmplx_CardDetails_SelfSupp_required"))){
				String Card = "";
				if(formObject.getNGValue("cmplx_CardDetails_Selected_Card_Product").contains("("))
				{
					Card =formObject.getNGValue("cmplx_CardDetails_Selected_Card_Product").substring(0,formObject.getNGValue("cmplx_CardDetails_Selected_Card_Product").indexOf("(")+1);
				}
				else
				{
					Card =formObject.getNGValue("cmplx_CardDetails_Selected_Card_Product");
				}
			int Row_count=formObject.getLVWRowCount("cmplx_CardDetails_cmplx_CardCRNDetails");
			int Self_added=0,primary_added=0;
			for(int i=0;i<Row_count;i++)
			{
			//String sel_Card=formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails", i, 0);
			String Applicant=formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails", i, 9);
			String Passport=formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails", i, 10);
			if( "Primary".equalsIgnoreCase(Applicant) && Passport.equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_PAssportNo")))
			{
				primary_added++;

			}
			if( "Supplement".equalsIgnoreCase(Applicant) && Passport.equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_PAssportNo")))
			{
			Self_added++;
			break;
			}
			
			}
			if(Self_added==0 && primary_added>0)
			{
			Add_selfcard_to_CRNGrid(Card,formObject);	
			}
			}


		}
		public void updateselfLimit()
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_CardDetails_Selected_Card_Product")) && "Yes".equalsIgnoreCase(formObject.getNGValue("cmplx_CardDetails_SelfSupp_required"))){
			String Card=formObject.getNGValue("cmplx_CardDetails_Selected_Card_Product");
			int Row_count=formObject.getLVWRowCount("cmplx_CardDetails_cmplx_CardCRNDetails");
			for(int i=0;i<Row_count;i++)
			{
			String sel_Card=formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails", i, 0);
			String Applicant=formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails", i, 9);
			String Passport=formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails", i, 10);
			if( sel_Card.equalsIgnoreCase(Card) && "Supplement".equalsIgnoreCase(Applicant) && Passport.equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_PAssportNo")))
			{
			formObject.setNGValue("cmplx_CardDetails_cmplx_CardCRNDetails", i, 7, getFinalLimit(Card, formObject));
			break;
			}
			}
			
			}


		}
		
		public String getFinalLimit(String Card_name,FormReference formObject)
		{
			/*String Final_limit="";
			List<List<String>> FinalLimit=null;
			try
			{
			String Query="select top 1 Final_Limit from ng_rlos_IGR_Eligibility_CardLimit where Cardproductselect='true' and Child_Wi='"+formObject.getWFWorkitemName()+"' and Card_Product='"+Card_name+"'";
			FinalLimit=formObject.getDataFromDataSource(Query);
			if(FinalLimit!=null && FinalLimit.size()>0)
			{
				Final_limit=FinalLimit.get(0).get(0);
			}
			}
			catch(Exception ex)
			{
				CreditCard.mLogger.info("exception in get final Limit "+ex.getMessage() );
			}*/
			
			return formObject.getNGValue("cmplx_CardDetails_SelfSupp_Limit");
			
		}
		public void Remove_Self_Card()
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			int Row_count=formObject.getLVWRowCount("cmplx_CardDetails_cmplx_CardCRNDetails");
			for(int i=0;i<Row_count;i++)
			{
			//String sel_Card=formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails", i, 0);
			String Applicant=formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails", i, 9);
			String Passport=formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails", i, 10);
			if( "Supplement".equalsIgnoreCase(Applicant) && Passport.equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_PAssportNo")))
			{
			formObject.setNGListIndex("cmplx_CardDetails_cmplx_CardCRNDetails", i);
			formObject.ExecuteExternalCommand("NGDeleteRow", "cmplx_CardDetails_cmplx_CardCRNDetails");
			break;
			}
			}
		}
		public void Add_selfcard_to_CRNGrid(String Card,FormReference formObject)
		{
			//added by nikhil to solve blank grid issue
			if(!"".equalsIgnoreCase(Card) && !"--Select--".equalsIgnoreCase(Card))
			{
			List<String> newRecord = new ArrayList<String>();
			newRecord.add(Card);
			newRecord.add("");
			newRecord.add("");
			newRecord.add(Card_profiles("ng_master_TransactionFeeProfile", Card));
			newRecord.add(Card_profiles("ng_master_InterestProfile", Card));
			newRecord.add(Card_profiles("NG_MASTER_feeprofile", Card));
			newRecord.add(formObject.getWFWorkitemName());
			newRecord.add(getFinalLimit(Card,formObject));//final limit
			newRecord.add("");
			newRecord.add("Supplement");
			newRecord.add(formObject.getNGValue("cmplx_Customer_PAssportNo"));
			newRecord.add("");
			formObject.addItemFromList("cmplx_CardDetails_cmplx_CardCRNDetails", newRecord);
			newRecord.clear();
			}
		}
		public void Update_Status_Self_Cards(FormReference formObject)
		{
			int row_count=formObject.getLVWRowCount("cmplx_CCCreation_cmplx_CCCreationGrid");
			for(int i=0;i<row_count;i++)
			{
				if("SUPPLEMENT".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",i,12)) && formObject.getNGValue("cmplx_Customer_PAssportNo").equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",i,13)))
				{
					formObject.setNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",i,10,"Y");
				}
			}
		}
		//--above code added by nikhil for Self-Supp CR
		
		public boolean isCustomerMinor(FormReference formObject) {
			try
			{
				Double Age=Double.parseDouble(formObject.getNGValue("cmplx_Customer_age"));
				if(Age >= 21) {
					return false;
				}
				if(Age >= 18.06 && Age<=20.06 && formObject.getNGValue("cmplx_Customer_Nationality").equalsIgnoreCase("AE")) 
                    return true;
				else if(Age < 21 && !formObject.getNGValue("cmplx_Customer_Nationality").equalsIgnoreCase("AE"))
					return true;
				else
					return false;
			}
			catch(Exception ex)
			{
				PersonalLoanS.mLogger.info("Exception in isCustomerMinor"+ex.getMessage());
				return false;
			}
		}
		/*public void enable_cad1(){
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			formObject.setVisible("Office_verification", false);
			
		}*/
		// start hritik imd cal to call at frag expand
/*		public void IMD(){
			 float lpfVatAmt=0;
			 float insuranceVatAmt=0;
			 FormReference formObject = FormContext.getCurrentInstance().getFormReference();

			 try
			 {
				 //added By Tarang for drop 4 point 8 started on 08/03/2018
				 lpfVatAmt=(Float.parseFloat(formObject.getNGValue("cmplx_LoanDetails_LoanProcessingVATPercent"))*Float.parseFloat(formObject.getNGValue("cmplx_LoanDetails_lpfamt")))/100f;
				 insuranceVatAmt=(Float.parseFloat(formObject.getNGValue("cmplx_LoanDetails_InsuranceVATPercent"))*Float.parseFloat(formObject.getNGValue("cmplx_LoanDetails_insuramt")))/100f;
				 lpfVatAmt = ((float)(Math.round((lpfVatAmt*100)))/100);
				 insuranceVatAmt = ((float)(Math.round((insuranceVatAmt*100)))/100);
			 }
			 catch(Exception ex)
			 {

			 }
			 finally
			 {
				 formObject.setNGValue("cmplx_LoanDetails_LoanProcessingVat", lpfVatAmt);
				 formObject.setNGValue("cmplx_LoanDetails_InsuranceVat", insuranceVatAmt);
				 formObject.setLocked("cmplx_LoanDetails_LoanProcessingVat", true);
				 formObject.setLocked("cmplx_LoanDetails_InsuranceVat", true);
				 //added By Tarang for drop 4 point 8 started on 08/03/2018
				 String lpf = formObject.getNGValue("cmplx_EligibilityAndProductInfo_LPFAmount");
				 String insurance = formObject.getNGValue("cmplx_EligibilityAndProductInfo_InsuranceAmount");
				 //change by saurabh for UG points 17/6
				 float Amount = Float.parseFloat(lpf) + Float.parseFloat(insurance)+lpfVatAmt+insuranceVatAmt;		
				 formObject.setNGValue("cmplx_LoanDetails_amt",Amount);
			 }	 
		} */
		// end
		public void enable_CPV(){
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			//Done By aman for Sprint2
			if (formObject.getNGValue("RefFrmCAD").equals("Y") && (formObject.getWFActivityName().equalsIgnoreCase("CAd_Analyst1")|| formObject.getWFActivityName().equalsIgnoreCase("CAd_Analyst1"))){
				formObject.setSheetVisible(tabName,8,true);
			}
			//Done By aman for Sprint2
			if (formObject.getNGValue("RefFrmCPV").equals("Y") && (formObject.getWFActivityName().equalsIgnoreCase("CPV")|| formObject.getWFActivityName().equalsIgnoreCase("CPV_Analyst"))){
				formObject.setSheetVisible(tabName,8,true);
			}
			//as per cPV FSD by SHweta
			String appType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,4);
			if(appType.contains("RESC") || appType.contains("TKO") || appType.contains("NEW") ){
				//formObject.setVisible("Home_country_verification", false);
				formObject.setVisible("Residence_verification", false);
				formObject.setVisible("Reference_detail_verification", false);
				formObject.setVisible("Business_Verification", false);
				formObject.setVisible("Guarantor_verification", false);
				formObject.setVisible("Loan_card_details", false);
				adjustFrameTops("Cust_Detail_verification,Home_country_verification,Office_verification,Smart_check");

				
			} else if(appType.contains("TOP")) {
				formObject.setVisible("Residence_verification", false);
				formObject.setVisible("Reference_detail_verification", false);
				formObject.setVisible("Business_Verification", false);
				formObject.setVisible("Guarantor_verification", false);
				formObject.setVisible("Loan_card_details", false);
				formObject.setVisible("Home_country_verification", false);
				adjustFrameTops("Cust_Detail_verification,Office_verification,Smart_check");

				
			}
			else if(NGFUserResourceMgr_PL.getGlobalVar("PL_Salaried").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5)) || NGFUserResourceMgr_PL.getGlobalVar("PL_SalariedPensioner").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5)))
			{
				formObject.setVisible("Home_country_verification", false);
				formObject.setVisible("Residence_verification", false);
				formObject.setVisible("Reference_detail_verification", false);
				formObject.setVisible("Business_Verification", false);
				formObject.setVisible("Guarantor_verification", false);
				adjustFrameTops("Cust_Detail_verification,Loan_card_details,Office_verification,Smart_check");


				//formObject.setVisible("Smart_Check", false);
			}

			else{
				formObject.setVisible("Home_country_verification", false);
				formObject.setVisible("Residence_verification", false);
				formObject.setVisible("Reference_detail_verification", false);
				formObject.setVisible("Guarantor_verification", false);
				adjustFrameTops("Cust_Detail_verification,Loan_card_details,Office_verification,Smart_check");

				

				//formObject.setVisible("Smart_Check", false);
			}

			if("Cad_Analyst2".equalsIgnoreCase(formObject.getWFActivityName()) || "CAD_Analyst1".equalsIgnoreCase(formObject.getWFActivityName()) || "Smart_CPV".equalsIgnoreCase(formObject.getWFActivityName()))
			{
				formObject.setVisible("Smart_check", true);
				formObject.setVisible("Home_country_verification", false); // hritik 22.8.21 -3779
			}
			formObject.setVisible("ReferHistory",false);//added b y akshay on 14/3/18 for proc 6226
		}
		
		public void  disableforNA_Off()
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_OffVerification_fxdsal_ver","cmplx_OffVerification_accpvded_ver","cmplx_OffVerification_desig_ver","cmplx_OffVerification_doj_ver","cmplx_OffVerification_cnfminjob_ver");
			List<String> LoadPicklist_update= Arrays.asList("cmplx_OffVerification_fxdsal_upd","cmplx_OffVerification_accpvded_upd","cmplx_OffVerification_desig_upd","cmplx_OffVerification_doj_ver","cmplx_OffVerification_cnfrminjob_upd");
			int i=0;
			for(String ver : LoadPicklist_Verification)
			{
				if(!"Mismatch".equalsIgnoreCase(formObject.getNGValue(ver)))
				{
					formObject.setLocked(LoadPicklist_update.get(i), true);
				}
				i++;
			}
			
		} //by jahnavi for JIRA 3010
		public void  disableforNA()
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_CustDetailVerification_mobno1_ver","cmplx_CustDetailVerification_mobno2_ver","cmplx_CustDetailVerification_dob_ver","cmplx_CustDetailVerification_POBoxno_ver","cmplx_CustDetailVerification_emirates_ver","cmplx_CustDetailVerification_persorcompPOBox_ver","cmplx_CustDetailVerification_hcountrytelno_ver","cmplx_CustDetailVerification_hcontryaddr_ver","cmplx_CustDetailVerification_email1_ver","cmplx_CustDetailVerification_email2_ver","cmplx_CustDetailVerification_Mother_name_ver");
			List<String> LoadPicklist_update= Arrays.asList("cmplx_CustDetailVerification_mobno1_upd","cmplx_CustDetailVerification_mobno2_upd","cmplx_CustDetailVerification_dob_upd","cmplx_CustDetailVerification_POBoxno_upd","cmplx_CustDetailVerification_emirates_upd","cmplx_CustDetailVerification_persorcompPOBox_upd","cmplx_CustDetailVerification_hcountrytelno_upd","cmplx_CustDetailVerification_hcountryaddr_upd","cmplx_CustDetailVerification_email1_upd","cmplx_CustDetailVerification_email2_upd","cmplx_CustDetailVerification_mother_name_upd");
			int i=0;
			for(String ver : LoadPicklist_Verification)
			{
				if(!"Mismatch".equalsIgnoreCase(formObject.getNGValue(ver)))
				{
					formObject.setLocked(LoadPicklist_update.get(i), true);
				}
				i++;
			}
			
		}
		public void mismatchFieldProperty(FormReference formObject , List<String> LoadPicklist_Verification,List<String> LoadPicklist_update){
			int i=0;
			for(String ver : LoadPicklist_Verification)
			{
				if( !"Mismatch".equalsIgnoreCase(formObject.getNGValue(ver)))
				{
					formObject.setLocked(LoadPicklist_update.get(i), true);
				}
				if(ver.equalsIgnoreCase("cmplx_emp_ver_sp2_sal_pay_drop") && "Other".equalsIgnoreCase(formObject.getNGValue(ver))){
					formObject.setLocked(LoadPicklist_update.get(i), false);
				} 
				i++;
			}
			
			
		}
		
		public void loadPicklist_custverification(){
			LoadPickList("cmplx_CustDetailVerification_mobno1_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock) where field_name='Verification'");
			LoadPickList("cmplx_CustDetailVerification_mobno2_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Verification'");
			LoadPickList("cmplx_CustDetailVerification_dob_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock) where field_name='Verification' ");
			LoadPickList("cmplx_CustDetailVerification_POBoxno_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Verification' ");
			LoadPickList("cmplx_CustDetailVerification_emirates_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Verification' ");
			LoadPickList("cmplx_CustDetailVerification_persorcompPOBox_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Verification' ");
			LoadPickList("cmplx_CustDetailVerification_resno_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Verification' ");
			LoadPickList("cmplx_CustDetailVerification_offtelno_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Verification' ");
			LoadPickList("cmplx_CustDetailVerification_hcountrytelno_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Verification' ");
			LoadPickList("cmplx_CustDetailVerification_hcontryaddr_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Verification' ");
			LoadPickList("cmplx_CustDetailVerification_email1_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Verification' ");
			LoadPickList("cmplx_CustDetailVerification_email2_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Verification' ");
			LoadPickList("cmplx_CustDetailVerification_Mother_name_ver", "select '--Select--' union select convert(varchar, decision) from NG_MASTER_CPV_DEC with (nolock)  where field_name='Verification' ");

		}
		
		public void saveSmartCheckGrid(){

			PersonalLoanS.mLogger.info( "Inside saveSmartCheckGrid: "); 
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String query="insert into NG_RLOS_GR_SmartCheck(smart_wi_name, creditremarks, decision) values('"+formObject.getWFWorkitemName()+"','"+formObject.getNGValue("SmartCheck_CR_Remarks")+"','"+formObject.getNGValue("cmplx_SmartCheck_Decision")+"')";
			PersonalLoanS.mLogger.info("Query is"+query);
			formObject.saveDataIntoDataSource(query);            
		}
		
		//added by nikhil to handle CPV refer in case of CA refer to DDVT Scenario
		//code sync with CC by shweta
		@SuppressWarnings("finally")
		public boolean	Check_CPV_Refer_DDVT()
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			int Count=0;
			String query="select COUNT(WorkItemId) from WFINSTRUMENTTABLE with (nolock) where ActivityName='Refer_Hold' and VAR_STR1='CPV' and ProcessInstanceID ='"+formObject.getWFWorkitemName()+"'";
			try
			{
				PersonalLoanS.mLogger.info("query name :"+query);
				List<List<String>> result=formObject.getDataFromDataSource(query);
				if(!result.isEmpty()){
					Count=Integer.parseInt(result.get(0).get(0));
				}
			} catch(Exception ex) {
				PersonalLoanS.mLogger.info("Exception occured in Check_CPV_Refer_DDVT" +ex.getMessage());
			}
			finally
			{	
				if(Count>0) {
					return true;
				}else {
					return false;
				}
			}
		}
		
		//added by nikhil for All Limit Check
		public void Check_All_Limits()
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String wi_name=formObject.getWFWorkitemName();
			String AlertMsg="";
			String Query="select count(Supp_Limit) from (select Selected_Card_Product as Card_product,SelfSupp_Limit as  Supp_Limit,(select Final_Limit from ng_rlos_IGR_Eligibility_CardLimit where Card_Product=(select Selected_Card_Product from NG_RLOS_CardDetails where winame='"+wi_name+"') and Child_Wi='"+wi_name+"') as Card_limit from NG_RLOS_CardDetails where winame='"+wi_name+"'  union all select a.CardProduct,a.ApprovedLimit as Supp_Limit,b.Final_Limit as card_limit from ng_RLOS_GR_SupplementCardDetails a inner join ng_rlos_IGR_Eligibility_CardLimit b on a.supplement_WIname=b.Child_Wi where a.supplement_WIname='"+wi_name+"' and b.Cardproductselect = 'true') as ezt where cast(Supp_Limit as float) >cast(card_limit as float)";
			try
			{
				List<List<String>> result=formObject.getDataFromDataSource(Query);
				if(!result.isEmpty()){
					if(Integer.parseInt(result.get(0).get(0)) >0)
					{
						AlertMsg="Supplementary Limit greater than Primary Limit. Please Select Refer to DDVT!";
					}
				}
			}
			catch(Exception ex)
			{
				PersonalLoanS.mLogger.info("Error In Check_All_Limits" + ex.getStackTrace());
			}
			if(!AlertMsg.equalsIgnoreCase(""))
			{
				formObject.setLocked("cmplx_Decision_Decision", false);
				throw new ValidatorException(new FacesMessage(AlertMsg));
			}
			
		}
		public void autopopulateValuesFCU(FormReference formObject) {
			String mobNo1 = formObject.getNGValue("AlternateContactDetails_MobileNo1");
			String mobNo2 = formObject.getNGValue("AlternateContactDetails_MobileNo2");
			String dob = formObject.getNGValue("cmplx_Customer_DOb");
			int adressrowcount = formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
			String poboxResidence = "";
	        String poboxOffice = "";
			String emirate = "";

			for(int i=0;i<adressrowcount;i++){
				String addType = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,0);
				
				if("OFFICE".equalsIgnoreCase(addType))
				{
					poboxOffice = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,8);
					emirate = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,6);
				}
				
			}
			try
			{
				//below query chenged by nikhil for fcu issued on 4/12/17
				if(!"NA".equals(emirate))
				{
					List<List<String>> db=formObject.getNGDataFromDataCache("select Description from NG_MASTER_state with (nolock) where Code='"+emirate+"'") ;
					if(db!=null && db.get(0)!=null && db.get(0).get(0)!=null){
						emirate= db.get(0).get(0); 
						PersonalLoanS.mLogger.info("fcu @ emirate"+emirate);
					}
				}
			}
			catch(Exception ex)
			{
				emirate="";
				PersonalLoanS.mLogger.info("inside fcu catch");
			}
			/*String homeNo = formObject.getNGValue("AlternateContactDetails_HOMECOUNTRYNO");
			String email1 = formObject.getNGValue("AlternateContactDetails_EMAIL1_PRI");
			String email2 = formObject.getNGValue("AlternateContactDetails_EMAIL2_SEC");*/
			String officeNo = formObject.getNGValue("AlternateContactDetails_OfficeNo");
			
			String emirateid=formObject.getNGValue("cmplx_Customer_EmiratesID");
			String cifid=formObject.getNGValue("cmplx_Customer_CIFNO");
			formObject.setNGValue("cmplx_CustDetailverification1_EmiratesId", emirateid);
			//changed by nikhil 25/11
			formObject.setNGValue("CustDetailVerification1_Text8", cifid);
			setValuesFCU(formObject,mobNo1,mobNo2,dob,poboxResidence,emirate,officeNo );

		}
		
		public void setValuesFCU(FormReference formObject,String...values) {
			String[] controls = new String[]{"cmplx_CustDetailverification1_MobNo1_value","cmplx_CustDetailverification1_MobNo2_value","cmplx_CustDetailverification1_DOB_value",
					"cmplx_CustDetailverification1_PO_Box_Value","cmplx_CustDetailverification1_Emirates_value","cmplx_CustDetailverification1_Off_Telephone_value"
			};
			int i=0;
			for(String str : values){
				if(checkValue(str)){
					formObject.setNGValue(controls[i], str);
					//formObject.setLocked(controls[i], true); 
					PersonalLoanS.mLogger.info(controls[i]+"::"+ str);

				}
				else{
					PersonalLoanS.mLogger.info( "value received at index "+i+" is: "+str);
				}
				formObject.setLocked(controls[i], true);
				i++;
			}
		}
		//by shweta to set  classification code
		public void AECBHistMonthCnt()
		{
			int aecb_months=0;
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			if("".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_ClassificationCode"))||"--Select--".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_ClassificationCode"))){
				String ClassificationCode = "";
				String Wi_Name=formObject.getWFWorkitemName();
				try
				{
					String Query="select isnull(max(AECBHistMonthCnt),0) as AECBHistMonthCnt from ( select MAX(cast(isnull(AECBHistMonthCnt,'0') as int)) as AECBHistMonthCnt  from ng_rlos_cust_extexpo_CardDetails with (nolock) where  Child_wi  = '"	+ Wi_Name+ "' union select Max(cast(isnull(AECBHistMonthCnt,'0')) as int) from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Child_wi  = '"+ Wi_Name + "') as ext_expo";
					List<List<String>> records = formObject.getDataFromDataSource(Query);
					if (records != null){
						aecb_months=Integer.parseInt(records.get(0).get(0));
					}
					if(aecb_months==0)
					{
						ClassificationCode = "AECB1";
					}
					else if(aecb_months<12)
					{
						ClassificationCode = "AECB12";
					}
					else if(aecb_months>=12)
					{
						ClassificationCode = "AECB4";
					}
					else
					{
						ClassificationCode = "AECBNA";
					}
				}
				catch(Exception ex)
				{
					ClassificationCode = "AECBNA";
					PersonalLoanS.mLogger.info("Error in getting AECBHistMonthCnt");
				}
				formObject.setNGValue("cmplx_EmploymentDetails_ClassificationCode", ClassificationCode);
			}
		}
		
		@SuppressWarnings("finally")
		public void Check_EFC_Limit()
		{
			PersonalLoanS.mLogger.info("Inside Check_EFC_Limit");
			String alerttxt = "";
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			List<List<String>> CardTypeandLimitXML=null;
			try
			{
				//Deepak Changes done for production issue when card is selected and no final limit is entered - 1 July 2019
				//String query="select Card_Product,Final_Limit,Cardproductselect,combined_limit from ng_rlos_IGR_Eligibility_CardLimit with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
				String query="select Card_Product,case when isnull(Ltrim(Final_Limit),0)='' then 0 else isnull(Final_Limit,0) end as Final_Limit,Cardproductselect,combined_limit from ng_rlos_IGR_Eligibility_CardLimit with (nolock) where Child_Wi='"+formObject.getWFWorkitemName()+"'";
				PersonalLoanS.mLogger.info("Inside Check_EFC_Limit : "+ query);
				CardTypeandLimitXML=formObject.getDataFromDataSource(query);
				int cardselected=0;
				double FinalLimit=0;
				double Conv=0;
				
				if (CardTypeandLimitXML != null){

					
					for (int i = 0; i<CardTypeandLimitXML.size();i++){
						if("true".equalsIgnoreCase(CardTypeandLimitXML.get(i).get(2)))
						{
							cardselected++;
							if(CardTypeandLimitXML.get(i).get(3).equalsIgnoreCase("0"))
							{
								FinalLimit+=Double.parseDouble(CardTypeandLimitXML.get(i).get(1));
							}
							else if(CardTypeandLimitXML.get(i).get(3).equalsIgnoreCase("1"))
							{
								Conv=Double.parseDouble(CardTypeandLimitXML.get(i).get(1));
							}	
						}
					}
					FinalLimit+=Conv;
					PersonalLoanS.mLogger.info("Inside Check_EFC_Limit Cardselected : "+ cardselected);
					PersonalLoanS.mLogger.info("Inside Check_EFC_Limit FinalLimit: "+ FinalLimit);
					if(CardTypeandLimitXML.size()>0 && cardselected==0 )
					{
						alerttxt = NGFUserResourceMgr_PL.getAlert("VAL103");
					}
					else if(CardTypeandLimitXML.size()>0 && cardselected>0)
					{
					//Changed for Sonar
						if(Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"))!=FinalLimit)
						{
							alerttxt = NGFUserResourceMgr_PL.getAlert("VAL102");
						}

					}
				}
				
				if("Conventional".equalsIgnoreCase(formObject.getNGValue("loan_type"))){
					// Deepak 25 Aug 2019 Query corrected as per JIRA PCAS-2741
					 //query="select isnull(max(CreditLimit),'') from ng_RLOS_CUSTEXPOSE_CardDetails with (nolock) where  Child_wi='"+formObject.getWFWorkitemName()+"'";
					  query="select isnull(max(CreditLimit),0) as conventionalCreditLimit from (select CreditLimit,case when SchemeCardProd is null then cardtype else SchemeCardProd end as cardprd from ng_RLOS_CUSTEXPOSE_CardDetails with (nolock) where Child_wi='"+formObject.getWFWorkitemName()+"' and  CifId in (select CIF_ID from ng_RLOS_GR_FinacleCRMCustInfo where fincust_wi_name='"+formObject.getWFWorkitemName()+"' and Consider_For_Obligations='true') and CardStatus not in ('C','CLSB','CLSC')) as temp_EXPOSE_CardDetails where"+
							 	" cardprd not in(select distinct code from ng_master_cardproduct where COMBINEDLIMIT_ELIGIBILITY='0')";
					  PersonalLoanS.mLogger.info("Query to validate conventionalCreditLimit & final Limit is:"+query);
						List<List<String>> result=formObject.getDataFromDataSource(query);
						if(!result.isEmpty())
						{
							if(Double.parseDouble(result.get(0).get(0))>FinalLimit)
							{
								alerttxt = NGFUserResourceMgr_PL.getAlert("VAL114");
							}
						}
				}
				
				
				
			}
			catch(Exception ex)
			{
				PersonalLoanS.mLogger.info("Inside Check_EFC_Limit exception"+ex.getStackTrace());
			}
			if(!alerttxt.equalsIgnoreCase("") && !"IM".equalsIgnoreCase(formObject.getNGValue("Sub_Product"))){
				throw new ValidatorException(new FacesMessage(alerttxt));
			}
			
			
		}
		
		//below code added by nikhil for PCAS-3036
	public void CheckforRejects(String Workstep)
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String Query="";
		String AlertMsg="";
		String IS_CA=formObject.getNGValue("IS_CA");
		String IS_CPV=formObject.getNGValue("IS_CPV");
		if(!"Y".equalsIgnoreCase(IS_CA) || !"Y".equalsIgnoreCase(IS_CPV))
		{
			PersonalLoanS.mLogger.info("Inside CheckforRejects 1st condition ");
			if("CAD_Analyst1".equalsIgnoreCase(Workstep) || "CAD_Analyst2".equalsIgnoreCase(Workstep))
			{
				PersonalLoanS.mLogger.info("Inside CheckforRejects CA condition ");
				Query="select top 1 Decision from NG_RLOS_GR_DECISION with (nolock) where workstepName in ('CPV','CPV_Analyst') and dec_wi_name = '"+formObject.getWFWorkitemName()+"' and insertionOrderId>(select isnull(max(insertionOrderId),0) from NG_RLOS_GR_DECISION with(nolock) where dec_wi_name = '"+formObject.getWFWorkitemName()+"' and workstepName='Reject_Queue' and Decision='Re-Initiate') order by insertionOrderId desc ";
				List <List <String>> Decision = formObject.getDataFromDataSource(Query);
				if(!Decision.isEmpty() && Decision!=null)
				{
					if("Reject".equalsIgnoreCase(Decision.get(0).get(0)) && "N".equalsIgnoreCase(IS_CPV))
					{
						AlertMsg="Workitem Rejected by CPV Unit!";
						throw new ValidatorException(new FacesMessage(AlertMsg));
					}
				}

			}
			else if("CPV".equalsIgnoreCase(Workstep) || "CPV_Analyst".equalsIgnoreCase(Workstep))
			{
				PersonalLoanS.mLogger.info("Inside CheckforRejects CPV condition ");
				//Query="select top 1 Decision from NG_RLOS_GR_DECISION with (nolock) where workstepName in ('CAD_Analyst2') and dec_wi_name = '"+formObject.getWFWorkitemName()+"' and 'Re-Initiate'<>(select top 1 decision from NG_RLOS_GR_DECISION where dec_wi_name = '"+formObject.getWFWorkitemName()+"' order by dateLastChanged desc ) order by dateLastChanged desc";
				Query="select top 1 Decision from NG_RLOS_GR_DECISION with (nolock) where workstepName in('CAD_Analyst1','CAD_Analyst2') and dec_wi_name = '"+formObject.getWFWorkitemName()+"' and insertionOrderId>(select isnull(max(insertionOrderId),0) from NG_RLOS_GR_DECISION with(nolock) where dec_wi_name = '"+formObject.getWFWorkitemName()+"' and workstepName='Reject_Queue' and Decision='Re-Initiate') order by insertionOrderId desc";
				List <List <String>> Decision = formObject.getDataFromDataSource(Query);
				
				if(!Decision.isEmpty() && Decision!=null)
				{
					PersonalLoanS.mLogger.info("Inside CheckforRejects CPV condition "+ Decision.get(0).get(0));
					PersonalLoanS.mLogger.info("Inside CheckforRejects CPV condition "+ IS_CA);
					if("Reject".equalsIgnoreCase(Decision.get(0).get(0)) && "N".equalsIgnoreCase(IS_CA))
					{
						AlertMsg="Workitem Rejected by UW Unit!";
						throw new ValidatorException(new FacesMessage(AlertMsg));
					}
				}
			}
		}
	}
	
	//Added by Rajan for PCASP-594
	public void LoadPickList1(String controlname, String query) {
		//CreditCard.mLogger.info( "Inside loadPicklist "); 
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			List<List<String>> result=formObject.getNGDataFromDataCache(query);
			if(!result.isEmpty()){
				formObject.clear(controlname);
				StringBuffer sb = new StringBuffer();
				for(List<String> result1 :result)
				{
					  formObject.addItem(controlname, result1);			      
					  for (String s : result1) {
					     sb.append(s);
					     sb.append("\n");
					  }
					  String str = sb.toString();
					  formObject.setToolTip(controlname,str);
				}
			}
		}
	
		
		public void Update_Office_Address()
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			openDemographicTabs();
			String Grid_name="cmplx_AddressDetails_cmplx_AddressGrid";
			int Grid_row_count=formObject.getLVWRowCount(Grid_name);
			Boolean Is_Office=false;
			int office_index=0;
			String Designation_code=formObject.getNGValue("cmplx_Customer_Designation")==null?"":formObject.getNGValue("cmplx_Customer_Designation");
			String Designation_desc=new PL_Integration_Input().getCodeDesc("NG_MASTER_Designation", "Description", Designation_code);
			String Employer_Name=formObject.getNGValue("cmplx_EmploymentDetails_EmpName")==null?"":formObject.getNGValue("cmplx_EmploymentDetails_EmpName");
			String Fname=formObject.getNGValue("cmplx_Customer_FIrstNAme");
			String Lname=formObject.getNGValue("cmplx_Customer_LAstNAme");
			String Customer_type="P-"+Fname+" "+Lname;
			String Deptt=formObject.getNGValue("cmplx_EmploymentDetails_Dept")==null?"":formObject.getNGValue("cmplx_EmploymentDetails_Dept");
			String StaffID=formObject.getNGValue("cmplx_EmploymentDetails_StaffID")==null?"":formObject.getNGValue("cmplx_EmploymentDetails_StaffID");
			String DepttStaffID=(!"".equals(Deptt) || !"".equals(StaffID))?Deptt+" - "+StaffID:"";
			for(int i=0;i<Grid_row_count;i++)
			{
				if("OFFICE".equalsIgnoreCase(formObject.getNGValue(Grid_name, i, 0)))
				{
					Is_Office=true;
					office_index=i;
					break;
				}
			}
			if(Is_Office)//if row is there
			{
				if(!Designation_desc.equalsIgnoreCase(formObject.getNGValue(Grid_name, office_index, 1)) && !"".equals(Designation_desc))
				{
					formObject.setNGValue(Grid_name, office_index, 1, Designation_desc);
				}
				if(!Employer_Name.equalsIgnoreCase(formObject.getNGValue(Grid_name, office_index, 2)) && !"".equals(Employer_Name))
				{
					formObject.setNGValue(Grid_name, office_index, 2, Employer_Name);
				}
				if(!DepttStaffID.equalsIgnoreCase(formObject.getNGValue(Grid_name, office_index, 3)) && !"".equals(DepttStaffID))					
				{
					formObject.setNGValue(Grid_name, office_index, 3, DepttStaffID);
				}
				if(!Customer_type.equalsIgnoreCase(formObject.getNGValue(Grid_name, office_index, 13)) )					
				{
					formObject.setNGValue(Grid_name, office_index, 13, Customer_type);
				}

			}
			else // if no row there 
			{
				formObject.setNGValue("buildname", Employer_Name);
				formObject.setNGValue("house", Designation_code);
				formObject.setNGValue("addtype", "OFFICE");
				formObject.setNGValue("Customer_Type", Customer_type);
				formObject.setNGValue("street", DepttStaffID);
				formObject.ExecuteExternalCommand("NGAddRow", "cmplx_AddressDetails_cmplx_AddressGrid");
			}

		}
		//added by shweta for PL
		public void setEfms()
		{//sagarika for efms changes
			try{
				String Efms_status="";
				FormReference formObject = FormContext.getCurrentInstance().getFormReference();
				String app_no = formObject.getWFWorkitemName().substring(formObject.getWFWorkitemName().indexOf("-")+1, formObject.getWFWorkitemName().lastIndexOf("-"));
				String query_efms ="select CASE_STATUS from (select top 1 case when CASE_STATUS='Confirmed Fraud' then 'Negative case' when CASE_STATUS='Closed' then 'False Positive' else isnull(CASE_STATUS,'') end as CASE_STATUS from NG_EFMS_RESPONSE where APPLICATION_NUMBER='"+app_no+"' order by CASE_STATUS desc) as EFMS_CASE_STATUS union all select APPLICATION_STATUS from (select top 1 APPLICATION_STATUS from NG_EFMS_RESPONSE where APPLICATION_NUMBER='"+app_no+"' order by  APPLICATION_STATUS) as EFMS_APPLICATION_STATUS";
				List<List<String>> result = formObject.getNGDataFromDataCache(query_efms);
				PersonalLoanS.mLogger.info("saaaaG"+query_efms);
				if(result!=null && !result.isEmpty())  //if(result!=null && result.size()>0)
				{
					PersonalLoanS.mLogger.info("efms_ sagarika::"+result.get(0).get(0));
					Efms_status=result.get(0).get(0);
					if("".equalsIgnoreCase(Efms_status)){
						Efms_status=result.get(1).get(0);
					}
					PersonalLoanS.mLogger.info("efms_ sagarika::"+Efms_status);
				}

				formObject.setNGValue("EFMS", Efms_status);
				formObject.setNGValue("EFMS_Status",Efms_status );
			}
			catch(Exception e){
				PersonalLoanS.mLogger.info("Exception occured in setting Efms status: "+ e.getMessage());
			}
			

		}
		
		public void expandFinacleCRMCustInfo(){
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			formObject.fetchFragment("FinacleCRM_CustInfo", "FinacleCRMCustInfo", "q_FinCRMCustInfo");
			formObject.setNGValue("FinacleCRMCustInfo_WINAME",formObject.getWFWorkitemName());
			formObject.setNGFrameState("FinacleCRM_CustInfo",0);
			if(!"DDVT_Maker".equalsIgnoreCase(formObject.getWFActivityName()))
			{
				if("Salaried".equalsIgnoreCase(formObject.getNGValue("EmploymentType")))
				{
					formObject.addComboItem("FinacleCRMCustInfo_ApplicantType","--Select--","");
					formObject.addComboItem("FinacleCRMCustInfo_ApplicantType", "Individual", "Individual");
				}
				else
				{
					formObject.addComboItem("FinacleCRMCustInfo_ApplicantType","--Select--","");
					formObject.addComboItem("FinacleCRMCustInfo_ApplicantType", "Individual", "Individual");
					formObject.addComboItem("FinacleCRMCustInfo_ApplicantType", "Corporate", "Corporate");
				}
			}
		}
		
		public void SetReminder_CA(FormReference formObject)
		{
			if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_SetReminder_CA")) && "CA_HOLD".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision"))){	
				String Email_sub="REMINDER- case no "+formObject.getWFWorkitemName()+" pending for the action of "+formObject.getUserName();
				//CreditCard.mLogger.info("Email_sub: "+ Email_sub);
				String Email_message="<html><body>Dear User, \n Kindly call the Customer back. \n \n Remarks: "+formObject.getNGValue("cmplx_OffVerification_reamrks")+". \n Decision Remarks � "+formObject.getNGValue("cmplx_Decision_REMARKS")+". </body></html>";
				//CreditCard.mLogger.info("Email_sub: "+ Email_message);
				String query = "select count(WI_Name) from NG_RLOS_EmailReminder with(nolock) where WI_Name='"+formObject.getWFWorkitemName()+"' and Email_Name='CA_HOLD'";
				List<List<String>> Emailrecords = formObject.getDataFromDataSource(query);
				String emailTo = "select  ConstantValue from CONSTANTDEFTABLE where constantName='CONST_CONST_CAD' and processdefid=1";
				List<List<String>> mailTo = formObject.getNGDataFromDataCache(emailTo);
				String mailto_cpv=mailTo.get(0).get(0);
				String email_From = "select  ConstantValue from CONSTANTDEFTABLE where constantName='CONST_mailFrom' and processdefid=19";
				List<List<String>> mail_From = formObject.getNGDataFromDataCache(email_From);
				String mail_from_cpv=mail_From.get(0).get(0);
				PersonalLoanS.mLogger.info("@sagarika"+ " mail fromssss" + mail_from_cpv);
				PersonalLoanS.mLogger.info("query NG_RLOS_EmailReminder count is: "+query);
				PersonalLoanS.mLogger.info("query NG_RLOS_EmailReminder count result: "+Emailrecords);
			
				try{
					String reminderInsertQuery="";
					if(Emailrecords!=null && Emailrecords.size()>0 && Emailrecords.get(0)!=null ){
						String EmailRow_count=Emailrecords.get(0).get(0);
						PersonalLoanS.mLogger.info("query NG_RLOS_EmailReminder count EmailRow_count: "+EmailRow_count);
						if("0".equalsIgnoreCase(EmailRow_count)){
							reminderInsertQuery="insert into NG_RLOS_EmailReminder(Email_Name,Email_Status,Email_To,Email_From,EmailSubject,EmailMessage,SetReminder,WI_Name,Workstep_Name)"
									+" values('CA_HOLD','P','"+mailto_cpv+"','"+mail_from_cpv+"','"+Email_sub+"','"+Email_message+"','"+formObject.getNGValue("cmplx_Decision_SetReminder_CA")+"','"+formObject.getWFWorkitemName()+"','"+formObject.getWFActivityName()+"')";
						}
						else{
							reminderInsertQuery="update NG_RLOS_EmailReminder set Email_Status='P' where WI_Name='"+formObject.getWFWorkitemName()+"' and Email_Name='CA_HOLD'";
						}
						PersonalLoanS.mLogger.info("Query to insert NG_RLOS_EmailReminder: "+ reminderInsertQuery);
						formObject.saveDataIntoDataSource(reminderInsertQuery);
					}
				}
				catch(Exception e){
					PersonalLoanS.mLogger.info("qException occured in CPV reminder Email insert:"+query);
				}}
		}
		public void SetReminder_Smart(FormReference formObject)
		{
			if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_SetReminder_Smart")) && "Smart CPV Hold".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision"))){	
				String Email_sub="REMINDER- case no "+formObject.getWFWorkitemName()+" pending for the action of "+formObject.getUserName();
				String Email_message="<html><body>Dear User, \n Kindly call the Customer back. \n \n Remarks: "+formObject.getNGValue("cmplx_OffVerification_reamrks")+". \n Decision Remarks � "+formObject.getNGValue("cmplx_Decision_REMARKS")+". </body></html>";
				String query = "select count(WI_Name) from NG_RLOS_EmailReminder with(nolock) where WI_Name='"+formObject.getWFWorkitemName()+"' and Email_Name='Smart_Hold'";
				List<List<String>> Emailrecords = formObject.getDataFromDataSource(query);
				String emailTo = "select  ConstantValue from CONSTANTDEFTABLE where constantName='CONST_Smart_CPV_Refer' and processdefid=1";
				List<List<String>> mailTo = formObject.getNGDataFromDataCache(emailTo);
				String mailto_cpv=mailTo.get(0).get(0);
				PersonalLoanS.mLogger.info("@sagarika"+ " mail to" + mailto_cpv);
				String email_From = "select  ConstantValue from CONSTANTDEFTABLE where constantName='CONST_mailFrom' and processdefid=19";
				List<List<String>> mail_From = formObject.getNGDataFromDataCache(email_From);
				String mail_from_cpv=mail_From.get(0).get(0);
				PersonalLoanS.mLogger.info("@sagarika"+ " mail fromssss" + mail_from_cpv);
				PersonalLoanS.mLogger.info("query NG_RLOS_EmailReminder count is: "+query);
				PersonalLoanS.mLogger.info("query NG_RLOS_EmailReminder count result: "+Emailrecords);
			
				try{
					String reminderInsertQuery="";
					if(Emailrecords!=null && Emailrecords.size()>0 && Emailrecords.get(0)!=null ){
						String EmailRow_count=Emailrecords.get(0).get(0);
						PersonalLoanS.mLogger.info("query NG_RLOS_EmailReminder count EmailRow_count: "+EmailRow_count);
						if("0".equalsIgnoreCase(EmailRow_count)){
							reminderInsertQuery="insert into NG_RLOS_EmailReminder(Email_Name,Email_Status,Email_To,Email_From,EmailSubject,EmailMessage,SetReminder,WI_Name,Workstep_Name)"
									+" values('Smart_Hold','P','"+mailto_cpv+"','"+mail_from_cpv+"','"+Email_sub+"','"+Email_message+"','"+formObject.getNGValue("cmplx_Decision_SetReminder_Smart")+"','"+formObject.getWFWorkitemName()+"','"+formObject.getWFActivityName()+"')";
						}
						else{
							reminderInsertQuery="update NG_RLOS_EmailReminder set Email_Status='P' where WI_Name='"+formObject.getWFWorkitemName()+"' and Email_Name='Smart_Hold'";
						}
						PersonalLoanS.mLogger.info("Query to insert NG_RLOS_EmailReminder: "+ reminderInsertQuery);
						formObject.saveDataIntoDataSource(reminderInsertQuery);
					}
				}
				catch(Exception e){
					PersonalLoanS.mLogger.info("qException occured in CPV reminder Email insert:"+query);
				}}
		}
		
		
		public void alignDemographiTab(FormReference formObject){
			if( "N".equalsIgnoreCase(formObject.getNGValue("is_cc_waiver_require")) || "CSM".equalsIgnoreCase(formObject.getWFActivityName())){
				if("Yes".equalsIgnoreCase(formObject.getNGValue("cmplx_CardDetails_suppcardreq"))){
					adjustFrameTops("Address_Details_container,Alt_Contact_container,Card_Details,Supplementary_Card_Details,FATCA,KYC,OECD,ReferenceDetails");

				}else {
					adjustFrameTops("Address_Details_container,Alt_Contact_container,Card_Details,FATCA,KYC,OECD,ReferenceDetails");

				}
			} else {
				if("Yes".equalsIgnoreCase(formObject.getNGValue("cmplx_CardDetails_suppcardreq"))){
					adjustFrameTops("Address_Details_container,Alt_Contact_container,Supplementary_Card_Details,FATCA,KYC,OECD,ReferenceDetails");

				}else {
					adjustFrameTops("Address_Details_container,Alt_Contact_container,FATCA,KYC,OECD,ReferenceDetails");

				}
			}

		}
		
		//Deepak New method added to Check Customer NTB/Existing.
		public boolean Check_ECRNGenOnChange(){
			boolean ECRNGenFlag=false;
			try{
				FormReference formObject = FormContext.getCurrentInstance().getFormReference();
				if ("".equalsIgnoreCase(formObject.getNGValue("ECRN"))){
					int gridRowCount = formObject.getLVWRowCount("cmplx_CardDetails_cmplx_CardCRNDetails");
					for(int i=0;i<gridRowCount;i++)
					{
						String ECRN = formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails", i, 1);
						PersonalLoanS.mLogger.info("ECRN: "+ ECRN);
						if(!"".equalsIgnoreCase(ECRN)){
							ECRNGenFlag=true;
							break;
						}
					}
				}
				else{
					ECRNGenFlag=true;
				}
			}
			catch(Exception e){
				PersonalLoanS.mLogger.info("Exception occured in Check_ECRNGenOnChange" +e.getMessage());
			}
			return ECRNGenFlag;
		}
		
		// Hritik 6.7.21 
		public void CC_waiver_check(){
			try
			{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			PersonalLoanS.mLogger.info("cc_check outside");
			String query="select COUNT(Card_Product) from ng_rlos_IGR_Eligibility_CardLimit where Child_Wi = '" + formObject.getWFWorkitemName() + "' and Cardproductselect = 'true'";
			PersonalLoanS.mLogger.info("cc_check Q"+ query);
			List<List<String>> CC_wavier_check = formObject.getDataFromDataSource(query);
			PersonalLoanS.mLogger.info("cc_check Q_res"+ CC_wavier_check);
			if (Integer.parseInt(CC_wavier_check.get(0).get(0))>0){
				PersonalLoanS.mLogger.info("cc_check Q_size"+ CC_wavier_check.size());
				formObject.setNGValue("is_cc_waiver_require","N");
			}
			else{
				formObject.setNGValue("is_cc_waiver_require","Y");
			}
			PersonalLoanS.mLogger.info("cc_check_exit res"+ formObject.getNGValue("is_cc_waiver_require"));
			}
			catch(Exception e){
				PersonalLoanS.mLogger.info("Exception occured in CC waiver check "+e.getMessage());
			}
		} //end
		
		//hritik - pCASI - 3801
		public void Moratorium(){
			try{
				PersonalLoanS.mLogger.info("Inside Moratorium");
				FormReference formObject = FormContext.getCurrentInstance().getFormReference();
				String disb = formObject.getNGValue("LoanDetails_disbdate");
				String frep = formObject.getNGValue("cmplx_LoanDetails_frepdate");
				PersonalLoanS.mLogger.info("Inside Moratorium:disb: "+disb+"frep:"+frep);
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				
				Date d1 = sdf.parse(disb);
				Date d2 = sdf.parse(frep);
				
				PersonalLoanS.mLogger.info("d1"+d1+"d2"+d2);
				long diffTime= d2.getTime()- d1.getTime();
				long diff = (diffTime / (1000*60*60*24))%365;
				
				PersonalLoanS.mLogger.info("diff"+diff);
				formObject.setNGValue("cmplx_LoanDetails_moratorium",diff);
				PersonalLoanS.mLogger.info("Value set of Moratorium"+ formObject.getNGValue("cmplx_LoanDetails_moratorium"));
			}
			catch(Exception e){
				PersonalLoanS.mLogger.info("Exception occured in Moratorium: " +e.getMessage());
			}
		}
		
		public void takeoveramt(){ //jahanvi   3257 pcasi
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			
			try{
			String	query1="select isnull(sum(convert(float,Take_Amount)),0) as takeoveramt from ng_rlos_cust_extexpo_LoanDetails  with (nolock) where child_Wi='"+formObject.getWFWorkitemName()+"' and Take_Over_Indicator='true' " ;

			String	query2="select isnull(sum(convert(float,CurrentBalance)),0) as takeoveramt from ng_rlos_cust_extexpo_CardDetails where  child_Wi='"+formObject.getWFWorkitemName()+" ' and Take_Over_Indicator='true'";

			String	query3= "select isnull(sum(convert(float,TakeOver_Amount)),0) as takeoveramt from ng_rlos_gr_LiabilityAddition where LiabilityAddition_wiName= '"+formObject.getWFWorkitemName()+"' and TakeOverIndicator='true'";
			
			List<List<String>> l1=formObject.getDataFromDataSource(query1);
			List<List<String>> l2=formObject.getDataFromDataSource(query2);
			List<List<String>> l3=formObject.getDataFromDataSource(query3);
			PersonalLoanS.mLogger.info("queriessssss"+query1+" "+query2+" "+query3);
			double s1=Double.parseDouble(l1.get(0).get(0));
			double s2=Double.parseDouble(l2.get(0).get(0));
			double s3=Double.parseDouble(l3.get(0).get(0));
			double summ=0;
			summ=s1+s2+s3;
			formObject.setNGValue("cmplx_EligibilityAndProductInfo_TakeoverAMount", summ);		
			PersonalLoanS.mLogger.info(summ);
			}
			catch(Exception e){
				PersonalLoanS.mLogger.info("Exception occured in takeoveramt: " +e.getMessage());
			}
			
		}
	
		
	
		
		public void CheckNTBOnFinCrmChange(){
			try{
				FormReference formObject = FormContext.getCurrentInstance().getFormReference();
				int Row_count=formObject.getLVWRowCount("cmplx_FinacleCRMCustInfo_FincustGrid");
				String Cif="";
				
				for(int i=0;i<Row_count;i++)
				{
					String considerForObligation=formObject.getNGValue("cmplx_FinacleCRMCustInfo_FincustGrid", i, 13);
					if("true".equalsIgnoreCase(considerForObligation)){
						Cif=formObject.getNGValue("cmplx_FinacleCRMCustInfo_FincustGrid", i, 0);
						break;
					}
				}
				 if("".equalsIgnoreCase(Cif) && "false".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")) && !"".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_CIFNO"))){
					formObject.setNGValue("cmplx_Customer_NTB","true");
					formObject.setNGValue("cmplx_Customer_CIFNO","");
					formObject.setNGValue("CIF_ID","");
					formObject.setNGValue("CifLabel","");
				}
				else if((!"".equalsIgnoreCase(Cif)) && "true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB")) ){
					formObject.setNGValue("cmplx_Customer_NTB","false");
					formObject.setNGValue("cmplx_Customer_CIFNO",Cif);
					formObject.setNGValue("CIF_ID",Cif);
					formObject.setNGValue("CifLabel",Cif);
				}
				
			}
			catch(Exception e){
				PersonalLoanS.mLogger.info("Exception occured in CheckNTBOnFinCrmChange" +e.getMessage());
			}
			
		}
		
		public String getBlacklistDetails(String Operation)
		{
			PersonalLoanS.mLogger.info("Inside Blacklist getBlacklistDetails " + Operation);
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			Boolean PartMatch_isVisible = formObject.isVisible("PartMatch_Frame1");
			String Final_Xml="";
			String addstr_xml="";
			if(!PartMatch_isVisible)
			{

				formObject.fetchFragment("Part_Match", "PartMatch", "q_PartMatch");
				formObject.setNGFrameState("Part_Match", 0);
			}
				adjustFrameTops("Part_Match,FinacleCRM_Incidents,FinacleCRM_CustInfo,External_Blacklist,Finacle_Core,MOL,World_Check,Reject_Enq,Sal_Enq,Credit_card_Enq1,LOS1,Case_History1");

			
			String GridName="cmplx_PartMatch_cmplx_PartBlacklistGrid";
			int row_count = formObject.getLVWRowCount(GridName);
			PersonalLoanS.mLogger.info("Inside Blacklist getBlacklistDetails rowcound" + row_count);
			for(int i=0;i<row_count;i++)
			{
				addstr_xml="";
				if("Blacklist".equalsIgnoreCase(Operation) && !"".equalsIgnoreCase(formObject.getNGValue(GridName,i,4)))
				{
					addstr_xml="<BlacklistDetails><blacklist_cust_type>I</blacklist_cust_type>";
					addstr_xml+="<internal_blacklist>"+formObject.getNGValue(GridName,i,4)+"</internal_blacklist>";
					addstr_xml+="<internal_blacklist_date>"+formObject.getNGValue(GridName,i,18)+"</internal_blacklist_date>";
					addstr_xml+="<internal_blacklist_code>"+formObject.getNGValue(GridName,i,7)+"</internal_blacklist_code></BlacklistDetails>";
				}
				else if("Negated".equalsIgnoreCase(Operation) && !"".equalsIgnoreCase(formObject.getNGValue(GridName,i,8)))
				{
					addstr_xml="<NegatedDetails><negative_cust_type>I</negative_cust_type>";
					addstr_xml+="<internal_negative_flag>"+formObject.getNGValue(GridName,i,8)+"</internal_negative_flag>";
					addstr_xml+="<internal_negative_date>"+formObject.getNGValue(GridName,i,18)+"</internal_negative_date>";
					addstr_xml+="<internal_negative_code>"+formObject.getNGValue(GridName,i,11)+"</internal_negative_code></NegatedDetails>";
				}
				PersonalLoanS.mLogger.info("Inside Blacklist getBlacklistDetails semi xml" + addstr_xml);
				Final_Xml+=addstr_xml;
			}
			PersonalLoanS.mLogger.info("Inside Blacklist getBlacklistDetails semi xml" + Final_Xml);
			return Final_Xml;
		}
		
		
		public void checkforCurrentAccounts()
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			try{
				String query = "select distinct acctid from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where child_wi = '"+formObject.getWFWorkitemName()+"' and AcctType not like '%CHARGE RECEIVABLE%'  and AcctStat='Active'";//query modified by akshay on 5/7/18 for proc 11633
				List<List<String>> accounts = formObject.getDataFromDataSource(query);
				int count = accounts.size();
				String Acc_num="";
				for(List<String> account : accounts)
				{
					if("".equalsIgnoreCase(Acc_num)){
						Acc_num=account.get(0);
					}
					else
					{
						Acc_num+=","+account.get(0);
					}
				}
				formObject.setNGValue("Account_Number",Acc_num);
				if(count>0){
					formObject.setEnabled("AlternateContactDetails_RetainAccIfLoanReq", false);

				}
				else{
					formObject.setEnabled("AlternateContactDetails_RetainAccIfLoanReq", true);

				}
							
			}
			catch(Exception ex){
				printException(ex);
			}
		}
		public void autopopulate_emp_verification()
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			int framestate3=formObject.getNGFrameState("EmploymentDetails");
			if(framestate3 != 0){
				loadEmployment();
			}	
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
			int framestate4=formObject.getNGFrameState("Address_Details_container");
			if(framestate4 == 0){
				PersonalLoanS.mLogger.info("Address_Details_container");
			}
			else {
				formObject.fetchFragment("Address_Details_container", "AddressDetails", "q_AddressDetails");
				formObject.setNGFrameState("Address_Details_container", 0);
				alignDemographiTab(formObject);
				PersonalLoanS.mLogger.info("fetched address details");
			}
			int framestate5=formObject.getNGFrameState("MOL");
			if(framestate5!= 0){
				formObject.fetchFragment("MOL", "MOL1", "q_MOL");
				formObject.setNGFrameState("MOL", 0);
				adjustFrameTops("MOL,World_Check,Reject_Enq,Sal_Enq,Credit_card_Enq1,LOS1,Case_History1");			

			}
			formObject.setNGValue("EmploymentVerification_s2_Text23", formObject.getNGValue("AlternateContactDetails_OFFICENO"));
			formObject.setNGValue("cmplx_emp_ver_sp2_application_type", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5));
			formObject.setNGValue("EmploymentVerification_s2_Text1", formObject.getNGValue("cmplx_EmploymentDetails_Designation"));
			formObject.setNGValue("EmploymentVerification_s2_Text4", formObject.getNGValue("cmplx_EmploymentDetails_DOJ"));
			
			formObject.setNGValue("EmploymentVerification_s2_Combo14", formObject.getNGValue("cmplx_EmploymentDetails_EmpStatus"));
			formObject.setNGValue("EmploymentVerification_s2_Text7", formObject.getNGValue("cmplx_IncomeDetails_AvgNetSal"));
			//LoadPickList("cmplx_emp_ver_sp2_empstatus_remarks","select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_EmploymentStatus with (nolock) where isActive='Y' order by Code");

			if("Y".equalsIgnoreCase(formObject.getNGValue("cmplx_IncomeDetails_SalaryXferToBank")))
			{//Generate_Template
				formObject.setNGValue("EmploymentVerification_s2_Text29",formObject.getNGValue("cmplx_Customer_CIFNo"));
				
			}
			else
			{
				
				formObject.setNGValue("EmploymentVerification_s2_Text29","NA-No salary transfer with Rakbank");
				
			}
			formObject.setNGValue("EmploymentVerification_s2_desig_visa", formObject.getNGValue("cmplx_EmploymentDetails_DesigVisa"));
			int offc_index=-1;
			
			for (int i=0;i<formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");i++)
			{
				PersonalLoanS.mLogger.info("Check office index value" + formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,0));

				if("OFFICE".equalsIgnoreCase(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,0)))
				{
					offc_index = i;
					break;
				}
			}
			PersonalLoanS.mLogger.info("Check office index" + offc_index);

			
			if(offc_index>-1)
			{
				formObject.setNGValue("EmploymentVerification_s2_flat_vila", formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",offc_index,1));
				formObject.setNGValue("EmploymentVerification_s2_off_add", formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",offc_index,0));
				formObject.setNGValue("EmploymentVerification_s2_Build_name", formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",offc_index,2));
				formObject.setNGValue("EmploymentVerification_s2_street_name", formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",offc_index,3));
				formObject.setNGValue("EmploymentVerification_s2_landmark", formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",offc_index,4));
				formObject.setNGValue("EmploymentVerification_s2_Area_city", formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",offc_index,5));
			}
			formObject.setNGValue("EmploymentVerification_s2_freezone", formObject.getNGValue("cmplx_EmploymentDetails_Freezone").equals("false")?"No":"Yes");
			if(formObject.getNGValue("cmplx_EmploymentDetails_Freezone").equals("true"))
			{
			formObject.setNGValue("EmploymentVerification_s2_Freezone_name", formObject.getNGValue("cmplx_EmploymentDetails_FreezoneName"));
			}
			formObject.setNGValue("EmploymentVerification_s2_visa_company_name", formObject.getNGValue("cmplx_EmploymentDetails_EmpName"));
			formObject.setNGValue("EmploymentVerification_s2_TL_emirate", formObject.getNGValue("cmplx_EmploymentDetails_TL_Emirate"));
			formObject.setNGValue("EmploymentVerification_s2_TL_Number", formObject.getNGValue("cmplx_EmploymentDetails_TL_Number"));
			formObject.setNGValue("EmploymentVerification_s2_loan_emp_name", formObject.getNGValue("cmplx_EmploymentDetails_EmpName"));	
			formObject.setNGValue("EmploymentVerification_s2_aloc_name", formObject.getNGValue("cmplx_EmploymentDetails_EmpName"));	
			//hritik 3267
			formObject.setNGValue("EmploymentVerification_s2_COB", formObject.getNGValue("cmplx_EmploymentDetails_JobConfirmed"));
			//below field added by bandana for PCASI-3033 related
			formObject.setNGValue("cmplx_emp_ver_sp2_company_name", formObject.getNGValue("cmplx_EmploymentDetails_EmpName"));	
			
			formObject.setNGValue("EmploymentVerification_s2_labor_card", formObject.getNGValue("cmplx_MOL_Labour_card_no"));	
			formObject.setNGValue("EmploymentVerification_s2_MOL_salary", formObject.getNGValue("cmplx_MOL_gross"));
			formObject.setLocked("cmplx_emp_ver_sp2_landline",true);
			formObject.setEnabled("cmplx_emp_ver_sp2_landline",false);
			formObject.setNGValue("EmploymentVerification_s2_auth_name", formObject.getNGValue("cmplx_EmploymentDetails_authsigname"));
			formObject.setNGValue("EmploymentVerification_s2_Text31", formObject.getNGValue("cmplx_emp_ver_sp2_fpu_rem"));
			formObject.setNGValue("EmploymentVerification_s2_Text47", formObject.getNGValue("cmplx_emp_ver_sp2_FPU_remarks_comp_mismatch"));
			formObject.setNGValue("EmploymentVerification_s2_comp_emp_name", formObject.getNGValue("cmplx_EmploymentDetails_EmpName"));
			formObject.setNGValue("EmploymetlntVerification_s2_comp_emirate", formObject.getNGValue("cmplx_EmploymentDetails_TL_Emirate"));
			formObject.setNGValue("EmploymentVerification_s2_comp_tl_no", formObject.getNGValue("cmplx_EmploymentDetails_TL_Number"));
			//formObject.setNGValue("EmploymentVerification_s2_Combo12", formObject.getNGValue("cmplx_emp_ver_sp2_empstatus_remarks"));
			
			List<String> LoadPicklist_Verification= Arrays.asList("cmplx_emp_ver_sp2_desig_drop","cmplx_emp_ver_sp2_desig_drop","cmplx_emp_ver_sp2_doj_drop","cmplx_emp_ver_sp2_empstatus_drop","cmplx_emp_ver_sp2_salary_drop","cmplx_emp_ver_sp2_salary_break_drop","cmplx_emp_ver_sp2_sal_pay_drop");
			List<String> LoadPicklist_update= Arrays.asList("EmploymentVerification_s2_Designation_button2","EmploymentVerification_s2_Designation_button6_View","cmplx_emp_ver_sp2_doj_remarks","cmplx_emp_ver_sp2_empstatus_remarks","cmplx_emp_ver_sp2_salary_remarks","cmplx_emp_ver_sp2_salary_break_remarks","cmplx_emp_ver_sp2_sal_pay_remarks","cmplx_emp_ver_sp2_landline");
			mismatchFieldProperty(formObject,LoadPicklist_Verification,LoadPicklist_update);
			PersonalLoanS.mLogger.info( "all values");
			if("".equals(formObject.getNGValue("cmplx_EmploymentDetails_TL_Number")))
			{
				formObject.setLocked("EmploymentVerification_s2_Frame3", true);
			}
			try{
				String query = "select count(wi_name) from ng_RLOS_EmpDetails where EmpCode='"+formObject.getNGValue("cmplx_EmploymentDetails_EMpCode")+"' and wi_name in(select ProcessInstanceID from WFINSTRUMENTTABLE where ActivityId=9 and ProcessDefID=1)";
				List<List<String>> Result = formObject.getDataFromDataSource(query);
				if(!Result.isEmpty() && Result!=null)
				{
					formObject.setNGValue("cmplx_emp_ver_sp2_num_active_CC", Result.get(0).get(0));
				}
				
				
			}
			catch(Exception ex){
				printException(ex);
			}
			try{
				//String query = "select count(wi_name) from ng_RLOS_EmpDetails where EmpCode='"+formObject.getNGValue("cmplx_EmploymentDetails_EMpCode")+"' and wi_name in(select ProcessInstanceID from WFINSTRUMENTTABLE where ActivityId=112 and ProcessDefID=19)";
				String query="select count(wi_name) from ng_RLOS_EmpDetails where employer_name= '"+formObject.getNGValue("EmploymentVerification_s2_loan_emp_name")+"' and wi_name in(select pl_wi_name from ng_pl_exttable where CURR_WSNAME= 'Disbursed')";
				//above modified code by jahnavi as earlier query was fetching wrong count
				List<List<String>> Result = formObject.getDataFromDataSource(query);
				if(!Result.isEmpty() && Result!=null)
				{
					formObject.setNGValue("cmplx_emp_ver_sp2_num_active_loan", Result.get(0).get(0));
				}
				
							
			}
			
			catch(Exception ex){
				printException(ex);
			}
			try{
				formObject.clear("cmplx_emp_ver_sp2_cmplx_emp_ver_last_Loan_cc");
				//String query = "select top 1 CC_Wi_Name,DSA_Name,RM_Name,(select top 1 isnull(dateLastChanged,'') as checklatest from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='Disbursal' order by dateLastChanged  ),Final_Limit,(select EmirateVisa from ng_RLOS_Customer where wi_name=cc_Wi_name ),'"+formObject.getWFWorkitemName()+"' from NG_CC_EXTTABLE where Reject_dec is null  and CC_Wi_Name in (select wi_name from ng_RLOS_EmpDetails where EmpCode='"+formObject.getNGValue("cmplx_EmploymentDetails_EMpCode")+"' and wi_name in(select ProcessInstanceID from WFINSTRUMENTTABLE where ActivityId=9 and ProcessDefID=1 )) union all select top 1 PL_Wi_Name,DSA_Name,RM_Name,(select top 1 dateLastChanged as latestdate from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='Disbursal' order by dateLastChanged ),Final_Limit,(select EmirateVisa from ng_RLOS_Customer where wi_name=PL_Wi_Name),'"+formObject.getWFWorkitemName()+"' from NG_PL_EXTTABLE where Reject_dec is null and PL_Wi_Name in (select wi_name from ng_RLOS_EmpDetails where EmpCode='"+formObject.getNGValue("cmplx_EmploymentDetails_EMpCode")+"' and wi_name in(select ProcessInstanceID from WFINSTRUMENTTABLE where ActivityId=112 and ProcessDefID=19 ))";
				String query="select top 1 PL_Wi_Name,DSA_Name,RM_Name,(select top 1 dateLastChanged as latestdate from NG_RLOS_GR_DECISION where  workstepname='Disbursal_Checker' and decision in ('Approve and Exit','Approve and Post Disbursal','Approve and Takeover') and dec_wi_name=PL_Wi_Name order by dateLastChanged ) as disb_dec,Final_Limit,(select case when EmirateVisa='--Select--' then '' else EmirateVisa end  from ng_RLOS_Customer where wi_name=PL_Wi_Name),'"+formObject.getWFWorkitemName()+"' from NG_PL_EXTTABLE where  CURR_WSNAME= 'Disbursed' and PL_Wi_Name in (select wi_name from ng_RLOS_EmpDetails where EmpCode='"+formObject.getNGValue("cmplx_EmploymentDetails_EMpCode")+"')and Reject_dec is null order by disb_dec desc ";
				PersonalLoanS.mLogger.info("query for last loan from same company"+query);
				List<List<String>> list_acc_rej=formObject.getDataFromDataSource(query);
				for(List<String> mylist : list_acc_rej)
				{
					PersonalLoanS.mLogger.info( "Data to be added in Grid account Grid: "+mylist.toString());
					formObject.addItemFromList("cmplx_emp_ver_sp2_cmplx_emp_ver_last_Loan_cc", mylist);
					PersonalLoanS.mLogger.info("check the grid please"+formObject.getNGValue("cmplx_emp_ver_sp2_cmplx_emp_ver_last_Loan_cc"));
				}
			
				
							
			}
			catch(Exception ex){
				printException(ex);
			}
			try
			{
				String query1="";
				formObject.clear("cmplx_emp_ver_sp2_cmplx_loan_disb");
				//Deepak Query changed for PCAS-1260
				
				//query="select Product,'24 Months',CC_Wi_Name,CRN,CIF_ID,CUSTOMERNAME,Employer_Name,(select EmirateVisa from ng_RLOS_Customer where wi_name=cc_Wi_name),(select TL_Number from ng_RLOS_EmpDetails where wi_name=cc_Wi_name),(select OfficeNo from ng_RLOS_AltContactDetails where wi_name=cc_Wi_name),(select LOS from ng_RLOS_EmpDetails where wi_name=cc_Wi_name),(select Nationality from ng_RLOS_Customer where wi_name=cc_Wi_name),(select yearsINUAE from ng_RLOS_Customer where wi_name=cc_Wi_name),DSA_Name,RM_Name,(select desigstatus from ng_RLOS_EmpDetails where wi_name=cc_Wi_name),(select top 1 userName from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='FPU'),(select top 1 remarks from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='FPU'),(select top 1 Decision from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='FPU'),(select subfeedback from ng_rlos_decisionHistory where wi_name=CC_Wi_Name),(select top 1 dateLastChanged from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='FPU'),(select top 1 dateLastChanged from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='Disbursal' order by dateLastChanged),Final_Limit,'"+formObject.getWFWorkitemName()+"' from NG_CC_EXTTABLE where Reject_dec is null  and CC_Wi_Name in (select wi_name from ng_RLOS_EmpDetails where EmpCode='"+formObject.getNGValue("cmplx_EmploymentDetails_EMpCode")+"' and wi_name in(select ProcessInstanceID from WFINSTRUMENTTABLE where ActivityId=9 and ProcessDefID=1 and EntryDATETIME between GETDATE()-365 and GETDATE())) union all select Product,'24 Months',PL_Wi_Name,CRN,CIF_ID,CUSTOMERNAME,Employer_Name,(select EmirateVisa from ng_RLOS_Customer where wi_name=PL_Wi_Name),(select TL_Number from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),(select OfficeNo from ng_RLOS_AltContactDetails where wi_name=PL_Wi_Name),(select LOS from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),(select Nationality from ng_RLOS_Customer where wi_name=PL_Wi_Name),(select yearsINUAE from ng_RLOS_Customer where wi_name=PL_Wi_Name),DSA_Name,RM_Name,(select desigstatus from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),(select top 1 userName from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),(select top 1 remarks from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),(select top 1 Decision from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),(select subfeedback from ng_rlos_decisionHistory where wi_name=PL_Wi_Name),(select top 1 dateLastChanged from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),(select top 1 dateLastChanged from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='Disbursal' order by dateLastChanged),Final_Limit,'"+formObject.getWFWorkitemName()+"'  from NG_PL_EXTTABLE where Reject_dec is null and PL_Wi_Name in (select wi_name from ng_RLOS_EmpDetails where EmpCode='"+formObject.getNGValue("cmplx_EmploymentDetails_EMpCode")+"' and wi_name in(select ProcessInstanceID from WFINSTRUMENTTABLE where ActivityId=112 and ProcessDefID=19 and EntryDATETIME between GETDATE()-365 and GETDATE()))";


				query1="select  Product, case when DATEDIFF(MONTH,( (select top 1  convert(varchar,dateLastChanged,120)  from NG_RLOS_GR_DECISION with(nolock) where dec_wi_name=PL_Wi_Name order by dateLastChanged desc)),GETDATE())< 6 then '0-6 month' when DATEDIFF(MONTH,( (select top 1  convert(varchar,dateLastChanged,120)  from NG_RLOS_GR_DECISION with(nolock) where dec_wi_name=PL_Wi_Name order by dateLastChanged desc)),GETDATE())<12 then'6-12 month' else '12-24 month' end  as month_diff,PL_Wi_Name,isnull((select AgreementID from ng_rlos_LoanDisbursal where wi_name=PL_Wi_Name),''),isnull(CIF_ID,''),CUSTOMERNAME,(select employer_name from ng_RLOS_empdetails where wi_name=PL_Wi_Name),isnull((select case when EmirateVisa='--Select--' then '' else EmirateVisa end  from ng_RLOS_Customer where wi_name=PL_Wi_Name),''),isnull((select TL_Number from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),''),(select OfficeNo from ng_RLOS_AltContactDetails where wi_name=PL_Wi_Name),isnull((select LOS from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),''),isnull((select top 1 description from ng_master_country where code=( select Nationality from ng_RLOS_Customer where wi_name=PL_Wi_Name)),''),isnull((select yearsINUAE from ng_RLOS_Customer where wi_name=PL_Wi_Name),''),isnull(DSA_Name,''),isnull(RM_Name,''),(select case when  (select top 1 description from ng_master_designation where code=(select desigstatus from ng_rlos_empdetails where wi_name=PL_Wi_Name)) is not null then (select top 1 description from ng_master_designation where code=(select desigstatus from ng_rlos_empdetails where wi_name = PL_Wi_Name))  else (select desigstatus from ng_rlos_Empdetails where wi_name = PL_Wi_Name) end),isnull((select top 1 isnull(userName,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select top 1 isnull(remarks,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select top 1 isnull(subfeedback,'') from ng_rlos_decisionHistory where wi_name=PL_Wi_Name),''),isnull((select top 1 convert(varchar,dateLastChanged,120)  from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select top 1  convert(varchar,dateLastChanged,120)  from NG_RLOS_GR_DECISION where workstepname='Disbursal_Checker' and decision in ('Approve and Exit','Approve and Post Disbursal','Approve and Takeover') and dec_wi_name=PL_Wi_Name order by dateLastChanged desc),'') as disb_Date,Final_Limit,'"+formObject.getWFWorkitemName()+"'  from NG_PL_EXTTABLE where CURR_WSNAME= 'Disbursed' and PL_Wi_Name in (select wi_name from ng_RLOS_EmpDetails where employer_name='"+formObject.getNGValue("EmploymentVerification_s2_loan_emp_name")+"') and CREATED_DATE between GETDATE()-730 and GETDATE()  union all select  Product, case when DATEDIFF(MONTH,( (select top 1  convert(varchar,dateLastChanged,120)  from NG_RLOS_GR_DECISION with(nolock) where workstepname='Disbursal' and decision ='Submit' and dec_wi_name=CC_Wi_Name order by dateLastChanged desc)),GETDATE())< 6 then '0-6 month' when DATEDIFF(MONTH,( (select top 1  convert(varchar,dateLastChanged,120)  from NG_RLOS_GR_DECISION with(nolock) where dec_wi_name=cc_Wi_name order by dateLastChanged desc)),GETDATE())<12 then'6-12 month' else '12-24 month' end  as month_diff,CC_Wi_Name,isnull(CRN,''),isnull(CIF_ID,''),CUSTOMERNAME,(select Employer_Name from ng_RLOS_EmpDetails where  wi_name=CC_Wi_Name),isnull((select case when EmirateVisa='--Select--' then '' else EmirateVisa end  from ng_RLOS_Customer where wi_name=cc_Wi_name),''),isnull((select TL_Number from ng_RLOS_EmpDetails where wi_name=cc_Wi_name),''),isnull((select OfficeNo from ng_RLOS_AltContactDetails where wi_name=cc_Wi_name),''),isnull((select LOS from ng_RLOS_EmpDetails where wi_name=cc_Wi_name),''),isnull((select top 1 description from ng_master_country where code=(select Nationality from ng_RLOS_Customer where wi_name=cc_Wi_name)),''),isnull((select yearsINUAE from ng_RLOS_Customer where wi_name=cc_Wi_name),''),isnull(DSA_Name,''),isnull(RM_Name,''),(select case when  (select top 1 description from ng_master_designation where code=(select desigstatus from ng_rlos_empdetails where wi_name=CC_Wi_Name)) is not null then (select top 1 description from ng_master_designation where code=(select desigstatus from ng_rlos_empdetails where wi_name = CC_Wi_Name))  else (select desigstatus from ng_rlos_Empdetails where wi_name = CC_Wi_Name) end),isnull((select top 1 isnull(userName,'') from NG_RLOS_GR_DECISION where dec_wi_name=cc_Wi_name and workstepName='FPU'),''),isnull((select top 1 isnull(remarks,'') from NG_RLOS_GR_DECISION where dec_wi_name=cc_Wi_name and workstepName='FPU'),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=cc_Wi_name and workstepName='FPU'),''),isnull((select top 1 isnull(subfeedback,'') from ng_rlos_decisionHistory where wi_name=cc_Wi_name),''),isnull((select  top 1  convert(varchar,dateLastChanged,120) from NG_RLOS_GR_DECISION where dec_wi_name=cc_Wi_name and workstepName='FPU'),''),isnull((select top 1  convert(varchar,dateLastChanged,120)  from NG_RLOS_GR_DECISION where dec_wi_name=cc_Wi_name order by dateLastChanged desc),'') as disb_Date,Final_Limit,'"+formObject.getWFWorkitemName()+"'  from NG_CC_EXTTABLE where CURR_WSNAME= 'Courier_Auto' and Employer_name='"+formObject.getNGValue("EmploymentVerification_s2_loan_emp_name")+"' and employmenttype='Salaried' and cc_Wi_name in (select wi_name from ng_RLOS_EmpDetails where Employer_name='"+formObject.getNGValue("EmploymentVerification_s2_loan_emp_name")+"') and CREATED_DATE between GETDATE()-730 and GETDATE()  union all select  Product, case when DATEDIFF(MONTH,( (select top 1  convert(varchar,dateLastChanged,120)  from NG_RLOS_GR_DECISION with(nolock) where workstepname='Disbursal' and decision ='Submit' and dec_wi_name=CC_Wi_Name order by dateLastChanged desc)),GETDATE())< 6 then '0-6 month' when DATEDIFF(MONTH,( (select top 1 isnull(dateLastChanged,'') from NG_RLOS_GR_DECISION with(nolock) where dec_wi_name=cc_Wi_name order by dateLastChanged desc)),GETDATE())<12 then'6-12 month' else '12-24 month' end  as month_diff,CC_Wi_Name,isnull(CRN,''),isnull(CIF_ID,''),CUSTOMERNAME,Employer_Name,isnull((select case when EmirateVisa='--Select--' then '' else EmirateVisa end  from ng_RLOS_Customer where wi_name=cc_Wi_name),''),isnull((select top 1 tradeliceneceNo from NG_RLOS_GR_CompanyDetails where comp_winame =cc_Wi_name and application_type='Secondary'),''),isnull((select OfficeNo from ng_RLOS_AltContactDetails where wi_name=cc_Wi_name),''),isnull((select LOS from ng_RLOS_EmpDetails where wi_name=cc_Wi_name),''),isnull((select top 1 description from ng_master_country where code=(select Nationality from ng_RLOS_Customer where wi_name=cc_Wi_name)),''),isnull((select yearsINUAE from ng_RLOS_Customer where wi_name=cc_Wi_name),''),isnull(DSA_Name,''),isnull(RM_Name,''),(select case when  (select top 1 description from ng_master_designation where code=(select desigstatus from ng_rlos_empdetails where wi_name=CC_Wi_Name)) is not null then (select top 1 description from ng_master_designation where code=(select desigstatus from ng_rlos_empdetails where wi_name = CC_Wi_Name))  else (select desigstatus from ng_rlos_Empdetails where wi_name = CC_Wi_Name) end),isnull((select top 1 isnull(userName,'') from NG_RLOS_GR_DECISION where dec_wi_name=cc_Wi_name and workstepName='FPU'),''),isnull((select top 1 isnull(remarks,'') from NG_RLOS_GR_DECISION where dec_wi_name=cc_Wi_name and workstepName='FPU'),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=cc_Wi_name and workstepName='FPU'),''),isnull((select top 1  isnull(subfeedback,'') from ng_rlos_decisionHistory where wi_name=cc_Wi_name),''),isnull((select top 1 convert(varchar,dateLastChanged,120) from NG_RLOS_GR_DECISION where dec_wi_name=cc_Wi_name and workstepName='FPU'),''),isnull((select top 1  convert(varchar,dateLastChanged,120)  from NG_RLOS_GR_DECISION where dec_wi_name=cc_Wi_name order by dateLastChanged desc),'') as disb_Date,Final_Limit,'"+formObject.getWFWorkitemName()+"'  from NG_CC_EXTTABLE where CURR_WSNAME= 'Courier_Auto' and Employer_name='"+formObject.getNGValue("EmploymentVerification_s2_loan_emp_name")+"' and employmenttype!='Salaried' and  cc_Wi_name in (select wi_name from ng_RLOS_EmpDetails ) and CREATED_DATE between GETDATE()-730 and GETDATE() order by disb_date desc";


	
				PersonalLoanS.mLogger.info( "Data to be added in Grid account Grid query 1: "+query1);

				List<List<String>> list_acc=formObject.getDataFromDataSource(query1);
				int count1 = 0;
				for(List<String> mylist : list_acc)
				{
					PersonalLoanS.mLogger.info( "Data to be added in Grid account Grid: "+mylist.toString());
					formObject.addItemFromList("cmplx_emp_ver_sp2_cmplx_loan_disb", mylist);
					count1++;
					if(count1 == 10)
					{
						break;
					}
				}
				
								//List modified to change 24 months constant to 0-6,6-12 and 12-24 months : bandana 
			/*	List<List<String>> list_acc_modified = new ArrayList<List<String>>();
				for(int i=0;i<list_acc.size();i++){
					List<String> onerow = list_acc.get(i);
					for (int j=0;j<onerow.size();j++){
						String timeFrame= "";
						String dte = onerow.get(21);
						if(dte==null || "null".equalsIgnoreCase(dte)){
							list_acc_modified.add(onerow);
						}else{
						PersonalLoanS.mLogger.info("date of disbursal"+ dte);
						PersonalLoanS.mLogger.info("disbursal date components"+ Integer.parseInt(dte.substring(0, 4))+ 
								Integer.parseInt(dte.substring(5, 7))+Integer.parseInt(dte.substring(8, 10)));
						
						LocalDate disbDate = new LocalDate(Integer.parseInt(dte.substring(0, 4)), 
								Integer.parseInt(dte.substring(5, 7)),Integer.parseInt(dte.substring(8,10)));
						LocalDate todatDate = LocalDate.now();
						
						int monthsinbetween = Months.monthsBetween(disbDate, todatDate).getMonths();
						PersonalLoanS.mLogger.info("monthsinbetween " +monthsinbetween);
						
						if (monthsinbetween<=6){
							timeFrame = "0-6 Months";
						}else if (monthsinbetween<=12 && monthsinbetween>6){
							timeFrame = "6-12 Months";
						}else{
							timeFrame = "12-24 Months";
						}
						onerow.set(1, timeFrame);
						list_acc_modified.add(onerow);
						}
					}
					
				}*/
				//hritik 29.6.21 PCASI 3499 - 3606
				if(("FPU".equalsIgnoreCase(formObject.getWFActivityName()) || "FCU".equalsIgnoreCase(formObject.getWFActivityName())))
				{
					PersonalLoanS.mLogger.info("Inside autopop plcomm employmentverf");
					formObject.setLocked("EmploymentVerification_s2_search_TL_number", false);
					formObject.setEnabled("EmploymentVerification_s2_search_TL_number", true);
				}
				//List modified and search TL number button enabled above 
			
				
				formObject.clear("cmplx_emp_ver_sp2_cmplx_loan_rej");
				//Deepak Query changed for PCAS-1260
				String query2="";
				//query2="select Product,'6 Months',CC_Wi_Name,isnull(CRN,''),CIF_ID,CUSTOMERNAME,Employer_Name,(select EmirateVisa from ng_RLOS_Customer where wi_name=cc_Wi_name),(select TL_Number from ng_RLOS_EmpDetails where wi_name=cc_Wi_name),(select OfficeNo from ng_RLOS_AltContactDetails where wi_name=cc_Wi_name),(select LOS from ng_RLOS_EmpDetails where wi_name=cc_Wi_name),(select Nationality from ng_RLOS_Customer where wi_name=cc_Wi_name),(select yearsINUAE from ng_RLOS_Customer where wi_name=cc_Wi_name),DSA_Name,RM_Name,(select isnull(designation,'') from ng_RLOS_EmpDetails where wi_name=cc_Wi_name),(select top 1 isnull(userName,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='FPU'),(select top 1 isnull(remarks,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='FPU'),(select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='FPU'),(select isnull(subfeedback,'') from ng_rlos_decisionHistory where wi_name=CC_Wi_Name),(select top 1 isnull(dateLastChanged,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='FPU'),(select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='Cad_Analyst2' order by dateLastChanged),(select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='CPV' order by dateLastChanged),(select top 1 isnull(dateLastChanged,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='Rejected_queue' order by dateLastChanged),'"+formObject.getWFWorkitemName()+"'  from NG_CC_EXTTABLE where CC_Wi_Name in (select wi_name from ng_RLOS_EmpDetails where EmpCode='"+formObject.getNGValue("cmplx_EmploymentDetails_EMpCode")+"' and wi_name in(select ProcessInstanceID from WFINSTRUMENTTABLE where ActivityId=22 and ProcessDefID=1 and EntryDATETIME between GETDATE()-180 and GETDATE())) union all select Product,'6 Months',PL_Wi_Name,CRN,CIF_ID,CUSTOMERNAME,Employer_Name,(select EmirateVisa from ng_RLOS_Customer where wi_name=PL_Wi_Name),(select TL_Number from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),(select OfficeNo from ng_RLOS_AltContactDetails where wi_name=PL_Wi_Name),(select LOS from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),(select Nationality from ng_RLOS_Customer where wi_name=PL_Wi_Name),(select yearsINUAE from ng_RLOS_Customer where wi_name=PL_Wi_Name),DSA_Name,RM_Name,(select designation from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),(select top 1 userName from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),(select top 1 remarks from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),(select top 1 Decision from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),(select subfeedback from ng_rlos_decisionHistory where wi_name=PL_Wi_Name),(select top 1 dateLastChanged from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),(select top 1 Decision from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='Cad_Analyst2' order by dateLastChanged),(select top 1 Decision from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='CPV' order by dateLastChanged),(select top 1 dateLastChanged from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='Reject_Queue' order by dateLastChanged),'"+formObject.getWFWorkitemName()+"'  from NG_PL_EXTTABLE where PL_Wi_Name in (select wi_name from ng_RLOS_EmpDetails where EmpCode='"+formObject.getNGValue("cmplx_EmploymentDetails_EMpCode")+"' and wi_name in(select ProcessInstanceID from WFINSTRUMENTTABLE where ActivityId=32 and ProcessDefID=19 and EntryDATETIME between GETDATE()-180 and GETDATE()))";


				query2="select Product,'6 Months',CC_Wi_Name,isnull(CRN,''),isnull(CIF_ID,''),CUSTOMERNAME,(select Employer_Name from ng_RLOS_EmpDetails where  wi_name=CC_Wi_Name),isnull((select  case when EmirateVisa='--Select--' then '' else EmirateVisa end   from ng_RLOS_Customer where wi_name=cc_Wi_name),''),isnull((select TL_Number from ng_RLOS_EmpDetails where wi_name=cc_Wi_name),''),(select OfficeNo from ng_RLOS_AltContactDetails where wi_name=cc_Wi_name),isnull((select LOS from ng_RLOS_EmpDetails where wi_name=cc_Wi_name),''),(select description from ng_master_country where code=( select  Nationality from ng_RLOS_Customer where wi_name=cc_Wi_name)),(select yearsINUAE from ng_RLOS_Customer where wi_name=cc_Wi_name),isnull(DSA_Name,''),isnull(RM_Name,''),(select case when  (select description from ng_master_designation where code=(select desigstatus from ng_rlos_empdetails where wi_name=CC_Wi_Name)) is not null then (select description from ng_master_designation where code=(select desigstatus from ng_rlos_empdetails where wi_name = CC_Wi_Name))  else (select desigstatus from ng_rlos_Empdetails where wi_name = CC_Wi_Name) end),isnull((select top 1 isnull(userName,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='FPU'),''),isnull((select top 1 isnull(remarks,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='FPU'),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='FPU'),''),isnull((select isnull(subfeedback,'') from ng_rlos_decisionHistory where wi_name=CC_Wi_Name),''),isnull(( select   convert(varchar,dateLastChanged,120)  from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName in('FPU','FPU')),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='Cad_Analyst2' order by dateLastChanged desc),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='CPV' order by dateLastChanged desc),''),(select top 1 isnull(dateLastChanged,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and decision='Reject' order by dateLastChanged desc) as rej_date,'"+formObject.getWFWorkitemName()+"'  from NG_CC_EXTTABLE where CURR_WSNAME in('Rejected_Application')  and employmenttype='Salaried' and CC_Wi_Name in (select wi_name from ng_RLOS_EmpDetails where Employer_name='"+formObject.getNGValue("EmploymentVerification_s2_loan_emp_name")+"' ) and CREATED_DATE between GETDATE()-180 and GETDATE() 						union all select Product,'6 Months',PL_Wi_Name,isnull(CRN,''),isnull(CIF_ID,''),CUSTOMERNAME,(select employer_name from ng_RLOS_empdetails where wi_name=PL_Wi_Name),(select case when EmirateVisa='--Select--' then '' else EmirateVisa end   from ng_RLOS_Customer where wi_name=PL_Wi_Name),isnull((select TL_Number from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),''),isnull((select OfficeNo from ng_RLOS_AltContactDetails where wi_name=PL_Wi_Name),''),isnull((select LOS from ng_RLOS_EmpDetails where wi_name=PL_Wi_Name),''),isnull((select description from ng_master_country where code=( select Nationality from ng_RLOS_Customer where wi_name=PL_Wi_Name)),''),isnull((select yearsINUAE from ng_RLOS_Customer where wi_name=PL_Wi_Name),''),isnull(DSA_Name,''),isnull(RM_Name,''),(select case when  (select description from ng_master_designation where code=(select desigstatus from ng_rlos_empdetails where wi_name=PL_Wi_Name)) is not null then (select description from ng_master_designation where code=(select desigstatus from ng_rlos_empdetails where wi_name = PL_Wi_Name))  else (select desigstatus from ng_rlos_Empdetails where wi_name = PL_Wi_Name) end),isnull((select top 1 userName from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select top 1 isnull(remarks,'')from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select top 1  Decision from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='FPU'),''),isnull((select subfeedback from ng_rlos_decisionHistory where wi_name=PL_Wi_Name),''),isnull((select  convert(varchar,dateLastChanged,120) from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName IN('FPU','FCU')),''),isnull((select top 1 Decision from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='Cad_Analyst2' order by dateLastChanged desc),''),isnull((select top 1 Decision from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and workstepName='CPV' order by dateLastChanged desc),''),isnull((select top 1 convert(varchar,dateLastChanged,120) from NG_RLOS_GR_DECISION where dec_wi_name=PL_Wi_Name and decision='Reject' order by dateLastChanged desc),'') as rej_dec,'"+formObject.getWFWorkitemName()+"'  from NG_PL_EXTTABLE where CURR_WSNAME in ('Rejected_Application') and PL_Wi_Name in (select wi_name from ng_RLOS_EmpDetails where Employer_name='"+formObject.getNGValue("EmploymentVerification_s2_loan_emp_name")+"') and CREATED_DATE between GETDATE()-180 and GETDATE() union all select Product,'6 Months',CC_Wi_Name,isnull(CRN,''),isnull(CIF_ID,''),CUSTOMERNAME,Employer_Name,isnull((select  case when EmirateVisa='--Select--' then '' else EmirateVisa end   from ng_RLOS_Customer where wi_name=cc_Wi_name),''),isnull((select top 1 tradeliceneceNo from NG_RLOS_GR_CompanyDetails where comp_winame =cc_Wi_name and application_type='Secondary'),''),(select OfficeNo from ng_RLOS_AltContactDetails where wi_name=cc_Wi_name),isnull((select LOS from ng_RLOS_EmpDetails where wi_name=cc_Wi_name),''),(select description from ng_master_country where code=( select  Nationality from ng_RLOS_Customer where wi_name=cc_Wi_name)),(select yearsINUAE from ng_RLOS_Customer where wi_name=cc_Wi_name),isnull(DSA_Name,''),isnull(RM_Name,''),(select case when  (select description from ng_master_designation where code=(select desigstatus from ng_rlos_empdetails where wi_name=CC_Wi_Name)) is not null then (select description from ng_master_designation where code=(select desigstatus from ng_rlos_empdetails where wi_name = CC_Wi_Name))  else (select desigstatus from ng_rlos_Empdetails where wi_name = CC_Wi_Name) end),isnull((select top 1 isnull(userName,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='FPU'),''),isnull((select top 1 isnull(remarks,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='FPU'),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='FPU'),''),isnull((select isnull(subfeedback,'') from ng_rlos_decisionHistory where wi_name=CC_Wi_Name),''),isnull(( select   convert(varchar,dateLastChanged,120)  from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName in('FPU','FPU')),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='Cad_Analyst2' order by dateLastChanged desc),''),isnull((select top 1 isnull(Decision,'') from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and workstepName='CPV' order by dateLastChanged desc),''),(select top 1 convert(varchar,dateLastChanged,120) from NG_RLOS_GR_DECISION where dec_wi_name=CC_Wi_Name and decision='Reject' order by dateLastChanged desc) as rej_date,'"+formObject.getWFWorkitemName()+"'  from NG_CC_EXTTABLE where CURR_WSNAME in ('Rejected_Application') and employmenttype!='Salaried' and  Employer_name='"+formObject.getNGValue("EmploymentVerification_s2_loan_emp_name")+"' and  CC_Wi_Name in (select wi_name from ng_RLOS_EmpDetails ) and CREATED_DATE between GETDATE()-180 and GETDATE() order by rej_date desc";



	
				PersonalLoanS.mLogger.info( "Data to be added in Grid account Grid query 1: "+query2);

				List<List<String>> list_acc_rej=formObject.getDataFromDataSource(query2);
				int count = 0;
				for(List<String> mylist : list_acc_rej)
				{
					PersonalLoanS.mLogger.info( "Data to be added in Grid account Grid: "+mylist.toString());
					formObject.addItemFromList("cmplx_emp_ver_sp2_cmplx_loan_rej", mylist);
					count++;
					if(count == 10)
					{
						break;
					}
				}
			}
			catch(Exception ex)
			{
				printException(ex);
			}
			formObject.setLocked("cmplx_emp_ver_sp2_FPU_remarks_comp_mismatch",false);
			formObject.setEnabled("cmplx_emp_ver_sp2_FPU_remarks_comp_mismatch", true);
		}
		//Deepak method added to load supplymentry card limit
		public Map<String,String> loadSupplementCardLimit(){
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			Map<String,String> SupplementCardMap = new HashMap<String,String>();
			
			try{
				int crngridRowCount = formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid");
				for(int girdCount=0;girdCount<crngridRowCount;girdCount++){
					String passport = formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",girdCount,3);
					String cardproduct = formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",girdCount,30);
					String final_Limit = formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",girdCount,28);
					if(passport!=null && cardproduct!=null){
						SupplementCardMap.put(passport+"-"+cardproduct, final_Limit);
					}
				}
			}
			catch(Exception e){
				PersonalLoanS.mLogger.info("CC Common Exception Inside loadSupplementCardLimit()"+e.getMessage());
			}
			return SupplementCardMap;
		}
		
		public void LoadDecisionSubReason(FormReference formObject){
			String Workstep=formObject.getWFActivityName();
			if(NGFUserResourceMgr_PL.getGlobalVar("PL_SUB_REFER_WS").contains(Workstep)){
				PersonalLoanS.mLogger.info("DecisionSubReason Load desison Drop down: "+Workstep );
				formObject.setLocked("DecisionHistory_DecisionSubReason", false);
				if(Workstep.contains("DDVT")){
					Workstep="DDVT";
				} else if (Workstep.contains("Disbursal")){
					Workstep="Disbursal";
				}
				if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision")) && !"--Select--".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_Decision"))){
					if("ToTeam".equalsIgnoreCase(formObject.getWFActivityName()) && !"".equalsIgnoreCase(formObject.getNGValue("DecisionHistory_DecisionReasonCode"))){
						LoadPickList1("DecisionHistory_DecisionSubReason", "select description from ng_MASTER_DecisionSubReason with (nolock) where workstep='"+Workstep+"' and decision='"+formObject.getNGValue("cmplx_Decision_Decision")+"' and PendingReason='"+formObject.getNGValue("DecisionHistory_DecisionReasonCode")+"' order by code");
					}else {
						LoadPickList1("DecisionHistory_DecisionSubReason", "select description from ng_MASTER_DecisionSubReason with (nolock) where workstep='"+Workstep+"' and decision='"+formObject.getNGValue("cmplx_Decision_Decision")+"' order by code");
					}
				}else{
					LoadPickList1("DecisionHistory_DecisionSubReason", "select description from ng_MASTER_DecisionSubReason with (nolock) where workstep='"+Workstep+"'  order by code");
				}
			} else {
				formObject.setLocked("DecisionHistory_DecisionSubReason", true);

			}
		}
		
		public String Card_profiles(String Type, String Card_product)
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String Profile="";
			String query1="select TOP 1 code,convert(varchar, Description) as description from "+Type+" with (nolock) where isActive='Y' and Product = '"+Card_product+"'";
			List<List<String>> result = formObject.getNGDataFromDataCache(query1);
			//CreditCard.mLogger.info("card product result"+result);
			if(result!=null && !result.isEmpty())  //if(result!=null && result.size()>0)
			{
				//CreditCard.mLogger.info("result"+result.get(0).get(0));
				//cmplx_EmploymentDetails_CurrEmployer
				Profile=result.get(0).get(0);
			}
			else{
				 Profile="";
			}
			return Profile;
		}
		protected void calculateNetPayout(FormReference formObject) {
			try{
				PersonalLoanS.mLogger.info("inside method calculateNetPayout");
				int prd_rowCount=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
				PersonalLoanS.mLogger.info("prodct row count "+prd_rowCount);
				if(prd_rowCount>0){
					String application_type = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 4);
				PersonalLoanS.mLogger.info(application_type);
				if(application_type.contains("TOP")){
						String outBal = "select liab.TotalOutstandingAmt from ng_RLOS_CUSTEXPOSE_LoanDetails liab " +
						" with (nolock) where liab.child_wi = '"+formObject.getWFWorkitemName()+"' and " +
						"liab.Settlement_Flag = 'true' and liab.limit_increase = 'true'"; //increase_limit conditioned replaced by settlement flag as per PCAS1-3093 
						PersonalLoanS.mLogger.info("net payout query:"+outBal);
						List<List<String>> record = formObject.getNGDataFromDataCache(outBal);
						PersonalLoanS.mLogger.info("outstanding list is: "+record);
						if(record.size()>0){
							float bal=0;
							float lim=0;
							int gridsize = record.size();
							for (int i=0;i<gridsize;i++){
								String outBalance = record.get(i).get(0);
								if (outBalance!=null && !outBalance.equalsIgnoreCase("null"))
									bal = bal+Float.parseFloat(outBalance);
							}	
							lim = Float.parseFloat(formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
							PersonalLoanS.mLogger.info("lim is:"+lim);
							PersonalLoanS.mLogger.info("bal is:"+bal);

							formObject.setNGValue("cmplx_EligibilityAndProductInfo_NetPayout", lim-bal);
						}
						else{
							formObject.setNGValue("cmplx_EligibilityAndProductInfo_NetPayout", formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
						}
					}
				}
			}
			catch(Exception ex){
				PersonalLoanS.mLogger.info("Exception in calculating net payout: "+ex);
			}
}		
}