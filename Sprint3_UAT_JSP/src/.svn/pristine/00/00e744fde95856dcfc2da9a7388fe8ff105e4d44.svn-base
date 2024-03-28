<%@ page import="java.io.*,java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.lang.String.*"%>
<%@ page import="java.lang.Object"%>
<%@ page import="com.newgen.wfdesktop.xmlapi.*" %>
<%@ page import="com.newgen.wfdesktop.util.*" %>
<%@ page import="com.newgen.omni.jts.cmgr.XMLParser"%>
<%@ page import="com.newgen.mvcbeans.model.*,javax.faces.context.FacesContext,com.newgen.mvcbeans.controller.workdesk.*"%>
<!--svt points start -->
<%@ page import="org.owasp.esapi.ESAPI"%>
<%@ page import="org.owasp.esapi.codecs.OracleCodec"%>
<%@ page import="org.owasp.esapi.User" %>
<!--svt points end -->
<jsp:useBean id="wDSession" class="com.newgen.wfdesktop.session.WDSession" scope="session"/>

<%
response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader ("Expires", 0);   
  //String sessionId = request.getParameter("SessionId");
  //svt points start
	String input6 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("sessionId"), 1000, true) );
	String sSessionId = ESAPI.encoder().encodeForSQL(new OracleCodec(), input6);
   //svt points end 
	if (sessionId == null) {
        out.println("Invalid Session. Please login again.");
    }
	
%>

<HTML>
<HEAD>
<TITLE>Create Duplicate Workitem</TITLE>

</HEAD>
		


<script type="text/javascript">
var Session_ID = '<%=sessionId%>';
function validateForm() {
    var x = document.forms["mainFrm"]["pname"].value;
	var y = document.forms["mainFrm"]["winame"].value;
	
	
	if (x == "--Select--" || x=="") {
        alert("Process Name must be filled !!");
        return false;
    }
    //var y = document.forms["mainFrm"]["winame"].value;
	
    if (y == "") {
        alert("Workitem Name must be filled !!");
        return false;
    }
	
	
	//return true;
	
	var ajaxReq;
	var ajaxResult;
		
		if (window.XMLHttpRequest) 
		{
			ajaxReq= new XMLHttpRequest();
		}
		else if (window.ActiveXObject)
		{
			ajaxReq= new ActiveXObject("Microsoft.XMLHTTP");
		}
		
		var url = "UploadWIAndCopyData.jsp?pname="+x+"&old_WI_Name="+y+"&sessionId="+Session_ID;
		
		//window.open(url);
		
        
		
		ajaxReq.open("POST", url, false);
		ajaxReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
		ajaxReq.send(null);
		
		if (ajaxReq.status == 200 && ajaxReq.readyState == 4)
		{
			
			ajaxResult=ajaxReq.responseText;	    				
			var resp = ajaxResult.split("~");
			if(resp[0]=='Success')
				alert(resp[1]);	
			else if(resp[0]=='Error')
				alert("Error Occurred : "+resp[1]);	
		}
		else
		{
			
			alert("Error: Please Contact Administrator ~"+ajaxReq.status);
		}		
}
	
</script>

<BODY >

<form name="mainFrm" onsubmit="return validateForm()" method="post">


<table id="newwi" align="center" border="1" width="100%" cellspacing="1"  bgColor=#ebf3ff >
<tr>
	<td  valign="top" colspan="3" >
		<table cellspacing="1" cellpadding="2" width="100%" border="0">
			<tr>
			    <td bgcolor="#b6cee7"  colspan="3">
					<font color="#000000" face="arial" size="2"><b>Please Fill Process Name and Workitem Name to Proceed</b>
					</font>
				</td>
            </tr>
			
			<tr>
				<td  valign="top" width="20%"><b><font color="#330099" face="Arial" size="2">Process&nbsp;Name</font></b>
				</td>
				<td  align="left" width="21%" colspan="2">
					<select width="80%" align="center" tabindex=1 name="pname" style="width:65%;" > 
					<option value="" selected>--Select--</option>
					<option value="CreditCard" >CREDIT CARD</option>
					<option value="PersonalLoanS" >PERSONAL LOAN</option>
					<option value="RLOS">RLOS</option>
					</select>
					</td>
			</tr>
			
			<tr>	
			   <td  valign="top" width="20%"><b><font color="#330099" face="Arial" size="2">Workitem&nbsp;Name</font></b></td>
			   	<td  align="left" width="80%">
					<input  type="Text"  width="40%" align="center" name="winame"   tabindex=2  >
				</td>
			</tr>
			
						<tr>
				<td></td>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input align="left" type="submit" value="Submit" tabindex=3>&nbsp;&nbsp;
					
				</td>
				<td>
				
				<div id="process" style="display:none;"><img src="/webdesktop/resources/images/progressimg.gif"></div>
				
				</td>
			</tr>
		</table>
	</td>
</tr>
</table>
</form>

</BODY></HTML>