<!-------------------------------------------------------------------------------------------------------------------------------------
//           NEWGEN SOFTWARE TECHNOLOGIES LIMITED

//Group						 : Application –Projects
//Product / Project			 : RAKBank 
//Module                     : Request-Initiation 
//File Name					 : VerifySignature.jsp
//Author                     : 	
//Date written (DD/MM/YYYY)  : 05-Feb-2016
//Description                : File used to upload signature status in external table and download document
CHANGE HISTORY

---------------------------------------------------------------------------------------------------------------------------------------
Problem No/CR No              Change Date            Changed By             Change Description
-------------------------------------------------------------------------------------------------------------------------------------->

<%@ include file="Log.process"%>
<jsp:useBean id="wDSession" class="com.newgen.wfdesktop.session.WDSession" scope="session"/>
<jsp:useBean id="WDCabinet" class="com.newgen.wfdesktop.baseclasses.WDCabinetInfo" scope="session"/>

<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.io.*,java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.net.*"%>
<%@ page import="com.newgen.wfdesktop.xmlapi.*" %>
<%@ page import="com.newgen.wfdesktop.util.*" %>
<%@ page import="XMLParser.XMLParser"%>   
<%@ page import="com.newgen.custom.*" %>
<%@ page import="javax.crypto.Cipher" %>
<%@ page import="javax.crypto.spec.SecretKeySpec" %>
<%@ page import="org.apache.commons.codec.binary.Base64" %>
<%@ page import="java.io.UnsupportedEncodingException" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.net.URLDecoder" %>

<%@ page import="java.io.ByteArrayOutputStream,java.io.File,java.io.FileInputStream,java.io.IOException,java.net.HttpURLConnection,java.net.URL,java.net.URLConnection" %>
<%@ page import="com.newgen.custom.*,ISPack.ISUtil.JPISException,org.apache.commons.codec.binary.Base64,Jdts.DataObject.JPDBString,ISPack.CImageServer" %>

<%@page import="com.newgen.omni.wf.util.app.NGEjbClient"%>
<%@page import="com.newgen.omni.wf.util.excp.NGException"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.owasp.esapi.ESAPI"%>
<%@ page import="org.owasp.esapi.codecs.OracleCodec"%>
<%@ page import="org.owasp.esapi.User" %>

<%
//svt points start
String VolumeId="1";


Integer SiteId=1;

	
	String input1 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("CustomerName"), 1000, true) );
	String CustomerName = ESAPI.encoder().encodeForSQL(new OracleCodec(), input1);
	
	String input2 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("docIndex"), 1000, true) );
	String Mandates = ESAPI.encoder().encodeForSQL(new OracleCodec(), input2);
	
	String input3 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("AccountNo"), 1000, true) );
	String AccountNo = ESAPI.encoder().encodeForSQL(new OracleCodec(), input3);
	
	String input4 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("CifId"), 1000, true) );
	String CifId = ESAPI.encoder().encodeForSQL(new OracleCodec(), input4);
	
	String input5 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("workstepName"), 1000, true) );
	String wsname = ESAPI.encoder().encodeForSQL(new OracleCodec(), input5);
	
	String input6 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("WIName"), 1000, true) );
	String WIName = ESAPI.encoder().encodeForSQL(new OracleCodec(), input6);
	WriteLog("verify Signature WINAME: "+WIName);
	String input7 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("ItemIndex"), 1000, true) );
	String ItemIndex = ESAPI.encoder().encodeForSQL(new OracleCodec(), input7);
	
	String input8 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("CustSeqNo"), 1000, true) );
	String CustSeqNo = ESAPI.encoder().encodeForSQL(new OracleCodec(), input8);

//svt points end

String appServerType = wDSession.getM_objCabinetInfo().getM_strAppServerType();
	String wrapperIP = wDSession.getM_objCabinetInfo().getM_strServerIP();
	String wrapperPort = wDSession.getM_objCabinetInfo().getM_strServerPort()+"";    
	String sessionId = wDSession.getM_objUserInfo().getM_strSessionId();
	String EngineName = wDSession.getM_objCabinetInfo().getM_strCabinetName();



