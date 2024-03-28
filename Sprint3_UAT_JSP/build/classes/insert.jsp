<%@ page import="java.sql.*"%>
<%@ page import="java.util.Date,java.io.*,java.util.ArrayList,java.util.List" %>
<%@ page import="java.lang.Object"%>
<%@ page import="java.text.*"%>
<%@ page import="java.lang.String.*"%>

<%@page import="XMLParser.XMLParser"%>
<%@page import="com.newgen.omni.wf.util.excp.NGException,com.newgen.omni.wf.util.app.NGEjbClient"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.owasp.esapi.ESAPI"%>
<%@ page import="org.owasp.esapi.codecs.OracleCodec"%>
<%@ page import="org.owasp.esapi.User" %>
<%@ page import="com.newgen.*" %>
<jsp:useBean id="wDSession" class="com.newgen.wfdesktop.session.WDSession" scope="session"/>
<jsp:useBean id="WDCabinet" class="com.newgen.wfdesktop.baseclasses.WDCabinetInfo" scope="session"/>
    
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert In Database</title>
</head>
<body>
	<%    
		String input1 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("grpName"), 1000, true) );
		String grpName = ESAPI.encoder().encodeForSQL(new OracleCodec(), input1);
	
		String input2 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("To_Date"), 1000, true) );
		String To_Date = ESAPI.encoder().encodeForSQL(new OracleCodec(), input2);
	
		String input3 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("frm_Date"), 1000, true) );
		String frm_Date = ESAPI.encoder().encodeForSQL(new OracleCodec(), input3);
	
		String input4 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("assignTo"), 1000, true) );
		String assignTo = ESAPI.encoder().encodeForSQL(new OracleCodec(), input4);
	
		String input5 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("WD_UID"), 1000, true) );
		String WD_UID = ESAPI.encoder().encodeForSQL(new OracleCodec(), input5);
	     //String grpName=request.getParameter("grpname");
		 //String To_Date=request.getParameter("To_Date");
		 //String frm_Date=request.getParameter("frm_Date");
		 //String assignTo=request.getParameter("assignTo");
		 String SqlQuery="";
		
		 String grpIndex1=null;
		 String grpIndex2=null;
		 String[] UserIndex=null;
		 int j=0;
		 List<String> listUserIndex=new ArrayList();
		 List<String> listGroupIndex=new ArrayList();
		 String strFlag="";
		 Boolean found=false;
		 String sInputXML="";
		 String sOutputXML="";
		 XMLParser xmlParserData=null;
		 XMLParser objXmlParser=null;
		 String mainCodeValue="";
		 int recordcount=0;
		 String strGrpIndex="";	
		 String subXML="";
		 String mainCodeCheck ="0";
		 String sCabName="";
		 String sSessionId ="";
		 String wrapperPort="";
		 String wrapperIP ="";
		 String appServerType="";
		 String strFrmDate="";
		 String strToDate="";
		 List<String> listFrmDate= new ArrayList();
		 List<String> listToDate=new ArrayList();
		 Boolean isDateinRange=false;
		 Date dtFrmDate;
		 Date dtTo_Date;
	try{
			appServerType = wDSession.getM_objCabinetInfo().getM_strAppServerType();
			wrapperIP = wDSession.getM_objCabinetInfo().getM_strServerIP();
			wrapperPort = wDSession.getM_objCabinetInfo().getM_strServerPort()+"";    
			sSessionId = wDSession.getM_objUserInfo().getM_strSessionId();
			sCabName = wDSession.getM_objCabinetInfo().getM_strCabinetName();
			
			
			SimpleDateFormat formatter1=new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat formatter2=new SimpleDateFormat("MM/dd/yyyy");
			
			//Getting date string in 'mm/dd/yyyy' format and converting in 'yyyy-mm-dd' format
			dtFrmDate = formatter2.parse(frm_Date);
            String strFrm_Date= formatter1.format(dtFrmDate);
			//out.println(strFrm_Date);
			dtFrmDate= formatter1.parse(strFrm_Date);
			
             dtTo_Date = formatter2.parse(To_Date);
            String strTo_Date= formatter1.format(dtTo_Date);
			//out.println(strTo_Date);
			dtTo_Date=formatter1.parse(strTo_Date);
			
			//Checking if entered date by User already exists in table--start
			SqlQuery="Select From_Date, To_Date from NG_RM_LeaveInfo where RM_leave='"+grpName+"'and Flag='Y'";
			//out.println(SqlQuery);
			sInputXML = "<?xml version='1.0'?>"+
						"<APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option>"+
						 "<Query>" + SqlQuery + "</Query>"+
						 "<EngineName>" + sCabName + "</EngineName>"+
						 "<SessionId>" + sSessionId + "</SessionId></APSelectWithColumnNames_Input>";
			
			sOutputXML = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, sInputXML);
			xmlParserData=new XMLParser();
			xmlParserData.setInputXML((sOutputXML));
			mainCodeValue = xmlParserData.getValueOf("MainCode");
			if(mainCodeValue.equals("0")){
				recordcount=Integer.parseInt(xmlParserData.getValueOf("TotalRetrieved"));	
					//out.println("recordcount="+recordcount);
				 if(recordcount>0){
						for(int k=0; k<recordcount; k++){	
								subXML = xmlParserData.getNextValueOf("Record");
								objXmlParser = new XMLParser(subXML);
								strFrmDate=objXmlParser.getValueOf("From_Date");
								strToDate= objXmlParser.getValueOf("To_Date");
								listFrmDate.add(strFrmDate);
								listToDate.add(strToDate);
						}
				}			  
			}
				int len=listFrmDate.size();
				//out.println("len="+len);
				for(int iCount=0;iCount<len;iCount++){
					Date dtStartDate= formatter1.parse(listFrmDate.get(iCount));
					//out.println("dtStartDate="+listFrmDate.get(iCount));
					Date dtEndDate = formatter1.parse(listToDate.get(iCount));
					//out.println("dtEndDate="+listToDate.get(iCount));
					 if((dtFrmDate.after(dtStartDate)) && (dtFrmDate.before(dtEndDate)))  {
						 isDateinRange= true;
					}
					 else if(dtFrmDate.equals(dtEndDate))
						 isDateinRange= true;
					 else if((dtFrmDate.before(dtStartDate)) && (dtTo_Date.after(dtEndDate)))
					   isDateinRange= true;
					 else if((dtFrmDate.before(dtStartDate)) && (dtTo_Date.before(dtEndDate)))
						   isDateinRange= true;
					 else if(dtFrmDate.after(dtEndDate)){
						   isDateinRange= false;
					}
				}
			//Checking if entered date by User already exists in table--end
         	 //out.println("isDateinRange="+isDateinRange);
           		if(!isDateinRange){
					long timeDiff = Math.abs(dtTo_Date.getTime() - dtFrmDate.getTime()); 
					int days = (int) (timeDiff / (1000*60*60*24));
					
				     SqlQuery="SELECT GroupIndex FROM PDBGroup WHERE  Groupname='"+assignTo+"'" ;
				     sInputXML = "<?xml version='1.0'?>"+
								 "<APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option>"+
								 "<Query>" + SqlQuery + "</Query>"+
								 "<EngineName>" + sCabName + "</EngineName>"+
								 "<SessionId>" + sSessionId + "</SessionId></APSelectWithColumnNames_Input>";
					sOutputXML = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, sInputXML);
					 //out.println("SqlQuery of select from PDBGRoup= "+sOutputXML);
					 xmlParserData=new XMLParser();
					 xmlParserData.setInputXML((sOutputXML));
					 mainCodeValue = xmlParserData.getValueOf("MainCode");
					if(mainCodeValue.equals("0")){
					  recordcount=Integer.parseInt(xmlParserData.getValueOf("TotalRetrieved"));	
					
					  if(recordcount>0){
						  for(int k=0; k<recordcount; k++) {	
							 subXML = xmlParserData.getNextValueOf("Record");
							 objXmlParser = new XMLParser(subXML);
							 strGrpIndex=objXmlParser.getValueOf("GroupIndex");
							 }
							}
						  }
					  recordcount=0;
						
					grpIndex2= strGrpIndex;
					
					SqlQuery="SELECT userIndex FROM PDBGroupMember  WHERE GroupIndex IN (SELECT GroupIndex FROM PDBGroup WHERE Groupname='"+grpName+"')";
					
					sInputXML = "<?xml version='1.0'?>"+
								 "<APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option>"+
								 "<Query>" + SqlQuery + "</Query>"+
								 "<EngineName>" + sCabName + "</EngineName>"+
								 "<SessionId>" + sSessionId + "</SessionId></APSelectWithColumnNames_Input>";
					
					 
					sOutputXML = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, sInputXML); 
					 // out.println("SqlQuery="+sOutputXML);
					 xmlParserData=new XMLParser();
					 xmlParserData.setInputXML((sOutputXML));
					 mainCodeValue = xmlParserData.getValueOf("MainCode");
					 if(mainCodeValue.equals("0")){
					  recordcount=Integer.parseInt(xmlParserData.getValueOf("TotalRetrieved"));	
					  if(recordcount>0){
						 for(int k=0; k<recordcount; k++) {	
							 subXML = xmlParserData.getNextValueOf("Record");
							 objXmlParser = new XMLParser(subXML);
							 String index=objXmlParser.getValueOf("userIndex");
							 listUserIndex.add(index);
						   }
					 	}
					
					 int iSize = listUserIndex.size();
					 for(int iCount=0;iCount<iSize;iCount++){
					  sInputXML = "<?xml version=\"1.0\"?>" +
									"<APInsert_Input>" +
									"<Option>APInsert</Option>" +
									"<TableName>PDBGroupMember</TableName>" +
									"<ColName>" + "GroupIndex,userIndex" + "</ColName>" +
									"<Values>" + "'"+grpIndex2+"','"+listUserIndex.get(iCount)+"'</Values>" +
									"<EngineName>" + sCabName + "</EngineName>" +
									"<SessionId>" + sSessionId + "</SessionId>" +
									"</APInsert_Input>";
						sOutputXML = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, sInputXML);
						//out.println("sOutputXML of insert= "+sOutputXML);
						 }
					  }
					 recordcount=0;
					
					//if(sOutputXML.indexOf("<MainCode>0</MainCode>")>-1){
					
					 SqlQuery="SELECT ID FROM  NG_RM_LeaveInfo where RM_leave='"+grpName+"' AND RM_AssignedTo='"+assignTo+"' and From_date='"+strFrm_Date+"' and To_Date='"+strTo_Date+"'";
						
					 sInputXML = "<?xml version='1.0'?>"+
								 "<APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option>"+
								 "<Query>" + SqlQuery + "</Query>"+
								 "<EngineName>" + sCabName + "</EngineName>"+
								 "<SessionId>" + sSessionId + "</SessionId></APSelectWithColumnNames_Input>";
					  sOutputXML = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, sInputXML); 
					  out.println("SqlQuery="+sOutputXML);
					  xmlParserData=new XMLParser();
					  xmlParserData.setInputXML((sOutputXML));
					  mainCodeValue = xmlParserData.getValueOf("MainCode");
					  if(mainCodeValue.equals("0")){
					   recordcount=Integer.parseInt(xmlParserData.getValueOf("TotalRetrieved"));	
						 if(recordcount>0){
							subXML = xmlParserData.getNextValueOf("Record");
							 objXmlParser = new XMLParser(subXML);
							 String strRowId=objXmlParser.getValueOf("ID");
							 String sWhere="Id='"+strRowId +"'";
							 String sInputUpdateXML = "<?xml version=\"1.0\"?>" +
														"<APUpdate_Input>" +
															"<Option>APUpdate</Option>" +
															"<TableName>NG_RM_LeaveInfo</TableName>" +
															"<ColName>Flag</ColName>" +
															"<Values>Y</Values>" +
															"<WhereClause>"+sWhere+"</WhereClause>"+
															"<EngineName>" + sCabName + "</EngineName>" +
															"<SessionId>" + sSessionId + "</SessionId>" +
														"</APUpdate_Input>";
							 sOutputXML = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, sInputXML);
							}else{
								sInputXML = "<?xml version=\"1.0\"?>" +
											"<APInsert_Input>" +
												"<Option>APInsert</Option>" +
												"<TableName>NG_RM_LeaveInfo</TableName>" +
												"<ColName>" + "RM_Leave,RM_AssignedTo,From_Date,To_Date,Duration_IN_Days,Flag" + "</ColName>" +
												"<Values>" + "'"+grpName+"','"+assignTo+"','"+strFrm_Date +"','"+strTo_Date +"','"+(days+1) +"','Y'</Values>" +
												"<EngineName>" + sCabName + "</EngineName>" +
												"<SessionId>" + sSessionId + "</SessionId>" +
											"</APInsert_Input>";
						//out.println("sOutputXML of insert to leaveinfo= "+sInputXML);
							
						 		sOutputXML = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, sInputXML);
							 		}
							}
					
					
					  //out.println("sOutputXML of insert to leaveinfo= "+sOutputXML.indexOf("<MainCode>0</MainCode>"));
					
						if(sOutputXML.indexOf("<MainCode>0</MainCode>")>-1)
						{
							//mainCodeCheck2=sOutputXML.substring(sOutputXML.indexOf("<Results>")+"<Results>".length(),sOutputXML.indexOf("</Results>"));					
							mainCodeCheck="34";	//If task is successfully assigned
							//out.println(mainCodeCheck);
						}
											
										
				} else
						   mainCodeCheck="12";		//If date alreday exists 
			}catch(Exception e){
		        e.printStackTrace();
				//out.println("Exception="+e.getMessage());
	        }
        %>
        
      
</body>
</html>
<%
out.clear();
out.println(mainCodeCheck);		
%>