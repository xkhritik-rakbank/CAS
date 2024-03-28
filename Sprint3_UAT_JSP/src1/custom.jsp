
<%@page import="com.newgen.mvcbeans.model.wfobjects.WFDynamicConstant"%>

<%@ page import="com.newgen.mvcbeans.model.*"%>
<%@ page import="com.newgen.mvcbeans.controller.workdesk.*"%>
<%@ page import="javax.faces.context.FacesContext"%>
<%@ page import="com.newgen.mvcbeans.controller.workdesk.*"%>
<%@ page import="java.util.LinkedHashMap"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.owasp.esapi.ESAPI"%>
<%@ page import="org.owasp.esapi.codecs.OracleCodec"%>
<%@ page import="org.owasp.esapi.User" %>
<%@ page import="com.newgen.*" %>
<%@ include file="Log.process"%>
<%@ page import="java.io.*,java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.lang.String.*"%>
<%@ page import="java.lang.Object"%>
<%@ page import="com.newgen.wfdesktop.xmlapi.*" %>
<%@ page import="com.newgen.wfdesktop.util.*" %>
<%@ page import="XMLParser.XMLParser"%>

<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.newgen.mvcbeans.model.*,javax.faces.context.FacesContext,com.newgen.mvcbeans.controller.workdesk.*"%>
<%@page import="com.newgen.omni.wf.util.excp.NGException,com.newgen.omni.wf.util.app.NGEjbClient"%>
<%@page import="org.w3c.dom.Node"%>
<%@page import="org.w3c.dom.NodeList"%>
<%@ page  import= "com.newgen.custom.*, org.w3c.dom.*, org.w3c.dom.NodeList,org.w3c.dom.Document,javax.xml.parsers.DocumentBuilder, javax.xml.parsers.DocumentBuilderFactory"%>
<%@ page  import= "org.w3c.dom.Element"%>
<%@ page import= "org.w3c.dom.Document"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.owasp.esapi.ESAPI"%>
<%@ page import="org.owasp.esapi.codecs.OracleCodec"%>
<%@ page import="org.owasp.esapi.User" %>
<%@ page import="com.newgen.*" %>

<jsp:useBean id="wDSession" class="com.newgen.wfdesktop.session.WDSession" scope="session" />
<jsp:useBean id="WDCabinet" class="com.newgen.wfdesktop.baseclasses.WDCabinetInfo" scope="session"/>

<html>
    	
		
		 <div id="logo">
        <img src="../resources/images/rak-logo.gif" class="ribbon"/>
		</div>
		
        <head>
           <link href="https://10.15.12.136:9443/omniapp/javax.faces.resource/themes.css.jsf?ln=en_us//css" rel="stylesheet" type="text/css">

			<link rel="stylesheet" href="/formviewer/resources/en_us/css/RLOS/jquery-ui.css" type="text/css" />
			<link rel="stylesheet" href="/formviewer/resources/en_us/css/RLOS/RLOS_CSS.css" type="text/css" />
			<%
			String sSessionId_1 = request.getParameter("SessionId");
			String input1 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("SessionId"), 1000, true) );
			String sSessionId = ESAPI.encoder().encodeForSQL(new OracleCodec(), input1);
			String WD_UID = wDSession.getM_strUniqueUserId();
			XMLParser objXmlParser=null;
			String cabinetName="";

			String wrapperPort="";
			String wrapperIP ="";
			String appServerType="";
			%>

			
           
