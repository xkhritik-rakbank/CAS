<!------------------------------------------------------------------------------------------------------
//           NEWGEN SOFTWARE TECHNOLOGIES LIMITED

//Group						 : Application ?Projects
//Product / Project			 : RAKBank
//File Name					 : OpenImage.jsp
//Author                     : Mandeep
//Date written (DD/MM/YYYY)  : 29-01-2016
Description                  : File used to signature window on clicking on the view signaturew button  
//---------------------------------------------------------------------------------------------------->
<%@ include file="Log.process"%>
<jsp:useBean id="wDSession" class="com.newgen.wfdesktop.session.WDSession" scope="session"/>
<jsp:useBean id="WDCabinet" class="com.newgen.wfdesktop.baseclasses.WDCabinetInfo" scope="session"/>
<%@ page import="java.io.UnsupportedEncodingException" %>
<%@ page import="com.newgen.mvcbeans.model.wfobjects.WFDynamicConstant, com.newgen.mvcbeans.model.*,com.newgen.mvcbeans.controller.workdesk.*, javax.faces.context.FacesContext"%>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@page import="com.newgen.omni.wf.util.excp.NGException"%>
<%@page import="com.newgen.omni.wf.util.app.NGEjbClient"%>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page  import= "com.newgen.custom.*, org.w3c.dom.*, org.w3c.dom.NodeList,org.w3c.dom.Document,javax.xml.parsers.DocumentBuilder, javax.xml.parsers.DocumentBuilderFactory"%>
<%@ page import ="java.text.DecimalFormat,org.xml.sax.InputSource"%>

