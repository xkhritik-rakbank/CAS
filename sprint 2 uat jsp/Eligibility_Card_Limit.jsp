<!------------------------------------------------------------------------------------------------------
//           NEWGEN SOFTWARE TECHNOLOGIES LIMITED

//Group						 : Application ?Projects
//Product / Project			 : RAKBank-RLOS
//File Name					 : Eligibility Card Limit
//Author                     : Deepak Kumar	
//DESC						 : To generate and save Editable Grid JSP
//Date written (DD/MM/YYYY)  : 2/02/2017
//---------------------------------------------------------------------------------------------------->


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
	var activityName = winame.substring(0,2);
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
	var table_name="ng_rlos_IGR_Eligibility_CardLimit";
	var row_id_arr=[];
	var selectedRow = {};
	var ws_name = window.parent.activityName;
	var cc_waiver_flag = false;
	var notExisting_flag = false;
	var initiationSteps = ['Branch_Init','CSM','Telesales_Init','Fulfillment_RM','Telesales_RM'];
	var pname="ECL";
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
		//change by saurabh on 7th March
		var total_limit = window.parent.document.getElementById('cmplx_EligibilityAndProductInfo_FinalLimit').value;
		if(activityName=='CA')
		{
		var PL_HIDDEN =window.parent.document.getElementById('cmplx_EligibilityAndProductInfo_PLHidden').value;
		if (PL_HIDDEN != ''){
		 total_limit=PL_HIDDEN;
		}
		}
		
		var total_combined ="0";
		var total_ind ="0";
		var scolumnname = "";
		var scolumnsvalues = "";
		var swhere="";
		var update_result="";
		
		var row_arr = WiDetails.split("~");		
		var stable="ng_rlos_IGR_Eligibility_CardLimit";
	
		for(var i=1;i<=row_id_arr.length;i++){
			var final_limit = "Final_Limit_"+i;
			var combined_limit_flag = "combined_limit_"+i;
			var curr_flag= "flag_"+i;
			var card_selected="Cardproductselect_"+i;
			//changed by nikhil for selected card only
			if(document.getElementById(curr_flag).value =='N'){
				if(document.getElementById(combined_limit_flag).value == '1' && total_combined=='0' && document.getElementById(card_selected).checked==true){
					total_combined = document.getElementById(final_limit).value
				}
				//changed by nikhil for selected card only
				else if((document.getElementById(combined_limit_flag).value == '0')&&(document.getElementById(final_limit).value!='') && document.getElementById(card_selected).checked==true){
					total_ind = parseInt(total_ind)+parseInt(document.getElementById(final_limit).value);
				}
			}
		}
		//change by saurabh on 25th Dec
		if(total_limit<(parseInt(total_ind)+parseInt(total_combined)) && !cc_waiver_flag && notExisting_flag)
		{
			alert("All Eligible Cards limits should not be greater then total Final Limit!")
			return false;
		}
	for(var row_arr_count= 1;row_arr_count<=totalrowid;row_arr_count++){
				var value = "";
				data_save_var_arr = InputField_id.split(",");
				for(var x= 0 ; x<data_save_var_arr.length ; x++){
					var variable_id = data_save_var_arr[x]+"_"+row_arr_count;
					var Card_Product = "Card_Product_"+row_arr_count;
					var EligibleAmount = "Eligible_Limit_"+row_arr_count;
					if(activityName=='CA'){
					swhere = "Wi_Name = '"+winame+"'  and Card_Product ='"+document.getElementById(Card_Product).value+"' and Eligible_Limit='"+document.getElementById(EligibleAmount).value+"'";
					}
					else {
					swhere = "Child_wi = '"+winame+"'  and Card_Product ='"+document.getElementById(Card_Product).value+"' and Eligible_Limit='"+document.getElementById(EligibleAmount).value+"'";
					}
					
					
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
				var params="table="+stable+"&columnsNames="+InputField_id+"&columnsValues="+value+"&swhere="+swhere;
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
				alert('eligible for card grid Data saved!!');
			}
			document.getElementById('hiddenFlag').value='';
			}
	
	function saveDateonChange() {
    	var totalrowid= $('table#mytab tr:last').index();
		var final_data = "";
		var new_row = "Y";
		var total_limit =  window.parent.document.getElementById('cmplx_EligibilityAndProductInfo_FinalLimit').value;
		var total_combined ="0";
		var total_ind ="0";
		var scolumnname = "";
		var scolumnsvalues = "";
		var swhere="";
		var update_result="";
		var row_arr = WiDetails.split("~");		
		var stable="ng_rlos_IGR_Eligibility_CardLimit";
	
		
	for(var row_arr_count= 1;row_arr_count<=totalrowid;row_arr_count++){
				var value = "";
				data_save_var_arr = InputField_id.split(",");
				for(var x= 0 ; x<data_save_var_arr.length ; x++){
					var variable_id = data_save_var_arr[x]+"_"+row_arr_count;
					var Card_Product = "Card_Product_"+row_arr_count;
					var EligibleAmount = "Eligible_Limit_"+row_arr_count;
					if(activityName=='CA'){
					swhere = "Wi_Name = '"+winame+"'  and Card_Product ='"+document.getElementById(Card_Product).value+"' and Eligible_Limit='"+document.getElementById(EligibleAmount).value+"'";
					}
					else {
					swhere = "Child_wi = '"+winame+"'  and Card_Product ='"+document.getElementById(Card_Product).value+"' and Eligible_Limit='"+document.getElementById(EligibleAmount).value+"'";
					}
					
					
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
				var params="table="+stable+"&columnsNames="+InputField_id+"&columnsValues="+value+"&swhere="+swhere;
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
					//alert('eligible for card grid Data saved!!');
			}
		}
function limitDecimal(Id){
	if(Id.indexOf('Final')>-1){
		var value = document.getElementById(Id).value;
		if(isNaN(parseFloat(value))){
			alert('Please enter only numberic Values');
			document.getElementById(Id).value = "";
		}
		else{
		if(value.indexOf('.')>-1 && value.indexOf('.')!=0){
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
				else if(value.indexOf('.')==0){
				alert('Incorrect Field value');
					document.getElementById(Id).value = "";
				}
			}
		}
	
	}
	
}
	function Final_limitCal(id){
	limitDecimal(id);
	var islamic=0;
	var conventional=0;
	var plHidden='';
	if(window.parent.document.getElementById('cmplx_EligibilityAndProductInfo_PLHidden')){
		plHidden = window.parent.document.getElementById('cmplx_EligibilityAndProductInfo_PLHidden').value;
	}
	var limit = document.getElementById(id).value;
	var curr_row_arr = id.split('_');
	var curr_row = curr_row_arr[2];
	var combined_limit_flag = "combined_limit_"+curr_row;
	if(document.getElementById(combined_limit_flag).value=='1'){
		var total_row_count= $('table#mytab tr:last').index();
		for (var row_count = 1;row_count<= total_row_count ; row_count++){
			var curr_flag= "flag_"+row_count;
			var curr_combined_limit_flag = "combined_limit_"+row_count;
			
			if(document.getElementById(curr_flag).value=='N' && document.getElementById(curr_combined_limit_flag).value=='1'){
			var cuur_final_limit = "Final_Limit_"+row_count;
				document.getElementById(cuur_final_limit).value = limit;
			}
		}
	}
	//Removed by aman to save Value in case of CC
	var total_row_count= $('table#mytab tr:last').index();
	for (var row_count = 1;row_count<= total_row_count ; row_count++){
		var curr_flag= "combined_limit_"+row_count;
		var curr_flag_value=document.getElementById(curr_flag).value;
		var cuur_final_limit = "Final_Limit_"+row_count;
		var cuur_final_limit_val=document.getElementById(cuur_final_limit).value;
		
		if 	(curr_flag_value=='1' && cuur_final_limit_val!= ''){
			conventional=document.getElementById(cuur_final_limit).value;
		}
		else  if 	(curr_flag_value=='0' && cuur_final_limit_val!= ''){
			islamic=parseInt(islamic)+parseInt(document.getElementById(cuur_final_limit).value);
		}
	}
	window.parent.document.getElementById('cmplx_EligibilityAndProductInfo_EFCHidden').value = parseInt(islamic)+parseInt(conventional);
	var efcHidden=window.parent.document.getElementById('cmplx_EligibilityAndProductInfo_EFCHidden').value;
	if (plHidden==''){
	plHidden=0;
	}
	if (efcHidden==''){
	efcHidden=0;
	}
	if (window.parent.com.newgen.omniforms.formviewer.getLVWAT('cmplx_Product_cmplx_ProductGrid',0,1)=='Credit Card' ){
	window.parent.document.getElementById('cmplx_EligibilityAndProductInfo_FinalLimit').value = parseInt(plHidden)+parseInt(efcHidden);
	}
	else{
	window.parent.document.getElementById('cmplx_EligibilityAndProductInfo_FinalLimit').value = parseInt(plHidden);
	
	}
	/*
	if(window.parent.document.getElementById('Application_Type').value.indexOf('TOP')>-1)
	{
		var outstandingAmt_Liability=window.parent.document.getElementById('Application_Type').value;
	}
	*/
	
	saveDateonChange();
	
	//below code by nikhil to save jsp on change on master save
	var value = 'EligibilityAndProductInformation';
		var field_string_value = window.parent.document.getElementById('FrameName').value;
		if(field_string_value.indexOf(value)==-1){
			var hiddenString = value+',';
			var previousValue = field_string_value;
			hiddenString = previousValue + hiddenString;
			window.parent.document.getElementById('FrameName').value=hiddenString;
			}
	}
function isNumberKey(evt,fieldId){
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    //return !(charCode > 31 && (charCode < 48 || charCode > 57));
	if(charCode<48 || charCode>57){
		document.getElementById(fieldId).value = '';
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
		disable_CCWaiver();
		disable_existing();
		listIslamicCards();
		disable_at_DDVT();
		CC_Waiver_Check('');
		
		//window.style.top="100px";

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

function disable_existing(){
	var toal_row_count= $('table#mytab tr:last').index() + 1;
		for (var i=1;i<toal_row_count;i++){
			var exis_flag = "flag_"+i;
			
			if(document.getElementById(exis_flag).value=='E' || document.getElementById(exis_flag).value=='e'){
				var limit = "Final_Limit_"+i;
				var select = "Cardproductselect_"+i;
				var waiver = "CC_Waiver_"+i;
				document.getElementById(limit).disabled = true;
				document.getElementById(select).disabled = true;
				document.getElementById(waiver).disabled = true;
				}
				else{
					notExisting_flag = true;
				}
		}
}

function disable_CCWaiver(){
	var toal_row_count= $('table#mytab tr:last').index() + 1;
	if((activityName=="CA" || activityName=="CC") && window.parent.com.newgen.omniforms.formviewer.getLVWAT('cmplx_Product_cmplx_ProductGrid',0,1)  =='Credit Card'){
		for (var i=1;i<toal_row_count;i++){
				var CC_Waiver = "CC_Waiver_"+i;
				document.getElementById(CC_Waiver).disabled = true;
				
				}
		}
}

//added by nikhil to disable at ddvt
function disable_at_DDVT(){
	var toal_row_count= $('table#mytab tr:last').index() + 1;
	if(((activityName=="CC") && window.parent.com.newgen.omniforms.formviewer.getLVWAT('cmplx_Product_cmplx_ProductGrid',0,1)  =='Credit Card' && (ws_name=='DDVT_Maker' || ws_name=='Cad_Analyst2')) || ((activityName=="PL") && (ws_name=='DDVT_Checker' || ws_name=='Original_Validation'))){
		for (var i=1;i<toal_row_count;i++){
				var product =  "Cardproductselect_"+i;
				var CC_Waiver = "CC_Waiver_"+i;
				var card_prod = "Cardproductselect_"+i;
				var elig_limit = "Eligible_Limit_"+i;
				var final_limit = "Final_Limit_"+i;
				document.getElementById(product).disabled = true;
				document.getElementById(CC_Waiver).disabled = true;
				document.getElementById(card_prod).disabled = true;
				document.getElementById(elig_limit).disabled = true;
				document.getElementById(final_limit).disabled = true;
				
				}
		}
	//DOne for PCSP-128 by aman	
		
	if(((activityName=="CC") && window.parent.com.newgen.omniforms.formviewer.getLVWAT('cmplx_Product_cmplx_ProductGrid',0,1)  =='Credit Card' && ( ws_name=='CAD_Analyst1')) || ((activityName=="PL") && (ws_name=='CAD_Analyst1'))){
		for (var i=1;i<toal_row_count;i++){
				var product =  "Cardproductselect_"+i;
				document.getElementById(product).disabled = true;
				
				}
		}	
//DOne for PCSP-128 by aman		
		
}


function fetchGridData()
{
	jspName="CustomSelect.jsp";
	var params="";
	var result="";
	var sQry="";
	var wparams="";
	//var sQry="select Card_Product,Eligible_Limit,Final_Limit FROM  "+table_name+" with (nolock) WHERE wi_name = '"+winame+"'";
			if(activityName=='CA'){//RLOS
			sQry="CA";
				//sQry="select distinct Cardproductselect,Card_Product,Eligible_Limit,Final_Limit,CC_Waiver,flag,combined_limit FROM  "+table_name+" with (nolock) WHERE wi_name = '"+winame+"' order by Cardproductselect desc";
				wparams="wi_name=="+winame;
			}
			else{
					sQry="!CA";
				//sQry="select distinct Cardproductselect,Card_Product,Eligible_Limit,Final_Limit,CC_Waiver,flag,combined_limit FROM  "+table_name+" with (nolock) WHERE Child_wi = '"+winame+"' order by Cardproductselect desc";
				wparams="Child_wi=="+winame;
			}
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
				var result_arr = result.split("~");
				for(var i=0;i<result_arr.length-1;i++){
				var nextrowid= $('table#mytab tr:last').index() + 1;
					row_id_arr.push(nextrowid);
				var markup = tb_row(nextrowid,result_arr[i]);
				$("table#mytab tbody").append(markup);	
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
			  	 dhtml+="<div>"+
				"<table id='mytab' border='0' bordercolor='#337ab7' max-height='500px' style='overflow-x:scroll;overflow-y:scroll;'>";
						var sQry="QueueData";
			//var sQry="select col_name ,col_type,max_len,col_id,default_value,is_readonly from NG_Dynamic_Grid_config with (nolock) WHERE Grid_name = 'Eligibility_Card_Limit' order by col_seq";
			wparams="Grid_name==Eligibility_Card_Limit";
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
			dhtml+=/*"<image src='addImage.png' height='15px' width='15px' id='addimage1"+"' style='padding-right:10px' onclick='addClick(this)'/>*/"<input type='button' id='savedata' value='save data' style='background-color:#e01414; color:#ffffff' onclick='saveDate()'/></div></table>";
			
		}
		else if 	(ws_name=='CSM'){
			dhtml+=/*"<image src='addImage.png' height='15px' width='15px' id='addimage1"+"' style='padding-right:10px' onclick='addClick(this)'/>*/"<input type='button' id='savedata' value='save data' style='background-color:#e01414; color:#ffffff' onclick='saveDate()'/></div></table>";
			
		}
		else{
			if (ws_name=='CAD_Analyst1'){	
			 dhtml+=/*"<image src='addImage.png' height='15px' width='15px' id='addimage1"+"' style='padding-right:10px' onclick='addClick(this)'/>*/"<input type='button' id='savedata' value='save data' style='background-color:#e01414; color:#ffffff' onclick='saveDate()'/></div></table>";
			}
			else{
			 dhtml+=/*"<image src='addImage.png' height='15px' width='15px' id='addimage1"+"' style='padding-right:10px' onclick='addClick(this)'/>*/"<input type='button' id='savedata' value='save data' style='background-color:#848786; color:#ffffff' disabled/></div></table>";
			}
		}
		dhtml+="<input type='hidden' id='hiddenFlag' hidden>";
	document.getElementById("mainDivContent").innerHTML=dhtml;
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
		if(col_arr[0]=="flag" || col_arr[0]=="combined_limit"){
		}
		else{
			table_header = table_header +"<th><label color:#ffffff><label>"+col_arr[0]+"</label></th>";
		}
		
	}
	return "<tr background-color:#e01414><p align='right'>"+table_header+"</tr>";
}

function tb_row(row_num, val){
	var row_arr = WiDetails.split("~");
	var col_id_arr="";
	var temp = "";
	//InputField_id="WI_Name";
	InputField_id="";//Aman 15Nov2017 changes to remove Wi_name from save field list.
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
			if (InputField_id==''){
			InputField_id=col_id_arr[3];
			}
			else{
			InputField_id = InputField_id+","+col_id_arr[3];
			}
			if(col_id_arr[1]=="text" && col_id_arr[3]=="Final_Limit"){
			
				if(initiationSteps.indexOf(ws_name)==-1 && 'CAD_Analyst1'.indexOf(ws_name)==-1){
					table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+val_new[i]+"' maxlength ='"+col_id_arr[2]+"'"+col_id_arr[5]+" disabled >"+"</td>";
				}
				else{
					table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+val_new[i]+"' maxlength ='"+col_id_arr[2]+"' onkeypress='isNumberKey(event,this.id);' onchange ='Final_limitCal(this.id);setCardProdDropdown();' "+col_id_arr[5]+">"+"</td>";
				}
			}
			else if(col_id_arr[3]=="flag" || col_id_arr[3]=="combined_limit" ){
				table_row = table_row +"<td>"+"<input type='hidden' id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+val_new[i]+"' maxlength ='"+col_id_arr[2]+"' onchange ='Final_limitCal(this.id);' "+col_id_arr[5]+">"+"</td>";
			}
			else if(col_id_arr[1]=="text"){
				table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+val_new[i]+"'  maxlength ='"+col_id_arr[2]+"' "+col_id_arr[5]+">"+"</td>";
			}
			else if(col_id_arr[1]=="select"){
				table_row = table_row +"<td>"+"<select id = '"+col_id_arr[3]+"_"+row_num+"' name = '"+col_id_arr[3]+"_"+row_num+"' onchange='saveDateonChange();SelectCheck(this.id)'>";
				var default_val = col_id_arr[4].split(",");
				for(var x=0;x<default_val.length;x++){
					table_row = table_row+"<option value='"+default_val[x]+"' "+col_id_arr[5]+">"+default_val[x]+"</option>"
				}	
				table_row = table_row+col_id_arr[5]+"</select></td>";
			}
			else if(col_id_arr[1]=="checkbox"){
				var value = val_new[i];
				
				if(initiationSteps.indexOf(ws_name)==-1 && 'CAD_Analyst1'.indexOf(ws_name)==-1){
					if(value == "true"){
						table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+col_id_arr[4]+" "+col_id_arr[5]+"  checked disabled>"+"</td>";
					}
					else{
						table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+col_id_arr[4]+"' "+col_id_arr[5]+" disabled >"+"</td>";
					}
				}
				
				else{
					if(col_id_arr[3] == "CC_Waiver"){
						if(value == "true"){
						table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+col_id_arr[4]+" "+col_id_arr[5]+"  checked onclick='CC_Waiver_Check(this.id);islamicFieldsVisibility(this.id);' >"+"</td>";
						}
						else{
						table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+col_id_arr[4]+"' "+col_id_arr[5]+" onclick='CC_Waiver_Check(this.id);islamicFieldsVisibility(this.id);' >"+"</td>";
						}
					}	
					else{
						if(value == "true"){
						table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+col_id_arr[4]+" "+col_id_arr[5]+"  checked onclick='islamicFieldsVisibility(this.id);SelectCheck(this.id);setCardProdDropdown()'  onchange='saveDateonChange()'>"+"</td>";
						}
						else{
						table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+col_id_arr[4]+"' "+col_id_arr[5]+" onclick='islamicFieldsVisibility(this.id);SelectCheck(this.id);setCardProdDropdown()'  onchange='saveDateonChange()'>"+"</td>";
						}
					}
				}	
			}
		}
	}
	
	return "<tr background-color:#e01414 class='HeaderClass' id = row_"+row_num+" ><p align='right'>"+table_row+"</tr>";
}
//added by akshay on 27/12/17
function CC_Waiver_Check(id)
{
	
	var total_row_count= $('table#mytab tr:last').index();
	var flag_CC_Waiver=false;
	for (var row_count = 1;row_count<= total_row_count ; row_count++){
		if(document.getElementById('CC_Waiver_'+row_count).checked==true){
			window.parent.document.getElementById('is_cc_waiver_require').value='Y';
	for (var row_count = 1;row_count<= total_row_count ; row_count++){
					document.getElementById('CC_Waiver_'+row_count).checked=true;
					document.getElementById('Cardproductselect_'+row_count).disabled=true;
					document.getElementById('Cardproductselect_'+row_count).checked=false;
		}
		
			flag_CC_Waiver=true;
			break;
		}
		else{
			for (var row_count = 1;row_count<= total_row_count ; row_count++){
				document.getElementById('CC_Waiver_'+row_count).checked=false;
				//Deepak below condition changed for PCSP128
				//if(initiationSteps.indexOf(ws_name)>-1 || ws_name=='CAD_Analyst1')
				if(initiationSteps.indexOf(ws_name)>-1)
				{
					document.getElementById('Cardproductselect_'+row_count).disabled=false;
				}
			}	
		}
	}
	if(flag_CC_Waiver==false){
				window.parent.document.getElementById('is_cc_waiver_require').value='N';
		}
}
//ended by akshay on 27/12/17