<!-- esapi4js i18n resources -->
<script type="text/javascript" language="JavaScript" src="esapi4js/resources/i18n/ESAPI_Standard_en_US.properties.js"></script>
<!-- esapi4js configuration -->
<script type="text/javascript" language="JavaScript" src="esapi4js/esapi-compressed.js"></script>
<script type="text/javascript" language="JavaScript" src="esapi4js/resources/Base.esapi.properties.js"></script>
<!-- esapi4js core -->
	<script type="text/javascript"> 
			

    Base.esapi.properties.application.Name = "My Application v1.0";
    
    
    // Initialize the api
    org.owasp.esapi.ESAPI.initialize();
		            
			
			//var sSessionId = '<%=sSessionId%>';
			var WD_UID = '<%=WD_UID%>';
			var sSessionId_encode = '<%=sSessionId%>';
			var sSessionId = ($ESAPI.encoder().decodeForHTML(sSessionId_encode));
			
			
			//sSessionId = '1234567';
                // custom scripts here
                function onCustomLinkClick(op){
                    var url="";
					if(op=="1"){
                        url = "custom"+op+".jsp";
                        window.open(url,"custom"+op,'height=400,width=400,resizable=0,status=1,scrollbars=0,top=200,left=200');
                    }
					if(op=="2"){
						url="leavemgt.jsp";
						window.open(url,"leavemgt",'height=400,width=400,resizable=1,status=1,scrollbars=0,top=200,left=200');
					}
					if(op=="3"){
						url="DisplayWI.jsp?sSessionId="+sSessionId+ "&WD_UID="+WD_UID;
						window.open(url,"DisplayWI",'height=400,width=1000,resizable=1,status=1,scrollbars=0,top=200,left=200');
					}
					if(op=="5"){
						url="MurhabhaIslamicCSV.jsp?sSessionId="+sSessionId+ "&WD_UID="+WD_UID;
						window.open(url,"Murhabha File Generation",'height=400, width=1000,resizable=1,status=1,scrollbars=0,top=200,left=200');
					}

					//++ Below Code Added by nikhil for Duplicate Creation JSP
					if(op=="4"){
						url="CreateNewWorkitem.jsp";
						window.open(url,"Create New Workitem Link",'height=150 , width=400 ,resizable=1,status=1,scrollbars=0,top=200,left=200');
					}
					//-- Above Code Added by nikhil for Duplicate Creation JSP
                }
				  </script>
				
        </head>
		
        <body>
			<div>
				<table border = "1"  width = "100%" bgcolor="white">
				<%
				if (sSessionId == null) {
					  out.println("<html><title>Error</title><body><span style=\"margin-left:-9px;display: block\"><img src=\"ibps.png\"></span><label style=\"white-space: nowrap; font: bold 11pt Arial; color: #0072c6;padding-left: 580px;padding-top: 7px;display: inline-block;\" class=\"labelbluebold\">Invalid Session. Please login again.</label></body></html>");
						return;	    }
				

			
			
			try {
					String prop_file_loc=System.getProperty("user.dir") + File.separatorChar+"CustomConfig"+File.separatorChar+"ServerConfig.properties";
				    File file = new File(prop_file_loc);
		            FileInputStream fileInput = new FileInputStream(file);
		            Properties properties = new Properties();
		            properties.load(fileInput);
		            fileInput.close();
		            WriteLog("winame custom.jsp sSessionId_1 "+sSessionId_1);
		            WriteLog("winame custom.jsp sSessionId "+sSessionId);
					cabinetName = properties.getProperty("cabinetName");
					wrapperIP = properties.getProperty("wrapperIP");
					wrapperPort = properties.getProperty("wrapperPort");
					appServerType = properties.getProperty("appServerType");
					boolean check=validateJSP(sSessionId,cabinetName,wrapperIP, wrapperPort, appServerType);
					if(check==false){
						  out.println("<html><title>Error</title><body><span style=\"margin-left:-9px;display: block\"><img src=\"ibps.png\"></span><label style=\"white-space: nowrap; font: bold 11pt Arial; color: #0072c6;padding-left: 580px;padding-top: 7px;display: inline-block;\" class=\"labelbluebold\">Invalid Session. Please login again.</label></body></html>");
					return;
					}
					WriteLog("winame cabinetName "+check);
			
		        } 
				catch(Exception e){
					
					WriteLog("Excetion Occured while reading from properties file: "+e.getMessage());
				}

				%>
						<%!
			public boolean validateJSP(String sessionId,String cabinetName,String wrapperIP,String wrapperPort,String appServerType){
				String query="select sessionId from WFSESSIONVIEW where sessionId='" +sessionId+ "' and statusFlag='Y'";
				String inputXML="";
				String OutputXML="";
				String params="";
				XMLParser xmlParserSession=null;
				inputXML = "<?xml version='1.0'?>"+
				 "<APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option>"+
				 "<Query>" + query + "</Query>"+
				 "<Params>"+ params + "</Params>"+
				 "<EngineName>" + cabinetName + "</EngineName>"+
				 "<SessionId>" + sessionId + "</SessionId></APSelectWithColumnNames_Input>";
				
				//inputXML = "<?xml version='1.0'?><APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option><Query>" + query + "</Query><EngineName>" + cabinetName + "</EngineName><SessionId>" + sessionId+ "</SessionId></APSelectWithColumnNames_Input>";

				WriteLog(" properties file: "+inputXML);

				try{
					OutputXML = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, inputXML);
				}
				catch(NGException e){
					WriteLog(" properties OutputXML"+OutputXML);
				}
				catch (Exception ex) 
				{
				   ex.printStackTrace();
				}
				WriteLog("outputXML " + OutputXML);
				try{
					xmlParserSession=new XMLParser();
					xmlParserSession.setInputXML((OutputXML));
					String mainCode = xmlParserSession.getValueOf("MainCode");
					WriteLog("mainCode " + mainCode);
					int recordcount=Integer.parseInt(xmlParserSession.getValueOf("TotalRetrieved"));
					WriteLog("recordcount " + recordcount);
					if(recordcount>0){
						  return true;
					}
				}catch (Exception ex) 
				{
					   ex.printStackTrace();
				}
				return false;
			}

		%>
					<thead>
						<tr  width = "300%">
							<!--<th><span onclick="onCustomLinkClick('1')" class="linkstyle" style="margin:5px" style="font-size: 550%">Custom Link1</span></th>
							
							<th><span onclick="onCustomLinkClick('2')" class="linkstyle" style="margin:5px" style="font-size: 550%">Leave Management Module</span></th>  -->
							
							<th><span onclick="onCustomLinkClick('3')" class="linkstyle" style="margin:5px" style="font-size: 650%">CC Disbursal Bulk Auto WI</span></th>
							<!--<th><span onclick="onCustomLinkClick('4')" class="linkstyle" style="margin:5px" style="font-size: 550%">Create Duplicate Workitem</span></th>  -->
							<th><span onclick="onCustomLinkClick('5')" class="linkstyle" style="margin:5px" style="font-size: 550%">Murhabha File Generation</span></th>

						</tr>
					</thead>
				</table>
			</div>
        </body>
		
		
  
</html>