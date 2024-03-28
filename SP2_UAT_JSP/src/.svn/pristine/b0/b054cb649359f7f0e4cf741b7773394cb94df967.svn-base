<!------------------------------------------------------------------------------------------------------
//Name:Aman Shukla
//Changes: To save Data in RecordDestribution table 
//Date:14th Sept
//---------------------------------------------------------------------------------------------------->
<%@ include file="Log.process"%>
<jsp:useBean id="wDSession" class="com.newgen.wfdesktop.session.WDSession" scope="session"/>
<jsp:useBean id="WDCabinet" class="com.newgen.wfdesktop.baseclasses.WDCabinetInfo" scope="session"/>
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


<script language="javascript" src="/webdesktop/custom/CustomJSP/workdesk.js"></script>

<%
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader ("Expires", 0);
	String result = request.getParameter("result");
	String wi_name = request.getParameter("wi_name");
	String prod = request.getParameter("prod");
	String subprod = request.getParameter("subprod");
	String cifId = request.getParameter("cifId").trim();
	String is_mobility = request.getParameter("is_mobility");
	String sessionId = request.getParameter("SeesionId");
	String row_count = request.getParameter("row_count"); 
	String CardNumber = request.getParameter("CardNumber");
	String cabinetName = "";
	String wrapperIP = "";
	String wrapperPort = "";
	String appServerType = "";
	String cust_type = request.getParameter("cust_type");
	//WriteLog("Integration jsp: request_name: in custexpose_output.jsp");

	try {
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
		out.print("false"+"@#"+row_count);
		//WriteLog("Excetion Occured while reading from properties file: "+e.getMessage());
	}
	
	//WriteLog("CustExpose_Output jsp: is_mobility: "+is_mobility);
	//WriteLog("subprod jsp: cust_type: "+cust_type);
	String outputXMLMsg = "";
	String flagOp="";
		try
		{	
			String process_tablename="NG_RLOS_XMLLOG_HISTORY";
			String squery = "select OUTPUT_XML from "+process_tablename+" with (nolock) where MESSAGE_ID ='"+result+"' and WI_NAME = '"+wi_name+"'";
			String strInputXml_1 = ExecuteQuery_APSelect(squery,cabinetName,sessionId);
			String strOutputXml_1 = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml_1);
			WriteLog("Out put XML of "+result+" : "+strOutputXml_1+"and row_count"+row_count);
			if(!"".equalsIgnoreCase(strOutputXml_1)){
				String row_count_str = strOutputXml_1.substring(strOutputXml_1.indexOf("<TotalRetrieved>")+16,strOutputXml_1.indexOf("</TotalRetrieved>"));
				int result_count = Integer.parseInt(row_count_str);
				if (result_count>0){
					if(strOutputXml_1.indexOf("<MQ_RESPONSE_XML>")>-1)
					{
						outputXMLMsg=strOutputXml_1.substring(strOutputXml_1.indexOf("<MQ_RESPONSE_XML>")+17,strOutputXml_1.indexOf("</MQ_RESPONSE_XML>"));
						
						cifId =cifId.trim();
					 flagOp = getOutputXMLValues(outputXMLMsg,appServerType,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,prod,subprod,cifId,CardNumber,cust_type);
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

	public static String getOutputXMLValues(String parseXml,String appServerType,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name, String prod, String subprod, String cifId,String CardNumber,String cust_type)
	{
		String outputXMLHead = "";
        String outputXMLMsg = "";
        String returnDesc = "";
        String returnCode = "";
        String response= "";
        String returnType="";
		String result_str = "";
		String MsgFormat="";
	
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
			if(parseXml.indexOf("<RequestType>")>-1)
			{
				returnType= parseXml.substring(parseXml.indexOf("<RequestType>")+13,parseXml.indexOf("</RequestType>"));
				//Deepak changes done for Commented for PCSP-526 
				if("0000".equalsIgnoreCase(returnCode) || ("ExternalExposure".equalsIgnoreCase(returnType) && "B003".equalsIgnoreCase(returnCode)))
				{	
					if(returnType.equalsIgnoreCase("InternalExposure"))
					{
						result_str=parseInternalExposure(returnType,parseXml,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,appServerType,prod,subprod,cifId,cust_type);
					}
					else if(returnType.equalsIgnoreCase("ExternalExposure"))
					{
						result_str=parseExternalExposure(returnType,parseXml,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,appServerType,prod,subprod,cifId,cust_type);
					}
					else if(returnType.equalsIgnoreCase("CollectionsSummary"))
					{
						result_str=parseCollectionSummary(returnType,parseXml,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,appServerType,prod,subprod,cifId,cust_type);
					}
				}

				if(!"0000".equalsIgnoreCase(returnCode)){
					String errorQuery="SELECT isnull((SELECT alert FROM ng_MASTER_INTEGRATION_ERROR_CODE WHERE  error_code='"+returnCode+"'),(SELECT alert FROM ng_MASTER_INTEGRATION_ERROR_CODE WHERE error_code='DEFAULT')) As Alert";
					String strInputXml = ExecuteQuery_APSelect(errorQuery,cabinetName,sessionId);
					String strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);
					result_str=strOutputXml.substring(strOutputXml.indexOf("<Alert>")+"</Alert>".length()-1,strOutputXml.indexOf("</Alert>"));
					WriteLog("Return code is not 0000 so result_str: "+result_str);
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
				
					/* String errorQuery="SELECT isnull((SELECT alert FROM ng_MASTER_INTEGRATION_ERROR_CODE WHERE Integration_call='"+returnType+"' AND error_code='"+ReturnCode+"'),(SELECT alert FROM ng_MASTER_INTEGRATION_ERROR_CODE WHERE  Integration_call='"+returnType+"' AND error_code='DEFAULT'))"; */
					String errorQuery="SELECT isnull((SELECT alert FROM ng_MASTER_INTEGRATION_ERROR_CODE WHERE  error_code='"+returnCode+"'),(SELECT alert FROM ng_MASTER_INTEGRATION_ERROR_CODE WHERE error_code='DEFAULT')) As Alert";
					//WriteLog("@@@@@@@@@@@@@@  "+errorQuery);
					String strInputXml = ExecuteQuery_APSelect(errorQuery,cabinetName,sessionId);
					//WriteLog("@@@@@@@@@@@@@@  "+strInputXml);
					String strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);
					//WriteLog("@@@@@@@@@@@@@@  "+strOutputXml);
					result_str=strOutputXml.substring(strOutputXml.indexOf("<Alert>")+"</Alert>".length()-1,strOutputXml.indexOf("</Alert>"));
					//WriteLog("@@@@@@@@@@@@@@  "+result_str); 
					return result_str;
				}
				if(returnType.equalsIgnoreCase("CARD_INSTALLMENT_DETAILS"))
				{
					//WriteLog("cifId jsp: getOutputXMLValuesresult:CardInstallmentDetailsFlag inside card installment123");
					result_str=parseCardInstallmentsDetails(returnType,parseXml,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,appServerType,prod,subprod,cifId,CardNumber);
						//WriteLog("cifId jsp: getOutputXMLValuesresult:CardInstallmentDetailsFlag "+result_str);
				}
			}
			//ended
			
				if(parseXml.indexOf("<OperationType>")>-1)
			{
				returnType= parseXml.substring(parseXml.indexOf("<OperationType>")+15,parseXml.indexOf("</OperationType>"));
				//WriteLog("$$returnTypereturnTypereturnTypereturnType "+returnType);
				//Added By Prabhakar
			if(!"0000".equalsIgnoreCase(returnCode))
				{	
				
					/* String errorQuery="SELECT isnull((SELECT alert FROM ng_MASTER_INTEGRATION_ERROR_CODE WHERE Integration_call='"+returnType+"' AND error_code='"+ReturnCode+"'),(SELECT alert FROM ng_MASTER_INTEGRATION_ERROR_CODE WHERE  Integration_call='"+returnType+"' AND error_code='DEFAULT'))"; */
					String errorQuery="SELECT isnull((SELECT alert FROM ng_MASTER_INTEGRATION_ERROR_CODE WHERE  error_code='"+returnCode+"'),(SELECT alert FROM ng_MASTER_INTEGRATION_ERROR_CODE WHERE error_code='DEFAULT')) As Alert";
					//WriteLog("@@@@@@@@@@@@@@  "+errorQuery);
					String strInputXml = ExecuteQuery_APSelect(errorQuery,cabinetName,sessionId);
					//WriteLog("@@@@@@@@@@@@@@  "+strInputXml);
					String strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);
					//WriteLog("@@@@@@@@@@@@@@  "+strOutputXml);
					result_str=strOutputXml.substring(strOutputXml.indexOf("<Alert>")+"</Alert>".length()-1,strOutputXml.indexOf("</Alert>"));
					//WriteLog("@@@@@@@@@@@@@@  "+result_str); 
					return result_str;
				}
				
				if(returnType.equalsIgnoreCase("AVGBALDET"))
				{
					result_str=parseAVGBALDET(returnType,parseXml,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,appServerType,cifId);
				}
				else if(returnType.equalsIgnoreCase("RETURNDET"))
				{
					result_str=parseRETURNDET(returnType,parseXml,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,appServerType,cifId);
				}
				else if(returnType.equalsIgnoreCase("LIENDET"))
				{
					result_str=parseLIENDET(returnType,parseXml,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,appServerType,cifId);
				}
				else if(returnType.equalsIgnoreCase("SIDET"))
				{
					result_str=parseSIDET(returnType,parseXml,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,appServerType,cifId);
				}
				else if(returnType.equalsIgnoreCase("SALDET"))
				{
					result_str=parseSALDET(returnType,parseXml,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,appServerType,cifId);
				}
				
				else if(returnType.equalsIgnoreCase("TRANSUM"))
				{
					result_str=parseTRANSUM(returnType,parseXml,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,appServerType,cifId);
				}
				WriteLog("$$result_str "+result_str);
			}
			
				returnType= parseXml.substring(parseXml.indexOf("<MsgFormat>")+11,parseXml.indexOf("</MsgFormat>"));
				WriteLog("$$returnType result_strresult_strresult_str"+returnType);
				WriteLog("$$MsgFormat "+returnType);
				if(returnType.equalsIgnoreCase("FINANCIAL_SUMMARY") &&(result_str.equalsIgnoreCase(""))){
					result_str=returnCode;
					WriteLog("$$result_str result_strresult_strresult_str"+result_str);
				}
				if(returnType.equalsIgnoreCase("CUSTOMER_EXPOSURE") &&(result_str.equalsIgnoreCase(""))){
					result_str=returnCode;
					WriteLog("$$result_str result_strresult_strresult_str"+result_str);
				}
				WriteLog("$$result_str saurabh"+result_str);
			
			
		}
		catch(Exception e)
        {     
			System.out.println("Exception occured in getOutputXMLValues method: "+ e.getMessage());
			e.printStackTrace();
			WriteLog("Exception occured in getOutputXMLValues method:  "+e.getMessage());
			result_str ="Failure";
        }
		
		return (result_str);
	}

