<%@ include file="Log.process"%>
<jsp:useBean id="wDSession" class="com.newgen.wfdesktop.session.WDSession" scope="session"/>
<jsp:useBean id="WDCabinet" class="com.newgen.wfdesktop.baseclasses.WDCabinetInfo" scope="session"/>
<%@ page import ="java.io.*" %>
<%@ page import ="java.util.*"%>
<%@ page import="com.newgen.wfdesktop.xmlapi.WFCallBroker" %>
<%@ page import="org.apache.commons.fileupload.*" %>
<%@ page import="org.apache.commons.fileupload.disk.*" %>
<%@ page import="org.apache.commons.fileupload.servlet.*" %>
<%@ page import="org.apache.commons.io.output.*" %>
<%@ page import = "com.lowagie.text.pdf.PdfReader, com.lowagie.text.pdf.codec.TiffImage, com.lowagie.text.pdf.RandomAccessFileOrArray"%>
<%@page import="com.newgen.omni.wf.util.excp.NGException"%>
<%@page import="com.newgen.omni.wf.util.app.NGEjbClient"%>
<%@ page import="XMLParser.XMLParser"%>
<%@page import="org.w3c.dom.Node"%>
<%@page import="org.w3c.dom.NodeList"%>
<%@page import="java.lang.Iterable"%>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page  import= "com.newgen.custom.*, org.w3c.dom.*, org.w3c.dom.NodeList,org.w3c.dom.Document,javax.xml.parsers.DocumentBuilder, javax.xml.parsers.DocumentBuilderFactory"%>
<%@ page  import= "org.w3c.dom.Element"%>
<%@ page  import= "java.io.File"%>
<%@ page  import= "java.util.LinkedHashMap"%>
<%@ page  import= "java.util.Map"%>
<%@ page  import= "java.io.ByteArrayInputStream"%>
<%@ page  import= "java.io.File"%>
<%@ page  import= "java.io.FileInputStream"%>
<%@ page  import= "java.io.FileNotFoundException"%>
<%@ page  import= "java.io.InputStream"%>
<%@ page import= "org.w3c.dom.Document"%>
<%@ page import = "java.util.ResourceBundle" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.owasp.esapi.ESAPI"%>
<%@ page import="org.owasp.esapi.codecs.OracleCodec"%>
<%@ page import="org.owasp.esapi.User" %>
<%@ page import="com.newgen.*" %>
<%@ page import="java.text.DateFormat,java.text.SimpleDateFormat,java.util.Date" %>


<script language="javascript" src="/webdesktop/resources/scripts/workdesk.js"></script>

