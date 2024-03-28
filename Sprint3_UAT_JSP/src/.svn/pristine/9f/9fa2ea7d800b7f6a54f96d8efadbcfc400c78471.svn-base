<!------------------------------------------------------------------------------------------------------
//Group						 : Application ?Projects
//Product / Project			 : RAKBank-RLOS
//File Name					 : 
//Author                     :Tanshu Aggarwal
//Date written (DD/MM/YYYY)  : 2/02/2017

//Change historty
//Name				date			reasone								comment
//Deepak Kumar		14-Sept-2017	to run EIDA Genuine for mobility    Changes done for EIDA from Mobility, select call header column name is chaged.

//---------------------------------------------------------------------------------------------------->



<%@ include file="Log.process"%>
<%@ page import="java.io.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.net.*"%>
<%@ page import="java.net.SocketTimeoutException"%>
<%@ page import="javax.xml.parsers.ParserConfigurationException" %>
<%@ page import="com.newgen.wfdesktop.exception.*" %>
<%@ page import="com.newgen.wfdesktop.xmlapi.*" %>
<%@ page import="com.newgen.wfdesktop.util.*" %>
<%@ page import="com.newgen.custom.*" %>
<%@ page import="java.io.UnsupportedEncodingException" %>
<%@ page import="com.newgen.mvcbeans.model.wfobjects.WFDynamicConstant, com.newgen.mvcbeans.model.*,com.newgen.mvcbeans.controller.workdesk.*, javax.faces.context.FacesContext"%>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@page import="com.newgen.omni.wf.util.excp.NGException"%>
<%@page import="com.newgen.omni.wf.util.app.NGEjbClient"%>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page  import= "com.newgen.custom.*, org.w3c.dom.*, org.w3c.dom.Node , org.w3c.dom.NodeList , org.w3c.dom.Document , javax.xml.parsers.DocumentBuilder , javax.xml.parsers.DocumentBuilderFactory"%>
<%@ page import="javax.xml.transform.Transformer, javax.xml.transform.TransformerFactory, javax.xml.transform.dom.DOMSource, javax.xml.transform.stream.StreamResult"%>
<%@ page import ="java.text.DecimalFormat,org.xml.sax.InputSource"%>
<%@ page import="org.json.JSONObject"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.io.FileInputStream" %>
<%@ page import="java.io.File" %>
<%@ page language="java" import="java.util.*" %> 
<%@ page import = "java.util.ResourceBundle" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.owasp.esapi.ESAPI"%>
<%@ page import="org.owasp.esapi.codecs.OracleCodec"%>
<%@ page import="org.owasp.esapi.User" %>


