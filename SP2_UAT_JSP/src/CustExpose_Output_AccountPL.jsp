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
<%@ page import="org.w3c.dom.Document"%>

<script language="javascript"
	src="/webdesktop/custom/CustomJSP/workdesk.js"></script>

<%
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader ("Expires", 0);
	String result = request.getParameter("result");
	String wi_name = request.getParameter("wi_name");
	String accNo = request.getParameter("accNo").trim();
	String parentWiName = request.getParameter("parentWiName");
	String row_count = request.getParameter("row_count");
	WriteLog("CustExpose_Output jsp: result: "+result);
	String appServerType = "";
	String wrapperIP = "";
	String wrapperPort = "";    
	String sessionId = wDSession.getM_objUserInfo().getM_strSessionId();
	String cabinetName = "";
	
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
		if(null==row_count){
			row_count="";
		}
    } 
	catch(Exception e){
		System.out.println("Exception occured in custexpose_output_PL: "+ e.getMessage());
		e.printStackTrace();
		out.print("false");
	}
	
	String outputXMLMsg = "";
	try
	{
		String process_tablename="";
			if("PL".equalsIgnoreCase(wi_name.substring(0, 2))){
				process_tablename="NG_PL_XMLLOG_HISTORY";
			}
			else{
				process_tablename="NG_CC_XMLLOG_HISTORY";
			}
			String squery = "select OUTPUT_XML from "+process_tablename+" with (nolock) where MESSAGE_ID ='"+result+"' and WI_NAME = '"+wi_name+"'";
			WriteLog("Deepak squery:"+squery);
			String strInputXml_1 = ExecuteQuery_APSelect(squery,cabinetName,sessionId);
			WriteLog("Deepak strInputXml_1:"+strInputXml_1);
			String strOutputXml_1 = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml_1);
			WriteLog("Deepak strOutputXml_1:"+strOutputXml_1);
			String flagOp="";
			if(!"".equalsIgnoreCase(strOutputXml_1)){
				String row_count_str = strOutputXml_1.substring(strOutputXml_1.indexOf("<TotalRetrieved>")+16,strOutputXml_1.indexOf("</TotalRetrieved>"));
				int result_count = Integer.parseInt(row_count_str);
				if (result_count>0){
					if(strOutputXml_1.indexOf("<MQ_RESPONSE_XML>")>-1)
					{
						outputXMLMsg=strOutputXml_1.substring(strOutputXml_1.indexOf("<MQ_RESPONSE_XML>")+17,strOutputXml_1.indexOf("</MQ_RESPONSE_XML>"));
						WriteLog("$$outputXMLMsg custexpose_output_AccountPL "+outputXMLMsg);
						accNo =accNo.trim();
						flagOp = getOutputXMLValues(outputXMLMsg,appServerType,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,accNo,parentWiName);
						WriteLog("Deepak12 flagOp:"+flagOp);
				}
				else{
					flagOp="false";
				}
			}
			else{
				flagOp="waiting";
			}
		}
		else{
			flagOp="false";
		}
			
		WriteLog("Deepak flagOp:"+flagOp);
		out.clear();
		out.print(flagOp+"@#"+row_count);
	}
	catch(Exception e)
	{            
		WriteLog("Exception occured in valueSetCustomer method:  "+e.getMessage());
		out.clear();
		out.print("false");
	}
	
%>

<%!

	public static String getOutputXMLValues(String parseXml,String appServerType,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name, String accNo, String parentWiName)
	{
		WriteLog("wrapperIP jsp:getOutputXMLValues result: "+wrapperIP);
		WriteLog("wrapperPort jsp:getOutputXMLValues result: "+wrapperPort);
		WriteLog("sessionId jsp: getOutputXMLValuesresult: "+sessionId);
		WriteLog("cabinetName jsp:getOutputXMLValues result: "+cabinetName);
		WriteLog("wi_name jsp: getOutputXMLValuesresult: "+wi_name);
		WriteLog("appServerType jsp:getOutputXMLValues result: "+appServerType);
		WriteLog("parseXml jsp: getOutputXMLValuesresult: "+parseXml);
		WriteLog("accNo jsp: getOutputXMLValuesresult: "+accNo);
		WriteLog("parentWiName jsp: getOutputXMLValuesresult: "+parentWiName);
		
		 String outputXMLHead = "";
        String outputXMLMsg = "";
        String returnDesc = "";
        String returnCode = "";
        String response= "";
        String returnType="";
		
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
			if("0000".equalsIgnoreCase(returnCode))
			{
				if(response.equalsIgnoreCase("ACCOUNT_SUMMARY"))
				{
					parseAccountSummary(response,parseXml,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,appServerType,accNo,parentWiName);
				}
				else if(response.equalsIgnoreCase("CARD_SUMMARY"))
				{
					parseCardSummary(response,parseXml,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,appServerType,accNo,parentWiName);
				}
				return("true");
			}
			else{
				String errorQuery="SELECT isnull((SELECT alert FROM ng_MASTER_INTEGRATION_ERROR_CODE WHERE  error_code='"+returnCode+"'),(SELECT alert FROM ng_MASTER_INTEGRATION_ERROR_CODE WHERE error_code='DEFAULT')) As Alert";
				String strInputXml = ExecuteQuery_APSelect(errorQuery,cabinetName,sessionId);
				String strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);
				String result_str=strOutputXml.substring(strOutputXml.indexOf("<Alert>")+"</Alert>".length()-1,strOutputXml.indexOf("</Alert>"));
				return result_str;
				//return("false");
			}
						
		}
		catch(Exception e)
        {            
            WriteLog("Exception occured in getOutputXMLValues method:  "+e.getMessage());
            return("false");
        }
		
	}

