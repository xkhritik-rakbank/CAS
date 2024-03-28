<!------------------------------------------------------------------------------------------------------
//           NEWGEN SOFTWARE TECHNOLOGIES LIMITED

//Group						 : Application ?Projects
//Product / Project			 : RAKBank-RLOS
//File Name					 : Pipeline
//Author                     : Deepak Kumar	
//DESC						 : To generate and save Editable Grid JSP
//Date written (DD/MM/YYYY)  : 2/02/2017
//---------------------------------------------------------------------------------------------------->

<%@ include file="Log.process"%>
<%@ page import="java.io.*,java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.lang.String.*"%>
<%@ page import="java.lang.Object"%>
<%@ page import="com.newgen.wfdesktop.xmlapi.*" %>
<%@ page import="com.newgen.wfdesktop.util.*" %>
<%@ page import="com.newgen.mvcbeans.model.*,javax.faces.context.FacesContext,com.newgen.mvcbeans.controller.workdesk.*"%>
<jsp:useBean id="wDSession" class="com.newgen.wfdesktop.session.WDSession" scope="session"/>

 

<%
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader ("Expires", 0);
    String sSessionId = wDSession.getM_objUserInfo().getM_strSessionId();
    String sCabname = wDSession.getM_objCabinetInfo().getM_strCabinetName();
    String sJtsIp = wDSession.getM_objCabinetInfo().getM_strServerIP();
    String iJtsPort = wDSession.getM_objCabinetInfo().getM_strServerPort();
    String sUserName = wDSession.getM_objUserInfo().getM_strUserName();
	String UserId= wDSession.getM_objUserInfo().getM_strUserIndex()+"";
	//String WiName=request.getParameter("winame");
	
	String ChecklistAbbr=request.getParameter("ChecklistAbbr");
    if (sSessionId == null) {
        out.println("Invalid Session. Please login again.");
    }
		String User_Index = "";
		String sOutputXML="";
	
%>
<!--   <script src="js/jquery-1.9.1.min.js"></script>
    <script src="js/bootstrap-datepicker.js"></script>    -->
<!--     <script type="text/javascript">  </script>   -->

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
//select distinct(groupindex) from PDBGroupMember where UserIndex=1 and GroupIndex in (1,10)
var Cab_Name = '<%=sCabname%>';
	var Session_ID = '<%=sSessionId%>';
	var User_Name = '<%=sUserName%>';
	var User_Index = '<%=UserId%>';
	var sJtsIp = '<%=sJtsIp%>';
	var iJtsPort = '<%=iJtsPort%>';
	var winame= window.parent.workitemName;
	var activityName = winame.substring(0,winame.indexOf("-"));
	var ChecklistAbbr='<%=ChecklistAbbr%>';
	var enabledSteps = ['Branch_Init','CSM','Telesales_Init','Fulfillment_RM','Telesales_RM','CAD_Analyst1'];

	var noOfBorrower=0;
	var noOfGuarantor=0;
	var WiDetails="";
	var ChecklistDetails="";
	var PrevResCol1="";
	var pResStr="";
	var rResStr="";	
	var ResCol1="";
	var InputField_id="";
	var table_name="ng_RLOS_CUSTEXPOSE_LoanDetails";
	var row_id_arr=[];
	var ws_name = window.parent.activityName;
	var data_save_var="";
	var enabled_Worksteps=['Branch_Init','CSM','Telesales_Init','Fulfillment_RM','Telesales_RM','CAD_Analyst1'];
	var pname="PL";
	if(activityName=='CAS'){
		activityName='CA';
	}
	else if(activityName=='CDOB'){
		activityName='CC';
	}
function addClick(obj) {
		var nextrowid= $('table#mytab tr:last').index();
		if (row_id_arr.length < 1){
			row_id_arr.push(nextrowid);
		}
		else {
			nextrowid = parseInt(row_id_arr[row_id_arr.length-1]) + 1;
			row_id_arr.push(nextrowid);
		}
		var markup = tb_row(nextrowid,"");
            $("table#mytab tbody").append(markup);
    }
	
