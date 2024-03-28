package com.newgen.omniforms.user;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.util.Common;
import com.newgen.omniforms.util.SKLogger_CC;

public class CC_Integration_Input extends Common implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	CC_Common CC_Comn = new CC_Common();
	
	

	public String GenerateXML(String callName, String Operation_name) {

		SKLogger_CC.writeLog("RLOSCommon", "Inside GenerateXML():");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		StringBuffer final_xml = new StringBuffer("");
		String header = "";
		String footer = "";
		String parentTagName = "";
		String sQuery = null;
		java.util.Date startDate;

		String fin_call_name = "Customer_details, Customer_eligibility,new_customer_req,new_account_req,DEDUP_SUMMARY";
		SKLogger_CC.writeLog("$$outputgGridtXML ", "before try");
		try {

			String sQuery_header = "SELECT Header,Footer,parenttagname FROM NG_Integration with (nolock) where Call_name='" + callName + "'";
			SKLogger_CC.writeLog("RLOSCommon", "sQuery" + sQuery_header);
			List<List<String>> OutputXML_header = formObject.getDataFromDataSource(sQuery_header);
			if (!OutputXML_header.isEmpty()) {
				SKLogger_CC.writeLog("RLOSCommon header: ", OutputXML_header.get(0).get(0)+ " footer: "+ OutputXML_header.get(0).get(1)+ " parenttagname: " + OutputXML_header.get(0).get(2));
				header = OutputXML_header.get(0).get(0);
				footer = OutputXML_header.get(0).get(1);
				parentTagName = OutputXML_header.get(0).get(2);
				String col_n = "call_type,Call_name,form_control,parent_tag_name,xmltag_name,is_repetitive,default_val,data_format";
				if (!(Operation_name.equalsIgnoreCase("") || Operation_name.equalsIgnoreCase(null))) {
					SKLogger_CC.writeLog("inside if of operation","operation: " + Operation_name+ "\n callName: "+ callName);
					sQuery = "SELECT "+ col_n+ " FROM NG_Integration_CC_field_Mapping with (nolock) where Call_name='"+ callName+ "' and active = 'Y' and Operation_name='"+ Operation_name + "' ORDER BY tag_seq ASC";

				} else {
					SKLogger_CC.writeLog("inside else of operation","operation" + Operation_name);
					sQuery = "SELECT "+ col_n+ " FROM NG_Integration_CC_field_Mapping with (nolock) where Call_name='"+ callName+ "' and active = 'Y' ORDER BY tag_seq ASC";
				}

				SKLogger_CC.writeLog("RLOSCommon", "sQuery " + sQuery);
				List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
				SKLogger_CC.writeLog("OutputXML", "OutputXML" + OutputXML);

				if (!OutputXML.isEmpty()) {
					// SKLogger_CC.writeLog("$$AKSHAY",OutputXML.get(0).get(0)+OutputXML.get(0).get(1)+OutputXML.get(0).get(2)+OutputXML.get(0).get(3));
					SKLogger_CC.writeLog("GenerateXML Integration field mapping table",	OutputXML.get(0).get(0) + OutputXML.get(0).get(1)+ OutputXML.get(0).get(2)+ OutputXML.get(0).get(3)+ OutputXML.get(0).get(4));
					SKLogger_CC.writeLog("GenerateXML Integration field mapping table",	OutputXML.get(0).get(0) + OutputXML.get(0).get(1)+ OutputXML.get(0).get(2)+ OutputXML.get(0).get(3));
					int n = OutputXML.size();
					SKLogger_CC.writeLog("CC_Common", "row count:" + n);

					if (n > 0) {
						SKLogger_CC.writeLog("", "column length"+ col_n.length());
						Map<String, String> int_xml = new LinkedHashMap<String, String>();
						Map<String, String> recordFileMap = new HashMap<String, String>();

						for (List<String> mylist : OutputXML) {
							// for(int i=0;i<col_n.length();i++)
							for (int i = 0; i < 8; i++) {
								// SKLogger_CC.writeLog("rec: "+records.item(rec));
								SKLogger_CC.writeLog("", "column length values"+ col_n);
								String[] col_name = col_n.split(",");
								recordFileMap.put(col_name[i], mylist.get(i));
							}
							recordFileMap.get("call_type");
							String Call_name = (String) recordFileMap.get("Call_name");
							String form_control = (String) recordFileMap.get("form_control");
							String parent_tag = (String) recordFileMap.get("parent_tag_name");
							String tag_name = (String) recordFileMap.get("xmltag_name");
							String is_repetitive = (String) recordFileMap.get("is_repetitive");
							String default_val = (String) recordFileMap.get("default_val");
							String data_format12 = (String) recordFileMap.get("data_format");

							SKLogger_CC.writeLog("#RLOS COmmonm inside generate XML: ","tag_name : " + tag_name+ " valuie of default_val: "+ default_val + " Call_name: "+ Call_name + " parent_tag"+ parent_tag);
							String form_control_val = "";

							if (Call_name.equalsIgnoreCase("DEDUP_SUMMARY")) {
								int_xml = DEDUP_SUMMARY_Custom(int_xml, recordFileMap);
							} else if (Call_name.equalsIgnoreCase("BLACKLIST_DETAILS")) {
								int_xml = Blacklist_Details_custom(int_xml, recordFileMap);
							} else if (Call_name.equalsIgnoreCase("NEW_CUSTOMER_REQ")) {
								int_xml = NEW_CUSTOMER_Custom(int_xml, recordFileMap);
							} else if (Call_name.equalsIgnoreCase("CUSTOMER_UPDATE_REQ")) {
								int_xml = CUSTOMER_UPDATE_Custom(int_xml, recordFileMap);
							} else if (Call_name.equalsIgnoreCase("NEW_CARD_REQ")) {
								int_xml = NEW_CARD_Custom(int_xml, recordFileMap);
							} else if (Call_name.equalsIgnoreCase("DECTECH")) {
								int_xml = DECTECH_Custom(int_xml, recordFileMap);
							} else if (Call_name.equalsIgnoreCase("CHEQUE_BOOK_ELIGIBILITY")) {
								int_xml = CHEQUE_BOOK_ELIGIBILITY_Custom(int_xml,recordFileMap);
							}

							else if (parent_tag != null && !parent_tag.equalsIgnoreCase("")) {
								SKLogger_CC.writeLog("inside 1st if","inside 1st if");
								if (int_xml.containsKey(parent_tag)) {
									SKLogger_CC.writeLog("inside 1st if","inside 2nd if");
									String xml_str = int_xml.get(parent_tag);
									SKLogger_CC.writeLog("inside 1st if","inside 2nd if xml string"+ xml_str);
									if (is_repetitive.equalsIgnoreCase("Y")&& int_xml.containsKey(tag_name)) {
										SKLogger_CC.writeLog("inside 1st if","inside 3rd if xml string");
										xml_str = int_xml.get(tag_name) + "</"+ tag_name + ">" + "<"+ tag_name + ">";
										SKLogger_CC.writeLog("CC_Common","value after adding "+ Call_name + ": "+ xml_str);
										SKLogger_CC.writeLog("inside 1st if","inside 3rd if xml string xml string"+ xml_str);
										int_xml.remove(tag_name);
										int_xml.put(tag_name, xml_str);
										SKLogger_CC.writeLog("inside 1st if","inside 3rd if xml string xml string int_xml");
									} else {
										SKLogger_CC.writeLog("inside else of parent tag","value after adding "+ Call_name+ ": "+ xml_str+ " form_control name: "+ form_control);
										SKLogger_CC.writeLog("","valuie of form control: "+ formObject.getNGValue(form_control));
										if (form_control.trim().equalsIgnoreCase("")&& default_val.trim().equalsIgnoreCase("")) {
											SKLogger_CC.writeLog("inside if added by me","inside");
											xml_str = xml_str + "<" + tag_name+ ">" + "</" + tag_name+ ">";
											SKLogger_CC.writeLog("added by xml", "xml_str"+ xml_str);
										} else if (!(formObject.getNGValue(form_control) == null|| formObject.getNGValue(form_control).trim().equalsIgnoreCase("") || formObject.getNGValue(form_control).equalsIgnoreCase("null"))) {
											SKLogger_CC.writeLog("inside else of parent tag 1","form_control_val"+ form_control_val);
											if (fin_call_name.toUpperCase().contains(callName.toUpperCase())) {
												form_control_val = formObject.getNGValue(form_control).toUpperCase();
											} else
												form_control_val = formObject.getNGValue(form_control);

											if (!data_format12.equalsIgnoreCase("text")) {
												String[] format_arr = data_format12.split(":");
												String format_name = format_arr[0];
												String format_type = format_arr[1];
												SKLogger_CC.writeLog("","format_name"+ format_name);
												SKLogger_CC.writeLog("","format_type"+ format_type);

												if (format_name.equalsIgnoreCase("date")) {
													DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
													DateFormat df_new = new SimpleDateFormat(format_type);

													try {
														startDate = df.parse(form_control_val);
														form_control_val = df_new.format(startDate);
														SKLogger_CC.writeLog("RLOSCommon#Create Input"," date conversion: final Output: "+ form_control_val+ " requested format: "+ format_type);

													} catch (ParseException e) {
														SKLogger_CC.writeLog("RLOSCommon#Create Input"," Error while format conversion: "+ e.getMessage());
														e.printStackTrace();
													} catch (Exception e) {
														SKLogger_CC.writeLog("RLOSCommon#Create Input"," Error while format conversion: "+ e.getMessage());
														e.printStackTrace();
													}
												} else if (format_name.equalsIgnoreCase("number")) {
													if (form_control_val.contains(",")) {
														form_control_val = form_control_val.replace(",","");
													}

												}
											}
											SKLogger_CC.writeLog("inside else of parent tag","form_control_val"+ form_control_val);
											xml_str = xml_str + "<" + tag_name+ ">" + form_control_val+ "</" + tag_name + ">";
											SKLogger_CC.writeLog("inside else of parent tag xml_str","xml_str" + xml_str);
										}

										else if (default_val == null || default_val.trim().equalsIgnoreCase("")) {
											SKLogger_CC.writeLog("#RLOS Common GenerateXML IF part","no value found for tag name: "+ tag_name);
										} else {
											SKLogger_CC.writeLog("#RLOS Common GenerateXML inside set default value","");
											form_control_val = default_val;

											SKLogger_CC.writeLog("#RLOS Common GenerateXML inside set default value","form_control_val"+ form_control_val);
											xml_str = xml_str + "<" + tag_name+ ">" + form_control_val+ "</" + tag_name + ">";
											SKLogger_CC.writeLog("#RLOS Common GenerateXML inside else of parent tag form_control_val xml_str1","xml_str" + xml_str);

										}

										if (tag_name.equalsIgnoreCase("DocumentRefNumber") && parent_tag.equalsIgnoreCase("Document") && form_control_val.trim().equalsIgnoreCase("")) {
											if (xml_str.contains("</Document>")) {
												xml_str = xml_str.substring(0,xml_str.lastIndexOf("</Document>"));
												int_xml.put(parent_tag,xml_str);
											} else
												int_xml.remove(parent_tag);
										} else if (tag_name.equalsIgnoreCase("DocRefNum") && parent_tag.equalsIgnoreCase("DocDetails") && form_control_val.equalsIgnoreCase("")) {
											if (xml_str.contains("</DocDetails>")) {
												xml_str = xml_str.substring(0,xml_str.lastIndexOf("</DocDetails>"));
												int_xml.put(parent_tag,xml_str);
											} else
												int_xml.remove(parent_tag);
										} else if (tag_name.equalsIgnoreCase("PhnLocalCode") && parent_tag.equalsIgnoreCase("PhnDetails") && form_control_val.equalsIgnoreCase("")) {
											if (xml_str.contains("</PhnDetails>")) {
												xml_str = xml_str.substring(0,xml_str.lastIndexOf("</PhnDetails>"));
												int_xml.put(parent_tag,xml_str);
											} else
												int_xml.remove(parent_tag);
										} else if (tag_name.equalsIgnoreCase("Email") && parent_tag.equalsIgnoreCase("EmlDet") && form_control_val.equalsIgnoreCase("")) {
											if (xml_str.contains("</EmlDet>")) {
												xml_str = xml_str.substring(0,xml_str.lastIndexOf("</EmlDet>"));
												int_xml.put(parent_tag,xml_str);
											} else
												int_xml.remove(parent_tag);
										} else if (tag_name.equalsIgnoreCase("DocNo") && parent_tag.equalsIgnoreCase("DocDet") && form_control_val.equalsIgnoreCase("")) {
											if (xml_str.contains("</DocDet>")) {
												xml_str = xml_str.substring(0,xml_str.lastIndexOf("</DocDet>"));
												int_xml.put(parent_tag,xml_str);
											} else
												int_xml.remove(parent_tag);
										} else {
											int_xml.put(parent_tag, xml_str);
										}
										SKLogger_CC.writeLog("else of generatexml","RLOSCommon" + "inside else"+ xml_str);
									}

								} else {
									String new_xml_str = "";
									SKLogger_CC.writeLog("inside else of parent tag main 2","value after adding " + Call_name+ ": " + new_xml_str+ " form_control name: "+ form_control);
									SKLogger_CC.writeLog("","valuie of form control: "+ formObject.getNGValue(form_control));
									if (!(formObject.getNGValue(form_control) == null|| formObject.getNGValue(form_control).trim().equalsIgnoreCase("") || formObject.getNGValue(form_control).equalsIgnoreCase("null"))) {
										SKLogger_CC.writeLog("inside else of parent tag 1","form_control_val"+ form_control_val);
										if (fin_call_name.toUpperCase().contains(callName.toUpperCase())) {
											form_control_val = formObject.getNGValue(form_control).toUpperCase();
										} else
											form_control_val = formObject.getNGValue(form_control);
										if (!data_format12.equalsIgnoreCase("text")) {
											String[] format_arr = data_format12.split(":");
											String format_name = format_arr[0];
											String format_type = format_arr[1];
											if (format_name.equalsIgnoreCase("date")) {
												DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
												DateFormat df_new = new SimpleDateFormat(format_type);
												// java.util.Date startDate;
												try {
													startDate = df.parse(form_control_val);
													form_control_val = df_new.format(startDate);
													SKLogger_CC.writeLog("RLOSCommon#Create Input",	" date conversion: final Output: "+ form_control_val+" requested format: "+ format_type);

												} catch (ParseException e) {
													SKLogger_CC.writeLog("RLOSCommon#Create Input",	" Error while format conversion: "+ e.getMessage());
													e.printStackTrace();
												} catch (Exception e) {
													SKLogger_CC.writeLog("RLOSCommon#Create Input",	" Error while format conversion: "+ e.getMessage());
													e.printStackTrace();
												}
											}
											else if (format_name.equalsIgnoreCase("number")) {
												if (form_control_val.contains(",")) {
													form_control_val = form_control_val.replace(",", "");
												}
											}
										}
										SKLogger_CC.writeLog("inside else of parent tag","form_control_val"+ form_control_val);
										new_xml_str = new_xml_str + "<"+ tag_name + ">"+ form_control_val + "</"+ tag_name + ">";
										SKLogger_CC.writeLog("inside else of parent tag xml_str","new_xml_str"+ new_xml_str);
									}

									else if (default_val == null
											|| default_val.trim().equalsIgnoreCase("")) {
										if (int_xml.containsKey(parent_tag) || is_repetitive.equalsIgnoreCase("Y")) {
											new_xml_str = new_xml_str + "<"+ tag_name + ">" + "</"+ tag_name + ">";
										}
										SKLogger_CC.writeLog("#RLOS Common GenerateXML Inside Else Part","no value found for tag name: "+ tag_name);
									} else {
										SKLogger_CC.writeLog("#RLOS Common GenerateXML inside set default value","");
										form_control_val = default_val;
										SKLogger_CC.writeLog("#RLOS Common GenerateXML inside set default value","form_control_val"+ form_control_val);
										new_xml_str = new_xml_str + "<"+ tag_name + ">"+ form_control_val + "</"+ tag_name + ">";
										SKLogger_CC.writeLog("#RLOS Common GenerateXML inside else of parent tag form_control_val xml_str1","xml_str" + new_xml_str);
									}
									int_xml.put(parent_tag, new_xml_str);
									SKLogger_CC.writeLog("else of generatexml","RLOSCommon" + "inside else"+ new_xml_str);
								}
							}

						}

						final_xml = final_xml.append("<").append(parentTagName).append(">");
						SKLogger_CC.writeLog("RLOS", "Final XMLold--"+ final_xml);

						Iterator<Map.Entry<String, String>> itr = int_xml.entrySet().iterator();
						SKLogger_CC.writeLog("itr of hashmap", "itr" + itr);
						while (itr.hasNext()) {
							Map.Entry<String, String> entry = itr.next();
							SKLogger_CC.writeLog("entry of hashmap", "entry"+ entry);
							if (final_xml.indexOf((entry.getKey())) > -1) {
								SKLogger_CC.writeLog("RLOS", "itr_value: Key: "
										+ entry.getKey() + " Value: "
										+ entry.getValue());
								final_xml = final_xml.insert(final_xml.indexOf("<" + entry.getKey() + ">")
										+ entry.getKey().length() + 2, entry.getValue());
								SKLogger_CC.writeLog("value of final xml","final_xml" + final_xml);
								itr.remove();
							}
						}
						final_xml = final_xml.append("</").append(parentTagName).append(">");
						SKLogger_CC.writeLog("CC_Common", "final_xml: "+ final_xml);
						SKLogger_CC.writeLog("FInal XMLnew is: ", final_xml.toString());
						final_xml.insert(0, header);
						final_xml.append(footer);
						SKLogger_CC.writeLog("FInal XMLnew with header: ",final_xml.toString());
						formObject.setNGValue("Is_" + callName, "Y");
						SKLogger_CC.writeLog("value of " + callName + " Flag: ", formObject.getNGValue("Is_" + callName));

						return MQ_connection_response(final_xml);
					}

				} else {
					SKLogger_CC.writeLog("Genrate XML: ","Entry is not maintained in field mapping Master table for : "+ callName);
					return "Call not maintained";
				}

			} else {
				SKLogger_CC.writeLog("Genrate XML: ","Entry is not maintained in Master table for : "+ callName);
				return "Call not maintained";
			}
		}

		catch (Exception e) {
			SKLogger_CC.writeLog("Exception ocurred: ", e.getLocalizedMessage());
			SKLogger_CC.writeLog("CC_Common", "$$final_xml: " + final_xml);
			SKLogger_CC.writeLog("CC_Common",
					"Exception occured in main thread: " + e.getMessage());
			return "0";
		}
		return "";

	}

	public String MQ_connection_response(StringBuffer finalXml) {
		
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		Socket socket = null;
		OutputStream out = null;
		InputStream socketInputStream = null;
		DataOutputStream dout = null;
		DataInputStream din = null;
		String mqOutputResponse = null;
		String mqInputRequest = null;
		String cabinetName = null;
		String wi_name = null;
		String ws_name = null;
		String sessionID = null;
		String userName = null;
		String socketServerIP;
		Integer socketServerPort;
		try {


			cabinetName = FormContext.getCurrentInstance().getFormConfig().getConfigElement("EngineName");
			SKLogger_CC.writeLog("$$outputgGridtXML ","cabinetName " + cabinetName);
			wi_name = FormContext.getCurrentInstance().getFormConfig().getConfigElement("ProcessInstanceId");
			ws_name = FormContext.getCurrentInstance().getFormConfig().getConfigElement("ActivityName");
			SKLogger_CC.writeLog("$$outputgGridtXML ","ActivityName " + ws_name);
			sessionID = FormContext.getCurrentInstance().getFormConfig().getConfigElement("DMSSessionId");
			userName = FormContext.getCurrentInstance().getFormConfig().getConfigElement("UserName");
			SKLogger_CC.writeLog("$$outputgGridtXML ", "userName "+ userName);
			SKLogger_CC.writeLog("$$outputgGridtXML ", "sessionID "+ sessionID);

			String sMQuery = "SELECT SocketServerIP,SocketServerPort FROM NG_RLOS_MQ_TABLE with (nolock)";
			List<List<String>> outputMQXML = formObject.getDataFromDataSource(sMQuery);
			SKLogger_CC.writeLog("$$outputgGridtXML ", "sMQuery " + sMQuery);
			if (!outputMQXML.isEmpty()) {
				SKLogger_CC.writeLog("$$outputgGridtXML ", outputMQXML.get(0).get(0) + "," + outputMQXML.get(0).get(1));
				socketServerIP = outputMQXML.get(0).get(0);
				SKLogger_CC.writeLog("$$outputgGridtXML ", "socketServerIP " + socketServerIP);
				socketServerPort = Integer.parseInt(outputMQXML.get(0).get(1));
				SKLogger_CC.writeLog("$$outputgGridtXML ", "socketServerPort " + socketServerPort);
				if (!(socketServerIP.equalsIgnoreCase("") && socketServerIP.equalsIgnoreCase(null) && socketServerPort.equals(null) && socketServerPort.equals(0))) {
					socket = new Socket(socketServerIP, socketServerPort);
					out = socket.getOutputStream();
					socketInputStream = socket.getInputStream();
					dout = new DataOutputStream(out);
					din = new DataInputStream(socketInputStream);
					mqOutputResponse = "";
					mqInputRequest = getMQInputXML(sessionID, cabinetName,wi_name, ws_name, userName, finalXml);
					SKLogger_CC.writeLog("$$outputgGridtXML ","mqInputRequest " + mqInputRequest);

					if (mqInputRequest != null && mqInputRequest.length() > 0) {int outPut_len = mqInputRequest.getBytes("UTF-16LE").length;SKLogger_CC.writeLog("Final XML output len: ",outPut_len + "");mqInputRequest = outPut_len + "##8##;" + mqInputRequest;SKLogger_CC.writeLog("MqInputRequest","Input Request Bytes : "+ mqInputRequest.getBytes("UTF-16LE"));dout.write(mqInputRequest.getBytes("UTF-16LE"));dout.flush();
					}
					byte[] readBuffer = new byte[50000];
					int num = din.read(readBuffer);
					boolean wait_flag = true;
					int out_len = 0;

					if (num > 0) {while (wait_flag) {	SKLogger_CC.writeLog("MqOutputRequest", "num :"+ num);	byte[] arrayBytes = new byte[num];	System.arraycopy(readBuffer, 0, arrayBytes, 0, num);	mqOutputResponse = mqOutputResponse+ new String(arrayBytes, "UTF-16LE");	SKLogger_CC.writeLog("MqOutputRequest","inside loop output Response :\n"+ mqOutputResponse);	if (mqOutputResponse.contains("##8##;")) {		String[] mqOutputResponse_arr = mqOutputResponse.split("##8##;");		mqOutputResponse = mqOutputResponse_arr[1];		out_len = Integer.parseInt(mqOutputResponse_arr[0]);		SKLogger_CC.writeLog("MqOutputRequest","First Output Response :\n"+ mqOutputResponse);		SKLogger_CC.writeLog("MqOutputRequest","Output length :\n" + out_len);	}	if (out_len <= mqOutputResponse.getBytes("UTF-16LE").length) {		wait_flag = false;	}	Thread.sleep(100);	num = din.read(readBuffer);
					}// Aman Code added for dectech to replace the &lt and// &gt start 13 sept 2017if (mqOutputResponse.contains("&lt;")) {	SKLogger_CC.writeLog("MqOutputRequest","inside for Dectech :\n"+ mqOutputResponse);	mqOutputResponse = mqOutputResponse.replaceAll("&lt;", "<");	SKLogger_CC.writeLog("MqOutputRequest","after replacing lt :\n"+ mqOutputResponse);	mqOutputResponse = mqOutputResponse.replaceAll("&gt;", ">");	SKLogger_CC.writeLog("MqOutputRequest","after replacing gt :\n"+ mqOutputResponse);}// Aman Code added for dectech to replace the &lt and// &gt end 13 sept 2017
					SKLogger_CC.writeLog("MqOutputRequest","Final Output Response :\n" + mqOutputResponse);socket.close();return mqOutputResponse;
					}

				} else {
					SKLogger_CC	.writeLog("SocketServerIp and SocketServerPort is not maintained ","");
					SKLogger_CC.writeLog("SocketServerIp is not maintained ",	socketServerIP);
					SKLogger_CC.writeLog(" SocketServerPort is not maintained ",	socketServerPort.toString());
					return "MQ details not maintained";
				}
			} else {
				SKLogger_CC.writeLog(		"SOcket details are not maintained in NG_RLOS_MQ_TABLE table","");
				return "MQ details not maintained";
			}
			return "";

		} catch (Exception e) {
			return "";
		}

	}

	public String InternalBureauIndividualProducts() {
		SKLogger_CC.writeLog("RLOSCommon java file","inside InternalBureauIndividualProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "SELECT cifid,agreementid,loantype,loantype,custroletype,loan_start_date,loanmaturitydate,lastupdatedate ,totaloutstandingamt,totalloanamount,NextInstallmentAmt,paymentmode,totalnoofinstalments,remaininginstalments,totalloanamount,      overdueamt,nofdayspmtdelay,monthsonbook,currentlycurrentflg,currmaxutil,DPD_30_in_last_6_months,DPD_60_in_last_18_months,propertyvalue,loan_disbursal_date,marketingcode,DPD_30_in_last_3_months,DPD_30_in_last_6_months,DPD_30_in_last_9_months,DPD_30_in_last_12_months,DPD_30_in_last_18_months,DPD_30_in_last_24_months,DPD_60_in_last_3_months,DPD_60_in_last_6_months,DPD_60_in_last_9_months,DPD_60_in_last_12_months,DPD_60_in_last_18_months,DPD_60_in_last_24_months,DPD_90_in_last_3_months,DPD_90_in_last_6_months,DPD_90_in_last_9_months,DPD_90_in_last_12_months,DPD_90_in_last_18_months,DPD_90_in_last_24_months,DPD_120_in_last_3_months,DPD_120_in_last_6_months,DPD_120_in_last_9_months,DPD_120_in_last_12_months,DPD_120_in_last_18_months,DPD_120_in_last_24_months,DPD_150_in_last_3_months,DPD_150_in_last_6_months,DPD_150_in_last_9_months,DPD_150_in_last_12_months,DPD_150_in_last_18_months,DPD_150_in_last_24_months,DPD_180_in_last_3_months,DPD_180_in_last_6_months,DPD_180_in_last_9_months,DPD_180_in_last_12_months,DPD_180_in_last_24_months,'' as col1,Consider_For_Obligations,LoanStat,'LOANS',writeoffStat,writeoffstatdt,lastrepmtdt,limit_increase,PartSettlementDetails,'' as SchemeCardProduct,General_Status,Internal_WriteOff_Check FROM ng_RLOS_CUSTEXPOSE_LoanDetails WHERE Child_wi = '"
			+ formObject.getWFWorkitemName()
			+ "' and LoanStat !='Pipeline' union select CifId,CardEmbossNum,CardType,CardType,CustRoleType,'' as col6,'' as col7,'' as col8,OutstandingAmt,CreditLimit,PaymentsAmount,PaymentMode,'' as col13,'' as col14,CashLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,'' as col19,CurrMaxUtil,DPD_30_in_last_6_months,DPD_60_in_last_18_months,'' as col23,'' as col24,'' as col25,DPD_30_in_last_3_months,DPD_30_in_last_6_months,DPD_30_in_last_9_months,DPD_30_in_last_12_months,DPD_30_in_last_18_months,DPD_30_in_last_24_months,DPD_60_in_last_3_months,DPD_60_in_last_6_months,DPD_60_in_last_9_months,DPD_60_in_last_12_months,DPD_60_in_last_18_months,DPD_60_in_last_24_months,DPD_90_in_last_3_months,DPD_90_in_last_6_months,DPD_90_in_last_9_months,DPD_90_in_last_12_months,DPD_90_in_last_18_months,DPD_90_in_last_24_months,DPD_120_in_last_3_months,DPD_120_in_last_6_months,DPD_120_in_last_9_months,DPD_120_in_last_12_months,DPD_120_in_last_18_months,DPD_120_in_last_24_months,DPD_150_in_last_3_months,DPD_150_in_last_6_months,DPD_150_in_last_9_months,DPD_150_in_last_12_months,DPD_150_in_last_18_months,DPD_150_in_last_24_months,DPD_180_in_last_3_months,DPD_180_in_last_6_months,DPD_180_in_last_9_months,DPD_180_in_last_12_months,DPD_180_in_last_24_months,ExpiryDate,Consider_For_Obligations,CardStatus,'CARDS',writeoffStat,writeoffstatdt,'' as lastrepdate,limit_increase,'' as PartSettlementDetails,SchemeCardProd,General_Status,Internal_WriteOff_Check FROM ng_RLOS_CUSTEXPOSE_CardDetails where      Child_wi = '"
			+ formObject.getWFWorkitemName()
			+ "' and Request_Type In ('InternalExposure','CollectionsSummary')";

		SKLogger_CC.writeLog("InternalBureauIndividualProducts sQuery" + sQuery, "");
		String CountQuery = "select count(*) from ng_RLOS_CUSTEXPOSE_CardDetails where Child_wi = '"+ formObject.getWFWorkitemName() + "' and cardStatus='A'";
		List<List<String>> CountXML = formObject.getDataFromDataSource(CountQuery);
		String add_xml_str = "";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		SKLogger_CC.writeLog("InternalBureauIndividualProducts list size"+ OutputXML.size(), "");
		SKLogger_CC.writeLog("InternalBureauIndividualProducts list "+ OutputXML, "");
		String ReqProd = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 1);
		String mol_sal_var = formObject.getNGValue("cmplx_MOL_molsalvar");

		try {
			for (int i = 0; i < OutputXML.size(); i++) {

				String cifId = "";
				String agreementId = "";
				String product_type = "";
				String loan_start_date = "";
				String loanmaturitydate = "";
				String lastupdatedate = "";

				String outstandingamt = "";
				String totalloanamount = "";
				String Emi = "";
				String paymentmode = "";
				String totalnoofinstalments = "";
				String remaininginstalments = "";
				String overdueamt = "";

				String nofdayspmtdelay = "";
				String monthsonbook = "";
				String currentlycurrent = "";
				String currmaxutil = "";
				String DPD_30_in_last_6_months = "";
				String DPD_60_in_last_18_months = "";
				String propertyvalue = "";

				String loan_disbursal_date = "";
				String marketingcode = "";
				String DPD_30_in_last_3_months = "";
				String DPD_30_in_last_9_months = "";
				String DPD_30_in_last_12_months = "";
				String DPD_30_in_last_18_months = "";
				String DPD_30_in_last_24_months = "";

				String DPD_60_in_last_3_months = "";
				String DPD_60_in_last_6_months = "";
				String DPD_60_in_last_9_months = "";
				String DPD_60_in_last_12_months = "";
				String DPD_60_in_last_24_months = "";
				String DPD_90_in_last_3_months = "";
				String DPD_90_in_last_6_months = "";
				String DPD_90_in_last_9_months = "";
				String DPD_90_in_last_12_months = "";
				String DPD_90_in_last_18_months = "";
				String DPD_90_in_last_24_months = "";
				String DPD_120_in_last_3_months = "";
				String DPD_120_in_last_6_months = "";
				String DPD_120_in_last_9_months = "";
				String DPD_120_in_last_12_months = "";
				String DPD_120_in_last_18_months = "";
				String DPD_120_in_last_24_months = "";
				String DPD_150_in_last_3_months = "";
				String DPD_150_in_last_6_months = "";
				String DPD_150_in_last_9_months = "";
				String DPD_150_in_last_12_months = "";
				String DPD_150_in_last_18_months = "";
				String DPD_150_in_last_24_months = "";
				String DPD_180_in_last_3_months = "";
				String DPD_180_in_last_6_months = "";
				String DPD_180_in_last_9_months = "";
				String DPD_180_in_last_12_months = "";
				String DPD_180_in_last_24_months = "";
				String CardExpiryDate = "";
				String Consider_For_Obligations = "";
				String phase = "";
				String writeoffStat = "";
				String writeoffstatdt = "";
				String lastrepmtdt = "";
				String Limit_increase = "";
				String part_settlement_date = "";
				String part_settlement_amount = "";
				String SchemeCardProduct = "";
				String General_Status = "";
				String Combined_Limit = "";
				String SecuredCard = "";
				String Internal_WriteOff_Check = "";
				String EmployerType = formObject.getNGValue("EMploymentDetails_Combo5");
				String Kompass = formObject.getNGValue("cmplx_EmploymentDetails_Kompass");
				SKLogger_CC.writeLog("Inside for", "asdasdasd");
				String paid_installment = "";
				if (!(OutputXML.get(i).get(0) == null || OutputXML.get(i).get(0).equals(""))) {
					cifId = OutputXML.get(i).get(0).toString();
				}
				if (!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals(""))) {
					agreementId = OutputXML.get(i).get(1).toString();
				}
				if (!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2).equals(""))) {
					product_type = OutputXML.get(i).get(2).toString();
					if (product_type.equalsIgnoreCase("Home In One")) {product_type = "HIO";
					} else {product_type = OutputXML.get(i).get(63).toString();
					}
				}
				if (!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals(""))) {
				}
				if (!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals(""))) {
				}
				if (!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5).equals(""))) {
					loan_start_date = OutputXML.get(i).get(5).toString();
				}
				if (OutputXML.get(i).get(6) != null && !OutputXML.get(i).get(6).isEmpty() && !OutputXML.get(i).get(6).equals("") && !OutputXML.get(i).get(6).equalsIgnoreCase("null")) {
					loanmaturitydate = OutputXML.get(i).get(6).toString();
				}
				if (OutputXML.get(i).get(7) != null && !OutputXML.get(i).get(7).isEmpty() && !OutputXML.get(i).get(7).equals("") && !OutputXML.get(i).get(7).equalsIgnoreCase("null")) {
					lastupdatedate = OutputXML.get(i).get(7).toString();
				}
				if (OutputXML.get(i).get(8) != null && !OutputXML.get(i).get(8).isEmpty() && !OutputXML.get(i).get(8).equals("") && !OutputXML.get(i).get(8).equalsIgnoreCase("null")) {
					outstandingamt = OutputXML.get(i).get(8).toString();
				}
				if (!(OutputXML.get(i).get(9) == null || OutputXML.get(i).get(9).equals(""))) {
					totalloanamount = OutputXML.get(i).get(9).toString();
				}
				if (!(OutputXML.get(i).get(10) == null || OutputXML.get(i).get(10).equals(""))) {
					Emi = OutputXML.get(i).get(10).toString();
				}
				if (!(OutputXML.get(i).get(11) == null || OutputXML.get(i).get(11).equals(""))) {
					paymentmode = OutputXML.get(i).get(11).toString();
				}
				if (OutputXML.get(i).get(12) != null && !OutputXML.get(i).get(12).isEmpty() && !OutputXML.get(i).get(12).equals("") && !OutputXML.get(i).get(12).equalsIgnoreCase("null")) {
					// SKLogger.writeLog("Inside for",
					// "asdasdasd"+OutputXML.get(i).get(12));
					totalnoofinstalments = OutputXML.get(i).get(12).toString();
				}
				if (!(OutputXML.get(i).get(13) == null || OutputXML.get(i).get(13).equals(""))) {
					remaininginstalments = OutputXML.get(i).get(13).toString();
				}
				if (!(OutputXML.get(i).get(14) == null || OutputXML.get(i).get(14).equals(""))) {
				}
				if (!(OutputXML.get(i).get(15) == null || OutputXML.get(i).get(15).equals(""))) {
					overdueamt = OutputXML.get(i).get(15).toString();
				}
				if (!(OutputXML.get(i).get(16) == null || OutputXML.get(i).get(16).equals(""))) {
					nofdayspmtdelay = OutputXML.get(i).get(16).toString();
				}
				if (!(OutputXML.get(i).get(17) == null || OutputXML.get(i).get(17).equals(""))) {
					monthsonbook = OutputXML.get(i).get(17).toString();
				}
				if (!(OutputXML.get(i).get(18) == null || OutputXML.get(i).get(18).equals(""))) {
					currentlycurrent = OutputXML.get(i).get(18).toString();
				}
				if (!(OutputXML.get(i).get(19) == null || OutputXML.get(i).get(19).equals(""))) {
					currmaxutil = OutputXML.get(i).get(19).toString();
				}
				if (!(OutputXML.get(i).get(20) == null || OutputXML.get(i).get(20).equals(""))) {
					DPD_30_in_last_6_months = OutputXML.get(i).get(20)	.toString();
				}
				if (!(OutputXML.get(i).get(21) == null || OutputXML.get(i).get(21).equals(""))) {
					DPD_60_in_last_18_months = OutputXML.get(i).get(21)	.toString();
				}
				if (!(OutputXML.get(i).get(22) == null || OutputXML.get(i).get(22).equals(""))) {
					propertyvalue = OutputXML.get(i).get(22).toString();
				}
				if (!(OutputXML.get(i).get(23) == null || OutputXML.get(i).get(23).equals(""))) {
					loan_disbursal_date = OutputXML.get(i).get(23).toString();
				}
				if (!(OutputXML.get(i).get(24) == null || OutputXML.get(i).get(24).equals(""))) {
					marketingcode = OutputXML.get(i).get(24).toString();
				}
				if (!(OutputXML.get(i).get(25) == null || OutputXML.get(i).get(25).equals(""))) {
					DPD_30_in_last_3_months = OutputXML.get(i).get(25)	.toString();
				}
				if (!(OutputXML.get(i).get(26) == null || OutputXML.get(i).get(26).equals(""))) {
					DPD_30_in_last_9_months = OutputXML.get(i).get(26)	.toString();
				}
				if (!(OutputXML.get(i).get(27) == null || OutputXML.get(i).get(27).equals(""))) {
					DPD_30_in_last_12_months = OutputXML.get(i).get(27)	.toString();
				}
				if (!(OutputXML.get(i).get(28) == null || OutputXML.get(i).get(28).equals(""))) {
					DPD_30_in_last_18_months = OutputXML.get(i).get(28)	.toString();
				}
				if (!(OutputXML.get(i).get(29) == null || OutputXML.get(i).get(29).equals(""))) {
					DPD_30_in_last_24_months = OutputXML.get(i).get(29)	.toString();
				}
				if (!(OutputXML.get(i).get(30) == null || OutputXML.get(i).get(30).equals(""))) {
					DPD_60_in_last_3_months = OutputXML.get(i).get(30)	.toString();
				}
				if (!(OutputXML.get(i).get(31) == null || OutputXML.get(i).get(31).equals(""))) {
					DPD_60_in_last_6_months = OutputXML.get(i).get(31)	.toString();
				}
				if (!(OutputXML.get(i).get(32) == null || OutputXML.get(i).get(32).equals(""))) {
					DPD_60_in_last_9_months = OutputXML.get(i).get(32)	.toString();
				}
				if (!(OutputXML.get(i).get(33) == null || OutputXML.get(i).get(33).equals(""))) {
					DPD_60_in_last_12_months = OutputXML.get(i).get(33)	.toString();
				}
				if (!(OutputXML.get(i).get(34) == null || OutputXML.get(i).get(34).equals(""))) {
					DPD_60_in_last_24_months = OutputXML.get(i).get(34)	.toString();
				}
				if (!(OutputXML.get(i).get(35) == null || OutputXML.get(i).get(35).equals(""))) {
					DPD_90_in_last_3_months = OutputXML.get(i).get(35)	.toString();
				}
				if (!(OutputXML.get(i).get(36) == null || OutputXML.get(i).get(36).equals(""))) {
					DPD_90_in_last_6_months = OutputXML.get(i).get(36)	.toString();
				}
				if (!(OutputXML.get(i).get(37) == null || OutputXML.get(i).get(37).equals(""))) {
					DPD_90_in_last_9_months = OutputXML.get(i).get(37)	.toString();
				}
				if (!(OutputXML.get(i).get(38) == null || OutputXML.get(i).get(38).equals(""))) {
					DPD_90_in_last_12_months = OutputXML.get(i).get(38)	.toString();
				}
				if (!(OutputXML.get(i).get(39) == null || OutputXML.get(i).get(39).equals(""))) {
					DPD_90_in_last_18_months = OutputXML.get(i).get(39)	.toString();
				}
				if (!(OutputXML.get(i).get(40) == null || OutputXML.get(i).get(40).equals(""))) {
					DPD_90_in_last_24_months = OutputXML.get(i).get(40)	.toString();
				}
				if (!(OutputXML.get(i).get(41) == null || OutputXML.get(i).get(41).equals(""))) {
					DPD_120_in_last_3_months = OutputXML.get(i).get(41)	.toString();
				}
				if (!(OutputXML.get(i).get(42) == null || OutputXML.get(i).get(42).equals(""))) {
					DPD_120_in_last_6_months = OutputXML.get(i).get(42)	.toString();
				}
				if (!(OutputXML.get(i).get(43) == null || OutputXML.get(i).get(43).equals(""))) {
					DPD_120_in_last_9_months = OutputXML.get(i).get(43)	.toString();
				}
				if (!(OutputXML.get(i).get(44) == null || OutputXML.get(i).get(44).equals(""))) {
					DPD_120_in_last_12_months = OutputXML.get(i).get(44)	.toString();
				}
				if (!(OutputXML.get(i).get(45) == null || OutputXML.get(i).get(45).equals(""))) {
					DPD_120_in_last_18_months = OutputXML.get(i).get(45)	.toString();
				}
				if (!(OutputXML.get(i).get(46) == null || OutputXML.get(i).get(46).equals(""))) {
					DPD_120_in_last_24_months = OutputXML.get(i).get(46)	.toString();
				}
				if (!(OutputXML.get(i).get(47) == null || OutputXML.get(i).get(47).equals(""))) {
					DPD_150_in_last_3_months = OutputXML.get(i).get(47)	.toString();
				}
				if (!(OutputXML.get(i).get(48) == null || OutputXML.get(i).get(48).equals(""))) {
					DPD_150_in_last_6_months = OutputXML.get(i).get(48)	.toString();
				}
				if (!(OutputXML.get(i).get(49) == null || OutputXML.get(i).get(49).equals(""))) {
					DPD_150_in_last_9_months = OutputXML.get(i).get(49)	.toString();
				}
				if (!(OutputXML.get(i).get(50) == null || OutputXML.get(i).get(50).equals(""))) {
					DPD_150_in_last_12_months = OutputXML.get(i).get(50)	.toString();
				}
				if (!(OutputXML.get(i).get(51) == null || OutputXML.get(i).get(51).equals(""))) {
					DPD_150_in_last_18_months = OutputXML.get(i).get(51)	.toString();
				}
				if (!(OutputXML.get(i).get(52) == null || OutputXML.get(i).get(52).equals(""))) {
					DPD_150_in_last_24_months = OutputXML.get(i).get(52)	.toString();
				}
				if (!(OutputXML.get(i).get(53) == null || OutputXML.get(i).get(53).equals(""))) {
					DPD_180_in_last_3_months = OutputXML.get(i).get(53)	.toString();
				}
				if (!(OutputXML.get(i).get(54) == null || OutputXML.get(i).get(54).equals(""))) {
					DPD_180_in_last_6_months = OutputXML.get(i).get(54)	.toString();
				}
				if (!(OutputXML.get(i).get(55) == null || OutputXML.get(i).get(55).equals(""))) {
					DPD_180_in_last_9_months = OutputXML.get(i).get(55)	.toString();
				}
				if (!(OutputXML.get(i).get(56) == null || OutputXML.get(i).get(56).equals(""))) {
					DPD_180_in_last_12_months = OutputXML.get(i).get(56)	.toString();
				}
				if (!(OutputXML.get(i).get(57) == null || OutputXML.get(i).get(57).equals(""))) {
					DPD_180_in_last_24_months = OutputXML.get(i).get(57)	.toString();
				}
				if (!(OutputXML.get(i).get(60) == null || OutputXML.get(i).get(60).equals(""))) {
					CardExpiryDate = OutputXML.get(i).get(60).toString();
				}

				if (!(OutputXML.get(i).get(61) == null || OutputXML.get(i).get(61).equals(""))) {
					Consider_For_Obligations = OutputXML.get(i).get(61)	.toString();
					if (Consider_For_Obligations.equalsIgnoreCase("false")) {Consider_For_Obligations = "Y";
					} else {Consider_For_Obligations = "N";
					}
				}

				if (!(OutputXML.get(i).get(62) == null || OutputXML.get(i).get(62).equals(""))) {
					phase = OutputXML.get(i).get(62).toString();
					if (phase.startsWith("C")) {phase = "C";
					} else {phase = "A";
					}
				}
				if (!(OutputXML.get(i).get(64) == null || OutputXML.get(i).get(64).equals(""))) {
					writeoffStat = OutputXML.get(i).get(64).toString();
				}
				if (!(OutputXML.get(i).get(65) == null || OutputXML.get(i).get(65).equals(""))) {
					writeoffstatdt = OutputXML.get(i).get(65).toString();
				}
				if (!(OutputXML.get(i).get(66) == null || OutputXML.get(i).get(66).equals(""))) {
					lastrepmtdt = OutputXML.get(i).get(66).toString();
				}
				if (!(OutputXML.get(i).get(67) == null || OutputXML.get(i).get(67).equals(""))) {
					Limit_increase = OutputXML.get(i).get(67).toString();
					if (Limit_increase.equalsIgnoreCase("false")) {Limit_increase = "N";
					} else {Limit_increase = "Y";
					}
				}
				if (!(OutputXML.get(i).get(68) == null || OutputXML.get(i).get(68).equals(""))) {
					// commented by aman 22-10-2017 to handdle part settelment
					// data
					// part_settlement_date =
					// OutputXML.get(i).get(67).toString();
					String abc = OutputXML.get(i).get(68).toString();
					abc = abc.substring(0, 10) + "split"+ abc.substring(10, abc.length());
					String abcsa[] = abc.split("split");
					part_settlement_date = abcsa[0];
					part_settlement_amount = abcsa[1];
				}
				if (!(OutputXML.get(i).get(69) == null || OutputXML.get(i).get(69).equals(""))) {
					SchemeCardProduct = OutputXML.get(i).get(69).toString();
				}
				if (!(OutputXML.get(i).get(70) == null || OutputXML.get(i).get(70).equals(""))) {
					General_Status = OutputXML.get(i).get(70).toString();
				}
				if (!(OutputXML.get(i).get(71) == null || OutputXML.get(i).get(71).equals(""))) {
					Internal_WriteOff_Check = OutputXML.get(i).get(71)	.toString();
				}
				String sQueryCombinedLimit = "select Distinct(COMBINEDLIMIT_ELIGIBILITY) from ng_master_cardProduct where code='" + SchemeCardProduct + "'";
				List<List<String>> sQueryCombinedLimitXML = formObject.getDataFromDataSource(sQueryCombinedLimit);
				if (sQueryCombinedLimitXML != null) {
					Combined_Limit = sQueryCombinedLimitXML.get(0).get(0)	.equalsIgnoreCase("1") ? "Y" : "N";
				}
				String sQuerySecuredCard = "select count(*) from ng_master_cardProduct where code='" + SchemeCardProduct + "'  and subproduct='SEC'";
				List<List<String>> sQuerySecuredCardXML = formObject.getDataFromDataSource(sQuerySecuredCard);
				if (sQuerySecuredCardXML != null) {
					SecuredCard = sQuerySecuredCardXML.get(0).get(0)	.equalsIgnoreCase("0") ? "N" : "Y";
				}

				if (cifId != null && !cifId.equalsIgnoreCase("") && !cifId.equalsIgnoreCase("null")) {
					SKLogger_CC.writeLog("Inside if", "asdasdasd");
					add_xml_str = add_xml_str+ "<InternalBureauIndividualProducts><applicant_id>"+ cifId + "</applicant_id>";
					add_xml_str = add_xml_str+ "<internal_bureau_individual_products_id>"+ agreementId+ "</internal_bureau_individual_products_id>";
					add_xml_str = add_xml_str + "<type_product>" + product_type+ "</type_product>";
					if (OutputXML.get(i).get(63).equalsIgnoreCase("cards")) {
						add_xml_str = add_xml_str+ "<contract_type>CC</contract_type>";
					}
					if (OutputXML.get(i).get(63).equalsIgnoreCase("Loans")) {
						add_xml_str = add_xml_str+ "<contract_type>PL</contract_type>";
					}
					add_xml_str = add_xml_str + "<provider_no>" + "RAKBANK"+ "</provider_no>";
					add_xml_str = add_xml_str + "<phase>" + phase + "</phase>";
					add_xml_str = add_xml_str+ "<role_of_customer>Primary</role_of_customer>";

					add_xml_str = add_xml_str + "<start_date>"+ loan_start_date + "</start_date>";
					add_xml_str = add_xml_str + "<close_date>"+ loanmaturitydate + "</close_date>";
					add_xml_str = add_xml_str + "<date_last_updated>"+ lastupdatedate + "</date_last_updated>";
					add_xml_str = add_xml_str + "<outstanding_balance>"+ outstandingamt + "</outstanding_balance>";
					if (OutputXML.get(i).get(63).equalsIgnoreCase("Loans")) {
						add_xml_str = add_xml_str + "<total_amount>"+ totalloanamount + "</total_amount>";
					}
					add_xml_str = add_xml_str + "<payments_amount>" + Emi+ "</payments_amount>";
					add_xml_str = add_xml_str + "<method_of_payment>"+ paymentmode + "</method_of_payment>";
					add_xml_str = add_xml_str + "<total_no_of_instalments>"+ totalnoofinstalments+ "</total_no_of_instalments>";
					add_xml_str = add_xml_str + "<no_of_remaining_instalments>"+ remaininginstalments+ "</no_of_remaining_instalments>";
					add_xml_str = add_xml_str + "<worst_status>" + writeoffStat+ "</worst_status>";
					add_xml_str = add_xml_str + "<worst_status_date>"+ writeoffstatdt + "</worst_status_date>";
					if (OutputXML.get(i).get(63).equalsIgnoreCase("cards")) {
						add_xml_str = add_xml_str + "<credit_limit>"+ totalloanamount + "</credit_limit>";
					}
					add_xml_str = add_xml_str + "<overdue_amount>" + overdueamt+ "</overdue_amount>";
					add_xml_str = add_xml_str + "<no_of_days_payment_delay>"+ nofdayspmtdelay + "</no_of_days_payment_delay>";
					add_xml_str = add_xml_str + "<mob>" + monthsonbook+ "</mob>";
					add_xml_str = add_xml_str + "<last_repayment_date>"+ lastrepmtdt + "</last_repayment_date>";
					add_xml_str = add_xml_str + "<currently_current>"+ currentlycurrent + "</currently_current>";
					add_xml_str = add_xml_str + "<current_utilization>"+ currmaxutil + "</current_utilization>";
					add_xml_str = add_xml_str + "<dpd_30_last_6_mon>"+ DPD_30_in_last_6_months + "</dpd_30_last_6_mon>";
					add_xml_str = add_xml_str + "<dpd_60p_in_last_18_mon>"+ DPD_60_in_last_18_months+ "</dpd_60p_in_last_18_mon>";

					add_xml_str = add_xml_str + "<card_product>"+ SchemeCardProduct + "</card_product>";
					add_xml_str = add_xml_str + "<property_value>"+ propertyvalue + "</property_value>";
					add_xml_str = add_xml_str + "<disbursal_date>"+ loan_disbursal_date + "</disbursal_date>";
					add_xml_str = add_xml_str + "<marketing_code>"+ marketingcode + "</marketing_code>";
					add_xml_str = add_xml_str + "<card_expiry_date>"+ CardExpiryDate + "</card_expiry_date>";
					add_xml_str = add_xml_str + "<card_upgrade_indicator>"+ Limit_increase + "</card_upgrade_indicator>";
					add_xml_str = add_xml_str + "<part_settlement_date>"+ part_settlement_date + "</part_settlement_date>";
					add_xml_str = add_xml_str + "<part_settlement_amount>"+ part_settlement_amount+ "</part_settlement_amount>";
					add_xml_str = add_xml_str + "<part_settlement_reason>" + ""+ "</part_settlement_reason>";
					add_xml_str = add_xml_str + "<limit_expiry_date>" + ""+ "</limit_expiry_date>";
					add_xml_str = add_xml_str + "<no_of_primary_cards>"+ CountXML.get(0).get(0) + "</no_of_primary_cards>";
					add_xml_str = add_xml_str + "<no_of_repayments_done>"+ remaininginstalments + "</no_of_repayments_done>";
					add_xml_str = add_xml_str + "<card_segment>"+ SchemeCardProduct + "</card_segment>";
					add_xml_str = add_xml_str + "<product_type>"+ OutputXML.get(i).get(63) + "</product_type>";
					add_xml_str = add_xml_str + "<product_category>"+ SchemeCardProduct + "</product_category>";
					add_xml_str = add_xml_str + "<combined_limit_flag>"+ Combined_Limit + "</combined_limit_flag>";
					add_xml_str = add_xml_str + "<secured_card_flag>"+ SecuredCard + "</secured_card_flag>";
					add_xml_str = add_xml_str + "<resch_tko_flag>"+ Limit_increase + "</resch_tko_flag>";

					add_xml_str = add_xml_str + "<general_status>"+ General_Status + "</general_status>";
					add_xml_str = add_xml_str + "<consider_for_obligation>"+ Consider_For_Obligations+ "</consider_for_obligation>";
					add_xml_str = add_xml_str + "<limit_increase>"+ Limit_increase + "</limit_increase>";

					add_xml_str = add_xml_str + "<role>Primary</role>";
					add_xml_str = add_xml_str + "<limit>" + "" + "</limit>";
					add_xml_str = add_xml_str + "<status>" + phase+ "</status>";
					add_xml_str = add_xml_str + "<emi>" + Emi + "</emi>";
					add_xml_str = add_xml_str + "<os_amt>" + outstandingamt+ "</os_amt>";

					add_xml_str = add_xml_str + "<dpd_30_in_last_3mon>"+ DPD_30_in_last_3_months+ "</dpd_30_in_last_3mon>";
					add_xml_str = add_xml_str + "<dpd_30_in_last_6mon>"+ DPD_30_in_last_6_months+ "</dpd_30_in_last_6mon>";
					add_xml_str = add_xml_str + "<dpd_30_in_last_9mon>"+ DPD_30_in_last_9_months+ "</dpd_30_in_last_9mon>";
					add_xml_str = add_xml_str + "<dpd_30_in_last_12mon>"+ DPD_30_in_last_12_months+ "</dpd_30_in_last_12mon>";

					add_xml_str = add_xml_str + "<dpd_30_in_last_18mon>"+ DPD_30_in_last_18_months+ "</dpd_30_in_last_18mon>";
					add_xml_str = add_xml_str + "<dpd_30_in_last_24mon>"+ DPD_30_in_last_24_months+ "</dpd_30_in_last_24mon>";
					add_xml_str = add_xml_str + "<dpd_60_in_last_3mon>"+ DPD_60_in_last_3_months+ "</dpd_60_in_last_3mon>";
					add_xml_str = add_xml_str + "<dpd_60_in_last_6mon>"+ DPD_60_in_last_6_months+ "</dpd_60_in_last_6mon>";
					add_xml_str = add_xml_str + "<dpd_60_in_last_9mon>"+ DPD_60_in_last_9_months+ "</dpd_60_in_last_9mon>";
					add_xml_str = add_xml_str + "<dpd_60_in_last_12mon>"+ DPD_60_in_last_12_months+ "</dpd_60_in_last_12mon>";
					add_xml_str = add_xml_str + "<dpd_60_in_last_18mon>"+ DPD_60_in_last_18_months+ "</dpd_60_in_last_18mon>";
					add_xml_str = add_xml_str + "<dpd_60_in_last_24mon>"+ DPD_60_in_last_24_months+ "</dpd_60_in_last_24mon>";
					add_xml_str = add_xml_str + "<dpd_90_in_last_3mon>"+ DPD_90_in_last_3_months+ "</dpd_90_in_last_3mon>";
					add_xml_str = add_xml_str + "<dpd_90_in_last_6mon>"+ DPD_90_in_last_6_months+ "</dpd_90_in_last_6mon>";
					add_xml_str = add_xml_str + "<dpd_90_in_last_9mon>"+ DPD_90_in_last_9_months+ "</dpd_90_in_last_9mon>";
					add_xml_str = add_xml_str + "<dpd_90_in_last_12mon>"+ DPD_90_in_last_12_months+ "</dpd_90_in_last_12mon>";
					add_xml_str = add_xml_str + "<dpd_90_in_last_18mon>"+ DPD_90_in_last_18_months+ "</dpd_90_in_last_18mon>";
					add_xml_str = add_xml_str + "<dpd_90_in_last_24mon>"+ DPD_90_in_last_24_months+ "</dpd_90_in_last_24mon>";
					add_xml_str = add_xml_str + "<dpd_120_in_last_3mon>"+ DPD_120_in_last_3_months+ "</dpd_120_in_last_3mon>";
					add_xml_str = add_xml_str + "<dpd_120_in_last_6mon>"+ DPD_120_in_last_6_months+ "</dpd_120_in_last_6mon>";
					add_xml_str = add_xml_str + "<dpd_120_in_last_9mon>"+ DPD_120_in_last_9_months+ "</dpd_120_in_last_9mon>";
					add_xml_str = add_xml_str + "<dpd_120_in_last_12mon>"+ DPD_120_in_last_12_months+ "</dpd_120_in_last_12mon>";
					add_xml_str = add_xml_str + "<dpd_120_in_last_18mon>"+ DPD_120_in_last_18_months+ "</dpd_120_in_last_18mon>";
					add_xml_str = add_xml_str + "<dpd_120_in_last_24mon>"+ DPD_120_in_last_24_months+ "</dpd_120_in_last_24mon>";

					add_xml_str = add_xml_str + "<dpd_150_in_last_3mon>"+ DPD_150_in_last_3_months+ "</dpd_150_in_last_3mon>";
					add_xml_str = add_xml_str + "<dpd_150_in_last_6mon>"+ DPD_150_in_last_6_months+ "</dpd_150_in_last_6mon>";
					add_xml_str = add_xml_str + "<dpd_150_in_last_9mon>"+ DPD_150_in_last_9_months+ "</dpd_150_in_last_9mon>";
					add_xml_str = add_xml_str + "<dpd_150_in_last_12mon>"+ DPD_150_in_last_12_months+ "</dpd_150_in_last_12mon>";
					add_xml_str = add_xml_str + "<dpd_150_in_last_18mon>"+ DPD_150_in_last_18_months+ "</dpd_150_in_last_18mon>";
					add_xml_str = add_xml_str + "<dpd_150_in_last_24mon>"+ DPD_150_in_last_24_months+ "</dpd_150_in_last_24mon>";
					add_xml_str = add_xml_str + "<dpd_180_in_last_3mon>"+ DPD_180_in_last_3_months+ "</dpd_180_in_last_3mon>";
					add_xml_str = add_xml_str + "<dpd_180_in_last_6mon>"+ DPD_180_in_last_6_months+ "</dpd_180_in_last_6mon>";
					add_xml_str = add_xml_str + "<dpd_180_in_last_9mon>"+ DPD_180_in_last_9_months+ "</dpd_180_in_last_9mon>";
					add_xml_str = add_xml_str + "<dpd_180_in_last_12mon>"+ DPD_180_in_last_12_months+ "</dpd_180_in_last_12mon>";
					add_xml_str = add_xml_str + "<dpd_180_in_last_18mon>" + ""+ "</dpd_180_in_last_18mon>";
					add_xml_str = add_xml_str + "<dpd_180_in_last_24mon>"+ DPD_180_in_last_24_months+ "</dpd_180_in_last_24mon>";
					add_xml_str = add_xml_str + "<last_temp_limit_exp>" + ""+ "</last_temp_limit_exp>";
					add_xml_str = add_xml_str + "<last_per_limit_exp>" + ""+ "</last_per_limit_exp>";
					add_xml_str = add_xml_str + "<security_cheque_amt>" + ""+ "</security_cheque_amt>";
					add_xml_str = add_xml_str + "<mol_salary_variance>"+ mol_sal_var + "</mol_salary_variance>";
					if (Kompass != null) {if (Kompass.equalsIgnoreCase("true")) {	add_xml_str = add_xml_str + "<kompass>" + "Y"+ "</kompass>";} else {	add_xml_str = add_xml_str + "<kompass>" + "N"+ "</kompass>";}
					}
					add_xml_str = add_xml_str + "<employer_type>"+ EmployerType + "</employer_type>";

					if (totalnoofinstalments != null	&& remaininginstalments != null	&& !totalnoofinstalments.equals("")	&& !remaininginstalments.equals("")) {paid_installment = Integer.toString(Integer.parseInt(totalnoofinstalments)		- Integer.parseInt(remaininginstalments));SKLogger_CC.writeLog("Inside paid_installment","paid_installment" + paid_installment);

					}
					if (ReqProd.equalsIgnoreCase("Credit Card")) {
						add_xml_str = add_xml_str+ "<no_of_paid_installment>"+ paid_installment+ "</no_of_paid_installment><write_off_amount>"+ Internal_WriteOff_Check+ "</write_off_amount><company_flag>N</company_flag></InternalBureauIndividualProducts>";
					} else {add_xml_str = add_xml_str+ "<no_of_paid_installment>"+ paid_installment+ "</no_of_paid_installment><company_flag>N</company_flag><type_of_od>"+ ""+ "</type_of_od><amt_paid_last6mnths>"+ ""+ "</amt_paid_last6mnths></InternalBureauIndividualProducts>";

					}
				}

			}
		} catch (Exception e) {
			SKLogger_CC.writeLog("RLOSCommon", "Internal liab tag Cration: " + CC_Comn.printException(e));
		}
		SKLogger_CC.writeLog("RLOSCommon", "Internal liab tag Cration: "
				+ add_xml_str);
		return add_xml_str;
	}

	public String getCustAddress_details() {
		SKLogger_CC.writeLog("RLOSCommon java file","inside getCustAddress_details : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int add_row_count = formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
		SKLogger_CC.writeLog("RLOSCommon java file","inside getCustAddress_details add_row_count+ : " + add_row_count);
		String add_xml_str = "";
		for (int i = 0; i < add_row_count; i++) {
			String Address_type = formObject.getNGValue(
					"cmplx_AddressDetails_cmplx_AddressGrid", i, 0); // 0
			String Po_Box = formObject.getNGValue(
					"cmplx_AddressDetails_cmplx_AddressGrid", i, 8);// 1
			String flat_Villa = formObject.getNGValue(
					"cmplx_AddressDetails_cmplx_AddressGrid", i, 1);// 2
			String Building_name = formObject.getNGValue(
					"cmplx_AddressDetails_cmplx_AddressGrid", i, 2);// 3
			String street_name = formObject.getNGValue(
					"cmplx_AddressDetails_cmplx_AddressGrid", i, 3);// 4
			String Landmard = formObject.getNGValue(
					"cmplx_AddressDetails_cmplx_AddressGrid", i, 4);// 5
			String city = formObject.getNGValue(
					"cmplx_AddressDetails_cmplx_AddressGrid", i, 5);// 6
			String Emirates = formObject.getNGValue(
					"cmplx_AddressDetails_cmplx_AddressGrid", i, 6);// 7
			String country = formObject.getNGValue(
					"cmplx_AddressDetails_cmplx_AddressGrid", i, 7);// 8
			String preferrd = "";
			if (formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",
					i, 10).equalsIgnoreCase("true"))// 10
				preferrd = "Y";
			else
				preferrd = "N";

			add_xml_str = add_xml_str + "<AddressDetails><AddressType>" + Address_type + "</AddressType><AddrPrefFlag>" + preferrd + "</AddrPrefFlag><PrefFormat>STRUCTURED_FORMAT</PrefFormat>";
			add_xml_str = add_xml_str + "<AddrLine1>" + flat_Villa + "</AddrLine1>";
			add_xml_str = add_xml_str + "<AddrLine2>" + Building_name + "</AddrLine2>";
			add_xml_str = add_xml_str + "<AddrLine3>" + street_name + "</AddrLine3>";
			add_xml_str = add_xml_str + "<AddrLine4>" + Landmard + "</AddrLine4>";
			add_xml_str = add_xml_str + "<City>" + city + "</City>";
			add_xml_str = add_xml_str + "<CountryCode>" + country + "</CountryCode>";
			add_xml_str = add_xml_str + "<State>" + Emirates + "</State>";
			add_xml_str = add_xml_str + "<POBox>" + Po_Box + "</POBox></AddressDetails>";
		}
		SKLogger_CC.writeLog("RLOSCommon", "Address tag Cration: "
				+ add_xml_str);
		return add_xml_str;
	}

	public String InternalBouncedCheques() {
		SKLogger_CC.writeLog("RLOSCommon java file","inside InternalBouncedCheques : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "select CIFID,AcctId,returntype,returnNumber,returnAmount,retReasonCode,returnDate from ng_rlos_FinancialSummary_ReturnsDtls with (nolock) where Child_Wi = '"
			+ formObject.getWFWorkitemName() + "'";
		SKLogger_CC.writeLog("InternalBouncedCheques sQuery" + sQuery, "");
		String add_xml_str = "";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		SKLogger_CC.writeLog("InternalBouncedCheques list size"
				+ OutputXML.size(), "");

		for (int i = 0; i < OutputXML.size(); i++) {

			String applicantID = "";
			String chequeNo = "";
			String internal_bounced_cheques_id = "";
			String bouncedCheq = "";
			String amount = "";
			String reason = "";
			String returnDate = "";

			if (!(OutputXML.get(i).get(0) == null || OutputXML.get(i).get(0)
					.equals(""))) {
				applicantID = OutputXML.get(i).get(0).toString();
			}
			if (!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1)
					.equals(""))) {
				internal_bounced_cheques_id = OutputXML.get(i).get(1).toString();
			}
			if (!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2)
					.equals(""))) {
				bouncedCheq = OutputXML.get(i).get(2).toString();
			}
			if (!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3)
					.equals(""))) {
				chequeNo = OutputXML.get(i).get(3).toString();
			}
			if (!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4)
					.equals(""))) {
				amount = OutputXML.get(i).get(4).toString();
			}
			if (!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5)
					.equals(""))) {
				reason = OutputXML.get(i).get(5).toString();
			}
			if (!(OutputXML.get(i).get(6) == null || OutputXML.get(i).get(6)
					.equals(""))) {
				returnDate = OutputXML.get(i).get(6).toString();
			}

			if (applicantID != null && !applicantID.equalsIgnoreCase("")
					&& !applicantID.equalsIgnoreCase("null")) {
				add_xml_str = add_xml_str + "<InternalBouncedCheques><applicant_id>" + applicantID + "</applicant_id>";
				add_xml_str = add_xml_str + "<internal_bounced_cheques_id>" + internal_bounced_cheques_id + "</internal_bounced_cheques_id>";
				if (bouncedCheq.equalsIgnoreCase("ICCS")) {
					add_xml_str = add_xml_str + "<bounced_cheque>" + "1"+ "</bounced_cheque>";
				}
				add_xml_str = add_xml_str + "<cheque_no>" + chequeNo + "</cheque_no>";
				add_xml_str = add_xml_str + "<amount>" + amount + "</amount>";
				add_xml_str = add_xml_str + "<reason>" + reason + "</reason>";
				add_xml_str = add_xml_str + "<return_date>" + returnDate + "</return_date>";
				add_xml_str = add_xml_str + "<provider_no>" + "RAKBANK" + "</provider_no>";
				if (bouncedCheq.equalsIgnoreCase("DDS")) {
					add_xml_str = add_xml_str + "<bounced_cheque_dds>" + "1"+ "</bounced_cheque_dds>";
				}
				add_xml_str = add_xml_str + "<company_flag>N</company_flag></InternalBouncedCheques>";
			}

		}
		SKLogger_CC.writeLog("RLOSCommon", "Internal liab tag Cration: "
				+ add_xml_str);
		return add_xml_str;
	}

	public String InternalBureauPipelineProducts() {
		SKLogger_CC.writeLog("RLOSCommon java file","inside InternalBureauPipelineProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "SELECT cifid,product_type,custroletype,lastupdatedate,totalamount,totalnoofinstalments,totalloanamount,agreementId FROM ng_RLOS_CUSTEXPOSE_LoanDetails  with (nolock) where Child_wi = '"
			+ formObject.getWFWorkitemName()
			+ "' and  LoanStat = 'Pipeline'";
		SKLogger_CC.writeLog("InternalBureauPipelineProducts sQuery" + sQuery,"");
		String add_xml_str = "";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		SKLogger_CC.writeLog("InternalBureauPipelineProducts list size"
				+ OutputXML.size(), "");

		for (int i = 0; i < OutputXML.size(); i++) {

			String cifId = "";
			String Product = "";
			String lastUpdateDate = "";
			String TotAmount = "";
			String TotNoOfInstlmnt = "";
			String TotLoanAmt = "";
			String agreementId = "";

			if (!(OutputXML.get(i).get(0) == null || OutputXML.get(i).get(0)
					.equals(""))) {
				cifId = OutputXML.get(i).get(0).toString();
			}
			if (!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1)
					.equals(""))) {
				Product = OutputXML.get(i).get(1).toString();
			}
			if (!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2)
					.equals(""))) {
			}
			if (!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3)
					.equals(""))) {
				lastUpdateDate = OutputXML.get(i).get(3).toString();
			}
			if (!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4)
					.equals(""))) {
				TotAmount = OutputXML.get(i).get(4).toString();
			}
			if (!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5)
					.equals(""))) {
				TotNoOfInstlmnt = OutputXML.get(i).get(5).toString();
			}
			if (!(OutputXML.get(i).get(6) == null || OutputXML.get(i).get(6)
					.equals(""))) {
				TotLoanAmt = OutputXML.get(i).get(6).toString();
			}
			if (!(OutputXML.get(i).get(7) == null || OutputXML.get(i).get(7)
					.equals(""))) {
				agreementId = OutputXML.get(i).get(7).toString();
			}

			if (cifId != null && !cifId.equalsIgnoreCase("")
					&& !cifId.equalsIgnoreCase("null")) {
				add_xml_str = add_xml_str + "<InternalBureauPipelineProducts>";
				add_xml_str = add_xml_str + "<applicant_id>" + cifId + "</applicant_id>";
				add_xml_str = add_xml_str + "<internal_bureau_pipeline_products_id>" + agreementId + "</internal_bureau_pipeline_products_id>";// to be
				// populated
				// later
				add_xml_str = add_xml_str + "<ppl_provider_no>" + "" + "</ppl_provider_no>";
				add_xml_str = add_xml_str + "<ppl_product>" + Product + "</ppl_product>";
				add_xml_str = add_xml_str + "<ppl_type_of_contract>" + "" + "</ppl_type_of_contract>";
				add_xml_str = add_xml_str + "<ppl_phase>" + "" + "</ppl_phase>"; // to
				// be
				// populated
				// later

				add_xml_str = add_xml_str + "<ppl_role>" + Product + "</ppl_role>";
				add_xml_str = add_xml_str + "<ppl_date_of_last_update>" + lastUpdateDate + "</ppl_date_of_last_update>";
				add_xml_str = add_xml_str + "<ppl_total_amount>" + TotAmount + "</ppl_total_amount>";
				add_xml_str = add_xml_str + "<ppl_no_of_instalments>" + TotNoOfInstlmnt + "</ppl_no_of_instalments>";
				add_xml_str = add_xml_str + "<ppl_credit_limit>" + TotLoanAmt + "</ppl_credit_limit>";

				add_xml_str = add_xml_str + "<ppl_no_of_days_in_pipeline>" + "" + "</ppl_no_of_days_in_pipeline><company_flag>N</company_flag></InternalBureauPipelineProducts>"; // to
				// be
				// populated
				// later
			}

		}
		SKLogger_CC.writeLog("RLOSCommon", "Internal liab tag Cration: "
				+ add_xml_str);
		return add_xml_str;
	}

	public String ExternalBureauData() {
		SKLogger_CC.writeLog("RLOSCommon java file","inside ExternalBureauData : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "select  CifId,fullnm,TotalOutstanding,TotalOverdue,NoOfContracts,Total_Exposure,WorstCurrentPaymentDelay,Worst_PaymentDelay_Last24Months,Worst_Status_Last24Months,Nof_Records,NoOf_Cheque_Return_Last3,Nof_DDES_Return_Last3Months,Nof_Cheque_Return_Last6,DPD30_Last6Months,Internal_WriteOff_Check from NG_rlos_custexpose_Derived where Child_wi  = '"
			+ formObject.getWFWorkitemName()
			+ "' and Request_type= 'ExternalExposure'";
		SKLogger_CC.writeLog("ExternalBureauData sQuery" + sQuery, "");
		String AecbHistQuery = "select isnull(max(AECBHistMonthCnt),0) as AECBHistMonthCnt from ( select MAX(AECBHistMonthCnt) as AECBHistMonthCnt  from ng_rlos_cust_extexpo_CardDetails where  Child_wi  = '"
			+ formObject.getWFWorkitemName()
			+ "' union select Max(AECBHistMonthCnt) from ng_rlos_cust_extexpo_LoanDetails where Child_wi  = '"
			+ formObject.getWFWorkitemName() + "') as ext_expo";
		String add_xml_str = "";
		try {
			List<List<String>> OutputXML = formObject
			.getDataFromDataSource(sQuery);
			SKLogger_CC.writeLog("ExternalBureauData list size" + OutputXML.size(), "");
			List<List<String>> AecbHistQueryData = formObject
			.getDataFromDataSource(AecbHistQuery);

			if (AecbHistQueryData.get(0).get(0).equalsIgnoreCase("0")) {

				String CifId = formObject.getNGValue("cmplx_Customer_CIFNO");
				String fullnm = formObject.getNGValue("cmplx_Customer_FIrstNAme") + " " + formObject.getNGValue("cmplx_Customer_LAstNAme");
				String TotalOutstanding = "";
				String TotalOverdue = "";
				String NoOfContracts = "";
				String Total_Exposure = "";
				String WorstCurrentPaymentDelay = "";
				String Worst_PaymentDelay_Last24Months = "";
				String Worst_Status_Last24Months = "";
				String Nof_Records = "";
				String NoOf_Cheque_Return_Last3 = "";
				String Nof_DDES_Return_Last3Months = "";
				String Nof_Cheque_Return_Last6 = "";
				String DPD30_Last6Months = "";
				add_xml_str = add_xml_str + "<ExternalBureau>";
				add_xml_str = add_xml_str + "<applicant_id>" + CifId + "</applicant_id>";

				add_xml_str = add_xml_str + "<full_name>" + fullnm + "</full_name>";
				add_xml_str = add_xml_str + "<total_out_bal>" + TotalOutstanding + "</total_out_bal>";

				add_xml_str = add_xml_str + "<total_overdue>" + TotalOverdue + "</total_overdue>";
				add_xml_str = add_xml_str + "<no_default_contract>" + NoOfContracts + "</no_default_contract>";
				add_xml_str = add_xml_str + "<total_exposure>" + Total_Exposure + "</total_exposure>";
				add_xml_str = add_xml_str + "<worst_curr_pay>" + WorstCurrentPaymentDelay + "</worst_curr_pay>";
				add_xml_str = add_xml_str + "<worst_curr_pay_24>" + Worst_PaymentDelay_Last24Months + "</worst_curr_pay_24>";
				add_xml_str = add_xml_str + "<worst_status_24>" + Worst_Status_Last24Months + "</worst_status_24>";

				add_xml_str = add_xml_str + "<no_of_rec>" + Nof_Records + "</no_of_rec>";
				add_xml_str = add_xml_str + "<cheque_return_3mon>" + NoOf_Cheque_Return_Last3 + "</cheque_return_3mon>";
				add_xml_str = add_xml_str + "<dds_return_3mon>" + Nof_DDES_Return_Last3Months + "</dds_return_3mon>";
				add_xml_str = add_xml_str + "<cheque_return_6mon>" + Nof_Cheque_Return_Last6 + "</cheque_return_6mon>";
				add_xml_str = add_xml_str + "<dds_return_6mon>" + DPD30_Last6Months + "</dds_return_6mon>";
				add_xml_str = add_xml_str + "<prod_external_writeoff_amount>" + "" + "</prod_external_writeoff_amount>";

				add_xml_str = add_xml_str + "<no_months_aecb_history >" + AecbHistQueryData.get(0).get(0) + "</no_months_aecb_history >";

				add_xml_str = add_xml_str + "<company_flag>N</company_flag></ExternalBureau>";

				SKLogger_CC.writeLog("RLOSCommon","Internal liab tag Cration: " + add_xml_str);
				return add_xml_str;
			} else {
				for (int i = 0; i < OutputXML.size(); i++) {

					String CifId = formObject	.getNGValue("cmplx_Customer_CIFNO");
					String fullnm = "";
					String TotalOutstanding = "";
					String TotalOverdue = "";
					String NoOfContracts = "";
					String Total_Exposure = "";
					String WorstCurrentPaymentDelay = "";
					String Worst_PaymentDelay_Last24Months = "";
					String Worst_Status_Last24Months = "";
					String Nof_Records = "";
					String NoOf_Cheque_Return_Last3 = "";
					String Nof_DDES_Return_Last3Months = "";
					String Nof_Cheque_Return_Last6 = "";
					String DPD30_Last6Months = "";
					if (!(OutputXML.get(i).get(1) == null || OutputXML.get(i)	.get(1).equals(""))) {fullnm = OutputXML.get(i).get(1).toString();
					}
					if (!(OutputXML.get(i).get(2) == null || OutputXML.get(i)	.get(2).equals(""))) {TotalOutstanding = OutputXML.get(i).get(2).toString();

					}
					if (!(OutputXML.get(i).get(3) == null || OutputXML.get(i)	.get(3).equals(""))) {TotalOverdue = OutputXML.get(i).get(3).toString();
					}
					if (!(OutputXML.get(i).get(4) == null || OutputXML.get(i)	.get(4).equals(""))) {NoOfContracts = OutputXML.get(i).get(4).toString();
					}
					if (!(OutputXML.get(i).get(5) == null || OutputXML.get(i)	.get(5).equals(""))) {Total_Exposure = OutputXML.get(i).get(5).toString();
					}
					if (OutputXML.get(i).get(6) != null	&& !OutputXML.get(i).get(6).isEmpty()	&& !OutputXML.get(i).get(6).equals("")	&& !OutputXML.get(i).get(6).equalsIgnoreCase("null")) {WorstCurrentPaymentDelay = OutputXML.get(i).get(6).toString();
					}
					if (OutputXML.get(i).get(7) != null	&& !OutputXML.get(i).get(7).isEmpty()	&& !OutputXML.get(i).get(7).equals("")	&& !OutputXML.get(i).get(7).equalsIgnoreCase("null")) {Worst_PaymentDelay_Last24Months = OutputXML.get(i).get(		7).toString();
					}
					if (OutputXML.get(i).get(8) != null	&& !OutputXML.get(i).get(8).isEmpty()	&& !OutputXML.get(i).get(8).equals("")	&& !OutputXML.get(i).get(8).equalsIgnoreCase("null")) {Worst_Status_Last24Months = OutputXML.get(i).get(8).toString();
					}
					if (!(OutputXML.get(i).get(9) == null || OutputXML.get(i)	.get(9).equals(""))) {Nof_Records = OutputXML.get(i).get(9).toString();
					}
					if (!(OutputXML.get(i).get(10) == null || OutputXML.get(i)	.get(10).equals(""))) {NoOf_Cheque_Return_Last3 = OutputXML.get(i).get(10).toString();
					}
					if (!(OutputXML.get(i).get(11) == null || OutputXML.get(i)	.get(11).equals(""))) {Nof_DDES_Return_Last3Months = OutputXML.get(i).get(11).toString();
					}
					if (OutputXML.get(i).get(12) != null	&& !OutputXML.get(i).get(12).isEmpty()	&& !OutputXML.get(i).get(12).equals("")	&& !OutputXML.get(i).get(12).equalsIgnoreCase("null")) {// SKLogger.writeLog("Inside for",// "asdasdasd"+OutputXML.get(i).get(12));Nof_Cheque_Return_Last6 = OutputXML.get(i).get(12).toString();
					}
					if (!(OutputXML.get(i).get(13) == null || OutputXML.get(i)	.get(13).equals(""))) {DPD30_Last6Months = OutputXML.get(i).get(13).toString();
					}

					add_xml_str = add_xml_str + "<ExternalBureau>";
					add_xml_str = add_xml_str + "<applicant_id>" + CifId+ "</applicant_id>";

					add_xml_str = add_xml_str + "<full_name>" + fullnm+ "</full_name>";
					add_xml_str = add_xml_str + "<total_out_bal>"+ TotalOutstanding + "</total_out_bal>";

					add_xml_str = add_xml_str + "<total_overdue>"+ TotalOverdue + "</total_overdue>";
					add_xml_str = add_xml_str + "<no_default_contract>"+ NoOfContracts + "</no_default_contract>";
					add_xml_str = add_xml_str + "<total_exposure>"+ Total_Exposure + "</total_exposure>";
					add_xml_str = add_xml_str + "<worst_curr_pay>"+ WorstCurrentPaymentDelay + "</worst_curr_pay>";
					add_xml_str = add_xml_str + "<worst_curr_pay_24>"+ Worst_PaymentDelay_Last24Months+ "</worst_curr_pay_24>";
					add_xml_str = add_xml_str + "<worst_status_24>"+ Worst_Status_Last24Months + "</worst_status_24>";

					add_xml_str = add_xml_str + "<no_of_rec>" + Nof_Records+ "</no_of_rec>";
					add_xml_str = add_xml_str + "<cheque_return_3mon>"+ NoOf_Cheque_Return_Last3+ "</cheque_return_3mon>";
					add_xml_str = add_xml_str + "<dds_return_3mon>"+ Nof_DDES_Return_Last3Months+ "</dds_return_3mon>";
					add_xml_str = add_xml_str + "<cheque_return_6mon>"+ Nof_Cheque_Return_Last6 + "</cheque_return_6mon>";
					add_xml_str = add_xml_str + "<dds_return_6mon>"+ DPD30_Last6Months + "</dds_return_6mon>";
					add_xml_str = add_xml_str+ "<prod_external_writeoff_amount>" + ""+ "</prod_external_writeoff_amount>";

					add_xml_str = add_xml_str + "<no_months_aecb_history >"+ AecbHistQueryData.get(0).get(0)+ "</no_months_aecb_history >";

					add_xml_str = add_xml_str+ "<company_flag>N</company_flag></ExternalBureau>";

				}
				SKLogger_CC.writeLog("RLOSCommon","Internal liab tag Cration: " + add_xml_str);
				return add_xml_str;
			}

		}

		catch (Exception e) {
			SKLogger_CC.writeLog("RLOSCommon",
					"Exception occurred in externalBureauData()"+ e.getMessage() + "\n Error: "+ CC_Comn.printException(e));
			return null;
		}
	}

	public String ExternalBouncedCheques() {
		SKLogger_CC.writeLog("RLOSCommon java file","inside ExternalBouncedCheques : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "SELECT cifid,number,amount,reasoncode,returndate,providerno FROM ng_rlos_cust_extexpo_ChequeDetails  with (nolock) where child_wi = '"
			+ formObject.getWFWorkitemName()
			+ "' and Request_Type = 'ExternalExposure'";
		SKLogger_CC.writeLog("ExternalBouncedCheques sQuery" + sQuery, "");
		String add_xml_str = "";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		SKLogger_CC.writeLog("ExternalBouncedCheques list size"
				+ OutputXML.size(), "");

		for (int i = 0; i < OutputXML.size(); i++) {

			String CifId = formObject.getNGValue("cmplx_Customer_CIFNO");
			String chqNo = "";
			String Amount = "";
			String Reason = "";
			String returnDate = "";
			String providerNo = "";

			if (!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1)
					.equals(""))) {
				chqNo = OutputXML.get(i).get(1).toString();
			}
			if (!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2)
					.equals(""))) {
				Amount = OutputXML.get(i).get(2).toString();
			}
			if (!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3)
					.equals(""))) {
				Reason = OutputXML.get(i).get(3).toString();
			}
			if (!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4)
					.equals(""))) {
				returnDate = OutputXML.get(i).get(4).toString();
			}
			if (!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5)
					.equals(""))) {
				providerNo = OutputXML.get(i).get(5).toString();
			}

			add_xml_str = add_xml_str + "<ExternalBouncedCheques><applicant_id>" + CifId + "</applicant_id>";
			add_xml_str = add_xml_str + "<external_bounced_cheques_id>" + "" + "</external_bounced_cheques_id>";
			add_xml_str = add_xml_str + "<bounced_cheque>" + "" + "</bounced_cheque>";
			add_xml_str = add_xml_str + "<cheque_no>" + chqNo + "</cheque_no>";
			add_xml_str = add_xml_str + "<amount>" + Amount + "</amount>";
			add_xml_str = add_xml_str + "<reason>" + Reason + "</reason>";
			add_xml_str = add_xml_str + "<return_date>" + returnDate + "</return_date>"; // to be populated later
			add_xml_str = add_xml_str + "<provider_no>" + providerNo + "</provider_no><company_flag>N</company_flag></ExternalBouncedCheques>"; // to
			// be
			// populated
			// later

		}
		SKLogger_CC.writeLog("RLOSCommon", "Internal liab tag Cration: "
				+ add_xml_str);
		return add_xml_str;
	}

	public String ExternalBureauIndividualProducts() {
		SKLogger_CC.writeLog("RLOSCommon java file","inside ExternalBureauIndividualProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "select CifId,AgreementId,LoanType,ProviderNo,LoanStat,CustRoleType,LoanApprovedDate,LoanMaturityDate,OutstandingAmt,TotalAmt,PaymentsAmt,TotalNoOfInstalments,RemainingInstalments,WriteoffStat,WriteoffStatDt,CreditLimit,OverdueAmt,NofDaysPmtDelay,MonthsOnBook,lastrepmtdt,IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last18Months,AECBHistMonthCnt,DPD5_Last3Months,'' as qc_Amnt,'' as Qc_emi,'' as Cac_indicator,Take_Over_Indicator,Consider_For_Obligations from ng_rlos_cust_extexpo_LoanDetails where Child_wi= '"
			+ formObject.getWFWorkitemName()
			+ "'  and LoanStat != 'Pipeline' union select CifId,CardEmbossNum,CardType,ProviderNo,CardStatus,CustRoleType,StartDate,ClosedDate,CurrentBalance,'' as col6,PaymentsAmount,NoOfInstallments,'' as col5,WriteoffStat,WriteoffStatDt,CashLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,lastrepmtdt,IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last18Months,AECBHistMonthCnt,DPD5_Last3Months,qc_amt,qc_emi,CAC_Indicator,Take_Over_Indicator,Consider_For_Obligations from ng_rlos_cust_extexpo_CardDetails where Child_wi  =  '"
			+ formObject.getWFWorkitemName()
			+ "' and cardstatus != 'Pipeline' ";
		SKLogger_CC.writeLog("ExternalBureauIndividualProducts sQuery" + sQuery, "");
		String add_xml_str = "";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		SKLogger_CC.writeLog("ExternalBureauIndividualProducts list size"
				+ OutputXML.size(), "");
		String ReqProd = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 1);
		for (int i = 0; i < OutputXML.size(); i++) {

			String CifId = formObject.getNGValue("cmplx_Customer_CIFNO");
			String AgreementId = "";
			String ContractType = "";
			String provider_no = "";
			String phase = "";
			String CustRoleType = "";
			String start_date = "";
			String close_date = "";
			String OutStanding_Balance = "";
			String TotalAmt = "";
			String PaymentsAmt = "";
			String TotalNoOfInstalments = "";
			String RemainingInstalments = "";
			String WorstStatus = "";
			String WorstStatusDate = "";
			String CreditLimit = "";
			String OverdueAmt = "";
			String NofDaysPmtDelay = "";
			String MonthsOnBook = "";
			String last_repayment_date = "";
			String DPD60Last18Months = "";
			String AECBHistMonthCnt = "";
			String DPD30Last6Months = "";
			String currently_current = "";
			String current_utilization = "";
			String delinquent_in_last_3months = "";
			String QC_Amt = "";
			String QC_emi = "";
			String CAC_Indicator = "";
			String consider_for_obligation = "";

			if (!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1)
					.equals(""))) {
				AgreementId = OutputXML.get(i).get(1).toString();
			}
			if (!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2)
					.equals(""))) {
				ContractType = OutputXML.get(i).get(2).toString();
				try {
					String cardquery = "select code from ng_master_contract_type where description='"+ ContractType + "'";
					SKLogger_CC.writeLog("ExternalBureauIndividualProducts sQuery"+ cardquery, "");
					List<List<String>> cardqueryXML = formObject	.getDataFromDataSource(cardquery);
					ContractType = cardqueryXML.get(0).get(0);
					SKLogger_CC.writeLog("ExternalBureauIndividualProducts ContractType"+ ContractType, "ContractType");
				} catch (Exception e) {
					SKLogger_CC.writeLog("ExternalBureauIndividualProducts ContractType Exception"+ e, "Exception");

					ContractType = OutputXML.get(i).get(2).toString();
				}
			}
			if (!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3)
					.equals(""))) {
				provider_no = OutputXML.get(i).get(3).toString();
			}
			if (!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4)
					.equals(""))) {
				phase = OutputXML.get(i).get(4).toString();
				if (phase.startsWith("A")) {
					phase = "A";
				} else {
					phase = "C";
				}
			}
			if (!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5)
					.equals(""))) {
				CustRoleType = OutputXML.get(i).get(5).toString();
			}
			if (!(OutputXML.get(i).get(6) == null || OutputXML.get(i).get(6)
					.equals(""))) {
				start_date = OutputXML.get(i).get(6).toString();
			}
			if (!(OutputXML.get(i).get(7) == null || OutputXML.get(i).get(7)
					.equals(""))) {
				close_date = OutputXML.get(i).get(7).toString();
			}
			if (!(OutputXML.get(i).get(8) == null || OutputXML.get(i).get(8)
					.equals(""))) {
				OutStanding_Balance = OutputXML.get(i).get(8).toString();
			}
			if (!(OutputXML.get(i).get(9) == null || OutputXML.get(i).get(9)
					.equals(""))) {
				TotalAmt = OutputXML.get(i).get(9).toString();
			}
			if (!(OutputXML.get(i).get(10) == null || OutputXML.get(i).get(10)
					.equals(""))) {
				PaymentsAmt = OutputXML.get(i).get(10).toString();
			}
			if (!(OutputXML.get(i).get(11) == null || OutputXML.get(i).get(11)
					.equals(""))) {
				TotalNoOfInstalments = OutputXML.get(i).get(11).toString();
			}
			if (!(OutputXML.get(i).get(12) == null || OutputXML.get(i).get(12)
					.equals(""))) {
				RemainingInstalments = OutputXML.get(i).get(12).toString();
			}
			if (!(OutputXML.get(i).get(13) == null || OutputXML.get(i).get(13)
					.equals(""))) {
				WorstStatus = OutputXML.get(i).get(13).toString();
			}
			if (!(OutputXML.get(i).get(14) == null || OutputXML.get(i).get(14)
					.equals(""))) {
				WorstStatusDate = OutputXML.get(i).get(14).toString();
			}
			if (!(OutputXML.get(i).get(15) == null || OutputXML.get(i).get(15)
					.equals(""))) {
				CreditLimit = OutputXML.get(i).get(15).toString();
			}
			if (!(OutputXML.get(i).get(16) == null || OutputXML.get(i).get(16)
					.equals(""))) {
				OverdueAmt = OutputXML.get(i).get(16).toString();
			}
			if (!(OutputXML.get(i).get(17) == null || OutputXML.get(i).get(17)
					.equals(""))) {
				NofDaysPmtDelay = OutputXML.get(i).get(17).toString();
			}
			if (!(OutputXML.get(i).get(18) == null || OutputXML.get(i).get(18)
					.equals(""))) {
				MonthsOnBook = OutputXML.get(i).get(18).toString();
			}
			if (!(OutputXML.get(i).get(19) == null || OutputXML.get(i).get(19)
					.equals(""))) {
				last_repayment_date = OutputXML.get(i).get(19).toString();
			}
			if (!(OutputXML.get(i).get(20) == null || OutputXML.get(i).get(20)
					.equals(""))) {
				currently_current = OutputXML.get(i).get(20).toString();
			}
			if (!(OutputXML.get(i).get(21) == null || OutputXML.get(i).get(21)
					.equals(""))) {
				current_utilization = OutputXML.get(i).get(21).toString();
			}
			if (!(OutputXML.get(i).get(22) == null || OutputXML.get(i).get(22)
					.equals(""))) {
				DPD30Last6Months = OutputXML.get(i).get(22).toString();
			}
			if (!(OutputXML.get(i).get(23) == null || OutputXML.get(i).get(23)
					.equals(""))) {
				DPD60Last18Months = OutputXML.get(i).get(23).toString();
			}
			if (!(OutputXML.get(i).get(24) == null || OutputXML.get(i).get(24)
					.equals(""))) {
				AECBHistMonthCnt = OutputXML.get(i).get(24).toString();
			}

			if (!(OutputXML.get(i).get(25) == null || OutputXML.get(i).get(25)
					.equals(""))) {
				delinquent_in_last_3months = OutputXML.get(i).get(25).toString();
			}
			if (!(OutputXML.get(i).get(26) == null || OutputXML.get(i).get(26)
					.equals(""))) {
				QC_Amt = OutputXML.get(i).get(26).toString();
			}
			if (!(OutputXML.get(i).get(27) == null || OutputXML.get(i).get(27)
					.equals(""))) {
				QC_emi = OutputXML.get(i).get(27).toString();
			}
			if (!(OutputXML.get(i).get(28) == null || OutputXML.get(i).get(28)
					.equals(""))) {
				CAC_Indicator = OutputXML.get(i).get(28).toString();
				if (CAC_Indicator != null && !(CAC_Indicator.equalsIgnoreCase(""))) {
					if (CAC_Indicator.equalsIgnoreCase("true")) {CAC_Indicator = "Y";
					} else {CAC_Indicator = "N";
					}
				}
			}
			String TakeOverIndicator = "";
			if (!(OutputXML.get(i).get(29) == null || OutputXML.get(i).get(29)
					.equals(""))) {
				TakeOverIndicator = OutputXML.get(i).get(29).toString();
				if (TakeOverIndicator != null && !(TakeOverIndicator.equalsIgnoreCase(""))) {
					if (TakeOverIndicator.equalsIgnoreCase("true")) {TakeOverIndicator = "Y";
					} else {TakeOverIndicator = "N";
					}
				}
			}
			if (!(OutputXML.get(i).get(30) == null || OutputXML.get(i).get(30)
					.equals(""))) {
				consider_for_obligation = OutputXML.get(i).get(30).toString();
				if (consider_for_obligation != null && !(consider_for_obligation.equalsIgnoreCase(""))) {
					if (consider_for_obligation.equalsIgnoreCase("true")) {consider_for_obligation = "Y";
					} else {consider_for_obligation = "N";
					}
				}
			}

			add_xml_str = add_xml_str + "<ExternalBureauIndividualProducts><applicant_id>" + CifId + "</applicant_id>";
			add_xml_str = add_xml_str + "<external_bureau_individual_products_id>" + AgreementId + "</external_bureau_individual_products_id>";
			add_xml_str = add_xml_str + "<contract_type>" + ContractType + "</contract_type>";
			add_xml_str = add_xml_str + "<provider_no>" + provider_no + "</provider_no>";
			add_xml_str = add_xml_str + "<phase>" + phase + "</phase>";
			add_xml_str = add_xml_str + "<role_of_customer>" + CustRoleType + "</role_of_customer>";
			add_xml_str = add_xml_str + "<start_date>" + start_date + "</start_date>";

			add_xml_str = add_xml_str + "<close_date>" + close_date + "</close_date>";
			add_xml_str = add_xml_str + "<outstanding_balance>" + OutStanding_Balance + "</outstanding_balance>";
			add_xml_str = add_xml_str + "<total_amount>" + TotalAmt + "</total_amount>";
			add_xml_str = add_xml_str + "<payments_amount>" + PaymentsAmt + "</payments_amount>";
			add_xml_str = add_xml_str + "<total_no_of_instalments>" + TotalNoOfInstalments + "</total_no_of_instalments>";
			add_xml_str = add_xml_str + "<no_of_remaining_instalments>" + RemainingInstalments + "</no_of_remaining_instalments>";
			add_xml_str = add_xml_str + "<worst_status>" + WorstStatus + "</worst_status>";
			add_xml_str = add_xml_str + "<worst_status_date>" + WorstStatusDate + "</worst_status_date>";

			add_xml_str = add_xml_str + "<credit_limit>" + CreditLimit + "</credit_limit>";
			add_xml_str = add_xml_str + "<overdue_amount>" + OverdueAmt + "</overdue_amount>";
			add_xml_str = add_xml_str + "<no_of_days_payment_delay>" + NofDaysPmtDelay + "</no_of_days_payment_delay>";
			add_xml_str = add_xml_str + "<mob>" + MonthsOnBook + "</mob>";
			add_xml_str = add_xml_str + "<last_repayment_date>" + last_repayment_date + "</last_repayment_date>";
			if (currently_current != null
					&& currently_current.equalsIgnoreCase("1"))
				add_xml_str = add_xml_str + "<currently_current>Y</currently_current>";
			else {
				add_xml_str = add_xml_str + "<currently_current>N</currently_current>";
			}
			add_xml_str = add_xml_str + "<current_utilization>" + current_utilization + "</current_utilization>";
			add_xml_str = add_xml_str + "<dpd_30_last_6_mon>" + DPD30Last6Months + "</dpd_30_last_6_mon>";

			add_xml_str = add_xml_str + "<dpd_60p_in_last_18_mon>" + DPD60Last18Months + "</dpd_60p_in_last_18_mon>";
			add_xml_str = add_xml_str + "<no_months_aecb_history>" + AECBHistMonthCnt + "</no_months_aecb_history>";
			add_xml_str = add_xml_str + "<delinquent_in_last_3months>" + delinquent_in_last_3months + "</delinquent_in_last_3months>";
			add_xml_str = add_xml_str + "<clean_funded>" + "" + "</clean_funded>";
			add_xml_str = add_xml_str + "<cac_indicator>" + CAC_Indicator + "</cac_indicator>";
			add_xml_str = add_xml_str + "<qc_emi>" + QC_emi + "</qc_emi>";
			if (ReqProd.equalsIgnoreCase("Credit Card")) {
				add_xml_str = add_xml_str + "<qc_amount>" + QC_Amt + "</qc_amount><company_flag>N</company_flag><cac_bank_name>" + formObject.getNGValue("cmplx_EmploymentDetails_tenancycntrctemirate") + "</cac_bank_name><take_over_indicator>" + TakeOverIndicator + "</take_over_indicator><consider_for_obligation>" + consider_for_obligation + "</consider_for_obligation></ExternalBureauIndividualProducts>";
			} else {
				add_xml_str = add_xml_str + "<qc_amount>" + QC_Amt + "</qc_amount><company_flag>N</company_flag><cac_bank_name>" + formObject.getNGValue("cmplx_EmploymentDetails_tenancycntrctemirate") + "</cac_bank_name><take_over_indicator>" + TakeOverIndicator + "</take_over_indicator></ExternalBureauIndividualProducts>";
			}

		}
		SKLogger_CC.writeLog("RLOSCommon", "Internal liab tag Cration: "
				+ add_xml_str);
		return add_xml_str;
	}

	public String ExternalBureauManualAddIndividualProducts() {
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		SKLogger_CC.writeLog("ExternalBureauManualAddIndividualProducts sQuery", "");
		int Man_liab_row_count = formObject.getLVWRowCount("cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
		String applicant_id = formObject.getNGValue("cmplx_Customer_CIFNO");
		String add_xml_str = "";
		Date date = new Date();
		String modifiedDate = new SimpleDateFormat("dd/MM/yyyy").format(date);
		SKLogger_CC.writeLog("ExternalBureauIndividualProducts list size"
				+ Man_liab_row_count, "");
		if (Man_liab_row_count != 0) {
			for (int i = 0; i < Man_liab_row_count; i++) {
				String Type_of_Contract = formObject.getNGValue(		"cmplx_Liability_New_cmplx_LiabilityAdditionGrid",		i, 0); // 0
				String Limit = formObject.getNGValue(		"cmplx_Liability_New_cmplx_LiabilityAdditionGrid",		i, 1); // 0
				String EMI = formObject.getNGValue(		"cmplx_Liability_New_cmplx_LiabilityAdditionGrid",		i, 2); // 0
				String Take_over_Indicator = (formObject.getNGValue(		"cmplx_Liability_New_cmplx_LiabilityAdditionGrid",		i, 3).equalsIgnoreCase("true") ? "Y" : "N"); // 0
				String cac_Indicator = (formObject.getNGValue(		"cmplx_Liability_New_cmplx_LiabilityAdditionGrid",		i, 5).equalsIgnoreCase("true") ? "Y" : "N"); // 0
				String Qc_amt = formObject.getNGValue(		"cmplx_Liability_New_cmplx_LiabilityAdditionGrid",		i, 6); // 0
				String Qc_Emi = formObject.getNGValue(		"cmplx_Liability_New_cmplx_LiabilityAdditionGrid",		i, 7); // 0
				if (cac_Indicator.equalsIgnoreCase("true")) {
					cac_Indicator = "Y";
				} else {
					cac_Indicator = "N";
				}
				String consider_for_obligation = (formObject.getNGValue(		"cmplx_Liability_New_cmplx_LiabilityAdditionGrid",		i, 8).equalsIgnoreCase("true") ? "Y" : "N"); // 0
				// String MOB =
				// formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",
				// i, 9); //0
				String Utilization = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i,11); // 0
				String OutStanding = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i,12); // 0
				String mob = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i,10);
				String delinquent_in_last_3months = (formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i,13).equalsIgnoreCase("true") ? "1" : "0");
				String dpd_30_last_6_mon = (formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i,14).equalsIgnoreCase("true") ? "1" : "0");
				String dpd_60p_in_last_18_mon = (formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i,15).equalsIgnoreCase("true") ? "1" : "0");
				add_xml_str = add_xml_str + "<ExternalBureauIndividualProducts><applicant_id>" + applicant_id + "</applicant_id>";
				add_xml_str = add_xml_str + "<external_bureau_individual_products_id></external_bureau_individual_products_id>";
				add_xml_str = add_xml_str + "<contract_type>" + Type_of_Contract + "</contract_type>";
				add_xml_str = add_xml_str + "<provider_no></provider_no>";
				add_xml_str = add_xml_str + "<phase>A</phase>";
				add_xml_str = add_xml_str + "<role_of_customer>Main</role_of_customer>";
				add_xml_str = add_xml_str + "<start_date>" + modifiedDate + "</start_date>";

				add_xml_str = add_xml_str + "<close_date></close_date>";
				add_xml_str = add_xml_str + "<outstanding_balance>" + OutStanding + "</outstanding_balance>";
				add_xml_str = add_xml_str + "<total_amount>" + Limit + "</total_amount>";
				add_xml_str = add_xml_str + "<payments_amount>" + Limit + "</payments_amount>";
				add_xml_str = add_xml_str + "<total_no_of_instalments></total_no_of_instalments>";
				add_xml_str = add_xml_str + "<no_of_remaining_instalments></no_of_remaining_instalments>";
				add_xml_str = add_xml_str + "<worst_status></worst_status>";
				add_xml_str = add_xml_str + "<worst_status_date></worst_status_date>";

				add_xml_str = add_xml_str + "<credit_limit>" + EMI + "</credit_limit>";
				add_xml_str = add_xml_str + "<overdue_amount></overdue_amount>";
				add_xml_str = add_xml_str + "<no_of_days_payment_delay></no_of_days_payment_delay>";
				add_xml_str = add_xml_str + "<mob>" + mob + "</mob>";
				add_xml_str = add_xml_str + "<last_repayment_date></last_repayment_date>";

				add_xml_str = add_xml_str + "<currently_current>N</currently_current>";

				add_xml_str = add_xml_str + "<current_utilization>" + Utilization + "</current_utilization>";
				add_xml_str = add_xml_str + "<dpd_30_last_6_mon>" + dpd_30_last_6_mon + "</dpd_30_last_6_mon>";

				add_xml_str = add_xml_str + "<dpd_60p_in_last_18_mon>" + dpd_60p_in_last_18_mon + "</dpd_60p_in_last_18_mon>";
				add_xml_str = add_xml_str + "<no_months_aecb_history></no_months_aecb_history>";
				add_xml_str = add_xml_str + "<delinquent_in_last_3months>" + delinquent_in_last_3months + "</delinquent_in_last_3months>";
				add_xml_str = add_xml_str + "<clean_funded>" + "" + "</clean_funded>";
				add_xml_str = add_xml_str + "<cac_indicator>" + cac_Indicator + "</cac_indicator>";
				add_xml_str = add_xml_str + "<qc_emi>" + Qc_Emi + "</qc_emi>";
				add_xml_str = add_xml_str + "<qc_amount>" + Qc_amt + "</qc_amount><cac_bank_name>" + formObject.getNGValue("cmplx_EmploymentDetails_tenancycntrctemirate") + "</cac_bank_name><take_over_indicator>" + Take_over_Indicator + "</take_over_indicator><company_flag>N</company_flag><consider_for_obligation>" + consider_for_obligation + "</consider_for_obligation></ExternalBureauIndividualProducts>";

			}

		}
		SKLogger_CC.writeLog("RLOSCommon", "Internal liab tag Cration: "
				+ add_xml_str);
		return add_xml_str;
	}

	public String ExternalBureauPipelineProducts() {
		SKLogger_CC.writeLog("RLOSCommon java file","inside ExternalBureauPipelineProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "select AgreementId,ProviderNo,LoanType,LoanDesc,CustRoleType,Datelastupdated,Total_Amount,TotalNoOfInstalments,'' as col1,NoOfDaysInPipeline from ng_rlos_cust_extexpo_LoanDetails where child_wi  =  '"
			+ formObject.getWFWorkitemName()
			+ "' and LoanStat = 'Pipeline' union select CardEmbossNum,ProviderNo,CardTypeDesc,CardType,CustRoleType,LastUpdateDate,'' as col2,NoOfInstallments,TotalAmount,NoOfDaysInPipeLine  from ng_rlos_cust_extexpo_CardDetails where child_wi  =  '"
			+ formObject.getWFWorkitemName()
			+ "' and cardstatus = 'Pipeline'";
		SKLogger_CC.writeLog("ExternalBureauPipelineProducts sQuery" + sQuery,"");
		String add_xml_str = "";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		SKLogger_CC.writeLog("ExternalBureauPipelineProducts list size"
				+ OutputXML.size(), "");
		String cifId = formObject.getNGValue("cmplx_Customer_CIFNO");

		for (int i = 0; i < OutputXML.size(); i++) {

			String agreementID = "";
			String ProviderNo = "";
			String contractType = "";
			String productType = "";
			String role = "";
			String lastUpdateDate = "";
			String TotAmt = "";
			String noOfInstalmnt = "";
			String creditLimit = "";
			String noOfDayinPpl = "";
			if (!(OutputXML.get(i).get(0) == null || OutputXML.get(i).get(0)
					.equals(""))) {
				agreementID = OutputXML.get(i).get(0).toString();
			}
			if (!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1)
					.equals(""))) {
				ProviderNo = OutputXML.get(i).get(1).toString();
			}
			if (OutputXML.get(i).get(2) != null
					&& !OutputXML.get(i).get(2).isEmpty()
					&& !OutputXML.get(i).get(2).equals("")
					&& !OutputXML.get(i).get(2).equalsIgnoreCase("null")) {
				contractType = OutputXML.get(i).get(2).toString();
			}
			if (!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3)
					.equals(""))) {
				productType = OutputXML.get(i).get(3).toString();
			}
			if (!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4)
					.equals(""))) {
				role = OutputXML.get(i).get(4).toString();
			}
			if (OutputXML.get(i).get(5) != null
					&& !OutputXML.get(i).get(5).isEmpty()
					&& !OutputXML.get(i).get(5).equals("")
					&& !OutputXML.get(i).get(5).equalsIgnoreCase("null")) {
				lastUpdateDate = OutputXML.get(i).get(5).toString();
			}
			if (OutputXML.get(i).get(6) != null
					&& !OutputXML.get(i).get(6).isEmpty()
					&& !OutputXML.get(i).get(6).equals("")
					&& !OutputXML.get(i).get(6).equalsIgnoreCase("null")) {
				TotAmt = OutputXML.get(i).get(6).toString();
			}
			if (!(OutputXML.get(i).get(7) == null || OutputXML.get(i).get(7)
					.equals(""))) {
				noOfInstalmnt = OutputXML.get(i).get(7).toString();
			}
			if (!(OutputXML.get(i).get(8) == null || OutputXML.get(i).get(8)
					.equals(""))) {
				creditLimit = OutputXML.get(i).get(8).toString();
			}
			if (OutputXML.get(i).get(9) != null
					&& !OutputXML.get(i).get(9).isEmpty()
					&& !OutputXML.get(i).get(9).equals("")
					&& !OutputXML.get(i).get(9).equalsIgnoreCase("null")) {
				noOfDayinPpl = OutputXML.get(i).get(9).toString();
			}

			add_xml_str = add_xml_str + "<ExternalBureauPipelineProducts><applicant_ID>" + cifId + "</applicant_ID>";
			add_xml_str = add_xml_str + "<external_bureau_pipeline_products_id>" + agreementID + "</external_bureau_pipeline_products_id>";
			add_xml_str = add_xml_str + "<ppl_provider_no>" + ProviderNo + "</ppl_provider_no>";
			add_xml_str = add_xml_str + "<ppl_type_of_contract>" + contractType + "</ppl_type_of_contract>";
			add_xml_str = add_xml_str + "<ppl_type_of_product>" + productType + "</ppl_type_of_product>";
			add_xml_str = add_xml_str + "<ppl_phase>" + "PIPELINE" + "</ppl_phase>";
			add_xml_str = add_xml_str + "<ppl_role>" + role + "</ppl_role>";

			add_xml_str = add_xml_str + "<ppl_date_of_last_update>" + lastUpdateDate + "</ppl_date_of_last_update>";
			add_xml_str = add_xml_str + "<ppl_total_amount>" + TotAmt + "</ppl_total_amount>";
			add_xml_str = add_xml_str + "<ppl_no_of_instalments>" + noOfInstalmnt + "</ppl_no_of_instalments>";
			add_xml_str = add_xml_str + "<ppl_credit_limit>" + creditLimit + "</ppl_credit_limit>";

			add_xml_str = add_xml_str + "<ppl_no_of_days_in_pipeline>" + noOfDayinPpl + "</ppl_no_of_days_in_pipeline><company_flag>N</company_flag><consider_for_obligation></consider_for_obligation></ExternalBureauPipelineProducts>"; // to
			// be
			// populated
			// later

		}
		SKLogger_CC.writeLog("RLOSCommon", "Internal liab tag Cration: "
				+ add_xml_str);
		return add_xml_str;
	}

	private static String getMQInputXML(String sessionID, String cabinetName,
			String wi_name, String ws_name, String userName,
			StringBuffer final_xml) {
		FormContext.getCurrentInstance().getFormConfig();

		StringBuffer strBuff = new StringBuffer();
		strBuff.append("<APMQPUTGET_Input>");
		strBuff.append("<SessionId>" + sessionID + "</SessionId>");
		strBuff.append("<EngineName>" + cabinetName + "</EngineName>");
		strBuff.append("<XMLHISTORY_TABLENAME>NG_CC_XMLLOG_HISTORY</XMLHISTORY_TABLENAME>");
		strBuff.append("<WI_NAME>" + wi_name + "</WI_NAME>");
		strBuff.append("<WS_NAME>" + ws_name + "</WS_NAME>");
		strBuff.append("<USER_NAME>" + userName + "</USER_NAME>");
		strBuff.append("<MQ_REQUEST_XML>");
		strBuff.append(final_xml);
		strBuff.append("</MQ_REQUEST_XML>");
		strBuff.append("</APMQPUTGET_Input>");
		SKLogger_CC.writeLog("inside getOutputXMLValues", "getMQInputXML"
				+ strBuff.toString());
		return strBuff.toString();
	}

	private Map<String, String> DEDUP_SUMMARY_Custom(Map<String, String> int_xml, Map<String, String> recordFileMap) {

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String parent_tag = (String) recordFileMap.get("parent_tag_name");
		String tag_name = (String) recordFileMap.get("xmltag_name");

		if (tag_name.equalsIgnoreCase("AddressDetails")
				&& int_xml.containsKey(parent_tag)) {
			String xml_str = int_xml.get(parent_tag);
			SKLogger_CC.writeLog("RLOS COMMON", " before adding address+ " + xml_str);
			xml_str = xml_str + getCustAddress_details();
			SKLogger_CC.writeLog("RLOS COMMON", " after adding address+ " + xml_str);
			int_xml.put(parent_tag, xml_str);
		} else if (tag_name.equalsIgnoreCase("MaritalStatus")) {
			String marrital_code = formObject.getNGValue(
			"cmplx_Customer_MAritalStatus").substring(0, 1);
			String xml_str = int_xml.get(parent_tag);
			xml_str = xml_str + "<" + tag_name + ">" + marrital_code + "</" + tag_name + ">";

			SKLogger_CC.writeLog("RLOS COMMON", " after adding Minor flag+ " + xml_str);
			int_xml.put(parent_tag, xml_str);
		}
		return int_xml;
	}

	private Map<String, String> Blacklist_Details_custom(Map<String, String> int_xml, Map<String, String> recordFileMap) {

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String parent_tag = (String) recordFileMap.get("parent_tag_name");
		String tag_name = (String) recordFileMap.get("xmltag_name");

		if (tag_name.equalsIgnoreCase("AddressDetails")
				&& int_xml.containsKey(parent_tag)) {
			String xml_str = int_xml.get(parent_tag);
			SKLogger_CC.writeLog("RLOS COMMON", " before adding address+ " + xml_str);
			xml_str = xml_str + getCustAddress_details();
			SKLogger_CC.writeLog("RLOS COMMON", " after adding address+ " + xml_str);
			int_xml.put(parent_tag, xml_str);
		} else if (tag_name.equalsIgnoreCase("MaritalStatus")) {
			String marrital_code = formObject.getNGValue(
			"cmplx_Customer_MAritalStatus").substring(0, 1);
			String xml_str = int_xml.get(parent_tag);
			xml_str = xml_str + "<" + tag_name + ">" + marrital_code + "</" + tag_name + ">";

			SKLogger_CC.writeLog("RLOS COMMON", " after adding Minor flag+ " + xml_str);
			int_xml.put(parent_tag, xml_str);
		}
		return int_xml;
	}

	private Map<String, String> NEW_CUSTOMER_Custom(Map<String, String> int_xml, Map<String, String> recordFileMap) {

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String parent_tag = (String) recordFileMap.get("parent_tag_name");
		String tag_name = (String) recordFileMap.get("xmltag_name");
		
		if (tag_name.equalsIgnoreCase("AddressDetails")
				&& int_xml.containsKey(parent_tag)) {
			String xml_str = int_xml.get(parent_tag);
			SKLogger_CC.writeLog("RLOS COMMON", " before adding address+ " + xml_str);
			xml_str = xml_str + getCustAddress_details();
			SKLogger_CC.writeLog("RLOS COMMON", " after adding address+ " + xml_str);
			int_xml.put(parent_tag, xml_str);
		} else if (tag_name.equalsIgnoreCase("MinorFlag")
				&& parent_tag.equalsIgnoreCase("PersonDetails")) {
			if (int_xml.containsKey(parent_tag)) {
				float Age = Float.parseFloat(formObject.getNGValue("cmplx_Customer_age"));
				String age_flag = "N";
				if (Age < 18)
					age_flag = "Y";
				String xml_str = int_xml.get(parent_tag);
				xml_str = xml_str + "<" + tag_name + ">" + age_flag + "</" + tag_name + ">";

				SKLogger_CC.writeLog("RLOS COMMON"," after adding Minor flag+ " + xml_str);
				int_xml.put(parent_tag, xml_str);
			}
		} else if (tag_name.equalsIgnoreCase("NonResidentFlag")
				&& parent_tag.equalsIgnoreCase("PersonDetails")) {
			if (int_xml.containsKey(parent_tag)) {
				String xml_str = int_xml.get(parent_tag);
				String res_flag = "N";

				if (formObject.getNGValue("cmplx_Customer_ResidentNonResident").equalsIgnoreCase("Resident")) {
					res_flag = "Y";
				}

				xml_str = xml_str + "<" + tag_name + ">" + res_flag + "</" + tag_name + ">";

				SKLogger_CC.writeLog("RLOS COMMON", " after adding res_flag+ " + xml_str);
				int_xml.put(parent_tag, xml_str);
			}
		}
		return int_xml;
	}

	private Map<String, String> CUSTOMER_UPDATE_Custom(Map<String, String> int_xml,Map<String, String> recordFileMap) {
		
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String Call_name = (String) recordFileMap.get("Call_name");
		String form_control = (String) recordFileMap.get("form_control");
		String parent_tag = (String) recordFileMap.get("parent_tag_name");
		String tag_name = (String) recordFileMap.get("xmltag_name");
		
		if (tag_name.equalsIgnoreCase("OECDDet")&& int_xml.containsKey(parent_tag)) {
			SKLogger_CC.writeLog("inside 1st if",
			"inside customer update req2123");
			String xml_str = int_xml.get(parent_tag);
			SKLogger_CC.writeLog("RLOS COMMON", " before adding OECD+ " + xml_str);
			xml_str = xml_str + CC_Comn.getCustOECD_details(Call_name);
			SKLogger_CC.writeLog("RLOS COMMON", " after adding OeCD+ " + xml_str);
			int_xml.put(parent_tag, xml_str);
		} else if (tag_name.equalsIgnoreCase("PhnLocalCode")) {
			SKLogger_CC.writeLog("inside PL Common generate xml",
			"PhnLocalCode to substring");
			String xml_str = int_xml.get(parent_tag);
			String phn_no = formObject.getNGValue(form_control);
			if ((!phn_no.equalsIgnoreCase("")) && phn_no.indexOf("00971") > -1) {
				phn_no = phn_no.substring(5);
			}

			xml_str = xml_str + "<" + tag_name + ">" + phn_no + "</" + tag_name + ">";

			SKLogger_CC.writeLog("PL COMMON", " after adding ApplicationID:  " + xml_str);
			int_xml.put(parent_tag, xml_str);
		} else if (tag_name.equalsIgnoreCase("AddrDet")) {
			SKLogger_CC.writeLog("inside 1st if", "inside customer update req1");
			if (int_xml.containsKey(parent_tag)) {
				SKLogger_CC.writeLog("inside 1st if","inside customer update req2");
				String xml_str = int_xml.get(parent_tag);
				SKLogger_CC.writeLog("RLOS COMMON", " before adding address+ " + xml_str);
				xml_str = xml_str + CC_Comn.getCustAddress_details(Call_name);
				SKLogger_CC.writeLog("RLOS COMMON", " after adding address+ " + xml_str);
				int_xml.put(parent_tag, xml_str);
			}
		}

		return int_xml;

	}

	private Map<String, String> NEW_CARD_Custom(Map<String, String> int_xml, Map<String, String> recordFileMap) {
		String parent_tag = (String) recordFileMap.get("parent_tag_name");
		String tag_name = (String) recordFileMap.get("xmltag_name");

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		if (tag_name.equalsIgnoreCase("VIPFlg")) {
			String vip_flag = "N";
			if (formObject.getNGValue("cmplx_Customer_VIPFlag")
					.equalsIgnoreCase("true")) {
				vip_flag = "Y";
			}
			String xml_str = int_xml.get(parent_tag);
			xml_str = xml_str + "<" + tag_name + ">" + vip_flag + "</" + tag_name + ">";

			SKLogger_CC.writeLog("RLOS COMMON", " after adding VIP flag+ " + xml_str);
			int_xml.put(parent_tag, xml_str);
		} else if (tag_name.equalsIgnoreCase("ProcessingUserId")) {
			String xml_str = int_xml.get(parent_tag);
			xml_str = xml_str + "<" + tag_name + ">" + formObject.getUserName() + "</" + tag_name + ">";

			SKLogger_CC.writeLog("RLOS COMMON", " after adding Minor flag+ " + xml_str);
			int_xml.put(parent_tag, xml_str);
		} else if (tag_name.equalsIgnoreCase("ProcessingDate")) {
			String xml_str = int_xml.get(parent_tag);
			SimpleDateFormat sdf1 = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss.mmm+hh:mm");
			xml_str = xml_str + "<" + tag_name + ">" + sdf1.format(new Date()) + "</" + tag_name + ">";

			SKLogger_CC.writeLog("RLOS COMMON", " after adding Minor flag+ " + xml_str);
			int_xml.put(parent_tag, xml_str);
		}
		return int_xml;
	}

	private Map<String, String> DECTECH_Custom(Map<String, String> int_xml, Map<String, String> recordFileMap) {
		String parent_tag = (String) recordFileMap.get("parent_tag_name");
		String tag_name = (String) recordFileMap.get("xmltag_name");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String emp_type = "";
		int prod_count = formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if (prod_count != 0) {
			emp_type = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0, 6) == null ? "" : formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 6);
		}

		if (tag_name.equalsIgnoreCase("Channel")) {
			SKLogger_CC.writeLog("RLOS COMMON", " iNSIDE channelcode+ ");

			String ReqProd = formObject.getNGValue(
					"cmplx_Product_cmplx_ProductGrid", 0, 1);

			String xml_str = int_xml.get(parent_tag);
			xml_str = "<" + tag_name + ">" + (ReqProd.equalsIgnoreCase("Personal Loan") ? "PL" : "CC") + "</" + tag_name + ">";

			SKLogger_CC.writeLog("RLOS COMMON", " after adding channelcode+ " + xml_str);
			int_xml.put(parent_tag, xml_str);

		}

		else if (tag_name.equalsIgnoreCase("emp_type")) {
			SKLogger_CC.writeLog("RLOS COMMON", " iNSIDE channelcode+ ");

			String empttype = formObject.getNGValue(
					"cmplx_Product_cmplx_ProductGrid", 0, 6);
			if (empttype != null) {
				if (empttype.equalsIgnoreCase("Salaried")) {
					empttype = "S";
				} else if (empttype.equalsIgnoreCase("Salaried Pensioner")) {
					empttype = "SP";
				} else {
					empttype = "SE";
				}
			}
			String xml_str = int_xml.get(parent_tag);
			xml_str = xml_str + "<" + tag_name + ">" + empttype + "</" + tag_name + ">";

			SKLogger_CC.writeLog("RLOS COMMON", " after adding channelcode+ " + xml_str);
			int_xml.put(parent_tag, xml_str);

		} else if (tag_name.equalsIgnoreCase("world_check")) {
			SKLogger_CC.writeLog("RLOS COMMON", " iNSIDE world_check+ ");

			String world_check = formObject.getNGValue("IS_WORLD_CHECK");
			SKLogger_CC.writeLog("RLOS COMMON"," iNSIDE world_check+ "+ formObject					.getLVWRowCount("cmplx_WorldCheck_WorldCheck_Grid"));
			if (formObject.getLVWRowCount("cmplx_WorldCheck_WorldCheck_Grid") == 0) {
				world_check = "Negative";
			} else {
				world_check = "Positive";
			}

			String xml_str = int_xml.get(parent_tag);
			xml_str = xml_str + "<" + tag_name + ">" + world_check + "</" + tag_name + ">";

			SKLogger_CC.writeLog("RLOS COMMON", " after adding world_check+ " + xml_str);
			int_xml.put(parent_tag, xml_str);

		}

		else if (tag_name.equalsIgnoreCase("prev_loan_dbr")) {
			SKLogger_CC.writeLog("RLOS COMMON", " iNSIDE prev_loan_dbr+ ");
			String PreviousLoanDBR = "";
			String PreviousLoanEmp = "";
			String PreviousLoanMultiple = "";
			String PreviousLoanTAI = "";

			String squeryloan = "select PreviousLoanDBR,PreviousLoanEmp,PreviousLoanMultiple,PreviousLoanTAI from ng_RLOS_CUSTEXPOSE_LoanDetails where Request_Type='CollectionsSummary' and Limit_Increase='true' and Child_Wi= '" + formObject.getWFWorkitemName() + "'";
			List<List<String>> prevLoan = formObject
			.getDataFromDataSource(squeryloan);
			SKLogger_CC.writeLog("RLOS COMMON", " iNSIDE prev_loan_dbr+ " + squeryloan);

			if (prevLoan != null && prevLoan.size() > 0) {
				PreviousLoanDBR = prevLoan.get(0).get(0);
				PreviousLoanEmp = prevLoan.get(0).get(1);
				PreviousLoanMultiple = prevLoan.get(0).get(2);
				PreviousLoanTAI = prevLoan.get(0).get(3);
			}

			String xml_str = int_xml.get(parent_tag);
			xml_str = xml_str + "<" + tag_name + ">" + PreviousLoanDBR + "</" + tag_name + "><prev_loan_tai>" + PreviousLoanTAI + "</prev_loan_tai><prev_loan_multiple>" + PreviousLoanMultiple + "</prev_loan_multiple><prev_loan_amount></prev_loan_amount><prev_loan_employer>" + PreviousLoanEmp + "</prev_loan_employer>";

			SKLogger_CC.writeLog("RLOS COMMON", " after adding world_check+ " + xml_str);
			int_xml.put(parent_tag, xml_str);

		}

		else if (tag_name.equalsIgnoreCase("no_of_cheque_bounce_int_3mon_Ind")) {
			SKLogger_CC.writeLog("RLOS COMMON",
			" iNSIDE no_of_cheque_bounce_int_3mon_Ind+ ");
			String squerynoc = "SELECT count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-3,GETDATE()) and returntype='ICCS' and Child_Wi='" + formObject.getWFWorkitemName() + "'";
			List<List<String>> NOC = formObject
			.getDataFromDataSource(squerynoc);
			if (NOC != null && NOC.size() > 0) {
				String xml_str = int_xml.get(parent_tag);
				xml_str = xml_str + "<" + tag_name + ">" + NOC.get(0).get(0) + "</" + tag_name + ">";

				SKLogger_CC.writeLog("RLOS COMMON"," after adding internal_blacklist+ " + xml_str);
				int_xml.put(parent_tag, xml_str);

			}

		} else if (tag_name.equalsIgnoreCase("no_of_DDS_return_int_3mon_Ind")) {
			SKLogger_CC.writeLog("RLOS COMMON",
			" iNSIDE no_of_cheque_bounce_int_3mon_Ind+ ");
			String squerynoc = "SELECT count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-3,GETDATE()) and returntype='DDS' and Child_Wi='" + formObject.getWFWorkitemName() + "'";
			List<List<String>> NOC = formObject
			.getDataFromDataSource(squerynoc);
			if (NOC != null && NOC.size() > 0) {
				String xml_str = int_xml.get(parent_tag);
				xml_str = xml_str + "<" + tag_name + ">" + NOC.get(0).get(0) + "</" + tag_name + ">";

				SKLogger_CC.writeLog("RLOS COMMON"," after adding internal_blacklist+ " + xml_str);
				int_xml.put(parent_tag, xml_str);

			}

		} else if (tag_name.equalsIgnoreCase("blacklist_cust_type")) {
			SKLogger_CC.writeLog("RLOS COMMON", " iNSIDE channelcode+ ");
			String ParentWI_Name = formObject.getNGValue("Parent_WIName");
			String squeryBlacklist = "select BlacklistFlag,BlacklistDate,BlacklistReasonCode,NegatedFlag,NegatedDate,NegatedReasonCode from ng_rlos_cif_detail where cif_wi_name='" + ParentWI_Name + "' and cif_searchType = 'Internal'";
			List<List<String>> Blacklist = formObject
			.getDataFromDataSource(squeryBlacklist);
			String internal_blacklist = "";
			String internal_blacklist_date = "";
			String internal_blacklist_code = "";
			String internal_negative_flag = "";
			String internal_negative_date = "";
			String internal_negative_code = "";

			if (Blacklist != null && Blacklist.size() > 0) {
				internal_blacklist = Blacklist.get(0).get(0);
				internal_blacklist_date = Blacklist.get(0).get(1);
				internal_blacklist_code = Blacklist.get(0).get(2);
				internal_negative_flag = Blacklist.get(0).get(3);
				internal_negative_date = Blacklist.get(0).get(4);
				internal_negative_code = Blacklist.get(0).get(5);
			}
			String xml_str = int_xml.get(parent_tag);
			xml_str = xml_str + "<" + tag_name + ">I</" + tag_name + "><internal_blacklist>" + internal_blacklist + "</internal_blacklist><internal_blacklist_date>" + internal_blacklist_date + "</internal_blacklist_date><internal_blacklist_code>" + internal_blacklist_code + "</internal_blacklist_code><negative_cust_type>I</negative_cust_type><internal_negative_flag>" + internal_negative_flag + "</internal_negative_flag><internal_negative_date>" + internal_negative_date + "</internal_negative_date><internal_negative_code>" + internal_negative_code + "</internal_negative_code>";

			SKLogger_CC.writeLog("RLOS COMMON",
					" after adding internal_blacklist+ " + xml_str);
			int_xml.put(parent_tag, xml_str);

		} else if (((tag_name.equalsIgnoreCase("lob"))
				|| (tag_name.equalsIgnoreCase("target_segment_code"))
				|| (tag_name.equalsIgnoreCase("designation"))
				|| (tag_name.equalsIgnoreCase("emp_name"))
				|| (tag_name.equalsIgnoreCase("industry_sector"))
				|| (tag_name.equalsIgnoreCase("industry_marco"))
				|| (tag_name.equalsIgnoreCase("industry_micro"))
				|| (tag_name.equalsIgnoreCase("bvr"))
				|| (tag_name.equalsIgnoreCase("eff_date_estba"))
				|| (tag_name.equalsIgnoreCase("poa"))
				|| (tag_name.equalsIgnoreCase("tlc_issue_date"))
				|| (tag_name.equalsIgnoreCase("cc_employer_status"))
				|| (tag_name.equalsIgnoreCase("pl_employer_status")) || (tag_name.equalsIgnoreCase("marketing_code")))
				&& emp_type.equalsIgnoreCase("Self Employed")) {
			SKLogger_CC.writeLog("RLOSCommon java file",
			"inside getProduct_details : ");
			String xml_str = int_xml.get(parent_tag);
			int Comp_row_count = formObject
			.getLVWRowCount("cmplx_CompanyDetails_cmplx_CompanyGrid");
			String lob = "";
			String target_segment_code = "";
			String designation = "";
			String emp_name = "";
			String industry_sector = "";
			String eff_date_estba = "";
			String industry_marco = "";
			String industry_micro = "";
			String bvr = "";
			String poa = "";
			String marketing_code = "";

			for (int i = 0; i < Comp_row_count; i++) {
				lob = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 15); // 0
				target_segment_code = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 25); // 0
				designation = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 11); // 0
				emp_name = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 0); // 0
				// industry_sector =
				// formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",
				// i, 8); //0
				eff_date_estba = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 14); // 0
				industry_sector = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 8); // 0
				industry_marco = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 9); // 0
				industry_micro = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 10); // 0
				bvr = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 19); // 0
				poa = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 20); // 0
				marketing_code = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 27); // 0

			}
			if (tag_name.equalsIgnoreCase("lob")) {
				xml_str = xml_str + "<" + tag_name + ">" + lob + "</" + tag_name + ">";
			} else if (tag_name.equalsIgnoreCase("target_segment_code")) {
				xml_str = xml_str + "<" + tag_name + ">" + target_segment_code + "</" + tag_name + ">";
			} else if (tag_name.equalsIgnoreCase("designation")) {
				xml_str = xml_str + "<" + tag_name + ">" + designation + "</" + tag_name + ">";
			} else if (tag_name.equalsIgnoreCase("emp_name")) {
				xml_str = xml_str + "<" + tag_name + ">" + emp_name + "</" + tag_name + ">";
			} else if (tag_name.equalsIgnoreCase("industry_sector")) {
				xml_str = xml_str + "<" + tag_name + ">" + industry_sector + "</" + tag_name + ">";
			} else if (tag_name.equalsIgnoreCase("industry_marco")) {
				xml_str = xml_str + "<" + tag_name + ">" + industry_marco + "</" + tag_name + ">";
			} else if (tag_name.equalsIgnoreCase("industry_micro")) {
				xml_str = xml_str + "<" + tag_name + ">" + industry_micro + "</" + tag_name + ">";
			} else if (tag_name.equalsIgnoreCase("bvr")) {
				xml_str = xml_str + "<" + tag_name + ">" + bvr + "</" + tag_name + ">";
			} else if (tag_name.equalsIgnoreCase("eff_date_estba")) {
				xml_str = xml_str + "<" + tag_name + ">" + eff_date_estba + "</" + tag_name + ">";
			} else if (tag_name.equalsIgnoreCase("poa")) {
				xml_str = xml_str + "<" + tag_name + ">" + poa + "</" + tag_name + ">";
			} else if (tag_name.equalsIgnoreCase("cc_employer_status")) {
				xml_str = xml_str + "<" + tag_name + ">" + bvr + "</" + tag_name + ">";
			} else if (tag_name.equalsIgnoreCase("tlc_issue_date")) {
				xml_str = xml_str + "<" + tag_name + ">" + eff_date_estba + "</" + tag_name + ">";
			} else if (tag_name.equalsIgnoreCase("pl_employer_status")) {
				xml_str = xml_str + "<" + tag_name + ">" + poa + "</" + tag_name + ">";
			} else if (tag_name.equalsIgnoreCase("marketing_code")) {
				xml_str = xml_str + "<" + tag_name + ">" + marketing_code + "</" + tag_name + ">";
			}
			SKLogger_CC.writeLog("RLOS COMMON",
					" after adding cmplx_CompanyDetails_cmplx_CompanyGrid+ "+ xml_str);
			int_xml.put(parent_tag, xml_str);
		} else if ((tag_name.equalsIgnoreCase("auth_sig_sole_emp") || tag_name.equalsIgnoreCase("shareholding_perc"))
				&& emp_type.equalsIgnoreCase("Self Employed")) {
			SKLogger_CC.writeLog("RLOS COMMON", " iNSIDE channelcode+ ");
			String auth_sig_sole_emp = "";
			String shareholding_perc = "";
			int Authsign_row_count = formObject
			.getLVWRowCount("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails");
			if (Authsign_row_count != 0) {
				auth_sig_sole_emp = (formObject.getNGValue("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails",0, 10).equalsIgnoreCase("Yes") ? "Y" : "N"); // 0
				shareholding_perc = formObject.getNGValue("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails",0, 9); // 0

			}
			String xml_str = int_xml.get(parent_tag);
			if (tag_name.equalsIgnoreCase("auth_sig_sole_emp")) {
				xml_str = xml_str + "<" + tag_name + ">" + auth_sig_sole_emp + "</" + tag_name + ">";
			} else if (tag_name.equalsIgnoreCase("shareholding_perc")) {
				xml_str = xml_str + "<" + tag_name + ">" + shareholding_perc + "</" + tag_name + ">";
			}

			SKLogger_CC.writeLog("RLOS COMMON",
					" after adding shareholding_perc+ " + xml_str);
			int_xml.put(parent_tag, xml_str);

		} else if (tag_name.equalsIgnoreCase("external_blacklist_flag")
				|| tag_name.equalsIgnoreCase("external_blacklist_date")
				|| tag_name.equalsIgnoreCase("external_blacklist_code")) {
			SKLogger_CC.writeLog("RLOS COMMON", " iNSIDE channelcode+ ");
			String ParentWI_Name = formObject.getNGValue("Parent_WIName");
			String squeryBlacklist = "select BlacklistFlag,BlacklistDate,BlacklistReasonCode from ng_rlos_cif_detail where (cif_wi_name='" + ParentWI_Name + "' or cif_wi_name='" + formObject.getWFWorkitemName() + "') and cif_searchType = 'External'";
			SKLogger_CC.writeLog("RLOS COMMON", " iNSIDE channelcode+ " + squeryBlacklist);
			List<List<String>> Blacklist = formObject
			.getDataFromDataSource(squeryBlacklist);
			String External_blacklist_date = "";
			String External_blacklist_code = "";

			if (Blacklist != null && Blacklist.size() > 0) {
				External_blacklist_date = Blacklist.get(0).get(1);
				External_blacklist_code = Blacklist.get(0).get(2);
			}
			String xml_str = int_xml.get(parent_tag);
			if (tag_name.equalsIgnoreCase("external_blacklist_flag")) {
				xml_str = xml_str + "<" + tag_name + ">I</" + tag_name + ">";
			} else if (tag_name.equalsIgnoreCase("external_blacklist_date")) {
				xml_str = xml_str + "<" + tag_name + ">" + External_blacklist_date + "</" + tag_name + ">";
			} else if (tag_name.equalsIgnoreCase("external_blacklist_code")) {
				xml_str = xml_str + "<" + tag_name + ">" + External_blacklist_code + "</" + tag_name + ">";
			}

			SKLogger_CC.writeLog("RLOS COMMON",
					" after adding internal_blacklist+ " + xml_str);
			int_xml.put(parent_tag, xml_str);

		} else if (tag_name.equalsIgnoreCase("ApplicationDetails")) {
			SKLogger_CC.writeLog("inside 1st if", "inside DECTECH req1");

			SKLogger_CC.writeLog("inside 1st if", "inside customer update req2");
			String xml_str = int_xml.get(parent_tag);
			SKLogger_CC.writeLog("RLOS COMMON", " before adding product+ " + xml_str);
			xml_str = xml_str + CC_Comn.getProduct_details();
			SKLogger_CC.writeLog("RLOS COMMON", " after adding product+ " + xml_str);
			int_xml.put(parent_tag, xml_str);

		} else if (tag_name.equalsIgnoreCase("cust_name")) {
			if (int_xml.containsKey(parent_tag)) {
				String first_name = formObject.getNGValue("cmplx_Customer_FIrstNAme");
				String middle_name = formObject.getNGValue("cmplx_Customer_MiddleName");
				String last_name = formObject.getNGValue("cmplx_Customer_LAstNAme");

				String full_name = first_name + " " + middle_name + "" + last_name;

				String xml_str = int_xml.get(parent_tag);
				xml_str = xml_str + "<" + tag_name + ">" + full_name + "</" + tag_name + ">";

				SKLogger_CC.writeLog("RLOS COMMON"," after adding confirmedinjob+ " + xml_str);
				int_xml.put(parent_tag, xml_str);

			}
		}

		else if (tag_name.equalsIgnoreCase("ref_phone_no")) {
			if (int_xml.containsKey(parent_tag)) {
				SKLogger_CC.writeLog("RLOS COMMON", " INSIDE ref_phone_no+ ");
				int count = formObject.getLVWRowCount("cmplx_RefDetails_cmplx_Gr_ReferenceDetails");
				String ref_phone_no = "";
				String ref_relationship = "";
				SKLogger_CC.writeLog("RLOS COMMON", " INSIDE ref_phone_no+ " + count);
				if (count != 0) {
					ref_phone_no = formObject.getNGValue("cmplx_RefDetails_cmplx_Gr_ReferenceDetails", 0, 4);
					ref_relationship = formObject.getNGValue("cmplx_RefDetails_cmplx_Gr_ReferenceDetails", 0, 2);
					SKLogger_CC.writeLog("RLOS COMMON"," after adding ref_phone_no+ " + ref_phone_no);
					SKLogger_CC.writeLog("RLOS COMMON"," after adding ref_relationship+ "+ ref_relationship);
				}

				String xml_str = int_xml.get(parent_tag);
				xml_str = xml_str + "<" + tag_name + ">" + ref_phone_no + "</" + tag_name + "><ref_relationship>" + ref_relationship + "</ref_relationship>";

				SKLogger_CC.writeLog("RLOS COMMON"," after adding confirmedinjob+ " + xml_str);
				int_xml.put(parent_tag, xml_str);

			}
		}

		else if (tag_name.equalsIgnoreCase("confirmed_in_job")) {
			if (int_xml.containsKey(parent_tag)) {
				String confirmedinjob = formObject.getNGValue("cmplx_EmploymentDetails_JobConfirmed");

				if (confirmedinjob != null) {
					if (confirmedinjob.equalsIgnoreCase("true")) {confirmedinjob = "Y";
					} else {confirmedinjob = "N";
					}

					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<" + tag_name + ">" + confirmedinjob+ "</" + tag_name + ">";

					SKLogger_CC.writeLog("RLOS COMMON"," after adding confirmedinjob+ " + xml_str);
					int_xml.put(parent_tag, xml_str);
				}
			}
		} else if (tag_name.equalsIgnoreCase("included_pl_aloc")) {
			if (int_xml.containsKey(parent_tag)) {
				String included_pl_aloc = formObject.getNGValue("cmplx_EmploymentDetails_IncInPL");

				if (included_pl_aloc != null) {
					if (included_pl_aloc.equalsIgnoreCase("true")) {included_pl_aloc = "Y";
					} else {included_pl_aloc = "N";
					}

					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<" + tag_name + ">" + included_pl_aloc+ "</" + tag_name + ">";

					SKLogger_CC.writeLog("RLOS COMMON"," after adding included_pl_aloc+ " + xml_str);
					int_xml.put(parent_tag, xml_str);
				}
			}
		} else if (tag_name.equalsIgnoreCase("included_cc_aloc")) {
			if (int_xml.containsKey(parent_tag)) {
				String included_cc_aloc = formObject.getNGValue("cmplx_EmploymentDetails_IncInCC");

				if (included_cc_aloc != null) {
					if (included_cc_aloc.equalsIgnoreCase("true")) {included_cc_aloc = "Y";
					} else {included_cc_aloc = "N";
					}

					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<" + tag_name + ">" + included_cc_aloc+ "</" + tag_name + ">";

					SKLogger_CC.writeLog("RLOS COMMON"," after adding cmplx_EmploymentDetails_IncInCC+ "+ xml_str);
					int_xml.put(parent_tag, xml_str);
				}
			}
		} else if (tag_name.equalsIgnoreCase("vip_flag")) {
			if (int_xml.containsKey(parent_tag)) {
				String vip_flag = formObject.getNGValue("cmplx_Customer_VIPFlag");

				if (vip_flag.equalsIgnoreCase("true")) {
					vip_flag = "Y";
				} else {
					vip_flag = "N";
				}

				String xml_str = int_xml.get(parent_tag);
				xml_str = xml_str + "<" + tag_name + ">" + vip_flag + "</" + tag_name + ">";

				SKLogger_CC.writeLog("RLOS COMMON"," after adding cmplx_Customer_VIPFlag+ " + xml_str);
				int_xml.put(parent_tag, xml_str);
			}
		} else if (tag_name.equalsIgnoreCase("standing_instruction")) {
			SKLogger_CC.writeLog("RLOS COMMON",
			" iNSIDE standing_instruction+ ");
			String squerynoc = "SELECT count(*) FROM ng_rlos_FinancialSummary_SiDtls WHERE Child_wi='" + formObject.getWFWorkitemName() + "'";
			List<List<String>> NOC = formObject
			.getDataFromDataSource(squerynoc);
			SKLogger_CC.writeLog("RLOS COMMON",
					" after adding cmplx_Customer_VIPFlag+ " + squerynoc);
			String standing_instruction = "";
			standing_instruction = NOC.get(0).get(0);
			if (NOC != null && NOC.size() > 0) {
				String xml_str = int_xml.get(parent_tag);
				if (standing_instruction.equalsIgnoreCase("0")) {
					standing_instruction = "N";
				} else {
					standing_instruction = "Y";
				}

				xml_str = xml_str + "<" + tag_name + ">" + standing_instruction + "</" + tag_name + ">";

				SKLogger_CC.writeLog("RLOS COMMON"," after adding standing_instruction+ " + xml_str);
				int_xml.put(parent_tag, xml_str);
			}
		} else if (tag_name.equalsIgnoreCase("accomodation_provided")) {
			if (int_xml.containsKey(parent_tag)) {
				String accomodation_provided = formObject.getNGValue("cmplx_IncomeDetails_Accomodation")!=null &&
					formObject.getNGValue("cmplx_IncomeDetails_Accomodation").equalsIgnoreCase("yes")?"Y":"N";

					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<" + tag_name + ">"+ accomodation_provided + "</" + tag_name + ">";

					SKLogger_CC.writeLog("RLOS COMMON"," after adding confirmedinjob+ " + xml_str);
					int_xml.put(parent_tag, xml_str);
				
			}
		} else if (tag_name.equalsIgnoreCase("AccountDetails")) {
			if (int_xml.containsKey(parent_tag)) {
				String xml_str = int_xml.get(parent_tag);
				SKLogger_CC.writeLog("RLOS COMMON"," before adding internal liability+ " + xml_str);
				xml_str = xml_str + CC_Comn.getInternalLiabDetails();
				SKLogger_CC.writeLog("RLOS COMMON"," after internal liability+ " + xml_str);
				int_xml.put(parent_tag, xml_str);
			}

		} else if (tag_name.equalsIgnoreCase("InternalBureau")) {

			String xml_str = int_xml.get(parent_tag);
			SKLogger_CC.writeLog("RLOS COMMON"," before adding InternalBureauData+ " + xml_str);
			String temp = CC_Comn.InternalBureauData();
			if (!temp.equalsIgnoreCase("")) {
				if (xml_str == null) {
					SKLogger_CC.writeLog("RLOS COMMON", " before adding bhrabc"+ xml_str);
					xml_str = "";
				}
				xml_str = xml_str + temp;
				SKLogger_CC.writeLog("RLOS COMMON"," after InternalBureauData+ " + xml_str);
				int_xml.get(parent_tag);
				int_xml.put(parent_tag, xml_str);
			}

		} else if (tag_name.equalsIgnoreCase("InternalBouncedCheques")) {

			String xml_str = int_xml.get(parent_tag);
			SKLogger_CC.writeLog("RLOS COMMON"," before adding InternalBouncedCheques+ " + xml_str);
			String temp = InternalBouncedCheques();
			if (!temp.equalsIgnoreCase("")) {
				if (xml_str == null) {
					SKLogger_CC.writeLog("RLOS COMMON", " before adding bhrabc"+ xml_str);
					xml_str = "";
				}
				xml_str = xml_str + temp;
				SKLogger_CC.writeLog("RLOS COMMON"," after InternalBouncedCheques+ " + xml_str);
				int_xml.put(parent_tag, xml_str);
			}

		} else if (tag_name.equalsIgnoreCase("InternalBureauIndividualProducts")) {

			String xml_str = int_xml.get(parent_tag);
			SKLogger_CC.writeLog("RLOS COMMON"," before adding InternalBureauIndividualProducts+ "+ xml_str);
			String temp = InternalBureauIndividualProducts();
			if (!temp.equalsIgnoreCase("")) {
				if (xml_str == null) {
					SKLogger_CC.writeLog("RLOS COMMON", " before adding bhrabc"+ xml_str);
					xml_str = "";
				}
				xml_str = xml_str + temp;
				SKLogger_CC.writeLog("RLOS COMMON"," after InternalBureauIndividualProducts+ " + xml_str);
				int_xml.put(parent_tag, xml_str);
			}

		} else if (tag_name.equalsIgnoreCase("InternalBureauPipelineProducts")) {

			String xml_str = int_xml.get(parent_tag);
			SKLogger_CC.writeLog("RLOS COMMON"," before adding InternalBureauPipelineProducts+ "+ xml_str);
			String temp = InternalBureauPipelineProducts();
			if (!temp.equalsIgnoreCase("")) {
				if (xml_str == null) {
					SKLogger_CC.writeLog("RLOS COMMON", " before adding bhrabc"+ xml_str);
					xml_str = "";
				}
				xml_str = xml_str + temp;
				SKLogger_CC.writeLog("RLOS COMMON"," after InternalBureauPipelineProducts+ " + xml_str);
				int_xml.put(parent_tag, xml_str);
			}

		} else if (tag_name.equalsIgnoreCase("ExternalBureau")) {

			String xml_str = int_xml.get(parent_tag);
			SKLogger_CC.writeLog("RLOS COMMON",
					" before adding ExternalBureau+ " + xml_str);
			String temp = ExternalBureauData();
			if (!temp.equalsIgnoreCase("")) {
				if (xml_str == null) {
					SKLogger_CC.writeLog("RLOS COMMON", " before adding bhrabc"+ xml_str);
					xml_str = "";
				}
				xml_str = xml_str + temp;
				SKLogger_CC.writeLog("RLOS COMMON", " after ExternalBureau+ " + xml_str);
				int_xml.put(parent_tag, xml_str);
			}

		} else if (tag_name.equalsIgnoreCase("ExternalBouncedCheques")) {

			String xml_str = int_xml.get(parent_tag);
			SKLogger_CC.writeLog("RLOS COMMON"," before adding ExternalBouncedCheques+ " + xml_str);
			String temp = ExternalBouncedCheques();
			if (!temp.equalsIgnoreCase("")) {
				if (xml_str == null) {
					SKLogger_CC.writeLog("RLOS COMMON", " before adding bhrabc"+ xml_str);
					xml_str = "";
				}
				xml_str = xml_str + temp;
				SKLogger_CC.writeLog("RLOS COMMON"," after ExternalBouncedCheques+ " + xml_str);
				int_xml.put(parent_tag, xml_str);
			}
		} else if (tag_name.equalsIgnoreCase("ExternalBureauIndividualProducts")) {

			String xml_str = int_xml.get(parent_tag);
			String temp = ExternalBureauIndividualProducts();
			SKLogger_CC.writeLog("RLOS COMMON"," value of temp to be adding temp+ " + temp);
			String Manual_add_Liab = ExternalBureauManualAddIndividualProducts();

			if ((!temp.equalsIgnoreCase(""))|| (!Manual_add_Liab.equalsIgnoreCase(""))) {
				if (xml_str == null) {
					SKLogger_CC.writeLog("RLOS COMMON", " before adding bhrabc"+ xml_str);
					xml_str = "";
				}
				xml_str = xml_str + temp + Manual_add_Liab;
				SKLogger_CC.writeLog("RLOS COMMON"," after ExternalBureauIndividualProducts+ " + xml_str);
				int_xml.put(parent_tag, xml_str);
			}
		} else if (tag_name.equalsIgnoreCase("ExternalBureauPipelineProducts")) {

			String xml_str = int_xml.get(parent_tag);
			SKLogger_CC.writeLog("RLOS COMMON"," before adding ExternalBureauPipelineProducts+ "+ xml_str);
			String temp = ExternalBureauPipelineProducts();
			if (!temp.equalsIgnoreCase("")) {
				if (xml_str == null) {
					SKLogger_CC.writeLog("RLOS COMMON", " before adding bhrabc"+ xml_str);
					xml_str = "";
				}
				xml_str = xml_str + temp;
				SKLogger_CC.writeLog("RLOS COMMON"," after ExternalBureauPipelineProducts+ " + xml_str);
				int_xml.put(parent_tag, xml_str);
			}
		}
		return int_xml;
	}

	private Map<String, String> CHEQUE_BOOK_ELIGIBILITY_Custom( Map<String, String> int_xml, Map<String, String> recordFileMap) {
		String parent_tag = (String) recordFileMap.get("parent_tag_name");
		String tag_name = (String) recordFileMap.get("xmltag_name");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		if (tag_name.equalsIgnoreCase("RecipientAddress")) {
			int add_len = formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
			String add_res_val = "";
			String xml_str = int_xml.get(parent_tag);
			if (add_len > 0) {
				for (int i = 0; i < add_len; i++) {
					SKLogger_CC.writeLog("selecting Emirates of residence: ",formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i, 0));
					if (formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0).equalsIgnoreCase("Home")) {formObject.setNGValue("cmplx_Customer_EmirateOfResidence",formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i, 6));add_res_val = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 1)+ " "		+formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i, 2)+ formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i, 3)+ formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i, 4)+ formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i, 5)+ formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i, 6);
					}
				}
				xml_str = xml_str + "<" + tag_name + ">" + add_res_val + "</"+ tag_name + ">";

				SKLogger_CC.writeLog("RLOS COMMON", " after adding res_flag+ "+ xml_str);
				int_xml.put(parent_tag, xml_str);
			}
		}
		return int_xml;
	}
}