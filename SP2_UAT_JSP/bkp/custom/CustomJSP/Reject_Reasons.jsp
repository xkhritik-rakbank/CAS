<!--------------------------------------------------------------------------------------------------------
//           NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group						 : Application –Projects
Product / Project			 : RAK Bank 
Module                     : Request-Screen Form Painitng
File Name					 : RejectReasons.jsp          
Author                     : Mandeep Singh
Date written (DD/MM/YYYY) : 27-Jan-2016
Description                : Reject Reasons
//---------------------------------------------------------------------------------------------------->
<%@ include file="Log.process"%>
<jsp:useBean id="wDSession" class="com.newgen.wfdesktop.session.WDSession" scope="session"/>
<jsp:useBean id="WDCabinet" class="com.newgen.wfdesktop.baseclasses.WDCabinetInfo" scope="session"/>
<%@ page import="java.util.*"%>
<%@ page import="java.io.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.net.*"%>
<%@ page import="javax.xml.parsers.ParserConfigurationException" %>
<%@ page import="com.newgen.wfdesktop.exception.*" %>
<%@ page import="com.newgen.wfdesktop.xmlapi.*" %>
<%@ page import="com.newgen.wfdesktop.util.*" %>
<%@ page import="com.newgen.wfdesktop.util.xmlapi.*" %>
<%@ page import="com.newgen.custom.*" %>
<%@ page import="java.io.UnsupportedEncodingException" %>
<%@ page import="com.newgen.mvcbeans.model.wfobjects.WFDynamicConstant, com.newgen.mvcbeans.model.*,com.newgen.mvcbeans.controller.workdesk.*, javax.faces.context.FacesContext"%>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@page import="com.newgen.omni.wf.util.excp.NGException"%>
<%@page import="com.newgen.omni.wf.util.app.NGEjbClient"%>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page  import= "com.newgen.custom.*, org.w3c.dom.*, org.w3c.dom.NodeList,org.w3c.dom.Document,javax.xml.parsers.DocumentBuilder, javax.xml.parsers.DocumentBuilderFactory"%>
<%@ page import ="java.text.DecimalFormat,org.xml.sax.InputSource"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.owasp.esapi.ESAPI"%>
<%@ page import="org.owasp.esapi.codecs.OracleCodec"%>
<%@ page import="org.owasp.esapi.User" %>
<%@ page import="com.newgen.*" %>


<script language="javascript" src="/webdesktop/customJS/jquery-latest.js"></script>
<script language="javascript" src="/webdesktop/customJS/jquery.autocomplete.js"></script>

 
<!-- esapi4js i18n resources -->
<script type="text/javascript" language="JavaScript" src="esapi4js/resources/i18n/ESAPI_Standard_en_US.properties.js"></script>
<!-- esapi4js configuration -->
<script type="text/javascript" language="JavaScript" src="esapi4js/esapi-compressed.js"></script>
<script type="text/javascript" language="JavaScript" src="esapi4js/resources/Base.esapi.properties.js"></script>
<!-- esapi4js core -->
<script language="javascript">
	var OKCancelClicked=false;
</script>

<%
Map<String,String> RejectReasonsMap = new HashMap<String, String>();

     String input = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("ProcessName"), 1000, true) );
	 String process_Name = ESAPI.encoder().encodeForSQL(new OracleCodec(), input);
	 
%>
	<% String RejectReasons="";%>

	

<html>
	<head>
		<title>Reject Reasons</title>
		<link rel="stylesheet" href="\webdesktop\resources\en_us\css\docstyle.css">
		<link rel="stylesheet" href="\webdesktop\resources\en_us\css\jquery.autocomplete.css">
		<style>
			table {border-collapse:collapse;}
			table, th, td {
				border: none;
				
			}
			body{
				bgcolor:"#FFFBF0";
			}
			
			.scrollable{
			   overflow-x: scroll;
			   overflow-y: auto;
			   width: 400px; 
			   height: 150px; 
			   border: 1px silver solid;
			 }
			 .scrollable select{
			   border: none;
			 }
		
		</style>
		<script language=JavaScript> 
		

    Base.esapi.properties.application.Name = "My Application v1.0";
    
    
    // Initialize the api
    org.owasp.esapi.ESAPI.initialize();
	
		var process_Name_encode = '<%=process_Name%>';
	var process_Name = ($ESAPI.encoder().decodeForHTML(process_Name_encode));
//***********************************************************************************************************//

//          NEWGEN SOFTWARE TECHNOLOGIES LIMITED

 

