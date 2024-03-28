/*
 * Change History:
 * Deepak 31Aug2017: Chnages done to save blacklist code, date Negated code and date in DB: comment:Chnages done to add blacklist reasone code, data Negated Reasoncode and date start
 * Saurabh 19th September : insert failing in Decision history changes.  
 * */
package com.newgen.omniforms.user;

import com.newgen.custom.XMLParser;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.IRepeater;
import com.newgen.omniforms.component.PickList;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.excp.CustomExceptionHandler;
import com.newgen.omniforms.skutil.*;

import com.newgen.wfdesktop.xmlapi.WFCallBroker;

import java.util.List;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.Socket;
import java.net.UnknownHostException;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Map;
import java.util.LinkedHashMap;

import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;

public class RLOSCommon extends Common implements Serializable{
	HashMap<String,String> hm= new HashMap<String,String>(); // not a nullable HashMap
	String popupFlag="";
	public String getRMName(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		SKLogger.writeLog("RLOS_Common", "Inside getRMName()"); 
		String query="Select RMId from ng_master_RMname where userid='"+formObject.getUserName()+"'";
		SKLogger.writeLog("RLOS_Common", "Query to fetch RMName:"+query); 
		List<List<String>> result=formObject.getDataFromDataSource(query);
		SKLogger.writeLog("RLOS_Common", "result of fetch RMname query: "+result); 
		if(result==null || result.equals("") || result.isEmpty()){
			return "";
		}
		else{
			return result.get(0).get(0);
		}
	}
	
	//function by saurabh on 11th Oct for setting charges fields values based on schemeID.
		
	//function by saurabh on 11th Oct for setting charges fields values based on schemeID.
	public void setChargesFields(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try{
		String scheme = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,8);
		String product = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0,1);
		if(product!=null && product.equalsIgnoreCase("Personal Loan")){
			if(scheme!=null && !(scheme.equalsIgnoreCase("")||scheme.equalsIgnoreCase("--Select--")|| scheme.equalsIgnoreCase(" "))){
				String query = "select CHARGERATE,CHARGEAMT from ng_master_charges where ChargeID = (select LPF_ChargeID from NG_master_Scheme where SCHEMEID = '"+scheme+"')";
				SKLogger.writeLog("RLOS_Common", "Query for charges is charges: "+query);
				List<List<String>> result = formObject.getDataFromDataSource(query);
				if(result!=null && result.size()>0 && result.get(0)!=null){
					String chargerate = result.get(0).get(0);
					String chargeamt = result.get(0).get(1);
					formObject.setNGValue("cmplx_EligibilityAndProductInfo_Insurance",chargerate);
					formObject.setNGValue("cmplx_EligibilityAndProductInfo_InsuranceAmount",chargeamt);
				}
			}
		}
		}catch(Exception ex){
			SKLogger.writeLog("RLOS_Common", "Exception in fetching charges values: "+printException(ex));
		}
	}
	public String getYearsDifference(FormReference formObject,String controlName, String controlName2) {
		int MON;
		int Year;
		String age="";
		String DOB=formObject.getNGValue(controlName);
		SKLogger.writeLog("RLOS COMMON"," Inside age + "+DOB);
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
			SKLogger.writeLog("RLOS_Common", "Inside calculateMaturityValues(): ");
			String moratorium = formObject.getNGValue("cmplx_EligibilityAndProductInfo_Moratorium");
			if(moratorium!=null && !moratorium.equalsIgnoreCase("") && !moratorium.equalsIgnoreCase(" ")){
				int mor = Integer.parseInt(moratorium);	
				Calendar cal = Calendar.getInstance();
				int curyear = cal.get(Calendar.YEAR);
				int currmonth = cal.get(Calendar.MONTH);
				int currDate = cal.get(Calendar.DATE);
				int offset = mor/12;
				int bal = mor%12;
				int newYear = curyear + offset;
				int newmonth = currmonth+bal;
				formObject.setNGValue("cmplx_EligibilityAndProductInfo_FirstRepayDate",currDate+"/"+newmonth+"/"+newYear);
			}
			String tenore = formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor");
			if(tenore!=null && !tenore.equalsIgnoreCase("") && !tenore.equalsIgnoreCase(" ")){
				int ten = Integer.parseInt(tenore);
				int offset = ten/12;
				int bal = ten%12;
				String frd = formObject.getNGValue("cmplx_EligibilityAndProductInfo_FirstRepayDate");
				int year = Integer.parseInt(frd.split("/")[2]);
				int month = Integer.parseInt(frd.split("/")[1]);
				int day = Integer.parseInt(frd.split("/")[0]);
				formObject.setNGValue("cmplx_EligibilityAndProductInfo_MaturityDate",day+"/"+(month+bal)+"/"+(year+offset));
			}
			formObject.setNGValue("cmplx_EligibilityAndProductInfo_ageAtMaturity",getYearsDifference(formObject, "cmplx_Customer_DOb", "cmplx_EligibilityAndProductInfo_MaturityDate"));
		}
		catch(Exception ex){
			SKLogger.writeLog("RLOS_Common", "Exception Inside calculateMaturityValues(): "+printException(ex));
		}
	}
