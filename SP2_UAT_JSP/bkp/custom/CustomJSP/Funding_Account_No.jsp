<!------------------------------------------------------------------------------------------------------
//           NEWGEN SOFTWARE TECHNOLOGIES LIMITED

//Group						 : Application ?Projects
//Product / Project			 : RAKBank-RLOS
//File Name					 : Funding Account No
//Author                     : Disha
//DESC						 : To generate and save Editable Grid JSP
//Date written (DD/MM/YYYY)  : 13/04/2017
//---------------------------------------------------------------------------------------------------->


<%@ page import="java.io.*,java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.lang.String.*"%>
<%@ page import="java.lang.Object"%>
<%@ page import="com.newgen.wfdesktop.xmlapi.*" %>
<%@ page import="com.newgen.wfdesktop.util.*" %>
<%@ page import="com.newgen.omni.jts.cmgr.XMLParser"%>
<%@ page import="com.newgen.mvcbeans.model.*,javax.faces.context.FacesContext,com.newgen.mvcbeans.controller.workdesk.*"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.owasp.esapi.ESAPI"%>
<%@ page import="org.owasp.esapi.codecs.OracleCodec"%>
<%@ page import="org.owasp.esapi.User" %>
<%@ page import="com.newgen.*" %>
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
	    
	String input1 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("WD_UID"), 1000, true) );
	String WD_UID = ESAPI.encoder().encodeForSQL(new OracleCodec(), input1);
	
	String input2 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("ChecklistAbbr"), 1000, true) );
	String ChecklistAbbr = ESAPI.encoder().encodeForSQL(new OracleCodec(), input2);
	
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
        <title>Funding Account</title>
        <link href="${pageContext.request.contextPath}/customJS/checklistCSS.css" rel="stylesheet" type="text/css">
  
		<script src="${pageContext.request.contextPath}/customJS/jquery/jquery-1.12.3.js"></script>	
		<script src="${pageContext.request.contextPath}/customJS/jquery/jquery-ui.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/customJS/jquery/jquery-ui.css" type="text/css" />
<script src="${pageContext.request.contextPath}/customJS/jquery/jquery-ui.min.js"></script>


 
<!-- esapi4js i18n resources -->
<script type="text/javascript" language="JavaScript" src="esapi4js/resources/i18n/ESAPI_Standard_en_US.properties.js"></script>
<!-- esapi4js configuration -->
<script type="text/javascript" language="JavaScript" src="esapi4js/esapi-compressed.js"></script>
<script type="text/javascript" language="JavaScript" src="esapi4js/resources/Base.esapi.properties.js"></script>
<!-- esapi4js core -->

<script type="text/javascript">



//select distinct(groupindex) from PDBGroupMember where UserIndex=1 and GroupIndex in (1,10)


    Base.esapi.properties.application.Name = "My Application v1.0";
    
    
    // Initialize the api
    org.owasp.esapi.ESAPI.initialize();
	
var Cab_Name = '<%=sCabname%>';
	var Session_ID = '<%=sSessionId%>';
	var User_Name = '<%=sUserName%>';
	var User_Index = '<%=UserId%>';
	var sJtsIp = '<%=sJtsIp%>';
	var iJtsPort = '<%=iJtsPort%>';
	var winame = window.parent.workitemName;
	//var winame= window.parent.pid;
	var activityName = winame.substring(0,2);
	
	var WD_UID_encode = '<%=WD_UID%>';
	var WD_UID = ($ESAPI.encoder().decodeForHTML(WD_UID_encode));
	
	var ChecklistAbbr_encode = '<%=ChecklistAbbr%>';
	var ChecklistAbbr = ($ESAPI.encoder().decodeForHTML(ChecklistAbbr_encode));
	
	var noOfBorrower=0;
	var noOfGuarantor=0;
	var WiDetails="";
	var ChecklistDetails="";
	var PrevResCol1="";
	var pResStr="";
	var rResStr="";	
	var ResCol1="";
	var InputField_id="";
	var table_name="ng_RLOS_CUSTEXPOSE_AcctDetails";
	var row_id_arr=[];
	var data_save_var="";
	var ws_name = window.parent.activityName;
	var pname="FAN";
	  
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

