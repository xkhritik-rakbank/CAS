package com.newgen.omniforms.user;

import com.newgen.custom.XMLParser;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.IRepeater;
import com.newgen.omniforms.component.PickList;
import com.newgen.omniforms.component.render.ListViewRenderer;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.excp.CustomExceptionHandler;
import com.newgen.omniforms.skutil.*;
import com.newgen.wfdesktop.xmlapi.WFCallBroker;
import com.newgen.wfdesktop.xmlapi.WFInputXml;

import java.util.List;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Calendar;
import java.util.Map;
import java.util.LinkedHashMap;

import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.text.DateFormat;
import java.text.ParseException;


public class RLOSCommon extends Common {
	HashMap<String,String> hm= new HashMap<String,String>(); // not a nullable HashMap
	String popupFlag="";
	public void getAge(String dateBirth){
		hm.put("Liability_New_Button1","Clicked");
		popupFlag="N";
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			SKLogger.writeLog("RLOS_Common", "Inside getAge(): "); 


			if (dateBirth.contains("/")){
				String parts[] = dateBirth.split("/");
				Calendar dob = Calendar.getInstance();
				Calendar today = Calendar.getInstance();

				dob.set(Integer.parseInt(parts[2]), Integer.parseInt(parts[1])-1,Integer.parseInt(parts[0])); 

				Integer age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

				if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
					age--; 
				}
				SKLogger.writeLog("RLOS_Common", "Values are with /: "+parts[2]+parts[1]+parts[0]+" age: "+age); 


				formObject.setNGValue("cmplx_Customer_age",age.toString(),false); 
			}
			else if(dateBirth.contains("-")) {
				String parts[] = dateBirth.split("-");
				Calendar dob = Calendar.getInstance();
				Calendar today = Calendar.getInstance();

				dob.set(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])-1,Integer.parseInt(parts[2])); 

				Integer age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

				if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
					age--; 
				}
				SKLogger.writeLog("RLOS_Common", "Values are with-: "+parts[2]+parts[1]+parts[0]+" age: "+age); 


				formObject.setNGValue("cmplx_Customer_age",age.toString(),false); 
			}
		}
		catch(Exception e){
			try{ throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));}finally{hm.clear();}
		}

	}

	public void loadPicklistCustomer()  
	{
		SKLogger.writeLog("RLOS_Common", "Inside loadPicklistCustomer: ");
		LoadPickList("cmplx_Customer_Nationality", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
		LoadPickList("cmplx_Customer_Title", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_title with (nolock) order by code");
		LoadPickList("cmplx_Customer_SecNationality", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
		LoadPickList("cmplx_Customer_COuntryOFResidence", "select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from ng_MASTER_Country order by Code");	
		LoadPickList("cmplx_Customer_MAritalStatus", "select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_Maritalstatus order by Code");
		LoadPickList("cmplx_Customer_EmirateOfResidence", "select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_emirate order by Code");
		LoadPickList("cmplx_Customer_EMirateOfVisa", "select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_emirate order by Code");
	}
	public static String Convert_dateFormat(String date,String Old_Format,String new_Format)
	{
		SKLogger.writeLog("RLOS Common ", "Inside Convert_dateFormat()"+date);
		String new_date="";
		if(date.equals(null) || date.equals(""))
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
			SKLogger.writeLog("RLOS Common ", "Exception occurred in parsing date:"+e.getMessage());
		}
		return new_date;
	}

	public void loadPicklistELigibiltyAndProductInfo()  
	{
		LoadPickList("cmplx_EligibilityAndProductInfo_RepayFreq", "select '--Select--' union select convert(varchar, description) from NG_MASTER_frequency with (nolock)");
		LoadPickList("cmplx_EligibilityAndProductInfo_InterestType", "select '--Select--' union select convert(varchar, description) from NG_MASTER_InterestType");
		LoadPickList("cmplx_EligibilityAndProductInfo_InstrumentType", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_InstrumentType with (nolock) where isactive = 'Y'  order by code");
	}


	public void loadPicklistProduct(String ReqProd)
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		SKLogger.writeLog("RLOS_Common", "Inside loadPicklistProduct$"+ReqProd+"$");
		String ProdType=formObject.getNGValue("Product_type");

		if(ReqProd.equalsIgnoreCase("Personal Loan")){
			SKLogger.writeLog("RLOS_Common", "Inside equalsIgnoreCase()"); 
			formObject.setVisible("Scheme", true);
			formObject.setLeft("Scheme", 555);
			formObject.clear("SubProd");
			formObject.setNGValue("SubProd", "--Select--");
			formObject.clear("Scheme");
			formObject.setNGValue("Scheme", "--Select--");
			LoadPickList("SubProd", "select '--select--','' as code union select convert(varchar(50),description),code  from ng_MASTER_SubProduct_PL   where WorkstepName = '"+formObject.getWFActivityName()+"' order by code");
			LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar,SchemeDesc),SCHEMEID from ng_MASTER_Scheme with (nolock) where SchemeDesc like 'P_%' order by SCHEMEID");
			LoadPickList("EmpType", " select convert(varchar, description),code from ng_MASTER_PRD_EMPTYPE where SubProduct = '"+ReqProd+"' order by code");

		}
		else if(ReqProd.equalsIgnoreCase("Credit Card")){
			formObject.setVisible("CardProd", true);
			formObject.clear("CardProd");
			formObject.clear("SubProd");
			formObject.setNGValue("SubProd", "--Select--");
			LoadPickList("SubProd", "select '--Select--','' as code union select  convert(varchar,description),code from ng_master_Subproduct_CC with (nolock) order by code");
			LoadPickList("EmpType", "select convert(varchar, description),code from ng_MASTER_PRD_EMPTYPE where SubProduct = '"+ReqProd+"' order by code");

			if(ProdType.equalsIgnoreCase("Islamic"))
				LoadPickList("CardProd", "select '--Select--','' as code union select convert(varchar,description),code from ng_MASTER_CardProduct with (nolock) where reqProduct='Islamic' order by code");
			else
				LoadPickList("CardProd", "select '--Select--','' as code union select convert(varchar,description),code from ng_MASTER_CardProduct with (nolock) where reqProduct='Conventional'  order by code");

			formObject.setVisible("Scheme", false);
		}
		else{
			formObject.clear("SubProd");
			formObject.setNGValue("SubProd", "--Select--");
		}
	}

	public void loadPicklist_Company()
	{
		SKLogger.writeLog("RLOS_Common", "Inside loadPicklist_Address: "); 
		LoadPickList("CompanyDetails_appType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_ApplicantType with (nolock) order by code");
        LoadPickList("CompanyDetails_indusSector", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_IndustrySector with (nolock) order by code");
        LoadPickList("CompanyDetails_indusMAcro", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_EmpIndusMacro with (nolock) order by code");
        LoadPickList("CompanyDetails_indusMicro", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_EmpIndusMicro with (nolock) order by code");
        LoadPickList("CompanyDetails_legalEntity", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_LegalEntity with (nolock) order by code");
        LoadPickList("CompanyDetails_desig", "select '--Select--'as description,'' as code union select convert(varchar, description),code from NG_MASTER_Designation with (nolock) order by code");
        LoadPickList("CompanyDetails_desigVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_Designation with (nolock) order by code");
        LoadPickList("CompanyDetails_EOW", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_Emirate with (nolock) order by code");
        LoadPickList("CompanyDetails_headOffice", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_Emirate with (nolock) order by code");
        LoadPickList("CompanyDetails_TLPlaceIssue", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_master_TradeLicensePlace with (nolock) order by code");
}
	
	public void loadPicklist_Address()
	{
		SKLogger.writeLog("RLOS_Common", "Inside loadPicklist_Address: "); 
		LoadPickList("addtype", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_AddressType with (nolock) order by code");
		LoadPickList("city", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_city with (nolock) order by code");
		LoadPickList("state", "select '--Select--'as description,'' as code union select convert(varchar, description),code from NG_MASTER_state with (nolock) order by code");
		LoadPickList("country", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
	}

	public void loadPicklist_ServiceRequest()
	{
		SKLogger.writeLog("RLOS_Common", "Inside loadPicklist_ServiceRequest: "); 
		LoadPickList("cmplx_CC_Loan_transType", "select '--Select--','' as code union select convert(varchar, description),code from NG_MASTER_TransactionType with (nolock) order by code");
		LoadPickList("cmplx_CC_Loan_TransMode", "select '--Select--','' as code union select convert(varchar, description),code from NG_MASTER_TransferMode with (nolock) order by code");
		LoadPickList("cmplx_CC_Loan_DispatchChannel", "select '--Select--','' as code union select convert(varchar, description),code from NG_MASTER_DispatchChannel with (nolock) order by code");
		LoadPickList("cmplx_CC_Loan_MarketingCode", "select '--Select--','' as code union select convert(varchar, description),code from NG_MASTER_MarketingCode with (nolock) order by code");
		LoadPickList("cmplx_CC_Loan_SourceCode", "select '--Select--','' as code union select convert(varchar, description),code from NG_MASTER_sourceCode with (nolock) order by code");
		LoadPickList("cmplx_CC_Loan_appstatus", "select '--Select--','' as code union select convert(varchar, description),code from ng_MASTER_ApplicationStatus with (nolock) order by code");
		LoadPickList("cmplx_CC_Loan_approvalcode", "select '--Select--','' as code union select convert(varchar, description),code from NG_MASTER_ApprovalCode with (nolock) order by code");
		LoadPickList("cmplx_CC_Loan_mchequestatus", "select '--Select--','' as code union select convert(varchar, description),code from NG_MASTER_MChequeStatus with (nolock) order by code");
		LoadPickList("cmplx_CC_Loan_DDSMode", "select '--Select--','' as code union select convert(varchar, description),code from NG_MASTER_DDSMode with (nolock) order by code");
		LoadPickList("cmplx_CC_Loan_AccType", "select '--Select--','' as code union select convert(varchar, description),code from NG_MASTER_AccountType with (nolock) order by code");
		LoadPickList("cmplx_CC_Loan_DDSBankAName", "select '--Select--','' as code union select convert(varchar, description),code from NG_MASTER_BankName with (nolock) order by code");
		LoadPickList("cmplx_CC_Loan_ModeOfSI", "select '--Select--','' as code union select convert(varchar, description),code from NG_MASTER_ModeOfSI with (nolock) order by code");
		LoadPickList("cmplx_CC_Loan_VPSPAckage", "select '--Select--','' as code union select convert(varchar, description),code from NG_MASTER_VPSPackage with (nolock) order by code");
		LoadPickList("cmplx_CC_Loan_VPSSourceCode", "select '--Select--','' as code union select convert(varchar, description),code from NG_MASTER_sourceCode with (nolock) order by code");
		LoadPickList("cmplx_CC_Loan_StartMonth", "select '--Select--','' as code union select convert(varchar, description),code from NG_MASTER_Month with (nolock) order by code");
	}


	public void loadPicklist4()    
	{
		SKLogger.writeLog("RLOSCommon", "Inside loadpicklist4:"); 
		//LoadPickList("cmplx_EmploymentDetails_ApplicationCateg", "select '--Select--' union select  convert(varchar,description) from NG_MASTER_ApplicatonCategory with (nolock)");
		LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) order by code");
		LoadPickList("cmplx_EmploymentDetails_Designation", "select '--Select--' as description,'' as code union select  convert(varchar, description),code from NG_MASTER_Designation with (nolock) order by code");
		LoadPickList("cmplx_EmploymentDetails_DesigVisa", "select '--Select--' as description,'' as code union select  convert(varchar, description),code from NG_MASTER_Designation with (nolock) order by code");
		LoadPickList("cmplx_EmploymentDetails_Emp_Type", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_EmploymentType with (nolock) order by code");
		LoadPickList("cmplx_EmploymentDetails_EmpStatus", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_EmploymentStatus with (nolock) order by code");
		LoadPickList("cmplx_EmploymentDetails_EmirateOfWork", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_state with (nolock) order by code");
		LoadPickList("cmplx_EmploymentDetails_HeadOfficeEmirate", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_state with (nolock) order by code");
		LoadPickList("cmplx_EmploymentDetails_tenancntrct", "select '--Select--' union select convert(varchar, description) from NG_MASTER_state with (nolock)");
		LoadPickList("cmplx_EmploymentDetails_EmpIndusSector", "select '--Select--' union select convert(varchar, description) from NG_MASTER_IndustrySector with (nolock)");
		LoadPickList("cmplx_EmploymentDetails_EmpContractType", "select '--Select--' union select convert(varchar, description) from NG_MASTER_EmpContractType with (nolock)");
		LoadPickList("cmplx_EmploymentDetails_channelcode","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_ChannelCode where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_IndusSeg", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_IndustrySegment  where isActive='Y'  order by code");

	}

	public void loadPicklist3()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		try
		{
			SKLogger.writeLog("RLOSCommon", "Inside loadpicklist3:");
			String Query = "select '--Select--' union select  convert(varchar,Decision) from NG_MASTER_Decision with (nolock) where ProcessName='RLOS' and workstepname='"+formObject.getWFActivityName()+"'";
			SKLogger.writeLog("RLOSCommon Load desison Drop down: ",Query );
			LoadPickList("cmplx_DecisionHistory_Decision", Query);
		}
		catch(Exception e){ SKLogger.writeLog("PLCommon","Exception Occurred loadPicklist3 :"+e.getMessage());}
	}


	// added by yash on 27/7/2017 for employmnent tabs visible check
	public void Field_employment()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String empid="AVI,MED,EDU,HOT,PROM";
		if(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode").equalsIgnoreCase("NEP")){

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

		else if(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode").equalsIgnoreCase("FZD") ||formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode").equalsIgnoreCase("FZE")){
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

		else if(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode").equalsIgnoreCase("TEN"))
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

		else if(formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg").equalsIgnoreCase("Surrogate") && empid.indexOf(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode"))>-1)
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

	// ended by yash on 27/7/2017 


	public void Fields_ApplicationType_Employment()
	{
		SKLogger.writeLog("RLOSCommon", "Inside Fields_ApplicationType_Employment:"); 

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(n>0){
			for(int i=0;i<n;i++){
				if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,4).equalsIgnoreCase("RESCE") && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")){
					formObject.setVisible("EMploymentDetails_Label36",true); 
					formObject.setVisible("cmplx_EmploymentDetails_channelcode",true); 
					break;
				}
				else{
					formObject.setVisible("EMploymentDetails_Label36",false); 
					formObject.setVisible("cmplx_EmploymentDetails_channelcode",false); 
				}
			}
		}
	}


	public void Fields_Liabilities()
	{
		SKLogger.writeLog("RLOSCommon", "Inside Fields_Liabilities:"); 
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(n>0){
			for(int i=0;i<n;i++){
				SKLogger.writeLog("RLOS", "Grid Data[2][9] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9));
				if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("Business titanium Card") || formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("Self Employed Credit Card")&& formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")){
					formObject.setVisible("Button2",false);
					formObject.setVisible("Label9",false);
					formObject.setVisible("Liability_New_Label21",false); 
					formObject.setVisible("Liability_New_Label14",false);
					formObject.setVisible("cmplx_Liability_New_DBR",false);
					formObject.setVisible("cmplx_Liability_New_DBRNet",false); 
					formObject.setVisible("Label15",false);
					formObject.setVisible("cmplx_Liability_New_TAI",false); 
					break;
				}

			}	
		}

		/*else{
			formObject.setVisible("Button2",true);
			formObject.setVisible("Label9",true);
			formObject.setVisible("Liability_New_Label21",true); 
			formObject.setVisible("Liability_New_Label14",true);
			formObject.setVisible("cmplx_Liability_New_DBR",true);
			formObject.setVisible("cmplx_Liability_New_DBRNet",true); 
			}*/
	}

	public void Fields_ApplicationType_Eligibility()
	{
		SKLogger.writeLog("RLOSCommon", "Inside Fields_ApplicationType_Eligibility:"); 

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(n>0){
			for(int i=0;i<n;i++){
				if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,4).equalsIgnoreCase("Top up") && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")){
					formObject.setLocked("cmplx_EligibilityAndProductInfo_NetPayout",false); 
					break;
				}
				else{
					formObject.setLocked("cmplx_EligibilityAndProductInfo_NetPayout",true); 
				}
			}
		}
	}

	public void  Fields_Eligibility() 
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(n>0){
			for(int i=0;i<n;i++){

				if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1).equalsIgnoreCase("Personal Loan") && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")){
					formObject.setVisible("ELigibiltyAndProductInfo_Label39",true);
					//formObject.setVisible("ELigibiltyAndProductInfo_Label40",true);
					formObject.setVisible("cmplx_EligibilityAndProductInfo_instrumenttype",true);

					if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,4).equalsIgnoreCase("Take Over"))
					{
						formObject.setVisible("ELigibiltyAndProductInfo_Label1",true);
						formObject.setVisible("cmplx_EligibilityAndProductInfo_TakeoverAMount",true);
						formObject.setVisible("ELigibiltyAndProductInfo_Label2",true);
						formObject.setVisible("cmplx_EligibilityAndProductInfo_takeoverBank",true);

					}
				}

				else{

					if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("Instant Money") && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")){
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
						formObject.setVisible("ELigibiltyAndProductInfo_Label31",false);
						formObject.setVisible("ELigibiltyAndProductInfo_Label15",false);
						formObject.setVisible("ELigibiltyAndProductInfo_Label28",false);
						formObject.setVisible("ELigibiltyAndProductInfo_Label17",false);
						formObject.setVisible("ELigibiltyAndProductInfo_Label10",false);
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
						formObject.setVisible("ELigibiltyAndProductInfo_Label37",false);
						formObject.setVisible("ELigibiltyAndProductInfo_Label22",false);
						formObject.setVisible("ELigibiltyAndProductInfo_Label38",false);

					}	

					else if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("Salaried Credit Card") && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")){
						Eligibility_Hide();
						formObject.setVisible("ELigibiltyAndProductInfo_Label41",true); 
						formObject.setVisible("cmplx_EligibilityAndProductInfo_FinalTAI",true);
						break;
					}

					else if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("Business titanium Card")  || formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("Self Employed Credit Card") && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")){
						formObject.setVisible("cmplx_EligibilityAndProductInfo_FinalDBR",false);
						formObject.setVisible("ELigibiltyAndProductInfo_Label3",false);
						Eligibility_Hide();					
						break;
					}
					else if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("Limit Increase")  || formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("Salaried Credit Card") && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")){
						Eligibility_Hide();
						break;
					}

					else if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("Product Upgrade")  && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")){ 
						Eligibility_Hide();
						break;
					}
					else
						Eligibility_UnHide();

				}	
			}
		}	
		else{
			formObject.setVisible("cmplx_EligibilityAndProductInfo_FinalDBR",true);
			formObject.setVisible("ELigibiltyAndProductInfo_Label3",true);
			formObject.setVisible("ELigibiltyAndProductInfo_Label39",false);
			//formObject.setVisible("ELigibiltyAndProductInfo_Label40",false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_instrumenttype",false);
			Eligibility_UnHide();
		}
	}

	public void  Fields_ServiceRequest()
	{
		SKLogger.writeLog("RLOSCommon", "Inside Fields_ServiceRequest:"); 
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(n>0){
			for(int i=0;i<n;i++){
				SKLogger.writeLog("grid[2],grid[9]:", formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9)); 
				if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("Instant Money") || formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("Salaried Credit Card") && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")){
					SKLogger.writeLog("RLOS", "hi");
					formObject.setVisible("CC_Loan_Frame3",false); 
					formObject.setVisible("CC_Loan_Frame5",true); 
					break;
				}
				else if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).contains("Business titanium Card") || formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("Self Employed Credit Card") && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")){
					SKLogger.writeLog("RLOS", "hello");
					formObject.setVisible("CC_Loan_Frame3",false);
					formObject.setVisible("CC_Loan_Frame5",true); 					
					break;
				}
				else if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("Limit Increase") && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")){
					SKLogger.writeLog("RLOS", "Bye");
					formObject.setVisible("CC_Loan_Frame5",false); 
					formObject.setVisible("CC_Loan_Frame3",false); 
					break;
				}
				else if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("Product Upgrade") && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")){
					SKLogger.writeLog("RLOS", "PU");
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
		SKLogger.writeLog("RLOSCommon", "Inside Eligibility_Hide:"); 
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
		formObject.setVisible("ELigibiltyAndProductInfo_Label37",false);
		formObject.setVisible("ELigibiltyAndProductInfo_Label38",false);	
	}

	public void Eligibility_UnHide()
	{
		SKLogger.writeLog("RLOSCommon", "Inside Eligibility_UnHide():"); 
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
		formObject.setVisible("ELigibiltyAndProductInfo_Label37",true);
		formObject.setVisible("ELigibiltyAndProductInfo_Label38",true);	
	}

	public void loadInCardGrid()
	{

		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		SKLogger.writeLog("RLOS ","Inside  ->loadInCardGrid()");
		String WI_Name = formObject.getNGValue("WIname");
		String query="select CardType,CardNo,CardStatus,CardLimit,Outstanding,ConsiderforObligations,QCAvailed,QCAmtorEMI,LastPaidDate,Worststatusdate,AECBExcepRemarks,Liabililtytype,Status,RequestedDate,RequestedAmount,MOBforIM,NoofRepayments,SettledAmount,TypeofSettlement,encryp_CardNo,card_wi_name,ConsiderForApplication from ng_RLOS_GR_CardExternalLiability with (nolock) where card_wi_name='"+WI_Name+"'";
		List<List<String>> list=formObject.getDataFromDataSource(query);
		SKLogger.writeLog("RLOS query is:",query);
		AesUtil myutil=new AesUtil();
		for(List<String> mylist : list)
		{
			String CardType=mylist.get(0);
			//String CardNo=mylist.get(1);
			String CardStatus=mylist.get(2);
			String CardLimit=mylist.get(3);
			String Outstanding=mylist.get(4);
			String ConsiderforObligations=mylist.get(5);
			String QCAvailed=mylist.get(6);
			String QCAmtorEMI=mylist.get(7);
			//String LastPaidDate=mylist.get(8);
			String LastPaidDate="01/11/17";

			//String Worststatusdate=mylist.get(9);
			String Worststatusdate="01/11/17";
			String AECBExcepRemarks=mylist.get(10);
			String Liabililtytype=mylist.get(11);
			String Status=mylist.get(12);
			// String RequestedDate=mylist.get(13);
			String RequestedDate="01/11/17";
			String RequestedAmount=mylist.get(14);
			String MOBforIM=mylist.get(15);
			String NoofRepayments=mylist.get(16);
			String SettledAmount=mylist.get(17);
			String TypeofSettlement=mylist.get(18);
			String encryp_CardNo=mylist.get(19);
			String card_wi_name=mylist.get(20);
			String ConsiderForApplication=mylist.get(21);

			String decrypt_CardNo=myutil.Decrypt(encryp_CardNo);
			SKLogger.writeLog("RLOS decrypt_CardNo is:",decrypt_CardNo);
			List<String> tempList=new ArrayList<String>();
			tempList.add(CardType);
			tempList.add(decrypt_CardNo);
			tempList.add(CardStatus);
			tempList.add(CardLimit);
			tempList.add(Outstanding);
			tempList.add(ConsiderforObligations);
			tempList.add(QCAvailed);
			tempList.add(QCAmtorEMI);
			tempList.add(LastPaidDate);
			tempList.add(Worststatusdate);
			tempList.add(AECBExcepRemarks);
			tempList.add(Liabililtytype);
			tempList.add(Status);
			tempList.add(RequestedDate);
			tempList.add(RequestedAmount);
			tempList.add(MOBforIM);
			tempList.add(NoofRepayments);
			tempList.add(SettledAmount);
			tempList.add(TypeofSettlement);
			tempList.add(encryp_CardNo);
			tempList.add(card_wi_name);
			tempList.add(ConsiderForApplication);

			SKLogger.writeLog("RLOS TempList is:",tempList.toString());
			formObject.addItem("cmplx_ExternalLiabilities_cmplx_CardGrid", tempList);

		}
		// String query2="Select encryp_CardNo from ng_RLOS_GR_CardExternalLiability where card_wi_name='"+WI_Name+"'";
		// List<List<String>> list2=formObject.getDataFromDataSource(query2);


		// String CardNo=list2.get(0).get(0);
		//SKLogger.writeLog("RLOS CardNo is:",query);

		//SKLogger.writeLog("RLOS decrypt_CardNo is:",decrypt_CardNo);

		/* List<String> tempList=new ArrayList();
		 tempList.add(decrypt_CardNo);
		 SKLogger.writeLog("RLOS tempList is:",tempList.get(0));

		 list.add(1, tempList);
		 for(List<String> mylist : list)
			for(String mystring:mylist)
			 SKLogger.writeLog("RLOS mystring is:",mystring);


		//SKLogger.writeLog("PersonnalLoanS> ","Inside PLCommon ->loadInDecGrid()"+list.get(0).get(0)+list.get(0).get(1)+list.get(0).get(2)+list.get(0).get(3)+list.get(0).get(4));
		for (List<String> a : list) 
		{

			formObject.addItem("Decision_ListView1", a);
		}
		 */
	}

	public void TypeOfProduct()
	{
		SKLogger.writeLog("RLOSCommon", "Inside TypeOfProduct:"); 
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		for(int i=0;i<n;i++)
		{
			if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")){
				String Type_Prod=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,0);
				String req_Prod=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1);
				SKLogger.writeLog("RLOSCommon", "Loan_Type,req_Prod:"+Type_Prod+req_Prod); 
				if(req_Prod.equalsIgnoreCase("Personal Loan"))
					formObject.setNGValue("PL_LoanType",Type_Prod);
				else if(req_Prod.equalsIgnoreCase("Credit Card"))
					formObject.setNGValue("CC_LoanType",Type_Prod);
			}
		}
	}	


	public void saveIndecisionGrid(){
		try{
			SKLogger.writeLog("RLOS_Common", "Inside adddecisionGrid: "); 
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String currDate=new SimpleDateFormat("MM/dd/yyyy HH:mm").format(Calendar.getInstance().getTime());
			String query="insert into ng_rlos_gr_Decision values('"+currDate+"','"+formObject.getUserName()+"','"+formObject.getWFActivityName()+"','"+formObject.getNGValue("cmplx_DecisionHistory_Decision")+"','"+formObject.getNGValue("cmplx_DecisionHistory_Remarks")+"','"+formObject.getWFWorkitemName()+"')";

			SKLogger.writeLog("RLOS_Common","Query is"+query);
			formObject.saveDataIntoDataSource(query);

		}


		catch(Exception e)
		{
			SKLogger.writeLog("RLOS_Common", "Exception Occurred in adddecisionGrid: " + e.getMessage());
		}
	}

	public void AddPrimaryData()
	{
		SKLogger.writeLog("RLOS","RLOSCommon Inside AddPrimaryData() ");

		FormReference formObject=FormContext.getCurrentInstance().getFormReference();
		String LoanType="",ReqProd="",SubProd="",CardProd="";


		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		for(int i=0;i<n;i++)	
		{
			if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equals("Primary")){
				LoanType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,0);	
				ReqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1);
				SubProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2);
				CardProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,5);					
			}	
		}

		formObject.setNGValue("LoanType_Primary", LoanType);
		formObject.setNGValue("PrimaryProduct", ReqProd);
		formObject.setNGValue("Subproduct_productGrid", SubProd);
		formObject.setNGValue("CardProduct_Primary", CardProd);

		SKLogger.writeLog("RLOS","Inside ParentToChild -> n :"+n+"PrimaryData is:"+ReqProd+SubProd+CardProd);

	}


	public void  AddProducts(){
		SKLogger.writeLog("RLOS","RLOSCommon Inside ParentTochild() ");

		FormReference formObject=FormContext.getCurrentInstance().getFormReference();
		String mystring="",product="";

		try{
			int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			for(int i=0;i<n;i++)	
			{
				mystring=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1);
				SKLogger.writeLog("RLOS","Inside ParentTochild() mystring:"+mystring);
				if(!product.contains(mystring))
					product+=mystring;			
			}

			formObject.setNGValue("Product_Type", product);
			SKLogger.writeLog("RLOS","Inside ParentToChild -> n :"+n+"Product_Type is:"+product);
		}catch(Exception e){
			SKLogger.writeLog("RLOS","Inside ParentToChild ->Exception occurred:"+e.getMessage());

		}
	}



	public void CIFIDCheck(){

		FormReference formObject=FormContext.getCurrentInstance().getFormReference();
		SKLogger.writeLog("RLOS", "Inside CIFIDCheck()");
		String query="Select WIname from NG_RLOS_EXTTABLE with (nolock) WHERE introduction_date BETWEEN DATEADD(minute, -10, getdate()) AND getdate() and cifId = '"+formObject.getNGValue("cmplx_Customer_CIFNO")+"' AND WIname !='"+formObject.getWFWorkitemName()+"' order by Introduction_date desc";
		List<List<String>> list=formObject.getDataFromDataSource(query);
		String wiName=list.get(0).get(0);
		SKLogger.writeLog("RLOS", "QUERY is:"+query);
		SKLogger.writeLog("RLOS", "Data from DB:"+wiName);
		if(wiName!=null && wiName!="")
			throw new ValidatorException(new FacesMessage("Another instance for this CIFID was created less than 10 mins ago: "+wiName));
		else
			SKLogger.writeLog("RLOS", "No pipeline Case");
	}

	public void populatePickListWindow(String sQuery, String sControlName,String sColName, boolean bBatchingRequired, int iBatchSize)
	{

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PickList objPickList = formObject.getNGPickList(sControlName, sColName, bBatchingRequired, iBatchSize);

		if (sControlName.equalsIgnoreCase("EMploymentDetails_Button2")) 
			objPickList.setWindowTitle("Search Employer");

		objPickList.setHeight(600);
		objPickList.setWidth(800);
		objPickList.setVisible(true);
		objPickList.setSearchEnabled(true);
		objPickList.addPickListListener(new EventListenerHandler(objPickList.getClientId()));
		List<List<String>> result=formObject.getDataFromDataSource(sQuery);
		SKLogger.writeLog("EventListenerHandler: Result Of Query:",result.toString());   
		objPickList.populateData(result);
		formObject = null;

	}



	//code added here
	public String FinancialSummaryXML(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String Employment_type="";
		String requested_subproduct="";

		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		SKLogger.writeLog("INSIDE Financial Summary value_doc_name:" ,"valu of financial summary"+n);
		if(n>0)
		{
			for(int i=0;i<n;i++)
			{
				requested_subproduct= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2);
				SKLogger.writeLog("INSIDE Financial Summary value requested_subproduct" ,requested_subproduct);
				Employment_type= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 6);
				SKLogger.writeLog("INSIDE Financial Summary value Employment_type " ,Employment_type);

			}
		}
		SKLogger.writeLog("INSIDE INCOMING DOCUMENT value_doc_name outsid for:" ,requested_subproduct+","+Employment_type);

		String sQuery_header = "SELECT Operation_name from ng_rlos_Financial_Summary  with (nolock) where Description='"+requested_subproduct+"' and Employment='"+Employment_type+"' ";
		SKLogger.writeLog("RLOSCommon", "Financial_Summary"+sQuery_header);
		List<List<String>> OutputXML_header=formObject.getDataFromDataSource(sQuery_header);

		String Operation_name = OutputXML_header.get(0).get(0);
		SKLogger.writeLog("RLOSCommon", "Financial_Summary Operation_name"+Operation_name);
		return Operation_name;
	}
	//code changed here

	public String GenerateXML(String callName,String Operation_name)
	{
		SKLogger.writeLog("RLOSCommon", "Inside GenerateXML():");

		StringBuffer final_xml= new StringBuffer("");
		String header ="";
		String footer = "";
		String parentTagName="";
		Socket socket = null;
		OutputStream out = null;
		InputStream socketInputStream = null;
		DataOutputStream dout = null;
		DataInputStream din = null;
		String mqOutputResponse=null;
		String mqInputRequest=null;
		String cabinetName=null;
		String wi_name=null;
		String ws_name=null;
		String sessionID=null;
		String userName=null;
		String socketServerIP;
		String sQuery=null;
		Integer socketServerPort;
		String fin_call_name="Customer_details, Customer_eligibility,new_customer_req,new_account_req";
		SKLogger.writeLog("$$outputgGridtXML ","before try");
		try
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String sQuery_header = "SELECT Header,Footer,parenttagname FROM NG_Integration with (nolock) where Call_name='"+callName+"'";
			SKLogger.writeLog("RLOSCommon", "sQuery"+sQuery_header);
			List<List<String>> OutputXML_header=formObject.getDataFromDataSource(sQuery_header);
			if(!OutputXML_header.isEmpty()){
				SKLogger.writeLog("RLOSCommon header: ",OutputXML_header.get(0).get(0)+" footer: "+OutputXML_header.get(0).get(1)+" parenttagname: "+OutputXML_header.get(0).get(2));
				header = OutputXML_header.get(0).get(0);
				footer = OutputXML_header.get(0).get(1);
				parentTagName = OutputXML_header.get(0).get(2);
				String col_n = "call_type,Call_name,form_control,parent_tag_name,xmltag_name,is_repetitive,default_val,data_format";
				// String col_n = "call_type,Call_name,form_control,parent_tag_name,xmltag_name,is_repetitive,default_val";
				// String sQuery = "SELECT call_type, Call_name,form_control,parent_tag_name,xmltag_name,is_repetitive FROM NG_Integration_field_Mapping where Call_name='"+callName+"'ORDER BY tag_seq ASC" ;
				if(!(Operation_name.equalsIgnoreCase("") || Operation_name.equalsIgnoreCase(null))){   
					SKLogger.writeLog("inside if of operation","operation111"+Operation_name);
					SKLogger.writeLog("inside if of operation","callName111"+callName);
					sQuery = "SELECT "+col_n +" FROM NG_Integration_field_Mapping with (nolock) where Call_name='"+callName+"' and active = 'Y' and Operation_name='"+Operation_name+"' ORDER BY tag_seq ASC" ;
					SKLogger.writeLog("RLOSCommon", "sQuery "+sQuery);
				}
				else{
					SKLogger.writeLog("inside else of operation","operation"+Operation_name);
					sQuery = "SELECT "+col_n +" FROM NG_Integration_field_Mapping with (nolock) where Call_name='"+callName+"' and active = 'Y' ORDER BY tag_seq ASC" ;
					SKLogger.writeLog("RLOSCommon", "sQuery "+sQuery);
				}

				List<List<String>> OutputXML=formObject.getDataFromDataSource(sQuery);
				SKLogger.writeLog("OutputXML","OutputXML"+OutputXML);
				if(!OutputXML.isEmpty()){
					//SKLogger.writeLog("$$AKSHAY",OutputXML.get(0).get(0)+OutputXML.get(0).get(1)+OutputXML.get(0).get(2)+OutputXML.get(0).get(3));
					SKLogger.writeLog("GenerateXML Integration field mapping table",OutputXML.get(0).get(0)+OutputXML.get(0).get(1)+OutputXML.get(0).get(2)+OutputXML.get(0).get(3)+OutputXML.get(0).get(4));
					SKLogger.writeLog("GenerateXML Integration field mapping table",OutputXML.get(0).get(0)+OutputXML.get(0).get(1)+OutputXML.get(0).get(2)+OutputXML.get(0).get(3));
					int n=OutputXML.size();
					System.out.println(n);


					if( n> 0)
					{

						SKLogger.writeLog("","column length"+col_n.length());
						Map<String, String> int_xml = new LinkedHashMap<String, String>();
						Map<String, String> recordFileMap = new HashMap<String, String>();

						for(List<String> mylist:OutputXML)
						{
							// for(int i=0;i<col_n.length();i++)
							for(int i=0;i<8;i++)
							{
								//System.out.println("rec: "+records.item(rec));
								SKLogger.writeLog("","column length values"+col_n);
								String[] col_name = col_n.split(",");
								recordFileMap.put(col_name[i],mylist.get(i));
							}
							recordFileMap.get("call_type");
							String Call_name = (String) recordFileMap.get("Call_name");
							String form_control = (String) recordFileMap.get("form_control");
							String parent_tag = (String) recordFileMap.get("parent_tag_name");
							String tag_name = (String) recordFileMap.get("xmltag_name");
							String is_repetitive = (String) recordFileMap.get("is_repetitive");
							String default_val = (String) recordFileMap.get("default_val");
							String data_format12 = (String) recordFileMap.get("data_format");
							SKLogger.writeLog("#RLOS COmmonm inside generate XML: ","tag_name : "+tag_name +" valuie of default_val: "+default_val+" Call_name: "+Call_name+" parent_tag"+ parent_tag);
							String form_control_val="";
							java.util.Date startDate;

							if(tag_name.equalsIgnoreCase("AddressDetails") && Call_name.equalsIgnoreCase("NEW_CUSTOMER_REQ")){
								if(int_xml.containsKey(parent_tag))
								{
									String xml_str = int_xml.get(parent_tag);
									SKLogger.writeLog("RLOS COMMON"," before adding address+ "+xml_str);
									xml_str = xml_str + getCustAddress_details();
									SKLogger.writeLog("RLOS COMMON"," after adding address+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}		                            	
							}
							else if(tag_name.equalsIgnoreCase("MinorFlag") && Call_name.equalsIgnoreCase("NEW_CUSTOMER_REQ") && parent_tag.equalsIgnoreCase("PersonDetails")){
								if(int_xml.containsKey(parent_tag))
								{
									int Age = Integer.parseInt(formObject.getNGValue("cmplx_Customer_age"));
									String age_flag = "N";
									if(Age<18)
										age_flag="Y";
									String xml_str = int_xml.get(parent_tag);
									xml_str = xml_str + "<"+tag_name+">"+age_flag
									+"</"+ tag_name+">";

									SKLogger.writeLog("RLOS COMMON"," after adding Minor flag+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}		                            	
							}
							else if(tag_name.equalsIgnoreCase("ServiceId") && Call_name.equalsIgnoreCase("EID_Genuine") && parent_tag.equalsIgnoreCase("SecurityDataInfo")){
								String ServiceId ="";
								Timestamp timestamp = new Timestamp(System.currentTimeMillis());
								ServiceId = timestamp.getTime()+"";
								String xml_str = "<"+tag_name+">"+ServiceId.substring(1,ServiceId.length())
								+"</"+ tag_name+">";

								SKLogger.writeLog("RLOS COMMON"," after adding res_flag+ "+xml_str);
								int_xml.put(parent_tag, xml_str);

							}


							else if(tag_name.equalsIgnoreCase("NonResidentFlag") && Call_name.equalsIgnoreCase("NEW_CUSTOMER_REQ") && parent_tag.equalsIgnoreCase("PersonDetails")){
								if(int_xml.containsKey(parent_tag))
								{
									String xml_str = int_xml.get(parent_tag);
									String res_flag ="N";

									if(formObject.getNGValue("cmplx_Customer_ResidentNonResident").equalsIgnoreCase("Resident")){
										res_flag="Y";
									}

									xml_str = xml_str + "<"+tag_name+">"+res_flag
									+"</"+ tag_name+">";

									SKLogger.writeLog("RLOS COMMON"," after adding res_flag+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}		                            	
							}

							else if(tag_name.equalsIgnoreCase("AcRequired") && Call_name.equalsIgnoreCase("NEW_ACCOUNT_REQ")){
								SKLogger.writeLog("inside 1st if","inside New acc request for AcRequired");
								String loantype = (formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 0).equalsIgnoreCase("Conventional")?"KC":"AC");
								String xml_str = int_xml.get(parent_tag);
								xml_str = xml_str+"<"+tag_name+">"+loantype+"</"+ tag_name+">";
								SKLogger.writeLog("COMMON","adding Host name in New Loan Req:  "+xml_str);
								int_xml.put(parent_tag, xml_str);	                            	
							}

							// added for dectech call 
							else if(tag_name.equalsIgnoreCase("CallType") && Call_name.equalsIgnoreCase("DECTECH")){
								SKLogger.writeLog("RLOS COMMON"," iNSIDE CallType+ ");
								String CallType=formObject.getNGValue("ParentToChild");
								SKLogger.writeLog("RLOS COMMON"," CallType "+CallType);
								//String ReqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);

								String xml_str = int_xml.get(parent_tag);
								if (CallType.equalsIgnoreCase("Eligibility")){	
									xml_str = xml_str+ "<"+tag_name+">"+"PM"
									+"</"+ tag_name+">";
								}
								else if(CallType.equalsIgnoreCase("CalculateDBR")){
									xml_str = xml_str+ "<"+tag_name+">"+"CA"
									+"</"+ tag_name+">";
								}
								/*else{
									String alert_msg="Error Occured in CAS";
									throw new ValidatorException(new FacesMessage(alert_msg));
								}*/
								SKLogger.writeLog("RLOS COMMON"," after adding CallType+ "+xml_str);
								int_xml.put(parent_tag, xml_str);

							} 
							else if(tag_name.equalsIgnoreCase("Channel") && Call_name.equalsIgnoreCase("DECTECH")){
								SKLogger.writeLog("RLOS COMMON"," iNSIDE channelcode+ ");

								String ReqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);

								String xml_str = int_xml.get(parent_tag);
								xml_str =  "<"+tag_name+">"+(ReqProd.equalsIgnoreCase("Personal Loan")?"PL":"CC")
								+"</"+ tag_name+">";

								SKLogger.writeLog("RLOS COMMON"," after adding channelcode+ "+xml_str);
								int_xml.put(parent_tag, xml_str);

							}

							else if(tag_name.equalsIgnoreCase("emp_type") && Call_name.equalsIgnoreCase("DECTECH")){
								SKLogger.writeLog("RLOS COMMON"," iNSIDE channelcode+ ");

								String empttype=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6);

								String xml_str = int_xml.get(parent_tag);
								xml_str = xml_str+ "<"+tag_name+">"+empttype+"</"+ tag_name+">";

								SKLogger.writeLog("RLOS COMMON"," after adding channelcode+ "+xml_str);
								int_xml.put(parent_tag, xml_str);

							}


							else if(tag_name.equalsIgnoreCase("ApplicationDetails") && (Call_name.equalsIgnoreCase("DECTECH"))){
								SKLogger.writeLog("inside 1st if","inside DECTECH req1");

								SKLogger.writeLog("inside 1st if","inside customer update req2");
								String xml_str = int_xml.get(parent_tag);
								SKLogger.writeLog("RLOS COMMON"," before adding product+ "+xml_str);
								xml_str = xml_str + getProduct_details();
								SKLogger.writeLog("RLOS COMMON"," after adding product+ "+xml_str);
								int_xml.put(parent_tag, xml_str);

							}



							else if(tag_name.equalsIgnoreCase("LOS") && Call_name.equalsIgnoreCase("DECTECH")){
								if(int_xml.containsKey(parent_tag))
								{
									String LOS=formObject.getNGValue("cmplx_EmploymentDetails_LOS");
									SKLogger.writeLog("RLOS COMMON"," LOS length+ "+LOS.length());
									if (LOS.length()!=0){
										if (LOS.length()==1){
											LOS="0"+LOS+".00";
										}
										else{
											LOS=LOS+".00";
										}
									}
									String xml_str = int_xml.get(parent_tag);
									xml_str = xml_str + "<"+tag_name+">"+LOS
									+"</"+ tag_name+">";

									SKLogger.writeLog("RLOS COMMON"," after adding los+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}		                            	
							}
							else if(tag_name.equalsIgnoreCase("confirmed_in_job") && Call_name.equalsIgnoreCase("DECTECH")){
								if(int_xml.containsKey(parent_tag))
								{
									String confirmedinjob=formObject.getNGValue("cmplx_EmploymentDetails_JobConfirmed");


									if (confirmedinjob.equalsIgnoreCase("true")){
										confirmedinjob="Y";
									}
									else{
										confirmedinjob="N";
									}

									String xml_str = int_xml.get(parent_tag);
									xml_str = xml_str + "<"+tag_name+">"+confirmedinjob
									+"</"+ tag_name+">";

									SKLogger.writeLog("RLOS COMMON"," after adding confirmedinjob+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}		                            	
							}
							else if(tag_name.equalsIgnoreCase("AccountDetails") && Call_name.equalsIgnoreCase("DECTECH")){
								String squeryLoan="select count(*) from ng_rlos_custexpose_loandetails where Wi_Name='"+formObject.getWFWorkitemName()+"'";
								List<List<String>> LoanCount=formObject.getDataFromDataSource(squeryLoan);
								String squeryCard="select count(*) from ng_rlos_custexpose_Carddetails where Wi_Name='"+formObject.getWFWorkitemName()+"'";
								List<List<String>> CardCount=formObject.getDataFromDataSource(squeryCard);
								if ((!LoanCount.get(0).get(0).equalsIgnoreCase("0"))&&(!CardCount.get(0).get(0).equalsIgnoreCase("0"))){
									SKLogger.writeLog("RLOS COMMON"," has internal liability+ ");	
									if(int_xml.containsKey(parent_tag))
									{
										String xml_str = int_xml.get(parent_tag);
										SKLogger.writeLog("RLOS COMMON"," before adding internal liability+ "+xml_str);
										xml_str = xml_str + getInternalLiabDetails();
										SKLogger.writeLog("RLOS COMMON"," after internal liability+ "+xml_str);
										int_xml.put(parent_tag, xml_str);
									}
								}
							}
							else if(tag_name.equalsIgnoreCase("InternalBureau") && Call_name.equalsIgnoreCase("DECTECH")){

								String xml_str = int_xml.get(parent_tag);
								SKLogger.writeLog("RLOS COMMON"," before adding InternalBureauData+ "+xml_str);
								String temp = InternalBureauData();
								if(!temp.equalsIgnoreCase("")){
									xml_str =  temp;
									SKLogger.writeLog("RLOS COMMON"," after InternalBureauData+ "+xml_str);
									int_xml.get(parent_tag);
									int_xml.put(parent_tag, xml_str);
								}


							}
							else if(tag_name.equalsIgnoreCase("InternalBouncedCheques") && Call_name.equalsIgnoreCase("DECTECH")){

								String xml_str = int_xml.get(parent_tag);
								SKLogger.writeLog("RLOS COMMON"," before adding InternalBouncedCheques+ "+xml_str);
								String temp = InternalBouncedCheques();
								if(!temp.equalsIgnoreCase("")){
									xml_str =  xml_str + temp;
									SKLogger.writeLog("RLOS COMMON"," after InternalBouncedCheques+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}

							}
							else if(tag_name.equalsIgnoreCase("InternalBureauIndividualProducts") && Call_name.equalsIgnoreCase("DECTECH")){

								String xml_str = int_xml.get(parent_tag);
								SKLogger.writeLog("RLOS COMMON"," before adding InternalBureauIndividualProducts+ "+xml_str);
								String temp = InternalBureauIndividualProducts();
								if(!temp.equalsIgnoreCase("")){
									xml_str =  xml_str + temp;
									SKLogger.writeLog("RLOS COMMON"," after InternalBureauIndividualProducts+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}

							}
							else if(tag_name.equalsIgnoreCase("InternalBureauPipelineProducts") && Call_name.equalsIgnoreCase("DECTECH")){

								String xml_str = int_xml.get(parent_tag);
								SKLogger.writeLog("RLOS COMMON"," before adding InternalBureauPipelineProducts+ "+xml_str);
								String temp = InternalBureauPipelineProducts();
								if(!temp.equalsIgnoreCase("")){
									xml_str =  xml_str + temp;
									SKLogger.writeLog("RLOS COMMON"," after InternalBureauPipelineProducts+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}

							}
							else if(tag_name.equalsIgnoreCase("ExternalBureau") && Call_name.equalsIgnoreCase("DECTECH")){

								String xml_str = int_xml.get(parent_tag);
								SKLogger.writeLog("RLOS COMMON"," before adding ExternalBureau+ "+xml_str);
								String temp = ExternalBureauData();
								if(!temp.equalsIgnoreCase("")){
									xml_str =  temp;
									SKLogger.writeLog("RLOS COMMON"," after ExternalBureau+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}

							}
							else if(tag_name.equalsIgnoreCase("ExternalBouncedCheques") && Call_name.equalsIgnoreCase("DECTECH")){

								String xml_str = int_xml.get(parent_tag);
								SKLogger.writeLog("RLOS COMMON"," before adding ExternalBouncedCheques+ "+xml_str);
								String temp = ExternalBouncedCheques();
								if(!temp.equalsIgnoreCase("")){
									xml_str =  xml_str + temp;
									SKLogger.writeLog("RLOS COMMON"," after ExternalBouncedCheques+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}                    	
							}
							else if(tag_name.equalsIgnoreCase("ExternalBureauIndividualProducts") && Call_name.equalsIgnoreCase("DECTECH")){

								String xml_str = int_xml.get(parent_tag);
								SKLogger.writeLog("RLOS COMMON"," before adding ExternalBureauIndividualProducts+ "+xml_str);
								String temp =  ExternalBureauIndividualProducts();
								if(!temp.equalsIgnoreCase("")){
									xml_str =  xml_str + temp;
									SKLogger.writeLog("RLOS COMMON"," after ExternalBureauIndividualProducts+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}	                            	
							}
							else if(tag_name.equalsIgnoreCase("ExternalBureauPipelineProducts") && Call_name.equalsIgnoreCase("DECTECH")){

								String xml_str = int_xml.get(parent_tag);
								SKLogger.writeLog("RLOS COMMON"," before adding ExternalBureauPipelineProducts+ "+xml_str);
								String temp =  ExternalBureauPipelineProducts();
								if(!temp.equalsIgnoreCase("")){
									xml_str =  xml_str + temp;
									SKLogger.writeLog("RLOS COMMON"," after ExternalBureauPipelineProducts+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}                        	
							}
							// added for dectech call by abhishek 

							else if (parent_tag!=null && !parent_tag.equalsIgnoreCase(""))
							{
								SKLogger.writeLog("inside 1st if","inside 1st if");
								if(int_xml.containsKey(parent_tag))
								{
									SKLogger.writeLog("inside 1st if","inside 2nd if");
									String xml_str = int_xml.get(parent_tag);
									SKLogger.writeLog("inside 1st if","inside 2nd if xml string"+xml_str);
									if(is_repetitive.equalsIgnoreCase("Y") && int_xml.containsKey(tag_name)){
										SKLogger.writeLog("inside 1st if","inside 3rd if xml string");
										xml_str = int_xml.get(tag_name)+ "</"+tag_name+">" +"<"+ tag_name+">";
										System.out.println("value after adding "+ Call_name+": "+xml_str);
										SKLogger.writeLog("inside 1st if","inside 3rd if xml string xml string"+xml_str);
										int_xml.remove(tag_name);
										int_xml.put(tag_name, xml_str);
										SKLogger.writeLog("inside 1st if","inside 3rd if xml string xml string int_xml");
									}
									else{
										SKLogger.writeLog("inside else of parent tag","value after adding "+ Call_name+": "+xml_str+" form_control name: "+form_control);
										SKLogger.writeLog("","valuie of form control: "+formObject.getNGValue(form_control));
										if(form_control.equalsIgnoreCase("") && default_val.equalsIgnoreCase("")){
											SKLogger.writeLog("inside if added by me","inside");
											xml_str = xml_str + "<"+tag_name+">"+"</"+ tag_name+">";
											SKLogger.writeLog("added by xml","xml_str"+xml_str);
										}
										else if (!(formObject.getNGValue(form_control)==null || formObject.getNGValue(form_control).equalsIgnoreCase("")||  formObject.getNGValue(form_control).equalsIgnoreCase("null")))
										{
											SKLogger.writeLog("inside else of parent tag 1","form_control_val"+ form_control_val);
											if(fin_call_name.toUpperCase().contains(callName.toUpperCase())){
												form_control_val = formObject.getNGValue(form_control).toUpperCase();
											}
											else
												form_control_val = formObject.getNGValue(form_control);

											if(!data_format12.equalsIgnoreCase("text")){
												String[] format_arr = data_format12.split(":");
												String format_name = format_arr[0];
												String format_type = format_arr[1];
												SKLogger.writeLog("","format_name"+format_name);
												SKLogger.writeLog("","format_type"+format_type);

												if(format_name.equalsIgnoreCase("date")){
													DateFormat df = new SimpleDateFormat("dd/MM/yyyy"); 
													DateFormat df_new = new SimpleDateFormat(format_type);

													try {
														startDate = df.parse(form_control_val);
														form_control_val = df_new.format(startDate);
														SKLogger.writeLog("RLOSCommon#Create Input"," date conversion: final Output: "+form_control_val+ " requested format: "+format_type);

													} catch (ParseException e) {
														SKLogger.writeLog("RLOSCommon#Create Input"," Error while format conversion: "+e.getMessage());
														e.printStackTrace();
													}
													catch (Exception e) {
														SKLogger.writeLog("RLOSCommon#Create Input"," Error while format conversion: "+e.getMessage());
														e.printStackTrace();
													}
												}
												//change here for other input format

											}
											SKLogger.writeLog("inside else of parent tag","form_control_val"+ form_control_val);
											xml_str = xml_str + "<"+tag_name+">"+form_control_val
											+"</"+ tag_name+">";
											SKLogger.writeLog("inside else of parent tag xml_str","xml_str"+ xml_str);
										}

										else if(default_val==null || default_val.equalsIgnoreCase("")){
											SKLogger.writeLog("#RLOS Common GenerateXML IF part","no value found for tag name: "+ tag_name);
										}

										//added by akshay for World Check at initiation
										else if(Call_name.equalsIgnoreCase("CUSTOMER_SEARCH_REQUEST")&&(tag_name.equalsIgnoreCase("PhoneValue") && parent_tag.equalsIgnoreCase("PhoneFax") && form_control_val.equalsIgnoreCase(""))){
											if(xml_str.contains("</PhoneFax>")){
												xml_str = xml_str.substring(0, xml_str.lastIndexOf("</PhoneFax>"));
												int_xml.put(parent_tag, xml_str);
											}
											else
												int_xml.remove(parent_tag);
										}
										//ended by akshay for World Check at initiation

										else{
											SKLogger.writeLog("#RLOS Common GenerateXML inside set default value","");
											if(fin_call_name.toUpperCase().contains(callName.toUpperCase())){
												form_control_val = default_val.toUpperCase();
											}
											else
												form_control_val = default_val;

											SKLogger.writeLog("#RLOS Common GenerateXML inside set default value","form_control_val"+ form_control_val);
											xml_str = xml_str + "<"+tag_name+">"+form_control_val
											+"</"+ tag_name+">";
											SKLogger.writeLog("#RLOS Common GenerateXML inside else of parent tag form_control_val xml_str1","xml_str"+ xml_str);

										}
										//code change for to remove docdect incase ref no is not present start	                                       
										if(tag_name.equalsIgnoreCase("DocRefNum") && parent_tag.equalsIgnoreCase("DocDetails") && form_control_val.equalsIgnoreCase("")){
											if(xml_str.contains("</DocDetails>")){
												xml_str = xml_str.substring(0, xml_str.lastIndexOf("</DocDetails>"));
												int_xml.put(parent_tag, xml_str);
											}
											else
												int_xml.remove(parent_tag);
										}
										else{
											int_xml.put(parent_tag, xml_str);
										}
										//code change for to remove docdect incase ref no is not present end
										//int_xml.put(parent_tag, xml_str);
										SKLogger.writeLog("else of generatexml","RLOSCommon"+"inside else"+xml_str);

									}

								}
								else{
									String new_xml_str ="";
									SKLogger.writeLog("inside else of parent tag main 2","value after adding "+ Call_name+": "+new_xml_str+" form_control name: "+form_control);
									SKLogger.writeLog("","valuie of form control: "+formObject.getNGValue(form_control));
									if (!(formObject.getNGValue(form_control)==null || formObject.getNGValue(form_control).equalsIgnoreCase("")||  formObject.getNGValue(form_control).equalsIgnoreCase("null"))){
										SKLogger.writeLog("inside else of parent tag 1","form_control_val"+ form_control_val);
										if(fin_call_name.toUpperCase().contains(callName.toUpperCase())){
											form_control_val = formObject.getNGValue(form_control).toUpperCase();
										}
										else
											form_control_val = formObject.getNGValue(form_control);

										if(!data_format12.equalsIgnoreCase("text")){
											String[] format_arr = data_format12.split(":");
											String format_name = format_arr[0];
											String format_type = format_arr[1];
											if(format_name.equalsIgnoreCase("date")){                                                                   
												form_control_val=Convert_dateFormat(form_control_val,"MM/dd/yyyy",format_type);                                                                       
												SKLogger.writeLog("RLOSCommon#Create Input"," date conversion: final Output: "+form_control_val+ " requested format: "+format_type);
											}
											//change here for other input format

										}

										SKLogger.writeLog("inside else of parent tag","form_control_val"+ form_control_val);
										new_xml_str = new_xml_str + "<"+tag_name+">"+form_control_val
										+"</"+ tag_name+">";
										SKLogger.writeLog("inside else of parent tag xml_str","new_xml_str"+ new_xml_str);
									}

									else if(default_val==null || default_val.equalsIgnoreCase("")){
										if(int_xml.containsKey(parent_tag)|| is_repetitive.equalsIgnoreCase("Y")){
											new_xml_str = new_xml_str + "<"+tag_name+">"+"</"+ tag_name+">";
										}
										SKLogger.writeLog("#RLOS Common GenerateXML Inside Else Part","no value found for tag name: "+ tag_name);
									}
									else{
										SKLogger.writeLog("#RLOS Common GenerateXML inside set default value","");
										if(fin_call_name.toUpperCase().contains(callName.toUpperCase())){
											form_control_val = default_val.toUpperCase();
										}
										else
											form_control_val = default_val;
										SKLogger.writeLog("#RLOS Common GenerateXML inside set default value","form_control_val"+ form_control_val);
										new_xml_str = new_xml_str + "<"+tag_name+">"+form_control_val
										+"</"+ tag_name+">";
										SKLogger.writeLog("#RLOS Common GenerateXML inside else of parent tag form_control_val xml_str1","xml_str"+ new_xml_str);

									}
									int_xml.put(parent_tag, new_xml_str);
									SKLogger.writeLog("else of generatexml","RLOSCommon"+"inside else"+new_xml_str);

								}
							}

						}


						final_xml=final_xml.append("<").append(parentTagName).append(">");
						SKLogger.writeLog("RLOS","Final XMLold--"+final_xml);

						Iterator<Map.Entry<String,String>> itr = int_xml.entrySet().iterator();
						SKLogger.writeLog("itr of hashmap","itr"+itr);
						while (itr.hasNext())
						{
							Map.Entry<String, String> entry =  itr.next();
							SKLogger.writeLog("entry of hashmap","entry"+entry);
							if(final_xml.indexOf((entry.getKey()))>-1){
								SKLogger.writeLog("RLOS","itr_value: Key: "+ entry.getKey()+" Value: "+entry.getValue());
								final_xml = final_xml.insert(final_xml.indexOf("<"+entry.getKey()+">")+entry.getKey().length()+2, entry.getValue());
								SKLogger.writeLog("value of final xml","final_xml"+final_xml);
								itr.remove();
							}

						}    
						final_xml=final_xml.append("</").append(parentTagName).append(">");
						System.out.println("final_xml: "+final_xml);
						SKLogger.writeLog("FInal XMLnew is: ", final_xml.toString());
						final_xml.insert(0, header);
						final_xml.append(footer);
						SKLogger.writeLog("FInal XMLnew with header: ", final_xml.toString());
						formObject.setNGValue("Is_"+callName,"Y");
						SKLogger.writeLog("value of "+callName+" Flag: ",formObject.getNGValue("Is_"+callName));

						//added above code
						

						cabinetName = FormContext.getCurrentInstance().getFormConfig().getConfigElement("EngineName");
						SKLogger.writeLog("$$outputgGridtXML ","cabinetName "+cabinetName);
						wi_name = FormContext.getCurrentInstance().getFormConfig().getConfigElement("ProcessInstanceId");
						ws_name = FormContext.getCurrentInstance().getFormConfig().getConfigElement("ActivityName");
						SKLogger.writeLog("$$outputgGridtXML ","ActivityName "+ws_name);
						sessionID = FormContext.getCurrentInstance().getFormConfig().getConfigElement("DMSSessionId");
						userName = FormContext.getCurrentInstance().getFormConfig().getConfigElement("UserName");
						SKLogger.writeLog("$$outputgGridtXML ","userName "+userName);
						SKLogger.writeLog("$$outputgGridtXML ","sessionID "+sessionID);


						String sMQuery = "SELECT SocketServerIP,SocketServerPort FROM NG_RLOS_MQ_TABLE with (nolock)";
						List<List<String>> outputMQXML=formObject.getDataFromDataSource(sMQuery);
						SKLogger.writeLog("$$outputgGridtXML ","sMQuery "+sMQuery);
						if(!outputMQXML.isEmpty()){
							SKLogger.writeLog("$$outputgGridtXML ",outputMQXML.get(0).get(0)+","+outputMQXML.get(0).get(1));
							socketServerIP =  outputMQXML.get(0).get(0);
							SKLogger.writeLog("$$outputgGridtXML ","socketServerIP "+socketServerIP);
							socketServerPort = Integer.parseInt(outputMQXML.get(0).get(1));
							SKLogger.writeLog("$$outputgGridtXML ","socketServerPort "+socketServerPort);
							if(!(socketServerIP.equalsIgnoreCase("") && socketServerIP.equalsIgnoreCase(null) && socketServerPort.equals(null) && socketServerPort.equals(0)))
							{
							
									socket = new Socket(socketServerIP, socketServerPort);
									out = socket.getOutputStream();
									socketInputStream = socket.getInputStream();
									dout = new DataOutputStream(out);
									din = new DataInputStream(socketInputStream);
									mqOutputResponse="";	     
									mqInputRequest= getMQInputXML(sessionID,cabinetName,wi_name,ws_name,userName,final_xml);
									SKLogger.writeLog("$$outputgGridtXML ","mqInputRequest "+mqInputRequest);

									if (mqInputRequest != null && mqInputRequest.length() > 0) 
									{
										int outPut_len = mqInputRequest.getBytes("UTF-16LE").length;
										SKLogger.writeLog("Final XML output len: ",outPut_len+"");
										mqInputRequest = outPut_len+"##8##;"+mqInputRequest;
										SKLogger.writeLog("MqInputRequest","Input Request Bytes : "+mqInputRequest.getBytes("UTF-16LE"));
										dout.write(mqInputRequest.getBytes("UTF-16LE"));
										dout.flush();                
									}
									byte[] readBuffer = new byte[50000];
								/*	Thread.sleep(100000);*/
									int num = din.read(readBuffer);
									SKLogger.writeLog("RLOS common ", "num val: "+ num);
									int sleep_count= 0;
									/*while(num <0 && sleep_count<1000){
										SKLogger.writeLog("RLOS COmmon", "Inside while loop till data come: "+ sleep_count);
										Thread.sleep(100);
										num = din.read(readBuffer);
										sleep_count++;
									}*/
									boolean wait_flag = true;
									int out_len=0;
									
									if (num > 0) 
									{
										while(wait_flag){
											SKLogger.writeLog("MqOutputRequest","num :"+num);
											byte[] arrayBytes = new byte[num];
											System.arraycopy(readBuffer, 0, arrayBytes, 0, num);
											mqOutputResponse = mqOutputResponse + new String(arrayBytes, "UTF-16LE");
											SKLogger.writeLog("MqOutputRequest","inside loop output Response :\n"+mqOutputResponse);
											if(mqOutputResponse.contains("##8##;")){
												String[]	mqOutputResponse_arr = mqOutputResponse.split("##8##;");
												mqOutputResponse = mqOutputResponse_arr[1];
												out_len = Integer.parseInt(mqOutputResponse_arr[0]);
												SKLogger.writeLog("MqOutputRequest","First Output Response :\n"+mqOutputResponse);
												SKLogger.writeLog("MqOutputRequest","Output length :\n"+out_len);
											}
											if(out_len <= mqOutputResponse.getBytes("UTF-16LE").length){
												wait_flag=false;
											}
											Thread.sleep(100);
											num = din.read(readBuffer);

										}
										if(mqOutputResponse.contains("&lt;")){
											SKLogger.writeLog("MqOutputRequest","inside for Dectech :\n"+mqOutputResponse);
											mqOutputResponse=mqOutputResponse.replaceAll("&lt;", "<");
											SKLogger.writeLog("MqOutputRequest","after replacing lt :\n"+mqOutputResponse);
											mqOutputResponse=mqOutputResponse.replaceAll("&gt;", ">");
											SKLogger.writeLog("MqOutputRequest","after replacing gt :\n"+mqOutputResponse);
										}
										SKLogger.writeLog("MqOutputRequest","Final Output Response :\n"+mqOutputResponse);
										socket.close();
										return mqOutputResponse;
									}
								
							}
							else{
								SKLogger.writeLog("SocketServerIp and SocketServerPort is not maintained ","");
								SKLogger.writeLog("SocketServerIp is not maintained ",socketServerIP);
								SKLogger.writeLog(" SocketServerPort is not maintained ",socketServerPort.toString());
								return "MQ details not maintained";
							}
						}
						else{
							SKLogger.writeLog("SOcket details are not maintained in NG_RLOS_MQ_TABLE table","");
							return "MQ details not maintained";
						}
					}

				}
				else {
					SKLogger.writeLog("Genrate XML: ","Entry is not maintained in field mapping Master table for : "+callName);
					return "Call not maintained";
				}


			}
			else{
				SKLogger.writeLog("Genrate XML: ","Entry is not maintained in Master table for : "+callName);
				return "Call not maintained";
			}
		}
		catch (UnknownHostException e) 
		{        
			SKLogger.writeLog("Exception ocurred: ",e.getMessage());
			return "0";
		}
		catch(Exception e){
			SKLogger.writeLog("Exception ocurred: ",e.getLocalizedMessage());
			SKLogger.writeLog("$$Exception: ",printException(e));
			return "0";
		}    
		return "";

	} 

	public String getProduct_details(){
		SKLogger.writeLog("RLOSCommon java file", "inside getProduct_details : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int prod_row_count = formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		SKLogger.writeLog("RLOSCommon java file", "inside getCustAddress_details add_row_count+ : "+prod_row_count);
		String  prod_xml_str ="";

		for (int i = 0; i<prod_row_count;i++){
			String prod_type = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 0); //0
			String reqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 1);//1
			String subProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2);//2
			String reqLimit=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 3);//3
			String appType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 4);//4
			String cardProd = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 5);//5
			String scheme=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 8);//6
			String tenure=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 7);//7
			String limitExpiry=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 12);//8
			limitExpiry=Convert_dateFormat(limitExpiry, "dd/MM/yyyy", "yyyy-MM-dd");

			prod_xml_str = prod_xml_str + "<ApplicationDetails><product_type>"+(prod_type.equalsIgnoreCase("Conventional")?"CON":"ISL")+"</product_type>";
			prod_xml_str = prod_xml_str + "<app_category>"+formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg")+"</app_category>";
			prod_xml_str = prod_xml_str + "<requested_product>"+(reqProd.equalsIgnoreCase("Personal Loan")?"PL":"CC")+"</requested_product>";
			prod_xml_str = prod_xml_str + "<requested_limit>"+reqLimit+"</requested_limit>";
			prod_xml_str = prod_xml_str + "<sub_product>"+subProd+"</sub_product>";
			prod_xml_str = prod_xml_str + "<requested_card_product>"+cardProd+"</requested_card_product>";
			prod_xml_str = prod_xml_str + "<application_type>"+appType+"</application_type>";
			prod_xml_str = prod_xml_str + "<scheme>"+scheme+"</scheme>";
			prod_xml_str = prod_xml_str + "<tenure>20</tenure>";
			prod_xml_str = prod_xml_str + "<customer_type>"+(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true")?"NTB":"Existing")+"</customer_type>";
			prod_xml_str = prod_xml_str + "<limit_Expiry_Date>"+limitExpiry+"</limit_Expiry_Date><final_limit>0</final_limit><emi></emi><manual_deviation>N</manual_deviation></ApplicationDetails>";

		}
		SKLogger.writeLog("RLOSCommon", "Address tag Cration: "+ prod_xml_str);
		return prod_xml_str;
	}

	private static String getMQInputXML(String sessionID, String cabinetName, String wi_name, String ws_name, String userName, StringBuffer final_xml) 
	{
		FormContext.getCurrentInstance().getFormConfig( );

		StringBuffer strBuff=new StringBuffer();
		strBuff.append("<APMQPUTGET_Input>");
		strBuff.append("<SessionId>"+sessionID+"</SessionId>");
		strBuff.append("<EngineName>"+cabinetName+"</EngineName>");
		strBuff.append("<XMLHISTORY_TABLENAME>NG_RLOS_XMLLOG_HISTORY</XMLHISTORY_TABLENAME>");
		strBuff.append("<WI_NAME>"+wi_name+"</WI_NAME>");
		strBuff.append("<WS_NAME>"+ws_name+"</WS_NAME>");
		strBuff.append("<USER_NAME>"+userName+"</USER_NAME>");
		strBuff.append("<MQ_REQUEST_XML>");
		strBuff.append(final_xml);		
		strBuff.append("</MQ_REQUEST_XML>");
		strBuff.append("</APMQPUTGET_Input>");	
		SKLogger.writeLog("inside getOutputXMLValues","getMQInputXML"+strBuff.toString());
		return strBuff.toString();
	}


	public void valueSetCustomer(String outputResponse, String operationName)
	{
		SKLogger.writeLog("RLOSCommon valueSetCustomer", "Inside valueSetCustomer():");
		String outputXMLHead = "";
		String outputXMLMsg = "";
		String returnDesc = "";
		String returnCode = "";
		String response= "";

		XMLParser objXMLParser = new XMLParser();
		try
		{
			if(outputResponse.indexOf("<EE_EAI_HEADER>")>-1)
			{
				outputXMLHead=outputResponse.substring(outputResponse.indexOf("<EE_EAI_HEADER>"),outputResponse.indexOf("</EE_EAI_HEADER>")+16);
				SKLogger.writeLog("RLOSCommon valueSetCustomer", "outputXMLHead");
			}
			objXMLParser.setInputXML(outputXMLHead);
			if(outputXMLHead.indexOf("<MsgFormat>")>-1)
			{
				response= outputXMLHead.substring(outputXMLHead.indexOf("<MsgFormat>")+11,outputXMLHead.indexOf("</MsgFormat>"));
				SKLogger.writeLog("$$response ",response);
			}
			if(outputXMLHead.indexOf("<ReturnDesc>")>-1)
			{
				returnDesc= outputXMLHead.substring(outputXMLHead.indexOf("<ReturnDesc>")+12,outputXMLHead.indexOf("</ReturnDesc>"));
				SKLogger.writeLog("$$returnDesc ",returnDesc);
			}
			if(outputXMLHead.indexOf("<ReturnCode>")>-1)
			{
				returnCode= outputXMLHead.substring(outputXMLHead.indexOf("<ReturnCode>")+12,outputXMLHead.indexOf("</ReturnCode>"));
				SKLogger.writeLog("$$returnCode ",returnCode);
			}

			if(outputResponse.indexOf("<MQ_RESPONSE_XML>")>-1)
			{
				outputXMLMsg=outputResponse.substring(outputResponse.indexOf("<MQ_RESPONSE_XML>")+17,outputResponse.indexOf("</MQ_RESPONSE_XML>"));
				//SKLogger.writeLog("$$outputXMLMsg ",outputXMLMsg);
				SKLogger.writeLog("$$outputXMLMsg getOutputXMLValues","check inside getOutputXMLValues");
				//getOutputXMLValues(outputXMLMsg,response);
				getOutputXMLValues(outputXMLMsg,response,operationName);
				SKLogger.writeLog("$$outputXMLMsg ","outputXMLMsg");
			}
			// for dectech CALL
			if(outputResponse.indexOf("<PM_Reason_Codes>")>-1 && outputResponse.indexOf("<PM_Outputs>")>-1 ){
				String squery="";
				String Output_TAI="";
				String Output_Final_DBR="";
				String Output_Existing_DBR  ="";
				String Output_Eligible_Amount="";
				String Output_Delegation_Authority="";
				String Grade="";
				String Output_Interest_Rate="";
				String Output_Net_Salary_DBR="";

				FormReference formObject = FormContext.getCurrentInstance().getFormReference();
				String strOutputXml = "";
				//Setting the value in ELIGANDPROD info
				if (outputResponse.contains("Output_TAI")){
					Output_TAI = outputResponse.substring(outputResponse.indexOf("<Output_TAI>")+12,outputResponse.indexOf("</Output_TAI>"));
					if (Output_TAI!=null){
						try{
							formObject.setNGValue("cmplx_Liability_New_TAI", Output_TAI);
							formObject.setNGValue("cmplx_EligibilityAndProductInfo_FinalTAI", Output_TAI);

						}
						catch (Exception e){
							SKLogger.writeLog("Dectech error", "Exception:"+e.getMessage());
						}


					}
					SKLogger.writeLog("$$outputXMLMsg ","inside outpute get Output_TAI"+Output_TAI);
				}
				if (outputResponse.contains("Output_Final_DBR")){

					Output_Final_DBR = outputResponse.substring(outputResponse.indexOf("<Output_Final_DBR>")+18,outputResponse.indexOf("</Output_Final_DBR>"));
					if (Output_Final_DBR!=null){
						formObject.setNGValue("cmplx_EligibilityAndProductInfo_FinalDBR", Output_Final_DBR);
					}
					SKLogger.writeLog("$$outputXMLMsg ","inside outpute get Output_Final_DBR"+Output_Final_DBR);
				}
				if (outputResponse.contains("Output_Interest_Rate")){
					Output_Interest_Rate = outputResponse.substring(outputResponse.indexOf("<Output_Interest_Rate>")+22,outputResponse.indexOf("</Output_Interest_Rate>")); ;
					if (Output_Interest_Rate!=null){
						formObject.setNGValue("cmplx_EligibilityAndProductInfo_InterestRate", Output_Interest_Rate);
					}
					SKLogger.writeLog("$$outputXMLMsg ","inside outpute get Output_Interest_Rate"+Output_Interest_Rate);

				}
				//Setting the value in ELIGANDPROD info
				//Setting the value in lIABILITY info
				if (outputResponse.contains("Output_Existing_DBR")){
					Output_Existing_DBR = outputResponse.substring(outputResponse.indexOf("<Output_Existing_DBR>")+21,outputResponse.indexOf("</Output_Existing_DBR>")); ;
					if (Output_Existing_DBR!=null){
						formObject.setNGValue("cmplx_Liability_New_DBR", Output_Existing_DBR);
					}
					SKLogger.writeLog("$$outputXMLMsg ","inside outpute get Output_Existing_DBR"+Output_Existing_DBR);

				}


				if (outputResponse.contains("Output_Net_Salary_DBR")){
					Output_Net_Salary_DBR = outputResponse.substring(outputResponse.indexOf("<Output_Net_Salary_DBR>")+23,outputResponse.indexOf("</Output_Net_Salary_DBR>")); ;
					if (Output_Net_Salary_DBR!=null){
						formObject.setNGValue("cmplx_Liability_New_DBRNet", Output_Net_Salary_DBR);
					}
					SKLogger.writeLog("$$outputXMLMsg ","inside outpute get Output_Net_Salary_DBR"+Output_Net_Salary_DBR);

				}
				//Setting the value in lIABILITY info
				//Setting the value in creditCard iFrame
				if (outputResponse.contains("Output_Delegation_Authority")){
					Output_Delegation_Authority = outputResponse.substring(outputResponse.indexOf("<Output_Delegation_Authority>")+29,outputResponse.indexOf("</Output_Delegation_Authority>")); ;
					SKLogger.writeLog("$$outputXMLMsg ","inside outpute get Output_Existing_DBR"+Output_Eligible_Amount);

				}
				if (outputResponse.contains("Grade")){
					Grade = outputResponse.substring(outputResponse.indexOf("<Grade>")+7,outputResponse.indexOf("</Grade>")); ;
					SKLogger.writeLog("$$outputXMLMsg ","inside outpute get Grade"+Grade);

				}
				if (outputResponse.contains("Output_Eligible_Amount")){
					Output_Eligible_Amount = outputResponse.substring(outputResponse.indexOf("<Output_Eligible_Amount>")+24,outputResponse.indexOf("</Output_Eligible_Amount>")); ;
					SKLogger.writeLog("$$outputXMLMsg ","inside outpute get Output_Existing_DBR"+Output_Eligible_Amount);

				}
				//Setting the value in creditCard iFrame


				String sGeneralData = formObject.getWFGeneralData();
				SKLogger.writeLog("$$outputXMLMsg ","inside outpute get sGeneralData"+sGeneralData);

				String cabinetName = sGeneralData.substring(sGeneralData.indexOf("<EngineName>")+12,sGeneralData.indexOf("</EngineName>")); 
				String sessionId = sGeneralData.substring(sGeneralData.indexOf("<DMSSessionId>")+14,sGeneralData.indexOf("</DMSSessionId>"));
				String jtsIp = sGeneralData.substring(sGeneralData.indexOf("<JTSIP>")+7,sGeneralData.indexOf("</JTSIP>")); ;
				String jtsPort = sGeneralData.substring(sGeneralData.indexOf("<JTSPORT>")+9,sGeneralData.indexOf("</JTSPORT>")); ;

				//String strInputXML = ExecuteQuery_APUpdate("ng_rlos_IGR_Eligibility_CardProduct","Product,Card_Product,Eligible_Card,Decision,Declined_Reason,wi_name","'Credit Card','"+subProd+"','"+Output_Eligible_Amount+"','Declined','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+formObject.getWFWorkitemName()+"'","WI_NAME ='" + formObject.getWFWorkitemName() + "'",cabinetName,sessionId);
				//SKLogger.writeLog("$$Value set for DECTECH->>","UpdateinputXML is:"+UpdateinputXML);
				String strInputXML =ExecuteQuery_APdelete("ng_rlos_IGR_Eligibility_CardProduct","WI_NAME ='" + formObject.getWFWorkitemName() + "'",cabinetName,sessionId);
				
				strOutputXml = WFCallBroker.execute(strInputXML,jtsIp,Integer.parseInt(jtsPort),1);
				SKLogger.writeLog("$$Value set for DECTECH->>","strOutputXml is:"+strOutputXml);

				String mainCode=strOutputXml.substring(strOutputXml.indexOf("<MainCode>")+10,strOutputXml.indexOf("</MainCode>"));
				String rowUpdated=strOutputXml.substring(strOutputXml.indexOf("<Output>")+8,strOutputXml.indexOf("</Output>"));
				SKLogger.writeLog("$$Value set for DECTECH->>","mainCode,rowUpdated is: "+mainCode+", "+rowUpdated);
				
				outputResponse = outputResponse.substring(outputResponse.indexOf("<ProcessManagerResponse>")+24, outputResponse.indexOf("</ProcessManagerResponse>"));
				outputResponse = "<?xml version=\"1.0\"?><Response>" + outputResponse;
				outputResponse = outputResponse+"</Response>";
				SKLogger.writeLog("$$outputResponse ","inside outpute get outputResponse"+outputResponse);
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				InputSource is = new InputSource(new StringReader(outputResponse));

				Document doc = builder.parse(is);
				doc.getDocumentElement().normalize();

				SKLogger.writeLog("Root element :" ,doc.getDocumentElement().getNodeName());

				NodeList nList = doc.getElementsByTagName("PM_Reason_Codes");
				System.out.println("----------------------------");

				for (int temp = 0; temp < nList.getLength(); temp++) {
					String Reason_Decision="";
					Node nNode = nList.item(temp);
					SKLogger.writeLog("$$outputResponse ","\nCurrent Element :" + nNode.getNodeName());

					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						//System.out.println("Student roll no : " + eElement.getAttribute("rollno"));
						SKLogger.writeLog("$$outputResponse ","inside outpute get Decision_Objective"+ eElement.getElementsByTagName("Decision_Objective").item(0).getTextContent());
						SKLogger.writeLog("$$outputResponse ","inside outpute get Decision_Sequence_Number"+eElement.getElementsByTagName("Decision_Sequence_Number").item(0).getTextContent());
						SKLogger.writeLog("$$outputResponse ","inside outpute get Sequence_Number"+eElement.getElementsByTagName("Sequence_Number").item(0).getTextContent());

						SKLogger.writeLog("$$outputResponse ","inside outpute get Reason_Description"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent());
						String subProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2);//2


						Reason_Decision = eElement.getElementsByTagName("Reason_Decision").item(0).getTextContent() ;
						SKLogger.writeLog("$$outputResponse ","Value of Reason_Decision"+Reason_Decision);


					
							if (Reason_Decision.equalsIgnoreCase("D")){
								squery="Insert into ng_rlos_IGR_Eligibility_CardProduct (Product,Card_Product,Eligible_Card,Decision,Declined_Reason,Delegation_Authorithy,Score_grade,wi_name) values('Credit Card','"+subProd+"','"+Output_Eligible_Amount+"','Declined','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"')";
							}
							if (Reason_Decision.equalsIgnoreCase("R")){
								squery="Insert into ng_rlos_IGR_Eligibility_CardProduct (Product,Card_Product,Eligible_Card,Decision,Deviation_Code_Refer,Delegation_Authorithy,Score_grade,wi_name) values('Credit Card','"+subProd+"','"+Output_Eligible_Amount+"','Refer','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"')";
							}

							SKLogger.writeLog("$$outputResponse ","Squery is"+squery);
							formObject.saveDataIntoDataSource(squery);							
											
					}
					
				}
			}
		}
		catch(Exception e)
		{            
			SKLogger.writeLog("Exception occured in valueSetCustomer method:  ",e.getMessage());
			System.out.println("Exception occured in valueSetCustomer method: "+ e.getMessage());
		}
	}



	public static void getOutputXMLValues(String outputXMLMsg, String response,String Operation_name) throws ParserConfigurationException
	{
		String sQuery = "";
		String tagValue = "";
		String EffectFrom="";
		String EffectTo ="";
		try
		{   
			SKLogger.writeLog("inside getOutputXMLValues","inside outputxml");
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String col_name = "Form_Control,Parent_Tag_Name,XmlTag_Name,InDirect_Mapping,Grid_Mapping,Grid_Table,grid_table_xml_tags";

			//  sQuery = "SELECT "+col_name+" FROM NG_RLOS_Integration_Field_Response where Call_Name ='"+response+"'";
			//code added here
			if(!(Operation_name.equalsIgnoreCase("") || Operation_name.equalsIgnoreCase(null))){   
				SKLogger.writeLog("inside if of operation","operation111"+Operation_name);
				SKLogger.writeLog("inside if of operation","callName111"+col_name);
				sQuery = "SELECT "+col_name+" FROM NG_RLOS_Integration_Field_Response where Call_Name ='"+response+"' and Operation_name='"+Operation_name+"'" ;
				SKLogger.writeLog("RLOSCommon", "sQuery "+sQuery);
			}
			else{
				sQuery = "SELECT "+col_name+" FROM NG_RLOS_Integration_Field_Response where Call_Name ='"+response+"'";
			}

			//ended here
			List<List<String>> outputTableXML=formObject.getDataFromDataSource(sQuery);


			String[] col_name_arr = col_name.split(",");

			SKLogger.writeLog("$$outputTableXML",outputTableXML.get(0).get(0)+outputTableXML.get(0).get(1)+outputTableXML.get(0).get(2)+outputTableXML.get(0).get(3));
			int n=outputTableXML.size();
			SKLogger.writeLog("outputTableXML size: " , n+"");

			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(outputXMLMsg)));
			SKLogger.writeLog("name is doc : ", doc+"");
			NodeList nl = doc.getElementsByTagName("*");

			if( n> 0)
			{
				new LinkedHashMap<String, String>();
				Map<String, String> responseFileMap = new HashMap<String, String>();
				for(List<String> mylist:outputTableXML)
				{
					for(int i=0;i<col_name_arr.length;i++)
					{
						responseFileMap.put(col_name_arr[i],mylist.get(i));
					}
					String form_control = (String) responseFileMap.get("Form_Control");
					String parent_tag = (String) responseFileMap.get("Parent_Tag_Name");
					String fielddbxml_tag = (String) responseFileMap.get("XmlTag_Name");
					String Grid_col_tag = (String) responseFileMap.get("grid_table_xml_tags");
					SKLogger.writeLog("Grid_col_tag","Grid_col_tag"+Grid_col_tag);

					String flas="N";
					if (parent_tag!=null && !parent_tag.equalsIgnoreCase(""))
					{                    
						for (int i = 0; i < nl.getLength(); i++)
						{
							nl.item(i);
							if(nl.item(i).getNodeName().equalsIgnoreCase(fielddbxml_tag))
							{
								SKLogger.writeLog("RLOS Common# getOutputXMLValues()"," fielddbxml_tag: "+fielddbxml_tag);
								String indirectMapping = (String) responseFileMap.get("InDirect_Mapping");
								String gridMapping = (String) responseFileMap.get("Grid_Mapping");

								if(gridMapping.equalsIgnoreCase("Y"))
								{
									SKLogger.writeLog("Grid_col_tag_arr","inside indirect mapping");
									if(Grid_col_tag.contains(",")){
										String Grid_col_tag_arr[]  =Grid_col_tag.split(",");
										//String grid_detail_str = nl.item(i).getNodeValue();
										NodeList childnode  = nl.item(i).getChildNodes();
										SKLogger.writeLog("Grid_col_tag_arr","Grid_col_tag_arr: "+Grid_col_tag);   
										SKLogger.writeLog("childnode","childnode"+childnode); 
										List<String> Grid_row = new ArrayList<String>(); 
										Grid_row.clear();

										String flaga="N";
										String flgYrs="N";
										for(int k = 0;k<Grid_col_tag_arr.length;k++){

											for (int child_node_len = 0 ;child_node_len< childnode.getLength();child_node_len++){
												if(childnode.item(child_node_len).getNodeName().equalsIgnoreCase("EffectiveFrom")){
													EffectFrom= childnode.item(child_node_len).getTextContent();
												}


												if(childnode.item(child_node_len).getNodeName().equalsIgnoreCase("EffectiveTo")){
													EffectTo= childnode.item(child_node_len).getTextContent();
												}


												if(childnode.item(child_node_len).getNodeName().equalsIgnoreCase("ReporCntryDet")){
													SKLogger.writeLog("RLOS Common","inside ReporCntryDet for OECD");
													NodeList ReporCntry_childnode  = childnode.item(child_node_len).getChildNodes();
													SKLogger.writeLog("RLOS Common","ReporCntry_childnode: "+ReporCntry_childnode.toString());
													for (int ReporCntry_childnode_len = 0; ReporCntry_childnode_len<ReporCntry_childnode.getLength();ReporCntry_childnode_len++){
														if(ReporCntry_childnode.item(ReporCntry_childnode_len).getNodeName().equalsIgnoreCase(Grid_col_tag_arr[k])){
															Grid_row.add(ReporCntry_childnode.item(ReporCntry_childnode_len).getTextContent());
															flaga="Y";
															break;
														}
														else {
															flaga="N";
														}
													}													
												}

												if(childnode.item(child_node_len).getNodeName().equalsIgnoreCase(Grid_col_tag_arr[k])){
													SKLogger.writeLog("child_node_len ","getTextContent: "+childnode.item(child_node_len).getNodeName());
													SKLogger.writeLog("child_node_len ","getTextContent: "+childnode.item(child_node_len).getTextContent());
													if(childnode.item(child_node_len).getNodeName().equalsIgnoreCase("AddrPrefFlag"))
													{
														if(childnode.item(child_node_len).getTextContent().equalsIgnoreCase("Y"))
															Grid_row.add("true");

														else if(childnode.item(child_node_len).getTextContent().equalsIgnoreCase("N"))
															Grid_row.add("false");

													}
													else
														Grid_row.add(childnode.item(child_node_len).getTextContent());
													flaga="Y";
													break;
												}                                            
											}
											//need to see the years from product grid and add the grid
											if (Grid_col_tag_arr[k].equalsIgnoreCase("Years") && flgYrs.equalsIgnoreCase("N")){

												Date d1,d2;
												SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
												SKLogger.writeLog("RLSO Common: ","Inside Years tag:");
												int Diff=0;
												if(EffectFrom!=null && !EffectFrom.equalsIgnoreCase("")&& EffectTo!=null && !EffectTo.equalsIgnoreCase("")){
													d1=(Date) sdf.parse(EffectFrom);
													d2=(Date) sdf.parse(EffectTo);
													SKLogger.writeLog("EffectFrom ",EffectFrom+"  EffectTo: "+EffectTo);
													Diff= d2.getYear()-d1.getYear();
												}else{
													SKLogger.writeLog("RLSO Common: ","Exception as Effective from or Effective is not received");
												}

												flgYrs="Y";
												flaga="Y";
												SKLogger.writeLog("RLOS","Inside Years tag: "+Diff);     
												Grid_row.add(Diff+"");

											}
											if(flaga.equalsIgnoreCase("N") ){
												SKLogger.writeLog("child_node_len ","value of flaga in if of for loop "+flaga);
												SKLogger.writeLog("child_node_len ","Grid_col_tag_arr[k]: "+Grid_col_tag_arr[k]);
												// SKLogger.writeLog("child_node_len ","getTextContent: "+childnode.item(child_node_len).getTextContent());
												Grid_row.add("NA");

											}
											flaga="N";
											flgYrs="N";

										}
										//Grid_row.add("true");
										Grid_row.add(formObject.getWFWorkitemName());
										//Grid_col_tag_arr[k]
										//code to add row in grid. and pass Grid_row in that.
										SKLogger.writeLog("RLOS Common# getOutputXMLValues()", "$$AKSHAY$$List to be added in address grid: "+ Grid_row.toString());

										if(fielddbxml_tag.equalsIgnoreCase("AddrDet")){ 
											if(!Grid_row.get(0).equalsIgnoreCase("swift")){//added By Akshay on 28/6/17 for removing swift
												formObject.addItem("cmplx_AddressDetails_cmplx_AddressGrid", Grid_row);
											}
										}

										// added	 by yash on 10/7/2017 for getting values in worldcheck grid 
										else if(fielddbxml_tag.equalsIgnoreCase("CustomerDetails")){
											SKLogger.writeLog("RLOS Common# getOutputXMLValues()","Form Control: "+form_control+" Grid data to be added for World check: "+ Grid_row);
											formObject.addItem(form_control,Grid_row);
										}
										//ended 10/07/2017 for worldcheck grid

										else if(fielddbxml_tag.equalsIgnoreCase("OECDDet")){ 
											SKLogger.writeLog("Inside GETOutputXMLValues:","fieldxml_tag is:"+fielddbxml_tag+" form_control is:"+form_control);
											SKLogger.writeLog("Inside GETOutputXMLValues:","Grid row is:"+Grid_row);
											formObject.addItem(form_control, Grid_row);
										}
										else{
											//formObject.addItem(form_control, Grid_row);-----commented By Akshay

											SKLogger.writeLog("RLOS Common# ADDED IN GRID form_control"+form_control, "");
										}
									}
									else{
										SKLogger.writeLog("RLOS Common# getOutputXMLValues()", "Grid mapping is Y for this tag and grid_table_xml_tags column is blank tag name: "+ fielddbxml_tag);
									}

								}
								else if(indirectMapping.equalsIgnoreCase("Y")){
									SKLogger.writeLog("Grid_col_tag_arr","inside indirect mapping");
									String col_list = "xmltag_name,tag_value,indirect_tag_list,indirect_formfield_list,indirect_child_tag,form_control,indirect_val,IS_Master,Master_Name";
									//code added
									if(!(Operation_name.equalsIgnoreCase("") || Operation_name.equalsIgnoreCase(null))){   
										SKLogger.writeLog("inside if of operation","operation111"+Operation_name);
										SKLogger.writeLog("inside if of operation","callName111"+col_name);
										sQuery = "SELECT "+col_list+" FROM NG_RLOS_Integration_Indirect_Response where Call_Name ='"+response+"' and XmlTag_Name = '"+nl.item(i).getNodeName()+"' and Operation_name='"+Operation_name+"'" ;
										SKLogger.writeLog("RLOSCommon", "sQuery "+sQuery);
									}

									else{
										sQuery = "SELECT "+col_list+" FROM NG_RLOS_Integration_Indirect_Response where Call_Name ='"+response+"' and XmlTag_Name = '"+nl.item(i).getNodeName()+"'";
										SKLogger.writeLog("#RLOS Common Inside indirectMapping ", "query: "+sQuery);
									}
									List<List<String>> outputindirect=formObject.getDataFromDataSource(sQuery);
									SKLogger.writeLog("#RLOS Common Inside indirectMapping test tanshu ", "1");
									String col_list_arr[] = col_list.split(",");
									Map<String, String> gridResponseMap = new HashMap<String, String>();
									for(List<String> mygridlist:outputindirect)
									{
										SKLogger.writeLog("#RLOS Common Inside indirectMapping test tanshu ", "inside list loop for value: "+mygridlist);

										for(int x=0;x<col_list_arr.length;x++)
										{
											gridResponseMap.put(col_list_arr[x],mygridlist.get(x));
											//SKLogger.writeLog("#RLOS Common Inside indirectMapping ", "inside put map"+x);
										}
										String xmltag_name = gridResponseMap.get("xmltag_name").toString();
										String tag_value = gridResponseMap.get("tag_value").toString();
										String indirect_tag_list = gridResponseMap.get("indirect_tag_list").toString();
										String indirect_formfield_list = gridResponseMap.get("indirect_formfield_list").toString();
										String indirect_child_tag = gridResponseMap.get("indirect_child_tag").toString();
										String indirect_form_control = gridResponseMap.get("form_control").toString();
										String indirect_val = gridResponseMap.get("indirect_val").toString();
										String IS_Master = gridResponseMap.get("IS_Master").toString();
										SKLogger.writeLog("#RLOS Common Inside indirectMapping ", "IS_Master"+IS_Master);
										String Master_Name = gridResponseMap.get("Master_Name").toString();
										SKLogger.writeLog("#RLOS Common Inside indirectMapping ", "Master_Name"+Master_Name);
										SKLogger.writeLog("#RLOS Common Inside indirectMapping ", "all details fetched for XML tag name: "+xmltag_name+" tag_value: "+tag_value);
										if(IS_Master.equalsIgnoreCase("Y")){
											String code = nl.item(i).getTextContent();
											SKLogger.writeLog("#RLOS Common Inside indirectMapping code:",code);
											sQuery= "Select description from "+Master_Name+" where Code='"+code+"'";
											SKLogger.writeLog("#RLOS Common Inside indirectMapping code:","Query to select master value: "+  sQuery);
											List<List<String>> query=formObject.getDataFromDataSource(sQuery);
											String value=query.get(0).get(0);
											SKLogger.writeLog("#query.get(0).get(0)",value );
											SKLogger.writeLog("#RLOS Common Inside indirectMapping code:","Query to select master value: "+  sQuery);
											formObject.setNGValue(indirect_form_control,value);
											SKLogger.writeLog("indirect_form_control value",formObject.getNGValue(indirect_form_control));
										}

										else if(indirect_form_control!=null && !indirect_form_control.trim().equalsIgnoreCase("")){
											if( nl.item(i).getTextContent().equalsIgnoreCase(tag_value)){
												SKLogger.writeLog("RLOS common: getOutputXMLValues","Indirect value for "+xmltag_name+" Indirect control name: "+indirect_form_control+" final value: "+indirect_val+" tag_value: "+tag_value);
												formObject.setNGValue(indirect_form_control,indirect_val,false);
											}
											// System.out.println("form Control: "+ indirect_form_control + "indirect val: "+ indirect_val);

										}

										else{
											SKLogger.writeLog("Grid_col_tag_arr","inside indirect mapping part2 else");
											if(indirect_tag_list!=null ){
												NodeList childnode  = nl.item(i).getChildNodes();
												SKLogger.writeLog("childnode","childnode"+childnode);
												if(indirect_tag_list.contains(",")){
													SKLogger.writeLog("#RLOS common indirect field values","inside indirect mapping part2 indirect_tag_list with ,");
													String indirect_tag_list_arr[] = indirect_tag_list.split(",");
													String indirect_formfield_list_arr[] = indirect_formfield_list.split(",");
													if(indirect_tag_list_arr.length == indirect_formfield_list_arr.length){
														for (int k=0; k<indirect_tag_list_arr.length;k++){
															SKLogger.writeLog("#RLOS Common inside child node 1 ", "node name : "+childnode.item(0).getNodeName() +" node data: "+childnode.item(0).getTextContent());
															if(tag_value.equalsIgnoreCase(childnode.item(0).getTextContent()) && indirect_child_tag.equalsIgnoreCase(childnode.item(0).getNodeName())){
																for (int child_node_len = 2 ;child_node_len< childnode.getLength();child_node_len++){
																	SKLogger.writeLog("#RLOS common: ", "child node IF: Child node value: "+ child_node_len+" name: "+ childnode.item(child_node_len).getNodeName()+" child node value: "+ childnode.item(child_node_len).getTextContent());

																	if(childnode.item(child_node_len).getNodeName().equalsIgnoreCase(indirect_tag_list_arr[k])){

																		SKLogger.writeLog("child_node_len","getTextContent"+childnode.item(child_node_len).getNodeName());
																		SKLogger.writeLog("child_node_len","getTextContent"+childnode.item(child_node_len).getTextContent());
																		SKLogger.writeLog("RLOS common: getOutputXMLValues","");
																		SKLogger.writeLog(""+indirect_formfield_list_arr[k]," :"+childnode.item(child_node_len).getTextContent());
																		formObject.setNGValue(indirect_formfield_list_arr[k],childnode.item(child_node_len).getTextContent(),false);
																	}
																}

															}
														}
													}
													else{
														SKLogger.writeLog("RLOS common: getOutputXMLValues","Error as for :"+xmltag_name+" indirect_formfield_list and indirect_tag_list dosent match");
													} 
												}
												else{
													SKLogger.writeLog("#RLOS common indirect field values","inside indirect mapping part2 indirect_tag_list without ,");
													SKLogger.writeLog("RLOS Common indirect mapping deepak changes ", "tag_value: "+ tag_value+" indirect_child_tag: "+indirect_child_tag );
													SKLogger.writeLog("RLOS Common indirect mapping deepak changes ", "textcontent: "+ childnode.item(0).getTextContent()+" NodeName: "+childnode.item(0).getNodeName() );
													//changed for data populating in the Contact Details Frame
													if(tag_value.equalsIgnoreCase(childnode.item(0).getTextContent()) && indirect_child_tag.equalsIgnoreCase(childnode.item(0).getNodeName())){
														for (int child_node_len = 0 ;child_node_len< childnode.getLength();child_node_len++){
															SKLogger.writeLog("#RLOS common: ", "child node IF: Child node value: "+ child_node_len+" name: "+ childnode.item(child_node_len).getNodeName()+" child node value: "+ childnode.item(child_node_len).getTextContent());
															if(childnode.item(child_node_len).getNodeName().equalsIgnoreCase(indirect_tag_list)){

																SKLogger.writeLog("child_node_len","getTextContent"+childnode.item(child_node_len).getNodeName());
																SKLogger.writeLog("child_node_len","getTextContent"+childnode.item(child_node_len).getTextContent());
																SKLogger.writeLog("RLOS common: getOutputXMLValues","");
																SKLogger.writeLog(""+indirect_formfield_list," :"+childnode.item(child_node_len).getTextContent());
																formObject.setNGValue(indirect_formfield_list,childnode.item(child_node_len).getTextContent(),false);
															}
														}
													}
													//changed for data populating in the Contact Details Frame
												}


											}
										}     
									}
									//List<List<String>> outputIndirectXML=formObject.getNGDataFromDataCache(sQuery);
									//System.out.println("$$outputIndirectXML "+outputIndirectXML.get(0).get(0)+outputIndirectXML.get(0).get(1)+outputIndirectXML.get(0).get(2));

								}
								if(indirectMapping.equalsIgnoreCase("N") && gridMapping.equalsIgnoreCase("N"))
								{    
									SKLogger.writeLog("check14 " ,"check");
									tagValue = getTagValue(outputXMLMsg,nl.item(i).getNodeName());
									SKLogger.writeLog("Node value ","tagValue:"+tagValue);
									SKLogger.writeLog("Node form_control ","form_control:"+ form_control);

									SKLogger.writeLog("$$tagValue NN  ",tagValue);
									SKLogger.writeLog("$$form_control  NN ",form_control);
									if(form_control.equalsIgnoreCase("cmplx_FATCA_selectedreason")){
										formObject.addItem(form_control,tagValue);
									}
									else{
										formObject.setNGValue(form_control,tagValue,false);
									}

								}
							}
						}
					}
					//till for loop

				}
			}
		}
		catch(Exception e)
		{
			SKLogger.writeLog("Exception occured in getOutputXMLValues:  ",e.getMessage());

		}
	}


	public static String getTagValue(String xml, String tag) throws ParserConfigurationException, SAXException, IOException 
	{   
		//System.out.println("Tag:"+tag+" XML:"+xml);
		Document doc = getDocument(xml);
		NodeList nodeList = doc.getElementsByTagName(tag);

		int length = nodeList.getLength();
		//System.out.println("NodeList Length: " + length);

		if (length > 0) {
			Node node =  nodeList.item(0);
			//System.out.println("Node : " + node);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				NodeList childNodes = node.getChildNodes();
				String value = "";
				int count = childNodes.getLength();
				for (int i = 0; i < count; i++) {
					Node item = childNodes.item(i);
					if (item.getNodeType() == Node.TEXT_NODE) {
						value += item.getNodeValue();
					}
				}
				return value;
			} else if (node.getNodeType() == Node.TEXT_NODE) {
				return node.getNodeValue();
			}

		}
		return "";
	}

	/* public void valueSetCustomer(String outputResponse)
	    {
	        SKLogger.writeLog("RLOSCommon valueSetCustomer", "Inside valueSetCustomer():");
	        String outputXMLHead = "";
	        String outputXMLMsg = "";
	        String returnDesc = "";
	        String returnCode = "";
	        String response= "";
	        XMLParser objXMLParser = new XMLParser();
	        try
	        {
	            if(outputResponse.indexOf("<EE_EAI_HEADER>")>-1)
	            {
	                outputXMLHead=outputResponse.substring(outputResponse.indexOf("<EE_EAI_HEADER>"),outputResponse.indexOf("</EE_EAI_HEADER>")+16);
	                SKLogger.writeLog("RLOSCommon valueSetCustomer", "outputXMLHead");
	            }
	            objXMLParser.setInputXML(outputXMLHead);
	            if(outputXMLHead.indexOf("<MsgFormat>")>-1)
	            {
	                response= outputXMLHead.substring(outputXMLHead.indexOf("<MsgFormat>")+11,outputXMLHead.indexOf("</MsgFormat>"));
	                SKLogger.writeLog("$$response ",response);
	            }
	            if(outputXMLHead.indexOf("<ReturnDesc>")>-1)
	            {
	                returnDesc= outputXMLHead.substring(outputXMLHead.indexOf("<ReturnDesc>")+12,outputXMLHead.indexOf("</ReturnDesc>"));
	                SKLogger.writeLog("$$returnDesc ",returnDesc);
	            }
	            if(outputXMLHead.indexOf("<ReturnCode>")>-1)
	            {
	                returnCode= outputXMLHead.substring(outputXMLHead.indexOf("<ReturnCode>")+12,outputXMLHead.indexOf("</ReturnCode>"));
	                SKLogger.writeLog("$$returnCode ",returnCode);
	            }

	            if(outputResponse.indexOf("<MQ_RESPONSE_XML>")>-1)
	            {
	                outputXMLMsg=outputResponse.substring(outputResponse.indexOf("<MQ_RESPONSE_XML>")+17,outputResponse.indexOf("</MQ_RESPONSE_XML>"));
	                //SKLogger.writeLog("$$outputXMLMsg ",outputXMLMsg);
	                SKLogger.writeLog("$$outputXMLMsg getOutputXMLValues","check inside getOutputXMLValues");
	                getOutputXMLValues(outputXMLMsg,response);
	                SKLogger.writeLog("$$outputXMLMsg ","outputXMLMsg");
	            }
	            //ended by me
	        }
	        catch(Exception e)
	        {            
	            SKLogger.writeLog("Exception occured in valueSetCustomer method:  ",e.getMessage());
	            System.out.println("Exception occured in valueSetCustomer method: "+ e.getMessage());
	        }
	    }*/

	public static Document getDocument(String xml) throws ParserConfigurationException, SAXException, IOException
	{
		// Step 1: create a DocumentBuilderFactory
		DocumentBuilderFactory dbf =
			DocumentBuilderFactory.newInstance();

		// Step 2: create a DocumentBuilder
		DocumentBuilder db = dbf.newDocumentBuilder();

		// Step 3: parse the input file to get a Document object
		Document doc = db.parse(new InputSource(new StringReader(xml)));
		return doc;
	}  

	public void loadAllCombo_RLOS_Documents_Deferral(){
		SKLogger.writeLog(" Inside loadAllCombo_LeadManagement_Documents_Deferral", "inside loadAllCombo_RLOS_Documents_Deferral");

		FormContext.getCurrentInstance().getFormConfig().getConfigElement("ProcessName");

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		SKLogger.writeLog(" Inside loadAllCombo_LeadManagement_Documents_Deferral", "");

		formObject.getNGDataFromDataCache("SELECT DocName FROM ng_rlos_DocTable with (nolock) WHERE ProcessName='RLOS'","DocCategory");
	} 

	public void setMailId(String userName)
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		SKLogger.writeLog(" Inside setMailId", "");
		try
		{			
			String squery= "select mailid from pdbuser with (nolock) WHERE UPPER(USERNAME)=UPPER('"+userName+"')";
			List<List<String>> outputindex = null;
			outputindex = formObject.getNGDataFromDataCache(squery);
			SKLogger.writeLog("RLOS Initiation", "mailID outputItemindex is: " +  outputindex);
			String mailID =outputindex.get(0).get(0);
			SKLogger.writeLog("RLOS Initiation", "mailID is:" +  mailID);
			formObject.setNGValue("processby_email",  mailID);

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void fetchIncomingDocRepeater(){

		SKLogger.writeLog(" Inside loadAllCombo_LeadManagement_Documents_Deferral", "inside fetchIncomingDocRepeater");

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		SKLogger.writeLog("INSIDE INCOMING DOCUMENT:" ,"");
		String requested_product="";
		String requested_subproduct="";
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		SKLogger.writeLog("INSIDE INCOMING DOCUMENT value_doc_name:" ,"valu of row count"+n);
		if(n>0)
		{
			for(int i=0;i<n;i++)
			{
				requested_product= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 1);
				SKLogger.writeLog("INSIDE INCOMING DOCUMENT value_doc_name:" ,requested_product);
				requested_subproduct= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2);
				SKLogger.writeLog("INSIDE INCOMING DOCUMENT value_doc_name:" ,requested_subproduct);

			}
		}
		SKLogger.writeLog("INSIDE INCOMING DOCUMENT value_doc_name outsid for:" ,requested_product);


		List<String> repeaterHeaders = new ArrayList<String>();

		repeaterHeaders.add("Document Name");

		repeaterHeaders.add("Expiry Date");
		repeaterHeaders.add("Mandatory");
		//repeaterHeaders.add("Deferred/Waived");
		repeaterHeaders.add("Status");
		repeaterHeaders.add("Remarks");

		repeaterHeaders.add("Add from DMS");

		repeaterHeaders.add("Add from PC");

		repeaterHeaders.add("Scan");
		repeaterHeaders.add("View");

		repeaterHeaders.add("Print");

		repeaterHeaders.add("Download");

		SKLogger.writeLog("INSIDE INCOMING DOCUMENT:" ,"after making headers");



		FormContext.getCurrentInstance().getFormConfig().getConfigElement("ProcessName");


		List<List<String>> docName = null;
		String documentName = null;
		String documentNameMandatory=null;

		String query = "";

		IRepeater repObj=null;

		int repRowCount = 0;



		repObj = formObject.getRepeaterControl("IncomingDoc_Frame");

		SKLogger.writeLog("INSIDE INCOMING DOCUMENT:" ,""+repObj.toString());



		query = "SELECT distinct DocName,Mandatory FROM ng_rlos_DocTable WHERE ProductName='"+requested_product+"' and SubProductName='"+requested_subproduct+"' and ProcessName='RLOS'";

		// ProcessDefId IN" + "(SELECT ProcessDefId FROM PROCESSDEFTABLE WHERE ProcessName ='"+processName+"')";




		docName = formObject.getDataFromDataSource(query);
		SKLogger.writeLog("Incomingdoc",""+ docName);

		try{

			if (repObj.getRepeaterRowCount() != 0) {

				SKLogger.writeLog("RLOS incoming document", "CLeared repeater object");

				repObj.clear();

			}
			repObj.setRepeaterHeaders(repeaterHeaders);

			for(int i=0;i<docName.size();i++ ){
				repObj.addRow();

				documentName = docName.get(i).get(0);
				documentNameMandatory = docName.get(i).get(1);

				SKLogger.writeLog("Column Added in Repeater documentName"," "+ documentName);
				SKLogger.writeLog("Column Added in Repeater documentNameMandatory"," "+ documentNameMandatory);

				repObj.setValue(i, 0, documentName);
				repObj.setValue(i, 2, documentNameMandatory);
				repRowCount = repObj.getRepeaterRowCount();

				SKLogger.writeLog("Repeater Row Count ", " " + repRowCount);

			}
			// added by abhishek
			repObj.setColumnDisabled(0, true);
			repObj.setColumnDisabled(2, true);

		}

		catch (Exception e) {

			SKLogger.writeLog("EXCEPTION    :    ", " " + e.toString());

		} finally {

			repObj = null;

			repeaterHeaders = null;         
		}
	}

	public void loadInDecGrid()
	{

		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		try{
			String ParentWI_Name = formObject.getNGValue("Parent_WIName");
			String query="select FORMAT(datelastchanged,'dd-MM-yyyy hh:mm'),userName,workstepName,Decisiom,remarks,wi_nAme from ng_rlos_gr_Decision with (nolock) where wi_nAme='"+ParentWI_Name+"' or wi_nAme='"+formObject.getWFWorkitemName()+"'";
			SKLogger.writeLog("PersonnalLoanS> ","Inside PLCommon ->loadInDecGrid()"+query);
			List<List<String>> list=formObject.getDataFromDataSource(query);
			SKLogger.writeLog("PersonnalLoanS> ","Inside PLCommon ->loadInDecGrid()"+list.get(0).get(0)+list.get(0).get(1)+list.get(0).get(2)+list.get(0).get(3)+list.get(0).get(4));
			/* try{
			 String date=list.get(0).get(0);
			 Date d=new SimpleDateFormat("MM/dd/yyyy HH:mm").parse(date);
			 SKLogger.writeLog("PersonnalLoanS>","value of date is:"+d.toString());*/
			for (List<String> a : list) 
			{
				/*List<String> mylist=new ArrayList<String>();
				 mylist.add(d.toString());
				 mylist.add(list.get(0).get(1));
				 mylist.add(list.get(0).get(2));
				 mylist.add(list.get(0).get(3));
				 mylist.add(list.get(0).get(4));
				 mylist.add(list.get(0).get(5));*/
				formObject.addItem("Decision_ListView1", a);
			}
		}catch(Exception e){  SKLogger.writeLog("PLCommon","Exception Occurred loadInDecGrid :"+e.getMessage());}	
	}
	public void parse_cif_eligibility(String output){

		try{
			output=output.substring(output.indexOf("<MQ_RESPONSE_XML>")+17,output.indexOf("</MQ_RESPONSE_XML>"));
			SKLogger.writeLog("inside parse_cif_eligibility","");
			Map<String, HashMap<String, String> > Cus_details = new HashMap<String, HashMap<String, String>>();
			String passport_list = "";
			//Map<String, String> cif_details = new HashMap<String, String>();
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(output)));
			SKLogger.writeLog("name is doc : ", doc+"");
			NodeList nl = doc.getElementsByTagName("*");

			for (int nodelen=0; nodelen<nl.getLength();nodelen++){
				Map<String, String> cif_details = new HashMap<String, String>();
				nl.item(nodelen);
				if(nl.item(nodelen).getNodeName().equalsIgnoreCase("CustomerDetails"))
				{
					int no_of_product = 0;
					NodeList childnode  = nl.item(nodelen).getChildNodes();
					for (int childnodelen= 0;childnodelen<childnode.getLength();childnodelen++){
						String tag_name = childnode.item(childnodelen).getNodeName();
						String tag_value=childnode.item(childnodelen).getTextContent();
						if(tag_name!=null && tag_value!=null){
							if(tag_name.equalsIgnoreCase("Products")){
								++no_of_product;
								cif_details.put(tag_name, no_of_product+"");
							}else{
								if(tag_name.equalsIgnoreCase("PassportNum"))
									passport_list = tag_value+ ","+passport_list;
								cif_details.put(tag_name, tag_value);
							}

						}
						else{
							SKLogger.writeLog("tag name and tag value can not be null:: tag name: ",tag_name+ " tag value: " +tag_value);
						}

					}
					Cus_details.put(cif_details.get("CustId"), (HashMap<String, String>) cif_details) ;
				}
			}
			int Prim_cif = primary_cif_identify(Cus_details);
			save_cif_data(Cus_details,Prim_cif);
			if(Prim_cif!=0){
				SKLogger.writeLog("parse_cif_eligibility Primary CIF: ",Prim_cif+"");
				Map<String, String> prim_entry = new HashMap<String, String>();
				prim_entry = Cus_details.get(Prim_cif+"");
				String primary_pass = prim_entry.get("PassportNum");
				passport_list = passport_list.replace(primary_pass, "");
				set_nonprimaryPassport(Prim_cif,passport_list);
			}

			SKLogger.writeLog("Prim_cif: ", Prim_cif+"");

		}
		catch(Exception e){
			SKLogger.writeLog("Exception occured while parsing Customer Eligibility : ", e.getMessage());
		}

	}
	public void save_cif_data(Map<String, HashMap<String, String>> cusDetails ,int prim_cif){
		try{

			SKLogger.writeLog("RLSOCommon", "inside save_cif_data methos");
			FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
			String WI_Name = formObject.getWFWorkitemName();

			SKLogger.writeLog("RLSOCommon", "inside save_cif_data methos wi_name: "+WI_Name );
			Map<String, String> curr_entry = new HashMap<String, String>();
			Iterator<Map.Entry<String, HashMap<String, String>>> itr = cusDetails.entrySet().iterator();

			while(itr.hasNext()){
				List<String> Cif_data=new ArrayList<String>();
				Map.Entry<String, HashMap<String, String>> entry =  itr.next();
				curr_entry = entry.getValue();

				Cif_data.add(curr_entry.get("SearchType"));
				Cif_data.add(curr_entry.get("CustId"));
				Cif_data.add(curr_entry.get("PassportNum"));
				Cif_data.add(curr_entry.get("BlacklistFlag"));
				Cif_data.add(curr_entry.get("DuplicationFlag"));
				Cif_data.add(curr_entry.get("NegatedFlag"));
				Cif_data.add(curr_entry.get("Products"));
				if(curr_entry.get("CustId").equalsIgnoreCase(prim_cif+"")){
					Cif_data.add("Y");
				}
				else{
					Cif_data.add("N");	
				}
				Cif_data.add(WI_Name);
				formObject.addItem("q_cif_detail", Cif_data);

				SKLogger.writeLog("RLSOCommon", "data to dave in cif details grid: "+ Cif_data);
				curr_entry=null;
				Cif_data=null;
			}
		}
		catch(Exception e){
			SKLogger.writeLog("Exception occured while saving data for Customer Eligibility : ", e.getMessage());
		}


	}
	public int primary_cif_identify(Map<String, HashMap<String, String>> cusDetails )
	{
		int primary_cif = 0;
		try{
			Map<String, String> prim_entry = new HashMap<String, String>();
			Map<String, String> curr_entry = new HashMap<String, String>();


			Iterator<Map.Entry<String, HashMap<String, String>>> itr = cusDetails.entrySet().iterator();
			while(itr.hasNext()){
				Map.Entry<String, HashMap<String, String>> entry =  itr.next();

				curr_entry = entry.getValue();
				if(curr_entry.get("SearchType").equalsIgnoreCase("Internal"))
				{
					if(primary_cif==0 && curr_entry.containsKey("Products")){
						primary_cif = Integer.parseInt(entry.getKey());
					}
					else if(curr_entry.containsKey("Products"))
					{
						prim_entry = cusDetails.get(primary_cif+"");
						int prim_entry_prod_no = Integer.parseInt(prim_entry.get("Products"));
						int curr_entry_prod_no = Integer.parseInt(curr_entry.get("Products"));

						if(curr_entry_prod_no>prim_entry_prod_no){
							primary_cif = Integer.parseInt(entry.getKey());
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
			SKLogger.writeLog("Exception occured while parsing Customer Eligibility : ", e.getMessage());
		}

		return primary_cif;
	}
	public void set_nonprimaryPassport(int cif_id, String pass_list){
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
			SKLogger.writeLog("Exception occured while seting non primary CIF: ", e.getMessage());
		}

	}
	public void Customer_enable()
	{
		SKLogger.writeLog("RLOSCommon: Customer_enable()", "inside Customer enable method");
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
		SKLogger.writeLog("RLOSCommon: Customer_enable()", "End Customer enable method");
	}
	public void SetDisableCustomer()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String fields="cmplx_Customer_Title,cmplx_Customer_ResidentNonResident,cmplx_Customer_gender,cmplx_Customer_age,cmplx_Customer_MotherName,cmplx_Customer_EmirateIDExpiry,cmplx_Customer_IdIssueDate,cmplx_Customer_VisaNo,cmplx_Customer_PassPortExpiry,cmplx_Customer_VIsaExpiry,cmplx_Customer_SecNAtionApplicable,cmplx_Customer_SecNationality,cmplx_Customer_MAritalStatus,cmplx_Customer_COuntryOFResidence,cmplx_Customer_EMirateOfVisa,cmplx_Customer_EmirateOfResidence,cmplx_Customer_yearsInUAE,cmplx_Customer_CustomerCategory,cmplx_Customer_GCCNational,cmplx_Customer_VIPFlag"; 
		String[] field_array=fields.split(",");
		for(int i=0;i<field_array.length;i++)
			formObject.setEnabled("DecisionHistory_Button5", true);
	}

	public String getCustAddress_details(){
		SKLogger.writeLog("RLOSCommon java file", "inside getCustAddress_details : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int add_row_count = formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
		SKLogger.writeLog("RLOSCommon java file", "inside getCustAddress_details add_row_count+ : "+add_row_count);
		String  add_xml_str ="";
		for (int i = 0; i<add_row_count;i++){
			String Address_type = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0); //0
			String Po_Box=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 1);//1
			String flat_Villa=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 2);//2
			String Building_name=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 3);//3
			String street_name=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 4);//4
			String Landmard = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 5);//5
			String city=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 6);//6
			String Emirates=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 7);//7
			String country=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 8);//8
			String years_in_current_add=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 9);//9
			String preferrd="";
			if(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 10).equalsIgnoreCase("true"))//10
				preferrd = "Y";
			else
				preferrd = "N";

			add_xml_str = add_xml_str + "<AddressDetails><AddressType>"+Address_type+"</AddressType><AddrPrefFlag>"+preferrd+"</AddrPrefFlag><PrefFormat>STRUCTURED_FORMAT</PrefFormat>";
			add_xml_str = add_xml_str + "<AddrLine1>"+flat_Villa+"</AddrLine1>";
			add_xml_str = add_xml_str + "<AddrLine2>"+Building_name+"</AddrLine2>";
			add_xml_str = add_xml_str + "<AddrLine3>"+street_name+"</AddrLine3>";
			add_xml_str = add_xml_str + "<AddrLine4>"+Landmard+"</AddrLine4>";
			add_xml_str = add_xml_str + "<City>"+city+"</City>";
			add_xml_str = add_xml_str + "<CountryCode>"+country+"</CountryCode>";
			add_xml_str = add_xml_str + "<State>"+Emirates+"</State>";
			add_xml_str = add_xml_str + "<POBox>"+Po_Box+"</POBox></AddressDetails>";
		}
		SKLogger.writeLog("RLOSCommon", "Address tag Cration: "+ add_xml_str);
		return add_xml_str;
	}
	public void IncomingDoc(String itemIndex)
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		List<List<String>> outputindex = null;
		List<List<String>> secondquery=null;

		//code for IncomingDocument started here
		//HashMap<String,String> hm1= new HashMap<String,String>();
		String requested_product="";
		String requested_subproduct="";
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		SKLogger.writeLog("INSIDE INCOMING DOCUMENT value_doc_name:" ,"valu of row count"+n);
		if(n>0)
		{
			for(int i=0;i<n;i++)
			{
				requested_product= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 1);
				SKLogger.writeLog("INSIDE INCOMING DOCUMENT requested_product:" ,requested_product);
				requested_subproduct= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2);
				SKLogger.writeLog("INSIDE INCOMING DOCUMENT requested_subproduct:" ,requested_subproduct);

			}
		}
		String sQuery="SELECT Name,DocumentIndex FROM PDBDocument with(nolock) WHERE DocumentIndex IN (SELECT DocumentIndex FROM PDBDocumentContent with(nolock) WHERE ParentFolderIndex= '"+itemIndex+"')";


		outputindex = null;
		SKLogger.writeLog("RLOS Initiation", "sQuery for document name is:" +  sQuery);
		outputindex = formObject.getDataFromDataSource(sQuery);
		//	     outputindex=outputindex.get(0).get(0);


		SKLogger.writeLog("RLOS Initiation", "outputItemindex is:" +  outputindex);


		IRepeater repObj=null;
		repObj = formObject.getRepeaterControl("IncomingDoc_Frame");
		SKLogger.writeLog("RLOS Initiation","repObj::"+repObj+"row::+"+repObj.getRepeaterRowCount());
		if(outputindex==null || outputindex.size()==0) {
			SKLogger.writeLog("","output index is blank");
			String  query = "SELECT distinct DocName,Mandatory FROM ng_rlos_DocTable WHERE ProductName='"+requested_product+"' and SubProductName='"+requested_subproduct+"' and ProcessName='RLOS'";
			SKLogger.writeLog("RLOS Initiation", "sQuery for document name is:" +  query);
			secondquery = formObject.getDataFromDataSource(query);
			for(int j = 0; j < secondquery.size(); j++) {
				if("Y".equalsIgnoreCase(secondquery.get(j).get(1))) {
					//added to update the flag for Deferred and Waived case
					String StatusValue=repObj.getValue(j,"cmplx_DocName_Status");
					SKLogger.writeLog("","StatusValue::"+StatusValue);
					if(!(StatusValue.equalsIgnoreCase("Deferred") || StatusValue.equalsIgnoreCase("Waived"))){
						throw new ValidatorException(new FacesMessage("You have not attached Mandatory Documents"));
					}
					else if(StatusValue.equalsIgnoreCase("Deferred")){
						formObject.setNGValue("is_deferral_approval_require","Y");
						formObject.RaiseEvent("WFSave");
						SKLogger.writeLog("Deferred flag value inside no document",formObject.getNGValue("is_deferral_approval_require"));
					}
					else if(StatusValue.equalsIgnoreCase("Waived")){
						formObject.setNGValue("is_waiver_approval_require","Y");
						formObject.RaiseEvent("WFSave");
						SKLogger.writeLog("waived flag value inside no document",formObject.getNGValue("is_waiver_approval_require"));
					}
					//ended to update the flag for Deferred and Waived case
				}
			}
		}

		List<List<String>> outputupdateindex = null;
		String Document_Name =outputindex.get(0).get(0);
		String DocIndex=outputindex.get(0).get(1);
		SKLogger.writeLog("RLOS Initiation", "Document_Index Document_Name is:" + Document_Name);
		SKLogger.writeLog("RLOS Initiation", "DocIndex is:" +  DocIndex);
		String[] arrval=new String[outputindex.size()];
		String[] arrdocIndex=new String[outputindex.size()];
		//IRepeater repObj=null;
		//	    repObj = formObject.getRepeaterControl("IncomingDoc_Frame");
		//	    SKLogger.writeLog("RLOS Initiation","repObj::"+repObj+"row::+"+repObj.getRepeaterRowCount());
		if(outputindex != null && outputindex.size() != 0)
		{
			System.out.println("Staff List "+outputindex);
			for(int i = 0; i < outputindex.size(); i++)
			{
				arrval[i]=outputindex.get(i).get(0);
				arrdocIndex[i]=outputindex.get(i).get(1);
				SKLogger.writeLog("","docIndex array is :"+arrdocIndex[i]);
				SKLogger.writeLog("RLOS Initiation", " doc index sMap is:" +  outputindex.get(i).get(1));
				SKLogger.writeLog("RLOS Initiation", "outputItemindex of update is:" +  outputupdateindex);
			}
		}

		for(int k=0;k<arrval.length;k++)
		{
			SKLogger.writeLog("RLOS Initiation", " arrval is:" +  arrval[k]);
		}

		SKLogger.writeLog("INSIDE INCOMING DOCUMENT value_doc_name outsid for:" ,requested_product);
		String  query = "SELECT distinct DocName,Mandatory FROM ng_rlos_DocTable WHERE ProductName='"+requested_product+"' and SubProductName='"+requested_subproduct+"' and ProcessName='RLOS'";
		outputindex = null;
		SKLogger.writeLog("RLOS Initiation", "sQuery for document name is:" +  query);
		outputindex = formObject.getDataFromDataSource(query);
		SKLogger.writeLog("RLOS Initiation", "outputItemindex is:" +  outputindex+"::Size::"+outputindex.size());
		//IRepeater repObj=null;
		//	    repObj = formObject.getRepeaterControl("IncomingDoc_Frame");
		//	    SKLogger.writeLog("RLOS Initiation","repObj::"+repObj+"row::+"+repObj.getRepeaterRowCount());
		String [] misseddoc=new String[outputindex.size()];
		int count=0;
		for(int j = 0; j < outputindex.size(); j++)

		{
			String DocName =outputindex.get(j).get(0);
			String Mandatory =outputindex.get(j).get(1);
			SKLogger.writeLog("RLOS Initiation", "Document_Index Document_Name is:" + DocName+","+Mandatory);

			if (repObj.getRepeaterRowCount() != 0) {
				if(Mandatory.equalsIgnoreCase("Y")){

					int l=0;

					while(l<arrval.length)
					{
						SKLogger.writeLog("","DocName::"+DocName+":str:"+arrval[l]);

						if(arrval[l].equalsIgnoreCase(DocName))
						{
							SKLogger.writeLog("","document is present in the list");
							SKLogger.writeLog("DocIndex","DocIndex value for the corrseponding document"+arrdocIndex[l]);
							repObj.setValue(j, "cmplx_DocName_DocIndex",arrdocIndex[l]);

							String StatusValue=repObj.getValue(j,"cmplx_DocName_Status");
							SKLogger.writeLog("","StatusValue::"+StatusValue);
							if(!StatusValue.equalsIgnoreCase("Recieved")){
								repObj.setValue(j,"cmplx_DocName_Status","Recieved");
								repObj.setEditable(j, "cmplx_DocName_Status", false);
								SKLogger.writeLog("","StatusValue::123final"+StatusValue);
							}
							count=1;
							SKLogger.writeLog("","StatusValue::123final count value:"+count);
							//continue;
						}


						//    count=0;
						l++;
						SKLogger.writeLog("","StatusValue::123final count value:"+l);
					}
					if(count==0){
						SKLogger.writeLog("","Document is not present in the list");
						misseddoc[j]=DocName;
						//l++;
						SKLogger.writeLog("RLOS Initiation", " misseddoc is in j is:" +  misseddoc[j]);

						String StatusValue=repObj.getValue(j, "cmplx_DocName_Status");
						SKLogger.writeLog("","StatusValue::"+StatusValue);
						String Remarks=repObj.getValue(j, "cmplx_DocName_Remarks");
						SKLogger.writeLog("","Remarks::"+Remarks);
						//   if(!(StatusValue.equalsIgnoreCase("Recieved")||StatusValue.equalsIgnoreCase("Deferred"))){
						/*if(!(StatusValue.equalsIgnoreCase("Recieved"))){
	                         if(Remarks.equalsIgnoreCase("")||Remarks.equalsIgnoreCase(null)||Remarks.equalsIgnoreCase("null")){
	                         SKLogger.writeLog("It is Mandatory to fill Remarks","As you have not attached the Mandatory Document fill the Remarks");
	                         throw new ValidatorException(new FacesMessage("As you have not attached the Mandatory Document fill the Remarks"));
	                         }

	                         else if(!Remarks.equalsIgnoreCase("")||Remarks.equalsIgnoreCase(null)||Remarks.equalsIgnoreCase("null")){
	                             SKLogger.writeLog("You may proceed further","Proceed further");
	                         }
	                     }*/
						//added to update the flag for Deferred and Waived case
						if(StatusValue.equalsIgnoreCase("Deferred")){
							formObject.setNGValue("is_deferral_approval_require","Y");
							formObject.RaiseEvent("WFSave");
							SKLogger.writeLog("Deferred flag value inside some documents are present",formObject.getNGValue("is_deferral_approval_require"));

						}
						if(StatusValue.equalsIgnoreCase("Waived")){
							formObject.setNGValue("is_waiver_approval_require","Y");
							formObject.RaiseEvent("WFSave");
							SKLogger.writeLog("Waived flag value inside some documents are present",formObject.getNGValue("is_waiver_approval_require"));
						}
						//ended to update the flag for Deferred and Waived case
					}
				}

			}
			count=0;
		}
		StringBuilder mandatoryDocName = new StringBuilder("");
		for(int k=0;k<misseddoc.length;k++)
		{
			if(null != misseddoc[k]) {
				mandatoryDocName.append(misseddoc[k]).append(",");
			}
			SKLogger.writeLog("RLOS Initiation", "misseddoc is:" +misseddoc[k]);
		}
		mandatoryDocName.setLength(Math.max(mandatoryDocName.length()-1,0));
		SKLogger.writeLog("RLOS Initiation", "misseddoc is:" +mandatoryDocName.toString());
		throw new ValidatorException(new FacesMessage("You have not attached Mandatory Documents: "+mandatoryDocName.toString()));

		//code for IncomingDocument ended here    

	}
	public void  ParentToChild(){
		SKLogger.writeLog("RLOS","RLOSCommon Inside ParentTochild() ");

		FormReference formObject=FormContext.getCurrentInstance().getFormReference();
		String mystring="",product="";

		try{
			int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			for(int i=0;i<n;i++)	
			{
				mystring=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1);
				SKLogger.writeLog("RLOS","Inside ParentTochild() mystring:"+mystring);
				if(!product.contains(mystring))
					product+=mystring;			
			}

			formObject.setNGValue("Product_Type", product);
			SKLogger.writeLog("RLOS","Inside ParentToChild -> n :"+n+"Product_Type is:"+product);
		}catch(Exception e){
			SKLogger.writeLog("RLOS","Inside ParentToChild ->Exception occurred:"+e.getMessage());

		}
	}

	//new method to fetch customer details for primary customer.
	public String fetch_cust_details_primary(){
		String outputResponse="";
		String ReturnCode="";
		String alert_msg="";
		try{

			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			outputResponse = GenerateXML("CUSTOMER_DETAILS","Primary_CIF");
			SKLogger.writeLog("RLOS value of ReturnCode","Inside Customer");
			String Gender =  (outputResponse.contains("<Gender>")) ? outputResponse.substring(outputResponse.indexOf("<Gender>")+"</Gender>".length()-1,outputResponse.indexOf("</Gender>")):"";
			SKLogger.writeLog("RLOS value of Gender",Gender);
			String  Marital_Status =  (outputResponse.contains("<MaritialStatus>")) ? outputResponse.substring(outputResponse.indexOf("<MaritialStatus>")+"</MaritialStatus>".length()-1,outputResponse.indexOf("</MaritialStatus>")):"";
			SKLogger.writeLog("RLOS value of Marital_Status",Marital_Status);
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);

			if(ReturnCode.equalsIgnoreCase("0000") ){
				alert_msg="Existing Customer Details fetched Sucessfully";
				formObject.setVisible("Customer_Frame2", true);
				formObject.setNGValue("cmplx_Customer_IscustomerSave", "Y");
				//code to enter started difference of years+month in UAE field
				String ResideSince =  (outputResponse.contains("<ResideSince>")) ? outputResponse.substring(outputResponse.indexOf("<ResideSince>")+"</ResideSince>".length()-1,outputResponse.indexOf("</ResideSince>")):"";    
				SKLogger.writeLog("RLOS value of ResideSince",ResideSince);
				if(!ResideSince.equalsIgnoreCase("")){
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					Calendar cal = Calendar.getInstance();
					int CurrentYear=cal.get(Calendar.YEAR);
					int CurrentMonth=cal.get(Calendar.MONTH)+1;
					int CurrentDate=cal.get(Calendar.DATE);
					SKLogger.writeLog("RLOS value of CurrentDate",""+CurrentYear+"-"+CurrentMonth+"-"+CurrentDate);

					Date d1 = null;
					Date d2 = null;
					try {
						d1 = format.parse(ResideSince);
						d2 = format.parse(""+CurrentYear+"-"+CurrentMonth+"-"+CurrentDate);
					} catch (Exception ex) {
					}
					int totalDays = (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
					int diffYear = totalDays/365;
					int diffdays = totalDays%365;
					int diffMonths=0;
					if(diffdays>=30) {
						diffMonths = diffdays/30;
						diffdays = diffdays%30;
					}
					SKLogger.writeLog("year difference:","year difference"+diffYear);
					SKLogger.writeLog("year difference:","diffMonths difference"+diffMonths);
					SKLogger.writeLog("year difference:","diffdays difference"+diffdays);
					formObject.setNGValue("cmplx_Customer_yearsInUAE",diffYear+"."+diffMonths);
					SKLogger.writeLog("year difference:","diffdays difference"+formObject.getNGValue("cmplx_Customer_yearsInUAE")); 
				}

				//code to enter started difference of years+month in UAE field

				formObject.fetchFragment("Address_Details_Container", "AddressDetails","q_AddressDetails");    
				formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
				formObject.fetchFragment("FATCA", "FATCA", "q_FATCA");
				formObject.fetchFragment("KYC", "KYC", "q_KYC");
				formObject.fetchFragment("OECD", "OECD", "q_OECD");
				formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmploymentDetails");
				formObject.setTop("Alt_Contact_container",formObject.getTop("Address_Details_Container")+formObject.getHeight("Address_Details_Container"));
				formObject.setTop("CardDetails", formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container"));
				if(formObject.isVisible("Supplementary_Container")){
					formObject.setTop("Supplementary_Container", formObject.getTop("CardDetails")+formObject.getHeight("CardDetails"));  

					formObject.setTop("FATCA", formObject.getTop("Supplementary_Container")+formObject.getHeight("Supplementary_Container"));
					formObject.setTop("KYC", formObject.getTop("FATCA")+formObject.getHeight("FATCA"));
					formObject.setTop("OECD", formObject.getTop("KYC")+formObject.getHeight("KYC"));
					formObject.setTop("ReferenceDetails", formObject.getTop("OECD")+formObject.getHeight("OECD")+10);
				}
				else{
					formObject.setTop("FATCA", formObject.getTop("CardDetails")+formObject.getHeight("CardDetails"));
					formObject.setTop("KYC", formObject.getTop("FATCA")+formObject.getHeight("FATCA")+5);
					formObject.setTop("OECD", formObject.getTop("KYC")+formObject.getHeight("KYC")+10);
					formObject.setTop("ReferenceDetails", formObject.getTop("OECD")+formObject.getHeight("OECD")+5);
				}
				valueSetCustomer(outputResponse,"Primary_CIF");
				//code to set Emirates of residence start.


				int n=formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
				if(n>0)
				{
					for(int i=0;i<n;i++)
					{
						SKLogger.writeLog("selecting Emirates of residence: ",formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0));
						if(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0).equalsIgnoreCase("RESIDENCE"))
						{
							SKLogger.writeLog("selecting Emirates of residence: settign value: ",formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0));
							formObject.setNGValue("cmplx_Customer_EmirateOfResidence",formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 6));
						}
					}
				}
				//code to set Emirates of residence End.
				//code change to save the date in desired format.

				try{
					SKLogger.writeLog("converting date entered: ",formObject.getNGValue("cmplx_Customer_DOb")+" cmplx_Customer_PassPortExpiry: "+formObject.getNGValue("cmplx_Customer_PassPortExpiry")+" cmplx_Customer_EmirateIDExpiry: "+formObject.getNGValue("cmplx_Customer_EmirateIDExpiry")+" VIsaExpiry: "+ formObject.getNGValue("cmplx_Customer_VIsaExpiry")+" cmplx_Customer_IdIssueDate: "+ formObject.getNGValue("cmplx_Customer_IdIssueDate"));
					String str_dob=formObject.getNGValue("cmplx_Customer_DOb");
					String str_IDissuedate=formObject.getNGValue("cmplx_Customer_IdIssueDate");
					String str_passExpDate=formObject.getNGValue("cmplx_Customer_PassPortExpiry");
					String str_visaExpDate=formObject.getNGValue("cmplx_Customer_VIsaExpiry");
					String str_EIDExpDate=formObject.getNGValue("cmplx_Customer_EmirateIDExpiry");
					SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-mm-dd");
					SimpleDateFormat sdf2=new SimpleDateFormat("dd/mm/yyyy");
					if(str_dob!=null&&!str_dob.equalsIgnoreCase("")){
						String n_str_dob=sdf2.format(sdf1.parse(str_dob));
						SKLogger.writeLog("converting date entered",n_str_dob+"asd");
						formObject.setNGValue("cmplx_Customer_DOb",n_str_dob,false);
					}
					if(str_IDissuedate!=null&&!str_IDissuedate.equalsIgnoreCase("")){
						formObject.setNGValue("cmplx_Customer_IdIssueDate",sdf2.format(sdf1.parse(str_IDissuedate)),false);
					}
					if(str_passExpDate!=null&&!str_passExpDate.equalsIgnoreCase("")){
						formObject.setNGValue("cmplx_Customer_PassPortExpiry",sdf2.format(sdf1.parse(str_passExpDate)),false);
					}
					if(str_visaExpDate!=null&&!str_visaExpDate.equalsIgnoreCase("")){
						formObject.setNGValue("cmplx_Customer_VIsaExpiry",sdf2.format(sdf1.parse(str_visaExpDate)),false);
					}
					if(str_EIDExpDate!=null&&!str_EIDExpDate.equalsIgnoreCase("")){
						formObject.setNGValue("cmplx_Customer_EmirateIDExpiry",sdf2.format(sdf1.parse(str_EIDExpDate)),false);
					}
					formObject.setNGValue("cmplx_Customer_VIPFlag", "true");

				}
				catch(Exception e){
					SKLogger.writeLog("RLOS Initiation Set custumer data from fetch customer details ", "Exception Occured: "+e.getMessage());
					popupFlag = "Y";
					SKLogger.writeLog("RLOS", "inside Customer Eligibility to through Exception to Exit:");
					alert_msg = "Existing Customer Details fetched Sucessfully!";
					throw new ValidatorException(new FacesMessage(alert_msg));

				}
			}
			else{
				alert_msg="Error While Fetch Customer Details.";
				formObject.setNGValue("Is_Customer_Details","N");
			}
		}
		catch(Exception e){
			SKLogger.writeLog("RLOS Common", "Exception occured in fetch_cust_details_primary method"+e.getMessage());
			if(alert_msg.equalsIgnoreCase("")){
				alert_msg="Error occured while performing Fetch Customer details, Kinldy contact administrator or contact after sometime";
			}

		}
		return alert_msg;
	}

	// added for dectech call on 28july2017
	public String  getInternalLiabDetails(){
		SKLogger.writeLog("RLOSCommon java file", "inside getCustAddress_details : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "SELECT AcctType,CustRoleType,AcctId,AccountOpenDate,AcctStat,AcctSegment,AcctSubSegment,CreditGrade FROM ng_RLOS_CUSTEXPOSE_AcctDetails  with (nolock) where Wi_Name = '"+formObject.getWFWorkitemName()+"' and Request_Type = 'InternalExposure'";
		SKLogger.writeLog("getInternalLiabDetails sQuery"+sQuery, "");
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		SKLogger.writeLog("getInternalLiabDetails list size"+OutputXML.size(), "");

		for (int i = 0; i<OutputXML.size();i++){

			String accountType = "";
			String role = "";
			String accNumber = "";
			String acctOpenDate = ""; 
			String acctStatus = "";
			String acctSegment = "";
			String acctSubSegment = "";
			String acctCreditGrade = "";
			if(!(OutputXML.get(i).get(0) == null || OutputXML.get(i).get(0).equals("")) ){
				accountType = OutputXML.get(i).get(0).toString();
			}
			if(!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals("")) ){
				role = OutputXML.get(i).get(1).toString();
			}
			if(!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2).equals("")) ){
				accNumber = OutputXML.get(i).get(2).toString();
			}
			if(!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals("")) ){
				acctOpenDate = OutputXML.get(i).get(3).toString();
			}
			if(!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals("")) ){
				acctStatus = OutputXML.get(i).get(4).toString();
			}
			if(!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5).equals("")) ){
				acctSegment = OutputXML.get(i).get(5).toString();
			}
			if(!(OutputXML.get(i).get(6) == null || OutputXML.get(i).get(6).equals("")) ){
				acctSubSegment = OutputXML.get(i).get(6).toString();
			}
			if(!(OutputXML.get(i).get(7) == null || OutputXML.get(i).get(7).equals("")) ){
				acctCreditGrade = OutputXML.get(i).get(7).toString();
			}
			if(accNumber!=null && !accNumber.equalsIgnoreCase("") && !accNumber.equalsIgnoreCase("null")){
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
		SKLogger.writeLog("RLOSCommon", "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}


	public String InternalBureauData(){
		SKLogger.writeLog("RLOSCommon java file", "inside InternalBureauData : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "SELECT CifId,OutstandingAmt,OverdueAmt,CreditLimit,numofchequeret3,numofddsret3,numofchequeret6,numofddsret6,Request_Type FROM ng_RLOS_CUSTEXPOSE_CardDetails WHERE Wi_Name = '"+formObject.getWFWorkitemName()+"' AND Request_Type = 'InternalExposure'  union SELECT   cifid,TotalOutstandingAmt ,OverdueAmt,TotalLoanAmount, '' as Col5, '' as Col6, '' as Col7, '' as Col8, '' as Col9 FROM ng_RLOS_CUSTEXPOSE_LoanDetails   with (nolock) WHERE Wi_Name = '"+formObject.getWFWorkitemName()+"'";
		SKLogger.writeLog("getInternalBureauData sQuery"+sQuery, "");
		String  add_xml_str ="";
		try{
			List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
			SKLogger.writeLog("InternalBureauData list size"+OutputXML.size(), "");
			//SKLogger.writeLog("InternalBureauData list "+OutputXML, "");
			SKLogger.writeLog("obefor list ", "values");
			float TotOutstandingAmt;
			float TotOverdueAmt;
			float TotalExposure;
			SKLogger.writeLog("outsidefor list ", "values");
			TotOutstandingAmt=0.0f;
			TotOverdueAmt=0.0f;
			TotalExposure=0.0f;
			SKLogger.writeLog("outsidefor2 list ", "values");
			for(int i = 0; i<OutputXML.size();i++){
				SKLogger.writeLog("insidefor list "+i, "values"+OutputXML.get(i).get(1));
				if(OutputXML.get(i).get(1)!=null && !OutputXML.get(i).get(1).isEmpty() &&  !OutputXML.get(i).get(1).equals("") && !OutputXML.get(i).get(1).equalsIgnoreCase("null") ){
					SKLogger.writeLog("Totaloutstanding"+i, "values."+TotOutstandingAmt+"..");
					TotOutstandingAmt = TotOutstandingAmt + Float.parseFloat(OutputXML.get(i).get(1));
				}
				if(OutputXML.get(i).get(2)!=null && !OutputXML.get(i).get(1).isEmpty() && !OutputXML.get(i).get(2).equals("") && !OutputXML.get(i).get(2).equalsIgnoreCase("null") ){
					SKLogger.writeLog("TotOverdueAmt"+i, "values."+TotOutstandingAmt+"..");
					TotOverdueAmt = TotOverdueAmt + Float.parseFloat(OutputXML.get(i).get(2));
				}
				if(OutputXML.get(i).get(3)!=null && !OutputXML.get(i).get(1).isEmpty() &&  !OutputXML.get(i).get(3).equals("") && !OutputXML.get(i).get(3).equalsIgnoreCase("null")) {
					SKLogger.writeLog("TotalExposure"+i, "values."+TotOutstandingAmt+"..");
					TotalExposure = TotalExposure + Float.parseFloat(OutputXML.get(i).get(3));
				}
			}
			SKLogger.writeLog("InternalBureauData after calculation"+TotOutstandingAmt+"....."+TotOverdueAmt, ",,,"+TotalExposure);
			for (int i = 0; i<OutputXML.size();i++){
				if(OutputXML.get(i).get(8).equalsIgnoreCase("InternalExposure")){
					String cifId = "";
					String chqRet3 = "";
					String ddsRet3 = "";
					String chqRet6 = ""; 
					String ddsRet6 = "";

					SKLogger.writeLog("getInternalBureauData loopcount"+i, "");
					if(!(OutputXML.get(i).get(0)==null || OutputXML.get(i).get(0).equals("")) ){
						cifId = OutputXML.get(i).get(0).toString();

					}
					if(!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals("")) ){

						chqRet3 = OutputXML.get(i).get(4).toString();

					}
					if(!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5).equals("")) ){

						ddsRet3 = OutputXML.get(i).get(5).toString();

					}
					if(!(OutputXML.get(i).get(6) == null || OutputXML.get(i).get(6).equals("")) ){

						chqRet6 = OutputXML.get(i).get(6).toString();

					}
					if(!(OutputXML.get(i).get(7) == null || OutputXML.get(i).get(7).equals("")) ){

						ddsRet6 = OutputXML.get(i).get(7).toString();

					}

					if(cifId!=null && !cifId.equalsIgnoreCase("") && !cifId.equalsIgnoreCase("null")){
						add_xml_str = add_xml_str + "<InternalBureau><applicant_id>"+cifId+"</applicant_id>";
						add_xml_str = add_xml_str + "<full_name>"+formObject.getNGValue("cmplx_Customer_FIrstNAme")+"</full_name>";// fullname fieldname to be confirmed from onsite

						add_xml_str = add_xml_str + "<total_out_bal>"+TotOutstandingAmt+"</total_out_bal>";
						add_xml_str = add_xml_str + "<total_overdue>"+TotOverdueAmt+"</total_overdue>";
						add_xml_str = add_xml_str + "<no_default_contract>"+OutputXML.size()+"</no_default_contract>";

						add_xml_str = add_xml_str + "<total_exposure>"+TotalExposure+"</total_exposure>";
						add_xml_str = add_xml_str + "<worst_curr_pay>"+""+"</worst_curr_pay>"; // to be populated later
						add_xml_str = add_xml_str + "<worst_curr_pay_24>"+""+"</worst_curr_pay_24>"; // to be populated later
						add_xml_str = add_xml_str + "<no_of_rec>"+""+"</no_of_rec>"; // to be populated later
						add_xml_str = add_xml_str + "<cheque_return_3mon>"+chqRet3+"</cheque_return_3mon>";
						add_xml_str = add_xml_str + "<dds_return_3mon>"+ddsRet3+"</dds_return_3mon>";
						add_xml_str = add_xml_str + "<cheque_return_6mon>"+chqRet6+"</cheque_return_6mon>";
						add_xml_str = add_xml_str + "<dds_return_6mon>"+ddsRet6+"</dds_return_6mon>";
						add_xml_str = add_xml_str + "<internal_charge_off>"+""+"</internal_charge_off><company_flag>N</company_flag></InternalBureau>";// to be populated later
					}

				}
			}
			SKLogger.writeLog("RLOSCommon", "Internal liab tag Cration: "+ add_xml_str);
			return add_xml_str;
		}
		catch(Exception e)
		{
			SKLogger.writeLog("RLOSCommon", "Exception occurred in InternalBureauData()"+e.getMessage()+printException(e));
			return "";
		}
	}

	public String InternalBouncedCheques(){
		SKLogger.writeLog("RLOSCommon java file", "inside InternalBouncedCheques : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "SELECT cifid,number,amount,reasoncode,returndate,providerno FROM ng_RLOS_CUSTEXPOSE_ChequeDetails with (nolock) where Wi_Name = '"+formObject.getWFWorkitemName()+"' and Request_Type = 'InternalExposure'";
		SKLogger.writeLog("InternalBouncedCheques sQuery"+sQuery, "");
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		SKLogger.writeLog("InternalBouncedCheques list size"+OutputXML.size(), "");

		for (int i = 0; i<OutputXML.size();i++){

			String applicantID = "";
			String chequeNo = "";
			String amount = "";
			String reason = ""; 
			String returnDate = "";
			String providerNo = "";

			if(!(OutputXML.get(i).get(0) == null || OutputXML.get(i).get(0).equals("")) ){
				applicantID = OutputXML.get(i).get(0).toString();
			}
			if(!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals("")) ){
				chequeNo = OutputXML.get(i).get(1).toString();
			}
			if(!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2).equals("")) ){
				amount = OutputXML.get(i).get(2).toString();
			}
			if(!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals("")) ){
				reason = OutputXML.get(i).get(3).toString();
			}
			if(!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals("")) ){
				returnDate = OutputXML.get(i).get(4).toString();
			}
			if(!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5).equals("")) ){
				providerNo = OutputXML.get(i).get(5).toString();
			}

			if(applicantID!=null && !applicantID.equalsIgnoreCase("") && !applicantID.equalsIgnoreCase("null")){
				add_xml_str = add_xml_str + "<InternalBouncedCheques><applicant_id>"+applicantID+"</applicant_id>";
				add_xml_str = add_xml_str + "<internal_bounced_cheques_id>"+"123"+"</internal_bounced_cheques_id>";// to be populated later
				add_xml_str = add_xml_str + "<bounced_cheque>"+""+"</bounced_cheque>";// to be populated later
				add_xml_str = add_xml_str + "<cheque_no>"+chequeNo+"</cheque_no>";
				add_xml_str = add_xml_str + "<amount>"+amount+"</amount>";
				add_xml_str = add_xml_str + "<reason>"+reason+"</reason>";
				add_xml_str = add_xml_str + "<return_date>"+returnDate+"</return_date>"; 
				add_xml_str = add_xml_str + "<provider_no>"+providerNo+"</provider_no>"; 
				add_xml_str = add_xml_str + "<bounced_cheque_dds>"+""+"</bounced_cheque_dds><company_flag>N</company_flag></InternalBouncedCheques>"; // to be populated later
			}

		}
		SKLogger.writeLog("RLOSCommon", "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}

	public String InternalBureauIndividualProducts(){
		SKLogger.writeLog("RLOSCommon java file", "inside InternalBureauIndividualProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "SELECT cifid,agreementid,product_type,loantype,custroletype,loan_start_date,loanmaturitydate,lastupdatedate ,outstandingamt,totalloanamount,paymentsamt,paymentmode,totalnoofinstalments,remaininginstalments,totalloanamount,	overdueamt,nofdayspmtdelay,monthsonbook,currentlycurrent,currmaxutil,DPD_30_in_last_6_months,DPD_60_in_last_18_months,propertyvalue,loan_disbursal_date,marketingcode,DPD_30_in_last_3_months,DPD_30_in_last_6_months,DPD_30_in_last_9_months,DPD_30_in_last_12_months,DPD_30_in_last_18_months,DPD_30_in_last_24_months,DPD_60_in_last_3_months,DPD_60_in_last_6_months,DPD_60_in_last_9_months,DPD_60_in_last_12_months,DPD_60_in_last_18_months,DPD_60_in_last_24_months,DPD_90_in_last_3_months,DPD_90_in_last_6_months,DPD_90_in_last_9_months,DPD_90_in_last_12_months,DPD_90_in_last_18_months,DPD_90_in_last_24_months,DPD_120_in_last_3_months,DPD_120_in_last_6_months,DPD_120_in_last_9_months,DPD_120_in_last_12_months,DPD_120_in_last_18_months,DPD_120_in_last_24_months,DPD_150_in_last_3_months,DPD_150_in_last_6_months,DPD_150_in_last_9_months,DPD_150_in_last_12_months,DPD_150_in_last_18_months,DPD_150_in_last_24_months,DPD_180_in_last_3_months,DPD_180_in_last_6_months,DPD_180_in_last_9_months,DPD_180_in_last_12_months,DPD_180_in_last_24_months,'' as col1 FROM ng_RLOS_CUSTEXPOSE_LoanDetails WHERE Wi_Name = '"+formObject.getWFWorkitemName()+"' union select CifId,CardEmbossNum,CardType,'' as col4,CustRoleType,'' as col6,'' as col7,'' as col8,OutstandingAmt,CreditLimit,'' as col11,PaymentMode,'' as col13,'' as col14,CashLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,'' as col19,CurrMaxUtil,DPD_30_in_last_6_months,DPD_60_in_last_18_months,'' as col23,'' as col24,'' as col25,DPD_30_in_last_3_months,DPD_30_in_last_6_months,DPD_30_in_last_9_months,DPD_30_in_last_12_months,DPD_30_in_last_18_months,DPD_30_in_last_24_months,DPD_60_in_last_3_months,DPD_60_in_last_6_months,DPD_60_in_last_9_months,DPD_60_in_last_12_months,DPD_60_in_last_18_months,DPD_60_in_last_24_months,DPD_90_in_last_3_months,DPD_90_in_last_6_months,DPD_90_in_last_9_months,DPD_90_in_last_12_months,DPD_90_in_last_18_months,DPD_90_in_last_24_months,DPD_120_in_last_3_months,DPD_120_in_last_6_months,DPD_120_in_last_9_months,DPD_120_in_last_12_months,DPD_120_in_last_18_months,DPD_120_in_last_24_months,DPD_150_in_last_3_months,DPD_150_in_last_6_months,DPD_150_in_last_9_months,DPD_150_in_last_12_months,DPD_150_in_last_18_months,DPD_150_in_last_24_months,DPD_180_in_last_3_months,DPD_180_in_last_6_months,DPD_180_in_last_9_months,DPD_180_in_last_12_months,DPD_180_in_last_24_months,ExpiryDate FROM ng_RLOS_CUSTEXPOSE_CardDetails where 	Wi_Name = '"+formObject.getWFWorkitemName()+"' and Request_Type = 'InternalExposure'";
		SKLogger.writeLog("InternalBureauIndividualProducts sQuery"+sQuery, "");
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		SKLogger.writeLog("InternalBureauIndividualProducts list size"+OutputXML.size(), "");
		SKLogger.writeLog("InternalBureauIndividualProducts list "+OutputXML, "");

		for (int i = 0; i<OutputXML.size();i++){

			String cifId = "";
			String agreementId = "";
			String product_type = "";
			String contractType = ""; 
			String custroletype = "";
			String loan_start_date = "";
			String loanmaturitydate = "";
			String lastupdatedate = "";

			String outstandingamt = "";
			String totalloanamount = "";
			String paymentsamt = "";
			String paymentmode = ""; 
			String totalnoofinstalments = "";
			String remaininginstalments = "";
			String CashLimit = "";
			String overdueamt = "";

			String nofdayspmtdelay = "";
			String monthsonbook = "";
			String currentlycurrent = "";
			String currmaxutil = ""; 
			String DPD_30_in_last_6_months = "";
			String DPD_60_in_last_18_months = "";
			String propertyvalue = "";

			String loan_disbursal_date = "";
			String marketingcode = "";
			String DPD_30_in_last_3_months = "";
			String DPD_30_in_last_9_months = ""; 
			String DPD_30_in_last_12_months = "";
			String DPD_30_in_last_18_months = "";
			String DPD_30_in_last_24_months = "";

			String DPD_60_in_last_3_months = "";
			String DPD_60_in_last_6_months = ""; 
			String DPD_60_in_last_9_months = "";
			String DPD_60_in_last_12_months = "";
			String DPD_60_in_last_24_months = "";
			String DPD_90_in_last_3_months = "";
			String DPD_90_in_last_6_months = ""; 
			String DPD_90_in_last_9_months = "";
			String DPD_90_in_last_12_months = "";
			String DPD_90_in_last_18_months = "";
			String DPD_90_in_last_24_months = "";
			String DPD_120_in_last_3_months = ""; 
			String DPD_120_in_last_6_months = "";
			String DPD_120_in_last_9_months = "";
			String DPD_120_in_last_12_months = "";
			String DPD_120_in_last_18_months = "";
			String DPD_120_in_last_24_months = ""; 
			String DPD_150_in_last_3_months = "";
			String DPD_150_in_last_6_months = "";
			String DPD_150_in_last_9_months = "";
			String DPD_150_in_last_12_months = ""; 
			String DPD_150_in_last_18_months = "";
			String DPD_150_in_last_24_months = "";
			String DPD_180_in_last_3_months = "";
			String DPD_180_in_last_6_months = "";
			String DPD_180_in_last_9_months = ""; 
			String DPD_180_in_last_12_months = "";
			String DPD_180_in_last_24_months = "";
			String CardExpiryDate = "";

			if(!(OutputXML.get(i).get(0) == null || OutputXML.get(i).get(0).equals("")) ){
				cifId = OutputXML.get(i).get(0).toString();
			}
			if(!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals("")) ){
				agreementId = OutputXML.get(i).get(1).toString();
			}				
			if(!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2).equals("")) ){
				product_type = OutputXML.get(i).get(2).toString();
			}
			if(!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals("")) ){
				contractType = OutputXML.get(i).get(3).toString();
			}
			if(!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals("")) ){
				custroletype = OutputXML.get(i).get(4).toString();
			}				
			if(!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5).equals("")) ){
				loan_start_date = OutputXML.get(i).get(5).toString();
			}
			if(!(OutputXML.get(i).get(6) == null || OutputXML.get(i).get(6).equals("")) ){
				loanmaturitydate = OutputXML.get(i).get(6).toString();
			}
			if(!(OutputXML.get(i).get(7) == null || OutputXML.get(i).get(7).equals("")) ){
				lastupdatedate = OutputXML.get(i).get(7).toString();
			}				
			if(!(OutputXML.get(i).get(8) == null || OutputXML.get(i).get(8).equals("")) ){
				outstandingamt = OutputXML.get(i).get(8).toString();
			}
			if(!(OutputXML.get(i).get(9) == null || OutputXML.get(i).get(9).equals("")) ){
				totalloanamount = OutputXML.get(i).get(9).toString();
			}
			if(!(OutputXML.get(i).get(10) == null || OutputXML.get(i).get(10).equals("")) ){
				paymentsamt = OutputXML.get(i).get(10).toString();
			}				
			if(!(OutputXML.get(i).get(11) == null || OutputXML.get(i).get(11).equals("")) ){
				paymentmode = OutputXML.get(i).get(11).toString();
			}
			if(!(OutputXML.get(i).get(12) == null) || OutputXML.get(i).get(12).equals("")) {
				totalnoofinstalments = OutputXML.get(i).get(12).toString();
			}
			if(!(OutputXML.get(i).get(13) == null || OutputXML.get(i).get(13).equals("")) ){
				remaininginstalments = OutputXML.get(i).get(13).toString();
			}				
			if(!(OutputXML.get(i).get(14) == null || OutputXML.get(i).get(14).equals("")) ){
				CashLimit = OutputXML.get(i).get(14).toString();
			}
			if(!(OutputXML.get(i).get(15) == null || OutputXML.get(i).get(15).equals("")) ){
				overdueamt = OutputXML.get(i).get(15).toString();
			}
			if(!(OutputXML.get(i).get(16) == null || OutputXML.get(i).get(16).equals("")) ){
				nofdayspmtdelay = OutputXML.get(i).get(16).toString();
			}				
			if(!(OutputXML.get(i).get(17) == null || OutputXML.get(i).get(17).equals("")) ){
				monthsonbook = OutputXML.get(i).get(17).toString();
			}
			if(!(OutputXML.get(i).get(18) == null || OutputXML.get(i).get(18).equals("")) ){
				currentlycurrent = OutputXML.get(i).get(18).toString();
			}
			if(!(OutputXML.get(i).get(19) == null || OutputXML.get(i).get(19).equals("")) ){
				currmaxutil = OutputXML.get(i).get(19).toString();
			}				
			if(!(OutputXML.get(i).get(20) == null || OutputXML.get(i).get(20).equals("")) ){
				DPD_30_in_last_6_months = OutputXML.get(i).get(20).toString();
			}
			if(!(OutputXML.get(i).get(21) == null || OutputXML.get(i).get(21).equals("")) ){
				DPD_60_in_last_18_months = OutputXML.get(i).get(21).toString();
			}
			if(!(OutputXML.get(i).get(22) == null || OutputXML.get(i).get(22).equals("")) ){
				propertyvalue = OutputXML.get(i).get(22).toString();
			}				
			if(!(OutputXML.get(i).get(23) == null || OutputXML.get(i).get(23).equals("")) ){
				loan_disbursal_date = OutputXML.get(i).get(23).toString();
			}
			if(!(OutputXML.get(i).get(24) == null || OutputXML.get(i).get(24).equals("")) ){
				marketingcode = OutputXML.get(i).get(24).toString();
			}
			if(!(OutputXML.get(i).get(25) == null || OutputXML.get(i).get(25).equals("")) ){
				DPD_30_in_last_3_months = OutputXML.get(i).get(25).toString();
			}				
			if(!(OutputXML.get(i).get(26) == null || OutputXML.get(i).get(26).equals("")) ){
				DPD_30_in_last_9_months	 = OutputXML.get(i).get(26).toString();
			}
			if(!(OutputXML.get(i).get(27) == null || OutputXML.get(i).get(27).equals("")) ){
				DPD_30_in_last_12_months = OutputXML.get(i).get(27).toString();
			}
			if(!(OutputXML.get(i).get(28) == null || OutputXML.get(i).get(28).equals("")) ){
				DPD_30_in_last_18_months = OutputXML.get(i).get(28).toString();
			}				
			if(!(OutputXML.get(i).get(29) == null || OutputXML.get(i).get(29).equals("")) ){
				DPD_30_in_last_24_months = OutputXML.get(i).get(29).toString();
			}
			if(!(OutputXML.get(i).get(30) == null || OutputXML.get(i).get(30).equals("")) ){
				DPD_60_in_last_3_months = OutputXML.get(i).get(30).toString();
			}
			if(!(OutputXML.get(i).get(31) == null || OutputXML.get(i).get(31).equals("")) ){
				DPD_60_in_last_6_months = OutputXML.get(i).get(31).toString();
			}				
			if(!(OutputXML.get(i).get(32) == null || OutputXML.get(i).get(32).equals("")) ){
				DPD_60_in_last_9_months = OutputXML.get(i).get(32).toString();
			}
			if(!(OutputXML.get(i).get(33) == null || OutputXML.get(i).get(33).equals("")) ){
				DPD_60_in_last_12_months = OutputXML.get(i).get(33).toString();
			}
			if(!(OutputXML.get(i).get(34) == null || OutputXML.get(i).get(34).equals("")) ){
				DPD_60_in_last_24_months = OutputXML.get(i).get(34).toString();
			}				
			if(!(OutputXML.get(i).get(35) == null || OutputXML.get(i).get(35).equals("")) ){
				DPD_90_in_last_3_months = OutputXML.get(i).get(35).toString();
			}
			if(!(OutputXML.get(i).get(36) == null || OutputXML.get(i).get(36).equals("")) ){
				DPD_90_in_last_6_months = OutputXML.get(i).get(36).toString();
			}
			if(!(OutputXML.get(i).get(37) == null || OutputXML.get(i).get(37).equals("")) ){
				DPD_90_in_last_9_months = OutputXML.get(i).get(37).toString();
			}				
			if(!(OutputXML.get(i).get(38) == null || OutputXML.get(i).get(38).equals("")) ){
				DPD_90_in_last_12_months = OutputXML.get(i).get(38).toString();
			}
			if(!(OutputXML.get(i).get(39) == null || OutputXML.get(i).get(39).equals("")) ){
				DPD_90_in_last_18_months = OutputXML.get(i).get(39).toString();
			}
			if(!(OutputXML.get(i).get(40) == null || OutputXML.get(i).get(40).equals("")) ){
				DPD_90_in_last_24_months = OutputXML.get(i).get(40).toString();
			}				
			if(!(OutputXML.get(i).get(41) == null || OutputXML.get(i).get(41).equals("")) ){
				DPD_120_in_last_3_months = OutputXML.get(i).get(41).toString();
			}
			if(!(OutputXML.get(i).get(42) == null || OutputXML.get(i).get(42).equals("")) ){
				DPD_120_in_last_6_months = OutputXML.get(i).get(42).toString();
			}
			if(!(OutputXML.get(i).get(43) == null || OutputXML.get(i).get(43).equals("")) ){
				DPD_120_in_last_9_months = OutputXML.get(i).get(43).toString();
			}				
			if(!(OutputXML.get(i).get(44) == null || OutputXML.get(i).get(44).equals("")) ){
				DPD_120_in_last_12_months = OutputXML.get(i).get(44).toString();
			}
			if(!(OutputXML.get(i).get(45) == null || OutputXML.get(i).get(45).equals("")) ){
				DPD_120_in_last_18_months = OutputXML.get(i).get(45).toString();
			}
			if(!(OutputXML.get(i).get(46) == null || OutputXML.get(i).get(46).equals("")) ){
				DPD_120_in_last_24_months = OutputXML.get(i).get(46).toString();
			}				
			if(!(OutputXML.get(i).get(47) == null || OutputXML.get(i).get(47).equals("")) ){
				DPD_150_in_last_3_months = OutputXML.get(i).get(47).toString();
			}
			if(!(OutputXML.get(i).get(48) == null || OutputXML.get(i).get(48).equals("")) ){
				DPD_150_in_last_6_months = OutputXML.get(i).get(48).toString();
			}
			if(!(OutputXML.get(i).get(49) == null || OutputXML.get(i).get(49).equals("")) ){
				DPD_150_in_last_9_months = OutputXML.get(i).get(49).toString();
			}				
			if(!(OutputXML.get(i).get(50) == null || OutputXML.get(i).get(50).equals("")) ){
				DPD_150_in_last_12_months = OutputXML.get(i).get(50).toString();
			}
			if(!(OutputXML.get(i).get(51) == null || OutputXML.get(i).get(51).equals("")) ){
				DPD_150_in_last_18_months = OutputXML.get(i).get(51).toString();
			}
			if(!(OutputXML.get(i).get(52) == null || OutputXML.get(i).get(52).equals("")) ){
				DPD_150_in_last_24_months = OutputXML.get(i).get(52).toString();
			}				
			if(!(OutputXML.get(i).get(53) == null || OutputXML.get(i).get(53).equals("")) ){
				DPD_180_in_last_3_months = OutputXML.get(i).get(53).toString();
			}
			if(!(OutputXML.get(i).get(54) == null || OutputXML.get(i).get(54).equals("")) ){
				DPD_180_in_last_6_months = OutputXML.get(i).get(54).toString();
			}
			if(!(OutputXML.get(i).get(55) == null || OutputXML.get(i).get(55).equals("")) ){
				DPD_180_in_last_9_months = OutputXML.get(i).get(55).toString();
			}				
			if(!(OutputXML.get(i).get(56) == null || OutputXML.get(i).get(56).equals("")) ){
				DPD_180_in_last_12_months = OutputXML.get(i).get(56).toString();
			}
			if(!(OutputXML.get(i).get(57) == null || OutputXML.get(i).get(57).equals("")) ){
				DPD_180_in_last_24_months = OutputXML.get(i).get(57).toString();
			}
			if(!(OutputXML.get(i).get(58) == null || OutputXML.get(i).get(58).equals("")) ){
				CardExpiryDate = OutputXML.get(i).get(58).toString();
			}				



			if(cifId!=null && !cifId.equalsIgnoreCase("") && !cifId.equalsIgnoreCase("null")){
				add_xml_str = add_xml_str + "<InternalBureauIndividualProducts><applicant_id>"+cifId+"</applicant_id>";
				add_xml_str = add_xml_str + "<internal_bureau_individual_products_id>"+agreementId+"</internal_bureau_individual_products_id>";
				add_xml_str = add_xml_str + "<type_product>"+product_type+"</type_product>";
				add_xml_str = add_xml_str + "<contract_type>"+contractType+"</contract_type>";
				add_xml_str = add_xml_str + "<provider_no>"+""+"</provider_no>";
				add_xml_str = add_xml_str + "<phase>"+""+"</phase>";
				add_xml_str = add_xml_str + "<role_of_customer>"+custroletype+"</role_of_customer>"; 

				add_xml_str = add_xml_str + "<start_date>"+loan_start_date+"</start_date>"; 
				add_xml_str = add_xml_str + "<close_date>"+loanmaturitydate+"</close_date>"; 
				add_xml_str = add_xml_str + "<date_last_updated>"+lastupdatedate+"</date_last_updated>"; 
				add_xml_str = add_xml_str + "<outstanding_balance>"+outstandingamt+"</outstanding_balance>"; 
				add_xml_str = add_xml_str + "<total_amount>"+totalloanamount+"</total_amount>"; 
				add_xml_str = add_xml_str + "<payments_amount>"+paymentsamt+"</payments_amount>"; 
				add_xml_str = add_xml_str + "<method_of_payment>"+paymentmode+"</method_of_payment>"; 
				add_xml_str = add_xml_str + "<total_no_of_instalments>"+totalnoofinstalments+"</total_no_of_instalments>"; 
				add_xml_str = add_xml_str + "<no_of_remaining_instalments>"+remaininginstalments+"</no_of_remaining_instalments>"; 
				add_xml_str = add_xml_str + "<worst_status>"+""+"</worst_status>"; 
				add_xml_str = add_xml_str + "<worst_status_date>"+""+"</worst_status_date>"; 
				add_xml_str = add_xml_str + "<credit_limit>"+CashLimit+"</credit_limit>"; 
				add_xml_str = add_xml_str + "<overdue_amount>"+overdueamt+"</overdue_amount>"; 
				add_xml_str = add_xml_str + "<no_of_days_payment_delay>"+nofdayspmtdelay+"</no_of_days_payment_delay>"; 
				add_xml_str = add_xml_str + "<mob>"+monthsonbook+"</mob>"; 
				add_xml_str = add_xml_str + "<type_of_od>"+""+"</type_of_od>"; 
				add_xml_str = add_xml_str + "<currently_current>"+currentlycurrent+"</currently_current>"; 
				add_xml_str = add_xml_str + "<current_utilization>"+currmaxutil+"</current_utilization>"; 
				add_xml_str = add_xml_str + "<dpd_30_last_6_mon>"+DPD_30_in_last_6_months+"</dpd_30_last_6_mon>"; 
				add_xml_str = add_xml_str + "<dpd_60p_in_last_18_mon>"+DPD_60_in_last_18_months+"</dpd_60p_in_last_18_mon>"; 

				add_xml_str = add_xml_str + "<cards_b_score>"+""+"</cards_b_score>"; 
				add_xml_str = add_xml_str + "<card_product>"+""+"</card_product>"; 
				add_xml_str = add_xml_str + "<property_value>"+propertyvalue+"</property_value>"; 
				add_xml_str = add_xml_str + "<disbursal_date>"+loan_disbursal_date+"</disbursal_date>"; 
				add_xml_str = add_xml_str + "<marketing_code>"+marketingcode+"</marketing_code>"; 
				add_xml_str = add_xml_str + "<card_expiry_date>"+CardExpiryDate+"</card_expiry_date>"; 
				add_xml_str = add_xml_str + "<card_upgrade_indicator>"+""+"</card_upgrade_indicator>"; 
				add_xml_str = add_xml_str + "<part_settlement_date>"+""+"</part_settlement_date>"; 
				add_xml_str = add_xml_str + "<no_of_remaining_instalments>"+remaininginstalments+"</no_of_remaining_instalments>"; 
				add_xml_str = add_xml_str + "<part_settlement_amount>"+""+"</part_settlement_amount>"; 
				add_xml_str = add_xml_str + "<part_settlement_reason>"+""+"</part_settlement_reason>"; 
				add_xml_str = add_xml_str + "<limit_expiry_date>"+""+"</limit_expiry_date>"; 
				add_xml_str = add_xml_str + "<no_of_primary_cards>"+""+"</no_of_primary_cards>"; 
				add_xml_str = add_xml_str + "<no_of_repayments_done>"+""+"</no_of_repayments_done>"; 
				add_xml_str = add_xml_str + "<card_segment>"+""+"</card_segment>"; 
				add_xml_str = add_xml_str + "<product_type>"+"Cards"+"</product_type>"; 
				add_xml_str = add_xml_str + "<product_category>"+""+"</product_category>"; 
				add_xml_str = add_xml_str + "<combined_limit_flag>"+""+"</combined_limit_flag>"; 
				add_xml_str = add_xml_str + "<secured_card_flag>"+""+"</secured_card_flag>"; 
				add_xml_str = add_xml_str + "<resch_tko_flag>"+""+"</resch_tko_flag>"; 

				add_xml_str = add_xml_str + "<general_status>"+""+"</general_status>"; 
				add_xml_str = add_xml_str + "<consider_for_obligation>"+""+"</consider_for_obligation>"; 
				add_xml_str = add_xml_str + "<limit_increase>"+""+"</limit_increase>"; 
				add_xml_str = add_xml_str + "<card_against_card>"+""+"</card_against_card>"; 
				add_xml_str = add_xml_str + "<quick_cash_amount>"+""+"</quick_cash_amount>"; 
				add_xml_str = add_xml_str + "<quick_cash_emi>"+""+"</quick_cash_emi>"; 
				add_xml_str = add_xml_str + "<take_over_indicator>"+""+"</take_over_indicator>"; 
				add_xml_str = add_xml_str + "<role>"+""+"</role>"; 
				add_xml_str = add_xml_str + "<limit>"+""+"</limit>"; 
				add_xml_str = add_xml_str + "<status>"+""+"</status>"; 
				add_xml_str = add_xml_str + "<part_settlement_reason>"+""+"</part_settlement_reason>"; 
				add_xml_str = add_xml_str + "<emi>"+""+"</emi>"; 
				add_xml_str = add_xml_str + "<os_amt>"+""+"</os_amt>"; 
				add_xml_str = add_xml_str + "<dpd_5_in_last_12mon>"+""+"</dpd_5_in_last_12mon>"; 
				add_xml_str = add_xml_str + "<dpd_5_in_last_18mon>"+""+"</dpd_5_in_last_18mon>"; 
				add_xml_str = add_xml_str + "<dpd_5_in_last_24mon>"+""+"</dpd_5_in_last_24mon>"; 
				add_xml_str = add_xml_str + "<dpd_30_in_last_3mon>"+DPD_30_in_last_3_months+"</dpd_30_in_last_3mon>"; 
				add_xml_str = add_xml_str + "<dpd_30_in_last_6mon>"+DPD_30_in_last_6_months+"</dpd_30_in_last_6mon>"; 
				add_xml_str = add_xml_str + "<dpd_30_in_last_9mon>"+DPD_30_in_last_9_months+"</dpd_30_in_last_9mon>"; 
				add_xml_str = add_xml_str + "<dpd_30_in_last_12mon>"+DPD_30_in_last_12_months+"</dpd_30_in_last_12mon>";

				add_xml_str = add_xml_str + "<dpd_30_in_last_18mon>"+DPD_30_in_last_18_months+"</dpd_30_in_last_18mon>"; 
				add_xml_str = add_xml_str + "<dpd_30_in_last_24mon>"+DPD_30_in_last_24_months+"</dpd_30_in_last_24mon>"; 
				add_xml_str = add_xml_str + "<dpd_60_in_last_3mon>"+DPD_60_in_last_3_months+"</dpd_60_in_last_3mon>"; 
				add_xml_str = add_xml_str + "<dpd_60_in_last_6mon>"+DPD_60_in_last_6_months+"</dpd_60_in_last_6mon>"; 
				add_xml_str = add_xml_str + "<dpd_60_in_last_9mon>"+DPD_60_in_last_9_months+"</dpd_60_in_last_9mon>"; 
				add_xml_str = add_xml_str + "<dpd_60_in_last_12mon>"+DPD_60_in_last_12_months+"</dpd_60_in_last_12mon>"; 
				add_xml_str = add_xml_str + "<dpd_60_in_last_18mon>"+DPD_60_in_last_18_months+"</dpd_60_in_last_18mon>"; 
				add_xml_str = add_xml_str + "<dpd_60_in_last_24mon>"+DPD_60_in_last_24_months+"</dpd_60_in_last_24mon>"; 
				add_xml_str = add_xml_str + "<dpd_90_in_last_3mon>"+DPD_90_in_last_3_months+"</dpd_90_in_last_3mon>"; 
				add_xml_str = add_xml_str + "<dpd_90_in_last_6mon>"+DPD_90_in_last_6_months+"</dpd_90_in_last_6mon>"; 
				add_xml_str = add_xml_str + "<dpd_90_in_last_9mon>"+DPD_90_in_last_9_months+"</dpd_90_in_last_9mon>"; 
				add_xml_str = add_xml_str + "<dpd_90_in_last_12mon>"+DPD_90_in_last_12_months+"</dpd_90_in_last_12mon>"; 
				add_xml_str = add_xml_str + "<dpd_90_in_last_18mon>"+DPD_90_in_last_18_months+"</dpd_90_in_last_18mon>"; 
				add_xml_str = add_xml_str + "<dpd_90_in_last_24mon>"+DPD_90_in_last_24_months+"</dpd_90_in_last_24mon>"; 
				add_xml_str = add_xml_str + "<dpd_120_in_last_3mon>"+DPD_120_in_last_3_months+"</dpd_120_in_last_3mon>"; 
				add_xml_str = add_xml_str + "<dpd_120_in_last_6mon>"+DPD_120_in_last_6_months+"</dpd_120_in_last_6mon>"; 
				add_xml_str = add_xml_str + "<dpd_120_in_last_9mon>"+DPD_120_in_last_9_months+"</dpd_120_in_last_9mon>"; 
				add_xml_str = add_xml_str + "<dpd_120_in_last_12mon>"+DPD_120_in_last_12_months+"</dpd_120_in_last_12mon>"; 
				add_xml_str = add_xml_str + "<dpd_120_in_last_18mon>"+DPD_120_in_last_18_months+"</dpd_120_in_last_18mon>"; 
				add_xml_str = add_xml_str + "<dpd_120_in_last_24mon>"+DPD_120_in_last_24_months+"</dpd_120_in_last_24mon>";

				add_xml_str = add_xml_str + "<dpd_150_in_last_3mon>"+DPD_150_in_last_3_months+"</dpd_150_in_last_3mon>"; 
				add_xml_str = add_xml_str + "<dpd_150_in_last_6mon>"+DPD_150_in_last_6_months+"</dpd_150_in_last_6mon>"; 
				add_xml_str = add_xml_str + "<dpd_150_in_last_9mon>"+DPD_150_in_last_9_months+"</dpd_150_in_last_9mon>"; 
				add_xml_str = add_xml_str + "<dpd_150_in_last_12mon>"+DPD_150_in_last_12_months+"</dpd_150_in_last_12mon>"; 
				add_xml_str = add_xml_str + "<dpd_150_in_last_18mon>"+DPD_150_in_last_18_months+"</dpd_150_in_last_18mon>"; 
				add_xml_str = add_xml_str + "<dpd_150_in_last_24mon>"+DPD_150_in_last_24_months+"</dpd_150_in_last_24mon>"; 
				add_xml_str = add_xml_str + "<dpd_180_in_last_3mon>"+DPD_180_in_last_3_months+"</dpd_180_in_last_3mon>"; 
				add_xml_str = add_xml_str + "<dpd_180_in_last_6mon>"+DPD_180_in_last_6_months+"</dpd_180_in_last_6mon>"; 
				add_xml_str = add_xml_str + "<dpd_180_in_last_9mon>"+DPD_180_in_last_9_months+"</dpd_180_in_last_9mon>"; 
				add_xml_str = add_xml_str + "<dpd_180_in_last_12mon>"+DPD_180_in_last_12_months+"</dpd_180_in_last_12mon>"; 
				add_xml_str = add_xml_str + "<dpd_180_in_last_18mon>"+""+"</dpd_180_in_last_18mon>"; 
				add_xml_str = add_xml_str + "<dpd_180_in_last_24mon>"+DPD_180_in_last_24_months+"</dpd_180_in_last_24mon>"; 
				add_xml_str = add_xml_str + "<last_temp_limit_exp>"+""+"</last_temp_limit_exp>"; 
				add_xml_str = add_xml_str + "<last_per_limit_exp>"+""+"</last_per_limit_exp>"; 
				add_xml_str = add_xml_str + "<security_cheque_amt>"+""+"</security_cheque_amt>"; 
				add_xml_str = add_xml_str + "<mol_salary_variance>"+""+"</mol_salary_variance>"; 
				add_xml_str = add_xml_str + "<kompass>"+""+"</kompass>"; 
				add_xml_str = add_xml_str + "<employer_type>"+""+"</employer_type>"; 



				add_xml_str = add_xml_str + "<no_of_paid_installment>"+""+"</no_of_paid_installment><company_flag>N</company_flag></InternalBureauIndividualProducts>"; // to be populated later
			}	

		}
		SKLogger.writeLog("RLOSCommon", "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}

	public String InternalBureauPipelineProducts(){
		SKLogger.writeLog("RLOSCommon java file", "inside InternalBureauPipelineProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "SELECT cifid,product_type,custroletype,lastupdatedate,totalamount,totalnoofinstalments,totalloanamount FROM ng_RLOS_CUSTEXPOSE_LoanDetails  with (nolock) where Wi_Name = '"+formObject.getWFWorkitemName()+"' and  LoanStat = 'Pipeline'";
		SKLogger.writeLog("InternalBureauPipelineProducts sQuery"+sQuery, "");
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		SKLogger.writeLog("InternalBureauPipelineProducts list size"+OutputXML.size(), "");

		for (int i = 0; i<OutputXML.size();i++){

			String cifId = "";
			String Product = "";
			String role = "";
			String lastUpdateDate = ""; 
			String TotAmount = "";
			String TotNoOfInstlmnt = "";
			String TotLoanAmt = "";

			if(!(OutputXML.get(i).get(0) == null || OutputXML.get(i).get(0).equals("")) ){
				cifId = OutputXML.get(i).get(0).toString();
			}
			if(!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals("")) ){
				Product = OutputXML.get(i).get(1).toString();
			}
			if(!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2).equals("")) ){
				role = OutputXML.get(i).get(2).toString();
			}
			if(!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals("")) ){
				lastUpdateDate = OutputXML.get(i).get(3).toString();
			}
			if(!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals("")) ){
				TotAmount = OutputXML.get(i).get(4).toString();
			}
			if(!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5).equals("")) ){
				TotNoOfInstlmnt = OutputXML.get(i).get(5).toString();
			}
			if(!(OutputXML.get(i).get(6) == null || OutputXML.get(i).get(6).equals("")) ){
				TotLoanAmt = OutputXML.get(i).get(6).toString();
			}

			if(cifId!=null && !cifId.equalsIgnoreCase("") && !cifId.equalsIgnoreCase("null")){
				add_xml_str = add_xml_str + "<InternalBureauPipelineProducts><Pipeline_Products_ID>"+""+"</Pipeline_Products_ID>";// to be populated later
				add_xml_str = add_xml_str + "<applicant_id>"+cifId+"</applicant_id>";
				add_xml_str = add_xml_str + "<internal_bureau_pipeline_products_id>"+"123123"+"</internal_bureau_pipeline_products_id>";// to be populated later
				add_xml_str = add_xml_str + "<ppl_provider_no>"+""+"</ppl_provider_no>";
				add_xml_str = add_xml_str + "<ppl_product>"+Product+"</ppl_product>";
				add_xml_str = add_xml_str + "<ppl_type_of_contract>"+""+"</ppl_type_of_contract>";
				add_xml_str = add_xml_str + "<ppl_phase>"+""+"</ppl_phase>"; // to be populated later

				add_xml_str = add_xml_str + "<ppl_role>"+Product+"</ppl_role>";
				add_xml_str = add_xml_str + "<ppl_date_of_last_update>"+lastUpdateDate+"</ppl_date_of_last_update>";
				add_xml_str = add_xml_str + "<ppl_total_amount>"+TotAmount+"</ppl_total_amount>";
				add_xml_str = add_xml_str + "<ppl_no_of_instalments>"+TotNoOfInstlmnt+"</ppl_no_of_instalments>";
				add_xml_str = add_xml_str + "<ppl_credit_limit>"+TotLoanAmt+"</ppl_credit_limit>";

				add_xml_str = add_xml_str + "<ppl_no_of_days_in_pipeline>"+""+"</ppl_no_of_days_in_pipeline><company_flag>N</company_flag></InternalBureauPipelineProducts>"; // to be populated later
			}

		}
		SKLogger.writeLog("RLOSCommon", "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}

	public String ExternalBureauData(){
		SKLogger.writeLog("RLOSCommon java file", "inside ExternalBureauData : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "select CifId,OutStanding_Balance,OverdueAmt,TotalAmt,'' as col1,'' as col2, '' as col3, '' as col4,'' as col5 from nr_rlos_cust_extexpo_LoanDetails where wi_name  = '"+formObject.getWFWorkitemName()+"' union select CifId,CurrentBalance,OverdueAmount,CashLimit,numofchequeret3,numofddsret3,numofchequeret6,numofddsret6,AECBHistMonthCnt from ng_rlos_cust_extexpo_CardDetails where wi_name  = '"+formObject.getWFWorkitemName()+"'";
		SKLogger.writeLog("ExternalBureauData sQuery"+sQuery, "");
		String  add_xml_str ="";
		try{
			List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
			SKLogger.writeLog("ExternalBureauData list size"+OutputXML.size(), "");

			float TotOutstandingAmt = 0.0f;
			float TotOverdueAmt = 0.0f;
			float TotalExposure = 0.0f;
			for(int i = 0; i<OutputXML.size();i++){
				if(OutputXML.get(i).get(1)!=null && !OutputXML.get(i).get(1).isEmpty() &&  !OutputXML.get(i).get(1).equals("") && !OutputXML.get(i).get(1).equalsIgnoreCase("null") ){
					TotOutstandingAmt = TotOutstandingAmt + Float.parseFloat(OutputXML.get(i).get(1));
				}
				if(OutputXML.get(i).get(2)!=null && !OutputXML.get(i).get(2).isEmpty() &&  !OutputXML.get(i).get(2).equals("") && !OutputXML.get(i).get(2).equalsIgnoreCase("null") ){
					TotOverdueAmt = TotOverdueAmt + Float.parseFloat(OutputXML.get(i).get(2));
				}
				if(OutputXML.get(i).get(3)!=null && !OutputXML.get(i).get(3).isEmpty() &&  !OutputXML.get(i).get(3).equals("") && !OutputXML.get(i).get(3).equalsIgnoreCase("null") ){
					TotalExposure = TotalExposure + Float.parseFloat(OutputXML.get(i).get(3));
				}
			}

			for (int i = 0; i<OutputXML.size();i++){

				String CifId = "";

				String numofchequeret3 = "";
				String numofddsret3 = "";
				String numofchequeret6 = "";
				String numofddsret6 = "";
				String AECBHistMonthCnt = "";
				if(!(OutputXML.get(i).get(0) == null || OutputXML.get(i).get(0).equals("")) ){
					CifId = OutputXML.get(i).get(0).toString();
				}
				if(!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals("")) ){
					numofchequeret3 = OutputXML.get(i).get(4).toString();
				}				
				if(!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5).equals("")) ){
					numofddsret3 = OutputXML.get(i).get(5).toString();
				}
				if(!(OutputXML.get(i).get(6) == null || OutputXML.get(i).get(6).equals("")) ){
					numofchequeret6 = OutputXML.get(i).get(6).toString();
				}
				if(!(OutputXML.get(i).get(7) == null || OutputXML.get(i).get(7).equals("")) ){
					numofddsret6 = OutputXML.get(i).get(7).toString();
				}				
				if(!(OutputXML.get(i).get(8) == null || OutputXML.get(i).get(8).equals("")) ){
					AECBHistMonthCnt = OutputXML.get(i).get(8).toString();
				}

				if(CifId!=null && !CifId.equalsIgnoreCase("") && !CifId.equalsIgnoreCase("null")){
					add_xml_str = add_xml_str + "<ExternalBureau>"; // to be populated later
					add_xml_str = add_xml_str + "<applicant_id>"+CifId+"</applicant_id>";
					add_xml_str = add_xml_str + "<full_name>"+formObject.getNGValue("cmplx_Customer_FIrstNAme")+"</full_name>"; // fullname fieldname from form
					add_xml_str = add_xml_str + "<total_out_bal>"+TotOutstandingAmt+"</total_out_bal>";

					add_xml_str = add_xml_str + "<total_overdue>"+TotOverdueAmt+"</total_overdue>";
					add_xml_str = add_xml_str + "<no_default_contract>"+OutputXML.size()+"</no_default_contract>";
					add_xml_str = add_xml_str + "<total_exposure>"+TotalExposure+"</total_exposure>";
					add_xml_str = add_xml_str + "<worst_curr_pay>"+""+"</worst_curr_pay>"; // to be populated later
					add_xml_str = add_xml_str + "<worst_curr_pay_24>"+""+"</worst_curr_pay_24>"; // to be populated later
					add_xml_str = add_xml_str + "<worst_status_24>"+""+"</worst_status_24>"; // to be populated later


					add_xml_str = add_xml_str + "<no_of_rec>"+""+"</no_of_rec>"; // to be populated later
					add_xml_str = add_xml_str + "<cheque_return_3mon>"+numofchequeret3+"</cheque_return_3mon>";
					add_xml_str = add_xml_str + "<dds_return_3mon>"+numofddsret3+"</dds_return_3mon>";
					add_xml_str = add_xml_str + "<cheque_return_6mon>"+numofchequeret6+"</cheque_return_6mon>";
					add_xml_str = add_xml_str + "<dds_return_6mon>"+numofddsret6+"</dds_return_6mon>";
					add_xml_str = add_xml_str + "<prod_external_writeoff_amount>"+""+"</prod_external_writeoff_amount>";
					add_xml_str = add_xml_str + "<no_of_cheque_bounce_ext_3mon>"+""+"</no_of_cheque_bounce_ext_3mon>";

					add_xml_str = add_xml_str + "<no_of_dds_return_ext_3mon>"+""+"</no_of_dds_return_ext_3mon>";
					add_xml_str = add_xml_str + "<no_months_aecb_history >"+AECBHistMonthCnt+"</no_months_aecb_history >";

					add_xml_str = add_xml_str + "<no_of_declined_rejected_last_3mon>"+""+"</no_of_declined_rejected_last_3mon><company_flag>N</company_flag></ExternalBureau>";// to be populated later
				}
			}
			SKLogger.writeLog("RLOSCommon", "Internal liab tag Cration: "+ add_xml_str);
			return add_xml_str;
		}
		catch(Exception e)
		{
			SKLogger.writeLog("RLOSCommon", "Exception occurred in externalBureauData()"+e.getMessage()+printException(e));
			return null;
		}
	}

	public String ExternalBouncedCheques(){
		SKLogger.writeLog("RLOSCommon java file", "inside ExternalBouncedCheques : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "SELECT cifid,number,amount,reasoncode,returndate,providerno FROM nr_rlos_cust_extexpo_ChequeDetails  with (nolock) where Wi_Name = '"+formObject.getWFWorkitemName()+"' and Request_Type = 'ExternalExposure'";
		SKLogger.writeLog("ExternalBouncedCheques sQuery"+sQuery, "");
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		SKLogger.writeLog("ExternalBouncedCheques list size"+OutputXML.size(), "");

		for (int i = 0; i<OutputXML.size();i++){

			String cifId = "";
			String chqNo = "";
			String Amount = "";
			String Reason = ""; 
			String returnDate = "";
			String providerNo = "";

			if(!(OutputXML.get(i).get(0) == null || OutputXML.get(i).get(0).equals("")) ){
				cifId = OutputXML.get(i).get(0).toString();
			}
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

			if(cifId!=null && !cifId.equalsIgnoreCase("") && !cifId.equalsIgnoreCase("null")){
				add_xml_str = add_xml_str + "<ExternalBouncedCheques><applicant_id>"+cifId+"</applicant_id>";
				add_xml_str = add_xml_str + "<external_bounced_cheques_id>"+""+"</external_bounced_cheques_id>";
				add_xml_str = add_xml_str + "<bounced_cheque>"+""+"</bounced_cheque>";
				add_xml_str = add_xml_str + "<cheque_no>"+chqNo+"</cheque_no>";
				add_xml_str = add_xml_str + "<amount>"+Amount+"</amount>";
				add_xml_str = add_xml_str + "<reason>"+Reason+"</reason>";
				add_xml_str = add_xml_str + "<return_date>"+returnDate+"</return_date>"; // to be populated later
				add_xml_str = add_xml_str + "<provider_no>"+providerNo+"</provider_no><company_flag>N</company_flag></ExternalBouncedCheques>"; // to be populated later
			}

		}
		SKLogger.writeLog("RLOSCommon", "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}

	public String ExternalBureauIndividualProducts(){
		SKLogger.writeLog("RLOSCommon java file", "inside ExternalBureauIndividualProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "select CifId,AgreementId,LoanType,CustRoleType,LoanApprovedDate,LoanMaturityDate,OutStanding_Balance,TotalAmt,PaymentsAmt,TotalNoOfInstalments,RemainingInstalments,CreditLimit,OverdueAmt,NofDaysPmtDelay,'' as col1,'' as col2,'' as col3,'' as col4 from nr_rlos_cust_extexpo_LoanDetails where wi_name  = '"+formObject.getWFWorkitemName()+"' union select CifId,CardEmbossNum,CardType,CustRoleType,StartDate,StartDate,CurrentBalance,TotalAmount,PaymentsAmount,NoOfInstallments,'' as col5,CashLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,DPD30Last6Months,DPD60Last18Months,AECBHistMonthCnt from ng_rlos_cust_extexpo_CardDetails where wi_name  = '"+formObject.getWFWorkitemName()+"'";
		SKLogger.writeLog("ExternalBureauIndividualProducts sQuery"+sQuery, "");
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		SKLogger.writeLog("ExternalBureauIndividualProducts list size"+OutputXML.size(), "");

		for (int i = 0; i<OutputXML.size();i++){

			String CifId = "";
			String AgreementId = "";
			String ContractType = "";
			String CustRoleType = ""; 
			String start_date = "";
			String close_date = "";
			String OutStanding_Balance = "";
			String TotalAmt = "";
			String PaymentsAmt = "";
			String TotalNoOfInstalments = "";
			String RemainingInstalments = "";
			String CreditLimit = ""; 
			String OverdueAmt = "";
			String NofDaysPmtDelay = "";
			String MonthsOnBook = "";
			String DPD30Last6Months = "";
			String DPD60Last18Months = "";
			String AECBHistMonthCnt = "";

			if(!(OutputXML.get(i).get(0) == null || OutputXML.get(i).get(0).equals("")) ){
				CifId = OutputXML.get(i).get(0).toString();
			}
			if(!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals("")) ){
				AgreementId = OutputXML.get(i).get(1).toString();
			}				
			if(!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2).equals("")) ){
				ContractType = OutputXML.get(i).get(2).toString();
			}
			if(!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals("")) ){
				CustRoleType = OutputXML.get(i).get(3).toString();
			}
			if(!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals("")) ){
				start_date = OutputXML.get(i).get(4).toString();
			}				
			if(!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5).equals("")) ){
				close_date = OutputXML.get(i).get(5).toString();
			}
			if(!(OutputXML.get(i).get(6) == null || OutputXML.get(i).get(6).equals("")) ){
				OutStanding_Balance = OutputXML.get(i).get(6).toString();
			}
			if(!(OutputXML.get(i).get(7) == null || OutputXML.get(i).get(7).equals("")) ){
				TotalAmt = OutputXML.get(i).get(7).toString();
			}				
			if(!(OutputXML.get(i).get(8) == null || OutputXML.get(i).get(8).equals("")) ){
				PaymentsAmt = OutputXML.get(i).get(8).toString();
			}
			if(!(OutputXML.get(i).get(9) == null || OutputXML.get(i).get(9).equals("")) ){
				TotalNoOfInstalments = OutputXML.get(i).get(9).toString();
			}
			if(!(OutputXML.get(i).get(10) == null || OutputXML.get(i).get(10).equals("")) ){
				RemainingInstalments = OutputXML.get(i).get(10).toString();
			}				
			if(!(OutputXML.get(i).get(11) == null || OutputXML.get(i).get(11).equals("")) ){
				CreditLimit = OutputXML.get(i).get(11).toString();
			}
			if(!(OutputXML.get(i).get(12) == null || OutputXML.get(i).get(12).equals("")) ){
				OverdueAmt = OutputXML.get(i).get(12).toString();
			}
			if(!(OutputXML.get(i).get(13) == null || OutputXML.get(i).get(13).equals("")) ){
				NofDaysPmtDelay = OutputXML.get(i).get(13).toString();
			}				
			if(!(OutputXML.get(i).get(14) == null || OutputXML.get(i).get(14).equals("")) ){
				MonthsOnBook = OutputXML.get(i).get(14).toString();
			}
			if(!(OutputXML.get(i).get(15) == null || OutputXML.get(i).get(15).equals("")) ){
				DPD30Last6Months = OutputXML.get(i).get(15).toString();
			}
			if(!(OutputXML.get(i).get(16) == null || OutputXML.get(i).get(16).equals("")) ){
				DPD60Last18Months = OutputXML.get(i).get(16).toString();
			}				
			if(!(OutputXML.get(i).get(17) == null || OutputXML.get(i).get(17).equals("")) ){
				AECBHistMonthCnt = OutputXML.get(i).get(17).toString();
			}
			if(CifId!=null && !CifId.equalsIgnoreCase("") && !CifId.equalsIgnoreCase("null")){
				add_xml_str = add_xml_str + "<ExternalBureauIndividualProducts><applicant_id>"+CifId+"</applicant_id>";
				add_xml_str = add_xml_str + "<external_bureau_individual_products_id>"+AgreementId+"</external_bureau_individual_products_id>";
				add_xml_str = add_xml_str + "<contract_type>"+ContractType+"</contract_type>";
				add_xml_str = add_xml_str + "<provider_no>"+""+"</provider_no>";
				add_xml_str = add_xml_str + "<phase>"+""+"</phase>";
				add_xml_str = add_xml_str + "<role_of_customer>"+CustRoleType+"</role_of_customer>";
				add_xml_str = add_xml_str + "<start_date>"+start_date+"</start_date>"; // to be populated later

				add_xml_str = add_xml_str + "<close_date>"+close_date+"</close_date>";
				add_xml_str = add_xml_str + "<outstanding_balance>"+OutStanding_Balance+"</outstanding_balance>";
				add_xml_str = add_xml_str + "<total_amount>"+TotalAmt+"</total_amount>";
				add_xml_str = add_xml_str + "<payments_amount>"+PaymentsAmt+"</payments_amount>";
				add_xml_str = add_xml_str + "<total_no_of_instalments>"+TotalNoOfInstalments+"</total_no_of_instalments>";
				add_xml_str = add_xml_str + "<no_of_remaining_instalments>"+RemainingInstalments+"</no_of_remaining_instalments>";
				add_xml_str = add_xml_str + "<worst_status>"+""+"</worst_status>";
				add_xml_str = add_xml_str + "<worst_status_date>"+""+"</worst_status_date>";

				add_xml_str = add_xml_str + "<credit_limit>"+CreditLimit+"</credit_limit>";
				add_xml_str = add_xml_str + "<overdue_amount>"+OverdueAmt+"</overdue_amount>";
				add_xml_str = add_xml_str + "<no_of_days_payment_delay>"+NofDaysPmtDelay+"</no_of_days_payment_delay>";
				add_xml_str = add_xml_str + "<mob>"+MonthsOnBook+"</mob>";
				add_xml_str = add_xml_str + "<last_repayment_date>"+""+"</last_repayment_date>";
				add_xml_str = add_xml_str + "<currently_current>"+""+"</currently_current>";
				add_xml_str = add_xml_str + "<current_utilization>"+""+"</current_utilization>";
				add_xml_str = add_xml_str + "<dpd_30_last_6_mon>"+DPD30Last6Months+"</dpd_30_last_6_mon>";

				add_xml_str = add_xml_str + "<dpd_60p_in_last_18_mon>"+DPD60Last18Months+"</dpd_60p_in_last_18_mon>";
				add_xml_str = add_xml_str + "<no_months_aecb_history>"+AECBHistMonthCnt+"</no_months_aecb_history>";
				add_xml_str = add_xml_str + "<other_bank_card_type>"+""+"</other_bank_card_type>";
				add_xml_str = add_xml_str + "<delinquent_in_last_3months>"+""+"</delinquent_in_last_3months>";
				add_xml_str = add_xml_str + "<clean_funded>"+""+"</clean_funded>";
				add_xml_str = add_xml_str + "<cac_indicator>"+""+"</cac_indicator>";
				add_xml_str = add_xml_str + "<qc_emi>"+""+"</qc_emi>";
				add_xml_str = add_xml_str + "<qc_amount>"+""+"</qc_amount><company_flag>N</company_flag></ExternalBureauIndividualProducts>";
			}


		}
		SKLogger.writeLog("RLOSCommon", "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}

	public String ExternalBureauPipelineProducts(){
		SKLogger.writeLog("RLOSCommon java file", "inside ExternalBureauPipelineProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "SELECT  cifid,loantype,product_type,custroletype,datelastupdated,total_amount,totalnoofinstalments,creditlimit,noofdaysinpipeline  FROM nr_rlos_cust_extexpo_LoanDetails  with (nolock) where Wi_Name = '"+formObject.getWFWorkitemName()+"' and LoanStat = 'Pipeline'";
		SKLogger.writeLog("ExternalBureauPipelineProducts sQuery"+sQuery, "");
		String  add_xml_str = "";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		SKLogger.writeLog("ExternalBureauPipelineProducts list size"+OutputXML.size(), "");

		for (int i = 0; i<OutputXML.size();i++){

			String cifId = "";
			String contractType = "";
			String productType = "";
			String role = ""; 
			String lastUpdateDate = "";
			String TotAmt = "";
			String noOfInstalmnt = "";
			String creditLimit = "";
			String noOfDayinPpl = "";
			if(!(OutputXML.get(i).get(0) == null || OutputXML.get(i).get(0).equals("")) ){
				cifId = OutputXML.get(i).get(0).toString();
			}
			if(!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals("")) ){
				contractType = OutputXML.get(i).get(1).toString();
			}
			if(!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2).equals("")) ){
				productType = OutputXML.get(i).get(2).toString();
			}
			if(!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals("")) ){
				role = OutputXML.get(i).get(3).toString();
			}
			if(!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals("")) ){
				lastUpdateDate = OutputXML.get(i).get(4).toString();
			}
			if(!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5).equals("")) ){
				TotAmt = OutputXML.get(i).get(5).toString();
			}
			if(!(OutputXML.get(i).get(6) == null || OutputXML.get(i).get(6).equals("")) ){
				noOfInstalmnt = OutputXML.get(i).get(6).toString();
			}
			if(!(OutputXML.get(i).get(7) == null || OutputXML.get(i).get(7).equals("")) ){
				creditLimit = OutputXML.get(i).get(7).toString();
			}
			if(!(OutputXML.get(i).get(8) == null || OutputXML.get(i).get(8).equals("")) ){
				noOfDayinPpl = OutputXML.get(i).get(8).toString();
			}
			if(cifId!=null && !cifId.equalsIgnoreCase("") && !cifId.equalsIgnoreCase("null")){
				add_xml_str = add_xml_str + "<ExternalBureauPipelineProducts><applicant_ID>"+cifId+"</applicant_ID>";
				add_xml_str = add_xml_str + "<external_bureau_pipeline_products_id>"+"3234"+"</external_bureau_pipeline_products_id>";
				add_xml_str = add_xml_str + "<ppl_provider_no>"+""+"</ppl_provider_no>";
				add_xml_str = add_xml_str + "<ppl_type_of_contract>"+contractType+"</ppl_type_of_contract>";
				add_xml_str = add_xml_str + "<ppl_type_of_product>"+productType+"</ppl_type_of_product>";
				add_xml_str = add_xml_str + "<ppl_phase>"+""+"</ppl_phase>";
				add_xml_str = add_xml_str + "<ppl_role>"+role+"</ppl_role>"; 

				add_xml_str = add_xml_str + "<ppl_date_of_last_update>"+lastUpdateDate+"</ppl_date_of_last_update>";
				add_xml_str = add_xml_str + "<ppl_total_amount>"+TotAmt+"</ppl_total_amount>";
				add_xml_str = add_xml_str + "<ppl_no_of_instalments>"+noOfInstalmnt+"</ppl_no_of_instalments>";
				add_xml_str = add_xml_str + "<ppl_credit_limit>"+"creditLimit"+"</ppl_credit_limit>";

				add_xml_str = add_xml_str + "<ppl_no_of_days_in_pipeline>"+"noOfDayinPpl"+"</ppl_no_of_days_in_pipeline><company_flag>N</company_flag></ExternalBureauPipelineProducts>"; // to be populated later

			}


		}
		SKLogger.writeLog("RLOSCommon", "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}

	public String printException(Exception e){
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String exception = sw.toString();
		return exception;

	}

	public static String ExecuteQuery_APUpdate(String tableName,String columnName,String strValues,String sWhere,String cabinetName,String sessionId)
	{
		WFInputXml wfInputXml = new WFInputXml();
		if(strValues==null)
		{
			strValues = "''";
		}
		wfInputXml.appendStartCallName("APUpdate", "Input");
		wfInputXml.appendTagAndValue("TableName",tableName);
		wfInputXml.appendTagAndValue("ColName",columnName);
		wfInputXml.appendTagAndValue("Values",strValues);
		wfInputXml.appendTagAndValue("WhereClause",sWhere);
		wfInputXml.appendTagAndValue("EngineName",cabinetName);
		wfInputXml.appendTagAndValue("SessionId",sessionId);
		wfInputXml.appendEndCallName("APUpdate","Input");
		return wfInputXml.toString();
	}
	
	 public static String ExecuteQuery_APdelete(String tableName,String sWhere, String cabinetName, String sessionId)
	    {
	  String sInputXML = "<?xml version=\"1.0\"?>"+
						"<APDelete_Input><Option>APDelete</Option>"+
									"<TableName>"+tableName+"</TableName>"+
									"<WhereClause>"+sWhere+"</WhereClause>"+
									"<EngineName>"+cabinetName+"</EngineName>"+
									"<SessionId>"+sessionId+"</SessionId>"+
								"</APDelete_Input>";
	    return sInputXML;	
	}
	 
	public static String ExecuteQuery_APInsert(String tableName,String columnName,String strValues, String cabinetName, String sessionId)
	{
		String sInputXML = "<?xml version=\"1.0\"?>" +"\n"+
		"<APInsert_Input>" +"\n"+
		"<Option>APInsert</Option>" +"\n"+
		"<TableName>" + tableName + "</TableName>" +"\n"+
		"<ColName>" + columnName + "</ColName>" +"\n"+
		"<Values>" + strValues + "</Values>" +"\n"+
		"<EngineName>" + cabinetName + "</EngineName>" +"\n"+
		"<SessionId>" + sessionId + "</SessionId>" +"\n"+
		"</APInsert_Input>";
		return sInputXML;	
	}

	public static String getTagValue(String parseXml,String tagName,String subTagName)
	{

		SKLogger.writeLog("getTagValue jsp: inside: ","");
		String [] valueArr= null;
		String mainCodeValue = "";


		SKLogger.writeLog("tagName jsp: getTagValue: "+tagName,"");
		SKLogger.writeLog("subTagName jsp: getTagValue: "+subTagName,"");



		Map<Integer, String> tagValuesMap= new LinkedHashMap<Integer, String>();		 
		tagValuesMap=getTagDataParent(parseXml,tagName,subTagName);

		Map<Integer, String> map = tagValuesMap;
		String colValue="";
		for (Map.Entry<Integer, String> entry : map.entrySet())
		{
			valueArr=entry.getValue().split("~");

			SKLogger.writeLog("tag values" + entry.getValue(),"");
			mainCodeValue = valueArr[1];	

			SKLogger.writeLog("mainCodeValue" + mainCodeValue,"");

		}
		return mainCodeValue;
	}

	public static Map<Integer, String> getTagDataParent(String parseXml,String tagName,String subTagName){

		Map<Integer, String> tagValuesMap= new LinkedHashMap<Integer, String>(); 
		try {
			//WriteLog("getTagDataParent jsp: parseXml: "+parseXml);
			//WriteLog("getTagDataParent jsp: tagName: "+tagName);
			//WriteLog("getTagDataParent jsp: subTagName: "+subTagName);
			//InputStream is = new FileInputStream(parseXml);
			InputStream is = new ByteArrayInputStream(parseXml.getBytes());
			//WriteLog("getTagDataParent jsp: strOutputXml: "+is);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName(tagName);

			String[] values =subTagName.split(",");
			String value="";
			String subTagDerivedvalue="";
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					Node uNode=eElement.getParentNode();

					for(int j=0;j<values.length;j++){
						if(eElement.getElementsByTagName(values[j]).item(0) !=null){
							value=value+","+eElement.getElementsByTagName(values[j]).item(0).getTextContent();
							subTagDerivedvalue=subTagDerivedvalue+","+values[j];
						}

					}
					value=value.substring(1,value.length());
					subTagDerivedvalue=subTagDerivedvalue.substring(1,subTagDerivedvalue.length());

					Node nNode_c = doc.getElementsByTagName(uNode.getNodeName()).item(temp);
					Element eElement_agg = (Element) nNode_c;
					String id_val = "";
					if(uNode.getNodeName().equalsIgnoreCase("LoanDetails")){
						id_val = eElement_agg.getElementsByTagName("AgreementId").item(0).getTextContent();
					}
					else if(uNode.getNodeName().equalsIgnoreCase("CardDetails")){
						id_val = eElement_agg.getElementsByTagName("CardEmbossNum").item(0).getTextContent();
					}
					else if(uNode.getNodeName().equalsIgnoreCase("AcctDetails")){
						id_val = eElement_agg.getElementsByTagName("AcctId").item(0).getTextContent();
					}
					else{
						id_val="";
					}

					tagValuesMap.put(temp+1, subTagDerivedvalue+"~"+value+"~"+uNode.getNodeName()+"~"+id_val);
					value="";
					subTagDerivedvalue="";
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
			//WriteLog("Exception occured in getTagDataParent method:  "+e.getMessage());
		}
		return tagValuesMap;
	}


}


