<%@ include file="Log.process"%>
<%@ page import="java.io.*,java.util.*,java.io.FileWriter"%>
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
<%@ page import="com.newgen.wfdesktop.xmlapi.WFCallBroker"%>
<%@ page  import= "java.util.Map"%>
<%@ page  import= "java.io.ByteArrayInputStream"%>
<%@ page  import= "java.io.File"%>
<%@ page  import= "java.io.FileInputStream"%>
<%@ page  import= "java.io.FileNotFoundException"%>
<%@ page  import= "java.io.InputStream"%>
<%@ page import="javax.xml.parsers.ParserConfigurationException,org.xml.sax.InputSource,org.xml.sax.SAXException" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.owasp.esapi.ESAPI"%>
<%@ page import="org.owasp.esapi.codecs.OracleCodec"%>
<%@ page import="org.owasp.esapi.User" %>
<%@ page import="com.newgen.*" %>
<jsp:useBean id="wDSession" class="com.newgen.wfdesktop.session.WDSession" scope="session"/>
<jsp:useBean id="WDCabinet" class="com.newgen.wfdesktop.baseclasses.WDCabinetInfo" scope="session"/>


  <%
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader ("Expires", 0);
		XMLParser objXmlParser=new XMLParser();

		String cabinetName="";
		String sInputXML="";
		String sOutputXML="";
		String params="";
		String wrapperPort="";
		String wrapperIP ="";
		String appServerType="";

		//String wiName_cardProduct = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("wiName_cardProduct"), 1000, true) );
		//String wiName_cardProduct = ESAPI.encoder().encodeForSQL(new OracleCodec(), input1);
		String wiName_cardProduct = (String)request.getParameter("wiName_cardProduct");
		WriteLog("wiName_cardProduct in starting of generate_CSV "+wiName_cardProduct);
		String input2 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("sessionId"), 1000, true) );
		String sessionId = ESAPI.encoder().encodeForSQL(new OracleCodec(), input2);
		
		String input3 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("userName"), 1000, true) );
		String userName = ESAPI.encoder().encodeForSQL(new OracleCodec(), input3);
		
		String input4 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("WD_UID"), 1000, true) );
		String WD_UID = ESAPI.encoder().encodeForSQL(new OracleCodec(), input4);
		
		//String sessionId =request.getParameter("sessionId");
		//String userName= (String)request.getParameter("userName");;
		String File_Generated_Name="";
		String mainCode=""; 
		String total_retrieved="";
		String tableName="";
		String columnList="";
		String sWhere="";
		String tempWI="";
		String tempCP="";
		String total_records="";
		String SqlQuery="";
		String records="";
		int startSequence=1000;
		
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
			WriteLog("cabinetName "+cabinetName);
			
			
			prop_file_loc=System.getProperty("user.dir") + File.separatorChar+"CustomConfig"+File.separatorChar+"Murabha.properties";
			file = new File(prop_file_loc);
			fileInput = new FileInputStream(file);
			 properties.load(fileInput);
			 fileInput.close();
			 tableName= properties.getProperty("tableName");
			 columnList= properties.getProperty("ColumnList");
			 startSequence= Integer.parseInt(properties.getProperty("startsequence"));
			WriteLog("tableName "+tableName);
			WriteLog("columnList "+columnList);
        } 
		catch(Exception e){
			
			WriteLog("Excetion Occured while reading from properties file: "+e.getMessage());
		}
	
	
	
	try
	{
		WriteLog("Inside function Generate_CSV" );
		String[] Wi_Name_array=wiName_cardProduct.split("~");
		String[] wi_CP_Arr;
		String status="";
		if(Wi_Name_array != null)
		{	
			sInputXML=ExecuteQuery_APUpdate(tableName,"Murhabha_WIName","''","1=2",cabinetName,sessionId);//dummy query to check session	
			sOutputXML = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, sInputXML);
			WriteLog(sInputXML );
			WriteLog(sOutputXML);

			objXmlParser.setInputXML(sOutputXML);
			mainCode=objXmlParser.getValueOf("MainCode");
			if(mainCode.equals("0"))
			{
				SqlQuery="SELECT max(isnull(File_id,0)) as uniqueField from ng_rlos_Murabha_Warranty with(nolock)";
				sInputXML = "<?xml version='1.0'?>"+
						 "<APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option>"+
						 "<Query>" + SqlQuery + "</Query>"+
						 "<Params>"+ params + "</Params>"+
						 "<EngineName>" + cabinetName + "</EngineName>"+
						 "<SessionId>" + sessionId + "</SessionId></APSelectWithColumnNames_Input>";
				WriteLog("sInputXML for selecting  WI : " + sInputXML);		 
				sOutputXML = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, sInputXML);
				WriteLog("sOutputXML for selecting  WI : " + sOutputXML);
				objXmlParser.setInputXML(sOutputXML);
				mainCode=objXmlParser.getValueOf("MainCode");
				total_retrieved=objXmlParser.getValueOf("TotalRetrieved");
				WriteLog("mainCode : " + mainCode+" :: total_retrieved"+total_retrieved);
				if(mainCode.equals("0") && Integer.parseInt(total_retrieved)>0)
				{
					if(!"0".equalsIgnoreCase(objXmlParser.getValueOf("uniqueField"))){
						startSequence=Integer.parseInt(objXmlParser.getValueOf("uniqueField"))+1;
					}
					
				}
				columnList = columnList.replace("File_id", startSequence+" as File_id");
				for(int i=0;i<Wi_Name_array.length;i++)	
				{		
					wi_CP_Arr = Wi_Name_array[i].split(",");
					tempWI = wi_CP_Arr[0];
					tempCP = wi_CP_Arr[1];
					
					SqlQuery="SELECT "+ columnList+" from "+tableName+" with(nolock) where Murhabha_WIName = "+tempWI+" and Card_Product = "+tempCP+" and (File_Generated_Name IS NULL OR File_Generated_Name = '' ) ";
					 sWhere="Murhabha_WIName = "+tempWI+" and Card_Product = "+tempCP+" and (File_Generated_Name IS NULL OR File_Generated_Name = '' ) ";	
					 WriteLog("Input XML for selecting  data : " + SqlQuery);
						 
					 sInputXML = "<?xml version='1.0'?>"+
								 "<APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option>"+
								 "<Query>" + SqlQuery + "</Query>"+
								 "<Params>"+ params + "</Params>"+
								 "<EngineName>" + cabinetName + "</EngineName>"+
								 "<SessionId>" + sessionId + "</SessionId></APSelectWithColumnNames_Input>";
							  
					
					sOutputXML = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, sInputXML);
					WriteLog("sOutputXML for selecting  WI : " + sOutputXML);
					objXmlParser.setInputXML(sOutputXML);
					mainCode=objXmlParser.getValueOf("MainCode");
					total_retrieved=objXmlParser.getValueOf("TotalRetrieved");
					records=objXmlParser.getValueOf("Records");
					WriteLog("Records is " + records);
					if(mainCode.equals("0") && Integer.parseInt(total_retrieved)>0)
					{
						if(total_records.equalsIgnoreCase(""))
						  total_records = records;
					  else
						  total_records =total_records+"~"+records;
					}			
			
				}
				
				Date d=new Date();	
				DateFormat logDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
				SimpleDateFormat df_new = new SimpleDateFormat("dd-MMM-yy");
				String timestamp=logDateFormat.format(d);
				String Trade_DATE = df_new.format(d);
				Calendar c = Calendar.getInstance();
				c.setTime(d);
				c.add(Calendar.YEAR, 10);
				d=c.getTime();
				String Warranty_Expiry_Date = df_new.format(d);
				File_Generated_Name="Murhabha_Warranty_"+timestamp;
				WriteLog("total_records is:" + total_records);
				if(!total_records.equals("")){
					status = Generate_File(total_records,columnList,File_Generated_Name,userName);
					if (status.equalsIgnoreCase("Success"))
					{
						for(int i=0;i<Wi_Name_array.length;i++)	
						{
							wi_CP_Arr = Wi_Name_array[i].split(",");
							tempWI = wi_CP_Arr[0];
							tempCP = wi_CP_Arr[1];
							
							sWhere="Murhabha_WIName = "+tempWI+" and Card_Product = "+tempCP+" ";	
							 
							sInputXML=ExecuteQuery_APUpdate(tableName,"File_Generated_Name,File_Generated_Date,Login_User_id,status,Trade_DATE,Settlement_Date,Warranty_Expiry_Date,File_id","'"+File_Generated_Name+"','"+timestamp+"','"+userName+"','File Generated','"+Trade_DATE+"','"+Trade_DATE+"','"+Warranty_Expiry_Date+"','"+startSequence+"'",sWhere,cabinetName,sessionId);
							WriteLog("After ExecuteQuery_APUpdate: " + sInputXML);
							sOutputXML = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, sInputXML);
							WriteLog("After APUpdate-->output: " + sOutputXML);
							objXmlParser.setInputXML(sOutputXML);
							String row_updated = objXmlParser.getValueOf("Output");
							//String 	row_updated = getTagValue(sOutputXML,"APUpdate_Output","Output");
							WriteLog("row_updated: "+row_updated);
							if(row_updated.equalsIgnoreCase("0")){
								WriteLog("NO row updated....!!!!!");
							}
							else{
								WriteLog("before out.print "+File_Generated_Name);	
											
							}										
						}
						out.clear();
						out.println(File_Generated_Name);	
					}
					else{
						out.clear();
						out.print("CSV file already generated for this wi!!");
					}
				}
				else{
					out.clear();
					out.print("Some error occurred...Please check logs!!!");	
				}				
			}
			else{
				out.clear();
				out.print("Session invalid....Please login again!!!!");	
			}	
		
		}	
			else{
				out.clear();
				out.print("No Workitem to process");
			}
		
		}
				
	catch(Exception e){		
			WriteLog("Exception Occurred while ng_rlos_Murabha_Warranty "+e.getMessage());
	}
	

  %>
  
  <%!

 public  List<String> parseXML(String sOutput)  throws SAXException, IOException, ParserConfigurationException
	{
	  WriteLog("inside parseXML sOutput is: "+sOutput);
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
    		   if("#text".equalsIgnoreCase(childNode.item(j).getNodeName())){
    			   
    		   }
    		   else if(j==1||j==0){
    		   result.add(temp,childNode.item(j).getTextContent().trim());
    		   }
    		   else{
    			   //Deepak Changes done to change | seprated file to , seprated
    			   result.set(temp,result.get(temp)+","+childNode.item(j).getTextContent().trim());
    		   }
    	   }
       }
    }
    
	WriteLog("Before returning result is: "+result);
	return result;
 } 

