<!------------------------------------------------------------------------------------------------------
//           NEWGEN SOFTWARE TECHNOLOGIES LIMITED

//Group						 : Application ?Projects
//Product / Project			 : RAKBank-RLOS
//File Name					 : Eligibility Card Product
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
<%@ page import="XMLParser.XMLParser"%>
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
        <title>Eligibility Card Product</title>
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
	var noOfBorrower=0;
	var noOfGuarantor=0;
	var WiDetails="";
	var ChecklistDetails="";
	var PrevResCol1="";
	var pResStr="";
	var rResStr="";	
	var ResCol1="";
	var InputField_id="";
	var table_name="ng_rlos_IGR_Eligibility_CardProduct";
	var row_id_arr=[];
	var data_save_var="";
	var ws_name = window.parent.activityName;
	var pname="ECP";
	//alert(ws_name);
	  if(activityName=='Cr'|| activityName=='CDOB'){
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
		var stable="ng_rlos_IGR_Eligibility_CardProduct";
		var scolumnname = "";
		var scolumnsvalues = "";
		var swhere="";
		var update_result="";
		
		for(var row_arr_count= 1;row_arr_count<=totalrowid;row_arr_count++){
				var value = "";
				data_save_var_arr = data_save_var.split(",");
				for(var x= 0 ; x<data_save_var_arr.length ; x++){
					var variable_id = data_save_var_arr[x]+"_"+row_arr_count;
					var s_no = "S_no_"+row_arr_count;
					if(activityName=='CA'){
					swhere = "Wi_Name = '"+winame+"'  and s_no ='"+document.getElementById(s_no).value+"'";
					}
					else {
					swhere = "Child_wi = '"+winame+"'  and s_no ='"+document.getElementById(s_no).value+"'";
					}
					
					
					if(document.getElementById(variable_id).type =="checkbox"){
					console.log('Value is:'+value);
					console.log('type of Value is:'+typeof value);
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
				var params="table="+stable+"&columnsNames="+data_save_var+"&columnsValues="+value+"&swhere="+swhere;
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
				alert('Credit Card Data saved!!');
			}	
			
			document.getElementById('hiddenFlag').value='';
	}	
	function saveDateonChange() {
    	var totalrowid= $('table#mytab tr:last').index();
		var final_data = "";
		var new_row = "Y";
		var row_arr = WiDetails.split("~");		
		var stable="ng_rlos_IGR_Eligibility_CardProduct";
		var scolumnname = "";
		var scolumnsvalues = "";
		var swhere="";
		var update_result="";
		
		for(var row_arr_count= 1;row_arr_count<=totalrowid;row_arr_count++){
				var value = "";
				data_save_var_arr = data_save_var.split(",");
				for(var x= 0 ; x<data_save_var_arr.length ; x++){
					var variable_id = data_save_var_arr[x]+"_"+row_arr_count;
					var s_no = "S_no_"+row_arr_count;
					if(activityName=='CA'){
					swhere = "Wi_Name = '"+winame+"'  and s_no ='"+document.getElementById(s_no).value+"'";
					}
					else {
					swhere = "Child_wi = '"+winame+"'  and s_no ='"+document.getElementById(s_no).value+"'";
					}
					
					
					if(document.getElementById(variable_id).type =="checkbox"){
					console.log('Value is:'+value);
					console.log('type of Value is:'+typeof value);
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
				var params="table="+stable+"&columnsNames="+data_save_var+"&columnsValues="+value+"&swhere="+swhere;
				update_result=update_result+","+CallAjax("CustomUpdate.jsp",params);
			}
		var update_result_arr=update_result.split(",");
		var update_flag=0;
		for(var j=0 ; j<update_result_arr.length ; j++){
			if(update_result_arr[j].indexOf('ERROR')>-1){
				update_flag=1;
				break;
			}	
		}
			if(update_flag==0){
				//alert('Credit Card Data saved!!');
			}	
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
	window.parent.top="100px";
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
	jspName="CustomSelect.jsp";
	var params="";
	var result="";
	var sQry="";
	var wparams="";
		if(activityName=='CA'){//RLOS
			sQry="CA";
			 //sQry="select s_no,Product,Card_Product,CACCalculatedLimit,Eligible_Card,final_limit,Decision,Declined_Reason,Deviation_Code_Refer,'',Delegation_Authorithy,Score_Grade,CC_Waiver FROM  "+table_name+" with (nolock) WHERE wi_name = '"+winame+"'";
			 
			 wparams="wi_name=="+winame;
		}
		else{
			sQry="!CA";
			 //sQry="select s_no,Product,Card_Product,CACCalculatedLimit,Eligible_Card,final_limit,Decision,Declined_Reason,Deviation_Code_Refer,'',Delegation_Authorithy,Score_Grade FROM  "+table_name+" with (nolock) WHERE child_wi = '"+winame+"'"
			
			wparams="child_wi=="+winame;
		}
		
			params="Query="+sQry+"&wparams="+wparams+"&pname="+pname;
			result=CallAjax(jspName,params);
			//alert(result);
				
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
				if(result.length>6)
					var result_check = result.substring(0,6);
				if(result_check=='NODATA'){
					
				}
				else{
					var result_arr = result.split("~");
					for(var i=0;i<result_arr.length-1;i++){
					//alert(result_arr);
					var nextrowid= $('table#mytab tr:last').index() + 1;
						row_id_arr.push(nextrowid);
					var markup = tb_row(nextrowid,result_arr[i]);
					//alert(markup);
					$("table#mytab tbody").append(markup);	
					}
				}	
			}
}

function fetchValidQueueData()
{
			
			jspName="CustomSelect.jsp";
			var params="";
			var result="";
			var wparams="";
			//if(ev=='R')
			//{
			//result=CallAjax(jspName,params);
			//}
				var dhtml="";
			  	 dhtml+="<div >"+
				"<table id='mytab' border='0' bordercolor='#337ab7' max-height='500px' style='overflow-x:scroll;overflow-y:scroll;'>";
			var sQry="QueueData";			
			//var sQry="select col_name ,col_type,max_len,col_id,default_value,is_readonly from NG_Dynamic_Grid_config with (nolock) WHERE Grid_name = 'Eligibility_Card_Product' and  ActivityName='"+activityName+"'order by col_seq";
			wparams="Grid_name==Eligibility_Card_Product~~ActivityName=="+activityName;
			params="Query="+sQry+"&wparams="+wparams+"&pname="+pname;
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
			WiDetails=result;
					
			dhtml+=tb_header();
		}
				dhtml+="</table></div>";
				dhtml+="<div style='width: 100%; border: 0px #e01414  solid; padding:0px; margin: 0px'><table border='0' borderColor='#e01414 overflow-x:scroll;overflow-y:scroll'>";
		if 	(activityName=='CA'){
			 dhtml+=/*"<image src='addImage.png' height='15px' width='15px' id='addimage1"+"' style='padding-right:10px' onclick='addClick(this)'/>*/"<input type='button' id='savedata'  value='save data' style='background-color:#e01414; color:#ffffff' onclick='saveDate()'/></div></table>";
				
		}
		else{
		//change by saurabh on 12/4/19
			if ((ws_name=='CAD_Analyst1') ||(ws_name=='CSM')){	
			 dhtml+=/*"<image src='addImage.png' height='15px' width='15px' id='addimage1"+"' style='padding-right:10px' onclick='addClick(this)'/>*/"<input type='button' id='savedata'  value='save data' style='background-color:#e01414; color:#ffffff' onclick='saveDate()'/></div></table>";
			}
			else{
			 dhtml+=/*"<image src='addImage.png' height='15px' width='15px' id='addimage1"+"' style='padding-right:10px' onclick='addClick(this)'/>*/"<input type='button' id='savedata'  value='save data' style='background-color:#848786; color:#ffffff' disabled/></div></table>";
			}
		}	
	dhtml+="<input type='hidden' id='hiddenFlag' hidden>";
	document.getElementById("mainDivContent").innerHTML=dhtml;
}
//added by aman for PCSP-297
function isNumberKey(evt,fieldId){
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    //return !(charCode > 31 && (charCode < 48 || charCode > 57));
	if(charCode<48 || charCode>57){
		document.getElementById(fieldId).value = '';
	}
}	
//added by aman for PCSP-297

function setFinalLimit(id){
	limitDecimal(id);
		var CalculatedLimit_1=document.getElementById('CASCalculatedlimit_1').value;
		var subprod = window.parent.com.newgen.omniforms.formviewer.getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2);

	if (subprod=='IM' && (parseFloat(CalculatedLimit_1)< parseFloat(document.getElementById(id).value))){
		alert('Final limit cannot exceed CAS Calculated limit');
		document.getElementById(id).value='';
		//window.parent.document.getElementById('cmplx_EligibilityAndProductInfo_FinalLimit').value='';
		window.parent.setNGValueCustom('cmplx_EligibilityAndProductInfo_FinalLimit',document.getElementById(id).value);
		window.parent.setNGValueCustom('Eligibility_Trigger','Y');
		return false;
	}
	
	if(checkFalterCase()){
		//window.parent.com.newgen.omniforms.formviewer.setEnabled('cmplx_EligibilityAndProductInfo_FinalLimit',true);
		//window.parent.document.getElementById('cmplx_EligibilityAndProductInfo_FinalLimit').value = document.getElementById(id).value;
		window.parent.setNGValueCustom('cmplx_EligibilityAndProductInfo_FinalLimit',document.getElementById(id).value);
		//window.parent.com.newgen.omniforms.formviewer.setEnabled('cmplx_EligibilityAndProductInfo_FinalLimit',false);
		//below code added by nikhil to save eligibility fragment on limit change 28/11
		var value = 'EligibilityAndProductInformation';
		var field_string_value = window.parent.document.getElementById('FrameName').value;
		window.parent.setNGValueCustom('Eligibility_Trigger','Y');
		if(field_string_value.indexOf(value)==-1){
			var hiddenString = value+',';
			var previousValue = field_string_value;
			hiddenString = previousValue + hiddenString;
			window.parent.document.getElementById('FrameName').value=hiddenString;
		}
		saveDateonChange();
	}
	
}
function checkFalterCase(){
	var subprod = window.parent.com.newgen.omniforms.formviewer.getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2);
	

	if(subprod=='LI' || subprod == 'PULI'){
		if(window.parent.document.getElementById('Liability_New_IFrame_internal').contentWindow.document.getElementById('mytab')==null){
			window.parent.com.newgen.omniforms.util.showError(document.getElementById('Liability_New_IFrame_internal'),'Please expand Internal Liabilities first');
			document.getElementById('final_limit_1').value="";
			return false;
			//document.getElementById('Liability_New_IFrame_internal').contentWindow.document.getElementById('mytab').rows[1].cells[0].childNodes[0].value
		}
		else{
			var iframeId;
			if(activityName=='CA'){iframeId='Liability_New_IFrame_internal';}
			else if(activityName=='CC'){iframeId='ExtLiability_IFrame_internal';}
			var table = window.parent.document.getElementById(iframeId).contentWindow.document.getElementById('mytab');
			var totalOutstanding=0.0;
			var closedArrayInternalLiab = window.parent.document.getElementById(iframeId).contentWindow.closedOrActive;
			for(var i=1;i<table.rows.length;i++){
				if(table.rows[i].cells[0].childNodes[0].value == 'ng_RLOS_CUSTEXPOSE_CardDetails' && !closedArrayInternalLiab[i].indexOf(',C')>-1 && table.rows[i].cells[9].childNodes[0].checked){
					totalOutstanding+= Math.abs(parseFloat(table.rows[i].cells[10].childNodes[0].value));
				}
			}
			var finalLimit = document.getElementById('final_limit_1').value;
			if(totalOutstanding>Math.abs(parseFloat(finalLimit))){
				window.parent.com.newgen.omniforms.util.showError(document.getElementById('final_limit_1'),'Final limit cannot be less than Total Outstanding Amount');
				document.getElementById('final_limit_1').value = '';
				return false;
			}
		}
	}
	return true
}

