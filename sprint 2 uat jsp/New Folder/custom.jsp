
<%@page import="com.newgen.mvcbeans.model.wfobjects.WFDynamicConstant"%>

<%@ page import="com.newgen.mvcbeans.model.*"%>
<%@ page import="com.newgen.mvcbeans.controller.workdesk.*"%>
<%@ page import="javax.faces.context.FacesContext"%>
<%@ page import="com.newgen.mvcbeans.controller.workdesk.*"%>
<%@ page import="java.util.LinkedHashMap"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.owasp.esapi.ESAPI"%>
<%@ page import="org.owasp.esapi.codecs.OracleCodec"%>
<%@ page import="org.owasp.esapi.User" %>
<%@ page import="com.newgen.*" %>

<jsp:useBean id="wDSession" class="com.newgen.wfdesktop.session.WDSession" scope="session" />

<html>
    	
		
		 <div id="logo">
        <img src="../resources/images/rak-logo.gif" class="ribbon"/>
		</div>
		
        <head>
           <link href="https://10.15.13.148:9443/omniapp/javax.faces.resource/themes.css.jsf?ln=en_us//css" rel="stylesheet" type="text/css">

			<link rel="stylesheet" href="/formviewer/resources/en_us/css/RLOS/jquery-ui.css" type="text/css" />
			<link rel="stylesheet" href="/formviewer/resources/en_us/css/RLOS/RLOS_CSS.css" type="text/css" />
			<%
			//String sSessionId = request.getParameter("SessionId");
			String input1 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("SessionId"), 1000, true) );
			String sSessionId = ESAPI.encoder().encodeForSQL(new OracleCodec(), input1);
			String WD_UID = wDSession.getM_strUniqueUserId();
			
			%>
           
<!-- esapi4js i18n resources -->
<script type="text/javascript" language="JavaScript" src="esapi4js/resources/i18n/ESAPI_Standard_en_US.properties.js"></script>
<!-- esapi4js configuration -->
<script type="text/javascript" language="JavaScript" src="esapi4js/esapi-compressed.js"></script>
<script type="text/javascript" language="JavaScript" src="esapi4js/resources/Base.esapi.properties.js"></script>
<!-- esapi4js core -->
	<script type="text/javascript"> 
			

    Base.esapi.properties.application.Name = "My Application v1.0";
    
    
    // Initialize the api
    org.owasp.esapi.ESAPI.initialize();
		            
			
			//var sSessionId = '<%=sSessionId%>';
			var WD_UID = '<%=WD_UID%>';
			var sSessionId_encode = '<%=sSessionId%>';
			var sSessionId = ($ESAPI.encoder().decodeForHTML(sSessionId_encode));
			
			
			//sSessionId = '1234567';
                // custom scripts here
                function onCustomLinkClick(op){
                    var url="";
					if(op=="1"){
                        url = "custom"+op+".jsp";
                        window.open(url,"custom"+op,'height=400,width=400,resizable=0,status=1,scrollbars=0,top=200,left=200');
                    }
					if(op=="2"){
						url="leavemgt.jsp";
						window.open(url,"leavemgt",'height=400,width=400,resizable=1,status=1,scrollbars=0,top=200,left=200');
					}
					if(op=="3"){
						url="DisplayWI.jsp?sSessionId="+sSessionId+ "&WD_UID="+WD_UID;
						window.open(url,"DisplayWI",'height=400,width=1000,resizable=1,status=1,scrollbars=0,top=200,left=200');
					}
					if(op=="5"){
						url="MurhabhaIslamicCSV.jsp?sSessionId="+sSessionId+ "&WD_UID="+WD_UID;
						window.open(url,"Murhabha File Generation",'height=400, width=1000,resizable=1,status=1,scrollbars=0,top=200,left=200');
					}

					//++ Below Code Added by nikhil for Duplicate Creation JSP
					if(op=="4"){
						url="CreateNewWorkitem.jsp";
						window.open(url,"Create New Workitem Link",'height=150 , width=400 ,resizable=1,status=1,scrollbars=0,top=200,left=200');
					}
					//-- Above Code Added by nikhil for Duplicate Creation JSP
                }
				  </script>
				
        </head>
		
        <body>
			<div>
				<table border = "1"  width = "100%" bgcolor="white">
					<thead>
						<tr  width = "300%">
							<!--<th><span onclick="onCustomLinkClick('1')" class="linkstyle" style="margin:5px" style="font-size: 550%">Custom Link1</span></th>
							
							<th><span onclick="onCustomLinkClick('2')" class="linkstyle" style="margin:5px" style="font-size: 550%">Leave Management Module</span></th>  -->
							
							<th><span onclick="onCustomLinkClick('3')" class="linkstyle" style="margin:5px" style="font-size: 650%">CC Disbursal Bulk Auto WI</span></th>
							<!--<th><span onclick="onCustomLinkClick('4')" class="linkstyle" style="margin:5px" style="font-size: 550%">Create Duplicate Workitem</span></th>  -->
							<th><span onclick="onCustomLinkClick('5')" class="linkstyle" style="margin:5px" style="font-size: 550%">Murhabha File Generation</span></th>

						</tr>
					</thead>
				</table>
			</div>
        </body>
		
		
  
</html>