<%
	
	//String input1 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("wi_name"), 1000, true) );
	//String wi_name = ESAPI.encoder().encodeForSQL(new OracleCodec(), input1);
	String wi_name =  ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("wi_name"), 1000, true) ;
	String input2 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("sessionId"), 1000, true) );
	String sessionId = ESAPI.encoder().encodeForSQL(new OracleCodec(), input2);
	String input3 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("WD_UID"), 1000, true) );
	String WD_UID = ESAPI.encoder().encodeForSQL(new OracleCodec(), input3);
	
	String input4 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("loggedInUser"), 1000, true) );
	String loggedInUser = ESAPI.encoder().encodeForSQL(new OracleCodec(), input4);
	
	String appServerType = "";
	String wrapperIP = "";
	String wrapperPort = "";    
	String cabinetName = "";
	String strInputXML = "";
	String strOutputXML = "";
	String MainCode = "";
	String workItemDone = "";
	XMLParser xmlParserData=null;
	String workitem_id = "1";
	 
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
	WriteLog("wi_name -- : "+wi_name);
	String workitem_name[] = wi_name.toString().split("~");
	if(workitem_name != null)
	{
		for(int i=0;i<workitem_name.length;i++)		
		{
			String tempWI = workitem_name[i];
			//WriteLog("length"+workitem_name.length);
			WriteLog("tempWI"+workitem_name);
			workitem_id = fetchWorkitem_id(tempWI, cabinetName,  sessionId,  wrapperIP, wrapperPort, appServerType);
			//call to lock the wi
			strInputXML	= 	"<?xml version=\"1.0\" ?>" + "\n" +
							"<WMGetWorkItem_Input>"+ "\n" +
							"<Option>WMGetWorkItem</Option>"+ "\n" +
							"<EngineName>" + cabinetName + "</EngineName>"+ "\n" +
							"<SessionId>" + sessionId + "</SessionId>"+ "\n" +
							"<ProcessInstanceId>" + tempWI + "</ProcessInstanceId>" + "\n" +
							"<WorkItemId>"+workitem_id+"</WorkItemId>"+ "\n" +
							"</WMGetWorkItem_Input>";
			WriteLog("Input XML for locking WI : " + strInputXML);
			strOutputXML = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXML);
			//strOutputXML=WFCallBroker.execute(strInputXML,wrapperIP,wrapperPort,1);
			WriteLog("Output XML for locking WI : " + strOutputXML);
			
			//MainCode = getTagValue(strOutputXML,"MainCode");
			
			xmlParserData=new XMLParser();
			xmlParserData.setInputXML((strOutputXML));
			MainCode = xmlParserData.getValueOf("MainCode");
			if(!MainCode.equalsIgnoreCase("0"))
			{
				workItemDone="No";
				out.clear();
				if(MainCode.equalsIgnoreCase("11")){
					out.println("Invalid Session!!!");
				}
				else{
					out.println("Some error occurred!!!");
				}
			}
			//call to set WI Attribute
			else{ 
					strInputXML	= 	"<?xml version=\"1.0\" ?>" + "\n" +
									"<WMAssignWorkItemAttributes_Input>"+ "\n" +
									"<Option>WMAssignWorkItemAttributes</Option>"+ "\n" +
									"<EngineName>" + cabinetName + "</EngineName>"+ "\n" +
									"<SessionId>" + sessionId + "</SessionId>"+ "\n" +
									"<ProcessInstanceId>" + tempWI + "</ProcessInstanceId>"+ "\n" +
									"<ProcessDefId>1</ProcessDefId>"+ "\n" +
									"<WorkItemId>"+workitem_id+"</WorkItemId>"+ "\n" +
									"<UserDefVarFlag>Y</UserDefVarFlag>"+ "\n" +
									"<Attributes>"+ "\n" +
									"<RouteToAutoBookFlag>Y</RouteToAutoBookFlag>"+ "\n" +
									"</Attributes>"+ "\n" +
									"</WMAssignWorkItemAttributes_Input>";
									
									


					WriteLog("Input XML for setAttribute : " + strInputXML);
					strOutputXML = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXML);
					WriteLog("Output XML for setAttribute : " + strOutputXML);
										
					xmlParserData.setInputXML((strOutputXML));
					MainCode = xmlParserData.getValueOf("MainCode");
					if(!MainCode.equalsIgnoreCase("0"))
					{
						workItemDone="No";
						unlockworkItem(tempWI,cabinetName,sessionId,wrapperIP,wrapperPort,appServerType,workitem_id);
						out.clear();
						out.println("Some error occurred...Please contact support!!!!");
					}
					else
					{
						WriteLog("before retriving entryDATETIME : ");
						String entryDATETIME=fetchEntryDateTime( tempWI, cabinetName,  sessionId,  wrapperIP, wrapperPort, appServerType);
						
						strInputXML	=	"<?xml version=\"1.0\"?>"+
										"<WMCompleteWorkItem_Input>"+
										"<Option>WMCompleteWorkItem</Option>"+
										"<EngineName>"+cabinetName+"</EngineName>"+
										"<SessionId>"+sessionId+"</SessionId>"+
										"<ProcessInstanceId>"+tempWI+"</ProcessInstanceId>"+
										"<WorkItemId>"+workitem_id+"</WorkItemId>"+
										"<AuditStatus></AuditStatus>"+
										"</WMCompleteWorkItem_Input>";
						WriteLog("Input XML for WI Done : " + strInputXML);
						strOutputXML = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXML);
						//strOutputXML=WFCallBroker.execute(strInputXML,wrapperIP,wrapperPort,1);
						WriteLog("Output XML for WI Done : " + strOutputXML);
						
						//MainCode = getTagValue(strOutputXML,"MainCode");
						
						xmlParserData.setInputXML((strOutputXML));
						MainCode = xmlParserData.getValueOf("MainCode");
			
						if(!MainCode.equalsIgnoreCase("0"))
						{
							workItemDone="No";						
							unlockworkItem(tempWI,cabinetName,sessionId,wrapperIP,wrapperPort,appServerType,workitem_id);
							out.clear();
							out.println("Error in getting maincode");
						}
						else
						{
							workItemDone="Yes";
							insertDecHistory(cabinetName,  sessionId, tempWI,loggedInUser,wrapperIP,wrapperPort,appServerType,entryDATETIME);
							out.clear();
							out.println(workItemDone);
						}
					}
				}
			
		  }
	}
	else
	{
		WriteLog("workitem  is Null");
		out.clear();
		out.println("No Workitem Selected");
	}

