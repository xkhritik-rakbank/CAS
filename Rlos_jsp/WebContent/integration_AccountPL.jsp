<%@ include file="Log.process"%>
<%@ page import="java.io.*,java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.lang.String.*"%>
<%@ page import="java.lang.Object"%>
<%@ page import="com.newgen.wfdesktop.xmlapi.*" %>
<%@ page import="com.newgen.wfdesktop.util.*" %>
<%@ page import="com.newgen.omni.jts.cmgr.XMLParser"%>
<%@ page import="com.newgen.mvcbeans.model.*,javax.faces.context.FacesContext,com.newgen.mvcbeans.controller.workdesk.*"%>
<jsp:useBean id="wDSession" class="com.newgen.wfdesktop.session.WDSession" scope="session"/>


<%
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader ("Expires", 0);
	String request_name = request.getParameter("request_name");
	WriteLog("Integration jsp: request_name: "+request_name);
	String sSessionId = wDSession.getM_objUserInfo().getM_strSessionId();
	
	String wi_name = request.getParameter("wi_name");
	String activityName = request.getParameter("activityName");
	String parentWiName = request.getParameter("parentWiName");
		
	String param_json = request.getParameter("param_json");
		
	WriteLog("Integration PL jsp: wi_name: "+wi_name);
	WriteLog("Integration PL jsp: activityName: "+activityName);
	WriteLog("Integration PL jsp: param_json: "+param_json);
	WriteLog("Integration PL jsp: sSessionId: "+sSessionId);
	WriteLog("Integration PL jsp: parentWiName: "+parentWiName);