%>
<%! 
String strSignatureStatusTable = "AO_signature_status";
String fileDownloadLoc="CustomLog";
String downloadStatus="";
String uploadDocStatus="";
String subXML;
String subXMLParser = null;
String trDate = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS").format(new Date());
%>

<% 
WriteLog("Inside ProcessWI... for CustSeqNo ");
String msg=processWI(ItemIndex,sessionId,EngineName,WIName,AccountNo,CifId,CustomerName,Mandates,CustSeqNo,wrapperIP,wrapperPort,VolumeId,SiteId,appServerType,wsname) ;
out.println(msg);
%>
<%!
//**********************************************************************************//

//          NEWGEN SOFTWARE TECHNOLOGIES LIMITED

 

//Group                               :           Application Projects
//Project                             :           Rakbank  Account-Opening-Automation//Date Written           
//Date Modified                       :          
//Author                              :          Amandeep
//Description                		  :          

//***********************************************************************************//
public String processWI(String ItemIndex,String SessionId,String CabinetName,String WIName,String AccountNo,String CifId,String CustomerName,String Mandates,String CustSeqNo,String JtsIP,String JtsPort,String VolumeId,Integer SiteId, String appServerType, String wsname) 
{
	String WIProcessedStatus ="";
	
	try
	{	
		
		XMLParser xmlParser = new XMLParser();
		
		
		WriteLog("After SigUpload Status "+Mandates);		
		
		String sQuery = "select imageindex  from pdbdocument with (nolock) WHERE documentindex="+Mandates;
		String inputXML = "<?xml version='1.0'?><APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option><Query>" + sQuery + "</Query><EngineName>" + CabinetName + "</EngineName><SessionId>" + SessionId+ "</SessionId></APSelectWithColumnNames_Input>";
			String outputXML="";	
				try 
				{
					outputXML = NGEjbClient.getSharedInstance().makeCall(JtsIP, JtsPort+"", appServerType, inputXML);
				} 
				catch (NGException e) 
				{
				   e.printStackTrace();
				} 
		WriteLog("outputXML: "+outputXML);
		xmlParser = new XMLParser();
		xmlParser.setInputXML(outputXML);
		String mainCode = xmlParser.getValueOf("MainCode");
	
		if(mainCode.equals("0"))
		{
			String Iindex = xmlParser.getValueOf("imageindex");
			WriteLog("OKKKK: "+Iindex);

			downloadStatus = DownloadDocument(Iindex,WIName,"License",strSignatureStatusTable,AccountNo,CustomerName,CustSeqNo,CabinetName,SessionId,JtsIP,JtsPort,VolumeId,String.valueOf(SiteId));
					WriteLog("download status..: " +downloadStatus);
													
				if(downloadStatus!="F")
				 {
					WriteLog("downloadStatus is !=F "); 
					
					 //if(CustSeqNo==null)
						// map.put(customer_seq_no, downloadStatus);
					 
				 uploadDocStatus=uploadSinaturesinFinacle(downloadStatus,WIName, AccountNo,CifId, trDate, CustomerName, Mandates ,CabinetName,SessionId,JtsIP,JtsPort,wsname,appServerType);
				 WriteLog("uploadDocStatus is... " +uploadDocStatus); 
					 
					  
				 }
					
			}
		
	}
	catch (Exception e) 
	{
		WriteLog("Exception occured: " +e.getMessage());	
	}

	return uploadDocStatus;	
}
//**********************************************************************************//

//          NEWGEN SOFTWARE TECHNOLOGIES LIMITED

 

//Group                               :           Application Projects
//Project                             :           Rakbank  Account-Opening-Automation//Date Written           
//Date Modified                       :          
//Author                              :          Amandeep
//Description                		  :          

