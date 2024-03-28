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
<%@ page import="com.newgen.omni.wf.util.excp.NGException"%>
<%@ include file="Log.process"%>
<jsp:useBean id="wDSession" class="com.newgen.wfdesktop.session.WDSession" scope="session"/>


<%  String sCabname="";
	String sSessionId = wDSession.getM_objUserInfo().getM_strSessionId();
	String sJtsIp = "";
	String iJtsPort = "";
	String appServerType = "";
	
	String winame= request.getParameter("wi_name");
	String ProcName ="NG_RLOS_COPY_DATA_PROC";//request.getParameter("ProcName");
	String old_app_no = request.getParameter("old_app_no");
	
	
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
	
	String sQuery="Select winame from ng_rlos_exttable where itemindex='"+old_app_no+"'";
  
  String strInputXML = "<?xml version=\"1.0\"?>"
	+"<APSelect_Input>"
	+"<Option>APSelect</Option>"
	+"<Query>" + sQuery + "</Query>"
	+"<EngineName>" + sCabname + "</EngineName>"
	+"<SessionId>" + sSessionId + "</SessionId>" 
	+"</APSelect_Input>";

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
		 sQuery = "select VAR_REC_1 from WFINSTRUMENTTABLE where ProcessInstanceID ='"+winame+"' ";

	 strInputXML = "<?xml version=\"1.0\"?>"
	+"<APSelect_Input>"
	+"<Option>APSelect</Option>"
	+"<Query>" + sQuery + "</Query>"
	+"<EngineName>" + sCabname + "</EngineName>"
	+"<SessionId>" + sSessionId + "</SessionId>" 
	+"</APSelect_Input>";

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

