<%@ page import ="java.io.*" %>
<%@ page import ="java.util.*"%>
<%@ page import="com.newgen.wfdesktop.xmlapi.WFCallBroker" %>
<%@ page import="org.apache.commons.fileupload.*" %>
<%@ page import="org.apache.commons.fileupload.disk.*" %>
<%@ page import="org.apache.commons.fileupload.servlet.*" %>
<%@ page import="org.apache.commons.io.output.*" %>
<%@ page import = "com.lowagie.text.pdf.PdfReader, com.lowagie.text.pdf.codec.TiffImage, com.lowagie.text.pdf.RandomAccessFileOrArray"%>
<%@page import="com.newgen.omni.wf.util.excp.NGException"%>
<%@page import="com.newgen.omni.wf.util.app.NGEjbClient"%>
<%@page import="com.newgen.custom.XMLParser"%>
<%@page import="org.w3c.dom.Node"%>
<%@page import="org.w3c.dom.NodeList"%>
<%@page import="java.lang.Iterable"%>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page  import= "com.newgen.custom.*, org.w3c.dom.*, org.w3c.dom.NodeList,org.w3c.dom.Document,javax.xml.parsers.DocumentBuilder, javax.xml.parsers.DocumentBuilderFactory"%>
<%@ page  import= "org.w3c.dom.Element"%>
<%@ page  import= "java.io.File"%>
<%@ page  import= "java.util.LinkedHashMap"%>
<%@ page  import= "java.util.Map"%>
<%@ page  import= "java.io.ByteArrayInputStream"%>
<%@ page  import= "java.io.File"%>
<%@ page  import= "java.io.FileInputStream"%>
<%@ page  import= "java.io.FileNotFoundException"%>
<%@ page  import= "java.io.InputStream"%>
<%@ page  import= "java.util.ArrayList"%>
<%@ page  import= "java.util.Arrays"%>
<%@ page import= "org.w3c.dom.ls.DOMImplementationLS"%>
<%@ page import= "org.w3c.dom.ls.LSSerializer"%>
<%@ page import= "org.w3c.dom.Document"%>
<%@ page import="org.owasp.esapi.ESAPI"%>
<%@ page import="org.owasp.esapi.codecs.OracleCodec"%>
<%@ page import="org.owasp.esapi.User" %>


