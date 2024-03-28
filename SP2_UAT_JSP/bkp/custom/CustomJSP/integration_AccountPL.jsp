<%@ include file="Log.process"%>
<%@ page import="java.io.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.net.*"%>
<%@ page import="javax.xml.parsers.ParserConfigurationException" %>
<%@ page import="com.newgen.wfdesktop.exception.*" %>
<%@ page import="com.newgen.wfdesktop.xmlapi.*" %>
<%@ page import="com.newgen.wfdesktop.util.*" %>
<%@ page import="com.newgen.wfdesktop.util.xmlapi.*" %>
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
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.owasp.esapi.ESAPI"%>
<%@ page import="org.owasp.esapi.codecs.OracleCodec"%>
<%@ page import="org.owasp.esapi.User" %>
<%@ page import="com.newgen.*" %>
<jsp:useBean id="wDSession" class="com.newgen.wfdesktop.session.WDSession" scope="session"/>

 
<!-- esapi4js i18n resources -->
<script type="text/javascript" language="JavaScript" src="esapi4js/resources/i18n/ESAPI_Standard_en_US.properties.js"></script>
<!-- esapi4js configuration -->
<script type="text/javascript" language="JavaScript" src="esapi4js/esapi-compressed.js"></script>
<script type="text/javascript" language="JavaScript" src="esapi4js/resources/Base.esapi.properties.js"></script>
<!-- esapi4js core -->

<%
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader ("Expires", 0);

	String sSessionId = wDSession.getM_objUserInfo().getM_strSessionId();
	
	String input1 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("request_name"), 1000, true) );
	String request_name = ESAPI.encoder().encodeForSQL(new OracleCodec(), input1);
	
	String input2 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("wi_name"), 1000, true) );
	String wi_name = ESAPI.encoder().encodeForSQL(new OracleCodec(), input2);
	
	String input3 = ESAPI.encoder().encodeForHTML(ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("activityName"), 1000, true));
	String activityName = ESAPI.encoder().encodeForSQL(new OracleCodec(), input3);
	
	String input4 = ESAPI.encoder().encodeForHTML(ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("parentWiName"), 1000, true) );
	String parentWiName = ESAPI.encoder().encodeForSQL(new OracleCodec(), input4);
	
	//String input5 = ESAPI.encoder().encodeForHTML(ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("param_json"), 1000, true));
	//String param_json = ESAPI.encoder().encodeForSQL(new OracleCodec(), input5);
	
	String param_json = request.getParameter("param_json");
	
	String input6 = ESAPI.encoder().encodeForHTML(ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("WD_UID"), 1000, true));
	String WD_UID = ESAPI.encoder().encodeForSQL(new OracleCodec(), input6);
	
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
		String params = "";
		String sQuery_header = "SELECT Header as Call_header,Footer,parenttagname FROM NG_Integration with (nolock) where Call_name=:req_name";
		params="req_name=="+req_name;
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
		inputXML = ExecuteQuery_APSelectwithparam(sQuery_header,params,cabinetName,SessionId);
		
		//inputXML = "<?xml version='1.0'?><APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option><Query>" + sQuery_header + "</Query><EngineName>" + cabinetName + "</EngineName><SessionId>" + SessionId+ "</SessionId></APSelectWithColumnNames_Input>";
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
	public static String ExecuteQuery_APSelectwithparam(String sQry,String params, String cabinetName, String sessionId)
	{
	String sInputXML = "<?xml version='1.0'?><APSelectWithNamedParam_Input>"
			+ "<option>APSelectWithNamedParam</option>" + "<Query>" + sQry + "</Query>" + "<Params>" + params
			+ "</Params>" + "<EngineName>" + cabinetName + "</EngineName>" + "<SessionID>" + sessionId
			+ "</SessionID>" + "</APSelectWithNamedParam_Input>";
	return sInputXML;
	}
%>
<%
try{
		executeCalls("ACCOUNT_SUMMARY");
	}
	catch(Exception e){
		System.out.println("Exception occured in integration Pl: "+e.getMessage());
		e.printStackTrace();
	}
	
