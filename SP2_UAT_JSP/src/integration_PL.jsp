
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

<%
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader ("Expires", 0);
	String request_name = request.getParameter("request_name");
	//WriteLog("Integration jsp: request_name: "+request_name);
	String sSessionId = request.getParameter("sessionId");
	
	String wi_name = request.getParameter("wi_name");
	String activityName = request.getParameter("activityName");
	String param_json = request.getParameter("param_json");
	String parentWiName = request.getParameter("parentWiName");
	
	String prod =null;
	String subprod =null;
	if(request_name.equalsIgnoreCase("InternalExposure") ||request_name.equalsIgnoreCase("ExternalExposure") ||request_name.equalsIgnoreCase("CollectionsSummary")||request_name.equalsIgnoreCase("CARD_INSTALLMENT_DETAILS"))
	{	
		prod = request.getParameter("prod");
		subprod = request.getParameter("subprod");
	}
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
		var req_name = '<%=request_name%>';
		var sessionId = '<%=sSessionId%>';
		var wi_name = '<%=wi_name%>';
		var activityName = '<%=activityName%>';
		var param_json = '<%=param_json%>';
		var prod = '<%=prod%>';
		var subprod = '<%=subprod%>';
		var parentWiName = '<%=parentWiName%>';
		
		var row_count = 1;		
		var aecb_rep_url;
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
			cell1.width = "320px";
			cell2.width = "200px";
			cell3.width = "80px";
			
			if(req_name_arr!=''){
			cell1.innerHTML = req_name_arr+" for "+cifID;
			cell3.innerHTML = "<input type ='button' name = 'retry_btn_"+row_count+"' id = 'retry_btn_"+row_count+"' value = 'Re-try' style='visibility:hidden;' />";
			document.getElementById('retry_btn_'+row_count).onclick = function(){integ(req_name_arr,cifID);}
			}
			else{
			cell1.innerHTML = cifID;
			}
			//condition added by akshay on 31/1/18
			if(cifID==''){
				cell1.innerHTML = req_name_arr;
			}	
			else{
				cell1.innerHTML = req_name_arr+" for "+cifID;
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
			var time_out=2000;
			if(req_name.indexOf('Exposure')>-1)
			{
				time_out=6000;
			}
			var time_intr = setInterval(function(){
				if(document.getElementById(eml_name).innerHTML=="Failure" || document.getElementById(eml_name).innerHTML=="Sucessfull"){
					check_complete();
					clearInterval(time_intr);
				}
				else if(loop_count<10 && document.getElementById(eml_name).innerHTML=='In Process'){
					set_result_val(result,row_count,CardNumber,cifId,cust_type);
				}
				else if(loop_count>9 && document.getElementById(eml_name).innerHTML=='In Process'){
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
							data={ result: result, wi_name : wi_name, prod : prod, subprod : subprod,is_mobility : "N", SeesionId:sessionId,row_count:row_count,cifId:cifId,CardNumber:CardNumber,parentWiName:parentWiName,cust_type:cust_type}
							}
							else{
							data= { result: result, wi_name : wi_name, prod : prod, subprod : subprod,is_mobility : "N", SeesionId:sessionId,row_count:row_count,cifId:cifId,parentWiName:parentWiName,cust_type:cust_type} 
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
							SetDataOnForm(result);
							window.returnValue ='SUCCESS';
							//added by nikhil for SP 2.1
							Set_Score_Range();
						   window.opener.setNGValueCustom('aecb_call_status','SUCCESS');
							
						}
						else if(flag_value[0]=="waiting"){
							return 'waiting';
							//alert('waiting from rep');
						}
						else if(flag_value[0]=="Failure" || flag_value[0]=="false")
						{
							document.getElementById(eml_name).innerHTML="Failure";
							if(document.getElementById("retry_btn_"+trimStr(flag_value[1]))){
							document.getElementById("retry_btn_"+trimStr(flag_value[1])).style.visibility="visible";
							}							
							window.returnValue = "FAIL";	
						}
						else{
							document.getElementById(eml_name).innerHTML=flag_value[0];
							if(flag_value[0]==""||flag_value[0]=="null"){
								document.getElementById(eml_name).innerHTML="Failure";
							}
							Set_Score_Range();
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
		function Set_Score_Range()
		{
			var wparams="Child_wi=="+window.opener.window.parent.pid;
			var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
			var query='Score_Range';
			var params="Query="+query+"&wparams="+wparams+"&pname="+'CCTemplateData';
			var integ_Query=CallAjax(jspName,params);
			var fields = integ_Query.split('#');
			var ReferenceNumber = fields[0]=='undefined' || fields[0]=='NODATAFOUND'?'':fields[0];
			var Aecb_score = fields[1]=='undefined' || fields[1]=='NODATAFOUND'?'':fields[1];
			var Range = fields[2]=='undefined' || fields[2]=='NODATAFOUND'?'':fields[2];
			window.opener.setNGValueCustom('cmplx_Liability_New_AECBScore',Aecb_score);
			window.opener.setNGValueCustom('cmplx_Liability_New_Range',Range);
			window.opener.setNGValueCustom('cmplx_Liability_New_ReferenceNo',ReferenceNumber);
			
		}
		var GlobalFlag = false;
		function integration_all (){
				var result = null;
				var finalxmlResponse = null;
				
				$.ajax({
					type: "POST",
					url: "/webdesktop/custom/CustomJSP/MultipleCifId.jsp",
					data: { wi_name:parentWiName , req_name : req_name , activity : activityName , child_wi: wi_name,sessionId:sessionId} ,
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

				ajaxReq.open("POST", url, false);
				ajaxReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
				ajaxReq.send("sub_product="+sub_product+"&emp_type="+emp_type+"&acc_type="+acc_type+"&activityName="+activityName+"&SeesionId="+sessionId+"&application_type="+application_type);
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

				ajaxReq.open("POST", url, false);
				ajaxReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
				ajaxReq.send("wi_name="+parentWiName+"&SeesionId="+sessionId+"&child_wi="+wi_name+"&activityName="+activityName);
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
					data: { wi_name:parentWiName , SeesionId:sessionId} ,
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
			var linkedCifs = null;
			response = trimStr(response);
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
				if(response!=''){
					response = response + ',' + companyCif;			  
				}
				else{
					response = companyCif;			  
				}
				linkedCifs = trimStr(fetchLinkedCif());
					if(linkedCifs !='' && linkedCifs !='null')
					{
						response = response + ',' + linkedCifs;
					}
			}

			// above changes done by disha on 27-02-2018 for Collection summary should be run for the linked cif as well
			
			else if(TLNo!='' && TLNo!=null && companyCif=='')
			{
				if(response!=''){
					response = response+','+'Corporate';
				}
				else{
					response = 'Corporate';			  
				}
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
				//changes condition added by Deepak 22May2019 as below need to be set for external exposure only.
				if(req_name == 'ExternalExposure'){
					//changes done by Deepak to make provider no unique start
					jsondata["wi_name"] = provider_no+Math.floor((Math.random()*90)+10);
					//Changes done by Aman to save AECB provide no.
					window.opener.com.newgen.omniforms.formviewer.setNGValue('cmplx_Liability_New_AECB_Provider_Number',jsondata["wi_name"]);
					param_json = JSON.stringify(jsondata);
					//changes done by Deepak to make provider no unique end
				}
				
				
				if(cifId_arr[j].indexOf(":")>-1){
					cif_acc_Arr = cifId_arr[j].split(":");
					cifID = trimStr(cif_acc_Arr[0]);
					acc_id = cif_acc_Arr[1];
					// below code done to find opertaion names of financial summary on 29th Dec by disha
					acc_type = cif_acc_Arr[2];
					req_name = trimStr(fetchOperation_Name(sub_product,emp_type,acc_type,application_type));					
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
									data= { request_name: req_name_arr[i], is_mobility : "N", SeesionId:sessionId ,wi_name:wi_name ,activityName:activityName ,param_json:param_json,cifId:cifID,acc_id:acc_id,row_count:row_count,Customer_Type:cust_type,parentWiName:parentWiName,header:'<%=headerOp.get("ExternalExposure")%>',maincode:'<%=maincodeOp.get("ExternalExposure")%>',footer:'<%=footerOp.get("ExternalExposure")%>',parentTagName:'<%=parentTagNameOp.get("ExternalExposure")%>'}
									integration_call_flag="Y";
								 }
								 //added for External call of Company when cif is not available
								 else if(req_name_arr[i] != 'ExternalExposure' && cifID=='Corporate'){
								 	integration_call_flag="N";
								 }
								 //added for External call of Company when cif is not available
								//below one condition Added by deepak to trigger only 1 exposure call when multiple CIF's are identified. PCAS-1777
								else if(req_name_arr[i] == 'ExternalExposure' && primary_Cif!=cifID ){
									integration_call_flag="N";
								 }
								 else if(req_name_arr[i] == 'InternalExposure'  && (cifID=="null" || cifID=="")){
									integration_call_flag="N";
								 }
								 else if(req_name_arr[i] != 'ExternalExposure'){
								 if(req_name_arr[i]=='TRANSUM' || req_name_arr[i]=='AVGBALDET' || req_name_arr[i]=='RETURNDET' || req_name_arr[i]=='LIENDET' || req_name_arr[i]=='SIDET' || req_name_arr[i]=='SALDET' || req_name_arr[i]=='Primary_CIF' || req_name_arr[i]=='Corporation_CIF'){
									 data= { request_name: req_name_arr[i], is_mobility : "N", SeesionId:sessionId ,wi_name:wi_name ,activityName:activityName ,param_json:param_json,cifId:cifID,acc_id:acc_id,row_count:row_count,parentWiName:parentWiName,header:'<%=headerOp.get("FINANCIAL_SUMMARY")%>',maincode:'<%=maincodeOp.get("FINANCIAL_SUMMARY")%>',footer:'<%=footerOp.get("FINANCIAL_SUMMARY")%>',parentTagName:'<%=parentTagNameOp.get("FINANCIAL_SUMMARY")%>'}
									 integration_call_flag="Y";
									}
									else if(req_name_arr[i]=='InternalExposure'){
										data= { request_name: req_name_arr[i], is_mobility : "N", SeesionId:sessionId ,wi_name:wi_name ,activityName:activityName ,param_json:param_json,cifId:cifID,acc_id:acc_id,row_count:row_count,parentWiName:parentWiName,header:'<%=headerOp.get("InternalExposure")%>',maincode:'<%=maincodeOp.get("InternalExposure")%>',footer:'<%=footerOp.get("InternalExposure")%>',parentTagName:'<%=parentTagNameOp.get("InternalExposure")%>'}
										 integration_call_flag="Y";
									}
									else if(req_name_arr[i]=='CollectionsSummary'){
										data= { request_name: req_name_arr[i], is_mobility : "N", SeesionId:sessionId ,wi_name:wi_name ,activityName:activityName ,param_json:param_json,cifId:cifID,acc_id:acc_id,row_count:row_count,parentWiName:parentWiName,header:'<%=headerOp.get("CollectionsSummary")%>',maincode:'<%=maincodeOp.get("CollectionsSummary")%>',footer:'<%=footerOp.get("CollectionsSummary")%>',parentTagName:'<%=parentTagNameOp.get("CollectionsSummary")%>'}
										 integration_call_flag="Y";
									}
									else if(req_name_arr[i]=='ACCOUNT_SUMMARY'){
										data= { request_name: req_name_arr[i], is_mobility : "N", SeesionId:sessionId ,wi_name:wi_name ,activityName:activityName ,param_json:param_json,cifId:cifID,acc_id:acc_id,row_count:row_count,parentWiName:parentWiName,header:'<%=headerOp.get("ACCOUNT_SUMMARY")%>',maincode:'<%=maincodeOp.get("ACCOUNT_SUMMARY")%>',footer:'<%=footerOp.get("ACCOUNT_SUMMARY")%>',parentTagName:'<%=parentTagNameOp.get("ACCOUNT_SUMMARY")%>'}
										 integration_call_flag="Y";
									}
									else if(req_name_arr[i]=='CARD_SUMMARY'){
										data= { request_name: req_name_arr[i], is_mobility : "N", SeesionId:sessionId ,wi_name:wi_name ,activityName:activityName ,param_json:param_json,cifId:cifID,acc_id:acc_id,row_count:row_count,parentWiName:parentWiName,header:'<%=headerOp.get("CARD_SUMMARY")%>',maincode:'<%=maincodeOp.get("CARD_SUMMARY")%>',footer:'<%=footerOp.get("CARD_SUMMARY")%>',parentTagName:'<%=parentTagNameOp.get("CARD_SUMMARY")%>'}
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
				
		function check_complete(){
			
			var jsondata = JSON.parse(param_json);
			var NTB=jsondata["cmplx_Customer_NTB"]; 
			
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
					if(return_flag!='Incomplete' && (req_name.indexOf('CARD_INSTALLMENT_DETAILS')>-1 || (req_name.indexOf('ExternalExposure')>-1) && NTB=='true')){
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
			
			var table = document.getElementById("dyn_tbl_row");
			var tbl_rowcount = table.rows.length;
			for(var scount=0; scount<=tbl_rowcount-1;scount++){
				table.deleteRow(0);
			}
			if(req_name.indexOf('InternalExposure')>-1 && NTB=='false'){
				req_name="CollectionsSummary";
			}
			else if(req_name.indexOf('CollectionsSummary')>-1 && NTB=='false'){
				req_name="CARD_INSTALLMENT_DETAILS";
			}
			else{
				document.getElementById("Close_btn").style.visibility="visible";
			}
			document.getElementById("continue_btn").style.visibility="hidden";
			loadform();
		}
		
		
		function final_close(){
			if(req_name=='CARD_INSTALLMENT_DETAILS'){
				refreshWriteOff();
			}
			//Deepak upcommneted to reload Internal, External and pipeline jsp on exposure call completion. 08July2019
			if(req_name.indexOf('InternalExposure')>-1 || req_name.indexOf('CollectionsSummary')>-1 ||req_name.indexOf('ExternalExposure')>-1||req_name.indexOf("CARD_INSTALLMENT_DETAILS")>-1){
				window.opener.document.getElementById('ExtLiability_IFrame_internal').src='/webdesktop/custom/CustomJSP/internal_liability.jsp';
				window.opener.document.getElementById('ExtLiability_IFrame_external').src='/webdesktop/custom/CustomJSP/External_Liability.jsp';
				window.opener.document.getElementById('ExtLiability_IFrame_pipeline').src='/webdesktop/custom/CustomJSP/Pipeline.jsp';
				//window.opener.document.getElementById('Liability_New_Overwrite').style.visibility="visible";
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