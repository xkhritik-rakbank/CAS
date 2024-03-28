<%@ include file="Log.process"%>
<%@ page import="java.io.*,java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.lang.String.*"%>
<%@ page import="java.lang.Object"%>
<%@ page import="com.newgen.wfdesktop.xmlapi.*" %>
<%@ page import="com.newgen.wfdesktop.util.*" %>
<%@ page import="XMLParser.XMLParser"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.newgen.mvcbeans.model.*,javax.faces.context.FacesContext,com.newgen.mvcbeans.controller.workdesk.*"%>
<%@page import="com.newgen.omni.wf.util.excp.NGException,com.newgen.omni.wf.util.app.NGEjbClient"%>
<%@page import="org.w3c.dom.Node"%>
<%@page import="org.w3c.dom.NodeList"%>
<%@ page  import= "com.newgen.custom.*, org.w3c.dom.*, org.w3c.dom.NodeList,org.w3c.dom.Document,javax.xml.parsers.DocumentBuilder, javax.xml.parsers.DocumentBuilderFactory"%>
<%@ page  import= "org.w3c.dom.Element"%>
<%@ page import= "org.w3c.dom.Document"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.owasp.esapi.ESAPI"%>
<%@ page import="org.owasp.esapi.codecs.OracleCodec"%>
<%@ page import="org.owasp.esapi.User" %>
<%@ page import="com.newgen.*" %>

