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
//-------------------------------------------------------------------------
package com.newgen.mq;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import com.ibm.mq.MQC;
import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;

public class MQHelper  
{
	private Logger mLogger;
	private String hostName;
	private String port;
	private String qmgrName;
	//	private String ReqChannelId;
	private String channel;
	private String qNameSource;
	private String qNameDest;
	private String lTimeInterval;
	private String spl_TimeInterval;
	private String spl_callname;
	private MQMessage message;
	private MQQueueManager mqQueueManager;
	private MQQueue queue;

	public MQHelper(MQMessage message)
	{
		this.message = message;
	}
	public MQHelper(MQMessage message,Logger recMLogger)
	{
		this.message = message;
		this.mLogger= recMLogger;
	}

	public MQHelper()
	{

	}

	public String getMQResponse(String requestMessage)
	{
		String strReturnMsg = "";
		String req_name= "";
		if(requestMessage!=null && requestMessage.indexOf("<ProcessName>")>-1)
		{
			requestMessage=requestMessage.substring(0,requestMessage.indexOf("<ProcessName>")) +requestMessage.substring( requestMessage.indexOf("</ProcessName>")+14,requestMessage.length());
		}
		
		req_name =  (requestMessage.contains("<ReturnCode>")) ? requestMessage.substring(requestMessage.indexOf("<ReturnCode>")+"</ReturnCode>".length()-1,requestMessage.indexOf("</ReturnCode>")):"";
		

		StringBuffer stringbuffer = new StringBuffer();
		try{
			try {
				//mLogger.debug("Checking details\n HostName :" + hostName+"\nChannel : "+channel+"\n port : "+port+"\nqmgrName : "+qmgrName);
//				mLogger.debug("Formatted Message:" + requestMessage.toString());
				//mLogger.debug("MessageID:" + message.messageId);
				MQEnvironment.hostname = hostName;
				MQEnvironment.channel = channel;
				MQEnvironment.port = Integer.parseInt(port);
				mqQueueManager = new MQQueueManager(qmgrName);				


			} catch (NoClassDefFoundError noclass) {
				noclass.printStackTrace();
				strReturnMsg = "<Output><Description>No Class Def Found Exception.</Description>\n<Exception>\n<CompletionCode>"
					+ noclass
					+ "</CompletionCode>\n<ReasonCode>"
					+ noclass+ "</ReasonCode><ReturnCode>"+noclass+"</ReturnCode>";
				strReturnMsg = displayError(0, 0,
						strReturnMsg);
				mLogger.debug(strReturnMsg);
				stringbuffer.append(strReturnMsg);
				try { // try block added by Amandeep
					mqQueueManager.disconnect();
				} catch (Exception e) {
				}
				return stringbuffer.toString();
			} catch (MQException mqExp) {
				mLogger.debug("Exception while getting queue:"+mqExp.getMessage());
				//mLogger.debug(mqExp.printStackTrace());
				strReturnMsg = "<Output><Description>Error during connecting to Queue Manager.</Description>\n<Exception>\n<CompletionCode>"
					+ mqExp.completionCode
					+ "</CompletionCode>\n<ReasonCode>"
					+ mqExp.reasonCode + "</ReasonCode><ReturnCode>"+mqExp.reasonCode+"</ReturnCode>";
				strReturnMsg = displayError(mqExp.completionCode, mqExp.reasonCode,
						strReturnMsg);
				mLogger.debug(strReturnMsg);
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
				mLogger.debug(strReturnMsg);
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
				queue = mqQueueManager.accessQueue(qNameSource, openOption, null,null, null);
				// message.writeUTF(strMsg);
				message.replyToQueueName = qNameDest;
				//message.replyToQueueName = "clq_default_vm108";
				mLogger.debug("Message before putting in Queue: "+requestMessage); 
				message.writeString(requestMessage);
				MQPutMessageOptions pmo = new MQPutMessageOptions();
				message.format = "MQSTR";
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
				mLogger.debug(strReturnMsg);
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
				mLogger.debug(strReturnMsg);
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
				mLogger.debug(strReturnMsg);
				stringbuffer.append(strReturnMsg);
				return stringbuffer.toString();
			}
			// Get Message
			try {
				// int openOption2 = MQC.MQOO_INPUT_EXCLUSIVE | MQC.MQOO_BROWSE; //
				// open options for browse & share
				int openOption2 = MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_FAIL_IF_QUIESCING;
				mLogger.debug("A");

				queue = mqQueueManager.accessQueue(qNameDest, openOption2, null,null, null);

				MQGetMessageOptions gmo = new MQGetMessageOptions();

				gmo.options = MQC.MQGMO_WAIT | MQC.MQGMO_SYNCPOINT
				| MQC.MQGMO_FAIL_IF_QUIESCING; // MQC.MQGMO_WAIT |
				// MQC.MQGMO_BROWSE_FIRST;
				
				if(!"".equals(req_name) && spl_callname.contains(req_name)){
					gmo.waitInterval = Integer.parseInt(spl_TimeInterval);
				}
				else{
					gmo.waitInterval = Integer.parseInt(lTimeInterval);
				}
				MQMessage messageObj = new MQMessage();

				// message.messageId = messageId.getBytes();
				//mLogger.debug("Message Id"+message.messageId);
				messageObj.correlationId = message.messageId;
				mLogger.debug("Correlation Id"+messageObj.correlationId);

				messageObj.format = "MQSTR";
				mLogger.debug("message format set");
				queue.get(messageObj, gmo);
				mLogger.debug("getting message from queue");
				//strReturnMsg= messageObj.String(messageObj.getMessageLength());
				strReturnMsg= messageObj.readStringOfByteLength(messageObj.getMessageLength());

				mLogger.debug("response"+strReturnMsg);
				queue.close();

			} catch (MQException mqExp) {
				mLogger.debug("Inside MQ exception block");
				strReturnMsg = "<Output><Description>Error during getting message from Queue.</Description>\n<Exception>\n<CompletionCode>"
					+ mqExp.completionCode
					+ "</CompletionCode>\n<ReasonCode>"
					+ mqExp.reasonCode + "</ReasonCode><ReturnCode>"+mqExp.reasonCode+"</ReturnCode>";
				strReturnMsg = displayError(mqExp.completionCode, mqExp.reasonCode,
						strReturnMsg);
				mLogger.debug(strReturnMsg);
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
				mLogger.debug("Inside IO exception block");
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
				mLogger.debug(strReturnMsg);
				stringbuffer.append(strReturnMsg);
				return stringbuffer.toString();
			} catch (Exception exc) {
				mLogger.debug("Inside exc exception block");
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
				mLogger.debug(strReturnMsg);
				stringbuffer.append(strReturnMsg);
				return stringbuffer.toString();
			} finally {
				mLogger.debug("Inside final block");
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
				mLogger.debug(strReturnMsg);
				stringbuffer.append(strReturnMsg);
				return stringbuffer.toString();
			} catch (Exception exc) {
				strReturnMsg = "<Output><Description>Error during disconnecting from queue manager.</Description>\n<Exception>\n<ErrorMessageReasonCode>"
					+ exc.toString()
					+ "</ErrorMessageReasonCode>\n</Exception></Output>";
				mLogger.debug(strReturnMsg);
				stringbuffer.append(strReturnMsg);
				return stringbuffer.toString();
			}
		}catch(Exception e){System.out.println("Exception caught in MQHelper getResponse method "+e.getMessage());}
		return strReturnMsg;
	}
	private String displayError(int completionCode, int reasonCode, String sOutputXML) 
	{
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

	public void readPropertyFile()
	{
		try
		{
			Properties properties = new Properties();
			//            properties.load(getClass().getResourceAsStream("configFiles"+File.separator+"MQSettings.properties"));
			properties.load(new FileInputStream("configFiles"+File.separator+"MQSettings.properties"));

			hostName = properties.getProperty("HostName");
			port = properties.getProperty("PortNumber");
			qmgrName = properties.getProperty("QMgrName");
			//            ReqChannelId = properties.getProperty("RequestorChannelId");
			channel = properties.getProperty("MQChannelId");
			qNameSource = properties.getProperty("SourceQueueName");
			qNameDest = properties.getProperty("DestinationQueueName");
			lTimeInterval = properties.getProperty("TimeInterval");
			spl_TimeInterval = properties.getProperty("spl_TimeInterval");
			spl_callname = properties.getProperty("spl_callname");
		}
		catch(Exception exception)
		{
			System.out.println("exception in reading argument file...\n");
			exception.printStackTrace();
			System.exit(0);
		}
	}

}