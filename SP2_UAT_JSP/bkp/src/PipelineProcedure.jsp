<%@ page import="java.io.*,java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.lang.String.*"%>
<%@ page import="java.util.regex.Matcher"%>
<%@ page import="java.util.regex.Pattern" %>
<%@ page import="java.lang.Object"%>
<%@ page import="java.util.LinkedHashMap,java.util.*,java.io.*"%>
<%@ page import="java.util.Date"%>
<%@ page import="com.newgen.wfdesktop.xmlapi.*" %>
<%@ page import="com.newgen.wfdesktop.util.*" %>
<%@ page import="org.json.*" %>
<%@ page import="java.sql.*"%>
  
<%@ page import="XMLParser.XMLParser"%>   
<%@ page import="com.newgen.omni.wf.util.app.NGEjbClient"%>
<%@page import="com.newgen.omni.wf.util.excp.NGException"%>

<%@ include file="Log.process"%>
<jsp:useBean id="wDSession" class="com.newgen.wfdesktop.session.WDSession" scope="session"/>


<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader ("Expires", 0);
	String sCabname=wDSession.getM_objCabinetInfo().getM_strCabinetName();
	String sSessionId = wDSession.getM_objUserInfo().getM_strSessionId();
	String sJtsIp = wDSession.getM_objCabinetInfo().getM_strServerIP();
	String iJtsPort = wDSession.getM_objCabinetInfo().getM_strServerPort();
	String appServerType = wDSession.getM_objCabinetInfo().getM_strAppServerType();
	String wi_name = request.getParameter("wi_name");
	String ProcName = "ng_RLOS_CASPipelineCheck";
	
	WriteLog("Exec_procedure jsp: request params: "+wi_name );
	String sOutputXML="";
	String sFinalString="";
	XMLParser xmlParser = new XMLParser();
	

	StringBuffer sInputXML=new StringBuffer();
	sInputXML.append("<?xml version=\"1.0\"?>");
	sInputXML.append("<APProcedure_WithDBO_Input>");
	sInputXML.append("<option>APProcedure_WithDBO</option>");
	sInputXML.append("<ProcName>"+ProcName+"</ProcName>");	
	sInputXML.append("<Params>"+wi_name+"</Params>");	
	sInputXML.append("<EngineName>"+sCabname+"</EngineName>");
	sInputXML.append("<SessionId>"+sSessionId+"</SessionID>");
	sInputXML.append("</APProcedure_WithDBO_Input>");
	
	WriteLog("Exec_procedure jsp: sInputXML: "+sInputXML.toString());
	System.out.println("inputXML---"+sInputXML.toString());	
		
	
						
		try	{
		
		//sOutputXML=WFCallBroker.execute(sInputXML.toString(),sJtsIp,iJtsPort, appServerType);
		sOutputXML = NGEjbClient.getSharedInstance().makeCall(sJtsIp, iJtsPort, appServerType, sInputXML.toString());
		
		 WriteLog("Exec_procedure jsp: sInputXML: "+sOutputXML);
		WriteLog("sOutputXML---"+sOutputXML);
	} catch(Exception exp) {
		WriteLog("Exception while getting data from database : "+exp);
		}
	%>