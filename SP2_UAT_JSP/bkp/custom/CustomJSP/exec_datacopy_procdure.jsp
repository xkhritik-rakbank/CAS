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
<%@ page import="com.newgen.*" %>
<jsp:useBean id="wDSession" class="com.newgen.wfdesktop.session.WDSession" scope="session"/>


<%  String sCabname="";
	String sSessionId = wDSession.getM_objUserInfo().getM_strSessionId();
	String sJtsIp = "";
	String iJtsPort = "";
	String appServerType = "";
	

	String ProcName ="NG_RLOS_COPY_DATA_PROC";//request.getParameter("ProcName");

	String input = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("wi_name"), 1000, true) );
	String winame = ESAPI.encoder().encodeForSQL(new OracleCodec(), input);
	
	String input1 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("old_app_no"), 1000, true) );
	String old_app_no = ESAPI.encoder().encodeForSQL(new OracleCodec(), input1);
	
	String input2 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("WD_UID"), 1000, true) );	 
	String WD_UID = ESAPI.encoder().encodeForSQL(new OracleCodec(), input2);
	
	WriteLog("Exec_datacopy_procedure jsp: request params old_app_no: "+old_app_no);
	WriteLog("Exec_datacopy_procedure jsp: request params ProcName: "+ProcName);
	WriteLog("Exec_datacopy_procedure jsp: request params winame: "+winame);
	
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
	
	String sQuery="Select winame from ng_rlos_exttable where itemindex=:itemindex";
	String params="itemindex=="+old_app_no;
  
  String strInputXML = "<?xml version='1.0'?><APSelectWithNamedParam_Input>"
			+ "<option>APSelectWithNamedParam</option>" + "<Query>" + sQuery + "</Query>" + "<Params>" + params
			+ "</Params>" + "<EngineName>" + sCabname + "</EngineName>" + "<SessionID>" + sSessionId
			+ "</SessionID>" + "</APSelectWithNamedParam_Input>";

	WriteLog("Exec_datacopy_procedure jsp: 1 strInputXML: "+strInputXML);
	try 
	{
		strOutputXML = NGEjbClient.getSharedInstance().makeCall(sJtsIp, iJtsPort, appServerType, strInputXML);
		WriteLog("Exec_datacopy_procedure jsp: 1 strOutputXML: "+strOutputXML);
	} 
	catch (NGException e) 
	{
	   e.printStackTrace();
	} 
	catch (Exception ex) 
	{
	   ex.printStackTrace();
	}
	
	if(strOutputXML.equals(""))
	{
		System.out.println("Network Error !! Could not connect to server");
	}
	else
	{
		startIndex=strOutputXML.indexOf("<TotalRetrieved>");			
		endIndex=strOutputXML.indexOf("</TotalRetrieved>");	
		totalRecord=strOutputXML.substring(startIndex+16,endIndex);
	}

	if(totalRecord.equals("0"))
	{			
		out.clear();
		out.println("NO DATA");
	}
	else
	{
		 sQuery = "select VAR_REC_1 from WFINSTRUMENTTABLE where ProcessInstanceID =:ProcessInstanceID ";
		 params="ProcessInstanceID=="+winame;

		 strInputXML = "<?xml version='1.0'?><APSelectWithNamedParam_Input>"
					+ "<option>APSelectWithNamedParam</option>" + "<Query>" + sQuery + "</Query>" + "<Params>" + params
					+ "</Params>" + "<EngineName>" + sCabname + "</EngineName>" + "<SessionID>" + sSessionId
					+ "</SessionID>" + "</APSelectWithNamedParam_Input>";

	WriteLog("Exec_datacopy_procedure jsp: 2 strInputXML: "+strInputXML);
	try 
	{
		strOutputXML = NGEjbClient.getSharedInstance().makeCall(sJtsIp, iJtsPort, appServerType, strInputXML);
		WriteLog("Exec_datacopy_procedure jsp: 2 strOutputXML: "+strOutputXML);
	} 
	catch (NGException e) 
	{
	   e.printStackTrace();
	} 
	catch (Exception ex) 
	{
	   ex.printStackTrace();
	}
	
	if(strOutputXML.equals(""))
	{
		System.out.println("Network Error !! Could not connect to server");
	}
	else
	{
		startIndex=strOutputXML.indexOf("<TotalRetrieved>");			
		endIndex=strOutputXML.indexOf("</TotalRetrieved>");	
		totalRecord=strOutputXML.substring(startIndex+16,endIndex);
	}

	if(totalRecord.equals("0"))
	{			
		System.out.println("No workitem - data Exists!!");
	}
	else
	{
		startIndex=strOutputXML.indexOf("<td>",4);	
		endIndex=strOutputXML.indexOf("</td>");	
		itemIndex=strOutputXML.substring(startIndex+4,endIndex);
	}
	WriteLog("Exec_datacopy_procedure jsp: itemIndex: "+itemIndex);
	String sOutputXML="";
	String sFinalString="";
	XMLParser xmlParser = new XMLParser();
	
	
	String sInputXML = "<?xml version=\"1.0\"?>\n" 
		+"<APProcedure_WithDBO_Input>\n" 
		+"<option>APProcedure_WithDBO</option>\n" 
		+"<ProcName>"+ProcName+"</ProcName>\n" 
		+"<Params>"+"'"+old_app_no+"','"+itemIndex+"'"+"</Params>\n" 
		+"<EngineName>"+sCabname+"</EngineName>\n"
		+"<SessionId>"+sSessionId+"</SessionID>\n"
		+"</APProcedure_WithDBO_Input>";
	
	
	WriteLog("Exec_datacopy_procedure jsp: sInputXML: "+sInputXML.toString());
	System.out.println("inputXML---"+sInputXML.toString());	
		
	
	try 
	{
		sOutputXML = NGEjbClient.getSharedInstance().makeCall(sJtsIp, iJtsPort, appServerType, sInputXML);
		WriteLog("Exec_datacopy_procedure jsp: sOutputXML: "+sOutputXML);
	} 
	catch (NGException e) 
	{
	   e.printStackTrace();
	} 
	catch (Exception ex) 
	{
	   ex.printStackTrace();
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
	}
	
%>