function saveDate() {
    	var totalrowid= $('table#mytab tr:last').index();
		var final_data = "";
		var new_row = "Y";
		var row_arr = WiDetails.split("~");		
		var stable="";
		var scolumnname = "";
		var scolumnsvalues = "";
		var swhere="";
		var update_result="";
		var data_save_var_db_name="";
		var agreementId_val="";
		for(var row_arr_count= 1;row_arr_count<=totalrowid;row_arr_count++){
				var value = "";
					var table_name = "datatablename_"+row_arr_count;
					var agreementId  = "Provider_No_"+row_arr_count;
					var custrole = document.getElementById("Role_"+row_arr_count).value;
					stable = document.getElementById(table_name).value;
					agreementId_val = document.getElementById(agreementId).value;
					if(agreementId_val.indexOf('-')>-1){
						agreementId_val=agreementId_val.split('-')[0];
					}
					/*if((activityName=="CC" || activityName=="PL" || activityName=="Cr")&&(stable=="ng_rlos_cust_extexpo_LoanDetails" || stable=="ng_rlos_cust_extexpo_CardDetails")){
					var agreementId_val = document.getElementById(agreementId).innerHTML;
					}
					else{
					var agreementId_val = document.getElementById(agreementId).value;
					}*/
					
					if(stable=="ng_rlos_cust_extexpo_LoanDetails"){
						if(activityName=="CC" || activityName=="PL" || activityName=="Cr"){
						data_save_var_db_name="Consider_For_Obligations,Take_Over_Indicator,Take_Amount";
						data_save_var="Consider_For_Obligations,Takeover_Indicator,Takeover_Amount";
						swhere = "Child_Wi = '"+winame+"' and AgreementId = '"+agreementId_val+"'";
						}
						else{
						data_save_var_db_name="Consider_For_Obligations,Take_Over_Indicator,Take_Amount";
						data_save_var="Consider_For_Obligations,Takeover_Indicator,Takeover_Amount";
						swhere = "Wi_Name = '"+winame+"' and AgreementId = '"+agreementId_val+"'";
						}
					}
					else if(stable=="ng_rlos_cust_extexpo_CardDetails"){
						if(activityName=="CC" || activityName=="PL" || activityName=="Cr"){
						data_save_var_db_name="Consider_For_Obligations,Take_Over_Indicator,TakeOverAmount";
						data_save_var="Consider_For_Obligations,Takeover_Indicator,Takeover_Amount";
						swhere = "Child_Wi = '"+winame+"' and CardEmbossNum = '"+agreementId_val+"'";
						}
						else{
						data_save_var_db_name="Consider_For_Obligations,Take_Over_Indicator,TakeOverAmount";
						data_save_var="Consider_For_Obligations,Takeover_Indicator,Takeover_Amount";
						swhere = "Wi_Name = '"+winame+"' and CardEmbossNum = '"+agreementId_val+"'";
						}
					}
					else if(stable=="ng_RLOS_CUSTEXPOSE_LoanDetails"){
						if(activityName=="CC" || activityName=="PL" || activityName=="Cr"){
						data_save_var_db_name="Consider_For_Obligations";
						data_save_var="Consider_For_Obligations";
							if(custrole=='Primary'){
							swhere = "Wi_Name = '"+winame+"' and AgreementId = '"+agreementId_val+"'";
							}
							else{
							swhere = "Child_Wi = '"+winame+"' and AgreementId = '"+agreementId_val+"'";
							}
						}
						else{
						data_save_var_db_name="Consider_For_Obligations";
						data_save_var="Consider_For_Obligations";
						swhere = "Wi_Name = '"+winame+"' and AgreementId = '"+agreementId_val+"'";
						}
					}
					
					data_save_var_arr = data_save_var.split(",");
				for(var x= 0 ; x<data_save_var_arr.length ; x++){
					var variable_id = data_save_var_arr[x]+"_"+row_arr_count;
					
					if(document.getElementById(variable_id).type =="checkbox"){
					//console.log('Value is:'+value);
					//console.log('type of Value is:'+typeof value);
						if(x==0){	
							value = "'"+document.getElementById(variable_id).checked+"'";
						}
						else{
							value = value+",'"+document.getElementById(variable_id).checked+"'";
						}
					}
					else{
						if(value==""){	
							value = "'"+document.getElementById(variable_id).value+"'";;
						}
						else{
							value = value+",'"+document.getElementById(variable_id).value+"'";
						}
			}
		}
				var params="table="+stable+"&columnsNames="+data_save_var_db_name+"&columnsValues="+value+"&swhere="+swhere;
				update_result=update_result+","+CallAjax("CustomUpdate.jsp",params);
			}
		var update_result_arr=update_result.split(",");
		var hiddenFlag=document.getElementById('hiddenFlag').value;
		var update_flag=0;
		for(var j=0 ; j<update_result_arr.length ; j++){
			if(update_result_arr[j].indexOf('ERROR')>-1){
				update_flag=1;
				break;
			}	
		}
			if(update_flag==0 && hiddenFlag!='true'){
				alert('Pipeline Data saved!!');
    }
	document.getElementById('hiddenFlag').value="";
	}	
	
  function removeClick(obj) {
   
		try{
			$("table tbody").find('input[name="check"]').each(function(){
                if($(this).is(":checked")){
                    $(this).parents("tr").remove();
						var att_name = $(this).attr('id'); 
						var remove_rowindex=att_name.substring(att_name.indexOf('_')+1);
						alert("removed ID:"+remove_rowindex);
						alert("arr: "+row_id_arr);
						row_id_arr.splice(remove_rowindex ,1);
					}
            });
		}
		catch(e)
		{
			
		}
   }
    
	
    function loadform() {
        //window.moveTo(0, 0);
        //window.resizeTo(screen.availWidth, screen.availHeight);
		fetchValidQueueData();	  
		fetchGridData();
		disable_postload();
		//fetchInternalGridData(); //Arun (29/10/17) added new method
		disabletakeoverforCC();
		try{
		window.parent.style.top="100px";
		}
		catch(ex){}
    }
	
	
		function disabletakeoverforCC(){
	var toal_row_count= $('table#mytab tr:last').index() + 1;
		
		if((activityName=="CC" )|| ((activityName=="CA" )&&(window.parent.com.newgen.omniforms.formviewer.getLVWAT('cmplx_Product_cmplx_ProductGrid',0,1)=='Credit Card' ))){
		for (var i=1;i<toal_row_count;i++){
				var TakeOver_Amount = "Takeover_Amount_"+i;
				var TakeOver_Indicator = "Takeover_Indicator_"+i;
					document.getElementById(TakeOver_Amount).disabled = true;
					document.getElementById(TakeOver_Indicator).disabled = true;
			}
		}
	
	}
	
	
	function getPosition(element) {
		var xPosition = 0;
		var yPosition = 0;
	  
		while(element) {
			xPosition += (element.offsetLeft - element.scrollLeft + element.clientLeft);
			yPosition += (element.offsetTop - element.scrollTop + element.clientTop);
			element = element.offsetParent;
		}
		return { x: xPosition, y: yPosition };
	}