//***********************************************************************************//
public String GetDocumentsList(String itemindex , String sessionId,String cabinetName,String JtsIP,Integer JtsPort)
{
	WriteLog("Inside GetDocumentsList Method ...");	

	XMLParser xmlParser = new XMLParser();
	xmlParser = new XMLParser();
	String mainCode="";
	String response="F";
	String outputXML ="";
	try
	{
		String inputXML = "<?xml version=\"1.0\"?><NGOGetDocumentListExt_Input>" +
			"<Option>NGOGetDocumentListExt</Option>" +
			"<CabinetName>"+cabinetName+"</CabinetName>" +
			"<UserDBId>"+sessionId+"</UserDBId>" +
			"<CurrentDateTime></CurrentDateTime>" +
			"<FolderIndex>"+itemindex+"</FolderIndex>" +
			"<DocumentIndex></DocumentIndex>" +
			"<PreviousIndex>0</PreviousIndex>" +
			"<LastSortField></LastSortField>" +
			"<StartPos>0</StartPos>" +
			"<NoOfRecordsToFetch>10</NoOfRecordsToFetch>" +
			"<OrderBy>5</OrderBy><SortOrder>A</SortOrder><DataAlsoFlag>N</DataAlsoFlag>" +
			"<AnnotationFlag>Y</AnnotationFlag><LinkDocFlag>Y</LinkDocFlag>" +
			"<PreviousRefIndex>0</PreviousRefIndex><LastRefField></LastRefField>" +
			"<RefOrderBy>2</RefOrderBy><RefSortOrder>A</RefSortOrder>" +
			"<NoOfReferenceToFetch>10</NoOfReferenceToFetch>" +
			"<DocumentType>B</DocumentType>" +
			"<RecursiveFlag>N</RecursiveFlag><ThumbnailAlsoFlag>N</ThumbnailAlsoFlag>" +
			"</NGOGetDocumentListExt_Input>";
		
		WriteLog("Inside GetDocumentsList Method inputXML..."+ inputXML);				

		WriteLog("Inside GetDocumentsList Method JtsIP..."+ JtsIP);
		WriteLog("Inside GetDocumentsList Method JtsPort..."+ JtsPort);
		outputXML = WFCallBroker.execute(inputXML, JtsIP, JtsPort, 1);
		WriteLog("Inside GetDocumentsList Method outputXML..."+ outputXML);
		xmlParser.setInputXML(outputXML);
		mainCode = xmlParser.getValueOf("Status");	
		
		if(mainCode.equals("0"))
		{
			response=outputXML;
		}		
	}
	catch (Exception e) 
	{
		response ="F";
		WriteLog("EXCEPTION: "+e.getMessage());
	}
	return response;
}
//**********************************************************************************//

//          NEWGEN SOFTWARE TECHNOLOGIES LIMITED

 

//Group                               :           Application Projects
//Project                             :           Rakbank  Account-Opening-Automation//Date Written           
//Date Modified                       :          
//Author                              :          Amandeep
//Description                		  :          

//***********************************************************************************//
public String updateSigUploadStatus(String strSignatureStatusTable,String colName,String values,String where,String cabinetName,String sessionId,String JtsIP,String JtsPort)
{ 
	WriteLog("Inside updateSigUploadStatus Method ...");
	String sigUploadStatus="N";
	try
	{
		XMLParser xmlParser = new XMLParser();
		String inputXML=ExecuteQuery_APUpdate(strSignatureStatusTable,colName,values,where,cabinetName,sessionId);
		WriteLog("Inside updateSigUploadStatus Method inputXML..."+inputXML);
		String outPutXML = "";
		WriteLog("Inside updateSigUploadStatus Method outPutXML..."+outPutXML);
		
		xmlParser.setInputXML(outPutXML);		
		String mainCode = xmlParser.getValueOf("MainCode");
		
		if(mainCode.equals("0"))
		{
			sigUploadStatus="Y";
		}
		else
		{
			//
		}
	}
	catch (Exception e) 
	{
		WriteLog("Exception caught:"+e.getMessage());
	}
	return sigUploadStatus;
}
//**********************************************************************************//

