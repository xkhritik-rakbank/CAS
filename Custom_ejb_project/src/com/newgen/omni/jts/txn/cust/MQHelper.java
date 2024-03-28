//-------------------------------------------------------------------------
//				NEWGEN SOFTWARE TECHNOLOGIES LIMITED
//		Group						: AP-2
//		Product / Project			: RAKBANK-SRM
//		Module						: WFCustomBean
//		File Name					: MQHelper.java
//		Author						: Amandeep
//		Date written (DD/MM/YYYY)	: 31/07/2014
//		Description					: MQ Integration Helper class.
//-------------------------------------------------------------------------
//					CHANGE HISTORY
//-------------------------------------------------------------------------
//	Date			 Change By	 	Change Description (Bug No. (If Any))
//
//-------------------------------------------------------------------------
//
// (21/05/2017)		Ankit/Angad     Added message.format = "MQSTR" before putting message on the queue.
//-------------------------------------------------------------------------
package com.newgen.omni.jts.txn.cust;

import nu.xom.*;
import com.ibm.mq.*;
import com.newgen.omni.jts.txn.*;
import com.newgen.omni.jts.cmgr.*;
import com.newgen.omni.jts.excp.*;
import com.newgen.omni.jts.util.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.text.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import java.sql.Timestamp;
import java.util.Random;

public class MQHelper  
{
	private static final String REQ_TYPE_NOT_SPECIFIED="REQ_TYPE_NOT_SPECIFIED";
	private static final String XML_NOT_SPECIFIED="XML_NOT_SPECIFIED";
	private String hostName;
	private String port;
	private String qmgrName;
	private String ReqChannelId;
	private String channel;
	private String qNameSource;
	private String qNameDest;
	private String lTimeInterval;
	private static final boolean consoleWrite = true;
	private MQMessage message;
	private MQQueueManager mqQueueManager;
	private MQQueue queue;
	
	//Fieldnames explicit to Balance Transfer and CCC
	private String cardStatus;
	private String availableBalance;
	private String availableCashBalance;
	private String requestAmount;
	private String overdueAmount;
	private String serviceType;
	private String serviceSubType;
	private String paymentType;
	private String requestFor;
	private String otherBankCode;
	private String otherBankCard;
	private String otherBankIBAN;
	private String beneficiaryName;
	private String marketingCode;
	private String blockAmount;
	private String cardExpiry;
	private String transactionType;
	private String remarks;
	private String debitAuthId;
	private String trnRqUID;
	private String approvalId;
	private String merchantCode;
	private String processingCode;
	private String freefield1;
	private String PaymentMode;
	private String OtherBankAcctId;
	public MQHelper(MQMessage message)
	{
		this.message = message;
	}
	public MQHelper()
	{
		
	}
	//FieldNames explicit to Account Opening
	private String CIFID;
	private String SRNumber;
	
	//Fieldnames explicit for TT processingCode
	private String accountNumber;
	private String creditCurCode;
	private String debitCurCode;
	//private String cifID = "";
	private String trAmount = "";
	private String trCurrency = "";
	
	//function added to set the BT/CCC fields from WFCustom
	public void setCardServiceEligibilityFields(String cardStatus, String availableBalance, String availableCashBalance, String requestAmount, String overdueAmount, String serviceType, String serviceSubType, String paymentType, String requestFor, String otherBankCode, String otherBankCard, String otherBankIBAN, String beneficiaryName, String marketingCode, String blockAmount, String transactionType, String cardExpiry,String remarks,String debitAuthId,String trnRqUID,String approvalId, String merchantCode, String processingCode,String freefield1, String PaymentMode, String OtherBankAcctId, String CIFID)
	{
		this.cardStatus = cardStatus;
		this.availableBalance = availableBalance;
		this.availableCashBalance = availableCashBalance;
		this.requestAmount = requestAmount;
		this.overdueAmount = overdueAmount;
		this.serviceType = serviceType;
		this.serviceSubType = serviceSubType;
		this.paymentType = paymentType;
		this.requestFor = requestFor;
		this.otherBankCode = otherBankCode;
		this.otherBankCard = otherBankCard;
		this.otherBankIBAN = otherBankIBAN;
		this.beneficiaryName = beneficiaryName;
		this.marketingCode = marketingCode;
		this.blockAmount = blockAmount;
		this.cardExpiry = cardExpiry;
		this.transactionType = transactionType;
		this.remarks = remarks;
		this.debitAuthId = debitAuthId;
		this.trnRqUID = trnRqUID;
		this.approvalId = approvalId;		
		this.merchantCode = merchantCode;
		this.processingCode = processingCode;
		this.freefield1 = freefield1;
		this.PaymentMode = PaymentMode;
		this.OtherBankAcctId = OtherBankAcctId;
		this.CIFID = CIFID;
	}
	