<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.io.*,java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.net.*"%>
<%@ page import="com.newgen.wfdesktop.xmlapi.*" %>
<%@ page import="com.newgen.wfdesktop.util.*" %>
<%@ page import="com.newgen.custom.*" %>
<%@ page import="java.io.*,java.util.*,java.math.*"%>
<%@ page import="com.newgen.wfdesktop.exception.*" %>
<%@ page import ="java.text.DecimalFormat"%>
<%@ page import ="javax.xml.parsers.DocumentBuilder"%>
<%@ page import ="javax.xml.parsers.DocumentBuilderFactory"%>
<%@ page import ="org.w3c.dom.*"%>
<%@ page import ="org.xml.sax.InputSource"%>
<%@ page import="com.newgen.mvcbeans.model.wfobjects.WFDynamicConstant ,com.newgen.mvcbeans.model.*,com.newgen.mvcbeans.controller.workdesk.*,javax.faces.context.FacesContext,com.newgen.mvcbeans.controller.workdesk.*,java.util.*"%>
<%@ page import="com.newgen.wfdesktop.xmlapi.WFCallBroker" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
	
	String appServerType = wDSession.getM_objCabinetInfo().getM_strAppServerType();
	String sJtsIp = wDSession.getM_objCabinetInfo().getM_strServerIP();
	String iJtsPort = wDSession.getM_objCabinetInfo().getM_strServerPort()+"";    
	String sSessionId = wDSession.getM_objUserInfo().getM_strSessionId();
	String sCabName = wDSession.getM_objCabinetInfo().getM_strCabinetName();
	
	String wsname =request.getParameter("workstepName");
	String debt_acc_num =request.getParameter("debt_acc_num");
	String wi_name = request.getParameter("wi_name");
	String cif_id = request.getParameter("CifId");
	String userName = "Test";
	String sInputXML = "";
	String sMappOutPutXML = "";
	int returnedSignatures = 0;
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
	String mqOutputResponse="";
	String outputResponse="";
	String outputXML="";
	String inputXML="";
	String mqInputRequest="";
	String signMatchNeededAtChecker = "";
	// String filePath ="."+File.separatorChar+"CustomImage";
	String filePath =System.getProperty("user.dir")+File.separatorChar+"installedApps"+File.separatorChar+"ant2casapps01Node01Cell"+File.separatorChar+"webdesktop_war_45.ear"+File.separatorChar+"webdesktop.war"+File.separatorChar+"CustomForms"+File.separatorChar+"RLOS";
	
	try
	{
			sInputXML = "<EE_EAI_MESSAGE>"+
			"<EE_EAI_HEADER>\n"+
			"<MsgFormat>SIGNATURE_DETAILS</MsgFormat>\n"+
			"<MsgVersion>0001</MsgVersion>\n"+
			"<RequestorChannelId>CAS</RequestorChannelId>\n"+
			"<RequestorUserId>RAKUSER</RequestorUserId>\n"+
			"<RequestorLanguage>E</RequestorLanguage>\n"+
			"<RequestorSecurityInfo>secure</RequestorSecurityInfo>\n"+
			"<ReturnCode>0000</ReturnCode>\n"+
			"<ReturnDesc>REQ</ReturnDesc>\n"+
			"<MessageId>UTCFUNDBLOCK002</MessageId>\n"+
			"<Extra1>REQ||CAS.123</Extra1>\n"+
			"<Extra2>2014-03-25T11:05:30.000+04:00</Extra2>\n"+
			"</EE_EAI_HEADER>\n"+
			"<SignatureDetailsReq><BankId>RAK</BankId><CustId>"+cif_id+"</CustId><AcctId>"+debt_acc_num+"</AcctId></SignatureDetailsReq>\n" +
			"</EE_EAI_MESSAGE>";
		WriteLog("sInputXML from OpenImage.jsp- "+sInputXML);	
		
		//code added here
		String sMQuery = "SELECT SocketServerIP,SocketServerPort FROM NG_RLOS_MQ_TABLE";
		WriteLog("sMQuery "+sMQuery);
		WriteLog("sSessionId"+sSessionId);
		inputXML = "<?xml version='1.0'?><APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option><Query>" + sMQuery + "</Query><EngineName>" + sCabName + "</EngineName><SessionId>" + sSessionId+ "</SessionId></APSelectWithColumnNames_Input>";
		

		try 
		{
			outputXML = NGEjbClient.getSharedInstance().makeCall(sJtsIp, iJtsPort, appServerType, inputXML);
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
				mqInputRequest= getMQInputXML(sSessionId,sCabName,wi_name,wsname,userName,sInputXML);
				WriteLog("mqInputRequest is:"+mqInputRequest);
				if (mqInputRequest != null && mqInputRequest.length() > 0){ 
	         		int outPut_len = mqInputRequest.getBytes("UTF-16LE").length;
					mqInputRequest = outPut_len+"##8##;"+mqInputRequest;
					dout.write(mqInputRequest.getBytes("UTF-16LE"));
					dout.flush();
				}
				socket.setSoTimeout(10*1000);
							
				byte[] readBuffer = new byte[50000];
	                int num = din.read(readBuffer);
	               // boolean wait_flag = true;
	                int out_len=0;
                    WriteLog("num lenght: "+num);
	               	if (num > 0) {

						byte[] arrayBytes = new byte[num];
						System.arraycopy(readBuffer, 0, arrayBytes, 0, num);
						mqOutputResponse = mqOutputResponse+ new String(arrayBytes, "UTF-16LE");
						WriteLog("mqOutputResponse/message ID :  "+mqOutputResponse);
						if(!"".equalsIgnoreCase(mqOutputResponse)){
							mqOutputResponse = getOutWtthMessageID(mqOutputResponse,sSessionId,wi_name,sJtsIp,iJtsPort,appServerType,sCabName);
						}
						
					}
	               	WriteLog("MqOutputRequestOutput Response :\n"+mqOutputResponse);
					socket.close();
					sMappOutPutXML= mqOutputResponse;
				
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
		
		
		//WriteLog("sMappOutPutXML - "+sMappOutPutXML);
		strCode="";
		if(sMappOutPutXML!=null && sMappOutPutXML.indexOf("<ReturnCode>")>-1)
		{
			WriteLog("INSIDE PARSE SIGNATURE RESPONSE :\n");
			
			if(sMappOutPutXML.indexOf("<MQ_RESPONSE_XML>")>-1)
			{
				sMappOutPutXML=sMappOutPutXML.substring(sMappOutPutXML.indexOf("<MQ_RESPONSE_XML>")+17,sMappOutPutXML.indexOf("</MQ_RESPONSE_XML>"));
				WriteLog("$$outputXMLMsg "+sMappOutPutXML);
			
			}
			DocumentBuilderFactory dbf =   DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(sMappOutPutXML));

			Document doc = db.parse(is);
			NodeList returnCode = doc.getElementsByTagName("ReturnCode");
			Element elementReturnCode = (Element) returnCode.item(0);

			 strCode = getCharacterDataFromElement(elementReturnCode);
			
			if (strCode.equals("0000"))
			{
				NodeList nodesReturnedSignature = doc.getElementsByTagName("returnedSignature");
				NodeList nodesRemarksArr = doc.getElementsByTagName("remarks");
				NodeList nodesSignGrpNameArr = doc.getElementsByTagName("SignGrpName");
				NodeList nodesCustomerNameArr = doc.getElementsByTagName("CustomerName");
				
				//First Check if call is success or not
				
				//Initializing the Image Array
				returnedSignatures = nodesReturnedSignature.getLength();
				imageArr = new String [returnedSignatures];
				remarksArr = new String [returnedSignatures];
				signGrpNameArr = new String [returnedSignatures];
				customerNameArr = new String [returnedSignatures];
				
				WriteLog("returnedSignatures - "+returnedSignatures);
				
				// iterate the returnedSignatures and put it in an array
				for (int i = 0; i < returnedSignatures; i++) {
					Element element = (Element) nodesReturnedSignature.item(i);
					Element element1 = (Element) nodesRemarksArr.item(i);
					Element element2 = (Element) nodesSignGrpNameArr.item(i);
					Element element3 = (Element) nodesCustomerNameArr.item(i);
					try{
						imageArr [i] = getCharacterDataFromElement(element);
						remarksArr [i] = getCharacterDataFromElement(element1);
						signGrpNameArr [i] = getCharacterDataFromElement(element2);	
						customerNameArr [i] = getCharacterDataFromElement(element3);	
					}catch(Exception ex){
					
					}
				}
			}
		}
	}
	catch (UnknownHostException e) 
		{		
			e.printStackTrace();
			outputResponse= "1";
		}
		catch(Exception e){
			WriteLog("Exception ocurred: "+e.getMessage());
			System.out.println("$$sInputXML final_xml: "+sInputXML);
			System.out.println("Exception occured in main thread: "+ e.getMessage());
			
			outputResponse = "";
			
		}