%>
<%!
	public static String parseInternalExposure(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String prod, String subprod, String cifId,String cust_type)
	{
		String flag1="";
		
		String tagName="";
		String subTagName="";
		String sTableName="";
		String sParentTagName="";
		String result="";
		//Deepak code commented method changed with new subtag_single param 23jan2018
		String subtag_single="";
		InputStream is = new ByteArrayInputStream(parseXml.getBytes());
	    WriteLog("getTagDataParent_deep jsp: strOutputXml: "+is);
	    try{
	    	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();
		
			NodeList nList_loan = doc.getElementsByTagName("CustomerExposureResponse");
			
			 
			for(int i = 0 ; i<nList_loan.getLength();i++){
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
				 tagName="LoanDetails"; 
					subTagName = "KeyDt,AmountDtls,DelinquencyInfo";
					sTableName="ng_RLOS_CUSTEXPOSE_LoanDetails";
					subtag_single="";
					flag1=commonParseProduct(n_parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,cust_type,subtag_single);
					
					if(flag1.equalsIgnoreCase("true")){
					
						tagName="CardDetails";
						subTagName = "KeyDt,AmountDtls,DelinquencyInfo";
						sTableName="ng_RLOS_CUSTEXPOSE_CardDetails";
						subtag_single="";
						flag1=commonParseProduct(n_parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,cust_type,subtag_single);
						
						if(flag1.equalsIgnoreCase("true")){
							tagName="AcctDetails";
							subTagName = "KeyDt,AmountDtls,DelinquencyInfo";
							sTableName="ng_RLOS_CUSTEXPOSE_AcctDetails";
							subtag_single="ODDetails";
							flag1=commonParseProduct(n_parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,cust_type,subtag_single);
							
								if(flag1.equalsIgnoreCase("true")){
								tagName="Derived";
								subTagName = "";
								sTableName="NG_rlos_custexpose_Derived";
								subtag_single="";
								flag1=commonParseProduct(n_parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,cust_type,subtag_single);
										if(flag1.equalsIgnoreCase("true")){
											tagName="RecordDestribution";
											subTagName = "";
											sTableName="NG_RLOS_CUSTEXPOSE_RecordDestribution";
											subtag_single="";
											flag1=commonParseProduct(n_parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,cust_type,subtag_single);
											  //Deepak 22 july 2019 new condition added to save custinfo	
												if(flag1.equalsIgnoreCase("true")){
													tagName="CustInfo";
													subTagName = "";
													sTableName="ng_RLOS_CUSTEXPOSE_CustInfo";
													subtag_single="";
													flag1=commonParseProduct(n_parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,cust_type,subtag_single);
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
	public static String parseExternalExposure(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String prod, String subprod, String cifId,String cust_type)
	{
		String ReturnCode="";
		
		if(parseXml.indexOf("<ReturnCode>")>-1)
		{
			ReturnCode= parseXml.substring(parseXml.indexOf("<ReturnCode>")+12,parseXml.indexOf("</ReturnCode>"));
			WriteLog("$$return Code "+ReturnCode);
		}
		
		//Commented for PCSP-526 
		/* if(ReturnCode.equalsIgnoreCase("B003"))
		{
			WriteLog("AECB:No record found!!");
			return "B003";
		} */
		
		String flag1="";
		
		String tagName="";
		String subTagName="";
		String sTableName="";
		String sParentTagName="";
		String result="";
		//Deepak code commented method changed with new subtag_single param 23jan2018
		String subtag_single="";
		
		tagName="ChequeDetails"; 
		subTagName = "";
		sTableName="ng_rlos_cust_extexpo_ChequeDetails";
		flag1 = commonParseProduct(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,cust_type,subtag_single);
		WriteLog("cifId jsp: parseExternalExposure: updated or inserted"+flag1);
		
		
		if(flag1.equalsIgnoreCase("true")){
			tagName="LoanDetails"; 
			subTagName = "KeyDt,AmountDtls";
			sTableName="ng_rlos_cust_extexpo_LoanDetails";
			flag1 = commonParseProduct(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,cust_type,subtag_single);
			WriteLog("cifId jsp: parseExternalExposure: updated or inserted1"+flag1);
			
			if(flag1.equalsIgnoreCase("true")){
				tagName="CardDetails"; 
				subTagName = "KeyDt,AmountDtls,DelinquencyInfo";
				sTableName="ng_rlos_cust_extexpo_CardDetails";
				flag1 = commonParseProduct(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,cust_type,subtag_single);	
				WriteLog("cifId jsp: parseExternalExposure: updated or inserted2"+flag1);
				
					if(flag1.equalsIgnoreCase("true")){
					tagName="Derived"; 
					subTagName = "";
					sTableName="NG_rlos_custexpose_Derived";
					flag1 = commonParseProduct(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,cust_type,subtag_single);	
					WriteLog("cifId jsp: parseExternalExposure: updated or NG_rlos_custexpose_Derived"+flag1);
								if(flag1.equalsIgnoreCase("true")){
								tagName="RecordDestribution";
								subTagName = "";
								sTableName="NG_RLOS_CUSTEXPOSE_RecordDestribution";
								flag1=commonParseProduct(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,cust_type,subtag_single);
									if(flag1.equalsIgnoreCase("true")){
										tagName="AcctDetails";
										subTagName = "KeyDt,AmountDtls";
										sTableName="ng_rlos_cust_extexpo_AccountDetails";
										flag1=commonParseProduct(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,cust_type,subtag_single);
											if(flag1.equalsIgnoreCase("true")){
												tagName="ServicesDetails";
												subTagName = "KeyDt,AmountDtls";
												sTableName="ng_rlos_cust_extexpo_ServicesDetails";
												flag1=commonParseProduct(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,cust_type,subtag_single);
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
						WriteLog("cifId jsp: parseExternalExposure: updated or NG_rlos_custexpose_Derived"+flag1);
						}
			}
			else{
				flag1 ="false";
				WriteLog("cifId jsp: parseExternalExposure: updated or insertedfalse"+flag1);
			}
		
			
		}
		else{
			flag1 ="false";
			WriteLog("cifId jsp: parseExternalExposure: updated or insertedfalse1"+flag1);
		}
		WriteLog("cifId jsp: parseExternalExposure: updated or inserted final value"+flag1);
	return flag1;
	}
	
	public static String parseCollectionSummary(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String prod, String subprod, String cifId,String cust_type)
	{
		String tagName="";
		String subTagName="";
		String sTableName="";
		String sParentTagName="";
		String result="";
		String flag1="";
		//Deepak code commented method changed with new subtag_single param 23jan2018
		String subtag_single="";
		
		
		tagName="LoanDetails"; 
		subTagName = "KeyDt,AmountDtls,DelinquencyInfo";
		sTableName="ng_RLOS_CUSTEXPOSE_LoanDetails";
		flag1=commonParseProduct_collection(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,subtag_single,cust_type);
		
		if(flag1.equalsIgnoreCase("true")){
		tagName="CardDetails";
		subTagName = "KeyDt,AmountDtls,DelinquencyInfo";
		sTableName="ng_RLOS_CUSTEXPOSE_CardDetails";
		flag1=commonParseProduct_collection(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,subtag_single,cust_type);
					if(flag1.equalsIgnoreCase("true")){
					tagName="Derived";
					subTagName = "";
					sTableName="NG_rlos_custexpose_Derived";
					flag1=commonParseProduct(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,cust_type,subtag_single);
					
					}
					else{
						flag1="false";
					}
		}
		else{
			flag1="false";
		}
		return flag1;
	}
	//added
		public static String parseCardInstallmentsDetails(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String prod, String subprod, String cifId,String CardNumber)
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
		
		
		tagName="TransactionDetailsRec"; 
		subTagName = "";
		sTableName="ng_RLOS_CUSTEXPOSE_CardInstallmentDetails";
		flag1=commonParseFinance_CardInstallment(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,CardNumber,subtag_single);
		
		//WriteLog("wrapperIP jsp: CardInstallmentDetailsResponse: "+flag1);
		
	
		//WriteLog("wrapperIP jsp: CardInstallmentDetailsResponse final value: "+flag1);
		return flag1;
	}
	//ended
	public static String parseTRANSUM(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String cifId)
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
		
		tagName= "TxnSummaryDtls";		
		subTagName= "";
		sTableName="ng_rlos_FinancialSummary_TxnSummary";
		//commonParseFinance(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName);
		flag1=commonParseFinance(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,subtag_single);
		if(flag1.equalsIgnoreCase("true")){
			flag1="true";
		}
		else{
			flag1="false";
		}
	return flag1;
	}
	public static String parseAVGBALDET(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String cifId)
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
		
		tagName= "FinancialSummaryRes";		
		subTagName= "AvgBalanceDtls";
		sTableName="ng_rlos_FinancialSummary_AvgBalanceDtls";
		//commonParseFinance(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName);
		flag1=commonParseFinance(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,subtag_single);
		if(flag1.equalsIgnoreCase("true")){
			flag1="true";
		}
		else{
			flag1="false";
		}
	return flag1;
	
	}
	public static String parseRETURNDET(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String cifId)
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
		
		tagName= "ReturnsDtls";		
		subTagName= "";
		sTableName="ng_rlos_FinancialSummary_ReturnsDtls";
		//commonParseFinance(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName);
		flag1=commonParseFinance(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,subtag_single);
		if(flag1.equalsIgnoreCase("true")){
			flag1="true";
		}
		else{
			flag1="false";
		}
	return flag1;
	
	}
	public static String parseLIENDET(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String cifId)
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
		//commonParseFinance(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName);
		flag1=commonParseFinance(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,subtag_single);

		if(flag1.equalsIgnoreCase("true")){
			flag1="true";
		}
		else{
			flag1="false";
		}
	return flag1;
		
	}
	public static String parseSIDET(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String cifId)
	{
		//WriteLog("wrapperIP jsp: parseSIDET: "+wrapperIP);
		//WriteLog("wrapperPort jsp: parseSIDET: "+wrapperPort);
		//WriteLog("sessionId jsp: parseSIDET: "+sessionId);
		//WriteLog("cabinetName jsp: parseSIDET: "+cabinetName);
		//WriteLog("wi_name jsp: parseSIDET: "+wi_name);
		//WriteLog("appServerType jsp: parseSIDET: "+appServerType);
		//WriteLog("parseXml jsp: parseSIDET: "+parseXml);
		//WriteLog("returnType jsp: parseSIDET: "+returnType);
		//WriteLog("cifId jsp: parseSIDET: "+cifId);
		
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
		//commonParseFinance(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName);
		flag1=commonParseFinance(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,subtag_single);
		if(flag1.equalsIgnoreCase("true")){
				flag1="true";
			}
			else{
				flag1="false";
			}
	return flag1;
	
	}
	public static String parseSALDET(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String cifId)
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
		//commonParseFinance(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName);
		flag1=commonParseFinance(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,subtag_single);
		//WriteLog("return flag1 jsp: parseSALDET: "+flag1);
		return flag1;
	}
	
	
	
	public static String commonParse(String parseXml,String tagName,String wi_name,String returnType,String sTableName,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String appServerType, String subTagName, String cifId)
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
			  columnName = valueArr[0]+",Wi_Name,Request_Type,CifId";
			  columnValues = colValue+",'"+wi_name+"','"+returnType+"','"+cifId+"'";
			   
			  
			   
			  //WriteLog( "columnName commonParse" + columnName);
			  //WriteLog( "columnValues commonParse" + columnValues);
			  String[] columnValues_arr = columnValues.split(",");
			  if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_CardDetails")){
				   sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND CardEmbossNum = "+columnValues_arr[0]+" AND Liability_type='Individual_CIF'";
			  }
			  else if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_LoanDetails")){
				  sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+columnValues_arr[0]+" AND Liability_type='Individual_CIF'";
			  }
				  else{
			   sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND Liability_type='Individual_CIF'";
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
		   //WriteLog("CustExpose_Output jsp: commonParse true for final return value: "+retVal);
		  return retVal;
		 
	}
	
	//added 12/07/2017
	
		public static String commonParseFinance_CardInstallment(String parseXml,String tagName,String wi_name,String returnType,String sTableName,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String appServerType, String subTagName,String CardNumber,String subtag_single)
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
				  
				  columnName = valueArr[0]+",Wi_Name,Request_Type,"+header_info_arr[0];
				  columnValues = valueArr[1]+",'"+wi_name+"','CARD_INSTALLMENT_DETAILS',"+header_info_arr[1];
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
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND TxnSerialNum = "+txnNum+"";
				
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
			return retVal;
	}
	
	
	//ended 12/07/2017
		public static String commonParseFinance(String parseXml,String tagName,String wi_name,String returnType,String sTableName,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String appServerType, String subTagName,String subtag_single)
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
		//WriteLog("tagName jsp: commonParseFinance: "+tagName);
		//WriteLog("subTagName jsp: commonParseFinance: "+subTagName);
		//WriteLog("sTableName jsp: commonParseFinance: "+sTableName);
		
		
		if((returnType.equalsIgnoreCase("RETURNDET")&& parseXml.contains("ReturnsDtls"))||(returnType.equalsIgnoreCase("AVGBALDET")&& parseXml.contains("AcctId"))||(returnType.equalsIgnoreCase("LIENDET")&& parseXml.contains("LienDetails"))||(returnType.equalsIgnoreCase("SIDET")&& parseXml.contains("SIDetails"))||(returnType.equalsIgnoreCase("TRANSUM")&& parseXml.contains("TxnSummary"))||(returnType.equalsIgnoreCase("SALDET")&& parseXml.contains("SalDetails")))
		{
			
		//WriteLog("returnType jsp: commonParseFinance: "+returnType);
		 Map<String, String> tagValuesMap= new LinkedHashMap<String, String>();		 
		 tagValuesMap=getTagDataParent_deep(parseXml,tagName,subTagName, subtag_single);
		
		//WriteLog("Inside commonParseFinance-->tagValuesMap of column and values: "+tagValuesMap);

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
				  columnName = valueArr[0]+",Wi_Name";
				  columnValues = valueArr[1]+",'"+wi_name+"'";
				  sWhere="Wi_Name='"+wi_name+"' AND OperationType='"+returnType+"' AND AcctId = "+id;
			  }
			  else if(sTableName.equalsIgnoreCase("ng_rlos_FinancialSummary_ReturnsDtls")){
				  //WriteLog( "Inside commonParseFinance for ng_rlos_FinancialSummary_ReturnsDtls");
				  String header_info = getTagDataParent_financ_header(parseXml,"FinancialSummaryRes","CIFID,AcctId,OperationType");
				  //WriteLog( "Inside commonParseFinance for ng_rlos_FinancialSummary_ReturnsDtls header info: "+ header_info);
				  String [] header_info_arr = header_info.split(":");
				  columnName = valueArr[0]+",Wi_Name,"+header_info_arr[0];
				  columnValues = valueArr[1]+",'"+wi_name+"',"+header_info_arr[1];
				  String columnName_arr[] = columnName.split(",");
				  String columnValues_arr[] = columnValues.split(",");
				   if(returnType.equalsIgnoreCase("RETURNDET") && valueArr[0].contains("ReturnNumber")){
					id = columnValues_arr[Arrays.asList(columnName_arr).indexOf("ReturnNumber")];
					sWhere="Wi_Name='"+wi_name+"' AND OperationType='"+returnType+"' AND ReturnNumber = "+id;
				  }else{
					  id = columnValues_arr[Arrays.asList(columnName_arr).indexOf("AcctId")];
					  sWhere="Wi_Name='"+wi_name+"' AND OperationType='"+returnType+"' AND AcctId = "+id;
				  }
				  //WriteLog( "returndetails sWhere="+sWhere);
				}
			  else if(sTableName.equalsIgnoreCase("ng_rlos_FinancialSummary_TxnSummary")){
				  //WriteLog( "Inside commonParseFinance for ng_rlos_FinancialSummary_TxnSummary");
				  String header_info = getTagDataParent_financ_header(parseXml,"FinancialSummaryRes","CIFID,AcctId,OperationType");
				  //WriteLog( "Inside commonParseFinance for ng_rlos_FinancialSummary_TxnSummary header info: "+ header_info);
				  String [] header_info_arr = header_info.split(":");
				  String AvgCrTurnOver=(parseXml.contains("<AvgCrTurnOver>")) ? parseXml.substring(parseXml.indexOf("<AvgCrTurnOver>")+"</AvgCrTurnOver>".length()-1,parseXml.indexOf("</AvgCrTurnOver>")):"";
				  //WriteLog( "Inside commonParseFinance for ng_rlos_FinancialSummary_TxnSummary AvgCrTurnOver info: "+ AvgCrTurnOver);
				  columnName = valueArr[0]+",Wi_Name,AvgCrTurnOver,"+header_info_arr[0];
				  columnValues = valueArr[1]+",'"+wi_name+"','"+AvgCrTurnOver+"',"+header_info_arr[1];
				  String columnName_arr[] = columnName.split(",");
				  String columnValues_arr[] = columnValues.split(",");
				  id = columnValues_arr[Arrays.asList(columnName_arr).indexOf("AcctId")];
				  String Month = columnValues_arr[Arrays.asList(columnName_arr).indexOf("Month")];
				  sWhere="Wi_Name='"+wi_name+"' AND OperationType='"+returnType+"' AND AcctId = "+id+" and Month = "+Month+"";
			  }
			  else if(sTableName.equalsIgnoreCase("ng_rlos_FinancialSummary_LienDetails")){
				    //WriteLog( "Inside commonParseFinance for ng_rlos_FinancialSummary_LienDetails");
				  String header_info = getTagDataParent_financ_header(parseXml,"FinancialSummaryRes","CIFID,AcctId,OperationType");
				  //WriteLog( "Inside commonParseFinance for ng_rlos_FinancialSummary_LienDetails header info: "+ header_info);
				  String [] header_info_arr = header_info.split(":");
				  String columnName_arr[] = header_info_arr[0].split(",");
				  String columnValues_arr[] = header_info_arr[1].split(",");
				  id = columnValues_arr[Arrays.asList(columnName_arr).indexOf("AcctId")];
				  String leinId=columnValues_arr[Arrays.asList(columnName_arr).indexOf("LienId")];
				  columnName = valueArr[0]+",Wi_Name,"+header_info_arr[0];
				  columnValues = valueArr[1]+",'"+wi_name+"',"+header_info_arr[1];
				  sWhere="Wi_Name='"+wi_name+"' AND OperationType='"+returnType+"' AND AcctId = "+id+" and LienId = "+leinId;
				}
			  else if(sTableName.equalsIgnoreCase("ng_rlos_FinancialSummary_SalTxnDetails")){
				//WriteLog( "Inside commonParseFinance for ng_rlos_FinancialSummary_SalTxnDetails");
				  String header_info = getTagDataParent_financ_header(parseXml,"FinancialSummaryRes","CifId,AcctId,OperationType");
				  String [] header_info_arr = header_info.split(":");
				  columnName = valueArr[0]+",Wi_Name,"+header_info_arr[0];
				  columnValues = valueArr[1]+",'"+wi_name+"',"+header_info_arr[1];
				   String columnName_arr[] = columnName.split(",");
				  String columnValues_arr[] = columnValues.split(",");
				  //WriteLog( "Inside commonParseFinance for ng_rlos_FinancialSummary_SalTxnDetails--->columnName:"+columnName);
				  //WriteLog( "Inside commonParseFinance for ng_rlos_FinancialSummary_SalTxnDetails--->columnValues:"+columnValues);

				  id = columnValues_arr[Arrays.asList(columnName_arr).indexOf("AcctId")];
				  String SalCreditDate = columnValues_arr[Arrays.asList(columnName_arr).indexOf("SalCreditDate")];
				  sWhere="Wi_Name='"+wi_name+"' AND OperationType='"+returnType+"' AND AcctId = "+id+" and SalCreditDate = "+SalCreditDate+"";
			  }
			  else if(sTableName.equalsIgnoreCase("ng_rlos_FinancialSummary_SiDtls")){
					  String header_info = getTagDataParent_financ_header(parseXml,"FinancialSummaryRes","CifId,AcctId,OperationType");
					  String [] header_info_arr = header_info.split(":");
					  String columnName_arr[] = header_info_arr[0].split(",");
					  String columnValues_arr[] = header_info_arr[1].split(",");
					  id = columnValues_arr[Arrays.asList(columnName_arr).indexOf("AcctId")];
					  columnName = valueArr[0]+",Wi_Name,"+header_info_arr[0];
					  columnValues = valueArr[1]+",'"+wi_name+"',"+header_info_arr[1];
					  sWhere="Wi_Name='"+wi_name+"' AND OperationType='"+returnType+"' AND AcctId = "+id;
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
			  else{
				columnName = valueArr[0]+",Wi_Name,Request_Type";
				columnValues = colValue+",'"+wi_name+"','"+returnType+"'";  
			  }
			  
			  
			  //WriteLog( "columnName commonParse" + columnName);
			  //WriteLog( "columnValues commonParse" + columnValues);
			  
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
					//WriteLog("getTagValue select mainCode --> "+row_updated);
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
		//WriteLog("CustExpose_Output jsp: final value for financial summary "+retVal);
		return retVal;
	}
	
	
	public static String commonParseProduct(String parseXml,String tagName,String wi_name,String returnType,String sTableName,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String appServerType, String subTagName,String prod,String subprod, String cifId,String cust_type,String subtag_single)
	{
		//WriteLog("inside commonParseProduct for cif: "+cifId);
		String retVal = "";
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
			String ReportUrl = "";
			String FullNm = "";
			String TotalOutstanding = "";
			String TotalOverdue = "";
			String NoOfContracts = "";
			String ECRN = "";
			String BorrowingCustomer = "";
			String 	sQry="";
			String selectdata="";
			String companyUpdateQuery="";
			String companiestobeUpdated = "";
			String referenceNo = "";
			String scoreInfo = "";
			String Aecb_Score = "";
			String range = "";
			boolean stopIndividualToInsert = false;
			
			//WriteLog("tagName jsp: commonParse: "+tagName);
			//WriteLog("subTagName jsp: commonParse: "+subTagName);
			//WriteLog("parsexml jsp: commonParse: "+parseXml);			
			//Parsing AECB score, range and Reference No. for 2.1 start, Added by Shivang
			
			referenceNo=(parseXml.contains("<ReferenceNumber>")) ? parseXml.substring(parseXml.indexOf("<ReferenceNumber>")+"</ReferenceNumber>".length()-1,parseXml.indexOf("</ReferenceNumber>")):"";
			if(parseXml.contains("<ScoreInfo>")){
				scoreInfo = parseXml.substring(parseXml.indexOf("<ScoreInfo>")+"</ScoreInfo>".length()-1,parseXml.indexOf("</ScoreInfo>"));
				Aecb_Score=(scoreInfo.contains("<Value>")) ? scoreInfo.substring(scoreInfo.indexOf("<Value>")+"</Value>".length()-1,scoreInfo.indexOf("</Value>")):"";
				range=(scoreInfo.contains("<Range>")) ? scoreInfo.substring(scoreInfo.indexOf("<Range>")+"</Range>".length()-1,scoreInfo.indexOf("</Range>")):"";
				WriteLog("parsexml jsp: commonParse: AECB Score: "+Aecb_Score + " Range: "+range);
			}
			
			//Parsing AECB score, range and Reference No. for 2.1 end, Added by Shivang
			ReportUrl=(parseXml.contains("<ReportUrl>")) ? parseXml.substring(parseXml.indexOf("<ReportUrl>")+"</ReportUrl>".length()-1,parseXml.indexOf("</ReportUrl>")):"";
			FullNm=(parseXml.contains("<FullNm>")) ? parseXml.substring(parseXml.indexOf("<FullNm>")+"</FullNm>".length()-1,parseXml.indexOf("</FullNm>")):"";
			TotalOutstanding=(parseXml.contains("<TotalOutstanding>")) ? parseXml.substring(parseXml.indexOf("<TotalOutstanding>")+"</TotalOutstanding>".length()-1,parseXml.indexOf("</TotalOutstanding>")):"";
			TotalOverdue=(parseXml.contains("<TotalOverdue>")) ? parseXml.substring(parseXml.indexOf("<TotalOverdue>")+"</TotalOverdue>".length()-1,parseXml.indexOf("</TotalOverdue>")):"";
			NoOfContracts=(parseXml.contains("<NoOfContracts>")) ? parseXml.substring(parseXml.indexOf("<NoOfContracts>")+"</NoOfContracts>".length()-1,parseXml.indexOf("</NoOfContracts>")):"";
			ECRN=(parseXml.contains("<ECRN>")) ? parseXml.substring(parseXml.indexOf("<ECRN>")+"</ECRN>".length()-1,parseXml.indexOf("</ECRN>")):"";
			BorrowingCustomer=(parseXml.contains("<BorrowingCustomer>")) ? parseXml.substring(parseXml.indexOf("<BorrowingCustomer>")+"</BorrowingCustomer>".length()-1,parseXml.indexOf("</BorrowingCustomer>")):"";
			//WriteLog("ReportUrl jsp: ReportUrl: "+ReportUrl);
			//WriteLog("Cifid jsp: ReportUrl: "+cifId);
			//WriteLog("FullNm jsp: ReportUrl: "+FullNm);
			//WriteLog("TotalOutstanding jsp: TotalOutstanding: "+TotalOutstanding);
			//WriteLog("TotalOverdue jsp: TotalOverdue: "+TotalOverdue);
			//WriteLog("NoOfContracts jsp: NoOfContracts: "+NoOfContracts);
			//WriteLog("ECRN jsp: ECRN: "+ECRN);
			//WriteLog("BorrowingCustomer jsp: BorrowingCustomer: "+BorrowingCustomer);
			
			 Map<String, String> tagValuesMap= new LinkedHashMap<String, String>();		 
			 tagValuesMap=getTagDataParent_deep(parseXml,tagName,subTagName,subtag_single);
			
			 Map<String, String> map = tagValuesMap;
				String colValue="";
			  for (Map.Entry<String, String> entry : map.entrySet())
			  {
				  valueArr=entry.getValue().split("~");
				  //WriteLog( "tag values" + entry.getValue());
				 
					colValue = "'"+valueArr[1].replaceAll("[,]", "','")+"'";
					columnName = valueArr[0]+",Wi_Name,Request_Type,Product_Type,CardType,CifId";
					columnValues = valueArr[1]+",'"+wi_name+"','"+returnType+"','"+prod+"','"+subprod+"','"+cifId+"'";
				  
				   
				   
				  //WriteLog( "columnName commonParse" + columnName);
				  //WriteLog( "columnValues commonParse" + columnValues);
				  if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_CardDetails")){
					  columnName = valueArr[0]+",Wi_Name,Request_Type,CifId,Liability_type,BorrowingCustomer,ECRN";
					  columnValues = valueArr[1]+",'"+wi_name+"','"+returnType+"','"+cifId+"','"+cust_type+"','"+BorrowingCustomer+"','"+ECRN+"'";
					   sWhere="Wi_Name='"+wi_name+"' AND CardEmbossNum = '"+entry.getKey()+"' And Liability_type ='"+cust_type+"'";
					   
					   //change by saurabh on 2nd Feb for cust_type
					   sQry="Select count(*) as selectdata from "+sTableName+" where Wi_Name='"+wi_name+"' And CardEmbossNum = '"+entry.getKey()+"' And Liability_type ='Individual_CIF' ";
						 //WriteLog( "sQry sQry" + sQry);
						if(cust_type.equalsIgnoreCase("Individual_CIF")) {
							companyUpdateQuery="Select count(*) as selectdata from "+sTableName+" where Wi_Name='"+wi_name+"' And CardEmbossNum = '"+entry.getKey()+"' And Liability_type ='Corporate_CIF'";
						}
				  }
				  else if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_LoanDetails")){
					  columnName = valueArr[0]+",Wi_Name,Request_Type,Product_Type,CardType,CifId,Liability_type,BorrowingCustomer,ECRN";
					  columnValues = valueArr[1]+",'"+wi_name+"','"+returnType+"','"+prod+"','"+subprod+"','"+cifId+"','"+cust_type+"','"+BorrowingCustomer+"','"+ECRN+"'";
					  columnName =columnName.replace("OutStandingAmt","TotalOutStandingAmt");
					  sWhere="Wi_Name='"+wi_name+"' AND AgreementId = '"+entry.getKey()+"' And Liability_type ='"+cust_type+"'";
					  //change by saurabh on 2nd Feb for cust_type
					    sQry="Select count(*) as selectdata from "+sTableName+" where Wi_Name='"+wi_name+"' And  AgreementId = '"+entry.getKey()+"' And Liability_type ='Individual_CIF'";
						if(cust_type.equalsIgnoreCase("Individual_CIF")) {
							companyUpdateQuery="Select count(*) as selectdata from "+sTableName+" where Wi_Name='"+wi_name+"' And AgreementId = '"+entry.getKey()+"' And Liability_type ='Corporate_CIF'";
						}
				  //WriteLog( "sQry  loan sQry" + sQry);
				  }
				  else if(sTableName.equalsIgnoreCase("ng_rlos_cust_extexpo_ChequeDetails")){
						  columnName = valueArr[0]+",Wi_Name,Request_Type,CifId";
						  columnValues = valueArr[1]+",'"+wi_name+"','"+returnType+"','"+cifId+"'";
						  sWhere="Wi_Name='"+wi_name+"' AND ChqType = '"+entry.getKey()+"'";
						   //WriteLog( "columnName entry.getKey()" + entry.getKey());
				  }
				  else if(sTableName.equalsIgnoreCase("ng_rlos_cust_extexpo_LoanDetails")){
					   columnName = valueArr[0]+",Wi_Name,Request_Type,Product_Type,CardType,CifId,ReportURL,Liability_type";
					  columnValues = valueArr[1]+",'"+wi_name+"','"+returnType+"','"+prod+"','"+subprod+"','"+cifId+"','"+ReportUrl+"','"+cust_type+"'";
					  String columnName_arr[] = columnName.split(",");
					  String columnValues_arr[] = columnValues.split(",");
					  for(int arrlen=0;arrlen<columnName_arr.length;arrlen++){
						  if("LoanType".equalsIgnoreCase(columnName_arr[arrlen])){
							  WriteLog( "inside loan desc tag name" + columnName_arr[arrlen]);
							  WriteLog( "inside loan desc tag value" + columnValues_arr[arrlen]);
							  String loan_desc = get_loanDesc(columnValues_arr[arrlen], cabinetName, sessionId, wrapperIP,wrapperPort, appServerType);
							  columnValues = columnValues.replaceFirst(columnValues_arr[arrlen], loan_desc);
							  break;
						  }
					  }
					  WriteLog( "updated columnValues : " + columnValues);
					  columnName =columnName.replace("OutStanding Balance","OutStanding_Balance");
					  columnName =columnName.replace("LastUpdateDate","datelastupdated");
					  columnName =columnName.replace("Total Amount","Total_Amount");
					  columnName =columnName.replace("Payments Amount","Payments_Amount");
					  columnName =columnName.replace("Overdue Amount","Overdue_Amount");
					  sWhere="Wi_Name='"+wi_name+"' AND AgreementId = '"+entry.getKey()+"'";
				  }
				  else if(sTableName.equalsIgnoreCase("ng_rlos_cust_extexpo_CardDetails")){
					  columnName = valueArr[0]+",Wi_Name,Request_Type,Product_Type,sub_product_type,CifId,Liability_type";
					  columnValues = valueArr[1]+",'"+wi_name+"','"+returnType+"','"+prod+"','"+subprod+"','"+cifId+"','"+cust_type+"'";
					  String columnName_arr[] = columnName.split(",");
					  String columnValues_arr[] = columnValues.split(",");
					  for(int arrlen=0;arrlen<columnName_arr.length;arrlen++){
						  if("CardType".equalsIgnoreCase(columnName_arr[arrlen])){
							  WriteLog( "inside loan desc tag name" + columnName_arr[arrlen]);
							  WriteLog( "inside loan desc tag value" + columnValues_arr[arrlen]);
							  String loan_desc = get_loanDesc(columnValues_arr[arrlen], cabinetName, sessionId, wrapperIP,wrapperPort, appServerType);
							  columnValues = columnValues.replaceFirst(columnValues_arr[arrlen], loan_desc);
							  break;
						  }
					  }
					  sWhere="Wi_Name='"+wi_name+"' AND CardEmbossNum = '"+entry.getKey()+"'";
				  }
				  else if(sTableName.equalsIgnoreCase("NG_rlos_custexpose_Derived")){
					      //Added 3 New value for 2.1 by shivang.
						  columnName = valueArr[0]+",Wi_Name,Request_Type,CifId,FullNm,TotalOutstanding,TotalOverdue,NoOfContracts,ReportURL,ReferenceNo,AECB_Score,Range";
						  columnValues = valueArr[1]+",'"+wi_name+"','"+returnType+"','"+cifId+"','"+FullNm+"','"+TotalOutstanding+"','"+TotalOverdue+"','"+NoOfContracts+"','"+ReportUrl+"','"+referenceNo+"','"+Aecb_Score+"','"+range+"'";
						  sWhere="Wi_Name='"+wi_name+"' AND Request_Type = '"+returnType+"' and CifId='"+cifId+"'";
				  }
				  //Changes Done to save data in NG_RLOS_CUSTEXPOSE_RecordDestribution table on 14th sept by Aman
				   else if(sTableName.equalsIgnoreCase("NG_RLOS_CUSTEXPOSE_RecordDestribution")){
						  columnName = valueArr[0]+",Wi_Name,Request_Type,CifId";
						  columnValues = valueArr[1]+",'"+wi_name+"','"+returnType+"','"+cifId+"'";
						  sWhere="Wi_Name='"+wi_name+"' AND ContractType = '"+entry.getKey()+"'";
				  }
				  //Changes Done to save data in NG_RLOS_CUSTEXPOSE_RecordDestribution table on 14th sept by Aman
				   else if(sTableName.equalsIgnoreCase("ng_rlos_cust_extexpo_AccountDetails")){
						  columnName = valueArr[0]+",Wi_Name,Request_Type,CifId";
						  columnValues = valueArr[1]+",'"+wi_name+"','"+returnType+"','"+cifId+"'";
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
						  sWhere="Wi_Name='"+wi_name+"' AND AcctId = '"+entry.getKey()+"'";
				  }
				  //Deepak changes done for Service details
				   else if(sTableName.equalsIgnoreCase("ng_rlos_cust_extexpo_ServicesDetails")){
						  columnName = valueArr[0]+",Wi_Name,Request_Type,CifId";
						  columnValues = valueArr[1]+",'"+wi_name+"','"+returnType+"','"+cifId+"'";
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
						  sWhere="Wi_Name='"+wi_name+"' AND ServiceID = '"+entry.getKey()+"'";
				  }
				  //below changes Done to save AccountType in ng_RLOS_CUSTEXPOSE_AcctDetails table on 29th Dec by Disha
				  else if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_AcctDetails")){
					 String CreditGrade = (parseXml.contains("<CreditGrade>")) ? parseXml.substring(parseXml.indexOf("<CreditGrade>")+"</CreditGrade>".length()-1,parseXml.indexOf("</CreditGrade>")):"";
					 //String ODType = (parseXml.contains("<ODType>")) ? parseXml.substring(parseXml.indexOf("<ODType>")+"</ODType>".length()-1,parseXml.indexOf("</ODType>")):"";
					 //String ODDesc = (parseXml.contains("<ODDesc>")) ? parseXml.substring(parseXml.indexOf("<ODDesc>")+"</ODDesc>".length()-1,parseXml.indexOf("</ODDesc>")):"";
						  columnName = valueArr[0]+",Wi_Name,Request_Type,CifId,CreditGrade,Account_Type";
						  columnValues = valueArr[1]+",'"+wi_name+"','"+returnType+"','"+cifId+"','"+CreditGrade+"','"+cust_type+"'";
						  sWhere="Wi_Name='"+wi_name+"' AND AcctId = '"+entry.getKey()+"' AND Account_Type = '"+cust_type+"'";
						  //change by saurabh on 24th Feb for skipping account save for Employer cif.
					sQry="Select count(*) as selectdata from NG_RLOS_ALOC_OFFLINE_DATA where CIF_ID ='"+cifId+"'";
					//WriteLog( "sQry  loan sQry" + sQry);	  
				  }
				  //Deepak 22 july 2019 new condition added to save custinfo
				  else if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_CustInfo")){
					  columnName = valueArr[0]+",Wi_Name,Request_Type,CifId";
						columnValues = valueArr[1]+",'"+wi_name+"','"+returnType+"','"+cifId+"'";
					   sWhere="Wi_Name='"+wi_name+"' AND Request_Type = '"+returnType+"' AND CifId = '"+cifId+"'";	  
					  }
				  //above changes Done to save AccountType in ng_RLOS_CUSTEXPOSE_AcctDetails table on 29th Dec by Disha
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
						//WriteLog("getTagValue select mainCode for update query for cif"+cifId+"--> "+mainCode);
						//WriteLog("getTagValue select rowUpdated for update query for cif"+cifId+" --> "+row_updated);
						if(!mainCode.equalsIgnoreCase("0") || row_updated.equalsIgnoreCase("0"))
						{
						//WriteLog("sQry sQry sQry inside for insert--> "+sQry);
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
								e.printStackTrace();
							} 
							catch (Exception ex) 
							{
								ex.printStackTrace();
							}
							
						
							
							mainCode = (strOutputXml.contains("<MainCode>")) ? strOutputXml.substring(strOutputXml.indexOf("<MainCode>")+"</MainCode>".length()-1,strOutputXml.indexOf("</MainCode>")):"";
							//WriteLog("getTagValue select mainCode --> "+mainCode);
						
							companiestobeUpdated=(strOutputXml.contains("<selectdata>")) ? strOutputXml.substring(strOutputXml.indexOf("<selectdata>")+"</selectdata>".length()-1,strOutputXml.indexOf("</selectdata>")):"";
							//WriteLog("getTagValue select companiestobeUpdated --> "+companiestobeUpdated);
							
							if(Integer.parseInt(companiestobeUpdated)>0){
								sWhere="Wi_Name='"+wi_name+"' AND CardEmbossNum = '"+entry.getKey()+"' And Liability_type ='Corporate_CIF'";
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
								//row_updated = getTagValue(strOutputXml,tagNameU,subTagNameU_2);
								////WriteLog("getTagValue select mainCode for update query for cif"+cifId+"--> "+mainCode);
								////WriteLog("getTagValue select rowUpdated for company for update query for cif"+cifId+" --> "+row_updated);
								stopIndividualToInsert = true;
							}
							
							
							}
							
							
							
							if(sQry.equalsIgnoreCase("") ||(mainCode.equalsIgnoreCase("0") && selectdata.equalsIgnoreCase("0") && !stopIndividualToInsert)){
						
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
								   e.printStackTrace();
								} 
								catch (Exception ex) 
								{
								   ex.printStackTrace();
								}
						  }
						  //change by saurabh for company if its not able to overwrite data of individual but on frontend it should be success.change on 2nd feb
						  else{
							retVal = "true";
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
	
	public static String commonParseProduct_collection(String parseXml,String tagName,String wi_name,String returnType,String sTableName,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String appServerType, String subTagName,String prod,String subprod, String cifId,String subtag_single,String cust_type)
	{
		//WriteLog("Inside commonParseProduct_collection for CifId: "+cifId);
		if(!parseXml.contains(tagName)){
			return "true";
		}
		else
		{ 
		
			//WriteLog("commonParse jsp: inside: ");
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
			String 	sQry="";
			String selectdata="";
			String companyUpdateQuery="";
			String companiestobeUpdated = "";
			boolean stopIndividualToInsert = false;
			//WriteLog("tagName jsp: commonParse: "+tagName);
			//WriteLog("subTagName jsp: commonParse: "+subTagName);
			
			
			 Map<String, String> tagValuesMap= new LinkedHashMap<String, String>();		 
			 tagValuesMap=getTagDataParent_deep(parseXml,tagName,subTagName, subtag_single);
			
			 Map<String, String> map = tagValuesMap;
		  String colValue="";
			  for (Map.Entry<String, String> entry : map.entrySet())
			  {
				  valueArr=entry.getValue().split("~");
				  //WriteLog( "tag values" + entry.getValue());
				  
				  
				  colValue = "'"+valueArr[1].replaceAll("[,]", "','")+"'";
				  columnName = valueArr[0]+",Wi_Name,Request_Type,Product_Type,CardType,CifId";
				  columnValues = valueArr[1]+",'"+wi_name+"','"+returnType+"','"+prod+"','"+subprod+"','"+cifId+"'";
				  
				   
				   
				  //WriteLog( "columnName commonParse" + columnName);
				  //WriteLog( "columnValues commonParse" + columnValues);
				  if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_CardDetails")){
					  
					  columnName = valueArr[0]+",Request_Type,CifId,Wi_Name,Liability_type";
					  columnName = columnName.replaceAll("Card_approve_date","ApplicationCreationDate");
					  columnName = columnName.replaceAll("Outstanding_balance","OutstandingAmt");
					  columnName = columnName.replaceAll("Credit_limit","CreditLimit");
					  columnName = columnName.replaceAll("Overdue_amount","OverdueAmt");
					  columnName = columnName.replaceAll("GeneralStatus","General_Status");
					  columnValues = valueArr[1]+",'"+returnType+"','"+cifId+"','"+wi_name+"','"+cust_type+"'";
					   sWhere="Wi_Name='"+wi_name+"' AND CardEmbossNum = '"+entry.getKey()+"' And Liability_type ='"+cust_type+"'";
					   //change by saurabh below cust_type on 2nd Feb
			     	sQry="Select count(*) as selectdata from "+sTableName+" where Wi_Name='"+wi_name+"' And CardEmbossNum = '"+entry.getKey()+"' And Liability_type ='Individual_CIF'";
					  if(cust_type.equalsIgnoreCase("Individual_CIF")) {
							companyUpdateQuery="Select count(*) as selectdata from "+sTableName+" where Wi_Name='"+wi_name+"' And CardEmbossNum = '"+entry.getKey()+"' And Liability_type ='Corporate_CIF'";
						}
				  }
				  else if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_LoanDetails")){
					  columnName = valueArr[0]+",Wi_Name,Request_Type,Product_Type,CardType,CifId,Liability_type";
					  columnValues = valueArr[1]+",'"+wi_name+"','"+returnType+"','"+prod+"','"+subprod+"','"+cifId+"','"+cust_type+"'";
					  columnName = columnName.replaceAll("OutstandingAmt","TotalOutstandingAmt");
					  columnName = columnName.replaceAll("GeneralStatus","General_Status");
					  columnName = columnName.replaceAll("Loan_close_date","LoanMaturityDate");
					  sWhere="Wi_Name='"+wi_name+"' AND AgreementId = '"+entry.getKey()+"' And Liability_type ='"+cust_type+"'";
					  //change by saurabh on 2nd Feb cust_type  
					  sQry="Select count(*) as selectdata from "+sTableName+" where Wi_Name='"+wi_name+"' And AgreementId = '"+entry.getKey()+"' And Liability_type ='Individual_CIF'";
					  if(cust_type.equalsIgnoreCase("Individual_CIF")) {
							companyUpdateQuery="Select count(*) as selectdata from "+sTableName+" where Wi_Name='"+wi_name+"' And AgreementId = '"+entry.getKey()+"' And Liability_type ='Corporate_CIF'";
						}

				  }
			  //Changes Done by aman to save the data of collection summary
		/*			  else if(sTableName.equalsIgnoreCase("NG_rlos_custexpose_Derived")){
						  columnName = valueArr[0]+",Wi_Name,Request_Type,CifId,FullNm,TotalOutstanding,TotalOverdue,NoOfContracts";
						  columnValues = valueArr[1]+",'"+wi_name+"','"+returnType+"','"+cifId+"','"+FullNm+"','"+TotalOutstanding+"','"+TotalOverdue+"','"+NoOfContracts+"'";
						  sWhere="Wi_Name='"+wi_name+"' AND Total_Exposure = '"+entry.getKey()+"'";
				  }
		*/		  
				    //Changes Done by aman to save the data of collection summary
				  else if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_AcctDetails")){
					 String CreditGrade = (parseXml.contains("<CreditGrade>")) ? parseXml.substring(parseXml.indexOf("<CreditGrade>")+"</CreditGrade>".length()-1,parseXml.indexOf("</CreditGrade>")):"";
						  columnName = valueArr[0]+",Wi_Name,Request_Type,CifId,CreditGrade";
						  columnValues = valueArr[1]+",'"+wi_name+"','"+returnType+"','"+cifId+"','"+CreditGrade+"'";
						  sWhere="Wi_Name='"+wi_name+"' AND AcctId = '"+entry.getKey()+"'";
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
						//WriteLog("getTagValue select mainCode --> "+row_updated);
						if(!mainCode.equalsIgnoreCase("0") || row_updated.equalsIgnoreCase("0"))
						{
						
						//WriteLog("sQry sQry sQry --> "+sQry);
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
								e.printStackTrace();
							} 
							catch (Exception ex) 
							{
								ex.printStackTrace();
							}
							
						
							
							mainCode = (strOutputXml.contains("<MainCode>")) ? strOutputXml.substring(strOutputXml.indexOf("<MainCode>")+"</MainCode>".length()-1,strOutputXml.indexOf("</MainCode>")):"";
							//WriteLog("getTagValue select mainCode --> "+mainCode);
						
							companiestobeUpdated=(strOutputXml.contains("<selectdata>")) ? strOutputXml.substring(strOutputXml.indexOf("<selectdata>")+"</selectdata>".length()-1,strOutputXml.indexOf("</selectdata>")):"";
							//WriteLog("getTagValue select companiestobeUpdated --> "+companiestobeUpdated);
							
							if(Integer.parseInt(companiestobeUpdated)>0){
								if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_CardDetails")){
								sWhere="Wi_Name='"+wi_name+"' AND CardEmbossNum = '"+entry.getKey()+"' And Liability_type ='Corporate_CIF'";
								}
								else if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_LoanDetails")){
								sWhere="Wi_Name='"+wi_name+"' AND AgreementId = '"+entry.getKey()+"' And Liability_type ='Corporate_CIF'";
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
									mainCode = getTagValue(strOutputXml,"APInsert_Output",subTagNameU);
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
								   e.printStackTrace();
								} 
								catch (Exception ex) 
								{
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
	
	public static String commonParseParent(String parseXml,String tagName,String wi_name,String returnType,String sTableName,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String appServerType, String sParentTagName, String subTagName, String cifId)
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
			  
			  columnName = valueArr[0]+","+sParentTagName+",Wi_Name,Request_Type,CifId";
			  columnValues = colValue+",'"+valueArr[2]+"','"+wi_name+"','"+returnType+"','"+cifId+"'";
			  
			  //WriteLog( "columnName commonParseParent" + columnName);
			  //WriteLog( "columnValues commonParseParent" + columnValues);
			  sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"'";
			  
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
		   //WriteLog("CustExpose_Output jsp: final value of commonParseParent "+retVal);
		  return retVal;
		 
	}
	
	public static Map<Integer, String> getTagDataParent(String parseXml,String tagName,String subTagName){
	  
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
		    	System.out.println("Exception occured in getTagDataParent method: "+ e.getMessage());
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
  
  public static void commonParseParentKey(String parseXml,String tagName,String wi_name,String returnType,String sTableName,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String appServerType, String sParentTagName, String subTagName, String cifId)
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
				  if(keyDtType.equalsIgnoreCase("LoanApprovedDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];					
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("LoanMaturityDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				}
				if(returnType.equalsIgnoreCase("ExternalExposure"))
				{
				  if(keyDtType.equalsIgnoreCase("LoanApprovedDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("LoanMaturityDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				    if(keyDtType.equalsIgnoreCase("MaxOverdueAmountDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				}
				if(returnType.equalsIgnoreCase("CollectionsSummary"))
				{
				  if(keyDtType.equalsIgnoreCase("LoanApprovedDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("LoanMaturityDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
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
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND CardEmbossNum = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("ExpiryDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND CardEmbossNum = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				}
				if(returnType.equalsIgnoreCase("ExternalExposure"))
				{
				  if(keyDtType.equalsIgnoreCase("MaxOverDueAmountDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND CardEmbossNum = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
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
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AcctId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("LimitSactionDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AcctId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("LimitExpiryDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AcctId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				}
				if(returnType.equalsIgnoreCase("ExternalExposure"))
				{
				  if(keyDtType.equalsIgnoreCase("StartDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AcctId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("ClosedDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AcctId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("MaxOverdueAmtDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AcctId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
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
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("SvcExpDt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
					
				  }
				}
				
			 }		  			  
			  
		  }
	}
	
	public static void commonParseParentAmt(String parseXml,String tagName,String wi_name,String returnType,String sTableName,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String appServerType, String sParentTagName, String subTagName, String cifId)
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
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];					
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("LoanDisbursementAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("TotalOutstandingAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("OverdueAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("MortgagesValue"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("NextInstallmentAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				}
				if(returnType.equalsIgnoreCase("ExternalExposure"))
				{
				  if(keyDtType.equalsIgnoreCase("TotalAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("OutstandingAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				    if(keyDtType.equalsIgnoreCase("PaymentsAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				     if(keyDtType.equalsIgnoreCase("OverdueAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				     if(keyDtType.equalsIgnoreCase("MaximumOvrdueAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				}
				if(returnType.equalsIgnoreCase("CollectionsSummary"))
				{
				  if(keyDtType.equalsIgnoreCase("OutstandingAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("ProductAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("NextInstallmentAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("OverdueAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
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
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND CardEmbossNum = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("OutstandingAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND CardEmbossNum = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("OverdueAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND CardEmbossNum = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("CurrMaxUtil"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND CardEmbossNum = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				}
				if(returnType.equalsIgnoreCase("ExternalExposure"))
				{
				  if(keyDtType.equalsIgnoreCase("CurrentBalance"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND CardEmbossNum = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("CashLimit"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND CardEmbossNum = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("OverdueAmount"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND CardEmbossNum = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("MaxOverdueAmount"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND CardEmbossNum = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
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
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AcctId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("ClearBalanceAmount"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AcctId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("LedgerBalance"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AcctId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("LoanDisbursementAmount"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AcctId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("OverdueAmount"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AcctId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("EffectiveAvailableBalance"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AcctId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("CumulativeDebitAmount"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AcctId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("CurOutstandingAmount"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AcctId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				}
				if(returnType.equalsIgnoreCase("ExternalExposure"))
				{
				  if(keyDtType.equalsIgnoreCase("CashLimit"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AcctId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("OverdueAmount"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AcctId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("MaxOverdueAmount"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AcctId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
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
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				 
				}
				
			 }		  			  
			  
		  }
	}
	
	public static void commonParseParentDelinquency(String parseXml,String tagName,String wi_name,String returnType,String sTableName,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String appServerType, String sParentTagName, String subTagName, String cifId)
	{
		//WriteLog("commonParseParent jsp: inside: ");
		String [] valueArr= null;
		String sWhere = "";
		String columnName ="";
		String colValue ="";
		String keyDtType = "";
		String [] colArr= null;
		String detailType= null;
		 
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
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("DPD60Last18Months"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
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
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				 
				}
				if(returnType.equalsIgnoreCase("ExternalExposure"))
				{
				  if(keyDtType.equalsIgnoreCase("DPD30Last6Months"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("DPD60Last18Months"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					//WriteLog( "valueArr[2] sWhere" + sWhere);
					//WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				
				}
			 }					  			  
			  
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
		
		
		 Map<Integer, String> tagValuesMap= new LinkedHashMap<Integer, String>();		 
		 tagValuesMap=getTagDataParent(parseXml,tagName,subTagName);
		
		 Map<Integer, String> map = tagValuesMap;
		String colValue="";
		  for (Map.Entry<Integer, String> entry : map.entrySet())
		  {
			  valueArr=entry.getValue().split("~");
			  //WriteLog( "tag values" + entry.getValue());
			  mainCodeValue = valueArr[1];	
			  //WriteLog( "mainCodeValue" + mainCodeValue);
				
		  }
		  return mainCodeValue;
	}
	
	public static void updateQuery(String sTableName,String columnName,String colValue,String sWhere,String cabinetName,String sessionId,String returnType,String wrapperIP,String wrapperPort, String appServerType,String cifId,String wi_name)
  {
	String strInputXml="";
	String strOutputXml="";
	String mainCode="";
	String tagNameU="";
	String subTagNameU="";
	String subTagNameU_2="";
	String columnValues="";
	String row_updated="";
	
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
			  
			  columnName = columnName+",Wi_Name,Request_Type,CifId";
			  columnValues = colValue+",'"+wi_name+"','"+returnType+"','"+cifId+"'";
			   
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
	//Deepak code commented method changed with new subtag_single param 23jan2018
	public static Map<String, String> getTagDataParent_deep(String parseXml,String tagName,String sub_tag,String subtag_single){
		  Map<String, String> tagValuesMap= new LinkedHashMap<String, String>(); 
		   InputStream is = new ByteArrayInputStream(parseXml.getBytes());
		  try {
				//WriteLog("getTagDataParent_deep jsp: parseXml: "+parseXml);
				//WriteLog("getTagDataParent_deep jsp: tagName: "+tagName);
				//WriteLog("getTagDataParent_deep jsp: subTagName: "+sub_tag);
				String Operationtype="";
				String id="";
				String tag_notused = "BankId,OperationDesc,TxnSummary,#text";
				if(parseXml.indexOf("<OperationType>")>-1)
					{
						Operationtype= parseXml.substring(parseXml.indexOf("<OperationType>")+15,parseXml.indexOf("</OperationType>"));
						//WriteLog("$$Operationtype "+Operationtype);
					}
				
			   	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(is);
				doc.getDocumentElement().normalize();
				NodeList nList_loan = doc.getElementsByTagName(tagName);
				//WriteLog("getTagDataParent_deep jsp: nList_loan: "+nList_loan);
				for(int i = 0 ; i<nList_loan.getLength();i++){
					String col_name = "";
					String col_val ="";
					NodeList ch_nodeList = nList_loan.item(i).getChildNodes();
					////WriteLog("getTagDataParent_deep jsp: ch_nodeList: "+ch_nodeList);
					if (Operationtype.equalsIgnoreCase("SALDET")){
						id = ch_nodeList.item(1).getTextContent();
					}
					else if (Operationtype.equalsIgnoreCase("RETURNDET")){
							int id_num = 0;
							for(int ch_len = 0 ;ch_len< ch_nodeList.getLength(); ch_len++){
								if(ch_nodeList.item(ch_len).getNodeName().equalsIgnoreCase("ReturnNumber")){
									id_num = ch_len;
								}
							}
							id = ch_nodeList.item(id_num).getTextContent();
						}
					else if("SalDetails".equalsIgnoreCase(tagName)){
						id = ch_nodeList.item(0).getTextContent()+i;
					}
					else if("ServicesDetails".equalsIgnoreCase(tagName)){
						id = ch_nodeList.item(1).getTextContent();
					}
					else {
						id = ch_nodeList.item(0).getTextContent();
					}
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
									////WriteLog("inside else if");
									col_name = ch_nodeList.item(ch_len).getNodeName();
									col_val = "'"+ch_nodeList.item(ch_len).getTextContent()+"'";
								}
								else if(!col_name.contains(ch_nodeList.item(ch_len).getNodeName())){
								//WriteLog("inside else else if"+ch_nodeList.item(ch_len).getNodeName());
								//WriteLog("inside else else if"+ch_nodeList.item(ch_len).getTextContent());
												col_name = col_name+","+ch_nodeList.item(ch_len).getNodeName();
												//WriteLog("inside else col_name"+col_name);
												col_val = col_val+",'"+ch_nodeList.item(ch_len).getTextContent()+"'";
												//WriteLog("inside else col_name"+col_val);
											}
							}
						}
						if(!col_name.equalsIgnoreCase(""))
							//WriteLog("inside else col_name "+col_name+" id "+id+" col_val "+col_val);
							tagValuesMap.put(id, col_name+"~"+col_val);	
							//WriteLog("tagValuesMap inside loop"+tagValuesMap);
				}
			    } catch (Exception e) {
					System.out.println("Exception occured in getTagDataParent_deep method: "+ e.getMessage());
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
	//Deepak code commented method changed with new subtag_single param 23jan2018
  	/*public static Map<String, String> getTagDataParent_deep(String parseXml,String tagName,String sub_tag){
	  
	  Map<String, String> tagValuesMap= new LinkedHashMap<String, String>(); 
	  try {
			//WriteLog("getTagDataParent_deep jsp: parseXml: "+parseXml);
			//WriteLog("getTagDataParent_deep jsp: tagName: "+tagName);
			//WriteLog("getTagDataParent_deep jsp: subTagName: "+sub_tag);
			String Operationtype="";
			String id="";
			String tag_notused = "BankId,OperationDesc,TxnSummary,#text";
			if(parseXml.indexOf("<OperationType>")>-1)
				{
					Operationtype= parseXml.substring(parseXml.indexOf("<OperationType>")+15,parseXml.indexOf("</OperationType>"));
					//WriteLog("$$Operationtype "+Operationtype);
				}
			 InputStream is = new ByteArrayInputStream(parseXml.getBytes());
		   	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();
		
			NodeList nList_loan = doc.getElementsByTagName(tagName);
			//WriteLog("getTagDataParent_deep jsp: nList_loan: "+nList_loan);
			for(int i = 0 ; i<nList_loan.getLength();i++){
				String col_name = "";
				String col_val ="";
				NodeList ch_nodeList = nList_loan.item(i).getChildNodes();
				////WriteLog("getTagDataParent_deep jsp: ch_nodeList: "+ch_nodeList);
				if (Operationtype.equalsIgnoreCase("SALDET")){
					id = ch_nodeList.item(1).getTextContent();
				}
				else if (Operationtype.equalsIgnoreCase("RETURNDET")){
						int id_num = 0;
						for(int ch_len = 0 ;ch_len< ch_nodeList.getLength(); ch_len++){
							if(ch_nodeList.item(ch_len).getNodeName().equalsIgnoreCase("ReturnNumber")){
								id_num = ch_len;
							}
						}
						id = ch_nodeList.item(id_num).getTextContent();
					}
				else {
					id = ch_nodeList.item(0).getTextContent();
				}
				////WriteLog("getTagDataParent_deep jsp: id: "+id);
					for(int ch_len = 0 ;ch_len< ch_nodeList.getLength(); ch_len++){
					////WriteLog("getTagDataParent_deep jsp: sub_tag: "+sub_tag);
					////WriteLog("getTagDataParent_deep jsp: ch_nodeList.item(ch_len).getNodeName(): "+ch_nodeList.item(ch_len).getNodeName());
					////WriteLog("getTagDataParent_deep jsp: ch_nodeList.item(ch_len).getNodevalues(): "+ch_nodeList.item(ch_len).getTextContent());
						if(sub_tag.contains(ch_nodeList.item(ch_len).getNodeName())){
							////WriteLog("inside if");
							NodeList sub_ch_nodeList =  ch_nodeList.item(ch_len).getChildNodes();
							if(!sub_ch_nodeList.item(0).getTextContent().equalsIgnoreCase("#text")){
								////WriteLog("inside if if");
								if(col_name.equalsIgnoreCase("")){
								////WriteLog("inside if if if");
								col_name = sub_ch_nodeList.item(0).getTextContent();
								col_val = "'"+sub_ch_nodeList.item(1).getTextContent()+"'";
								}
								else if(!col_name.contains(sub_ch_nodeList.item(0).getTextContent())){
								////WriteLog("inside if if else if");
											col_name = col_name+","+sub_ch_nodeList.item(0).getTextContent();
											col_val = col_val+",'"+sub_ch_nodeList.item(1).getTextContent()+"'";
								}
							}
							
						}
						else if(tag_notused.contains(ch_nodeList.item(ch_len).getNodeName())){
							
							//WriteLog("this tag not to be passed: "+ch_nodeList.item(ch_len).getNodeName());
						}
						else{
							////WriteLog("inside else"+col_name);
							////WriteLog("inside else col val"+col_val);
							
							if(col_name.equalsIgnoreCase("")){
								////WriteLog("inside else if");
								col_name = ch_nodeList.item(ch_len).getNodeName();
								col_val = "'"+ch_nodeList.item(ch_len).getTextContent()+"'";
							}
							else if(!col_name.contains(ch_nodeList.item(ch_len).getNodeName())){
							//WriteLog("inside else else if"+ch_nodeList.item(ch_len).getNodeName());
							//WriteLog("inside else else if"+ch_nodeList.item(ch_len).getTextContent());
											col_name = col_name+","+ch_nodeList.item(ch_len).getNodeName();
											//WriteLog("inside else col_name"+col_name);
											col_val = col_val+",'"+ch_nodeList.item(ch_len).getTextContent()+"'";
											//WriteLog("inside else col_name"+col_val);
										}
							
						}
						
					}
					if(!col_name.equalsIgnoreCase(""))
						//WriteLog("inside else col_name "+col_name+" id "+id+" col_val "+col_val);
						tagValuesMap.put(id, col_name+"~"+col_val);	
						//WriteLog("tagValuesMap inside loop"+tagValuesMap);
						
			}
			
		    } catch (Exception e) {
		    	System.out.println("Exception occured in getTagDataParent_deep method: "+ e.getMessage());
				e.printStackTrace();
			//WriteLog("Exception occured in getTagDataParent method:  "+e.getMessage());
		    }
		    return tagValuesMap;
  }*/
  
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
			    	System.out.println("Exception occured in getTagDataParent_cardInstallment_header method: "+ e.getMessage());
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
		WriteLog("inside get_loanDesc return loan_desc:  "+loan_desc);
		return "'"+loan_desc+"'";
	}
	//ended
	  
%>

