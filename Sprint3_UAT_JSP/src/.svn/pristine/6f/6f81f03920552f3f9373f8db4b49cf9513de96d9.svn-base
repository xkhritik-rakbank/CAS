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
<%@ page  import= "com.newgen.custom.*, org.xml.sax.InputSource, org.w3c.dom.*, org.w3c.dom.NodeList,org.w3c.dom.Document,javax.xml.parsers.DocumentBuilder, javax.xml.parsers.DocumentBuilderFactory"%>
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
<%@ page import= "org.w3c.dom.Document"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.owasp.esapi.ESAPI"%>
<%@ page import="org.owasp.esapi.codecs.OracleCodec"%>
<%@ page import="org.owasp.esapi.User" %>

<%
String cabinetName="";
		String sessionId ="";
		String wrapperPort="";
		String wrapperIP ="";
		String appServerType="";
		String username="";
		String SqlQuery="";
		String sInputXML="";
		 String sOutputXML="";
		 String subXML="";
		 XMLParser xmlParserData=null;
		 XMLParser objXmlParser=null;
		 String mainCodeValue="";
		 int recordcount=0;
		 String noteDesc="";
		 String noteCode="";
		 Boolean isShow=false;
		 String parent_Winame= "";
		 String activityName = "";
		 
		 String sFinalString="";
		//svt points start
        String input1 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("Wi_Name"), 1000, true) );
		String wi_name = ESAPI.encoder().encodeForSQL(new OracleCodec(), input1);
		
        String input2 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("activityname"), 1000, true) ); 
	    String ws_name = ESAPI.encoder().encodeForSQL(new OracleCodec(), input2);
		//svt points end
		Boolean Tickler_Found=false;//added by akshay on 10/1/18
		WriteLog("wi_name while MarqueeNotepadSelect : "+wi_name);
		
		appServerType = wDSession.getM_objCabinetInfo().getM_strAppServerType();
		wrapperIP =    wDSession.getM_objCabinetInfo().getM_strServerIP();
		wrapperPort =  wDSession.getM_objCabinetInfo().getM_strServerPort()+"";    
		sessionId =    wDSession.getM_objUserInfo().getM_strSessionId();
		cabinetName =  wDSession.getM_objCabinetInfo().getM_strCabinetName();
		
		
		try
		{
			String procName = wi_name.substring(0,2);
			if(procName.equalsIgnoreCase("PL")){
			SqlQuery = "SELECT Parent_WIName,CURR_WSNAME FROM NG_PL_EXTTABLE with (nolock) WHERE PL_wi_name ='"+wi_name+"'";
			}
			else if(procName.equalsIgnoreCase("CC")){
			SqlQuery = "SELECT Parent_WIName,CURR_WSNAME FROM NG_CC_EXTTABLE with (nolock) where CC_Wi_Name ='"+wi_name+"'";
			}
			sInputXML = "<?xml version='1.0'?>"+
						 "<APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option>"+
						 "<Query>" + SqlQuery + "</Query>"+
						 "<EngineName>" + cabinetName + "</EngineName>"+
						 "<SessionId>" + sessionId + "</SessionId></APSelectWithColumnNames_Input>";
						 
			 WriteLog("sInputXML while MarqueeNotepadSelect : "+sInputXML);
			sOutputXML = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, sInputXML);
			WriteLog("sOutputXML while MarqueeNotepadSelect : "+sOutputXML);
		}
		catch(Exception exp) {
			WriteLog("Exception while getting data from ext table : "+exp);
			sFinalString="EXCEPTION";
		}
		xmlParserData=new XMLParser();
		xmlParserData.setInputXML((sOutputXML));
		mainCodeValue = xmlParserData.getValueOf("MainCode");
		if(mainCodeValue.equals("0"))
		{
		 recordcount=Integer.parseInt(xmlParserData.getValueOf("TotalRetrieved"));	
		 
		   if(recordcount>0){
			  // subXML = xmlParserData.getNextValueOf("Record");
			   //objXmlParser = new XMLParser(subXML);
			   //parent_Winame=objXmlParser.getValueOf("Parent_WIName");
				//activityName=objXmlParser.getValueOf("Current_ws");
				parent_Winame = xmlParserData.getNextValueOf("Parent_WIName");
				activityName = xmlParserData.getNextValueOf("Current_ws");
				
				WriteLog("parent_Winame while MarqueeNotepadSelect : "+parent_Winame);
				WriteLog("activityName while MarqueeNotepadSelect : "+activityName);
			}
		 }
			
		try
		{
			SqlQuery = "SELECT NoteDet,NoteCode FROM NG_RLOS_GR_Note1 with (nolock) WHERE (Notepad_winame ='"+wi_name+"') and ( NoteCode='tickler')";
			
			sInputXML = "<?xml version='1.0'?>"+
						 "<APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option>"+
						 "<Query>" + SqlQuery + "</Query>"+
						 "<EngineName>" + cabinetName + "</EngineName>"+
						 "<SessionId>" + sessionId + "</SessionId></APSelectWithColumnNames_Input>";
						 
			WriteLog("sInputXML while NG_RLOS_GR_Note1  MarqueeNotepadSelect : "+sInputXML);			 
			sOutputXML = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, sInputXML);
			WriteLog("sOutputXML while NG_RLOS_GR_Note1  MarqueeNotepadSelect : "+sOutputXML);
				
				
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(new InputSource(new StringReader(sOutputXML)));
         doc.getDocumentElement().normalize();
			
			mainCodeValue = doc.getElementsByTagName("MainCode").item(0).getTextContent();
			if(mainCodeValue.equals("0")){
			 recordcount=Integer.parseInt(doc.getElementsByTagName("TotalRetrieved").item(0).getTextContent());	
			 
			   if(recordcount>0){
				   subXML = xmlParserData.getNextValueOf("Record");
				   objXmlParser = new XMLParser(subXML);
				   //modified by akshay on 10/1/18 for proc 3567
				   NodeList records = doc.getElementsByTagName("Record");
				   for(int i=0;i<records.getLength();i++){
					   sFinalString+=((Element) records.item(i)).getElementsByTagName("NoteDet").item(0).getTextContent() +";";
					 if(!"Tickler".equalsIgnoreCase(((Element) records.item(i)).getElementsByTagName("NoteCode").item(0).getTextContent())){  
						//sFinalString+= ((Element) records.item(i)).getElementsByTagName("NoteCode").item(0).getTextContent() +"#"+ ((Element) records.item(i)).getElementsByTagName("NoteDesc").item(0).getTextContent()+"~";
						 sFinalString+=((Element) records.item(i)).getElementsByTagName("NoteDet").item(0).getTextContent() +";";
					
					}
					else{
						Tickler_Found=true;
					}
				   }
				   if(Tickler_Found==false){
						sFinalString="NODATA";
					}
				}
			   else
				   {
					sFinalString="NODATA";
					}
					WriteLog("sFinalString while NG_RLOS_GR_Note1  MarqueeNotepadSelect : "+sFinalString);
			 }
			 }
			 catch(Exception e)
		{		
		     WriteLog("Exception while getting data from database : "+e.getMessage());
			sFinalString="EXCEPTION";
				
		}
		out.clear();
	out.println(sFinalString);
%>