//Group                               :   Application Projects
//Project                             :   Rakbank - Telegraphic Transfer      
//Date Modified                       :   27/01/2016      
//Author                              :   Mandeep
//Description                		  :   This function sets the autocomplete data form database to the input

//***********************************************************************************************************//
			function setAutocompleteData() {
				var data ="";
				var ele=document.getElementById("AutocompleteValues");
				if(ele)
					data=ele.value;
				if(data!=null && data!="" && data!='{}'){
					data = data.replace('{','').replace('}','');
					var arrACTFields = data.split(",");
					
					for(var i=0 ; i< arrACTFields.length ; i++){
						var temp = arrACTFields[i].split("=");
						var values = temp[1].split(";");
						
						$(document).ready(function(){
							$("#"+temp[0]).autocomplete(values,{max:15,minChars:0,matchContains:true});							
						});
					}		
				}
			}
//*****************************************************************************************//

//          NEWGEN SOFTWARE TECHNOLOGIES LIMITED                               
//Group                               :    Application Projects   
//Project                             :    Rakbank - Telegraphic Transfer    
//Date Modified                       :    27-Jan-2016                   
//Author                              :    Mandeep
//Description                		  :    This functions handles the event of the Ok button     

//*****************************************************************************************//
			function AddClick()	{
				var Val = document.getElementById('RejectReasons').value;
				var code=getCode(Val);
				
				if(typeof code=="undefined" || code==null || code=="" || code=="null" || code=="undefined")
					return;
					
				if(code!='')
				{
					var bItemAlreadyAdded=false;
					var select = document.getElementById('RejectReasonsList');
					if(select.options==null)
						bItemAlreadyAdded=false;
					else{
						for(var i=0;i<select.options.length;i++){
							if(select.options[i].value==Val){
								alert("Reason already selected.");
								bItemAlreadyAdded=true;	
								break;
							}
						}
					}
					if(!bItemAlreadyAdded){										
						var opt = document.createElement('option');
						opt.value = Val;
						opt.innerHTML = Val;
						opt.length = Val.length;
						//alert(opt.length);
						opt.className="EWNormalGreenGeneral1";
						select.appendChild(opt);
					}	
					document.getElementById('RejectReasons').value='';							
				}
			}
				
//*******************************************************************************************//

//          NEWGEN SOFTWARE TECHNOLOGIES LIMITED

 

//Group                               :    Application Projects
//Project                             :    Rakbank - Telegraphic Transfer           
//Date Modified                       :    27/01/2016                
//Author                              :    Mandeep
//Description                		  :    This function populate the lis box with the values    

//********************************************************************************************//			
			function populateListbox(codeString) {
				
				//alert(codeString);
				if(typeof codeString === "undefined"  ||codeString==null||codeString==""||codeString=="null"||codeString=="undefined")
					return;	
					
				//if(codeString.indexOf("#")==-1)	
					//return;	
					
				var codesArr=codeString.split("#");
				for (var i=0;i<codesArr.length;i++)	{
					
					var fieldVal=getVal(codesArr[i]);
					
					var select = document.getElementById('RejectReasonsList');
					var found=false;
					for(var i=0;i<select.options.length;i++){
						if(select.options[i].value==fieldVal){							
							found=true;
							break;
						}
					}
					
					var opt = document.createElement('option');
					opt.value = fieldVal;
					opt.innerHTML = fieldVal;
					opt.className="EWNormalGreenGeneral1";
					select.appendChild(opt);
				}
			}
//***************************************************************************************************//

//          NEWGEN SOFTWARE TECHNOLOGIES LIMITED

 

//Group                               :   Application Projects
//Project                             :   Rakbank - Telegraphic Transfer         
//Date Modified                       :   27/01/2016                
//Author                              :   Mandeep
//Description                		  :   This function removes the selected items from the select box       

//***************************************************************************************************//			
			function RemoveSelected(){
				var select = document.getElementById('RejectReasonsList');				
				if(select.options.length==0)
				{
					alert('No reason in the grid.');
					return;	
				}	
				var removed=false;
				for(i = select.options.length-1; i >=0; i--) {
					if (select.options[i].selected) {
						select.remove(i);
						removed=true;
					}
				}
				if(!removed)
					alert('Select some reason(s) to remove.');
			}
//*******************************************************************************************//

//          NEWGEN SOFTWARE TECHNOLOGIES LIMITED

 

//Group                               :   Application Projects
//Project                             :   Rakbank - Telegraphic Transfer         
//Date Modified                       :   27/01/2016                
//Author                              :   Mandeep
//Description                		  :   This function returns the code of the given value       