function tb_header(){
	var row_arr = WiDetails.split("~");
	var col_arr;
	var temp = "";
	var table_header ="";
	var table_col = "<td>";
		
	for(var i=0;i<row_arr.length-1;i++){
		temp = row_arr[i];
		col_arr=temp.split("#");
		table_header = table_header +"<th><label color:#ffffff><label>"+col_arr[0]+"</label></th>";
		if(col_arr[5]!='readonly' && col_arr[5]!='disabled'){
			if(data_save_var==""){
				data_save_var = col_arr[3];	
			}
			else{
				data_save_var = data_save_var+","+col_arr[3];	
			}
			
		}
	}
	return "<tr background-color:#e01414><p align='right'>"+table_header+"</tr>";
}


function tb_row(row_num, val){
	var row_arr = WiDetails.split("~");
	var col_id_arr="";
	var FinalLimit= window.parent.document.getElementById('cmplx_EligibilityAndProductInfo_FinalLimit').value;
	var subprod = window.parent.com.newgen.omniforms.formviewer.getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2);
	var temp = "";
	InputField_id="WI_Name";
	if(val==""){
		var table_row ="";
		for(var i=0;i<row_arr.length-1;i++){
			temp = row_arr[i];
			col_id_arr=temp.split("#");
			InputField_id = InputField_id+","+col_id_arr[3];
				
			if(col_id_arr[1]=="text"){
				table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+col_id_arr[4]+"' maxlength ='"+col_id_arr[2]+"' "+col_id_arr[5]+">"+"</td>";
			}
			else if(col_id_arr[1]=="select"){
				table_row = table_row +"<td>"+"<select id = '"+col_id_arr[3]+"_"+row_num+"' name = '"+col_id_arr[3]+"_"+row_num+"'>";
				var default_val = col_id_arr[4].split(",");
				for(var x=0;x<default_val.length;x++){
					table_row = table_row+"<option value='"+default_val[x]+"' "+col_id_arr[5]+">"+default_val[x]+"</option>"
				}	
				table_row = table_row+col_id_arr[5]+"</select></td>";
			}
			else if(col_id_arr[1]=="checkbox"){
				table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+col_id_arr[4]+"' "+col_id_arr[5]+">"+"</td>";
			}
		}
	}
	else{
		var val_new = val.split("#");
		var table_row ="";
		for(var i=0;i<row_arr.length-1;i++){
			temp = row_arr[i];
			col_id_arr=temp.split("#");
			InputField_id = InputField_id+","+col_id_arr[3];
			if((col_id_arr[1]=="text")){
				if(activityName=='CA'){ 
					if((row_num!=1)&&(i==5)){
						table_row=  table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = 'text' name = '"+col_id_arr[3]+"_"+row_num+"' value='' maxlength ='50' readonly>"+"</td>";
					}
					else if ((row_num==1)&&(i==5)){
						table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+FinalLimit+"'  onchange ='setFinalLimit(this.id);'  maxlength ='21' "+col_id_arr[5]+">"+"</td>";
					}
					else if(col_id_arr[3].indexOf('CAS')>-1){
						if (subprod=='IM'){
							table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' data-toggle='tooltip' onmousemove='title=this.value' onmouseover='title=this.value' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+val_new[i]+"' maxlength ='"+col_id_arr[2]+"' "+col_id_arr[5]+">"+"</td>";
					
						}
						else{
						//below code changed by nikhil for PCSP-390
						
							table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' data-toggle='tooltip' onmousemove='title=this.value' onmouseover='title=this.value' name = '"+col_id_arr[3]+"_"+row_num+"' value='' maxlength ='"+col_id_arr[2]+"' "+col_id_arr[5]+" disabled>"+"</td>";
						
					
						}
						
					}
					else{
					if(col_id_arr[3]=="Deviation_Code_Refer")
					{
						table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"'  title='"+val_new[i]+"'  name = '"+col_id_arr[3]+"_"+row_num+"' value='' maxlength ='"+col_id_arr[2]+"' "+col_id_arr[5]+">"+"</td>";
					}
					else
					{
						table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"'  title='"+val_new[i]+"'  name = '"+col_id_arr[3]+"_"+row_num+"' value='"+val_new[i]+"' maxlength ='"+col_id_arr[2]+"' "+col_id_arr[5]+">"+"</td>";
					}
					}
				}
				else {
					if((row_num!=1)&&(i==5)){
						table_row=  table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = 'text' name = '"+col_id_arr[3]+"_"+row_num+"' value='' maxlength ='50' readonly>"+"</td>";
					}
					else if ((row_num==1)&&(i==5)){
						if (ws_name=='CAD_Analyst1'  || ws_name=='CSM'){
						table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+FinalLimit+"'    onchange ='setFinalLimit(this.id);' maxlength ='21' "+col_id_arr[5]+">"+"</td>";
						}
						else{
							table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+FinalLimit+"'   onchange ='setFinalLimit(this.id);' maxlength ='"+col_id_arr[2]+"' "+col_id_arr[5]+" disabled>"+"</td>";
						}
					}
					
					else if(row_num==1 && i==3){
						if(subprod!='IM')
						{
							table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"'  title='"+val_new[i]+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"'maxlength ='"+col_id_arr[2]+"' disabled>"+"</td>";
						}
						else{
							table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"'  title='"+val_new[i]+"'  type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+val_new[i]+"' maxlength ='"+col_id_arr[2]+"' "+col_id_arr[5]+">"+"</td>";
						}	
					}
					//ended by akshay	commented for PROC-7583*/				
					else{
						//below code added by nikhil for PCSP-484 and 390
						if(col_id_arr[3]=="CASCalculatedlimit" || col_id_arr[3]=="Deviation_Code_Refer")
						{
							table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"'    name = '"+col_id_arr[3]+"_"+row_num+"'  maxlength ='"+col_id_arr[2]+"' "+col_id_arr[5]+">"+"</td>";
						}
						
						else
						{
							table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"'  title='"+val_new[i]+"'  name = '"+col_id_arr[3]+"_"+row_num+"' value='"+val_new[i]+"' maxlength ='"+col_id_arr[2]+"' "+col_id_arr[5]+">"+"</td>";
						}
					}
					//Changes Done by aman for tooltip
				}	
			}
			else if((col_id_arr[1]=="hidden")){
				table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = 'text' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+val_new[i]+"' maxlength ='"+col_id_arr[2]+"' "+col_id_arr[5]+">"+"</td>";
			}
			else if(col_id_arr[1]=="select"){
				table_row = table_row +"<td>"+"<select id = '"+col_id_arr[3]+"_"+row_num+"' name = '"+col_id_arr[3]+"_"+row_num+"'>";
				var default_val = col_id_arr[4].split(",");
				for(var x=0;x<default_val.length;x++){
					table_row = table_row+"<option value='"+default_val[x]+"' "+col_id_arr[5]+">"+default_val[x]+"</option>"
				}	
				table_row = table_row+col_id_arr[5]+"</select></td>";
			}
			else if(col_id_arr[1]=="checkbox"){
				table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+col_id_arr[4]+"' "+col_id_arr[5]+">"+"</td>";
			}
		}
	}
	
	return "<tr background-color:#e01414 class='HeaderClass' id = row_"+row_num+" ><p align='right'>"+table_row+"</tr>";
}

