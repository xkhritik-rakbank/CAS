//----------------------------------------------------------------------------------------------------


//		NEWGEN SOFTWARE TECHNOLOGIES LIMITED
//Group						: AP2
//Product / Project			: RLOS
//Module					: CAS
//File Name					: RLOSCommon.java
//Author					: Akshay Gupta
//Date written (DD/MM/YYYY)	: 06/08/2017
//Description				: 
//----------------------------------------------------------------------------------------------------

package com.newgen.omniforms.user;


import com.newgen.custom.Common_Utils;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.Column;
import com.newgen.omniforms.component.IRepeater;
import com.newgen.omniforms.component.ListView;
import com.newgen.omniforms.component.PickList;
import com.newgen.omniforms.context.FormContext;
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
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Calendar;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.validator.ValidatorException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import java.text.DecimalFormat;


public class RLOSCommon extends Common implements Serializable
{


	private static final long serialVersionUID = 1L;
	HashMap<String,String> hm= new HashMap<String,String>(); // not a nullable HashMap te
	String popupFlag="";
//CardDispatchToButton

	public String getDataFromSourceCodeMaster(String columnName){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//RLOS.mLogger.info( "Inside getRMName()"); 
		String query="Select "+columnName+" from NG_MASTER_SourceCode with(nolock) where userid='"+formObject.getUserName()+"'";
		RLOS.mLogger.info( "Query to fetch RMName:"+query); 
		List<List<String>> result=formObject.getNGDataFromDataCache(query);
		RLOS.mLogger.info( "result of fetch RMname query: "+result); 

		if(result==null  || result.isEmpty()){
			return "";
		}
		else{
			return result.get(0).get(0);
		}
	}
	public void checkforBPACase(){
		//changes by saurabh on 5th Dec
		try{
			String result ="";
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_BPA").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2))){
				String apptype = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,4);
				if(apptype.equalsIgnoreCase("ALBUN")){
					apptype = "AUTO";
				}
				else if(apptype.equalsIgnoreCase("PLBUN")){
					apptype = "PERSONAL";
				}
				String declinedCode = "D999";
				String query = "select count(loantype) from (select liab.loantype,dectech.DeclinedReasonCode from ng_RLOS_CUSTEXPOSE_LoanDetails liab with(nolock) join ng_rlos_IGR_Eligibility_PersonalLoan dectech with(nolock) on liab.wi_name=dectech.wi_name where liab.wi_name = '"+formObject.getWFWorkitemName()+"' and liab.LoanType = '"+apptype+"' and datediff(dd,convert(datetime,liab.Loan_disbursal_date),GETDATE())<30 and dectech.DeclinedReasonCode like '%"+declinedCode+"%') as tempTable";
				List<List<String>> record = formObject.getNGDataFromDataCache(query);
				if(record!=null && record.size()>0){
					//RLOS.mLogger.info( "BPA inside record found: "+record.get(0).get(0));
					if(record.get(0)!=null && record.get(0).get(0)!=null && !record.get(0).get(0).equalsIgnoreCase("0")){
						result = "BPA is not Applicable";
						formObject.setNGValue("freeze_Form","Y");
						freezeForm(true);
					}
				}
			}
		}catch(Exception ex){
			printException(ex);
		}
	}
	public void freezeForm(boolean val){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		/*String[] frames = new String[]{"Customer_Frame1","GuarantorDetails_Frame1","EMploymentDetails_Frame1","IncomeDetails_Frame1","CompanyDetails_Frame1","CardDetails_Frame1","PartnerDetails_Frame1","AuthorisedSignDetails_Frame1","ELigibiltyAndProductInfo_Frame1","Liability_New_Frame1","CC_Loan_Frame1","AddressDetails_Frame1","ReferenceDetails_Frame1","AltContactDetails_Frame1","SupplementCardDetails_Frame1","FATCA_Frame1","KYC_Frame1","OECD_Frame1","IncomingDoc_Frame1","DecisionHistory_Frame1"};
		for(String frame:frames){
			formObject.setNGValue("cmplx_Customer_DOb")
			if(formObject.isVisible(frame)){
				formObject.setLocked(frame, val);
			}
		}*/
		if(val){
			formObject.setNGValue("cmplx_DecisionHistory_Decision", "Reject");
			formObject.setNGFocus("Modify");
		}
		else{
			formObject.setNGValue("cmplx_DecisionHistory_Decision", "");
		}
		formObject.setLocked("cmplx_DecisionHistory_Decision", val);
		formObject.setLocked("ELigibiltyAndProductInfo_Button1", val);
	}

	//added by aman for Drop4 on 4th April	

	public void multipleLiability(){
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();


			String sQueryInt = "SELECT count(settlement_flag) FROM (SELECT settlement_flag FROM ng_RLOS_CUSTEXPOSE_CardDetails with(nolock) WHERE Settlement_Flag='true' AND wi_name='"+formObject.getWFWorkitemName()+"' UNION SELECT settlement_flag FROM ng_RLOS_CUSTEXPOSE_LoanDetails with(nolock) WHERE Settlement_Flag='true' AND wi_name='"+formObject.getWFWorkitemName()+"' )asas";
			String Int="";
			String sQueryExt = "SELECT count(settlement_flag) FROM (SELECT settlement_flag FROM ng_rlos_cust_extexpo_CardDetails with(nolock) WHERE Settlement_Flag='true' AND wi_name='"+formObject.getWFWorkitemName()+"' UNION SELECT settlement_flag FROM ng_rlos_cust_extexpo_LoanDetails with(nolock) WHERE Settlement_Flag='true' AND wi_name='"+formObject.getWFWorkitemName()+"' )asas";
			String Ext="";
			List<List<String>> OutputXMLInt = formObject.getDataFromDataSource(sQueryInt);
			List<List<String>> OutputXMLExt = formObject.getDataFromDataSource(sQueryExt);



			if(!OutputXMLInt.isEmpty()){
				if(OutputXMLInt.get(0).get(0)!=null && !OutputXMLInt.get(0).get(0).isEmpty() &&  !OutputXMLInt.get(0).get(0).equals("") && !OutputXMLInt.get(0).get(0).equalsIgnoreCase("null") && !OutputXMLInt.get(0).get(0).equalsIgnoreCase("0")){
					{
						Int="Y";
					}
				}
			}
			if(!OutputXMLExt.isEmpty()){
				if(OutputXMLExt.get(0).get(0)!=null && !OutputXMLExt.get(0).get(0).isEmpty() &&  !OutputXMLExt.get(0).get(0).equals("") && !OutputXMLExt.get(0).get(0).equalsIgnoreCase("null") && !OutputXMLExt.get(0).get(0).equalsIgnoreCase("0")){
					{
						Ext="Y";
					}
				}
			}
			if (Int.equalsIgnoreCase("Y") && Ext.equalsIgnoreCase("Y")){
				formObject.setNGValue("SettlementInternalExternal", "I-E");
			}
			else if (Int.equalsIgnoreCase("Y")){
				formObject.setNGValue("SettlementInternalExternal", "I");
			}
			else if (Ext.equalsIgnoreCase("Y")){
				formObject.setNGValue("SettlementInternalExternal", "E");
			}
			else{
				formObject.setNGValue("SettlementInternalExternal", "None");
			}


		}catch(Exception e)
		{
			RLOS.mLogger.info( "Exception occurred in multipleLiability"+e.getMessage()+printException(e));
		}
	}
	//added by aman for Drop4 on 4th April
	//function by saurabh on 11th Oct for setting charges fields values based on schemeID.

	public void setChargesFields(){//logic changed by saurabh on 8th nov 2017.
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try{
			String scheme = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,8);
			String product = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,1);
			String application_type = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,4);
			if(application_type.contains("NEW")){
				application_type = "New";
			}
			else if(application_type.contains("TOP")){
				application_type = "Top up";
			}
			else if(application_type.contains("TKO")){
				application_type = "Take Over";
			}
			if(product!=null && product.equalsIgnoreCase(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_PersonalLoan"))){
				if(scheme!=null && !(scheme.equalsIgnoreCase("")||scheme.equalsIgnoreCase("--Select--")|| scheme.equalsIgnoreCase(" "))){
					String query = "select CHARGERATE,CHARGEAMT from ng_master_charges with(nolock) where ChargeID = (select LPF_ChargeID from NG_master_Scheme with(nolock) where SCHEMEID = '"+scheme+"' and Applicationtype = '"+application_type+"')";
					//RLOS.mLogger.info("RLOS_Common"+ "Query for charges is charges: "+query);
					List<List<String>> result = formObject.getNGDataFromDataCache(query);
					if(result!=null && result.size()>0 && result.get(0)!=null){
						String chargerate = result.get(0).get(0);
						formObject.setNGValue("cmplx_EligibilityAndProductInfo_LPF",chargerate);
						//below line commented for future functionality on LPF amount.
						//formObject.setNGValue("cmplx_EligibilityAndProductInfo_InsuranceAmount",chargeamt);
					}
				}
			}
		}catch(Exception ex){
			RLOS.mLogger.info("RLOS_Common"+"Exception in fetching charges values: "+printException(ex));
		}
	}
	//sagarika for air arabia CR
	 public void AirArabiaValid() {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String query = "select count(*) from ng_rlos_IGR_Eligibility_CardLimit limit with (nolock) join ng_master_cardProduct mstr with (nolock) on limit.Card_Product=mstr.CODE where wi_name like '"+formObject.getWFWorkitemName()+"' and Cardproductselect='true' and mstr.DESCRIPTION like '%Air Arabia Card%'";
		List<List<String>> records = formObject.getDataFromDataSource(query);
		RLOS.mLogger.info("query produced is:"+query);
		try{
			if(records!=null && records.size()>0 && records.get(0)!=null){
				RLOS.mLogger.info("records list is:"+records);
				if("0".equals(records.get(0).get(0))){
					formObject.setVisible("AltContactDetails_Label7", false);
					formObject.setVisible("AlternateContactDetails_AirArabia", false);
				}
				else{
					formObject.setVisible("AltContactDetails_Label7", true);
					formObject.setVisible("AlternateContactDetails_AirArabia", true);
				}
			}
		}catch(Exception ex){
			RLOS.mLogger.info( "Exception in AirArabiaValid Function"+printException(ex));
		}
	}




	public String getYearsDifference(FormReference formObject,String controlName, String controlName2) {
		int MON;
		int Year;
		String age="";
		String DOB=formObject.getNGValue(controlName);
		//RLOS.mLogger.info(" Inside age + "+DOB);
		String Date2= formObject.getNGValue(controlName2);
		if (DOB!=null){	
			String[] Dob=DOB.split("/");
			String[] CurreDate=Date2.split("/");
			int monthbirthDate=Integer.parseInt(Dob[1]);
			int monthcurrDate=Integer.parseInt(CurreDate[1]);
			int YearbirthDate=Integer.parseInt(Dob[2]);
			int yearcurrDate=Integer.parseInt(CurreDate[2]);
			int daybirthDate=Integer.parseInt(Dob[0]);
			int daycurrDate=Integer.parseInt(CurreDate[0]);
			if (monthcurrDate<monthbirthDate){
				yearcurrDate=yearcurrDate-1;
				Year=yearcurrDate-YearbirthDate;
				MON=monthcurrDate-monthbirthDate;
				MON=12+MON;
				//below if added by saurabh on 15th Oct.
				if(daycurrDate<daybirthDate){
					MON--;
				}
				if ((MON==10)||(MON==11)){
					age=Year+"."+MON;}
				else{
					age=Year+".0"+MON;
				}

			}
			else if (monthcurrDate>monthbirthDate){
				Year=yearcurrDate-YearbirthDate;
				MON=monthcurrDate-monthbirthDate;
				//below if added by saurabh on 15th Oct.
				if(daycurrDate<daybirthDate){
					MON--;
				}

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



	//function by saurabh on 15th Oct.
	public void calculateMaturityValues(){
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			//RLOS.mLogger.info("Inside calculateMaturityValues(): ");
			String maturityDate = "";
			String tenore = formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor");
			if(tenore!=null && !"".equalsIgnoreCase(tenore) && !" ".equalsIgnoreCase(tenore)){
				int ten = Integer.parseInt(tenore);
				String frd = formObject.getNGValue("cmplx_EligibilityAndProductInfo_FirstRepayDate");
				String moratorium = formObject.getNGValue("cmplx_EligibilityAndProductInfo_Moratorium");
				if(moratorium!=null && !"".equalsIgnoreCase(moratorium)){
					maturityDate = plusyear(frd, 0, (ten-1), Integer.parseInt(moratorium),"dd/MM/yyy" );
				}
				else{
					maturityDate = plusyear(frd, 0, (ten-1), 0,"dd/MM/yyy" );
				}
				//RLOS.mLogger.info("value of maturity Date is : "+maturityDate);
				formObject.setNGValue("cmplx_EligibilityAndProductInfo_MaturityDate",maturityDate);
				formObject.setNGValue("cmplx_EligibilityAndProductInfo_ageAtMaturity",getYearsDifference(formObject, "cmplx_Customer_DOb", "cmplx_EligibilityAndProductInfo_MaturityDate"));
			}

		}
		catch(Exception ex){
			RLOS.mLogger.info("Exception Inside calculateMaturityValues(): "+printException(ex));

		}
	}	


	public static String plusyear(String currentDate,int i,int j,int k,String dateformat){
		DateTime dt = new DateTime();
		DateTimeFormatter fp = DateTimeFormat.forPattern(dateformat);
		DateTime cd = dt.parse(currentDate,fp).plusYears(i).plusMonths(j).plusDays(k);
		String close_date ="";
		if(dateformat.contains("-")){
			close_date= cd.getYear()+"-"+(cd.getMonthOfYear()<10?"0"+cd.getMonthOfYear():cd.getMonthOfYear())+"-"+(cd.getDayOfMonth()<10?"0"+cd.getDayOfMonth():cd.getDayOfMonth());
		}
		else if(dateformat.contains("/")){
			close_date = (cd.getDayOfMonth()<10?"0"+cd.getDayOfMonth():cd.getDayOfMonth())+"/"+(cd.getMonthOfYear()<10?"0"+cd.getMonthOfYear():cd.getMonthOfYear())+"/"+cd.getYear();	
		}
		return close_date;
	}

	public void Eligibilityfields()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//RLOS.mLogger.info("Inside loadPicklist_Address: "); 

		//RLOS.mLogger.info("Grid Data[1][6] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6));
		if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SE").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2)) || (NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_BTC").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2))) || (NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SelfEmployed").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6)))){
			formObject.setVisible("cmplx_EligibilityAndProductInfo_FinalDBR", false);//cmplx_EligibilityAndProductInfo_FinalDBR
			formObject.setVisible("ELigibiltyAndProductInfo_Label3", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label41", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_FinalTAI", false);

			formObject.setVisible("ELigibiltyAndProductInfo_Label5", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_InterestRate", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label6", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_EMI", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label7", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_Tenor", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label8", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_NetPayout", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label2", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_takeoverBank", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label1", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_TakeoverAMount", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label39", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_instrumenttype", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label11", false);

			formObject.setVisible("cmplx_EligibilityAndProductInfo_RepayFreq", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label39", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label39", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label31", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_Moratorium", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label12", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_FirstRepayDate", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label14", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_InterestType", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label15", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_BaseRateType", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label17", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_MArginRate",false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label16",false); 
			formObject.setVisible("cmplx_EligibilityAndProductInfo_BAseRate",false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_ProdPrefRate",false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_ageAtMaturity",false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_NumberOfInstallment",false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_LPF",false);	
			formObject.setVisible("cmplx_EligibilityAndProductInfo_LPFAmount",false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_Insurance",false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_InsuranceAmount",false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label18",false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label23", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_FinalInterestRate", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label13", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_MaturityDate", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label30",false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label32",false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label19",false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label20",false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label21",false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label22",false);

			formObject.setTop("ELigibiltyAndProductInfo_Save", 290);
			formObject.setLeft("cmplx_EligibilityAndProductInfo_FinalLimit", 16);
			formObject.setLeft("ELigibiltyAndProductInfo_Label4", 16);

			//RLOS.mLogger.info("Grid Data[1][6] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6));

		}
		else if((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Salaried").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6)) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Pensioner").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6)) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Salariedpensioner").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6)) ) && !NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_IM").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2))){
			if(!(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_PersonalLoan").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1)))){
				formObject.setVisible("ELigibiltyAndProductInfo_Label5", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_InterestRate", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label6", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_EMI", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label7", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_Tenor", false);


				formObject.setVisible("ELigibiltyAndProductInfo_Label8", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_NetPayout", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label2", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_takeoverBank", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label1", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_TakeoverAMount", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label39", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_instrumenttype", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label11", false);

				formObject.setVisible("ELigibiltyAndProductInfo_Label11", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_RepayFreq", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label39", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label39", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label31", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_Moratorium", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label12", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_FirstRepayDate", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label14", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_InterestType", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label15", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_BaseRateType", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label17", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_MArginRate",false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label16",false); 
				formObject.setVisible("cmplx_EligibilityAndProductInfo_BAseRate",false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_ProdPrefRate",false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_ageAtMaturity",false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_NumberOfInstallment",false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_LPF",false);	
				formObject.setVisible("cmplx_EligibilityAndProductInfo_LPFAmount",false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_Insurance",false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_InsuranceAmount",false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label18",false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label23", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_FinalInterestRate", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label13", false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_MaturityDate", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label30",false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label32",false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label19",false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label20",false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label21",false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label22",false);

				formObject.setVisible("cmplx_EligibilityAndProductInfo_NetRate", false);
				formObject.setTop("ELigibiltyAndProductInfo_Save", 240);
			}
			else{
				if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 4).contains("TKO")){
					formObject.setVisible("ELigibiltyAndProductInfo_Label8",false);
					formObject.setVisible("cmplx_EligibilityAndProductInfo_NetPayout",false);
					formObject.setVisible("ELigibiltyAndProductInfo_Label2",true);
					formObject.setVisible("cmplx_EligibilityAndProductInfo_takeoverBank",true);
					formObject.setVisible("ELigibiltyAndProductInfo_Label1",true);
					formObject.setVisible("cmplx_EligibilityAndProductInfo_TakeoverAMount",true);
				}
				else if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 4).contains("TOP")){
					formObject.setVisible("ELigibiltyAndProductInfo_Label2",false);
					formObject.setVisible("cmplx_EligibilityAndProductInfo_takeoverBank",false);
					formObject.setVisible("ELigibiltyAndProductInfo_Label1",false);
					formObject.setVisible("cmplx_EligibilityAndProductInfo_TakeoverAMount",false);
					formObject.setVisible("ELigibiltyAndProductInfo_Label8",true);
					formObject.setVisible("cmplx_EligibilityAndProductInfo_NetPayout",true);
				}
				else{
					formObject.setVisible("ELigibiltyAndProductInfo_Label8",false);
					formObject.setVisible("cmplx_EligibilityAndProductInfo_NetPayout",false);
					formObject.setVisible("ELigibiltyAndProductInfo_Label2",false);
					formObject.setVisible("cmplx_EligibilityAndProductInfo_takeoverBank",false);
					formObject.setVisible("ELigibiltyAndProductInfo_Label1",false);
					formObject.setVisible("cmplx_EligibilityAndProductInfo_TakeoverAMount",false);
				}
			}


			//formObject.setLeft("cmplx_EligibilityAndProductInfo_FinalLimit", 16);
			//formObject.setLeft("ELigibiltyAndProductInfo_Label5", 16);



		}
		else{
			formObject.setVisible("ELigibiltyAndProductInfo_Label8", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_NetPayout", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label39", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_instrumenttype", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_Moratorium", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label31", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label15", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_BaseRateType", false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label17", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_MArginRate",false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label16",false); 
			formObject.setVisible("cmplx_EligibilityAndProductInfo_BAseRate",false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_ProdPrefRate",false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label18",false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_ageAtMaturity",false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_NumberOfInstallment",false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_LPF",false);	
			formObject.setVisible("cmplx_EligibilityAndProductInfo_LPFAmount",false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_Insurance",false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_InsuranceAmount",false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label30",false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label32",false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label19",false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label20",false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label21",false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label22",false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label2",false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_takeoverBank",false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label1",false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_TakeoverAMount",false);
			formObject.setVisible("ELigibiltyAndProductInfo_Label14", false);
			formObject.setVisible("cmplx_EligibilityAndProductInfo_InterestType", false);

		}

	}

	public void loadPicklistCustomer()  

	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//RLOS.mLogger.info("Inside loadPicklistCustomer: ");
		LoadPickList("cmplx_Customer_Nationality", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
		LoadPickList("cmplx_Customer_Title", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_title with (nolock) order by code");
		//LoadPickList("cmplx_Customer_SecNationality", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
		LoadPickList("cmplx_Customer_NEP", " select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from ng_MASTER_NEPType with(nolock) order by Code");
		LoadPickList("cmplx_Customer_CustomerCategory", " select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_CustomerCategory with(nolock) order by Code");
		LoadPickList("cmplx_Customer_COuntryOFResidence", " select convert(varchar, Description),code from ng_MASTER_Country with(nolock) order by Code");	
		LoadPickList("cmplx_Customer_MAritalStatus", "select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_Maritalstatus with(nolock) order by Code");
		LoadPickList("cmplx_Customer_EmirateOfResidence", "select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_emirate with(nolock) order by Code");
		LoadPickList("cmplx_Customer_EMirateOfVisa", "select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_emirate with(nolock) order by Code");
		
		

	}

	public void setcustomer_enable(){
		//RLOS.mLogger.info("Inside setCustomer enable function");
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
		formObject.setLocked("cmplx_Customer_Designation", false);
		formObject.setEnabled("Designation_button1", true);
		formObject.setLocked("cmplx_Customer_MAritalStatus", false);
		formObject.setLocked("cmplx_Customer_COuntryOFResidence", false);
		formObject.setLocked("cmplx_Customer_VIsaExpiry",false);
		//added to make the textbox editable
		formObject.setLocked("cmplx_Customer_VisaIssuedate",false);
		formObject.setLocked("cmplx_Customer_PassportIssueDate",false);
		//below code added by nikhil for PCSP-255
		formObject.setLocked("cmplx_Customer_PassPortExpiry",false);
		//added to make the textbox editable
		formObject.setEnabled("cmplx_Customer_SecNAtionApplicable", true);
		formObject.setEnabled("cmplx_Customer_IdIssueDate", true);
		//formObject.setLocked("cmplx_Customer_IdIssueDate", false);
		formObject.setEnabled("cmplx_Customer_EmirateIDExpiry", true);
		//formObject.setLocked("cmplx_Customer_EmirateIDExpiry", false);
		//changed by nikhil PCSP-201
		if("No".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_SecNAtionApplicable"))){
			formObject.setLocked("cmplx_Customer_SecNationality", true);
			formObject.setLocked("SecNationality_Button", true);
		}
		else{
			formObject.setLocked("cmplx_Customer_SecNationality", true);
			formObject.setLocked("SecNationality_Button", false);	
		}
		if("AE".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_Nationality")))
		{
		 formObject.setNGValue("cmplx_Customer_ResidentNonResident", "Y");
		}
		formObject.setLocked("cmplx_Customer_EMirateOfVisa", false);
		formObject.setLocked("cmplx_Customer_EmirateOfResidence", false);
		formObject.setLocked("cmplx_Customer_yearsInUAE", false);
		formObject.setLocked("cmplx_Customer_CustomerCategory", false);
		//formObject.setLocked("cmplx_Customer_GCCNational", false);
		formObject.setEnabled("cmplx_Customer_VIPFlag", true);
		//formObject.setLocked("cmplx_Customer_PassPortExpiry", false);
		//formObject.setLocked("cmplx_Customer_VIsaExpiry", false);
		formObject.setEnabled("Send_OTP_Btn",true);

		//added By Akshay

		String  GCC="BH,IQ,KW,OM,QA,SA,AE";
		if(GCC.contains(formObject.getNGValue("cmplx_Customer_Nationality")))
		{
			//RLOS.mLogger.info("RLOS val change Inside GCC for Nationality");
			formObject.setNGValue("cmplx_Customer_GCCNational","Y");
		}
		else

		{
			formObject.setNGValue("cmplx_Customer_GCCNational","N");

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
		//RLOS.mLogger.info("Inside loadPicklistProduct$"+ReqProd+"$");
		String ProdType=formObject.getNGValue("Product_type");
//CardProd
		if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_PersonalLoan").equalsIgnoreCase(ReqProd)){

			//RLOS.mLogger.info("Inside equalsIgnoreCase()"); 
			formObject.setVisible("Scheme", true);
			formObject.setLeft("Product_Label3", 555);
			formObject.setLeft("Scheme", 555);
			formObject.clear("SubProd");
			formObject.setNGValue("SubProd", "--Select--");
			formObject.clear("Scheme");
			formObject.setNGValue("Scheme",0);
			formObject.setNGValue("CardProd","");
			LoadPickList("SubProd", " select '--Select--' as description,'' as code union select convert(varchar(50),description),code  from ng_MASTER_SubProduct_PL with(nolock) where WorkstepName = '"+formObject.getWFActivityName()+"' and isActive='Y'");
			//LoadPickList("SubProd", "select '--Select--' union select convert(varchar(50),description) from ng_MASTER_SubProduct_PL with (nolock) where workstepName='"+formObject.getWFActivityName()+"'");
			LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar,SchemeDesc),SCHEMEID from ng_MASTER_Scheme with (nolock) where SchemeDesc like 'P_%' order by SCHEMEID");
			//added by abhishek
			//RLOS.mLogger.info("Inside equalsIgnoreCase()"+ReqProd);
			LoadPickList("EmpType", " select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_PRD_EMPTYPE with(nolock) where SubProduct = '"+ReqProd+"' and IsActive='Y' order by code");

		}//subProd
		else if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard").equalsIgnoreCase(ReqProd)){

			formObject.setVisible("CardProd", true);
			formObject.clear("CardProd");
			formObject.clear("SubProd");
			formObject.setNGValue("SubProd", "--Select--");

			if("Telesales_Init".equalsIgnoreCase(formObject.getWFActivityName())){
				LoadPickList("SubProd", " select '--Select--' as description,'' as code union select convert(varchar(50),description),code  from ng_master_Subproduct_CC with(nolock) where code='"+NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_BTC")+"' or code='"+NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_IM")+"' and  IsActive='Y' order by code");
			}
			else{
				LoadPickList("SubProd", " select '--Select--' as description,'' as code union select  convert(varchar,description),code from ng_master_Subproduct_CC with (nolock) where  IsActive='Y' order by code");
			}
			//added by abhishek
			//RLOS.mLogger.info("Inside equalsIgnoreCase()"+ReqProd);
//AppType
			LoadPickList("EmpType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_PRD_EMPTYPE with(nolock) where SubProduct = '"+ReqProd+"'  and IsActive='Y' order by code");
			//Deepak Code change to load card product with new master.
			LoadPickList("CardProd","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_cardProduct with (nolock) where ReqProduct = '"+ProdType+"' and  IsActive='Y' order by code");

			/*if("Islamic".equalsIgnoreCase(ProdType))

				LoadPickList("CardProd", "select '--Select--','' as code union select convert(varchar,description),code from ng_MASTER_CardProduct with (nolock) where code like '%AMAL%' order by code");
			else
				LoadPickList("CardProd", "select '--Select--','' as code union select convert(varchar,description),code from ng_MASTER_CardProduct with (nolock) where code NOT like '%AMAL%' order by code");
			 */
			formObject.setVisible("Scheme", false);
		}
		else{
			formObject.clear("SubProd");
			formObject.setNGValue("SubProd", "--Select--");
		}
	}

	public void loadPicklist_Address()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//RLOS.mLogger.info( "Inside loadPicklist_Address: "); 
		//	LoadPickList("addtype", "select '--Select--' as description  union select convert(varchar, description)  from NG_MASTER_AddressType with (nolock) order by description desc");
		LoadPickList("addtype", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_AddressType with (nolock)  where isActive='Y' order by code");
		//below both query changed by nikhil 
		/*LoadPickList("city", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_city with (nolock)  where isActive='Y' order by description asc");
		LoadPickList("state", "select  '--Select--' as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_state  with (nolock) order by Description asc");
		 *///LoadPickList("city", "select '--Select--','' as code union select convert(varchar, description),code from NG_MASTER_city with (nolock) order by code");
		//LoadPickList("state", "select '--Select--','' as code union select convert(varchar, description),code from NG_MASTER_state with (nolock) order by code");
		//LoadPickList("country", "select '--Select--'as description,'' as code  union all select convert(varchar, description),code from ng_MASTER_Country with (nolock)  where isActive='Y' order by description asc");
		LoadPickList("ResidenceAddrType", "select '--Select--' as description,'' as code union select  convert(varchar, description),code from NG_MASTER_ResidAddressType with (nolock)  where isActive='Y' order by code");	
		//LoadPickList("Customer_Type","SELECT '--Select--' AS customername UNION ALL  SELECT 'P-'+CAST(customername AS VARCHAR(200))  FROM NG_CC_EXTTABLE where cc_wi_name='"+formObject.getWFWorkitemName()+"'   UNION ALL SELECT 'G-'+firstname+' '+lastname FROM ng_RLOS_GR_GuarantorDetails where guarandet_wi_name='"+formObject.getWFWorkitemName()+"'  union ALL SELECT 'S-'+FistName+' '+lastname FROM ng_RLOS_GR_SupplementCardDetails WHERE  supplement_WIname='"+formObject.getWFWorkitemName()+"'");
		LoadPickListSource("Customer_Type","SELECT * FROM (SELECT '--Select--' AS customername UNION ALL  SELECT 'P-'+CAST(FirstName+' '+LAstName AS VARCHAR(200))  FROM ng_RLOS_Customer with(nolock) where wi_name='"+formObject.getWFWorkitemName()+"'   UNION ALL SELECT 'G-'+firstname+' '+lastname as customername FROM ng_RLOS_GR_GuarantorDetails with(nolock) where guarandet_wi_name='"+formObject.getWFWorkitemName()+"' and firstname IS NOT NULL  union ALL SELECT distinct 'S-'+FistName+' '+lastname+'-'+passportNo as customername FROM ng_RLOS_GR_SupplementCardDetails with(nolock) WHERE  supplement_WIname='"+formObject.getWFWorkitemName()+"') as u where customername is not null order by case when customername = '--Select--' then 0 else 1 end");
	}
	//Added by Prabhakar drop-4 point-3
	public void loadPicklist_Fatca()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		LoadPickList("cmplx_FATCA_Category", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_category with (nolock) order by code");
		LoadPickList("FATCA_USRelaton", " select '--Select--' as description, '' as code  union select convert(varchar,description),code  from ng_master_usrelation with(nolock) order by code");
		LoadPickListSource("cmplx_FATCA_CustomerType","SELECT * FROM (SELECT '--Select--' AS customername UNION ALL  SELECT 'P-'+CAST(FirstName+' '+LAstName AS VARCHAR(200))  FROM ng_RLOS_Customer with(nolock) where wi_name='"+formObject.getWFWorkitemName()+"'   UNION ALL SELECT 'G-'+firstname+' '+lastname as customername FROM ng_RLOS_GR_GuarantorDetails with(nolock) where guarandet_wi_name='"+formObject.getWFWorkitemName()+"' and firstname IS NOT NULL  union ALL SELECT distinct 'S-'+FistName+' '+lastname+'-'+passportNo as customername FROM ng_RLOS_GR_SupplementCardDetails with(nolock) WHERE  supplement_WIname='"+formObject.getWFWorkitemName()+"') as u where customername is not null order by case when customername = '--Select--' then 0 else 1 end");
		try{
		//Map<String,String> ReasonMap =getFatcaReasons();
		//List<String> selectedReasonList= new ArrayList<String>();
		//formObject.clear("cmplx_FATCA_ListedReason");
		LoadPickList("cmplx_FATCA_ListedReason", "Select description,code from ng_master_fatcaReasons");

		/*for (String reason : ReasonMap.values()) {
			//selectedReasonList.add(reason);
			formObject.addItem("cmplx_FATCA_ListedReason", reason);
		}*/
		}catch(Exception e){
			RLOS.mLogger.info("Exception in loadPicklist_Fatca"+printException(e));	
		}
		//RLOS.mLogger.info("inside Fatca PickList");	
		}
	//Added by Prabhakar drop-4 point-3
	public void loadPicklist_Kyc()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		LoadPickListSource("KYC_CustomerType","SELECT * FROM (SELECT '--Select--' AS customername UNION ALL  SELECT 'P-'+CAST(FirstName+' '+LAstName AS VARCHAR(200))  FROM ng_RLOS_Customer with(nolock) where wi_name='"+formObject.getWFWorkitemName()+"'   UNION ALL SELECT 'G-'+firstname+' '+lastname as customername FROM ng_RLOS_GR_GuarantorDetails with(nolock) where guarandet_wi_name='"+formObject.getWFWorkitemName()+"' and firstname IS NOT NULL  union ALL SELECT distinct 'S-'+FistName+' '+lastname+'-'+passportNo as customername FROM ng_RLOS_GR_SupplementCardDetails with(nolock) WHERE  supplement_WIname='"+formObject.getWFWorkitemName()+"') as u where customername is not null order by case when customername = '--Select--' then 0 else 1 end");
		//RLOS.mLogger.info("inside KYC PickList");
	}
	//Added by Prabhakar drop-4 point-3
	public void loadPicklist_OECD()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		LoadPickListSource("OECD_CustomerType","SELECT * FROM (SELECT '--Select--' AS customername UNION ALL  SELECT 'P-'+CAST(FirstName+' '+LAstName AS VARCHAR(200))  FROM ng_RLOS_Customer with(nolock) where wi_name='"+formObject.getWFWorkitemName()+"'   UNION ALL SELECT 'G-'+firstname+' '+lastname as customername FROM ng_RLOS_GR_GuarantorDetails with(nolock) where guarandet_wi_name='"+formObject.getWFWorkitemName()+"' and firstname IS NOT NULL  union ALL SELECT distinct 'S-'+FistName+' '+lastname+'-'+passportNo as customername FROM ng_RLOS_GR_SupplementCardDetails with(nolock) WHERE  supplement_WIname='"+formObject.getWFWorkitemName()+"') as u where customername is not null order by case when customername = '--Select--' then 0 else 1 end");
		LoadPickList("OECD_CountryBirth", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_Country with (nolock) order by code");
		//LoadPickList("OECD_townBirth", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_city with (nolock) order by code");
		LoadPickList("OECD_CountryTaxResidence", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_Country with (nolock) order by code");
		LoadPickList("OECD_CRSFlagReason", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_CRSReason with (nolock) order by code");
		//RLOS.mLogger.info("inside OECD PickList");
	}
	// above code Added by Prabhakar
	public void loadPicklist_AuthSign()
	{
		//RLOS.mLogger.info( "Inside loadPicklist_AuthSign: "); 
		//	LoadPickList("addtype", "select '--Select--' as description  union select convert(varchar, description)  from NG_MASTER_AddressType with (nolock) order by description desc");
		LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_Country with (nolock) order by code");
		LoadPickList("SignStatus", "select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock) where isactive='Y'");
		LoadPickList("Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
		LoadPickList("DesignationAsPerVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
	}

	public void loadPicklist_ServiceRequest()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//RLOS.mLogger.info("Inside loadPicklist_ServiceRequest: "); 
		LoadPickList("transtype", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_TransactionType with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("transferMode", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_TransferMode with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("dispatchChannel", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_DispatchChannel with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("marketingCode", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_MarketingCode with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("sourceCode", "select Branch , SOL_ID,0 as sno from NG_MASTER_SourceCode with(nolock) where userid = '"+formObject.getNGValue("lbl_user_name_val")+"'  union select distinct Branch,SOL_ID,1 as sno from NG_MASTER_SourceCode with(nolock) where Branch !='' and SOL_ID !='' order by sno");
		LoadPickList("AppStatus", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_ApplicationStatus with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("approvalCode", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_ApprovalCode with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("chequeStatus", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_MChequeStatus with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("cmplx_CC_Loan_DDSMode", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_DDSMode with (nolock) where IsActive = 'Y' order by code ");
		LoadPickList("cmplx_CC_Loan_AccType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_AccountType with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("cmplx_CC_Loan_DDSBankAName", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_BankName with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("cmplx_CC_Loan_ModeOfSI", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_ModeOfSI with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("cmplx_CC_Loan_VPSPAckage", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_VPSPackage with (nolock) where IsActive = 'Y' order by code");
		//LoadPickList("cmplx_CC_Loan_VPSSourceCode", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_sourceCode with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("cmplx_CC_Loan_StartMonth", "select description,code FROM (SELECT '--Select--' as description,'' as code,'' as sno union select description,code,sno from NG_MASTER_Month with (nolock) where IsActive = 'Y') new_table order by sno");
		LoadPickList("cmplx_CC_Loan_HoldType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_HoldType with (nolock) where IsActive = 'Y' order by code");
		LoadPickList("cmplx_CC_Loan_VPSSourceCode", "select Branch , SOL_ID,0 as sno from NG_MASTER_SourceCode with(nolock) where userid = '"+formObject.getNGValue("lbl_user_name_val")+"'  union select distinct Branch,SOL_ID,1 as sno from NG_MASTER_SourceCode with(nolock) where Branch !='' and SOL_ID !='' order by sno");
		//added by akshay on 8/12/17
		LoadPickList("bankName", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_bankname with (nolock) where IsActive = 'Y' order by code");
	}

	public boolean product_Add_Validate()
	{
		//RLOS.mLogger.info( "Inside product_Add_Validate");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String priority=formObject.getNGValue("Priority");
		String ReqProd=formObject.getNGValue("ReqProd");
		String EmpType=formObject.getNGValue("EmpType");
		formObject.getNGValue("Product_type");
		formObject.getNGValue("AppType");
		formObject.getNGValue("ReqTenor");
		formObject.getNGValue("ReqLimit");

		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(n>0)
		{
			for(int i=0;i<n;i++)
			{
				String grid_EmpType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,6);
				String grid_Priority=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9);

				if(!grid_EmpType.equalsIgnoreCase(EmpType) && !"--Select--".equalsIgnoreCase(grid_EmpType) && !"--Select--".equalsIgnoreCase(EmpType))
					throw new ValidatorException(new FacesMessage("Cannot add two different Employment Types ","EmpType"));

				String grid_Prod=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1);
				//RLOS.mLogger.info("Value of Grid_prod:"+grid_Prod);

				if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_PersonalLoan").equalsIgnoreCase(grid_Prod) && grid_Prod.equalsIgnoreCase(ReqProd))
					throw new ValidatorException(new FacesMessage("Cannot add 2 Personal Loans ","ReqProd"));


				if("Primary".equalsIgnoreCase(grid_Priority) && grid_Priority.equalsIgnoreCase(priority))
					throw new ValidatorException(new FacesMessage("Cannot add 2 primary products ","Priority"));

			}
		}


		return true;
	}

	public boolean product_Modify_Validate()
	{
		//RLOS.mLogger.info( "product_Modify_Validate product_Validate");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String priority=formObject.getNGValue("Priority");
		String ReqProd=formObject.getNGValue("ReqProd");
		String EmpType=formObject.getNGValue("EmpType");
		String TypeProd=formObject.getNGValue("Product_type");
		String appType=formObject.getNGValue("AppType");
		formObject.getNGValue("ReqTenor");
		String ReqLimit=formObject.getNGValue("ReqLimit");

		boolean isEmptyTypeAndReqProd = "".equalsIgnoreCase(TypeProd)||"--Select--".equalsIgnoreCase(ReqProd)|| "".equalsIgnoreCase(ReqProd);
		boolean isEmptyAppAndEmpType =  "".equalsIgnoreCase(appType)||"--Select--".equalsIgnoreCase(EmpType) ||"".equalsIgnoreCase(EmpType);
		boolean isEmptyLimitAndPriority =  "".equalsIgnoreCase(ReqLimit) || "".equalsIgnoreCase(priority);

		if(isEmptyTypeAndReqProd || isEmptyAppAndEmpType || isEmptyLimitAndPriority)
		{	
			throw new ValidatorException(new FacesMessage("All fields are mandatory "));
		}

		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(n>1)
		{
			for(int i=0;i<n;i++)
			{
				String grid_EmpType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,6);
				String grid_Priority=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9);

				if(!grid_EmpType.equalsIgnoreCase(EmpType) && !"--Select--".equalsIgnoreCase(grid_EmpType) && !"--Select--".equalsIgnoreCase(EmpType))
					throw new ValidatorException(new FacesMessage("Cannot add two different Employment Types ","EmpType"));
				//RLOS.mLogger.info("Value of Selected index: "+formObject.getSelectedIndex("cmplx_Product_cmplx_ProductGrid"));
				String grid_Prod=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1);
				//RLOS.mLogger.info("Value of Grid_prod:"+grid_Prod);
				if("PL".equalsIgnoreCase(grid_Prod) && grid_Prod.equalsIgnoreCase(ReqProd)&& !formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",formObject.getSelectedIndex("cmplx_Product_cmplx_ProductGrid"),1).equalsIgnoreCase(ReqProd))
					throw new ValidatorException(new FacesMessage("Cannot add 2 PL ","ReqProd"));

				if("Primary".equalsIgnoreCase(grid_Priority) && grid_Priority.equalsIgnoreCase(priority))
					throw new ValidatorException(new FacesMessage("Cannot add 2 primary products ","Priority"));

			}
		}



		return 	true;
	}


	public void  setALOCListed(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//RLOS.mLogger.info( "Inside setALOCListed()");
		String NewEmployer=formObject.getNGValue("cmplx_EmploymentDetails_Others");
		String IncInCC=formObject.getNGValue("cmplx_EmploymentDetails_IncInCC");
		String INcInPL=formObject.getNGValue("cmplx_EmploymentDetails_IncInPL");
		String subprod=formObject.getNGValue("PrimaryProduct");


		if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_PersonalLoan").equalsIgnoreCase(subprod) && "true".equalsIgnoreCase(NewEmployer) && ("false".equalsIgnoreCase(IncInCC) || "false".equalsIgnoreCase(INcInPL)))
		{
			formObject.setNGValue("cmplx_EmploymentDetails_EmpStatusPL", "CN");
			formObject.setNGValue("cmplx_EmploymentDetails_EmpStatusCC", "CN");
			formObject.setLocked("cmplx_EmploymentDetails_EmpStatusCC", true);
		}


		else if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard").equalsIgnoreCase(subprod) && "true".equalsIgnoreCase(NewEmployer) && ("false".equalsIgnoreCase(IncInCC) || "false".equalsIgnoreCase(INcInPL)))
		{

			formObject.setNGValue("cmplx_EmploymentDetails_EmpStatusPL", "CN");
			formObject.setNGValue("cmplx_EmploymentDetails_EmpStatusCC", "CN");
			formObject.setLocked("cmplx_EmploymentDetails_EmpStatusPL", true);
		}


		else{
			if("true".equalsIgnoreCase(NewEmployer)){
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
		//RLOS.mLogger.info( "Inside Addess_Validate");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String AddType=formObject.getNGValue("addtype");
		int n=formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
		if(n>0)
		{
			for(int i=0;i<n;i++){
				String mystring=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,0);
				//RLOS.mLogger.info("mystring:$"+mystring+"$");

				if("Permanent".equalsIgnoreCase(mystring) && AddType.equalsIgnoreCase(mystring)){
					//RLOS.mLogger.info("inside validator");
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
		String query;
		//added by akshay on 17/1/18 for resch case
		//below both query modified by nikhil .pls merge
		if("Reschedulment".equalsIgnoreCase(formObject.getNGValue("initiationChannel"))){
			//query="SELECT '--Select--' union all select decision FROM NG_MASTER_Decision with(nolock) WHERE ProcessName = 'RLOS' AND Initiation_Type = 'Reschedulment'";
			query="select decision from (select '--Select--' as decision union all select  Decision  from NG_MASTER_Decision with (nolock) where ProcessName='RLOS' and Initiation_Type = 'Reschedulment') as new   order by (case when decision='--Select--' then 0 else 1 end),decision";

		}
		else{
			//query changed by akshay for proc 10348
			query="select decision from (select '--Select--' as decision union all select  Decision  from NG_MASTER_Decision with (nolock) where ProcessName='RLOS' and workstepName='"+formObject.getWFActivityName()+"' AND Initiation_Type = '') as new   order by (case when decision='--Select--' then 0 else 1 end),decision";
			//query="select '--Select--' union all select  Decision from NG_MASTER_Decision with (nolock) where ProcessName='RLOS' and workstepName='"+formObject.getWFActivityName()+"' AND Initiation_Type = ''";
		}
		RLOS.mLogger.info( "Inside loadpicklist3: Query is:  "+query); 
		LoadPickList("cmplx_DecisionHistory_Decision", query);
		//LoadPickList("DecisionHistory_DecisionReasonCode", "select '--Select--' as description,'' as code union all select  convert(varchar, description),code from ng_MASTER_RejectReasons with (nolock)  where isActive='Y' order by code");
	}

	// Function created by aman to load picklist in altcontact details
	public void loadPicklistAltContDet()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//LoadPickList("AlternateContactDetails_carddispatch", "select '--Select--' as description,'' as code union all select convert(varchar,description),code from NG_MASTER_Dispatch order by code ");// Load picklist added by aman to load the picklist in card dispatch to					
		//Deepak Code change to add requested product filter
		String TypeOfProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0)==null?"":formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,0);
		LoadPickList("AlternateContactDetails_custdomicile", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_sol with (nolock)  where product_type = '"+TypeOfProd+"' order by code");					
	}


	public void employment_details_load(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		loadPicklist_TargetSegmentCode();
		
		//added by akshay for proc 12869
		/*if(!"EMPID".equals(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")))
		{
			formObject.setVisible("EMploymentDetails_Label9",false);
			formObject.setVisible("cmplx_EmploymentDetails_LengthOfBusiness",false);
		}*/
		if ("Personal Loan".equalsIgnoreCase(formObject.getNGValue("PrimaryProduct"))){
			formObject.setVisible("cmplx_EmploymentDetails_Kompass",true);
		}
		else{
			formObject.setVisible("cmplx_EmploymentDetails_Kompass",false);
		}
		if("".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP"))){
			//formObject.setLocked("EMploymentDetails_Label25",true);//Added by Akshay on 9/9/17 for enabling NEP type as per FSD
			formObject.setLocked("cmplx_EmploymentDetails_NepType",true);
		}
		else if("NEWJ".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP")) || "NEWC".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP"))){
			formObject.setNGValue("cmplx_EmploymentDetails_NepType",formObject.getNGValue("cmplx_Customer_NEP"));
		}
		setALOCListed();
		if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard").equalsIgnoreCase(formObject.getNGValue("PrimaryProduct"))){
			formObject.setVisible("EMploymentDetails_Label71", false);
			formObject.setVisible("cmplx_EmploymentDetails_EmpContractType", false);
			formObject.setLocked("cmplx_EmploymentDetails_EmpStatusCC", false);
		}
		else{
			formObject.setVisible("EMploymentDetails_Label71", true);
			formObject.setVisible("cmplx_EmploymentDetails_EmpContractType", true);
			formObject.setLocked("cmplx_EmploymentDetails_EmpStatusPL", false);
		}
		//formObject.setVisible("cmplx_EmploymentDetails_channelcode",false);
		//formObject.setVisible("EMploymentDetails_Label36",false);
		formObject.setEnabled("EMploymentDetails_Button2",false);//added by akshay on 29/4/18 for proc 8441

		loadPicklist4();
		//formObject.setNGValue("cmplx_EmploymentDetails_FreezoneName", "");
		//formObject.setNGValue("cmplx_EmploymentDetails_EmpStatusCC", "");
		Fields_ApplicationType_Employment();
		//RLOS.mLogger.info("before loading employment details");
		//RLOS.mLogger.info("value of other checkbox: "+formObject.getNGValue("cmplx_EmploymentDetails_Others"));
		if(!formObject.getNGValue("cmplx_EmploymentDetails_Others").equalsIgnoreCase("true")){
			//RLOS.mLogger.info("before if");
			//LoadPickList("cmplx_EmploymentDetails_ApplicationCateg", "select '--Select--' as Description,'' as code union select  convert(varchar,description),code from NG_MASTER_ApplicatonCategory with (nolock) where code NOT LIKE '%CN%' order by code");//added By Tarang started on 22/02/2018 as per drop 4 point 20
			//formObject.setEnabled("cmplx_EmploymentDetails_ApplicationCateg", true);//added By Tarang started on 22/02/2018 as per drop 4 point 20
			formObject.setLocked("cmplx_EmploymentDetails_EmpName",true);
			formObject.setLocked("cmplx_EmploymentDetails_EMpCode",true);	
			formObject.setLocked("cmplx_EmploymentDetails_CurrEmployer",true);
			formObject.setLocked("cmplx_EmploymentDetails_EmpIndusSector",true);
			formObject.setLocked("cmplx_EmploymentDetails_Indus_Macro",true);
			formObject.setLocked("cmplx_EmploymentDetails_Indus_Micro",true);
			formObject.setLocked("cmplx_EmploymentDetails_FreezoneName",true);
			formObject.setLocked("FreeZone_Button",true);
			formObject.setLocked("cmplx_EmploymentDetails_Freezone",true);
			formObject.setEnabled("cmplx_EmploymentDetails_IncInPL", false);
			formObject.setEnabled("cmplx_EmploymentDetails_IncInCC", false);
		}
		else{
			//RLOS.mLogger.info("before else");
			//LoadPickList("cmplx_EmploymentDetails_ApplicationCateg", "select '--Select--' as Description,'' as code union select  convert(varchar,description),code from NG_MASTER_ApplicatonCategory with (nolock) order by code");
			//formObject.setNGValue("cmplx_EmploymentDetails_ApplicationCateg","CN");//added By Tarang started on 22/02/2018 as per drop 4 point 20
			//formObject.setEnabled("cmplx_EmploymentDetails_ApplicationCateg",false);//added By Tarang started on 22/02/2018 as per drop 4 point 20
			formObject.setLocked("EMploymentDetails_Text21",true);
			formObject.setLocked("EMploymentDetails_Text22",true);
			formObject.setEnabled("EMploymentDetails_Button2",false);
			formObject.setVisible("cmplx_EmploymentDetails_EMpCode", false);
			formObject.setVisible("EMploymentDetails_Label72", false);

		}

		formObject.setLocked("cmplx_EmploymentDetails_EmpStatusPL",true);
		formObject.setLocked("cmplx_EmploymentDetails_EmpStatusCC",true);
		formObject.setLocked("cmplx_EmploymentDetails_LOS",true);

		//added By Akshay on 12/9/2017 to set this field as CAC if CAC is true
		if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Y").equalsIgnoreCase(formObject.getNGValue("Is_CAC_Checked")))
		{

			formObject.setNGValue("cmplx_EmploymentDetails_targetSegCode", "CAC");
			formObject.setVisible("EMploymentDetails_Label3",true);//added By Tarang started on 28/02/2018 as per drop 4 point 23
			formObject.setVisible("cmplx_EmploymentDetails_OtherBankCAC",true);//added By Tarang started on 28/02/2018 as per drop 4 point 23
			formObject.setVisible("EmploymentDetails_Bank_Button",true);//added By Tarang started on 28/02/2018 as per drop 4 point 23
			
		}
		if("CAC".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")))
		{
			formObject.setVisible("EMploymentDetails_Label3",true);//added By Tarang started on 28/02/2018 as per drop 4 point 23
			formObject.setVisible("cmplx_EmploymentDetails_OtherBankCAC",true);//added By Tarang started on 28/02/2018 as per drop 4 point 23
			formObject.setVisible("EmploymentDetails_Bank_Button",true);//added By Tarang started on 28/02/2018 as per drop 4 point 23
			
		}
		//ended By Akshay on 12/9/2017 to set this field as CAC if CAC is true

		//formObject.setLocked("cmplx_EmploymentDetails_LOSPrevious", false);
		//formObject.setEnabled("cmplx_EmploymentDetails_LOSPrevious", true);
		//code by saurabh on 5th Dec
		String hasEligibilityRun="";
		if(formObject.getNGValue("reEligbility_init_counter").contains(";")){
		hasEligibilityRun = formObject.getNGValue("reEligbility_init_counter").split(";")[1];
		}			
		/*if(hasEligibilityRun.equalsIgnoreCase("Y")){
			formObject.setEnabled("Eligibility_Emp", false);
		}*/
		int product_count=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(product_count>0){
			if (formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1).equalsIgnoreCase("Credit Card")){
				formObject.setVisible("cmplx_EmploymentDetails_Kompass",false);
			}
			else{
				formObject.setVisible("cmplx_EmploymentDetails_Kompass",true);
			}
		}
		if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_Freezone")))
		{
			formObject.setEnabled("cmplx_EmploymentDetails_Freezone", true);
			formObject.setLocked("FreeZone_Button", false);		
			
			
		}
		formObject.setNGValue("cmplx_EmploymentDetails_JobConfirmed", formObject.getNGValue("cmplx_Customer_Confirmed_in_Job"));
		//added by nikhil for Button positioning
		formObject.setTop("Eligibility_Emp", 620);
		formObject.setTop("WorldCheck_fetch", 670);
		if("Y".equalsIgnoreCase(formObject.getNGValue("Is_Principal_Approval")))
		{
			formObject.setLocked("cmplx_EmploymentDetails_EmpIndusSector", false);
		}
		//below code added by nikhil for PCAS-3257
		formObject.setNGValue("cmplx_EmploymentDetails_Designation", formObject.getNGValue("cmplx_Customer_Designation"));
	}
	public void loadPicklist4()    
	{
		//RLOS.mLogger.info( "Inside loadpicklist4:");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		
		//below query changed by saurabh on 19th Oct for JIRA-2125.
		LoadPickList("cmplx_EmploymentDetails_ApplicationCateg", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_ApplicatonCategory with (nolock) order by code");
		//LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' order by code");
		//LoadPickList("cmplx_EmploymentDetails_Designation", "select '--Select--' as description,'' as code union select  convert(varchar, description),code from NG_MASTER_Designation with (nolock)  where isActive='Y' order by code");
		//LoadPickList("cmplx_EmploymentDetails_DesigVisa", "select '--Select--' as description,'' as code union select  convert(varchar, description),code from NG_MASTER_Designation with (nolock)  where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_Emp_Type", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_EmploymentType with (nolock)  where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_EmpStatus", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_EmploymentStatus with (nolock)  where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_EmirateOfWork", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_emirate with (nolock)  where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_HeadOfficeEmirate", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_emirate with (nolock)  where isActive='Y' order by code");
		//LoadPickList("cmplx_EmploymentDetails_tenancntrct", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_emirate with (nolock) where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_EmpIndusSector", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_IndustrySector with (nolock)  where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_EmpContractType", "select '--Select--' as description,'' as code  union select convert(varchar, description),code from NG_MASTER_EmpContractType with (nolock)  where isActive='Y' and EmpStatus='"+formObject.getNGValue("cmplx_EmploymentDetails_EmpStatus")+"' order by code");
		LoadPickList("cmplx_EmploymentDetails_IndusSeg", "select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_IndustrySegment with(nolock) where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_channelcode","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_ChannelCode with(nolock) where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_EmpStatusPL","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_EmployerStatusPL with(nolock) where isActive='Y'  order by code");
		LoadPickList("cmplx_EmploymentDetails_EmpStatusCC","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_EmployerStatusCC with(nolock) where isActive='Y'  order by code");
		//LoadPickList("cmplx_EmploymentDetails_FreezoneName","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_freezoneName where isActive='Y' order by code");
		LoadPickList("cmplx_EmploymentDetails_CurrEmployer","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_EmployerCategory_PL with(nolock) where isActive='Y' order by code"); //Only description will go now, code removed for dectech
		LoadPickList("cmplx_EmploymentDetails_EmployerType","select '--Select--' as description,'' as code union select convert(varchar, Description),code from NG_MASTER_EmployerType with(nolock) where isActive='Y' order by code");
		//LoadPickList("cmplx_EmploymentDetails_Indus_Macro", "Select code,description from (select '--Select--' as description,'' as code union select Description,code from NG_MASTER_EmpIndusMacro with (nolock)) new_table order by case  when description ='--Select--' then 0 else 1 end");
		//LoadPickList("cmplx_EmploymentDetails_Indus_Micro", "Select code,description from (select '--Select--' as description,'' as code union select Description,code from NG_MASTER_EmpIndusMicro with (nolock)) new_table order by case  when description ='--Select--' then 0 else 1 end");

		LoadPickList("cmplx_EmploymentDetails_Indus_Macro", " Select macro from (select '--Select--' as macro union select macro from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where  IsActive='Y') new_table order by case  when macro ='--Select--' then 0 else 1 end");
		LoadPickList("cmplx_EmploymentDetails_Indus_Micro", "Select micro from (select '--Select--' as micro union select micro from NG_MASTER_EmpIndusMacroAndMicro with (nolock) where  IsActive='Y') new_table order by case  when micro ='--Select--' then 0 else 1 end");
		//LoadPickList("cmplx_EmploymentDetails_OtherBankCAC","select '--Select--' as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_othBankCAC where isActive='Y' order by code");
		loadPicklist_TargetSegmentCode();
	}


	public void IMFields_Employment()
	{
		//RLOS.mLogger.info( "Inside IMFields_Employment:"); 

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(n>0){
			for(int i=0;i<n;i++){
				if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_InstantMoney").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2))){
					formObject.setLocked("cmplx_EmploymentDetails_EmpName",false); 
					formObject.setLocked("cmplx_EmploymentDetails_EmpIndus",false); 
					formObject.setLocked("cmplx_EmploymentDetails_LOS",false); 
					formObject.setLocked("cmplx_EmploymentDetails_Designation",false);
					formObject.setLocked("cmplx_EmploymentDetails_DesigVisa",false); 
					formObject.setLocked("Designation_button",false);
					formObject.setLocked("DesignationAsPerVisa_button",false);
					formObject.setLocked("cmplx_EmploymentDetails_JobConfirmed",false); 
					break;
				}
			}
		}
	}

	public void BTCFields_Employment()
	{
		//RLOS.mLogger.info( "Inside BTCFields_Employment:"); 

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(n>0){
			for(int i=0;i<n;i++){
				if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_BusinessTitaniumCard").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2))){
					formObject.setEnabled("cmplx_EmploymentDetails_EmpName",false); 
					formObject.setEnabled("cmplx_EmploymentDetails_EmpIndus",false); 
					formObject.setEnabled("cmplx_EmploymentDetails_Designation",false);
					formObject.setEnabled("cmplx_EmploymentDetails_DesigVisa",false); 
					formObject.setLocked("Designation_button",false);
					formObject.setLocked("DesignationAsPerVisa_button",false);
				
					formObject.setEnabled("cmplx_EmploymentDetails_JobConfirmed",false); 
					formObject.setEnabled("cmplx_IncomeDetails_Accomodation",false); 
					break;
				}
			}	
		}
	}

	public void LimitIncreaseFields_Employment()
	{
		//RLOS.mLogger.info( "Inside LimitIncreaseFields_Employment:"); 

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(n>0){
			for(int i=0;i<n;i++){ 
				if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_LimitIncrease").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2))){
					formObject.setEnabled("cmplx_EmploymentDetails_EmpName",true); 
					formObject.setEnabled("cmplx_EmploymentDetails_EmpIndus",true); 
					formObject.setEnabled("cmplx_EmploymentDetails_LOS",true); 
					formObject.setEnabled("cmplx_EmploymentDetails_Designation",true);
					formObject.setLocked("Designation_button",true);
					formObject.setLocked("DesignationAsPerVisa_button",true);
				
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
		//RLOS.mLogger.info( "Inside ProductUpgrade_Employment:"); 

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(n>0){
			for(int i=0;i<n;i++){ 
				if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_ProductUpgrade").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2))){
					formObject.setEnabled("cmplx_EmploymentDetails_EmpName",true); 
					formObject.setEnabled("cmplx_EmploymentDetails_EmpIndus",true); 
					formObject.setEnabled("cmplx_EmploymentDetails_LOS",true); 
					formObject.setEnabled("cmplx_EmploymentDetails_Designation",true);
					formObject.setEnabled("cmplx_EmploymentDetails_DesigVisa",true); 
					formObject.setLocked("Designation_button",true);
					formObject.setLocked("DesignationAsPerVisa_button",true);
				
					formObject.setEnabled("cmplx_EmploymentDetails_JobConfirmed",true); 
					formObject.setEnabled("cmplx_IncomeDetails_Accomodation",true); 
				}
			}
		}
	}

	public void Fields_ApplicationType_Employment()
	{
		//RLOS.mLogger.info( "Inside Fields_ApplicationType_Employment:"); 

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESC").equalsIgnoreCase(formObject.getNGValue("Application_Type")) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESN").equalsIgnoreCase(formObject.getNGValue("Application_Type")) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESR").equalsIgnoreCase(formObject.getNGValue("Application_Type"))){
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
		//formObject.setLocked("NotepadDetails_ActuserRemarks",true);
		formObject.setTop("NotepadDetails_SaveButton",400);

		formObject.setLocked("NotepadDetails_Modify", true);//added by akshay on 16/10/17 as per point 27 in PL_NTB sheet-User can not modify the row which he added to the grid
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
		int rowCount = formObject.getLVWRowCount("cmplx_NotepadDetails_cmplx_notegrid");
		//RLOS.mLogger.info( "Notepad_add rowCount is:" + rowCount);
		String time = date.getHours()+":"+date.getMinutes();
		//RLOS.mLogger.info( "Notepad_add value to set is:" +  (modifiedDate+" "+time));
		formObject.setNGValue("cmplx_NotepadDetails_cmplx_notegrid",rowCount-1,4, (modifiedDate+" "+time));

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



	public void Notepad_modify(int rowindex){
		// added by abhishek as per RLOS FSD
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
		Date date = new Date();
		String modifiedDate= new SimpleDateFormat("dd/MM/yyyy").format(date);
		// SKLogger_CC.writeLog("CCyash ", "Activity name is:" + sActivityName);
		String time = date.getHours()+":"+date.getMinutes();
		formObject.setNGValue("cmplx_NotepadDetails_cmplx_notegrid",rowindex,4, modifiedDate+" "+time);
		
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
		//RLOS.mLogger.info( "Inside Notepad_grid"); 
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
		Date date = new Date();
		String modifiedDate= new SimpleDateFormat("dd/MM/yyyy").format(date);
		//RLOS.mLogger.info( "modifiedDate :"+modifiedDate); 
		formObject.setNGValue("NotepadDetails_Actusername",formObject.getUserId()+"-"+formObject.getUserName(),false);
		formObject.setNGValue("NotepadDetails_insqueue",sActivityName,false);
		formObject.setNGValue("NotepadDetails_Actdate",modifiedDate,false);								
		if(formObject.getSelectedIndex("cmplx_NotepadDetails_cmplx_notegrid")>-1){
		if(formObject.getSelectedIndex("cmplx_NotepadDetails_cmplx_notegrid")==-1)
		{
			formObject.setLocked("NotepadDetails_notedesc",false);
			formObject.setLocked("NotepadDetails_notecode",false);
			formObject.setLocked("NotepadDetails_notedetails",false);
		}
		else{
		formObject.setLocked("NotepadDetails_notedesc",true);
		formObject.setLocked("NotepadDetails_notecode",true);
		formObject.setLocked("NotepadDetails_notedetails",true);
		}
		
		formObject.setLocked("NotepadDetails_noteDate",true);
		formObject.setLocked("NotepadDetails_Actusername",true);
		formObject.setLocked("NotepadDetails_user",true);
		formObject.setLocked("NotepadDetails_insqueue",true);
		formObject.setLocked("NotepadDetails_Actdate",true);

		//formObject.setLocked("NotepadDetails_inscompletion",false);
		formObject.setLocked("NotepadDetails_ActuserRemarks",false);
		}

	}


	public void IMFields_Income()
	{
		//RLOS.mLogger.info( "Inside IMFields_Income:"); 

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(n>0){
			for(int i=0;i<n;i++){
				if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_InstantMoney").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2))){
					formObject.setLocked("cmplx_IncomeDetails_AvgBal",true); 
					formObject.setLocked("cmplx_IncomeDetails_AvgBalFreq",true); 
					formObject.setLocked("cmplx_IncomeDetails_CredTurnover",true); 
					formObject.setLocked("cmplx_IncomeDetails_CreditTurnoverFreq",true); 
					formObject.setLocked("cmplx_IncomeDetails_NoOfBankStat",true); 
					break;
				}
			}
		}
	}

	public void BTCFields_Income()
	{
		//RLOS.mLogger.info( "Inside BTCFields_Income:"); 

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(n>0){
			for(int i=0;i<n;i++){
				if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_BusinessTitaniumCard").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2)) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_BTCPlus").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,5)) ){
					formObject.setLocked("cmplx_IncomeDetails_BasicSal",true); 
					formObject.setLocked("cmplx_IncomeDetails_GrossSal",true); 
					formObject.setLocked("cmplx_IncomeDetails_NetSal1",true); 
					formObject.setLocked("cmplx_IncomeDetails_TotSal",true);
					formObject.setLocked("cmplx_IncomeDetails_NotAccBonus",true); 
					formObject.setLocked("cmplx_IncomeDetails_NoOfBankStat",true); 
					formObject.setLocked("cmplx_IncomeDetails_Accomodation",true); 
					break;
				}
			}	
		}
	}

	public void LimitIncreaseFields_Income()
	{
		//RLOS.mLogger.info( "Inside LimitIncreaseFields_Income:"); 

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(n>0){
			for(int i=0;i<n;i++){ 
				if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_LimitIncrease").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2))){
					formObject.setLocked("cmplx_IncomeDetails_MonthlyRent",true); 
					formObject.setLocked("cmplx_IncomeDetails_NoOfBankStat",true); 
					break;
				}
			}
		}
	}


	public void ProductUpgrade_Income()
	{
		//RLOS.mLogger.info( "Inside ProductUpgrade_Income:"); 

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(n>0){
			for(int i=0;i<n;i++){ 
				if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_ProductUpgrade").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2))){
					formObject.setLocked("cmplx_IncomeDetails_MonthlyRent",true); 
					formObject.setLocked("cmplx_IncomeDetails_NoOfBankStat",true); 		
				}
			}
		}
	}




	public void Fields_Liabilities()
	{
		//RLOS.mLogger.info( "Inside Fields_Liabilities:"); 
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(n>0){
			for(int i=0;i<n;i++){
				//RLOS.mLogger.info("Grid Data[2][9] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9));
				if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_BusinessTitaniumCard").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2)) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SelfEmployedCreditCard").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2))&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Primary").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9))){

					formObject.setVisible("Button2",false);
					formObject.setVisible("Label9",false);
					formObject.setVisible("Liability_New_Label21",false); 
					formObject.setVisible("Liability_New_Label14",false);
					formObject.setVisible("cmplx_Liability_New_DBR",false);
					formObject.setVisible("cmplx_Liability_New_DBRNet",false); 
					formObject.setVisible("Label15",false);
					formObject.setVisible("cmplx_Liability_New_TAI",false);
					LoadPickList("contractType","Select '--Select--' as Description,'' as code, union select convert(varchar,Description),code from ng_master_contract_type with(nolock) order by code'");
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
		//RLOS.mLogger.info( "Inside Fields_ApplicationType_Eligibility:"); 

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		//added By Akshay on 29/9/17 for point 52---Net payout field not to be visible for new / takeover

		if(formObject.getNGValue("Application_Type").contains("TOP")){
			formObject.setVisible("ELigibiltyAndProductInfo_Label8",false); 
			formObject.setVisible("cmplx_EligibilityAndProductInfo_NetPayout",false); 
			formObject.setLocked("cmplx_EligibilityAndProductInfo_NetPayout",false); 
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
			formObject.setLocked("cmplx_EligibilityAndProductInfo_NetPayout",true); 

			//ended By Akshay on 29/9/17 for point 52---Net payout field not to be visible for new / takeover

		}

	}

	public void  Fields_Eligibility() 
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_PersonalLoan").equalsIgnoreCase(formObject.getNGValue("PrimaryProduct"))){
			//formObject.setVisible("ELigibiltyAndProductInfo_Label39",true);//This field is always true by default
			//formObject.setVisible("cmplx_EligibilityAndProductInfo_instrumenttype",true);




			/*	formObject.setTop("ELigibiltyAndProductInfo_Label39",formObject.getTop("ELigibiltyAndProductInfo_Frame2")+formObject.getHeight("ELigibiltyAndProductInfo_Frame2")+10);
			formObject.setTop("ELigibiltyAndProductInfo_Label4",formObject.getTop("ELigibiltyAndProductInfo_Frame2")+formObject.getHeight("ELigibiltyAndProductInfo_Frame2")+10);
			formObject.setTop("cmplx_EligibilityAndProductInfo_FinalLimit",formObject.getTop("ELigibiltyAndProductInfo_Label4")+16);
			formObject.setTop("cmplx_EligibilityAndProductInfo_instrumenttype",formObject.getTop("ELigibiltyAndProductInfo_Label39")+16);
			 */	// commented by aman because its not working

			formObject.setTop("ELigibiltyAndProductInfo_Label39",73);
			formObject.setTop("ELigibiltyAndProductInfo_Label4",23);
			formObject.setTop("cmplx_EligibilityAndProductInfo_FinalLimit",39);
			formObject.setTop("cmplx_EligibilityAndProductInfo_instrumenttype",89);


			//formObject.setTop("ELigibiltyAndProductInfo_Button1",formObject.getTop("cmplx_EligibilityAndProductInfo_FinalLimit")+16);
			//formObject.setTop("ELigibiltyAndProductInfo_Save",formObject.getTop("cmplx_EligibilityAndProductInfo_FinalLimit")+35);
		}

		else{

			if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_IM").equalsIgnoreCase(formObject.getNGValue("Subproduct_productGrid"))){
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



				formObject.setLeft("ELigibiltyAndProductInfo_Label12",264);
				formObject.setLeft("cmplx_EligibilityAndProductInfo_FirstRepayDate",264);
				formObject.setLeft("cmplx_EligibilityAndProductInfo_InterestType",536);
				formObject.setLeft("ELigibiltyAndProductInfo_Label14",536);
				formObject.setTop("cmplx_EligibilityAndProductInfo_FinalInterestRate",192);
				formObject.setTop("ELigibiltyAndProductInfo_Label23",176);
				formObject.setTop("cmplx_EligibilityAndProductInfo_MaturityDate",192);
				formObject.setTop("ELigibiltyAndProductInfo_Label13",176);


				formObject.setTop("ELigibiltyAndProductInfo_Save",224);
				//formObject.setHeight("ELigibiltyAndProductInfo_Frame1",530);
				//formObject.setHeight("EligibilityAndProductInformation",550);
			}	


			else if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SAL").equalsIgnoreCase(formObject.getNGValue("Subproduct_productGrid"))){
				Eligibility_Hide();
				formObject.setVisible("ELigibiltyAndProductInfo_Label41",true); 
				formObject.setVisible("cmplx_EligibilityAndProductInfo_FinalTAI",true);
				formObject.setTop("ELigibiltyAndProductInfo_Frame7",80);

			}


			else if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_BTC").equalsIgnoreCase(formObject.getNGValue("Subproduct_productGrid"))){


				formObject.setVisible("cmplx_EligibilityAndProductInfo_FinalDBR",false);
				formObject.setVisible("ELigibiltyAndProductInfo_Label3",false);
				Eligibility_Hide();		
				formObject.setLeft("ELigibiltyAndProductInfo_Label4",17);



				formObject.setLeft("cmplx_EligibilityAndProductInfo_FinalLimit",17);
				formObject.setTop("ELigibiltyAndProductInfo_Label4",60);

				formObject.setTop("cmplx_EligibilityAndProductInfo_FinalLimit",75);
				formObject.setTop("ELigibiltyAndProductInfo_Button1",formObject.getTop("cmplx_EligibilityAndProductInfo_FinalLimit")+formObject.getHeight("cmplx_EligibilityAndProductInfo_FinalLimit")+10);
				formObject.setTop("ELigibiltyAndProductInfo_Save",formObject.getTop("ELigibiltyAndProductInfo_Button1")+formObject.getHeight("ELigibiltyAndProductInfo_Button1")+30);


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
		//RLOS.mLogger.info( "Inside Fields_ServiceRequest:"); 
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(n>0){
			for(int i=0;i<n;i++){
				//RLOS.mLogger.info( formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9)); 
				if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_InstantMoney").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2)) || "Salaried Credit Card".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2)) && NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Primary").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9))){
					//RLOS.mLogger.info("hi");

					formObject.setVisible("CC_Loan_Frame3",false); 
					formObject.setVisible("CC_Loan_Frame5",true); 
					break;
				}
				else if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).contains("Business titanium Card") || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SelfEmployedCreditCard").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2)) && NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Primary").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9))){
					//RLOS.mLogger.info("hello");
					formObject.setVisible("CC_Loan_Frame3",false);
					formObject.setVisible("CC_Loan_Frame5",true); 					
					break;
				}
				else if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_LimitIncrease").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2)) && NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Primary").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9))){
					//RLOS.mLogger.info("Bye");

					formObject.setVisible("CC_Loan_Frame5",false); 
					formObject.setVisible("CC_Loan_Frame3",false); 
					break;
				}
				else if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_ProductUpgrade").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2)) && NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Primary").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9))){
					//RLOS.mLogger.info("PU");

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
		//RLOS.mLogger.info( "Inside Eligibility_Hide:"); 
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
		//RLOS.mLogger.info( "Inside Eligibility_UnHide():"); 
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
		//RLOS.mLogger.info( "Inside TypeOfProduct:"); 
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		for(int i=0;i<n;i++)
		{
			if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Primary").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9))){
				String Type_Prod=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,0);
				String req_Prod=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1);
				//RLOS.mLogger.info( "Loan_Type,req_Prod:"+Type_Prod+req_Prod); 
				if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_PersonalLoan").equalsIgnoreCase(req_Prod))

					formObject.setNGValue("PL_LoanType",Type_Prod);
				else if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard").equalsIgnoreCase(req_Prod))

					formObject.setNGValue("CC_LoanType",Type_Prod);
			}
		}
	}	


	public void saveIndecisionGrid(){

		try{
			//RLOS.mLogger.info("Inside adddecisionGrid: "); 

			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			Common_Utils common=new Common_Utils(RLOS.mLogger);
			//SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
			String entry_date = formObject.getNGValue("Entry_Date");
			//Date d = format.parse(entry_date);
			entry_date = common.Convert_dateFormat(entry_date,"dd/MM/yyyy", "MM/dd/yyyy");//format.format(d);
			//RLOS.mLogger.info("after formatting date: "+entry_date); 
			//String currDate=new SimpleDateFormat("MM/dd/yyyy HH:mm").format(Calendar.getInstance().getTime());
			String currDate=common.Convert_dateFormat("","","MM/dd/yyyy HH:mm");

			// EntryDateTime changed by saurabh on 19th September 2017 as this value was passing as null and hence decision history entry was failing.
			String query="insert into ng_rlos_gr_Decision(dateLastChanged, userName, workstepName, Decision, remarks, dec_wi_name,Entry_date,DecisionReasonCode) values('"+currDate+"','"+formObject.getUserName()+"','"+formObject.getWFActivityName()+"','"+formObject.getNGValue("cmplx_DecisionHistory_Decision")+"','"+formObject.getNGValue("cmplx_DecisionHistory_Remarks")+"','"+formObject.getWFWorkitemName()+"','"+entry_date+"','"+formObject.getNGValue("DecisionHistory_DecisionReasonCode")+"')";

			//RLOS.mLogger.info("Query is"+query);
			formObject.saveDataIntoDataSource(query);

		}


		catch(Exception e)
		{
			RLOS.mLogger.info( "Exception Occurred in adddecisionGrid: " + e.getMessage());
		}

	}


	public void AddPrimaryData()
	{
		//RLOS.mLogger.info("RLOSCommon Inside AddPrimaryData() ");

		FormReference formObject=FormContext.getCurrentInstance().getFormReference();
		String LoanType="";
		String ReqProd="";
		String SubProd="";
		String CardProd="";
		String AppType="";


		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		for(int i=0;i<n;i++)	
		{
			if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Primary").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9))){
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
		if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESC").equalsIgnoreCase(AppType) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESR").equalsIgnoreCase(AppType) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESN").equalsIgnoreCase(AppType)){//changed by akshay for rechedulement case on 7/2/18
			//RLOS.mLogger.info("Application type is reschedulement!!!! ");
			formObject.setNGValue("initiationChannel", "Reschedulment");
		}
		//RLOS.mLogger.info("Inside ParentToChild -> n :"+n+"PrimaryData is:"+ReqProd+SubProd+CardProd);

	}


	public void  AddProducts()
	{
		//RLOS.mLogger.info("RLOSCommon Inside AddProducts() ");

		FormReference formObject=FormContext.getCurrentInstance().getFormReference();
		String mystring="";
		String product="";

		try
		{

			int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			for(int i=0;i<n;i++)	
			{
				mystring=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1);
				//RLOS.mLogger.info("Inside AddProducts() mystring:"+mystring);
				if(!product.contains(mystring))
					product+=mystring;			
			}

			formObject.setNGValue("Product_Type", product);
			//RLOS.mLogger.info("Inside AddProducts -> n :"+n+"Product_Type is:"+product);
		}
		catch(Exception e)
		{
			RLOS.mLogger.info("Inside AddProducts ->Exception occurred:"+e.getMessage());
			RLOS.logException(e);

		}
	}



	public void CIFIDCheck(){

		FormReference formObject=FormContext.getCurrentInstance().getFormReference();
		//RLOS.mLogger.info("Inside CIFIDCheck()");
		String query="Select WIname from NG_RLOS_EXTTABLE with (nolock) WHERE Introduction_date BETWEEN DATEADD(minute, -10, getdate()) AND getdate() and cifId = '"+formObject.getNGValue("cmplx_Customer_CIFNO")+"' AND WIname !='"+formObject.getWFWorkitemName()+"' order by Introduction_date desc";
		List<List<String>> list=formObject.getNGDataFromDataCache(query);
		String wiName=list.get(0).get(0);
		//RLOS.mLogger.info("QUERY is:"+query);
		//RLOS.mLogger.info("Data from DB:"+wiName);
		if(wiName!=null && wiName!=""){
			throw new ValidatorException(new FacesMessage("Another instance for this CIFID was created less than 10 mins ago: "+wiName));
			}
	}


	public void populatePickListWindow(String sQuery, String sControlName,String sColName, boolean bBatchingRequired, int iBatchSize,String windowTitle)
	{

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PickList objPickList = formObject.getNGPickList(sControlName, sColName, bBatchingRequired, iBatchSize);

		objPickList.setWindowTitle("Search "+windowTitle);
		
		List<List<String>> result=formObject.getDataFromDataSource(sQuery);
		if (sControlName.equalsIgnoreCase("EMploymentDetails_Button2") || "Customer_search".equalsIgnoreCase(sControlName) || "CompanyDetails_SearchAloc".equalsIgnoreCase(sControlName)){
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

				//RLOS.mLogger.info(result.toString());   

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
				objPickList.addPickListListener(new EventListenerHandler(objPickList.getClientId()));

				//RLOS.mLogger.info("Aman "+result.toString());   

				objPickList.populateData(result);			
			}
		}	

	}

	//code added here
	public String FinancialSummaryXML(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String Employment_type="";
		String requested_subproduct="";

		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		//RLOS.mLogger.info("valu of financial summary"+n);
		if(n>0)
		{
			for(int i=0;i<n;i++)
			{
				requested_subproduct= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2);
				//RLOS.mLogger.info(requested_subproduct);
				Employment_type= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 6);
				//RLOS.mLogger.info(Employment_type);

			}
		}
		//RLOS.mLogger.info(requested_subproduct+","+Employment_type);

		String sQuery_header = "SELECT Operation_name from ng_rlos_Financial_Summary  with (nolock) where Description='"+requested_subproduct+"' and Employment='"+Employment_type+"' ";
		//RLOS.mLogger.info( "Financial_Summary"+sQuery_header);
		List<List<String>> OutputXML_header=formObject.getNGDataFromDataCache(sQuery_header);

		String Operation_name = OutputXML_header.get(0).get(0);
		//RLOS.mLogger.info( "Financial_Summary Operation_name"+Operation_name);
		return Operation_name;
	}
	//code changed here





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




	public static String getTagValue(String xml, String tag)  
	{   
		//RLOS.mLogger.info("Tag:"+tag+" XML:"+xml);
		if(xml.indexOf("<MQ_RESPONSE_XML>")>-1)
		{
			xml = xml.substring(xml.indexOf("<MQ_RESPONSE_XML>")+17,xml.indexOf("</MQ_RESPONSE_XML>"));
		}
		try
		{
			Document doc = getDocument(xml);
			if(doc !=null)
			{
				NodeList nodeList = doc.getElementsByTagName(tag);


				int length = nodeList.getLength();
				//RLOS.mLogger.info("NodeList Length: " + length);


				if (length > 0) {
					Node node =  nodeList.item(0);
					//RLOS.mLogger.info("Node : " + node);
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



			}
		}
		catch(Exception ex)
		{
			RLOS.mLogger.info("Exception occured in getTagValue method");
			RLOS.logException(ex);

		}
		return "";
	}



	public static Document getDocument(String xml) 
	{
		Document doc=null;
		try
		{
			// Step 1: create a DocumentBuilderFactory
			DocumentBuilderFactory dbf =
				DocumentBuilderFactory.newInstance();


			// Step 2: create a DocumentBuilder
			DocumentBuilder db = dbf.newDocumentBuilder();


			// Step 3: parse the input file to get a Document object
			doc = db.parse(new InputSource(new StringReader(xml)));		


		}		
		catch(Exception ex)
		{
			RLOS.logException(ex);
		}
		finally {
			//RLOS.mLogger.info("Inside finally block of getDocument method");

		}
		return doc;
	}  

	public void loadAllCombo_RLOS_Documents_Deferral(){
		//RLOS.mLogger.info( "inside loadAllCombo_RLOS_Documents_Deferral");

		FormContext.getCurrentInstance().getFormConfig().getConfigElement("ProcessName");

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();



		formObject.getNGDataFromDataCache("SELECT DocName FROM ng_rlos_DocTable with (nolock) WHERE ProcessName='RLOS'","DocCategory");
	} 

	public void setMailId(String userName)
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();


		try
		{			
			String squery= "select mailid from pdbuser with (nolock) WHERE UPPER(USERNAME)=UPPER('"+userName+"')";
			List<List<String>> outputindex;
			outputindex = formObject.getNGDataFromDataCache(squery);			

			//RLOS.mLogger.info( "mailID outputItemindex is: " +  outputindex);
			String mailID =outputindex.get(0).get(0);
			//RLOS.mLogger.info( "mailID is:" +  mailID);
			formObject.setNGValue("processby_email",  mailID);

		}
		catch(Exception e)
		{
			RLOS.logException(e);//pgarg

		}
	}

	//new function by Saurabh
	//changed by nikhilon 12 th jan
	public void fetchIncomingDocRepeaterNew(){
		RLOS.mLogger.info( "inside fetchIncomingDocRepeaterNew");
		try {
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();

			String requested_product;
			String requested_subproduct;
			String application_type;
			String product_type;
			product_type= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 0);
			RLOS.mLogger.info(product_type);
			requested_product= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 1);

			RLOS.mLogger.info(requested_product);
			requested_subproduct= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2);
			RLOS.mLogger.info(requested_subproduct);
			application_type= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 4);
			RLOS.mLogger.info(application_type);
			RLOS.mLogger.info(requested_product);

			List<List<String>> docName = null;
			List<String> docType = new ArrayList<String>();
			String documentName = "";
			String documentNameMandatory="";
			String documentNameNonMandatory="";
			String query = "";
			if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_PersonalLoan").equalsIgnoreCase(requested_product)){
				query = "SELECT distinct doctype,mandatory FROM ng_rlos_DocTable WHERE (ProductName = '"+NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_PersonalLoan")+"' and SubProductName = '"+requested_subproduct+"' and Application_Type = '"+application_type+"' and Product_Type = '"+product_type+"' and Active = 'Y') or ProductName='All' order by Mandatory desc";
				RLOS.mLogger.info( "when row count is  zero inside if"+query);
				docName = formObject.getDataFromDataSource(query);
				RLOS.mLogger.info(""+ docName);
			}
			else{
				//Query corrected by Deepak.
				//change in queries by Saurabh on 4th Jan 19.
				String targetsegCode="";
				if("Salaried".equalsIgnoreCase(formObject.getNGValue("EmploymentType"))){
					targetsegCode = formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode");
				}
				else{
					int rowCount = formObject.getLVWRowCount("cmplx_CompanyDetails_cmplx_CompanyGrid");
					for(int i=0;i<rowCount;i++){
						if("Secondary".equalsIgnoreCase(formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",i,2))){
							targetsegCode = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",i,22);
						}
					}
				}
				RLOS.mLogger.info("EmploymentType: "+formObject.getNGValue("EmploymentType") );
				RLOS.mLogger.info("TagertSegmentCode: "+targetsegCode );
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
					query="SELECT distinct doctype,mandatory,Designation FROM ng_rlos_DocTable WHERE ((ProductName = '"+requested_product+"' and SubProductName = '"+requested_subproduct+"') or ProductName='All') and Active = 'Y' and (Designation='CAC' or Designation='All') and (Nationality='"+nationality+"' or Nationality='All') and (minor_flag='"+minor_flag+"' or minor_flag='N') ORDER BY Mandatory desc,DocType";
				}
				else if("DOC".equalsIgnoreCase(targetsegCode)){
					query="SELECT distinct doctype,mandatory,Designation FROM ng_rlos_DocTable WHERE ((ProductName = '"+requested_product+"' and SubProductName = '"+requested_subproduct+"') or ProductName='All') and Active = 'Y' and (Designation='DOC' or Designation='All') and (Nationality='"+nationality+"' or Nationality='All') and (minor_flag='"+minor_flag+"' or minor_flag='N') ORDER BY Mandatory desc,DocType";
				}
				else if("EMPID".equalsIgnoreCase(targetsegCode)){
					query="SELECT distinct doctype,mandatory,Designation FROM ng_rlos_DocTable WHERE ((ProductName = '"+requested_product+"' and SubProductName = '"+requested_subproduct+"') or ProductName='All') and Active = 'Y' and (Designation='EMPID' or Designation='All') and (Nationality='"+nationality+"' or Nationality='All') and (minor_flag='"+minor_flag+"' or minor_flag='N')  ORDER BY Mandatory desc,DocType";
				}
				else if("NEPALO".equalsIgnoreCase(targetsegCode) || "NEPNAL".equalsIgnoreCase(targetsegCode)){
					query="SELECT distinct doctype,mandatory,Designation FROM ng_rlos_DocTable WHERE ((ProductName = '"+requested_product+"' and SubProductName = '"+requested_subproduct+"') or ProductName='All') and Active = 'Y' and (Designation='NEP' or Designation='All') and (Nationality='"+nationality+"' or Nationality='All') and (minor_flag='"+minor_flag+"' or minor_flag='N')  ORDER BY Mandatory desc,DocType";
				}
				else if("VIS".equalsIgnoreCase(targetsegCode)){
					query="SELECT distinct doctype,mandatory,Designation FROM ng_rlos_DocTable WHERE ((ProductName = '"+requested_product+"' and SubProductName = '"+requested_subproduct+"') or ProductName='All') and Active = 'Y' and (Designation='VC' or Designation='All') and (Nationality='"+nationality+"' or Nationality='All') and (minor_flag='"+minor_flag+"' or minor_flag='N')  ORDER BY Mandatory desc,DocType";
				}
				else{
					query="SELECT distinct doctype,mandatory,Designation FROM ng_rlos_DocTable WHERE ((ProductName = '"+requested_product+"' and SubProductName = '"+requested_subproduct+"') or ProductName='All') and Active = 'Y' and (Designation='"+targetsegCode+"' or Designation='All') and (Nationality='"+nationality+"' or Nationality='All') and (minor_flag='"+minor_flag+"' or minor_flag='N')  ORDER BY Mandatory desc,DocType";
				}
			
				
				RLOS.mLogger.info( "when row count is  zero inside else"+query);
				docName = formObject.getDataFromDataSource(query);

				RLOS.mLogger.info(""+ docName);
			}
			
			if(null!=docName && docName.size()>0) {
				for(List<String> row: docName) {
					if("Y".equalsIgnoreCase(row.get(1)) ) {
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
			//change by saurabh for Deferred Until Date.
			if(!"Deferred".equalsIgnoreCase(formObject.getNGValue("IncomingDocNew_Status"))){
				formObject.setLocked("IncomingDocNew_DeferredUntilDate",true);
			}
			formObject.setNGValue("cmplx_IncomingDocNew_MandatoryDocument", documentNameMandatory);
			formObject.setNGValue("cmplx_IncomingDocNew_NonMandatoryDoc", documentNameNonMandatory);
		}catch(Exception ex) {
			RLOS.mLogger.info( "exception in fetchIncomingDocRepeater"+printException(ex));
		}
	}
	
	
	//new code on 11th september
	//tanshu aggarwal for documents(1/06/2017)
	public void fetchIncomingDocRepeater(){

		//RLOS.mLogger.info( "inside fetchIncomingDocRepeater");

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		String requested_product;
		String requested_subproduct;
		String application_type;
		String product_type;
		product_type= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 0);
		//RLOS.mLogger.info(product_type);
		requested_product= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 1);

		//RLOS.mLogger.info(requested_product);
		requested_subproduct= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2);
		//RLOS.mLogger.info(requested_subproduct);
		application_type= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 4);
		//RLOS.mLogger.info(application_type);
		//RLOS.mLogger.info(requested_product);

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
		//added by yash for adding one column in incoming document
		repeaterHeaders.add("DeferredUntil");


		//RLOS.mLogger.info("after making headers");
		FormContext.getCurrentInstance().getFormConfig().getConfigElement("ProcessName");

		List<List<String>> docName = null;

		String documentName = null;
		String documentNameMandatory=null;
		String query = "";
		IRepeater repObj= formObject.getRepeaterControl("IncomingDoc_Frame");
		//RLOS.mLogger.info("after creating the object for repeater");
		int repRowCount = 0;
		//repObj = formObject.getRepeaterControl("IncomingDoc_Frame");
		//RLOS.mLogger.info(""+repObj.toString());
		repObj.setRepeaterHeaders(repeaterHeaders);

		try
		{
			if (repObj.getRepeaterRowCount() == 0) {
				repObj.clear();
				//change in query by saurabh on 26th Dec
				if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_PersonalLoan").equalsIgnoreCase(requested_product)){
					query = "SELECT distinct DocName,Mandatory FROM ng_rlos_DocTable WHERE (ProductName = '"+NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_PersonalLoan")+"' and SubProductName = '"+requested_subproduct+"' and Application_Type = '"+application_type+"' and Product_Type = '"+product_type+"' and Active = 'Y') or ProductName='All' order by Mandatory desc";
					//RLOS.mLogger.info( "when row count is  zero inside if"+query);
					docName = formObject.getDataFromDataSource(query);
					//RLOS.mLogger.info(""+ docName);
				}
				else{
					//Query corrected by Deepak.
					query = "SELECT distinct DocName,Mandatory FROM ng_rlos_DocTable with(nolock) WHERE (ProductName = '"+requested_product+"' and SubProductName = '"+requested_subproduct+"' and Active = 'Y') or ProductName='All' ORDER BY Mandatory desc";
					//RLOS.mLogger.info( "when row count is  zero inside else"+query);
					docName = formObject.getDataFromDataSource(query);
					//RLOS.mLogger.info(""+ docName);
				}

				//added
				for(int i=0;i<docName.size();i++ ){
					repObj.addRow();
					documentName = docName.get(i).get(0);
					documentNameMandatory = docName.get(i).get(1);
					//RLOS.mLogger.info(" "+ documentName);
					//RLOS.mLogger.info(" "+ documentNameMandatory);
					repObj.setValue(i, 0, documentName);
					repObj.setValue(i, 2, documentNameMandatory);
					repRowCount = repObj.getRepeaterRowCount();
					//RLOS.mLogger.info( " " + repRowCount);
				}


				//ended
			}
			else {
				if (repObj.getRepeaterRowCount() != 0) {
					//RLOS.mLogger.info( ""+repObj.getRepeaterRowCount());
					//RLOS.mLogger.info("when row count is not zero");  	
				}
			}
			repObj.setColumnEditable(0, false);
			repObj.setColumnEditable(2, false);
			repObj.setColumnVisible(11, false);
		}
		catch (Exception e) 
		{
			//RLOS.mLogger.info( " " + e.toString());
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

			String query="select FORMAT(datelastchanged,'dd-MM-yyyy hh:mm'),userName,workstepName,Decision,remarks,dec_wi_name,Entry_date,decisionReasonCode from ng_rlos_gr_Decision with (nolock) where dec_wi_name='"+formObject.getWFWorkitemName()+"'";
			//RLOS.mLogger.info("Inside PLCommon ->loadInDecGrid()"+query);
			List<List<String>> list=formObject.getNGDataFromDataCache(query);
			if(!list.isEmpty()){
				//RLOS.mLogger.info("Inside PLCommon ->loadInDecGrid()"+list.get(0).get(0)+list.get(0).get(1)+list.get(0).get(2)+list.get(0).get(3)+list.get(0).get(4));
				/* try{
			 String date=list.get(0).get(0);
			 Date d=new SimpleDateFormat("MM/dd/yyyy HH:mm").parse(date);
			 //RLOS.mLogger.info("value of date is:"+d.toString());*/
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
		}
		catch(Exception e)
		{
			//RLOS.mLogger.info("Exception Occurred loadInDecGrid :"+e.getMessage());
			RLOS.logException(e);
		}	
	}


	//Code commented to handel CIF starting with 0 End
	public void parse_cif_eligibility(String output,String operation_name)
	{
		try
		{

			//RLOS.mLogger.info( "outputresponse is: "+output);
			String outputXML=output.substring(output.indexOf("<MQ_RESPONSE_XML>")+17,output.indexOf("</MQ_RESPONSE_XML>"));
			//;

			Map<Integer, HashMap<String, String> > Cus_details = new HashMap<Integer, HashMap<String, String>>();
			String passport_list = "";
			//Map<String, String> cif_details = new HashMap<String, String>();
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(outputXML)));
			//RLOS.mLogger.info( doc+"");

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
							//RLOS.mLogger.info(tag_name+ " tag value: " +tag_value);
						}

					}
					//changes done to remove internal blacklist without cif
					if(cif_details.containsKey("CustId")){
						Cus_details.put(Integer.parseInt(cif_details.get("CustId")), (HashMap<String, String>) cif_details) ;
					}
				}
			}
			int Prim_cif = primary_cif_identify(Cus_details);
			//RLOS.mLogger.info("Primary Cif Identified: "+Prim_cif);
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
					//RLOS.mLogger.info("Inside Primary Cif not null and if condition: "+Prim_cif);
					Map<String, String> prim_entry;
					prim_entry = Cus_details.get(Prim_cif);
					String primary_pass = prim_entry.get("PassportNum");
					passport_list = passport_list.replace(primary_pass, "");
					//RLOS.mLogger.info(prim_entry.get("CustId"));
					set_nonprimaryPassport(prim_entry.get("CustId")==null?"":prim_entry.get("CustId"),passport_list);//changes done to save CIF without 0.
				}
			}			
		}
		catch(Exception e)
		{
			//RLOS.mLogger.info( e.getMessage());
			RLOS.logException(e);


		}

	}
	public void save_cif_data(Map<Integer, HashMap<String, String>> cusDetails ,int prim_cif){
		try{
			//RLOS.mLogger.info( "inside save_cif_data methos");
			FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
			String WI_Name = formObject.getWFWorkitemName();
			//RLOS.mLogger.info( "inside save_cif_data methos wi_name: "+WI_Name );
			formObject.clear("q_cif_detail");//code added to clear the grid below adding cif details 
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
				if(curr_entry.get("CustId").equalsIgnoreCase(Integer.toString(prim_cif))|| curr_entry.get("CustId").contains(Integer.toString(prim_cif)))
				{
					Cif_data.add("Y");
				}
				else
				{

					Cif_data.add("N");	
				}
				Cif_data.add(WI_Name);
				formObject.addItemFromList("q_cif_detail", Cif_data);
			}
			
		}
		catch(Exception e)
		{
			//RLOS.mLogger.info( e.getMessage());
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
					//Below condition changed to handle the case in which product is not received Deepak 15JAn2018 
					//if(primary_cif==0 && curr_entry.containsKey("Products")){
					if(primary_cif==0 ){
						primary_cif = entry.getKey();
					}
					else if(curr_entry.containsKey("Products"))
					{
						prim_entry = cusDetails.get(primary_cif);
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
			//RLOS.mLogger.info( e.getMessage());
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
			//RLOS.mLogger.info( e.getMessage());
			RLOS.logException(e);
		}

	}







	//incoming doc function

	public void IncomingDoc(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		IRepeater repObj=null;
		repObj = formObject.getRepeaterControl("IncomingDoc_Frame");
		boolean emiratesIdCardAttached = false;
		boolean nationalIdCardAttached = false;
		boolean passportAttached = false;
		String [] finalmisseddoc=new String[70];
		
		int rowRowcount=repObj.getRepeaterRowCount();
		//RLOS.mLogger.info("RLOS Initiation");
		if (repObj.getRepeaterRowCount() != 0) {

			for(int j = 0; j < rowRowcount; j++)
			{
				String DocName=repObj.getValue(j, "cmplx_DocName_DocName");
				//RLOS.mLogger.info("sQuery for document name is: DocName" +  DocName);

				String Mandatory=repObj.getValue(j, "cmplx_DocName_Mandatory");
				//RLOS.mLogger.info("sQuery for document name is: Mandatory" +  Mandatory);

				if("Y".equalsIgnoreCase(Mandatory))
				{
					String DocIndex=repObj.getValue(j,"cmplx_DocName_DocIndex");
					//RLOS.mLogger.info("DocIndex"+DocIndex);
					String StatusValue=repObj.getValue(j,"cmplx_DocName_DocStatus");
					//RLOS.mLogger.info("StatusValue"+StatusValue);
					String Remarks=repObj.getValue(j, "cmplx_DocName_Remarks");
					//RLOS.mLogger.info("Remarks"+Remarks);
					/*//temporary change by saurabh on 29th Dec
					if(!StatusValue.equalsIgnoreCase("Received")){
						RLOS.mLogger.info("StatusValue inside DocIndex recieved");
						finalmisseddoc[j]=DocName;
					}*/

					if(DocIndex.equalsIgnoreCase("")||DocIndex.equalsIgnoreCase(null)||DocIndex.equalsIgnoreCase("null")){
						//RLOS.mLogger.info("StatusValue inside DocIndex"+DocIndex);
						if(StatusValue.equalsIgnoreCase("Received")){
							//RLOS.mLogger.info("StatusValue inside DocIndex recieved");
							//finalmisseddoc[j]=DocName;
						}

						else if(StatusValue.equalsIgnoreCase("Deferred")){
							formObject.setNGValue("is_deferral_approval_require","Y");
							//formObject.RaiseEvent("WFSave");
							//RLOS.mLogger.info("Deferred flag value inside no document"+formObject.getNGValue("is_deferral_approval_require"));
							if(Remarks.equalsIgnoreCase("")||Remarks.equalsIgnoreCase(null)||Remarks.equalsIgnoreCase("null")){
								//RLOS.mLogger.info("As you have not attached the Mandatory Document and the status is Deferred");
								//	throw new ValidatorException(new FacesMessage("As you have Deferred "+DocName+" Document,So kindly fill the Remarks"));
								//throw new ValidatorException(new FacesMessage(NGFUserResourceMgr_RLOS.getAlert("VAL052")));
							}
							else if(!Remarks.equalsIgnoreCase("")||Remarks.equalsIgnoreCase(null)||Remarks.equalsIgnoreCase("null")){
								//RLOS.mLogger.info("Proceed further");
							}
						}
						else if(StatusValue.equalsIgnoreCase("Waived")){
							formObject.setNGValue("is_waiver_approval_require","Y");
							//formObject.RaiseEvent("WFSave");
							//RLOS.mLogger.info("waived flag value inside no document"+formObject.getNGValue("is_waiver_approval_require"));
							if(Remarks.equalsIgnoreCase("")||Remarks.equalsIgnoreCase(null)||Remarks.equalsIgnoreCase("null")){
								//RLOS.mLogger.info("It is Mandatory to fill Remarks As you have not attached the Mandatory Document and the status is Waived");
								//throw new ValidatorException(new FacesMessage(NGFUserResourceMgr_RLOS.getAlert("VAL052")));
							}
							else if(!Remarks.equalsIgnoreCase("")||Remarks.equalsIgnoreCase(null)||Remarks.equalsIgnoreCase("null")){
								//RLOS.mLogger.info("You may proceed further Proceed further");
							}
						}
						else if((StatusValue.equalsIgnoreCase("--Select--"))||(StatusValue.equalsIgnoreCase(""))){
							//RLOS.mLogger.info("StatusValue inside DocIndex is blank");
							//finalmisseddoc[j]=DocName;
						}
						else if((StatusValue.equalsIgnoreCase("Pending"))){
							//RLOS.mLogger.info("StatusValue of doc is Pending");

						}
					}
					else{
						if(!(DocIndex.equalsIgnoreCase(""))){
							if(!StatusValue.equalsIgnoreCase("Received")){
								repObj.setValue(j,"cmplx_DocName_DocStatus","Received");
								repObj.setEditable(j, "cmplx_DocName_DocStatus", false);
								//RLOS.mLogger.info("StatusValue::123final"+StatusValue);
							}
							else {
								if(DocName.equalsIgnoreCase("Emirates_ID_Card")){
									emiratesIdCardAttached = true;
								}
								else if(DocName.equalsIgnoreCase("National_ID_card")){
									nationalIdCardAttached = true;
								}
								else if(DocName.equalsIgnoreCase("Passport")){
									passportAttached = true;
								}
								//RLOS.mLogger.info("StatusValue::123final status is already received");
							}
						}
					}

				}
			}
		}
		StringBuilder mandatoryDocName = new StringBuilder("");

		//RLOS.mLogger.info("length of missed document"+finalmisseddoc.length);
		//RLOS.mLogger.info("length of missed document mandatoryDocName.length"+mandatoryDocName.length());

		for(int k=0;k<finalmisseddoc.length;k++)
		{
			if("AECBconsentform".equalsIgnoreCase(finalmisseddoc[k]) && "true".equals(formObject.getNGValue("cmplx_Liability_New_AECBconsentAvail"))){
				k++;
			}
			else if("Emirates_id_reciept".equalsIgnoreCase(finalmisseddoc[k]) && emiratesIdCardAttached){
				continue;
			}
			else if("National_ID_card".equalsIgnoreCase(finalmisseddoc[k]) && passportAttached){
				continue;
			}
			else if("Passport".equalsIgnoreCase(finalmisseddoc[k]) && nationalIdCardAttached){
				continue;
			}
			if(null != finalmisseddoc[k]) {
				mandatoryDocName.append(finalmisseddoc[k]).append(",");
			}
			//RLOS.mLogger.info("RLOS Initiation finalmisseddoc is:" +finalmisseddoc[k]);
			//RLOS.mLogger.info("length of missed document mandatoryDocName.length1111"+mandatoryDocName.length());
		}
		mandatoryDocName.setLength(Math.max(mandatoryDocName.length()-1,0));
		//RLOS.mLogger.info("RLOS Initiation misseddoc is:" +mandatoryDocName.toString());

		if(mandatoryDocName.length()<=0){

			//RLOS.mLogger.info("RLOS Initiation misseddoc is: inside if condition");

		}
		else{
			//RLOS.mLogger.info("RLOS Initiation misseddoc is: inside if condition");
			//RLOS.mLogger.info("length of missed document mandatoryDocName.length1111222"+mandatoryDocName.length());
			//throw new ValidatorException(new FacesMessage("You have not attached Mandatory Documents: "+mandatoryDocName.toString()));
		}
	}
	/*public void IncomingDoc(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		IRepeater repObj=null;
		//IRepeater repObj=formObject.getRepeaterControl("IncomingDoc_Frame");

		//repObj = formObject.getRepeaterControl("IncomingDoc_Frame2");
		repObj = formObject.getRepeaterControl("IncomingDoc_Frame");


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

					String DocIndex=repObj.getValue(j,"cmplx_DocName_DocIndex")==null?"":repObj.getValue(j,"cmplx_DocName_DocIndex");
					RLOS.mLogger.info("DocIndex"+DocIndex);

					String StatusValue=repObj.getValue(j,"cmplx_DocName_Status")==null?"":repObj.getValue(j,"cmplx_DocName_Status");

					String Remarks=repObj.getValue(j, "cmplx_DocName_Remarks")==null?"":repObj.getValue(j, "cmplx_DocName_Remarks");
					RLOS.mLogger.info("Remarks"+Remarks);

					if(DocIndex==null || "".equalsIgnoreCase(DocIndex) || "null".equalsIgnoreCase(DocIndex))

					{

						RLOS.mLogger.info("StatusValue inside DocIndex"+DocIndex);
						if("Received".equalsIgnoreCase(StatusValue)){

							RLOS.mLogger.info("StatusValue inside DocIndex recieved");
							//below line commented as this mandatory document is already received. 
							//finalmisseddoc[j]=DocName;
						}

						else if("Deferred".equalsIgnoreCase(StatusValue)){

							formObject.setNGValue("is_deferral_approval_require","Y");
							formObject.RaiseEvent("WFSave");
							RLOS.mLogger.info(formObject.getNGValue("is_deferral_approval_require"));
							if("".equalsIgnoreCase(Remarks))
							{

								RLOS.mLogger.info("As you have not attached the Mandatory Document and the status is Deferred");
								throw new ValidatorException(new FacesMessage("As you have not attached the Mandatory Document fill the Remarks"));
							}
							else if(!"".equalsIgnoreCase(Remarks)|| Remarks.equalsIgnoreCase(null) || "null".equalsIgnoreCase(Remarks)){


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
							else if(!"".equalsIgnoreCase(Remarks)||Remarks.equalsIgnoreCase(null)||"null".equalsIgnoreCase(Remarks)){


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
	 */

	//incomingdoc function





	// Deepak Change to calculate EMI start
	public static String getEMI(double loanAmount,double rate,double tenureMonths)
	{       
		String loanAmt_DaysDiff="";
		try{


			if(loanAmount < 0 ||rate < 0 ||tenureMonths < 0 ){
				return "0";
			}
			
			BigDecimal B_intrate=  BigDecimal.valueOf(rate);
			BigDecimal B_tenure=  BigDecimal.valueOf(tenureMonths);
			BigDecimal B_loamamount=  BigDecimal.valueOf(loanAmount);
			loanAmt_DaysDiff=calcgoalseekEMI(B_intrate,B_tenure,B_loamamount);
		}
		catch(Exception e)
		{
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
			emi = numerator.divide(denominator,0);
		} 


		catch(Exception e){
			RLOS.logException(e);
		}
		return emi;
	}

	public static String calcgoalseekEMI(BigDecimal B_intrate,BigDecimal B_tenure,BigDecimal B_loamamount) {
		String loanAmt_DaysDiff="0";
		try{
			BigDecimal PMTEMI=calcEMI(B_loamamount,B_tenure,B_intrate);
			PMTEMI = PMTEMI.setScale(2,BigDecimal.ROUND_HALF_EVEN);
			double seedvalue=PMTEMI.doubleValue();
			loanAmt_DaysDiff=Double.toString(seedvalue);
		}
		catch(Exception e){
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
		}
		catch(Exception e){
			RLOS.logException(e);
			pmt=0;
		}
		return pmt;

	}
	//Akshay for comma Change
	public String putComma(String field){
		
		String limit=field.replaceAll(",","");
		double amount = Double.parseDouble(limit);
		DecimalFormat myFormatter = new DecimalFormat("#,###.000");
		String convString=myFormatter.format(amount);
		return convString;
	}
	//Akshay for comma Change
	// Deepak Change to calculate EMI END
	public String Convert_dateFormat(String date,String Old_Format,String new_Format)
	{
		//RLOS.mLogger.info( "Inside Convert_dateFormat()"+date);
		String new_date="";
		if("".equalsIgnoreCase(date) || date==null )
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
			//RLOS.mLogger.info( "Exception occurred in parsing date:"+e.getMessage());
			RLOS.logException(e);
		}
		return new_date;
	}


	public void  ParentToChild(){
		//RLOS.mLogger.info("RLOSCommon Inside ParentTochild() ");

		FormReference formObject=FormContext.getCurrentInstance().getFormReference();
		String mystring="";
		String product="";

		try{
			int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			for(int i=0;i<n;i++)	
			{
				mystring=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1);
				//RLOS.mLogger.info("Inside ParentTochild() mystring:"+mystring);
				if(!product.contains(mystring))
					product+=mystring;			
			}

			formObject.setNGValue("Product_Type", product);
			//RLOS.mLogger.info("Inside ParentToChild -> n :"+n+"Product_Type is:"+product);
		}
		catch(Exception e)
		{
			//RLOS.mLogger.info("Inside ParentToChild ->Exception occurred:"+e.getMessage());
			RLOS.logException(e);

		}
	}


	public String fetch_cust_details_primary(){
		String outputResponse="";
		String ReturnCode="";
		String alert_msg="";
		String errorCodeAlert="";
		RLOS_IntegrationInput GenXml=new RLOS_IntegrationInput();
		Common_Utils common=new Common_Utils(RLOS.mLogger);
		try{

			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			outputResponse = GenXml.GenerateXML("CUSTOMER_DETAILS","Primary_CIF");
			//RLOS.mLogger.info("Inside Customer");
			ReturnCode = getTagValue(outputResponse, "ReturnCode");
			//RLOS.mLogger.info("ReturnCode"+ReturnCode);
			errorCodeAlert=formObject.getNGDataFromDataCache("SELECT isnull((SELECT alert FROM ng_MASTER_INTEGRATION_ERROR_CODE with(nolock) WHERE Integration_call='CUSTOMER_DETAILS' AND error_code='"+ReturnCode+"'),(SELECT alert FROM ng_MASTER_INTEGRATION_ERROR_CODE with(nolock) WHERE  Integration_call='CUSTOMER_DETAILS' AND error_code='DEFAULT'))").get(0).get(0);
			//RLOS.mLogger.info("errorCodeAlert is:"+errorCodeAlert);
			if("0000".equalsIgnoreCase(ReturnCode) ){
				String Staff_cif = "";
				Staff_cif = getTagValue(outputResponse, "IsStaff");  
				//RLOS.mLogger.info("Value for StaffCIF is:"+Staff_cif);
				if("Y".equalsIgnoreCase(Staff_cif)){
					alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL051");
					return alert_msg;
				}
				else{
					//alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL058");
					//return alert_msg;
					
					
					//added by saurabh on 19th july.
					setcustomer_enable();
					formObject.setNGValue("Is_Customer_Details", "Y");
					alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL006");

					formObject.setVisible("Customer_Frame2", true);
					//formObject.setNGValue("cmplx_Customer_IscustomerSave", "Y");
					formObject.setLocked("Customer_Button1", false);
					formObject.setLocked("cmplx_Customer_CIFNO", true);
					formObject.setNGValue("Cust_Name", formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+ formObject.getNGValue("cmplx_Customer_MiddleName")+" "+ formObject.getNGValue("cmplx_Customer_LAstNAme"));
					formObject.setNGValue("CIF_ID", formObject.getNGValue("cmplx_Customer_CIFNO"));//
					formObject.setNGValue("Cif_Id", formObject.getNGValue("cmplx_Customer_CIFNO"));
					//code to enter started difference of years+month in UAE field
					String ResideSince =  (outputResponse.contains("<ResideSince>")) ? outputResponse.substring(outputResponse.indexOf("<ResideSince>")+"</ResideSince>".length()-1,outputResponse.indexOf("</ResideSince>")):"";    
					//RLOS.mLogger.info(ResideSince);
					if(!"".equalsIgnoreCase(ResideSince)){

						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
						Calendar cal = Calendar.getInstance();
						int CurrentYear=cal.get(Calendar.YEAR);
						int CurrentMonth=cal.get(Calendar.MONTH)+1;
						int CurrentDate=cal.get(Calendar.DATE);
						//RLOS.mLogger.info(CurrentYear+"-"+CurrentMonth+"-"+CurrentDate);

						Date d1 = null;
						Date d2 = null;
						int totalDays=1;//default set in code optimization(Sonar Code Optimization)
						try {
							d1 = format.parse(ResideSince);
							d2 = format.parse(CurrentYear+"-"+CurrentMonth+"-"+CurrentDate);
							totalDays = (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
						}
						catch (Exception ex) {
							RLOS.logException(ex);
						}
						
						int diffYear = totalDays/365;
						int diffdays = totalDays%365;
						int diffMonths=0;
						if(diffdays>=30) {
							diffMonths = diffdays/30;
							diffdays = diffdays%30;
						}
						//RLOS.mLogger.info("year difference"+diffYear);
						//RLOS.mLogger.info("diffMonths difference"+diffMonths);
						//RLOS.mLogger.info("diffdays difference"+diffdays);
						formObject.setNGValue("cmplx_Customer_yearsInUAE",diffYear+"."+diffMonths);
						//RLOS.mLogger.info("diffdays difference"+formObject.getNGValue("cmplx_Customer_yearsInUAE")); 
					}
					try{
						//added By Aman-28/6/17 for checking aecb checkbox
						String AECBheld =  (outputResponse.contains("<AECBConsentHeld>")) ? outputResponse.substring(outputResponse.indexOf("<AECBConsentHeld>")+"</AECBConsentHeld>".length()-1,outputResponse.indexOf("</AECBConsentHeld>")):"";
						if ("Y".equalsIgnoreCase(AECBheld))

						{
							//RLOS.mLogger.info( "Inside AECB Consent true check!!");
							formObject.fetchFragment("Liability_container", "Liability_New", "q_LiabilityNew");
							formObject.setNGValue("cmplx_Liability_New_AECBconsentAvail", "true");
							formObject.setLocked("Liability_New_fetchLiabilities",false);
							formObject.setEnabled("Liability_New_fetchLiabilities",true);
							if((!NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_BTC").equals(formObject.getNGValue("Subproduct_productGrid")) || !NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SE").equals(formObject.getNGValue("Subproduct_productGrid"))) && !"".equals(formObject.getNGValue("Subproduct_productGrid"))){
								formObject.setLocked("Liability_New_fetchLiabilities",false);
							}
							else{
								formObject.setLocked("Liability_New_fetchLiabilities",true);
							}
						}
						else 
						{
							formObject.setNGValue("cmplx_Liability_New_AECBconsentAvail", "false");
							formObject.setLocked("Liability_New_fetchLiabilities",true);
							formObject.setEnabled("Liability_New_fetchLiabilities",false);

						}
						//ended By Aman-28/6/17 for checking aecb checkbox
						//fetch fragment done by aman for salary transfer flag 07th Dec
						//change by saurabh on 7may
						//formObject.setTop("ProductDetailsLoader",formObject.getTop("CustomerDetails")+formObject.getHeight("CustomerDetails"));
						boolean framestate=formObject.isVisible("IncomeDetails_Frame1");
						if(!framestate)
						{
							formObject.fetchFragment("Incomedetails", "IncomeDetails", "q_IncomeDetails");
							adjustFrameTops("GuarantorDetails,Incomedetails");
							//formObject.setTop("Incomedetails",formObject.getTop("ProductDetailsLoader")+formObject.getHeight("ProductDetailsLoader"));
							RLOSCommonCode.visibilityFrameIncomeDetails(formObject);
						}
						//fetch fragment done by aman for salary transfer flag 07th Dec
						//code to enter started difference of years+month in UAE field

						formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmploymentDetails");
						formObject.setLocked("cmplx_EmploymentDetails_DOJ",false);//forDOJ enable
						String EmpCode = formObject.getNGValue("cmplx_EmploymentDetails_EMpCode");
						if(EmpCode!=null && !"".equalsIgnoreCase(EmpCode) && !" ".equalsIgnoreCase(EmpCode)){
							getDataFromALOC(formObject,EmpCode);
						}
						
						//below code added by Arun (11/12/17) to fetch fragemnts after cust details call success
						openDemographicTabs();

						
						RLOS_IntegrationOutput.valueSetIntegration(outputResponse,"Primary_CIF");
						//chnage by saurabh for JIRA- 9155
						String mobNo = setPreferredMobile(outputResponse);
						if(mobNo !=null){
							formObject.setNGValue("cmplx_Customer_MobNo",mobNo);
							formObject.setNGValue("AlternateContactDetails_MobileNo1",mobNo);
						}
						if ("".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_EmiratesID"))){
							formObject.setLocked("Liability_New_fetchLiabilities",false);
						}
						else {
							formObject.setLocked("Liability_New_fetchLiabilities",true);
						}
						//COde SHifted by aman on 7/12
						employment_details_load();
						loadPicklist_Address();
						formObject.setNGValue("AlternateContactDetails_MobNo2", formObject.getNGValue("AlternateContactDetails_MobileNo1"));

					}
					catch(Exception ex){
						RLOS.logException(ex);

					}

					int n=formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
					if(n>0)
					{
						for(int i=0;i<n;i++)
						{
							//RLOS.mLogger.info(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0));
							if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESIDENCE").equalsIgnoreCase(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0)))
							{
								//RLOS.mLogger.info(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0));
								formObject.setNGValue("cmplx_Customer_EmirateOfResidence",formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 6));
							}
						}
					}
					//code to set Emirates of residence End.
					//code change to save the date in desired format.

					try{
						//RLOS.mLogger.info(formObject.getNGValue("cmplx_Customer_DOb")+" cmplx_Customer_PassPortExpiry: "+formObject.getNGValue("cmplx_Customer_PassPortExpiry")+" cmplx_Customer_EmirateIDExpiry: "+formObject.getNGValue("cmplx_Customer_EmirateIDExpiry")+" VIsaExpiry: "+ formObject.getNGValue("cmplx_Customer_VIsaExpiry")+" cmplx_Customer_IdIssueDate: "+ formObject.getNGValue("cmplx_Customer_IdIssueDate"));
						String str_dob=formObject.getNGValue("cmplx_Customer_DOb");
						String str_IDissuedate=formObject.getNGValue("cmplx_Customer_IdIssueDate");
						String str_passExpDate=formObject.getNGValue("cmplx_Customer_PassPortExpiry");
						String str_visaExpDate=formObject.getNGValue("cmplx_Customer_VIsaExpiry");
						String str_EIDExpDate=formObject.getNGValue("cmplx_Customer_EmirateIDExpiry");
						String str_DOJDate=formObject.getNGValue("cmplx_EmploymentDetails_DOJ");
						//Added By Aman TO save the Visa Issue and Passport issue in desired format
						String str_VisaIssDate=formObject.getNGValue("cmplx_Customer_VisaIssuedate");
						String str_PassIssDate=formObject.getNGValue("cmplx_Customer_PassportIssueDate");

						if(str_VisaIssDate!=null&&!"".equalsIgnoreCase(str_VisaIssDate)){
							formObject.setNGValue("cmplx_Customer_VisaIssuedate",Convert_dateFormat(str_VisaIssDate, "yyyy-mm-dd", "dd/mm/yyyy"),false);
						}
						if(str_PassIssDate!=null&&!"".equalsIgnoreCase(str_PassIssDate)){
							formObject.setNGValue("cmplx_Customer_PassportIssueDate",Convert_dateFormat(str_PassIssDate, "yyyy-mm-dd", "dd/mm/yyyy"),false);
						}

						//Added By Aman TO save the Visa Issue and Passport issue in desired format
						if(str_dob!=null && !"".equalsIgnoreCase(str_dob)){
							formObject.setNGValue("cmplx_Customer_DOb",Convert_dateFormat(str_dob, "yyyy-mm-dd", "dd/mm/yyyy"),false);
							//RLOS.mLogger.info("Value of dob is"+formObject.getNGValue("cmplx_Customer_DOb"));
							common.getAge(formObject.getNGValue("cmplx_Customer_DOb"),"cmplx_Customer_age");
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
						if(str_DOJDate!=null&&!"".equalsIgnoreCase(str_DOJDate)){
							formObject.setNGValue("cmplx_EmploymentDetails_DOJ",Convert_dateFormat(str_DOJDate, "yyyy-mm-dd", "dd/mm/yyyy"),false);
							common.getAge(formObject.getNGValue("cmplx_EmploymentDetails_DOJ"),"cmplx_EmploymentDetails_LOS");

						}
						String  GCC="BH,IQ,KW,OM,QA,SA,AE";
						if(GCC.contains(formObject.getNGValue("cmplx_Customer_Nationality")))
						{
							//RLOS.mLogger.info("Inside GCC for Nationality");
							formObject.setNGValue("cmplx_Customer_GCCNational","Y",false);
						}
						else
						{
							//RLOS.mLogger.info("Nationality: "+formObject.getNGValue("cmplx_Customer_Nationality")+" is not GCC");
							formObject.setNGValue("cmplx_Customer_GCCNational","N",false);
						}

						String mobileNo = formObject.getNGValue("cmplx_Customer_MobNo");
						if(mobileNo!=null && !"".equalsIgnoreCase(mobileNo)){
							formObject.setNGValue("OTP_Mobile_NO", mobileNo);
							formObject.setEnabled("OTP_Mobile_NO", false);
							formObject.setEnabled("Send_OTP_Btn", true);
						}
						//changed by akshay on 15Th dec 2017 for NEPTYpe check.
						if("".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP"))){
							//formObject.setLocked("EMploymentDetails_Label25",true);//Added by Akshay on 9/9/17 for enabling NEP type as per FSD
							formObject.setLocked("cmplx_EmploymentDetails_NepType",true);
						}
						else if("NEWJ".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP")) || "NEWC".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NEP"))){
							formObject.setNGValue("cmplx_EmploymentDetails_NepType",formObject.getNGValue("cmplx_Customer_NEP"));
						}

						FATCA_Validations(formObject);//added by akshay on 14/12/17 for FATCA validations after call
						EID_Visa_date(formObject);

					}
					catch(Exception e)
					{
						RLOS.logException(e);
						//RLOS.mLogger.info("Exception Occured: "+e.getMessage());
						popupFlag = "Y";
						//RLOS.mLogger.info("inside Customer Eligibility to through Exception to Exit:");
						alert_msg = NGFUserResourceMgr_RLOS.getAlert("VAL006");

						throw new ValidatorException(new FacesMessage(alert_msg));

					}

				
					//temp commented by aman for sprint 1
					}

			}
			// below changes done by disha on 28-02-2018 drop-4 Unverified CIF only cif will be populated and rest form will be editable and blank
			else if("CINF0282".equalsIgnoreCase(ReturnCode) || "CINF377".equalsIgnoreCase(ReturnCode) )
			{
				//RLOS.mLogger.info(errorCodeAlert);
				formObject.setNGValue("CIF_ID", formObject.getNGValue("cmplx_Customer_CIFNO"));
				alert_msg=errorCodeAlert;
				setcustomer_enable();

			}
			// above changes done by disha on 28-02-2018 drop-4 Unverified CIF only cif will be populated and rest form will be editable and blank
			else{
				//RLOS.mLogger.info(errorCodeAlert);
				alert_msg=NGFUserResourceMgr_RLOS.getAlert(errorCodeAlert);
				formObject.setNGValue("Is_Customer_Details","N");
			}
		}
		catch(Exception e)
		{
			RLOS.logException(e);
			//RLOS.mLogger.info( "Exception occured in fetch_cust_details_primary method"+e.getMessage());
			if("".equalsIgnoreCase(alert_msg)){
				alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL047");


			}

		}
		return alert_msg;
	}	


	private String setPreferredMobile(String str) {
		try{
		if(str.indexOf("<PhnPrefFlag>Y</PhnPrefFlag>")>-1){
		int prefIndex = str.indexOf("<PhnPrefFlag>Y</PhnPrefFlag>");
		int endIndex = str.indexOf("</PhnDet>",prefIndex);
		str=str.substring(0,endIndex);
		int begIndex = str.lastIndexOf("<PhnDet>");
		str = str.substring(begIndex, endIndex);
		return str.substring(str.indexOf("<PhoneNo>")+9, str.indexOf("</PhoneNo>"));
		}
		else{
			return null;
		}
		}catch(Exception ex){
			printException(ex);
			return null;
		}
		 
	}
	public String fetch_cust_details_supplementary(){
		String outputResponse="";
		String ReturnCode="";
		String alert_msg="";
		RLOS_IntegrationInput GenXml=new RLOS_IntegrationInput();
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			outputResponse = GenXml.GenerateXML("CUSTOMER_DETAILS","Supplementary_Card_Details");
			//RLOS.mLogger.info("Inside Customer");
			/*String Gender =  (outputResponse.contains("<Gender>")) ? outputResponse.substring(outputResponse.indexOf("<Gender>")+"</Gender>".length()-1,outputResponse.indexOf("</Gender>")):"";
			RLOS.mLogger.info(Gender);

			String  Marital_Status =  (outputResponse.contains("<MaritialStatus>")) ? outputResponse.substring(outputResponse.indexOf("<MaritialStatus>")+"</MaritialStatus>".length()-1,outputResponse.indexOf("</MaritialStatus>")):"";
			RLOS.mLogger.info(Marital_Status);*/
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			//RLOS.mLogger.info(ReturnCode);

			if("0000".equalsIgnoreCase(ReturnCode) ){
				formObject.clear("SupplementCardDetails_AddressList");
				formObject.clear("SupplementCardDetails_FatcaList");
				formObject.clear("SupplementCardDetails_KYCList");
				formObject.clear("SupplementCardDetails_OecdList");

				RLOS_IntegrationOutput.valueSetIntegration(outputResponse,"Supplementary_Card_Details");
				alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL006");
				formObject.setLocked("SupplementCardDetails_FetchDetails", true);

				/*	SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-mm-dd");
				SimpleDateFormat sdf2=new SimpleDateFormat("dd/mm/yyyy");
				String Datechanged=sdf2.format(sdf1.parse(Date1));
				RLOS.mLogger.info(Datechanged);
				formObject.setNGValue("DOB",Datechanged,false);*/
				String dob=formObject.getNGValue("DOB");
				String str_IDissuedate=formObject.getNGValue("SupplementCardDetails_IDIssueDate");
				String str_PassIssDate=formObject.getNGValue("SupplementCardDetails_PassportIssueDate");
				String str_VisaIssDate=formObject.getNGValue("SupplementCardDetails_VisaIssueDate");

				if(dob!=null && !"".equalsIgnoreCase(dob)){
					formObject.setNGValue("DOB",Convert_dateFormat(dob, "yyyy-mm-dd", "dd/mm/yyyy"),false);
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
			}
			else{
				alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL046");

			}

		}catch(Exception ex){
			RLOS.mLogger.info( "Exception occured in fetch_cust_details_primary method"+printException(ex));
			if("".equalsIgnoreCase(alert_msg)){
				alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL047");


			}
		}
		return alert_msg;
	}

	//New method added to fetch Eligibility and Product fragment
	public void fetch_EligPrd_frag(){
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			openDemographicTabs();
			
			formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligAndProductInfo");
			formObject.setNGFrameState("EligibilityAndProductInformation", 0);
			//sagarika for CR eligibility
			String query="select max(CreditLimit) from ng_RLOS_CUSTEXPOSE_CardDetails with (nolock) where  wi_name='"+formObject.getWFWorkitemName()+"'";
	//RLOS.mLogger.info("Inside ELigibiltyAndProductInfo_Button1-->Query is:"+queryTakeOver);
			List<List<String>> result=formObject.getDataFromDataSource(query);//sagarika for CR
			if(!result.isEmpty())
			{
		RLOS.mLogger.info("@sagarika new ");
		formObject.setNGValue("ELigibiltyAndProductInfo_Text24",result.get(0).get(0));
		RLOS.mLogger.info(result.get(0).get(0)+"ss");
		RLOS.mLogger.info("@sv"+formObject.getNGValue("ELigibiltyAndProductInfo_Text24"));
			}
			//change by saurabh on 10th Oct
			//RLOS.mLogger.info("Value of EMI is: "+formObject.getNGValue("cmplx_EligibilityAndProductInfo_EMI"));
			//setChargesFields();//Commented by Deepak new line of code written in check re-eligibility click event.
			formObject.setEnabled("cmplx_EligibilityAndProductInfo_FinalInterestRate", false);


			//RLOS.mLogger.info("Eligibility grid");
			if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_PersonalLoan").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1))/* && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")*/){
				formObject.setVisible("ELigibiltyAndProductInfo_Frame2", true);//Funding Account Number Grid
				formObject.setVisible("ELigibiltyAndProductInfo_Frame4", true);//Personal Loan
				formObject.setVisible("ELigibiltyAndProductInfo_Frame6",true);//ELigible for Card
				formObject.setVisible("ELigibiltyAndProductInfo_Frame3", false);//Lein Details
				formObject.setVisible("ELigibiltyAndProductInfo_Frame5",false);//CC
				//added by akshay on 29/9/17 for improving visibility and order of fields
				formObject.setTop("ELigibiltyAndProductInfo_Frame6",formObject.getTop("ELigibiltyAndProductInfo_Frame4")+25);
				formObject.setTop("ELigibiltyAndProductInfo_Frame2",formObject.getTop("ELigibiltyAndProductInfo_Frame6")+25);
				//ended by akshay on 29/9/17 for improving visibility and order of fields
				String tenure=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 7);
				//RLOS.mLogger.info("Funding Account Details now Visible...!!!");
				formObject.setNGValue("cmplx_EligibilityAndProductInfo_Tenor", tenure);
			}
			else if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1))/* && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")*/){
				formObject.setVisible("ELigibiltyAndProductInfo_Frame5",true);//CC
				formObject.setVisible("ELigibiltyAndProductInfo_Frame6",true);//ELigible for Card
				formObject.setVisible("ELigibiltyAndProductInfo_Frame2", false);//Funding Account Number Grid
				formObject.setVisible("ELigibiltyAndProductInfo_Frame4", false);//Personal Loan
				formObject.setVisible("ELigibiltyAndProductInfo_Frame3", false);//Lein Details
				formObject.setTop("ELigibiltyAndProductInfo_Frame5",5);//CC
				formObject.setTop("ELigibiltyAndProductInfo_Frame6",formObject.getTop("ELigibiltyAndProductInfo_Frame5")+25);//Eligible For Card

				if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("SEC") /*&& formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")*/){
					formObject.setVisible("ELigibiltyAndProductInfo_Frame3", true);//Lein Details
					formObject.setTop("ELigibiltyAndProductInfo_Frame3",formObject.getTop("ELigibiltyAndProductInfo_Frame6")+25);
					//RLOS.mLogger.info("Lein Details now Visible...!!!");
					loadDatainLiengrid();
				}
				//break;
			}
			//}
			//}
			else{
				formObject.setVisible("ELigibiltyAndProductInfo_Frame2", false);
				formObject.setVisible("ELigibiltyAndProductInfo_Frame3", false);
			}
			Eligibilityfields();

			//change by saurabh on 30th Nov for FSD 2.7
			String counter = formObject.getNGValue("reEligbility_init_counter").split(";")[0];
			if(counter.equalsIgnoreCase(NGFUserResourceMgr_RLOS.getAlert("VAL049")))
			{
				formObject.setEnabled("ELigibiltyAndProductInfo_Button1", false);
			}

			//below fetch added by akshay on 27/3/18
			if(formObject.isVisible("Liability_New_Frame1")==false){
				fetch_Liability_frag(formObject);	
			}
			openDemographicTabs();
			/*//added by akshay on 27/12/17
		if(formObject.isVisible("CardDetails_Frame1")==false){
			formObject.fetchFragment("CardDetails_container", "CardDetails", "q_CardDetails");
			formObject.setTop("CardDetails_container", formObject.getTop("Alt_Contact_container")+formObject.getHeight("Alt_Contact_container")+30);
			formObject.setNGFrameState("CardDetails_container", 0);
			if(!formObject.isVisible("SupplementCardDetails_Frame1") && "Yes".equals(formObject.getNGValue("cmplx_CardDetails_SuppCardReq"))){
				formObject.setVisible("Supplementary_Container", true);
			}
			formObject.setTop("Supplementary_Container", formObject.getTop("CardDetails_container")+formObject.getHeight("CardDetails_container")+30);
			formObject.setTop("FATCA", formObject.getTop("Supplementary_Container")+30);
			formObject.setTop("KYC", formObject.getTop("FATCA")+30);
			formObject.setTop("OECD", formObject.getTop("KYC")+30);
			formObject.setTop("ReferenceDetails", formObject.getTop("OECD")+30);

		}*/
			if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_IM").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2)))
			{
				formObject.setVisible("ELigibiltyAndProductInfo_Label14", true);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_InterestType", true);
			}

			//RLOS.mLogger.info("Value of EMI is: "+formObject.getNGValue("cmplx_EligibilityAndProductInfo_EMI"));
		}catch(Exception ex){
			RLOS.mLogger.info("Exception in fetch_EligPrd_frag: "+printException(ex));
		}
	}
	public void loadDatainLiengrid(){
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String query = "select AcctId,LienAmount,LienRemarks,Wi_Name from ng_rlos_FinancialSummary_LienDetails with (nolock) where Wi_Name = '"+formObject.getWFWorkitemName()+"'";
			//RLOS.mLogger.info("Value of query is: "+query);
			List<List<String>> records = formObject.getNGDataFromDataCache(query);
			//RLOS.mLogger.info("Value of records is: "+records);
			for(List<String> record : records){
				formObject.addItemFromList("ELigibiltyAndProductInfo_ListView5", record);
			}
		}catch(Exception ex){
			RLOS.mLogger.info("Exception in loadDatainLiengrid: "+printException(ex));
		}
	}

	public String makeInputForGrid(String customerName) {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int columns=0;
		String temp = "<ListItem>";
		//added by akshay on 22/11/17 for authorised details
		String cif = formObject.getNGValue("cmplx_Customer_CIFNO");
		String name = formObject.getNGValue("cmplx_Customer_FIrstNAme");
		String nationality = formObject.getNGValue("cmplx_Customer_Nationality");
		String dob = formObject.getNGValue("cmplx_Customer_DOb");
		String visanum = formObject.getNGValue("cmplx_Customer_VisaNo");
		String visaexp = formObject.getNGValue("cmplx_Customer_VIsaExpiry");
		String pass = formObject.getNGValue("cmplx_Customer_PAssportNo");
		String passexp = formObject.getNGValue("cmplx_Customer_PassPortExpiry");

		String Executexml= "<ListItems><ListItem><AuthSignCIFNo>"+cif+"</AuthSignCIFNo>" +
		"<AuthSignName>"+name+"</AuthSignName>"+
		"<AuthSignNationality>"+nationality+"</AuthSignNationality>"+
		"<AuthSignDOB>"+dob+"</AuthSignDOB>"+
		"<AuthSignVisaNo>"+visanum+"</AuthSignVisaNo>"+
		"<AuthSignVisaExpiry>"+visaexp+"</AuthSignVisaExpiry>"+
		"<AuthSignStatus></AuthSignStatus>"+
		"<AuthSignPassportNo>"+pass+"</AuthSignPassportNo>"+
		"<AuthSignPassportExpiry>"+passexp+"</AuthSignPassportExpiry>"+
		"<AuthSignShareholding></AuthSignShareholding>"+
		"<AuthSignSoleEmp></AuthSignSoleEmp>"+
		"<AuthorisedSign_wiName>"+formObject.getWFWorkitemName()+"</AuthorisedSign_wiName>"
		+"<AuthSignDesignation></AuthSignDesignation><AuthSignDesignationAsPerVisa></AuthSignDesignationAsPerVisa></ListItem></ListItems>";
		//ended by akshay on 22/11/17 for authorised details
		try{
			UIComponent pComp =formObject.getComponent("cmplx_CompanyDetails_cmplx_CompanyGrid");
			if( pComp != null && pComp instanceof ListView )
			{			
				ListView objListView = ( ListView )pComp;
				columns  = objListView.getChildCount();
				Column c=(Column)(pComp.getChildren().get(28));
				//RLOS.mLogger.info("name of 29th column: "+c.getName());
				//RLOS.mLogger.info("columns: "+ columns+"");
				for(int i=0;i<columns;i++){
					c=(Column)(pComp.getChildren().get(i));
					if("Applicant Name".equals(c.getName()))
						temp+= "<SubItem>"+customerName+"</SubItem>";
					else if("Applicant Category".equals(c.getName()))
						temp+="<SubItem>Authorised signatory</SubItem>";
					else if("Applicant Type".equals(c.getName()))
						temp+="<SubItem>Primary</SubItem>";
					//Loop changed from 21 to 26 by Saurabh on 12th Oct.  
					else if("wi_name".equals(c.getName())){
						temp+= "<SubItem>"+formObject.getWFWorkitemName()+"</SubItem>";
					}
					else if("Auth Grid".equals(c.getName())){
						temp+="<SubItem>"+Executexml+"</SubItem>";
					}
					else{
						temp+= "<SubItem></SubItem>";
					}
				}
			}
			temp+="</ListItem>";
		}
		catch(Exception e)
		{
			RLOS.mLogger.info("Inside makeinputForGrid(): exception occurred"+ e.getMessage()+printException(e));
		}
		return temp;
	}


	public void AddInAuthorisedGrid(FormReference formObject,String customerName)
	{
		formObject.setNGValue("AuthorisedSignDetails_CIFNo", formObject.getNGValue("cmplx_Customer_CIFNO"));
		formObject.setNGValue("AuthorisedSignDetails_Name",customerName);
		formObject.setNGValue("AuthorisedSignDetails_nationality", formObject.getNGValue("cmplx_Customer_Nationality"));
		formObject.setNGValue("AuthorisedSignDetails_DOB", formObject.getNGValue("cmplx_Customer_DOb"));
		formObject.setNGValue("AuthorisedSignDetails_VisaNumber",  formObject.getNGValue("cmplx_Customer_VisaNo"));
		formObject.setNGValue("AuthorisedSignDetails_VisaExpiryDate",formObject.getNGValue("cmplx_Customer_VIsaExpiry"));
		formObject.setNGValue("AuthorisedSignDetails_PassportNo",formObject.getNGValue("cmplx_Customer_PAssportNo"));
		formObject.setNGValue("AuthorisedSignDetails_PassportExpiryDate",formObject.getNGValue("cmplx_Customer_PassPortExpiry"));
		formObject.setNGValue("AuthorisedSign_wiName", formObject.getWFWorkitemName());
		if("false".equals(formObject.getNGValue("cmplx_Customer_NTB"))){//added by akshay on 18/1/18 to set Designation whenits coming in customer details
			formObject.setNGValue("Designation",formObject.getNGValue("cmplx_EmploymentDetails_Designation"));
		}
		formObject.ExecuteExternalCommand("NGAddRow", "cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails");
	}


	public void AddInCompanyGrid(FormReference formObject,String customerName)
	{
		formObject.setNGValue("appNAme", customerName);
		formObject.setNGValue("appCateg", "Authorised signatory");
		formObject.setNGValue("appType", "Primary");
		formObject.setNGValue("company_winame", formObject.getWFWorkitemName());
		formObject.ExecuteExternalCommand("NGAddRow", "cmplx_CompanyDetails_cmplx_CompanyGrid");

	}

	public void getDataFromALOC(FormReference formObject2, String corpName) {
		try{
			String EmpCode=formObject2.getNGValue("cmplx_EmploymentDetails_EMpCode");
			String query = "select INCLUDED_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,NAME_OF_FREEZONE_AUTHORITY,EMPLOYER_CATEGORY_PL,COMPANY_STATUS_CC,COMPANY_STATUS_PL,CONSTITUTION from NG_RLOS_ALOC_OFFLINE_DATA with(nolock) where EMPLOYER_CODE = '"+EmpCode+"'";
			List<List<String>> result = formObject2.getDataFromDataSource(query);
			if(result!=null && !result.isEmpty())  //if(result!=null && result.size()>0)
			{
				formObject2.setNGValue("cmplx_EmploymentDetails_IncInCC", (result.get(0).get(0).equalsIgnoreCase("Y")?"true":"false"));
				formObject2.setNGValue("cmplx_EmploymentDetails_IncInPL", (result.get(0).get(1).equalsIgnoreCase("Y")?"true":"false"));
				formObject2.setNGValue("cmplx_EmploymentDetails_EmpIndusSector", result.get(0).get(2));
				formObject2.setNGValue("cmplx_EmploymentDetails_Indus_Macro", result.get(0).get(3));
				formObject2.setNGValue("cmplx_EmploymentDetails_Indus_Micro", result.get(0).get(4));
				formObject2.setNGValue("cmplx_EmploymentDetails_FreezoneName", result.get(0).get(5));
				formObject2.setNGValue("cmplx_EmploymentDetails_CurrEmployer", result.get(0).get(6));
				formObject2.setNGValue("cmplx_EmploymentDetails_EmpStatusPL", result.get(0).get(7));
				formObject2.setNGValue("cmplx_EmploymentDetails_EmpStatusCC", result.get(0).get(8));
				formObject2.setNGValue("cmplx_EmploymentDetails_Freezone", (result.get(0).get(9).equalsIgnoreCase("Y")?"true":"false"));
			}
		}
		catch(Exception ex){
			RLOS.mLogger.info( printException(ex));
		}
	}


	public void loadPicklist_TargetSegmentCode()
	{
		try{
			FormReference formObject=FormContext.getCurrentInstance().getFormReference();
			//RLOS.mLogger.info("Inside loadPicklist_TargetSegmentCode()");
			String reqProd = formObject.getNGValue("PrimaryProduct");
			if("".equals(reqProd)){//added by akshay for proc 10580
				reqProd=formObject.getNGValue("Product_Type");
			}
			String appCategory = formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg");
			String subproduct = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2);
			if(!"".equals(subproduct) && !"".equals(reqProd)){
				if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_PersonalLoan").equalsIgnoreCase(reqProd)){

					if(appCategory!=null && "BAU".equalsIgnoreCase(appCategory)){
						LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  description,code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and BAU='Y' and (product = 'PL' or product='B') order by code");
					}
					else if(appCategory!=null &&  "S".equalsIgnoreCase(appCategory)){
						LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  description,code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and Surrogate='Y' and (product = 'PL' or product='B') order by code");
					}
					else{
						LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  description,code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and (product = 'PL' or product='B') order by code");
					}
				}
				else if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard").equalsIgnoreCase(reqProd)){
					if(appCategory!=null &&  "BAU".equalsIgnoreCase(appCategory)){
						LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  description,code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and BAU='Y' and (product = 'CC' or product='B') order by code");
					}
					else if(appCategory!=null &&  "S".equalsIgnoreCase(appCategory)){
						LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  description,code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and Surrogate='Y' and (product = 'CC' or product='B') order by code");
					}
					else{
						LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  description,code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and (product = 'CC' or product='B') order by code");	
					}
				}
			}
		}catch(Exception e){
			printException(e);
		}
	}

	public void IslamicFieldsvisibility() {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String query = "select distinct limit.Card_Product,limit.CC_Waiver,mstr.ReqProduct from ng_rlos_IGR_Eligibility_CardLimit limit with(nolock) join  ng_master_cardProduct mstr with(nolock) on limit.Card_Product=mstr.CODE where wi_name = '"+formObject.getWFWorkitemName()+"' and Cardproductselect='true'";
		List<List<String>> records = formObject.getDataFromDataSource(query);
		//RLOS.mLogger.info("query produced is:"+query);
		try{
			boolean cc_waiver = false;
			boolean islamic_visible = false;
			boolean islamic_mandatory = false;
			boolean kalyanCompany = false;
			if(records!=null && records.size()>0){
				//RLOS.mLogger.info("records list is:"+records);
				for(List<String> row:records){
					//RLOS.mLogger.info("row list is:"+records);
					if( row.get(2)!=null && row.get(2).equalsIgnoreCase("Islamic") && !row.get(0).contains("LOC")){
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
				formObject.setVisible("CardDetails_Label2", true);
				formObject.setVisible("cmplx_CardDetails_CharityOrg", true);
				formObject.setVisible("CardDetails_Label4", true);
				formObject.setVisible("cmplx_CardDetails_CharityAmount", true);
				formObject.setLeft("CardDetails_Label5", 1059);
				formObject.setLeft("cmplx_CardDetails_SuppCardReq", 1059);
			}
			else
			{
				formObject.setVisible("CardDetails_Label2", false);
				formObject.setVisible("cmplx_CardDetails_CharityOrg", false);
				formObject.setVisible("CardDetails_Label4", false);
				formObject.setVisible("cmplx_CardDetails_CharityAmount", false);

			}
			if(islamic_mandatory){
				formObject.setNGValue("CardDetails_Islamic_mandatory","Y");
			}
			if(kalyanCompany){
				formObject.setVisible("CardDetails_Label3", true);
				formObject.setVisible("cmplx_CardDetails_CompanyEmbossingName", true);
			}
			/*if(!cc_waiver){
				formObject.setVisible("CardDetails_Label7", true);
				formObject.setVisible("cmplx_CardDetails_statCycle", true);
			}*/
		}catch(Exception ex){
			RLOS.mLogger.info( "Exception in IslamicFieldsVisibility Function"+printException(ex));
		}
	}
	public void FATCA_Validations(FormReference formObject){
		//addedby akshay on 2/12/17 for FATCA enabling
		//RLOS.mLogger.info( "Inside FATCA_Validations");
		if("N".equalsIgnoreCase(formObject.getNGValue("cmplx_FATCA_USRelation")) || "R".equalsIgnoreCase(formObject.getNGValue("cmplx_FATCA_USRelation")))
		{
			formObject.setLocked("cmplx_FATCA_SignedDate",false);
			formObject.setLocked("cmplx_FATCA_ExpiryDate",false);
			formObject.setLocked("cmplx_FATCA_TINNo",false);
			formObject.setLocked("cmplx_FATCA_ControllingPersonUSRel",false);
			formObject.setNGValue("cmplx_FATCA_iddoc",true);
		}
		else if("O".equalsIgnoreCase(formObject.getNGValue("cmplx_FATCA_USRelation"))){
			formObject.setEnabled("FATCA_Frame6", false);
			formObject.setEnabled("FATCA_Save", true);
			formObject.setEnabled("cmplx_FATCA_USRelation",true);
			formObject.setNGValue("cmplx_FATCA_iddoc", "true");
			formObject.setNGValue("cmplx_FATCA_decforIndv", "true");
		}
		else if("C".equalsIgnoreCase(formObject.getNGValue("cmplx_FATCA_USRelation"))){
			formObject.setEnabled("FATCA_Frame6", false);
			formObject.setEnabled("FATCA_Save", true);
			formObject.setEnabled("cmplx_FATCA_USRelation",true);
			formObject.setLocked("cmplx_FATCA_SignedDate",false);
		}
		//ended by akshay
	}

	public void fetch_Liability_frag(FormReference formObject){
		formObject.fetchFragment("Liability_container", "Liability_New", "q_LiabilityNew");  
		//RLOS.mLogger.info("RLOSCommon Inside Liability_container:"); 
		if((!formObject.isVisible("EMploymentDetails_Frame1"))){//employment tab
			formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmploymentDetails");
			formObject.setLocked("cmplx_EmploymentDetails_DOJ",false);//forDOJ enable
			//DOJ
			//change by saurabh on 1St Dec for Tanshu points.
			
			//RLOS.mLogger.info(" fetched employment details");
			//below code added by nikhil for CAC Bank Name Fieldm to be diplayed
			if("CAC".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")))
			{
			formObject.setVisible("cmplx_Liability_New_CACBankName", true);
			formObject.setVisible("Liability_New_Label5", true);
			}
			else 
			{
				formObject.setVisible("cmplx_Liability_New_CACBankName", false);
				formObject.setVisible("Liability_New_Label5", false);
			}
		}

		if(formObject.isVisible("CompanyDetails_Frame1") ==false && NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SelfEmployed").equals(formObject.getNGValue("EmploymentType"))){
			formObject.fetchFragment("CompanyDetails", "CompanyDetails", "q_CompanyDetails");
			//below code added by nikhil for CAC Bank Name Fieldm to be diplayed
			if("CAC".equalsIgnoreCase(formObject.getNGValue("TargetSegmentCode")))
			{
			formObject.setVisible("cmplx_Liability_New_CACBankName", true);
			formObject.setVisible("Liability_New_Label5", true);
			}
			else 
			{
				formObject.setVisible("cmplx_Liability_New_CACBankName", false);
				formObject.setVisible("Liability_New_Label5", false);
			}
		}	
		//int framestatecomp=formObject.getNGFrameState("Alt_Contact_container");
		/*if(formObject.isVisible("CompanyDetails_Frame1") ==false && NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SelfEmployed").equals(formObject.getNGValue("EmploymentType"))){
			formObject.fetchFragment("CompanyDetails", "CompanyDetails", "q_CompanyDetails");
		}*/	
		//formObject.setTop("AuthorisedSignatoryDetails", formObject.getTop("CompanyDetails")+formObject.getHeight("CompanyDetails")+25);

		/*if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SE").equalsIgnoreCase(formObject.getNGValue("Subproduct_productGrid")) && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")){
				formObject.setVisible("CompanyDetails_Label27", true);
				formObject.setVisible("EOW", true);//Arun (22/10)
				formObject.setVisible("CompanyDetails_Label29", true);
				formObject.setVisible("headOffice", true); //Arun (22/10)
				formObject.setVisible("CompanyDetails_Label28", true);
				formObject.setVisible("visaSponsor", true);


			}
			else{
				formObject.setVisible("CompanyDetails_Label27", false);
				formObject.setVisible("EOW", false);//Emirate of work Arun (22/10)
				formObject.setVisible("CompanyDetails_Label29", false);
				formObject.setVisible("headOffice", false);//Arun (22/10)
				formObject.setVisible("CompanyDetails_Label28", false);
				formObject.setVisible("visaSponsor", false);
				//formObject.setVisible("CompanyDetails_Label8", false);
				formObject.setVisible("CompanyDetails_effecLOB", false);
			}*/

		//}
		/*int framestateauth=formObject.getNGFrameState("AuthorisedSignatoryDetails");
		if(framestateauth == 1){
			formObject.fetchFragment("AuthorisedSignatoryDetails", "AuthorisedSignDetails", "q_AuthorisedSignDetails");
			formObject.setTop("PartnerDetails", formObject.getTop("AuthorisedSignatoryDetails")+formObject.getHeight("AuthorisedSignatoryDetails")+25);

		}*/
		LoadPickList("Liability_New_worstStatus24Months","Select '--Select--' as Description,'' as code union select convert(varchar,Description),code from ng_master_Aecb_Codes with(nolock) order by code");		
		LoadPickList("contractType","Select '--Select--' as Description,'' as code union select convert(varchar,Description),code from ng_master_contract_type with(nolock) order by code");
		//RLOS.mLogger.info("RLOSCommon OUTSIDE Liability_container:"); 
	}

	public void adjustFrameTops(String frameList){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try{
			/*String[] framelist_array=frameList.split(",");
			if(framelist_array.length<2)*/
			List<String> frameList_arr = new ArrayList<String>(Arrays.asList(frameList.split(",")));
			if(frameList_arr.size()<2)
				return;

			for(int i=1;i<frameList_arr.size();i++){
				//RLOS.mLogger.info("Frame state for "+frameList_arr.get(i)+": "+formObject.getNGFrameState(frameList_arr.get(i)));
				//RLOS.mLogger.info("height for "+frameList_arr.get(i)+" : "+formObject.getHeight(frameList_arr.get(i)));
				//RLOS.mLogger.info("frame visibility "+ frameList_arr.get(i)+" : "+formObject.isVisible(frameList_arr.get(i)));

				if(formObject.isVisible(frameList_arr.get(i))){
					if(formObject.getNGFrameState(frameList_arr.get(i-1))==0 || formObject.getNGFrameState(frameList_arr.get(i-1))==-1){//frame expanded
						//RLOS.mLogger.info("Inside the frame expanded function"+frameList_arr.get(i)+": "+formObject.getNGFrameState(frameList_arr.get(i)));
						formObject.setTop(frameList_arr.get(i), formObject.getTop(frameList_arr.get(i-1))+formObject.getHeight(frameList_arr.get(i-1))+30);
					}
					else{//frame not expanded
						//RLOS.mLogger.info("outside the frame expanded function"+frameList_arr.get(i)+": "+formObject.getNGFrameState(frameList_arr.get(i)));
						formObject.setTop(frameList_arr.get(i), formObject.getTop(frameList_arr.get(i-1))+30);
					}
				}
				else{
					//RLOS.mLogger.info("Frame not visible: "+ frameList_arr.get(i));
					frameList_arr.remove(i);
				}
			}
		}catch(Exception e){
			RLOS.mLogger.info("Exception occurred in adjustFrameTops()"+e.getMessage());
			printException(e);
		}
	}
	
	public void checkforCurrentAccounts()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try{
			String query = "select acctid from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where Wi_Name = '"+formObject.getWFWorkitemName()+"' and AcctType not like '%CHARGE RECEIVABLE%'  and AcctStat='Active'";//query modified by akshay on 5/7/18 for proc 11633
			//RLOS.mLogger.info("query for current accounts is: "+query);
			List<List<String>> accounts = formObject.getDataFromDataSource(query);
			//RLOS.mLogger.info("value of noOfAccts is: "+query);
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
			/*if(count==1){
				formObject.setNGValue("Account_Number",accounts.get(0).get(0));
			}*/
			formObject.setNGValue("AccountCount", count);
			if(count>0){
				formObject.setEnabled("AlternateContactDetails_retainAccount", false);
				formObject.setNGValue("ExistingCustomerAccount", "Y");
			}
			else{
				formObject.setEnabled("AlternateContactDetails_retainAccount", true);
				formObject.setNGValue("ExistingCustomerAccount", "N");
			}
						
		}
		catch(Exception ex){
			//RLOS.mLogger.info("Exception occurred in finding current accounts");
			printException(ex);
		}
	}
	
	//below code added by nikhil for drop-4 delivery
	public void EID_Visa_date(FormReference formObject)
	{
		String visa_issue =formObject.getNGValue("cmplx_Customer_VisaIssuedate");
		String id_issue=formObject.getNGValue("cmplx_Customer_IdIssueDate");
		if(!"".equalsIgnoreCase(visa_issue) && !"".equalsIgnoreCase(id_issue))
		{
			formObject.setNGValue("cmplx_Customer_VisaIssuedate", id_issue);
			formObject.setNGValue("cmplx_Customer_IdIssueDate", id_issue);
		}
		else if(!"".equalsIgnoreCase(visa_issue) && "".equalsIgnoreCase(id_issue))
		{
			formObject.setNGValue("cmplx_Customer_VisaIssuedate", visa_issue);
			formObject.setNGValue("cmplx_Customer_IdIssueDate", "");

		}
		else if("".equalsIgnoreCase(visa_issue) && !"".equalsIgnoreCase(id_issue))
		{
			//for expact customer
			if((!"AE".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_Nationality"))) && "Y".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_ResidentNonResident")))
			{
				formObject.setNGValue("cmplx_Customer_VisaIssuedate", id_issue);
			}
			else
			{
				formObject.setNGValue("cmplx_Customer_VisaIssuedate", "");	
			}

			formObject.setNGValue("cmplx_Customer_IdIssueDate", id_issue);
		}
	}

	public void expandDecision(){
		try{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//below code commented on 25/7 to reduce load on decision history and distributed to another fragment!
		/*if(!formObject.isVisible("IncomeDetails_Frame1"))
		{
			formObject.fetchFragment("Incomedetails", "IncomeDetails", "q_IncomeDetails");
			RLOSCommonCode.visibilityFrameIncomeDetails(formObject);
			RLOS.mLogger.info(" fetched income details");
			//formObject.setTop("Incomedetails",formObject.getTop("ProductDetailsLoader")+formObject.getHeight("ProductDetailsLoader"));
		}*/
		//below code commented on 25/7 to reduce load on decision history and distributed to another fragment!
		//added by akshay on 15/1/18
		/*if((!formObject.isVisible("EMploymentDetails_Frame1"))){//employment tab
			formObject.fetchFragment("EmploymentDetails", "EMploymentDetails", "q_EmploymentDetails");
			//change by saurabh on 1St Dec for Tanshu points.
			formObject.setTop("Eligibility_Emp", 460);
			formObject.setTop("WorldCheck_fetch", 500);
			RLOS.mLogger.info(" fetched employment details");
		}

		if(formObject.isVisible("CompanyDetails_Frame1") ==false && NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SelfEmployed").equals(formObject.getNGValue("EmploymentType"))){
			formObject.fetchFragment("CompanyDetails", "CompanyDetails", "q_CompanyDetails");
		}*/	
		//RLOS.mLogger.info("service request: "+formObject.isVisible("ParentTab_tab_7"));

		if((!formObject.isVisible("CC_Loan_Frame1"))){//service req tab
			formObject.fetchFragment("CC_Loan_container", "CC_Loan", "q_CC_Loan");
			//RLOS.mLogger.info(" fetched service request");
		}

		//openDemographicTabs();
		//below code commented on 25/7 to reduce load on decision history!
		if(!formObject.isVisible("IncomingDocNew_Frame1")){
			formObject.fetchFragment("IncomingDocuments","IncomingDocNew","q_incomingDocNew");
			formObject.setNGFrameState("IncomingDocuments",0 );
			//RLOS.mLogger.info("RLOS Initiation Inside fetchIncomingDocRepeater first fetched then function is called");
			fetchIncomingDocRepeaterNew();
		}

		formObject.fetchFragment("DecisionHistoryContainer", "DecisionHistory", "q_DecisionHistory");
		//25/07/2017  for mandatory checks fragments should be fetched first

		/*String countCurrentAccount=formObject.getNGDataFromDataCache("SELECT count(acctId) from ng_RLOS_CUSTEXPOSE_AcctDetails where AcctType='CURRENT ACCOUNT' AND Wi_Name='"+formObject.getWFWorkitemName()+"' ").get(0).get(0);
		RLOS.mLogger.info(countCurrentAccount);
		formObject.setNGValue("ExistingCustomerAccount",countCurrentAccount);

		//Deepak 13 Nov 2017 changes done for create Cif visible
		if("0".equals(countCurrentAccount))
		{
			RLOS.mLogger.info("inside visibility true");
			formObject.setVisible("DecisionHistory_Button3", true);//Create CIF/Acc
		}
		else{
			RLOS.mLogger.info("inside visibility false");
			formObject.setVisible("DecisionHistory_Button3", false);//Create CIFAcc
		}*/
		//added Tanshu Aggarwal(22/06/2017)
		if	(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Y").equalsIgnoreCase(formObject.getNGValue("Is_Customer_Req")) && NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Y").equalsIgnoreCase(formObject.getNGValue("Is_Account_Create")))
		{
			//RLOS.mLogger.info( "inside customer req Y");
			formObject.setLocked("DecisionHistory_Button3", true);
		}
		else{
			//RLOS.mLogger.info( "inside customer req N");
			formObject.setLocked("DecisionHistory_Button3", false);
		}

		if ("A".equalsIgnoreCase(formObject.getNGValue("DectechCategory"))){
			formObject.setLocked("cmplx_DecisionHistory_Decision", true);
			formObject.setNGValue("cmplx_DecisionHistory_Decision", "Reject");
		}
	}catch(Exception ex){
		RLOS.mLogger.info("Exceptiion in fetching decision"+printException(ex));
	}
		//added Tanshu Aggarwal(22/06/2017)
	}

	public void openDemographicTabs()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		if(!formObject.isVisible("AddressDetails_Frame1")){
			formObject.fetchFragment("Address_Details_container", "AddressDetails", "q_AddressDetails");				
			formObject.setNGFrameState("Address_Details_container", 0);

			//RLOS.mLogger.info(" fetched address details");
		}

		if(!formObject.isVisible("AltContactDetails_Frame1")){
			formObject.fetchFragment("Alt_Contact_container", "AltContactDetails", "q_AltContactDet");
			formObject.setNGFrameState("Alt_Contact_container", 0);

			//RLOS.mLogger.info("fetched address details");
		}
		//RLOS.mLogger.info("value of CC waiver require : "+formObject.getNGValue("is_cc_waiver_require"));
		//RLOS.mLogger.info("card details visibility : "+formObject.isVisible("CardDetails_Frame1"));
		if(!formObject.isVisible("CardDetails_Frame1") && !"Y".equalsIgnoreCase(formObject.getNGValue("is_cc_waiver_require"))){
			formObject.fetchFragment("CardDetails_container", "CardDetails", "q_CardDetails");
			formObject.setNGFrameState("CardDetails_container", 0);

			//RLOS.mLogger.info("fetched card details");
		}

		if(!formObject.isVisible("SupplementCardDetails_Frame1") && ("Yes".equals(formObject.getNGValue("cmplx_CardDetails_SuppCardReq")) || "Y".equals(formObject.getNGValue("IS_SupplementCard_Required")))){
			formObject.setVisible("Supplementary_Container", true);
			formObject.fetchFragment("Supplementary_Container", "SupplementCardDetails", "q_SuppCardDetails");
			formObject.setNGFrameState("Supplementary_Container", 0);
			//RLOS.mLogger.info("fetched Supplementary details");
		}
		//condition added by akshay on 30/1/18 to handle supplement tab on decision tab
		if(!formObject.isVisible("FATCA_Fatca_Frame1")){
			formObject.fetchFragment("FATCA_container", "FATCA", "q_FATCA");
			formObject.setNGFrameState("FATCA_container", 0);
			//formObject.setTop("Supplementary_Container",formObject.getTop("CardDetails_container")+formObject.getHeight("CardDetails_container")+10);
			//formObject.setTop("FATCA", formObject.getTop("CardDetails_container")+formObject.getHeight("CardDetails_container")+10);
			//RLOS.mLogger.info("fetched FATCA details top: "+formObject.getTop("FATCA"));
			//RLOS.mLogger.info("fetched FATCA details");
		}

		if(!formObject.isVisible("KYC_Frame7")){
			formObject.fetchFragment("KYC_container", "KYC", "q_KYC");
			formObject.setNGFrameState("KYC_container", 0);

			//RLOS.mLogger.info("fetched address details");
		}


		if(!formObject.isVisible("OECD_Frame8")){
			formObject.fetchFragment("OECD_container", "OECD", "q_OECD");
			formObject.setNGFrameState("OECD_container", 0);
			//formObject.setTop("OECD", formObject.getTop("KYC")+formObject.getHeight("KYC")+30);
			//RLOS.mLogger.info("fetched address details");
		}

		if(!formObject.isVisible("ReferenceDetails_Frame1")){
			formObject.fetchFragment("ReferenceDetails_container", "ReferenceDetails", "q_ReferenceDetails");
			formObject.setNGFrameState("ReferenceDetails_container", 0);
			//RLOS.mLogger.info("fetched reference details");
		}
		if(formObject.isVisible("SupplementCardDetails_Frame1")){
			adjustFrameTops("Address_Details_container,Alt_Contact_container,CardDetails_container,Supplementary_Container,FATCA_container,KYC_container,OECD_container,ReferenceDetails_container");
		}
		else{
			adjustFrameTops("Address_Details_container,Alt_Contact_container,CardDetails_container,FATCA_container,KYC_container,OECD_container,ReferenceDetails_container");
		}
	}


	public void FieldsVisibility_CardDetails(FormReference formObject)
	{
		if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Salaried").equalsIgnoreCase(formObject.getNGValue("EmploymentType")) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Salariedpensioner").equalsIgnoreCase(formObject.getNGValue("EmploymentType")) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Pensioner").equalsIgnoreCase(formObject.getNGValue("EmploymentType"))){

			if("KALYAN-EXPAT".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 5)) ||
					"KALYAN-PRIORITY".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 5)) ||
					"KALYAN-SEC".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 5)) ||
					"KALYAN-STAFF".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 5)) ||
					"KALYAN-UAE".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 5)) ||
					"KALYAN-VVIPS".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 5)))
			{
				formObject.setVisible("CardDetails_Label3", true);
				formObject.setVisible("cmplx_CardDetails_CompanyEmbossingName", true);//added by akshay on 11/9/17
				formObject.setLeft("CardDetails_Label5", 552);
				formObject.setLeft("cmplx_CardDetails_SuppCardReq", 552);
				formObject.setVisible("CardDetails_Label2", false);
				formObject.setVisible("cmplx_CardDetails_CharityOrg", false);
				formObject.setVisible("CardDetails_Label4", false);
				formObject.setVisible("cmplx_CardDetails_CharityAmount", false);
			}
			else
			{
				formObject.setVisible("CardDetails_Label3", false);
				formObject.setVisible("cmplx_CardDetails_CompanyEmbossingName", false);//added by akshay on 11/9/17
				formObject.setLeft("CardDetails_Label5", 552);
				formObject.setLeft("cmplx_CardDetails_SuppCardReq", 552);
			}
			formObject.setVisible("CardDetails_Label2", false);
			formObject.setVisible("cmplx_CardDetails_CharityOrg", false);
			formObject.setVisible("CardDetails_Label4", false);
			formObject.setVisible("cmplx_CardDetails_CharityAmount", false);
		}	
		/*if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard").equalsIgnoreCase(formObject.getNGValue("PrimaryProduct"))){
		formObject.setLeft("CardDetails_Label5", 552);
		formObject.setLeft("cmplx_CardDetails_SuppCardReq", 552);
	}*/
		//below code uncommented by 15/1/18
		//if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SelfEmployed").equalsIgnoreCase(formObject.getNGValue("empType")))
		else if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SelfEmployed").equalsIgnoreCase(formObject.getNGValue("EmploymentType")))
		{
			formObject.setVisible("CardDetails_Label3", true);
			formObject.setVisible("cmplx_CardDetails_CompanyEmbossingName", true);//added by akshay on 11/9/17
			formObject.setLeft("CardDetails_Label5", 552);
			formObject.setLeft("cmplx_CardDetails_SuppCardReq", 552);
			formObject.setVisible("CardDetails_Label2", false);
			formObject.setVisible("cmplx_CardDetails_CharityOrg", false);
			formObject.setVisible("CardDetails_Label4", false);
			formObject.setVisible("cmplx_CardDetails_CharityAmount", false);
		}

		IslamicFieldsvisibility();
		if("Y".equalsIgnoreCase(formObject.getNGValue("is_cc_waiver_require"))){
			formObject.setLocked("CardDetails_Frame1",true);
		}
		
		
	}
	public void setDataInMultipleAppGrid(){
		//RLOS.mLogger.info("Inside setDataInMultipleAppGrid() function");
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			//if(formObject.isVisible("cmplx_DecisionHistory_MultipleApplicantsGrid")){
			//un-commenetd by nikhil to refresh data on done
			formObject.clear("cmplx_DecisionHistory_MultipleApplicantsGrid");
			//}
			//added by akshay on 27/2/18 for drop4
			if(formObject.isVisible("GuarantorDetails_Frame1")==false && NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_PersonalLoan").equalsIgnoreCase(formObject.getNGValue("PrimaryProduct")) && !"".equals(formObject.getNGValue("cmplx_Customer_age")) && Float.parseFloat(formObject.getNGValue("cmplx_Customer_age"))<21)
			{
				//RLOS.mLogger.info("Before guarantor fetch");
				formObject.fetchFragment("GuarantorDetails", "GuarantorDetails", "q_GuarantorDetails");
				formObject.setNGFrameState("GuarantorDetails", 0);
				adjustFrameTops("GuarantorDetails,Incomedetails");
			}
			//RLOS.mLogger.info("MultipleApplicantsGrid rowcount: "+formObject.getLVWRowCount("cmplx_DecisionHistory_MultipleApplicantsGrid"));
			
			if(formObject.getLVWRowCount("cmplx_DecisionHistory_MultipleApplicantsGrid")==0){
				List<List<String>> mylist=new ArrayList<List<String>>();
				List<String> subList=new ArrayList<String>();
				subList.add("Primary");
				subList.add(formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme"));	
				subList.add(formObject.getNGValue("cmplx_Customer_PAssportNo"));
				subList.add(formObject.getNGValue("cmplx_Customer_CIFNO"));
				subList.add(formObject.getWFWorkitemName());
				mylist.add(new ArrayList<String>(subList));
				//RLOS.mLogger.info("mylist 1: "+mylist);
				subList.clear();
				// for guarantor
				int m=formObject.getLVWRowCount("cmplx_GuarantorDetails_cmplx_GuarantorGrid");
				//RLOS.mLogger.info("m:"+m);
				for(int i=0;i<m;i++){
					subList.clear();
					subList.add("Guarantor");
					subList.add(formObject.getNGValue("cmplx_GuarantorDetails_cmplx_GuarantorGrid",i,7)+" "+formObject.getNGValue("cmplx_GuarantorDetails_cmplx_GuarantorGrid",i,9));
					subList.add(formObject.getNGValue("cmplx_GuarantorDetails_cmplx_GuarantorGrid",i,5));
					subList.add(formObject.getNGValue("cmplx_GuarantorDetails_cmplx_GuarantorGrid",i,1));
					subList.add(formObject.getWFWorkitemName());
					mylist.add(new ArrayList<String>(subList));//each time adding we need to give a new list reference or it will pick up the old one and uon clear it will get deleted as well 
					//RLOS.mLogger.info("Inside loop:mylist "+i+" :"+mylist);
				}
				//RLOS.mLogger.info("mylist 3: "+mylist);
				if(mylist.size()>0){
					for(List<String> temp:mylist){
						formObject.addItemFromList("cmplx_DecisionHistory_MultipleApplicantsGrid", temp);
					}
				}
			}		

		}
		catch(Exception e)
		{
			RLOS.mLogger.info( "Exception occurred in Decision History tab:"+e.getMessage()+printException(e));

		}

	}
	//added by saurabh for Active/inactive status for supplementary.
	public void validateStatusForSupplementary(){
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			if(formObject.isVisible("SupplementCardDetails_Frame1")){
				int supplementaryRows = formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid");
				//RLOS.mLogger.info("RLOS supplementary row count: "+supplementaryRows);
				Map<Integer, String> suppGridCardsSelected = new HashMap<Integer, String>();
				if(supplementaryRows>0){
					List<String>selectedCardProds = new ArrayList<String>();
					String query = "SELECT distinct card_product FROM ng_rlos_IGR_Eligibility_CardLimit WITH (nolock) WHERE wi_name='"+formObject.getWFWorkitemName()+"' AND cardproductselect = 'true'";
					//RLOS.mLogger.info("RLOS supplementary query: "+query);
					List<List<String>> records = formObject.getDataFromDataSource(query);
					//RLOS.mLogger.info("RLOS supplementary records: "+records);
					if(records!=null && records.size()>0){
						if(records.get(0) != null){
							for(List<String> cardProd : records){
								selectedCardProds.add(cardProd.get(0));
							}
						}
					}
					//RLOS.mLogger.info("RLOS supplementary selected card prods: "+selectedCardProds);
					for(int j=0;j<supplementaryRows;j++){
						//RLOS.mLogger.info("value for cardproduct for row: "+j+" is: "+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid", j, 22));
						if(selectedCardProds.contains(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",j,22))){
							//suppGridCardsSelected.put(j, "Active");
							formObject.setNGValue("SupplementCardDetails_cmplx_SupplementGrid", j, 27, "Active");
						}
						else{
							formObject.setNGValue("SupplementCardDetails_cmplx_SupplementGrid", j, 27, "InActive");
						}
						//RLOS.mLogger.info("value for status for row: "+j+" is: "+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid", j, 27));
						//below code nikhil 05/12/18 
						//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
						Custom_fragmentSave("Supplementary_Container");
					}
				}
			}
		}catch(Exception e)
		{
			RLOS.mLogger.info( "Exception occurred in validateStatusForSupplementary function:"+e.getMessage()+printException(e));
		}
	}
	public static void loadDynamicPickList()
	{
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			//RLOS.mLogger.info("inside loadDynamicPickList");
			List<String> controls= Arrays.asList("Customer_Type","cmplx_FATCA_CustomerType","KYC_CustomerType","OECD_CustomerType");
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
				//RLOS.mLogger.info("inside loadDynamicPickList pvalue"+pvalue);
				List<String> entries = new ArrayList<String>();
				//Supplementary
				if(formObject.isVisible("SupplementCardDetails_Frame1")){
					int rowCount = formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid");
					if(rowCount>0){
						for(int i=0;i<rowCount;i++){
							String sFname = formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,0);
							String sLname = formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,2);
							String sPass = formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,3);
							sValue = "S-"+sFname+" "+sLname+"-"+sPass;
							if(!entries.contains(sValue)){
								formObject.addItem(controlName,sValue);
								entries.add(sValue);
							}
						}

					}
				}
				//RLOS.mLogger.info("inside loadDynamicPickList sValue"+sValue);

				//Guarantor
				if(formObject.isVisible("GuarantorDetails_Frame1")){
					int rowCount = formObject.getLVWRowCount("cmplx_GuarantorDetails_cmplx_GuarantorGrid");
					if(rowCount>0){
						for(int i=0;i<rowCount;i++){
							String gFname = formObject.getNGValue("cmplx_GuarantorDetails_cmplx_GuarantorGrid",i,7);
							String gLname = formObject.getNGValue("cmplx_GuarantorDetails_cmplx_GuarantorGrid",i,9);
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
					if(formObject.getLVWRowCount("cmplx_GuarantorDetails_cmplx_GuarantorGrid")>0){
						if(guarantorPresent==0){
							//RLOS.mLogger.info("inside condition to replace the guarantor");
							String gridguarName =  formObject.getNGValue("cmplx_GuarantorDetails_cmplx_GuarantorGrid",0,7)+" "+formObject.getNGValue("cmplx_GuarantorDetails_cmplx_GuarantorGrid",0,9);
							String gridguarpass = formObject.getNGValue("cmplx_GuarantorDetails_cmplx_GuarantorGrid",0,5);
							String gridguarcif = formObject.getNGValue("cmplx_GuarantorDetails_cmplx_GuarantorGrid",0,1);
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
							subList.add(formObject.getNGValue("cmplx_GuarantorDetails_cmplx_GuarantorGrid",0,7)+" "+formObject.getNGValue("cmplx_GuarantorDetails_cmplx_GuarantorGrid",0,9));
							subList.add(formObject.getNGValue("cmplx_GuarantorDetails_cmplx_GuarantorGrid",0,5));
							subList.add(formObject.getNGValue("cmplx_GuarantorDetails_cmplx_GuarantorGrid",0,1));
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
				//RLOS.mLogger.info("inside loadDynamicPickList gValue"+gValue);
				//RLOS.mLogger.info("@@@@@Prabhakar"+pvalue);
				//formObject.addItem("Customer_Type",pvalue);Customer_Frame2

			}
		}catch(Exception ex){
			RLOS.mLogger.info( "Exception occurred in validateStatusForSupplementary function:"+printException(ex));
		}
	}

	public void fetch_cardProductMasters(FormReference formObject)
	{
		String subprod=formObject.getNGValue("subProd");
		String VIPFlag="true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_VIPFlag"))?"Y":"N";
		String Nationality=formObject.getNGValue("cmplx_Customer_Nationality");
		String TypeOfProd=formObject.getNGValue("Product_type");
		String empType=formObject.getNGValue("EmpType");
		formObject.clear("CardProd");
		String query1="";
		String query2="";
		if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Salariedpensioner").equalsIgnoreCase(empType) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Salaried").equalsIgnoreCase(empType) || "Pensioner".equalsIgnoreCase(empType)){
			//both query changed by nikgil 28/11 isactive filter.
			//Deepak Nationality changed from null to All in master so tha query also modified accordingly.
			query1="select '--Select--' as Description,'' as Code union select convert(varchar, Description),code from ng_master_cardProduct with (nolock) where Reqproduct='"+TypeOfProd+"' and SUBproduct = '"+subprod+"'  and VIPFlag='"+VIPFlag+"' and Nationality='AE' and EmployerCategory='Salaried' and isActive='Y' order by code";	
			query2="select '--Select--' as Description,'' as Code union select convert(varchar, Description),code from ng_master_cardProduct with (nolock) where Reqproduct='"+TypeOfProd+"' and SUBproduct = '"+subprod+"'  and VIPFlag='"+VIPFlag+"' and Nationality ='ALL' and EmployerCategory='Salaried' and isActive='Y'  order by code"; //Arun (05/12/17) modified to load the corret masters if nationality is null
		}
		else{
			query1="select '--Select--' as Description,'' as Code union select convert(varchar, Description),code from ng_master_cardProduct with (nolock) where Reqproduct='"+TypeOfProd+"' and SUBproduct = '"+subprod+"'  and VIPFlag='"+VIPFlag+"' and Nationality='AE' and EmployerCategory='Self Employed' order by code";	
			query2="select '--Select--' as Description,'' as Code union select convert(varchar, Description),code from ng_master_cardProduct with (nolock) where Reqproduct='"+TypeOfProd+"' and SUBproduct = '"+subprod+"'  and VIPFlag='"+VIPFlag+"' and Nationality ='ALL' and EmployerCategory='Self Employed'  order by code"; //Arun (05/12/17) modified to load the corret masters if nationality is null

		}
		//RLOS.mLogger.info( ": "+Nationality);
		//RLOS.mLogger.info(query1);
		if("AE".equalsIgnoreCase(Nationality)){
			//RLOS.mLogger.info(query1);
			LoadPickList("CardProd",query1);
		}
		else{
			//RLOS.mLogger.info(query2);
			LoadPickList("CardProd",query2);
		}
	}

	public void fetch_SchemeMasters(FormReference formObject)
	{
		String subprod=formObject.getNGValue("SubProd");
		String apptype=formObject.getNGValue("AppType");
		String TypeofProduct=formObject.getNGValue("Product_type");

		if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Conventional").equalsIgnoreCase(TypeofProduct))&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_EXP").equalsIgnoreCase(subprod)&&("TKOE".equalsIgnoreCase(apptype))){

			//RLOS.mLogger.info( "1Value of AppType is:"+formObject.getNGValue("AppType"));
			formObject.clear("Scheme");
			formObject.setNGValue("Scheme","--Select--");
			LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Take Over'  and TypeofProduct='Conventional' order by SCHEMEID");

		}
		else if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Conventional").equalsIgnoreCase(TypeofProduct))&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_EXP").equalsIgnoreCase(subprod)&&("NEWE".equalsIgnoreCase(apptype))){
			//RLOS.mLogger.info( "2Value of AppType is:"+formObject.getNGValue("AppType"));
			formObject.clear("Scheme");
			formObject.setNGValue("Scheme","--Select--");
			LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'New' and TypeofProduct='Conventional' order by SCHEMEID");

		}
		else if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Conventional").equalsIgnoreCase(TypeofProduct)&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_EXP").equalsIgnoreCase(subprod))&&("TOPE".equalsIgnoreCase(apptype))){
			//RLOS.mLogger.info( "3Value of AppType is:"+formObject.getNGValue("AppType"));

			formObject.clear("Scheme");
			formObject.setNGValue("Scheme","--Select--");
			LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Top up' and TypeofProduct='Conventional' order by SCHEMEID");
		}

		//changed by akshay for rechedulement case on 7/2/18	
		else if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Conventional").equalsIgnoreCase(TypeofProduct)&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_EXP").equalsIgnoreCase(subprod))&&(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESC").equalsIgnoreCase(apptype) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESN").equalsIgnoreCase(apptype) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESR").equalsIgnoreCase(apptype))){
			//RLOS.mLogger.info( "4Value of AppType is:"+formObject.getNGValue("AppType"));

			formObject.clear("Scheme");
			formObject.setNGValue("Scheme","--Select--");
			LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Reschedulment' and TypeofProduct='Conventional' order by SCHEMEID");

		}
		else if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Conventional").equalsIgnoreCase(TypeofProduct)&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_NAT").equalsIgnoreCase(subprod))&&(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_TOPN").equalsIgnoreCase(apptype))){
			//RLOS.mLogger.info( "5Value of AppType is:"+formObject.getNGValue("AppType"));
			formObject.clear("Scheme");
			formObject.setNGValue("Scheme","--Select--");
			LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Top up' and TypeofProduct='Conventional' order by SCHEMEID");

		}
		else if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Conventional").equalsIgnoreCase(TypeofProduct)&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_NAT").equalsIgnoreCase(subprod))&&(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_TKON").equalsIgnoreCase(apptype))){
			//RLOS.mLogger.info( "6Value of AppType is:"+formObject.getNGValue("AppType"));
			formObject.clear("Scheme");
			formObject.setNGValue("Scheme","--Select--");
			LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Take Over' and TypeofProduct='Conventional' order by SCHEMEID");

		}
		else if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Conventional").equalsIgnoreCase(TypeofProduct)&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_NAT").equalsIgnoreCase(subprod))&&(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_NEWN").equalsIgnoreCase(apptype))){
			//RLOS.mLogger.info( "7Value of AppType is:"+formObject.getNGValue("AppType"));
			formObject.clear("Scheme");
			formObject.setNGValue("Scheme","--Select--");
			LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'New' and TypeofProduct='Conventional' order by SCHEMEID");

		}
		else if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Conventional").equalsIgnoreCase(TypeofProduct)&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_NAT").equalsIgnoreCase(subprod))&&(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESC").equalsIgnoreCase(apptype) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESN").equalsIgnoreCase(apptype) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESR").equalsIgnoreCase(apptype))){
			//RLOS.mLogger.info( "8Value of AppType is:"+formObject.getNGValue("AppType"));
			formObject.clear("Scheme");
			formObject.setNGValue("Scheme","--Select--");
			LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Reschedulment' and TypeofProduct='Conventional' order by SCHEMEID");

		}
		else if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic").equalsIgnoreCase(TypeofProduct))&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_EXP").equalsIgnoreCase(subprod) &&("TKOE".equalsIgnoreCase(apptype))){

			//RLOS.mLogger.info( "1Value of AppType is:"+formObject.getNGValue("AppType"));

			formObject.clear("Scheme");
			formObject.setNGValue("Scheme","--Select--");
			LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Take Over'  and TypeofProduct='"+NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic")+"' order by SCHEMEID");				
		}

		else if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic").equalsIgnoreCase(TypeofProduct))&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_EXP").equalsIgnoreCase(subprod)&&("NEWE".equalsIgnoreCase(apptype))){
			//RLOS.mLogger.info( "2Value of AppType is:"+formObject.getNGValue("AppType"));
			formObject.clear("Scheme");
			formObject.setNGValue("Scheme","--Select--");
			LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'New' and TypeofProduct='"+NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic")+"' order by SCHEMEID");				
		}

		else if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic").equalsIgnoreCase(TypeofProduct)&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_EXP").equalsIgnoreCase(subprod))&&("TOPE".equalsIgnoreCase(apptype))){
			//RLOS.mLogger.info( "3Value of AppType is:"+formObject.getNGValue("AppType"));

			formObject.clear("Scheme");
			formObject.setNGValue("Scheme","--Select--");
			LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Top up' and TypeofProduct='"+NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic")+"' order by SCHEMEID");

		}
		else if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic").equalsIgnoreCase(TypeofProduct)&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_EXP").equalsIgnoreCase(subprod))&&(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESC").equalsIgnoreCase(apptype) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESN").equalsIgnoreCase(apptype) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESR").equalsIgnoreCase(apptype))){
			//RLOS.mLogger.info( "4Value of AppType is:"+formObject.getNGValue("AppType"));

			formObject.clear("Scheme");
			formObject.setNGValue("Scheme","--Select--");
			LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='Expat' and Applicationtype = 'Reschedulment' and TypeofProduct='"+NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic")+"' order by SCHEMEID");

		}
		else if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic").equalsIgnoreCase(TypeofProduct)&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_NAT").equalsIgnoreCase(subprod))&&(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_TOPN").equalsIgnoreCase(apptype))){
			//RLOS.mLogger.info( "5Value of AppType is:"+formObject.getNGValue("AppType"));
			formObject.clear("Scheme");
			formObject.setNGValue("Scheme","--Select--");
			LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Top up' and TypeofProduct='"+NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic")+"' order by SCHEMEID");


		}
		else if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic").equalsIgnoreCase(TypeofProduct)&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_NAT").equalsIgnoreCase(subprod))&&(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_TKON").equalsIgnoreCase(apptype))){
			//RLOS.mLogger.info( "6Value of AppType is:"+formObject.getNGValue("AppType"));
			formObject.clear("Scheme");
			formObject.setNGValue("Scheme","--Select--");
			LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Take Over' and TypeofProduct='"+NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic")+"' order by SCHEMEID");


		}
		else if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic").equalsIgnoreCase(TypeofProduct)&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_NAT").equalsIgnoreCase(subprod))&&(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_NEWN").equalsIgnoreCase(apptype))){
			//RLOS.mLogger.info( "7Value of AppType is:"+formObject.getNGValue("AppType"));
			formObject.clear("Scheme");
			formObject.setNGValue("Scheme","--Select--");
			LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'New' and TypeofProduct='"+NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic")+"' order by SCHEMEID");


		}
		else if ((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic").equalsIgnoreCase(TypeofProduct)&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_NAT").equalsIgnoreCase(subprod))&&(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESC").equalsIgnoreCase(apptype) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESN").equalsIgnoreCase(apptype) || NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_RESR").equalsIgnoreCase(apptype))){
			//RLOS.mLogger.info( "8Value of AppType is:"+formObject.getNGValue("AppType"));
			formObject.clear("Scheme");
			formObject.setNGValue("Scheme","--Select--");
			LoadPickList("Scheme", "select '--Select--','0' as SCHEMEID union select convert(varchar, SCHEMEDESC),SCHEMEID from ng_MASTER_Scheme with (nolock) where subproduct='National' and Applicationtype = 'Reschedulment' and TypeofProduct='"+NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Islamic")+"' order by SCHEMEID");

		}
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
				//RLOS.mLogger.info("firstname is:"+firstName);
				//RLOS.mLogger.info("lastName is:"+lastName);
				//RLOS.mLogger.info("passport is:"+passport);
				match = "S-"+firstName+" "+lastName+"-"+passport;

			}
			else if(operationName.equalsIgnoreCase("Guarantor_CIF")){
				firstName=formObject.getNGValue("Fname");
				lastName=formObject.getNGValue("lname");
				match = "G-"+firstName+" "+lastName;
			}
			//RLOS.mLogger.info("	"+match);
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
					//RLOS.mLogger.info("appTypeinGrid is:"+appTypeinGrid);
					if(match.equalsIgnoreCase(appTypeinGrid)){
						formObject.setSelectedIndex(grid_control, (i-deleteCount));
						//RLOS.mLogger.info("selectedIndex is:"+formObject.getSelectedIndex(grid_control));
						formObject.ExecuteExternalCommand("NGDeleteRow",grid_control);
						deleteCount++;
						//RLOS.mLogger.info("selectedIndex after deleting is:"+formObject.getSelectedIndex(grid_control));
					}
				}
			}
		}
		catch(Exception ex){
			//RLOS.mLogger.info("Exception in clearSelectiveRows function");
			RLOS.mLogger.info(printException(ex));

		}
	}

	public int returnGridColumnIndex(String GridName,String ColumnName)
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		UIComponent pComp =formObject.getComponent(GridName);
		int columns=0;
		Column c;
		if( pComp != null && pComp instanceof ListView )
		{			
			ListView objListView = ( ListView )pComp;
			columns  = objListView.getChildCount();
			//RLOS.mLogger.info("columns: "+ columns+"");
			for(int i=0;i<columns;i++){
				c=(Column)(pComp.getChildren().get(i));
				if(ColumnName.equals(c.getName())){
					return i;
				}

			}
		}
		return 0;
	}

	public void AddFromHiddenList(String hiddenfieldName,String GridName){
		//RLOS.mLogger.info("inside AddFromHiddenList");
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String ListTobeAdded=formObject.getNGValue(hiddenfieldName);
			UIComponent pComp=formObject.getComponent(GridName);
			if( pComp != null && pComp instanceof ListView )
			{
			if(!ListTobeAdded.equals("") && !ListTobeAdded.equals("#")){
				//RLOS.mLogger.info("ListTobeAdded: "+ListTobeAdded);
				String[] ListTobeAdded_array=ListTobeAdded.split("#");
				for(int i=0;i<ListTobeAdded_array.length;i++){
					List<String> rowTobeAdded = Arrays.asList(ListTobeAdded_array[i].substring(0, ListTobeAdded_array[i].length()).split("~cas_sep~"));
					Clean(rowTobeAdded);
					for(int j=0;j<rowTobeAdded.size();j++){
						if(!((Column)(pComp.getChildren().get(j))).getFormat().equals("")){
							rowTobeAdded.set(j, "");
						}
					}
					//RLOS.mLogger.info("rowTobeAdded: "+ rowTobeAdded);
					formObject.addItemFromList(GridName, rowTobeAdded);
				}
				formObject.clear(hiddenfieldName);
			}
		}
		}
		catch(Exception ex){
			//RLOS.mLogger.info("Exception in AddFromHiddenList function");
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
	
	public void CustomSaveForm(){
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();

			String sampleString = formObject.getNGValue("FrameName");
			String[] items = sampleString.split(",");
			RLOS.mLogger.info("Custom save form ::"+sampleString);
			for (String item : items) {
				RLOS.mLogger.info("Custom save form items::"+item.toString());
				if(!"".equalsIgnoreCase(item)){
					//changed by nikhil 25/11 to save notepad details as replacing frame name with its container to save.
			    	  if("NotepadDetails_Frame1".equals(item))
			    	  {
			    		  item="Notepad_Values";
			    	  }
					 RLOS.mLogger.info("Custom save form items::"+item.toString());
					formObject.saveFragment(item);  
				}
			}
			formObject.saveFragment("IncomingDocuments");
			formObject.setNGValue("FrameName","");
		}
		catch(Exception e){
			RLOS.mLogger.info("Exception occured in CustomSaveForm::"+e.getMessage());
		}
	}
	
	public String getKeyByValue(Map hm, String value) {
		//RLOS.mLogger.info("Inside getKeyByValue-->Arguement Value: "+value);

		List<String> keys=new ArrayList(hm.keySet());
		List<String> values =new ArrayList(hm.values());
		int matchingindex=-1;
		for(String iterator:values){
			if(value.equalsIgnoreCase(iterator)){
				matchingindex=values.indexOf(iterator);
				break;
			}
		}
		//RLOS.mLogger.info("matchingindex, Matching key is: "+matchingindex+", "+keys.get(matchingindex));
		if(matchingindex!=-1){
			return keys.get(matchingindex);
		}
	return "";	
	}
	public void pickListMasterLoad(String buttonName){
		try{
			String val=NGFUserResourceMgr_RLOS.getMasterManager(buttonName);
			String[] value= val.split(":");
			String query;
			String stableName="";
			String sColumnName="";
			String sfieldName="";
			String sHeaderName="";
			//RLOS.mLogger.info("Invalid entry value.length for :"+val);
			//RLOS.mLogger.info("Invalid entry value.length for :"+value.length);
			if(value.length==4){
				stableName=value[0];
				sColumnName=value[1];
				sfieldName=value[2];
				sHeaderName=value[3];
				query="select "+sColumnName+" from "+stableName+" with (nolock)  where isActive='Y'";
				populatePickListWindow(query,sfieldName,sColumnName, true, 20,sHeaderName);	
			}
			else{
				//RLOS.mLogger.info("Invalid entry maintained for :"+buttonName);
			}
			
					     

		}
		catch(Exception e){
			RLOS.mLogger.info("PLCommon Exception Occurred genericMaster :"+e.getMessage());
			printException(e);
		}
		
	}
	//Deepak 24 Dec, New method added to handle invalid session in fragment save event.
	public void Custom_fragmentSave(String fragment_name){
		 String alert_msg="";
		 try{
			 FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			 String return_Arr[] = formObject.saveFragment(fragment_name);
				if(return_Arr.length>0 && !"0".equalsIgnoreCase(return_Arr[0])){
					if("11".equalsIgnoreCase(return_Arr[0])){
						alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL059");
					}
					else{
						alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL060");
					}
				}
				else if (return_Arr.length==0){
					alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL060");
				}
		 }
		 catch(Exception e){
			 alert_msg=NGFUserResourceMgr_RLOS.getAlert("VAL060");
		 }
		 if(!"".equalsIgnoreCase(alert_msg)){
			 throw new ValidatorException(new FacesMessage(alert_msg));
		 }
	 }
	//below code added by nikhil for PCSP-427
	//changed by nikhil for islamic cases
	@SuppressWarnings("finally")
	public void Check_EFC_Limit()
	{
		RLOS.mLogger.info("Inside Check_EFC_Limit");
		String alerttxt = "";
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		List<List<String>> CardTypeandLimitXML=null;
		try
		{
			//Deepak Changes done for production issue when card is selected and no final limit is entered - 1 July 2019
			//String query="select Card_Product,Final_Limit,Cardproductselect,combined_limit from ng_rlos_IGR_Eligibility_CardLimit with (nolock) where wi_name='"+formObject.getWFWorkitemName()+"'";
			String query="select Card_Product,case when isnull(Ltrim(Final_Limit),0)='' then 0 else isnull(Final_Limit,0) end as Final_Limit,Cardproductselect,combined_limit from ng_rlos_IGR_Eligibility_CardLimit with (nolock) where wi_name='"+formObject.getWFWorkitemName()+"'";
			
			RLOS.mLogger.info("Inside Check_EFC_Limit : "+ query);
			CardTypeandLimitXML=formObject.getDataFromDataSource(query);
			double FinalLimit=0;
			if (CardTypeandLimitXML != null){

				int cardselected=0;
				
				double Conv=0;
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
				RLOS.mLogger.info("Inside Check_EFC_Limit Cardselected : "+ cardselected);
				RLOS.mLogger.info("Inside Check_EFC_Limit FinalLimit: "+ FinalLimit);
				if(CardTypeandLimitXML.size()>0 && cardselected==0 )
				{
					alerttxt = NGFUserResourceMgr_RLOS.getAlert("VAL062");
				}
				else if(CardTypeandLimitXML.size()>0 && cardselected>0 && !"Y".equalsIgnoreCase(formObject.getNGValue("Is_Principal_Approval")))
				{
					if(Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"))!=FinalLimit)
					{
						alerttxt = NGFUserResourceMgr_RLOS.getAlert("VAL061");
					}

				}
			}
			// Deepak 25 Aug 2019 Query corrected as per JIRA PCAS-2741
			 //query="select isnull(max(CreditLimit),'') from ng_RLOS_CUSTEXPOSE_CardDetails with (nolock) where  Child_wi='"+formObject.getWFWorkitemName()+"'";
			query="select isnull(max(CreditLimit),0) as conventionalCreditLimit from (select CreditLimit,case when SchemeCardProd is null then cardtype else SchemeCardProd end as cardprd from ng_RLOS_CUSTEXPOSE_CardDetails with (nolock) where Child_wi='"+formObject.getWFWorkitemName()+"') as temp_EXPOSE_CardDetails where"+
				 	"cardprd not in(select distinct code from ng_master_cardproduct where ReqProduct = 'islamic')";
			RLOS.mLogger.info("Query to validate conventionalCreditLimit & final Limit is:"+query);
			List<List<String>> result=formObject.getDataFromDataSource(query);
			if(!result.isEmpty())
			{
				if(Double.parseDouble(result.get(0).get(0))>FinalLimit &&  !"Y".equalsIgnoreCase(formObject.getNGValue("Is_Principal_Approval")))
				{
					alerttxt = NGFUserResourceMgr_RLOS.getAlert("VAL065");
				}
			}
		}
		catch(Exception ex)
		{
			RLOS.mLogger.info("Inside Check_EFC_Limit exception"+ex.getStackTrace());
		}
		if(!alerttxt.equalsIgnoreCase("")){
			
			throw new ValidatorException(new FacesMessage(alerttxt));
		}
		
		
	}
	//++below code added by nikhil for Self-Supp CR
	public void Load_Self_Supp_Data()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		if("Yes".equalsIgnoreCase(formObject.getNGValue("cmplx_CardDetails_SelfSupp_required")))
		{
			formObject.setEnabled("cmplx_CardDetails_Selected_Card_Product", true);
			formObject.setEnabled("cmplx_CardDetails_SelfSupp_Limit", true);
			LoadPickList("cmplx_CardDetails_Selected_Card_Product", "SELECT CASE WHEN temptble.Description!='--Select--' then concat(temptble.Description,'(',temptble.final_limit,')') else temptble.Description end,temptble.Description FROM (SELECT '--Select--' as Description,'' as final_limit union SELECT distinct card_product as Description,final_limit FROM ng_rlos_IGR_Eligibility_CardLimit WITH (nolock) WHERE wi_name = '"+formObject.getWFWorkitemName()+"' AND cardproductselect = 'true') as temptble order by case when Description='--Select--' then 0 else 1 end");
			
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
	public List<List<String>> get_Avl_Cards()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		List<List<String>> Avl_Cards=null;
		try
		{
		String Avl_card_Query="select Card_Product from ng_rlos_IGR_Eligibility_CardLimit where Cardproductselect='true' and wi_name='"+formObject.getWFWorkitemName()+"'";
		Avl_Cards = formObject.getDataFromDataSource(Avl_card_Query);
		}
		catch (Exception ex)
		{
			RLOS.mLogger.info("Exception in get_Avl_Cards data"+ex.getMessage());
		}
		finally
		{
			return Avl_Cards;
		}
		
	}
	/*public void Load_AVL_cards()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try
		{
			List<List<String>> Avl_Cards = get_Avl_Cards();
			if(Avl_Cards!=null && Avl_Cards.size()>0)
			{
				for(int sel_card=0;sel_card<Avl_Cards.size();sel_card++)
				{
					formObject.addItem("CardDetails_Avl_Card_Product", Avl_Cards.get(sel_card));
				}
			}
			
		}
		catch (Exception ex)
		{
			RLOS.mLogger.info("Exception in loading Load_AVL_cards"+ex.getMessage());
		}
	}
	public void Load_SEL_Cards()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String Selected_cards=formObject.getNGValue("cmplx_CardDetails_Selected_Card_Product");
		try
		{
			if(!"".equalsIgnoreCase(Selected_cards))
			{
				String[] Cards=Selected_cards.split(",");
				for(String Card : Cards)
				{
					formObject.addItem("CardDetails_Sel_Card_Product", Card);
				}
			}
		}
		catch (Exception ex)
		{
			RLOS.mLogger.info("Exception in loading Load_AVL_cards"+ex.getMessage());
		}
		
	}*/
	//change in self-supp 20-08-2019
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
	//--above code added by nikhil for Self-Supp CR
	
	public  String getLinkedProduct (String outxml){
		String LinkedProduct="";
		try{
			outxml=outxml.substring(outxml.indexOf("<MQ_RESPONSE_XML>")+17,outxml.indexOf("</MQ_RESPONSE_XML>"));
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(outxml));
			Document doc = builder.parse(is);
			doc.getDocumentElement().normalize();
			NodeList productList = doc.getElementsByTagName("Products");
			
			for (int prdcount = 0; prdcount < productList.getLength(); prdcount++) {
				if(prdcount==0){
					LinkedProduct = productList.item(prdcount).getFirstChild().getTextContent();
				}else{
					LinkedProduct = LinkedProduct+","+productList.item(prdcount).getFirstChild().getTextContent();
				}
			}
			RLOS.mLogger.info("LinkedProduct: "+LinkedProduct);
		}
		catch(Exception e){
			RLOS.mLogger.info("Exception occured in getLinkedProduct: "+e.getMessage());
		}
		return LinkedProduct;
	}	
	//++below code added by nikhil for PCAS-1212 CR
	public void Update_Office_Address()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		openDemographicTabs();
		String Grid_name="cmplx_AddressDetails_cmplx_AddressGrid";
		int Grid_row_count=formObject.getLVWRowCount(Grid_name);
		Boolean Is_Office=false;
		int office_index=0;
		String Employer_Name=formObject.getNGValue("cmplx_EmploymentDetails_EmpName")==null?"":formObject.getNGValue("cmplx_EmploymentDetails_EmpName");
		String Designation_code=formObject.getNGValue("cmplx_Customer_Designation")==null?"":formObject.getNGValue("cmplx_Customer_Designation");
		String Designation_desc=getCodeDesc("NG_MASTER_Designation", "Description", Designation_code);
		String Fname=formObject.getNGValue("cmplx_Customer_FIrstNAme");
		String Lname=formObject.getNGValue("cmplx_Customer_LAstNAme");
		String Customer_type="P-"+Fname+" "+Lname;
		for(int i=0;i<Grid_row_count;i++)
		{
			if("OFFICE".equalsIgnoreCase(formObject.getNGValue(Grid_name, i, 0)))
			{
				Is_Office=true;
				office_index=i;
				break;
			}
		}
		LoadPickListSource("Customer_Type","SELECT * FROM (SELECT '--Select--' AS customername UNION ALL  SELECT 'P-'+CAST(FirstName+' '+LAstName AS VARCHAR(200))  FROM ng_RLOS_Customer with(nolock) where wi_name='"+formObject.getWFWorkitemName()+"'   UNION ALL SELECT 'G-'+firstname+' '+lastname as customername FROM ng_RLOS_GR_GuarantorDetails with(nolock) where guarandet_wi_name='"+formObject.getWFWorkitemName()+"' and firstname IS NOT NULL  union ALL SELECT distinct 'S-'+FistName+' '+lastname+'-'+passportNo as customername FROM ng_RLOS_GR_SupplementCardDetails with(nolock) WHERE  supplement_WIname='"+formObject.getWFWorkitemName()+"') as u where customername is not null order by case when customername = '--Select--' then 0 else 1 end");
		if(Is_Office)//if row is there
		{
			if(!Designation_desc.equalsIgnoreCase(formObject.getNGValue(Grid_name, office_index, 2)))
			{
				formObject.setNGValue(Grid_name, office_index, 2, Designation_desc);
			}
			if(!Employer_Name.equalsIgnoreCase(formObject.getNGValue(Grid_name, office_index, 3)))
			{
				formObject.setNGValue(Grid_name, office_index, 3, Employer_Name);
			}
			if(!Customer_type.equalsIgnoreCase(formObject.getNGValue(Grid_name, office_index, 13)))
			{
				formObject.setNGValue(Grid_name, office_index, 13, Customer_type);
			}

		}
		else // if no row there 
		{
			formObject.setNGValue("buildname", Employer_Name);
			formObject.setNGValue("house", Designation_desc);
			formObject.setNGValue("addtype", "OFFICE");
			formObject.setNGValue("Customer_Type", Customer_type);
			formObject.setNGValue("AddressDetails_address_Wi_name", formObject.getWFWorkitemName());//Deepak changes done on 14 Aug to add workitem no as well for NTB case.
			formObject.ExecuteExternalCommand("NGAddRow", "cmplx_AddressDetails_cmplx_AddressGrid");
		}

	}
	public String getCodeDesc(String Master_Name,String Col_name,String Code){
		String desc = "";
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String query="select "+Col_name+" from "+Master_Name+" with (nolock) where code='"+Code+"' AND isActive='Y'";
			RLOS.mLogger.info("query name :"+query);
			List<List<String>> result=formObject.getDataFromDataSource(query);
			if(!result.isEmpty()){
				if(null!=result.get(0).get(0) && !result.get(0).get(0).equals("")){
					desc = result.get(0).get(0);
				}
			}
		}
		catch(Exception e){
			RLOS.mLogger.info("CC Integration exception in getCodeDesc: "+ e.getMessage());
		}
		return desc;
		
	}
	//--above code added by nikhil for PCAS-1212 CR
	//Method added by Deepak to check supplementary coming for Primary Card
	public boolean supplementry_to_PrimaryCheck(){
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String Cif = formObject.getNGValue("cmplx_Customer_CIFNO");
			String ntb_cust = formObject.getNGValue("cmplx_Customer_NTB");
			if("false".equalsIgnoreCase(ntb_cust)&& !"".equalsIgnoreCase(Cif)){
				String query="select case when SUBSTRING(CRN,8,9)=00 then 'primary' else 'supplementary' end from CAPS_MAIN_MIG_DATA with(nolock) where CIF='"+Cif+"' order by CRN";
				RLOS.mLogger.info("query name :"+query);
				List<List<String>> result=formObject.getDataFromDataSource(query);
				if(!result.isEmpty()){
					String result_Str = result.toString();
					if(!result_Str.contains("primary")){
						formObject.setNGValue("cmplx_Customer_NTB","true");
						formObject.setNGValue("cmplx_Customer_CIFNO","");
						formObject.clear("q_cif_detail");
						formObject.RaiseEvent("WFSave");
						return true;
					}
				}
				else{
					 return false;
				}
			}
			else{
				return false;
			}
		}
		catch(Exception e){
			return false;
		}
		return false;
	}
	//added by nikhil for PCAS-2408 CR
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
		RLOS.mLogger.info("Check_Elite_Customer : Nationality :"+ Nationality);
		RLOS.mLogger.info("Check_Elite_Customer: CustomerSubSeg : "+ CustomerSubSeg);
		RLOS.mLogger.info("Check_Elite_Customer : Salary :"+ Salary);
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
	//Method added by Deepak for product dedupe check
		public void Product_DedupeCheck(){
			boolean Product_Dedupeflag=false;
			try{
				FormReference formObject = FormContext.getCurrentInstance().getFormReference();
				if(formObject.getNGValue("cmplx_Customer_NTB").equals("false")){
			        List<String> objInput=new ArrayList<String>();
			        List<Object> objOutput=new ArrayList<Object>();
			        objInput.add("Text:" + formObject.getWFWorkitemName());
			        
			        int Prod_rowCount=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			        String SubProd="";
			        String AppType="";
			        if(Prod_rowCount>0){
			            SubProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2);
			            AppType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,4);
			        }    
			        if((NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_TOPN").equalsIgnoreCase(AppType))||(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_TOPE").equalsIgnoreCase(AppType))||
			                (NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_LimitIncrease_code").equalsIgnoreCase(SubProd))||
			                (NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_IM").equalsIgnoreCase(SubProd)&&(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_IM_Additional").equalsIgnoreCase(AppType)||NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_IM_TopUp").equalsIgnoreCase(AppType)))){
			            RLOS.mLogger.info("RLOS Comman: Product dedupe will not run as SubProd: "+SubProd+" AppType: "+AppType);
			        }
			        else{
			            
			            objOutput.add("Text");
			            //RLOS.mLogger.info("objInput args is: " + formObject.getWFWorkitemName());

			            objOutput = formObject.getDataFromStoredProcedure("ng_RLOS_DedupeCheck", objInput, objOutput);
			            RLOS.mLogger.info("objOutput args is: " + (String) objOutput.get(0));
			            formObject.setNGValue("dedupecheck", (String) objOutput.get(0));
			            if (formObject.getNGValue("dedupecheck").equalsIgnoreCase("Y")){
			            	RLOS.mLogger.info("product dedupe positve");
			            	Product_Dedupeflag=true;
			            }
			            else{
			            	RLOS.mLogger.info("product dedupe positve");
			            }
			        }
			       
			    }
				else{
					RLOS.mLogger.info("NTB customer so no product dedupe");
				}

			}
			catch(Exception e){
				RLOS.mLogger.info("exception occured inside Product_DedupeCheck: "+ e.getMessage());
			}
			if(Product_Dedupeflag){
				throw new ValidatorException(new FacesMessage(NGFUserResourceMgr_RLOS.getAlert("VAL057")));
			}
		}
}
