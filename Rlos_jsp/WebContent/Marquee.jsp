<%@ include file="Log.process"%>
<%@ page import="java.io.*,java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.lang.String.*"%>
<%@ page import="java.lang.Object"%>
<%@ page import="com.newgen.wfdesktop.xmlapi.*" %>
<%@ page import="com.newgen.wfdesktop.util.*" %>
<%@ page import="com.newgen.omni.jts.cmgr.XMLParser"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.newgen.mvcbeans.model.*,javax.faces.context.FacesContext,com.newgen.mvcbeans.controller.workdesk.*"%>
<%@page import="com.newgen.omni.wf.util.excp.NGException,com.newgen.omni.wf.util.app.NGEjbClient"%>
<jsp:useBean id="wDSession" class="com.newgen.wfdesktop.session.WDSession" scope="session"/>
<jsp:useBean id="WDCabinet" class="com.newgen.wfdesktop.baseclasses.WDCabinetInfo" scope="session"/>

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

var winame= window.parent.pid;
	//alert("winame "+ winame);
	
function loadform()
{	
	var jspName="MarqueeNotepadSelect.jsp";
	var params="";
	var result="";
	 var dhtml="";
	 var winame= window.parent.pid;
	 params="Wi_Name="+winame;
	
	result=CallAjax(jspName,params);
	//alert('result '+ result)
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
		
			var rows = result.split('~');
			var textToDisplay='';
			for(var i=0;i<rows.length-1;i++){
			if(rows.length>2){
			textToDisplay+= rows[i].split('#')[0]+'-'+rows[i].split('#')[1]+',';
			}
			else{
			textToDisplay+= rows[i].split('#')[0]+'-'+rows[i].split('#')[1];
			}
			}
			dhtml='<font size="+1"> <font color="red"><marquee bgcolor="#66CD00" behavior="alternate"  scrollamount="10">Notepad Instructions provided.'+textToDisplay+' </marquee></font></font>';
			document.getElementById("mainDivContent").innerHTML=dhtml;
		}
	}
	}
	/*
	function loadform (){
	alert("winame "+ winame);
				var result = null;
				var finalxmlResponse = null;
				$.ajax({
					type: "GET",
					url: "/webdesktop/resources/scripts/MarqueeNotepadSelect.jsp",
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
					else if (xmlReq.status==404)
						alert("CallAjax : URL doesn't exist!");
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
		alert("CallAjax : "+e.message);
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