	public void setAccountNoFetchFields(String CIFID,String SRNo)
	{
		this.CIFID = CIFID;
		this.SRNumber = SRNo;
	}
	//function added to set Account Number for TT
	public void setAccountNoTTField(String accountNumber)
	{
		this.accountNumber = accountNumber;
	}
	//function added to set Exchange Rate Details field for TT
	public void setExchangeRateTTField(String creditCurCode, String debitCurCode,String cifID,String trAmount,String trCurrency)
	{
		this.creditCurCode = creditCurCode;
		this.debitCurCode = debitCurCode;
		this.CIFID = cifID;
		this.trAmount = trAmount;
		this.trCurrency = trCurrency;
	}
	public String getMQRequestXML(Connection connection, XMLParser xmlparser, XMLGenerator xmlgenerator, String strRequestType, String cardNumber, String cardType, String reasonCode) throws ParserConfigurationException, SAXException, IOException
	{
		ExtDBLog.writeLog("Inside getMQRequestXML");
		
		String strXMLFileName=getXMLFileName(strRequestType);
		ExtDBLog.writeLog("strXMLFileName" + strXMLFileName);
		if(strXMLFileName==null ||strXMLFileName.equals("") || strXMLFileName.equals(XML_NOT_SPECIFIED))	return XML_NOT_SPECIFIED;
		else if(strXMLFileName.equals(REQ_TYPE_NOT_SPECIFIED))	return REQ_TYPE_NOT_SPECIFIED;
		
		//InputStream is = this.getClass().getResourceAsStream(".\\RequestXMLs\\"+strXMLFileName);		
		InputStream is = this.getClass().getResourceAsStream(strXMLFileName);		
		InputStreamReader isr = new InputStreamReader(is);		
		BufferedReader br = new BufferedReader(isr);	
		StringBuilder builder = new StringBuilder();	
		String line=br.readLine();	
		while ((line = br.readLine()) != null) {			
			builder.append(line);
			builder.append("\n");
		}
	
		return getXML(builder.toString(), xmlparser, connection, xmlgenerator, cardNumber, cardType, reasonCode);
	}
	
	public String getXML(String xml, XMLParser xmlparser, Connection connection, XMLGenerator xmlgenerator, String cardNumber, String cardType, String reasonCode)throws ParserConfigurationException, SAXException, IOException 
	{
		//setMQDetails(connection,xmlparser,xmlgenerator);
		ExtDBLog.writeLog("inside getXML");
		Document doc = getDocument(xml);
		
		ArrayList derivedKeys = getKeys(doc, "%%");
		for(int i=0;i<derivedKeys.size();i++)
			ExtDBLog.writeLog((String)derivedKeys.get(i));
		xml = setderivedKeys(xml, derivedKeys, xmlparser, connection, xmlgenerator, cardNumber);	
		
		ArrayList directKeys = getKeys(doc, "##");
		for(int i=0;i<directKeys.size();i++)
			ExtDBLog.writeLog((String)directKeys.get(i));
		xml = setdirectKeys(xml, directKeys, xmlparser, cardNumber, cardType, reasonCode);		
		
		return xml;
	}
	
	private String getXMLFileName(String strRequestType)
	{
		String strXMLFileName="";
		ExtDBLog.writeLog(strRequestType);
		if (strRequestType==null || strRequestType.equals(""))	strXMLFileName=REQ_TYPE_NOT_SPECIFIED;
		else if(strRequestType.equalsIgnoreCase("CardDetails"))	strXMLFileName="CardDetails_Request.xml";
		else if(strRequestType.equalsIgnoreCase("CustomerDetails"))	strXMLFileName="EntityDetails_Request.xml";
		else if(strRequestType.equalsIgnoreCase("Redeem"))	strXMLFileName="CBR_Redeem_Request.xml";
		else if(strRequestType.equalsIgnoreCase("Adjust"))	strXMLFileName="CBR_Adjust_Request.xml";
		else if(strRequestType.equalsIgnoreCase("Forfeit"))	strXMLFileName="CBR_Forfeit_Request.xml";
		else if(strRequestType.equalsIgnoreCase("CardList"))	strXMLFileName="CardList_Request.xml";
		else if(strRequestType.equalsIgnoreCase("BOC_CardList_Debit"))	strXMLFileName="BOC_CardList_Request_Debit.xml";
		else if(strRequestType.equalsIgnoreCase("BOC_CardList_Credit"))	strXMLFileName="BOC_CardList_Request_Credit.xml";
		else if(strRequestType.equalsIgnoreCase("CardMaintenance"))	strXMLFileName="CardMaintenance_Request.xml";
		else if(strRequestType.equalsIgnoreCase("FundBlock"))	strXMLFileName="FundBlock_Request.xml";
		else if(strRequestType.equalsIgnoreCase("BalanceEnq"))	strXMLFileName="BalanceEnq_Request.xml";
		else if(strRequestType.equalsIgnoreCase("CardServiceEligibility"))	strXMLFileName="CardServiceEligibility_Request.xml";
		else if(strRequestType.equalsIgnoreCase("CardServiceEligibilityOB"))	strXMLFileName="CardServiceEligibilityOB_Request.xml";
		else if(strRequestType.equalsIgnoreCase("FetchAccounts"))	strXMLFileName="AO_FetchAccounts.xml";
		else if(strRequestType.equalsIgnoreCase("ENTITY_DETAILS"))	strXMLFileName="TT_EntityDetails_Request.xml";
		else if(strRequestType.equalsIgnoreCase("ACCOUNT_DETAILS"))	strXMLFileName="TT_GetAccountBalance_Request.xml";
		else if(strRequestType.equalsIgnoreCase("EXCHANGE_RATE_DETAILS"))	strXMLFileName="TT_ExchangeRateDetails.xml";
		else if(strRequestType.equalsIgnoreCase("SIGNATURE_DETAILS"))	strXMLFileName="SignatureDetails.xml";
		else if(strRequestType.equalsIgnoreCase("MEMOPAD_DETAILS"))	strXMLFileName="FetchMemoPadDetails.xml";
		else strXMLFileName=XML_NOT_SPECIFIED;
			
		return strXMLFileName;
	}
	