function saveData(showAlert) {
    	var totalrowid= $('table#mytab tr:last').index();
		var final_data = "";
		var new_row = "Y";
		var row_arr = WiDetails.split("~");		
		var stable="ng_RLOS_CUSTEXPOSE_AcctDetails";
		var scolumnname = "";
		var scolumnsvalues = "";
		var swhere="";
		var update_result="";
		
		for(var row_arr_count= 1;row_arr_count<=totalrowid;row_arr_count++){
				var value = "";
				data_save_var_arr = data_save_var.split(",");
				for(var x= 0 ; x<data_save_var_arr.length ; x++){
					var variable_id = data_save_var_arr[x]+"_"+row_arr_count;
					var AcctId = "AcctId_"+row_arr_count;
					
					if(activityName=='CA'){
					swhere = "Wi_Name = '"+winame+"'  and AcctId ='"+document.getElementById(AcctId).value+"'";
					}
					else {
					swhere = "Child_wi = '"+winame+"'  and AcctId ='"+document.getElementById(AcctId).value+"'";
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
				var params="table="+stable+"&columnsNames="+data_save_var+"&columnsValues="+value+"&swhere="+swhere+"&WD_UID="+WD_UID;
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
			if(update_flag==0 && showAlert=='Y' && hiddenFlag!='true'){
				alert('Funding Account Data saved!!');
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
        window.moveTo(0, 0);
        window.resizeTo(screen.availWidth, screen.availHeight);
		fetchValidQueueData();	  
		fetchGridData();
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
	//var sQry="select TypeOfContact,ProviderNumber,Limit1,EMI,TakeOverIndicator,TakeOvrAmount,CACIndicator,QCAmt,QCEMI,OutStandingAmount,ConsiderForObligations,Remarks,WorstStatus,PrevNoOfInstallments,cleanFunded  FROM  "+table_name+" with (nolock) WHERE wi_name = '"+winame+"'";
	if(activityName=='CA'){
		sQry="CA";
		//sQry="SELECT fundingAccount,AcctId,AcctType,AcctNm,AccountOpenDate,AcctSegment,AcctSubSegment,AcctStat,CreditGrade FROM ng_RLOS_CUSTEXPOSE_AcctDetails  with (nolock) where Wi_Name = '"+winame+"' and Request_Type = 'InternalExposure' and (AcctType = 'CURRENT ACCOUNT' or AcctType = 'AMAL CURRENT ACCOUNT' or AcctType = 'SAVINGS ACCOUNT' or AcctType = 'AMAL SAVINGS ACCOUNT' or AcctType = 'AMAL JOOD SAVINGS ACCOUNT') and AcctStat='Active'";//quwery modified by akshay on 25/6/18 for proc 7731
		wparams="Wi_Name=="+winame+"~~Request_Type==InternalExposure";
	}else{
		sQry="!CA";
		//sQry="SELECT fundingAccount,AcctId,AcctType,AcctNm,AccountOpenDate,AcctSegment,AcctSubSegment,AcctStat,CreditGrade FROM ng_RLOS_CUSTEXPOSE_AcctDetails  with (nolock) where Child_Wi = '"+winame+"' and Request_Type = 'InternalExposure' and (AcctType = 'CURRENT ACCOUNT' or AcctType = 'AMAL CURRENT ACCOUNT' or AcctType ='SAVINGS ACCOUNT' or AcctType = 'AMAL SAVINGS ACCOUNT' or AcctType = 'AMAL JOOD SAVINGS ACCOUNT') and AcctStat='Active'";//quwery modified by akshay on 25/6/18 for proc 7731
		wparams="Child_Wi=="+winame+"~~Request_Type==InternalExposure";
	}
	
			params="Query="+sQry+"&wparams="+wparams+"&pname="+pname+"&WD_UID="+WD_UID;
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
			else if(result.trim()=='NODATAFOUND' && ws_name=='CAD_Analyst1' && window.parent.getNGValue('cmplx_Customer_NTB')==false)
			{
				window.parent.showAlert('',"No Account found...Refer the workitem back to the RM CSO!!");
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
			  	 dhtml+="<div style='overflow-x:scroll;overflow-y:scroll;'>"+
				"<table id='mytab' border='0' bordercolor='#337ab7' max-height='500px'>";
			
			//var sQry="select col_name ,col_type,max_len,col_id,default_value,is_readonly,ActivityName from NG_Dynamic_Grid_config with (nolock) WHERE Grid_name = 'Funding_Account_No' and ActivityName='"+activityName+"'ORDER BY col_seq";
			var sQry="QueueData";
			wparams="Grid_name==Funding_Account_No~~ActivityName=="+activityName;
			params="Query="+sQry+"&wparams="+wparams+"&pname="+pname+"&WD_UID="+WD_UID;
			
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
		var table_row ="";
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
				if(value == "true"){
					table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' "+col_id_arr[4]+" "+col_id_arr[5]+"  checked >"+"</td>";
				}
				else{
					table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' value='"+col_id_arr[4]+"' "+col_id_arr[5]+">"+"</td>";
				}
			}
			else if(col_id_arr[1]=="link"){
				table_row = table_row +"<td>"+"<a id='"+col_id_arr[3]+"_"+row_num+"' href='#' onclick='openPopup();return false;'>1000</a>"+"</td>";
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
				var value = val_new[i];
				if(value == "true"){
					table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' onchange = 'check_change(this.id);' "+col_id_arr[4]+" "+col_id_arr[5]+"  checked >"+"</td>";
				}
				else{
					table_row = table_row +"<td>"+"<input id = '"+col_id_arr[3]+"_"+row_num+"' type = '"+ col_id_arr[1] +"' name = '"+col_id_arr[3]+"_"+row_num+"' onchange = 'check_change(this.id);' value='"+col_id_arr[4]+"' "+col_id_arr[5]+">"+"</td>";
				}
			}
			else if(col_id_arr[1]=="link"){
				table_row = table_row +"<td>"+"<a id='"+col_id_arr[3]+"_"+row_num+"' href='#' onclick='openPopup();return false;'>1000</a>"+"</td>";
			}
		}
	}
	return "<tr background-color:#e01414 class='HeaderClass' id = row_"+row_num+" ><p align='right'>"+table_row+"</tr>";
}

function check_change(obj_id){
	var totalrowid= $('table#mytab tr:last').index();
		var is_Account_Checked=document.getElementById(obj_id).checked;
		var current_row=obj_id.split('_')[1];
		var acct_no = "AcctId_"+current_row;
		if(is_Account_Checked==true){
			for(var row_arr_count= 1;row_arr_count<=totalrowid;row_arr_count++){
				var selectedAcct = "FundingAccount_"+row_arr_count;
				if(row_arr_count!=current_row){
					document.getElementById(selectedAcct).checked= false;
				}
			}
		}
		if(is_Account_Checked==true){
			document.getElementById(obj_id).checked= true;
			window.parent.com.newgen.omniforms.formviewer.setEnabled('AlternateContactDetails_retainAccount',false);
			//if(document.getElementById(acct_no_name).value!=''){
				window.parent.com.newgen.omniforms.formviewer.setNGValue('Account_Number',document.getElementById(acct_no).value);
			//}
		}
		else{
			window.parent.com.newgen.omniforms.formviewer.setEnabled('AlternateContactDetails_retainAccount',true);
			window.parent.com.newgen.omniforms.formviewer.setNGValue('Account_Number','');
		}
		saveData('N');
}
function saveDate()
	{
	saveData('Y');
	}
	
function openPopup()
	{
	var url="/webdesktop/custom/CustomJSP/external.html";
	window.open(url, "", "width=500,height=500");
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
					{
						alert("Funding_Account_No : URL doesn't exist!");
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
		alert("Funding_Account_No : "+e.message);
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
