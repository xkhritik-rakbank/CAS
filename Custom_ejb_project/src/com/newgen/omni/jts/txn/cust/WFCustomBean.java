//-------------------------------------------------------------------------
//				NEWGEN SOFTWARE TECHNOLOGIES LIMITED
//		Group						: CIG
//		Product / Project			: Omni Flow 7.1.1
//		Module						: Workflow Server
//		File Name					: WFCustomBean.java
//		Author						: Krishna Kant Singh
//		Date written (DD/MM/YYYY)	: 4/2/2008
//		Description					: WFCustom bean to write custom code.
//-------------------------------------------------------------------------
//					CHANGE HISTORY
//-------------------------------------------------------------------------
//	Date			 Change By	 	Change Description (Bug No. (If Any))
// (01/05/2014)		Aishwarya Gupta Added calls for CSRMSGetData, CSRMSSetIdentityData,
//									CSRMSModifyData, CSRMSSetBlobData, APSelectWithColumnNames,
//									SRM_APMQPutGetMessage, APMQPutGetMessage, APMQPutGetMessage_DSR
// (25/02/2015)		Aishwarya Gupta Added function SRM_APMQPutGetMessage_CARDS_BT_CCC for restricting
//									card list calls to credit cards only in case of BT/CCC
//
// (04/03/2015)		Aishwarya Gupta Added function AO_APMQPutGetMessage for fetching account details
// (05/03/2016)		Aishwarya Gupta Added function BPM_APMQPutGetMessageDirect for Eforms phase 1 process
// (31/05/2016)		Aishwarya Gupta Added function APUpdate2 for Eforms phase 1 process to include comma in values
// (05/07/2016)		Aishwarya Gupta Added function APEncryptString for Convergence
// (10/12/2017)		Angad Shah		Added (this condition added for EIDA_Genuine_Check because it has different headers)
// (15/01/2018)		Angad Shah		Added (subCategoryID = 5 is added for Smart Cash on 15012018 by Angad)
// (22/04/2018)     Angad Shah		Replacing special characters from MessageId for Cheque Book Request Call Only for RAO Process
//-------------------------------------------------------------------------
//
//-------------------------------------------------------------------------
package com.newgen.omni.jts.txn.cust;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;

import com.newgen.WebServiceHandler;
import com.newgen.omni.jts.cmgr.XMLGenerator;
import com.newgen.omni.jts.cmgr.XMLParser;
import com.newgen.omni.jts.excp.JTSException;
import com.newgen.omni.jts.excp.WFSError;
import com.newgen.omni.jts.excp.WFSErrorMsg;
import com.newgen.omni.jts.excp.WFSException;
import com.newgen.omni.jts.txn.wapi.WFParticipant;
import com.newgen.omni.jts.util.WFSUtil;


import com.newgen.omni.jts.cmgr.*;
import com.newgen.omni.jts.excp.*;
import com.newgen.omni.jts.txn.*;
//import com.microsoft.sqlserver.jdbc.*;
import java.sql.*;
import java.io.*;
import nu.xom.*;
import com.newgen.omni.jts.util.*;
import java.security.MessageDigest;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import com.newgen.omni.jts.txn.cust.NamedParameterStatement;

import com.ibm.mq.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import com.newgen.omni.jts.txn.wapi.WFParticipant;
import javax.naming.*;
import javax.sql.DataSource;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.sql.Timestamp;
import java.util.Random;

public class WFCustomBean extends com.newgen.omni.jts.txn.NGOServerInterface
{
	private static String _strFilepath;//check this one
	private MQQueueManager mqQueueManager;
	private MQQueue queue;
	private int openOptionInquire;
	private String hostName;
	private String ReqChannelId;
	private String channel;
	private String port;
	private String qmgrName;
	private String qNameSource;
	private String qNameDest;
	private String lTimeInterval;
	private String strMsgFormat;
	private String strMsgVersion;
	private String strRequestorLanguage;
	private String strRequestorSecurityInfo;
	private String strReturnCode;
	private String  strUserID = "";
	private String  strAccNo = "";
	private String  strAgrID = "";
	private String strCardNo="";
	private String strCardType="";
	private String strCardProduct="";
	private String rewardPoints="";
	private String strBlockRequest = "";
	private static final String LOAN="LOAN";
	private static final String DEBITCARD="DEBITCARD";
	private static final String CARDS="PrimeFetch~FinacleFetch";
	private static final String REDEEM="Redeem";
	private static final String ADJUST="Adjust";
	private static final String FORFEIT="Forfeit";
	private static final String CARDLIST="CardList~FinacleFetch";
	// private static final String CARDLIST="FinacleFetch~CardList";
	private static final String CARDMAINTENANCE="CardMaintenance";
	private static final String FUNDBLOCK="FundBlock";
	private static final String BALANCEENQ="BalanceEnq";
	private static final String CSE="CardServiceEligibility";
	private static final String CSEOB="CardServiceEligibilityOB";
	private static final String AOFETCH="FetchAccounts";
	
	static {
		_strFilepath = getLogLocation();//check this one
	}
	
	private static final long serialVersionUID = 1L;
	//private static String _strFilepath = "."+File.separatorChar+"OFWeb_logs"+File.separatorChar+"FBD_DBLink.log";

	private static final String MIN_DIFF_SELECT_QUERY = "select constantname,constantvalue as MAX_DURATION,to_char(sysdate,'DD-MON-YY HH24:MI') as CURR_DATE_TIME,(to_char(sysdate,'DD-MON-YY')||' '||constantvalue) as MAX_DATE_TIME,FLOOR(((sysdate-to_date((to_char(sysdate,'DD-MON-YY')||' '||constantvalue),'DD-MON-YY hh24:mi'))*24*60)) as MINUTE_DIFF from constantdeftable where processdefid=(select processdefid from processdeftable where processname=?) and constantname='CONST_Intro_Max_Time_Limit'";
	
	public String execute(Connection con, XMLParser parser, XMLGenerator gen) throws JTSException, WFSException
	{
		String option = parser.getValueOf("Option", "", false);
		String outputXml = null;        

		// -- Do not change this
        if(option.equals("CSRMSGetData")) {
            outputXml = getCSRMSData(con, parser, gen);
        } else if(option.equals("CSRMSSetIdentityData")) {
            outputXml = setCSRMSIdentityData(con, parser, gen);
        } else if(option.equals("CSRMSModifyData")) {
            outputXml = modifyCSRMSData(con, parser, gen);
        } else if(option.equals("CSRMSSetBlobData")) {
            outputXml = setCSRMSBlobData(con, parser, gen);
        } else if (option.equals("IGGetData")) {
			outputXml = getData(con, parser, gen);
		} else if (option.equals("IGSetData")) {
			outputXml = setData(con, parser, gen);
		} else if (option.equalsIgnoreCase("APSelect"))
		{		
			outputXml = APSelect(con, parser, gen);
		}
		else if (option.equalsIgnoreCase("APInsert")) 
		{        
			outputXml = APInsert(con, parser, gen);
		}
		else if (option.equalsIgnoreCase("APInsertExtd")) 
		{
			outputXml = APInsertExtd(con, parser, gen);
		}
		else if (option.equalsIgnoreCase("APUpdate")) 
		{
			outputXml = APUpdate(con, parser, gen);
		}
		else if (option.equals("APUpdate2")) {
			outputXml = APUpdate2(con, parser, gen);
		}
		else if (option.equalsIgnoreCase("APDelete")) 
		{
			outputXml = APDelete(con, parser, gen); 
		}
		else if(option.equalsIgnoreCase("APProcedure"))
		{
			outputXml = APProcedure(con, parser, gen); 
		}
		else if (option.equals("APProcedure2")) {
			outputXml = APProcedure2(con, parser, gen);
		} else if (option.equals("APSelectCAPS")) {
			outputXml = APSelectCAPS(con, parser, gen);
		}
		else if(option.equalsIgnoreCase("APSelectWithColumnNames"))
		{
			outputXml = APSelectWithColumnNames(con, parser, gen); 
		}	
		else if(option.equalsIgnoreCase("APWebservice")) 
		{
			try 
			{
				outputXml = Webservice(con, parser, gen);			
			} 
			
			catch (RemoteException e) 
			{
				ExtDBLog.writeLog("In Webservice Exception");
			    ExtDBLog.writeLog("RemoteException in calling Webservice'" +e.getMessage() + "'");
				e.printStackTrace();
			}
			catch (MalformedURLException e) 
			{
				ExtDBLog.writeLog("MalformedURLException in calling Webservice'" +e.getMessage() + "'");
				e.printStackTrace();
			}
			catch(Exception e)
			{
				ExtDBLog.writeLog("Exception---"+e);
				
			}
		}
		else if(option.equalsIgnoreCase("APValidateWiExport")){
			outputXml = APValidateWiExport(con, parser, gen); 
		}
		else if (option.equalsIgnoreCase("APSelectWithNamedParam")) {
			outputXml = APSelectWithNamedParam(con, parser, gen);
		}
		else if (option.equals("SRM_APMQPutGetMessage")) {
			outputXml = SRM_APMQPutGetMessage(con, parser, gen);
		}else if (option.equals("APMQPutGetMessage")) {
            outputXml  = APMQPutGetMessage(con, parser, gen);
		} else if (option.equals("APMQPutGetMessage_DSR")) {			
            outputXml  = APMQPutGetMessage_DSR(con, parser, gen);
            ExtDBLog.writeLog("wfcustombean create method,APMQPutGetMessage_DSR output: "+outputXml);
		}else if (option.equals("APDecryptString")) {			//Added by Amandeep- AES Decryption
            outputXml  = APDecryptString(con, parser, gen);
						
		}else if (option.equals("APEncryptString")) {			//Added by Amandeep- AES Decryption
            outputXml  = APEncryptString(con, parser, gen);
		}else if (option.equalsIgnoreCase("TT_APMQPutGetMessage")) {	//Added by Anurag Anand on 26 Jan 2016
            outputXml  = TT_APMQPutGetMessage(con, parser, gen);
		}else if (option.equalsIgnoreCase("TT_APMQPutGetMessageDirect")) {	//Added by Anurag Anand on 26 Jan 2016
            outputXml  = TT_APMQPutGetMessageDirect(con, parser, gen);
		}else if (option.equalsIgnoreCase("BPM_APMQPutGetMessageDirect")) {	//Added by Anurag Anand on 26 Jan 2016
            outputXml  = BPM_APMQPutGetMessageDirect(con, parser, gen);
		}
		else if (option.equals("AO_APMQPutGetMessage")) {			//Added by Aishwarya- Account Opening Fetch
            outputXml  = AO_APMQPutGetMessage(con, parser, gen);
		} 
		// Added By Ajay on 12 Jab 2017 Starts
		else if(option.equalsIgnoreCase("APProcedure_WithDBO"))
		{
			outputXml = APProcedure_WithDBO(con, parser, gen); 
		}
		else if (option.equalsIgnoreCase("APProcedure2")) {
			outputXml = APProcedure2(con, parser, gen);
		}		
		else if (option.equalsIgnoreCase("APExecuteQueryWithSession")) {
			outputXml = APExecuteQueryWithSession(con, parser, gen);
		}
		else if (option.equalsIgnoreCase("APExecuteQueryWithoutSession")) {		
			outputXml = APExecuteQueryWithoutSession(con, parser, gen);
		}
		// Added By Ajay on 12 Jab 2017 Ends
		else
		{
			outputXml = gen.writeError("Client", WFSError.WF_INVALID_OPERATION_SPECIFICATION, 0,
					WFSErrorMsg.getMessage(WFSError.WF_INVALID_OPERATION_SPECIFICATION), null, WFSError.WF_TMP);
		}
        ExtDBLog.writeLog("wfcustombean create method,APMQPutGetMessage_DSR output before return: "+outputXml);
        return outputXml;
	}
	
	/**
     * APMQPutGetMessage_DSR
     * Added by Amandeep for second MQ server request
     * @param connection Connection
     * @param xmlparser XMLParser
     * @param xmlgenerator XMLGenerator
     * @return String
     */
	public String APMQPutGetMessage_DSR(Connection connection, XMLParser xmlparser, XMLGenerator xmlgenerator)
	{
		StringBuffer stringbuffer;
        stringbuffer = new StringBuffer();
		stringbuffer.append(xmlgenerator.createOutputFile("APMQPutGetMessage_DSR"));
		String strMsg = "";
		String strCardNo = "";
		String strReqType = "";
		String msg = "";
		String strReturnMsg = "";
		String strUserID = "";
		
		MQMessage message = new MQMessage();

		String Msg_id = genMessageID();
		
		try{
			//Read Property File
			readPropertyFile_DSR();
			
			
			/* Amandeep - Order of execution changed of below lines of Code: tcp connections issue.
			
			//Connect TO Queue Manager
			
			MQEnvironment.hostname = hostName;
			MQEnvironment.channel = channel;
			MQEnvironment.port = Integer.parseInt(port);
			mqQueueManager = new MQQueueManager(qmgrName);

			//Format message						
			strUserID = xmlparser.getValueOf("UserID", "", false);
			strCardNo = xmlparser.getValueOf("AccountNo", "", false);
			
			*/
			
			//Format message						
			strUserID = xmlparser.getValueOf("UserID", "", false);
			strCardNo = xmlparser.getValueOf("CardNo", "", false);
			strReqType = xmlparser.getValueOf("RequestType", "", false);
			
			WriteCustomLog("strUserID for DSR"+strUserID);
			WriteCustomLog("strCardNo for DSR"+strCardNo);
			WriteCustomLog("strReqType for DSR"+strReqType);
			//Connect TO Queue Manager			
			MQEnvironment.hostname = hostName;
			MQEnvironment.channel = channel;
			MQEnvironment.port = Integer.parseInt(port);
			mqQueueManager = new MQQueueManager(qmgrName);

			
			
			/*strMsg = "<EE_EAI_MESSAGE>" +
						"<EE_EAI_HEADER>" +
							"<MsgFormat>"+strMsgFormat+"</MsgFormat>" +
							"<MsgVersion>"+strMsgVersion+"</MsgVersion>" +
							"<RequestorChannelId>"+ReqChannelId+"</RequestorChannelId>" +
							"<RequestorUserId>"+strUserID+"</RequestorUserId>" +
							"<RequestorLanguage>"+strRequestorLanguage+"</RequestorLanguage>" +
							"<RequestorSecurityInfo>"+strRequestorSecurityInfo+"</RequestorSecurityInfo>" +
							"<ReturnCode>"+strReturnCode+"</ReturnCode>" +
							"<ReturnDesc></ReturnDesc>" +
							//Changed below by Amandeep
							//"<MessageId></MessageId>" +
							"<MessageId>"+message.messageId+"</MessageId>" +
							//Changed Above by Amandeep
							"<Extra1></Extra1>" +
							"<Extra2></Extra2>" +
						"</EE_EAI_HEADER>" +
						"<DCR_CARD_ENQUIRY>" +
							"<ACCOUNTNO>"+strAccNo+"</ACCOUNTNO>" +
						"</DCR_CARD_ENQUIRY>" +
					"</EE_EAI_MESSAGE>";
					
				*/
			java.util.Date d = new Date();
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.mmm");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
			String extra2 = sdf1.format(d)+"+04:00";
				if(strReqType.equalsIgnoreCase("Cards"))
				{
					strMsg="<EE_EAI_MESSAGE>" +
							"<EE_EAI_HEADER>" +
								"<MsgFormat>CARD_DETAILS</MsgFormat>" +
								"<MsgVersion>"+strMsgVersion+"</MsgVersion>" +
								"<RequestorChannelId>"+ReqChannelId+"</RequestorChannelId>" +
								"<RequestorUserId>"+strUserID+"</RequestorUserId>" +
								"<RequestorLanguage>"+strRequestorLanguage+"</RequestorLanguage>" +
								"<RequestorSecurityInfo>"+strRequestorSecurityInfo+"</RequestorSecurityInfo>" +
								"<ReturnCode>"+strReturnCode+"</ReturnCode>" +
								"<ReturnDesc>success</ReturnDesc>" +
								"<MessageId>"+Msg_id+"</MessageId>" +
								"<Extra1>REQ||BPM.123</Extra1>" +
								"<Extra2>"+extra2+"</Extra2>" +
								//"<Extra1>REQ||BPM.123</Extra1>" +
								//"<Extra2>2014-06-05T12:45:08.045+04:00</Extra2>" +
							"</EE_EAI_HEADER>" +
							"<CardDetails>" +
								"<BankId>RAK</BankId>" +
								"<CIFID>"+"</CIFID>" +
								"<CardCRNNumber>" +"</CardCRNNumber>" +
								"<CardNumber>"+strCardNo+"</CardNumber>" +
								"<ResponseReqd>" +"</ResponseReqd>" +
							"</CardDetails>" +
						"</EE_EAI_MESSAGE>"	;
				}						
				else if(strReqType.equalsIgnoreCase("Customer"))
				{
					strMsg="<EE_EAI_MESSAGE>" +
							"<EE_EAI_HEADER>" +
								"<MsgFormat>ENTITY_DETAILS</MsgFormat>" +
								"<MsgVersion>"+strMsgVersion+"</MsgVersion>" +
								"<RequestorChannelId>"+ReqChannelId+"</RequestorChannelId>" +
								"<RequestorUserId>"+strUserID+"</RequestorUserId>" +
								"<RequestorLanguage>"+strRequestorLanguage+"</RequestorLanguage>" +
								"<RequestorSecurityInfo>"+strRequestorSecurityInfo+"</RequestorSecurityInfo>" +
								"<ReturnCode>"+strReturnCode+"</ReturnCode>" +
								"<ReturnDesc>success</ReturnDesc>" +
								"<MessageId>"+Msg_id+"</MessageId>" +
								"<Extra1>REQ||BPM.123</Extra1>" +
								"<Extra2>"+extra2+"</Extra2>" +
								//"<Extra1>REQ||BPM.123</Extra1>" +
								//"<Extra2>2014-06-05T12:45:08.045+04:00</Extra2>" +
							"</EE_EAI_HEADER>" +
							"<CustomerDetails>" +
								"<BankId>RAK</BankId>" +
								"<CIFID>"+"</CIFID>" +
								"<ACCType>DC</ACCType>" +
								"<ACCNumber>"+strCardNo+"</ACCNumber>" +
								"<InquiryType>Customer</InquiryType>"+
							"</CustomerDetails>" +
						"</EE_EAI_MESSAGE>"	;
				}
				else
				{
					strReturnMsg="<Output><Description>Incorrect request Type.</Description>\n<Exception>\n<CompletionCode>-1</CompletionCode>\n<ReasonCode>Incorrect request Type.</ReasonCode>";
					//strReturnMsg = displayError(mqExp.completionCode,mqExp.reasonCode,strReturnMsg);
					ExtDBLog.writeLog(strReturnMsg);
					stringbuffer.append(strReturnMsg);
					stringbuffer.append(xmlgenerator.closeOutputFile("APMQPutGetMessage_DSR"));
					try{	//try block added by Amandeep
						mqQueueManager.disconnect();
					}catch(Exception e){}
					return stringbuffer.toString();
				}
			WriteCustomLog("inputrequest for DSR"+strMsg);				
			ExtDBLog.writeLog("Formatted Message:" + strMsg);			
			ExtDBLog.writeLog("MessageID:" + message.messageId);			
			ExtDBLog.writeLog("MessageID:" + Msg_id);			
			
		}catch(MQException mqExp)
        {
			strReturnMsg="<Output><Description>Error during connecting to Queue Manager.</Description>\n<Exception>\n<CompletionCode>"+mqExp.completionCode+"</CompletionCode>\n<ReasonCode>"+mqExp.reasonCode+"</ReasonCode>";
			strReturnMsg = displayError(mqExp.completionCode,mqExp.reasonCode,strReturnMsg);
			ExtDBLog.writeLog(strReturnMsg);
			stringbuffer.append(strReturnMsg);
			stringbuffer.append(xmlgenerator.closeOutputFile("APMQPutGetMessage_DSR"));
			try{	//try block added by Amandeep
				mqQueueManager.disconnect();
			}catch(Exception e){}
			return stringbuffer.toString();
        }catch (Exception exc) {  
			strReturnMsg="<Output><Description>Error during connecting to Queue Manager.</Description>\n<Exception>\n<ErrorMessageReasonCode>"+exc.toString()+"</ErrorMessageReasonCode>\n</Exception></Output>";
			ExtDBLog.writeLog(strReturnMsg);
			stringbuffer.append(strReturnMsg);
			stringbuffer.append(xmlgenerator.closeOutputFile("APMQPutGetMessage_DSR"));
			try{	//try block added by Amandeep
				mqQueueManager.disconnect();
			}catch(Exception e){}
        	return stringbuffer.toString();
		}
		
			
		
		// Put Message to source queue
		try
		{
			int openOption = MQC.MQOO_OUTPUT; // open options for browse & share
			queue = mqQueueManager.accessQueue(qNameSource, openOption,null,null,null);
			//message.writeUTF(strMsg);
			message.replyToQueueName =  qNameDest;
			message.writeString(strMsg);
			MQPutMessageOptions pmo = new MQPutMessageOptions();
			queue.put(message,pmo);
			queue.close();
		}catch(MQException mqExp)
        {
			try{
				mqQueueManager.disconnect();
			}catch(Exception e){}
			strReturnMsg="<Output><Description>Error during putting message to Queue .</Description>\n<Exception>\n<CompletionCode>"+mqExp.completionCode+"</CompletionCode>\n<ReasonCode>"+mqExp.reasonCode+"</ReasonCode>";
			strReturnMsg = displayError(mqExp.completionCode,mqExp.reasonCode,strReturnMsg);
			ExtDBLog.writeLog(strReturnMsg);
			stringbuffer.append(strReturnMsg);
			stringbuffer.append(xmlgenerator.closeOutputFile("APMQPutGetMessage_DSR"));
        	return stringbuffer.toString();
        }catch (java.io.IOException Exp) {
			try{
				mqQueueManager.disconnect();
			}catch(Exception e){}
			strReturnMsg="<Output><Description>Error during putting message to Queue .IOException.</Description>\n<Exception>\n<ErrorMessageReasonCode>"+Exp.toString()+"</ErrorMessageReasonCode>\n</Exception></Output>";
			ExtDBLog.writeLog(strReturnMsg);
			stringbuffer.append(strReturnMsg);
			stringbuffer.append(xmlgenerator.closeOutputFile("APMQPutGetMessage_DSR"));
			return stringbuffer.toString();			
		}catch (Exception exc) {     
			try{
				mqQueueManager.disconnect();
			}catch(Exception e){}
			strReturnMsg="<Output><Description>Error during putting message to Queue .</Description>\n<Exception>\n<ErrorMessageReasonCode>"+exc.toString()+"</ErrorMessageReasonCode>\n</Exception></Output>";
			ExtDBLog.writeLog(strReturnMsg);
			stringbuffer.append(strReturnMsg);
			stringbuffer.append(xmlgenerator.closeOutputFile("APMQPutGetMessage_DSR"));
        	return stringbuffer.toString();
		}
		//Get Message			
		try
		{
			//int openOption2 =  MQC.MQOO_INPUT_EXCLUSIVE | MQC.MQOO_BROWSE; // open options for browse & share
			int openOption2 =  MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_FAIL_IF_QUIESCING ;
			ExtDBLog.writeLog("A");
			queue = mqQueueManager.accessQueue(qNameDest, openOption2,null,null,null);
			ExtDBLog.writeLog("B");
			MQGetMessageOptions gmo = new MQGetMessageOptions();
			ExtDBLog.writeLog("C");
			gmo.options = MQC.MQGMO_WAIT | MQC.MQGMO_SYNCPOINT | MQC.MQGMO_FAIL_IF_QUIESCING; //MQC.MQGMO_WAIT | MQC.MQGMO_BROWSE_FIRST;
			gmo.waitInterval = Integer.parseInt(lTimeInterval);
			ExtDBLog.writeLog("E");
			MQMessage messageObj = new MQMessage();
			ExtDBLog.writeLog("F");

			//message.messageId = messageId.getBytes();
			messageObj.correlationId = message.messageId;
			ExtDBLog.writeLog("G");
			messageObj.format="MQSTR";
			ExtDBLog.writeLog("H");
			queue.get(messageObj, gmo);
			ExtDBLog.writeLog("I");
			msg = messageObj.readString(messageObj.getMessageLength()); 
			ExtDBLog.writeLog("J");
			queue.close();
			ExtDBLog.writeLog("H");
		}catch(MQException mqExp)
        {
			
			strReturnMsg="<Output><Description>Error during getting message from Queue.</Description>\n<Exception>\n<CompletionCode>"+mqExp.completionCode+"</CompletionCode>\n<ReasonCode>"+mqExp.reasonCode+"</ReasonCode>";
			strReturnMsg = displayError(mqExp.completionCode,mqExp.reasonCode,strReturnMsg);
			ExtDBLog.writeLog(strReturnMsg);
			stringbuffer.append(strReturnMsg);
			try{
				queue.close();
			}catch(MQException e){}
			try{
				mqQueueManager.disconnect();
			}catch(Exception e){}
			stringbuffer.append(xmlgenerator.closeOutputFile("APMQPutGetMessage_DSR"));
			return stringbuffer.toString();
        }catch (java.io.IOException Exp) {
			try{
				queue.close();
			}catch(MQException e){}
			try{
				mqQueueManager.disconnect();
			}catch(Exception e){}
			strReturnMsg="<Output><Description>Error during getting message from Queue.IOException.</Description>\n<Exception>\n<ErrorMessageReasonCode>"+Exp.toString()+"</ErrorMessageReasonCode>\n</Exception></Output>";
			ExtDBLog.writeLog(strReturnMsg);
			stringbuffer.append(strReturnMsg);
			stringbuffer.append(xmlgenerator.closeOutputFile("APMQPutGetMessage_DSR"));
			return stringbuffer.toString();			
		}catch (Exception exc) {   
			try{
				queue.close();
			}catch(MQException e){}
			try{
				mqQueueManager.disconnect();
			}catch(Exception e){}
			strReturnMsg="<Output><Description>Error during getting message from Queue.</Description>\n<Exception>\n<ErrorMessageReasonCode>"+exc.toString()+"</ErrorMessageReasonCode>\n</Exception></Output>";
			ExtDBLog.writeLog(strReturnMsg);
			stringbuffer.append(strReturnMsg);
			stringbuffer.append(xmlgenerator.closeOutputFile("APMQPutGetMessage_DSR"));
			return stringbuffer.toString();
		}
		finally{
			try{
				queue.close();
			}catch(MQException e){}
			try{
				mqQueueManager.disconnect();
			}catch(Exception e){}
		}
		//Disconnect from Q Mgr
		try
		{
			mqQueueManager.disconnect();
        
		}catch(MQException mqExp)
        {
			strReturnMsg="<Output><Description>Error during disconnecting from queue manager.</Description>\n<Exception>\n<CompletionCode>"+mqExp.completionCode+"</CompletionCode>\n<ReasonCode>"+mqExp.reasonCode+"</ReasonCode>";
			strReturnMsg = displayError(mqExp.completionCode,mqExp.reasonCode,strReturnMsg);
			ExtDBLog.writeLog(strReturnMsg);
			stringbuffer.append(strReturnMsg);
			stringbuffer.append(xmlgenerator.closeOutputFile("APMQPutGetMessage_DSR"));
        	return stringbuffer.toString();
        }catch (Exception exc) {         					        
			strReturnMsg="<Output><Description>Error during disconnecting from queue manager.</Description>\n<Exception>\n<ErrorMessageReasonCode>"+exc.toString()+"</ErrorMessageReasonCode>\n</Exception></Output>";
			ExtDBLog.writeLog(strReturnMsg);
			stringbuffer.append(strReturnMsg);
			stringbuffer.append(xmlgenerator.closeOutputFile("APMQPutGetMessage_DSR"));
        	return stringbuffer.toString();
		}
		stringbuffer.append("<Output><Message>"+msg+"</Message>\n<Exception>\n<CompletionCode>0</CompletionCode>\n<ReasonCode>0</ReasonCode><Description>success</Description>\n</Exception>\n</Output>");
		stringbuffer.append(xmlgenerator.closeOutputFile("APMQPutGetMessage_DSR"));
        ExtDBLog.writeLog(stringbuffer.toString());
		return stringbuffer.toString();

	}
	
