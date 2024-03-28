<!------------------------------------------------------------------------------------------------------
//           NEWGEN SOFTWARE TECHNOLOGIES LIMITED

//Group						 : Application ?Projects
//Product / Project			 : RAKBank-RLOS
//File Name					 : External Liability
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
<%@ page import="com.newgen.omni.jts.cmgr.XMLParser"%>
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
	var winame= window.parent.pid;
	var application_takeover ="N";
	var activityName = winame.substring(0,2);
	var ws_name = window.parent.activityName;
	
	if(activityName=='CA'){
			if(window.parent.document.getElementById('Application_Type').value =='TKOE' || window.parent.document.getElementById('Application_Type').value =='TKON'){
				application_takeover ="Y";
			}
	}
	
	 
	
	
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
	var table_name="NG_RLOS_CUSTEXPOSE_ServicesDetails";
	var row_id_arr=[];
	var data_save_var="";
	var closedOrActive=[];
	  
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
		
		for(var row_arr_count= 1;row_arr_count<=totalrowid;row_arr_count++){
				var value = "";
				var table_name = "datatablename_"+row_arr_count;
				stable = document.getElementById(table_name).value;
				var Provider_Number  = "Provider_Number_"+row_arr_count;
				//added by Akshay on 12/9/2017 for saving CAC_Ind on form
				var CAC_Indicator  = "CAC_Indicator_"+row_arr_count;
				if(document.getElementById(CAC_Indicator).checked=='true'){
					console.log(ocument.getElementById(CAC_Indicator).checked);
					window.parent.document.getElementById('Liability_New_IS_CAC').value ='Y';
				}
				//ended by Akshay on 12/9/2017 for saving CAC_Ind on form
				if(activityName=="CC" || activityName=="PL" || activityName=="Cr"){
				var Provider_Number_val = document.getElementById(Provider_Number).innerHTML;
				}
				else{
					var Provider_Number_val = document.getElementById(Provider_Number).value;
					}
				if(stable=="ng_rlos_cust_extexpo_loanDetails"){
				if(activityName != 'CA'){
					data_save_var="Take_Over_Indicator,QC_EMI,QC_Amt,Take_Amount,Consider_For_Obligations";
					swhere = "Child_Wi = '"+winame+"' and AgreementId = '"+Provider_Number_val+"'";
				}
					//start temporary change done for saving data in respective tables on 30/8/17
					else{
					data_save_var="Take_Over_Indicator,Take_Amount,Consider_For_Obligations";
					swhere = "Wi_Name = '"+winame+"' and AgreementId = '"+Provider_Number_val+"'";
					}
					
				}
				else if(stable=="ng_rlos_cust_extexpo_CardDetails"){
				if(activityName != 'CA'){
					data_save_var="CAC_Indicator,QC_Amt,QC_EMI,Consider_For_Obligations,Take_Over_Indicator";
						swhere = "Child_Wi = '"+winame+"' and CardEmbossNum = '"+Provider_Number_val+"'";
						}
						else{
						data_save_var="CAC_Indicator,QC_Amt,QC_EMI,Consider_For_Obligations,Take_Over_Indicator";
						swhere = "Wi_Name = '"+winame+"' and CardEmbossNum = '"+Provider_Number_val+"'";
						}
				}
				//end temporary change done for saving data in respective tables on 30/8/17	
				
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
				alert('Liabilities Data saved!!');
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
        window.moveTo(0, 0);
        window.resizeTo(screen.availWidth, screen.availHeight);
		fetchValidQueueData();	  
		fetchGridData();
		fetchCardGridData();
		disable_postload();
		colorClosedLiab();
    }
	
	function colorClosedLiab(){
		if(closedOrActive.length>0){
			for(var i=0;i<closedOrActive.length;i++){
				if(closedOrActive[i]=='Closed'){
				document.getElementById('row_'+(i+1)).style.backgroundColor = "DarkKhaki";
				}
			}
		}
	}
	
	function disable_postload(){
		var toal_row_count= $('table#mytab tr:last').index() + 1;
		
		if((activityName=="CC" || activityName=="PL" ) && ws_name.indexOf('CAD_Analyst1')<0){
			for (var i=1;i<toal_row_count;i++){
				var Take_Over_Indicator = "Take_Over_Indicator_"+i;
				var CAC_Indicator = "CAC_Indicator_"+i;
				var QC_Amt = "QC_Amt_"+i;
				var Consider_For_Obligations = "Consider_For_Obligations_"+i;
				var Take_Amount = "Take_Amount_"+i;
				var QC_EMI = "QC_EMI_"+i;
				var FinalcleanfundedAmt = "FinalcleanfundedAmt_"+i;
					document.getElementById(Take_Over_Indicator).disabled = true;
					document.getElementById(CAC_Indicator).disabled = true;
					document.getElementById(QC_Amt).disabled = true;
					document.getElementById(Consider_For_Obligations).disabled = true;
					document.getElementById(Take_Amount).disabled = true;
					document.getElementById(QC_EMI).disabled = true;
					document.getElementById(FinalcleanfundedAmt).disabled = true;
			}
		}
		else if(activityName=="CA"){
			for (var i=1;i<toal_row_count;i++){
				var table_name = "datatablename_"+i;
				if(application_takeover=="Y" && document.getElementById(table_name).value=="ng_rlos_cust_extexpo_loanDetails"){
						var Take_Over_Indicator = "Take_Over_Indicator_"+i;
						document.getElementById(Take_Over_Indicator).disabled = false;
				}
				
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
	jspName="CustomSelect.jsp";
	var params="";
	var result="";
	
	if(activityName=='CA'){
			sQry="select 'ng_rlos_cust_extexpo_loanDetails' as table_name, LoanType,Liability_type, agreementid,totalamt,Paymentsamt,Take_Over_Indicator,Take_Amount,'','','',OutstandingAmt,Consider_For_Obligations,'',concat((select description from ng_master_Aecb_Codes where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'',loanstat from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Wi_Name = '"+winame+"' and LoanStat != 'Pipeline'";
	}
	else if(activityName=='Cr' || activityName=='CC'){
		sQry="select 'ng_rlos_cust_extexpo_loanDetails' as table_name, LoanType, agreementid,Liability_type,totalamt,Paymentsamt,Take_Over_Indicator,Take_Amount,'','','',OutstandingAmt,'','',concat((select description from ng_master_Aecb_Codes where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'','','',loanstat from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Child_Wi = '"+winame+"' and LoanStat != 'Pipeline' ";
	}
	else{
		sQry="select 'ng_rlos_cust_extexpo_loanDetails' as table_name, LoanType, agreementid,Liability_type,totalamt,Paymentsamt,Take_Over_Indicator,Take_Amount,'','','',OutstandingAmt,'','',concat((select description from ng_master_Aecb_Codes where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'','',loanstat from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Child_Wi = '"+winame+"' and LoanStat != 'Pipeline' ";
} 
			params="Query="+sQry;
			
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
					var nextrowid= $('table#mytab tr:last').index() + 1;
						row_id_arr.push(nextrowid);
					var markup = tb_row(nextrowid,result_arr[i]);
					$("table#mytab tbody").append(markup);	
					}
				}
			}
}


