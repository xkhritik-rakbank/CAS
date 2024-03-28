<%@ include file="Log.process"%>
<jsp:useBean id="wDSession"
	class="com.newgen.wfdesktop.session.WDSession" scope="session" />
<jsp:useBean id="WDCabinet"
	class="com.newgen.wfdesktop.baseclasses.WDCabinetInfo" scope="session" />
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.newgen.wfdesktop.xmlapi.WFCallBroker"%>
<%@ page import="org.apache.commons.fileupload.*"%>
<%@ page import="org.apache.commons.fileupload.disk.*"%>
<%@ page import="org.apache.commons.fileupload.servlet.*"%>
<%@ page import="org.apache.commons.io.output.*"%>
<%@ page
	import="com.lowagie.text.pdf.PdfReader, com.lowagie.text.pdf.codec.TiffImage, com.lowagie.text.pdf.RandomAccessFileOrArray"%>
<%@page import="com.newgen.omni.wf.util.excp.NGException"%>
<%@page import="com.newgen.omni.wf.util.app.NGEjbClient"%>
<%@page import="com.newgen.custom.XMLParser"%>
<%@page import="org.w3c.dom.Node"%>
<%@page import="org.w3c.dom.NodeList"%>
<%@page import="java.lang.Iterable"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page
	import="com.newgen.custom.*, org.w3c.dom.*, org.w3c.dom.NodeList,org.w3c.dom.Document,javax.xml.parsers.DocumentBuilder, javax.xml.parsers.DocumentBuilderFactory"%>
<%@ page import="org.w3c.dom.Element"%>
<%@ page import="java.io.File"%>
<%@ page import="java.util.LinkedHashMap"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.io.ByteArrayInputStream"%>
<%@ page import="java.io.File"%>
<%@ page import="java.io.FileInputStream"%>
<%@ page import="java.io.FileNotFoundException"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Arrays"%>
<%@ page import= "org.w3c.dom.ls.DOMImplementationLS"%>
<%@ page import= "org.w3c.dom.ls.LSSerializer"%>
<%@ page import="org.w3c.dom.Document"%>
<%@ page import="java.io.StringReader" %>
<%@ page import="org.xml.sax.InputSource" %>
<%@ page import= "java.io.StringWriter" %>
<%@ page import= "javax.xml.parsers.DocumentBuilder" %>
<%@ page import= "javax.xml.parsers.DocumentBuilderFactory" %>
<%@ page import= "javax.xml.transform.OutputKeys" %>
<%@ page import = "javax.xml.transform.Transformer" %>
<%@ page import= "javax.xml.transform.TransformerException" %>
<%@ page import= "javax.xml.transform.TransformerFactory" %>
<%@ page import= "javax.xml.transform.dom.DOMSource" %>
<%@ page import= "javax.xml.transform.stream.StreamResult" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.owasp.esapi.ESAPI"%>
<%@ page import="org.owasp.esapi.codecs.OracleCodec"%>
<%@ page import="org.owasp.esapi.User" %>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>


<script language="javascript"
	src="/webdesktop/custom/CustomJS/workdesk.js"></script>

<%
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader ("Expires", 0);
	//svt points start
	String row_count = ""; 
	String CardNumber = "";

	String appServerType = "";
	String wrapperIP = "";
	String wrapperPort = "";    

	String cabinetName = "";

	String input1 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("result"), 1000, true) );
	String result = ESAPI.encoder().encodeForSQL(new OracleCodec(), input1!=null?input1:"");
	
	String input2 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("wi_name"), 1000, true) );
	String wi_name = ESAPI.encoder().encodeForSQL(new OracleCodec(), input2!=null?input2:"");
	
	String input3 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("prod"), 1000, true) );
	String prod = ESAPI.encoder().encodeForSQL(new OracleCodec(), input3!=null?input3:"");
	
	String input4 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("subprod"), 1000, true) );
	String subprod = ESAPI.encoder().encodeForSQL(new OracleCodec(), input4!=null?input4:"");
	
	String input5 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("cifId").trim(), 1000, true) );
	String cifId = ESAPI.encoder().encodeForSQL(new OracleCodec(), input5!=null?input5:"");
	
	String input6 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("parentWiName"), 1000, true) );
	String parentWiName = ESAPI.encoder().encodeForSQL(new OracleCodec(), input6!=null?input6:"");
	
	/*String input7 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("SeesionId"), 1000, true) ); */
	String sessionId = wDSession.getM_objUserInfo().getM_strSessionId();
	
	String input8 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("cust_type"), 1000, true) );
	String cust_type = ESAPI.encoder().encodeForSQL(new OracleCodec(), input8!=null?input8:"");
	//svt points end
	
	try {
		//svt points start
		String input10 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("row_count"), 1000, true) );
		row_count = ESAPI.encoder().encodeForSQL(new OracleCodec(), input10!=null?input10:"");
		
		
		String input11 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("CardNumber"), 1000, true) );
		String CardNumber_encode = ESAPI.encoder().encodeForSQL(new OracleCodec(), input11!=null?input11:"");
		//svt points end
		String prop_file_loc=System.getProperty("user.dir") + File.separatorChar+"CustomConfig"+File.separatorChar+"ServerConfig.properties";
	    File file = new File(prop_file_loc);
        FileInputStream fileInput = new FileInputStream(file);
        Properties properties = new Properties();
        properties.load(fileInput);
        fileInput.close();
		cabinetName = properties.getProperty("cabinetName");
		wrapperIP = properties.getProperty("wrapperIP");
		wrapperPort = properties.getProperty("wrapperPort");
		appServerType = properties.getProperty("appServerType");
    } 
	catch(Exception e){
		System.out.println("Exception occured in custexpose_output_PL: "+ e.getMessage());
		e.printStackTrace();
		out.print("false"+"@#"+row_count);
	}
	String outputXMLMsg = "";
	String flagOp="";
	try
		{		
			String process_tablename="";
			if("PL".equalsIgnoreCase(wi_name.substring(0, 2))){
				process_tablename="NG_PL_XMLLOG_HISTORY";
			}
			else if(wi_name.substring(0,wi_name.indexOf("-")).equalsIgnoreCase("CDOB")){
				process_tablename="NG_CDOB_XMLLOG_HISTORY";
			}
			else{
				process_tablename="NG_CC_XMLLOG_HISTORY";
			}
			String squery = "select OUTPUT_XML from "+process_tablename+" with (nolock) where MESSAGE_ID ='"+result+"' and WI_NAME = '"+wi_name+"'";
			String strInputXml_1 = ExecuteQuery_APSelect(squery,cabinetName,sessionId);
			String strOutputXml_1 = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml_1);
			
			//WriteLog("Out put XML of "+result+" : "+strOutputXml_1+"and row_count"+row_count);
			if(!"".equalsIgnoreCase(strOutputXml_1)){
				String row_count_str = strOutputXml_1.substring(strOutputXml_1.indexOf("<TotalRetrieved>")+16,strOutputXml_1.indexOf("</TotalRetrieved>"));
				int result_count = Integer.parseInt(row_count_str);
				if (result_count>0){
					if(strOutputXml_1.indexOf("<MQ_RESPONSE_XML>")>-1)
					{
						outputXMLMsg=strOutputXml_1.substring(strOutputXml_1.indexOf("<MQ_RESPONSE_XML>")+17,strOutputXml_1.indexOf("</MQ_RESPONSE_XML>"));
						
						cifId =cifId.trim();
					 flagOp = getOutputXMLValues(outputXMLMsg,appServerType,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,prod,subprod,cifId,parentWiName,CardNumber,cust_type);
					}
				}
				else{
					flagOp="waiting";
				}
			}
		}
		catch(Exception e)
		{          
			System.out.println("Exception occured in custexpose_output_PL: "+ e.getMessage());
			e.printStackTrace();
			flagOp="false";
		}
	
	out.clear();
	out.println(flagOp+"@#"+row_count);
	//WriteLog("wrapperIP jsp: parseInternalExposure:flagOp "+flagOp+"and row_count"+row_count);
%>

