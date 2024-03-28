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
		for(var i=0;i<row_id_arr.length;i++){
			InputField_id_arr = InputField_id.split(",");
			if(i>0)
				final_data = final_data+";";
			var new_row = "Y";	
			for(var j=1;j<InputField_id_arr.length;j++){
				var fld_name= InputField_id_arr[j]+"_"+row_id_arr[i];
				if(final_data!="" && new_row=='N')
					final_data = final_data+","+document.getElementById(fld_name).value;
				else if(final_data!="" && new_row=='Y')
					final_data = final_data+document.getElementById(fld_name).value;
				else
					final_data = document.getElementById(fld_name).value;
				new_row = "N";
			}
		}
			//jspName="exec_procdure.jsp";
			//var params="wi_name="+winame+"&table_name="+table_name+"&col_name_list="+InputField_id_arr+"&col_val="+final_data;
			//result=CallAjax(jspName,params);
			//alert("data update result: "+ result);
		
		//alert("final_data: "+ table_name+";"+InputField_id_arr+";"+final_data);	
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
		fetchInternalGridData(); //Arun (29/10/17) added new method
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
	if(activityName=="CC"||activityName=="Cr"||activityName=="PL"){
	var sQry="select AgreementId as reference,LoanType as Product_Type,'',Datelastupdated as DLP,'',Consider_For_Obligations,SchemeCardProd,TotalAmt as LimAmt,PaymentsAmt as EMI,NoOfDaysInPipeline,Take_Over_Indicator as TKO,Take_Amount as TK from ng_rlos_cust_extexpo_LoanDetails where child_Wi = '"+winame+"' and LoanStat = 'Pipeline' or LoanStat = 'CAS-Pipeline' union select CardEmbossNum as reference,CardType as Product_Type,'',LastUpdateDate as DLP,'',Consider_For_Obligations,SchemeCardProd,TotalAmount as LimAmt,PaymentsAmount as EMI,NoOfDaysInPipeLine,'' as TKO,'' as TK from ng_rlos_cust_extexpo_CardDetails where child_Wi = '"+winame+"' and CardStatus = 'Pipeline' or CardStatus = 'CAS-Pipeline'";
	}
	else{
	var sQry="select AgreementId as reference,LoanType as Product_Type,'',Datelastupdated as DLP,'',Consider_For_Obligations,SchemeCardProd,TotalAmt as LimAmt,PaymentsAmt as EMI,NoOfDaysInPipeline,Take_Over_Indicator as TKO,Take_Amount as TK from ng_rlos_cust_extexpo_LoanDetails where Wi_Name = '"+winame+"' and LoanStat = 'Pipeline' or LoanStat = 'CAS-Pipeline' union select CardEmbossNum as reference,CardType as Product_Type,'',LastUpdateDate as DLP,'',Consider_For_Obligations,SchemeCardProd,TotalAmount as LimAmt,PaymentsAmount as EMI,NoOfDaysInPipeLine,'' as TKO,'' as TK from ng_rlos_cust_extexpo_CardDetails where Wi_Name = '"+winame+"' and CardStatus = 'Pipeline' or CardStatus = 'CAS-Pipeline'  union Select AgreementID as reference,LoanType,'','','',Consider_For_Obligations,SchemeCardProd,TotalAmount,'','','','' from ng_RLOS_CUSTEXPOSE_LoanDetails where Wi_Name = '"+winame+"' and LoanStat = 'Pipeline' or LoanStat = 'CAS-Pipeline'";
	}
	
	
	//fetchdataCustom(sQry);
		jspName="CustomSelect.jsp";
			params="Query="+sQry;
		
			console.log(params);
			
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

function fetchInternalGridData()
{
	jspName="CustomSelect.jsp";
			var params="";
			var result="";
	
	
	if(activityName=="CC"||activityName=="Cr"||activityName=="PL"){
	var sQry="Select AgreementID as reference,LoanType,'','','',Consider_For_Obligations,SchemeCardProd,TotalAmount,'','','','' from ng_RLOS_CUSTEXPOSE_LoanDetails where Child_Wi = '"+winame+"' and LoanStat = 'Pipeline' or LoanStat = 'CAS-Pipeline'";
	}
	else
	{
	var sQry="Select AgreementID as reference,LoanType,'','','',Consider_For_Obligations,SchemeCardProd,TotalAmount,'','','','' from ng_RLOS_CUSTEXPOSE_LoanDetails where Wi_Name = '"+winame+"' and LoanStat = 'Pipeline' or LoanStat = 'CAS-Pipeline'";
	}
	
	
	//fetchdataCustom(sQry);
	
			params="Query="+sQry;
		
			console.log(params);
			
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
			var sQry="select col_name ,col_type,max_len,col_id,default_value,is_readonly,ActivityName from NG_Dynamic_Grid_config with (nolock) WHERE Grid_name = 'Pipeline' and ActivityName='"+activityName+"'ORDER BY col_seq";
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
			 dhtml+="<input type='button' value='save data' style='background-color:#e01414; color:#ffffff' onclick='saveDate()'/></div></table>";//<image src='addImage.png' height='15px' width='15px' id='addimage1"+"' style='padding-right:10px' onclick='addClick(this)'/>
	
	document.getElementById("mainDivContent").innerHTML=dhtml;
}

function tb_header(){
	var row_arr = WiDetails.split("~");
	var col_arr;
	var temp = "";
	var table_header ="";//<th><label color:#ffffff><label>select</label></th>
	var table_col = "<td>";
		
	for(var i=0;i<row_arr.length-1;i++){
		temp = row_arr[i];
		col_arr=temp.split("#");
		table_header = table_header +"<th><label color:#ffffff><label>"+col_arr[0]+"</label></th>";
	}
	return "<tr background-color:#e01414><p align='right'>"+table_header+"</tr>";
}



function tb_row(row_num, val){
	var row_arr = WiDetails.split("~");
	var col_id_arr="";
	var temp = "";
	InputField_id="WI_Name";
	//alert("val is:" +val);
	if(val==""){
		
		var table_row ="";//<td><input type='checkbox' id ='check_"+row_num+"' name='check'></td>
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
				table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+col_id_arr[4]+"' "+col_id_arr[5]+">"+"</td>";
			}
		}
	}
	//alert("0:"+col_id_arr[0]+","+"1:"+col_id_arr[1]+","+"2:"+col_id_arr[2]+","+"3:"+col_id_arr[3]+","+"4:"+col_id_arr[4]+","+"5:"+col_id_arr[5]+","+"6:"+col_id_arr[6]+","+"length:"+col_id_arr.length);
	else{
		var val_new = val.split("#");
		var table_row ="";//<td><input type='checkbox' id ='check_"+row_num+"' name='check'></td>
		for(var i=0;i<row_arr.length-1;i++){
			temp = row_arr[i];
			col_id_arr=temp.split("#");
			InputField_id = InputField_id+","+col_id_arr[3];
			if(col_id_arr[1]=="text"){
				table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+val_new[i]+"' maxlength ='"+col_id_arr[2]+"'"+col_id_arr[5]+">"+"</td>";
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
				table_row = table_row +"<td>"+"<a id='"+col_id_arr[3]+"_"+row_num+"' href='#' onclick='openPopup();return false;'>"+val_new[i]+"</a>"+"</td>";
			}
		}
	}
	
	return "<tr background-color:#e01414 class='HeaderClass' id = row_"+row_num+" ><p align='right'>"+table_row+"</tr>";
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