	public String APMQPutGetMessage(Connection connection, XMLParser xmlparser, XMLGenerator xmlgenerator)
	{
		StringBuffer stringbuffer;
        stringbuffer = new StringBuffer();
		stringbuffer.append(xmlgenerator.createOutputFile("APMQPutGetMessage"));
		String strMsg = "";
		String strAgrID = "";
		String msg = "";
		String strReturnMsg = "";
		String strUserID = "";
		
		MQMessage message = new MQMessage();

		String Msg_id = genMessageID();	
		
		try{
			//Read Property File
			readPropertyFile();
			//Connect TO Queue Manager
			MQEnvironment.hostname = hostName;
			MQEnvironment.channel = channel;
			MQEnvironment.port = Integer.parseInt(port);
			mqQueueManager = new MQQueueManager(qmgrName);
			WriteCustomLog("Quemanagername from propeties file" + qmgrName);	

			//Format message						
			strUserID = xmlparser.getValueOf("UserID", "", false);
			strAgrID = xmlparser.getValueOf("AgreementNo", "", false);
			
			strMsg = "<EE_EAI_MESSAGE>" +
						"<EE_EAI_HEADER>" +
							"<MsgFormat>"+strMsgFormat+"</MsgFormat>" +
							"<MsgVersion>"+strMsgVersion+"</MsgVersion>" +
							"<RequestorChannelId>"+ReqChannelId+"</RequestorChannelId>" +
							"<RequestorUserId>"+strUserID+"</RequestorUserId>" +
							"<RequestorLanguage>"+strRequestorLanguage+"</RequestorLanguage>" +
							"<RequestorSecurityInfo>"+strRequestorSecurityInfo+"</RequestorSecurityInfo>" +
							"<ReturnCode>"+strReturnCode+"</ReturnCode>" +
							"<ReturnDesc></ReturnDesc>" +
							"<MessageId>"+Msg_id+"</MessageId>" +
							"<Extra1></Extra1>" +
							"<Extra2></Extra2>" +
						"</EE_EAI_HEADER>" +
						"<LoanDetailInquiry>" +
							"<APPLICATIONID>"+strAgrID+"</APPLICATIONID>" +
						"</LoanDetailInquiry>" +
					"</EE_EAI_MESSAGE>";
			WriteCustomLog("Formatted Message:" + strMsg);	
			ExtDBLog.writeLog("Formatted Message:" + strMsg);			
			ExtDBLog.writeLog("MessageID:" + message.messageId);			
			ExtDBLog.writeLog("MessageID:" + Msg_id);			
			
		}catch(MQException mqExp)
        {
			strReturnMsg="<Output><Description>Error during connecting to Queue Manager.</Description>\n<Exception>\n<CompletionCode>"+mqExp.completionCode+"</CompletionCode>\n<ReasonCode>"+mqExp.reasonCode+"</ReasonCode>";
			strReturnMsg = displayError(mqExp.completionCode,mqExp.reasonCode,strReturnMsg);
			ExtDBLog.writeLog(strReturnMsg);
			stringbuffer.append(strReturnMsg);
			try{	//try block added by Amandeep
				mqQueueManager.disconnect();
			}catch(Exception e){}
			return stringbuffer.toString();
        }catch (Exception exc) {         					        
			strReturnMsg="<Output><Description>Error during connecting to Queue Manager.</Description>\n<Exception>\n<ErrorMessageReasonCode>"+exc.toString()+"</ErrorMessageReasonCode>\n</Exception></Output>";
			ExtDBLog.writeLog(strReturnMsg);
			stringbuffer.append(strReturnMsg);
			try{	//try block added by Amandeep
				mqQueueManager.disconnect();
			}catch(Exception e){}
			return stringbuffer.toString();
		}
		// Put Message to source queue
		try
		{
			int openOption = MQC.MQOO_OUTPUT; // open options for browse & share
			queue = mqQueueManager.accessQueue(qNameSource, openOption,null,null,null);
			//message.writeUTF(strMsg);
			message.replyToQueueName =  qNameDest;
			message.writeString(strMsg);
			MQPutMessageOptions pmo = new MQPutMessageOptions();
			queue.put(message,pmo);
			queue.close();
		}catch(MQException mqExp)
        {
			try{
				mqQueueManager.disconnect();
			}catch(Exception e){}
			strReturnMsg="<Output><Description>Error during putting message to Queue .</Description>\n<Exception>\n<CompletionCode>"+mqExp.completionCode+"</CompletionCode>\n<ReasonCode>"+mqExp.reasonCode+"</ReasonCode>";
			strReturnMsg = displayError(mqExp.completionCode,mqExp.reasonCode,strReturnMsg);
			ExtDBLog.writeLog(strReturnMsg);
			stringbuffer.append(strReturnMsg);
			return stringbuffer.toString();
        }catch (java.io.IOException Exp) {
			try{
				mqQueueManager.disconnect();
			}catch(Exception e){}
			strReturnMsg="<Output><Description>Error during putting message to Queue .IOException.</Description>\n<Exception>\n<ErrorMessageReasonCode>"+Exp.toString()+"</ErrorMessageReasonCode>\n</Exception></Output>";
			ExtDBLog.writeLog(strReturnMsg);
			stringbuffer.append(strReturnMsg);
			return stringbuffer.toString();			
		}catch (Exception exc) {     
			try{
				mqQueueManager.disconnect();
			}catch(Exception e){}
			strReturnMsg="<Output><Description>Error during putting message to Queue .</Description>\n<Exception>\n<ErrorMessageReasonCode>"+exc.toString()+"</ErrorMessageReasonCode>\n</Exception></Output>";
			ExtDBLog.writeLog(strReturnMsg);
			stringbuffer.append(strReturnMsg);
			return stringbuffer.toString();
		}
		//Get Message			
		try
		{
			//int openOption2 =  MQC.MQOO_INPUT_EXCLUSIVE | MQC.MQOO_BROWSE; // open options for browse & share
			int openOption2 =  MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_FAIL_IF_QUIESCING ;
			ExtDBLog.writeLog("A");
			queue = mqQueueManager.accessQueue(qNameDest, openOption2,null,null,null);
			ExtDBLog.writeLog("B");
			MQGetMessageOptions gmo = new MQGetMessageOptions();
			ExtDBLog.writeLog("C");
			gmo.options = MQC.MQGMO_WAIT | MQC.MQGMO_SYNCPOINT | MQC.MQGMO_FAIL_IF_QUIESCING; //MQC.MQGMO_WAIT | MQC.MQGMO_BROWSE_FIRST;
			gmo.waitInterval = Integer.parseInt(lTimeInterval);
			ExtDBLog.writeLog("E");
			MQMessage messageObj = new MQMessage();
			ExtDBLog.writeLog("F");

			//message.messageId = messageId.getBytes();
			messageObj.correlationId = message.messageId;
			ExtDBLog.writeLog("G");
			messageObj.format="MQSTR";
			ExtDBLog.writeLog("H");
			queue.get(messageObj, gmo);
			ExtDBLog.writeLog("I");
			msg = messageObj.readString(messageObj.getMessageLength()); 
			ExtDBLog.writeLog("J");
			queue.close();
			ExtDBLog.writeLog("H");
		}catch(MQException mqExp)
        {
			
			strReturnMsg="<Output><Description>Error during getting message from Queue.</Description>\n<Exception>\n<CompletionCode>"+mqExp.completionCode+"</CompletionCode>\n<ReasonCode>"+mqExp.reasonCode+"</ReasonCode>";
			strReturnMsg = displayError(mqExp.completionCode,mqExp.reasonCode,strReturnMsg);
			ExtDBLog.writeLog(strReturnMsg);
			stringbuffer.append(strReturnMsg);
			try{
				queue.close();
			}catch(MQException e){}
			try{
				mqQueueManager.disconnect();
			}catch(Exception e){}
			return stringbuffer.toString();
        }catch (java.io.IOException Exp) {
			try{
				queue.close();
			}catch(MQException e){}
			try{
				mqQueueManager.disconnect();
			}catch(Exception e){}
			strReturnMsg="<Output><Description>Error during getting message from Queue.IOException.</Description>\n<Exception>\n<ErrorMessageReasonCode>"+Exp.toString()+"</ErrorMessageReasonCode>\n</Exception></Output>";
			ExtDBLog.writeLog(strReturnMsg);
			stringbuffer.append(strReturnMsg);
			return stringbuffer.toString();			
		}catch (Exception exc) {   
			try{
				queue.close();
			}catch(MQException e){}
			try{
				mqQueueManager.disconnect();
			}catch(Exception e){}
			strReturnMsg="<Output><Description>Error during getting message from Queue.</Description>\n<Exception>\n<ErrorMessageReasonCode>"+exc.toString()+"</ErrorMessageReasonCode>\n</Exception></Output>";
			ExtDBLog.writeLog(strReturnMsg);
			stringbuffer.append(strReturnMsg);
			return stringbuffer.toString();
		}
		finally{
			try{
				queue.close();
			}catch(MQException e){}
			try{
				mqQueueManager.disconnect();
			}catch(Exception e){}
		}
		//Disconnect from Q Mgr
		try
		{
			mqQueueManager.disconnect();
        
		}catch(MQException mqExp)
        {
			strReturnMsg="<Output><Description>Error during disconnecting from queue manager.</Description>\n<Exception>\n<CompletionCode>"+mqExp.completionCode+"</CompletionCode>\n<ReasonCode>"+mqExp.reasonCode+"</ReasonCode>";
			strReturnMsg = displayError(mqExp.completionCode,mqExp.reasonCode,strReturnMsg);
			ExtDBLog.writeLog(strReturnMsg);
			stringbuffer.append(strReturnMsg);
			return stringbuffer.toString();
        }catch (Exception exc) {         					        
			strReturnMsg="<Output><Description>Error during disconnecting from queue manager.</Description>\n<Exception>\n<ErrorMessageReasonCode>"+exc.toString()+"</ErrorMessageReasonCode>\n</Exception></Output>";
			ExtDBLog.writeLog(strReturnMsg);
			stringbuffer.append(strReturnMsg);
			return stringbuffer.toString();
		}
		stringbuffer.append("<Output><Message>"+msg+"</Message>\n<Exception>\n<CompletionCode>0</CompletionCode>\n<ReasonCode>0</ReasonCode><Description>success</Description>\n</Exception>\n</Output>");
		ExtDBLog.writeLog(stringbuffer.toString());
		return stringbuffer.toString();

	}
	/**
     * APMQPutGetMessage_DSR
     * Added by Amandeep for second MQ server request
     * @param connection Connection
     * @param xmlparser XMLParser
     * @param xmlgenerator XMLGenerator
     * @return String
     */

