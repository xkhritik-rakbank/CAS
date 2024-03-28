<!------------------------------------------------------------------------------------------------------
//           NEWGEN SOFTWARE TECHNOLOGIES LIMITED

//Group						 : Application ?Projects
//Product / Project			 : RAKBank-RLOS
//File Name					 : Internal Liability
//Author                     : Deepak Kumar	
//DESC						 : To generate and save Editable Grid JSP
//Date written (DD/MM/YYYY)  : 2/02/2017
//---------------------------------------------------------------------------------------------------->

<%@ include file="Log.process"%>
<%@ page import="java.io.*,java.util.*,javax.servlet.*,javax.servlet.http.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.lang.String.*"%>
<%@ page import="java.lang.Object"%>
<%@ page import="com.newgen.wfdesktop.xmlapi.*" %>
<%@ page import="com.newgen.wfdesktop.util.*" %>
<%@ page import="com.newgen.omni.jts.cmgr.XMLParser"%>
<%@ page import="com.newgen.mvcbeans.model.*,javax.faces.context.FacesContext,com.newgen.mvcbeans.controller.workdesk.*"%>
<%@ page import="com.newgen.omni.webdesktop.ngappbean.ngappbean" %>

<%
response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader ("Expires", 0);   
   String sSessionId = "";//wDSession.getM_objUserInfo().getM_strSessionId();
    
    
	if (sSessionId == null) {
        out.println("Invalid Session. Please login again.");
    }
	try{
	
	String fieldId = request.getParameter("control");
	WriteLog("Security_Check.jsp: fieldId: "+fieldId);
	String data=request.getParameter("data");
	WriteLog("Security_Check.jsp: data: "+data);
	String state = request.getParameter("state");
	WriteLog("Security_Check.jsp: state: "+state);
	String identifier = request.getParameter("identifier");
	WriteLog("Security_Check.jsp: identifier: "+identifier);
	HashMap<String,String> valMap = new HashMap<String,String>();
	HashMap<String,String> stateMap = new HashMap<String,String>();
	
	if(null!=ngappbean.getBeanDataCust(identifier+"_value")){
		//WriteLog("Security_Check.jsp: session attribute for valueMap is not null ");
	 valMap =  ngappbean.getBeanDataCust(identifier+"_value");
	}
	else{
		//WriteLog("Security_Check.jsp: session attribute for value map is null ");
	valMap = new HashMap<String,String>();	
	}
	if(null!=ngappbean.getBeanDataCust(identifier+"_state")){
		//WriteLog("Security_Check.jsp: session attribute for stateMap is not null ");
	 stateMap =  ngappbean.getBeanDataCust(identifier+"_state");
	}
	else{
		//WriteLog("Security_Check.jsp: session attribute for stateMap is null ");
	stateMap = new HashMap<String,String>();	
	}
	
	
	addEntry(valMap,fieldId,data);
	
	ngappbean.setBeanDataCust(identifier+"_value",valMap);
	addEntry(stateMap,fieldId,state);	
	
	ngappbean.setBeanDataCust(identifier+"_state",stateMap);
	WriteLog("Security_Check.jsp: valMap is: "+valMap);
	WriteLog("Security_Check.jsp: stateMap is: "+stateMap);
	
	
	//WriteLog("Security_Check.jsp: getCustBean: "+ngappbean.getBeanDataCust(identifier+"_value"));
	}
	catch(Exception ex){
		ex.printStackTrace();
		WriteLog("Security_Check.jsp: throwable excp: "+ printException(ex));
		//WriteLog("Security_Check.jsp: throwable excp: "+ ex.getMessage());
	}
	catch(Throwable t) {
		WriteLog("Security_Check.jsp: throwable error: "+ printException(t));
	}

	
%>
<%!
	public static void addEntry(Map<String,String> hm,String fieldId,String fieldValue){
		try{
		WriteLog("Security_Check.jsp: inside add entry method ");
		hm.put(fieldId, fieldValue);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public static String printException(Exception e){
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String exception = sw.toString();
		return exception;	

	}
	public static String printException(Throwable e){
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String exception = sw.toString();
		return exception;	

	}
%>