%>


<%!
	String imageArr[] = null;
	String strCode =null;
	String remarksArr[] = null;
	String signGrpNameArr[] = null;
	String customerNameArr[] = null;
	
	public String getImages(String tempImagePath,String debt_acc_num)
	{
		
		WriteLog("tempImagePath"+tempImagePath);
		StringBuilder html = new StringBuilder();
		if(imageArr==null)
			return "";
		for (int i=0;i<imageArr.length;i++)
		{
			try
			{
				byte[] btDataFile = new sun.misc.BASE64Decoder().decodeBuffer(imageArr[i]);
				
				WriteLog("dbg 1");
				
				File of = new File(tempImagePath+File.separatorChar+debt_acc_num+"imageCreatedN"+i+".jpg");
				
				WriteLog("dbg 2 "+of.getAbsolutePath());
				
				FileOutputStream osf = new FileOutputStream(of);
				osf.write(btDataFile);
				osf.flush();
				osf.close();
				
				//Changed By Ashish
				String sImagePath = tempImagePath+File.separatorChar+debt_acc_num+"imageCreatedN"+i+".jpg";
				WriteLog("sImagePath :"+sImagePath);
				//End Changes
				
				html.append("<tr>");
				html.append("<td border='2' id='imageCreated"+i+"' width='40' nowrap='nowrap' class='EWNormalGreenGeneral1'><img src='"+sImagePath+"'/></td>");
				html.append("</tr>");
				html.append("<tr>");
				html.append("<td class='EWNormalGreenGeneral1'>Signature Remarks : "+remarksArr[i]+"</td>");
				html.append("</tr>");
				html.append("<tr>");
				html.append("<td class='EWNormalGreenGeneral1'>Group Name : "+signGrpNameArr[i]+"</td>");
				html.append("</tr>");
				html.append("<tr>");
				html.append("<td class='EWNormalGreenGeneral1'>Customer Name : "+customerNameArr[i]+"</td>");
				html.append("</tr>");
				html.append("<tr>");
				html.append("<td class='EWNormalGreenGeneral1'><hr/></td>");
				html.append("</tr>");
			}
			catch (Exception e)
			{
				WriteLog(e.getMessage());
				WriteLog("Not able to get the image imageCreated"+i);
			}
		}
		WriteLog( html.toString());
		return html.toString();
	}
	
	private static String getMQInputXML(String sessionID, String cabinetName, String wi_name, String ws_name, String userName, String final_xml) 
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
	
	
	public static String getCharacterDataFromElement(Element e) {
		Node child = e.getFirstChild();
		if (child instanceof CharacterData) {
		   CharacterData cd = (CharacterData) child;
		   return cd.getData();
		}
		return "NO_DATA";
	}

	public String getOutWtthMessageID(String message_ID,String sSessionId, String wi_name, String sJtsIp, String iJtsPort, String appServerType, String sCabName){
		String outputxml="";
		try{
			
			String sMQuery = "select OUTPUT_XML from NG_PL_XMLLOG_HISTORY with (nolock) where MESSAGE_ID ='"+message_ID+"' and WI_NAME = '"+wi_name+"'";
			WriteLog("sMQuery "+sMQuery);
			WriteLog("sSessionId"+sSessionId);
			String inputXML = "<?xml version='1.0'?><APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option><Query>" + sMQuery + "</Query><EngineName>" + sCabName + "</EngineName><SessionId>" + sSessionId+ "</SessionId></APSelectWithColumnNames_Input>";
			String outputXML="";
			
			for(int i=0;i<10;i++){
				try 
				{
					outputXML = NGEjbClient.getSharedInstance().makeCall(sJtsIp, iJtsPort, appServerType, inputXML);
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
				XMLParser xmlParserData = new XMLParser();
				xmlParserData.setInputXML((outputXML));
				int recordcount2 = Integer.parseInt(xmlParserData.getValueOf("TotalRetrieved"));
				WriteLog("Number of records are in 3rd query are ="+recordcount2);
				String mainCodeData  = xmlParserData.getValueOf("MainCode");
				if(mainCodeData.equals("0") && recordcount2>0)
				{
					outputxml = xmlParserData.getNextValueOf("OUTPUT_XML");
					WriteLog("socketServerIP"+outputxml);
					break;
				}
				else{
					Thread.sleep(10000);
				}
			}
			
			if("".equalsIgnoreCase(outputxml)){
				outputxml="Error";
			}
			WriteLog("getOutWtthMessageID" + outputxml);				
		}
		catch(Exception e){
			WriteLog("Exception occurred in getOutWtthMessageID" + e.getMessage());
			WriteLog("Exception occurred in getOutWtthMessageID" + e.getStackTrace());
			outputxml="Error";
		}
		return outputxml;
	}
%>


<!DOCTYPE html>
<html>
<head>
<title>Signature Matching</title>
<link rel="stylesheet" href="\webdesktop\webtop\en_us\css\docstyle.css">
</head>
<body onUnload="deleteImageFromServer ('<%=filePath%>','<%=debt_acc_num%>','<%=returnedSignatures%>');">


<table id="imageTable" width="850" border="0" cellpadding="3">


<% 
out.println(strCode);
	if (strCode.equals("0000"))
		out.print(getImages(filePath,debt_acc_num));

%>

</table>
<input type="hidden" id="wsname_sign" value="<%=wsname%>">
<br/>

<%	
	if (!strCode.equals("0000")) {
		out.clear();
		out.print("<title>Signature Matching Error</title>");
		if(strCode.equalsIgnoreCase("SVS-101")){
			out.print("<div style='margin-top:200px;margin-left:100px;font-size:18'><label>No matched signature found</label></div>");
		}
		else{
			out.print("<div style='margin-top:200px;margin-left:100px;font-size:18'><label>Error in fetching Signature</label></div>");		
		}
	}
	else
	{
		if (wsname.equals("RemittanceHelpDesk_Checker"))
		{
			%>
			<input type="button" style="margin-left:100px" id="sign_matchedOk" value="Sign Matched" onclick="sendValues('Ok');" class="EWButtonRB" />
			<input type="button" id="sign_matchedCancel" value="Sign Not Matched" onclick="sendValues('Cancel');" class="EWButtonRB" />
			<%	
		} 
		else if (wsname.equals("Ops_Checker") && signMatchNeededAtChecker.equals("Y")) 
		{ 
			%>
			<input type="button" style="margin-left:100px" id="sign_matchedOk" value="Sign Matched" onclick="sendValues('Ok');" class="EWButtonRB" />
			<input type="button" id="sign_matchedCancel" value="Sign Not Matched" onclick="sendValues('Cancel');" class="EWButtonRB" />
			<%	
		}
	}
%>

<script>

//**********************************************************************************//

//          NEWGEN SOFTWARE TECHNOLOGIES LIMITED

 

//Group                               :  Application Projects
//Project                             :  Rakbank - Telegrahic Transfer           
//Date Modified                       :  15/01/2015     
//Author                              :  Mandeep
//Description                		  :  This function sets the sign_matched value to the parent window i.e. TT.jsp

//***********************************************************************************//	
	
function sendValues(status) {
	
	if (status=='Ok')
	{
		//sendObj = {name:"SignMatched",value:"Yes"};
		//window.returnValue = sendObj;
		var wsname=document.getElementById('wsname_sign').value;
		window.opener.setSignMatchValues(wsname,'Yes');
	}
	else if (status=='Cancel')
	{
		//sendObj = {name:"SignMatched",value:"No"};
		//window.returnValue = sendObj;		 
		var wsname=document.getElementById('wsname_sign').value;
		window.opener.setSignMatchValues(wsname,'No');
	}
	
	deleteImageFromServer ('<%=filePath%>','<%=debt_acc_num%>','<%=returnedSignatures%>');
	window.close();
}

//**********************************************************************************//

//          NEWGEN SOFTWARE TECHNOLOGIES LIMITED

//Group                       :           Application Projects
//Project                     :           RAKBank eForms Phase-I 
//Date Written                : 
//Date Modified               : 
//Author                      : 
//Description                 :          This function is used to delete signature from server

//***********************************************************************************//			

function deleteImageFromServer (filePath,debt_acc_num,returnedSignatures)
{
	//alert("Deleting files from server");
	var url = 'DeleteSignFromServer.jsp?debt_acc_num='+debt_acc_num+"&returnedSignatures="+returnedSignatures;
	var xhr;
	var ajaxResult;	
	
	if(window.XMLHttpRequest)
		 xhr=new XMLHttpRequest();
	else if(window.ActiveXObject)
		 xhr=new ActiveXObject("Microsoft.XMLHTTP");

	 xhr.open("GET",url,false); 
	 xhr.send(null);
	 
	if (xhr.status == 200);
	else
	{
		alert("Error while deleting signature files from server");
		return false;
	}
}

</script>


</body>
</html>