	private ArrayList getKeys(Node doc, String identifier) {
		// TODO Auto-generated method stub
		ArrayList keys = new ArrayList();
		NodeList list = doc.getChildNodes();
		
		int length = list.getLength();
		
		for (int i = 0; i < length; ++i) {
			Node item = list.item(i);
			//writeToConsole("HIII");
			if (item.getNodeType() == Node.ELEMENT_NODE) {
				keys.addAll(getKeys(item, identifier));
				
			} else if (item.getNodeType() == Node.TEXT_NODE) {
				String value = item.getTextContent().trim();
				if (value.startsWith(identifier) && value.endsWith(identifier)) {
					
					String key = value.substring(2,value.length()-2);
					keys.add(key);
				}
			}
		}
		
		return keys;
		
	}
	
	public static Document getDocument(String xml) throws ParserConfigurationException, SAXException, IOException  {

		// Step 1: create a DocumentBuilderFactory
		DocumentBuilderFactory dbf =
			DocumentBuilderFactory.newInstance();

		// Step 2: create a DocumentBuilder
		DocumentBuilder db = dbf.newDocumentBuilder();

		// Step 3: parse the input file to get a Document object
		Document doc = db.parse(new InputSource(new StringReader(xml)));
		return doc;
	}
	
	public void setMQDetails(Connection connection, XMLParser xmlparser,
			XMLGenerator xmlgenerator){
		String sQuery="select MQ_Properties from USR_0_SRM_MQ_SETTINGS";
		String sEngineName=xmlparser.getValueOf("EngineName");
		String sSessionId=xmlparser.getValueOf("DMSSessionId");
		String sInputXML = "<?xml version=\"1.0\"?>" +
		  "<APSelect_Input>" +
			"<Option>APSelect</Option>" +
			"<Query>" + sQuery + "</Query>" +
			"<EngineName>" + sEngineName + "</EngineName>" +
			"<SessionId>" + sSessionId + "</SessionId>" +
		  "</APSelect_Input>";
		XMLParser xmlparserMQ = new XMLParser();
		xmlparserMQ.setInputXML(sInputXML);
		String strOutXML=APSelect_execute(connection,xmlparserMQ,xmlgenerator) ;	
		
		String mqProperties = "";
		
		ExtDBLog.writeLog("setMQDetails retrived values xml from mq table :strOutXML " +strOutXML);
		
		if(strOutXML!=null && strOutXML.indexOf("<td>")>-1 && strOutXML.indexOf("<tr>")>-1 )
		{
			while(strOutXML.indexOf("<td>")>-1)
			{
				mqProperties=strOutXML.substring(strOutXML.indexOf("<td>")+"<td>".length(),strOutXML.indexOf("</td>"));
				strOutXML=strOutXML.substring(strOutXML.indexOf("</td>")+"</td>".length());
				ExtDBLog.writeLog("MQDetails : String "+mqProperties);
			}
		}
		
		String parameters [] = mqProperties.split(",");
		hostName=parameters[0].split("=")[1];
		port=parameters[1].split("=")[1];
		qmgrName=parameters[2].split("=")[1];
		ReqChannelId=parameters[3].split("=")[1];
		channel=parameters[4].split("=")[1];
		qNameSource=parameters[5].split("=")[1];
		qNameDest=parameters[6].split("=")[1];
		lTimeInterval=parameters[7].split("=")[1];
		
	}
	public String getCardType(Connection connection, XMLParser xmlparser, XMLGenerator xmlgenerator, String strCardNo,int fetchcase)
	{
		String sQuery="";
		if(fetchcase==1)
			sQuery="select case when Card_Type='Credit' or Card_Type='Credit Card' then 'CC' when  Card_Type='Debit' or Card_Type='Debit Card' then 'DC' when Card_Type='Prepaid' or Card_Type='Prepaid Card' then 'PC' else Card_Type end from USR_0_SRM_CARDS_BIN where Parameter = '"+strCardNo.substring(0,6)+"' and IsActive = 'Y'";
		else if(fetchcase==2)
			sQuery="select case when Card_Type='Credit' or Card_Type='Credit Card' then 'C' when  Card_Type='Debit' or Card_Type='Debit Card' then 'D' when Card_Type='Prepaid' or Card_Type='Prepaid Card' then 'P' else Card_Type end from USR_0_SRM_CARDS_BIN where Parameter = '"+strCardNo.substring(0,6)+"' and IsActive = 'Y'";
		else if(fetchcase==3)
			sQuery="select case when Card_Type='Credit' or Card_Type='Credit Card' then 'Credit' when  Card_Type='Debit' or Card_Type='Debit Card' then 'Debit' when Card_Type='Prepaid' or Card_Type='Prepaid Card' then 'Prepaid' else Card_Type end from USR_0_SRM_CARDS_BIN where Parameter = '"+strCardNo.substring(0,6)+"' and IsActive = 'Y'";
		else if(fetchcase==4)
			sQuery="select Merchant from USR_0_SRM_CARDS_BIN where Parameter = '"+strCardNo.substring(0,6)+"' and IsActive = 'Y'";
		else
			sQuery="select Card_Type from USR_0_SRM_CARDS_BIN where Parameter = '"+strCardNo.substring(0,6)+"' and IsActive = 'Y'";
		
		String sEngineName=xmlparser.getValueOf("EngineName");
		String sSessionId=xmlparser.getValueOf("DMSSessionId");
		String sInputXML = "<?xml version=\"1.0\"?>" +
		  "<APSelect_Input>" +
			"<Option>APSelect</Option>" +
			"<Query>" + sQuery + "</Query>" +
			"<EngineName>" + sEngineName + "</EngineName>" +
			"<SessionId>" + sSessionId + "</SessionId>" +
		  "</APSelect_Input>";
		  XMLParser xmlparserAPSelect = new XMLParser();
		xmlparserAPSelect.setInputXML(sInputXML);
		ExtDBLog.writeLog("sInputXML"+sInputXML);
		String strOutXML=APSelect_execute(connection,xmlparserAPSelect,xmlgenerator) ;	
		
		String cardType = "";
		
		ExtDBLog.writeLog("getting card type from cards bin  :strOutXML " +strOutXML);
		
		if(strOutXML!=null && strOutXML.indexOf("<td>")>-1 && strOutXML.indexOf("<tr>")>-1 )
		{
			while(strOutXML.indexOf("<td>")>-1)
			{
				cardType=strOutXML.substring(strOutXML.indexOf("<td>")+"<td>".length(),strOutXML.indexOf("</td>"));
				strOutXML=strOutXML.substring(strOutXML.indexOf("</td>")+"</td>".length());
				ExtDBLog.writeLog("Card Type "+cardType);
			}
		}
		return cardType;
		
	}
	public String setderivedKeys(String xml, ArrayList derivedKeys, XMLParser xmlparser, Connection connection, XMLGenerator xmlgenerator, String cardNumber)
	{
		message = new MQMessage();
		String Msg_id = genMessageID();
		java.util.Date d = new java.util.Date();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.mmm");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		String extra2 = sdf1.format(d)+"+04:00";
		String date = sdf2.format(d);

		Iterator itr = derivedKeys.iterator();
		
		while (itr.hasNext()) {
			String key = (String)itr.next();
			if(key.equals("messageId"))
				xml = xml.replaceAll("%%" + key + "%%", Msg_id);
			else if (key.equals("extra2"))
				xml = xml.replaceAll("%%" + key + "%%", extra2);
			else if (key.equals("card_type"))	
			{
				String strCardType=getCardType(connection, xmlparser, xmlgenerator, cardNumber,1);
				ExtDBLog.writeLog("Getting card type"+strCardType);
				xml=xml.replaceAll("%%" + key + "%%", strCardType);
			}
			else if (key.equals("card_type_short"))	
			{
				String strCardType=getCardType(connection, xmlparser, xmlgenerator, cardNumber,2);
				ExtDBLog.writeLog("Getting short card type"+strCardType);
				xml=xml.replaceAll("%%" + key + "%%", strCardType);
			}
			else if (key.equals("card_type_full"))	
			{
				String strCardType=getCardType(connection, xmlparser, xmlgenerator, cardNumber,3);
				ExtDBLog.writeLog("Getting full card type"+strCardType);
				xml=xml.replaceAll("%%" + key + "%%", strCardType);
			}
			else if (key.equals("sr_cbr_txndate") || key.equals("sr_cbr_valuedate")|| key.equals("date"))	
				xml = xml.replaceAll("%%" + key + "%%", date);
			else if (key.equals("RequestType"))
			{
				String strCardType=getCardType(connection, xmlparser, xmlgenerator, cardNumber,3);
				if(strCardType.equals("Credit"))
					xml = xml.replaceAll("%%" + key + "%%", "CardStatus");
				else if(strCardType.equals("Debit"))
					xml = xml.replaceAll("%%" + key + "%%", "AccountStatus");
				else 
					xml = xml.replaceAll("%%" + key + "%%", "ChangeStatus");
			}
			
		}
		return xml;
	}
	public String setdirectKeys(String xml, ArrayList directKeys, XMLParser xmlparser, String cardNumber, String cardType, String reasonCode)
	{
		Iterator itr = directKeys.iterator();
		
		while (itr.hasNext()) {
			String key = (String)itr.next();
			String value = xmlparser.getValueOf(key);
			value=value.toUpperCase();
			ExtDBLog.writeLog("key:"+key);
			ExtDBLog.writeLog("value:"+value);
			if(key.equals("CardNumber"))
				value = cardNumber;			
			else if(key.equals("CardType"))
				value = cardType;			
			else if(key.equals("ReasonCode"))
				value = reasonCode;
			else if(key.equals("CardStatus"))
				value = this.cardStatus;
			else if(key.equals("AvailableBalance"))
				value = this.availableBalance;
			else if(key.equals("AvailableCashBalance"))
				value = this.availableCashBalance;
			else if(key.equals("RequestAmount"))
				value = this.requestAmount;
			else if(key.equals("OverdueAmount"))
				value = this.overdueAmount;
			else if(key.equals("ServiceType"))
				value = this.serviceType;
			else if(key.equals("ServiceSubType"))
				value = this.serviceSubType;
			else if(key.equals("PaymentType"))
				value = this.paymentType;
			else if(key.equals("PaymentMode"))
				value = this.PaymentMode;
			else if(key.equals("RequestFor"))
				value = this.requestFor;
			else if(key.equals("OtherBankCode"))
				value = this.otherBankCode;
			else if(key.equals("OtherBankCard"))
				value = this.otherBankCard;
			else if(key.equals("OtherBankIBAN"))
				value = this.otherBankIBAN;
			else if(key.equals("BeneficiaryName"))
				value = this.beneficiaryName;
			else if(key.equals("MarketingCode"))
				value = this.marketingCode;
			else if(key.equals("Amount"))
				value = this.blockAmount;
			else if(key.equals("CardExpiryDate"))
				value = this.cardExpiry;
			else if(key.equals("TrnType"))
				value = this.transactionType;
			else if(key.equals("Remarks"))
				value = this.remarks;
			else if(key.equals("DebitAuthId"))
				value = this.debitAuthId;
			else if(key.equals("TrnRqUID"))
				value = this.trnRqUID;
			else if(key.equals("ApprovalId"))
				value = this.approvalId;
			else if(key.equals("CIFID"))
				value = this.CIFID;
			else if(key.equals("SRNumber"))
				value = this.SRNumber;
			else if(key.equals("MerchantCode"))
				value = this.merchantCode;		
			else if(key.equals("ProcessingCode"))
				value = this.processingCode;
			else if(key.equals("FreeField1"))
				value = this.freefield1;
			else if(key.equals("AccountNumber"))
				value = this.accountNumber;	
			else if(key.equals("DbtCurCode"))
				value = this.debitCurCode;	
			else if(key.equals("CrdCurCode"))
				value = this.creditCurCode;	
			else if(key.equals("TxnAmount"))
				value = this.trAmount;					
			else if(key.equals("TxnCurCode"))
				value = this.trCurrency;					
			xml = xml.replaceAll("##" + key + "##", value);
		}
		
		return xml;
	}
	public String APSelect_execute(Connection connection, XMLParser xmlparser,
			XMLGenerator xmlgenerator) {
		ResultSet rs = null;
		Statement stmt = null;
		ResultSetMetaData objRsmd = null;
		StringBuffer strBuff = new StringBuffer();
		StringBuffer outStrBuff = new StringBuffer();
		String sOut = "";

		strBuff.append(xmlgenerator.createOutputFile("APSelect"));
		try {
			String query = xmlparser.getValueOf("Query", "", false);

			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			objRsmd = rs.getMetaData();
			int iNoOfCols = objRsmd.getColumnCount();
			int iTotalRetrieved = 0;

			while (rs.next()) {
				outStrBuff.append("\n\t\t<tr>");
				for (int i = 1; i <= iNoOfCols; i++) {
					outStrBuff.append("\n\t\t\t<td>");
					outStrBuff.append(removeNULL(rs.getString(i)));
					outStrBuff.append("</td>");
				}
				outStrBuff.append("\n\t\t</tr>");
				iTotalRetrieved = iTotalRetrieved + 1;
			}
			sOut = outStrBuff.toString();
			sOut = "\n\t<Records>" + sOut + "\n\t</Records>";
			sOut = sOut + "\n\t<TotalRetrieved>" + iTotalRetrieved
					+ "</TotalRetrieved>";
			strBuff
					.append("<Exception>\n<MainCode>0</MainCode>\n</Exception>\n");
			strBuff.append("<Output>" + sOut + "\n</Output>");
		} catch (SQLException sqlE) {
			strBuff.append("<Exception>\n<MainCode>" + sqlE.getErrorCode()
					+ "</MainCode>\n</Exception>\n");
			strBuff.append("<Output>" + sqlE.getMessage() + "\n</Output>\n");
		} catch (Exception exception3) {
			strBuff
					.append("<Exception>\n<MainCode>9</MainCode>\n</Exception>\n");
			strBuff
					.append("<Output>" + exception3.toString()
							+ "\n</Output>\n");
		} finally {
			try {
				rs.close();
			} catch (Exception ext) {
			}
			try {
				stmt.close();
			} catch (Exception ext) {
			}
		}
		strBuff.append(xmlgenerator.closeOutputFile("APSelect"));
		ExtDBLog.writeLog(xmlparser.toString());
		ExtDBLog.writeLog(strBuff.toString());
		return strBuff.toString();
	}
	private String removeNULL(String str) {
		if (str == null) {
			return "";
		} else {
			return str.trim();
		}

	}
	private static void writeToConsole(String toWrite)
	{
		if(consoleWrite)
			System.out.println(toWrite);
	}
	public String getMQResponse(String requestMessage)
	{
		String strReturnMsg = "";
		String strProcessName="";
		if(requestMessage!=null && requestMessage.indexOf("<ProcessName>")>-1)
		{
			strProcessName=requestMessage.substring(requestMessage.indexOf("<ProcessName>")+13, requestMessage.indexOf("</ProcessName>"));
			requestMessage=requestMessage.substring(0,requestMessage.indexOf("<ProcessName>")) +requestMessage.substring( requestMessage.indexOf("</ProcessName>")+14,requestMessage.length());
		}	
		
		StringBuffer stringbuffer = new StringBuffer();
		try{
		try {
				
				MQEnvironment.hostname = hostName;
				MQEnvironment.channel = channel;
				MQEnvironment.port = Integer.parseInt(port);
				mqQueueManager = new MQQueueManager(qmgrName);

				ExtDBLog.writeLog("Checking details:" + hostName+channel+port+qmgrName);
				ExtDBLog.writeLog("Formatted Message:" + requestMessage.toString());
				ExtDBLog.writeLog("MessageID:" + message.messageId);
				WFCustomBean.WriteCustomLog("Formatted Message:" + requestMessage.toString(),strProcessName);
				WFCustomBean.WriteCustomLog("MessageID:" + message.messageId,strProcessName);
				
			} catch (MQException mqExp) {
				strReturnMsg = "<Output><Description>Error during connecting to Queue Manager.</Description>\n<Exception>\n<CompletionCode>"
						+ mqExp.completionCode
						+ "</CompletionCode>\n<ReasonCode>"
						+ mqExp.reasonCode + "</ReasonCode><ReturnCode>"+mqExp.reasonCode+"</ReturnCode>";
				strReturnMsg = displayError(mqExp.completionCode, mqExp.reasonCode,
						strReturnMsg);
				ExtDBLog.writeLog(strReturnMsg);WFCustomBean.WriteCustomLog(strReturnMsg,strProcessName);
				stringbuffer.append(strReturnMsg);
				try { // try block added by Amandeep
					mqQueueManager.disconnect();
				} catch (Exception e) {
				}
				return stringbuffer.toString();
			} catch (Exception exc) {
				strReturnMsg = "<Output><Description>Error during connecting to Queue Manager.</Description>\n<Exception>\n<ErrorMessageReasonCode>"
						+ exc.toString()
						+ "</ErrorMessageReasonCode>\n</Exception></Output>";
				ExtDBLog.writeLog(strReturnMsg);WFCustomBean.WriteCustomLog(strReturnMsg,strProcessName);
				stringbuffer.append(strReturnMsg);
				try { // try block added by Amandeep
					mqQueueManager.disconnect();
				} catch (Exception e) {
				}
				return stringbuffer.toString();
			}
			// Put Message to source queue
			try {
				int openOption = MQC.MQOO_OUTPUT; // open options for browse & share
				queue = mqQueueManager.accessQueue(qNameSource, openOption, null,
						null, null);
				// message.writeUTF(strMsg);
				message.replyToQueueName = qNameDest;
				//message.replyToQueueName = "clq_default_vm108";
				message.writeString(requestMessage.toString());
				MQPutMessageOptions pmo = new MQPutMessageOptions();
				message.format = "MQSTR"; // refer change history (21/05/2017)
				queue.put(message, pmo);
				queue.close();
			} catch (MQException mqExp) {
				try {
					mqQueueManager.disconnect();
				} catch (Exception e) {
				}
				strReturnMsg = "<Output><Description>Error during putting message to Queue .</Description>\n<Exception>\n<CompletionCode>"
						+ mqExp.completionCode
						+ "</CompletionCode>\n<ReasonCode>"
						+ mqExp.reasonCode + "</ReasonCode><ReturnCode>"+mqExp.reasonCode+"</ReturnCode>";
				strReturnMsg = displayError(mqExp.completionCode, mqExp.reasonCode,
						strReturnMsg);
				ExtDBLog.writeLog(strReturnMsg);WFCustomBean.WriteCustomLog(strReturnMsg,strProcessName);
				stringbuffer.append(strReturnMsg);
				return stringbuffer.toString();
			} catch (java.io.IOException Exp) {
				try {
					mqQueueManager.disconnect();
				} catch (Exception e) {
				}
				strReturnMsg = "<Output><Description>Error during putting message to Queue .IOException.</Description>\n<Exception>\n<ErrorMessageReasonCode>"
						+ Exp.toString()
						+ "</ErrorMessageReasonCode>\n</Exception></Output>";
				ExtDBLog.writeLog(strReturnMsg);WFCustomBean.WriteCustomLog(strReturnMsg,strProcessName);
				stringbuffer.append(strReturnMsg);
				return stringbuffer.toString();
			} catch (Exception exc) {
				try {
					mqQueueManager.disconnect();
				} catch (Exception e) {
				}
				strReturnMsg = "<Output><Description>Error during putting message to Queue .</Description>\n<Exception>\n<ErrorMessageReasonCode>"
						+ exc.toString()
						+ "</ErrorMessageReasonCode>\n</Exception></Output>";
				ExtDBLog.writeLog(strReturnMsg);WFCustomBean.WriteCustomLog(strReturnMsg,strProcessName);
				stringbuffer.append(strReturnMsg);
				return stringbuffer.toString();
			}
			// Get Message
			try {
				// int openOption2 = MQC.MQOO_INPUT_EXCLUSIVE | MQC.MQOO_BROWSE; //
				// open options for browse & share
				int openOption2 = MQC.MQOO_INPUT_AS_Q_DEF
						| MQC.MQOO_FAIL_IF_QUIESCING;
				ExtDBLog.writeLog("A");
				
				queue = mqQueueManager.accessQueue(qNameDest, openOption2, null,
						null, null);
				
				MQGetMessageOptions gmo = new MQGetMessageOptions();
				
				gmo.options = MQC.MQGMO_WAIT | MQC.MQGMO_SYNCPOINT
						| MQC.MQGMO_FAIL_IF_QUIESCING; // MQC.MQGMO_WAIT |
				// MQC.MQGMO_BROWSE_FIRST;
				gmo.waitInterval = Integer.parseInt(lTimeInterval);
				
				MQMessage messageObj = new MQMessage();
				

				// message.messageId = messageId.getBytes();
				ExtDBLog.writeLog("Message Id"+message.messageId);
				messageObj.correlationId = message.messageId;
				ExtDBLog.writeLog("Correlation Id"+messageObj.correlationId);
				
				messageObj.format = "MQSTR";
				
				queue.get(messageObj, gmo);
				
				//strReturnMsg= messageObj.readString(messageObj.getMessageLength());
				strReturnMsg= messageObj.readStringOfByteLength(messageObj.getMessageLength());
				
				ExtDBLog.writeLog("response"+strReturnMsg);WFCustomBean.WriteCustomLog("response"+strReturnMsg,strProcessName);
				queue.close();
				
			} catch (MQException mqExp) {

				strReturnMsg = "<Output><Description>Error during getting message from Queue.</Description>\n<Exception>\n<CompletionCode>"
						+ mqExp.completionCode
						+ "</CompletionCode>\n<ReasonCode>"
						+ mqExp.reasonCode + "</ReasonCode><ReturnCode>"+mqExp.reasonCode+"</ReturnCode>";
				strReturnMsg = displayError(mqExp.completionCode, mqExp.reasonCode,
						strReturnMsg);
				ExtDBLog.writeLog(strReturnMsg);WFCustomBean.WriteCustomLog(strReturnMsg,strProcessName);
				stringbuffer.append(strReturnMsg);
				try {
					queue.close();
				} catch (MQException e) {
				}
				try {
					mqQueueManager.disconnect();
				} catch (Exception e) {
				}
				return stringbuffer.toString();
			} catch (java.io.IOException Exp) {
				try {
					queue.close();
				} catch (MQException e) {
				}
				try {
					mqQueueManager.disconnect();
				} catch (Exception e) {
				}
				strReturnMsg = "<Output><Description>Error during getting message from Queue.IOException.</Description>\n<Exception>\n<ErrorMessageReasonCode>"
						+ Exp.toString()
						+ "</ErrorMessageReasonCode>\n</Exception></Output>";
				ExtDBLog.writeLog(strReturnMsg);WFCustomBean.WriteCustomLog(strReturnMsg,strProcessName);
				stringbuffer.append(strReturnMsg);
				return stringbuffer.toString();
			} catch (Exception exc) {
				try {
					queue.close();
				} catch (MQException e) {
				}
				try {
					mqQueueManager.disconnect();
				} catch (Exception e) {
				}
				strReturnMsg = "<Output><Description>Error during getting message from Queue.</Description>\n<Exception>\n<ErrorMessageReasonCode>"
						+ exc.toString()
						+ "</ErrorMessageReasonCode>\n</Exception></Output>";
				ExtDBLog.writeLog(strReturnMsg);WFCustomBean.WriteCustomLog(strReturnMsg,strProcessName);
				stringbuffer.append(strReturnMsg);
				return stringbuffer.toString();
			} finally {
				try {
					queue.close();
				} catch (MQException e) {
				}
				try {
					mqQueueManager.disconnect();
				} catch (Exception e) {
				}
			}
			// Disconnect from Q Mgr
			try {
				mqQueueManager.disconnect();

			} catch (MQException mqExp) {
				strReturnMsg = "<Output><Description>Error during disconnecting from queue manager.</Description>\n<Exception>\n<CompletionCode>"
						+ mqExp.completionCode
						+ "</CompletionCode>\n<ReasonCode>"
						+ mqExp.reasonCode + "</ReasonCode><ReturnCode>"+mqExp.reasonCode+"</ReturnCode>";
				strReturnMsg = displayError(mqExp.completionCode, mqExp.reasonCode,
						strReturnMsg);
				ExtDBLog.writeLog(strReturnMsg);WFCustomBean.WriteCustomLog(strReturnMsg,strProcessName);
				stringbuffer.append(strReturnMsg);
				return stringbuffer.toString();
			} catch (Exception exc) {
				strReturnMsg = "<Output><Description>Error during disconnecting from queue manager.</Description>\n<Exception>\n<ErrorMessageReasonCode>"
						+ exc.toString()
						+ "</ErrorMessageReasonCode>\n</Exception></Output>";
				ExtDBLog.writeLog(strReturnMsg);WFCustomBean.WriteCustomLog(strReturnMsg,strProcessName);
				stringbuffer.append(strReturnMsg);
				return stringbuffer.toString();
			}
		}catch(Exception e){System.out.println("Exception caught in MQHelper getResponse method "+e.getMessage());}
			return strReturnMsg;
	}
	private String displayError(int completionCode, int reasonCode,
			String sOutputXML) {

		if (completionCode == 2) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageCompletionCode>Call failed (MQCC_FAILED)</ErrorMessageCompletionCode>";
		}
		if (reasonCode == 2002) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_ALREADY_CONNECTED</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2004) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_BUFFER_ERROR</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2005) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_BUFFER_LENGTH_ERROR</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2009) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_CONNECTION_BROKEN</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2011) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_DYNAMIC_Q_NAME_ERROR</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2012) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_ENVIRONMENT_ERROR</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2013) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_EXPIRY_ERROR</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2020) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_INHIBIT_VALUE_ERROR</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2025) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_MAX_CONNS_LIMIT_REACHED</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2026) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_MD_ERROR</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2030) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_MSG_TOO_BIG_FOR_Q</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2031) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_MSG_TOO_BIG_FOR_Q_MGR</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2033) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_NO_MSG_AVAILABLE</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2035) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_NOT_AUTHORIZED</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2036) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_NOT_OPEN_FOR_BROWSE</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2037) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_NOT_OPEN_FOR_INPUT</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2038) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_NOT_OPEN_FOR_INQUIRE</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2039) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_NOT_OPEN_FOR_OUTPUT</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2045) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_OPTION_NOT_VALID_FOR_TYPE</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2051) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_PUT_INHIBITED</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2052) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_Q_DELETED</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2053) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_Q_FULL</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2055) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_Q_NOT_EMPTY</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2056) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_Q_SPACE_NOT_AVAILABLE</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2057) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_Q_TYPE_ERROR</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2058) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_Q_MGR_NAME_ERROR</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2059) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_Q_MGR_NOT_AVAILABLE</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2063) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_SECURITY_ERROR</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2071) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_STORAGE_NOT_AVAILABLE</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2079) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_TRUNCATED_MSG_ACCEPTED</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2087) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_UNKNOWN_REMOTE_Q_MGR</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2090) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_WAIT_INTERVAL_ERROR</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2103) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_ANOTHER_Q_MGR_CONNECTED</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2129) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_ADAPTER_CONN_LOAD_ERROR</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2137) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_OPEN_FAILED</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2161) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_Q_MGR_QUIESCING</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2162) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_Q_MGR_STOPPING</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2173) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_PMO_ERROR</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2186) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_GMO_ERROR</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2202) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_CONNECTION_QUIESCING</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2203) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_CONNECTION_STOPPING</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2207) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_CORREL_ID_ERROR</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2218) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_MSG_TOO_BIG_FOR_CHANNEL</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2219) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_CALL_IN_PROGRESS</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2223) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_Q_MGR_NOT_ACTIVE</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2250) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_MSG_SEQ_NUMBER_ERROR</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2283) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_CHANNEL_STOPPED</ErrorMessageReasonCode>\n";
		} else if (reasonCode == 2296) {
			sOutputXML = sOutputXML
					+ "\n<ErrorMessageReasonCode>MQRC_CHANNEL_NOT_ACTIVATED</ErrorMessageReasonCode>\n";
		}
		sOutputXML = sOutputXML + "</Exception></Output>";
		return sOutputXML;

	}
	/*public static void main(String args[])
	{
		writeToConsole("Testing MQHelper");
		Connection connection = null;
		XMLParser xmlParser = new XMLParser();
		xmlParser.setInputXML("<?xml version='1.0'?><Outer><RewardPoints>200</RewardPoints><ReasonCode>123</ReasonCode><AvailableBalance>300</AvailableBalance></Outer>");
		writeToConsole("xmlParser "+xmlParser.getValueOf("ReasonCode"));
		XMLGenerator xmlgenerator = new XMLGenerator();
		MQHelper mqHelper = new MQHelper();
		try{
			writeToConsole("Inside Try");
			Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");
			String url = "jdbc:sqlserver://192.168.6.213:1433;DatabaseName=rakcabinet_first";
			connection = DriverManager.getConnection(url, "sa", "system123#");
			writeToConsole("Connection obtained");
			//mqHelper.setMQDetails(connection,xmlParser,xmlgenerator);
			String requestMessage = mqHelper.getMQRequestXML(connection, xmlParser, xmlgenerator, "CardServiceEligibility", "5239260220958914");
			writeToConsole(requestMessage);
			//String responseMQ = getMQResponse(requestMessage);
			
		}
		catch(Exception e){writeToConsole("Exception Caught "+e.getMessage());}
	}*/
	public void readPropertyFile()
    {
        try
        {
            Properties properties = new Properties();
            properties.load(getClass().getResourceAsStream("MQSettings.properties"));
            hostName = properties.getProperty("HostName");
            port = properties.getProperty("PortNumber");
            qmgrName = properties.getProperty("QMgrName");
            ReqChannelId = properties.getProperty("RequestorChannelId");
			channel = properties.getProperty("MQChannelId");
            qNameSource = properties.getProperty("SourceQueueName");
			qNameDest = properties.getProperty("DestinationQueueName");
			lTimeInterval = properties.getProperty("TimeInterval");

        }
        catch(Exception exception)
        {
            System.out.println("exception in reading argument file...\n");
            exception.printStackTrace();
            System.exit(0);
        }
    }
	
	private String genMessageID(){
		String Msgid="";
		try{
			Timestamp localTimestamp = new Timestamp(System.currentTimeMillis());
			Random rdn = new Random();
			Msgid = "BPM" + localTimestamp.getDate()+localTimestamp.getMonth()+localTimestamp.getYear()+localTimestamp.getTime()+rdn.nextInt(100)+rdn.nextInt(10000);
		}
		catch(Exception e){
			//SocketMQConnection.mLogger.debug("Exception occured in genMessageID"+e.getMessage());
			e.printStackTrace();
		}
		return Msgid;
	}
	
}