//aditi	

function fetchGridData()
{
	jspName="PipelineProcedure.jsp";
	var params="";
	var result="";
	var wparams="";
	
	params="wi_name="+winame;
			
			result=CallAjax(jspName,params);
			
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
	
	//added by saurabh on 28th July 17.
	
	if (activityName == "CC" || activityName == "Cr" || activityName == "PL") {
			var cifId = window.parent.com.newgen.omniforms.formviewer.getNGValue('cmplx_Customer_CIFNo');
			if (cifId == "") {
				cifId = "123";
			}
			var sQry = "CCCRPL";
			//var sQry="select 'ng_rlos_cust_extexpo_LoanDetails' as table_name,AgreementId as reference,LoanType as Product_Type,CustRoleType,Datelastupdated as DLP,TotalNoOfInstalments as Tenor,isNull(Consider_For_Obligations,'true'),SchemeCardProd,TotalAmt as LimAmt,PaymentsAmt as EMI,NoOfDaysInPipeline,Take_Over_Indicator as TKO,Take_Amount as TK from ng_rlos_cust_extexpo_LoanDetails with (nolock) where child_Wi = '"+winame+"' and LoanStat = 'Pipeline' or LoanStat = 'CAS-Pipeline' union select 'ng_rlos_cust_extexpo_CardDetails' as table_name,CardEmbossNum as reference,CardType as Product_Type,CustRoleType,LastUpdateDate as DLP,'',isNull(Consider_For_Obligations,'true'),SchemeCardProd,TotalAmount as LimAmt,PaymentsAmount as EMI,NoOfDaysInPipeLine,'' as TKO,'' as TK from ng_rlos_cust_extexpo_CardDetails with (nolock) where child_Wi = '"+winame+"' and (CardStatus = 'Pipeline' or CardStatus = 'CAS-Pipeline') union Select 'ng_RLOS_CUSTEXPOSE_LoanDetails' as table_name,AgreementID as reference,LoanType,case when LoanStat='Pipeline' then CustRoleType else 'Primary'end ,LastUpdateDate,TotalNoOfInstalments as Tenor,isNull(Consider_For_Obligations,'true'),SchemeCardProd,TotalloanAmount,RemainingInstalments,NoOfDaysInPipeline,'','' from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where (child_Wi = '"+winame+"' or Wi_Name = '"+winame+"') and (LoanStat = 'Pipeline' or (LoanStat = 'CAS-Pipeline' and CifId='"+cifId+"') or (LoanStat = 'AL-Pipeline' and CifId='"+cifId+"'))";
			wparams = "child_Wi==" + winame + "~~child_Wi==" + winame
					+ "~~child_Wi==" + winame + "~~child_Wi==" + winame
					+ "~~cifId==" + cifId + "~~cifId==" + cifId;
		} else {//RLOS
			var sQry = "!CCCRPL";
			var cifId = window.parent.com.newgen.omniforms.formviewer.getNGValue('cmplx_Customer_CIFNO');
			//var sQry="select 'ng_rlos_cust_extexpo_LoanDetails' as table_name,AgreementId as reference,LoanType as Product_Type,CustRoleType,Datelastupdated as DLP,TotalNoOfInstalments as Tenor,isNull(Consider_For_Obligations,'true'),SchemeCardProd,TotalAmt as LimAmt,PaymentsAmt as EMI,NoOfDaysInPipeline,Take_Over_Indicator as TKO,Take_Amount as TK from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Wi_Name = '"+winame+"' and LoanStat = 'Pipeline' or LoanStat = 'CAS-Pipeline' union select 'ng_rlos_cust_extexpo_CardDetails' as table_name,CardEmbossNum as reference,CardType as Product_Type,CustRoleType,LastUpdateDate as DLP,'',isNull(Consider_For_Obligations,'true'),SchemeCardProd,TotalAmount as LimAmt,PaymentsAmount as EMI,NoOfDaysInPipeLine,'' as TKO,'' as TK from ng_rlos_cust_extexpo_CardDetails with (nolock) where Wi_Name = '"+winame+"' and CardStatus = 'Pipeline' or CardStatus = 'CAS-Pipeline'  union Select 'ng_RLOS_CUSTEXPOSE_LoanDetails' as table_name,AgreementID as reference,LoanType,case when LoanStat='Pipeline' then CustRoleType else 'Primary'end ,LastUpdateDate,TotalNoOfInstalments as Tenor,isNull(Consider_For_Obligations,'true'),SchemeCardProd,TotalloanAmount,NextInstallmentAmt,NoOfDaysInPipeline,'','' from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where Wi_Name = '"+winame+"' and (LoanStat = 'Pipeline' or (LoanStat = 'CAS-Pipeline' and CifId='"+cifId+"')or (LoanStat = 'AL-Pipeline' and CifId='"+cifId+"'))";
			wparams = "Wi_Name==" + winame + "~~Wi_Name==" + winame
					+ "~~Wi_Name==" + winame + "~~CifId==" + cifId
					+ "~~CifId==" + cifId;
		}

		//fetchdataCustom(sQry);
		jspName = "CustomSelect.jsp";
		params = "Query=" + sQry + "&wparams=" + wparams + "&pname=" + pname;

		//console.log(params);

		result = CallAjax(jspName, params);

		if (result.indexOf("FAIL") != -1) {
			alert("Some error Encountered. Please try after some time");
			return;
		} else if (result.indexOf("FailedException") != -1) {
			alert("Some Exception Occurred. Please try after some time");
			return;
		} else {
			if (result.length > 6)
				var result_check = result.substring(0, 6);
			if (result_check == 'NODATA') {

			} else {

				var result_arr = result.split("~");

				for (var i = 0; i < result_arr.length - 1; i++) {
					var nextrowid = $('table#mytab tr:last').index() + 1;
					row_id_arr.push(nextrowid);
					var markup = tb_row(nextrowid, result_arr[i]);
					$("table#mytab tbody").append(markup);
				}
			}
		}
	}

	function fetchInternalGridData() {
		jspName = "CustomSelect.jsp";
		var params = "";
		var result = "";
		var wparams = "";

		if (activityName == "CC" || activityName == "Cr"
				|| activityName == "PL") {
			var sQry = "fetchCCCRPL";
			//var sQry="Select 'ng_RLOS_CUSTEXPOSE_LoanDetails' as table_name,AgreementID as reference,LoanType,'Primary','','',isNull(Consider_For_Obligations,'true'),SchemeCardProd,TotalAmount,'','','','' from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where Child_Wi = '"+winame+"' and LoanStat = 'Pipeline' or LoanStat = 'CAS-Pipeline'";
			wparams = "Child_Wi==" + winame;
		} else {
			var sQry = "!fetchCCCRPL";
			//var sQry="Select 'ng_RLOS_CUSTEXPOSE_LoanDetails' as table_name,AgreementID as reference,LoanType,'Primary','','',isNull(Consider_For_Obligations,'true'),SchemeCardProd,TotalAmount,'','','','' from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where Wi_Name = '"+winame+"' and LoanStat = 'Pipeline' or LoanStat = 'CAS-Pipeline'";
			wparams = "Wi_Name==" + winame;
		}

		//fetchdataCustom(sQry);

		params = "Query=" + sQry + "&wparams=" + wparams + "&pname=" + pname;

		console.log(params);

		result = CallAjax(jspName, params);

		if (result.indexOf("FAIL") != -1) {
			alert("Some error Encountered. Please try after some time");
			return;
		} else if (result.indexOf("FailedException") != -1) {
			alert("Some Exception Occurred. Please try after some time");
			return;
		} else {
			if (result.length > 6)
				var result_check = result.substring(0, 6);
			if (result_check == 'NODATA') {

			} else {

				var result_arr = result.split("~");

				for (var i = 0; i < result_arr.length - 1; i++) {
					var nextrowid = $('table#mytab tr:last').index() + 1;
					row_id_arr.push(nextrowid);
					var markup = tb_row(nextrowid, result_arr[i]);
					$("table#mytab tbody").append(markup);
				}
			}
		}
	}

	/*
	 //function created by saurabh on 28th July 17.
	 function fetchdataCustom(sQry){
	 var result="";
	 jspName="CustomSelect.jsp";
	 var params="";
	 params="Query="+sQry;
	 result=CallAjax(jspName,params);
	
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
	 var result_arr = result.split("~");
	 for(var i=0;i<result_arr.length-1;i++){
	 var nextrowid= $('table#mytab tr:last').index() + 1;
	 row_id_arr.push(nextrowid);
	 var markup = tb_row(nextrowid,result_arr[i]);
	 $("table#mytab tbody").append(markup);	
	 }
	 }
	 }
	 */
	function fetchValidQueueData() {

		jspName = "CustomSelect.jsp";
		var params = "";
		var result = "";
		var wparams = "";
		//if(ev=='R')
		//{
		//result=CallAjax(jspName,params);
		//}
		var dhtml = "";
		dhtml += "<div>"
				+ "<table id='mytab' border='0' bordercolor='#337ab7' max-height='500px' style='overflow-x:scroll;overflow-y:scroll;' >";
		if (activityName == 'CC') {
			activityName = 'Cr';
		}
		sQry = "QueueData";
		//var sQry="select col_name ,col_type,max_len,col_id,default_value,is_readonly,ActivityName from NG_Dynamic_Grid_config with (nolock) WHERE Grid_name = 'Pipeline' and ActivityName='"+activityName+"'ORDER BY col_seq";
		wparams = "ActivityName==" + activityName;
		params = "Query=" + sQry + "&wparams=" + wparams + "&pname=" + pname;
		result = CallAjax(jspName, params);

		if (result.indexOf("FAIL") != -1) {
			alert("Some error Encountered. Please try after some time");
			return;
		} else if (result.indexOf("FailedException") != -1) {
			alert("Some Exception Occurred. Please try after some time");
			return;
		} else {
			WiDetails = result;

			dhtml += tb_header();
		}
		dhtml += "</table></div>";
		dhtml += "<div style='width: 100%; border: 0px #e01414  solid; padding:0px; margin: 0px'><table border='0' borderColor='#e01414' overflow-x:scroll;overflow-y:scroll>";
		if (enabledSteps.indexOf(ws_name) > -1) {
			dhtml += "<input type='button' id='savedata' value='save data' style='background-color:#e01414; color:#ffffff' onclick='saveDate()'/></div></table>";
		} else {
			dhtml += "<input type='button' id='savedata' value='save data' style='background-color:#848786; color:#ffffff' disabled/></div></table>";
		}
		//height='15px' width='15px' id='addimage1"+"' style='padding-right:10px' onclick='addClick(this)'/>
		dhtml += "<input type='hidden' id='hiddenFlag' hidden>";
		document.getElementById("mainDivContent").innerHTML = dhtml;
	}

	function tb_header() {
		var row_arr = WiDetails.split("~");
		var col_arr;
		var temp = "";
		var table_header = "<th><input type='hidden' id='table_name' name = 'table_name'></th>";
		//var table_header ="";//<th><label color:#ffffff><label>select</label></th>
		var table_col = "<td>";

		for (var i = 0; i < row_arr.length - 1; i++) {
			temp = row_arr[i];
			col_arr = temp.split("#");
			table_header = table_header + "<th><label color:#ffffff><label>"
					+ col_arr[0] + "</label></th>";
		}
		return "<tr background-color:#e01414><p align='right'>" + table_header
				+ "</tr>";
	}

	function tb_row(row_num, val) {
		var row_arr = WiDetails.split("~");
		var col_id_arr = "";
		var temp = "";
		InputField_id = "WI_Name";
		//alert("val is:" +val);
		if (val == "") {
			var table_row = "<td><input type='hidden' id ='datatablename_"+row_num+"' name='datatablename' value='"+val_new[0]+"' ></td>";
			//<td><input type='checkbox' id ='check_"+row_num+"' name='check'></td>
			for (var i = 0; i < row_arr.length - 1; i++) {
				temp = row_arr[i];
				col_id_arr = temp.split("#");
				InputField_id = InputField_id + "," + col_id_arr[3];
				if (col_id_arr[1] == "text") {
					table_row = table_row
							+ "<td>"
							+ "<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+col_id_arr[4]+"' maxlength ='"+col_id_arr[2]+"'"+col_id_arr[5]+">"
							+ "</td>";
				} else if (col_id_arr[1] == "select") {
					table_row = table_row
							+ "<td>"
							+ "<select id = '"+col_id_arr[3]+"_"+row_num+"' name = '"+col_id_arr[3]+"_"+row_num+"'>";
					var default_val = col_id_arr[4].split(",");
					for (var x = 0; x < default_val.length; x++) {
						table_row = table_row
								+ "<option value='"+default_val[x]+"' "+col_id_arr[5]+">"
								+ default_val[x] + "</option>"
					}
					table_row = table_row + col_id_arr[5] + "</select></td>";
				} else if (col_id_arr[1] == "checkbox") {
					//++ below code added by abhishek on 08/11/2017
					if ((activityName == "CC" || activityName == "PL")
							&& ws_name == "CSM") {
						table_row = table_row
								+ "<td>"
								+ "<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+col_id_arr[4]+"'>"
								+ "</td>";
					} else {
						table_row = table_row
								+ "<td>"
								+ "<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+col_id_arr[4]+"' "+col_id_arr[5]+">"
								+ "</td>";
					}
					//++ Above code added by abhishek on 08/11/2017
				}
			}
		}
		//alert("0:"+col_id_arr[0]+","+"1:"+col_id_arr[1]+","+"2:"+col_id_arr[2]+","+"3:"+col_id_arr[3]+","+"4:"+col_id_arr[4]+","+"5:"+col_id_arr[5]+","+"6:"+col_id_arr[6]+","+"length:"+col_id_arr.length);
		else {
			var val_new = val.split("#");
			var table_row = "<td><input type='hidden' id ='datatablename_"+row_num+"' name='datatablename' value='"+val_new[0]+"' ></td>";
			//var table_row ="";<td><input type='checkbox' id ='check_"+row_num+"' name='check'></td>
			for (var i = 0; i < row_arr.length - 1; i++) {
				temp = row_arr[i];
				col_id_arr = temp.split("#");
				InputField_id = InputField_id + "," + col_id_arr[3];
				if (col_id_arr[1] == "text") {
					table_row = table_row
							+ "<td>"
							+ "<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+val_new[i+1]+"' maxlength ='"+col_id_arr[2]+"'"+col_id_arr[5]+">"
							+ "</td>";
				} else if (col_id_arr[1] == "select") {
					table_row = table_row
							+ "<td>"
							+ "<select id = '"+col_id_arr[3]+"_"+row_num+"' name = '"+col_id_arr[3]+"_"+row_num+"'>";
					var default_val = col_id_arr[4].split(",");
					for (var x = 0; x < default_val.length; x++) {
						table_row = table_row
								+ "<option value='"+default_val[x]+"' "+col_id_arr[5]+">"
								+ default_val[x] + "</option>"
					}
					table_row = table_row + col_id_arr[5] + "</select></td>";
				} else if (col_id_arr[1] == "checkbox") {
					var value = val_new[i + 1];

					if (col_id_arr[3] == "Takeover_Indicator") {
						if (value == 'true') {
							table_row = table_row
									+ "<td>"
									+ "<input id = '"
									+ col_id_arr[3]
									+ "_"
									+ row_num
									+ "' type = '"
									+ col_id_arr[1]
									+ "' name = '"
									+ col_id_arr[3]
									+ "_"
									+ row_num
									+ "' "
									+ col_id_arr[4]
									+ " "
									+ col_id_arr[5]
									+ " onclick='enableTakeoverAmount(this.id);' >"
									+ "</td>";
						} else {
							table_row = table_row
									+ "<td>"
									+ "<input id = '"
									+ col_id_arr[3]
									+ "_"
									+ row_num
									+ "' type = '"
									+ col_id_arr[1]
									+ "' name = '"
									+ col_id_arr[3]
									+ "_"
									+ row_num
									+ "' "
									+ col_id_arr[5]
									+ " onclick='enableTakeoverAmount(this.id);' >"
									+ "</td>";
						}
					}
					//below code added by akshay on 3/12/17 for auto checking editable checkboxes
					else if (value == 'false' && col_id_arr[4] == 'checked') {//case for consider for obligations.
						table_row = table_row + "<td>" + "<input id = '"
								+ col_id_arr[3] + "_" + row_num + "' type = '"
								+ col_id_arr[1] + "' name = '" + col_id_arr[3]
								+ "_" + row_num + "' " + col_id_arr[5]
								+ "</td>";
					} else {
						table_row = table_row + "<td>" + "<input id = '"
								+ col_id_arr[3] + "_" + row_num + "' type = '"
								+ col_id_arr[1] + "' name = '" + col_id_arr[3]
								+ "_" + row_num + "' " + col_id_arr[4] + " "
								+ col_id_arr[5]
								+ "  onclick='Re_trigger_dectech(this.id);'>"
								+ "</td>";
					}
				} else if (col_id_arr[1] == "link") {
					table_row = table_row + "<td>" + "<a id='" + col_id_arr[3]
							+ "_" + row_num
							+ "' href='#' onclick='openPopup();return false;'>"
							+ val_new[i] + "</a>" + "</td>";
				}
			}
		}

		return "<tr background-color:#e01414 class='HeaderClass' id = row_"+row_num+" ><p align='right'>"
				+ table_row + "</tr>";
	}

	//added by nikhil to disable at pipeline grid
	function disable_postload() {
		var toal_row_count = $('table#mytab tr:last').index() + 1;

		//if((ws_name=='Disbursal' || ws_name=='CAD_Analyst1' || ws_name=='Cad_Analyst2' || ws_name=='DDVT_Maker' || ws_name=='DDVT_Checker' )){
		if (enabled_Worksteps.indexOf(ws_name) == -1) {
			for (var i = 1; i < toal_row_count; i++) {
				var Consider_For_Obligations = "Consider_For_Obligations_" + i;
				var takeover_indicator = "Takeover_Indicator_" + i;
				var takeover_amount = "Takeover_Amount_" + i;

				document.getElementById(Consider_For_Obligations).disabled = true;
				document.getElementById(takeover_indicator).disabled = true;
				document.getElementById(takeover_amount).disabled = true;

			}
			document.getElementById("savedata").disabled = true;
			document.getElementById("savedata").style.backgroundColor = 'Silver';
		}
	}

	//below function added by akshay on 31/5/18
	function enableTakeoverAmount(id) {
		var idArr = id.split('_');
		var rowNumber = idArr[idArr.length - 1];//Take_Over_Indicator_no
		if (document.getElementById(id).checked) {
			document.getElementById('Takeover_Amount_' + rowNumber).disabled = false;
			//document.getElementById('Take_Amount_'+rowNumber).value = document.getElementById('Outstanding_Amount_'+rowNumber).value;
			//document.getElementById('Consider_For_Obligations_'+rowNumber).checked = false;

		} else {
			document.getElementById('Takeover_Amount_' + rowNumber).disabled = true;
			//document.getElementById('Take_Amount_'+rowNumber).value = "";
			//document.getElementById('Consider_For_Obligations_'+rowNumber).checked = true;
		}

	}
	function Re_trigger_dectech(id) {
		window.parent.setNGValueCustom('Eligibility_Trigger', 'Y');
	}

	function ltrimString(str) {

		for (var k = 0; k < str.length && isWhitespace(str.charAt(k)); k++)
			;
		return str.substring(k, str.length);
	}
	function rtrimString(str) {
		for (var j = str.length - 1; j >= 0 && isWhitespace(str.charAt(j)); j--)
			;
		return str.substring(0, j + 1);
	}
	function trimString(str) {
		return ltrimString(rtrimString(str));
	}

	function isWhitespace(charToCheck) {
		var whitespaceChars = " \t\n\r\f";
		return (whitespaceChars.indexOf(charToCheck) != -1);
	}

	function CallAjax(jspName, params) {
		var response = "";
		try {
			var xmlReq = null;
			if (window.XMLHttpRequest)
				xmlReq = new XMLHttpRequest();
			else if (window.ActiveXObject)
				xmlReq = new ActiveXObject("Microsoft.XMLHTTP");
			if (xmlReq == null)
				return; // Failed to create the request
			xmlReq.onreadystatechange = function() {
				switch (xmlReq.readyState) {
				case 0:
					break;
				case 1:
					break;
				case 2:
					break;
				case 3:
					break;
				case 4:
					if (xmlReq.status == 200) {
						response = xmlReq.responseText;
					} else if (xmlReq.status == 404)
						alert("Some error occured. Please try after sometime: URL doesn't exist!");
					else
						alert("Some error occured. Please try after sometime or contact administrator : Pipeline");
					break;
				default:
					alert(xmlReq.status);
					break;
				}
			}

			xmlReq.open('POST', jspName, false);
			xmlReq.setRequestHeader('Content-Type',
					'application/x-www-form-urlencoded');
			xmlReq.send(params);
			return response;
		}

		catch (e) {
			alert("Some error occured. Please try after sometime or contact administrator : subprocess-e");
			//return false;
		}
	}
</script>

    </head>

    <body leftmargin="0" topmargin="0" marginwidth="0" 
          marginheight="0"  onload ="loadform();" class="dark-matter">
		   <!-- Load jQuery and bootstrap datepicker scripts -->
            <form name="wdesk">
           <div id="mainDivContent">
				
				
			</div>
			
		</form>
		<script>
		
		</script>
    </body>
</html>
