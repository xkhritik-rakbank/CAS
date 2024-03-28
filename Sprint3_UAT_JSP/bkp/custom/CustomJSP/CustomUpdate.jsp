<%@ include file="Log.process"%>
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
<%@ page import="flexjson.JSONSerializer.*" %>
<%@ page import="flexjson.*" %>
<%@ page import="java.sql.*"%>
<%@ page import="com.newgen.omni.jts.srvr.*"%>   
<%@ page import="com.newgen.omni.jts.cmgr.XMLParser"%>
<%@ page import="com.newgen.omni.wf.util.app.NGEjbClient"%>
<%@page import="com.newgen.omni.wf.util.excp.NGException"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.owasp.esapi.ESAPI"%>
<%@ page import="org.owasp.esapi.codecs.OracleCodec"%>
<%@ page import="org.owasp.esapi.User" %>
<%@ page import="com.newgen.*" %>
<jsp:useBean id="wDSession" class="com.newgen.wfdesktop.session.WDSession" scope="session"/>


<%  String sCabname=wDSession.getM_objCabinetInfo().getM_strCabinetName();
	String sSessionId = wDSession.getM_objUserInfo().getM_strSessionId();
	String sJtsIp = wDSession.getM_objCabinetInfo().getM_strServerIP();
	String iJtsPort = wDSession.getM_objCabinetInfo().getM_strServerPort();
	String appServerType = wDSession.getM_objCabinetInfo().getM_strAppServerType();
	WriteLog("Inside CustomUpdate.jsp");	
	String sOutputXML="";
	String sFinalString="";
	XMLParser xmlParser = new XMLParser();
	
	String input1 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("table"), 1000, true) );
	String table = ESAPI.encoder().encodeForSQL(new OracleCodec(), input1);
		
	String columnsNames =  ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("columnsNames"), 1000, true) ;
	
	
	//String columnsValues = ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("columnsValues"), 1000, true) ;
	
	String columnsValues =  request.getParameter("columnsValues") ;
		
	String swhere =  ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("swhere"), 1000, true) ;
		
	String input5 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("WD_UID"), 1000, true) );
	String WD_UID = ESAPI.encoder().encodeForSQL(new OracleCodec(), input5);
	
	StringBuffer sInputXML=new StringBuffer();
	sInputXML.append("<?xml version=\"1.0\"?>");
	sInputXML.append("<APUpdate_Input>");
	sInputXML.append("<option>APUpdate</option>");
	sInputXML.append("<TableName>" + table + "</TableName>");
	sInputXML.append("<ColName>" + columnsNames + "</ColName>");
	sInputXML.append("<Values>" + columnsValues + "</Values>");
	sInputXML.append("<WhereClause>" + swhere + "</WhereClause>");
	sInputXML.append("<EngineName>"+sCabname+"</EngineName>");
	sInputXML.append("<SessionID>"+sSessionId+"</SessionID>");
	sInputXML.append("</APUpdate_Input>");
	WriteLog("inputXML---"+sInputXML.toString());	
	
			try 
			{
				sOutputXML = NGEjbClient.getSharedInstance().makeCall(sJtsIp, iJtsPort, appServerType, sInputXML.toString());
				WriteLog("CustomUpdate >> OutPutXML---"+sOutputXML);
			} 
			catch (NGException e) 
			{
				WriteLog("Exception while getting data from database : ");
			  // e.printStackTrace();
			} 
			catch (Exception ex) 
			{
				WriteLog("Exception while getting data from database : ");
			 // ex.printStackTrace();
			}
	%>
<%
	try {
		xmlParser.setInputXML(sOutputXML);
		String mainCode = xmlParser.getValueOf("MainCode");
            if(mainCode.equals("0")) {
				sFinalString="SUCCESS";
            } else {
               sFinalString="ERROR";
            }
        } catch (Exception exp) {
		    out.println(exp);
            sFinalString+=exp;
        }
		out.clear();
	out.println(sFinalString);	
%>