//          NEWGEN SOFTWARE TECHNOLOGIES LIMITED

 

//Group                               :           Application Projects
//Project                             :           Rakbank  Account-Opening-Automation//Date Written           
//Date Modified                       :          
//Author                              :          Amandeep
//Description                		  :          

//***********************************************************************************//
public String ExecuteQuery_APUpdate(String tableName,String columnName,String strValues,String sWhere,String cabinetName,String sessionId)
{
	
	System.out.println("inside ExecuteQuery_APUpdate");
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
	System.out.println("wfInputXml.toString()-------"+wfInputXml.toString());
	return wfInputXml.toString();
}
//**********************************************************************************//

//          NEWGEN SOFTWARE TECHNOLOGIES LIMITED

 

//Group                               :           Application Projects
//Project                             :           Rakbank  Account-Opening-Automation//Date Written           
//Date Modified                       :          
//Author                              :          Amandeep
//Description                		  :          

//***********************************************************************************//
public String LockRecordBeforeProcessing(String winame, String account_no, String cif_id, String customer_name1, String customer_seq_no,String CabinetName,String SessionId,String JtsIP,Integer JtsPort)
{
	try
	{
		WriteLog("Inside LockRecordBeforeProcessing method...");
		XMLParser xmlParser = new XMLParser();
		String strSignatureStatusTable="AO_signature_status";
		String colName="SIGUPLOAD_STATUS";
		String values="'L'";
		String where = "WI_NAME='"+winame+"' and account_no='"+account_no+"' and "
				+ "cif_id='"+cif_id+"' and customer_name1='"+customer_name1+"' and customer_seq_no='"+customer_seq_no+"'";
			
		String inputXML=ExecuteQuery_APUpdate(strSignatureStatusTable,colName,values,where,CabinetName,SessionId);
		WriteLog("Inside LockRecordBeforeProcessing method inputXML..." +inputXML);
		String outPutXML = WFCallBroker.execute(inputXML, JtsIP, JtsPort, 1);
		WriteLog("Inside LockRecordBeforeProcessing method outPutXML..." +outPutXML);
		
		xmlParser.setInputXML(outPutXML);		
		String mainCode = xmlParser.getValueOf("MainCode");
		
		if(mainCode.equals("0"))
		{
			return "S";
		}	
		else
		{
			return "F";
		}
	}
	catch (Exception e) {
		WriteLog("Exception ::"+e.getMessage());		
	}
	return "F";	
}
//**********************************************************************************//

//          NEWGEN SOFTWARE TECHNOLOGIES LIMITED

 

//Group                               :           Application Projects
//Project                             :           Rakbank  Account-Opening-Automation//Date Written           
//Date Modified                       :          
//Author                              :          Amandeep
//Description                		  :          