function SelectCheck(id){
	var total_row_count= $('table#mytab tr:last').index();
	var i=0;
	for (var row_count = 1;row_count<= total_row_count ; row_count++){
		var exis_flag = "flag_"+row_count;
		var Selected = "Cardproductselect_"+row_count;
		if(document.getElementById(exis_flag).value=='E' || document.getElementById(exis_flag).value=='e'){
			i++;
		}
		if (document.getElementById(Selected).checked==true)
		{
			i++;
		}
	}
	if (parseInt(i)>9){
		document.getElementById(id).checked=false;
		alert('Cannot select more then 9 Cards.');
		
	}
	//console.log('Existing card are'+i);
	
}


function setCardProdDropdown(){
	var dropdown = ['--Select--'];
	var final_limitArr = ['0'];
	var element = window.parent.document.getElementById('SupplementCardDetails_CardProduct');
	if(element){
	var len = element.options.length;
	for(var i=0;i<len;i++){
		element.options.remove(0);
	}
	var table = document.getElementById('mytab');
	for(var i=1;i<table.rows.length;i++){
		if(table.rows[i].cells[0].childNodes[0].checked){
			dropdown.push(table.rows[i].cells[1].childNodes[0].value);
			final_limitArr.push(table.rows[i].cells[3].childNodes[0].value);
		}
	}
	if(dropdown.length>0){
		var element = window.parent.document.getElementById('SupplementCardDetails_CardProduct');
		for(var j=0;j<dropdown.length;j++){
			var option = document.createElement("option");
			if(dropdown[j]!='--Select--'){
			option.innerHTML = dropdown[j]+'('+final_limitArr[j]+')';
			}
			else{
			option.innerHTML = dropdown[j];	
			}
			option.value= dropdown[j];
			//option.name = final_limitArr[j];
			element.options.add(option);
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

function listIslamicCards(){
	var table = document.getElementById('mytab');
	for(var i=1; i<table.rows.length;i++){
		var cardProd;
		var selected;
		var combinedLimit;
		for(var j=0;j< table.rows[i].cells.length;j++){
			if(table.rows[i].cells[j].childNodes[0].id.indexOf('Card_Product')>-1){cardProd = table.rows[i].cells[j].childNodes[0].value;}
			else if(table.rows[i].cells[j].childNodes[0].id.indexOf('Cardproductselect')>-1){selected = table.rows[i].cells[j].childNodes[0].checked;}
			else if(table.rows[i].cells[j].childNodes[0].id.indexOf('combined_limit')>-1){combinedLimit = table.rows[i].cells[j].childNodes[0].value;}
		}
		//changed by nikhil for PROC-8675
		if(cardProd && combinedLimit=='0' && selected && cardProd.indexOf("LOC")==-1){
				selectedRow[i]='VM';
			}
			/*else if(cardProd && cardProd.indexOf('MY RAK')>-1 && selected){
				selectedRow[i]='VM';
			}*/
			else if(cardProd && cardProd.indexOf('KALYAN')>-1 && selected){
				selectedRow[i]='KV';
			}			
	}

}

function islamicFieldsVisibility(id){
var row = id.substring(id.lastIndexOf('_')+1,id.length);
if(window.parent.com.newgen.omniforms.formviewer.isVisible('CardDetails_Frame1'))
{
		if(id.indexOf('Cardproductselect')>-1)
		{
			
			var row = id.substring(id.lastIndexOf('_')+1,id.length);
			if(!selectedRow[row]){
				var cardProd = document.getElementById('Card_Product_'+row).value;
				var combinedLimit = document.getElementById('combined_limit_'+row).value;
				if(combinedLimit=='0')
				{
					selectedRow[row]='VM';
					setVisibility(true);
				}
				/*else if(cardProd.indexOf('MY RAK')>-1)
				{
					selectedRow[row]='VM';
					setVisibility(true);
					window.parent.com.newgen.omniforms.formviewer.setNGValue('CardDetails_Islamic_mandatory','Y');
				}*/
				else if(cardProd.indexOf('KALYAN')>-1){
					selectedRow[row]='KV';
					setKalyanVisibility(true);
					//window.parent.com.newgen.omniforms.formviewer.setNGValue('CardDetails_Islamic_mandatory','Y');
				}
			}
			else{
				delete selectedRow[row];
				if(Object.keys(selectedRow).length==0){
					setVisibility(false);
					setKalyanVisibility(false);
					window.parent.com.newgen.omniforms.formviewer.setNGValue('CardDetails_Islamic_mandatory','N');
				}
				else{
				var mandatory_flag =false;
					for(var key in selectedRow){
						if(selectedRow[key]=='VM'){
							mandatory_flag =true;
						}
						else if(selectedRow[key]=='KV'){
							setKalyanVisibility(true);
						}
					}
					if(mandatory_flag){
						window.parent.com.newgen.omniforms.formviewer.setNGValue('CardDetails_Islamic_mandatory','Y');
					}
					else{
					window.parent.com.newgen.omniforms.formviewer.setNGValue('CardDetails_Islamic_mandatory','N');
					}
				}
			}
		}
		else if(id.indexOf('CC_Waiver')>-1){
			
			var table = document.getElementById('mytab');
			for(var i=1;i<table.rows.length;i++){
			var cc_waiv = document.getElementById('CC_Waiver_'+i).checked;
				if(window.parent.com.newgen.omniforms.formviewer.getNGValue('cmplx_CardDetails_SuppCardReq')=='Yes' && cc_waiv){
					window.parent.com.newgen.omniforms.util.showError(document.getElementById('cmplx_CardDetails_SuppCardReq'),'Cannot select CC Waiver if Supplement Card required is Yes!!');
					document.getElementById('CC_Waiver_'+i).checked=false;
				}		
				else if(cc_waiv){
					cc_waiver_flag =true;
				}
				else{
					cc_waiver_flag =false;
			}	
			}
			if(cc_waiver_flag){
				window.parent.com.newgen.omniforms.formviewer.setVisible('CardDetails_Label7',false);
				window.parent.com.newgen.omniforms.formviewer.setVisible('cmplx_CardDetails_statCycle',false);
				window.parent.com.newgen.omniforms.formviewer.setEnabled('cmplx_CardDetails_SuppCardReq',false);
				window.parent.com.newgen.omniforms.formviewer.setLocked('CardDetails_Frame1',true);//added by akshay on 27/12/17
				
			}
			else{
				window.parent.com.newgen.omniforms.formviewer.setVisible('CardDetails_Label7',true);
				window.parent.com.newgen.omniforms.formviewer.setVisible('cmplx_CardDetails_statCycle',true);
				window.parent.com.newgen.omniforms.formviewer.setEnabled('cmplx_CardDetails_SuppCardReq',true);
				window.parent.com.newgen.omniforms.formviewer.setLocked('CardDetails_Frame1',false);//added by akshay on 27/12/17

			}
		}
	}
	else{
			//alert('Please expand Card Details');
			window.parent.com.newgen.omniforms.util.showError(document.getElementById('CardDetails_Frame1'),'Please expand Card Details first');
			document.getElementById(id).checked = false;
		}

}
function setVisibility(value){
	window.parent.com.newgen.omniforms.formviewer.setVisible('CardDetails_Label2',value);
	window.parent.com.newgen.omniforms.formviewer.setVisible('cmplx_CardDetails_CharityOrg',value);
	window.parent.com.newgen.omniforms.formviewer.setVisible('CardDetails_Label4',value);
	window.parent.com.newgen.omniforms.formviewer.setVisible('cmplx_CardDetails_CharityAmount',value);
	if(value){
	window.parent.com.newgen.omniforms.formviewer.setLeft("CardDetails_Label5", '1059px');
	window.parent.com.newgen.omniforms.formviewer.setLeft("cmplx_CardDetails_SuppCardReq", '1059px');
	}
	else{
	window.parent.com.newgen.omniforms.formviewer.setLeft("CardDetails_Label5", '552px');
	window.parent.com.newgen.omniforms.formviewer.setLeft("cmplx_CardDetails_SuppCardReq", '552px');
	}
}

function setKalyanVisibility(value){
	window.parent.com.newgen.omniforms.formviewer.setVisible('CardDetails_Label3',value);
	window.parent.com.newgen.omniforms.formviewer.setVisible('cmplx_CardDetails_CompanyEmbossingName',value);
}

function CallAjax(jspName,params)
{
	var response="";
	try
	{			
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
							alert("Error in Eligibility_Card_Limit. Params:"+params+". Please Contact Administrator");
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