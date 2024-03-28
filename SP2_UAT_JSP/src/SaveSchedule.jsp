<jsp:useBean id="wDSession" class="com.newgen.wfdesktop.session.WDSession" scope="session"/>
<jsp:useBean id="WDCabinet" class="com.newgen.wfdesktop.baseclasses.WDCabinetInfo" scope="session"/>

<%@ page import="com.newgen.reschedule.Payment" %>
<%@ page import="com.newgen.reschedule.PaymentFactory" %>
<%@ page import="com.newgen.bean.ResponseVO" %>
<%@ page import="com.newgen.bean.LoanSchedule" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="java.util.*"%>
<%@ page import="com.newgen.reschedule.XLSXReaderWriter" %>
<%@ page import="java.text.*" %>
<%@ page import="java.io.*"%>
<%@ page import="java.net.*"%>
<%@ include file="Log.process"%>
<%@ page import="com.newgen.wfdesktop.xmlapi.WFCallBroker" %>
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
<%@ page import= "org.w3c.dom.Document"%>
<%@ page import= " org.json.JSONObject"%>
<%@ page import= " org.json.XML"%>
<%@ page import= "org.xml.sax.InputSource"%>
<%@ page import= " java.io.StringReader"%>
<%@ page import= " java.io.StringWriter"%>



<%
	String result = request.getParameter("result");
	String wi_name = request.getParameter("wi_name");
	
	String appServerType = "";
	String wrapperIP = "";
	String wrapperPort = "";    
	String sessionId = request.getParameter("sSessionId");
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
        } 
		catch(Exception e){
			
			WriteLog("Excetion Occured while reading from properties file: "+e.getMessage());
		}
	
	String sTableName = "NG_PL_EMI_Schedule";
	String columnName = "EMI_Days,Principal,Interest,EMI,Month,EMI_DATE,Opening_Principle,Closing_Principle,Life_Insurance,Property_Insurance,Total_Repayable,Adv_Flag,Excess_Interest,Workitem_Name";
	
	String sInputXML="";
	
	
	WriteLog("Save Schedule jsp: result: "+result);
	WriteLog("Save Schedule jsp: wi_name: "+wi_name);
	WriteLog("Save Schedule jsp: sessionId: "+sessionId);
	
	
	JSONObject xmlJSONObj =null;
	try
	{

		String sXML1 = result;
		
		WriteLog("Save Schedule jsp: sXML1: "+sXML1);
		sXML1="<xml version=\"1.0\">"+sXML1.trim()+"</xml>";
		//sInputXML=getBalancedXML(sXML1.trim());
		WriteLog("Save Scheduledsgfdht jsp: sXML1: "+sXML1);
		String tagName = "data";
	    String subTagName = "daysInMonth,monthlyPrincipal,monthlyInterest,emi,month,date,openingPrinciple,closingPrinciple,lifeInsurance,propertyInsurance,totalRepayable,advFlag,excessInterest";
	    String statusTable =commonParse(sXML1,tagName,subTagName,sTableName,columnName,cabinetName,sessionId, wrapperIP, wrapperPort,  appServerType,wi_name);
		WriteLog("Save Scheduledsgfdht jsp: statusTable: "+statusTable);
		statusTable = statusTable.trim();
		
		
		out.clear();
		out.print(statusTable);
	}
	catch (Exception e) 
	{
	out.print("exception "+e.getMessage());
		e.printStackTrace();
	}
			

