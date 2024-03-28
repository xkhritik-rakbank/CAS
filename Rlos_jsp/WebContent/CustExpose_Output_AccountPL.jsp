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
<%@ page import="org.w3c.dom.Document"%>

<script language="javascript"
	src="/webdesktop/resources/scripts/workdesk.js"></script>

<%
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader ("Expires", 0);
	String result = request.getParameter("result");
	String wi_name = request.getParameter("wi_name");
	String accNo = request.getParameter("accNo").trim();
	String parentWiName = request.getParameter("parentWiName");
	WriteLog("CustExpose_Output jsp: result: "+result);
	String appServerType = wDSession.getM_objCabinetInfo().getM_strAppServerType();
	String wrapperIP = wDSession.getM_objCabinetInfo().getM_strServerIP();
	String wrapperPort = wDSession.getM_objCabinetInfo().getM_strServerPort()+"";    
	String sessionId = wDSession.getM_objUserInfo().getM_strSessionId();
	String cabinetName = wDSession.getM_objCabinetInfo().getM_strCabinetName();
	
	WriteLog("wrapperIP jsp: result: "+wrapperIP);
	WriteLog("wrapperPort jsp: result: "+wrapperPort);
	WriteLog("sessionId jsp: result: "+sessionId);
	WriteLog("cabinetName jsp: result: "+cabinetName);
	WriteLog("wi_name jsp: result: "+wi_name);
	String outputXMLMsg = "";
	try
	{		
		if(result.indexOf("<MQ_RESPONSE_XML>")>-1)
		{
			outputXMLMsg=result.substring(result.indexOf("<MQ_RESPONSE_XML>")+17,result.indexOf("</MQ_RESPONSE_XML>"));
			WriteLog("$$outputXMLMsg "+outputXMLMsg);
			accNo =accNo.trim();
			getOutputXMLValues(outputXMLMsg,appServerType,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,accNo,parentWiName);
		}
	}
	catch(Exception e)
	{            
		WriteLog("Exception occured in valueSetCustomer method:  "+e.getMessage());
	}
	
%>