<jsp:useBean id="wDSession" class="com.newgen.wfdesktop.session.WDSession" scope="session"/>
<jsp:useBean id="WDCabinet" class="com.newgen.wfdesktop.baseclasses.WDCabinetInfo" scope="session"/>
<html xmlns="http://www.w3.org/1999/xhtml">
  
        <head>
            <link href="https://10.15.12.136:9443/omniapp/javax.faces.resource/themes.css.jsf?ln=en_us//css" rel="stylesheet" type="text/css">			
			
			<link rel="stylesheet" href="${pageContext.request.contextPath}/custom/css/jquery-ui.css" type="text/css" />
			<link rel="stylesheet" href="/formviewer/resources/en_us/css/RLOS/RLOS_CSS.css" type="text/css" />
			<link rel="stylesheet" href="${pageContext.request.contextPath}/custom/css/datatables.min.css" type="text/css" />
			<script language="javascript" src="/webdesktop/custom/js/jquery.min.js"></script>
			<script language="javascript" src="/webdesktop/custom/js/datatables.min.js"></script>
			
		

		</head>
		 <style>
    .heading #headingText{
        COLOR: #000000;
        FONT: bold 14pt verdana;
        padding:10px;
		text-align:center;
    }
	table tbody tr.highlight {
		background-color: rgba(200, 195, 193, 0.8);
		/*#3c8dbc;rgba(200, 195, 193, 0.8)*/
		color: black;
	}
	
	table.dataTable.select tbody tr,
		table.dataTable thead th:first-child {
		  cursor: pointer;
	}
 </style>
        <body bgcolor="white" vlink="white" onload='load_Pagination()'>
            <%
		String sInputXML="";
		String sOutputXML="";
		XMLParser xmlParserData=null;
		XMLParser objXmlParser=null;
		String mainCodeValue="";
		int recordcount=0;
		String WI_NAME="";
		String Cif_Id="";
		String Customer_Name="";
		String Application_type="";
		String Sub_Product="";
		String Card_Product="";
		String GrpName="";
		String subXML="";
		String cabinetName="";

		String wrapperPort="";
		String wrapperIP ="";
		String appServerType="";
		String params="";
		String MainCode="";
		String strInputXML="";
	       String strOutputXML="";
		String workItemDone="";
		String Is_murhabha_confirmed="";
		
		
		
		//String Wi_Name =(String) request.getParameter("Wi_Name");
		//WriteLog("winame in starting of jsp"+Wi_Name);
 
		    
		//String sessionId = request.getParameter("sSessionId");
		
		//String input1 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("sSessionId"), 1000, true) );	
		//String sessionId = ESAPI.encoder().encodeForSQL(new OracleCodec(), input1);
		String sessionId =  request.getParameter("sSessionId");
	
		String WD_UID = request.getParameter("WD_UID");
		
		//String input2 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("WD_UID"), 1000, true) );	
		//String WD_UID = ESAPI.encoder().encodeForSQL(new OracleCodec(), input2);
		if (sessionId == null) {
			  out.println("<html><title>Error</title><body><span style=\"margin-left:-9px;display: block\"><img src=\"ibps.png\"></span><label style=\"white-space: nowrap; font: bold 11pt Arial; color: #0072c6;padding-left: 580px;padding-top: 7px;display: inline-block;\" class=\"labelbluebold\">Invalid Session. Please login again.</label></body></html>");
				return;	    }
		

	
	
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
			boolean check=validateJSP(sessionId,cabinetName,wrapperIP, wrapperPort, appServerType);
			if(check==false){
				  out.println("<html><title>Error</title><body><span style=\"margin-left:-9px;display: block\"><img src=\"ibps.png\"></span><label style=\"white-space: nowrap; font: bold 11pt Arial; color: #0072c6;padding-left: 580px;padding-top: 7px;display: inline-block;\" class=\"labelbluebold\">Invalid Session. Please login again.</label></body></html>");
			return;
			}
			WriteLog("winame cabinetName "+check);
	
        } 
		catch(Exception e){
			
			WriteLog("Excetion Occured while reading from properties file: "+e.getMessage());
		}
		
	%>
	<%!
		public boolean validateJSP(String sessionId,String cabinetName,String wrapperIP,String wrapperPort,String appServerType){
			String query="select sessionId from WFSESSIONVIEW where sessionId='" +sessionId+ "' and statusFlag='Y'";
			String inputXML="";
			String OutputXML="";
			String params="";
			XMLParser xmlParserSession=null;
			inputXML = "<?xml version='1.0'?>"+
			 "<APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option>"+
			 "<Query>" + query + "</Query>"+
			 "<Params>"+ params + "</Params>"+
			 "<EngineName>" + cabinetName + "</EngineName>"+
			 "<SessionId>" + sessionId + "</SessionId></APSelectWithColumnNames_Input>";
			
			//inputXML = "<?xml version='1.0'?><APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option><Query>" + query + "</Query><EngineName>" + cabinetName + "</EngineName><SessionId>" + sessionId+ "</SessionId></APSelectWithColumnNames_Input>";

			WriteLog(" properties file: "+inputXML);

			try{
				OutputXML = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, inputXML);
			}
			catch(NGException e){
				WriteLog(" properties OutputXML"+OutputXML);
			}
			catch (Exception ex) 
			{
			   ex.printStackTrace();
			}
			WriteLog("outputXML " + OutputXML);
			try{
				xmlParserSession=new XMLParser();
				xmlParserSession.setInputXML((OutputXML));
				String mainCode = xmlParserSession.getValueOf("MainCode");
				WriteLog("mainCode " + mainCode);
				int recordcount=Integer.parseInt(xmlParserSession.getValueOf("TotalRetrieved"));
				WriteLog("recordcount " + recordcount);
				if(recordcount>0){
					  return true;
				}
			}catch (Exception ex) 
			{
				   ex.printStackTrace();
			}
			return false;
		}

	%>