<%
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader ("Expires", 0);
    //All information will be available through wDSession object. Refer below sample code.
    WriteLog("in requestxmljsp");
	String outputResponse="";
	String sQuery="";
	String call_type="";
	String Call_name="";
	String form_control="";
	String Mobility_form_control="";
	String parent_tag="";
	String tag_name="";
	String is_repetitive ="";
	String parent_tag_name="";
	String xmltag_name="";
	String inputXML="";
	String outputXML="";
	String OutputXML_header="";
	String mqInputRequest="";
	/*String is_mobility = request.getParameter("is_mobility");
	String sessionId = request.getParameter("SeesionId");
	WriteLog("sessionId :" + sessionId);*/
    String operation_type= "";
	String appServerType="";
	String wrapperIP = "";
	String wrapperPort = "";
	String cabinetName = "";
	//svt points start
	String row_count1 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("row_count"), 1000, true) );
	String row_count = ESAPI.encoder().encodeForSQL(new OracleCodec(), row_count1);
	//String row_count = request.getParameter("row_count"); 
	
	String CardNumber1 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("CardNumber"), 1000, true) );
	String CardNumber = ESAPI.encoder().encodeForSQL(new OracleCodec(), CardNumber1);
	//String CardNumber = request.getParameter("CardNumber"); 
	String input3 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("WD_UID"), 1000, true) );	
	String WD_UID = ESAPI.encoder().encodeForSQL(new OracleCodec(), input3);
	//svt points end
	String socketServerIP="";
	Integer socketServerPort=0;
	NGEjbClient ngejclient_obj=null;
	
	
	try {
			ngejclient_obj = NGEjbClient.getSharedInstance();
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
			socketServerIP = properties.getProperty("mq_Integration_Ip");
			socketServerPort = Integer.parseInt(properties.getProperty("mq_Integration_Port"));
        } 
		catch(Exception e){
			out.print("false"+"!@#"+row_count+"!@#"+CardNumber);
			WriteLog("Excetion Occured while reading from properties file: "+e.getMessage());
		}
	
	JSONObject jsonObj=null;
	String form_control_val=null;
	
	String input1 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("is_mobility"), 1000, true) );	
	String is_mobility = ESAPI.encoder().encodeForSQL(new OracleCodec(), input1);
	
	String input2 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("SeesionId"), 1000, true) );	
	String sessionId = ESAPI.encoder().encodeForSQL(new OracleCodec(), input2);
	/*String request_name = request.getParameter("request_name");	
	String wi_name = request.getParameter("wi_name");	
	String ws_name = request.getParameter("activityName");	
	String cifId = request.getParameter("cifId");
	String acc_id = request.getParameter("acc_id");
	String Customer_type = request.getParameter("Customer_Type");*/
	
	
	String input4 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("request_name"), 1000, true) );	
	String request_name = ESAPI.encoder().encodeForSQL(new OracleCodec(), input4);
	WriteLog("%%%%%% request_name : "+request_name);
	String input5 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("wi_name"), 1000, true) );	
	String wi_name = ESAPI.encoder().encodeForSQL(new OracleCodec(), input5);
	WriteLog("before esapi: wi_name "+wi_name);
	String input6 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("activityName"), 1000, true) );	
	String ws_name = ESAPI.encoder().encodeForSQL(new OracleCodec(), input6);	
	WriteLog("before esapi: ws_name "+ws_name);
	String input7 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("cifId"), 1000, true) );	
	String cifId = ESAPI.encoder().encodeForSQL(new OracleCodec(), input7);
	
	String input8 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("acc_id"), 1000, true) );	
	String acc_id = ESAPI.encoder().encodeForSQL(new OracleCodec(), input8);
	
	String input9 = ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("Customer_Type"), 1000, true) );	
	String Customer_type = ESAPI.encoder().encodeForSQL(new OracleCodec(), input9);
	WriteLog("%%%%%%%%%%%%% input9 %%%%%%%%%%% ");
	request_name = request_name.trim();
	String request_Arr[]=request_name.split(",");
	WriteLog("jsonobj1is_mobility --: "+is_mobility);	
	WriteLog("jsonobj2: request_name "+request_name);
	WriteLog("jsonobj2: ws_name "+ws_name);
	WriteLog("jsonobj2: wi_name "+wi_name);
	WriteLog("jsonobj2: cifId "+cifId);
	WriteLog("jsonobj2: Customer_type "+Customer_type);
	
	if(is_mobility.equalsIgnoreCase("Y")){
		String operation_type1=ESAPI.encoder().encodeForHTML( ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("acc_id"), 1000, true) );
		operation_type=ESAPI.encoder().encodeForSQL(new OracleCodec(), operation_type1);
		//operation_type = request.getParameter("operation_type");
		WriteLog("INSIDE MOB");
		String jsonobj1=request.getParameter("param_json");
		jsonObj= new JSONObject(jsonobj1);
		WriteLog("json result Result: " + jsonObj);	
		WriteLog("jsonobj1: "+jsonobj1);
		Customer_type = jsonObj.getString("Customer_Type");		
	}
	else{
		String jsonobj2 = ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("param_json"), 1000, true);	
		jsonObj= new JSONObject(jsonobj2);
		
		if(request_name.equalsIgnoreCase("CARD_INSTALLMENT_DETAILS")){
			if(CardNumber!=null && !CardNumber.equalsIgnoreCase("")){
			jsonObj.put("CardNumber",CardNumber);
			WriteLog("jsonobj2: jsonObj123 CardNumber"+CardNumber);
			}
		}
		
		
		jsonObj.put("cmplx_Customer_CIFNO",cifId.trim());
		
		//Changes done for company AECB start
		if(request_name.equalsIgnoreCase("ExternalExposure")){
			WriteLog("inside ExternalExposure condition");
			String comp_cif = jsonObj.getString("cif");
			String trade_comp_name= jsonObj.getString("trade_comp_name");
			String trade_lic_no = jsonObj.getString("trade_lic_no");
			if(comp_cif.contains(",")){
				WriteLog("Company cif contains multiple cif");
				String[] comp_cif_arr = comp_cif.split(",");
				String[] trade_comp_name_arr = trade_comp_name.split(",");
				String[] trade_lic_no_arr = trade_lic_no.split(",");
				int item_indx = 0 ;
				for(int x=0;x<comp_cif_arr.length;x++){
						if(comp_cif_arr[x].equalsIgnoreCase(cifId.trim()))
							item_indx=x;
					}
				jsonObj.put("trade_comp_name",trade_comp_name_arr[item_indx]);
				jsonObj.put("trade_lic_no",trade_lic_no_arr[item_indx]);
			}
			String EIDA_no = jsonObj.getString("cmplx_Customer_EmiratesID");
			if(!"".equalsIgnoreCase(EIDA_no)){
				EIDA_no = EIDA_no.substring(0, 3)+"-"+EIDA_no.substring(3, 7)+"-"+EIDA_no.substring(7, 14)+"-"+EIDA_no.charAt(14);
				jsonObj.put("cmplx_Customer_EmiratesID",EIDA_no);
			}
		}
		//Changes done for company AECB end
		if(cifId!=null && !cifId.equalsIgnoreCase("")){
			jsonObj.put("cmplx_Customer_CIFNO",cifId.trim());
		}
		else{
			jsonObj.put("cmplx_Customer_CIFNO","");
					WriteLog("jsonobj2: jsonObj "+jsonObj);
		}
		
		if(acc_id!=null && !acc_id.equalsIgnoreCase("")){
			jsonObj.put("acc_id",acc_id.trim());
		}
		else{
			jsonObj.put("acc_id","");
					WriteLog("jsonobj2: jsonObj "+jsonObj);
		}
	}
	
	
	WriteLog("RLOSCommon");

		StringBuilder final_xml= new StringBuilder("");
		String header ="";
	    String footer = "";
	    String parentTagName="";
	    Socket socket = null;
		OutputStream outstream = null;
		InputStream socketInputStream = null;
	    DataOutputStream dout = null;
	    DataInputStream din = null;
	    String mqOutputResponse="";
		String default_val=null;
	    String data_format12=null;
	    String userName=null;
		String mainCodeData=null;
	    XMLParser xmlParserData=null;
		String sQuery_header=null;
		String fin_call_name="Customer_details, Customer_eligibility,new_account_req";
		
	   WriteLog("$$outputgGridtXML");
		try
		{
			
			for(int i =0;i<request_Arr.length;i++){
				if(is_mobility.equalsIgnoreCase("Y")){
				if(request_Arr[i].equalsIgnoreCase("TRANSUM") || request_Arr[i].equalsIgnoreCase("AVGBALDET") || request_Arr[i].equalsIgnoreCase("RETURNDET")|| request_Arr[i].equalsIgnoreCase("LIENDET") || request_Arr[i].equalsIgnoreCase("SIDET")|| request_Arr[i].equalsIgnoreCase("SALDET")||request_Arr[i].equalsIgnoreCase("Primary_CIF")|| request_Arr[i].equalsIgnoreCase("Corporation_CIF"))
				{
					//Changes done for EIDA from Mobility, select call header column name is chaged.
					WriteLog("INSIDE FINANCIAL_SUMMARY case 1 = "+request_Arr[i]);
					sQuery_header = "SELECT Header as Call_header,Footer,parenttagname FROM NG_Integration with (nolock) where Call_name='FINANCIAL_SUMMARY'";
				}
				
				else if ((request_Arr[i].equalsIgnoreCase("OTP_SUMMARY"))&&(is_mobility.equalsIgnoreCase("Y"))){
					WriteLog("INSIDE OTP_Summary case ="+request_Arr[i]);
					sQuery_header = "SELECT Header as Call_header,Footer,parenttagname  FROM NG_Integration with (nolock) where Call_name='OTP_MANAGEMENT'";
				}
				else{
					sQuery_header = "SELECT Header as Call_header,Footer,parenttagname FROM NG_Integration with (nolock) where Call_name='"+request_Arr[i]+"'";
				}
			
						
	        WriteLog("RLOSCommon--- "+sQuery_header);
			inputXML = "<?xml version='1.0'?><APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option><Query>" + sQuery_header + "</Query><EngineName>" + cabinetName + "</EngineName><SessionId>" + sessionId+ "</SessionId></APSelectWithColumnNames_Input>";
			
			WriteLog("inputXML -->"+inputXML);
			//OutputXML_header = WFCallBroker.execute(inputXML, wrapperIP, Integer.parseInt(wDSession.getM_objCabinetInfo().getM_strServerPort()), appServerType);
			
			try 
			{
				OutputXML_header = ngejclient_obj.makeCall(wrapperIP, wrapperPort, appServerType, inputXML);
			} 
			catch (NGException e) 
			{
			   e.printStackTrace();
			} 
			catch (Exception ex) 
			{
			   ex.printStackTrace();
			}
			WriteLog("outputXML exceptions-->"+OutputXML_header);
	        
			xmlParserData = new XMLParser();
			xmlParserData.setInputXML((OutputXML_header));
			
			int recordcount = Integer.parseInt(xmlParserData.getValueOf("TotalRetrieved"));
			WriteLog("Number of records are ="+recordcount);
			
			mainCodeData = xmlParserData.getValueOf("MainCode");
			}
			else{
			//svt points start
			mainCodeData = ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("maincode"), 1000, true);//request.getParameter("maincode");
			}
	
			if(mainCodeData.equals("0"))
	       	{
				//Changes done for EIDA from Mobility, select call header column name is chaged.
				if(is_mobility.equalsIgnoreCase("Y")){
	        	header = xmlParserData.getNextValueOf("Call_header");
	            footer = xmlParserData.getNextValueOf("Footer");
	            parentTagName = xmlParserData.getNextValueOf("parenttagname");
				}
				else{
				//svt points start
				header = ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("header"), 1000, true);//request.getParameter("header");
	            footer = ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("footer"), 1000, true);//request.getParameter("footer");
	            parentTagName = ESAPI.validator().getValidSafeHTML("htmlInput", request.getParameter("parentTagName"), 1000, true);//request.getParameter("parentTagName");
				//svt points end
				}
	            WriteLog("header ="+header+" footer="+footer+" parentTagName"+parentTagName );
				WriteLog("operation_type : "+ operation_type);
				if(is_mobility.equalsIgnoreCase("Y"))
				{ 
					WriteLog("INSIDE MOBILITY CALLING DB ="+request_Arr[i]);
					
					if(request_Arr[i].equalsIgnoreCase("INTERNALEXPOSURE") || request_Arr[i].equalsIgnoreCase("COLLECTIONSSUMMARY")||request_Arr[i].equalsIgnoreCase("CARD_INSTALLMENT_DETAILS"))
					{
						sQuery = "SELECT call_type, Call_name,Mobility_form_control,parent_tag_name,xmltag_name,is_repetitive,default_val,data_format FROM NG_Integration_field_Mapping with (nolock) where Call_name='"+ request_Arr[i] +"' and  Active = 'Y' ORDER BY tag_seq ASC" ;
					}
					
					else if(request_Arr[i].equalsIgnoreCase("EXTERNALEXPOSURE"))
					{
					//Changes done by aman for AECB	Start 
					sQuery = "SELECT call_type, Call_name,Mobility_form_control,parent_tag_name,xmltag_name,is_repetitive,default_val,data_format FROM NG_Integration_field_Mapping with (nolock) where Call_name='"+ request_Arr[i] +"' and Operation_Name='"+Customer_type+"' and Active = 'Y' ORDER BY tag_seq ASC" ;
					}
					//Changes done by aman for AECB	end 
					
					
					else if(request_Arr[i].equalsIgnoreCase("TRANSUM") || request_Arr[i].equalsIgnoreCase("AVGBALDET") || request_Arr[i].equalsIgnoreCase("RETURNDET")|| request_Arr[i].equalsIgnoreCase("LIENDET") || request_Arr[i].equalsIgnoreCase("SIDET")|| request_Arr[i].equalsIgnoreCase("SALDET")||request_Arr[i].equalsIgnoreCase("Primary_CIF")|| request_Arr[i].equalsIgnoreCase("Corporation_CIF"))
					{
						sQuery = "SELECT call_type, Call_name,Mobility_form_control,parent_tag_name,xmltag_name,is_repetitive,default_val,data_format FROM NG_Integration_field_Mapping with (nolock) where Call_name='FINANCIAL_SUMMARY' and Operation_name='"+jsonObj.getString("OperationType")+"' ORDER BY tag_seq ASC" ;
					}
					else if (request_Arr[i].equalsIgnoreCase("OTP_Management"))
					{
						WriteLog("INSIDE OTP_Management ="+request_Arr[i]);
						
						sQuery = "SELECT call_type, Call_name,Mobility_form_control,parent_tag_name,xmltag_name,is_repetitive,default_val,data_format FROM NG_Integration_field_Mapping with (nolock) where Call_name='OTP_MANAGEMENT' and  Operation_name='"+operation_type+"' and  Active = 'Y' ORDER BY tag_seq ASC";
						
					}
					else if (request_Arr[i].equalsIgnoreCase("Customer_Details"))
					{
						WriteLog("INSIDE CustDE|T ="+request_Arr[i]);
						sQuery = "SELECT call_type, Call_name,Mobility_form_control,parent_tag_name,xmltag_name,is_repetitive,default_val,data_format FROM NG_Integration_field_Mapping with (nolock) where Call_name='Customer_Details' and  Operation_name='Primary_CIF' and  Active = 'Y' ORDER BY tag_seq ASC";
					}
					else if (request_Arr[i].equalsIgnoreCase("OTP_Summary"))
					{
						WriteLog("INSIDE OTP_Summary ="+request_Arr[i]);
						
						sQuery = "SELECT call_type, Call_name,Mobility_form_control,parent_tag_name,xmltag_name,is_repetitive,default_val,data_format FROM NG_Integration_field_Mapping with (nolock) where Call_name='OTP_MANAGEMENT' and  Operation_name='ValidateOTP' and  Active = 'Y' ORDER BY tag_seq ASC";
					}
					else if (operation_type!=null && !operation_type.equalsIgnoreCase("")){
						sQuery = "SELECT call_type, Call_name,Mobility_form_control,parent_tag_name,xmltag_name,is_repetitive,default_val,data_format FROM NG_Integration_field_Mapping with (nolock) where Call_name='"+request_Arr[i]+"' and Operation_name='"+operation_type+"'and  Active = 'Y' ORDER BY tag_seq ASC" ;
					}
					else
					{
						sQuery = "SELECT call_type, Call_name,Mobility_form_control,parent_tag_name,xmltag_name,is_repetitive,default_val,data_format FROM NG_Integration_field_Mapping with (nolock) where Call_name='"+request_Arr[i]+"' and  Active = 'Y' ORDER BY tag_seq ASC" ;
					}
				} 
				else
				{
					if( request_Arr[i].equalsIgnoreCase("INTERNALEXPOSURE") || request_Arr[i].equalsIgnoreCase("COLLECTIONSSUMMARY")||request_Arr[i].equalsIgnoreCase("CARD_INSTALLMENT_DETAILS"))
					{
						
						sQuery = "SELECT call_type, Call_name,form_control,parent_tag_name,xmltag_name,is_repetitive,default_val,data_format FROM NG_Integration_field_Mapping with (nolock) where Call_name='"+ request_Arr[i] +"' and Active = 'Y' ORDER BY tag_seq ASC" ;
					}
						 
						
					else if(request_Arr[i].equalsIgnoreCase("EXTERNALEXPOSURE"))
					{
					//Changes done by aman for AECB	Start 
					sQuery = "SELECT call_type, Call_name,form_control,parent_tag_name,xmltag_name,is_repetitive,default_val,data_format FROM NG_Integration_field_Mapping with (nolock) where Call_name='"+ request_Arr[i] +"' and Operation_Name='"+Customer_type+"' and Active = 'Y' ORDER BY tag_seq ASC" ;
					}
					//Changes done by aman for AECB	end 
					
					else if(request_Arr[i].equalsIgnoreCase("TRANSUM") || request_Arr[i].equalsIgnoreCase("AVGBALDET") || request_Arr[i].equalsIgnoreCase("RETURNDET")|| request_Arr[i].equalsIgnoreCase("LIENDET") || request_Arr[i].equalsIgnoreCase("SIDET")|| request_Arr[i].equalsIgnoreCase("SALDET")||request_Arr[i].equalsIgnoreCase("Primary_CIF")|| request_Arr[i].equalsIgnoreCase("Corporation_CIF"))
					{
						sQuery = "SELECT call_type, Call_name,form_control,parent_tag_name,xmltag_name,is_repetitive,default_val,data_format FROM NG_Integration_field_Mapping with (nolock) where Call_name='FINANCIAL_SUMMARY' and Operation_name='"+ request_Arr[i] +"' ORDER BY tag_seq ASC" ;
					}
					else
					{
						sQuery = "SELECT call_type, Call_name,form_control,parent_tag_name,xmltag_name,is_repetitive,default_val,data_format FROM NG_Integration_field_Mapping with (nolock) where Call_name='"+request_Arr[i]+"'ORDER BY tag_seq ASC" ;
					}									
				}
	    		
				
				WriteLog("sQuery123 "+sQuery);
				inputXML = "<?xml version='1.0'?><APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option><Query>" + sQuery + "</Query><EngineName>" + cabinetName + "</EngineName><SessionId>" + sessionId+ "</SessionId></APSelectWithColumnNames_Input>";
				//outputXML = WFCallBroker.execute(inputXML, wrapperIP, Integer.parseInt(wDSession.getM_objCabinetInfo().getM_strServerPort()), appServerType);
				
				try 
				{
					outputXML = ngejclient_obj.makeCall(wrapperIP, wrapperPort, appServerType, inputXML);
				} 
				catch (NGException e) 
				{
				   e.printStackTrace();
				} 
				catch (Exception ex) 
				{
				   ex.printStackTrace();
				}
				
				WriteLog("outputXML exceptions-->"+outputXML);
				xmlParserData = new XMLParser();
				xmlParserData.setInputXML((outputXML));
				int recordcount1 = Integer.parseInt(xmlParserData.getValueOf("TotalRetrieved"));
				WriteLog("Number of records are in 2nd query are ="+recordcount1);
				mainCodeData = xmlParserData.getValueOf("MainCode");
				if(mainCodeData.equals("0"))
				{
					final_xml.setLength(0);
					WriteLog("final_xml in maincode:"+final_xml);
					Map<String, String> int_xml = new LinkedHashMap<String, String>();
	        		Map<String, String> recordFileMap = new HashMap<String, String>();

				 for(int k=0; k<recordcount1; k++)
				 {
					 call_type = xmlParserData.getNextValueOf("call_type");
					 Call_name = xmlParserData.getNextValueOf("Call_name");
					 if(is_mobility.equalsIgnoreCase("Y")){
						form_control = xmlParserData.getNextValueOf("Mobility_form_control");
					 }
					 else{
						form_control = xmlParserData.getNextValueOf("form_control");
					 }
					 parent_tag_name = xmlParserData.getNextValueOf("parent_tag_name");
					 xmltag_name = xmlParserData.getNextValueOf("xmltag_name");
					 is_repetitive = xmlParserData.getNextValueOf("is_repetitive");
					 default_val = xmlParserData.getNextValueOf("default_val");
					 data_format12 = xmlParserData.getNextValueOf("data_format");
					 WriteLog("call_type ="+call_type+" Call_name="+Call_name+" form_control="+form_control+" parent_tag_name="+parent_tag_name+" xmltag_name="+xmltag_name+" is_repetitive="+is_repetitive+"default_val="+default_val );
						
	        				recordFileMap.put("call_type",call_type);
							recordFileMap.put("Call_name",Call_name);
							recordFileMap.put("form_control",form_control);
							recordFileMap.put("parent_tag_name",parent_tag_name);
							recordFileMap.put("xmltag_name",xmltag_name);
							recordFileMap.put("is_repetitive",is_repetitive);
							recordFileMap.put("default_val",default_val);
							recordFileMap.put("data_format",data_format12);
	        				
	        				 call_type = (String) recordFileMap.get("call_type");
	        				 Call_name = (String) recordFileMap.get("Call_name");
	        				 form_control = (String) recordFileMap.get("form_control");
							 parent_tag = (String) recordFileMap.get("parent_tag_name");
	        				 tag_name = (String) recordFileMap.get("xmltag_name");
	        				 is_repetitive = (String) recordFileMap.get("is_repetitive");
							 default_val=(String) recordFileMap.get("default_val");
							  data_format12 = (String) recordFileMap.get("data_format");
							WriteLog("call_type ="+call_type+" Call_name="+Call_name+" form_control="+form_control+" parent_tag_name="+	parent_tag_name+" xmltag_name="+xmltag_name+" is_repetitive="+is_repetitive+"default_val="+default_val );
		                    					
							 form_control_val="";
	                            java.util.Date startDate;
	        				
							
						   if (parent_tag!=null && !parent_tag.equalsIgnoreCase(""))
                            {
                            	WriteLog("inside 1st if inside 1st if Parent tag: "+parent_tag+" Child tag: "+tag_name);
								WriteLog("inside 1st if int_xml: "+int_xml);
                                if(int_xml.containsKey(parent_tag))
                                {
                                	WriteLog("inside 1st if inside 2nd if");
                                    String xml_str = int_xml.get(parent_tag);
                                    WriteLog("inside 1st if inside 2nd if xml string"+xml_str);
                                    if(is_repetitive.equalsIgnoreCase("Y") && int_xml.containsKey(tag_name)){
                                    	WriteLog("inside 1st if inside 3rd if xml string");
                                        xml_str = int_xml.get(tag_name)+ "</"+tag_name+">" +"<"+ tag_name+">";
                                      //  System.out.println("value after adding "+ Call_name+": "+xml_str);
                                        WriteLog("inside 1st if inside 3rd if xml string xml string"+xml_str);
                                        int_xml.remove(tag_name);
                                        int_xml.put(tag_name, xml_str);
                                        WriteLog("inside 1st if inside 3rd if xml string xml string int_xml");
                                    }
                                    else{
                                    	WriteLog("inside else of parent tag value after adding "+ Call_name+": "+xml_str+" form_control name: "+form_control);
										WriteLog(" valuie of form control123: "+form_control);
										
										if(form_control.equalsIgnoreCase("") && default_val.equalsIgnoreCase("")){
											WriteLog("inside if added by me inside");
											xml_str = xml_str + "<"+tag_name+">"+"</"+ tag_name+">";
											WriteLog("added by xml xml_str"+xml_str);
										}
										else if (!(form_control==null || form_control.equalsIgnoreCase("")||  form_control.equalsIgnoreCase("null"))){
	                                            WriteLog("inside else of parent tag 1 form_control_val"+ form_control_val);
												if(fin_call_name.toUpperCase().contains(request_Arr[i].toUpperCase())){
													form_control_val = (jsonObj.getString(form_control)+"").toUpperCase();
												}
												else {
													form_control_val = jsonObj.getString(form_control)+"";
												}
	                                            
	                                            if(!data_format12.equalsIgnoreCase("text")){
		                                        	String[] format_arr = data_format12.split(":");
		                                        	String format_name = format_arr[0];
		                                        	String format_type = format_arr[1];
		                                        	WriteLog(""+"format_name"+format_name);
		                                        	WriteLog(""+"format_type"+format_type);
		                                        	
		                                        	if(format_name.equalsIgnoreCase("date")){
		                                        		DateFormat df = new SimpleDateFormat("dd/MM/yyyy"); 
			                                            DateFormat df_new = new SimpleDateFormat(format_type);
			                                           
			                                            try {
			                                                startDate = df.parse(form_control_val);
			                                                form_control_val = df_new.format(startDate);
			                                                WriteLog("RLOSCommon#Create Input"+" date conversion: final Output: "+form_control_val+ " requested format: "+format_type);
			                                             
			                                            } catch (ParseException e) {
			                                            	WriteLog("RLOSCommon#Create Input"+" Error while format conversion: "+e.getMessage());
			                                                e.printStackTrace();
			                                            }
			                                            catch (Exception e) {
			                                            	WriteLog("RLOSCommon#Create Input"+" Error while format conversion: "+e.getMessage());
			                                                e.printStackTrace();
			                                            }
		                                        	}
		                                        	//change here for other input format
		                                        	
		                                        }
	                                            
	                                            WriteLog("inside else of parent tag form_control_val"+ form_control_val);
	                                            xml_str = xml_str + "<"+tag_name+">"+form_control_val
	                                                    +"</"+ tag_name+">";
	                                            WriteLog("inside else of parent tag xml_str xml_str"+ xml_str);
	                                        }
                                        else if (tag_name.equalsIgnoreCase("CIFIdValue"))
										{
											form_control_val = cifId;
											WriteLog("inside else of parent tag form_control_val"+ form_control_val);
                                            xml_str = xml_str + "<"+tag_name+">"+form_control_val
                                                    +"</"+ tag_name+">";
                                            WriteLog("inside else of parent tag xml_str xml_str"+ xml_str);
										}
                                        else if(default_val==null || default_val.equalsIgnoreCase("")){
                                        	  WriteLog("#RLOS Common GenerateXML IF part no value found for tag name: "+ tag_name);
                                        }
                                        else{
                                            WriteLog("#RLOS Common GenerateXML inside set default value");
                                            form_control_val = default_val;
                                                WriteLog("#RLOS Common GenerateXML inside set default value form_control_val"+ form_control_val);
                                                xml_str = xml_str + "<"+tag_name+">"+form_control_val
                                                        +"</"+ tag_name+">";
                                                WriteLog("#RLOS Common GenerateXML inside else of parent tag form_control_val xml_str1"+ xml_str);
                                            
                                        }
                                        int_xml.put(parent_tag, xml_str);
                                        WriteLog("else of generatexml RLOSCommon inside else"+xml_str);
										
										//code change for to remove docdect incase ref no is not present start	                                       
	                                        if(tag_name.equalsIgnoreCase("DocRefNum") && parent_tag.equalsIgnoreCase("DocDetails") && form_control_val.equalsIgnoreCase("")){
	                                        	if(xml_str.contains("</DocDetails>")){
	                                        		xml_str = xml_str.substring(0, xml_str.lastIndexOf("</DocDetails>"));
	                                        		int_xml.put(parent_tag, xml_str);
	                                        	}
	                                        	else
	                                        		int_xml.remove(parent_tag);
	                                        }
											else if(tag_name.equalsIgnoreCase("DocNum") && parent_tag.equalsIgnoreCase("LegalDocInfo") && form_control_val.equalsIgnoreCase("")){
	                                        	if(xml_str.contains("</LegalDocInfo>")){
	                                        		xml_str = xml_str.substring(0, xml_str.lastIndexOf("</LegalDocInfo>"));
	                                        		int_xml.put(parent_tag, xml_str);
	                                        	}
	                                        	else
	                                        		int_xml.remove(parent_tag);
	                                        }
											else if(tag_name.equalsIgnoreCase("CIFIdValue") && parent_tag.equalsIgnoreCase("CIFId") && form_control_val.equalsIgnoreCase("")){
		                                        	if(xml_str.contains("</CIFId>")){
		                                        		xml_str = xml_str.substring(0, xml_str.lastIndexOf("</CIFId>"));
		                                        		int_xml.put(parent_tag, xml_str);
		                                        	}
		                                        	else
		                                        		int_xml.remove(parent_tag);
		                                        	String comp_xml = int_xml.get("CustomerExposureRequest");
		                                        	comp_xml = comp_xml.replace("<CIFId>", "");
		                                        	comp_xml = comp_xml.replace("</CIFId>", "");
		                                        	int_xml.put("CustomerExposureRequest",comp_xml);
		                                        }
		                                    else{
		                                    	int_xml.put(parent_tag, xml_str);
		                                    }
	                                        //code change for to remove docdect incase ref no is not present end
                                    }
                                    
                                }
                                else{
                                    String new_xml_str ="";
                                    WriteLog("inside else of parent tag main 2 value after adding "+ Call_name+": "+new_xml_str+" form_control name: "+form_control);
									WriteLog("valuie of form control1234: "+form_control);
									if(!(form_control==null || form_control.equalsIgnoreCase("")||  form_control.equalsIgnoreCase("null")))
									//if(!(jsonObj.getString(form_control)==null || jsonObj.getString(form_control).equalsIgnoreCase("")||  jsonObj.getString(form_control).equalsIgnoreCase("null")))
									{
                                        WriteLog("inside else of parent tag 1 form_control_val"+ form_control_val);
										if(fin_call_name.toUpperCase().contains(request_Arr[i].toUpperCase())){
											form_control_val = jsonObj.getString(form_control);
										}
										else{
											form_control_val = jsonObj.getString(form_control);
										}
                                        
                                        WriteLog("inside else of parent tag form_control_val"+ form_control_val);
										if(!data_format12.equalsIgnoreCase("text")){
			                                        	String[] format_arr = data_format12.split(":");
			                                        	String format_name = format_arr[0];
			                                        	String format_type = format_arr[1];
			                                        	
														WriteLog("format_name"+format_name);
			                                        	
														WriteLog("format_type"+format_type);
			                                        	
			                                        	if(format_name.equalsIgnoreCase("date")){
			                                        		DateFormat df = new SimpleDateFormat("dd/MM/yyyy"); 
				                                            DateFormat df_new = new SimpleDateFormat(format_type);
				                                           
				                                            try {
				                                                startDate = df.parse(form_control_val);
				                                                form_control_val = df_new.format(startDate);
				                                                
				                                             
				                                            } catch (ParseException e) {
				                                            	 
				                                                e.printStackTrace();
				                                            }
				                                            catch (Exception e) {
				                                            	 
				                                                e.printStackTrace();
				                                            }
			                                        	}
			                                        	//change here for other input format
			                                        	
			                                        }
                                        new_xml_str = new_xml_str + "<"+tag_name+">"+form_control_val
                                                +"</"+ tag_name+">";
                                        WriteLog("inside else of parent tag xml_str new_xml_str"+ new_xml_str);
                                    }
                                    
                                    else if(default_val==null || default_val.equalsIgnoreCase("")){
											if(int_xml.containsKey(parent_tag)||is_repetitive.equalsIgnoreCase("Y")){
												new_xml_str = new_xml_str + "<"+tag_name+">"+"</"+ tag_name+">";
											}
                                        WriteLog("#RLOS Common GenerateXML Inside Else Part no value found for tag name: "+ tag_name);
                                    }
                                    else{
                                        WriteLog("#RLOS Common GenerateXML inside set default value");
                                        form_control_val = default_val;
                                            WriteLog("#RLOS Common GenerateXML inside set default value form_control_val"+ form_control_val);
                                            new_xml_str = new_xml_str + "<"+tag_name+">"+form_control_val
                                                    +"</"+ tag_name+">";
                                            WriteLog("#RLOS Common GenerateXML inside else of parent tag form_control_val xml_str1 xml_str"+ new_xml_str);
                                        
                                    }
                                    //String new_xml_str = "<"+tag_name+">"+jsonObj.getString(form_control)
                                        //                +"</"+ tag_name+">";
                                    int_xml.put(parent_tag, new_xml_str);
                                    WriteLog("else of generatexml RLOSCommon inside else"+new_xml_str);
                                    
                                }
                            }
							
							
						}
						
						final_xml=final_xml.append("<").append(parentTagName).append(">");
						WriteLog("RLOS:"+final_xml);
	        			
	        			Iterator<Map.Entry<String,String>> itr = int_xml.entrySet().iterator();
	        			while (itr.hasNext())
	        			{
	        				Map.Entry<String, String> entry =  itr.next();
	        				if(final_xml.indexOf((entry.getKey()))>-1){
	        					WriteLog("RLOS itr_value: Key: "+ entry.getKey()+" Value: "+entry.getValue());
	        					final_xml = final_xml.insert(final_xml.indexOf("<"+entry.getKey()+">")+entry.getKey().length()+2, entry.getValue());
	        					itr.remove();
	        				}

	        			}

						
						final_xml=final_xml.append("</").append(parentTagName).append(">");
	        				WriteLog("FInal XMLnew before clean method is: "+final_xml.toString());
							if ("DECTECH".equalsIgnoreCase(Call_name)){
		        					final_xml = new StringBuilder(Clean_Xml(final_xml.toString(),Call_name));
		        				}
							WriteLog("FInal XMLnew after clean method is: "+final_xml.toString());
	        				final_xml.insert(0, header);
	        				final_xml.append(footer);
	        				WriteLog("FInal XMLnew with header: "+final_xml.toString());
	        	 }
				 
				 //commented because Socket IP and port maintained in property file
				/* 
				//code added here
				String sMQuery = "SELECT SocketServerIP,SocketServerPort FROM NG_RLOS_MQ_TABLE with (nolock)";
	    		WriteLog("sMQuery "+sMQuery);
				WriteLog("sessionId"+sessionId);
				inputXML = "<?xml version='1.0'?><APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option><Query>" + sMQuery + "</Query><EngineName>" + cabinetName + "</EngineName><SessionId>" + sessionId+ "</SessionId></APSelectWithColumnNames_Input>";
				//outputXML = WFCallBroker.execute(inputXML, wrapperIP, Integer.parseInt(wDSession.getM_objCabinetInfo().getM_strServerPort()), appServerType);
				
				try 
				{
					outputXML = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, inputXML);
				} 
				catch (NGException e) 
				{
				   e.printStackTrace();
				} 
				catch (Exception ex) 
				{
				   ex.printStackTrace();
				}
				WriteLog("outputXML exceptions mq --> "+outputXML);
				xmlParserData = new XMLParser();
				xmlParserData.setInputXML((outputXML));
				int recordcount2 = Integer.parseInt(xmlParserData.getValueOf("TotalRetrieved"));
				WriteLog("Number of records are in 3rd query are ="+recordcount2);
				mainCodeData = xmlParserData.getValueOf("MainCode");
				if(mainCodeData.equals("0"))
				{
				socketServerIP = xmlParserData.getNextValueOf("SocketServerIP");
				WriteLog("socketServerIP"+socketServerIP);
	            socketServerPort = Integer.parseInt(xmlParserData.getNextValueOf("SocketServerPort"));
				WriteLog("socketServerPort"+socketServerPort.toString());
				*/
				if(!(socketServerIP.equalsIgnoreCase("") && socketServerIP.equalsIgnoreCase(null) && socketServerPort.equals(null) && socketServerPort.equals(0)))
	           	   		 	{
								/*
	           	   		 			mqInputRequest= getMQInputXML(sessionId,cabinetName,wi_name,ws_name,userName,final_xml);
									socket = new Socket(socketServerIP, socketServerPort);
									outstream = socket.getOutputStream();
									socketInputStream = socket.getInputStream();
									dout = new DataOutputStream(outstream);
									din = new DataInputStream(socketInputStream);
									WriteLog("mqInputRequest is:"+mqInputRequest);
		            	         	if (mqInputRequest != null && mqInputRequest.length() > 0) 
		            				{
		            	         		int outPut_len = mqInputRequest.getBytes("UTF-16LE").length;
										mqInputRequest = outPut_len+"##8##;"+mqInputRequest;
										dout.write(mqInputRequest.getBytes("UTF-16LE"));
										dout.flush();
		            				}
									
									socket.setSoTimeout(60*1000);
								byte[] readBuffer = new byte[50000];
	                			int num = din.read(readBuffer);
	                			boolean wait_flag = true;
	                			int out_len=0;
                                
	                			if (num > 0) 
	                			{
	                				while(wait_flag){
	                					byte[] arrayBytes = new byte[num];
	                					System.arraycopy(readBuffer, 0, arrayBytes, 0, num);
	                					mqOutputResponse = mqOutputResponse + new String(arrayBytes, "UTF-16LE");
	                					if(mqOutputResponse.contains("##8##;")){
	                					String[]	mqOutputResponse_arr = mqOutputResponse.split("##8##;");
	                					mqOutputResponse = mqOutputResponse_arr[1];
										out_len = Integer.parseInt(mqOutputResponse_arr[0]);
										}
	                					if(out_len <= mqOutputResponse.getBytes("UTF-16LE").length){
	                						wait_flag=false;
	                					}
	                					Thread.sleep(200);
	                					 num = din.read(readBuffer);
	                					 
	                				}
	                					outputResponse= mqOutputResponse;	
		            				}
									*/
								outputResponse= dummyResponse(request_Arr[i]);
	           	   		 	}
								else{
	           	   		 		WriteLog("SocketServerIp and SocketServerPort is not maintained");
	           	   		 		WriteLog("SocketServerIp is not maintained "+socketServerIP);
	           	   		 		WriteLog(" SocketServerPort is not maintained "+socketServerPort.toString());
	           	   		 		outputResponse= "MQ details not maintained";
	           	   		 		}
								
							//} commented because Socket IP and port maintained in property file
				
				//ended here
				
				 
	    /*
	        else{
	        	WriteLog("Genrate XML:,, ");
	        	outputResponse="Call not maintained";
	        }
			*/
				}
			}
		}
		/*catch(SocketTimeoutException e){
			outputResponse = "Error";
		}		
		catch (UnknownHostException e) 
		{		
			e.printStackTrace();
			outputResponse= "1";
		}*/
		catch(Exception e){
			WriteLog("Exception ocurred: "+e.getMessage());
			outputResponse = "";
			//outputResponse= valueSetCustomer(outputResponse);
		}
		finally	
		{		
		//working fine till here
		WriteLog("outputResponse123"+outputResponse+ "row_count" +row_count+ "CardNumber" +CardNumber);
		out.clear();
		out.flush();
		out.print(outputResponse+"!@#"+row_count+"!@#"+CardNumber);
		if(outputResponse.equalsIgnoreCase("")){
		WriteLog("outputResponse1111"+outputResponse);
			//valueSetCustomer(outputResponse,Call_name);
		}
		try{
		if(socket!=null)
		{
		socket.close();
		socket=null;
		}
		if(outstream!=null)
		{
		outstream.close();
		outstream=null;
		}
		if(socketInputStream!=null)
		{
		socketInputStream.close();
		socketInputStream=null;
		}
		if(dout!=null)
		{
		dout.close();
		dout=null;
		}
		if(din!=null)
		{
		din.close();
		din=null;
		}
		}
		catch (Exception e)
		{
		}
		}
		
		
