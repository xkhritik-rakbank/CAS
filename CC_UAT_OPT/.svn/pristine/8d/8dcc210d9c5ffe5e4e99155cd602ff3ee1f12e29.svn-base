package com.newgen.omniforms.user;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.newgen.custom.Common_Utils;
import com.newgen.mvcbeans.view.newWIView;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.Column;
import com.newgen.omniforms.component.ListView;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.util.Common;


public class CC_Integration_Input extends Common implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	CC_Common CC_Comn = new CC_Common();
	private String col_n = "call_type,Call_name,form_control,parent_tag_name,xmltag_name,is_repetitive,default_val,data_format";			
	private String fin_call_name = "Customer_details, Customer_eligibility,new_customer_req,new_account_req,DEDUP_SUMMARY";


	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : Generate XML           

	 ***********************************************************************************  */
	public String GenerateXML(String callName, String Operation_name) {

		CreditCard.mLogger.info("RLOSCommon"+ "Inside GenerateXML():");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		StringBuilder final_xml = new StringBuilder("");
		String header = "";
		String footer = "";
		String parentTagName = "";
		String sQuery = null;

		CreditCard.mLogger.info("$$outputgGridtXML "+ "before try");
		try {

			String sQuery_header = "SELECT Header,Footer,parenttagname FROM NG_Integration with (nolock) where Call_name='" + callName + "'";
			CreditCard.mLogger.info("RLOSCommon"+ "sQuery" + sQuery_header);
			List<List<String>> OutputXML_header = formObject.getNGDataFromDataCache(sQuery_header);
			if (!OutputXML_header.isEmpty()) {
				CreditCard.mLogger.info("RLOSCommon header: "+ OutputXML_header.get(0).get(0)+ " footer: "+ OutputXML_header.get(0).get(1)+ " parenttagname: " + OutputXML_header.get(0).get(2));
				header = OutputXML_header.get(0).get(0);
				footer = OutputXML_header.get(0).get(1);
				parentTagName = OutputXML_header.get(0).get(2);

				if (!("".equalsIgnoreCase(Operation_name) || Operation_name==null)) {
					CreditCard.mLogger.info("inside if of operation"+"operation: " + Operation_name+ "\n callName: "+ callName);
					sQuery = "SELECT "+ col_n+ " FROM NG_Integration_CC_field_Mapping with (nolock) where Call_name='"+ callName+ "' and active = 'Y' and Operation_name='"+ Operation_name + "' ORDER BY tag_seq ASC";

				} else {
					CreditCard.mLogger.info("inside else of operation"+"operation" + Operation_name);
					sQuery = "SELECT "+ col_n+ " FROM NG_Integration_CC_field_Mapping with (nolock) where Call_name='"+ callName+ "' and active = 'Y' ORDER BY tag_seq ASC";
				}

				CreditCard.mLogger.info("RLOSCommon"+ "sQuery " + sQuery);
				List<List<String>> DB_List=formObject.getNGDataFromDataCache(sQuery);//chnage to get data from DB directly
				CreditCard.mLogger.info("OutputXML"+ "OutputXML" + DB_List);

				if(!DB_List.isEmpty()){
					// CreditCard.mlogger.info("$$AKSHAY"+OutputXML.get(0).get(0)+OutputXML.get(0).get(1)+OutputXML.get(0).get(2)+OutputXML.get(0).get(3));
					CreditCard.mLogger.info("GenerateXML Integration field mapping table"+	DB_List.get(0).get(0) + DB_List.get(0).get(1)+ DB_List.get(0).get(2)+ DB_List.get(0).get(3)+ DB_List.get(0).get(4));
					CreditCard.mLogger.info("GenerateXML Integration field mapping table"+	DB_List.get(0).get(0) + DB_List.get(0).get(1)+ DB_List.get(0).get(2)+ DB_List.get(0).get(3));

					Map<String, String> int_xml = new LinkedHashMap<String, String>();

					if ("DEDUP_SUMMARY".equalsIgnoreCase(callName)) {
						int_xml = DEDUP_SUMMARY_Custom(DB_List,formObject,callName);
					} else if ("BLACKLIST_DETAILS".equalsIgnoreCase(callName)) {
						int_xml = Blacklist_Details_custom(DB_List,formObject,callName);
					} else if ("NEW_CUSTOMER_REQ".equalsIgnoreCase(callName)) {
						int_xml = NEW_CUSTOMER_Custom(DB_List,formObject,callName);
					} else if ("CUSTOMER_UPDATE_REQ".equalsIgnoreCase(callName)) {
						int_xml = CUSTOMER_UPDATE_Custom(DB_List,formObject,callName);
					} else if ("NEW_CARD_REQ".equalsIgnoreCase(callName)) {
						int_xml = NEW_CARD_Custom(DB_List,formObject,callName);
					} else if ("DECTECH".equalsIgnoreCase(callName)) {
						int_xml = DECTECH_Custom(DB_List,formObject,callName);
					} else if ("CHEQUE_BOOK_ELIGIBILITY".equalsIgnoreCase(callName)) {
						int_xml = CHEQUE_BOOK_ELIGIBILITY_Custom(DB_List,formObject,callName);
					} else if ("CARD_SERVICES_REQUEST".equalsIgnoreCase(callName)) {
						int_xml = CARD_SERVICES_REQUEST_Custom(DB_List,formObject,callName);
					}
					else if ("CARD_NOTIFICATION".equalsIgnoreCase(callName)) {
						int_xml = CARD_NOTIFICATION_Custom(DB_List,formObject,callName);
					}
					else if ("NEW_CREDITCARD_REQ".equalsIgnoreCase(callName)) {
						int_xml = NEW_CREDITCARD_Custom(DB_List,formObject,callName);
					}
					else{
						int_xml = Non_Custom(DB_List,formObject,callName);
						//new method added for method in which nothing custom is required.
					}

					final_xml = final_xml.append("<").append(parentTagName).append(">");
					CreditCard.mLogger.info("RLOS"+ "Final XMLold--"+ final_xml);

					Iterator<Map.Entry<String, String>> itr = int_xml.entrySet().iterator();
					CreditCard.mLogger.info("itr of hashmap"+ "itr" + itr);
					while (itr.hasNext()) {
						Map.Entry<String, String> entry = itr.next();
						CreditCard.mLogger.info("entry of hashmap"+ "entry"+ entry);
						if (final_xml.indexOf((entry.getKey())) > -1) {
							CreditCard.mLogger.info("RLOS"+ "itr_value: Key: "
									+ entry.getKey() + " Value: "
									+ entry.getValue());
							final_xml = final_xml.insert(final_xml.indexOf("<" + entry.getKey() + ">")
									+ entry.getKey().length() + 2, entry.getValue());
							CreditCard.mLogger.info("value of final xml"+"final_xml" + final_xml);
							itr.remove();
						}
					}
					final_xml = final_xml.append("</").append(parentTagName).append(">");
					CreditCard.mLogger.info("CC_Common"+ "final_xml: "+ final_xml);
					final_xml = new StringBuilder( Clean_Xml(final_xml.toString()));
					CreditCard.mLogger.info("FInal XMLnew is: "+ final_xml.toString());
					final_xml.insert(0, header);
					final_xml.append(footer);
					CreditCard.mLogger.info("FInal XMLnew with header: "+final_xml.toString());
					formObject.setNGValue("Is_" + callName, "Y");
					CreditCard.mLogger.info("value of " + callName + " Flag: "+ formObject.getNGValue("Is_" + callName));

					return MQ_connection_response(final_xml);


				} else {
					CreditCard.mLogger.info("Genrate XML: "+"Entry is not maintained in field mapping Master table for : "+ callName);
					return "Call not maintained";
				}

			} else {
				CreditCard.mLogger.info("Genrate XML: "+"Entry is not maintained in Master table for : "+ callName);
				return "Call not maintained";
			}
		}

		catch (Exception e) {
			CreditCard.mLogger.info("Exception ocurred: "+ e.getLocalizedMessage()+CC_Common.printException(e));
			CreditCard.mLogger.info("CC_Common"+ "$$final_xml: " + final_xml);
			CreditCard.mLogger.info("CC_Common"+
					"Exception occured in main thread: " + e.getMessage());
			return "0";
		}
	}



	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : GMq Connection response           

	 ***********************************************************************************  */
	@SuppressWarnings("resource")
	public String MQ_connection_response(StringBuilder finalXml) {

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		Socket socket = null;
		OutputStream out = null;
		InputStream socketInputStream = null;
		DataOutputStream dout = null;
		DataInputStream din = null;
		String mqOutputResponse = null;
		String mqInputRequest = null;
		String cabinetName = null;
		String wi_name = null;
		String ws_name = null;
		String sessionID = null;
		String userName = null;
		String socketServerIP;
		int socketServerPort;
		try {


			cabinetName = FormContext.getCurrentInstance().getFormConfig().getConfigElement("EngineName");
			CreditCard.mLogger.info("$$outputgGridtXML "+"cabinetName " + cabinetName);
			wi_name = FormContext.getCurrentInstance().getFormConfig().getConfigElement("ProcessInstanceId");
			ws_name = FormContext.getCurrentInstance().getFormConfig().getConfigElement("ActivityName");
			CreditCard.mLogger.info("$$outputgGridtXML "+"ActivityName " + ws_name);
			sessionID = FormContext.getCurrentInstance().getFormConfig().getConfigElement("DMSSessionId");
			userName = FormContext.getCurrentInstance().getFormConfig().getConfigElement("UserName");
			CreditCard.mLogger.info("$$outputgGridtXML "+ "userName "+ userName);
			CreditCard.mLogger.info("$$outputgGridtXML "+ "sessionID "+ sessionID);

			String sMQuery = "SELECT SocketServerIP,SocketServerPort FROM NG_RLOS_MQ_TABLE with (nolock)";
			List<List<String>> outputMQXML = formObject.getNGDataFromDataCache(sMQuery);
			CreditCard.mLogger.info("$$outputgGridtXML "+ "sMQuery " + sMQuery);
			if (!outputMQXML.isEmpty()) {
				CreditCard.mLogger.info("$$outputgGridtXML "+ outputMQXML.get(0).get(0) + "," + outputMQXML.get(0).get(1));
				socketServerIP = outputMQXML.get(0).get(0);
				CreditCard.mLogger.info("$$outputgGridtXML "+ "socketServerIP " + socketServerIP);
				socketServerPort = Integer.parseInt(outputMQXML.get(0).get(1));
				CreditCard.mLogger.info("$$outputgGridtXML "+ "socketServerPort " + socketServerPort);
				if (!("".equalsIgnoreCase(socketServerIP) && socketServerIP == null && socketServerPort==0)) {
					socket = new Socket(socketServerIP, socketServerPort);
					out = socket.getOutputStream();
					socketInputStream = socket.getInputStream();
					dout = new DataOutputStream(out);
					din = new DataInputStream(socketInputStream);
					if(socket!=null)
					{
						socket.close();
					}
					mqOutputResponse = "";
					mqInputRequest = getMQInputXML(sessionID, cabinetName,wi_name, ws_name, userName, finalXml);
					CreditCard.mLogger.info("$$outputgGridtXML "+"mqInputRequest " + mqInputRequest);

					if (mqInputRequest != null && mqInputRequest.length() > 0) {
						int outPut_len = mqInputRequest.getBytes("UTF-16LE").length;
						CreditCard.mLogger.info("Final XML output len: "+outPut_len + "");
						mqInputRequest = outPut_len + "##8##;" + mqInputRequest;
						CreditCard.mLogger.info("MqInputRequest"+"Input Request Bytes : "+ mqInputRequest.getBytes("UTF-16LE"));
						dout.write(mqInputRequest.getBytes("UTF-16LE"));dout.flush();
					}
					byte[] readBuffer = new byte[50000];
					int num = din.read(readBuffer);
					boolean wait_flag = true;
					int out_len = 0;

					if (num > 0) {while (wait_flag) {
						CreditCard.mLogger.info("MqOutputRequest"+ "num :"+ num);	
						byte[] arrayBytes = new byte[num];	
						System.arraycopy(readBuffer, 0, arrayBytes, 0, num);	
						mqOutputResponse = mqOutputResponse+ new String(arrayBytes, "UTF-16LE");	
						CreditCard.mLogger.info("MqOutputRequest"+"inside loop output Response :\n"+ mqOutputResponse);	
						if (mqOutputResponse.contains("##8##;")) 
						{		
							String[] mqOutputResponse_arr = mqOutputResponse.split("##8##;");		
							mqOutputResponse = mqOutputResponse_arr[1];		
							out_len = Integer.parseInt(mqOutputResponse_arr[0]);	
							CreditCard.mLogger.info("MqOutputRequest"+"First Output Response :\n"+ mqOutputResponse);	
							CreditCard.mLogger.info("MqOutputRequest"+"Output length :\n" + out_len);	
						}	
						if (out_len <= mqOutputResponse.getBytes("UTF-16LE").length) 
						{		
							wait_flag = false;	
						}	
						Thread.sleep(100);	
						num = din.read(readBuffer);
					}
					// Aman Code added for dectech to replace the &lt and// &gt start 13 sept 2017if (mqOutputResponse.contains("&lt;")) {	CreditCard.mLogger.info("MqOutputRequest"+"inside for Dectech :\n"+ mqOutputResponse);	mqOutputResponse = mqOutputResponse.replaceAll("&lt;", "<");	CreditCard.mLogger.info("MqOutputRequest"+"after replacing lt :\n"+ mqOutputResponse);	mqOutputResponse = mqOutputResponse.replaceAll("&gt;", ">");	CreditCard.mLogger.info("MqOutputRequest"+"after replacing gt :\n"+ mqOutputResponse);}// Aman Code added for dectech to replace the &lt and// &gt end 13 sept 2017
					if(mqOutputResponse.contains("&lt;")){
						//RLOS.mLogger.info("MqOutputRequest"+"inside for Dectech :\n"+mqOutputResponse);
						mqOutputResponse=mqOutputResponse.replaceAll("&lt;", "<");
						//RLOS.mLogger.info("MqOutputRequest"+"after replacing lt :\n"+mqOutputResponse);
						mqOutputResponse=mqOutputResponse.replaceAll("&gt;", ">");
						//RLOS.mLogger.info("MqOutputRequest"+"after replacing gt :\n"+mqOutputResponse);
					}
					CreditCard.mLogger.info("MqOutputRequest"+"Final Output Response :\n" + mqOutputResponse);socket.close();return mqOutputResponse;
					}

				} else {
					CreditCard.mLogger.info("SocketServerIp and SocketServerPort is not maintained "+"");
					CreditCard.mLogger.info("SocketServerIp is not maintained "+	socketServerIP);
					CreditCard.mLogger.info(" SocketServerPort is not maintained "+	socketServerPort);
					return "MQ details not maintained";
				}
			} else {
				CreditCard.mLogger.info("SOcket details are not maintained in NG_RLOS_MQ_TABLE table"+"");
				return "MQ details not maintained";
			}
			return "";

		} catch (Exception e) {
			return "";
		}
		finally
		{
			try
			{
				if(socket!=null)
				socket.close();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				CreditCard.mLogger.info(e);
				
			}
		}

	}

	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : Internal Bureau Indiviual Product        

	 ***********************************************************************************  */
	public String InternalBureauIndividualProducts() {
		CreditCard.mLogger.info("RLOSCommon java file"+"inside InternalBureauIndividualProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "SELECT cifid,agreementid,loantype,loantype,custroletype,loan_start_date,loanmaturitydate,lastupdatedate ,totaloutstandingamt,totalloanamount,NextInstallmentAmt,paymentmode,totalnoofinstalments,remaininginstalments,totalloanamount,      overdueamt,nofdayspmtdelay,monthsonbook,currentlycurrentflg,currmaxutil,DPD_30_in_last_6_months,DPD_60_in_last_18_months,propertyvalue,loan_disbursal_date,marketingcode,DPD_30_in_last_3_months,DPD_30_in_last_6_months,DPD_30_in_last_9_months,DPD_30_in_last_12_months,DPD_30_in_last_18_months,DPD_30_in_last_24_months,DPD_60_in_last_3_months,DPD_60_in_last_6_months,DPD_60_in_last_9_months,DPD_60_in_last_12_months,DPD_60_in_last_18_months,DPD_60_in_last_24_months,DPD_90_in_last_3_months,DPD_90_in_last_6_months,DPD_90_in_last_9_months,DPD_90_in_last_12_months,DPD_90_in_last_18_months,DPD_90_in_last_24_months,DPD_120_in_last_3_months,DPD_120_in_last_6_months,DPD_120_in_last_9_months,DPD_120_in_last_12_months,DPD_120_in_last_18_months,DPD_120_in_last_24_months,DPD_150_in_last_3_months,DPD_150_in_last_6_months,DPD_150_in_last_9_months,DPD_150_in_last_12_months,DPD_150_in_last_18_months,DPD_150_in_last_24_months,DPD_180_in_last_3_months,DPD_180_in_last_6_months,DPD_180_in_last_9_months,DPD_180_in_last_12_months,DPD_180_in_last_24_months,'' as col1,Consider_For_Obligations,LoanStat,'LOANS',writeoffStat,writeoffstatdt,lastrepmtdt,limit_increase,PartSettlementDetails,'' as SchemeCardProduct,General_Status,Internal_WriteOff_Check FROM ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) WHERE Child_wi = '"
			+ formObject.getWFWorkitemName()
			+ "' and LoanStat !='Pipeline' union select CifId,CardEmbossNum,CardType,CardType,CustRoleType,'' as col6,'' as col7,'' as col8,OutstandingAmt,CreditLimit,case when CardType like '%LOC%' then (select monthlyamount from ng_RLOS_CUSTEXPOSE_CardInstallmentDetails where child_wi ='"+formObject.getWFWorkitemName()+"' and CardCRNNumber=CardEmbossNum) else PaymentsAmount end,PaymentMode,'' as col13,'' as col14,CashLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,'' as col19,CurrMaxUtil,DPD_30_in_last_6_months,DPD_60_in_last_18_months,'' as col23,'' as col24,'' as col25,DPD_30_in_last_3_months,DPD_30_in_last_6_months,DPD_30_in_last_9_months,DPD_30_in_last_12_months,DPD_30_in_last_18_months,DPD_30_in_last_24_months,DPD_60_in_last_3_months,DPD_60_in_last_6_months,DPD_60_in_last_9_months,DPD_60_in_last_12_months,DPD_60_in_last_18_months,DPD_60_in_last_24_months,DPD_90_in_last_3_months,DPD_90_in_last_6_months,DPD_90_in_last_9_months,DPD_90_in_last_12_months,DPD_90_in_last_18_months,DPD_90_in_last_24_months,DPD_120_in_last_3_months,DPD_120_in_last_6_months,DPD_120_in_last_9_months,DPD_120_in_last_12_months,DPD_120_in_last_18_months,DPD_120_in_last_24_months,DPD_150_in_last_3_months,DPD_150_in_last_6_months,DPD_150_in_last_9_months,DPD_150_in_last_12_months,DPD_150_in_last_18_months,DPD_150_in_last_24_months,DPD_180_in_last_3_months,DPD_180_in_last_6_months,DPD_180_in_last_9_months,DPD_180_in_last_12_months,DPD_180_in_last_24_months,ExpiryDate,Consider_For_Obligations,CardStatus,'CARDS',writeoffStat,writeoffstatdt,'' as lastrepdate,limit_increase,'' as PartSettlementDetails,SchemeCardProd,General_Status,Internal_WriteOff_Check FROM ng_RLOS_CUSTEXPOSE_CardDetails with (nolock) where      Child_wi = '"
			+ formObject.getWFWorkitemName()
			+ "' and Request_Type In ('InternalExposure','CollectionsSummary') union select CifId,AcctId,'OverDraft' as loantype,'OverDraft' as loantype,CustRoleType,LimitSactionDate as loan_start_date,LimitExpiryDate as loanmaturitydate,'' as lastupdateddate,ClearBalanceAmount,SanctionLimit,'','','','',SanctionLimit,OverdueAmount,DaysPastDue,MonthsOnBook,IsCurrent,CurUtilRate,DPD30Last6Months,DPD60Last18Months,'',AccountOpenDate,'','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','',LimitExpiryDate,isNull(Consider_For_Obligations,'true'),AcctStat,'OVERDRAFT',WriteoffStat,WriteoffStatDt,LastRepmtDt,'','','','','','' from ng_rlos_custexpose_acctdetails where Child_wi = '"+formObject.getWFWorkitemName()+"'  and ODType != '' ";

		CreditCard.mLogger.info("InternalBureauIndividualProducts sQuery" + sQuery+ "");
		String CountQuery = "select count(*) from ng_RLOS_CUSTEXPOSE_CardDetails with (nolock) where Child_wi = '"+ formObject.getWFWorkitemName() + "' and cardStatus='A'";
		List<List<String>> CountXML = formObject.getNGDataFromDataCache(CountQuery);
		String add_xml_str = "";
		List<List<String>> OutputXML = formObject.getNGDataFromDataCache(sQuery);
		CreditCard.mLogger.info("InternalBureauIndividualProducts list size"+ OutputXML.size()+ "");
		CreditCard.mLogger.info("InternalBureauIndividualProducts list "+ OutputXML+ "");
		String ReqProd = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 1);
		String mol_sal_var = formObject.getNGValue("cmplx_MOL_molsalvar");

		try {
			for (int i = 0; i < OutputXML.size(); i++) {

				String cifId = "";
				String agreementId = "";
				String product_type = "";
				String loan_start_date = "";
				String loanmaturitydate = "";
				String lastupdatedate = "";

				String outstandingamt = "";
				String totalloanamount = "";
				String Emi = "";
				String paymentmode = "";
				String totalnoofinstalments = "";
				String remaininginstalments = "";
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
				String Internal_WriteOff_Check = "";
				String EmployerType = formObject.getNGValue("cmplx_EmploymentDetails_Emp_Type");
				String Kompass = formObject.getNGValue("cmplx_EmploymentDetails_Kompass");
				CreditCard.mLogger.info("Inside for"+ "asdasdasd");
				String paid_installment = "";
				if (!(OutputXML.get(i).get(0) == null || OutputXML.get(i).get(0).equals(""))) {
					cifId = OutputXML.get(i).get(0).toString();
				}
				if (!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals(""))) {
					agreementId = OutputXML.get(i).get(1).toString();
				}
				if (!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2).equals(""))) {
					product_type = OutputXML.get(i).get(2).toString();
					if ("Home In One".equalsIgnoreCase(product_type)) 
					{
						product_type = "HIO";
					} 
					else 
					{
						product_type = OutputXML.get(i).get(63).toString();
					}
				}
				if (!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals(""))) {
				}
				if (!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals(""))) {
				}
				if (!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5).equals(""))) {
					loan_start_date = OutputXML.get(i).get(5).toString();
				}
				if (OutputXML.get(i).get(6) != null && !OutputXML.get(i).get(6).isEmpty() && !OutputXML.get(i).get(6).equals("") && !OutputXML.get(i).get(6).equalsIgnoreCase("null")) {
					loanmaturitydate = OutputXML.get(i).get(6).toString();
				}
				if (OutputXML.get(i).get(7) != null && !OutputXML.get(i).get(7).isEmpty() && !OutputXML.get(i).get(7).equals("") && !OutputXML.get(i).get(7).equalsIgnoreCase("null")) {
					lastupdatedate = OutputXML.get(i).get(7).toString();
				}
				if (OutputXML.get(i).get(8) != null && !OutputXML.get(i).get(8).isEmpty() && !OutputXML.get(i).get(8).equals("") && !OutputXML.get(i).get(8).equalsIgnoreCase("null")) {
					outstandingamt = OutputXML.get(i).get(8).toString();
				}
				if (!(OutputXML.get(i).get(9) == null || OutputXML.get(i).get(9).equals(""))) {
					totalloanamount = OutputXML.get(i).get(9).toString();
				}
				if (!(OutputXML.get(i).get(10) == null || OutputXML.get(i).get(10).equals(""))) {
					Emi = OutputXML.get(i).get(10).toString();
				}
				if (!(OutputXML.get(i).get(11) == null || OutputXML.get(i).get(11).equals(""))) {
					paymentmode = OutputXML.get(i).get(11).toString();
				}
				if (OutputXML.get(i).get(12) != null && !OutputXML.get(i).get(12).isEmpty() && !OutputXML.get(i).get(12).equals("") && !OutputXML.get(i).get(12).equalsIgnoreCase("null")) {
					// SKLogger.writeLog("Inside for",
					// "asdasdasd"+OutputXML.get(i).get(12));
					totalnoofinstalments = OutputXML.get(i).get(12).toString();
				}
				if (!(OutputXML.get(i).get(13) == null || OutputXML.get(i).get(13).equals(""))) {
					remaininginstalments = OutputXML.get(i).get(13).toString();
				}
				if (!(OutputXML.get(i).get(14) == null || OutputXML.get(i).get(14).equals(""))) {
				}
				if (!(OutputXML.get(i).get(15) == null || OutputXML.get(i).get(15).equals(""))) {
					overdueamt = OutputXML.get(i).get(15).toString();
				}
				if (!(OutputXML.get(i).get(16) == null || OutputXML.get(i).get(16).equals(""))) {
					nofdayspmtdelay = OutputXML.get(i).get(16).toString();
				}
				if (!(OutputXML.get(i).get(17) == null || OutputXML.get(i).get(17).equals(""))) {
					monthsonbook = OutputXML.get(i).get(17).toString();
				}
				if (!(OutputXML.get(i).get(18) == null || OutputXML.get(i).get(18).equals(""))) {
					currentlycurrent = OutputXML.get(i).get(18).toString();
				}
				if (!(OutputXML.get(i).get(19) == null || OutputXML.get(i).get(19).equals(""))) {
					currmaxutil = OutputXML.get(i).get(19).toString();
				}
				if (!(OutputXML.get(i).get(20) == null || OutputXML.get(i).get(20).equals(""))) {
					DPD_30_in_last_6_months = OutputXML.get(i).get(20)	.toString();
				}
				if (!(OutputXML.get(i).get(21) == null || OutputXML.get(i).get(21).equals(""))) {
					DPD_60_in_last_18_months = OutputXML.get(i).get(21)	.toString();
				}
				if (!(OutputXML.get(i).get(22) == null || OutputXML.get(i).get(22).equals(""))) {
					propertyvalue = OutputXML.get(i).get(22).toString();
				}
				if (!(OutputXML.get(i).get(23) == null || OutputXML.get(i).get(23).equals(""))) {
					loan_disbursal_date = OutputXML.get(i).get(23).toString();
				}
				if (!(OutputXML.get(i).get(24) == null || OutputXML.get(i).get(24).equals(""))) {
					marketingcode = OutputXML.get(i).get(24).toString();
				}
				if (!(OutputXML.get(i).get(25) == null || OutputXML.get(i).get(25).equals(""))) {
					DPD_30_in_last_3_months = OutputXML.get(i).get(25)	.toString();
				}
				if (!(OutputXML.get(i).get(26) == null || OutputXML.get(i).get(26).equals(""))) {
					DPD_30_in_last_9_months = OutputXML.get(i).get(26)	.toString();
				}
				if (!(OutputXML.get(i).get(27) == null || OutputXML.get(i).get(27).equals(""))) {
					DPD_30_in_last_12_months = OutputXML.get(i).get(27)	.toString();
				}
				if (!(OutputXML.get(i).get(28) == null || OutputXML.get(i).get(28).equals(""))) {
					DPD_30_in_last_18_months = OutputXML.get(i).get(28)	.toString();
				}
				if (!(OutputXML.get(i).get(29) == null || OutputXML.get(i).get(29).equals(""))) {
					DPD_30_in_last_24_months = OutputXML.get(i).get(29)	.toString();
				}
				if (!(OutputXML.get(i).get(30) == null || OutputXML.get(i).get(30).equals(""))) {
					DPD_60_in_last_3_months = OutputXML.get(i).get(30)	.toString();
				}
				if (!(OutputXML.get(i).get(31) == null || OutputXML.get(i).get(31).equals(""))) {
					DPD_60_in_last_6_months = OutputXML.get(i).get(31)	.toString();
				}
				if (!(OutputXML.get(i).get(32) == null || OutputXML.get(i).get(32).equals(""))) {
					DPD_60_in_last_9_months = OutputXML.get(i).get(32)	.toString();
				}
				if (!(OutputXML.get(i).get(33) == null || OutputXML.get(i).get(33).equals(""))) {
					DPD_60_in_last_12_months = OutputXML.get(i).get(33)	.toString();
				}
				if (!(OutputXML.get(i).get(34) == null || OutputXML.get(i).get(34).equals(""))) {
					DPD_60_in_last_24_months = OutputXML.get(i).get(34)	.toString();
				}
				if (!(OutputXML.get(i).get(35) == null || OutputXML.get(i).get(35).equals(""))) {
					DPD_90_in_last_3_months = OutputXML.get(i).get(35)	.toString();
				}
				if (!(OutputXML.get(i).get(36) == null || OutputXML.get(i).get(36).equals(""))) {
					DPD_90_in_last_6_months = OutputXML.get(i).get(36)	.toString();
				}
				if (!(OutputXML.get(i).get(37) == null || OutputXML.get(i).get(37).equals(""))) {
					DPD_90_in_last_9_months = OutputXML.get(i).get(37)	.toString();
				}
				if (!(OutputXML.get(i).get(38) == null || OutputXML.get(i).get(38).equals(""))) {
					DPD_90_in_last_12_months = OutputXML.get(i).get(38)	.toString();
				}
				if (!(OutputXML.get(i).get(39) == null || OutputXML.get(i).get(39).equals(""))) {
					DPD_90_in_last_18_months = OutputXML.get(i).get(39)	.toString();
				}
				if (!(OutputXML.get(i).get(40) == null || OutputXML.get(i).get(40).equals(""))) {
					DPD_90_in_last_24_months = OutputXML.get(i).get(40)	.toString();
				}
				if (!(OutputXML.get(i).get(41) == null || OutputXML.get(i).get(41).equals(""))) {
					DPD_120_in_last_3_months = OutputXML.get(i).get(41)	.toString();
				}
				if (!(OutputXML.get(i).get(42) == null || OutputXML.get(i).get(42).equals(""))) {
					DPD_120_in_last_6_months = OutputXML.get(i).get(42)	.toString();
				}
				if (!(OutputXML.get(i).get(43) == null || OutputXML.get(i).get(43).equals(""))) {
					DPD_120_in_last_9_months = OutputXML.get(i).get(43)	.toString();
				}
				if (!(OutputXML.get(i).get(44) == null || OutputXML.get(i).get(44).equals(""))) {
					DPD_120_in_last_12_months = OutputXML.get(i).get(44)	.toString();
				}
				if (!(OutputXML.get(i).get(45) == null || OutputXML.get(i).get(45).equals(""))) {
					DPD_120_in_last_18_months = OutputXML.get(i).get(45)	.toString();
				}
				if (!(OutputXML.get(i).get(46) == null || OutputXML.get(i).get(46).equals(""))) {
					DPD_120_in_last_24_months = OutputXML.get(i).get(46)	.toString();
				}
				if (!(OutputXML.get(i).get(47) == null || OutputXML.get(i).get(47).equals(""))) {
					DPD_150_in_last_3_months = OutputXML.get(i).get(47)	.toString();
				}
				if (!(OutputXML.get(i).get(48) == null || OutputXML.get(i).get(48).equals(""))) {
					DPD_150_in_last_6_months = OutputXML.get(i).get(48)	.toString();
				}
				if (!(OutputXML.get(i).get(49) == null || OutputXML.get(i).get(49).equals(""))) {
					DPD_150_in_last_9_months = OutputXML.get(i).get(49)	.toString();
				}
				if (!(OutputXML.get(i).get(50) == null || OutputXML.get(i).get(50).equals(""))) {
					DPD_150_in_last_12_months = OutputXML.get(i).get(50)	.toString();
				}
				if (!(OutputXML.get(i).get(51) == null || OutputXML.get(i).get(51).equals(""))) {
					DPD_150_in_last_18_months = OutputXML.get(i).get(51)	.toString();
				}
				if (!(OutputXML.get(i).get(52) == null || OutputXML.get(i).get(52).equals(""))) {
					DPD_150_in_last_24_months = OutputXML.get(i).get(52)	.toString();
				}
				if (!(OutputXML.get(i).get(53) == null || OutputXML.get(i).get(53).equals(""))) {
					DPD_180_in_last_3_months = OutputXML.get(i).get(53)	.toString();
				}
				if (!(OutputXML.get(i).get(54) == null || OutputXML.get(i).get(54).equals(""))) {
					DPD_180_in_last_6_months = OutputXML.get(i).get(54)	.toString();
				}
				if (!(OutputXML.get(i).get(55) == null || OutputXML.get(i).get(55).equals(""))) {
					DPD_180_in_last_9_months = OutputXML.get(i).get(55)	.toString();
				}
				if (!(OutputXML.get(i).get(56) == null || OutputXML.get(i).get(56).equals(""))) {
					DPD_180_in_last_12_months = OutputXML.get(i).get(56)	.toString();
				}
				if (!(OutputXML.get(i).get(57) == null || OutputXML.get(i).get(57).equals(""))) {
					DPD_180_in_last_24_months = OutputXML.get(i).get(57)	.toString();
				}
				if (!(OutputXML.get(i).get(60) == null || OutputXML.get(i).get(60).equals(""))) {
					CardExpiryDate = OutputXML.get(i).get(60).toString();
				}

				if (!(OutputXML.get(i).get(61) == null || OutputXML.get(i).get(61).equals(""))) {
					Consider_For_Obligations = OutputXML.get(i).get(61)	.toString();
					if ("true".equalsIgnoreCase(Consider_For_Obligations)) 
					{
						Consider_For_Obligations = "Y";
					} 
					else 
					{
						Consider_For_Obligations = "N";
					}
				}

				if (!(OutputXML.get(i).get(62) == null || OutputXML.get(i).get(62).equals(""))) {
					phase = OutputXML.get(i).get(62).toString();
					if (phase.startsWith("C")) {phase = "C";
					} else {phase = "A";
					}
				}
				if (!(OutputXML.get(i).get(64) == null || OutputXML.get(i).get(64).equals(""))) {
					writeoffStat = OutputXML.get(i).get(64).toString();
				}
				if (!(OutputXML.get(i).get(65) == null || OutputXML.get(i).get(65).equals(""))) {
					writeoffstatdt = OutputXML.get(i).get(65).toString();
				}
				if (!(OutputXML.get(i).get(66) == null || OutputXML.get(i).get(66).equals(""))) {
					lastrepmtdt = OutputXML.get(i).get(66).toString();
				}
				if (!(OutputXML.get(i).get(67) == null || OutputXML.get(i).get(67).equals(""))) {
					Limit_increase = OutputXML.get(i).get(67).toString();
					if ("false".equalsIgnoreCase(Limit_increase)) 
					{
						Limit_increase = "N";
					} 
					else 
					{
						Limit_increase = "Y";
					}
				}
				if (!(OutputXML.get(i).get(68) == null || OutputXML.get(i).get(68).equals(""))) {
					// commented by aman 22-10-2017 to handdle part settelment
					// data
					// part_settlement_date =
					// OutputXML.get(i).get(67).toString();
					String abc = OutputXML.get(i).get(68).toString();
					abc = abc.substring(0, 10) + "split"+ abc.substring(10, abc.length());
					String abcsa[] = abc.split("split");
					part_settlement_date = abcsa[0];
					part_settlement_amount = abcsa[1];
				}
				if (!(OutputXML.get(i).get(69) == null || OutputXML.get(i).get(69).equals(""))) {
					SchemeCardProduct = OutputXML.get(i).get(69).toString();
				}
				if (!(OutputXML.get(i).get(70) == null || OutputXML.get(i).get(70).equals(""))) {
					General_Status = OutputXML.get(i).get(70).toString();
				}
				if (!(OutputXML.get(i).get(71) == null || OutputXML.get(i).get(71).equals(""))) {
					Internal_WriteOff_Check = OutputXML.get(i).get(71)	.toString();
				}
				String sQueryCombinedLimit = "select Distinct(COMBINEDLIMIT_ELIGIBILITY) from ng_master_cardProduct with (nolock) where code='" + SchemeCardProduct + "'";
				List<List<String>> sQueryCombinedLimitXML = formObject.getNGDataFromDataCache(sQueryCombinedLimit);
				try{
					if(sQueryCombinedLimitXML!=null && sQueryCombinedLimitXML.size()>0 && sQueryCombinedLimitXML.get(0)!=null){
						Combined_Limit=sQueryCombinedLimitXML.get(0).get(0).equalsIgnoreCase("1")?"Y":"N";
					}
				}
				catch(Exception e){
					CreditCard.mLogger.info("Exception occured at sQueryCombinedLimit for"+sQueryCombinedLimit);

				}
				String sQuerySecuredCard = "select count(*) from ng_master_cardProduct with (nolock) where code='" + SchemeCardProduct + "'  and subproduct='SEC'";
				List<List<String>> sQuerySecuredCardXML = formObject.getNGDataFromDataCache(sQuerySecuredCard);
				try{
					if(sQuerySecuredCardXML!=null && sQuerySecuredCardXML.size()>0 && sQuerySecuredCardXML.get(0)!=null){
						SecuredCard=sQuerySecuredCardXML.get(0).get(0).equalsIgnoreCase("0")?"N":"Y";
					}	
				}
				catch(Exception e){
					CreditCard.mLogger.info("Exception occured at SecuredCard for"+SecuredCard);

				}

				if (cifId != null && !"".equalsIgnoreCase(cifId) && !"null".equalsIgnoreCase(cifId)) {
					CreditCard.mLogger.info("Inside if"+ "asdasdasd");
					add_xml_str = add_xml_str+ "<InternalBureauIndividualProducts><applicant_id>"+ cifId + "</applicant_id>";
					add_xml_str = add_xml_str+ "<internal_bureau_individual_products_id>"+ agreementId+ "</internal_bureau_individual_products_id>";
					add_xml_str = add_xml_str + "<type_product>" + product_type+ "</type_product>";
					if ("cards".equalsIgnoreCase(OutputXML.get(i).get(63))) {
						if (SchemeCardProduct.startsWith("LOC")){
							add_xml_str = add_xml_str + "<contract_type>IM</contract_type>";
						}
						else{
						add_xml_str = add_xml_str + "<contract_type>CC</contract_type>";
						}
					
					}
					if ("Loans".equalsIgnoreCase(OutputXML.get(i).get(63))) {
						add_xml_str = add_xml_str+ "<contract_type>PL</contract_type>";
					}
					add_xml_str = add_xml_str + "<provider_no>" + "RAKBANK"+ "</provider_no>";
					add_xml_str = add_xml_str + "<phase>" + phase + "</phase>";
					add_xml_str = add_xml_str+ "<role_of_customer>Primary</role_of_customer>";

					add_xml_str = add_xml_str + "<start_date>"+ loan_start_date + "</start_date>";
					add_xml_str = add_xml_str + "<close_date>"+ loanmaturitydate + "</close_date>";
					add_xml_str = add_xml_str + "<date_last_updated>"+ lastupdatedate + "</date_last_updated>";
					add_xml_str = add_xml_str + "<outstanding_balance>"+ outstandingamt + "</outstanding_balance>";
					if ("Loans".equalsIgnoreCase(OutputXML.get(i).get(63))) {
						add_xml_str = add_xml_str + "<total_amount>"+ totalloanamount + "</total_amount>";
					}
					add_xml_str = add_xml_str + "<payments_amount>" + Emi+ "</payments_amount>";
					add_xml_str = add_xml_str + "<method_of_payment>"+ paymentmode + "</method_of_payment>";
					add_xml_str = add_xml_str + "<total_no_of_instalments>"+ totalnoofinstalments+ "</total_no_of_instalments>";
					add_xml_str = add_xml_str + "<no_of_remaining_instalments>"+ remaininginstalments+ "</no_of_remaining_instalments>";
					add_xml_str = add_xml_str + "<worst_status>" + writeoffStat+ "</worst_status>";
					add_xml_str = add_xml_str + "<worst_status_date>"+ writeoffstatdt + "</worst_status_date>";
					if ("cards".equalsIgnoreCase(OutputXML.get(i).get(63))) {
						add_xml_str = add_xml_str + "<credit_limit>"+ totalloanamount + "</credit_limit>";
					}
					add_xml_str = add_xml_str + "<overdue_amount>" + overdueamt+ "</overdue_amount>";
					add_xml_str = add_xml_str + "<no_of_days_payment_delay>"+ nofdayspmtdelay + "</no_of_days_payment_delay>";
					add_xml_str = add_xml_str + "<mob>" + monthsonbook+ "</mob>";
					add_xml_str = add_xml_str + "<last_repayment_date>"+ lastrepmtdt + "</last_repayment_date>";
					add_xml_str = add_xml_str + "<currently_current>"+ currentlycurrent + "</currently_current>";
					add_xml_str = add_xml_str + "<current_utilization>"+ currmaxutil + "</current_utilization>";
					add_xml_str = add_xml_str + "<dpd_30_last_6_mon>"+ DPD_30_in_last_6_months + "</dpd_30_last_6_mon>";
					add_xml_str = add_xml_str + "<dpd_60p_in_last_18_mon>"+ DPD_60_in_last_18_months+ "</dpd_60p_in_last_18_mon>";

					add_xml_str = add_xml_str + "<card_product>"+ SchemeCardProduct + "</card_product>";
					add_xml_str = add_xml_str + "<property_value>"+ propertyvalue + "</property_value>";
					add_xml_str = add_xml_str + "<disbursal_date>"+ loan_disbursal_date + "</disbursal_date>";
					add_xml_str = add_xml_str + "<marketing_code>"+ marketingcode + "</marketing_code>";
					add_xml_str = add_xml_str + "<card_expiry_date>"+ CardExpiryDate + "</card_expiry_date>";
					add_xml_str = add_xml_str + "<card_upgrade_indicator>"+ Limit_increase + "</card_upgrade_indicator>";
					add_xml_str = add_xml_str + "<part_settlement_date>"+ part_settlement_date + "</part_settlement_date>";
					add_xml_str = add_xml_str + "<part_settlement_amount>"+ part_settlement_amount+ "</part_settlement_amount>";
					add_xml_str = add_xml_str + "<part_settlement_reason>" + ""+ "</part_settlement_reason>";
					add_xml_str = add_xml_str + "<limit_expiry_date>" + ""+ "</limit_expiry_date>";
					add_xml_str = add_xml_str + "<no_of_primary_cards>"+ CountXML.get(0).get(0) + "</no_of_primary_cards>";
					add_xml_str = add_xml_str + "<no_of_repayments_done>"+ remaininginstalments + "</no_of_repayments_done>";
					add_xml_str = add_xml_str + "<card_segment>"+ SchemeCardProduct + "</card_segment>";
					add_xml_str = add_xml_str + "<product_type>"+ OutputXML.get(i).get(63) + "</product_type>";
					add_xml_str = add_xml_str + "<product_category>"+ SchemeCardProduct + "</product_category>";
					add_xml_str = add_xml_str + "<combined_limit_flag>"+ Combined_Limit + "</combined_limit_flag>";
					add_xml_str = add_xml_str + "<secured_card_flag>"+ SecuredCard + "</secured_card_flag>";
					add_xml_str = add_xml_str + "<resch_tko_flag>"+ Limit_increase + "</resch_tko_flag>";

					add_xml_str = add_xml_str + "<general_status>"+ General_Status + "</general_status>";
					add_xml_str = add_xml_str + "<consider_for_obligation>"+ Consider_For_Obligations+ "</consider_for_obligation>";
					add_xml_str = add_xml_str + "<limit_increase>"+ Limit_increase + "</limit_increase>";

					add_xml_str = add_xml_str + "<role>Primary</role>";
					add_xml_str = add_xml_str + "<limit>" + "" + "</limit>";
					add_xml_str = add_xml_str + "<status>" + phase+ "</status>";
					add_xml_str = add_xml_str + "<emi>" + Emi + "</emi>";
					add_xml_str = add_xml_str + "<os_amt>" + outstandingamt+ "</os_amt>";

					add_xml_str = add_xml_str + "<dpd_30_in_last_3mon>"+ DPD_30_in_last_3_months+ "</dpd_30_in_last_3mon>";
					add_xml_str = add_xml_str + "<dpd_30_in_last_6mon>"+ DPD_30_in_last_6_months+ "</dpd_30_in_last_6mon>";
					add_xml_str = add_xml_str + "<dpd_30_in_last_9mon>"+ DPD_30_in_last_9_months+ "</dpd_30_in_last_9mon>";
					add_xml_str = add_xml_str + "<dpd_30_in_last_12mon>"+ DPD_30_in_last_12_months+ "</dpd_30_in_last_12mon>";

					add_xml_str = add_xml_str + "<dpd_30_in_last_18mon>"+ DPD_30_in_last_18_months+ "</dpd_30_in_last_18mon>";
					add_xml_str = add_xml_str + "<dpd_30_in_last_24mon>"+ DPD_30_in_last_24_months+ "</dpd_30_in_last_24mon>";
					add_xml_str = add_xml_str + "<dpd_60_in_last_3mon>"+ DPD_60_in_last_3_months+ "</dpd_60_in_last_3mon>";
					add_xml_str = add_xml_str + "<dpd_60_in_last_6mon>"+ DPD_60_in_last_6_months+ "</dpd_60_in_last_6mon>";
					add_xml_str = add_xml_str + "<dpd_60_in_last_9mon>"+ DPD_60_in_last_9_months+ "</dpd_60_in_last_9mon>";
					add_xml_str = add_xml_str + "<dpd_60_in_last_12mon>"+ DPD_60_in_last_12_months+ "</dpd_60_in_last_12mon>";
					add_xml_str = add_xml_str + "<dpd_60_in_last_18mon>"+ DPD_60_in_last_18_months+ "</dpd_60_in_last_18mon>";
					add_xml_str = add_xml_str + "<dpd_60_in_last_24mon>"+ DPD_60_in_last_24_months+ "</dpd_60_in_last_24mon>";
					add_xml_str = add_xml_str + "<dpd_90_in_last_3mon>"+ DPD_90_in_last_3_months+ "</dpd_90_in_last_3mon>";
					add_xml_str = add_xml_str + "<dpd_90_in_last_6mon>"+ DPD_90_in_last_6_months+ "</dpd_90_in_last_6mon>";
					add_xml_str = add_xml_str + "<dpd_90_in_last_9mon>"+ DPD_90_in_last_9_months+ "</dpd_90_in_last_9mon>";
					add_xml_str = add_xml_str + "<dpd_90_in_last_12mon>"+ DPD_90_in_last_12_months+ "</dpd_90_in_last_12mon>";
					add_xml_str = add_xml_str + "<dpd_90_in_last_18mon>"+ DPD_90_in_last_18_months+ "</dpd_90_in_last_18mon>";
					add_xml_str = add_xml_str + "<dpd_90_in_last_24mon>"+ DPD_90_in_last_24_months+ "</dpd_90_in_last_24mon>";
					add_xml_str = add_xml_str + "<dpd_120_in_last_3mon>"+ DPD_120_in_last_3_months+ "</dpd_120_in_last_3mon>";
					add_xml_str = add_xml_str + "<dpd_120_in_last_6mon>"+ DPD_120_in_last_6_months+ "</dpd_120_in_last_6mon>";
					add_xml_str = add_xml_str + "<dpd_120_in_last_9mon>"+ DPD_120_in_last_9_months+ "</dpd_120_in_last_9mon>";
					add_xml_str = add_xml_str + "<dpd_120_in_last_12mon>"+ DPD_120_in_last_12_months+ "</dpd_120_in_last_12mon>";
					add_xml_str = add_xml_str + "<dpd_120_in_last_18mon>"+ DPD_120_in_last_18_months+ "</dpd_120_in_last_18mon>";
					add_xml_str = add_xml_str + "<dpd_120_in_last_24mon>"+ DPD_120_in_last_24_months+ "</dpd_120_in_last_24mon>";

					add_xml_str = add_xml_str + "<dpd_150_in_last_3mon>"+ DPD_150_in_last_3_months+ "</dpd_150_in_last_3mon>";
					add_xml_str = add_xml_str + "<dpd_150_in_last_6mon>"+ DPD_150_in_last_6_months+ "</dpd_150_in_last_6mon>";
					add_xml_str = add_xml_str + "<dpd_150_in_last_9mon>"+ DPD_150_in_last_9_months+ "</dpd_150_in_last_9mon>";
					add_xml_str = add_xml_str + "<dpd_150_in_last_12mon>"+ DPD_150_in_last_12_months+ "</dpd_150_in_last_12mon>";
					add_xml_str = add_xml_str + "<dpd_150_in_last_18mon>"+ DPD_150_in_last_18_months+ "</dpd_150_in_last_18mon>";
					add_xml_str = add_xml_str + "<dpd_150_in_last_24mon>"+ DPD_150_in_last_24_months+ "</dpd_150_in_last_24mon>";
					add_xml_str = add_xml_str + "<dpd_180_in_last_3mon>"+ DPD_180_in_last_3_months+ "</dpd_180_in_last_3mon>";
					add_xml_str = add_xml_str + "<dpd_180_in_last_6mon>"+ DPD_180_in_last_6_months+ "</dpd_180_in_last_6mon>";
					add_xml_str = add_xml_str + "<dpd_180_in_last_9mon>"+ DPD_180_in_last_9_months+ "</dpd_180_in_last_9mon>";
					add_xml_str = add_xml_str + "<dpd_180_in_last_12mon>"+ DPD_180_in_last_12_months+ "</dpd_180_in_last_12mon>";
					add_xml_str = add_xml_str + "<dpd_180_in_last_18mon>" + ""+ "</dpd_180_in_last_18mon>";
					add_xml_str = add_xml_str + "<dpd_180_in_last_24mon>"+ DPD_180_in_last_24_months+ "</dpd_180_in_last_24mon>";
					add_xml_str = add_xml_str + "<last_temp_limit_exp>" + ""+ "</last_temp_limit_exp>";
					add_xml_str = add_xml_str + "<last_per_limit_exp>" + ""+ "</last_per_limit_exp>";
					add_xml_str = add_xml_str + "<security_cheque_amt>" + ""+ "</security_cheque_amt>";
					add_xml_str = add_xml_str + "<mol_salary_variance>"+ mol_sal_var + "</mol_salary_variance>";
					if (Kompass != null) 
					{
						if ("true".equalsIgnoreCase(Kompass))
						{	
							add_xml_str = add_xml_str + "<kompass>" + "Y"+ "</kompass>";
						} else 
						{	
							add_xml_str = add_xml_str + "<kompass>" + "N"+ "</kompass>";
						}
					}
					add_xml_str = add_xml_str + "<employer_type>"+ EmployerType + "</employer_type>";

					if (totalnoofinstalments != null	&& remaininginstalments != null	&& !totalnoofinstalments.equals("")	&& !remaininginstalments.equals("")) 
					{
						paid_installment = Integer.toString(Integer.parseInt(totalnoofinstalments)- Integer.parseInt(remaininginstalments));
						CreditCard.mLogger.info("Inside paid_installment"+"paid_installment" + paid_installment);

					}
					if (NGFUserResourceMgr_CreditCard.getGlobalVar("CC_CreditCard").equalsIgnoreCase(ReqProd)) 
					{
						add_xml_str = add_xml_str+ "<no_of_paid_installment>"+ paid_installment+ "</no_of_paid_installment><write_off_amount>"+ Internal_WriteOff_Check+ "</write_off_amount><company_flag>N</company_flag></InternalBureauIndividualProducts>";
					} 
					else 
					{
						add_xml_str = add_xml_str+ "<no_of_paid_installment>"+ paid_installment+ "</no_of_paid_installment><company_flag>N</company_flag><type_of_od>"+ ""+ "</type_of_od><amt_paid_last6mnths>"+ ""+ "</amt_paid_last6mnths></InternalBureauIndividualProducts>";

					}
				}

			}
		} catch (Exception e) {
			CreditCard.mLogger.info("RLOSCommon"+ "Internal liab tag Cration: " + CC_Common.printException(e));
		}
		CreditCard.mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "	+ add_xml_str);
		return add_xml_str;
	}
	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : get Customer Adress Details       

	 ***********************************************************************************  */
	public String getCustAddress_details(String call_name){
		CreditCard.mLogger.info("RLOSCommon java file"+ " inside getCustAddress_details : ");
		String  add_xml_str ="";
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			int add_row_count = formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
			CreditCard.mLogger.info("RLOSCommon java file"+ " inside getCustAddress_details add_row_count+ : "+add_row_count);

			for (int i = 0; i<add_row_count;i++){
				String Address_type = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0); //0
				String flat_Villa=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 1);//1
				String Building_name=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 2);//2
				String street_name=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 3);//3
				String Landmard=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 4);//4
				String city = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 5);//5
				String Emirates=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 6);//6
				String country=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 7);//7
				String Po_Box=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 8);//8
				String years_in_current_add=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 9);//9

				//added here
				CreditCard.mLogger.info("RLOSCommon java file"+ " inside getCustAddress_details add_row_count+ : "+years_in_current_add);
				int years=0;
				
				//ended here
				CreditCard.mLogger.info("PLCommon java file"+ " ADD Type: "+Address_type);
				/*if (Address_type.equalsIgnoreCase("HOME")){
					Address_type="Home Country";
				}*/
				CreditCard.mLogger.info("PLCommon java file"+ " ADD Type after: "+Address_type);

				String preferrd="";
				//Code change to added Effective from and to start
				String EffectiveFrom="";
				String EffectiveTo="";
				if (!"".equalsIgnoreCase(years_in_current_add)){
					years=(int)Float.parseFloat(years_in_current_add);
					
				SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
				Calendar cal = Calendar.getInstance();
				EffectiveTo=sdf1.format(cal.getTime());
				cal.add(Calendar.YEAR, -years);
				EffectiveFrom=sdf1.format(cal.getTime());
				CreditCard.mLogger.info("RLOS value of CurrentDate EffectiveTo"+" "+EffectiveTo);
				CreditCard.mLogger.info("RLOS value of EffectiveFromDate"+" "+EffectiveFrom);
				}
				//Code change to added Effective from and to End
				CreditCard.mLogger.info("RLOS value of prefered Add: Address Type: "+Address_type+" Address pref flag: "+formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 11));
				if(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 11).equalsIgnoreCase("true"))//10
					preferrd = "Y";
				else
					preferrd = "N";


				if(call_name.equalsIgnoreCase("CUSTOMER_UPDATE_REQ")){
					add_xml_str = add_xml_str + "<AddrDet><AddressType>"+Address_type+"</AddressType>";
					//Code change to added Effective from and to start
					add_xml_str = add_xml_str + "<EffectiveFrom>"+EffectiveFrom+"</EffectiveFrom>";
					add_xml_str = add_xml_str + "<EffectiveTo>"+EffectiveTo+"</EffectiveTo>";
					add_xml_str = add_xml_str + "<HoldMailFlag>N</HoldMailFlag>";
					add_xml_str = add_xml_str + "<ReturnFlag>N</ReturnFlag>";
					add_xml_str = add_xml_str + "<AddrPrefFlag>"+preferrd+"</AddrPrefFlag>";
					//Code change to added Effective from and to End

					add_xml_str = add_xml_str + "<AddrLine1>"+flat_Villa+"</AddrLine1>";
					add_xml_str = add_xml_str + "<AddrLine2>"+Building_name+"</AddrLine2>";
					add_xml_str = add_xml_str + "<AddrLine3>"+street_name+"</AddrLine3>";
					add_xml_str = add_xml_str + "<AddrLine4>"+Landmard+"</AddrLine4>";
					add_xml_str = add_xml_str + "<POBox>"+Po_Box+"</POBox>";
					add_xml_str = add_xml_str + "<State>"+Emirates+"</State>";
					add_xml_str = add_xml_str + "<City>"+city+"</City>";
					add_xml_str = add_xml_str + "<CountryCode>"+country+"</CountryCode></AddrDet>";
					//add_xml_str = add_xml_str + "<POBox>"+Po_Box+"</POBox></AddrDet>";
				}
				else if(call_name.equalsIgnoreCase("CARD_NOTIFICATION")){
					if (Address_type.equalsIgnoreCase("HOME")){
						Address_type="Home Country";
					}
					add_xml_str = add_xml_str + "<AddrDet><AddressType>"+Address_type+"</AddressType>";
					add_xml_str = add_xml_str + "<AddressLine1>"+flat_Villa+"</AddressLine1>";
					add_xml_str = add_xml_str + "<AddressLine2>"+Building_name+"</AddressLine2>";
					add_xml_str = add_xml_str + "<AddressLine3>"+street_name+"</AddressLine3>";
					add_xml_str = add_xml_str + "<AddressLine4>"+Landmard+"</AddressLine4>";
					add_xml_str = add_xml_str + "<City>"+city+"</City>";
					add_xml_str = add_xml_str + "<State>"+Emirates+"</State>";
					add_xml_str = add_xml_str + "<Country>"+country+"</Country>";
					if(Address_type.equalsIgnoreCase("Home Country")){
						add_xml_str = add_xml_str + "<ZipCode>"+Po_Box+"</ZipCode>";
					}
					add_xml_str = add_xml_str + "<POBox>"+Po_Box+"</POBox>";
					add_xml_str = add_xml_str + "<EffectiveFromDate>"+EffectiveFrom+"</EffectiveFromDate>";
					add_xml_str = add_xml_str + "<EffectiveToDate>"+EffectiveTo+"</EffectiveToDate>";
					add_xml_str = add_xml_str + "<NumberOfYears>"+years+"</NumberOfYears>";
					add_xml_str = add_xml_str + "<ResidenceType>R</ResidenceType>";
					add_xml_str = add_xml_str + "<AddrPrefFlag>"+preferrd+"</AddrPrefFlag>"
					+ "</AddrDet>";
					//add_xml_str = add_xml_str + "<POBox>"+Po_Box+"</POBox></AddrDet>";
				}
				else if (call_name.equalsIgnoreCase("NEW_CUSTOMER_REQ")){
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

				//10/08/2017 added for the respective call
				else if(call_name.equalsIgnoreCase("NEW_CREDITCARD_REQ")){
					if (Address_type.equalsIgnoreCase("Mailing")){

					}
					else{
						if(Address_type.equalsIgnoreCase("RESIDENCE")){
							Address_type="Residence";
						}
						else if(Address_type.equalsIgnoreCase("Home")){
							Address_type="Additional";
						}		

						String UseExistingAddress = formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true")?"-1":"0";
						if(preferrd.equalsIgnoreCase("Y")){
							add_xml_str = add_xml_str + "<AddressDetails><AddrType>Statement</AddrType><UseExistingAddress>"+UseExistingAddress+"</UseExistingAddress><AddressLocation>STMT</AddressLocation><AddressReferenceLink>0</AddressReferenceLink><IsPreferedAddr>"+preferrd+"</IsPreferedAddr>";
						}
						else{
							add_xml_str = add_xml_str + "<AddressDetails><AddrType>"+Address_type+"</AddrType><UseExistingAddress>"+UseExistingAddress+"</UseExistingAddress><AddressLocation>OTHER</AddressLocation><AddressReferenceLink>0</AddressReferenceLink><IsPreferedAddr>"+preferrd+"</IsPreferedAddr>";
						}
						add_xml_str = add_xml_str + "<Addr1>"+flat_Villa+"</Addr1>";
						add_xml_str = add_xml_str + "<Addr2>"+Building_name+"</Addr2>";
						add_xml_str = add_xml_str + "<Addr3>"+street_name+"</Addr3>";
						add_xml_str = add_xml_str + "<Addr4>"+Landmard+"</Addr4>";
						add_xml_str = add_xml_str + "<Addr5>"+city+"</Addr5>";
						add_xml_str = add_xml_str + "<PostalCode>"+country+"</PostalCode>";
						add_xml_str = add_xml_str + "<ZipCode>"+Emirates+"</ZipCode>";
						add_xml_str = add_xml_str + "<City>"+city+"</City>";
						//change by saurabh on 28th Jan
						//add_xml_str = add_xml_str + "<StateProv>"+city+"</StateProv>";
						//add_xml_str = add_xml_str + "<County>"+city+"</County>";
						add_xml_str = add_xml_str + "<Country>UAE</Country>"+ "</AddressDetails>";
					}
				}
				//10/08/2017 added for the respective call
				else{
					add_xml_str = add_xml_str + "<AddressDetails><AddressType>"+Address_type+"</AddressType>";
					add_xml_str = add_xml_str + "<AddressLine1>"+flat_Villa+"</AddressLine1>";
					add_xml_str = add_xml_str + "<AddressLine2>"+Building_name+"</AddressLine2>";
					add_xml_str = add_xml_str + "<AddressLine3>"+street_name+"</AddressLine3>";
					add_xml_str = add_xml_str + "<City>"+city+"</City>";
					add_xml_str = add_xml_str + "<State>"+Emirates+"</State>";
					add_xml_str = add_xml_str + "<Country>"+country+"</Country>";
					add_xml_str = add_xml_str + "<POBox>"+Po_Box+"</POBox></AddressDetails>";
				}
			}
			CreditCard.mLogger.info("RLOSCommon"+ " Address tag Cration: "+ add_xml_str);
			return add_xml_str;
		}
		catch(Exception e){
			CreditCard.mLogger.info("PLCommon getCustAddress_details method"+ " Exception Occure in generate Address XMl"+e.getMessage()+new CC_Common().printException(e));
			return add_xml_str;
		}
	}
	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : Internal Bounced cheques       

	 ***********************************************************************************  */
	public String InternalBouncedCheques() {
		CreditCard.mLogger.info("RLOSCommon java file"+"inside InternalBouncedCheques : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "select CIFID,AcctId,returntype,returnNumber,returnAmount,retReasonCode,returnDate from ng_rlos_FinancialSummary_ReturnsDtls with (nolock) where Child_Wi = '"+ formObject.getWFWorkitemName() + "'";
		CreditCard.mLogger.info("InternalBouncedCheques sQuery" + sQuery+ "");
		String add_xml_str = "";
		List<List<String>> OutputXML = formObject.getNGDataFromDataCache(sQuery);
		CreditCard.mLogger.info("InternalBouncedCheques list size"+ OutputXML.size()+ "");

		for (int i = 0; i < OutputXML.size(); i++) {

			String applicantID = "";
			String chequeNo = "";
			String internal_bounced_cheques_id = "";
			String bouncedCheq = "";
			String amount = "";
			String reason = "";
			String returnDate = "";

			if (!(OutputXML.get(i).get(0) == null || OutputXML.get(i).get(0)
					.equals(""))) {
				applicantID = OutputXML.get(i).get(0).toString();
			}
			if (!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1)
					.equals(""))) {
				internal_bounced_cheques_id = OutputXML.get(i).get(1).toString();
			}
			if (!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2)
					.equals(""))) {
				bouncedCheq = OutputXML.get(i).get(2).toString();
			}
			if (!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3)
					.equals(""))) {
				chequeNo = OutputXML.get(i).get(3).toString();
			}
			if (!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4)
					.equals(""))) {
				amount = OutputXML.get(i).get(4).toString();
			}
			if (!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5)
					.equals(""))) {
				reason = OutputXML.get(i).get(5).toString();
			}
			if (!(OutputXML.get(i).get(6) == null || OutputXML.get(i).get(6)
					.equals(""))) {
				returnDate = OutputXML.get(i).get(6).toString();
			}

			if (applicantID != null && !"".equalsIgnoreCase(applicantID) && !"null".equalsIgnoreCase(applicantID)) 
			{
				add_xml_str = add_xml_str + "<InternalBouncedCheques><applicant_id>" + applicantID + "</applicant_id>";
				add_xml_str = add_xml_str + "<internal_bounced_cheques_id>" + internal_bounced_cheques_id + "</internal_bounced_cheques_id>";
				if ("ICCS".equalsIgnoreCase(bouncedCheq)) {
					add_xml_str = add_xml_str + "<bounced_cheque>" + "1"+ "</bounced_cheque>";
				}
				add_xml_str = add_xml_str + "<cheque_no>" + chequeNo + "</cheque_no>";
				add_xml_str = add_xml_str + "<amount>" + amount + "</amount>";
				add_xml_str = add_xml_str + "<reason>" + reason + "</reason>";
				add_xml_str = add_xml_str + "<return_date>" + returnDate + "</return_date>";
				add_xml_str = add_xml_str + "<provider_no>" + "RAKBANK" + "</provider_no>";
				if ("DDS".equalsIgnoreCase(bouncedCheq)) {
					add_xml_str = add_xml_str + "<bounced_cheque_dds>" + "1"+ "</bounced_cheque_dds>";
				}
				add_xml_str = add_xml_str + "<company_flag>N</company_flag></InternalBouncedCheques>";
			}

		}
		CreditCard.mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "
				+ add_xml_str);
		return add_xml_str;
	}
	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : Internal Bureau pipeline Products       

	 ***********************************************************************************  */
	public String InternalBureauPipelineProducts() {
		CreditCard.mLogger.info("RLOSCommon java file"+"inside InternalBureauPipelineProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "SELECT cifid,product_type,custroletype,lastupdatedate,totalamount,totalnoofinstalments,totalloanamount,agreementId,NoOfDaysInPipeline FROM ng_RLOS_CUSTEXPOSE_LoanDetails  with (nolock) where Child_wi = '"
			+ formObject.getWFWorkitemName()
			+ "' and  LoanStat = 'Pipeline'";
		CreditCard.mLogger.info("InternalBureauPipelineProducts sQuery" + sQuery+"");
		String add_xml_str = "";
		List<List<String>> OutputXML = formObject.getNGDataFromDataCache(sQuery);
		CreditCard.mLogger.info("InternalBureauPipelineProducts list size"+ OutputXML.size()+ "");

		for (int i = 0; i < OutputXML.size(); i++) {

			String cifId = "";
			String Product = "";
			String lastUpdateDate = "";
			String TotAmount = "";
			String TotNoOfInstlmnt = "";
			String TotLoanAmt = "";
			String agreementId = "";
			String NoOfDaysInPipeline="";
			if (!(OutputXML.get(i).get(0) == null || OutputXML.get(i).get(0).equals(""))) {
				cifId = OutputXML.get(i).get(0).toString();
			}
			if (!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals(""))) {
				Product = OutputXML.get(i).get(1).toString();
			}
			if (!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2).equals(""))) {
			}
			if (!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals(""))) {
				lastUpdateDate = OutputXML.get(i).get(3).toString();
			}
			if (!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals(""))) {
				TotAmount = OutputXML.get(i).get(4).toString();
			}
			if (!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5).equals(""))) {
				TotNoOfInstlmnt = OutputXML.get(i).get(5).toString();
			}
			if (!(OutputXML.get(i).get(6) == null || OutputXML.get(i).get(6).equals(""))) {
				TotLoanAmt = OutputXML.get(i).get(6).toString();
			}
			if (!(OutputXML.get(i).get(7) == null || OutputXML.get(i).get(7).equals(""))) {
				agreementId = OutputXML.get(i).get(7).toString();
			}
			if(!(OutputXML.get(i).get(8) == null || "".equalsIgnoreCase(OutputXML.get(i).get(8))) ){
				NoOfDaysInPipeline = OutputXML.get(i).get(8);
			}
			if (cifId != null && !"".equalsIgnoreCase(cifId)&& !"null".equalsIgnoreCase(cifId)) 
			{
				add_xml_str = add_xml_str + "<InternalBureauPipelineProducts>";
				add_xml_str = add_xml_str + "<applicant_id>" + cifId + "</applicant_id>";
				add_xml_str = add_xml_str + "<internal_bureau_pipeline_products_id>" + agreementId + "</internal_bureau_pipeline_products_id>";// to be
				// populated
				// later
				add_xml_str = add_xml_str + "<ppl_provider_no>RAKBANK</ppl_provider_no>";
				add_xml_str = add_xml_str + "<ppl_product>" + Product + "</ppl_product>";
				add_xml_str = add_xml_str + "<ppl_type_of_contract>" + "" + "</ppl_type_of_contract>";
				add_xml_str = add_xml_str + "<ppl_phase>PIPELINE</ppl_phase>"; // to
				// be
				// populated
				// later

				add_xml_str = add_xml_str + "<ppl_role>Primary</ppl_role>";
				add_xml_str = add_xml_str + "<ppl_date_of_last_update>" + lastUpdateDate + "</ppl_date_of_last_update>";
				add_xml_str = add_xml_str + "<ppl_total_amount>" + TotAmount + "</ppl_total_amount>";
				add_xml_str = add_xml_str + "<ppl_no_of_instalments>" + TotNoOfInstlmnt + "</ppl_no_of_instalments>";
				add_xml_str = add_xml_str + "<ppl_credit_limit>" + TotLoanAmt + "</ppl_credit_limit>";

				add_xml_str = add_xml_str + "<ppl_no_of_days_in_pipeline>"+NoOfDaysInPipeline+"</ppl_no_of_days_in_pipeline><company_flag>N</company_flag></InternalBureauPipelineProducts>"; // to be populated later
				}

		}
		CreditCard.mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "
				+ add_xml_str);
		return add_xml_str;
	}
	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : external bureau data      

	 ***********************************************************************************  */
	public String ExternalBureauData() 
	{
		CreditCard.mLogger.info("RLOSCommon java file"+"inside ExternalBureauData : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "select  CifId,fullnm,TotalOutstanding,TotalOverdue,NoOfContracts,Total_Exposure,WorstCurrentPaymentDelay,Worst_PaymentDelay_Last24Months,Worst_Status_Last24Months,Nof_Records,NoOf_Cheque_Return_Last3,Nof_DDES_Return_Last3Months,Nof_Cheque_Return_Last6,DPD30_Last6Months,Internal_WriteOff_Check from NG_rlos_custexpose_Derived with (nolock) where Child_wi  = '"
			+ formObject.getWFWorkitemName()
			+ "' and Request_type= 'ExternalExposure'";
		CreditCard.mLogger.info("ExternalBureauData sQuery" + sQuery+ "");
		String AecbHistQuery = "select isnull(max(AECBHistMonthCnt),0) as AECBHistMonthCnt from ( select MAX(AECBHistMonthCnt) as AECBHistMonthCnt  from ng_rlos_cust_extexpo_CardDetails with (nolock) where  Child_wi  = '"
			+ formObject.getWFWorkitemName()
			+ "' union select Max(AECBHistMonthCnt) from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Child_wi  = '"
			+ formObject.getWFWorkitemName() + "') as ext_expo";
		String add_xml_str = "";
		try {
			List<List<String>> OutputXML = formObject
			.getNGDataFromDataCache(sQuery);
			CreditCard.mLogger.info("ExternalBureauData list size" + OutputXML.size()+ "");
			List<List<String>> AecbHistQueryData = formObject
			.getNGDataFromDataCache(AecbHistQuery);

			if ("0".equalsIgnoreCase(AecbHistQueryData.get(0).get(0))) 
			{
				String CifId = formObject.getNGValue("cmplx_Customer_CIFNO");
				String fullnm = formObject.getNGValue("cmplx_Customer_FIrstNAme") + " " + formObject.getNGValue("cmplx_Customer_LAstNAme");
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
				add_xml_str = add_xml_str + "<ExternalBureau>";
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

				add_xml_str = add_xml_str + "<no_months_aecb_history >" + AecbHistQueryData.get(0).get(0) + "</no_months_aecb_history >";

				add_xml_str = add_xml_str + "<company_flag>N</company_flag></ExternalBureau>";

				CreditCard.mLogger.info("RLOSCommon"+"Internal liab tag Cration: " + add_xml_str);
				return add_xml_str;
			} else {
				for (int i = 0; i < OutputXML.size(); i++) {

					String CifId = formObject	.getNGValue("cmplx_Customer_CIFNO");
					String fullnm = "";
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
					if (!(OutputXML.get(i).get(1) == null || OutputXML.get(i)	.get(1).equals(""))) {fullnm = OutputXML.get(i).get(1).toString();
					}
					if (!(OutputXML.get(i).get(2) == null || OutputXML.get(i)	.get(2).equals(""))) {TotalOutstanding = OutputXML.get(i).get(2).toString();

					}
					if (!(OutputXML.get(i).get(3) == null || OutputXML.get(i)	.get(3).equals(""))) {TotalOverdue = OutputXML.get(i).get(3).toString();
					}
					if (!(OutputXML.get(i).get(4) == null || OutputXML.get(i)	.get(4).equals(""))) {NoOfContracts = OutputXML.get(i).get(4).toString();
					}
					if (!(OutputXML.get(i).get(5) == null || OutputXML.get(i)	.get(5).equals(""))) {Total_Exposure = OutputXML.get(i).get(5).toString();
					}
					if (OutputXML.get(i).get(6) != null	&& !OutputXML.get(i).get(6).isEmpty()	&& !OutputXML.get(i).get(6).equals("")	&& !OutputXML.get(i).get(6).equalsIgnoreCase("null")) {WorstCurrentPaymentDelay = OutputXML.get(i).get(6).toString();
					}
					if (OutputXML.get(i).get(7) != null	&& !OutputXML.get(i).get(7).isEmpty()	&& !OutputXML.get(i).get(7).equals("")	&& !OutputXML.get(i).get(7).equalsIgnoreCase("null")) {Worst_PaymentDelay_Last24Months = OutputXML.get(i).get(		7).toString();
					}
					if (OutputXML.get(i).get(8) != null	&& !OutputXML.get(i).get(8).isEmpty()	&& !OutputXML.get(i).get(8).equals("")	&& !OutputXML.get(i).get(8).equalsIgnoreCase("null")) {Worst_Status_Last24Months = OutputXML.get(i).get(8).toString();
					}
					if (!(OutputXML.get(i).get(9) == null || OutputXML.get(i)	.get(9).equals(""))) {Nof_Records = OutputXML.get(i).get(9).toString();
					}
					if (!(OutputXML.get(i).get(10) == null || OutputXML.get(i)	.get(10).equals(""))) {NoOf_Cheque_Return_Last3 = OutputXML.get(i).get(10).toString();
					}
					if (!(OutputXML.get(i).get(11) == null || OutputXML.get(i)	.get(11).equals(""))) {Nof_DDES_Return_Last3Months = OutputXML.get(i).get(11).toString();
					}
					if (OutputXML.get(i).get(12) != null	&& !OutputXML.get(i).get(12).isEmpty()	&& !OutputXML.get(i).get(12).equals("")	&& !OutputXML.get(i).get(12).equalsIgnoreCase("null")) {// SKLogger.writeLog("Inside for",// "asdasdasd"+OutputXML.get(i).get(12));Nof_Cheque_Return_Last6 = OutputXML.get(i).get(12).toString();
					}
					if (!(OutputXML.get(i).get(13) == null || OutputXML.get(i)	.get(13).equals(""))) {DPD30_Last6Months = OutputXML.get(i).get(13).toString();
					}

					add_xml_str = add_xml_str + "<ExternalBureau>";
					add_xml_str = add_xml_str + "<applicant_id>" + CifId+ "</applicant_id>";

					add_xml_str = add_xml_str + "<full_name>" + fullnm+ "</full_name>";
					add_xml_str = add_xml_str + "<total_out_bal>"+ TotalOutstanding + "</total_out_bal>";

					add_xml_str = add_xml_str + "<total_overdue>"+ TotalOverdue + "</total_overdue>";
					add_xml_str = add_xml_str + "<no_default_contract>"+ NoOfContracts + "</no_default_contract>";
					add_xml_str = add_xml_str + "<total_exposure>"+ Total_Exposure + "</total_exposure>";
					add_xml_str = add_xml_str + "<worst_curr_pay>"+ WorstCurrentPaymentDelay + "</worst_curr_pay>";
					add_xml_str = add_xml_str + "<worst_curr_pay_24>"+ Worst_PaymentDelay_Last24Months+ "</worst_curr_pay_24>";
					add_xml_str = add_xml_str + "<worst_status_24>"+ Worst_Status_Last24Months + "</worst_status_24>";

					add_xml_str = add_xml_str + "<no_of_rec>" + Nof_Records+ "</no_of_rec>";
					add_xml_str = add_xml_str + "<cheque_return_3mon>"+ NoOf_Cheque_Return_Last3+ "</cheque_return_3mon>";
					add_xml_str = add_xml_str + "<dds_return_3mon>"+ Nof_DDES_Return_Last3Months+ "</dds_return_3mon>";
					add_xml_str = add_xml_str + "<cheque_return_6mon>"+ Nof_Cheque_Return_Last6 + "</cheque_return_6mon>";
					add_xml_str = add_xml_str + "<dds_return_6mon>"+ DPD30_Last6Months + "</dds_return_6mon>";
					add_xml_str = add_xml_str+ "<prod_external_writeoff_amount>" + ""+ "</prod_external_writeoff_amount>";

					add_xml_str = add_xml_str + "<no_months_aecb_history >"+ AecbHistQueryData.get(0).get(0)+ "</no_months_aecb_history >";

					add_xml_str = add_xml_str+ "<company_flag>N</company_flag></ExternalBureau>";

				}
				CreditCard.mLogger.info("RLOSCommon"+"Internal liab tag Cration: " + add_xml_str);
				return add_xml_str;
			}

		}

		catch (Exception e) {
			CreditCard.mLogger.info("RLOSCommon"+
					"Exception occurred in externalBureauData()"+ e.getMessage() + "\n Error: "+ CC_Common.printException(e));
			return null;
		}
	}
	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : External bounced cheques      

	 ***********************************************************************************  */
	public String ExternalBouncedCheques() {
		CreditCard.mLogger.info("RLOSCommon java file"+"inside ExternalBouncedCheques : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "SELECT cifid,number,amount,reasoncode,returndate,providerno FROM ng_rlos_cust_extexpo_ChequeDetails  with (nolock) where child_wi = '"
			+ formObject.getWFWorkitemName()
			+ "' and Request_Type = 'ExternalExposure'";
		CreditCard.mLogger.info("ExternalBouncedCheques sQuery" + sQuery+ "");
		String add_xml_str = "";
		List<List<String>> OutputXML = formObject.getNGDataFromDataCache(sQuery);
		CreditCard.mLogger.info("ExternalBouncedCheques list size"+ OutputXML.size()+ "");

		for (int i = 0; i < OutputXML.size(); i++) {

			String CifId = formObject.getNGValue("cmplx_Customer_CIFNO");
			String chqNo = "";
			String Amount = "";
			String Reason = "";
			String returnDate = "";
			String providerNo = "";

			if (!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1)
					.equals(""))) {
				chqNo = OutputXML.get(i).get(1).toString();
			}
			if (!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2)
					.equals(""))) {
				Amount = OutputXML.get(i).get(2).toString();
			}
			if (!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3)
					.equals(""))) {
				Reason = OutputXML.get(i).get(3).toString();
			}
			if (!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4)
					.equals(""))) {
				returnDate = OutputXML.get(i).get(4).toString();
			}
			if (!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5)
					.equals(""))) {
				providerNo = OutputXML.get(i).get(5).toString();
			}

			add_xml_str = add_xml_str + "<ExternalBouncedCheques><applicant_id>" + CifId + "</applicant_id>";
			add_xml_str = add_xml_str + "<external_bounced_cheques_id>" + "" + "</external_bounced_cheques_id>";
			add_xml_str = add_xml_str + "<bounced_cheque>" + "" + "</bounced_cheque>";
			add_xml_str = add_xml_str + "<cheque_no>" + chqNo + "</cheque_no>";
			add_xml_str = add_xml_str + "<amount>" + Amount + "</amount>";
			add_xml_str = add_xml_str + "<reason>" + Reason + "</reason>";
			add_xml_str = add_xml_str + "<return_date>" + returnDate + "</return_date>"; // to be populated later
			add_xml_str = add_xml_str + "<provider_no>" + providerNo + "</provider_no><company_flag>N</company_flag></ExternalBouncedCheques>"; // to
			// be
			// populated
			// later

		}
		CreditCard.mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "
				+ add_xml_str);
		return add_xml_str;
	}
	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : External Bureau Individual Products       

	 ***********************************************************************************  */
	public String ExternalBureauIndividualProducts() {
		CreditCard.mLogger.info("RLOSCommon java file"+"inside ExternalBureauIndividualProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "select CifId,AgreementId,LoanType,ProviderNo,LoanStat,CustRoleType,LoanApprovedDate,LoanMaturityDate,OutstandingAmt,TotalAmt,PaymentsAmt,TotalNoOfInstalments,RemainingInstalments,WriteoffStat,WriteoffStatDt,CreditLimit,OverdueAmt,NofDaysPmtDelay,MonthsOnBook,lastrepmtdt,IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last18Months,AECBHistMonthCnt,DPD5_Last3Months,'' as qc_Amnt,'' as Qc_emi,'' as Cac_indicator,Take_Over_Indicator,Consider_For_Obligations,case when IsDuplicate= '1' then 'Y' else 'N' end from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Child_wi= '"
			+ formObject.getWFWorkitemName()
			+ "'  and LoanStat != 'Pipeline' union select CifId,CardEmbossNum,CardType,ProviderNo,CardStatus,CustRoleType,StartDate,ClosedDate,CurrentBalance,'' as col6,PaymentsAmount,NoOfInstallments,'' as col5,WriteoffStat,WriteoffStatDt,CashLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,lastrepmtdt,IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last18Months,AECBHistMonthCnt,DPD5_Last3Months,qc_amt,qc_emi,CAC_Indicator,Take_Over_Indicator,Consider_For_Obligations,case when IsDuplicate= '1' then 'Y' else 'N' end from ng_rlos_cust_extexpo_CardDetails with (nolock) where Child_wi  =  '"
			+ formObject.getWFWorkitemName()
			+ "' and cardstatus != 'Pipeline' union select CifId,AcctId,AcctType,AcctId,AcctStat,CustRoleType,StartDate,ClosedDate,OutStandingBalance,TotalAmount,PaymentsAmount,'','',WriteoffStat,WriteoffStatDt,CreditLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,'',IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last18Months,AECBHistMonthCnt,DPD5_Last3Months,'','','','',isnull(Consider_For_Obligations,'true'),case when IsDuplicate= '1' then 'Y' else 'N' end from ng_rlos_cust_extexpo_AccountDetails where AcctType='Overdraft' and child_wi  =  '"+formObject.getWFWorkitemName()+"' ";
		CreditCard.mLogger.info("ExternalBureauIndividualProducts sQuery" + sQuery+ "");
		String add_xml_str = "";
		List<List<String>> OutputXML = formObject.getNGDataFromDataCache(sQuery);
		CreditCard.mLogger.info("ExternalBureauIndividualProducts list size"+ OutputXML.size()+ "");
		String ReqProd = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 1);
		for (int i = 0; i < OutputXML.size(); i++) {

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
			String consider_for_obligation = "";
			String Duplicate_flag="";
			if (!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1)
					.equals(""))) {
				AgreementId = OutputXML.get(i).get(1).toString();
			}
			if (!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2).equals(""))) {
				ContractType = OutputXML.get(i).get(2).toString();
				try {
					String cardquery = "select code from ng_master_contract_type with (nolock) where description='"+ ContractType + "'";
					CreditCard.mLogger.info("ExternalBureauIndividualProducts sQuery"+ cardquery+ "");
					List<List<String>> cardqueryXML = formObject.getNGDataFromDataCache(cardquery);
					ContractType = cardqueryXML.get(0).get(0);
					CreditCard.mLogger.info("ExternalBureauIndividualProducts ContractType"+ ContractType+ "ContractType");
				} catch (Exception e) {
					CreditCard.mLogger.info("ExternalBureauIndividualProducts ContractType Exception"+ e+ "Exception");

					ContractType = OutputXML.get(i).get(2).toString();
				}
			}
			if (!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3)
					.equals(""))) {
				provider_no = OutputXML.get(i).get(3).toString();
			}
			if (!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4)
					.equals(""))) {
				phase = OutputXML.get(i).get(4).toString();
				if (phase.startsWith("A")) {
					phase = "A";
				} else {
					phase = "C";
				}
			}
			if (!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5)
					.equals(""))) {
				CustRoleType = OutputXML.get(i).get(5).toString();
				String sQueryCustRoleType = "select code from ng_master_role_of_customer where Description='"+CustRoleType+"'";
				CreditCard.mLogger.info("CustRoleType"+sQueryCustRoleType);
				List<List<String>> sQueryCustRoleTypeXML = formObject.getNGDataFromDataCache(sQueryCustRoleType);
				try{
					if(sQueryCustRoleTypeXML!=null && sQueryCustRoleTypeXML.size()>0 && sQueryCustRoleTypeXML.get(0)!=null){
						CustRoleType=sQueryCustRoleTypeXML.get(0).get(0);
					}
				}
				catch(Exception e){
					CreditCard.mLogger.info("Exception occured at sQueryCombinedLimit for"+sQueryCustRoleTypeXML);

				}	}
			if (!(OutputXML.get(i).get(6) == null || OutputXML.get(i).get(6)
					.equals(""))) {
				start_date = OutputXML.get(i).get(6).toString();
			}
			if (!(OutputXML.get(i).get(7) == null || OutputXML.get(i).get(7)
					.equals(""))) {
				close_date = OutputXML.get(i).get(7).toString();
			}
			if (!(OutputXML.get(i).get(8) == null || OutputXML.get(i).get(8)
					.equals(""))) {
				OutStanding_Balance = OutputXML.get(i).get(8).toString();
			}
			if (!(OutputXML.get(i).get(9) == null || OutputXML.get(i).get(9)
					.equals(""))) {
				TotalAmt = OutputXML.get(i).get(9).toString();
			}
			if (!(OutputXML.get(i).get(10) == null || OutputXML.get(i).get(10)
					.equals(""))) {
				PaymentsAmt = OutputXML.get(i).get(10).toString();
			}
			if (!(OutputXML.get(i).get(11) == null || OutputXML.get(i).get(11)
					.equals(""))) {
				TotalNoOfInstalments = OutputXML.get(i).get(11).toString();
			}
			if (!(OutputXML.get(i).get(12) == null || OutputXML.get(i).get(12)
					.equals(""))) {
				RemainingInstalments = OutputXML.get(i).get(12).toString();
			}
			if (!(OutputXML.get(i).get(13) == null || OutputXML.get(i).get(13)
					.equals(""))) {
				WorstStatus = OutputXML.get(i).get(13).toString();
			}
			if (!(OutputXML.get(i).get(14) == null || OutputXML.get(i).get(14)
					.equals(""))) {
				WorstStatusDate = OutputXML.get(i).get(14).toString();
			}
			if (!(OutputXML.get(i).get(15) == null || OutputXML.get(i).get(15)
					.equals(""))) {
				CreditLimit = OutputXML.get(i).get(15).toString();
			}
			if (!(OutputXML.get(i).get(16) == null || OutputXML.get(i).get(16)
					.equals(""))) {
				OverdueAmt = OutputXML.get(i).get(16).toString();
			}
			if (!(OutputXML.get(i).get(17) == null || OutputXML.get(i).get(17)
					.equals(""))) {
				NofDaysPmtDelay = OutputXML.get(i).get(17).toString();
			}
			if (!(OutputXML.get(i).get(18) == null || OutputXML.get(i).get(18)
					.equals(""))) {
				MonthsOnBook = OutputXML.get(i).get(18).toString();
			}
			if (!(OutputXML.get(i).get(19) == null || OutputXML.get(i).get(19)
					.equals(""))) {
				last_repayment_date = OutputXML.get(i).get(19).toString();
			}
			if (!(OutputXML.get(i).get(20) == null || OutputXML.get(i).get(20)
					.equals(""))) {
				currently_current = OutputXML.get(i).get(20).toString();
			}
			if (!(OutputXML.get(i).get(21) == null || OutputXML.get(i).get(21)
					.equals(""))) {
				current_utilization = OutputXML.get(i).get(21).toString();
			}
			if (!(OutputXML.get(i).get(22) == null || OutputXML.get(i).get(22)
					.equals(""))) {
				DPD30Last6Months = OutputXML.get(i).get(22).toString();
			}
			if (!(OutputXML.get(i).get(23) == null || OutputXML.get(i).get(23)
					.equals(""))) {
				DPD60Last18Months = OutputXML.get(i).get(23).toString();
			}
			if (!(OutputXML.get(i).get(24) == null || OutputXML.get(i).get(24)
					.equals(""))) {
				AECBHistMonthCnt = OutputXML.get(i).get(24).toString();
			}

			if (!(OutputXML.get(i).get(25) == null || OutputXML.get(i).get(25)
					.equals(""))) {
				delinquent_in_last_3months = OutputXML.get(i).get(25).toString();
			}
			if (!(OutputXML.get(i).get(26) == null || OutputXML.get(i).get(26)
					.equals(""))) {
				QC_Amt = OutputXML.get(i).get(26).toString();
			}
			if (!(OutputXML.get(i).get(27) == null || OutputXML.get(i).get(27)
					.equals(""))) {
				QC_emi = OutputXML.get(i).get(27).toString();
			}
			if (!(OutputXML.get(i).get(28) == null || OutputXML.get(i).get(28)
					.equals(""))) {
				CAC_Indicator = OutputXML.get(i).get(28).toString();
				if (CAC_Indicator != null && !("".equalsIgnoreCase(CAC_Indicator))) {
					if ("true".equalsIgnoreCase(CAC_Indicator)) 
					{
						CAC_Indicator = "Y";
					} 
					else 
					{
						CAC_Indicator = "N";
					}
				}
			}
			
			String TakeOverIndicator = "";
			if (!(OutputXML.get(i).get(29) == null || "".equals(OutputXML.get(i).get(29)))) {
				TakeOverIndicator = OutputXML.get(i).get(29).toString();
				if (TakeOverIndicator != null && !("".equalsIgnoreCase(TakeOverIndicator))) 
				{
					if ("true".equalsIgnoreCase(TakeOverIndicator)) 
					{
						TakeOverIndicator = "Y";
					} 
					else 
					{
						TakeOverIndicator = "N";
					}
				}
			}
			if (!(OutputXML.get(i).get(30) == null || "".equals(OutputXML.get(i).get(30))))
			{
				consider_for_obligation = OutputXML.get(i).get(30).toString();
				if (consider_for_obligation != null && !("".equalsIgnoreCase(consider_for_obligation))) {
					if ("true".equalsIgnoreCase(consider_for_obligation)) 
					{
						consider_for_obligation = "Y";
					} 
					else 
					{
						consider_for_obligation = "N";
					}
				}
			}
			if(!(OutputXML.get(i).get(31) == null || OutputXML.get(i).get(31).equals("")) ){
				Duplicate_flag = OutputXML.get(i).get(31).toString();
			}

			add_xml_str = add_xml_str + "<ExternalBureauIndividualProducts><applicant_id>" + CifId + "</applicant_id>";
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
			if (currently_current != null && "1".equalsIgnoreCase(currently_current))
			{
				add_xml_str = add_xml_str + "<currently_current>Y</currently_current>";
			}
			else 
			{
				add_xml_str = add_xml_str + "<currently_current>N</currently_current>";
			}
			add_xml_str = add_xml_str + "<current_utilization>" + current_utilization + "</current_utilization>";
			add_xml_str = add_xml_str + "<dpd_30_last_6_mon>" + DPD30Last6Months + "</dpd_30_last_6_mon>";

			add_xml_str = add_xml_str + "<dpd_60p_in_last_18_mon>" + DPD60Last18Months + "</dpd_60p_in_last_18_mon>";
			add_xml_str = add_xml_str + "<no_months_aecb_history>" + AECBHistMonthCnt + "</no_months_aecb_history>";
			add_xml_str = add_xml_str + "<delinquent_in_last_3months>" + delinquent_in_last_3months + "</delinquent_in_last_3months>";
			add_xml_str = add_xml_str + "<clean_funded>" + "" + "</clean_funded>";
			add_xml_str = add_xml_str + "<cac_indicator>" + CAC_Indicator + "</cac_indicator>";
			add_xml_str = add_xml_str + "<qc_emi>" + QC_emi + "</qc_emi>";
			add_xml_str = add_xml_str + "<qc_amount>" + QC_Amt + "</qc_amount><company_flag>N</company_flag><cac_bank_name>" + formObject.getNGValue("cmplx_EmploymentDetails_tenancycntrctemirate") + "</cac_bank_name><take_over_indicator>" + TakeOverIndicator + "</take_over_indicator><consider_for_obligation>" + consider_for_obligation + "</consider_for_obligation><duplicate_flag>"+Duplicate_flag+"</duplicate_flag></ExternalBureauIndividualProducts>";
			
			
		}
		CreditCard.mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "	+ add_xml_str);
		return add_xml_str;
	}
	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         :External Bureau Manual Add Individual Products       

	 ***********************************************************************************  */
	public String ExternalBureauManualAddIndividualProducts() 
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		CreditCard.mLogger.info("ExternalBureauManualAddIndividualProducts sQuery"+ "");
		int Man_liab_row_count = formObject.getLVWRowCount("cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
		String applicant_id = formObject.getNGValue("cmplx_Customer_CIFNO");
		String add_xml_str = "";
		Date currentDate=new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String modifiedDate= sdf.format(currentDate);
		String close_date= CC_Comn.plusyear(modifiedDate,4,0,0);
		CreditCard.mLogger.info("ExternalBureauIndividualProducts list size"+ Man_liab_row_count+ "");
		if (Man_liab_row_count != 0) {
			for (int i = 0; i < Man_liab_row_count; i++) {
				String Type_of_Contract = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",i, 0); // 0
				String Limit = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",	i, 1); // 0
				String EMI = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",i, 2); // 0
				String Take_over_Indicator = ("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",i, 3)) ? "Y" : "N"); // 0
				String cac_Indicator = ("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",i, 5)) ? "Y" : "N"); // 0
				String Qc_amt = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",i, 6); // 0
				String Qc_Emi = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",i, 7); // 0
				
				String consider_for_obligation = ("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",i, 8)) ? "Y" : "N"); // 0
				// String MOB =
				// formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",
				// i, 9); //0
				String Utilization = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i,11); // 0
				String OutStanding = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i,12); // 0
				String mob = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i,10);
				String delinquent_in_last_3months = ("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i,13)) ? "1" : "0");
				String dpd_30_last_6_mon = ("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i,14)) ? "1" : "0");
				String dpd_60p_in_last_18_mon = ("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i,15)) ? "1" : "0");
				add_xml_str = add_xml_str + "<ExternalBureauIndividualProducts><applicant_id>" + applicant_id + "</applicant_id>";
				add_xml_str = add_xml_str + "<external_bureau_individual_products_id></external_bureau_individual_products_id>";
				add_xml_str = add_xml_str + "<contract_type>" + Type_of_Contract + "</contract_type>";
				add_xml_str = add_xml_str + "<provider_no></provider_no>";
				add_xml_str = add_xml_str + "<phase>A</phase>";
				add_xml_str = add_xml_str + "<role_of_customer>A</role_of_customer>";
				add_xml_str = add_xml_str + "<start_date>" + modifiedDate + "</start_date>";

				add_xml_str = add_xml_str + "<close_date>"+close_date+"</close_date>";
				add_xml_str = add_xml_str + "<outstanding_balance>" + OutStanding + "</outstanding_balance>";
				if (!Type_of_Contract.equalsIgnoreCase("01")){
					add_xml_str = add_xml_str + "<total_amount>"+Limit+"</total_amount>";
					}
					add_xml_str = add_xml_str + "<payments_amount>"+EMI+"</payments_amount>";
					add_xml_str = add_xml_str + "<total_no_of_instalments></total_no_of_instalments>";
					add_xml_str = add_xml_str + "<no_of_remaining_instalments></no_of_remaining_instalments>";
					add_xml_str = add_xml_str + "<worst_status></worst_status>";
					add_xml_str = add_xml_str + "<worst_status_date></worst_status_date>";
					if (Type_of_Contract.equalsIgnoreCase("01")){
					add_xml_str = add_xml_str + "<credit_limit>"+Limit+"</credit_limit>";
					}
				add_xml_str = add_xml_str + "<overdue_amount></overdue_amount>";
				add_xml_str = add_xml_str + "<no_of_days_payment_delay></no_of_days_payment_delay>";
				add_xml_str = add_xml_str + "<mob>" + mob + "</mob>";
				add_xml_str = add_xml_str + "<last_repayment_date></last_repayment_date>";

				add_xml_str = add_xml_str + "<currently_current>N</currently_current>";

				add_xml_str = add_xml_str + "<current_utilization>" + Utilization + "</current_utilization>";
				add_xml_str = add_xml_str + "<dpd_30_last_6_mon>" + dpd_30_last_6_mon + "</dpd_30_last_6_mon>";

				add_xml_str = add_xml_str + "<dpd_60p_in_last_18_mon>" + dpd_60p_in_last_18_mon + "</dpd_60p_in_last_18_mon>";
				add_xml_str = add_xml_str + "<no_months_aecb_history></no_months_aecb_history>";
				add_xml_str = add_xml_str + "<delinquent_in_last_3months>" + delinquent_in_last_3months + "</delinquent_in_last_3months>";
				add_xml_str = add_xml_str + "<clean_funded>" + "" + "</clean_funded>";
				add_xml_str = add_xml_str + "<cac_indicator>" + cac_Indicator + "</cac_indicator>";
				add_xml_str = add_xml_str + "<qc_emi>" + Qc_Emi + "</qc_emi>";
				add_xml_str = add_xml_str + "<qc_amount>" + Qc_amt + "</qc_amount><cac_bank_name>" + formObject.getNGValue("cmplx_EmploymentDetails_tenancycntrctemirate") + "</cac_bank_name><take_over_indicator>" + Take_over_Indicator + "</take_over_indicator><company_flag>N</company_flag><consider_for_obligation>" + consider_for_obligation + "</consider_for_obligation><duplicate_flag>N</duplicate_flag></ExternalBureauIndividualProducts>";

			}

		}
		CreditCard.mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "	+ add_xml_str);
		return add_xml_str;
	}
	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : External bureau Pipeline Products       

	 ***********************************************************************************  */
	public String ExternalBureauPipelineProducts() {
		CreditCard.mLogger.info("RLOSCommon java file"+"inside ExternalBureauPipelineProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "select AgreementId,ProviderNo,LoanType,LoanDesc,CustRoleType,Datelastupdated,Total_Amount,TotalNoOfInstalments,'' as col1,NoOfDaysInPipeline,isnull(Consider_For_Obligations,'true'),case when IsDuplicate= '1' then 'Y' else 'N' end from ng_rlos_cust_extexpo_LoanDetails with (nolock) where child_wi  =  '"
			+ formObject.getWFWorkitemName()
			+ "' and LoanStat = 'Pipeline' union select CardEmbossNum,ProviderNo,CardTypeDesc,CardType,CustRoleType,LastUpdateDate,'' as col2,NoOfInstallments,TotalAmount,NoOfDaysInPipeLine,isnull(Consider_For_Obligations,'true'),case when IsDuplicate= '1' then 'Y' else 'N' end  from ng_rlos_cust_extexpo_CardDetails with (nolock) where child_wi  =  '"
			+ formObject.getWFWorkitemName()
			+ "' and cardstatus = 'Pipeline'";
		CreditCard.mLogger.info("ExternalBureauPipelineProducts sQuery" + sQuery+"");
		String add_xml_str = "";
		List<List<String>> OutputXML = formObject.getNGDataFromDataCache(sQuery);
		CreditCard.mLogger.info("ExternalBureauPipelineProducts list size"+ OutputXML.size()+ "");
		String cifId = formObject.getNGValue("cmplx_Customer_CIFNO");

		for (int i = 0; i < OutputXML.size(); i++) {

			String agreementID = "";
			String ProviderNo = "";
			String contractType = "";
			String productType = "";
			String role = "";
			String lastUpdateDate = "";
			String TotAmt = "";
			String noOfInstalmnt = "";
			String creditLimit = "";
			String noOfDayinPpl = "";
			String consider_for_obligation="";
			String ppl_Duplicate_flag="";
			
			if (!(OutputXML.get(i).get(0) == null || OutputXML.get(i).get(0)
					.equals(""))) {
				agreementID = OutputXML.get(i).get(0).toString();
			}
			if (!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1)
					.equals(""))) {
				ProviderNo = OutputXML.get(i).get(1).toString();
			}
			if (OutputXML.get(i).get(2) != null	&& !OutputXML.get(i).get(2).isEmpty() && !"".equals(OutputXML.get(i).get(2)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(2))) 
			{
				contractType = OutputXML.get(i).get(2).toString();
			}
			if (!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3)
					.equals(""))) {
				productType = OutputXML.get(i).get(3).toString();
			}
			if (!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4)
					.equals(""))) {
				role = OutputXML.get(i).get(4).toString();
			}
			if (OutputXML.get(i).get(5) != null	&& !OutputXML.get(i).get(5).isEmpty() && !"".equals(OutputXML.get(i).get(5)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(5))) 
			{
				lastUpdateDate = OutputXML.get(i).get(5).toString();
			}
			if (OutputXML.get(i).get(6) != null	&& !OutputXML.get(i).get(6).isEmpty() && !OutputXML.get(i).get(6).equals("")	&& !"null".equalsIgnoreCase(OutputXML.get(i).get(6))) {
				TotAmt = OutputXML.get(i).get(6).toString();
			}
			if (!(OutputXML.get(i).get(7) == null || OutputXML.get(i).get(7).equals(""))) 
			{
				noOfInstalmnt = OutputXML.get(i).get(7).toString();
			}
			if (!(OutputXML.get(i).get(8) == null || OutputXML.get(i).get(8).equals(""))) 
			{
				creditLimit = OutputXML.get(i).get(8).toString();
			}
			if (OutputXML.get(i).get(9) != null	&& !OutputXML.get(i).get(9).isEmpty() && !OutputXML.get(i).get(9).equals("")&& !"null".equalsIgnoreCase(OutputXML.get(i).get(9))) 
			{
				noOfDayinPpl = OutputXML.get(i).get(9).toString();
			}
			if(!(OutputXML.get(i).get(10) == null || "".equals(OutputXML.get(i).get(10))) ){
				consider_for_obligation = OutputXML.get(i).get(10);
				if (consider_for_obligation != null && !("".equalsIgnoreCase(consider_for_obligation))){
					if ("true".equalsIgnoreCase(consider_for_obligation)){
						consider_for_obligation="Y";
					}
					else {
						consider_for_obligation="N";
					}
				}
			}
			if(OutputXML.get(i).get(11)!=null && !OutputXML.get(i).get(11).isEmpty() &&  !"".equalsIgnoreCase(OutputXML.get(i).get(11)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(11)) ){
				ppl_Duplicate_flag = OutputXML.get(i).get(11);
			}
			add_xml_str = add_xml_str + "<ExternalBureauPipelineProducts><applicant_ID>" + cifId + "</applicant_ID>";
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

			add_xml_str = add_xml_str + "<ppl_no_of_days_in_pipeline>" + noOfDayinPpl + "</ppl_no_of_days_in_pipeline><company_flag>N</company_flag><consider_for_obligation>"+consider_for_obligation+"</consider_for_obligation><ppl_duplicate_flag>"+ppl_Duplicate_flag+"</ppl_duplicate_flag></ExternalBureauPipelineProducts>"; // to
			// be
			// populated
			// later

		}
		CreditCard.mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "	+ add_xml_str);
		return add_xml_str;
	}
	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : get MQ InputXML      

	 ***********************************************************************************  */
	private static String getMQInputXML(String sessionID, String cabinetName,
			String wi_name, String ws_name, String userName,
			StringBuilder final_xml) {
		FormContext.getCurrentInstance().getFormConfig();

		StringBuffer strBuff = new StringBuffer();
		strBuff.append("<APMQPUTGET_Input>");
		strBuff.append("<SessionId>" + sessionID + "</SessionId>");
		strBuff.append("<EngineName>" + cabinetName + "</EngineName>");
		strBuff.append("<XMLHISTORY_TABLENAME>NG_CC_XMLLOG_HISTORY</XMLHISTORY_TABLENAME>");
		strBuff.append("<WI_NAME>" + wi_name + "</WI_NAME>");
		strBuff.append("<WS_NAME>" + ws_name + "</WS_NAME>");
		strBuff.append("<USER_NAME>" + userName + "</USER_NAME>");
		strBuff.append("<MQ_REQUEST_XML>");
		strBuff.append(final_xml);
		strBuff.append("</MQ_REQUEST_XML>");
		strBuff.append("</APMQPUTGET_Input>");
		CreditCard.mLogger.info("inside getOutputXMLValues"+ "getMQInputXML"+ strBuff.toString());
		return strBuff.toString();
	}
	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : DEDUP SUMMARY CUSTOM       

	 ***********************************************************************************  */


	private Map<String, String> DEDUP_SUMMARY_Custom(List<List<String>> DB_List,FormReference formObject,String callName) {

		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();

		try{
			for (List<String> mylist : DB_List) {

				for (int i = 0; i < 8; i++) {

					CreditCard.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");

				if ("AddressDetails".equalsIgnoreCase(tag_name)	&& int_xml.containsKey(parent_tag)) {
					String xml_str = int_xml.get(parent_tag);
					CreditCard.mLogger.info("RLOS COMMON"+ " before adding address+ " + xml_str);
					xml_str = xml_str + getCustAddress_details(callName);
					CreditCard.mLogger.info("RLOS COMMON"+ " after adding address+ " + xml_str);
					int_xml.put(parent_tag, xml_str);
				} else if ("MaritalStatus".equalsIgnoreCase(tag_name)) {
					String marrital_code = formObject.getNGValue("cmplx_Customer_MAritalStatus").substring(0, 1);
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<" + tag_name + ">" + marrital_code + "</" + tag_name + ">";

					CreditCard.mLogger.info("RLOS COMMON"+ " after adding Minor flag+ " + xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
				}
			}
		}
		catch(Exception e){
			CreditCard.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + "+e.getMessage());
		}
		return int_xml;
	}


	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 22/11/2017              
	Author                              : Deepak              
	Description                         : Function for Dedupe Summary call    

	 ***********************************************************************************  */

	private Map<String, String> Non_Custom(List<List<String>> DB_List,FormReference formObject,String callName) {


		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();

		//FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try{
			for (List<String> mylist : DB_List) {
				// for(int i=0;i<col_n.length();i++)
				for (int i = 0; i < 8; i++) {
					// CreditCard.mLogger.info("rec: "+records.item(rec));
					CreditCard.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);

			}
		}
		catch(Exception e){
			CreditCard.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + "+e.getMessage());

		}

		return int_xml;
	}


	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : Blacklist details Custom       

	 ***********************************************************************************  */
	private Map<String, String> Blacklist_Details_custom(List<List<String>> DB_List,FormReference formObject,String callName) {

		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();

		try{
			for (List<String> mylist : DB_List) {

				for (int i = 0; i < 8; i++) {

					CreditCard.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");


				if ("AddressDetails".equalsIgnoreCase(tag_name)	&& int_xml.containsKey(parent_tag)) {
					String xml_str = int_xml.get(parent_tag);
					CreditCard.mLogger.info("RLOS COMMON"+ " before adding address+ " + xml_str);
					xml_str = xml_str + getCustAddress_details(callName);
					CreditCard.mLogger.info("RLOS COMMON"+ " after adding address+ " + xml_str);
					int_xml.put(parent_tag, xml_str);
				} else if ("MaritalStatus".equalsIgnoreCase(tag_name)) 
				{
					String marrital_code = formObject.getNGValue("cmplx_Customer_MAritalStatus").substring(0, 1);
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<" + tag_name + ">" + marrital_code + "</" + tag_name + ">";

					CreditCard.mLogger.info("RLOS COMMON"+ " after adding Minor flag+ " + xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
				}
			}
		}
		catch(Exception e){
			CreditCard.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + "+e.getMessage());
		}
		return int_xml;
	}
	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : new customer custom       

	 ***********************************************************************************  */
	private Map<String, String> NEW_CUSTOMER_Custom(List<List<String>> DB_List,FormReference formObject,String callName) {

		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();

		try{
			for (List<String> mylist : DB_List) {

				for (int i = 0; i < 8; i++) {

					CreditCard.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");
				if ("AddressDetails".equalsIgnoreCase(tag_name)	&& int_xml.containsKey(parent_tag)) {
					String xml_str = int_xml.get(parent_tag);
					CreditCard.mLogger.info("RLOS COMMON"+ " before adding address+ " + xml_str);
					xml_str = xml_str + getCustAddress_details(callName);
					CreditCard.mLogger.info("RLOS COMMON"+ " after adding address+ " + xml_str);
					int_xml.put(parent_tag, xml_str);
				} else if ("MinorFlag".equalsIgnoreCase(tag_name)&& "PersonDetails".equalsIgnoreCase(parent_tag)) {
					if (int_xml.containsKey(parent_tag)) {
						float Age = Float.parseFloat(formObject.getNGValue("cmplx_Customer_age"));
						String age_flag = "N";
						if (Age < 18)
							age_flag = "Y";
						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<" + tag_name + ">" + age_flag + "</" + tag_name + ">";

						CreditCard.mLogger.info("RLOS COMMON"+" after adding Minor flag+ " + xml_str);
						int_xml.put(parent_tag, xml_str);
					}
				} else if ("NonResidentFlag".equalsIgnoreCase(tag_name)	&& "PersonDetails".equalsIgnoreCase(parent_tag)) 
				{
					if (int_xml.containsKey(parent_tag)) {
						String xml_str = int_xml.get(parent_tag);
						String res_flag = "N";

						if ("Resident".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_ResidentNonResident"))) {
							res_flag = "Y";
						}

						xml_str = xml_str + "<" + tag_name + ">" + res_flag + "</" + tag_name + ">";

						CreditCard.mLogger.info("RLOS COMMON"+ " after adding res_flag+ " + xml_str);
						int_xml.put(parent_tag, xml_str);
					}
				}
				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
				}
			}
		}
		catch(Exception e){
			CreditCard.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + "+e.getMessage());
		}
		return int_xml;
	}
	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : Customer Update Custom       

	 ***********************************************************************************  */
	private Map<String, String> CUSTOMER_UPDATE_Custom(List<List<String>> DB_List,FormReference formObject,String callName) {

		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();

		try{
			for (List<String> mylist : DB_List) {

				for (int i = 0; i < 8; i++) {

					CreditCard.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");

				String Call_name = (String) recordFileMap.get("Call_name");
				String form_control = (String) recordFileMap.get("form_control");


				if ("OECDDet".equalsIgnoreCase(tag_name) && int_xml.containsKey(parent_tag)) {
					CreditCard.mLogger.info("inside 1st if"+ "inside customer update req2123");
					String xml_str = int_xml.get(parent_tag);
					CreditCard.mLogger.info("RLOS COMMON"+ " before adding OECD+ " + xml_str);
					xml_str = xml_str + CC_Comn.getCustOECD_details(Call_name);
					CreditCard.mLogger.info("RLOS COMMON"+ " after adding OeCD+ " + xml_str);
					int_xml.put(parent_tag, xml_str);
				} else if ("PhnLocalCode".equalsIgnoreCase(tag_name)) {
					CreditCard.mLogger.info("inside PL Common generate xml"+"PhnLocalCode to substring");
					String xml_str = int_xml.get(parent_tag);
					String phn_no = formObject.getNGValue(form_control);
					if ((!"".equalsIgnoreCase(phn_no)) && phn_no.indexOf("00971") > -1) 
					{
						phn_no = phn_no.substring(5);
					}

					xml_str = xml_str + "<" + tag_name + ">" + phn_no + "</" + tag_name + ">";

					CreditCard.mLogger.info("PL COMMON"+ " after adding ApplicationID:  " + xml_str);
					int_xml.put(parent_tag, xml_str);
				} else if ("AddrDet".equalsIgnoreCase(tag_name)) {
					CreditCard.mLogger.info("inside 1st if"+ "inside customer update req1");
					if (int_xml.containsKey(parent_tag)) {
						CreditCard.mLogger.info("inside 1st if"+"inside customer update req2");
						String xml_str = int_xml.get(parent_tag);
						CreditCard.mLogger.info("RLOS COMMON"+ " before adding address+ " + xml_str);
						xml_str = xml_str + CC_Comn.getCustAddress_details(Call_name);
						CreditCard.mLogger.info("RLOS COMMON"+ " after adding address+ " + xml_str);
						int_xml.put(parent_tag, xml_str);
					}
				}
				//added by akshay on 17/1/18 fpr proc 1595
				else if("AECBconsentHeld".equalsIgnoreCase(tag_name)){
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<" + tag_name + ">" + ("true".equalsIgnoreCase(formObject.getNGValue(form_control))?"Y":"N") + "</" + tag_name + ">";
	
					CreditCard.mLogger.info("RLOS COMMON"+ " after adding AECBconsentHeld flag+ " + xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
				}
			}
		}
		catch(Exception e){
			CreditCard.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + "+e.getMessage());
		}
		return int_xml;

	}
	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : new Card Custom       

	 ***********************************************************************************  */
	private Map<String, String> NEW_CARD_Custom(List<List<String>> DB_List,FormReference formObject,String callName) {

		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();

		try{
			for (List<String> mylist : DB_List) {

				for (int i = 0; i < 8; i++) {

					CreditCard.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");
				if ("VIPFlg".equalsIgnoreCase(tag_name)) {
					String vip_flag = "N";
					if ("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_VIPFlag"))) {
						vip_flag = "Y";
					}
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<" + tag_name + ">" + vip_flag + "</" + tag_name + ">";

					CreditCard.mLogger.info("RLOS COMMON"+ " after adding VIP flag+ " + xml_str);
					int_xml.put(parent_tag, xml_str);
				} else if ("ProcessingUserId".equalsIgnoreCase(tag_name)) {
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<" + tag_name + ">" + formObject.getUserName() + "</" + tag_name + ">";

					CreditCard.mLogger.info("RLOS COMMON"+ " after adding Minor flag+ " + xml_str);
					int_xml.put(parent_tag, xml_str);
				} else if ("ProcessingDate".equalsIgnoreCase(tag_name)) {
					String xml_str = int_xml.get(parent_tag);
					SimpleDateFormat sdf1 = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss.mmm+hh:mm");
					xml_str = xml_str + "<" + tag_name + ">" + sdf1.format(new Date()) + "</" + tag_name + ">";

					CreditCard.mLogger.info("RLOS COMMON"+ " after adding Minor flag+ " + xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
				}
			}
		}
		catch(Exception e){
			CreditCard.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + "+e.getMessage());
		}
		return int_xml;
	}
	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 10/12/2017              
		Author                              : Saurabh Gupta              
		Description                         : CARD_SERVICES_REQUEST_Custom       

	 ***********************************************************************************  */
	private Map<String, String> CARD_SERVICES_REQUEST_Custom(List<List<String>> DB_List,FormReference formObject,String callName) {

		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();

		try{
			for (List<String> mylist : DB_List) {

				for (int i = 0; i < 8; i++) {

					CreditCard.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");
				CreditCard.mLogger.info("CC_Integration_Input"+" CARD SERVICE custom function parent_tag: "+parent_tag+" tag_name: "+tag_name);
				if(tag_name.equalsIgnoreCase("LimitChangeType")){
					String xml_str = int_xml.get(parent_tag);

					xml_str = xml_str + "<"+tag_name+">"+(formObject.getNGValue("cmplx_LimitInc_Permanant").equalsIgnoreCase("Permanent")?"P":"T")
					+"</"+ tag_name+">";

					CreditCard.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}

				else if(tag_name.equalsIgnoreCase("ProcessedBy")){
					String xml_str = int_xml.get(parent_tag);

					xml_str = xml_str + "<"+tag_name+">"+formObject.getUserName()
					+"</"+ tag_name+">";

					CreditCard.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else if(tag_name.equalsIgnoreCase("SecurityChequeDetails")){
					CreditCard.mLogger.info("inside Generate XML "+"inside SecurityChequeDetails");
					if(int_xml.containsKey(parent_tag))
					{						
						String xml_str = int_xml.get(parent_tag);
						CreditCard.mLogger.info("RLOS COMMON"+" before adding cheque+ "+xml_str);
						xml_str = xml_str + getCheque_details("CARD_SERVICES_REQUEST");
						CreditCard.mLogger.info("RLOS COMMON"+" after adding cheque+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}		                            	
				}

				else if(tag_name.equalsIgnoreCase("LienDetails")){
					CreditCard.mLogger.info("inside 1st if"+"inside customer update req1");
					if(int_xml.containsKey(parent_tag))
					{
						CreditCard.mLogger.info("inside 1st if"+"inside customer update req2");
						String xml_str = int_xml.get(parent_tag);
						CreditCard.mLogger.info("RLOS COMMON"+" before adding LienDetails+ "+xml_str);
						xml_str = xml_str + getLien_details("CARD_SERVICES_REQUEST");
						CreditCard.mLogger.info("RLOS COMMON"+" after adding LienDetails+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}		                            	
				}
				else if(tag_name.equalsIgnoreCase("AppProcessingInfo")){
					CreditCard.mLogger.info("inside 1st if"+"inside Card Service <AppProcessingInfo>");                                              
					String xml_str = int_xml.get(parent_tag);
					CreditCard.mLogger.info("RLOS COMMON"+" before adding AppProcessingInfo+ "+xml_str);
					xml_str = xml_str + new CC_Common().getDecisionHistory_details();
					CreditCard.mLogger.info("RLOS COMMON"+" after adding AppProcessingInfo+ "+xml_str);
					int_xml.put(parent_tag, xml_str);

				}
				//change by saurabh on 12th Dec
				else if(tag_name.equalsIgnoreCase("SalaryDay")){
					CreditCard.mLogger.info("inside 1st if"+"inside customer update req1");
					String xml_str = int_xml.get(parent_tag);
					Calendar now = Calendar.getInstance();
					String month = "";
					String day = "";
					if((now.get(Calendar.MONTH) - 1)<10){
						month = "0"+(now.get(Calendar.MONTH) - 1);
					}else{
						month = ""+(now.get(Calendar.MONTH) - 1);
					}
					if(formObject.getNGValue("cmplx_IncomeDetails_SalaryDay").length()<2){
						day = "0" + formObject.getNGValue("cmplx_IncomeDetails_SalaryDay");
					}else{
						day = formObject.getNGValue("cmplx_IncomeDetails_SalaryDay");
					}


					String Current_date="";

					Current_date = now.get(Calendar.YEAR)+"-"+month+"-"+day;
					xml_str = xml_str+"<"+tag_name+">"+Current_date+"</"+ tag_name+">";

					CreditCard.mLogger.info("PL COMMON"+" after adding ApplicationID:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}
				//below code by saurabh on 15th Dec 17
				else if(tag_name.equalsIgnoreCase("FinancialServices") ){
					CreditCard.mLogger.info("inside Generate XML inside FinancialServices");
					if(int_xml.containsKey(parent_tag))
					{						
						String xml_str = int_xml.get(parent_tag);
						CreditCard.mLogger.info("Cc_integration_input before adding financialservices+ "+xml_str);
						xml_str = xml_str + getFinancialServices_details(callName);
						CreditCard.mLogger.info("Cc_integration_input after adding financialservices+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}		                            	
				}
				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
				}
			}
		}
		catch(Exception e){
			CreditCard.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + "+e.getMessage());
			CreditCard.mLogger.info("CC_INtegration Exception in CARD services custom function:  "+CC_Common.printException(e));
		}
		return int_xml;
	}
	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 10/12/2017              
		Author                              : Saurabh Gupta              
		Description                         : CARD_SERVICES_REQUEST_Custom       

	 ***********************************************************************************  */
	private Map<String, String> CARD_NOTIFICATION_Custom(List<List<String>> DB_List,FormReference formObject,String callName) {

		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();

		try{
			for (List<String> mylist : DB_List) {

				for (int i = 0; i < 8; i++) {

					CreditCard.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");
				String form_control = recordFileMap.get("form_control");
				CreditCard.mLogger.info("CC_Integration_Input"+" CARD SERVICE custom function parent_tag: "+parent_tag+" tag_name: "+tag_name);
				if("ReferenceDetails".equalsIgnoreCase(tag_name)){
					CreditCard.mLogger.info("inside 1st if"+" inside ReferenceDetails for Card NOTIF");
					if(int_xml.containsKey(parent_tag))
					{
						String xml_str = int_xml.get(parent_tag);
						CreditCard.mLogger.info("RLOS COMMON"+" before adding ReferenceDetails+ "+xml_str);
						xml_str = xml_str + getReference_details();
						CreditCard.mLogger.info("RLOS COMMON"+" after adding ReferenceDetails+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}

				}
				else if("AddrDet".equalsIgnoreCase(tag_name) || "AddressDetails".equalsIgnoreCase(tag_name)){
					CreditCard.mLogger.info("inside 1st if"+" inside customer update req1");
					if(int_xml.containsKey(parent_tag))
					{
						CreditCard.mLogger.info("inside 1st if"+" inside customer update req2");
						String xml_str = int_xml.get(parent_tag);
						CreditCard.mLogger.info("RLOS COMMON"+" before adding address+ "+xml_str);
						xml_str = xml_str + getCustAddress_details(callName);
						CreditCard.mLogger.info("RLOS COMMON"+" after adding address+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}		                            	
				}
				else if(tag_name.equalsIgnoreCase("InterestProfile")||tag_name.equalsIgnoreCase("FeeProfile")||tag_name.equalsIgnoreCase("TransactionFeeProfile")){
					CreditCard.mLogger.info("inside 1st if inside ReferenceDetails for Card NOTIF");
					if(int_xml.containsKey(parent_tag))
					{
						try{
							String xml_str = int_xml.get(parent_tag);
							CreditCard.mLogger.info("RLOS COMMON before adding ReferenceDetails+ "+xml_str);
							String intrest_val = "";
							String intrest_Query = "";
							String card_product = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5);
							if(card_product==null|| card_product.equals("")){
								intrest_val="NA";
							} 
							else{
								if(tag_name.equalsIgnoreCase("InterestProfile")){
									intrest_Query="select code from NG_MASTER_Int_Profile with (nolock) where Product = '"+card_product+"'";
								}
								else if(tag_name.equalsIgnoreCase("FeeProfile")||tag_name.equalsIgnoreCase("FeeProfileSerNo")){
									intrest_Query="select code from NG_MASTER_Fee_Profile with (nolock) where Product = '"+card_product+"'";
								}
								else if(tag_name.equalsIgnoreCase("TransactionFeeProfile")||tag_name.equalsIgnoreCase("TxnProfileSerNo")){
									intrest_Query="select code from NG_MASTER_TransactionFee_Profile with (nolock) where Product = '"+card_product+"'";
								}
								List<List<String>> intsert_list = formObject.getNGDataFromDataCache(intrest_Query);
								if(!intsert_list.isEmpty()){
									intrest_val = intsert_list.get(0).get(0);
								}	
							}

							xml_str = xml_str+"<"+tag_name+">"+intrest_val+"</"+ tag_name+">";
							CreditCard.mLogger.info("RLOS COMMON after adding ReferenceDetails+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}
						catch(Exception e){
							CreditCard.mLogger.info("CC Common Exception occured while setting intrest data");
						}
					}		                            	
				}
				else if("RVCStatus".equalsIgnoreCase(tag_name)){
					String xml_str = int_xml.get(parent_tag);
					String RVC_status = (formObject.getNGValue(form_control).equalsIgnoreCase("true")?"Y":"N");;

					xml_str = xml_str + "<"+tag_name+">"+RVC_status+"</"+ tag_name+">";

					CreditCard.mLogger.info("RLOS COMMON"+" after adding RVCStatus+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else if("DispatchMode".equalsIgnoreCase(tag_name)){
					//String xml_str = int_xml.get(parent_tag);
					String Dispatch_details = formObject.getNGValue(form_control);
					String Branch_details="";
					if(Dispatch_details.equalsIgnoreCase("ByBranch")){
						Branch_details = "<BranchDetails><BranchCode>"+formObject.getNGValue("AlternateContactDetails_CustdomBranch")+"</BranchCode></BranchDetails>";
					}
					String xml_str =  "<"+tag_name+">"+Dispatch_details+"</"+ tag_name+">"+Branch_details;

					CreditCard.mLogger.info("RLOS COMMON"+" after adding RVCStatus+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else if(tag_name.equalsIgnoreCase("IsNTB") || tag_name.equalsIgnoreCase("VIPFlag") || tag_name.equalsIgnoreCase("VIPFlg") || tag_name.equalsIgnoreCase("NonResidentFlag")||tag_name.equalsIgnoreCase("EStatementFlag") ){
					String xml_str = int_xml.get(parent_tag);
					String tagValue="";

					if(tag_name.equalsIgnoreCase("IsNTB")){
						tagValue=(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true")?"Y":"N");
					}
					/*else if((tag_name.equalsIgnoreCase("VIPFlag") || tag_name.equalsIgnoreCase("VIPFlg"))&&Call_name.equalsIgnoreCase("NEW_CREDITCARD_REQ")){
						tagValue=(formObject.getNGValue("cmplx_Customer_VIPFlag").equalsIgnoreCase("true")?"1":"0");
					}*/
					else if((tag_name.equalsIgnoreCase("VIPFlag") || tag_name.equalsIgnoreCase("VIPFlg"))&& !callName.equalsIgnoreCase("NEW_CREDITCARD_REQ")){
						tagValue=(formObject.getNGValue("cmplx_Customer_VIPFlag").equalsIgnoreCase("true")?"Y":"N");
					}
					else if(tag_name.equalsIgnoreCase("NonResidentFlag")){
						tagValue=(formObject.getNGValue("cmplx_Customer_ResidentNonResident").equalsIgnoreCase("Resident")?"Y":"N");
					}	
					else if(tag_name.equalsIgnoreCase("EStatementFlag")){
						tagValue=(formObject.getNGValue("AlternateContactDetails_eStatementFlag").equalsIgnoreCase("Yes")?"Y":"N");
					}

					if(!tagValue.equals("")){
						xml_str = xml_str + "<"+tag_name+">"+tagValue+"</"+ tag_name+">";
					}


					CreditCard.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else if(tag_name.equalsIgnoreCase("SecurityChequeDetails") ){
					CreditCard.mLogger.info("inside Generate XML inside SecurityChequeDetails");
					if(int_xml.containsKey(parent_tag))
					{						
						String xml_str = int_xml.get(parent_tag);
						CreditCard.mLogger.info("RLOS COMMON before adding cheque+ "+xml_str);
						xml_str = xml_str + getCheque_details(callName);
						CreditCard.mLogger.info("RLOS COMMON after adding cheque+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}		                            	
				}

				else if(tag_name.equalsIgnoreCase("LienDetails")){
					CreditCard.mLogger.info("inside 1st if inside customer update req1");
					if(int_xml.containsKey(parent_tag))
					{
						CreditCard.mLogger.info("inside 1st if"+"inside customer update req2");
						String xml_str = int_xml.get(parent_tag);
						CreditCard.mLogger.info("RLOS COMMON"+" before adding LienDetails+ "+xml_str);
						xml_str = xml_str + getLien_details(callName);
						CreditCard.mLogger.info("RLOS COMMON after adding LienDetails+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}		                            	
				}
				else if(tag_name.equalsIgnoreCase("ContactDetails")){
					CreditCard.mLogger.info("inside 1st if"+"inside customer update req1");
					if(int_xml.containsKey(parent_tag))
					{
						CreditCard.mLogger.info("inside 1st if"+"inside customer update req2");
						String xml_str = int_xml.get(parent_tag);
						CreditCard.mLogger.info("RLOS COMMON"+" before contact details+ "+xml_str);
						xml_str = xml_str + getcontact_details();
						CreditCard.mLogger.info("RLOS COMMON"+" after adding contact details+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}		                            	
				}
				else if(tag_name.equalsIgnoreCase("ResidenceSince"))
				{
					CreditCard.mLogger.info("inside 1st if inside customer update req1");
					String xml_str = int_xml.get(parent_tag);
					int size=formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
					String residenceSince="0";
					for(int i=0;i<size;i++){
						if(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,0).equalsIgnoreCase("RESIDENCE")){
							residenceSince=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,9);
							CreditCard.mLogger.info("inside ResidenceSince Value of Years in RESD Address: "+residenceSince);
						}
					}
					SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.YEAR, -Integer.parseInt(residenceSince));
					String residenceSince_date=sdf1.format(cal.getTime());
					CreditCard.mLogger.info("inside ResidenceSince Value of residenceSince_date: "+residenceSince_date);
					xml_str = xml_str + "<"+tag_name+">"+residenceSince_date+"</"+ tag_name+">";
					int_xml.put(parent_tag, xml_str);								
				}


				else if(tag_name.equalsIgnoreCase("SalaryDate") ){
					CreditCard.mLogger.info("inside 1st if inside customer update req1");
					String xml_str = int_xml.get(parent_tag);
					Calendar now = Calendar.getInstance();
					String month = "";
					String day = "";
					if((now.get(Calendar.MONTH) - 1)<10){
						month = "0"+(now.get(Calendar.MONTH) - 1);
					}else{
						month = ""+(now.get(Calendar.MONTH) - 1);
					}
					if(formObject.getNGValue("cmplx_IncomeDetails_SalaryDay").length()<2){
						day = "0" + formObject.getNGValue("cmplx_IncomeDetails_SalaryDay");
					}else{
						day = formObject.getNGValue("cmplx_IncomeDetails_SalaryDay");
					}


					String Current_date="";

					Current_date = now.get(Calendar.YEAR)+"-"+month+"-"+day;
					xml_str = xml_str+"<"+tag_name+">"+Current_date+"</"+ tag_name+">";

					CreditCard.mLogger.info("PL COMMON  after adding ApplicationID:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}
				else if(tag_name.equalsIgnoreCase("MinorFlag") ){
					if(int_xml.containsKey(parent_tag))
					{	//change from int to float by saurabh on 16th dec
						float Age = Float.parseFloat(formObject.getNGValue("cmplx_Customer_age"));
						String age_flag = "N";
						if(Age<18)
							age_flag="Y";
						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<"+tag_name+">"+age_flag
						+"</"+ tag_name+">";

						CreditCard.mLogger.info("RLOS COMMON  after adding Minor flag+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}		                            	
				}
				else if(tag_name.equalsIgnoreCase("ApplicationNumber") || tag_name.equalsIgnoreCase("InstitutionID")){
					CreditCard.mLogger.info("inside 1st if inside customer update req1");
					String xml_str = int_xml.get(parent_tag);

					xml_str = xml_str+"<"+tag_name+">"+formObject.getWFWorkitemName().substring(5,14)+"</"+ tag_name+">";

					CreditCard.mLogger.info("CC COMMON  after adding ApplicationNumber:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}


				else if(tag_name.equalsIgnoreCase("DSAId")){
					CreditCard.mLogger.info("inside 1st if inside customer update req1");
					String xml_str = int_xml.get(parent_tag);
					xml_str =xml_str+ "<"+tag_name+">"+"99D1243"+"</"+ tag_name+">";

					CreditCard.mLogger.info("PL COMMON  after adding DSAId:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}
				//below code by saurabh on 15th Dec 17
				else if(tag_name.equalsIgnoreCase("FinancialServices") ){
					CreditCard.mLogger.info("inside Generate XML inside FinancialServices");
					if(int_xml.containsKey(parent_tag))
					{						
						String xml_str = int_xml.get(parent_tag);
						CreditCard.mLogger.info("Cc_integration_input before adding financialservices+ "+xml_str);
						xml_str = xml_str + getFinancialServices_details(callName);
						CreditCard.mLogger.info("Cc_integration_input after adding financialservices+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}		                            	
				}
				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
				}
			}
		}
		catch(Exception e){
			CreditCard.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + "+e.getMessage());
			CreditCard.mLogger.info("CC_INtegration Exception in CARD services custom function:  "+CC_Common.printException(e));
		}
		return int_xml;
	}
	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 10/12/2017              
		Author                              : Saurabh Gupta              
		Description                         : CARD_SERVICES_REQUEST_Custom       

	 ***********************************************************************************  */
	private Map<String, String> NEW_CREDITCARD_Custom(List<List<String>> DB_List,FormReference formObject,String callName) {

		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();

		try{
			for (List<String> mylist : DB_List) {

				for (int i = 0; i < 8; i++) {

					CreditCard.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");
				String form_control = recordFileMap.get("form_control");

				CreditCard.mLogger.info("CC_Integration_Input"+" CARD SERVICE custom function parent_tag: "+parent_tag+" tag_name: "+tag_name);
				if("ReferenceDetails".equalsIgnoreCase(tag_name)){
					CreditCard.mLogger.info("inside 1st if"+" inside ReferenceDetails for Card NOTIF");
					if(int_xml.containsKey(parent_tag))
					{
						String xml_str = int_xml.get(parent_tag);
						CreditCard.mLogger.info("RLOS COMMON"+" before adding ReferenceDetails+ "+xml_str);
						xml_str = xml_str + getReference_details();
						CreditCard.mLogger.info("RLOS COMMON"+" after adding ReferenceDetails+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}

				}
				else if("PeriodicCashLimit".equalsIgnoreCase(tag_name) || "StatementCycle".equalsIgnoreCase(tag_name)){
					CreditCard.mLogger.info("inside else if for PeriodicCashLimit");
					if(int_xml.containsKey(parent_tag))
					{
						String statCycle = formObject.getNGValue("cmplx_IncomeDetails_StatementCycle");
						if(statCycle!=null && !"".equalsIgnoreCase(statCycle) && !" ".equalsIgnoreCase(statCycle) && !"--Select--".equalsIgnoreCase(statCycle)){
							String result = "";
							String subStr = statCycle.substring(0, statCycle.indexOf("of each month"));
							for(int i=0;i<subStr.length();i++){
								int num = Character.getNumericValue(subStr.charAt(i));
								if(num>=0 && num<=9){
									result+=subStr.charAt(i);
								}
							}
							String xml_str = int_xml.get(parent_tag);
							CreditCard.mLogger.info("RLOS COMMON before adding PeriodicCashLimit+ "+xml_str);
							xml_str = xml_str +"<"+tag_name+">"+result+"</"+tag_name+">";
							CreditCard.mLogger.info("RLOS COMMON"+" after adding PeriodicCashLimit+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}
					}
				}
				else if("AddrDet".equalsIgnoreCase(tag_name) || "AddressDetails".equalsIgnoreCase(tag_name)){
					CreditCard.mLogger.info("inside 1st if"+" inside customer update req1");
					if(int_xml.containsKey(parent_tag))
					{
						CreditCard.mLogger.info("inside 1st if"+" inside customer update req2");
						String xml_str = int_xml.get(parent_tag);
						CreditCard.mLogger.info("RLOS COMMON"+" before adding address+ "+xml_str);
						xml_str = xml_str + getCustAddress_details(callName);
						CreditCard.mLogger.info("RLOS COMMON"+" after adding address+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}		                            	
				}
				else if(tag_name.equalsIgnoreCase("FeeProfileSerNo")||tag_name.equalsIgnoreCase("TxnProfileSerNo")){
					CreditCard.mLogger.info("inside 1st if"+" inside ReferenceDetails for Card NOTIF");
					if(int_xml.containsKey(parent_tag))
					{
						try{
							String xml_str = int_xml.get(parent_tag);
							CreditCard.mLogger.info("RLOS COMMON"+" before adding ReferenceDetails+ "+xml_str);
							String intrest_val = "";
							String intrest_Query = "";
							String card_product = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5);
							if(card_product==null|| card_product.equals("")){
								intrest_val="NA";
							} 
							else{
								if(tag_name.equalsIgnoreCase("InterestProfile")){
									intrest_Query="select code from NG_MASTER_Int_Profile with (nolock) where Product = '"+card_product+"'";
								}
								else if(tag_name.equalsIgnoreCase("FeeProfile")||tag_name.equalsIgnoreCase("FeeProfileSerNo")){
									intrest_Query="select code from NG_MASTER_Fee_Profile with (nolock) where Product = '"+card_product+"'";
								}
								else if(tag_name.equalsIgnoreCase("TransactionFeeProfile")||tag_name.equalsIgnoreCase("TxnProfileSerNo")){
									intrest_Query="select code from NG_MASTER_TransactionFee_Profile with (nolock) where Product = '"+card_product+"'";
								}
								List<List<String>> intsert_list = formObject.getNGDataFromDataCache(intrest_Query);
								if(!intsert_list.isEmpty()){
									intrest_val = intsert_list.get(0).get(0);
								}	
							}

							xml_str = xml_str+"<"+tag_name+">"+intrest_val+"</"+ tag_name+">";
							CreditCard.mLogger.info("RLOS COMMON"+" after adding ReferenceDetails+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}
						catch(Exception e){
							CreditCard.mLogger.info("CC Common"+ "Exception occured while setting intrest data");
						}
					}		                            	
				}
				//case added by saurabh on 28th Jan
				else if(tag_name.equalsIgnoreCase("FullName")){
					String xml_str = int_xml.get(parent_tag);
					String fullName = formObject.getNGValue("cmplx_Customer_FirstNAme")+" "+formObject.getNGValue("cmplx_Customer_MiddleNAme")+" "+formObject.getNGValue("cmplx_Customer_LastNAme");
					xml_str = xml_str + "<"+tag_name+">"+fullName+"</"+ tag_name+">";

					CreditCard.mLogger.info("RLOS COMMON"+" after adding BankName+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else if(tag_name.equalsIgnoreCase("CIFId")){
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<"+tag_name+">C"+formObject.getNGValue(form_control)+"</"+ tag_name+">";

					CreditCard.mLogger.info("RLOS COMMON"+" after adding BankName+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else if(tag_name.equalsIgnoreCase("IsNTB") || tag_name.equalsIgnoreCase("VIPFlag") || tag_name.equalsIgnoreCase("VIPFlg") || tag_name.equalsIgnoreCase("NonResidentFlag")||tag_name.equalsIgnoreCase("EStatementFlag")){
					String xml_str = int_xml.get(parent_tag);
					String tagValue="";
					if(tag_name.equalsIgnoreCase("IsNTB")){
						tagValue=(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true")?"-1":"0");
					}
					/*else if(tag_name.equalsIgnoreCase("IsNTB")){
						tagValue=(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true")?"Y":"N");
					}*/
					/*else if((tag_name.equalsIgnoreCase("VIPFlag") || tag_name.equalsIgnoreCase("VIPFlg"))&&Call_name.equalsIgnoreCase("NEW_CREDITCARD_REQ")){
						tagValue=(formObject.getNGValue("cmplx_Customer_VIPFlag").equalsIgnoreCase("true")?"1":"0");
					}*/
					else if(tag_name.equalsIgnoreCase("VIPFlag") || tag_name.equalsIgnoreCase("VIPFlg")){
						//change by saurabh on 28th Jan
						tagValue=(formObject.getNGValue("cmplx_Customer_VIPFlag").equalsIgnoreCase("true")?"1":"0");
					}
					else if(tag_name.equalsIgnoreCase("NonResidentFlag")){
						tagValue=(formObject.getNGValue("cmplx_Customer_ResidentNonResident").equalsIgnoreCase("Resident")?"Y":"N");
					}	
					else if(tag_name.equalsIgnoreCase("EStatementFlag")){
						tagValue=(formObject.getNGValue("AlternateContactDetails_eStatementFlag").equalsIgnoreCase("Yes")?"Y":"N");
					}

					if(!tagValue.equals("")){
						xml_str = xml_str + "<"+tag_name+">"+tagValue+"</"+ tag_name+">";
					}


					CreditCard.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}

				else if("MaritalStatus".equalsIgnoreCase(tag_name)){
					CreditCard.mLogger.info("inside 1st if"+"inside maritial NEW_CREDITCARD_REQ");
					String xml_str = int_xml.get(parent_tag);
					String marital="5";
					if(formObject.getNGValue("cmplx_Customer_MAritalStatus").equalsIgnoreCase("M")){
						marital="2";
					}
					else if(formObject.getNGValue("cmplx_Customer_MAritalStatus").equalsIgnoreCase("S")){
						marital="1";
					}
					else if(formObject.getNGValue("cmplx_Customer_MAritalStatus").equalsIgnoreCase("D")){
						marital="3";
					}
					else if(formObject.getNGValue("cmplx_Customer_MAritalStatus").equalsIgnoreCase("W")){
						marital="4";
					}
					xml_str = xml_str+"<"+tag_name+">"+marital+"</"+ tag_name+">";

					CreditCard.mLogger.info("PL COMMON"+" after adding maritia] NEW_CREDITCARD_REQ:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}

				/*else if("StatementCycle".equalsIgnoreCase(tag_name)){
					String xml_str = int_xml.get(parent_tag);
					String statCycle=formObject.getNGValue("cmplx_IncomeDetails_StatementCycle");
					if(statCycle.length()==1)
						statCycle="-20"+statCycle;

					else if(statCycle.length()==2)
						statCycle="-2"+statCycle;

					xml_str = xml_str + "<"+tag_name+">"+statCycle+"</"+ tag_name+">";

					CreditCard.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}*/

				else if("BranchId".equalsIgnoreCase(tag_name) || "BankSortCode".equalsIgnoreCase(tag_name)){
					String xml_str = "";
					if(int_xml.containsKey(parent_tag)){
						xml_str = int_xml.get(parent_tag);
					}
					String CardDispatch=formObject.getNGValue("AlternateContactDetails_CardDisp");
					String tagValue="";

					if(CardDispatch!=null && CardDispatch.equalsIgnoreCase("ByCourier"))
					{
						tagValue="998";
					}
						else
						{
						tagValue=formObject.getNGValue("AlternateContactDetails_CustdomBranch")!=null?formObject.getNGValue("AlternateContactDetails_CustdomBranch"):"";
						}
						xml_str = xml_str+"<"+tag_name+">"+tagValue+"</"+ tag_name+">";
						CreditCard.mLogger.info("RLOS COMMON"+" after adding bhrabc id flag+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
						
				}
/*
				else if("ProcessedBy".equalsIgnoreCase(tag_name) ){
					String xml_str = int_xml.get(parent_tag);

					xml_str = xml_str + "<"+tag_name+">"+formObject.getUserName()
					+"</"+ tag_name+">";

					CreditCard.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}*/
				else if("ApplicationNumber".equalsIgnoreCase(tag_name) || "InstitutionID".equalsIgnoreCase(tag_name)){
					CreditCard.mLogger.info("inside 1st if inside customer update req1");
					String xml_str = int_xml.get(parent_tag);
					//change by saurabh on 28th Jan
					String wiNum = (formObject.getWFWorkitemName()).split("-")[1];
					xml_str = xml_str+"<"+tag_name+">"+wiNum+"</"+ tag_name+">";

					CreditCard.mLogger.info("CC COMMON  after adding ApplicationNumber:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}
				else if("Gender".equalsIgnoreCase(tag_name) ){
					CreditCard.mLogger.info("inside 1st if inside customer update req1");
					String xml_str = int_xml.get(parent_tag);
					xml_str =xml_str+ "<"+tag_name+">"+"1"+"</"+ tag_name+">";

					CreditCard.mLogger.info("PL COMMON  after adding DSAId:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}

				else if("ProcessDate".equalsIgnoreCase(tag_name) || "ApplicationDate".equalsIgnoreCase(tag_name)){
					CreditCard.mLogger.info("inside 1st if inside NEW_CREDITCARD_REQ req1");
					String xml_str = int_xml.get(parent_tag);
					Date d1=new Date();
					String Date="";

					SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
					Date=sf.format(d1);

					//d2.parse(sf.format(d1));
					xml_str =xml_str+ "<"+tag_name+">"+Date+"</"+ tag_name+">";

					CreditCard.mLogger.info("PL COMMON  after adding DSAId:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}

				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
				}
			}
		}
		catch(Exception e){
			CreditCard.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + "+e.getMessage());
			CreditCard.mLogger.info("CC_INtegration Exception in CARD services custom function:  "+CC_Common.printException(e));
		}
		return int_xml;
	}

	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 10/12/2017              
		Author                              : Saurabh Gupta              
		Description                         : get Contract details for Card Notification       

	 ***********************************************************************************  */
	public String getcontact_details(){
		CreditCard.mLogger.info("RLOSCommon java file inside getContact_details : ");
		String  Contactdetails_xml_str ="";

		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			Contactdetails_xml_str = "<ContactDetails><PrefModeOfContact>Phone</PrefModeOfContact><PhnDet><PhoneType>MobileNumber1</PhoneType><PhnCountryCode>971</PhnCountryCode><CityCode></CityCode><PhnLocalCode>00971</PhnLocalCode><PhoneNumber>"+formObject.getNGValue("cmplx_Customer_MobileNo")+"</PhoneNumber>";
			Contactdetails_xml_str = Contactdetails_xml_str+"<PhnPrefFlag>Y</PhnPrefFlag></PhnDet></ContactDetails><ContactDetails><PrefModeOfContact>Email</PrefModeOfContact><EmailDet><MailIdType>WORKEML</MailIdType><EmailID>"+formObject.getNGValue("AlternateContactDetails_Email1")+"</EmailID><MailPrefFlag>Y</MailPrefFlag></EmailDet></ContactDetails><ContactDetails><PrefModeOfContact>Phone</PrefModeOfContact><PhnDet><PhoneType>HOMEPH2</PhoneType><PhnCountryCode>00971</PhnCountryCode>";
			Contactdetails_xml_str = Contactdetails_xml_str+"<CityCode></CityCode><PhnLocalCode>00971</PhnLocalCode><PhoneNumber>"+formObject.getNGValue("AlternateContactDetails_OfficeNo")+"</PhoneNumber><PhnPrefFlag>N</PhnPrefFlag></PhnDet></ContactDetails>";

		}
		catch(Exception e){
			CreditCard.mLogger.info("PL Common java file Exception occured in get lien details method : "+e.getMessage());
		}

		return Contactdetails_xml_str;

	}
	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 10/12/2017              
		Author                              : Saurabh Gupta              
		Description                         : get cheque details for Card Notification       

	 ***********************************************************************************  */
	public String getCheque_details(String Call_name){
		CreditCard.mLogger.info("RLOSCommon java file inside getCheque_details : ");
		String  cheque_xml_str ="";

		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			int cheque_row_count = formObject.getLVWRowCount("cmplx_CardDetails_cmpmx_gr_cardDetails");
			CreditCard.mLogger.info("PL Common java file inside getCheque_details cheque_row_count+ : "+cheque_row_count);

			SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
			Date maxdate=sdf.parse(formObject.getNGValue("cmplx_CardDetails_cmpmx_gr_cardDetails", 0, 3));
			CreditCard.mLogger.info("PL Common inside getCheque_details maxDate in seconds : "+maxdate);
			int maxindex=0;
			for(int i=1;i<cheque_row_count;i++)
			{				
				String date = formObject.getNGValue("cmplx_CardDetails_cmpmx_gr_cardDetails", i, 3); //0
				Date curr_date=sdf.parse(date);
				CreditCard.mLogger.info("PL Common inside getCheque_details Grid date,curr_date is: "+date+" ,"+curr_date);

				if(curr_date.after(maxdate)){
					CreditCard.mLogger.info("PL Common inside getCheque_details Current Date is after max date");
					maxdate=curr_date;
					maxindex=i;
				}
			}	

			String bankName = formObject.getNGValue("cmplx_CardDetails_cmpmx_gr_cardDetails", maxindex, 0); //0
			String cheqNo = formObject.getNGValue("cmplx_CardDetails_cmpmx_gr_cardDetails", maxindex, 1); //0
			String Amount = formObject.getNGValue("cmplx_CardDetails_cmpmx_gr_cardDetails", maxindex, 2); //0
			String chqDate = formObject.getNGValue("cmplx_CardDetails_cmpmx_gr_cardDetails", maxindex, 3); //0

			if(Call_name.equalsIgnoreCase("CARD_NOTIFICATION")){
				cheque_xml_str = cheque_xml_str+ "<SecurityChequeDetails><BankName>"+bankName+"</BankName>";
				cheque_xml_str = cheque_xml_str+ "<ChequeNo>"+cheqNo+"</ChequeNo>";
				cheque_xml_str = cheque_xml_str+ "<Amount>"+Amount+"</Amount>";
				cheque_xml_str = cheque_xml_str+ "<Date>"+new Common_Utils(CreditCard.mLogger).Convert_dateFormat(chqDate, "dd/MM/yyyy","yyyy-MM-dd")+"</Date></SecurityChequeDetails>";
			}
			else if(Call_name.equalsIgnoreCase("CARD_SERVICES_REQUEST")){
				cheque_xml_str = cheque_xml_str+ "<SecurityChequeDetails><BankName>"+bankName+"</BankName>";
				cheque_xml_str = cheque_xml_str+ "<ChequeNo>"+cheqNo+"</ChequeNo>";
				cheque_xml_str = cheque_xml_str+ "<ChequeAmount>"+Amount+"</ChequeAmount>";
				cheque_xml_str = cheque_xml_str+ "<ChequeDate>"+new Common_Utils(CreditCard.mLogger).Convert_dateFormat(chqDate, "dd/MM/yyyy","yyyy-MM-dd")+"</ChequeDate></SecurityChequeDetails>";

			}

			CreditCard.mLogger.info("PL Common cheque_xml_str after adding cheque Details : "+cheque_xml_str);


			/*else{
				CreditCard.mLogger.info("CC Common cheque_xml_str NO record found in Grid");
			}*/

		}
		catch(Exception e){
			CreditCard.mLogger.info("PL Common java file Exception occured in get lien details method : "+e.getMessage());
		}

		return cheque_xml_str;

	}

	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 15/12/2017              
		Author                              : Saurabh Gupta              
		Description                         : get Financial Services details for Card Notification       

	 ***********************************************************************************  */
	public String getFinancialServices_details(String Call_name){
		CreditCard.mLogger.info("CC_Integration_Input java file inside getFinancialServices_details : ");
		String  Financial_xml_str ="";
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String listViewname = "cmplx_CC_Loan_cmplx_btc";
			int rowCount = formObject.getLVWRowCount(listViewname);
			if(rowCount>0){
				UIComponent pComp =formObject.getComponent("cmplx_CC_Loan_cmplx_btc");
				int columns=0;
				List<String> gridColumnNames = new ArrayList<String>();
				List<String> transTypes =  new ArrayList<String>();
				if( pComp != null && pComp instanceof ListView )
				{			
					ListView objListView = ( ListView )pComp;
					columns  = objListView.getChildCount();
					for(int i=0;i<columns;i++){
						gridColumnNames.add(((Column)(pComp.getChildren().get(i))).getName());
					}
					for(int j=0;j<rowCount;j++){
						transTypes.add(formObject.getNGValue(listViewname, j, gridColumnNames.indexOf("Transaction Type")));
					}
				}

				int chosenRow = 0;//currently 0 for testing but has to be -1.
				String subProduct = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2);
				String appType = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,4);
				if(subProduct.equalsIgnoreCase("IM") && appType.contains("NEW")){
					chosenRow = transTypes.indexOf("LOC");
				}
				else if(subProduct.equalsIgnoreCase("BTC")){
					chosenRow = transTypes.indexOf("BT");
				}
				if(chosenRow!=-1){
					String serviceType = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Transaction Type"));
					String amount = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Amount"));
					String cc_no = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("CreditCard No"));
					String benefName = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Beneficiary name"));
					String payMode = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Transfer Mode"));
					String paymentPurpose = "PSC";
					String dispChanel = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Dispatch Channel"));
					String servMarkCode = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Marketing code"));
					String servAppCode = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Approval code"));
					String servSourceCode = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Source Code"));
					String nameOnCard = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Name on card"));
					String trfDate = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Date"));
					String tenor = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Tenor"));
					String appstatus = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Application Status"));

					//String servAppCode = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Approval code"));
					//String bankName = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("bank Name"));
					String marketingCode = formObject.getNGValue("cmplx_EmploymentDetails_marketcode");

					String bankCode = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("bank Name"));
					formObject.setNGValue("bankName", bankCode);
					String bankName = formObject.getNGItemText("bankName", formObject.getSelectedIndex("bankName"));
					formObject.setNGValue("bankName", "");
					String branchName = formObject.getNGValue("AlternateContactDetails_CustdomBranch");
					String iban = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("IBAN"));
					String cardEmboss = cc_no;
					String custFullName = formObject.getNGValue("cmplx_Customer_Shortname");
					Financial_xml_str+="<FinancialServices>";
					if(Call_name.equalsIgnoreCase("CARD_NOTIFICATION")){
						if(checkValue(serviceType)){
							Financial_xml_str+="<ServiceType>"+serviceType+"</ServiceType>";
						}
						if(checkValue(amount)){
							Financial_xml_str+="<Amount>"+amount+"</Amount>";
						}
						if(checkValue(cc_no)){
							Financial_xml_str+="<CardNumber>"+cc_no+"</CardNumber>";
						}
						if(checkValue(benefName)){
							Financial_xml_str+="<BenificiaryName>"+benefName+"</BenificiaryName>";
						}
						if(checkValue(payMode)){
							Financial_xml_str+="<PaymentMode>"+payMode+"</PaymentMode>";
						}
						Financial_xml_str+="<PaymentPurpose>PSC</PaymentPurpose>";
						if(checkValue(dispChanel)){
							Financial_xml_str+="<DispatchChannel>"+dispChanel+"</DispatchChannel>";
						}
						if(checkValue(servMarkCode)){
							Financial_xml_str+="<ServiceMarketingCode>"+servMarkCode+"</ServiceMarketingCode>";
						}
						if(checkValue(servAppCode)){
							Financial_xml_str+="<ServiceApprovalCode>"+servAppCode+"</ServiceApprovalCode>";
						}
						if(checkValue(servSourceCode)){
							Financial_xml_str+="<ServiceSourceCode>"+servSourceCode+"</ServiceSourceCode>";
						}
						Financial_xml_str+="<OtherBankInfo>";
						if(checkValue(bankCode)){
							Financial_xml_str+="<BankCode>"+bankCode+"</BankCode>";
						}
						if(checkValue(iban)){
							Financial_xml_str+="<IBAN>"+iban+"</IBAN>";
						}
						if(checkValue(cardEmboss)){
							Financial_xml_str+="<CardEmbossNum>"+cardEmboss+"</CardEmbossNum>";
						}
						if(checkValue(custFullName)){
							Financial_xml_str+="<CustomerName>"+custFullName+"</CustomerName>";
						}
						Financial_xml_str+="</OtherBankInfo>";
					}
					else if(Call_name.equalsIgnoreCase("CARD_SERVICES_REQUEST")){
						if(checkValue(serviceType)){
							if(serviceType.equalsIgnoreCase("LOC")){
								serviceType = "LoanOnCard";
							}
							Financial_xml_str+="<RequestType>"+serviceType+"</RequestType>";
						}
						Financial_xml_str+="<ReferenceNumber>12345</ReferenceNumber>";
						if(checkValue(payMode)){
							Financial_xml_str+="<PaymentMode>"+payMode+"</PaymentMode>";
						}
						if(checkValue(bankCode)){
							Financial_xml_str+="<BankCode>"+bankCode+"</BankCode>";
						}
						if(checkValue(bankName)){
							Financial_xml_str+="<BankName>"+bankCode+"</BankName>";
						}
						if(checkValue(benefName)){
							Financial_xml_str+="<BenificiaryName>"+benefName+"</BenificiaryName>";
						}
						if(checkValue(cc_no)){
							Financial_xml_str+="<OtherBankCardNo>"+cc_no+"</OtherBankCardNo>";
						}
						if(checkValue(custFullName)){
							Financial_xml_str+="<CustomerName>"+custFullName+"</CustomerName>";
						}
						if(checkValue(nameOnCard)){
							Financial_xml_str+="<NameOnCard>"+nameOnCard+"</NameOnCard>";
						}
						if(checkValue(dispChanel)){
							Financial_xml_str+="<DispatchChannel>"+dispChanel+"</DispatchChannel>";
						}
						if(checkValue(branchName)){
							Financial_xml_str+="<BranchName>"+branchName+"</BranchName>";
						}
						if(checkValue(iban)){
							Financial_xml_str+="<IBAN>"+iban+"</IBAN>";
						}
						if(checkValue(amount)){
							Financial_xml_str+="<Amount>"+amount+"</Amount>";
						}
						if(checkValue(trfDate)){
							Financial_xml_str+="<TransferDate>"+new Common_Utils(CreditCard.mLogger).Convert_dateFormat(trfDate, "dd/MM/yyyy","yyyy-MM-dd")+"</TransferDate>";
						}
						if(checkValue(tenor)){
							Financial_xml_str+="<Tenor>"+tenor+"</Tenor>";
						}
						if(checkValue(marketingCode)){
							Financial_xml_str+="<MarketingCode>"+marketingCode+"</MarketingCode>";
						}
						if(checkValue(appstatus)){
							Financial_xml_str+="<ApplicationStatus>"+appstatus+"</ApplicationStatus>";
						}
						if(checkValue(servAppCode)){
							Financial_xml_str+="<ApprovalCode>"+servAppCode+"</ApprovalCode>";
						}
					}
					Financial_xml_str+="</FinancialServices>";
				}

			}
			return Financial_xml_str;
		}catch(Exception ex){
			CreditCard.mLogger.info("PL Common java file Exception occured in get lien details method : "+CC_Common.printException(ex));
			return "";
		}
	}

	public boolean checkValue(String ngValue){
		if(ngValue==null || "".equalsIgnoreCase(ngValue) || " ".equalsIgnoreCase(ngValue) || "--Select--".equalsIgnoreCase(ngValue)){
			return false;
		}
		return true;
	}


	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 10/12/2017              
		Author                              : Saurabh Gupta              
		Description                         : Get Lien details for Card Notification       

	 ***********************************************************************************  */
	public String getLien_details(String Call_name){
		CreditCard.mLogger.info("RLOSCommon java file inside getLienDetails_details : ");
		String  lien_xml_str ="";

		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			int add_row_count = formObject.getLVWRowCount("cmplx_FinacleCore_liendet_grid");
			CreditCard.mLogger.info("PL Common java file inside Lien details add_row_count+ : "+add_row_count);

			if (add_row_count>=0){

				String Lien_no = formObject.getNGValue("cmplx_FinacleCore_liendet_grid", 0, 1); //0
				String Lien_amount = formObject.getNGValue("cmplx_FinacleCore_liendet_grid", 0, 2); //0
				String Lien_maturity_date = formObject.getNGValue("cmplx_FinacleCore_liendet_grid", 0, 6); //0
				if(Call_name.equalsIgnoreCase("CARD_SERVICES_REQUEST")){
					lien_xml_str = lien_xml_str+ "<LienDetails><LienNumber>"+Lien_no+"</LienNumber>";
					lien_xml_str = lien_xml_str+ "<LienCurrency>AED</LienCurrency><LienAmount>"+Lien_amount+"</LienAmount>";
					if(!(Lien_maturity_date.equals("") || Lien_maturity_date==null)){
						lien_xml_str = lien_xml_str+ "<LienMaturityDt>"+Lien_maturity_date+"</LienMaturityDt>";
					}
				}
				else if(Call_name.equalsIgnoreCase("CARD_NOTIFICATION")){

					lien_xml_str = lien_xml_str+ "<LienDetails><LienNumber>"+Lien_no+"</LienNumber>";
					lien_xml_str = lien_xml_str+ "<LienCurrency>AED</LienCurrency><Amount>"+Lien_amount+"</Amount>";
					if(!(Lien_maturity_date.equals("") || Lien_maturity_date==null)){
						lien_xml_str = lien_xml_str+ "<MaturityDate>"+Lien_maturity_date+"</MaturityDate>";
					}
				}
				lien_xml_str = lien_xml_str+ "</LienDetails>";
			}
		}
		catch(Exception e){
			CreditCard.mLogger.info("PL Common java file Exception occured in get lien details method : "+e.getMessage());
		}

		return lien_xml_str;

	}
	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : Dectech Custom    

	 ***********************************************************************************  */
	private Map<String, String> DECTECH_Custom(List<List<String>> DB_List,FormReference formObject,String callName) {

		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();

		try{
			for (List<String> mylist : DB_List) {

				for (int i = 0; i < 8; i++) {

					CreditCard.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");
				String emp_type = "";
				int prod_count = formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
				if (prod_count != 0) {
					emp_type = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",
							0, 6) == null ? "" : formObject.getNGValue(
									"cmplx_Product_cmplx_ProductGrid", 0, 6);
				}

				if ("Channel".equalsIgnoreCase(tag_name)) 
				{
					CreditCard.mLogger.info("RLOS COMMON"+ " iNSIDE channelcode+ ");

					String ReqProd = formObject.getNGValue(
							"cmplx_Product_cmplx_ProductGrid", 0, 1);

					String xml_str = int_xml.get(parent_tag);
					xml_str = "<" + tag_name + ">" + (NGFUserResourceMgr_CreditCard.getGlobalVar("CC_PersonalLoan").equalsIgnoreCase(ReqProd) ? "PL" : "CC") + "</" + tag_name + ">";

					CreditCard.mLogger.info("RLOS COMMON"+ " after adding channelcode+ " + xml_str);
					int_xml.put(parent_tag, xml_str);

				}

				else if ("emp_type".equalsIgnoreCase(tag_name)) {
					CreditCard.mLogger.info("RLOS COMMON"+ " iNSIDE channelcode+ ");

					String empttype = formObject.getNGValue(
							"cmplx_Product_cmplx_ProductGrid", 0, 6);
					if (empttype != null) {
						if ("Salaried".equalsIgnoreCase(empttype)) {
							empttype = "S";
						} else if ("Salaried Pensioner".equalsIgnoreCase(empttype)) {
							empttype = "SP";
						} else {
							empttype = "SE";
						}
					}
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<" + tag_name + ">" + empttype + "</" + tag_name + ">";

					CreditCard.mLogger.info("RLOS COMMON"+ " after adding channelcode+ " + xml_str);
					int_xml.put(parent_tag, xml_str);

				} else if ("world_check".equalsIgnoreCase(tag_name)) {
					CreditCard.mLogger.info("RLOS COMMON"+ " iNSIDE world_check+ ");

					String world_check = formObject.getNGValue("IS_WORLD_CHECK");
					CreditCard.mLogger.info("RLOS COMMON"+" iNSIDE world_check+ "+ formObject.getLVWRowCount("cmplx_WorldCheck_WorldCheck_Grid"));
					if (formObject.getLVWRowCount("cmplx_WorldCheck_WorldCheck_Grid") == 0) {
						world_check = "Negative";
					} else {
						world_check = "Positive";
					}

					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<" + tag_name + ">" + world_check + "</" + tag_name + ">";

					CreditCard.mLogger.info("RLOS COMMON"+ " after adding world_check+ " + xml_str);
					int_xml.put(parent_tag, xml_str);

				}
				else if("fd_amount".equalsIgnoreCase(tag_name)){
					CreditCard.mLogger.info("RLOS iNSIDE fd_amount+ ");
					String fd_amount=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 16);	
						String xml_str =  int_xml.get(parent_tag);
						xml_str = xml_str+ "<"+tag_name+">"+fd_amount
						+"</"+ tag_name+">";

						CreditCard.mLogger.info("after adding fd_amount+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}
				else if ("prev_loan_dbr".equalsIgnoreCase(tag_name)) {
					CreditCard.mLogger.info("RLOS COMMON"+ " iNSIDE prev_loan_dbr+ ");
					String PreviousLoanDBR = "";
					String PreviousLoanEmp = "";
					String PreviousLoanMultiple = "";
					String PreviousLoanTAI = "";

					String squeryloan = "select PreviousLoanDBR,PreviousLoanEmp,PreviousLoanMultiple,PreviousLoanTAI from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where Request_Type='CollectionsSummary' and Limit_Increase='true' and Child_Wi= '" + formObject.getWFWorkitemName() + "'";
					List<List<String>> prevLoan = formObject.getNGDataFromDataCache(squeryloan);
					CreditCard.mLogger.info("RLOS COMMON"+ " iNSIDE prev_loan_dbr+ " + squeryloan);

					if (prevLoan != null && prevLoan.size() > 0) {
						PreviousLoanDBR = prevLoan.get(0).get(0);
						PreviousLoanEmp = prevLoan.get(0).get(1);
						PreviousLoanMultiple = prevLoan.get(0).get(2);
						PreviousLoanTAI = prevLoan.get(0).get(3);
					}

					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<" + tag_name + ">" + PreviousLoanDBR + "</" + tag_name + "><prev_loan_tai>" + PreviousLoanTAI + "</prev_loan_tai><prev_loan_multiple>" + PreviousLoanMultiple + "</prev_loan_multiple><prev_loan_amount></prev_loan_amount><prev_loan_employer>" + PreviousLoanEmp + "</prev_loan_employer>";

					CreditCard.mLogger.info("RLOS COMMON"+ " after adding world_check+ " + xml_str);
					int_xml.put(parent_tag, xml_str);

				}

				else if ("no_of_cheque_bounce_int_3mon_Ind".equalsIgnoreCase(tag_name)) {
					CreditCard.mLogger.info("RLOS COMMON"+
					" iNSIDE no_of_cheque_bounce_int_3mon_Ind+ ");
					String squerynoc = "SELECT count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls with (nolock) WHERE CAST(returnDate AS datetime) >= DATEADD(month,-3,GETDATE()) and returntype='ICCS' and Child_Wi='" + formObject.getWFWorkitemName() + "'";
					List<List<String>> NOC = formObject
					.getNGDataFromDataCache(squerynoc);
					if (NOC != null && NOC.size() > 0) {
						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<" + tag_name + ">" + NOC.get(0).get(0) + "</" + tag_name + ">";

						CreditCard.mLogger.info("RLOS COMMON"+" after adding internal_blacklist+ " + xml_str);
						int_xml.put(parent_tag, xml_str);

					}

				} else if ("no_of_DDS_return_int_3mon_Ind".equalsIgnoreCase(tag_name)) {
					CreditCard.mLogger.info("RLOS COMMON"+
					" iNSIDE no_of_cheque_bounce_int_3mon_Ind+ ");
					String squerynoc = "SELECT count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls with (nolock) WHERE CAST(returnDate AS datetime) >= DATEADD(month,-3,GETDATE()) and returntype='DDS' and Child_Wi='" + formObject.getWFWorkitemName() + "'";
					List<List<String>> NOC = formObject
					.getNGDataFromDataCache(squerynoc);
					if (NOC != null && NOC.size() > 0) {
						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<" + tag_name + ">" + NOC.get(0).get(0) + "</" + tag_name + ">";

						CreditCard.mLogger.info("RLOS COMMON"+" after adding internal_blacklist+ " + xml_str);
						int_xml.put(parent_tag, xml_str);

					}

				} else if ("blacklist_cust_type".equalsIgnoreCase(tag_name)) {
					CreditCard.mLogger.info("RLOS COMMON"+ " iNSIDE channelcode+ ");
					String ParentWI_Name = formObject.getNGValue("Parent_WIName");
					String squeryBlacklist = "select BlacklistFlag,BlacklistDate,BlacklistReasonCode,NegatedFlag,NegatedDate,NegatedReasonCode from ng_rlos_cif_detail with (nolock) where cif_wi_name='" + ParentWI_Name + "' and cif_searchType = 'Internal'";
					List<List<String>> Blacklist = formObject
					.getNGDataFromDataCache(squeryBlacklist);
					String internal_blacklist = "";
					String internal_blacklist_date = "";
					String internal_blacklist_code = "";
					String internal_negative_flag = "";
					String internal_negative_date = "";
					String internal_negative_code = "";

					if (Blacklist != null && Blacklist.size() > 0) {
						internal_blacklist = Blacklist.get(0).get(0);
						internal_blacklist_date = Blacklist.get(0).get(1);
						internal_blacklist_code = Blacklist.get(0).get(2);
						internal_negative_flag = Blacklist.get(0).get(3);
						internal_negative_date = Blacklist.get(0).get(4);
						internal_negative_code = Blacklist.get(0).get(5);
					}
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<" + tag_name + ">I</" + tag_name + "><internal_blacklist>" + internal_blacklist + "</internal_blacklist><internal_blacklist_date>" + internal_blacklist_date + "</internal_blacklist_date><internal_blacklist_code>" + internal_blacklist_code + "</internal_blacklist_code><negative_cust_type>I</negative_cust_type><internal_negative_flag>" + internal_negative_flag + "</internal_negative_flag><internal_negative_date>" + internal_negative_date + "</internal_negative_date><internal_negative_code>" + internal_negative_code + "</internal_negative_code>";

					CreditCard.mLogger.info("RLOS COMMON"+
							" after adding internal_blacklist+ " + xml_str);
					int_xml.put(parent_tag, xml_str);

				} else if ((("lob".equalsIgnoreCase(tag_name))
						|| ("target_segment_code".equalsIgnoreCase(tag_name))
						|| ("designation".equalsIgnoreCase(tag_name))
						|| ("emp_name".equalsIgnoreCase(tag_name))
						|| ("industry_sector".equalsIgnoreCase(tag_name))
						|| ("industry_marco".equalsIgnoreCase(tag_name))
						|| ("industry_micro".equalsIgnoreCase(tag_name))
						|| ("bvr".equalsIgnoreCase(tag_name))
						|| ("eff_date_estba".equalsIgnoreCase(tag_name))
						|| ("poa".equalsIgnoreCase(tag_name))
						|| ("tlc_issue_date".equalsIgnoreCase(tag_name))
						|| ("cc_employer_status".equalsIgnoreCase(tag_name))
						|| ("pl_employer_status".equalsIgnoreCase(tag_name)) || ("marketing_code".equalsIgnoreCase(tag_name))||("eff_lob".equalsIgnoreCase(tag_name))||("current_emp_catogery".equalsIgnoreCase(tag_name)))&& "Self Employed".equalsIgnoreCase(emp_type)) {
					CreditCard.mLogger.info("RLOSCommon java file"+	"inside getProduct_details : ");
					String xml_str = int_xml.get(parent_tag);
					int Comp_row_count = formObject
					.getLVWRowCount("cmplx_CompanyDetails_cmplx_CompanyGrid");
					String lob = "";
					String target_segment_code = "";
					String designation = "";
					String emp_name = "";
					String industry_sector = "";
					String eff_date_estba = "";
					String industry_marco = "";
					String industry_micro = "";
					String bvr = "";
					String cc_employer_status = "";
					String pl_employer_status = "";
					String poa = "";
					String marketing_code = "";
					String effLOB="";
					String current_emp_catogery="";
					
					
					for (int i = 0; i < Comp_row_count; i++) {
						if (formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 2).equalsIgnoreCase("Secondary")){
						lob = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 15); // 0
						target_segment_code = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 22); // 0
						designation = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 11); // 0
						emp_name = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 0); // 0
						// industry_sector =
						// formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",
						// i, 8); //0
						eff_date_estba = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 14); // 0
						industry_sector = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 8); // 0
						industry_marco = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 9); // 0
						industry_micro = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 10); // 0
						bvr = (formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 18).equalsIgnoreCase("true")?"Y":"N"); // 0
						cc_employer_status = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 34); // 0
						pl_employer_status = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 35); // 0
						poa = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 20); // 0
						marketing_code = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 28); // 0
						current_emp_catogery = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 33);
						effLOB = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 37); //0
						}
					}
					if ("lob".equalsIgnoreCase(tag_name)) {
						xml_str = xml_str + "<" + tag_name + ">" + lob + "</" + tag_name + ">";
					} else if ("target_segment_code".equalsIgnoreCase(tag_name)) {
						xml_str = xml_str + "<" + tag_name + ">" + target_segment_code + "</" + tag_name + ">";
					} else if ("designation".equalsIgnoreCase(tag_name)) {
						xml_str = xml_str + "<" + tag_name + ">" + designation + "</" + tag_name + ">";
					} else if ("emp_name".equalsIgnoreCase(tag_name)) {
						xml_str = xml_str + "<" + tag_name + ">" + emp_name + "</" + tag_name + ">";
					} else if ("industry_sector".equalsIgnoreCase(tag_name)) {
						xml_str = xml_str + "<" + tag_name + ">" + industry_sector + "</" + tag_name + ">";
					} else if ("industry_marco".equalsIgnoreCase(tag_name)) {
						xml_str = xml_str + "<" + tag_name + ">" + industry_marco + "</" + tag_name + ">";
					} else if ("industry_micro".equalsIgnoreCase(tag_name)) {
						xml_str = xml_str + "<" + tag_name + ">" + industry_micro + "</" + tag_name + ">";
					} else if ("bvr".equalsIgnoreCase(tag_name)) {
						xml_str = xml_str + "<" + tag_name + ">" + bvr + "</" + tag_name + ">";
					} else if ("eff_date_estba".equalsIgnoreCase(tag_name)) {
						xml_str = xml_str + "<" + tag_name + ">" + eff_date_estba + "</" + tag_name + ">";
					} else if ("poa".equalsIgnoreCase(tag_name)) {
						xml_str = xml_str + "<" + tag_name + ">" + poa + "</" + tag_name + ">";
					} else if ("cc_employer_status".equalsIgnoreCase(tag_name)) {
						xml_str = xml_str + "<" + tag_name + ">" + cc_employer_status + "</" + tag_name + ">";
					} else if ("tlc_issue_date".equalsIgnoreCase(tag_name)) {
						xml_str = xml_str + "<" + tag_name + ">" + eff_date_estba + "</" + tag_name + ">";
					} else if ("pl_employer_status".equalsIgnoreCase(tag_name)) {
						xml_str = xml_str + "<" + tag_name + ">" + pl_employer_status + "</" + tag_name + ">";
					} else if ("marketing_code".equalsIgnoreCase(tag_name)) {
						xml_str = xml_str + "<" + tag_name + ">" + marketing_code + "</" + tag_name + ">";
					} else if("eff_lob".equalsIgnoreCase(tag_name)){
						xml_str = xml_str+ "<"+tag_name+">"+effLOB+"</"+ tag_name+">";
					} else if("current_emp_catogery".equalsIgnoreCase(tag_name)){
						xml_str = xml_str+ "<"+tag_name+">"+current_emp_catogery+"</"+ tag_name+">";
					}
					CreditCard.mLogger.info("RLOS COMMON"+	" after adding cmplx_CompanyDetails_cmplx_CompanyGrid+ "+ xml_str);
					int_xml.put(parent_tag, xml_str);
				} else if (("auth_sig_sole_emp".equalsIgnoreCase(tag_name) || "shareholding_perc".equalsIgnoreCase(tag_name))&& "Self Employed".equalsIgnoreCase(emp_type)) {
					CreditCard.mLogger.info("RLOS COMMON"+ " iNSIDE channelcode+ ");
					String auth_sig_sole_emp = "";
					String shareholding_perc = "";
					int Authsign_row_count = formObject
					.getLVWRowCount("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails");
					if (Authsign_row_count != 0) {
						auth_sig_sole_emp = ("Yes".equalsIgnoreCase(formObject.getNGValue("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails",0, 10)) ? "Y" : "N"); // 0
						shareholding_perc = formObject.getNGValue("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails",0, 9); // 0

					}
					String xml_str = int_xml.get(parent_tag);
					if ("auth_sig_sole_emp".equalsIgnoreCase(tag_name)) {
						xml_str = xml_str + "<" + tag_name + ">" + auth_sig_sole_emp + "</" + tag_name + ">";
					} else if ("shareholding_perc".equalsIgnoreCase(tag_name)) {
						xml_str = xml_str + "<" + tag_name + ">" + shareholding_perc + "</" + tag_name + ">";
					}

					CreditCard.mLogger.info("RLOS COMMON"+	" after adding shareholding_perc+ " + xml_str);
					int_xml.put(parent_tag, xml_str);

				}
				
				else if("current_emp_catogery".equalsIgnoreCase(tag_name))
				{
					CreditCard.mLogger.info(" iNSIDE current_emp_catogery+ ");

					String current_emp_catogery=formObject.getNGValue("cmplx_EmploymentDetails_CurrEmployer");
					CreditCard.mLogger.info(" value of current_emp_catogery "+current_emp_catogery);
					String squerycurremp="select Description from NG_MASTER_EmployerCategory_PL where isActive='Y' and code='"+current_emp_catogery+"'";
					CreditCard.mLogger.info(" query is "+squerycurremp);
					List<List<String>> squerycurrempXML=formObject.getNGDataFromDataCache(squerycurremp);
					CreditCard.mLogger.info(" query is "+squerycurrempXML);

					if(!squerycurrempXML.isEmpty() && squerycurrempXML.get(0).get(0)!=null)
					{								
						CreditCard.mLogger.info(" iNSIDE squerycurrempXML+ "+squerycurrempXML.get(0).get(0));
						current_emp_catogery=squerycurrempXML.get(0).get(0);								
					}


					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str+ "<"+tag_name+">"+current_emp_catogery+"</"+ tag_name+">";

					CreditCard.mLogger.info(" after adding current_emp_catogery+ "+xml_str);
					int_xml.put(parent_tag, xml_str);

				}
				
				else if ("external_blacklist_flag".equalsIgnoreCase(tag_name)	|| "external_blacklist_date".equalsIgnoreCase(tag_name)	|| "external_blacklist_code".equalsIgnoreCase(tag_name)) {
					CreditCard.mLogger.info("RLOS COMMON"+ " iNSIDE channelcode+ ");
					String ParentWI_Name = formObject.getNGValue("Parent_WIName");
					String squeryBlacklist = "select BlacklistFlag,BlacklistDate,BlacklistReasonCode from ng_rlos_cif_detail with (nolock) where (cif_wi_name='" + ParentWI_Name + "' or cif_wi_name='" + formObject.getWFWorkitemName() + "') and cif_searchType = 'External'";
					CreditCard.mLogger.info("RLOS COMMON"+ " iNSIDE channelcode+ " + squeryBlacklist);
					List<List<String>> Blacklist = formObject
					.getNGDataFromDataCache(squeryBlacklist);
					String External_blacklist_date = "";
					String External_blacklist_code = "";

					if (Blacklist != null && Blacklist.size() > 0) {
						External_blacklist_date = Blacklist.get(0).get(1);
						External_blacklist_code = Blacklist.get(0).get(2);
					}
					String xml_str = int_xml.get(parent_tag);
					if ("external_blacklist_flag".equalsIgnoreCase(tag_name)) {
						xml_str = xml_str + "<" + tag_name + ">I</" + tag_name + ">";
					} else if ("external_blacklist_date".equalsIgnoreCase(tag_name)) {
						xml_str = xml_str + "<" + tag_name + ">" + External_blacklist_date + "</" + tag_name + ">";
					} else if ("external_blacklist_code".equalsIgnoreCase(tag_name)) {
						xml_str = xml_str + "<" + tag_name + ">" + External_blacklist_code + "</" + tag_name + ">";
					}

					CreditCard.mLogger.info("RLOS COMMON"+" after adding internal_blacklist+ " + xml_str);
					int_xml.put(parent_tag, xml_str);

				} else if ("ApplicationDetails".equalsIgnoreCase(tag_name)) {
					CreditCard.mLogger.info("inside 1st if"+ "inside DECTECH req1");

					CreditCard.mLogger.info("inside 1st if"+ "inside customer update req2");
					String xml_str = int_xml.get(parent_tag);
					CreditCard.mLogger.info("RLOS COMMON"+ " before adding product+ " + xml_str);
					xml_str = xml_str + CC_Comn.getProduct_details();
					CreditCard.mLogger.info("RLOS COMMON"+ " after adding product+ " + xml_str);
					int_xml.put(parent_tag, xml_str);

				} else if ("cust_name".equalsIgnoreCase(tag_name)) {
					if (int_xml.containsKey(parent_tag)) {
						String first_name = formObject.getNGValue("cmplx_Customer_FIrstNAme");
						String middle_name = formObject.getNGValue("cmplx_Customer_MiddleName");
						String last_name = formObject.getNGValue("cmplx_Customer_LAstNAme");

						String full_name = first_name + " " + middle_name + "" + last_name;

						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<" + tag_name + ">" + full_name + "</" + tag_name + ">";

						CreditCard.mLogger.info("RLOS COMMON"+" after adding confirmedinjob+ " + xml_str);
						int_xml.put(parent_tag, xml_str);

					}
				}

				else if ("ref_phone_no".equalsIgnoreCase(tag_name)) {
					if (int_xml.containsKey(parent_tag)) {
						CreditCard.mLogger.info("RLOS COMMON"+ " INSIDE ref_phone_no+ ");
						int count = formObject.getLVWRowCount("cmplx_RefDetails_cmplx_Gr_ReferenceDetails");
						String ref_phone_no = "";
						String ref_relationship = "";
						CreditCard.mLogger.info("RLOS COMMON"+ " INSIDE ref_phone_no+ " + count);
						if (count != 0) {
							ref_phone_no = formObject.getNGValue("cmplx_RefDetails_cmplx_Gr_ReferenceDetails", 0, 4);
							ref_relationship = formObject.getNGValue("cmplx_RefDetails_cmplx_Gr_ReferenceDetails", 0, 2);
							CreditCard.mLogger.info("RLOS COMMON"+" after adding ref_phone_no+ " + ref_phone_no);
							CreditCard.mLogger.info("RLOS COMMON"+" after adding ref_relationship+ "+ ref_relationship);
						}

						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<" + tag_name + ">" + ref_phone_no + "</" + tag_name + "><ref_relationship>" + ref_relationship + "</ref_relationship>";

						CreditCard.mLogger.info("RLOS COMMON"+" after adding confirmedinjob+ " + xml_str);
						int_xml.put(parent_tag, xml_str);

					}
				}

				else if ("confirmed_in_job".equalsIgnoreCase(tag_name)) {
					if (int_xml.containsKey(parent_tag)) {
						String confirmedinjob = formObject.getNGValue("cmplx_EmploymentDetails_JobConfirmed");

						if (confirmedinjob != null) {
							if ("true".equalsIgnoreCase(confirmedinjob)) 
							{
								confirmedinjob = "Y";
							} 
							else 
							{
								confirmedinjob = "N";
							}

							String xml_str = int_xml.get(parent_tag);
							xml_str = xml_str + "<" + tag_name + ">" + confirmedinjob+ "</" + tag_name + ">";

							CreditCard.mLogger.info("RLOS COMMON"+" after adding confirmedinjob+ " + xml_str);
							int_xml.put(parent_tag, xml_str);
						}
					}
				} else if ("included_pl_aloc".equalsIgnoreCase(tag_name)) {
					if (int_xml.containsKey(parent_tag)) {
						String included_pl_aloc = formObject.getNGValue("cmplx_EmploymentDetails_IncInPL");

						if (included_pl_aloc != null) {
							if ("true".equalsIgnoreCase(included_pl_aloc)) 
							{
								included_pl_aloc = "Y";
							} 
							else 
							{
								included_pl_aloc = "N";
							}

							String xml_str = int_xml.get(parent_tag);
							xml_str = xml_str + "<" + tag_name + ">" + included_pl_aloc+ "</" + tag_name + ">";

							CreditCard.mLogger.info("RLOS COMMON"+" after adding included_pl_aloc+ " + xml_str);
							int_xml.put(parent_tag, xml_str);
						}
					}
				} else if ("included_cc_aloc".equalsIgnoreCase(tag_name)) {
					if (int_xml.containsKey(parent_tag)) {
						String included_cc_aloc = formObject.getNGValue("cmplx_EmploymentDetails_IncInCC");

						if (included_cc_aloc != null) {
							if ("true".equalsIgnoreCase(included_cc_aloc)) 
							{
								included_cc_aloc = "Y";
							} else 
							{
								included_cc_aloc = "N";
							}

							String xml_str = int_xml.get(parent_tag);
							xml_str = xml_str + "<" + tag_name + ">" + included_cc_aloc+ "</" + tag_name + ">";

							CreditCard.mLogger.info("RLOS COMMON"+" after adding cmplx_EmploymentDetails_IncInCC+ "+ xml_str);
							int_xml.put(parent_tag, xml_str);
						}
					}
				} else if ("vip_flag".equalsIgnoreCase(tag_name)) {
					if (int_xml.containsKey(parent_tag)) {
						String vip_flag = formObject.getNGValue("cmplx_Customer_VIPFlag");

						if ("true".equalsIgnoreCase(vip_flag)) {
							vip_flag = "Y";
						} else {
							vip_flag = "N";
						}

						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<" + tag_name + ">" + vip_flag + "</" + tag_name + ">";

						CreditCard.mLogger.info("RLOS COMMON"+" after adding cmplx_Customer_VIPFlag+ " + xml_str);
						int_xml.put(parent_tag, xml_str);
					}
				} else if ("standing_instruction".equalsIgnoreCase(tag_name)) {
					CreditCard.mLogger.info("RLOS COMMON"+
					" iNSIDE standing_instruction+ ");
					String squerynoc = "SELECT count(*) FROM ng_rlos_FinancialSummary_SiDtls with (nolock) WHERE Child_wi='" + formObject.getWFWorkitemName() + "'";
					List<List<String>> NOC = formObject
					.getNGDataFromDataCache(squerynoc);
					CreditCard.mLogger.info("RLOS COMMON"+
							" after adding cmplx_Customer_VIPFlag+ " + squerynoc);
					String standing_instruction = "";
					standing_instruction = NOC.get(0).get(0);
					if (NOC != null && NOC.size() > 0) {
						String xml_str = int_xml.get(parent_tag);
						if ("0".equalsIgnoreCase(standing_instruction)) {
							standing_instruction = "N";
						} else {
							standing_instruction = "Y";
						}

						xml_str = xml_str + "<" + tag_name + ">" + standing_instruction + "</" + tag_name + ">";

						CreditCard.mLogger.info("RLOS COMMON"+" after adding standing_instruction+ " + xml_str);
						int_xml.put(parent_tag, xml_str);
					}
				}
				else if("aggregate_exposed".equalsIgnoreCase(tag_name)){
					CreditCard.mLogger.info(" Inside Aggregate ");
					if(int_xml.containsKey(parent_tag))
					{
						formObject.saveFragment("EligibilityAndProductInformation");
						String aeQuery = " select (select isNull((Sum(convert(float,replace([TotalOutstandingAmt],'NA','0')))),0) as TotalOutstandingAmt from ng_RLOS_CUSTEXPOSE_loanDetails with (nolock) where Consider_For_Obligations='true' and LoanStat in ('A','ACTIVE') and child_wi ='"+formObject.getWFWorkitemName()+"') + (select isNull((Sum(convert(float,replace([TotalOutstandingAmt],'NA','0')))),0) as TotalOutstandingAmt from ng_RLOS_CUSTEXPOSE_loanDetails with (nolock) where Consider_For_Obligations='true' and LoanStat ='Pipeline' and child_wi ='"+formObject.getWFWorkitemName()+"') +(select isNull((Sum(convert(float,replace([OutstandingAmt],'NA','0')))),0)  from ng_RLOS_CUSTEXPOSE_CardDetails with (nolock) where  Consider_For_Obligations='true' and CardStatus in ('A','ACTIVE') and child_wi  ='"+formObject.getWFWorkitemName()+"' and SchemeCardProd like '%LOC%') + (select isNull((Sum(convert(float,replace([CreditLimit],'NA','0')))),0) as OutstandingAmt from ng_RLOS_CUSTEXPOSE_CardDetails with (nolock) where  Consider_For_Obligations='true' and CardStatus in ('A','ACTIVE') and child_wi  ='"+formObject.getWFWorkitemName()+"' and SchemeCardProd not like '%LOC%')+( select isNull((Sum(convert(float,replace([final_limit],'NA','0')))),0) from ng_rlos_EligAndProdInfo with (nolock) where wi_name ='"+formObject.getWFWorkitemName()+"') as aggregateExposure";
						CreditCard.mLogger.info(" Inside Aggregate "+aeQuery);

						List<List<String>> aggregate_exposed = formObject.getNGDataFromDataCache(aeQuery);


					
						String aggr_expo=aggregate_exposed.get(0).get(0);
						double aggreg=Double.parseDouble(aggr_expo);
						aggr_expo=String.format("%.2f", aggreg);

						formObject.setNGValue("cmplx_Liability_New_AggrExposure", aggr_expo);//changed by akshay on 25/9/17 as per point 2 of problem sheet
						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<"+tag_name+">"+aggr_expo+"</"+ tag_name+">";

						CreditCard.mLogger.info(" after adding aggregate_exposed+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}                                        
				}
				
				else if ("accomodation_provided".equalsIgnoreCase(tag_name)) {
					if (int_xml.containsKey(parent_tag)) {
						String accomodation_provided = formObject.getNGValue("cmplx_IncomeDetails_Accomodation")!=null && "yes".equalsIgnoreCase(formObject.getNGValue("cmplx_IncomeDetails_Accomodation"))?"Y":"N";

						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<" + tag_name + ">"+ accomodation_provided + "</" + tag_name + ">";

						CreditCard.mLogger.info("RLOS COMMON"+" after adding confirmedinjob+ " + xml_str);
						int_xml.put(parent_tag, xml_str);

					}
				} else if ("AccountDetails".equalsIgnoreCase(tag_name)) {
					
					if (int_xml.containsKey(parent_tag)) {
						String xml_str = int_xml.get(parent_tag);
						CreditCard.mLogger.info("RLOS COMMON"+" before adding internal liability+ " + xml_str);
						xml_str = xml_str + CC_Comn.getInternalLiabDetails();
						CreditCard.mLogger.info("RLOS COMMON"+" after internal liability+ " + xml_str);
						int_xml.put(parent_tag, xml_str);
					}
						

				} else if ("InternalBureau".equalsIgnoreCase(tag_name)) {

					String xml_str = int_xml.get(parent_tag);
					CreditCard.mLogger.info("RLOS COMMON"+" before adding InternalBureauData+ " + xml_str);
					String temp = CC_Comn.InternalBureauData();
					if (!"".equalsIgnoreCase(temp)) {
						if (xml_str == null) {
							CreditCard.mLogger.info("RLOS COMMON"+ " before adding bhrabc"+ xml_str);
							xml_str = "";
						}
						xml_str = xml_str + temp;
						CreditCard.mLogger.info("RLOS COMMON"+" after InternalBureauData+ " + xml_str);
						int_xml.get(parent_tag);
						int_xml.put(parent_tag, xml_str);
					}

				} else if ("InternalBouncedCheques".equalsIgnoreCase(tag_name)) {

					String xml_str = int_xml.get(parent_tag);
					CreditCard.mLogger.info("RLOS COMMON"+" before adding InternalBouncedCheques+ " + xml_str);
					String temp = InternalBouncedCheques();
					if (!"".equalsIgnoreCase(temp)) {
						if (xml_str == null) {
							CreditCard.mLogger.info("RLOS COMMON"+ " before adding bhrabc"+ xml_str);
							xml_str = "";
						}
						xml_str = xml_str + temp;
						CreditCard.mLogger.info("RLOS COMMON"+" after InternalBouncedCheques+ " + xml_str);
						int_xml.put(parent_tag, xml_str);
					}

				} else if ("InternalBureauIndividualProducts".equalsIgnoreCase(tag_name)) {

					String xml_str = int_xml.get(parent_tag);
					CreditCard.mLogger.info("RLOS COMMON"+" before adding InternalBureauIndividualProducts+ "+ xml_str);
					String temp = InternalBureauIndividualProducts();
					if (!"".equalsIgnoreCase(temp)) {
						if (xml_str == null) {
							CreditCard.mLogger.info("RLOS COMMON"+ " before adding bhrabc"+ xml_str);
							xml_str = "";
						}
						xml_str = xml_str + temp;
						CreditCard.mLogger.info("RLOS COMMON"+" after InternalBureauIndividualProducts+ " + xml_str);
						int_xml.put(parent_tag, xml_str);
					}

				} else if ("InternalBureauPipelineProducts".equalsIgnoreCase(tag_name)) {

					String xml_str = int_xml.get(parent_tag);
					CreditCard.mLogger.info("RLOS COMMON"+" before adding InternalBureauPipelineProducts+ "+ xml_str);
					String temp = InternalBureauPipelineProducts();
					if (!"".equalsIgnoreCase(temp)) {
						if (xml_str == null) {
							CreditCard.mLogger.info("RLOS COMMON"+ " before adding bhrabc"+ xml_str);
							xml_str = "";
						}
						xml_str = xml_str + temp;
						CreditCard.mLogger.info("RLOS COMMON"+" after InternalBureauPipelineProducts+ " + xml_str);
						int_xml.put(parent_tag, xml_str);
					}

				} else if ("ExternalBureau".equalsIgnoreCase(tag_name)) {

					String xml_str = int_xml.get(parent_tag);
					CreditCard.mLogger.info("RLOS COMMON"+
							" before adding ExternalBureau+ " + xml_str);
					String temp = ExternalBureauData();
					if (!"".equalsIgnoreCase(temp)) {
						if (xml_str == null) {
							CreditCard.mLogger.info("RLOS COMMON"+ " before adding bhrabc"+ xml_str);
							xml_str = "";
						}
						xml_str = xml_str + temp;
						CreditCard.mLogger.info("RLOS COMMON"+ " after ExternalBureau+ " + xml_str);
						int_xml.put(parent_tag, xml_str);
					}

				} else if ("ExternalBouncedCheques".equalsIgnoreCase(tag_name)) {

					String xml_str = int_xml.get(parent_tag);
					CreditCard.mLogger.info("RLOS COMMON"+" before adding ExternalBouncedCheques+ " + xml_str);
					String temp = ExternalBouncedCheques();
					if (!"".equalsIgnoreCase(temp)) {
						if (xml_str == null) {
							CreditCard.mLogger.info("RLOS COMMON"+ " before adding bhrabc"+ xml_str);
							xml_str = "";
						}
						xml_str = xml_str + temp;
						CreditCard.mLogger.info("RLOS COMMON"+" after ExternalBouncedCheques+ " + xml_str);
						int_xml.put(parent_tag, xml_str);
					}
				} else if ("ExternalBureauIndividualProducts".equalsIgnoreCase(tag_name)) {

					String xml_str = int_xml.get(parent_tag);
					String temp = ExternalBureauIndividualProducts();
					CreditCard.mLogger.info("RLOS COMMON"+" value of temp to be adding temp+ " + temp);
					String Manual_add_Liab = ExternalBureauManualAddIndividualProducts();

					if ((!"".equalsIgnoreCase(temp))|| (!"".equalsIgnoreCase(Manual_add_Liab))) {
						if (xml_str == null) {
							CreditCard.mLogger.info("RLOS COMMON"+ " before adding bhrabc"+ xml_str);
							xml_str = "";
						}
						xml_str = xml_str + temp + Manual_add_Liab;
						CreditCard.mLogger.info("RLOS COMMON"+" after ExternalBureauIndividualProducts+ " + xml_str);
						int_xml.put(parent_tag, xml_str);
					}
				} else if ("ExternalBureauPipelineProducts".equalsIgnoreCase(tag_name)) {

					String xml_str = int_xml.get(parent_tag);
					CreditCard.mLogger.info("RLOS COMMON"+" before adding ExternalBureauPipelineProducts+ "+ xml_str);
					String temp = ExternalBureauPipelineProducts();
					if (!"".equalsIgnoreCase(temp)) {
						if (xml_str == null) {
							CreditCard.mLogger.info("RLOS COMMON"+ " before adding bhrabc"+ xml_str);
							xml_str = "";
						}
						xml_str = xml_str + temp;
						CreditCard.mLogger.info("RLOS COMMON"+" after ExternalBureauPipelineProducts+ " + xml_str);
						int_xml.put(parent_tag, xml_str);
					}
				}
				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
				}
			}
		}
		catch(Exception e){
			CreditCard.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + "+e.getMessage());
			CreditCard.mLogger.info("CC_INtegration Exception in CARD services custom function:  "+CC_Common.printException(e));
		}
		return int_xml;
	}
	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : Cheque book eligibility      

	 ***********************************************************************************  */
	private Map<String, String> CHEQUE_BOOK_ELIGIBILITY_Custom(List<List<String>> DB_List,FormReference formObject,String callName) {

		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();

		try{
			for (List<String> mylist : DB_List) {

				for (int i = 0; i < 8; i++) {

					CreditCard.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");
				if ("RecipientAddress".equalsIgnoreCase(tag_name)) {
					int add_len = formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
					String add_res_val = "";
					String xml_str = int_xml.get(parent_tag);
					if (add_len > 0) {
						for (int i = 0; i < add_len; i++) {
							CreditCard.mLogger.info("selecting Emirates of residence: "+formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i, 0));
							if ("Home".equalsIgnoreCase(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0))) {formObject.setNGValue("cmplx_Customer_EmirateOfResidence",formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i, 6));add_res_val = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 1)+ " "		+formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i, 2)+ formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i, 3)+ formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i, 4)+ formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i, 5)+ formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i, 6);
							}
						}
						xml_str = xml_str + "<" + tag_name + ">" + add_res_val + "</"+ tag_name + ">";

						CreditCard.mLogger.info("RLOS COMMON"+ " after adding res_flag+ "+ xml_str);
						int_xml.put(parent_tag, xml_str);
					}
				}
				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
				}
			}
		}
		catch(Exception e){
			CreditCard.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + "+e.getMessage());
			CreditCard.mLogger.info("CC_INtegration Exception in CARD services custom function:  "+CC_Common.printException(e));
		}
		return int_xml;
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Generate Input XML for DB INPUT

	 ***********************************************************************************  */
	private Map<String, String> GenDefault_Input_DB(Map<String, String> int_xml, Map<String, String> recordFileMap,FormReference formObject,String callName){

		String Call_name =  recordFileMap.get("Call_name");
		String form_control =  recordFileMap.get("form_control");
		String parent_tag =  recordFileMap.get("parent_tag_name");
		String tag_name =  recordFileMap.get("xmltag_name");
		String is_repetitive =  recordFileMap.get("is_repetitive");
		String default_val =  recordFileMap.get("default_val");
		String data_format12 =  recordFileMap.get("data_format");

		String form_control_val = "";
		java.util.Date startDate;

		if (parent_tag != null && !"".equalsIgnoreCase(parent_tag)) {
			CreditCard.mLogger.info("inside 1st if"+"inside 1st if");
			if (int_xml.containsKey(parent_tag)) {
				CreditCard.mLogger.info("inside 1st if"+"inside 2nd if");
				String xml_str = int_xml.get(parent_tag);
				CreditCard.mLogger.info("inside 1st if"+"inside 2nd if xml string"+ xml_str);
				if ("Y".equalsIgnoreCase(is_repetitive)&& int_xml.containsKey(tag_name)) {
					CreditCard.mLogger.info("inside 1st if"+"inside 3rd if xml string");
					xml_str = int_xml.get(tag_name) + "</"+ tag_name + ">" + "<"+ tag_name + ">";
					CreditCard.mLogger.info("CC_Common"+"value after adding "+ Call_name + ": "+ xml_str);
					CreditCard.mLogger.info("inside 1st if"+"inside 3rd if xml string xml string"+ xml_str);
					int_xml.remove(tag_name);
					int_xml.put(tag_name, xml_str);
					CreditCard.mLogger.info("inside 1st if"+"inside 3rd if xml string xml string int_xml");
				} else {
					CreditCard.mLogger.info("inside else of parent tag"+"value after adding "+ Call_name+ ": "+ xml_str+ " form_control name: "+ form_control);
					CreditCard.mLogger.info(""+"valuie of form control: "+ formObject.getNGValue(form_control));
					if ("".equalsIgnoreCase(form_control.trim())&& "".equalsIgnoreCase(default_val.trim())) {
						CreditCard.mLogger.info("inside 1st if"+"inside");
						xml_str = xml_str + "<" + tag_name+ ">" + "</" + tag_name+ ">";
						CreditCard.mLogger.info("added by xml"+ "xml_str"+ xml_str);
					} else if (!(formObject.getNGValue(form_control) == null|| "".equalsIgnoreCase(formObject.getNGValue(form_control).trim()) || "null".equalsIgnoreCase(formObject.getNGValue(form_control)))) {
						CreditCard.mLogger.info("inside else of parent tag 1"+"form_control_val"+ form_control_val);
						if (fin_call_name.toUpperCase().contains(callName.toUpperCase())) {
							form_control_val = formObject.getNGValue(form_control).toUpperCase();
						} else
							form_control_val = formObject.getNGValue(form_control);

						if (!"text".equalsIgnoreCase(data_format12)) {
							String[] format_arr = data_format12.split(":");
							String format_name = format_arr[0];
							String format_type = format_arr[1];
							CreditCard.mLogger.info(""+"format_name"+ format_name);
							CreditCard.mLogger.info(""+"format_type"+ format_type);

							if ("date".equalsIgnoreCase(format_name)) {
								DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
								DateFormat df_new = new SimpleDateFormat(format_type);

								try {
									startDate = df.parse(form_control_val);
									form_control_val = df_new.format(startDate);
									CreditCard.mLogger.info("RLOSCommon#Create Input"+" date conversion: final Output: "+ form_control_val+ " requested format: "+ format_type);

								} catch (ParseException e) {
									CreditCard.mLogger.info("RLOSCommon#Create Input"+" Error while format conversion: "+ e.getMessage());
									//e.printStackTrace();
								} catch (Exception e) {
									CreditCard.mLogger.info("RLOSCommon#Create Input"+" Error while format conversion: "+ e.getMessage());
									//e.printStackTrace();
								}
							} else if ("number".equalsIgnoreCase(format_name)) {
								if (form_control_val.contains(",")) {
									form_control_val = form_control_val.replace(",","");
								}

							}
						}
						CreditCard.mLogger.info("inside else of parent tag"+"form_control_val"+ form_control_val);
						xml_str = xml_str + "<" + tag_name+ ">" + form_control_val+ "</" + tag_name + ">";
						CreditCard.mLogger.info("inside else of parent tag xml_str"+"xml_str" + xml_str);
					}

					else if (default_val == null || "".equalsIgnoreCase(default_val.trim())) {
						CreditCard.mLogger.info("#RLOS Common GenerateXML IF part"+"no value found for tag name: "+ tag_name);
					} else {
						CreditCard.mLogger.info("#RLOS Common GenerateXML inside set default value"+"");
						form_control_val = default_val;

						CreditCard.mLogger.info("#RLOS Common GenerateXML inside set default value"+"form_control_val"+ form_control_val);
						xml_str = xml_str + "<" + tag_name+ ">" + form_control_val+ "</" + tag_name + ">";
						CreditCard.mLogger.info("#RLOS Common GenerateXML inside else of parent tag form_control_val xml_str1"+"xml_str" + xml_str);

					}

					if ("DocumentRefNumber".equalsIgnoreCase(tag_name) && "Document".equalsIgnoreCase(parent_tag) && "".equalsIgnoreCase(form_control_val.trim())) {
						if (xml_str.contains("</Document>")) {
							xml_str = xml_str.substring(0,xml_str.lastIndexOf("</Document>"));
							int_xml.put(parent_tag,xml_str);
						} else
							int_xml.remove(parent_tag);
					} else if ("DocRefNum".equalsIgnoreCase(tag_name) && parent_tag.equalsIgnoreCase("DocDetails") && "".equalsIgnoreCase(form_control_val)) {
						if (xml_str.contains("</DocDetails>")) {
							xml_str = xml_str.substring(0,xml_str.lastIndexOf("</DocDetails>"));
							int_xml.put(parent_tag,xml_str);
						} else
							int_xml.remove(parent_tag);
					} else if ("PhnLocalCode".equalsIgnoreCase(tag_name) && "PhnDetails".equalsIgnoreCase(parent_tag) && "".equalsIgnoreCase(form_control_val)) {
						if (xml_str.contains("</PhnDetails>")) {
							xml_str = xml_str.substring(0,xml_str.lastIndexOf("</PhnDetails>"));
							int_xml.put(parent_tag,xml_str);
						} else
							int_xml.remove(parent_tag);
					} else if ("Email".equalsIgnoreCase(tag_name) && "EmlDet".equalsIgnoreCase(parent_tag) && "".equalsIgnoreCase(form_control_val)) {
						if (xml_str.contains("</EmlDet>")) {
							xml_str = xml_str.substring(0,xml_str.lastIndexOf("</EmlDet>"));
							int_xml.put(parent_tag,xml_str);
						} else
							int_xml.remove(parent_tag);
					} else if ("DocNo".equalsIgnoreCase(tag_name) && "DocDet".equalsIgnoreCase(parent_tag) && "".equalsIgnoreCase(form_control_val)) {
						if (xml_str.contains("</DocDet>")) {
							xml_str = xml_str.substring(0,xml_str.lastIndexOf("</DocDet>"));
							int_xml.put(parent_tag,xml_str);
						} else
							int_xml.remove(parent_tag);
					}else if ("IncomeAmount".equalsIgnoreCase(tag_name) && "OtherIncomeDetails".equalsIgnoreCase(parent_tag) && "".equalsIgnoreCase(form_control_val)) {
						if (xml_str.contains("</OtherIncomeDetails>")) {
							xml_str = xml_str.substring(0,xml_str.lastIndexOf("</OtherIncomeDetails>"));
							int_xml.put(parent_tag,xml_str);
						} else
							int_xml.remove(parent_tag);
					}
					else {
						int_xml.put(parent_tag, xml_str);
					}
					CreditCard.mLogger.info("else of generatexml"+"RLOSCommon" + "inside else"+ xml_str);
				}

			} else {
				String new_xml_str = "";
				CreditCard.mLogger.info("inside else of parent tag main 2"+"value after adding " + Call_name+ ": " + new_xml_str+ " form_control name: "+ form_control);
				CreditCard.mLogger.info(""+"valuie of form control: "+ formObject.getNGValue(form_control));
				if (!(formObject.getNGValue(form_control) == null|| "".equalsIgnoreCase(formObject.getNGValue(form_control).trim()) || "null".equalsIgnoreCase(formObject.getNGValue(form_control)))) {
					CreditCard.mLogger.info("inside else of parent tag 1"+"form_control_val"+ form_control_val);
					if (fin_call_name.toUpperCase().contains(callName.toUpperCase())) {
						form_control_val = formObject.getNGValue(form_control).toUpperCase();
					} else
						form_control_val = formObject.getNGValue(form_control);
					if (!"text".equalsIgnoreCase(data_format12)) {
						String[] format_arr = data_format12.split(":");
						String format_name = format_arr[0];
						String format_type = format_arr[1];
						if ("date".equalsIgnoreCase(format_name)) {
							DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
							DateFormat df_new = new SimpleDateFormat(format_type);
							// java.util.Date startDate;
							try {
								startDate = df.parse(form_control_val);
								form_control_val = df_new.format(startDate);
								CreditCard.mLogger.info("RLOSCommon#Create Input"+	" date conversion: final Output: "+ form_control_val+" requested format: "+ format_type);

							} catch (ParseException e) {
								CreditCard.mLogger.info("RLOSCommon#Create Input"+	" Error while format conversion: "+ e.getMessage());
								//e.printStackTrace();
							} catch (Exception e) {
								CreditCard.mLogger.info("RLOSCommon#Create Input"+	" Error while format conversion: "+ e.getMessage());
								//e.printStackTrace();
							}
						}
						else if ("number".equalsIgnoreCase(format_name)) {
							if (form_control_val.contains(",")) {
								form_control_val = form_control_val.replace(",", "");
							}
						}
					}
					CreditCard.mLogger.info("inside else of parent tag"+"form_control_val"+ form_control_val);
					new_xml_str = new_xml_str + "<"+ tag_name + ">"+ form_control_val + "</"+ tag_name + ">";
					CreditCard.mLogger.info("inside else of parent tag xml_str"+"new_xml_str"+ new_xml_str);
				}

				else if (default_val == null || "".equalsIgnoreCase(default_val.trim())) {
					if (int_xml.containsKey(parent_tag) || "Y".equalsIgnoreCase(is_repetitive)) {
						new_xml_str = new_xml_str + "<"+ tag_name + ">" + "</"+ tag_name + ">";
					}
					CreditCard.mLogger.info("#RLOS Common GenerateXML Inside Else Part"+"no value found for tag name: "+ tag_name);
				} else {
					CreditCard.mLogger.info("#RLOS Common GenerateXML inside set default value"+"");
					form_control_val = default_val;
					CreditCard.mLogger.info("#RLOS Common GenerateXML inside set default value"+"form_control_val"+ form_control_val);
					new_xml_str = new_xml_str + "<"+ tag_name + ">"+ form_control_val + "</"+ tag_name + ">";
					CreditCard.mLogger.info("#RLOS Common GenerateXML inside else of parent tag form_control_val xml_str1"+"xml_str" + new_xml_str);
				}
				int_xml.put(parent_tag, new_xml_str);
				CreditCard.mLogger.info("else of generatexml"+"RLOSCommon" + "inside else"+ new_xml_str);
			}
		}
		return int_xml;
	}
	public String getReference_details(){
		CreditCard.mLogger.info("RLOSCommon java file"+ "inside getCustAddress_details : ");
		String  ref_xml_str ="";
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			int ref_row_count = formObject.getLVWRowCount("cmplx_RefDetails_cmplx_Gr_ReferenceDetails");
			CreditCard.mLogger.info("RLOSCommon java file"+ "inside getReference_details row_count+ : "+ref_row_count);

			for (int i = 0; i<ref_row_count;i++){
				String ref_name = formObject.getNGValue("cmplx_RefDetails_cmplx_Gr_ReferenceDetails", i, 0); //0
				String ref_phone=formObject.getNGValue("cmplx_RefDetails_cmplx_Gr_ReferenceDetails", i, 1);//1
				//String ref_relation=formObject.getNGValue("cmplx_RefDetails_cmplx_Gr_ReferenceDetails", i, 2);//2
				ref_xml_str = ref_xml_str + "<ReferenceDetails><PersonName>"+ref_name+"</PersonName>";
				ref_xml_str = ref_xml_str + "<PhnDet><PhoneType>OFFCPH1</PhoneType><PhnCountryCode>00971</PhnCountryCode><CityCode></CityCode><PhnLocalCode>00971</PhnLocalCode>";
				ref_xml_str = ref_xml_str +"<PhoneNumber>"+ref_phone+"</PhoneNumber><ExtensionNumber></ExtensionNumber><PhnPrefFlag>N</PhnPrefFlag></PhnDet></ReferenceDetails>";
			}
			CreditCard.mLogger.info("RLOSCommon"+ "reference tag Cration: "+ ref_xml_str);
			return ref_xml_str;
		}
		catch(Exception e){
			CreditCard.mLogger.info("PLCommon getReference_details method"+ "Exception Occure in getReference_details"+CC_Common.printException(e));
			return ref_xml_str;
		}	
	}	
	public  String Clean_Xml(String InputXml){
		String Output_Xml="";

		try{
			//below change by saurabh on 18th Dec
			if(InputXml.indexOf("&")>-1){
				InputXml=InputXml.replaceAll("&", "ampr");
			}
			Document doc = CC_Common.getDocument(InputXml);
			removeEmptyNodes(doc);
			DOMSource domSource = new DOMSource(doc);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
			Output_Xml = writer.toString().substring(38);
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