//***********************************************************************************//
public String uploadSinaturesinFinacle(String downloadStatus,String winame,String account_no,String cif_id,String trDate,String customer_name1,String remarks,String cabinetName,String sessionId,String JtsIP,String JtsPort, String wsname, String appServerType )
{

	WriteLog("Inside uploadSinaturesinFinacle---");
	String update_Status="";
	String ServletUrl="http://dsisbfinuatwebs01.rakbank.co.ae:8081/FISERVLET/fihttp";
	String values="";
	String sigUploadStatus="";
	String outputXML="";
	String mainCodeData=null;
	String socketServerIP;
	String socketPort;	
	Integer socketServerPort;
	XMLParser xmlParserData=null;
	Socket socket = null;
	OutputStream outstream = null;
	InputStream socketInputStream = null;
	DataOutputStream dout = null;
	DataInputStream din = null;
	String mqInputRequest="";
	String sMappOutPutXML = "";
	String mqOutputResponse="";
	String userName = "Test";
	String inputXML = "";
	
	try
	{
		//XMLParser xmlParser = new XMLParser();
		
		String inputUploadXML = getSignatureUploadXML(downloadStatus, account_no,cif_id, trDate, customer_name1,remarks );
		
		//code added here
		String sMQuery = "SELECT SocketServerIP,SocketServerPort FROM NG_RLOS_MQ_TABLE  where host_name = 'mq'";
		WriteLog("sMQuery "+sMQuery);
		WriteLog("sessionId"+sessionId);
		inputXML = "<?xml version='1.0'?><APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option><Query>" + sMQuery + "</Query><EngineName>" + cabinetName + "</EngineName><SessionId>" + sessionId+ "</SessionId></APSelectWithColumnNames_Input>";
		
		try 
		{
			outputXML = NGEjbClient.getSharedInstance().makeCall(JtsIP, JtsPort, appServerType, inputXML);
		} 
		catch (NGException e) 
		{
		   e.printStackTrace();
		} 
		catch (Exception ex) 
		{
		   ex.printStackTrace();
		}
		
		WriteLog("outputXML exceptions mq --> "+outputXML);
		xmlParserData = new XMLParser();
		xmlParserData.setInputXML((outputXML));
		int recordcount2 = Integer.parseInt(xmlParserData.getValueOf("TotalRetrieved"));
		WriteLog("Number of records are in 3rd query are ="+recordcount2);
		mainCodeData = xmlParserData.getValueOf("MainCode");
		
		if(mainCodeData.equals("0"))
		{
			socketServerIP = xmlParserData.getNextValueOf("SocketServerIP");
			WriteLog("socketServerIP"+socketServerIP);
			socketServerPort = Integer.parseInt(xmlParserData.getNextValueOf("SocketServerPort"));
			WriteLog("socketServerPort"+socketServerPort.toString());
			
			if(!(socketServerIP.equalsIgnoreCase("") && socketServerIP.equalsIgnoreCase(null) && socketServerPort.equals(null) && socketServerPort.equals(0)))
			{
				socket = new Socket(socketServerIP, socketServerPort);
				WriteLog("socket"+socket);
				outstream = socket.getOutputStream();
				WriteLog("outstream"+outstream);
				socketInputStream = socket.getInputStream();
				WriteLog("socketInputStream:"+socketInputStream);
				dout = new DataOutputStream(outstream);
				WriteLog("dout"+dout);
				din = new DataInputStream(socketInputStream);
				WriteLog("din"+din);
				mqInputRequest= getMQInputXML(sessionId,cabinetName,winame,wsname,userName,inputUploadXML);
				WriteLog("mqInputRequest is:"+mqInputRequest);
				if (mqInputRequest != null && mqInputRequest.length() > 0) 
				{
					int outPut_len = mqInputRequest.getBytes("UTF-16LE").length;
                    mqInputRequest = outPut_len+"##8##;"+mqInputRequest;
					WriteLog("mqInputRequest is:"+mqInputRequest);
					WriteLog("MqInputRequestInput Request Bytes : "+mqInputRequest.getBytes("UTF-16LE"));
					dout.write(mqInputRequest.getBytes("UTF-16LE"));
					dout.flush();				
				}
								
				byte[] readBuffer = new byte[50000];
	            int num = din.read(readBuffer);
	            boolean wait_flag = true;
	            int out_len=0;
                WriteLog("num lenght: "+num);
	            if (num > 0) 
	            {
	            	while(wait_flag){
	            		byte[] arrayBytes = new byte[num];
	            		System.arraycopy(readBuffer, 0, arrayBytes, 0, num);
	            		mqOutputResponse = mqOutputResponse + new String(arrayBytes, "UTF-16LE");
	            		if(mqOutputResponse.contains("##8##;")){
	            		String[]	mqOutputResponse_arr = mqOutputResponse.split("##8##;");
	            		mqOutputResponse = mqOutputResponse_arr[1];
						WriteLog("mqOutputResponse: "+ mqOutputResponse);
						WriteLog("OutPut Len: "+ mqOutputResponse_arr[0]);
	            		out_len = Integer.parseInt(mqOutputResponse_arr[0]);
						WriteLog("OutPut Len: "+ out_len);
	            		}
	            		if(out_len <= mqOutputResponse.getBytes("UTF-16LE").length){
	            			wait_flag=false;
	            		}
	            		Thread.sleep(200);
	            		 num = din.read(readBuffer);
	            		 
	            	}
	            	
                   		WriteLog("MqOutputRequestOutput Response :\n"+mqOutputResponse);
		        		sMappOutPutXML= mqOutputResponse;	
		        		
		        		
		       }
			}
			else
			{
				WriteLog("SocketServerIp and SocketServerPort is not maintained");
				WriteLog("SocketServerIp is not maintained "+socketServerIP);
				WriteLog(" SocketServerPort is not maintained "+socketServerPort.toString());
				sMappOutPutXML= "MQ details not maintained";
			}
		}
		else{
	        	WriteLog("Genrate XML:,, ");
	        	sMappOutPutXML="Call not maintained";
	        }
		
		WriteLog("uploadSinaturesinFinacle inputXML---" +sMappOutPutXML);
			
		xmlParserData.setInputXML(sMappOutPutXML);
		String statusCode =  xmlParserData.getValueOf("ReturnCode");
		
		inputXML=inputXML.replaceAll("'","''");
		sMappOutPutXML=sMappOutPutXML.replaceAll("'","''");

		
		if(statusCode.equalsIgnoreCase("0000"))
		{
			update_Status = "Signature uploaded Successfully !";
		}	
		else
		{
			update_Status = "Signature uploaded failed, Please try after sometime.";
		}
	}
	catch (Exception e) {
				
		WriteLog("Exception  ::"+e.getMessage());
	}
	
	return update_Status;
}
//**********************************************************************************//

