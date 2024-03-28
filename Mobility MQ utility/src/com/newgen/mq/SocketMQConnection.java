package com.newgen.mq;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger; 
import org.apache.log4j.PropertyConfigurator;


import com.ibm.mq.MQMessage;
import com.newgen.dmsapi.DMSCallBroker;
//import com.newgen.mcap.core.external.logging.concrete.LogMe;
import com.newgen.mq.MapXML;
//import com.newgen.mq.User;
import com.newgen.mq.MQHelper;

import com.newgen.omni.wf.util.app.NGEjbClient;
import com.newgen.wfdesktop.xmlapi.WFCallBroker;
/*-------------------------------------------------------------Change History------------------------------------------------------------------
 * Deepak	16-Sept-2017	random no added in message ID to make is unique		//Changes done to add random number in the end of message ID.
 * */
public class SocketMQConnection 
{
    static Properties propConfig;
    static int port;
	static int socketTimeout;
    
    static String strPropertyPath;
	
    boolean flagSocketSetUp;
    private static Logger mLogger;
	
//	static String sUserName;
//	static String sPassword;
	static Map <String,String> configPropertyMap = new HashMap<String, String>();
		
//	private static User userPwd;	
	
	public static NGEjbClient ngEjbClient;
	
    static
	{
		mLogger = Logger.getLogger("mLogger");
		
		try
		{
			ngEjbClient = NGEjbClient.getSharedInstance();
		}
		catch(Exception e)
		{
			mLogger.info("Exception in static block: "+ e.getMessage());
		}
		
		System.out.println("Reading Configuration Parameters Starts");
		mLogger.info("Reading Configuration Parameters Starts");
		
    	FileInputStream proConfigFileStream;
		
		try 
		{
			strPropertyPath = "configFiles"+File.separator+"Config.properties";
			proConfigFileStream = new FileInputStream(strPropertyPath);
			propConfig = new Properties();
			propConfig.load(proConfigFileStream);
			
			configPropertyMap.clear();
			Set<Object> keys = propConfig.keySet();
			
			for(Object k:keys)
			{
				configPropertyMap.put((String)k,propConfig.getProperty((String)k));
			}
			
			strPropertyPath=null;proConfigFileStream=null;propConfig=null;keys=null;
			
			
			port =Integer.parseInt(configPropertyMap.get("SocketServerPort"));
			socketTimeout =Integer.parseInt(configPropertyMap.get("SocketTimeOut"));
			
//			userPwd=new User("main");
//			sUserName=userPwd.getUsername();
//			sPassword=userPwd.getPassword();
			
		} 
		catch (FileNotFoundException e) 
		{
			mLogger.error("Config.properties not found. "+e);
		}
		catch (IOException e) 
		{
			mLogger.error("Config.properties Load/Read Failed with IOException. "+e);
		}
		
		System.out.println("Reading Configuration Parameters Completed");
		mLogger.info("Reading Configuration Parameters Completed");
	}
    
    
	
	Logger mLogger()
	{
        return mLogger;
    }
	
	private static void createLogFile()
	{
		try
		{
			Date date = new Date();
			DateFormat logDateFormat = new SimpleDateFormat("dd-MM-yyyy");
			String dynamicLog = "Log/"+logDateFormat.format(date)+"/ReceiveFile.xml"; 
			
			Properties p = new Properties();
			p.load(new FileInputStream("log4j.properties"));  
			String orgFileName = p.getProperty("log4j.appender.mLogger.File");
			
			if(!(orgFileName==null || orgFileName.equalsIgnoreCase("")))
			{
				dynamicLog = "Log/"+logDateFormat.format(date)+orgFileName.substring(orgFileName.lastIndexOf("/"));
			}
			
			File d = new File("Log/"+logDateFormat.format(date));
			d.mkdirs();
			File fl = new File(dynamicLog);
			if(!fl.exists())
				fl.createNewFile();
			
			p.put("log4j.appender.mLogger.File", dynamicLog ); // overwrite "log.dir"  
			PropertyConfigurator.configure(p); 
		}
		catch(Exception e)
		{
			System.out.println("Eexception in creating dynamic log :"+e);
			mLogger.error("Eexception in creating dynamic log :"+e);
		}
	}
	