	public String getCSRMSData(Connection con, XMLParser parser, XMLGenerator gen) throws JTSException, WFSException
    {
        StringBuilder strBldOutputXML = null;
        Statement stmt = null;
        ResultSet rs = null;
        int mainCode = 0;
        int subCode = 0;
        String subject = null;
        String descr = null;
        String errType = WFSError.WF_TMP;

        String strEngineName = parser.getValueOf("EngineName", "", false);

        try
        {
            Builder objBuilder = new Builder();
            Document docInput = objBuilder.build(new StringReader(parser.toString()));
            Element rootNode = docInput.getRootElement();
            //LogFilenew("****** GetData ******");            
            writeLog(strEngineName, "Error::* * * * * * GetData * * * * * *");
            Element nodeQuery = rootNode.getFirstChildElement("QueryString");
            String queryString = nodeQuery.getValue();
            //LogFilenew("Input Query:" + queryString);
            writeLog(strEngineName, "Error::Input Query:" + queryString);

            if(queryString.trim().toUpperCase().startsWith("CALL "))
            {
                stmt = con.prepareCall("{" + queryString + "}");
                CallableStatement cstmt = (CallableStatement) stmt;
                cstmt.registerOutParameter(1, -10);
                cstmt.execute();
                rs = (ResultSet) cstmt.getObject(1);
            }
            else
            {
                stmt = con.createStatement();
                rs = stmt.executeQuery(queryString);
            }
            //int count = 0;
            int clmnCount = 0;
            StringBuilder strBldDataElement = new StringBuilder("<DataList>");
            StringBuilder strBldColumnElement = new StringBuilder("<Columns>");
            if(rs != null)
            {
                clmnCount = rs.getMetaData().getColumnCount();
                ResultSetMetaData objResultSetMetaData = rs.getMetaData();
                boolean isFirstColumn = true;
                for(int intColumnId = 1; intColumnId <= objResultSetMetaData.getColumnCount(); intColumnId++)
                {
                    if(isFirstColumn)
                    {
                        strBldColumnElement.append(objResultSetMetaData.getColumnName(intColumnId));
                        isFirstColumn = false;
                    }
                    else
                    {
                        strBldColumnElement.append(",").append(objResultSetMetaData.getColumnName(intColumnId));
                    }
                }

                while(rs.next())
                {
                    strBldDataElement.append("<Data>");
                    for(int f = 1; f <= clmnCount; f++)
                    {
                        writeLog(strEngineName, "Info :: Type:" + objResultSetMetaData.getColumnType(f));
                        if(objResultSetMetaData.getColumnType(f) == java.sql.Types.LONGVARBINARY)
                        {
                            strBldDataElement.append("<Value" + f + ">");
                            InputStream is = rs.getBinaryStream(f);

                            writeLog(strEngineName, "Info :: Type:is" + is);
                            writeLog(strEngineName, "Info :: Type:is.available()" + is.available());
                            if(is != null)
                            {
                                BufferedInputStream bis = new BufferedInputStream(is);
                                DataInputStream dis = new DataInputStream(bis);
                                byte[] buffer = new byte[dis.available()];
                                dis.readFully(buffer);
                                strBldDataElement.append(new String(buffer));
                                /*while(is.available() > 0)
                                 {
                                 int intReadBytes = is.read(buffer);
                                 strBldDataElement.append(new String(buffer, 0, intReadBytes));
                                 }*/
                                dis.close();
                                bis.close();
                                is.close();
                            }
                            strBldDataElement.append("</Value" + f + ">");
                        }
                        else if(objResultSetMetaData.getColumnType(f) == java.sql.Types.BLOB)
                        {
                            strBldDataElement.append("<Value" + f + ">");
                            Blob objBlob = null;
                            writeLog(strEngineName, "Info :: Type:Blob");
                            objBlob = rs.getBlob(f);

                            byte[] buff = objBlob.getBytes(1, (int) objBlob.length());
                            strBldDataElement.append(new String(buff));
                            if(objBlob != null)
                            {
                                objBlob.free();
                            }
                            strBldDataElement.append("</Value" + f + ">");
                        }
                        else if(objResultSetMetaData.getColumnType(f) == java.sql.Types.CLOB)
                        {
                            strBldDataElement.append("<Value" + f + ">");
                            Clob objClob = null;
                            writeLog(strEngineName, "Info :: Type:Blob");
                            objClob = rs.getClob(f);
                            strBldDataElement.append(objClob.getSubString(1, (int) objClob.length()));
                            if(objClob != null)
                            {
                                objClob.free();
                            }
                            strBldDataElement.append("</Value" + f + ">");
                        }
                        else if(rs.getString(f) != null && rs.getString(f).trim().length() != 0)
                        {
                            strBldDataElement.append("<Value" + f + ">");

                            String strColumnValue = rs.getString(f);
                            strColumnValue = strColumnValue.replaceAll("\u00ff", "").replaceAll("\u00fc", "").replaceAll("\u0016", "");
                            //System.out.println(strColumnValue);
                            if(strColumnValue != null)
                            {
                                strBldDataElement.append(strColumnValue.trim());
                            }
                            strBldDataElement.append("</Value" + f + ">");
                        }
                    }
                    strBldDataElement.append("</Data>");
                }
            }
            strBldColumnElement.append("</Columns>");
            strBldDataElement.append("</DataList>");
            rs.close();
            stmt.close();


            if(mainCode == 0)
            {
                StringBuilder strBldOutXML = new StringBuilder();
                strBldOutXML.append("<CSRMSGetData_Output>");
                strBldOutXML.append("<Option>").append("CSRMSGetData").append("</Option>");
                strBldOutXML.append("<Exception>");
                strBldOutXML.append("<MainCode>0</MainCode>");
                strBldOutXML.append("</Exception>");
                //Attribute atrColCount = new Attribute("Columns", String.valueOf(clmnCount));
                //nodeDataList.addAttribute(atrColCount);
                strBldOutXML.append(strBldColumnElement);
                strBldOutXML.append(strBldDataElement);
                strBldOutXML.append("</CSRMSGetData_Output>");
                strBldOutputXML = strBldOutXML;
            }

            if(mainCode == WFSError.WM_NO_MORE_DATA)
            {
                strBldOutputXML = new StringBuilder();
                strBldOutputXML.append(gen.writeError("CSRMSGetData", WFSError.WM_NO_MORE_DATA, 0,
                        WFSError.WF_TMP, WFSErrorMsg.getMessage(WFSError.WM_NO_MORE_DATA), ""));
                mainCode = 0;
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            mainCode = WFSError.WM_INVALID_FILTER;
            subCode = WFSError.WFS_SQL;
            subject = WFSErrorMsg.getMessage(mainCode);
            errType = WFSError.WF_FAT;
            if(e.getErrorCode() == 0)
            {
                if(e.getSQLState().equalsIgnoreCase("08S01"))
                {
                    descr = (new JTSSQLError(e.getSQLState())).getMessage() + "(SQL State : " + e.getSQLState() + ")";
                }
            }
            else
            {
                descr = e.getMessage();
            }
        }
        catch(NumberFormatException e)
        {
            e.printStackTrace();
            mainCode = WFSError.WF_OPERATION_FAILED;
            subCode = WFSError.WFS_ILP;
            subject = WFSErrorMsg.getMessage(mainCode);
            errType = WFSError.WF_TMP;
            descr = e.toString();
        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
            mainCode = WFSError.WF_OPERATION_FAILED;
            subCode = WFSError.WFS_SYS;
            subject = WFSErrorMsg.getMessage(mainCode);
            errType = WFSError.WF_TMP;
            descr = e.toString();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            mainCode = WFSError.WF_OPERATION_FAILED;
            subCode = WFSError.WFS_EXP;
            subject = WFSErrorMsg.getMessage(mainCode);
            errType = WFSError.WF_TMP;
            descr = e.toString();
        }
        catch(Error e)
        {
            e.printStackTrace();
            mainCode = WFSError.WF_OPERATION_FAILED;
            subCode = WFSError.WFS_EXP;
            subject = WFSErrorMsg.getMessage(mainCode);
            errType = WFSError.WF_TMP;
            descr = e.toString();
        }
        finally
        {
            try
            {
                if(rs != null)
                {
                    rs.close();
                    rs = null;
                }
            }
            catch(Exception e)
            {
            }
            try
            {
                if(stmt != null)
                {
                    stmt.close();
                    stmt = null;
                }
            }
            catch(Exception e)
            {
            }
            if(mainCode != 0)
            {
                String strOutputXml = getErrorOutputXml("CSRMSGetData", mainCode, errType, subject);
                //LogFilenew("Error::Subject:" + subject + "\r\nError::Desc:" + descr);
                writeLog(strEngineName, "Error::Subject:" + subject + "\r\nError::Desc:" + descr);
                return strOutputXml;
            }
            //LogFilenew("Executed.");
            writeLog(strEngineName, "Error::Executed");
            return strBldOutputXML.toString();
        }
    }
	
	public String setCSRMSIdentityData(Connection connection, XMLParser xmlparser, XMLGenerator xmlgenerator) throws JTSException, WFSException
    {
        StringBuffer outputXML = null;
        ResultSet rs = null;

        int iMainCode = 0;
        int iSubCode = 0;
        String strSubject = null;
        String strDesc = null;
        String strErrType = WFSError.WF_TMP;
        String strEngineName = xmlparser.getValueOf("EngineName", "", false);
        String strOutputXML = "<CSRMSSetData_Output><Option>CSRMSSetData</Option><Exception><MainCode>-1</MainCode></Exception></CSRMSSetData_Output>";
        boolean isAutoCommit = false;

        try
        {
            if(connection.getAutoCommit())
            {
                isAutoCommit = true;
                connection.setAutoCommit(false);
            }
            else
            {
                isAutoCommit = false;
            }
        }
        catch(Throwable th)
        {
            return strOutputXML;
        }

        try
        {
            String strQuery = xmlparser.getValueOf("Query", "", false);

            if(connection.getMetaData().getDatabaseProductName().toLowerCase().contains("oracle"))
            {
                Statement objStatement = null;
                String strTableName = "";
                try
                {//Obtain the query without Select @@IDENTITY
                    //Assumption: 1 single insert query followed by ; followed by Select @@IDENTITY
                    int iStartindex = 0;
                    int iEndindex = 0;
                    int iTmpEndIndex = 0;

                    //Get table name
                    String strTempQuery = strQuery.toLowerCase();
                    iStartindex = strTempQuery.indexOf("into") + 4;
                    strTempQuery = strTempQuery.substring(iStartindex).trim();
                    iStartindex = 0;
                    iEndindex = strTempQuery.indexOf(" ");
                    iTmpEndIndex = strTempQuery.indexOf("(");

                    if(iEndindex > 0)
                    {
                        if(iTmpEndIndex > 0 && iTmpEndIndex < iEndindex)
                        {
                            strTableName = strTempQuery.substring(iStartindex, iTmpEndIndex);
                        }
                        else
                        {
                            strTableName = strTempQuery.substring(iStartindex, iEndindex);
                        }
                    }
                    else
                    {
                        strTableName = strTempQuery.substring(iStartindex, iTmpEndIndex);
                    }

                    System.out.println("Table Name: " + strTableName);

                    objStatement = connection.createStatement();

                    //Execute
                    objStatement.execute(strQuery);
                    objStatement.close();
                    objStatement = connection.createStatement();
                    if(strTableName.compareToIgnoreCase("Usr_0_Tickets") == 0)
                    {
                        strQuery = "SELECT SEQ_Tickets_TicketNo.CURRVAL from DUAL";
                    }
                    else if(strTableName.compareToIgnoreCase("Usr_0_ResponseMessages") == 0)
                    {
                        strQuery = "SELECT SEQ_RMessages_ResponseID.CURRVAL from DUAL";
                    }
                    else if(strTableName.compareToIgnoreCase("Usr_0_MassRequests") == 0)
                    {
                        strQuery = "SELECT SEQ_Usr_0_MassRequests.CURRVAL from DUAL";
                    }
                    else if(strTableName.compareToIgnoreCase("Usr_0_CSRMSPageTextDir") == 0)
                    {
                        strQuery = "SELECT SEQ_CSRMSPageTextDir_ID.CURRVAL from DUAL";
                    }
                    else if(strTableName.compareToIgnoreCase("Usr_0_CSRMS_AgentBreaksInfo") == 0)
                    {
                        strQuery = "SELECT SEQ_CSRMS_AgentBreaksInfo.CURRVAL from DUAL";
                    }
                    objStatement.execute(strQuery);
                    rs = objStatement.getResultSet();
                    rs.next();
                    String strIdentityValue = Integer.toString(rs.getInt(1));
                    iMainCode = 0;
                    strOutputXML = "<CSRMSSetData_Output><Option>CSRMSSetData</Option><Exception><MainCode>0</MainCode><IdentityID>" + strIdentityValue + "</IdentityID></Exception></CSRMSSetData_Output>";

                }
                finally
                {
                    if(objStatement != null)
                    {
                        objStatement.close();
                        objStatement = null;
                    }
                }

            }
            else
            {
                Statement statement = connection.createStatement();

                //System.out.println(xmlparser.toString());
                //System.out.println(strQuery);
                statement.execute(strQuery);
                statement.close();
                statement = connection.createStatement();
                statement.execute("SELECT (@@IDENTITY) as IDValue");
                rs = statement.getResultSet();
                rs.next();
                String strIdentityValue = Integer.toString(rs.getInt("IDValue"));
                iMainCode = 0;
                strOutputXML = "<CSRMSSetData_Output><Option>CSRMSSetData</Option><Exception><MainCode>0</MainCode><IdentityID>" + strIdentityValue + "</IdentityID></Exception></CSRMSSetData_Output>";
            }


            if(iMainCode == WFSError.WM_NO_MORE_DATA)
            {
                outputXML = new StringBuffer(500);
                outputXML.append(xmlgenerator.writeError("CSRMSGetData", WFSError.WM_NO_MORE_DATA, 0,
                        WFSError.WF_TMP, WFSErrorMsg.getMessage(WFSError.WM_NO_MORE_DATA), ""));
                iMainCode = 0;
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            iMainCode = 400;
            iSubCode = 804;
            strSubject = WFSErrorMsg.getMessage(iMainCode);
            strErrType = "TEMPORARY";
            strDesc = exception.toString();
        }
        catch(Error error)
        {
            error.printStackTrace();
            iMainCode = 400;
            iSubCode = 804;
            strSubject = WFSErrorMsg.getMessage(iMainCode);
            strErrType = "TEMPORARY";
            strDesc = error.toString();
        }
        finally
        {
            try
            {
                if(iMainCode == 0)
                {
                    connection.commit();
                    if(isAutoCommit)
                    {
                        connection.setAutoCommit(true);
                    }
                }
                else
                {
                    connection.rollback();
                    if(isAutoCommit)
                    {
                        connection.setAutoCommit(true);
                    }
                }
            }
            catch(Exception ignored)
            {
                ignored.printStackTrace();
            }
            if(iMainCode != 0)
            {
                String strOutputXml = getErrorOutputXml("CSRMSSetData", iMainCode, strErrType, strSubject);
                //LogFilenew("Error::Subject:" + strSubject + "\r\nError::Desc:" + strDesc);
                writeLog(strEngineName, "Error::Subject:" + strSubject + "\r\nError::Desc:" + strDesc);
                return strOutputXml;
            }
            //LogFilenew("Executed.");
            writeLog(strEngineName, "Error::Executed");
            return strOutputXML;
        }
    }

    public String modifyCSRMSData(Connection connection, XMLParser xmlparser, XMLGenerator xmlgenerator) throws JTSException, WFSException
    {
        Statement statement = null;
        int iMainCode = 0;
        int iSubCode = 0;
        String strSubject = null;
        String strDesc = null;
        String strErrType = "TEMPORARY";
        String strEngineName = xmlparser.getValueOf("EngineName", "", false);
        String strOutputXML = "<CSRMSGetData_Output><Option>CSRMSGetData</Option><Exception><MainCode>0</MainCode></Exception></CSRMSGetData_Output>";


        try
        {
            statement = connection.createStatement();
            NGXmlList objNGXmlList = xmlparser.createList("IGSetData_Input", "Query");
            for(; objNGXmlList.hasMoreElements(); objNGXmlList.skip())
            {
                String strQuery = objNGXmlList.getVal("Query").trim();
                for(; strQuery.endsWith(";");)
                {
                    strQuery = strQuery.substring(0, strQuery.lastIndexOf(";"));
                }
                //System.out.println("Query: " + strQuery);
                statement.addBatch(strQuery);
            }
            statement.executeBatch();
            statement.close();
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            iMainCode = 400;
            iSubCode = 804;
            strSubject = WFSErrorMsg.getMessage(iMainCode);
            strErrType = "TEMPORARY";
            strDesc = exception.toString();
        }
        catch(Error error)
        {
            error.printStackTrace();
            iMainCode = 400;
            iSubCode = 804;
            strSubject = WFSErrorMsg.getMessage(iMainCode);
            strErrType = "TEMPORARY";
            strDesc = error.toString();
        }
        finally
        {
            try
            {
                if(iMainCode == 0)
                {
                    if(!connection.getAutoCommit())
                    {
                        connection.commit();
                        connection.setAutoCommit(true);
                    }
                }
                else
                {
                    if(!connection.getAutoCommit())
                    {
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                }
                if(statement != null)
                {
                    statement.close();
                    statement = null;
                }
            }
            catch(Exception ignored)
            {
            }
            if(iMainCode != 0)
            {
                String strOutputXml = getErrorOutputXml("CSRMSGetData", iMainCode, strErrType, strSubject);
                //LogFilenew("Error::Subject:" + strSubject + "\r\nError::Desc:" + strDesc);
                writeLog(strEngineName, "Error::Subject:" + strSubject + "\r\nError::Desc:" + strDesc);
                return strOutputXml;
            }
            //LogFilenew("Executed.");
            writeLog(strEngineName, "Error::Executed");
            return strOutputXML;
        }
    }

    public String setCSRMSBlobData(Connection connection, XMLParser xmlparser, XMLGenerator xmlgenerator) throws JTSException, WFSException
    {
        PreparedStatement pstmt = null;
        int iMainCode = 0;
        int iSubCode = 0;
        String strSubject = null;
        String strDesc = null;
        String strErrType = "TEMPORARY";
        String strEngineName = xmlparser.getValueOf("EngineName", "", false);
        String strOutputXML = "<CSRMSSetBlobData_Output><Option>CSRMSSetBlobData</Option><Exception><MainCode>0</MainCode></Exception></CSRMSSetBlobData_Output>";

        try
        {
            
            String strQuery = xmlparser.getValueOf("Query", "", false);
            XMLParser objXMLParser = new XMLParser(strQuery);
            NGXmlList objNGXmlList =  objXMLParser.createList("Params", "Param");
            String strTableName = objXMLParser.getValueOf("TableName");
            StringBuilder strBldColumns = new StringBuilder();
            StringBuilder strBldValues = new StringBuilder();
            boolean isFirstParam = true;
            for(; objNGXmlList.hasMoreElements(); objNGXmlList.skip())
            {
                if(isFirstParam)
                {
                    strBldColumns.append(objNGXmlList.getVal("Name"));
                    strBldValues.append("?");
                    isFirstParam = false;
                }
                else
                {
                    strBldColumns.append(",").append(objNGXmlList.getVal("Name"));
                    strBldValues.append(",").append("?");
                }
            }
            //System.out.println("Insert into " + strTableName + "(" + strBldColumns.toString() + ") values(" + strBldValues.toString() + ")");
            pstmt = connection.prepareStatement("Insert into " + strTableName + "(" + strBldColumns.toString() + ") values(" + strBldValues.toString() + ")");
            //System.out.println("Insert into " + strTableName + "(" + strBldColumns.toString() + ") values(" + strBldValues.toString() + ")");
            int i = 1;
            //System.out.println("starting");
            for(objNGXmlList.reInitialize(); objNGXmlList.hasMoreElements(); objNGXmlList.skip(), i++)
            {
                //System.out.println(i);
                String strType = objNGXmlList.getVal("Type").toLowerCase();
                //System.out.println("strType" + strType);
                String strValue = objNGXmlList.getVal("Value");


                if(strType.compareTo("int") == 0)
                {
                    pstmt.setInt(i, Integer.parseInt(strValue));
                    //System.out.println("strValue" + strValue);
                }
                else if(strType.compareTo("blob") == 0)
                {
                    //System.out.println("blob" + strValue);
                    //strValue = new String(new BASE64Decoder().decodeBuffer(strValue));
                    ByteArrayInputStream bais = new ByteArrayInputStream(strValue.getBytes());
                    pstmt.setBinaryStream(i, (InputStream) bais, bais.available());
                }
                else if(strType.compareTo("clob") == 0)
                {
                    //System.out.println("blob" + strValue);
                    //strValue = new String(new BASE64Decoder().decodeBuffer(strValue));
                    //strValue.get
                    StringReader objStringReader = new StringReader(strValue);
                    pstmt.setCharacterStream(i, objStringReader);
                }
                else
                {
                    //System.out.println("strValue" + strValue);
                    pstmt.setString(i, strValue);
                }
            }
            pstmt.execute();
            pstmt.close();
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            iMainCode = 400;
            iSubCode = 804;
            strSubject = WFSErrorMsg.getMessage(iMainCode);
            strErrType = "TEMPORARY";
            strDesc = exception.toString();
        }
        catch(Error error)
        {
            error.printStackTrace();
            iMainCode = 400;
            iSubCode = 804;
            strSubject = WFSErrorMsg.getMessage(iMainCode);
            strErrType = "TEMPORARY";
            strDesc = error.toString();
        }
        finally
        {
            try
            {
                if(iMainCode == 0)
                {
                    if(!connection.getAutoCommit())
                    {
                        connection.commit();
                        connection.setAutoCommit(true);
                    }
                }
                else
                {
                    if(!connection.getAutoCommit())
                    {
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                }
                if(pstmt != null)
                {
                    pstmt.close();
                    pstmt = null;
                }
            }
            catch(Exception ignored)
            {
            }
            if(iMainCode != 0)
            {
                String strOutputXml = getErrorOutputXml("CSRMSSetBlobData", iMainCode, strErrType, strSubject);
                //LogFilenew("Error::Subject:" + strSubject + "\r\nError::Desc:" + strDesc);
                writeLog(strEngineName, "Error::Subject:" + strSubject + "\r\nError::Desc:" + strDesc);
                return strOutputXml;
            }
            //LogFilenew("Executed.");
            writeLog(strEngineName, "Error::Executed");
            return strOutputXML;
        }
    }
	
	public String getData(Connection con, XMLParser parser, XMLGenerator gen)
			throws JTSException, WFSException {
		StringBuffer outputXML = null;
		Statement stmt = null;
		ResultSet rs = null;
		int mainCode = 0;
		int subCode = 0;
		String subject = null;
		String descr = null;
		String errType = WFSError.WF_TMP;

		String strEngineName = parser.getValueOf("EngineName", "", false);

		try {
			Builder objBuilder = new Builder();
			Document docInput = objBuilder.build(new StringReader(parser
					.toString()));
			Element rootNode = docInput.getRootElement();

			// LogFilenew("****** GetData ******");
			writeLog(strEngineName, "****** GetData ******");

			int iSessionId = Integer.parseInt(rootNode.getFirstChildElement(
					"SessionId").getValue());
			if (WFSUtil.WFCheckSession(con, iSessionId) == null) {
				mainCode = -1;
				subCode = -1;
				subject = "Invalid session.";
				descr = "Invalid session.";
				Element nodeOutXml = new Element("IGGetData_Output");
				Element nodeExp = new Element("Exception");
				nodeOutXml.appendChild(nodeExp);
				Element node = new Element("MainCode");
				node.appendChild("-1");
				nodeExp.appendChild(node);

				node = new Element("TypeOfError");
				node.appendChild("TEMPORARY");
				nodeExp.appendChild(node);

				node = new Element("Subject");
				node.appendChild("Invalid session.");
				nodeExp.appendChild(node);
				outputXML = new StringBuffer(nodeOutXml.toXML());
			} else {
				Element nodeQuery = rootNode
						.getFirstChildElement("QueryString");
				String queryString = nodeQuery.getValue();
				// LogFilenew("Input Query:" + queryString);
				writeLog(strEngineName, "Input Query:" + queryString);

				int iCheckSum = Integer.parseInt(nodeQuery
						.getAttributeValue("CS"));
				if (validateCheckSum(queryString, iSessionId, iCheckSum)) {
					if (queryString.trim().toUpperCase().startsWith("CALL ")) {
						stmt = con.prepareCall("{" + queryString + "}");
						CallableStatement cstmt = (CallableStatement) stmt;
						cstmt.registerOutParameter(1, -10);
						cstmt.execute();
						rs = (ResultSet) cstmt.getObject(1);
					} else {
						stmt = con.createStatement();
						rs = stmt.executeQuery(queryString);
					}
					int count = 0;
					int clmnCount = 0;
					Element nodeDataList = new Element("DataList");
					if (rs != null) {
						clmnCount = rs.getMetaData().getColumnCount();
						while (rs.next()) {
							Element nodeData = new Element("Data");
							nodeDataList.appendChild(nodeData);
							count++;

							for (int f = 1; f <= clmnCount; f++) {
								if (rs.getString(f) != null
										&& rs.getString(f).trim().length() != 0) {
									Element nodeValue = new Element("Value" + f);
									nodeValue.appendChild(rs.getString(f)
											.trim());
									nodeData.appendChild(nodeValue);
								}
							}
						}
					}
					rs.close();
					stmt.close();

					if (count <= 0) {
						mainCode = WFSError.WM_NO_MORE_DATA;
						subCode = 0;
						subject = WFSErrorMsg.getMessage(mainCode);
						descr = WFSErrorMsg.getMessage(subCode);
						errType = WFSError.WF_TMP;
					}
					if (mainCode == 0) {
						Element nodeOutXml = new Element("IGGetData_Output");
						Element nodeExp = new Element("Exception");
						nodeOutXml.appendChild(nodeExp);

						Element nodeMainCode = new Element("MainCode");
						nodeMainCode.appendChild("0");
						nodeExp.appendChild(nodeMainCode);

						Attribute atrColCount = new Attribute("Columns", String
								.valueOf(clmnCount));
						nodeDataList.addAttribute(atrColCount);
						nodeOutXml.appendChild(nodeDataList);

						outputXML = new StringBuffer(nodeOutXml.toXML());
					}
					/* Added by Ashish 08/08/2008 */
					if (mainCode == WFSError.WM_NO_MORE_DATA) {
						outputXML = new StringBuffer(500);
						outputXML.append(gen.writeError("IGGetData",
								WFSError.WM_NO_MORE_DATA, 0, WFSError.WF_TMP,
								WFSErrorMsg
										.getMessage(WFSError.WM_NO_MORE_DATA),
								""));
						mainCode = 0;
					}
					/* end Added by Ashish 08/08/2008 */
				} else {
					mainCode = -1;
					subCode = -1;
					subject = "Query validation failed.";
					Element nodeOutXml = new Element("IGGetData_Output");
					Element nodeExp = new Element("Exception");
					nodeOutXml.appendChild(nodeExp);
					Element node = new Element("MainCode");
					node.appendChild("-1");
					nodeExp.appendChild(node);

					node = new Element("TypeOfError");
					node.appendChild("TEMPORARY");
					nodeExp.appendChild(node);

					node = new Element("Subject");
					node.appendChild(subject);
					nodeExp.appendChild(node);
					outputXML = new StringBuffer(nodeOutXml.toXML());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			mainCode = WFSError.WM_INVALID_FILTER;
			subCode = WFSError.WFS_SQL;
			subject = WFSErrorMsg.getMessage(mainCode);
			errType = WFSError.WF_FAT;
			if (e.getErrorCode() == 0) {
				if (e.getSQLState().equalsIgnoreCase("08S01")) {
					descr = (new JTSSQLError(e.getSQLState())).getMessage()
							+ "(SQL State : " + e.getSQLState() + ")";
				}
			} else {
				descr = e.getMessage();
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			mainCode = WFSError.WF_OPERATION_FAILED;
			subCode = WFSError.WFS_ILP;
			subject = WFSErrorMsg.getMessage(mainCode);
			errType = WFSError.WF_TMP;
			descr = e.toString();
		} catch (NullPointerException e) {
			e.printStackTrace();
			mainCode = WFSError.WF_OPERATION_FAILED;
			subCode = WFSError.WFS_SYS;
			subject = WFSErrorMsg.getMessage(mainCode);
			errType = WFSError.WF_TMP;
			descr = e.toString();
		} catch (Exception e) {
			e.printStackTrace();
			mainCode = WFSError.WF_OPERATION_FAILED;
			subCode = WFSError.WFS_EXP;
			subject = WFSErrorMsg.getMessage(mainCode);
			errType = WFSError.WF_TMP;
			descr = e.toString();
		} catch (Error e) {
			e.printStackTrace();
			mainCode = WFSError.WF_OPERATION_FAILED;
			subCode = WFSError.WFS_EXP;
			subject = WFSErrorMsg.getMessage(mainCode);
			errType = WFSError.WF_TMP;
			descr = e.toString();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
			} catch (Exception e) {
			}
			try {
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (Exception e) {
			}
			if (mainCode != 0) {
				// LogFilenew("Error::Subject:" + subject + "\r\nError::Desc:" +
				// descr);
				writeLog(strEngineName, "Error::Subject:" + subject
						+ "\r\nError::Desc:" + descr);
				throw new WFSException(mainCode, subCode, errType, subject,
						descr);
			}
			// LogFilenew("Executed.");
			writeLog(strEngineName, "Executed");
			return outputXML.toString();
		}
	}

	public String setData(Connection connection, XMLParser xmlparser,
			XMLGenerator xmlgenerator) throws JTSException, WFSException {
		Statement statement = null;
		int iMainCode = 0;
		int iSubCode = 0;
		String strSubject = null;
		String strDesc = null;
		String strErrType = "TEMPORARY";
		Element nodeOutXml = null;

		String strEngineName = xmlparser.getValueOf("EngineName", "", false);
		try {
			// LogFilenew("****** SetData ******");
			writeLog(strEngineName, "****** SetData ******");

			Builder objBuilder = new Builder();
			Document docInput = objBuilder.build(new StringReader(xmlparser
					.toString()));
			Element rootNode = docInput.getRootElement();

			int iSessionId = Integer.parseInt(rootNode.getFirstChildElement(
					"SessionId").getValue());
			if (WFSUtil.WFCheckSession(connection, iSessionId) == null) {
				iMainCode = -1;
				iSubCode = -1;
				strSubject = "Invalid session.";
				strDesc = "Invalid session.";
				nodeOutXml = new Element("IGSetData_Output");
				Element nodeExp = new Element("Exception");
				nodeOutXml.appendChild(nodeExp);
				Element node = new Element("MainCode");
				node.appendChild("-1");
				nodeExp.appendChild(node);

				node = new Element("TypeOfError");
				node.appendChild("TEMPORARY");
				nodeExp.appendChild(node);

				node = new Element("Subject");
				node.appendChild("Invalid session.");
				nodeExp.appendChild(node);
			} else {
				// transaction started
				if (connection.getAutoCommit()) {
					connection.setAutoCommit(false);
				}

				statement = connection.createStatement();

				Elements nodeQueries = rootNode.getChildElements("Query");
				if (nodeQueries != null) {
					for (int i = 0; i < nodeQueries.size(); i++) {
						Element nodeQuery = nodeQueries.get(i);
						String strQuery = nodeQuery.getValue();
						// LogFilenew("Input Query:" + strQuery);
						writeLog(strEngineName, "Input Query:" + strQuery);

						int iCheckSum = Integer.parseInt(nodeQuery
								.getAttributeValue("CS"));
						if (!validateCheckSum(strQuery, iSessionId, iCheckSum)) {
							iMainCode = -1;
							iSubCode = -1;
							strSubject = "Query validation failed.";
							strDesc = "Query validation failed.";
							nodeOutXml = new Element("IGSetData_Output");
							Element nodeExp = new Element("Exception");
							nodeOutXml.appendChild(nodeExp);
							Element node = new Element("MainCode");
							node.appendChild("-1");
							nodeExp.appendChild(node);

							node = new Element("TypeOfError");
							node.appendChild("TEMPORARY");
							nodeExp.appendChild(node);

							node = new Element("Subject");
							node.appendChild(strSubject);
							nodeExp.appendChild(node);
							return nodeOutXml.toString();
						}
						statement.addBatch(strQuery);
					}
					int[] iRetVal = statement.executeBatch();
					statement.close();
				}
				if (iMainCode == 0) {
					nodeOutXml = new Element("IGSetData_Output");
					Element nodeExp = new Element("Exception");
					nodeOutXml.appendChild(nodeExp);

					Element nodeMainCode = new Element("MainCode");
					nodeMainCode.appendChild("0");
					nodeExp.appendChild(nodeMainCode);
				}
			}
		} catch (SQLException sqlexception) {
			sqlexception.printStackTrace();
			iMainCode = 15;
			iSubCode = 801;
			strSubject = WFSErrorMsg.getMessage(iMainCode);
			strErrType = "FATAL";
			if (sqlexception.getErrorCode() == 0) {
				if (sqlexception.getSQLState().equalsIgnoreCase("08S01")) {
					strDesc = (new JTSSQLError(sqlexception.getSQLState()))
							.getMessage()
							+ "(SQL State : "
							+ sqlexception.getSQLState()
							+ ")";
				}
			} else {
				strDesc = sqlexception.getMessage();
			}
		} catch (NumberFormatException numberformatexception) {
			numberformatexception.printStackTrace();
			iMainCode = 400;
			iSubCode = 802;
			strSubject = WFSErrorMsg.getMessage(iMainCode);
			strErrType = "TEMPORARY";
			strDesc = numberformatexception.toString();
		} catch (NullPointerException nullpointerexception) {
			nullpointerexception.printStackTrace();
			iMainCode = 400;
			iSubCode = 803;
			strSubject = WFSErrorMsg.getMessage(iMainCode);
			strErrType = "TEMPORARY";
			strDesc = nullpointerexception.toString();
		}
		// catch (JTSException jtsexception)
		// {
		// LogFilenew("JTSException");
		// jtsexception.printStackTrace();
		// iMainCode = 400;
		// iSubCode = jtsexception.getErrorCode();
		// strSubject = WFSErrorMsg.getMessage(iMainCode);
		// strErrType = "TEMPORARY";
		// strDesc = jtsexception.getMessage();
		// }
		catch (Exception exception) {
			exception.printStackTrace();
			iMainCode = 400;
			iSubCode = 804;
			strSubject = WFSErrorMsg.getMessage(iMainCode);
			strErrType = "TEMPORARY";
			strDesc = exception.toString();
		} catch (Error error) {
			error.printStackTrace();
			iMainCode = 400;
			iSubCode = 804;
			strSubject = WFSErrorMsg.getMessage(iMainCode);
			strErrType = "TEMPORARY";
			strDesc = error.toString();
		} finally {
			try {
				if (iMainCode == 0) {
					if (!connection.getAutoCommit()) {
						connection.commit();
						connection.setAutoCommit(true);
					}
				} else {
					if (!connection.getAutoCommit()) {
						connection.rollback();
						connection.setAutoCommit(true);
					}
				}
				if (statement != null) {
					statement.close();
					statement = null;
				}
			} catch (Exception ignored) {
			}
			if (iMainCode != 0) {
				// LogFilenew("Error::Subject:" + strSubject +
				// "\r\nError::Desc:" + strDesc);
				writeLog(strEngineName, "Error::Subject:" + strSubject
						+ "\r\nError::Desc:" + strDesc);
				throw new WFSException(iMainCode, iSubCode, strErrType,
						strSubject, strDesc);
			}
			// LogFilenew("Executed.");
			writeLog(strEngineName, "Executed");
			return nodeOutXml.toXML();
		}
	}
	
	/**
	 * APUpdate2
	 * 
	 * @param connection
	 *            Connection
	 * @param xmlparser
	 *            XMLParser
	 * @param xmlgenerator
	 *            XMLGenerator
	 * @return String
	 * Delimiter used to parse values is ~ instead of comma
	 */	
	
	public String APUpdate2(Connection connection, XMLParser xmlparser,
			XMLGenerator xmlgenerator) {
		Statement stmt = null;
		StringBuffer strBuff = new StringBuffer();

		strBuff.append(xmlgenerator.createOutputFile("APUpdate2"));
		try {
			boolean bValidSession = false;
			int iSessionId = xmlparser.getIntOf("SessionId", 0, false);
			bValidSession = ValidateSession(connection, iSessionId);
			if (bValidSession) {
				String tableName = xmlparser.getValueOf("TableName", "", false);
				StringTokenizer columnsTKN = new StringTokenizer(xmlparser
						.getValueOf("ColName", "", false), ",");
				StringTokenizer valuesTKN = new StringTokenizer(xmlparser
						.getValueOf("Values", "", false), "~");
				String s15 = xmlparser.getValueOf("WhereClause", "", false);
				String query;
				for (query = "Update " + tableName + " set "; columnsTKN
						.hasMoreTokens(); query = query
						+ columnsTKN.nextToken() + "=" + valuesTKN.nextToken()
						+ ",") {
					;
				}
				if (query.endsWith(",")) {
					query = query.substring(0, query.length() - 1);
				}
				query = query + " where " + s15;
				stmt = connection.createStatement();
				int iReturn = stmt.executeUpdate(query);
				strBuff
						.append("<Exception>\n<MainCode>0</MainCode>\n</Exception>\n");
				strBuff.append("<Output>" + iReturn + "</Output>");
			} else {
				strBuff
						.append("<Exception>\n<MainCode>11</MainCode>\n</Exception>\n");
				strBuff.append("<Output>Invalid Session.\n</Output>\n");
			}
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
				stmt.close();
			} catch (Exception ext) {
			}

		}
		strBuff.append(xmlgenerator.closeOutputFile("APUpdate2"));
		ExtDBLog.writeLog(xmlparser.toString());
		ExtDBLog.writeLog(strBuff.toString());
		return strBuff.toString();
	}
	
	public String APProcedure2(Connection connection, XMLParser xmlparser,
			XMLGenerator xmlgenerator) {
		Statement statement = null;
		String s = null;
		String s1 = "";
		try {
			String s2 = xmlparser.getValueOf("ProcessDefId", "", true);
			String s3 = xmlparser.getValueOf("Status", "", true);
			String s4 = xmlparser.getValueOf("SessionId", "", true);
			String s5 = xmlparser.getValueOf("EngineName", "", false);
			String s6 = xmlparser.getValueOf("ProcName", "", false);
			String s7 = xmlparser.getValueOf("Params", "", false);
			String s8 = "";
			String s9 = "";
			s6 = s6.trim();
			String s10 = "{call " + s6 + "(" + s7 + ")}";
			// System.out.println("Stored Procedure" + s10);
			CallableStatement callablestatement = connection.prepareCall(s10);
			// System.out.println("vineet 11");
			callablestatement.execute();
			// System.out.println("vineet 12");
			Object obj = null;
			// System.out.println("vineet 13");
			StringTokenizer stringtokenizer = new StringTokenizer(xmlparser
					.getValueOf("NoOfCols", "", false), "#");
			// System.out.println("vineet 14");
			do {
				ResultSet resultset = callablestatement.getResultSet();
				// System.out.println("vineet 15");
				int i = Integer.parseInt(stringtokenizer.nextToken());
				// System.out.println("vineet 16");
				for (; resultset.next(); System.out.println("vineet 20-" + s8)) {
					for (int j = 0; j < i; j++)
						if (j == 0) {
							if (s8.equals("")) {
								s8 = resultset.getString(j + 1);
								// System.out.println("vineet 17" + s8);
							} else {
								s8 = s8 + resultset.getString(j + 1);
								// System.out.println("vineet 18" + s8);
							}
						} else {
							s8 = s8 + "!" + resultset.getString(j + 1);
							// System.out.println("vineet 19" + s8);
						}

					s8 = s8 + "~";
				}

				if (s8.endsWith("~"))
					s8 = s8.substring(0, s8.length() - 1);
				s8 = s8 + "#";
				// System.out.println("vineet 22" + s8);
				resultset.close();
				// System.out.println("vineet 23");
			} while (callablestatement.getMoreResults());
			callablestatement.close();
			// System.out.println("vineet 24");
			if (s8.endsWith("#"))
				// System.out.println("vineet 25");
				s8 = s8.substring(0, s8.length() - 1);
			// System.out.println("vineet 26");
			s = "<? Xml Version=\"1.0\"?><APProcedure2_output><Option>APProcedure2</Option><MainCode>0</MainCode><Results>"
					+ s8 + "</Results>" + "</APProcedure2_output>";
		} catch (Exception exception) {
			exception.printStackTrace();
			s = "<? Xml Version=\"1.0\"?><APProcedure2_output><Option>APProcedure2</Option><MainCode>15</MainCode><Message>"
					+ exception.getMessage()
					+ "</Message>"
					+ "</APProcedure2_output>";
		} finally {
			try {
				if (!connection.getAutoCommit())
					connection.setAutoCommit(true);
				if (statement != null)
					statement.close();
			} catch (Exception exception2) {
				exception2.printStackTrace();
			}
		}
		return s;
	}
	
	public String APSelectCAPS(Connection paramConnection,
			XMLParser paramXMLParser, XMLGenerator paramXMLGenerator) {
		Connection localConnection = null;
		ResultSet localResultSet = null;
		Statement localStatement = null;
		ResultSetMetaData localResultSetMetaData = null;
		StringBuffer localStringBuffer1 = new StringBuffer();
		StringBuffer localStringBuffer2 = new StringBuffer();
		String str1 = "";

		localStringBuffer1.append(paramXMLGenerator
				.createOutputFile("APSelectCAPS"));
		try {
			InitialContext localInitialContext = new InitialContext();
			DataSource localDataSource = (DataSource) localInitialContext
					.lookup("jdbc/cbop");
			localConnection = localDataSource.getConnection();

			String str2 = paramXMLParser.getValueOf("Query", "", false);

			localStatement = localConnection.createStatement();
			localResultSet = localStatement.executeQuery(str2);
			localResultSetMetaData = localResultSet.getMetaData();
			int i = localResultSetMetaData.getColumnCount();
			int j = 0;

			while (localResultSet.next()) {
				localStringBuffer2.append("\n\t\t<tr>");
				for (int k = 1; k <= i; ++k) {
					localStringBuffer2.append("\n\t\t\t<td>");
					localStringBuffer2.append(removeNULL(localResultSet
							.getString(k)));
					localStringBuffer2.append("</td>");
				}
				localStringBuffer2.append("\n\t\t</tr>");
				j += 1;
			}
			str1 = localStringBuffer2.toString();
			str1 = "\n\t<Records>" + str1 + "\n\t</Records>";
			str1 = str1 + "\n\t<TotalRetrieved>" + j + "</TotalRetrieved>";
			localStringBuffer1
					.append("<Exception>\n<MainCode>0</MainCode>\n</Exception>\n");

			localStringBuffer1.append("<Output>" + str1 + "\n</Output>");
		} catch (SQLException localException6) {
			localStringBuffer1.append("<Exception>\n<MainCode>"
					+ localException6.getErrorCode()
					+ "</MainCode>\n</Exception>\n");

			localStringBuffer1.append("<Output>" + localException6.getMessage()
					+ "\n</Output>\n");
		} catch (Exception localException10) {
			localStringBuffer1
					.append("<Exception>\n<MainCode>9</MainCode>\n</Exception>\n");

			localStringBuffer1.append("<Output>" + localException10.toString()
					+ "\n</Output>\n");
		} finally {
			try {
				localResultSet.close();
				localResultSet = null;
			} catch (Exception localException11) {
			}
			try {
				localStatement.close();
				localStatement = null;
			} catch (Exception localException12) {
			}
			try {
				localConnection.close();
				localConnection = null;
			} catch (Exception localException13) {
			}
		}
		localStringBuffer1.append(paramXMLGenerator
				.closeOutputFile("APSelectCAPS"));
		ExtDBLog.writeLog(paramXMLParser.toString());
		ExtDBLog.writeLog(localStringBuffer1.toString());
		return localStringBuffer1.toString();
	}
	
	public void LogFilenew(String s)
	{
		FileWriter filewriter = null;
		try
		{            
			File flLog = new File(_strFilepath);
			//2MB = 1024*1024*2 = 2097152
			if (flLog.exists() && flLog.length() >= 2097152)
			{
				//flLog.delete();
				flLog.renameTo(new File(_strFilepath+System.currentTimeMillis()));
				flLog = new File(_strFilepath);
			}
			filewriter = new FileWriter(flLog, true);
			filewriter.write(s);
			filewriter.write("\n");
			filewriter.flush();
		}
		catch (IOException exception)
		{
			exception.printStackTrace();
		}
		finally
		{
			if (filewriter != null)
			{
				try
				{
					filewriter.close();
				}
				catch (IOException ex)
				{
				}
			}
		}
	}    
	
	public String APSelect(Connection connection, XMLParser xmlparser,
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


			while(rs.next())
			{
				outStrBuff.append("\n\t\t<tr>");	
				for(int i=1;i<=iNoOfCols;i++)
				{
					outStrBuff.append("\n\t\t\t<td>");	
					outStrBuff.append(removeNULL(rs.getString(i)));
					outStrBuff.append("</td>");	
				}
				outStrBuff.append("\n\t\t</tr>");		
				iTotalRetrieved = iTotalRetrieved  + 1;
			}
			sOut = outStrBuff.toString();
			sOut = "\n\t<Records>" + sOut + "\n\t</Records>";
			sOut = sOut + "\n\t<TotalRetrieved>"  + iTotalRetrieved  + "</TotalRetrieved>";
			strBuff.append(
					"<Exception>\n<MainCode>0</MainCode>\n</Exception>\n");
			strBuff.append("<Output>" + sOut + "\n</Output>");
		} catch (SQLException sqlE) {
			strBuff.append("<Exception>\n<MainCode>" +
					sqlE.getErrorCode() +
					"</MainCode>\n</Exception>\n");
			strBuff.append("<Output>" + sqlE.getMessage() +
					"\n</Output>\n");
		} catch (Exception exception3) {
			strBuff.append(
					"<Exception>\n<MainCode>9</MainCode>\n</Exception>\n");
			strBuff.append("<Output>" + exception3.toString() +
					"\n</Output>\n");
		} finally {
			try {
				rs.close();
			} catch (Exception ext) {}
			try {
				stmt.close();
			} catch (Exception ext) {}
		}
		strBuff.append(xmlgenerator.closeOutputFile("APSelect"));
		ExtDBLog.writeLog(xmlparser.toString());
		ExtDBLog.writeLog(strBuff.toString());
		return strBuff.toString();
	}

	/**
	 * APInsert
	 *
	 * @param connection Connection
	 * @param xmlparser XMLParser
	 * @param xmlgenerator XMLGenerator
	 * @return String
	 */
	/*public String APInsert(Connection connection, XMLParser xmlparser,
			XMLGenerator xmlgenerator) {
		Statement statement = null;
		StringBuffer strBuff = new StringBuffer();

		strBuff.append(xmlgenerator.createOutputFile("APInsert"));
		try {
			boolean bValidSession = false;
			int iSessionId = xmlparser.getIntOf("SessionId", 0, false);
			bValidSession = ValidateSession(connection, iSessionId);
			if(bValidSession)
			{
				String tableName = xmlparser.getValueOf("TableName", "", false);
				String column = xmlparser.getValueOf("ColName", "", false);
				String values = xmlparser.getValueOf("Values", "", false);
				
				if(!"".equalsIgnoreCase(values)){
					values =values+",'blank_check'";//Deepak last element added to handale blank vale in last
					values = values.substring(1,values.length()-1);
					String [] Values_arr = values.split("','");
					values="";
					for(int paramlen = 0;paramlen < Values_arr.length-1;paramlen++){
						if(paramlen==0){
							values = values+"'"+Values_arr[paramlen].replaceAll("'", "''")+"'";
						}
						else{
							values = values+",'"+Values_arr[paramlen].replaceAll("'", "''")+"'";
						}
					}
				}
				
				String query = "Insert into " + tableName + "(" + column +
						") values" + "(" + values + ")";
				statement = connection.createStatement();
				int iReturn = statement.executeUpdate(query);
				strBuff.append(
						"<Exception>\n<MainCode>0</MainCode>\n</Exception>\n");
				strBuff.append("<Output>" + iReturn + "</Output>");
			}
			else
			{
				strBuff.append("<Exception>\n<MainCode>11</MainCode>\n</Exception>\n");
				strBuff.append("<Output>Invalid Session.\n</Output>\n");
			}
		} catch (SQLException sqlE) {
			strBuff.append("<Exception>\n<MainCode>" +
					sqlE.getErrorCode() +
					"</MainCode>\n</Exception>\n");
			strBuff.append("<Output>" + sqlE.getMessage() +
					"\n</Output>\n");
		} catch (Exception exception3) {
			strBuff.append(
					"<Exception>\n<MainCode>9</MainCode>\n</Exception>\n");
			strBuff.append("<Output>" + exception3.toString() +
					"\n</Output>\n");
		} finally {
			try {
				statement.close();
			} catch (Exception ext) {}
		}
		strBuff.append(xmlgenerator.closeOutputFile("APInsert"));
		ExtDBLog.writeLog(xmlparser.toString());
		ExtDBLog.writeLog(strBuff.toString());
		return strBuff.toString();
	}*/
	public String APInsert(Connection connection, XMLParser xmlparser,
			XMLGenerator xmlgenerator) {
		Statement statement = null;
		StringBuffer strBuff = new StringBuffer();

		strBuff.append(xmlgenerator.createOutputFile("APInsert"));
		try {
			boolean bValidSession = false;
			int iSessionId = xmlparser.getIntOf("SessionId", 0, false);
			bValidSession = ValidateSession(connection, iSessionId);
			if(bValidSession)
			{
				String tableName = xmlparser.getValueOf("TableName", "", false);
				String column = xmlparser.getValueOf("ColName", "", false);
				String values = xmlparser.getValueOf("Values", "", false);
				String query = "Insert into " + tableName + "(" + column +
						") values" + "(" + values + ")";
				statement = connection.createStatement();
				int iReturn = statement.executeUpdate(query);
				strBuff.append(
						"<Exception>\n<MainCode>0</MainCode>\n</Exception>\n");
				strBuff.append("<Output>" + iReturn + "</Output>");
			}
			else
			{
				strBuff.append("<Exception>\n<MainCode>11</MainCode>\n</Exception>\n");
				strBuff.append("<Output>Invalid Session.\n</Output>\n");
			}
		} catch (SQLException sqlE) {
			strBuff.append("<Exception>\n<MainCode>" +
					sqlE.getErrorCode() +
					"</MainCode>\n</Exception>\n");
			strBuff.append("<Output>" + sqlE.getMessage() +
					"\n</Output>\n");
		} catch (Exception exception3) {
			strBuff.append(
					"<Exception>\n<MainCode>9</MainCode>\n</Exception>\n");
			strBuff.append("<Output>" + exception3.toString() +
					"\n</Output>\n");
		} finally {
			try {
				statement.close();
			} catch (Exception ext) {}
		}
		strBuff.append(xmlgenerator.closeOutputFile("APInsert"));
		ExtDBLog.writeLog(xmlparser.toString());
		ExtDBLog.writeLog(strBuff.toString());
		return strBuff.toString();
	}

	public String APInsertExtd(Connection paramConnection,XMLParser paramXMLParser, XMLGenerator paramXMLGenerator) 
	{		
		ExtDBLog.writeLog("Input XML :: "+paramXMLParser.toString());
		
		Statement localStatement = null;
		StringBuffer localStringBuffer = new StringBuffer();

		localStringBuffer.append(paramXMLGenerator.createOutputFile("APInsertExtd"));
		try 
		{
			String str1 = paramXMLParser.getValueOf("TableName", "", false);
			String str2 = paramXMLParser.getValueOf("ColName", "", false);
			String str3 = paramXMLParser.getValueOf("Values", "", false);
			String str4 = "Insert into " + str1 + "(" + str2 + ") values" + "("+ str3 + ")";

			ExtDBLog.writeLog("Insert Query in APInsertExtd() :: "+str4);
			System.out.println("Insert Query in APInsertExtd() :: "+str4);
			
			localStatement = paramConnection.createStatement();
			int j = localStatement.executeUpdate(str4);
			localStringBuffer.append("<Exception>\n<MainCode>0</MainCode>\n</Exception>\n");
			localStringBuffer.append("<Output>" + j + "</Output>");
		}
		catch (SQLException e) 
		{
			localStringBuffer.append("<Exception>\n<MainCode>"+ e.getErrorCode() + "</MainCode>\n</Exception>\n");
			localStringBuffer.append("<Output>" + e.getMessage()+ "\n</Output>\n");
		}
		catch (Exception e) 
		{
			localStringBuffer.append("<Exception>\n<MainCode>9</MainCode>\n</Exception>\n");
			localStringBuffer.append("<Output>" + e.toString()+ "\n</Output>\n");
		} finally {
			try {
				if(localStatement!=null){
					localStatement.close();
					localStatement=null;
				}
			} catch (Exception e) {
				ExtDBLog.writeLog("Exception :: "+e.getMessage());
			}
		}
		localStringBuffer.append(paramXMLGenerator.closeOutputFile("APInsertExtd"));		
		ExtDBLog.writeLog("Output XML :: "+localStringBuffer.toString());
		return localStringBuffer.toString();
	}
	
	/**
	 * APUpdate
	 *
	 * @param connection Connection
	 * @param xmlparser XMLParser
	 * @param xmlgenerator XMLGenerator
	 * @return String
	 */
	/*public String APUpdate(Connection connection, XMLParser xmlparser,
			XMLGenerator xmlgenerator) {
		Statement stmt = null;
		StringBuffer strBuff = new StringBuffer();

		strBuff.append(xmlgenerator.createOutputFile("APUpdate"));
		try {
			boolean bValidSession = false;
			int iSessionId = xmlparser.getIntOf("SessionId", 0, false);
			bValidSession = ValidateSession(connection, iSessionId);
			if(bValidSession)
			{
				String tableName = xmlparser.getValueOf("TableName", "", false);
				String ColName_str = xmlparser.getValueOf("ColName", "", false);
				String Values_str = xmlparser.getValueOf("Values", "", false);
				tableName = tableName.replaceAll("'", "''");
				ColName_str = ColName_str.replaceAll("'", "''");
				
				if(!"".equalsIgnoreCase(Values_str)){
					Values_str = Values_str+",'blank_check'";//Deepak last element added to handale blank vale in last
					Values_str = Values_str.substring(1,Values_str.length()-1);
					String [] Values_arr = Values_str.split("','");
					Values_str="";
					for(int paramlen = 0;paramlen < Values_arr.length-1;paramlen++){
						if(paramlen==0){
							Values_str = Values_str+"'"+Values_arr[paramlen].replaceAll("'", "''")+"'";
						}
						else{
							Values_str = Values_str+",'"+Values_arr[paramlen].replaceAll("'", "''")+"'";
						}
					}
				}
				
				
				StringTokenizer columnsTKN = new StringTokenizer(ColName_str, ",");
				StringTokenizer valuesTKN = new StringTokenizer(Values_str, ",");
				String Whereclause = xmlparser.getValueOf("WhereClause", "", false);
				
				Whereclause = Whereclause.toUpperCase();
				if(!"".equalsIgnoreCase(Whereclause)){
					String [] where_str_arr = Whereclause.split(" AND | OR | NOT");
					for(int Wcount = 0;Wcount<where_str_arr.length;Wcount++){
						String ind_val = where_str_arr[Wcount];
						if(!"".equalsIgnoreCase(ind_val)){
							String [] ind_val_arr = ind_val.split("=|<>|>|<|>=|<=|BETWEEN|LIKE");
								for(int Wcount_2 = 0;Wcount_2<ind_val_arr.length;Wcount_2++){
									String var_val = ind_val_arr[Wcount_2].trim();
									if(var_val.startsWith("'")){
										var_val = var_val.substring(1,var_val.length());
									}
									if(var_val.endsWith("'")){
										var_val = var_val.substring(0,var_val.length()-1);
									}
									Whereclause = Whereclause.replaceAll(var_val, var_val.replaceAll("'", "''"));
								}
						}
					}
				}
				
				String query;
				for (query = "Update " + tableName + " set ";
						columnsTKN.hasMoreTokens();
						query = query + columnsTKN.nextToken() + "=" +
								valuesTKN.nextToken() + ",") {
					;
				}
				if (query.endsWith(",")) {
					query = query.substring(0, query.length() - 1);
				}
				query = query + " where " + Whereclause;
				stmt = connection.createStatement();
				int iReturn = stmt.executeUpdate(query);
				strBuff.append(
						"<Exception>\n<MainCode>0</MainCode>\n</Exception>\n");
				strBuff.append("<Output>" + iReturn + "</Output>");
			}
			else
			{
				strBuff.append("<Exception>\n<MainCode>11</MainCode>\n</Exception>\n");
				strBuff.append("<Output>Invalid Session.\n</Output>\n");
			}
		} catch (SQLException sqlE) {
			strBuff.append("<Exception>\n<MainCode>" +
					sqlE.getErrorCode() +
					"</MainCode>\n</Exception>\n");
			strBuff.append("<Output>" + sqlE.getMessage() +
					"\n</Output>\n");
		} catch (Exception exception3) {
			strBuff.append(
					"<Exception>\n<MainCode>9</MainCode>\n</Exception>\n");
			strBuff.append("<Output>" + exception3.toString() +
					"\n</Output>\n");
		} finally {

			try {
				stmt.close();
			} catch (Exception ext) {}

		}
		strBuff.append(xmlgenerator.closeOutputFile("APUpdate"));
		ExtDBLog.writeLog(xmlparser.toString());
		ExtDBLog.writeLog(strBuff.toString());
		return strBuff.toString();
	}*/
	public String APUpdate(Connection connection, XMLParser xmlparser,
			XMLGenerator xmlgenerator) {
		Statement stmt = null;
		StringBuffer strBuff = new StringBuffer();

		strBuff.append(xmlgenerator.createOutputFile("APUpdate"));
		try {
			boolean bValidSession = false;
			int iSessionId = xmlparser.getIntOf("SessionId", 0, false);
			bValidSession = ValidateSession(connection, iSessionId);
			if(bValidSession)
			{
				String tableName = xmlparser.getValueOf("TableName", "", false);
				StringTokenizer columnsTKN = new StringTokenizer(
						xmlparser.
						getValueOf("ColName", "", false), ",");
				StringTokenizer valuesTKN = new StringTokenizer(
						xmlparser.
						getValueOf("Values", "", false), ",");
				String s15 = xmlparser.getValueOf("WhereClause", "", false);
				String query;
				for (query = "Update " + tableName + " set ";
						columnsTKN.hasMoreTokens();
						query = query + columnsTKN.nextToken() + "=" +
								valuesTKN.nextToken() + ",") {
					;
				}
				if (query.endsWith(",")) {
					query = query.substring(0, query.length() - 1);
				}
				query = query + " where " + s15;
				ExtDBLog.writeLog("query for update: "+query);
				stmt = connection.createStatement();
				int iReturn = stmt.executeUpdate(query);
				strBuff.append(
						"<Exception>\n<MainCode>0</MainCode>\n</Exception>\n");
				strBuff.append("<Output>" + iReturn + "</Output>");
			}
			else
			{
				strBuff.append("<Exception>\n<MainCode>11</MainCode>\n</Exception>\n");
				strBuff.append("<Output>Invalid Session.\n</Output>\n");
			}
		} catch (SQLException sqlE) {
			strBuff.append("<Exception>\n<MainCode>" +
					sqlE.getErrorCode() +
					"</MainCode>\n</Exception>\n");
			strBuff.append("<Output>" + sqlE.getMessage() +
					"\n</Output>\n");
		} catch (Exception exception3) {
			strBuff.append(
					"<Exception>\n<MainCode>9</MainCode>\n</Exception>\n");
			strBuff.append("<Output>" + exception3.toString() +
					"\n</Output>\n");
		} finally {

			try {
				stmt.close();
			} catch (Exception ext) {}

		}
		strBuff.append(xmlgenerator.closeOutputFile("APUpdate"));
		ExtDBLog.writeLog(xmlparser.toString());
		ExtDBLog.writeLog(strBuff.toString());
		return strBuff.toString();
	}
	/**
	 * APDelete
	 *
	 * @param connection Connection
	 * @param xmlparser XMLParser
	 * @param xmlgenerator XMLGenerator
	 * @return String
	 */
	private String APDelete(Connection connection, XMLParser xmlparser,
			XMLGenerator xmlgenerator) {
		Statement stmt = null;
		StringBuffer strBuff = new StringBuffer();

		strBuff.append(xmlgenerator.createOutputFile("APDelete"));
		try {
			boolean bValidSession = false;
			int iSessionId = xmlparser.getIntOf("SessionId", 0, false);
			bValidSession = ValidateSession(connection, iSessionId);
			if(bValidSession)
			{
				String tableName = xmlparser.getValueOf("TableName", "", false);
				String whereClause = xmlparser.getValueOf("WhereClause", "", false);
				String query = "Delete from " + tableName + " where " + whereClause;
				stmt = connection.createStatement();
				int iReturn = stmt.executeUpdate(query);
				strBuff.append(
						"<Exception>\n<MainCode>0</MainCode>\n</Exception>\n");
				strBuff.append("<Output>" + iReturn + "</Output>");
			}
			else
			{
				strBuff.append("<Exception>\n<MainCode>11</MainCode>\n</Exception>\n");
				strBuff.append("<Output>Invalid Session.\n</Output>\n");
			}
		} catch (SQLException sqlE) {
			strBuff.append("<Exception>\n<MainCode>" +
					sqlE.getErrorCode() +
					"</MainCode>\n</Exception>\n");
			strBuff.append("<Output>" + sqlE.getMessage() +
					"\n</Output>\n");
		} catch (Exception exception3) {
			strBuff.append(
					"<Exception>\n<MainCode>9</MainCode>\n</Exception>\n");
			strBuff.append("<Output>" + exception3.toString() +
					"\n</Output>\n");
		} finally {

			try {
				stmt.close();
			} catch (Exception ext) {}
		}
		strBuff.append(xmlgenerator.closeOutputFile("APDelete"));
		ExtDBLog.writeLog(xmlparser.toString());
		ExtDBLog.writeLog(strBuff.toString());
		return strBuff.toString();
	}
	
	private String APProcedure(Connection connection, XMLParser xmlparser,
			XMLGenerator xmlgenerator) {
		ResultSet resultset1 = null;
		CallableStatement cstmt = null;
		StringBuffer strBuff = new StringBuffer();
		StringBuffer outStrBuff = new StringBuffer();
		strBuff.append(xmlgenerator.createOutputFile("APProcedure"));
		String procNameToCheck = "";
		try {
			procNameToCheck = xmlparser.getValueOf("ProcName", "", false);
			
		}catch(Exception exception)
		{
			
		}
		
		if(procNameToCheck.equalsIgnoreCase("RB_REQUESTTYPE") || procNameToCheck.equalsIgnoreCase("RB_GETFORMFIELDS")){
		
			try {
				boolean bValidSession = false;
				int iSessionId = xmlparser.getIntOf("SessionId", 0, false);
				bValidSession = ValidateSession(connection, iSessionId);
				if (bValidSession) {
					String procudure = xmlparser.getValueOf("ProcName", "", false)
							.trim().toUpperCase();
					String param = xmlparser.getValueOf("Params", "", true);
					String sQuery = "{call " + procudure + "(" + param + ")}";
					cstmt = connection.prepareCall(sQuery);
					cstmt.execute();
					ExtDBLog.writeLog("Stage1");

					if (procudure.equals("RB_REQUESTTYPE")) {
						resultset1 = cstmt.getResultSet();
						outStrBuff.append("<Param2>");
						outStrBuff.append("\n<Records>");
						while (resultset1.next()) {
							outStrBuff.append("\n\t<Record>");
							outStrBuff.append("\n\t\t<REQUESTID>"
									+ resultset1.getString(1) + "</REQUESTID>");
							outStrBuff.append("\n\t\t<REQUESTNAME>"
									+ resultset1.getString(2) + "</REQUESTNAME>");
							outStrBuff.append("\n\t\t<TYPENAME>"
									+ resultset1.getString(3) + "</TYPENAME>");
							outStrBuff.append("\n\t\t<TYPEID>"
									+ resultset1.getString(4) + "</TYPEID>");
							outStrBuff.append("\n\t</Record>");
						}
						outStrBuff.append("\n</Records>");
						outStrBuff.append("</Param2>");
						cstmt.getMoreResults();
					}
					resultset1 = cstmt.getResultSet();
					ExtDBLog.writeLog("Stage2");
					outStrBuff.append("<Param1>");
					outStrBuff.append("\n<Records>");
					if (procudure.equals("RB_REQUESTTYPE")) {
						while (resultset1.next()) {
							outStrBuff.append("\n\t<Record>");
							outStrBuff.append("\n\t\t<BRANCHNAME>"
									+ resultset1.getString(1) + "</BRANCHNAME>");
							outStrBuff.append("\n\t</Record>");
						}

					} else if (procudure.equals("RB_GETFORMFIELDS")) {
						while (resultset1.next()) {
							outStrBuff.append("\n\t<Record>");

							outStrBuff.append("\n\t\t<FIELD_NAME>"
									+ resultset1.getString(1) + "</FIELD_NAME>");
							outStrBuff.append("\n\t\t<FIELD_DESC>"
									+ resultset1.getString(2) + "</FIELD_DESC>");
							outStrBuff.append("\n\t\t<FIELD_TYPE>"
									+ resultset1.getString(3) + "</FIELD_TYPE>");
							outStrBuff.append("\n\t\t<FIELD_LENGTH>"
									+ resultset1.getString(4) + "</FIELD_LENGTH>");
							outStrBuff.append("\n\t</Record>");

						}
					} else {
						outStrBuff.append("\n\t\t Procedure not found");
					}
					outStrBuff.append("\n</Records>");
					outStrBuff.append("</Param1>");
					strBuff
							.append("<Exception>\n<MainCode>0</MainCode>\n</Exception>\n");
					strBuff.append("<Output>" + outStrBuff.toString()
							+ "\n</Output>\n");
					ExtDBLog.writeLog("Stage3");
				} else {
					strBuff
							.append("<Exception>\n<MainCode>11</MainCode>\n</Exception>\n");
					strBuff.append("<Output>Invalid Session.\n</Output>\n");
				}
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
					resultset1.close();
					cstmt.close();

				} catch (Exception ext) {
				}
			}
		}else{
			
				try
			{
				String s = xmlparser.getValueOf("ProcName", "", false);
				String s1 = xmlparser.getValueOf("Params", "", true);
				String s2 = xmlparser.getValueOf("EngineName", "", false);
				String s3 = null;
				String s4 = "";
				if(s.indexOf(".") != -1)
				{
					s3 = s.substring(0, s.indexOf("."));
					s = s.substring(s.indexOf(".") + 1);
				}
				
				ExtDBLog.writeLog("Catalog = " + s3);
				ExtDBLog.writeLog("EngineName = " + s2);
				ExtDBLog.writeLog("Procedure = " + s);
				DatabaseMetaData databasemetadata = connection.getMetaData();
				ExtDBLog.writeLog("Procedure resultset1 = " + resultset1);
				resultset1 = databasemetadata.getProcedures(s3, s2, s);
				ExtDBLog.writeLog("Procedure resultset1 = " + resultset1);
				if(!resultset1.next())
				{
					strBuff.append("<Exception>\n<MainCode>99</MainCode>\n</Exception>\n");
					strBuff.append("<Output>Procedure Not Found</Output>\n");
					resultset1.close();
					resultset1 = null;
				} else
				{
					resultset1.close();
					resultset1 = databasemetadata.getProcedureColumns(s3, s2, s, "%");
					do
					{
						if(!resultset1.next())
							break;
						ExtDBLog.writeLog("Type = " + resultset1.getString("TYPE_NAME"));
						if(resultset1.getInt("COLUMN_TYPE") == 4)
						{
							if(resultset1.getString("TYPE_NAME").equals("REF CURSOR"))
								s4 = s4 + "RS,";
							else
								if(resultset1.getString("TYPE_NAME").equals("NUMBER"))
									s4 = s4 + "N,";
								else
									if(resultset1.getString("TYPE_NAME").equals("VARCHAR2"))
										s4 = s4 + "S,";
							if(s1.equals(""))
								s1 = s1 + "?";
							else
								s1 = s1 + ",?";
						}
					} while(true);
					resultset1.close();
					resultset1 = null;
					databasemetadata = null;
					s = xmlparser.getValueOf("ProcName", "", false).trim().toUpperCase();
					String s6 = "{call " + s + "(" + s1 + ")}";
					cstmt = connection.prepareCall(s6);
					ExtDBLog.writeLog("Outparams = " + s4);
					if(!s4.equals(""))
					{
						s4 = s4.substring(0, s4.length() - 1);
						ExtDBLog.writeLog("Outparams = " + s4);
						StringTokenizer stringtokenizer = new StringTokenizer(s4, ",");
						int k = 0;
						do
						{
							if(!stringtokenizer.hasMoreTokens())
								break;
							String s7 = stringtokenizer.nextToken();
							k++;
							if(s7.trim().toUpperCase().equals("RS"))
								cstmt.registerOutParameter(k, -10);
							else
								if(s7.trim().toUpperCase().equals("N"))
									cstmt.registerOutParameter(k, 2);
								else
									if(s7.trim().toUpperCase().equals("S"))
										cstmt.registerOutParameter(k, 12);
						} while(true);
					}
					ExtDBLog.writeLog("Stage1 = " + s4);
					cstmt.execute();
					ExtDBLog.writeLog("Stage2 = " + s4);
					if(!s4.equals(""))
					{
						StringTokenizer stringtokenizer1 = new StringTokenizer(s4, ",");
						int l = 0;
						
						for(; stringtokenizer1.hasMoreTokens(); outStrBuff.append("</Param" + l + ">"))
						{
							String s8 = stringtokenizer1.nextToken();
							l++;
							outStrBuff.append("\n\t<Param" + l + ">");
							if(s8.trim().toUpperCase().equals("RS"))
							{
								resultset1 = (ResultSet)cstmt.getObject(l);
								if(resultset1 == null)
									continue;
								ResultSetMetaData resultset1metadata1 = resultset1.getMetaData();
								int i1 = resultset1metadata1.getColumnCount();
								int j1 = 0;
								outStrBuff.append("\n\t<Records>");
								for(; resultset1.next(); outStrBuff.append("\n\t</Record>"))
								{
									outStrBuff.append("\n\t<Record>");
									for(int k1 = 1; k1 <= i1; k1++)
										outStrBuff.append("\n\t\t<" + resultset1metadata1.getColumnName(k1) + ">" + removeNULL(resultset1.getString(k1)) + "</" + resultset1metadata1.getColumnName(k1) + ">");

									j1++;
								}

								outStrBuff.append("\n\t</Records>");
								outStrBuff.append("\n\t<TotalRetrieved>" + j1 + "</TotalRetrieved>\n\t");
								resultset1.close();
								continue;
							}
							if(s8.trim().toUpperCase().equals("N"))
							{
								outStrBuff.append(cstmt.getLong(l));
								continue;
							}
							if(s8.trim().toUpperCase().equals("S"))
								outStrBuff.append(removeNULL(cstmt.getString(l)));
						}

					}
					strBuff.append("<Exception>\n<MainCode>0</MainCode>\n</Exception>\n");
					strBuff.append("<Output>" + outStrBuff.toString() + "\n</Output>\n");
				}
			}
			catch(SQLException sqlexception)
			{
				strBuff.append("<Exception>\n<MainCode>" + sqlexception.getErrorCode() + "</MainCode>\n</Exception>\n");
				strBuff.append("<Output>" + sqlexception.getMessage() + "\n</Output>\n");
			}
			catch(Exception exception)
			{
				strBuff.append("<Exception>\n<MainCode>9</MainCode>\n</Exception>\n");
				strBuff.append("<Output>" + exception.toString() + "\n</Output>\n");
			}
			finally
			{
				try
				{
					resultset1.close();
					resultset1 = null;
				}
				catch(Exception exception2) { }
				try
				{
					cstmt.close();
					cstmt = null;
				}
				catch(Exception exception3) { }
			}
		}
		strBuff.append(xmlgenerator.closeOutputFile("APProcedure"));
		ExtDBLog.writeLog(xmlparser.toString());
		ExtDBLog.writeLog(strBuff.toString());
		return strBuff.toString();
	}
	
	/*private String APProcedure(Connection connection, XMLParser xmlparser,
			XMLGenerator xmlgenerator) {
		ResultSet resultset1 = null;
		CallableStatement cstmt = null;
		StringBuffer strBuff = new StringBuffer();
		StringBuffer outStrBuff = new StringBuffer();
		strBuff.append(xmlgenerator.createOutputFile("APProcedure"));
		try {
			boolean bValidSession = false;
			int iSessionId = xmlparser.getIntOf("SessionId", 0, false);
			bValidSession = ValidateSession(connection, iSessionId);
			if (bValidSession) {
				String procudure = xmlparser.getValueOf("ProcName", "", false)
						.trim().toUpperCase();
				String param = xmlparser.getValueOf("Params", "", true);
				String sQuery = "{call " + procudure + "(" + param + ")}";
				cstmt = connection.prepareCall(sQuery);
				cstmt.execute();
				ExtDBLog.writeLog("Stage1");

				if (procudure.equals("RB_REQUESTTYPE")) {
					resultset1 = cstmt.getResultSet();
					outStrBuff.append("<Param2>");
					outStrBuff.append("\n<Records>");
					while (resultset1.next()) {
						outStrBuff.append("\n\t<Record>");
						outStrBuff.append("\n\t\t<REQUESTID>"
								+ resultset1.getString(1) + "</REQUESTID>");
						outStrBuff.append("\n\t\t<REQUESTNAME>"
								+ resultset1.getString(2) + "</REQUESTNAME>");
						outStrBuff.append("\n\t\t<TYPENAME>"
								+ resultset1.getString(3) + "</TYPENAME>");
						outStrBuff.append("\n\t\t<TYPEID>"
								+ resultset1.getString(4) + "</TYPEID>");
						outStrBuff.append("\n\t</Record>");
					}
					outStrBuff.append("\n</Records>");
					outStrBuff.append("</Param2>");
					cstmt.getMoreResults();
				}
				resultset1 = cstmt.getResultSet();
				ExtDBLog.writeLog("Stage2");
				outStrBuff.append("<Param1>");
				outStrBuff.append("\n<Records>");
				if (procudure.equals("RB_REQUESTTYPE")) {
					while (resultset1.next()) {
						outStrBuff.append("\n\t<Record>");
						outStrBuff.append("\n\t\t<BRANCHNAME>"
								+ resultset1.getString(1) + "</BRANCHNAME>");
						outStrBuff.append("\n\t</Record>");
					}

				} else if (procudure.equals("RB_GETFORMFIELDS")) {
					while (resultset1.next()) {
						outStrBuff.append("\n\t<Record>");

						outStrBuff.append("\n\t\t<FIELD_NAME>"
								+ resultset1.getString(1) + "</FIELD_NAME>");
						outStrBuff.append("\n\t\t<FIELD_DESC>"
								+ resultset1.getString(2) + "</FIELD_DESC>");
						outStrBuff.append("\n\t\t<FIELD_TYPE>"
								+ resultset1.getString(3) + "</FIELD_TYPE>");
						outStrBuff.append("\n\t\t<FIELD_LENGTH>"
								+ resultset1.getString(4) + "</FIELD_LENGTH>");
						outStrBuff.append("\n\t</Record>");

					}
				} else {
					outStrBuff.append("\n\t\t Procedure not found");
				}
				outStrBuff.append("\n</Records>");
				outStrBuff.append("</Param1>");
				strBuff
						.append("<Exception>\n<MainCode>0</MainCode>\n</Exception>\n");
				strBuff.append("<Output>" + outStrBuff.toString()
						+ "\n</Output>\n");
				ExtDBLog.writeLog("Stage3");
			} else {
				strBuff
						.append("<Exception>\n<MainCode>11</MainCode>\n</Exception>\n");
				strBuff.append("<Output>Invalid Session.\n</Output>\n");
			}
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
				resultset1.close();
				cstmt.close();

			} catch (Exception ext) {
			}
		}
		strBuff.append(xmlgenerator.closeOutputFile("APProcedure"));
		ExtDBLog.writeLog(xmlparser.toString());
		ExtDBLog.writeLog(strBuff.toString());
		return strBuff.toString();
	}*/


	/*private String APProcedure(Connection connection, XMLParser xmlparser, XMLGenerator xmlgenerator)
	{
		ResultSet resultset = null;
		CallableStatement callablestatement = null;
		
		StringBuffer stringbuffer = new StringBuffer();
		StringBuffer stringbuffer1 = new StringBuffer();
		
		stringbuffer.append(xmlgenerator.createOutputFile("APProcedure"));
		try
		{
			String s = xmlparser.getValueOf("ProcName", "", false);
			String s1 = xmlparser.getValueOf("Params", "", true);
			String s2 = xmlparser.getValueOf("EngineName", "", false);
			String s3 = null;
			String s4 = "";
			if(s.indexOf(".") != -1)
			{
				s3 = s.substring(0, s.indexOf("."));
				s = s.substring(s.indexOf(".") + 1);
			}
			
			ExtDBLog.writeLog("Catalog = " + s3);
			ExtDBLog.writeLog("EngineName = " + s2);
			ExtDBLog.writeLog("Procedure = " + s);
			DatabaseMetaData databasemetadata = connection.getMetaData();
			ExtDBLog.writeLog("Procedure resultset = " + resultset);
			resultset = databasemetadata.getProcedures(s3, s2, s);
			ExtDBLog.writeLog("Procedure resultset = " + resultset);
			if(!resultset.next())
			{
				stringbuffer.append("<Exception>\n<MainCode>99</MainCode>\n</Exception>\n");
				stringbuffer.append("<Output>Procedure Not Found</Output>\n");
				resultset.close();
				resultset = null;
			} else
			{
				resultset.close();
				resultset = databasemetadata.getProcedureColumns(s3, s2, s, "%");
				do
				{
					if(!resultset.next())
						break;
					ExtDBLog.writeLog("Type = " + resultset.getString("TYPE_NAME"));
					if(resultset.getInt("COLUMN_TYPE") == 4)
					{
						if(resultset.getString("TYPE_NAME").equals("REF CURSOR"))
							s4 = s4 + "RS,";
						else
							if(resultset.getString("TYPE_NAME").equals("NUMBER"))
								s4 = s4 + "N,";
							else
								if(resultset.getString("TYPE_NAME").equals("VARCHAR2"))
									s4 = s4 + "S,";
						if(s1.equals(""))
							s1 = s1 + "?";
						else
							s1 = s1 + ",?";
					}
				} while(true);
				resultset.close();
				resultset = null;
				databasemetadata = null;
				s = xmlparser.getValueOf("ProcName", "", false).trim().toUpperCase();
				String s6 = "{call " + s + "(" + s1 + ")}";
				callablestatement = connection.prepareCall(s6);
				ExtDBLog.writeLog("Outparams = " + s4);
				if(!s4.equals(""))
				{
					s4 = s4.substring(0, s4.length() - 1);
					ExtDBLog.writeLog("Outparams = " + s4);
					StringTokenizer stringtokenizer = new StringTokenizer(s4, ",");
					int k = 0;
					do
					{
						if(!stringtokenizer.hasMoreTokens())
							break;
						String s7 = stringtokenizer.nextToken();
						k++;
						if(s7.trim().toUpperCase().equals("RS"))
							callablestatement.registerOutParameter(k, -10);
						else
							if(s7.trim().toUpperCase().equals("N"))
								callablestatement.registerOutParameter(k, 2);
							else
								if(s7.trim().toUpperCase().equals("S"))
									callablestatement.registerOutParameter(k, 12);
					} while(true);
				}
				ExtDBLog.writeLog("Stage1 = " + s4);
				callablestatement.execute();
				ExtDBLog.writeLog("Stage2 = " + s4);
				if(!s4.equals(""))
				{
					StringTokenizer stringtokenizer1 = new StringTokenizer(s4, ",");
					int l = 0;
					
					for(; stringtokenizer1.hasMoreTokens(); stringbuffer1.append("</Param" + l + ">"))
					{
						String s8 = stringtokenizer1.nextToken();
						l++;
						stringbuffer1.append("\n\t<Param" + l + ">");
						if(s8.trim().toUpperCase().equals("RS"))
						{
							resultset = (ResultSet)callablestatement.getObject(l);
							if(resultset == null)
								continue;
							ResultSetMetaData resultsetmetadata1 = resultset.getMetaData();
							int i1 = resultsetmetadata1.getColumnCount();
							int j1 = 0;
							stringbuffer1.append("\n\t<Records>");
							for(; resultset.next(); stringbuffer1.append("\n\t</Record>"))
							{
								stringbuffer1.append("\n\t<Record>");
								for(int k1 = 1; k1 <= i1; k1++)
									stringbuffer1.append("\n\t\t<" + resultsetmetadata1.getColumnName(k1) + ">" + removeNULL(resultset.getString(k1)) + "</" + resultsetmetadata1.getColumnName(k1) + ">");

								j1++;
							}

							stringbuffer1.append("\n\t</Records>");
							stringbuffer1.append("\n\t<TotalRetrieved>" + j1 + "</TotalRetrieved>\n\t");
							resultset.close();
							continue;
						}
						if(s8.trim().toUpperCase().equals("N"))
						{
							stringbuffer1.append(callablestatement.getLong(l));
							continue;
						}
						if(s8.trim().toUpperCase().equals("S"))
							stringbuffer1.append(removeNULL(callablestatement.getString(l)));
					}

				}
				stringbuffer.append("<Exception>\n<MainCode>0</MainCode>\n</Exception>\n");
				stringbuffer.append("<Output>" + stringbuffer1.toString() + "\n</Output>\n");
			}
		}
		catch(SQLException sqlexception)
		{
			stringbuffer.append("<Exception>\n<MainCode>" + sqlexception.getErrorCode() + "</MainCode>\n</Exception>\n");
			stringbuffer.append("<Output>" + sqlexception.getMessage() + "\n</Output>\n");
		}
		catch(Exception exception)
		{
			stringbuffer.append("<Exception>\n<MainCode>9</MainCode>\n</Exception>\n");
			stringbuffer.append("<Output>" + exception.toString() + "\n</Output>\n");
		}
		finally
		{
			try
			{
				resultset.close();
				resultset = null;
			}
			catch(Exception exception2) { }
			try
			{
				callablestatement.close();
				callablestatement = null;
			}
			catch(Exception exception3) { }
		}
		stringbuffer.append(xmlgenerator.closeOutputFile("APProcedure"));
		ExtDBLog.writeLog(xmlparser.toString());
		ExtDBLog.writeLog(stringbuffer.toString());
		return stringbuffer.toString();
	}*/
	public String WFTestProcedure(Connection connection, XMLParser xmlparser, XMLGenerator xmlgenerator)
	{
		Statement statement = null;
		String s = null;
		
		ResultSet resultset = null;
		try
		{
			String s6 = xmlparser.getValueOf("ProcName", "", false);
			String s7 = xmlparser.getValueOf("Params", "", false);
			String s8 = "";
			s6 = s6.trim();
			String s10 = "{call " + s6 + "(" + s7 + ")}";
			CallableStatement callablestatement = connection.prepareCall(s10);
			callablestatement.execute();
			StringTokenizer stringtokenizer = new StringTokenizer(xmlparser.getValueOf("NoOfCols", "", false), "#");
			do
			{
				resultset = callablestatement.getResultSet();
				int i = Integer.parseInt(stringtokenizer.nextToken());
				while(resultset.next()) 
				{
					for(int j = 0; j < i; j++)
						if(j == 0)
						{
							if(s8.equals(""))
								s8 = resultset.getString(j + 1);
							else
								s8 = s8 + resultset.getString(j + 1);
						} else
						{
							s8 = s8 + "!" + resultset.getString(j + 1);
						}

					s8 = s8 + "~";
				}
				if(s8.endsWith("~"))
					s8 = s8.substring(0, s8.length() - 1);
				s8 = s8 + "#";
				resultset.close();
				resultset = null;
			} while(callablestatement.getMoreResults());
			callablestatement.close();
			callablestatement = null;
			if(s8.endsWith("#"))
				s8 = s8.substring(0, s8.length() - 1);
			s = "<? Xml Version=\"1.0\"?><WFTestProcedure_output><Option>WFTestProcedure</Option><MainCode>0</MainCode><Results>" + s8 + "</Results>" + "</WFTestProcedure_output>";
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
			s = "<? Xml Version=\"1.0\"?><WFTestProcedure_output><Option>WFTestProcedure</Option><MainCode>15</MainCode><Message>" + exception.getMessage() + "</Message>" + "</WFTestProcedure_output>";
		}
		finally
		{
			try
			{
				if(!connection.getAutoCommit())
					connection.setAutoCommit(true);
				if(statement != null)
					statement.close();
				if(resultset != null)
					resultset.close();
			}
			catch(Exception exception2)
			{
				exception2.printStackTrace();
			}
		}
		return s;
	}

    public String SRM_APMQPutGetMessage(Connection connection, XMLParser xmlparser,
			XMLGenerator xmlgenerator) 
	{
		StringBuffer stringbuffer;
		stringbuffer = new StringBuffer();
		ExtDBLog.writeLog("Inside SRM_APMQPutGetMessage");
		//stringbuffer.append(xmlgenerator.createOutputFile("APMQPutGetMessage"));
		String strMsg = "";
		String msg = "";
		String strReturnMsg = "";
		String strRequestType = "";
		String categoryID = "";
		String subCategoryID = "";
		MQMessage message = new MQMessage();
		MQHelper mqHelper = new MQHelper();
		
		//Specific fields for BT and CCC
		String cardStatus="";
		String availableBalance="";
		String availableCashBalance="";
		String requestAmount="";
		String overdueAmount="";
		String serviceType="";
		String serviceSubType="";
		String paymentType="";
		String requestFor="";
		String otherBankCode="";
		String otherBankCard="";
		String otherBankIBAN="";
		String beneficiaryName="";
		String marketingCode="";
		String blockAmount="";
		String cardExpiry="";
	    String transactionType="";
		String remarks="";
		String debitAuthId="";
		String trnRqUID="";
		String approvalId="";
		String merchantCode="";
		String processingCode="";
		String freefield1="";
		
		String PaymentMode="";
		String OtherBankAcctId="";
		String CIFID="";

		try{
			strRequestType = xmlparser.getValueOf("RequestType", "", false); //"DEBITCARD";
			try{
				categoryID = xmlparser.getValueOf("CategoryID", "", false); //"DEBITCARD";
				subCategoryID = xmlparser.getValueOf("SubCategoryID", "", false); //"DEBITCARD";
			}
			catch(Exception e)
			{
				ExtDBLog.writeLog("CategoryID :"+categoryID+"exception in getting category id");
				ExtDBLog.writeLog("SubCategoryID :"+subCategoryID+"exception in getting subcategory id");
			}
			ExtDBLog.writeLog("strRequestType :"+strRequestType);
			ExtDBLog.writeLog("categoryID :"+categoryID);
			ExtDBLog.writeLog("subCategoryID :"+subCategoryID);
			
			if (strRequestType.equalsIgnoreCase(WFCustomBean.LOAN)) {
				strAgrID = xmlparser.getValueOf("AgreementNo", "", false);
			}else if(strRequestType.equalsIgnoreCase(WFCustomBean.DEBITCARD)){
				strAccNo = xmlparser.getValueOf("AccountNo", "", false);
			}else if(strRequestType.equalsIgnoreCase(WFCustomBean.REDEEM)){
				strCardNo = xmlparser.getValueOf("CardNumber", "", false);
				rewardPoints = xmlparser.getValueOf("RewardPoints", "", false);
				
			}else if(strRequestType.equalsIgnoreCase(WFCustomBean.ADJUST)){
				strCardNo = xmlparser.getValueOf("CardNumber", "", false);
				rewardPoints = xmlparser.getValueOf("RewardPoints", "", false);
			}else if(strRequestType.equalsIgnoreCase(WFCustomBean.FORFEIT)){
				strCardNo = xmlparser.getValueOf("CardNumber", "", false);
				rewardPoints = xmlparser.getValueOf("RewardPoints", "", false);	
			}else if(strRequestType.equalsIgnoreCase(WFCustomBean.CARDMAINTENANCE)){
				strCardNo = xmlparser.getValueOf("CardNumber", "", false);
				strBlockRequest = xmlparser.getValueOf("ReasonCode", "", false);
			}else if(strRequestType.equalsIgnoreCase(WFCustomBean.FUNDBLOCK)){
				strCardNo = xmlparser.getValueOf("CardNumber", "", false);
				blockAmount = xmlparser.getValueOf("Amount", "", false);
				transactionType = xmlparser.getValueOf("TrnType", "", false);
				cardExpiry = xmlparser.getValueOf("CardExpiryDate", "", false);
				merchantCode = xmlparser.getValueOf("MerchantCode", "", false);
				processingCode = xmlparser.getValueOf("ProcessingCode", "", false);
				freefield1 = xmlparser.getValueOf("FreeField1", "", false);
				try{
					if(transactionType.equals("Reversal"))
					{
						debitAuthId = xmlparser.getValueOf("DebitAuthId", "", false);
						trnRqUID = xmlparser.getValueOf("TrnRqUID", "", false);
						approvalId = xmlparser.getValueOf("ApprovalId", "", false);
					}
				}
				catch(Exception e){ExtDBLog.writeLog("No value in reversal mandatory parameters--exception caught");}
				
				try{
					remarks = xmlparser.getValueOf("Remarks", "", false);
				}
				catch(Exception e){ExtDBLog.writeLog("No value in remarks--exception caught");}
				
				mqHelper.setCardServiceEligibilityFields(cardStatus,availableBalance,availableCashBalance,requestAmount,overdueAmount,serviceType,serviceSubType,paymentType,requestFor,otherBankCode,otherBankCard,otherBankIBAN,beneficiaryName,marketingCode,blockAmount,transactionType,cardExpiry,remarks,debitAuthId,trnRqUID,approvalId,merchantCode,processingCode,freefield1,PaymentMode,OtherBankAcctId,CIFID);
			}else if(strRequestType.equalsIgnoreCase(WFCustomBean.BALANCEENQ)){
				strCardNo = xmlparser.getValueOf("CardNumber", "", false);
				strCardType = xmlparser.getValueOf("CardType", "", false);
			}else if(strRequestType.equalsIgnoreCase(WFCustomBean.CSE)){
				strCardNo = xmlparser.getValueOf("CardNumber", "", false);
				cardStatus = xmlparser.getValueOf("CardStatus", "", false);
				availableBalance = xmlparser.getValueOf("AvailableBalance", "", false);
				availableCashBalance = xmlparser.getValueOf("AvailableCashBalance", "", false);
				//requestAmount = xmlparser.getValueOf("RequestAmount", "", false);
				overdueAmount = xmlparser.getValueOf("OverdueAmount", "", false);
				serviceType = xmlparser.getValueOf("ServiceType", "", false);
				serviceSubType = xmlparser.getValueOf("ServiceSubType", "", false);
				if("CCC".equals(serviceSubType))
					paymentType = xmlparser.getValueOf("PaymentType", "", false);
				requestFor = xmlparser.getValueOf("RequestFor", "", false);
				ExtDBLog.writeLog("I am here Aishwarya"+requestFor);
				//otherBankCode = xmlparser.getValueOf("otherBankCode", "", false);
				//otherBankCard = xmlparser.getValueOf("otherBankCard", "", false);
				//otherBankIBAN = xmlparser.getValueOf("otherBankIBAN", "", false);
				//beneficiaryName = xmlparser.getValueOf("beneficiaryName", "", false);
				//marketingCode = xmlparser.getValueOf("marketingCode", "", false);
				mqHelper.setCardServiceEligibilityFields(cardStatus,availableBalance,availableCashBalance,requestAmount,overdueAmount,serviceType,serviceSubType,paymentType,requestFor,otherBankCode,otherBankCard,otherBankIBAN,beneficiaryName,marketingCode,blockAmount,transactionType,cardExpiry,remarks,debitAuthId,trnRqUID,approvalId,merchantCode,processingCode,freefield1,PaymentMode,OtherBankAcctId,CIFID);
				
			}else if(strRequestType.equalsIgnoreCase(WFCustomBean.CSEOB)){
				strCardNo = xmlparser.getValueOf("CardNumber", "", false);
				 
				//cifid passed in Eligibility validation for all SubCategory as part of smart cash
				try {
					CIFID = xmlparser.getValueOf("CIFID", "", false);  // Passed for SmartCash
				}catch(Exception e)
				{
					ExtDBLog.writeLog("CifId :"+CIFID+"exception in getting CifId");
				}
				
				//cardStatus = xmlparser.getValueOf("CardStatus", "", false);
				//availableBalance = xmlparser.getValueOf("AvailableBalance", "", false);
				//availableCashBalance = xmlparser.getValueOf("AvailableCashBalance", "", false);
				requestAmount = xmlparser.getValueOf("RequestAmount", "", false);
				//overdueAmount = xmlparser.getValueOf("OverdueAmount", "", false);
				serviceType = xmlparser.getValueOf("ServiceType", "", false);
				serviceSubType = xmlparser.getValueOf("ServiceSubType", "", false);
				//if("CCC".equals(serviceSubType))
					//paymentType = xmlparser.getValueOf("PaymentType", "", false);
				requestFor = xmlparser.getValueOf("RequestFor", "", false);
				//ExtDBLog.writeLog("I am here Aishwarya"+requestFor);
				//otherBankCode = xmlparser.getValueOf("otherBankCode", "", false);
				if("BT".equals(serviceSubType))
					otherBankCard = xmlparser.getValueOf("OtherBankCard", "", false);
				//otherBankIBAN = xmlparser.getValueOf("otherBankIBAN", "", false);
				beneficiaryName = xmlparser.getValueOf("BeneficiaryName", "", false);
				marketingCode = xmlparser.getValueOf("MarketingCode", "", false);
				
				try {
					PaymentMode = xmlparser.getValueOf("PaymentMode", "", false);  // Passed for SmartCash
				}catch(Exception e)
				{
					ExtDBLog.writeLog("PaymentMode :"+PaymentMode+"exception in getting PaymentMode");
				}
				
				try{
					OtherBankAcctId = xmlparser.getValueOf("OtherBankAcctId", "", false);  // Passed for SmartCash
				}catch(Exception e)
				{
					ExtDBLog.writeLog("OtherBankAcctId :"+OtherBankAcctId+"exception in getting OtherBankAcctId");
				}
				
				mqHelper.setCardServiceEligibilityFields(cardStatus,availableBalance,availableCashBalance,requestAmount,overdueAmount,serviceType,serviceSubType,paymentType,requestFor,otherBankCode,otherBankCard,otherBankIBAN,beneficiaryName,marketingCode,blockAmount,transactionType,cardExpiry,remarks,debitAuthId,trnRqUID,approvalId,merchantCode,processingCode,freefield1,PaymentMode,OtherBankAcctId,CIFID);				
			}else if(strRequestType.equalsIgnoreCase(WFCustomBean.CARDS) || strRequestType.equalsIgnoreCase(WFCustomBean.CARDLIST))
				{
					ExtDBLog.writeLog("Got the request type as " + WFCustomBean.CARDS);
					
					if(categoryID.equals("1") && subCategoryID.equals("3"))
						return SRM_APMQPutGetMessage_CARDS_BOC(connection,xmlparser,xmlgenerator,strRequestType, mqHelper);
					else if(categoryID.equals("1") && (subCategoryID.equals("2") || subCategoryID.equals("4") || subCategoryID.equals("5")))  // subCategoryID = 5 is added for Smart Cash on 15012018 by Angad
						return SRM_APMQPutGetMessage_CARDS_BT_CCC(connection,xmlparser,xmlgenerator,strRequestType, mqHelper);
					else
						return SRM_APMQPutGetMessage_CARDS(connection,xmlparser,xmlgenerator,strRequestType, mqHelper);
				}
			strUserID = xmlparser.getValueOf("UserID", "", false);
			ExtDBLog.writeLog("XMLParser values :strUserID " + strUserID+" strAgrID: "+strAgrID+" strAccNo: "+strAccNo);

			// Format message

			//if(strRequestType.equalsIgnoreCase("Redeem") || strRequestType.equalsIgnoreCase("Adjust") || strRequestType.equalsIgnoreCase("Forfeit"))
			//{
				if(!(strRequestType.equalsIgnoreCase(WFCustomBean.BALANCEENQ) || strRequestType.equalsIgnoreCase(WFCustomBean.CARDMAINTENANCE) || strRequestType.equalsIgnoreCase(WFCustomBean.CSE) || strRequestType.equalsIgnoreCase(WFCustomBean.CSEOB) || strRequestType.equalsIgnoreCase(WFCustomBean.FUNDBLOCK)))
					strCardNo = AesUtil.Decrypt(strCardNo);
				ExtDBLog.writeLog("Decrypted Card Number for: strRequestType " + strCardNo);
				strMsg=mqHelper.getMQRequestXML(connection,xmlparser,xmlgenerator,strRequestType,strCardNo, strCardType, strBlockRequest);
			//}
			
			//Enter code for getting request xml in case of other calls except redeem and adjust
			
			ExtDBLog.writeLog("Formatted Message:" + strMsg.toString());
			WriteCustomLog("Message to MW " + strMsg.toString());

			ExtDBLog.writeLog("MessageID:" + message.messageId);
		
			mqHelper.setMQDetails(connection,xmlparser,xmlgenerator);
			strReturnMsg = mqHelper.getMQResponse(strMsg);
			return strReturnMsg;
			
			} catch (Exception e) {
			ExtDBLog.writeLog("Exception in getting response from MQHelper "+e.getMessage());
			return "<Output><Description>Error during getting response from MQHelper</Description>\n<Exception>\n<ReasonCode>-1</ReasonCode>\n</Exception></Output>";
			} 

	}
	
	public String AO_APMQPutGetMessage(Connection connection, XMLParser xmlparser,
			XMLGenerator xmlgenerator) 
	{
		StringBuffer stringbuffer;
		stringbuffer = new StringBuffer();
		ExtDBLog.writeLog("Inside AO_APMQPutGetMessage");
		//stringbuffer.append(xmlgenerator.createOutputFile("APMQPutGetMessage"));
		String strMsg = "";
		String msg = "";
		String strReturnMsg = "";
		String strRequestType = "";
		String CIFID = "";
		String SRNo = "";
		MQMessage message = new MQMessage();
		MQHelper mqHelper = new MQHelper();


		try{
			strRequestType = xmlparser.getValueOf("RequestType", "", false); 

			ExtDBLog.writeLog("strRequestType :"+strRequestType);
			
			if (strRequestType.equalsIgnoreCase(WFCustomBean.AOFETCH)) {
				CIFID = xmlparser.getValueOf("CIFID", "", false);
				SRNo = xmlparser.getValueOf("SRNo", "", false);
				mqHelper.setAccountNoFetchFields(CIFID,SRNo);
			}
			strUserID = xmlparser.getValueOf("UserID", "", false);
			ExtDBLog.writeLog("XMLParser values :strUserID " + strUserID+" CIF: "+CIFID+" SRNumber :"+SRNo);

			strMsg=mqHelper.getMQRequestXML(connection,xmlparser,xmlgenerator,strRequestType,"", "", "");
			
			ExtDBLog.writeLog("Formatted Message:" + strMsg.toString());
			WriteCustomLog("Message to MW " + strMsg.toString());

			ExtDBLog.writeLog("MessageID:" + message.messageId);
		
			mqHelper.setMQDetails(connection,xmlparser,xmlgenerator);
			strReturnMsg = mqHelper.getMQResponse(strMsg);
			return strReturnMsg;
			
			} catch (Exception e) {
			ExtDBLog.writeLog("Exception in getting response from MQHelper "+e.getMessage());
			return "<Output><Description>Error during getting response from MQHelper</Description>\n<Exception>\n<ReasonCode>-1</ReasonCode>\n</Exception></Output>";
			} 

	}
	//Function Added for TT process
	public String TT_APMQPutGetMessage(Connection connection, XMLParser xmlparser,
			XMLGenerator xmlgenerator) 
	{
		StringBuffer stringbuffer;
		stringbuffer = new StringBuffer();
		ExtDBLog.writeLog("Inside TT_APMQPutGetMessage");
		//stringbuffer.append(xmlgenerator.createOutputFile("APMQPutGetMessage"));
		String strMsg = "";
		String msg = "";
		String strReturnMsg = "";
		String strRequestType = "";
		String accountNumber = "";
		String creditCurCode = "";
		String debitCurCode = "";
		String cifID = "";
		String trAmount = "";
		String trCurrency = "";
		MQMessage message = new MQMessage();
		MQHelper mqHelper = new MQHelper();


		try{
			strRequestType = xmlparser.getValueOf("RequestType", "", false); 

			ExtDBLog.writeLog("strRequestType :"+strRequestType);
			
			if (strRequestType.equalsIgnoreCase("ENTITY_DETAILS")) {
				accountNumber = xmlparser.getValueOf("AccountNumber", "", false);
				strUserID = xmlparser.getValueOf("UserID", "", false);
				ExtDBLog.writeLog("XMLParser values :strUserID " + strUserID+" Account Number: "+accountNumber);
				mqHelper.setAccountNoTTField(accountNumber);
			}
			else if (strRequestType.equalsIgnoreCase("ACCOUNT_DETAILS")) {
				accountNumber = xmlparser.getValueOf("AccountNumber", "", false);
				strUserID = xmlparser.getValueOf("UserID", "", false);
				ExtDBLog.writeLog("XMLParser values :strUserID " + strUserID+" Account Number: "+accountNumber);
				mqHelper.setAccountNoTTField(accountNumber);
			}
			else if (strRequestType.equalsIgnoreCase("EXCHANGE_RATE_DETAILS")) {
				creditCurCode = xmlparser.getValueOf("transCurrType", "", false);
				debitCurCode = xmlparser.getValueOf("remitCurrType", "", false);
				cifID = xmlparser.getValueOf("cifID", "", false);
				trAmount = xmlparser.getValueOf("trAmount", "", false);
				trCurrency = xmlparser.getValueOf("trCurrency", "", false);
				strUserID = xmlparser.getValueOf("UserID", "", false);
				ExtDBLog.writeLog("XMLParser values :strUserID -" + strUserID+" CreditCurCode- "+creditCurCode+" CIFID -"+cifID+" trAmount -"+trAmount+" trCurrency- "+trCurrency);
				mqHelper.setExchangeRateTTField(creditCurCode,debitCurCode,cifID,trAmount,trCurrency);
			}
			else if (strRequestType.equalsIgnoreCase("SIGNATURE_DETAILS")) {
				accountNumber = xmlparser.getValueOf("AccountNumber", "", false);
				strUserID = xmlparser.getValueOf("UserID", "", false);
				ExtDBLog.writeLog("XMLParser values :strUserID " + strUserID+" Account Number: "+accountNumber);
				mqHelper.setAccountNoTTField(accountNumber);
			}
			else if (strRequestType.equalsIgnoreCase("MEMOPAD_DETAILS")) {
				accountNumber = xmlparser.getValueOf("AccountNumber", "", false);
				strUserID = xmlparser.getValueOf("UserID", "", false);
				ExtDBLog.writeLog("XMLParser values :strUserID " + strUserID+" Account Number: "+accountNumber);
				mqHelper.setAccountNoTTField(accountNumber);
			}
			

			strMsg=mqHelper.getMQRequestXML(connection,xmlparser,xmlgenerator,strRequestType,"", "", "");
			
			ExtDBLog.writeLog("Formatted Message:" + strMsg.toString());
			WriteCustomLog("Message to MW " + strMsg.toString());

			ExtDBLog.writeLog("MessageID:" + message.messageId);
		
			mqHelper.setMQDetails(connection,xmlparser,xmlgenerator);
			strReturnMsg = mqHelper.getMQResponse(strMsg);
			return strReturnMsg;
			
			} catch (Exception e) {
			ExtDBLog.writeLog("Exception in getting response from MQHelper "+e.getMessage());
			return "<Output><Description>Error during getting response from MQHelper</Description>\n<Exception>\n<ReasonCode>-1</ReasonCode>\n</Exception></Output>";
			} 

	}
	public String TT_APMQPutGetMessageDirect(Connection connection, XMLParser xmlparser,
			XMLGenerator xmlgenerator) 
	{

		String requestHeader ="";
		String requestBody="";
		String requestMessage="";
		String strRequestType="";
		String strReturnMsg="";
		
		ExtDBLog.writeLog("Inside TT_APMQPutGetMessageDirect");
		java.util.Date d = new java.util.Date();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.mmm");
		String extra2 = sdf1.format(d)+"+04:00";
		
		MQMessage message = new MQMessage();
		MQHelper mqHelper = new MQHelper(message);
		mqHelper.readPropertyFile();
		
		String Msg_id = genMessageID();
		
		try{
			strRequestType = xmlparser.getValueOf("RequestType", "", false); 
			requestHeader = "<EE_EAI_MESSAGE>"+
						"<EE_EAI_HEADER>"+
							"<MsgFormat>"+strRequestType+"</MsgFormat>" +
							"<MsgVersion>0001</MsgVersion>" +
							"<RequestorChannelId>BPM</RequestorChannelId>" +
							"<RequestorUserId>BPMUSER</RequestorUserId>" +
							"<RequestorLanguage>E</RequestorLanguage>" +
							"<RequestorSecurityInfo>secure</RequestorSecurityInfo>" +
							"<ReturnCode>0000</ReturnCode>" +
							"<ReturnDesc>success</ReturnDesc>" +
							"<MessageId>"+Msg_id+"</MessageId>" +
							"<Extra1>REQ||SHELL.JOHN</Extra1>"+
							"<Extra2>"+extra2+"</Extra2>"+
						"</EE_EAI_HEADER>";
			requestBody = xmlparser.getValueOf("EE_EAI_BODY", "", false); 
			requestBody = requestBody.replaceAll("&", "&amp;");
			//requestBody = requestBody.replaceAll("\"", "&quot;");
			//requestBody = requestBody.replaceAll("'", "&apos;");
			requestMessage = requestHeader+requestBody+"</EE_EAI_MESSAGE>";
			
			strReturnMsg = mqHelper.getMQResponse(requestMessage);
			return strReturnMsg;
			
			} catch (Exception e) {
			ExtDBLog.writeLog("Exception in getting response from MQHelper "+e.getMessage());
			return "<Output><Description>Error during getting response from MQHelper</Description>\n<Exception>\n<ReasonCode>-1</ReasonCode>\n</Exception></Output>";
			} 

	}
	
	public String BPM_APMQPutGetMessageDirect(Connection connection, XMLParser xmlparser,
			XMLGenerator xmlgenerator) 
	{

		String requestHeader ="";
		String requestBody="";
		String requestMessage="";
		String strRequestType="";
		String strReturnMsg="";
		
		ExtDBLog.writeLog("Inside BPM_APMQPutGetMessageDirect");
		java.util.Date d = new java.util.Date();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.mmm");
		String extra2 = sdf1.format(d)+"+04:00";
		
		MQMessage message = new MQMessage();
		MQHelper mqHelper = new MQHelper(message);
		mqHelper.readPropertyFile();

		String Msg_id = genMessageID();
		
		try{
			requestMessage = xmlparser.getValueOf("RequestMessage", "", false); 
			// this condition added for EIDA_Genuine_Check because it has different headers added on 10-Dec-2017 by Angad
			if (requestMessage.contains("RAKHeader:RequestID")) {  
		        requestMessage = requestMessage.substring(0,requestMessage.indexOf("<RAKHeader:RequestID>")+21)+Msg_id+requestMessage.substring(requestMessage.indexOf("</RAKHeader:RequestID>"));
				
				requestMessage = requestMessage.substring(0,requestMessage.indexOf("<RAKHeader:TimeStampyyyymmddhhmmsss>")+36)+extra2+requestMessage.substring(requestMessage.indexOf("</RAKHeader:TimeStampyyyymmddhhmmsss>"));
		    } else {
				requestMessage = requestMessage.substring(0,requestMessage.indexOf("<MessageId>")+11)+Msg_id+requestMessage.substring(requestMessage.indexOf("</MessageId>"));
				
				requestMessage = requestMessage.substring(0,requestMessage.indexOf("<Extra2>")+8)+extra2+requestMessage.substring(requestMessage.indexOf("</Extra2>"));
				
				// Start - Replacing special characters from MessageId for Cheque Book Request Call Only added on 22042018 by Angad for RAO Process
				String sCallType = (requestMessage.contains("<MsgFormat>")) ? requestMessage.substring(requestMessage.indexOf("<MsgFormat>")+"</MsgFormat>".length()-1,requestMessage.indexOf("</MsgFormat>")):"";
				ExtDBLog.writeLog("Inside BPM_APMQPutGetMessageDirect 1"+sCallType);
				/*if ("CHEQUE_BOOK_ELIGIBILITY".equalsIgnoreCase(sCallType))
				{
					//String strMsgId= message.messageId.toString();
					String strMsgId= Msg_id;
					strMsgId = strMsgId.replace("[", "A").replace("@", "C");
					requestMessage = requestMessage.substring(0,requestMessage.indexOf("<MessageId>")+11)+strMsgId+requestMessage.substring(requestMessage.indexOf("</MessageId>"));
				}*/
				// End - Replacing special characters from MessageId for Cheque Book Request Call Only added on 22042018 by Angad for RAO Process
			}
			requestMessage = requestMessage.replaceAll("&", "&amp;");
			//requestMessage = requestMessage.replaceAll("\"", "&quot;");
			//requestMessage = requestMessage.replaceAll("'", "&apos;");

			strReturnMsg = mqHelper.getMQResponse(requestMessage);
			return strReturnMsg;
			
			} catch (Exception e) {
			ExtDBLog.writeLog("Exception in getting response from MQHelper "+e.getMessage());
			return "<Output><Description>Error during getting response from MQHelper</Description>\n<Exception>\n<ReasonCode>-1</ReasonCode>\n</Exception></Output>";
			} 

	}

    //APDecryptAESString-Added by Amandeep
	public String APDecryptString(Connection connection, XMLParser xmlparser, XMLGenerator xmlgenerator) //AES
	{
		StringBuffer stringbuffer;
        stringbuffer = new StringBuffer();
		stringbuffer.append(xmlgenerator.createOutputFile("APDecryptString"));
		try{	
			stringbuffer.append("<MainCode>0</MainCode><Output>"+AesUtil.Decrypt(xmlparser.getValueOf("StringValue", "", false))+"</Output>");
		}catch(Exception e){
			stringbuffer.append("<MainCode>12321</MainCode><Exception>"+e.getMessage()+"</Exception>");
		}
		stringbuffer.append(xmlgenerator.closeOutputFile("APDecryptString"));
		return stringbuffer.toString();		
	}
	//APEncryptAESString-Added by Aishwarya
	public String APEncryptString(Connection connection, XMLParser xmlparser, XMLGenerator xmlgenerator) //AES
	{
		StringBuffer stringbuffer;
        stringbuffer = new StringBuffer();
		stringbuffer.append(xmlgenerator.createOutputFile("APEncryptString"));
		try{	
			stringbuffer.append("<MainCode>0</MainCode><Output>"+AesUtil.Encrypt(xmlparser.getValueOf("StringValue", "", false))+"</Output>");
		}catch(Exception e){
			stringbuffer.append("<MainCode>12321</MainCode><Exception>"+e.getMessage()+"</Exception>");
		}
		stringbuffer.append(xmlgenerator.closeOutputFile("APEncryptString"));
		return stringbuffer.toString();		
	}    
	
	private boolean ValidateSession(Connection connection, int iSessionId)
	{
		//return true;
		//Code uncommented Deepak 11Dec2018 not to validate session
		
		boolean flag = false;
		try 
		{
			WFParticipant participant = WFSUtil.WFCheckSession(connection,iSessionId);
			flag = (participant != null && (participant.gettype() == 'U' || participant.gettype() == 'P'));
		}
		catch (Exception e) {
			ExtDBLog.writeLog(e.toString());
		}
		return flag;
		
	}
	private String removeNULL(String s)
	{
		if(s == null)
			return "";
		else
			return s.trim();
	}

	/**
	 * FetchViewData
	 *
	 * @param connection Connection
	 * @param xmlparser XMLParser
	 * @param xmlgenerator XMLGenerator
	 * @return String
	 */


	/**
	 * APSelectWithColumnNames
	 *
	 * @param connection Connection
	 * @param xmlparser XMLParser
	 * @param xmlgenerator XMLGenerator
	 * @return String
	 */
	public String APSelectWithColumnNames(Connection connection,
			XMLParser xmlparser, XMLGenerator xmlgenerator) {
		ResultSet rs = null;
		Statement stmt = null;
		ResultSetMetaData objRsmd = null;
		StringBuffer strBuff = new StringBuffer();
		StringBuffer outStrBuff = new StringBuffer();
		StringBuffer strBuffCLOB = new StringBuffer(1024);
		StringBuffer tempStrBuff = null;
		String query = null;
		int iNoOfCols;
		int iTotalRetrieved;
		int jdbcType;
		int len = 0;
		strBuff
				.append(xmlgenerator
						.createOutputFile("APSelectWithColumnNames"));
		try {
			query = xmlparser.getValueOf("Query", "", false);
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			objRsmd = rs.getMetaData();
			iNoOfCols = objRsmd.getColumnCount();
			iTotalRetrieved = 0;
			while (rs.next()) {
				strBuffCLOB = new StringBuffer(1024);
				outStrBuff.append("\n\t\t<Record>");
				for (int i = 1; i <= iNoOfCols; i++) {
					jdbcType = objRsmd.getColumnType(i);
					ExtDBLog.writeLog(objRsmd.getColumnName(i) + " : "
							+ jdbcType);
					if (jdbcType == java.sql.Types.CLOB) {
						ExtDBLog.writeLog(objRsmd.getColumnName(i)
								+ " : is CLOB ");
						java.io.Reader strReader = rs
								.getCharacterStream(objRsmd.getColumnName(i));
						if (!rs.wasNull()) {
							int size = 0;
							char[] buf = null;
							while (true) {
								buf = new char[1024];
								size = strReader.read(buf);
								if (size == -1) {
									break;
								}
								len += size;
								strBuffCLOB.append(new String(buf, 0, size));
							}
							strReader.close();
							ExtDBLog.writeLog("Clob Data : "
									+ strBuffCLOB.toString());
							outStrBuff.append("\n\t\t\t<"
									+ objRsmd.getColumnName(i) + ">");
							outStrBuff.append(strBuffCLOB.toString());
							outStrBuff.append("</" + objRsmd.getColumnName(i)
									+ ">");
						}
					} else {
						outStrBuff.append("\n\t\t\t<"
								+ objRsmd.getColumnName(i) + ">");
						outStrBuff.append(removeNULL(rs.getString(i)));
						outStrBuff
								.append("</" + objRsmd.getColumnName(i) + ">");
					}
				}
				outStrBuff.append("\n\t\t</Record>");
				iTotalRetrieved = iTotalRetrieved + 1;
			}
			/*
			 * sOut = outStrBuff.toString(); sOut = "\n\t<Records>" + sOut +
			 * "\n\t</Records>"; sOut = sOut + "\n\t<TotalRetrieved>" +
			 * iTotalRetrieved + "</TotalRetrieved>"; strBuff.append(
			 * "<Exception>\n<MainCode>0</MainCode>\n</Exception>\n");
			 * strBuff.append("<Output>" + sOut + "\n</Output>");
			 */
			tempStrBuff = new StringBuffer();
			tempStrBuff.append("\n\t<Records>");
			tempStrBuff.append(outStrBuff.toString());
			tempStrBuff.append("\n\t</Records>");
			tempStrBuff.append("\n\t<TotalRetrieved>");
			tempStrBuff.append((iTotalRetrieved + "").trim());
			tempStrBuff.append("</TotalRetrieved>");
			// ExtDBLog.writeLog("tempStrBuff:   "+tempStrBuff);
			strBuff
					.append("<Exception>\n<MainCode>0</MainCode>\n</Exception>\n");
			strBuff.append("<Output>");
			strBuff.append(tempStrBuff.toString());
			strBuff.append("\n</Output>");
		} catch (SQLException sqlE) {
			strBuff.append("<Exception>\n<MainCode>");
			strBuff.append(sqlE.getErrorCode());
			strBuff.append("</MainCode>\n</Exception>\n");
			strBuff.append("<Output>");
			strBuff.append(sqlE.getMessage());
			strBuff.append("\n</Output>\n");
		} catch (Exception exception3) {
			strBuff
					.append("<Exception>\n<MainCode>9</MainCode>\n</Exception>\n");
			strBuff.append("<Output>");
			strBuff.append(exception3.getMessage());
			strBuff.append("\n</Output>\n");
		} finally {
			try {
				rs.close();
				query = null;
				outStrBuff = null;
				tempStrBuff = null;

			} catch (Exception ext) {
				ExtDBLog
						.writeLog("Method : APSelectWithColumnNames, Exception in closing Resultset: "
								+ ext.getMessage());
			}
			try {
				stmt.close();
				outStrBuff = null;
				tempStrBuff = null;
				query = null;
			} catch (Exception ext) {
				ExtDBLog
						.writeLog("Method : APSelectWithColumnNames, Exception in closing Statement: "
								+ ext.getMessage());
			}
		}
		strBuff.append(xmlgenerator.closeOutputFile("APSelectWithColumnNames"));
		// ExtDBLog.writeLog(xmlparser.toString());
		ExtDBLog.writeLog("Output APSelectWithColumnNames: "
				+ strBuff.toString());
		return strBuff.toString();
	}
	
	public String Webservice(Connection connection, XMLParser xmlparser, XMLGenerator xmlgenerator) throws Exception
	{
    	WebServiceHandler wsHandler= new WebServiceHandler();
		String sXmlParser=xmlparser.toString();
		ExtDBLog.writeLog("In Method sXmlParser---"+sXmlParser);
    	String sOutput= wsHandler.callType(sXmlParser);
		ExtDBLog.writeLog("In Method sOutput---"+sOutput);
		return sOutput;
	}
	
	

	/**
	 * APValidateWiExport
	 *
	 * @param connection Connection
	 * @param xmlparser XMLParser
	 * @param xmlgenerator XMLGenerator
	 * @return String
	 */
	public String APValidateWiExport(Connection connection, XMLParser xmlparser,XMLGenerator xmlgenerator) {
		ExtDBLog.writeLog("APValidateWiExport-->RequestXml-->"+xmlparser.toString());
		
		ResultSet rs = null;
		PreparedStatement pStmnt=null;
		
		StringBuilder outXml = new StringBuilder();
		
		//String sOut = "Valid";
		String sOut = "Valid";

		outXml.append(xmlgenerator.createOutputFile("APValidateWiExport"));
		
		try {
			String processName = xmlparser.getValueOf("ProcessName");
			ExtDBLog.writeLog("APValidateWiExport-->processName-->"+processName);
			
			if(processName!=null && processName.trim().length()>0){
			
				ExtDBLog.writeLog("APValidateWiExport-->MIN_DIFF_SELECT_QUERY-->"+MIN_DIFF_SELECT_QUERY);
				
				pStmnt = connection.prepareStatement(MIN_DIFF_SELECT_QUERY);
				pStmnt.setString(1, processName.trim());
				
				rs=pStmnt.executeQuery();
				
				if(rs!=null){
					String diffInMinutes=null;
					String maxDuration=null;
					
					while(rs.next()){
						diffInMinutes=rs.getString("MINUTE_DIFF");
						maxDuration=rs.getString("MAX_DURATION");
					}
					
					ExtDBLog.writeLog("APValidateWiExport-->diffInMinutes-->"+diffInMinutes);
					
					if(diffInMinutes!=null){
						int iDiffInMinutes=getIntValue(diffInMinutes);
						
						if(iDiffInMinutes>=0){
							sOut="Invalid~"+maxDuration;
						}
					}
				}
				outXml.append("<Exception>\n<MainCode>0</MainCode>\n</Exception>\n");
				//outXml.append("<Output>" + sOut + "\n</Output>");
			}else{
				outXml.append("<Exception>\n<MainCode>-1</MainCode>\n</Exception>\n");
				sOut="Process Name not entered.";
			}
			
		} catch (SQLException sqlE) {
			outXml.append("<Exception>\n<MainCode>" +sqlE.getErrorCode() +"</MainCode>\n</Exception>\n");
			//outXml.append("<Output>" + sqlE.getMessage() +"\n</Output>\n");
			sOut=sqlE.getMessage(); 
		} catch (Exception exception3) {
			outXml.append("<Exception>\n<MainCode>9</MainCode>\n</Exception>\n");
			//outXml.append("<Output>" + exception3.toString() +"\n</Output>\n");
			sOut=exception3.toString(); 
		} finally {
			try {
				if(rs!=null){
					rs.close();
					rs=null;
				}
				if(pStmnt!=null){
					pStmnt.close();
					pStmnt=null;
				}
			} catch (Exception e) {
				ExtDBLog.writeLog("Exception in APValidateWiExport-->"+stackTraceToString(e));
			}
		}
		outXml.append("\n<Output>" +sOut+"</Output>\n");
		outXml.append(xmlgenerator.closeOutputFile("APValidateWiExport"));
		//ExtDBLog.writeLog(xmlparser.toString());
		ExtDBLog.writeLog(outXml.toString());
		return outXml.toString();
	}
	
	private int getIntValue(final String str) {
        int result = 0;
        try {
            result = Integer.parseInt(str);
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }
	
	private String stackTraceToString(Throwable e) {
	    StringBuilder sb = new StringBuilder();
	    for (StackTraceElement element : e.getStackTrace()) {
	        sb.append(element.toString());
	        sb.append("\n");
	    }
	    return sb.toString();
	}
//------------------Deepak new method incorporated for SQL -------------------------------------------------------------------------
	
	
	public String APSelectWithNamedParam(Connection connection,	XMLParser xmlparser, XMLGenerator xmlgenerator) 
	{
		ResultSet rs = null;
		NamedParameterStatement stmt = null;
		ResultSetMetaData objRsmd = null;
		StringBuffer strBuff = new StringBuffer();
		StringBuffer outStrBuff = new StringBuffer();
		StringBuffer strBuffCLOB = new StringBuffer(1024);
		StringBuffer tempStrBuff = null;
		String query = null;
		String params = null;
		int iNoOfCols;
		int iTotalRetrieved;
		int jdbcType;
		int len = 0;
		strBuff.append(xmlgenerator.createOutputFile("APSelectWithNamedParam"));
		try 
		{
			query = xmlparser.getValueOf("Query", "", false);
			//Changes done for SQL Injection in APProcedure call.
			if(!query.trim().toUpperCase().startsWith("SELECT"))
			{
				strBuff.append("<Exception>\n<MainCode>9</MainCode>\n</Exception>\n");
				strBuff.append("<Output>Incorrect select query: Unable to find keyword 'SELECT'</Output>\n");
			}
			else
			{
				params = xmlparser.getValueOf("Params", "", false);
				stmt = new NamedParameterStatement(connection, query);
				//Changes done for SQL Injection in select call.
				params = params.replace("'", "''");
				String[] columns = params.split("~~");
				
				for(int i=0; i<columns.length ; i++)
				{
					String[] colNameValue = columns[i].split("==");
					if(colNameValue.length==1){
						stmt.setString(colNameValue[0] ,"");
					}
					else{
						stmt.setString(colNameValue[0] ,colNameValue[1]);
					}
					
				}
							
				rs = stmt.executeQuery();
				objRsmd = rs.getMetaData();
				iNoOfCols = objRsmd.getColumnCount();
				iTotalRetrieved = 0;
				while (rs.next()) 
				{
					strBuffCLOB = new StringBuffer(1024);
					outStrBuff.append("\n\t\t<Record>");
					for (int i = 1; i <= iNoOfCols; i++) 
					{
						jdbcType = objRsmd.getColumnType(i);
						ExtDBLog.writeLog(objRsmd.getColumnName(i) + " : "+ jdbcType);
						if (jdbcType == java.sql.Types.CLOB) 
						{
							ExtDBLog.writeLog(objRsmd.getColumnName(i)+ " : is CLOB ");
							java.io.Reader strReader = rs.getCharacterStream(objRsmd.getColumnName(i));
							if (!rs.wasNull()) 
							{
								int size = 0;
								char[] buf = null;
								while (true) 
								{
									buf = new char[1024];
									size = strReader.read(buf);
									if (size == -1) 
									{
										break;
									}
									len += size;
									strBuffCLOB.append(new String(buf, 0, size));
								}
								strReader.close();
								ExtDBLog.writeLog("Clob Data : "+ strBuffCLOB.toString());
								outStrBuff.append("\n\t\t\t<"+ objRsmd.getColumnName(i) + ">");
								outStrBuff.append(strBuffCLOB.toString());
								outStrBuff.append("</" + objRsmd.getColumnName(i)+ ">");
							}
						} else {
							outStrBuff.append("\n\t\t\t<"+ objRsmd.getColumnName(i) + ">");
							outStrBuff.append(removeNULL(rs.getString(i)));
							outStrBuff.append("</" + objRsmd.getColumnName(i) + ">");
						}
					}
					outStrBuff.append("\n\t\t</Record>");
					iTotalRetrieved = iTotalRetrieved + 1;
				}
				
				tempStrBuff = new StringBuffer();
				tempStrBuff.append("\n\t<Records>");
				tempStrBuff.append(outStrBuff.toString());
				tempStrBuff.append("\n\t</Records>");
				tempStrBuff.append("\n\t<TotalRetrieved>");
				tempStrBuff.append((iTotalRetrieved + "").trim());
				tempStrBuff.append("</TotalRetrieved>");
				strBuff.append("<Exception>\n<MainCode>0</MainCode>\n</Exception>\n");
				strBuff.append("<Output>");
				strBuff.append(tempStrBuff.toString());
				strBuff.append("\n</Output>");
			}
		} catch (SQLException sqlE) {
			strBuff.append("<Exception>\n<MainCode>");
			strBuff.append(sqlE.getErrorCode());
			strBuff.append("</MainCode>\n</Exception>\n");
			strBuff.append("<Output>");
			strBuff.append(sqlE.getMessage());
			strBuff.append("\n</Output>\n");
		} catch (Exception exception3) {
			strBuff.append("<Exception>\n<MainCode>9</MainCode>\n</Exception>\n");
			strBuff.append("<Output>");
			strBuff.append(exception3.getMessage());
			strBuff.append("\n</Output>\n");
		} finally {
			try {
				rs.close();
				query = null;
				outStrBuff = null;
				tempStrBuff = null;

			} catch (Exception ext) {
				ExtDBLog.writeLog("Method : APSelectWithNamedParam, Exception in closing Resultset: "+ ext.getMessage());
			}
			try {
				stmt.close();
				outStrBuff = null;
				tempStrBuff = null;
				query = null;
			} catch (Exception ext) {
				ExtDBLog.writeLog("Method : APSelectWithNamedParam, Exception in closing Statement: "+ ext.getMessage());
			}
		}
		strBuff.append(xmlgenerator.closeOutputFile("APSelectWithNamedParam"));
		ExtDBLog.writeLog("Output APSelectWithNamedParam: "+ strBuff.toString());
		return strBuff.toString();
	}
	
	
	private static String getLogLocation() {
		String strPath = ".";
		String strLibpath = new File(nu.xom.Builder.class.getProtectionDomain()
				.getCodeSource().getLocation().getPath()).getParent();
		File objConfigFile = new File(strLibpath + File.separatorChar
				+ "WFCustom_Configuration.xml");
		if (objConfigFile.exists()) {
			Builder objBuilder = new Builder();
			try {
				Element objRoot = objBuilder.build(objConfigFile)
						.getRootElement();
				Nodes objNodes = objRoot
						.query("Location[Name = 'WFCustom_Logs_Location']/Path");
				if (objNodes.size() == 1) {
					strPath = objNodes.get(0).getValue();
					if (strPath.trim().length() == 0) {
						strPath = ".";
					}
				}
			} catch (ParsingException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		File objLogFolder = new File(strPath + File.separatorChar
				+ "WFCustom_logs" + File.separatorChar);
		if (!objLogFolder.exists()) {
			objLogFolder.mkdirs();
		}
		return objLogFolder.getAbsolutePath();
	}
	
	//-*------------------------------------------------------------------------------------------------

	
	public static void main(String[] args) {
		String outXml= 
			"<?xml version=\"1.0\"?>\n"
			+"<APValidateWiExport_Input>\n"
			+"<Option>APValidateWiExport</Option>\n"
			+"<ProcessName>-1</ProcessName>\n"
			+"</APValidateWiExport_Input>";
	}
	
	// Added By Ajay on 12 Jab 2017 Starts
	
	private String APProcedure_WithDBO(Connection connection, XMLParser xmlparser, XMLGenerator xmlgenerator)
	{
		ResultSet resultset = null;
		CallableStatement callablestatement = null;
		
		StringBuffer stringbuffer = new StringBuffer();
		StringBuffer stringbuffer1 = new StringBuffer();
		
		stringbuffer.append(xmlgenerator.createOutputFile("APProcedure_WithDBO"));
		try
		{
			String s = xmlparser.getValueOf("ProcName", "", false).trim().toUpperCase().replaceAll("'", "''");
			String Params = xmlparser.getValueOf("Params", "", true);
			String s2 = xmlparser.getValueOf("EngineName", "", false).toUpperCase();
			String s3 = null;
			String s4 = "";
			//Changes done for SQL Injection in APProcedure call.
			if(!"".equalsIgnoreCase(Params)){
				Params = Params+",'blank_check'";//Deepak last element added to handale blank vale in last
				Params = Params.substring(1,Params.length()-1);
				String [] Params_arr = Params.split("','");
				Params="";
				for(int paramlen = 0;paramlen < Params_arr.length-1;paramlen++){
					if(paramlen==0){
						Params = Params+"'"+Params_arr[paramlen].replaceAll("'", "''")+"'";
					}
					else{
						Params = Params+",'"+Params_arr[paramlen].replaceAll("'", "''")+"'";
					}
				}
			}
			ExtDBLog.writeLog("new param: "+Params );
			
			if(s.indexOf(".") != -1)
			{
				s3 = s.substring(0, s.indexOf("."));
				s = s.substring(s.indexOf(".") + 1);
			}
			
			ExtDBLog.writeLog("Catalog = " + s3);
			ExtDBLog.writeLog("Procedure = " + s);
			DatabaseMetaData databasemetadata = connection.getMetaData();
			resultset = databasemetadata.getProcedures(s3, s2, s);
			boolean proceedFurther=false;
			if(!resultset.next())
			{
				resultset.close();
				resultset = null;
				
				s2="dbo";
				resultset = databasemetadata.getProcedures(s3, s2, s);
				if(!resultset.next())
				{
					stringbuffer.append("<Exception>\n<MainCode>99</MainCode>\n</Exception>\n");
					stringbuffer.append("<Output>Procedure Not Found</Output>\n");
					resultset.close();
					resultset = null;
				}
				else
				{
					proceedFurther=true;
				}
			} 
			else
			{
				proceedFurther=true;
			}
			
			if(proceedFurther)
			{
				resultset.close();
				resultset = databasemetadata.getProcedureColumns(s3, s2, s, "%");
				do
				{
					if(!resultset.next())
						break;
					ExtDBLog.writeLog("Type = " + resultset.getString("TYPE_NAME"));
					if(resultset.getInt("COLUMN_TYPE") == 4)
					{
						if(resultset.getString("TYPE_NAME").equals("REF CURSOR"))
							s4 = s4 + "RS,";
						else
							if(resultset.getString("TYPE_NAME").equals("NUMBER"))
								s4 = s4 + "N,";
							else
								if(resultset.getString("TYPE_NAME").equals("VARCHAR2"))
									s4 = s4 + "S,";
						if("".equals(Params))
							Params = Params + "?";
						else{
							Params = Params + ",?";
						}
							
					}
				} while(true);
				resultset.close();
				resultset = null;
				databasemetadata = null;
				s = xmlparser.getValueOf("ProcName", "", false).trim().toUpperCase();
				String s6 = "{call " + s + "(" + Params + ")}";
				callablestatement = connection.prepareCall(s6);
				ExtDBLog.writeLog("Outparams = " + s4);
				if(!s4.equals(""))
				{
					s4 = s4.substring(0, s4.length() - 1);
					ExtDBLog.writeLog("Outparams = " + s4);
					StringTokenizer stringtokenizer = new StringTokenizer(s4, ",");
					int k = 0;
					do
					{
						if(!stringtokenizer.hasMoreTokens())
							break;
						String s7 = stringtokenizer.nextToken();
						k++;
						if(s7.trim().toUpperCase().equals("RS"))
							callablestatement.registerOutParameter(k, -10);
						else
							if(s7.trim().toUpperCase().equals("N"))
								callablestatement.registerOutParameter(k, 2);
							else
								if(s7.trim().toUpperCase().equals("S"))
									callablestatement.registerOutParameter(k, 12);
					} while(true);
				}
				ExtDBLog.writeLog("Stage1 = " + s4);
				callablestatement.execute();
				ExtDBLog.writeLog("Stage2 = " + s4);
				if(!s4.equals(""))
				{
					StringTokenizer stringtokenizer1 = new StringTokenizer(s4, ",");
					int l = 0;
					
					for(; stringtokenizer1.hasMoreTokens(); stringbuffer1.append("</Param" + l + ">"))
					{
						String s8 = stringtokenizer1.nextToken();
						l++;
						stringbuffer1.append("\n\t<Param" + l + ">");
						if(s8.trim().toUpperCase().equals("RS"))
						{
							resultset = (ResultSet)callablestatement.getObject(l);
							if(resultset == null)
								continue;
							ResultSetMetaData resultsetmetadata1 = resultset.getMetaData();
							int i1 = resultsetmetadata1.getColumnCount();
							int j1 = 0;
							stringbuffer1.append("\n\t<Records>");
							for(; resultset.next(); stringbuffer1.append("\n\t</Record>"))
							{
								stringbuffer1.append("\n\t<Record>");
								for(int k1 = 1; k1 <= i1; k1++)
									stringbuffer1.append("\n\t\t<" + resultsetmetadata1.getColumnName(k1) + ">" + removeNULL(resultset.getString(k1)) + "</" + resultsetmetadata1.getColumnName(k1) + ">");

								j1++;
							}

							stringbuffer1.append("\n\t</Records>");
							stringbuffer1.append("\n\t<TotalRetrieved>" + j1 + "</TotalRetrieved>\n\t");
							resultset.close();
							continue;
						}
						if(s8.trim().toUpperCase().equals("N"))
						{
							stringbuffer1.append(callablestatement.getLong(l));
							continue;
						}
						if(s8.trim().toUpperCase().equals("S"))
							stringbuffer1.append(removeNULL(callablestatement.getString(l)));
					}

				}
				stringbuffer.append("<Exception>\n<MainCode>0</MainCode>\n</Exception>\n");
				stringbuffer.append("<Output>" + stringbuffer1.toString() + "\n</Output>\n");
			}
		}
		catch(SQLException sqlexception)
		{
			stringbuffer.append("<Exception>\n<MainCode>" + sqlexception.getErrorCode() + "</MainCode>\n</Exception>\n");
			stringbuffer.append("<Output>" + sqlexception.getMessage() + "\n</Output>\n");
		}
		catch(Exception exception)
		{
			stringbuffer.append("<Exception>\n<MainCode>9</MainCode>\n</Exception>\n");
			stringbuffer.append("<Output>" + exception.toString() + "\n</Output>\n");
		}
		finally
		{
			try
			{
				resultset.close();
				resultset = null;
			}
			catch(Exception exception2) { }
			try
			{
				callablestatement.close();
				callablestatement = null;
			}
			catch(Exception exception3) { }
		}
		stringbuffer.append(xmlgenerator.closeOutputFile("APProcedure_WithDBO"));
		ExtDBLog.writeLog(xmlparser.toString());
		ExtDBLog.writeLog(stringbuffer.toString());
		return stringbuffer.toString();
	}
	
		
	public String APExecuteQueryWithSession(Connection paramConnection, XMLParser paramXMLParser,
			XMLGenerator paramXMLGenerator) 
	{

		ExtDBLog.writeLog("Inside APExecuteQueryWithSession() of WFCustomBean.java");
		System.out.println("Inside APExecuteQueryWithSession() of WFCustomBean.java");
		ExtDBLog.writeLog("Input XML :: " + paramXMLParser.toString());

		Statement localStatement = null;
		StringBuffer localStringBuffer = new StringBuffer();

		localStringBuffer.append(paramXMLGenerator.createOutputFile("APExecuteQueryWithSession"));
		try 
		{
			boolean bool = false;
			int i = paramXMLParser.getIntOf("SessionId", 0, false);
			bool = ValidateSession(paramConnection, i);
			
			if (bool) 
			{
				String strQuery = paramXMLParser.getValueOf("Query", "", false);

				ExtDBLog.writeLog("Query in APExecuteQueryWithSession() :: " + strQuery);
				System.out.println("Query in APExecuteQueryWithSession() :: " + strQuery);

				localStatement = paramConnection.createStatement();
				int j = localStatement.executeUpdate(strQuery);
				localStringBuffer.append("<Exception>\n<MainCode>0</MainCode>\n</Exception>\n");

				localStringBuffer.append("<Output>" + j + "</Output>");
			}
			else
			{
				localStringBuffer.append("<Exception>\n<MainCode>11</MainCode>\n</Exception>\n");
				localStringBuffer.append("<Output>Invalid Session.\n</Output>\n");
			}
		} catch (SQLException e) {
			localStringBuffer.append("<Exception>\n<MainCode>" + e.getErrorCode()
					+ "</MainCode>\n</Exception>\n");

			localStringBuffer.append("<Output>" + e.getMessage() + "\n</Output>\n");
		} catch (Exception e) {
			localStringBuffer.append("<Exception>\n<MainCode>9</MainCode>\n</Exception>\n");

			localStringBuffer.append("<Output>" + e.toString() + "\n</Output>\n");
		} finally {
			try {
				if (localStatement != null) {
					localStatement.close();
					localStatement = null;
				}
			} catch (Exception e) {
				ExtDBLog.writeLog("Exception :: " + e.getMessage());
			}
		}
		localStringBuffer.append(paramXMLGenerator.closeOutputFile("APExecuteQueryWithSession"));

		ExtDBLog.writeLog("Output XML :: " + localStringBuffer.toString());
		return localStringBuffer.toString();
	}
	
	public String APExecuteQueryWithoutSession(Connection paramConnection, XMLParser paramXMLParser,
			XMLGenerator paramXMLGenerator) 
	{

		ExtDBLog.writeLog("Inside APExecuteQueryWithoutSession() of WFCustomBean.java");
		System.out.println("Inside APExecuteQueryWithoutSession() of WFCustomBean.java");
		ExtDBLog.writeLog("Input XML :: " + paramXMLParser.toString());

		Statement localStatement = null;
		StringBuffer localStringBuffer = new StringBuffer();

		localStringBuffer.append(paramXMLGenerator.createOutputFile("APExecuteQueryWithoutSession"));
		try 
		{
			String strQuery = paramXMLParser.getValueOf("Query", "", false);

			ExtDBLog.writeLog("Query in APExecuteQueryWithoutSession() :: " + strQuery);
			System.out.println("Query in APExecuteQueryWithoutSession() :: " + strQuery);

			localStatement = paramConnection.createStatement();
			int j = localStatement.executeUpdate(strQuery);
			localStringBuffer.append("<Exception>\n<MainCode>0</MainCode>\n</Exception>\n");

			localStringBuffer.append("<Output>" + j + "</Output>");
			
		} catch (SQLException e) {
			localStringBuffer.append("<Exception>\n<MainCode>" + e.getErrorCode()
					+ "</MainCode>\n</Exception>\n");

			localStringBuffer.append("<Output>" + e.getMessage() + "\n</Output>\n");
		} catch (Exception e) {
			localStringBuffer.append("<Exception>\n<MainCode>9</MainCode>\n</Exception>\n");

			localStringBuffer.append("<Output>" + e.toString() + "\n</Output>\n");
		} finally {
			try {
				if (localStatement != null) {
					localStatement.close();
					localStatement = null;
				}
			} catch (Exception e) {
				ExtDBLog.writeLog("Exception :: " + e.getMessage());
			}
		}
		localStringBuffer.append(paramXMLGenerator.closeOutputFile("APExecuteQueryWithoutSession"));

		ExtDBLog.writeLog("Output XML :: " + localStringBuffer.toString());
		return localStringBuffer.toString();
	}	
	// Added By Ajay on 12 Jab 2017 Ends
    
    private String getErrorOutputXml(String strOption, int iMainCode, String strErrorType, String strSubject)
    {
        String strCallName = strOption + "_Output";
        StringBuilder strBldOutputXml = new StringBuilder();
        strBldOutputXml.append("<?xml version='1.0'?>");
        strBldOutputXml.append("<").append(strCallName).append(">");
        strBldOutputXml.append("<Option>").append(strOption).append("</Option>");
        strBldOutputXml.append("<Exception>");
        strBldOutputXml.append("<MainCode>").append(iMainCode).append("</MainCode>");
        strBldOutputXml.append("<TypeOfError>").append(strErrorType).append("</TypeOfError>");
        strBldOutputXml.append("<Subject>").append(strSubject).append("</Subject>");
        strBldOutputXml.append("</Exception>");
        strBldOutputXml.append("</").append(strCallName).append(">");
        return strBldOutputXml.toString();
    }	
	
	//Modified by Amandeep for writing process-wise Custom logs
	//public static void WriteCustomLog(String strMsg) 
	//throws Exception 
	public static void WriteCustomLog(String strMsg,String... args) 
	throws Exception 
	{
		StringBuffer strFilePath = new StringBuffer(50);
		File dir= null;
		FileOutputStream fos = null;
		String sFileName = "CustomLog.Log";
		Writer wrt = null;
		
		DateFormat dtFormat = new SimpleDateFormat("ddMMyyyy");
		
		//Modified below by Amandeep for writing process-wise Custom logs
		String strProcessName="";
		if(args!=null && args.length>0)	strProcessName="_"+args[0];
		
		String sFName = "CustomLog_RakBank" + dtFormat.format(new java.util.Date())+strProcessName+".Log";	
		//String sFName = "CustomLog_RakBank" + dtFormat.format(new java.util.Date())+".Log";	
		//Modified above by Amandeep for writing process-wise Custom logs
	
		try
		{
			strFilePath.append(System.getProperty("user.dir"));
			
			dir = new File(strFilePath.toString(), "CustomLog");
			if (!dir.exists()) 
			{
				dir.mkdir();
			}
			strFilePath.append(File.separatorChar);
			strFilePath.append("CustomLog");
			strFilePath.append(File.separatorChar);
			strFilePath.append(sFName);
			java.util.Date objDate=new java.util.Date();
			fos = new FileOutputStream(strFilePath.toString(),true);
			wrt = new BufferedWriter(new OutputStreamWriter(fos));
			wrt.write("[" + objDate.toString() + "]\n" + strMsg + "\n\n" );
			wrt.flush();
			wrt.close();
		}
		catch(Exception e)
		{
		
			System.out.println(e.toString());
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
	
	private void readPropertyFile_DSR()
    {
        try
        {
            Properties properties = new Properties();
            properties.load(getClass().getResourceAsStream("MQSettings_DSR.properties"));
            hostName = properties.getProperty("HostName");
            port = properties.getProperty("PortNumber");
            qmgrName = properties.getProperty("QMgrName");
            ReqChannelId = properties.getProperty("RequestorChannelId");
			channel = properties.getProperty("MQChannelId");
            qNameSource = properties.getProperty("SourceQueueName");
			qNameDest = properties.getProperty("DestinationQueueName");
			lTimeInterval = properties.getProperty("TimeInterval");
			strMsgFormat = properties.getProperty("MsgFormat");
			strMsgVersion = properties.getProperty("MsgVersion");
			strRequestorLanguage = properties.getProperty("RequestorLanguage");
			strRequestorSecurityInfo = properties.getProperty("RequestorSecurityInfo");
			strReturnCode =	properties.getProperty("ReturnCode");


        }
        catch(Exception exception)
        {
            System.out.println("exception in reading argument file...\n");
            exception.printStackTrace();
            System.exit(0);
        }
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
	
	private void readPropertyFile()
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
			strMsgFormat = properties.getProperty("MsgFormat");
			strMsgVersion = properties.getProperty("MsgVersion");
			strRequestorLanguage = properties.getProperty("RequestorLanguage");
			strRequestorSecurityInfo = properties.getProperty("RequestorSecurityInfo");
			strReturnCode =	properties.getProperty("ReturnCode");


        }
        catch(Exception exception)
        {
            System.out.println("exception in reading argument file...\n");
            exception.printStackTrace();
            System.exit(0);
        }
    }
	
	private void writeLog(String pEngineName, String pMsg) {
		try {
			Logger objLogger = Logger.getLogger(pEngineName);
			String strAppName = pEngineName + "_app";
			if (objLogger.getAppender(strAppName) == null) {
				File objPath = new File(_strFilepath + File.separatorChar
						+ pEngineName);
				if (!objPath.exists()) {
					objPath.mkdirs();
				}
				FileAppender objAppender = new org.apache.log4j.DailyRollingFileAppender(
						new PatternLayout("%d{ISO8601} %-5p - %m%n"), objPath
								.getAbsolutePath()
								+ File.separatorChar + "FBD_DBLink.log",
						"'.'yyyy-MM-dd");
				objAppender.setName(strAppName);
				objLogger.addAppender(objAppender);
			}
			objLogger.log(Level.DEBUG, pMsg);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	private boolean validateCheckSum(String pQuery, int pSessionId,
			int pCheckSum) {
		int iCheckSumCal = calculateCheckSum(pQuery, pSessionId);
		if (pCheckSum == iCheckSumCal) {
			return true;
		}
		return false;
	}
	
	private int calculateCheckSum(String pInput, int pSessionId) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(pInput.getBytes("UTF-16"));
			md.update(String.valueOf(pSessionId).getBytes("UTF-16"));
			byte[] digest = md.digest();
			int decValue = 0;
			for (int i = 0; i < digest.length; i++) {
				if (digest[i] >= 0) {
					decValue += digest[i];
				} else {
					decValue += 256 + digest[i];
				}
			}
			return decValue;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
		}
		return 0;
	}
	
	/**
	Function added to restrict the BT/CCC calls to Credit Cards Only
	*/
	public String SRM_APMQPutGetMessage_CARDS_BT_CCC(Connection connection, XMLParser xmlparser, XMLGenerator xmlgenerator, String strRequestType, MQHelper mqHelper)
	{
		ExtDBLog.writeLog("Here Start of  SRM_APMQPutGetMessage_CARDS_BT_CCC ");
		StringBuffer stringbuffer;
		stringbuffer = new StringBuffer();
		//stringbuffer.append(xmlgenerator.createOutputFile("APMQPutGetMessage"));
		String strMsg = "";
		String msg[] = new String[2];
		String strReturnMsg = "";
		//MQMessage message = new MQMessage();
		XMLParser objXmlParser=null;
		
		try{
			strCardNo = xmlparser.getValueOf("CardNumber", "", false);
			strUserID = xmlparser.getValueOf("UserID", "", false);
			
			ExtDBLog.writeLog("strCardNo :"+strCardNo);
			ExtDBLog.writeLog("strUserID :"+strUserID);
			
			
		}catch(Exception e){e.printStackTrace();ExtDBLog.writeLog("Exception in getting values");}
		
		String temp[]= strRequestType.split("~");
		strCardNo = AesUtil.Decrypt(strCardNo);
		ExtDBLog.writeLog("Decrypted Card Number : " + strCardNo);
		String cardDetails[] = getCardDetails(connection,xmlparser,xmlgenerator, strCardNo).split(",");
		strCardType = cardDetails[0];
		strCardProduct = cardDetails[1];
		String merchant = cardDetails[2];
		ExtDBLog.writeLog("strCardType :"+strCardType);
		ExtDBLog.writeLog("strCardProduct :"+strCardProduct);
		
		try{
			mqHelper.setMQDetails(connection,xmlparser,xmlgenerator);
			for(int t=0;t<temp.length;t++)
			{	
				if(temp[t].equals("PrimeFetch"))
				{
					ExtDBLog.writeLog("Inside PrimeFetch ");
					String requestXML = mqHelper.getMQRequestXML(connection, xmlparser, xmlgenerator, "CardDetails", strCardNo, "", "");
					ExtDBLog.writeLog("PrimeFetch Request : " + requestXML);
					msg[t]= mqHelper.getMQResponse(requestXML);
					ExtDBLog.writeLog("PrimeFetch Response : " + msg[t]);
				}
				else if(temp[t].equals("CardList"))
				{
					ExtDBLog.writeLog("Inside CardList with card type as "+strCardType);
					if(strCardType.equals("Credit Card"))
					{
						String requestXML = mqHelper.getMQRequestXML(connection, xmlparser, xmlgenerator, "CardList", strCardNo, "", "");
						ExtDBLog.writeLog("CardList Request : " + requestXML);
						msg[t]= mqHelper.getMQResponse(requestXML);
						ExtDBLog.writeLog("CardList Response : " + msg[t]);
					}
					else
					{
						ExtDBLog.writeLog("Card Type is not allowed for BT/CCC" + strCardType);
						msg[t]="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><EE_EAI_MESSAGE><EE_EAI_HEADER><MsgFormat>CARD_LIST</MsgFormat><MsgVersion>0000</MsgVersion><RequestorChannelId>BPM</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>Debit</ReturnCode><ReturnDesc>Failure</ReturnDesc><MessageId>123123453</MessageId><Extra1>REP||SHELL.JOHN</Extra1><Extra2>YYYY-MM-DDThh:mm:ss.mmm+hh:mm</Extra2></EE_EAI_HEADER>";
					}
				}
				else if(temp[t].equals("FinacleFetch"))
				{
					ExtDBLog.writeLog("Inside FinacleFetch............. ");
					String requestXML = mqHelper.getMQRequestXML(connection, xmlparser, xmlgenerator, "CustomerDetails", strCardNo, "", "");
					ExtDBLog.writeLog("FinacleFetch Request : " + requestXML);
					msg[t]= mqHelper.getMQResponse(requestXML);
					ExtDBLog.writeLog("FinacleFetch Response : " + msg[t]);
				}
			}
		}
		catch(Exception e){ExtDBLog.writeLog("Exception in MQHelper " + e.getMessage());}
		
		objXmlParser = new XMLParser(msg[1]);
		String FullName="";
		String PhoneNo="";
		String CIFID="";
		try{		
			FullName = objXmlParser.getValueOf("FullName", "", false);
		}catch(Exception e){e.printStackTrace();ExtDBLog.writeLog("Exception in getting full name from finacle");}
		try{
			CIFID = objXmlParser.getValueOf("CIFID", "", false);
		}catch(Exception e){e.printStackTrace();ExtDBLog.writeLog("Exception in getting CIFID from finacle");}
		try{
			PhoneNo = objXmlParser.getValueOf("PhoneNo", "", false);
		}catch(Exception e){e.printStackTrace();ExtDBLog.writeLog("Exception in getting PhoneNO from finacle");
		ExtDBLog.writeLog("Finacle value of FullName "+FullName);
		ExtDBLog.writeLog("Finacle value of CIFID "+CIFID);
		ExtDBLog.writeLog("Finacle value of PhoneNo "+PhoneNo);
		}

		if(temp[0].equals("PrimeFetch"))
		{
			try{
				
				CharSequence isCBF = "<IsCashbackForfeited></IsCashbackForfeited>";
				CharSequence disputeT = "<IsDisputedTran></IsDisputedTran>";
				CharSequence CIF = "<CIFID></CIFID>";
				if(msg[0].contains(isCBF))
					msg[0]=msg[0].replaceAll("<IsCashbackForfeited></IsCashbackForfeited>", "<IsCashbackForfeited>N</IsCashbackForfeited>");
				ExtDBLog.writeLog("I reached here2");
				if(msg[0].contains(disputeT))
					msg[0]=msg[0].replaceAll("<IsDisputedTran></IsDisputedTran>", "<IsDisputedTran>N</IsDisputedTran>");
				if(msg[0].contains(CIF))
					msg[0]=msg[0].replaceAll("<CIFID></CIFID>", "<CIFID>"+CIFID+"</CIFID>");					
				
				msg[0] = msg[0].substring(0, msg[0].indexOf("<CardSubType>")+"<CardSubType>".length())+strCardProduct+msg[0].substring(msg[0].indexOf("</CardSubType>"));
			msg[0]=msg[0].replaceAll("<CardType></CardType>", "<CardType>"+strCardType+"</CardType>"+"<CustomerName>"+FullName+"</CustomerName><MobileNo>"+PhoneNo+"</MobileNo>");
			}catch(Exception e){e.printStackTrace();ExtDBLog.writeLog("Exception in getting values of dispute transaction and cashback forfeited tags");}
		}
		else if(temp[0].equals("CardList"))
		{
			String strOutxml=msg[0];
			String strCardNoTag="CardNumber";
			XMLParser xp ;
			XMLParser xp2 =  new XMLParser(strOutxml);
			String cardholdertype="";
			String overdueAmount = "";
			String accountNo = "";
			String crnNoFetchedCard = "";
			try{
				overdueAmount=xp2.getValueOf("CurrentOverdueAmt", "", false);
			}catch(Exception e){ExtDBLog.writeLog("exception in getting first overdue amount "+overdueAmount);}	
			ExtDBLog.writeLog("getting first overdue amount "+overdueAmount);
			try{
				accountNo=xp2.getValueOf("AccountNumber", "", false);
			}catch(Exception e){ExtDBLog.writeLog("exception in getting first account no "+accountNo);}	
			ExtDBLog.writeLog("getting first account no"+accountNo);
			try{
				while (strOutxml.indexOf("<"+strCardNoTag+">")>-1)
				{
					String strCardNum=strOutxml.substring(strOutxml.indexOf("<"+strCardNoTag+">")+("<"+strCardNoTag+">").length(),strOutxml.indexOf("</"+strCardNoTag+">"));
					//String strCardType=mqHelper.getCardType(connection, xmlparser, xmlgenerator, strCardNum,3);
					merchant=mqHelper.getCardType(connection, xmlparser, xmlgenerator, strCardNum,4);
					xp = new XMLParser(strOutxml);
					if (strCardNum.equals(strCardNo))
					{
						cardholdertype = xp.getValueOf("IsPrimaryCard", "", false);
						crnNoFetchedCard = xp.getValueOf("CRNNo", "", false);
						ExtDBLog.writeLog("getting card holder type and crnno fetched card "+cardholdertype+"::"+crnNoFetchedCard);
					}
					if(strOutxml.indexOf("</CardAccountDetails>")<strOutxml.indexOf("</CardDetails>"))
					{
						overdueAmount = xp.getValueOf("CurrentOverdueAmt", "", false);
						accountNo = xp.getValueOf("AccountNumber", "", false);
						ExtDBLog.writeLog("getting next overdue amount "+overdueAmount);
					}
					msg[0]=msg[0].replaceAll("<"+strCardNoTag+">"+strCardNum+"</"+strCardNoTag+">","<"+strCardNoTag+">"+strCardNum+"</"+strCardNoTag+"><CardType>"+merchant+"</CardType><CustomerName>"+FullName+"</CustomerName><MobileNo>"+PhoneNo+"</MobileNo><CIFID>"+CIFID+"</CIFID><card_holder_type></card_holder_type><OverDueAmount>"+overdueAmount+"</OverDueAmount><AccountNumber>"+accountNo+"</AccountNumber><c_crn_no></c_crn_no><encrypted_cardno>"+AesUtil.Encrypt(strCardNum)+"</encrypted_cardno>");
				
					strOutxml=strOutxml.substring(strOutxml.indexOf("</CardDetails>")+("</CardDetails>").length());					 xp=null;	
				}
				msg[0]=msg[0].replaceAll("<card_holder_type></card_holder_type>","<card_holder_type>"+cardholdertype+"</card_holder_type>");
				msg[0]=msg[0].replaceAll("<c_crn_no></c_crn_no>","<c_crn_no>"+crnNoFetchedCard+"</c_crn_no>");
				msg[0]=msg[0].replaceAll("<FreeField1></FreeField1>","<FreeField1>"+strCardNo+"</FreeField1>");
				
			}catch(Exception e){e.printStackTrace();ExtDBLog.writeLog("Exception in setting values not fetched.");}	
		}
		
		

		stringbuffer.append("<Output><Message>"+ msg[0]+"</Message>\n<Exception>\n<CompletionCode>0</CompletionCode>\n<ReasonCode>0</ReasonCode><Description>success</Description>\n</Exception>\n</Output>");
		ExtDBLog.writeLog(stringbuffer.toString());
		
		return stringbuffer.toString();
	}
	public String SRM_APMQPutGetMessage_CARDS_BOC(Connection connection, XMLParser xmlparser, XMLGenerator xmlgenerator, String strRequestType, MQHelper mqHelper)
	{
		ExtDBLog.writeLog("Here Start of  SRM_APMQPutGetMessage_CARDS_BOC ");
		StringBuffer stringbuffer;
		stringbuffer = new StringBuffer();
		//stringbuffer.append(xmlgenerator.createOutputFile("APMQPutGetMessage"));
		String strMsg = "";
		String msg[] = new String[2];
		String strReturnMsg = "";
		String CustomerDetailsData="";
		String FullName="";
		String PhoneNo="";
		String CIFID="";
		XMLParser objXmlParser=null;
		
		try{
			strCardNo = xmlparser.getValueOf("CardNumber", "", false);
			strUserID = xmlparser.getValueOf("UserID", "", false);
			
			ExtDBLog.writeLog("strCardNo :"+strCardNo);
			ExtDBLog.writeLog("strUserID :"+strUserID);
			
			
		}catch(Exception e){e.printStackTrace();ExtDBLog.writeLog("Exception in getting values");}
		
		strRequestType = "FinacleFetch~CardList";
		String temp[]= strRequestType.split("~");
		strCardNo = AesUtil.Decrypt(strCardNo);
		ExtDBLog.writeLog("Decrypted Card Number : " + strCardNo);
		String cardDetails[] = getCardDetails(connection,xmlparser,xmlgenerator, strCardNo).split(",");
		strCardType = cardDetails[0];
		strCardProduct = cardDetails[1];
		
		ExtDBLog.writeLog("strCardType :"+strCardType);
		ExtDBLog.writeLog("strCardProduct :"+strCardProduct);
		
		try{
			mqHelper.setMQDetails(connection,xmlparser,xmlgenerator);
			for(int t=0;t<temp.length;t++)
			{	
				if(temp[t].equals("CardList"))
				{
					ExtDBLog.writeLog("Inside CardList ");
					String requestXML = mqHelper.getMQRequestXML(connection, xmlparser, xmlgenerator, "BOC_CardList_Debit", "D"+CIFID, "", "");
					ExtDBLog.writeLog("CardList Request debit: " + requestXML);
					msg[t]= mqHelper.getMQResponse(requestXML);
					ExtDBLog.writeLog("CardList Response debit : " + msg[t]);
					XMLParser debitresponse = new XMLParser(msg[t]);
					String debitReturnCode = debitresponse.getValueOf("ReturnCode", "", false);
					if("0000".equals(debitReturnCode))
						msg[t]=AppendOtherCardDetails_BOC(msg[t] ,strCardNo ,"Debit",FullName, PhoneNo, CIFID);
					else 
						msg[t]="";
					requestXML = mqHelper.getMQRequestXML(connection, xmlparser, xmlgenerator, "BOC_CardList_Credit", "C"+CIFID, "", "");
					ExtDBLog.writeLog("CardList Request credit: " + requestXML);
					String crdt= mqHelper.getMQResponse(requestXML);
					crdt=AppendOtherCardDetails_BOC(crdt , strCardNo,"Credit",FullName, PhoneNo, CIFID);
					
					
					ExtDBLog.writeLog("CardList Response credit: " + crdt);
					if("".equals(msg[t]))
						msg[t]=crdt;
					else
					{
						crdt = crdt.substring(crdt.indexOf("<CardDetails>"),crdt.lastIndexOf("</CardDetails>")+14);
						msg[t] = msg[t].substring(0,msg[t].lastIndexOf("</CardDetails>")+14)+ crdt + msg[t].substring(msg[t].lastIndexOf("</CardDetails>")+14,msg[t].length());
					}
					//code added to set primary card holder name in case of BoC multiple cards
					String XmlData=msg[t];
					String cardholdertype="";
					try
					{
						while (msg[t].indexOf("<CardNumber>")>-1)
						{
							String strCardNum=msg[t].substring(msg[t].indexOf("<CardNumber>")+("<CardNumber>").length(),msg[t].indexOf("</CardNumber>"));
							strCardType=msg[t].substring(msg[t].indexOf("<CardType>")+("<CardType>").length(),msg[t].indexOf("</CardType>"));
							// String strCardType=mqHelper.getCardType(connection, xmlparser, xmlgenerator, strCardNum,3);
							 
							objXmlParser = new XMLParser(msg[t]);
							if (strCardNum.equals(strCardNo))
							{
								cardholdertype = objXmlParser.getValueOf("IsPrimaryCard", "", false);
							}
							XmlData=XmlData.replaceAll("<CardNumber>"+strCardNum+"</CardNumber>","<CardNumber>"+strCardNum+"</CardNumber><CardType>"+strCardType+"</CardType><CustomerName>"+FullName+"</CustomerName><MobileNo>"+PhoneNo+"</MobileNo><CIFID>"+CIFID+"</CIFID><card_holder_type></card_holder_type>");
						
							msg[t]=msg[t].substring(msg[t].indexOf("</CardDetails>")+("</CardDetails>").length()); 
							objXmlParser=null;	
						}
						XmlData=XmlData.replaceAll("<card_holder_type></card_holder_type>","<card_holder_type>"+cardholdertype+"</card_holder_type>");
						msg[t]=XmlData;
					}
					catch(Exception e)
					{
						e.printStackTrace();ExtDBLog.writeLog("Exception in setting values not fetched.");
					}
					ExtDBLog.writeLog("CardList Response : " + msg[t]+crdt);
				}
				else if(temp[t].equals("FinacleFetch"))
				{
					ExtDBLog.writeLog("Inside FinacleFetch............. ");
					String requestXML = mqHelper.getMQRequestXML(connection, xmlparser, xmlgenerator, "CustomerDetails", strCardNo, "", "");
					ExtDBLog.writeLog("FinacleFetch Request : " + requestXML);
					msg[t]= mqHelper.getMQResponse(requestXML);
					objXmlParser = new XMLParser(msg[t]);
					try
					{		
						FullName = objXmlParser.getValueOf("FullName", "", false);
					}
					catch(Exception e)
					{	
						e.printStackTrace();
						ExtDBLog.writeLog("Exception in getting full name from finacle");
					}
					try
					{
						CIFID = objXmlParser.getValueOf("CIFID", "", false);
					}
					catch(Exception e)
					{
						e.printStackTrace();
						ExtDBLog.writeLog("Exception in getting CIFID from finacle");
					}
					try
					{
						PhoneNo = objXmlParser.getValueOf("PhoneNo", "", false);
					}
					catch(Exception e)
					{
						e.printStackTrace();ExtDBLog.writeLog("Exception in getting PhoneNO from finacle");
						ExtDBLog.writeLog("Finacle value of FullName "+FullName);
						ExtDBLog.writeLog("Finacle value of CIFID "+CIFID);
						ExtDBLog.writeLog("Finacle value of PhoneNo "+PhoneNo);
					}
					CustomerDetailsData = msg[t];
					ExtDBLog.writeLog("FinacleFetch Response : " + msg[t]);
				}
			}
		}
		catch(Exception e)
		{
			ExtDBLog.writeLog("Exception in MQHelper " + e.getMessage());
		}
		
		

		stringbuffer.append("<Output><Message>"+ msg[1]+"</Message>\n<Exception>\n<CompletionCode>0</CompletionCode>\n<ReasonCode>0</ReasonCode><Description>success</Description>\n</Exception>\n</Output>");
		ExtDBLog.writeLog(stringbuffer.toString());
		
		return stringbuffer.toString();
	}
	
	public String SRM_APMQPutGetMessage_CARDS(Connection connection, XMLParser xmlparser, XMLGenerator xmlgenerator, String strRequestType, MQHelper mqHelper)
	{
		ExtDBLog.writeLog("Here Start of  SRM_APMQPutGetMessage_CARDS ");
		StringBuffer stringbuffer;
		stringbuffer = new StringBuffer();
		//stringbuffer.append(xmlgenerator.createOutputFile("APMQPutGetMessage"));
		String strMsg = "";
		String msg[] = new String[2];
		String strReturnMsg = "";
		//MQMessage message = new MQMessage();
		XMLParser objXmlParser=null;
		
		try{
			strCardNo = xmlparser.getValueOf("CardNumber", "", false);
			strUserID = xmlparser.getValueOf("UserID", "", false);
			
			ExtDBLog.writeLog("strCardNo :"+strCardNo);
			ExtDBLog.writeLog("strUserID :"+strUserID);
			
			
		}catch(Exception e){e.printStackTrace();ExtDBLog.writeLog("Exception in getting values");}
		
		String temp[]= strRequestType.split("~");
		strCardNo = AesUtil.Decrypt(strCardNo);
		ExtDBLog.writeLog("Decrypted Card Number : " + strCardNo);
		String cardDetails[] = getCardDetails(connection,xmlparser,xmlgenerator, strCardNo).split(",");
		strCardType = cardDetails[0];
		strCardProduct = cardDetails[1];
		
		ExtDBLog.writeLog("strCardType :"+strCardType);
		ExtDBLog.writeLog("strCardProduct :"+strCardProduct);
		
		try{
			mqHelper.setMQDetails(connection,xmlparser,xmlgenerator);
			for(int t=0;t<temp.length;t++)
			{	
				if(temp[t].equals("PrimeFetch"))
				{
					ExtDBLog.writeLog("Inside PrimeFetch ");
					String requestXML = mqHelper.getMQRequestXML(connection, xmlparser, xmlgenerator, "CardDetails", strCardNo, "", "");
					ExtDBLog.writeLog("PrimeFetch Request : " + requestXML);
					msg[t]= mqHelper.getMQResponse(requestXML);
					ExtDBLog.writeLog("PrimeFetch Response : " + msg[t]);
				}
				else if(temp[t].equals("CardList"))
				{
					ExtDBLog.writeLog("Inside CardList ");
					String requestXML = mqHelper.getMQRequestXML(connection, xmlparser, xmlgenerator, "CardList", strCardNo, "", "");
					ExtDBLog.writeLog("CardList Request : " + requestXML);
					msg[t]= mqHelper.getMQResponse(requestXML);
					ExtDBLog.writeLog("CardList Response : " + msg[t]);
				}
				else if(temp[t].equals("FinacleFetch"))
				{
					ExtDBLog.writeLog("Inside FinacleFetch............. ");
					String requestXML = mqHelper.getMQRequestXML(connection, xmlparser, xmlgenerator, "CustomerDetails", strCardNo, "", "");
					ExtDBLog.writeLog("FinacleFetch Request : " + requestXML);
					msg[t]= mqHelper.getMQResponse(requestXML);
					ExtDBLog.writeLog("FinacleFetch Response : " + msg[t]);
				}
			}
		}
		catch(Exception e){ExtDBLog.writeLog("Exception in MQHelper " + e.getMessage());}
		
		objXmlParser = new XMLParser(msg[1]);
		String FullName="";
		String PhoneNo="";
		String CIFID="";
		try{		
			FullName = objXmlParser.getValueOf("FullName", "", false);
		}catch(Exception e){e.printStackTrace();ExtDBLog.writeLog("Exception in getting full name from finacle");}
		try{
			CIFID = objXmlParser.getValueOf("CIFID", "", false);
		}catch(Exception e){e.printStackTrace();ExtDBLog.writeLog("Exception in getting CIFID from finacle");}
		try{
			PhoneNo = objXmlParser.getValueOf("PhoneNo", "", false);
		}catch(Exception e){e.printStackTrace();ExtDBLog.writeLog("Exception in getting PhoneNO from finacle");
		ExtDBLog.writeLog("Finacle value of FullName "+FullName);
		ExtDBLog.writeLog("Finacle value of CIFID "+CIFID);
		ExtDBLog.writeLog("Finacle value of PhoneNo "+PhoneNo);
		}

		if(temp[0].equals("PrimeFetch"))
		{
			try{
				
				CharSequence isCBF = "<IsCashbackForfeited></IsCashbackForfeited>";
				CharSequence disputeT = "<IsDisputedTran></IsDisputedTran>";
				CharSequence CIF = "<CIFID></CIFID>";
				if(msg[0].contains(isCBF))
					msg[0]=msg[0].replaceAll("<IsCashbackForfeited></IsCashbackForfeited>", "<IsCashbackForfeited>N</IsCashbackForfeited>");
				ExtDBLog.writeLog("I reached here2");
				if(msg[0].contains(disputeT))
					msg[0]=msg[0].replaceAll("<IsDisputedTran></IsDisputedTran>", "<IsDisputedTran>N</IsDisputedTran>");
				if(msg[0].contains(CIF))
					msg[0]=msg[0].replaceAll("<CIFID></CIFID>", "<CIFID>"+CIFID+"</CIFID>");					
				
				msg[0] = msg[0].substring(0, msg[0].indexOf("<CardSubType>")+"<CardSubType>".length())+strCardProduct+msg[0].substring(msg[0].indexOf("</CardSubType>"));
			msg[0]=msg[0].replaceAll("<CardType></CardType>", "<CardType>"+strCardType+"</CardType>"+"<CustomerName>"+FullName+"</CustomerName><MobileNo>"+PhoneNo+"</MobileNo>");
			}catch(Exception e){e.printStackTrace();ExtDBLog.writeLog("Exception in getting values of dispute transaction and cashback forfeited tags");}
		}
		else if(temp[0].equals("CardList"))
		{
			String strOutxml=msg[0];
			String strCardNoTag="CardNumber";
			XMLParser xp ;
			XMLParser xp2 =  new XMLParser(strOutxml);
			String cardholdertype="";
			String overdueAmount = "";
			String accountNo = "";
			String crnNoFetchedCard = "";
			try{
				overdueAmount=xp2.getValueOf("CurrentOverdueAmt", "", false);
			}catch(Exception e){ExtDBLog.writeLog("exception in getting first overdue amount "+overdueAmount);}	
			ExtDBLog.writeLog("getting first overdue amount "+overdueAmount);
			try{
				accountNo=xp2.getValueOf("AccountNumber", "", false);
			}catch(Exception e){ExtDBLog.writeLog("exception in getting first account no "+accountNo);}	
			ExtDBLog.writeLog("getting first account no"+accountNo);
			try{
				while (strOutxml.indexOf("<"+strCardNoTag+">")>-1)
				{
					String strCardNum=strOutxml.substring(strOutxml.indexOf("<"+strCardNoTag+">")+("<"+strCardNoTag+">").length(),strOutxml.indexOf("</"+strCardNoTag+">"));
					String strCardType=mqHelper.getCardType(connection, xmlparser, xmlgenerator, strCardNum,3);
					xp = new XMLParser(strOutxml);
					if (strCardNum.equals(strCardNo))
					{
						cardholdertype = xp.getValueOf("IsPrimaryCard", "", false);
						crnNoFetchedCard = xp.getValueOf("CRNNo", "", false);
					}
					if(strOutxml.indexOf("</CardAccountDetails>")<strOutxml.indexOf("</CardDetails>"))
					{
						overdueAmount = xp.getValueOf("CurrentOverdueAmt", "", false);
						accountNo = xp.getValueOf("AccountNumber", "", false);
						ExtDBLog.writeLog("getting next overdue amount "+overdueAmount);
					}
					msg[0]=msg[0].replaceAll("<"+strCardNoTag+">"+strCardNum+"</"+strCardNoTag+">","<"+strCardNoTag+">"+strCardNum+"</"+strCardNoTag+"><CardType>"+strCardType+"</CardType><CustomerName>"+FullName+"</CustomerName><MobileNo>"+PhoneNo+"</MobileNo><CIFID>"+CIFID+"</CIFID><card_holder_type></card_holder_type><OverDueAmount>"+overdueAmount+"</OverDueAmount><AccountNumber>"+accountNo+"</AccountNumber><c_crn_no>"+crnNoFetchedCard+"</c_crn_no>");
				
					strOutxml=strOutxml.substring(strOutxml.indexOf("</CardDetails>")+("</CardDetails>").length());					 xp=null;	
				}
				msg[0]=msg[0].replaceAll("<card_holder_type></card_holder_type>","<card_holder_type>"+cardholdertype+"</card_holder_type>");
				msg[0]=msg[0].replaceAll("<FreeField1></FreeField1>","<FreeField1>"+strCardNo+"</FreeField1>");
				
			}catch(Exception e){e.printStackTrace();ExtDBLog.writeLog("Exception in setting values not fetched.");}	
		}
		
		

		stringbuffer.append("<Output><Message>"+ msg[0]+"</Message>\n<Exception>\n<CompletionCode>0</CompletionCode>\n<ReasonCode>0</ReasonCode><Description>success</Description>\n</Exception>\n</Output>");
		ExtDBLog.writeLog(stringbuffer.toString());
		
		return stringbuffer.toString();
	}
	
	public String getCardDetails(Connection connection, XMLParser xmlparser,
			XMLGenerator xmlgenerator, String strCardNo)
	{
		String sQuery="select Card_Type, Card_Product, Merchant from USR_0_SRM_CARDS_BIN where Parameter = '"+strCardNo.substring(0,6)+"' and IsActive = 'Y'";
		String sEngineName=xmlparser.getValueOf("EngineName");
		String sSessionId=xmlparser.getValueOf("DMSSessionId");
		String sInputXML = "<?xml version='1.0'?><APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option><Query>" + sQuery + "</Query><EngineName>" + sEngineName + "</EngineName><SessionId>" + sSessionId + "</SessionId></APSelectWithColumnNames_Input>";
		xmlparser.setInputXML(sInputXML);
		String strOutXML=APSelectWithColumnNames(connection,xmlparser,xmlgenerator) ;	
		
		String cardType= "";
		String cardProduct = "";
		String merchant = "";
		String subXML="";
		XMLParser objXmlParser=null;
		ExtDBLog.writeLog("getting card details from cards decode table :strOutXML " +strOutXML);
		
		xmlparser.setInputXML(strOutXML);
		String mainCode = xmlparser.getValueOf("MainCode");
		int records= Integer.parseInt(xmlparser.getValueOf("TotalRetrieved"));
		if(mainCode.equals("0"))
		{	
			if(records>0)
			{
				for(int i=0;i<records;i++)
				{	subXML = xmlparser.getNextValueOf("Record");
					objXmlParser = new XMLParser(subXML);
					cardType=objXmlParser.getValueOf("Card_Type");
					cardProduct=objXmlParser.getValueOf("Card_Product");
					merchant=objXmlParser.getValueOf("Merchant");
				}
				
			}
		}
		ExtDBLog.writeLog("Card Type + Card Product: Merchant "+cardType+" "+cardProduct+" "+merchant);

		
		return cardType+","+cardProduct+","+merchant;
		
	}
	
	public String AppendOtherCardDetails_BOC(String XmlData, String strCardNo, String strCardType, String FullName, String PhoneNo, String CIFID)
	{
	
		String strOutxml=XmlData;
		String strCardNoTag="CardNumber";
		String cardholdertype="";
		XMLParser objXmlParser=null;
		try
		{
			while (strOutxml.indexOf("<"+strCardNoTag+">")>-1)
			{
				String strCardNum=strOutxml.substring(strOutxml.indexOf("<"+strCardNoTag+">")+("<"+strCardNoTag+">").length(),strOutxml.indexOf("</"+strCardNoTag+">"));
				// String strCardType=mqHelper.getCardType(connection, xmlparser, xmlgenerator, strCardNum,3);
				 
				objXmlParser = new XMLParser(strOutxml);
				if (strCardNum.equals(strCardNo))
				{
					cardholdertype = objXmlParser.getValueOf("IsPrimaryCard", "", false);
				}
				XmlData=XmlData.replaceAll("<"+strCardNoTag+">"+strCardNum+"</"+strCardNoTag+">","<"+strCardNoTag+">"+strCardNum+"</"+strCardNoTag+"><CardType>"+strCardType+"</CardType><CustomerName>"+FullName+"</CustomerName><MobileNo>"+PhoneNo+"</MobileNo><CIFID>"+CIFID+"</CIFID><card_holder_type></card_holder_type>");
			
				strOutxml=strOutxml.substring(strOutxml.indexOf("</CardDetails>")+("</CardDetails>").length()); objXmlParser=null;	
			}
			XmlData=XmlData.replaceAll("<card_holder_type></card_holder_type>","<card_holder_type>"+cardholdertype+"</card_holder_type>");		 			
		}
		catch(Exception e)
		{
			e.printStackTrace();ExtDBLog.writeLog("Exception in setting values not fetched.");
		}

		return XmlData;
	}
    	
}