%>

<%!	

   String strOutputXML="";
   XMLParser xmlParserData=null;
  
	public void unlockworkItem(String WI_Name,String cabinetName,String sessionId,String wrapperIP,String wrapperPort, String appServerType, String workitem_id)
    {
        try
        {
        String strInputXML = "<?xml version=\"1.0\" ?>" + "\n" +
        "<WMUnlockWorkItem_Input>"+ "\n" +
        "<Option>WMUnlockWorkItem</Option>"+ "\n" +
        "<EngineName>" + cabinetName + "</EngineName>"+ "\n" +
        "<SessionId>" + sessionId + "</SessionId>"+ "\n" +
        "<ProcessInstanceId>" + WI_Name + "</ProcessInstanceId>" + "\n" +
        "<WorkItemId>"+workitem_id+"</WorkItemId>"+ "\n" +
        "</WMUnlockWorkItem_Input>";
        WriteLog("Input XML for unlock WI : " + strInputXML);
		strOutputXML = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXML);
        //String strOutputXML=WFCallBroker.execute(strInputXML,wrapperIP,wrapperPort,1);
        WriteLog("Output XML for unlock WI : " + strOutputXML);
        
		//String MainCode = getTagValue(strOutputXML,"MainCode");
		xmlParserData=new XMLParser();
		xmlParserData.setInputXML((strOutputXML));
		String MainCode = xmlParserData.getValueOf("MainCode");
        if(!MainCode.equalsIgnoreCase("0"))
        {
            //out.println("<script>alert('Error in getting maincode for unlocking WI');</script>");
        }
        }
        catch(Exception e)
        {}
    }
	
	public void insertDecHistory(String cabinetName, String sessionId, String wiName, String loggedInUser, String wrapperIP,String wrapperPort,String appServerType,String entry_datetime)
	{
		
		try{
			String mainCodeValue ="";
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String currDate = dateFormat.format(date); 
			 WriteLog( "insertDecHistory currDate" + currDate);
			 
			String sTableName = "ng_rlos_gr_Decision";
			String columnName = "dateLastChanged, userName, workstepName, Decision, remarks, dec_wi_name,Entry_date";
			String columnValues =  "'"+currDate+"',(select top 1 UserName from PDBUser with(nolock) where UserIndex in (select UserID from WFSESSIONVIEW with(nolock) where SessionID = '"+sessionId+"')),'Disbursal','Submit','WI moved for Bulk Disbursal','"+wiName+"','"+entry_datetime+"'";
			 String strInputXml =	ExecuteQuery_APInsert(sTableName,columnName,columnValues,cabinetName,sessionId);
					  WriteLog( "insertDecHistory strInputXml" + strInputXml);
					  try 
						{
							String strOutputHistoryXml = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, strInputXml);
							
							 xmlParserData=new XMLParser();
							  xmlParserData.setInputXML(strOutputHistoryXml);
							  mainCodeValue = xmlParserData.getValueOf("MainCode");
							 WriteLog("After insertDecHistory,maincode "+ mainCodeValue);
							if(mainCodeValue.equals("0"))
							{
								
								//WriteLog("insertDecHistory successfull ");
							}
							else
							{
								
								//WriteLog("insertDecHistory fail ");
							}
						} 
						catch (NGException e) 
						{
						   e.printStackTrace();
						} 
						catch (Exception ex) 
						{
						   ex.printStackTrace();
						}
			
		}
		 catch(Exception e)
        {
			 WriteLog("Exception occured while completing the case: " +e.getMessage());
        }
	}
	
	public static String ExecuteQuery_APInsert(String tableName,String columnName,String strValues, String cabinetName, String sessionId)
    {
    String sInputXML = "<?xml version=\"1.0\"?>" +"\n"+
            "<APInsert_Input>" +"\n"+
			"<Option>APInsert</Option>" +"\n"+
			"<TableName>" + tableName + "</TableName>" +"\n"+
			"<ColName>" + columnName + "</ColName>" +"\n"+
			"<Values>" + strValues + "</Values>" +"\n"+
			"<EngineName>" + cabinetName + "</EngineName>" +"\n"+
			"<SessionId>" + sessionId + "</SessionId>" +"\n"+
            "</APInsert_Input>";
    return sInputXML;	
	}
	
	public String fetchEntryDateTime(String Wi_name,String cabinetName, String sessionId, String wrapperIP,String wrapperPort,String appServerType){
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String entry_date_time = dateFormat.format(date); 
		int recordcount=0;
		try{
			String SqlQuery="select entryDATETIME from QUEUEVIEW where processinstanceid = '"+Wi_name+"' and activityname='Disbursal'";
			WriteLog("Input XML for selecting  EntryDateTime : " + SqlQuery);
			String sInputXML = "<?xml version='1.0'?>"+
			 "<APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option>"+
			 "<Query>" + SqlQuery + "</Query>"+
			 "<Params></Params>"+
			 "<EngineName>" + cabinetName + "</EngineName>"+
			 "<SessionId>" + sessionId + "</SessionId></APSelectWithColumnNames_Input>";
		  
			WriteLog("Input XML for selecting  EntryDateTime : " + sInputXML);
			String sOutputXML = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, sInputXML);
			WriteLog("Ouput XML for selecting EntryDateTime : " + sOutputXML); 
		
			xmlParserData=new XMLParser();
			xmlParserData.setInputXML((sOutputXML));
			String mainCodeValue = xmlParserData.getValueOf("MainCode");
			recordcount=Integer.parseInt(xmlParserData.getValueOf("TotalRetrieved"));
		
		 if	(mainCodeValue.equals("0") && recordcount>0){
			 entry_date_time = xmlParserData.getValueOf("entryDATETIME");
		 }
		}
		catch(Exception e){
			WriteLog("Excetion Occured while retriving entryDATETIME: "+e.getMessage());
		}
		return entry_date_time;
	}
	public String fetchWorkitem_id(String Wi_name,String cabinetName, String sessionId, String wrapperIP,String wrapperPort,String appServerType){
		int recordcount=0;
		String Workitem_id = "1";
		try{
			String SqlQuery="select WorkItemId from WFINSTRUMENTTABLE where ProcessInstanceID = '"+Wi_name+"' and ActivityName = 'CC_Disbursal'";
			WriteLog("Input XML for selecting  workitem ID : " + SqlQuery);
			String sInputXML = "<?xml version='1.0'?>"+
			 "<APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option>"+
			 "<Query>" + SqlQuery + "</Query>"+
			 "<Params></Params>"+
			 "<EngineName>" + cabinetName + "</EngineName>"+
			 "<SessionId>" + sessionId + "</SessionId></APSelectWithColumnNames_Input>";
		  
			WriteLog("Input XML for selecting  workitem ID : " + sInputXML);
			String sOutputXML = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, sInputXML);
			WriteLog("Ouput XML for selecting workitem ID : " + sOutputXML); 
		
			xmlParserData=new XMLParser();
			xmlParserData.setInputXML((sOutputXML));
			String mainCodeValue = xmlParserData.getValueOf("MainCode");
			recordcount=Integer.parseInt(xmlParserData.getValueOf("TotalRetrieved"));
		
		 if	(mainCodeValue.equals("0") && recordcount>0){
			 Workitem_id = xmlParserData.getValueOf("WorkItemId");
		 }
		}
		catch(Exception e){
			WriteLog("Excetion Occured while retriving workitem ID: "+e.getMessage());
		}
		return Workitem_id;
	}



	
		%>