    class completeOperation implements Runnable
	{		
	    private Socket socket;
	    private String msgID="";
		
		completeOperation(Socket server) 
		{
			this.socket=server;
		}
		
		
		private InputStream in = null;
		private OutputStream out = null;
		
		private DataInputStream din = null;
		private DataOutputStream dout = null;
			
		public void run()
		{
			try
			{
				in = socket.getInputStream();
				out = socket.getOutputStream();
			}
			catch (IOException ex)
			{
				mLogger.error("Can't get socket input stream. "+ex);
				System.out.println("Can't get socket input stream. "+ex);
			}		
			
			din = new DataInputStream(in);
			dout = new DataOutputStream(out);				
			
			StringBuffer tempOutputResponse = new StringBuffer();
			tempOutputResponse.append("<APMQPUTGET_Output>\n<MQ_RESPONSE_XML>");
			String strOutput="<APMQPUTGET_Output>\n<MQ_RESPONSE_XML>DEFAULT_RESPONSE<MQ_RESPONSE_XML>\n</APMQPUTGET_Output>";
			
			String strInput=readData(din);
			String Msg_id="";
			try
			{	
				//mLogger.info("Processing of Input Request starts..###"+strInput+"###");
				//System.out.println("Processing of Input Request starts...");
				Msg_id = genMessageID();
				//writeData(dout,Msg_id);
				String mqIpXML=MapXML.getTagValueusingSubstring(strInput,"MQ_REQUEST_XML");
				
				
				String callName="";
				
				//String callName=MapXML.getTagValue(mqIpXML,"MsgFormat").trim();
				//mLogger.debug("Updated mqIpXML "+mqIpXML);
				if(mqIpXML.contains("ServiceProviderId")){
					callName=MapXML.getTagValue(mqIpXML,"ServiceProviderId").trim();
				}
				/*else if(mqIpXML.contains("RAKHeader:RequestID")){
					callName=MapXML.getTagValue(mqIpXML,"ServiceProviderId").trim();
				}*/
				else{
					callName=MapXML.getTagValue(mqIpXML,"MsgFormat").trim();
				}
				
				String requestType ="";
				if(callName.equalsIgnoreCase("CUSTOMER_EXPOSURE")){
					requestType = MapXML.getTagValue(mqIpXML,"RequestType").trim();
				}
					
					
				if(validateSessionID(MapXML.getTagValue(strInput,"SessionId"),MapXML.getTagValue(strInput,"EngineName")))
				{
					if(((configPropertyMap.get("ALLCALLDUMMY")!=null)&&(configPropertyMap.get("ALLCALLDUMMY").equalsIgnoreCase("Y")))
							||((configPropertyMap.get(callName)!=null)&&(configPropertyMap.get(callName).equalsIgnoreCase("Y"))))
					{
						tempOutputResponse.append(getDummyResponse(callName,requestType));
					}
					else
					{
						tempOutputResponse.append(getOutputMessageFromMQ(mqIpXML,Msg_id));
					}
					tempOutputResponse.append("</MQ_RESPONSE_XML>\n</APMQPUTGET_Output>");
				}
				else
				{
					tempOutputResponse.append("INVALID SESSION");
					tempOutputResponse.append("</MQ_RESPONSE_XML>\n</APMQPUTGET_Output>");
				}
				strOutput = tempOutputResponse.toString();
				mLogger.error("strOutput@@ :"+strOutput);
				writeData(dout,strOutput);
			}
			catch(EOFException eofExcp)
			{
				tempOutputResponse.append("EOFException "+eofExcp);
				tempOutputResponse.append("</MQ_RESPONSE_XML>\n</APMQPUTGET_Output>");
				strOutput=tempOutputResponse.toString();
				writeData(dout,strOutput);
				
				mLogger.error("EOFException Occured :"+eofExcp);
				StringWriter errors = new StringWriter();
				eofExcp.printStackTrace(new PrintWriter(errors));						
				mLogger.error(errors.toString());		
				
				try
				{
					out.close();
					in.close();
					socket.close();
				}
				catch (IOException ioE)
				{}
			}
			catch(SocketException socketExcp)
			{
				tempOutputResponse.append("SocketException "+socketExcp);
				tempOutputResponse.append("</MQ_RESPONSE_XML>\n</APMQPUTGET_Output>");
				strOutput=tempOutputResponse.toString();
				writeData(dout,strOutput);
				
				System.out.println("Socket Exception Occured :"+socketExcp);
				mLogger.error("Socket Exception Occured :"+socketExcp);
				StringWriter errors = new StringWriter();
				socketExcp.printStackTrace(new PrintWriter(errors));						
				mLogger.error(errors.toString());	
				
				try
				{
					out.close();
					in.close();
					socket.close();
				}
				catch (IOException ioE)
				{}
			}
			catch(SocketTimeoutException socketTimeoutExcp)
			{
				tempOutputResponse.append("SocketTimeoutException "+socketTimeoutExcp);
				tempOutputResponse.append("</MQ_RESPONSE_XML>\n</APMQPUTGET_Output>");
				strOutput=tempOutputResponse.toString();
				writeData(dout,strOutput);
				
				System.out.println("Socket Time out Exception Occured :"+socketTimeoutExcp);
				mLogger.error("Socket Exception Occured :"+socketTimeoutExcp);
				StringWriter errors = new StringWriter();
				socketTimeoutExcp.printStackTrace(new PrintWriter(errors));						
				mLogger.error(errors.toString());	
				try
				{
					out.close();
					in.close();
					socket.close(); 
				}
				catch (IOException ioE)
				{}
			}
			catch (Exception excp) 
			{
				tempOutputResponse.append("Exception "+excp);
				tempOutputResponse.append("</MQ_RESPONSE_XML>\n</APMQPUTGET_Output>");
				strOutput=tempOutputResponse.toString();
				writeData(dout,strOutput);
				
				System.out.println("Exception Occured :"+excp);
				mLogger.error("Exception Occured :"+excp);
				
				final Writer result = new StringWriter();
				final PrintWriter printWriter = new PrintWriter(result);
				excp.printStackTrace(printWriter);
				mLogger.error("Exception AK : "+result.toString());
				
				StringWriter errors = new StringWriter();
				excp.printStackTrace(new PrintWriter(errors));						
				mLogger.error(errors.toString());	
				try
				{
					out.close();
					in.close();
					socket.close(); 
				}
				catch (IOException ioE)
				{}
			}
			finally
			{
				try
				{
					if (socket!=null)
					{	
						out.close();
						in.close();
						socket.close();
						socket=null;
					}
				}
				catch(Exception exCl)
				{
					System.out.println("Exception " + exCl.toString());
				}
				
				try
				{					
					insertReqRespINDatabase(strInput,strOutput,Msg_id);
				}
				catch(Exception exc2)
				{
					System.out.println("Exception " + exc2.toString());
				}
				System.gc();
			}
			mLogger.info("Socket Connection Completed...");
			System.out.println("Socket Connection Completed...");
		}
		  private String getOutputMessageFromMQ(String paramString, String Msg_ID)
		  {
			  String str1 = "";
			  SocketMQConnection.mLogger.debug("Inside getOutputMessageFromMQ");
			  Date localDate = new Date();
			  SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.mmm");
			  String str2 = localSimpleDateFormat.format(localDate) + "+04:00";

			  MQMessage localMQMessage = new MQMessage();
			  MQHelper localMQHelper = new MQHelper(localMQMessage, SocketMQConnection.mLogger);
			  localMQHelper.readPropertyFile();

			  //Timestamp localTimestamp = new Timestamp(System.currentTimeMillis());
			  //Changes done to add random number in the end of message ID.
			  //Random rdn = new Random();
			  this.msgID = Msg_ID;
			  //SocketMQConnection.mLogger.debug("Deepak new msdID: " + this.msgID);
			  //SocketMQConnection.mLogger.debug("Deepak new msdID: " + paramString);
			  try
			  {
				  //SocketMQConnection.mLogger.debug("whole paramstring msdID: " + paramString);
				  if (paramString.contains("RAKHeader:RequestID")) {
					  paramString = paramString.substring(0, paramString.indexOf("<RAKHeader:RequestID>") + 21) + this.msgID + paramString.substring(paramString.indexOf("</RAKHeader:RequestID>"));
				  }
				  else if (paramString.contains("soap:Header")) {
					  SocketMQConnection.mLogger.debug("Iside else if condition" + paramString);
					  paramString = paramString.substring(0, paramString.indexOf("<RequestID>") + 11) + this.msgID + paramString.substring(paramString.indexOf("</RequestID>"));
					  paramString = paramString.substring(0, paramString.indexOf("<TimeStampyyyymmddhhmmsss>") + 26) + str2 + paramString.substring(paramString.indexOf("</TimeStampyyyymmddhhmmsss>"));
				  }
				  else {
					  paramString = paramString.substring(0, paramString.indexOf("<MessageId>") + 11) + this.msgID + paramString.substring(paramString.indexOf("</MessageId>"));
					  paramString = paramString.substring(0, paramString.indexOf("<Extra2>") + 8) + str2 + paramString.substring(paramString.indexOf("</Extra2>"));
				  }
				  //code moved from here to when XML is received. 
				  // paramString = paramString.replaceAll("&", "&amp;");

				  str1 = localMQHelper.getMQResponse(paramString);
				  //SocketMQConnection.mLogger.debug("Outputr From MQ " + str1);
				  mLogger.info("str1..."+str1);
				  return str1;
			  }
			  catch (Exception localException)
			  {
				  SocketMQConnection.mLogger.debug("Exception in getting response from MQHelper " + localException.getMessage()); }
			  return "<Output><Description>Error during getting response from MQHelper</Description>\n<Exception>\n<ReasonCode>-1</ReasonCode>\n</Exception></Output>";
		  }
		

    }
    private String genMessageID(){
    	String Msgid="";
    	try{
    		Timestamp localTimestamp = new Timestamp(System.currentTimeMillis());
    		Random rdn = new Random();
    		Msgid = "CAS" + localTimestamp.getTime()+rdn.nextInt(100);
    	}
    	catch(Exception e){
    		SocketMQConnection.mLogger.debug("Exception occured in genMessageID"+e.getMessage());
    		e.printStackTrace();
    	}
    	return Msgid;
    }
	
