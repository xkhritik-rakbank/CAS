<jsp:useBean id="wDSession" class="com.newgen.wfdesktop.session.WDSession" scope="session"/>
<jsp:useBean id="WDCabinet" class="com.newgen.wfdesktop.baseclasses.WDCabinetInfo" scope="session"/>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.owasp.esapi.ESAPI"%>
<%@ page import="org.owasp.esapi.codecs.OracleCodec"%>
<%@ page import="org.owasp.esapi.User" %>
<%@ page import="com.newgen.*" %>
<%
	try{
		
      String input = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("WD_UID"), 1000, true) );
	  String WD_UID = ESAPI.encoder().encodeForSQL(new OracleCodec(), input);
	  
		String sessionId ="";
		sessionId =    wDSession.getM_objUserInfo().getM_strSessionId();
		//System.out.println("Inside Session_refresh Session ID: " + sessionId);
		}
		catch(Exception e){
			System.out.println("exception occured in session refresh"+ e.getMessage());
		}
%>