public  String Generate_File(String stotalOutputXML,String columnList,String fileName,String userName)
{
	XMLParser objXmlParser=new XMLParser();
	String sOutputXML="";
	String resultStatus="";
	
	try{		
		WriteLog("Inside Generate_File--->stotalOutputXML: "+stotalOutputXML);
			//objXmlParser.setInputXML(stotalOutputXML);
		
		
		String[] tempOutputXML=stotalOutputXML.split("~");
		String total_retrieved = Integer.toString(tempOutputXML.length);
		FileWriter fw  = new FileWriter(System.getProperty("user.dir") + File.separatorChar+"Murhabha"+File.separatorChar+"Generated"+File.separatorChar+fileName+".csv");
		//fw.append("H|Login_User_id|"+columnList.replaceAll(",","|")+"\n");
		for(int j=0;j<tempOutputXML.length;j++)	
		{		
			sOutputXML = tempOutputXML[j];
			
			//String total_retrieved = objXmlParser.getValueOf("TotalRetrieved");		
			//WriteLog("Generate_File total_retrieved :"+total_retrieved);
			List<String> result=parseXML(sOutputXML);
			WriteLog("parseXML result is"+result);
				
			
			for(int i=0;i<result.size();i++){
				//deepak changes done to remove file name in last column.
				//result.set(i,result.get(i)+fileName);
				result.set(i,result.get(i));
				//Deepak Changes done to change | seprated file to , seprated
				fw.append(""+userName+","+result.get(i)+"\n");
			}
		}
		//fw.append("T|"+total_retrieved);
		fw.close();
		resultStatus = "Success";
		return resultStatus;
		
	}
	catch(Exception e){
			
			WriteLog("Exception Occured in Generate_File"+e.getMessage());
			resultStatus = "Fail";
			
	}
	return resultStatus;
}

