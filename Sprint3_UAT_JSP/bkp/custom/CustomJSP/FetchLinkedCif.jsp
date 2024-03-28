<%@ include file="Log.process"%>
<%@ page import ="ISPack.CPISDocumentTxn"%>
<%@ page import ="ISPack.ISUtil.*"%>
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
<%@ page import= "org.w3c.dom.Document"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.owasp.esapi.ESAPI"%>
<%@ page import="org.owasp.esapi.User" %>
<%@ page import="com.newgen.*" %>

<script language="javascript" src="/webdesktop/resources/scripts/workdesk.js"></script>

<%
	
	
	
	String appServerType = "";
	String wrapperIP = "";
	String wrapperPort = "";    
	
	String cabinetName = "";
	String params="";
	String child_wi = "";
	if(!activityName.equalsIgnoreCase("Branch_Init") && !activityName.equalsIgnoreCase("Re_Initiate")){
				
		String input1 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("child_wi"), 1000, true) );
		child_wi = ESAPI.encoder().encodeForSQL(new OracleCodec(), input1);
	}
	
	String input2 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("wi_name"), 1000, true) );
	String wi_name = ESAPI.encoder().encodeForSQL(new OracleCodec(), input2);
	
	String input3 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("SeesionId"), 1000, true) );
	String sessionId = ESAPI.encoder().encodeForSQL(new OracleCodec(), input3);
	
	String input4 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("activityName"), 1000, true) );
	String activityName = ESAPI.encoder().encodeForSQL(new OracleCodec(), input4);
	
	String input5 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("WD_UID"), 1000, true) );
	String WD_UID = ESAPI.encoder().encodeForSQL(new OracleCodec(), input5);

	
	WriteLog("wi_name jsp: result: "+wi_name);
	String outputResponse="";
	XMLParser xmlParserData=null;
	String mainCodeData=null;
	String inputXML=null;
	String outputXML=null;
	String linkedCIFs ="";
	String tagName="";
	String subTagName="";
	String sQuery="";
	
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
			WriteLog("Inside FetchLinkedCIFs---->cabinetName: "+cabinetName);
			WriteLog("wrapperIP: "+wrapperIP);
			WriteLog("wrapperPort: "+wrapperPort);
			WriteLog("appServerType: "+appServerType);

        } 
		catch(Exception e){
			
			WriteLog("Excetion Occured while reading from properties file: "+e.getMessage());
		}
			
			if(child_wi.equalsIgnoreCase(""))
			{
				 WriteLog("sMQuery "+sQuery);
				sQuery = "SELECT distinct LinkedCIFs FROM ng_RLOS_CUSTEXPOSE_AcctDetails WHERE  Wi_Name = :wi_name and LinkedCIFs is not null";
				params="wi_name=="+wi_name;
			}
			else
			{	
				sQuery = "SELECT distinct LinkedCIFs FROM ng_RLOS_CUSTEXPOSE_AcctDetails WHERE  Wi_Name = :child_wi and LinkedCIFs is not null";
				params="child_wi=="+child_wi;
			}
			  WriteLog("sMQuery "+sQuery);
				WriteLog("sessionId : "+sessionId);
				inputXML=ExecuteQuery_APSelectwithparam(sQuery,params,cabinetName,sessionId);
				//inputXML = "<?xml version='1.0'?><APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option><Query>" + sQuery + "</Query><EngineName>" + cabinetName + "</EngineName><SessionId>" + sessionId+ "</SessionId></APSelectWithColumnNames_Input>";
				
				try 
				{
					outputXML = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, inputXML);
				} 
				catch (NGException e) 
				{
				   e.printStackTrace();
				} 
				catch (Exception ex) 
				{
				   ex.printStackTrace();
				}  
				WriteLog("outputXML select linkedCIFs --> "+outputXML);
				tagName = "Record";
				subTagName = "LinkedCIFs";
				linkedCIFs = commonParse(outputXML,tagName,subTagName);
				WriteLog("commonParse select LinkedCIFs --> "+linkedCIFs);
				outputResponse= linkedCIFs;	
			
			out.clear();
			out.println(outputResponse);
%>
<%!
public static String ExecuteQuery_APSelectwithparam(String sQry,String params, String cabinetName, String sessionId)
{
String sInputXML = "<?xml version='1.0'?><APSelectWithNamedParam_Input>"
		+ "<option>APSelectWithNamedParam</option>" + "<Query>" + sQry + "</Query>" + "<Params>" + params
		+ "</Params>" + "<EngineName>" + cabinetName + "</EngineName>" + "<SessionID>" + sessionId
		+ "</SessionID>" + "</APSelectWithNamedParam_Input>";
return sInputXML;
}
public static String commonParse(String parseXml,String tagName,String subTagName)
	{
		WriteLog("commonParse jsp: inside: ");
		String [] valueArr= null;
		String ciflink = "";
		String onlyCif = "";
		
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
			  if(ciflink.equalsIgnoreCase(""))
				{
				  onlyCif = valueArr[1].substring(0,7);
				   WriteLog( "onlyCif first - " + onlyCif);
				  ciflink = onlyCif;				  
				 } 
			  else
			  {
				  onlyCif = valueArr[1].substring(0,7);
					WriteLog( "onlyCif second - " + onlyCif);
				  ciflink =ciflink+","+onlyCif;
			  }	  
				WriteLog( "ciflink : " + ciflink);
				
		  }
		  return ciflink;
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

%>



