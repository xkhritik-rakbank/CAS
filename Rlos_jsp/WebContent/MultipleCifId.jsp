<%@ include file="Log.process"%>
<jsp:useBean id="wDSession" class="com.newgen.wfdesktop.session.WDSession" scope="session"/>
<jsp:useBean id="WDCabinet" class="com.newgen.wfdesktop.baseclasses.WDCabinetInfo" scope="session"/>
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

<script language="javascript" src="/webdesktop/resources/scripts/workdesk.js"></script>

<%
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader ("Expires", 0);
	
	String wi_name = request.getParameter("wi_name");
	String req_name = request.getParameter("req_name");
	String appServerType = wDSession.getM_objCabinetInfo().getM_strAppServerType();
	String wrapperIP = wDSession.getM_objCabinetInfo().getM_strServerIP();
	String wrapperPort = wDSession.getM_objCabinetInfo().getM_strServerPort()+"";    
	String sessionId = wDSession.getM_objUserInfo().getM_strSessionId();
	String cabinetName = wDSession.getM_objCabinetInfo().getM_strCabinetName();

	WriteLog("wi_name jsp: result: "+wi_name);
	WriteLog("req_name jsp: result: "+req_name);
	
	String outputResponse="";
	XMLParser xmlParserData=null;
	String mainCodeData=null;
	String inputXML=null;
	String outputXML=null;
	String cifId ="";
	String tagName="";
	String subTagName="";
	String sQuery ="";
	
	
	if(req_name.equalsIgnoreCase("CARD_INSTALLMENT_DETAILS")){
					//sQuery =" SELECT cardembossnum FROM ng_RLOS_CUSTEXPOSE_CardDetails WHERE Wi_name = '"+wi_name+"' AND (CardType = 'LOC PREFERRED' or CardType = 'LOC STANDARD') ";
				sQuery ="select max(CardEmbossNum) as CardEmbossNum from ng_RLOS_CUSTEXPOSE_CardDetails  where CifId IN (select distinct cifid from ng_RLOS_CUSTEXPOSE_CardDetails where Wi_Name ='"+wi_name+"' and  (SchemeCardProd = 'LOC PREFERRED' or SchemeCardProd = 'LOC STANDARD')) and Wi_Name='"+wi_name+"' group by CifId";
				WriteLog("$$$$$$Squery for Card Installment: "+sQuery);
				}
			
			
			
		else {
				sQuery = "select CustId from ng_rlos_cif_detail where cif_wi_name ='"+wi_name+"' ";
			}	

				WriteLog("sMQuery "+sQuery);
				WriteLog("sessionId"+sessionId);
				inputXML = "<?xml version='1.0'?><APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option><Query>" + sQuery + "</Query><EngineName>" + cabinetName + "</EngineName><SessionId>" + sessionId+ "</SessionId></APSelectWithColumnNames_Input>";
				
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
				WriteLog("outputXML select CardNumbers --> "+outputXML);
				tagName = "Record";
				if(req_name.equalsIgnoreCase("CARD_INSTALLMENT_DETAILS")){
					subTagName = "CardEmbossNum";
				}
				else{	
					subTagName = "CustId";
				}
				cifId = commonParse(outputXML,tagName,subTagName);
				WriteLog("commonParse select Cif --> "+cifId);
				outputResponse= cifId;	
				WriteLog("commonParse select Cif -- outputResponse> "+outputResponse);
			
			out.clear();
			out.println(outputResponse);
%>
<%!

	public static String commonParse(String parseXml,String tagName,String subTagName)
	{
		WriteLog("commonParse jsp: inside: ");
		String [] valueArr= null;
		String cifValues = "";
		
		WriteLog("tagName jsp: commonParse: "+tagName);
		WriteLog("subTagName jsp: commonParse: "+subTagName);
		
		
		 Map<Integer, String> tagValuesMap= new LinkedHashMap<Integer, String>();		 
		 tagValuesMap=getTagDataParent(parseXml,tagName,subTagName);
		
		 Map<Integer, String> map = tagValuesMap;
		  for (Map.Entry<Integer, String> entry : map.entrySet())
		  {
			  valueArr=entry.getValue().split("~");
			  WriteLog( "tag values: " + entry.getValue());
			  if(cifValues.equalsIgnoreCase(""))
				  cifValues = valueArr[1];
			  else
				  cifValues =cifValues+","+valueArr[1];
				WriteLog( "tcifValues" + cifValues);
				
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



