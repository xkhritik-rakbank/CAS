<jsp:useBean id="wDSession" class="com.newgen.wfdesktop.session.WDSession" scope="session"/>
<jsp:useBean id="WDCabinet" class="com.newgen.wfdesktop.baseclasses.WDCabinetInfo" scope="session"/>
<%
	try{
		String sessionId ="";
		sessionId =    wDSession.getM_objUserInfo().getM_strSessionId();
		//System.out.println("Inside Session_refresh Session ID: " + sessionId);
		}
		catch(Exception e){
			System.out.println("exception occured in session refresh"+ e.getMessage());
		}
%>