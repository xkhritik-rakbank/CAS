
<jsp:useBean id="wDSession" class="com.newgen.wfdesktop.session.WDSession" scope="session"/>
<jsp:useBean id="WDCabinet" class="com.newgen.wfdesktop.baseclasses.WDCabinetInfo" scope="session"/>
<%@ page import="java.util.*"%>
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
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.owasp.esapi.ESAPI"%>
<%@ page import="org.owasp.esapi.codecs.OracleCodec"%>
<%@ page import="org.owasp.esapi.User" %>


 
<!-- esapi4js i18n resources -->
<script type="text/javascript" language="JavaScript" src="${pageContext.request.contextPath}/custom/esapi4js/resources/i18n/ESAPI_Standard_en_US.properties.js"></script>
<!-- esapi4js configuration -->
<script type="text/javascript" language="JavaScript" src="${pageContext.request.contextPath}/custom/esapi4js/esapi-compressed.js"></script>
<script type="text/javascript" language="JavaScript" src="${pageContext.request.contextPath}/custom/esapi4js/resources/Base.esapi.properties.js"></script>
<!-- esapi4js core -->

<%
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader ("Expires", 0);
	//svt points start
	String input1 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("request_name"), 1000, true) );
	String request_name = ESAPI.encoder().encodeForSQL(new OracleCodec(), input1!=null?input1:"");
	//WriteLog("Integration jsp: request_name: "+request_name);
	//String sSessionId = request.getParameter("sessionId");
	String WD_UID = wDSession.getM_strUniqueUserId();
	
	
	String prod =null;
	String subprod =null;
	if(request_name.equalsIgnoreCase("InternalExposure") ||request_name.equalsIgnoreCase("ExternalExposure") ||request_name.equalsIgnoreCase("CollectionsSummary")||request_name.equalsIgnoreCase("CARD_INSTALLMENT_DETAILS"))
	{			
	
	 String input7 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("prod"), 1000, true) );
	 prod = ESAPI.encoder().encodeForSQL(new OracleCodec(), input7!=null?input7:"");
	
	 String input8 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("subprod"), 1000, true) );
	 subprod = ESAPI.encoder().encodeForSQL(new OracleCodec(), input8!=null?input8:"");
	}
		
	String input2 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("sessionId"), 1000, true) );
	String sSessionId = ESAPI.encoder().encodeForSQL(new OracleCodec(), input2!=null?input2:"");
	
	String input3 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("wi_name"), 1000, true) );
	String wi_name = ESAPI.encoder().encodeForSQL(new OracleCodec(), input3!=null?input3:"");
	
	String input4 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("activityName"), 1000, true) );
	String activityName = ESAPI.encoder().encodeForSQL(new OracleCodec(), input4!=null?input4:"");
	
	//String input5 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("param_json"), 1000, true) );
	//String param_json = ESAPI.encoder().encodeForSQL(new OracleCodec(), input5);
	  String param_json =request.getParameter("param_json");
	
	
	String input6 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("parentWiName"), 1000, true) );
	String parentWiName = ESAPI.encoder().encodeForSQL(new OracleCodec(), input6!=null?input6:"");
	//svt points end
	//WriteLog("Integration jsp: wi_name: "+wi_name);
	//WriteLog("Integration jsp: activityName: "+activityName);
	//WriteLog("Integration jsp: param_json: "+param_json);
	//WriteLog("Integration jsp: sSessionId: "+sSessionId);
	//WriteLog("Integration jsp: parentWiName: "+parentWiName);
	
%>

<%!	

