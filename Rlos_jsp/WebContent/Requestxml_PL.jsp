//Group						 : Application ?Projects
//Product / Project			 : RAKBank-RLOS
//File Name					 : 
//Author                     :Tanshu Aggarwal
//Date written (DD/MM/YYYY)  : 2/02/2017
//---------------------------------------------------------------------------------------------------->

<%@ include file="Log.process"%>
<jsp:useBean id="wDSession" class="com.newgen.wfdesktop.session.WDSession" scope="session"/>
<jsp:useBean id="WDCabinet" class="com.newgen.wfdesktop.baseclasses.WDCabinetInfo" scope="session"/>
<%@ page import="java.util.*"%>
<%@ page import="java.io.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.net.*"%>
<%@ page import="javax.xml.parsers.ParserConfigurationException" %>
<%@ page import="com.newgen.wfdesktop.exception.*" %>
<%@ page import="com.newgen.wfdesktop.xmlapi.*" %>
<%@ page import="com.newgen.wfdesktop.util.*" %>
<%@ page import="com.newgen.wfdesktop.util.xmlapi.*" %>
<%@ page import="com.newgen.custom.*" %>
<%@ page import="java.io.UnsupportedEncodingException" %>
<%@ page import="com.newgen.mvcbeans.model.wfobjects.WFDynamicConstant, com.newgen.mvcbeans.model.*,com.newgen.mvcbeans.controller.workdesk.*, javax.faces.context.FacesContext"%>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@page import="com.newgen.omni.wf.util.excp.NGException"%>
<%@page import="com.newgen.omni.wf.util.app.NGEjbClient"%>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page  import= "com.newgen.custom.*, org.w3c.dom.*, org.w3c.dom.NodeList,org.w3c.dom.Document,javax.xml.parsers.DocumentBuilder, javax.xml.parsers.DocumentBuilderFactory"%>
<%@ page import ="java.text.DecimalFormat,org.xml.sax.InputSource"%>
<%@ page import="org.json.JSONObject"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>