<%!

	public static String getOutputXMLValues(String parseXml,String appServerType,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name, String prod, String subprod, String cifId, String parentWiName,String CardNumber,String cust_type)
	{
		String outputXMLHead = "";
		String outputXMLMsg = "";
		String returnDesc = "";
		String returnCode = "";
		String response= "";
		String returnType="";
		String result_str = "";
		String MsgFormat="";
		String CompanyCIF="";
	try
		{		
			
			String squery_comp = "select case when COUNT(CompanyCIF)>0 then  ISNULL(CompanyCIF,'') else '' end as CompanyCIF  from NG_RLOS_GR_CompanyDetails where comp_winame ='"+wi_name+"' and applicantCategory='Business' group by CompanyCIF";
			String strInputXml_comp = ExecuteQuery_APSelect(squery_comp,cabinetName,sessionId);
			String strOutputXml_comp = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml_comp);
			
			WriteLog("Out put XML of company : "+strOutputXml_comp);
			if(!"".equalsIgnoreCase(strOutputXml_comp)){
				String row_count_str_comp = strOutputXml_comp.substring(strOutputXml_comp.indexOf("<TotalRetrieved>")+16,strOutputXml_comp.indexOf("</TotalRetrieved>"));
				int result_count_comp = Integer.parseInt(row_count_str_comp);
				if (result_count_comp>0){
					CompanyCIF=strOutputXml_comp.substring(strOutputXml_comp.indexOf("<CompanyCIF>")+12,strOutputXml_comp.indexOf("</CompanyCIF>"));
				}
				else{
					CompanyCIF="";
				}
			}
		}
		catch(Exception e)
		{          
			System.out.println("Exception occured in custexpose_output_PL company : "+ e.getMessage());
			e.printStackTrace();
			
		}
		try
		{
			
			if(parseXml.indexOf("<EE_EAI_HEADER>")>-1)
			{
				outputXMLHead=parseXml.substring(parseXml.indexOf("<EE_EAI_HEADER>"),parseXml.indexOf("</EE_EAI_HEADER>")+16);
				//WriteLog("RLOSCommon valueSetCustomer"+ outputXMLHead);
			}
			if(outputXMLHead.indexOf("<MsgFormat>")>-1)
			{
				response= outputXMLHead.substring(outputXMLHead.indexOf("<MsgFormat>")+11,outputXMLHead.indexOf("</MsgFormat>"));
				//WriteLog("$$response "+response);
			}
			if(outputXMLHead.indexOf("<ReturnDesc>")>-1)
			{
				returnDesc= outputXMLHead.substring(outputXMLHead.indexOf("<ReturnDesc>")+12,outputXMLHead.indexOf("</ReturnDesc>"));
				//WriteLog("$$returnDesc "+returnDesc);
			}
			if(outputXMLHead.indexOf("<ReturnCode>")>-1)
			{
				returnCode= outputXMLHead.substring(outputXMLHead.indexOf("<ReturnCode>")+12,outputXMLHead.indexOf("</ReturnCode>"));
				//WriteLog("$$returnCode "+returnCode);
			}
			//Deepak changes done for Commented for PCSP-526
			if(parseXml.indexOf("<RequestType>")>-1)
			{
				returnType= parseXml.substring(parseXml.indexOf("<RequestType>")+13,parseXml.indexOf("</RequestType>"));
				
				if("0000".equalsIgnoreCase(returnCode) || ("ExternalExposure".equalsIgnoreCase(returnType) && ("B003".equalsIgnoreCase(returnCode)||"B005".equalsIgnoreCase(returnCode))))
				{
					if("InternalExposure".equalsIgnoreCase(returnType))
					{
						result_str=parseInternalExposure(returnType,parseXml,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,appServerType,prod,subprod,cifId,parentWiName,cust_type,CompanyCIF);
					}
					else if("ExternalExposure".equalsIgnoreCase(returnType))
					{
						result_str=parseExternalExposure(returnType,parseXml,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,appServerType,prod,subprod,cifId,parentWiName,cust_type);
					}
					else if("CollectionsSummary".equalsIgnoreCase(returnType))
					{
						result_str=parseCollectionSummary(returnType,parseXml,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,appServerType,prod,subprod,cifId,parentWiName,cust_type,CompanyCIF);
					}	
				}
				
				if(!"0000".equalsIgnoreCase(returnCode))
				{	
					String errorQuery="SELECT isnull((SELECT alert FROM ng_MASTER_INTEGRATION_ERROR_CODE WHERE  error_code='"+returnCode+"'),(SELECT alert FROM ng_MASTER_INTEGRATION_ERROR_CODE WHERE error_code='DEFAULT')) As Alert";
					String strInputXml = ExecuteQuery_APSelect(errorQuery,cabinetName,sessionId);
					String strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);
					result_str=strOutputXml.substring(strOutputXml.indexOf("<Alert>")+"</Alert>".length()-1,strOutputXml.indexOf("</Alert>"));
					return result_str;
				}
			}

			//added
			if(parseXml.indexOf("<MsgFormat>")>-1)
			{
				returnType= parseXml.substring(parseXml.indexOf("<MsgFormat>")+11,parseXml.indexOf("</MsgFormat>"));
				//WriteLog("$$MsgFormat "+returnType);
				//Added By Prabhakar
				if(!"0000".equalsIgnoreCase(returnCode))
				{
					String errorQuery="SELECT isnull((SELECT alert FROM ng_MASTER_INTEGRATION_ERROR_CODE WHERE  error_code='"+returnCode+"'),(SELECT alert FROM ng_MASTER_INTEGRATION_ERROR_CODE WHERE error_code='DEFAULT')) As Alert";
					//WriteLog("@@@@@@@@@@@@@@  "+errorQuery);
					String strInputXml = ExecuteQuery_APSelect(errorQuery,cabinetName,sessionId);
					String strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);
					//WriteLog("Deepak Out put: "+strOutputXml);
					result_str=strOutputXml.substring(strOutputXml.indexOf("<Alert>")+"</Alert>".length()-1,strOutputXml.indexOf("</Alert>"));
					//WriteLog("@@@@@@@@@@@@@@  "+result_str);
					return result_str;
				}
				if(returnType.equalsIgnoreCase("CARD_INSTALLMENT_DETAILS"))
				{
					//WriteLog("cifId jsp: getOutputXMLValuesresult:CardInstallmentDetailsFlag inside card installment123");
					result_str=parseCardInstallmentsDetails(returnType,parseXml,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,appServerType,prod,subprod,cifId,CardNumber,parentWiName);
					//WriteLog("cifId jsp: getOutputXMLValuesresult:CardInstallmentDetailsFlag "+result_str);
				}
			}
			//ended


			if(parseXml.indexOf("<OperationType>")>-1)
			{
				returnType= parseXml.substring(parseXml.indexOf("<OperationType>")+15,parseXml.indexOf("</OperationType>"));
				//WriteLog("$$returnType "+returnType);
				//Added By Prabhakar
				if(!"0000".equalsIgnoreCase(returnCode))
				{

					String errorQuery="SELECT isnull((SELECT alert FROM ng_MASTER_INTEGRATION_ERROR_CODE WHERE  error_code='"+returnCode+"'),(SELECT alert FROM ng_MASTER_INTEGRATION_ERROR_CODE WHERE error_code='DEFAULT')) As Alert";
					//WriteLog("@@@@@@@@@@@@@@  "+errorQuery);
					String strInputXml = ExecuteQuery_APSelect(errorQuery,cabinetName,sessionId);
					String strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);
					result_str=strOutputXml.substring(strOutputXml.indexOf("<alert>")+"</alert>".length()-1,strOutputXml.indexOf("</alert>"));
					//WriteLog("select result is: "+result_str);
					//WriteLog("@@@@@@@@@@@@@@  "+result_str);
					return result_str;
				}
				if(returnType.equalsIgnoreCase("TRANSUM"))
				{
					result_str=parseTRANSUM(returnType,parseXml,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,appServerType,cifId,parentWiName);
				}
				else if(returnType.equalsIgnoreCase("AVGBALDET"))
				{
					result_str=parseAVGBALDET(returnType,parseXml,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,appServerType,cifId,parentWiName);
				}
				else if(returnType.equalsIgnoreCase("RETURNDET"))
				{
					result_str=parseRETURNDET(returnType,parseXml,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,appServerType,cifId,parentWiName);
				}
				else if(returnType.equalsIgnoreCase("LIENDET"))
				{
					result_str=parseLIENDET(returnType,parseXml,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,appServerType,cifId,parentWiName);
				}
				else if(returnType.equalsIgnoreCase("SIDET"))
				{
					result_str=parseSIDET(returnType,parseXml,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,appServerType,cifId,parentWiName);
				}
				else if(returnType.equalsIgnoreCase("SALDET"))
				{
					result_str=parseSALDET(returnType,parseXml,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,appServerType,cifId,parentWiName);
				}

			}
			returnType= parseXml.substring(parseXml.indexOf("<MsgFormat>")+11,parseXml.indexOf("</MsgFormat>"));
			//WriteLog("$$returnType result_strresult_strresult_str"+returnType);
			//WriteLog("$$MsgFormat "+returnType);
			if(returnType.equalsIgnoreCase("FINANCIAL_SUMMARY") &&(result_str.equalsIgnoreCase(""))){
				result_str=returnCode;
				//WriteLog("$$result_str result_strresult_strresult_str"+result_str);
			}
			//ended

		}
		catch(Exception e)
		{            
			System.out.println("Exception occured in getOutputXMLValues: "+ e.getMessage());
			e.printStackTrace();
			//WriteLog("Exception occured in getOutputXMLValues method:  "+e.getMessage());
			result_str ="Failure";
		}
		return (result_str);
	}


	public static String parseInternalExposure(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String prod, String subprod, String cifId, String parentWiName,String cust_type,String CompanyCIF)
	{
		String flag1="";
		//WriteLog("wrapperIP jsp: parseInternalExposure: "+wrapperIP);
		//WriteLog("wrapperPort jsp: parseInternalExposure: "+wrapperPort);
		//WriteLog("sessionId jsp: parseInternalExposure: "+sessionId);
		//WriteLog("cabinetName jsp: parseInternalExposure: "+cabinetName);
		//WriteLog("wi_name jsp: parseInternalExposure: "+wi_name);
		//WriteLog("appServerType jsp: parseInternalExposure: "+appServerType);
		//WriteLog("parseXml jsp: parseInternalExposure: "+parseXml);
		//WriteLog("returnType jsp: parseInternalExposure: "+returnType);
		//WriteLog("cifId jsp: parseInternalExposure: "+cifId);
		//WriteLog("parentWiName jsp: parseInternalExposure: "+parentWiName);

		String tagName="";
		String subTagName="";
		String sTableName="";
		String sParentTagName="";
		String result="";
		//Deepak code commented method changed with new subtag_single param 23jan2018
		String subtag_single="";
		InputStream is = new ByteArrayInputStream(parseXml.getBytes());
		//WriteLog("getTagDataParent_deep jsp: strOutputXml: "+is);
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();

			NodeList nList_loan = doc.getElementsByTagName("CustomerExposureResponse");


			for(int i = 0 ; i<nList_loan.getLength();i++)
			{
				Node node  = nList_loan.item(i);
				Document newXmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
				DOMImplementationLS abc  = (DOMImplementationLS) newXmlDocument.getImplementation();
				LSSerializer lsSerializer = abc.createLSSerializer();

				Element root = newXmlDocument.createElement("root");
				newXmlDocument.appendChild(root);
				root.appendChild(newXmlDocument.importNode(node, true));
				String n_parseXml = lsSerializer.writeToString(newXmlDocument);
				n_parseXml = n_parseXml.substring(n_parseXml.indexOf("<root>")+6,n_parseXml.indexOf("</root>"));
				cifId =  (n_parseXml.contains("<CustIdValue>")) ? n_parseXml.substring(n_parseXml.indexOf("<CustIdValue>")+"</CustIdValue>".length()-1,n_parseXml.indexOf("</CustIdValue>")):cifId;
				WriteLog("cif parseInternalExposure: "+cifId);
				WriteLog("Company cif parseInternalExposure: "+CompanyCIF);
				if(!CompanyCIF.equalsIgnoreCase("") && cifId.equalsIgnoreCase(CompanyCIF))
				{
					cust_type="Corporate_CIF";
				}
				else
				{
					cust_type="Individual_CIF";
				}
				tagName="LoanDetails"; 
				subTagName = "KeyDt,AmountDtls,DelinquencyInfo";
				sTableName="ng_RLOS_CUSTEXPOSE_LoanDetails";
				subtag_single="";
				flag1=commonParseProduct(n_parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,parentWiName,cust_type,subtag_single);

				if(flag1.equalsIgnoreCase("true")){
					tagName="CardDetails";
					subTagName = "KeyDt,AmountDtls,DelinquencyInfo";
					sTableName="ng_RLOS_CUSTEXPOSE_CardDetails";
					subtag_single="";
					flag1=commonParseProduct(n_parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,parentWiName,cust_type,subtag_single);
					
					if(flag1.equalsIgnoreCase("true")){
					tagName="InvestmentDetails";
					subTagName = "AmountDtls";
					sTableName="ng_RLOS_CUSTEXPOSE_InvestmentDetails";
					subtag_single="";
					flag1=commonParseProduct(n_parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,parentWiName,cust_type,subtag_single);
					
					if(flag1.equalsIgnoreCase("true")){
						tagName="AcctDetails";
						subTagName = "KeyDt,AmountDtls,DelinquencyInfo";
						sTableName="ng_RLOS_CUSTEXPOSE_AcctDetails";
						subtag_single="ODDetails";
						flag1=commonParseProduct(n_parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,parentWiName,cust_type,subtag_single);
						if(flag1.equalsIgnoreCase("true")){
							tagName="Derived";
							subTagName = "";
							sTableName="NG_rlos_custexpose_Derived";
							subtag_single="";
							flag1=commonParseProduct(n_parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,parentWiName,cust_type,subtag_single);
							if(flag1.equalsIgnoreCase("true")){
								tagName="RecordDestribution";
								subTagName = "";
								sTableName="NG_RLOS_CUSTEXPOSE_RecordDestribution";
								subtag_single="";
								flag1=commonParseProduct(n_parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,parentWiName,cust_type,subtag_single);
								  //Deepak 22 july 2019 new condition added to save custinfo
								if(flag1.equalsIgnoreCase("true")){
									tagName="CustInfo";
									subTagName = "";
									sTableName="ng_RLOS_CUSTEXPOSE_CustInfo";
									subtag_single="";
									flag1=commonParseProduct(n_parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,parentWiName,cust_type,subtag_single);
									}
								else{
									flag1="false";
									}
							}
							else{
								flag1="false";
							}
						}
						else{
							flag1="false";
						}
					}
					else
					{
					flag1="false";
					}
					}
					else{
						flag1="false";
					}
				}
				else
				{
					flag1="false";
				}
			}
		}
		catch(Exception e){
			System.out.println("Exception occured in parseInternalExposure: "+ e.getMessage());
			e.printStackTrace();
			flag1="false";
		}
				finally
			{
				try{
			    		if(is!=null)
			    		{
			    		is.close();
			    		is=null;
			    		}
			    	}
			    	catch(Exception e){
			    		WriteLog("Exception occured in is close:  "+e.getMessage());
			    	}
			}
		return flag1;
	}
	public static String parseExternalExposure(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String prod, String subprod, String cifId, String parentWiName,String cust_type)
	{
		String flag1="";
		try{
			String ReturnCode="";

			if(parseXml.indexOf("<ReturnCode>")>-1)
			{
				ReturnCode= parseXml.substring(parseXml.indexOf("<ReturnCode>")+12,parseXml.indexOf("</ReturnCode>"));
				//WriteLog("$$return Code "+ReturnCode);
			}

			//Commented for PCSP-526
			/* if(ReturnCode.equalsIgnoreCase("B003"))
			{
				//WriteLog("AECB:No record found!!");
				return "B003";
			} */
			//WriteLog("wrapperIP jsp: parseExternalExposure: "+wrapperIP);
			//WriteLog("wrapperPort jsp: parseExternalExposure: "+wrapperPort);
			//WriteLog("sessionId jsp: parseExternalExposure: "+sessionId);
			//WriteLog("cabinetName jsp: parseExternalExposure: "+cabinetName);
			//WriteLog("wi_name jsp: parseExternalExposure: "+wi_name);
			//WriteLog("appServerType jsp: parseExternalExposure: "+appServerType);
			//WriteLog("parseXml jsp: parseExternalExposure: "+parseXml);
			//WriteLog("returnType jsp: parseExternalExposure: "+returnType);
			//WriteLog("cifId jsp: parseExternalExposure: "+cifId);
			//WriteLog("parentWiName jsp: parseExternalExposure: "+parentWiName);

			String tagName="";
			String subTagName="";
			String sTableName="";

			String subtag_single="";

			tagName="ChequeDetails"; 
			subTagName = "";
			sTableName="ng_rlos_cust_extexpo_ChequeDetails";
			flag1 = commonParseProduct(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,parentWiName,cust_type,subtag_single);
			//WriteLog("cifId jsp: parseExternalExposure: updated or inserted"+flag1);

			if(flag1.equalsIgnoreCase("true")){
				tagName="LoanDetails"; 
				subTagName = "KeyDt,AmountDtls";
				sTableName="ng_rlos_cust_extexpo_LoanDetails";
				flag1 = commonParseProduct(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,parentWiName,cust_type,subtag_single);
				//WriteLog("cifId jsp: parseExternalExposure: updated or inserted1"+flag1);

				if(flag1.equalsIgnoreCase("true")){
					tagName="CardDetails"; 
					subTagName = "KeyDt,AmountDtls,DelinquencyInfo";
					sTableName="ng_rlos_cust_extexpo_CardDetails";
					flag1 = commonParseProduct(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,parentWiName,cust_type,subtag_single);
					//WriteLog("cifId jsp: parseExternalExposure: updated or inserted2"+flag1);
					if(flag1.equalsIgnoreCase("true")){
						tagName="Derived"; 
						subTagName = "";
						sTableName="NG_rlos_custexpose_Derived";
						flag1 = commonParseProduct(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,parentWiName,cust_type,subtag_single);	
						//WriteLog("cifId jsp: parseExternalExposure: updated or NG_rlos_custexpose_Derived"+flag1);
						if(flag1.equalsIgnoreCase("true")){
							tagName="RecordDestribution";
							subTagName = "";
							sTableName="NG_RLOS_CUSTEXPOSE_RecordDestribution";
							flag1=commonParseProduct(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,parentWiName,cust_type,subtag_single);
							if(flag1.equalsIgnoreCase("true")){
								tagName="AcctDetails";
								subTagName = "KeyDt,AmountDtls";
								sTableName="ng_rlos_cust_extexpo_AccountDetails";
								flag1=commonParseProduct(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,parentWiName,cust_type,subtag_single);
								if(flag1.equalsIgnoreCase("true")){
									tagName="ServicesDetails";
									subTagName = "KeyDt,AmountDtls";
									sTableName="ng_rlos_cust_extexpo_ServicesDetails";
									flag1=commonParseProduct(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,parentWiName,cust_type,subtag_single);
									}
								else{
									flag1="false";
									}
							}
							else{
								flag1="false";
							}
						}
						else{
							flag1="false";
						}
					}
					else{
						flag1 ="false";
						//WriteLog("cifId jsp: parseExternalExposure: updated or NG_rlos_custexpose_Derived"+flag1);
					}
				}
				else{
					flag1 ="false";
					//WriteLog("cifId jsp: parseExternalExposure: updated or insertedfalse"+flag1);
				}


			}
			else{
				flag1 ="false";
				//WriteLog("cifId jsp: parseExternalExposure: updated or insertedfalse1"+flag1);
			}
		}
		catch(Exception e){
			System.out.println("Exception occured in parseInternalExposure: "+ e.getMessage());
			e.printStackTrace();
			flag1="false";
		}

		//WriteLog("cifId jsp: parseExternalExposure: updated or inserted final value"+flag1);
		return flag1;
	}
	public static String parseCollectionSummary(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String prod, String subprod, String cifId, String parentWiName,String cust_type,String CompanyCIF)
	{
		//WriteLog("wrapperIP jsp: parseCollectionSummary: "+wrapperIP);
		//WriteLog("wrapperPort jsp: parseCollectionSummary: "+wrapperPort);
		//WriteLog("sessionId jsp: parseCollectionSummary: "+sessionId);
		//WriteLog("cabinetName jsp: parseCollectionSummary: "+cabinetName);
		//WriteLog("wi_name jsp: parseCollectionSummary: "+wi_name);
		//WriteLog("appServerType jsp: parseCollectionSummary: "+appServerType);
		//WriteLog("parseXml jsp: parseCollectionSummary: "+parseXml);
		//WriteLog("returnType jsp: parseCollectionSummary: "+returnType);
		//WriteLog("cifId jsp: parseCollectionSummary: "+cifId);
		//WriteLog("parentWiName jsp: parseCollectionSummary: "+parentWiName);
		String tagName="";
		String subTagName="";
		String sTableName="";
		String sParentTagName="";
		String result="";
		String flag1="";
		try{	

			//Deepak code commented method changed with new subtag_single param 23jan2018
			String subtag_single="";

			tagName="LoanDetails"; 
			subTagName = "KeyDt,AmountDtls,DelinquencyInfo";
			sTableName="ng_RLOS_CUSTEXPOSE_LoanDetails";
			flag1=commonParseProduct_collection(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,parentWiName,subtag_single,cust_type,CompanyCIF);

			if(flag1.equalsIgnoreCase("true")){
				tagName="CardDetails";
				subTagName = "KeyDt,AmountDtls,DelinquencyInfo";
				sTableName="ng_RLOS_CUSTEXPOSE_CardDetails";
				flag1=commonParseProduct_collection(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,parentWiName,subtag_single,cust_type,CompanyCIF);
				if(flag1.equalsIgnoreCase("true")){
					tagName="Derived";
					subTagName = "";
					sTableName="NG_rlos_custexpose_Derived";
					flag1=commonParseProduct(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,parentWiName,cust_type,subtag_single);

				}
				else{
					flag1="false";
				}
			}
			else{
				flag1="false";
			}
		}
		catch(Exception e){
			System.out.println("Exception occured in parseInternalExposure: "+ e.getMessage());
			e.printStackTrace();
			flag1="false";
		}

		return flag1;
	}

	//added
	public static String parseCardInstallmentsDetails(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String prod, String subprod, String cifId,String CardNumber, String parentWiName)
	{
		//WriteLog("wrapperIP jsp: parseCardInstallmentsDetails: "+wrapperIP);
		//WriteLog("wrapperPort jsp: parseCardInstallmentsDetails: "+wrapperPort);
		//WriteLog("sessionId jsp: parseCardInstallmentsDetails: "+sessionId);
		//WriteLog("cabinetName jsp: parseCardInstallmentsDetails: "+cabinetName);
		//WriteLog("wi_name jsp: parseCardInstallmentsDetails: "+wi_name);
		//WriteLog("appServerType jsp: parseCardInstallmentsDetails: "+appServerType);
		//WriteLog("parseXml jsp: parseCardInstallmentsDetails: "+parseXml);
		//WriteLog("returnType jsp: parseCardInstallmentsDetails: "+returnType);
		//WriteLog("cifId jsp: parseCardInstallmentsDetails: "+cifId);
		//WriteLog("cifId jsp: parseCardInstallmentsDetails:CardNumber "+CardNumber);

		String tagName="";
		String subTagName="";
		String sTableName="";
		String sParentTagName="";
		String result="";
		String flag1="";
		//Deepak code commented method changed with new subtag_single param 23jan2018
		String subtag_single="";
		try{
			tagName="TransactionDetailsRec"; 
			subTagName = "";
			sTableName="ng_RLOS_CUSTEXPOSE_CardInstallmentDetails";
			flag1=commonParseFinance_CardInstallment(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,parentWiName,CardNumber,subtag_single);

		}
		catch(Exception e){
			System.out.println("Exception occured in parseInternalExposure: "+ e.getMessage());
			e.printStackTrace();
			flag1="false";
		}

		//WriteLog("wrapperIP jsp: CardInstallmentDetailsResponse: "+flag1);


		//WriteLog("wrapperIP jsp: CardInstallmentDetailsResponse final value: "+flag1);
		return flag1;
	}
	//ended


	public static String parseTRANSUM(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String cifId, String parentWiName)
	{
		//WriteLog("wrapperIP jsp: parseTRANSUM: "+wrapperIP);
		//WriteLog("wrapperPort jsp: parseTRANSUM: "+wrapperPort);
		//WriteLog("sessionId jsp: parseTRANSUM: "+sessionId);
		//WriteLog("cabinetName jsp: parseTRANSUM: "+cabinetName);
		//WriteLog("wi_name jsp: parseTRANSUM: "+wi_name);
		//WriteLog("appServerType jsp: parseTRANSUM: "+appServerType);
		//WriteLog("parseXml jsp: parseTRANSUM: "+parseXml);
		//WriteLog("returnType jsp: parseTRANSUM: "+returnType);
		//WriteLog("cifId jsp: parseTRANSUM: "+cifId);

		String tagName="";
		String subTagName="";
		String sTableName="";
		String sParentTagName="";
		String flag1="";
		//Deepak code commented method changed with new subtag_single param 23jan2018
		String subtag_single="";
		try{
			tagName= "TxnSummaryDtls";		
			subTagName= "";
			sTableName="ng_rlos_FinancialSummary_TxnSummary";
			flag1=commonParseFinance(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,parentWiName,subtag_single);
			if(flag1.equalsIgnoreCase("true")){
				flag1="true";
			}
			else{
				flag1="false";
			}
		}
		catch(Exception e){
			System.out.println("Exception occured in parseInternalExposure: "+ e.getMessage());
			e.printStackTrace();
			flag1="false";
		}
		return flag1;
	}
	public static String parseAVGBALDET(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String cifId, String parentWiName)
	{
		//WriteLog("wrapperIP jsp: parseAVGBALDET: "+wrapperIP);
		//WriteLog("wrapperPort jsp: parseAVGBALDET: "+wrapperPort);
		//WriteLog("sessionId jsp: parseAVGBALDET: "+sessionId);
		//WriteLog("cabinetName jsp: parseAVGBALDET: "+cabinetName);
		//WriteLog("wi_name jsp: parseAVGBALDET: "+wi_name);
		//WriteLog("appServerType jsp: parseAVGBALDET: "+appServerType);
		//WriteLog("parseXml jsp: parseAVGBALDET: "+parseXml);
		//WriteLog("returnType jsp: parseAVGBALDET: "+returnType);
		//WriteLog("cifId jsp: parseAVGBALDET: "+cifId);

		String flag1="";
		String tagName="";
		String subTagName="";
		String sTableName="";
		String sParentTagName="";
		//Deepak code commented method changed with new subtag_single param 23jan2018
		String subtag_single="";

		try{
			tagName= "FinancialSummaryRes";		
			subTagName= "AvgBalanceDtls";
			sTableName="ng_rlos_FinancialSummary_AvgBalanceDtls";
			flag1=commonParseFinance(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,parentWiName,subtag_single);
			if(flag1.equalsIgnoreCase("true")){
				flag1="true";
			}
			else{
				flag1="false";
			}
		}
		catch(Exception e){
			System.out.println("Exception occured in parseInternalExposure: "+ e.getMessage());
			e.printStackTrace();
			flag1="false";
		}
		return flag1;
	}
	public static String parseRETURNDET(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String cifId, String parentWiName)
	{
		//WriteLog("wrapperIP jsp: parseRETURNDET: "+wrapperIP);
		//WriteLog("wrapperPort jsp: parseRETURNDET: "+wrapperPort);
		//WriteLog("sessionId jsp: parseRETURNDET: "+sessionId);
		//WriteLog("cabinetName jsp: parseRETURNDET: "+cabinetName);
		//WriteLog("wi_name jsp: parseRETURNDET: "+wi_name);
		//WriteLog("appServerType jsp: parseRETURNDET: "+appServerType);
		//WriteLog("parseXml jsp: parseRETURNDET: "+parseXml);
		//WriteLog("returnType jsp: parseRETURNDET: "+returnType);
		//WriteLog("cifId jsp: parseRETURNDET: "+cifId);

		String tagName="";
		String subTagName="";
		String sTableName="";
		String sParentTagName="";
		String flag1="";
		//Deepak code commented method changed with new subtag_single param 23jan2018
		String subtag_single="";
		try{
			tagName= "ReturnsDtls";		
			subTagName= "";
			sTableName="ng_rlos_FinancialSummary_ReturnsDtls";
			flag1=commonParseFinance(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,parentWiName,subtag_single);
			if(flag1.equalsIgnoreCase("true")){
				flag1="true";
			}
			else{
				flag1="false";
			}
		}
		catch(Exception e){
			System.out.println("Exception occured in parseInternalExposure: "+ e.getMessage());
			e.printStackTrace();
			flag1="false";
		}
		return flag1;

	}
	public static String parseLIENDET(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String cifId, String parentWiName)
	{
		//WriteLog("wrapperIP jsp: parseLIENDET: "+wrapperIP);
		//WriteLog("wrapperPort jsp: parseLIENDET: "+wrapperPort);
		//WriteLog("sessionId jsp: parseLIENDET: "+sessionId);
		//WriteLog("cabinetName jsp: parseLIENDET: "+cabinetName);
		//WriteLog("wi_name jsp: parseLIENDET: "+wi_name);
		//WriteLog("appServerType jsp: parseLIENDET: "+appServerType);
		//WriteLog("parseXml jsp: parseLIENDET: "+parseXml);
		//WriteLog("returnType jsp: parseLIENDET: "+returnType);
		//WriteLog("cifId jsp: parseLIENDET: "+cifId);

		String tagName="";
		String subTagName="";
		String sTableName="";
		String sParentTagName="";
		String flag1="";
		//Deepak code commented method changed with new subtag_single param 23jan2018
		String subtag_single="";
		tagName= "LienDetails";		
		subTagName= "";
		sTableName="ng_rlos_FinancialSummary_LienDetails";
		try{
			flag1=commonParseFinance(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,parentWiName,subtag_single);
			if(flag1.equalsIgnoreCase("true")){
				flag1="true";
			}
			else{
				flag1="false";
			}
		}
		catch(Exception e){
			System.out.println("Exception occured in parseInternalExposure: "+ e.getMessage());
			e.printStackTrace();
			flag1="false";
		}
		return flag1;
	}
	public static String parseSIDET(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String cifId, String parentWiName)
	{
		WriteLog("inside parseSIDET: ");
		

		String tagName="";
		String subTagName="";
		String sTableName="";
		String sParentTagName="";
		String flag1="";
		//Deepak code commented method changed with new subtag_single param 23jan2018
		String subtag_single="";

		tagName= "SIDetails";		
		subTagName= "";
		sTableName="ng_rlos_FinancialSummary_SiDtls";
		try{
			flag1=commonParseFinance(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,parentWiName,subtag_single);
			if(flag1.equalsIgnoreCase("true")){
				flag1="true";
			}
			else{
				flag1="false";
			}
		}
		catch(Exception e){
			System.out.println("Exception occured in parseInternalExposure: "+ e.getMessage());
			e.printStackTrace();
			flag1="false";
		}
		return flag1;

	}
	public static String parseSALDET(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String cifId, String parentWiName)
	{
		//WriteLog("wrapperIP jsp: parseSALDET: "+wrapperIP);
		//WriteLog("wrapperPort jsp: parseSALDET: "+wrapperPort);
		//WriteLog("sessionId jsp: parseSALDET: "+sessionId);
		//WriteLog("cabinetName jsp: parseSALDET: "+cabinetName);
		//WriteLog("wi_name jsp: parseSALDET: "+wi_name);
		//WriteLog("appServerType jsp: parseSALDET: "+appServerType);
		//WriteLog("parseXml jsp: parseSALDET: "+parseXml);
		//WriteLog("returnType jsp: parseSALDET: "+returnType);
		//WriteLog("cifId jsp: parseSALDET: "+cifId);

		String tagName="";
		String subTagName="";
		String sTableName="";
		String sParentTagName="";
		String flag1="";
		//Deepak code commented method changed with new subtag_single param 23jan2018
		String subtag_single="";
		tagName= "SalDetails";		
		subTagName= "";
		sTableName="ng_rlos_FinancialSummary_SalTxnDetails";
		
		if(parseXml.indexOf("<AcctId>")>-1){
			String acc_no = parseXml.substring(parseXml.indexOf("<AcctId>")+"</AcctId>".length()-1,parseXml.indexOf("</AcctId>"));	
			String sWhere="Child_Wi='"+wi_name+"' AND OperationType='"+returnType+"' AND AcctId = '"+acc_no+"'";
			String strInputXml =	ExecuteQuery_APdelete(sTableName,sWhere,cabinetName,sessionId);
			WriteLog( "strInputXml delete returndtls " + strInputXml);
			try 
			{
				String strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);
				WriteLog("CustExpose_Output jsp: strOutputXml delete SalDetails: "+strOutputXml);
			} 
			catch (NGException e) 
			{
				e.printStackTrace();
			} 
			catch (Exception ex) 
			{
				ex.printStackTrace();
			}

			
		}
		try{
			flag1=commonParseFinance(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,parentWiName,subtag_single);
			//WriteLog("return flag1 jsp: parseSALDET: "+flag1);
		}
		catch(Exception e){
			System.out.println("Exception occured in parseInternalExposure: "+ e.getMessage());
			e.printStackTrace();
			flag1="false";
		}
		return flag1;
	}
	public static String commonParse(String parseXml,String tagName,String wi_name,String returnType,String sTableName,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String appServerType, String subTagName, String cifId, String parentWiName)
	{
		String retVal = "";
		//WriteLog("commonParse jsp: inside: ");
		String [] valueArr= null;
		String strInputXml="";
		String strOutputXml="";
		String columnName = "";
		String columnValues = "";
		String tagNameU = "";
		String subTagNameU = "";
		String subTagNameU_2 = "";
		String mainCode = "";

		String sWhere = "";
		String row_updated = "";

		//WriteLog("tagName jsp: commonParse: "+tagName);
		//WriteLog("subTagName jsp: commonParse: "+subTagName);

		try{
			Map<Integer, String> tagValuesMap= new LinkedHashMap<Integer, String>();		 
			tagValuesMap=getTagDataParent(parseXml,tagName,subTagName);

			Map<Integer, String> map = tagValuesMap;
			String colValue="";
			for (Map.Entry<Integer, String> entry : map.entrySet())
			{
				valueArr=entry.getValue().split("~");
				//WriteLog( "tag values" + entry.getValue());

				//columnValues = valueArr[1].spilt(",");
				// columnValues=columnValues+",'"+getCellData(SheetName1, rCnt, cCnt)+"'";
				colValue = "'"+valueArr[1].replaceAll("[,]", "','")+"'";
				columnName = valueArr[0]+",Child_Wi,Request_Type,CifId";
				columnValues = colValue+",'"+wi_name+"','"+returnType+"','"+cifId+"'";

				//WriteLog( "columnName commonParse" + columnName);
				//WriteLog( "columnValues commonParse" + columnValues);
				String[] columnValues_arr = columnValues.split(",");
				if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_CardDetails")){
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND CardEmbossNum = "+columnValues_arr[0];
				}
				else if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_LoanDetails")){
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+columnValues_arr[0];
				}
				else if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_InvestmentDetails")){
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND InvestmentID = "+columnValues_arr[0];
				}
				else{
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"'";
				}

				strInputXml =	ExecuteQuery_APUpdate(sTableName,columnName,columnValues,sWhere,cabinetName,sessionId);
				//WriteLog( "strInputXml update " + strInputXml);
				try 
				{
					strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);

					//WriteLog("CustExpose_Output jsp: strOutputXml update: "+strOutputXml);
				} 
				catch (NGException e) 
				{
					e.printStackTrace();
				} 
				catch (Exception ex) 
				{
					ex.printStackTrace();
				}

				tagNameU = "APUpdate_Output";
				subTagNameU = "MainCode";
				subTagNameU_2 = "Output";
				mainCode = getTagValue(strOutputXml,tagNameU,subTagNameU);
				row_updated = getTagValue(strOutputXml,tagNameU,subTagNameU_2);
				//WriteLog("getTagValue select mainCode --> "+mainCode);
				if(!mainCode.equalsIgnoreCase("0") || row_updated.equalsIgnoreCase("0"))
				{
					strInputXml =	ExecuteQuery_APInsert(sTableName,columnName,columnValues,cabinetName,sessionId);
					//WriteLog( "strInputXml" + strInputXml);
					try 
					{
						strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);

						//WriteLog("CustExpose_Output jsp: strOutputXml: "+strOutputXml);
						mainCode = getTagValue(strOutputXml,"APInsert_Output",subTagNameU);
						if(!mainCode.equalsIgnoreCase("0"))
						{
							retVal = "false";
							//WriteLog("CustExpose_Output jsp: commonParse true for insert: "+retVal);
						}
						else
						{
							retVal = "true";
							//WriteLog("CustExpose_Output jsp: commonParse false for insert: "+retVal);
						}
					} 
					catch (NGException e) 
					{
						e.printStackTrace();
					} 
					catch (Exception ex) 
					{
						ex.printStackTrace();
					}
				}
				else
				{
					retVal = "true";
					//WriteLog("CustExpose_Output jsp: commonParse true for update: "+retVal);
				}
			}
		}
		catch(Exception e){
			System.out.println("Exception occured in parseInternalExposure: "+ e.getMessage());
			e.printStackTrace();
			retVal="false";
		}
		//WriteLog("CustExpose_Output jsp: commonParse true for final return value: "+retVal);
		return retVal;

	}

	public static String commonParseFinance_CardInstallment(String parseXml,String tagName,String wi_name,String returnType,String sTableName,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String appServerType, String subTagName,String parentWiName, String CardNumber,String subtag_single)
	{
		//WriteLog("commonParseFinance jsp: inside: ");
		String retVal = "";
		String [] valueArr= null;
		String strInputXml="";
		String strOutputXml="";
		String columnName = "";
		String columnValues = "";
		String tagNameU = "";
		String subTagNameU = "";
		String subTagNameU_2 = "";
		String mainCode = "";
		String sWhere = "";
		String row_updated = "";
		String txnNum="";
		//WriteLog("tagName jsp: commonParseFinance: "+tagName);
		//WriteLog("subTagName jsp: commonParseFinance: "+subTagName);
		//WriteLog("sTableName jsp: commonParseFinance: "+sTableName);
		try{

			if((returnType.equalsIgnoreCase("CARD_INSTALLMENT_DETAILS")&& parseXml.contains("TransactionDetailsRec")))
			{

				//WriteLog("returnType jsp: commonParseFinance: "+returnType);
				Map<String, String> tagValuesMap= new LinkedHashMap<String, String>();		 
				tagValuesMap=getTagDataParent_deep(parseXml,tagName,subTagName,subtag_single);

				Map<String , String> map = tagValuesMap;
				String colValue="";
				for (Map.Entry<String, String> entry : map.entrySet())
				{
					valueArr=entry.getValue().split("~");
					for(int i=0;i<valueArr.length;i++)
					{
						//WriteLog( "tag values:12345 " +valueArr[i]);
					}
					//WriteLog( "tag values: " + entry.getValue());
					//WriteLog( "Key values: " + entry.getKey());

					colValue = "'"+valueArr[1].replaceAll("[,]", "','")+"'";


					//added
					if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_CardInstallmentDetails")){
						//WriteLog( "Inside commonParseFinance for ng_RLOS_CUSTEXPOSE_CardInstallmentDetails");
						String header_info = getTagDataParent_cardInstallment_header(parseXml,"CardInstallmentDetailsResponse","CIFID,CardCRNNumber,CardSerialNumber,OTBAmount,TotalExposureAmount,TotalRepaymentAmount,InstallmentAccountStatus");

						//WriteLog("Inside commonParseFinance for ng_RLOS_CUSTEXPOSE_CardInstallmentDetails header info: "+ header_info);
						String [] header_info_arr = header_info.split(":");

						columnName = valueArr[0]+",Wi_Name,Child_Wi,Request_Type,"+header_info_arr[0];
						columnValues = valueArr[1]+",'"+parentWiName+"','"+wi_name+"','CARD_INSTALLMENT_DETAILS',"+header_info_arr[1];
						String columnName_arr[] = columnName.split(",");
						String columnValues_arr[] = columnValues.split(",");
						columnValues="";
						for(int i=0;i<columnName_arr.length;i++)
						{
							//WriteLog("Inside Card Installment for loop to remove I:"+columnName_arr[i]);
							if(columnName_arr[i].equalsIgnoreCase("CardNumber"))
							{
								columnValues_arr[i]=columnValues_arr[i].replace("I","");
							}
							if(i==0){
								columnValues=columnValues_arr[i];
							}
							else{
								columnValues=columnValues+","+columnValues_arr[i];
							}
						}

						txnNum = columnValues_arr[Arrays.asList(columnName_arr).indexOf("TxnSerialNum")];
						//WriteLog("Inside Cardinstallment: columnName after merging:"+columnValues);

						// sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND TxnSerialNum = '"+entry.getKey()+"' ";
						sWhere="Wi_Name='"+parentWiName+"' AND Child_Wi='"+wi_name+"' AND Request_Type='"+returnType+"' AND TxnSerialNum = "+txnNum+"";

						//WriteLog("sWhere of cardinstallmentDetails"+sWhere);
					}
					//ended

					else{
						columnName = valueArr[0]+",Wi_Name,Request_Type";
						columnValues = colValue+",'"+wi_name+"','"+returnType+"'";  
					}


					//WriteLog( "columnName commonParse123" + columnName);
					//WriteLog( "columnValues commonParse456" + columnValues);

					strInputXml =	ExecuteQuery_APUpdate(sTableName,columnName,columnValues,sWhere,cabinetName,sessionId);
					//WriteLog( "strInputXml update for finance " + strInputXml);
					try 
					{
						strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);

						//WriteLog("CustExpose_Output jsp: strOutputXml update:123 "+strOutputXml);
					} 
					catch (NGException e) 
					{
						e.printStackTrace();
					} 
					catch (Exception ex) 
					{
						ex.printStackTrace();
					}

					tagNameU = "APUpdate_Output";
					subTagNameU = "MainCode";
					subTagNameU_2 = "Output";
					mainCode = getTagValue(strOutputXml,tagNameU,subTagNameU);
					row_updated = getTagValue(strOutputXml,tagNameU,subTagNameU_2);
					//WriteLog("getTagValue select mainCode123 --> "+mainCode);
					//WriteLog("getTagValue select mainCode123 --> "+row_updated);
					if(!mainCode.equalsIgnoreCase("0") || row_updated.equalsIgnoreCase("0"))
					{
						strInputXml =	ExecuteQuery_APInsert(sTableName,columnName,columnValues,cabinetName,sessionId);
						//WriteLog( "strInputXml123Installment insert Query:" + strInputXml);
						try 
						{
							strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);

							//WriteLog("CustExpose_Output jsp: strOutputXml:1234 "+strOutputXml);
							//WriteLog("CustExpose_Output jsp: strOutputXml:mainCode value "+mainCode);
							mainCode = getTagValue(strOutputXml,"APInsert_Output",subTagNameU);
							//WriteLog("CustExpose_Output jsp: strOutputXml:mainCode value1234 "+mainCode);
							if(!mainCode.equalsIgnoreCase("0"))
							{
								retVal = "false";
								//WriteLog("CustExpose_Output jsp: commonparseproduct:false "+retVal);
							}
							else
							{
								retVal = "true";
								//WriteLog("CustExpose_Output jsp: commonparseproduct:true "+retVal);
							}
						} 
						catch (NGException e) 
						{
							e.printStackTrace();
						} 
						catch (Exception ex) 
						{
							ex.printStackTrace();
						}
					}
					else
					{
						retVal = "true";
						//WriteLog("CustExpose_Output jsp: commonparseproductapupdate:true "+retVal);
					}
				}

			}
			else{
				//WriteLog("returnType jsp: commonParseFinance Empty tag : "+returnType+" Wi_Name: "+wi_name);
			}
			//WriteLog("CustExpose_Output jsp: final value for financial summary "+retVal);

		}
		catch(Exception e){
			System.out.println("Exception occured in commonParseFinance_CardInstallment: "+ e.getMessage());
			e.printStackTrace();
			retVal="";
		}
		return retVal;
	}



	public static void parsePrimary_CIF(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String cifId, String parentWiName,String subtag_single)
	{
		//WriteLog("wrapperIP jsp: parsePrimary_CIF: "+wrapperIP);
		//WriteLog("wrapperPort jsp: parsePrimary_CIF: "+wrapperPort);
		//WriteLog("sessionId jsp: parsePrimary_CIF: "+sessionId);
		//WriteLog("cabinetName jsp: parsePrimary_CIF: "+cabinetName);
		//WriteLog("wi_name jsp: parsePrimary_CIF: "+wi_name);
		//WriteLog("appServerType jsp: parsePrimary_CIF: "+appServerType);
		//WriteLog("parseXml jsp: parsePrimary_CIF: "+parseXml);
		//WriteLog("returnType jsp: parsePrimary_CIF: "+returnType);
		//WriteLog("cifId jsp: parsePrimary_CIF: "+cifId);

		String tagName="";
		String subTagName="";
		String sTableName="";



		tagName= "FinancialSummaryRes";		
		subTagName= "BankId,CIFID,AcctId,OperationDesc,TxnSummary";
		sTableName="ng_rlos_FinancialSummary_tags";
		try{
			commonParseFinance(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,parentWiName,subtag_single);
		}
		catch(Exception e){
			System.out.println("Exception occured in parsePrimary_CIF: "+ e.getMessage());
			e.printStackTrace();
		
		}
		

	}
	public static void parseCorporation_CIF(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String cifId, String parentWiName,String subtag_single)
	{
		//WriteLog("wrapperIP jsp: parseCorporation_CIF: "+wrapperIP);
		//WriteLog("wrapperPort jsp: parseCorporation_CIF: "+wrapperPort);
		//WriteLog("sessionId jsp: parseCorporation_CIF: "+sessionId);
		//WriteLog("cabinetName jsp: parseCorporation_CIF: "+cabinetName);
		//WriteLog("wi_name jsp: parseCorporation_CIF: "+wi_name);
		//WriteLog("appServerType jsp: parseCorporation_CIF: "+appServerType);
		//WriteLog("parseXml jsp: parseCorporation_CIF: "+parseXml);
		//WriteLog("returnType jsp: parseCorporation_CIF: "+returnType);
		//WriteLog("cifId jsp: parseCorporation_CIF: "+cifId);

		String tagName="";
		String subTagName="";
		String sTableName="";
		try{
			tagName= "FinancialSummaryRes";		
			subTagName= "BankId,CIFID,AcctId,OperationDesc,TxnSummary";
			sTableName="ng_rlos_FinancialSummary_tags";
			commonParseFinance(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,parentWiName,subtag_single);
		}
		catch(Exception e){
			System.out.println("Exception occured in parseCorporation_CIF: "+ e.getMessage());
			e.printStackTrace();
			}
		}


	//added


	public static String commonParseFinance(String parseXml,String tagName,String wi_name,String returnType,String sTableName,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String appServerType, String subTagName, String parentWiName,String subtag_single)
	{
		//WriteLog("commonParseFinance jsp: inside: ");
		String retVal = "";
		String [] valueArr= null;
		String strInputXml="";
		String strOutputXml="";
		String columnName = "";
		String columnValues = "";
		String tagNameU = "";
		String subTagNameU = "";
		String subTagNameU_2 = "";
		String mainCode = "";
		String sWhere = "";
		String row_updated = "";
		String id="";
		WriteLog("tagName jsp: commonParseFinance: "+tagName);
		WriteLog("subTagName jsp: commonParseFinance: "+subTagName);
		WriteLog("sTableName jsp: commonParseFinance: "+sTableName);
		WriteLog("sTableName jsp: commonParseFinance: "+parseXml);
		try{
			if((returnType.equalsIgnoreCase("RETURNDET")&& parseXml.contains("ReturnsDtls"))||(returnType.equalsIgnoreCase("AVGBALDET")&& parseXml.contains("AcctId"))||(returnType.equalsIgnoreCase("LIENDET")&& parseXml.contains("LienDetails"))||(returnType.equalsIgnoreCase("SIDET")&& parseXml.contains("SIDetails"))||(returnType.equalsIgnoreCase("TRANSUM")&& parseXml.contains("TxnSummary"))||(returnType.equalsIgnoreCase("SALDET")&& parseXml.contains("SalDetails")))
			{

				//WriteLog("returnType jsp: commonParseFinance: "+returnType);
				Map<String, String> tagValuesMap= new LinkedHashMap<String, String>();		 
				tagValuesMap=getTagDataParent_deep(parseXml,tagName,subTagName,subtag_single);

				Map<String , String> map = tagValuesMap;
				String colValue="";


				for (Map.Entry<String, String> entry : map.entrySet())
				{
					valueArr=entry.getValue().split("~");
					//WriteLog( "tag values:1234 " +valueArr);
					//WriteLog( "tag values: " + entry.getValue());

					colValue = "'"+valueArr[1].replaceAll("[,]", "','")+"'";
					if(returnType.equalsIgnoreCase("AVGBALDET")&& valueArr[0].contains("AcctId")){
						String columnName_arr[] = valueArr[0].split(",");
						String columnValues_arr[] = valueArr[1].split(",");
						id = columnValues_arr[Arrays.asList(columnName_arr).indexOf("AcctId")];
					}

					if(sTableName.equalsIgnoreCase("ng_rlos_FinancialSummary_AvgBalanceDtls")){
						columnName = valueArr[0]+",Wi_Name,Child_Wi";
						columnValues = valueArr[1]+",'"+parentWiName+"','"+wi_name+"'";
						sWhere="Child_Wi='"+wi_name+"' AND OperationType='"+returnType+"' AND AcctId = "+id;
					}
					//modified by akshay on 6/2/18	
					else if(sTableName.equalsIgnoreCase("ng_rlos_FinancialSummary_ReturnsDtls")){
						//WriteLog( "Inside commonParseFinance for ng_rlos_FinancialSummary_ReturnsDtls");
						String header_info = getTagDataParent_financ_header(parseXml,"FinancialSummaryRes","CIFID,AcctId,OperationType");
						//WriteLog( "Inside commonParseFinance for ng_rlos_FinancialSummary_ReturnsDtls header info: "+ header_info);
						String [] header_info_arr = header_info.split(":");
						columnName = valueArr[0]+",Wi_Name,Child_Wi,"+header_info_arr[0];
						columnValues = valueArr[1]+",'"+parentWiName+"','"+wi_name+"',"+header_info_arr[1];
						//WriteLog("Inside Return Details-->columnValues: "+columnValues);
						String columnName_arr[] = columnName.split(",");
						String columnValues_arr[] = columnValues.split(",");
						if(returnType.equalsIgnoreCase("RETURNDET") && valueArr[0].contains("ReturnNumber")){
							id = columnValues_arr[Arrays.asList(columnName_arr).indexOf("ReturnNumber")];
							sWhere="Child_Wi='"+wi_name+"' AND OperationType='"+returnType+"' AND ReturnNumber = "+id;
						}else{
							id = columnValues_arr[Arrays.asList(columnName_arr).indexOf("AcctId")];
							sWhere="Child_Wi='"+wi_name+"' AND OperationType='"+returnType+"' AND AcctId = "+id;
						}
					}
					else if(sTableName.equalsIgnoreCase("ng_rlos_FinancialSummary_LienDetails")){
						//WriteLog( "Inside commonParseFinance for ng_rlos_FinancialSummary_LienDetails");
						String header_info = getTagDataParent_financ_header(parseXml,"FinancialSummaryRes","CIFID,AcctId,OperationType");
						//WriteLog( "Inside commonParseFinance for ng_rlos_FinancialSummary_LienDetails header info: "+ header_info);
						String [] header_info_arr = header_info.split(":");
						columnName = valueArr[0]+",Wi_Name,Child_Wi,"+header_info_arr[0];
						columnValues = valueArr[1]+",'"+parentWiName+"','"+wi_name+"',"+header_info_arr[1];

						String columnName_arr[] = columnName.split(",");
						String columnValues_arr[] = columnValues.split(",");
						id = columnValues_arr[Arrays.asList(columnName_arr).indexOf("AcctId")];
						String leinId=columnValues_arr[Arrays.asList(columnName_arr).indexOf("LienId")];
						sWhere="Child_Wi='"+wi_name+"' AND OperationType='"+returnType+"' AND AcctId = "+id+" and LienId = "+leinId;
						strInputXml =	ExecuteQuery_APdelete(sTableName,sWhere,cabinetName,sessionId);
						//WriteLog( "strInputXml delete returndtls " + strInputXml);
						try 
						{
							strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);

							//WriteLog("CustExpose_Output jsp: strOutputXml delete returndtls: "+strOutputXml);
						} 
						catch (NGException e) 
						{
							e.printStackTrace();
						} 
						catch (Exception ex) 
						{
							ex.printStackTrace();
						}
					}
					else if(sTableName.equalsIgnoreCase("ng_rlos_FinancialSummary_TxnSummary")){
						//WriteLog( "Inside commonParseFinance for ng_rlos_FinancialSummary_TxnSummary");
						String header_info = getTagDataParent_financ_header(parseXml,"FinancialSummaryRes","CIFID,AcctId,OperationType");
						//WriteLog( "Inside commonParseFinance for ng_rlos_FinancialSummary_TxnSummary header info: "+ header_info);
						String [] header_info_arr = header_info.split(":");
						columnName = valueArr[0]+",Wi_Name,Child_Wi,"+header_info_arr[0];
						columnValues = valueArr[1]+",'"+parentWiName+"','"+wi_name+"',"+header_info_arr[1];
						String columnName_arr[] =columnName.split(",");
						String columnValues_arr[] = columnValues.split(",");
						id = columnValues_arr[Arrays.asList(columnName_arr).indexOf("AcctId")];
						String Month = columnValues_arr[Arrays.asList(columnName_arr).indexOf("Month")];
						sWhere="Child_Wi='"+wi_name+"' AND OperationType='"+returnType+"' AND AcctId = "+id+" and Month = "+Month+"";
						strInputXml =	ExecuteQuery_APdelete(sTableName,sWhere,cabinetName,sessionId);
					}
					else if(sTableName.equalsIgnoreCase("ng_rlos_FinancialSummary_SalTxnDetails")){
						//WriteLog( "Inside commonParseFinance for ng_rlos_FinancialSummary_SalTxnDetails");
						String header_info = getTagDataParent_financ_header(parseXml,"FinancialSummaryRes","CifId,AcctId,OperationType");
						String [] header_info_arr = header_info.split(":");
						columnName = valueArr[0]+",Wi_Name,Child_Wi,"+header_info_arr[0];
						columnValues = valueArr[1]+",'"+parentWiName+"','"+wi_name+"',"+header_info_arr[1];

						String columnName_arr[] = columnName.split(",");
						String columnValues_arr[] = columnValues.split(",");
						id = columnValues_arr[Arrays.asList(columnName_arr).indexOf("AcctId")];
						String SalCreditDate = columnValues_arr[Arrays.asList(columnName_arr).indexOf("SalCreditDate")];
						sWhere="Child_Wi='"+wi_name+"' AND OperationType='"+returnType+"' AND AcctId = "+id+" and 1=2 and SalCreditDate = "+SalCreditDate+"";

					}
					else if(sTableName.equalsIgnoreCase("ng_rlos_FinancialSummary_SiDtls")){
						WriteLog( "inside commonParseFinance: ng_rlos_FinancialSummary_SiDtls "); 
						try 
						{
							String header_info = getTagDataParent_financ_header(parseXml,"FinancialSummaryRes","CifId,AcctId,OperationType");
							String [] header_info_arr = header_info.split(":");
							columnName = valueArr[0]+",Wi_Name,Child_Wi,"+header_info_arr[0];
							columnValues = valueArr[1]+",'"+parentWiName+"','"+wi_name+"',"+header_info_arr[1];
							String columnName_arr[] = columnName.split(",");
							String columnValues_arr[] = columnValues.split(",");
							id = columnValues_arr[Arrays.asList(columnName_arr).indexOf("AcctId")];
							String SINumber = columnValues_arr[Arrays.asList(columnName_arr).indexOf("SINumber")];
							columnName = valueArr[0]+",Wi_Name,Child_Wi,"+header_info_arr[0];
							columnValues = valueArr[1]+",'"+parentWiName+"','"+wi_name+"',"+header_info_arr[1];
							String sWhere_delete="Child_Wi='"+wi_name+"' AND OperationType='"+returnType+"' AND AcctId = "+id;
							sWhere="Child_Wi='"+wi_name+"' AND OperationType='"+returnType+"' AND AcctId = "+id+" And SINumber="+SINumber;
						//strInputXml =	ExecuteQuery_APdelete(sTableName,sWhere_delete,cabinetName,sessionId);
						//WriteLog( "strInputXml delete ng_rlos_FinancialSummary_SiDtls " + strInputXml);
						/* try 
						{
							strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);

							WriteLog("CustExpose_Output jsp: strOutputXml delete returndtls: "+strOutputXml);
						} 
						catch (NGException e) 
						{
							e.printStackTrace();
							*/
						}
						catch (Exception ex) 
						{
							WriteLog("Exception occured in ng_rlos_FinancialSummary_SiDtls: "+ex.getMessage());  
						}
					}
					else{
						columnName = valueArr[0]+",Wi_Name,Child_Wi,Request_Type";
						columnValues = colValue+",'"+parentWiName+"','"+wi_name+"','"+returnType+"'";  
					}


					//WriteLog( "columnName commonParse" + columnName);
					//WriteLog( "columnValues commonParse" + columnValues);

					strInputXml =	ExecuteQuery_APUpdate(sTableName,columnName,columnValues,sWhere,cabinetName,sessionId);
					WriteLog( "strInputXml update " + strInputXml);
					try 
					{
						strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);

						//WriteLog("CustExpose_Output jsp: strOutputXml update: "+strOutputXml);
					} 
					catch (NGException e) 
					{
						e.printStackTrace();
					} 
					catch (Exception ex) 
					{
						ex.printStackTrace();
					}
					//changed by akshay on 2/5/18 for proc 8964
					tagNameU = "APUpdate_Output";
					subTagNameU = "MainCode";
					subTagNameU_2 = "Output";
					mainCode = getTagValue(strOutputXml,tagNameU,subTagNameU);
					row_updated = getTagValue(strOutputXml,tagNameU,subTagNameU_2);
					//WriteLog("getTagValue select mainCode --> "+mainCode);
					//WriteLog("getTagValue select mainCode --> "+row_updated);
					if(!(mainCode.equalsIgnoreCase("0")) || row_updated.equalsIgnoreCase("0"))
					{
						strInputXml =	ExecuteQuery_APInsert(sTableName,columnName,columnValues,cabinetName,sessionId);
						WriteLog( "strInputXml" + strInputXml);
						try 
						{
							strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);
							tagNameU = "APInsert_Output";
							//WriteLog("CustExpose_Output jsp: strOutputXml: "+strOutputXml);
							mainCode = getTagValue(strOutputXml,tagNameU,subTagNameU);
							//WriteLog( "mainCode value is: " +mainCode );
							if(!mainCode.equalsIgnoreCase("0"))
							{
								retVal = "false";
								//WriteLog("CustExpose_Output jsp: ApINsertfalse for financial summary: "+retVal);
							}
							else
							{
								retVal = "true";
								//WriteLog("CustExpose_Output jsp: ApINserttrue for financial summary: "+retVal);
							}
						} 
						catch (NGException e) 
						{
							e.printStackTrace();
						} 
						catch (Exception ex) 
						{
							ex.printStackTrace();
						}
					}
					else
					{
						retVal = "true";
						//WriteLog("CustExpose_Output jsp: commonparseproductapupdate:true "+retVal);
					}
				}
			}
			else{
				retVal = "true";
				//WriteLog("returnType jsp: commonParseFinance Empty tag : "+returnType+" Wi_Name: "+wi_name);
			}
		}catch(Exception e){
			System.out.println("Exception occured in commonParseFinance: "+ e.getMessage());
			e.printStackTrace();
			retVal = "false";
		}
		//WriteLog("CustExpose_Output jsp: final value for financial summary "+retVal);
		return retVal;
	}


	public static String commonParseProduct(String parseXml,String tagName,String wi_name,String returnType,String sTableName,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String appServerType, String subTagName,String prod,String subprod, String cifId, String parentWiName,String cust_type,String subtag_single)
	{
		String retVal = "";

		try{
			if(!parseXml.contains(tagName)){
				return "true";
			} 
			else
			{
				//WriteLog("commonParse jsp: inside: ");
				String [] valueArr= null;
				String strInputXml="";
				String strOutputXml="";
				String columnName = "";
				String columnValues = "";
				String tagNameU = "";
				String subTagNameU = "";
				String subTagNameU_2 = "";
				String mainCode = "";
				String sWhere = "";
				String row_updated = "";
				String selectdata="";
				String 	sQry="";
				String ReportUrl = "";
				String NoOfContracts = "";
				String ECRN = "";
				String BorrowingCustomer = "";
				String FullNm = "";
				String TotalOutstanding = "";
				String TotalOverdue = "";

				String companyUpdateQuery="";
				String companiestobeUpdated = "";
				boolean stopIndividualToInsert = false;
				String referenceNo = "";
				String scoreInfo = "";
				String Aecb_Score = "";
				String range = "";

				//WriteLog("tagName jsp: commonParse: "+tagName);
				//WriteLog("subTagName jsp: commonParse: "+subTagName);
				//Parsing AECB score, range and Reference No. for 2.1 start, Added by Shivang
			
				referenceNo=(parseXml.contains("<ReferenceNumber>")) ? parseXml.substring(parseXml.indexOf("<ReferenceNumber>")+"</ReferenceNumber>".length()-1,parseXml.indexOf("</ReferenceNumber>")):"";
				if(parseXml.contains("<ScoreInfo>")){
					scoreInfo = parseXml.substring(parseXml.indexOf("<ScoreInfo>")+"</ScoreInfo>".length()-1,parseXml.indexOf("</ScoreInfo>"));
					Aecb_Score=(scoreInfo.contains("<Value>")) ? scoreInfo.substring(scoreInfo.indexOf("<Value>")+"</Value>".length()-1,scoreInfo.indexOf("</Value>")):"";
					range=(scoreInfo.contains("<Range>")) ? scoreInfo.substring(scoreInfo.indexOf("<Range>")+"</Range>".length()-1,scoreInfo.indexOf("</Range>")):"";
					WriteLog("parsexml jsp: commonParse: AECB Score: "+Aecb_Score + " Range: "+range);
				}
			
				//Parsing AECB score, range and Reference No. for 2.1 end, Added by Shivang
				//Deepak 23 Dec changes done to save updated Rerport URL in DB
				ReportUrl=(parseXml.contains("<ReportUrl>")) ? parseXml.substring(parseXml.indexOf("<ReportUrl>")+"</ReportUrl>".length()-1,parseXml.indexOf("</ReportUrl>")):"";
				//cifId=(parseXml.contains("<CustIdValue>")) ? parseXml.substring(parseXml.indexOf("<CustIdValue>")+"</CustIdValue>".length()-1,parseXml.indexOf("</CustIdValue>")):"";
				FullNm=(parseXml.contains("<FullNm>")) ? parseXml.substring(parseXml.indexOf("<FullNm>")+"</FullNm>".length()-1,parseXml.indexOf("</FullNm>")):"";
				TotalOutstanding=(parseXml.contains("<TotalOutstanding>")) ? parseXml.substring(parseXml.indexOf("<TotalOutstanding>")+"</TotalOutstanding>".length()-1,parseXml.indexOf("</TotalOutstanding>")):"";
				TotalOverdue=(parseXml.contains("<TotalOverdue>")) ? parseXml.substring(parseXml.indexOf("<TotalOverdue>")+"</TotalOverdue>".length()-1,parseXml.indexOf("</TotalOverdue>")):"";
				NoOfContracts=(parseXml.contains("<NoOfContracts>")) ? parseXml.substring(parseXml.indexOf("<NoOfContracts>")+"</NoOfContracts>".length()-1,parseXml.indexOf("</NoOfContracts>")):"";
				ECRN=(parseXml.contains("<ECRN>")) ? parseXml.substring(parseXml.indexOf("<ECRN>")+"</ECRN>".length()-1,parseXml.indexOf("</ECRN>")):"";
				BorrowingCustomer=(parseXml.contains("<BorrowingCustomer>")) ? parseXml.substring(parseXml.indexOf("<BorrowingCustomer>")+"</BorrowingCustomer>".length()-1,parseXml.indexOf("</BorrowingCustomer>")):"";


				Map<String, String> tagValuesMap= new LinkedHashMap<String, String>();		 
				tagValuesMap=getTagDataParent_deep(parseXml,tagName,subTagName,subtag_single);

				Map<String, String> map = tagValuesMap;
				// String colValue="";
				for (Map.Entry<String, String> entry : map.entrySet())
				{
					valueArr=entry.getValue().split("~");
					//WriteLog( "tag values" + entry.getValue());

					//columnValues = valueArr[1].spilt(",");
					// columnValues=columnValues+",'"+getCellData(SheetName1, rCnt, cCnt)+"'";
					//colValue = "'"+valueArr[1].replaceAll("[,]", "','")+"'";
					columnName = valueArr[0]+",Wi_Name,Request_Type,Product_Type,CardType,CifId,Child_Wi";
					columnValues = valueArr[1]+",'"+parentWiName+"','"+returnType+"','"+prod+"','"+subprod+"','"+cifId+"','"+wi_name+"'";



					//WriteLog( "columnName commonParse" + columnName);
					//WriteLog( "columnValues commonParse" + columnValues);
					if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_CardDetails")){
						columnName = valueArr[0]+",Wi_Name,Request_Type,CifId,Child_Wi,Liability_type";
						columnValues = valueArr[1]+",'"+parentWiName+"','"+returnType+"','"+cifId+"','"+wi_name+"','"+cust_type+"'";
						sWhere="CardEmbossNum = '"+entry.getKey()+"' AND Child_Wi='"+wi_name+"' And Liability_type ='"+cust_type+"'";
						sQry="Select count(*) as selectdata from "+sTableName+" where Child_Wi='"+wi_name+"' And CardEmbossNum = '"+entry.getKey()+"' And Liability_type ='Individual_CIF' ";
						//WriteLog( "sQry sQry" + sQry);
						if(cust_type.equalsIgnoreCase("Individual_CIF")) {
							companyUpdateQuery="Select count(*) as selectdata from "+sTableName+" where Child_Wi='"+wi_name+"' And CardEmbossNum = '"+entry.getKey()+"' And Liability_type ='Corporate_CIF'";
						}
						if(parseXml.contains("<LinkedCIFs>"))
						{
							parseLinkedCif(parseXml,sTableName,cifId,parentWiName,wi_name,entry.getKey(),cust_type,"Card",cabinetName,sessionId,wrapperIP,wrapperPort,appServerType);
						}
					}
					else if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_LoanDetails")){
						columnName = valueArr[0]+",Wi_Name,Request_Type,Product_Type,CardType,CifId,Child_Wi,Liability_type";
						columnValues = valueArr[1]+",'"+parentWiName+"','"+returnType+"','"+prod+"','"+subprod+"','"+cifId+"','"+wi_name+"','"+cust_type+"'";
						columnName =columnName.replace("OutStandingAmt","TotalOutStandingAmt");
						sWhere="AgreementId = '"+entry.getKey()+"' AND Child_Wi='"+wi_name+"' And Liability_type ='"+cust_type+"'";
						sQry="Select count(*) as selectdata from "+sTableName+" where Child_Wi='"+wi_name+"' And  AgreementId = '"+entry.getKey()+"' And Liability_type ='Individual_CIF'";
						//WriteLog( "sQry  loan sQry" + sQry);
						if(cust_type.equalsIgnoreCase("Individual_CIF")) {
							companyUpdateQuery="Select count(*) as selectdata from "+sTableName+" where Child_Wi='"+wi_name+"' And AgreementId = '"+entry.getKey()+"' And Liability_type ='Corporate_CIF'";
						}
						if(parseXml.contains("<LinkedCIFs>"))
						{
							parseLinkedCif(parseXml,sTableName,cifId,parentWiName,wi_name,entry.getKey(),cust_type,"Loan",cabinetName,sessionId,wrapperIP,wrapperPort,appServerType);
						}
					}
					else if(sTableName.equalsIgnoreCase("ng_rlos_cust_extexpo_ChequeDetails")){
						columnName = valueArr[0]+",Wi_Name,Request_Type,CifId,Child_Wi";
						columnValues = valueArr[1]+",'"+parentWiName+"','"+returnType+"','"+cifId+"','"+wi_name+"'";
						sWhere="Wi_Name='"+parentWiName+"' AND Number = '"+entry.getKey()+"' AND Child_Wi='"+wi_name+"'";
					}
					else if(sTableName.equalsIgnoreCase("ng_rlos_cust_extexpo_LoanDetails")){
						String History = parseHistoryUtilization(parseXml, entry.getKey(), "LoanDetails", "<History>", "</History>");
						History = History.replace("\n", "").replace("\r", "");
						String Utilization = parseHistoryUtilization(parseXml, entry.getKey(), "LoanDetails", "<Utilizations24Months>", "</Utilizations24Months>");
						Utilization = Utilization.replace("\n", "").replace("\r", "");
						 WriteLog( "inside parseHistoryUtilization" + History);
						 WriteLog( "inside parseHistoryUtilization" + Utilization);
						columnName = valueArr[0]+",Wi_Name,Request_Type,Product_Type,CardType,CifId,Child_Wi,Liability_type";
						columnValues = valueArr[1]+",'"+parentWiName+"','"+returnType+"','"+prod+"','"+subprod+"','"+cifId+"','"+wi_name+"','"+cust_type+"'";
						String columnName_arr[] = columnName.split(",");
						  String columnValues_arr[] = columnValues.split(",");
						  for(int arrlen=0;arrlen<columnName_arr.length;arrlen++){
							  if("LoanType".equalsIgnoreCase(columnName_arr[arrlen])){
								  WriteLog( "inside loan desc tag name" + columnName_arr[arrlen]);
								  WriteLog( "inside loan desc tag value" + columnValues_arr[arrlen]);
								  String loan_desc = get_loanDesc(columnValues_arr[arrlen], cabinetName, sessionId, wrapperIP,wrapperPort, appServerType);
								  columnValues = columnValues.replaceFirst(columnValues_arr[arrlen], loan_desc);
								  
							  }
							  if("History".equalsIgnoreCase(columnName_arr[arrlen])){
								 // WriteLog( "inside loan desc tag name" + columnName_arr[arrlen]);
								 // WriteLog( "inside loan desc tag value" + columnValues_arr[arrlen]);
								  //String loan_desc = get_loanDesc(columnValues_arr[arrlen], cabinetName, sessionId, wrapperIP,wrapperPort, appServerType);
								  columnValues = columnValues.replace(columnValues_arr[arrlen], "'"+History+"'");
								  
							  }
							  if("Utilizations24Months".equalsIgnoreCase(columnName_arr[arrlen])){
								 // WriteLog( "inside loan desc tag name" + columnName_arr[arrlen]);
								 // WriteLog( "inside loan desc tag value" + columnValues_arr[arrlen]);
								  //String loan_desc = get_loanDesc(columnValues_arr[arrlen], cabinetName, sessionId, wrapperIP,wrapperPort, appServerType);
								  columnValues = columnValues.replaceFirst(columnValues_arr[arrlen], "'"+Utilization+"'");
								  
							  }
						  }
						columnName =columnName.replace("OutStanding Balance","OutStanding_Balance");
						columnName =columnName.replace("LastUpdateDate","datelastupdated");
						columnName =columnName.replace("Total Amount","Total_Amount");
						columnName =columnName.replace("Payments Amount","Payments_Amount");
						columnName =columnName.replace("Overdue Amount","Overdue_Amount");
						 WriteLog( "inside parseHistoryUtilization" + columnName);
						 WriteLog( "inside parseHistoryUtilization" + columnValues);
						//sWhere="Wi_Name='"+parentWiName+"' AND AgreementId = '"+entry.getKey()+"' AND Child_Wi='"+wi_name+"'";
						sWhere="Wi_Name='"+parentWiName+"' AND AgreementId = '"+entry.getKey()+"'";
					}
					else if(sTableName.equalsIgnoreCase("ng_rlos_cust_extexpo_CardDetails")){
						String History = parseHistoryUtilization(parseXml, entry.getKey(), "CardDetails", "<History>", "</History>");
						History = History.replace("\n", "").replace("\r", "");
						String Utilization = parseHistoryUtilization(parseXml, entry.getKey(), "CardDetails", "<Utilizations24Months>", "</Utilizations24Months>");
						Utilization = Utilization.replace("\n", "").replace("\r", "");
						columnName = valueArr[0]+",Wi_Name,Request_Type,Product_Type,sub_product_type,CifId,Child_Wi,Liability_type";
						columnValues = valueArr[1]+",'"+parentWiName+"','"+returnType+"','"+prod+"','"+subprod+"','"+cifId+"','"+wi_name+"','"+cust_type+"'";
						 String columnName_arr[] = columnName.split(",");
						  String columnValues_arr[] = columnValues.split(",");
						  for(int arrlen=0;arrlen<columnName_arr.length;arrlen++){
							  if("CardType".equalsIgnoreCase(columnName_arr[arrlen])){
								  WriteLog( "inside loan desc tag name" + columnName_arr[arrlen]);
								  WriteLog( "inside loan desc tag value" + columnValues_arr[arrlen]);
								  String loan_desc = get_loanDesc(columnValues_arr[arrlen], cabinetName, sessionId, wrapperIP,wrapperPort, appServerType);
								  columnValues = columnValues.replaceFirst(columnValues_arr[arrlen], loan_desc);
								  
							  }
							  if("History".equalsIgnoreCase(columnName_arr[arrlen])){
									 // WriteLog( "inside loan desc tag name" + columnName_arr[arrlen]);
									 // WriteLog( "inside loan desc tag value" + columnValues_arr[arrlen]);
									  //String loan_desc = get_loanDesc(columnValues_arr[arrlen], cabinetName, sessionId, wrapperIP,wrapperPort, appServerType);
									  columnValues = columnValues.replace(columnValues_arr[arrlen], "'"+History+"'");
									  
								  }
								  if("Utilizations24Months".equalsIgnoreCase(columnName_arr[arrlen])){
									 // WriteLog( "inside loan desc tag name" + columnName_arr[arrlen]);
									 // WriteLog( "inside loan desc tag value" + columnValues_arr[arrlen]);
									  //String loan_desc = get_loanDesc(columnValues_arr[arrlen], cabinetName, sessionId, wrapperIP,wrapperPort, appServerType);
									  columnValues = columnValues.replace(columnValues_arr[arrlen], "'"+Utilization+"'");
									  
								  }
						  }
						 sWhere="Wi_Name='"+parentWiName+"' AND CardEmbossNum = '"+entry.getKey()+"'";
						//sWhere="Wi_Name='"+parentWiName+"' AND CardEmbossNum = '"+entry.getKey()+"' AND Child_Wi='"+wi_name+"'";
					}
					else if(sTableName.equalsIgnoreCase("NG_rlos_custexpose_Derived")){
					//Deepak 23 Dec changes done to save updated Rerport URL in DB.
						if("ExternalExposure".equalsIgnoreCase(returnType)){
							
							String sWhere_driveddelete = "Child_Wi='" + wi_name + "' AND Request_Type ='ExternalExposure'";
							String strInputXml_driveddelete = ExecuteQuery_APdelete("NG_RLOS_CUSTEXPOSE_Derived", sWhere, cabinetName,
									sessionId);
							WriteLog("strInputXml delete CUSTEXPOSE_Derived " + strInputXml_driveddelete);
							try {
								String strOutputXml_driveddelete = NGEjbClient.getSharedInstance().makeCall(wrapperIP,
										wrapperPort, appServerType, strInputXml_driveddelete);
								WriteLog("CustExpose_Output jsp: strOutputXml delete CUSTEXPOSE_Derived: "
										+ strOutputXml_driveddelete);
							} catch (NGException e) {
								e.printStackTrace();
							} catch (Exception ex) {
								ex.printStackTrace();
							}

						}
						columnName = valueArr[0]+",Wi_Name,Request_Type,CifId,FullNm,TotalOutstanding,TotalOverdue,NoOfContracts,ReportURL,Child_Wi,ReferenceNo,AECB_Score,Range";
						columnValues = valueArr[1]+",'"+parentWiName+"','"+returnType+"','"+cifId+"','"+FullNm+"','"+TotalOutstanding+"','"+TotalOverdue+"','"+NoOfContracts+"','"+ReportUrl+"','"+wi_name+"','"+referenceNo+"','"+Aecb_Score+"','"+range+"'";
						sWhere="Wi_Name='"+parentWiName+"' AND Request_Type = '"+returnType+"' and Child_Wi='"+wi_name+"' and cifid='"+cifId+"'";
					}
					//Changes Done to save data in NG_RLOS_CUSTEXPOSE_RecordDestribution table on 14th sept by Aman
					//Deepak Child workitem added in both columnName & columnValues to get it saved in backend - 8 July 2019.
					else if(sTableName.equalsIgnoreCase("NG_RLOS_CUSTEXPOSE_RecordDestribution")){
						columnName = valueArr[0]+",Wi_Name,Request_Type,CifId,Child_Wi";
						columnValues = valueArr[1]+",'"+parentWiName+"','"+returnType+"','"+cifId+"','"+wi_name+"'";
						sWhere="Wi_Name='"+parentWiName+"' AND ContractType = '"+entry.getKey()+"'and CifId='"+cifId+"'";
					}
					//Changes Done to save data in NG_RLOS_CUSTEXPOSE_RecordDestribution table on 14th sept by Aman
					//Deepak Child workitem added in both columnName & columnValues to get it saved in backend - 8 July 2019.
					else if(sTableName.equalsIgnoreCase("ng_rlos_cust_extexpo_AccountDetails")){
						columnName = valueArr[0]+",Wi_Name,Request_Type,CifId,Child_Wi";
						columnValues = valueArr[1]+",'"+parentWiName+"','"+returnType+"','"+cifId+"','"+wi_name+"'";
						String columnName_arr[] = columnName.split(",");
						  String columnValues_arr[] = columnValues.split(",");
						  for(int arrlen=0;arrlen<columnName_arr.length;arrlen++){
							  if("AcctType".equalsIgnoreCase(columnName_arr[arrlen])){
								  WriteLog( "inside loan desc tag name" + columnName_arr[arrlen]);
								  WriteLog( "inside loan desc tag value" + columnValues_arr[arrlen]);
								  String loan_desc = get_loanDesc(columnValues_arr[arrlen], cabinetName, sessionId, wrapperIP,wrapperPort, appServerType);
								  columnValues = columnValues.replaceFirst(columnValues_arr[arrlen], loan_desc);
								  break;
							  }
						  }
						sWhere="Wi_Name='"+parentWiName+"' AND AcctId = '"+entry.getKey()+"'";//Cif_id removed
					}
						//Deepak changes done for Service details
					   else if(sTableName.equalsIgnoreCase("ng_rlos_cust_extexpo_ServicesDetails")){
						  	  columnName = valueArr[0]+",Wi_Name,Request_Type,CifId,Child_Wi";
							  columnValues = valueArr[1]+",'"+parentWiName+"','"+returnType+"','"+cifId+"','"+wi_name+"'";
							  String columnName_arr[] = columnName.split(",");
							  String columnValues_arr[] = columnValues.split(",");
							 
						   for(int arrlen=0;arrlen<columnName_arr.length;arrlen++){
								  if("ServiceName".equalsIgnoreCase(columnName_arr[arrlen])){
									  WriteLog( "inside loan desc tag name" + columnName_arr[arrlen]);
									  WriteLog( "inside loan desc tag value" + columnValues_arr[arrlen]);
									  String loan_desc = get_loanDesc(columnValues_arr[arrlen], cabinetName, sessionId, wrapperIP,wrapperPort, appServerType);
									  columnValues = columnValues.replaceFirst(columnValues_arr[arrlen], loan_desc);
									  break;
								  }
						   }
							  sWhere="Wi_Name='"+parentWiName+"' AND ServiceID = '"+entry.getKey()+"'";
					  }
					//below changes Done to save AccountType in ng_RLOS_CUSTEXPOSE_AcctDetails table on 29th Dec by Disha
					else if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_AcctDetails")){
						String CreditGrade = (parseXml.contains("<CreditGrade>")) ? parseXml.substring(parseXml.indexOf("<CreditGrade>")+"</CreditGrade>".length()-1,parseXml.indexOf("</CreditGrade>")):"";
						//PCASP-2833 
						String isDirect = (parseXml.contains("<IsDirect>")) ? parseXml.substring(parseXml.indexOf("<IsDirect>")+"</IsDirect>".length()-1,parseXml.indexOf("</IsDirect>")):"";
						columnName = valueArr[0]+",Wi_Name,Request_Type,CifId,Child_Wi,CreditGrade,Account_Type,isDirect";
						columnValues = valueArr[1]+",'"+parentWiName+"','"+returnType+"','"+cifId+"','"+wi_name+"','"+CreditGrade+"','"+cust_type+"','"+isDirect+"'";
						sWhere="Request_Type='"+returnType+"' AND AcctId = '"+entry.getKey()+"' AND Child_Wi='"+wi_name+"' AND Account_Type = '"+cust_type+"'";
						String columnName_arr[] = columnName.split(",");
						  String columnValues_arr[] = columnValues.split(",");
						  String LimitSactionDate="";
						  for(int arrlen=0;arrlen<columnName_arr.length;arrlen++){
						  if("LimitSactionDate".equalsIgnoreCase(columnName_arr[arrlen])){
						  WriteLog( "inside LimitSactionDate tag name" + columnName_arr[arrlen]);
						 WriteLog( "inside LimitSactionDate value" + columnValues_arr[arrlen]);
						  LimitSactionDate = columnValues_arr[arrlen];
						  }
							  if("MonthsOnBook".equalsIgnoreCase(columnName_arr[arrlen])){
								  WriteLog( "inside MonthsOnBook tag name" + columnName_arr[arrlen]);
								  WriteLog( "inside MonthsOnBook value" + columnValues_arr[arrlen]);
								  if(!LimitSactionDate.equals(""))
								  {
								  String MOB = get_Mob_forOD(LimitSactionDate);
								  WriteLog( "inside MonthsOnBook value" + MOB);
								  if(!MOB.equalsIgnoreCase("Invalid"))
								  {
								  columnValues = columnValues.replace(columnValues_arr[arrlen], "'"+MOB+"'");
								  }
								  }
								  
							  }
							  }
						//change by saurabh on 24th Feb for skipping employer accounts to save.
						sQry="Select count(*) as selectdata from NG_RLOS_ALOC_OFFLINE_DATA where CIF_ID ='Nikhil123'";
						if(parseXml.contains("<LinkedCIFs>"))
						{
							parseLinkedCif(parseXml,sTableName,cifId,parentWiName,wi_name,entry.getKey(),cust_type,"Account",cabinetName,sessionId,wrapperIP,wrapperPort,appServerType);
						}
						//WriteLog( "sQry  loan sQry" + sQry);	  
					}
					else if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_InvestmentDetails")){
						
						columnName = valueArr[0]+",Wi_Name,Request_Type,CifId,Child_Wi";
						columnValues = valueArr[1]+",'"+parentWiName+"','"+returnType+"','"+cifId+"','"+wi_name+"'";
						sWhere="Request_Type='"+returnType+"' AND Child_Wi='"+wi_name+"' and InvestmentID='"+entry.getKey()+"'";
						  
					}
					//above changes Done to save AccountType in ng_RLOS_CUSTEXPOSE_AcctDetails table on 29th Dec by Disha
					  //Deepak 22 july 2019 new condition added to save custinfo
					  else if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_CustInfo")){
					  String isDirect = (parseXml.contains("<IsDirect>")) ? parseXml.substring(parseXml.indexOf("<IsDirect>")+"</IsDirect>".length()-1,parseXml.indexOf("</IsDirect>")):"";
						  columnName = valueArr[0]+",Wi_Name,Child_Wi,Request_Type,CifId,isDirect";
							columnValues = valueArr[1]+",'"+parentWiName+"','"+wi_name+"','"+returnType+"','"+cifId+"','"+isDirect+"'";
						   sWhere="Child_Wi='"+wi_name+"' AND Request_Type = '"+returnType+"' AND CifId = '"+cifId+"'";	  
						  }
					else{
						sWhere="Request_Type='"+returnType+"' AND Child_Wi='"+wi_name+"'";;
					}
	
					strInputXml =	ExecuteQuery_APUpdate(sTableName,columnName,columnValues,sWhere,cabinetName,sessionId);
					//WriteLog( "strInputXml update " + strInputXml);
					try 
					{
						strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);

						//WriteLog("CustExpose_Output jsp: strOutputXml update: "+strOutputXml);
					} 
					catch (NGException e) 
					{
						System.out.println("Exception occured in commonParseProduct: "+ e.getMessage());
						e.printStackTrace();
					} 
					catch (Exception ex) 
					{
						System.out.println("Exception occured in commonParseProduct: "+ ex.getMessage());
						ex.printStackTrace();
					}

					tagNameU = "APUpdate_Output";
					subTagNameU = "MainCode";
					subTagNameU_2 = "Output";
					mainCode = getTagValue(strOutputXml,tagNameU,subTagNameU);
					row_updated = getTagValue(strOutputXml,tagNameU,subTagNameU_2);
					//WriteLog("getTagValue select mainCode --> "+mainCode);
					//WriteLog("getTagValue select mainCode --> "+row_updated);
					if(!mainCode.equalsIgnoreCase("0") || row_updated.equalsIgnoreCase("0"))
					{	//WriteLog("sQry sQry sQry --> "+sQry);
						if (!sQry.equalsIgnoreCase("")){
							strInputXml =	ExecuteQuery_APSelect(sQry,cabinetName,sessionId);
							try 
							{
								strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);
								//WriteLog("CustExpose_Output jsp: strOutputXml ExecuteQuery_APSelect: "+strOutputXml);
							} 
							catch (NGException e) 
							{
								System.out.println("Exception occured in commonParseProduct: "+ e.getMessage());
								e.printStackTrace();
							} 
							catch (Exception ex) 
							{
								System.out.println("Exception occured in commonParseProduct: "+ ex.getMessage());
								ex.printStackTrace();
							}
							mainCode = (strOutputXml.contains("<MainCode>")) ? strOutputXml.substring(strOutputXml.indexOf("<MainCode>")+"</MainCode>".length()-1,strOutputXml.indexOf("</MainCode>")):"";
							//WriteLog("getTagValue select mainCode --> "+mainCode);
							selectdata=(strOutputXml.contains("<selectdata>")) ? strOutputXml.substring(strOutputXml.indexOf("<selectdata>")+"</selectdata>".length()-1,strOutputXml.indexOf("</selectdata>")):"";
							//WriteLog("getTagValue select selectdata --> "+selectdata);
						}
						if (!companyUpdateQuery.equalsIgnoreCase("")){
							strInputXml =	ExecuteQuery_APSelect(companyUpdateQuery,cabinetName,sessionId);
							try 
							{
								strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);
								//WriteLog("CustExpose_Output jsp: strOutputXml ExecuteQuery_APSelect: "+strOutputXml);
							} 
							catch (NGException e) 
							{
								System.out.println("Exception occured in commonParseProduct: "+ e.getMessage());
								e.printStackTrace();
							} 
							catch (Exception ex) 
							{
								System.out.println("Exception occured in commonParseProduct: "+ ex.getMessage());
								ex.printStackTrace();
							}

							mainCode = (strOutputXml.contains("<MainCode>")) ? strOutputXml.substring(strOutputXml.indexOf("<MainCode>")+"</MainCode>".length()-1,strOutputXml.indexOf("</MainCode>")):"";
							//WriteLog("getTagValue select mainCode --> "+mainCode);

							companiestobeUpdated=(strOutputXml.contains("<selectdata>")) ? strOutputXml.substring(strOutputXml.indexOf("<selectdata>")+"</selectdata>".length()-1,strOutputXml.indexOf("</selectdata>")):"";
							//WriteLog("getTagValue select companiestobeUpdated --> "+companiestobeUpdated);

							if(Integer.parseInt(companiestobeUpdated)>0){
								sWhere="Child_Wi='"+wi_name+"' AND CardEmbossNum = '"+entry.getKey()+"' And Liability_type ='Corporate_CIF'";
								strInputXml =	ExecuteQuery_APUpdate(sTableName,columnName,columnValues,sWhere,cabinetName,sessionId);
								//WriteLog( "strInputXml update " + strInputXml);
								try 
								{
									strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);

									//WriteLog("CustExpose_Output jsp: strOutputXml update: "+strOutputXml);
								} 
								catch (NGException e) 
								{
									System.out.println("Exception occured in commonParseProduct: "+ e.getMessage());
									e.printStackTrace();
								} 
								catch (Exception ex) 
								{
									System.out.println("Exception occured in commonParseProduct: "+ ex.getMessage());
									ex.printStackTrace();
								}

								tagNameU = "APUpdate_Output";
								subTagNameU = "MainCode";
								subTagNameU_2 = "Output";
								mainCode = getTagValue(strOutputXml,tagNameU,subTagNameU);
								//row_updated = getTagValue(strOutputXml,tagNameU,subTagNameU_2);
								////WriteLog("getTagValue select mainCode for update query for cif"+cifId+"--> "+mainCode);
								////WriteLog("getTagValue select rowUpdated for company for update query for cif"+cifId+" --> "+row_updated);
								stopIndividualToInsert = true;
							}
						}

						if(sQry.equalsIgnoreCase("") || (mainCode.equalsIgnoreCase("0") && selectdata.equalsIgnoreCase("0") && !stopIndividualToInsert)){
							//WriteLog("calling APInsert for cif --> "+cifId);
							//WriteLog("calling APInsert for table --> "+sTableName);
							//WriteLog("calling APInsert for cust_type --> "+cust_type);
							strInputXml =	ExecuteQuery_APInsert(sTableName,columnName,columnValues,cabinetName,sessionId);
							//WriteLog( "strInputXml" + strInputXml);
							try 
							{
								strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);

								//WriteLog("CustExpose_Output jsp: strOutputXml: "+strOutputXml);
								mainCode = getTagValue(strOutputXml,"APInsert_Output",subTagNameU);
								if(!mainCode.equalsIgnoreCase("0"))
								{
									retVal = "false";
									//WriteLog("CustExpose_Output jsp: commonparseproduct:false "+retVal);
								}
								else
								{
									retVal = "true";
									//WriteLog("CustExpose_Output jsp: commonparseproduct:true "+retVal);
								}
							} 
							catch (NGException e) 
							{
								System.out.println("Exception occured in commonParseProduct_1: "+ e.getMessage());
								e.printStackTrace();
							} 
							catch (Exception ex) 
							{
								System.out.println("Exception occured in commonParseProduct: "+ ex.getMessage());
								ex.printStackTrace();
							}
						}
						else{
							retVal = "true";
							//WriteLog("CustExpose_Output jsp: commonparseproductapupdate:true "+retVal);
						}
					}
					else
					{
						retVal = "true";
						//WriteLog("CustExpose_Output jsp: commonparseproductapupdate:true "+retVal);
					}

				}
				//WriteLog("CustExpose_Output jsp: finalValue: "+retVal);
				return retVal;
			} 
		}
		catch(Exception e){
			System.out.println("Exception occured in commonParseProduct: "+ e.getMessage());
			e.printStackTrace();
			retVal = "false";
		}
		return retVal;
	}
	
	public static void parseLinkedCif(String Xml,String TableName,String Main_CIF,String Wi_name, String Child_wi,String Agreement_id,String Cust_Type,String Liability_type,String cabinetName,String sessionId,String wrapperIP,String wrapperPort,String appServerType)
	{
		WriteLog("Inside parse CIF");
		WriteLog("Inside parse CIF:: Input_XMl"+Xml);
	
		try
		{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new InputSource(new StringReader(Xml)));
			doc.getDocumentElement().normalize();
			String Liabilityid = "";
			//String ParentTag= doc.getDocumentElement().getNodeName();
			NodeList nList;
			if("Account".equalsIgnoreCase(Liability_type))
			{
			nList = doc.getElementsByTagName("AcctDetails");
			Liabilityid="AcctId";
			}
			else if ("Loan".equalsIgnoreCase(Liability_type))
			{
				nList = doc.getElementsByTagName("LoanDetails");
				Liabilityid="AgreementId";
			}
			else 
			{
				nList = doc.getElementsByTagName("CardDetails");
				Liabilityid="CardEmbossNum";
			}
				
			WriteLog("Inside parse CIF:: nList.getLength()"+nList.getLength());
			   for (int temp = 0; temp < nList.getLength(); temp++) 
			   {
		            Node nNode = nList.item(temp);
		          //  System.out.println("\nCurrent Element :" + nNode.getNodeName());
		            
		            if (nNode.getNodeType() == Node.ELEMENT_NODE) 
		            {
		            
		             Element eElement = (Element) nNode;
		             String Liability_ID=eElement.getElementsByTagName(Liabilityid).item(0).getTextContent();
		             WriteLog("Inside parse CIF:: AcctId"+Liability_ID);
		             if(Liability_ID.equalsIgnoreCase(Agreement_id))
		             {
		            			             
		            	 NodeList Linked_CIF= eElement.getElementsByTagName("LinkedCIFs");
		            	 WriteLog("Inside parse CIF:: Linked_CIF.getLength()"+Linked_CIF.getLength());
		                 for (int temp1 = 0; temp1 < Linked_CIF.getLength(); temp1++)
		                    {
		                     Node node1 = Linked_CIF.item(temp1);
		                     if (node1.getNodeType() == Node.ELEMENT_NODE)
		                       { 
		                           Element eElement1 = (Element) node1;
		                           String Linked_CIF1= eElement1.getElementsByTagName("CIFId").item(0).getTextContent();
		                           String Relation1 = eElement1.getElementsByTagName("RelationType").item(0).getTextContent();
		                           WriteLog("Inside parse CIF:: Linked_CIF"+Linked_CIF1);
		     					  WriteLog("Inside parse CIF:: Relation"+Relation1);
		                    
		             /*  String Linked_CIF= eElement.getElementsByTagName("CIFId").item(0).getTextContent();
		              String Relation = eElement.getElementsByTagName("RelationType").item(0).getTextContent(); */
		              String SQuery = "select count(wi_name) as Select_Count from ng_rlos_custexpose_LinkedICF where Linked_CIFs='"+Linked_CIF1+"' and Relation='"+Relation1+"' and wi_name='"+Wi_name+"' and  child_wi='"+Child_wi+"' and Main_Cif='"+Main_CIF+"' and AgreementId='"+Agreement_id+"'";
		              String strInputXml =	ExecuteQuery_APSelect(SQuery,cabinetName,sessionId);
		              String strOutputXml="";
		              WriteLog("Inside parse CIF:: ExecuteQuery_APSelect"+strInputXml);
						try 
						{
							strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);
							//WriteLog("CustExpose_Output jsp: strOutputXml ExecuteQuery_APSelect: "+strOutputXml);
							WriteLog("Inside parse CIF:: ExecuteQuery_APSelect output"+strOutputXml);
						} 
						catch (Exception ex) 
						{
							System.out.println("Exception occured in commonParseProduct: "+ ex.getMessage());
							ex.printStackTrace();
						}
						String mainCode = (strOutputXml.contains("<MainCode>")) ? strOutputXml.substring(strOutputXml.indexOf("<MainCode>")+"</MainCode>".length()-1,strOutputXml.indexOf("</MainCode>")):"";
						//WriteLog("getTagValue select mainCode --> "+mainCode);
						WriteLog("Inside parse CIF select mainCode --> "+mainCode);
						if("0".equalsIgnoreCase(mainCode))
						{
						String selectdata=(strOutputXml.contains("<Select_Count>")) ? strOutputXml.substring(strOutputXml.indexOf("<Select_Count>")+"</Select_Count>".length()-1,strOutputXml.indexOf("</Select_Count>")):"";
						WriteLog("Inside parse CIF select selectdata --> "+selectdata);
						int totalretrieved=Integer.parseInt(selectdata);
						if(totalretrieved==0)
						{
							String sTableName="ng_rlos_custexpose_LinkedICF";
							String columnName="Wi_name,Child_wi,Linked_CIFs,Relation,AgreementId,Main_Cif,Liability_Type,Cust_Type";
							String columnValues="'"+Wi_name+"','"+Child_wi+"','"+Linked_CIF1+"','"+Relation1+"','"+Agreement_id+"','"+Main_CIF+"','"+Liability_type+"','"+Cust_Type+"'";
							strInputXml =	ExecuteQuery_APInsert(sTableName,columnName,columnValues,cabinetName,sessionId);
							//WriteLog( "strInputXml" + strInputXml);
							WriteLog( " Parse linked cif  strInputXml" + strInputXml);
							try 
							{
								strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);
								WriteLog( " Parse linked cif  strOutputXml" + strOutputXml);
								//WriteLog("CustExpose_Output jsp: strOutputXml: "+strOutputXml);
								mainCode = getTagValue(strOutputXml,"APInsert_Output","MainCode");
								
								
							}
							catch (Exception ex) 
							{
								WriteLog("Exception occured in parseCIF: "+ ex.getMessage());
								ex.printStackTrace();
							}
						}
						}
						//WriteLog("getTagValue select selectdata --> "+selectdata);
		             }
		            }
		             }
		            }
			   }
		}
		catch(Exception ex)
		{
			WriteLog("Exception occured in parse linked cif : "+ ex.getMessage());
			ex.printStackTrace();
		}
	}
	public static String parseHistoryUtilization(String Xml,String Agreement_id,String Liability_type,String StartType, String EndType)
	{
		WriteLog("Inside parse parseHistoryUtilization");
		WriteLog("Inside parse CIF:: Input_XMl" + Xml);
		WriteLog("Inside parse CIF:: Agreement_id" + Agreement_id);
		String Output_desired = "";

		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new InputSource(new StringReader(Xml)));
			doc.getDocumentElement().normalize();
			String Liabilityid = "";
			//String ParentTag= doc.getDocumentElement().getNodeName();
			NodeList nList;
			 if ("LoanDetails".equalsIgnoreCase(Liability_type)) {
				nList = doc.getElementsByTagName("LoanDetails");
				Liabilityid = "AgreementId";
			} else {
				nList = doc.getElementsByTagName("CardDetails");
				Liabilityid = "CardEmbossNum";
			}

			WriteLog("Inside parse CIF:: nList.getLength()" + nList.getLength());

					for (int temp = 0; temp < nList.getLength(); temp++) {
						Node nNode = nList.item(temp);
						  if (nNode.getNodeType() == Node.ELEMENT_NODE) 
				            {
				            
				             Element eElement = (Element) nNode;
							 
				             String Liability_ID=eElement.getElementsByTagName(Liabilityid).item(0).getTextContent();
				             WriteLog("Inside parse CIF:: AcctId"+Liability_ID);
				             if(Liability_ID.equalsIgnoreCase(Agreement_id))
				             {
								 WriteLog("Inside if block of parseHistoryUtilization");
						//  System.out.println("\nCurrent Element :" + nNode.getNodeName());
						WriteLog("Inside parse CIF:: ExecuteQuery_APSelect" + nodeToString(nNode));
						String Liability_aggregate = nodeToString(nNode);
						 //WriteLog("Liability_aggregate of parseHistoryUtilization "+Liability_aggregate);
						Output_desired = Liability_aggregate.substring(Liability_aggregate.indexOf(StartType),
								Liability_aggregate.lastIndexOf(EndType) + EndType.length());
						WriteLog("Output_desired of parseHistoryUtilization "+Output_desired);
					}
				}
			}
		
		}catch (Exception ex) {
			WriteLog("Exception occured in parse history Utilitixation cif : " + ex.getMessage());
			ex.printStackTrace();
		}
		return Output_desired;
	}
	
	public static String get_Mob_forOD(String LimitSactionDate)
	{
		try 
		{
	LimitSactionDate = LimitSactionDate.replaceAll("'", "");
	Date Current_date = new Date();
	Date Old_Date = new SimpleDateFormat("yyyy-MM-dd").parse(LimitSactionDate);
	int yy = Current_date.getYear()-Old_Date.getYear();
	int mm = Current_date.getMonth()-Old_Date.getMonth();
	if(mm<0)
	{
		yy--;
		mm = 12 - Old_Date.getMonth() + Current_date.getMonth();
		if(Current_date.getDate() < Old_Date.getDate())
		{
			mm--;
		}
	}
	else if (mm == 0 && Current_date.getDate() < Old_Date.getDate())
	{
		yy--;
		mm = 11 - Old_Date.getMonth() + Current_date.getMonth();
	}
	else if (mm > 0 && Current_date.getDate() < Old_Date.getDate())
	{
		mm--;
	}
	else if (Current_date.getDate() - Old_Date.getDate() !=0)
	{
		if(mm==12)
		{
			yy++;
			mm=0;
		}
	}
	
	return String.valueOf((yy*12)+mm);
	}
	catch (Exception ex)
		{
		WriteLog("Exception occured in get_Mob_forOD: "+ ex.getMessage());
		ex.printStackTrace();
		return "Invalid";
		}
	
	}

	public static String commonParseProduct_collection(String parseXml, String tagName, String wi_name,
			String returnType, String sTableName, String wrapperIP, String wrapperPort, String sessionId,
			String cabinetName, String appServerType, String subTagName, String prod, String subprod, String cifId,
			String parentWiName, String subtag_single, String cust_type, String CompanyCIF) {
		String retVal = "";
		try{
			if(!parseXml.contains(tagName)){
				return "true";
			}
			else
			{ 
				//WriteLog("commonParse jsp: inside: ");

				String [] valueArr= null;
				String strInputXml="";
				String strOutputXml="";
				String columnName = "";
				String columnValues = "";
				String tagNameU = "";
				String subTagNameU = "";
				String subTagNameU_2 = "";
				String mainCode = "";
				String sWhere = "";
				String row_updated = "";
				String 	sQry="";
				String selectdata="";
				String companyUpdateQuery="";
				String companiestobeUpdated = "";
				boolean stopIndividualToInsert = false;
				cifId=(parseXml.contains("<CustIdValue>")) ? parseXml.substring(parseXml.indexOf("<CustIdValue>")+"</CustIdValue>".length()-1,parseXml.indexOf("</CustIdValue>")):"";
				if(!CompanyCIF.equalsIgnoreCase("") && cifId.equalsIgnoreCase(CompanyCIF))
				{
					cust_type="Corporate_CIF";
				}
				else
				{
					cust_type="Individual_CIF";
				}
				//WriteLog("Cifid jsp: ReportUrl: "+cifId);
				//WriteLog("tagName jsp: commonParse: "+tagName);
				//WriteLog("subTagName jsp: commonParse: "+subTagName);


				Map<String, String> tagValuesMap= new LinkedHashMap<String, String>();		 
				tagValuesMap=getTagDataParent_deep(parseXml,tagName,subTagName,subtag_single);

				Map<String, String> map = tagValuesMap;
				//	String colValue="";
				for (Map.Entry<String, String> entry : map.entrySet())
				{
					valueArr=entry.getValue().split("~");
					//WriteLog( "tag values" + entry.getValue());


					//colValue = "'"+valueArr[1].replaceAll("[,]", "','")+"'";
					columnName = valueArr[0]+",Wi_Name,Request_Type,Product_Type,CardType,CifId";
					columnValues = valueArr[1]+",'"+wi_name+"','"+returnType+"','"+prod+"','"+subprod+"','"+cifId+"'";



					//WriteLog( "columnName commonParse" + columnName);
					//WriteLog( "columnValues commonParse" + columnValues);
					if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_CardDetails")){

						columnName = valueArr[0]+",Wi_Name,Request_Type,CifId,Child_Wi";
						columnName = columnName.replaceAll("Card_approve_date","ApplicationCreationDate");
						columnName = columnName.replaceAll("Outstanding_balance","OutstandingAmt");
						columnName = columnName.replaceAll("Credit_limit","CreditLimit");
						columnName = columnName.replaceAll("Overdue_amount","OverdueAmt");
						columnName = columnName.replaceAll("GeneralStatus","General_Status");
						columnValues = valueArr[1]+",'"+parentWiName+"','"+returnType+"','"+cifId+"','"+wi_name+"'";
						sWhere="Child_Wi='"+wi_name+"' AND CardEmbossNum = '"+entry.getKey()+"'";
						sQry="Select count(*) as selectdata from "+sTableName+" where Child_Wi='"+wi_name+"' And CardEmbossNum = '"+entry.getKey()+"' And Liability_type ='Individual_CIF'";
						if(cust_type.equalsIgnoreCase("Individual_CIF")) {
							companyUpdateQuery="Select count(*) as selectdata from "+sTableName+" where Child_Wi='"+wi_name+"' And CardEmbossNum = '"+entry.getKey()+"' And Liability_type ='Corporate_CIF'";
						}

					}
					else if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_LoanDetails")){
						columnName = valueArr[0]+",Wi_Name,Request_Type,Product_Type,CardType,CifId,Child_Wi";
						columnValues = valueArr[1]+",'"+parentWiName+"','"+returnType+"','"+prod+"','"+subprod+"','"+cifId+"','"+wi_name+"'";
						columnName = columnName.replaceAll("OutstandingAmt","TotalOutstandingAmt");
						columnName = columnName.replaceAll("Loan_close_date","LoanMaturityDate");
						columnName = columnName.replaceAll("GeneralStatus","General_Status");//Deepak code added to save value in General_Status for PCAS-1264 as it was mising in PL & CC And same was there in RLOS
						sWhere="Child_Wi='"+wi_name+"' AND AgreementId = '"+entry.getKey()+"'";
						sQry="Select count(*) as selectdata from "+sTableName+" where Child_Wi='"+wi_name+"' And AgreementId = '"+entry.getKey()+"' And Liability_type ='Individual_CIF'";
						if(cust_type.equalsIgnoreCase("Individual_CIF")) {
							companyUpdateQuery="Select count(*) as selectdata from "+sTableName+" where Child_Wi='"+wi_name+"' And AgreementId = '"+entry.getKey()+"' And Liability_type ='Corporate_CIF'";
						}
					}
					else if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_AcctDetails")){
						String CreditGrade = (parseXml.contains("<CreditGrade>")) ? parseXml.substring(parseXml.indexOf("<CreditGrade>")+"</CreditGrade>".length()-1,parseXml.indexOf("</CreditGrade>")):"";
						//PCASP-2833 
						//String isDirect = (parseXml.contains("<IsDirect>")) ? parseXml.substring(parseXml.indexOf("<IsDirect>")+"</IsDirect>".length()-1,parseXml.indexOf("</IsDirect>")):"";
						columnName = valueArr[0]+",Wi_Name,Request_Type,CifId,CreditGrade,Child_Wi";
						columnValues = valueArr[1]+",'"+parentWiName+"','"+returnType+"','"+cifId+"','"+CreditGrade+"','"+wi_name+"'";
						sWhere="Child_Wi='"+wi_name+"' AND AcctId = '"+entry.getKey()+"'";
						sQry="Select count(*) as selectdata from "+sTableName+" where Wi_Name='"+wi_name+"' And AcctId = '"+entry.getKey()+"' And Account_Type ='Individual_CIF'";
					}
					else{
						sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"'";
					}

					strInputXml =	ExecuteQuery_APUpdate(sTableName,columnName,columnValues,sWhere,cabinetName,sessionId);
					//WriteLog( "strInputXml update " + strInputXml);
					try 
					{
						strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);

						//WriteLog("CustExpose_Output jsp: strOutputXml update: "+strOutputXml);
					} 
					catch (NGException e) 
					{
						System.out.println("Exception occured in commonParseProduct_collection: "+ e.getMessage());
						e.printStackTrace();
					} 
					catch (Exception ex) 
					{
						System.out.println("Exception occured in commonParseProduct_collection: "+ ex.getMessage());
						ex.printStackTrace();
					}

					tagNameU = "APUpdate_Output";
					subTagNameU = "MainCode";
					subTagNameU_2 = "Output";
					mainCode = getTagValue(strOutputXml,tagNameU,subTagNameU);
					row_updated = getTagValue(strOutputXml,tagNameU,subTagNameU_2);
					//WriteLog("getTagValue select mainCode --> "+mainCode);
					//WriteLog("getTagValue select mainCode --> "+row_updated);
					if(!mainCode.equalsIgnoreCase("0") || row_updated.equalsIgnoreCase("0"))
					{	//WriteLog("sQry sQry sQry --> "+sQry);
						if (!sQry.equalsIgnoreCase("")){
							strInputXml =	ExecuteQuery_APSelect(sQry,cabinetName,sessionId);
							try 
							{
								strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);
								//WriteLog("CustExpose_Output jsp: strOutputXml ExecuteQuery_APSelect: "+strOutputXml);
							} 
							catch (NGException e) 
							{
								e.printStackTrace();
							} 
							catch (Exception ex) 
							{
								ex.printStackTrace();
							}
							mainCode = (strOutputXml.contains("<MainCode>")) ? strOutputXml.substring(strOutputXml.indexOf("<MainCode>")+"</MainCode>".length()-1,strOutputXml.indexOf("</MainCode>")):"";
							//WriteLog("getTagValue select mainCode --> "+mainCode);
							selectdata=(strOutputXml.contains("<selectdata>")) ? strOutputXml.substring(strOutputXml.indexOf("<selectdata>")+"</selectdata>".length()-1,strOutputXml.indexOf("</selectdata>")):"";
							//WriteLog("getTagValue select selectdata --> "+selectdata);
						}

						if (!companyUpdateQuery.equalsIgnoreCase("")){
							strInputXml =	ExecuteQuery_APSelect(companyUpdateQuery,cabinetName,sessionId);
							try 
							{
								strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);
								//WriteLog("CustExpose_Output jsp: strOutputXml ExecuteQuery_APSelect: "+strOutputXml);
							} 
							catch (NGException e) 
							{
								System.out.println("Exception occured in commonParseProduct_collection: "+ e.getMessage());
								e.printStackTrace();
							} 
							catch (Exception ex) 
							{
								System.out.println("Exception occured in commonParseProduct_collection: "+ ex.getMessage());
								ex.printStackTrace();
							}



							mainCode = (strOutputXml.contains("<MainCode>")) ? strOutputXml.substring(strOutputXml.indexOf("<MainCode>")+"</MainCode>".length()-1,strOutputXml.indexOf("</MainCode>")):"";
							//WriteLog("getTagValue select mainCode --> "+mainCode);

							companiestobeUpdated=(strOutputXml.contains("<selectdata>")) ? strOutputXml.substring(strOutputXml.indexOf("<selectdata>")+"</selectdata>".length()-1,strOutputXml.indexOf("</selectdata>")):"";
							//WriteLog("getTagValue select companiestobeUpdated --> "+companiestobeUpdated);

							if(Integer.parseInt(companiestobeUpdated)>0){
								if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_CardDetails")){
									sWhere="Child_Wi='"+wi_name+"' AND CardEmbossNum = '"+entry.getKey()+"' And Liability_type ='Corporate_CIF'";
								}
								else if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_LoanDetails")){
									sWhere="Child_Wi='"+wi_name+"' AND AgreementId = '"+entry.getKey()+"' And Liability_type ='Corporate_CIF'";
								}
								strInputXml =	ExecuteQuery_APUpdate(sTableName,columnName,columnValues,sWhere,cabinetName,sessionId);
								//WriteLog( "strInputXml update " + strInputXml);
								try 
								{
									strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);

									//WriteLog("CustExpose_Output jsp: strOutputXml update: "+strOutputXml);
								} 
								catch (NGException e) 
								{
									System.out.println("Exception occured in commonParseProduct_collection: "+ e.getMessage());
									e.printStackTrace();
								} 
								catch (Exception ex) 
								{
									System.out.println("Exception occured in commonParseProduct_collection: "+ ex.getMessage());
									ex.printStackTrace();
								}

								tagNameU = "APUpdate_Output";
								subTagNameU = "MainCode";
								subTagNameU_2 = "Output";
								mainCode = getTagValue(strOutputXml,tagNameU,subTagNameU);
								//row_updated = getTagValue(strOutputXml,tagNameU,subTagNameU_2);
								////WriteLog("getTagValue select mainCode for update query for cif"+cifId+"--> "+mainCode);
								////WriteLog("getTagValue select rowUpdated for company for update query for cif"+cifId+" --> "+row_updated);
								stopIndividualToInsert = true;
							}


						}

						if(sQry.equalsIgnoreCase("") || (mainCode.equalsIgnoreCase("0") && selectdata.equalsIgnoreCase("0") && !stopIndividualToInsert)){
							strInputXml =	ExecuteQuery_APInsert(sTableName,columnName,columnValues,cabinetName,sessionId);
							//WriteLog( "strInputXml" + strInputXml);
							try 
							{
								strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);

								//WriteLog("CustExpose_Output jsp: strOutputXml: "+strOutputXml);
								mainCode = getTagValue(strOutputXml,tagNameU,subTagNameU);
								if(!mainCode.equalsIgnoreCase("0"))
								{
									retVal = "false";
									//WriteLog("CustExpose_Output jsp: ApINsertfalse for collection summary: "+retVal);
								}
								else
								{
									retVal = "true";
									//WriteLog("CustExpose_Output jsp: ApINserttrue for collection summary: "+retVal);
								}
							} 
							catch (NGException e) 
							{
								System.out.println("Exception occured in commonParseProduct_collection: "+ e.getMessage());
								e.printStackTrace();
							} 
							catch (Exception ex) 
							{
								System.out.println("Exception occured in commonParseProduct_collection: "+ ex.getMessage());
								ex.printStackTrace();
							}
						}
						//change by saurabh for company call if its not able to overwrite individual data but call was successful so at frontend it should be successfull. Change on 2nd feb.
						else{
							retVal = "true";
						}
					} 
					else
					{
						retVal = "true";
						//WriteLog("CustExpose_Output jsp: ApUpdatetrue for collection summary: "+retVal);
					}
				}

				//WriteLog("CustExpose_Output jsp: final value for collection summary: "+retVal);
				return retVal;
			}

		}
		catch(Exception e){
			System.out.println("Exception occured in commonParseProduct_collection: "+ e.getMessage());
			e.printStackTrace();
			retVal = "false";
		}
		return retVal;
	}

	public static String commonParseParent(String parseXml,String tagName,String wi_name,String returnType,String sTableName,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String appServerType, String sParentTagName, String subTagName, String cifId, String parentWiName)
	{
		String retVal="";
		//WriteLog("commonParseParent jsp: inside: ");
		String [] valueArr= null;
		String strInputXml="";
		String strOutputXml="";
		String columnName = "";
		String columnValues = "";
		String sWhere = "";
		String tagNameU = "";
		String subTagNameU = "";
		String subTagNameU_2 = "";
		String mainCode = "";
		String row_updated = "";

		try{
			//WriteLog("tagName jsp: commonParseParent: "+tagName);
			//WriteLog("subTagName jsp: commonParseParent: "+subTagName);

			Map<Integer, String> tagValuesMap= new LinkedHashMap<Integer, String>();
			tagValuesMap=getTagDataParent(parseXml,tagName,subTagName);

			Map<Integer, String> map = tagValuesMap;
			String colValue ="";
			for (Map.Entry<Integer, String> entry : map.entrySet())
			{
				valueArr=entry.getValue().split("~");
				//WriteLog( "tag values" + entry.getValue());

				colValue = "'"+valueArr[1].replaceAll("[,]", "','")+"'";

				columnName = valueArr[0]+","+sParentTagName+",CifId,Child_Wi";
				columnValues = colValue+",'"+valueArr[2]+"','"+cifId+"','"+wi_name+"'";

				//WriteLog( "columnName commonParseParent" + columnName);
				//WriteLog( "columnValues commonParseParent" + columnValues);
				sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";

				strInputXml =	ExecuteQuery_APUpdate(sTableName,columnName,columnValues,sWhere,cabinetName,sessionId);
				//WriteLog( "strInputXml update " + strInputXml);
				try 
				{
					strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);

					//WriteLog("CustExpose_Output jsp: strOutputXml update: "+strOutputXml);
				} 
				catch (NGException e) 
				{
					e.printStackTrace();
				} 
				catch (Exception ex) 
				{
					ex.printStackTrace();
				}

				tagNameU = "APUpdate_Output";
				subTagNameU = "MainCode";
				subTagNameU_2 = "Output";
				mainCode = getTagValue(strOutputXml,tagNameU,subTagNameU);
				row_updated = getTagValue(strOutputXml,tagNameU,subTagNameU_2);
				//WriteLog("getTagValue select mainCode --> "+mainCode);
				if(!mainCode.equalsIgnoreCase("0") || row_updated.equalsIgnoreCase("0"))
				{	
					strInputXml =	ExecuteQuery_APInsert(sTableName,columnName,columnValues,cabinetName,sessionId);
					//WriteLog( "strInputXml" + strInputXml);
					try 
					{
						strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);

						//WriteLog("CustExpose_Output jsp: strOutputXml: "+strOutputXml);
						mainCode = getTagValue(strOutputXml,"APInsert_Output",subTagNameU);
						//WriteLog("maincode value for SIDTLS: "+mainCode);
						if(!mainCode.equalsIgnoreCase("0"))
						{
							retVal = "false";
							//WriteLog("CustExpose_Output jsp: commonparseparentinsert:false "+retVal);
						}					
						else
						{
							retVal = "true";
							//WriteLog("CustExpose_Output jsp: commonparseparentinsert:true "+retVal);
						}
					} 
					catch (NGException e) 
					{
						e.printStackTrace();
					} 
					catch (Exception ex) 
					{
						ex.printStackTrace();
					}
				}
				else
				{
					retVal = "true";
					//WriteLog("CustExpose_Output jsp: commonparseparentupdate:true "+retVal);
				}
			}
		}
		catch(Exception e){
			System.out.println("Exception occured in commonParseParent: "+ e.getMessage());
			e.printStackTrace();
			retVal = "false";
		}
		
		//WriteLog("CustExpose_Output jsp: final value of commonParseParent "+retVal);
		return retVal;
	}

	public static Map<Integer, String> getTagDataParent(String parseXml,String tagName,String subTagName)
	{
		Map<Integer, String> tagValuesMap= new LinkedHashMap<Integer, String>();
		InputStream is = new ByteArrayInputStream(parseXml.getBytes());		
		try {
			//WriteLog("getTagDataParent jsp: parseXml: "+parseXml);
			//WriteLog("getTagDataParent jsp: tagName: "+tagName);
			//WriteLog("getTagDataParent jsp: subTagName: "+subTagName);
			//InputStream is = new FileInputStream(parseXml);
		
			//WriteLog("getTagDataParent jsp: strOutputXml: "+is);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName(tagName);

			String[] values =subTagName.split(",");
			String value="";
			String subTagDerivedvalue="";
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					Node uNode=eElement.getParentNode();

					for(int j=0;j<values.length;j++){
						if(eElement.getElementsByTagName(values[j]).item(0) !=null){
							value=value+","+eElement.getElementsByTagName(values[j]).item(0).getTextContent();
							subTagDerivedvalue=subTagDerivedvalue+","+values[j];
						}

					}
					value=value.substring(1,value.length());
					subTagDerivedvalue=subTagDerivedvalue.substring(1,subTagDerivedvalue.length());

					Node nNode_c = doc.getElementsByTagName(uNode.getNodeName()).item(temp);
					Element eElement_agg = (Element) nNode_c;
					String id_val = "";
					if(uNode.getNodeName().equalsIgnoreCase("LoanDetails")){
						id_val = eElement_agg.getElementsByTagName("AgreementId").item(0).getTextContent();
					}
					else if(uNode.getNodeName().equalsIgnoreCase("CardDetails")){
						id_val = eElement_agg.getElementsByTagName("CardEmbossNum").item(0).getTextContent();
					}
					else if(uNode.getNodeName().equalsIgnoreCase("AcctDetails")){
						id_val = eElement_agg.getElementsByTagName("AcctId").item(0).getTextContent();
					}
					else{
						id_val="";
					}

					tagValuesMap.put(temp+1, subTagDerivedvalue+"~"+value+"~"+uNode.getNodeName()+"~"+id_val);
					value="";
					subTagDerivedvalue="";
				}
			}

		} catch (Exception e) {
			System.out.println("Exception occured in getTagDataParent"+e.getMessage());
			e.printStackTrace();
			//WriteLog("Exception occured in getTagDataParent method:  "+e.getMessage());
		}
				finally
			{
				try{
			    		if(is!=null)
			    		{
			    		is.close();
			    		is=null;
			    		}
			    	}
			    	catch(Exception e){
			    		WriteLog("Exception occured in is close:  "+e.getMessage());
			    	}
			}
		return tagValuesMap;
	}

	public static void commonParseParentKey(String parseXml,String tagName,String wi_name,String returnType,String sTableName,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String appServerType, String sParentTagName, String subTagName, String cifId, String parentWiName)
	{
		//WriteLog("commonParseParent jsp: inside: ");
		String [] valueArr= null;
		String sWhere = "";
		String columnName ="";
		String colValue ="";
		String keyDtType = "";
		String [] colArr= null;
		String detailType= null;
		//	String id_val = "";
		try{
			//WriteLog("tagName jsp: commonParseParent: "+tagName);
			//WriteLog("subTagName jsp: commonParseParent: "+subTagName);

			Map<Integer, String> tagValuesMap= new LinkedHashMap<Integer, String>();
			tagValuesMap=getTagDataParent(parseXml,tagName,subTagName);

			Map<Integer, String> map = tagValuesMap;

			for (Map.Entry<Integer, String> entry : map.entrySet())
			{
				valueArr=entry.getValue().split("~");
				//WriteLog( "tag values" + entry.getValue());
				//WriteLog( "valueArr[2]" + valueArr[2]);

				detailType = valueArr[2];
				//  id_val = valueArr[3];

				colArr = valueArr[1].split(",");
				//WriteLog( "colArr[0]" + colArr[0]);
				keyDtType = colArr[0];

				if(detailType.equalsIgnoreCase("LoanDetails"))
				{
					sTableName="ng_RLOS_CUSTEXPOSE_LoanDetails";
					if(returnType.equalsIgnoreCase("InternalExposure"))
					{
						if(keyDtType.equalsIgnoreCase("LoanApprovedDate"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";					
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
						if(keyDtType.equalsIgnoreCase("LoanMaturityDate"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
					}
					if(returnType.equalsIgnoreCase("ExternalExposure"))
					{
						if(keyDtType.equalsIgnoreCase("LoanApprovedDate"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
						if(keyDtType.equalsIgnoreCase("LoanMaturityDate"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
						if(keyDtType.equalsIgnoreCase("MaxOverdueAmountDate"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
					}
					if(returnType.equalsIgnoreCase("CollectionsSummary"))
					{
						if(keyDtType.equalsIgnoreCase("LoanApprovedDate"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
						if(keyDtType.equalsIgnoreCase("LoanMaturityDate"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}

					}
				}
				if(detailType.equalsIgnoreCase("CardDetails"))
				{
					sTableName="ng_RLOS_CUSTEXPOSE_CardDetails";
					if(returnType.equalsIgnoreCase("InternalExposure"))
					{
						if(keyDtType.equalsIgnoreCase("ApplicationCreationDate"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
						if(keyDtType.equalsIgnoreCase("ExpiryDate"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
					}
					if(returnType.equalsIgnoreCase("ExternalExposure"))
					{
						if(keyDtType.equalsIgnoreCase("MaxOverDueAmountDate"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
					}
				}
				if(detailType.equalsIgnoreCase("AcctDetails"))
				{
					sTableName="ng_RLOS_CUSTEXPOSE_AcctDetails";
					if(returnType.equalsIgnoreCase("InternalExposure"))
					{
						if(keyDtType.equalsIgnoreCase("AccountOpenDate"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
						if(keyDtType.equalsIgnoreCase("LimitSactionDate"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
						if(keyDtType.equalsIgnoreCase("LimitExpiryDate"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
					}
					if(returnType.equalsIgnoreCase("ExternalExposure"))
					{
						if(keyDtType.equalsIgnoreCase("StartDate"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
						if(keyDtType.equalsIgnoreCase("ClosedDate"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
						if(keyDtType.equalsIgnoreCase("MaxOverdueAmtDate"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
					}
				}
				if(detailType.equalsIgnoreCase("ServicesDetails"))
				{
					sTableName="NG_RLOS_CUSTEXPOSE_ServicesDetails";
					if(returnType.equalsIgnoreCase("ExternalExposure"))
					{
						if(keyDtType.equalsIgnoreCase("SubscriptionDt"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
						if(keyDtType.equalsIgnoreCase("SvcExpDt"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);

						}
					}

				}		  			  

			}
		}
		catch(Exception e){
			System.out.println("Exception occured in commonParseParentKey: "+e.getMessage());
			e.printStackTrace();
		}
	}

	public static void commonParseParentAmt(String parseXml,String tagName,String wi_name,String returnType,String sTableName,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String appServerType, String sParentTagName, String subTagName, String cifId, String parentWiName)
	{
		//WriteLog("commonParseParent jsp: inside: ");
		String [] valueArr= null;
		String sWhere = "";
		String columnName ="";
		String colValue ="";
		String keyDtType = "";
		String [] colArr= null;
		String detailType= null;
		String id_val = ""; 

		try{
			//WriteLog("tagName jsp: commonParseParent: "+tagName);
			//WriteLog("subTagName jsp: commonParseParent: "+subTagName);

			Map<Integer, String> tagValuesMap= new LinkedHashMap<Integer, String>();
			tagValuesMap=getTagDataParent(parseXml,tagName,subTagName);

			Map<Integer, String> map = tagValuesMap;

			for (Map.Entry<Integer, String> entry : map.entrySet())
			{
				valueArr=entry.getValue().split("~");
				//WriteLog( "tag values" + entry.getValue());
				//WriteLog( "valueArr[2]" + valueArr[2]);

				detailType = valueArr[2];
				id_val = valueArr[3];

				colArr = valueArr[1].split(",");
				//WriteLog( "colArr[0]" + colArr[0]);
				keyDtType = colArr[0];

				if(detailType.equalsIgnoreCase("LoanDetails"))
				{
					sTableName="ng_RLOS_CUSTEXPOSE_LoanDetails";
					if(returnType.equalsIgnoreCase("InternalExposure"))
					{
						if(keyDtType.equalsIgnoreCase("TotalLoanAmount"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
						if(keyDtType.equalsIgnoreCase("LoanDisbursementAmt"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
						if(keyDtType.equalsIgnoreCase("TotalOutstandingAmt"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
						if(keyDtType.equalsIgnoreCase("OverdueAmt"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
						if(keyDtType.equalsIgnoreCase("MortgagesValue"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
						if(keyDtType.equalsIgnoreCase("NextInstallmentAmt"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
					}
					if(returnType.equalsIgnoreCase("ExternalExposure"))
					{
						if(keyDtType.equalsIgnoreCase("TotalAmt"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
						if(keyDtType.equalsIgnoreCase("OutstandingAmt"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
						if(keyDtType.equalsIgnoreCase("PaymentsAmt"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
						if(keyDtType.equalsIgnoreCase("OverdueAmt"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
						if(keyDtType.equalsIgnoreCase("MaximumOvrdueAmt"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
					}
					if(returnType.equalsIgnoreCase("CollectionsSummary"))
					{
						if(keyDtType.equalsIgnoreCase("OutstandingAmt"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
						if(keyDtType.equalsIgnoreCase("ProductAmt"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
						if(keyDtType.equalsIgnoreCase("NextInstallmentAmt"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
						if(keyDtType.equalsIgnoreCase("OverdueAmt"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}

					}
				}
				if(detailType.equalsIgnoreCase("CardDetails"))
				{
					sTableName="ng_RLOS_CUSTEXPOSE_CardDetails";
					if(returnType.equalsIgnoreCase("InternalExposure"))
					{
						if(keyDtType.equalsIgnoreCase("CreditLimit"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
						if(keyDtType.equalsIgnoreCase("OutstandingAmt"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
						if(keyDtType.equalsIgnoreCase("OverdueAmt"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
						if(keyDtType.equalsIgnoreCase("CurrMaxUtil"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
					}
					if(returnType.equalsIgnoreCase("ExternalExposure"))
					{
						if(keyDtType.equalsIgnoreCase("CurrentBalance"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
						if(keyDtType.equalsIgnoreCase("CashLimit"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
						if(keyDtType.equalsIgnoreCase("OverdueAmount"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
						if(keyDtType.equalsIgnoreCase("MaxOverdueAmount"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
					}
				}
				if(detailType.equalsIgnoreCase("AcctDetails"))
				{
					sTableName="ng_RLOS_CUSTEXPOSE_AcctDetails";
					if(returnType.equalsIgnoreCase("InternalExposure"))
					{
						if(keyDtType.equalsIgnoreCase("AvailableBalance"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
						if(keyDtType.equalsIgnoreCase("ClearBalanceAmount"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
						if(keyDtType.equalsIgnoreCase("LedgerBalance"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
						if(keyDtType.equalsIgnoreCase("LoanDisbursementAmount"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
						if(keyDtType.equalsIgnoreCase("OverdueAmount"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
						if(keyDtType.equalsIgnoreCase("EffectiveAvailableBalance"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
						if(keyDtType.equalsIgnoreCase("CumulativeDebitAmount"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
						if(keyDtType.equalsIgnoreCase("CurOutstandingAmount"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
					}
					if(returnType.equalsIgnoreCase("ExternalExposure"))
					{
						if(keyDtType.equalsIgnoreCase("CashLimit"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
						if(keyDtType.equalsIgnoreCase("OverdueAmount"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"' AND AcctId = "+id_val;
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
						if(keyDtType.equalsIgnoreCase("MaxOverdueAmount"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"' AND AcctId = "+id_val;
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
					}
				}
				if(detailType.equalsIgnoreCase("ServicesDetails"))
				{
					sTableName="NG_RLOS_CUSTEXPOSE_ServicesDetails";
					if(returnType.equalsIgnoreCase("ExternalExposure"))
					{
						if(keyDtType.equalsIgnoreCase("OverDue"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
					}
				}		  			  
			}
		}
		catch(Exception e){
			System.out.println("Exception occured :"+e.getMessage());
			e.printStackTrace();
		}
	}

	public static void commonParseParentDelinquency(String parseXml,String tagName,String wi_name,String returnType,String sTableName,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String appServerType, String sParentTagName, String subTagName, String cifId, String parentWiName)
	{
		//WriteLog("commonParseParent jsp: inside: ");
		String [] valueArr= null;
		String sWhere = "";
		String columnName ="";
		String colValue ="";
		String keyDtType = "";
		String [] colArr= null;
		String detailType= null;

		try{
			//WriteLog("tagName jsp: commonParseParent: "+tagName);
			//WriteLog("subTagName jsp: commonParseParent: "+subTagName);

			Map<Integer, String> tagValuesMap= new LinkedHashMap<Integer, String>();
			tagValuesMap=getTagDataParent(parseXml,tagName,subTagName);

			Map<Integer, String> map = tagValuesMap;

			for (Map.Entry<Integer, String> entry : map.entrySet())
			{
				valueArr=entry.getValue().split("~");
				//WriteLog( "tag values" + entry.getValue());
				//WriteLog( "valueArr[2]" + valueArr[2]);

				detailType = valueArr[2];

				colArr = valueArr[1].split(",");
				//WriteLog( "colArr[0]" + colArr[0]);
				keyDtType = colArr[0];

				if(detailType.equalsIgnoreCase("CardDetails"))
				{
					sTableName="ng_RLOS_CUSTEXPOSE_CardDetails";
					if(returnType.equalsIgnoreCase("ExternalExposure"))
					{
						if(keyDtType.equalsIgnoreCase("DPD30Last6Months"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
						if(keyDtType.equalsIgnoreCase("DPD60Last18Months"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}

					}
				}
				if(detailType.equalsIgnoreCase("AcctDetails"))
				{
					sTableName="ng_RLOS_CUSTEXPOSE_AcctDetails";
					if(returnType.equalsIgnoreCase("InternalExposure"))
					{
						if(keyDtType.equalsIgnoreCase("DaysPastDue"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}

					}
					if(returnType.equalsIgnoreCase("ExternalExposure"))
					{
						if(keyDtType.equalsIgnoreCase("DPD30Last6Months"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}
						if(keyDtType.equalsIgnoreCase("DPD60Last18Months"))
						{
							sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
							columnName = colArr[0]+",Child_Wi";
							colValue = "'"+colArr[1]+"','"+wi_name+"'";
							//WriteLog( "valueArr[2] sWhere" + sWhere);
							//WriteLog( "colValue[2]" + colValue);
							updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
						}

					}
				}					  			  

			}
		}
		catch(Exception e){
			System.out.println("Exception occured commonParseParentDelinquency :"+e.getMessage());
			e.printStackTrace();
		}
		
	}

	public static String ExecuteQuery_APInsert(String tableName,String columnName,String strValues, String cabinetName, String sessionId)
	{
		String sInputXML = "<?xml version=\"1.0\"?>" +"\n"+
		"<APInsert_Input>" +"\n"+
		"<Option>APInsert</Option>" +"\n"+
		"<TableName>" + tableName + "</TableName>" +"\n"+
		"<ColName>" + columnName + "</ColName>" +"\n"+
		"<Values>" + strValues + "</Values>" +"\n"+
		"<EngineName>" + cabinetName + "</EngineName>" +"\n"+
		"<SessionId>" + sessionId + "</SessionId>" +"\n"+
		"</APInsert_Input>";
		return sInputXML;	
	}

	public static String ExecuteQuery_APdelete(String tableName,String sWhere, String cabinetName, String sessionId)
	{
		String sInputXML = "<?xml version=\"1.0\"?>"+
		"<APDelete_Input><Option>APDelete</Option>"+
		"<TableName>"+tableName+"</TableName>"+
		"<WhereClause>"+sWhere+"</WhereClause>"+
		"<EngineName>"+cabinetName+"</EngineName>"+
		"<SessionId>"+sessionId+"</SessionId>"+
		"</APDelete_Input>";
		return sInputXML;	
	}

	public static String ExecuteQuery_APUpdate(String tableName,String columnName,String strValues,String sWhere, String cabinetName, String sessionId)
	{
		String sInputXML = "<?xml version=\"1.0\"?>"+
		"<APUpdate_Input><Option>APUpdate</Option>"+
		"<TableName>"+tableName+"</TableName>"+
		"<ColName>"+columnName+"</ColName>"+
		"<Values>"+strValues+"</Values>"+
		"<WhereClause>"+sWhere+"</WhereClause>"+
		"<EngineName>"+cabinetName+"</EngineName>"+
		"<SessionId>"+sessionId+"</SessionId>"+
		"</APUpdate_Input>";
		return sInputXML;	
	}
public static String ExecuteQuery_APSelectwithparam(String sQry,String params, String cabinetName, String sessionId)
{
	String sInputXML = "<?xml version='1.0'?><APSelectWithNamedParam_Input>"
			+ "<option>APSelectWithNamedParam</option>" + "<Query>" + sQry + "</Query>" + "<Params>" + params
			+ "</Params>" + "<EngineName>" + cabinetName + "</EngineName>" + "<SessionID>" + sessionId
			+ "</SessionID>" + "</APSelectWithNamedParam_Input>";
	return sInputXML;
}
	public static String ExecuteQuery_APSelect(String sQry, String cabinetName, String sessionId)
	{
		String sInputXML = "<?xml version=\"1.0\"?>"+
		"<WFSelectWithColumnNames_Input>"+
		"<option>APSelectWithColumnNames</option>"+
		"<EngineName>"+cabinetName+"</EngineName>"+
		"<SessionID>"+sessionId+"</SessionID>"+
		"<Query>"+sQry+"</Query>"+
		"</WFSelectWithColumnNames_Input>";
		return sInputXML;	
	}
	public static String getTagValue(String parseXml,String tagName,String subTagName)
	{
		//WriteLog("getTagValue jsp: inside: ");
		String [] valueArr= null;
		String mainCodeValue = "";

		//WriteLog("tagName jsp: getTagValue: "+tagName);
		//WriteLog("subTagName jsp: getTagValue: "+subTagName);

		try{
			Map<Integer, String> tagValuesMap= new LinkedHashMap<Integer, String>();		 
			tagValuesMap=getTagDataParent(parseXml,tagName,subTagName);

			Map<Integer, String> map = tagValuesMap;
			for (Map.Entry<Integer, String> entry : map.entrySet())
			{
				valueArr=entry.getValue().split("~");
				//WriteLog( "tag values" + entry.getValue());
				mainCodeValue = valueArr[1];	
				//WriteLog( "mainCodeValue" + mainCodeValue);
			}
		}
		catch(Exception e){
			System.out.println("Exception occured getTagValue: "+e.getMessage());
			e.printStackTrace();
		}
		return mainCodeValue;
	}

	public static void updateQuery(String sTableName,String columnName,String colValue,String sWhere,String cabinetName,String sessionId,String returnType,String wrapperIP,String wrapperPort, String appServerType,String cifId,String wi_name, String parentWiName)
	{
		String strInputXml="";
		String strOutputXml="";
		String mainCode="";
		String tagNameU="";
		String subTagNameU="";
		String subTagNameU_2="";
		String columnValues="";
		String row_updated="";
		try{
			strInputXml =	ExecuteQuery_APUpdate(sTableName,columnName,colValue,sWhere,cabinetName,sessionId);
			//WriteLog( "strInputXml update " + strInputXml);
			try 
			{
				strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);

				//WriteLog("CustExpose_Output jsp: strOutputXml update: "+strOutputXml);
			} 
			catch (NGException e) 
			{
				e.printStackTrace();
			} 
			catch (Exception ex) 
			{
				ex.printStackTrace();
			}

			tagNameU = "APUpdate_Output";
			subTagNameU = "MainCode";
			subTagNameU_2 = "Output";
			mainCode = getTagValue(strOutputXml,tagNameU,subTagNameU);
			row_updated = getTagValue(strOutputXml,tagNameU,subTagNameU_2);
			//WriteLog("getTagValue select mainCode --> "+mainCode);
			if(!mainCode.equalsIgnoreCase("0") || row_updated.equalsIgnoreCase("0"))
			{
				//colValue = "'"+valueArr[1].replaceAll("[,]", "','")+"'";

				columnName = columnName+",Wi_Name,Request_Type,CifId,Child_Wi";
				columnValues = colValue+",'"+parentWiName+"','"+returnType+"','"+cifId+"','"+wi_name+"'";

				strInputXml =	ExecuteQuery_APInsert(sTableName,columnName,columnValues,cabinetName,sessionId);
				//WriteLog( "strInputXml insert " + strInputXml);
				try 
				{
					strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);

					//WriteLog("CustExpose_Output jsp: strOutputXml insert: "+strOutputXml);
				} 
				catch (NGException e) 
				{
					e.printStackTrace();
				} 
				catch (Exception ex) 
				{
					ex.printStackTrace();
				}
			}
		}
		catch(Exception e){
			System.out.println("Exception occured updateQuery: "+e.getMessage());
			e.printStackTrace();
		}
	}
	//Deepak code commented method changed with new subtag_single param 23jan2018
	public static Map<String, String> getTagDataParent_deep(String parseXml,String tagName,String sub_tag,String subtag_single){

		Map<String, String> tagValuesMap= new LinkedHashMap<String, String>(); 
		InputStream is = new ByteArrayInputStream(parseXml.getBytes());
		try {
			//WriteLog("getTagDataParent_deep jsp: parseXml: "+parseXml);
			//WriteLog("getTagDataParent_deep jsp: tagName: "+tagName);
			//WriteLog("getTagDataParent_deep jsp: subTagName: "+sub_tag);
			String tag_notused = "BankId,OperationDesc,TxnSummary,#text";

			
			//WriteLog("getTagDataParent_deep jsp: strOutputXml: "+is);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();

			NodeList nList_loan = doc.getElementsByTagName(tagName);
			for(int i = 0 ; i<nList_loan.getLength();i++){
				String col_name = "";
				String col_val ="";
				NodeList ch_nodeList = nList_loan.item(i).getChildNodes();
				String id ="";
				if("ReturnsDtls".equalsIgnoreCase(tagName)){
					id = ch_nodeList.item(1).getTextContent();
				}
				else if("SalDetails".equalsIgnoreCase(tagName)){
					id = ch_nodeList.item(0).getTextContent()+i;
				}
				else if("ServicesDetails".equalsIgnoreCase(tagName)){
					id = ch_nodeList.item(1).getTextContent();
				}
				else if("InvestmentDetails".equalsIgnoreCase(tagName)){
					id = ch_nodeList.item(1).getTextContent();
				}
				else if("ChequeDetails".equalsIgnoreCase(tagName)){
					id = ch_nodeList.item(1).getTextContent();
				}
				else{
					id = ch_nodeList.item(0).getTextContent();
				}
				//String id = ch_nodeList.item(0).getTextContent();
				for(int ch_len = 0 ;ch_len< ch_nodeList.getLength(); ch_len++){
					if(sub_tag.contains(ch_nodeList.item(ch_len).getNodeName())){
						NodeList sub_ch_nodeList =  ch_nodeList.item(ch_len).getChildNodes();
						if(!sub_ch_nodeList.item(0).getTextContent().equalsIgnoreCase("#text")){
							if(col_name.equalsIgnoreCase("")){
								col_name = sub_ch_nodeList.item(0).getTextContent();
								col_val = "'"+sub_ch_nodeList.item(1).getTextContent()+"'";
							}
							else if(!col_name.contains(sub_ch_nodeList.item(0).getTextContent())){
								col_name = col_name+","+sub_ch_nodeList.item(0).getTextContent();
								col_val = col_val+",'"+sub_ch_nodeList.item(1).getTextContent()+"'";
							}
						}	

					}
					else if(tag_notused.contains(ch_nodeList.item(ch_len).getNodeName())){
						//WriteLog("this tag not to be passed: "+ch_nodeList.item(ch_len).getNodeName());
					}
					else if(subtag_single.contains(ch_nodeList.item(ch_len).getNodeName())){
						NodeList sub_ch_nodeList =  ch_nodeList.item(ch_len).getChildNodes();
						if(!sub_ch_nodeList.item(0).getTextContent().equalsIgnoreCase("#text")){
							for(int sub_chd_len=0;sub_chd_len<sub_ch_nodeList.getLength();sub_chd_len++){
								if(col_name.equalsIgnoreCase("")){
									col_name = sub_ch_nodeList.item(sub_chd_len).getNodeName();
									col_val = "'"+sub_ch_nodeList.item(sub_chd_len).getTextContent()+"'";
								}
								else if(!col_name.contains(sub_ch_nodeList.item(0).getTextContent())){
									col_name = col_name+","+sub_ch_nodeList.item(sub_chd_len).getNodeName();
									col_val = col_val+",'"+sub_ch_nodeList.item(sub_chd_len).getTextContent()+"'";
								}
							}
						}
					}
					else{
						if(col_name.equalsIgnoreCase("")){
							col_name = ch_nodeList.item(ch_len).getNodeName();
							col_val = "'"+ch_nodeList.item(ch_len).getTextContent()+"'";
						}
						else if(!col_name.contains(ch_nodeList.item(ch_len).getNodeName())){
							col_name = col_name+","+ch_nodeList.item(ch_len).getNodeName();
							col_val = col_val+",'"+ch_nodeList.item(ch_len).getTextContent()+"'";
						}

					}

				}
				//WriteLog("insert/update for id: "+id);
				//WriteLog("insert/update cal_name: "+col_name);
				//WriteLog("insert/update col_val: "+col_val);
				if(!col_name.equalsIgnoreCase(""))
					tagValuesMap.put(id, col_name+"~"+col_val);	
			}

		} catch (Exception e) {
			System.out.println("Exception occured in getTagDataParent_deep: "+e.getMessage());
			e.printStackTrace();
			//WriteLog("Exception occured in getTagDataParent method:  "+e.getMessage());
		}
				finally
			{
				try{
			    		if(is!=null)
			    		{
			    		is.close();
			    		is=null;
			    		}
			    	}
			    	catch(Exception e){
			    		WriteLog("Exception occured in is close:  "+e.getMessage());
			    	}
			}
		return tagValuesMap;
	}

	public static String getTagDataParent_financ_header(String parseXml,String tagName,String sub_tag){
		String col_name = "";
		String col_val ="";
		InputStream is = new ByteArrayInputStream(parseXml.getBytes());
		try {
			//WriteLog("getTagDataParent_financ_header jsp: parseXml: "+parseXml);
			//WriteLog("getTagDataParent_financ_header jsp: tagName: "+tagName);
			//WriteLog("getTagDataParent_financ_header jsp: subTagName: "+sub_tag);

			//InputStream is = new FileInputStream(parseXml);
			
			//WriteLog("getTagDataParent_financ_header jsp: strOutputXml: "+is);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();

			NodeList nList_loan = doc.getElementsByTagName(tagName);
			for(int i = 0 ; i<nList_loan.getLength();i++){

				NodeList ch_nodeList = nList_loan.item(i).getChildNodes();
				String id = ch_nodeList.item(0).getTextContent();
				for(int ch_len = 0 ;ch_len< ch_nodeList.getLength(); ch_len++){
					if(sub_tag.toUpperCase().contains(ch_nodeList.item(ch_len).getNodeName().toUpperCase())){
						if(col_name.equalsIgnoreCase("")){
							col_name = ch_nodeList.item(ch_len).getNodeName();
							col_val = "'"+ch_nodeList.item(ch_len).getTextContent()+"'";
						}
						else{
							col_name = col_name+","+ch_nodeList.item(ch_len).getNodeName();
							col_val = col_val+",'"+ch_nodeList.item(ch_len).getTextContent()+"'";
						}
					}							
				}
				//WriteLog("insert/update getTagDataParent_financ_header for id: "+id);
				//WriteLog("insert/update getTagDataParent_financ_header cal_name: "+col_name);
				//WriteLog("insert/update getTagDataParent_financ_header col_val: "+col_val);

			}

		} catch (Exception e) {
			System.out.println("Exception occured in getTagDataParent_financ_header: "+e.getMessage());
			e.printStackTrace();
			//WriteLog("Exception occured in getTagDataParent_financ_header method:  "+e.getMessage());
		}
				finally
			{
				try{
			    		if(is!=null)
			    		{
			    		is.close();
			    		is=null;
			    		}
			    	}
			    	catch(Exception e){
			    		WriteLog("Exception occured in is close:  "+e.getMessage());
			    	}
			}
		return col_name+":"+col_val;
	}
	public static String getTagDataParent_cardInstallment_header(String parseXml,String tagName,String sub_tag){
		String col_name = "";
		String col_val ="";
		InputStream is = new ByteArrayInputStream(parseXml.getBytes());
		try {
			//WriteLog("getTagDataParent_cardInstallment_header jsp: parseXml: "+parseXml);
			//WriteLog("getTagDataParent_cardInstallment_header jsp: tagName: "+tagName);
			//WriteLog("getTagDataParent_cardInstallment_header jsp: subTagName: "+sub_tag);

			//InputStream is = new FileInputStream(parseXml);
			
			//WriteLog("getTagDataParent_cardInstallment_header jsp: strOutputXml: "+is);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();

			NodeList nList_loan = doc.getElementsByTagName(tagName);
			for(int i = 0 ; i<nList_loan.getLength();i++){

				NodeList ch_nodeList = nList_loan.item(i).getChildNodes();
				String id = ch_nodeList.item(0).getTextContent();
				for(int ch_len = 0 ;ch_len< ch_nodeList.getLength(); ch_len++){
					if(sub_tag.toUpperCase().contains(ch_nodeList.item(ch_len).getNodeName().toUpperCase())){
						if(col_name.equalsIgnoreCase("")){
							col_name = ch_nodeList.item(ch_len).getNodeName();
							col_val = "'"+ch_nodeList.item(ch_len).getTextContent()+"'";
						}
						else{
							col_name = col_name+","+ch_nodeList.item(ch_len).getNodeName();
							col_val = col_val+",'"+ch_nodeList.item(ch_len).getTextContent()+"'";
						}
					}							
				}
				//WriteLog("insert/update getTagDataParent_cardInstallment_header for id: "+id);
				//WriteLog("insert/update getTagDataParent_cardInstallment_header cal_name: "+col_name);
				//WriteLog("insert/update getTagDataParent_cardInstallment_header col_val: "+col_val);

			}

		} catch (Exception e) {
			System.out.println("Exception occured in getTagDataParent_cardInstallment_header: "+e.getMessage());
			e.printStackTrace();
			//WriteLog("Exception occured in getTagDataParent_cardInstallment_header method:  "+e.getMessage());
		}
				finally
			{
				try{
			    		if(is!=null)
			    		{
			    		is.close();
			    		is=null;
			    		}
			    	}
			    	catch(Exception e){
			    		WriteLog("Exception occured in is close:  "+e.getMessage());
			    	}
			}
		return col_name+":"+col_val;
	}
	public static String get_loanDesc(String loan_code,String cabinetName,String sessionId,String wrapperIP,String wrapperPort,String appServerType ){
		String loan_desc="";
		try{
			String str_Loandesc = "select Description from NG_MASTER_contract_type with(nolock) where code =:code";
			String params="code=="+loan_code.replace("'", "");
			String strInputXml=ExecuteQuery_APSelectwithparam(str_Loandesc,params,cabinetName,sessionId);
			String strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);
			WriteLog("inside get_loanDesc strOutputXml:  "+strOutputXml);
			String Maincode = strOutputXml.substring(strOutputXml.indexOf("<MainCode>")+"</MainCode>".length()-1,strOutputXml.indexOf("</MainCode>"));
			if("0".equalsIgnoreCase(Maincode)){
				loan_desc=strOutputXml.substring(strOutputXml.indexOf("<Description>")+"</Description>".length()-1,strOutputXml.indexOf("</Description>"));	
			}else{
				loan_desc = loan_code;
			}
		}
		catch(Exception e){
			WriteLog("Exception occured in get_loanDesc:  "+e.getMessage());
			loan_desc = loan_code;
		}
		return "'"+loan_desc+"'";
	}

	public static String nodeToString(Node node) {
		StringWriter sw = new StringWriter();
		try {
			Transformer t = TransformerFactory.newInstance().newTransformer();
			t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			t.setOutputProperty(OutputKeys.INDENT, "yes");
			t.transform(new DOMSource(node), new StreamResult(sw));
		} catch (TransformerException te) {
			System.out.println("nodeToString Transformer Exception");
		}
		return sw.toString();
	}%>