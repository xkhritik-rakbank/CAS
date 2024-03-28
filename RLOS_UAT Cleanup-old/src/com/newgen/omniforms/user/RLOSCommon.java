/*
 * Change History:
 * Deepak 31Aug2017: Chnages done to save blacklist code, date Negated code and date in DB: comment:Chnages done to add blacklist reasone code, data Negated Reasoncode and date start  
 * */
package com.newgen.omniforms.user;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.IRepeater;
import com.newgen.omniforms.component.PickList;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.excp.CustomExceptionHandler;
import com.newgen.omniforms.skutil.*;
import java.util.List;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Calendar;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import java.text.DecimalFormat;

public class RLOSCommon extends Common implements Serializable
{
	HashMap<String,String> hm= new HashMap<String,String>(); // not a nullable HashMap
	String popupFlag="";
	
	
	public String getRMName(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		RLOS.mLogger.info( "Inside getRMName()"); 
		String query="Select RMId from ng_master_RMname where userid='"+formObject.getUserName()+"'";
		RLOS.mLogger.info( "Query to fetch RMName:"+query); 
		List<List<String>> result=formObject.getDataFromDataSource(query);
		RLOS.mLogger.info( "result of fetch RMname query: "+result); 

		if(result==null || result.isEmpty()){
			return "";
		}
		else{
			return result.get(0).get(0);
		}
	}
	public String getYearsDifference(FormReference formObject,String controlName, String controlName2) {
		int MON;
		int Year;
		String age="";
		String DOB=formObject.getNGValue(controlName);
		RLOS.mLogger.info(" Inside age + "+DOB);
		String Date2= formObject.getNGValue(controlName2);
	if (DOB!=null){	
		String[] Dob=DOB.split("/");
		String[] CurreDate=Date2.split("/");
		int monthbirthDate=Integer.parseInt(Dob[1]);
		int monthcurrDate=Integer.parseInt(CurreDate[1]);
		int YearbirthDate=Integer.parseInt(Dob[2]);
		int yearcurrDate=Integer.parseInt(CurreDate[2]);
			if (monthcurrDate<monthbirthDate){
			yearcurrDate=yearcurrDate-1;
			Year=yearcurrDate-YearbirthDate;
			MON=monthcurrDate-monthbirthDate;
			MON=12+MON;
			if ((MON==10)||(MON==11)){
				age=Year+"."+MON;}
				else{
					age=Year+".0"+MON;
				}
			
			}
		else if (monthcurrDate>monthbirthDate){
			Year=yearcurrDate-YearbirthDate;
			MON=monthcurrDate-monthbirthDate;
			if ((MON==10)||(MON==11)){
				age=Year+"."+MON;}
			else{
				age=Year+".0"+MON;
				}
		}
		else {
			Year=yearcurrDate-YearbirthDate;
			if(Year<10){
			age="0"+Year+".00";
			}
			else{
				age=Year+".00";
			}
					
		}
	}
	return age;
}
	public void getAge(String dateBirth,String controlName){
	
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			RLOS.mLogger.info( "Inside getAge(): "); 
			Calendar dob = Calendar.getInstance();
			Calendar today = Calendar.getInstance();
			String parts[];
			if (dateBirth.contains("/")){
				 parts = dateBirth.split("/");				
				dob.set(Integer.parseInt(parts[2]), Integer.parseInt(parts[1])-1,Integer.parseInt(parts[0])); 
			}
			
			else if(dateBirth.contains("-")) {
				 parts = dateBirth.split("-");			
				dob.set(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])-1,Integer.parseInt(parts[2])); 
			}
			
			Integer age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
			int month=today.get(Calendar.MONTH) - dob.get(Calendar.MONTH);
			if (month < 0){
				age--;
				month= 12-dob.get(Calendar.MONTH) + today.get(Calendar.MONTH);
			}
			else if(month == 0 && today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)) {
				age--;
				month= 11-dob.get(Calendar.MONTH) + today.get(Calendar.DAY_OF_MONTH);//If month is same as current no need to count it
			}
			if(today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)){
				month=month-1;
			}	
			formObject.setNGValue(controlName,(age+"."+month).toString(),false); 
			
		}
		catch(Exception e)
		{
			RLOS.logException(e);
			try
			{ 
				throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));
			}
			finally
			{
				hm.clear();
			}
		}

	}
	
	public void loadPicklistCustomer()  
	{
		RLOS.mLogger.info( "Inside loadPicklistCustomer: ");
		LoadPickList("cmplx_Customer_Nationality", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
		LoadPickList("cmplx_Customer_Title", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_title with (nolock) order by code");
		LoadPickList("cmplx_Customer_SecNationality", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
		LoadPickList("cmplx_Customer_CustomerCategory", "select '--Select--'as description,'' as code union select convert(varchar, Description),code from NG_MASTER_CustomerCategory with (nolock) order by Code");
		LoadPickList("cmplx_Customer_COuntryOFResidence", " select convert(varchar, Description),code from ng_MASTER_Country order by Code");	
		LoadPickList("cmplx_Customer_MAritalStatus", "select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_Maritalstatus order by Code");
		LoadPickList("ref_Relationship", "select convert(varchar, Description),code from NG_MASTER_Relationship union select '--Select--','' order by Code");
		LoadPickList("cmplx_Customer_EmirateOfResidence", "select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_emirate order by Code");
		LoadPickList("cmplx_Customer_EMirateOfVisa", "select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_emirate order by Code");
	}

	public void setcustomer_enable(){
		RLOS.mLogger.info( "Inside setCustomer enable function");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setLocked("cmplx_Customer_FIrstNAme", false);
		formObject.setLocked("cmplx_Customer_MiddleName", false);
		formObject.setLocked("cmplx_Customer_LAstNAme", false);
		formObject.setLocked("cmplx_Customer_PAssportNo", false);
		//formObject.setLocked("cmplx_Customer_DOb", false);
		formObject.setLocked("cmplx_Customer_Nationality", false);
		formObject.setLocked("cmplx_Customer_Title", false);
		formObject.setLocked("cmplx_Customer_ResidentNonResident", false);
		formObject.setLocked("cmplx_Customer_gender", false);
		formObject.setLocked("cmplx_Customer_MotherName", false);
		formObject.setLocked("cmplx_Customer_VisaNo", false);
		formObject.setLocked("cmplx_Customer_MAritalStatus", false);
		formObject.setLocked("cmplx_Customer_COuntryOFResidence", false);
		formObject.setLocked("cmplx_Customer_VIsaExpiry",false);
		formObject.setEnabled("cmplx_Customer_SecNAtionApplicable", true);
		formObject.setEnabled("cmplx_Customer_IdIssueDate", true);
		//formObject.setLocked("cmplx_Customer_IdIssueDate", false);
		formObject.setEnabled("cmplx_Customer_EmirateIDExpiry", true);
		//formObject.setLocked("cmplx_Customer_EmirateIDExpiry", false);
		if("No".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_SecNAtionApplicable")))
			formObject.setLocked("cmplx_Customer_SecNationality", true);
		else
			formObject.setLocked("cmplx_Customer_SecNationality", false);
		formObject.setLocked("cmplx_Customer_EMirateOfVisa", false);
		formObject.setLocked("cmplx_Customer_EmirateOfResidence", false);
		formObject.setLocked("cmplx_Customer_yearsInUAE", false);
		formObject.setLocked("cmplx_Customer_CustomerCategory", false);
		//formObject.setLocked("cmplx_Customer_GCCNational", false);
		formObject.setEnabled("cmplx_Customer_VIPFlag", true);
		//formObject.setLocked("cmplx_Customer_PassPortExpiry", false);
		//formObject.setLocked("cmplx_Customer_VIsaExpiry", false);
		
		//added By Akshay

		String  GCC="BH,IQ,KW,OM,QA,SA,AE";
		if(GCC.contains(formObject.getNGValue("cmplx_Customer_Nationality")))
		{
			RLOS.mLogger.info("Inside GCC for Nationality");
			formObject.setNGValue("cmplx_Customer_GCCNational","Yes");
		}
		else
		{
			formObject.setNGValue("cmplx_Customer_GCCNational","No");
		}
		//ended By akshay	

	}
	
	public static String printException(Exception e){
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String exception = sw.toString();
		return exception;
		
	}


	public void loadPicklistProduct(String ReqProd)
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		RLOS.mLogger.info( "Inside loadPicklistProduct$"+ReqProd+"$");
		String ProdType=formObject.getNGValue("Product_type");

		if("Personal Loan".equalsIgnoreCase(ReqProd)){
			RLOS.mLogger.info( "Inside equalsIgnoreCase()"); 
			formObject.setVisible("Scheme", true);
			formObject.setLeft("Product_Label3", 555);
			formObject.setLeft("Scheme", 555);
			formObject.clear("SubProd");
			formObject.setNGValue("SubProd", "--Select--");
			formObject.clear("Scheme");
			formObject.setNGValue("Scheme", "--Select--");
			LoadPickList("SubProd", " select '--Select--' as description,'' as code union select convert(varchar(50),description),code  from ng_MASTER_SubProduct_PL   where WorkstepName = '"+formObject.getWFActivityName()+"' order by code");
			//LoadPickList("SubProd", "select '--Select--' union select convert(varchar(50),description) from ng_MASTER_SubProduct_PL with (nolock) where workstepName='"+formObject.getWFActivityName()+"'");
			LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar,SchemeDesc),SCHEMEID from ng_MASTER_Scheme with (nolock) where SchemeDesc like 'P_%' order by SCHEMEID");
			//added by abhishek
			RLOS.mLogger.info( "Inside equalsIgnoreCase()"+ReqProd);
			LoadPickList("EmpType", " select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_PRD_EMPTYPE where SubProduct = '"+ReqProd+"' order by code");

		}
		else if("Credit Card".equalsIgnoreCase(ReqProd))
		{
			formObject.setVisible("CardProd", true);
			formObject.clear("CardProd");
			formObject.clear("SubProd");
			formObject.setNGValue("SubProd", "--Select--");
			if("Telesales_Init".equals(formObject.getWFActivityName())){
				LoadPickList("SubProd", " select '--Select--' as description,'' as code union select convert(varchar(50),description),code  from ng_master_Subproduct_CC  where code='BTC' or code='IM' order by code");

			}
			else{
				LoadPickList("SubProd", " select '--Select--' as description,'' as code union select  convert(varchar,description),code from ng_master_Subproduct_CC with (nolock) order by code");
			}
			//added by abhishek
			RLOS.mLogger.info( "Inside equalsIgnoreCase()"+ReqProd);

			LoadPickList("EmpType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_PRD_EMPTYPE where SubProduct = '"+ReqProd+"' order by code");


			if("Islamic".equalsIgnoreCase(ProdType))
				LoadPickList("CardProd", "select '--Select--','' as code union select convert(varchar,description),code from ng_MASTER_CardProduct with (nolock) where code like '%AMAL%' order by code");
			else
				LoadPickList("CardProd", "select '--Select--','' as code union select convert(varchar,description),code from ng_MASTER_CardProduct with (nolock) where code NOT like '%AMAL%' order by code");

			formObject.setVisible("Scheme", false);
		}
		else{
			formObject.clear("SubProd");
			formObject.setNGValue("SubProd", "--Select--");
		}
	}

	public void loadPicklist_Address()
	{
		RLOS.mLogger.info( "Inside loadPicklist_Address: "); 
		//	LoadPickList("addtype", "select '--Select--' as description  union select convert(varchar, description)  from NG_MASTER_AddressType with (nolock) order by description desc");
		LoadPickList("addtype", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_AddressType with (nolock)  where isActive='Y' order by code");
		LoadPickList("city", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_city with (nolock)  where isActive='Y' order by code");
		LoadPickList("state", "select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_emirate order by Code");
		//LoadPickList("city", "select '--Select--','' as code union select convert(varchar, description),code from NG_MASTER_city with (nolock) order by code");
		//LoadPickList("state", "select '--Select--','' as code union select convert(varchar, description),code from NG_MASTER_state with (nolock) order by code");
		LoadPickList("country", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock)  where isActive='Y' order by Code");
	}

	public void loadPicklist_AuthSign()
	{
		RLOS.mLogger.info( "Inside loadPicklist_AuthSign: "); 
		//	LoadPickList("addtype", "select '--Select--' as description  union select convert(varchar, description)  from NG_MASTER_AddressType with (nolock) order by description desc");
		LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_Country with (nolock) order by code");
		LoadPickList("AuthorisedSignDetails_Status", "select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");
		LoadPickList("Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
		LoadPickList("DesignationAsPerVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
		}
	
	public void loadPicklist_ServiceRequest()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		RLOS.mLogger.info( "Inside loadPicklist_ServiceRequest: "); 
		LoadPickList("transtype", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_TransactionType with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("transferMode", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_TransferMode with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("dispatchChannel", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_DispatchChannel with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("marketingCode", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_MarketingCode with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("sourceCode", "select Branch , SOL_ID,0 as sno from NG_MASTER_SourceCode where userid = '"+formObject.getNGValue("lbl_user_name_val")+"'  union select distinct Branch,SOL_ID,1 as sno from NG_MASTER_SourceCode where Branch !='' and SOL_ID !='' order by sno");
		LoadPickList("AppStatus", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_ApplicationStatus with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("approvalCode", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_ApprovalCode with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("chequeStatus", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_MChequeStatus with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("cmplx_CC_Loan_DDSMode", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_DDSMode with (nolock) where IsActive = 'Y' order by code ");
		LoadPickList("cmplx_CC_Loan_AccType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_AccountType with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("cmplx_CC_Loan_DDSBankAName", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_BankName with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("cmplx_CC_Loan_ModeOfSI", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_ModeOfSI with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("cmplx_CC_Loan_VPSPAckage", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_VPSPackage with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("cmplx_CC_Loan_VPSSourceCode", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_sourceCode with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("cmplx_CC_Loan_StartMonth", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_Month with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("cmplx_CC_Loan_HoldType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_HoldType with (nolock) where IsActive = 'Y' order by code");
		
		//cmplx_CC_Loan_HoldType
	}



public void  setALOCListed(){
	FormReference formObject = FormContext.getCurrentInstance().getFormReference();
	RLOS.mLogger.info( "Inside setALOCListed()");
	String NewEmployer=formObject.getNGValue("cmplx_EmploymentDetails_Others");
	String IncInCC=formObject.getNGValue("cmplx_EmploymentDetails_IncInCC");
	String INcInPL=formObject.getNGValue("cmplx_EmploymentDetails_IncInPL");
	String subprod=formObject.getNGValue("PrimaryProduct");
	
	if("Personal Loan".equalsIgnoreCase(subprod) && "true".equals(NewEmployer) && ("false".equals(IncInCC) || "false".equals(INcInPL)))
	{
		formObject.setNGValue("cmplx_EmploymentDetails_EmpStatusPL", "CN");
		formObject.setNGValue("cmplx_EmploymentDetails_EmpStatusCC", "CN");
		formObject.setLocked("cmplx_EmploymentDetails_EmpStatusCC", true);
	}
	
	else if("Credit Card".equalsIgnoreCase(subprod) && "true".equals(NewEmployer) && ("false".equals(IncInCC) || "false".equals(INcInPL)))
	{
		formObject.setNGValue("cmplx_EmploymentDetails_EmpStatusPL", "CN");
		formObject.setNGValue("cmplx_EmploymentDetails_EmpStatusCC", "CN");
		formObject.setLocked("cmplx_EmploymentDetails_EmpStatusPL", true);
	}
	
	else{
		if("true".equals(NewEmployer)){
			formObject.setNGValue("cmplx_EmploymentDetails_EmpStatusPL", "");
			formObject.setNGValue("cmplx_EmploymentDetails_EmpStatusCC", "");
			formObject.setLocked("cmplx_EmploymentDetails_EmpStatusPL", false);
			formObject.setLocked("cmplx_EmploymentDetails_EmpStatusCC", false);
		}
	}
}

	public boolean Address_Validate()
	{
		boolean flag_addressType=false;
		RLOS.mLogger.info( "Inside Addess_Validate");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String AddType=formObject.getNGValue("addtype");
		int n=formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
		if(n>0)
		{
			for(int i=0;i<n;i++){
				String mystring=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,0);
				RLOS.mLogger.info("mystring:$"+mystring+"$");

				if("Permanent".equalsIgnoreCase(mystring) && AddType.equalsIgnoreCase(mystring)){
					RLOS.mLogger.info("inside validator");
					throw new ValidatorException(new FacesMessage("Cannot add 2 permanent addresses","addtype"));

				}

				else
					flag_addressType=true;
			}		
		}			

		else
			flag_addressType=true;				

		return flag_addressType;	
	}

	public void loadPicklist3()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String query="select '--Select--' union select  convert(varchar,Decision) from NG_MASTER_Decision with (nolock) where ProcessName='RLOS' and workstepName='"+formObject.getWFActivityName()+"'";
		RLOS.mLogger.info( "Inside loadpicklist3: Query is:  "+query); 
		LoadPickList("cmplx_DecisionHistory_Decision", query);
		LoadPickList("cmplx_DecisionHistory_DecisionReasonCode", "select '--Select--' as description,'' as code union select  convert(varchar, description),code from ng_MASTER_RejectReasons with (nolock)  where isActive='Y' order by code");
	}

	public void loadPicklist4()    
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		RLOS.mLogger.info( "Inside loadpicklist4:");
		String reqProd = formObject.getNGValue("PrimaryProduct");
		String appCategory = formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg");
		LoadPickList("cmplx_EmploymentDetails_ApplicationCateg", "select '--Select--' union select  convert(varchar,description) from NG_MASTER_ApplicatonCategory with (nolock)");
		String subproduct = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2);
		if("Personal Loan".equalsIgnoreCase(reqProd)){
			if(appCategory!=null && "BAU".equalsIgnoreCase(appCategory)){
				LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and BAU='Y' and (product = 'PL' or product='B') order by code");
			}
			else if(appCategory!=null &&  "Surrogate".equalsIgnoreCase(appCategory)){
				LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and Surrogate='Y' and (product = 'PL' or product='B') order by code");
			}
			else{
			LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and (product = 'PL' or product='B') order by code");
			}
		}
		else if("Credit Card".equalsIgnoreCase(reqProd)){
			if(appCategory!=null &&  "BAU".equalsIgnoreCase(appCategory)){
				LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and BAU='Y' and (product = 'CC' or product='B') order by code");
			}
			else if(appCategory!=null &&  "Surrogate".equalsIgnoreCase(appCategory)){
				LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and Surrogate='Y' and (product = 'CC' or product='B') order by code");
			}
			else{
			LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and (product = 'CC' or product='B') order by code");	
			}
		}
		//LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_Designation", "select '--Select--' as description,'' as code union select  convert(varchar, description),code from NG_MASTER_Designation with (nolock)  where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_DesigVisa", "select '--Select--' as description,'' as code union select  convert(varchar, description),code from NG_MASTER_Designation with (nolock)  where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_Emp_Type", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_EmploymentType with (nolock)  where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_EmpStatus", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_EmploymentStatus with (nolock)  where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_EmirateOfWork", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_emirate with (nolock)  where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_HeadOfficeEmirate", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_emirate with (nolock)  where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_tenancntrct", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_emirate with (nolock) where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_EmpIndusSector", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_IndustrySector with (nolock)  where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_EmpContractType", "select '--Select--' union select convert(varchar, description) from NG_MASTER_EmpContractType with (nolock)  where isActive='Y'");
		LoadPickList("cmplx_EmploymentDetails_IndusSeg", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_IndustrySegment  where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_channelcode","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_ChannelCode where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_EmpStatusPL","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_EmployerStatusPL where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_EmpStatusCC","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_EmployerStatusCC where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_FreezoneName","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_freezoneName where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_CurrEmployer","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_EmployerCategory_PL where isActive='Y' order by code"); //Only description will go now, code removed for dectech
	}




	public void IMFields_Employment()
	{
		RLOS.mLogger.info( "Inside IMFields_Employment:"); 

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(n>0){
			for(int i=0;i<n;i++){
				if("Instant Money".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2))){
					formObject.setLocked("cmplx_EmploymentDetails_EmpName",false); 
					formObject.setLocked("cmplx_EmploymentDetails_EmpIndus",false); 
					formObject.setLocked("cmplx_EmploymentDetails_LOS",false); 
					formObject.setLocked("cmplx_EmploymentDetails_Designation",false);
					formObject.setLocked("cmplx_EmploymentDetails_DesigVisa",false); 
					formObject.setLocked("cmplx_EmploymentDetails_JobConfirmed",false); 
					break;
				}
			}
		}
	}

	public void BTCFields_Employment()
	{
		RLOS.mLogger.info( "Inside BTCFields_Employment:"); 

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(n>0){
			for(int i=0;i<n;i++){
				if("Business titanium Card".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2))){
					formObject.setEnabled("cmplx_EmploymentDetails_EmpName",false); 
					formObject.setEnabled("cmplx_EmploymentDetails_EmpIndus",false); 
					formObject.setEnabled("cmplx_EmploymentDetails_Designation",false);
					formObject.setEnabled("cmplx_EmploymentDetails_DesigVisa",false); 
					formObject.setEnabled("cmplx_EmploymentDetails_JobConfirmed",false); 
					formObject.setEnabled("cmplx_IncomeDetails_Accomodation",false); 
					break;
				}
			}	
		}
	}

	public void LimitIncreaseFields_Employment()
	{
		RLOS.mLogger.info( "Inside LimitIncreaseFields_Employment:"); 

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(n>0){
			for(int i=0;i<n;i++){ 
				if("Limit Increase".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2))){
					formObject.setEnabled("cmplx_EmploymentDetails_EmpName",true); 
					formObject.setEnabled("cmplx_EmploymentDetails_EmpIndus",true); 
					formObject.setEnabled("cmplx_EmploymentDetails_LOS",true); 
					formObject.setEnabled("cmplx_EmploymentDetails_Designation",true);
					formObject.setEnabled("cmplx_EmploymentDetails_DesigVisa",true); 
					formObject.setEnabled("cmplx_EmploymentDetails_JobConfirmed",true); 
					formObject.setEnabled("cmplx_IncomeDetails_Accomodation",true); 
					break;
				}
			}
		}
	}


	public void ProductUpgrade_Employment()
	{
		RLOS.mLogger.info( "Inside ProductUpgrade_Employment:"); 

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(n>0){
			for(int i=0;i<n;i++){ 
				if("Product Upgrade".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2))){
					formObject.setEnabled("cmplx_EmploymentDetails_EmpName",true); 
					formObject.setEnabled("cmplx_EmploymentDetails_EmpIndus",true); 
					formObject.setEnabled("cmplx_EmploymentDetails_LOS",true); 
					formObject.setEnabled("cmplx_EmploymentDetails_Designation",true);
					formObject.setEnabled("cmplx_EmploymentDetails_DesigVisa",true); 
					formObject.setEnabled("cmplx_EmploymentDetails_JobConfirmed",true); 
					formObject.setEnabled("cmplx_IncomeDetails_Accomodation",true); 
				}
			}
		}
	}

	public void Fields_ApplicationType_Employment()
	{
		RLOS.mLogger.info( "Inside Fields_ApplicationType_Employment:"); 

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
				if("RESCE".equalsIgnoreCase(formObject.getNGValue("Application_Type")) || "RESCN".equalsIgnoreCase(formObject.getNGValue("Application_Type"))){
					formObject.setVisible("EMploymentDetails_Label36",true); 
					formObject.setVisible("cmplx_EmploymentDetails_channelcode",true); 
				}
				else{
					formObject.setVisible("EMploymentDetails_Label36",false); 
					formObject.setVisible("cmplx_EmploymentDetails_channelcode",false); 
				}
	}

	//added by abhishek as per RLOS FSD for notepad details load add modify delete button
		
		public void notepad_load(){
				FormReference formObject = FormContext.getCurrentInstance().getFormReference();
				String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
				 Date date = new Date();
					String modifiedDate= new SimpleDateFormat("dd/MM/yyyy").format(date);
				// SKLogger_CC.writeLog("CCyash ", "Activity name is:" + sActivityName);
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
				formObject.setLocked("NotepadDetails_inscompletion",true);
				formObject.setLocked("NotepadDetails_ActuserRemarks",true);
				formObject.setTop("NotepadDetails_SaveButton",400);
		}
			
		public void Notepad_add(){
				FormReference formObject = FormContext.getCurrentInstance().getFormReference();
				String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
				
				//SKLogger_CC.writeLog("Notepad details username values@@@@",""+formObject.getUserId()+"-"+formObject.getUserName());
				//formObject.setNGValue("NotepadDetails_Actusername",formObject.getUserId()+"-"+formObject.getUserName());
		        formObject.setNGValue("NotepadDetails_user",formObject.getUserId()+"-"+formObject.getUserName(),false);		    	
				formObject.setNGValue("NotepadDetails_insqueue",sActivityName,false);
				// added by abhishek as per CC FSD
				Date date = new Date();
				String modifiedDate= new SimpleDateFormat("dd/MM/yyyy").format(date);
				formObject.setNGValue("NotepadDetails_noteDate",modifiedDate,false);
				//formObject.setNGValue("NotepadDetails_Actdate",modifiedDate);
				
				// to make frame in Add state
				formObject.setLocked("NotepadDetails_noteDate",true);
				formObject.setLocked("NotepadDetails_Actusername",true);
				formObject.setLocked("NotepadDetails_user",true);
				formObject.setLocked("NotepadDetails_insqueue",true);
				formObject.setLocked("NotepadDetails_Actdate",true);
				formObject.setLocked("NotepadDetails_Actdate",true);
				//formObject.setLocked("NotepadDetails_inscompletion",true);
				formObject.setLocked("NotepadDetails_ActuserRemarks",true);
				
				formObject.setLocked("NotepadDetails_notedesc",false);
				formObject.setLocked("NotepadDetails_notecode",true);
				formObject.setLocked("NotepadDetails_notedetails",false);
			}
				
			public void Notepad_modify(){
				// added by abhishek as per RLOS FSD
				FormReference formObject = FormContext.getCurrentInstance().getFormReference();
				 String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
				 Date date = new Date();
					String modifiedDate= new SimpleDateFormat("dd/MM/yyyy").format(date);
				// SKLogger_CC.writeLog("CCyash ", "Activity name is:" + sActivityName);
		        //formObject.setNGValue("NotepadDetails_Actusername",formObject.getUserId()+"-"+formObject.getUserName());
		        formObject.setNGValue("NotepadDetails_user",formObject.getUserId()+"-"+formObject.getUserName(),false);
		        formObject.setNGValue("NotepadDetails_noteDate",modifiedDate,false);
				//formObject.setNGValue("NotepadDetails_Actdate",modifiedDate);
		    	formObject.setNGValue("NotepadDetails_insqueue",sActivityName,false);
		    	
				formObject.setLocked("NotepadDetails_noteDate",true);
				formObject.setLocked("NotepadDetails_Actusername",true);
				formObject.setLocked("NotepadDetails_user",true);
				formObject.setLocked("NotepadDetails_insqueue",true);
				formObject.setLocked("NotepadDetails_Actdate",true);
				formObject.setLocked("NotepadDetails_Actdate",true);
				formObject.setLocked("NotepadDetails_ActuserRemarks",true);
				
				formObject.setLocked("NotepadDetails_notedesc",false);
				formObject.setLocked("NotepadDetails_notecode",true);
				formObject.setLocked("NotepadDetails_notedetails",false);
				}
			
			public void Notepad_delete()
			{
				// added by abhishek as per RLOS FSD
				FormReference formObject = FormContext.getCurrentInstance().getFormReference();
				 String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
				 Date date = new Date();
					String modifiedDate= new SimpleDateFormat("dd/MM/yyyy").format(date);
				// SKLogger_CC.writeLog("CCyash ", "Activity name is:" + sActivityName);
		        //formObject.setNGValue("NotepadDetails_Actusername",formObject.getUserId()+"-"+formObject.getUserName());
		        formObject.setNGValue("NotepadDetails_user",formObject.getUserId()+"-"+formObject.getUserName(),false);
		       
				
		    	formObject.setNGValue("NotepadDetails_insqueue",sActivityName,false);
		    	 formObject.setNGValue("NotepadDetails_noteDate",modifiedDate,false);
		    	//formObject.setNGValue("NotepadDetails_Actdate",modifiedDate);
		    	
				formObject.setLocked("NotepadDetails_noteDate",true);
				formObject.setLocked("NotepadDetails_Actusername",true);
				formObject.setLocked("NotepadDetails_user",true);
				formObject.setLocked("NotepadDetails_insqueue",true);
				formObject.setLocked("NotepadDetails_Actdate",true);
				formObject.setLocked("NotepadDetails_Actdate",true);
				//formObject.setLocked("NotepadDetails_inscompletion",true);
				formObject.setLocked("NotepadDetails_ActuserRemarks",true);
				
				formObject.setLocked("NotepadDetails_notedesc",false);
				formObject.setLocked("NotepadDetails_notecode",true);
				formObject.setLocked("NotepadDetails_notedetails",false);
			}
			
			public void Notepad_grid()
			{
				RLOS.mLogger.info( "Inside Notepad_grid"); 
				FormReference formObject = FormContext.getCurrentInstance().getFormReference();
				String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
				Date date = new Date();
				String modifiedDate= new SimpleDateFormat("dd/MM/yyyy").format(date);
				RLOS.mLogger.info( "modifiedDate :"+modifiedDate); 
				formObject.setNGValue("NotepadDetails_Actusername",formObject.getUserId()+"-"+formObject.getUserName(),false);
				formObject.setNGValue("NotepadDetails_insqueue",sActivityName,false);
				formObject.setNGValue("NotepadDetails_Actdate",modifiedDate,false);								
				formObject.setLocked("NotepadDetails_notedesc",true);
				formObject.setLocked("NotepadDetails_notecode",true);
				formObject.setLocked("NotepadDetails_notedetails",true);
				formObject.setLocked("NotepadDetails_noteDate",true);
				formObject.setLocked("NotepadDetails_Actusername",true);
				formObject.setLocked("NotepadDetails_user",true);
				formObject.setLocked("NotepadDetails_insqueue",true);
				formObject.setLocked("NotepadDetails_Actdate",true);
				
				//formObject.setLocked("NotepadDetails_inscompletion",false);
				formObject.setLocked("NotepadDetails_ActuserRemarks",false);
			}



	public void Fields_ApplicationType_Eligibility()
	{
		RLOS.mLogger.info( "Inside Fields_ApplicationType_Eligibility:"); 

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		
		//added By Akshay on 29/9/17 for point 52---Net payout field not to be visible for new / takeover

		if(formObject.getNGValue("Application_Type").contains("TOP")){
			formObject.setVisible("ELigibiltyAndProductInfo_Label8",false); 
			formObject.setVisible("cmplx_EligibilityAndProductInfo_NetPayout",false); 
		}

		else if(formObject.getNGValue("Application_Type").contains("TKO"))
		{

			formObject.setVisible("ELigibiltyAndProductInfo_Label1",true);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_TakeoverAMount",true);
			formObject.setVisible("ELigibiltyAndProductInfo_Label2",true);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_takeoverBank",true);
		}

		else{
			formObject.setVisible("ELigibiltyAndProductInfo_Label8",true); 
			formObject.setVisible("cmplx_EligibilityAndProductInfo_NetPayout",true); 
		
		//ended By Akshay on 29/9/17 for point 52---Net payout field not to be visible for new / takeover
		}
		
	}

	public void  Fields_Eligibility() 
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		if("Personal Loan".equalsIgnoreCase(formObject.getNGValue("PrimaryProduct"))){
			//formObject.setVisible("ELigibiltyAndProductInfo_Label39",true);//This field is always true by default
			//formObject.setVisible("cmplx_EligibilityAndProductInfo_instrumenttype",true);
			formObject.setTop("ELigibiltyAndProductInfo_Frame7",90);
			formObject.setTop("ELigibiltyAndProductInfo_Save",formObject.getTop("ELigibiltyAndProductInfo_Frame7")+formObject.getHeight("ELigibiltyAndProductInfo_Frame7")+10);
			formObject.setTop("ELigibiltyAndProductInfo_Label39",formObject.getTop("ELigibiltyAndProductInfo_Frame2")+formObject.getHeight("ELigibiltyAndProductInfo_Frame2")+10);
			formObject.setTop("ELigibiltyAndProductInfo_Label4",formObject.getTop("ELigibiltyAndProductInfo_Frame2")+formObject.getHeight("ELigibiltyAndProductInfo_Frame2")+10);
			formObject.setTop("cmplx_EligibilityAndProductInfo_FinalLimit",formObject.getTop("ELigibiltyAndProductInfo_Label4")+16);
			formObject.setTop("cmplx_EligibilityAndProductInfo_instrumenttype",formObject.getTop("ELigibiltyAndProductInfo_Label39")+16);
			//formObject.setTop("ELigibiltyAndProductInfo_Button1",formObject.getTop("cmplx_EligibilityAndProductInfo_FinalLimit")+16);
			//formObject.setTop("ELigibiltyAndProductInfo_Save",formObject.getTop("cmplx_EligibilityAndProductInfo_FinalLimit")+35);
		}

		else{

			if("IM".equalsIgnoreCase(formObject.getNGValue("Subproduct_productGrid"))){
				formObject.setVisible("ELigibiltyAndProductInfo_Label8",false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label2",false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_NetPayout",false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_Moratorium",false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_BaseRateType",false); 
				formObject.setVisible("cmplx_EligibilityAndProductInfo_MArginRate",false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_BAseRate",false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_ProdPrefRate",false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_ageAtMaturity",false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_NumberOfInstallment",false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_LPF",false);	
				formObject.setVisible("cmplx_EligibilityAndProductInfo_LPFAmount",false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_Insurance",false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_InsuranceAmount",false);		
				formObject.setVisible("cmplx_EligibilityAndProductInfo_instrumenttype",false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label31",false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label15",false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label28",false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label17",false);
				//formObject.setVisible("ELigibiltyAndProductInfo_Label10",false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label16",false); 
				formObject.setVisible("ELigibiltyAndProductInfo_Label29",false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label18",false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label33",false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label30",false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label32",false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label19",false);	
				formObject.setVisible("ELigibiltyAndProductInfo_Label35",false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label20",false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label36",false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label21",false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label39",false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label22",false);
				formObject.setTop("ELigibiltyAndProductInfo_Frame7",80);

				formObject.setLeft("ELigibiltyAndProductInfo_Label12",264);
				formObject.setLeft("cmplx_EligibilityAndProductInfo_FirstRepayDate",264);
				formObject.setLeft("cmplx_EligibilityAndProductInfo_InterestType",536);
				formObject.setLeft("ELigibiltyAndProductInfo_Label14",536);
				formObject.setTop("cmplx_EligibilityAndProductInfo_FinalInterestRate",192);
				formObject.setTop("ELigibiltyAndProductInfo_Label23",176);
				formObject.setTop("cmplx_EligibilityAndProductInfo_MaturityDate",192);
				formObject.setTop("ELigibiltyAndProductInfo_Label13",176);
				formObject.setHeight("ELigibiltyAndProductInfo_Frame7",formObject.getTop("cmplx_EligibilityAndProductInfo_MaturityDate")+formObject.getHeight("cmplx_EligibilityAndProductInfo_MaturityDate")+10);
				formObject.setTop("ELigibiltyAndProductInfo_Save",formObject.getTop("ELigibiltyAndProductInfo_Frame7")+formObject.getHeight("ELigibiltyAndProductInfo_Frame7")+10);
				//formObject.setHeight("ELigibiltyAndProductInfo_Frame1",530);
				//formObject.setHeight("EligibilityAndProductInformation",550);
			}	


			else if("SAL".equalsIgnoreCase(formObject.getNGValue("Subproduct_productGrid"))){
				Eligibility_Hide();
				formObject.setVisible("ELigibiltyAndProductInfo_Label41",true); 
				formObject.setVisible("cmplx_EligibilityAndProductInfo_FinalTAI",true);
				formObject.setTop("ELigibiltyAndProductInfo_Frame7",80);

			}

			else if("BTC".equalsIgnoreCase(formObject.getNGValue("Subproduct_productGrid"))){
				formObject.setVisible("cmplx_EligibilityAndProductInfo_FinalDBR",false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label3",false);
				Eligibility_Hide();		
				formObject.setLeft("ELigibiltyAndProductInfo_Label4",8);
				formObject.setLeft("cmplx_EligibilityAndProductInfo_FinalLimit",8);
				formObject.setTop("ELigibiltyAndProductInfo_Frame7",80);
				formObject.setTop("ELigibiltyAndProductInfo_Label4",10);
				formObject.setTop("cmplx_EligibilityAndProductInfo_FinalLimit",25);
				formObject.setTop("ELigibiltyAndProductInfo_Button1",formObject.getTop("cmplx_EligibilityAndProductInfo_FinalLimit")+formObject.getHeight("cmplx_EligibilityAndProductInfo_FinalLimit")+10);
				formObject.setHeight("ELigibiltyAndProductInfo_Frame7",formObject.getTop("ELigibiltyAndProductInfo_Button1")+formObject.getHeight("ELigibiltyAndProductInfo_Button1")+10);
				formObject.setTop("ELigibiltyAndProductInfo_Save",formObject.getTop("ELigibiltyAndProductInfo_Frame7")+formObject.getHeight("ELigibiltyAndProductInfo_Frame7")+10);

				//formObject.setHeight("ELigibiltyAndProductInfo_Frame1",430);
				//formObject.setHeight("EligibilityAndProductInformation",450);
			}
			
			else if("LI".equalsIgnoreCase(formObject.getNGValue("Subproduct_productGrid")) || "PU".equalsIgnoreCase(formObject.getNGValue("Subproduct_productGrid"))){
				Eligibility_Hide();
				formObject.setTop("ELigibiltyAndProductInfo_Frame7",80);
				formObject.setLeft("ELigibiltyAndProductInfo_Label4",264);
				formObject.setLeft("cmplx_EligibilityAndProductInfo_FinalLimit",264);
				formObject.setTop("ELigibiltyAndProductInfo_Button1",formObject.getTop("cmplx_EligibilityAndProductInfo_FinalLimit")+formObject.getHeight("cmplx_EligibilityAndProductInfo_FinalLimit")+10);
				formObject.setHeight("ELigibiltyAndProductInfo_Frame7",formObject.getTop("ELigibiltyAndProductInfo_Button1")+formObject.getHeight("ELigibiltyAndProductInfo_Button1")+10);
				formObject.setTop("ELigibiltyAndProductInfo_Save",formObject.getTop("ELigibiltyAndProductInfo_Frame7")+formObject.getHeight("ELigibiltyAndProductInfo_Frame7")+10);
			}

			else{
				Eligibility_UnHide();
				formObject.setVisible("cmplx_EligibilityAndProductInfo_FinalDBR",true);
				formObject.setVisible("ELigibiltyAndProductInfo_Label3",true);
				formObject.setVisible("ELigibiltyAndProductInfo_Label39",false);
				//formObject.setVisible("ELigibiltyAndProductInfo_Label40",false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_instrumenttype",false);
				formObject.setTop("ELigibiltyAndProductInfo_Frame7",80);
				formObject.setTop("ELigibiltyAndProductInfo_Save",formObject.getTop("ELigibiltyAndProductInfo_Frame7")+formObject.getHeight("ELigibiltyAndProductInfo_Frame7")+10);

			}
		}
	}

	public void  Fields_ServiceRequest()
	{
		RLOS.mLogger.info( "Inside Fields_ServiceRequest:"); 
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(n>0){
			for(int i=0;i<n;i++){
				RLOS.mLogger.info( formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9)); 
				if("Instant Money".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2)) || "Salaried Credit Card".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2)) && "Primary".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9))){
					RLOS.mLogger.info("hi");
					formObject.setVisible("CC_Loan_Frame3",false); 
					formObject.setVisible("CC_Loan_Frame5",true); 
					break;
				}
				else if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).contains("Business titanium Card") || "Self Employed Credit Card".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2)) && "Primary".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9))){
					RLOS.mLogger.info("hello");
					formObject.setVisible("CC_Loan_Frame3",false);
					formObject.setVisible("CC_Loan_Frame5",true); 					
					break;
				}
				else if("Limit Increase".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2)) && "Primary".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9))){
					RLOS.mLogger.info("Bye");
					formObject.setVisible("CC_Loan_Frame5",false); 
					formObject.setVisible("CC_Loan_Frame3",false); 
					break;
				}
				else if("Product Upgrade".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2)) && "Primary".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9))){
					RLOS.mLogger.info("PU");
					formObject.setVisible("CC_Loan_Frame5",false); 
					formObject.setVisible("CC_Loan_Frame3",false); 
				}
				else{
					formObject.setVisible("CC_Loan_Frame5",true); 
					formObject.setVisible("CC_Loan_Frame3",true); 
				}
			}	
		}
		else{
			formObject.setVisible("CC_Loan_Frame5",true); 
			formObject.setVisible("CC_Loan_Frame3",true); 
		}
	}



	public void Eligibility_Hide()
	{
		RLOS.mLogger.info( "Inside Eligibility_Hide:"); 
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setVisible("ELigibiltyAndProductInfo_Label41",false); 
		formObject.setVisible("cmplx_EligibilityAndProductInfo_FinalTAI",false); //Final TAI
		formObject.setVisible("cmplx_EligibilityAndProductInfo_InterestRate",false); 
		formObject.setVisible("cmplx_EligibilityAndProductInfo_EMI",false);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_Tenor",false);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_NetPayout",false);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_RepayFreq",false);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_FirstRepayDate",false);	
		formObject.setVisible("cmplx_EligibilityAndProductInfo_Moratorium",false);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_InterestType",false);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_BaseRateType",false); 
		formObject.setVisible("cmplx_EligibilityAndProductInfo_MArginRate",false);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_BAseRate",false);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_ProdPrefRate",false);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_FinalInterestRate",false);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_MaturityDate",false);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_LPF",false);	
		formObject.setVisible("cmplx_EligibilityAndProductInfo_LPFAmount",false);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_Insurance",false);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_InsuranceAmount",false);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_ageAtMaturity",false);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_NumberOfInstallment",false);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_instrumenttype",false);
		formObject.setVisible("ELigibiltyAndProductInfo_Label5",false);
		formObject.setVisible("ELigibiltyAndProductInfo_Label6",false); 
		formObject.setVisible("ELigibiltyAndProductInfo_Label7",false);
		formObject.setVisible("ELigibiltyAndProductInfo_Label8",false);
		formObject.setVisible("ELigibiltyAndProductInfo_Label11",false);
		formObject.setVisible("ELigibiltyAndProductInfo_Label12",false);
		formObject.setVisible("ELigibiltyAndProductInfo_Label14",false);
		formObject.setVisible("ELigibiltyAndProductInfo_Label15",false);	
		formObject.setVisible("ELigibiltyAndProductInfo_Label17",false);
		formObject.setVisible("ELigibiltyAndProductInfo_Label16",false);
		formObject.setVisible("ELigibiltyAndProductInfo_Label18",false);
		formObject.setVisible("ELigibiltyAndProductInfo_Label23",false);
		formObject.setVisible("ELigibiltyAndProductInfo_Label13",false);
		formObject.setVisible("ELigibiltyAndProductInfo_Label19",false);	
		formObject.setVisible("ELigibiltyAndProductInfo_Label20",false);
		formObject.setVisible("ELigibiltyAndProductInfo_Label21",false);
		formObject.setVisible("ELigibiltyAndProductInfo_Label22",false);	
		formObject.setVisible("ELigibiltyAndProductInfo_Label24",false);
		formObject.setVisible("ELigibiltyAndProductInfo_Label25",false);
		formObject.setVisible("ELigibiltyAndProductInfo_Label9",false);
		formObject.setVisible("ELigibiltyAndProductInfo_Label27",false);
		formObject.setVisible("ELigibiltyAndProductInfo_Label28",false);	
		formObject.setVisible("ELigibiltyAndProductInfo_Label10",false);
		formObject.setVisible("ELigibiltyAndProductInfo_Label29",false);
		formObject.setVisible("ELigibiltyAndProductInfo_Label31",false);
		formObject.setVisible("ELigibiltyAndProductInfo_Label30",false);
		formObject.setVisible("ELigibiltyAndProductInfo_Label32",false);
		formObject.setVisible("ELigibiltyAndProductInfo_Label33",false);
		formObject.setVisible("ELigibiltyAndProductInfo_Label34",false);
		formObject.setVisible("ELigibiltyAndProductInfo_Label26",false);
		formObject.setVisible("ELigibiltyAndProductInfo_Label35",false);	
		formObject.setVisible("ELigibiltyAndProductInfo_Label36",false);
		formObject.setVisible("ELigibiltyAndProductInfo_Label39",false);
		//formObject.setVisible("ELigibiltyAndProductInfo_Label38",false);	
	}

	public void Eligibility_UnHide()
	{
		RLOS.mLogger.info( "Inside Eligibility_UnHide():"); 
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.setVisible("ELigibiltyAndProductInfo_Label41",true); 
		formObject.setVisible("ELigibiltyAndProductInfo_Text7",true);//final tai
		formObject.setVisible("cmplx_EligibilityAndProductInfo_InterestRate",true); 
		formObject.setVisible("cmplx_EligibilityAndProductInfo_EMI",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_Tenor",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_NetPayout",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_RepayFreq",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_FirstRepayDate",true);	
		formObject.setVisible("cmplx_EligibilityAndProductInfo_Moratorium",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_InterestType",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_BaseRateType",true); 
		formObject.setVisible("cmplx_EligibilityAndProductInfo_MArginRate",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_BAseRate",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_ProdPrefRate",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_FinalInterestRate",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_MaturityDate",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_LPF",true);	
		formObject.setVisible("cmplx_EligibilityAndProductInfo_LPFAmount",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_Insurance",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_InsuranceAmount",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_ageAtMaturity",true);
		formObject.setVisible("cmplx_EligibilityAndProductInfo_NumberOfInstallment",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label3",true);		
		formObject.setVisible("ELigibiltyAndProductInfo_Label5",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label6",true); 
		formObject.setVisible("ELigibiltyAndProductInfo_Label7",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label8",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label11",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label12",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label14",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label15",true);	
		formObject.setVisible("ELigibiltyAndProductInfo_Label17",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label16",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label18",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label23",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label13",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label19",true);	
		formObject.setVisible("ELigibiltyAndProductInfo_Label20",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label21",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label22",true);	
		formObject.setVisible("ELigibiltyAndProductInfo_Label24",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label25",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label9",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label27",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label28",true);	
		formObject.setVisible("ELigibiltyAndProductInfo_Label10",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label29",true);	
		formObject.setVisible("ELigibiltyAndProductInfo_Label33",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label34",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label26",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label35",true);	
		formObject.setVisible("ELigibiltyAndProductInfo_Label36",true);
		//formObject.setVisible("ELigibiltyAndProductInfo_Label37",true);
		//formObject.setVisible("ELigibiltyAndProductInfo_Label38",true);	
	}


	public void TypeOfProduct()
	{
		RLOS.mLogger.info( "Inside TypeOfProduct:"); 
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		for(int i=0;i<n;i++)
		{
			if("Primary".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9))){
				String Type_Prod=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,0);
				String req_Prod=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1);
				RLOS.mLogger.info( "Loan_Type,req_Prod:"+Type_Prod+req_Prod); 
				if("Personal Loan".equalsIgnoreCase(req_Prod))
					formObject.setNGValue("PL_LoanType",Type_Prod);
				else if("Credit Card".equalsIgnoreCase(req_Prod))
					formObject.setNGValue("CC_LoanType",Type_Prod);
			}
		}
	}	


public void saveIndecisionGrid(){
        
        RLOS.mLogger.info( "Inside saveIndecisionGrid: "); 
        String entrydate ;
        FormReference formObject = FormContext.getCurrentInstance().getFormReference();
        String EntrydateTime = formObject.getNGValue("Entry_Date") ;
        String[] parts = EntrydateTime.split("/");
        entrydate = parts[1]+"/"+parts[0]+"/"+parts[2]; 
        RLOS.mLogger.info( "Inside saveIndecisionGrid:EntrydateTime "+EntrydateTime); 
        String currDate=new SimpleDateFormat("MM/dd/yyyy").format(Calendar.getInstance().getTime());
        RLOS.mLogger.info( "Inside saveIndecisionGrid...currDate "+currDate); 
      // added by abhishek for insert into DB
           String query="insert into ng_rlos_gr_Decision(dateLastChanged, userName, workstepName, Decisiom, remarks, dec_wi_name,Entry_Date) values('"+currDate+"','"+formObject.getUserName()+"','"+formObject.getWFActivityName()+"','"+formObject.getNGValue("cmplx_DecisionHistory_Decision")+"','"+formObject.getNGValue("cmplx_DecisionHistory_Remarks")+"','"+formObject.getWFWorkitemName()+"',convert(varchar(30),cast('"+entrydate+"' as datetime),102))";
 
           RLOS.mLogger.info("Query is"+query);
            formObject.saveDataIntoDataSource(query);
       
        }
 


	public void AddPrimaryData()
	{
		RLOS.mLogger.info("RLOSCommon Inside AddPrimaryData() ");

		FormReference formObject=FormContext.getCurrentInstance().getFormReference();
		String LoanType="";
		String ReqProd="";
		String SubProd="";
		String CardProd="";
		String AppType="";

		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		for(int i=0;i<n;i++)	
		{
			if("Primary".equals(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9))){
				LoanType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,0);	
				ReqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1);
				SubProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2);
				CardProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,5);	
				AppType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,4);
			}	
		}

		formObject.setNGValue("LoanType_Primary", LoanType);
		formObject.setNGValue("PrimaryProduct", ReqProd);
		formObject.setNGValue("Subproduct_productGrid", SubProd);
		formObject.setNGValue("CardProduct_Primary", CardProd);
		formObject.setNGValue("Application_Type", AppType);

		RLOS.mLogger.info("Inside ParentToChild -> n :"+n+"PrimaryData is:"+ReqProd+SubProd+CardProd);

	}


	public void  AddProducts()
	{
		RLOS.mLogger.info("RLOSCommon Inside AddProducts() ");

		FormReference formObject=FormContext.getCurrentInstance().getFormReference();
		String mystring="";
		String product="";

		try
		{
			int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			for(int i=0;i<n;i++)	
			{
				mystring=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1);
				RLOS.mLogger.info("Inside AddProducts() mystring:"+mystring);
				if(!product.contains(mystring))
					product+=mystring;			
			}

			formObject.setNGValue("Product_Type", product);
			RLOS.mLogger.info("Inside AddProducts -> n :"+n+"Product_Type is:"+product);
		}
		catch(Exception e)
		{
			RLOS.mLogger.info("Inside AddProducts ->Exception occurred:"+e.getMessage());
			RLOS.logException(e);

		}
	}



	public void CIFIDCheck(){

		FormReference formObject=FormContext.getCurrentInstance().getFormReference();
		RLOS.mLogger.info("Inside CIFIDCheck()");
		String query="Select WIname from NG_RLOS_EXTTABLE with (nolock) WHERE introduction_date BETWEEN DATEADD(minute, -10, getdate()) AND getdate() and cifId = '"+formObject.getNGValue("cmplx_Customer_CIFNO")+"' AND WIname !='"+formObject.getWFWorkitemName()+"' order by Introduction_date desc";
		List<List<String>> list=formObject.getDataFromDataSource(query);
		String wiName=list.get(0).get(0);
		RLOS.mLogger.info("QUERY is:"+query);
		RLOS.mLogger.info("Data from DB:"+wiName);
		if(wiName!=null && wiName!="")
			throw new ValidatorException(new FacesMessage("Another instance for this CIFID was created less than 10 mins ago: "+wiName));
		else
			RLOS.mLogger.info("No pipeline Case");
	}

	
	public void populatePickListWindow(String sQuery, String sControlName,String sColName, boolean bBatchingRequired, int iBatchSize)
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PickList objPickList = formObject.getNGPickList(sControlName, sColName, bBatchingRequired, iBatchSize);

		if("EMploymentDetails_Button2".equalsIgnoreCase(sControlName)) 
			objPickList.setWindowTitle("Search Employer");

		List<List<String>> result=formObject.getDataFromDataSource(sQuery);
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
			objPickList.addPickListListener(new EventListenerHandler(objPickList.getClientId()));
			
			RLOS.mLogger.info(result.toString());   
			objPickList.populateData(result);			
		}
	}

	//code added here
	public String FinancialSummaryXML(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String Employment_type="";
		String requested_subproduct="";

		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		RLOS.mLogger.info("valu of financial summary"+n);
		if(n>0)
		{
			for(int i=0;i<n;i++)
			{
				requested_subproduct= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2);
				RLOS.mLogger.info(requested_subproduct);
				Employment_type= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 6);
				RLOS.mLogger.info(Employment_type);

			}
		}
		RLOS.mLogger.info(requested_subproduct+","+Employment_type);

		String sQuery_header = "SELECT Operation_name from ng_rlos_Financial_Summary  with (nolock) where Description='"+requested_subproduct+"' and Employment='"+Employment_type+"' ";
		RLOS.mLogger.info( "Financial_Summary"+sQuery_header);
		List<List<String>> OutputXML_header=formObject.getDataFromDataSource(sQuery_header);

		String Operation_name = OutputXML_header.get(0).get(0);
		RLOS.mLogger.info( "Financial_Summary Operation_name"+Operation_name);
		return Operation_name;
	}
	//code changed here


	


	public void setMailId(String userName)
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		
		try
		{			
			String squery= "select mailid from pdbuser with (nolock) WHERE UPPER(USERNAME)=UPPER('"+userName+"')";
			List<List<String>> outputindex;
			outputindex = formObject.getDataFromDataSource(squery);			
			RLOS.mLogger.info( "mailID outputItemindex is: " +  outputindex);
			String mailID =outputindex.get(0).get(0);
			RLOS.mLogger.info( "mailID is:" +  mailID);
			formObject.setNGValue("processby_email",  mailID);

		}
		catch(Exception e)
		{
			RLOS.logException(e);//pgarg
		}
	}
	
	//new code on 11th september
	   //tanshu aggarwal for documents(1/06/2017)
