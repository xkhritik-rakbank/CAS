<%@ include file="Log.process"%>
<%@ page import="java.io.*,java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.lang.String.*"%>
<%@ page import="java.util.regex.Matcher"%>
<%@ page import="java.util.regex.Pattern" %>
<%@ page import="java.lang.Object"%>
<%@ page import="java.util.LinkedHashMap,java.util.*,java.io.*"%>
<%@ page import="java.util.Date"%>
<%@ page import="com.newgen.wfdesktop.xmlapi.*" %>
<%@ page import="com.newgen.wfdesktop.util.*" %>
<%@ page import="org.json.*" %>
<%@ page import="flexjson.JSONSerializer.*" %>
<%@ page import="flexjson.*" %>
<%@ page import="java.sql.*"%>
<%@ page import="com.newgen.omni.jts.srvr.*"%>   
<%@ page import="com.newgen.omni.jts.cmgr.XMLParser"%>
<%@ page import="com.newgen.omni.wf.util.app.NGEjbClient"%>
<jsp:useBean id="wDSession" class="com.newgen.wfdesktop.session.WDSession" scope="session"/>


<% 
response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader ("Expires", 0); 
	String sCabname=wDSession.getM_objCabinetInfo().getM_strCabinetName();
	String sSessionId = wDSession.getM_objUserInfo().getM_strSessionId();
	String sJtsIp = wDSession.getM_objCabinetInfo().getM_strServerIP();
	//String iJtsPort = wDSession.getM_objCabinetInfo().getM_strServerPort();
	int iJtsPort = Integer.parseInt(wDSession.getM_objCabinetInfo().getM_strServerPort());
	String appServerType = wDSession.getM_objCabinetInfo().getM_strAppServerType();

	//String sUserId=wfsession.getUserName();
	
	String sOutputXML="";
	String sFinalString="";
	XMLParser xmlParser = new XMLParser();
	String sQry=request.getParameter("Query");

	StringBuffer sInputXML=new StringBuffer();
	sInputXML.append("<?xml version=\"1.0\"?>");
	sInputXML.append("<WFSelectWithColumnNames_Input>");
	sInputXML.append("<option>APSelectWithColumnNames</option>");
	sInputXML.append("<EngineName>"+sCabname+"</EngineName>");
	sInputXML.append("<SessionID>"+sSessionId+"</SessionID>");
	sInputXML.append("<Query>"+sQry+"</Query>");
	sInputXML.append("</WFSelectWithColumnNames_Input>");
	
	WriteLog("inputXML---"+sInputXML.toString());	
		
	
						
		try	{
		
		sOutputXML=WFCallBroker.execute(sInputXML.toString(),sJtsIp,iJtsPort, appServerType);
		WriteLog("sOutputXML---"+sOutputXML);
	} catch(Exception exp) {
		WriteLog("Exception while getting data from database : "+exp);

	}
	%>

<%
	try {
		
		xmlParser.setInputXML(sOutputXML);
		String mainCode = xmlParser.getValueOf("MainCode");
            if(mainCode.equals("0")) {
		
                int iTotalRetreived = Integer.parseInt(sOutputXML.substring(sOutputXML.indexOf("<TotalRetrieved>") + 16, sOutputXML.indexOf("</TotalRetrieved>")));
                WFXmlResponse objXmlResponse = new WFXmlResponse(sOutputXML);
				WriteLog("total row received : "+iTotalRetreived);
                if (iTotalRetreived == 0) {
                   sFinalString="NODATAFOUND";
                } else {
                    
                    int j = 0;
                    for (WFXmlList objList = objXmlResponse.createList("Records", "Record"); objList.hasMoreElements(); objList.skip()) {
                        String sRowData = objList.getVal("Record");
        
						
                        while (sRowData.indexOf("<") > 0) {
                            String sColName = sRowData.substring(sRowData.indexOf("<") + 1, sRowData.indexOf(">"));
                            String sColValue = objList.getVal(sColName);
                            sColValue = (sColValue.toUpperCase()).equals("NULL") ? "" : sColValue;
							sColValue=sColValue.replaceAll("COMMA:",",");
							sColValue=sColValue.replaceAll("APOSTRO:","'");
							sColValue=sColValue.replaceAll("AMP:","&");
							sColValue=sColValue.replaceAll("DOL:","$");
							sFinalString=sFinalString+sColValue+"#";
                            
                            sRowData = sRowData.substring(sRowData.indexOf("</" + sColName + ">") + sColName.length() + 3, sRowData.length());
                        }
                        
                        sFinalString=sFinalString+"~";
                    }
                }
            } else {
               sFinalString="ERROR";
            }
        } catch (Exception exp) {
		    out.println(exp);
            sFinalString+=exp;
        }
		out.clear();
	out.println(sFinalString);	
%>

