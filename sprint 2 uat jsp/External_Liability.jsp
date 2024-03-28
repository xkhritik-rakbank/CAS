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
	var winame= window.parent.workitemName;
	var application_takeover ="N";
	var activityName = winame.substring(0,2);
	var ws_name = window.parent.activityName;
	var enabledSteps = ['Branch_Init','CSM','Telesales_Init','Fulfillment_RM','Telesales_RM','CAD_Analyst1'];
	var pname="EL";	
	if(activityName=='CA'){
			if(window.parent.com.newgen.omniforms.formviewer.getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4) =='TKOE' || window.parent.com.newgen.omniforms.formviewer.getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4) =='TKON'){
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
	var closedOrActive={};
	var stopExecution=false;
	  
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
				var avg_utilization  = "avg_utilization_"+row_arr_count;
				//changed by nikhil PCSP-883
				var provider_value="";
				if(document.getElementById(CAC_Indicator).checked=='true'){
					console.log(document.getElementById(CAC_Indicator).checked);
					window.parent.document.getElementById('Liability_New_IS_CAC').value ='Y';
				}
				//changed by nikhil for PCSP-883 modified
				if(document.getElementById(CAC_Indicator).checked==true && document.getElementById(avg_utilization).value==''  )
				{
				//change by saurabh for PCSP-123
				//below code for PCSP-683
				//changed by nikhil PCSP-883
				if(activityName=='CA' )
				{
					provider_value=document.getElementById(Provider_Number).value;
				}
				else
				{
					provider_value=document.getElementById(Provider_Number).innerHTML;
				}
					window.parent.showAlert('ExtLiability_IFrame_external','Please fill Average utilization in External Liabilities section with Provider Number :'+provider_value);
					window.parent.document.getElementById('stopexecution').value="stop";
					return false;
					stopExecution=true;
					
				}
				//ended by Akshay on 12/9/2017 for saving CAC_Ind on form
				if(activityName=="CC" || activityName=="PL" || activityName=="Cr"){
				var Provider_Number_val = document.getElementById(Provider_Number).innerHTML;
				}
				else{
					var Provider_Number_val = document.getElementById(Provider_Number).value;
					}
				if(stable=="ng_rlos_cust_extexpo_loanDetails"){
				if(activityName != 'CA' && activityName != 'LE'){
					data_save_var="Take_Over_Indicator,QC_EMI,QC_Amt,Take_Amount,Consider_For_Obligations,Settlement_Flag,Remarks,FinalcleanfundedAmt";
					swhere = "Child_Wi = '"+winame+"' and AgreementId = '"+Provider_Number_val+"'";
				}
					//start temporary change done for saving data in respective tables on 30/8/17
					else{
					data_save_var="Take_Over_Indicator,Take_Amount,Consider_For_Obligations,Settlement_Flag,Remarks";
					swhere = "Wi_Name = '"+winame+"' and AgreementId = '"+Provider_Number_val+"'";
					}
					
				}
				else if(stable=="ng_rlos_cust_extexpo_CardDetails"){
				if(activityName != 'CA' && activityName != 'LE'){
				//changes done for PCSP-122
					data_save_var="CAC_Indicator,QC_Amt,QC_EMI,Consider_For_Obligations,Take_Over_Indicator,Settlement_Flag,Remarks,FinalcleanfundedAmt,avg_utilization";
						swhere = "Child_Wi = '"+winame+"' and CardEmbossNum = '"+Provider_Number_val+"'";
						}
						else{
						data_save_var="CAC_Indicator,QC_Amt,QC_EMI,Consider_For_Obligations,Take_Over_Indicator,Settlement_Flag,Remarks,avg_utilization";
						swhere = "Wi_Name = '"+winame+"' and CardEmbossNum = '"+Provider_Number_val+"'";
						}
				}
				
				else if(stable=="ng_rlos_cust_extexpo_AccountDetails"){
				if(activityName != 'CA'){
					data_save_var="CAC_Indicator,QC_Amt,QC_EMI,Consider_For_Obligations,Take_Over_Indicator,Settlement_Flag";
						swhere = "Child_Wi = '"+winame+"' and AcctId = '"+Provider_Number_val+"'";
						}
						else{
						data_save_var="CAC_Indicator,QC_Amt,QC_EMI,Consider_For_Obligations,Take_Over_Indicator,Settlement_Flag";
						swhere = "Wi_Name = '"+winame+"' and AcctId = '"+Provider_Number_val+"'";
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
		var hiddenFlag=document.getElementById('hiddenFlag').value;
		var update_flag=0;
		for(var j=0 ; j<update_result_arr.length ; j++){
			if(update_result_arr[j].indexOf('ERROR')>-1){
				update_flag=1;
				break;
			}	
		}
			if(update_flag==0 && hiddenFlag!='true'){
				alert('Liabilities Data saved!!');
			}	
			document.getElementById('hiddenFlag').value='';
			
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
		fetchCardGridData();
		fetchODData();
		disable_postload();
		colorClosedLiab();
		disableCACIndicatorsonLoad();
		disableSettlementforCC();
		//added by nikhil 
		if(ws_name=="CSM")
		{
			hideatcsm();
		}
		//added by akshay on 9/1/17
		try{
		window.parent.style.top="100px";
		}
		catch(ex){}
    }
	
	function disableSettlementforCC(){
	var toal_row_count= $('table#mytab tr:last').index() + 1;
		
		if((activityName=="CC" )|| ((activityName=="CA" )&&(window.parent.com.newgen.omniforms.formviewer.getLVWAT('cmplx_Product_cmplx_ProductGrid',0,1)=='Credit Card' ))){
		for (var i=1;i<toal_row_count;i++){
				var Settlement_Flag = "Settlement_Flag_"+i;
					document.getElementById(Settlement_Flag).disabled = true;
			}
		}
	
	}
	
	function colorClosedLiab(){
		if(Object.keys(closedOrActive).length>0){
			var table = document.getElementById("mytab");
			var row;
			for(var key in closedOrActive){
					if(closedOrActive[key].indexOf('Corporate_CIF')>-1){
					document.getElementById('row_'+key).style.backgroundColor = "DarkKhaki";
					setDesiredColorinCells(table,key,'DarkKhaki');
				}
			}
		swaprow(table);
				
			
		}
	}
	function disableCACIndicatorsonLoad(){
		var disableAllCAC = false;
		var checkedRownum;
		var table = document.getElementById("mytab");
		for(var i=0;i<table.rows.length;i++){
			for(var j=0;j<table.rows[i].cells.length;j++){
				if(table.rows[i].cells[j].childNodes[0].id.indexOf('CAC_Indicator')>-1 && document.getElementById('CAC_Indicator_'+i).checked){
					disableAllCAC=true;
					//change by saurabh for PCSP-123
					document.getElementById('avg_utilization_'+i).disabled=false;
					//changed for PCSP-883 28th 
					document.getElementById('avg_utilization_'+i).readOnly=false;
					checkedRownum = i;
					break;
				}
			}
		}
		if(disableAllCAC){
			CAC_Check('CAC_Indicator_'+checkedRownum);
		}
	}
	/*function swaprow(table,color,count){

	var row = table.rows[color[0]-count];
    var row1 = table.insertRow(table.rows.length);
    row1.innerHTML = row.innerHTML;
    
    table.deleteRow(color[0]-count);
    count++;
    color.shift();
    return count;
}*/
function swaprow(table){
	var count=0;
	for(var key in closedOrActive){
		if(closedOrActive[key].indexOf(',Closed')>-1){
		var row = table.rows[key-count];
		var row1 = table.insertRow(table.rows.length);
		row1.innerHTML = row.innerHTML;
		row1.style.backgroundColor = row.style.backgroundColor;
		//change by saurabh on 13th Dec
		if(closedOrActive[key].indexOf('Individual_CIF')>-1 || closedOrActive[key].indexOf(',')==0){
			setDesiredColorinCells(table,(table.rows.length-1),'Silver');
		}
		table.deleteRow(key-count);
		count++;
		//document.getElementById('row_'+(table.rows.length-1)).style.backgroundColor = "DarkKhaki";
		}
	}
}

function setDesiredColorinCells(table,key,color){
	for(var i =0;i<table.rows[key].cells.length;i++){
		if(table.rows[key].cells[i].children.length>0){
			if(table.rows[key].cells[i].children[0].type == 'text'){
				table.rows[key].cells[i].children[0].style.backgroundColor=color;
				//change by saurabh on 13th Dec
				if(color=='Silver'){
				table.rows[key].cells[i].childNodes[0].disabled = true;
				}
			}
			//change by saurabh on 13th Dec
				if(color=='Silver' && table.rows[key].cells[i].childNodes[0].id.indexOf('Provider_Number')==-1){
				table.rows[key].cells[i].childNodes[0].disabled = true;
				}
		}
	}
}

	
	function disable_postload(){
		var toal_row_count= $('table#mytab tr:last').index() + 1;
		
		if((activityName=="CC" || activityName=="Cr" || activityName=="PL" ) && enabledSteps.indexOf(ws_name)<0){
			for (var i=1;i<toal_row_count;i++){
				var Take_Over_Indicator = "Take_Over_Indicator_"+i;
				var CAC_Indicator = "CAC_Indicator_"+i;
				var QC_Amt = "QC_Amt_"+i;
				var Consider_For_Obligations = "Consider_For_Obligations_"+i;
				var Take_Amount = "Take_Amount_"+i;
				var QC_EMI = "QC_EMI_"+i;
				var FinalcleanfundedAmt = "FinalcleanfundedAmt_"+i;
				var CleanFunded = "CleanFunded_"+i;
				var cleanfundedAmt = "cleanfundedAmt_"+i;
				var Settlement_Flag = "Settlement_Flag_"+i;
				var Remarks='Remarks_'+i;
					document.getElementById(Take_Over_Indicator).disabled = true;
					document.getElementById(CAC_Indicator).disabled = true;
					document.getElementById(QC_Amt).disabled = true;
					document.getElementById(Consider_For_Obligations).disabled = true;
					document.getElementById(Take_Amount).disabled = true;
					document.getElementById(QC_EMI).disabled = true;
					document.getElementById(FinalcleanfundedAmt).disabled = true;
					//document.getElementById(cleanfundedAmt).disabled = true;
					document.getElementById(CleanFunded).disabled = true;
					document.getElementById(Settlement_Flag).disabled = true;
					document.getElementById(Remarks).disabled = true;
			}
		}
		else if(activityName=="CA"){
			for (var i=1;i<toal_row_count;i++){
				var table_name = "datatablename_"+i;
				if(application_takeover=="Y" && (document.getElementById(table_name).value=="ng_rlos_cust_extexpo_loanDetails" || document.getElementById(table_name).value=="ng_rlos_cust_extexpo_CardDetails" )){
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
	var wparams="";
	
	if(activityName=='CA'){
		sQry='CA';
			/* sQry="select 'ng_rlos_cust_extexpo_loanDetails' as table_name, LoanType,Liability_type, agreementid,totalamt,Paymentsamt,Take_Over_Indicator,Take_Amount,'','','',OutstandingAmt,Consider_For_Obligations,'',concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'','',MonthsOnBook from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Wi_Name =:winame and LoanStat not in  ('Pipeline','Closed') order by Liability_type desc"; */
			wparams="Wi_Name=="+winame;
	}
	else if(activityName=='Cr'){
		//sQry="select 'ng_rlos_cust_extexpo_loanDetails' as table_name, LoanType, agreementid,Liability_type,totalamt,Paymentsamt,Take_Over_Indicator,Take_Amount,'','','','',OutstandingAmt,Consider_For_Obligations,remarks,concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'','',MonthsOnBook,Settlement_Flag,loanstat from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Child_Wi = '"+winame+"' and LoanStat != 'Pipeline'  and ProviderNo != 'B01' order by Liability_type desc ";
		sQry='Cr';
		/* sQry="select 'ng_rlos_cust_extexpo_loanDetails' as table_name, LoanType, agreementid,Liability_type,totalamt,Paymentsamt,Take_Over_Indicator,Take_Amount,'','','',OutstandingAmt,'','',concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'','',MonthsOnBook,loanstat from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Child_Wi =:Child_Wi and LoanStat != 'Pipeline' order by Liability_type desc "; */
		wparams="Child_Wi=="+winame;
	}
	else{
	if(ws_name!='CSM'){
		sQry="PL";
		//sQry="select 'ng_rlos_cust_extexpo_loanDetails' as table_name, LoanType, agreementid,Liability_type,totalamt,Take_Over_Indicator,Paymentsamt,Take_Amount,'','','',OutstandingAmt,Consider_For_Obligations,remarks,concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'',finalCleanfundedAmt,MonthsOnBook,Settlement_Flag,loanstat from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Child_Wi = '"+winame+"' and LoanStat != 'Pipeline'  and ProviderNo != 'B01' order by Liability_type desc ";
	}
	else{
			sQry="PL_CSM";
		//sQry="select 'ng_rlos_cust_extexpo_loanDetails' as table_name, LoanType, agreementid,Liability_type,totalamt,Take_Over_Indicator,Paymentsamt,Take_Amount,'','','',OutstandingAmt,Consider_For_Obligations,remarks,concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'','',MonthsOnBook,Settlement_Flag,loanstat from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Child_Wi = '"+winame+"' and LoanStat not in  ('Pipeline','Closed') and ProviderNo != 'B01'  order by Liability_type desc ";
	}
		wparams="Child_Wi=="+winame;
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
	var wparams="";
	if(activityName=='CA' || activityName=='LE'){//rlos
		sQry="fetchCA";
		//sQry="select 'ng_rlos_cust_extexpo_CardDetails' as table_name,CardType,Liability_type,CardEmbossNum,totalamount,PaymentsAmount as EMI,Take_Over_Indicator,'',CAC_Indicator,QC_Amt,QC_EMI,avg_utilization,CurrentBalance,Consider_For_Obligations,remarks,concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'',settlement_flag,'',MonthsOnBook from ng_rlos_cust_extexpo_cardDetails with (nolock) where wi_name='"+winame+"' and CardStatus not in  ('Pipeline','Closed') and ProviderNo != 'B01' order by Liability_type desc";
		wparams="wi_name=="+winame;
	}
	else if(activityName=='Cr'){//cc
		sQry="fetchCr";
		//sQry="select 'ng_rlos_cust_extexpo_CardDetails' as table_name,CardType,CardEmbossNum,Liability_type,totalamount,PaymentsAmount as EMI,TakeOverAmount,'',CAC_Indicator,QC_Amt,QC_EMI,avg_utilization,CurrentBalance,Consider_For_Obligations,remarks,concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'','',MonthsOnBook,cardstatus,Settlement_Flag from ng_rlos_cust_extexpo_cardDetails with (nolock) where Child_wi='"+winame+"' and CardStatus != 'Pipeline' and ProviderNo != 'B01'  order by Liability_type desc";
		wparams="Child_wi=="+winame;

	}else{//PL
		if(ws_name!='CSM'){
			sQry="fetchPL";
	//	sQry="select 'ng_rlos_cust_extexpo_CardDetails' as table_name,CardType,CardEmbossNum,Liability_type,totalamount,Take_Over_Indicator,PaymentsAmount as EMI,TakeOverAmount,CAC_Indicator,QC_Amt,QC_EMI,CurrentBalance,ISNULL(Consider_For_Obligations,''),remarks,concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'',finalCleanfundedAmt,MonthsOnBook,cardstatus,Settlement_Flag from ng_rlos_cust_extexpo_cardDetails with (nolock) where Child_wi='"+winame+"' and CardStatus != 'Pipeline' and ProviderNo != 'B01'  order by Liability_type desc";
		wparams="Child_wi=="+winame;
	}
	else{
		sQry="fetchPLE";
		//sQry="select 'ng_rlos_cust_extexpo_CardDetails' as table_name,CardType,CardEmbossNum,Liability_type,totalamount,Take_Over_Indicator,PaymentsAmount as EMI,TakeOverAmount,CAC_Indicator,QC_Amt,QC_EMI,CurrentBalance,ISNULL(Consider_For_Obligations,''),remarks,concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'',Settlement_Flag,MonthsOnBook,cardstatus from ng_rlos_cust_extexpo_cardDetails with (nolock) where Child_wi='"+winame+"' and CardStatus not in  ('Pipeline','Closed') and ProviderNo != 'B01'  order by Liability_type desc";
		wparams="Child_wi=="+winame;
	}
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

function fetchODData()
{//alert('Inside OD'+activityName);
	jspName="CustomSelect.jsp";
	var params="";
	var result="";
	var sQry="";
	var wparams="";
	if(activityName=='CA'|| activityName=='LE'){
		sQry="ODCA";	
			//sQry="select 'ng_rlos_cust_extexpo_AccountDetails' as table_name,'Overdraft' as TypeOfContract,'Individual_CIF' as LiabilityType,AcctId,CreditLimit,PaymentsAmount,'' as TakeOverIndicator,'' as TakeoverAmount,'' as CACAmount,'' as QCAmt,'' as QCEMI,'',OutStandingBalance,Consider_For_Obligations as considerforObligation,'' as remarks,concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'' as cleanfunded,'',MonthsOnBook,Settlement_Flag from ng_rlos_cust_extexpo_AccountDetails with (nolock) where Wi_Name = '"+winame+"' and AcctStat not in  ('Pipeline','Closed')";
			wparams="Wi_Name=="+winame;
	}
	else if(activityName=='Cr' || activityName=='CC'){
		sQry="ODCrCC";
		//sQry="select 'ng_rlos_cust_extexpo_AccountDetails' as table_name,'Overdraft' as TypeOfContract,AcctId,'Individual_CIF' as LiabilityType,CreditLimit,PaymentsAmount,'' as TakeOverIndicator,'' as TakeoverAmount,'' as CACAmount,'' as QCAmt,'' as QCEMI,'',OutStandingBalance,Consider_For_Obligations as considerforObligation,'' as remarks,concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'' as cleanfunded,Settlement_Flag from ng_rlos_cust_extexpo_AccountDetails with (nolock) where Child_Wi = '"+winame+"' and AcctStat != 'Pipeline'  ";
		wparams="Child_Wi=="+winame;
	}
	else{
		sQry="ODPL";
		//sQry="select 'ng_rlos_cust_extexpo_AccountDetails' as table_name,'Overdraft' as TypeOfContract,'Individual_CIF' as LiabilityType,AcctId,CreditLimit,PaymentsAmount,'' as TakeOverIndicator,'' as TakeoverAmount,'' as CACAmount,'' as QCAmt,'' as QCEMI,OutStandingBalance,Consider_For_Obligations as considerforObligation,'' as remarks,concat((select description from ng_master_Aecb_Codes with (nolock) where code = WriteoffStat),'-',WriteoffStatDt) as WriteoffStatDt,'' as cleanfunded,Settlement_Flag from ng_rlos_cust_extexpo_AccountDetails with (nolock) where Child_Wi = '"+winame+"' and AcctStat != 'Pipeline' ";
		wparams="Child_Wi=="+winame;
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
			var wparams="";
			//if(ev=='R')
			//{
			//result=CallAjax(jspName,params);
			//}
				 var dhtml="";
			  	 dhtml+="<div>"+
				"<table id='mytab' border='0' bordercolor='#337ab7' style='overflow-x:scroll;overflow-y:auto;'>";
			
			if(activityName == 'CC'){
			activityName = 'Cr';
			}
			sQry="QueueData";
			//var sQry="select col_name ,col_type,max_len,col_id,default_value,is_readonly,ActivityName from NG_Dynamic_Grid_config with (nolock) WHERE Grid_name = 'External_Liability' and ActivityName='"+activityName+"'ORDER BY col_seq";
			wparams="ActivityName=="+activityName;
			//alert(sQry);
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
			WiDetails=result;
					
			dhtml+=tb_header();
		}
				dhtml+="</table></div>";
				dhtml+="<div style='width: 100%; border: 0px #e01414  solid; padding:0px; margin: 0px'><table border='0' borderColor='#e01414' overflow-x:scroll;overflow-y:scroll>";
			if 	(activityName=='CA'){
			dhtml+=/*"<image src='addImage.png' height='15px' width='15px' id='addimage1"+"' style='padding-right:10px' onclick='addClick(this)'/>*/"<input type='button' value='save data'  id='savedata'  style='background-color:#e01414; color:#ffffff' onclick='saveDate()'/></div></table>";
			
		}
		else{
			if (ws_name=='CAD_Analyst1'){	
			 dhtml+=/*"<image src='addImage.png' height='15px' width='15px' id='addimage1"+"' style='padding-right:10px' onclick='addClick(this)'/>*/"<input type='button'  id='savedata'  value='save data' style='background-color:#e01414; color:#ffffff' onclick='saveDate()'/></div></table>";
			}
			else{
			 dhtml+=/*"<image src='addImage.png' height='15px' width='15px' id='addimage1"+"' style='padding-right:10px' onclick='addClick(this)'/>*/"<input type='button'  id='savedata'  value='save data' style='background-color:#848786; color:#ffffff' disabled/></div></table>";
			}
		}
		dhtml+="<input type='hidden' id='hiddenFlag' hidden>";
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
				//++ below code added by abhishek on 08/11/2017
				if((activityName=="CC" || activityName=="PL" ) && ws_name == "CSM"){
					table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+col_id_arr[4]+"'>"+"</td>";
				}else{
					table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+col_id_arr[4]+"' "+col_id_arr[5]+">"+"</td>";
				}
				//++ Above code added by abhishek on 08/11/2017
			}
			else if(col_id_arr[1]=="link"){
				//table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' value='1000' onclick='fun()'"+"</td>";
				table_row = table_row +"<td>"+"<a id='"+col_id_arr[3]+"_"+row_num+"' href='#' onclick='openPopup();return false;'>"+col_id_arr[4]+"</a>"+"</td>";
			}
		}
	}
	else{
		var val_new = val.split("#");
		if(activityName!='CA'){
		closedOrActive[row_num]= val_new[3]+','+ val_new[val_new.length-2];
		}
		else{
		closedOrActive[row_num]= val_new[3];
		}
		var table_row ="<td><input type='hidden' id ='datatablename_"+row_num+"' name='datatablename' value='"+val_new[0]+"' ></td>";
		for(var i=0;i<row_arr.length-1;i++){
			temp = row_arr[i];
			col_id_arr=temp.split("#");
			InputField_id = InputField_id+","+col_id_arr[3];
			/*if(col_id_arr[1]=="text" && (col_id_arr[3] == "QC_Amt" || col_id_arr[3] == "QC_EMI")){
				table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = 'number' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+val_new[i]+"' maxlength ='"+col_id_arr[2]+"'"+col_id_arr[5]+"onchange='limitDecimal(this.id)'>"+"</td>";
			}*/
			if(ws_name=='CSM' && col_id_arr[3].indexOf('Provider_Number')>-1 && col_id_arr[1]=="link"){
				col_id_arr[1]="text";
			}
			if(col_id_arr[1]=="text" && col_id_arr[3] == "Take_Amount"){
				table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+val_new[i+1]+"' disabled = 'disabled'  title= '"+val_new[i+1]+"' maxlength ='"+col_id_arr[2]+"' onchange ='limitDecimal(this.id);' onkeypress='keypresshandler(event)' "+col_id_arr[5]+">"+"</td>";
			}
			else if(col_id_arr[1]=="text"){
			var app_type = window.parent.com.newgen.omniforms.formviewer.getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4);
				if((col_id_arr[3]=="QC_Amt" || col_id_arr[3]=="QC_EMI") && (val_new[0].indexOf('loan') >-1 ||val_new[0].indexOf('Account') >-1)){
					table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+val_new[i+1]+"'  maxlength ='"+col_id_arr[2]+"'"+col_id_arr[5]+" disabled='disabled' title= '"+val_new[i+1]+"'>"+"</td>";
				}
				
				//added by nikhil to make final clean funded amount to be editable at CA WS
				else if(ws_name=='CAD_Analyst1' && col_id_arr[3]=="FinalcleanfundedAmt" )
				{
				table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+val_new[i+1]+"' onkeypress='validateNum(this.id)' onchange ='limitDecimal(this.id);'  title= '"+val_new[i+1]+"' maxlength ='"+col_id_arr[2]+"'>"+"</td>";
				}
				
				else if((ws_name=='CAD_Analyst1' || ws_name=='CSM') && col_id_arr[3]=="Take_Amount" && activityName=='Cr')
				{
				table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+val_new[i+1]+"' onkeypress='validateNum(this.id)' onchange ='limitDecimal(this.id);'  title= '"+val_new[i+1]+"' maxlength ='"+col_id_arr[2]+"'>"+"</td>";
				}
				else if(ws_name=='CAD_Analyst1' && col_id_arr[3]=="Take_Amount" && activityName=='PL' && (app_type=='TKOE' || app_type=='TKOE'))
				{
				table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+val_new[i+1]+"' onkeypress='validateNum(this.id)' onchange ='limitDecimal(this.id);'  title= '"+val_new[i+1]+"' maxlength ='"+col_id_arr[2]+"'>"+"</td>";
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
			//below commented for PCSP-49/50/51
			//added by akshay on 29/12/17 for closed cards
			/*else if(val_new[val_new.length-2]=='Closed'  && ws_name!='CSM' && ws_name!='CAS_Branch_init'){
				table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+col_id_arr[4]+" disabled >"+"</td>";
					
			}*/
			//ended by akshay 	
			else if(col_id_arr[1]=="checkbox" && col_id_arr[3] == "Take_Over_Indicator"){
				var app_type = window.parent.com.newgen.omniforms.formviewer.getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4);
				
				if(val_new[i+1] == 'true'){
					table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+col_id_arr[4]+" "+col_id_arr[5]+" onclick='enableTakeoverAmount(this.id);' checked>"+"</td>";
				}
				else if((ws_name=='CAD_Analyst1' || ws_name=='CSM')  && activityName=='Cr')
				{
				//change for PCSP-104 by saurabh.
				table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+col_id_arr[4]+" "+col_id_arr[5]+" onclick='enableTakeoverAmount(this.id);' disabled = 'disabled' >"+"</td>";
				}
				else if((ws_name=='CAD_Analyst1' || ws_name=='CSM')  && activityName=='PL' && (app_type=='TKOE' || app_type=='TKOE'))
				{
				table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+col_id_arr[4]+" "+col_id_arr[5]+" onclick='enableTakeoverAmount(this.id);'  >"+"</td>";
				}
				else{
					table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+col_id_arr[4]+" "+col_id_arr[5]+" onclick='enableTakeoverAmount(this.id);' disabled = 'disabled' >"+"</td>";
				}
				
			}
			else if(col_id_arr[1]=="checkbox" && col_id_arr[3] == "CAC_Indicator"){		
				var subproduct = window.parent.com.newgen.omniforms.formviewer.getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2);
		
				if(val_new[0].indexOf('loan')>-1 || subproduct=='IM'||val_new[0].indexOf('Account')>-1 ){
					table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+col_id_arr[4]+" "+col_id_arr[5]+" disabled= 'disabled'>"+"</td>";
				}
				else{
					// Changes Done by Aman to show the checked at front end for cacIndicator
					if (val_new[i+1]=="true"){
						table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+col_id_arr[4]+" "+col_id_arr[5]+" onclick='Enable_QCFields(this.id);CAC_Check(this.id);' checked  >"+"</td>";
						
						}
					else {
						table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+col_id_arr[4]+" "+col_id_arr[5]+" onclick='Enable_QCFields(this.id);CAC_Check(this.id);'>"+"</td>";
						
					}					// Changes Done by Aman to show the checked at front end for cacIndicator end	
				}
			}
			
			else if(col_id_arr[1]=="checkbox"){
				var value = val_new[i+1];
				if((value == "true" || value == ""|| value=="false") && col_id_arr[3]=="Consider_For_Obligations"){
					//change by saurabh for PCSP-760
					if(closedOrActive[row_num].indexOf('Closed')>-1){
						table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+col_id_arr[5]+" onclick='Settlementflag(this.id);'>"+"</td>";
					}
					else{
						if(value == "true"){
						table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+col_id_arr[5]+" onclick='Settlementflag(this.id);' checked >"+"</td>";
						}
						else if(value=="false"){
						table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+" "+col_id_arr[5]+" onclick='Settlementflag(this.id);' >"+"</td>";
						}
						else{
							table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+col_id_arr[4]+" "+col_id_arr[5]+" onclick='Settlementflag(this.id);'>"+"</td>";
						}	
					}	
			  }
			  else if((value == "true" || value == ""|| value=="false") && col_id_arr[3]=="Settlement_Flag"){
				//++ below code added by abhishek on 08/11/2017
				/*if((activityName=="CC" || activityName=="PL" ) && ws_name == "CSM"){
					table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+col_id_arr[4]+" onclick='Settlementflag(this.id);'>"+"</td>";
				}*/
				//++ Above code added by abhishek on 08/11/2017
				//table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+col_id_arr[4]+" "+col_id_arr[5]+" >"+"</td>";
				 if(value == "true"){
				table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+col_id_arr[5]+" onclick='Settlementflag(this.id);' checked >"+"</td>";
				}
				else if(value=="false"){
				table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+" "+col_id_arr[5]+" onclick='Settlementflag(this.id);' >"+"</td>";
				}
				else{
					table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+col_id_arr[4]+" "+col_id_arr[5]+" onclick='Settlementflag(this.id);'>"+"</td>";
				}
			  }
			   //added by  nikhil make clean funded non-editable at ddvt maker proc-8825
			  else if((ws_name=='DDVT_Maker' || ws_name=='DDVT_Checker' || ws_name == "CSM" || ws_name=='CAD_Analyst1') && col_id_arr[3]=="CleanFunded")
			  {
				 table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+col_id_arr[4]+" "+col_id_arr[5]+" onclick='Settlementflag(this.id);' disabled= 'disabled'>"+"</td>"; 
			  }
			  else{
					table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+col_id_arr[4]+" "+col_id_arr[5]+" onclick='Settlementflag(this.id);' >"+"</td>";
				}
			}			
				//++ Above code added by abhishek on 08/11/2017
					//table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+" "+col_id_arr[5]+">"+"</td>";
				
			
			else if(col_id_arr[1]=="link"){
				table_row = table_row +"<td>"+"<a id='"+col_id_arr[3]+"_"+row_num+"' href='#' onclick='openPopup(this.id);'>"+val_new[i+1]+"</a>"+"</td>";
			}
			
		}
	}
	
	return "<tr background-color:#e01414 class='HeaderClass' id = row_"+row_num+" ><p align='right'>"+table_row+"</tr>";
}

function Settlementflag(id){
	if(id.indexOf('Settlement_Flag_')>-1){
	var arr = id.split('_');
	var rownum = arr[arr.length - 1];
		if(document.getElementById(id).checked || document.getElementById(id).checked == 'true'){
			document.getElementById('Consider_For_Obligations_'+rownum).checked = false;
			//document.getElementById('Consider_For_Obligations_'+rownum).disabled = true;--commented by akshay on 29/12/17 for drop 3 change
				}
		else {
			document.getElementById('Consider_For_Obligations_'+rownum).checked = true;
		}	
	}
	
    }

function enableTakeoverAmount(id){
	var idArr = id.split('_');
	var rowNumber = idArr[idArr.length - 1];//Take_Over_Indicator_no
	//var cfostat = document.getElementById('Consider_For_Obligations_'+rowNumber).checked;
	if(document.getElementById(id).checked){
	document.getElementById('Take_Amount_'+rowNumber).disabled = false;
	document.getElementById('Take_Amount_'+rowNumber).value = document.getElementById('Outstanding_Amount_'+rowNumber).value;
	document.getElementById('Consider_For_Obligations_'+rowNumber).checked = false;
	
	}
	else{
	document.getElementById('Take_Amount_'+rowNumber).disabled = true;
	document.getElementById('Take_Amount_'+rowNumber).value = "";
	document.getElementById('Consider_For_Obligations_'+rowNumber).checked = true;
	}

}

function Enable_QCFields(id){
	var idArr = id.split('_');
	var rowNumber = idArr[idArr.length - 1];//CAC no
	if(document.getElementById(id).checked==true){
	document.getElementById('QC_Amt_'+rowNumber).disabled=false;
	document.getElementById('QC_EMI_'+rowNumber).disabled=false;
	document.getElementById('avg_utilization_'+rowNumber).disabled=false;
	//beow code modified for 883 PCSP
	document.getElementById('QC_Amt_'+rowNumber).readOnly=false;
	document.getElementById('QC_EMI_'+rowNumber).readOnly=false;
	document.getElementById('avg_utilization_'+rowNumber).readOnly=false;
	}
	else{
	document.getElementById('QC_Amt_'+rowNumber).disabled=true;
	document.getElementById('QC_EMI_'+rowNumber).disabled=true;
	document.getElementById('avg_utilization_'+rowNumber).disabled=true;
	document.getElementById('QC_Amt_'+rowNumber).value='';
	document.getElementById('QC_EMI_'+rowNumber).value='';
	document.getElementById('avg_utilization_'+rowNumber).value='';

	}

}
//function by saurabh on 4th Dec
function CAC_Check(id){
	var table = document.getElementById('mytab');
	var col_id = id.substring(0,id.lastIndexOf('_'));
	var row_num = id.substring(id.lastIndexOf('_')+1,id.length);
	if(document.getElementById(id).checked){
		for(var i=1;i<table.rows.length;i++){
			if(i==row_num){
			continue;
			}
			else{
				document.getElementById(col_id+'_'+i).disabled=true;
				window.parent.com.newgen.omniforms.formviewer.setEnabled('ExtLiability_CACIndicator',false);
			}
		}
	}
	else{
		for(var i=1;i<table.rows.length;i++){
			//change by saurabh on 13th Dec
			
			if((table.rows[i].cells[0].childNodes[0].value.indexOf('loan')==-1 &&table.rows[i].cells[0].childNodes[0].value.indexOf('Account')==-1 )&& closedOrActive[i].indexOf(',Closed')==-1){
			document.getElementById(col_id+'_'+i).disabled=false;
			window.parent.com.newgen.omniforms.formviewer.setEnabled('ExtLiability_CACIndicator',true);
			}
		}
	}
}

function keypresshandler(event)
{
	var charCode=event.keyCode;
	if(charCode>31 && (charCode<48 || charCode>57)){
		return false;
	}
}
function validateNum(Id){
	//changes by saurabh for pcsp-668
	if(Id.indexOf('QC_Amt')>-1 || Id.indexOf('QC_EMI')>-1 || Id.indexOf('Take_Amount')>-1 || Id.indexOf('avg_utilization')>-1){
	
		var value = document.getElementById(Id).value;
		for(var i=0;i<value.length;i++){
			if( value.charAt(i)!='.' && isNaN(parseFloat(value.charAt(i)))){
				document.getElementById(Id).value="";
			}
		}
	
	}

}
function limitDecimal(id){
//changes by saurabh for pcsp-668
	if(id.indexOf('QC_Amt')>-1 || id.indexOf('QC_EMI')>-1 || id.indexOf('Take_Amount')>-1 || id.indexOf('Finalclean')>-1 || id.indexOf('avg_utilization')>-1){
		var value = document.getElementById(id).value;
		if(isNaN(parseFloat(value))){
			alert('Please enter only numberic Values');
			document.getElementById(id).value = "";
		}
		else{
		if(value.indexOf('.')>-1){
			var valArr = value.split('.');
			var beforeDecimal = valArr[0];
			var afterDecimal = valArr[1];
				if(beforeDecimal.length >18 || afterDecimal.length>2){
					alert('Incorrect Field Pattern. It should be (18,2)');
					document.getElementById(id).value = "";
				}
			}
			else{
				if(value.length > 18){
				alert('Incorrect Field Pattern. It should be (18,2)');
				document.getElementById(id).value = "";
				}
			}
		}
	
	}
	
}

//below function added by nikhil
function hideatcsm() {
  var table=document.getElementById('mytab');
    for(var i=0;i<table.rows.length;i++){
		//document.getElementById('mytab').rows[i].cells.namedItem("FinalcleanfundedAmt_"+i).style.display="none";
		//document.getElementById("mytab").rows[i].cells.namedItem("MOB_"+i).style.display="none";
		table.rows[i].deleteCell(17);
		table.rows[i].deleteCell(16);	
 }  
} 

function openPopup(id)
	{
	
	var agreementval=document.getElementById(id).innerHTML;
	var arr = id.split('_');
	var rownum = arr[arr.length-1];
	var tablename = document.getElementById('datatablename_'+rownum).value;
	
	var url='/webdesktop/custom/CustomJSP/external.jsp?Wi_Name='+winame+'&AgreementId='+agreementval+'&table='+tablename;
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
							alert("Error in External_Liability. Params:"+params+". Please Contact Administrator");
					}							
					break;
				default:
					//alert(xmlReq.status);
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
		alert("Some error occured while fetching Customer Exposure. Please try after sometime or contact administrator");
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

    <body leftmargin="0"  marginwidth="0" 
          marginheight="0"  onload ="loadform();" class="dark-matter" >
		   <!-- Load jQuery and bootstrap datepicker scripts -->
		<form name="wdesk">
           <div id="mainDivContent">
				
				
			</div>
			
		</form>
		<script>
		
		</script>
    </body>
</html>