//          NEWGEN SOFTWARE TECHNOLOGIES LIMITED

 

//Group                               :           Application Projects
//Project                             :           Rakbank  Account-Opening-Automation//Date Written           
//Date Modified                       :          
//Author                              :          Amandeep
//Description                		  :          

//***********************************************************************************//
public static String getSignatureUploadXML(String base64String,String ACCNO,String CIFID,String DATE,String CustomerNameWithSingleQ,String RemarksWithSingleQ)
{	
	double n = Math.random();
	long Lrandom = Math.round(Math.random()*1000000000);
	String RequestUID="MB"+Lrandom+"";
	
	
	String integrationXML = "" +
				"<EE_EAI_MESSAGE>" +
				"<EE_EAI_HEADER>" +
				  "<MsgFormat>SIGNATURE_MAINTENANCE_REQ</MsgFormat>" +
				  "<MsgVersion>0001</MsgVersion>" +
				  "<RequestorChannelId>CAS</RequestorChannelId>" +
				  "<RequestorUserId>RAKUSER</RequestorUserId>" +
				  "<RequestorLanguage>E</RequestorLanguage>" +
				  "<RequestorSecurityInfo>secure</RequestorSecurityInfo>" +
				  "<ReturnCode>911</ReturnCode>" +
				  "<ReturnDesc>Issuer Timed Out</ReturnDesc>" +
				  "<MessageId>UniqueMessageId123</MessageId>" +
				  "<Extra1>REQ||SHELL.JOHN</Extra1>" +
				  "<Extra2>YYYY-MM-DDThh:mm:ss.mmm+hh:mm</Extra2>" +
			   "</EE_EAI_HEADER>" +
			   "<SignatureMaintenanceReq>" +
				  "<BankId>RAK</BankId>" +
				  "<AcctId>"+ACCNO+"</AcctId>" +
				  "<AccType>N</AccType>" +
				  "<CustId>"+CIFID+"</CustId>" +
				  "<TypeOfUpdate></TypeOfUpdate>"+
				  "<ImageAccessCode>1</ImageAccessCode>" +
				  "<SignExpDate>2112-03-06T23:59:59.000</SignExpDate>" +
				  "<SignEffDate>"+DATE+"</SignEffDate>" +
				  "<SignFile>"+base64String+"</SignFile>" +
				  "<PictureExpDate>2099-12-31T23:59:59.000</PictureExpDate>" +
				  "<PictureEffDate>2010-12-31T23:59:59.000</PictureEffDate>" +
				 "<PictureFile></PictureFile>" +
				  "<SignGroupId>A1010</SignGroupId>" +
				  "<Remarks>POA FLAG – NO – TO SIGN SINGLY AS PER AOF HELD</Remarks>" +
			   "</SignatureMaintenanceReq>" +
			"</EE_EAI_MESSAGE>";
			
	
			
		return integrationXML;
}
//**********************************************************************************//

