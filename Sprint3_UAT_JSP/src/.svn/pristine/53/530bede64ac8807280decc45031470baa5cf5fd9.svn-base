<%@ include file="Log.process"%>
<jsp:useBean id="wDSession" class="com.newgen.wfdesktop.session.WDSession" scope="session"/>
<jsp:useBean id="WDCabinet" class="com.newgen.wfdesktop.baseclasses.WDCabinetInfo" scope="session"/>
<%@ page import="java.util.*"%>
<%@ page import="java.io.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.net.*"%>
<%@ page import="java.net.SocketTimeoutException"%>
<%@ page import="javax.xml.parsers.ParserConfigurationException" %>
<%@ page import="com.newgen.wfdesktop.exception.*" %>
<%@ page import="com.newgen.wfdesktop.xmlapi.*" %>
<%@ page import="com.newgen.wfdesktop.util.*" %>
<%@ page import="com.newgen.custom.*" %>
<%@ page import="java.io.UnsupportedEncodingException" %>
<%@ page import="com.newgen.mvcbeans.model.wfobjects.WFDynamicConstant, com.newgen.mvcbeans.model.*,com.newgen.mvcbeans.controller.workdesk.*, javax.faces.context.FacesContext"%>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@page import="com.newgen.omni.wf.util.excp.NGException"%>
<%@page import="com.newgen.omni.wf.util.app.NGEjbClient"%>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page  import= "com.newgen.custom.*, org.w3c.dom.*, org.w3c.dom.NodeList,org.w3c.dom.Document,javax.xml.parsers.DocumentBuilder, javax.xml.parsers.DocumentBuilderFactory"%>
<%@ page import ="java.text.DecimalFormat,org.xml.sax.InputSource"%>
<%@ page import="org.json.JSONObject"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.lang.String.*"%>
<%@ page import="java.lang.Object"%>






<%!
public String FolderSessionCall(String sSessionId){
	try 
		{
			System.out.println("inside FolderSessionCall: " + sSessionId);
			return "123";
			/*
		String prop_file_loc=System.getProperty("user.dir") + File.separatorChar+"CustomConfig"+File.separatorChar+"ServerConfig.properties";
		File file = new File(prop_file_loc);
		FileInputStream fileInput = new FileInputStream(file);
		Properties properties = new Properties();
		properties.load(fileInput);
		fileInput.close();
		String cabinetName = properties.getProperty("cabinetName");
		String wrapperIP = properties.getProperty("wrapperIP");
		String wrapperPort = properties.getProperty("wrapperPort");
		String appServerType = properties.getProperty("appServerType");
		String inputXML="<?xml version='1.0'?><NGOGetIDFromName_Input><Option>NGOGetIDFromName</Option><UserDBId>"+sSessionId+"</UserDBId><CabinetName>"+cabinetName+"</CabinetName><ObjectType>F</ObjectType>  <ObjectName>UAE</ObjectName><Index>19032</Index><MainGroupIndex>0</MainGroupIndex><PageNo>1</PageNo><CreatedByAppName>ACT</CreatedByAppName></NGOGetIDFromName_Input>";
		
			String outputXML = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, inputXML);
			if(outputXML.contains("<Error>")){
				return outputXML.substring(outputXML.indexOf("<Error>")+7, outputXML.indexOf("</Error>"));
			}
			else if(outputXML.contains("<Status>")){
				return outputXML.substring(outputXML.indexOf("<Status>")+8, outputXML.indexOf("</Status>"));
			}
			return outputXML;
			*/
		} 
		catch (Exception ex) 
		{
		   ex.printStackTrace();
		   return ex.getMessage();
		}
		
	}
%>
<%

 String sSessionId = wDSession.getM_objUserInfo().getM_strSessionId();
%>
<html>
    <head>
        <title>System Queue Process</title>
       <link href="checklistCSS.css" rel="stylesheet" type="text/css">
		<!-- Bootstrap CSS and bootstrap datepicker CSS used for styling the demo pages-->
        
		<script src="${pageContext.request.contextPath}/custom/jquery/jquery-1.12.3.js"></script>	
		<script src="${pageContext.request.contextPath}/custom/jquery/jquery-ui.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/custom/jquery/jquery-ui.css" type="text/css" />
