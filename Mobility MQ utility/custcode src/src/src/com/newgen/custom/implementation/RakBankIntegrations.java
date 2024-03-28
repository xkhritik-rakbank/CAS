/**--------------------------------------------------------------------------
 *        NEWGEN SOFTWARE TECHNOLOGIES LIMITED
 *    Group                     : AP2
 *    Product / Project         : NEMF 3.2/RakBank Implementation
 *    Module                    : Server Module
 *    File Name                 : RakBankIntegrations.java
 *    Author                    : Gaurav Sharma(gaurav-s)
 *    Date written (DD/MM/YYYY) : 25/03/2017
 *    Description               : This file contains methods to be called from tab
 * --------------------------------------------------------------------------
 *                   CHANGE HISTORY
 * ---------------------------------------------------------------------------
 * Date           Name      		Comment
 * ---------------------------------------------------------------------------
 * 
 * ---------------------------------------------------------------------------
 */

package com.newgen.custom.implementation;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.newgen.custom.rakbank.interfaces.RakBank;
import com.newgen.custom.rakbank.resources.concrete.Call;
import com.newgen.custom.rakbank.resources.concrete.CustExpose;
import com.newgen.custom.rakbank.resources.concrete.IntegrationEntity;
import com.newgen.custom.rakbank.resources.concrete.ResponseEntity;
import com.newgen.custom.rakbank.resources.concrete.WebServiceResponseHandler;
import com.newgen.custom.rakbank.resources.concrete.WebServicerequesthandler;
import com.newgen.custom.rakbank.resources.concrete.multiplecifresponsehandler;
import com.newgen.mcap.core.external.apiengine.concrete.APICallContext;
import com.newgen.mcap.core.external.basic.entities.concrete.User;
import com.newgen.mcap.core.external.configuration.entities.concrete.Configuration;
import com.newgen.mcap.core.external.logging.concrete.LogMe;
import com.newgen.mcap.core.external.resources.concrete.StreamedResource;
import com.newgen.mcap.core.external.utils.ThreadUtils;
import com.newgen.omni.jts.cmgr.XMLParser;
import com.newgen.wfdesktop.xmlapi.WFCallBroker;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class RakBankIntegrations implements RakBank {

	private Configuration configuration;
	private APICallContext apiCallContext;

	@Override
	public APICallContext getAPICallContext() throws Exception {
		return this.apiCallContext;
	}

	@Override
	public void setAPICallContext(APICallContext apiCallContext)
			throws Exception {
		this.apiCallContext = apiCallContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.newgen.mcap.core.external.basic.interfaces.AbstractedFunctionality
	 * #getAbstractionName()
	 */
	@Override
	public String getAbstractionName() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Configuration getConfiguration() throws Exception {
		// TODO Auto-generated method stub
		return configuration;
	}

	@Override
	public void setConfiguration(Configuration configuration) throws Exception {
		// TODO Auto-generated method stub
		this.configuration = configuration;
	}

	@Override
	public String getIdentifier() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.newgen.mcap.core.external.basic.interfaces.AbstractedFunctionality
	 * #getSolutionVersion()
	 */
	@Override
	public String getSolutionVersion() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Takes an IntegrationEntity object which is an entity.
	 * 
	 * @author gaurav
	 * @param IntegrationEntity
	 *            with required parameters
	 * @return ResponseEntity with webservice response
	 */
	@Override
	public ResponseEntity callWebService(IntegrationEntity requestObject) throws Exception {
			
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "[callWebService initiated] "+ new Date().toString());
		ResponseEntity toreturn = new ResponseEntity();
		String xPath = null;
		String url = null;
		String ip = null;
		String port = null;
		String proxy_username = null;
		String proxy_password = null;
		String isproxyenabled = null;
		
		// added by abhishek for making IP configurable start
		String serverAddress = null;
		String serverPort = null;
		String serverType = null;
		String cabinetName = null;
		String MQServerAddress = null;
		String MQServerPort = null;
		Node server = null;
		// added by abhishek for making IP configurable start
		Node apiUrlMapping = null;
		Node proxydetails = null;
		Call callObject = new Call();
		XMLParser xmlparser = new XMLParser();
		String callname = requestObject.getCallname();
		String operationtype = requestObject.getOperationtype();
		JSONObject responseObject = new JSONObject();
		// added by abhishek for JSP to JAVA code
		String ResponseXml = "";
		String baseurl = "";
		try {
			xPath = "./*/ApiUrlMapping";
			apiUrlMapping = configuration.searchObjectGraph(xPath, configuration.getConfigurationXmlText()).item(0);

			xPath = "./Key[@name = '" + callname + "']";
			baseurl = configuration.searchObjectGraph(xPath, (Element) apiUrlMapping).item(0).getTextContent().trim();
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "xPath=" + xPath);

			xPath = "./Key[@name = 'CUST_EXPOS_JSP']";
			String custexpojspurl = configuration.searchObjectGraph(xPath, (Element) apiUrlMapping).item(0).getTextContent().trim();
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "xPath=" + xPath);

			xPath = "./*/ProxyDetails";
			proxydetails = configuration.searchObjectGraph(xPath, configuration.getConfigurationXmlText()).item(0);
			
			xPath = "./*/Server";
			server = configuration.searchObjectGraph(xPath, configuration.getConfigurationXmlText()).item(0);

			xPath = "./Key[@name = 'IS_PROXY_ENABLED']";
			isproxyenabled = configuration.searchObjectGraph(xPath, (Element) proxydetails).item(0).getTextContent().trim();

			xPath = "./Key[@name = 'IP']";
			ip = configuration.searchObjectGraph(xPath, (Element) proxydetails).item(0).getTextContent().trim();

			xPath = "./Key[@name = 'PORT']";
			port = configuration.searchObjectGraph(xPath, (Element) proxydetails).item(0).getTextContent().trim();

			xPath = "./Key[@name = 'USERNAME']";
			proxy_username = configuration.searchObjectGraph(xPath, (Element) proxydetails).item(0).getTextContent().trim();

			xPath = "./Key[@name = 'PASSWORD']";
			proxy_password = configuration.searchObjectGraph(xPath, (Element) proxydetails).item(0).getTextContent().trim();
			
			xPath = "./Key[@name = 'serverAddress']";
			serverAddress = configuration.searchObjectGraph(xPath, (Element) server).item(0).getTextContent().trim();
			
			xPath = "./Key[@name = 'serverPort']";
			serverPort = configuration.searchObjectGraph(xPath, (Element) server).item(0).getTextContent().trim();
			
			xPath = "./Key[@name = 'serverType']";
			serverType = configuration.searchObjectGraph(xPath, (Element) server).item(0).getTextContent().trim();
			
			xPath = "./Key[@name = 'cabinetName']";
			cabinetName = configuration.searchObjectGraph(xPath, (Element) server).item(0).getTextContent().trim();
			
			xPath = "./Key[@name = 'MQServerAddress']";
			MQServerAddress = configuration.searchObjectGraph(xPath, (Element) server).item(0).getTextContent().trim();
			
			xPath = "./Key[@name = 'MQServerPort']";
			MQServerPort = configuration.searchObjectGraph(xPath, (Element) server).item(0).getTextContent().trim();

			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "PROXY_DETAILS=" + "PROXY STATUS: " + isproxyenabled + "IP: " + ip + "PORT :" + port + "USERNAME : " + proxy_username + "PASSWORD : " + proxy_password);

			JSONObject param_json_object = new JSONObject();
			JSONParser parser_json = new JSONParser();
			param_json_object = (JSONObject) parser_json.parse(requestObject.getInputjsonstring());
			User user = (User) ThreadUtils.getThreadVariable("user");
			String wiName = param_json_object.get("processId").toString().trim();
			if (callname.equalsIgnoreCase("DECTECH")) {
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "[callWebService Dectech initiated] "+ new Date().toString());
				//Gaurav Berry 19/03/2018 - put employdetails-curempcat instead customerDetails-cif
				String ApplicationDetails = getProduct_details(wiName, param_json_object);
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, " ApplicationDetails after getProduct_details method@@ + " + ApplicationDetails);
				param_json_object.put("ApplicationDetails", ApplicationDetails);
				
				
				String current_emp_catogery = param_json_object.get("employdetails-curempcat").toString().trim();
				String squerycurremp = "select code from NG_MASTER_EmployerCategory_PL with (nolock) where isActive='Y' and Description='" + current_emp_catogery + "'";
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Dectech current_emp_catogery query is " + squerycurremp);
				List < List < String >> squerycurrempXML = ExecuteSelectQueryGetList(squerycurremp);
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Dectech current_emp_catogery query is " + squerycurrempXML);
				if (!squerycurrempXML.isEmpty()) {
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Dectech current_emp_catogery query is " + squerycurrempXML.get(0).get(0));
					if (squerycurrempXML.get(0).get(0) != null) {
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "INSIDE squerycurrempXML+ " + squerycurrempXML.get(0).get(0));
						current_emp_catogery = squerycurrempXML.get(0).get(0);
					}
				}
				param_json_object.put("current_emp_catogery", current_emp_catogery);
				
				//Added by Tarang on 10/06/2019 for CR
				String cc_employer_status = param_json_object.get("employdetails-empstatusCC").toString();
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Dectech employdetails-empstatusCC's value is " + cc_employer_status);
				if ("".equalsIgnoreCase(cc_employer_status)||"--Select--".equalsIgnoreCase(cc_employer_status) || "UL".equalsIgnoreCase(cc_employer_status)) {
					cc_employer_status = "CN";
				}
				param_json_object.put("cc_employer_status", cc_employer_status);
				//Added by Tarang on 10/06/2019 for CR

				String Limit_Increase = "";
				String PreviousLoanDBR = "";
				String PreviousLoanEmp = "";
				String PreviousLoanMultiple = "";
				String PreviousLoanTAI = "";
				JSONArray prevLoanArray = getData("select isNull(PreviousLoanDBR,0) as PreviousLoanDBR, isNull(PreviousLoanEmp,0) as PreviousLoanEmp, isNull(PreviousLoanMultiple,0) as PreviousLoanMultiple, isNull(PreviousLoanTAI,0) as PreviousLoanTAI from ng_RLOS_CUSTEXPOSE_LoanDetails where Limit_Increase='true'  and Wi_Name= '" + wiName + "'",
						"PreviousLoanDBR,PreviousLoanEmp,PreviousLoanMultiple,PreviousLoanTAI");
				if (prevLoanArray != null && prevLoanArray.size() > 0) {
					JSONObject loanobject = (JSONObject) prevLoanArray.get(0);
					PreviousLoanDBR = loanobject.get("PreviousLoanDBR").toString();
					PreviousLoanEmp = loanobject.get("PreviousLoanEmp").toString();
					PreviousLoanMultiple = loanobject.get("PreviousLoanMultiple").toString();
					PreviousLoanTAI = loanobject.get("PreviousLoanTAI").toString();
				}
				param_json_object.put("prev_loan_dbr", PreviousLoanDBR);
				param_json_object.put("prev_loan_tai", PreviousLoanTAI);
				param_json_object.put("prev_loan_multiple", PreviousLoanMultiple);
				param_json_object.put("prev_loan_employer", PreviousLoanEmp);

				JSONArray NOCArray = getData("SELECT count(*) as count FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-3,GETDATE()) and returntype='ICCS' and WI_Name='" + wiName + "'", "count");
				if (NOCArray != null && NOCArray.size() > 0) {
					JSONObject chequebounceObject = (JSONObject) NOCArray.get(0);
					param_json_object.put("no_of_cheque_bounce_int_3mon_Ind", chequebounceObject.get("count").toString());
				} else {
					param_json_object.put("no_of_cheque_bounce_int_3mon_Ind", "");
				}

				JSONArray NODArray = getData("SELECT count(*) as count FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-3,GETDATE()) and returntype='DDS' and WI_Name='" + wiName + "'", "count");
				if (NODArray != null && NODArray.size() > 0) {
					JSONObject DDSReturnObject = (JSONObject) NODArray.get(0);
					param_json_object.put("no_of_DDS_return_int_3mon_Ind", DDSReturnObject.get("count").toString());
				}

				String squeryBorrow = "select distinct(borrowingCustomer) from ng_RLOS_CUSTEXPOSE_CardDetails with (nolock) WHERE  Wi_Name ='" + wiName + "' union select distinct(borrowingCustomer) from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) WHERE Wi_Name ='" + wiName + "'";
				List < List < String >> borrowing_customer = ExecuteSelectQueryGetList(squeryBorrow);
				if (borrowing_customer != null && borrowing_customer.size() > 0) {
					param_json_object.put("borrowing_customer", borrowing_customer.get(0).get(0));
				}
				
				//Gaurav 19/03/18
				String squeryfund="select distinct(FundingPattern) from ng_RLOS_CUSTEXPOSE_AcctDetails  with (nolock) WHERE  Wi_Name ='"+wiName+"'";
				List < List < String >> funding_pattern = ExecuteSelectQueryGetList(squeryfund);
				if (funding_pattern != null && funding_pattern.size() > 0) {
					param_json_object.put("funding_pattern", funding_pattern.get(0).get(0));
				}
				
				String internal_blacklist = "";
				String internal_blacklist_date = "";
				String internal_blacklist_code = "";
				String internal_negative_flag = "";
				String internal_negative_date = "";
				String internal_negative_code = "";
				JSONArray InternalBlacklistArray = getData("select BlacklistFlag,BlacklistDate,BlacklistReasonCode,NegatedFlag,NegatedDate,NegatedReasonCode from ng_rlos_cif_detail with (nolock) where cif_wi_name='" + wiName + "' and cif_searchType = 'Internal'", "BlacklistFlag,BlacklistDate,BlacklistReasonCode,NegatedFlag,NegatedDate,NegatedReasonCode");
				if (InternalBlacklistArray != null && InternalBlacklistArray.size() > 0) {
					JSONObject BlacklistObject = (JSONObject) InternalBlacklistArray.get(0);
					internal_blacklist = BlacklistObject.get("BlacklistFlag").toString();
					internal_blacklist_date = BlacklistObject.get("BlacklistDate").toString();
					internal_blacklist_code = BlacklistObject.get("BlacklistReasonCode").toString();
					internal_negative_flag = BlacklistObject.get("NegatedFlag").toString();
					internal_negative_date = BlacklistObject.get("NegatedDate").toString();
					internal_negative_code = BlacklistObject.get("NegatedReasonCode").toString();
				}
				param_json_object.put("blacklist_cust_type", "I");
				param_json_object.put("internal_blacklist", internal_blacklist);
				param_json_object.put("internal_blacklist_date", internal_blacklist_date);
				param_json_object.put("internal_blacklist_code", internal_blacklist_code);
				param_json_object.put("negative_cust_type", "I");
				param_json_object.put("internal_negative_flag", internal_negative_flag);
				param_json_object.put("internal_negative_date", internal_negative_date);
				param_json_object.put("internal_negative_code", internal_negative_code);

				String External_blacklist = "";
				String External_blacklist_date = "";
				String External_blacklist_code = "";
				JSONArray ExternalBlacklistArray = getData("select BlacklistFlag,BlacklistDate,BlacklistReasonCode from ng_rlos_cif_detail with (nolock) where cif_wi_name='" + wiName + "' and cif_searchType = 'External'", "BlacklistFlag,BlacklistDate,BlacklistReasonCode");
				if (ExternalBlacklistArray != null && ExternalBlacklistArray.size() > 0) {
					JSONObject BlacklistObject = (JSONObject) ExternalBlacklistArray.get(0);
					External_blacklist = BlacklistObject.get("BlacklistFlag").toString();
					External_blacklist_date = BlacklistObject.get("BlacklistDate").toString();
					External_blacklist_code = BlacklistObject.get("BlacklistReasonCode").toString();
				}
				param_json_object.put("external_blacklist_flag", External_blacklist);
				param_json_object.put("external_blacklist_date", External_blacklist_date);
				param_json_object.put("external_blacklist_code", External_blacklist_code);
				
				//Below Added by Tarang on 01/05/2019
                String nmfQuery="";
               	nmfQuery="select count(*) from NG_Master_NMF_not_listed_Comp  with (nolock) where comp_name='"+param_json_object.get("employdetails-curempcat").toString().trim()+"'";
                List<List<String>> nmfQueryData = ExecuteSelectQueryGetList(nmfQuery);
                if (nmfQueryData!=null && !nmfQueryData.isEmpty()){
                	LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "nmfQueryData as on 01/05 :" + nmfQueryData);
                    String NMFflag="";
                    if (nmfQueryData.get(0).get(0).equalsIgnoreCase("0")){
                        NMFflag="N";
                    }
                    else {
                        NMFflag="Y";
                    }
                    LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "NMFflag as on 01/05 :" + NMFflag);
                    param_json_object.put("nmf_flag", NMFflag);
                }
                
                
                List<List<String>> partner_count=ExecuteSelectQueryGetList("Select count(*) from NG_RLOS_GR_PartnerDetails where part_winame='"+wiName+"'");
                String  no_of_partners;
                //    RLOS.mLogger.info("Partner count is "+partner_count);
                if(partner_count!=null && !partner_count.isEmpty()){
                    no_of_partners=partner_count.get(0).get(0);
                }
                else{
                    no_of_partners="0";
                }
                LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "no_of_partners as on 01/05 :" + no_of_partners);
                param_json_object.put("no_of_partners", no_of_partners);
				//Above added by Tarang on 01/05/2019
                
				JSONArray StandingArray = getData("SELECT count(*) as count FROM ng_rlos_FinancialSummary_SiDtls WHERE WI_Name='" + wiName + "'", "count");
				if (StandingArray != null && StandingArray.size() > 0) {
					JSONObject StandingObject = (JSONObject) StandingArray.get(0);
					String standing_instruction = "";
					if (StandingObject.get("count").toString().equalsIgnoreCase("0")) {
						standing_instruction = "N";
					} else {
						standing_instruction = "Y";
					}
					param_json_object.put("standing_instruction", standing_instruction);
				}

				double avg_credit_turnover6th = 0;
				double avg_credit_turnover3rd = 0;
				double avg_bal_3 = 0;
				double avg_bal_6 = 0;
				String avg_credit_turnover_freq = param_json_object.get("selfemployed-avgcredit-turnover-frequency").toString();
				String avg_bal_freq = param_json_object.get("selfemployed-income-average-frequency").toString();
				String avg_credit_turnover = param_json_object.get("selfemployed-avgcredit-turnover").toString();
				String avg_bal = param_json_object.get("selfemployed-income-average").toString();
				String avg_bal_6_str="";
				String avg_bal_3_str="";
				String avg_credit_turnover3rd_str="";
				String avg_credit_turnover6th_str="";
				if (avg_credit_turnover != (null) && (!avg_credit_turnover.equalsIgnoreCase(""))) {
					avg_credit_turnover6th = Double.parseDouble(avg_credit_turnover);
					System.out.println("avg_credit_turnover6th value" + avg_credit_turnover6th);

					if (avg_credit_turnover_freq.equalsIgnoreCase("Annually")) {
						avg_credit_turnover6th = avg_credit_turnover6th / 2;
						avg_credit_turnover3rd = avg_credit_turnover6th / 2;
					} else if (avg_credit_turnover_freq.equalsIgnoreCase("Half Yearly")) {
						avg_credit_turnover3rd = avg_credit_turnover6th / 2;
					} else if (avg_credit_turnover_freq.equalsIgnoreCase("Quaterly")) {
						avg_credit_turnover6th = 2 * avg_credit_turnover6th;
						avg_credit_turnover3rd = avg_credit_turnover6th / 2;
					} else if (avg_credit_turnover_freq.equalsIgnoreCase("monthly")) {
						avg_credit_turnover6th = 6 * avg_credit_turnover6th;
						avg_credit_turnover3rd = avg_credit_turnover6th / 2;
					}
					avg_credit_turnover3rd_str=String.format("%.0f", avg_credit_turnover3rd);
					avg_credit_turnover6th_str=String.format("%.0f", avg_credit_turnover6th);
					
				}
				if (avg_bal != (null) && (!avg_bal.equalsIgnoreCase(""))) {
					//avg_bal_6=Double.parseDouble(avg_bal);
					System.out.println("avg_credit_turnover6th value" + avg_credit_turnover6th);

					avg_bal_6 = Double.parseDouble(avg_bal);
					if (avg_bal_freq.equalsIgnoreCase("Annually")) {
						avg_bal_6 = avg_bal_6 / 2;
						avg_bal_3 = avg_bal_6 / 2;
					} else if (avg_bal_freq.equalsIgnoreCase("Half Yearly")) {
						avg_bal_3 = avg_bal_6 / 2;
					} else if (avg_bal_freq.equalsIgnoreCase("Quaterly")) {
						avg_bal_6 = 2 * avg_bal_6;
						avg_bal_3 = avg_bal_6 / 2;
					} else if (avg_bal_freq.equalsIgnoreCase("monthly")) {
						avg_bal_6 = 6 * avg_bal_6;
						avg_bal_3 = avg_bal_6 / 2;
					}
					
					 avg_bal_6_str=String.format("%.0f", avg_bal_6);
					 avg_bal_3_str=String.format("%.0f", avg_bal_3);
				}
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "avg_credit_turnover6th :" + avg_credit_turnover6th_str);
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "avg_credit_turnover3rd :" + avg_credit_turnover3rd_str);
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "avg_bal_3 :" + avg_bal_3_str);
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "avg_bal_6 :" + avg_bal_6_str);
				param_json_object.put("avg_credit_turnover_6",avg_credit_turnover6th_str);
				param_json_object.put("avg_credit_turnover_3", avg_credit_turnover3rd_str);
				param_json_object.put("avg_bal_3", avg_bal_3_str);
				param_json_object.put("avg_bal_6", avg_bal_6_str);


				double CreditLimit = 0.0f;
				double TotalOutstandingAmt = 0.0f;
				double TotalAmount = 0.0f;
				double TotalAmt = 0.0f;
				double Total;
				double takeOverAmount = 0.0f;
				//List < List < String >> aggregate_exposed = ExecuteSelectQueryGetList("select (select isNull((Sum(convert(float,replace([TotalOutstandingAmt],'NA','0')))),0) as TotalOutstandingAmt from ng_RLOS_CUSTEXPOSE_loanDetails with (nolock) where Consider_For_Obligations='true' and LoanStat in ('A','ACTIVE') and wi_name ='" + wiName + "') + (select isNull((Sum(convert(float,replace([TotalOutstandingAmt],'NA','0')))),0) as TotalOutstandingAmt from ng_RLOS_CUSTEXPOSE_loanDetails with (nolock) where Consider_For_Obligations='true' and LoanStat ='Pipeline' and wi_name='" + wiName + "') +(select isNull((Sum(convert(float,replace([OutstandingAmt],'NA','0')))),0)  from ng_RLOS_CUSTEXPOSE_CardDetails with (nolock) where  Consider_For_Obligations='true' and CardStatus in ('A','ACTIVE') and wi_name ='" + wiName + "' and SchemeCardProd like '%LOC%') + (select isNull((Sum(convert(float,replace([CreditLimit],'NA','0')))),0) as OutstandingAmt from ng_RLOS_CUSTEXPOSE_CardDetails with (nolock) where  Consider_For_Obligations='true' and CardStatus in ('A','ACTIVE') and wi_name ='" + wiName + "' and SchemeCardProd not like '%LOC%')+( select  isNull((Sum(convert(float,replace([final_limit],'NA','0')))),0) from ng_rlos_EligAndProdInfo with (nolock) where Wi_Name ='" + wiName + "') +  ( select isNull((Sum(convert(float,replace([sanctionLimit],'NA','0')))),0) from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where Wi_Name ='" + wiName + "' and ODDesc is not null and AcctStat in ('A','ACTIVE')) as aggregateExposure");
								
				List < List < String >> aggregate_exposed = ExecuteSelectQueryGetList("select (select isNull((Sum(convert(float,replace([TotalOutstandingAmt],'NA','0')))),0) as TotalOutstandingAmt from ng_RLOS_CUSTEXPOSE_loanDetails with (nolock) where Consider_For_Obligations='true' and LoanStat in ('A','ACTIVE') and wi_name ='"+wiName+"')+ (select isNull((Sum(convert(float,replace([TotalOutstandingAmt],'NA','0')))),0)  as TotalOutstandingAmt from ng_RLOS_CUSTEXPOSE_loanDetails with (nolock) where  Consider_For_Obligations='true' and LoanStat ='Pipeline' and wi_name ='"+wiName+"')  +(select isNull((Sum(convert(float,replace([OutstandingBalance],'NA','0')))),0)   from ng_RLOS_CUSTEXPOSE_CardinstallmentDetails installment join ng_RLOS_CUSTEXPOSE_CardDetails  carddetails with (nolock) on carddetails.CardEmbossNum=installment.CardcrnNumber where   carddetails.Consider_For_Obligations='true' and carddetails.CardStatus in ('A','ACTIVE')  and carddetails.wi_name  ='"+wiName+"' and SchemeCardProd like '%LOC%')+  (select isNull((Sum(convert(float,replace([CreditLimit],'NA','0')))),0) as OutstandingAmt  from ng_RLOS_CUSTEXPOSE_CardDetails with (nolock) where  Consider_For_Obligations='true' and  CardStatus in ('A','ACTIVE') and wi_name  ='"+wiName+"' and  SchemeCardProd not like '%LOC%')+( select isNull((Sum(convert(float,replace([final_limit],  'NA','0')))),0) from ng_rlos_EligAndProdInfo with (nolock) where Wi_Name ='"+wiName+"')  +( select isNull((Sum(convert(float,replace([sanctionLimit],'NA','0')))),0) from  ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where Wi_Name ='"+wiName+"'  and ODDesc is not null and AcctStat in ('A','ACTIVE')) as aggregateExposure");
				
				String aggr_expo = aggregate_exposed.get(0).get(0);
				double aggreg = Double.parseDouble(aggr_expo);
				aggr_expo = String.format("%.2f", aggreg);
				param_json_object.put("aggregate_exposed", aggr_expo);
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "AggregateExposure :" + aggr_expo);
				responseObject.put("AggregateExposure", aggr_expo);
				param_json_object.put("aggregate_exposed", aggr_expo);

				JSONArray LoanDetailsArray = getData("select count(*) as count from ng_rlos_custexpose_loandetails where Wi_Name='" + wiName + "'", "count");
				JSONArray CardDetailsArray = getData("select count(*) as count from ng_rlos_custexpose_Carddetails where Wi_Name='" + wiName + "'", "count");
				if (LoanDetailsArray.size() > 0 && CardDetailsArray.size() > 0) {
					String xml_str = getInternalLiabDetails(wiName);
					param_json_object.put("AccountDetails", xml_str);
				}

				//if (isStringNotNullEmpty(param_json_object.get("customerDetails-cif").toString().trim())) {
					String InternalBureauData = InternalBureauData(wiName, param_json_object.get("customerDetails-cif").toString().trim(), param_json_object.get("fullName").toString().trim());
					param_json_object.put("InternalBureau", InternalBureauData);
				//}

				String InternalBouncedCheques = InternalBouncedCheques(wiName);
				param_json_object.put("InternalBouncedCheques", InternalBouncedCheques);

				String InternalBureauIndividualProducts = InternalBureauIndividualProducts(wiName, param_json_object.get("employdetails-check-kompass").toString().trim(), param_json_object);
				param_json_object.put("InternalBureauIndividualProducts", InternalBureauIndividualProducts);

				String InternalBureauPipelineProducts = InternalBureauPipelineProducts(wiName);
				param_json_object.put("InternalBureauPipelineProducts", InternalBureauPipelineProducts);

				String ExternalBureauData = ExternalBureauData(wiName, param_json_object.get("customerDetails-cif").toString().trim(), param_json_object.get("fullName").toString().trim());
				param_json_object.put("ExternalBureau", ExternalBureauData);

				String ExternalBouncedCheques = ExternalBouncedCheques(wiName, param_json_object.get("customerDetails-cif").toString().trim());
				param_json_object.put("ExternalBouncedCheques", ExternalBouncedCheques);

				String ExternalBureauIndividualProducts = ExternalBureauIndividualProducts(wiName, param_json_object.get("customerDetails-cif").toString().trim(), param_json_object.get("requested_product").toString().trim(),param_json_object.get("eligibilitydetails-takeoverbank").toString().trim());
				String Manual_add_Liab = ExternalBureauManualAddIndividualProducts(param_json_object);
				param_json_object.put("ExternalBureauIndividualProducts", ExternalBureauIndividualProducts + Manual_add_Liab);

				String ExternalBureauPipelineProducts = ExternalBureauPipelineProducts(wiName, param_json_object.get("customerDetails-cif").toString().trim());
				param_json_object.put("ExternalBureauPipelineProducts", ExternalBureauPipelineProducts);
			}
			// include json in url
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "dectech final xml for input " + param_json_object.toString());
			//param_json_object=removeEmptyJsontags(param_json_object);

			//String paramjsonstring=removeEmptyJsontags(param_json_object).toString();
			//LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "AFTER REMOVING EMPTY JSON AND XML TAGS " + paramjsonstring);
			String decoded = param_json_object.toString().replaceAll(
					"customerDetails-cif", "cif").replaceAll("gaurantor-cif", "cif").replaceAll("&", "%26");
			/*Iterator<?> iter = param_json_object.entrySet().iterator();
   JSONObject final_json_object = new JSONObject();
   while (iter.hasNext()) {
   	Entry<String, Object> entry = (Entry<String, Object>) iter.next();
   	Object obj = entry.getValue();
   	if(!"".equals(obj)){
   		final_json_object.put(entry.getKey(), obj);
   	}
   }
   String decoded = (final_json_object.toString()).replaceAll(
   		"customerDetails-cif", "cif").replaceAll("gaurantor-cif","cif").replaceAll("&", "%26");*/
			//baseurl = baseurl + "?cabinetName=cas&wrapperIP=10.15.11.147&wrapperPort=2809&appServerType=WebSphere&SeesionId=" + user.getAuthenticationToken() + "&activityName=mobility&is_mobility=Y&wi_name=" + param_json_object.get("processId").toString().trim();
			String encoded = URLEncoder.encode(decoded, "UTF-8");
			//url = baseurl + "&request_name=" + callname + "&operation_type=" + operationtype + "&param_json=" + encoded;
			//  added by abhishek for JSP to JAVA code change start
			JSONObject json = new JSONObject();
		    json.put("callname",requestObject.getCallname());
		    json.put("operationtype", requestObject.getOperationtype());
		    json.put("is_mobility", "Y");
		    json.put("sessionId", user.getAuthenticationToken());
		    json.put("cabinetName",cabinetName);
		    json.put("wrapperIP",serverAddress );
		    json.put("wrapperPort",serverPort );
		    json.put("appServerType",serverType );
		    json.put("MQServerAddress",MQServerAddress );
		    json.put("MQServerPort",MQServerPort );
		    json.put("wi_name", param_json_object.get("processId").toString().trim());
		    json.put("ws_name", "mobility" );
		    LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "json : = " + json);
			
			// added by abhishek for JSP to JAVA code change end
			String result = "";
			String result_to_parse = "";
			if (callname.trim().equalsIgnoreCase("FINANCIAL_SUMMARY")) {
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "[callWebService financial summary initiated] "+ new Date().toString());
				JSONParser parser = new JSONParser();
				JSONObject inputjson = (JSONObject) parser.parse(requestObject.getInputjsonstring());
				ArrayList < String > list = getOperationName("NG_RLOS_FINANCIAL_SUMMARY", inputjson.get("Category").toString(), inputjson.get("employmenttype").toString());
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "\n " + callname + "  list : = " + list);
				//SELECT CustId FROM ng_rlos_cif_detail where cif_wi_name='CAS-0000005237-PROCESS'

				ArrayList < String > ciflist = getCifListfromArray(getData("SELECT CustId as 'cif-list' FROM ng_rlos_cif_detail where cif_wi_name='" + inputjson.get("processId").toString() + "'", "cif-list"));

				//ArrayList<String> ciflist = new ArrayList<String>(Arrays.asList(inputjson.get("cif-list").toString().split(",")));
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "\n " + callname + "  ciflist : = " + ciflist);
				HashMap < String, String > recordmap = ExecuteSelectquery("select CifId,acctid from ng_RLOS_CUSTEXPOSE_AcctDetails where Wi_Name = '" + inputjson.get("processId").toString() + "'");
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "\n " + callname + "  recordmap : = " + recordmap);
				XMLParser finparser = new XMLParser();
				Set < String > keyset = recordmap.keySet();
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "\n " + callname + "  keyset : = " + keyset);
				//List<String> valuelist = new ArrayList<String>(recordmap.values());
				// for(String cif : ciflist){
				// gaurav-s changed for Asynch Calls
				//				ExecutorService service;
				//				service = Executors.newFixedThreadPool(50); // 50 consecutive
				// call support
				// to hold future object
				HashMap < String, String > resultmap = new HashMap < String, String > ();
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "\n " + callname + "  resultmap : = " + resultmap);
				int counter = 0;

				for (String cif: ciflist) { // for ciflist from tab
					for (String accountId: keyset) { // for account id from db
						// query
						for (String listelement: list) { // for operation type
							// from query

							url = baseurl + "&request_name=" + listelement.toString();
							String paramjsonstr = "{cif:" + "\"" + cif.toString() + "\"" + ",OperationType:" + listelement.toString() + ",acct-id:" + "\"" + accountId + "\"" + ",\"Customer_Type\":\"Individual_CIF\"}".replaceAll("&", "%26");
							url = url + "&param_json=" + new org.json.JSONObject(paramjsonstr).toString();
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "\n " + callname + "  TO HIT : = " + url);
							result = callObject.request(url, isproxyenabled, ip, port, proxy_username, proxy_password);
							//							Future<String> task;
							//							ServiceExecuter obj = new ServiceExecuter(url,isproxyenabled, ip, port, proxy_username,proxy_password);
							//							task = service.submit(obj);
							resultmap.put(cif + "|" + accountId + "|" + listelement, result); // cif|account|SALDET
							counter++;
						}
					}
				}
				String listelement = "";
				String ciftodb = "";
				String accntidtodb = "";
				for (String compoundkey: resultmap.keySet()) {
					ciftodb = compoundkey.split("|")[0];
					accntidtodb = compoundkey.split("|")[1];
					listelement = compoundkey.split("|")[2];
					result = resultmap.get(compoundkey).toString();
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "\n " + callname + "::::: " + compoundkey + " RESULT : = " + result);
					finparser.setInputXML(result);
					HashMap < String, String > headerprop = new HashMap < String, String > ();
					headerprop.put("SeesionId", user.getAuthenticationToken());
					headerprop.put("is_mobility", "Y");
					headerprop.put("request_name", callname);
					headerprop.put("result", result);
					headerprop.put("wi_name", inputjson.get("processId").toString());
					headerprop.put("prod", inputjson.get("select-product-value").toString());
					headerprop.put("subprod", inputjson.get("Category").toString());
					headerprop.put("cifId", ciftodb.toString());
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "\n HEADER PROPERTY : = " + headerprop.toString());
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "\n CUST_EXPOSURE URL TO HIT : = " + custexpojspurl);
					callObject.postCall(custexpojspurl, headerprop);
					if (ciftodb.equalsIgnoreCase(inputjson.get("customerDetails-cif").toString().trim()) && ((listelement.equalsIgnoreCase("SALDET") || listelement.equalsIgnoreCase("AVGBALDET")))) {
						handleFinancialSummaryResponse(result, responseObject, listelement);
						//						toreturn.setOutputjsonstring(responseObject.toString());
					}
				}
				String query = "select isNull(((Sum(convert(float,replace([jan],'NA','0'))))+(Sum(convert(float,replace([feb],'NA','0'))))+(Sum(convert(float,replace([mar],'NA','0'))))+(Sum(convert(float,replace([apr],'NA','0'))))+(Sum(convert(float,replace([may],'NA','0'))))+(Sum(convert(float,replace([jun],'NA','0'))))+(Sum(convert(float,replace([jul],'NA','0'))))+(Sum(convert(float,replace([aug],'NA','0'))))+(Sum(convert(float,replace([sep],'NA','0'))))+(Sum(convert(float,replace([oct],'NA','0'))))+(Sum(convert(float,replace([nov],'NA','0')))) +(Sum(convert(float,replace([dec],'NA','0'))))),0) as AVGBALDET from ng_rlos_FinancialSummary_AvgBalanceDtls where WI_Name ='" + wiName + "'";
				List < List < String >> resultAvgBal = ExecuteSelectQueryGetList(query);

				String query2 = "select isNull((Sum(convert(float,replace([TotalCrAmt],'NA','0')))),0) as TotalCreditTurnover, case when isNull(max(AvgCrTurnOver),0) like '' then '0' else isNull(max(AvgCrTurnOver),0) end  as AvgCrTurnOver from ng_rlos_FinancialSummary_TxnSummary where WI_Name ='" + wiName + "'";
				List < List < String >> resultTurnOver = ExecuteSelectQueryGetList(query2);

				if (resultAvgBal != null && !resultAvgBal.isEmpty()) {
					responseObject.put("cmplx_IncomeDetails_AvgBal", resultAvgBal.get(0).get(0));
				}
				if (resultTurnOver != null && !resultTurnOver.isEmpty()) {
					responseObject.put("cmplx_IncomeDetails_CredTurnover", resultTurnOver.get(0).get(0));
					responseObject.put("cmplx_IncomeDetails_AvgCredTurnover", resultTurnOver.get(0).get(1));
				}
				toreturn.setOutputjsonstring(responseObject.toString());
				//				service.shutdownNow();
			} else {
				if (callname.equalsIgnoreCase("INTERNALEXPOSURE") || callname.equalsIgnoreCase("EXTERNALEXPOSURE") || callname.equalsIgnoreCase("CollectionsSummary")) {
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "[callWebService "+callname+" initiated] "+ new Date().toString());
					JSONParser parser = new JSONParser();
					JSONObject inputjson = (JSONObject) parser.parse(requestObject.getInputjsonstring());
					url = baseurl + "&request_name=" + callname + "&param_json=" +
							URLEncoder.encode((requestObject.getInputjsonstring()).replaceAll("customerDetails-cif", "cif").replaceAll(
									"gaurantor-cif", "cif").replaceAll("&", "%26"), "UTF-8");
				}
				// url=URLEncoder.encode(url, "UTF-8");
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "param_json_object: = " + param_json_object);
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "encoded: = " + encoded);
				WebServicerequesthandler requesthandler = new WebServicerequesthandler();
				JSONParser parser1 = new JSONParser(); 
				JSONObject param_json = (JSONObject) parser1.parse(param_json_object.toString().replaceAll("customerDetails-cif", "cif").replaceAll("gaurantor-cif", "cif"));
				param_json_object = param_json;
				result = requesthandler.getInputXMLValues(json,param_json_object);
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "  result : = " + result);
				/*LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "\n " + callname + "URL TO HIT: = " + url);
				result = callObject.request(url, isproxyenabled, ip, port, proxy_username, proxy_password);
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "\n" + callname + " Result: = " + result);*/
				if (!result.isEmpty()) {
					WebServiceResponseHandler responsehandler = new WebServiceResponseHandler();
					result = result.trim().replaceAll("&lt;", "<").replaceAll("&gt;", ">");
					xmlparser.setInputXML(result);
					result_to_parse = xmlparser.getValueOf("MQ_RESPONSE_XML");
					if (result_to_parse.trim().equalsIgnoreCase("INVALID SESSION")) {
						throw new Exception("INVALID SESSION");
					}
					if (callname.trim().equalsIgnoreCase("EID_Genuine")) {
						String ReturnCode = xmlparser.getValueOf("ns3:StatusCode");
						String Returndesc = xmlparser.getValueOf("ns3:ServiceStatusDesc");
						if (ReturnCode.equalsIgnoreCase("s") && Returndesc.equalsIgnoreCase("Valid")) {
							responseObject.put("StatusCode", ReturnCode);
							responseObject.put("Message", Returndesc);
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, callname + " StatusCode" + ReturnCode + ", Message" + Returndesc);
						} else {
							responseObject.put("StatusCode", ReturnCode);
							responseObject.put("Message", Returndesc);
						}
						toreturn.setOutputjsonstring(responseObject.toString());
						return toreturn;
					} else if (callname.trim().equalsIgnoreCase("DECTECH")) {
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "[callWebService eligibility initiated] "+ new Date().toString());
						if (param_json_object.get("CallType").toString().trim().equals("CA")) {
							String DBR = xmlparser.getValueOf("Output_Existing_DBR");
							String TAI = xmlparser.getValueOf("Output_TAI");
							String DBRNetSalary = xmlparser.getValueOf("Output_Net_Salary_DBR");
							String InterestRate = xmlparser.getValueOf("Interest_Rate");
							responseObject.put("DBR", DBR);
							responseObject.put("TAI", TAI);
							responseObject.put("DBRNetSalary", DBRNetSalary);
							responseObject.put("InterestRate", InterestRate);
							toreturn.setOutputjsonstring(responseObject.toString());
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "[callWebService eligibility if completed] "+ new Date().toString());
							return toreturn;
						} else {
							JSONObject returnVal = dectechfromeligbility(result, param_json_object, wiName, "cas", user.getAuthenticationToken(), "10.15.11.147", Short.parseShort("2809"));
							toreturn.setOutputjsonstring(returnVal.toString());
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "[callWebService eligibility else completed] "+ new Date().toString());
							return toreturn;
						}
					} else if (!xmlparser.getValueOf("ReturnCode").equalsIgnoreCase("0000")) {
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, callname + " REQUEST STATUS - UNSUCCESSFUL");
						responseObject.put("StatusCode", xmlparser.getValueOf("ReturnCode"));
						responseObject.put("Message", xmlparser.getValueOf("ReturnDesc"));
						//gaurav-s added the code to return query result in case of collection summary failure response
						if (callname.equalsIgnoreCase("CollectionsSummary")) {
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "[callWebService CollectionsSummary initiated] "+ new Date().toString());
							JSONParser parser = new JSONParser();
							JSONObject inputjson = (JSONObject) parser.parse(requestObject.getInputjsonstring());
							setCollectionSummaryResposne(responseObject, inputjson);
							cardInstallmentCall(baseurl, inputjson, custexpojspurl, isproxyenabled, ip, port, proxy_username, proxy_password);
						}
						toreturn.setOutputjsonstring(responseObject.toString());
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "[callWebService CollectionsSummary completed] "+ new Date().toString());
						return toreturn;
					} else {
						// Successfull result handling
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, callname + " REQUEST STATUS - SUCCESSFUL");
						Connection conn = nemfDbconnection();
						responseObject = responsehandler.getOutputXMLValues(result_to_parse, callname, conn);
						if (callname.trim().equalsIgnoreCase("INTERNALEXPOSURE") || callname.equalsIgnoreCase("CollectionsSummary") || callname.equalsIgnoreCase("EXTERNALEXPOSURE")) {
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "[callWebService "+callname.trim()+" initiated] "+ new Date().toString());
							JSONParser parser = new JSONParser();
							JSONObject inputjson = (JSONObject) parser.parse(requestObject.getInputjsonstring());
							HashMap < String, String > headerprop = new HashMap < String, String > ();
							String winame = inputjson.get("processId").toString();
							headerprop.put("SeesionId", user.getAuthenticationToken());
							headerprop.put("is_mobility", "Y");
							headerprop.put("request_name", callname);
							headerprop.put("result", result);
							headerprop.put("wi_name", winame);
							headerprop.put("prod", "");
							headerprop.put("subprod", "");
							headerprop.put("cust_type", inputjson.get("Customer_Type").toString());
							headerprop.put("cifId", inputjson.get("customerDetails-cif").toString());
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, callname + "\n HEADER PROPERTY : = " + headerprop.toString());
							CustExpose CE = new CustExpose();
							String Out = CE.CustExpose_datasave(result,winame,"","",inputjson.get("customerDetails-cif").toString(),"Y",user.getAuthenticationToken(),"","",inputjson.get("Customer_Type").toString(),cabinetName,serverAddress,serverPort,serverType);
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, callname + "\n Out : = " + Out);
							//callObject.postCall(custexpojspurl, headerprop);

							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "\n CUST_EXPOSURE URL TO HIT : = " + custexpojspurl);
							if (callname.equalsIgnoreCase("INTERNALEXPOSURE")) {
								LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "\n Inside INTERNALEXPOSURE Db call for response: ");
								responseObject.put("status", "success");
							} else if (callname.equalsIgnoreCase("EXTERNALEXPOSURE")) {
								LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "\n Inside EXTERNALEXPOSURE Db call for response: ");
								JSONArray externalloandetails_datavalues = getData(
										"select 'ng_rlos_cust_extexpo_loanDetails' as table_name, LoanType as TypeofContract, Liability_type as LiabilityType, agreementid as ProviderNo, totalamt as LimitAmtFin," +
												"Paymentsamt as EMI,Take_Over_Indicator as TakeoverIndicator,Take_Amount as TakeoverAmount,'' as CACIndicator,'' as QCAmt," +
												"'' as QCEMI,OutstandingAmt as OutstandingAmt,Consider_For_Obligations as ConsiderForObligations,'' as Remarks,concat((select description from ng_master_Aecb_Codes where code = WriteoffStat),'-',WriteoffStatDt) as WorstStatus,'' as cleanfunded,'' as finalCleanFunded,MonthsOnBook from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Wi_Name = '" + winame + "' and LoanStat not in  ('Pipeline','Closed') order by Liability_type desc",
										"TypeofContract,LiabilityType,ProviderNo,LimitAmtFin,EMI,TakeoverIndicator,TakeoverAmount,CACIndicator,QCAmt,QCEMI,OutstandingAmt,ConsiderForObligations,Remarks,WorstStatus,cleanfunded,finalCleanFunded,MonthsOnBook");
								responseObject.put("externalloandetails", externalloandetails_datavalues);

								JSONArray externalcarddetails_datavalues = getData(
										"select 'ng_rlos_cust_extexpo_CardDetails' as table_name,CardType as TypeofContract,Liability_type as LiabilityType,CardEmbossNum as ProviderNo,totalamount as LimitAmtFin,PaymentsAmount as EMI,Take_Over_Indicator as TakeoverIndicator," +
										"'' as TakeoverAmount,CAC_Indicator as CACIndicator,QC_Amt as QCAmt,QC_EMI as QCEMI,CurrentBalance as OutstandingAmt,Consider_For_Obligations as ConsiderForObligations,'' as Remarks,concat((select description from ng_master_Aecb_Codes where code = WriteoffStat),'-',WriteoffStatDt) as WorstStatus,'' as cleanfunded,'' as finalCleanFunded,MonthsOnBook from ng_rlos_cust_extexpo_cardDetails with (nolock) where wi_name='" + winame + "' and CardStatus not in ('Pipeline','Closed') order by Liability_type desc",
								"TypeofContract,LiabilityType,ProviderNo,LimitAmtFin,EMI,TakeoverIndicator,TakeoverAmount,CACIndicator,QCAmt,QCEMI,OutstandingAmt,ConsiderForObligations,Remarks,WorstStatus,cleanfunded,finalCleanFunded,MonthsOnBook");
								responseObject.put("externalcarddetails", externalcarddetails_datavalues);

								JSONArray externalaccountdetails_datavalues = getData(
										"select 'ng_RLOS_CUSTEXPOSE_AcctDetails' as table_name,CifId,Account_Type as LiabilityType,'OverDraft' as ProductType,ODDesc as TypeofCardLoan,AcctId as AgreementId,SanctionLimit as LimitAmtFin,'' as EMI,case when WriteoffStat='Y' then 'P2' else 'P6' end as GeneralStatus,'' as LimitIncrease,ClearBalanceAmount as OutstandingAmount,Consider_For_Obligations as ConsiderForObligations,'' as InterestRate,MonthsOnBook as MonthsOnBook,'' as NoOfRepaymentDone,ODType as TypeofOD,'' as PreviousLoanTAI,'' as PreviousLoanDBR from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where Wi_Name  ='" + inputjson.get("processId").toString() + "'  and ODType != ''",
										"table_name,CifId,LiabilityType,ProductType,TypeofCardLoan,AgreementId,LimitAmtFin,EMI,GeneralStatus,LimitIncrease,OutstandingAmount,ConsiderForObligations,InterestRate,MonthsOnBook,NoOfRepaymentDone,TypeofOD,PreviousLoanTAI,PreviousLoanDBR");
								responseObject.put("externalaccountdetails", externalaccountdetails_datavalues);

								responseObject.put("status", "success");
							} else if (callname.equalsIgnoreCase("CollectionsSummary")) {
								setCollectionSummaryResposne(responseObject, inputjson);
								cardInstallmentCall(baseurl, inputjson, custexpojspurl, isproxyenabled, ip, port, proxy_username, proxy_password);
							}
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "[callWebService "+callname.trim()+" initiated] "+ new Date().toString());
							
						} else if (callname.trim().equalsIgnoreCase("CUSTOMER_ELIGIBILITY")) {
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "[callWebService CUSTOMER_ELIGIBILITY initiated] "+ new Date().toString());
							
							multiplecifresponsehandler multi_cif_handler = new multiplecifresponsehandler();
							multi_cif_handler.parse_cif_eligibility(result, responseObject);
							
							
						} else if (callname.trim().equalsIgnoreCase("CUSTOMER_SEARCH_REQUEST")) {

							String id = ExecuteDbqueryReturn("INSERT INTO WFMAPPINGTABLE_parentMapping  DEFAULT VALUES");
							result += "<childmapping>" + id + "</childmapping>";
							result += "<world_wi_name>" + param_json_object.get("processId").toString().trim() + "</world_wi_name>";

							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, callname + "\nResponse for childmapping query" + id);
							String query = prepQueryforWC("CUSTOMER_SEARCH_REQUEST", result, "ng_RLOS_GR_WorldCheck");
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, callname + "\nng_RLOS_GR_WorldCheck Insert query " + query);
							ExecuteDbquery(query);
							String query2 = "INSERT INTO ng_RLOS_WorldCheck (parentmapping,wi_name) values('" + id + "','" + param_json_object.get("processId") + "')";
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, callname + "\nng_RLOS_WorldCheck Insert query " + query2);
							ExecuteDbquery(query2);
						} else if (callname.trim().equalsIgnoreCase("CUSTOMER_DETAILS")) {
							/*	kush bharadwaj	
							 * 	02-07-2018
							 * 	return NRI flag value
							 * */
							if(result.contains("<CustomerNRIFlag>")){
								String nriFlag = (result.contains("<CustomerNRIFlag>")) ? result.substring(result.indexOf("<CustomerNRIFlag>") + "</CustomerNRIFlag>".length() - 1, result.indexOf("</CustomerNRIFlag>")) : "";
								LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, nriFlag + "CustomerNRIFlag 2 ");
								if (nriFlag.trim().equalsIgnoreCase("y")) {
									LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, nriFlag + "CustomerNRIFlag 3 ");
									responseObject.put("CustomerNRIFlag", "Y");
			                    }
			                    else{
			                        responseObject.put("CustomerNRIFlag", "N");
			                    }
  
					        }							
					        else{
					        	LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, callname + "CustomerNRIFlag 4 ");
					            responseObject.put("CustomerNRIFlag", "");
					        }
							
							/*	END	*/
							String isStaff = (result.contains("<IsStaff>")) ? result.substring(result.indexOf("<IsStaff>") + "</IsStaff>".length() - 1, result.indexOf("</IsStaff>")) : "";
							if (isStaff.trim().equalsIgnoreCase("y")) {
								responseObject.put("IsStaff", "Y");
							} else {
								handleRepeatedTags(responseObject, callname.trim(), result);
								String ResideSince = (result.contains("<ResideSince>")) ? result.substring(result.indexOf("<ResideSince>") + "</ResideSince>".length() - 1, result.indexOf("</ResideSince>")) : "";
								responseObject.put("ResideSince", ResideSince);
							}
							
						}
					}
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "[callWebService "+callname+" completed] "+ new Date().toString());
					
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, callname + "\nResponse From WebServiceresponsehandler" + responseObject.toString());
					toreturn.setOutputjsonstring(responseObject.toString());
				} else {
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, callname + "Web Service Result Empty ");
					throw new Exception("Web Service Result Empty");
				}
			}
		} catch (Exception e) {
			//e.printStackTrace();
			writePrintStackTrace(e);
			throw new Exception(e);
		}
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "[callWebService Completed]"+ new Date().toString());
		return toreturn;
	}
		/***
		 * Description		:	For writing e.printStackTrace use writePrintStackTrace() function e.g. Exception/Throwable
		 * Author			:	Sumit Balyan
		 * Date				:	10/08/2018					
		 * 
		 *  **/
	 private void writePrintStackTrace(Exception e) {
	        StringWriter sw = new StringWriter();
	        PrintWriter pw = new PrintWriter(sw);
	        e.printStackTrace(pw);
	        String msg = sw.toString();
	        LogMe.logMe(LogMe.LOG_LEVEL_ERROR, msg);
	    }

	private void writePrintStackTrace(Throwable t) {
	            // TODO: handle exception
	            LogMe.logMe(LogMe.LOG_LEVEL_ERROR,
	                    "EXCEPTION OCCOURED while submitting->> t" + t);
	            StringWriter sw = new StringWriter();
	            PrintWriter pw = new PrintWriter(sw);
	            t.printStackTrace(pw);
	            // sw.toString();
	            LogMe.logMe(LogMe.LOG_LEVEL_ERROR,
	                    "EXCEPTION OCCOURED while submitting->>" + sw);
	}
	
	//26022018 - for crash logging
	  public ResponseEntity  ExecuteOnDb(IntegrationEntity request) throws Exception{
	        ResponseEntity toreturn=new ResponseEntity();
	        JSONObject responsejson=new JSONObject();
	        if(request.getCallname().equalsIgnoreCase("logInTable")){
	            if(logInTable(request)){
	                responsejson.put("status", "success");
	                responsejson.put("message", "successfully logged  in table");
	            }    
	            else{
	                responsejson.put("status", "failure");
	                responsejson.put("message", "Error while logging in table");
	            }
	        }
	        //gaurav-s added on 19032018 start
	        /*else if(request.getCallname().equalsIgnoreCase("sendsms")){
	            if(insertSms(request)){
	                responsejson.put("status", "success");
	                responsejson.put("message", "successfully inserted in sms table");
	            }    
	            else{
	                responsejson.put("status", "failure");
	                responsejson.put("message", "Error while inserting  in sms table");
	            }
	        }else{
	            throw new Exception("Invalid Call Name in Input Json");
	        }*/
	        //gaurav-s added on 19032018 end
	        toreturn.setOutputjsonstring(responsejson.toString());
	        return toreturn;
	    }

	//gaurav-s added on 19032018 start
	    private boolean insertSms(IntegrationEntity request) throws Exception{
	        boolean toreturn=false;
	        try {
	            String inputJsonString=request.getInputjsonstring();
	            JSONParser parser=new JSONParser();
	            JSONObject inputjson=(JSONObject)parser.parse(inputJsonString);
	            String Query = inputjson.get("query").toString().trim();
	            ExecuteDbquery(Query.replaceAll("#~#", "'"));
	            toreturn=true;
	            } catch (Exception e) {
	            	writePrintStackTrace(e);
	                throw new Exception(e);
	            }
	        return toreturn;
	        
	    }


	//gaurav-s added on 19032018 end

	private boolean logInTable(IntegrationEntity request) throws Exception {
		boolean toreturn = false;
		try {
			String inputJsonString = request.getInputjsonstring();
			JSONParser parser = new JSONParser();
			JSONObject inputjson = (JSONObject) parser.parse(inputJsonString);
			String Query = inputjson.get("query").toString().trim();
			ExecuteDbquery(Query.replaceAll("#~#", "'"));
			toreturn = true;
		} catch (Exception e) {
			writePrintStackTrace(e);
			throw new Exception(e);
		}
		return toreturn;

	}
	public String putComma(String field) {
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Inside putComma()" + field);
		String limit = field.replaceAll(",", "");
		double amount = Double.parseDouble(limit);
		DecimalFormat myFormatter = new DecimalFormat("#,###.000");
		String convString = myFormatter.format(amount);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
				"Inside putComma(): Coverted String is:" + convString);
		return convString;
	}

	public JSONObject dectechfromeligbility(String outputResponse,
			JSONObject param_json_object, String wiName, String cabinetName,
			String sessionId, String jtsAddress, short jtsPort) {
		JSONObject responseObject = new JSONObject();
		try {
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"Inside dectechfromeligiblity method start:" );
			if (outputResponse.indexOf("<PM_Reason_Codes>") > -1 && outputResponse.indexOf("<PM_Outputs>") > -1) {
				String squery = "";
				String Output_TAI = "";
				String Output_Final_DBR = "";
				String Output_Existing_DBR = "";
				String Output_Eligible_Amount = "";
				String Output_Affordable_EMI = "";
				String Output_Delegation_Authority = "";
				String Grade = "";
				String Output_Interest_Rate = "";
				String Output_Net_Salary_DBR = "";
				double cac_calc_limit = 0.0;
				String cac_calc_limit_str = null;
				String DeviationReferCode = "";
				int a=0;
				int b=0;
				int c=0;

				String strOutputXml = "";

				String ReqProd = param_json_object.get("requested_product")
						.toString().trim();
				String subProd = param_json_object.get("sub_product")
						.toString().trim();
				String appType = param_json_object.get("application_type")
						.toString().trim();
				
				// sending Aggregate exposure value for proc-9704
				String AggregateExposure  = param_json_object.get("aggregate_exposed").toString().trim();
				responseObject.put("Liability_AggregateExposure", AggregateExposure);
				
				// Setting the value in ELIGANDPROD info
				if (outputResponse.contains("Output_TAI")) {
					Output_TAI = outputResponse.substring(outputResponse.indexOf("<Output_TAI>") + 12, outputResponse.indexOf("</Output_TAI>"));
					if (Output_TAI != null) {
						try {
							responseObject.put("New_TAI", Output_TAI);
							responseObject.put("EligibilityAndProductInfo_FinalTAI", Output_TAI);

						} catch (Exception e) {
							writePrintStackTrace(e);
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Exception:" + e.getMessage());
						}
					}
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "output Output_TAI" + Output_TAI);
				}
				if (outputResponse.contains("Output_Eligible_Cards")) {
					try {
						// gaurav-s changed parsing code 09102017
						XMLParser parser = new XMLParser();
						parser.setInputXML(outputResponse);
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Output_Eligible_Cards IN XML ::" + parser.getValueOf("Output_Eligible_Cards"));
						responseObject.put("Output_Eligible_Cards", parser.getValueOf("Output_Eligible_Cards"));
						String Output_Eligible_Cards = outputResponse.substring(outputResponse.indexOf("<Output_Eligible_Cards>") + 23, outputResponse.indexOf("</Output_Eligible_Cards>"));
						Output_Eligible_Cards = Output_Eligible_Cards.substring(2, Output_Eligible_Cards.length() - 2);
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "after removing the starting and Trailing:" + Output_Eligible_Cards);
						String query = "delete from ng_rlos_IGR_Eligibility_CardLimit where WI_NAME ='" + wiName + "'";
						ExecuteDbquery(query);
						//String strInputXML=ExecuteQuery_APdelete("ng_rlos_IGR_Eligibility_CardLimit","WI_NAME ='"+ wiName + "'",cabinetName,sessionId);
						//strOutputXml = DMSCallBroker.execute(strInputXML,jtsAddress, jtsPort, 1);
						//strOutputXml =WFCallBroker.execute(strInputXML, jtsAddress, jtsPort, 0);
						//strOutputXml = makeCall(jtsAddress, jtsPort,strInputXML.toString().replaceAll("&", "&amp;"));
						//Rajeshwar: changes made as AP delete was not working
						String querytodel = "delete from ng_rlos_IGR_Eligibility_CardProduct where WI_NAME ='" + wiName + "'";
						ExecuteDbquery(querytodel);
						//LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"APdelete Output xml:"+strOutputXml);
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Query executed for delete: " + query);

						String[] Output_Eligible_Cards_Arr = Output_Eligible_Cards.split("\\},\\{");
						if (Output_Eligible_Cards_Arr.length > 0) {
							for (int i = 0; i < Output_Eligible_Cards_Arr.length; i++) {
								String[]
										Output_Eligible_Cards_Array = Output_Eligible_Cards_Arr[i].split(",");
								LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Output_Eligible_Cards_Array:" + Output_Eligible_Cards_Array);
								if(Output_Eligible_Cards_Array.length==3){
								String[] Card_Prod = Output_Eligible_Cards_Array[0].split(":");
								String[] Limit = Output_Eligible_Cards_Array[1].split(":");
								String[] flag = Output_Eligible_Cards_Array[2].split(":");
								String Card_Product = Card_Prod[1].substring(1, Card_Prod[1].length() - 1);
								String LIMIT = Limit[1];
								String FLAG = flag[1].substring(1, flag[1].length() - 1);
								String combined_limitquery = "select COMBINEDLIMIT_ELIGIBILITY from ng_master_cardProduct with (nolock)  where CODE = '" + Card_Product + "'";

								LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "combined_limitquery:" + combined_limitquery);

								List < List < String >> combined_limitqueryXML = ExecuteSelectQueryGetList(combined_limitquery);
								String combined_limit = "";
								if (!combined_limitqueryXML.isEmpty()) {
									combined_limit = combined_limitqueryXML.get(0).get(0);
								}
								LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Card_Prod:" + Card_Prod[1]);
								LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Limit:" + Limit[1]);
								LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "flag:" + flag[1]);
								//Rajeshwar: renamed query to quety1 because created query variable for delete above
								String query1 = "INSERT INTO ng_rlos_IGR_Eligibility_CardLimit(Card_product,Eligible_limit,wi_name,flag,combined_limit) VALUES ('" + Card_Product + "','" + LIMIT + "','" + wiName + "','" + FLAG + "','" + combined_limit + "')";

								LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "QUERY:" + query1);
								ExecuteDbquery(query1);
								}
							}
						}
					} catch (Exception e) {
						writePrintStackTrace(e);
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Exception occurred in elig dectech" + e.getMessage());
						//e.printStackTrace();
					}
				}
				if (outputResponse.contains("Output_Final_DBR")) {

					Output_Final_DBR = outputResponse.substring(outputResponse.indexOf("<Output_Final_DBR>") + 18, outputResponse.indexOf("</Output_Final_DBR>"));
					if (Output_Final_DBR != null) {
						responseObject.put("EligibilityAndProductInfo_FinalDBR", Output_Final_DBR);
					}
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "output EligibilityAndProductInfo_FinalDBR" + Output_Final_DBR);
				}
				if (outputResponse.contains("Output_Interest_Rate")) {
					Output_Interest_Rate = outputResponse.substring(outputResponse.indexOf("<Output_Interest_Rate>") + 22, outputResponse.indexOf("</Output_Interest_Rate>"));
					if (Output_Interest_Rate != null) {
						// added by abhishek to calculate EMI
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "EMI by abhishek");
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "TENURE" + param_json_object.get("eligibilitydetails-tenor").toString().trim());
						String tenure = param_json_object.get("eligibilitydetails-tenor").toString().trim();
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Limit" + param_json_object.get("eligibilitydetails-finallimit").toString().trim());
						String finalLimit = param_json_object.get("eligibilitydetails-finallimit").toString().trim();
						String Emi = "";
						if (!"".equals(tenure) && !"".equals(finalLimit)) {
							BigDecimal tenor = new BigDecimal(tenure);
							BigDecimal limit = new BigDecimal(finalLimit);
							BigDecimal InterestRate = new BigDecimal(Output_Interest_Rate);


							Emi = calculateEmi(tenor, limit, InterestRate);
						}
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "EMI calculated" + Emi);
						responseObject.put("EligibilityAndProductInfo_InterestRate", Output_Interest_Rate);
						responseObject.put("Emi", Emi);
					}
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "output EligibilityAndProductInfo_InterestRate" + Output_Interest_Rate);
				}

				if (outputResponse.contains("Output_Affordable_EMI")) {
					Output_Affordable_EMI = outputResponse.substring(outputResponse.indexOf("<Output_Affordable_EMI>") + 23, outputResponse.indexOf("</Output_Affordable_EMI>"));
					if (Output_Interest_Rate != null) {
						responseObject.put("EligibilityAndProductInfo_AffordableEMI", Output_Affordable_EMI);
					}
					try{
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "output EligibilityAndProductInfo_AffordableEMI" + Output_Affordable_EMI);
						if(!"".equalsIgnoreCase(Output_Affordable_EMI)){
							double tenor=Double.parseDouble(param_json_object.get("eligibilitydetails-tenor").toString().trim()==null||"".equalsIgnoreCase(param_json_object.get("eligibilitydetails-tenor").toString().trim())?"0":param_json_object.get("eligibilitydetails-tenor").toString().trim());
							double RateofInt=Double.parseDouble(Output_Interest_Rate==null|| "".equals(Output_Interest_Rate)?"0":Output_Interest_Rate);
							double out_aff_emi=Double.parseDouble(Output_Affordable_EMI);
							// check put to send cas calculated limited blank in case of CC when tenure is not there
							if(tenor!=0.0){
								cac_calc_limit=Math.round(Cas_Limit(out_aff_emi,RateofInt,tenor));
								cac_calc_limit_str = String.format("%.2f", cac_calc_limit);
							}else{
								cac_calc_limit_str = "";
							}
								
						}
						
						
						responseObject.put("CASCalculatedLimit", cac_calc_limit_str);
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "output CAS Calculated Limit" + cac_calc_limit_str);
						}
						catch(Exception e)
						{
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"Exception occured :"+e.getMessage());

						}
					
				}

				// Setting the value in ELIGANDPROD info
				// Setting the value in lIABILITY info
				if (outputResponse.contains("Output_Existing_DBR")) {
					Output_Existing_DBR = outputResponse.substring(outputResponse.indexOf("<Output_Existing_DBR>") + 21, outputResponse.indexOf("</Output_Existing_DBR>"));
					if (Output_Existing_DBR != null) {
						responseObject.put("Liability_New_DBR", Output_Existing_DBR);
					}
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "output Liability_New_DBR" + Output_Existing_DBR);
				}

				if (outputResponse.contains("Output_Net_Salary_DBR")) {
					Output_Net_Salary_DBR = outputResponse
							.substring(
									outputResponse
									.indexOf("<Output_Net_Salary_DBR>") + 23,
									outputResponse
									.indexOf("</Output_Net_Salary_DBR>"));;
									if (Output_Net_Salary_DBR != null) {
										responseObject.put("Liability_New_DBRNet",
												Output_Net_Salary_DBR);
									}
									LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
											"output Liability_New_DBRNet" + Output_Net_Salary_DBR);
				}
				// Setting the value in lIABILITY info
				// Setting the value in creditCard iFrame
				if (outputResponse.contains("Output_Delegation_Authority")) {
					Output_Delegation_Authority = outputResponse
							.substring(
									outputResponse
									.indexOf("<Output_Delegation_Authority>") + 29,
									outputResponse
									.indexOf("</Output_Delegation_Authority>"));;
									LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
											"output Output_Delegation_Authority" + Output_Delegation_Authority);
									responseObject.put("DeligationAuthority",
											Output_Delegation_Authority);
				}
				if (outputResponse.contains("Grade")) {
					Grade = outputResponse.substring(outputResponse
							.indexOf("<Grade>") + 7, outputResponse
							.indexOf("</Grade>"));;
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "output Grade" + Grade);
							responseObject.put("Grade", Grade);

				}
				if (outputResponse.contains("Output_Eligible_Amount")) {
					Output_Eligible_Amount = outputResponse
							.substring(
									outputResponse
									.indexOf("<Output_Eligible_Amount>") + 24,
									outputResponse
									.indexOf("</Output_Eligible_Amount>"));;
									LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
											"output Output_Eligible_Amount" + Output_Eligible_Amount);
									responseObject.put("EligibleLimit", Output_Eligible_Amount);
				}

				// Setting the value in creditCard iFrame
				if (ReqProd.equalsIgnoreCase("Credit Card")) {
					String query = "delete from ng_rlos_IGR_Eligibility_CardProduct where WI_NAME ='" + wiName + "'";
					ExecuteDbquery(query);
					// String strInputXML
					// =ExecuteQuery_APdelete("ng_rlos_IGR_Eligibility_CardProduct","WI_NAME ='"
					// + wiName + "'",cabinetName,sessionId);
					// strOutputXml = DMSCallBroker.execute(strInputXML,
					// jtsAddress, jtsPort, 1);
					// strOutputXml =
					// NGEjbClient.getSharedInstance().makeCall(jtsAddress,
					// String.valueOf(jtsPort), "WebSpere", strInputXML);
					// strOutputXml = WFCallBroker.execute(strInputXML,
					// jtsAddress, jtsPort, 0);
					// LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"strOutputXml is:"+strOutputXml);
					//
					// if(strOutputXml.contains("<MainCode>") &&
					// strOutputXml.contains("<Output>")){
					// String
					// mainCode=strOutputXml.substring(strOutputXml.indexOf("<MainCode>")+10,strOutputXml.indexOf("</MainCode>"));
					// String
					// rowUpdated=strOutputXml.substring(strOutputXml.indexOf("<Output>")+8,strOutputXml.indexOf("</Output>"));
					// LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"mainCode,rowUpdated is: "+mainCode+", "+rowUpdated);
					// }
				} else {
					String query = "delete from ng_rlos_IGR_Eligibility_PersonalLoan where WI_NAME ='" + wiName + "'";
					ExecuteDbquery(query);
					// String strInputXML
					// =ExecuteQuery_APdelete("ng_rlos_IGR_Eligibility_PersonalLoan","WI_NAME ='"
					// + wiName + "'",cabinetName,sessionId);
					// strOutputXml = WFCallBroker.execute(strInputXML,
					// jtsAddress, jtsPort, 0);
					// strOutputXml =
					// NGEjbClient.getSharedInstance().makeCall(jtsAddress,
					// String.valueOf(jtsPort), "WebSpere", strInputXML);
					// strOutputXml = DMSCallBroker.execute(strInputXML,
					// jtsAddress, jtsPort, 1);
					// LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"strOutputXml is:"+strOutputXml);
					//
					// if(strOutputXml.contains("<MainCode>") &&
					// strOutputXml.contains("<Output>")){
					// String
					// mainCode=strOutputXml.substring(strOutputXml.indexOf("<MainCode>")+10,strOutputXml.indexOf("</MainCode>"));
					// String
					// rowUpdated=strOutputXml.substring(strOutputXml.indexOf("<Output>")+8,strOutputXml.indexOf("</Output>"));
					// LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"mainCode,rowUpdated is: "+mainCode+", "+rowUpdated);
					// }
				}
				outputResponse = outputResponse.substring(outputResponse
						.indexOf("<ProcessManagerResponse>") + 24,
						outputResponse.indexOf("</ProcessManagerResponse>"));
				outputResponse = "<?xml version=\"1.0\"?><Response>" + outputResponse;
				outputResponse = outputResponse + "</Response>";
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
						"inside outpute get outputResponse" + outputResponse);

				handleRepeatedTags(responseObject, "DECTECH", outputResponse);
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
						"Output response for Dectech Eligibilty" + outputResponse);

				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				InputSource is = new InputSource(new StringReader(
						outputResponse));

				Document doc = builder.parse(is);
				doc.getDocumentElement().normalize();

				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, doc.getDocumentElement()
						.getNodeName());

				NodeList nList = doc.getElementsByTagName("PM_Reason_Codes");

				for (int List_len_num = 0; List_len_num < nList.getLength(); List_len_num++) {
					String Reason_Decision = "";
					String Category="";
					Node nNode = nList.item(List_len_num);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "\nCurrent Element :" + nNode.getNodeName());

					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						// System.out.println("Student roll no : " +
						// eElement.getAttribute("rollno"));
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
								"inside outpute get Decision_Objective" + eElement.getElementsByTagName(
										"Decision_Objective").item(0)
								.getTextContent());
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
								"inside outpute get Decision_Sequence_Number" + eElement.getElementsByTagName(
										"Decision_Sequence_Number")
								.item(0).getTextContent());
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
								"inside outpute get Sequence_Number" + eElement.getElementsByTagName(
										"Sequence_Number").item(0)
								.getTextContent());
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
								"inside outpute get Reason_Description" + eElement.getElementsByTagName(
										"Reason_Description").item(0)
								.getTextContent());

						String subprodquery="Select Description from ng_MASTER_SubProduct_PL where code='"+subProd+"'";
						String subprodCCquery="Select Description from ng_MASTER_SubProduct_CC where code='"+subProd+"'";
						String apptypequery="Select Description from ng_master_applicationtype where code='"+appType+"' and subprodflag='"+subProd+"'";
						List < List < String >> subprodqueryXML = ExecuteSelectQueryGetList(subprodquery);
						List < List < String >> apptypequeryXML = ExecuteSelectQueryGetList(apptypequery);
						List < List < String >> subprodCCqueryXML = ExecuteSelectQueryGetList(subprodCCquery);
						Reason_Decision = eElement.getElementsByTagName(
								"Reason_Decision").item(0).getTextContent();
						
						DeviationReferCode = DeviationReferCode + eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"|";
						
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
								"Value of Reason_Decision" + Reason_Decision);
						
						
						//Changes Done to eliminate Null pointer exception
						if (eElement.getElementsByTagName("Category").getLength()!=0){
							
							Category = eElement.getElementsByTagName("Category").item(0).getTextContent() ;
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"DectechCategory  Categoryis"+Category);
							
							if (Category.contains("A -")){
								a++;
									}
							else if (Category.contains("B -")){
								b++;
									}
							else{
								c++;
									}
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"DectechCategory  a is"+a);
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"DectechCategory  b is"+b);
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"DectechCategory  C is"+c);
							
							
							
							
						}
						//Changes Done to eliminate Null pointer exception
						
						if (List_len_num==0){
							if (ReqProd.equalsIgnoreCase("Credit Card")) {
								if ("D".equalsIgnoreCase(Reason_Decision)) {
									squery = "Insert into ng_rlos_IGR_Eligibility_CardProduct (S_no,Product,Card_Product,Eligible_Card,Decision,Declined_Reason,Delegation_Authorithy,Score_grade,wi_name,CACCalculatedLimit,category) values('" + List_len_num + "','Credit Card','" + subprodCCqueryXML.get(0).get(0) + "','" + Output_Eligible_Amount + "','Declined','" + eElement.getElementsByTagName(
											"Reason_Description").item(0)
											.getTextContent() + "-" + eElement.getElementsByTagName(
													"Reason_Code").item(0)
											.getTextContent() + "','" + Output_Delegation_Authority + "','" + Grade + "','" + wiName + "','"+cac_calc_limit_str+"','"+Category+"')";
								} else if ("R".equalsIgnoreCase(Reason_Decision)) {
									squery = "Insert into ng_rlos_IGR_Eligibility_CardProduct (S_no,Product,Card_Product,Eligible_Card,Decision,Deviation_Code_Refer,Delegation_Authorithy,Score_grade,wi_name,CACCalculatedLimit,category) values('" + List_len_num + "','Credit Card','" + subprodCCqueryXML.get(0).get(0) + "','" + Output_Eligible_Amount + "','Refer','" + eElement.getElementsByTagName(
											"Reason_Description").item(0)
											.getTextContent() + "-" + eElement.getElementsByTagName(
													"Reason_Code").item(0)
											.getTextContent() + "','" + Output_Delegation_Authority + "','" + Grade + "','" + wiName + "','"+cac_calc_limit_str+"','"+Category+"')";
								} else if ("A".equalsIgnoreCase(Reason_Decision)) {
									squery = "Insert into ng_rlos_IGR_Eligibility_CardProduct (S_no,Product,Card_Product,Eligible_Card,Decision,Deviation_Code_Refer,Delegation_Authorithy,Score_grade,wi_name,CACCalculatedLimit,category) values('" + List_len_num + "','Credit Card','" + subprodCCqueryXML.get(0).get(0) + "','" + Output_Eligible_Amount + "','Approve','" + eElement.getElementsByTagName(
											"Reason_Description").item(0)
											.getTextContent() + "-" + eElement.getElementsByTagName(
													"Reason_Code").item(0)
											.getTextContent() + "','" + Output_Delegation_Authority + "','" + Grade + "','" + wiName + "','"+cac_calc_limit_str+"','"+Category+"')";
								}
							} 
							else{
								if ("D".equalsIgnoreCase(Reason_Decision)) {
									squery = "Insert into ng_rlos_IGR_Eligibility_PersonalLoan (S_no,RequestedLoan,RequestedAppType,EligibleExposure,Decision,DeclinedReasonCode,DelegationAuthorithy,wi_name,CACCalculatedLimit,category) values('" + List_len_num + "','" + subprodqueryXML.get(0).get(0) + "','" +apptypequeryXML.get(0).get(0)+"','"+ Output_Eligible_Amount + "','Declined','" + eElement.getElementsByTagName(
											"Reason_Description").item(0)
											.getTextContent() + "-" + eElement.getElementsByTagName(
													"Reason_Code").item(0)
											.getTextContent() + "','" + Output_Delegation_Authority + "','" + wiName + "','"+cac_calc_limit_str+"','"+Category+"')";
								} else if ("R".equalsIgnoreCase(Reason_Decision)) {
									squery = "Insert into ng_rlos_IGR_Eligibility_PersonalLoan (S_no,RequestedLoan,RequestedAppType,EligibleExposure,Decision,ReferReasoncode,DelegationAuthorithy,wi_name,CACCalculatedLimit,category) values('" + List_len_num + "','" + subprodqueryXML.get(0).get(0) + "','" +apptypequeryXML.get(0).get(0)+"','"+ Output_Eligible_Amount + "','Refer','" + eElement.getElementsByTagName(
											"Reason_Description").item(0)
											.getTextContent() + "-" + eElement.getElementsByTagName(
													"Reason_Code").item(0)
											.getTextContent() + "','" + Output_Delegation_Authority + "','" + wiName + "','"+cac_calc_limit_str+"','"+Category+"')";
								} else if ("A".equalsIgnoreCase(Reason_Decision)) {
									squery = "Insert into ng_rlos_IGR_Eligibility_PersonalLoan (S_no,RequestedLoan,RequestedAppType,EligibleExposure,Decision,ReferReasoncode,DelegationAuthorithy,wi_name,CACCalculatedLimit,category) values('" + List_len_num + "','" + subprodqueryXML.get(0).get(0) + "','" +apptypequeryXML.get(0).get(0)+"','"+ Output_Eligible_Amount + "','Approve','" + eElement.getElementsByTagName(
											"Reason_Description").item(0)
											.getTextContent() + "-" + eElement.getElementsByTagName(
													"Reason_Code").item(0)
											.getTextContent() + "','" + Output_Delegation_Authority + "','" + wiName + "','"+cac_calc_limit_str+"','"+Category+"')";
								}
							}
						}
						else {
							if (ReqProd.equalsIgnoreCase("Credit Card")) {
								if ("D".equalsIgnoreCase(Reason_Decision)) {
									squery = "Insert into ng_rlos_IGR_Eligibility_CardProduct (S_no,Product,Card_Product,Eligible_Card,Decision,Declined_Reason,Delegation_Authorithy,Score_grade,wi_name,CACCalculatedLimit,category) values('" + List_len_num + "','Credit Card','" + subprodCCqueryXML.get(0).get(0) + "','" + Output_Eligible_Amount + "','Declined','" + eElement.getElementsByTagName(
											"Reason_Description").item(0)
											.getTextContent() + "-" + eElement.getElementsByTagName(
													"Reason_Code").item(0)
											.getTextContent() + "','" + Output_Delegation_Authority + "','" + Grade + "','" + wiName + "','"+cac_calc_limit_str+"','"+Category+"')";
								} else if ("R".equalsIgnoreCase(Reason_Decision)) {
									squery = "Insert into ng_rlos_IGR_Eligibility_CardProduct (S_no,Product,Card_Product,Eligible_Card,Decision,Deviation_Code_Refer,Delegation_Authorithy,Score_grade,wi_name,CACCalculatedLimit,category) values('" + List_len_num + "','Credit Card','" + subprodCCqueryXML.get(0).get(0) + "','" + Output_Eligible_Amount + "','Refer','" + eElement.getElementsByTagName(
											"Reason_Description").item(0)
											.getTextContent() + "-" + eElement.getElementsByTagName(
													"Reason_Code").item(0)
											.getTextContent() + "','" + Output_Delegation_Authority + "','" + Grade + "','" + wiName + "','"+cac_calc_limit_str+"','"+Category+"')";
								} else if ("A".equalsIgnoreCase(Reason_Decision)) {
									squery = "Insert into ng_rlos_IGR_Eligibility_CardProduct (S_no,Product,Card_Product,Eligible_Card,Decision,Deviation_Code_Refer,Delegation_Authorithy,Score_grade,wi_name,CACCalculatedLimit,category) values('" + List_len_num + "','Credit Card','" + subprodCCqueryXML.get(0).get(0) + "','" + Output_Eligible_Amount + "','Approve','" + eElement.getElementsByTagName(
											"Reason_Description").item(0)
											.getTextContent() + "-" + eElement.getElementsByTagName(
													"Reason_Code").item(0)
											.getTextContent() + "','" + Output_Delegation_Authority + "','" + Grade + "','" + wiName + "','"+cac_calc_limit_str+"','"+Category+"')";
								}
							} 
							else{
								if ("D".equalsIgnoreCase(Reason_Decision)) {
									squery = "Insert into ng_rlos_IGR_Eligibility_PersonalLoan (S_no,RequestedLoan,RequestedAppType,EligibleExposure,Decision,DeclinedReasonCode,DelegationAuthorithy,wi_name,CACCalculatedLimit,category) values('" + List_len_num + "','" + subprodqueryXML.get(0).get(0) + "','" +apptypequeryXML.get(0).get(0)+"','"+ Output_Eligible_Amount + "','Declined','" + eElement.getElementsByTagName(
											"Reason_Description").item(0)
											.getTextContent() + "-" + eElement.getElementsByTagName(
													"Reason_Code").item(0)
											.getTextContent() + "','" + Output_Delegation_Authority + "','" + wiName + "','"+cac_calc_limit_str+"','"+Category+"')";
								} else if ("R".equalsIgnoreCase(Reason_Decision)) {
									squery = "Insert into ng_rlos_IGR_Eligibility_PersonalLoan (S_no,RequestedLoan,RequestedAppType,EligibleExposure,Decision,ReferReasoncode,DelegationAuthorithy,wi_name,CACCalculatedLimit,category) values('" + List_len_num + "','" + subprodqueryXML.get(0).get(0) + "','" +apptypequeryXML.get(0).get(0)+"','"+ Output_Eligible_Amount + "','Refer','" + eElement.getElementsByTagName(
											"Reason_Description").item(0)
											.getTextContent() + "-" + eElement.getElementsByTagName(
													"Reason_Code").item(0)
											.getTextContent() + "','" + Output_Delegation_Authority + "','" + wiName + "','"+cac_calc_limit_str+"','"+Category+"')";
								} else if ("A".equalsIgnoreCase(Reason_Decision)) {
									squery = "Insert into ng_rlos_IGR_Eligibility_PersonalLoan (S_no,RequestedLoan,RequestedAppType,EligibleExposure,Decision,ReferReasoncode,DelegationAuthorithy,wi_name,CACCalculatedLimit,category) values('" + List_len_num + "','" + subprodqueryXML.get(0).get(0) + "','" +apptypequeryXML.get(0).get(0)+"','"+ Output_Eligible_Amount + "','Approve','" + eElement.getElementsByTagName(
											"Reason_Description").item(0)
											.getTextContent() + "-" + eElement.getElementsByTagName(
													"Reason_Code").item(0)
											.getTextContent() + "','" + Output_Delegation_Authority + "','" + wiName + "','"+cac_calc_limit_str+"','"+Category+"')";
								}
							}
						}

						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Squery is" + squery);
						ExecuteDbquery(squery);
						
						//Added by Tarang on 10/06/2019 against CR
						if (a!=0){
							responseObject.put("DectechCategory", "A");
						}
						else if ((b !=0)&&(a ==0)){
							responseObject.put("DectechCategory", "B");
						}
						else{
							responseObject.put("DectechCategory", "C");
						}
						//Added by Tarang on 10/06/2019 against CR
					}
				}
				DeviationReferCode = DeviationReferCode.replaceAll("&gt;", ">").replaceAll("&lt;", "<");
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"Value of DeviationReferCode" + DeviationReferCode);
				responseObject.put("DeviationReferCode", DeviationReferCode);
			}
		} catch (Exception e) {
			writePrintStackTrace(e);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Exception:" + e.getMessage());
			//e.printStackTrace();
		}
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"Inside dectechfromeligiblity method end:" );
		return responseObject;
	}

	public static String ExecuteQuery_APdelete(String tableName, String sWhere,
			String cabinetName, String sessionId) {
		String sInputXML = "<?xml version=\"1.0\"?>" + "<APDelete_Input><Option>APDelete</Option>" + "<TableName>" + tableName + "</TableName>" + "<WhereClause>" + sWhere + "</WhereClause>" + "<EngineName>" + cabinetName + "</EngineName>" + "<SessionId>" + sessionId + "</SessionId>" + "</APDelete_Input>";
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "APdelete INputXml is:" + sInputXML);
		return sInputXML;
	}

	public static String calculateEmi(BigDecimal tenor, BigDecimal finalLimit, BigDecimal InterestRate) {
		String emi = "0";



		try {

			BigDecimal PMTEMI = calcEMI(finalLimit, tenor, InterestRate);

			PMTEMI = PMTEMI.setScale(2, BigDecimal.ROUND_HALF_EVEN);



			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "PMTEMI  **************" + PMTEMI);

			double seedvalue = PMTEMI.doubleValue();

			emi = Double.toString(seedvalue);
		} catch (Exception e) {

			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Exception occured in calcgoalseekEMI() : ");



			emi = "0";

		}
		return emi;
	}

	public static BigDecimal calcEMI(BigDecimal P, BigDecimal N, BigDecimal ROI) {

		BigDecimal emi = new BigDecimal(0);

		try {

			MathContext mc = MathContext.DECIMAL128;

			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "mc : " + mc);

			BigDecimal R = ROI.divide(new BigDecimal(1200), mc);

			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "R : " + R);

			BigDecimal nemi1 = P.multiply(R, mc);

			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "nemi1 : " + nemi1);

			BigDecimal npower1 = (BigDecimal.ONE.add(R)).pow(N.intValue(), mc);



			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "npower1 : " + npower1);

			BigDecimal dpower1 = (BigDecimal.ONE.add(R)).pow(N.intValue(), mc);

			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "dpower1 : " + dpower1);

			BigDecimal denominator = dpower1.subtract(BigDecimal.ONE);

			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "denominator : " + denominator);

			BigDecimal numerator = nemi1.multiply(npower1);

			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "numerator : " + numerator);

			emi = numerator.divide(denominator, 0);

			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "emi : " + emi);



		} catch (Exception e) {

			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Exception occured in calcEMI() : ");

		}

		return emi;

	}


	public boolean isStringNotNullEmpty(final String s) {
		return s != null && !(s.trim().isEmpty()) && !(s.trim().equalsIgnoreCase("null"));
	}

	public String Convert_dateFormat(String date, String Old_Format,
			String new_Format) {
		LogMe
		.logMe(LogMe.LOG_LEVEL_DEBUG, "Inside Convert_dateFormat()" + date);
		String new_date = "";
		if (date.equals(null) || date.equals("")) {
			return new_date;
		}

		try {
			SimpleDateFormat sdf_old = new SimpleDateFormat(Old_Format);
			SimpleDateFormat sdf_new = new SimpleDateFormat(new_Format);
			new_date = sdf_new.format(sdf_old.parse(date));
		} catch (Exception e) {
			writePrintStackTrace(e);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
					"Exception occurred in parsing date:" + e.getMessage());
		}
		return new_date;
	}