<%!

	public static void getOutputXMLValues(String parseXml,String appServerType,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name, String accNo, String parentWiName)
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
			if(response.equalsIgnoreCase("ACCOUNT_SUMMARY"))
			{
				parseAccountSummary(response,parseXml,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,appServerType,accNo,parentWiName);
			}
			if(response.equalsIgnoreCase("CARD_SUMMARY"))
			{
				parseCardSummary(response,parseXml,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,appServerType,accNo,parentWiName);
			}			
		}
		catch(Exception e)
        {            
            WriteLog("Exception occured in getOutputXMLValues method:  "+e.getMessage());
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
		subTagName = "CardNumber,CardType,CardStatus,ExpiryDate,TotalCreditLimit,TotalAmtDue,OverDueAmt,OutstandingBalance";
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
			  
			  //columnValues = valueArr[1].spilt(",");
			 // columnValues=columnValues+",'"+getCellData(SheetName1, rCnt, cCnt)+"'";
			  colValue = "'"+valueArr[1].replaceAll("[,]", "','")+"'";

			  String [] colValuearr = colValue.split(",");
			  
				  cardNo = (colValuearr[0] !=null)?colValuearr[0]:"";
				  WriteLog("cardNo received in response: "+ cardNo);
				  columnName = "CardEmbossNum,CardType,CardStatus,ExpiryDate,CreditLimit,OverdueAmt,OverdueAmount,OutstandingAmt";
				 sWhere="Wi_Name='"+parentWiName+"'  AND CardEmbossNum="+cardNo;
			 
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
					tagValuesMap.put(temp+1, subTagDerivedvalue+"~"+value+"~"+uNode.getNodeName());
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
  
  public static void commonParseParentKey(String parseXml,String tagName,String wi_name,String response,String sTableName,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String appServerType, String sParentTagName, String subTagName, String cifId)
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
			
			if(detailType.equalsIgnoreCase("LoanDetails"))
			{
				sTableName="ng_RLOS_CUSTEXPOSE_LoanDetails";
				if(response.equalsIgnoreCase("InternalExposure"))
				{
				  if(keyDtType.equalsIgnoreCase("LoanApprovedDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];					
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("LoanMaturityDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				}
				if(response.equalsIgnoreCase("ExternalExposure"))
				{
				  if(keyDtType.equalsIgnoreCase("LoanApprovedDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("LoanMaturityDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				    if(keyDtType.equalsIgnoreCase("MaxOverdueAmountDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				}
				if(response.equalsIgnoreCase("CollectionsSummary"))
				{
				  if(keyDtType.equalsIgnoreCase("LoanApprovedDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("LoanMaturityDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  
				}
			 }
			if(detailType.equalsIgnoreCase("CardDetails"))
			{
				sTableName="ng_RLOS_CUSTEXPOSE_CardDetails";
				if(response.equalsIgnoreCase("InternalExposure"))
				{
				  if(keyDtType.equalsIgnoreCase("ApplicationCreationDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("ExpiryDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				}
				if(response.equalsIgnoreCase("ExternalExposure"))
				{
				  if(keyDtType.equalsIgnoreCase("MaxOverDueAmountDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				}
			 }
			if(detailType.equalsIgnoreCase("AcctDetails"))
			{
				sTableName="ng_RLOS_CUSTEXPOSE_AcctDetails";
				if(response.equalsIgnoreCase("InternalExposure"))
				{
				  if(keyDtType.equalsIgnoreCase("AccountOpenDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("LimitSactionDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("LimitExpiryDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				}
				if(response.equalsIgnoreCase("ExternalExposure"))
				{
				  if(keyDtType.equalsIgnoreCase("StartDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("ClosedDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("MaxOverdueAmtDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				}
			 }
			 if(detailType.equalsIgnoreCase("ServicesDetails"))
			{
				sTableName="NG_RLOS_CUSTEXPOSE_ServicesDetails";
				if(response.equalsIgnoreCase("ExternalExposure"))
				{
				  if(keyDtType.equalsIgnoreCase("SubscriptionDt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("SvcExpDt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
					
				  }
				}
				
			 }		  			  
			  
		  }
	}
	
	public static void commonParseParentAmt(String parseXml,String tagName,String wi_name,String response,String sTableName,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String appServerType, String sParentTagName, String subTagName, String cifId)
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
			
			if(detailType.equalsIgnoreCase("LoanDetails"))
			{
				sTableName="ng_RLOS_CUSTEXPOSE_LoanDetails";
				if(response.equalsIgnoreCase("InternalExposure"))
				{
				  if(keyDtType.equalsIgnoreCase("TotalLoanAmount"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];					
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("LoanDisbursementAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("TotalOutstandingAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("OverdueAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("MortgagesValue"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("NextInstallmentAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				}
				if(response.equalsIgnoreCase("ExternalExposure"))
				{
				  if(keyDtType.equalsIgnoreCase("TotalAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("OutstandingAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				    if(keyDtType.equalsIgnoreCase("PaymentsAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				     if(keyDtType.equalsIgnoreCase("OverdueAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				     if(keyDtType.equalsIgnoreCase("MaximumOvrdueAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				}
				if(response.equalsIgnoreCase("CollectionsSummary"))
				{
				  if(keyDtType.equalsIgnoreCase("OutstandingAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("ProductAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("NextInstallmentAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("OverdueAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  
				}
			 }
			if(detailType.equalsIgnoreCase("CardDetails"))
			{
				sTableName="ng_RLOS_CUSTEXPOSE_CardDetails";
				if(response.equalsIgnoreCase("InternalExposure"))
				{
				  if(keyDtType.equalsIgnoreCase("CreditLimit"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("OutstandingAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("OverdueAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("CurrMaxUtil"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				}
				if(response.equalsIgnoreCase("ExternalExposure"))
				{
				  if(keyDtType.equalsIgnoreCase("CurrentBalance"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("CashLimit"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("OverdueAmount"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("MaxOverdueAmount"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				}
			 }
			if(detailType.equalsIgnoreCase("AcctDetails"))
			{
				sTableName="ng_RLOS_CUSTEXPOSE_AcctDetails";
				if(response.equalsIgnoreCase("InternalExposure"))
				{
				  if(keyDtType.equalsIgnoreCase("AvailableBalance"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("ClearBalanceAmount"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("LedgerBalance"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("LoanDisbursementAmount"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("OverdueAmount"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("EffectiveAvailableBalance"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("CumulativeDebitAmount"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("CurOutstandingAmount"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				}
				if(response.equalsIgnoreCase("ExternalExposure"))
				{
				  if(keyDtType.equalsIgnoreCase("CashLimit"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("OverdueAmount"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("MaxOverdueAmount"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				}
			 }
			 if(detailType.equalsIgnoreCase("ServicesDetails"))
			{
				sTableName="NG_RLOS_CUSTEXPOSE_ServicesDetails";
				if(response.equalsIgnoreCase("ExternalExposure"))
				{
				  if(keyDtType.equalsIgnoreCase("OverDue"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+response+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					WriteLog( "valueArr[2] sWhere" + sWhere);
					WriteLog( "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,response,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
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

%>