	private boolean validateSessionID(String sessionID,String cabName)
	{
		return true;
	/*	try
		{
			mLogger.debug("sessionID in Validate Session ID : "+sessionID);
			if(sessionID==null || sessionID.equalsIgnoreCase(""))
			{
				return false;
			}
			
			String inputXML = getAPUpdateXML("PDBCONNECTION","HOSTNAME","'A'","1=2",cabName,sessionID);
						
			mLogger.debug("inputXML validate session ID "+inputXML);			
			String outputXML =  WFNGExecute(inputXML,configPropertyMap.get("JTSIP"),configPropertyMap.get("JTSPORT"),1);
			//String outputXML =makeCall(configPropertyMap.get("JTSIP"), Short.valueOf(configPropertyMap.get("JTSPORT")), inputXML);
			mLogger.debug("outputXML validate session ID "+outputXML);
			
			inputXML=null;
			if(MapXML.getTagValue(outputXML,"MainCode").equalsIgnoreCase("0"))
			{
				outputXML=null;
				return true;
			}
			outputXML=null;
			return false;
		}
		catch(Exception e)	
		{
			mLogger.error("Exception in validate SessionID method "+e);
			return false;
		}
	*/}
	
	private String getAPUpdateXML(String tableName,String columnName,String strValues,String sWhere,String engineName,String sessionId)
	{
		StringBuffer bfrInputXML = new StringBuffer();
		bfrInputXML.append("<?xml version=\"1.0\"?>\n");
		bfrInputXML.append("<APUpdate_Input>\n");
		bfrInputXML.append("<Option>APUpdate</Option>\n");
		bfrInputXML.append("<TableName>");
		bfrInputXML.append(tableName);
		bfrInputXML.append("</TableName>");
		bfrInputXML.append("<ColName>");
		bfrInputXML.append(columnName);
		bfrInputXML.append("</ColName>");
		bfrInputXML.append("<Values>");
		bfrInputXML.append(strValues);
		bfrInputXML.append("</Values>");
		bfrInputXML.append("<WhereClause>");
		bfrInputXML.append(sWhere);
		bfrInputXML.append("</WhereClause>");
		bfrInputXML.append("<EngineName>");
		bfrInputXML.append(engineName);
		bfrInputXML.append("</EngineName>");
		bfrInputXML.append("<SessionId>");
		bfrInputXML.append(sessionId);
		bfrInputXML.append("</SessionId>");
		bfrInputXML.append("</APUpdate_Input>");		
		return bfrInputXML.toString();
	}
	
