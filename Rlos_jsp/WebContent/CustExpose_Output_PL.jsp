<%@ include file="Log.process"%>
<jsp:useBean id="wDSession"
	class="com.newgen.wfdesktop.session.WDSession" scope="session" />
<jsp:useBean id="WDCabinet"
	class="com.newgen.wfdesktop.baseclasses.WDCabinetInfo" scope="session" />
<%@ page import="ISPack.CPISDocumentTxn"%>
<%@ page import="ISPack.ISUtil.*"%>
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
<%@ page import="org.w3c.dom.Document"%>

<script language="javascript"
	src="/webdesktop/resources/scripts/workdesk.js"></script>

<%
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader ("Expires", 0);
	WriteLog("CustExpose_Output jsp: result: in custexpose");
	String result = request.getParameter("result");
	WriteLog("CustExpose_Output jsp: result: "+result);
	String wi_name = request.getParameter("wi_name");
	WriteLog("CustExpose_Output jsp: wi_name: "+wi_name);
	String prod = request.getParameter("prod");
	WriteLog("CustExpose_Output jsp: prod: "+prod);
	String subprod = request.getParameter("subprod");
	WriteLog("CustExpose_Output jsp: subprod: "+subprod);
	String cifId = request.getParameter("cifId").trim();
	WriteLog("CustExpose_Output jsp: cifId: "+cifId);
	
	
	String parentWiName = request.getParameter("parentWiName");
	String row_count = request.getParameter("row_count"); 
	WriteLog("CustExpose_Output jsp: row_count:"+row_count);
	String CardNumber = request.getParameter("CardNumber");
	WriteLog("CustExpose_Output jsp: result: "+result);
	WriteLog("CustExpose_Output jsp: row_count:123 "+row_count);
	String appServerType = wDSession.getM_objCabinetInfo().getM_strAppServerType();
	String wrapperIP = wDSession.getM_objCabinetInfo().getM_strServerIP();
	String wrapperPort = wDSession.getM_objCabinetInfo().getM_strServerPort()+"";    
	String sessionId = wDSession.getM_objUserInfo().getM_strSessionId();
	String cabinetName = wDSession.getM_objCabinetInfo().getM_strCabinetName();
	
	WriteLog("appServerType jsp: result: "+appServerType);
	WriteLog("wrapperIP jsp: result: "+wrapperIP);
	WriteLog("wrapperPort jsp: result: "+wrapperPort);
	WriteLog("sessionId jsp: result: "+sessionId);
	WriteLog("cabinetName jsp: result: "+cabinetName);
	WriteLog("wi_name jsp: result: "+wi_name);
	WriteLog("prod jsp: result: "+prod);
	WriteLog("subprod jsp: result: "+subprod);
	WriteLog("parentWiName jsp: result: "+parentWiName);
	String outputXMLMsg = "";
	String flagOp="";
	try
	{		
		if(result.indexOf("<MQ_RESPONSE_XML>")>-1)
		{
			outputXMLMsg=result.substring(result.indexOf("<MQ_RESPONSE_XML>")+17,result.indexOf("</MQ_RESPONSE_XML>"));
			WriteLog("$$outputXMLMsg "+outputXMLMsg);
			cifId =cifId.trim();
		 flagOp = getOutputXMLValues(outputXMLMsg,appServerType,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,prod,subprod,cifId,parentWiName,CardNumber);
			WriteLog("wrapperIP jsp: parseInternalExposure:row_count "+row_count+",result"+result);
			WriteLog("wrapperIP jsp: parseInternalExposure:flagOp "+flagOp);
		}
	}
	catch(Exception e)
	{            
		WriteLog("Exception occured in valueSetCustomer method:  "+e.getMessage());
	}
	
	out.clear();
	out.println(flagOp+"@#"+row_count);
	WriteLog("wrapperIP jsp: parseInternalExposure:flagOp "+flagOp+"and row_count"+row_count);
%>

<%!

	public static String getOutputXMLValues(String parseXml,String appServerType,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name, String prod, String subprod, String cifId, String parentWiName,String CardNumber)
	{
		WriteLog("wrapperIP jsp:getOutputXMLValues result: "+wrapperIP);
		WriteLog("wrapperPort jsp:getOutputXMLValues result: "+wrapperPort);
		WriteLog("sessionId jsp: getOutputXMLValuesresult: "+sessionId);
		WriteLog("cabinetName jsp:getOutputXMLValues result: "+cabinetName);
		WriteLog("wi_name jsp: getOutputXMLValuesresult: "+wi_name);
		WriteLog("appServerType jsp:getOutputXMLValues result: "+appServerType);
		WriteLog("parseXml jsp: getOutputXMLValuesresult: "+parseXml);
		WriteLog("cifId jsp: getOutputXMLValuesresult: "+cifId);
		WriteLog("parentWiName jsp: getOutputXMLValuesresult: "+parentWiName);
		WriteLog("CardNumber jsp: getOutputXMLValuesresult: "+CardNumber);
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
				WriteLog("RLOSCommon valueSetCustomer"+ outputXMLHead);
			}
			if(outputXMLHead.indexOf("<MsgFormat>")>-1)
			{
				response= outputXMLHead.substring(outputXMLHead.indexOf("<MsgFormat>")+11,outputXMLHead.indexOf("</MsgFormat>"));
				WriteLog("$$response "+response);
			}
			if(outputXMLHead.indexOf("<ReturnDesc>")>-1)
			{
				returnDesc= outputXMLHead.substring(outputXMLHead.indexOf("<ReturnDesc>")+12,outputXMLHead.indexOf("</ReturnDesc>"));
				WriteLog("$$returnDesc "+returnDesc);
			}
			if(outputXMLHead.indexOf("<ReturnCode>")>-1)
			{
				returnCode= outputXMLHead.substring(outputXMLHead.indexOf("<ReturnCode>")+12,outputXMLHead.indexOf("</ReturnCode>"));
				WriteLog("$$returnCode "+returnCode);
			}
			if(parseXml.indexOf("<RequestType>")>-1)
			{
				returnType= parseXml.substring(parseXml.indexOf("<RequestType>")+13,parseXml.indexOf("</RequestType>"));
				WriteLog("$$returnType "+returnType);
				if(returnType.equalsIgnoreCase("InternalExposure"))
				{
					result_str=parseInternalExposure(returnType,parseXml,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,appServerType,prod,subprod,cifId,parentWiName);
					WriteLog("cifId jsp: getOutputXMLValuesresult:InternalExpFlag "+result_str);
				}
				if(returnType.equalsIgnoreCase("ExternalExposure"))
				{
					result_str=parseExternalExposure(returnType,parseXml,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,appServerType,prod,subprod,cifId,parentWiName);
					WriteLog("cifId jsp: getOutputXMLValuesresult:ExternalExpFlag "+result_str);
				}
				if(returnType.equalsIgnoreCase("CollectionsSummary"))
				{
					result_str=parseCollectionSummary(returnType,parseXml,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,appServerType,prod,subprod,cifId,parentWiName);
				}
			}

				//added
			if(parseXml.indexOf("<MsgFormat>")>-1)
			{
				returnType= parseXml.substring(parseXml.indexOf("<MsgFormat>")+11,parseXml.indexOf("</MsgFormat>"));
				WriteLog("$$MsgFormat "+returnType);
				if(returnType.equalsIgnoreCase("CARD_INSTALLMENT_DETAILS"))
				{
					WriteLog("cifId jsp: getOutputXMLValuesresult:CardInstallmentDetailsFlag inside card installment123");
					result_str=parseCardInstallmentsDetails(returnType,parseXml,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,appServerType,prod,subprod,cifId,CardNumber,parentWiName);
					WriteLog("cifId jsp: getOutputXMLValuesresult:CardInstallmentDetailsFlag "+result_str);
				}
			}
			//ended
			
			
			if(parseXml.indexOf("<OperationType>")>-1)
			{
				returnType= parseXml.substring(parseXml.indexOf("<OperationType>")+15,parseXml.indexOf("</OperationType>"));
				WriteLog("$$returnType "+returnType);
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
			
			//ended
			
		}
		catch(Exception e)
        {            
            WriteLog("Exception occured in getOutputXMLValues method:  "+e.getMessage());
        }
		return (result_str);
	}