Map<String,String> maincodeOp = new HashMap<String,String>();
	Map<String,String> headerOp = new HashMap<String,String>();
	Map<String,String> footerOp = new HashMap<String,String>();
	Map<String,String> parentTagNameOp = new HashMap<String,String>();
	String SessionId = "-1711340385";
	String appServerType="";
		String wrapperIP = "";
		String wrapperPort = "";
		String cabinetName = "";
	public void executeCalls(String req_name){
		String sQuery_header = "SELECT Header as Call_header,Footer,parenttagname FROM NG_Integration with (nolock) where Call_name='"+req_name+"'";
		
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
		String request_Arr[]=request_name.split(",");
		if(request_name.equals("")){
		executeCalls("FINANCIAL_SUMMARY");
		}
		else if(request_Arr.length>0){
			for(int i=0;i<request_Arr.length;i++){
				executeCalls(request_Arr[i]);
			}
			executeCalls("CollectionsSummary");
			executeCalls("CARD_INSTALLMENT_DETAILS");
		}
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
        <script src="${pageContext.request.contextPath}/custom/js/json2.js"></script>	
		<script src="${pageContext.request.contextPath}/custom/jquery/jquery-1.12.3.js"></script>	
		<script src="${pageContext.request.contextPath}/custom/jquery/jquery-ui.js"></script>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/custom/jquery/jquery-ui.css" type="text/css" />
		<script src="${pageContext.request.contextPath}/custom/jquery/jquery-ui.min.js"></script>
		<script type="text/javascript">
		
	//svt points start
    Base.esapi.properties.application.Name = "My Application v1.0";
    
    
    // Initialize the api
    org.owasp.esapi.ESAPI.initialize();
	
		var req_name_encode = '<%=request_name%>';
		var sessionId_encode = '<%=sSessionId%>';
		var wi_name_encode = '<%=wi_name%>';
		var activityName_encode = '<%=activityName%>';
		//var param_json_encode = '<%=param_json%>';
		var prod_encode = '<%=prod%>';
		var subprod_encode = '<%=subprod%>';
		var parentWiName_encode = '<%=parentWiName%>';
		var WD_UID_encode = '<%=WD_UID%>';
		var req_name='';
		if(req_name_encode!=''){
			req_name = ($ESAPI.encoder().decodeForHTML(req_name_encode));	
		}
		
	var sessionId = ($ESAPI.encoder().decodeForHTML(sessionId_encode));
	var wi_name = ($ESAPI.encoder().decodeForHTML(wi_name_encode));
	var activityName = ($ESAPI.encoder().decodeForHTML(activityName_encode));
	//var param_json = ($ESAPI.encoder().decodeForHTML(param_json_encode));
	var prod = ($ESAPI.encoder().decodeForHTML(prod_encode));
	var subprod = ($ESAPI.encoder().decodeForHTML(subprod_encode));
	var parentWiName = ($ESAPI.encoder().decodeForHTML(parentWiName_encode));	
	var WD_UID = ($ESAPI.encoder().decodeForHTML(WD_UID_encode));
	var param_json = '<%=param_json%>';
	//svt points end
		var Account_track ='';
		var row_count = 1;		
		var aecb_rep_url;
		function loadform() {
		var req_name_arr = req_name.split(',');
		var visibility="hidden";
			if(req_name.indexOf('InternalExposure')>-1 || req_name.indexOf('CollectionsSummary')>-1 ||req_name.indexOf('ExternalExposure')>-1||req_name.indexOf("CARD_INSTALLMENT_DETAILS")>-1){
				integration_all();
			}
			else{
				integration_all_Financial();
			}		
		}
		
		function calculateLiabilityValues(){
			var Sub_product=window.opener.getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2);
			var applicationCategory = window.opener.getLVWAT('cmplx_CompanyDetails_cmplx_CompanyGrid',1,21);
			var app_type = window.opener.getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4);
			
			if(Sub_product !=null && app_type !=null 
					&& (Sub_product=="BTC" || 
							(Sub_product=="PA" && app_type != "RELT" && app_type != "RELTN" ) ||
							(Sub_product=="SE" && applicationCategory== "BAU"))){
				
				Set_Borrowing_Rel_comp();
				Set_Borrowing_Rel_auth();
			}if(Sub_product !=null && Sub_product=="BTC"){						
				Set_BTC_Tot_exposure();
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
			cell1.width = "320px";
			cell2.width = "200px";
			cell3.width = "80px";
			var org_CIF=cifID;
			if(req_name_arr!=''){
			if((req_name_arr=='AVGBALDET' || req_name_arr=='LIENDET' || req_name_arr=='RETURNDET' || req_name_arr=='SALDET' || req_name_arr=='SIDET' || req_name_arr=='TRANSUM') && cifID!='')
			{
			var cif_acc_Arr=null;
			var cifId_arr = cifID.split(':');
			cifID=cifId_arr[1];
			}
			if(req_name_arr=='ExternalExposure' && cifID=='')
			{
			cifID='Individual';
			}
			cell1.innerHTML = "<label id = call_name_"+row_count+">"+req_name_arr+" for "+cifID+"</label>";
			cell3.innerHTML = "<input type ='button' name = 'retry_btn_"+row_count+"' id = 'retry_btn_"+row_count+"' value = 'Re-try' style='visibility:hidden;' />";
			document.getElementById('retry_btn_'+row_count).onclick = function(){integ(req_name_arr,org_CIF);}
			}
			else{
			cell1.innerHTML ="<label id = call_name_"+row_count+">"+cifID+"</label>";
			}
			//condition added by akshay on 31/1/18
			if(cifID==''){
				cell1.innerHTML ="<label id = call_name_"+row_count+">"+req_name_arr+"</label>";
			}	
			else{
				cell1.innerHTML = "<label id = call_name_"+row_count+">"+req_name_arr+" for "+cifID+"</label>";
			}
			
			cell2.innerHTML = "<label color:#ffffff id = call_status_"+row_count+">In Process</label>";
		}
		//added
		
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
								alert("Some error occured. Please try after sometime: URL doesn't exist!");
							else 
								alert("Some error occured. Please try after sometime or contact administrator : subprocess");	
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
				alert("Some error occured. Please try after sometime or contact administrator : subprocess-e");
				//return false;
			}
		}
		
		function integ( req_name_arr,cifID ) {
			document.getElementById("continue_btn").style.visibility="hidden";
			document.getElementById("Close_btn").style.visibility="hidden";
			cifIntegration(req_name_arr,cifID);
		}
		
		function set_result_val_withInterval(result,row_count,CardNumber,cifId,cust_type,req_name){
		
			var eml_name='call_status_' + trimStr(row_count)+'';
			var loop_count=0;
			//below code added by nikhil 11/08/18
			var time_out=5000;
			if(req_name.indexOf('Exposure')>-1)
			{
				time_out=6000;
			}
			var time_intr = setInterval(function(){
				if(document.getElementById(eml_name).innerHTML=="Failure" || document.getElementById(eml_name).innerHTML=="Sucessfull"){
					check_complete();
					clearInterval(time_intr);
				}
				else if(loop_count<20 && document.getElementById(eml_name).innerHTML=='In Process'){
					set_result_val(result,row_count,CardNumber,cifId,cust_type);
				}
				else if(loop_count>19 && document.getElementById(eml_name).innerHTML=='In Process'){
						document.getElementById(eml_name).innerHTML="Failure";
						if(document.getElementById("retry_btn_"+trimStr(row_count))){
							document.getElementById("retry_btn_"+trimStr(row_count)).style.visibility="visible";
						}
						check_complete();
						clearInterval(time_intr);
				}
				loop_count++;
			},time_out);
			
		}
		
		function set_result_val(result,row_count,CardNumber,cifId,cust_type)
		{
				if(req_name == 'CARD_INSTALLMENT_DETAILS'){
							data={ result: result, wi_name : wi_name, prod : prod, subprod : subprod,is_mobility : "N", SeesionId:sessionId,row_count:row_count,cifId:cifId,CardNumber:CardNumber,parentWiName:parentWiName,cust_type:cust_type , WD_UID : WD_UID}//svt points
							}
							else{
							data= { result: result, wi_name : wi_name, prod : prod, subprod : subprod,is_mobility : "N", SeesionId:sessionId,row_count:row_count,cifId:cifId,parentWiName:parentWiName,cust_type:cust_type, WD_UID : WD_UID} //svt points
							}
			$.ajax({
					type: "POST",
					url: "/webdesktop/custom/CustomJSP/CustExpose_Output_PL.jsp",
					data: data,
					dataType: "text",
					async: false,
					success: function (response) {
						var flag_value=response.split('@#');
						var last4 = flag_value[0].slice(-4);
						var eml_name='call_status_' + trimStr(flag_value[1])+'';
						var failure_call = 'call_name_' + trimStr(flag_value[1])+'';
						if(last4=="true"||last4=="0000"){
							document.getElementById(eml_name).innerHTML="Sucessfull";
							//if(document.getElementById('callname_'+trimStr(flag_value[1])).innerHTML.indexOf('External')>-1){

							//window.returnValue = "SUCCESS";
							/*Aecb_report_url(result);
							 if(aecb_rep_url){
								var returnVal={aecb_call_status:"SUCCESS",ReportUrl:aecb_rep_url};
							}
							 else{*/
								var returnVal="SUCCESS";
							 //}
							  if(result.indexOf('CAS')>-1 && result.indexOf('MQ_RESPONSE_XML')==-1){
								var wparams="Code=="+result;
								var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
								var query='';
								if(window.opener.window.parent.pid.indexOf('CAS')==0){
									query='XmlLogIntegration_RLOS';
								}
								else if(window.opener.window.parent.pid.indexOf('CC')==0 || window.opener.window.parent.pid.indexOf('Cr')==0){
									query='XmlLogIntegration_CC';
								}
								else if(window.opener.window.parent.pid.indexOf('CDOB')==0){
									query='XmlLogIntegration_CDOB';
								}
								else if(window.opener.window.parent.pid.indexOf('PL')==0){
									query='XmlLogIntegration_PL';
								}
								
								var params="Query="+query+"&wparams="+wparams+"&pname="+'CCTemplateData';
								var integ_Query=CallAjax(jspName,params);
								var fields = integ_Query.split('#');
								result = fields[0];
								result = result.substring(result.indexOf("<MQ_RESPONSE_XML>")+17,result.indexOf("</MQ_RESPONSE_XML>"));
							}
							else if(result.indexOf('MQ_RESPONSE_XML')>-1){
								result = result.substring(result.indexOf("<MQ_RESPONSE_XML>")+17,result.indexOf("</MQ_RESPONSE_XML>"));
							} 
						
								
							
							  Set_Score_Range();			  
							  setFormHeader();
							SetDataOnForm(result);
							window.returnValue ='SUCCESS';
							//added by nikhil for SP 2.1
							var call_status=window.opener.getNGValue('all_call_status');
							if(call_status.indexOf(document.getElementById(failure_call).innerHTML + "#")>-1)
							{
								call_status=call_status.replace((document.getElementById(failure_call).innerHTML + "#"),"");
							}
							window.opener.setNGValueCustom('all_call_status',call_status);
						   window.opener.setNGValueCustom('aecb_call_status','SUCCESS');
						   
							
						}
						else if(flag_value[0]=="waiting"){
							return 'waiting';
							//alert('waiting from rep');
						}
						else if(flag_value[0]=="Failure" || flag_value[0]=="false")
						{	document.getElementById(eml_name).innerHTML="Failure";
							if(document.getElementById("retry_btn_"+trimStr(flag_value[1]))){
							document.getElementById("retry_btn_"+trimStr(flag_value[1])).style.visibility="visible";
							}		
							var call_status=window.opener.getNGValue('all_call_status');
							if(call_status.indexOf(document.getElementById(failure_call).innerHTML + "#")==-1)
							{
							call_status= call_status + document.getElementById(failure_call).innerHTML + "#";
							window.opener.setNGValueCustom('all_call_status',call_status);
							}
							window.returnValue = "FAIL";	
						}
						else{
							document.getElementById(eml_name).innerHTML=flag_value[0];
							
							if(flag_value[0]==""||flag_value[0]=="null"){
								document.getElementById(eml_name).innerHTML="Failure";
							}
							Set_Score_Range();
							setFormHeader();
							var call_status=window.opener.getNGValue('all_call_status');
							if(call_status.indexOf(document.getElementById(failure_call).innerHTML + "#")>-1)
							{
								call_status=call_status.replace((document.getElementById(failure_call).innerHTML + "#"),"");
							}
							window.opener.setNGValueCustom('all_call_status',call_status);
						}
						check_complete();
					},
					error: function (XMLHttpRequest, textStatus, errorThrown) {
						var status = "Fail";
						alert('Exception: '+ errorThrown);
					}
				});
				
		}
		
		function Aecb_report_url(result){
			try{
				result = result.substring((result.indexOf('<MQ_RESPONSE_XML>')+'<MQ_RESPONSE_XML>'.length),result.indexOf('</MQ_RESPONSE_XML>'));
				xmlDoc = $.parseXML(result),
				$xml = $( xmlDoc ),
				$RequestType = $xml.find("RequestType");
				if($RequestType.text()=='ExternalExposure'){
					$ReportUrl =  $xml.find("ReportUrl");
					$ScoreInfo = $xml.find("ScoreInfo");
					window.opener.document.getElementById("cmplx_Liability_New_Aecb_Report_Url").value =$ReportUrl.text() ;
					aecb_rep_url = $ReportUrl.text();
					//window.opener.com.newgen.omniforms.formviewer.setNGValue('cmplx_Liability_New_Aecb_Report_Url',aecb_rep_url);
					//chnages done by shivang for 21.
					//ScoreInfo = $ScoreInfo.text();
					aecb_score =$ScoreInfo.find('Value');
					aecb_score=$aecb_score.text();
					range =$ScoreInfo.find('Range');
					range=$range.text();
					$ReferenceNumber = $xml.find("ReferenceNumber");
					refNo = $ReferenceNumber.text();
					//window.opener.setNGValueCustom('cmplx_Liability_New_Aecb_Report_Url',aecb_rep_url);
					window.opener.setNGValueCustom('cmplx_Liability_New_AECBScore',aecb_score);
					window.opener.setNGValueCustom('cmplx_Liability_New_Range',range);
					window.opener.setNGValueCustom('cmplx_Liability_New_ReferenceNo',refNo);
					/* window.opener.setLocked('cmplx_Liability_New_AECBScore',true);
					window.opener.setLocked('cmplx_Liability_New_Range',true);
					window.opener.setLocked("cmplx_Liability_New_ReferenceNo", true); */
				}
			}
			catch(e){
			}
			
		}
		function Set_Borrowing_Rel_comp(){
			var wparams="Child_wi=="+window.opener.window.parent.pid;
			var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
			var query='Borrowing_Rel_comp';
			var params="Query="+query+"&wparams="+wparams+"&pname="+'CCTemplateData';
			var integ_Query=CallAjax(jspName,params);
			var field = integ_Query.split('#');
			//WriteLog("Shweta 1: "+ field);
			if(field.length>0){
				var no_borrowing_comp = field[0]=='undefined' || field[0]=='NODATAFOUND' || field[0]=='ERROR'?'':field[0];
				window.opener.setNGValueCustom('cmplx_Liability_New_NoofotherbankCompany',no_borrowing_comp);		
			}
		}
		function Set_Borrowing_Rel_auth(){
			var wparams="Child_wi=="+window.opener.window.parent.pid;
			var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
			var query='Borrowing_Rel_auth';
			var params="Query="+query+"&wparams="+wparams+"&pname="+'CCTemplateData';
			var integ_Query=CallAjax(jspName,params);
			var field = integ_Query.split('#');
			//WriteLog("Shweta 1: "+ field);
			if(field.length>0){
				var no_borrowing_auth = field[0]=='undefined' || field[0]=='NODATAFOUND' || field[0]=='ERROR'?'':field[0];
				window.opener.setNGValueCustom('cmplx_Liability_New_NoofotherbankAuth',no_borrowing_auth);	
			}
		}
		function Set_BTC_Tot_exposure(){
			var wparams="Child_wi=="+window.opener.window.parent.pid;
			var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
			var query='BTC_Tot_Exposure';
			var params="Query="+query+"&wparams="+wparams+"&pname="+'CCTemplateData';
			var integ_Query=CallAjax(jspName,params);
			var field = integ_Query.split('#');
			//WriteLog("Shweta 1: "+ field);
			if(field.length>0){
				var btc_tot_expo = field[0]=='undefined' || field[0]=='NODATAFOUND' || field[0]=='ERROR'?'':field[0];
				window.opener.setNGValueCustom('cmplx_Liability_New_BTCTotExpsure',btc_tot_expo);	
			}
		}
		function Set_Score_Range()
		{
			var wparams="Child_wi=="+window.opener.window.parent.pid;
			var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
			var query='Score_Range';
			var params="Query="+query+"&wparams="+wparams+"&pname="+'CCTemplateData';
			var integ_Query=CallAjax(jspName,params);
			var fields = trimStr(integ_Query).split('#');
			if(fields.length>2){
				var ReferenceNumber = fields[0]=='undefined' || fields[0]=='NODATAFOUND'?'':fields[0];
				var Aecb_score = fields[1]=='undefined' || fields[1]=='NODATAFOUND'?'':fields[1];
				var Range = fields[2]=='undefined' || fields[2]=='NODATAFOUND'?'':fields[2];
				window.opener.setNGValueCustom('cmplx_Liability_New_AECBScore',Aecb_score);
				window.opener.setNGValueCustom('cmplx_Liability_New_Range',Range);
				
				if(ReferenceNumber=='NODATAFOUND' || ReferenceNumber==''){
					window.opener.setNGValueCustom('cmplx_Liability_New_ReferenceNo',"");
				}else{
					window.opener.setNGValueCustom('cmplx_Liability_New_ReferenceNo',ReferenceNumber);
				}
			}
			
			var query='Score_Range_Comp';
			var params="Query="+query+"&wparams="+wparams+"&pname="+'CCTemplateData';
			var integ_Query=CallAjax(jspName,params);
			var fields = trimStr(integ_Query).split('#');
			if(fields.length>2){
				var ReferenceNumber_Comp = fields[0]=='undefined' || fields[0]=='NODATAFOUND'?'':fields[0];
				var Aecb_score_Comp = fields[1]=='undefined' || fields[1]=='NODATAFOUND'?'':fields[1];
				var Range_Comp = fields[2]=='undefined' || fields[2]=='NODATAFOUND'?'':fields[2];
				window.opener.setNGValueCustom('cmplx_Liability_New_Aecb_Score_Company',Aecb_score_Comp);
				window.opener.setNGValueCustom('cmplx_Liability_New_Aecb_range_Company',Range_Comp);
				
				if(ReferenceNumber_Comp=='NODATAFOUND' || ReferenceNumber_Comp==''){
					window.opener.setNGValueCustom('cmplx_Liability_New_ReferenceNo_Company',"");
				}else{
					window.opener.setNGValueCustom('cmplx_Liability_New_ReferenceNo_Company',ReferenceNumber_Comp);
				}
			}
		}	
			function setFormHeader()
		{//sv
			var wparams="Wi_Name=="+window.opener.window.parent.pid;
			var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
			var query='Score_header';
			var params="Query="+query+"&wparams="+wparams+"&pname="+'CCTemplateData';
			var integ_Query=CallAjax(jspName,params);
			var fields = integ_Query.split('#');
			var aecb = fields[0]=='undefined' || fields[0]=='NODATAFOUND'?'':fields[0];
			window.opener.setNGValueCustom('AECB_History',aecb);
			//WriteLog("sv: "+aecb);
			
		}
		var GlobalFlag = false;
		function integration_all (){
				var result = null;
				var finalxmlResponse = null;
				//svt points
				$.ajax({
					type: "POST",
					url: "/webdesktop/custom/CustomJSP/MultipleCifId.jsp",
					data: { wi_name:parentWiName , req_name : req_name , activity : activityName , child_wi: wi_name,sessionId:sessionId, WD_UID : WD_UID} ,
					dataType: "text",
					async: false,
					success: function (response) {
						cifIntegration(req_name,response);
						//console.log(response);
					},
					error: function (XMLHttpRequest, textStatus, errorThrown) {
						alert('Exception: '+ errorThrown);
					}
				});
				           			
			return result;
           }
		   
		   // below code done to find opertaion names of financial summary on 29th Dec by disha
		   // below changes done by disha 18-7-2016 for Pre-approved cases application type added in parameter
		   function fetchOperation_Name(sub_product,emp_type,acc_type,application_type)
		   {
				
				var ajaxReq;
				var dataFromAjax;				
				if (window.XMLHttpRequest) 
				{
					ajaxReq= new XMLHttpRequest();
				}
				else if (window.ActiveXObject)
				{
					ajaxReq= new ActiveXObject("Microsoft.XMLHTTP");
				}		
				
				var url = "/webdesktop/custom/CustomJSP/Fetch_OperationName.jsp";
				//svt points
				ajaxReq.open("POST", url, false);
				ajaxReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
				ajaxReq.send("sub_product="+sub_product+"&emp_type="+emp_type+"&acc_type="+acc_type+"&activityName="+activityName+"&SeesionId="+sessionId+"&application_type="+application_type+"&WD_UID="+WD_UID);
				if (ajaxReq.status == 200 && ajaxReq.readyState == 4)
				{
					dataFromAjax=ajaxReq.responseText;
					//alert("result copy data "+ dataFromAjax);					
				}
				else
				{
					alert("INVALID_REQUEST_ERROR : "+ajaxReq.status);
					dataFromAjax="";
				}
		   
				return dataFromAjax;	   
		   }
		   
		   // above code done to find opertaion names of financial summary on 29th Dec by disha
		   
		    // below changes done by disha on 27-02-2018 for Collection summary should be run for the linked cif as well
		   function fetchLinkedCif()
		   {				
				var ajaxReq;
				var dataFromAjax;				
				if (window.XMLHttpRequest) 
				{
					ajaxReq= new XMLHttpRequest();
				}
				else if (window.ActiveXObject)
				{
					ajaxReq= new ActiveXObject("Microsoft.XMLHTTP");
				}		
								
				var url = "/webdesktop/custom/CustomJSP/FetchLinkedCif.jsp";
				//svt points
				ajaxReq.open("POST", url, false);
				ajaxReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
				ajaxReq.send("wi_name="+parentWiName+"&SeesionId="+sessionId+"&child_wi="+wi_name+"&activityName="+activityName+"&WD_UID="+WD_UID);
				if (ajaxReq.status == 200 && ajaxReq.readyState == 4)
				{
					dataFromAjax=ajaxReq.responseText;
					//alert("result copy data "+ dataFromAjax);					
				}
				else
				{
					alert("INVALID_REQUEST_ERROR : "+ajaxReq.status);
					dataFromAjax="";
				}
		   
				return trimStr(dataFromAjax);	   
		   }
		   // above changes done by disha on 27-02-2018 for Collection summary should be run for the linked cif as well
		   
		   function integration_all_Financial (){
				var result = null;
				var finalxmlResponse = null;
				$.ajax({
					type: "POST",
					url: "/webdesktop/custom/CustomJSP/MultipleCifId_accId.jsp",
					data: { wi_name:wi_name , SeesionId:sessionId} ,
					dataType: "text",
					async: false,
					success: function (response) {
						//condition added by akshay on 31/1/18
						if(trimStr(response)=='NO DATA'){
								var jsondata = JSON.parse(decodeURIComponent(param_json));
								var companyCif=jsondata["company_cif"]; 
								var sub_product=jsondata["sub_product"]; 
								var emp_type=jsondata["emp_type"]; 
								var application_type=jsondata["application_type"]; 
								if(companyCif=='' || companyCif==null){
									req_name = fetchOperation_Name(sub_product,emp_type,'Individual_CIF',application_type);
								}
								else{
									req_name = fetchOperation_Name(sub_product,emp_type,'',application_type);
								}
							var req_name_arr = req_name.split(',');
							for (var i=0;i<req_name_arr.length;i++)
							{								
								dynamicTable(req_name_arr[i],'',row_count);
								var eml_name='call_status_' +row_count+'';
								document.getElementById(eml_name).innerHTML="No Account exists for this Customer!!";
								document.getElementById("Close_btn").style.visibility="visible";								
								row_count++;
							}
							window.opener.document.getElementById("aecb_call_status").value="FAIL";
							window.returnValue = "FAIL";	
							document.getElementById("Close_btn").style.visibility="visible";
						}
						else{
							if(response.indexOf(",")>-1){
								var response_arr = response.split(",");
								//for(var int_count = 0; int_count<response_arr.length;int_count++){
									executeFinCalls(req_name,response_arr);
										//cifIntegration(req_name,response_arr[int_count]);
								//}
							}
							else{
								cifIntegration(req_name,response);
								document.getElementById("Close_btn").style.visibility="visible";
							}
							
							//console.log(response);
						}
					},
					error: function (XMLHttpRequest, textStatus, errorThrown) {
						alert('Exception: '+ errorThrown);
					}
				});
				           			
			return result;
           }
		  
	function executeFinCalls(req_name,response_arr){
		   
			cifIntegration(req_name,response_arr[0]);
			setInterval(function(){
				if(check_complete_fincall(response_arr[0])){
					response_arr.shift();
					if(response_arr.length>0){
							cifIntegration(req_name,response_arr[0]);
					}
					else{
						document.getElementById("Close_btn").style.visibility="visible";
					}
				}
			},1000);
	   }
	   
	   function check_complete_fincall(cifAcc){
			if(document.getElementById("dyn_tbl_row")!=undefined){
					var table = document.getElementById("dyn_tbl_row");
					var tbl_rowcount = table.rows.length;
					var return_flag ='';
					for(var scount=0; scount<=tbl_rowcount-1;scount++){
						if(table.rows[scount].childNodes[0].innerText.indexOf(cifAcc)>-1 && table.rows[scount].childNodes[1].innerText=='In Process'){
								return_flag = "Incomplete";
						}
						break;
					}
					if(return_flag!="Incomplete"){
						return true;
					}
					else{
							return false;
					}
			}
			return true;
		}		  
		   
 function cifIntegration(req_name,response)
		   {
			var result = null;
			var cifID = '';
			var acc_id='';
			var CardNumber = '';
			var jsondata = JSON.parse(param_json);
			var integration_call_flag = "";
			//Changes done by aman for AECB Start
			var companyCif=jsondata["cif"]; 
			var cust_type = "Individual_CIF";
			var TLNo=jsondata["trade_lic_no"]; 
			var primary_Cif=jsondata["cmplx_Customer_CIFNO"]; 
			// below code done to find opertaion names of financial summary on 29th Dec by disha
			var sub_product=jsondata["sub_product"]; 
			var emp_type=jsondata["emp_type"]; 
			var guarantor_CIF=jsondata['guarantor_CIF'];
			var application_type=jsondata['application_type']; // changes done by disha 18-7-2018 for pre approved
			// below changes done by disha on 27-02-2018 for Collection summary should be run for the linked cif as well
			var provider_no = jsondata["wi_name"];
			var Individual_Aecb = jsondata["Individual_Aecb"];
			var Corporate_Aecb = jsondata["Corporate_Aecb"];
			var linkedCifs = null;
			response = trimStr(response);
			if (response == 'NO DATA')
			{
			response='';
			}
			/*if (companyCif !=''&& companyCif !='null' && (req_name =='InternalExposure,ExternalExposure' || req_name =='CollectionsSummary')){
				response = response + ',' + companyCif;			  
			}*/
			if(guarantor_CIF!='' && guarantor_CIF !='null' && guarantor_CIF != undefined && guarantor_CIF !='undefined'&& (req_name !='CollectionsSummary'&& req_name !='CARD_INSTALLMENT_DETAILS'&& req_name !=''))
			{
				if(response!=''){
					response = response + ',' + guarantor_CIF;			  
				}
				else{
					response = guarantor_CIF;			  
				}
			}	
			else if (companyCif !=''&& companyCif !='null' && (req_name =='InternalExposure,ExternalExposure')){
				response = response + ',' + companyCif;			  
			}		
			else if (companyCif !=''&& companyCif !='null' && (req_name =='CollectionsSummary'))
			{		
				response = trimStr(response);
				if(response.indexOf(companyCif)==-1)
				{
				if(response!=''){
					response = response + ',' + companyCif;			  
				}
				else{
					response = companyCif;			  
				}
				}
				/*linkedCifs = trimStr(fetchLinkedCif());
					if(linkedCifs !='' && linkedCifs !='null')
					{
						response = response + ',' + linkedCifs;
					}*/
			}

			// above changes done by disha on 27-02-2018 for Collection summary should be run for the linked cif as well
			
			else if(TLNo!='' && TLNo!=null && companyCif=='' && req_name!='CARD_INSTALLMENT_DETAILS')
			{
				if(response!=''){
					response = response+','+'Corporate';
				}
				else{
					response = 'Corporate';			  
				}
			}
			//Changes done by aman for AECB End
			else if(req_name == 'ExternalExposure' && companyCif !=''&& companyCif !='null'){//by shweta for PL
				/*
				response = jsondata["cmplx_Customer_CIFNO"];
				if(trimStr(response)!='' && trimStr(response)!='null'){
					response = response + ',' + companyCif;
				}
				*/
				response = companyCif;
			}
			if(response=='Corporate' && primary_Cif=='')
			{
			response=",Corporate";
			}
			var cif_acc_Arr=null;
			var cifId_arr = response.split(',');
			var dhtml="";
			
			for (var j=0;j<cifId_arr.length;j++)
			{
				//changes condition added by Deepak 22May2019 as below need to be set for external exposure only.
				/*if(req_name == 'ExternalExposure'){
					//changes done by Deepak to make provider no unique start
					jsondata["wi_name"] = provider_no+Math.floor((Math.random()*90)+10);
					//Changes done by Aman to save AECB provide no.
					window.opener.com.newgen.omniforms.formviewer.setNGValue('cmplx_Liability_New_AECB_Provider_Number',jsondata["wi_name"]);
					param_json = JSON.stringify(jsondata);
					//changes done by Deepak to make provider no unique end
				}*/
				
				
				if(cifId_arr[j].indexOf(":")>-1){
					cif_acc_Arr = cifId_arr[j].split(":");
					cifID = trimStr(cif_acc_Arr[0]);
					acc_id = cif_acc_Arr[1];
					
					// below code done to find opertaion names of financial summary on 29th Dec by disha
					acc_type = cif_acc_Arr[2];
					req_name = trimStr(fetchOperation_Name(sub_product,emp_type,acc_type,application_type));
					if(Account_track.indexOf(acc_id+'#')>-1)
					{
						req_name='';
					}
					else
					{
					Account_track = Account_track +acc_id+'#';	
					}					
					// above code done to find opertaion names of financial summary on 29th Dec by disha
				}
				else{
					cifID = trimStr(cifId_arr[j]);
				}
				
				var req_name_arr = req_name.split(',');
						if(guarantor_CIF!="null" && guarantor_CIF!="" && guarantor_CIF != undefined && guarantor_CIF !='undefined' && cifID.indexOf(guarantor_CIF)>-1){
						req_name_arr= ("InternalExposure").split(',');
					
					}
					for (var i=0;i<req_name_arr.length;i++)
					{
						if(req_name_arr[i] == 'ExternalExposure'){
						//changes done by Deepak to make provider no unique start
						jsondata["wi_name"] = provider_no+Math.floor((Math.random()*90)+10);
						//Changes done by Aman to save AECB provide no.
						window.opener.com.newgen.omniforms.formviewer.setNGValue('cmplx_Liability_New_AECB_Provider_Number',jsondata["wi_name"]);
						param_json = JSON.stringify(jsondata);
						//changes done by Deepak to make provider no unique end
					}
						if((req_name_arr[i] != 'ExternalExposure' && cifID.indexOf(companyCif)<0)||(req_name_arr[i] == 'ExternalExposure' && (cifID.indexOf(jsondata["cmplx_Customer_CIFNO"])>-1)||cifID.indexOf(companyCif)>-1))
						{
							
							if(req_name == 'CARD_INSTALLMENT_DETAILS' && cifID!='Corporate'){

								data={ request_name: "CARD_INSTALLMENT_DETAILS", is_mobility : "N", SeesionId:sessionId ,wi_name:wi_name ,activityName:activityName ,param_json:param_json,cifId:jsondata["cmplx_Customer_CIFNO"],acc_id:acc_id,row_count:row_count,CardNumber:cifID,parentWiName:parentWiName,header:'<%=headerOp.get("CARD_INSTALLMENT_DETAILS")%>',maincode:'<%=maincodeOp.get("CARD_INSTALLMENT_DETAILS")%>',footer:'<%=footerOp.get("CARD_INSTALLMENT_DETAILS")%>',parentTagName:'<%=parentTagNameOp.get("CARD_INSTALLMENT_DETAILS")%>'}
								integration_call_flag='Y';
							}
							else{
								//Changes done by aman for AECB	Start
								//change by saurabh on 25th jan. changed = with indexOf for company cif as company cif is coming with a preceding ','. Hence = not working.
								if(cifID=='Corporate' || (companyCif && companyCif.indexOf(cifID)>-1 && cifID!="null" && cifID!="")){
									cust_type = "Corporate_CIF";
								}
								//below  condition modified by deepak to trigger only 1 exposure call when multiple CIF's are identified. PCAS-1777
								if (req_name_arr[i] == 'ExternalExposure' && (primary_Cif==cifID ||(primary_Cif=="" && cifID=='NO DATA') || (companyCif.indexOf(cifID)>-1 && cifID!="null" && cifID!="") ))
								 {
								 if((primary_Cif==cifID ||(primary_Cif=="" && cifID=='NO DATA')) && Individual_Aecb=='N')
									{
										integration_call_flag="N";
									}
									else if((companyCif.indexOf(cifID)>-1 && cifID!="null" && cifID!="") && Corporate_Aecb=='N')
									{
										integration_call_flag="N";
									}
									else
									{//svt points
									data= { request_name: req_name_arr[i], is_mobility : "N", SeesionId:sessionId ,wi_name:wi_name ,activityName:activityName ,param_json:param_json,cifId:cifID,acc_id:"B01",row_count:row_count,Customer_Type:cust_type,parentWiName:parentWiName,header:'<%=headerOp.get("ExternalExposure")%>',maincode:'<%=maincodeOp.get("ExternalExposure")%>',footer:'<%=footerOp.get("ExternalExposure")%>',parentTagName:'<%=parentTagNameOp.get("ExternalExposure")%>', WD_UID : WD_UID}
									integration_call_flag="Y";
									}
								 }
								 //added for External call of Company when cif is not available
								 else if(req_name_arr[i] != 'ExternalExposure' && cifID=='Corporate'){
								 	integration_call_flag="N";
								 }
								 //added for External call of Company when cif is not available
								//below one condition Added by deepak to trigger only 1 exposure call when multiple CIF's are identified. PCAS-1777
								else if(req_name_arr[i] == 'ExternalExposure' && primary_Cif!=cifID && cifID!='Corporate' ){
									integration_call_flag="N";
								 }
								  else if(req_name_arr[i] == 'ExternalExposure' && cifID=='Corporate'){
								 	//svt points
								 	data= { request_name: req_name_arr[i], is_mobility : "N", SeesionId:sessionId ,wi_name:wi_name ,activityName:activityName ,param_json:param_json,cifId:cifID,acc_id:acc_id,row_count:row_count,Customer_Type:cust_type,parentWiName:parentWiName,header:'<%=headerOp.get("ExternalExposure")%>',maincode:'<%=maincodeOp.get("ExternalExposure")%>',footer:'<%=footerOp.get("ExternalExposure")%>',parentTagName:'<%=parentTagNameOp.get("ExternalExposure")%>', WD_UID : WD_UID}
									integration_call_flag="Y";
								 }
								 else if(req_name_arr[i] == 'InternalExposure'  && (cifID=="null" || cifID=="")){
									integration_call_flag="N";
								 }
								 else if(req_name_arr[i] != 'ExternalExposure'){
								 if(req_name_arr[i]=='TRANSUM' || req_name_arr[i]=='AVGBALDET' || req_name_arr[i]=='RETURNDET' || req_name_arr[i]=='LIENDET' || req_name_arr[i]=='SIDET' || req_name_arr[i]=='SALDET' || req_name_arr[i]=='Primary_CIF' || req_name_arr[i]=='Corporation_CIF'){
									 //svt points
									 data= { request_name: req_name_arr[i], is_mobility : "N", SeesionId:sessionId ,wi_name:wi_name ,activityName:activityName ,param_json:param_json,cifId:cifID,acc_id:acc_id,row_count:row_count,parentWiName:parentWiName,header:'<%=headerOp.get("FINANCIAL_SUMMARY")%>',maincode:'<%=maincodeOp.get("FINANCIAL_SUMMARY")%>',footer:'<%=footerOp.get("FINANCIAL_SUMMARY")%>',parentTagName:'<%=parentTagNameOp.get("FINANCIAL_SUMMARY")%>', WD_UID : WD_UID}
									 integration_call_flag="Y";
									}
									else if(req_name_arr[i]=='InternalExposure'){
										//svt points
										data= { request_name: req_name_arr[i], is_mobility : "N", SeesionId:sessionId ,wi_name:wi_name ,activityName:activityName ,param_json:param_json,cifId:cifID,acc_id:acc_id,row_count:row_count,parentWiName:parentWiName,header:'<%=headerOp.get("InternalExposure")%>',maincode:'<%=maincodeOp.get("InternalExposure")%>',footer:'<%=footerOp.get("InternalExposure")%>',parentTagName:'<%=parentTagNameOp.get("InternalExposure")%>', WD_UID : WD_UID}
										 integration_call_flag="Y";
									}
									else if(req_name_arr[i]=='CollectionsSummary'){
										//svt points
										data= { request_name: req_name_arr[i], is_mobility : "N", SeesionId:sessionId ,wi_name:wi_name ,activityName:activityName ,param_json:param_json,cifId:cifID,acc_id:acc_id,row_count:row_count,parentWiName:parentWiName,header:'<%=headerOp.get("CollectionsSummary")%>',maincode:'<%=maincodeOp.get("CollectionsSummary")%>',footer:'<%=footerOp.get("CollectionsSummary")%>',parentTagName:'<%=parentTagNameOp.get("CollectionsSummary")%>', WD_UID : WD_UID}
										 integration_call_flag="Y";
									}
									else if(req_name_arr[i]=='ACCOUNT_SUMMARY'){
										//svt points
										data= { request_name: req_name_arr[i], is_mobility : "N", SeesionId:sessionId ,wi_name:wi_name ,activityName:activityName ,param_json:param_json,cifId:cifID,acc_id:acc_id,row_count:row_count,parentWiName:parentWiName,header:'<%=headerOp.get("ACCOUNT_SUMMARY")%>',maincode:'<%=maincodeOp.get("ACCOUNT_SUMMARY")%>',footer:'<%=footerOp.get("ACCOUNT_SUMMARY")%>',parentTagName:'<%=parentTagNameOp.get("ACCOUNT_SUMMARY")%>', WD_UID : WD_UID}
										 integration_call_flag="Y";
									}
									else if(req_name_arr[i]=='CARD_SUMMARY'){
										//svt points
										data= { request_name: req_name_arr[i], is_mobility : "N", SeesionId:sessionId ,wi_name:wi_name ,activityName:activityName ,param_json:param_json,cifId:cifID,acc_id:acc_id,row_count:row_count,parentWiName:parentWiName,header:'<%=headerOp.get("CARD_SUMMARY")%>',maincode:'<%=maincodeOp.get("CARD_SUMMARY")%>',footer:'<%=footerOp.get("CARD_SUMMARY")%>',parentTagName:'<%=parentTagNameOp.get("CARD_SUMMARY")%>', WD_UID : WD_UID}
										 integration_call_flag="Y";
									}
								//Changes done by aman for AECB End
								}
							}						
							
							if(integration_call_flag=="Y"){
								//REF-18092017 start
								if(req_name_arr[i]=='CARD_INSTALLMENT_DETAILS' && (trimStr(response)=='NO DATA' || trimStr(response)=='')){
									dynamicTable(req_name_arr[i],'',row_count);
									var eml_name='call_status_' +row_count+'';
									document.getElementById(eml_name).innerHTML="No LOC Card found for this Customer";
									check_complete();
									window.returnValue = "SUCCESS";
								}
								else{
								dynamicTable(req_name_arr[i], cifId_arr[j],row_count);
								final_integration(data,cifID,cust_type,req_name_arr[i]);
								/*
									$.ajax({
									type: "GET",
									url: "/webdesktop/custom/CustomJSP/Requestxml_1.jsp",
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
								});*/
								}
								//REF-18092017 End
						}
						row_count++;
					}
			}
			
			}
					return result;
		   
		}
		function final_integration(data,cifID,cust_type,req_name_arr){
			try{
				if(req_name_arr == 'ExternalExposure'){
					refreshExternalExposure();
				}
			}
			catch(ex){
				
			}
			
			$.ajax({
					type: "POST",
					url: "/webdesktop/custom/CustomJSP/Requestxml_PL.jsp",
					data: data,
					dataType: "text",
					async: true,
					success: function (response) {
						var rowcount=response.split('!@#');
						if(rowcount[0]=="Error"){
							var var_field_cout= trimStr(rowcount[1]);
							var eml_name='call_status_' + var_field_cout+'';
							document.getElementById(eml_name).innerHTML="Failure";
							if(document.getElementById("retry_btn_"+trimStr(var_field_cout))){
							document.getElementById("retry_btn_"+trimStr(var_field_cout)).style.visibility="visible";
							}
							check_complete();
						}
						else{
							set_result_val_withInterval(rowcount[0],rowcount[1],'',cifID,cust_type,req_name_arr);
						}
					},
					error: function (XMLHttpRequest, textStatus, errorThrown) {
						document.getElementById("retry_btn_"+req_name_arr).style.visibility="visible";		
						alert('Exception: '+ errorThrown);
					}
				});
		}  
		
		//Deepal new one to set data.
		function SetDataOnForm(response)
		{
			var jsondata = JSON.parse(param_json);
			var NTB=jsondata["cmplx_Customer_NTB"]; 
			var month =3.0;
			//var returnValues= new Map();//added by akshay on 10/8/18---not working
			if(response){
				
				xmlDoc = $.parseXML(response),
				$xml = $( xmlDoc ),
				$BandId = $xml.find("BankId");
				$SalTxnDetails = $xml.find("SalDetails");
				$AvgBalanceDtls=$xml.find("AvgBalanceDtls");
				$TxnSummary=$xml.find("TxnSummary");
								
				
				//window.opener.document.getElementById('cmplx_IncomeDetails_SalaryXferToBank').value =$SalTxnDetails.find('SalAmount').text();
				var salcredDate = new Date($SalTxnDetails.find('SalCreditDate').first().text().split('-').join('/'));//done for IE compatibility---getDate() doesnt work with -
				//change by saurabh for PPG changes on 20/4/19				
				var salaries = {};
				var arr = []; 
				try{
					if($SalTxnDetails){
						$xml.find("SalDetails").each(function(){
							var salmonth = $(this).find('SalCreditDate').text().substring(0,4)+$(this).find('SalCreditDate').text().substring(5,7);
							var sum=0;
							if(salaries[salmonth]!=undefined){
								sum=salaries[salmonth];
								}
							//salaries[] = $(this).find('SalCreditDate').text()
							$(this).find('SalAmount').each(function(){
								sum+= parseInt($(this).text());
							});
							salaries[salmonth] = sum;
							arr.push(salmonth);
						});
						arr.sort();
					}
				}catch(ex){
					
				}
				
				//$AvgCrTurnOver = $xml.find("AvgCrTurnOver");
				//var avgBalance=$AvgBalanceDtls.find('AvgBalance').first().text();
				try{
						if($AvgBalanceDtls.length>0) {
						var averageBal=window.opener.document.getElementById('cmplx_IncomeDetails_AvgBal').value;
						if(	averageBal==''){
							averageBal=0;
						}
						else{
							averageBal=parseFloat(averageBal);
						}					
							$AvgBalanceDtls.find('AvgBalance').each(function() {
							//averageBal+=(parseFloat($(this).text())/month);
							var amount=parseFloat($(this).text());
							averageBal+=(amount<0?0:amount)/month;
						});				
						//var finavg=(average/$AvgBalanceDtls.length);
						
						window.opener.setNGValueCustom('cmplx_IncomeDetails_AvgBal',averageBal.toFixed(2)) ;
						//returnValues.set("AvgBalanceDtls",averageBal.toFixed(2));
					}
					
					else if($TxnSummary.length>0) {
					
						var AvgCrTurnOver=window.opener.document.getElementById('cmplx_IncomeDetails_AvgCredTurnover').value;
						var TotalCrAmt=window.opener.document.getElementById('cmplx_IncomeDetails_CredTurnover').value;
						if(	AvgCrTurnOver==''){
							AvgCrTurnOver=0;
						}
						else{
							AvgCrTurnOver=parseFloat(AvgCrTurnOver);
						}	
						if(TotalCrAmt==''){
							TotalCrAmt=0;
						}
						else{
							TotalCrAmt=parseFloat(TotalCrAmt);
						}						
							$TxnSummary.find('AvgCrTurnOver').each(function() {
							AvgCrTurnOver+=parseFloat($(this).text());
						});				
						$TxnSummaryDtls=$TxnSummary.find("TxnSummaryDtls");
						if($TxnSummaryDtls.length>0){
						$TxnSummaryDtls.find('TotalCrAmt').each(function() {
							TotalCrAmt+=parseFloat($(this).text());
						});	

						}			
						
						//returnValues.set("TotalCrAmt",TotalCrAmt.toFixed(2));
						if(AvgCrTurnOver!=undefined){
							window.opener.setNGValueCustom('cmplx_IncomeDetails_AvgCredTurnover',AvgCrTurnOver.toFixed(2));
							
						}
						
						if(TotalCrAmt!=undefined){
							window.opener.setNGValueCustom('cmplx_IncomeDetails_CredTurnover',TotalCrAmt.toFixed(2));
							//returnValues.set("AvgCrTurnOver",AvgCrTurnOver.toFixed(2));
						}
						
					}
					
					if(window.opener.getNGValue('EmploymentType').indexOf('Salaried')>-1){
						if($SalTxnDetails.length>0){
							if(salcredDate!='undefined'){
								window.opener.setNGValueCustom('cmplx_IncomeDetails_SalaryDay',salcredDate.getDate());
							}	
							//change by saurabh for PPG changes on 20/4/19
							var Last3arr= getLastMonths(3,salaries);
							if(salaries[Last3arr[0]]!=undefined){
								window.opener.setNGValueCustom('cmplx_IncomeDetails_netSal1',salaries[Last3arr[0]]);
							}
							else{
								window.opener.setNGValueCustom('cmplx_IncomeDetails_netSal1',0);
							}
							if(salaries[Last3arr[1]]!=undefined){
								window.opener.setNGValueCustom('cmplx_IncomeDetails_netSal2',salaries[Last3arr[1]]);
							}else{
								window.opener.setNGValueCustom('cmplx_IncomeDetails_netSal2',0);
							}
							if(salaries[Last3arr[2]]!=undefined){
								window.opener.setNGValueCustom('cmplx_IncomeDetails_netSal3',salaries[Last3arr[2]]);	
							}else{
								window.opener.setNGValueCustom('cmplx_IncomeDetails_netSal3',0);
							}
							window.opener.setNGValueCustom("cmplx_IncomeDetails_AvgNetSal",window.opener.calcAvgNet());
							window.opener.putComma('cmplx_IncomeDetails_AvgNetSal');
						}
					}
				}
				catch(e){
					check_complete();
				}
				
			}
			//return returnValues;
		}
		//Deepak method add to identify last 3 month
		function getLastMonths(n,salaries) {
		    var months = new Array();
		    var today = new Date();
		    var year = today.getFullYear();
		    if(salaries[year + (month > 9 ? "" : "0") + month]!=undefined){
		    	var month = today.getMonth() + 1;
		    }
		    else{
		    	var month = today.getMonth();	
		    }
		    
		    var i = 0;
		    do {
		        months.push(year + (month > 9 ? "" : "0") + month);
		        if(month == 1) {
		            month = 12;
		            year--;
		        } else {
		            month--;
		        }
		        i++;
		    } while(i < n);
		    return months;
		}
		/*
		function SetDataOnForm(response)
		{
			var jsondata = JSON.parse(param_json);
			var NTB=jsondata["cmplx_Customer_NTB"]; 
			
			var returnValues= new Map();//added by akshay on 10/8/18---not working
			if(response.indexOf('MQ_RESPONSE_XML')>-1){
				response = response.substring(response.indexOf("<MQ_RESPONSE_XML>")+17,response.indexOf("</MQ_RESPONSE_XML>"));
				
				xmlDoc = $.parseXML(response),
				$xml = $( xmlDoc ),
				$BandId = $xml.find("BankId");
				$SalTxnDetails = $xml.find("SalDetails");
				$AvgBalanceDtls=$xml.find("AvgBalanceDtls");
				$TxnSummary=$xml.find("TxnSummary");
				//changes by saurabh on 21/12.
				window.opener.document.getElementById('cmplx_IncomeDetails_SalaryXferToBank').value =new Date($SalTxnDetails.find('SalCreditDate').first().text().split('-').join('/'));
				//var salcredDate = new Date($SalTxnDetails.find('SalCreditDate').first().text().split('-').join('/'));
				
				
				//$AvgCrTurnOver = $xml.find("AvgCrTurnOver");
				//var avgBalance=$AvgBalanceDtls.find('AvgBalance').first().text();
				try{
						if($AvgBalanceDtls.length>0) {
						var averageBal='';//window.opener.document.getElementById('cmplx_IncomeDetails_AvgBal').value;
						if(	averageBal==''){
							averageBal=0;
						}
						else{
							averageBal=parseFloat(averageBal);
						}					
							$AvgBalanceDtls.find('AvgBalance').each(function() {
							averageBal+=parseFloat($(this).text());
						});				
						//var finavg=(average/$AvgBalanceDtls.length);
						
						window.opener.document.getElementById('cmplx_IncomeDetails_AvgBal').value =averageBal.toFixed(2);
						//returnValues.set("AvgBalanceDtls",averageBal.toFixed(2));
					}
					
					else if($TxnSummary.length>0) {
					
						var AvgCrTurnOver='';//window.opener.document.getElementById('cmplx_IncomeDetails_AvgCredTurnover').value;
						var TotalCrAmt='';//window.opener.document.getElementById('cmplx_IncomeDetails_CredTurnover').value;
						if(	AvgCrTurnOver==''){
							AvgCrTurnOver=0;
						}
						else{
							AvgCrTurnOver=parseFloat(AvgCrTurnOver);
						}	
						if(TotalCrAmt==''){
							TotalCrAmt=0;
						}
						else{
							TotalCrAmt=parseFloat(TotalCrAmt);
						}						
							$TxnSummary.find('AvgCrTurnOver').each(function() {
							AvgCrTurnOver+=parseFloat($(this).text());
						});				
						$TxnSummaryDtls=$TxnSummary.find("TxnSummaryDtls");
						if($TxnSummaryDtls.length>0){
						$TxnSummaryDtls.find('TotalCrAmt').each(function() {
							TotalCrAmt+=parseFloat($(this).text());
						});	

						}					
						//window.opener.document.getElementById('cmplx_IncomeDetails_AvgCredTurnover').value =AvgCrTurnOver.toFixed(2);
						//window.opener.document.getElementById('cmplx_IncomeDetails_CredTurnover').value =TotalCrAmt.toFixed(2);
						returnValues.set("AvgCrTurnOver",AvgCrTurnOver.toFixed(2));
						returnValues.set("TotalCrAmt",TotalCrAmt.toFixed(2));
					}
					
					/*if((window.opener.document.getElementById('Product').value.indexOf('Personal')>-1)||(window.opener.document.getElementById('Product').value.indexOf('Credit')>-1 && window.opener.document.getElementById('Credit Card').value.indexOf('Salaried')>-1)){
						if($SalTxnDetails.length>0){
							window.opener.document.getElementById('cmplx_IncomeDetails_SalaryDay').value = salcredDate.getDate();	
						}
					}*/
			/*	}
				catch(e){
					check_complete();
				}
				
				
			}
			return returnValues;
		}
		*/
		
		function check_complete(){
			
			var jsondata = JSON.parse(param_json);
			var NTB=jsondata["cmplx_Customer_NTB"]; 
			var Company=jsondata["cif"]; 
			if(document.getElementById("dyn_tbl_row")!=undefined){
					var table = document.getElementById("dyn_tbl_row");
					var tbl_rowcount = table.rows.length;
					var return_flag ='';
					for(var scount=0; scount<=tbl_rowcount-1;scount++){
						if(table.rows[scount].childNodes[1].innerText=='In Process'){
							return_flag = "Incomplete";
							break;
						}
					}
					//alert("return_flag:"+return_flag);
					if(return_flag!='Incomplete' && (req_name.indexOf('CARD_INSTALLMENT_DETAILS')>-1 || (req_name.indexOf('ExternalExposure')>-1) && NTB=='true' && (Company=='' || Company==null))){
						document.getElementById("continue_btn").style.visibility="hidden";
						document.getElementById("Close_btn").style.visibility="visible";
						}
						else if(return_flag!='Incomplete' && req_name!=""){
							document.getElementById("continue_btn").style.visibility="visible";
							
						}
						else if(return_flag!='Incomplete' && req_name==""){
							document.getElementById("continue_btn").style.visibility="hidden";
							document.getElementById("Close_btn").style.visibility="visible";
						}
					
			}
		}
		function continue_next(){
			var jsondata = JSON.parse(param_json);
			var NTB=jsondata["cmplx_Customer_NTB"]; 
			var Company=jsondata["cif"];
			var table = document.getElementById("dyn_tbl_row");
			var tbl_rowcount = table.rows.length;
			for(var scount=0; scount<=tbl_rowcount-1;scount++){
				table.deleteRow(0);
			}
			if(req_name.indexOf('ExternalExposure')>-1 && (NTB=='false' || (Company!='' && Company!=null))){
				req_name="CollectionsSummary";
			}
			else if(req_name.indexOf('ExternalExposure')==-1 && req_name.indexOf('InternalExposure')>-1)
			{
				req_name="CollectionsSummary";
			}
			else if(req_name.indexOf('CollectionsSummary')>-1 && (NTB=='false' || (Company!='' && Company!=null))){
				req_name="CARD_INSTALLMENT_DETAILS";
			}
			else{
				document.getElementById("Close_btn").style.visibility="visible";
			}
			document.getElementById("continue_btn").style.visibility="hidden";
			loadform();
		}
		
		
		function final_close(){
			var EmploymentType = window.opener.getLVWAT('cmplx_Product_cmplx_ProductGrid',0,6);
			if(EmploymentType!=null && EmploymentType=="Self Employed"){
				calculateLiabilityValues();
			}	
			if(req_name=='CARD_INSTALLMENT_DETAILS'){
				refreshWriteOff();
			}
			
			try{
				UpdateLOSApprovedLoan();
			}
			catch(e){
				
			}
			//Deepak upcommneted to reload Internal, External and pipeline jsp on exposure call completion. 08July2019
			if(req_name.indexOf('InternalExposure')>-1 || req_name.indexOf('CollectionsSummary')>-1 ||req_name.indexOf('ExternalExposure')>-1||req_name.indexOf("CARD_INSTALLMENT_DETAILS")>-1){
				window.opener.document.getElementById('ExtLiability_IFrame_internal').src='/webdesktop/custom/CustomJSP/internal_liability.jsp';
				window.opener.document.getElementById('ExtLiability_IFrame_external').src='/webdesktop/custom/CustomJSP/External_Liability.jsp';
				window.opener.document.getElementById('ExtLiability_IFrame_pipeline').src='/webdesktop/custom/CustomJSP/Pipeline.jsp';
				//window.opener.document.getElementById('Liability_New_Overwrite').style.visibility="visible";
				}
				if(req_name.indexOf('AVGBALDET')>-1 || req_name.indexOf('LIENDET')>-1 ||req_name.indexOf('RETURNDET')>-1||req_name.indexOf("SALDET")>-1 || req_name.indexOf('SIDET')>-1||req_name.indexOf("TRANSUM")>-1 || req_name==''){
					if (wi_name.indexOf('PL')<0 ){
						window.opener.document.getElementById("IncomeDetails_RefreshFinacle").click();
					}
				}





			window.close();
		}
		 function refreshWriteOff()
		   {				
				var ajaxReq;
				var dataFromAjax;				
				if (window.XMLHttpRequest) 
				{
					ajaxReq= new XMLHttpRequest();
				}
				else if (window.ActiveXObject)
				{
					ajaxReq= new ActiveXObject("Microsoft.XMLHTTP");
				}		
								
				var url = "/webdesktop/custom/CustomJSP/exec_procdure.jsp";

				ajaxReq.open("POST", url, false);
				ajaxReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
				ajaxReq.send("wi_name="+wi_name+"&ProcName=ng_RLOS_CheckWriteOff");
				if (ajaxReq.status == 200 && ajaxReq.readyState == 4)
				{
					dataFromAjax=ajaxReq.responseText;
					//alert("result copy data "+ dataFromAjax);					
				}
				else
				{
					alert("INVALID_REQUEST_ERROR : "+ajaxReq.status);
					dataFromAjax="";
				}	   
		   }
		function refreshExternalExposure()
		   {				
				var ajaxReq;
				var dataFromAjax;				
				if (window.XMLHttpRequest) 
				{
					ajaxReq= new XMLHttpRequest();
				}
				else if (window.ActiveXObject)
				{
					ajaxReq= new ActiveXObject("Microsoft.XMLHTTP");
				}		
								
				var url = "/webdesktop/custom/CustomJSP/exec_procdure.jsp";

				ajaxReq.open("POST", url, false);
				ajaxReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
				ajaxReq.send("wi_name="+wi_name+"&ProcName=Delete_operationforexistingdata");
				if (ajaxReq.status == 200 && ajaxReq.readyState == 4)
				{
					dataFromAjax=ajaxReq.responseText;
					//alert("result copy data "+ dataFromAjax);					
				}
				else
				{
					alert("INVALID_REQUEST_ERROR : "+ajaxReq.status);
					dataFromAjax="";
				}	   
		   }
		 function UpdateLOSApprovedLoan()
		   {				
				var ajaxReq;
				var dataFromAjax;				
				if (window.XMLHttpRequest) 
				{
					ajaxReq= new XMLHttpRequest();
				}
				else if (window.ActiveXObject)
				{
					ajaxReq= new ActiveXObject("Microsoft.XMLHTTP");
				}		
								
				var url = "/webdesktop/custom/CustomJSP/exec_procdure.jsp";

				ajaxReq.open("POST", url, false);
				ajaxReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
				ajaxReq.send("wi_name="+wi_name+"&ProcName=NG_Rlos_LOS_ApprovedLoan_insert");
				if (ajaxReq.status == 200 && ajaxReq.readyState == 4)
				{
					dataFromAjax=ajaxReq.responseText;
					//alert("result copy data "+ dataFromAjax);					
				}
				else
				{
					alert("INVALID_REQUEST_ERROR : "+ajaxReq.status);
					dataFromAjax="";
				}	   
		   }
		</script>	
			
		<title>Integration in process</title>
	</head>
	<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0"  onload ="loadform();" class="dark-matter">
		<form name="integration_new" id="integration_new" method="post" >
		<div id="mainDivContent">
					<div style='overflow:scroll;height:500px'>
					<p style='color:#e62e00'>Please do not Close the window as transaction is in process.Use close button once the transaction completed.</p>
						<input type='button'  id='continue_btn' value='continue' style='visibility:hidden;background-color:#0c658e; color:#ffffff' onclick='continue_next()'/>
						<input type='button'  id='Close_btn' value='Close' style='visibility:hidden;background-color:#0c658e; color:#ffffff' onclick='final_close();'/>
					<table id='mytab' border='1' bordercolor='#337ab7' width='600px' max-height='400px'>
						<tr>
								<th width ='320px' >Call Name</th><th width ='200px'>Call Status</th><th width ='80px'>Re-try </th>
						</tr>
					</table>
					
						<table id='dyn_tbl_row' border='1' bordercolor='#337ab7' width='600px' max-height='500px'>
					</table>
				</div>
		</div>			
			
		</form>
	</body>
	
</html>