// commented by abhishek dure to null pointer issue
	/*public String getProduct_details(String wiName, JSONObject param_json_object) {
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "inside getProduct_details : ");
		String prod_xml_str = "";
		try {
//			String FinalLimitQuery = "select isnull(Final_limit,0) from ng_rlos_IGR_Eligibility_CardProduct where wi_name='" + wiName + "'";
//			List < List < String >> FinalLimitXML = ExecuteSelectQueryGetList(FinalLimitQuery);
//
//			String FinalLimitPLQuery = "select isnull(FinalLoanAmount,0) from ng_rlos_IGR_Eligibility_PersonalLoan where wi_name='" + wiName + "'";
//			List < List < String >> FinalLimitPLXML = ExecuteSelectQueryGetList(FinalLimitPLQuery);

			JSONArray prod_array = (JSONArray) param_json_object.get("cmplx_ProductGrid");
			int prod_row_count = prod_array.size();
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"inside getCustAddress_details add_row_count+ : "+prod_row_count);
			
			for (int i = 0; i<prod_row_count;i++){
				JSONObject product_obj = (JSONObject) prod_array.get(i);
				
				String prod_type = product_obj.get("product_type").toString().trim();
				String reqProd = product_obj.get("requested_product").toString().trim();
				String subProd = product_obj.get("sub_product").toString().trim();
				String reqLimit = product_obj.get("requested_limit").toString().trim();
				reqLimit = reqLimit.replaceAll(",", "");
				String appType = product_obj.get("application_type").toString().trim();
				String cardProd = product_obj.get("requested_card_product").toString().trim();
				String scheme = product_obj.get("scheme").toString().trim();
				String tenure = product_obj.get("tenure").toString().trim();
				String limitExpiry = product_obj.get("limit_Expiry_Date").toString().trim();
				limitExpiry = Convert_dateFormat(limitExpiry, "yyyy-MM-dd","yyyy-MM-dd");
				
				String ApplicationDate = param_json_object.get("IntroductionDateTime").toString().trim();
				String AppCateg = param_json_object.get("app_category").toString().trim();
//				if ("Self Employed".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 6))){
//					RLOS.mLogger.info( "inside cmplx_Product_cmplx_ProductGrid add_row_count+ : "+prod_row_count);
//					int Comp_row_count = formObject.getLVWRowCount("cmplx_CompanyDetails_cmplx_CompanyGrid");
//					RLOS.mLogger.info( "inside cmplx_Product_cmplx_ProductGrid Comp_row_count+ : "+Comp_row_count);
//					
//					for (int j = 0; j<Comp_row_count;j++){
//						if (formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", j, 2).equalsIgnoreCase("Secondary")){
//							AppCateg = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", j, 24); //0
//							RLOS.mLogger.info( "inside cmplx_Product_cmplx_ProductGrid Comp_row_count+ : "+AppCateg);
//							
//						}
//						}
//					
//				}
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "inside getCustAddress_details ApplicationDate+ : "+ApplicationDate);
				ApplicationDate=ApplicationDate.substring(0, 10)+"T"+ApplicationDate.substring(11, 19);

				String EMI = param_json_object.get("eligibilitydetails-emi").toString().trim();
				if (EMI == null) {
					EMI = "";
				}
				if (AppCateg == null) {
					AppCateg = "";
				}
				String finalLimit = param_json_object.get("eligibilitydetails-finallimit").toString().trim();
				
				prod_xml_str = prod_xml_str + "<product_type>" + (prod_type.equalsIgnoreCase("Conventional") ? "CON" : "ISL") + "</product_type>";
				// commented by abhishek to correct dectech input XML
				//prod_xml_str = prod_xml_str + "<app_category>" + AppCateg + "</app_category>";
				prod_xml_str = prod_xml_str + "<requested_product>" + (reqProd.equalsIgnoreCase("Personal Loan") ? "PL" : "CC") + "</requested_product>";
				prod_xml_str = prod_xml_str + "<requested_limit>" + reqLimit + "</requested_limit>";
				prod_xml_str = prod_xml_str + "<sub_product>" + subProd + "</sub_product>";
				// commented by abhishek to correct dectech call
				//prod_xml_str = prod_xml_str + "<requested_card_product>" + cardProd  + "</requested_card_product>";
				prod_xml_str = prod_xml_str + "<application_type>" + appType + "</application_type>";
				prod_xml_str = prod_xml_str + "<scheme>" + scheme + "</scheme>";
				prod_xml_str = prod_xml_str + "<tenure>" + tenure + "</tenure>";
				String cust_type = (isStringNotNullEmpty(param_json_object.get("customerDetails-cif").toString().trim())) ? "Existing" : "NTB";
				prod_xml_str = prod_xml_str + "<customer_type>" + cust_type + "</customer_type>";
//				if (reqProd.equalsIgnoreCase("Credit Card")) {
//					prod_xml_str = prod_xml_str + "<limit_expiry_date>" + limitExpiry + "</limit_expiry_date><final_limit>" + finalLimit + "</final_limit><emi>" + EMI + "</emi><manual_deviation>N</manual_deviation>";
//				} else {
					prod_xml_str = prod_xml_str + "<limit_expiry_date>" + limitExpiry + "</limit_expiry_date><final_limit>" + finalLimit + "</final_limit><emi>" + EMI + "</emi><manual_deviation>N</manual_deviation>" + "<application_date>" + ApplicationDate + "</application_date>";
//				}
			}
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Address tag Cration: " + prod_xml_str);
		} catch (Exception e) {
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Exception occurred in getProduct_details " + e.getMessage());
			e.printStackTrace();
		}
		return prod_xml_str;
	}*/
	
	public String getProduct_details(String wiName, JSONObject param_json_object) {
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "inside getProduct_details :json " +param_json_object.toString());
		String prod_xml_str = "";
		try {
			// int prod_row_count =
			// formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			// SKLogger.writeLog("RLOSCommon java file",
			// "inside getCustAddress_details add_row_count+ : "+prod_row_count);
			/*String FinalLimitQuery = "select isnull(Final_limit,0) from ng_rlos_IGR_Eligibility_CardProduct where wi_name='"
				+ wiName + "'";
			List<List<String>> FinalLimitXML = ExecuteSelectQueryGetList(FinalLimitQuery);

			String FinalLimitPLQuery = "select isnull(FinalLoanAmount,0) from ng_rlos_IGR_Eligibility_PersonalLoan where wi_name='"
				+ wiName + "'";
			List<List<String>> FinalLimitPLXML = ExecuteSelectQueryGetList(FinalLimitPLQuery);*/

			String finalLimit = param_json_object.get(
			"eligibilitydetails-finallimit").toString().trim();
			// if (FinalLimitXML.size()!= 0){
			// finalLimit=FinalLimitXML.get(0).get(0);
			// //
			// formObject.setNGValue("cmplx_EligibilityAndProductInfo_FinalLimit",
			// finalLimit);
			// }
			// else if (FinalLimitPLXML.size()!= 0){
			// finalLimit=FinalLimitPLXML.get(0).get(0);
			// //
			// formObject.setNGValue("cmplx_EligibilityAndProductInfo_FinalLimit",
			// finalLimit);
			// }

			// for (int i = 0; i<prod_row_count;i++){
			String prod_type = param_json_object.get("product_type").toString()
			.trim();
			String reqProd = param_json_object.get("requested_product")
			.toString().trim();
			String subProd = param_json_object.get("sub_product").toString()
			.trim();
			String reqLimit = param_json_object.get("requested_limit")
			.toString().trim();
			reqLimit = reqLimit.replaceAll(",", "");
			String appType = param_json_object.get("application_type")
			.toString().trim();
			String cardProd = param_json_object.get("requested_card_product")
			.toString().trim();
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "inside getProduct_details :json testing done on 30/04" +param_json_object.toString());
			String scheme = param_json_object.get("scheme").toString().trim();
			String tenure = param_json_object.get("tenure").toString().trim();
			String limitExpiry = (String) param_json_object.get("limit_Expiry_Date");
			
			String ApplicationDate = param_json_object.get(
			"IntroductionDateTime").toString().trim();
			ApplicationDate=ApplicationDate.substring(0, 10)+"T"+ApplicationDate.substring(11, 19);
			//String ApplicationDate = "2018-03-21T11:12:12";
			String AppCateg = (String) param_json_object.get("app_category");
			/*limitExpiry = Convert_dateFormat(limitExpiry, "yyyy-MM-dd",
			"yyyy-MM-dd");*/
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "inside getProduct_details AppCategory :json " +AppCateg);
			String EMI = param_json_object.get("eligibilitydetails-emi")
			.toString().trim();
			if (EMI == null) {
				EMI = "";
			}
			if (AppCateg == null) {
				AppCateg = "";
			}
			// ApplicationDate=Convert_dateFormat(ApplicationDate, "dd/MM/yyyy",
			// "yyyy-MM-dd");

			prod_xml_str = prod_xml_str
			+ "<product_type>"
			+ (prod_type.equalsIgnoreCase("Conventional") ? "CON"
					: "ISL") + "</product_type>";
			prod_xml_str = prod_xml_str + "<app_category>"
			+ AppCateg
			+ "</app_category>";
			prod_xml_str = prod_xml_str + "<requested_product>"
			+ (reqProd.equalsIgnoreCase("Personal Loan") ? "PL" : "CC")
			+ "</requested_product>";
			prod_xml_str = prod_xml_str + "<requested_limit>" + reqLimit
			+ "</requested_limit>";
			prod_xml_str = prod_xml_str + "<sub_product>" + subProd
			+ "</sub_product>";
			prod_xml_str = prod_xml_str + "<requested_card_product>" + cardProd
			+ "</requested_card_product>";
			prod_xml_str = prod_xml_str + "<application_type>" + appType
			+ "</application_type>";
			prod_xml_str = prod_xml_str + "<scheme>" + scheme + "</scheme>";
			prod_xml_str = prod_xml_str + "<tenure>" + tenure + "</tenure>";
			String cust_type = (isStringNotNullEmpty(param_json_object.get(
			"customerDetails-cif").toString().trim())) ? "Existing"
					: "NTB";
			prod_xml_str = prod_xml_str + "<customer_type>" + cust_type
			+ "</customer_type>";
			//if (reqProd.equalsIgnoreCase("Credit Card")) {
				/*prod_xml_str = prod_xml_str + "<limit_expiry_date>"
				+ limitExpiry + "</limit_expiry_date><final_limit>"
				+ finalLimit + "</final_limit><emi>" + EMI
				+ "</emi><manual_deviation>N</manual_deviation>";*/
			//} else {
				prod_xml_str = prod_xml_str + "<limit_expiry_date>"
				+ limitExpiry + "</limit_expiry_date><final_limit>"
				+ finalLimit + "</final_limit><emi>" + EMI
				+ "</emi><manual_deviation>N</manual_deviation>"
				+ "<application_date>" + ApplicationDate
				+ "</application_date>";
			//}
			// }
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Address tag Cration: "
					+ prod_xml_str);
		} catch (Exception e) {
			writePrintStackTrace(e);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
					"Exception occurred in getProduct_details: "
					+ e.getMessage());
			//e.printStackTrace();
		}
		return prod_xml_str;
	}
	

	public String getInternalLiabDetails(String wiName) {
		String add_xml_str = "";
		try {
			String sQuery = "SELECT AcctType,CustRoleType,AcctId,AccountOpenDate,AcctStat,AcctSegment,AcctSubSegment,CreditGrade FROM ng_RLOS_CUSTEXPOSE_AcctDetails  with (nolock) where Wi_Name = '" + wiName + "' and Request_Type = 'InternalExposure'";
			JSONArray InternalLiabArray = getData(sQuery,"AcctType,CustRoleType,AcctId,AccountOpenDate,AcctStat,AcctSegment,AcctSubSegment,CreditGrade");

			for (int i = 0; i < InternalLiabArray.size(); i++) {
				JSONObject InternalLiabObject = (JSONObject) InternalLiabArray.get(i);
				String accountType = InternalLiabObject.get("AcctType").toString();
				String role = InternalLiabObject.get("CustRoleType").toString();
				String accNumber = InternalLiabObject.get("AcctId").toString();
				String acctOpenDate = InternalLiabObject.get("AccountOpenDate").toString();
				String acctStatus = InternalLiabObject.get("AcctStat").toString();
				String acctSegment = InternalLiabObject.get("AcctSegment").toString();
				String acctSubSegment = InternalLiabObject.get("AcctSubSegment").toString();
				String acctCreditGrade = InternalLiabObject.get("CreditGrade").toString();

				if (accNumber != null && !accNumber.equalsIgnoreCase("") && !accNumber.equalsIgnoreCase("null")) {
					add_xml_str = add_xml_str + "<type_of_account>" + accountType + "</type_of_account>";
					add_xml_str = add_xml_str + "<role>" + role + "</role>";
					add_xml_str = add_xml_str + "<account_number>" + accNumber + "</account_number>";
					add_xml_str = add_xml_str + "<acct_open_date>" + acctOpenDate + "</acct_open_date>";
					add_xml_str = add_xml_str + "<acct_status>" + acctStatus + "</acct_status>";
					add_xml_str = add_xml_str + "<account_segment>" + acctSegment + "</account_segment>";
					add_xml_str = add_xml_str + "<account_sub_segment>" + acctSubSegment + "</account_sub_segment>";
					add_xml_str = add_xml_str + "<credit_grate_code>" + acctCreditGrade + "</credit_grate_code>";
					add_xml_str = add_xml_str + "<cust_type>" + role + "</cust_type>";
//					add_xml_str = add_xml_str + "</AccountDetails><AccountDetails>";
				}


				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Internal Liabilty Details: " + add_xml_str);
			}
		} catch (Exception e) {
			writePrintStackTrace(e);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Exception occurred in getInternalLiabDetails" + e.getMessage());
			//e.printStackTrace();
		}
		return add_xml_str;
	}

	public String InternalBureauData(String wiName, String CifId, String fullName) {

		String NoOfContracts = "";
		String Total_Exposure = "";
		String WorstCurrentPaymentDelay = "";
		String Worst_PaymentDelay_Last24Months = "";
		String Nof_Records = "";

		String add_xml_str = "";
		try {
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Inside InternalBureauData : ");
			add_xml_str = add_xml_str + "<applicant_id>" + CifId + "</applicant_id>";
			add_xml_str = add_xml_str + "<full_name>" + fullName + "</full_name>";
			//String sQuery = "SELECT isNull((Sum(convert(float,replace([OutstandingAmt],'NA','0')))),0),isNull((Sum(convert(float,replace([OverdueAmt],'NA','0')))),0),isNull((Sum(convert(float,replace([CreditLimit],'NA','0')))),0)FROM ng_RLOS_CUSTEXPOSE_CardDetails WHERE wi_name= '"+wiName+"' AND Request_Type = 'InternalExposure'  union SELECT   isNull((Sum(convert(float,replace([TotalOutstandingAmt],'NA','0')))),0),isNull((Sum(convert(float,replace([OverdueAmt],'NA','0')))),0),isNull((Sum(convert(float,replace([TotalLoanAmount],'NA','0')))),0) FROM ng_RLOS_CUSTEXPOSE_LoanDetails   with (nolock) WHERE wi_name = '"+wiName+"'";
			String sQuery = "SELECT isNull((Sum(Abs(convert(float,replace([OutstandingAmt],'NA','0'))))),0),isNull((Sum(Abs(convert(float,replace([OverdueAmt],'NA','0'))))),0) FROM ng_RLOS_CUSTEXPOSE_CardDetails WHERE wi_name= '"+wiName+"' AND Request_Type = 'InternalExposure'  union SELECT   isNull((Sum(Abs(convert(float,replace([TotalOutstandingAmt],'NA','0'))))),0),isNull((Sum(Abs(convert(float,replace([OverdueAmt],'NA','0'))))),0) FROM ng_RLOS_CUSTEXPOSE_LoanDetails   with (nolock) WHERE wi_name = '"+wiName+"'";
		
			List<List<String>> InternalBureauArray = ExecuteSelectQueryGetList(sQuery);

			double TotOutstandingAmt = 0.0f;
			double TotOverdueAmt = 0.0f;

			for (int i = 0; i < InternalBureauArray.size(); i++) {
				if (isStringNotNullEmpty(InternalBureauArray.get(i).get(0)))
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "before : TotOutstandingAmt: " + TotOutstandingAmt);
					TotOutstandingAmt = TotOutstandingAmt + Double.parseDouble(InternalBureauArray.get(i).get(0));
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "after : TotOutstandingAmt: " + TotOutstandingAmt);
				if (isStringNotNullEmpty(InternalBureauArray.get(i).get(1)))
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "before : TotOverdueAmt: " + TotOverdueAmt);
					TotOverdueAmt = TotOverdueAmt + Double.parseDouble(InternalBureauArray.get(i).get(1));
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "after : TotOverdueAmt: " + TotOverdueAmt);
			}
			String TotOutstandingAmtSt=String.format("%.0f", TotOutstandingAmt);
			String TotOverdueAmtSt=String.format("%.0f", TotOverdueAmt);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "after format : TotOverdueAmt: " + TotOverdueAmt);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "after f: TotOutstandingAmt: " + TotOutstandingAmt);
			add_xml_str = add_xml_str + "<total_out_bal>" + TotOutstandingAmtSt + "</total_out_bal>";
			add_xml_str = add_xml_str + "<total_overdue>" + TotOverdueAmtSt + "</total_overdue>";
			String sQueryDerived = "select NoOfContracts,Total_Exposure,WorstCurrentPaymentDelay,Worst_PaymentDelay_Last24Months,Nof_Records from NG_RLOS_CUSTEXPOSE_Derived where Request_Type='CollectionsSummary' and Wi_Name='" + wiName + "'";
			JSONArray OutputXMLDerived = getData(sQueryDerived,"NoOfContracts,Total_Exposure,WorstCurrentPaymentDelay,Worst_PaymentDelay_Last24Months,Nof_Records");
			if (OutputXMLDerived.size() > 0) {
				JSONObject OutputXMLDerivedObject = (JSONObject) OutputXMLDerived.get(0);
				NoOfContracts = OutputXMLDerivedObject.get("NoOfContracts").toString();
				Total_Exposure = OutputXMLDerivedObject.get("Total_Exposure").toString();
				WorstCurrentPaymentDelay = OutputXMLDerivedObject.get("WorstCurrentPaymentDelay").toString();
				Worst_PaymentDelay_Last24Months = OutputXMLDerivedObject.get("Worst_PaymentDelay_Last24Months").toString();
				Nof_Records = OutputXMLDerivedObject.get("Nof_Records").toString();
			}

			add_xml_str = add_xml_str + "<no_default_contract>" + NoOfContracts + "</no_default_contract>";

			add_xml_str = add_xml_str + "<total_exposure>" + Total_Exposure + "</total_exposure>";
			add_xml_str = add_xml_str + "<worst_curr_pay>" + WorstCurrentPaymentDelay + "</worst_curr_pay>"; // to be
			add_xml_str = add_xml_str + "<worst_curr_pay_24>" + Worst_PaymentDelay_Last24Months + "</worst_curr_pay_24>"; // to
			add_xml_str = add_xml_str + "<no_of_rec>" + Nof_Records + "</no_of_rec>";

			String sQuerycheque = "SELECT 'DDS 3 months' as DDS3Months,count(*) as count FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-3,GETDATE()) and returntype='DDS' and Wi_Name='" + wiName + "' union SELECT 'DDS 6 months',count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-6,GETDATE()) and returntype='DDS' and Wi_Name='" + wiName + "' union SELECT 'ICCS 3 months',count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-3,GETDATE()) and returntype='ICCS' and Wi_Name='" + wiName + "' union SELECT 'ICCS 6 months',count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-6,GETDATE()) and returntype='ICCS' and Wi_Name='" + wiName + "'";
			List<List<String>> OutputXMLcheque = ExecuteSelectQueryGetList(sQuerycheque);
			
			add_xml_str = add_xml_str + "<cheque_return_3mon>"+OutputXMLcheque.get(2).get(1)+"</cheque_return_3mon>";
			add_xml_str = add_xml_str + "<dds_return_3mon>"+OutputXMLcheque.get(0).get(1)+"</dds_return_3mon>";
			add_xml_str = add_xml_str + "<cheque_return_6mon>"+OutputXMLcheque.get(3).get(1)+"</cheque_return_6mon>";
			add_xml_str = add_xml_str + "<dds_return_6mon>"+OutputXMLcheque.get(1).get(1)+"</dds_return_6mon>";
			add_xml_str = add_xml_str + "<internal_charge_off>" + "N" + "</internal_charge_off><company_flag>N</company_flag>"; // to

			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Internal Bureau Data: " + add_xml_str);
		} catch (Exception e) {
			writePrintStackTrace(e);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"Exception occurred in InternalBureauData()" + e.getMessage());
			//e.printStackTrace();
		}
		return add_xml_str;
	}

	public String InternalBouncedCheques(String wiName) {
		String add_xml_str = "";
		try {
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"Inside InternalBouncedCheques : ");
			String sQuery = "select CIFID,AcctId,returntype,returnNumber,returnAmount,retReasonCode,returnDate from ng_rlos_FinancialSummary_ReturnsDtls with (nolock) where Wi_Name = '" + wiName + "'";
			JSONArray InternalBouncedChequesArray = getData(sQuery,
					"CIFID,AcctId,returntype,returnNumber,returnAmount,retReasonCode,returnDate");

			for (int i = 0; i < InternalBouncedChequesArray.size(); i++) {
				JSONObject InternalBouncedChequesObject = (JSONObject) InternalBouncedChequesArray
						.get(i);

				String applicantID = InternalBouncedChequesObject.get("CIFID")
						.toString();
				String chequeNo = InternalBouncedChequesObject.get("AcctId")
						.toString();
				String internal_bounced_cheques_id = InternalBouncedChequesObject
						.get("returntype").toString();
				String bouncedCheq = InternalBouncedChequesObject.get(
						"returnNumber").toString();
				String amount = InternalBouncedChequesObject
						.get("returnAmount").toString();
				String reason = InternalBouncedChequesObject.get(
						"retReasonCode").toString();
				String returnDate = InternalBouncedChequesObject.get(
						"returnDate").toString();

				if (applicantID != null && !applicantID.equalsIgnoreCase("") && !applicantID.equalsIgnoreCase("null")) {
					add_xml_str = add_xml_str + "<applicant_id>" + applicantID + "</applicant_id>";
					add_xml_str = add_xml_str + "<internal_bounced_cheques_id>" + internal_bounced_cheques_id + "</internal_bounced_cheques_id>";
					if (bouncedCheq.equalsIgnoreCase("ICCS")) {
						add_xml_str = add_xml_str + "<bounced_cheque>" + "1" + "</bounced_cheque>";
					}
					add_xml_str = add_xml_str + "<cheque_no>" + chequeNo + "</cheque_no>";
					add_xml_str = add_xml_str + "<amount>" + amount + "</amount>";
					add_xml_str = add_xml_str + "<reason>" + reason + "</reason>";
					add_xml_str = add_xml_str + "<return_date>" + returnDate + "</return_date>";
					add_xml_str = add_xml_str + "<provider_no>" + "RAKBANK" + "</provider_no>";
					if (bouncedCheq.equalsIgnoreCase("DDS")) {
						add_xml_str = add_xml_str + "<bounced_cheque_dds>" + "1" + "</bounced_cheque_dds>";
					}
					add_xml_str = add_xml_str + "<company_flag>N</company_flag>";
				}

				if (i < InternalBouncedChequesArray.size()) {
					add_xml_str = add_xml_str + "</InternalBouncedCheques><InternalBouncedCheques>";
				}
			}
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Internal Bounced Cheques: " + add_xml_str);
		} catch (Exception e) {
			writePrintStackTrace(e);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
					"Exception occurred in InternalBouncedCheques()" + e.getMessage());
			//e.printStackTrace();
		}
		return add_xml_str;
	}

	public String InternalBureauIndividualProducts(String wiName, String Kompass, JSONObject param_json_object) {
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "inside InternalBureauIndividualProducts : ");
		String add_xml_str = "";
		try {
			String sQuery = "SELECT cifid,agreementid,loantype,loantype,custroletype,loan_start_date,loanmaturitydate,lastupdatedate ,totaloutstandingamt,totalloanamount,NextInstallmentAmt,paymentmode,totalnoofinstalments,remaininginstalments,totalloanamount,                overdueamt,nofdayspmtdelay,monthsonbook,currentlycurrentflg,currmaxutil,DPD_30_in_last_6_months,DPD_60_in_last_18_months,propertyvalue,loan_disbursal_date,marketingcode,DPD_30_in_last_3_months,DPD_30_in_last_6_months,DPD_30_in_last_9_months,DPD_30_in_last_12_months,DPD_30_in_last_18_months,DPD_30_in_last_24_months,DPD_60_in_last_3_months,DPD_60_in_last_6_months,DPD_60_in_last_9_months,DPD_60_in_last_12_months,DPD_60_in_last_18_months,DPD_60_in_last_24_months,DPD_90_in_last_3_months,DPD_90_in_last_6_months,DPD_90_in_last_9_months,DPD_90_in_last_12_months,DPD_90_in_last_18_months,DPD_90_in_last_24_months,DPD_120_in_last_3_months,DPD_120_in_last_6_months,DPD_120_in_last_9_months,DPD_120_in_last_12_months,DPD_120_in_last_18_months,DPD_120_in_last_24_months,DPD_150_in_last_3_months,DPD_150_in_last_6_months,DPD_150_in_last_9_months,DPD_150_in_last_12_months,DPD_150_in_last_18_months,DPD_150_in_last_24_months,DPD_180_in_last_3_months,DPD_180_in_last_6_months,DPD_180_in_last_9_months,DPD_180_in_last_12_months,DPD_180_in_last_24_months,'' as ExpiryDate,isnull(Consider_For_Obligations,'true') as Consider_For_Obligations,LoanStat,'LOANS' as Loans,writeoffStat,writeoffstatdt,lastrepmtdt,limit_increase,PartSettlementDetails,SchemeCardProd as SchemeCardProduct,General_Status,Internal_WriteOff_Check,'' as odtype,AmountPaidInLst6Mnths,InterestRate FROM ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) WHERE Wi_Name = '" + wiName + "' and LoanStat  not in ('Pipeline','CAS-Pipeline') union select CifId,CardEmbossNum,CardType,CardType,CustRoleType,'' as col6,'' as col7,'' as col8,OutstandingAmt,CreditLimit,case when SchemeCardProd like '%LOC%' then (select Top 1 monthlyamount from ng_RLOS_CUSTEXPOSE_CardInstallmentDetails where Wi_Name = '" + wiName + "' and CardCRNNumber=CardEmbossNum) else PaymentsAmount end as PaymentsAmount,PaymentMode,'' as col13,case when SchemeCardProd like 'LOC%' then (select top 1 ISNULL((CAST(INSTALMENTpERIOD AS INT)-CAST(rEMAININGemi AS INT)),'') from ng_RLOS_CUSTEXPOSE_CardInstallmentDetails where Wi_Name = '" + wiName + "') else ''end as col14,CashLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,'' as col19,CurrMaxUtil,DPD_30_in_last_6_months,DPD_60_in_last_18_months,'' as col23,'' as col24,'' as col25,DPD_30_in_last_3_months,DPD_30_in_last_6_months,DPD_30_in_last_9_months,DPD_30_in_last_12_months,DPD_30_in_last_18_months,DPD_30_in_last_24_months,DPD_60_in_last_3_months,DPD_60_in_last_6_months,DPD_60_in_last_9_months,DPD_60_in_last_12_months,DPD_60_in_last_18_months,DPD_60_in_last_24_months,DPD_90_in_last_3_months,DPD_90_in_last_6_months,DPD_90_in_last_9_months,DPD_90_in_last_12_months,DPD_90_in_last_18_months,DPD_90_in_last_24_months,DPD_120_in_last_3_months,DPD_120_in_last_6_months,DPD_120_in_last_9_months,DPD_120_in_last_12_months,DPD_120_in_last_18_months,DPD_120_in_last_24_months,DPD_150_in_last_3_months,DPD_150_in_last_6_months,DPD_150_in_last_9_months,DPD_150_in_last_12_months,DPD_150_in_last_18_months,DPD_150_in_last_24_months,DPD_180_in_last_3_months,DPD_180_in_last_6_months,DPD_180_in_last_9_months,DPD_180_in_last_12_months,DPD_180_in_last_24_months,ExpiryDate,isnull(Consider_For_Obligations,'true') as Consider_For_Obligations,CardStatus,'CARDS',writeoffStat,writeoffstatdt,'' as lastrepdate,limit_increase,'' as PartSettlementDetails,SchemeCardProd,General_Status,Internal_WriteOff_Check,'' as odtype,AmountPaidInLst6Mnths,InterestRate FROM ng_RLOS_CUSTEXPOSE_CardDetails with (nolock) where Wi_Name = '" + wiName + "' and Request_Type In ('InternalExposure','CollectionsSummary') union select CifId,AcctId,'OverDraft' as loantype,'OverDraft' as loantype,CustRoleType,LimitSactionDate as loan_start_date,LimitExpiryDate as loanmaturitydate,'' as lastupdateddate,ClearBalanceAmount,SanctionLimit,'','','','',SanctionLimit,OverdueAmount,DaysPastDue,MonthsOnBook,IsCurrent,CurUtilRate,DPD30Last6Months,DPD60Last18Months,'',AccountOpenDate,'','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','',LimitExpiryDate,isNull(Consider_For_Obligations,'true'),AcctStat,'OVERDRAFT',WriteoffStat,WriteoffStatDt,LastRepmtDt,'','','','','',odtype,'','' from ng_rlos_custexpose_acctdetails where Wi_Name = '" + wiName + "'  and ODType != ''";
			JSONArray InternalBureauIndividualArray = getData(sQuery, "cifid,agreementid,loantype,loantype,custroletype,loan_start_date,loanmaturitydate,lastupdatedate,totaloutstandingamt,totalloanamount,NextInstallmentAmt,paymentmode,totalnoofinstalments,remaininginstalments,totalloanamount,overdueamt,nofdayspmtdelay,monthsonbook,currentlycurrentflg,currmaxutil,DPD_30_in_last_6_months,DPD_60_in_last_18_months,propertyvalue,loan_disbursal_date,marketingcode,DPD_30_in_last_3_months,DPD_30_in_last_6_months,DPD_30_in_last_9_months,DPD_30_in_last_12_months,DPD_30_in_last_18_months,DPD_30_in_last_24_months,DPD_60_in_last_3_months,DPD_60_in_last_6_months,DPD_60_in_last_9_months,DPD_60_in_last_12_months,DPD_60_in_last_18_months,DPD_60_in_last_24_months,DPD_90_in_last_3_months,DPD_90_in_last_6_months,DPD_90_in_last_9_months,DPD_90_in_last_12_months,DPD_90_in_last_18_months,DPD_90_in_last_24_months,DPD_120_in_last_3_months,DPD_120_in_last_6_months,DPD_120_in_last_9_months,DPD_120_in_last_12_months,DPD_120_in_last_18_months,DPD_120_in_last_24_months,DPD_150_in_last_3_months,DPD_150_in_last_6_months,DPD_150_in_last_9_months,DPD_150_in_last_12_months,DPD_150_in_last_18_months,DPD_150_in_last_24_months,DPD_180_in_last_3_months,DPD_180_in_last_6_months,DPD_180_in_last_9_months,DPD_180_in_last_12_months,DPD_180_in_last_24_months,ExpiryDate,Consider_For_Obligations,LoanStat,Loans,writeoffStat,writeoffstatdt,lastrepmtdt,limit_increase,PartSettlementDetails,SchemeCardProduct,General_Status,Internal_WriteOff_Check,odtype,AmountPaidInLst6Mnths,InterestRate");
			String CountQuery = "select count(*) from ng_RLOS_CUSTEXPOSE_CardDetails where Wi_Name = '" + wiName + "' and cardStatus='A'";
			List < List < String >> CountXML = ExecuteSelectQueryGetList(CountQuery);
			String ReqProd = param_json_object.get("requested_product").toString().trim();
			//String cardprod = param_json_object.get("requested_card_product").toString().trim();
			
			
            LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "inside InternalBureauIndividualProducts sQuery : "+sQuery);
            LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "InternalBureauIndividualArray.size()  : "+InternalBureauIndividualArray.size());
			for (int i = 0; i < InternalBureauIndividualArray.size(); i++) {
				JSONObject InternalBureauObject = (JSONObject) InternalBureauIndividualArray.get(i);
				String cifId = InternalBureauObject.get("cifid").toString();
				String agreementId = InternalBureauObject.get("agreementid").toString();
				String product_type = InternalBureauObject.get("loantype").toString().equalsIgnoreCase("Home In One") ? "HIO" : InternalBureauObject.get("Loans").toString();
				String contractType = InternalBureauObject.get("loantype").toString();
				String custroletype = InternalBureauObject.get("custroletype").toString();
				String loan_start_date = InternalBureauObject.get("loan_start_date").toString();
				String loanmaturitydate = InternalBureauObject.get("loanmaturitydate").toString();
				String lastupdatedate = InternalBureauObject.get("lastupdatedate").toString();
				String outstandingamt = InternalBureauObject.get("totaloutstandingamt").toString();
				String totalloanamount = InternalBureauObject.get("totalloanamount").toString();
				String emi = InternalBureauObject.get("NextInstallmentAmt").toString();
				
				// Rajeshwar: to change
				/*if (emi == "") {
					emi = "1000";
				}*/
				String paymentmode = InternalBureauObject.get("paymentmode").toString();
				String totalnoofinstalments = InternalBureauObject.get("totalnoofinstalments").toString();
				String remaininginstalments = InternalBureauObject.get("remaininginstalments").toString();
				String CashLimit = InternalBureauObject.get("totalloanamount").toString();
				String overdueamt = InternalBureauObject.get("overdueamt").toString();
				String nofdayspmtdelay = InternalBureauObject.get("nofdayspmtdelay").toString();
				String monthsonbook = InternalBureauObject.get("monthsonbook").toString();
				String currentlycurrent = InternalBureauObject.get("currentlycurrentflg").toString();
				String currmaxutil = InternalBureauObject.get("currmaxutil").toString();
				String propertyvalue = InternalBureauObject.get("propertyvalue").toString();
				String loan_disbursal_date = InternalBureauObject.get("loan_disbursal_date").toString();
				String marketingcode = InternalBureauObject.get("marketingcode").toString();
				String DPD_30_in_last_3_months = InternalBureauObject.get("DPD_30_in_last_3_months").toString();
				String DPD_30_in_last_6_months = InternalBureauObject.get("DPD_30_in_last_6_months").toString();
				String DPD_30_in_last_9_months = InternalBureauObject.get("DPD_30_in_last_9_months").toString();
				String DPD_30_in_last_12_months = InternalBureauObject.get("DPD_30_in_last_12_months").toString();
				String DPD_30_in_last_18_months = InternalBureauObject.get("DPD_30_in_last_18_months").toString();
				String DPD_30_in_last_24_months = InternalBureauObject.get("DPD_30_in_last_24_months").toString();
				String DPD_60_in_last_3_months = InternalBureauObject.get("DPD_60_in_last_3_months").toString();
				String DPD_60_in_last_6_months = InternalBureauObject.get("DPD_60_in_last_6_months").toString();
				String DPD_60_in_last_9_months = InternalBureauObject.get("DPD_60_in_last_9_months").toString();
				String DPD_60_in_last_12_months = InternalBureauObject.get("DPD_60_in_last_12_months").toString();
				String DPD_60_in_last_18_months = InternalBureauObject.get("DPD_60_in_last_18_months").toString();
				String DPD_60_in_last_24_months = InternalBureauObject.get("DPD_60_in_last_24_months").toString();
				String DPD_90_in_last_3_months = InternalBureauObject.get("DPD_90_in_last_3_months").toString();
				String DPD_90_in_last_6_months = InternalBureauObject.get("DPD_90_in_last_6_months").toString();
				String DPD_90_in_last_9_months = InternalBureauObject.get("DPD_90_in_last_9_months").toString();
				String DPD_90_in_last_12_months = InternalBureauObject.get("DPD_90_in_last_12_months").toString();
				String DPD_90_in_last_18_months = InternalBureauObject.get("DPD_90_in_last_18_months").toString();
				String DPD_90_in_last_24_months = InternalBureauObject.get("DPD_90_in_last_24_months").toString();
				String DPD_120_in_last_3_months = InternalBureauObject.get("DPD_120_in_last_3_months").toString();
				String DPD_120_in_last_6_months = InternalBureauObject.get("DPD_120_in_last_6_months").toString();
				String DPD_120_in_last_9_months = InternalBureauObject.get("DPD_120_in_last_9_months").toString();
				String DPD_120_in_last_12_months = InternalBureauObject.get("DPD_120_in_last_12_months").toString();
				String DPD_120_in_last_18_months = InternalBureauObject.get("DPD_120_in_last_18_months").toString();
				String DPD_120_in_last_24_months = InternalBureauObject.get("DPD_120_in_last_24_months").toString();
				String DPD_150_in_last_3_months = InternalBureauObject.get("DPD_150_in_last_3_months").toString();
				String DPD_150_in_last_6_months = InternalBureauObject.get("DPD_150_in_last_6_months").toString();
				String DPD_150_in_last_9_months = InternalBureauObject.get("DPD_150_in_last_9_months").toString();
				String DPD_150_in_last_12_months = InternalBureauObject.get("DPD_150_in_last_12_months").toString();
				String DPD_150_in_last_18_months = InternalBureauObject.get("DPD_150_in_last_18_months").toString();
				String DPD_150_in_last_24_months = InternalBureauObject.get("DPD_150_in_last_24_months").toString();
				String DPD_180_in_last_3_months = InternalBureauObject.get("DPD_180_in_last_3_months").toString();
				String DPD_180_in_last_6_months = InternalBureauObject.get("DPD_180_in_last_6_months").toString();
				String DPD_180_in_last_9_months = InternalBureauObject.get("DPD_180_in_last_9_months").toString();
				String DPD_180_in_last_12_months = InternalBureauObject.get("DPD_180_in_last_12_months").toString();
				String DPD_180_in_last_24_months = InternalBureauObject.get("DPD_180_in_last_24_months").toString();
				String CardExpiryDate = InternalBureauObject.get("ExpiryDate").toString();
				String Consider_For_Obligations = InternalBureauObject.get("Consider_For_Obligations").toString().equalsIgnoreCase("true") ? "Y" : "N";

				// Rajeshwar: to change later
				// String Consider_For_Obligations = "Y";
				String phase = InternalBureauObject.get("LoanStat").toString().startsWith("C") ? "C" : "A";

				String writeoffStat = InternalBureauObject.get("Loans").toString();
				String writeoffstatdt = InternalBureauObject.get("writeoffstatdt").toString();
				String lastrepmtdt = InternalBureauObject.get("lastrepmtdt").toString();
				String Limit_increase = InternalBureauObject.get("limit_increase").toString().equalsIgnoreCase("true") ? "Y" : "N";
				// added by abhishek for drop5
				String amt_paid_last6mnths = InternalBureauObject.get("AmountPaidInLst6Mnths").toString();
				String interest_rate = InternalBureauObject.get("InterestRate").toString();
				String odtype = InternalBureauObject.get("odtype").toString();
				String paid_installment = "";
				if (totalnoofinstalments != null && remaininginstalments != null && !totalnoofinstalments.equals("") && !remaininginstalments.equals("")) {
					paid_installment = Integer.toString(Integer.parseInt(totalnoofinstalments) - Integer.parseInt(remaininginstalments));
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "paid_installment" + paid_installment);
				}

				String part_settlement_date = "";
				String part_settlement_amount = "";
				String PartSettlementDetails = InternalBureauObject.get("PartSettlementDetails").toString();
				if (PartSettlementDetails != null && !PartSettlementDetails.equals("")) {
					// Rajeshwar: logic change
					part_settlement_date = PartSettlementDetails.substring(0, 10);
					part_settlement_amount = PartSettlementDetails.substring(10, PartSettlementDetails.toString().length());
				}

				String SchemeCardProduct = InternalBureauObject.get(
						"SchemeCardProduct").toString();
				String General_Status = InternalBureauObject.get(
						"General_Status").toString();
				String EmployerType = param_json_object.get(
						"employdetails-employmerType").toString().trim();
				String Internal_WriteOff_Check = InternalBureauObject.get(
						"Internal_WriteOff_Check").toString();
				String Combined_Limit = "";
				String SecuredCard = "";

				String sQueryCombinedLimit = "select Distinct(COMBINEDLIMIT_ELIGIBILITY) from ng_master_cardProduct where code='" + SchemeCardProduct + "'";
				List < List < String >> sQueryCombinedLimitXML = ExecuteSelectQueryGetList(sQueryCombinedLimit);
				try {
					if (sQueryCombinedLimitXML != null && sQueryCombinedLimitXML.size() > 0 && sQueryCombinedLimitXML.get(0) != null) {
						Combined_Limit = sQueryCombinedLimitXML.get(0).get(0)
								.equalsIgnoreCase("1") ? "Y" : "N";
					}
				} catch (Exception e) {
					writePrintStackTrace(e);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "sQueryCombinedLimit" + sQueryCombinedLimit);

				}
				String sQuerySecuredCard = "select count(*) from ng_master_cardProduct where code='" + SchemeCardProduct + "'  and subproduct='SEC'";
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "sQueryCombinedLimit" + sQueryCombinedLimit);
				List < List < String >> sQuerySecuredCardXML = ExecuteSelectQueryGetList(sQuerySecuredCard);
				try {
					if (sQuerySecuredCardXML != null && sQuerySecuredCardXML.size() > 0 && sQuerySecuredCardXML.get(0) != null) {
						SecuredCard = sQuerySecuredCardXML.get(0).get(0)
								.equalsIgnoreCase("0") ? "N" : "Y";
					}
				} catch (Exception e) {
					writePrintStackTrace(e);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "SecuredCard" + SecuredCard);

				}
				if (cifId != null && !cifId.equalsIgnoreCase("") && !cifId.equalsIgnoreCase("null")) {
					add_xml_str = add_xml_str + "<applicant_id>" + cifId + "</applicant_id>";
					add_xml_str = add_xml_str + "<internal_bureau_individual_products_id>" + agreementId + "</internal_bureau_individual_products_id>";
					add_xml_str = add_xml_str + "<type_product>" + product_type + "</type_product>";
					if (writeoffStat.equalsIgnoreCase("cards")) {
						if (SchemeCardProduct.startsWith("LOC")){
							add_xml_str = add_xml_str + "<contract_type>IM</contract_type>";
						}
						else{
						add_xml_str = add_xml_str + "<contract_type>CC</contract_type>";
						}
						//add_xml_str = add_xml_str + "<contract_type>CC</contract_type>";
					}
					/*if (writeoffStat.equalsIgnoreCase("Loans")) {
						add_xml_str = add_xml_str + "<contract_type>PL</contract_type>";
					}*/
					else if ("Loans".equalsIgnoreCase(writeoffStat))
					{
						if (product_type.equalsIgnoreCase("MURABAHA")){
							
							if (SchemeCardProduct.indexOf("AMAL PERSONAL FINANCE")>-1){
								add_xml_str = add_xml_str + "<contract_type>IPL</contract_type>";
							}
							else if (SchemeCardProduct.indexOf("AMAL AUTO FINANCE")>-1){
								add_xml_str = add_xml_str + "<contract_type>IAL</contract_type>";
							}
						}
						else if (product_type.equalsIgnoreCase("AUTO")){
							add_xml_str = add_xml_str + "<contract_type>AL</contract_type>";
						}
						else if (product_type.equalsIgnoreCase("HOME")){
							add_xml_str = add_xml_str + "<contract_type>ML</contract_type>";
						}
						else if (product_type.equalsIgnoreCase("IJARAH")){
							add_xml_str = add_xml_str + "<contract_type>IML</contract_type>";
						}
						else if (product_type.equalsIgnoreCase("PERSONAL")){
							add_xml_str = add_xml_str + "<contract_type>PL</contract_type>";
						}
						else if (product_type.equalsIgnoreCase("HOME IN ONE")){
							add_xml_str = add_xml_str + "<contract_type>HIO</contract_type>";
						}
						else{
							add_xml_str = add_xml_str + "<contract_type>TRADE</contract_type>";
						}
					}
					
					else if ("OVERDRAFT".equalsIgnoreCase(writeoffStat))
					{
						add_xml_str = add_xml_str + "<contract_type>OD</contract_type>";
						
					}
					
					add_xml_str = add_xml_str + "<provider_no>" + "RAKBANK" + "</provider_no>";
					add_xml_str = add_xml_str + "<phase>" + phase + "</phase>";
					add_xml_str = add_xml_str + "<role_of_customer>Primary</role_of_customer>";
					add_xml_str = add_xml_str + "<start_date>" + loan_start_date + "</start_date>";
					add_xml_str = add_xml_str + "<close_date>" + loanmaturitydate + "</close_date>";
					add_xml_str = add_xml_str + "<date_last_updated>" + lastupdatedate + "</date_last_updated>";
					add_xml_str = add_xml_str + "<outstanding_balance>" + outstandingamt + "</outstanding_balance>";
					if (writeoffStat.equalsIgnoreCase("Loans")) {
						add_xml_str = add_xml_str + "<total_amount>" + totalloanamount + "</total_amount>";
					}
					add_xml_str = add_xml_str + "<payments_amount>" + emi + "</payments_amount>";
					add_xml_str = add_xml_str + "<method_of_payment>" + paymentmode + "</method_of_payment>";
					add_xml_str = add_xml_str + "<total_no_of_instalments>" + totalnoofinstalments + "</total_no_of_instalments>";

					// Rajeshwar : changed remaining installment to paid
					// installment
					add_xml_str = add_xml_str + "<no_of_remaining_instalments>" + paid_installment + "</no_of_remaining_instalments>";
					add_xml_str = add_xml_str + "<worst_status>" + writeoffStat + "</worst_status>";
					add_xml_str = add_xml_str + "<worst_status_date>" + writeoffstatdt + "</worst_status_date>";
					if (writeoffStat.equalsIgnoreCase("cards")) {
						add_xml_str = add_xml_str + "<credit_limit>" + CashLimit + "</credit_limit>";
					}
					add_xml_str = add_xml_str + "<overdue_amount>" + overdueamt + "</overdue_amount>";
					add_xml_str = add_xml_str + "<no_of_days_payment_delay>" + nofdayspmtdelay + "</no_of_days_payment_delay>";
					add_xml_str = add_xml_str + "<mob>" + monthsonbook + "</mob>";
					add_xml_str = add_xml_str + "<last_repayment_date>" + lastrepmtdt + "</last_repayment_date>";
					add_xml_str = add_xml_str + "<currently_current>" + currentlycurrent + "</currently_current>";
					add_xml_str = add_xml_str + "<current_utilization>" + currmaxutil + "</current_utilization>";
					add_xml_str = add_xml_str + "<dpd_30_last_6_mon>" + DPD_30_in_last_6_months + "</dpd_30_last_6_mon>";
					add_xml_str = add_xml_str + "<dpd_60p_in_last_18_mon>" + DPD_60_in_last_18_months + "</dpd_60p_in_last_18_mon>";
					// add_xml_str = add_xml_str +
					// "<cards_b_score>"+""+"</cards_b_score>";
					add_xml_str = add_xml_str + "<card_product>" + SchemeCardProduct + "</card_product>";
					add_xml_str = add_xml_str + "<property_value>" + propertyvalue + "</property_value>";
					add_xml_str = add_xml_str + "<disbursal_date>" + loan_disbursal_date + "</disbursal_date>";
					add_xml_str = add_xml_str + "<marketing_code>" + marketingcode + "</marketing_code>";
					add_xml_str = add_xml_str + "<card_expiry_date>" + CardExpiryDate + "</card_expiry_date>";
					add_xml_str = add_xml_str + "<card_upgrade_indicator>" + Limit_increase + "</card_upgrade_indicator>";
					add_xml_str = add_xml_str + "<part_settlement_date>" + part_settlement_date + "</part_settlement_date>";
					add_xml_str = add_xml_str + "<part_settlement_amount>" + part_settlement_amount + "</part_settlement_amount>";
					// add_xml_str = add_xml_str +
					// "<no_of_remaining_instalments>"+remaininginstalments+"</no_of_remaining_instalments>";
					add_xml_str = add_xml_str + "<part_settlement_reason>" + "" + "</part_settlement_reason>";
					add_xml_str = add_xml_str + "<limit_expiry_date>" + "" + "</limit_expiry_date>";
					add_xml_str = add_xml_str + "<no_of_primary_cards>" + CountXML.get(0).get(0) + "</no_of_primary_cards>";
					add_xml_str = add_xml_str + "<no_of_repayments_done>" + remaininginstalments + "</no_of_repayments_done>";
					add_xml_str = add_xml_str + "<card_segment>" + SchemeCardProduct + "</card_segment>";
					add_xml_str = add_xml_str + "<product_type>" + writeoffStat + "</product_type>";
					add_xml_str = add_xml_str + "<product_category>" + SchemeCardProduct + "</product_category>";
					add_xml_str = add_xml_str + "<combined_limit_flag>" + Combined_Limit + "</combined_limit_flag>";
					add_xml_str = add_xml_str + "<secured_card_flag>" + SecuredCard + "</secured_card_flag>";
					add_xml_str = add_xml_str + "<resch_tko_flag>" + Limit_increase + "</resch_tko_flag>";
					add_xml_str = add_xml_str + "<general_status>" + General_Status + "</general_status>";
					add_xml_str = add_xml_str + "<consider_for_obligation>" + Consider_For_Obligations + "</consider_for_obligation>";
					add_xml_str = add_xml_str + "<limit_increase>" + Limit_increase + "</limit_increase>";
					// add_xml_str = add_xml_str +
					// "<card_against_card>"+""+"</card_against_card>";
					// add_xml_str = add_xml_str +
					// "<quick_cash_amount>"+""+"</quick_cash_amount>";
					// add_xml_str = add_xml_str +
					// "<quick_cash_emi>"+""+"</quick_cash_emi>";
					// add_xml_str = add_xml_str +
					// "<take_over_indicator>"+""+"</take_over_indicator>";
					add_xml_str = add_xml_str + "<role>Primary</role>";
					add_xml_str = add_xml_str + "<limit>" + "" + "</limit>";
					add_xml_str = add_xml_str + "<status>" + phase + "</status>";
					// add_xml_str = add_xml_str +
					// "<part_settlement_reason>"+""+"</part_settlement_reason>";
					add_xml_str = add_xml_str + "<emi>" + emi + "</emi>";
					add_xml_str = add_xml_str + "<os_amt>" + outstandingamt + "</os_amt>";
					// add_xml_str = add_xml_str +
					// "<dpd_5_in_last_12mon>"+""+"</dpd_5_in_last_12mon>";
					// add_xml_str = add_xml_str +
					// "<dpd_5_in_last_18mon>"+""+"</dpd_5_in_last_18mon>";
					// add_xml_str = add_xml_str +
					// "<dpd_5_in_last_24mon>"+""+"</dpd_5_in_last_24mon>";
					add_xml_str = add_xml_str + "<dpd_30_in_last_3mon>" + DPD_30_in_last_3_months + "</dpd_30_in_last_3mon>";
					add_xml_str = add_xml_str + "<dpd_30_in_last_6mon>" + DPD_30_in_last_6_months + "</dpd_30_in_last_6mon>";
					add_xml_str = add_xml_str + "<dpd_30_in_last_9mon>" + DPD_30_in_last_9_months + "</dpd_30_in_last_9mon>";
					add_xml_str = add_xml_str + "<dpd_30_in_last_12mon>" + DPD_30_in_last_12_months + "</dpd_30_in_last_12mon>";
					add_xml_str = add_xml_str + "<dpd_30_in_last_18mon>" + DPD_30_in_last_18_months + "</dpd_30_in_last_18mon>";
					add_xml_str = add_xml_str + "<dpd_30_in_last_24mon>" + DPD_30_in_last_24_months + "</dpd_30_in_last_24mon>";
					add_xml_str = add_xml_str + "<dpd_60_in_last_3mon>" + DPD_60_in_last_3_months + "</dpd_60_in_last_3mon>";
					add_xml_str = add_xml_str + "<dpd_60_in_last_6mon>" + DPD_60_in_last_6_months + "</dpd_60_in_last_6mon>";
					add_xml_str = add_xml_str + "<dpd_60_in_last_9mon>" + DPD_60_in_last_9_months + "</dpd_60_in_last_9mon>";
					add_xml_str = add_xml_str + "<dpd_60_in_last_12mon>" + DPD_60_in_last_12_months + "</dpd_60_in_last_12mon>";
					add_xml_str = add_xml_str + "<dpd_60_in_last_18mon>" + DPD_60_in_last_18_months + "</dpd_60_in_last_18mon>";
					add_xml_str = add_xml_str + "<dpd_60_in_last_24mon>" + DPD_60_in_last_24_months + "</dpd_60_in_last_24mon>";
					add_xml_str = add_xml_str + "<dpd_90_in_last_3mon>" + DPD_90_in_last_3_months + "</dpd_90_in_last_3mon>";
					add_xml_str = add_xml_str + "<dpd_90_in_last_6mon>" + DPD_90_in_last_6_months + "</dpd_90_in_last_6mon>";
					add_xml_str = add_xml_str + "<dpd_90_in_last_9mon>" + DPD_90_in_last_9_months + "</dpd_90_in_last_9mon>";
					add_xml_str = add_xml_str + "<dpd_90_in_last_12mon>" + DPD_90_in_last_12_months + "</dpd_90_in_last_12mon>";
					add_xml_str = add_xml_str + "<dpd_90_in_last_18mon>" + DPD_90_in_last_18_months + "</dpd_90_in_last_18mon>";
					add_xml_str = add_xml_str + "<dpd_90_in_last_24mon>" + DPD_90_in_last_24_months + "</dpd_90_in_last_24mon>";
					add_xml_str = add_xml_str + "<dpd_120_in_last_3mon>" + DPD_120_in_last_3_months + "</dpd_120_in_last_3mon>";
					add_xml_str = add_xml_str + "<dpd_120_in_last_6mon>" + DPD_120_in_last_6_months + "</dpd_120_in_last_6mon>";
					add_xml_str = add_xml_str + "<dpd_120_in_last_9mon>" + DPD_120_in_last_9_months + "</dpd_120_in_last_9mon>";
					add_xml_str = add_xml_str + "<dpd_120_in_last_12mon>" + DPD_120_in_last_12_months + "</dpd_120_in_last_12mon>";
					add_xml_str = add_xml_str + "<dpd_120_in_last_18mon>" + DPD_120_in_last_18_months + "</dpd_120_in_last_18mon>";
					add_xml_str = add_xml_str + "<dpd_120_in_last_24mon>" + DPD_120_in_last_24_months + "</dpd_120_in_last_24mon>";
					add_xml_str = add_xml_str + "<dpd_150_in_last_3mon>" + DPD_150_in_last_3_months + "</dpd_150_in_last_3mon>";
					add_xml_str = add_xml_str + "<dpd_150_in_last_6mon>" + DPD_150_in_last_6_months + "</dpd_150_in_last_6mon>";
					add_xml_str = add_xml_str + "<dpd_150_in_last_9mon>" + DPD_150_in_last_9_months + "</dpd_150_in_last_9mon>";
					add_xml_str = add_xml_str + "<dpd_150_in_last_12mon>" + DPD_150_in_last_12_months + "</dpd_150_in_last_12mon>";
					add_xml_str = add_xml_str + "<dpd_150_in_last_18mon>" + DPD_150_in_last_18_months + "</dpd_150_in_last_18mon>";
					add_xml_str = add_xml_str + "<dpd_150_in_last_24mon>" + DPD_150_in_last_24_months + "</dpd_150_in_last_24mon>";
					add_xml_str = add_xml_str + "<dpd_180_in_last_3mon>" + DPD_180_in_last_3_months + "</dpd_180_in_last_3mon>";
					add_xml_str = add_xml_str + "<dpd_180_in_last_6mon>" + DPD_180_in_last_6_months + "</dpd_180_in_last_6mon>";
					add_xml_str = add_xml_str + "<dpd_180_in_last_9mon>" + DPD_180_in_last_9_months + "</dpd_180_in_last_9mon>";
					add_xml_str = add_xml_str + "<dpd_180_in_last_12mon>" + DPD_180_in_last_12_months + "</dpd_180_in_last_12mon>";
					add_xml_str = add_xml_str + "<dpd_180_in_last_18mon>" + "" + "</dpd_180_in_last_18mon>";
					add_xml_str = add_xml_str + "<dpd_180_in_last_24mon>" + DPD_180_in_last_24_months + "</dpd_180_in_last_24mon>";
					add_xml_str = add_xml_str + "<last_temp_limit_exp>" + "" + "</last_temp_limit_exp>";
					add_xml_str = add_xml_str + "<last_per_limit_exp>" + "" + "</last_per_limit_exp>";
					add_xml_str = add_xml_str + "<security_cheque_amt>" + "" + "</security_cheque_amt>";
					add_xml_str = add_xml_str + "<mol_salary_variance>" + "" + "</mol_salary_variance>";
					if (Kompass != null) {
						// Rajeshwar: Commented as Kompass is coming as Y/N from
						// client
						// if(Kompass.equalsIgnoreCase("true")){
						// add_xml_str = add_xml_str +
						// "<kompass>"+"Y"+"</kompass>";
						// }
						// else{
						// add_xml_str = add_xml_str +
						// "<kompass>"+"N"+"</kompass>";
						// }

						add_xml_str = add_xml_str + "<kompass>" + Kompass + "</kompass>";
					}
					add_xml_str = add_xml_str + "<employer_type>" + EmployerType + "</employer_type>";

					/*if (ReqProd.equalsIgnoreCase("Credit Card")) {
						add_xml_str = add_xml_str + "<no_of_paid_installment>" + paid_installment + "</no_of_paid_installment><write_off_amount>" + Internal_WriteOff_Check + "</write_off_amount><company_flag>N</company_flag>";
					} else {*/
						add_xml_str = add_xml_str + "<no_of_paid_installment>" + paid_installment + "</no_of_paid_installment><write_off_amount>" + Internal_WriteOff_Check + "</write_off_amount><company_flag>N</company_flag><type_of_od>" + odtype + "</type_of_od><amt_paid_last6mnths>" + amt_paid_last6mnths + "</amt_paid_last6mnths><topup_indicator>"+Limit_increase+"</topup_indicator><interest_rate>"+interest_rate+"</interest_rate>";
				//	}

					if (i < InternalBureauIndividualArray.size()) {
						add_xml_str = add_xml_str + "</InternalBureauIndividualProducts><InternalBureauIndividualProducts>";
					}

				}
			}
		} catch (Exception e) {
			writePrintStackTrace(e);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
					"Exception occurred in InternalBureauIndividualProducts()" + e.getMessage());
			//e.printStackTrace();
		}
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
				"Internal Bureau Individual Products tag Creation: " + add_xml_str);
		return add_xml_str;
	}

	public String InternalBureauPipelineProducts(String wiName) {
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
				"inside InternalBureauPipelineProducts : ");
		String add_xml_str = "";
		try {
			String sQuery = "SELECT cifid,product_type,custroletype,lastupdatedate,totalamount,totalnoofinstalments,totalloanamount,agreementId,NoOfDaysInPipeline FROM ng_RLOS_CUSTEXPOSE_LoanDetails  with (nolock) where Wi_Name = '" + wiName + "' and  LoanStat = 'Pipeline'";
			JSONArray Array = getData(
					sQuery,
					"cifid,product_type,custroletype,lastupdatedate,totalamount,totalnoofinstalments,totalloanamount,agreementId,NoOfDaysInPipeline");

			for (int i = 0; i < Array.size(); i++) {
				JSONObject Object = (JSONObject) Array.get(i);
				String cifId = Object.get("cifid").toString();
				String Product = Object.get("product_type").toString();
				String role = Object.get("custroletype").toString();
				String lastUpdateDate = Object.get("lastupdatedate").toString();
				String TotAmount = Object.get("totalamount").toString();
				String TotNoOfInstlmnt = Object.get("totalnoofinstalments")
						.toString();
				String TotLoanAmt = Object.get("totalloanamount").toString();
				String agreementId = Object.get("agreementId").toString();
				String NoofDaysinPipeline = Object.get("NoOfDaysInPipeline").toString();

				if (cifId != null && !cifId.equalsIgnoreCase("") && !cifId.equalsIgnoreCase("null")) {
					// add_xml_str = add_xml_str +
					// "<Pipeline_Products_ID>"+""+"</Pipeline_Products_ID>";//
					// to be populated later
					//add_xml_str = add_xml_str + "<InternalBureauPipelineProducts>";
					add_xml_str = add_xml_str + "<applicant_id>" + cifId + "</applicant_id>";
					add_xml_str = add_xml_str + "<internal_bureau_pipeline_products_id>" + agreementId + "</internal_bureau_pipeline_products_id>"; // to be
					// populated
					// later
					add_xml_str = add_xml_str + "<ppl_provider_no>RAKBANK</ppl_provider_no>";
					add_xml_str = add_xml_str + "<ppl_product>" + Product + "</ppl_product>";
					add_xml_str = add_xml_str + "<ppl_type_of_contract>" + "" + "</ppl_type_of_contract>";
					add_xml_str = add_xml_str + "<ppl_phase>PIPELINE</ppl_phase>"; // to be populated later

					add_xml_str = add_xml_str + "<ppl_role>Primary</ppl_role>";
					add_xml_str = add_xml_str + "<ppl_date_of_last_update>" + lastUpdateDate + "</ppl_date_of_last_update>";
					add_xml_str = add_xml_str + "<ppl_total_amount>" + TotAmount + "</ppl_total_amount>";
					add_xml_str = add_xml_str + "<ppl_no_of_instalments>" + TotNoOfInstlmnt + "</ppl_no_of_instalments>";
					add_xml_str = add_xml_str + "<ppl_credit_limit>" + TotLoanAmt + "</ppl_credit_limit>";

					add_xml_str = add_xml_str + "<ppl_no_of_days_in_pipeline>" + NoofDaysinPipeline + "</ppl_no_of_days_in_pipeline><company_flag>N</company_flag>"; // to
					// be
					// populated
					// later
				}

			}
		} catch (Exception e) {
			writePrintStackTrace(e);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
					"Exception occurred in InternalBureauPipelineProducts()" + e.getMessage());
			//e.printStackTrace();
		}
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
				"Internal Bureau Pipeline Products tag Creation: " + add_xml_str);
		return add_xml_str;
	}

	public String ExternalBureauData(String wiName, String cifId,
			String fullName) {
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "inside ExternalBureauData : ");
		String add_xml_str = "";
		try {
			String sQuery = "select CifId,fullnm,TotalOutstanding,TotalOverdue,NoOfContracts,Total_Exposure,WorstCurrentPaymentDelay,Worst_PaymentDelay_Last24Months,Worst_Status_Last24Months,Nof_Records,NoOf_Cheque_Return_Last3,Nof_DDES_Return_Last3Months,Nof_Cheque_Return_Last6,DPD30_Last6Months,(select max(ExternalWriteOffCheck) ExternalWriteOffCheck from ((select convert(int,isNULL(ExternalWriteOffCheck,0)) ExternalWriteOffCheck  from ng_rlos_cust_extexpo_CardDetails where Wi_name  = '" + wiName + "' union select convert(int,isNULL(ExternalWriteOffCheck,0))ExternalWriteOffCheck    from ng_rlos_cust_extexpo_LoanDetails where wi_name  = '" + wiName + "')) as ExternalWriteOffCheck) as External_WriteOff_Check from NG_rlos_custexpose_Derived where wi_name  = '" + wiName + "' and Request_type= 'ExternalExposure'";
			JSONArray OutputXML = getData(
					sQuery,
					"CifId,fullnm,TotalOutstanding,TotalOverdue,NoOfContracts,Total_Exposure,WorstCurrentPaymentDelay,Worst_PaymentDelay_Last24Months,Worst_Status_Last24Months,Nof_Records,NoOf_Cheque_Return_Last3,Nof_DDES_Return_Last3Months,Nof_Cheque_Return_Last6,DPD30_Last6Months,External_WriteOff_Check");

			String AecbHistQuery = "select isnull(max(AECBHistMonthCnt),0) as AECBHistMonthCnt from ( select MAX(AECBHistMonthCnt) as AECBHistMonthCnt  from ng_rlos_cust_extexpo_CardDetails where  wi_name  = '" + wiName + "' union select Max(AECBHistMonthCnt) from ng_rlos_cust_extexpo_LoanDetails where wi_name  = '" + wiName + "') as AECBHistMonthCnt";
			JSONArray AecbHistQueryData = getData(AecbHistQuery,
					"AECBHistMonthCnt");
			/*
			 * float TotOutstandingAmt = 0.0f; float TotOverdueAmt = 0.0f; float
			 * TotalExposure = 0.0f; for(int i = 0; i<OutputXML.size();i++){
			 * if(OutputXML.get(i).get(1)!=null &&
			 * !OutputXML.get(i).get(1).isEmpty() &&
			 * !OutputXML.get(i).get(1).equals("") &&
			 * !OutputXML.get(i).get(1).equalsIgnoreCase("null") ){
			 * TotOutstandingAmt = TotOutstandingAmt +
			 * Float.parseFloat(OutputXML.get(i).get(1)); }
			 * if(OutputXML.get(i).get(2)!=null &&
			 * !OutputXML.get(i).get(2).isEmpty() &&
			 * !OutputXML.get(i).get(2).equals("") &&
			 * !OutputXML.get(i).get(2).equalsIgnoreCase("null") ){
			 * TotOverdueAmt = TotOverdueAmt +
			 * Float.parseFloat(OutputXML.get(i).get(2)); }
			 * if(OutputXML.get(i).get(3)!=null &&
			 * !OutputXML.get(i).get(3).isEmpty() &&
			 * !OutputXML.get(i).get(3).equals("") &&
			 * !OutputXML.get(i).get(3).equalsIgnoreCase("null") ){
			 * TotalExposure = TotalExposure +
			 * Float.parseFloat(OutputXML.get(i).get(3)); } }
			 */
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "AecbHistQueryData.size() : "+AecbHistQueryData.size());
			String AecbCount = ((JSONObject) AecbHistQueryData.get(0)).get("AECBHistMonthCnt").toString();
			if ("0".equalsIgnoreCase(AecbCount)) {

				String CifId = cifId;
				String fullnm = fullName;
				String TotalOutstanding = "";
				String TotalOverdue = "";
				String NoOfContracts = "";
				String Total_Exposure = "";
				String WorstCurrentPaymentDelay = "";
				String Worst_PaymentDelay_Last24Months = "";
				String Worst_Status_Last24Months = "";
				String Nof_Records = "";
				String NoOf_Cheque_Return_Last3 = "";
				String Nof_DDES_Return_Last3Months = "";
				String Nof_Cheque_Return_Last6 = "";
				String DPD30_Last6Months = "";
				//String Internal_WriteOff_Check = "";

				add_xml_str = add_xml_str + "<applicant_id>" + CifId + "</applicant_id>";

				add_xml_str = add_xml_str + "<full_name>" + fullnm + "</full_name>";
				add_xml_str = add_xml_str + "<total_out_bal>" + TotalOutstanding + "</total_out_bal>";

				add_xml_str = add_xml_str + "<total_overdue>" + TotalOverdue + "</total_overdue>";
				add_xml_str = add_xml_str + "<no_default_contract>" + NoOfContracts + "</no_default_contract>";
				add_xml_str = add_xml_str + "<total_exposure>" + Total_Exposure + "</total_exposure>";
				add_xml_str = add_xml_str + "<worst_curr_pay>" + WorstCurrentPaymentDelay + "</worst_curr_pay>";
				add_xml_str = add_xml_str + "<worst_curr_pay_24>" + Worst_PaymentDelay_Last24Months + "</worst_curr_pay_24>";
				add_xml_str = add_xml_str + "<worst_status_24>" + Worst_Status_Last24Months + "</worst_status_24>";

				add_xml_str = add_xml_str + "<no_of_rec>" + Nof_Records + "</no_of_rec>";
				add_xml_str = add_xml_str + "<cheque_return_3mon>" + NoOf_Cheque_Return_Last3 + "</cheque_return_3mon>";
				add_xml_str = add_xml_str + "<dds_return_3mon>" + Nof_DDES_Return_Last3Months + "</dds_return_3mon>";
				add_xml_str = add_xml_str + "<cheque_return_6mon>" + Nof_Cheque_Return_Last6 + "</cheque_return_6mon>";
				add_xml_str = add_xml_str + "<dds_return_6mon>" + DPD30_Last6Months + "</dds_return_6mon>";
				add_xml_str = add_xml_str + "<prod_external_writeoff_amount>" + "" + "</prod_external_writeoff_amount>";

				add_xml_str = add_xml_str + "<no_months_aecb_history>" + AecbCount + "</no_months_aecb_history>";

				add_xml_str = add_xml_str + "<company_flag>N</company_flag>";

				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
						"Internal liab tag Cration: " + add_xml_str);
				return add_xml_str;
			} else {
				for (int i = 0; i < OutputXML.size(); i++) {
					JSONObject resultObject = (JSONObject) OutputXML.get(i);
					String CifId = cifId;
					String fullnm = resultObject.get("fullnm").toString();
					String TotalOutstanding = resultObject.get(
							"TotalOutstanding").toString();
					String TotalOverdue = resultObject.get("TotalOverdue")
							.toString();
					String NoOfContracts = resultObject.get("NoOfContracts")
							.toString();
					String Total_Exposure = resultObject.get("Total_Exposure")
							.toString();
					String WorstCurrentPaymentDelay = resultObject.get(
							"WorstCurrentPaymentDelay").toString();
					String Worst_PaymentDelay_Last24Months = resultObject.get(
							"Worst_PaymentDelay_Last24Months").toString();
					String Worst_Status_Last24Months = resultObject.get(
							"Worst_Status_Last24Months").toString();
					String Nof_Records = resultObject.get("Nof_Records")
							.toString();
					String NoOf_Cheque_Return_Last3 = resultObject.get(
							"NoOf_Cheque_Return_Last3").toString();
					String Nof_DDES_Return_Last3Months = resultObject.get(
							"Nof_DDES_Return_Last3Months").toString();
					String Nof_Cheque_Return_Last6 = resultObject.get(
							"Nof_Cheque_Return_Last6").toString();
					String DPD30_Last6Months = resultObject.get(
							"DPD30_Last6Months").toString();
					String External_WriteOff_Check = resultObject.get(
							"External_WriteOff_Check").toString();

					add_xml_str = add_xml_str + "<applicant_id>" + CifId + "</applicant_id>";
					add_xml_str = add_xml_str + "<full_name>" + fullnm + "</full_name>";
					add_xml_str = add_xml_str + "<total_out_bal>" + TotalOutstanding + "</total_out_bal>";
					add_xml_str = add_xml_str + "<total_overdue>" + TotalOverdue + "</total_overdue>";
					add_xml_str = add_xml_str + "<no_default_contract>" + NoOfContracts + "</no_default_contract>";
					add_xml_str = add_xml_str + "<total_exposure>" + Total_Exposure + "</total_exposure>";
					add_xml_str = add_xml_str + "<worst_curr_pay>" + WorstCurrentPaymentDelay + "</worst_curr_pay>";
					add_xml_str = add_xml_str + "<worst_curr_pay_24>" + Worst_PaymentDelay_Last24Months + "</worst_curr_pay_24>";
					add_xml_str = add_xml_str + "<worst_status_24>" + Worst_Status_Last24Months + "</worst_status_24>";
					add_xml_str = add_xml_str + "<no_of_rec>" + Nof_Records + "</no_of_rec>";
					add_xml_str = add_xml_str + "<cheque_return_3mon>" + NoOf_Cheque_Return_Last3 + "</cheque_return_3mon>";
					add_xml_str = add_xml_str + "<dds_return_3mon>" + Nof_DDES_Return_Last3Months + "</dds_return_3mon>";
					add_xml_str = add_xml_str + "<cheque_return_6mon>" + Nof_Cheque_Return_Last6 + "</cheque_return_6mon>";
					add_xml_str = add_xml_str + "<dds_return_6mon>" + DPD30_Last6Months + "</dds_return_6mon>";
					add_xml_str = add_xml_str + "<prod_external_writeoff_amount>" + External_WriteOff_Check + "</prod_external_writeoff_amount>";
					add_xml_str = add_xml_str + "<no_months_aecb_history>" + AecbCount + "</no_months_aecb_history>";
					add_xml_str = add_xml_str + "<company_flag>N</company_flag>";
					
					if(i < OutputXML.size()){
						add_xml_str= add_xml_str + "</ExternalBureau><ExternalBureau>";
					}
				}
			}
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
					"External Bureau Data tag Creation: " + add_xml_str);
		} catch (Exception e) {
			writePrintStackTrace(e);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
					"Exception occurred in externalBureauData()" + e.getMessage());
			//e.printStackTrace();
		}
		return add_xml_str;
	}

	public String ExternalBouncedCheques(String wiName, String cif) {
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "inside ExternalBouncedCheques : ");
		String add_xml_str = "";
		try {
			String sQuery = "SELECT cifid,number,amount,reasoncode,returndate,providerno FROM ng_rlos_cust_extexpo_ChequeDetails  with (nolock) where Wi_Name = '" + wiName + "' and Request_Type = 'ExternalExposure'";
			JSONArray resultArray = getData(sQuery,
					"cifid,number,amount,reasoncode,returndate,providerno");
			for (int i = 0; i < resultArray.size(); i++) {
				JSONObject resultObject = (JSONObject) resultArray.get(i);
				String CifId = cif;
				String chqNo = resultObject.get("number").toString();
				String Amount = resultObject.get("amount").toString();
				String Reason = resultObject.get("reasoncode").toString();
				String returnDate = resultObject.get("returndate").toString();
				String providerNo = resultObject.get("providerno").toString();

				add_xml_str = add_xml_str + "<applicant_id>" + CifId + "</applicant_id>";
				add_xml_str = add_xml_str + "<external_bounced_cheques_id>" + "" + "</external_bounced_cheques_id>";
				add_xml_str = add_xml_str + "<bounced_cheque>" + "" + "</bounced_cheque>";
				add_xml_str = add_xml_str + "<cheque_no>" + chqNo + "</cheque_no>";
				add_xml_str = add_xml_str + "<amount>" + Amount + "</amount>";
				add_xml_str = add_xml_str + "<reason>" + Reason + "</reason>";
				add_xml_str = add_xml_str + "<return_date>" + returnDate + "</return_date>"; // to be populated later
				add_xml_str = add_xml_str + "<provider_no>" + providerNo + "</provider_no><company_flag>N</company_flag>"; // to
				// be
				// populated
				// later
			}
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
					"External Bounced Cheques tag Creation: " + add_xml_str);
		} catch (Exception e) {
			writePrintStackTrace(e);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
					"Exception occurred in ExternalBouncedCheques()" + e.getMessage());
			//e.printStackTrace();
		}
		return add_xml_str;
	}

	public String ExternalBureauIndividualProducts(String wiName, String cif,
			String ReqProd,String cac_bankname) {
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
				"inside ExternalBureauIndividualProducts : ");
		String add_xml_str = "";
		try {
			String sQuery = "select CifId,AgreementId,LoanType,ProviderNo,LoanStat,CustRoleType,LoanApprovedDate,LoanMaturityDate,OutstandingAmt,TotalAmt,PaymentsAmt,TotalNoOfInstalments,RemainingInstalments,WriteoffStat,WriteoffStatDt,CreditLimit,OverdueAmt,NofDaysPmtDelay,MonthsOnBook,lastrepmtdt,IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last12Months,AECBHistMonthCnt,DPD5_Last3Months,'' as qc_Amnt,'' as Qc_emi,'' as Cac_indicator,Take_Over_Indicator,isnull(Consider_For_Obligations,'true') as Consider_For_Obligations,case when IsDuplicate= '1' then 'Y' else 'N' end from ng_rlos_cust_extexpo_LoanDetails where wi_name= '" + wiName + "'  and LoanStat != 'Pipeline' union select CifId,CardEmbossNum,CardType,ProviderNo,CardStatus,CustRoleType,StartDate,ClosedDate,CurrentBalance,'' as col6,PaymentsAmount,NoOfInstallments,'' as col5,WriteoffStat,WriteoffStatDt,CashLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,lastrepmtdt,IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last12Months,AECBHistMonthCnt,DPD5_Last3Months,qc_amt,qc_emi,isNull(CAC_Indicator,'false'),Take_Over_Indicator,isnull(Consider_For_Obligations,'true') as Consider_For_Obligations,case when IsDuplicate= '1' then 'Y' else 'N' end from ng_rlos_cust_extexpo_CardDetails where wi_name  =  '" + wiName + "' and cardstatus != 'Pipeline' union select CifId,AcctId,AcctType,AcctId,AcctStat,CustRoleType,StartDate,ClosedDate,OutStandingBalance,TotalAmount,PaymentsAmount,'','',WriteoffStat,WriteoffStatDt,CreditLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,'',IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last12Months,AECBHistMonthCnt,DPD5_Last3Months,'','','','',isnull(Consider_For_Obligations,'true') as Consider_For_Obligations,case when IsDuplicate= '1' then 'Y' else 'N' end from ng_rlos_cust_extexpo_AccountDetails where AcctType='Overdraft' and wi_name  =  '" + wiName + "'";
			JSONArray resultArray = getData(
					sQuery,
					"CifId,AgreementId,LoanType,ProviderNo,LoanStat,CustRoleType,LoanApprovedDate,LoanMaturityDate,OutstandingAmt,TotalAmt,PaymentsAmt,TotalNoOfInstalments,RemainingInstalments,WriteoffStat,WriteoffStatDt,CreditLimit,OverdueAmt,NofDaysPmtDelay,MonthsOnBook,lastrepmtdt,IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last18Months,AECBHistMonthCnt,DPD5_Last3Months,qc_Amnt,Qc_emi,Cac_indicator,Take_Over_Indicator,Consider_For_Obligations");

			for (int i = 0; i < resultArray.size(); i++) {
				JSONObject resultObject = (JSONObject) resultArray.get(i);
				String CifId = cif;
				String AgreementId = resultObject.get("AgreementId").toString();
				String ContractType = "";
				try {
					ContractType = resultObject.get("LoanType").toString();
					String cardquery = "select code from ng_master_contract_type where description='" + ContractType + "'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
							"ExternalBureauIndividualProducts sQuery" + sQuery);
					List < List < String >> cardqueryXML = ExecuteSelectQueryGetList(cardquery);
					ContractType = cardqueryXML.get(0).get(0);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
							"ExternalBureauIndividualProducts ContractType" + ContractType);
				} catch (Exception e) {
					writePrintStackTrace(e);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
							"ExternalBureauIndividualProducts ContractType Exception" + e);

					ContractType = resultObject.get("LoanType").toString();
				}
				// Rajeshwar: to change
				if (ContractType.equalsIgnoreCase("CreditCard")) {
					ContractType = "CC";
				}
				String provider_no = resultObject.get("ProviderNo").toString();
				String phase = resultObject.get("LoanStat").toString()
						.startsWith("C") ? "C" : "A";
				String CustRoleType = resultObject.get("CustRoleType")
						.toString();
				String sQueryCustRoleType = "select code from ng_master_role_of_customer where Description='"+CustRoleType+"'";
			//	RLOS.mLogger.info("CustRoleType"+sQueryCustRoleType);
				List<List<String>> sQueryCustRoleTypeXML = ExecuteSelectQueryGetList(sQueryCustRoleType);
				try{
					if(sQueryCustRoleTypeXML!=null && sQueryCustRoleTypeXML.size()>0 && sQueryCustRoleTypeXML.get(0)!=null){
						CustRoleType=sQueryCustRoleTypeXML.get(0).get(0);
					}
				}
				catch(Exception e){
					

				}
				
				String start_date = resultObject.get("LoanApprovedDate")
						.toString();
				String close_date = resultObject.get("LoanMaturityDate")
						.toString();
				String OutStanding_Balance = resultObject.get("OutstandingAmt")
						.toString();
				String TotalAmt = resultObject.get("TotalAmt").toString();
				String PaymentsAmt = resultObject.get("PaymentsAmt").toString();
				String TotalNoOfInstalments = resultObject.get(
						"TotalNoOfInstalments").toString();
				String RemainingInstalments = resultObject.get(
						"RemainingInstalments").toString();
				String WorstStatus = resultObject.get("WriteoffStat")
						.toString();
				String WorstStatusDate = resultObject.get("WriteoffStatDt")
						.toString();
				String CreditLimit = resultObject.get("CreditLimit").toString();
				String OverdueAmt = resultObject.get("OverdueAmt").toString();
				String NofDaysPmtDelay = resultObject.get("NofDaysPmtDelay")
						.toString();
				String MonthsOnBook = resultObject.get("MonthsOnBook")
						.toString();
				String last_repayment_date = resultObject.get("lastrepmtdt")
						.toString();
				String DPD60Last12Months = resultObject.get(
						"DPD60_Last12Months").toString();
				String AECBHistMonthCnt = resultObject.get("AECBHistMonthCnt")
						.toString();
				String DPD30Last6Months = resultObject.get("DPD30_Last6Months")
						.toString();
				String currently_current = resultObject.get("IsCurrent")
						.toString();
				String current_utilization = resultObject.get("CurUtilRate")
						.toString();
				String delinquent_in_last_3months = resultObject.get(
						"DPD5_Last3Months").toString();
				String QC_Amt = resultObject.get("qc_Amnt").toString();
				String QC_emi = resultObject.get("Qc_emi").toString();
				String CAC_Indicator = resultObject.get("Cac_indicator")
						.toString();
				if (CAC_Indicator != null && !(CAC_Indicator.equalsIgnoreCase(""))) {
					if (CAC_Indicator.equalsIgnoreCase("true")) {
						CAC_Indicator = "Y";
					} else {
						CAC_Indicator = "N";
					}
				}
				String TakeOverIndicator = resultObject.get(
						"Take_Over_Indicator").toString();
				if (TakeOverIndicator != null && !(TakeOverIndicator.equalsIgnoreCase(""))) {
					if (TakeOverIndicator.equalsIgnoreCase("true")) {
						TakeOverIndicator = "Y";
					} else {
						TakeOverIndicator = "N";
					}
				}
				String consider_for_obligation = resultObject.get(
						"Consider_For_Obligations").toString();
				if (consider_for_obligation != null && !(consider_for_obligation.equalsIgnoreCase(""))) {
					if (consider_for_obligation.equalsIgnoreCase("true")) {
						consider_for_obligation = "Y";
					} else {
						consider_for_obligation = "N";
					}
				}
				
				
				add_xml_str = add_xml_str + "<applicant_id>" + CifId + "</applicant_id>";
				add_xml_str = add_xml_str + "<external_bureau_individual_products_id>" + AgreementId + "</external_bureau_individual_products_id>";
				add_xml_str = add_xml_str + "<contract_type>" + ContractType + "</contract_type>";
				add_xml_str = add_xml_str + "<provider_no>" + provider_no + "</provider_no>";
				add_xml_str = add_xml_str + "<phase>" + phase + "</phase>";
				add_xml_str = add_xml_str + "<role_of_customer>" + CustRoleType + "</role_of_customer>";
				add_xml_str = add_xml_str + "<start_date>" + start_date + "</start_date>";

				add_xml_str = add_xml_str + "<close_date>" + close_date + "</close_date>";
				add_xml_str = add_xml_str + "<outstanding_balance>" + OutStanding_Balance + "</outstanding_balance>";
				add_xml_str = add_xml_str + "<total_amount>" + TotalAmt + "</total_amount>";
				add_xml_str = add_xml_str + "<payments_amount>" + PaymentsAmt + "</payments_amount>";
				add_xml_str = add_xml_str + "<total_no_of_instalments>" + TotalNoOfInstalments + "</total_no_of_instalments>";
				add_xml_str = add_xml_str + "<no_of_remaining_instalments>" + RemainingInstalments + "</no_of_remaining_instalments>";
				add_xml_str = add_xml_str + "<worst_status>" + WorstStatus + "</worst_status>";
				add_xml_str = add_xml_str + "<worst_status_date>" + WorstStatusDate + "</worst_status_date>";

				add_xml_str = add_xml_str + "<credit_limit>" + CreditLimit + "</credit_limit>";
				add_xml_str = add_xml_str + "<overdue_amount>" + OverdueAmt + "</overdue_amount>";
				add_xml_str = add_xml_str + "<no_of_days_payment_delay>" + NofDaysPmtDelay + "</no_of_days_payment_delay>";
				add_xml_str = add_xml_str + "<mob>" + MonthsOnBook + "</mob>";
				add_xml_str = add_xml_str + "<last_repayment_date>" + last_repayment_date + "</last_repayment_date>";
				if (currently_current != null && currently_current.equalsIgnoreCase("1"))
					add_xml_str = add_xml_str + "<currently_current>Y</currently_current>";
				else {
					add_xml_str = add_xml_str + "<currently_current>N</currently_current>";
				}
				add_xml_str = add_xml_str + "<current_utilization>" + current_utilization + "</current_utilization>";
				add_xml_str = add_xml_str + "<dpd_30_last_6_mon>" + DPD30Last6Months + "</dpd_30_last_6_mon>";

				add_xml_str = add_xml_str + "<dpd_60p_in_last_12_mon>" + DPD60Last12Months + "</dpd_60p_in_last_12_mon>";
				add_xml_str = add_xml_str + "<no_months_aecb_history>" + AECBHistMonthCnt + "</no_months_aecb_history>";
				add_xml_str = add_xml_str + "<delinquent_in_last_3months>" + delinquent_in_last_3months + "</delinquent_in_last_3months>";
				add_xml_str = add_xml_str + "<clean_funded>" + "" + "</clean_funded>";
				add_xml_str = add_xml_str + "<cac_indicator>" + CAC_Indicator + "</cac_indicator>";
				add_xml_str = add_xml_str + "<qc_emi>" + QC_emi + "</qc_emi>";
				// add_xml_str = add_xml_str +
				// "<qc_amount>"+QC_Amt+"</qc_amount><company_flag>N</company_flag>";
				if (ReqProd.equalsIgnoreCase("Credit Card")) {
					add_xml_str = add_xml_str + "<qc_amount>" + QC_Amt + "</qc_amount><company_flag>N</company_flag><cac_bank_name>"+cac_bankname+"</cac_bank_name><consider_for_obligation>" + consider_for_obligation + "</consider_for_obligation>";
				} else {
					add_xml_str = add_xml_str + "<qc_amount>" + QC_Amt + "</qc_amount><company_flag>N</company_flag><cac_bank_name>"+cac_bankname+"</cac_bank_name><take_over_indicator>" + TakeOverIndicator + "</take_over_indicator><consider_for_obligation>" + consider_for_obligation + "</consider_for_obligation>";
				}
				
				if (i < resultArray.size()) {
				add_xml_str = add_xml_str + "</ExternalBureauIndividualProducts><ExternalBureauIndividualProducts>";
			}
				
			}
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
					"External Bureau Individual Products tag Creation: " + add_xml_str);
		} catch (Exception e) {
			writePrintStackTrace(e);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
					"Exception occurred in ExternalBouncedCheques()" + e.getMessage());
			//e.printStackTrace();
		}
		return add_xml_str;
	}

	public String ExternalBureauManualAddIndividualProducts(JSONObject param_json_object) {

		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Inside ExternalBureauManualAddIndividualProducts");
		JSONArray array = (JSONArray) param_json_object.get("liability_addition");
		int Man_liab_row_count = array.size(); // formObject.getLVWRowCount("cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Man_liab_row_count list size: " + Man_liab_row_count);
		String applicant_id = param_json_object.get("customerDetails-cif")
				.toString(); // formObject.getNGValue("cmplx_Customer_CIFNO");
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
				"ExternalBureauIndividualProducts applicant_id: " + applicant_id);
		String add_xml_str = "";
		Date currentDate=new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String modifiedDate= sdf.format(currentDate);
		String close_date= plusyear(modifiedDate,4,0,0,"yyyy-MM-dd");
		
		if (Man_liab_row_count != 0) {

			for (int i = 0; i < Man_liab_row_count; i++) {
				JSONObject obj = (JSONObject) array.get(i);
				String Type_of_Contract = obj.get("Type of Contract")
						.toString(); // 0
				String Limit = obj.get("Limit").toString(); // formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",
				// i, 1); //0
				
				String EMI = obj.get("EMI").toString();
				String Take_over_Indicator = obj.get("Take Over Indicator")
						.toString(); // formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",
				// i, 2); //0
				if (Take_over_Indicator != null || !(Take_over_Indicator.equalsIgnoreCase(""))) {
					if (Take_over_Indicator.equalsIgnoreCase("true")) {
						Take_over_Indicator = "Y";
					} else {
						Take_over_Indicator = "N";
					}
				}
				String Take_over_amount = obj.get("Takeover Amount").toString(); // formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",
				// i,
				// 3);
				// //0
				String cac_Indicator = obj.get("CAC Indicator").toString(); // formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",
				// i,
				// 4);
				// //0
				if (cac_Indicator != null || !(cac_Indicator.equalsIgnoreCase(""))) {
					if (cac_Indicator.equalsIgnoreCase("true")) {
						cac_Indicator = "Y";
					} else {
						cac_Indicator = "N";
					}
				}
				String Qc_amt = obj.get("QC Amount").toString(); // formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",
				// i, 5); //0
				String Qc_Emi = obj.get("QC EMI").toString(); // formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",
				// i, 6); //0
				if (cac_Indicator.equalsIgnoreCase("true")) {
					cac_Indicator = "Y";
				} else {
					cac_Indicator = "N";
				}
				String consider_for_obligation = obj.get(
						"Consider for Obligations").toString(); // (formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",
				// i,
				if (consider_for_obligation != null || !(consider_for_obligation.equalsIgnoreCase(""))) {
					if (consider_for_obligation.equalsIgnoreCase("true")) {
						consider_for_obligation = "Y";
					} else {
						consider_for_obligation = "N";
					}
				}
				
				// 7).equalsIgnoreCase("true")?"Y":"N");
				// //0
				String remarks = obj.get("Remarks").toString(); // formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",
				// i, 8); //0
				// String MOB =
				// formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",
				// i, 9); //0
				String Utilization = obj.get("Utilization").toString(); // formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",
				// i,
				// 11);
				// //0
				String OutStanding = obj.get("Outstanding").toString(); // formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",
				// i,
				// 12);
				// //0
				String mob = obj.get("MOB").toString(); // formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",
				// i, 10);
				String delinquent_in_last_3months = obj.get(
						"Delinquest In Last 3 Months").toString().equalsIgnoreCase("false") ? "0" : "1"; // (formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",
				// i, 13).equalsIgnoreCase("true")?"1":"0");
				String dpd_30_last_6_mon = obj.get("DPD30+ In Last 6 Months")
						.toString().equalsIgnoreCase("false") ? "0" : "1"; // (formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",
				// i,
				// 14).equalsIgnoreCase("true")?"1":"0");
				String dpd_60p_in_last_18_mon = obj.get(
						"DPD60+ In Last 18 Months").toString().equalsIgnoreCase("false") ? "0" : "1"; // (formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",
				// i, 15).equalsIgnoreCase("true")?"1":"0");
				add_xml_str = add_xml_str + "<applicant_id>" + applicant_id + "</applicant_id>";
				add_xml_str = add_xml_str + "<external_bureau_individual_products_id></external_bureau_individual_products_id>";
				add_xml_str = add_xml_str + "<contract_type>" + Type_of_Contract + "</contract_type>";
				add_xml_str = add_xml_str + "<provider_no></provider_no>";
				add_xml_str = add_xml_str + "<phase>A</phase>";
				add_xml_str = add_xml_str + "<role_of_customer>A</role_of_customer>";
				add_xml_str = add_xml_str + "<start_date>" + modifiedDate + "</start_date>";

				add_xml_str = add_xml_str + "<close_date>"+close_date+"</close_date>";
				add_xml_str = add_xml_str + "<outstanding_balance>" + OutStanding + "</outstanding_balance>";
				//add_xml_str = add_xml_str + "<outstanding_balance>1000000</outstanding_balance>";
				if (!Type_of_Contract.equalsIgnoreCase("01")){
				add_xml_str = add_xml_str + "<total_amount>" + Limit + "</total_amount>";
				}
				add_xml_str = add_xml_str + "<payments_amount>" + EMI + "</payments_amount>";
				add_xml_str = add_xml_str + "<total_no_of_instalments></total_no_of_instalments>";
				add_xml_str = add_xml_str + "<no_of_remaining_instalments></no_of_remaining_instalments>";
				add_xml_str = add_xml_str + "<worst_status></worst_status>";
				add_xml_str = add_xml_str + "<worst_status_date></worst_status_date>";
				if (Type_of_Contract.equalsIgnoreCase("01")){
				add_xml_str = add_xml_str + "<credit_limit>" + Limit + "</credit_limit>";
				}
				add_xml_str = add_xml_str + "<overdue_amount></overdue_amount>";
				add_xml_str = add_xml_str + "<no_of_days_payment_delay></no_of_days_payment_delay>";
				add_xml_str = add_xml_str + "<mob>" + mob + "</mob>";
				//add_xml_str = add_xml_str + "<mob>06.00</mob>";
				add_xml_str = add_xml_str + "<last_repayment_date></last_repayment_date>";

				add_xml_str = add_xml_str + "<currently_current>N</currently_current>";

				add_xml_str = add_xml_str + "<current_utilization>" + Utilization + "</current_utilization>";
				//add_xml_str = add_xml_str + "<current_utilization>10</current_utilization>";
				add_xml_str = add_xml_str + "<dpd_30_last_6_mon>" + dpd_30_last_6_mon + "</dpd_30_last_6_mon>";

				add_xml_str = add_xml_str + "<dpd_60p_in_last_18_mon>" + dpd_60p_in_last_18_mon + "</dpd_60p_in_last_18_mon>";
				add_xml_str = add_xml_str + "<no_months_aecb_history></no_months_aecb_history>";
				add_xml_str = add_xml_str + "<delinquent_in_last_3months>" + delinquent_in_last_3months + "</delinquent_in_last_3months>";
				add_xml_str = add_xml_str + "<clean_funded>" + "" + "</clean_funded>";
				add_xml_str = add_xml_str + "<cac_indicator>" + cac_Indicator + "</cac_indicator>";
				add_xml_str = add_xml_str + "<qc_emi>" + Qc_amt + "</qc_emi>";
				add_xml_str = add_xml_str + "<qc_amount>" + Qc_Emi + "</qc_amount><company_flag>N</company_flag><cac_bank_name>"+param_json_object.get("eligibilitydetails-takeoverbank").toString().trim()+"</cac_bank_name><take_over_indicator>" + Take_over_Indicator + "</take_over_indicator><consider_for_obligation>" + consider_for_obligation + "</consider_for_obligation><duplicate_flag>N</duplicate_flag>";
				// added by abhishek to solve proc-5228e
				if (i < Man_liab_row_count) {
				add_xml_str = add_xml_str + "</ExternalBureauIndividualProducts><ExternalBureauIndividualProducts>";
			}
			}
		}
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Internal liab tag Creation: " + add_xml_str);
		return add_xml_str;
	}

	public String ExternalBureauPipelineProducts(String wiName, String cif) {
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
				"inside ExternalBureauPipelineProducts : ");
		String add_xml_str = "";
		try {
			String sQuery = "select AgreementId,ProviderNo,LoanType,LoanDesc,CustRoleType,Datelastupdated,TotalAmt,TotalNoOfInstalments,'' as col1,NoOfDaysInPipeline,isnull(Consider_For_Obligations,'true') as Consider_For_Obligations,case when IsDuplicate= '1' then 'Y' else 'N' end as Duplicate from ng_rlos_cust_extexpo_LoanDetails where wi_name  =  '" + wiName + "' and LoanStat = 'Pipeline' union select CardEmbossNum,ProviderNo,CardTypeDesc,CardType,CustRoleType,LastUpdateDate,'' as col2,NoOfInstallments,TotalAmount,NoOfDaysInPipeLine,isnull(Consider_For_Obligations,'true') as Consider_For_Obligations,case when IsDuplicate= '1' then 'Y' else 'N' end as Duplicate from ng_rlos_cust_extexpo_CardDetails where wi_name  =  '" + wiName + "' and cardstatus = 'Pipeline'";
			JSONArray resultArray = getData(
					sQuery,
					"AgreementId,ProviderNo,LoanType,LoanDesc,CustRoleType,Datelastupdated,TotalAmt,TotalNoOfInstalments,col1,NoOfDaysInPipeline,Consider_For_Obligations,Duplicate");
			String cifId = cif;

			for (int i = 0; i < resultArray.size(); i++) {
				JSONObject resultObject = (JSONObject) resultArray.get(i);
				String agreementID = resultObject.get("AgreementId").toString();
				String ProviderNo = resultObject.get("ProviderNo").toString();
				String contractType = resultObject.get("LoanDesc").toString();
				String productType = resultObject.get("LoanType").toString();
				String role = resultObject.get("CustRoleType").toString();
				String lastUpdateDate = resultObject.get("Datelastupdated")
						.toString();
				String TotAmt = resultObject.get("Total_Amount").toString();
				String noOfInstalmnt = resultObject.get("TotalNoOfInstalments")
						.toString();
				String creditLimit = resultObject.get("col1").toString();
				String noOfDayinPpl = resultObject.get("NoOfDaysInPipeline")
						.toString();
				String Consider_For_Obligations = resultObject.get("Consider_For_Obligations")
						.toString();
				String Duplicate = resultObject.get("Duplicate")
						.toString();

				add_xml_str = add_xml_str + "<applicant_ID>" + cifId + "</applicant_ID>";
				add_xml_str = add_xml_str + "<external_bureau_pipeline_products_id>" + agreementID + "</external_bureau_pipeline_products_id>";
				add_xml_str = add_xml_str + "<ppl_provider_no>" + ProviderNo + "</ppl_provider_no>";
				add_xml_str = add_xml_str + "<ppl_type_of_contract>" + contractType + "</ppl_type_of_contract>";
				add_xml_str = add_xml_str + "<ppl_type_of_product>" + productType + "</ppl_type_of_product>";
				add_xml_str = add_xml_str + "<ppl_phase>" + "PIPELINE" + "</ppl_phase>";
				add_xml_str = add_xml_str + "<ppl_role>" + role + "</ppl_role>";
				add_xml_str = add_xml_str + "<ppl_date_of_last_update>" + lastUpdateDate + "</ppl_date_of_last_update>";
				add_xml_str = add_xml_str + "<ppl_total_amount>" + TotAmt + "</ppl_total_amount>";
				add_xml_str = add_xml_str + "<ppl_no_of_instalments>" + noOfInstalmnt + "</ppl_no_of_instalments>";
				add_xml_str = add_xml_str + "<ppl_credit_limit>" + creditLimit + "</ppl_credit_limit>";
				add_xml_str = add_xml_str + "<ppl_no_of_days_in_pipeline>" + noOfDayinPpl + "</ppl_no_of_days_in_pipeline><company_flag>N</company_flag><consider_for_obligation>" + Consider_For_Obligations + "</consider_for_obligation><ppl_duplicate_flag>" + Duplicate + "</ppl_duplicate_flag>"; // to
				// be
				// populated
				// later
			}
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
					"External Bureau Pipeline Products tag Creation: " + add_xml_str);
		} catch (Exception e) {
			writePrintStackTrace(e);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
					"Exception occurred in ExternalBouncedCheques()" + e.getMessage());
			//e.printStackTrace();
		}
		return add_xml_str;
	}


	private JSONObject removeEmptyJsontags(JSONObject json) {
		Set < String > keys = json.keySet();
		JSONObject newjson = new JSONObject();
		for (String key: keys) {

			if (json.get(key) != null && !json.get(key).toString().trim().isEmpty()) {

				newjson.put(key, removeEmptyXmlTags(json.get(key).toString().trim()));
				//System.out.println("done");
			}
		}
		return newjson;
	}
	private static String removeEmptyXmlTags(String xml) {
		String[] patterns = new String[] {
				// This will remove empty elements that look like <ElementName/>
				"\\s*<\\w+/>",
				// This will remove empty elements that look like <ElementName></ElementName>
				"\\s*<\\w+></\\w+>",
				// This will remove empty elements that look like 
				// <ElementName>
				// </ElementName>
				"\\s*<\\w+>\n*\\s*</\\w+>"
		};
		xml = xml.replaceFirst("\\/", "/");
		String xmltoreturn = "";
		for (String pattern: patterns) {
			Matcher matcher = Pattern.compile(pattern).matcher(xml);
			xmltoreturn = matcher.replaceAll("");
		}
		return xmltoreturn;
		//  System.out.println(xml);

	}


	public ResponseEntity getIdList(IntegrationEntity requestobject)
			throws Exception {
		String xPath = "";
		String tablename = "";
		String columnname = "";
		Node DbMapping = null;
		StreamedResource toreturn = null;
		Connection conn = nemfDbconnection();
		ResponseEntity response = new ResponseEntity();
		JSONObject responsejson = new JSONObject();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			xPath = "./*/DbMapping";
			DbMapping = configuration.searchObjectGraph(xPath,
					configuration.getConfigurationXmlText()).item(0);
			xPath = "./Key[@name = 'INTEGRATION_XML_TABLE_NAME']";
			tablename = configuration.searchObjectGraph(xPath,
					(Element) DbMapping).item(0).getTextContent().trim();
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "xPath=" + xPath);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "tablename=" + tablename);
			xPath = "./Key[@name = 'MOBILITY_FIELD_MAPPING_COLUMN']";
			columnname = configuration.searchObjectGraph(xPath,
					(Element) DbMapping).item(0).getTextContent().trim();
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "columnname=" + columnname);
			if (conn != null) {
				String query = "SELECT " + columnname + " FROM " + tablename + " WHERE Call_name=" + "'" + requestobject.getCallname() + "'";
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "QUERY TO HIT : " + query);
				pstmt = conn.prepareStatement(query);
				rs = pstmt.executeQuery();
				if (rs != null) {
					ArrayList < String > recordarray = new ArrayList < String > ();
					while (rs.next()) {
						if (rs.getString(columnname) != null) {
							recordarray.add(rs.getString(columnname));
						}
					}
					responsejson.put("idarray", recordarray.toString());
					responsejson.put("status", "success");
					responsejson.put("message", "success");
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Response:  " + responsejson.toString());
					response.setOutputjsonstring(responsejson.toString());

				} else {
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
							"ERROR WHILE EXECUTING QUERY:  ");
				}
			}
		} catch (Exception e) {
			writePrintStackTrace(e);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
					"INSIDE getIdList EXCEPTION BLOCK :  ");
			//e.printStackTrace();
			throw new Exception("G-Error While Opening Connection!!");
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				//e.printStackTrace();
				writePrintStackTrace(e);
				throw new Exception("G-Error While Closing Connection!!");
			}
		}
		return response;
	}

	public ArrayList < String > getOperationName(String tablename,
			String subproduct, String employment) throws Exception {
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "INSIDE: getOperationName ");
		Connection conn = nemfDbconnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList < String > toreturn = new ArrayList < String > ();
		try {
			if (conn != null) {
				String query = "SELECT DISTINCT(OPERATION_NAME) FROM " + tablename + " WHERE Sub_Product ='" + subproduct + "' AND Employment='" + employment + "'";
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "QUERY TO HIT : " + query);
				pstmt = conn.prepareStatement(query);
				rs = pstmt.executeQuery();
				if (rs != null) {
					ArrayList < String > recordarray = new ArrayList < String > ();
					while (rs.next()) {
						if (rs.getString("OPERATION_NAME") != null) {
							toreturn.add(rs.getString("OPERATION_NAME"));
						}
					}
					// toreturn.add(recordarray.toString());
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Response:  " + toreturn.toString());

				} else {
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
							"ERROR WHILE EXECUTING QUERY:  ");
				}
			}
		} catch (Exception e) {
			writePrintStackTrace(e);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
					"INSIDE getOperationName EXCEPTION BLOCK :  ");
			//e.printStackTrace();
			throw new Exception("G-Error While Opening Connection!!");
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				writePrintStackTrace(e);
				//e.printStackTrace();
				throw new Exception("G-Error While Closing Connection!!");
			}
		}
		return toreturn;
	}

	public  Connection nemfDbconnection() {
		String xPath = "";
		String dburl = "";
		String dbdriver = "";
		String source = "";
		String datasource = "";
		Node DbMapping = null;
		InitialContext localInitialContext = null;
		DataSource localDataSource = null;
		PreparedStatement pstmt = null;
		Connection connection = null;
		LogMe
		.logMe(LogMe.LOG_LEVEL_DEBUG,
				"Inside Get OF DB Connection method");
		try {
			xPath = "./*/DbMapping";
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, xPath + "\n");
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, configuration.searchObjectGraph(
					xPath, configuration.getConfigurationXmlText()) + "\n");
			DbMapping = configuration.searchObjectGraph(xPath,
					configuration.getConfigurationXmlText()).item(0);
			xPath = "./Key[@name = 'SOURCE']";
			source = configuration
					.searchObjectGraph(xPath, (Element) DbMapping).item(0)
					.getTextContent().trim();
			if (source.equalsIgnoreCase("DATASOURCE")) {
				xPath = "./Key[@name = 'DATASOURCE']";
				datasource = configuration.searchObjectGraph(xPath,
						(Element) DbMapping).item(0).getTextContent().trim();
			//	LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, xPath + "\n" + datasource);
			} else if (source.equalsIgnoreCase("DBCONNECTION")) {
				xPath = "./Key[@name = 'DBURL']";
				dburl = configuration.searchObjectGraph(xPath,
						(Element) DbMapping).item(0).getTextContent().trim();
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "xPath=" + xPath);
			//	LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "dburl=" + dburl);
				xPath = "./Key[@name = 'DBDRIVER']";
				dbdriver = configuration.searchObjectGraph(xPath,
						(Element) DbMapping).item(0).getTextContent().trim();
			//	LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, xPath + "\n" + dburl + "\n" + dbdriver);
			}
			if (source.equalsIgnoreCase("DBCONNECTION")) {
				Class.forName(dbdriver).newInstance();
				connection = DriverManager.getConnection(dburl);
			} else if (source.equalsIgnoreCase("DATASOURCE")) {
				localInitialContext = new InitialContext();
				localDataSource = (DataSource) localInitialContext
						.lookup(datasource);
				connection = localDataSource.getConnection();
			}

			if (connection != null) {
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
						"Connection Estb. with NEMF Database");
			} else {
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
						"Error while Opening Connection with OF Db");
			}
		} catch (Exception e) {
			writePrintStackTrace(e);
			//e.printStackTrace();
			return null;
		}
		return connection;
	}

	public void handleRepeatedTags(JSONObject object, String Callname,
			String response) {
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Inside handleRepeatedTags");
		String xPath = "";
		Node arraytags = null;
		String parenttag = "";
		HashMap < String, HashMap < String, HashMap < String, String >>> callnameparentchildmap = new HashMap < String, HashMap < String, HashMap < String, String >>> ();
		HashMap < String, HashMap < String, String >> parentchildmap = new HashMap < String, HashMap < String, String >> ();

		try {
			xPath = "./*/Arraytags";
			arraytags = configuration.searchObjectGraph(xPath,
					configuration.getConfigurationXmlText()).item(0);
			xPath = "./" + Callname + "";
			Node callnamenode = configuration.searchObjectGraph(xPath,
					(Element) arraytags).item(0);
			xPath = "./ParentTag";
			NodeList parentnodes = configuration.searchObjectGraph(xPath,
					(Element) callnamenode);
			for (int k = 0; k < parentnodes.getLength(); k++) {
				Node parentnode = parentnodes.item(k);
				parenttag = ((Element) parentnode).getAttribute("name");
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "PARENT TAG: " + parenttag);
				xPath = "./ChildTag";
				NodeList childnodes = configuration.searchObjectGraph(xPath,
						(Element) parentnode);
				HashMap < String, String > xmlapkmap = new HashMap < String, String > ();
				for (int j = 0; j < childnodes.getLength(); j++) {
					new HashMap < String, String > ();
					Node childnode = childnodes.item(j);
					String xmltag = ((Element) childnode)
							.getAttribute("xmlTag");
					String apkid = ((Element) childnode).getAttribute("apkId");
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "XML_TAG : APKID " + xmltag + ":" + apkid);
					xmlapkmap.put(xmltag, apkid);
				}
				parentchildmap.put(parenttag, xmlapkmap);
			}
			// callnameparentchildmap.put(Callname,parentchildmap);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "PARENT CHILD MAP  : " + parentchildmap.toString());
			XMLParser parser = new XMLParser();
			parser.setInputXML(response);
			if (!Callname.trim().equals("DECTECH")) {
				response = parser.getValueOf("MQ_RESPONSE_XML").trim()
						.replaceAll("> <", "><");
			}

			Set < String > parentset = parentchildmap.keySet();
			for (String parent: parentset) {
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "PARSING FOR PARENT : " + parent);
				JSONArray array = new JSONArray();
				Set < String > xmltags = parentchildmap.get(parent).keySet();
				DocumentBuilder builder = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder();
				InputSource src = new InputSource();
				src.setCharacterStream(new StringReader(response));
				Document doc = builder.parse(src);
				NodeList nodes = doc.getElementsByTagName(parent);
				for (int i = 0; i < nodes.getLength(); i++) {
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "NO OF PARENT XML BLOCKS  : " + nodes.getLength());
					Element element = (Element) nodes.item(i);
					JSONObject tempobject = new JSONObject();
					NodeList childnodes = element.getChildNodes();
					for (int j = 0; j < childnodes.getLength(); j++) {
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "NO OF CHILD NODES  : " + childnodes.getLength());
						if (childnodes != null) {
							Node childnode = childnodes.item(j);
							System.out.println(childnode.getNodeName());
							if (xmltags.contains(childnode.getNodeName())) {
								tempobject.put(parentchildmap.get(parent).get(childnode.getNodeName()), childnode.getTextContent());
							}
						}
					}
					array.add(tempobject);
				}
				object.put(parent, array);
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "RETURNING FROM handleRepeatedTags" + object.toString());
			}
		} catch (Exception e) {
			writePrintStackTrace(e);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "INSIDE handleRepeatedTags EXCEPTION BLOCK : " + e.getMessage());
			//e.printStackTrace();
		}
	}

	public HashMap < String, String > ExecuteSelectquery(String query)
			throws Exception {

		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Inside ExecuteDbquery Block");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		HashMap < String, String > map = new HashMap < String, String > ();
		int count;
		conn = nemfDbconnection();
		try {
			if (conn != null) {
				String Sql = query;
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "QUERY TO HIT : " + query);
				pstmt = conn.prepareStatement(query);
				rs = pstmt.executeQuery();
				if (rs != null) {
					while (rs.next()) {
						map.put(rs.getString("acctid"), rs.getString("CifId"));
					}
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "RECORDS FROM DB : " + map.toString());
					// System.out.println("RECORDS FROM DB : "+ map.toString());
					return map;
				} else {
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
							"ERROR WHILE EXECUTING QUERY:  ");
				}

			} else {
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
						"ERROR WHILE OPENING CONNECTION ");
			}
		} catch (Exception e) {
			writePrintStackTrace(e);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
					"INSIDE EXECUTE DBQUERY EXCEPTION BLOCK : " + e.getMessage());
			//e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				writePrintStackTrace(e);
				//e.printStackTrace();
			}
		}
		return map;
	}

	public JSONArray getData(String query, String columns) throws Exception {
		// TODO Auto-generated method stub
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Inside getData Block");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		// HashMap<String,List<String>> map=new HashMap<String,List<String>>();
		JSONArray recordArray = new JSONArray();
		conn = nemfDbconnection();
		try {
			if (conn != null) {
				String Sql = query;
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Column Name : " + columns);
				ArrayList < String > columnarray = new ArrayList < String > (Arrays
						.asList(columns.split(",")));
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "QUERY TO HIT : " + query);
				pstmt = conn.prepareStatement(query);
				rs = pstmt.executeQuery();
				if (rs != null) {
					while (rs.next()) {
						JSONObject temp = new JSONObject();
						for (String column: columnarray) {
							if (rs.getString(column) != null) {
								temp.put(column, rs.getString(column));
							} else {
								temp.put(column, "");
							}
						}
						recordArray.add(temp);
					}
					// for(String column : columnarray){
					// map.put(columns, new LinkedList<String>());
					// }
					//						
					//						 
					// while(rs.next()){
					//						
					// for (String columnName : columnarray) {
					// List<String> columnDataList = map.get(columnName);
					// columnDataList.add(rs.getString(columnName));
					// map.put(columnName, columnDataList);
					// }
					//						
					// }
					// LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"RECORDS FROM DB : "+
					// map.toString());
					// //System.out.println("RECORDS FROM DB : "+
					// map.toString());
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
							"Result getData Method: " + recordArray);
					return recordArray;
				} else {
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
							"ERROR WHILE EXECUTING QUERY:  ");
				}

			} else {
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
						"ERROR WHILE OPENING CONNECTION ");
			}
		} catch (Exception e) {
			writePrintStackTrace(e);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
					"INSIDE getData EXCEPTION BLOCK : " + e.getMessage());
			//e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				writePrintStackTrace(e);
				//e.printStackTrace();
			}
		}
		return recordArray;
	}

	public void handleFinancialSummaryResponse(String result, JSONObject responseobject, String operationtype) {
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "INSIDE handleFinancialSummaryResponse : ");
		String xPath = "";
		Node financialcallnode = null;
		XMLParser parser = new XMLParser();
		try {
			if (operationtype.equalsIgnoreCase("SALDET")) {
				xPath = "./*/SALDET";
				financialcallnode = configuration.searchObjectGraph(xPath, configuration.getConfigurationXmlText()).item(0);
				xPath = "./Mapping";
				NodeList mappingnodes = configuration.searchObjectGraph(xPath, (Element) financialcallnode);
				HashMap < String, String > xmlapkmap = new HashMap < String, String > ();
				for (int k = 0; k < mappingnodes.getLength(); k++) {
					Node node = mappingnodes.item(k);
					String xmltag = ((Element) node).getAttribute("xmlTag");
					String apkid = ((Element) node).getAttribute("apkId");
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, xmltag + " : " + apkid);
					xmlapkmap.put(xmltag, apkid);
				}
				Set < String > xmltags = xmlapkmap.keySet();
				parser.setInputXML(result);
				for (String tag: xmltags) {
					responseobject.put(xmlapkmap.get(tag), parser.getValueOf(tag));
				}
			} else if (operationtype.equalsIgnoreCase("AVGBALDET")) {
				Double avg = 0.00;
				parser.setInputXML(result);
				parser.setInputXML(result);
				XMLParser newxmlParser = new XMLParser();
				newxmlParser.setInputXML(parser.getValueOf("FinancialSummaryRes"));
				int count = (newxmlParser.getNoOfFields("AvgBalanceDtls"));
				XMLParser innerparser = new XMLParser();
				String value = "";
				for (int i = 1; i <= count; i++) {
					if (i == 1) {
						value = newxmlParser.getNextValueOf("AvgBalanceDtls");
						innerparser.setInputXML(value);
						avg += Double.parseDouble(newxmlParser.getValueOf("AvgBalance").trim());
					} else {
						value = newxmlParser.getNextValueOf("AvgBalanceDtls");
						innerparser.setInputXML(value);
						avg += Double.parseDouble(newxmlParser.getValueOf("AvgBalance").trim());
					}
				}
				responseobject.put("income-salaried-avg-salary", avg / count);
			}
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "RESPONSE OBJECT IN handleFinancialSummaryResponse" + responseobject.toString());
		} catch (Exception e) {
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "INSIDE handleFinancialSummaryResponse EXCEPTION " + e.getMessage());
			writePrintStackTrace(e);
			//e.printStackTrace();
		}
	}

	@Override
	public ResponseEntity getAlocData(IntegrationEntity requestobject)
			throws Exception {
		ResponseEntity toreturn = new ResponseEntity();
		try {
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "INSIDE getAlocData Method");
			JSONObject responseObject = new JSONObject();
			String callname = requestobject.getOperationtype();
			JSONParser parser_json = new JSONParser();
			JSONObject param_json_object = (JSONObject) parser_json
					.parse(requestobject.getInputjsonstring());
			String query = "select distinct(EMPR_NAME),EMPLOYER_CODE,INCLUDED_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,EMPLOYER_CATEGORY_PL,COMPANY_STATUS_CC,COMPANY_STATUS_PL,main_Employer_code from NG_RLOS_ALOC_OFFLINE_DATA";
			String query1 = "select distinct top 100 (EMPR_NAME),EMPLOYER_CODE,INCLUDED_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,EMPLOYER_CATEGORY_PL,COMPANY_STATUS_CC,COMPANY_STATUS_PL,main_Employer_code from NG_RLOS_ALOC_OFFLINE_DATA";
			if (callname.trim().equalsIgnoreCase("select")) {
				query = query + " where " + "EMPLOYER_CODE = '" + param_json_object.get("employer_code") + "'";

			} else if (callname.trim().equalsIgnoreCase("search")) {
				int count = 1;
				if (!param_json_object.get("employer_name").toString()
						.isEmpty()) {
					query = query + " where EMPR_NAME like'" + param_json_object.get("employer_name") + "%'";
					count++;
				}
				if (!param_json_object.get("employer_code").toString()
						.isEmpty()) {
					//count++;
					query = query + " where " + " EMPLOYER_CODE Like'" + param_json_object.get("employer_code") + "%'";
					if (count == 2) {
						query = query + " where EMPR_NAME like'" + param_json_object.get("employer_name") + "%' or EMPLOYER_CODE Like'" + param_json_object.get("employer_code") + "%'";

					}
					// LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Query to Fire: " +
					// query);
				}
				if (param_json_object.get("employer_name").toString().isEmpty() && param_json_object.get("employer_code").toString()
						.isEmpty()) {
					query = query1;
				}
			}
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Query to Fire: " + query);
			JSONArray alocdetails_datavalues = getData(
					query,
					"EMPR_NAME,EMPLOYER_CODE,INCLUDED_IN_PL_ALOC,INCLUDED_IN_CC_ALOC,INDUSTRY_SECTOR,INDUSTRY_MACRO,INDUSTRY_MICRO,CONSTITUTION,NAME_OF_FREEZONE_AUTHORITY,EMPLOYER_CATEGORY_PL,COMPANY_STATUS_CC,COMPANY_STATUS_PL,main_Employer_code");
			responseObject.put("AlocDetails", alocdetails_datavalues);
			toreturn.setOutputjsonstring(responseObject.toString());
		} catch (Exception e) {
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
					"INSIDE getAlocData EXCEPTION BLOCk " + e.getMessage());
			writePrintStackTrace(e);
			//e.printStackTrace();
		}
		return toreturn;
	}

	public void cardInstallmentCall(String baseurl, JSONObject paramJson,
			String custexpourl, String isproxyenabled, String proxyIP,
			String proxyport, String proxyuser, String proxypassword) {
		try {
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
					"INSIDE cardInstallmentCall Method");
			JSONArray cardnumArray = getData(
					"select max(CardEmbossNum) as CardEmbossNum from ng_RLOS_CUSTEXPOSE_CardDetails  where CifId IN (select distinct cifid from ng_RLOS_CUSTEXPOSE_CardDetails where Wi_Name ='" + paramJson.get("processId") + "' and  (SchemeCardProd = 'LOC PREFERRED' or SchemeCardProd = 'LOC STANDARD' or CardType = 'LOC PREFERRED' or CardType = 'LOC STANDARD')) and Wi_Name='" + paramJson.get("processId") + "' group by CifId",
					"cardembossnum");
			if (cardnumArray.size() > 0) {
				JSONObject cardobject = (JSONObject) cardnumArray.get(0);
				String url = baseurl + "&request_name=CARD_INSTALLMENT_DETAILS&param_json={\"CIFID\":\"" + paramJson.get("customerDetails-cif") + "\",\"CardCRNNumber\":\"" + cardobject.get("cardembossnum") + "\"," + "\"CardNumber\":\"" + cardobject.get("cardembossnum") + "\"," + "\"Customer_Type\":\"Individual_CIF\"}";
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "URL TO HIT : " + url);
				Call callObject = new Call();
				String result = callObject.request(url, isproxyenabled,
						proxyIP, proxyport, proxyuser, proxypassword);
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Result : " + result);
				HashMap < String, String > headerprop = new HashMap < String, String > ();
				User user = (User) ThreadUtils.getThreadVariable("user");
				headerprop.put("SeesionId", user.getAuthenticationToken());
				headerprop.put("is_mobility", "Y");
				headerprop.put("request_name", "CARD_INSTALLMENT_DETAILS");
				headerprop.put("result", result);
				headerprop
				.put("wi_name", paramJson.get("processId").toString());
				headerprop.put("prod", "");
				headerprop.put("subprod", "");
				headerprop.put("cifId", paramJson.get("customerDetails-cif")
						.toString());
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CARD_INSTALLMENT_DETAILS" + "\n HEADER PROPERTY : = " + headerprop.toString());
				callObject.postCall(custexpourl, headerprop);
			} else {
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
						"NO DATA RECIEVED FOR THE QUERY");
			}
		} catch (Exception e) {
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
					"cardInstallmentCall Method EXCEPTION BLOCK : " + e.getMessage());
			writePrintStackTrace(e);
			//e.printStackTrace();
		}
	}

	public HashMap < String, String > prepareXmlDbMap(String callname) {
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "In prepareMapForWrldCheck Method ");
		HashMap < String, String > xmldbMap = new HashMap < String, String > ();
		try {
			String dbcolumn = "";
			String xmltag = "";
			String xPath = "./*/XmlDbMapping";
			// HashMap<String,String> xmldbMap=new HashMap<String, String>();
			Node arraytags = configuration.searchObjectGraph(xPath,
					configuration.getConfigurationXmlText()).item(0);
			xPath = "./" + callname + "";
			Node callnamenode = configuration.searchObjectGraph(xPath,
					(Element) arraytags).item(0);
			xPath = "./Mapping";
			NodeList mappings = configuration.searchObjectGraph(xPath,
					(Element) callnamenode);
			for (int k = 0; k < mappings.getLength(); k++) {
				Node mapnode = mappings.item(k);
				dbcolumn = ((Element) mapnode).getAttribute("dbcolumn");
				xmltag = ((Element) mapnode).getAttribute("xmlTag");
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "DB COLUMN==> " + dbcolumn + "==>" + xmltag);
				xmldbMap.put(xmltag, dbcolumn);
			}
		} catch (Exception e) {
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
					"prepareXmlDbMap Method EXCEPTION BLOCK : " + e.getMessage());
			writePrintStackTrace(e);
			//e.printStackTrace();
		}
		return xmldbMap;
	}

	// gaurav-s 08082017
	public String prepQueryforWC(String callname, String xml, String Tablename) {
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "INSIDE prepQueryforWC ");
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "XML TO PARSE " + xml);
		String query = "";
		try {
			String columns = "";
			String values = "";
			HashMap < String, String > xmldbMap = prepareXmlDbMap(callname);
			Set < String > xmltags = xmldbMap.keySet();
			XMLParser parser = new XMLParser();
			HashMap < String, String > columnValueMap = new HashMap < String, String > ();
			parser.setInputXML(xml);
			for (String tag: xmltags) {
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "For Tag " + tag);
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "XMLTAG==>VALUE" + tag + "==>" + parser.getValueOf(tag));
				columns = columns + xmldbMap.get(tag) + ",";
				values = values + "'" + parser.getValueOf(tag) + "',";
			}
			query = "INSERT INTO " + Tablename + " (" + columns.substring(0, columns.lastIndexOf(",")) + ") VALUES (" + values.substring(0, values.lastIndexOf(",")) + ")";
		} catch (Exception e) {
			LogMe
			.logMe(LogMe.LOG_LEVEL_DEBUG,
					"prepQueryforWC Method EXCEPTION BLOCK : " + e.getMessage());
			writePrintStackTrace(e);
			//e.printStackTrace();
		}
		return query;
	}

	// For world check
	public String ExecuteDbqueryReturn(String query) throws Exception {
		// TODO Auto-generated method stub

		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Inside ExecuteDbquery Block");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection connection = null;
		String toreturn = null;
		try {
			connection = nemfDbconnection();
			if (connection != null) {
				PreparedStatement statement = connection.prepareStatement(
						query, Statement.RETURN_GENERATED_KEYS);
				statement.executeUpdate();
				ResultSet generatedKeys = statement.getGeneratedKeys();
				generatedKeys.next();
				toreturn = String.valueOf(generatedKeys.getLong(1));
			}
		} catch (Exception e) {
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
					"ExecuteDbqueryReturn Method EXCEPTION BLOCK : " + e.getMessage());
			writePrintStackTrace(e);
			//e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				writePrintStackTrace(e);
				//e.printStackTrace();
			}
		}
		return toreturn;
	}

	// gaurav-s 08082017
	public void ExecuteDbquery(String query) throws Exception {
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Inside ExecuteDbquery Block");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		conn = nemfDbconnection();

		int count;
		try {
			if (conn != null) {
				String Sql = query;
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "QUERY TO HIT : " + query);
				pstmt = conn.prepareStatement(query);
				count = pstmt.executeUpdate();
				if (count > 0) {
					LogMe
					.logMe(LogMe.LOG_LEVEL_DEBUG,
							"SUCCESSFULLY EXECUTED ");
				} else {
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
							count + " record affected ");
				}
			} else {
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
						"ERROR WHILE OPENING CONNECTION ");
			}
		} catch (Exception e) {
			LogMe
			.logMe(LogMe.LOG_LEVEL_DEBUG,
					"ExecuteDbquery Method EXCEPTION BLOCK : " + e.getMessage());
			writePrintStackTrace(e);
			//e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				writePrintStackTrace(e);
				//e.printStackTrace();
			}
		}
	}

	public  List < List < String >> ExecuteSelectQueryGetList(String query)
			throws Exception {

		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
				"Inside ExecuteSelectQueryGetList Method");
		List < List < String >> result = new ArrayList < List < String >> ();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		conn = nemfDbconnection();
		try {
			if (conn != null) {
				String Sql = query;
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "QUERY TO HIT : " + query);
				pstmt = conn.prepareStatement(query);
				rs = pstmt.executeQuery();
				int numcols = rs.getMetaData().getColumnCount();

				while (rs.next()) {
					List < String > row = new ArrayList < String > (numcols);
					int i = 1;
					while (i <= numcols) { // don't skip the last column, use <=
						row.add(rs.getString(i++));
					}
					result.add(row); // add it to the result
				}
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
						"ExecuteSelectQueryGetList Method Result: " + result);
			} else {
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
						"ERROR WHILE OPENING CONNECTION ");
			}
		} catch (Exception e) {
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
					"ExecuteSelectQueryGetList Method EXCEPTION BLOCK : " + e.getMessage());
			writePrintStackTrace(e);
			//e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				writePrintStackTrace(e);
				//e.printStackTrace();
			}
		}
		return result;
	}

	public void setCollectionSummaryResposne(JSONObject responseObject,
			JSONObject inputjson) {



		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
		"\n Inside Collection Summary Db call for response: ");
		JSONArray loandetails_datavalues;
		try {
			loandetails_datavalues = getData(
					"select 'ng_RLOS_CUSTEXPOSE_LoanDetails' as table_name,CifId,Liability_type as LiabilityType,"
					+ "LoanType as ProductType,SchemeCardProd as TypeofCardLoan,AgreementId,TotalLoanAmount as LimitAmtFin,NextInstallmentAmt as EMI,General_Status as GeneralStatus,Limit_Increase as LimitIncrease,"
					+ "TotalOutstandingAmt as OutstandingAmount,Consider_For_Obligations as ConsiderForObligations,InterestRate,MonthsOnBook,RemainingInstalments as NoOfRepaymentDone,'' as TypeofOD,PreviousLoanTAI,PreviousLoanDBR from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where Wi_Name = '"
					+ inputjson.get("processId").toString()
					+ "' and (Request_Type = 'InternalExposure' or Request_Type = 'CollectionsSummary') and LoanStat not in ('Pipeline','C') order by Liability_type desc",
			"table_name,CifId,LiabilityType,ProductType,TypeofCardLoan,AgreementId,LimitAmtFin,EMI,GeneralStatus,LimitIncrease,OutstandingAmount,ConsiderForObligations,InterestRate,MonthsOnBook,NoOfRepaymentDone,TypeofOD,PreviousLoanTAI,PreviousLoanDBR");

			responseObject.put("loandetails", loandetails_datavalues);

			JSONArray fundingaccgrid_datavalues = getData(
					"select AcctId,AcctType,AcctNm,AccountOpenDate,AcctSegment,AcctSubSegment,AcctStat,CreditGrade FROM ng_RLOS_CUSTEXPOSE_AcctDetails  with (nolock) where Wi_Name = '"
					+ inputjson.get("processId").toString() + "'",
			"AcctId,AcctType,AcctNm,AccountOpenDate,AcctSegment,AcctSubSegment,AcctStat,CreditGrade");
			responseObject.put("fundinggrid", fundingaccgrid_datavalues);
			// gaurav-s changed on 03/08/2017 added cardInstalment call

			/*JSONArray carddetails_datavalues = getData(
					"select 'ng_RLOS_CUSTEXPOSE_CardDetails' as table_name,A.CifId,Liability_type as LiabilityType,"
					+ "CardType as ProductType,SchemeCardProd as TypeofCardLoan,CardEmbossNum as AgreementId, CreditLimit as LimitAmtFin, PaymentsAmount as EMI,General_Status as GeneralStatus,"
					+ "Limit_Increase as LimitIncrease,OutstandingAmt as OutstandingAmount,Consider_For_Obligations as ConsiderForObligations,ISNULL(b.InterestRate,'') as InterestRate,MonthsOnBook,ISNULL((CAST(INSTALMENTpERIOD AS INT)-CAST(rEMAININGemi AS INT)),'') as NoOfRepaymentDone,'' as TypeofOD,'' as PreviousLoanTAI,'' as PreviousLoanDBR from ng_RLOS_CUSTEXPOSE_CardDetails a left join ng_RLOS_CUSTEXPOSE_CardInstallmentDetails  b with (nolock) on CardEmbossNum=replace(CardNumber,'I','') and a.Wi_Name=b.Wi_Name and a.SchemeCardProd  in ('LOC STANDARD','LOC PREFERRED') and (a.Request_Type = 'InternalExposure' or a.Request_Type = 'CollectionsSummary' or b.Request_Type='CARD_INSTALLMENT_DETAILS') where  a.Wi_Name ='"
					+ inputjson.get("processId").toString() + "'",
			"table_name,CifId,LiabilityType,ProductType,TypeofCardLoan,AgreementId,LimitAmtFin,EMI,GeneralStatus,LimitIncrease,OutstandingAmount,ConsiderForObligations,InterestRate,MonthsOnBook,NoOfRepaymentDone,TypeofOD,PreviousLoanTAI,PreviousLoanDBR");*/
			
			// changed by abhishek on 27/02/2018
			
			JSONArray carddetails_datavalues = getData(
					"select distinct 'ng_RLOS_CUSTEXPOSE_CardDetails' as table_name,A.CifId,Liability_type as LiabilityType,CardType as ProductType,SchemeCardProd as TypeofCardLoan, CardEmbossNum as AgreementId, CreditLimit as LimitAmtFin,  (case when (a.SchemeCardProd = 'LOC PREFERRED' or a.SchemeCardProd = 'LOC STANDARD') then isnull(b.MonthlyAmount,0) ELSE ISnull(PaymentsAmount,0) End) as EMI , General_Status as GeneralStatus ,Limit_Increase as LimitIncrease,OutstandingAmt as OutstandingAmount, Consider_For_Obligations as ConsiderForObligations,ISNULL(b.InterestRate,'') as InterestRate,MonthsOnBook, ISNULL((CAST(INSTALMENTpERIOD AS INT)-CAST(rEMAININGemi AS INT)),'') as NoOfRepaymentDone,'' as TypeofOD,'' as PreviousLoanTAI,'' as PreviousLoanDBR from ng_RLOS_CUSTEXPOSE_CardDetails a with (nolock) left join ng_RLOS_CUSTEXPOSE_CardInstallmentDetails  b with (nolock) on CardEmbossNum=replace(CardNumber,'I','') and a.Wi_Name=b.Wi_Name and a.SchemeCardProd  in ('LOC STANDARD','LOC PREFERRED') and (a.Request_Type = 'InternalExposure' or a.Request_Type = 'CollectionsSummary' or b.Request_Type='CARD_INSTALLMENT_DETAILS') where  a.Wi_Name ='"
					+ inputjson.get("processId").toString() + "' and CustRoleType !='Secondary' and CardStatus !='C' order by Liability_type desc",
			"table_name,CifId,LiabilityType,ProductType,TypeofCardLoan,AgreementId,LimitAmtFin,EMI,GeneralStatus,LimitIncrease,OutstandingAmount,ConsiderForObligations,InterestRate,MonthsOnBook,NoOfRepaymentDone,TypeofOD,PreviousLoanTAI,PreviousLoanDBR");

			responseObject.put("carddetails", carddetails_datavalues);

			JSONArray accountdetails_datavalues = getData(
					"select 'ng_RLOS_CUSTEXPOSE_AcctDetails' as table_name,CifId,Account_Type as LiabilityType,'OverDraft' as ProductType,ODDesc as TypeofCardLoan,AcctId as AgreementId,SanctionLimit as LimitAmtFin,'' as EMI,case when WriteoffStat='Y' then 'P2' else 'P6' end as GeneralStatus,'' as LimitIncrease,ClearBalanceAmount as OutstandingAmount,Consider_For_Obligations as ConsiderForObligations,'' as InterestRate,MonthsOnBook as MonthsOnBook,'' as NoOfRepaymentDone,ODType as TypeofOD,'' as PreviousLoanTAI,'' as PreviousLoanDBR from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where Wi_Name  ='"
					+inputjson.get("processId").toString()+"'  and ODType != ''",
			"table_name,CifId,LiabilityType,ProductType,TypeofCardLoan,AgreementId,LimitAmtFin,EMI,GeneralStatus,LimitIncrease,OutstandingAmount,ConsiderForObligations,InterestRate,MonthsOnBook,NoOfRepaymentDone,TypeofOD,PreviousLoanTAI,PreviousLoanDBR");

			responseObject.put("accountdetails", accountdetails_datavalues);
			//gaurav-s union ng_RLOS_CUSTEXPOSE_LoanDetails on 16032018
			User user = (User) ThreadUtils.getThreadVariable("user");
			//{call ng_rlos_SMSIntegration(#~#" + wiName + "#~#,#~#INIT#~#)}

			 
			ExecuteDbquery("{call ng_RLOS_CASPipelineCheck('" + inputjson.get("processId").toString() + "')}");
//			String stroutXML=ExecuteQuery_APProcedure( user.getAuthenticationToken(), "ng_RLOS_CASPipelineCheck", inputjson.get("processId").toString());
//			XMLParser xmlparser=new XMLParser();
//			xmlparser.setInputXML(stroutXML);
//			if(xmlparser.getValueOf("MainCode").trim().equalsIgnoreCase("0")){
			//LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "NEW JAR!!");
			
			
			JSONArray pipelineliablity_datavalues = getData("select 'ng_rlos_cust_extexpo_LoanDetails' as table_name,AgreementId as reference,LoanType as Product_Type," +
					"CustRoleType,Datelastupdated as DLP,TotalNoOfInstalments as Tenor,Consider_For_Obligations," +
					"SchemeCardProd,TotalAmt as LimAmt,PaymentsAmt as EMI,NoOfDaysInPipeline,Take_Over_Indicator as TKO," +
					"Take_Amount as TK from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Wi_Name = '"+inputjson.get("processId").toString()+"' and LoanStat = 'Pipeline' or LoanStat = 'CAS-Pipeline' union select 'ng_rlos_cust_extexpo_CardDetails' as table_name,CardEmbossNum as reference,CardType as Product_Type,CustRoleType,LastUpdateDate as DLP,'',Consider_For_Obligations,SchemeCardProd,TotalAmount as LimAmt,PaymentsAmount as EMI,NoOfDaysInPipeLine,'' as TKO,'' as TK from ng_rlos_cust_extexpo_CardDetails with (nolock) where Wi_Name = '"+inputjson.get("processId").toString()+"' and CardStatus = 'Pipeline' or CardStatus = 'CAS-Pipeline'  union Select 'ng_RLOS_CUSTEXPOSE_LoanDetails' as table_name,AgreementID as reference,LoanType,'Primary',LastUpdateDate,TotalNoOfInstalments as Tenor,Consider_For_Obligations,SchemeCardProd,TotalAmount,RemainingInstalments,NoOfDaysInPipeline,'','' from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where Wi_Name = '"+inputjson.get("processId").toString()+"' and (LoanStat = 'Pipeline' or (LoanStat = 'CAS-Pipeline' ))",
			"table_name,reference,Product_Type,CustRoleType,DLP,Tenor,Consider_For_Obligations,SchemeCardProd,LimAmt,EMI,NoOfDaysInPipeline,TKO,TK");
			responseObject.put("pipelineliablity", pipelineliablity_datavalues);
			//}
		} catch (Exception e) {
			writePrintStackTrace(e);
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}

	@Override
	public ResponseEntity exceuteSelect(IntegrationEntity requestobject)
			throws Exception {
		// TODO Auto-generated method stub
		// JSONObject paramjson=new JSONObject();
		ResponseEntity toreturn = new ResponseEntity();
		try {
			JSONParser parser_json = new JSONParser();
			JSONObject paramjson = (JSONObject) parser_json.parse(requestobject.getInputjsonstring());
			String query = paramjson.get("query").toString();
			String columnnames = paramjson.get("columnnames").toString();
			toreturn
			.setOutputjsonstring(getData(query, columnnames).toString());
		} catch (Exception e) {
			writePrintStackTrace(e);
			throw new Exception(e);
		}
		return toreturn;
	}

	public ArrayList < String > getCifListfromArray(JSONArray jsonarray) {
		ArrayList < String > returnlist = new ArrayList < String > ();
		for (int i = 0; i < jsonarray.size(); i++) {
			JSONObject tempobj = (JSONObject) jsonarray.get(i);
			returnlist.add(tempobj.get("cif-list").toString());
		}
		return returnlist;
	}
	
	/**
	 * Changes for updated date for rejection/accepted
	 * Date			:	18-06-2018
	 * Author : Sumit Balyan
	 * **/
	/**Start*/
	
	@Override
	public StreamedResource fetchBUCStatus(StreamedResource bucData) throws Exception {

		StreamedResource toReturn = new StreamedResource();
		User user = (User) ThreadUtils.getThreadVariable("user");
		if (user == null || user.getAuthenticationToken() == null) {
			throw new Exception("cannot fetch without user");
		}

		String xPath = null;
		String sessionId = user.getAuthenticationToken();
		String userName = user.getUserName();
		String password = user.getPassword();

		Node ofConfigNode = null;

		xPath = "./*/Server";
		Node serverInfoNode = this.configuration.searchObjectGraph(xPath, configuration.getConfigurationXmlText()).item(0);

		xPath = "./Key[@name = 'serverAddress']";
		String jtsAddress = this.configuration.searchObjectGraph(xPath, (Element) serverInfoNode).item(0).getTextContent().trim();

		xPath = "./Key[@name = 'serverPort']";
		String jtsPort = this.configuration.searchObjectGraph(xPath, (Element) serverInfoNode).item(0).getTextContent().trim();

		xPath = "./Key[@name = 'cabinetName']";
		String cabinetName = this.configuration.searchObjectGraph(xPath, (Element) serverInfoNode).item(0).getTextContent().trim();

		xPath = "./Key[@name = 'serverType']";
		String appServerType = this.configuration.searchObjectGraph(xPath, (Element) serverInfoNode).item(0).getTextContent().trim();

		xPath = "./Key[@name = 'wrapperless']";
		String wrapperLess = this.configuration.searchObjectGraph(xPath, (Element) serverInfoNode).item(0).getTextContent().trim();

		xPath = "./Key[@name = 'processDefId']";
		String processDefId = this.configuration.searchObjectGraph(xPath, (Element) serverInfoNode).item(0).getTextContent().trim();

		byte[] data = bucData.getStream();
		JSON.JSONObject jsonObject = new JSON.JSONObject(new String(data));
		JSON.JSONArray bucArray = jsonObject.getJSONArray("bucArray");

		JSONObject returnJsonObj = new JSONObject();
		JSONArray returnJsonArr = new JSONArray();

		for (int i = 0; i < bucArray.length(); i++) {
			JSON.JSONObject jsonObj = bucArray.getJSONObject(i);
			String processId = null;
			try {
				processId = (String) jsonObj.get("processId");
			} catch (Exception e) {
				writePrintStackTrace(e);
				processId = null;
			}
			String applicantId = (String) jsonObj.get("applicantId");
			String product = (String) jsonObj.get("product");
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "ProcessID=>" + processId);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "ApplicantID=>" + applicantId);
			if (product.equals("Credit Card")) {
				
				/**
				 * Changes for updated date for rejection/accepted
				 * Date			:	12-06-2018
				 * **/
				/**Start*/
				String query = "SELECT parent_winame,cc_wi_name,CURR_WSNAME,Entry_Date 'EntryDate' FROM NG_CC_EXTTABLE with (nolock) where parent_WIName='" + processId + "'";
				JSONArray dataArray = getData(query, "parent_winame,cc_wi_name,CURR_WSNAME,EntryDate");
				/**End*/
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "dataArray.size()=>" + dataArray.size());
				if (dataArray != null && dataArray.size() > 0) {
					JSONObject object = (JSONObject) dataArray.get(0);
					returnJsonObj.put("statusRemarks", object.get("CURR_WSNAME").toString());
					returnJsonObj.put("processId", object.get("parent_winame").toString());
					returnJsonObj.put("childprocessId", object.get("cc_wi_name").toString());
					/**Start*/
					returnJsonObj.put("EntryDate", object.get("EntryDate").toString());
					/**End*/
				} else {
										
					/**
					 * Changes for updated date for rejection/accepted
					 * Date			:	12-06-2018
					 * **/
					/**Start*/
					String query2 = "select ActivityName,EntryDATETIME 'EntryDate' from WFINSTRUMENTTABLE with (nolock) where ProcessInstanceID='" + processId + "' and WorkItemId='1'";
					JSONArray dataArray2 = getData(query2, "ActivityName,EntryDate");
					/**End*/
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "dataArray2.size()=>" + dataArray2.size());
					String statusRemark= "Branch_Init";
					if(dataArray2 != null && dataArray2.size() > 0) {
						JSONObject object = (JSONObject) dataArray2.get(0);
						statusRemark = object.get("ActivityName").toString();
						/**Start*/
						returnJsonObj.put("EntryDate", object.get("EntryDate").toString());
						/**End*/
					}
					returnJsonObj.put("statusRemarks", statusRemark);
					returnJsonObj.put("processId", processId);
					returnJsonObj.put("childprocessId", "");
				}
			} else if (product.equals("Personal Loan")) {
				
				/**
				 * Desciption	:	Changes for updated date for rejection/accepted
				 * Date			:	12-06-2018
				 * **/
				/**Start*/
				String query = "SELECT parent_winame,pl_wi_name,CURR_WSNAME,Entry_Date 'EntryDate' FROM NG_PL_EXTTABLE with (nolock) where parent_WIName='" + processId + "'";
				JSONArray dataArray = getData(query, "parent_winame,pl_wi_name,CURR_WSNAME,EntryDate");
				/**End*/
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "dataArray.size()=>" + dataArray.size());
				if (dataArray != null && dataArray.size() > 0) {
					JSONObject object = (JSONObject) dataArray.get(0);
					returnJsonObj.put("statusRemarks", object.get("CURR_WSNAME").toString());
					returnJsonObj.put("processId", object.get("parent_winame").toString());
					returnJsonObj.put("childprocessId", object.get("pl_wi_name").toString());
					/**Start*/
					returnJsonObj.put("EntryDate", object.get("EntryDate").toString());
					/**End*/
				} else {
					/**
					 * Desciption	:	Changes for updated date for rejection/accepted
					 * Date			:	12-06-2018
					 * **/
					/**Start*/
					String query2 = "select ActivityName,EntryDATETIME 'EntryDate' from WFINSTRUMENTTABLE with (nolock) where ProcessInstanceID='" + processId + "' and WorkItemId='1'";
					JSONArray dataArray2 = getData(query2, "ActivityName,EntryDate");
					String statusRemark= "Branch_Init";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "dataArray2.size()=>" + dataArray2.size());
					if(dataArray2 != null && dataArray2.size() > 0) {
						JSONObject object = (JSONObject) dataArray2.get(0);
						statusRemark = object.get("ActivityName").toString();
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "else statusRemark=>" + statusRemark);
						/**Start*/
						returnJsonObj.put("EntryDate", object.get("EntryDate").toString());
						/**End*/
					}
					returnJsonObj.put("statusRemarks", statusRemark);
					returnJsonObj.put("processId", processId);
					returnJsonObj.put("childprocessId", "");
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "else returnJsonObj=>" + returnJsonObj.toString());
				}
			}
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "adding returnJsonObj=>" + returnJsonObj.toString());
			returnJsonArr.add(returnJsonObj);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "adding after returnJsonArr=>" + returnJsonArr.toString());
			returnJsonObj = new JSONObject();
			
		}
		toReturn.setStream(new String(returnJsonArr.toString()).getBytes());
		return toReturn;
	}

	
	
	
	