//          NEWGEN SOFTWARE TECHNOLOGIES LIMITED

 

//Group                               :           Application Projects
//Project                             :           Rakbank  Account-Opening-Automation//Date Written           
//Date Modified                       :          
//Author                              :          Amandeep
//Description                		  :          

//***********************************************************************************//
public String DownloadDocument(String xmlParser,String winame,String docName,String strSignatureStatusTable,String account_no,String customer_name1,String customer_seq_no,String cabinetName,String sessionId,String JtsIP,String JtsPort, String VolumeId,String SiteId )
{
	JtsIP="10.15.12.136";
	JtsPort="3333";
	WriteLog("inside DownloadDocument");
	String status="F";
	String msg="Error";
	String JtsP= JtsPort.toString();
	StringBuffer strFilePath = new StringBuffer();
	try
	{
		String base64String = null;
		String imageIndex =xmlParser;
		
		strFilePath.append(System.getProperty("user.dir"));
		strFilePath.append(File.separator);
		strFilePath.append(fileDownloadLoc);
		strFilePath.append(File.separatorChar);
		strFilePath.append(winame);
		strFilePath.append("_");
		strFilePath.append(docName);
		strFilePath.append(".");
		strFilePath.append("JPG");

		WriteLog("inside Download document"+cabinetName+VolumeId+imageIndex+strFilePath);
		CImageServer cImageServer=null;
		try 
		{
			cImageServer = new CImageServer(null, JtsIP, Short.parseShort(JtsP));			
		}
		catch (JPISException e) 
		{
			WriteLog("Error Downloading signature:"+e.getMessage());
			msg = e.getMessage();
			status="F";
		}
		int odDownloadCode=cImageServer.JPISGetDocInFile_MT(null,JtsIP, Short.parseShort(JtsP), cabinetName,Short.parseShort(SiteId), Short.parseShort(VolumeId), Integer.parseInt(imageIndex),"",strFilePath.toString(), new JPDBString());
		//int odDownloadCode=1;
		WriteLog(strFilePath.toString());

		
		WriteLog("ODsDownloadCode: "+String.valueOf(odDownloadCode));
		
		if(odDownloadCode==1)
		{
			try
			{
				base64String=convertToBase64(strFilePath.toString());
				status=base64String;
				File fForDeletion= new File(strFilePath.toString());
				fForDeletion.delete();	
			}
			catch(Exception e)
			{
				msg=e.getMessage();
				status="F";
			}
		}
		else
		{
			msg="Error occured while downloading the document :"+docName;
			status="F";
		}
	}
	catch (Exception e) 
	{
		WriteLog("Error Downloading file:"+e.getMessage());
		msg=e.getMessage();
		status="F";
	}
		
	if(status.equals("F"))
	{
		String colName="SIGUPLOAD_STATUS,PROCESSING_REMARKS";
		String values="'F','"+msg+"'";
		String where = "WI_NAME='"+winame+"' and account_no ='"+account_no+"' and  customer_name1='"+customer_name1+"' and customer_seq_no='"+customer_seq_no+"'";
		String sigUploadStatus = "";//updateSigUploadStatus(strSignatureStatusTable,colName,values,where,cabinetName,sessionId,JtsIP,JtsPort);
	}
	
	return status;	
}
//**********************************************************************************//

