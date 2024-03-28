package com.newgen.custom.rakbank.resources.concrete;
import java.io.*;
import java.text.*;
import java.net.*;
//import java.sql.Connection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

//import com.newgen.wfdesktop.exception.*;
//import com.newgen.wfdesktop.xmlapi.*;
//import com.newgen.wfdesktop.util.*;
//import com.newgen.custom.*;
import com.newgen.dmsapi.DMSCallBroker;
import com.newgen.mcap.core.external.logging.concrete.LogMe;

//import java.io.UnsupportedEncodingException;
//import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.newgen.omni.wf.util.excp.NGException;
import com.newgen.omni.jts.cmgr.XMLParser;
import com.newgen.omni.wf.util.app.NGEjbClient;

//import java.util.Date;
import java.text.SimpleDateFormat;
//import java.io.FileInputStream;
//import java.io.File;
import java.util.*;
//import  java.util.ResourceBundle;
//import com.newgen.wfdesktop.xmlapi.WFCallBroker;




public class WebServicerequesthandler {
	

	
	public String getInputXMLValues(JSONObject json,JSONObject param_json) throws ParserConfigurationException{
		String outputResponse="";
		String sQuery="";
		String call_type="";
		String Call_name="";
		String form_control="";
//		String Mobility_form_control="";
		String parent_tag="";
		String tag_name="";
		String is_repetitive ="";
		String parent_tag_name="";
		String xmltag_name="";
		String inputXML="";
		String outputXML="";
		String OutputXML_header="";
		String mqInputRequest="";
		String is_mobility = json.get("is_mobility").toString().trim();
		String sessionId = json.get("sessionId").toString().trim();
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"sessionId :" + sessionId);	
		String appServerType=json.get("appServerType").toString().trim();
		String wrapperIP = json.get("wrapperIP").toString().trim();
		String MQServerAddress = json.get("MQServerAddress").toString().trim();
		String MQServerPort = json.get("MQServerPort").toString().trim();
		String wrapperPort = json.get("wrapperPort").toString().trim();
		String cabinetName = json.get("cabinetName").toString().trim();
	    String operation_type= "";
	    String Customer_type = "";
	    String cifId = "";
	    LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"appServerType --: "+appServerType);	
	    LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"wrapperIP --: "+wrapperIP);	
	    LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"wrapperPort --: "+wrapperPort);	
	    LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"cabinetName --: "+cabinetName);	
	    LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"jsonobj1is_mobility --: "+is_mobility);	
		
		//String row_count = request.getParameter("row_count"); 
		//String CardNumber = request.getParameter("CardNumber"); 
		

	
			
			/*try {
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
				out.print("false"+"!@#"+row_count+"!@#"+CardNumber);
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"Excetion Occured while reading from properties file: "+e.getMessage());
			}*/
		
		
		String form_control_val=null;
		JSONObject jsonObj=null;
		JSONParser parser=null;
		
		String request_name = json.get("callname").toString().trim();
	    String wi_name = json.get("wi_name").toString().trim();
		String ws_name = json.get("ws_name").toString().trim();
		
	//	String acc_id = request.getParameter("acc_id");
		
		
		//String Customer_type = request.getParameter("Customer_Type");
		request_name = request_name.trim();
		String request_Arr[]=request_name.split(",");
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"jsonobj1is_mobility --: "+is_mobility);	
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"jsonobj2: request_name "+request_name);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"jsonobj2: ws_name "+ws_name);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"jsonobj2: wi_name "+wi_name);
	//	LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"jsonobj2: cifId "+cifId);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"jsonobj2: Customer_type "+Customer_type);
		
		if(is_mobility.equalsIgnoreCase("Y")){
			operation_type =json.get("operationtype").toString().trim();
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"INSIDE MOB");
			//String jsonobj1=param_json;
			
			 parser = new JSONParser();
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"json result Result: " + jsonObj);	
			//LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"jsonobj1: "+jsonobj1);
			jsonObj = param_json;
			//containerObject= new JSONObject(container);
			Customer_type = jsonObj.get("Customer_Type").toString().trim();	
			if (jsonObj.containsKey("cif")) {
				cifId = jsonObj.get("cif").toString().trim();
				
				}
			 
			 LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"cifId:  "+cifId);
		}
		/*else{
			String jsonobj2 = request.getParameter("param_json");	
			jsonObj= new JSONObject(jsonobj2);
			
			if(request_name.equalsIgnoreCase("CARD_INSTALLMENT_DETAILS")){
				if(CardNumber!=null && !CardNumber.equalsIgnoreCase("")){
				jsonObj.put("CardNumber",CardNumber);
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"jsonobj2: jsonObj123 CardNumber"+CardNumber);
				}
			}
			
			
			jsonObj.put("cmplx_Customer_CIFNO",cifId.trim());
			
			//Changes done for company AECB start
			if(request_name.equalsIgnoreCase("ExternalExposure")){
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"inside ExternalExposure condition");
				String comp_cif = jsonObj.getString("cif");
				String trade_comp_name= jsonObj.getString("trade_comp_name");
				String trade_lic_no = jsonObj.getString("trade_lic_no");
				if(comp_cif.contains(",")){
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"Company cif contains multiple cif");
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
			}
			//Changes done for company AECB end
			if(cifId!=null && !cifId.equalsIgnoreCase("")){
				jsonObj.put("cmplx_Customer_CIFNO",cifId.trim());
			}
			else{
				jsonObj.put("cmplx_Customer_CIFNO","");
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"jsonobj2: jsonObj "+jsonObj);
			}
			
			if(acc_id!=null && !acc_id.equalsIgnoreCase("")){
				jsonObj.put("acc_id",acc_id.trim());
			}
			else{
				jsonObj.put("acc_id","");
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"jsonobj2: jsonObj "+jsonObj);
			}
		}*/
		
		
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"RLOSCommon");

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
		    String socketServerIP;
