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
<%@ page import="com.newgen.omni.wf.util.excp.NGException"%>
<%@ include file="Log.process"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.owasp.esapi.ESAPI"%>
<%@ page import="org.owasp.esapi.codecs.OracleCodec"%>

<%@ page import="org.owasp.esapi.User" %>

<jsp:useBean id="wDSession" class="com.newgen.wfdesktop.session.WDSession" scope="session"/>

<%  
String sCabname="";
String sSessionId = wDSession.getM_objUserInfo().getM_strSessionId();
String sJtsIp = "";
String iJtsPort = "";
String appServerType = "";
//svt point start
String input1 = ESAPI.encoder().encodeForHTML(  ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("wi_name"), 1000, true) );
String wi_name = ESAPI.encoder().encodeForSQL(new OracleCodec(), input1);
String input5 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("ProcName"), 1000, true) );
String ProcName = ESAPI.encoder().encodeForSQL(new OracleCodec(), input5);

WriteLog("Exec_datacopy_procedure jsp: request params ProcName: "+ProcName);
WriteLog("Exec_datacopy_procedure jsp: request params winame: "+wi_name);
//svt point end
String strOutputXML = null;
String totalRecord=null;
int startIndex;
int endIndex;
String itemIndex=null;


try {
		String prop_file_loc=System.getProperty("user.dir") + File.separatorChar+"CustomConfig"+File.separatorChar+"ServerConfig.properties";
	    File file = new File(prop_file_loc);
        FileInputStream fileInput = new FileInputStream(file);
        Properties properties = new Properties();
        properties.load(fileInput);
        fileInput.close();
		sCabname = properties.getProperty("cabinetName");
		sJtsIp = properties.getProperty("wrapperIP");
		iJtsPort = properties.getProperty("wrapperPort");
		appServerType = properties.getProperty("appServerType");
    } 
	catch(Exception e){
		
		WriteLog("Exception Occured while reading from properties file: "+e.getMessage());
	}


	XMLParser xmlParser = new XMLParser();
	String sOutputXML="";
	String sFinalString="";
	StringBuffer sInputXML=new StringBuffer();
	sInputXML.append("<?xml version=\"1.0\"?>");
	sInputXML.append("<APProcedure_WithDBO_Input>");
	sInputXML.append("<option>APProcedure_WithDBO</option>");
	sInputXML.append("<ProcName>"+ProcName+"</ProcName>");	
	sInputXML.append("<Params>'"+wi_name+"',''</Params>");//svt points
	sInputXML.append("<EngineName>"+sCabname+"</EngineName>");
	sInputXML.append("<SessionId>"+sSessionId+"</SessionID>");
	sInputXML.append("</APProcedure_WithDBO_Input>");
	
	WriteLog("Exec_procedure jsp: sInputXML: "+sInputXML.toString());
	WriteLog("inputXML---"+sInputXML.toString());	
		
	
						
		try	{
		
		// sOutputXML=WFCallBroker.execute(sInputXML.toString(),sJtsIp,iJtsPort, appServerType);
		sOutputXML = NGEjbClient.getSharedInstance().makeCall(sJtsIp, iJtsPort, appServerType, sInputXML.toString());
		 WriteLog("Exec_procedure jsp: sInputXML: "+sOutputXML);
		
	} catch(Exception exp) {
		WriteLog("Exception while getting data from database : "+exp);
		}
	%>

<%
	try {
		
		xmlParser.setInputXML(sOutputXML);
		String mainCode = xmlParser.getValueOf("MainCode");
            if(mainCode.equals("0")) {
				sFinalString="success";
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