%>	
<!DOCTYPE html>
<html>
	<head>
		<link href="${pageContext.request.contextPath}/customJS/checklistCSS.css" rel="stylesheet" type="text/css">
		<!-- Bootstrap CSS and bootstrap datepicker CSS used for styling the demo pages-->
        
		<script src="${pageContext.request.contextPath}/customJS/jquery/jquery-1.12.3.js"></script>	
		<script src="${pageContext.request.contextPath}/customJS/jquery/jquery-ui.js"></script>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/customJS/jquery/jquery-ui.css" type="text/css" />
		<script src="${pageContext.request.contextPath}/customJS/jquery/jquery-ui.min.js"></script>
		<script type="text/javascript">
		
		
    Base.esapi.properties.application.Name = "My Application v1.0";
    
    
    // Initialize the api
    org.owasp.esapi.ESAPI.initialize();
	
		var req_name_encode = '<%=request_name%>';
		var sessionId = '<%=sSessionId%>';
		var wi_name_encode = '<%=wi_name%>';
		var activityName_encode = '<%=activityName%>';
		//var param_json_encode = '<%=param_json%>';
		var parentWiName_encode = '<%=parentWiName%>';
		var WD_UID = '<%=WD_UID%>';
		var req_name = ($ESAPI.encoder().decodeForHTML(req_name_encode));
	var activityName = ($ESAPI.encoder().decodeForHTML(activityName_encode));
	//var param_json = ($ESAPI.encoder().decodeForHTML(param_json_encode));
	var wi_name = ($ESAPI.encoder().decodeForHTML(wi_name_encode));
	var parentWiName = ($ESAPI.encoder().decodeForHTML(parentWiName_encode));
	var WD_UID = ($ESAPI.encoder().decodeForHTML(WD_UID_encode));
	var param_json ='<%=param_json%>';

		function trimStr(str) {
                       return str.replace(/^\s+|\s+$/g, '');
            }
			
		function loadform() {
			var dhtml="";
			dhtml+="<p style='color:#e62e00'>Please do not Close the window as transaction is in process.Use close button once the transaction completed.</p><div style='overflow-x:hidden;overflow-y:hidden;height:400px'>"+
				"<table id='mytab' border='1' bordercolor='#337ab7' width='500px' max-height='400px'>";
				dhtml+="<tr><th width ='320px' >Call Name</th><th width ='200px'>Call Status</th><th width ='80px'>Re-try </th></tr>";
				var req_name_arr = req_name.split(',');
				for (var i=0;i<req_name_arr.length;i++){
					dhtml+="<tr><td width = 33% >"+req_name_arr[i]+" </td><td width = 33%><label color:#ffffff id = call_status_"+req_name_arr[i]+">In Process</label></td><td width = 33%><input type ='button' name = 'retry_btn_"+req_name_arr[i]+"' id = 'retry_btn_"+req_name_arr[i]+"' value = 'Re-try' style='visibility:hidden;' onclick = 'integ(this.id)' /></td></tr>";
				}
				dhtml+="</table><input type='button' name='Close_btn' id='Close_btn' value='Close' style='visibility:hidden;background-color:#0c658e; color:#ffffff' onclick='final_close();'/></div>";
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
								alert("Some error occured. Please try after sometime: URL doesn't exist!");
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
			
			var call_name = btn_id.substring(10,btn_id.length);
			var result = null;
				var finalxmlResponse = null;
				$.ajax({
					type: "GET",
					url: "/webdesktop/custom/CustomJSP/MultipleAccNo.jsp",
					data: { wi_name:parentWiName,WD_UID : WD_UID } ,
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
					url: "/webdesktop/custom/CustomJSP/CustExpose_Output_AccountPL.jsp",
					data: { result: result, wi_name : wi_name ,accNo:accNo,parentWiName:parentWiName, WD_UID : WD_UID} ,
					dataType: "text",
					async: true,
					success: function (response) {
						var status = "Success";
						window.opener.document.getElementById('aecb_call_status').value = status;	
						window.opener.document.getElementById('ExtLiability_IFrame_internal').src='/webdesktop/custom/CustomJSP/internal_liability.jsp?WD_UID='+WD_UID;												
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
					url: "/webdesktop/custom/CustomJSP/MultipleCifId.jsp",
					data: { wi_name:parentWiName , req_name : req_name , activity : activityName , child_wi: wi_name , WD_UID : WD_UID} ,
					dataType: "text",
					async: true,
					success: function (response) {
					
					//condition added by akshay on 31/1/18
						if(trimStr(response)=='NO DATA'){
								var eml_name = "call_status_"+req_name;		
								document.getElementById("Close_btn").style.visibility="visible";								
								document.getElementById(eml_name).innerHTML="Sucessfull";
								document.getElementById(eml_name).innerHTML="No Islamic Liability exists for this Customer!!";
								window.opener.com.newgen.omniforms.formviewer.setNGValue('Is_Overwrite_Details','SUCCESS');
								window.returnValue = "SUCCESS";		
																
						}
						else{
							cifIntegration(response);
							//console.log(response);
						}
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
			
			var cif_arr = trimStr(response).split(',');
			
			for (var j=0;j<cif_arr.length;j++)
			{
			
				cif_no = cif_arr[j];
				var req_name_arr = req_name.split(',');
					for (var i=0;i<req_name_arr.length;i++)
					{
						var eml_name = "call_status_"+req_name_arr[i];
						
						$.ajax({
							type: "GET",
							url: "/webdesktop/custom/CustomJSP/Requestxml_PL.jsp",
							data: { request_name: req_name_arr[i], is_mobility : "N", SeesionId:sessionId ,wi_name:wi_name ,activityName:activityName ,param_json:param_json,cifId:cif_no,header:'<%=headerOp.get("ACCOUNT_SUMMARY")%>',maincode:'<%=maincodeOp.get("ACCOUNT_SUMMARY")%>',footer:'<%=footerOp.get("ACCOUNT_SUMMARY")%>',parentTagName:'<%=parentTagNameOp.get("ACCOUNT_SUMMARY")%>', WD_UID : WD_UID} ,
							dataType: "text",
							async: true,
							success: function (response) {
								//alert('result cifIntegration: '+ response);
								set_result_val(response,"");
								document.getElementById("Close_btn").style.visibility="visible";
								document.getElementById(eml_name).innerHTML="Sucessfull";
								var status = "Success";
								window.opener.document.getElementById('Is_Overwrite_Details').value = status;
								console.log(response);
								//SetDataOnForm(response); //Tanshu
							},
							error: function (XMLHttpRequest, textStatus, errorThrown) {
								document.getElementById("retry_btn_"+req_name_arr[i]).style.visibility="visible";		
								
								var status = "Fail";
								document.getElementById("Close_btn").style.visibility="visible";
								window.opener.document.getElementById('Is_Overwrite_Details').value = status;
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
		
		function final_close(){
			window.close();
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