//		    String socketPort;
		    Integer socketServerPort;
		   	XMLParser xmlParserData=null;
			String sQuery_header=null;
			String fin_call_name="Customer_details, Customer_eligibility,new_account_req";
			
		   LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"$$outputgGridtXML");
			try
			{
				
				for(int i =0;i<request_Arr.length;i++){
		
					if(request_Arr[i].equalsIgnoreCase("TRANSUM") || request_Arr[i].equalsIgnoreCase("AVGBALDET") || request_Arr[i].equalsIgnoreCase("RETURNDET")|| request_Arr[i].equalsIgnoreCase("LIENDET") || request_Arr[i].equalsIgnoreCase("SIDET")|| request_Arr[i].equalsIgnoreCase("SALDET")||request_Arr[i].equalsIgnoreCase("Primary_CIF")|| request_Arr[i].equalsIgnoreCase("Corporation_CIF"))
					{
						//Changes done for EIDA from Mobility, select call header column name is chaged.
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"INSIDE FINANCIAL_SUMMARY case 1 = "+request_Arr[i]);
						sQuery_header = "SELECT Header as Call_header,Footer,parenttagname FROM NG_Integration with (nolock) where Call_name='FINANCIAL_SUMMARY'";
					}
					
					else if ((request_Arr[i].equalsIgnoreCase("OTP_SUMMARY"))&&(is_mobility.equalsIgnoreCase("Y"))){
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"INSIDE OTP_Summary case ="+request_Arr[i]);
						sQuery_header = "SELECT Header as Call_header,Footer,parenttagname with (nolock) FROM NG_Integration where Call_name='OTP_MANAGEMENT'";
					}
					else{
						sQuery_header = "SELECT Header as Call_header,Footer,parenttagname FROM NG_Integration with (nolock) where Call_name='"+request_Arr[i]+"'";
					}
				
							
		        LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"RLOSCommon--- "+sQuery_header);
				inputXML = "<?xml version='1.0'?><APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option><Query>" + sQuery_header + "</Query><EngineName>" + cabinetName + "</EngineName><SessionId>" + sessionId+ "</SessionId></APSelectWithColumnNames_Input>";
				
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"inputXML -->"+inputXML);
				//OutputXML_header = WFCallBroker.execute(inputXML, wrapperIP, Integer.parseInt(wDSession.getM_objCabinetInfo().getM_strServerPort()), appServerType);
				
				try 
				{
					//OutputXML_header = NGEjbClient.getSharedInstance().makeCall(wrapperIP, wrapperPort, appServerType, inputXML);
					OutputXML_header = makeCall(wrapperIP, Short.valueOf(wrapperPort), inputXML);
				
				} 
				catch (NGException e) 
				{
					writePrintStackTrace(e);
				   e.printStackTrace();
				} 
				catch (Exception ex) 
				{
					writePrintStackTrace(ex);
				   ex.printStackTrace();
				}
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"outputXML exceptions-->"+OutputXML_header);
		        
				xmlParserData = new XMLParser();
				xmlParserData.setInputXML((OutputXML_header));
				
				int recordcount = Integer.parseInt(xmlParserData.getValueOf("TotalRetrieved"));
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"Number of records are ="+recordcount);
				
				mainCodeData = xmlParserData.getValueOf("MainCode");
		
				if(mainCodeData.equals("0"))
		       	{
					//Changes done for EIDA from Mobility, select call header column name is chaged.
		        	header = xmlParserData.getNextValueOf("Call_header");
		            footer = xmlParserData.getNextValueOf("Footer");
		            parentTagName = xmlParserData.getNextValueOf("parenttagname");
					
		            LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"header ="+header+" footer="+footer+" parentTagName"+parentTagName );
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"operation_type : "+ operation_type);
					if(is_mobility.equalsIgnoreCase("Y"))
					{ 
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"INSIDE MOBILITY CALLING DB ="+request_Arr[i]);
						
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
							sQuery = "SELECT call_type, Call_name,Mobility_form_control,parent_tag_name,xmltag_name,is_repetitive,default_val,data_format FROM NG_Integration_field_Mapping with (nolock) where Call_name='FINANCIAL_SUMMARY' and Operation_name='"+jsonObj.get("OperationType")+"' ORDER BY tag_seq ASC" ;
						}
						else if (request_Arr[i].equalsIgnoreCase("OTP_Management"))
						{
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"INSIDE OTP_Management ="+request_Arr[i]);
							
							sQuery = "SELECT call_type, Call_name,Mobility_form_control,parent_tag_name,xmltag_name,is_repetitive,default_val,data_format FROM NG_Integration_field_Mapping where Call_name='OTP_MANAGEMENT' and  Operation_name='"+operation_type+"' and  Active = 'Y' ORDER BY tag_seq ASC";
							
						}
						else if (request_Arr[i].equalsIgnoreCase("Customer_Details"))
						{
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"INSIDE CustDE|T ="+request_Arr[i]);
							sQuery = "SELECT call_type, Call_name,Mobility_form_control,parent_tag_name,xmltag_name,is_repetitive,default_val,data_format FROM NG_Integration_field_Mapping with (nolock) where Call_name='Customer_Details' and  Operation_name='Primary_CIF' and  Active = 'Y' ORDER BY tag_seq ASC";
						}
						else if (request_Arr[i].equalsIgnoreCase("OTP_Summary"))
						{
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"INSIDE OTP_Summary ="+request_Arr[i]);
							
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
					/*else
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
							sQuery = "SELECT call_type, Call_name,form_control,parent_tag_name,xmltag_name,is_repetitive,default_val,data_format FROM NG_Integration_field_Mapping where Call_name='"+request_Arr[i]+"'ORDER BY tag_seq ASC" ;
						}									
					}*/
		    		
					
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"sQuery123 "+sQuery);
					inputXML = "<?xml version='1.0'?><APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option><Query>" + sQuery + "</Query><EngineName>" + cabinetName + "</EngineName><SessionId>" + sessionId+ "</SessionId></APSelectWithColumnNames_Input>";
					//outputXML = WFCallBroker.execute(inputXML, wrapperIP, Integer.parseInt(wDSession.getM_objCabinetInfo().getM_strServerPort()), appServerType);
					
					try 
					{
						outputXML = makeCall(wrapperIP, Short.valueOf(wrapperPort), inputXML);
					} 
					catch (NGException e) 
					{
						writePrintStackTrace(e);
					   e.printStackTrace();
					} 
					catch (Exception ex) 
					{
						writePrintStackTrace(ex);
					   ex.printStackTrace();
					}
					
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"outputXML exceptions-->"+outputXML);
					xmlParserData = new XMLParser();
					xmlParserData.setInputXML((outputXML));
					int recordcount1 = Integer.parseInt(xmlParserData.getValueOf("TotalRetrieved"));
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"Number of records are in 2nd query are ="+recordcount1);
					mainCodeData = xmlParserData.getValueOf("MainCode");
					if(mainCodeData.equals("0"))
					{
						final_xml.setLength(0);
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"final_xml in maincode:"+final_xml);
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
						 LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"call_type ="+call_type+" Call_name="+Call_name+" form_control="+form_control+" parent_tag_name="+parent_tag_name+" xmltag_name="+xmltag_name+" is_repetitive="+is_repetitive+"default_val="+default_val );
							
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
								LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"call_type ="+call_type+" Call_name="+Call_name+" form_control="+form_control+" parent_tag_name="+	parent_tag_name+" xmltag_name="+xmltag_name+" is_repetitive="+is_repetitive+"default_val="+default_val );
			                    					
								 form_control_val="";
		                            java.util.Date startDate;
		        				
								
							   if (parent_tag!=null && !parent_tag.equalsIgnoreCase(""))
	                            {
	                            	LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"inside 1st if inside 1st if Parent tag: "+parent_tag+" Child tag: "+tag_name);
									LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"inside 1st if int_xml: "+int_xml);
	                                if(int_xml.containsKey(parent_tag))
	                                {
	                                	LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"inside 1st if inside 2nd if");
	                                    String xml_str = int_xml.get(parent_tag);
	                                    LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"inside 1st if inside 2nd if xml string"+xml_str);
	                                    if(is_repetitive.equalsIgnoreCase("Y") && int_xml.containsKey(tag_name)){
	                                    	LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"inside 1st if inside 3rd if xml string");
	                                        xml_str = int_xml.get(tag_name)+ "</"+tag_name+">" +"<"+ tag_name+">";
	                                        System.out.println("value after adding "+ Call_name+": "+xml_str);
	                                        LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"inside 1st if inside 3rd if xml string xml string"+xml_str);
	                                        int_xml.remove(tag_name);
	                                        int_xml.put(tag_name, xml_str);
	                                        LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"inside 1st if inside 3rd if xml string xml string int_xml");
	                                    }
	                                    else{
	                                    	LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"inside else of parent tag value after adding "+ Call_name+": "+xml_str+" form_control name: "+form_control);
											LogMe.logMe(LogMe.LOG_LEVEL_DEBUG," valuie of form control123: "+form_control);
											
											if(form_control.equalsIgnoreCase("") && default_val.equalsIgnoreCase("")){
												LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"inside if added by me inside");
												xml_str = xml_str + "<"+tag_name+">"+"</"+ tag_name+">";
												LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"added by xml xml_str"+xml_str);
											}
											else if (!(form_control==null || form_control.equalsIgnoreCase("")||  form_control.equalsIgnoreCase("null"))){
		                                            LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"inside else of parent tag 1 form_control_val"+ form_control_val);
													if(fin_call_name.toUpperCase().contains(request_Arr[i].toUpperCase())){
														form_control_val = (jsonObj.get(form_control).toString().trim()+"").toUpperCase();
													}
													else {
														form_control_val = jsonObj.get(form_control).toString().trim()+"";
													}
		                                            
		                                            if(!data_format12.equalsIgnoreCase("text")){
			                                        	String[] format_arr = data_format12.split(":");
			                                        	String format_name = format_arr[0];
			                                        	String format_type = format_arr[1];
			                                        	LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,""+"format_name"+format_name);
			                                        	LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,""+"format_type"+format_type);
			                                        	
			                                        	if(format_name.equalsIgnoreCase("date")){
			                                        		DateFormat df = new SimpleDateFormat("dd/MM/yyyy"); 
				                                            DateFormat df_new = new SimpleDateFormat(format_type);
				                                           
				                                            try {
				                                                startDate = df.parse(form_control_val);
				                                                form_control_val = df_new.format(startDate);
				                                                LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"RLOSCommon#Create Input"+" date conversion: final Output: "+form_control_val+ " requested format: "+format_type);
				                                             
				                                            } catch (ParseException e) {
				                                            	LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"RLOSCommon#Create Input"+" Error while format conversion: "+e.getMessage());
				                                            	writePrintStackTrace(e);
				                                            	e.printStackTrace();
				                                            }
				                                            catch (Exception e) {
				                                            	LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"RLOSCommon#Create Input"+" Error while format conversion: "+e.getMessage());
				                                                e.printStackTrace();
				                                                writePrintStackTrace(e);
				                                            }
			                                        	}
			                                        	//change here for other input format
			                                        	
			                                        }
		                                            
		                                            LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"inside else of parent tag form_control_val"+ form_control_val);
		                                            xml_str = xml_str + "<"+tag_name+">"+form_control_val
		                                                    +"</"+ tag_name+">";
		                                            LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"inside else of parent tag xml_str xml_str"+ xml_str);
		                                        }
	                                        else if (tag_name.equalsIgnoreCase("CIFIdValue"))
											{
												form_control_val = cifId;
												LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"inside else of parent tag form_control_val"+ form_control_val);
	                                            xml_str = xml_str + "<"+tag_name+">"+form_control_val
	                                                    +"</"+ tag_name+">";
	                                            LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"inside else of parent tag xml_str xml_str"+ xml_str);
											}
	                                        else if(default_val==null || default_val.equalsIgnoreCase("")){
	                                        	  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"#RLOS Common GenerateXML IF part no value found for tag name: "+ tag_name);
	                                        }
	                                        else{
	                                            LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"#RLOS Common GenerateXML inside set default value");
	                                            form_control_val = default_val;
	                                                LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"#RLOS Common GenerateXML inside set default value form_control_val"+ form_control_val);
	                                                xml_str = xml_str + "<"+tag_name+">"+form_control_val
	                                                        +"</"+ tag_name+">";
	                                                LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"#RLOS Common GenerateXML inside else of parent tag form_control_val xml_str1"+ xml_str);
	                                            
	                                        }
	                                        int_xml.put(parent_tag, xml_str);
	                                        LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"else of generatexml RLOSCommon inside else"+xml_str);
											
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
	                                    LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"inside else of parent tag main 2 value after adding "+ Call_name+": "+new_xml_str+" form_control name: "+form_control);
										LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"valuie of form control1234: "+form_control);
										if(!(form_control==null || form_control.equalsIgnoreCase("")||  form_control.equalsIgnoreCase("null")))
										//if(!(jsonObj.getString(form_control)==null || jsonObj.getString(form_control).equalsIgnoreCase("")||  jsonObj.getString(form_control).equalsIgnoreCase("null")))
										{
	                                        LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"inside else of parent tag 1 form_control_val"+ form_control_val);
											if(fin_call_name.toUpperCase().contains(request_Arr[i].toUpperCase())){
												form_control_val = jsonObj.get(form_control).toString().trim();
											}
											else{
												form_control_val = jsonObj.get(form_control).toString().trim();
											}
	                                        
	                                        LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"inside else of parent tag form_control_val"+ form_control_val);
											if(!data_format12.equalsIgnoreCase("text")){
				                                        	String[] format_arr = data_format12.split(":");
				                                        	String format_name = format_arr[0];
				                                        	String format_type = format_arr[1];
				                                        	
															LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"format_name"+format_name);
				                                        	
															LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"format_type"+format_type);
				                                        	
				                                        	if(format_name.equalsIgnoreCase("date")){
				                                        		DateFormat df = new SimpleDateFormat("dd/MM/yyyy"); 
					                                            DateFormat df_new = new SimpleDateFormat(format_type);
					                                           
					                                            try {
					                                                startDate = df.parse(form_control_val);
					                                                form_control_val = df_new.format(startDate);
					                                                
					                                             
					                                            } catch (ParseException e) {
					                                            	writePrintStackTrace(e);
					                                                e.printStackTrace();
					                                            }
					                                            catch (Exception e) {
					                                            	writePrintStackTrace(e);
					                                                e.printStackTrace();
					                                            }
				                                        	}
				                                        	//change here for other input format
				                                        	
				                                        }
	                                        new_xml_str = new_xml_str + "<"+tag_name+">"+form_control_val
	                                                +"</"+ tag_name+">";
	                                        LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"inside else of parent tag xml_str new_xml_str"+ new_xml_str);
	                                    }
	                                    
	                                    else if(default_val==null || default_val.equalsIgnoreCase("")){
												if(int_xml.containsKey(parent_tag)||is_repetitive.equalsIgnoreCase("Y")){
													new_xml_str = new_xml_str + "<"+tag_name+">"+"</"+ tag_name+">";
												}
	                                        LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"#RLOS Common GenerateXML Inside Else Part no value found for tag name: "+ tag_name);
	                                    }
	                                    else{
	                                        LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"#RLOS Common GenerateXML inside set default value");
	                                        form_control_val = default_val;
	                                            LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"#RLOS Common GenerateXML inside set default value form_control_val"+ form_control_val);
	                                            new_xml_str = new_xml_str + "<"+tag_name+">"+form_control_val
	                                                    +"</"+ tag_name+">";
	                                            LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"#RLOS Common GenerateXML inside else of parent tag form_control_val xml_str1 xml_str"+ new_xml_str);
	                                        
	                                    }
	                                    //String new_xml_str = "<"+tag_name+">"+jsonObj.getString(form_control)
	                                        //                +"</"+ tag_name+">";
	                                    int_xml.put(parent_tag, new_xml_str);
	                                    LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"else of generatexml RLOSCommon inside else"+new_xml_str);
	                                    
	                                }
	                            }
								
								
							}
							
							final_xml=final_xml.append("<").append(parentTagName).append(">");
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"RLOS:"+final_xml);
		        			
		        			Iterator<Map.Entry<String,String>> itr = int_xml.entrySet().iterator();
		        			while (itr.hasNext())
		        			{
		        				Map.Entry<String, String> entry =  itr.next();
		        				if(final_xml.indexOf((entry.getKey()))>-1){
		        					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"RLOS itr_value: Key: "+ entry.getKey()+" Value: "+entry.getValue());
		        					final_xml = final_xml.insert(final_xml.indexOf("<"+entry.getKey()+">")+entry.getKey().length()+2, entry.getValue());
		        					itr.remove();
		        				}

		        			}

							
							final_xml=final_xml.append("</").append(parentTagName).append(">");
		        				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"FInal XMLnew before clean method is: "+final_xml.toString());
								if ("DECTECH".equalsIgnoreCase(Call_name)){
			        					final_xml = new StringBuilder(Clean_Xml(final_xml.toString(),Call_name));
			        				}
								LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"FInal XMLnew after clean method is: "+final_xml.toString());
		        				final_xml.insert(0, header);
		        				final_xml.append(footer);
		        				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"FInal XMLnew with header: "+final_xml.toString());
		        	 }
					
					//code added here
					String sMQuery = "SELECT SocketServerIP,SocketServerPort FROM NG_RLOS_MQ_TABLE with (nolock)";
		    		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"sMQuery "+sMQuery);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"sessionId"+sessionId);
					inputXML = "<?xml version='1.0'?><APSelectWithColumnNames_Input><Option>APSelectWithColumnNames</Option><Query>" + sMQuery + "</Query><EngineName>" + cabinetName + "</EngineName><SessionId>" + sessionId+ "</SessionId></APSelectWithColumnNames_Input>";
					//outputXML = WFCallBroker.execute(inputXML, wrapperIP, Integer.parseInt(wDSession.getM_objCabinetInfo().getM_strServerPort()), appServerType);
					
					try 
					{
						outputXML = makeCall(wrapperIP, Short.valueOf(wrapperPort), inputXML);
					} 
					catch (NGException e) 
					{
						writePrintStackTrace(e);
					   e.printStackTrace();
					} 
					catch (Exception ex) 
					{
						writePrintStackTrace(ex);
					   ex.printStackTrace();
					}
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"outputXML exceptions mq --> "+outputXML);
					xmlParserData = new XMLParser();
					xmlParserData.setInputXML((outputXML));
					int recordcount2 = Integer.parseInt(xmlParserData.getValueOf("TotalRetrieved"));
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"Number of records are in 3rd query are ="+recordcount2);
					mainCodeData = xmlParserData.getValueOf("MainCode");
					if(mainCodeData.equals("0"))
					{
					//socketServerIP = xmlParserData.getNextValueOf("SocketServerIP");
					
					socketServerIP = MQServerAddress;
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"socketServerIP"+socketServerIP);
		            //socketServerPort = Integer.parseInt(xmlParserData.getNextValueOf("SocketServerPort"));
		        
		            socketServerPort = Integer.parseInt(MQServerPort);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"socketServerPort"+socketServerPort.toString());
					
					if(!(socketServerIP.equalsIgnoreCase("") && socketServerIP.equalsIgnoreCase(null) && socketServerPort.equals(null) && socketServerPort.equals(0)))
		           	   		 	{
		           	   		 			socket = new Socket(socketServerIP, socketServerPort);
										LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"socket"+socket);
		           	   		 			outstream = socket.getOutputStream();
										LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"outstream"+outstream);
		           	   		 			socketInputStream = socket.getInputStream();
										LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"socketInputStream:"+socketInputStream);
		           	   		 			dout = new DataOutputStream(outstream);
										LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"dout"+dout);
		           	   		 			din = new DataInputStream(socketInputStream);
										LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"din"+din);
										//LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"sessionId befor method"+Integer.parseInt(wDSession.getM_objCabinetInfo().getM_strServerPort()));
			           	   		 		mqInputRequest= getMQInputXML(sessionId,cabinetName,wi_name,ws_name,userName,final_xml);
										LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"mqInputRequest is:"+mqInputRequest);
			            	         	if (mqInputRequest != null && mqInputRequest.length() > 0) 
			            				{
			            	         		int outPut_len = mqInputRequest.getBytes("UTF-16LE").length;
											LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"Final XML output len: "+outPut_len+"");
											mqInputRequest = outPut_len+"##8##;"+mqInputRequest;
											LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"MqInputRequest"+"Input Request Bytes : "+mqInputRequest.getBytes("UTF-16LE"));
											dout.write(mqInputRequest.getBytes("UTF-16LE"));
											dout.flush();
			            				}
										LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"Result before output Loop");
									byte[] readBuffer = new byte[50000];
		                			int num = din.read(readBuffer);
		                			boolean wait_flag = true;
		                			int out_len=0;
	                                LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"num lenght: "+num);
		                			if (num > 0) 
		                			{
		                				while(wait_flag){
		                					byte[] arrayBytes = new byte[num];
		                					System.arraycopy(readBuffer, 0, arrayBytes, 0, num);
		                					mqOutputResponse = mqOutputResponse + new String(arrayBytes, "UTF-16LE");
		                					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"mqOutputResponse@@: "+ mqOutputResponse);
		                					if(mqOutputResponse.contains("##8##;")){
		                					String[]	mqOutputResponse_arr = mqOutputResponse.split("##8##;");
		                					// added by abhishek
		                					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"mqOutputResponse_arr: "+ out_len);
		                					for(int j=0;j<mqOutputResponse_arr.length;j++){
		                						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG," "+mqOutputResponse_arr[j]);
		                					}
		                					mqOutputResponse = mqOutputResponse_arr[1];
											LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"mqOutputResponse: "+ mqOutputResponse);
											LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"OutPut Len: "+ mqOutputResponse_arr[0]);
		                					out_len = Integer.parseInt(mqOutputResponse_arr[0]);
											LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"OutPut Len: "+ out_len);
		                					}
		                					if(out_len <= mqOutputResponse.getBytes("UTF-16LE").length){
		                						wait_flag=false;
		                					}
		                					Thread.sleep(200);
		                					 num = din.read(readBuffer);
		                					 
		                				}
		                				
	                                   		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"MqOutputRequestOutput Response :\n"+mqOutputResponse);
			            					outputResponse= mqOutputResponse;	
			            					
			            					
			            				}
		           	   		 		}
									else{
		           	   		 		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"SocketServerIp and SocketServerPort is not maintained");
		           	   		 		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"SocketServerIp is not maintained "+socketServerIP);
		           	   		 		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG," SocketServerPort is not maintained "+socketServerPort.toString());
		           	   		 		outputResponse= "MQ details not maintained";
		           	   		 		}
								}
					
					//ended here
					
					 
		    
		        else{
		        	LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"Genrate XML:,, ");
		        	outputResponse="Call not maintained";
		        }
					}
				}
			}
					
			catch (UnknownHostException e) 
			{		
				writePrintStackTrace(e);
				e.printStackTrace();
				outputResponse= "1";
			}
			catch(Exception e){
				writePrintStackTrace(e);
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"Exception ocurred: "+e.getMessage());
				outputResponse = "";
				//outputResponse= valueSetCustomer(outputResponse);
			}	
			//working fine till here
			//LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"outputResponse123"+outputResponse+ "row_count" +row_count+ "CardNumber" +CardNumber);
		
			System.out.flush();
			
			if(outputResponse.equalsIgnoreCase("")){
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"outputResponse1111"+outputResponse);
				//valueSetCustomer(outputResponse,Call_name);
			}
			return outputResponse;
			
		}
		
		private static String getMQInputXML(String sessionID, String cabinetName, String wi_name, String ws_name, String userName, StringBuilder final_xml) 
	    {
			
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"sessionId method getMQInputXML"+sessionID);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"cabinetName method getMQInputXML"+cabinetName);
			
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
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"Exception occured in valueSetCustomer method:  "+ex.getMessage());
			}
			finally {
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"Inside final block");
			}
			return doc;
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

				}
				return Output_Xml;
			}
			/***
			 * Description		:	For writing e.printStackTrace use writePrintStackTrace() function e.g. Exception/Throwable
			 * Author			:	Sumit Balyan
			 * Date				:	10/08/2018					
			 * 
			 *  **/
		 private void writePrintStackTrace(Exception e) {
		        StringWriter sw = new StringWriter();
		        PrintWriter pw = new PrintWriter(sw);
		        e.printStackTrace(pw);
		        String msg = sw.toString();
		        LogMe.logMe(LogMe.LOG_LEVEL_ERROR, msg);
		    }

		private void writePrintStackTrace(Throwable t) {
		            // TODO: handle exception
		            LogMe.logMe(LogMe.LOG_LEVEL_ERROR,
		                    "EXCEPTION OCCOURED while submitting->> t" + t);
		            StringWriter sw = new StringWriter();
		            PrintWriter pw = new PrintWriter(sw);
		            t.printStackTrace(pw);
		            // sw.toString();
		            LogMe.logMe(LogMe.LOG_LEVEL_ERROR,
		                    "EXCEPTION OCCOURED while submitting->>" + sw);
		}
		 public void valueSetCustomer(String outputResponse, String Call_name)
		    {
		        LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"RLOSCommon valueSetCustomer Inside valueSetCustomer():"+outputResponse);
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
		                LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"RLOSCommon valueSetCustomer outputXMLHead");
		            }
		            objXMLParser.setInputXML(outputXMLHead);
		            if(outputXMLHead.indexOf("<MsgFormat>")>-1)
		            {
		                response= outputXMLHead.substring(outputXMLHead.indexOf("<MsgFormat>")+11,outputXMLHead.indexOf("</MsgFormat>"));
		                LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"$$response "+response);
		            }
		            if(outputXMLHead.indexOf("<ReturnDesc>")>-1)
		            {
		                returnDesc= outputXMLHead.substring(outputXMLHead.indexOf("<ReturnDesc>")+12,outputXMLHead.indexOf("</ReturnDesc>"));					
		                LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"$$returnDesc "+returnDesc);
						if(Call_name.equalsIgnoreCase("CUSTOMER_EXPOSURE"))
						{
							//formObject.setNGValue("aecb_call_status",returnDesc);
						}
		            }
		            if(outputXMLHead.indexOf("<ReturnCode>")>-1)
		            {
		                returnCode= outputXMLHead.substring(outputXMLHead.indexOf("<ReturnCode>")+12,outputXMLHead.indexOf("</ReturnCode>"));
		                LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"$$returnCode "+returnCode);
		            }
		            
		            if(outputResponse.indexOf("<MQ_RESPONSE_XML>")>-1)
		            {
		                outputXMLMsg=outputResponse.substring(outputResponse.indexOf("<MQ_RESPONSE_XML>")+17,outputResponse.indexOf("</MQ_RESPONSE_XML>"));
		                LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"$$outputXMLMsg "+outputXMLMsg);
		                LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"$$outputXMLMsg getOutputXMLValues check inside getOutputXMLValues");
						//getOutputXMLValues(outputXMLMsg,response,sessionID,cabinetName);
		                LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"$$outputXMLMsg outputXMLMsg");
		            }
		           
		        }
		        catch(Exception e)
		        {            
		            LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"Exception occured in valueSetCustomer method:  "+e.getMessage());
		            System.out.println("Exception occured in valueSetCustomer method: "+ e.getMessage());
		        }
		    }
		//with wrapper
			private String makeCall(String jtsAddress, short jtsPort, String
					inputXML)
			throws Exception {
				String output = null;
				try {
					int debug = 0; // (0|1)
					output = DMSCallBroker
					.execute(inputXML, jtsAddress, jtsPort, debug);
				} catch (Exception e) {
					e.printStackTrace();
					LogMe.logMe(LogMe.LOG_LEVEL_ERROR, "error=> " + e);
					throw new Exception("OF setup incomplete");
				} catch (Error e) {
					e.printStackTrace();
					LogMe.logMe(LogMe.LOG_LEVEL_ERROR, "error=> " + e);
					throw new Exception("OF setup incomplete");
				}
				return output;
			}

		 
	}