	private String getAPProcedureInputXML_1(String engineName,String sSessionId,String procName,String Params)
	{
		StringBuffer bfrInputXML = new StringBuffer();
		bfrInputXML.append("<?xml version=\"1.0\"?>\n");
		bfrInputXML.append("<APProcedure_Input>\n");
		bfrInputXML.append("<Option>APProcedure2</Option>\n");
		bfrInputXML.append("<ProcName>");
		bfrInputXML.append(procName);
		bfrInputXML.append("</ProcName>");
		bfrInputXML.append("<Params>");
		bfrInputXML.append(Params);
		bfrInputXML.append("</Params>");
		bfrInputXML.append("<NoOfCols>");
		bfrInputXML.append("1");
		bfrInputXML.append("</NoOfCols>");		
		bfrInputXML.append("<EngineName>");
		bfrInputXML.append(engineName);
		bfrInputXML.append("</EngineName>");
		bfrInputXML.append("<SessionId>");
		bfrInputXML.append(sSessionId);
		bfrInputXML.append("</SessionId>");
		bfrInputXML.append("</APProcedure_Input>");		
		return bfrInputXML.toString();
	}
	
	private String getAPProcedureInputXML_2(String engineName,String sSessionId,String procName,String Params)
	{
		StringBuffer bfrInputXML = new StringBuffer();
		bfrInputXML.append("<?xml version=\"1.0\"?>\n");
		bfrInputXML.append("<APProcedure2_Input>\n");
		bfrInputXML.append("<Option>APProcedure2</Option>\n");
		bfrInputXML.append("<ProcName>");
		bfrInputXML.append(procName);
		bfrInputXML.append("</ProcName>");
		bfrInputXML.append("<Params>");
		bfrInputXML.append(Params);
		bfrInputXML.append("</Params>");
		bfrInputXML.append("<NoOfCols>");
		bfrInputXML.append("1");
		bfrInputXML.append("</NoOfCols>");		
		bfrInputXML.append("<EngineName>");
		bfrInputXML.append(engineName);
		bfrInputXML.append("</EngineName>");
		bfrInputXML.append("<SessionId>");
		bfrInputXML.append(sSessionId);
		bfrInputXML.append("</SessionId>");
		bfrInputXML.append("</APProcedure2_Input>");		
		return bfrInputXML.toString();
	}
	
