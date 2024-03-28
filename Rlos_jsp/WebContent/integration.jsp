<!--
	-------------------------------------------Change Historty-------------------------------------------------------
Name:			Date:			Refrence no:	Changes:
Deepak			18Sept2017		REF-18092017	Not to run CARD_INSTALLMENT_DETAILS enquiry in case no LOC card found
-->

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
	String param_json = request.getParameter("param_json");
	
	String prod =null;
	String subprod =null;
	if(request_name.equalsIgnoreCase("InternalExposure") ||request_name.equalsIgnoreCase("ExternalExposure") ||request_name.equalsIgnoreCase("CollectionsSummary")||request_name.equalsIgnoreCase("CARD_INSTALLMENT_DETAILS"))
	{	
		prod = request.getParameter("prod");
		subprod = request.getParameter("subprod");
	}
	WriteLog("Integration jsp: wi_name: "+wi_name);
	WriteLog("Integration jsp: activityName: "+activityName);
	WriteLog("Integration jsp: param_json: "+param_json);
	WriteLog("Integration jsp: sSessionId: "+sSessionId);
%>
<!DOCTYPE html>
<html>
	<head>
		<link href="checklistCSS.css" rel="stylesheet" type="text/css">
		<!-- Bootstrap CSS and bootstrap datepicker CSS used for styling the demo pages-->
        
		<script src="${pageContext.request.contextPath}/custom/js/json2.js"></script>	
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
		var prod = '<%=prod%>';
		var subprod = '<%=subprod%>';
		var row_count = 1;
					
		function loadform() {
		var req_name_arr = req_name.split(',');
			if(req_name.indexOf('InternalExposure')>-1 || req_name.indexOf('CollectionsSummary')>-1 ||req_name.indexOf('ExternalExposure')>-1||req_name.indexOf("CARD_INSTALLMENT_DETAILS")>-1){
				integration_all();
			}
			else{
				integration_all_Financial();
			}
			
		}
		
		function trimStr(str) {
                       return str.replace(/^\s+|\s+$/g, '');
            }
		
	function dynamicTable(req_name_arr,cifID,row_count){
			var table = document.getElementById("dyn_tbl_row");
			var row = table.insertRow(0);
			var cell1 = row.insertCell(0);
			var cell2 = row.insertCell(1);
			var cell3 = row.insertCell(2);
			cell1.width = "33%";
			cell2.width = "33%";
			cell3.width = "33%";
			cell1.innerHTML = req_name_arr+" for "+cifID;
			cell2.innerHTML = "<label color:#ffffff id = call_status_"+row_count+">In Process</label>";
			cell3.innerHTML = "<input type ='button' name = 'retry_btn_"+row_count+"' id = 'retry_btn_"+row_count+"' value = 'Re-try' style='visibility:hidden;' onclick = 'integ(this.id)' />";
			
		}
		//added
		/*function dynamicTable(req_name_arr,cifID,row_count){
			var dhtml="";
						dhtml+="<tr><td width = 33% >"+req_name_arr+" for "+cifID +" </td><td width = 33%><label color:#ffffff id = call_status_"+row_count+">In Process</label></td><td width = 33%><input type ='button' name = 'retry_btn_"+row_count+"' id = 'retry_btn_"+row_count+"' value = 'Re-try' style='visibility:hidden;' onclick = 'integ(this.id)' /></td></tr>";
				
			
			document.getElementById("dyn_tbl_row").innerHTML=document.getElementById("dyn_tbl_row").innerHTML+dhtml;
		}*/
		//ended
		
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
					url: "/webdesktop/resources/scripts/MultipleCifId.jsp",
					data: { wi_name:wi_name , req_name : req_name} ,
					dataType: "text",
					async: true,
					success: function (response) {
						cifIntegration(response);
						//console.log(response);
					},
					error: function (XMLHttpRequest, textStatus, errorThrown) {
						alert('Exception: '+ errorThrown);
					}
				});
				           			
			return result;
		}
		
		function set_result_val(result,row_count,CardNumber,cifId,cust_type)
		{
			if(req_name == 'CARD_INSTALLMENT_DETAILS'){
							data={ result: result, wi_name : wi_name, prod : prod, subprod : subprod,is_mobility : "N", SeesionId:sessionId,row_count:row_count,cifId:cifId,CardNumber:CardNumber,cust_type:cust_type}
							}
							else{
							data= { result: result, wi_name : wi_name, prod : prod, subprod : subprod,is_mobility : "N", SeesionId:sessionId,row_count:row_count,cifId:cifId,cust_type:cust_type} 
							}
	
			$.ajax({
					type: "POST",
					url: "/webdesktop/resources/scripts/CustExpose_Output.jsp",
					data: data,
					//{ result: result, wi_name : wi_name, prod : prod, subprod : subprod,cifId:cifId,is_mobility : "N", SeesionId:sessionId} ,
					dataType: "text",
					async: true,
					success: function (response){
						var flag_value=response.split('@#');
						
						var last4 = flag_value[0].slice(-4);
						var eml_name='call_status_' + trimStr(flag_value[1])+'';
						if(last4=="true"){
							document.getElementById(eml_name).innerHTML="Sucessfull";
							 window.returnValue = "SUCCESS";
							 Aecb_report_url(result);
						}
						else if(last4=="B003"){
							document.getElementById(eml_name).innerHTML="Consumer record not found at Bureau";
							 window.returnValue = "SUCCESS";
						}
						else{
							document.getElementById(eml_name).innerHTML="Failure";
							document.getElementById("retry_btn_"+trimStr(flag_value[1])).style.visibility="visible";
							window.returnValue = "FAIL";							
						}
					},
					error: function (XMLHttpRequest, textStatus, errorThrown) {
						var status = "Fail";
						
						alert('Exception: '+ errorThrown);
					}
				});
		}
		
		function Aecb_report_url(result){
			xmlDoc = $.parseXML(result),
				$xml = $( xmlDoc ),
				$RequestType = $xml.find("RequestType");
				if($RequestType.text()=='ExternalExposure'){
					$ReportUrl =  $xml.find("ReportUrl");
					window.opener.document.getElementById("cmplx_Liability_New_Aecb_Report_Url").value =$ReportUrl.text() ;
				}
		}
		var GlobalFlag = false;
		function integration_all (){
				var result = null;
				var finalxmlResponse = null;
				$.ajax({
					type: "GET",
					url: "/webdesktop/resources/scripts/MultipleCifId.jsp",
					data: { wi_name:wi_name , req_name : req_name} ,
					dataType: "text",
					async: true,
					success: function (response) {
						cifIntegration(response);
						//console.log(response);
					},
					error: function (XMLHttpRequest, textStatus, errorThrown) {
						alert('Exception: '+ errorThrown);
					}
				});
				           			
			return result;
           }
		   
		   function integration_all_Financial (){
				var result = null;
				var finalxmlResponse = null;
				$.ajax({
					type: "GET",
					url: "/webdesktop/resources/scripts/MultipleCifId_accId.jsp",
					data: { wi_name:wi_name} ,
					dataType: "text",
					async: true,
					success: function (response) {
						cifIntegration(response);
						//console.log(response);
					},
					error: function (XMLHttpRequest, textStatus, errorThrown) {
						alert('Exception: '+ errorThrown);
					}
				});
				           			
			return result;
           }

 function cifIntegration(response){
	 
			var result = null;
			var cifID = null;
			var acc_id='';
			var CardNumber = '';
			var jsondata = JSON.parse(decodeURIComponent(param_json));
			var integration_call_flag = "";
			//Changes done by aman for AECB Start
			var companyCif=jsondata["cif"]; 
			var cust_type = "Individual_CIF";
			var TLNo=jsondata["trade_lic_no"]; 
			if (companyCif !=''&& companyCif !='null' && req_name =='InternalExposure,ExternalExposure'){
				response = response + ',' + companyCif;			  
			}
			
			else if(TLNo!='' && TLNo!=null && companyCif=='')
			{
				response = response+','+'Corporate';
			}
			//Changes done by aman for AECB End
			else if(req_name == 'ExternalExposure'){
				/*
				response = jsondata["cmplx_Customer_CIFNO"];
				if(trimStr(response)!='' && trimStr(response)!='null'){
					response = response + ',' + companyCif;
				}
				*/
				response = companyCif;
			}	
						
			var cif_acc_Arr=null;
			var cifId_arr = response.split(',');
			var dhtml="";
			
			for (var j=0;j<cifId_arr.length;j++)
			{
				if(cifId_arr[j].indexOf(":")>-1){
					cif_acc_Arr = cifId_arr[j].split(":");
					cifID = trimStr(cif_acc_Arr[0]);
					acc_id = cif_acc_Arr[1];
				}
				else{
					cifID = trimStr(cifId_arr[j]);
				}
				
				var req_name_arr = req_name.split(',');
					for (var i=0;i<req_name_arr.length;i++)
					{
						if((req_name_arr[i] != 'ExternalExposure' && cifID.indexOf(companyCif)<0)||(req_name_arr[i] == 'ExternalExposure' && (cifID.indexOf(jsondata["cmplx_Customer_CIFNO"])>-1)||cifID.indexOf(companyCif)>-1))
						{
							
							if(req_name == 'CARD_INSTALLMENT_DETAILS' && cifID!='Corporate'){
								data={ request_name: "CARD_INSTALLMENT_DETAILS", is_mobility : "N", SeesionId:sessionId ,wi_name:wi_name ,activityName:activityName ,param_json:param_json,cifId:jsondata["cmplx_Customer_CIFNO"],acc_id:acc_id,row_count:row_count,CardNumber:cifID}
								integration_call_flag='Y';
							}
							else{
								//Changes done by aman for AECB	Start
								
								if(cifID=='Corporate' || (cifID==companyCif && cifID!="null" && cifID!="")){
									cust_type = "Corporate_CIF";
								}
								//change by saurabh on 16th Oct.
								else{
									cust_type = "Individual_CIF";
								}
							
								if (req_name_arr[i] == 'ExternalExposure')
								 {
									data= { request_name: req_name_arr[i], is_mobility : "N", SeesionId:sessionId ,wi_name:wi_name ,activityName:activityName ,param_json:param_json,cifId:cifID,acc_id:acc_id,row_count:row_count,Customer_Type:cust_type}
									integration_call_flag="Y";
								 }
								 
								 //added for External call of Company when cif is not available
								 else if(req_name_arr[i] != 'ExternalExposure' && cifID=='Corporate'){
								 	integration_call_flag="N";
								 }
								 //added for External call of Company when cif is not available
 
								 else if(req_name_arr[i] == 'InternalExposure'  && (cifID=="null" || cifID=="")){
									integration_call_flag="N";
								 }
								 else if(req_name_arr[i] != 'ExternalExposure'){
									 data= { request_name: req_name_arr[i], is_mobility : "N", SeesionId:sessionId ,wi_name:wi_name ,activityName:activityName ,param_json:param_json,cifId:cifID,acc_id:acc_id,row_count:row_count}
									 integration_call_flag="Y";
									}
								//Changes done by aman for AECB End
							}								 
							if(integration_call_flag=="Y"){
								dynamicTable(req_name_arr[i], cifId_arr[j],row_count);
								//REF-18092017 start
								if(req_name_arr[i]=='CARD_INSTALLMENT_DETAILS' && trimStr(response)==''){
									var eml_name='call_status_' +row_count+'';
									document.getElementById(eml_name).innerHTML="No LOC Card found for this Customer";
									window.returnValue = "SUCCESS";
								}
								else{
									$.ajax({
									type: "GET",
									url: "/webdesktop/resources/scripts/Requestxml_1.jsp",
									data: data,
									dataType: "text",
									async: true,
									success: function (response) {
										var rowcount=response.split('!@#');
										set_result_val(rowcount[0],rowcount[1],'',cifID,cust_type);
									},
									error: function (XMLHttpRequest, textStatus, errorThrown) {
										document.getElementById("retry_btn_"+req_name_arr[i]).style.visibility="visible";		
										alert('Exception: '+ errorThrown);
									}
								});
								}
								//REF-18092017 End
							}
							
							row_count++;
						}
						
					}
			}
			
					return result;
		   
		}
		  
		/*function SetDataOnForm(response)
		{
			if(response.indexOf('MQ_RESPONSE_XML')>-1){
				response = response.substring(response.indexOf("<MQ_RESPONSE_XML>")+17,response.indexOf("</MQ_RESPONSE_XML>"));
				
				xmlDoc = $.parseXML(response),
				$xml = $( xmlDoc ),
				$BandId = $xml.find("BankId");
				$SalTxnDetails = $xml.find("SalDetails");
				window.opener.document.getElementById('cmplx_IncomeDetails_SalaryXferToBank').value =$SalTxnDetails.find('SalAmount').text();
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
						//average = +average + +($(this).find('AvgBalance').text());
					  //console.log($( this ).find('Month').text());
				/*	});
					var finavg=(average/$AvgBalanceDtls.length);
				}
				window.opener.document.getElementById('cmplx_IncomeDetails_AvgBal').value =finavg;
			}
		}*/
		//Tanshu ended

		</script>
		<title>Integration in process</title>
	</head>
	<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0"  onload ="loadform();" class="dark-matter">
		<form name="integration_new" id="integration_new" method="post" >
		
			<div id="mainDivContent">
					<div style='overflow:scroll;height:400px'>
						<table id='mytab' border='1' bordercolor='#337ab7' width='500px' max-height='400px'>
							<tr>
								<th width = 33% >Call name</th><th width = 33%>Call status</th><th width = 33%>Re -try </th>
							</tr>
						</table>
						
						<table id='dyn_tbl_row' border='1' bordercolor='#337ab7' width='500px' max-height='500px'>
						</table>
					
					</div>
			</div>			
		</form>
	</body>
	
</html>