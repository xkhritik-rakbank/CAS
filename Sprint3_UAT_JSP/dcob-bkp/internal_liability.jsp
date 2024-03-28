<!------------------------------------------------------------------------------------------------------
//           NEWGEN SOFTWARE TECHNOLOGIES LIMITED

//Group						 : Application ?Projects
//Product / Project			 : RAKBank-RLOS
//File Name					 : Internal Liability
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

<html topmargin="25">
    <head>
        <title>Internal Liability</title>
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
	var table_name="ng_RLOS_CUSTEXPOSE_LoanDetails";
	var row_id_arr=[];
	var data_save_var="";
	var ws_name = window.parent.activityName;
	var closedOrActive={};
	var initiationSteps = ['Branch_Init','CSM','Telesales_Init','Fulfillment_RM','Telesales_RM'];
	var pname="IL";
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
		
		for(var row_arr_count= 1;row_arr_count<=totalrowid;row_arr_count++){
				var value = "";
				data_save_var_arr = data_save_var.split(",");
				for(var x= 0 ; x<data_save_var_arr.length ; x++){
					var variable_id = data_save_var_arr[x]+"_"+row_arr_count;
					var table_name = "datatablename_"+row_arr_count;
					var agreementId  = "AgreementId_"+row_arr_count;
					stable = document.getElementById(table_name).value;
					if(activityName=="CC" || activityName=="PL" || activityName=="Cr"){
					var agreementId_val = document.getElementById(agreementId).innerHTML;
					}
					else{
					var agreementId_val = document.getElementById(agreementId).value;
					}
					if(stable=="ng_RLOS_CUSTEXPOSE_LoanDetails"){
						if(activityName=="CC" || activityName=="PL" || activityName=="Cr"){
						swhere = "Child_Wi = '"+winame+"' and AgreementId = '"+agreementId_val+"'";
						}
						else{
						swhere = "Wi_Name = '"+winame+"' and AgreementId = '"+agreementId_val+"'";
						}
					}
					else if(stable=="ng_RLOS_CUSTEXPOSE_CardDetails"){
						if(activityName=="CC" || activityName=="PL" || activityName=="Cr"){
						swhere = "Child_Wi = '"+winame+"' and CardEmbossNum = '"+agreementId_val+"'";
						}
						else{
							swhere = "Wi_Name = '"+winame+"' and CardEmbossNum = '"+agreementId_val+"'";
						}
					}
					else if(stable=="ng_RLOS_CUSTEXPOSE_AcctDetails"){
						if(activityName=="CC" || activityName=="PL" || activityName=="Cr"){
						swhere = "Child_Wi = '"+winame+"' and AcctId = '"+agreementId_val+"'";
						}
						else{
							swhere = "Wi_Name = '"+winame+"' and AcctId = '"+agreementId_val+"'";
						}
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
				alert('Liabilities Data saved!!');
			}
			document.getElementById('hiddenFlag').value="";
						
	}	

	function saveDateOnChange() {
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
				data_save_var_arr = data_save_var.split(",");
				for(var x= 0 ; x<data_save_var_arr.length ; x++){
					var variable_id = data_save_var_arr[x]+"_"+row_arr_count;
					var table_name = "datatablename_"+row_arr_count;
					var agreementId  = "AgreementId_"+row_arr_count;
					stable = document.getElementById(table_name).value;
					if(activityName=="CC" || activityName=="PL" || activityName=="Cr"){
					var agreementId_val = document.getElementById(agreementId).innerHTML;
					}
					else{
					var agreementId_val = document.getElementById(agreementId).value;
					}
					if(stable=="ng_RLOS_CUSTEXPOSE_LoanDetails"){
						if(activityName=="CC" || activityName=="PL" || activityName=="Cr"){
						swhere = "Child_Wi = '"+winame+"' and AgreementId = '"+agreementId_val+"'";
						}
						else{
						swhere = "Wi_Name = '"+winame+"' and AgreementId = '"+agreementId_val+"'";
						}
					}
					else if(stable=="ng_RLOS_CUSTEXPOSE_CardDetails"){
						if(activityName=="CC" || activityName=="PL" || activityName=="Cr"){
						swhere = "Child_Wi = '"+winame+"' and CardEmbossNum = '"+agreementId_val+"'";
						}
						else{
							swhere = "Wi_Name = '"+winame+"' and CardEmbossNum = '"+agreementId_val+"'";
						}
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
				//alert('Liabilities Data saved!!');
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
		fetchCardGridData();
		fetchODData();
		disableSettlementforCC();
		disableSettlement();
		disable_postload();
		colorClosedLiab();
		saveDateOnChange();
		//added by akshay on 9/1/17
		try{
		window.parent.style.top="100px";
		}
		catch(ex){}
    }
	
	function disableSettlementforCC(){
	var toal_row_count= $('table#mytab tr:last').index() + 1;
		//changes by nikgil for SVT server
		if((activityName=="CC" )|| (activityName=="Cr" ) ||((activityName=="CA")&&(window.parent.com.newgen.omniforms.formviewer.getLVWAT('cmplx_Product_cmplx_ProductGrid',0,1)=='Credit Card' ))){
		for (var i=1;i<toal_row_count;i++){
				var Settlement_Flag = "Settlement_Flag_"+i;
					document.getElementById(Settlement_Flag).disabled = true;
			}
		}
	
	}
	
	function disableSettlement(){
	var toal_row_count= $('table#mytab tr:last').index() + 1;
		
		if(initiationSteps.indexOf(ws_name)<0 && ws_name.indexOf('CAD_Analyst1')<0){
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
					document.getElementById('row_'+key).style.backgroundColor = "orange";
					setDesiredColorinCells(table,key,'orange');
				}
				}
		swaprow(table);
				
			
		}
	}
	
function swaprow(table){
	var count=0;
	for(var key in closedOrActive){
		if(closedOrActive[key].indexOf(',C')>-1){
		var row = table.rows[key-count];
		var row1 = table.insertRow(table.rows.length);
		row1.innerHTML = row.innerHTML;
		row1.style.backgroundColor = row.style.backgroundColor;
		if(closedOrActive[key].indexOf('Individual_CIF')>-1 || closedOrActive[key].indexOf('')>-1){
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
				if(color=='Silver' && table.rows[key].cells[i].childNodes[0].id.indexOf('AgreementId')==-1){
				table.rows[key].cells[i].childNodes[0].disabled = true;
				}
				if(color=='Silver' && table.rows[key].cells[i].childNodes[0].id.indexOf('Consider_For_Obligations')>-1)
				{
				table.rows[key].cells[i].childNodes[0].checked = false;
				}
		}
	}
}

	
	function disable_postload(){
		var toal_row_count= $('table#mytab tr:last').index() + 1;
		//change done by nikhil for SVT server
		if(((activityName=="CC" || activityName=="PL" || activityName=="Cr") && ws_name.indexOf('CAD_Analyst1')<0) && initiationSteps.indexOf(ws_name)==-1){//changed by nikhil for editable at CSM
			for (var i=1;i<toal_row_count;i++){
				var Limit_Increase = "Limit_Increase_"+i;
				var Consider_For_Obligations = "Consider_For_Obligations_"+i;
				var Settlement_Flag = "Settlement_Flag_"+i;
					document.getElementById(Limit_Increase).disabled = true;
					document.getElementById(Consider_For_Obligations).disabled = true;
					document.getElementById(Settlement_Flag).disabled = true;
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
	var sQry="";
	var wparams="";
	//ng_RLOS_CUSTEXPOSE_LoanDetails - AgreementId
	//var sQry="select Product_Type,Type_of_card,Agreement_ID,Limit_Amt_Fin,EMI,General_Satus,Limit_Increase,Outstanding_Amount,Consider_For_Obligations,Interest_Rate,MOB,No_of_Repayments_Done,Type_Of_OD,Previous_loan_Amount,Prev_Loan_No_Of_Installments FROM  "+table_name+" with (nolock) WHERE wi_name = '"+winame+"'";
	if(activityName=='CA'){//RLOS
			sQry='CA';
			//sQry="select 'ng_RLOS_CUSTEXPOSE_LoanDetails' as table_name,CifId,Liability_type,LoanType,SchemeCardProd,AgreementId,ProductAmt,NextInstallmentAmt,General_Status,Limit_Increase,TotalOutstandingAmt,Consider_For_Obligations,InterestRate,MonthsOnBook,(cast(TotalNoOfInstalments as int) - cast(RemainingInstalments as int)) as NoOfRepaymentDone,'',PreviousLoanTAI,PreviousLoanDBR,Settlement_Flag from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where Wi_Name = '"+winame+"' and (Request_Type = 'InternalExposure' or Request_Type = 'CollectionsSummary') and LoanStat not in ('Pipeline','C') order by Liability_type desc";
			wparams="Wi_Name=="+winame;
	}
	//commneted for svt server
	/*else if(activityName=='Cr'){//CC
		sQry='Cr';
		//sQry="select 'ng_RLOS_CUSTEXPOSE_LoanDetails' as table_name,LoanType,'', AgreementId,ProductAmt,NextInstallmentAmt,Limit_Increase,TotalOutstandingAmt,Consider_For_Obligations,InterestRate,MonthsOnBook,(cast(TotalNoOfInstalments as int) - cast(RemainingInstalments as int)) as NoOfRepaymentDone,'','','',Settlement_Flag from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where Child_Wi = '"+winame+"' and (Request_Type = 'InternalExposure' or Request_Type = 'CollectionsSummary')";
		wparams="Child_Wi=="+winame;
	}*/
	else{//PL
		if(initiationSteps.indexOf(ws_name)>-1){
			sQry='PL';
		//sQry="select 'ng_RLOS_CUSTEXPOSE_LoanDetails' as table_name,LoanType,CifId,Liability_type,SchemeCardProd, AgreementId,ProductAmt,NextInstallmentAmt,General_Status,Limit_Increase,TotalOutstandingAmt,Consider_For_Obligations,InterestRate,MonthsOnBook,(cast(TotalNoOfInstalments as int) - cast(RemainingInstalments as int)) as NoOfRepaymentDone,'',PreviousLoanDBR,PreviousLoanTAI,Settlement_Flag,LoanStat from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where Child_Wi = '"+winame+"' and (Request_Type = 'InternalExposure' or Request_Type = 'CollectionsSummary') and LoanStat not in ('Pipeline','C') order by Liability_type desc";
		wparams="Child_Wi=="+winame;
		}
		else{
			sQry="PLE";
		//sQry="select 'ng_RLOS_CUSTEXPOSE_LoanDetails' as table_name,LoanType,CifId,Liability_type,SchemeCardProd, AgreementId,ProductAmt,NextInstallmentAmt,General_Status,Limit_Increase,TotalOutstandingAmt,Consider_For_Obligations,InterestRate,MonthsOnBook,(cast(TotalNoOfInstalments as int) - cast(RemainingInstalments as int)) as NoOfRepaymentDone,'',PreviousLoanDBR,PreviousLoanTAI,Settlement_Flag,LoanStat from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where Child_Wi = '"+winame+"' and (Request_Type = 'InternalExposure' or Request_Type = 'CollectionsSummary') and LoanStat != 'Pipeline' order by Liability_type desc";
		wparams="Child_Wi=="+winame;
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

function fetchCardGridData()
{
	jspName="CustomSelect.jsp";
	var params="";
	var result="";
	var sQry="";
	var wparams="";
	
	if(activityName=='CA' || activityName=='LE'){
		sQry="fetchCALE";	
		//sQry="select distinct 'ng_RLOS_CUSTEXPOSE_CardDetails' as table_name,A.CifId,Liability_type as LiabilityType,CardType as ProductType,SchemeCardProd as TypeofCardLoan, CardEmbossNum as AgreementId, CreditLimit as LimitAmtFin,  (case when (a.SchemeCardProd = 'LOC PREFERRED' or a.SchemeCardProd = 'LOC STANDARD') then isnull(b.MonthlyAmount,0) ELSE ISnull(PaymentsAmount,0) End) as EMI , General_Status,Limit_Increase as LimitIncrease,(case when (a.SchemeCardProd = 'LOC PREFERRED' or a.SchemeCardProd = 'LOC STANDARD') then isnull(b.OutstandingBalance,0) ELSE ISnull(outstandingAmt,0) End) as OutstandingAmount, Consider_For_Obligations as ConsiderForObligations,(case when (a.SchemeCardProd = 'LOC PREFERRED' or a.SchemeCardProd = 'LOC STANDARD') then isnull(b.interestRate,0) ELSE ISnull(a.InterestRate,0) End)  as InterestRate,MonthsOnBook,(case when (a.SchemeCardProd = 'LOC PREFERRED' or a.SchemeCardProd = 'LOC STANDARD') then ISNULL((CAST(b.INSTALMENTpERIOD AS INT)-CAST(b.rEMAININGemi AS INT)),'') else ISNULL((CAST(b.INSTALMENTpERIOD AS INT)-CAST(b.rEMAININGemi AS INT)),'') end )  as NoOfRepaymentDone,'' as TypeofOD,'' as PreviousLoanTAI,'' as PreviousLoanDBR,Settlement_Flag from ng_RLOS_CUSTEXPOSE_CardDetails a with (nolock) left join ng_RLOS_CUSTEXPOSE_CardInstallmentDetails  b with (nolock) on CardEmbossNum=replace(CardNumber,'I','') and a.Wi_Name=b.Wi_Name and a.SchemeCardProd  in ('LOC STANDARD','LOC PREFERRED') and (a.Request_Type = 'InternalExposure' or a.Request_Type = 'CollectionsSummary' or b.Request_Type='CARD_INSTALLMENT_DETAILS') where  a.Wi_Name ='"+winame+"' and CustRoleType !='Secondary' and CardStatus !='C' order by Liability_type desc";
		wparams="Wi_Name=="+winame;
	}
	else if(activityName=='Cr'){
		sQry="fetchCr";
		//sQry="select distinct 'ng_RLOS_CUSTEXPOSE_CardDetails' as table_name,CardType as ProductType,A.CifId,Liability_type as LiabilityType,SchemeCardProd as TypeofCardLoan,CardEmbossNum as AgreementId,CreditLimit as LimitAmtFin,  (case when (a.SchemeCardProd = 'LOC PREFERRED' or a.SchemeCardProd = 'LOC STANDARD') then isnull(b.MonthlyAmount,0) ELSE ISnull(PaymentsAmount,0) End) as EMI ,General_Status,Limit_Increase as LimitIncrease,OutstandingAmt as OutstandingAmount,Consider_For_Obligations as ConsiderForObligations,ISNULL(b.InterestRate,'') as InterestRate,MonthsOnBook,ISNULL((CAST(INSTALMENTpERIOD AS INT)-CAST(rEMAININGemi AS INT)),'') as NoOfRepaymentDone,'' as TypeofOD,'' as PreviousLoanTAI ,'' as PreviousLoanDBR,Settlement_Flag,'' as  Creditlimit from ng_RLOS_CUSTEXPOSE_CardDetails a with (nolock) left join ng_RLOS_CUSTEXPOSE_CardInstallmentDetails  b with (nolock) on CardEmbossNum=replace(CardNumber,'I','') and a.Wi_Name=b.Wi_Name and a.SchemeCardProd  in ('LOC STANDARD','LOC PREFERRED') and (a.Request_Type = 'InternalExposure' or a.Request_Type = 'CollectionsSummary' or b.Request_Type='CARD_INSTALLMENT_DETAILS') where  a.Child_Wi ='"+winame+"' and CustRoleType !='Secondary'";
		wparams="Child_Wi=="+winame;
	}else{
		if(initiationSteps.indexOf(ws_name)>-1){
		sQry="fetchPL";
		//sQry="select distinct 'ng_RLOS_CUSTEXPOSE_CardDetails' as table_name,CardType as ProductType,A.CifId,Liability_type as LiabilityType,SchemeCardProd as TypeofCardLoan,CardEmbossNum as AgreementId,CreditLimit as LimitAmtFin,  (case when (a.SchemeCardProd = 'LOC PREFERRED' or a.SchemeCardProd = 'LOC STANDARD') then isnull(b.MonthlyAmount,0) ELSE ISnull(PaymentsAmount,0) End) as EMI ,General_Status,Limit_Increase as LimitIncrease,OutstandingAmt as OutstandingAmount,Consider_For_Obligations as ConsiderForObligations,ISNULL(b.InterestRate,'') as InterestRate,MonthsOnBook,ISNULL((CAST(INSTALMENTpERIOD AS INT)-CAST(rEMAININGemi AS INT)),'') as NoOfRepaymentDone,'' as TypeofOD,'' as PreviousLoanTAI ,'' as PreviousLoanDBR,Settlement_Flag,'' as  Creditlimit,CardStatus from ng_RLOS_CUSTEXPOSE_CardDetails a with (nolock) left join ng_RLOS_CUSTEXPOSE_CardInstallmentDetails  b with (nolock) on CardEmbossNum=replace(CardNumber,'I','') and a.Wi_Name=b.Wi_Name and a.SchemeCardProd  in ('LOC STANDARD','LOC PREFERRED') and (a.Request_Type = 'InternalExposure' or a.Request_Type = 'CollectionsSummary' or b.Request_Type='CARD_INSTALLMENT_DETAILS') where  a.Child_Wi ='"+winame+"' and CustRoleType !='Secondary' and CardStatus !='C' order by Liability_type desc"; 
		wparams="Child_Wi=="+winame;
		}
		else{
		sQry="fetchPLE";
		//sQry="select distinct 'ng_RLOS_CUSTEXPOSE_CardDetails' as table_name,CardType as ProductType,A.CifId,Liability_type as LiabilityType,SchemeCardProd as TypeofCardLoan,CardEmbossNum as AgreementId,CreditLimit as LimitAmtFin,  (case when (a.SchemeCardProd = 'LOC PREFERRED' or a.SchemeCardProd = 'LOC STANDARD') then isnull(b.MonthlyAmount,0) ELSE ISnull(PaymentsAmount,0) End) as EMI ,General_Status,Limit_Increase as LimitIncrease,OutstandingAmt as OutstandingAmount,Consider_For_Obligations as ConsiderForObligations,ISNULL(b.InterestRate,'') as InterestRate,MonthsOnBook,ISNULL((CAST(INSTALMENTpERIOD AS INT)-CAST(rEMAININGemi AS INT)),'') as NoOfRepaymentDone,'' as TypeofOD,'' as PreviousLoanTAI ,'' as PreviousLoanDBR,Settlement_Flag,'' as  Creditlimit,CardStatus from ng_RLOS_CUSTEXPOSE_CardDetails a with (nolock) left join ng_RLOS_CUSTEXPOSE_CardInstallmentDetails  b with (nolock) on CardEmbossNum=replace(CardNumber,'I','') and a.Wi_Name=b.Wi_Name and a.SchemeCardProd  in ('LOC STANDARD','LOC PREFERRED') and (a.Request_Type = 'InternalExposure' or a.Request_Type = 'CollectionsSummary' or b.Request_Type='CARD_INSTALLMENT_DETAILS') where  a.Child_Wi ='"+winame+"' and CustRoleType !='Secondary' order by Liability_type desc";
		wparams="Child_Wi=="+winame;
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
{
	jspName="CustomSelect.jsp";
	var params="";
	var result="";
	var sQry="";
	var wparams="";
	
	if(activityName=='CA' || activityName=='LE'){
		sQry="ODCALE";
		//sQry="select 'ng_RLOS_CUSTEXPOSE_AcctDetails' as table_name,cifid,Account_Type,'OverDraft' as ProductType,ODDesc,AcctId,SanctionLimit,'' as EMI,case when WriteoffStat='Y' then 'P2' else 'P6' end as General_Status,'' as LimitIncrease,ClearBalanceAmount as OutstandingAmount,Consider_For_Obligations as ConsiderForObligation,'' as InterestRate,MonthsOnBook as MonthsOnBook,'' as NoOfRepaymentDone,ODType as TypeofOD,'' as PreviousLoanTAI,'' as PreviousLoanDBR from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where Wi_Name  ='"+winame+"'  and ODType != ''";
		wparams="Wi_Name=="+winame;
	}
	else if(activityName=='Cr'){
		sQry="ODCr";
		//sQry="select 'ng_RLOS_CUSTEXPOSE_AcctDetails' as table_name,cifid,Account_Type,'OverDraft' as ProductType,ODDesc,AcctId,SanctionLimit,'' as EMI,case when WriteoffStat='Y' then 'P2' else 'P6' end as General_Status,'' as LimitIncrease,ClearBalanceAmount as OutstandingAmount,Consider_For_Obligations as ConsiderForObligation,'' as InterestRate,MonthsOnBook as MonthsOnBook,'' as NoOfRepaymentDone,ODType as TypeofOD,'' as PreviousLoanTAI,'' as PreviousLoanDBR from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where Wi_Name  ='"+winame+"'  and ODType != ''";
		wparams="Wi_Name=="+winame;
	}else{
		if(initiationSteps.indexOf(ws_name)>-1){
			sQry="ODPL";
		//sQry="select 'ng_RLOS_CUSTEXPOSE_AcctDetails' as table_name,'OverDraft' as ProductType,cifid,Account_Type,ODDesc,AcctId,SanctionLimit,'' as EMI,case when WriteoffStat='Y' then 'P2' else 'P6' end as General_Status,'' as LimitIncrease,ClearBalanceAmount as OutstandingAmount,Consider_For_Obligations as ConsiderForObligation,'' as InterestRate,MonthsOnBook as MonthsOnBook,'' as NoOfRepaymentDone,ODType as TypeofOD,'' as PreviousLoanTAI,'' as PreviousLoanDBR from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where child_wi  ='"+winame+"'  and ODType != ''";
		wparams="Wi_Name=="+winame; 
	}
		else{
			sQry="ODPLE";
		//sQry="select 'ng_RLOS_CUSTEXPOSE_AcctDetails' as table_name,'OverDraft' as ProductType,cifid,Account_Type,ODDesc,AcctId,SanctionLimit,'' as EMI,case when WriteoffStat='Y' then 'P2' else 'P6' end as General_Status,'' as LimitIncrease,ClearBalanceAmount as OutstandingAmount,Consider_For_Obligations as ConsiderForObligation,'' as InterestRate,MonthsOnBook as MonthsOnBook,'' as NoOfRepaymentDone,ODType as TypeofOD,'' as PreviousLoanTAI,'' as PreviousLoanDBR from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where child_wi  ='"+winame+"'  and ODType != ''";
		wparams="Wi_Name=="+winame;
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


function fetchValidQueueData()
{
			
			jspName="CustomSelect.jsp";
			var params="";
			var result="";
			 var dhtml="";
			 var wparams="";
			 //scroll changes by saurabh on 21st nov to correct scroll.
			  	 dhtml+="<div>"+
				"<table id='mytab' border='0' bordercolor='#337ab7' max-height='500px' style='overflow-x:scroll;overflow-y:auto;' >";
				//done for svt server
			if(activityName == 'CC' || activityName=="Cr" ){
			activityName = 'PL';
			}
			var sQry="QueueData";
			//var sQry="select col_name ,col_type,max_len,col_id,default_value,is_readonly,ActivityName from NG_Dynamic_Grid_config with (nolock) WHERE Grid_name = 'Internal_Liability' and ActivityName='"+activityName+"'ORDER BY col_seq";
			wparams="Grid_name==Internal_Liability~~ActivityName=="+activityName;
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
				dhtml+="<div style='width: 100%; border: 0px #e01414  solid; padding:0px; margin: 0px'><table border='0' borderColor='#e01414' overflow-x:scroll;overflow-y:scroll>";
			 	if 	(activityName=='CA'){
			dhtml+=/*"<image src='addImage.png' height='15px' width='15px' id='addimage1"+"' style='padding-right:10px' onclick='addClick(this)'/>*/"<input type='button'  id='savedata' value='save data' style='background-color:#e01414; color:#ffffff' onclick='saveDate()'/></div></table>";
			
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
	return "<tr background-color:#e01414 ><p align='right'>"+table_header+"</tr>";
}



function tb_row(row_num, val){
	var row_arr = WiDetails.split("~");
	var col_id_arr="";
	var temp = "";
	InputField_id="WI_Name";
	if(val==""){
		var table_row ="<td><input type='checkbox' id ='check_"+row_num+"' name='check'></td>";
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
		if(activityName!='CA'){
		closedOrActive[row_num]= val_new[3]+','+ val_new[val_new.length-2];
		}
		else{
		closedOrActive[row_num]= val_new[2];
		}
		var table_row ="<td><input type='hidden' id ='datatablename_"+row_num+"' name='datatablename' value='"+val_new[0]+"' ></td>";
		var product_type = "";
			var cardType = "";
		for(var i=0;i<row_arr.length-1;i++){
			temp = row_arr[i];
			col_id_arr=temp.split("#");
			InputField_id = InputField_id+","+col_id_arr[3];
			//change by saurabh on 15th Jan to show ref no as text at CSM like CSO.
			if(initiationSteps.indexOf(ws_name)>-1 && col_id_arr[3].indexOf('AgreementId')>-1 && col_id_arr[1]=="link"){
				col_id_arr[1]="text";
			}
			if(col_id_arr[1]=="text"){
			//changes done by nikhil for PCAS-1229
			var textproperty = col_id_arr[5];
			if(textproperty=="readonly")
			{
				textproperty="disabled";
			}
				if(col_id_arr[3].indexOf('Product_Type')>-1){
				product_type = val_new[i+1];
				table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+val_new[i+1]+"' maxlength ='"+col_id_arr[2]+"'"+textproperty+">"+"</td>";
				}
				else if(col_id_arr[3].indexOf('CardType')>-1){
				cardType = val_new[i+1];
				table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+val_new[i+1]+"' maxlength ='"+col_id_arr[2]+"'"+textproperty+">"+"</td>";
				}
				
				else if(col_id_arr[3].indexOf("EMI")>-1){
					if(product_type.indexOf('Card')>-1 && cardType.indexOf('LOC')==-1){
						table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' value='' maxlength ='"+col_id_arr[2]+"'"+textproperty+">"+"</td>";
					}
					else{
						table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+val_new[i+1]+"' maxlength ='"+col_id_arr[2]+"'"+textproperty+">"+"</td>";
					}
				}
				else{
					table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+val_new[i+1]+"' maxlength ='"+col_id_arr[2]+"'"+textproperty+">"+"</td>";
					
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
			else if(col_id_arr[1]=="checkbox"){
			var value = val_new[i+1];
			var appType = window.parent.com.newgen.omniforms.formviewer.getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4);
			var subProd = window.parent.com.newgen.omniforms.formviewer.getLVWAT('cmplx_Product_cmplx_ProductGrid',0,2);
			
			//added by akshay on 29/12/17 for closed cards
			if(val_new[val_new.length-2]=='C'  && initiationSteps.indexOf(ws_name)==-1){
				table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+col_id_arr[4]+" disabled >"+"</td>";
					
			}
			//ended by akshay 	
			else if(value == "true"){
			table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+col_id_arr[4]+" "+col_id_arr[5]+" onclick = 'LimitincreaseIndicatorLogic(this.id);disableOthers(this.id);calculateNetPayout(this.id);SettlementFlag(this.id);' checked >"+"</td>";
			}
			else if(value == 'false' && col_id_arr[4] == 'checked'){//case for consider for obligations.
			table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+col_id_arr[5]+" onclick = 'LimitincreaseIndicatorLogic(this.id);disableOthers(this.id);calculateNetPayout(this.id);SettlementFlag(this.id);' >"+"</td>";
			}
			else if(col_id_arr[3]=='Limit_Increase' && !((appType.indexOf('TOP')>-1) || (appType.indexOf('RES')>-1) || subProd=='LI' || subProd=='PU' || subProd=='PULI' )){
			if(initiationSteps.indexOf(ws_name)==-1)
			{
			table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+col_id_arr[4]+" "+col_id_arr[5]+" onclick = 'LimitincreaseIndicatorLogic(this.id);disableOthers(this.id);calculateNetPayout(this.id);SettlementFlag(this.id);' disabled >"+"</td>";
			}
			else{
			table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+col_id_arr[4]+" "+col_id_arr[5]+" onclick = 'LimitincreaseIndicatorLogic(this.id);disableOthers(this.id);calculateNetPayout(this.id);SettlementFlag(this.id);'  >"+"</td>";
			}
			}
			else{
			table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+col_id_arr[4]+" "+col_id_arr[5]+" onclick = 'LimitincreaseIndicatorLogic(this.id);disableOthers(this.id);calculateNetPayout(this.id);SettlementFlag(this.id);' >"+"</td>";
			}
			
			}
			else if(col_id_arr[1]=="link"){
			
				table_row = table_row +"<td>"+"<a id='"+col_id_arr[3]+"_"+row_num+"' href='#' onclick='openPopup(this.id);'>"+val_new[i+1]+"</a>"+"</td>";
			}
		}
	}
	
	return "<tr background-color:#e01414 class='HeaderClass' id = row_"+row_num+" ><p align='right'>"+table_row+"</tr>";
}

function LimitincreaseIndicatorLogic(id){
var arr = id.split('_');
	var rownum = arr[arr.length - 1];
	if(id.indexOf('Limit_Increase')>-1){
			var arr = id.split('_');
			var rownum = arr[arr.length - 1];
			if(document.getElementById(id).checked || document.getElementById(id).checked == 'true'){
				if(window.parent.com.newgen.omniforms.formviewer.getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4)=='TOPE' || window.parent.com.newgen.omniforms.formviewer.getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4)=='TOPN' || window.parent.com.newgen.omniforms.formviewer.getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4)=='TOPIM' || window.parent.com.newgen.omniforms.formviewer.getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4)=='RESC' ||window.parent.com.newgen.omniforms.formviewer.getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4)=='RESN' ) {
					if(document.getElementById('Consider_For_Obligations_'+rownum).checked || document.getElementById('Consider_For_Obligations_'+rownum).checked == 'true'){
						document.getElementById('Consider_For_Obligations_'+rownum).checked = false;
						//document.getElementById('Consider_For_Obligations_'+rownum).disabled = true;--commented by akshay on 29/12/17 for drop 3 change
					}
				}
			}
			else{
			if(window.parent.com.newgen.omniforms.formviewer.getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4)=='TOPE' || window.parent.com.newgen.omniforms.formviewer.getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4)=='TOPN' || window.parent.com.newgen.omniforms.formviewer.getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4)=='TOPIM' || window.parent.com.newgen.omniforms.formviewer.getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4)=='RESC' ||window.parent.com.newgen.omniforms.formviewer.getLVWAT('cmplx_Product_cmplx_ProductGrid',0,4)=='RESN' ) {
					if(!document.getElementById('Consider_For_Obligations_'+rownum).checked || document.getElementById('Consider_For_Obligations_'+rownum).checked == 'false'){
						document.getElementById('Consider_For_Obligations_'+rownum).checked = true;
						//document.getElementById('Consider_For_Obligations_'+rownum).disabled = false;
					}
				}
				
			}
	}
	if(document.getElementById(id).checked){
		window,parent.setNGValue('CRN',document.getElementById('AgreementId_'+rownum).value);//added by akshay on 9/4/17 for proc 7710---to show CRN in mail template
	}
	else{
		window,parent.setNGValue('CRN',"");//added by akshay on 9/4/17
	}
	saveDateOnChange();
}


function SettlementFlag(id){
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
				
			

function disableOthers(id){
window.parent.setNGValueCustom('Eligibility_Trigger','Y');
	if(id.indexOf('Limit_Increase')>-1){
	var arr = id.split('_');
	var rownum = arr[arr.length - 1];
	var staticPart = id.substring(0,id.indexOf('_'+rownum));
	
	var table = document.getElementById('mytab');
	var check = false;
	if(document.getElementById(id).checked){check=true;}
	for(var i=1;i<table.rows.length;i++){
		if(i==rownum){continue;}
		else{
			document.getElementById(staticPart+'_'+i).disabled = check;
		}
		}
	}
}

function calculateNetPayout(id){
	var arr = id.split('_');
	var rownum = arr[arr.length - 1];
	var staticPart = id.substring(0,id.indexOf('_'+rownum));
	//case for Loan liab checked for Top up.
	if(id.indexOf('Limit_Increase')>-1 && document.getElementById(id).checked && document.getElementById('datatablename_'+rownum).value.indexOf('Loan')>-1){
		if(ngFormSyntaxVisible('cmplx_EligibilityAndProductInfo_NetPayout') && ngFormSyntaxGetvalue('cmplx_EligibilityAndProductInfo_FinalLimit')!=''){
			var outBal = document.getElementById('OutstandingAmt_'+rownum).value;
			var balance;
			if(outBal=='' || outBal==' '){
				balance = 0.0;
			}
			else{
				balance = parseFloat(outBal);
			}
			var netPayout = parseFloat(ngFormSyntaxGetvalue('cmplx_EligibilityAndProductInfo_FinalLimit')) - balance;
			ngFormSyntaxSetvalue('cmplx_EligibilityAndProductInfo_NetPayout',netPayout);
		}
	}
	////case for Loan or card liab unchecked for Top up.
	else if(id.indexOf('Limit_Increase')>-1 && !document.getElementById(id).checked && ngFormSyntaxVisible('cmplx_EligibilityAndProductInfo_NetPayout')){
		ngFormSyntaxSetvalue('cmplx_EligibilityAndProductInfo_NetPayout',ngFormSyntaxGetvalue('cmplx_EligibilityAndProductInfo_FinalLimit'));
	}
	////case for card liab checked for Top up after unchecking loan.
	else if(id.indexOf('Limit_Increase')>-1 && document.getElementById(id).checked && document.getElementById('datatablename_'+rownum).value.indexOf('Card')>-1 && ngFormSyntaxVisible('cmplx_EligibilityAndProductInfo_NetPayout') && ngFormSyntaxGetvalue('cmplx_EligibilityAndProductInfo_NetPayout')!=''){
		ngFormSyntaxSetvalue('cmplx_EligibilityAndProductInfo_NetPayout','');
	}
}

function ngFormSyntaxVisible(controlName){
	return window.parent.com.newgen.omniforms.formviewer.isVisible(controlName);
}
function ngFormSyntaxGetvalue(controlName){
	return window.parent.com.newgen.omniforms.formviewer.getNGValue(controlName);
}
function ngFormSyntaxSetvalue(controlName,value){
	return window.parent.com.newgen.omniforms.formviewer.setNGValue(controlName,value);
}

function openPopup(id)
	{
	
	var agreementval=document.getElementById(id).innerHTML;
	var arr = id.split('_');
	var rownum = arr[1];
	var tablename = document.getElementById('datatablename_'+rownum).value;
	//alert(agreementval);
	//WriteLog("appServerType jsp: result: "+agreementval);
	var url='/webdesktop/custom/CustomJSP/internal.jsp?Wi_Name='+winame+'&AgreementId='+agreementval+'&table='+tablename;
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
							alert("Error in Funding_Account_No. Params:"+params+". Please Contact Administrator");
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
		alert("Some error occured while fetching Customer Exposure. Please try ofter sometime or contact administrator");
		//return false;
    }
}

</script>

    </head>

    <body leftmargin="0" topmargin="15" marginwidth="0" marginheight="0"  onload ="loadform();" class="dark-matter">
		   <!-- Load jQuery and bootstrap datepicker scripts -->
            <form name="wdesk">
           <div id="mainDivContent">
				
				
			</div>
			
		</form>
		<script>
		
		</script>
    </body>
</html>
