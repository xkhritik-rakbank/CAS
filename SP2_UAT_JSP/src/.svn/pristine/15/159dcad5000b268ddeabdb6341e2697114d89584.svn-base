<%@ include file="Log.process"%>
<%@ page import="java.io.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.net.*"%>
<%@ page import="javax.xml.parsers.ParserConfigurationException" %>
<%@ page import="com.newgen.wfdesktop.exception.*" %>
<%@ page import="com.newgen.wfdesktop.xmlapi.*" %>
<%@ page import="com.newgen.wfdesktop.util.*" %>
<%@ page import="XMLParser.XMLParser"%>   
<%@ page import="com.newgen.custom.*" %>
<%@ page import="java.io.UnsupportedEncodingException" %>
<%@ page import="com.newgen.mvcbeans.model.wfobjects.WFDynamicConstant, com.newgen.mvcbeans.model.*,com.newgen.mvcbeans.controller.workdesk.*, javax.faces.context.FacesContext"%>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@page import="com.newgen.omni.wf.util.excp.NGException"%>
<%@page import="com.newgen.omni.wf.util.app.NGEjbClient"%>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page  import= "com.newgen.custom.*, org.w3c.dom.*, org.w3c.dom.Node , org.w3c.dom.NodeList , org.w3c.dom.Document , javax.xml.parsers.DocumentBuilder , javax.xml.parsers.DocumentBuilderFactory"%>
<%@ page import="javax.xml.transform.Transformer, javax.xml.transform.TransformerFactory, javax.xml.transform.dom.DOMSource, javax.xml.transform.stream.StreamResult"%>
<%@ page import ="java.text.DecimalFormat,org.xml.sax.InputSource"%>
<%@ page import="org.json.JSONObject"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.io.FileInputStream" %>
<%@ page import="java.io.File" %>
<%@ page language="java" import="java.util.*" %> 
<%@ page import = "java.util.ResourceBundle" %>
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
	
	String prod =null;
	String subprod =null;
	if(request_name.equalsIgnoreCase("InternalExposure") ||request_name.equalsIgnoreCase("ExternalExposure") ||request_name.equalsIgnoreCase("CollectionsSummary"))
	{	
		prod = request.getParameter("prod");
		subprod = request.getParameter("subprod");
	}
	WriteLog("Integration PL jsp: wi_name: "+wi_name);
	WriteLog("Integration PL jsp: activityName: "+activityName);
	WriteLog("Integration PL jsp: param_json: "+param_json);
	WriteLog("Integration PL jsp: sSessionId: "+sSessionId);
	WriteLog("Integration PL jsp: parentWiName: "+parentWiName);
%>
<%!	

Map<String,String> maincodeOp = new HashMap<String,String>();
	Map<String,String> headerOp = new HashMap<String,String>();
	Map<String,String> footerOp = new HashMap<String,String>();
	Map<String,String> parentTagNameOp = new HashMap<String,String>();
	String SessionId = "-1711340385";
	
	public void executeCalls(String req_name){
		String sQuery_header = "SELECT Header as Call_header,Footer,parenttagname FROM NG_Integration with (nolock) where Call_name='"+req_name+"'";
		String appServerType="";
		String wrapperIP = "";
		String wrapperPort = "";
		String cabinetName = "";
		String outputResponse="";
		String sQuery="";
		String call_type="";
		String Call_name="";
		String form_control="";
		String Mobility_form_control="";
		String parent_tag="";
		String tag_name="";
		String is_repetitive ="";
		String parent_tag_name="";
		String xmltag_name="";
		String inputXML="";
		String outputXML="";
		String OutputXML_header="";
		String mqInputRequest="";
		XMLParser xmlParserData=null;
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
			
		}
		inputXML = "<?xml version='1.0'?><APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option><Query>" + sQuery_header + "</Query><EngineName>" + cabinetName + "</EngineName><SessionId>" + SessionId+ "</SessionId></APSelectWithColumnNames_Input>";
			try 
			{
				OutputXML_header = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, inputXML);
			} 
			catch (NGException e) 
			{
			   e.printStackTrace();
			} 
			catch (Exception ex) 
			{
			   ex.printStackTrace();
			}
			
			xmlParserData = new XMLParser();
			xmlParserData.setInputXML((OutputXML_header));
			
			maincodeOp.put(req_name,xmlParserData.getValueOf("MainCode"));
			String parValue=xmlParserData.getNextValueOf("Call_header");
			
			if(parValue!=null){
				parValue=parValue.replace("\n","");
				headerOp.put(req_name,parValue);
			}
			else{
				headerOp.put(req_name,"");
			}
			
	        footerOp.put(req_name,xmlParserData.getNextValueOf("Footer"));
	        parentTagNameOp.put(req_name,xmlParserData.getNextValueOf("parenttagname"));
	}