public  String ExecuteQuery_APUpdate(String tableName,String columnName,String strValues,String sWhere, String cabinetName, String sessionId)
    {
   String sInputXML = "<?xml version=\"1.0\"?>"+
								"<APUpdate_Input><Option>APUpdate</Option>"+
								"<TableName>"+tableName+"</TableName>"+
								"<ColName>"+columnName+"</ColName>"+
								"<Values>"+strValues+"</Values>"+
								"<WhereClause>"+sWhere+"</WhereClause>"+
								"<EngineName>"+cabinetName+"</EngineName>"+
								"<SessionId>"+sessionId+"</SessionId>"+
							"</APUpdate_Input>";
    return sInputXML;	
}	

public  String ExecuteQuery_APProcedure(String ProcName, String Params, String cabinetName ,String sessionId)
	{
		String sInputXML = "<?xml version=\"1.0\"?>\n" 
				+"<APProcedure_WithDBO_Input>\n" 
				+"<option>APProcedure_WithDBO</option>\n" 
				+"<ProcName>"+ProcName+"</ProcName>\n" 
				+"<Params>"+Params+"</Params>\n" 
				+"<EngineName>"+cabinetName+"</EngineName>\n"
				+"<SessionId>"+sessionId+"</SessionID>\n"
				+"</APProcedure_WithDBO_Input>";
		return sInputXML;
	}
  %>