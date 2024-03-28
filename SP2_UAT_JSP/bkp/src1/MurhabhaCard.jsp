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
<%@ page import="javax.xml.parsers.ParserConfigurationException,org.xml.sax.InputSource,org.xml.sax.SAXException" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.owasp.esapi.ESAPI"%>
<%@ page import="org.owasp.esapi.codecs.OracleCodec"%>
<%@ page import="org.owasp.esapi.User" %>
<%@ page import="com.newgen.*" %>


<%!
public  List<String> parseXML(String sOutput)  throws SAXException, IOException, ParserConfigurationException
	{
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
     List<String> result=new ArrayList<String>();
    
    Document document = builder.parse(new StringBufferInputStream(sOutput));
     
    document.getDocumentElement().normalize();
     
    Element root = document.getDocumentElement();
     
    NodeList nList = document.getElementsByTagName("Record");
    for (int temp = 0; temp < nList.getLength(); temp++)
    {
    	Node node = nList.item(temp);
       if (node.getNodeType() == Node.ELEMENT_NODE)
       {
    	   NodeList childNode=node.getChildNodes();
    	   for(int j=0;j<childNode.getLength();j++){
    		   if(j==0){
    		   result.add(temp,childNode.item(j).getTextContent());
    		   }
    		   else{
    			   result.set(temp,result.get(temp)+"|"+childNode.item(j).getTextContent());
    		   }
    	   }
       }
    }
	WriteLog("Before returning result is: "+result);
	return result;
 }
 
 %>
 
 
