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
			<!-- esapi4js i18n resources -->
<script type="text/javascript" language="JavaScript" src="esapi4js/resources/i18n/ESAPI_Standard_en_US.properties.js"></script>
<!-- esapi4js configuration -->
<script type="text/javascript" language="JavaScript" src="esapi4js/esapi-compressed.js"></script>
<script type="text/javascript" language="JavaScript" src="esapi4js/resources/Base.esapi.properties.js"></script>
<!-- esapi4js core -->
			
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
        <body bgcolor="white" vlink="white" onload='load()'>
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
		String Product_Name="";
		String Sub_Product="";
		String Card_Product="";
		String subXML="";
		String cabinetName="";
		String wrapperPort="";
		String wrapperIP ="";
		String appServerType="";
		String params="";
		String cas_generate_FileName="";
		String status="";
		//String sessionId = request.getParameter("sSessionId");
		
		String input1 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("sSessionId"), 1000, true) );
		String sessionId = ESAPI.encoder().encodeForSQL(new OracleCodec(), input1);

		String input2 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("WD_UID"), 1000, true) );
		String WD_UID = ESAPI.encoder().encodeForSQL(new OracleCodec(), input2);

	
	
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
        } 
		catch(Exception e){
			
			WriteLog("Excetion Occured while reading from properties file: "+e.getMessage());
		}
		
	%>