	private String getAPProcedureInputXML(String engineName,String sSessionId,String procName,String Params)
	{
		StringBuffer bfrInputXML = new StringBuffer();
		bfrInputXML.append("<?xml version=\"1.0\"?>\n");
		bfrInputXML.append("<APProcedure_WithDBO_Input>\n");
		bfrInputXML.append("<Option>APProcedure_WithDBO</Option>\n");
		bfrInputXML.append("<ProcName>");
		bfrInputXML.append(procName);
		bfrInputXML.append("</ProcName>");
		bfrInputXML.append("<Params>");
		bfrInputXML.append(Params);
		bfrInputXML.append("</Params>");
		bfrInputXML.append("<EngineName>");
		bfrInputXML.append(engineName);
		bfrInputXML.append("</EngineName>");
		bfrInputXML.append("<SessionId>");
		bfrInputXML.append(sSessionId);
		bfrInputXML.append("</SessionId>");
		bfrInputXML.append("</APProcedure_WithDBO_Input>");		
		return bfrInputXML.toString();
	}
	
	public static String getConnectInputXML(String cabinetName,	String username, String password)
	{
		StringBuffer ipXMLBuffer=new StringBuffer();
		
		ipXMLBuffer.append("<?xml version=\"1.0\"?>");
		ipXMLBuffer.append("<WMConnect_Input>");
		ipXMLBuffer.append("<Option>WMConnect</Option>");
		ipXMLBuffer.append("<EngineName>");
		ipXMLBuffer.append(cabinetName);
		ipXMLBuffer.append("</EngineName>\n");
		ipXMLBuffer.append("<ApplicationInfo></ApplicationInfo>\n");
		ipXMLBuffer.append("<Participant>\n");
		ipXMLBuffer.append("<Name>");
		ipXMLBuffer.append(username);
		ipXMLBuffer.append("</Name>\n");
		ipXMLBuffer.append("<Password>");
		ipXMLBuffer.append(password);
		ipXMLBuffer.append("</Password>\n");
		ipXMLBuffer.append("<Scope></Scope>\n");
		ipXMLBuffer.append("<UserExist>N</UserExist>\n");
		ipXMLBuffer.append("<Locale>en-us</Locale>\n");
		ipXMLBuffer.append("<ParticipantType>U</ParticipantType>\n");
		ipXMLBuffer.append("</Participant>");
		ipXMLBuffer.append("</WMConnect_Input>");
		
		return ipXMLBuffer.toString(); 
	}
	