<%	
		try {			
			//sessionId=    wDSession.getM_objUserInfo().getM_strSessionId();		
			
			WriteLog("winame appServerType "+appServerType);
			WriteLog("winame wrapperIP "+wrapperIP);
			WriteLog("winame wrapperPort "+wrapperPort);
			WriteLog("winame sessionIdv "+sessionId);
			WriteLog("winame cabinetName "+cabinetName);
	%>
	<div id="wrapper" style="height:100%;overflow:scroll">
    <div id="logo">
        <img src="../resources/images/rak-logo.gif" class="ribbon" style="height:50px;width:150px"/>
    </div>
    <div class="heading">
        <div id="headingText">Bulk Auto Disbursal Workitems</div>
    </div>	
    <div class="shade1">
    </div>
	 <form name="DisplayWi" id="DisplayWi" method="post" action=""  class="form-horizontal" style="margin-left:5%;width:80%">
		<div class="form-group">		
			<div style="height:80%;">
			<table id='cas_table' border = "1"  width = "100%" bgcolor="wheat">
			<thead>
				<tr  width = "100%">
					<th>Select</th>
					<th>WorkItem Name</th>
					<th>CIF ID</th>
					<th>Customer Name</th>
					<th>Sub Product</th>
					<th>Application Type</th>
					
				</tr>
			</thead>
			<tbody>
			<%
			/*String SqlQuery="SELECT distinct CC_Wi_Name, CIF_ID, CustomerName, loan_type, Sub_Product FROM NG_CC_EXTTABLE LEFT  JOIN ng_rlos_Murabha_Warranty ON cc_wi_name=Murhabha_WIName where CURR_WSNAME='Disbursal' AND (Sub_Product is not null and cif_id is not null and cif_id!='' and Product is not null) union all SELECT distinct pl_Wi_Name, CIF_ID, CustomerName, loan_type, Sub_Product FROM NG_pl_EXTTABLE LEFT  JOIN ng_rlos_Murabha_Warranty ON cc_wi_name=Murhabha_WIName where CURR_WSNAME='CC_Disbursal' AND (Sub_Product is not null and cif_id is not null and cif_id!='' and Product is not null)";	*/
			//Change for falcon
			//String SqlQuery="SELECT distinct CC_Wi_Name, CIF_ID, CustomerName, Sub_Product,application_type FROM NG_CC_EXTTABLE  left JOIN ng_rlos_Murabha_Warranty ON cc_wi_name=Murhabha_WIName where CURR_WSNAME='Disbursal' AND (Sub_Product is not null and cif_id is not null and cif_id!='') union all SELECT distinct pl_Wi_Name, CIF_ID, CustomerName, Sub_Product,application_type FROM NG_pl_EXTTABLE  left JOIN ng_rlos_Murabha_Warranty ON pl_wi_name=Murhabha_WIName where CURR_WSNAME='CC_Disbursal' AND (Sub_Product is not null and cif_id is not null and cif_id!='')";
			String SqlQuery="SELECT distinct CC_Wi_Name, CIF_ID, CustomerName, Sub_Product,application_type FROM NG_CC_EXTTABLE  left JOIN ng_rlos_Murabha_Warranty ON cc_wi_name=Murhabha_WIName where CURR_WSNAME='Disbursal' AND (Sub_Product is not null and cif_id is not null and cif_id!='') union all SELECT distinct CC_Wi_Name, CIF_ID, CustomerName, Sub_Product,application_type FROM NG_DOB_EXTTABLE  left JOIN ng_rlos_Murabha_Warranty ON cc_wi_name=Murhabha_WIName where CURR_WSNAME='Disbursal' AND (Sub_Product is not null and cif_id is not null and cif_id!='') union all SELECT distinct pl_Wi_Name, CIF_ID, CustomerName, Sub_Product,application_type FROM NG_pl_EXTTABLE  left JOIN ng_rlos_Murabha_Warranty ON pl_wi_name=Murhabha_WIName where delivery_status='W' AND (Sub_Product is not null and cif_id is not null and cif_id!='')";
			
			WriteLog("Input XML for selecting  WI : " + SqlQuery);
			 
					sInputXML = "<?xml version='1.0'?>"+
					 "<APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option>"+
					 "<Query>" + SqlQuery + "</Query>"+
					 "<Params>"+ params + "</Params>"+
					 "<EngineName>" + cabinetName + "</EngineName>"+
					 "<SessionId>" + sessionId + "</SessionId></APSelectWithColumnNames_Input>";
				  
				 WriteLog("Input XML for selecting  WI : " + sInputXML);
				sOutputXML = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, sInputXML);
				
				WriteLog("Ouput XML for selecting WI : " + sOutputXML); 
				
				 xmlParserData=new XMLParser();
					  xmlParserData.setInputXML((sOutputXML));
					  mainCodeValue = xmlParserData.getValueOf("MainCode");
					 WriteLog("After bulk disbursal,maincode "+ mainCodeValue);
					  recordcount=Integer.parseInt(xmlParserData.getValueOf("TotalRetrieved"));
				
				 if(mainCodeValue.equals("0")){
						 for(int k=0; k<recordcount; k++){
						  subXML = xmlParserData.getNextValueOf("Record");
						  objXmlParser = new XMLParser(subXML);
						  WI_NAME=objXmlParser.getValueOf("CC_Wi_Name");
						  Cif_Id=objXmlParser.getValueOf("CIF_ID");
						  Customer_Name=objXmlParser.getValueOf("CustomerName");
						  Sub_Product=objXmlParser.getValueOf("Sub_Product");
						  Application_type=objXmlParser.getValueOf("application_type");
						  //Card_Product=objXmlParser.getValueOf("Card_Product");
						  //Is_murhabha_confirmed=objXmlParser.getValueOf("IS_Murhabah_Confirm");
%>		
			
				<tr  width = "100%">
					<td><input type="checkbox" name = "checkboxes"  id='mbCheckbox_<%=k%>' onclick='getCellValue(this.id)' /> </td>
					<td id = 'WINAME_<%=k%>' name ="WINAME" class ="WINAME"><%=WI_NAME%></td>
					<td><%= Cif_Id%></td>
					<td><%= Customer_Name%></td>
					<td><%= Sub_Product%></td>
					<td><%= Application_type%></td>
					
				</tr>
			

<%			
				}
				}
				}catch(Exception e){
								e.printStackTrace();
								
							}
							
		
							%>
	
			</tbody>
			</table>
				
			
			<input type="checkbox" name='checkboxes[]' id="chckAll" onclick='checkAll(this.id)' style="width:16px;height:16px"><font size="3" ><b>Select All</b></font></input>
			<button type="submit" class="btn btn-primary" value="Done" onclick="completewi()" style="margin-top:10px;float:right">Done </button>
		</div>				
	</div>
		

		</form>
	