function limitDecimal(Id){
	if(Id.indexOf('final_limit')>-1){
		var value = document.getElementById(Id).value;
		if(isNaN(value)){
			alert('Please enter only numberic Values');
			document.getElementById(Id).value = "";
		}
		else{
		if(value.indexOf('.')>-1){
			var valArr = value.split('.');
			var beforeDecimal = valArr[0];
			var afterDecimal = valArr[1];
				if(beforeDecimal.length >18 || afterDecimal.length>2){
					alert('Incorrect Field Pattern. It should be (18,2)');
					document.getElementById(Id).value = "";
				}
			}
			else{
				if(value.length > 18){
				alert('Incorrect Field Pattern. It should be (18,2)');
				document.getElementById(Id).value = "";
				}
			}
		}
	
	}
	
}

function ltrimString(str) 
{ 
	
	for(var k = 0; k < str.length && isWhitespace(str.charAt(k)); k++);
	return str.substring(k, str.length);
}
function rtrimString(str) 
{
	for(var j=str.length-1; j>=0 && isWhitespace(str.charAt(j)) ; j--) ;
	return str.substring(0,j+1);
}
function trimString(str)  
{	
	return ltrimString(rtrimString(str));
}

function isWhitespace(charToCheck) 
{
	var whitespaceChars = " \t\n\r\f";
	return (whitespaceChars.indexOf(charToCheck) != -1);
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
					{
						alert("URL doesn't exist!");
						}
					else 
					{
						//changed by nikhil 06/12/18 
							alert("Error in Eligibility_Card_Product. Params:"+params+". Please Contact Administrator");	
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
		alert("Some error occured. Please try after sometime or contact administrator");
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