%>
<%
try{
		executeCalls("CARD_SUMMARY");
	}
	catch(Exception e){
		System.out.println("Exception occured in integration Pl: "+e.getMessage());
		e.printStackTrace();
	}
	
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
		var prod = '<%=prod%>';
		var subprod = '<%=subprod%>';
		var parentWiName = '<%=parentWiName%>';
		
		//alert('prod ' + prod);
		//alert('subprod ' + subprod);
		
				
		function loadform() {
			var dhtml="";
			dhtml+="<p style='color:#e62e00'>Please do not Close the window as transaction is in process.Use close button once the transaction completed.</p><div style='overflow-x:hidden;overflow-y:hidden;height:400px'>"+
				"<table id='mytab' border='1' bordercolor='#337ab7' width='500px' max-height='400px'>";
				dhtml+="<tr><th width ='320px' >Call Name</th><th width ='200px'>Call Status</th><th width ='80px'>Re-try</th></tr>";
				var req_name_arr = req_name.split(',');
				for (var i=0;i<req_name_arr.length;i++){
					dhtml+="<tr><td width = 33% >"+req_name_arr[i]+" </td><td width = 33%><label color:#ffffff id = call_status_"+req_name_arr[i]+">In Process</label></td><td width = 33%><input type ='button' name = 'retry_btn_"+req_name_arr[i]+"' id = 'retry_btn_"+req_name_arr[i]+"' value = 'Re-try' style='visibility:hidden;' onclick = 'integ(this.id)' /></td></tr>";
				}
				dhtml+="</table><input type='button'  id='Close_btn' value='Close' style='visibility:hidden;background-color:#0c658e; color:#ffffff' onclick='final_close();'/></div>";
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
								alert("Some error occured. Please try after sometime : URL doesn't exist!");
							else 
								alert("Some error occured. Please try after sometime or contact administrator : subprocess");	
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
				alert("Some error occured. Please try after sometime or contact administrator");
				//return false;
			}
		}
		
		function integ( btn_id ) {
			//alert('retry button ID: '+ btn_id);
			var call_name = btn_id.substring(10,btn_id.length);
			console.log('wi name: '+wi_name);
			var result = null;
			var finalxmlResponse = null;
				$.ajax({
					type: "GET",
					url: "/webdesktop/custom/CustomJSP/MultipleCardNo.jsp",
					data: { wi_name:parentWiName,  child_wi: wi_name } ,
					dataType: "text",
					async: true,
					success: function (response) {
						//alert('result in integration_all: '+ response);	
							cifIntegration(response);
						console.log(response);
					},
					error: function (XMLHttpRequest, textStatus, errorThrown) {
						alert('Exception: '+ errorThrown);
					}
				});
				           			
			return result;
		}
		
		function set_result_val(result,cardNo)
		{
			$.ajax({
					type: "POST",
					url: "/webdesktop/custom/CustomJSP/CustExpose_Output_AccountPL.jsp",
					data: { result: result, wi_name : wi_name ,accNo:cardNo,parentWiName:parentWiName} ,
					dataType: "text",
					async: true,
					success: function (response) {
						//alert('result set_result_val: '+ response);		
						var status = "Success";
						console.log(response);
					},
					error: function (XMLHttpRequest, textStatus, errorThrown) {
						var status = "Fail";
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
					url: "/webdesktop/custom/CustomJSP/MultipleCardNo.jsp",
					data: { wi_name:parentWiName,  child_wi: wi_name } ,
					dataType: "text",
					async: true,
					success: function (response) {
						//alert('result in integration_all: '+ response);
							
						if(req_name.indexOf('CARD_SUMMARY')>-1 && trimStr(response)==''){
									var eml_name='call_status_' +req_name+'';
									document.getElementById("Close_btn").style.visibility="visible";
									document.getElementById(eml_name).innerHTML="No Cards found for this Customer";
									window.opener.com.newgen.omniforms.formviewer.setNGValue('Is_Overwrite_Details','SUCCESS');
									window.returnValue = "SUCCESS";
									
								}
								else{
							cifIntegration(response);
							}
						//console.log(response);
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
			var cardNo = null;
			var cardNo_arr = response.split(',');
			//alert('cifId_arr' + cifId_arr);
			for (var j=0;j<cardNo_arr.length;j++)
			{
				//alert('cardNo_arr' + cardNo_arr[j]);
				cardNo = cardNo_arr[j];
				var req_name_arr = req_name.split(',');
					for (var i=0;i<req_name_arr.length;i++)
					{
						var eml_name = "call_status_"+req_name_arr[i];
						$.ajax({
							type: "GET",
							url: "/webdesktop/custom/CustomJSP/Requestxml_PL.jsp",
							data: { request_name: req_name_arr[i], is_mobility : "N", SeesionId:sessionId ,wi_name:wi_name ,activityName:activityName ,param_json:param_json, cardNo:cardNo,header:'<%=headerOp.get("CARD_SUMMARY")%>',maincode:'<%=maincodeOp.get("CARD_SUMMARY")%>',footer:'<%=footerOp.get("CARD_SUMMARY")%>',parentTagName:'<%=parentTagNameOp.get("CARD_SUMMARY")%>'} ,
							dataType: "text",
							async: true,
							success: function (response) {
								set_result_val(response,cardNo);
								
								
								document.getElementById(eml_name).innerHTML="Sucessfull";
								var status = "Success";
								document.getElementById("Close_btn").style.visibility="visible";
								window.opener.document.getElementById('ExtLiability_IFrame_internal').src='/webdesktop/custom/CustomJSP/internal_liability.jsp';
								window.opener.com.newgen.omniforms.formviewer.setNGValue('Is_Overwrite_Details','SUCCESS');
								//SetDataOnForm(response); //Tanshu
								
							},
							error: function (XMLHttpRequest, textStatus, errorThrown) {
								document.getElementById("Close_btn").style.visibility="visible";
								document.getElementById("retry_btn_"+req_name_arr[i-1]).style.visibility="visible";		
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
			
			window.opener.document.getElementById('ExtLiability_IFrame_internal').src='/webdesktop/custom/CustomJSP/internal_liability.jsp';						
			xmlDoc = $.parseXML(response),
			$xml = $( xmlDoc ),
			$BandId = $xml.find("BankId");
			$SalTxnDetails = $xml.find("SalTxnDetails");
			window.opener.document.getElementById('cmplx_IncomeDetails_SalaryXferToBank').value =$SalTxnDetails.find('Amount').text();
			var salcredDate = new Date($SalTxnDetails.find('SalCreditDate').first().text());
				if(salcredDate){
					if(salcredDate.getDate()){
						window.opener.document.getElementById('cmplx_IncomeDetails_SalaryDay').value = salcredDate.getDate();	
					}
				}
			$AvgBalanceDtls = $xml.find("AvgBalanceDtls");
			//alert("Bank Id is: "+$BandId.text());
			var average=0;								
			if($AvgBalanceDtls.length!=0) {
					$AvgBalanceDtls.each(function( index ) {
														
					//alert("Month on index :"+index+" and value: "+$(this).find('Month').text());
					//alert("Month on index :"+index+" and value: "+$(this).find('AvgBalance').text());
					/* if(index==0)	{
					 window.opener.document.getElementById('cmplx_IncomeDetails_Overtime_Month1').value =$(this).find('Month').text();
					 }
					 else if(index==1) {
					 window.opener.document.getElementById('cmplx_IncomeDetails_Overtime_Month2').value =$(this).find('Month').text();
					 }*/
					average = +average + +($(this).find('AvgBalance').text());
				  //console.log($( this ).find('Month').text());
				});
				//alert(average);
				//alert(average/$AvgBalanceDtls.length);
				var finavg=(average/$AvgBalanceDtls.length);
				//alert("Month is: "+$AvgBalanceDtls.find('Month').text());
				//alert("Avg Bal is: "+$AvgBalanceDtls.find('AvgBalance').text());
			}
			window.opener.document.getElementById('cmplx_IncomeDetails_AvgBal').value =finavg;
		}
		//Tanshu ended
		
		function final_close(){
			window.close();
		}
		
		function trimStr(str) {
                       return str.replace(/^\s+|\s+$/g, '');
            }
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