public static void getAge(String dateBirth,String controlName){
	
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			SKLogger.writeLog("RLOS_Common", "Inside getAge(): "); 

			if (dateBirth.contains("/")){
				String parts[] = dateBirth.split("/");
				Calendar dob = Calendar.getInstance();
				Calendar today = Calendar.getInstance();
				System.out.println("today"+dob+today);
				dob.set(Integer.parseInt(parts[2]), Integer.parseInt(parts[1])-1,Integer.parseInt(parts[0])); 

				Integer age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
				System.out.println("age=="+age);
				int month=today.get(Calendar.MONTH) - dob.get(Calendar.MONTH);
				System.out.println("month=="+month);
				if (month<0){
					age--; 
					month=today.get(Calendar.MONTH);
					month= 12-dob.get(Calendar.MONTH) + today.get(Calendar.MONTH);
					if(today.get(Calendar.DATE) < dob.get(Calendar.DATE)){
						month=month-1;
					}
				}
				else if(month == 0 && today.get(Calendar.DATE) < dob.get(Calendar.DATE)) {
					age--;
					month= 11-dob.get(Calendar.MONTH) + today.get(Calendar.MONTH);//If month is same as current no need to count it
				}
				else if(month == 1 && today.get(Calendar.DATE) < dob.get(Calendar.DATE)){
					month=month-1;
				}
				else if(month == 2 && today.get(Calendar.DATE) < dob.get(Calendar.DATE)){
					month=month-1;
				}
				else if(month == 3 && today.get(Calendar.DATE) < dob.get(Calendar.DATE)){
					month=month-1;
				}
				else if(month == 4 && today.get(Calendar.DATE) < dob.get(Calendar.DATE)){
					month=month-1;
				}
				else if(month == 5 && today.get(Calendar.DATE) < dob.get(Calendar.DATE)){
					month=month-1;
				}
				else if(month == 6 && today.get(Calendar.DATE) < dob.get(Calendar.DATE)){
					month=month-1;
				}
				else if(month == 7 && today.get(Calendar.DATE) < dob.get(Calendar.DATE)){
					month=month-1;
				}
				else if(month == 8 && today.get(Calendar.DATE) < dob.get(Calendar.DATE)){
					month=month-1;
				}
				else if(month == 9 && today.get(Calendar.DATE) < dob.get(Calendar.DATE)){
					month=month-1;
				}
				else if(month == 10 && today.get(Calendar.DATE) < dob.get(Calendar.DATE)){
					month=month-1;
				}
				else if(month == 11 && today.get(Calendar.DATE) < dob.get(Calendar.DATE)){
					month=month-1;
				}
				//SKLogger.writeLog("RLOS_Common", "Values are with /: "+parts[2]+parts[1]+parts[0]+" age: "+age); 
				System.out.println("Age is====== "+age+"."+month);

				formObject.setNGValue(controlName,(age+"."+month).toString(),false); 
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
				System.out.println("RLOS_Common Values are with-: "+parts[2]+parts[1]+parts[0]); 


				formObject.setNGValue("cmplx_Customer_age",age.toString(),false); 
			}
		}

		catch(Exception e){
			//try{ throw new ValidatorException(new CustomExceptionHandler("Error Message", "Text1","EventType", hm));}finally{hm.clear();}
		}
}
	

	public void Eligibilityfields()
	{

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		SKLogger.writeLog("RLOS_Common", "Inside loadPicklist_Address: "); 


		SKLogger.writeLog("RLOS", "Grid Data[1][6] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6));
		if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("SE") || (formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("BTC")) || (formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6).equalsIgnoreCase("Self Employed"))){
			//formObject.setVisible("cmplx_EligibilityAndProductInfo_FinalDBR", false);//Arun (08/11/17) to show this field visible
			//formObject.setVisible("ELigibiltyAndProductInfo_Label3", false);//Arun (08/11/17) to show this field visible
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
			formObject.setLeft("cmplx_EligibilityAndProductInfo_FinalLimit", 297); //Arun (08/11/17) to place the field proper
			formObject.setLeft("ELigibiltyAndProductInfo_Label4", 297); //Arun (08/11/17) to place the field proper

			SKLogger.writeLog("RLOS @yash", "Grid Data[1][6] is:"+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1)+formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6));


		}
		else if((formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6).equalsIgnoreCase("Salaried") || formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6).equalsIgnoreCase("Salaried Pensioner") ) && !formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2).equalsIgnoreCase("IM")){
			if(!(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1).equalsIgnoreCase("Personal Loan"))){
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
				if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 4).equalsIgnoreCase("TKOE")){
					formObject.setVisible("ELigibiltyAndProductInfo_Label8",false);
					formObject.setVisible("cmplx_EligibilityAndProductInfo_NetPayout",false);
					formObject.setVisible("ELigibiltyAndProductInfo_Label2",true);
					formObject.setVisible("cmplx_EligibilityAndProductInfo_takeoverBank",true);
					formObject.setVisible("ELigibiltyAndProductInfo_Label1",true);
					formObject.setVisible("cmplx_EligibilityAndProductInfo_TakeoverAMount",true);
				}
				else if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 4).equalsIgnoreCase("TOPN")){
					formObject.setVisible("ELigibiltyAndProductInfo_Label2",false);
					formObject.setVisible("cmplx_EligibilityAndProductInfo_takeoverBank",false);
					formObject.setVisible("ELigibiltyAndProductInfo_Label1",false);
					formObject.setVisible("cmplx_EligibilityAndProductInfo_TakeoverAMount",false);
					formObject.setVisible("ELigibiltyAndProductInfo_Label8",true);
					formObject.setVisible("cmplx_EligibilityAndProductInfo_NetPayout",true);
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
		}

	}

	public void loadPicklistCustomer()  
	{
		SKLogger.writeLog("RLOS_Common", "Inside loadPicklistCustomer: ");
		LoadPickList("cmplx_Customer_Nationality", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");
		LoadPickList("cmplx_Customer_Title", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_title with (nolock) order by code");
		LoadPickList("cmplx_Customer_SecNationality", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock) order by Code");

		LoadPickList("cmplx_Customer_CustomerCategory", " select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_CustomerCategory order by Code");
		LoadPickList("cmplx_Customer_COuntryOFResidence", " select convert(varchar, Description),code from ng_MASTER_Country order by Code");	
		LoadPickList("cmplx_Customer_MAritalStatus", "select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_Maritalstatus order by Code");
		LoadPickList("ref_Relationship", "select convert(varchar, Description),code from NG_MASTER_Relationship union select '--Select--','' order by Code");
		LoadPickList("cmplx_Customer_EmirateOfResidence", "select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_emirate order by Code");
		LoadPickList("cmplx_Customer_EMirateOfVisa", "select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_emirate order by Code");
	}

	public void setcustomer_enable(){
		SKLogger.writeLog("RLOS_Common", "Inside setCustomer enable function");
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
		if(formObject.getNGValue("cmplx_Customer_SecNAtionApplicable").equalsIgnoreCase("No"))
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
			SKLogger.writeLog("RLOS val change ","Inside GCC for Nationality");
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
		SKLogger.writeLog("RLOS_Common", "Inside loadPicklistProduct$"+ReqProd+"$");
		String ProdType=formObject.getNGValue("Product_type");

		if(ReqProd.equalsIgnoreCase("Personal Loan")){
			SKLogger.writeLog("RLOS_Common", "Inside equalsIgnoreCase()"); 
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
			SKLogger.writeLog("RLOS_Common PIKLIST", "Inside equalsIgnoreCase()"+ReqProd);
			LoadPickList("EmpType", " select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_PRD_EMPTYPE where SubProduct = '"+ReqProd+"' order by code");

		}
		else if(ReqProd.equalsIgnoreCase("Credit Card")){
			formObject.setVisible("CardProd", true);
			formObject.clear("CardProd");
			formObject.clear("SubProd");
			formObject.setNGValue("SubProd", "--Select--");
			if(formObject.getWFActivityName().equals("Telesales_Init")){
				LoadPickList("SubProd", " select '--Select--' as description,'' as code union select convert(varchar(50),description),code  from ng_master_Subproduct_CC  where code='BTC' or code='IM' order by code");

			}
			else{
				LoadPickList("SubProd", " select '--Select--' as description,'' as code union select  convert(varchar,description),code from ng_master_Subproduct_CC with (nolock) order by code");
			}
			//added by abhishek
			SKLogger.writeLog("RLOS_Common PIKLIST", "Inside equalsIgnoreCase()"+ReqProd);

			LoadPickList("EmpType", "select '--Select--' as description,'' as code union select convert(varchar, description),code from ng_MASTER_PRD_EMPTYPE where SubProduct = '"+ReqProd+"' order by code");


			if(ProdType.equalsIgnoreCase("Islamic"))
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
		SKLogger.writeLog("RLOS_Common", "Inside loadPicklist_Address: "); 
		//	LoadPickList("addtype", "select '--Select--' as description  union select convert(varchar, description)  from NG_MASTER_AddressType with (nolock) order by description desc");
		LoadPickList("addtype", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_AddressType with (nolock)  where isActive='Y' order by code");
		LoadPickList("city", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from NG_MASTER_city with (nolock)  where isActive='Y' order by code");
		LoadPickList("state", "select  '--Select--'as description,'' as code  union select convert(varchar, Description),code from NG_MASTER_state order by Code");
		//LoadPickList("city", "select '--Select--','' as code union select convert(varchar, description),code from NG_MASTER_city with (nolock) order by code");
		//LoadPickList("state", "select '--Select--','' as code union select convert(varchar, description),code from NG_MASTER_state with (nolock) order by code");
		LoadPickList("country", "select '--Select--'as description,'' as code  union select convert(varchar, description),code from ng_MASTER_Country with (nolock)  where isActive='Y' order by Code");
	}

	public void loadPicklist_AuthSign()
	{
		SKLogger.writeLog("RLOS_Common", "Inside loadPicklist_AuthSign: "); 
		//	LoadPickList("addtype", "select '--Select--' as description  union select convert(varchar, description)  from NG_MASTER_AddressType with (nolock) order by description desc");
		LoadPickList("AuthorisedSignDetails_nationality", "select '--Select--' as description,'' as code union select convert(varchar, description),code from NG_MASTER_Country with (nolock) order by code");
		LoadPickList("SignStatus", "select convert(varchar, description) from NG_MASTER_SignatoryStatus with (nolock)");
		LoadPickList("Designation", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
		LoadPickList("DesignationAsPerVisa", "select '--Select--' as description,'' as code union select convert(varchar, description),code  from NG_MASTER_Designation with (nolock) order by code");
		}
	
	public void loadPicklist_ServiceRequest()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		SKLogger.writeLog("RLOS_Common", "Inside loadPicklist_ServiceRequest: "); 
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

	public boolean product_Add_Validate()
	{
		SKLogger.writeLog("RLOS_Common", "Inside product_Add_Validate");
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

				if(!grid_EmpType.equalsIgnoreCase(EmpType) && !grid_EmpType.equalsIgnoreCase("--Select--") && !EmpType.equalsIgnoreCase("--Select--"))
					throw new ValidatorException(new FacesMessage("Cannot add two different Employment Types ","EmpType"));

				String grid_Prod=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1);
				SKLogger.writeLog("RLOS", "Value of Grid_prod:"+grid_Prod);

				if(grid_Prod.equalsIgnoreCase("Personal Loan") && grid_Prod.equalsIgnoreCase(ReqProd))
					throw new ValidatorException(new FacesMessage("Cannot add 2 Personal Loans ","ReqProd"));


				if(grid_Priority.equalsIgnoreCase("Primary") && grid_Priority.equalsIgnoreCase(priority))
					throw new ValidatorException(new FacesMessage("Cannot add 2 primary products ","Priority"));

			}
		}


		return true;
	}

	public boolean product_Modify_Validate()
	{
		SKLogger.writeLog("RLOS_Common", "product_Modify_Validate product_Validate");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String priority=formObject.getNGValue("Priority");
		String ReqProd=formObject.getNGValue("ReqProd");
		String EmpType=formObject.getNGValue("EmpType");
		String TypeProd=formObject.getNGValue("Product_type");
		String appType=formObject.getNGValue("AppType");
		formObject.getNGValue("ReqTenor");
		String ReqLimit=formObject.getNGValue("ReqLimit");
		if(TypeProd.equalsIgnoreCase("")||ReqProd.equalsIgnoreCase("--Select--")|| ReqProd.equalsIgnoreCase("")||appType.equalsIgnoreCase("")||EmpType.equalsIgnoreCase("--Select--") ||EmpType.equalsIgnoreCase("")||ReqLimit.equalsIgnoreCase("") || priority.equalsIgnoreCase(""))
			throw new ValidatorException(new FacesMessage("All fields are mandatory "));

		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(n>1)
		{
			for(int i=0;i<n;i++)
			{
				String grid_EmpType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,6);
				String grid_Priority=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9);

				if(!grid_EmpType.equalsIgnoreCase(EmpType) && !grid_EmpType.equalsIgnoreCase("--Select--") && !EmpType.equalsIgnoreCase("--Select--"))
					throw new ValidatorException(new FacesMessage("Cannot add two different Employment Types ","EmpType"));
				SKLogger.writeLog("RLOS", "Value of Selected index: "+formObject.getSelectedIndex("cmplx_Product_cmplx_ProductGrid"));
				String grid_Prod=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,1);
				SKLogger.writeLog("RLOS", "Value of Grid_prod:"+grid_Prod);
				if(grid_Prod.equalsIgnoreCase("PL") && grid_Prod.equalsIgnoreCase(ReqProd)&& !formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",formObject.getSelectedIndex("cmplx_Product_cmplx_ProductGrid"),1).equalsIgnoreCase(ReqProd))
					throw new ValidatorException(new FacesMessage("Cannot add 2 PL ","ReqProd"));

				if(grid_Priority.equalsIgnoreCase("Primary") && grid_Priority.equalsIgnoreCase(priority))
					throw new ValidatorException(new FacesMessage("Cannot add 2 primary products ","Priority"));

			}
		}



		return 	true;
	}



	public void  setALOCListed(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		SKLogger.writeLog("RLOSCOmmon", "Inside setALOCListed()");
		String NewEmployer=formObject.getNGValue("cmplx_EmploymentDetails_Others");
		String IncInCC=formObject.getNGValue("cmplx_EmploymentDetails_IncInCC");
		String INcInPL=formObject.getNGValue("cmplx_EmploymentDetails_IncInPL");
		String subprod=formObject.getNGValue("PrimaryProduct");

		if(subprod.equalsIgnoreCase("Personal Loan") && NewEmployer.equals("true") && (IncInCC.equals("false") || INcInPL.equals("false")))
		{
			formObject.setNGValue("cmplx_EmploymentDetails_EmpStatusPL", "CN");
			formObject.setNGValue("cmplx_EmploymentDetails_EmpStatusCC", "CN");
			formObject.setLocked("cmplx_EmploymentDetails_EmpStatusCC", true);
		}

		else if(subprod.equalsIgnoreCase("Credit Card") && NewEmployer.equals("true") && (IncInCC.equals("false") || INcInPL.equals("false")))
		{
			formObject.setNGValue("cmplx_EmploymentDetails_EmpStatusPL", "CN");
			formObject.setNGValue("cmplx_EmploymentDetails_EmpStatusCC", "CN");
			formObject.setLocked("cmplx_EmploymentDetails_EmpStatusPL", true);
		}

		/*else{
			formObject.setNGValue("cmplx_EmploymentDetails_EmpStatusPL", "");
			formObject.setNGValue("cmplx_EmploymentDetails_EmpStatusCC", "");
			formObject.setLocked("cmplx_EmploymentDetails_EmpStatusPL", false);
			formObject.setLocked("cmplx_EmploymentDetails_EmpStatusCC", false);

		}*/
	}

	public boolean Address_Validate()
	{
		boolean flag_addressType=false;
		SKLogger.writeLog("RLOS_Common", "Inside Addess_Validate");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String AddType=formObject.getNGValue("addtype");
		int n=formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
		if(n>0)
		{
			for(int i=0;i<n;i++){
				String mystring=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,0);
				SKLogger.writeLog("RLOS", "mystring:$"+mystring+"$");

				if(mystring.equalsIgnoreCase("Permanent") && AddType.equalsIgnoreCase(mystring)){
					SKLogger.writeLog("RLOS", "inside validator");
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
		SKLogger.writeLog("RLOSCommon", "Inside loadpicklist3: Query is:  "+query); 
		LoadPickList("cmplx_DecisionHistory_Decision", query);
		LoadPickList("cmplx_DecisionHistory_DecisionReasonCode", "select '--Select--' as description,'' as code union select  convert(varchar, description),code from ng_MASTER_RejectReasons with (nolock)  where isActive='Y' order by code");
	}

	public void employment_details_load(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		if(formObject.getNGValue("cmplx_Customer_NEP").equals("false")){
		formObject.setLocked("EMploymentDetails_Label25",true);//Added by Akshay on 9/9/17 for enabling NEP type as per FSD
		formObject.setLocked("cmplx_EmploymentDetails_NepType",true);
		}
		setALOCListed();
		formObject.setVisible("cmplx_EmploymentDetails_tenancntrct",false);
		formObject.setVisible("EMploymentDetails_Label5",false);
		
		formObject.setVisible("cmplx_EmploymentDetails_channelcode",false);
		formObject.setVisible("EMploymentDetails_Label36",false);
		formObject.setLocked("cmplx_EmploymentDetails_EmpName",true);
		formObject.setLocked("cmplx_EmploymentDetails_EMpCode",true);
		formObject.setLocked("cmplx_EmploymentDetails_LOS",true);
		loadPicklist4();
		Fields_ApplicationType_Employment();
		//added By Akshay on 12/9/2017 to set this field as CAC if CAC is true
		if(formObject.getNGValue("cmplx_Liability_IS_CAC")!=null && formObject.getNGValue("cmplx_Liability_IS_CAC").equalsIgnoreCase("Y"))
		{
			formObject.setNGValue("cmplx_EmploymentDetails_targetSegCode", "CAC");
		}
		//ended By Akshay on 12/9/2017 to set this field as CAC if CAC is true

		/*if(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode").equalsIgnoreCase("NEP")){
			formObject.setVisible("EMploymentDetails_Label25",true);
			formObject.setVisible("cmplx_EmploymentDetails_NepType",true);
		}*/ //Arun (01/11) as this is not working

		else if(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode").equalsIgnoreCase("FZD") || formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode").equalsIgnoreCase("FZE")){
			formObject.setVisible("cmplx_EmploymentDetails_Freezone",true);
			formObject.setVisible("EMploymentDetails_Label62",true);
			formObject.setVisible("cmplx_EmploymentDetails_FreezoneName",true);
		}

		else if(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode").equalsIgnoreCase("TEN"))
		{
			formObject.setVisible("cmplx_EmploymentDetails_tenancntrct",true);
			formObject.setVisible("EMploymentDetails_Label5",true);
		}

		/*else if(formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg").equals("Surrogate") && empid.contains(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")))
		{
			formObject.setVisible("cmplx_EmploymentDetails_IndusSeg",true);
			formObject.setVisible("EMploymentDetails_Label59",true);
			
		}*/
		String reqProd = formObject.getNGValue("PrimaryProduct");
		String appCategory = formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg");
		String subproduct = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2);
		if(reqProd.equalsIgnoreCase("Personal Loan")){
			if(appCategory!=null && appCategory.equalsIgnoreCase("BAU")){
				LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and BAU='Y' and (product = 'PL' or product='B') order by code");
			}
			else if(appCategory!=null &&  appCategory.equalsIgnoreCase("Surrogate")){
				LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and Surrogate='Y' and (product = 'PL' or product='B') order by code");
			}
			else{
				LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and (product = 'PL' or product='B') order by code");	
			}
		}
		else if(reqProd.equalsIgnoreCase("Credit Card")){
			if(appCategory!=null &&  appCategory.equalsIgnoreCase("BAU")){
				LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and BAU='Y' and (product = 'CC' or product='B') order by code");
			}
			else if(appCategory!=null &&  appCategory.equalsIgnoreCase("Surrogate")){
				LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and Surrogate='Y' and (product = 'CC' or product='B') order by code");
			}
			else{
				LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and (product = 'CC' or product='B') order by code");	
			}
		}
		formObject.setLocked("cmplx_EmploymentDetails_LOSPrevious", false);
		formObject.setEnabled("cmplx_EmploymentDetails_LOSPrevious", true);
	
	}
	public void loadPicklist4()    
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		SKLogger.writeLog("RLOSCommon", "Inside loadpicklist4:");
		String reqProd = formObject.getNGValue("PrimaryProduct");
		//below query changed by saurabh on 19th Oct for JIRA-2125.
		LoadPickList("cmplx_EmploymentDetails_ApplicationCateg", "select description from (select '--Select--' as description union select  convert(varchar,description) from NG_MASTER_ApplicatonCategory with (nolock)) new_table order by case when description='--Select--' then 0 else 1 end");
		String appCategory = formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg");
		String subproduct = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2);
		if(reqProd.equalsIgnoreCase("Personal Loan")){
			if(appCategory!=null && appCategory.equalsIgnoreCase("BAU")){
				LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and BAU='Y' and (product = 'PL' or product='B') order by code");
			}
			else if(appCategory!=null &&  appCategory.equalsIgnoreCase("Surrogate")){
				LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and Surrogate='Y' and (product = 'PL' or product='B') order by code");
			}
			else{
			LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and (product = 'PL' or product='B') order by code");
			}
		}
		else if(reqProd.equalsIgnoreCase("Credit Card")){
			if(appCategory!=null &&  appCategory.equalsIgnoreCase("BAU")){
				LoadPickList("cmplx_EmploymentDetails_targetSegCode", "select '--Select--' as description,'' as code union select  convert(varchar,description),code from NG_MASTER_TargetSegmentCode with (nolock) where isActive='Y' and SubProduct = '"+subproduct+"' and BAU='Y' and (product = 'CC' or product='B') order by code");
			}
			else if(appCategory!=null &&  appCategory.equalsIgnoreCase("Surrogate")){
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
		LoadPickList("cmplx_EmploymentDetails_EmployerType","select '--Select--' as description,'' as code union select convert(varchar, Description),code from ng_master_EmployerType where isActive='Y' order by code"); //Arun (08/11/17) to load masters for Employer type
	}




	public void IMFields_Employment()
	{
		SKLogger.writeLog("RLOSCommon", "Inside IMFields_Employment:"); 

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(n>0){
			for(int i=0;i<n;i++){
				if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("Instant Money")){
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
		SKLogger.writeLog("RLOSCommon", "Inside BTCFields_Employment:"); 

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(n>0){
			for(int i=0;i<n;i++){
				if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("Business titanium Card")){
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
		SKLogger.writeLog("RLOSCommon", "Inside LimitIncreaseFields_Employment:"); 

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(n>0){
			for(int i=0;i<n;i++){ 
				if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("Limit Increase")){
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
		SKLogger.writeLog("RLOSCommon", "Inside ProductUpgrade_Employment:"); 

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(n>0){
			for(int i=0;i<n;i++){ 
				if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("Product Upgrade")){
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
		SKLogger.writeLog("RLOSCommon", "Inside Fields_ApplicationType_Employment:"); 

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		if(formObject.getNGValue("Application_Type").equalsIgnoreCase("RESCE") || formObject.getNGValue("Application_Type").equalsIgnoreCase("RESCN")){
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

	public void Notepad_delete(){
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

	public void Notepad_grid(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sActivityName=FormContext.getCurrentInstance().getFormConfig( ).getConfigElement("ActivityName");
		Date date = new Date();
		String modifiedDate= new SimpleDateFormat("dd/MM/yyyy").format(date);
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

	public void IMFields_Income()
	{
		SKLogger.writeLog("RLOSCommon", "Inside IMFields_Income:"); 

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(n>0){
			for(int i=0;i<n;i++){
				if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("Instant Money")){
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
		SKLogger.writeLog("RLOSCommon", "Inside BTCFields_Income:"); 

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(n>0){
			for(int i=0;i<n;i++){
				if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("Business titanium Card") || formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,5).equalsIgnoreCase("BTC Plus") ){
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
		SKLogger.writeLog("RLOSCommon", "Inside LimitIncreaseFields_Income:"); 

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(n>0){
			for(int i=0;i<n;i++){ 
				if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("Limit Increase")){
					formObject.setLocked("cmplx_IncomeDetails_MonthlyRent",true); 
					formObject.setLocked("cmplx_IncomeDetails_NoOfBankStat",true); 
					break;
				}
			}
		}
	}


	public void ProductUpgrade_Income()
	{
		SKLogger.writeLog("RLOSCommon", "Inside ProductUpgrade_Income:"); 

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(n>0){
			for(int i=0;i<n;i++){ 
				if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,2).equalsIgnoreCase("Product Upgrade")){
					formObject.setLocked("cmplx_IncomeDetails_MonthlyRent",true); 
					formObject.setLocked("cmplx_IncomeDetails_NoOfBankStat",true); 		
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
					LoadPickList("contractType","Select '--Select--' as Description,'' as code union select convert(varchar,Description),code from ng_master_contract_type with(nolock) order by code'");
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

		}
	}

	public void  Fields_Eligibility() 
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		if(formObject.getNGValue("PrimaryProduct").equalsIgnoreCase("Personal Loan")){
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

			if(formObject.getNGValue("Subproduct_productGrid").equalsIgnoreCase("IM")){
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










			else if(formObject.getNGValue("Subproduct_productGrid").equalsIgnoreCase("SAL")){
				Eligibility_Hide();
				formObject.setVisible("ELigibiltyAndProductInfo_Label41",true); 
				formObject.setVisible("cmplx_EligibilityAndProductInfo_FinalTAI",true);
				formObject.setTop("ELigibiltyAndProductInfo_Frame7",80);

			}


			else if(formObject.getNGValue("Subproduct_productGrid").equalsIgnoreCase("BTC")){


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
			else if(formObject.getNGValue("Subproduct_productGrid").equalsIgnoreCase("LI")){
				Eligibility_Hide();
			}

			else if(formObject.getNGValue("Subproduct_productGrid").equalsIgnoreCase("PU")){
				Eligibility_Hide();
			}

			else{
				Eligibility_UnHide();
				formObject.setVisible("cmplx_EligibilityAndProductInfo_FinalDBR",true);
				formObject.setVisible("ELigibiltyAndProductInfo_Label3",true);
				formObject.setVisible("ELigibiltyAndProductInfo_Label39",false);
				//formObject.setVisible("ELigibiltyAndProductInfo_Label40",false);
				formObject.setVisible("cmplx_EligibilityAndProductInfo_instrumenttype",false);

			}
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
		//formObject.setVisible("ELigibiltyAndProductInfo_Label37",true);
		//formObject.setVisible("ELigibiltyAndProductInfo_Label38",true);	
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
			formObject.addItemFromList("cmplx_ExternalLiabilities_cmplx_CardGrid", tempList);

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

			formObject.addItemFromList("Decision_ListView1", a);
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
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
			String entry_date = formObject.getNGValue("Entry_Date");
			Date d = format.parse(entry_date);
			entry_date = format.format(d);
			SKLogger.writeLog("RLOS_Common", "after fromatting date: "+entry_date); 
			String currDate=new SimpleDateFormat("MM/dd/yyyy HH:mm").format(Calendar.getInstance().getTime());
			// EntryDateTime changed by saurabh on 19th September 2017 as this value was passing as null and hence decision history entry was failing.
			String query="insert into ng_rlos_gr_Decision(dateLastChanged, userName, workstepName, Decisiom, remarks, dec_wi_name,Entry_date) values('"+currDate+"','"+formObject.getUserName()+"','"+formObject.getWFActivityName()+"','"+formObject.getNGValue("cmplx_DecisionHistory_Decision")+"','"+formObject.getNGValue("cmplx_DecisionHistory_Remarks")+"','"+formObject.getWFWorkitemName()+"','"+entry_date+"')";

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
		String LoanType="",ReqProd="",SubProd="",CardProd="",AppType="";


		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		for(int i=0;i<n;i++)	
		{
			if(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equals("Primary")){
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

		List<List<String>> result=formObject.getDataFromDataSource(sQuery);
		if(result.size()>0){
			objPickList.setHeight(600);
			objPickList.setWidth(800);
			objPickList.setVisible(true);
			objPickList.setSearchEnabled(true);
			objPickList.addPickListListener(new EventListenerHandler(objPickList.getClientId()));

			SKLogger.writeLog("EventListenerHandler: Result Of Query:",result.toString());   
			objPickList.populateData(result);
			formObject = null;
		}
		else{
			throw new ValidatorException(new FacesMessage("No Data Found"));
		}

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
			String emp_type ="";
			int prod_count=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			if (prod_count!=0){
				 emp_type = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 6)==null?"":formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 6);
			}
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
							else if(tag_name.equalsIgnoreCase("CCIFID")){
								if(int_xml.containsKey(parent_tag))
								{
									String xml_str = int_xml.get(parent_tag);
									SKLogger.writeLog("RLOS COMMON"," before CIF+ "+xml_str);
									String Cif = formObject.getNGValue(form_control);
									if(Cif!=null && !Cif.equalsIgnoreCase("") && Cif.length()!=7){
										Cif = "0"+Cif;
									}
									xml_str = xml_str + "<"+tag_name+">"+Cif+"</"+ tag_name+">";
									SKLogger.writeLog("RLOS COMMON"," after adding address+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}		                            	
							}
							else if(tag_name.equalsIgnoreCase("MinorFlag") && Call_name.equalsIgnoreCase("NEW_CUSTOMER_REQ") && parent_tag.equalsIgnoreCase("PersonDetails")){
								if(int_xml.containsKey(parent_tag))
								{
									float Age = Float.parseFloat(formObject.getNGValue("cmplx_Customer_age"));
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
							//added by saurabh on 23rd Aug 17.
							else if(tag_name.equalsIgnoreCase("AcRequired") && Call_name.equalsIgnoreCase("NEW_ACCOUNT_REQ")){
								SKLogger.writeLog("inside 1st if","inside New acc request for AcRequired");
								String loantype = (formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 0).equalsIgnoreCase("Conventional")?"KC":"AC");
								String xml_str = int_xml.get(parent_tag);
								xml_str = xml_str+"<"+tag_name+">"+loantype+"</"+ tag_name+">";
								SKLogger.writeLog("COMMON","adding Host name in New Loan Req:  "+xml_str);
								int_xml.put(parent_tag, xml_str);                                     
							}
							//added by saurabh on 23rd Aug 17.


							// added for dectech call 
							else if(tag_name.equalsIgnoreCase("CallType") && Call_name.equalsIgnoreCase("DECTECH")){
								SKLogger.writeLog("RLOS COMMON"," iNSIDE CallType+ ");
								String CallType=formObject.getNGValue("DecCallFired");
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
								if(empttype!=null){	
									if (empttype.equalsIgnoreCase("Salaried")){
										empttype="S";
									}
									else if (empttype.equalsIgnoreCase("Salaried Pensioner")){
										empttype="SP";
									}
									else {
										empttype="SE";
									}
								}
								String xml_str = int_xml.get(parent_tag);
								xml_str = xml_str+ "<"+tag_name+">"+empttype+"</"+ tag_name+">";

								SKLogger.writeLog("RLOS COMMON"," after adding channelcode+ "+xml_str);
								int_xml.put(parent_tag, xml_str);

							}
							else if(tag_name.equalsIgnoreCase("world_check") && Call_name.equalsIgnoreCase("DECTECH")){
								SKLogger.writeLog("RLOS COMMON"," iNSIDE world_check+ ");

								String world_check=formObject.getNGValue("IS_WORLD_CHECK");
								SKLogger.writeLog("RLOS COMMON"," iNSIDE world_check+ "+formObject.getLVWRowCount("cmplx_WorldCheck_WorldCheck_Grid"));
								if (formObject.getLVWRowCount("cmplx_WorldCheck_WorldCheck_Grid")==0){
									world_check="Negative";
								}
								else {
									world_check="Positive";
								}


								String xml_str = int_xml.get(parent_tag);
								xml_str = xml_str+ "<"+tag_name+">"+world_check+"</"+ tag_name+">";

								SKLogger.writeLog("RLOS COMMON"," after adding world_check+ "+xml_str);
								int_xml.put(parent_tag, xml_str);

							}
							else if(tag_name.equalsIgnoreCase("current_emp_catogery") && Call_name.equalsIgnoreCase("DECTECH")){
								SKLogger.writeLog("RLOS COMMON"," iNSIDE current_emp_catogery+ ");

								String current_emp_catogery=formObject.getNGValue("cmplx_EmploymentDetails_CurrEmployer");
								SKLogger.writeLog("RLOS COMMON"," value of current_emp_catogery "+current_emp_catogery);
								String squerycurremp="select Description from NG_MASTER_EmployerCategory_PL where isActive='Y' and code='"+current_emp_catogery+"'";
								SKLogger.writeLog("RLOS COMMON"," query is "+squerycurremp);
								List<List<String>> squerycurrempXML=formObject.getDataFromDataSource(squerycurremp);
								SKLogger.writeLog("RLOS COMMON"," query is "+squerycurrempXML);
							if(!squerycurrempXML.isEmpty()){
								if (squerycurrempXML.get(0).get(0)!=null){
									SKLogger.writeLog("RLOS COMMON"," iNSIDE squerycurrempXML+ "+squerycurrempXML.get(0).get(0));
									current_emp_catogery=squerycurrempXML.get(0).get(0);
								}
							}

								String xml_str = int_xml.get(parent_tag);
								xml_str = xml_str+ "<"+tag_name+">"+current_emp_catogery+"</"+ tag_name+">";

								SKLogger.writeLog("RLOS COMMON"," after adding current_emp_catogery+ "+xml_str);
								int_xml.put(parent_tag, xml_str);

							}	
							else if((tag_name.equalsIgnoreCase("prev_loan_dbr")||tag_name.equalsIgnoreCase("prev_loan_tai")||tag_name.equalsIgnoreCase("prev_loan_multiple")||tag_name.equalsIgnoreCase("prev_loan_employer")) && Call_name.equalsIgnoreCase("DECTECH")){
								SKLogger.writeLog("RLOS COMMON"," iNSIDE prev_loan_dbr+ ");
								String Limit_Increase="";
								String PreviousLoanDBR="";
								String PreviousLoanEmp="";
								String PreviousLoanMultiple="";
								String PreviousLoanTAI="";

								String squeryloan="select isNull(PreviousLoanDBR,0), isNull(PreviousLoanEmp,0), isNull(PreviousLoanMultiple,0), isNull(PreviousLoanTAI,0) from ng_RLOS_CUSTEXPOSE_LoanDetails where Limit_Increase='true'  and Wi_Name= '"+formObject.getWFWorkitemName()+"'";
								List<List<String>> prevLoan=formObject.getDataFromDataSource(squeryloan);
								SKLogger.writeLog("RLOS COMMON"," iNSIDE prev_loan_dbr+ "+squeryloan);

								if (prevLoan!=null && prevLoan.size()>0){
									PreviousLoanDBR=prevLoan.get(0).get(0);
									PreviousLoanEmp=prevLoan.get(0).get(1);
									PreviousLoanMultiple=prevLoan.get(0).get(2);
									PreviousLoanTAI=prevLoan.get(0).get(3);
								}


								String xml_str = int_xml.get(parent_tag);
								if(tag_name.equalsIgnoreCase("prev_loan_dbr")){
									xml_str = xml_str+ "<"+tag_name+">"+PreviousLoanDBR+"</"+ tag_name+">";
								}
								else if(tag_name.equalsIgnoreCase("prev_loan_tai")){
									xml_str = xml_str+ "<"+tag_name+">"+PreviousLoanTAI+"</"+ tag_name+">";
								}
								else if(tag_name.equalsIgnoreCase("prev_loan_multiple")){
									xml_str = xml_str+ "<"+tag_name+">"+PreviousLoanMultiple+"</"+ tag_name+">";
								}
								else if(tag_name.equalsIgnoreCase("prev_loan_employer")){
									xml_str = xml_str+ "<"+tag_name+">"+PreviousLoanEmp+"</"+ tag_name+">";
								}



								SKLogger.writeLog("RLOS COMMON"," after adding world_check+ "+xml_str);
								int_xml.put(parent_tag, xml_str);

							}

							else if(tag_name.equalsIgnoreCase("no_of_cheque_bounce_int_3mon_Ind") && Call_name.equalsIgnoreCase("DECTECH")){
								SKLogger.writeLog("RLOS COMMON"," iNSIDE no_of_cheque_bounce_int_3mon_Ind+ ");
								String squerynoc="SELECT count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-3,GETDATE()) and returntype='ICCS' and WI_Name='"+formObject.getWFWorkitemName()+"'";
								List<List<String>> NOC=formObject.getDataFromDataSource(squerynoc);
								if (NOC!=null && NOC.size()>0){
									String xml_str =  int_xml.get(parent_tag);
									xml_str = xml_str+ "<"+tag_name+">"+NOC.get(0).get(0)
									+"</"+ tag_name+">";

									SKLogger.writeLog("RLOS COMMON"," after adding internal_blacklist+ "+xml_str);
									int_xml.put(parent_tag, xml_str);

								}

							}
							else if(tag_name.equalsIgnoreCase("no_of_DDS_return_int_3mon_Ind") && Call_name.equalsIgnoreCase("DECTECH")){
								SKLogger.writeLog("RLOS COMMON"," iNSIDE no_of_cheque_bounce_int_3mon_Ind+ ");
								String squerynoc="SELECT count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-3,GETDATE()) and returntype='DDS' and WI_Name='"+formObject.getWFWorkitemName()+"'";
								List<List<String>> NOC=formObject.getDataFromDataSource(squerynoc);
								if (NOC!=null && NOC.size()>0){
									String xml_str =  int_xml.get(parent_tag);
									xml_str = xml_str+ "<"+tag_name+">"+NOC.get(0).get(0)
									+"</"+ tag_name+">";

									SKLogger.writeLog("RLOS COMMON"," after adding internal_blacklist+ "+xml_str);
									int_xml.put(parent_tag, xml_str);

								}

							}
							
							else if(tag_name.equalsIgnoreCase("borrowing_customer") && Call_name.equalsIgnoreCase("DECTECH")){
								SKLogger.writeLog("RLOS COMMON"," iNSIDE borrowing_customer+ ");
								String squeryBorrow="select distinct(borrowingCustomer) from ng_RLOS_CUSTEXPOSE_CardDetails WHERE  Wi_Name ='"+formObject.getWFWorkitemName()+"' union select distinct(borrowingCustomer) from ng_RLOS_CUSTEXPOSE_LoanDetails  WHERE Wi_Name ='"+formObject.getWFWorkitemName()+"'";
								SKLogger.writeLog("RLOS COMMON"," iNSIDE borrowing_customer query+ "+squeryBorrow);
								List<List<String>> borrowing_customer=formObject.getDataFromDataSource(squeryBorrow);
								if (borrowing_customer!=null && borrowing_customer.size()>0){
									String xml_str =  int_xml.get(parent_tag);
									xml_str = xml_str+ "<"+tag_name+">"+borrowing_customer.get(0).get(0)
									+"</"+ tag_name+">";

									SKLogger.writeLog("RLOS COMMON"," after adding borrowing_customer+ "+xml_str);
									int_xml.put(parent_tag, xml_str);

								}

							}
							
							
							else if(((tag_name.equalsIgnoreCase("lob"))||(tag_name.equalsIgnoreCase("target_segment_code"))||(tag_name.equalsIgnoreCase("designation"))||(tag_name.equalsIgnoreCase("emp_name"))||(tag_name.equalsIgnoreCase("industry_sector"))||(tag_name.equalsIgnoreCase("industry_marco"))||(tag_name.equalsIgnoreCase("industry_micro"))||(tag_name.equalsIgnoreCase("bvr"))||(tag_name.equalsIgnoreCase("eff_date_estba"))||(tag_name.equalsIgnoreCase("poa"))||(tag_name.equalsIgnoreCase("tlc_issue_date"))) && Call_name.equalsIgnoreCase("DECTECH") && emp_type.equalsIgnoreCase("Self Employed")){
								SKLogger.writeLog("RLOSCommon java file", "inside getProduct_details : ");
								String xml_str =  int_xml.get(parent_tag);
								int Comp_row_count = formObject.getLVWRowCount("cmplx_CompanyDetails_cmplx_CompanyGrid");
								String lob="";
								String target_segment_code="";
								String designation="";
								String emp_name="";
								String industry_sector="";
								String eff_date_estba="";
								String industry_marco="";
								String industry_micro="";
								String bvr="";
								String poa="";
								String tlc_issue_date="";
							if(Comp_row_count!=0){	
								for (int i = 0; i<Comp_row_count;i++){
									lob = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 15); //0
									target_segment_code = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 24); //0
									designation = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 11); //0
									emp_name = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 0); //0
									industry_sector = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 8); //0
									eff_date_estba = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 14); //0
									//industry_sector = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 5); //0
									industry_marco = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 9); //0
									industry_micro = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 10); //0
									bvr = (formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 19).equalsIgnoreCase("Yes")?"Y":"N"); //0
									poa = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 23); //0
									tlc_issue_date = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 26); //0
									
								
									}
								}
								if(tag_name.equalsIgnoreCase("lob")){
									xml_str = xml_str+ "<"+tag_name+">"+lob+"</"+ tag_name+">";
								}
								else if(tag_name.equalsIgnoreCase("target_segment_code")){
									xml_str = xml_str+ "<"+tag_name+">"+target_segment_code+"</"+ tag_name+">";
								}
								else if(tag_name.equalsIgnoreCase("designation")){
									xml_str = xml_str+ "<"+tag_name+">"+designation+"</"+ tag_name+">";
								}
								else if(tag_name.equalsIgnoreCase("emp_name")){
									xml_str = xml_str+ "<"+tag_name+">"+emp_name+"</"+ tag_name+">";
								}
								else if(tag_name.equalsIgnoreCase("industry_sector")){
									xml_str = xml_str+ "<"+tag_name+">"+industry_sector+"</"+ tag_name+">";
								}
								else if(tag_name.equalsIgnoreCase("industry_marco")){
									xml_str = xml_str+ "<"+tag_name+">"+industry_marco+"</"+ tag_name+">";
								}
								else if(tag_name.equalsIgnoreCase("industry_micro")){
									xml_str = xml_str+ "<"+tag_name+">"+industry_micro+"</"+ tag_name+">";
								}
								else if(tag_name.equalsIgnoreCase("bvr")){
									xml_str = xml_str+ "<"+tag_name+">"+bvr+"</"+ tag_name+">";
								}
								else if(tag_name.equalsIgnoreCase("eff_date_estba")){
									xml_str = xml_str+ "<"+tag_name+">"+eff_date_estba+"</"+ tag_name+">";
								}
								else if(tag_name.equalsIgnoreCase("poa")){
									xml_str = xml_str+ "<"+tag_name+">"+poa+"</"+ tag_name+">";
								}
								else if(tag_name.equalsIgnoreCase("tlc_issue_date")){
									xml_str = xml_str+ "<"+tag_name+">"+tlc_issue_date+"</"+ tag_name+">";
								}
								SKLogger.writeLog("RLOS COMMON"," after adding cmplx_CompanyDetails_cmplx_CompanyGrid+ "+xml_str);
								int_xml.put(parent_tag, xml_str);
							}
							
							else if(((tag_name.equalsIgnoreCase("blacklist_cust_type"))||(tag_name.equalsIgnoreCase("internal_blacklist"))||(tag_name.equalsIgnoreCase("internal_blacklist_date"))||(tag_name.equalsIgnoreCase("internal_blacklist_code"))||(tag_name.equalsIgnoreCase("negative_cust_type"))||(tag_name.equalsIgnoreCase("internal_negative_flag"))||(tag_name.equalsIgnoreCase("internal_negative_date"))||(tag_name.equalsIgnoreCase("internal_negative_code"))) && Call_name.equalsIgnoreCase("DECTECH")){
								SKLogger.writeLog("RLOS COMMON"," iNSIDE channelcode+ ");
								String squeryBlacklist="select BlacklistFlag,BlacklistDate,BlacklistReasonCode,NegatedFlag,NegatedDate,NegatedReasonCode from ng_rlos_cif_detail where cif_wi_name='"+formObject.getWFWorkitemName()+"' and cif_searchType = 'Internal'";
								List<List<String>> Blacklist=formObject.getDataFromDataSource(squeryBlacklist);
								String internal_blacklist =  "";
								String internal_blacklist_date =  "";
								String internal_blacklist_code =  "";
								String internal_negative_flag =  "";
								String internal_negative_date =  "";
								String internal_negative_code =  "";

								if (Blacklist!=null && Blacklist.size()>0){		
									internal_blacklist =  Blacklist.get(0).get(0);
									internal_blacklist_date =  Blacklist.get(0).get(1);
									internal_blacklist_code =  Blacklist.get(0).get(2);
									internal_negative_flag =  Blacklist.get(0).get(3);
									internal_negative_date =  Blacklist.get(0).get(4);
									internal_negative_code =  Blacklist.get(0).get(5);
								}
								String xml_str =  int_xml.get(parent_tag);

								if(tag_name.equalsIgnoreCase("blacklist_cust_type")){
									xml_str = xml_str+ "<"+tag_name+">I</"+ tag_name+">";
								}
								else if(tag_name.equalsIgnoreCase("internal_blacklist")){
									xml_str = xml_str+ "<"+tag_name+">"+internal_blacklist+"</"+ tag_name+">";
								}
								else if(tag_name.equalsIgnoreCase("internal_blacklist_date")){
									xml_str = xml_str+ "<"+tag_name+">"+internal_blacklist_date+"</"+ tag_name+">";
								}
								else if(tag_name.equalsIgnoreCase("internal_blacklist_code")){
									xml_str = xml_str+ "<"+tag_name+">"+internal_blacklist_code+"</"+ tag_name+">";
								}
								else if(tag_name.equalsIgnoreCase("negative_cust_type")){
									xml_str = xml_str+ "<"+tag_name+">I</"+ tag_name+">";
								}
								else if(tag_name.equalsIgnoreCase("internal_negative_flag")){
									xml_str = xml_str+ "<"+tag_name+">"+internal_negative_flag+"</"+ tag_name+">";
								}
								else if(tag_name.equalsIgnoreCase("internal_negative_date")){
									xml_str = xml_str+ "<"+tag_name+">"+internal_negative_date+"</"+ tag_name+">";
								}
								else if(tag_name.equalsIgnoreCase("internal_negative_code")){
									xml_str = xml_str+ "<"+tag_name+">"+internal_negative_code+"</"+ tag_name+">";
								}
								SKLogger.writeLog("RLOS COMMON"," after adding internal_blacklist+ "+xml_str);
								int_xml.put(parent_tag, xml_str);


							}

							else if((tag_name.equalsIgnoreCase("external_blacklist_flag")||tag_name.equalsIgnoreCase("external_blacklist_date")||tag_name.equalsIgnoreCase("external_blacklist_code")) && Call_name.equalsIgnoreCase("DECTECH")){
								SKLogger.writeLog("RLOS COMMON"," iNSIDE channelcode+ ");
								String squeryBlacklist="select BlacklistFlag,BlacklistDate,BlacklistReasonCode from ng_rlos_cif_detail where cif_wi_name='"+formObject.getWFWorkitemName()+"' and cif_searchType = 'External'";
								List<List<String>> Blacklist=formObject.getDataFromDataSource(squeryBlacklist);
								String External_blacklist =  "";
								String External_blacklist_date =  "";
								String External_blacklist_code =  "";

								if (Blacklist!=null && Blacklist.size()>0){		
									External_blacklist =  Blacklist.get(0).get(0);
									External_blacklist_date =  Blacklist.get(0).get(1);
									External_blacklist_code =  Blacklist.get(0).get(2);
								}
								String xml_str =  int_xml.get(parent_tag);
								if(tag_name.equalsIgnoreCase("external_blacklist_flag")){
									xml_str = xml_str+ "<"+tag_name+">"+External_blacklist+"</"+ tag_name+">";
								}
								else if(tag_name.equalsIgnoreCase("external_blacklist_date")){
									xml_str = xml_str+ "<"+tag_name+">"+External_blacklist_date+"</"+ tag_name+">";
								}
								else if(tag_name.equalsIgnoreCase("external_blacklist_code")){
									xml_str = xml_str+ "<"+tag_name+">"+External_blacklist_code+"</"+ tag_name+">";
								}

								SKLogger.writeLog("RLOS COMMON"," after adding internal_blacklist+ "+xml_str);
								int_xml.put(parent_tag, xml_str);


							}
							
							else if((tag_name.equalsIgnoreCase("auth_sig_sole_emp")||tag_name.equalsIgnoreCase("shareholding_perc")) && Call_name.equalsIgnoreCase("DECTECH") && emp_type.equalsIgnoreCase("Self Employed")){
								SKLogger.writeLog("RLOS COMMON"," iNSIDE channelcode+ ");
								String auth_sig_sole_emp =  "";
								String shareholding_perc =  "";
								int Authsign_row_count = formObject.getLVWRowCount("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails");
								
								if (Authsign_row_count !=0){
									auth_sig_sole_emp =(formObject.getNGValue("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails", 0, 10).equalsIgnoreCase("Yes")?"Y":"N"); //0
									shareholding_perc = formObject.getNGValue("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails", 0, 9); //0
									
									
								}
								String xml_str =  int_xml.get(parent_tag);
								if(tag_name.equalsIgnoreCase("auth_sig_sole_emp")){
									xml_str = xml_str+ "<"+tag_name+">"+auth_sig_sole_emp+"</"+ tag_name+">";
								}
								else if(tag_name.equalsIgnoreCase("shareholding_perc")){
									xml_str = xml_str+ "<"+tag_name+">"+shareholding_perc+"</"+ tag_name+">";
								}
								

								SKLogger.writeLog("RLOS COMMON"," after adding shareholding_perc+ "+xml_str);
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

							


							
							/*else if(tag_name.equalsIgnoreCase("resident_flag") && Call_name.equalsIgnoreCase("DECTECH")){
								if(int_xml.containsKey(parent_tag))
								{
									String resident_flag=formObject.getNGValue("cmplx_Customer_ResidentNonResident");

									String xml_str = int_xml.get(parent_tag);
									if (resident_flag!=null){
										if (resident_flag.equalsIgnoreCase("Resident")){
											resident_flag="Y";
										}
										else{
											resident_flag="N";
										}
									}
									xml_str = xml_str + "<"+tag_name+">"+resident_flag
									+"</"+ tag_name+">";

									SKLogger.writeLog("RLOS COMMON"," after adding confirmedinjob+ "+xml_str);
									int_xml.put(parent_tag, xml_str);

								}		                            	
							}*/
							else if(tag_name.equalsIgnoreCase("cust_name") && Call_name.equalsIgnoreCase("DECTECH")){
								if(int_xml.containsKey(parent_tag))
								{
									String first_name=formObject.getNGValue("cmplx_Customer_FIrstNAme");
									String middle_name=formObject.getNGValue("cmplx_Customer_MiddleName");
									String last_name=formObject.getNGValue("cmplx_Customer_LAstNAme");

									String full_name=first_name+" "+middle_name+" "+last_name;

									String xml_str = int_xml.get(parent_tag);
									xml_str = xml_str + "<"+tag_name+">"+full_name
									+"</"+ tag_name+">";

									SKLogger.writeLog("RLOS COMMON"," after adding confirmedinjob+ "+xml_str);
									int_xml.put(parent_tag, xml_str);

								}		                            	
							}
							else if(tag_name.equalsIgnoreCase("confirmed_in_job") && Call_name.equalsIgnoreCase("DECTECH")){
								if(int_xml.containsKey(parent_tag))
								{
									String confirmedinjob=formObject.getNGValue("cmplx_EmploymentDetails_JobConfirmed");

									if (confirmedinjob!=null){
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
							}
							else if(tag_name.equalsIgnoreCase("included_pl_aloc") && Call_name.equalsIgnoreCase("DECTECH")){
								if(int_xml.containsKey(parent_tag))
								{
									String included_pl_aloc=formObject.getNGValue("cmplx_EmploymentDetails_IncInPL");

									if (included_pl_aloc!=null){
										if (included_pl_aloc.equalsIgnoreCase("true")){
											included_pl_aloc="Y";
										}
										else{
											included_pl_aloc="N";
										}

										String xml_str = int_xml.get(parent_tag);
										xml_str = xml_str + "<"+tag_name+">"+included_pl_aloc
										+"</"+ tag_name+">";

										SKLogger.writeLog("RLOS COMMON"," after adding included_pl_aloc+ "+xml_str);
										int_xml.put(parent_tag, xml_str);
									}
								}
							}
							else if(tag_name.equalsIgnoreCase("included_cc_aloc") && Call_name.equalsIgnoreCase("DECTECH")){
								if(int_xml.containsKey(parent_tag))
								{
									String included_cc_aloc=formObject.getNGValue("cmplx_EmploymentDetails_IncInCC");

									if (included_cc_aloc!=null){
										if (included_cc_aloc.equalsIgnoreCase("true")){
											included_cc_aloc="Y";
										}
										else{
											included_cc_aloc="N";
										}

										String xml_str = int_xml.get(parent_tag);
										xml_str = xml_str + "<"+tag_name+">"+included_cc_aloc
										+"</"+ tag_name+">";

										SKLogger.writeLog("RLOS COMMON"," after adding cmplx_EmploymentDetails_IncInCC+ "+xml_str);
										int_xml.put(parent_tag, xml_str);
									}	
								}
							}
							else if(tag_name.equalsIgnoreCase("vip_flag") && Call_name.equalsIgnoreCase("DECTECH")){
								if(int_xml.containsKey(parent_tag))
								{
									String vip_flag=formObject.getNGValue("cmplx_Customer_VIPFlag");


									if (vip_flag.equalsIgnoreCase("true")){
										vip_flag="Y";
									}
									else{
										vip_flag="N";
									}

									String xml_str = int_xml.get(parent_tag);
									xml_str = xml_str + "<"+tag_name+">"+vip_flag
									+"</"+ tag_name+">";

									SKLogger.writeLog("RLOS COMMON"," after adding cmplx_Customer_VIPFlag+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}		                            	
							}
							else if(tag_name.equalsIgnoreCase("standing_instruction") && Call_name.equalsIgnoreCase("DECTECH")){
								SKLogger.writeLog("RLOS COMMON"," iNSIDE standing_instruction+ ");
								String squerynoc="SELECT count(*) FROM ng_rlos_FinancialSummary_SiDtls WHERE WI_Name='"+formObject.getWFWorkitemName()+"'";
								List<List<String>> NOC=formObject.getDataFromDataSource(squerynoc);
								SKLogger.writeLog("RLOS COMMON"," after adding cmplx_Customer_VIPFlag+ "+squerynoc);
								SKLogger.writeLog("RLOS COMMON"," after adding cmplx_Customer_VIPFlag+ "+NOC);
								String standing_instruction="";
								standing_instruction=NOC.get(0).get(0);
								if (NOC!=null && NOC.size()>0){
									String xml_str =  int_xml.get(parent_tag);
									if (standing_instruction.equalsIgnoreCase("0")){
										standing_instruction="N";
									}
									else{
										standing_instruction="Y";
									}

									xml_str = xml_str+ "<"+tag_name+">"+standing_instruction
									+"</"+ tag_name+">";

									SKLogger.writeLog("RLOS COMMON"," after adding standing_instruction+ "+xml_str);
									int_xml.put(parent_tag, xml_str);

								}

							}
							//Coding Done for avg_credit_turnover_6, avg_credit_turnover_3,avg_bal_3 and avg_bal_6 tag
							else if((tag_name.equalsIgnoreCase("avg_credit_turnover_6")||tag_name.equalsIgnoreCase("avg_credit_turnover_3")||tag_name.equalsIgnoreCase("avg_bal_3")||tag_name.equalsIgnoreCase("avg_bal_6") )&& Call_name.equalsIgnoreCase("DECTECH")){
								if(int_xml.containsKey(parent_tag))
								{	
									double avg_credit_turnover6th=0;
									double avg_credit_turnover3rd=0;
									double avg_bal_3=0;
									double avg_bal_6=0;
									String avg_credit_turnover_freq=formObject.getNGValue("cmplx_IncomeDetails_AvgCredTurnoverFreq");
									String avg_bal_freq=formObject.getNGValue("cmplx_IncomeDetails_AvgBalFreq");
									String avg_credit_turnover=formObject.getNGValue("cmplx_IncomeDetails_AvgCredTurnover");
									String avg_bal=formObject.getNGValue("cmplx_IncomeDetails_AvgBal");
									if (avg_credit_turnover!=(null)&&(!avg_credit_turnover.equalsIgnoreCase(""))){
										avg_credit_turnover6th=Double.parseDouble(avg_credit_turnover);
										System.out.println("avg_credit_turnover6th value"+avg_credit_turnover6th);

										if (avg_credit_turnover_freq.equalsIgnoreCase("Annually")){
											avg_credit_turnover6th=avg_credit_turnover6th/2;
											avg_credit_turnover3rd=avg_credit_turnover6th/2;
										}
										else if (avg_credit_turnover_freq.equalsIgnoreCase("Half Yearly")){
											avg_credit_turnover6th=avg_credit_turnover6th;
											avg_credit_turnover3rd=avg_credit_turnover6th/2;
										}
										else if (avg_credit_turnover_freq.equalsIgnoreCase("Quaterly")){
											avg_credit_turnover6th=2*avg_credit_turnover6th;
											avg_credit_turnover3rd=avg_credit_turnover6th/2;
										}
										else if (avg_credit_turnover_freq.equalsIgnoreCase("monthly")){
											avg_credit_turnover6th=6*avg_credit_turnover6th;
											avg_credit_turnover3rd=avg_credit_turnover6th/2;
										}
									}
									if (avg_bal!=(null)&&(!avg_bal.equalsIgnoreCase(""))){
										//avg_bal_6=Double.parseDouble(avg_bal);
										System.out.println("avg_credit_turnover6th value"+avg_credit_turnover6th);

										avg_bal_6=Double.parseDouble(avg_bal);
										if (avg_bal_freq.equalsIgnoreCase("Annually")){
											avg_bal_6=avg_bal_6/2;
											avg_bal_3=avg_bal_6/2;
										}
										else if (avg_bal_freq.equalsIgnoreCase("Half Yearly")){
											avg_bal_6=avg_bal_6;
											avg_bal_3=avg_bal_6/2;
										}
										else if (avg_bal_freq.equalsIgnoreCase("Quaterly")){
											avg_bal_6=2*avg_bal_6;
											avg_bal_3=avg_bal_6/2;
										}
										else if (avg_bal_freq.equalsIgnoreCase("monthly")){
											avg_bal_6=6*avg_bal_6;
											avg_bal_3=avg_bal_6/2;
										}

									}	

									String xml_str = int_xml.get(parent_tag);

									if (tag_name.equalsIgnoreCase("avg_credit_turnover_6")){
										xml_str = xml_str + "<"+tag_name+">"+avg_credit_turnover6th
										+"</"+ tag_name+">";
									}
									else if (tag_name.equalsIgnoreCase("avg_credit_turnover_3")){
										xml_str = xml_str + "<"+tag_name+">"+avg_credit_turnover3rd
										+"</"+ tag_name+">";
									}
									else if (tag_name.equalsIgnoreCase("avg_bal_3")){
										xml_str = xml_str + "<"+tag_name+">"+avg_bal_3
										+"</"+ tag_name+">";
									}
									else if (tag_name.equalsIgnoreCase("avg_bal_6")){
										xml_str = xml_str + "<"+tag_name+">"+avg_bal_6
										+"</"+ tag_name+">";
									}
									SKLogger.writeLog("RLOS COMMON"," after adding cmplx_Customer_VIPFlag+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}		                            	
							}
							//Coding Done for avg_credit_turnover_6, avg_credit_turnover_3,avg_bal_3 and avg_bal_6 tag
							else if(tag_name.equalsIgnoreCase("aggregate_exposed") && Call_name.equalsIgnoreCase("DECTECH")){
								if(int_xml.containsKey(parent_tag))
								{
									String aeQuery = "select isnull(CreditLimit,0) as CreditLimit,'' as TotalOutstandingAmt,'' as TotalAmt, '' as TotalAmount,'' as TakeOverAmount FROM ng_RLOS_CUSTEXPOSE_CardDetails where Wi_Name='"+formObject.getWFWorkitemName()+"' union all select '',isnull(TotalOutstandingAmt,0),'','','' FROM ng_RLOS_CUSTEXPOSE_LoanDetails where Wi_Name='"+formObject.getWFWorkitemName()+"' union all select '','',isnull(TotalAmt,0),'','' FROM ng_rlos_cust_extexpo_LoanDetails where Wi_Name='"+formObject.getWFWorkitemName()+"' union all select '','','',isnull(TotalAmount,0),'' FROM ng_rlos_cust_extexpo_CardDetails where Wi_Name='"+formObject.getWFWorkitemName()+"' union all select '','','','',isnull(TakeOverAmount,0) FROM ng_rlos_gr_LiabilityAddition where LiabilityAddition_wiName='"+formObject.getWFWorkitemName()+"'";
									   SKLogger.writeLog("aggregate_exposed sQuery"+aeQuery, "");
	                                    List<List<String>> aggregate_exposed = formObject.getDataFromDataSource(aeQuery);
	                                    SKLogger.writeLog("aggregate_exposed list size"+aggregate_exposed.size(), "");
	                                    double CreditLimit;
	                                    double TotalOutstandingAmt;
	                                    double TotalAmount;
	                                    double TotalAmt;
	                                    double takeOverAmount=0.0f;//added by akshay on 25/9/17 as per point 2 of problem sheet
	                                    SKLogger.writeLog("outsidefor list ", "values");
	                                    CreditLimit=0.0f;
	                                    TotalOutstandingAmt=0.0f;
	                                    TotalAmount=0.0f;
	                                    TotalAmt=0.0f;
	                                    double Total;
	                                    SKLogger.writeLog("outsidefor list ", "values");
	                                    Total=0.0f;
	                                    if (aggregate_exposed!=null && aggregate_exposed.size()>0){        
	                                    for (int i = 0; i<aggregate_exposed.size();i++){
	                                        
	                                        
	                                        if(aggregate_exposed.get(i).get(0)!=null && !aggregate_exposed.get(i).get(0).isEmpty() &&  !aggregate_exposed.get(i).get(0).equals("") && !aggregate_exposed.get(i).get(0).equalsIgnoreCase("null") ){
	                                                CreditLimit = CreditLimit+  Float.parseFloat(aggregate_exposed.get(i).get(0));
	                                                SKLogger.writeLog("CreditLimit list ", "values"+CreditLimit);
	                                        }
	                                        if(aggregate_exposed.get(i).get(1)!=null && !aggregate_exposed.get(i).get(1).isEmpty() &&  !aggregate_exposed.get(i).get(1).equals("") && !aggregate_exposed.get(i).get(1).equalsIgnoreCase("null") ){
	                                                TotalOutstandingAmt =TotalOutstandingAmt + Float.parseFloat(aggregate_exposed.get(i).get(1));
	                                                SKLogger.writeLog("TotalOutstandingAmt list ", "values"+TotalOutstandingAmt);
	                                        }
	                                        if(aggregate_exposed.get(i).get(2)!=null && !aggregate_exposed.get(i).get(2).isEmpty() &&  !aggregate_exposed.get(i).get(2).equals("") && !aggregate_exposed.get(i).get(2).equalsIgnoreCase("null") ){
	                                                TotalAmount =TotalAmount+  Float.parseFloat(aggregate_exposed.get(i).get(2));
	                                                SKLogger.writeLog("TotalAmount list ", "values"+TotalAmount);
	                                        }
	                                        if(aggregate_exposed.get(i).get(3)!=null && !aggregate_exposed.get(i).get(3).isEmpty() &&  !aggregate_exposed.get(i).get(3).equals("") && !aggregate_exposed.get(i).get(3).equalsIgnoreCase("null") ){
	                                                TotalAmt = TotalAmt+ Float.parseFloat(aggregate_exposed.get(i).get(3));
	                                                SKLogger.writeLog("TotalAmt list ", "values"+TotalAmt);
	                                        }
	                                        //added by akshay on 25/9/17 as per point 2 of problem sheet
	                                        if(aggregate_exposed.get(i).get(4)!=null && !aggregate_exposed.get(i).get(4).isEmpty() &&  !aggregate_exposed.get(i).get(4).equals("") && !aggregate_exposed.get(i).get(4).equalsIgnoreCase("null") ){
	                                            takeOverAmount = takeOverAmount+ Float.parseFloat(aggregate_exposed.get(i).get(4));
	                                            SKLogger.writeLog("TotalAmt list ", "values"+TotalAmt);
	                                    }
	                                        //ended by akshay on 25/9/17 as per point 2 of problem sheet
	                                    }
	                                    }
	                                    Total=    CreditLimit+TotalOutstandingAmt+TotalAmount+TotalAmt+takeOverAmount;
	                                    BigDecimal Tot=new BigDecimal(CreditLimit+TotalOutstandingAmt+TotalAmount+TotalAmt);
	                                    SKLogger.writeLog("Total list ", "values"+Total);
	                                    SKLogger.writeLog("Total list ", "values"+Tot);
	                                    
	                                    String conv_String=putComma(Tot.toString());//added by akshay on 25/9/17 as per point 3 of problem sheet
	                                    formObject.setNGValue("cmplx_Liability_New_AggrExposure", conv_String);//changed by akshay on 25/9/17 as per point 2 of problem sheet
	                                    String xml_str = int_xml.get(parent_tag);
	                                    xml_str = xml_str + "<"+tag_name+">"+Tot+"</"+ tag_name+">";
	 
	                                    SKLogger.writeLog("RLOS COMMON"," after adding aggregate_exposed+ "+xml_str);
	                                    int_xml.put(parent_tag, xml_str);
	                                }                                        
	                            }

							else if(tag_name.equalsIgnoreCase("accomodation_provided") && Call_name.equalsIgnoreCase("DECTECH")){
								if(int_xml.containsKey(parent_tag))
								{
									String accomodation_provided=formObject.getNGValue("cmplx_IncomeDetails_Accomodation");

									if (accomodation_provided!=null){
										if (accomodation_provided.equalsIgnoreCase("Yes")){
											accomodation_provided="Y";
										}
										else{
											accomodation_provided="N";
										}

										String xml_str = int_xml.get(parent_tag);
										xml_str = xml_str + "<"+tag_name+">"+accomodation_provided
										+"</"+ tag_name+">";

										SKLogger.writeLog("RLOS COMMON"," after adding confirmedinjob+ "+xml_str);
										int_xml.put(parent_tag, xml_str);
									}	
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
									if (xml_str==null){
										SKLogger.writeLog("RLOS COMMON"," before adding bhrabc"+xml_str);
										xml_str="";
									}
									xml_str = xml_str + temp;
									SKLogger.writeLog("RLOS COMMON"," after InternalBouncedCheques+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}

							}
							else if(tag_name.equalsIgnoreCase("InternalBureauIndividualProducts") && Call_name.equalsIgnoreCase("DECTECH")){

								String xml_str = int_xml.get(parent_tag);
								SKLogger.writeLog("RLOS COMMON"," before adding InternalBureauIndividualProducts+ "+xml_str);
								String temp = InternalBureauIndividualProducts();
								if(!temp.equalsIgnoreCase("")){
									if (xml_str==null){
										SKLogger.writeLog("RLOS COMMON"," before adding bhrabc"+xml_str);
										xml_str="";
									}
									xml_str = xml_str + temp;
									SKLogger.writeLog("RLOS COMMON"," after InternalBureauIndividualProducts+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}

							}
							else if(tag_name.equalsIgnoreCase("InternalBureauPipelineProducts") && Call_name.equalsIgnoreCase("DECTECH")){

								String xml_str = int_xml.get(parent_tag);
								SKLogger.writeLog("RLOS COMMON"," before adding InternalBureauPipelineProducts+ "+xml_str);
								String temp = InternalBureauPipelineProducts();
								if(!temp.equalsIgnoreCase("")){
									if (xml_str==null){
										SKLogger.writeLog("RLOS COMMON"," before adding bhrabc"+xml_str);
										xml_str="";
									}
									xml_str = xml_str + temp;
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
									if (xml_str==null){
										SKLogger.writeLog("RLOS COMMON"," before adding bhrabc"+xml_str);
										xml_str="";
									}
									xml_str =  xml_str + temp;
									SKLogger.writeLog("RLOS COMMON"," after ExternalBouncedCheques+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}                    	
							}
							else if(tag_name.equalsIgnoreCase("ExternalBureauIndividualProducts") && Call_name.equalsIgnoreCase("DECTECH")){

								String xml_str = int_xml.get(parent_tag);
								SKLogger.writeLog("RLOS COMMON"," before adding ExternalBureauIndividualProducts+ "+xml_str);
								String temp =  ExternalBureauIndividualProducts();
								String Manual_add_Liab =  ExternalBureauManualAddIndividualProducts();

								if((!temp.equalsIgnoreCase("")) || (!Manual_add_Liab.equalsIgnoreCase(""))){
									if (xml_str==null){
										SKLogger.writeLog("RLOS COMMON"," before adding bhrabc"+xml_str);
										xml_str="";
									}
									xml_str =  xml_str + temp + Manual_add_Liab;
									SKLogger.writeLog("RLOS COMMON"," after ExternalBureauIndividualProducts+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}	                            	
							}
							else if(tag_name.equalsIgnoreCase("ExternalBureauPipelineProducts") && Call_name.equalsIgnoreCase("DECTECH")){

								String xml_str = int_xml.get(parent_tag);
								SKLogger.writeLog("RLOS COMMON"," before adding ExternalBureauPipelineProducts+ "+xml_str);
								String temp =  ExternalBureauPipelineProducts();
								if(!temp.equalsIgnoreCase("")){
									if (xml_str==null){
										SKLogger.writeLog("RLOS COMMON"," before adding bhrabc"+xml_str);
										xml_str="";
									}
									xml_str =  xml_str + temp;
									SKLogger.writeLog("RLOS COMMON"," after ExternalBureauPipelineProducts+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}                        	
							}

							else if(tag_name.equalsIgnoreCase("ServiceId") && Call_name.equalsIgnoreCase("EID_Genuine") && parent_tag.equalsIgnoreCase("SecurityDataInfo"))
							{
								String ServiceId ="";
								Timestamp timestamp = new Timestamp(System.currentTimeMillis());
								ServiceId = timestamp.getTime()+"";
								String xml_str = "<"+tag_name+">"+ServiceId.substring(1,ServiceId.length())
								+"</"+ tag_name+">";

								SKLogger.writeLog("RLOS COMMON"," after adding res_flag+ "+xml_str);
								int_xml.put(parent_tag, xml_str);
							}

							/*else if(tag_name.equalsIgnoreCase("NonResidentFlag") && Call_name.equalsIgnoreCase("NEW_CUSTOMER_REQ") && parent_tag.equalsIgnoreCase("PersonDetails")){
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
							}*/
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

											//added by Akshay on 3/7/2017 to remove commas from numbers
											if(form_control_val.contains(",")){
												form_control_val.replaceAll(",","");
											}
											//ended by Akshay 3/7/2017 to remove commas from numbers

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
												else if(format_name.equalsIgnoreCase("number")){
													if(form_control_val.contains(",")){
														form_control_val = form_control_val.replace(",", "");
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
												DateFormat df = new SimpleDateFormat("MM/dd/yyyy"); 
												DateFormat df_new = new SimpleDateFormat(format_type);
												// java.util.Date startDate;
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
											else if(format_name.equalsIgnoreCase("number")){
												if(form_control_val.contains(",")){
													form_control_val = form_control_val.replace(",", "");
												}

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

						SKLogger.writeLog("RLOS Common before clean xml",final_xml.toString());
						final_xml = new StringBuffer( Clean_Xml(final_xml.toString()));
						SKLogger.writeLog("RLOS Common After clean xml",final_xml.toString());
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
								int num = din.read(readBuffer);
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
			SKLogger.writeLog("Generate XML:","Exception occured in main thread: "+ printException(e));
			return "0";
		}
		catch(Exception e){
			SKLogger.writeLog("Generate XML: Exception ocurred: ",e.getLocalizedMessage());
			System.out.println("$$final_xml: "+final_xml);
			SKLogger.writeLog("Generate XML:","Exception occured in main thread: "+ printException(e));
			return "0";
		}    
		return "";

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
		SKLogger.writeLog("RLOSCommon valueSetCustomer", "Inside valueSetCustomer():"+outputResponse);
		String outputXMLHead = "";
		String outputXMLMsg = "";
		String returnDesc = "";
		String returnCode = "";
		String response= "";
		String Call_type="";


		//XMLParser objXMLParser = new XMLParser();
		try
		{
			SKLogger.writeLog("RLOSCommon valueSetCustomer", "Inside try");
			if(outputResponse.indexOf("<EE_EAI_HEADER>")>-1)
			{
				outputXMLHead=outputResponse.substring(outputResponse.indexOf("<EE_EAI_HEADER>"),outputResponse.indexOf("</EE_EAI_HEADER>")+16);
				SKLogger.writeLog("RLOSCommon valueSetCustomer", "outputXMLHead");
			}
			//objXMLParser.setInputXML(outputXMLHead);
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
			if(outputResponse.indexOf("<CallType>")>-1){
				Call_type= outputResponse.substring(outputResponse.indexOf("<CallType>")+10,outputResponse.indexOf("</CallType>"));
				if	(Call_type!= null){
					if (Call_type.equals("CA")){
						dectechfromliability(outputResponse);
					}
					if (Call_type.equals("PM")){
						dectechfromeligbility(outputResponse);
					}
				}

			}
			
			//ended by me
		}
		catch(Exception e)
		{            
			SKLogger.writeLog("Exception occured in valueSetCustomer method:  ",e.getMessage());
			System.out.println("Exception occured in valueSetCustomer method: "+ e.getMessage());
		}
	}

	public static void dectechfromliability(String outputResponse){

		String Output_TAI="";
		String Output_Existing_DBR  ="";
		String Output_Net_Salary_DBR="";

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//Setting the value in ELIGANDPROD info
		try{
			if (outputResponse.contains("Output_TAI")){
				Output_TAI = outputResponse.substring(outputResponse.indexOf("<Output_TAI>")+12,outputResponse.indexOf("</Output_TAI>"));
				if (Output_TAI!=null){
					formObject.setNGValue("cmplx_Liability_New_TAI", Output_TAI);
				}
				SKLogger.writeLog("$$outputXMLMsg ","inside outpute get Output_TAI"+Output_TAI);
			}

			//FormReference formObject = FormContext.getCurrentInstance().getFormReference();
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
		}
		catch(Exception e){
			SKLogger.writeLog("Dectech error", "Exception:"+e.getMessage());

		}
	}

	public static void dectechfromeligbility(String outputResponse){
		try{
			if(outputResponse.indexOf("<PM_Reason_Codes>")>-1 && outputResponse.indexOf("<PM_Outputs>")>-1 ){
				String squery="";
				String Output_TAI="";
				String Output_Final_DBR="";
				String Output_Existing_DBR  ="";
				String Output_Eligible_Amount="";
				String Output_Affordable_EMI="";
				String Output_Delegation_Authority="";
				String Grade="";
				String Output_Interest_Rate="";
				String Output_Net_Salary_DBR="";
				double cac_calc_limit=0.0;

				FormReference formObject = FormContext.getCurrentInstance().getFormReference();
				String strOutputXml = "";
				String sGeneralData = formObject.getWFGeneralData();
				SKLogger.writeLog("$$outputXMLMsg ","inside outpute get sGeneralData"+sGeneralData);

				String cabinetName = sGeneralData.substring(sGeneralData.indexOf("<EngineName>")+12,sGeneralData.indexOf("</EngineName>")); 
				String sessionId = sGeneralData.substring(sGeneralData.indexOf("<DMSSessionId>")+14,sGeneralData.indexOf("</DMSSessionId>"));
				String jtsIp = sGeneralData.substring(sGeneralData.indexOf("<JTSIP>")+7,sGeneralData.indexOf("</JTSIP>")); ;
				String jtsPort = sGeneralData.substring(sGeneralData.indexOf("<JTSPORT>")+9,sGeneralData.indexOf("</JTSPORT>")); ;
				String ReqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
				String subProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2);
				String appType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,4);
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
				if (outputResponse.contains("Output_Eligible_Cards")){
					try{
						String Output_Eligible_Cards = outputResponse.substring(outputResponse.indexOf("<Output_Eligible_Cards>")+23,outputResponse.indexOf("</Output_Eligible_Cards>"));
						Output_Eligible_Cards=Output_Eligible_Cards.substring(2, Output_Eligible_Cards.length()-2);
						String strInputXML =ExecuteQuery_APdelete("ng_rlos_IGR_Eligibility_CardLimit","WI_NAME ='" + formObject.getWFWorkitemName() + "'",cabinetName,sessionId);
						strOutputXml = WFCallBroker.execute(strInputXML,jtsIp,Integer.parseInt(jtsPort),1);

						SKLogger.writeLog("Output_Eligible_Cards", "after removing the starting and Trailing:"+Output_Eligible_Cards);
						String[] Output_Eligible_Cards_Arr=Output_Eligible_Cards.split("\\},\\{");
						for (int i=0; i<Output_Eligible_Cards_Arr.length;i++){
							String[] Output_Eligible_Cards_Array=Output_Eligible_Cards_Arr[i].split(",");
							SKLogger.writeLog("Output_Eligible_Cards", "Output_Eligible_Cards_Array:"+Output_Eligible_Cards_Array);
							String[] Card_Prod=Output_Eligible_Cards_Array[0].split(":");
							String[] Limit=Output_Eligible_Cards_Array[1].split(":");
							String[] flag=Output_Eligible_Cards_Array[2].split(":");
							String Card_Product= Card_Prod[1].substring(1,Card_Prod[1].length()-1);
							String LIMIT= Limit[1];
							String FLAG= flag[1].substring(1,flag[1].length()-1);
							String combined_limitquery="select COMBINEDLIMIT_ELIGIBILITY from ng_master_cardProduct where CODE = '"+Card_Product+"' and Subproduct ='"+subProd+"'" ;
							SKLogger.writeLog("combined_limitquery", "combined_limitquery:"+combined_limitquery);
							List<List<String>> combined_limitqueryXML = formObject.getDataFromDataSource(combined_limitquery);
							String combined_limit= combined_limitqueryXML.get(0).get(0);
							SKLogger.writeLog("Output_Eligible_Cards", "Card_Prod:"+Card_Prod[1]);
							SKLogger.writeLog("Output_Eligible_Cards", "Limit:"+Limit[1]);
							SKLogger.writeLog("Output_Eligible_Cards", "flag:"+flag[1]);
							String query="INSERT INTO ng_rlos_IGR_Eligibility_CardLimit(Card_product,Eligible_limit,wi_name,flag,combined_limit) VALUES ('"+Card_Product+"','"+LIMIT+"','"+ formObject.getWFWorkitemName() +"','"+ FLAG +"','"+ combined_limit +"')";
							SKLogger.writeLog("Output_Eligible_Cards", "QUERY:"+query);
							formObject.saveDataIntoDataSource(query);

							/* for (int j=0; j<Output_Eligible_Cards_Array.length;j++){
	    					 String[] values=Output_Eligible_Cards_Array[j].split(":");
	    					 SKLogger.writeLog("Output_Eligible_Cards", "values:"+values);
	    					  if(values[0].contains("\"")){
	    						  values[0]=values[0].substring(1, values[0].length()-1);
	    				    	}
	    					  if(values[1].contains("\"")){
	    						  values[1]=values[1].substring(1, values[1].length()-1);
	    				    	}
	    					  String strInputXML =ExecuteQuery_APdelete("ng_rlos_IGR_Eligibility_CardProduct","WI_NAME ='" + formObject.getWFWorkitemName() + "'",cabinetName,sessionId);
	    					  strOutputXml = WFCallBroker.execute(strInputXML,jtsIp,Integer.parseInt(jtsPort),1);
	    					  String query="INSERT INTO ng_rlos_IGR_Eligibility_CardLimit("+values[0]+") VALUES ('"+values[1]+"')";
	    					  SKLogger.writeLog("Output_Eligible_Cards", "QUERY:"+query);
	    			    	  formObject.saveDataIntoDataSource(query);
	    				  }*/
						}
					}
					catch(Exception e){
						SKLogger.writeLog("RLOSCommon", "Exception occurred in elig dectech"+printException(e));

					}
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
						formObject.setNGValue("cmplx_EligibilityAndProductInfo_FinalInterestRate", Output_Interest_Rate);
						
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
				if (outputResponse.contains("Output_Affordable_EMI")){
					Output_Affordable_EMI = outputResponse.substring(outputResponse.indexOf("<Output_Affordable_EMI>")+23,outputResponse.indexOf("</Output_Affordable_EMI>")); ;
					SKLogger.writeLog("$$outputXMLMsg ","inside outpute get Output_Affordable_EMI"+Output_Affordable_EMI);

				}
				try{
				double tenor=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor"));
				double RateofInt=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate"));
				double out_aff_emi=Double.parseDouble(Output_Affordable_EMI);
				cac_calc_limit=Math.round(Cas_Limit(out_aff_emi,RateofInt,tenor));
				
				}
				catch(Exception e){}

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


				//String strInputXML = ExecuteQuery_APUpdate("ng_rlos_IGR_Eligibility_CardProduct","Product,Card_Product,Eligible_Card,Decision,Declined_Reason,wi_name","'Credit Card','"+subProd+"','"+Output_Eligible_Amount+"','Declined','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+formObject.getWFWorkitemName()+"'","WI_NAME ='" + formObject.getWFWorkitemName() + "'",cabinetName,sessionId);
				//SKLogger.writeLog("$$Value set for DECTECH->>","UpdateinputXML is:"+UpdateinputXML);
				if (ReqProd.equalsIgnoreCase("Credit Card")){
					String strInputXML =ExecuteQuery_APdelete("ng_rlos_IGR_Eligibility_CardProduct","WI_NAME ='" + formObject.getWFWorkitemName() + "'",cabinetName,sessionId);

					strOutputXml = WFCallBroker.execute(strInputXML,jtsIp,Integer.parseInt(jtsPort),1);
					SKLogger.writeLog("$$Value set for DECTECH->>","strOutputXml is:"+strOutputXml);

					String mainCode=strOutputXml.substring(strOutputXml.indexOf("<MainCode>")+10,strOutputXml.indexOf("</MainCode>"));
					String rowUpdated=strOutputXml.substring(strOutputXml.indexOf("<Output>")+8,strOutputXml.indexOf("</Output>"));
					SKLogger.writeLog("$$Value set for DECTECH->>","mainCode,rowUpdated is: "+mainCode+", "+rowUpdated);
				}
				else {
					String strInputXML =ExecuteQuery_APdelete("ng_rlos_IGR_Eligibility_PersonalLoan","WI_NAME ='" + formObject.getWFWorkitemName() + "'",cabinetName,sessionId);

					strOutputXml = WFCallBroker.execute(strInputXML,jtsIp,Integer.parseInt(jtsPort),1);
					SKLogger.writeLog("$$Value set for DECTECH->>","strOutputXml is:"+strOutputXml);

					String mainCode=strOutputXml.substring(strOutputXml.indexOf("<MainCode>")+10,strOutputXml.indexOf("</MainCode>"));
					String rowUpdated=strOutputXml.substring(strOutputXml.indexOf("<Output>")+8,strOutputXml.indexOf("</Output>"));
					SKLogger.writeLog("$$Value set for DECTECH->>","mainCode,rowUpdated is: "+mainCode+", "+rowUpdated);


				}
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
				SKLogger.writeLog("$$outputResponse ","----------------------------");

				for (int List_len_num = 0; List_len_num < nList.getLength(); List_len_num++) {
					String Reason_Decision="";
					Node nNode = nList.item(List_len_num);
					SKLogger.writeLog("$$outputResponse ","\nCurrent Element :" + nNode.getNodeName());

					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						//System.out.println("Student roll no : " + eElement.getAttribute("rollno"));
						SKLogger.writeLog("$$outputResponse ","inside outpute get Decision_Objective"+ eElement.getElementsByTagName("Decision_Objective").item(0).getTextContent());
						SKLogger.writeLog("$$outputResponse ","inside outpute get Decision_Sequence_Number"+eElement.getElementsByTagName("Decision_Sequence_Number").item(0).getTextContent());
						SKLogger.writeLog("$$outputResponse ","inside outpute get Sequence_Number"+eElement.getElementsByTagName("Sequence_Number").item(0).getTextContent());

						SKLogger.writeLog("$$outputResponse ","inside outpute get Reason_Description"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent());

						String subprodquery="Select Description from ng_MASTER_SubProduct_PL where code='"+subProd+"'";
						String subprodCCquery="Select Description from ng_MASTER_SubProduct_CC where code='"+subProd+"'";
						String apptypequery="Select Description from ng_master_applicationtype where code='"+appType+"'";
						List<List<String>> subprodqueryXML = formObject.getDataFromDataSource(subprodquery);
						List<List<String>> apptypequeryXML = formObject.getDataFromDataSource(apptypequery);
						List<List<String>> subprodCCqueryXML = formObject.getDataFromDataSource(subprodCCquery);
						Reason_Decision = eElement.getElementsByTagName("Reason_Decision").item(0).getTextContent() ;
						SKLogger.writeLog("$$outputResponse ","Value of Reason_Decision"+Reason_Decision);
						if (List_len_num==0){
							if (ReqProd.equalsIgnoreCase("Credit Card")){
								if (Reason_Decision.equalsIgnoreCase("D")){
								squery="Insert into ng_rlos_IGR_Eligibility_CardProduct (S_no,Product,Card_Product,Eligible_Card,Decision,Declined_Reason,Delegation_Authorithy,Score_grade,wi_name,CACCalculatedLimit) values('"+List_len_num+"','Credit Card','"+subprodCCqueryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Declined','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"','"+cac_calc_limit+"')";
								}
								else if (Reason_Decision.equalsIgnoreCase("R")){
									squery="Insert into ng_rlos_IGR_Eligibility_CardProduct (S_no,Product,Card_Product,Eligible_Card,Decision,Deviation_Code_Refer,Delegation_Authorithy,Score_grade,wi_name,CACCalculatedLimit) values('"+List_len_num+"','Credit Card','"+subprodCCqueryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Refer','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"','"+cac_calc_limit+"')";
								}
								else if (Reason_Decision.equalsIgnoreCase("A")){
									squery="Insert into ng_rlos_IGR_Eligibility_CardProduct (S_no,Product,Card_Product,Eligible_Card,Decision,Deviation_Code_Refer,Delegation_Authorithy,Score_grade,wi_name,CACCalculatedLimit) values('"+List_len_num+"','Credit Card','"+subprodCCqueryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Approve','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"','"+cac_calc_limit+"')";
								}
							}
							else {
								if (Reason_Decision.equalsIgnoreCase("D")){
									squery="Insert into ng_rlos_IGR_Eligibility_PersonalLoan (S_no,RequestedLoan,RequestedAppType,EligibleExposure,Decision,DeclinedReasonCode,DelegationAuthorithy,wi_name,CACCalculatedLimit) values('"+List_len_num+"','"+subprodqueryXML.get(0).get(0)+"','"+apptypequeryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Declined','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+formObject.getWFWorkitemName()+"','"+cac_calc_limit+"')";
								}
								else if (Reason_Decision.equalsIgnoreCase("R")){
									squery="Insert into ng_rlos_IGR_Eligibility_PersonalLoan (S_no,RequestedLoan,RequestedAppType,EligibleExposure,Decision,ReferReasoncode,DelegationAuthorithy,wi_name,CACCalculatedLimit) values('"+List_len_num+"','"+subprodqueryXML.get(0).get(0)+"','"+apptypequeryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Refer','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+formObject.getWFWorkitemName()+"','"+cac_calc_limit+"')";
								}
								else if (Reason_Decision.equalsIgnoreCase("A")){
									squery="Insert into ng_rlos_IGR_Eligibility_PersonalLoan (S_no,RequestedLoan,RequestedAppType,EligibleExposure,Decision,ReferReasoncode,DelegationAuthorithy,wi_name,CACCalculatedLimit) values('"+List_len_num+"','"+subprodqueryXML.get(0).get(0)+"','"+apptypequeryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Approve','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+formObject.getWFWorkitemName()+"','"+cac_calc_limit+"')";
								}
							}
						}
						else{
							if (ReqProd.equalsIgnoreCase("Credit Card")){
								if (Reason_Decision.equalsIgnoreCase("D")){
								squery="Insert into ng_rlos_IGR_Eligibility_CardProduct (S_no,Product,Card_Product,Eligible_Card,Decision,Declined_Reason,Delegation_Authorithy,Score_grade,wi_name,CACCalculatedLimit) values('"+List_len_num+"','Credit Card','"+subprodCCqueryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Declined','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"','')";
								}
								else if (Reason_Decision.equalsIgnoreCase("R")){
									squery="Insert into ng_rlos_IGR_Eligibility_CardProduct (S_no,Product,Card_Product,Eligible_Card,Decision,Deviation_Code_Refer,Delegation_Authorithy,Score_grade,wi_name,CACCalculatedLimit) values('"+List_len_num+"','Credit Card','"+subprodCCqueryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Refer','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"','')";
								}
								else if (Reason_Decision.equalsIgnoreCase("A")){
									squery="Insert into ng_rlos_IGR_Eligibility_CardProduct (S_no,Product,Card_Product,Eligible_Card,Decision,Deviation_Code_Refer,Delegation_Authorithy,Score_grade,wi_name,CACCalculatedLimit) values('"+List_len_num+"','Credit Card','"+subprodCCqueryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Approve','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"','')";
								}
							}
							else {
								if (Reason_Decision.equalsIgnoreCase("D")){
									squery="Insert into ng_rlos_IGR_Eligibility_PersonalLoan (S_no,RequestedLoan,RequestedAppType,EligibleExposure,Decision,DeclinedReasonCode,DelegationAuthorithy,wi_name,CACCalculatedLimit) values('"+List_len_num+"','"+subprodqueryXML.get(0).get(0)+"','"+apptypequeryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Declined','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+formObject.getWFWorkitemName()+"','')";
								}
								else if (Reason_Decision.equalsIgnoreCase("R")){
									squery="Insert into ng_rlos_IGR_Eligibility_PersonalLoan (S_no,RequestedLoan,RequestedAppType,EligibleExposure,Decision,ReferReasoncode,DelegationAuthorithy,wi_name,CACCalculatedLimit) values('"+List_len_num+"','"+subprodqueryXML.get(0).get(0)+"','"+apptypequeryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Refer','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+formObject.getWFWorkitemName()+"','')";
								}
								else if (Reason_Decision.equalsIgnoreCase("A")){
									squery="Insert into ng_rlos_IGR_Eligibility_PersonalLoan (S_no,RequestedLoan,RequestedAppType,EligibleExposure,Decision,ReferReasoncode,DelegationAuthorithy,wi_name,CACCalculatedLimit) values('"+List_len_num+"','"+subprodqueryXML.get(0).get(0)+"','"+apptypequeryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Approve','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+formObject.getWFWorkitemName()+"','')";
								}
							}
						}
						

						SKLogger.writeLog("$$outputResponse ","Squery is"+squery);
						formObject.saveDataIntoDataSource(squery);

					}
				}
			}
		}
		catch(Exception e){
			SKLogger.writeLog("Dectech error", "Exception:"+e.getMessage());

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
					SKLogger.writeLog(" Grid_col_tag","Grid_col_tag"+Grid_col_tag);

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
												if(childnode.item(child_node_len).getNodeName().equalsIgnoreCase("EffectiveFrom"))
													EffectFrom= childnode.item(child_node_len).getTextContent();



												if(childnode.item(child_node_len).getNodeName().equalsIgnoreCase("EffectiveTo"))
													EffectTo= childnode.item(child_node_len).getTextContent();

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
												if(EffectFrom!=null && !EffectFrom.equalsIgnoreCase("")){//&& EffectTo!=null && !EffectTo.equalsIgnoreCase("")){
													d1=(Date) sdf.parse(EffectFrom);
													int year = Calendar.getInstance().get(Calendar.YEAR);
													//d2=(Date) sdf.parse(EffectTo);
													SKLogger.writeLog("EffectFrom ",EffectFrom+"  EffectTo: "+EffectTo);
													String[] year1 = EffectFrom.split("-");
													Diff= year-Integer.parseInt(year1[0]);
												}else{
													SKLogger.writeLog("RLSO Common: ","Exception as Effective from or Effective is not received");
												}

												flgYrs="Y";
												flaga="Y";
												SKLogger.writeLog("AKSHAY##","Inside Years tag: "+Diff);     
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
												formObject.addItemFromList("cmplx_AddressDetails_cmplx_AddressGrid", Grid_row);
											}
										}	
										else if(fielddbxml_tag.equalsIgnoreCase("OECDDet")){ 
											SKLogger.writeLog("Inside GETOutputXMLValues:","fieldxml_tag is:"+fielddbxml_tag+" form_control is:"+form_control);
											SKLogger.writeLog("Inside GETOutputXMLValues:","Grid row is:"+Grid_row);
											formObject.addItemFromList(form_control, Grid_row);
										}

										// added	 by yash on 10/7/2017 for getting values in worldcheck grid 
										else if(fielddbxml_tag.equalsIgnoreCase("CustomerDetails")){
											SKLogger.writeLog("RLOS Common# getOutputXMLValues()","Form Control: "+form_control+" Grid data to be added for World check: "+ Grid_row);
											formObject.addItemFromList(form_control,Grid_row);
										}
										//ended 10/07/2017 for worldcheck grid


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
										SKLogger.writeLog("#RLOS Common Inside indirectMapping test tanshu ", "inside list loop");

										for(int x=0;x<col_list_arr.length;x++)
										{
											gridResponseMap.put(col_list_arr[x],mygridlist.get(x));
											SKLogger.writeLog("#RLOS Common Inside indirectMapping ", "inside put map"+x);
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
										SKLogger.writeLog("#RLOS Common Inside indirectMapping ", "all details fetched");
										if(IS_Master.equalsIgnoreCase("Y")){
											String code = nl.item(i).getTextContent();
											SKLogger.writeLog("#RLOS Common Inside indirectMapping code:",code);
											sQuery= "Select description from "+Master_Name+" where Code='"+code+"'";
											SKLogger.writeLog("#RLOS Common Inside indirectMapping code:","Query to select master value: "+  sQuery);
											List<List<String>> query=formObject.getDataFromDataSource(sQuery);
											String value=query.get(0).get(0);
											SKLogger.writeLog("#query.get(0).get(0)",value);
											SKLogger.writeLog("#RLOS Common Inside indirectMapping code:","Query to select master value: "+  sQuery);
											formObject.setNGValue(indirect_form_control,value,false);
											SKLogger.writeLog("indirect_form_control value",formObject.getNGValue(indirect_form_control));
										}

										else if(indirect_form_control!=null && !indirect_form_control.equalsIgnoreCase("")){
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
																for (int child_node_len = 1 ;child_node_len< childnode.getLength();child_node_len++){
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
			outputindex = formObject.getDataFromDataSource(squery);
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
	
	//new code on 11th september
	//tanshu aggarwal for documents(1/06/2017)
	public void fetchIncomingDocRepeater(){
		SKLogger.writeLog(" Inside loadAllCombo_LeadManagement_Documents_Deferral", "inside fetchIncomingDocRepeater");

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		SKLogger.writeLog("INSIDE INCOMING DOCUMENT:" ,"sgdgdgdgdfg");
		String requested_product="";
		String requested_subproduct="";
		String application_type="";
		String product_type="";
		List<List<String>> docName1 = null;
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		SKLogger.writeLog("INSIDE INCOMING DOCUMENT value_doc_name:" ,"valu of row count"+n);

		product_type= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 0);
		SKLogger.writeLog("INSIDE INCOMING DOCUMENT value_doc_name product_type" ,product_type);
		requested_product= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 1);
		SKLogger.writeLog("INSIDE INCOMING DOCUMENT value_doc_name:" ,requested_product);
		requested_subproduct= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2);
		SKLogger.writeLog("INSIDE INCOMING DOCUMENT value_doc_name:" ,requested_subproduct);
		application_type= formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 4);
		SKLogger.writeLog("INSIDE INCOMING DOCUMENT value_doc_name application_type:" ,application_type);

		SKLogger.writeLog("INSIDE INCOMING DOCUMENT value_doc_name outsid for:" ,requested_product);


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

		SKLogger.writeLog("INSIDE INCOMING DOCUMENT:" ,"after making headers");
		FormContext.getCurrentInstance().getFormConfig().getConfigElement("ProcessName");


		List<List<String>> docName = null;

		String documentName = null;
		String documentNameMandatory=null;

		String query = "";

		IRepeater repObj=null;
		SKLogger.writeLog("INSIDE INCOMING DOCUMENT:" ,"after creating the object for repeater");

		int repRowCount = 0;
		repObj = formObject.getRepeaterControl("IncomingDoc_Frame");
		SKLogger.writeLog("INSIDE INCOMING DOCUMENT:" ,""+repObj.toString());
		repObj.setRepeaterHeaders(repeaterHeaders);
		try{
			if (repObj.getRepeaterRowCount() == 0) {
				repObj.clear();
				if(requested_product.equalsIgnoreCase("Personal Loan")){
					//query = "SELECT distinct DocName,Mandatory FROM ng_rlos_DocTable WHERE ProductName='Personal Loan' OR ProductName='All' and ((SubProductName='"+requested_subproduct+"' and Application_Type='"+application_type+"' and Product_Type='"+product_type+"' and  ProcessName='RLOS')  OR ProductName='All') ";
					query = "SELECT distinct DocName,Mandatory FROM ng_rlos_DocTable WHERE (ProductName = 'Personal Loan' and SubProductName = '"+requested_subproduct+"' and Application_Type = '"+application_type+"') or ProductName='All' order by Mandatory desc";
					SKLogger.writeLog("RLOS incoming document", "when row count is  zero inside if"+query);
					docName = formObject.getNGDataFromDataCache(query);
					SKLogger.writeLog("Incomingdoc112222",""+ docName);
				}
				else{
					//query = "SELECT distinct DocName,Mandatory FROM ng_rlos_DocTable WHERE ProductName='"+requested_product+"' OR ProductName='All' and ((SubProductName='"+requested_subproduct+"'  and  ProcessName='RLOS')  OR ProductName='All') ";
					//Query corrected by Deepak.
					query = "SELECT distinct DocName,Mandatory FROM ng_rlos_DocTable WHERE (ProductName = '"+requested_product+"' and SubProductName = '"+requested_subproduct+"') or ProductName='All'";
					SKLogger.writeLog("RLOS incoming document", "when row count is  zero inside else"+query);
					docName = formObject.getNGDataFromDataCache(query);
					SKLogger.writeLog("Incomingdoc11111",""+ docName);
				}
				SKLogger.writeLog("Incomingdoc33333 inside if condition of size",""+ docName.size());
				//added
				for(int i=0;i<docName.size();i++ ){
					repObj.addRow();
					documentName = docName.get(i).get(0);
					documentNameMandatory = docName.get(i).get(1);
					SKLogger.writeLog("Column Added in Repeater documentName"," "+ documentName);
					SKLogger.writeLog("Column Added in Repeater documentNameMandatory"," "+ documentNameMandatory);
					repObj.setValue(i, 0, documentName);
					repObj.setValue(i, 2, documentNameMandatory);
					repObj.setColumnDisabled(0, true);
					repObj.setColumnDisabled(2, true);

					repRowCount = repObj.getRepeaterRowCount();
					SKLogger.writeLog("Repeater Row Count ", " " + repRowCount);
				}

				//ended
			}
			else if (repObj.getRepeaterRowCount() != 0) {
				repObj.clear();
				SKLogger.writeLog("RLOS incoming document", "when row count is not zero");

				//		       query = "SELECT distinct DocName,Mandatory,DocInd FROM ng_rlos_incomingDoc WHERE ProductName='"+requested_product+"' and SubProductName='"+requested_subproduct+"' and  wi_name='"+formObject.getWFWorkitemName()+"'";
				query = "SELECT distinct DocName,ExpiryDate,Mandatory,Status,Remarks,DocInd FROM ng_rlos_incomingDoc WHERE  wi_name='"+formObject.getWFWorkitemName()+"'";

				SKLogger.writeLog("RLOS incoming document", "when row count is not zero"+query);

				docName = formObject.getNGDataFromDataCache(query);
				SKLogger.writeLog("Incomingdoc1111",""+ docName);

				for(int i=0;i<docName.size();i++ ){
					SKLogger.writeLog("Repeater Row Count ", "inside else if for loop");
					repObj.addRow();

					documentName = docName.get(i).get(0);
					String expiryDate = docName.get(i).get(1);
					documentNameMandatory = docName.get(i).get(2);
					String Status = docName.get(i).get(3);
					String Remarks = docName.get(i).get(4);
					String DocInd = docName.get(i).get(5);

					SKLogger.writeLog("Column Added in Repeater documentName"," "+ documentName);
					SKLogger.writeLog("Column Added in Repeater documentNameMandatory"," "+ documentNameMandatory);
					SKLogger.writeLog("Column Added in Repeater expiryDate"," "+ expiryDate);
					SKLogger.writeLog("Column Added in Repeater Status"," "+ Status);
					SKLogger.writeLog("Column Added in Repeater Remarks"," "+ Remarks);
					SKLogger.writeLog("Column Added in Repeater DocInd"," "+ DocInd);

					repObj.setValue(i,0,documentName);
					repObj.setValue(i,1,expiryDate);
					repObj.setValue(i,2,documentNameMandatory);
					repObj.setValue(i,3,Status);
					repObj.setValue(i,4,Remarks);
					repObj.setValue(i,11,DocInd);
					repObj.setColumnDisabled(0, true);
					repObj.setColumnDisabled(2, true);
					repRowCount = repObj.getRepeaterRowCount();
					SKLogger.writeLog("Repeater Row Count ", " " + repRowCount);


				}

			}



		}

		catch (Exception e) {

			SKLogger.writeLog("EXCEPTION    :    ", " " + e.toString());

		} finally {

			repObj = null;

			repeaterHeaders = null;         
		}
	}
	//tanshu aggarwal for documents(1/06/2017)
	//new code on 11th september
	public void loadInDecGrid()
	{

		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		try{
			String ParentWI_Name = formObject.getNGValue("Parent_WIName");
			String query="select FORMAT(datelastchanged,'dd-MM-yyyy hh:mm'),userName,workstepName,Decisiom,remarks,wi_nAme,Entry_date from ng_rlos_gr_Decision with (nolock) where wi_nAme='"+ParentWI_Name+"' or wi_nAme='"+formObject.getWFWorkitemName()+"'";
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
				formObject.addItemFromList("Decision_ListView1", a);
			}
		}catch(Exception e){  SKLogger.writeLog("PLCommon","Exception Occurred loadInDecGrid :"+e.getMessage());}	
	}
	
	//Code commented to handel CIF starting with 0 Strart
	/*public void parse_cif_eligibility(String output,String operation_name){

		try{
			output=output.substring(output.indexOf("<MQ_RESPONSE_XML>")+17,output.indexOf("</MQ_RESPONSE_XML>"));
			SKLogger.writeLog("inside parse_cif_eligibility","");
			Map<Integer, HashMap<String, String> > Cus_details = new HashMap<Integer, HashMap<String, String>>();
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
					//changes done to remove internal blacklist without cif
					if(cif_details.containsKey("CustId")){
						Cus_details.put(cif_details.get("CustId"), (HashMap<String, String>) cif_details) ;
					}
				}
			}
			int Prim_cif = primary_cif_identify(Cus_details);
			if(operation_name.equalsIgnoreCase("Supplementary_Card_Details")){
				FormReference formObject = FormContext.getCurrentInstance().getFormReference();
				formObject.setNGValue("Supplementary_CIFNO",Prim_cif);
			}
			else if(operation_name.equalsIgnoreCase("Primary_CIF")){
				save_cif_data(Cus_details,Prim_cif);
				if(Prim_cif!=0){
					SKLogger.writeLog("parse_cif_eligibility Primary CIF: ",Prim_cif+"");
					Map<String, String> prim_entry = new HashMap<String, String>();
					prim_entry = Cus_details.get(Prim_cif+"");
					String primary_pass = prim_entry.get("PassportNum");
					passport_list = passport_list.replace(primary_pass, "");
					set_nonprimaryPassport(Prim_cif,passport_list);
				}
			}
			SKLogger.writeLog("Prim_cif: ", Prim_cif+"");

		}
		catch(Exception e){
			SKLogger.writeLog("Exception occured while parsing Customer Eligibility : ", e.getMessage());
		}

	}
	public void save_cif_data(Map<Integer, HashMap<String, String>> cusDetails ,int prim_cif){
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
				if(curr_entry.get("CustId").equalsIgnoreCase(prim_cif+"")){
					Cif_data.add("Y");
				}
				else{
					Cif_data.add("N");	
				}
				Cif_data.add(WI_Name);
				formObject.addItemFromList("q_cif_detail", Cif_data);

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
*/	
	//Code commented to handel CIF starting with 0 End
	public void parse_cif_eligibility(String output,String operation_name){

		try{
			output=output.substring(output.indexOf("<MQ_RESPONSE_XML>")+17,output.indexOf("</MQ_RESPONSE_XML>"));
			SKLogger.writeLog("inside parse_cif_eligibility","");
			Map<Integer, HashMap<String, String> > Cus_details = new HashMap<Integer, HashMap<String, String>>();
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
					//changes done to remove internal blacklist without cif
					if(cif_details.containsKey("CustId")){
						Cus_details.put(Integer.parseInt(cif_details.get("CustId")), (HashMap<String, String>) cif_details) ;
					}
				}
			}
			int Prim_cif = primary_cif_identify(Cus_details);
			if(operation_name.equalsIgnoreCase("Supplementary_Card_Details")){
				FormReference formObject = FormContext.getCurrentInstance().getFormReference();
				Map<String, String> Supplementary = new HashMap<String, String>();
				Supplementary = Cus_details.get(Prim_cif);
				formObject.setNGValue("Supplementary_CIFNO",Supplementary.get("CustId")==null?"":Supplementary.get("CustId"));
			}
			else if(operation_name.equalsIgnoreCase("Primary_CIF")){
				save_cif_data(Cus_details,Prim_cif);
				if(Prim_cif!=0){
					SKLogger.writeLog("parse_cif_eligibility Primary CIF: ",Prim_cif+"");
					Map<String, String> prim_entry = new HashMap<String, String>();
					prim_entry = Cus_details.get(Prim_cif);
					String primary_pass = prim_entry.get("PassportNum");
					passport_list = passport_list.replace(primary_pass, "");
					SKLogger.writeLog("Prim_cif: ",prim_entry.get("CustId"));
					set_nonprimaryPassport(prim_entry.get("CustId")==null?"":prim_entry.get("CustId"),passport_list);//changes done to save CIF without 0.
				}
			}
			SKLogger.writeLog("Prim_cif: ", Prim_cif+"");

		}
		catch(Exception e){
			SKLogger.writeLog("Exception occured while parsing Customer Eligibility : ", e.getMessage());
			//throw new ValidatorException(new FacesMessage("Error While processind Customer Eligibility Request, Please contact Administrator !","ReqProd"));
			//throw new ValidatorException(new FacesMessage("Error While processind Customer Eligibility Request, Please contact Administrator !"));
		}

	}
	public void save_cif_data(Map<Integer, HashMap<String, String>> cusDetails ,int prim_cif){
		try{
			SKLogger.writeLog("RLSOCommon", "inside save_cif_data methos");
			FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
			String WI_Name = formObject.getWFWorkitemName();
			SKLogger.writeLog("RLSOCommon", "inside save_cif_data methos wi_name: "+WI_Name );
			Map<String, String> curr_entry = new HashMap<String, String>();
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
				if(curr_entry.get("CustId").equalsIgnoreCase(prim_cif+"")){
					Cif_data.add("Y");
				}
				else{
					Cif_data.add("N");	
				}
				Cif_data.add(WI_Name);
				formObject.addItemFromList("q_cif_detail", Cif_data);

				SKLogger.writeLog("RLSOCommon", "data to dave in cif details grid: "+ Cif_data);
				curr_entry=null;
				Cif_data=null;
			}
		}
		catch(Exception e){
			SKLogger.writeLog("Exception occured while saving data for Customer Eligibility : ", e.getMessage());
		}


	}
	public int primary_cif_identify(Map<Integer, HashMap<String, String>> cusDetails )
	{
		int primary_cif = 0;
		try{
			Map<String, String> prim_entry = new HashMap<String, String>();
			Map<String, String> curr_entry = new HashMap<String, String>();


			Iterator<Map.Entry<Integer, HashMap<String, String>>> itr = cusDetails.entrySet().iterator();
			while(itr.hasNext()){
				Map.Entry<Integer, HashMap<String, String>> entry =  itr.next();

				curr_entry = entry.getValue();
				if(curr_entry.get("SearchType").equalsIgnoreCase("Internal"))
				{
					if(primary_cif==0 && curr_entry.containsKey("Products")){
						primary_cif = entry.getKey();
					}
					else if(curr_entry.containsKey("Products"))
					{
						prim_entry = cusDetails.get(primary_cif+"");
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
			SKLogger.writeLog("Exception occured while parsing Customer Eligibility : ", e.getMessage());
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
			SKLogger.writeLog("Exception occured while seting non primary CIF: ", e.getMessage());
		}

	}

	
	/*
	 * 	public void Customer_enable()
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
		formObject.setLocked("cmplx_Customer_SecNAtionApplicable",false);

		SKLogger.writeLog("RLOSCommon: Customer_enable()", "End Customer enable method");
	}
	public void SetDisableCustomer()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String fields="cmplx_Customer_Title,cmplx_Customer_ResidentNonResident,cmplx_Customer_gender,cmplx_Customer_age,cmplx_Customer_MotherName,cmplx_Customer_EmirateIDExpiry,cmplx_Customer_IdIssueDate,cmplx_Customer_VisaNo,cmplx_Customer_PassPortExpiry,cmplx_Customer_VIsaExpiry,cmplx_Customer_SecNationality,cmplx_Customer_MAritalStatus,cmplx_Customer_COuntryOFResidence,cmplx_Customer_EMirateOfVisa,cmplx_Customer_EmirateOfResidence,cmplx_Customer_yearsInUAE,cmplx_Customer_CustomerCategory,cmplx_Customer_GCCNational,cmplx_Customer_VIPFlag"; 
		String[] field_array=fields.split(",");
		for(int i=0;i<field_array.length;i++)
			formObject.setEnabled("DecisionHistory_Button5", true);
	}*/

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
	
	
	
	
	
	//document function
	//incoming doc function
	public void IncomingDoc(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		IRepeater repObj=null;
		repObj = formObject.getRepeaterControl("IncomingDoc_Frame2");
	
		String [] finalmisseddoc=new String[70];
		int rowRowcount=repObj.getRepeaterRowCount();
		SKLogger.writeLog("RLOS Initiation", "sQuery for document name is: rowRowcount" +  rowRowcount);
			if (repObj.getRepeaterRowCount() != 0) {
			
				for(int j = 0; j < rowRowcount; j++)
				{
					String DocName=repObj.getValue(j, "cmplx_DocName_DocName");
					SKLogger.writeLog("RLOS Initiation", "sQuery for document name is: DocName" +  DocName);
				
					String Mandatory=repObj.getValue(j, "cmplx_DocName_Mandatory");
					SKLogger.writeLog("RLOS Initiation", "sQuery for document name is: Mandatory" +  Mandatory);
						
					if("Y".equalsIgnoreCase(Mandatory))
						{
							 String DocIndex=repObj.getValue(j,"cmplx_DocName_DocIndex")==null?"":repObj.getValue(j,"cmplx_DocName_DocIndex");
							 SKLogger.writeLog("","DocIndex"+DocIndex);
							 String StatusValue=repObj.getValue(j,"cmplx_DocName_Status")==null?"":repObj.getValue(j,"cmplx_DocName_Status");
							 SKLogger.writeLog("","StatusValue"+StatusValue);
							 String Remarks=repObj.getValue(j, "cmplx_DocName_Remarks")==null?"":repObj.getValue(j, "cmplx_DocName_Remarks");
							 SKLogger.writeLog("","Remarks"+Remarks);
							
								 if(DocIndex==null||DocIndex.equalsIgnoreCase("")||DocIndex.equalsIgnoreCase("null")){
									 SKLogger.writeLog("","StatusValue inside DocIndex"+DocIndex);
									 if(StatusValue.equalsIgnoreCase("Received")){
										 SKLogger.writeLog("","StatusValue inside DocIndex recieved");
										 //below line commented as this mandatory document is already received. 
										// finalmisseddoc[j]=DocName;
									 }
								
					                  else if(StatusValue.equalsIgnoreCase("Deferred")){
					                      formObject.setNGValue("is_deferral_approval_require","Y");
					                      formObject.RaiseEvent("WFSave");
					                      SKLogger.writeLog("Deferred flag value inside no document",formObject.getNGValue("is_deferral_approval_require"));
										  if(Remarks.equalsIgnoreCase("")){
											  SKLogger.writeLog("It is Mandatory to fill Remarks","As you have not attached the Mandatory Document and the status is Deferred");
												throw new ValidatorException(new FacesMessage("As you have Deferred "+DocName+" Document,So kindly fill the Remarks"));
											}
											else if(!Remarks.equalsIgnoreCase("")||Remarks.equalsIgnoreCase(null)||Remarks.equalsIgnoreCase("null")){
												SKLogger.writeLog("You may proceed further","Proceed further");
											}
					                 }
					                  else if(StatusValue.equalsIgnoreCase("Waived")){
					                      formObject.setNGValue("is_waiver_approval_require","Y");
					                      formObject.RaiseEvent("WFSave");
					                      SKLogger.writeLog("waived flag value inside no document",formObject.getNGValue("is_waiver_approval_require"));
											if(Remarks.equalsIgnoreCase("")){
												SKLogger.writeLog("It is Mandatory to fill Remarks","As you have not attached the Mandatory Document and the status is Waived");
												throw new ValidatorException(new FacesMessage("As you have Waived "+DocName+" Document,So kindly fill the Remarks"));
											}
											else if(!Remarks.equalsIgnoreCase("")||Remarks.equalsIgnoreCase(null)||Remarks.equalsIgnoreCase("null")){
												SKLogger.writeLog("You may proceed further","Proceed further");
											}
									  }
					                  else if((StatusValue.equalsIgnoreCase("--Select--"))||(StatusValue.equalsIgnoreCase(""))){
					                	  SKLogger.writeLog("","StatusValue inside DocIndex is blank");
					                	  finalmisseddoc[j]=DocName;
					                  }
					                  else if((StatusValue.equalsIgnoreCase("Pending"))){
					                	  SKLogger.writeLog("","StatusValue of doc is Pending");
					                	  
					                  }
									}
									else{
										if(!(DocIndex.equalsIgnoreCase(""))){
											if(!StatusValue.equalsIgnoreCase("Received")){
												repObj.setValue(j,"cmplx_DocName_Status","Received");
												repObj.setEditable(j, "cmplx_DocName_Status", false);
												SKLogger.writeLog("","StatusValue::123final"+StatusValue);
											}
											else {
												
												SKLogger.writeLog("","StatusValue::123final status is already received");
											}
										}
									}
									
								}
							}
						}
						StringBuilder mandatoryDocName = new StringBuilder("");
						
						SKLogger.writeLog("","length of missed document"+finalmisseddoc.length);
						SKLogger.writeLog("","length of missed document mandatoryDocName.length"+mandatoryDocName.length());
					
						for(int k=0;k<finalmisseddoc.length;k++)
						{
							if(null != finalmisseddoc[k]) {
								mandatoryDocName.append(finalmisseddoc[k]).append(",");
							}
							SKLogger.writeLog("RLOS Initiation", "finalmisseddoc is:" +finalmisseddoc[k]);
							SKLogger.writeLog("","length of missed document mandatoryDocName.length1111"+mandatoryDocName.length());
						}
						mandatoryDocName.setLength(Math.max(mandatoryDocName.length()-1,0));
						SKLogger.writeLog("RLOS Initiation", "misseddoc is:" +mandatoryDocName.toString());
					
						if(mandatoryDocName.length()<=0){
							SKLogger.writeLog("RLOS Initiation", "misseddoc is: inside if condition");
						}
						else{
							SKLogger.writeLog("RLOS Initiation", "misseddoc is: inside if condition");
							SKLogger.writeLog("","length of missed document mandatoryDocName.length1111222"+mandatoryDocName.length());
							throw new ValidatorException(new FacesMessage("You have not attached Mandatory Documents: "+mandatoryDocName.toString()));
						}
					}
	//incomingdoc function
	
	/*
	public void IncomingDoc(int itemIndex)
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference(); 
		List<List<String>> outputindex = null;
		List<List<String>> secondquery=null;
		//String outputindex="";

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
		IRepeater repObj=null;
		repObj = formObject.getRepeaterControl("IncomingDoc_Frame");

		String sQuery="SELECT Name,DocumentIndex FROM PDBDocument with(nolock) WHERE DocumentIndex IN (SELECT DocumentIndex FROM PDBDocumentContent with(nolock) WHERE ParentFolderIndex= '"+itemIndex+"')";


		outputindex = null;
		SKLogger.writeLog("RLOS Initiation", "sQuery for document name is:" +  sQuery);
		outputindex = formObject.getDataFromDataSource(sQuery);



		SKLogger.writeLog("RLOS Initiation", "outputItemindex is:" +  outputindex);

		//IRepeater repObj=null;
		//repObj = formObject.getRepeaterControl("IncomingDoc_Frame");
		SKLogger.writeLog("RLOS Initiation","repObj::"+repObj+"row::+"+repObj.getRepeaterRowCount());

		if(outputindex==null || outputindex.size()==0) {
			SKLogger.writeLog("","output index is blank");
			String  query = "SELECT distinct DocName,Mandatory FROM ng_rlos_DocTable WHERE ProductName='"+requested_product+"' and SubProductName='"+requested_subproduct+"' and ProcessName='RLOS'";
			SKLogger.writeLog("RLOS Initiation", "sQuery for document name is:" +  query);
			secondquery = formObject.getNGDataFromDataCache(query);
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
		outputindex = formObject.getNGDataFromDataCache(query);
		SKLogger.writeLog("RLOS Initiation", "outputItemindex is:" +  outputindex+"::Size::"+outputindex.size());

		String [] misseddoc=new String[outputindex.size()];
		String [] finalmisseddoc=new String[70];
		int count=0;
		for(int j = 0; j < outputindex.size(); j++)

		{
			String DocName =outputindex.get(j).get(0);
			String Mandatory =outputindex.get(j).get(1);
			SKLogger.writeLog("RLOS Initiation", "Document_Index Document_Name is:" + DocName+","+Mandatory);

			if (repObj.getRepeaterRowCount() != 0) {
				//if(Mandatory.equalsIgnoreCase("Y"))
				//{

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
				if(Mandatory.equalsIgnoreCase("Y"))
				{
					if(count==0){
						SKLogger.writeLog("","Document is not present in the list");
						misseddoc[j]=DocName;
						//l++;
						SKLogger.writeLog("RLOS Initiation", " misseddoc is in j is:" +  misseddoc[j]);

						String StatusValue=repObj.getValue(j, "cmplx_DocName_Status");
						SKLogger.writeLog("","StatusValue::"+StatusValue);
						String Remarks=repObj.getValue(j, "cmplx_DocName_Remarks");
						SKLogger.writeLog("","Remarks::"+Remarks);
						if(!(StatusValue.equalsIgnoreCase("Deferred")||StatusValue.equalsIgnoreCase("Waived"))){
							finalmisseddoc[j]=DocName;
						}
						SKLogger.writeLog("RLOS Initiation", " finalmisseddoc is in j is:" +  finalmisseddoc[j]);
							if(!(StatusValue.equalsIgnoreCase("Recieved")||StatusValue.equalsIgnoreCase("Pending"))){
							if(Remarks.equalsIgnoreCase("")||Remarks.equalsIgnoreCase(null)||Remarks.equalsIgnoreCase("null")){
								SKLogger.writeLog("It is Mandatory to fill Remarks","As you have not attached the Mandatory Document fill the Remarks");
								throw new ValidatorException(new FacesMessage("As you have not attached the Mandatory Document fill the Remarks"));
							}
							else if(!Remarks.equalsIgnoreCase("")||Remarks.equalsIgnoreCase(null)||Remarks.equalsIgnoreCase("null")){
								SKLogger.writeLog("You may proceed further","Proceed further");
							}
						}

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
				//		}



			}
			count=0;
		}
		StringBuilder mandatoryDocName = new StringBuilder("");

		SKLogger.writeLog("","length of missed document"+finalmisseddoc.length);
		SKLogger.writeLog("","length of missed document mandatoryDocName.length"+mandatoryDocName.length());

		for(int k=0;k<finalmisseddoc.length;k++)
		{
			if(null != finalmisseddoc[k]) {
				mandatoryDocName.append(finalmisseddoc[k]).append(",");
			}
			SKLogger.writeLog("RLOS Initiation", "finalmisseddoc is:" +finalmisseddoc[k]);
			SKLogger.writeLog("","length of missed document mandatoryDocName.length1111"+mandatoryDocName.length());
		}
		mandatoryDocName.setLength(Math.max(mandatoryDocName.length()-1,0));
		SKLogger.writeLog("RLOS Initiation", "misseddoc is:" +mandatoryDocName.toString());

		if(mandatoryDocName.length()<=0){

			SKLogger.writeLog("RLOS Initiation", "misseddoc is: inside if condition");

		}
		else{
			SKLogger.writeLog("RLOS Initiation", "misseddoc is: inside if condition");
			SKLogger.writeLog("","length of missed document mandatoryDocName.length1111222"+mandatoryDocName.length());
			throw new ValidatorException(new FacesMessage("You have not attached Mandatory Documents: "+mandatoryDocName.toString()));

		}
		//code for IncomingDocument ended here    

	}
*/	//deepak code commented to add new document same as defined in PL Adn CC
	//document function 

	//commented from here
	/*  public void IncomingDoc(int itemIndex)
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
                    //     outputindex=outputindex.get(0).get(0);


                    SKLogger.writeLog("RLOS Initiation", "outputItemindex is:" +  outputindex);

                    IRepeater repObj=null;
                    repObj = formObject.getRepeaterControl("IncomingDoc_Frame");
                    SKLogger.writeLog("RLOS Initiation","repObj::"+repObj+"row::+"+repObj.getRepeaterRowCount());

                    if(outputindex==null || outputindex.size()==0) {
                                    SKLogger.writeLog("","output index is blank");
                                    String  query = "SELECT distinct DocName,Mandatory FROM ng_rlos_DocTable WHERE ProductName='"+requested_product+"' and SubProductName='"+requested_subproduct+"' and ProcessName='RLOS'";
                                    SKLogger.writeLog("RLOS Initiation", "sQuery for document name is:" +  query);
                                    secondquery = formObject.getNGDataFromDataCache(query);
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

                    if(outputindex != null && outputindex.size() != 0)
                    {
                                    System.out.println("Staff List "+outputindex);
                                    for(int i = 0; i < outputindex.size(); i++)
                                    {

                                                    //arrval[i].equalsIgnoreCase(repObj.getValue(i, "cmplx_DocName_DocName"));
                                                    arrval[i]=outputindex.get(i).get(0);
                                                    arrdocIndex[i]=outputindex.get(i).get(1);

                                                    SKLogger.writeLog("","docIndex array is :"+arrdocIndex[i]);


                                                    SKLogger.writeLog("RLOS Initiation", " doc index sMap is:" +  outputindex.get(i).get(1));
                                                    //String supdateQuery="UPDATE ng_rlos_incomingDoc SET DocInd='"+outputindex.get(i).get(1)+"' WHERE wi_name='"+formObject.getWFWorkitemName()+"' AND DocName='"+arrval[i]+"'";
                                                    ////outputupdateindex = null;
                                                    //SKLogger.writeLog("RLOS Initiation", "update sQuery for document name is:" +  supdateQuery);
                                                    //outputupdateindex = formObject.getDataFromDataSource(supdateQuery);
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
                    outputindex = formObject.getNGDataFromDataCache(query);
                    SKLogger.writeLog("RLOS Initiation", "outputItemindex is:" +  outputindex+"::Size::"+outputindex.size());
                    //IRepeater repObj=null;
                    //    repObj = formObject.getRepeaterControl("IncomingDoc_Frame");
                    //    SKLogger.writeLog("RLOS Initiation","repObj::"+repObj+"row::+"+repObj.getRepeaterRowCount());
                    String [] misseddoc=new String[outputindex.size()];
                    String [] finalmisseddoc=new String[70];
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
                                                                                    if(!(StatusValue.equalsIgnoreCase("Deferred")||StatusValue.equalsIgnoreCase("Waived"))){
                                                                                                    finalmisseddoc[j]=DocName;
                                                                                    }
                                                                                    SKLogger.writeLog("RLOS Initiation", " finalmisseddoc is in j is:" +  finalmisseddoc[j]);*/
	//commented from here
	/*                if(!(StatusValue.equalsIgnoreCase("Recieved")||StatusValue.equalsIgnoreCase("Pending"))){
                                                                                                    if(Remarks.equalsIgnoreCase("")||Remarks.equalsIgnoreCase(null)||Remarks.equalsIgnoreCase("null")){
                                                                                                                    SKLogger.writeLog("It is Mandatory to fill Remarks","As you have not attached the Mandatory Document fill the Remarks");
                                                                                                                    throw new ValidatorException(new FacesMessage("As you have not attached the Mandatory Document fill the Remarks"));
                                                                                                    }
                                                                                                    else if(!Remarks.equalsIgnoreCase("")||Remarks.equalsIgnoreCase(null)||Remarks.equalsIgnoreCase("null")){
                                                                                                                    SKLogger.writeLog("You may proceed further","Proceed further");
                                                                                                    }
                                                                                    }*/

	//added to update the flag for Deferred and Waived case
	/*if(StatusValue.equalsIgnoreCase("Deferred")){
               formObject.setNGValue("is_deferral_approval_require","Y");
              formObject.RaiseEvent("WFSave");
               SKLogger.writeLog("Deferred flag value inside some documents are present",formObject.getNGValue("is_deferral_approval_require"));

           }
            if(StatusValue.equalsIgnoreCase("Waived")){
               formObject.setNGValue("is_waiver_approval_require","Y");
              formObject.RaiseEvent("WFSave");
               SKLogger.writeLog("Waived flag value inside some documents are present",formObject.getNGValue("is_waiver_approval_require"));
           }*/
	//ended to update the flag for Deferred and Waived case


	/*     }
                                                    }

                                    }
                                    count=0;
                    }
                    StringBuilder mandatoryDocName = new StringBuilder("");
                    //SKLogger.writeLog("","length of missed document"+misseddoc.length);
                    SKLogger.writeLog("","length of missed document"+finalmisseddoc.length);
                    SKLogger.writeLog("","length of missed document mandatoryDocName.length"+mandatoryDocName.length());
    /*            for(int k=0;k<misseddoc.length;k++)
                    {
                                    if(null != misseddoc[k]) {
                                                    mandatoryDocName.append(misseddoc[k]).append(",");
                                    }
                                    SKLogger.writeLog("RLOS Initiation", "misseddoc is:" +misseddoc[k]);
                                    SKLogger.writeLog("","length of missed document mandatoryDocName.length1111"+mandatoryDocName.length());
                    }*/
	//commented from here
	/* for(int k=0;k<finalmisseddoc.length;k++)
                    {
                                    if(null != finalmisseddoc[k]) {
                                                    mandatoryDocName.append(finalmisseddoc[k]).append(",");
                                    }
                                    SKLogger.writeLog("RLOS Initiation", "finalmisseddoc is:" +finalmisseddoc[k]);
                                    SKLogger.writeLog("","length of missed document mandatoryDocName.length1111"+mandatoryDocName.length());
                    }
                    mandatoryDocName.setLength(Math.max(mandatoryDocName.length()-1,0));
                    SKLogger.writeLog("RLOS Initiation", "misseddoc is:" +mandatoryDocName.toString());
    //if(misseddoc.length>1){
                    if(mandatoryDocName.length()<=0){

                                    SKLogger.writeLog("RLOS Initiation", "misseddoc is: inside if condition");
                    //throw new ValidatorException(new FacesMessage("You have not attached Mandatory Documents: "+mandatoryDocName.toString()));
    }
                    else{
                                    SKLogger.writeLog("RLOS Initiation", "misseddoc is: inside if condition");
                                    SKLogger.writeLog("","length of missed document mandatoryDocName.length1111222"+mandatoryDocName.length());
                                    throw new ValidatorException(new FacesMessage("You have not attached Mandatory Documents: "+mandatoryDocName.toString()));

                    }
                    //code for IncomingDocument ended here    

    }*/
	//commented from here
	//need to add this function



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
		String CifId = formObject.getNGValue("cmplx_Customer_CIFNO");

		String NoOfContracts = "";
		String Total_Exposure = "";
		String WorstCurrentPaymentDelay = ""; 
		String Worst_PaymentDelay_Last24Months = "";
		String Nof_Records = "";

		String  add_xml_str ="";
		try{
			add_xml_str = add_xml_str + "<InternalBureau><applicant_id>"+CifId+"</applicant_id>";
			add_xml_str = add_xml_str + "<full_name>"+formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_MiddleName")+""+formObject.getNGValue("cmplx_Customer_LAstNAme")+"</full_name>";// fullname fieldname to be confirmed from onsite
			String sQuery = "SELECT OutstandingAmt,OverdueAmt,CreditLimit FROM ng_RLOS_CUSTEXPOSE_CardDetails WHERE Wi_Name = '"+formObject.getWFWorkitemName()+"'   union SELECT   TotalOutstandingAmt ,OverdueAmt,TotalLoanAmount FROM ng_RLOS_CUSTEXPOSE_LoanDetails   with (nolock) WHERE Wi_Name = '"+formObject.getWFWorkitemName()+"'";
			List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
			SKLogger.writeLog("InternalBureauData list size"+OutputXML.size(), "");
			//SKLogger.writeLog("InternalBureauData list "+OutputXML, "");
			SKLogger.writeLog("obefor list ", "values");
			float TotOutstandingAmt;
			float TotOverdueAmt;

			SKLogger.writeLog("outsidefor list ", "values");
			TotOutstandingAmt=0.0f;
			TotOverdueAmt=0.0f;

			SKLogger.writeLog("outsidefor2 list ", "values");
			for(int i = 0; i<OutputXML.size();i++){
				SKLogger.writeLog("insidefor list "+i, "values"+OutputXML.get(i).get(1));
				if(OutputXML.get(i).get(0)!=null && !OutputXML.get(i).get(1).isEmpty() &&  !OutputXML.get(i).get(1).equals("") && !OutputXML.get(i).get(1).equalsIgnoreCase("null") ){
					SKLogger.writeLog("Totaloutstanding"+i, "values."+TotOutstandingAmt+"..");
					TotOutstandingAmt = TotOutstandingAmt + Float.parseFloat(OutputXML.get(i).get(0));
				}
				if(OutputXML.get(i).get(1)!=null && !OutputXML.get(i).get(1).isEmpty() && !OutputXML.get(i).get(2).equals("") && !OutputXML.get(i).get(2).equalsIgnoreCase("null") ){
					SKLogger.writeLog("TotOverdueAmt"+i, "values."+TotOutstandingAmt+"..");
					TotOverdueAmt = TotOverdueAmt + Float.parseFloat(OutputXML.get(i).get(1));
				}

			}
			add_xml_str = add_xml_str + "<total_out_bal>"+TotOutstandingAmt+"</total_out_bal>";
			add_xml_str = add_xml_str + "<total_overdue>"+TotOverdueAmt+"</total_overdue>";
			String sQueryDerived = "select NoOfContracts,Total_Exposure,WorstCurrentPaymentDelay,Worst_PaymentDelay_Last24Months,Nof_Records from NG_RLOS_CUSTEXPOSE_Derived where Request_Type='CollectionsSummary' and Wi_Name='"+formObject.getWFWorkitemName()+"'" ;
			List<List<String>> OutputXMLDerived = formObject.getDataFromDataSource(sQueryDerived);
			SKLogger.writeLog("sQueryDerived sQueryDerived "+sQueryDerived, "sQueryDerived"+sQueryDerived);
			 if(OutputXMLDerived!=null && OutputXMLDerived.size()>0 && OutputXMLDerived.get(0)!=null){
			if(!(OutputXMLDerived.get(0).get(0)==null || OutputXMLDerived.get(0).get(0).equals("")) ){
				NoOfContracts= OutputXMLDerived.get(0).get(0).toString();
			}
			if(!(OutputXMLDerived.get(0).get(1)==null || OutputXMLDerived.get(0).get(1).equals("")) ){
				Total_Exposure= OutputXMLDerived.get(0).get(1).toString();
			}
			if(!(OutputXMLDerived.get(0).get(2)==null || OutputXMLDerived.get(0).get(2).equals("")) ){
				WorstCurrentPaymentDelay= OutputXMLDerived.get(0).get(2).toString();
			}
			if(!(OutputXMLDerived.get(0).get(3)==null || OutputXMLDerived.get(0).get(3).equals("")) ){
				Worst_PaymentDelay_Last24Months= OutputXMLDerived.get(0).get(3).toString();
			}
			if(!(OutputXMLDerived.get(0).get(4)==null || OutputXMLDerived.get(0).get(4).equals("")) ){
				Nof_Records= OutputXMLDerived.get(0).get(4).toString();
			}
			 }
			add_xml_str = add_xml_str + "<no_default_contract>"+NoOfContracts+"</no_default_contract>";

			add_xml_str = add_xml_str + "<total_exposure>"+Total_Exposure+"</total_exposure>";
			add_xml_str = add_xml_str + "<worst_curr_pay>"+WorstCurrentPaymentDelay+"</worst_curr_pay>"; // to be populated later
			add_xml_str = add_xml_str + "<worst_curr_pay_24>"+Worst_PaymentDelay_Last24Months+"</worst_curr_pay_24>"; // to be populated later
			add_xml_str = add_xml_str + "<no_of_rec>"+Nof_Records+"</no_of_rec>"; 
			String sQuerycheque = "SELECT 'DDS 3 months',count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-3,GETDATE()) and returntype='DDS' and Wi_Name='"+formObject.getWFWorkitemName()+"' union SELECT 'DDS 6 months',count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-6,GETDATE()) and returntype='DDS' and Wi_Name='"+formObject.getWFWorkitemName()+"' union SELECT 'ICCS 3 months',count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-3,GETDATE()) and returntype='ICCS' and Wi_Name='"+formObject.getWFWorkitemName()+"' union SELECT 'ICCS 6 months',count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-6,GETDATE()) and returntype='ICCS' and Wi_Name='"+formObject.getWFWorkitemName()+"'" ;
			List<List<String>> OutputXMLcheque = formObject.getDataFromDataSource(sQuerycheque);


			add_xml_str = add_xml_str + "<cheque_return_3mon>"+OutputXMLcheque.get(2).get(1)+"</cheque_return_3mon>";
			add_xml_str = add_xml_str + "<dds_return_3mon>"+OutputXMLcheque.get(0).get(1)+"</dds_return_3mon>";
			add_xml_str = add_xml_str + "<cheque_return_6mon>"+OutputXMLcheque.get(3).get(1)+"</cheque_return_6mon>";
			add_xml_str = add_xml_str + "<dds_return_6mon>"+OutputXMLcheque.get(1).get(1)+"</dds_return_6mon>";
			add_xml_str = add_xml_str + "<internal_charge_off>"+"N"+"</internal_charge_off><company_flag>N</company_flag></InternalBureau>";// to be populated later



			SKLogger.writeLog("RLOSCommon", "Internal liab tag Cration: "+ add_xml_str);
			return add_xml_str;
		}
		catch(Exception e)
		{
			SKLogger.writeLog("RLOSCommon", "Exception occurred in InternalBureauData()"+e.getMessage()+printException(e));
			return "";
		}

	}

	/*public String InternalBureauData(){
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
		String cifId = "";
		String chqRet3 = "";
		String ddsRet3 = "";
		String chqRet6 = ""; 
		String ddsRet6 = "";
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
			}
		}
			if(cifId!=null && !cifId.equalsIgnoreCase("") && !cifId.equalsIgnoreCase("null")){
				add_xml_str = add_xml_str + "<InternalBureau><applicant_id>"+cifId+"</applicant_id>";
				add_xml_str = add_xml_str + "<full_name>"+formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_MiddleName")+""+formObject.getNGValue("cmplx_Customer_LAstNAme")+"</full_name>";// fullname fieldname to be confirmed from onsite

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


		SKLogger.writeLog("RLOSCommon", "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
		}
		catch(Exception e)
		{
			SKLogger.writeLog("RLOSCommon", "Exception occurred in InternalBureauData()"+e.getMessage()+printException(e));
			return "";
		}
	}*/

	public String InternalBouncedCheques(){
		SKLogger.writeLog("RLOSCommon java file", "inside InternalBouncedCheques : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "select CIFID,AcctId,returntype,returnNumber,returnAmount,retReasonCode,returnDate from ng_rlos_FinancialSummary_ReturnsDtls with (nolock) where Wi_Name = '"+formObject.getWFWorkitemName()+"'";
		SKLogger.writeLog("InternalBouncedCheques sQuery"+sQuery, "");
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		SKLogger.writeLog("InternalBouncedCheques list size"+OutputXML.size(), "");

		for (int i = 0; i<OutputXML.size();i++){

			String applicantID = "";
			String chequeNo = "";
			String internal_bounced_cheques_id = "";
			String bouncedCheq="";
			String amount = "";
			String reason = ""; 
			String returnDate = "";


			if(!(OutputXML.get(i).get(0) == null || OutputXML.get(i).get(0).equals("")) ){
				applicantID = OutputXML.get(i).get(0).toString();
			}
			if(!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals("")) ){
				internal_bounced_cheques_id = OutputXML.get(i).get(1).toString();
			}
			if(!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2).equals("")) ){
				bouncedCheq = OutputXML.get(i).get(2).toString();
			}
			if(!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals("")) ){
				chequeNo = OutputXML.get(i).get(3).toString();
			}
			if(!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals("")) ){
				amount = OutputXML.get(i).get(4).toString();
			}
			if(!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5).equals("")) ){
				reason = OutputXML.get(i).get(5).toString();
			}
			if(!(OutputXML.get(i).get(6) == null || OutputXML.get(i).get(6).equals("")) ){
				returnDate = OutputXML.get(i).get(6).toString();
			}


			if(applicantID!=null && !applicantID.equalsIgnoreCase("") && !applicantID.equalsIgnoreCase("null")){
				add_xml_str = add_xml_str + "<InternalBouncedCheques><applicant_id>"+applicantID+"</applicant_id>";
				add_xml_str = add_xml_str + "<internal_bounced_cheques_id>"+internal_bounced_cheques_id+"</internal_bounced_cheques_id>";
				if (bouncedCheq.equalsIgnoreCase("ICCS")){
					add_xml_str = add_xml_str + "<bounced_cheque>"+"1"+"</bounced_cheque>";
				}
				add_xml_str = add_xml_str + "<cheque_no>"+chequeNo+"</cheque_no>";
				add_xml_str = add_xml_str + "<amount>"+amount+"</amount>";
				add_xml_str = add_xml_str + "<reason>"+reason+"</reason>";
				add_xml_str = add_xml_str + "<return_date>"+returnDate+"</return_date>"; 
				add_xml_str = add_xml_str + "<provider_no>"+"RAKBANK"+"</provider_no>";
				if (bouncedCheq.equalsIgnoreCase("DDS")){
					add_xml_str = add_xml_str + "<bounced_cheque_dds>"+"1"+"</bounced_cheque_dds>"; 
				}
				add_xml_str=  add_xml_str+"<company_flag>N</company_flag></InternalBouncedCheques>";
			}

		}
		SKLogger.writeLog("RLOSCommon", "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}

	public String InternalBureauIndividualProducts(){
		SKLogger.writeLog("RLOSCommon java file", "inside InternalBureauIndividualProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//Query Changed on 1st Nov
		String sQuery = "SELECT cifid,agreementid,loantype,loantype,custroletype,loan_start_date,loanmaturitydate,lastupdatedate ,totaloutstandingamt,totalloanamount,NextInstallmentAmt,paymentmode,totalnoofinstalments,remaininginstalments,totalloanamount,	overdueamt,nofdayspmtdelay,monthsonbook,currentlycurrentflg,currmaxutil,DPD_30_in_last_6_months,DPD_60_in_last_18_months,propertyvalue,loan_disbursal_date,marketingcode,DPD_30_in_last_3_months,DPD_30_in_last_6_months,DPD_30_in_last_9_months,DPD_30_in_last_12_months,DPD_30_in_last_18_months,DPD_30_in_last_24_months,DPD_60_in_last_3_months,DPD_60_in_last_6_months,DPD_60_in_last_9_months,DPD_60_in_last_12_months,DPD_60_in_last_18_months,DPD_60_in_last_24_months,DPD_90_in_last_3_months,DPD_90_in_last_6_months,DPD_90_in_last_9_months,DPD_90_in_last_12_months,DPD_90_in_last_18_months,DPD_90_in_last_24_months,DPD_120_in_last_3_months,DPD_120_in_last_6_months,DPD_120_in_last_9_months,DPD_120_in_last_12_months,DPD_120_in_last_18_months,DPD_120_in_last_24_months,DPD_150_in_last_3_months,DPD_150_in_last_6_months,DPD_150_in_last_9_months,DPD_150_in_last_12_months,DPD_150_in_last_18_months,DPD_150_in_last_24_months,DPD_180_in_last_3_months,DPD_180_in_last_6_months,DPD_180_in_last_9_months,DPD_180_in_last_12_months,DPD_180_in_last_24_months,'' as ExpiryDate,isnull(Consider_For_Obligations,'true'),LoanStat,'LOANS',writeoffStat,writeoffstatdt,lastrepmtdt,limit_increase,PartSettlementDetails,'' as SchemeCardProduct,General_Status,Internal_WriteOff_Check FROM ng_RLOS_CUSTEXPOSE_LoanDetails WHERE Wi_Name = '"+formObject.getWFWorkitemName()+"' and LoanStat !='Pipeline' union select CifId,CardEmbossNum,CardType,CardType,CustRoleType,'' as col6,'' as col7,'' as col8,OutstandingAmt,CreditLimit,PaymentsAmount,PaymentMode,'' as col13,'' as col14,CashLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,'' as col19,CurrMaxUtil,DPD_30_in_last_6_months,DPD_60_in_last_18_months,'' as col23,'' as col24,'' as col25,DPD_30_in_last_3_months,DPD_30_in_last_6_months,DPD_30_in_last_9_months,DPD_30_in_last_12_months,DPD_30_in_last_18_months,DPD_30_in_last_24_months,DPD_60_in_last_3_months,DPD_60_in_last_6_months,DPD_60_in_last_9_months,DPD_60_in_last_12_months,DPD_60_in_last_18_months,DPD_60_in_last_24_months,DPD_90_in_last_3_months,DPD_90_in_last_6_months,DPD_90_in_last_9_months,DPD_90_in_last_12_months,DPD_90_in_last_18_months,DPD_90_in_last_24_months,DPD_120_in_last_3_months,DPD_120_in_last_6_months,DPD_120_in_last_9_months,DPD_120_in_last_12_months,DPD_120_in_last_18_months,DPD_120_in_last_24_months,DPD_150_in_last_3_months,DPD_150_in_last_6_months,DPD_150_in_last_9_months,DPD_150_in_last_12_months,DPD_150_in_last_18_months,DPD_150_in_last_24_months,DPD_180_in_last_3_months,DPD_180_in_last_6_months,DPD_180_in_last_9_months,DPD_180_in_last_12_months,DPD_180_in_last_24_months,ExpiryDate,isnull(Consider_For_Obligations,'true'),CardStatus,'CARDS',writeoffStat,writeoffstatdt,'' as lastrepdate,limit_increase,'' as PartSettlementDetails,SchemeCardProd,General_Status,Internal_WriteOff_Check FROM ng_RLOS_CUSTEXPOSE_CardDetails where 	Wi_Name = '"+formObject.getWFWorkitemName()+"' and Request_Type In ('InternalExposure','CollectionsSummary')";
		//Query Changed on 1st Nov
		SKLogger.writeLog(" "+sQuery, "");
		String CountQuery ="select count(*) from ng_RLOS_CUSTEXPOSE_CardDetails where Wi_Name = '"+formObject.getWFWorkitemName()+"' and cardStatus='A'";
		List<List<String>> CountXML = formObject.getDataFromDataSource(CountQuery);
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		SKLogger.writeLog("InternalBureauIndividualProducts list size"+OutputXML.size(), "");
		SKLogger.writeLog("InternalBureauIndividualProducts list "+OutputXML, "");
		String ReqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
		String cardprod = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 5);


		try{
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
				String Emi = "";
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
				String Consider_For_Obligations = "";
				String phase = "";
				String writeoffStat = "";
				String writeoffstatdt = "";
				String lastrepmtdt = "";
				String Limit_increase = "";
				String part_settlement_date = "";
				String part_settlement_amount = "";
				String SchemeCardProduct = "";
				String General_Status = "";
				String Combined_Limit = "";
				String SecuredCard = "";
				String Internal_WriteOff_Check="";
				String EmployerType=formObject.getNGValue("EMploymentDetails_Combo5");
				String Kompass=formObject.getNGValue("cmplx_EmploymentDetails_Kompass");
				String paid_installment="";
				SKLogger.writeLog("Inside for", "asdasdasd");

				if(!(OutputXML.get(i).get(0) == null || OutputXML.get(i).get(0).equals("")) ){
					cifId = OutputXML.get(i).get(0).toString();
				}
				if(!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals("")) ){
					agreementId = OutputXML.get(i).get(1).toString();
				}				
				if(!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2).equals("")) ){
					product_type = OutputXML.get(i).get(2).toString();
					if (product_type.equalsIgnoreCase("Home In One")){
						product_type="HIO";
					}
					else{
						product_type = OutputXML.get(i).get(63).toString();
					}
				}
				if(!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals("")) ){
					contractType = OutputXML.get(i).get(2).toString();
				}
				if(!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals("")) ){
					custroletype = OutputXML.get(i).get(4).toString();
				}				
				if(!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5).equals("")) ){
					loan_start_date = OutputXML.get(i).get(5).toString();
				}
				if(OutputXML.get(i).get(6)!=null && !OutputXML.get(i).get(6).isEmpty() &&  !OutputXML.get(i).get(6).equals("") && !OutputXML.get(i).get(6).equalsIgnoreCase("null") ){
					loanmaturitydate = OutputXML.get(i).get(6).toString();
				}
				if(OutputXML.get(i).get(7)!=null && !OutputXML.get(i).get(7).isEmpty() &&  !OutputXML.get(i).get(7).equals("") && !OutputXML.get(i).get(7).equalsIgnoreCase("null") ){
					lastupdatedate = OutputXML.get(i).get(7).toString();
				}				
				if(OutputXML.get(i).get(8)!=null && !OutputXML.get(i).get(8).isEmpty() &&  !OutputXML.get(i).get(8).equals("") && !OutputXML.get(i).get(8).equalsIgnoreCase("null") ){
					outstandingamt = OutputXML.get(i).get(8).toString();
				}
				if(!(OutputXML.get(i).get(9) == null || OutputXML.get(i).get(9).equals("")) ){
					totalloanamount = OutputXML.get(i).get(9).toString();
				}
				if(!(OutputXML.get(i).get(10) == null || OutputXML.get(i).get(10).equals("")) ){
					Emi = OutputXML.get(i).get(10).toString();
				}				
				if(!(OutputXML.get(i).get(11) == null || OutputXML.get(i).get(11).equals("")) ){
					paymentmode = OutputXML.get(i).get(11).toString();
				}
				if(OutputXML.get(i).get(12)!=null && !OutputXML.get(i).get(12).isEmpty() &&  !OutputXML.get(i).get(12).equals("") && !OutputXML.get(i).get(12).equalsIgnoreCase("null") ){
					//SKLogger.writeLog("Inside for", "asdasdasd"+OutputXML.get(i).get(12));
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
				if(!(OutputXML.get(i).get(60) == null || OutputXML.get(i).get(60).equals("")) ){
					CardExpiryDate = OutputXML.get(i).get(60).toString();
				}

				if(!(OutputXML.get(i).get(61) == null || OutputXML.get(i).get(61).equals("")) ){
					Consider_For_Obligations = OutputXML.get(i).get(61).toString();
					if (Consider_For_Obligations.equalsIgnoreCase("false")){
						Consider_For_Obligations="Y";
					}
					else {
						Consider_For_Obligations="N";
					}
				}

				if(!(OutputXML.get(i).get(62) == null || OutputXML.get(i).get(62).equals("")) ){
					phase = OutputXML.get(i).get(62).toString();
					if (phase.startsWith("C")){
						phase="C";
					}
					else {
						phase="A";
					}
				}
				if(!(OutputXML.get(i).get(64) == null || OutputXML.get(i).get(64).equals("")) ){
					writeoffStat = OutputXML.get(i).get(64).toString();
				}
				if(!(OutputXML.get(i).get(65) == null || OutputXML.get(i).get(65).equals("")) ){
					writeoffstatdt = OutputXML.get(i).get(65).toString();
				}
				if(!(OutputXML.get(i).get(66) == null || OutputXML.get(i).get(66).equals("")) ){
					lastrepmtdt = OutputXML.get(i).get(66).toString();
				}
				if(!(OutputXML.get(i).get(67) == null || OutputXML.get(i).get(67).equals("")) ){
					Limit_increase = OutputXML.get(i).get(67).toString();
					if (Limit_increase.equalsIgnoreCase("false")){
						Limit_increase="N";
					}
					else{
						Limit_increase="Y";
					}
				}
				if(!(OutputXML.get(i).get(68) == null || OutputXML.get(i).get(68).equals("")) ){
					//part_settlement_date = OutputXML.get(i).get(68).toString();
					String abc=OutputXML.get(i).get(68).toString();
					abc=abc.substring(0, 10)+"split"+abc.substring(10,abc.length() );
					String abcsa[]=abc.split("split");
					part_settlement_date = abcsa[0];
					part_settlement_amount = abcsa[1];
				}
				if(!(OutputXML.get(i).get(69) == null || OutputXML.get(i).get(69).equals("")) ){
					SchemeCardProduct = OutputXML.get(i).get(69).toString();
				}
				if(!(OutputXML.get(i).get(70) == null || OutputXML.get(i).get(70).equals("")) ){
					General_Status = OutputXML.get(i).get(70).toString();
				}
				if(!(OutputXML.get(i).get(71) == null || OutputXML.get(i).get(71).equals("")) ){
					Internal_WriteOff_Check = OutputXML.get(i).get(71).toString();
				}
				String sQueryCombinedLimit = "select Distinct(COMBINEDLIMIT_ELIGIBILITY) from ng_master_cardProduct where code='"+SchemeCardProduct+"'";
				SKLogger.writeLog("sQueryCombinedLimit for", "sQueryCombinedLimit"+sQueryCombinedLimit);
				List<List<String>> sQueryCombinedLimitXML = formObject.getDataFromDataSource(sQueryCombinedLimit);
				try{
					if(sQueryCombinedLimitXML!=null && sQueryCombinedLimitXML.size()>0 && sQueryCombinedLimitXML.get(0)!=null){
						Combined_Limit=sQueryCombinedLimitXML.get(0).get(0).equalsIgnoreCase("1")?"Y":"N";
					}
				}
				catch(Exception e){
					SKLogger.writeLog("Exception occured at sQueryCombinedLimit for", "sQueryCombinedLimit"+sQueryCombinedLimit);
					
				}
				String sQuerySecuredCard = "select count(*) from ng_master_cardProduct where code='"+SchemeCardProduct+"'  and subproduct='SEC'";
				SKLogger.writeLog("sQueryCombinedLimit for", "sQueryCombinedLimit"+sQueryCombinedLimit);
				List<List<String>> sQuerySecuredCardXML = formObject.getDataFromDataSource(sQuerySecuredCard);
				try{
					if(sQuerySecuredCardXML!=null && sQuerySecuredCardXML.size()>0 && sQuerySecuredCardXML.get(0)!=null){
							SecuredCard=sQuerySecuredCardXML.get(0).get(0).equalsIgnoreCase("0")?"N":"Y";
				}	
				}
				catch(Exception e){
					SKLogger.writeLog("Exception occured at SecuredCard for", "SecuredCard"+SecuredCard);
					
				}
				if(cifId!=null && !cifId.equalsIgnoreCase("") && !cifId.equalsIgnoreCase("null")){
					SKLogger.writeLog("Inside if", "asdasdasd");
					add_xml_str = add_xml_str + "<InternalBureauIndividualProducts><applicant_id>"+cifId+"</applicant_id>";
					add_xml_str = add_xml_str + "<internal_bureau_individual_products_id>"+agreementId+"</internal_bureau_individual_products_id>";
					add_xml_str = add_xml_str + "<type_product>"+product_type+"</type_product>";
					if (OutputXML.get(i).get(63).equalsIgnoreCase("cards")){					
						add_xml_str = add_xml_str + "<contract_type>CC</contract_type>";
					}
					if (OutputXML.get(i).get(63).equalsIgnoreCase("Loans")){
						add_xml_str = add_xml_str + "<contract_type>PL</contract_type>";
					}	
					add_xml_str = add_xml_str + "<provider_no>"+"RAKBANK"+"</provider_no>";
					add_xml_str = add_xml_str + "<phase>"+phase+"</phase>";
					add_xml_str = add_xml_str + "<role_of_customer>Primary</role_of_customer>"; 

					add_xml_str = add_xml_str + "<start_date>"+loan_start_date+"</start_date>"; 
					add_xml_str = add_xml_str + "<close_date>"+loanmaturitydate+"</close_date>"; 
					add_xml_str = add_xml_str + "<date_last_updated>"+lastupdatedate+"</date_last_updated>"; 
					add_xml_str = add_xml_str + "<outstanding_balance>"+outstandingamt+"</outstanding_balance>"; 
					if (OutputXML.get(i).get(63).equalsIgnoreCase("Loans")){
						add_xml_str = add_xml_str + "<total_amount>"+totalloanamount+"</total_amount>";
					}
					add_xml_str = add_xml_str + "<payments_amount>"+Emi+"</payments_amount>"; 
					add_xml_str = add_xml_str + "<method_of_payment>"+paymentmode+"</method_of_payment>"; 
					add_xml_str = add_xml_str + "<total_no_of_instalments>"+totalnoofinstalments+"</total_no_of_instalments>"; 
					add_xml_str = add_xml_str + "<no_of_remaining_instalments>"+remaininginstalments+"</no_of_remaining_instalments>"; 
					add_xml_str = add_xml_str + "<worst_status>"+writeoffStat+"</worst_status>"; 
					add_xml_str = add_xml_str + "<worst_status_date>"+writeoffstatdt+"</worst_status_date>"; 
					if (OutputXML.get(i).get(63).equalsIgnoreCase("cards")){

						add_xml_str = add_xml_str + "<credit_limit>"+totalloanamount+"</credit_limit>"; 
					}
					add_xml_str = add_xml_str + "<overdue_amount>"+overdueamt+"</overdue_amount>"; 
					add_xml_str = add_xml_str + "<no_of_days_payment_delay>"+nofdayspmtdelay+"</no_of_days_payment_delay>"; 
					add_xml_str = add_xml_str + "<mob>"+monthsonbook+"</mob>"; 
					add_xml_str = add_xml_str + "<last_repayment_date>"+lastrepmtdt+"</last_repayment_date>"; 
					add_xml_str = add_xml_str + "<currently_current>"+currentlycurrent+"</currently_current>"; 
					add_xml_str = add_xml_str + "<current_utilization>"+currmaxutil+"</current_utilization>"; 
					add_xml_str = add_xml_str + "<dpd_30_last_6_mon>"+DPD_30_in_last_6_months+"</dpd_30_last_6_mon>"; 
					add_xml_str = add_xml_str + "<dpd_60p_in_last_18_mon>"+DPD_60_in_last_18_months+"</dpd_60p_in_last_18_mon>"; 


					add_xml_str = add_xml_str + "<card_product>"+SchemeCardProduct+"</card_product>"; 
					add_xml_str = add_xml_str + "<property_value>"+propertyvalue+"</property_value>"; 
					add_xml_str = add_xml_str + "<disbursal_date>"+loan_disbursal_date+"</disbursal_date>"; 
					add_xml_str = add_xml_str + "<marketing_code>"+marketingcode+"</marketing_code>"; 
					add_xml_str = add_xml_str + "<card_expiry_date>"+CardExpiryDate+"</card_expiry_date>"; 
					add_xml_str = add_xml_str + "<card_upgrade_indicator>"+Limit_increase+"</card_upgrade_indicator>"; 
					add_xml_str = add_xml_str + "<part_settlement_date>"+part_settlement_date+"</part_settlement_date>"; 
					add_xml_str = add_xml_str + "<part_settlement_amount>"+part_settlement_amount+"</part_settlement_amount>"; 
					add_xml_str = add_xml_str + "<part_settlement_reason>"+""+"</part_settlement_reason>"; 
					add_xml_str = add_xml_str + "<limit_expiry_date>"+""+"</limit_expiry_date>"; 
					add_xml_str = add_xml_str + "<no_of_primary_cards>"+CountXML.get(0).get(0)+"</no_of_primary_cards>"; 
					add_xml_str = add_xml_str + "<no_of_repayments_done>"+remaininginstalments+"</no_of_repayments_done>"; 
					add_xml_str = add_xml_str + "<card_segment>"+SchemeCardProduct+"</card_segment>"; 
					add_xml_str = add_xml_str + "<product_type>"+OutputXML.get(i).get(63)+"</product_type>"; 
					add_xml_str = add_xml_str + "<product_category>"+SchemeCardProduct+"</product_category>"; 
					add_xml_str = add_xml_str + "<combined_limit_flag>"+Combined_Limit+"</combined_limit_flag>"; 
					add_xml_str = add_xml_str + "<secured_card_flag>"+SecuredCard+"</secured_card_flag>"; 
					add_xml_str = add_xml_str + "<resch_tko_flag>"+Limit_increase+"</resch_tko_flag>"; 

					add_xml_str = add_xml_str + "<general_status>"+General_Status+"</general_status>"; 
					add_xml_str = add_xml_str + "<consider_for_obligation>"+Consider_For_Obligations+"</consider_for_obligation>"; 
					add_xml_str = add_xml_str + "<limit_increase>"+Limit_increase+"</limit_increase>"; 

					add_xml_str = add_xml_str + "<role>Primary</role>"; 
					add_xml_str = add_xml_str + "<limit>"+""+"</limit>"; 
					add_xml_str = add_xml_str + "<status>"+phase+"</status>";
					add_xml_str = add_xml_str + "<emi>"+Emi+"</emi>"; 
					add_xml_str = add_xml_str + "<os_amt>"+outstandingamt+"</os_amt>"; 

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
					if(Kompass!=null){
						if(Kompass.equalsIgnoreCase("true")){
							add_xml_str = add_xml_str + "<kompass>"+"Y"+"</kompass>";
						}
						else{
							add_xml_str = add_xml_str + "<kompass>"+"N"+"</kompass>";
						}
					}
					add_xml_str = add_xml_str + "<employer_type>"+EmployerType+"</employer_type>"; 

					if (totalnoofinstalments!=null && remaininginstalments!=null && !totalnoofinstalments.equals("") &&  !remaininginstalments.equals("")) {
						paid_installment= Integer.toString(Integer.parseInt(totalnoofinstalments) -Integer.parseInt(remaininginstalments));
						SKLogger.writeLog("Inside paid_installment", "paid_installment"+paid_installment);

					}
					if (ReqProd.equalsIgnoreCase("Credit Card")){

						add_xml_str = add_xml_str + "<no_of_paid_installment>"+paid_installment+"</no_of_paid_installment><write_off_amount>"+Internal_WriteOff_Check+"</write_off_amount><company_flag>N</company_flag></InternalBureauIndividualProducts>";
					}
					else{
						add_xml_str = add_xml_str + "<no_of_paid_installment>"+paid_installment+"</no_of_paid_installment><write_off_amount>"+Internal_WriteOff_Check+"</write_off_amount><company_flag>N</company_flag><type_of_od>"+""+"</type_of_od><amt_paid_last6mnths>"+""+"</amt_paid_last6mnths></InternalBureauIndividualProducts>";

					}
				}	

			}
		}
		catch(Exception e){
			SKLogger.writeLog("RLOSCommon", "Internal liab tag Cration: "+ printException(e));
		}
		SKLogger.writeLog("RLOSCommon", "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}

	public String InternalBureauPipelineProducts(){
		SKLogger.writeLog("RLOSCommon java file", "inside InternalBureauPipelineProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "SELECT cifid,product_type,custroletype,lastupdatedate,totalamount,totalnoofinstalments,totalloanamount,agreementId FROM ng_RLOS_CUSTEXPOSE_LoanDetails  with (nolock) where Wi_Name = '"+formObject.getWFWorkitemName()+"' and  LoanStat = 'Pipeline'";
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
			String agreementId = "";

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
			if(!(OutputXML.get(i).get(7) == null || OutputXML.get(i).get(7).equals("")) ){
				agreementId = OutputXML.get(i).get(7).toString();
			}

			if(cifId!=null && !cifId.equalsIgnoreCase("") && !cifId.equalsIgnoreCase("null")){
				add_xml_str = add_xml_str + "<InternalBureauPipelineProducts>";
				add_xml_str = add_xml_str + "<applicant_id>"+cifId+"</applicant_id>";
				add_xml_str = add_xml_str + "<internal_bureau_pipeline_products_id>"+agreementId+"</internal_bureau_pipeline_products_id>";// to be populated later
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
		String sQuery = "select  CifId,fullnm,TotalOutstanding,TotalOverdue,NoOfContracts,Total_Exposure,WorstCurrentPaymentDelay,Worst_PaymentDelay_Last24Months,Worst_Status_Last24Months,Nof_Records,NoOf_Cheque_Return_Last3,Nof_DDES_Return_Last3Months,Nof_Cheque_Return_Last6,DPD30_Last6Months,Internal_WriteOff_Check from NG_rlos_custexpose_Derived where wi_name  = '"+formObject.getWFWorkitemName()+"' and Request_type= 'ExternalExposure'";
		SKLogger.writeLog("ExternalBureauData sQuery"+sQuery, "");
		String AecbHistQuery = "select isnull(max(AECBHistMonthCnt),0) as AECBHistMonthCnt from ( select MAX(AECBHistMonthCnt) as AECBHistMonthCnt  from ng_rlos_cust_extexpo_CardDetails where  wi_name  = '"+formObject.getWFWorkitemName()+"' union select Max(AECBHistMonthCnt) from ng_rlos_cust_extexpo_LoanDetails where wi_name  = '"+formObject.getWFWorkitemName()+"') as ext_expo";
		String  add_xml_str ="";
		try{
			List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
			SKLogger.writeLog("ExternalBureauData list size"+OutputXML.size(), "");
			List<List<String>> AecbHistQueryData = formObject.getDataFromDataSource(AecbHistQuery);
			/*
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
		}*/
			if (AecbHistQueryData.get(0).get(0).equalsIgnoreCase("0")){


				String CifId = formObject.getNGValue("cmplx_Customer_CIFNO");
				String fullnm=formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme");
				String TotalOutstanding="";
				String TotalOverdue="";
				String NoOfContracts="";
				String Total_Exposure="";
				String WorstCurrentPaymentDelay="";
				String Worst_PaymentDelay_Last24Months="";
				String Worst_Status_Last24Months="";
				String Nof_Records="";
				String NoOf_Cheque_Return_Last3="";
				String Nof_DDES_Return_Last3Months="";
				String Nof_Cheque_Return_Last6="";
				String DPD30_Last6Months="";
				String Internal_WriteOff_Check="";



				add_xml_str = add_xml_str + "<ExternalBureau>"; 
				add_xml_str = add_xml_str + "<applicant_id>"+CifId+"</applicant_id>";

				add_xml_str = add_xml_str + "<full_name>"+fullnm+"</full_name>"; 
				add_xml_str = add_xml_str + "<total_out_bal>"+TotalOutstanding+"</total_out_bal>";

				add_xml_str = add_xml_str + "<total_overdue>"+TotalOverdue+"</total_overdue>";
				add_xml_str = add_xml_str + "<no_default_contract>"+NoOfContracts+"</no_default_contract>";
				add_xml_str = add_xml_str + "<total_exposure>"+Total_Exposure+"</total_exposure>";
				add_xml_str = add_xml_str + "<worst_curr_pay>"+WorstCurrentPaymentDelay+"</worst_curr_pay>";
				add_xml_str = add_xml_str + "<worst_curr_pay_24>"+Worst_PaymentDelay_Last24Months+"</worst_curr_pay_24>"; 
				add_xml_str = add_xml_str + "<worst_status_24>"+Worst_Status_Last24Months+"</worst_status_24>"; 


				add_xml_str = add_xml_str + "<no_of_rec>"+Nof_Records+"</no_of_rec>"; 
				add_xml_str = add_xml_str + "<cheque_return_3mon>"+NoOf_Cheque_Return_Last3+"</cheque_return_3mon>";
				add_xml_str = add_xml_str + "<dds_return_3mon>"+Nof_DDES_Return_Last3Months+"</dds_return_3mon>";
				add_xml_str = add_xml_str + "<cheque_return_6mon>"+Nof_Cheque_Return_Last6+"</cheque_return_6mon>";
				add_xml_str = add_xml_str + "<dds_return_6mon>"+DPD30_Last6Months+"</dds_return_6mon>";
				add_xml_str = add_xml_str + "<prod_external_writeoff_amount>"+""+"</prod_external_writeoff_amount>";

				add_xml_str = add_xml_str + "<no_months_aecb_history >"+AecbHistQueryData.get(0).get(0)+"</no_months_aecb_history >";

				add_xml_str = add_xml_str + "<company_flag>N</company_flag></ExternalBureau>";



				SKLogger.writeLog("RLOSCommon", "Internal liab tag Cration: "+ add_xml_str);
				return add_xml_str;
			}
			else{
				for (int i = 0; i<OutputXML.size();i++){

					String CifId = formObject.getNGValue("cmplx_Customer_CIFNO");
					String fullnm="";
					String TotalOutstanding="";
					String TotalOverdue="";
					String NoOfContracts="";
					String Total_Exposure="";
					String WorstCurrentPaymentDelay="";
					String Worst_PaymentDelay_Last24Months="";
					String Worst_Status_Last24Months="";
					String Nof_Records="";
					String NoOf_Cheque_Return_Last3="";
					String Nof_DDES_Return_Last3Months="";
					String Nof_Cheque_Return_Last6="";
					String DPD30_Last6Months="";
					String Internal_WriteOff_Check="";

					if(!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals("")) ){
						fullnm = OutputXML.get(i).get(1).toString();
					}				
					if(!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2).equals("")) ){
						TotalOutstanding = OutputXML.get(i).get(2).toString();

					}
					if(!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals("")) ){
						TotalOverdue = OutputXML.get(i).get(3).toString();
					}
					if(!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals("")) ){
						NoOfContracts = OutputXML.get(i).get(4).toString();
					}				
					if(!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5).equals("")) ){
						Total_Exposure = OutputXML.get(i).get(5).toString();
					}
					if(OutputXML.get(i).get(6)!=null && !OutputXML.get(i).get(6).isEmpty() &&  !OutputXML.get(i).get(6).equals("") && !OutputXML.get(i).get(6).equalsIgnoreCase("null") ){
						WorstCurrentPaymentDelay = OutputXML.get(i).get(6).toString();
					}
					if(OutputXML.get(i).get(7)!=null && !OutputXML.get(i).get(7).isEmpty() &&  !OutputXML.get(i).get(7).equals("") && !OutputXML.get(i).get(7).equalsIgnoreCase("null") ){
						Worst_PaymentDelay_Last24Months = OutputXML.get(i).get(7).toString();
					}				
					if(OutputXML.get(i).get(8)!=null && !OutputXML.get(i).get(8).isEmpty() &&  !OutputXML.get(i).get(8).equals("") && !OutputXML.get(i).get(8).equalsIgnoreCase("null") ){
						Worst_Status_Last24Months = OutputXML.get(i).get(8).toString();
					}
					if(!(OutputXML.get(i).get(9) == null || OutputXML.get(i).get(9).equals("")) ){
						Nof_Records = OutputXML.get(i).get(9).toString();
					}
					if(!(OutputXML.get(i).get(10) == null || OutputXML.get(i).get(10).equals("")) ){
						NoOf_Cheque_Return_Last3 = OutputXML.get(i).get(10).toString();
					}				
					if(!(OutputXML.get(i).get(11) == null || OutputXML.get(i).get(11).equals("")) ){
						Nof_DDES_Return_Last3Months = OutputXML.get(i).get(11).toString();
					}
					if(OutputXML.get(i).get(12)!=null && !OutputXML.get(i).get(12).isEmpty() &&  !OutputXML.get(i).get(12).equals("") && !OutputXML.get(i).get(12).equalsIgnoreCase("null") ){
						//SKLogger.writeLog("Inside for", "asdasdasd"+OutputXML.get(i).get(12));
						Nof_Cheque_Return_Last6 = OutputXML.get(i).get(12).toString();
					}
					if(!(OutputXML.get(i).get(13) == null || OutputXML.get(i).get(13).equals("")) ){
						DPD30_Last6Months = OutputXML.get(i).get(13).toString();
					}

					add_xml_str = add_xml_str + "<ExternalBureau>"; 
					add_xml_str = add_xml_str + "<applicant_id>"+CifId+"</applicant_id>";

					add_xml_str = add_xml_str + "<full_name>"+fullnm+"</full_name>"; 
					add_xml_str = add_xml_str + "<total_out_bal>"+TotalOutstanding+"</total_out_bal>";

					add_xml_str = add_xml_str + "<total_overdue>"+TotalOverdue+"</total_overdue>";
					add_xml_str = add_xml_str + "<no_default_contract>"+NoOfContracts+"</no_default_contract>";
					add_xml_str = add_xml_str + "<total_exposure>"+Total_Exposure+"</total_exposure>";
					add_xml_str = add_xml_str + "<worst_curr_pay>"+WorstCurrentPaymentDelay+"</worst_curr_pay>";
					add_xml_str = add_xml_str + "<worst_curr_pay_24>"+Worst_PaymentDelay_Last24Months+"</worst_curr_pay_24>"; 
					add_xml_str = add_xml_str + "<worst_status_24>"+Worst_Status_Last24Months+"</worst_status_24>"; 


					add_xml_str = add_xml_str + "<no_of_rec>"+Nof_Records+"</no_of_rec>"; 
					add_xml_str = add_xml_str + "<cheque_return_3mon>"+NoOf_Cheque_Return_Last3+"</cheque_return_3mon>";
					add_xml_str = add_xml_str + "<dds_return_3mon>"+Nof_DDES_Return_Last3Months+"</dds_return_3mon>";
					add_xml_str = add_xml_str + "<cheque_return_6mon>"+Nof_Cheque_Return_Last6+"</cheque_return_6mon>";
					add_xml_str = add_xml_str + "<dds_return_6mon>"+DPD30_Last6Months+"</dds_return_6mon>";
					add_xml_str = add_xml_str + "<prod_external_writeoff_amount>"+""+"</prod_external_writeoff_amount>";

					add_xml_str = add_xml_str + "<no_months_aecb_history >"+AecbHistQueryData.get(0).get(0)+"</no_months_aecb_history >";

					add_xml_str = add_xml_str + "<company_flag>N</company_flag></ExternalBureau>";


				}
				SKLogger.writeLog("RLOSCommon", "Internal liab tag Cration: "+ add_xml_str);
				return add_xml_str;
			}

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
		String sQuery = "SELECT cifid,number,amount,reasoncode,returndate,providerno FROM ng_rlos_cust_extexpo_ChequeDetails  with (nolock) where Wi_Name = '"+formObject.getWFWorkitemName()+"' and Request_Type = 'ExternalExposure'";
		SKLogger.writeLog("ExternalBouncedCheques sQuery"+sQuery, "");
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		SKLogger.writeLog("ExternalBouncedCheques list size"+OutputXML.size(), "");

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
		SKLogger.writeLog("RLOSCommon", "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}

	public String ExternalBureauIndividualProducts(){
		SKLogger.writeLog("RLOSCommon java file", "inside ExternalBureauIndividualProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//Query Changed on 1st Nov
		String sQuery = "select CifId,AgreementId,LoanType,ProviderNo,LoanStat,CustRoleType,LoanApprovedDate,LoanMaturityDate,OutstandingAmt,TotalAmt,PaymentsAmt,TotalNoOfInstalments,RemainingInstalments,WriteoffStat,WriteoffStatDt,CreditLimit,OverdueAmt,NofDaysPmtDelay,MonthsOnBook,lastrepmtdt,IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last18Months,AECBHistMonthCnt,DPD5_Last3Months,'' as qc_Amnt,'' as Qc_emi,'' as Cac_indicator,Take_Over_Indicator,isnull(Consider_For_Obligations,'true') from ng_rlos_cust_extexpo_LoanDetails where wi_name= '"+formObject.getWFWorkitemName()+"'  and LoanStat != 'Pipeline' union select CifId,CardEmbossNum,CardType,ProviderNo,CardStatus,CustRoleType,StartDate,ClosedDate,CurrentBalance,'' as col6,PaymentsAmount,NoOfInstallments,'' as col5,WriteoffStat,WriteoffStatDt,CashLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,lastrepmtdt,IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last18Months,AECBHistMonthCnt,DPD5_Last3Months,qc_amt,qc_emi,CAC_Indicator,Take_Over_Indicator,isnull(Consider_For_Obligations,'true') from ng_rlos_cust_extexpo_CardDetails where wi_name  =  '"+formObject.getWFWorkitemName()+"' and cardstatus != 'Pipeline' ";
		//Query Changed on 1st Nov
		
		SKLogger.writeLog("ExternalBureauIndividualProducts sQuery"+sQuery, "");
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		SKLogger.writeLog("ExternalBureauIndividualProducts list size"+OutputXML.size(), "");
		String ReqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
		for (int i = 0; i<OutputXML.size();i++){

			String CifId = formObject.getNGValue("cmplx_Customer_CIFNO");
			String AgreementId = "";
			String ContractType = "";
			String provider_no = ""; 
			String phase = "";
			String CustRoleType = ""; 
			String start_date = "";	
			String close_date = "";
			String OutStanding_Balance = "";
			String TotalAmt = "";
			String PaymentsAmt = "";
			String TotalNoOfInstalments = "";
			String RemainingInstalments = "";
			String WorstStatus = ""; 
			String WorstStatusDate = "";
			String CreditLimit = ""; 
			String OverdueAmt = "";
			String NofDaysPmtDelay = "";
			String MonthsOnBook = "";
			String last_repayment_date = "";
			String DPD60Last18Months = "";
			String AECBHistMonthCnt = "";
			String DPD30Last6Months = "";
			String currently_current = "";
			String current_utilization = "";
			String delinquent_in_last_3months = "";
			String QC_Amt = "";
			String QC_emi = "";
			String CAC_Indicator = "";
			String consider_for_obligation="";

			if(!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals("")) ){
				AgreementId = OutputXML.get(i).get(1).toString();
			}				
			if(!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2).equals("")) ){
				ContractType = OutputXML.get(i).get(2).toString();
				try{
				String cardquery = "select code from ng_master_contract_type where description='"+ContractType+"'";
				SKLogger.writeLog("ExternalBureauIndividualProducts sQuery"+sQuery, "");
				List<List<String>> cardqueryXML = formObject.getDataFromDataSource(cardquery);
				ContractType=cardqueryXML.get(0).get(0);
				SKLogger.writeLog("ExternalBureauIndividualProducts ContractType"+ContractType, "ContractType");
				}
				catch(Exception e){
					SKLogger.writeLog("ExternalBureauIndividualProducts ContractType Exception"+e, "Exception");
					
					ContractType= OutputXML.get(i).get(2).toString();
				}
			}
			if(!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals("")) ){
				provider_no = OutputXML.get(i).get(3).toString();
			}
			if(!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals("")) ){
				phase = OutputXML.get(i).get(4).toString();
				if (phase.startsWith("A")){
					phase="A";
				}
				else {
					phase="C";
				}
			}				
			if(!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5).equals("")) ){
				CustRoleType = OutputXML.get(i).get(5).toString();
			}
			if(!(OutputXML.get(i).get(6) == null || OutputXML.get(i).get(6).equals("")) ){
				start_date = OutputXML.get(i).get(6).toString();
			}
			if(!(OutputXML.get(i).get(7) == null || OutputXML.get(i).get(7).equals("")) ){
				close_date = OutputXML.get(i).get(7).toString();
			}				
			if(!(OutputXML.get(i).get(8) == null || OutputXML.get(i).get(8).equals("")) ){
				OutStanding_Balance = OutputXML.get(i).get(8).toString();
			}
			if(!(OutputXML.get(i).get(9) == null || OutputXML.get(i).get(9).equals("")) ){
				TotalAmt = OutputXML.get(i).get(9).toString();
			}
			if(!(OutputXML.get(i).get(10) == null || OutputXML.get(i).get(10).equals("")) ){
				PaymentsAmt = OutputXML.get(i).get(10).toString();
			}
			if(!(OutputXML.get(i).get(11) == null || OutputXML.get(i).get(11).equals("")) ){
				TotalNoOfInstalments = OutputXML.get(i).get(11).toString();
			}
			if(!(OutputXML.get(i).get(12) == null || OutputXML.get(i).get(12).equals("")) ){
				RemainingInstalments = OutputXML.get(i).get(12).toString();
			}
			if(!(OutputXML.get(i).get(13) == null || OutputXML.get(i).get(13).equals("")) ){
				WorstStatus = OutputXML.get(i).get(13).toString();
			}
			if(!(OutputXML.get(i).get(14) == null || OutputXML.get(i).get(14).equals("")) ){
				WorstStatusDate = OutputXML.get(i).get(14).toString();
			}				
			if(!(OutputXML.get(i).get(15) == null || OutputXML.get(i).get(15).equals("")) ){
				CreditLimit = OutputXML.get(i).get(15).toString();
			}
			if(!(OutputXML.get(i).get(16) == null || OutputXML.get(i).get(16).equals("")) ){
				OverdueAmt = OutputXML.get(i).get(16).toString();
			}
			if(!(OutputXML.get(i).get(17) == null || OutputXML.get(i).get(17).equals("")) ){
				NofDaysPmtDelay = OutputXML.get(i).get(17).toString();
			}				
			if(!(OutputXML.get(i).get(18) == null || OutputXML.get(i).get(18).equals("")) ){
				MonthsOnBook = OutputXML.get(i).get(18).toString();
			}
			if(!(OutputXML.get(i).get(19) == null || OutputXML.get(i).get(19).equals("")) ){
				last_repayment_date = OutputXML.get(i).get(19).toString();
			}
			if(!(OutputXML.get(i).get(20) == null || OutputXML.get(i).get(20).equals("")) ){
				currently_current = OutputXML.get(i).get(20).toString();
			}
			if(!(OutputXML.get(i).get(21) == null || OutputXML.get(i).get(21).equals("")) ){
				current_utilization = OutputXML.get(i).get(21).toString();
			}
			if(!(OutputXML.get(i).get(22) == null || OutputXML.get(i).get(22).equals("")) ){
				DPD30Last6Months = OutputXML.get(i).get(22).toString();
			}
			if(!(OutputXML.get(i).get(23) == null || OutputXML.get(i).get(23).equals("")) ){
				DPD60Last18Months = OutputXML.get(i).get(23).toString();
			}
			if(!(OutputXML.get(i).get(24) == null || OutputXML.get(i).get(24).equals("")) ){
				AECBHistMonthCnt = OutputXML.get(i).get(24).toString();
			}				


			if(!(OutputXML.get(i).get(25) == null || OutputXML.get(i).get(25).equals("")) ){
				delinquent_in_last_3months = OutputXML.get(i).get(25).toString();
			}
			if(!(OutputXML.get(i).get(26) == null || OutputXML.get(i).get(26).equals("")) ){
				QC_Amt = OutputXML.get(i).get(26).toString();
			}
			if(!(OutputXML.get(i).get(27) == null || OutputXML.get(i).get(27).equals("")) ){
				QC_emi = OutputXML.get(i).get(27).toString();
			}
			if(!(OutputXML.get(i).get(28) == null || OutputXML.get(i).get(28).equals("")) ){
				CAC_Indicator = OutputXML.get(i).get(28).toString();
				if (CAC_Indicator != null && !(CAC_Indicator.equalsIgnoreCase(""))){
					if (CAC_Indicator.equalsIgnoreCase("true")){
						CAC_Indicator="Y";
					}
					else {
						CAC_Indicator="N";
					}
				}
			}
			String TakeOverIndicator="";
			if(!(OutputXML.get(i).get(29) == null || OutputXML.get(i).get(29).equals("")) ){
				TakeOverIndicator = OutputXML.get(i).get(29).toString();
				if (TakeOverIndicator != null && !(TakeOverIndicator.equalsIgnoreCase(""))){
					if (TakeOverIndicator.equalsIgnoreCase("true")){
						TakeOverIndicator="Y";
					}
					else {
						TakeOverIndicator="N";
					}
				}
			}
			if(!(OutputXML.get(i).get(30) == null || OutputXML.get(i).get(30).equals("")) ){
				consider_for_obligation = OutputXML.get(i).get(30).toString();
				if (consider_for_obligation != null && !(consider_for_obligation.equalsIgnoreCase(""))){
					if (consider_for_obligation.equalsIgnoreCase("true")){
						consider_for_obligation="Y";
					}
					else {
						consider_for_obligation="N";
					}
				}
			}

			add_xml_str = add_xml_str + "<ExternalBureauIndividualProducts><applicant_id>"+CifId+"</applicant_id>";
			add_xml_str = add_xml_str + "<external_bureau_individual_products_id>"+AgreementId+"</external_bureau_individual_products_id>";
			add_xml_str = add_xml_str + "<contract_type>"+ContractType+"</contract_type>";
			add_xml_str = add_xml_str + "<provider_no>"+provider_no+"</provider_no>";
			add_xml_str = add_xml_str + "<phase>"+phase+"</phase>";
			add_xml_str = add_xml_str + "<role_of_customer>"+CustRoleType+"</role_of_customer>";
			add_xml_str = add_xml_str + "<start_date>"+start_date+"</start_date>"; 

			add_xml_str = add_xml_str + "<close_date>"+close_date+"</close_date>";
			add_xml_str = add_xml_str + "<outstanding_balance>"+OutStanding_Balance+"</outstanding_balance>";
			add_xml_str = add_xml_str + "<total_amount>"+TotalAmt+"</total_amount>";
			add_xml_str = add_xml_str + "<payments_amount>"+PaymentsAmt+"</payments_amount>";
			add_xml_str = add_xml_str + "<total_no_of_instalments>"+TotalNoOfInstalments+"</total_no_of_instalments>";
			add_xml_str = add_xml_str + "<no_of_remaining_instalments>"+RemainingInstalments+"</no_of_remaining_instalments>";
			add_xml_str = add_xml_str + "<worst_status>"+WorstStatus+"</worst_status>";
			add_xml_str = add_xml_str + "<worst_status_date>"+WorstStatusDate+"</worst_status_date>";

			add_xml_str = add_xml_str + "<credit_limit>"+CreditLimit+"</credit_limit>";
			add_xml_str = add_xml_str + "<overdue_amount>"+OverdueAmt+"</overdue_amount>";
			add_xml_str = add_xml_str + "<no_of_days_payment_delay>"+NofDaysPmtDelay+"</no_of_days_payment_delay>";
			add_xml_str = add_xml_str + "<mob>"+MonthsOnBook+"</mob>";
			add_xml_str = add_xml_str + "<last_repayment_date>"+last_repayment_date+"</last_repayment_date>";
			if (currently_current != null && currently_current.equalsIgnoreCase("1"))
				add_xml_str = add_xml_str + "<currently_current>Y</currently_current>";
			else
			{
				add_xml_str = add_xml_str + "<currently_current>N</currently_current>";
			}
			add_xml_str = add_xml_str + "<current_utilization>"+current_utilization+"</current_utilization>";
			add_xml_str = add_xml_str + "<dpd_30_last_6_mon>"+DPD30Last6Months+"</dpd_30_last_6_mon>";

			add_xml_str = add_xml_str + "<dpd_60p_in_last_18_mon>"+DPD60Last18Months+"</dpd_60p_in_last_18_mon>";
			add_xml_str = add_xml_str + "<no_months_aecb_history>"+AECBHistMonthCnt+"</no_months_aecb_history>";
			add_xml_str = add_xml_str + "<delinquent_in_last_3months>"+delinquent_in_last_3months+"</delinquent_in_last_3months>";
			add_xml_str = add_xml_str + "<clean_funded>"+""+"</clean_funded>";
			add_xml_str = add_xml_str + "<cac_indicator>"+CAC_Indicator+"</cac_indicator>";
			add_xml_str = add_xml_str + "<qc_emi>"+QC_emi+"</qc_emi>";
			if (ReqProd.equalsIgnoreCase("Credit Card")){
				add_xml_str = add_xml_str + "<qc_amount>"+QC_Amt+"</qc_amount><company_flag>N</company_flag><cac_bank_name></cac_bank_name><consider_for_obligation>"+consider_for_obligation+"</consider_for_obligation></ExternalBureauIndividualProducts>";
			}
			else{
				add_xml_str = add_xml_str + "<qc_amount>"+QC_Amt+"</qc_amount><company_flag>N</company_flag><cac_bank_name></cac_bank_name><take_over_indicator>"+TakeOverIndicator+"</take_over_indicator><consider_for_obligation>"+consider_for_obligation+"</consider_for_obligation></ExternalBureauIndividualProducts>";
			}

		}
		SKLogger.writeLog("RLOSCommon", "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}
	public String ExternalBureauManualAddIndividualProducts(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		SKLogger.writeLog("ExternalBureauManualAddIndividualProducts sQuery", "");
		int Man_liab_row_count = formObject.getLVWRowCount("cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
		String applicant_id = formObject.getNGValue("cmplx_Customer_CIFNO");
		String  add_xml_str ="";
		Date date = new Date();
		String modifiedDate= new SimpleDateFormat("dd/MM/yyyy").format(date);
		SKLogger.writeLog("ExternalBureauIndividualProducts list size"+Man_liab_row_count, "");
		if (Man_liab_row_count !=0){
			for (int i = 0; i<Man_liab_row_count;i++){
				String Type_of_Contract = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 0); //0
				String Limit = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 1); //0
				String EMI = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 2); //0
				String Take_over_Indicator = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 3); //0
				String Take_over_amount = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 4); //0
				String cac_Indicator = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 5); //0
				String Qc_amt = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 6); //0
				String Qc_Emi = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 7); //0
				if (cac_Indicator.equalsIgnoreCase("true")){
					cac_Indicator="Y";
				}
				else {
					cac_Indicator="N";
				}
				String consider_for_obligation = (formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 8).equalsIgnoreCase("true")?"Y":"N"); //0
				String remarks = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 9); //0
				//String MOB = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 9); //0
				String Utilization = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 11); //0
				String OutStanding = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 12); //0
				String mob = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 10);
				String delinquent_in_last_3months = (formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 13).equalsIgnoreCase("true")?"1":"0");
				String dpd_30_last_6_mon = (formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 14).equalsIgnoreCase("true")?"1":"0");
				String dpd_60p_in_last_18_mon = (formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 15).equalsIgnoreCase("true")?"1":"0");
				add_xml_str = add_xml_str + "<ExternalBureauIndividualProducts><applicant_id>"+applicant_id+"</applicant_id>";
				add_xml_str = add_xml_str + "<external_bureau_individual_products_id></external_bureau_individual_products_id>";
				add_xml_str = add_xml_str + "<contract_type>"+Type_of_Contract+"</contract_type>";
				add_xml_str = add_xml_str + "<provider_no></provider_no>";
				add_xml_str = add_xml_str + "<phase>A</phase>";
				add_xml_str = add_xml_str + "<role_of_customer>Main</role_of_customer>";
				add_xml_str = add_xml_str + "<start_date>"+modifiedDate+"</start_date>"; 

				add_xml_str = add_xml_str + "<close_date></close_date>";
				add_xml_str = add_xml_str + "<outstanding_balance>"+OutStanding+"</outstanding_balance>";
				add_xml_str = add_xml_str + "<total_amount>"+Limit+"</total_amount>";
				add_xml_str = add_xml_str + "<payments_amount>"+EMI+"</payments_amount>";
				add_xml_str = add_xml_str + "<total_no_of_instalments></total_no_of_instalments>";
				add_xml_str = add_xml_str + "<no_of_remaining_instalments></no_of_remaining_instalments>";
				add_xml_str = add_xml_str + "<worst_status></worst_status>";
				add_xml_str = add_xml_str + "<worst_status_date></worst_status_date>";

				add_xml_str = add_xml_str + "<credit_limit>"+Limit+"</credit_limit>";
				add_xml_str = add_xml_str + "<overdue_amount></overdue_amount>";
				add_xml_str = add_xml_str + "<no_of_days_payment_delay></no_of_days_payment_delay>";
				add_xml_str = add_xml_str + "<mob>"+mob+"</mob>";
				add_xml_str = add_xml_str + "<last_repayment_date></last_repayment_date>";

				add_xml_str = add_xml_str + "<currently_current>N</currently_current>";

				add_xml_str = add_xml_str + "<current_utilization>"+Utilization+"</current_utilization>";
				add_xml_str = add_xml_str + "<dpd_30_last_6_mon>"+dpd_30_last_6_mon+"</dpd_30_last_6_mon>";

				add_xml_str = add_xml_str + "<dpd_60p_in_last_18_mon>"+dpd_60p_in_last_18_mon+"</dpd_60p_in_last_18_mon>";
				add_xml_str = add_xml_str + "<no_months_aecb_history></no_months_aecb_history>";
				add_xml_str = add_xml_str + "<delinquent_in_last_3months>"+delinquent_in_last_3months+"</delinquent_in_last_3months>";
				add_xml_str = add_xml_str + "<clean_funded>"+""+"</clean_funded>";
				add_xml_str = add_xml_str + "<cac_indicator>"+cac_Indicator+"</cac_indicator>";
				add_xml_str = add_xml_str + "<qc_emi>"+Qc_Emi+"</qc_emi>";
				add_xml_str = add_xml_str + "<qc_amount>"+Qc_amt+"</qc_amount><company_flag>N</company_flag><cac_bank_name></cac_bank_name><take_over_indicator>"+Take_over_Indicator+"</take_over_indicator><consider_for_obligation>"+consider_for_obligation+"</consider_for_obligation></ExternalBureauIndividualProducts>";

			}

		}
		SKLogger.writeLog("RLOSCommon", "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}


	public String ExternalBureauPipelineProducts(){
		SKLogger.writeLog("RLOSCommon java file", "inside ExternalBureauPipelineProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "select AgreementId,ProviderNo,LoanType,LoanDesc,CustRoleType,Datelastupdated,TotalAmt,TotalNoOfInstalments,'' as col1,NoOfDaysInPipeline,isnull(Consider_For_Obligations,'true') from ng_rlos_cust_extexpo_LoanDetails where wi_name  =  '"+formObject.getWFWorkitemName()+"' and LoanStat = 'Pipeline' union select CardEmbossNum,ProviderNo,CardTypeDesc,CardType,CustRoleType,LastUpdateDate,'' as col2,NoOfInstallments,TotalAmount,NoOfDaysInPipeLine,isnull(Consider_For_Obligations,'true')  from ng_rlos_cust_extexpo_CardDetails where wi_name  =  '"+formObject.getWFWorkitemName()+"' and cardstatus = 'Pipeline'";
		SKLogger.writeLog("ExternalBureauPipelineProducts sQuery"+sQuery, "");
		String  add_xml_str = "";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		SKLogger.writeLog("ExternalBureauPipelineProducts list size"+OutputXML.size(), "");
		String cifId=formObject.getNGValue("cmplx_Customer_CIFNO");

		for (int i = 0; i<OutputXML.size();i++){

			String agreementID = "";
			String ProviderNo="";
			String contractType = "";
			String productType = "";
			String role = ""; 
			String lastUpdateDate = "";
			String TotAmt = "";
			String noOfInstalmnt = "";
			String creditLimit = "";
			String noOfDayinPpl = "";
			String consider_for_obligation="";
			if(!(OutputXML.get(i).get(0) == null || OutputXML.get(i).get(0).equals("")) ){
				agreementID = OutputXML.get(i).get(0).toString();
			}
			if(!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals("")) ){
				ProviderNo = OutputXML.get(i).get(1).toString();
			}
			if(OutputXML.get(i).get(2)!=null && !OutputXML.get(i).get(2).isEmpty() &&  !OutputXML.get(i).get(2).equals("") && !OutputXML.get(i).get(2).equalsIgnoreCase("null") ){
				contractType = OutputXML.get(i).get(2).toString();
			}
			if(!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals("")) ){
				productType = OutputXML.get(i).get(3).toString();
			}
			if(!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals("")) ){
				role = OutputXML.get(i).get(4).toString();
			}
			if(OutputXML.get(i).get(5)!=null && !OutputXML.get(i).get(5).isEmpty() &&  !OutputXML.get(i).get(5).equals("") && !OutputXML.get(i).get(5).equalsIgnoreCase("null") ){
				lastUpdateDate = OutputXML.get(i).get(5).toString();
			}
			if(OutputXML.get(i).get(6)!=null && !OutputXML.get(i).get(6).isEmpty() &&  !OutputXML.get(i).get(6).equals("") && !OutputXML.get(i).get(6).equalsIgnoreCase("null") ){
				TotAmt = OutputXML.get(i).get(6).toString();
			}
			if(!(OutputXML.get(i).get(7) == null || OutputXML.get(i).get(7).equals("")) ){
				noOfInstalmnt = OutputXML.get(i).get(7).toString();
			}
			if(!(OutputXML.get(i).get(8) == null || OutputXML.get(i).get(8).equals("")) ){
				creditLimit = OutputXML.get(i).get(8).toString();
			}
			if(OutputXML.get(i).get(9)!=null && !OutputXML.get(i).get(9).isEmpty() &&  !OutputXML.get(i).get(9).equals("") && !OutputXML.get(i).get(9).equalsIgnoreCase("null") ){
				noOfDayinPpl = OutputXML.get(i).get(9).toString();
			}
			if(!(OutputXML.get(i).get(10) == null || OutputXML.get(i).get(10).equals("")) ){
				consider_for_obligation = OutputXML.get(i).get(10).toString();
				if (consider_for_obligation != null && !(consider_for_obligation.equalsIgnoreCase(""))){
					if (consider_for_obligation.equalsIgnoreCase("true")){
						consider_for_obligation="Y";
					}
					else {
						consider_for_obligation="N";
					}
				}
			}

			add_xml_str = add_xml_str + "<ExternalBureauPipelineProducts><applicant_ID>"+cifId+"</applicant_ID>";
			add_xml_str = add_xml_str + "<external_bureau_pipeline_products_id>"+agreementID+"</external_bureau_pipeline_products_id>";
			add_xml_str = add_xml_str + "<ppl_provider_no>"+ProviderNo+"</ppl_provider_no>";
			add_xml_str = add_xml_str + "<ppl_type_of_contract>"+contractType+"</ppl_type_of_contract>";
			add_xml_str = add_xml_str + "<ppl_type_of_product>"+productType+"</ppl_type_of_product>";
			add_xml_str = add_xml_str + "<ppl_phase>"+"PIPELINE"+"</ppl_phase>";
			add_xml_str = add_xml_str + "<ppl_role>"+role+"</ppl_role>"; 

			add_xml_str = add_xml_str + "<ppl_date_of_last_update>"+lastUpdateDate+"</ppl_date_of_last_update>";
			add_xml_str = add_xml_str + "<ppl_total_amount>"+TotAmt+"</ppl_total_amount>";
			add_xml_str = add_xml_str + "<ppl_no_of_instalments>"+noOfInstalmnt+"</ppl_no_of_instalments>";
			add_xml_str = add_xml_str + "<ppl_credit_limit>"+creditLimit+"</ppl_credit_limit>";

			add_xml_str = add_xml_str + "<ppl_no_of_days_in_pipeline>"+noOfDayinPpl+"</ppl_no_of_days_in_pipeline><company_flag>N</company_flag><consider_for_obligation>"+consider_for_obligation+"</consider_for_obligation></ExternalBureauPipelineProducts>"; // to be populated later




		}
		SKLogger.writeLog("RLOSCommon", "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}

	public String getProduct_details(){
		SKLogger.writeLog("RLOSCommon java file", "inside getProduct_details : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int prod_row_count = formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		SKLogger.writeLog("RLOSCommon java file", "inside getCustAddress_details add_row_count+ : "+prod_row_count);
		String FinalLimitQuery="select isnull(Final_limit,0) from ng_rlos_IGR_Eligibility_CardProduct where wi_name='"+formObject.getWFWorkitemName()+"'";
		List<List<String>> FinalLimitXML = formObject.getDataFromDataSource(FinalLimitQuery);
		//changed by aman shukla on 18th Sept.
		String FinalLimitPLQuery="select isnull(FinalLoanAmount,0) from ng_rlos_IGR_Eligibility_PersonalLoan where wi_name='"+formObject.getWFWorkitemName()+"'";
		List<List<String>> FinalLimitPLXML = formObject.getDataFromDataSource(FinalLimitPLQuery);
		
		
		
		String finalLimit="";
		if (FinalLimitXML.size()!= 0){
			finalLimit=FinalLimitXML.get(0).get(0);
			formObject.setNGValue("cmplx_EligibilityAndProductInfo_FinalLimit", finalLimit);
		}
		else if (FinalLimitPLXML.size()!= 0){
			finalLimit=FinalLimitPLXML.get(0).get(0);
			formObject.setNGValue("cmplx_EligibilityAndProductInfo_FinalLimit", finalLimit);
		}

		String  prod_xml_str ="";

		for (int i = 0; i<prod_row_count;i++){
			String prod_type = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 0); //0
			String reqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 1);//1
			String subProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2);//2
			String reqLimit=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 3);//3
			reqLimit=reqLimit.replaceAll(",", "");
			String appType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 4);//4
			String cardProd = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 5);//5
			String scheme=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 8);//6
			String tenure=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 7);//7
			String limitExpiry=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 12);//8
			String ApplicationDate=formObject.getNGValue("Introduction_date");
			String AppCateg=formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg");
			SKLogger.writeLog("RLOSCommon java file", "inside getCustAddress_details ApplicationDate+ : "+ApplicationDate);
			ApplicationDate=ApplicationDate.substring(0, 10);
			limitExpiry=Convert_dateFormat(limitExpiry, "dd/MM/yyyy", "yyyy-MM-dd");
			String EMI="";
			EMI=formObject.getNGValue("cmplx_EligibilityAndProductInfo_EMI");
			if(EMI == null){
				EMI=""; 
			}
			if(AppCateg == null){
				AppCateg=""; 
			}
			// ApplicationDate=Convert_dateFormat(ApplicationDate, "dd/MM/yyyy", "yyyy-MM-dd");

			prod_xml_str = prod_xml_str + "<ApplicationDetails><product_type>"+(prod_type.equalsIgnoreCase("Conventional")?"CON":"ISL")+"</product_type>";
			prod_xml_str = prod_xml_str + "<app_category>"+AppCateg+"</app_category>";
			prod_xml_str = prod_xml_str + "<requested_product>"+(reqProd.equalsIgnoreCase("Personal Loan")?"PL":"CC")+"</requested_product>";
			prod_xml_str = prod_xml_str + "<requested_limit>"+reqLimit+"</requested_limit>";
			prod_xml_str = prod_xml_str + "<sub_product>"+subProd+"</sub_product>";
			prod_xml_str = prod_xml_str + "<requested_card_product>"+cardProd+"</requested_card_product>";
			prod_xml_str = prod_xml_str + "<application_type>"+appType+"</application_type>";
			prod_xml_str = prod_xml_str + "<scheme>"+scheme+"</scheme>";
			prod_xml_str = prod_xml_str + "<tenure>"+tenure+"</tenure>";
			prod_xml_str = prod_xml_str + "<customer_type>"+(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true")?"NTB":"Existing")+"</customer_type>";
			if (reqProd.equalsIgnoreCase("Credit Card")){
				prod_xml_str = prod_xml_str + "<limit_expiry_date>"+limitExpiry+"</limit_expiry_date><final_limit>"+finalLimit+"</final_limit><emi>"+EMI+"</emi><manual_deviation>N</manual_deviation></ApplicationDetails>";
			}
			else{
				prod_xml_str = prod_xml_str + "<limit_expiry_date>"+limitExpiry+"</limit_expiry_date><final_limit>"+finalLimit+"</final_limit><emi>"+EMI+"</emi><manual_deviation>N</manual_deviation><application_date>"+ApplicationDate+"</application_date></ApplicationDetails>";
			}

		}
		SKLogger.writeLog("RLOSCommon", "Address tag Cration: "+ prod_xml_str);
		return prod_xml_str;
	}
	// Deepak Change to calculate EMI start
	public static String getEMI(double loanAmount,double rate,double tenureMonths)
	{       
		String loanAmt_DaysDiff="";
		try{
			if(loanAmount==0 ||rate==0||tenureMonths==0){
				return "0";
			}
			BigDecimal B_intrate= new BigDecimal(rate);
			BigDecimal B_tenure= new BigDecimal(tenureMonths);
			BigDecimal B_loamamount= new BigDecimal(loanAmount);
			loanAmt_DaysDiff=calcgoalseekEMI(B_intrate,B_tenure,B_loamamount);

		}
		catch(Exception e){
			SKLogger.writeLog("RLOSCommon", "Exception occured in getEMI() : ");
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
			SKLogger.writeLog("RLOSCommon", "Exception occured in calcEMI() : ");
		}
		return emi;
	}
	public static String calcgoalseekEMI(BigDecimal B_intrate,BigDecimal B_tenure,BigDecimal B_loamamount) {
		String loanAmt_DaysDiff="0";
		try{
			BigDecimal PMTEMI=calcEMI(B_loamamount,B_tenure,B_intrate);

			double seedvalue=Math.round(PMTEMI.doubleValue());
			double loamamount=B_loamamount.doubleValue();;
			int tenure=B_tenure.intValue();;
			double intrate=(B_intrate.intValue())/100.0;	
			System.out.println("seedvalue  **************"+seedvalue);
			System.out.println("loamamount  **************"+loamamount);
			System.out.println("tenure=  **************"+tenure);
			System.out.println("intrate  **************"+intrate);

			int iterations=2*(int)Math.round(PMTEMI.intValue()*.10);
			System.out.println("PMTEMI   **"+PMTEMI+"  for intrate @"+intrate+ " iterations"+iterations);
			loanAmt_DaysDiff=seedvalue+"";
		}
		catch(Exception e){
			SKLogger.writeLog("RLOSCommon", "Exception occured in calcgoalseekEMI() : ");
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
			System.out.println("final_rate_new 1ST pmt11 : " + pmt);
		}
		catch(Exception e){
			pmt=0;
		}
		return pmt;
		
	}
	//Akshay for comma Change
	public String putComma(String field){
		SKLogger.writeLog("RLOS Common", "Inside putComma()"+field);
		String limit=field.replaceAll(",","");
		double amount = Double.parseDouble(limit);
	DecimalFormat myFormatter = new DecimalFormat("#,###.000");
	String convString=myFormatter.format(amount);
		SKLogger.writeLog("RLOS Common", "Inside putComma(): Coverted String is:"+convString);
		return convString;
	}
	//Akshay for comma Change
	// Deepak Change to calculate EMI END
	public String Convert_dateFormat(String date,String Old_Format,String new_Format)
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
				//added by saurabh on 19th july.
				setcustomer_enable();
				formObject.setNGValue("Is_Customer_Details", "Y");
				alert_msg="Existing Customer Details fetched Sucessfully";
				formObject.setVisible("Customer_Frame2", true);
				formObject.setNGValue("cmplx_Customer_IscustomerSave", "Y");
				formObject.setLocked("Customer_Button1", false);
				formObject.setNGValue("Cust_Name", formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+ formObject.getNGValue("cmplx_Customer_MiddleName")+" "+ formObject.getNGValue("cmplx_Customer_LAstNAme"));
				formObject.setNGValue("Cif_Id", formObject.getNGValue("cmplx_Customer_CIFNO"));
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
				try{
					//added By Aman-28/6/17 for checking aecb checkbox
					String AECBheld =  (outputResponse.contains("<AECBConsentHeld>")) ? outputResponse.substring(outputResponse.indexOf("<AECBConsentHeld>")+"</AECBConsentHeld>".length()-1,outputResponse.indexOf("</AECBConsentHeld>")):"";
					if (AECBheld.equalsIgnoreCase("Y"))
					{
						SKLogger.writeLog("RLOS Common", "Inside AECB Consent true check!!");
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
					SKLogger.writeLog("RLOS","height of supplement is: "+formObject.getHeight("Supplementary_Container"));
					formObject.setTop("Supplementary_Container", formObject.getTop("CardDetails")+80);  
					formObject.setTop("FATCA", formObject.getTop("Supplementary_Container")+30);

					formObject.setTop("KYC", formObject.getTop("FATCA")+formObject.getHeight("FATCA")+10);
					formObject.setTop("OECD", formObject.getTop("KYC")+formObject.getHeight("KYC")+20);
					formObject.setTop("ReferenceDetails", formObject.getTop("OECD")+formObject.getHeight("OECD")+30);
					formObject.setTop("AuthorisedSignatoryDetails", formObject.getTop("CompanyDetails")+formObject.getHeight("CompanyDetails")+20);
					formObject.setTop("PartnerDetails", formObject.getTop("AuthorisedSignatoryDetails")+formObject.getHeight("AuthorisedSignatoryDetails")+20);
					
					formObject.setTop("Eligibility_Emp",470);
					formObject.setTop("WorldCheck_fetch",520);
					valueSetCustomer(outputResponse,"Primary_CIF");
					//code to set Emirates of residence start.
					//code added to load the picklist of Employer details and address
					employment_details_load();
					loadPicklist_Address();
				}
				catch(Exception ex){
					
				}
				
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
					String str_PassportIssueDate=formObject.getNGValue("cmplx_Customer_PassportIssueDate");
					String str_VisaIssuedate=formObject.getNGValue("cmplx_Customer_VisaIssuedate");
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
					if(str_PassportIssueDate!=null&&!str_PassportIssueDate.equalsIgnoreCase("")){
						formObject.setNGValue("cmplx_Customer_PassportIssueDate",sdf2.format(sdf1.parse(str_PassportIssueDate)),false);
					}
					if(str_VisaIssuedate!=null&&!str_VisaIssuedate.equalsIgnoreCase("")){
						formObject.setNGValue("cmplx_Customer_VisaIssuedate",sdf2.format(sdf1.parse(str_VisaIssuedate)),false);
					}

					//formObject.setNGValue("cmplx_Customer_VIPFlag","true");
					String mobileNo = formObject.getNGValue("cmplx_Customer_MobNo");
					if(mobileNo!=null && !mobileNo.equalsIgnoreCase("")){
						formObject.setNGValue("OTP_Mobile_NO", mobileNo);
						formObject.setEnabled("OTP_Mobile_NO", false);
					}

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

	public String fetch_cust_details_supplementary(){
		String outputResponse="";
		String ReturnCode="";
		String alert_msg="";
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			outputResponse = GenerateXML("CUSTOMER_DETAILS","Supplementary_Card_Details");
			SKLogger.writeLog("RLOS value of ReturnCode","Inside Customer");
			/*String Gender =  (outputResponse.contains("<Gender>")) ? outputResponse.substring(outputResponse.indexOf("<Gender>")+"</Gender>".length()-1,outputResponse.indexOf("</Gender>")):"";
			SKLogger.writeLog("RLOS value of Gender",Gender);
			String  Marital_Status =  (outputResponse.contains("<MaritialStatus>")) ? outputResponse.substring(outputResponse.indexOf("<MaritialStatus>")+"</MaritialStatus>".length()-1,outputResponse.indexOf("</MaritialStatus>")):"";
			SKLogger.writeLog("RLOS value of Marital_Status",Marital_Status);*/
			ReturnCode =  (outputResponse.contains("<ReturnCode>")) ? outputResponse.substring(outputResponse.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,outputResponse.indexOf("</ReturnCode>")):"";
			SKLogger.writeLog("RLOS value of ReturnCode",ReturnCode);

			if(ReturnCode.equalsIgnoreCase("0000") ){
				valueSetCustomer(outputResponse,"Supplementary_Card_Details");
				alert_msg="Existing Customer Details fetched Sucessfully";

				String Date1=formObject.getNGValue("DOB");
				SKLogger.writeLog("RLOS value of Date1111",Date1);
				SimpleDateFormat sdf1=new SimpleDateFormat("dd-mm-yyyy");
				SimpleDateFormat sdf2=new SimpleDateFormat("dd/mm/yyyy");
				String Datechanged=sdf2.format(sdf1.parse(Date1));
				SKLogger.writeLog("RLOS value ofDatechanged",Datechanged);
				formObject.setNGValue("DOB",Datechanged,false);
			}
			else{
				alert_msg="Error While Fetch Customer Details.";
			}

		}catch(Exception ex){
			SKLogger.writeLog("RLOS Common", "Exception occured in fetch_cust_details_primary method"+printException(ex));
			if(alert_msg.equalsIgnoreCase("")){
				alert_msg="Error occured while performing Fetch Customer details, Kinldy contact administrator or contact after sometime";
			}
		}
		return alert_msg;
	}
	//New method added to fetch Eligibility and Product fragment
	public void fetch_EligPrd_frag(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		formObject.fetchFragment("EligibilityAndProductInformation", "ELigibiltyAndProductInfo", "q_EligAndProductInfo");
		formObject.setNGFrameState("EligibilityAndProductInformation", 0);
		SKLogger.writeLog("RLOS value of Reason","Saurabh ELPINFO,Framexpanded");
		Fields_Eligibility();
		Fields_ApplicationType_Eligibility();
		//change by saurabh on 10th Oct
		setChargesFields();
		formObject.setEnabled("cmplx_EligibilityAndProductInfo_FinalInterestRate", false);
		SKLogger.writeLog("RLOS value of Reason","Eligibility grid");
		if(formObject.getNGValue("PrimaryProduct").equalsIgnoreCase("Personal Loan")/* && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")*/){
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
			SKLogger.writeLog("RLOS", "Funding Account Details now Visible...!!!");
			formObject.setNGValue("cmplx_EligibilityAndProductInfo_Tenor", tenure);
		}
		else if(formObject.getNGValue("PrimaryProduct").equalsIgnoreCase("Credit Card")/* && formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")*/){
			formObject.setVisible("ELigibiltyAndProductInfo_Frame5",true);//CC
			formObject.setVisible("ELigibiltyAndProductInfo_Frame6",true);//ELigible for Card
			formObject.setVisible("ELigibiltyAndProductInfo_Frame2", false);//Funding Account Number Grid
			formObject.setVisible("ELigibiltyAndProductInfo_Frame4", false);//Personal Loan
			formObject.setVisible("ELigibiltyAndProductInfo_Frame3", false);//Lein Details
			formObject.setTop("ELigibiltyAndProductInfo_Frame5",5);//CC
			formObject.setTop("ELigibiltyAndProductInfo_Frame6",formObject.getTop("ELigibiltyAndProductInfo_Frame5")+25);//Eligible For Card

			if(formObject.getNGValue("CardProduct_Primary").toUpperCase().contains("-SEC") /*&& formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",i,9).equalsIgnoreCase("Primary")*/){
				formObject.setVisible("ELigibiltyAndProductInfo_Frame3", true);//Lein Details
				formObject.setTop("ELigibiltyAndProductInfo_Frame3",formObject.getTop("ELigibiltyAndProductInfo_Frame6")+25);
				SKLogger.writeLog("RLOS", "Lein Details now Visible...!!!");
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
		calculateMaturityValues();
	}
	public  String Clean_Xml(String InputXml){
		String Output_Xml="";
		try{
			Document doc = getDocument(InputXml);
				removeEmptyNodes(doc);
				DOMSource domSource = new DOMSource(doc);
		       StringWriter writer = new StringWriter();
		       StreamResult result = new StreamResult(writer);
		       TransformerFactory tf = TransformerFactory.newInstance();
		       Transformer transformer = tf.newTransformer();
		       transformer.transform(domSource, result);
		      Output_Xml = writer.toString().substring(38);
		      SKLogger.writeLog("RLSO Common Clean_xml","final1: "+ Output_Xml);
			
		}
		catch(Exception e){
			
		}
		return Output_Xml;
	}
	public  void removeEmptyNodes(Node node) {
	    NodeList list = node.getChildNodes();
	    List<Node> nodesToRecursivelyCall = new LinkedList<Node>();
	    for (int i = 0; i < list.getLength(); i++) {
	        nodesToRecursivelyCall.add(list.item(i));
	    }
	    for(Node tempNode : nodesToRecursivelyCall) {
	        removeEmptyNodes(tempNode);
	    }
	    boolean emptyElement = node.getNodeType() == Node.ELEMENT_NODE && node.getChildNodes().getLength() == 0;
	    boolean emptyText = node.getNodeType() == Node.TEXT_NODE && node.getNodeValue().trim().isEmpty();
	    boolean selectText = node.getNodeType() == Node.TEXT_NODE && (node.getNodeValue().trim().equalsIgnoreCase("--Select--")||node.getNodeValue().trim().equalsIgnoreCase("null"));
	    if (emptyElement || emptyText || selectText) {
	        if(!node.hasAttributes()) {
	            node.getParentNode().removeChild(node);
	        }
	    }
	  //  return node;
	}

	
}