<%
    response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader ("Expires", 0);
	//All information will be available through wDSession object. Refer below sample code.
    WriteLog("in requestxml for PL jsp");
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

	String appServerType = wDSession.getM_objCabinetInfo().getM_strAppServerType();
	String wrapperIP = wDSession.getM_objCabinetInfo().getM_strServerIP();
	String wrapperPort = wDSession.getM_objCabinetInfo().getM_strServerPort()+"";    
	String sessionId = wDSession.getM_objUserInfo().getM_strSessionId();
	String cabinetName = wDSession.getM_objCabinetInfo().getM_strCabinetName();
	
	WriteLog("Resquest xml PL appServerType :" + appServerType);	
	WriteLog("Resquest xml PL wrapperIP :" + wrapperIP);	
	WriteLog("Resquest xml PL wrapperPort :" + wrapperPort);	
	WriteLog("Resquest xml PL cabinetName :" + cabinetName);	
	WriteLog("Resquest xml PL sessionId :" + sessionId);	
	
	
	
	
	JSONObject jsonObj=null;
	String form_control_val=null;
	String is_mobility = request.getParameter("is_mobility");
	String request_name = request.getParameter("request_name");	
	String wi_name = request.getParameter("wi_name");	
	String ws_name = request.getParameter("activityName");	
	String cifId = request.getParameter("cifId");
	String acc_id = request.getParameter("acc_id");
	String row_count = request.getParameter("row_count"); 
	String CardNumber = request.getParameter("CardNumber"); 
	String Customer_type = request.getParameter("Customer_Type");
	String request_Arr[]=request_name.split(",");
	String cardNo="";
	WriteLog("jsonobj1is_mobility --: "+is_mobility);	
	WriteLog("jsonobj2: request_name "+request_name);
	WriteLog("jsonobj2: ws_name "+ws_name);
	WriteLog("jsonobj2: wi_name "+wi_name);
	WriteLog("jsonobj2: cifId "+cifId);
	WriteLog("jsonobj2: Customer_type "+Customer_type);
	WriteLog("jsonobj2: CardNumber "+CardNumber);
	if(is_mobility.equalsIgnoreCase("Y")){
		WriteLog("INSIDE MOB");
		String jsonobj1=request.getParameter("param_json");
		 jsonObj= new JSONObject(jsonobj1);
		WriteLog("json result Result: " + jsonObj);	
		WriteLog("jsonobj1: "+jsonobj1);	
		Customer_type = jsonObj.getString("Customer_Type");
	}
	else{
	String jsonobj2 = request.getParameter("param_json");	
		jsonObj= new JSONObject(jsonobj2);
		
		if(request_name.equalsIgnoreCase("CARD_INSTALLMENT_DETAILS")){
			if(CardNumber!=null && !CardNumber.equalsIgnoreCase("")){
			jsonObj.put("CardNumber",CardNumber);
			WriteLog("jsonobj2: jsonObj123 CardNumber"+CardNumber);
			}
		}
		if(request_name.contains("CARD_SUMMARY"))
		{
			cardNo = request.getParameter("cardNo");	
		}
		else if(request_name.contains("ACCOUNT_SUMMARY")){
			String cif_id = request.getParameter("cifno");	
			jsonObj.put("cmplx_Customer_CIFNO",cif_id.trim());
		}
		
		
		
		
		//Changes done for company AECB start
		if(request_name.equalsIgnoreCase("ExternalExposure")){
			WriteLog("inside ExternalExposure condition");
			String comp_cif = jsonObj.getString("cif");
			String trade_comp_name= jsonObj.getString("trade_comp_name");
			String trade_lic_no = jsonObj.getString("trade_lic_no");
			String trade_lic_place = jsonObj.getString("trade_lic_place");
			if(comp_cif.contains(",")){
				WriteLog("Company cif contains multiple cif");
				String[] comp_cif_arr = comp_cif.split(",");
				String[] trade_comp_name_arr = trade_comp_name.split(",");
				String[] trade_lic_no_arr = trade_lic_no.split(",");
				String[] trade_lic_place_arr = trade_lic_place.split(",");
				int item_indx = 0 ;
				for(int x=0;x<comp_cif_arr.length;x++){
						if(comp_cif_arr[x].equalsIgnoreCase(cifId.trim()))
							item_indx=x;
					}
				jsonObj.put("trade_comp_name",trade_comp_name_arr[item_indx]);
				jsonObj.put("trade_lic_no",trade_lic_no_arr[item_indx]);
				jsonObj.put("trade_lic_place",trade_lic_place_arr[item_indx]);
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
	
	WriteLog("json result cardNo: " + cardNo);	
	//String request_Arr[]=request_name.split(",");
	WriteLog("jsonobj1is_mobility --: "+is_mobility);	
	WriteLog("jsonobj2: request_name "+request_name);
	WriteLog("jsonobj2: ws_name "+ws_name);
	WriteLog("jsonobj2: wi_name "+wi_name);
	
		jsonObj.put("card_no",cardNo.trim());	
				WriteLog("jsonobj2: jsonObj "+jsonObj);
		
	
	WriteLog("RLOSCommon");

		StringBuffer final_xml= new StringBuffer("");
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
	    String socketServerIP;
	    String socketPort;
	    Integer socketServerPort;
	   	XMLParser xmlParserData=null;
		String sQuery_header=null;
		
	   WriteLog("$$outputgGridtXML");
		try
		{
			
			for(int i =0;i<request_Arr.length;i++){
	
				if(request_Arr[i].equalsIgnoreCase("TRANSUM") || request_Arr[i].equalsIgnoreCase("AVGBALDET") || request_Arr[i].equalsIgnoreCase("RETURNDET")|| request_Arr[i].equalsIgnoreCase("LIENDET") || request_Arr[i].equalsIgnoreCase("SIDET")|| request_Arr[i].equalsIgnoreCase("SALDET")||request_Arr[i].equalsIgnoreCase("Primary_CIF")|| request_Arr[i].equalsIgnoreCase("Corporation_CIF"))
				{
					sQuery_header = "SELECT Header,Footer,parenttagname FROM NG_Integration where Call_name='FINANCIAL_SUMMARY'";
				}
				else if ((request_Arr[i].equalsIgnoreCase("OTP_SUMMARY"))&&(is_mobility.equalsIgnoreCase("Y"))){
					WriteLog("INSIDE OTP_Summary case ="+request_Arr[i]);
					sQuery_header = "SELECT Header,Footer,parenttagname FROM NG_Integration where Call_name='OTP_MANAGEMENT'";
				}
				else{
					sQuery_header = "SELECT Header,Footer,parenttagname FROM NG_Integration where Call_name='"+request_Arr[i]+"'";
				}
								
	        WriteLog("RLOSCommon--- "+sQuery_header);
			inputXML = "<?xml version='1.0'?><APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option><Query>" + sQuery_header + "</Query><EngineName>" + cabinetName + "</EngineName><SessionId>" + sessionId+ "</SessionId></APSelectWithColumnNames_Input>";
			
			WriteLog("inputXML -->"+inputXML);
			//OutputXML_header = WFCallBroker.execute(inputXML, wrapperIP, Integer.parseInt(wDSession.getM_objCabinetInfo().getM_strServerPort()), appServerType);
			
			try 
			{
				OutputXML_header = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, inputXML);
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
	
				if(mainCodeData.equals("0"))
	       	{
	        	header = xmlParserData.getNextValueOf("Header");
	            footer = xmlParserData.getNextValueOf("Footer");
	            parentTagName = xmlParserData.getNextValueOf("parenttagname");
				
	            WriteLog("header ="+header+" footer="+footer+" parentTagName"+parentTagName );
				if(is_mobility.equalsIgnoreCase("Y"))
				{ 
					WriteLog("INSIDE MOBILITY CALLING DB ="+request_Arr[i]);
					
					if(request_Arr[i].equalsIgnoreCase("INTERNALEXPOSURE") || request_Arr[i].equalsIgnoreCase("COLLECTIONSSUMMARY")||request_Arr[i].equalsIgnoreCase("CARD_INSTALLMENT_DETAILS"))
					{
						sQuery = "SELECT call_type, Call_name,Mobility_form_control,parent_tag_name,xmltag_name,is_repetitive,default_val,data_format FROM NG_Integration_field_Mapping where Call_name='"+ request_Arr[i] +"' and  Active = 'Y' ORDER BY tag_seq ASC" ;
					}
					
					else if(request_Arr[i].equalsIgnoreCase("EXTERNALEXPOSURE"))
					{
					//Changes done by aman for AECB	Start 
					sQuery = "SELECT call_type, Call_name,Mobility_form_control,parent_tag_name,xmltag_name,is_repetitive,default_val,data_format FROM NG_Integration_field_Mapping where Call_name='"+ request_Arr[i] +"' and Operation_Name='"+Customer_type+"' and Active = 'Y' ORDER BY tag_seq ASC" ;
					}
					//Changes done by aman for AECB	end 
					
					
					else if(request_Arr[i].equalsIgnoreCase("TRANSUM") || request_Arr[i].equalsIgnoreCase("AVGBALDET") || request_Arr[i].equalsIgnoreCase("RETURNDET")|| request_Arr[i].equalsIgnoreCase("LIENDET") || request_Arr[i].equalsIgnoreCase("SIDET")|| request_Arr[i].equalsIgnoreCase("SALDET")||request_Arr[i].equalsIgnoreCase("Primary_CIF")|| request_Arr[i].equalsIgnoreCase("Corporation_CIF"))
					{
						sQuery = "SELECT call_type, Call_name,Mobility_form_control,parent_tag_name,xmltag_name,is_repetitive,default_val,data_format FROM NG_Integration_field_Mapping where Call_name='FINANCIAL_SUMMARY' and Operation_name='"+jsonObj.getString("OperationType")+"' ORDER BY tag_seq ASC" ;
					}
					else if (request_Arr[i].equalsIgnoreCase("OTP_Management"))
					{
						WriteLog("INSIDE OTP_Management ="+request_Arr[i]);
						sQuery = "SELECT call_type, Call_name,Mobility_form_control,parent_tag_name,xmltag_name,is_repetitive,default_val,data_format FROM NG_Integration_field_Mapping where Call_name='OTP_MANAGEMENT' and  Operation_name='GenerateOTP' and  Active = 'Y' ORDER BY tag_seq ASC";
					}
					else if (request_Arr[i].equalsIgnoreCase("Customer_Details"))
					{
						WriteLog("INSIDE CustDE|T ="+request_Arr[i]);
						sQuery = "SELECT call_type, Call_name,Mobility_form_control,parent_tag_name,xmltag_name,is_repetitive,default_val,data_format FROM NG_Integration_field_Mapping where Call_name='Customer_Details' and  Operation_name='Primary_CIF' and  Active = 'Y' ORDER BY tag_seq ASC";
					}
					else if (request_Arr[i].equalsIgnoreCase("OTP_Summary"))
					{
						WriteLog("INSIDE OTP_Summary ="+request_Arr[i]);
						

						sQuery = "SELECT call_type, Call_name,Mobility_form_control,parent_tag_name,xmltag_name,is_repetitive,default_val,data_format FROM NG_Integration_field_Mapping where Call_name='OTP_MANAGEMENT' and  Operation_name='ValidateOTP' and  Active = 'Y' ORDER BY tag_seq ASC";
					}
					else
					{
						sQuery = "SELECT call_type, Call_name,Mobility_form_control,parent_tag_name,xmltag_name,is_repetitive,default_val,data_format FROM NG_Integration_field_Mapping where Call_name='"+request_Arr[i]+"' and  Active = 'Y' ORDER BY tag_seq ASC" ;

					}	
				}
				else
				{
					if( request_Arr[i].equalsIgnoreCase("INTERNALEXPOSURE") || request_Arr[i].equalsIgnoreCase("COLLECTIONSSUMMARY")||request_Arr[i].equalsIgnoreCase("CARD_INSTALLMENT_DETAILS"))
					{
						
						sQuery = "SELECT call_type, Call_name,form_control,parent_tag_name,xmltag_name,is_repetitive,default_val,data_format FROM NG_Integration_field_Mapping where Call_name='"+ request_Arr[i] +"' and Active = 'Y' ORDER BY tag_seq ASC" ;
					}
						 
						
					else if(request_Arr[i].equalsIgnoreCase("EXTERNALEXPOSURE"))
					{
					//Changes done by aman for AECB	Start 
					sQuery = "SELECT call_type, Call_name,form_control,parent_tag_name,xmltag_name,is_repetitive,default_val,data_format FROM NG_Integration_field_Mapping where Call_name='"+ request_Arr[i] +"' and Operation_Name='"+Customer_type+"' and Active = 'Y' ORDER BY tag_seq ASC" ;
					}
					//Changes done by aman for AECB	end 
					
					else if(request_Arr[i].equalsIgnoreCase("TRANSUM") || request_Arr[i].equalsIgnoreCase("AVGBALDET") || request_Arr[i].equalsIgnoreCase("RETURNDET")|| request_Arr[i].equalsIgnoreCase("LIENDET") || request_Arr[i].equalsIgnoreCase("SIDET")|| request_Arr[i].equalsIgnoreCase("SALDET")||request_Arr[i].equalsIgnoreCase("Primary_CIF")|| request_Arr[i].equalsIgnoreCase("Corporation_CIF"))
					{
						sQuery = "SELECT call_type, Call_name,form_control,parent_tag_name,xmltag_name,is_repetitive,default_val,data_format FROM NG_Integration_PL_field_Mapping where Call_name='FINANCIAL_SUMMARY' and Operation_name='"+ request_Arr[i] +"' ORDER BY tag_seq ASC" ;
					}
					else
					{
						sQuery = "SELECT call_type, Call_name,form_control,parent_tag_name,xmltag_name,is_repetitive,default_val,data_format FROM NG_Integration_PL_field_Mapping where Call_name='"+request_Arr[i]+"'ORDER BY tag_seq ASC" ;
					}								
				}
	    		
				WriteLog("sQuery123 "+sQuery);
				inputXML = "<?xml version='1.0'?><APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option><Query>" + sQuery + "</Query><EngineName>" + cabinetName + "</EngineName><SessionId>" + sessionId+ "</SessionId></APSelectWithColumnNames_Input>";
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
                                        System.out.println("value after adding "+ Call_name+": "+xml_str);
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
	                                            form_control_val = jsonObj.get(form_control)+"";
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
                                        else if (tag_name.equalsIgnoreCase("CIFID"))
										{
											form_control_val = jsonObj.getString("cmplx_Customer_CIFNO");
											WriteLog("inside else of parent tag form_control_val"+ form_control_val);
                                            xml_str = xml_str + "<"+tag_name+">"+form_control_val
                                                    +"</"+ tag_name+">";
                                            WriteLog("inside else of parent tag xml_str xml_str"+ xml_str);
										}
										else if (tag_name.equalsIgnoreCase("CardNumber"))
										{
											form_control_val = cardNo;
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
											if(form_control_val==null || form_control_val.equalsIgnoreCase("")){
													form_control_val = default_val;
											}
                                            
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
                                        form_control_val = jsonObj.getString(form_control);
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
											if(int_xml.containsKey(parent_tag)){
												new_xml_str = new_xml_str + "<"+tag_name+">"+"</"+ tag_name+">";
											}
                                        WriteLog("#RLOS Common GenerateXML Inside Else Part no value found for tag name: "+ tag_name);
                                    }
                                    else{
                                        WriteLog("#RLOS Common GenerateXML inside set default value");
										if(form_control_val==null || form_control_val.equalsIgnoreCase("")){
													form_control_val = default_val;
											}
										
                                        
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
	        				System.out.println("final_xml: "+final_xml);
	        				WriteLog("FInal XMLnew is: "+final_xml.toString());
	        				final_xml.insert(0, header);
	        				final_xml.append(footer);
	        				WriteLog("FInal XMLnew with header: "+final_xml.toString());
	        	 }
				
				//code added here
				String sMQuery = "SELECT SocketServerIP,SocketServerPort FROM NG_RLOS_MQ_TABLE";
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
				
				if(!(socketServerIP.equalsIgnoreCase("") && socketServerIP.equalsIgnoreCase(null) && socketServerPort.equals(null) && socketServerPort.equals(0)))
	           	   		 		{
	           	   		 			socket = new Socket(socketServerIP, socketServerPort);
									WriteLog("socket"+socket);
	           	   		 			outstream = socket.getOutputStream();
									WriteLog("outstream"+outstream);
	           	   		 			socketInputStream = socket.getInputStream();
									WriteLog("socketInputStream:"+socketInputStream);
	           	   		 			dout = new DataOutputStream(outstream);
									WriteLog("dout"+dout);
	           	   		 			din = new DataInputStream(socketInputStream);
									WriteLog("din"+din);
									//WriteLog("sessionId befor method"+Integer.parseInt(wDSession.getM_objCabinetInfo().getM_strServerPort()));
		           	   		 		mqInputRequest= getMQInputXML(sessionId,cabinetName,wi_name,ws_name,userName,final_xml);
									WriteLog("mqInputRequest is:"+mqInputRequest);
		            	         	if (mqInputRequest != null && mqInputRequest.length() > 0) 
		            				{
										int outPut_len = mqInputRequest.getBytes("UTF-16LE").length;
										WriteLog("Final XML output len: "+outPut_len+"");
										mqInputRequest = outPut_len+"##8##;"+mqInputRequest;
										WriteLog("mqInputRequest is:"+mqInputRequest);
										WriteLog("MqInputRequestInput Request Bytes : "+mqInputRequest.getBytes("UTF-16LE"));
										dout.write(mqInputRequest.getBytes("UTF-16LE"));
		            					dout.flush();				
		            				}
									WriteLog("Result before output Loop");
									
									

									byte[] readBuffer = new byte[50000];
	                			int num = din.read(readBuffer);
	                			boolean wait_flag = true;
	                			int out_len=0;
                                WriteLog("num lenght: "+num);
	                			if (num > 0) 
	                			{
	                				while(wait_flag){
	                					byte[] arrayBytes = new byte[num];
	                					System.arraycopy(readBuffer, 0, arrayBytes, 0, num);
	                					mqOutputResponse = mqOutputResponse + new String(arrayBytes, "UTF-16LE");
	                					if(mqOutputResponse.contains("##8##;")){
	                					String[]	mqOutputResponse_arr = mqOutputResponse.split("##8##;");
	                					mqOutputResponse = mqOutputResponse_arr[1];
										WriteLog("mqOutputResponse: "+ mqOutputResponse);
										WriteLog("OutPut Len: "+ mqOutputResponse_arr[0]);
	                					out_len = Integer.parseInt(mqOutputResponse_arr[0]);
										WriteLog("OutPut Len: "+ out_len);
	                					}
	                					if(out_len <= mqOutputResponse.getBytes("UTF-16LE").length){
	                						wait_flag=false;
	                					}
	                					Thread.sleep(200);
	                					 num = din.read(readBuffer);
	                					 
	                				}
	                				
                                   		WriteLog("MqOutputRequestOutput Response :\n"+mqOutputResponse);
		            					outputResponse= mqOutputResponse;	
		            					
		            					
		            			}
	
	           	   		 		}
								else{
	           	   		 		WriteLog("SocketServerIp and SocketServerPort is not maintained");
	           	   		 		WriteLog("SocketServerIp is not maintained "+socketServerIP);
	           	   		 		WriteLog(" SocketServerPort is not maintained "+socketServerPort.toString());
	           	   		 		outputResponse= "MQ details not maintained";
	           	   		 		}
							}
				
				//ended here
				
				 
	    
	        else{
	        	WriteLog("Genrate XML:,, ");
	        	outputResponse="Call not maintained";
	        }
				}
			}
		}
				
		catch (UnknownHostException e) 
		{		
			e.printStackTrace();
			outputResponse= "1";
		}
		catch(Exception e){
			WriteLog("Exception ocurred: "+e.getMessage());
			System.out.println("$$final_xml: "+final_xml);
			System.out.println("Exception occured in main thread: "+ e.getMessage());
			
			outputResponse = "";
			//outputResponse= valueSetCustomer(outputResponse);
		}	
		//working fine till here
		WriteLog("outputResponse123"+outputResponse+ "row_count" +row_count+ "CardNumber" +CardNumber);
		out.clear();
		out.flush();
			out.print(outputResponse+"!@#"+row_count+"!@#"+CardNumber);
		
		if(outputResponse.equalsIgnoreCase("")){
		WriteLog("outputResponse1111"+outputResponse);
			//valueSetCustomer(outputResponse,Call_name);
		}
		
		
%>

<%!
 
	private static String getMQInputXML(String sessionID, String cabinetName, String wi_name, String ws_name, String userName, StringBuffer final_xml) 
    {
		
		WriteLog("sessionId method getMQInputXML"+sessionID);
		WriteLog("cabinetName method getMQInputXML"+cabinetName);
		
		StringBuffer strBuff=new StringBuffer();
		strBuff.append("<APMQPUTGET_Input>");
		strBuff.append("<SessionId>"+sessionID+"</SessionId>");
		strBuff.append("<EngineName>"+cabinetName+"</EngineName>");
		strBuff.append("<XMLHISTORY_TABLENAME>NG_PL_XMLLOG_HISTORY</XMLHISTORY_TABLENAME>");
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
		
		//working fine till here
		  
	
	
	
		
		
		
		
		
		
            
           /* 
		   public static void getOutputXMLValues(String outputXMLMsg, String response) throws ParserConfigurationException
    {
        String sQuery = "";
        String tagValue = "";
        try
        {   
			WriteLog("inside getOutputXMLValues inside outputxml");
            String col_name = "Form_Control,Parent_Tag_Name,XmlTag_Name,InDirect_Mapping,Grid_Mapping,Grid_Table,grid_table_xml_tags";
			String sQuery_output =  "SELECT "+col_name+" FROM NG_pl_Integration_Field_Response where Call_Name ='"+response+"'";
	        WriteLog("RLOSCommon"+sQuery_output);
			inputXML = "<?xml version='1.0'?><APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option><Query>" + sQuery_output + "</Query><EngineName>" + cabinetName + "</EngineName><SessionId>" + sessionId+ "</SessionId></APSelectWithColumnNames_Input>";
			//OutputXML_header = WFCallBroker.execute(inputXML, wrapperIP, Integer.parseInt(wDSession.getM_objCabinetInfo().getM_strServerPort()), appServerType);
			
				try 
				{
					OutputXML_header = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, inputXML);
				} 
				catch (NGException e) 
				{
				   e.printStackTrace();
				} 
				catch (Exception ex) 
				{
				   ex.printStackTrace();
				}
				
			WriteLog("outputXML exceptions sdgffdy--> "+OutputXML_header);
		}
	}
	
		   
            sQuery = "SELECT "+col_name+" FROM NG_RLOS_Integration_Field_Response where Call_Name ='"+response+"'";
            List<List<String>> outputTableXML=formObject.getNGDataFromDataCache(sQuery);
 
 
            String[] col_name_arr = col_name.split(",");
 
            WriteLog("$$outputTableXML",outputTableXML.get(0).get(0)+outputTableXML.get(0).get(1)+outputTableXML.get(0).get(2)+outputTableXML.get(0).get(3));
            int n=outputTableXML.size();
            WriteLog("outputTableXML size: " , n+"");
 
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(outputXMLMsg)));
            WriteLog("name is doc : ", doc+"");
            NodeList nl = doc.getElementsByTagName("*");
 
            if( n> 0)
            {
                new LinkedHashMap<String, String>();
                Map<String, String> responseFileMap = new HashMap<String, String>();
                for(List<String> mylist:outputTableXML)
                {
                    for(int i=0;i<col_name_arr.length;i++)
                    {
                        responseFileMap.put(col_name_arr[i],mylist.get(i));
                    }
                    String form_control = (String) responseFileMap.get("Form_Control");
                    String parent_tag = (String) responseFileMap.get("Parent_Tag_Name");
                    String fielddbxml_tag = (String) responseFileMap.get("XmlTag_Name");
                    String Grid_col_tag = (String) responseFileMap.get("grid_table_xml_tags");
                    WriteLog(" Grid_col_tag Grid_col_tag"+Grid_col_tag);
                    if (parent_tag!=null && !parent_tag.equalsIgnoreCase(""))
                    {                    
                        for (int i = 0; i < nl.getLength(); i++)
                        {
                            nl.item(i);
                            if(nl.item(i).getNodeName().equalsIgnoreCase(fielddbxml_tag))
                            {
                                WriteLog("RLOS Common# getOutputXMLValues()  fielddbxml_tag: "+fielddbxml_tag);
                                String indirectMapping = (String) responseFileMap.get("InDirect_Mapping");
                                String gridMapping = (String) responseFileMap.get("Grid_Mapping");
 
                             
                                if(gridMapping.equalsIgnoreCase("Y"))
                                {
                                    WriteLog("Grid_col_tag_arr inside indirect mapping");
                                    if(Grid_col_tag.contains(",")){
                                        String Grid_col_tag_arr[]  =Grid_col_tag.split(",");
                                        //String grid_detail_str = nl.item(i).getNodeValue();
                                        NodeList childnode  = nl.item(i).getChildNodes();
                                        WriteLog("Grid_col_tag_arr","Grid_col_tag_arr: "+Grid_col_tag);   
                                        WriteLog("childnode","childnode"+childnode); 
                                        List<String> Grid_row = new ArrayList<String>(); 
 
                                        for(int k = 0;k<Grid_col_tag_arr.length;k++){
                                            for (int child_node_len = 0 ;child_node_len< childnode.getLength();child_node_len++){
                                                if(childnode.item(child_node_len).getNodeName().equalsIgnoreCase(Grid_col_tag_arr[k])){
                                                    WriteLog("child_node_len ","getTextContent: "+childnode.item(child_node_len).getNodeName());
                                                    WriteLog("child_node_len ","getTextContent: "+childnode.item(child_node_len).getTextContent());
                                                    Grid_row.add(childnode.item(child_node_len).getTextContent());
                                                }
                                            }
                                        }
                                        Grid_row.add(formObject.getWFWorkitemName());
                                        //code to add row in grid. and pass Grid_row in that.
                                        WriteLog("RLOS Common# getOutputXMLValues()", "List to be added in address grid: "+ Grid_row.toString());
                                        //formObject.addItem("cmplx_AddressDetails_cmplx_AddressGrid", Grid_row);
                                        Grid_row.clear();
                                    }
                                    else{
                                        WriteLog("RLOS Common# getOutputXMLValues()", "Grid mapping is Y for this tag and grid_table_xml_tags column is blank tag name: "+ fielddbxml_tag);
                                    }
 
                                }
                                else if(indirectMapping.equalsIgnoreCase("Y")){
                                    WriteLog("Grid_col_tag_arr","inside indirect mapping");
                                    String col_list = "xmltag_name,tag_value,indirect_tag_list,indirect_formfield_list,indirect_child_tag,form_control,indirect_val";
                                    sQuery = "SELECT "+col_list+" FROM NG_RLOS_Integration_Indirect_Response where Call_Name ='"+response+"' and XmlTag_Name = '"+nl.item(i).getNodeName()+"'";
                                    WriteLog("#RLOS Common Inside indirectMapping ", "query: "+sQuery);
                                    List<List<String>> outputindirect=formObject.getNGDataFromDataCache(sQuery);
                                    WriteLog("#RLOS Common Inside indirectMapping test tanshu ", "1");
                                    String col_list_arr[] = col_list.split(",");
                                    Map<String, String> gridResponseMap = new HashMap<String, String>();
                                    for(List<String> mygridlist:outputindirect)
                                    {
                                        WriteLog("#RLOS Common Inside indirectMapping test tanshu ", "inside list loop");
 
                                        for(int x=0;x<col_list_arr.length;x++)
                                        {
                                            gridResponseMap.put(col_list_arr[x],mygridlist.get(x));
                                            WriteLog("#RLOS Common Inside indirectMapping ", "inside put map"+x);
                                        }
                                        String xmltag_name = gridResponseMap.get("xmltag_name").toString();
                                        String tag_value = gridResponseMap.get("tag_value").toString();
                                        String indirect_tag_list = gridResponseMap.get("indirect_tag_list").toString();
                                        String indirect_formfield_list = gridResponseMap.get("indirect_formfield_list").toString();
                                        String indirect_child_tag = gridResponseMap.get("indirect_child_tag").toString();
                                        String indirect_form_control = gridResponseMap.get("form_control").toString();
                                        String indirect_val = gridResponseMap.get("indirect_val").toString();
                                        WriteLog("#RLOS Common Inside indirectMapping ", "all details fetched");
                                        if(indirect_form_control!=null && !indirect_form_control.equalsIgnoreCase("")){
                                            if( nl.item(i).getTextContent().equalsIgnoreCase(tag_value)){
                                                WriteLog("RLOS common: getOutputXMLValues","Indirect value for "+xmltag_name+" Indirect control name: "+indirect_form_control+" final value: "+indirect_val+" tag_value: "+tag_value);
                                                formObject.setNGValue(indirect_form_control,indirect_val);
                                            }
                                            // System.out.println("form Control: "+ indirect_form_control + "indirect val: "+ indirect_val);
 
                                        }
                                       
                                        else{
                                            WriteLog("Grid_col_tag_arr","inside indirect mapping part2 else");
                                            if(indirect_tag_list!=null ){
                                                NodeList childnode  = nl.item(i).getChildNodes();
                                                WriteLog("childnode","childnode"+childnode);
                                                if(indirect_tag_list.contains(",")){
                                                    WriteLog("#RLOS common indirect field values","inside indirect mapping part2 indirect_tag_list with ,");
                                                    String indirect_tag_list_arr[] = indirect_tag_list.split(",");
                                                    String indirect_formfield_list_arr[] = indirect_formfield_list.split(",");
                                                    if(indirect_tag_list_arr.length == indirect_formfield_list_arr.length){
                                                        for (int k=0; k<indirect_tag_list_arr.length;k++){
                                                            WriteLog("#RLOS Common inside child node 1 ", "node name : "+childnode.item(0).getNodeName() +" node data: "+childnode.item(1).getTextContent());
                                                            if(tag_value.equalsIgnoreCase(childnode.item(1).getTextContent()) && indirect_child_tag.equalsIgnoreCase(childnode.item(1).getNodeName())){
                                                            for (int child_node_len = 2 ;child_node_len< childnode.getLength();child_node_len++){
                                                                WriteLog("#RLOS common: ", "child node IF: Child node value: "+ child_node_len+" name: "+ childnode.item(child_node_len).getNodeName()+" child node value: "+ childnode.item(child_node_len).getTextContent());
 
                                                                if(childnode.item(child_node_len).getNodeName().equalsIgnoreCase(indirect_tag_list_arr[k])){
                                                                	 
                                                                    WriteLog("child_node_len","getTextContent"+childnode.item(child_node_len).getNodeName());
                                                                    WriteLog("child_node_len","getTextContent"+childnode.item(child_node_len).getTextContent());
                                                                    WriteLog("RLOS common: getOutputXMLValues","");
                                                                    WriteLog(""+indirect_formfield_list_arr[k]," :"+childnode.item(child_node_len).getTextContent());
                                                                    //formObject.setNGValue(indirect_formfield_list_arr[k],childnode.item(child_node_len).getTextContent());
                                                                }
                                                            }
 
                                                            }
                                                        }
                                                    }
                                                    else{
                                                        WriteLog("RLOS common: getOutputXMLValues","Error as for :"+xmltag_name+" indirect_formfield_list and indirect_tag_list dosent match");
                                                    } 
                                                }
                                                else{
                                                    WriteLog("#RLOS common indirect field values","inside indirect mapping part2 indirect_tag_list without ,");
                                                    for (int child_node_len = 0 ;child_node_len< childnode.getLength();child_node_len++){
                                                        if(tag_value.equalsIgnoreCase(childnode.item(child_node_len).getTextContent()) && indirect_child_tag.equalsIgnoreCase(childnode.item(child_node_len).getNodeName())){
                                                            if(childnode.item(child_node_len).getNodeName().equalsIgnoreCase(indirect_tag_list)){
                                                                WriteLog("child_node_len","getTextContent"+childnode.item(child_node_len).getNodeName());
                                                                WriteLog("child_node_len","getTextContent"+childnode.item(child_node_len).getTextContent());
                                                                WriteLog("RLOS common: getOutputXMLValues","");
                                                                WriteLog(""+indirect_formfield_list," :"+childnode.item(child_node_len).getTextContent());
                                                                //formObject.setNGValue(indirect_formfield_list,childnode.item(child_node_len).getTextContent());
                                                            }
                                                        }
 
                                                    }
                                                }
 
 
                                            }
                                        }     
                                    }
                                    //List<List<String>> outputIndirectXML=formObject.getNGDataFromDataCache(sQuery);
                                    //System.out.println("$$outputIndirectXML "+outputIndirectXML.get(0).get(0)+outputIndirectXML.get(0).get(1)+outputIndirectXML.get(0).get(2));
 
                                }
                                if(indirectMapping.equalsIgnoreCase("N") && gridMapping.equalsIgnoreCase("N"))
                                {    
                                    WriteLog("check14 " ,"check");
                                    tagValue = getTagValue(outputXMLMsg,nl.item(i).getNodeName());
                                    WriteLog("Node value ","tagValue:"+tagValue);
                                    WriteLog("Node form_control ","form_control:"+ form_control);
 
                                    WriteLog("$$tagValue NN  ",tagValue);
                                    WriteLog("$$form_control  NN ",form_control);
                                    //formObject.setNGValue(form_control,tagValue);
                                }
                            }
                        }
                    }
                }
            }
        }
        catch(Exception e)
        {
            WriteLog("Exception occured in getOutputXMLValues:  "+e.getMessage());
 
        }
    }
	
	*/
	
%>