%>
<!DOCTYPE html>
<html>
	<head>
		<link href="checklistCSS.css" rel="stylesheet" type="text/css">
		<!-- Bootstrap CSS and bootstrap datepicker CSS used for styling the demo pages-->
        
		<script src="${pageContext.request.contextPath}/custom/jquery/jquery-1.12.3.js"></script>	
		<script src="${pageContext.request.contextPath}/custom/jquery/jquery-ui.js"></script>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/custom/jquery/jquery-ui.css" type="text/css" />
		<script src="${pageContext.request.contextPath}/custom/jquery/jquery-ui.min.js"></script>
		<script type="text/javascript">
		var req_name = '<%=request_name%>';
		var sessionId = '<%=sSessionId%>';
		var wi_name = '<%=wi_name%>';
		var activityName = '<%=activityName%>';
		var param_json = '<%=param_json%>';
		var parentWiName = '<%=parentWiName%>';

		function loadform() {
			var dhtml="";
			dhtml+="<div style='overflow-x:hidden;overflow-y:hidden;height:400px'>"+
				"<table id='mytab' border='1' bordercolor='#337ab7' width='500px' max-height='400px'>";
				dhtml+="<tr><th width = 33% >Call name</th><th width = 33%>Call status</th><th width = 33%>Re -try </th></tr>";
				var req_name_arr = req_name.split(',');
				for (var i=0;i<req_name_arr.length;i++){
					dhtml+="<tr><td width = 33% >"+req_name_arr[i]+" </td><td width = 33%><label color:#ffffff id = call_status_"+req_name_arr[i]+">In Process</label></td><td width = 33%><input type ='button' name = 'retry_btn_"+req_name_arr[i]+"' id = 'retry_btn_"+req_name_arr[i]+"' value = 'Re-try' style='visibility:hidden;' onclick = 'integ(this.id)' /></td></tr>";
				}
				dhtml+="</table></div>";
			document.getElementById("mainDivContent").innerHTML=dhtml;
		}
		
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
								alert("CallAjax : Status is "+xmlReq.status);	
							break;
						default:
							alert(xmlReq.status);
							break;
						}
					}
					
					xmlReq.open('POST',jspName,true);				
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
		
		function integ( btn_id ) {
			
			var call_name = btn_id.substring(10,btn_id.length);
			var result = null;
				var finalxmlResponse = null;
				$.ajax({
					type: "GET",
					url: "/webdesktop/resources/scripts/MultipleAccNo.jsp",
					data: { wi_name:parentWiName} ,
					dataType: "text",
					async: true,
					success: function (response) {
						cifIntegration(response);
						console.log(response);
					},
					error: function (XMLHttpRequest, textStatus, errorThrown) {
						alert('Exception: '+ errorThrown);
					}
				});
				           			
			return result;
		}
		
		function set_result_val(result,accNo)
		{
			$.ajax({
					type: "POST",
					url: "/webdesktop/resources/scripts/CustExpose_Output_AccountPL.jsp",
					data: { result: result, wi_name : wi_name ,accNo:accNo,parentWiName:parentWiName} ,
					dataType: "text",
					async: true,
					success: function (response) {
						var status = "Success";
						window.opener.document.getElementById('aecb_call_status').value = status;	
						window.opener.document.getElementById('ExtLiability_IFrame_internal').src='/webdesktop/resources/scripts/internal_liability.jsp';												
						console.log(response);
					},
					error: function (XMLHttpRequest, textStatus, errorThrown) {
						var status = "Fail";
						window.opener.document.getElementById('aecb_call_status').value = status;
						alert('Exception: '+ errorThrown);
					}
				});
				
		}
		
		var GlobalFlag = false;
		function integration_all (){
				var result = null;
				var finalxmlResponse = null;
				$.ajax({
					type: "GET",
					url: "/webdesktop/resources/scripts/MultipleCifId.jsp",
					data: { wi_name:parentWiName , req_name : req_name} ,
					dataType: "text",
					async: true,
					success: function (response) {
						cifIntegration(response);
						console.log(response);
					},
					error: function (XMLHttpRequest, textStatus, errorThrown) {
						alert('Exception: '+ errorThrown);
					}
				});
				           			
			return result;
           }
		   
		   function cifIntegration(response)
		   {
			var result = null;
			var cif_no = null;
			
			var cif_arr = response.split(',');
			
			for (var j=0;j<cif_arr.length;j++)
			{
			
				cif_no = cif_arr[j];
				var req_name_arr = req_name.split(',');
					for (var i=0;i<req_name_arr.length;i++)
					{
						var eml_name = "call_status_"+req_name_arr[i];
						
						$.ajax({
							type: "GET",
							url: "/webdesktop/resources/scripts/Requestxml_PL.jsp",
							data: { request_name: req_name_arr[i], is_mobility : "N", SeesionId:sessionId ,wi_name:wi_name ,activityName:activityName ,param_json:param_json,cifno:cif_no} ,
							dataType: "text",
							async: true,
							success: function (response) {
								//alert('result cifIntegration: '+ response);
								set_result_val(response,"");
								document.getElementById(eml_name).innerHTML="Sucessfull";
								var status = "Success";
								window.opener.document.getElementById('aecb_call_status').value = status;
								console.log(response);
								SetDataOnForm(response); //Tanshu
							},
							error: function (XMLHttpRequest, textStatus, errorThrown) {
								document.getElementById("retry_btn_"+req_name_arr[i]).style.visibility="visible";		
								
								var status = "Fail";
								window.opener.document.getElementById('aecb_call_status').value = status;
								//setNGValue("aecb_call_status","Fail");
								alert('Exception: '+ errorThrown);
							}
						});
					}
			}
					return result;
		   
		   }
			
		//Tanshu
		function SetDataOnForm(response)
		{
		
			xmlDoc = $.parseXML(response),
			$xml = $( xmlDoc ),
			$BandId = $xml.find("BankId");
			$SalTxnDetails = $xml.find("SalTxnDetails");
			window.opener.document.getElementById('cmplx_IncomeDetails_SalaryXferToBank').value =$SalTxnDetails.find('Amount').text();
			window.opener.document.getElementById('cmplx_IncomeDetails_SalaryDay').value =$SalTxnDetails.find('SalCreditDate').text();
			$AvgBalanceDtls = $xml.find("AvgBalanceDtls");
		
			var average=0;								
			if($AvgBalanceDtls.length!=0) {
					$AvgBalanceDtls.each(function( index ) {
														
					
					/* if(index==0)	{
					 window.opener.document.getElementById('cmplx_IncomeDetails_Overtime_Month1').value =$(this).find('Month').text();
					 }
					 else if(index==1) {
					 window.opener.document.getElementById('cmplx_IncomeDetails_Overtime_Month2').value =$(this).find('Month').text();
					 }*/
					average = +average + +($(this).find('AvgBalance').text());
				  //console.log($( this ).find('Month').text());
				});
				var finavg=(average/$AvgBalanceDtls.length);
				
			}
			window.opener.document.getElementById('cmplx_IncomeDetails_AvgBal').value =finavg;
		}
		//Tanshu ended

		</script>
		<title>Integration in process</title>
	</head>
	<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0"  onload ="loadform();integration_all();" class="dark-matter">
		<form name="integration_new" id="integration_new" method="post" >
		<div id="mainDivContent">
				
				
			</div>
		
		</form>
	</body>
	
</html>