//          NEWGEN SOFTWARE TECHNOLOGIES LIMITED

 

//Group                               :           Application Projects
//Project                             :           Rakbank  Account-Opening-Automation//Date Written           
//Date Modified                       :          
//Author                              :          Amandeep
//Description                		  :          

//***********************************************************************************//
public static String convertToBase64(String filePath)
{
	String retValue="";
	
	try
	{
		WriteLog("inside convertToBase64 method");
		File file = new File(filePath);
		 
		FileInputStream fis = new FileInputStream(file);
		//create FileInputStream which obtains input bytes from a file in a file system. FileInputStream is meant for reading streams of raw bytes such as image data. For reading streams of characters, consider using FileReader.
 
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		long size=0;
		
		try
		{
			for (int readNum; (readNum = fis.read(buf)) != -1;)
			{
				//Writes to this byte array output stream
				bos.write(buf, 0, readNum); 
				// out.println("read " + readNum + " bytes,");
				size=size+readNum;
			}
			
			byte[] encodedBytes = Base64.encodeBase64(bos.toByteArray());  
			String sEncodedBytes=new String(encodedBytes);
			
			retValue=sEncodedBytes;	
			//WriteLog("Base64 string..:" +retValue);
		}
		catch (IOException ex)
		{  
		  WriteLog("Error converting to Base64:"+ex.getMessage());
		}
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	return retValue;
}

//**********************************************************************************//

//          NEWGEN SOFTWARE TECHNOLOGIES LIMITED

 

//Group                               :           Application Projects
//Project                             :           Rakbank  Account-Opening-Automation//Date Written           
//Date Modified                       :          
//Author                              :          Amandeep
//Description                		  :          


public String UpdateWIProcessedStatus(String winame,String cabinetName, String sessionId,String JtsIP,Integer JtsPort)
{
	String UpdateWIStatus="";
	try
	{
		XMLParser xmlParser = new XMLParser();
		String strSignatureStatusTable="AO_signature_status";
		String colName="IsWIProcessed";
		String values="'Y'";
		String where = "WI_NAME='"+winame+"'";
			
		String inputXML=ExecuteQuery_APUpdate(strSignatureStatusTable,colName,values,where,cabinetName,sessionId);
		WriteLog("inputXML for UpdateWIProcessedStatus" +inputXML);	
		String outPutXML =WFCallBroker.execute(inputXML,JtsIP, JtsPort, 1);
		WriteLog("outPutXML for UpdateWIProcessedStatus" +outPutXML);
		
		xmlParser.setInputXML(outPutXML);		
		String mainCode = xmlParser.getValueOf("MainCode");
		
		if(mainCode.equals("0"))
		{

			UpdateWIStatus="Success";
		}	
		else
		{

			UpdateWIStatus="Failure";
		}
	}
	catch (Exception e) {
		WriteLog("Error updatig status:"+e.getMessage());
	}
	return UpdateWIStatus;
}

public static String getMQInputXML(String sessionID, String cabinetName, String wi_name, String ws_name, String userName, String final_xml) 
    {		
		WriteLog("sessionId method getMQInputXML"+sessionID);
		WriteLog("cabinetName method getMQInputXML"+cabinetName);

		String sInputXML = "";
		
		sInputXML = "<APMQPUTGET_Input>"
		+"<SessionId>"+sessionID+"</SessionId>\n"+
		"<EngineName>"+cabinetName+"</EngineName>\n"+
		"<XMLHISTORY_TABLENAME>NG_PL_XMLLOG_HISTORY</XMLHISTORY_TABLENAME>\n"+
		"<WI_NAME>"+wi_name+"</WI_NAME>\n"+
		"<WS_NAME>"+ws_name+"</WS_NAME>\n"+
		"<USER_NAME>"+userName+"</USER_NAME>\n"+
		"<MQ_REQUEST_XML>"+final_xml+"</MQ_REQUEST_XML>\n"+
		"</APMQPUTGET_Input>";		
		
    	return sInputXML;
	}

%>