function fetchCardGridData()
{
	jspName="CustomSelect.jsp";
	var params="";
	var result="";
	var sQry="";
	if(activityName=='CA'){//rlos
		sQry="select 'ng_rlos_cust_extexpo_CardDetails' as table_name,CardType,Liability_type,CardEmbossNum,totalamount,PaymentsAmount as EMI,Take_Over_Indicator,'',CAC_Indicator,QC_Amt,QC_EMI,CurrentBalance,Consider_For_Obligations,'',concat((select description from ng_master_Aecb_Codes where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'',cardstatus from ng_rlos_cust_extexpo_cardDetails with (nolock) where wi_name='"+winame+"' and CardStatus != 'Pipeline'";

	}
	else if(activityName=='Cr'|| activityName=='CC'){//cc
		sQry="select 'ng_rlos_cust_extexpo_CardDetails' as table_name,CardType,CardEmbossNum,'',totalamount,PaymentsAmount as EMI,'','',CAC_Indicator,QC_Amt,QC_EMI,CurrentBalance,Consider_For_Obligations,'',concat((select description from ng_master_Aecb_Codes where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'','','',cardstatus from ng_rlos_cust_extexpo_cardDetails with (nolock) where Child_wi='"+winame+"' and CardStatus != 'Pipeline'";

	}else{//PL
		sQry="select 'ng_rlos_cust_extexpo_CardDetails' as table_name,CardType,CardEmbossNum,Liability_type,totalamount,PaymentsAmount as EMI,Take_Over_Indicator,'',CAC_Indicator,QC_Amt,QC_EMI,CurrentBalance,ISNULL(Consider_For_Obligations,''),'',concat((select description from ng_master_Aecb_Codes where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'','','',cardstatus from ng_rlos_cust_extexpo_cardDetails with (nolock) where Child_wi='"+winame+"' and CardStatus != 'Pipeline'";

	}
	
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
				if(result.length>6)
					var result_check = result.substring(0,6);
				if(result_check=='NODATA'){
					
				}
				else{
					var result_arr = result.split("~");
					for(var i=0;i<result_arr.length-1;i++){
					var nextrowid= $('table#mytab tr:last').index() + 1;
						row_id_arr.push(nextrowid);
					var markup = tb_row(nextrowid,result_arr[i]);
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
			//if(ev=='R')
			//{
			//result=CallAjax(jspName,params);
			//}
				 var dhtml="";
			  	 dhtml+="<div style='overflow-x:scroll;overflow-y:scroll;'>"+
				"<table id='mytab' border='0' bordercolor='#337ab7' max-height='500px'>";
			if(activityName == 'CC'){
			activityName = 'Cr';
			}
			var sQry="select col_name ,col_type,max_len,col_id,default_value,is_readonly,ActivityName from NG_Dynamic_Grid_config with (nolock) WHERE Grid_name = 'External_Liability' and ActivityName='"+activityName+"'ORDER BY col_seq";
			//alert(sQry);
			params="Query="+sQry;
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
			WiDetails=result;
					
			dhtml+=tb_header();
		}
				dhtml+="</table></div>";
				dhtml+="<div style='width: 100%; border: 0px #e01414  solid; padding:0px; margin: 0px'><table border='0' borderColor='#e01414' overflow-x:scroll;overflow-y:scroll>";
			if 	(activityName=='CA'){
			dhtml+=/*"<image src='addImage.png' height='15px' width='15px' id='addimage1"+"' style='padding-right:10px' onclick='addClick(this)'/>*/"<input type='button' value='save data' style='background-color:#e01414; color:#ffffff' onclick='saveDate()'/></div></table>";
			
		}
		else{
			if (ws_name=='CAD_Analyst1'){	
			 dhtml+=/*"<image src='addImage.png' height='15px' width='15px' id='addimage1"+"' style='padding-right:10px' onclick='addClick(this)'/>*/"<input type='button' value='save data' style='background-color:#e01414; color:#ffffff' onclick='saveDate()'/></div></table>";
			}
			else{
			 dhtml+=/*"<image src='addImage.png' height='15px' width='15px' id='addimage1"+"' style='padding-right:10px' onclick='addClick(this)'/>*/"<input type='button' value='save data' style='background-color:#848786; color:#ffffff' disabled/></div></table>";
			}
		}
	document.getElementById("mainDivContent").innerHTML=dhtml;
}

function tb_header(){
	var row_arr = WiDetails.split("~");
	var col_arr;
	var temp = "";
	var table_header ="<th><input type='hidden' id='table_name' name = 'table_name'></th>";
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
	var temp = "";
	InputField_id="WI_Name";
	if(val==""){
		var table_row ="<td><input type='hidden' id ='datatablename_"+row_num+"' name='datatablename' value='"+val_new[0]+"' ></td>";
		for(var i=0;i<row_arr.length-1;i++){
			temp = row_arr[i];
			col_id_arr=temp.split("#");
			InputField_id = InputField_id+","+col_id_arr[3];
			if(col_id_arr[1]=="text"){
				table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+col_id_arr[4]+"' maxlength ='"+col_id_arr[2]+"'"+col_id_arr[5]+">"+"</td>";
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
				table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+col_id_arr[4]+" "+col_id_arr[5]+">"+"</td>";
			}
			else if(col_id_arr[1]=="link"){
				//table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' value='1000' onclick='fun()'"+"</td>";
				table_row = table_row +"<td>"+"<a id='"+col_id_arr[3]+"_"+row_num+"' href='#' onclick='openPopup();return false;'>"+col_id_arr[4]+"</a>"+"</td>";
			}
		}
	}
	else{
		var val_new = val.split("#");
		closedOrActive.push(val_new[val_new.length-2]);
		var table_row ="<td><input type='hidden' id ='datatablename_"+row_num+"' name='datatablename' value='"+val_new[0]+"' ></td>";
		for(var i=0;i<row_arr.length-1;i++){
			temp = row_arr[i];
			col_id_arr=temp.split("#");
			InputField_id = InputField_id+","+col_id_arr[3];
			/*if(col_id_arr[1]=="text" && (col_id_arr[3] == "QC_Amt" || col_id_arr[3] == "QC_EMI")){
				table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = 'number' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+val_new[i]+"' maxlength ='"+col_id_arr[2]+"'"+col_id_arr[5]+"onchange='limitDecimal(this.id)'>"+"</td>";
			}*/
			if(col_id_arr[1]=="text" && col_id_arr[3] == "Take_Amount"){
				table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+val_new[i+1]+"' disabled = 'disabled'  title= '"+val_new[i+1]+"' maxlength ='"+col_id_arr[2]+"'"+col_id_arr[5]+">"+"</td>";
			}
			else if(col_id_arr[1]=="text"){
				if((col_id_arr[3]=="QC_Amt" || col_id_arr[3]=="QC_EMI") && val_new[0].indexOf('loan') >-1){
					table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+val_new[i+1]+"'  maxlength ='"+col_id_arr[2]+"'"+col_id_arr[5]+" disabled='disabled' title= '"+val_new[i+1]+"'>"+"</td>";
				}
				else{
					table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+val_new[i+1]+"' onkeypress='validateNum(this.id)' onchange ='limitDecimal(this.id);'  title= '"+val_new[i+1]+"' maxlength ='"+col_id_arr[2]+"'"+col_id_arr[5]+">"+"</td>";
				}
			
			}
			else if(col_id_arr[1]=="select"){
				table_row = table_row +"<td>"+"<select id = '"+col_id_arr[3]+"_"+row_num+"' name = '"+col_id_arr[3]+"_"+row_num+"'>";
				var default_val = col_id_arr[4].split(",");
				for(var x=0;x<default_val.length;x++){
					table_row = table_row+"<option value='"+default_val[x]+"' "+col_id_arr[5]+">"+default_val[x]+"</option>"
				}	
				table_row = table_row+col_id_arr[5]+"</select></td>";
			}
			else if(col_id_arr[1]=="checkbox" && col_id_arr[3] == "Take_Over_Indicator"){
				
				if(val_new[i+1] == 'true'){
					table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+col_id_arr[4]+" "+col_id_arr[5]+" onclick='enableTakeoverAmount(this.id);' checked>"+"</td>";
				}
				else{
					table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+col_id_arr[4]+" "+col_id_arr[5]+" onclick='enableTakeoverAmount(this.id);' disabled = 'disabled' >"+"</td>";
				}
				
			}
			else if(col_id_arr[1]=="checkbox" && col_id_arr[3] == "CAC_Indicator"){		
				if(val_new[0].indexOf('loan')>-1){
					table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+col_id_arr[4]+" "+col_id_arr[5]+" disabled= 'disabled'>"+"</td>";
				}
				else{
					// Changes Done by Aman to show the checked at front end for cacIndicator
					if (val_new[i+1]=="true"){
						table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+col_id_arr[4]+" "+col_id_arr[5]+" onclick='setCAC_Value(this.id);' checked  >"+"</td>";
						}
					else {
						table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+col_id_arr[4]+" "+col_id_arr[5]+" onclick='setCAC_Value(this.id);'>"+"</td>";
					}					// Changes Done by Aman to show the checked at front end for cacIndicator end	
				}
			}
			
			else if(col_id_arr[1]=="checkbox"){
				var value = val_new[i+1];
				if((value == "true" || value == "") && col_id_arr[3]=="Consider_For_Obligations"){
				table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+col_id_arr[4]+" "+col_id_arr[5]+" >"+"</td>";
				}
				else if(value == "true"){
				table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+col_id_arr[4]+" "+col_id_arr[5]+" checked >"+"</td>";
				}
				else if(value=="false" && col_id_arr[3]=="Consider_For_Obligations"){
					table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+" "+col_id_arr[5]+">"+"</td>";
				}
				else if(value=="false" || value==""){
					table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+" "+col_id_arr[5]+">"+"</td>";
				}
			}
			else if(col_id_arr[1]=="link"){
				table_row = table_row +"<td>"+"<a id='"+col_id_arr[3]+"_"+row_num+"' href='#' onclick='openPopup(this.id);'>"+val_new[i+1]+"</a>"+"</td>";
			}
			
		}
	}
	
	return "<tr background-color:#e01414 class='HeaderClass' id = row_"+row_num+" ><p align='right'>"+table_row+"</tr>";
}

function enableTakeoverAmount(id){
	var idArr = id.split('_');
	var rowNumber = idArr[idArr.length - 1];//Take_Over_Indicator_no
	if(document.getElementById('Take_Amount_'+rowNumber).disabled){
	document.getElementById('Take_Amount_'+rowNumber).disabled = false;
	document.getElementById('Take_Amount_'+rowNumber).value = document.getElementById('Outstanding_Amount_'+rowNumber).value;
	}
	else{
	document.getElementById('Take_Amount_'+rowNumber).disabled = true;
	document.getElementById('Take_Amount_'+rowNumber).value = "";
	}

}

function setCAC_Value(id){

	//console.log(window.parent.document.getElementById('Liability_New_IS_CAC').value);
	//window.parent.document.getElementById('Liability_New_IS_CAC').value += document.getElementById(id).checked;

	
//}	
}
function validateNum(Id){
	if(Id.indexOf('QC_Amt')>-1 || Id.indexOf('QC_EMI')>-1){
	
		var value = document.getElementById(Id).value;
		for(var i=0;i<value.length;i++){
			if( value.charAt(i)!='.' && isNaN(parseFloat(value.charAt(i)))){
				document.getElementById(Id).value="";
			}
		}
	
	}

}
function limitDecimal(Id){
	if(Id.indexOf('QC_Amt')>-1 || Id.indexOf('QC_EMI')>-1){
		var value = document.getElementById(Id).value;
		if(isNaN(parseFloat(value))){
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

function openPopup(id)
	{
	
	var agreementval=document.getElementById(id).innerHTML;
	var arr = id.split('_');
	var rownum = arr[arr.length-1];
	var tablename = document.getElementById('datatablename_'+rownum).value;
	
	var url='/webdesktop/resources/scripts/external.jsp?Wi_Name='+winame+'&AgreementId='+agreementval+'&table='+tablename;
	window.showModalDialog(url, "", "dialogWidth:1000px;dialogHeight:800px;resizable:yes");
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




function openworkitems(pid,wid,sessionID,userID,userName,cabinetName,isBatchWI)
	{
	  var url ="";                         


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
			
			xmlReq.open('POST',jspName,false);				
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

function ReadPublicDataEx(sRefresh, sReadPhotography, sReadNonModifiableData, sReadModifiableData, sSignatureValidation,
		sReadV2Fields, sReadSignatureImage, sReadAddress)
{
	if(EIDAWebComponent == null)
	{
		alert("The Webcomponent is not initialized.");
		return;
	}
	var Ret = EIDAWebComponent.ReadPublicDataEx(sRefresh, sReadPhotography, sReadNonModifiableData, sReadModifiableData, sSignatureValidation,
			sReadV2Fields, sReadSignatureImage, sReadAddress);
	return Ret;
}
function Initialize() 
{
	try
	{		
		EIDAWebComponent = document.getElementById(EIDAWebComponentName);
		var Ret = EIDAWebComponent.Initialize();
		return Ret;
	}
	catch(e)
	{
		return "Webcomponent Initialization Failed, Details: "+e;
	}
}
function fetchEID()
{

	if(EIDAWebComponent == null)
	{
		alert("The Webcomponent is not initialized.");
		return;
	}
	return EIDAWebComponent.GetIDNumber();
}
function DisplayPublicDataEx()
{
	
	if(EIDAWebComponent == null)
	{
		alert("The Webcomponent is not initialized.");
		return;
	}
	
	var Ret = ReadPublicDataEx("true", "false", "true", "true", "false", "true", "true", "true");

}
function callEIDA() {
    Initialize();
    DisplayPublicDataEx();
    document.getElementById("emirates_id_val").value = fetchEID();
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