<script src="${pageContext.request.contextPath}/custom/jquery/jquery-ui.min.js"></script>
<script type="text/javascript">

var winame= window.parent.workitemName;
	//alert("winame "+ winame);
	
function loadform()
{	
	var jspName="MarqueeNotepadSelect.jsp";
	var params="";
	var result="";
	 var dhtml="";
	 var winame= window.parent.workitemName;
	 var ws_name = window.parent.activityName;
	 //svt points start
	 params="Wi_Name="+winame+"&activityname="+ws_name+"&WD_UID="+window.parent.WD_UID;
	
	result=CallAjax(jspName,params);
	if(result.indexOf('\n')>-1){
		result = result.split('\n')[0];
	}
	
	if(result.indexOf("FAIL")!=-1)
	{
		alert("Some error Encountered. Please try after some time");
		return;
	}
	else if(result.indexOf("FailedException")!=-1)
	{
		alert("Some Exception Occurred. Please try after some time");
		return;
	}
	else
	{		
		if(result=='NODATA' || result=='EXCEPTION')
		{					
		}
		else
		{
		
			//var rows = result.split('~');
			var textToDisplay=result;
			/*for(var i=0;i<rows.length-1;i++){
			if(rows.length>2){
			textToDisplay+= rows[i].split('#')[0]+'-'+rows[i].split('#')[1]+',';
			}
			else{
			textToDisplay+= rows[i].split('#')[0]+'-'+rows[i].split('#')[1];
			}
			}*/
			dhtml='<font size="+1"> <font color="red"><marquee bgcolor="#66CD00" behavior="alternate"  scrollamount="10">Notepad Instructions provided.'+textToDisplay+' </marquee></font></font>';
			document.getElementById("mainDivContent").innerHTML=dhtml;
		}
	}
	
	setInterval(function(){
	//svt points start
		var params="WD_UID="+window.parent.WD_UID;
					CallAjax("Session_refresh.jsp",params);
				},500000);
	
	
	}
	/*
	function loadform (){
	alert("winame "+ winame);
				var result = null;
				var finalxmlResponse = null;
				$.ajax({
					type: "GET",
					url: "/webdesktop/custom/CustomJSP/MarqueeNotepadSelect.jsp",
					data: { wi_name:winame} ,
					dataType: "text",
					async: true,
					success: function (response) {
								alert(response);
							console.log(response);
					},
					error: function (XMLHttpRequest, textStatus, errorThrown) {
						alert('Exception: '+ errorThrown);
					}
				});
				           			
			return result;
           }	*/
		
function CallAjax(jspName,params)
{
	var response="";
	try{			
			var xmlReq = null;
			if(window.XMLHttpRequest) xmlReq = new XMLHttpRequest();
			else if(window.ActiveXObject) xmlReq = new ActiveXObject("Microsoft.XMLHTTP");
			if(xmlReq==null) return; // Failed to create the request
				xmlReq.onreadystatechange = function()
			{
				switch(xmlReq.readyState)
				{
				case 0: 
					break;
				case 1: 
					break;
				case 2: 
					break;
				case 3: 
					break;
				case 4: 
					if (xmlReq.status==200) 
					{
					response=xmlReq.responseText;
					}
					else if (xmlReq.status==404){
					//alert("Invalid Session!");
					}
					else 
					{
						//alert("CallAjax : Status is "+xmlReq.status);
					}						
					break;
				default:
					alert(xmlReq.status);
					break;
				}
			}
			
			xmlReq.open('POST',jspName,false);				
			xmlReq.setRequestHeader('Content-Type','application/x-www-form-urlencoded');
			xmlReq.send(params);
			return response;
	   }
	
    catch(e)
    {
		alert("Exception in Marquee Invalid Session!");
		//return false;
    }
}	
	
</script>
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" 
          marginheight="0"  onload ="loadform();" class="dark-matter">
		 <form name="wdesk">
		<div id="mainDivContent">
			
		</div>
		</form>
</body>

</html>