//
//	@Override
//	public StreamedResource fetchBUCStatus(StreamedResource bucData) throws Exception {
//		StreamedResource toReturn = new StreamedResource();
//		User user = (User) ThreadUtils.getThreadVariable("user");
//		if (user == null || user.getAuthenticationToken() == null) {
//			throw new Exception("cannot fetch without user");
//		}
//		String xPath = null;
//		String sessionId = user.getAuthenticationToken();
//		String userName = user.getUserName();
//		String password = user.getPassword();
//
//		Node ofConfigNode = null;
//
//		xPath = "./*/Server";
//		Node serverInfoNode = this.configuration.searchObjectGraph(xPath, configuration.getConfigurationXmlText()).item(0);
//
//		xPath = "./Key[@name = 'serverAddress']";
//		String jtsAddress = this.configuration.searchObjectGraph(xPath, (Element) serverInfoNode).item(0).getTextContent().trim();
//
//		xPath = "./Key[@name = 'serverPort']";
//		String jtsPort = this.configuration.searchObjectGraph(xPath, (Element) serverInfoNode).item(0).getTextContent().trim();
//
//		xPath = "./Key[@name = 'cabinetName']";
//		String cabinetName = this.configuration.searchObjectGraph(xPath, (Element) serverInfoNode).item(0).getTextContent().trim();
//
//		xPath = "./Key[@name = 'serverType']";
//		String appServerType = this.configuration.searchObjectGraph(xPath, (Element) serverInfoNode).item(0).getTextContent().trim();
//
//		xPath = "./Key[@name = 'wrapperless']";
//		String wrapperLess = this.configuration.searchObjectGraph(xPath, (Element) serverInfoNode).item(0).getTextContent().trim();
//
//		xPath = "./Key[@name = 'processDefId']";
//		String processDefId = this.configuration.searchObjectGraph(xPath, (Element) serverInfoNode).item(0).getTextContent().trim();
//
//		byte[] data = bucData.getStream();
//		JSON.JSONObject jsonObject = new JSON.JSONObject(new String(data));
//		JSON.JSONArray bucArray = jsonObject.getJSONArray("bucArray");
//
//		JSONObject returnJsonObj = new JSONObject();
//		JSONArray returnJsonArr = new JSONArray();
//
//		for (int i = 0; i < bucArray.length(); i++) {
//			JSON.JSONObject jsonObj = bucArray.getJSONObject(i);
//			String processId = null;
//			try {
//				processId = (String) jsonObj.get("processId");
//			} catch (Exception e) {
//				processId = null;
//			}
//			String applicantId = (String) jsonObj.get("applicantId");
//			String product = (String) jsonObj.get("product");
//			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "ProcessID=>" + processId);
//			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "ApplicantID=>" + applicantId);
//			if (product.equals("Credit Card")) {
//				String query = "SELECT parent_winame,cc_wi_name,CURR_WSNAME FROM NG_CC_EXTTABLE with (nolock) where parent_WIName='" + processId + "'";
//				JSONArray dataArray = getData(query, "parent_winame,cc_wi_name,CURR_WSNAME");
//				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "dataArray.size()=>" + dataArray.size());
//				if (dataArray != null && dataArray.size() > 0) {
//					JSONObject object = (JSONObject) dataArray.get(0);
//					returnJsonObj.put("statusRemarks", object.get("CURR_WSNAME").toString());
//					returnJsonObj.put("processId", object.get("parent_winame").toString());
//					returnJsonObj.put("childprocessId", object.get("cc_wi_name").toString());
//				} else {
//					String query2 = "select ActivityName from WFINSTRUMENTTABLE with (nolock) where ProcessInstanceID='" + processId + "' and WorkItemId='1'";
//					JSONArray dataArray2 = getData(query2, "ActivityName");
//					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "dataArray2.size()=>" + dataArray2.size());
//					String statusRemark= "Branch_Init";
//					if(dataArray2 != null && dataArray2.size() > 0) {
//						JSONObject object = (JSONObject) dataArray2.get(0);
//						statusRemark = object.get("ActivityName").toString();
//					}
//					returnJsonObj.put("statusRemarks", statusRemark);
//					returnJsonObj.put("processId", processId);
//					returnJsonObj.put("childprocessId", "");
//				}
//			} else if (product.equals("Personal Loan")) {
//				String query = "SELECT parent_winame,pl_wi_name,CURR_WSNAME FROM NG_PL_EXTTABLE with (nolock) where parent_WIName='" + processId + "'";
//				JSONArray dataArray = getData(query, "parent_winame,pl_wi_name,CURR_WSNAME");
//				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "dataArray.size()=>" + dataArray.size());
//				if (dataArray != null && dataArray.size() > 0) {
//					JSONObject object = (JSONObject) dataArray.get(0);
//					returnJsonObj.put("statusRemarks", object.get("CURR_WSNAME").toString());
//					returnJsonObj.put("processId", object.get("parent_winame").toString());
//					returnJsonObj.put("childprocessId", object.get("pl_wi_name").toString());
//				} else {
//					String query2 = "select ActivityName from WFINSTRUMENTTABLE with (nolock) where ProcessInstanceID='" + processId + "' and WorkItemId='1'";
//					JSONArray dataArray2 = getData(query2, "ActivityName");
//					String statusRemark= "Branch_Init";
//					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "dataArray2.size()=>" + dataArray2.size());
//					if(dataArray2 != null && dataArray2.size() > 0) {
//						JSONObject object = (JSONObject) dataArray2.get(0);
//						statusRemark = object.get("ActivityName").toString();
//						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "else statusRemark=>" + statusRemark);
//					}
//					returnJsonObj.put("statusRemarks", statusRemark);
//					returnJsonObj.put("processId", processId);
//					returnJsonObj.put("childprocessId", "");
//					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "else returnJsonObj=>" + returnJsonObj.toString());
//				}
//			}
//			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "adding returnJsonObj=>" + returnJsonObj.toString());
//			returnJsonArr.add(returnJsonObj);
//			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "adding after returnJsonArr=>" + returnJsonArr.toString());
//			returnJsonObj = new JSONObject();
//			// String inputXML = generateXMLTofetchBucStatus(cabinetName,
//			// sessionId, processId, processDefId);
//			// LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
//			// "inputXML to fetch buc status : " + inputXML);
//			// String outputXML =WFCallBroker.execute(inputXML,jtsAddress,
//			// Integer.parseInt(jtsPort), 0);
//			// LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"ouputXML to fetch buc status : "+outputXML);
//			// if ((outputXML == null) || (outputXML.trim().isEmpty())) {
//			// throw new Exception("no outputXML generated for buc(" +
//			// applicantId + ")");
//			// }
//			// XMLParser parseOutXml = new XMLParser();
//			// parseOutXml.setInputXML(outputXML);
//			// if (parseOutXml.getValueOf("MainCode").trim().equals("0")) {
//			// String processState = parseOutXml.getValueOf("ActivityName");
//			// LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
//			// "ActivityName FOR fetch buc status : " + processState);
//			//
//			// //send processId & statusRemarks if mainCode is zero
//			// returnJsonObj.put("statusRemarks", processState);
//			// returnJsonObj.put("processId", processId);
//			// LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
//			// "WORKITEM: "+processId+" returnJson READY TO SEND: "+returnJsonObj);
//			//				
//			// returnJsonArr.add(returnJsonObj);
//			//				
//			// returnJsonObj = new JSONObject();
//			// }
//		}
//		toReturn.setStream(new String(returnJsonArr.toString()).getBytes());
//		return toReturn;
//	}

	private String generateXMLTofetchBucStatus(String cabinetName, String sessionId, String processId, String processDefId) {
		StringBuilder wmSearchWorkItemsInputXml = new StringBuilder();
		wmSearchWorkItemsInputXml.append("<?xml version=\"1.0\"?>");
		wmSearchWorkItemsInputXml.append("<WMSearchWorkItems_Input>");
		wmSearchWorkItemsInputXml.append("<Option>WMSearchWorkItems</Option>");
		wmSearchWorkItemsInputXml.append("<EngineName>" + cabinetName + "</EngineName>");
		wmSearchWorkItemsInputXml.append("<SessionId>" + sessionId + "</SessionId>");
		wmSearchWorkItemsInputXml.append("<Filter>");
		wmSearchWorkItemsInputXml.append("<ProcessInstanceName>" + processId + "</ProcessInstanceName>");
		wmSearchWorkItemsInputXml.append("<ProcessDefinitionID>" + processDefId + "</ProcessDefinitionID>");
		wmSearchWorkItemsInputXml.append("</Filter>");
		wmSearchWorkItemsInputXml.append("</WMSearchWorkItems_Input>");
		return wmSearchWorkItemsInputXml.toString();
	}
	//Added by Aman for Start and close date
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
	
	public static double Cas_Limit(double aff_emi,double rate,double tenureMonths)
	{
		double pmt;
		try{
			/* 30-04-2019*/
			double new_rate = (rate/100)/12;
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"new_rate : " + new_rate);
			pmt = (aff_emi)*(1-Math.pow(1+new_rate,-tenureMonths))/new_rate;
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"final_rate_new 1ST pmt11 : " + pmt);
		}
		catch(Exception e){
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"Exception occured while calculating cas calc limit"+e.getMessage());
			pmt=0;
		}
		return pmt;

	}
	//	public static void main(String args[]){
	//		String xml = "<applicant_id>2394267</applicant_id><internal_bureau_individual_products_id>087343100</internal_bureau_individual_products_id><type_product>CARDS</type_product><contract_type>CC</contract_type><provider_no>RAKBANK</provider_no><phase>A</phase><role_of_customer>Primary</role_of_customer><start_date></start_date><close_date></close_date><date_last_updated></date_last_updated><outstanding_balance>-21476.77</outstanding_balance><payments_amount>577.91</payments_amount><method_of_payment>Red Card INTEREST PROFILE</method_of_payment><total_no_of_instalments></total_no_of_instalments><no_of_remaining_instalments></no_of_remaining_instalments><worst_status>CARDS</worst_status><worst_status_date></worst_status_date><credit_limit>20000</credit_limit><overdue_amount></overdue_amount><no_of_days_payment_delay></no_of_days_payment_delay><mob>13</mob><last_repayment_date></last_repayment_date><currently_current></currently_current><current_utilization>0</current_utilization><dpd_30_last_6_mon>0</dpd_30_last_6_mon><dpd_60p_in_last_18_mon>0</dpd_60p_in_last_18_mon><card_product>Select</card_product><property_value></property_value><disbursal_date></disbursal_date><marketing_code></marketing_code><card_expiry_date>2025-01-31</card_expiry_date><card_upgrade_indicator>Y</card_upgrade_indicator><part_settlement_date></part_settlement_date><part_settlement_amount></part_settlement_amount><part_settlement_reason></part_settlement_reason><limit_expiry_date></limit_expiry_date><no_of_primary_cards>2</no_of_primary_cards><no_of_repayments_done></no_of_repayments_done><card_segment>REDCARD-EXPAT</card_segment><product_type>CARDS</product_type><product_category>REDCARD-EXPAT</product_category><combined_limit_flag>Y</combined_limit_flag><secured_card_flag>N</secured_card_flag><resch_tko_flag>Y</resch_tko_flag><general_status></general_status><consider_for_obligation>N</consider_for_obligation><limit_increase>Y</limit_increase><role>Primary</role><limit></limit><status>A</status><emi>577.91</emi><os_amt>-21476.77</os_amt><dpd_30_in_last_3mon></dpd_30_in_last_3mon><dpd_30_in_last_6mon>0</dpd_30_in_last_6mon><dpd_30_in_last_9mon></dpd_30_in_last_9mon><dpd_30_in_last_12mon></dpd_30_in_last_12mon><dpd_30_in_last_18mon></dpd_30_in_last_18mon><dpd_30_in_last_24mon></dpd_30_in_last_24mon><dpd_60_in_last_3mon></dpd_60_in_last_3mon><dpd_60_in_last_6mon></dpd_60_in_last_6mon><dpd_60_in_last_9mon></dpd_60_in_last_9mon><dpd_60_in_last_12mon></dpd_60_in_last_12mon><dpd_60_in_last_18mon>0</dpd_60_in_last_18mon><dpd_60_in_last_24mon></dpd_60_in_last_24mon><dpd_90_in_last_3mon></dpd_90_in_last_3mon><dpd_90_in_last_6mon></dpd_90_in_last_6mon><dpd_90_in_last_9mon></dpd_90_in_last_9mon><dpd_90_in_last_12mon></dpd_90_in_last_12mon><dpd_90_in_last_18mon></dpd_90_in_last_18mon><dpd_90_in_last_24mon></dpd_90_in_last_24mon><dpd_120_in_last_3mon></dpd_120_in_last_3mon><dpd_120_in_last_6mon></dpd_120_in_last_6mon><dpd_120_in_last_9mon></dpd_120_in_last_9mon><dpd_120_in_last_12mon></dpd_120_in_last_12mon><dpd_120_in_last_18mon></dpd_120_in_last_18mon><dpd_120_in_last_24mon></dpd_120_in_last_24mon><dpd_150_in_last_3mon></dpd_150_in_last_3mon><dpd_150_in_last_6mon></dpd_150_in_last_6mon><dpd_150_in_last_9mon></dpd_150_in_last_9mon><dpd_150_in_last_12mon></dpd_150_in_last_12mon><dpd_150_in_last_18mon></dpd_150_in_last_18mon><dpd_150_in_last_24mon></dpd_150_in_last_24mon><dpd_180_in_last_3mon></dpd_180_in_last_3mon><dpd_180_in_last_6mon></dpd_180_in_last_6mon><dpd_180_in_last_9mon></dpd_180_in_last_9mon><dpd_180_in_last_12mon></dpd_180_in_last_12mon><dpd_180_in_last_18mon></dpd_180_in_last_18mon><dpd_180_in_last_24mon></dpd_180_in_last_24mon><last_temp_limit_exp></last_temp_limit_exp><last_per_limit_exp></last_per_limit_exp><security_cheque_amt></security_cheque_amt><mol_salary_variance></mol_salary_variance><kompass>N</kompass><employer_type></employer_type><no_of_paid_installment></no_of_paid_installment><write_off_amount></write_off_amount><company_flag>N</company_flag><type_of_od></type_of_od><amt_paid_last6mnths></amt_paid_last6mnths></InternalBureauIndividualProducts><InternalBureauIndividualProducts><applicant_id>2394267</applicant_id><internal_bureau_individual_products_id>088454000</internal_bureau_individual_products_id><type_product>CARDS</type_product><contract_type>CC</contract_type><provider_no>RAKBANK</provider_no><phase>A</phase><role_of_customer>Primary</role_of_customer><start_date></start_date><close_date></close_date><date_last_updated></date_last_updated><outstanding_balance>-30212.44</outstanding_balance><payments_amount>976.12</payments_amount><method_of_payment>LOC JAN 12 INTEREST PROFILE</method_of_payment><total_no_of_instalments></total_no_of_instalments><no_of_remaining_instalments></no_of_remaining_instalments><worst_status>CARDS</worst_status><worst_status_date></worst_status_date><credit_limit>30000</credit_limit><overdue_amount></overdue_amount><no_of_days_payment_delay></no_of_days_payment_delay><mob>11</mob><last_repayment_date></last_repayment_date><currently_current></currently_current><current_utilization>0</current_utilization><dpd_30_last_6_mon>0</dpd_30_last_6_mon><dpd_60p_in_last_18_mon>0</dpd_60p_in_last_18_mon><card_product>Select</card_product><property_value></property_value><disbursal_date></disbursal_date><marketing_code></marketing_code><card_expiry_date>2025-01-31</card_expiry_date><card_upgrade_indicator>Y</card_upgrade_indicator><part_settlement_date></part_settlement_date><part_settlement_amount></part_settlement_amount><part_settlement_reason></part_settlement_reason><limit_expiry_date></limit_expiry_date><no_of_primary_cards>2</no_of_primary_cards><no_of_repayments_done></no_of_repayments_done><card_segment>LOC STANDARD</card_segment><product_type>CARDS</product_type><product_category>LOC STANDARD</product_category><combined_limit_flag>N</combined_limit_flag><secured_card_flag>N</secured_card_flag><resch_tko_flag>Y</resch_tko_flag><general_status></general_status><consider_for_obligation>N</consider_for_obligation><limit_increase>Y</limit_increase><role>Primary</role><limit></limit><status>A</status><emi>976.12</emi><os_amt>-30212.44</os_amt><dpd_30_in_last_3mon></dpd_30_in_last_3mon><dpd_30_in_last_6mon>0</dpd_30_in_last_6mon><dpd_30_in_last_9mon></dpd_30_in_last_9mon><dpd_30_in_last_12mon></dpd_30_in_last_12mon><dpd_30_in_last_18mon></dpd_30_in_last_18mon><dpd_30_in_last_24mon></dpd_30_in_last_24mon><dpd_60_in_last_3mon></dpd_60_in_last_3mon><dpd_60_in_last_6mon></dpd_60_in_last_6mon><dpd_60_in_last_9mon></dpd_60_in_last_9mon><dpd_60_in_last_12mon></dpd_60_in_last_12mon><dpd_60_in_last_18mon>0</dpd_60_in_last_18mon><dpd_60_in_last_24mon></dpd_60_in_last_24mon><dpd_90_in_last_3mon></dpd_90_in_last_3mon><dpd_90_in_last_6mon></dpd_90_in_last_6mon><dpd_90_in_last_9mon></dpd_90_in_last_9mon><dpd_90_in_last_12mon></dpd_90_in_last_12mon><dpd_90_in_last_18mon></dpd_90_in_last_18mon><dpd_90_in_last_24mon></dpd_90_in_last_24mon><dpd_120_in_last_3mon></dpd_120_in_last_3mon><dpd_120_in_last_6mon></dpd_120_in_last_6mon><dpd_120_in_last_9mon></dpd_120_in_last_9mon><dpd_120_in_last_12mon></dpd_120_in_last_12mon><dpd_120_in_last_18mon></dpd_120_in_last_18mon><dpd_120_in_last_24mon></dpd_120_in_last_24mon><dpd_150_in_last_3mon></dpd_150_in_last_3mon><dpd_150_in_last_6mon></dpd_150_in_last_6mon><dpd_150_in_last_9mon></dpd_150_in_last_9mon><dpd_150_in_last_12mon></dpd_150_in_last_12mon><dpd_150_in_last_18mon></dpd_150_in_last_18mon><dpd_150_in_last_24mon></dpd_150_in_last_24mon><dpd_180_in_last_3mon></dpd_180_in_last_3mon><dpd_180_in_last_6mon></dpd_180_in_last_6mon><dpd_180_in_last_9mon></dpd_180_in_last_9mon><dpd_180_in_last_12mon></dpd_180_in_last_12mon><dpd_180_in_last_18mon></dpd_180_in_last_18mon><dpd_180_in_last_24mon></dpd_180_in_last_24mon><last_temp_limit_exp></last_temp_limit_exp><last_per_limit_exp></last_per_limit_exp><security_cheque_amt></security_cheque_amt><mol_salary_variance></mol_salary_variance><kompass>N</kompass><employer_type></employer_type><no_of_paid_installment></no_of_paid_installment><write_off_amount></write_off_amount><company_flag>N</company_flag><type_of_od></type_of_od><amt_paid_last6mnths></amt_paid_last6mnths></InternalBureauIndividualProducts><InternalBureauIndividualProducts>";
	//		System.out.println(xml);
	//		System.out.println(RakBankIntegrations.removeEmptyXmlTags(xml));
	//
	//			
	//	}
	
	private String ExecuteQuery_APProcedure ( String sessionId, String procName, String wi_name)
	{
		Node ofConfigNode = null;
		String xPath = "";
		xPath = "./*/Server";
		String strOutputXml="";
		try{
		Node serverInfoNode = this.configuration.searchObjectGraph(xPath,configuration.getConfigurationXmlText()).item(0);

		xPath = "./Key[@name = 'serverAddress']";
		String jtsAddress = this.configuration.searchObjectGraph(xPath,(Element) serverInfoNode).item(0).getTextContent().trim();

		xPath = "./Key[@name = 'serverPort']";
		String jtsPort = this.configuration.searchObjectGraph(xPath, (Element) serverInfoNode).item(0).getTextContent().trim();

		xPath = "./Key[@name = 'cabinetName']";
		String cabinetName = this.configuration.searchObjectGraph(xPath, (Element) serverInfoNode).item(0).getTextContent().trim();

		xPath = "./Key[@name = 'serverType']";
		String appServerType = this.configuration.searchObjectGraph(xPath, (Element) serverInfoNode).item(0).getTextContent().trim();

		xPath = "./Key[@name = 'wrapperless']";
		String wrapperLess = this.configuration.searchObjectGraph(xPath, (Element) serverInfoNode).item(0).getTextContent().trim();

		xPath = "./Key[@name = 'processDefId']";
		String processDefId = this.configuration.searchObjectGraph(xPath, (Element) serverInfoNode).item(0).getTextContent().trim();
		String sInputXML = "<?xml version=\"1.0\"?>"+
				"<APProcedure_WithDBO_Input>"+
				"<option>APProcedure_WithDBO</option>"+
				"<ProcName>"+procName+"</ProcName>"+
				"<Params>'"+wi_name+"'</Params>"+
				"<EngineName>"+cabinetName+"</EngineName>"+
				"<SessionId>"+sessionId+"</SessionID>"+
				"</APProcedure_WithDBO_Input>";
		
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "sInputXML" + sInputXML);
		 strOutputXml = WFCallBroker.execute(sInputXML, jtsAddress, Integer.parseInt(jtsPort), 0);
		 LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "sInputXML" + strOutputXml);
		}
		catch(Exception e ){
			//e.printStackTrace();
		}
		
		return strOutputXml;           

	}

	@Override
	public ResponseEntity checkVersion(IntegrationEntity request)
			throws Exception {
		ResponseEntity response = new ResponseEntity();
		JSONObject responseobj=new JSONObject();
		try{
			JSONParser parser=new JSONParser();
			JSONObject requestobj=(JSONObject)parser.parse(request.getInputjsonstring());
			
			//"+requestobj.get("version")+"'"
			
			/** CHANGES FOR RETENTION POLICY **
			 * 
			 * ---------------------------------------------------------------------------
			 * Date           Name      		Comment
			 * ---------------------------------------------------------------------------
			 * 08/06/2018	SUMIT BALYAN		CHANGES RETENTION POLICY	
			 * --------------------------------------------------------------------------- 
			 **/
			 
			/** RETENTION-START  */
			/** OLD QUERY
			 * 
			 * JSONArray array =getData("SELECT TOP 1 VERSION  FROM NEMF_RAK_VERSION ORDER BY ID desc", "VERSION");	
			 * */
			JSONArray array =getData("SELECT TOP 1 VERSION,AcceptedTimer,RejectedTimer FROM NEMF_RAK_VERSION  ORDER BY ID DESC", "VERSION,AcceptedTimer,RejectedTimer");
			/**RETENTION-END*/
		if(array.size() > 0 ){
			JSONObject record = (JSONObject)array.get(0);
			double version =Double.valueOf(requestobj.get("version").toString());
			Double versionindb=Double.valueOf( record.get("VERSION").toString());
			/**RETENTION-START*/
			responseobj.put("AcceptedTimer", record.get("AcceptedTimer").toString());
			responseobj.put("RejectedTimer", record.get("RejectedTimer").toString());
			/**RETENTION-END*/
			if(version >=versionindb){
			responseobj.put("status", "success");
			responseobj.put("message", "Version matched");
			}
			else{
				responseobj.put("status", "failure");
				responseobj.put("message", "New Version Available! Please Upgrade to : "+versionindb);
			}
		}	else{
			responseobj.put("status", "failure");
			responseobj.put("message", "No Version Value Found In DB");
		}
		}
		catch(Exception e){
			throw new Exception(e);
		}
		// TODO Auto-generated method stub
		response.setOutputjsonstring(responseobj.toString());
		return response;
	}
	
	
	
	/*@Override
	public ResponseEntity checkVersion(IntegrationEntity request)
			throws Exception {
		ResponseEntity response = new ResponseEntity();
		JSONObject responseobj=new JSONObject();
		try{
			JSONParser parser=new JSONParser();
			JSONObject requestobj=(JSONObject)parser.parse(request.getInputjsonstring());
			
			//"+requestobj.get("version")+"'"
			JSONArray array =getData("SELECT TOP 1 VERSION  FROM NEMF_RAK_VERSION ORDER BY ID desc", "VERSION");	
		if(array.size() > 0 ){
			JSONObject record = (JSONObject)array.get(0);
			double version =Double.valueOf(requestobj.get("version").toString());
			Double versionindb=Double.valueOf( record.get("VERSION").toString());
			if(version >=versionindb){
			responseobj.put("status", "success");
			responseobj.put("message", "Version matched");
			}
			else{
				responseobj.put("status", "failure");
				responseobj.put("message", "New Version Available! Please Upgrade to : "+versionindb);
			}
		}	else{
			responseobj.put("status", "failure");
			responseobj.put("message", "No Version Value Found In DB");
		}
		}
		catch(Exception e){
			throw new Exception(e);
		}
		// TODO Auto-generated method stub
		response.setOutputjsonstring(responseobj.toString());
		return response;
	}*/
	
	
	public ResponseEntity getRetentionTime(IntegrationEntity request) throws Exception {
		ResponseEntity response = new ResponseEntity();
		JSONObject responseobj=new JSONObject();
		Node ofConfigNode = null;
		String xPath = "";
		xPath = "./*/RetentionTrays";
		try{
			Node serverInfoNode = this.configuration.searchObjectGraph(xPath,configuration.getConfigurationXmlText()).item(0);

			xPath = "./Trayname";
			NodeList trayNodes = this.configuration.searchObjectGraph(xPath,(Element) serverInfoNode);
			Node trayNode = null;
			for(int i=0; i <trayNodes.getLength();i++){
				JSONObject responseTrayobj=new JSONObject();
				trayNode = trayNodes.item(i);
				String trayName = ((Element) trayNode).getAttribute("name");
				xPath = "./Key";
				Node bucNode = null;
				NodeList bucNodes = this.configuration.searchObjectGraph(xPath,(Element)  trayNode);
				for(int j=0;j<bucNodes.getLength();j++){
					bucNode = bucNodes.item(j);
					String bucName = ((Element) bucNode).getAttribute("bucName");
					String unit = ((Element) bucNode).getAttribute("unit");
					if ((unit == null) || (unit.trim().isEmpty())) {
				        throw new Exception("no client side retention policy unit found  for: " + trayName +"-->"+bucName);
				      }
					String value = ((Element) bucNode).getAttribute("value");
					if ((value == null) || (value.trim().isEmpty())) {
				        throw new Exception("no client side retention policy unit found  for : " + trayName +"-->"+bucName);
				      }
					Integer Frequency = Integer.valueOf(Integer.parseInt(value));
				      if (unit != null) {
				        if (unit.equalsIgnoreCase("milliseconds")) {
				        	Frequency = Integer.valueOf(Frequency.intValue() * 1);
				        } else if (unit.equalsIgnoreCase("seconds")) {
				        	Frequency = Integer.valueOf(Frequency.intValue() * 1000);
				        } else if (unit.equalsIgnoreCase("minutes")) {
				        	Frequency = Integer.valueOf(Frequency.intValue() * 60000);
				        } else if (unit.equalsIgnoreCase("hours")) {
				        	Frequency = Integer.valueOf(Frequency.intValue() * 3600000);
				        } else if (unit.equalsIgnoreCase("days")) {
				        	Frequency = Integer.valueOf(Frequency.intValue() * 86400000);
				        } else {
				          throw new Exception("unrecognized retentionPolicyJobFrequencyUnit");
				        }
				        responseTrayobj.put(bucName, Frequency);
				      }
					
				}
				responseobj.put(trayName, responseTrayobj);
				
			}
			
		}
		catch(Exception e){
			throw new Exception(e);
		}
		// TODO Auto-generated method stub
		response.setOutputjsonstring(responseobj.toString());
		return response;
	}
	
	/*
	// added by abhishek for integration calls from java code.
	
	public String LiabilityIntegration(String request_name , String operation_type , String param_json ) {
		String result = "";
			if(request_name.contains("InternalExposure") || request_name.contains("CollectionsSummary") ||request_name.contains("ExternalExposure")||request_name.contains("CARD_INSTALLMENT_DETAILS")){
				result = integration_all(request_name);
			}
			else{
				result = integration_all_Financial(request_name);
			}
			
			return result;
			
		}
	
	public String integration_all(String request_name){
		String wi_Name = param_json_object.get("processId").toString().trim();
		String result = null;
		String  finalxmlResponse = null;
		String  MultipleCif = getMultipleCif(wi_name);
	    result = cifIntegration(request_name,MultipleCif);
	
		           			
	    return result;
	}
	// Need to do this code according to Mobility
	public String integration_all_Financial(String request_name){
		String req_name = "";
		String result = "";
		String response = MultipleCifId_accId(wi_name);
		
			
				if(trimStr(response)=="NO DATA"){
						String jsondata = JSON.parse(decodeURIComponent(param_json));
						String companyCif=jsondata["company_cif"]; 
						String sub_product=jsondata["sub_product"]; 
						String emp_type=jsondata["emp_type"]; 
						if(companyCif=="" || companyCif==null){
							req_name = fetchOperation_Name(sub_product,emp_type,"Individual_CIF");
						}
						else{
							req_name = fetchOperation_Name(sub_product,emp_type,"");
						}
					String req_name_arr = req_name.split(',');
					for (var i=0;i<req_name_arr.length;i++)
					{								
						dynamicTable(req_name_arr[i],'',row_count);
						var eml_name='call_status_' +row_count+'';
						document.getElementById(eml_name).innerHTML="No Account exists for this Customer!!";							
						row_count++;
					}
					window.opener.com.newgen.omniforms.formviewer.setNGValue('aecb_call_status','FAIL');
					window.returnValue = "SUCCESS";							
				}
				else{
					result = cifIntegration(req_name,response);
					
				}
			
		           			
	return result;
	}
	
	 public String fetchOperation_Name(String sub_product,String emp_type,String acc_type)
	   {
			
			String result = "";
				
			String sQuery = "";
			
			
			if(emp_type.equalsIgnoreCase("Salaried Pensioner")){
				emp_type="Salaried";
				}
				if(emp_type.equalsIgnoreCase("Salaried") || acc_type.equalsIgnoreCase(""))//modified by akshay on 2/1/18
				{
					sQuery = "select Operation_name from ng_rlos_Financial_Summary with (nolock) where WorkStep ='Branch_Init' and Sub_Product = '"+sub_product+"' and Employment = '"+emp_type+"'";
				}
				else
				{
					sQuery = "select Operation_name from ng_rlos_Financial_Summary with (nolock) where AccountType ='"+acc_type+"' and WorkStep ='Branch_Init' and Sub_Product = '"+sub_product+"' and Employment = '"+emp_type+"'";
				}
			
				List < List < String >> ReqList = ExecuteSelectQueryGetList(sQuery);
				
				if(ReqList.size()>0){
					for(int i=0; i<ReqList.size() ; i++ ){
						if(result.equalsIgnoreCase("")){
							result = ReqList.get(i).get(0);
						}else{
							result = result + "," + ReqList.get(i).get(0);
						}
					}
				}
						
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"outputXML request types --> "+result);

			
	   
			return result;	   
	   }
	   
	
		 
	public String MultipleCifId_accId(String wi_name){
		String cifValues = "";
		String sQuery = "select CifId,AcctId,Account_Type from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where Wi_Name ='"+wi_name+"' ";
		List < List < String >> List = ExecuteSelectQueryGetList(sQuery);
		if(List.size()>0){
			for(int i=0; i<List.size() ; i++ ){
				if(cifValues.equalsIgnoreCase("")){
					cifValues = List.get(i).get(0)+":"+List.get(i).get(1)+":"+List.get(i).get(2);
				}else{
					cifValues = cifValues + "," + List.get(i).get(0)+":"+List.get(i).get(1)+":"+List.get(i).get(2);
				}
			}
		}
		else{
			cifValues= "NO DATA";
		}
		return cifValues;
	}
	
	public String getMultipleCif(String wi_name){
		
	    String cifId ="";
		String sQuery ="";
	
		
				
		sQuery = "select CustId from ng_rlos_cif_detail with (nolock) where cif_wi_name ='"+wi_name+"' and cif_SearchType !='External'";
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"sMQuery "+sQuery);
		
		
		List < List < String >> CifList = ExecuteSelectQueryGetList(sQuery);
		
		if(CifList.size()>0){
			for(int i=0; i<CifList.size() ; i++ ){
				if(cifId.equalsIgnoreCase("")){
					cifId = CifList.get(i).get(0);
				}else{
					cifId = cifId + "," + CifList.get(i).get(0);
				}
			}
		}
				
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"outputXML  CIF IDs --> "+cifId);
		
		return cifId;
	
	}
	
	public String cifIntegration(String request_name , List<List<String>>  response){
		String result = null;
		String cifID = null;
		String acc_id="";
		String CardNumber = "";
		String jsondata = JSON.parse(decodeURIComponent(param_json));
		String integration_call_flag = "";
		//Changes done by aman for AECB Start
		String companyCif=jsondata["cif"]; 
		//console.log('companyCif:'+ companyCif);
		String cust_type = "Individual_CIF";
		String TLNo=jsondata["trade_lic_no"]; 
		String sub_product=jsondata["sub_product"]; 
		String emp_type=jsondata["emp_type"]; 
		// below changes done by disha on 27-02-2018 for Collection summary should be run for the linked cif as well
		String linkedCifs = null;
		response = trimStr(response);
		/*if(response=='NO DATA'){
			response="";
		}*/
		/*if (companyCif !=''&& companyCif !='null' && (req_name =='InternalExposure,ExternalExposure' || req_name =='CollectionsSummary')){
			response = response + ',' + companyCif;			  
		}
		if (companyCif !="" && companyCif !="null" && (request_name =="InternalExposure,ExternalExposure")){
			if(response!=""){
				for(int i=0 ; i<response.size(); i++){
					response = response + ',' + companyCif;	
				}
						  
			}
			else{
				response = companyCif;			  
			}
			
		}		
		else if (companyCif !=''&& companyCif !='null' && (req_name =='CollectionsSummary'))
		{		
			response =trimStr(response);
			if(response!=''){
				response = response + ',' + companyCif;			  
			}
			else{
				response = companyCif;			  
			}
			//response = response + ',' + companyCif;	
			//console.log('response before fetching Linked CIFs: '+response);
			linkedCifs = trimStr(fetchLinkedCif());
				if(linkedCifs !='' && linkedCifs !='null')
				{
					response = response + ',' + linkedCifs;
					//console.log('response after fetching Linked CIFs: '+response);
				}
		}

		// above changes done by disha on 27-02-2018 for Collection summary should be run for the linked cif as well		  	
		else if(TLNo!='' && TLNo!=null && companyCif=='')
		{
			if(response!=''){
				response = response+','+'Corporate';
			}
			else{
				response = 'Corporate';			  
			}
			
		}
		//Changes done by aman for AECB End
		else if(req_name == 'ExternalExposure'){
			/*
			response = jsondata["cmplx_Customer_CIFNO"];
			if(trimStr(response)!='' && trimStr(response)!='null'){
				response = response + ',' + companyCif;
			}
			
			response = companyCif;
		}	
					
		var cif_acc_Arr=null;
		var cifId_arr = response.split(',');
		var dhtml="";
		
		for (var j=0;j<cifId_arr.length;j++)
		{
			if(cifId_arr[j].indexOf(":")>-1){
				cif_acc_Arr = cifId_arr[j].split(":");
				cifID = trimStr(cif_acc_Arr[0]);
				acc_id = cif_acc_Arr[1];
				// below code done to find opertaion names of financial summary on 29th Dec by disha
				acc_type = cif_acc_Arr[2];
				
				req_name = fetchOperation_Name(sub_product,emp_type,acc_type);
									
				// above code done to find opertaion names of financial summary on 29th Dec by disha
			}
			else{
				cifID = trimStr(cifId_arr[j]);
			}
			
			var req_name_arr = req_name.split(',');
				for (var i=0;i<req_name_arr.length;i++)
				{
					if((req_name_arr[i] != 'ExternalExposure' && cifID.indexOf(companyCif)<0)||(req_name_arr[i] == 'ExternalExposure' && (cifID.indexOf(jsondata["cmplx_Customer_CIFNO"])>-1)||cifID.indexOf(companyCif)>-1))
					{
						
						if(req_name == 'CARD_INSTALLMENT_DETAILS' && cifID!='Corporate'){
							data={ request_name: "CARD_INSTALLMENT_DETAILS", is_mobility : "N", SeesionId:sessionId ,wi_name:wi_name ,activityName:activityName ,param_json:param_json,cifId:jsondata["cmplx_Customer_CIFNO"],acc_id:acc_id,row_count:row_count,CardNumber:cifID}
							integration_call_flag='Y';
						}
						else{
							//Changes done by aman for AECB	Start
							
							if(cifID=='Corporate' || (cifID==companyCif && cifID!="null" && cifID!="")){
								cust_type = "Corporate_CIF";
							}
							//change by saurabh on 16th Oct.
							else{
								cust_type = "Individual_CIF";
							}
						
							if (req_name_arr[i] == 'ExternalExposure')
							 {
								data= { request_name: req_name_arr[i], is_mobility : "N", SeesionId:sessionId ,wi_name:wi_name ,activityName:activityName ,param_json:param_json,cifId:cifID,acc_id:acc_id,row_count:row_count,Customer_Type:cust_type}
								integration_call_flag="Y";
							 }
							 
							 //added for External call of Company when cif is not available
							 else if(req_name_arr[i] != 'ExternalExposure' && cifID=='Corporate'){
							 	integration_call_flag="N";
							 }
							 //added for External call of Company when cif is not available

							 else if(req_name_arr[i] == 'InternalExposure'  && (cifID=="null" || cifID=="" || cifID=='NO DATA')){
								integration_call_flag="N";
							 }
							 else if(req_name_arr[i] != 'ExternalExposure'){
								 data= { request_name: req_name_arr[i], is_mobility : "N", SeesionId:sessionId ,wi_name:wi_name ,activityName:activityName ,param_json:param_json,cifId:cifID,acc_id:acc_id,row_count:row_count}
								 integration_call_flag="Y";
								}
							//Changes done by aman for AECB End
						}								 
						if(integration_call_flag=="Y"){
							
							//REF-18092017 start
							if(req_name_arr[i]=='CARD_INSTALLMENT_DETAILS' && (trimStr(response)=='NO DATA' || trimStr(response)=='')){//changed from '' to 'no data' by akshay on 26/3/18
								dynamicTable(req_name_arr[i],'',row_count);
								var eml_name='call_status_' +row_count+'';
								document.getElementById(eml_name).innerHTML="No LOC Card found for this Customer";
								window.returnValue = "SUCCESS";
							}
							else{
							dynamicTable(req_name_arr[i], cifId_arr[j],row_count);
							final_integration(data,cifID,cust_type,req_name_arr[i]);
							
								$.ajax({
								type: "GET",
								url: "/webdesktop/resources/scripts/Requestxml_1.jsp",
								data: data,
								dataType: "text",
								async: true,
								success: function (response) {
									var rowcount=response.split('!@#');
									set_result_val(rowcount[0],rowcount[1],'',cifID,cust_type);
								},
								error: function (XMLHttpRequest, textStatus, errorThrown) {
									document.getElementById("retry_btn_"+req_name_arr[i]).style.visibility="visible";		
									alert('Exception: '+ errorThrown);
								}
							});
							}
							//REF-18092017 End
						}
						
						row_count++;
					}
					
				}
		}
		
				return result;
	}
	
	*/
	
}