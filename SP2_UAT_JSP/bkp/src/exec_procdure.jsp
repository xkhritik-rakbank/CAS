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
<%@ include file="Log.process"%>
<jsp:useBean id="wDSession" class="com.newgen.wfdesktop.session.WDSession" scope="session"/>


<%  String sCabname=wDSession.getM_objCabinetInfo().getM_strCabinetName();
	String sSessionId = wDSession.getM_objUserInfo().getM_strSessionId();
	String sJtsIp = wDSession.getM_objCabinetInfo().getM_strServerIP();
	String iJtsPort = wDSession.getM_objCabinetInfo().getM_strServerPort();
	String wi_name = request.getParameter("wi_name");
	String table_name = request.getParameter("table_name");
	String col_name_list= request.getParameter("col_name_list");
	String col_val = request.getParameter("col_val");
	String ProcName = request.getParameter("ProcName");
	String appServerType = "";
	WriteLog("Exec_procedure jsp: request params: "+wi_name + " : "+table_name +" : "+col_name_list+" : "+col_val);
	String sOutputXML="";
	String sFinalString="";
	XMLParser xmlParser = new XMLParser();
	

	StringBuffer sInputXML=new StringBuffer();
	sInputXML.append("<?xml version=\"1.0\"?>");
	sInputXML.append("<APProcedure_WithDBO_Input>");
	sInputXML.append("<option>APProcedure_WithDBO</option>");
	sInputXML.append("<ProcName>"+ProcName+"</ProcName>");	
	sInputXML.append("<Params>"+"'"+wi_name+"','"+table_name+"','"+col_name_list+"','"+col_val+"'"+"</Params>");	
	sInputXML.append("<EngineName>"+sCabname+"</EngineName>");
	sInputXML.append("<SessionId>"+sSessionId+"</SessionID>");
	sInputXML.append("</APProcedure_WithDBO_Input>");
	
	WriteLog("Exec_procedure jsp: sInputXML: "+sInputXML.toString());
	System.out.println("inputXML---"+sInputXML.toString());	
		
	
						
		try	{
		
		// sOutputXML=WFCallBroker.execute(sInputXML.toString(),sJtsIp,iJtsPort, appServerType);
		sOutputXML = NGEjbClient.getSharedInstance().makeCall(sJtsIp, iJtsPort, appServerType, sInputXML.toString());
		 WriteLog("Exec_procedure jsp: sInputXML: "+sOutputXML);
		System.out.println("sOutputXML---"+sOutputXML);
	} catch(Exception exp) {
		System.out.println("Exception while getting data from database : "+exp);
		}
	%>
alert('Final String is'+<%=	sOutputXML %>);
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