<!-- esapi4js i18n resources -->
<script type="text/javascript" language="JavaScript" src="esapi4js/resources/i18n/ESAPI_Standard_en_US.properties.js"></script>
<!-- esapi4js configuration -->
<script type="text/javascript" language="JavaScript" src="esapi4js/esapi-compressed.js"></script>
<script type="text/javascript" language="JavaScript" src="esapi4js/resources/Base.esapi.properties.js"></script>
<!-- esapi4js core -->
<script language="javascript" src="/webdesktop/resources/scripts/workdesk.js"></script>
	<script language="javascript">
	
		var selectedRecords={};
		var checkAllRecords={};
		var recordcount =<%=recordcount%>;
	//Base.esapi.properties.application.Name = "My Application v1.0";
    
    
    // Initialize the api
    //org.owasp.esapi.ESAPI.initialize();
	
	
		//var WD_UID_encode = '<%=WD_UID%>';
		//var WD_UID = ($ESAPI.encoder().decodeForHTML(WD_UID_encode));
		var WD_UID ='<%=WD_UID%>';
		//var sessionId_encode = <%=sessionId%>;
	//	var sessionId = ($ESAPI.encoder().decodeForHTML(sessionId_encode));
		var sessionId = '<%=sessionId%>';
		 

			
		function load_Pagination() {
			$('#cas_table')
			.DataTable({"pageLength": 10,"lengthMenu": [[5, 10, 50, -1], [5, 10, 50, "All"]],"order": [[ 1, "asc" ]],"drawCallback":function(){DoOnPageChange();}});
		}
		
		function DoOnPageChange()
		{
			//console.log((++i) +'page changed '+document.getElementById('FileName_8').innerHTML);
			var table=document.getElementById('cas_table');
				var rows=table.rows.length;
			if(document.getElementById('chckAll').checked==true)
			{
				for(var i=1;i<rows;i++)
				{
					if(table.rows.item(i).cells[7].innerHTML=='')
					{
						table.rows.item(i).cells[0].children[0].checked=true;
						var row_no=table.rows.item(i).cells[1].id.split('_')[1];
						var wi_name=document.getElementById('WINAME_'+row_no).innerHTML;
						checkAllRecords[row_no]=wi_name;
					}
				}
			}
			else{
				for(var i=1;i<rows;i++)
				{
					var row_no=table.rows.item(i).cells[1].id.split('_')[1];
					if(selectedRecords.hasOwnProperty(row_no)==false){
						table.rows.item(i).cells[0].children[0].checked=false;
					}
					delete checkAllRecords[row_no];
				}
			}
		}
		
		function getCellValue(id)
		{
			
			//var checkbox_id=this.id;
			var row_no=id.split('_')[1];
			if(document.getElementById(id).checked==true){
				
					var wi_name=document.getElementById('WINAME_'+row_no).innerHTML;
					selectedRecords[row_no]=wi_name;
				
			}
			else{
					delete selectedRecords[row_no];
					delete checkAllRecords[row_no];		
				}
		}
		
		
			function checkAll(id) {
		
			// var checkboxes = document.getElementsByTagName('input');
			// var row_count=<%=recordcount%>;//document.getElementById('cas_table').rows.length-1;
			 var ele=document.getElementById(id);
			 if (ele.checked) {
				 for (var i = 0; i < recordcount; i++) {
					 if(document.getElementById('mbCheckbox_'+i)!=null){
						 if(document.getElementById('mbCheckbox_'+i).checked==false){
							var wi_name=document.getElementById('WINAME_'+i).innerHTML;
							checkAllRecords[i]=wi_name;
							document.getElementById('mbCheckbox_'+i).checked =true;
							//document.getElementById('mbCheckbox_'+i).click();
							}
							
						}
					 }
				 }
			  else {
				  for (var i = 0; i < recordcount; i++) {
					 if(document.getElementById('mbCheckbox_'+i)!=null){
						 document.getElementById('mbCheckbox_'+i).checked =false;
						checkAllRecords={};
						selectedRecords={};
					 }
				 }
			 }
			 
		 }
				
	function completewi()
	{
			//var recordcount =<%=recordcount%>;
			
			//var checkedindex =[];
			var x=0;
			//var wi =[];
			var temp="";
			var Murabha_flag="";
			var wi_names=[];
			var loggedInUser="";
			/*for(var y=0;y<recordcount;y++)
			{
				if(document.getElementById("mbCheckbox_"+y).checked == true)
				{
				  checkedindex[x] =y;
				  x++;
				}
			}*/
			if(Object.keys(checkAllRecords).length>0){
				for (var prop in checkAllRecords) {
					wi_names[x++]=checkAllRecords[prop];
				}
			}	

			else if(Object.keys(selectedRecords).length>0){
				for (var prop in selectedRecords) {
					wi_names[x++]=selectedRecords[prop];
				}
			}
			
			if(wi_names.length>0){
				for(var z=0;z<wi_names.length;z++)
				{
					var query="SELECT warranty.card_product,'' as Login_user_id ,CASE WHEN  combined_limit='0' THEN isnull(IS_Murhabah_Confirm,'N') ELSE 'NA' END as IS_Murhabah_Confirm FROM ng_rlos_Murabha_Warranty warranty JOIN ng_rlos_IGR_Eligibility_CardLimit limit ON child_wi=murhabha_winame AND warranty.card_product=limit.card_product WHERE murhabha_winame='"+wi_names[z]+"'";
					
					var output=getDataFromDB(query,'');
					if(Object.keys(output).length>0){
						for (var prop in output) {
							var value = output[prop].split("!!#")[0];
							loggedInUser = output[prop].split("!!#")[1];
							if(value=='' || value=='N'){
								alert('Murhabha File not confirmed for workitem :'+wi_names[z]+" and Card Product :"+prop);
								Murabha_flag='N';
								break;
							}
							else{
									Murabha_flag="Y";
								}
						}
					}
					else{
						Murabha_flag="Y";					
					}	
					
					
					if(Murabha_flag=='Y'){
							temp=temp+"~"+wi_names[z];
					}
				
				}	
				
				/*if(document.getElementById("Is_murhabha_confirmed_"+checkedindex[z]).innerHTML!=''){
					var s1=document.getElementById("WINAME_"+checkedindex[z]+"").innerHTML;
					for(var i=0;i<recordcount;i++)
					{
						if(document.getElementById("WINAME_"+i+"").innerHTML==s1 && document.getElementById("Is_murhabha_confirmed_"+i+"").innerHTML=='')	
						{
							alert('Murhabha File not confirmed for workitem :'+document.getElementById("WINAME_"+i).innerHTML+" and Card Product :"+document.getElementById("Card_Product_"+i).innerHTML);
							duplicateWI_flag="Y";
							break;
						}
										
					}
					if(duplicateWI_flag=='N'){
						temp=temp+"~"+s1;
					}		
					
				}*/
				
				if(temp!=''){
					temp=temp.substring(1, temp.length);
					integration_all(temp,loggedInUser);
				}
			}	
			else{
					alert('Please select a record first!!');
			}

}	
		   
		   function integration_all(winame,loggedInUser)
		   {
				var ajaxReq;
				var output;
				
				if (window.XMLHttpRequest) 
				{
					ajaxReq= new XMLHttpRequest();
				}
				else if (window.ActiveXObject)
				{
					ajaxReq= new ActiveXObject("Microsoft.XMLHTTP");
				}
				//alert("Here--------------------------");
				
				var url='/webdesktop/custom/BulkCompleteWi.jsp?wi_name=' + winame + '&sessionId=' + sessionId + '&WD_UID='+WD_UID + '&loggedInUser='+loggedInUser;
				//alert("After----------"+url);
				//window.open(url);
				ajaxReq.open("POST", url, false);
				ajaxReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
				ajaxReq.send(null);
				if (ajaxReq.status == 200 && ajaxReq.readyState == 4)
				{
					output=trimStr(ajaxReq.responseText);
					if(output=='Yes')
					{
						alert('Workitem done succesfully !!');
							 window.location.reload();
					}
					else{
						alert(output);
						return false;
					}
				}
				
				else
				{
					alert('Exception occured');
					return false;
				}
		   }
		   
	/*function getDataFromDB(query,columnName,winame){
		var result = '';
		var wparams="WiName=="+winame;
		var jspName="/webdesktop/custom/CustomJSP/CustomSelect.jsp";
		//var params="Query="+query+'&sessionId='+sessionId;
		
		var pname = "BulkDisplayWI";
		var	params="Query="+query+"&wparams="+wparams+"&pname="+pname;
		alert(params);
		result=CallAjax1(jspName,params);
		alert(result);
		if(result.indexOf("FAIL")!=-1 && result.indexOf("#FAIL#")==-1)
		{
			alert("Some error Encountered. Please try after some time");
			return;
		}
		else if(result.indexOf("FailedException")!=-1)
		{
			alert("Some Exception Occurred. Please try after some time");
			return;
		}
		else{
			var result_map={};
			var row_data=trimStr(result).split('~');
			for(var i=0;i<row_data.length-1;i++){
				var card_product=row_data[i].split('#')[0];
				var murabha_confirm=row_data[i].split('#')[1];
				result_map[card_product]=murabha_confirm;
			}	
			return result_map;
		}

}*/

function getDataFromDB(query,columnName){
		var result = '';
		var jspName="/webdesktop/custom/CustomSelect.jsp";
		var params="Query="+query+'&sessionId='+sessionId;
		result=CallAjax1(jspName,params);
		//alert(result);
		if(result.indexOf("FAIL")!=-1 && result.indexOf("#FAIL#")==-1)
		{
			alert("Some error Encountered. Please try after some time");
			return;
		}
		else if(result.indexOf("FailedException")!=-1)
		{
			alert("Some Exception Occurred. Please try after some time");
			return;
		}
		else{
			var result_map={};
			var row_data=trimStr(result).split('~');
			for(var i=0;i<row_data.length-1;i++){
				var card_product=row_data[i].split('#')[0];
				var murabha_confirm=row_data[i].split('#')[2];
				var login_user=row_data[i].split('#')[1];
				result_map[card_product]=murabha_confirm+"!!#"+login_user;
				
			}	
			return result_map;
		}

}

function CallAjax1(jspName,params)
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
	

function trimStr(str) {
                       return str.replace(/^\s+|\s+$/g, '');
            }

</script>
        </body>

</html>