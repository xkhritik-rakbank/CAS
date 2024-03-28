<%@page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.newgen.custom.mvcbeans.model.*" %>
<%@ page import="com.newgen.custom.mvcbeans.controller.workdesk.*" %>
<%@ page import="java.util.LinkedHashMap"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.net.*"%>
<%@ page import="com.newgen.custom.wfdesktop.xmlapi.*"%>

<jsp:useBean id="customSession" class="com.newgen.custom.wfdesktop.session.WFCustomSession" scope="session"></jsp:useBean>

<%

	String WINAME = null;
	String wid = null;
	String engineName = null;
	String userName = null;
	String userIndex = null;
	String routeID = null;
	String sessionId = null;
	String context = null;
	String formName = null;
	String jtsIP = null;
	int jtsPort = 0;
	int debugValue = 0;
	String activityId = null;
	String WSNAME = null;
	String attributeData = null;
	String dateFormat = null;
	String locale = null;
	String dataBaseType = null;
	String multitenancyVar = null;
	String appServerName = null;
	String routeName = null;
	String ViewMode = null;
	String ArchivalMode = null;
	CustomWiAttribHashMap attributeMap = null;
	
    Map parameterMap = request.getParameterMap();
    if(parameterMap != null && parameterMap.size() > 0)
    {
		WINAME = request.getParameter("wdesk:ProcessInstanceID");
		wid = request.getParameter("wdesk:WorkItemID");
		engineName = request.getParameter("wdesk:EngineName");
		userName = request.getParameter("wdesk:UserName");
		userIndex = request.getParameter("wdesk:UserIndex");
		routeID = request.getParameter("wdesk:RouteID");
		sessionId = request.getParameter("wdesk:SessionID");
		context = request.getParameter("wdesk:Context");
		formName = request.getParameter("wdesk:FormName");
		jtsIP = "127.0.0.1";
		jtsPort = 2809;
		debugValue = Integer.parseInt(request.getParameter("wdesk:DebugValue"));
		activityId = request.getParameter("wdesk:ActivityID");
		WSNAME = URLDecoder.decode(request.getParameter("wdesk:WorkStepName"),"UTF-8");
		attributeData = request.getParameter("wdesk:AttributeData");
		dateFormat = request.getParameter("wdesk:DateFormat");
		locale = request.getParameter("wdesk:Locale");
		dataBaseType = request.getParameter("wdesk:DatabaseType");
		multitenancyVar = request.getParameter("wdesk:MultiTenancyVar");
		appServerName = "WebSphere";
		routeName = request.getParameter("wdesk:RouteName");
		ViewMode = request.getParameter("wdesk:ViewMode");
		ArchivalMode = request.getParameter("wdesk:ArchivalMode");
		if(ArchivalMode.equalsIgnoreCase("Y")){
			customSession.setEngineName("rakbankdi_first");
		}else{
			customSession.setEngineName("rakcas");
			//customSession.setEngineName(request.getParameter("wdesk:EngineName"));
		}
		customSession.setDMSSessionId(request.getParameter("wdesk:SessionID"));
		customSession.setDebugValue(Integer.parseInt(request.getParameter("wdesk:DebugValue")));
		customSession.setEngineName(request.getParameter("wdesk:EngineName"));
		customSession.setJtsIp(jtsIP);
		customSession.setJtsPort(jtsPort);
		customSession.setLocale(request.getParameter("wdesk:Locale"));
		customSession.setStrDatabaseType(request.getParameter("wdesk:DataBaseType"));
		customSession.setUserIndex(Integer.parseInt(request.getParameter("wdesk:UserIndex")));
		customSession.setUserName(request.getParameter("wdesk:UserName"));
		customSession.setGenRsb(customSession.getRSB(locale));
		customSession.setWebContextPath(request.getParameter("wdesk:Context"));
		customSession.setM_strMultiTenancyVar(multitenancyVar);
		customSession.setAppServerName(appServerName);
		customSession.setStrDatabaseType(dataBaseType);
		customSession.setRouteName(routeName);
		WFCustomCallBroker.setAppServer(appServerName,jtsIP.trim(),String.valueOf(jtsPort));
    }
%>