//********************************************************************************************//			
			function getCode(Val){	
				var data ="";
				var code ="";
				var desc=document.getElementById("AutocompleteValues");
				if(desc)
					data=desc.value;
				if(data!=null && data!="" && data!='{}') {
					data = data.replace('{','').replace('}','');
					var arrACTFields = data.split("=")[1].split(";");
					var arrACTCodes = document.getElementById("CodeValues").value.replace('{','').replace('}','').split("=")[1].split(";");
					for(var i=0 ; i< arrACTFields.length ; i++)	{	
						if(Val==arrACTFields[i]){
							code=arrACTCodes[i];
							break;
						}
					}
				}
				return code;				
			}
//*****************************************************************************************//

//          NEWGEN SOFTWARE TECHNOLOGIES LIMITED

 

//Group                               :   Application Projects
//Project                             :   Rakbank - Telegraphic Transfer           
//Date Modified                       :   27/01/2016                
//Author                              :   Mandeep
//Description                		  :   This function returns the value of a given code       

//******************************************************************************************//			
			function getVal(code){					
				var data ="";
				var Val ="";
				var desc=document.getElementById("CodeValues");
				if(desc)
					data=desc.value;
				if(data!=null && data!="" && data!='{}'){
					data = data.replace('{','').replace('}','');
					var arrACTCodes = data.split("=")[1].split(";");
					var  arrACTFields= document.getElementById("AutocompleteValues").value.replace('{','').replace('}','').split("=")[1].split(";");
					for(var i=0 ; i< arrACTCodes.length ; i++){	
						if(code==arrACTCodes[i]){
							Val=arrACTFields[i];
							break;
						}
					}
				}
				return Val;				
			}
//**********************************************************************************//

//          NEWGEN SOFTWARE TECHNOLOGIES LIMITED

 

//Group                               :   Application Projects
//Project                             :   Rakbank - Telegraphic Transfer          
//Date Modified                       :   27/01/2016                
//Author                              :   Mandeep
//Description                		  :   This function handles the event of Ok button

//***********************************************************************************//			
			function OKClick()
			{
			   	var returnValue="";
				var rejectreason="";
				
				var select = document.getElementById('RejectReasonsList');	
				//alert('select:'+ select);
				for (var i=0;i<select.options.length;i++)	{
					if(returnValue=="" )
					{
						returnValue=getCode(select.options[i].value);
						rejectreason=select.options[i].value;						
					}
					else
					{
						returnValue+="#"+getCode(select.options[i].value);
						rejectreason+=","+select.options[i].value;
					}
				}
				//alert("rejectreason"+rejectreason);
				if(process_Name=='RLOS')
				{
					window.opener.document.getElementById('cmplx_DecisionHistory_RejectRemarks').value = rejectreason;
				}
				if(process_Name=='PersonalLoanS')
				{
					window.opener.document.getElementById('cmplx_Decision_rejreason').value = rejectreason;
				}
				window.returnValue = returnValue;
				OKCancelClicked=true;
				
				window.close();
			}
//**********************************************************************************//

//          NEWGEN SOFTWARE TECHNOLOGIES LIMITED

 

//Group                               :  Application Projects
//Project                             :  Rakbank - Telegraphic Transfer           
//Date Modified                       :  27/01/2016                
//Author                              :  Mandeep
//Description                		  :  This function handles the event of Cancel button   