	private String WFNGExecute(String ipXML, String jtsServerIP, String serverPort,int flag) throws IOException,Exception
	{
		mLogger.debug("In WF NG Execute : "+serverPort);
		try{
		if(serverPort.startsWith("33"))
			return WFCallBroker.execute(ipXML,jtsServerIP,Integer.parseInt(serverPort),1);
		else
			return ngEjbClient.makeCall(jtsServerIP,serverPort,"WebSphere",ipXML);
		}
		catch(Exception e){
			mLogger.debug("Exception Occured in WF NG Execute : "+e.getMessage());
			e.printStackTrace();
			return "Error";
		}
	}
	// added by abhishek
	
	//with wrapper
	/*private String makeCall(String jtsAddress, short jtsPort, String
			inputXML)
	throws Exception {
		String output = null;
		try {
			int debug = 0; // (0|1)
			output = DMSCallBroker
			.execute(inputXML, jtsAddress, jtsPort, debug);
		} catch (Exception e) {
			e.printStackTrace();
			mLogger.info("error=> " + e);
			throw new Exception("OF setup incomplete");
		} catch (Error e) {
			e.printStackTrace();
			mLogger.info("error=> " + e);
			throw new Exception("OF setup incomplete");
		}
		return output;
	}*/

    public static void main(String[] args) throws IOException
	{
		createLogFile();
		int threadCount=0;

		ServerSocket serverSocket = null;
		Socket socket=null;
		
		try
		{
			System.out.println("Setting up socket");
			mLogger.info("Setting up socket");
            
			serverSocket = new ServerSocket(port);
			
			System.out.println("Socket Set up successful on port : "+port);
			mLogger.info("Socket Set up successful on port : "+port);
			
			while(true)
			{
				threadCount++;
				mLogger.info("Waiting for Request Count : " + threadCount);
				System.out.println("Waiting for Request Count--" + threadCount);
				socket = serverSocket.accept();
				socket.setSoTimeout(socketTimeout);
				SocketMQConnection socketMQConnection =  new SocketMQConnection();
				completeOperation conn_c= socketMQConnection.new completeOperation(socket);
				Thread t = new Thread(conn_c);
				t.start();
			}			
        }
		catch (IOException ex)
		{   
        	System.out.println("Can't setup server on this port number. "+ex);
        	mLogger.error("Can't setup server on this port number. "+ex);            
        }
		finally
		{
			try
			{
				if (serverSocket!=null)
				{
					serverSocket.close();
					serverSocket=null;
					System.out.println("Closing Listener");
					mLogger.info("Closing Listener");
				}
				if (socket!=null)
				{
					socket.close();
					socket=null;
					System.out.println("Closing Server Socket");
					mLogger.info("Closing Server Socket");
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				mLogger.info("Exception " + e.toString());
				System.out.println("Exception " + e.toString());
			}
			System.gc();
		}
    }
	
	private String readData(DataInputStream xmlDataInputStream)
    {
    	String recvedMessage="";
		try {
			byte[] readBuffer = new byte[50000];
			int num = xmlDataInputStream.read(readBuffer);
			boolean wait_flag = true;
			int out_len=0;

			if (num > 0) 
			{
				while(wait_flag){
					mLogger.debug("readData"+" num :"+num);
					byte[] arrayBytes = new byte[num];
					System.arraycopy(readBuffer, 0, arrayBytes, 0, num);
					recvedMessage = recvedMessage + new String(arrayBytes, "UTF-16LE");
				//	mLogger.debug("readData"+" inside loop output Response :\n"+recvedMessage);
					if(recvedMessage.contains("##8##;")){
						String[]	mqOutputResponse_arr = recvedMessage.split("##8##;");
						recvedMessage = mqOutputResponse_arr[1];
						out_len = Integer.parseInt(mqOutputResponse_arr[0]);
						
						//mLogger.debug("readData"+" First Output Response :\n"+recvedMessage);
						//mLogger.debug("readData"+" Output length :\n"+out_len);
					}
					//if(out_len <= recvedMessage.getBytes("UTF-16LE").length){
					//below code change not to run last loop in which we are geting timeout exception.
					//mLogger.debug("out_len: "+out_len);
					//mLogger.debug("recvedMessage_len: "+ recvedMessage.getBytes("UTF-16LE").length);
					if(recvedMessage.getBytes("UTF-16LE").length == out_len){
						wait_flag=false;
						mLogger.debug("readData"+" Condition reached to end loop :\n output len: "+out_len+ " Message length: "+recvedMessage.getBytes("UTF-16LE").length);
						break;
					}
					try{
					Thread.sleep(50);
					}
					catch(Exception e){
						mLogger.debug("readData() "+ "exception occured in thread sleep: "+ e.getMessage());
					}
					if(wait_flag){
						mLogger.debug("Waiting flag is false: value: "+ wait_flag);
						num = xmlDataInputStream.read(readBuffer);
						mLogger.debug("Message read"+ num);
					}
					
				}
				//mLogger.debug("readData"+" Final Output Response :\n"+recvedMessage);
				//xmlDataInputStream.close();
				return recvedMessage.replaceAll("&", "&amp;");
			}
			else 
			{
                notify();
            }
        }
		catch (SocketException se) 
		{
            mLogger.error("Socket Exception Occured in Read Data :" + se);
			return recvedMessage;
        }
		catch (IOException i)
		{	
            mLogger.error("IO Exception Occured in Read Data :" + i);
			i.printStackTrace();
        }
		return recvedMessage.replaceAll("&", "&amp;");
    }
	
	
	private void writeData(DataOutputStream xmlDataOutputStream,String input)
    {
         try 
         {
         	String return_message = input;
             if (return_message != null && return_message.length() > 0) 
             {                 
            	//int response = return_message.getBytes("UTF-16LE").length;
            	//return_message = response + "##8##;" + return_message;
            	//mLogger.debug("WriteData"+" Testing CAS :\n"+return_message);
               	xmlDataOutputStream.write(return_message.getBytes("UTF-16LE"));
               	//mLogger.debug("WriteData"+" Testing CAS Done:\n"+return_message);
               	xmlDataOutputStream.flush();
            	//xmlDataOutputStream.close();
            	//mLogger.debug("WriteData"+" Final Output Response :\n"+return_message);
             }             
         }
         catch (IOException i) 
         {
        	 mLogger.debug("Exception occured while puting data: "+ i.getMessage());
             i.printStackTrace();	
         }
         catch (Exception ie) 
         {
        	 mLogger.debug("Exception occured while puting data: "+ ie.getMessage());
             ie.printStackTrace();
         }
    }
	
	private boolean insertReqRespINDatabase(String ipXML,String opXML,String messageID)
	{
		//return true;
		try
		{
			String params="'"+MapXML.getTagValue(ipXML,"XMLHISTORY_TABLENAME")+"'"
							+",'" +MapXML.getTagValue(ipXML,"WI_NAME")+"'"
							+",'" +MapXML.getTagValue(ipXML,"WS_NAME")+"'"
							+",'" +MapXML.getTagValue(ipXML,"MsgFormat").trim()+"'"
							+",'" +MapXML.getTagValue(ipXML,"USER_NAME")+"'"
							+",'" +messageID+"'"
							+",'" +ipXML+"'"
							+",'" +opXML+"'"
							;
			String inputXML = getAPProcedureInputXML(MapXML.getTagValue(ipXML,"EngineName"),MapXML.getTagValue(ipXML,"SessionId"),configPropertyMap.get("XML_INSERT_PROC_NAME"),params);			
			
			mLogger.debug("inputXML AP Procedure XML Entry "+inputXML);
			//String outputXML =  WFNGExecute(inputXML,configPropertyMap.get("JTSIP"),configPropertyMap.get("JTSPORT"),1);
			String outputXML =makeCall(configPropertyMap.get("JTSIP"), Short.valueOf(configPropertyMap.get("JTSPORT")), inputXML);
			mLogger.debug("outputXML AP Procedure Transaction Entries  "+outputXML);			
			
			if(MapXML.getTagValue(outputXML,"MainCode").equalsIgnoreCase("0"))
			if(outputXML.indexOf("<MainCode>0</MainCode>")>-1)	
			{
				return true;
			}	
			else
			{
				return true;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	private String makeCall(String jtsAddress, short jtsPort, String
			inputXML)
	throws Exception {
		mLogger.debug( "Inside make call() function of MQ utility");
		String output = null;
		try {
			int debug = 0; // (0|1)
			output = DMSCallBroker
			.execute(inputXML, jtsAddress, jtsPort, debug);
		} catch (Exception e) {
			e.printStackTrace();
			mLogger.debug( "error=> " + e.getMessage());
			throw new Exception("OF setup incomplete");
		} catch (Error e) {
			e.printStackTrace();
			mLogger.debug("error=> " + e.getMessage());
			throw new Exception("OF setup incomplete");
		}
		return output;
	}
		
	private String getDummyResponse(String file,String requestType)
	{
		FileReader fr = null;
		BufferedReader br = null;
		String msg = "";
		try
		{
			mLogger.info("inside get msg as string method");
			char[] c = { 0x0D, 0x0A };
			String crlf = new String(c);
			if(file.equalsIgnoreCase("CUSTOMER_EXPOSURE")){
				fr = new FileReader("dummyResponseXML"+File.separator+requestType+".xml");
			}else{
				fr = new FileReader("dummyResponseXML"+File.separator+file+".xml");
			}
			br = new BufferedReader(fr);
			String line = br.readLine();
			while(line!=null)
			{
				msg += line.trim()+crlf;
				line = br.readLine();
			}
			int msgLength = msg.length();
			if(msgLength == 0)
			{
				msg = "blank";
			}
			else
			{
				msg = msg.substring(0,msgLength-crlf.length());
			}
			mLogger.info("completion : get msg as string method");
		}
		catch(Exception e)
		{
			final Writer result = new StringWriter();
			final PrintWriter printWriter = new PrintWriter(result);
			e.printStackTrace(printWriter);
			mLogger.error("Exception while converting the file into String : "+result.toString());
			msg = "error In reading Dummy Response File";
		}
		finally
		{
			try 
			{
				fr.close();
				br.close();
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}			
		}
		return msg;
	}
}