%>

<%!

 
	private static String getMQInputXML(String sessionID, String cabinetName, String wi_name, String ws_name, String userName, StringBuilder final_xml) 
	    {
			
			WriteLog("sessionId method getMQInputXML"+sessionID);
			WriteLog("cabinetName method getMQInputXML"+cabinetName);
			
			StringBuffer strBuff=new StringBuffer();
			strBuff.append("<APMQPUTGET_Input>");
			strBuff.append("<SessionId>"+sessionID+"</SessionId>");
			strBuff.append("<EngineName>"+cabinetName+"</EngineName>");
			strBuff.append("<XMLHISTORY_TABLENAME>NG_RLOS_XMLLOG_HISTORY</XMLHISTORY_TABLENAME>");
			strBuff.append("<WI_NAME>"+wi_name+"</WI_NAME>");
			strBuff.append("<WS_NAME>"+ws_name+"</WS_NAME>");
			strBuff.append("<USER_NAME>"+userName+"</USER_NAME>");
			strBuff.append("<MQ_REQUEST_XML>");
			strBuff.append(final_xml);		
			strBuff.append("</MQ_REQUEST_XML>");
			strBuff.append("</APMQPUTGET_Input>");		
	    	return strBuff.toString();
		}
	
		 public void valueSetCustomer(String outputResponse, String Call_name)
	    {
	        WriteLog("RLOSCommon valueSetCustomer Inside valueSetCustomer():"+outputResponse);
	        String outputXMLHead = "";
	        String outputXMLMsg = "";
	        String returnDesc = "";
	        String returnCode = "";
	        String response= "";
	        XMLParser objXMLParser = new XMLParser();
	        try
	        {
	            if(outputResponse.indexOf("<EE_EAI_HEADER>")>-1)
	            {
	                outputXMLHead=outputResponse.substring(outputResponse.indexOf("<EE_EAI_HEADER>"),outputResponse.indexOf("</EE_EAI_HEADER>")+16);
	                WriteLog("RLOSCommon valueSetCustomer outputXMLHead");
	            }
	            objXMLParser.setInputXML(outputXMLHead);
	            if(outputXMLHead.indexOf("<MsgFormat>")>-1)
	            {
	                response= outputXMLHead.substring(outputXMLHead.indexOf("<MsgFormat>")+11,outputXMLHead.indexOf("</MsgFormat>"));
	                WriteLog("$$response "+response);
	            }
	            if(outputXMLHead.indexOf("<ReturnDesc>")>-1)
	            {
	                returnDesc= outputXMLHead.substring(outputXMLHead.indexOf("<ReturnDesc>")+12,outputXMLHead.indexOf("</ReturnDesc>"));					
	                WriteLog("$$returnDesc "+returnDesc);
					if(Call_name.equalsIgnoreCase("CUSTOMER_EXPOSURE"))
					{
						//formObject.setNGValue("aecb_call_status",returnDesc);
					}
	            }
	            if(outputXMLHead.indexOf("<ReturnCode>")>-1)
	            {
	                returnCode= outputXMLHead.substring(outputXMLHead.indexOf("<ReturnCode>")+12,outputXMLHead.indexOf("</ReturnCode>"));
	                WriteLog("$$returnCode "+returnCode);
	            }
	            
	            if(outputResponse.indexOf("<MQ_RESPONSE_XML>")>-1)
	            {
	                outputXMLMsg=outputResponse.substring(outputResponse.indexOf("<MQ_RESPONSE_XML>")+17,outputResponse.indexOf("</MQ_RESPONSE_XML>"));
	                WriteLog("$$outputXMLMsg "+outputXMLMsg);
	                WriteLog("$$outputXMLMsg getOutputXMLValues check inside getOutputXMLValues");
					//getOutputXMLValues(outputXMLMsg,response,sessionID,cabinetName);
	                WriteLog("$$outputXMLMsg outputXMLMsg");
	            }
	           
	        }
	        catch(Exception e)
	        {            
	            WriteLog("Exception occured in valueSetCustomer method:  "+e.getMessage());
	            System.out.println("Exception occured in valueSetCustomer method: "+ e.getMessage());
	        }
	    }
		 public static  String Clean_Xml(String InputXml,String Call_name){
					String Output_Xml="";
					try{
						Document doc = getDocument(InputXml);
						removeEmptyNodes(doc, Call_name);
						DOMSource domSource = new DOMSource(doc);
						StringWriter writer = new StringWriter();
						StreamResult result = new StreamResult(writer);
						TransformerFactory tf = TransformerFactory.newInstance();
						Transformer transformer = tf.newTransformer();
						transformer.transform(domSource, result);
						Output_Xml = writer.toString().substring(38);
					}
					catch(Exception e){
						System.out.println("Exception occured in valueSetCustomer method: "+ e.getMessage());
					}
					return Output_Xml;
				}
				public static  void removeEmptyNodes(Node node,String Call_name) {
					NodeList list = node.getChildNodes();
					List<Node> nodesToRecursivelyCall = new LinkedList<Node>();
					for (int i = 0; i < list.getLength(); i++) {
						nodesToRecursivelyCall.add(list.item(i));
					}
					for(Node tempNode : nodesToRecursivelyCall) {
						removeEmptyNodes(tempNode, Call_name);
					}
					boolean emptyElement = node.getNodeType() == Node.ELEMENT_NODE && node.getChildNodes().getLength() == 0;
					boolean emptyText = node.getNodeType() == Node.TEXT_NODE && node.getNodeValue().trim().isEmpty();
					boolean selectText = node.getNodeType() == Node.TEXT_NODE && (node.getNodeValue().trim().equalsIgnoreCase("--Select--")||node.getNodeValue().trim().equalsIgnoreCase("null")||node.getNodeValue().trim().equalsIgnoreCase("Select"));
					//changes done to remove empty tags in Dectech only.
					if ("DECTECH".equalsIgnoreCase(Call_name)){
						if(emptyElement || emptyText || selectText) {
							if(!node.hasAttributes()) {
								node.getParentNode().removeChild(node);
							}
						}
					}
					else if (emptyElement || selectText) {
						if(!node.hasAttributes()) {
							node.getParentNode().removeChild(node);
						}
					}
					//  return node;
				}
				public static Document getDocument(String xml) 
				{
					Document doc=null;
					try
					{
						// Step 1: create a DocumentBuilderFactory
						DocumentBuilderFactory dbf =
							DocumentBuilderFactory.newInstance();

						// Step 2: create a DocumentBuilder
						DocumentBuilder db = dbf.newDocumentBuilder();

						// Step 3: parse the input file to get a Document object
						doc = db.parse(new InputSource(new StringReader(xml)));		


					}		
					catch(Exception ex)
					{
						WriteLog("Exception occured in valueSetCustomer method:  "+ex.getMessage());
					}
					finally {
						WriteLog("Inside final block");
					}
					return doc;
				}
		public static String dummyResponse(String callName)
		{
			WriteLog("Inside dummyresponse function for callname: "+callName);
            String response="";
           
                   
             if(callName.equalsIgnoreCase("INTERNALEXPOSURE"))
            {
                  response = "<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>CUSTOMER_EXPOSURE</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153379503251654</MessageId><Extra1>REP||SHELL.JOHN</Extra1><Extra2>2018-08-09T10:10:35.712+04:00</Extra2></EE_EAI_HEADER><CustomerExposureResponse><RequestType>InternalExposure</RequestType><IsDirect>Y</IsDirect><CustInfo><CustId><CustIdType>CIF Id</CustIdType><CustIdValue>0536177</CustIdValue></CustId><FullNm></FullNm><BirthDt>1988-01-01</BirthDt><Nationality>INDIAN</Nationality><CustSegment>PERSONAL BANKING</CustSegment><CustSubSegment>PB - NORMAL</CustSubSegment><RMName>PERSONAL BANKER</RMName><CreditGrade>R6 - BUSINESS - SCORE 20-34</CreditGrade><ECRN>010790200</ECRN><BorrowingCustomer>N</BorrowingCustomer></CustInfo><ProductExposureDetails><CardDetails><CardEmbossNum>010790200</CardEmbossNum><CardStatus>STWO</CardStatus><CardType>VCLASSIC-EXPATR</CardType><CustRoleType>Primary</CustRoleType><KeyDt><KeyDtType>ApplicationCreationDate</KeyDtType><KeyDtValue>2012-06-09</KeyDtValue></KeyDt><KeyDt><KeyDtType>ExpiryDate</KeyDtType><KeyDtValue>2025-01-31</KeyDtValue></KeyDt><CurCode></CurCode><AmountDtls><AmtType>CreditLimit</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>OutstandingAmt</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>OverdueAmt</AmtType><Amt>0</Amt></AmountDtls></CardDetails><CardDetails><CardEmbossNum>010790300</CardEmbossNum><CardStatus>STWO</CardStatus><CardType>MSTANDARD-EXPATRIATE</CardType><CustRoleType>Primary</CustRoleType><KeyDt><KeyDtType>ApplicationCreationDate</KeyDtType><KeyDtValue>2012-06-09</KeyDtValue></KeyDt><KeyDt><KeyDtType>ExpiryDate</KeyDtType><KeyDtValue>2025-01-31</KeyDtValue></KeyDt><CurCode></CurCode><AmountDtls><AmtType>CreditLimit</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>OutstandingAmt</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>OverdueAmt</AmtType><Amt>0</Amt></AmountDtls></CardDetails><CardDetails><CardEmbossNum>075161000</CardEmbossNum><CardStatus>AUWO</CardStatus><CardType>SME-TITANIUM</CardType><CustRoleType>Primary</CustRoleType><KeyDt><KeyDtType>ApplicationCreationDate</KeyDtType><KeyDtValue>2015-05-29</KeyDtValue></KeyDt><KeyDt><KeyDtType>ExpiryDate</KeyDtType><KeyDtValue>2025-01-31</KeyDtValue></KeyDt><CurCode></CurCode><AmountDtls><AmtType>CreditLimit</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>OutstandingAmt</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>OverdueAmt</AmtType><Amt>-40184.49</Amt></AmountDtls></CardDetails><AcctDetails><AcctId>8032044263901</AcctId><IBANNumber>AE480400008032044263901</IBANNumber><AcctStat>ACTIVE</AcctStat><AcctCur>AED</AcctCur><AcctNm>FIRST NAME FOR 0536177  LAST NAME FOR 0536177</AcctNm><AcctType>AMAL BUSINESS FINANCE ACCOUNT</AcctType><AcctSegment>PBD</AcctSegment><AcctSubSegment>PSL</AcctSubSegment><CustRoleType>Auth Sign.</CustRoleType><KeyDt><KeyDtType>AccountOpenDate</KeyDtType><KeyDtValue>2013-02-25</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitSactionDate</KeyDtType><KeyDtValue>2013-02-25</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitExpiryDate</KeyDtType><KeyDtValue>2013-02-26</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitStartDate</KeyDtType><KeyDtValue>2013-02-25</KeyDtValue></KeyDt><AmountDtls><AmtType>AvailableBalance</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>ClearBalanceAmount</AmtType><Amt>-1824.32</Amt></AmountDtls><AmountDtls><AmtType>LedgerBalance</AmtType><Amt>-1824.32</Amt></AmountDtls><AmountDtls><AmtType>OverdueAmount</AmtType><Amt>-1824.32</Amt></AmountDtls><AmountDtls><AmtType>EffectiveAvailableBalance</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>CumulativeDebitAmount</AmtType><Amt>60443311.04</Amt></AmountDtls><AmountDtls><AmtType>ChrgOffBal</AmtType><Amt>1824.32</Amt></AmountDtls><AmountDtls><AmtType>SanctionLimit</AmtType><Amt>0</Amt></AmountDtls><WriteoffStat>N</WriteoffStat><WriteoffStatDt>2017-08-20</WriteoffStatDt><WorstDelay24Months>P6</WorstDelay24Months><WorstStatus24Months>DEFAULT</WorstStatus24Months><MonthsOnBook>66.00</MonthsOnBook><LastRepmtDt>AUG</LastRepmtDt><IsCurrent>N</IsCurrent><ChargeOffFlag>Y</ChargeOffFlag><DelinquencyInfo><BucketType>DaysPastDue</BucketType><BucketValue>29</BucketValue></DelinquencyInfo><LinkedCIFs><CIFId>2044263</CIFId><RelationType>Main Account Holder</RelationType></LinkedCIFs></AcctDetails><AcctDetails><AcctId>8032044263902</AcctId><IBANNumber>AE210400008032044263902</IBANNumber><AcctStat>ACTIVE</AcctStat><AcctCur>USD</AcctCur><AcctNm>FIRST NAME FOR 0536177  LAST NAME FOR 0536177</AcctNm><AcctType>AMAL BUSINESS FINANCE ACCOUNT</AcctType><AcctSegment>PBD</AcctSegment><AcctSubSegment>PSL</AcctSubSegment><CustRoleType>Auth Sign.</CustRoleType><KeyDt><KeyDtType>AccountOpenDate</KeyDtType><KeyDtValue>2013-02-25</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitSactionDate</KeyDtType><KeyDtValue>2013-02-25</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitExpiryDate</KeyDtType><KeyDtValue>2013-02-26</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitStartDate</KeyDtType><KeyDtValue>2013-02-25</KeyDtValue></KeyDt><AmountDtls><AmtType>AvailableBalance</AmtType><Amt>0.00</Amt></AmountDtls><AmountDtls><AmtType>ClearBalanceAmount</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>LedgerBalance</AmtType><Amt>0.00</Amt></AmountDtls><AmountDtls><AmtType>EffectiveAvailableBalance</AmtType><Amt>0.00</Amt></AmountDtls><AmountDtls><AmtType>CumulativeDebitAmount</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>SanctionLimit</AmtType><Amt>0</Amt></AmountDtls><WriteoffStat>Y</WriteoffStat><WorstDelay24Months>P2</WorstDelay24Months><MonthsOnBook>66.00</MonthsOnBook><LastRepmtDt>AUG</LastRepmtDt><IsCurrent>Y</IsCurrent><ChargeOffFlag>N</ChargeOffFlag><DelinquencyInfo><BucketType>DaysPastDue</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><LinkedCIFs><CIFId>2044263</CIFId><RelationType>Main Account Holder</RelationType></LinkedCIFs></AcctDetails><AcctDetails><AcctId>0030536177001</AcctId><IBANNumber>AE790400000030536177001</IBANNumber><AcctStat>ACTIVE</AcctStat><AcctCur>AED</AcctCur><AcctNm>FIRST NAME FOR 0536177  LAST NAME FOR 0536177</AcctNm><AcctType>CURRENT ACCOUNT</AcctType><AcctSegment>PBD</AcctSegment><AcctSubSegment>PBN</AcctSubSegment><CustRoleType>Main</CustRoleType><KeyDt><KeyDtType>AccountOpenDate</KeyDtType><KeyDtValue>2013-03-06</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitSactionDate</KeyDtType><KeyDtValue>2013-03-06</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitExpiryDate</KeyDtType><KeyDtValue>2013-03-07</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitStartDate</KeyDtType><KeyDtValue>2013-03-06</KeyDtValue></KeyDt><AmountDtls><AmtType>AvailableBalance</AmtType><Amt>0.00</Amt></AmountDtls><AmountDtls><AmtType>ClearBalanceAmount</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>LedgerBalance</AmtType><Amt>0.00</Amt></AmountDtls><AmountDtls><AmtType>EffectiveAvailableBalance</AmtType><Amt>0.00</Amt></AmountDtls><AmountDtls><AmtType>CumulativeDebitAmount</AmtType><Amt>10000</Amt></AmountDtls><AmountDtls><AmtType>SanctionLimit</AmtType><Amt>0</Amt></AmountDtls><WriteoffStat>Y</WriteoffStat><WorstDelay24Months>P2</WorstDelay24Months><MonthsOnBook>66.00</MonthsOnBook><LastRepmtDt>AUG</LastRepmtDt><IsCurrent>Y</IsCurrent><ChargeOffFlag>N</ChargeOffFlag><DelinquencyInfo><BucketType>DaysPastDue</BucketType><BucketValue>0</BucketValue></DelinquencyInfo></AcctDetails><AcctDetails><AcctId>0030536177998</AcctId><IBANNumber>AE290400000030536177998</IBANNumber><AcctStat>ACTIVE</AcctStat><AcctCur>AED</AcctCur><AcctNm>FIRST NAME FOR 0536177  LAST NAME FOR 0536177</AcctNm><AcctType>CHARGE RECEIVABLE</AcctType><AcctSegment>PBD</AcctSegment><AcctSubSegment>PBN</AcctSubSegment><CustRoleType>Main</CustRoleType><KeyDt><KeyDtType>AccountOpenDate</KeyDtType><KeyDtValue>2017-02-01</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitSactionDate</KeyDtType><KeyDtValue>2017-02-01</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitExpiryDate</KeyDtType><KeyDtValue>2099-01-01</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitStartDate</KeyDtType><KeyDtValue>2017-02-01</KeyDtValue></KeyDt><AmountDtls><AmtType>AvailableBalance</AmtType><Amt>99999999999599.00</Amt></AmountDtls><AmountDtls><AmtType>ClearBalanceAmount</AmtType><Amt>-400</Amt></AmountDtls><AmountDtls><AmtType>LedgerBalance</AmtType><Amt>-400.00</Amt></AmountDtls><AmountDtls><AmtType>EffectiveAvailableBalance</AmtType><Amt>99999999999599.00</Amt></AmountDtls><AmountDtls><AmtType>CumulativeDebitAmount</AmtType><Amt>400</Amt></AmountDtls><AmountDtls><AmtType>SanctionLimit</AmtType><Amt>99999999999999</Amt></AmountDtls><WriteoffStat>Y</WriteoffStat><WorstDelay24Months>P2</WorstDelay24Months><MonthsOnBook>19.00</MonthsOnBook><LastRepmtDt>AUG</LastRepmtDt><IsCurrent>Y</IsCurrent><ChargeOffFlag>N</ChargeOffFlag><DelinquencyInfo><BucketType>DaysPastDue</BucketType><BucketValue>0</BucketValue></DelinquencyInfo></AcctDetails><AcctDetails><AcctId>0003536177099</AcctId><IBANNumber></IBANNumber><AcctStat>CLOSED</AcctStat><AcctCur>AED</AcctCur><AcctNm>FIRST NAME FOR 0536177  LAST NAME FOR 0536177</AcctNm><AcctType>CUSTOMER SECURITY CHEQUE ACCOUNT</AcctType><AcctSegment>PBD</AcctSegment><AcctSubSegment>PBN</AcctSubSegment><CustRoleType>Main</CustRoleType><KeyDt><KeyDtType>AccountOpenDate</KeyDtType><KeyDtValue>2004-11-28</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitSactionDate</KeyDtType><KeyDtValue>2004-11-28</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitExpiryDate</KeyDtType><KeyDtValue>2012-06-08</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitStartDate</KeyDtType><KeyDtValue>2004-11-28</KeyDtValue></KeyDt><AmountDtls><AmtType>AvailableBalance</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>ClearBalanceAmount</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>LedgerBalance</AmtType><Amt>0.00</Amt></AmountDtls><AmountDtls><AmtType>EffectiveAvailableBalance</AmtType><Amt>0.00</Amt></AmountDtls><AmountDtls><AmtType>CumulativeDebitAmount</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>SanctionLimit</AmtType><Amt>0</Amt></AmountDtls><WriteoffStat>Y</WriteoffStat><WorstDelay24Months>P2</WorstDelay24Months><MonthsOnBook>104.00</MonthsOnBook><LastRepmtDt>JUL</LastRepmtDt><IsCurrent>Y</IsCurrent><ChargeOffFlag>N</ChargeOffFlag><DelinquencyInfo><BucketType>DaysPastDue</BucketType><BucketValue>0</BucketValue></DelinquencyInfo></AcctDetails></ProductExposureDetails></CustomerExposureResponse><CustomerExposureResponse><RequestType>InternalExposure</RequestType><IsDirect>N</IsDirect><CustInfo><CustId><CustIdType>CIF Id</CustIdType><CustIdValue>2044263</CustIdValue></CustId><FullNm></FullNm><BirthDt>2003-11-12</BirthDt><Nationality>UNITED ARAB EMIRATES</Nationality><CustSegment>PERSONAL BANKING</CustSegment><CreditGrade>W1-WRITE OFF ACCOUNT</CreditGrade></CustInfo><ProductExposureDetails><LoanDetails><AgreementId>0032044263001</AgreementId><LoanStat>ACTIVE</LoanStat><LoanType>SHORT TERM LOAN AGAINST INVOICE</LoanType><LoanDesc></LoanDesc><CustRoleType></CustRoleType><KeyDt><KeyDtType>LimitSactionDate</KeyDtType><KeyDtValue>2011-02-13</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitExpiryDate</KeyDtType><KeyDtValue>2099-02-13</KeyDtValue></KeyDt><CurCode>AED</CurCode><MonthsOnBook>37.00</MonthsOnBook><DelinquencyInfo><BucketType>DaysPastDue</BucketType><BucketValue>0</BucketValue></DelinquencyInfo></LoanDetails><LoanDetails><AgreementId>0032044263002</AgreementId><LoanStat>ACTIVE</LoanStat><LoanType>SHORT TERM LOAN AGAINST INVOICE PAST DUE</LoanType><LoanDesc></LoanDesc><CustRoleType></CustRoleType><KeyDt><KeyDtType>LimitSactionDate</KeyDtType><KeyDtValue>2011-02-13</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitExpiryDate</KeyDtType><KeyDtValue>2099-02-13</KeyDtValue></KeyDt><CurCode>AED</CurCode><AmountDtls><AmtType>OverdueAmt</AmtType><Amt>-110395.72</Amt></AmountDtls><WriteoffStat>ChargeOff</WriteoffStat><WriteoffStatDt>2016-07-26</WriteoffStatDt><MonthsOnBook>33.00</MonthsOnBook><DelinquencyInfo><BucketType>DaysPastDue</BucketType><BucketValue>0</BucketValue></DelinquencyInfo></LoanDetails><AcctDetails><AcctId>8032044263998</AcctId><IBANNumber>AE480400008032044263998</IBANNumber><AcctStat>CLOSED</AcctStat><AcctCur>AED</AcctCur><AcctNm>FIRST NAME FOR 0536177  LAST NAME FOR 0536177</AcctNm><AcctType>CHARGE RECEIVABLE</AcctType><AcctSegment>PBD</AcctSegment><AcctSubSegment>PSL</AcctSubSegment><CustRoleType>Main</CustRoleType><KeyDt><KeyDtType>AccountOpenDate</KeyDtType><KeyDtValue>2015-12-17</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitSactionDate</KeyDtType><KeyDtValue>2015-12-17</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitExpiryDate</KeyDtType><KeyDtValue>2099-01-01</KeyDtValue></KeyDt><AmountDtls><AmtType>AvailableBalance</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>ClearBalanceAmount</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>LedgerBalance</AmtType><Amt>0.00</Amt></AmountDtls><AmountDtls><AmtType>EffectiveAvailableBalance</AmtType><Amt>0</Amt></AmountDtls><WriteoffStat>Y</WriteoffStat><WorstDelay24Months>P2</WorstDelay24Months><MonthsOnBook>21.00</MonthsOnBook><LastRepmtDt>AUG</LastRepmtDt><IsCurrent>Y</IsCurrent><ChargeOffFlag>N</ChargeOffFlag><DelinquencyInfo><BucketType>DaysPastDue</BucketType><BucketValue>0</BucketValue></DelinquencyInfo></AcctDetails><AcctDetails><AcctId>8032044263999</AcctId><IBANNumber>AE210400008032044263999</IBANNumber><AcctStat>ACTIVE</AcctStat><AcctCur>AED</AcctCur><AcctNm>FIRST NAME FOR 0536177  LAST NAME FOR 0536177</AcctNm><AcctType>CHARGE RECEIVABLE</AcctType><AcctSegment>PBD</AcctSegment><AcctSubSegment>PSL</AcctSubSegment><CustRoleType>Main</CustRoleType><KeyDt><KeyDtType>AccountOpenDate</KeyDtType><KeyDtValue>2017-09-03</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitSactionDate</KeyDtType><KeyDtValue>2017-09-03</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitExpiryDate</KeyDtType><KeyDtValue>2099-01-01</KeyDtValue></KeyDt><AmountDtls><AmtType>AvailableBalance</AmtType><Amt>99999999997226.50</Amt></AmountDtls><AmountDtls><AmtType>ClearBalanceAmount</AmtType><Amt>-2772.5</Amt></AmountDtls><AmountDtls><AmtType>LedgerBalance</AmtType><Amt>-2772.50</Amt></AmountDtls><AmountDtls><AmtType>EffectiveAvailableBalance</AmtType><Amt>99999999997226.50</Amt></AmountDtls><WriteoffStat>Y</WriteoffStat><WorstDelay24Months>P2</WorstDelay24Months><MonthsOnBook>12.00</MonthsOnBook><LastRepmtDt>AUG</LastRepmtDt><IsCurrent>Y</IsCurrent><ChargeOffFlag>N</ChargeOffFlag><DelinquencyInfo><BucketType>DaysPastDue</BucketType><BucketValue>0</BucketValue></DelinquencyInfo></AcctDetails></ProductExposureDetails></CustomerExposureResponse></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";                        
            }

            else if(callName.equalsIgnoreCase("EXTERNALEXPOSURE"))
            {
                  response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>CUSTOMER_EXPOSURE</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153379503253258</MessageId><Extra1>REP||SHELL.JOHN</Extra1><Extra2>2018-08-09T10:10:36.002+04:00</Extra2></EE_EAI_HEADER><CustomerExposureResponse><RequestType>ExternalExposure</RequestType><ReferenceNumber>167054</ReferenceNumber><ReportUrl>https://ant2a2aapps01.rakbanktst.ae:446/GetPdf.aspx?refno=Ysk2f%2fhPUcQ%3d</ReportUrl><IsDirect>N</IsDirect><CustInfo><FullNm>ACCENTURE PVT LTD</FullNm><Activity>0</Activity><TotalOutstanding>0</TotalOutstanding><TotalOverdue>0.00</TotalOverdue><NoOfContracts>0</NoOfContracts><CustInfoListDet><ReferenceNumber>167054</ReferenceNumber><InfoType>NameENInfo</InfoType><CustName>ACCENTURE PVT LTD</CustName><CustNameType>CompanyTradeName</CustNameType><ActualFlag>true</ActualFlag><ProviderNo>B01</ProviderNo><CreatedOn>2018-08-09</CreatedOn><DateOfUpdate>2018-08-09</DateOfUpdate></CustInfoListDet><CustInfoListDet><ReferenceNumber>167054</ReferenceNumber><InfoType>TradeLicenseHistorylst</InfoType><ActualFlag>true</ActualFlag><ProviderNo>B01</ProviderNo><RegistrationPlace>3</RegistrationPlace><LicenseNumber>123658585</LicenseNumber><CreatedOn>2018-08-09</CreatedOn><DateOfUpdate>2018-08-09</DateOfUpdate></CustInfoListDet><PhoneInfo><ReportedDate>2018-08-09</ReportedDate></PhoneInfo><InquiryInfo><ContractCategory>C</ContractCategory></InquiryInfo></CustInfo><ScoreInfo></ScoreInfo><RecordDestributions><RecordDestribution><ContractType>TotalSummary</ContractType><Contract_Role_Type></Contract_Role_Type><TotalNo>1</TotalNo><DataProvidersNo>1</DataProvidersNo><RequestNo>1</RequestNo><DeclinedNo>0</DeclinedNo><RejectedNo>0</RejectedNo><NotTakenUpNo>0</NotTakenUpNo><ActiveNo>0</ActiveNo><ClosedNo>0</ClosedNo></RecordDestribution><RecordDestribution><ContractType>Installments</ContractType><Contract_Role_Type>Holder</Contract_Role_Type><TotalNo>0</TotalNo><DataProvidersNo>0</DataProvidersNo><RequestNo>0</RequestNo><DeclinedNo>0</DeclinedNo><RejectedNo>0</RejectedNo><NotTakenUpNo>0</NotTakenUpNo><ActiveNo>0</ActiveNo><ClosedNo>0</ClosedNo></RecordDestribution><RecordDestribution><ContractType>NotInstallments</ContractType><Contract_Role_Type>Holder</Contract_Role_Type><TotalNo>0</TotalNo><DataProvidersNo>0</DataProvidersNo><RequestNo>0</RequestNo><DeclinedNo>0</DeclinedNo><RejectedNo>0</RejectedNo><NotTakenUpNo>0</NotTakenUpNo><ActiveNo>0</ActiveNo><ClosedNo>0</ClosedNo></RecordDestribution><RecordDestribution><ContractType>CreditCards</ContractType><Contract_Role_Type>Holder</Contract_Role_Type><TotalNo>1</TotalNo><DataProvidersNo>0</DataProvidersNo><RequestNo>1</RequestNo><DeclinedNo>0</DeclinedNo><RejectedNo>0</RejectedNo><NotTakenUpNo>0</NotTakenUpNo><ActiveNo>0</ActiveNo><ClosedNo>0</ClosedNo></RecordDestribution><RecordDestribution><ContractType>Services</ContractType><Contract_Role_Type>Holder</Contract_Role_Type><TotalNo>0</TotalNo><DataProvidersNo>0</DataProvidersNo><RequestNo>0</RequestNo><DeclinedNo>0</DeclinedNo><RejectedNo>0</RejectedNo><NotTakenUpNo>0</NotTakenUpNo><ActiveNo>0</ActiveNo><ClosedNo>0</ClosedNo></RecordDestribution></RecordDestributions><Derived><Total_Exposure>0</Total_Exposure><WorstCurrentPaymentDelay>0</WorstCurrentPaymentDelay><Worst_PaymentDelay_Last24Months>0</Worst_PaymentDelay_Last24Months><Nof_Records>0</Nof_Records><NoOf_Cheque_Return_Last3>0</NoOf_Cheque_Return_Last3><Nof_DDES_Return_Last3Months>0</Nof_DDES_Return_Last3Months><Nof_DDES_Return_Last6Months>0</Nof_DDES_Return_Last6Months><Nof_Cheque_Return_Last6>0</Nof_Cheque_Return_Last6><DPD30_Last6Months>0</DPD30_Last6Months><Nof_Enq_Last90Days>0</Nof_Enq_Last90Days><Nof_Enq_Last60Days>0</Nof_Enq_Last60Days><Nof_Enq_Last30Days>0</Nof_Enq_Last30Days><TotOverDue_GuarteContrct>0</TotOverDue_GuarteContrct></Derived><ProductExposureDetails><ChequeDetails><ChqType></ChqType><Number></Number><Amount></Amount><ReturnDate></ReturnDate><ProviderNo></ProviderNo><ReasonCode></ReasonCode><Severity></Severity></ChequeDetails></ProductExposureDetails></CustomerExposureResponse></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
            }
            else if(callName.equalsIgnoreCase("COLLECTIONSSUMMARY"))
            {
                  response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>CUSTOMER_EXPOSURE</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153379505177990</MessageId><Extra1>REP||SHELL.JOHN</Extra1><Extra2>2018-08-09T10:10:52.790+04:00</Extra2></EE_EAI_HEADER><CustomerExposureResponse><RequestType>CollectionsSummary</RequestType><CustInfo><CustId><CustIdType>Primary</CustIdType><CustIdValue>0536177</CustIdValue></CustId><FullNm></FullNm><TotalOutstanding>97554.74</TotalOutstanding><TotalOverdue>0</TotalOverdue><NoOfContracts>0</NoOfContracts></CustInfo><Derived><Nof_Records>1</Nof_Records></Derived><ProductExposureDetails><CardDetails><CardEmbossNum>010790300</CardEmbossNum><CardStatus>C</CardStatus><CardType>CREDIT CARDS</CardType><KeyDt><KeyDtType>Card_approve_date</KeyDtType><KeyDtValue>2004-11-30</KeyDtValue></KeyDt><KeyDt><KeyDtType>CardsApplcnRecvdDate</KeyDtType><KeyDtValue>2004-11-30</KeyDtValue></KeyDt><CurCode>AED</CurCode><AmountDtls><AmtType>Outstanding_balance</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>Credit_limit</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>Overdue_amount</AmtType><Amt>0</Amt></AmountDtls><MonthsOnBook>164</MonthsOnBook><SchemeCardProd>MSTANDARD-EXPATRIATE</SchemeCardProd><CurrentlyCurrentFlg>Y</CurrentlyCurrentFlg><GeneralStatus>STWO</GeneralStatus><DelinquencyInfo><BucketType>DPD_30_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>Bucket</BucketType><BucketValue>0</BucketValue></DelinquencyInfo></CardDetails><CardDetails><CardEmbossNum>075161000</CardEmbossNum><CardStatus>A</CardStatus><CardType>CREDIT CARDS</CardType><KeyDt><KeyDtType>Card_approve_date</KeyDtType><KeyDtValue>2015-05-28</KeyDtValue></KeyDt><KeyDt><KeyDtType>CardsApplcnRecvdDate</KeyDtType><KeyDtValue>2015-05-28</KeyDtValue></KeyDt><CurCode>AED</CurCode><AmountDtls><AmtType>Outstanding_balance</AmtType><Amt>97554.74</Amt></AmountDtls><AmountDtls><AmtType>Credit_limit</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>Overdue_amount</AmtType><Amt>0</Amt></AmountDtls><MonthsOnBook>38</MonthsOnBook><LastRepmtDt>09-2016</LastRepmtDt><SchemeCardProd>SME-TITANIUM</SchemeCardProd><CurrentlyCurrentFlg>Y</CurrentlyCurrentFlg><GeneralStatus>AUWO</GeneralStatus><DelinquencyInfo><BucketType>DPD_30_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>Bucket</BucketType><BucketValue>0</BucketValue></DelinquencyInfo></CardDetails><CardDetails><CardEmbossNum>010790200</CardEmbossNum><CardStatus>C</CardStatus><CardType>CREDIT CARDS</CardType><KeyDt><KeyDtType>Card_approve_date</KeyDtType><KeyDtValue>2004-11-30</KeyDtValue></KeyDt><KeyDt><KeyDtType>CardsApplcnRecvdDate</KeyDtType><KeyDtValue>2004-11-30</KeyDtValue></KeyDt><CurCode>AED</CurCode><AmountDtls><AmtType>Outstanding_balance</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>Credit_limit</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>Overdue_amount</AmtType><Amt>0</Amt></AmountDtls><MonthsOnBook>164</MonthsOnBook><SchemeCardProd>VCLASSIC-EXPATR</SchemeCardProd><CurrentlyCurrentFlg>Y</CurrentlyCurrentFlg><GeneralStatus>STWO</GeneralStatus><DelinquencyInfo><BucketType>DPD_30_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>Bucket</BucketType><BucketValue>0</BucketValue></DelinquencyInfo></CardDetails></ProductExposureDetails></CustomerExposureResponse></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
            }
            else if (callName.equalsIgnoreCase("CARD_INSTALLMENT_DETAILS"))
            {
                  response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>CARD_INSTALLMENT_DETAILS</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>1</ReturnCode><ReturnDesc>PRMSD : This account does not exist in database</ReturnDesc><MessageId>CAS153379502338921</MessageId><Extra1>REP||BPM.123</Extra1><Extra2>2018-08-09T10:10:24.512+04:00</Extra2></EE_EAI_HEADER></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
            }
            else if(callName.equalsIgnoreCase("SALDET"))
            {
                  response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>FINANCIAL_SUMMARY</MsgFormat><MsgVersion>0000</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>CINF212</ReturnCode><ReturnDesc>FIN : [CINF212]-NO SALARY RECORD FOUND</ReturnDesc><MessageId>CAS153382636971559</MessageId><Extra1>REP||LAXMANRET.LAXMANRET</Extra1><Extra2>2018-08-09T06:52:50.103+04:00</Extra2></EE_EAI_HEADER></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
            }
            else if(callName.equalsIgnoreCase("RETURNDET"))
            {
                  response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>FINANCIAL_SUMMARY</MsgFormat><MsgVersion>0000</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>CINF728</ReturnCode><ReturnDesc>FIN : [CINF728]-NO CHEQUE OR DDS RETURN FOUND</ReturnDesc><MessageId>CAS153379510967534</MessageId><Extra1>REP||LAXMANRET.LAXMANRET</Extra1><Extra2>2018-08-09T10:11:50.233+04:00</Extra2></EE_EAI_HEADER></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
            }
            else if(callName.equalsIgnoreCase("TRANSUM"))
            {
                  response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>FINANCIAL_SUMMARY</MsgFormat><MsgVersion>0000</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>CINF731</ReturnCode><ReturnDesc>FIN : [CINF731]-TRANSACTION SUMMARY NOT FOUND</ReturnDesc><MessageId>CAS153414285806563</MessageId><Extra1>REP||LAXMANRET.LAXMANRET</Extra1><Extra2>2018-08-13T10:47:38.472+04:00</Extra2></EE_EAI_HEADER></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
            }
            else if(callName.equalsIgnoreCase("AVGBALDET"))
            {
                  response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>FINANCIAL_SUMMARY</MsgFormat><MsgVersion>0000</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153354273860916</MessageId><Extra1>REP||LAXMANRET.LAXMANRET</Extra1><Extra2>2018-08-06T12:05:39.106+04:00</Extra2></EE_EAI_HEADER><FinancialSummaryRes><BankId>RAK</BankId><CIFID>0181162</CIFID><AcctId>0033181162002</AcctId><OperationType>AVGBALDET</OperationType><OperationDesc>AVERAGE BALANCE</OperationDesc><TxnSummary></TxnSummary><AvgBalanceDtls><Month>JUN</Month><AvgBalance>0</AvgBalance></AvgBalanceDtls><AvgBalanceDtls><Month>MAY</Month><AvgBalance>0</AvgBalance></AvgBalanceDtls><AvgBalanceDtls><Month>APR</Month><AvgBalance>0</AvgBalance></AvgBalanceDtls><AvgBalanceDtls><Month>MAR</Month><AvgBalance>0</AvgBalance></AvgBalanceDtls><AvgBalanceDtls><Month>FEB</Month><AvgBalance>0</AvgBalance></AvgBalanceDtls><AvgBalanceDtls><Month>JAN</Month><AvgBalance>0</AvgBalance></AvgBalanceDtls></FinancialSummaryRes></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
            }
            else if(callName.equalsIgnoreCase("LIENDET"))
            {
                  response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>FINANCIAL_SUMMARY</MsgFormat><MsgVersion>0000</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>CINF729</ReturnCode><ReturnDesc>FIN : [CINF729]-NO LIEN DETAILS FOUND</ReturnDesc><MessageId>CAS153405218506324</MessageId><Extra1>REP||LAXMANRET.LAXMANRET</Extra1><Extra2>2018-08-12T09:36:26.355+04:00</Extra2></EE_EAI_HEADER></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
            }

            return response;        
      }				
%>

