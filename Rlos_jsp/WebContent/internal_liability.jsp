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
	var winame= window.parent.pid;
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
	var table_name="ng_RLOS_CUSTEXPOSE_LoanDetails";
	var row_id_arr=[];
	var data_save_var="";
	var ws_name = window.parent.activityName;
	
	  
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
		
    }
	
	function disable_postload(){
		var toal_row_count= $('table#mytab tr:last').index() + 1;
		
		if((activityName=="CC" || activityName=="PL" ) && ws_name.indexOf('CAD_Analyst1')<0){
			for (var i=1;i<toal_row_count;i++){
				var Limit_Increase = "Limit_Increase_"+i;
				var Consider_For_Obligations = "Consider_For_Obligations_"+i;
					document.getElementById(Limit_Increase).disabled = true;
					document.getElementById(Consider_For_Obligations).disabled = true;
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
	//ng_RLOS_CUSTEXPOSE_LoanDetails - AgreementId
	//var sQry="select Product_Type,Type_of_card,Agreement_ID,Limit_Amt_Fin,EMI,General_Satus,Limit_Increase,Outstanding_Amount,Consider_For_Obligations,Interest_Rate,MOB,No_of_Repayments_Done,Type_Of_OD,Previous_loan_Amount,Prev_Loan_No_Of_Installments FROM  "+table_name+" with (nolock) WHERE wi_name = '"+winame+"'";
	if(activityName=='CA'){//RLOS
			sQry="select 'ng_RLOS_CUSTEXPOSE_LoanDetails' as table_name,CifId,Liability_type,LoanType,SchemeCardProd,AgreementId,TotalLoanAmount,NextInstallmentAmt,General_Status,Limit_Increase,TotalOutstandingAmt,Consider_For_Obligations,InterestRate,MonthsOnBook,RemainingInstalments,'',PreviousLoanTAI,PreviousLoanDBR from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where Wi_Name = '"+winame+"' and (Request_Type = 'InternalExposure' or Request_Type = 'CollectionsSummary')";
	}
	else if(activityName=='Cr'){//CC
		sQry="select 'ng_RLOS_CUSTEXPOSE_LoanDetails' as table_name,LoanType,'', AgreementId,TotalLoanAmount,NextInstallmentAmt,Limit_Increase,TotalOutstandingAmt,Consider_For_Obligations,InterestRate,MonthsOnBook,RemainingInstalments,'','','' from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where Child_Wi = '"+winame+"' and (Request_Type = 'InternalExposure' or Request_Type = 'CollectionsSummary')";
	}
	else{//PL
		sQry="select 'ng_RLOS_CUSTEXPOSE_LoanDetails' as table_name,LoanType,CifId,Liability_type,SchemeCardProd, AgreementId,TotalLoanAmount,NextInstallmentAmt,General_Status,Limit_Increase,TotalOutstandingAmt,Consider_For_Obligations,InterestRate,MonthsOnBook,RemainingInstalments,'',PreviousLoanTAI,PreviousLoanDBR,'' from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where Child_Wi = '"+winame+"' and (Request_Type = 'InternalExposure' or Request_Type = 'CollectionsSummary')";
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

function fetchCardGridData()
{
	jspName="CustomSelect.jsp";
	var params="";
	var result="";
	var sQry="";
	
	if(activityName=='CA' || activityName=='LE'){
		sQry="select distinct 'ng_RLOS_CUSTEXPOSE_CardDetails' as table_name,A.CifId,Liability_type as LiabilityType,CardType as ProductType,SchemeCardProd as TypeofCardLoan, CardEmbossNum as AgreementId, CreditLimit as LimitAmtFin,  (case when (a.SchemeCardProd = 'LOC PREFERRED' or a.SchemeCardProd = 'LOC STANDARD') then isnull(b.MonthlyAmount,0) ELSE ISnull(PaymentsAmount,0) End) as EMI , General_Status,Limit_Increase as LimitIncrease,OutstandingAmt as OutstandingAmount, Consider_For_Obligations as ConsiderForObligations,ISNULL(b.InterestRate,'') as InterestRate,MonthsOnBook, ISNULL((CAST(INSTALMENTpERIOD AS INT)-CAST(rEMAININGemi AS INT)),'') as NoOfRepaymentDone,'' as TypeofOD,'' as PreviousLoanTAI,'' as PreviousLoanDBR from ng_RLOS_CUSTEXPOSE_CardDetails a left join ng_RLOS_CUSTEXPOSE_CardInstallmentDetails  b with (nolock) on CardEmbossNum=replace(CardNumber,'I','') and a.Wi_Name=b.Wi_Name and a.SchemeCardProd  in ('LOC STANDARD','LOC PREFERRED') and (a.Request_Type = 'InternalExposure' or a.Request_Type = 'CollectionsSummary' or b.Request_Type='CARD_INSTALLMENT_DETAILS') where  a.Wi_Name ='"+winame+"' and CustRoleType !='Secondary'";
	}
	else if(activityName=='Cr'){
		sQry="select distinct 'ng_RLOS_CUSTEXPOSE_CardDetails' as table_name,CardType as ProductType,A.CifId,Liability_type as LiabilityType,SchemeCardProd as TypeofCardLoan,CardEmbossNum as AgreementId,CreditLimit as LimitAmtFin,  (case when (a.SchemeCardProd = 'LOC PREFERRED' or a.SchemeCardProd = 'LOC STANDARD') then isnull(b.MonthlyAmount,0) ELSE ISnull(PaymentsAmount,0) End) as EMI ,General_Status,Limit_Increase as LimitIncrease,OutstandingAmt as OutstandingAmount,Consider_For_Obligations as ConsiderForObligations,ISNULL(b.InterestRate,'') as InterestRate,MonthsOnBook,ISNULL((CAST(INSTALMENTpERIOD AS INT)-CAST(rEMAININGemi AS INT)),'') as NoOfRepaymentDone,'' as TypeofOD,'' as PreviousLoanTAI ,'' as PreviousLoanDBR,'' as  Creditlimit from ng_RLOS_CUSTEXPOSE_CardDetails a left join ng_RLOS_CUSTEXPOSE_CardInstallmentDetails  b with (nolock) on CardEmbossNum=replace(CardNumber,'I','') and a.Wi_Name=b.Wi_Name and a.SchemeCardProd  in ('LOC STANDARD','LOC PREFERRED') and (a.Request_Type = 'InternalExposure' or a.Request_Type = 'CollectionsSummary' or b.Request_Type='CARD_INSTALLMENT_DETAILS') where  a.Child_Wi ='"+winame+"' and CustRoleType !='Secondary'";
	}else{
		sQry="select distinct 'ng_RLOS_CUSTEXPOSE_CardDetails' as table_name,CardType as ProductType,A.CifId,Liability_type as LiabilityType,SchemeCardProd as TypeofCardLoan,CardEmbossNum as AgreementId,CreditLimit as LimitAmtFin,  (case when (a.SchemeCardProd = 'LOC PREFERRED' or a.SchemeCardProd = 'LOC STANDARD') then isnull(b.MonthlyAmount,0) ELSE ISnull(PaymentsAmount,0) End) as EMI ,General_Status,Limit_Increase as LimitIncrease,OutstandingAmt as OutstandingAmount,Consider_For_Obligations as ConsiderForObligations,ISNULL(b.InterestRate,'') as InterestRate,MonthsOnBook,ISNULL((CAST(INSTALMENTpERIOD AS INT)-CAST(rEMAININGemi AS INT)),'') as NoOfRepaymentDone,'' as TypeofOD,'' as PreviousLoanTAI ,'' as PreviousLoanDBR,'' as  Creditlimit from ng_RLOS_CUSTEXPOSE_CardDetails a left join ng_RLOS_CUSTEXPOSE_CardInstallmentDetails  b with (nolock) on CardEmbossNum=replace(CardNumber,'I','') and a.Wi_Name=b.Wi_Name and a.SchemeCardProd  in ('LOC STANDARD','LOC PREFERRED') and (a.Request_Type = 'InternalExposure' or a.Request_Type = 'CollectionsSummary' or b.Request_Type='CARD_INSTALLMENT_DETAILS') where  a.Child_Wi ='"+winame+"' and CustRoleType !='Secondary'";
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
			 var dhtml="";
			  	 dhtml+="<div style='overflow-x:scroll;overflow-y:scroll;'>"+
				"<table id='mytab' border='0' bordercolor='#337ab7' max-height='500px'>";
			if(activityName == 'CC'){
			activityName = 'PL';
			}
			var sQry="select col_name ,col_type,max_len,col_id,default_value,is_readonly,ActivityName from NG_Dynamic_Grid_config with (nolock) WHERE Grid_name = 'Internal_Liability' and ActivityName='"+activityName+"'ORDER BY col_seq";
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
		var table_row ="<td><input type='hidden' id ='datatablename_"+row_num+"' name='datatablename' value='"+val_new[0]+"' ></td>";
		var product_type = "";
			var cardType = "";
		for(var i=0;i<row_arr.length-1;i++){
			temp = row_arr[i];
			col_id_arr=temp.split("#");
			InputField_id = InputField_id+","+col_id_arr[3];
			if(col_id_arr[1]=="text"){
				if(col_id_arr[3].indexOf('Product_Type')>-1){
				product_type = val_new[i+1];
				table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+val_new[i+1]+"' maxlength ='"+col_id_arr[2]+"'"+col_id_arr[5]+">"+"</td>";
				}
				else if(col_id_arr[3].indexOf('CardType')>-1){
				cardType = val_new[i+1];
				table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+val_new[i+1]+"' maxlength ='"+col_id_arr[2]+"'"+col_id_arr[5]+">"+"</td>";
				}
				
				else if(col_id_arr[3].indexOf("EMI")>-1){
					if(product_type.indexOf('Card')>-1 && cardType.indexOf('LOC')==-1){
						table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' value='' maxlength ='"+col_id_arr[2]+"'"+col_id_arr[5]+">"+"</td>";
					}
					else{
						table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+val_new[i+1]+"' maxlength ='"+col_id_arr[2]+"'"+col_id_arr[5]+">"+"</td>";
					}
				}
				else{
					table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+val_new[i+1]+"' maxlength ='"+col_id_arr[2]+"'"+col_id_arr[5]+">"+"</td>";
					
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
			if(value == "true"){
			table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+col_id_arr[4]+" "+col_id_arr[5]+" onclick = 'LimitincreaseIndicatorLogic(this.id)' checked >"+"</td>";
			}
			else if(value == 'false' && col_id_arr[4] == 'checked'){//case for consider for obligations.
			table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+col_id_arr[5]+" onclick = 'LimitincreaseIndicatorLogic(this.id)' >"+"</td>";
			}
			else{
			table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+col_id_arr[4]+" "+col_id_arr[5]+" onclick = 'LimitincreaseIndicatorLogic(this.id)' >"+"</td>";
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
	if(id.indexOf('Limit_Increase')>-1){
			var arr = id.split('_');
			var rownum = arr[arr.length - 1];
			if(document.getElementById(id).checked || document.getElementById(id).checked == 'true'){
				if(window.parent.document.getElementById('Application_Type').value=='TOPE' || window.parent.document.getElementById('Application_Type').value=='TOPN' || window.parent.document.getElementById('Application_Type').value=='TOPIM' || window.parent.document.getElementById('Application_Type').value=='RESCE' || window.parent.document.getElementById('Application_Type').value=='RESCN' ) {
					if(document.getElementById('Consider_For_Obligations_'+rownum).checked || document.getElementById('Consider_For_Obligations_'+rownum).checked == 'true'){
						document.getElementById('Consider_For_Obligations_'+rownum).checked = false;
						document.getElementById('Consider_For_Obligations_'+rownum).disabled = true;
					}
				}
			}
			else{
			if(window.parent.document.getElementById('Application_Type').value=='TOPE' || window.parent.document.getElementById('Application_Type').value=='TOPN' || window.parent.document.getElementById('Application_Type').value=='TOPIM' || window.parent.document.getElementById('Application_Type').value=='RESCE' || window.parent.document.getElementById('Application_Type').value=='RESCN' ) {
					if(!document.getElementById('Consider_For_Obligations_'+rownum).checked || document.getElementById('Consider_For_Obligations_'+rownum).checked == 'false'){
						document.getElementById('Consider_For_Obligations_'+rownum).checked = true;
						document.getElementById('Consider_For_Obligations_'+rownum).disabled = false;
					}
				}
				
			}
	}
}

function openPopup(id)
	{
	
	var agreementval=document.getElementById(id).innerHTML;
	var arr = id.split('_');
	var rownum = arr[1];
	var tablename = document.getElementById('datatablename_'+rownum).value;
	//alert(agreementval);
	//WriteLog("appServerType jsp: result: "+agreementval);
	var url='/webdesktop/resources/scripts/internal.jsp?Wi_Name='+winame+'&AgreementId='+agreementval+'&table='+tablename;
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