%>
<%!
	public static String parseInternalExposure(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String prod, String subprod, String cifId, String parentWiName)
	{
		String flag1="";
		WriteLog("wrapperIP jsp: parseInternalExposure: "+wrapperIP);
		WriteLog("wrapperPort jsp: parseInternalExposure: "+wrapperPort);
		WriteLog("sessionId jsp: parseInternalExposure: "+sessionId);
		WriteLog("cabinetName jsp: parseInternalExposure: "+cabinetName);
		WriteLog("wi_name jsp: parseInternalExposure: "+wi_name);
		WriteLog("appServerType jsp: parseInternalExposure: "+appServerType);
		WriteLog("parseXml jsp: parseInternalExposure: "+parseXml);
		WriteLog("returnType jsp: parseInternalExposure: "+returnType);
		WriteLog("cifId jsp: parseInternalExposure: "+cifId);
		WriteLog("parentWiName jsp: parseInternalExposure: "+parentWiName);
		
		String tagName="";
		String subTagName="";
		String sTableName="";
		String sParentTagName="";
		String result="";
		
		tagName="LoanDetails"; 
		subTagName = "KeyDt,AmountDtls,DelinquencyInfo";
		sTableName="ng_RLOS_CUSTEXPOSE_LoanDetails";
		flag1=commonParseProduct(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,parentWiName);
		
		if(flag1.equalsIgnoreCase("true")){
		
		tagName="CardDetails";
		subTagName = "KeyDt,AmountDtls,DelinquencyInfo";
		sTableName="ng_RLOS_CUSTEXPOSE_CardDetails";
		flag1=commonParseProduct(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,parentWiName);
		
			if(flag1.equalsIgnoreCase("true")){
				tagName="AcctDetails";
				subTagName = "KeyDt,AmountDtls,DelinquencyInfo";
				sTableName="ng_RLOS_CUSTEXPOSE_AcctDetails";
				flag1=commonParseProduct(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,parentWiName);
				}
			else{
				flag1="false";
			}
		}
		else
		{
			flag1="false";
		}
		return flag1;

		
		
		
		
		
	}
	public static String parseExternalExposure(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String prod, String subprod, String cifId, String parentWiName)
	{
		String ReturnCode="";
		
		if(parseXml.indexOf("<ReturnCode>")>-1)
		{
			ReturnCode= parseXml.substring(parseXml.indexOf("<ReturnCode>")+12,parseXml.indexOf("</ReturnCode>"));
			WriteLog("$$return Code "+ReturnCode);
		}
		
		if(ReturnCode.equalsIgnoreCase("B003"))
		{
			WriteLog("AECB:No record found!!");
			return "B003";
		}
		
		String flag1="";
		WriteLog("wrapperIP jsp: parseExternalExposure: "+wrapperIP);
		WriteLog("wrapperPort jsp: parseExternalExposure: "+wrapperPort);
		WriteLog("sessionId jsp: parseExternalExposure: "+sessionId);
		WriteLog("cabinetName jsp: parseExternalExposure: "+cabinetName);
		WriteLog("wi_name jsp: parseExternalExposure: "+wi_name);
		WriteLog("appServerType jsp: parseExternalExposure: "+appServerType);
		WriteLog("parseXml jsp: parseExternalExposure: "+parseXml);
		WriteLog("returnType jsp: parseExternalExposure: "+returnType);
		WriteLog("cifId jsp: parseExternalExposure: "+cifId);
		WriteLog("parentWiName jsp: parseExternalExposure: "+parentWiName);
				
		String tagName="";
		String subTagName="";
		String sTableName="";
		String sParentTagName="";
		String result="";
		
		tagName="ChequeDetails"; 
		subTagName = "";
		sTableName="nr_rlos_cust_extexpo_ChequeDetails";
		flag1 = commonParseProduct(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,parentWiName);
		WriteLog("cifId jsp: parseExternalExposure: updated or inserted"+flag1);
		
		if(flag1.equalsIgnoreCase("true")){
			tagName="LoanDetails"; 
			subTagName = "KeyDt,AmountDtls";
			sTableName="ng_rlos_cust_extexpo_LoanDetails";
			flag1 = commonParseProduct(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,parentWiName);
			WriteLog("cifId jsp: parseExternalExposure: updated or inserted1"+flag1);
		
			if(flag1.equalsIgnoreCase("true")){
				tagName="CardDetails"; 
				subTagName = "KeyDt,AmountDtls,DelinquencyInfo";
				sTableName="ng_rlos_cust_extexpo_CardDetails";
				flag1 = commonParseProduct(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,parentWiName);
				WriteLog("cifId jsp: parseExternalExposure: updated or inserted2"+flag1);
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
	public static String parseCollectionSummary(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String prod, String subprod, String cifId, String parentWiName)
	{
		WriteLog("wrapperIP jsp: parseCollectionSummary: "+wrapperIP);
		WriteLog("wrapperPort jsp: parseCollectionSummary: "+wrapperPort);
		WriteLog("sessionId jsp: parseCollectionSummary: "+sessionId);
		WriteLog("cabinetName jsp: parseCollectionSummary: "+cabinetName);
		WriteLog("wi_name jsp: parseCollectionSummary: "+wi_name);
		WriteLog("appServerType jsp: parseCollectionSummary: "+appServerType);
		WriteLog("parseXml jsp: parseCollectionSummary: "+parseXml);
		WriteLog("returnType jsp: parseCollectionSummary: "+returnType);
		WriteLog("cifId jsp: parseCollectionSummary: "+cifId);
		WriteLog("parentWiName jsp: parseCollectionSummary: "+parentWiName);
		
		String tagName="";
		String subTagName="";
		String sTableName="";
		String sParentTagName="";
		String result="";
		String flag1="";
		
		tagName="LoanDetails"; 
		subTagName = "KeyDt,AmountDtls,DelinquencyInfo";
		sTableName="ng_RLOS_CUSTEXPOSE_LoanDetails";
		flag1=commonParseProduct_collection(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,parentWiName);
		
		if(flag1.equalsIgnoreCase("true")){
			tagName="CardDetails";
			subTagName = "KeyDt,AmountDtls,DelinquencyInfo";
			sTableName="ng_RLOS_CUSTEXPOSE_CardDetails";
			flag1=commonParseProduct_collection(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,parentWiName);
		
		}
		else{
			flag1="false";
		}
		return flag1;
	}
	
		//added
	public static String parseCardInstallmentsDetails(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String prod, String subprod, String cifId,String CardNumber, String parentWiName)
	{
		WriteLog("wrapperIP jsp: parseCardInstallmentsDetails: "+wrapperIP);
		WriteLog("wrapperPort jsp: parseCardInstallmentsDetails: "+wrapperPort);
		WriteLog("sessionId jsp: parseCardInstallmentsDetails: "+sessionId);
		WriteLog("cabinetName jsp: parseCardInstallmentsDetails: "+cabinetName);
		WriteLog("wi_name jsp: parseCardInstallmentsDetails: "+wi_name);
		WriteLog("appServerType jsp: parseCardInstallmentsDetails: "+appServerType);
		WriteLog("parseXml jsp: parseCardInstallmentsDetails: "+parseXml);
		WriteLog("returnType jsp: parseCardInstallmentsDetails: "+returnType);
		WriteLog("cifId jsp: parseCardInstallmentsDetails: "+cifId);
		WriteLog("cifId jsp: parseCardInstallmentsDetails:CardNumber "+CardNumber);

		String tagName="";
		String subTagName="";
		String sTableName="";
		String sParentTagName="";
		String result="";
		String flag1="";


		tagName="TransactionDetailsRec"; 
		subTagName = "";
		sTableName="ng_RLOS_CUSTEXPOSE_CardInstallmentDetails";
		flag1=commonParseFinance_CardInstallment(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,parentWiName,CardNumber);

		WriteLog("wrapperIP jsp: CardInstallmentDetailsResponse: "+flag1);


		WriteLog("wrapperIP jsp: CardInstallmentDetailsResponse final value: "+flag1);
		return flag1;
	}
	//ended
	
	
	public static String parseTRANSUM(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String cifId, String parentWiName)
	{
		WriteLog("wrapperIP jsp: parseTRANSUM: "+wrapperIP);
		WriteLog("wrapperPort jsp: parseTRANSUM: "+wrapperPort);
		WriteLog("sessionId jsp: parseTRANSUM: "+sessionId);
		WriteLog("cabinetName jsp: parseTRANSUM: "+cabinetName);
		WriteLog("wi_name jsp: parseTRANSUM: "+wi_name);
		WriteLog("appServerType jsp: parseTRANSUM: "+appServerType);
		WriteLog("parseXml jsp: parseTRANSUM: "+parseXml);
		WriteLog("returnType jsp: parseTRANSUM: "+returnType);
		WriteLog("cifId jsp: parseTRANSUM: "+cifId);
		
		String tagName="";
		String subTagName="";
		String sTableName="";
		String sParentTagName="";
		String flag1="";
		
		
		tagName= "TxnSummaryDtls";		
		subTagName= "";
		sTableName="ng_rlos_FinancialSummary_TxnSummary";
		flag1=commonParseFinance(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,parentWiName);
		if(flag1.equalsIgnoreCase("true")){
			flag1="true";
		}
		else{
			flag1="false";
		}
	return flag1;
	}
	public static String parseAVGBALDET(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String cifId, String parentWiName)
	{
		WriteLog("wrapperIP jsp: parseAVGBALDET: "+wrapperIP);
		WriteLog("wrapperPort jsp: parseAVGBALDET: "+wrapperPort);
		WriteLog("sessionId jsp: parseAVGBALDET: "+sessionId);
		WriteLog("cabinetName jsp: parseAVGBALDET: "+cabinetName);
		WriteLog("wi_name jsp: parseAVGBALDET: "+wi_name);
		WriteLog("appServerType jsp: parseAVGBALDET: "+appServerType);
		WriteLog("parseXml jsp: parseAVGBALDET: "+parseXml);
		WriteLog("returnType jsp: parseAVGBALDET: "+returnType);
		WriteLog("cifId jsp: parseAVGBALDET: "+cifId);
		
		String flag1="";
		String tagName="";
		String subTagName="";
		String sTableName="";
		String sParentTagName="";
		
		
		tagName= "FinancialSummaryRes";		
		subTagName= "AvgBalanceDtls";
		sTableName="ng_rlos_FinancialSummary_AvgBalanceDtls";
		flag1=commonParseFinance(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,parentWiName);
		if(flag1.equalsIgnoreCase("true")){
			flag1="true";
		}
		else{
			flag1="false";
		}
	return flag1;
	}
	public static String parseRETURNDET(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String cifId, String parentWiName)
	{
		WriteLog("wrapperIP jsp: parseRETURNDET: "+wrapperIP);
		WriteLog("wrapperPort jsp: parseRETURNDET: "+wrapperPort);
		WriteLog("sessionId jsp: parseRETURNDET: "+sessionId);
		WriteLog("cabinetName jsp: parseRETURNDET: "+cabinetName);
		WriteLog("wi_name jsp: parseRETURNDET: "+wi_name);
		WriteLog("appServerType jsp: parseRETURNDET: "+appServerType);
		WriteLog("parseXml jsp: parseRETURNDET: "+parseXml);
		WriteLog("returnType jsp: parseRETURNDET: "+returnType);
		WriteLog("cifId jsp: parseRETURNDET: "+cifId);
		
		String tagName="";
		String subTagName="";
		String sTableName="";
		String sParentTagName="";
		String flag1="";
		
		tagName= "ReturnsDtls";		
		subTagName= "";
		sTableName="ng_rlos_FinancialSummary_ReturnsDtls";
		flag1=commonParseFinance(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,parentWiName);
		if(flag1.equalsIgnoreCase("true")){
			flag1="true";
		}
		else{
			flag1="false";
		}
	return flag1;
	
	}
	public static String parseLIENDET(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String cifId, String parentWiName)
	{
		WriteLog("wrapperIP jsp: parseLIENDET: "+wrapperIP);
		WriteLog("wrapperPort jsp: parseLIENDET: "+wrapperPort);
		WriteLog("sessionId jsp: parseLIENDET: "+sessionId);
		WriteLog("cabinetName jsp: parseLIENDET: "+cabinetName);
		WriteLog("wi_name jsp: parseLIENDET: "+wi_name);
		WriteLog("appServerType jsp: parseLIENDET: "+appServerType);
		WriteLog("parseXml jsp: parseLIENDET: "+parseXml);
		WriteLog("returnType jsp: parseLIENDET: "+returnType);
		WriteLog("cifId jsp: parseLIENDET: "+cifId);
		
		String tagName="";
		String subTagName="";
		String sTableName="";
		String sParentTagName="";
		String flag1="";
		
		tagName= "LienDetails";		
		subTagName= "";
		sTableName="ng_rlos_FinancialSummary_LienDetails";
		flag1=commonParseFinance(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,parentWiName);
		if(flag1.equalsIgnoreCase("true")){
			flag1="true";
		}
		else{
			flag1="false";
		}
	return flag1;
	}
	public static String parseSIDET(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String cifId, String parentWiName)
	{
		WriteLog("wrapperIP jsp: parseSIDET: "+wrapperIP);
		WriteLog("wrapperPort jsp: parseSIDET: "+wrapperPort);
		WriteLog("sessionId jsp: parseSIDET: "+sessionId);
		WriteLog("cabinetName jsp: parseSIDET: "+cabinetName);
		WriteLog("wi_name jsp: parseSIDET: "+wi_name);
		WriteLog("appServerType jsp: parseSIDET: "+appServerType);
		WriteLog("parseXml jsp: parseSIDET: "+parseXml);
		WriteLog("returnType jsp: parseSIDET: "+returnType);
		WriteLog("cifId jsp: parseSIDET: "+cifId);
		WriteLog("parentWiName jsp: parseSIDET: "+parentWiName);
		
		String tagName="";
		String subTagName="";
		String sTableName="";
		String sParentTagName="";
		String flag1="";
		
		tagName= "SIDetails";		
		subTagName= "";
		sTableName="ng_rlos_FinancialSummary_SiDtls";
		flag1=commonParseFinance(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,parentWiName);
		if(flag1.equalsIgnoreCase("true")){
				flag1="true";
			}
			else{
				flag1="false";
			}
	return flag1;
		
	}
	public static String parseSALDET(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String cifId, String parentWiName)
	{
		WriteLog("wrapperIP jsp: parseSALDET: "+wrapperIP);
		WriteLog("wrapperPort jsp: parseSALDET: "+wrapperPort);
		WriteLog("sessionId jsp: parseSALDET: "+sessionId);
		WriteLog("cabinetName jsp: parseSALDET: "+cabinetName);
		WriteLog("wi_name jsp: parseSALDET: "+wi_name);
		WriteLog("appServerType jsp: parseSALDET: "+appServerType);
		WriteLog("parseXml jsp: parseSALDET: "+parseXml);
		WriteLog("returnType jsp: parseSALDET: "+returnType);
		WriteLog("cifId jsp: parseSALDET: "+cifId);
		
		String tagName="";
		String subTagName="";
		String sTableName="";
		String sParentTagName="";
		String flag1="";
		
		
		tagName= "SalDetails";		
		subTagName= "";
		sTableName="ng_rlos_FinancialSummary_SalTxnDetails";
		flag1=commonParseFinance(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,parentWiName);
		WriteLog("return flag1 jsp: parseSALDET: "+flag1);
		return flag1;
	}
	public static String commonParse(String parseXml,String tagName,String wi_name,String returnType,String sTableName,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String appServerType, String subTagName, String cifId, String parentWiName)
	{
		String retVal = "";
		WriteLog("commonParse jsp: inside: ");
		String [] valueArr= null;
		String strInputXml="";
		String strOutputXml="";
		String columnName = "";
		String columnValues = "";
		String tagNameU = "";
		String subTagNameU = "";
		String subTagNameU_2 = "";
		String mainCode = "";
		String OutputCode = "";
		String sWhere = "";
		String row_updated = "";
		
		WriteLog("tagName jsp: commonParse: "+tagName);
		WriteLog("subTagName jsp: commonParse: "+subTagName);
		
		
		 Map<Integer, String> tagValuesMap= new LinkedHashMap<Integer, String>();		 
		 tagValuesMap=getTagDataParent(parseXml,tagName,subTagName);
		
		 Map<Integer, String> map = tagValuesMap;
	  String colValue="";
		  for (Map.Entry<Integer, String> entry : map.entrySet())
		  {
			  valueArr=entry.getValue().split("~");
			  WriteLog( "tag values" + entry.getValue());
			  
			  //columnValues = valueArr[1].spilt(",");
			 // columnValues=columnValues+",'"+getCellData(SheetName1, rCnt, cCnt)+"'";
			  colValue = "'"+valueArr[1].replaceAll("[,]", "','")+"'";
			  columnName = valueArr[0]+",Child_Wi";
			  columnValues = colValue+",'"+wi_name+"'";
			  		  
			  WriteLog( "columnName commonParse" + columnName);
			  WriteLog( "columnValues commonParse" + columnValues);
 			  String[] columnValues_arr = columnValues.split(",");
			  if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_CardDetails")){
				   sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND CardEmbossNum = "+columnValues_arr[0];
			  }
			  else if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_LoanDetails")){
				  sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+columnValues_arr[0];
			  }
				  else{
			   sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"'";
			  }

			  strInputXml =	ExecuteQuery_APUpdate(sTableName,columnName,columnValues,sWhere,cabinetName,sessionId);
				WriteLog( "strInputXml update " + strInputXml);
				  try 
					{
						strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);
						
						WriteLog("CustExpose_Output jsp: strOutputXml update: "+strOutputXml);
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
					WriteLog("getTagValue select mainCode --> "+mainCode);
					if(!mainCode.equalsIgnoreCase("0") || row_updated.equalsIgnoreCase("0"))
					{
					  strInputXml =	ExecuteQuery_APInsert(sTableName,columnName,columnValues,cabinetName,sessionId);
					  WriteLog( "strInputXml" + strInputXml);
					  try 
						{
							strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);
							
							WriteLog("CustExpose_Output jsp: strOutputXml: "+strOutputXml);
							mainCode = getTagValue(strOutputXml,"APInsert_Output",subTagNameU);
							if(!mainCode.equalsIgnoreCase("0"))
							{
								retVal = "false";
								WriteLog("CustExpose_Output jsp: commonParse true for insert: "+retVal);
							}
							else
							{
								retVal = "true";
								WriteLog("CustExpose_Output jsp: commonParse false for insert: "+retVal);
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
					WriteLog("CustExpose_Output jsp: commonParse true for update: "+retVal);
				}
		  }
		   WriteLog("CustExpose_Output jsp: commonParse true for final return value: "+retVal);
		  return retVal;
		 
	}
	
	public static String commonParseFinance_CardInstallment(String parseXml,String tagName,String wi_name,String returnType,String sTableName,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String appServerType, String subTagName,String parentWiName, String CardNumber)
	{
		WriteLog("commonParseFinance jsp: inside: ");
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
		WriteLog("tagName jsp: commonParseFinance: "+tagName);
		WriteLog("subTagName jsp: commonParseFinance: "+subTagName);
		WriteLog("sTableName jsp: commonParseFinance: "+sTableName);
		
		
		if((returnType.equalsIgnoreCase("CARD_INSTALLMENT_DETAILS")&& parseXml.contains("TransactionDetailsRec")))
		{
			
		WriteLog("returnType jsp: commonParseFinance: "+returnType);
		 Map<String, String> tagValuesMap= new LinkedHashMap<String, String>();		 
		 tagValuesMap=getTagDataParent_deep(parseXml,tagName,subTagName);
		
		 Map<String , String> map = tagValuesMap;
		String colValue="";
		  for (Map.Entry<String, String> entry : map.entrySet())
		  {
			  valueArr=entry.getValue().split("~");
			  for(int i=0;i<valueArr.length;i++)
			  {
				WriteLog( "tag values:12345 " +valueArr[i]);
			}
			  WriteLog( "tag values: " + entry.getValue());
			   WriteLog( "Key values: " + entry.getKey());
			   
			   colValue = "'"+valueArr[1].replaceAll("[,]", "','")+"'";
			  
			  
			  //added
			   if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_CardInstallmentDetails")){
				  WriteLog( "Inside commonParseFinance for ng_RLOS_CUSTEXPOSE_CardInstallmentDetails");
				  String header_info = getTagDataParent_cardInstallment_header(parseXml,"CardInstallmentDetailsResponse","CIFID,CardCRNNumber,CardSerialNumber,OTBAmount,TotalExposureAmount,TotalRepaymentAmount,InstallmentAccountStatus");
				  
				  WriteLog("Inside commonParseFinance for ng_RLOS_CUSTEXPOSE_CardInstallmentDetails header info: "+ header_info);
				  String [] header_info_arr = header_info.split(":");
				  
				  columnName = valueArr[0]+",Wi_Name,Child_Wi,Request_Type,"+header_info_arr[0];
				  columnValues = valueArr[1]+",'"+parentWiName+"','"+wi_name+"','CARD_INSTALLMENT_DETAILS',"+header_info_arr[1];
				  String columnName_arr[] = columnName.split(",");
				  String columnValues_arr[] = columnValues.split(",");
					columnValues="";
				  for(int i=0;i<columnName_arr.length;i++)
				  {
					WriteLog("Inside Card Installment for loop to remove I:"+columnName_arr[i]);
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
					WriteLog("Inside Cardinstallment: columnName after merging:"+columnValues);

				 // sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND TxnSerialNum = '"+entry.getKey()+"' ";
					sWhere="Wi_Name='"+parentWiName+"' AND Child_Wi='"+wi_name+"' AND Request_Type='"+returnType+"' AND TxnSerialNum = "+txnNum+"";
				
				   WriteLog("sWhere of cardinstallmentDetails"+sWhere);
				}
			  //ended
			 
			  else{
				columnName = valueArr[0]+",Wi_Name,Request_Type";
				columnValues = colValue+",'"+wi_name+"','"+returnType+"'";  
			  }
			  
			  
			  WriteLog( "columnName commonParse123" + columnName);
			  WriteLog( "columnValues commonParse456" + columnValues);
			  
			 strInputXml =	ExecuteQuery_APUpdate(sTableName,columnName,columnValues,sWhere,cabinetName,sessionId);
				WriteLog( "strInputXml update for finance " + strInputXml);
				  try 
					{
						strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);
						
						WriteLog("CustExpose_Output jsp: strOutputXml update:123 "+strOutputXml);
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
					WriteLog("getTagValue select mainCode123 --> "+mainCode);
					WriteLog("getTagValue select mainCode123 --> "+row_updated);
					if(!mainCode.equalsIgnoreCase("0") || row_updated.equalsIgnoreCase("0"))
					{
						  strInputXml =	ExecuteQuery_APInsert(sTableName,columnName,columnValues,cabinetName,sessionId);
						  WriteLog( "strInputXml123Installment insert Query:" + strInputXml);
						  try 
							{
								strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);
								
								WriteLog("CustExpose_Output jsp: strOutputXml:1234 "+strOutputXml);
								WriteLog("CustExpose_Output jsp: strOutputXml:mainCode value "+mainCode);
								mainCode = getTagValue(strOutputXml,"APInsert_Output",subTagNameU);
								WriteLog("CustExpose_Output jsp: strOutputXml:mainCode value1234 "+mainCode);
								if(!mainCode.equalsIgnoreCase("0"))
								{
									retVal = "false";
									WriteLog("CustExpose_Output jsp: commonparseproduct:false "+retVal);
								}
								else
								{
									retVal = "true";
									WriteLog("CustExpose_Output jsp: commonparseproduct:true "+retVal);
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
						WriteLog("CustExpose_Output jsp: commonparseproductapupdate:true "+retVal);
					}
		  }
			
		}
		else{
			WriteLog("returnType jsp: commonParseFinance Empty tag : "+returnType+" Wi_Name: "+wi_name);
		}
		WriteLog("CustExpose_Output jsp: final value for financial summary "+retVal);
			return retVal;
	}
	
	
	
	public static void parsePrimary_CIF(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String cifId, String parentWiName)
	{
		WriteLog("wrapperIP jsp: parsePrimary_CIF: "+wrapperIP);
		WriteLog("wrapperPort jsp: parsePrimary_CIF: "+wrapperPort);
		WriteLog("sessionId jsp: parsePrimary_CIF: "+sessionId);
		WriteLog("cabinetName jsp: parsePrimary_CIF: "+cabinetName);
		WriteLog("wi_name jsp: parsePrimary_CIF: "+wi_name);
		WriteLog("appServerType jsp: parsePrimary_CIF: "+appServerType);
		WriteLog("parseXml jsp: parsePrimary_CIF: "+parseXml);
		WriteLog("returnType jsp: parsePrimary_CIF: "+returnType);
		WriteLog("cifId jsp: parsePrimary_CIF: "+cifId);
		
		String tagName="";
		String subTagName="";
		String sTableName="";
		String sParentTagName="";
		
		
		tagName= "FinancialSummaryRes";		
		subTagName= "BankId,CIFID,AcctId,OperationDesc,TxnSummary";
		sTableName="ng_rlos_FinancialSummary_tags";
		commonParseFinance(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,parentWiName);
		
	}
	public static void parseCorporation_CIF(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String cifId, String parentWiName)
	{
		WriteLog("wrapperIP jsp: parseCorporation_CIF: "+wrapperIP);
		WriteLog("wrapperPort jsp: parseCorporation_CIF: "+wrapperPort);
		WriteLog("sessionId jsp: parseCorporation_CIF: "+sessionId);
		WriteLog("cabinetName jsp: parseCorporation_CIF: "+cabinetName);
		WriteLog("wi_name jsp: parseCorporation_CIF: "+wi_name);
		WriteLog("appServerType jsp: parseCorporation_CIF: "+appServerType);
		WriteLog("parseXml jsp: parseCorporation_CIF: "+parseXml);
		WriteLog("returnType jsp: parseCorporation_CIF: "+returnType);
		WriteLog("cifId jsp: parseCorporation_CIF: "+cifId);
		
		String tagName="";
		String subTagName="";
		String sTableName="";
		String sParentTagName="";
		
		
		tagName= "FinancialSummaryRes";		
		subTagName= "BankId,CIFID,AcctId,OperationDesc,TxnSummary";
		sTableName="ng_rlos_FinancialSummary_tags";
		commonParseFinance(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,parentWiName);
		
		
		
	}
	
	
	//added
	
	
	public static String commonParseFinance(String parseXml,String tagName,String wi_name,String returnType,String sTableName,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String appServerType, String subTagName, String parentWiName)
	{
		WriteLog("commonParseFinance jsp: inside: ");
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
		
		
		if((returnType.equalsIgnoreCase("RETURNDET")&& parseXml.contains("ReturnsDtls"))||(returnType.equalsIgnoreCase("AVGBALDET")&& parseXml.contains("AcctId"))||(returnType.equalsIgnoreCase("LIENDET")&& parseXml.contains("LienDetails"))||(returnType.equalsIgnoreCase("SIDET")&& parseXml.contains("SIDetails"))||(returnType.equalsIgnoreCase("TRANSUM")&& parseXml.contains("TxnSummary"))||(returnType.equalsIgnoreCase("SALDET")&& parseXml.contains("SalDetails")))
		{
			
		WriteLog("returnType jsp: commonParseFinance: "+returnType);
		 Map<String, String> tagValuesMap= new LinkedHashMap<String, String>();		 
		 tagValuesMap=getTagDataParent_deep(parseXml,tagName,subTagName);
		
		 Map<String , String> map = tagValuesMap;
		String colValue="";
		  for (Map.Entry<String, String> entry : map.entrySet())
		  {
			  valueArr=entry.getValue().split("~");
			WriteLog( "tag values:1234 " +valueArr);
			  WriteLog( "tag values: " + entry.getValue());
			  
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
			  else if(sTableName.equalsIgnoreCase("ng_rlos_FinancialSummary_ReturnsDtls")){
				  WriteLog( "Inside commonParseFinance for ng_rlos_FinancialSummary_ReturnsDtls");
				  String header_info = getTagDataParent_financ_header(parseXml,"FinancialSummaryRes","CIFID,AcctId,OperationType");
				  WriteLog( "Inside commonParseFinance for ng_rlos_FinancialSummary_ReturnsDtls header info: "+ header_info);
				  String [] header_info_arr = header_info.split(":");
				  String columnName_arr[] = header_info_arr[0].split(",");
				  String columnValues_arr[] = header_info_arr[1].split(",");
				  id = columnValues_arr[Arrays.asList(columnName_arr).indexOf("AcctId")];
				  columnName = valueArr[0]+",Wi_Name,Child_Wi,"+header_info_arr[0];
				  columnValues = valueArr[1]+",'"+parentWiName+"','"+wi_name+"',"+header_info_arr[1];
				  sWhere="Child_Wi='"+wi_name+"' AND OperationType='"+returnType+"' AND AcctId = "+id;
				  //strInputXml =	ExecuteQuery_APdelete(sTableName,sWhere,cabinetName,sessionId);
				WriteLog( "strInputXml delete returndtls " + strInputXml);
				 /*try 
					{
						strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);
						
						WriteLog("CustExpose_Output jsp: strOutputXml delete returndtls: "+strOutputXml);
					} 
					catch (NGException e) 
					{
					   e.printStackTrace();
					} 
					catch (Exception ex) 
					{
					   ex.printStackTrace();
					}*/
				  
			  }

			  
			  
			  
			  
			  else if(sTableName.equalsIgnoreCase("ng_rlos_FinancialSummary_LienDetails")){
				    WriteLog( "Inside commonParseFinance for ng_rlos_FinancialSummary_LienDetails");
				  String header_info = getTagDataParent_financ_header(parseXml,"FinancialSummaryRes","CIFID,AcctId,OperationType");
				  WriteLog( "Inside commonParseFinance for ng_rlos_FinancialSummary_LienDetails header info: "+ header_info);
				  String [] header_info_arr = header_info.split(":");
				  columnName = valueArr[0]+",Wi_Name,Child_Wi,"+header_info_arr[0];
				  columnValues = valueArr[1]+",'"+parentWiName+"','"+wi_name+"',"+header_info_arr[1];
				 
				  String columnName_arr[] = columnName.split(",");
				  String columnValues_arr[] = columnValues.split(",");
				  id = columnValues_arr[Arrays.asList(columnName_arr).indexOf("AcctId")];
				  sWhere="Child_Wi='"+wi_name+"' AND OperationType='"+returnType+"' AND AcctId = "+id;
				  strInputXml =	ExecuteQuery_APdelete(sTableName,sWhere,cabinetName,sessionId);
					WriteLog( "strInputXml delete returndtls " + strInputXml);
				  try 
					{
						strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);
						
						WriteLog("CustExpose_Output jsp: strOutputXml delete returndtls: "+strOutputXml);
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
				    WriteLog( "Inside commonParseFinance for ng_rlos_FinancialSummary_TxnSummary");
				  String header_info = getTagDataParent_financ_header(parseXml,"FinancialSummaryRes","CIFID,AcctId,OperationType");
				  WriteLog( "Inside commonParseFinance for ng_rlos_FinancialSummary_TxnSummary header info: "+ header_info);
				  String [] header_info_arr = header_info.split(":");
 				  columnName = valueArr[0]+",Wi_Name,Child_Wi,"+header_info_arr[0];
				  columnValues = valueArr[1]+",'"+parentWiName+"','"+wi_name+"',"+header_info_arr[1];
 				  String columnName_arr[] =columnName.split(",");
				  String columnValues_arr[] = columnValues.split(",");
				  id = columnValues_arr[Arrays.asList(columnName_arr).indexOf("AcctId")];
				  String Month = columnValues_arr[Arrays.asList(columnName_arr).indexOf("Month")];
				  sWhere="Child_Wi='"+wi_name+"' AND OperationType='"+returnType+"' AND AcctId = "+id+" and Month = "+Month+"";
				  strInputXml =	ExecuteQuery_APdelete(sTableName,sWhere,cabinetName,sessionId);
					WriteLog( "strInputXml delete returndtls " + strInputXml);
				 /* try 
					{
						strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);
						
						WriteLog("CustExpose_Output jsp: strOutputXml delete returndtls: "+strOutputXml);
					} 
					catch (NGException e) 
					{
					   e.printStackTrace();
					} 
					catch (Exception ex) 
					{
					   ex.printStackTrace();
					}*/
			  }
				 
				 





		 
 				else if(sTableName.equalsIgnoreCase("ng_rlos_FinancialSummary_SalTxnDetails")){
				WriteLog( "Inside commonParseFinance for ng_rlos_FinancialSummary_SalTxnDetails");
				  String header_info = getTagDataParent_financ_header(parseXml,"FinancialSummaryRes","CifId,AcctId,OperationType");
				  String [] header_info_arr = header_info.split(":");
 				  columnName = valueArr[0]+",Wi_Name,Child_Wi,"+header_info_arr[0];
				  columnValues = valueArr[1]+",'"+parentWiName+"','"+wi_name+"',"+header_info_arr[1];
				 
				  String columnName_arr[] = columnName.split(",");
				  String columnValues_arr[] = columnValues.split(",");
				  id = columnValues_arr[Arrays.asList(columnName_arr).indexOf("AcctId")];
				  String SalCreditDate = columnValues_arr[Arrays.asList(columnName_arr).indexOf("SalCreditDate")];
				  sWhere="Child_Wi='"+wi_name+"' AND OperationType='"+returnType+"' AND AcctId = "+id+" and SalCreditDate = "+SalCreditDate+"";
				  //strInputXml =	ExecuteQuery_APdelete(sTableName,sWhere,cabinetName,sessionId);
					WriteLog( "strInputXml delete returndtls " + strInputXml);
				 /* try 
					{
						strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);
						
						WriteLog("CustExpose_Output jsp: strOutputXml delete returndtls: "+strOutputXml);
					} 
					catch (NGException e) 
					{
					   e.printStackTrace();
					} 
					catch (Exception ex) 
					{
					   ex.printStackTrace();
					}*/
			  }
			  else if(sTableName.equalsIgnoreCase("ng_rlos_FinancialSummary_SiDtls")){
					  String header_info = getTagDataParent_financ_header(parseXml,"FinancialSummaryRes","CifId,AcctId,OperationType");
					  String [] header_info_arr = header_info.split(":");
					  String columnName_arr[] = header_info_arr[0].split(",");
					  String columnValues_arr[] = header_info_arr[1].split(",");
					  id = columnValues_arr[Arrays.asList(columnName_arr).indexOf("AcctId")];
					  columnName = valueArr[0]+",Wi_Name,Child_Wi,"+header_info_arr[0];
					  columnValues = valueArr[1]+",'"+parentWiName+"','"+wi_name+"',"+header_info_arr[1];
					  sWhere="Child_Wi='"+wi_name+"' AND OperationType='"+returnType+"' AND AcctId = "+id;
					  strInputXml =	ExecuteQuery_APdelete(sTableName,sWhere,cabinetName,sessionId);
						WriteLog( "strInputXml delete returndtls " + strInputXml);
					  try 
						{
							strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);
							
							WriteLog("CustExpose_Output jsp: strOutputXml delete returndtls: "+strOutputXml);
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
				columnName = valueArr[0]+",Wi_Name,Child_Wi,Request_Type";
				columnValues = colValue+",'"+parentWiName+"','"+wi_name+"','"+returnType+"'";  
			  }
			  
			  
			  WriteLog( "columnName commonParse" + columnName);
			  WriteLog( "columnValues commonParse" + columnValues);
			  
			 strInputXml =	ExecuteQuery_APUpdate(sTableName,columnName,columnValues,sWhere,cabinetName,sessionId);
				WriteLog( "strInputXml update " + strInputXml);
				  try 
					{
						strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);
						
						WriteLog("CustExpose_Output jsp: strOutputXml update: "+strOutputXml);
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
					WriteLog("getTagValue select mainCode --> "+mainCode);
					WriteLog("getTagValue select mainCode --> "+row_updated);
					if(!mainCode.equalsIgnoreCase("0") || row_updated.equalsIgnoreCase("0"))
					{
						  strInputXml =	ExecuteQuery_APInsert(sTableName,columnName,columnValues,cabinetName,sessionId);
						  WriteLog( "strInputXml" + strInputXml);
						  try 
							{
								strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);
								
								WriteLog("CustExpose_Output jsp: strOutputXml: "+strOutputXml);
								mainCode = getTagValue(strOutputXml,tagNameU,subTagNameU);
								if(!mainCode.equalsIgnoreCase("0"))
								{
									retVal = "false";
										WriteLog("CustExpose_Output jsp: ApINsertfalse for financial summary: "+retVal);
								}
								else
								{
									retVal = "true";
										WriteLog("CustExpose_Output jsp: ApINserttrue for financial summary: "+retVal);
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
						WriteLog("CustExpose_Output jsp: commonparseproductapupdate:true "+retVal);
					}
		  }
		}
		else{
		retVal = "true";
			WriteLog("returnType jsp: commonParseFinance Empty tag : "+returnType+" Wi_Name: "+wi_name);
		}
		WriteLog("CustExpose_Output jsp: final value for financial summary "+retVal);
		return retVal;
	}
	
	
	public static String commonParseProduct(String parseXml,String tagName,String wi_name,String returnType,String sTableName,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String appServerType, String subTagName,String prod,String subprod, String cifId, String parentWiName )
	{
		String retVal = "";
		if(!parseXml.contains(tagName)){
			return "true";
		} 
		else
	{
		WriteLog("commonParse jsp: inside: ");
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
		
		WriteLog("tagName jsp: commonParse: "+tagName);
		WriteLog("subTagName jsp: commonParse: "+subTagName);
		
		
		 Map<String, String> tagValuesMap= new LinkedHashMap<String, String>();		 
		 tagValuesMap=getTagDataParent_deep(parseXml,tagName,subTagName);
		
		 Map<String, String> map = tagValuesMap;
	  String colValue="";
		  for (Map.Entry<String, String> entry : map.entrySet())
		  {
			  valueArr=entry.getValue().split("~");
			  WriteLog( "tag values" + entry.getValue());
			  
			  //columnValues = valueArr[1].spilt(",");
			 // columnValues=columnValues+",'"+getCellData(SheetName1, rCnt, cCnt)+"'";
			  colValue = "'"+valueArr[1].replaceAll("[,]", "','")+"'";
			  columnName = valueArr[0]+",Wi_Name,Request_Type,Product_Type,CardType,CifId,Child_Wi";
			  columnValues = valueArr[1]+",'"+parentWiName+"','"+returnType+"','"+prod+"','"+subprod+"','"+cifId+"','"+wi_name+"'";
			  
			   
			   
			  WriteLog( "columnName commonParse" + columnName);
			  WriteLog( "columnValues commonParse" + columnValues);
			  if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_CardDetails")){
				  columnName = valueArr[0]+",Wi_Name,Request_Type,CifId,Child_Wi";
				  columnValues = valueArr[1]+",'"+parentWiName+"','"+returnType+"','"+cifId+"','"+wi_name+"'";
				   sWhere="CardEmbossNum = '"+entry.getKey()+"' AND Child_Wi='"+wi_name+"'";
			  }
			  else if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_LoanDetails")){
				  columnName = valueArr[0]+",Wi_Name,Request_Type,Product_Type,CardType,CifId,Child_Wi";
				  columnValues = valueArr[1]+",'"+parentWiName+"','"+returnType+"','"+prod+"','"+subprod+"','"+cifId+"','"+wi_name+"'";
				  columnName =columnName.replace("OutStandingAmt","TotalOutStandingAmt");
				  sWhere="AgreementId = '"+entry.getKey()+"' AND Child_Wi='"+wi_name+"'";
			  }
			  else if(sTableName.equalsIgnoreCase("nr_rlos_cust_extexpo_ChequeDetails")){
					  columnName = valueArr[0]+",Wi_Name,Request_Type,CifId,Child_Wi";
					  columnValues = valueArr[1]+",'"+parentWiName+"','"+returnType+"','"+cifId+"','"+wi_name+"'";
					  sWhere="Wi_Name='"+parentWiName+"' AND AcctId = '"+entry.getKey()+"' AND Child_Wi='"+wi_name+"'";
			  }
			  else if(sTableName.equalsIgnoreCase("ng_rlos_cust_extexpo_LoanDetails")){
				   columnName = valueArr[0]+",Wi_Name,Request_Type,Product_Type,CardType,CifId,Child_Wi";
				  columnValues = valueArr[1]+",'"+parentWiName+"','"+returnType+"','"+prod+"','"+subprod+"','"+cifId+"','"+wi_name+"'";
				  columnName =columnName.replace("OutStanding Balance","OutStanding_Balance");
					  columnName =columnName.replace("LastUpdateDate","datelastupdated");
				  columnName =columnName.replace("Total Amount","Total_Amount");
				  columnName =columnName.replace("Payments Amount","Payments_Amount");
				  columnName =columnName.replace("Overdue Amount","Overdue_Amount");
				  sWhere="Wi_Name='"+parentWiName+"' AND AgreementId = '"+entry.getKey()+"' AND Child_Wi='"+wi_name+"'";
			  }
			  else if(sTableName.equalsIgnoreCase("ng_rlos_cust_extexpo_CardDetails")){
				  columnName = valueArr[0]+",Wi_Name,Request_Type,Product_Type,sub_product_type,CifId,Child_Wi";
				  columnValues = valueArr[1]+",'"+parentWiName+"','"+returnType+"','"+prod+"','"+subprod+"','"+cifId+"','"+wi_name+"'";
				  sWhere="Wi_Name='"+parentWiName+"' AND CardEmbossNum = '"+entry.getKey()+"' AND Child_Wi='"+wi_name+"'";
			  }
			  else if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_AcctDetails")){
			  	 String CreditGrade = (parseXml.contains("<CreditGrade>")) ? parseXml.substring(parseXml.indexOf("<CreditGrade>")+"</CreditGrade>".length()-1,parseXml.indexOf("</CreditGrade>")):"";
				  columnName = valueArr[0]+",Wi_Name,Request_Type,CifId,Child_Wi";
				  columnValues = valueArr[1]+",'"+parentWiName+"','"+returnType+"','"+cifId+"','"+wi_name+"'";
				  sWhere="Request_Type='"+returnType+"' AND AcctId = '"+entry.getKey()+"' AND Child_Wi='"+wi_name+"'";
			  }
			else{
			   sWhere="Request_Type='"+returnType+"' AND Child_Wi='"+wi_name+"'";;
			  }
			  
			  strInputXml =	ExecuteQuery_APUpdate(sTableName,columnName,columnValues,sWhere,cabinetName,sessionId);
				WriteLog( "strInputXml update " + strInputXml);
				  try 
					{
						strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);
						
						WriteLog("CustExpose_Output jsp: strOutputXml update: "+strOutputXml);
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
					WriteLog("getTagValue select mainCode --> "+mainCode);
					WriteLog("getTagValue select mainCode --> "+row_updated);
					if(!mainCode.equalsIgnoreCase("0") || row_updated.equalsIgnoreCase("0"))
					{
						  strInputXml =	ExecuteQuery_APInsert(sTableName,columnName,columnValues,cabinetName,sessionId);
						  WriteLog( "strInputXml" + strInputXml);
						  try 
							{
								strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);
								
								WriteLog("CustExpose_Output jsp: strOutputXml: "+strOutputXml);
									mainCode = getTagValue(strOutputXml,"APInsert_Output",subTagNameU);
								if(!mainCode.equalsIgnoreCase("0"))
								{
									retVal = "false";
									WriteLog("CustExpose_Output jsp: commonparseproduct:false "+retVal);
								}
								else
								{
									retVal = "true";
									WriteLog("CustExpose_Output jsp: commonparseproduct:true "+retVal);
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
						WriteLog("CustExpose_Output jsp: commonparseproductapupdate:true "+retVal);
					}
			  
			  }
			  WriteLog("CustExpose_Output jsp: finalValue: "+retVal);
		 	 return retVal;
		}  
	}
	
	public static String commonParseProduct_collection(String parseXml,String tagName,String wi_name,String returnType,String sTableName,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String appServerType, String subTagName,String prod,String subprod, String cifId, String parentWiName )
	{
		if(!parseXml.contains(tagName)){
			return "true";
		}
		else
		{ 
			WriteLog("commonParse jsp: inside: ");
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

			WriteLog("tagName jsp: commonParse: "+tagName);
			WriteLog("subTagName jsp: commonParse: "+subTagName);


			Map<String, String> tagValuesMap= new LinkedHashMap<String, String>();		 
			tagValuesMap=getTagDataParent_deep(parseXml,tagName,subTagName);

			Map<String, String> map = tagValuesMap;
			String colValue="";
			for (Map.Entry<String, String> entry : map.entrySet())
			{
				valueArr=entry.getValue().split("~");
				WriteLog( "tag values" + entry.getValue());


				colValue = "'"+valueArr[1].replaceAll("[,]", "','")+"'";
				columnName = valueArr[0]+",Wi_Name,Request_Type,Product_Type,CardType,CifId";
				columnValues = valueArr[1]+",'"+wi_name+"','"+returnType+"','"+prod+"','"+subprod+"','"+cifId+"'";



				WriteLog( "columnName commonParse" + columnName);
				WriteLog( "columnValues commonParse" + columnValues);
				if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_CardDetails")){

					columnName = valueArr[0]+",Wi_Name,Request_Type,CifId,Child_Wi";
					columnName = columnName.replaceAll("Card_approve_date","ApplicationCreationDate");
					columnName = columnName.replaceAll("Outstanding_balance","OutstandingAmt");
					columnName = columnName.replaceAll("Credit_limit","CreditLimit");
					columnName = columnName.replaceAll("Overdue_amount","OverdueAmt");
					columnName = columnName.replaceAll("GeneralStatus","General_Status");
					columnValues = valueArr[1]+",'"+parentWiName+"','"+returnType+"','"+cifId+"','"+wi_name+"'";
					sWhere="Child_Wi='"+wi_name+"' AND CardEmbossNum = '"+entry.getKey()+"'";


				}
				else if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_LoanDetails")){
					columnName = valueArr[0]+",Wi_Name,Request_Type,Product_Type,CardType,CifId,Child_Wi";
					columnValues = valueArr[1]+",'"+parentWiName+"','"+returnType+"','"+prod+"','"+subprod+"','"+cifId+"','"+wi_name+"'";
					columnName = columnName.replaceAll("OutstandingAmt","TotalOutstandingAmt");
					sWhere="Child_Wi='"+wi_name+"' AND AgreementId = '"+entry.getKey()+"'";
				}
				else if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_AcctDetails")){
					String CreditGrade = (parseXml.contains("<CreditGrade>")) ? parseXml.substring(parseXml.indexOf("<CreditGrade>")+"</CreditGrade>".length()-1,parseXml.indexOf("</CreditGrade>")):"";
					columnName = valueArr[0]+",Wi_Name,Request_Type,CifId,CreditGrade,Child_Wi";
					columnValues = valueArr[1]+",'"+parentWiName+"','"+returnType+"','"+cifId+"','"+CreditGrade+"','"+wi_name+"'";
					sWhere="Child_Wi='"+wi_name+"' AND AcctId = '"+entry.getKey()+"'";
				}
				else{
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"'";
				}

				strInputXml =	ExecuteQuery_APUpdate(sTableName,columnName,columnValues,sWhere,cabinetName,sessionId);
				WriteLog( "strInputXml update " + strInputXml);
				try 
				{
					strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);

					WriteLog("CustExpose_Output jsp: strOutputXml update: "+strOutputXml);
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
				WriteLog("getTagValue select mainCode --> "+mainCode);
				WriteLog("getTagValue select mainCode --> "+row_updated);
				if(!mainCode.equalsIgnoreCase("0") || row_updated.equalsIgnoreCase("0"))
				{
					strInputXml =	ExecuteQuery_APInsert(sTableName,columnName,columnValues,cabinetName,sessionId);
					WriteLog( "strInputXml" + strInputXml);
					try 
					{
						strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);

						WriteLog("CustExpose_Output jsp: strOutputXml: "+strOutputXml);
						mainCode = getTagValue(strOutputXml,tagNameU,subTagNameU);
						if(!mainCode.equalsIgnoreCase("0"))
						{
							retVal = "false";
							WriteLog("CustExpose_Output jsp: ApINsertfalse for collection summary: "+retVal);
						}
						else
						{
							retVal = "true";
							WriteLog("CustExpose_Output jsp: ApINserttrue for collection summary: "+retVal);
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
					WriteLog("CustExpose_Output jsp: ApUpdatetrue for collection summary: "+retVal);
				}
			}

			WriteLog("CustExpose_Output jsp: final value for collection summary: "+retVal);
			return retVal;
		}
	}
	
	public static String commonParseParent(String parseXml,String tagName,String wi_name,String returnType,String sTableName,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String appServerType, String sParentTagName, String subTagName, String cifId, String parentWiName)
	{
		String retVal="";
		WriteLog("commonParseParent jsp: inside: ");
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
		
		WriteLog("tagName jsp: commonParseParent: "+tagName);
		WriteLog("subTagName jsp: commonParseParent: "+subTagName);
		
		 Map<Integer, String> tagValuesMap= new LinkedHashMap<Integer, String>();
		 tagValuesMap=getTagDataParent(parseXml,tagName,subTagName);
		
		 Map<Integer, String> map = tagValuesMap;
	  String colValue ="";
		  for (Map.Entry<Integer, String> entry : map.entrySet())
		  {
			  valueArr=entry.getValue().split("~");
			  WriteLog( "tag values" + entry.getValue());
			  
			  colValue = "'"+valueArr[1].replaceAll("[,]", "','")+"'";
			 
			  columnName = valueArr[0]+","+sParentTagName+",CifId,Child_Wi";
			  columnValues = colValue+",'"+valueArr[2]+"','"+cifId+"','"+wi_name+"'";
			  
			  WriteLog( "columnName commonParseParent" + columnName);
			  WriteLog( "columnValues commonParseParent" + columnValues);
			  sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
			  
			  strInputXml =	ExecuteQuery_APUpdate(sTableName,columnName,columnValues,sWhere,cabinetName,sessionId);
				WriteLog( "strInputXml update " + strInputXml);
				  try 
					{
						strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);
						
						WriteLog("CustExpose_Output jsp: strOutputXml update: "+strOutputXml);
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
					WriteLog("getTagValue select mainCode --> "+mainCode);
					if(!mainCode.equalsIgnoreCase("0") || row_updated.equalsIgnoreCase("0"))
					{	
					  strInputXml =	ExecuteQuery_APInsert(sTableName,columnName,columnValues,cabinetName,sessionId);
					  WriteLog( "strInputXml" + strInputXml);
					  try 
						{
							strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);
							
							WriteLog("CustExpose_Output jsp: strOutputXml: "+strOutputXml);
							mainCode = getTagValue(strOutputXml,"APInsert_Output",subTagNameU);
							if(!mainCode.equalsIgnoreCase("0"))
							{
								retVal = "false";
								WriteLog("CustExpose_Output jsp: commonparseparentinsert:false "+retVal);
							}					
							else
							{
								retVal = "true";
								WriteLog("CustExpose_Output jsp: commonparseparentinsert:true "+retVal);
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
						WriteLog("CustExpose_Output jsp: commonparseparentupdate:true "+retVal);
					}
		  }
		   WriteLog("CustExpose_Output jsp: final value of commonParseParent "+retVal);
		  return retVal;
	}
	
	public static Map<Integer, String> getTagDataParent(String parseXml,String tagName,String subTagName){
	  
	  Map<Integer, String> tagValuesMap= new LinkedHashMap<Integer, String>(); 
	  try {
			WriteLog("getTagDataParent jsp: parseXml: "+parseXml);
			WriteLog("getTagDataParent jsp: tagName: "+tagName);
			WriteLog("getTagDataParent jsp: subTagName: "+subTagName);
		    //InputStream is = new FileInputStream(parseXml);
			 InputStream is = new ByteArrayInputStream(parseXml.getBytes());
		    WriteLog("getTagDataParent jsp: strOutputXml: "+is);
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
		    	
			e.printStackTrace();
			WriteLog("Exception occured in getTagDataParent method:  "+e.getMessage());
		    }
		    return tagValuesMap;
  }
  
  public static void commonParseParentKey(String parseXml,String tagName,String wi_name,String returnType,String sTableName,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String appServerType, String sParentTagName, String subTagName, String cifId, String parentWiName)
	{
		WriteLog("commonParseParent jsp: inside: ");
		String [] valueArr= null;
		String sWhere = "";
		String columnName ="";
		String colValue ="";
		String keyDtType = "";
		String [] colArr= null;
		String detailType= null;
		String id_val = "";
		 
		WriteLog("tagName jsp: commonParseParent: "+tagName);
		WriteLog("subTagName jsp: commonParseParent: "+subTagName);
		
		 Map<Integer, String> tagValuesMap= new LinkedHashMap<Integer, String>();
		 tagValuesMap=getTagDataParent(parseXml,tagName,subTagName);
		
		 Map<Integer, String> map = tagValuesMap;
	 
		  for (Map.Entry<Integer, String> entry : map.entrySet())
		  {
			  valueArr=entry.getValue().split("~");
			  WriteLog( "tag values" + entry.getValue());
			  WriteLog( "valueArr[2]" + valueArr[2]);
			  
			  detailType = valueArr[2];
			  id_val = valueArr[3];
			  
			 colArr = valueArr[1].split(",");
			 WriteLog( "colArr[0]" + colArr[0]);
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
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
				  }
				  if(keyDtType.equalsIgnoreCase("LoanMaturityDate"))
				  {
					sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0]+",Child_Wi";
					colValue = "'"+colArr[1]+"','"+wi_name+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
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
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
				  }
				   if(keyDtType.equalsIgnoreCase("LoanMaturityDate"))
				  {
					sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0]+",Child_Wi";
					colValue = "'"+colArr[1]+"','"+wi_name+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
				  }
				    if(keyDtType.equalsIgnoreCase("MaxOverdueAmountDate"))
				  {
					sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0]+",Child_Wi";
					colValue = "'"+colArr[1]+"','"+wi_name+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
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
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
				  }
				   if(keyDtType.equalsIgnoreCase("LoanMaturityDate"))
				  {
					sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0]+",Child_Wi";
					colValue = "'"+colArr[1]+"','"+wi_name+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
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
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
				  }
				  if(keyDtType.equalsIgnoreCase("ExpiryDate"))
				  {
					sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0]+",Child_Wi";
					colValue = "'"+colArr[1]+"','"+wi_name+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
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
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
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
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
				  }
				  if(keyDtType.equalsIgnoreCase("LimitSactionDate"))
				  {
					sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0]+",Child_Wi";
					colValue = "'"+colArr[1]+"','"+wi_name+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
				  }
				  if(keyDtType.equalsIgnoreCase("LimitExpiryDate"))
				  {
					sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0]+",Child_Wi";
					colValue = "'"+colArr[1]+"','"+wi_name+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
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
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
				  }
				  if(keyDtType.equalsIgnoreCase("ClosedDate"))
				  {
					sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0]+",Child_Wi";
					colValue = "'"+colArr[1]+"','"+wi_name+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
				  }
				  if(keyDtType.equalsIgnoreCase("MaxOverdueAmtDate"))
				  {
					sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0]+",Child_Wi";
					colValue = "'"+colArr[1]+"','"+wi_name+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
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
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
				  }
				  if(keyDtType.equalsIgnoreCase("SvcExpDt"))
				  {
					sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0]+",Child_Wi";
					colValue = "'"+colArr[1]+"','"+wi_name+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
					
				  }
				}
				
			 }		  			  
			  
		  }
	}
	
	public static void commonParseParentAmt(String parseXml,String tagName,String wi_name,String returnType,String sTableName,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String appServerType, String sParentTagName, String subTagName, String cifId, String parentWiName)
	{
		WriteLog("commonParseParent jsp: inside: ");
		String [] valueArr= null;
		String sWhere = "";
		String columnName ="";
		String colValue ="";
		String keyDtType = "";
		String [] colArr= null;
		String detailType= null;
		String id_val = ""; 
		 
		WriteLog("tagName jsp: commonParseParent: "+tagName);
		WriteLog("subTagName jsp: commonParseParent: "+subTagName);
		
		 Map<Integer, String> tagValuesMap= new LinkedHashMap<Integer, String>();
		 tagValuesMap=getTagDataParent(parseXml,tagName,subTagName);
		
		 Map<Integer, String> map = tagValuesMap;
	 
		  for (Map.Entry<Integer, String> entry : map.entrySet())
		  {
			  valueArr=entry.getValue().split("~");
			  WriteLog( "tag values" + entry.getValue());
			  WriteLog( "valueArr[2]" + valueArr[2]);
			  
			  detailType = valueArr[2];
			  id_val = valueArr[3];

			 colArr = valueArr[1].split(",");
			 WriteLog( "colArr[0]" + colArr[0]);
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
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
				  }
				  if(keyDtType.equalsIgnoreCase("LoanDisbursementAmt"))
				  {
					sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0]+",Child_Wi";
					colValue = "'"+colArr[1]+"','"+wi_name+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
				  }
				   if(keyDtType.equalsIgnoreCase("TotalOutstandingAmt"))
				  {
					sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0]+",Child_Wi";
					colValue = "'"+colArr[1]+"','"+wi_name+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
				  }
				   if(keyDtType.equalsIgnoreCase("OverdueAmt"))
				  {
					sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0]+",Child_Wi";
					colValue = "'"+colArr[1]+"','"+wi_name+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
				  }
				   if(keyDtType.equalsIgnoreCase("MortgagesValue"))
				  {
					sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0]+",Child_Wi";
					colValue = "'"+colArr[1]+"','"+wi_name+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
				  }
				   if(keyDtType.equalsIgnoreCase("NextInstallmentAmt"))
				  {
					sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0]+",Child_Wi";
					colValue = "'"+colArr[1]+"','"+wi_name+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
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
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
				  }
				   if(keyDtType.equalsIgnoreCase("OutstandingAmt"))
				  {
					sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0]+",Child_Wi";
					colValue = "'"+colArr[1]+"','"+wi_name+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
				  }
				    if(keyDtType.equalsIgnoreCase("PaymentsAmt"))
				  {
					sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0]+",Child_Wi";
					colValue = "'"+colArr[1]+"','"+wi_name+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
				  }
				     if(keyDtType.equalsIgnoreCase("OverdueAmt"))
				  {
					sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0]+",Child_Wi";
					colValue = "'"+colArr[1]+"','"+wi_name+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
				  }
				     if(keyDtType.equalsIgnoreCase("MaximumOvrdueAmt"))
				  {
					sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0]+",Child_Wi";
					colValue = "'"+colArr[1]+"','"+wi_name+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
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
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
				  }
				   if(keyDtType.equalsIgnoreCase("ProductAmt"))
				  {
					sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0]+",Child_Wi";
					colValue = "'"+colArr[1]+"','"+wi_name+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
				  }
				   if(keyDtType.equalsIgnoreCase("NextInstallmentAmt"))
				  {
					sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0]+",Child_Wi";
					colValue = "'"+colArr[1]+"','"+wi_name+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
				  }
				   if(keyDtType.equalsIgnoreCase("OverdueAmt"))
				  {
					sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0]+",Child_Wi";
					colValue = "'"+colArr[1]+"','"+wi_name+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
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
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
				  }
				  if(keyDtType.equalsIgnoreCase("OutstandingAmt"))
				  {
					sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0]+",Child_Wi";
					colValue = "'"+colArr[1]+"','"+wi_name+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
				  }
				   if(keyDtType.equalsIgnoreCase("OverdueAmt"))
				  {
					sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0]+",Child_Wi";
					colValue = "'"+colArr[1]+"','"+wi_name+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
				  }
				   if(keyDtType.equalsIgnoreCase("CurrMaxUtil"))
				  {
					sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0]+",Child_Wi";
					colValue = "'"+colArr[1]+"','"+wi_name+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
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
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
				  }
				  if(keyDtType.equalsIgnoreCase("CashLimit"))
				  {
					sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0]+",Child_Wi";
					colValue = "'"+colArr[1]+"','"+wi_name+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
				  }
				  if(keyDtType.equalsIgnoreCase("OverdueAmount"))
				  {
					sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0]+",Child_Wi";
					colValue = "'"+colArr[1]+"','"+wi_name+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
				  }
				  if(keyDtType.equalsIgnoreCase("MaxOverdueAmount"))
				  {
					sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0]+",Child_Wi";
					colValue = "'"+colArr[1]+"','"+wi_name+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
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
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
				  }
				  if(keyDtType.equalsIgnoreCase("ClearBalanceAmount"))
				  {
					sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0]+",Child_Wi";
					colValue = "'"+colArr[1]+"','"+wi_name+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
				  }
				  if(keyDtType.equalsIgnoreCase("LedgerBalance"))
				  {
					sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0]+",Child_Wi";
					colValue = "'"+colArr[1]+"','"+wi_name+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
				  }
				   if(keyDtType.equalsIgnoreCase("LoanDisbursementAmount"))
				  {
					sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0]+",Child_Wi";
					colValue = "'"+colArr[1]+"','"+wi_name+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
				  }
				   if(keyDtType.equalsIgnoreCase("OverdueAmount"))
				  {
					sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0]+",Child_Wi";
					colValue = "'"+colArr[1]+"','"+wi_name+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
				  }
				   if(keyDtType.equalsIgnoreCase("EffectiveAvailableBalance"))
				  {
					sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0]+",Child_Wi";
					colValue = "'"+colArr[1]+"','"+wi_name+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
				  }
				   if(keyDtType.equalsIgnoreCase("CumulativeDebitAmount"))
				  {
					sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0]+",Child_Wi";
					colValue = "'"+colArr[1]+"','"+wi_name+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
				  }
				   if(keyDtType.equalsIgnoreCase("CurOutstandingAmount"))
				  {
					sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0]+",Child_Wi";
					colValue = "'"+colArr[1]+"','"+wi_name+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
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
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
				  }
				  if(keyDtType.equalsIgnoreCase("OverdueAmount"))
				  {
					sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"' AND AcctId = "+id_val;
					columnName = colArr[0]+",Child_Wi";
					colValue = "'"+colArr[1]+"','"+wi_name+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
				  }
				  if(keyDtType.equalsIgnoreCase("MaxOverdueAmount"))
				  {
					sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"' AND AcctId = "+id_val;
					columnName = colArr[0]+",Child_Wi";
					colValue = "'"+colArr[1]+"','"+wi_name+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
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
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
				  }
				 
				}
				
			 }		  			  
			  
		  }
	}
	
	public static void commonParseParentDelinquency(String parseXml,String tagName,String wi_name,String returnType,String sTableName,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String appServerType, String sParentTagName, String subTagName, String cifId, String parentWiName)
	{
		WriteLog("commonParseParent jsp: inside: ");
		String [] valueArr= null;
		String sWhere = "";
		String columnName ="";
		String colValue ="";
		String keyDtType = "";
		String [] colArr= null;
		String detailType= null;
		 
		WriteLog("tagName jsp: commonParseParent: "+tagName);
		WriteLog("subTagName jsp: commonParseParent: "+subTagName);
		
		 Map<Integer, String> tagValuesMap= new LinkedHashMap<Integer, String>();
		 tagValuesMap=getTagDataParent(parseXml,tagName,subTagName);
		
		 Map<Integer, String> map = tagValuesMap;
	 
		  for (Map.Entry<Integer, String> entry : map.entrySet())
		  {
			  valueArr=entry.getValue().split("~");
			  WriteLog( "tag values" + entry.getValue());
			  WriteLog( "valueArr[2]" + valueArr[2]);
			  
			  detailType = valueArr[2];
			  
			 colArr = valueArr[1].split(",");
			 WriteLog( "colArr[0]" + colArr[0]);
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
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
				  }
				  if(keyDtType.equalsIgnoreCase("DPD60Last18Months"))
				  {
					sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0]+",Child_Wi";
					colValue = "'"+colArr[1]+"','"+wi_name+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
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
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
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
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
				  }
				  if(keyDtType.equalsIgnoreCase("DPD60Last18Months"))
				  {
					sWhere="Wi_Name='"+parentWiName+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0]+",Child_Wi";
					colValue = "'"+colArr[1]+"','"+wi_name+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name,parentWiName);
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

 public static String getTagValue(String parseXml,String tagName,String subTagName)
	{
		WriteLog("getTagValue jsp: inside: ");
		String [] valueArr= null;
		String mainCodeValue = "";
		
		WriteLog("tagName jsp: getTagValue: "+tagName);
		WriteLog("subTagName jsp: getTagValue: "+subTagName);
		
		
		 Map<Integer, String> tagValuesMap= new LinkedHashMap<Integer, String>();		 
		 tagValuesMap=getTagDataParent(parseXml,tagName,subTagName);
		
		 Map<Integer, String> map = tagValuesMap;
		String colValue="";
		  for (Map.Entry<Integer, String> entry : map.entrySet())
		  {
			  valueArr=entry.getValue().split("~");
			  WriteLog( "tag values" + entry.getValue());
			  mainCodeValue = valueArr[1];	
			  WriteLog( "mainCodeValue" + mainCodeValue);
				
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
	
	strInputXml =	ExecuteQuery_APUpdate(sTableName,columnName,colValue,sWhere,cabinetName,sessionId);
	WriteLog( "strInputXml update " + strInputXml);
	  try 
		{
			strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);
			
			WriteLog("CustExpose_Output jsp: strOutputXml update: "+strOutputXml);
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
		WriteLog("getTagValue select mainCode --> "+mainCode);
		if(!mainCode.equalsIgnoreCase("0") || row_updated.equalsIgnoreCase("0"))
		{
			//colValue = "'"+valueArr[1].replaceAll("[,]", "','")+"'";
			  
			  columnName = columnName+",Wi_Name,Request_Type,CifId,Child_Wi";
			  columnValues = colValue+",'"+parentWiName+"','"+returnType+"','"+cifId+"','"+wi_name+"'";
			   
			 strInputXml =	ExecuteQuery_APInsert(sTableName,columnName,columnValues,cabinetName,sessionId);
			  WriteLog( "strInputXml insert " + strInputXml);
			  try 
				{
					strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);
					
					WriteLog("CustExpose_Output jsp: strOutputXml insert: "+strOutputXml);
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
  	public static Map<String, String> getTagDataParent_deep(String parseXml,String tagName,String sub_tag){
	  
	  Map<String, String> tagValuesMap= new LinkedHashMap<String, String>(); 
	  try {
			WriteLog("getTagDataParent_deep jsp: parseXml: "+parseXml);
			WriteLog("getTagDataParent_deep jsp: tagName: "+tagName);
			WriteLog("getTagDataParent_deep jsp: subTagName: "+sub_tag);
			String tag_notused = "BankId,OperationDesc,TxnSummary,#text";
			
			 InputStream is = new ByteArrayInputStream(parseXml.getBytes());
		    WriteLog("getTagDataParent_deep jsp: strOutputXml: "+is);
		  	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();
		
			NodeList nList_loan = doc.getElementsByTagName(tagName);
			for(int i = 0 ; i<nList_loan.getLength();i++){
				String col_name = "";
				String col_val ="";
				NodeList ch_nodeList = nList_loan.item(i).getChildNodes();
				String id = ch_nodeList.item(0).getTextContent();
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
							WriteLog("this tag not to be passed: "+ch_nodeList.item(ch_len).getNodeName());
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
					WriteLog("insert/update for id: "+id);
					WriteLog("insert/update cal_name: "+col_name);
					WriteLog("insert/update col_val: "+col_val);
					if(!col_name.equalsIgnoreCase(""))
						tagValuesMap.put(id, col_name+"~"+col_val);	
			}
			
		    } catch (Exception e) {
		    	
			e.printStackTrace();
			WriteLog("Exception occured in getTagDataParent method:  "+e.getMessage());
		    }
		    return tagValuesMap;
  }
  
 public static String getTagDataParent_financ_header(String parseXml,String tagName,String sub_tag){
		String col_name = "";
		String col_val ="";
		  try {
				WriteLog("getTagDataParent_financ_header jsp: parseXml: "+parseXml);
				WriteLog("getTagDataParent_financ_header jsp: tagName: "+tagName);
				WriteLog("getTagDataParent_financ_header jsp: subTagName: "+sub_tag);
								
			    //InputStream is = new FileInputStream(parseXml);
				 InputStream is = new ByteArrayInputStream(parseXml.getBytes());
			    WriteLog("getTagDataParent_financ_header jsp: strOutputXml: "+is);
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
						WriteLog("insert/update getTagDataParent_financ_header for id: "+id);
						WriteLog("insert/update getTagDataParent_financ_header cal_name: "+col_name);
						WriteLog("insert/update getTagDataParent_financ_header col_val: "+col_val);
							
				}
				
			    } catch (Exception e) {
			    	
				e.printStackTrace();
				WriteLog("Exception occured in getTagDataParent_financ_header method:  "+e.getMessage());
			    }
			    return col_name+":"+col_val;
	}
		public static String getTagDataParent_cardInstallment_header(String parseXml,String tagName,String sub_tag){
		String col_name = "";
		String col_val ="";
		  try {
				WriteLog("getTagDataParent_cardInstallment_header jsp: parseXml: "+parseXml);
				WriteLog("getTagDataParent_cardInstallment_header jsp: tagName: "+tagName);
				WriteLog("getTagDataParent_cardInstallment_header jsp: subTagName: "+sub_tag);
								
			    //InputStream is = new FileInputStream(parseXml);
				 InputStream is = new ByteArrayInputStream(parseXml.getBytes());
			    WriteLog("getTagDataParent_cardInstallment_header jsp: strOutputXml: "+is);
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
						WriteLog("insert/update getTagDataParent_cardInstallment_header for id: "+id);
						WriteLog("insert/update getTagDataParent_cardInstallment_header cal_name: "+col_name);
						WriteLog("insert/update getTagDataParent_cardInstallment_header col_val: "+col_val);
							
				}
				
			    } catch (Exception e) {
			    	
				e.printStackTrace();
				WriteLog("Exception occured in getTagDataParent_cardInstallment_header method:  "+e.getMessage());
			    }
			    return col_name+":"+col_val;
	}
%>