<html xmlns="http://www.w3.org/1999/xhtml">
  
        <head>
            <link href="https://192.168.52.243:9443/omniapp/javax.faces.resource/themes.css.jsf?ln=en_us//css" rel="stylesheet" type="text/css">			
			
			<link rel="stylesheet" href="/formviewer/resources/en_us/css/RLOS/RLOS_CSS.css" type="text/css" />
			
			<script language="javascript" src="/formviewer/resources/en_us/css/RLOS/jquery.min.js"></script>
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
        <body bgcolor="white" vlink="white" >
            <%
		String sInputXML="";
		String sOutputXML="";
		String cabinetName="";
		String wrapperPort="";
		String wrapperIP ="";
		String appServerType="";
		String params="";
		String cas_generate_FileName="";
		String input1 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("sessionId"), 1000, true) );
		String sessionId = ESAPI.encoder().encodeForSQL(new OracleCodec(), input1);

		String input2 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("WD_UID"), 1000, true) );
		String WD_UID = ESAPI.encoder().encodeForSQL(new OracleCodec(), input2);
		
		//String sessionId = request.getParameter("sessionId");
		List<String> File_Generated_Names=null;
	
	
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
			
			String query="SELECT '--Select--' as File_Generated_name union SELECT distinct File_Generated_name FROM ng_rlos_Murabha_Warranty with(nolock) WHERE Murhabha_WIName IN(SELECT processinstanceid FROM WFINSTRUMENTTABLE with(nolock) WHERE (activityname='Disbursal' AND ProcessName='CreditCard') OR ActivityName='CC_Disbursal') AND Is_Murhabah_Uploaded is not null and Is_Murhabah_Uploaded!='' and (IS_Murhabah_Confirm!='Y' or IS_Murhabah_Confirm is null)   order by File_Generated_Name desc";
			
			sInputXML = "<?xml version='1.0'?>"+
					 "<APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option>"+
					 "<Query>" + query + "</Query>"+
					 "<Params>"+ params + "</Params>"+
					 "<EngineName>" + cabinetName + "</EngineName>"+
					 "<SessionId>" + sessionId + "</SessionId></APSelectWithColumnNames_Input>";
					 
			sOutputXML = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, sInputXML);
			WriteLog(sOutputXML);
			File_Generated_Names=parseXML(sOutputXML.replaceAll("\\s","").replace("xmlversion", "xml version"));	
			WriteLog(File_Generated_Names.toString());
        } 
		catch(Exception e){
			
			WriteLog("Excetion Occured while reading from properties file: "+e.getMessage());
		}
		
	%>
	<div id="wrapper" style="height:100%;overflow:scroll">
    <div id="logo">
        <img src="../resources/images/rak-logo.gif" class="ribbon" style="height:50px ;width:150px "/>
    </div>
    <div class="heading">
        <div id="headingText">Murhabha Card </div>
    </div>	
    <div class="shade1">
    </div>
	 <form name="MurhabhaCard" id="MurhabhaCard" method="post" action=""  class="form-horizontal" style="margin-left:5%;width:80%">
		<div class="form-group">		
			<div style="height:50%;">
			<div class="form-group">
													
				<label class="control-label col-sm-2">Purchase File Name</label>
					 <div class="col-sm-2">
						<select class="form-control" id="PurchaseFileName"  style='text-transform:uppercase' onchange='setValue(id)'>
						<%for(int i=0;i<File_Generated_Names.size();i++){ %>
						<option><%=File_Generated_Names.get(i)%></option>
						<% } %>
					</select>
					</div>
			</div>	
			<div class="form-group">
				<label class="control-label col-sm-2">Purchase Generation Number</label>
				<div class="col-sm-2">
					<input class="form-control" id="PurchaseGenerationNumber"  disabled>
				</div>
			</div>
				
			<div class="form-group">
				<label class="control-label col-sm-2">Purchase Confirmation Number</label>
				<div class="col-sm-2">
					<input class="form-control" id="PurchaseConfirmationNumber" disabled>
				</div>	
			</div>
			<div class="form-group">
				<label class="control-label col-sm-2">Certificate Number</label>
				<div class="col-sm-2">
					<input class="form-control" id="CertificateNumber">
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-sm-2">Sales Confirmation Number</label>
				<div class="col-sm-2">
					<input class="form-control" id="SalesConfirmationNumber">
				</div>
			</div>
			

				
			</div>
			
			<button type="submit" class="btn btn-primary" value="Done" onclick="closeFunction()" style="margin-top:10px;float:right">Cancel </button>
			<button type="submit" class="btn btn-primary" value="Done" onclick="saveData()" style="margin-top:10px;float:right">Confirm </button>
		</div>
		
		

		</form>
	

		<script type="text/javascript">

    Base.esapi.properties.application.Name = "My Application v1.0";
    
    
    // Initialize the api
    org.owasp.esapi.ESAPI.initialize();

	
			function closeFunction()
			{
				window.close();
			}
			
			function setValue(id)
			{
				var fileName=document.getElementById(id).value;
				if(fileName!='--Select--')
				{
					var fileName_array=fileName.split("_");
					document.getElementById('PurchaseGenerationNumber').value=fileName_array[2]+"_"+fileName_array[3];
					document.getElementById('PurchaseConfirmationNumber').value=fileName_array[2]+"_"+fileName_array[3];
				}
				else{
					document.getElementById('PurchaseGenerationNumber').value='';
					document.getElementById('PurchaseConfirmationNumber').value='';
				}	
			}	
			
	function trimStr(str) {
                       return str.replace(/^\s+|\s+$/g, '');
            }
			
	function saveData()
	{
				//var sessionId = <%=sessionId%>;
			 
			 	var sessionId_encode = '<%=sessionId%>';
				var sessionId = ($ESAPI.encoder().decodeForHTML(sessionId_encode));
				var WD_UID_encode = '<%=WD_UID%>';
				var WD_UID = ($ESAPI.encoder().decodeForHTML(WD_UID_encode));
				var ip='<%=wrapperIP %>';
				var fileName=document.getElementById('PurchaseFileName').value;
				var CertificateNumber=document.getElementById('CertificateNumber').value;
				var SalesConfirmationNumber=document.getElementById('SalesConfirmationNumber').value;
				
				if(fileName!='--Select--')
				{
					if(CertificateNumber==''){
							alert('Please enter certificate Number!!!');
							return false;
					}	
					if(SalesConfirmationNumber==''){
							alert('Please enter Sales Confirmation Number!!!');
							return false;
					}
					var swhere="File_Generated_name='"+fileName+"'";					
					var stable='ng_rlos_Murabha_Warranty';
					var value="'"+CertificateNumber+"','"+SalesConfirmationNumber+"','Y','File Confirmed'";
					var params="table="+stable+"&columnsNames=CertificateNumber,Sales_Confirmation_Number,IS_Murhabah_Confirm,status"+"&columnsValues="+value+"&swhere="+swhere+"&sessionId="+sessionId+"&WD_UID="+WD_UID;
					//alert(params);

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
						if(trimStr(response)=='SUCCESS'){
							alert('Details updated successfully!!!');	
							window.opener.location.href="https://"+ip+":9443/webdesktop/custom/MurhabhaIslamicCSV.jsp?sSessionId="+sessionId + '&WD_UID='+WD_UID;
							/*var rows=window.opener.recordcount;
							for(var y=0;y<rows;y++)
							{
								if(window.opener.document.getElementById("FileName_"+y)!=null){
									if(window.opener.document.getElementById("FileName_"+y).innerHTML == fileName)
									{
										window.opener.document.getElementById("confirmFile_"+y).innerHTML='Y';
									}
								}	
							}*/
						}
						else{
							alert('Some error occurred.Please contact administrator!!!');	
						}	
						window.close();
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
				
				xmlReq.open('POST','/webdesktop/custom/CustomUpdate.jsp',false);				
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
		else{
				alert('Please select a record first!!!');
		}	
	}	
			
			
				
		</script>
	
        </body>

</html>