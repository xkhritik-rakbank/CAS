<%@ page import="java.sql.*"%>
<%@ page import="java.util.*,java.io.*" %>
<%@ page import="java.lang.Object"%>
<%@ page import="java.text.*"%>
<%@ page import="java.lang.String.*"%>
<%@page import="com.newgen.omni.wf.util.excp.NGException,com.newgen.omni.wf.util.app.NGEjbClient"%>
 <%@page import ="java.io.IOException,java.io.StringReader,java.util.HashMap,java.util.Map,javax.xml.parsers.DocumentBuilder,javax.xml.parsers.DocumentBuilderFactory,javax.xml.parsers.ParserConfigurationException,org.w3c.dom.Document,org.w3c.dom.Node,org.w3c.dom.NodeList,org.xml.sax.InputSource,org.xml.sax.SAXException"%>
<%@page import="com.newgen.wfdesktop.xmlapi.*,java.io.*,java.util.*,com.newgen.wfdesktop.session.*"%>
<%@ page import="org.owasp.esapi.ESAPI"%>
<%@ page import="org.owasp.esapi.codecs.OracleCodec"%>
<%@ page import="org.owasp.esapi.User" %>
 
<jsp:useBean id="wDSession" class="com.newgen.wfdesktop.session.WDSession" scope="session"/>


<%!
	public  String getTagValue(String xml,String tag) throws ParserConfigurationException, SAXException, IOException  
			{
				Document doc=getDocument(xml);
				NodeList nodeList = doc.getElementsByTagName(tag);
				
				int length = nodeList.getLength();
				
				if (length > 0) 
				{
					Node node =  nodeList.item(0);
					//System.out.println("Node : " + node);
					if (node.getNodeType() == Node.ELEMENT_NODE) 
					{
						NodeList childNodes = node.getChildNodes();
						String value = "";
						int count = childNodes.getLength();
						for (int i = 0; i < count; i++) 
						{
							Node item = childNodes.item(i);
							if (item.getNodeType() == Node.TEXT_NODE) 
							{
								value += item.getNodeValue();
							}
						}
						return value;
					} 
					else if (node.getNodeType() == Node.TEXT_NODE) 
					{
						return node.getNodeValue();
					}
				}
				return "";
			}
			public  Document getDocument(String xml) throws ParserConfigurationException, SAXException, IOException  
			{
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(new InputSource(new StringReader(xml)));
				return doc;
			}
			public  NodeList getNodeListFromDocument(Document doc,String identifier)
			{
				NodeList records = doc.getElementsByTagName(identifier);
				return records;
			}
			public  Map getKeyValueMapFromNode(Node record,String [] keys)
			{
				Map map = new HashMap();
				NodeList columnList = record.getChildNodes();
				int columnLength = columnList.getLength();
				//System.out.println("columnLength "+columnLength);
				for (int col = 0, i = 0; col  < columnLength; ++col) 
				{
					Node columnItem = columnList.item(col);
					if (columnItem.getNodeType() == Node.ELEMENT_NODE && columnItem.getNodeName().equalsIgnoreCase("td")) 
					{
						if( columnItem.getTextContent()==null)
							map.put(keys[i++].trim(),"");
						else if( columnItem.getTextContent().equalsIgnoreCase("null"))
							map.put(keys[i++].trim(),"");
						else
							map.put(keys[i++].trim(), columnItem.getTextContent());
					}
				}
				return map;
			}
	
 %>


<%
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader ("Expires", 0);

	boolean bError=false;
	String strInputXML="";
	String strOutputXML="";
	String processdefid = "";
	String pname="";
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
	//XMLParser xmlParserData=new XMLParser();	
	
	
		try
	{	//svt points start
		String input1 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("pname"), 1000, true) );
	    pname = ESAPI.encoder().encodeForSQL(new OracleCodec(), input1);
		String input2 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("old_WI_Name"), 1000, true) );
		old_winame= ESAPI.encoder().encodeForSQL(new OracleCodec(), input2);	  
		String input3 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("sessionId"), 1000, true) );		
		sessionId= ESAPI.encoder().encodeForSQL(new OracleCodec(), input3);
		//svt points end
		out.println("\n sessionId: "+sessionId);
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
	}
	catch(Exception ignore)
	{
		bError=true;
		result="Error"+"~"+"Some error occured in processing the request. Please contact administrator."+ignore;
		//out.println(result);
	}	
	
	try
	{
		strInputXML="<?xml version=\"1.0\"?><APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option><Query>select ProcessDefId from PROCESSDEFTABLE where ProcessName = '"+pname+"' and processstate='Enabled'</Query><Params></Params><EngineName>"+cabinetName+"</EngineName><SessionId>"+sessionId+"</SessionId></APSelectWithColumnNames_Input>";
		strOutputXML=NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXML);
		//xmlParserData.setInputXML((strOutputXML));
		
		//out.println("Select Input xml :"+strInputXML);
		//out.println("Select Output xml :"+strOutputXML);
		mainCodeValue = getTagValue(strOutputXML,"MainCode");
		if(mainCodeValue.equals("0"))
		{
			processdefid= getTagValue(strOutputXML,"ProcessDefId");
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
			strInputXML="<?xml version=\"1.0\"?><WFUploadWorkItem_Input><Option>WFUploadWorkItem</Option><EngineName>"+cabinetName+"</EngineName><SessionId>"+sessionId+"</SessionId><ValidationRequired></ValidationRequired><ProcessDefId>"+processdefid+"</ProcessDefId><InitiateFromActivityId>17</InitiateFromActivityId><DataDefName></DataDefName><Documents></Documents><Attributes></Attributes></WFUploadWorkItem_Input>";
				
			
			//out.println("WFUploadWorkItem Input xml :::::"+strInputXML);
			//out.println("WFUploadWorkItem Input xml ::"+strInputXML);
			strOutputXML=NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXML);
		
			mainCodeValue="";
			mainCodeValue = getTagValue(strOutputXML,"MainCode");
			
			
			//out.println("WFUploadWorkItem Output xml :"+strOutputXML);
			
			if(mainCodeValue.equals("0"))
			{
				new_winame= getTagValue(strOutputXML,"ProcessInstanceId");
				itemindex_new=getTagValue(strOutputXML,"FolderIndex");
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
			mainCodeValue="";
			mainCodeValue = getTagValue(strOutputXML,"MainCode");
			//out.println("itemindex Output xml :"+strOutputXML);
			if(mainCodeValue.equals("0"))
			{
				itemindex_old= getTagValue(strOutputXML,"itemindex");
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
		
			mainCodeValue="";
			mainCodeValue = getTagValue(strOutputXML,"MainCode");
			
			
			//out.println("approcedure_withdbo Output xml :"+strOutputXML);
			
			if(mainCodeValue.equals("0"))
			{
				result="Success~Successfully created workitem "+new_winame;
				//out.println(result);
			}	
			else
			{
				bError=true;
				result="Error"+"~"+"Some error occured while executing the procedure";
				//out.println(result);
			}
		}
	}
	catch(Exception ignore)
	{
		bError=true;
		result="Error"+"~"+"Some error occured in processing the request. Please contact administrator."+ignore;		
	}	
%>
<%
out.clear();
out.println(result);
%>