<%	
		try {			
			WriteLog("winame appServerType "+appServerType);
			WriteLog("winame wrapperIP "+wrapperIP);
			WriteLog("winame wrapperPort "+wrapperPort);
			WriteLog("Inside MurhabaIslamicCSV sessionId: "+sessionId);
			WriteLog("winame cabinetName "+cabinetName);
	%>
	<div id="wrapper" style="height:100%;overflow:scroll">
    <div id="logo" >
        <img src="../resources/images/rak-logo.gif" class="ribbon" style="height:50px ;width:150px "/>
    </div>
    <div class="heading">
        <div id="headingText">Islamic - Murhabha File Generation</div>
    </div>	
    <div class="shade1">
    </div>
	 <form name="DisplayWi" id="DisplayWi"   class="form-horizontal" style="margin-left:5%;width:80%">
		<div class="form-group">		
			<div style="height:80%;">
			<tr>
				<button  type="button" class="btn btn-primary" value="Confirmation" onclick="commoditySaleConfirmation()" style="margin-top:10px;float:right">Commodity Sale Confirmation</button>
				<button  type="button" class="btn btn-primary" value="Upload " style="margin-top:10px;float:right" onclick='call_TextRead()'>Upload File</button>
				</tr><br><br><br><br>
			<table id='cas_table' border = "1"  width = "110%" bgcolor="wheat" style="font-size: 15px">
			<thead>
				
				<tr  width = "100%">
					<th>Select</th>
					<th>WorkItem Name</th>
					<th>CIF ID</th>
					<th>Customer Name</th>
					<th>Sub Product</th>
					<th>Card Product</th>
					<th>CAS Gen File</th>
					<th>Status</th>
					
				</tr>
			</thead>
			<tbody  style="padding: 2px 10px;">
			<%
			String SqlQuery="SELECT cc_wi_name AS wi_name,CIF_ID, CustomerName, Sub_Product, warranty.Card_Product,file_generated_name,warranty.status FROM  ng_cc_exttable  cc WITH(nolock) JOIN ng_rlos_Murabha_Warranty warranty WITH(nolock) ON Murhabha_WIName=cc_wi_name WHERE CURR_WSNAME='Disbursal' and cif_id is not null and   warranty.Card_Product is not null UNION ALL SELECT pl_wi_name,CIF_ID, CustomerName, Sub_Product, warranty.card_Product,file_generated_name,warranty.status AS wi_name FROM NG_PL_EXTTABLE pl WITH(nolock) JOIN ng_rlos_Murabha_Warranty warranty WITH(nolock) ON Murhabha_WIName=pl_wi_name WHERE CURR_WSNAME='CC_Disbursal' and cif_id is not null   and warranty.Card_Product is not null";
			
			/*String SqlQuery="SELECT child_wi as wi_name,cif_id, CustomerName, loan_type, Sub_Product, NG_CC_EXTTABLE.Card_Product,file_generated_name,warranty.IS_Murhabah_Confirm FROM ng_rlos_IGR_Eligibility_CardLimit WITH(nolock)  JOIN ng_rlos_Murabha_Warranty warranty WITH(nolock) ON Murhabha_WIName=child_wi JOIN  NG_CC_EXTTABLE ON child_wi=cc_wi_name WHERE  Cardproductselect='true' AND CURR_WSNAME='Disbursal' and cif_id is not null and  loan_type = 'Islamic' UNION ALL SELECT ng_rlos_IGR_Eligibility_CardLimit.child_wi,cif_id, CustomerName, loan_type, Sub_Product, NG_pl_EXTTABLE.Card_Product,file_generated_name,warranty.IS_Murhabah_Confirm FROM ng_rlos_IGR_Eligibility_CardLimit WITH(nolock)  JOIN ng_rlos_Murabha_Warranty warranty WITH(nolock) ON Murhabha_WIName=child_wi JOIN  NG_pl_EXTTABLE ON ng_rlos_IGR_Eligibility_CardLimit.child_wi=pl_wi_name WHERE  Cardproductselect='true' AND CURR_WSNAME='CC_Disbursal' and cif_id is not null and  loan_type = 'Islamic'";*/
			
			params = "CURR_WSNAME='Disbursal' ";	
			
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
						  WI_NAME=objXmlParser.getValueOf("Wi_Name");
						  Cif_Id=objXmlParser.getValueOf("CIF_ID");
						  Customer_Name=objXmlParser.getValueOf("CustomerName");
						  Sub_Product=objXmlParser.getValueOf("Sub_Product");
						  Card_Product=objXmlParser.getValueOf("Card_Product");
						cas_generate_FileName=objXmlParser.getValueOf("file_generated_name");
						status=objXmlParser.getValueOf("status");
						 
%>		
			
				<tr  width = "100%" >
					<td><input type="checkbox" name = "checkboxes" id='mbCheckbox_<%=k%>' onclick='getCellValue(this.id)'/> </td>
					<input type='hidden' name="checkedwi" id="checkedwi"  />
					<td id = 'WINAME_<%=k%>' name ="WINAME" class ="WINAME"><%=WI_NAME%></td>
					<td><%= Cif_Id%></td>
					<td><%= Customer_Name%></td>
					<td><%= Sub_Product%></td>
					<td id = 'Card_Product_<%=k%>'><%= Card_Product%></td>
					<td id = 'FileName_<%=k%>' ><%= cas_generate_FileName%></td>
					<td id = 'status_<%=k%>' width="100px"><%= status%></td>
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
			<br><button type="button"  id='generate_csv' class="btn btn-primary" value="Done" style="margin-top:10px;float:right" onclick='call_Generate_CSV()'>Generate Commodity File </button>
		</div>
		</div>
		</form>
	

	<script language="javascript">

    Base.esapi.properties.application.Name = "My Application v1.0";
    
    
    // Initialize the api
    org.owasp.esapi.ESAPI.initialize();
		var selectedRecords={};
		var checkAllRecords={};
		var recordcount =<%=recordcount%>;
		var i=0;
		function load() {
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
						var cardProduct=document.getElementById('Card_Product_'+row_no).innerHTML;
						checkAllRecords[row_no]="'"+wi_name+"','"+cardProduct+"'";
					
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
				
				if(document.getElementById("FileName_"+row_no).innerHTML!=''){
					alert('File already generated for workitem Name: '+document.getElementById("WINAME_"+row_no).innerHTML +' and Card Product: '+document.getElementById("Card_Product_"+row_no).innerHTML);
					document.getElementById(id).checked =false;
					return false;
				}	
				else{
					var wi_name=document.getElementById('WINAME_'+row_no).innerHTML;
					var cardProduct=document.getElementById('Card_Product_'+row_no).innerHTML;
					selectedRecords[row_no]="'"+wi_name+"','"+cardProduct+"'";
				}
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
						 if(document.getElementById('mbCheckbox_'+i).checked==false && document.getElementById('FileName_'+i).innerHTML==''){
							var wi_name=document.getElementById('WINAME_'+i).innerHTML;
							var cardProduct=document.getElementById('Card_Product_'+i).innerHTML;
							checkAllRecords[i]="'"+wi_name+"','"+cardProduct+"'";
							document.getElementById('mbCheckbox_'+i).checked =true;
							//document.getElementById('mbCheckbox_'+i).click();
							}
							/*else{
									delete checkAllRecords[i];
							}*/
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
				
	
		  
		function commoditySaleConfirmation()
		{
			 //var sessionId = <%=sessionId%>;
			 
			 	var sessionId_encode = '<%=sessionId%>';
				var sessionId = ($ESAPI.encoder().decodeForHTML(sessionId_encode));
				var WD_UID_encode = '<%=WD_UID%>';
				var WD_UID = ($ESAPI.encoder().decodeForHTML(WD_UID_encode));
	               var url='/webdesktop/custom/MurhabhaCard.jsp?sessionId=' + sessionId + '&WD_UID='+WD_UID;
			 window.open(url, "", "width=500,height=500"); 

		}
	

function trimStr(str) {
                       return str.replace(/^\s+|\s+$/g, '');
            }

function call_TextRead()
{
	
	var sessionId_encode = '<%=sessionId%>';
	var sessionId = ($ESAPI.encoder().decodeForHTML(sessionId_encode));
	var WD_UID_encode = '<%=WD_UID%>';
	var WD_UID = ($ESAPI.encoder().decodeForHTML(WD_UID_encode));
		//alert(sessionId);
	var data={sessionId:sessionId,WD_UID:WD_UID}	
		$.ajax({
					type: "GET",
					url: "/webdesktop/custom/Text_Read.jsp",
					data: data,
					dataType: "text",
					async: false,
					success: function (response) {
						response=trimStr(response);					
						alert(response);	
						location.reload();	
					},
					error: function (XMLHttpRequest, textStatus, errorThrown) {
						//document.getElementById("retry_btn_"+req_name_arr).style.visibility="visible";		
						alert('Exception: '+ errorThrown);
					}
				});

}	

   

	
function call_Generate_CSV()
{
	var sessionId_encode = '<%=sessionId%>';
	var sessionId = ($ESAPI.encoder().decodeForHTML(sessionId_encode));
	var WD_UID_encode = '<%=WD_UID%>';
	var WD_UID = ($ESAPI.encoder().decodeForHTML(WD_UID_encode));
	//var wi_name="";
	//var Card_Product="";
	var wiName_cardProduct="";
	var userName=window.opener.parent.userName;
	
	
	//var checkedindex =[];
	//var x=0;
	//var wi =[];
	//var temp_WIName="";
	//var temp_CardProduct="";
	//var s1;
	var tempWiName_cardProduct="";
	
	if(Object.keys(checkAllRecords).length>0){
		for (var prop in selectedRecords) {
			tempWiName_cardProduct+=selectedRecords[prop]+'~';
		}
	}	

	else if(Object.keys(selectedRecords).length>0){
		for (var prop in selectedRecords) {
			tempWiName_cardProduct+=selectedRecords[prop]+'~';
		}
		
		/*for(var z=0;z<checkedindex.length;z++)
		{
			if(document.getElementById("FileName_"+checkedindex[z]+"").innerHTML==""){
				wi_name="'"+document.getElementById("WINAME_"+checkedindex[z]+"").innerHTML+"'";
				Card_Product="'"+document.getElementById("Card_Product_"+checkedindex[z]+"").innerHTML+"'";
				
				if(tempWiName_cardProduct=="")
				{
				  tempWiName_cardProduct = wi_name+","+Card_Product;
				}
			  else
				{
				  tempWiName_cardProduct =tempWiName_cardProduct+"~"+wi_name+","+Card_Product;
				}
			}
			else{
					alert('File already generated for workitem Name: '+document.getElementById("WINAME_"+checkedindex[z]+"").innerHTML);
					return false;
			}	
		}*/
		
		//temp_WIName=temp_WIName.substring(1, temp_WIName.length);
		//temp_CardProduct=temp_CardProduct.substring(1, temp_CardProduct.length);

		tempWiName_cardProduct=tempWiName_cardProduct.substring(0, tempWiName_cardProduct.length-1);
		//window.open("/webdesktop/custom/Generate_CSV.jsp?wiName_cardProduct="+tempWiName_cardProduct+"&sessionId="+sessionId+"&userName="+userName+"&WD_UID="+WD_UID);
		
		var data={wiName_cardProduct:tempWiName_cardProduct,sessionId:sessionId,userName:userName,WD_UID:WD_UID }
		$.ajax({
					type: "GET",
					url: "/webdesktop/custom/Generate_CSV.jsp",
					data: data,
					dataType: "text",
					async: false,
					success: function (response) {
						response=trimStr(response);
						if(response=='CSV file already generated for this wi!!' || response=='No Workitem to process' || response.indexOf("invalid")>-1 || response.indexOf("error")>-1)
						{
							alert(trimStr(response));
							/*for(var y=0;y<recordcount;y++)
							{
								if(document.getElementById("mbCheckbox_"+y)!=null){
									document.getElementById("mbCheckbox_"+y).checked =false;
								}
							}*/
							location.reload();							
						}
						
						else{
							alert('CSV file generated successfully with file name: '+response );	
							/*for(var y=0;y<recordcount;y++)
							{
								if(document.getElementById("mbCheckbox_"+y)!=null){
									if(document.getElementById("mbCheckbox_"+y).checked == true)
									{
										document.getElementById("FileName_"+y).innerHTML=trimStr(response);
										document.getElementById("mbCheckbox_"+y).checked=false;
									}
								}	
							}*/
							    location.reload();
						}	
					},
					error: function (XMLHttpRequest, textStatus, errorThrown) {
						//document.getElementById("retry_btn_"+req_name_arr).style.visibility="visible";		
						alert('Exception: '+ errorThrown);
					}
				});
	}
	else{
			alert('Please select a record first!!');
	}

}

</script>
        </body>

</html>