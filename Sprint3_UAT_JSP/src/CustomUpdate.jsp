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
<%@ page import="XMLParser.XMLParser"%>
<%@ page import="com.newgen.omni.wf.util.app.NGEjbClient"%>
<%@page import="com.newgen.omni.wf.util.excp.NGException"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.owasp.esapi.ESAPI"%>
<%@ page import="org.owasp.esapi.codecs.OracleCodec"%>
<%@ page import="org.owasp.esapi.User" %>

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
	//svt points start
	String input1 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("table"), 1000, true) );
	String table = ESAPI.encoder().encodeForSQL(new OracleCodec(), input1);
		
	String input2 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("columnsNames"), 1000, true) );
	String columnsNames = ESAPI.encoder().encodeForSQL(new OracleCodec(), input2);
	
	//String columnsNames =  ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("columnsNames"), 1000, true) ;
	
	String input3 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("columnsValues"), 1000, true) );
	String columnsValues = ESAPI.encoder().encodeForSQL(new OracleCodec(), input3);
	columnsValues = columnsValues.replaceAll("&#x27;", "'");
	
	
	
	//String columnsValues = ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("columnsValues"), 1000, true) ;
	
	String input4 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("swhere"), 1000, true) );
	String swhere = ESAPI.encoder().encodeForSQL(new OracleCodec(), input4);
	swhere =swhere.replaceAll("&#x27;", "'");
	swhere =swhere.replaceAll("&#x3d;", "=");
	if (swhere.toLowerCase().contains("wait") || swhere.toLowerCase().contains("dalay")|| swhere.toLowerCase().contains("select")|| swhere.toLowerCase().contains("delete")|| swhere.toLowerCase().contains("insert"))
	{
		swhere="1=2";
	}
	//String swhere =  ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("swhere"), 1000, true) ;
	//svt points end

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