<%
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader ("Expires", 0);

	boolean bError=false;
	String strInputXML="";
	String strOutputXML="";
	String processdefid = "";
	//String pname="";
	String old_winame="";
	String new_winame="";
	String result="";
	String mainCodeValue="";
	String cabinetName="";
	String sessionId ="";
	String wrapperPort="";
	String wrapperIP ="";
	String appServerType="";
	String itemindex_new="";
	String itemindex_old="";
	XMLParser xmlParserData=null;
	//XMLParser xmlParserData=new XMLParser();	
	
	//out.println("11113");
	
		try
	{			
		
		//svt points start
		String input1 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("pname"), 1000, true) );
		String pname = ESAPI.encoder().encodeForSQL(new OracleCodec(), input1);
		String input2 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("old_WI_Name"), 1000, true) );
		old_winame= ESAPI.encoder().encodeForSQL(new OracleCodec(), input2);
		//svt points end
		String prop_file_loc=System.getProperty("user.dir") + File.separatorChar+"CustomConfig"+File.separatorChar+"ServerConfig.properties";
		File file = new File(prop_file_loc);
        FileInputStream fileInput = new FileInputStream(file);
        Properties properties = new Properties();
        properties.load(fileInput);
        fileInput.close();
		cabinetName = properties.getProperty("cabinetName");
		out.println("Cabinet: "+cabinetName);
		wrapperIP = properties.getProperty("wrapperIP");
		out.println("\n wrapperIP: "+wrapperIP);
		wrapperPort = properties.getProperty("wrapperPort");
		out.println("\n wrapperPort: "+wrapperPort);
		appServerType = properties.getProperty("appServerType");
		out.println("\n appServerType: "+appServerType);
	  //svt points start
	   String input3 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("sessionId"), 1000, true) );		
		sessionId= ESAPI.encoder().encodeForSQL(new OracleCodec(), input3);
		//svt points end
		out.println("\n sessionId: "+sessionId);
			
	}
	catch(Exception ignore)
	{
		bError=true;
		ignore.printStackTrace();
		result="Errorr"+"~"+"Some error occured in processing the request. Please contact administrator."+ignore.getMessage();
		//out.println(result);
	}	
	
	try
	{
		strInputXML="<?xml version=\"1.0\"?><APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option><Query>select ProcessDefId from PROCESSDEFTABLE where ProcessName = '"+pname+"' and processstate='Enabled'</Query><Params></Params><EngineName>"+cabinetName+"</EngineName><SessionId>"+sessionId+"</SessionId></APSelectWithColumnNames_Input>";
		
		out.println("\n strInputXML: "+strInputXML);
		
		strOutputXML=NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXML);
		
		out.println("\n strOutputXML: "+strOutputXML);
		
		xmlParserData = new XMLParser();
		xmlParserData.setInputXML(strOutputXML);
		
		mainCodeValue = xmlParserData.getValueOf("MainCode");
		
		if(mainCodeValue.equals("0"))
		{
			processdefid= xmlParserData.getValueOf("ProcessDefId");
			//getTagValue(strOutputXML,"ProcessDefId");
			//out.println("processdefid :"+processdefid);
		}	
		else
		{
			bError=true;
			result="Error"+"~"+"Some error occured while fetching the processdefid.";
			//out.println(result);
		}
		
		if(processdefid!="" && !bError)
		{
			strInputXML="<?xml version=\"1.0\"?><WFUploadWorkItem_Input><Option>WFUploadWorkItem</Option><EngineName>"+cabinetName+"</EngineName><SessionId>"+sessionId+"</SessionId><ValidationRequired></ValidationRequired><ProcessDefId>1017</ProcessDefId><InitiateFromActivityId>17</InitiateFromActivityId><DataDefName></DataDefName><Documents></Documents><Attributes></Attributes></WFUploadWorkItem_Input>";
				
			
		
			strOutputXML=NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXML);
			xmlParserData.setInputXML((strOutputXML));
			mainCodeValue="";
			mainCodeValue = xmlParserData.getValueOf("MainCode");
			
			
			//out.println("WFUploadWorkItem Output xml :"+strOutputXML);
			
			if(mainCodeValue.equals("0"))
			{
				new_winame= xmlParserData.getValueOf("ProcessInstanceId");
				itemindex_new=xmlParserData.getValueOf("FolderIndex");
			}	
			else
			{
				bError=true;
				result="Error"+"~"+"Some error occured while creating the workitem.";
				//out.println(result);
			}
		}
		if(new_winame!="" && !bError)
		{
			strInputXML="<?xml version=\"1.0\"?><APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option><Query>select itemindex from NG_RLOS_EXTTABLE where WIname='"+old_winame+"'</Query><Params></Params><EngineName>"+cabinetName+"</EngineName><SessionId>"+sessionId+"</SessionId></APSelectWithColumnNames_Input>";
			//out.println("itemindex Input xml ::"+strInputXML);
			strOutputXML=NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXML);
			xmlParserData.setInputXML((strOutputXML));
			mainCodeValue="";
			mainCodeValue = xmlParserData.getValueOf("MainCode");
			//out.println("itemindex Output xml :"+strOutputXML);
			if(mainCodeValue.equals("0"))
			{
				itemindex_old= xmlParserData.getValueOf("itemindex");
				//out.println("old itemindex: "+itemindex_old);
			}	
			else
			{
				bError=true;
				result="Error"+"~"+"Some error occured while fetching the itemindex.";
				//out.println(result);
			}
		}
		if(itemindex_new!="" && itemindex_old!="" && !bError)
		{
			strInputXML="<APProcedure_Input><Option>APProcedure_WithDBO</Option><ProcName>NG_RLOS_COPY_DATA_PROC</ProcName><Params>'"+itemindex_old+"','"+itemindex_new+"'</Params>"+
						"<EngineName>"+cabinetName+"</EngineName>"+
						"<SessionId>"+sessionId+"</SessionId>"+
						"</APProcedure_Input>";
				
			
			//out.println("WFUploadWorkItem Input xml :::::"+strInputXML);
			//out.println("approcedure_withdbo Input xml ::"+strInputXML);
			strOutputXML=NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXML);
			xmlParserData.setInputXML((strOutputXML));
			mainCodeValue="";
			mainCodeValue = xmlParserData.getValueOf("MainCode");
			
			
			//out.println("approcedure_withdbo Output xml :"+strOutputXML);
			
			if(mainCodeValue.equals("0"))
			{
				result="Success!! new workitem "+new_winame+" created and data copied successfully from "+old_winame;
				out.println(result);
			}	
			else
			{
				bError=true;
				result="Error"+"~"+"Some error occured while executing the procedure";
				out.println(result);
			}
		}
	}
	catch(Exception ignore)
	{
		bError=true;
		result="Error"+"~"+"Some error occured in processing the request. Please contact administrator."+ignore;
		out.println(result);
	}	
%>