//***********************************************************************************//			
			function CancelClick(opt){				
				if(opt==2){
					window.returnValue = "NO_CHANGE";
					OKCancelClicked=true;
					window.close();				
				}	
				else if(!OKCancelClicked)
				{
					window.returnValue = "NO_CHANGE";				
				}	
			}
		</script>
	</head>
	<%
	RejectReasonsMap.put("RejectReasons","Description");
	String WSNAME="Reject_Queue";
	String Query="";
	String inputXML="";
	String outputXML="";
	String mainCodeValue="";
	
	String subXML="";
	String appServerType = wDSession.getM_objCabinetInfo().getM_strAppServerType();
    String wrapperIP = wDSession.getM_objCabinetInfo().getM_strServerIP();
    String wrapperPort = wDSession.getM_objCabinetInfo().getM_strServerPort()+"";    
    String sessionId = wDSession.getM_objUserInfo().getM_strSessionId();
    String cabinetName = wDSession.getM_objCabinetInfo().getM_strCabinetName();
	String params="";

	
	Query="select code,Description from ng_MASTER_RejectReasons with(nolock) where isactive='Y' AND WSNAME=:WSNAME order by id ASC";
	params="WSNAME=="+WSNAME;
	WriteLog("Query for getting reject reasons:"+Query);
	inputXML=ExecuteQuery_APSelectwithparam(Query,params,cabinetName,sessionId);
	
	//inputXML = "<?xml version='1.0'?><APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option><Query>" + Query + "</Query><EngineName>" + cabinetName + "</EngineName><SessionId>" + sessionId+ "</SessionId></APSelectWithColumnNames_Input>";
	//outputXML = WFCallBroker.execute(inputXML, wrapperIP, Integer.parseInt(wDSession.getM_objCabinetInfo().getM_strServerPort()), appServerType);
	outputXML = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, inputXML.toString());
	WriteLog("outputXML exceptions-->"+outputXML);
	DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(outputXML));
			Document xmlDoc = db.parse(is);
			String items="";
			String codes="";
		
			mainCodeValue="0";
			NodeList list = xmlDoc.getElementsByTagName("Record");
			if(mainCodeValue.equals("0"))
		{
			for (int i = 0; i < list.getLength(); i++) {
				Node subList = list.item(i);
				if (subList.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) subList;
				
					if(items.equals(""))
					{
						codes=eElement.getElementsByTagName("code").item(0).getTextContent();
						items=eElement.getElementsByTagName("Description").item(0).getTextContent();
					}	
					else 
					{					
						codes+=";"+eElement.getElementsByTagName("code").item(0).getTextContent();
						items+=";"+eElement.getElementsByTagName("Description").item(0).getTextContent();
					}		
				}
			}	

			HashMap<String, String> AutocompleteMap = new HashMap<String, String>();
			HashMap<String, String> CodeMap = new HashMap<String, String>();
			for(Map.Entry<String, String> entry : RejectReasonsMap.entrySet()) 
			{
			  String key = entry.getKey();
			  String value = entry.getValue();
			  
			  if(outputXML.indexOf(value)>=0)
			  {
				AutocompleteMap.put(key, items);
				CodeMap.put(key, codes);
			  } 
			}
%>
<%!
	public static String ExecuteQuery_APSelectwithparam(String sQry,String params, String cabinetName, String sessionId)
	{
	String sInputXML = "<?xml version='1.0'?><APSelectWithNamedParam_Input>"
			+ "<option>APSelectWithNamedParam</option>" + "<Query>" + sQry + "</Query>" + "<Params>" + params
			+ "</Params>" + "<EngineName>" + cabinetName + "</EngineName>" + "<SessionID>" + sessionId
			+ "</SessionID>" + "</APSelectWithNamedParam_Input>";
	return sInputXML;
	}
	%>
	<body class="EWGeneralRB" style="border: 2px solid #b20000; border-style:outset;" bgcolor="#FFFBF0" onload="setAutocompleteData();populateListbox('<%=RejectReasons%>');" onUnload="CancelClick(1);">		
		
		<table width="100%" border="1" >		
			<tr><td width=100% align=right valign=center><img src="\webdesktop\webtop\images\rak-logo.gif" style="margin-top: 15px;"></td></tr>
		</table>
		 
	       
	
		
		<hr style=" color: #b20000; height: 3px">
					

		<div align="center" border="1px" width="100%"> 
			<table cellpadding="2" cellspacing="1" border="1">
				
				<tr><td width="100%" class="EWNormalGreenGeneral1"><b>Reject Reasons </b></td></tr>
				<tr><td width='100%'  align="center" nowrap='nowrap' class='EWNormalGreenGeneral1'>
					 <input type='text' style="width:400;" data-toggle='tooltip' onmousemove='title=this.value' onmouseover='title=this.value' size = '32' name='RejectReasons' id="RejectReasons" value = '' ></td>
				</tr>
				<tr>
					<td width="100%"  class="EWNormalGreenGeneral1"><b>Selected Reject Reasons</b></td></tr>
					<tr>
						<td width='100%' align="center" nowrap='nowrap' class='EWNormalGreenGeneral1'>
							<div class="scrollable">
								<select multiple="multiple" style="width:630px;" id="RejectReasonsList" size='14'></select>
							</div>
						</td>
					</tr>
			</table>
		</div> 
		<input type=hidden name='AutocompleteValues' id='AutocompleteValues' value='<%=AutocompleteMap%>' style='visibility:hidden' >
		<input type=hidden name='CodeValues' id='CodeValues' value='<%=CodeMap%>' style='visibility:hidden' >
		<%
	}
%>			
		<div align="center"> 
			<input type="button" action="" value="Add" onclick="AddClick();" class="EWButtonRB" >
			<input type="button" action="" value="Remove" onclick="RemoveSelected();" class="EWButtonRB" >
			<input type="button" action="" value="Save & Exit" onclick="OKClick();" class="EWButtonRB" >
		</div>	
	</body>	
</html>