public void fetchIncomingDocRepeater(){

  RLOS.mLogger.info( "inside fetchIncomingDocRepeater");

  FormReference formObject = FormContext.getCurrentInstance().getFormReference();
  
   String requested_product;
   String requested_subproduct;
   String application_type;
   String product_type;
   List<List<String>> docName1;
   int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
    RLOS.mLogger.info("valu of row count"+n);

           product_type= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 0);
           RLOS.mLogger.info(product_type);
           requested_product= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 1);
           RLOS.mLogger.info(requested_product);
           requested_subproduct= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2);
           RLOS.mLogger.info(requested_subproduct);
           application_type= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 4);
           RLOS.mLogger.info(application_type);

    RLOS.mLogger.info(requested_product);
   

  List<String> repeaterHeaders = new ArrayList<String>();
  
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

  RLOS.mLogger.info("after making headers");
  FormContext.getCurrentInstance().getFormConfig().getConfigElement("ProcessName");

  
  List<List<String>> docName = null;
 
  String documentName = null;
  String documentNameMandatory=null;

  String query = "";

  IRepeater repObj= formObject.getRepeaterControl("IncomingDoc_Frame");
  RLOS.mLogger.info("after creating the object for repeater");

  int repRowCount = 0;
//  repObj = formObject.getRepeaterControl("IncomingDoc_Frame");
  RLOS.mLogger.info(""+repObj.toString());
  repObj.setRepeaterHeaders(repeaterHeaders);
  try
  {

			if (repObj.getRepeaterRowCount() == 0) {
				repObj.clear();
				if("Personal Loan".equalsIgnoreCase(requested_product)){
					query = "SELECT distinct DocName,Mandatory FROM ng_rlos_DocTable WHERE (ProductName = 'Personal Loan' and SubProductName = '"+requested_subproduct+"' and Application_Type = '"+application_type+"') or ProductName='All' order by Mandatory desc";
					RLOS.mLogger.info( "when row count is  zero inside if"+query);
					docName = formObject.getNGDataFromDataCache(query);
					RLOS.mLogger.info(""+ docName);
				}
				else{
					//query = "SELECT distinct DocName,Mandatory FROM ng_rlos_DocTable WHERE ProductName='"+requested_product+"' OR ProductName='All' and ((SubProductName='"+requested_subproduct+"'  and  ProcessName='RLOS')  OR ProductName='All') ";
					//Query corrected by Deepak.
					query = "SELECT distinct DocName,Mandatory FROM ng_rlos_DocTable WHERE (ProductName = '"+requested_product+"' and SubProductName = '"+requested_subproduct+"') or ProductName='All'";
					RLOS.mLogger.info( "when row count is  zero inside else"+query);
					docName = formObject.getNGDataFromDataCache(query);
					RLOS.mLogger.info(""+ docName);
				}
				
				//added
				for(int i=0;i<docName.size();i++ ){
					repObj.addRow();

           documentName = docName.get(i).get(0);
          documentNameMandatory = docName.get(i).get(1);

          RLOS.mLogger.info(" "+ documentName);
          RLOS.mLogger.info(" "+ documentNameMandatory);

          repObj.setValue(i, 0, documentName);
          repObj.setValue(i, 2, documentNameMandatory);
          repObj.setColumnDisabled(0, true);
			 repObj.setColumnDisabled(2, true);
				
          repRowCount = repObj.getRepeaterRowCount();
          RLOS.mLogger.info( " " + repRowCount);
         

      }
  
      //ended
  }
  else 
	  if (repObj.getRepeaterRowCount() != 0) {
		  RLOS.mLogger.info( ""+repObj.getRepeaterRowCount());
		  int rowCount = repObj.getRepeaterRowCount();
  	repObj.clear();
      RLOS.mLogger.info( "when row count is not zero"+rowCount);
     
//       query = "SELECT distinct DocName,Mandatory,DocInd FROM ng_rlos_incomingDoc WHERE ProductName='"+requested_product+"' and SubProductName='"+requested_subproduct+"' and  wi_name='"+formObject.getWFWorkitemName()+"'";
      query = "SELECT distinct DocName,ExpiryDate,Mandatory,Status,Remarks,DocInd FROM ng_rlos_incomingDoc WHERE  wi_name='"+formObject.getWFWorkitemName()+"'";
      
      RLOS.mLogger.info( "when row count is not zero"+query);
      
      docName = formObject.getNGDataFromDataCache(query);
      RLOS.mLogger.info(""+ docName);
    
      int docSize = rowCount + docName.size();
      RLOS.mLogger.info("docSize"+ docSize);
      for(int i=rowCount;i<docSize;i++)
  // 	 for(int i=0;i<docName.size();i++ )
      {
     		 RLOS.mLogger.info( "inside else if for loop");
              repObj.addRow();
              int index = i- rowCount;
              documentName = docName.get(index).get(0);
              String expiryDate = docName.get(index).get(1);
              documentNameMandatory = docName.get(index).get(2);
              String Status = docName.get(index).get(3);
              String Remarks = docName.get(index).get(4);
              String DocInd = docName.get(index).get(5);

              RLOS.mLogger.info(" "+ documentName);
              RLOS.mLogger.info(" "+ documentNameMandatory);
              RLOS.mLogger.info(" "+ expiryDate);
              RLOS.mLogger.info(" "+ Status);
              RLOS.mLogger.info(" "+ Remarks);
              RLOS.mLogger.info(" "+ DocInd);

              repObj.setValue(i,0,documentName);
              repObj.setValue(i,1,expiryDate);
              repObj.setValue(i,2,documentNameMandatory);
              repObj.setValue(i,3,Status);
              repObj.setValue(i,4,Remarks);
              repObj.setValue(i,11,DocInd);
              repObj.setColumnDisabled(0, true);
              repObj.setColumnDisabled(2, true);
              repRowCount = repObj.getRepeaterRowCount();
              RLOS.mLogger.info( " " + repRowCount);            
     }   	
  }
 }
 catch (Exception e) 
  {
     RLOS.mLogger.info( " " + e.toString());
     RLOS.logException(e);
  }  
}
//tanshu aggarwal for documents(1/06/2017)
	//new code on 11th september
	public void loadInDecGrid()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		try
		{
			String ParentWI_Name = formObject.getNGValue("Parent_WIName");
			String query="select FORMAT(datelastchanged,'dd-MM-yyyy hh:mm'),userName,workstepName,Decisiom,remarks,wi_nAme,Entry_date from ng_rlos_gr_Decision with (nolock) where wi_nAme='"+ParentWI_Name+"' or wi_nAme='"+formObject.getWFWorkitemName()+"'";
			RLOS.mLogger.info("Inside PLCommon ->loadInDecGrid()"+query);
			List<List<String>> list=formObject.getDataFromDataSource(query);
			RLOS.mLogger.info("Inside PLCommon ->loadInDecGrid()"+list.get(0).get(0)+list.get(0).get(1)+list.get(0).get(2)+list.get(0).get(3)+list.get(0).get(4));
			/* try{
			 String date=list.get(0).get(0);
			 Date d=new SimpleDateFormat("MM/dd/yyyy HH:mm").parse(date);
			 RLOS.mLogger.info("value of date is:"+d.toString());*/
			for (List<String> a : list) 
			{
				/*List<String> mylist=new ArrayList<String>();
				 mylist.add(d.toString());
				 mylist.add(list.get(0).get(1));
				 mylist.add(list.get(0).get(2));
				 mylist.add(list.get(0).get(3));
				 mylist.add(list.get(0).get(4));
				 mylist.add(list.get(0).get(5));*/
				formObject.addItemFromList("Decision_ListView1", a);
				
			}
		}
		catch(Exception e)
		{
			RLOS.mLogger.info("Exception Occurred loadInDecGrid :"+e.getMessage());
			RLOS.logException(e);
		}	
	}
	
	
	public void parse_cif_eligibility(String output,String operation_name)
	{
		try
		{
			String outputXML=output.substring(output.indexOf("<MQ_RESPONSE_XML>")+17,output.indexOf("</MQ_RESPONSE_XML>"));
			//;
			Map<Integer, HashMap<String, String> > Cus_details = new HashMap<Integer, HashMap<String, String>>();
			String passport_list = "";
			//Map<String, String> cif_details = new HashMap<String, String>();
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(outputXML)));
			RLOS.mLogger.info( doc+"");
			NodeList nl = doc.getElementsByTagName("*");

			for (int nodelen=0; nodelen<nl.getLength();nodelen++){
				Map<String, String> cif_details = new HashMap<String, String>();
				nl.item(nodelen);
				if("CustomerDetails".equalsIgnoreCase(nl.item(nodelen).getNodeName()))
				{
					int no_of_product = 0;
					NodeList childnode  = nl.item(nodelen).getChildNodes();
					for (int childnodelen= 0;childnodelen<childnode.getLength();childnodelen++){
						String tag_name = childnode.item(childnodelen).getNodeName();
						String tag_value=childnode.item(childnodelen).getTextContent();
						if(tag_name!=null && tag_value!=null){
							if("Products".equalsIgnoreCase(tag_name)){
								++no_of_product;
								cif_details.put(tag_name, Integer.toString(no_of_product));
							}else{
								if("PassportNum".equalsIgnoreCase(tag_name))
									passport_list = tag_value+ ","+passport_list;
								cif_details.put(tag_name, tag_value);
							}

						}
						else{
							RLOS.mLogger.info(tag_name+ " tag value: " +tag_value);
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
				FormReference formObject = FormContext.getCurrentInstance().getFormReference();
				Map<String, String> Supplementary ;
				Supplementary = Cus_details.get(Prim_cif);
				formObject.setNGValue("Supplementary_CIFNO",Supplementary.get("CustId")==null?"":Supplementary.get("CustId"));
			}
			else if("Primary_CIF".equalsIgnoreCase(operation_name)){
				save_cif_data(Cus_details,Prim_cif);
				if(Prim_cif!=0)
				{					
					Map<String, String> prim_entry;
					prim_entry = Cus_details.get(Prim_cif);
					String primary_pass = prim_entry.get("PassportNum");
					passport_list = passport_list.replace(primary_pass, "");
					RLOS.mLogger.info(prim_entry.get("CustId"));
					set_nonprimaryPassport(prim_entry.get("CustId")==null?"":prim_entry.get("CustId"),passport_list);//changes done to save CIF without 0.
				}
			}			
		}
		catch(Exception e)
		{
			RLOS.mLogger.info( e.getMessage());
			RLOS.logException(e);
		}

	}
	public void save_cif_data(Map<Integer, HashMap<String, String>> cusDetails ,int prim_cif){
		try{
			RLOS.mLogger.info( "inside save_cif_data methos");
			FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
			String WI_Name = formObject.getWFWorkitemName();
			RLOS.mLogger.info( "inside save_cif_data methos wi_name: "+WI_Name );
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
				//Chnages done to add blacklist reasone code, data Negated Reasoncode and date start
				Cif_data.add(curr_entry.get("BlacklistReasonCode")!=null?curr_entry.get("BlacklistReasonCode"):"");
				Cif_data.add(curr_entry.get("BlacklistDate")!=null?curr_entry.get("BlacklistDate"):"");
				Cif_data.add(curr_entry.get("NegatedReasonCode")!=null?curr_entry.get("NegatedReasonCode"):"");
				Cif_data.add(curr_entry.get("NegatedDate")!=null?curr_entry.get("NegatedDate"):"");
				//Chnages done to add blacklist reasone code, data Negated Reasoncode and date end
				if(curr_entry.get("CustId").equalsIgnoreCase(Integer.toString(prim_cif)))
				{
					Cif_data.add("Y");
				}
				else
				{
					Cif_data.add("N");	
				}
				Cif_data.add(WI_Name);
				formObject.addItemFromList("q_cif_detail", Cif_data);

				RLOS.mLogger.info( "data to dave in cif details grid: "+ Cif_data);				
			}
		}
		catch(Exception e)
		{
			RLOS.mLogger.info( e.getMessage());
			RLOS.logException(e);
		}
	}
	public int primary_cif_identify(Map<Integer, HashMap<String, String>> cusDetails )
	{
		int primary_cif = 0;
		try{
			Map<String, String> prim_entry;
			Map<String, String> curr_entry;


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
						prim_entry = cusDetails.get(Integer.toString(primary_cif));
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
			RLOS.mLogger.info( e.getMessage());
			RLOS.logException(e);
		}

		return primary_cif;
	}
	public void set_nonprimaryPassport(String cif_id, String pass_list){//changes done to save CIF without 0.
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
			RLOS.mLogger.info( e.getMessage());
			RLOS.logException(e);
		}

	}
	public void Customer_enable()
	{
		RLOS.mLogger.info( "inside Customer enable method");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String fields="cmplx_Customer_Title,cmplx_Customer_ResidentNonResident,cmplx_Customer_gender,cmplx_Customer_MotherName,cmplx_Customer_VisaNo,cmplx_Customer_MAritalStatus,cmplx_Customer_COuntryOFResidence,cmplx_Customer_SecNAtionApplicable,cmplx_Customer_SecNationality,cmplx_Customer_EMirateOfVisa,cmplx_Customer_EmirateOfResidence,cmplx_Customer_yearsInUAE,cmplx_Customer_CustomerCategory,cmplx_Customer_GCCNational,cmplx_Customer_VIPFlag"; 
		String[] field_array=fields.split(",");
		for(int i=0;i<field_array.length;i++)
			formObject.setLocked(field_array[i], true);

		formObject.setLocked("cmplx_Customer_Title", true);
		formObject.setLocked("cmplx_Customer_gender", false);

		formObject.setLocked("cmplx_Customer_EmirateIDExpiry",false);
		formObject.setLocked("cmplx_Customer_IdIssueDate",false);
		formObject.setLocked("cmplx_Customer_PassPortExpiry",false);
		formObject.setLocked("cmplx_Customer_VIsaExpiry",false);
		formObject.setLocked("cmplx_Customer_SecNAtionApplicable",false);

		RLOS.mLogger.info( "End Customer enable method");
	}
	public void SetDisableCustomer()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String fields="cmplx_Customer_Title,cmplx_Customer_ResidentNonResident,cmplx_Customer_gender,cmplx_Customer_age,cmplx_Customer_MotherName,cmplx_Customer_EmirateIDExpiry,cmplx_Customer_IdIssueDate,cmplx_Customer_VisaNo,cmplx_Customer_PassPortExpiry,cmplx_Customer_VIsaExpiry,cmplx_Customer_SecNationality,cmplx_Customer_MAritalStatus,cmplx_Customer_COuntryOFResidence,cmplx_Customer_EMirateOfVisa,cmplx_Customer_EmirateOfResidence,cmplx_Customer_yearsInUAE,cmplx_Customer_CustomerCategory,cmplx_Customer_GCCNational,cmplx_Customer_VIPFlag"; 
		String[] field_array=fields.split(",");
		for(int i=0;i<field_array.length;i++)
			formObject.setEnabled("DecisionHistory_Button5", true);
	}

	
	//incoming doc function
	public void IncomingDoc(){
	    FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
	    IRepeater repObj=formObject.getRepeaterControl("IncomingDoc_Frame");
	    //repObj = formObject.getRepeaterControl("IncomingDoc_Frame");
	 
	    String [] finalmisseddoc=new String[70];
	    int rowRowcount=repObj.getRepeaterRowCount();
	    RLOS.mLogger.info( "sQuery for document name is: rowRowcount" +  rowRowcount);
	        if (repObj.getRepeaterRowCount() != 0) {
	        
	            for(int j = 0; j < rowRowcount; j++)
	            {
	                String DocName=repObj.getValue(j, "cmplx_DocName_DocName");
	                RLOS.mLogger.info( "sQuery for document name is: DocName" +  DocName);
	            
	                String Mandatory=repObj.getValue(j, "cmplx_DocName_Mandatory");
	                RLOS.mLogger.info( "sQuery for document name is: Mandatory" +  Mandatory);
	                    
	                if("Y".equalsIgnoreCase(Mandatory))
	                    {
	                         String DocIndex=repObj.getValue(j,"cmplx_DocName_DocIndex");
	                         RLOS.mLogger.info("DocIndex"+DocIndex);
	                         String StatusValue=repObj.getValue(j,"cmplx_DocName_Status");
	                         RLOS.mLogger.info("StatusValue"+StatusValue);
	                         String Remarks=repObj.getValue(j, "cmplx_DocName_Remarks");
	                         RLOS.mLogger.info("Remarks"+Remarks);
	                        
	                             if("".equals(DocIndex))
								 {
	                                 RLOS.mLogger.info("StatusValue inside DocIndex"+DocIndex);
	                                 if("Received".equalsIgnoreCase(StatusValue)){
	                                     RLOS.mLogger.info("StatusValue inside DocIndex recieved");
	                                     finalmisseddoc[j]=DocName;
	                                 }
	                            
	                                  else if("Deferred".equalsIgnoreCase(StatusValue)){
	                                      formObject.setNGValue("is_deferral_approval_require","Y");
	                                      formObject.RaiseEvent("WFSave");
	                                      RLOS.mLogger.info(formObject.getNGValue("is_deferral_approval_require"));
	                                      if("".equals(Remarks))
										  {
	                                            RLOS.mLogger.info("As you have not attached the Mandatory Document and the status is Deferred");
	                                            throw new ValidatorException(new FacesMessage("As you have not attached the Mandatory Document fill the Remarks"));
	                                       }
	                                        else 
											{
	                                            RLOS.mLogger.info("Proceed further");
	                                        }
	                                 }
	                                  else if("Waived".equalsIgnoreCase(StatusValue)){
	                                      formObject.setNGValue("is_waiver_approval_require","Y");
	                                      formObject.RaiseEvent("WFSave");
	                                      RLOS.mLogger.info(formObject.getNGValue("is_waiver_approval_require"));
	                                        if("".equalsIgnoreCase(Remarks))
											{
	                                            RLOS.mLogger.info("As you have not attached the Mandatory Document and the status is Waived");
	                                            throw new ValidatorException(new FacesMessage("As you have not attached the Mandatory Document fill the Remarks"));
	                                        }
	                                        else
											{
	                                            RLOS.mLogger.info("Proceed further");
	                                        }
	                                  }
	                                  else if("--Select--".equalsIgnoreCase(StatusValue)|| "".equalsIgnoreCase(StatusValue))
									  {
	                                      RLOS.mLogger.info("StatusValue inside DocIndex is blank");
	                                      finalmisseddoc[j]=DocName;
	                                  }
	                                  else if(("Pending".equalsIgnoreCase(StatusValue)))
									  {
	                                      RLOS.mLogger.info("StatusValue of doc is Pending");	                                      
	                                  }
	                                }
	                                else{
	                                    if(!("".equalsIgnoreCase(DocIndex))){
	                                        if(!"Received".equalsIgnoreCase(StatusValue)){
	                                            repObj.setValue(j,"cmplx_DocName_Status","Received");
	                                            repObj.setEditable(j, "cmplx_DocName_Status", false);
	                                            RLOS.mLogger.info("StatusValue::123final"+StatusValue);
	                                        }
	                                        else {
	                                            
	                                            RLOS.mLogger.info("StatusValue::123final status is already received");
	                                        }
	                                    }
	                                }
	                                
	                            }
	                        }
	                    }
	                    StringBuilder mandatoryDocName = new StringBuilder("");
	                    
	                     RLOS.mLogger.info("length of missed document"+finalmisseddoc.length);
	                     RLOS.mLogger.info("length of missed document mandatoryDocName.length"+mandatoryDocName.length());
	                
	                    for(int k=0;k<finalmisseddoc.length;k++)
	                    {
	                        if(null != finalmisseddoc[k]) {
	                            mandatoryDocName.append(finalmisseddoc[k]).append(",");
	                        }
	                        RLOS.mLogger.info( "finalmisseddoc is:" +finalmisseddoc[k]);
	                         RLOS.mLogger.info("length of missed document mandatoryDocName.length1111"+mandatoryDocName.length());
	                    }
	                    mandatoryDocName.setLength(Math.max(mandatoryDocName.length()-1,0));
	                    RLOS.mLogger.info( "misseddoc is:" +mandatoryDocName.toString());
	                
	                    if(mandatoryDocName.length()<=0){
	                    
	                        RLOS.mLogger.info( "misseddoc is: inside if condition");
	                    
	                    }
	                    else{
	                        RLOS.mLogger.info( "misseddoc is: inside if condition");
	                        RLOS.mLogger.info("length of missed document mandatoryDocName.length1111222"+mandatoryDocName.length());
	                        throw new ValidatorException(new FacesMessage("You have not attached Mandatory Documents: "+mandatoryDocName.toString()));
	                    }
	                }
	
	// added for dectech call on 28july2017
	public String  getInternalLiabDetails(){
		RLOS.mLogger.info( "inside getCustAddress_details : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "SELECT AcctType,CustRoleType,AcctId,AccountOpenDate,AcctStat,AcctSegment,AcctSubSegment,CreditGrade FROM ng_RLOS_CUSTEXPOSE_AcctDetails  with (nolock) where Wi_Name = '"+formObject.getWFWorkitemName()+"' and Request_Type = 'InternalExposure'";
		
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		
		for (int i = 0; i<OutputXML.size();i++){

			String accountType = "";
			String role = "";
			String accNumber = "";
			String acctOpenDate = ""; 
			String acctStatus = "";
			String acctSegment = "";
			String acctSubSegment = "";
			String acctCreditGrade = "";
			if(!(OutputXML.get(i).get(0) == null || "".equals(OutputXML.get(i).get(0))) ){
				accountType = OutputXML.get(i).get(0);
			}
			if(!(OutputXML.get(i).get(1) == null || "".equals(OutputXML.get(i).get(1))) ){
				role = OutputXML.get(i).get(1);
			}
			if(!(OutputXML.get(i).get(2) == null || "".equals(OutputXML.get(i).get(2))) ){
				accNumber = OutputXML.get(i).get(2);
			}
			if(!(OutputXML.get(i).get(3) == null || "".equals(OutputXML.get(i).get(3))) ){
				acctOpenDate = OutputXML.get(i).get(3);
			}
			if(!(OutputXML.get(i).get(4) == null || "".equals(OutputXML.get(i).get(4))) ){
				acctStatus = OutputXML.get(i).get(4);
			}
			if(!(OutputXML.get(i).get(5) == null || "".equals(OutputXML.get(i).get(5))) ){
				acctSegment = OutputXML.get(i).get(5);
			}
			if(!(OutputXML.get(i).get(6) == null || "".equals(OutputXML.get(i).get(6))) ){
				acctSubSegment = OutputXML.get(i).get(6);
			}
			if(!(OutputXML.get(i).get(7) == null || "".equals(OutputXML.get(i).get(7))) ){
				acctCreditGrade = OutputXML.get(i).get(7);
			}
			if(!"".equalsIgnoreCase(accNumber))
			{
				add_xml_str = add_xml_str + "<AccountDetails><type_of_account>"+accountType+"</type_of_account>";
				add_xml_str = add_xml_str + "<role>"+role+"</role>";
				add_xml_str = add_xml_str + "<account_number>"+accNumber+"</account_number>";
				add_xml_str = add_xml_str + "<acct_open_date>"+acctOpenDate+"</acct_open_date>";
				add_xml_str = add_xml_str + "<acct_status>"+acctStatus+"</acct_status>";
				add_xml_str = add_xml_str + "<account_segment>"+acctSegment+"</account_segment>";
				add_xml_str = add_xml_str + "<account_sub_segment>"+acctSubSegment+"</account_sub_segment>";
				add_xml_str = add_xml_str + "<credit_grate_code>"+acctCreditGrade+"</credit_grate_code>";
				add_xml_str = add_xml_str + "<cust_type>"+role+"</cust_type></AccountDetails>";
			}
		}
		RLOS.mLogger.info( "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}


	
	// Deepak Change to calculate EMI start
	public static String getEMI(double loanAmount,double rate,double tenureMonths)
	{       
		String loanAmt_DaysDiff="";
		try{
			/*
			if(loanAmount==0 ||rate==0||tenureMonths==0){
				return "0";
			}
			*/
			BigDecimal B_intrate= BigDecimal.valueOf(rate);
			BigDecimal B_tenure= BigDecimal.valueOf(tenureMonths);
			BigDecimal B_loamamount= BigDecimal.valueOf(loanAmount);
			
			if(B_intrate.equals(BigDecimal.ZERO) ||
					B_tenure.equals(BigDecimal.ZERO) ||
				B_loamamount.equals(BigDecimal.ZERO)){
				return "0";
			}
			
			loanAmt_DaysDiff=calcgoalseekEMI(B_intrate,B_tenure,B_loamamount);

		}
		catch(Exception e)
		{
			RLOS.mLogger.info( "Exception occured in getEMI() : ");
			RLOS.logException(e);
			loanAmt_DaysDiff = "0";
		}
		return loanAmt_DaysDiff;
	}	
	public static BigDecimal calcEMI(BigDecimal P, BigDecimal N, BigDecimal ROI) {
		BigDecimal emi = new BigDecimal(0) ;
		try{
			MathContext mc = MathContext.DECIMAL128;     
			BigDecimal R = ROI.divide(new BigDecimal(1200),mc);
			BigDecimal nemi1 = P.multiply(R,mc);
			BigDecimal npower1 = (BigDecimal.ONE.add(R)).pow(N.intValue(),mc);
			BigDecimal dpower1 = (BigDecimal.ONE.add(R)).pow(N.intValue(),mc);
			BigDecimal denominator = dpower1.subtract(BigDecimal.ONE);
			BigDecimal numerator = nemi1.multiply(npower1);
			emi = numerator.divide(denominator,0,RoundingMode.UP);

		}
		catch(Exception e){
			RLOS.mLogger.info( "Exception occured in calcEMI() : ");
			RLOS.logException(e);
		}
		return emi;
	}
	public static String calcgoalseekEMI(BigDecimal B_intrate,BigDecimal B_tenure,BigDecimal B_loamamount) {
		String loanAmt_DaysDiff="0";
		try{
			BigDecimal PMTEMI=calcEMI(B_loamamount,B_tenure,B_intrate);

			double seedvalue=Math.round(PMTEMI.doubleValue());
			double loamamount=B_loamamount.doubleValue();
			int tenure=B_tenure.intValue();
			double intrate=(B_intrate.intValue())/100.0;	
			RLOS.mLogger.info("seedvalue  **************"+seedvalue);
			RLOS.mLogger.info("loamamount  **************"+loamamount);
			RLOS.mLogger.info("tenure=  **************"+tenure);
			RLOS.mLogger.info("intrate  **************"+intrate);

			int iterations=2*(int)Math.round(PMTEMI.intValue()*.10);
			RLOS.mLogger.info("PMTEMI   **"+PMTEMI+"  for intrate @"+intrate+ " iterations"+iterations);
			loanAmt_DaysDiff=Double.toString(seedvalue);
		}
		catch(Exception e){
			RLOS.mLogger.info( "Exception occured in calcgoalseekEMI() : ");
			RLOS.logException(e);
			loanAmt_DaysDiff="0";
		}

		return loanAmt_DaysDiff;
	}
	public static double Cas_Limit(double aff_emi,double rate,double tenureMonths)
	{
		double pmt;
		try{
			double new_rate = (rate/100)/12;
			 pmt = (aff_emi)*(1-Math.pow(1+new_rate,-tenureMonths))/new_rate;
			RLOS.mLogger.info("final_rate_new 1ST pmt11 : " + pmt);
		}
		catch(Exception e){
			RLOS.logException(e);
			pmt=0;
		}
		return pmt;
		
	}
	//Akshay for comma Change
	public String putComma(String field){
		RLOS.mLogger.info( "Inside putComma()"+field);
		String limit=field.replaceAll(",","");
		double amount = Double.parseDouble(limit);
	DecimalFormat myFormatter = new DecimalFormat("#,###.000");
	String convString=myFormatter.format(amount);
		RLOS.mLogger.info( "Inside putComma(): Coverted String is:"+convString);
		return convString;
	}
	//Akshay for comma Change
	// Deepak Change to calculate EMI END
	
	public static String Convert_dateFormat(String date,String Old_Format,String new_Format)
	{
		RLOS.mLogger.info( "Inside Convert_dateFormat()"+date);
		String new_date="";
		if("".equals(date))
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
			RLOS.mLogger.info( "Exception occurred in parsing date:"+e.getMessage());
			RLOS.logException(e);
		}
		return new_date;
	}
	
	public void  ParentToChild(){
		RLOS.mLogger.info("RLOSCommon Inside ParentTochild() ");

		FormReference formObject=FormContext.getCurrentInstance().getFormReference();
		String mystring="";
		String product="";

		try{
			int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			for(int i=0;i<n;i++)	
			{
				mystring=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1);
				RLOS.mLogger.info("Inside ParentTochild() mystring:"+mystring);
				if(!product.contains(mystring))
					product+=mystring;			
			}

			formObject.setNGValue("Product_Type", product);
			RLOS.mLogger.info("Inside ParentToChild -> n :"+n+"Product_Type is:"+product);
		}
		catch(Exception e)
		{
			RLOS.mLogger.info("Inside ParentToChild ->Exception occurred:"+e.getMessage());
			RLOS.logException(e);
		}
	}


	public String fetch_cust_details_primary(){
		String outputResponse="";
		String ReturnCode="";
		String alert_msg="";
		RLOS_Integration_Input GenXml=new RLOS_Integration_Input();
		try{

			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			outputResponse = GenXml.GenerateXML("CUSTOMER_DETAILS","Primary_CIF");
			RLOS.mLogger.info("Inside Customer");
			String Gender =  (outputResponse.contains("<Gender>")) ? outputResponse.substring(outputResponse.indexOf("<Gender>")+"</Gender>".length()-1,outputResponse.indexOf("</Gender>")):"";
			RLOS.mLogger.info(Gender);
			String  Marital_Status =  (outputResponse.contains("<MaritialStatus>")) ? outputResponse.substring(outputResponse.indexOf("<MaritialStatus>")+"</MaritialStatus>".length()-1,outputResponse.indexOf("</MaritialStatus>")):"";
			RLOS.mLogger.info(Marital_Status);
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			RLOS.mLogger.info(ReturnCode);

			if("0000".equalsIgnoreCase(ReturnCode) ){
				//added by saurabh on 19th july.
				setcustomer_enable();
				formObject.setNGValue("Is_Customer_Details", "Y");
				alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL006");
				formObject.setVisible("Customer_Frame2", true);
				formObject.setNGValue("cmplx_Customer_IscustomerSave", "Y");
				formObject.setLocked("Customer_Button1", false);
				formObject.setNGValue("Cust_Name", formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+ formObject.getNGValue("cmplx_Customer_MiddleName")+" "+ formObject.getNGValue("cmplx_Customer_LAstNAme"));
				formObject.setNGValue("Cif_Id", formObject.getNGValue("cmplx_Customer_CIFNO"));
				//code to enter started difference of years+month in UAE field
				String ResideSince =  (outputResponse.contains("<ResideSince>")) ? outputResponse.substring(outputResponse.indexOf("<ResideSince>")+"</ResideSince>".length()-1,outputResponse.indexOf("</ResideSince>")):"";    
				RLOS.mLogger.info(ResideSince);
				if(!"".equalsIgnoreCase(ResideSince)){
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					Calendar cal = Calendar.getInstance();
					int CurrentYear=cal.get(Calendar.YEAR);
					int CurrentMonth=cal.get(Calendar.MONTH)+1;
					int CurrentDate=cal.get(Calendar.DATE);
					RLOS.mLogger.info(CurrentYear+"-"+CurrentMonth+"-"+CurrentDate);

					Date d1 = null;
					Date d2 = null;
					try {
						d1 = format.parse(ResideSince);
						d2 = format.parse(CurrentYear+"-"+CurrentMonth+"-"+CurrentDate);
					}
					catch (Exception ex) {
						RLOS.logException(ex);
					}
					int totalDays = (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
					int diffYear = totalDays/365;
					int diffdays = totalDays%365;
					int diffMonths=0;
					if(diffdays>=30) {
						diffMonths = diffdays/30;
						diffdays = diffdays%30;
					}
					RLOS.mLogger.info("year difference"+diffYear);
					RLOS.mLogger.info("diffMonths difference"+diffMonths);
					RLOS.mLogger.info("diffdays difference"+diffdays);
					formObject.setNGValue("cmplx_Customer_yearsInUAE",diffYear+"."+diffMonths);
					RLOS.mLogger.info("diffdays difference"+formObject.getNGValue("cmplx_Customer_yearsInUAE")); 
				}
				try{
					//added By Aman-28/6/17 for checking aecb checkbox
					String AECBheld =  (outputResponse.contains("<AECBConsentHeld>")) ? outputResponse.substring(outputResponse.indexOf("<AECBConsentHeld>")+"</AECBConsentHeld>".length()-1,outputResponse.indexOf("</AECBConsentHeld>")):"";
					if ("Y".equalsIgnoreCase(AECBheld))
					{
						RLOS.mLogger.info( "Inside AECB Consent true check!!");
						formObject.fetchFragment("Liability_container", "Liability_New", "q_LiabilityNew");
						formObject.setNGValue("cmplx_Liability_New_AECBconsentAvail", "true");
						formObject.setLocked("Liability_New_fetchLiabilities",false);
					}
					else 
					{
						formObject.setNGValue("cmplx_Liability_New_AECBconsentAvail", "false");
					}
					//ended By Aman-28/6/17 for checking aecb checkbox


					//code to enter started difference of years+month in UAE field

					formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmploymentDetails");
					formObject.fetchFragment("Address_Details_Container", "AddressDetails","q_AddressDetails");   
					formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
					formObject.fetchFragment("FATCA", "FATCA", "q_FATCA");
					formObject.fetchFragment("KYC", "KYC", "q_KYC");
					formObject.fetchFragment("OECD", "OECD", "q_OECD");
					formObject.fetchFragment("CompanyDetails", "CompanyDetails", "q_CompanyDetails");
					formObject.setTop("Alt_Contact_container",formObject.getTop("Address_Details_Container")+formObject.getHeight("Address_Details_Container"));
					
					formObject.setTop("CardDetails", formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container")+10);
					RLOS.mLogger.info("height of supplement is: "+formObject.getHeight("Supplementary_Container"));
					formObject.setTop("Supplementary_Container", formObject.getTop("CardDetails")+80);  
					formObject.setTop("FATCA", formObject.getTop("Supplementary_Container")+30);

					formObject.setTop("KYC", formObject.getTop("FATCA")+formObject.getHeight("FATCA")+10);
					formObject.setTop("OECD", formObject.getTop("KYC")+formObject.getHeight("KYC")+20);
					formObject.setTop("ReferenceDetails", formObject.getTop("OECD")+formObject.getHeight("OECD")+30);
					formObject.setTop("AuthorisedSignatoryDetails", formObject.getTop("CompanyDetails")+formObject.getHeight("CompanyDetails")+20);
					formObject.setTop("PartnerDetails", formObject.getTop("AuthorisedSignatoryDetails")+formObject.getHeight("AuthorisedSignatoryDetails")+20);
					
					formObject.setTop("Eligibility_Emp",470);
					formObject.setTop("WorldCheck_fetch",520);
					RLOS_Integration_Output.valueSetCustomer(outputResponse,"Primary_CIF");
					//code to set Emirates of residence start.
					//code added to load the picklist of Employer details and address
					
					loadPicklist_Address();
				}
				catch(Exception ex){
					RLOS.logException(ex);
				}
				
				int n=formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
				if(n>0)
				{
					for(int i=0;i<n;i++)
					{
						RLOS.mLogger.info(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0));
						if("RESIDENCE".equalsIgnoreCase(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0)))
						{
							RLOS.mLogger.info(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0));
							formObject.setNGValue("cmplx_Customer_EmirateOfResidence",formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 6));
						}
					}
				}
				//code to set Emirates of residence End.
				//code change to save the date in desired format.

				try{
					RLOS.mLogger.info(formObject.getNGValue("cmplx_Customer_DOb")+" cmplx_Customer_PassPortExpiry: "+formObject.getNGValue("cmplx_Customer_PassPortExpiry")+" cmplx_Customer_EmirateIDExpiry: "+formObject.getNGValue("cmplx_Customer_EmirateIDExpiry")+" VIsaExpiry: "+ formObject.getNGValue("cmplx_Customer_VIsaExpiry")+" cmplx_Customer_IdIssueDate: "+ formObject.getNGValue("cmplx_Customer_IdIssueDate"));
					String str_dob=formObject.getNGValue("cmplx_Customer_DOb");
					String str_IDissuedate=formObject.getNGValue("cmplx_Customer_IdIssueDate");
					String str_passExpDate=formObject.getNGValue("cmplx_Customer_PassPortExpiry");
					String str_visaExpDate=formObject.getNGValue("cmplx_Customer_VIsaExpiry");
					String str_EIDExpDate=formObject.getNGValue("cmplx_Customer_EmirateIDExpiry");
					
					if(str_dob!=null && !"".equalsIgnoreCase(str_dob)){
						formObject.setNGValue("cmplx_Customer_DOb",Convert_dateFormat(str_dob, "yyyy-mm-dd", "dd/mm/yyyy"),false);
					}
					if(str_IDissuedate!=null&&!"".equalsIgnoreCase(str_IDissuedate)){
						formObject.setNGValue("cmplx_Customer_IdIssueDate",Convert_dateFormat(str_IDissuedate, "yyyy-mm-dd", "dd/mm/yyyy"),false);
					}
					if(str_passExpDate!=null&&!"".equalsIgnoreCase(str_passExpDate)){
						formObject.setNGValue("cmplx_Customer_PassPortExpiry",Convert_dateFormat(str_passExpDate, "yyyy-mm-dd", "dd/mm/yyyy"),false);
					}
					if(str_visaExpDate!=null&&!"".equalsIgnoreCase(str_visaExpDate)){
						formObject.setNGValue("cmplx_Customer_VIsaExpiry",Convert_dateFormat(str_visaExpDate, "yyyy-mm-dd", "dd/mm/yyyy"),false);
					}
					if(str_EIDExpDate!=null&&!"".equalsIgnoreCase(str_EIDExpDate)){
						formObject.setNGValue("cmplx_Customer_EmirateIDExpiry",Convert_dateFormat(str_EIDExpDate, "yyyy-mm-dd", "dd/mm/yyyy"),false);
					}

					String mobileNo = formObject.getNGValue("cmplx_Customer_MobNo");
					if(mobileNo!=null && !"".equalsIgnoreCase(mobileNo)){
						formObject.setNGValue("OTP_Mobile_NO", mobileNo);
						formObject.setEnabled("OTP_Mobile_NO", false);
					}
				}
				catch(Exception e)
				{
					RLOS.logException(e);
					RLOS.mLogger.info("Exception Occured: "+e.getMessage());
					popupFlag = "Y";
					RLOS.mLogger.info("inside Customer Eligibility to through Exception to Exit:");
					alert_msg = NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL006");
					throw new ValidatorException(new FacesMessage(alert_msg));
				}
			}
			else{
				alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL046");
				formObject.setNGValue("Is_Customer_Details","N");
			}
		}
		catch(Exception e)
		{
			RLOS.logException(e);
			RLOS.mLogger.info( "Exception occured in fetch_cust_details_primary method"+e.getMessage());
			if("".equalsIgnoreCase(alert_msg)){
				alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL047");
			}

		}
		return alert_msg;
	}	

	public String fetch_cust_details_supplementary(){
		String outputResponse="";
		String ReturnCode="";
		String alert_msg="";
		RLOS_Integration_Input GenXml=new RLOS_Integration_Input();
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			outputResponse = GenXml.GenerateXML("CUSTOMER_DETAILS","Supplementary_Card_Details");
			RLOS.mLogger.info("Inside Customer");
			/*String Gender =  (outputResponse.contains("<Gender>")) ? outputResponse.substring(outputResponse.indexOf("<Gender>")+"</Gender>".length()-1,outputResponse.indexOf("</Gender>")):"";
			RLOS.mLogger.info(Gender);
			String  Marital_Status =  (outputResponse.contains("<MaritialStatus>")) ? outputResponse.substring(outputResponse.indexOf("<MaritialStatus>")+"</MaritialStatus>".length()-1,outputResponse.indexOf("</MaritialStatus>")):"";
			RLOS.mLogger.info(Marital_Status);*/
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			RLOS.mLogger.info(ReturnCode);

			if("0000".equalsIgnoreCase(ReturnCode) ){
			RLOS_Integration_Output.valueSetCustomer(outputResponse,"Supplementary_Card_Details");
				alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL006");

				String Date1=formObject.getNGValue("DOB");
				RLOS.mLogger.info(Date1);
				SimpleDateFormat sdf1=new SimpleDateFormat("dd-mm-yyyy");
				SimpleDateFormat sdf2=new SimpleDateFormat("dd/mm/yyyy");
				String Datechanged=sdf2.format(sdf1.parse(Date1));
				RLOS.mLogger.info(Datechanged);
				formObject.setNGValue("DOB",Datechanged,false);
			}
			else{
				alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL046");
			}

		}catch(Exception ex){
			RLOS.mLogger.info( "Exception occured in fetch_cust_details_primary method"+printException(ex));
			if("".equalsIgnoreCase(alert_msg)){
				alert_msg=NGFUserResourceMgr_RLOS.getResourceString_rlosValidations("VAL047");
			}
		}
		return alert_msg;
	}
	
	

}