%>
<%!

	

	public static String commonParse(String parseXml,String tagName, String subTagName, String sTableName, String columnName,String cabinetName,String sessionId, String wrapperIP, String wrapperPort, String appServerType, String wi_name)
	{
		
		 WriteLog("commonParse jsp: inside: ");
		String [] valueArr= null;
		String strInputXml="";
		String strOutputXml="";
		String columnValues = "";
		String tableStatus="";
		
		
		 WriteLog("Save Schedule jsp:commonParse tagName: "+tagName);
		 WriteLog("Save Schedule jsp: commonParse subTagName: "+subTagName);
		
		
		 Map<Integer, String> tagValuesMap= new LinkedHashMap<Integer, String>();		 
		 tagValuesMap= getTagData(parseXml,tagName,subTagName);
		
		 Map<Integer, String> map = tagValuesMap;
	  String colValue="";
	  
			//Deepak code added to delete the repayment schedule before generating new Start -- 01April2017
				String swhere = "Workitem_Name='"+wi_name+"'";
			  strInputXml =	ExecuteQuery_APdelete(sTableName,swhere,cabinetName,sessionId);
			 
			   WriteLog("Save Schedule jsp: strInputXml: "+strInputXml);
			  try 
				{
					strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);
					
					
					  WriteLog("Save Schedule jsp: strOutputXml: "+strOutputXml);
				} 
				catch (NGException e) 
				{
					WriteLog("Exception"+e.getMessage());
				   e.printStackTrace();
				} 
				catch (Exception ex) 
				{
				   ex.printStackTrace();
				}
			  //Deepak code added to delete the repayment schedule before generating new END -- 01April2017
			  
			  
	  
		  for (Map.Entry<Integer, String> entry : map.entrySet())
		  {
			  valueArr=entry.getValue().split("~");
			
			  
			  //columnValues = valueArr[1].spilt(",");
			 // columnValues=columnValues+",'"+getCellData(SheetName1, rCnt, cCnt)+"'";
			  colValue = "'"+valueArr[1].replaceAll("[,]", "','")+"'";
			
			  columnValues = colValue;
			   columnValues = colValue+",'"+wi_name+"'";
			  			 
						 
			   WriteLog("Save Schedule jsp:commonParse columnName: "+columnName);
			   WriteLog("Save Schedule jsp:commonParse columnValues: "+columnValues);
			 
			  
			   strInputXml = ExecuteQuery_APInsert(sTableName,columnName,columnValues,cabinetName,sessionId);
			
			   WriteLog("Save Schedule jsp: strInputXml: "+strInputXml);
			  try 
				{
					strOutputXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);
					
					
					  WriteLog("Save Schedule jsp: strOutputXml: "+strOutputXml);
				} 
				catch (NGException e) 
				{
					WriteLog("Exception"+e.getMessage());
				   e.printStackTrace();
				} 
				catch (Exception ex) 
				{
				   ex.printStackTrace();
				}
				tagName = "APInsert_Output";
				subTagName = "Output";
				String  outCode = commonParseMainCode(strOutputXml,tagName,subTagName);
				WriteLog("outCode value "+ outCode);
				
				if(outCode.equalsIgnoreCase("1")) 
				{
					tableStatus = "Success";
					WriteLog("Inside success of save");
				}
				else
				{
					tableStatus = "Fail";
					WriteLog("Inside Fail of save");
				}					
				
			  //out.print(tableStatus); 
			  
		  }
		  return tableStatus;
	}
	
	public static Map<Integer, String> getTagData(String parseXml,String tagName,String subTagName)
	{	  
	  Map<Integer, String> tagValuesMap= new LinkedHashMap<Integer, String>();
	  try {
			
			//InputStream is = new FileInputStream(file);
			 InputStream is = new ByteArrayInputStream(parseXml.getBytes());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			//Document doc = dBuilder.parse();
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
					for(int j=0;j<values.length;j++){
					
						if(eElement.getElementsByTagName(values[j]).item(0) !=null){
							value=value+","+eElement.getElementsByTagName(values[j]).item(0).getTextContent();
							subTagDerivedvalue=subTagDerivedvalue+","+values[j];
						}
							
					}
					value=value.substring(1,value.length());
					subTagDerivedvalue=subTagDerivedvalue.substring(1,subTagDerivedvalue.length());
					tagValuesMap.put(temp+1, subTagDerivedvalue+"~"+value);
					value="";
					subTagDerivedvalue="";
				}
			}
			
			} catch (Exception e) {
				
			e.printStackTrace();
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
	public static String commonParseMainCode(String parseXml,String tagName,String subTagName)
	{
		WriteLog("commonParse jsp: inside: ");
		String [] valueArr= null;
		String cifValues = "";
		
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
				if(cifValues.equalsIgnoreCase(""))
				  cifValues = valueArr[1];
			  else

				  cifValues =cifValues+","+valueArr[1];
				WriteLog( "Output code" + cifValues);
				
		  }
		  return cifValues;
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
%>