%>
<%!
	public static void parseAccountSummary(String response,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String accNo, String parentWiName)
	{
		WriteLog("wrapperIP jsp: parseAccountSummary: "+wrapperIP);
		WriteLog("wrapperPort jsp: parseAccountSummary: "+wrapperPort);
		WriteLog("sessionId jsp: parseAccountSummary: "+sessionId);
		WriteLog("cabinetName jsp: parseAccountSummary: "+cabinetName);
		WriteLog("wi_name jsp: parseAccountSummary: "+wi_name);
		WriteLog("appServerType jsp: parseAccountSummary: "+appServerType);
		WriteLog("parseXml jsp: parseAccountSummary: "+parseXml);
		WriteLog("response jsp: parseAccountSummary: "+response);
		WriteLog("accNo jsp: parseAccountSummary: "+accNo);
		WriteLog("parentWiName jsp: parseAccountSummary: "+parentWiName);
		
		String tagName="";
		String subTagName="";
		String sTableName="";
		String sParentTagName="";
		
		tagName="IBSAccountDetail";
		subTagName = "Acid,CrnCode,AcctBal,AcctStatus,AcctOpnDt";
		sTableName="ng_RLOS_CUSTEXPOSE_LoanDetails";
		commonParseAccount(parseXml,tagName,wi_name,response,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,accNo,parentWiName);
		
		/*tagName="IBSAccountDetail";
		subTagName = "LoanAmount,MaturityDt";
		sTableName="ng_RLOS_CUSTEXPOSE_LoanDetails";
		commonParseAccount(parseXml,tagName,wi_name,response,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,accNo,parentWiName);
			*/				
	}
	public static void parseCardSummary(String response,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String cardNo, String parentWiName)
	{
		WriteLog("wrapperIP jsp: parseCardSummary: "+wrapperIP);
		WriteLog("wrapperPort jsp: parseCardSummary: "+wrapperPort);
		WriteLog("sessionId jsp: parseCardSummary: "+sessionId);
		WriteLog("cabinetName jsp: parseCardSummary: "+cabinetName);
		WriteLog("wi_name jsp: parseCardSummary: "+wi_name);
		WriteLog("appServerType jsp: parseCardSummary: "+appServerType);
		WriteLog("parseXml jsp: parseCardSummary: "+parseXml);
		WriteLog("response jsp: parseCardSummary: "+response);
		WriteLog("cardNo jsp: parseCardSummary: "+cardNo);
		WriteLog("parentWiName jsp: parseCardSummary: "+parentWiName);
				
		String tagName="";
		String subTagName="";
		String sTableName="";
		String sParentTagName="";	
		tagName="CardDetails";	
		subTagName = "OverDueAmt,OutstandingBalance,CardAsIsStatus";//changed by akshay on 28/6/18 for proc 6854
		sTableName="ng_RLOS_CUSTEXPOSE_CardDetails";
		commonParseCard(parseXml,tagName,wi_name,response,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,cardNo,parentWiName);		
	}	
	
	public static void commonParseAccount(String parseXml,String tagName,String wi_name,String response,String sTableName,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String appServerType, String subTagName, String accNo, String parentWiName)
	{
		WriteLog("commonParse jsp: inside: ");
		String [] valueArr= null;
		String strInputXml="";
		String strOutputXml="";
		String columnName = "";
		String columnValues = "";
		String tagNameU = "";
		String subTagNameU = "";
		String subTagNameO = "";		
		String mainCode = "";
		String OutputCode = "";
		String sWhere = "";
		
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
			  
			  colValue = "'"+valueArr[1].replaceAll("[,]", "','")+"'";
			  
			  String [] colValuearr = colValue.split(",");
			  
				  accNo = (colValuearr[0] !=null)?colValuearr[0]:"";
				  WriteLog("cardNo received in response: "+ accNo);
				  
			  if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_LoanDetails"))
			  {
				columnName = "AgreementId,CurCode,TotalOutstandingAmt,LoanStat,LoanApprovedDate,Child_Wi";
				sWhere="(Wi_Name='"+parentWiName+"' or Child_Wi='"+wi_name+"')  AND AgreementId="+accNo;
			}				
			  columnValues = colValue+",'"+wi_name+"'";
			  
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
					subTagNameO = "Output";
					mainCode = getTagValue(strOutputXml,tagNameU,subTagNameU);
					OutputCode = getTagValue(strOutputXml,tagNameU,subTagNameO);
					WriteLog("getTagValue select mainCode --> "+mainCode);
					if(mainCode.equalsIgnoreCase("0") && OutputCode.equalsIgnoreCase("0"))
					{				
					  WriteLog( "No Data to Update");	
					  strInputXml =	ExecuteQuery_APInsert(sTableName,columnName,columnValues,cabinetName,sessionId);
						  WriteLog( "strInputXml" + strInputXml);
						  try 
							{
								strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);
								
								WriteLog("CustExpose_Output jsp: strOutputXml: "+strOutputXml);
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
	}
	
	public static void commonParseCard(String parseXml,String tagName,String wi_name,String response,String sTableName,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String appServerType, String subTagName, String cardNo, String parentWiName)
	{
		WriteLog("commonParse jsp: inside: ");
		String [] valueArr= null;
		String strInputXml="";
		String strOutputXml="";
		String columnName = "";
		String columnValues = "";
		String tagNameU = "";
		String subTagNameU = "";
		String subTagNameO = "";		
		String mainCode = "";
		String OutputCode = "";
		String sWhere = "";
		
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
			  
			  colValue = "'"+valueArr[1].replaceAll("[,]", "','")+"'";

			  String [] colValuearr = colValue.split(",");
			  String CardAsIsStatus = colValuearr[2];
			  WriteLog("cardNo received in response: "+ CardAsIsStatus);
			  if(!"CLSD,CRWO,STWO".contains(CardAsIsStatus)){
				  cardNo = (colValuearr[0] !=null)?colValuearr[0]:"";
				  WriteLog("cardNo received in response: "+ cardNo);
				  String cardProduct=getTagValue(parseXml,"CardDetails","CardProductType");//added by akshay on 28/6/18 for proc 6854
				  String CifId=getTagValue(parseXml,"CardSummaryResponse","CIFID");//added by akshay on 28/6/18 for proc 6854
				   WriteLog("card product in response: "+ cardProduct);
				    WriteLog("cif id received in response: "+ CifId);
				  columnName = "OverdueAmt,OutstandingAmt,CardStatus";
				 sWhere="Wi_Name='"+parentWiName+"'  AND cardtype='"+cardProduct+"' and cifid='"+CifId+"'";//CardEmbossNum="+cardNo----changed by akshay on 28/6/18 for proc 6854
			 
			  columnValues = colValue+",'"+wi_name+"'";
			  
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
					subTagNameO = "Output";
					mainCode = getTagValue(strOutputXml,tagNameU,subTagNameU);
					OutputCode = getTagValue(strOutputXml,tagNameU,subTagNameO);
					WriteLog("getTagValue select mainCode --> "+mainCode);
					if(mainCode.equalsIgnoreCase("0") && OutputCode.equalsIgnoreCase("0"))
					{				
					  WriteLog( "No Data to Update");		
   				      strInputXml =	ExecuteQuery_APInsert(sTableName,columnName,columnValues,cabinetName,sessionId);
						  WriteLog( "strInputXml" + strInputXml);
						  try 
							{
								strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);
								
								WriteLog("CustExpose_Output jsp: strOutputXml: "+strOutputXml);
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
			
			  
		  }
	}

	public static Map<Integer, String> getTagDataParent(String parseXml,String tagName,String subTagName){
	  
	  Map<Integer, String> tagValuesMap= new LinkedHashMap<Integer, String>(); 
	  InputStream is = new ByteArrayInputStream(parseXml.getBytes());
	  try {
			WriteLog("getTagDataParent jsp: parseXml: "+parseXml);
			WriteLog("getTagDataParent jsp: tagName: "+tagName);
			WriteLog("getTagDataParent jsp: subTagName: "+subTagName);
		    //InputStream is = new FileInputStream(parseXml);
			 
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
					tagValuesMap.put(temp+1, subTagDerivedvalue+"~"+value+"~"+uNode.getNodeName());
					value="";
					subTagDerivedvalue="";
				}
			}
			
		    } catch (Exception e) {
		    	
			e.printStackTrace();
			WriteLog("Exception occured in getTagDataParent method:  "+e.getMessage());
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
	
	public static void updateQuery(String sTableName,String columnName,String colValue,String sWhere,String cabinetName,String sessionId,String response,String wrapperIP,String wrapperPort, String appServerType,String cifId,String wi_name)
  {
	String strInputXml="";
	String strOutputXml="";
	String mainCode="";
	String OutputCode="";
	String tagNameU="";
	String subTagNameU="";
	String subTagNameO = "";
	String columnValues="";
	
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
		subTagNameO = "Output";
		mainCode = getTagValue(strOutputXml,tagNameU,subTagNameU);
		OutputCode = getTagValue(strOutputXml,tagNameU,subTagNameO);
		WriteLog("getTagValue select mainCode --> "+mainCode);
		if(mainCode.equalsIgnoreCase("0") && OutputCode.equalsIgnoreCase("0"))
		{
			//colValue = "'"+valueArr[1].replaceAll("[,]", "','")+"'";
			  
			  columnName = columnName+",Wi_Name,Request_Type,CifId";
			  columnValues = colValue+",'"+wi_name+"','"+response+"','"+cifId+"'";
			   
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

%>

