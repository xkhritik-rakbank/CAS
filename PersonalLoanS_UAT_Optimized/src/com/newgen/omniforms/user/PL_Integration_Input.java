
package com.newgen.omniforms.user;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;

public class PL_Integration_Input implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	static Logger mLogger=PersonalLoanS.mLogger;
	private String col_n = "call_type,Call_name,form_control,parent_tag_name,xmltag_name,is_repetitive,default_val,data_format";
	private String fin_call_name = "Customer_details, Customer_eligibility,new_customer_req,new_account_req,DEDUP_SUMMARY,CUSTOMER_UPDATE_REQ";
	//Deepak Customer Update request added in fin call name string.  22Nov2017.
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Generate XML for Integration calls      
	 
	***********************************************************************************  */
	public String GenerateXML(String callName,String Operation_name)
	{
		mLogger.info("RLOSCommon"+ "Inside GenerateXML():");

		StringBuilder final_xml= new StringBuilder("");
		String header ="";
		String footer = "";
		String parentTagName="";
		String sQuery=null;
		PersonalLoanS.mLogger.info("$$outputgGridtXML "+"before try");
		try
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String sQuery_header = "SELECT Header,Footer,parenttagname FROM NG_Integration with (nolock) where Call_name='"+callName+"'";
			PersonalLoanS.mLogger.info("RLOSCommon"+ "sQuery"+sQuery_header);
			List<List<String>> DB_header=formObject.getNGDataFromDataCache(sQuery_header);
			if(!DB_header.isEmpty()){
				PersonalLoanS.mLogger.info("RLOSCommon header: "+DB_header.get(0).get(0)+" footer: "+DB_header.get(0).get(1)+" parenttagname: "+DB_header.get(0).get(2));
				header = DB_header.get(0).get(0);
				footer = DB_header.get(0).get(1);
				parentTagName = DB_header.get(0).get(2);

				
				if(!("".equalsIgnoreCase(Operation_name) || Operation_name.equalsIgnoreCase(null))){   
					PersonalLoanS.mLogger.info("inside if of operation"+"operation111"+Operation_name);
					PersonalLoanS.mLogger.info("inside if of operation"+"callName111"+callName);
					sQuery = "SELECT "+col_n +" FROM NG_Integration_PL_field_Mapping with (nolock) where Call_name='"+callName+"' and active = 'Y' and Operation_name='"+Operation_name+"' ORDER BY tag_seq ASC" ;
					PersonalLoanS.mLogger.info("RLOSCommon"+ "sQuery "+sQuery);
				}
				else{
					PersonalLoanS.mLogger.info("inside else of operation"+"operation"+Operation_name);
					sQuery = "SELECT "+col_n +" FROM NG_Integration_PL_field_Mapping with (nolock) where Call_name='"+callName+"' and active = 'Y' ORDER BY tag_seq ASC" ;
					PersonalLoanS.mLogger.info("RLOSCommon"+ "sQuery "+sQuery);
				}


				List<List<String>> DB_List=formObject.getDataFromDataSource(sQuery);//chnage to get data from DB directly
				PersonalLoanS.mLogger.info("OutputXML"+"OutputXML"+DB_List);
				if(!DB_List.isEmpty()){
					
					PersonalLoanS.mLogger.info("GenerateXML Integration field mapping table"+DB_List.get(0).get(0)+DB_List.get(0).get(1)+DB_List.get(0).get(2)+DB_List.get(0).get(3)+DB_List.get(0).get(4));
					PersonalLoanS.mLogger.info("GenerateXML Integration field mapping table"+DB_List.get(0).get(0)+DB_List.get(0).get(1)+DB_List.get(0).get(2)+DB_List.get(0).get(3));
					
						PersonalLoanS.mLogger.info(""+"column length"+col_n.length());
						Map<String, String> int_xml = new LinkedHashMap<String, String>();

						if ("DEDUP_SUMMARY".equalsIgnoreCase(callName)) {
							int_xml = DEDUP_SUMMARY_Custom(DB_List,formObject,callName);
						} else if ("BLACKLIST_DETAILS".equalsIgnoreCase(callName)) {
							int_xml = Blacklist_Details_custom(DB_List,formObject,callName);
						} else if ("NEW_CUSTOMER_REQ".equalsIgnoreCase(callName)) {
							int_xml = NEW_CUSTOMER_Custom(DB_List,formObject,callName);
						} else if ("CUSTOMER_UPDATE_REQ".equalsIgnoreCase(callName)) {
							int_xml = CUSTOMER_UPDATE_Custom(DB_List,formObject,callName);
						} else if ("NEW_CARD_REQ".equalsIgnoreCase(callName)) {
							int_xml = NEW_CARD_Custom(DB_List,formObject,callName);
						} else if ("DECTECH".equalsIgnoreCase(callName)) {
							int_xml = DECTECH_Custom(DB_List,formObject,callName);
						} else if ("CHEQUE_BOOK_ELIGIBILITY".equalsIgnoreCase(callName)) {
							int_xml = CHEQUE_BOOK_ELIGIBILITY_Custom(DB_List,formObject,callName);
						} else if ("NEW_LOAN_REQ".equalsIgnoreCase(callName)) {
							int_xml = NEW_LOAN_REQ_Custom(DB_List,formObject,callName);
						}
						else if ("CARD_NOTIFICATION".equalsIgnoreCase(callName)) {
							int_xml = CARD_NOTIFICATION_Custom(DB_List,formObject,callName);
						}
						else if ("CARD_SERVICES_REQUEST".equalsIgnoreCase(callName)) {
							int_xml = CARD_SERVICES_Custom(DB_List,formObject,callName);
						}
						else{
							int_xml = Non_Custom(DB_List,formObject,callName);
							//new method added for method in which nothing custom is required.
						}

						final_xml=final_xml.append("<").append(parentTagName).append(">");
						PersonalLoanS.mLogger.info("RLOS"+"Final XMLold--"+final_xml);

						Iterator<Map.Entry<String,String>> itr = int_xml.entrySet().iterator();
						PersonalLoanS.mLogger.info("itr of hashmap"+"itr"+itr);
						while (itr.hasNext())
						{
							Map.Entry<String, String> entry =  itr.next();
							PersonalLoanS.mLogger.info("entry of hashmap"+"entry"+entry);
							if(final_xml.indexOf(entry.getKey())>-1){
								PersonalLoanS.mLogger.info("RLOS"+"itr_value: Key: "+ entry.getKey()+" Value: "+entry.getValue());
								final_xml = final_xml.insert(final_xml.indexOf("<"+entry.getKey()+">")+entry.getKey().length()+2, entry.getValue());
								PersonalLoanS.mLogger.info("value of final xml"+"final_xml"+final_xml);
								itr.remove();
							}

						}    
						final_xml=final_xml.append("</").append(parentTagName).append(">");
						final_xml = new StringBuilder( Clean_Xml(final_xml.toString()));
						PersonalLoanS.mLogger.info("FInal XMLnew is: "+ final_xml);
						final_xml.insert(0, header);
						final_xml.append(footer);
						PersonalLoanS.mLogger.info("FInal XMLnew with header: "+ final_xml);
						formObject.setNGValue("Is_"+callName,"Y");
						PersonalLoanS.mLogger.info("value of "+callName+" Flag: "+formObject.getNGValue("Is_"+callName));

						return MQ_connection_response(final_xml);
					}

				}
				else {
					PersonalLoanS.mLogger.info("Genrate XML: "+"Entry is not maintained in field mapping Master table for : "+callName);
					return "Call not maintained";
				}
		}

		catch(Exception e){
			PersonalLoanS.mLogger.info("Exception ocurred: "+e.getLocalizedMessage());
			PLCommon.printException(e);
			return "0";
		}    
		return "";

	}
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function for Dedupe Summary call    
	 
	***********************************************************************************  */

	private Map<String, String> DEDUP_SUMMARY_Custom(List<List<String>> DB_List,FormReference formObject,String callName) {


		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();


		try{
			for (List<String> mylist : DB_List) {
				
				for (int i = 0; i < 8; i++) {
					
					PersonalLoanS.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");

				if ("AddressDetails".equalsIgnoreCase(tag_name) && int_xml.containsKey(parent_tag)) {
					String xml_str = int_xml.get(parent_tag);
					PersonalLoanS.mLogger.info("RLOS COMMON"+ " before adding address+ " + xml_str);
					xml_str = xml_str + getCustAddress_details(callName);
					PersonalLoanS.mLogger.info("RLOS COMMON"+ " after adding address+ " + xml_str);
					int_xml.put(parent_tag, xml_str);
				} 
				else if("MaritalStatus".equalsIgnoreCase(tag_name)){
					String marrital_code = formObject.getNGValue("cmplx_Customer_MAritalStatus").substring(0, 1);
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<"+tag_name+">"+marrital_code
							+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
				}
			}
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + ");
			PLCommon.printException(e);
		}

		return int_xml;
	}
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 22/11/2017              
	Author                              : Deepak              
	Description                         : Function for Dedupe Summary call    
	 
	***********************************************************************************  */

	private Map<String, String> Non_Custom(List<List<String>> DB_List,FormReference formObject,String callName) {


		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();

		//FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try{
			for (List<String> mylist : DB_List) {
				// for(int i=0;i<col_n.length();i++)
				for (int i = 0; i < 8; i++) {
					// SKLogger_CC.writeLog("rec: "+records.item(rec));
					PersonalLoanS.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
				
			}
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + ");
			PLCommon.printException(e);
		}

		return int_xml;
	}
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function for blacklist details call     
	 
	***********************************************************************************  */

	private Map<String, String> Blacklist_Details_custom(List<List<String>> DB_List,FormReference formObject, String Call_name) {

		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();

		
		try{
			for (List<String> mylist : DB_List) {
				
				for (int i = 0; i < 8; i++) {
					
					PersonalLoanS.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");

				if ("AddressDetails".equalsIgnoreCase(tag_name)
						&& int_xml.containsKey(parent_tag)) {
					String xml_str = int_xml.get(parent_tag);
					PersonalLoanS.mLogger.info("RLOS COMMON"+ " before adding address+ " + xml_str);
					xml_str = xml_str + getCustAddress_details(Call_name);
					PersonalLoanS.mLogger.info("RLOS COMMON"+ " after adding address+ " + xml_str);
					int_xml.put(parent_tag, xml_str);
				} else if("MaritalStatus".equalsIgnoreCase(tag_name)){
					String marrital_code = formObject.getNGValue("cmplx_Customer_MAritalStatus").substring(0, 1);
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<"+tag_name+">"+marrital_code
							+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,Call_name);
				}
			}
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + ");
			PLCommon.printException(e);
		}

		return int_xml;
	}
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function for New Customer Call      
	 
	***********************************************************************************  */

	private Map<String, String> NEW_CUSTOMER_Custom(List<List<String>> DB_List,FormReference formObject, String Call_name) {

		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();
		try{
			for (List<String> mylist : DB_List) {
				
				for (int i = 0; i < 8; i++) {

					PersonalLoanS.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");

				if ("AddressDetails".equalsIgnoreCase(tag_name)	&& int_xml.containsKey(parent_tag)) {
					String xml_str = int_xml.get(parent_tag);
					PersonalLoanS.mLogger.info("RLOS COMMON"+ " before adding address+ " + xml_str);
					xml_str = xml_str + getCustAddress_details(Call_name);
					PersonalLoanS.mLogger.info("RLOS COMMON"+ " after adding address+ " + xml_str);
					int_xml.put(parent_tag, xml_str);
				} else if ("MinorFlag".equalsIgnoreCase(tag_name) && "PersonDetails".equalsIgnoreCase(parent_tag)) {
					if (int_xml.containsKey(parent_tag)) {
						float Age = Float.parseFloat(formObject.getNGValue("cmplx_Customer_age"));
						String age_flag = "N";
						if (Age < 18)
							age_flag = "Y";
						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<" + tag_name + ">" + age_flag + "</" + tag_name + ">";

						PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding Minor flag+ " + xml_str);
						int_xml.put(parent_tag, xml_str);
					}
				} else if ("NonResidentFlag".equalsIgnoreCase(tag_name) && "PersonDetails".equalsIgnoreCase(parent_tag)) {
					if (int_xml.containsKey(parent_tag)) {
						String xml_str = int_xml.get(parent_tag);
						String res_flag = "N";

						if ("Resident".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_ResidentNonResident"))) {
							res_flag = "Y";
						}

						xml_str = xml_str + "<" + tag_name + ">" + res_flag + "</" + tag_name + ">";

						PersonalLoanS.mLogger.info("RLOS COMMON"+ " after adding res_flag+ " + xml_str);
						int_xml.put(parent_tag, xml_str);
					}
				}
				else if("MinorFlag".equalsIgnoreCase(tag_name)){
					if(int_xml.containsKey(parent_tag))
					{
						int Age = Integer.parseInt(formObject.getNGValue("cmplx_Customer_age"));
						String age_flag = "N";
						if(Age<18)
							age_flag="Y";
						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<"+tag_name+">"+age_flag
								+"</"+ tag_name+">";

						PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}		                            	
				}
				
				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,Call_name);
				}
			}
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + ");
			PLCommon.printException(e);
		}
		return int_xml;
	}
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function for Customer Update Call      
	 
	***********************************************************************************  */

	private Map<String, String> CUSTOMER_UPDATE_Custom(List<List<String>> DB_List,FormReference formObject, String Call_name) {
		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();
		try{
			for (List<String> mylist : DB_List) {
				
				for (int i = 0; i < 8; i++) {
					
					PersonalLoanS.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				String form_control =  recordFileMap.get("form_control");
				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");
				if("OECDDet".equalsIgnoreCase(tag_name) && "CUSTOMER_UPDATE_REQ".equalsIgnoreCase(Call_name)){
					PersonalLoanS.mLogger.info("PL Common"+"inside OECDDet inside customer update req1");
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + getCustOECD_details(Call_name);
					PersonalLoanS.mLogger.info("PL COMMON"+" after adding OECDDet: "+xml_str);
					int_xml.put(parent_tag, xml_str);                                    
				} else if ("PhnLocalCode".equalsIgnoreCase(tag_name)) {
					PersonalLoanS.mLogger.info("inside PL Common generate xml"+
							"PhnLocalCode to substring");
					String xml_str = int_xml.get(parent_tag);
					String phn_no = formObject.getNGValue(form_control);
					if ((!"".equalsIgnoreCase(phn_no)) && phn_no.indexOf("00971") > -1) {
						phn_no = phn_no.substring(5);
					}

					xml_str = xml_str + "<" + tag_name + ">" + phn_no + "</" + tag_name + ">";

					PersonalLoanS.mLogger.info("PL COMMON"+ " after adding ApplicationID:  " + xml_str);
					int_xml.put(parent_tag, xml_str);
				} else if("AddrDet".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("inside 1st if"+"inside customer update req1");
					if(int_xml.containsKey(parent_tag))
					{
						PersonalLoanS.mLogger.info("inside 1st if"+"inside customer update req2");
						String xml_str = int_xml.get(parent_tag);
						PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding address+ "+xml_str);
						xml_str = xml_str + getCustAddress_details(Call_name);
						PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding address+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}		                            	
				}
				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,Call_name);
				}
			}
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + ");
			PLCommon.printException(e);
		}
		return int_xml;

	}
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function for New Card Request call      
	 
	***********************************************************************************  */

	private Map<String, String> NEW_CARD_Custom(List<List<String>> DB_List,FormReference formObject, String Call_name) {

		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();
		try{
			for (List<String> mylist : DB_List) {
				
				for (int i = 0; i < 8; i++) {
					
					PersonalLoanS.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");
				if ("VIPFlg".equalsIgnoreCase(tag_name)) {
					String vip_flag = "N";
					if ("true"
							.equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_VIPFlag"))) {
						vip_flag = "Y";
					}
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<" + tag_name + ">" + vip_flag + "</" + tag_name + ">";

					PersonalLoanS.mLogger.info("RLOS COMMON"+ " after adding VIP flag+ " + xml_str);
					int_xml.put(parent_tag, xml_str);
				} else if ("ProcessingUserId".equalsIgnoreCase(tag_name)) {
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<" + tag_name + ">" + formObject.getUserName() + "</" + tag_name + ">";

					PersonalLoanS.mLogger.info("RLOS COMMON"+ " after adding Minor flag+ " + xml_str);
					int_xml.put(parent_tag, xml_str);
				} else if ("ProcessingDate".equalsIgnoreCase(tag_name)) {
					String xml_str = int_xml.get(parent_tag);
					SimpleDateFormat sdf1 = new SimpleDateFormat(
							"yyyy-MM-dd'T'HH:mm:ss.mmm+hh:mm");
					xml_str = xml_str + "<" + tag_name + ">" + sdf1.format(new Date()) + "</" + tag_name + ">";

					PersonalLoanS.mLogger.info("RLOS COMMON"+ " after adding Minor flag+ " + xml_str);
					int_xml.put(parent_tag, xml_str);
				}
			
				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,Call_name);
				}
			}
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + ");
			PLCommon.printException(e);
		}
		return int_xml;
	}

	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Generate XML for Address detail     
	 
	***********************************************************************************  */

	public String getCustAddress_details(String call_name){
		PersonalLoanS.mLogger.info("RLOSCommon java file"+ "inside getCustAddress_details : ");
		String  add_xml_str ="";
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			int add_row_count = formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
			PersonalLoanS.mLogger.info("RLOSCommon java file"+ "inside getCustAddress_details add_row_count+ : "+add_row_count);

			for (int i = 0; i<add_row_count;i++){
				String Address_type = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0); //0
				String flat_Villa=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 1);//1
				String Building_name=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 2);//2
				String street_name=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 3);//3
				String Landmard=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 4);//4
				String city = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 5);//5
				String Emirates=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 6);//6
				String country=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 7);//7
				String Po_Box=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 8);//8
				String years_in_current_add=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 9);//9
				PersonalLoanS.mLogger.info("PLCommon java file"+ "ADD Type: "+Address_type);
				//new condition added to handle address type for all finacle calls. 22Nov2017.
				if("HOME".equalsIgnoreCase(Address_type) && !fin_call_name.contains(call_name)) {
					Address_type="Home Country";
				}
				PersonalLoanS.mLogger.info("PLCommon java file"+ "ADD Type after: "+Address_type);
				//added here
				PersonalLoanS.mLogger.info("RLOSCommon java file"+ "inside getCustAddress_details add_row_count+ : "+years_in_current_add);
				int years=Integer.parseInt(years_in_current_add);
				//ended here

				String preferrd;
				//Code change to added Effective from and to start
				String EffectiveFrom;
				String EffectiveTo;
				SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
				Calendar cal = Calendar.getInstance();
				EffectiveTo=sdf1.format(cal.getTime());
				cal.add(Calendar.YEAR, -years);
				EffectiveFrom=sdf1.format(cal.getTime());
				PersonalLoanS.mLogger.info("RLOS value of CurrentDate EffectiveTo"+""+EffectiveTo);
				PersonalLoanS.mLogger.info("RLOS value of EffectiveFromDate"+""+EffectiveFrom);
				//Code change to added Effective from and to End
				PersonalLoanS.mLogger.info("RLOS value of prefered Add: Address Type: "+Address_type+" Address pref flag: "+formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid"+ i+ 11));
				if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 11)))//10
					preferrd = "Y";
				else
					preferrd = "N";


				if("CUSTOMER_UPDATE_REQ".equalsIgnoreCase(call_name)){
					add_xml_str = add_xml_str + "<AddrDet><AddressType>"+Address_type+"</AddressType>";
					//Code change to added Effective from and to start
					add_xml_str = add_xml_str + "<EffectiveFrom>"+EffectiveFrom+"</EffectiveFrom>";
					add_xml_str = add_xml_str + "<EffectiveTo>"+EffectiveTo+"</EffectiveTo>";
					add_xml_str = add_xml_str + "<HoldMailFlag>N</HoldMailFlag>";
					add_xml_str = add_xml_str + "<ReturnFlag>N</ReturnFlag>";
					add_xml_str = add_xml_str + "<AddrPrefFlag>"+preferrd+"</AddrPrefFlag>";
					//Code change to added Effective from and to End

					add_xml_str = add_xml_str + "<AddrLine1>"+flat_Villa+"</AddrLine1>";
					add_xml_str = add_xml_str + "<AddrLine2>"+Building_name+"</AddrLine2>";
					add_xml_str = add_xml_str + "<AddrLine3>"+street_name+"</AddrLine3>";
					add_xml_str = add_xml_str + "<AddrLine4>"+Landmard+"</AddrLine4>";
					add_xml_str = add_xml_str + "<POBox>"+Po_Box+"</POBox>";
					add_xml_str = add_xml_str + "<State>"+Emirates+"</State>";
					add_xml_str = add_xml_str + "<City>"+city+"</City>";
					add_xml_str = add_xml_str + "<CountryCode>"+country+"</CountryCode></AddrDet>";
					
				}
				else if("CARD_NOTIFICATION".equalsIgnoreCase(call_name)){
					add_xml_str = add_xml_str + "<AddrDet><AddressType>"+Address_type+"</AddressType>";
					add_xml_str = add_xml_str + "<AddressLine1>"+flat_Villa+"</AddressLine1>";
					add_xml_str = add_xml_str + "<AddressLine2>"+Building_name+"</AddressLine2>";
					add_xml_str = add_xml_str + "<AddressLine3>"+street_name+"</AddressLine3>";
					add_xml_str = add_xml_str + "<AddressLine4>"+Landmard+"</AddressLine4>";
					add_xml_str = add_xml_str + "<City>"+city+"</City>";
					add_xml_str = add_xml_str + "<State>"+Emirates+"</State>";
					add_xml_str = add_xml_str + "<Country>"+country+"</Country>";
					add_xml_str = add_xml_str + "<POBox>"+Po_Box+"</POBox>";
					add_xml_str = add_xml_str + "<EffectiveFromDate>"+EffectiveFrom+"</EffectiveFromDate>";
					add_xml_str = add_xml_str + "<EffectiveToDate>"+EffectiveTo+"</EffectiveToDate>";
					add_xml_str = add_xml_str + "<NumberOfYears>"+years+"</NumberOfYears>";
					add_xml_str = add_xml_str + "<AddrPrefFlag>"+preferrd+"</AddrPrefFlag>"
							+ "</AddrDet>";
				
				}
				else if ("NEW_CUSTOMER_REQ".equalsIgnoreCase(call_name)){
					add_xml_str = add_xml_str + "<AddressDetails><AddressType>"+Address_type+"</AddressType><AddrPrefFlag>"+preferrd+"</AddrPrefFlag><PrefFormat>STRUCTURED_FORMAT</PrefFormat>";
					add_xml_str = add_xml_str + "<AddrLine1>"+flat_Villa+"</AddrLine1>";
					add_xml_str = add_xml_str + "<AddrLine2>"+Building_name+"</AddrLine2>";
					add_xml_str = add_xml_str + "<AddrLine3>"+street_name+"</AddrLine3>";
					add_xml_str = add_xml_str + "<AddrLine4>"+Landmard+"</AddrLine4>";
					add_xml_str = add_xml_str + "<City>"+city+"</City>";
					add_xml_str = add_xml_str + "<CountryCode>"+country+"</CountryCode>";
					add_xml_str = add_xml_str + "<State>"+Emirates+"</State>";
					add_xml_str = add_xml_str + "<POBox>"+Po_Box+"</POBox></AddressDetails>";
				}
				//10/08/2017 added for the respective call
				else if("NEW_CREDITCARD_REQ".equalsIgnoreCase(call_name)){
					add_xml_str = add_xml_str + "<AddressDetails><AddrType>"+Address_type+"</AddrType><UseExistingAddress>"+"N"+"</UseExistingAddress><IsPreferedAddr>"+preferrd+"</IsPreferedAddr>";
					add_xml_str = add_xml_str + "<Addr1>"+flat_Villa+"</Addr1>";
					add_xml_str = add_xml_str + "<Addr2>"+Building_name+"</Addr2>";
					add_xml_str = add_xml_str + "<Addr3>"+street_name+"</Addr3>";
					add_xml_str = add_xml_str + "<Addr4>"+Landmard+"</Addr4>";
					add_xml_str = add_xml_str + "<Addr5>"+city+"</Addr5>";
					add_xml_str = add_xml_str + "<PostalCode>"+country+"</PostalCode>";
					add_xml_str = add_xml_str + "<ZipCode>"+Emirates+"</ZipCode>";
					add_xml_str = add_xml_str + "<City>"+city+"</City>";
					add_xml_str = add_xml_str + "<StateProv>"+city+"</StateProv>";
					add_xml_str = add_xml_str + "<County>"+city+"</County>";
					add_xml_str = add_xml_str + "<Country>"+country+"</Country>"+ "</AddressDetails>";
				}
				//10/08/2017 added for the respective call
				else{
					add_xml_str = add_xml_str + "<AddressDetails><AddressType>"+Address_type+"</AddressType>";
					add_xml_str = add_xml_str + "<AddressLine1>"+flat_Villa+"</AddressLine1>";
					add_xml_str = add_xml_str + "<AddressLine2>"+Building_name+"</AddressLine2>";
					add_xml_str = add_xml_str + "<AddressLine3>"+street_name+"</AddressLine3>";
					add_xml_str = add_xml_str + "<City>"+city+"</City>";
					add_xml_str = add_xml_str + "<State>"+Emirates+"</State>";
					add_xml_str = add_xml_str + "<Country>"+country+"</Country>";
					add_xml_str = add_xml_str + "<POBox>"+Po_Box+"</POBox></AddressDetails>";
				}
			}
			PersonalLoanS.mLogger.info("RLOSCommon"+ "Address tag Cration: "+ add_xml_str);
			return add_xml_str;
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("PLCommon getCustAddress_details method"+ "Exception Occure in generate Address XMl"+e.getMessage());
			PLCommon.printException(e);
			return add_xml_str;
		}

	}	
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Generate XML for Product Details    
	 
	***********************************************************************************  */

	public String getProduct_details(){
		PersonalLoanS.mLogger.info("RLOSCommon java file"+ "inside getProduct_details : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int prod_row_count = formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		PersonalLoanS.mLogger.info("RLOSCommon java file"+ "inside getCustAddress_details add_row_count+ : "+prod_row_count);
		String FinalLimitPLQuery="select isnull(FinalLoanAmount,0) from ng_rlos_IGR_Eligibility_PersonalLoan where Child_wi='"+formObject.getWFWorkitemName()+"'";
		List<List<String>> FinalLimitPLXML = formObject.getDataFromDataSource(FinalLimitPLQuery);
		String finalLimit;
		if (!FinalLimitPLXML.isEmpty()){
			finalLimit=FinalLimitPLXML.get(0).get(0);
			formObject.setNGValue("cmplx_EligibilityAndProductInfo_FinalLimit", finalLimit);
		}
		String  prod_xml_str ="";
		String Manual_Dev=formObject.getNGValue("cmplx_DEC_Manual_Deviation");

		for (int i = 0; i<prod_row_count;i++){
			PersonalLoanS.mLogger.info("PL_SKLogger java file"+ "inside getProduct_details add_row_count+ : "+prod_row_count);

			String prod_type = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 0); //0
			String priority = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 8);
			String reqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 1);//1
			String subProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2);//2
			String reqLimit=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 3);//3
			reqLimit=reqLimit.replaceAll(",", "");
			String appType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 4);//4
			String cardProd = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 5);//5
			String scheme=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 7);//6
			String tenure=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 6);//7
			// String limitExpiry=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 12);//8
			String ApplicationDate=formObject.getNGValue("CreatedDate");
			if((ApplicationDate != null)||(ApplicationDate != "")){
				ApplicationDate=PersonalLoanSCommonCode.Convert_dateFormat(ApplicationDate, "dd/MM/yyyy", "yyyy-MM-dd");
			}
			PersonalLoanS.mLogger.info("PL_SKLogger java file"+ "inside ApplicationDate ApplicationDate+ : "+ApplicationDate);

		
			String EMI;
			EMI=formObject.getNGValue("cmplx_EligibilityAndProductInfo_EMI");
			if(EMI == null){
				EMI=""; 
			}

			if ("Primary".equalsIgnoreCase(priority)){
				prod_xml_str = prod_xml_str + "<ApplicationDetails><product_type>"+("Conventional".equalsIgnoreCase(prod_type)?"CON":"ISL")+"</product_type>";
				prod_xml_str = prod_xml_str + "<app_category>"+formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg")+"</app_category>";
				prod_xml_str = prod_xml_str + "<requested_product>"+("Personal Loan".equalsIgnoreCase(reqProd)?"PL":"CC")+"</requested_product>";
				prod_xml_str = prod_xml_str + "<requested_limit>"+reqLimit+"</requested_limit>";
				prod_xml_str = prod_xml_str + "<sub_product>"+subProd+"</sub_product>";
				prod_xml_str = prod_xml_str + "<requested_card_product>"+cardProd+"</requested_card_product>";
				prod_xml_str = prod_xml_str + "<application_type>"+appType+"</application_type>";
				prod_xml_str = prod_xml_str + "<scheme>"+scheme+"</scheme>";
				prod_xml_str = prod_xml_str + "<tenure>"+tenure+"</tenure>";
				prod_xml_str = prod_xml_str + "<customer_type>"+("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))?"NTB":"Existing")+"</customer_type>";
				if(Manual_Dev!=null){ 
					if("true".equalsIgnoreCase(Manual_Dev)){
						prod_xml_str = prod_xml_str + "<limit_expiry_date></limit_expiry_date><final_limit></final_limit><emi></emi><manual_deviation>Y</manual_deviation><application_date>"+ApplicationDate+"</application_date></ApplicationDetails>";
					}
					else{
						prod_xml_str = prod_xml_str + "<limit_expiry_date></limit_expiry_date><final_limit></final_limit><emi></emi><manual_deviation>N</manual_deviation><application_date>"+ApplicationDate+"</application_date></ApplicationDetails>";
					}   
				}
				else {
					prod_xml_str = prod_xml_str + "<limit_expiry_date></limit_expiry_date><final_limit></final_limit><emi></emi><manual_deviation></manual_deviation><application_date>"+ApplicationDate+"</application_date></ApplicationDetails>";

				}
			}

		}
		PersonalLoanS.mLogger.info("RLOSCommon"+ "Address tag Cration: "+ prod_xml_str);
		return prod_xml_str;
	}
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Generate XML for Internal Bureau Data  
	 
	***********************************************************************************  */


	public String InternalBureauData(){
		PersonalLoanS.mLogger.info("RLOSCommon java file"+ "inside InternalBureauData : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String CifId = formObject.getNGValue("cmplx_Customer_CIFNO");

		String NoOfContracts = "";
		String Total_Exposure = "";
		String WorstCurrentPaymentDelay = ""; 
		String Worst_PaymentDelay_Last24Months = "";
		String Nof_Records = "";

		String  add_xml_str ="";
		try{
			add_xml_str = add_xml_str + "<InternalBureau><applicant_id>"+CifId+"</applicant_id>";
			add_xml_str = add_xml_str + "<full_name>"+formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_MiddleName")+""+formObject.getNGValue("cmplx_Customer_LAstNAme")+"</full_name>";// fullname fieldname to be confirmed from onsite
			String sQuery = "SELECT OutstandingAmt,OverdueAmt,CreditLimit FROM ng_RLOS_CUSTEXPOSE_CardDetails WHERE Child_Wi = '"+formObject.getWFWorkitemName()+"' AND Request_Type = 'InternalExposure'  union SELECT   TotalOutstandingAmt ,OverdueAmt,TotalLoanAmount FROM ng_RLOS_CUSTEXPOSE_LoanDetails   with (nolock) WHERE Child_wi = '"+formObject.getWFWorkitemName()+"'";
			List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
			PersonalLoanS.mLogger.info("InternalBureauData list size"+OutputXML.size()+ "");
		
			PersonalLoanS.mLogger.info("obefor list "+ "values");
			float TotOutstandingAmt;
			float TotOverdueAmt;

			PersonalLoanS.mLogger.info("outsidefor list "+ "values");
			TotOutstandingAmt=0.0f;
			TotOverdueAmt=0.0f;

			PersonalLoanS.mLogger.info("outsidefor2 list "+ "values");
			for(int i = 0; i<OutputXML.size();i++){
				PersonalLoanS.mLogger.info("insidefor list "+i+ "values"+OutputXML.get(i).get(1));
				if(OutputXML.get(i).get(0)!=null && !OutputXML.get(i).get(1).isEmpty() &&  !"".equals(OutputXML.get(i).get(1)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(1)) ){
					PersonalLoanS.mLogger.info("Totaloutstanding"+i+ "values."+TotOutstandingAmt+"..");
					TotOutstandingAmt = TotOutstandingAmt + Float.parseFloat(OutputXML.get(i).get(0));
				}
				if(OutputXML.get(i).get(1)!=null && !OutputXML.get(i).get(1).isEmpty() && !"".equals(OutputXML.get(i).get(2)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(2)) ){
					PersonalLoanS.mLogger.info("TotOverdueAmt"+i+ "values."+TotOutstandingAmt+"..");
					TotOverdueAmt = TotOverdueAmt + Float.parseFloat(OutputXML.get(i).get(1));
				}

			}
			add_xml_str = add_xml_str + "<total_out_bal>"+TotOutstandingAmt+"</total_out_bal>";
			add_xml_str = add_xml_str + "<total_overdue>"+TotOverdueAmt+"</total_overdue>";
			String sQueryDerived = "select NoOfContracts,Total_Exposure,WorstCurrentPaymentDelay,Worst_PaymentDelay_Last24Months,Nof_Records from NG_RLOS_CUSTEXPOSE_Derived where Request_Type='CollectionsSummary' and child_wi='"+formObject.getWFWorkitemName()+"'" ;
			List<List<String>> OutputXMLDerived = formObject.getDataFromDataSource(sQueryDerived);
			if(OutputXMLDerived!=null && !OutputXMLDerived.isEmpty() && OutputXMLDerived.get(0)!=null){
				if(!(OutputXMLDerived.get(0).get(0)==null || "".equals(OutputXMLDerived.get(0).get(0))) ){
					NoOfContracts= OutputXMLDerived.get(0).get(0);
				}
				if(!(OutputXMLDerived.get(0).get(1)==null || "".equals(OutputXMLDerived.get(0).get(1))) ){
					Total_Exposure= OutputXMLDerived.get(0).get(1);
				}
				if(!(OutputXMLDerived.get(0).get(2)==null || "".equals(OutputXMLDerived.get(0).get(2))) ){
					WorstCurrentPaymentDelay= OutputXMLDerived.get(0).get(2);
				}
				if(!(OutputXMLDerived.get(0).get(3)==null || "".equals(OutputXMLDerived.get(0).get(3))) ){
					Worst_PaymentDelay_Last24Months= OutputXMLDerived.get(0).get(3);
				}
				if(!(OutputXMLDerived.get(0).get(4)==null || "".equals(OutputXMLDerived.get(0).get(4))) ){
					Nof_Records= OutputXMLDerived.get(0).get(4);
				}
			}
			add_xml_str = add_xml_str + "<no_default_contract>"+NoOfContracts+"</no_default_contract>";

			add_xml_str = add_xml_str + "<total_exposure>"+Total_Exposure+"</total_exposure>";
			add_xml_str = add_xml_str + "<worst_curr_pay>"+WorstCurrentPaymentDelay+"</worst_curr_pay>"; // to be populated later
			add_xml_str = add_xml_str + "<worst_curr_pay_24>"+Worst_PaymentDelay_Last24Months+"</worst_curr_pay_24>"; // to be populated later
			add_xml_str = add_xml_str + "<no_of_rec>"+Nof_Records+"</no_of_rec>"; 
			String sQuerycheque = "SELECT 'DDS 3 months',count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-3,GETDATE()) and returntype='DDS' and child_wi='"+formObject.getWFWorkitemName()+"' union SELECT 'DDS 6 months',count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-6,GETDATE()) and returntype='DDS' and child_wi='"+formObject.getWFWorkitemName()+"' union SELECT 'ICCS 3 months',count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-3,GETDATE()) and returntype='ICCS' and child_wi='"+formObject.getWFWorkitemName()+"' union SELECT 'ICCS 6 months',count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-6,GETDATE()) and returntype='ICCS' and child_wi='"+formObject.getWFWorkitemName()+"'" ;
			List<List<String>> OutputXMLcheque = formObject.getDataFromDataSource(sQuerycheque);


			add_xml_str = add_xml_str + "<cheque_return_3mon>"+OutputXMLcheque.get(2).get(1)+"</cheque_return_3mon>";
			add_xml_str = add_xml_str + "<dds_return_3mon>"+OutputXMLcheque.get(0).get(1)+"</dds_return_3mon>";
			add_xml_str = add_xml_str + "<cheque_return_6mon>"+OutputXMLcheque.get(3).get(1)+"</cheque_return_6mon>";
			add_xml_str = add_xml_str + "<dds_return_6mon>"+OutputXMLcheque.get(1).get(1)+"</dds_return_6mon>";
			add_xml_str = add_xml_str + "<internal_charge_off>"+"N"+"</internal_charge_off><company_flag>N</company_flag></InternalBureau>";// to be populated later



			PersonalLoanS.mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "+ add_xml_str);
			return add_xml_str;
		}
		catch(Exception e)
		{
			new PersonalLoanSCommonCode();
			PersonalLoanS.mLogger.info("RLOSCommon"+ "Exception occurred in InternalBureauData()"+e.getMessage());
			PLCommon.printException(e);
			return "";
		}

	}
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Generate XML for Internal Bounced Cheques    
	 
	***********************************************************************************  */

	public String InternalBouncedCheques(){
		PersonalLoanS.mLogger.info("RLOSCommon java file"+ "inside InternalBouncedCheques : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "select CIFID,AcctId,returntype,returnNumber,returnAmount,retReasonCode,returnDate from ng_rlos_FinancialSummary_ReturnsDtls with (nolock) where Child_Wi = '"+formObject.getWFWorkitemName()+"'";
		PersonalLoanS.mLogger.info("InternalBouncedCheques sQuery"+sQuery+ "");
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		PersonalLoanS.mLogger.info("InternalBouncedCheques list size"+OutputXML.size()+ "");

		for (int i = 0; i<OutputXML.size();i++){

			String applicantID = "";
			String chequeNo = "";
			String internal_bounced_cheques_id = "";
			String bouncedCheq="";
			String amount = "";
			String reason = ""; 
			String returnDate = "";


			if(!(OutputXML.get(i).get(0) == null || "".equals(OutputXML.get(i).get(0))) ){
				applicantID = OutputXML.get(i).get(0);
			}
			if(!(OutputXML.get(i).get(1) == null || "".equals(OutputXML.get(i).get(1))) ){
				internal_bounced_cheques_id = OutputXML.get(i).get(1);
			}
			if(!(OutputXML.get(i).get(2) == null || "".equals(OutputXML.get(i).get(2))) ){
				bouncedCheq = OutputXML.get(i).get(2);
			}
			if(!(OutputXML.get(i).get(3) == null || "".equals(OutputXML.get(i).get(3))) ){
				chequeNo = OutputXML.get(i).get(3);
			}
			if(!(OutputXML.get(i).get(4) == null || "".equals(OutputXML.get(i).get(4))) ){
				amount = OutputXML.get(i).get(4);
			}
			if(!(OutputXML.get(i).get(5) == null || "".equals(OutputXML.get(i).get(5))) ){
				reason = OutputXML.get(i).get(5);
			}
			if(!(OutputXML.get(i).get(6) == null || "".equals(OutputXML.get(i).get(6))) ){
				returnDate = OutputXML.get(i).get(6);
			}


			if(applicantID!=null && !"".equalsIgnoreCase(applicantID) && !"null".equalsIgnoreCase(applicantID)){
				add_xml_str = add_xml_str + "<InternalBouncedCheques><applicant_id>"+applicantID+"</applicant_id>";
				add_xml_str = add_xml_str + "<internal_bounced_cheques_id>"+internal_bounced_cheques_id+"</internal_bounced_cheques_id>";
				if ("ICCS".equalsIgnoreCase(bouncedCheq)){
					add_xml_str = add_xml_str + "<bounced_cheque>"+"1"+"</bounced_cheque>";
				}
				add_xml_str = add_xml_str + "<cheque_no>"+chequeNo+"</cheque_no>";
				add_xml_str = add_xml_str + "<amount>"+amount+"</amount>";
				add_xml_str = add_xml_str + "<reason>"+reason+"</reason>";
				add_xml_str = add_xml_str + "<return_date>"+returnDate+"</return_date>"; 
				add_xml_str = add_xml_str + "<provider_no>"+"RAKBANK"+"</provider_no>";
				if ("DDS".equalsIgnoreCase(bouncedCheq)){
					add_xml_str = add_xml_str + "<bounced_cheque_dds>"+"1"+"</bounced_cheque_dds>"; 
				}
				add_xml_str=  add_xml_str+"<company_flag>N</company_flag></InternalBouncedCheques>";
			}

		}
		PersonalLoanS.mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Generate XML for Internal Bureau Individual Products   
	 
	***********************************************************************************  */


	public String InternalBureauIndividualProducts(){
		PersonalLoanS.mLogger.info("RLOSCommon java file"+ "inside InternalBureauIndividualProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//Query Changed on 1st Nov
		String sQuery = "SELECT cifid,agreementid,loantype,loantype,custroletype,loan_start_date,loanmaturitydate,lastupdatedate ,totaloutstandingamt,totalloanamount,NextInstallmentAmt,paymentmode,totalnoofinstalments,remaininginstalments,totalloanamount,	overdueamt,nofdayspmtdelay,monthsonbook,currentlycurrentflg,currmaxutil,DPD_30_in_last_6_months,DPD_60_in_last_18_months,propertyvalue,loan_disbursal_date,marketingcode,DPD_30_in_last_3_months,DPD_30_in_last_6_months,DPD_30_in_last_9_months,DPD_30_in_last_12_months,DPD_30_in_last_18_months,DPD_30_in_last_24_months,DPD_60_in_last_3_months,DPD_60_in_last_6_months,DPD_60_in_last_9_months,DPD_60_in_last_12_months,DPD_60_in_last_18_months,DPD_60_in_last_24_months,DPD_90_in_last_3_months,DPD_90_in_last_6_months,DPD_90_in_last_9_months,DPD_90_in_last_12_months,DPD_90_in_last_18_months,DPD_90_in_last_24_months,DPD_120_in_last_3_months,DPD_120_in_last_6_months,DPD_120_in_last_9_months,DPD_120_in_last_12_months,DPD_120_in_last_18_months,DPD_120_in_last_24_months,DPD_150_in_last_3_months,DPD_150_in_last_6_months,DPD_150_in_last_9_months,DPD_150_in_last_12_months,DPD_150_in_last_18_months,DPD_150_in_last_24_months,DPD_180_in_last_3_months,DPD_180_in_last_6_months,DPD_180_in_last_9_months,DPD_180_in_last_12_months,DPD_180_in_last_24_months,'' as col1,isnull(Consider_For_Obligations,'true'),LoanStat,'LOANS',writeoffStat,writeoffstatdt,lastrepmtdt,limit_increase,PartSettlementDetails,'' as SchemeCardProduct,General_Status,Internal_WriteOff_Check FROM ng_RLOS_CUSTEXPOSE_LoanDetails WHERE Child_wi = '"+formObject.getWFWorkitemName()+"' and LoanStat !='Pipeline' union select CifId,CardEmbossNum,CardType,CardType,CustRoleType,'' as col6,'' as col7,'' as col8,OutstandingAmt,CreditLimit,PaymentsAmount,PaymentMode,'' as col13,'' as col14,CashLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,'' as col19,CurrMaxUtil,DPD_30_in_last_6_months,DPD_60_in_last_18_months,'' as col23,'' as col24,'' as col25,DPD_30_in_last_3_months,DPD_30_in_last_6_months,DPD_30_in_last_9_months,DPD_30_in_last_12_months,DPD_30_in_last_18_months,DPD_30_in_last_24_months,DPD_60_in_last_3_months,DPD_60_in_last_6_months,DPD_60_in_last_9_months,DPD_60_in_last_12_months,DPD_60_in_last_18_months,DPD_60_in_last_24_months,DPD_90_in_last_3_months,DPD_90_in_last_6_months,DPD_90_in_last_9_months,DPD_90_in_last_12_months,DPD_90_in_last_18_months,DPD_90_in_last_24_months,DPD_120_in_last_3_months,DPD_120_in_last_6_months,DPD_120_in_last_9_months,DPD_120_in_last_12_months,DPD_120_in_last_18_months,DPD_120_in_last_24_months,DPD_150_in_last_3_months,DPD_150_in_last_6_months,DPD_150_in_last_9_months,DPD_150_in_last_12_months,DPD_150_in_last_18_months,DPD_150_in_last_24_months,DPD_180_in_last_3_months,DPD_180_in_last_6_months,DPD_180_in_last_9_months,DPD_180_in_last_12_months,DPD_180_in_last_24_months,ExpiryDate,isnull(Consider_For_Obligations,'true'),CardStatus,'CARDS',writeoffStat,writeoffstatdt,'' as lastrepdate,limit_increase,'' as PartSettlementDetails,SchemeCardProd,General_Status,Internal_WriteOff_Check FROM ng_RLOS_CUSTEXPOSE_CardDetails where 	Child_wi = '"+formObject.getWFWorkitemName()+"' and Request_Type In ('InternalExposure','CollectionsSummary')";
		//Query Changed on 1st Nov
		PersonalLoanS.mLogger.info("InternalBureauIndividualProducts sQuery"+sQuery+ "");
		String CountQuery ="select count(*) from ng_RLOS_CUSTEXPOSE_CardDetails where Child_wi = '"+formObject.getWFWorkitemName()+"' and cardStatus='A'";
		List<List<String>> CountXML = formObject.getDataFromDataSource(CountQuery);
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		PersonalLoanS.mLogger.info("InternalBureauIndividualProducts list size"+OutputXML.size()+ "");
		PersonalLoanS.mLogger.info("InternalBureauIndividualProducts list "+OutputXML+ "");
		String ReqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
		String mol_sal_var = "";
		PersonalLoanS.mLogger.info("InternalBureauIndividualProducts outside cmplx_MOL_molsalvar "+formObject.getNGValue("cmplx_MOL_molsalvar")+ "");
		if(formObject.getNGValue("cmplx_MOL_molsalvar")!= null){
			PersonalLoanS.mLogger.info("InternalBureauIndividualProducts  outside cmplx_MOL_molsalvar "+formObject.getNGValue("cmplx_MOL_molsalvar")+ "");
			mol_sal_var = formObject.getNGValue("cmplx_MOL_molsalvar");
		}
		try{
			for (int i = 0; i<OutputXML.size();i++){

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
				String EmployerType=formObject.getNGValue("EMploymentDetails_Combo5");
				String Kompass=formObject.getNGValue("cmplx_EmploymentDetails_Kompass");
				String paid_installment="";
				String Internal_WriteOff_Check="";
				PersonalLoanS.mLogger.info("Inside for"+ "asdasdasd");


				if(!(OutputXML.get(i).get(0) == null || "".equals(OutputXML.get(i).get(0))) ){
					cifId = OutputXML.get(i).get(0);
				}
				if(!(OutputXML.get(i).get(1) == null || "".equals(OutputXML.get(i).get(1))) ){
					agreementId = OutputXML.get(i).get(1);
				}				
				if(!(OutputXML.get(i).get(2) == null || "".equals(OutputXML.get(i).get(2))) ){
					product_type = OutputXML.get(i).get(2);
					if ("Home In One".equalsIgnoreCase(product_type)){
						product_type="HIO";
					}
					else{
						product_type = OutputXML.get(i).get(63);
					}
				}
						
				if(!(OutputXML.get(i).get(5) == null || "".equals(OutputXML.get(i).get(5))) ){
					loan_start_date = OutputXML.get(i).get(5);
				}
				if(OutputXML.get(i).get(6)!=null && !OutputXML.get(i).get(6).isEmpty() &&  !"".equals(OutputXML.get(i).get(6)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(6)) ){
					loanmaturitydate = OutputXML.get(i).get(6);
				}
				if(OutputXML.get(i).get(7)!=null && !OutputXML.get(i).get(7).isEmpty() &&  !"".equals(OutputXML.get(i).get(7)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(7)) ){
					lastupdatedate = OutputXML.get(i).get(7);
				}				
				if(OutputXML.get(i).get(8)!=null && !OutputXML.get(i).get(8).isEmpty() &&  !"".equals(OutputXML.get(i).get(8)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(8)) ){
					outstandingamt = OutputXML.get(i).get(8);
				}
				if(!(OutputXML.get(i).get(9) == null || "".equals(OutputXML.get(i).get(9))) ){
					totalloanamount = OutputXML.get(i).get(9);
				}
				if(!(OutputXML.get(i).get(10) == null || "".equals(OutputXML.get(i).get(10))) ){
					Emi = OutputXML.get(i).get(10);
				}				
				if(!(OutputXML.get(i).get(11) == null || "".equals(OutputXML.get(i).get(11))) ){
					paymentmode = OutputXML.get(i).get(11);
				}
				if(OutputXML.get(i).get(12)!=null && !OutputXML.get(i).get(12).isEmpty() &&  !"".equals(OutputXML.get(i).get(12)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(12)) ){
					
					totalnoofinstalments = OutputXML.get(i).get(12);
				}
				if(!(OutputXML.get(i).get(13) == null || "".equals(OutputXML.get(i).get(13))) ){
					remaininginstalments = OutputXML.get(i).get(13);
				}				
				
				if(!(OutputXML.get(i).get(15) == null || "".equals(OutputXML.get(i).get(15))) ){
					overdueamt = OutputXML.get(i).get(15);
				}
				if(!(OutputXML.get(i).get(16) == null || "".equals(OutputXML.get(i).get(16))) ){
					nofdayspmtdelay = OutputXML.get(i).get(16);
				}				
				if(!(OutputXML.get(i).get(17) == null || "".equals(OutputXML.get(i).get(17))) ){
					monthsonbook = OutputXML.get(i).get(17);
				}
				if(!(OutputXML.get(i).get(18) == null || "".equals(OutputXML.get(i).get(18))) ){
					currentlycurrent = OutputXML.get(i).get(18);
				}
				if(!(OutputXML.get(i).get(19) == null || "".equals(OutputXML.get(i).get(19))) ){
					currmaxutil = OutputXML.get(i).get(19);
				}				
				if(!(OutputXML.get(i).get(20) == null || "".equals(OutputXML.get(i).get(20))) ){
					DPD_30_in_last_6_months = OutputXML.get(i).get(20);
				}
				if(!(OutputXML.get(i).get(21) == null || "".equals(OutputXML.get(i).get(21))) ){
					DPD_60_in_last_18_months = OutputXML.get(i).get(21);
				}
				if(!(OutputXML.get(i).get(22) == null || "".equals(OutputXML.get(i).get(22))) ){
					propertyvalue = OutputXML.get(i).get(22);
				}				
				if(!(OutputXML.get(i).get(23) == null || "".equals(OutputXML.get(i).get(23))) ){
					loan_disbursal_date = OutputXML.get(i).get(23);
				}
				if(!(OutputXML.get(i).get(24) == null || "".equals(OutputXML.get(i).get(24))) ){
					marketingcode = OutputXML.get(i).get(24);
				}
				if(!(OutputXML.get(i).get(25) == null || "".equals(OutputXML.get(i).get(25))) ){
					DPD_30_in_last_3_months = OutputXML.get(i).get(25);
				}				
				if(!(OutputXML.get(i).get(26) == null || "".equals(OutputXML.get(i).get(26))) ){
					DPD_30_in_last_9_months	 = OutputXML.get(i).get(26);
				}
				if(!(OutputXML.get(i).get(27) == null || "".equals(OutputXML.get(i).get(27))) ){
					DPD_30_in_last_12_months = OutputXML.get(i).get(27);
				}
				if(!(OutputXML.get(i).get(28) == null || "".equals(OutputXML.get(i).get(28))) ){
					DPD_30_in_last_18_months = OutputXML.get(i).get(28);
				}				
				if(!(OutputXML.get(i).get(29) == null || "".equals(OutputXML.get(i).get(29))) ){
					DPD_30_in_last_24_months = OutputXML.get(i).get(29);
				}
				if(!(OutputXML.get(i).get(30) == null || "".equals(OutputXML.get(i).get(30))) ){
					DPD_60_in_last_3_months = OutputXML.get(i).get(30);
				}
				if(!(OutputXML.get(i).get(31) == null || "".equals(OutputXML.get(i).get(31))) ){
					DPD_60_in_last_6_months = OutputXML.get(i).get(31);
				}				
				if(!(OutputXML.get(i).get(32) == null || "".equals(OutputXML.get(i).get(32))) ){
					DPD_60_in_last_9_months = OutputXML.get(i).get(32);
				}
				if(!(OutputXML.get(i).get(33) == null || "".equals(OutputXML.get(i).get(33))) ){
					DPD_60_in_last_12_months = OutputXML.get(i).get(33);
				}
				if(!(OutputXML.get(i).get(34) == null || "".equals(OutputXML.get(i).get(34))) ){
					DPD_60_in_last_24_months = OutputXML.get(i).get(34);
				}				
				if(!(OutputXML.get(i).get(35) == null || "".equals(OutputXML.get(i).get(35))) ){
					DPD_90_in_last_3_months = OutputXML.get(i).get(35);
				}
				if(!(OutputXML.get(i).get(36) == null || "".equals(OutputXML.get(i).get(36))) ){
					DPD_90_in_last_6_months = OutputXML.get(i).get(36);
				}
				if(!(OutputXML.get(i).get(37) == null || "".equals(OutputXML.get(i).get(37))) ){
					DPD_90_in_last_9_months = OutputXML.get(i).get(37);
				}				
				if(!(OutputXML.get(i).get(38) == null || "".equals(OutputXML.get(i).get(38))) ){
					DPD_90_in_last_12_months = OutputXML.get(i).get(38);
				}
				if(!(OutputXML.get(i).get(39) == null || "".equals(OutputXML.get(i).get(39))) ){
					DPD_90_in_last_18_months = OutputXML.get(i).get(39);
				}
				if(!(OutputXML.get(i).get(40) == null || "".equals(OutputXML.get(i).get(40))) ){
					DPD_90_in_last_24_months = OutputXML.get(i).get(40);
				}				
				if(!(OutputXML.get(i).get(41) == null || "".equals(OutputXML.get(i).get(41))) ){
					DPD_120_in_last_3_months = OutputXML.get(i).get(41);
				}
				if(!(OutputXML.get(i).get(42) == null || "".equals(OutputXML.get(i).get(42))) ){
					DPD_120_in_last_6_months = OutputXML.get(i).get(42);
				}
				if(!(OutputXML.get(i).get(43) == null || "".equals(OutputXML.get(i).get(43))) ){
					DPD_120_in_last_9_months = OutputXML.get(i).get(43);
				}				
				if(!(OutputXML.get(i).get(44) == null || "".equals(OutputXML.get(i).get(44))) ){
					DPD_120_in_last_12_months = OutputXML.get(i).get(44);
				}
				if(!(OutputXML.get(i).get(45) == null || "".equals(OutputXML.get(i).get(45))) ){
					DPD_120_in_last_18_months = OutputXML.get(i).get(45);
				}
				if(!(OutputXML.get(i).get(46) == null || "".equals(OutputXML.get(i).get(46))) ){
					DPD_120_in_last_24_months = OutputXML.get(i).get(46);
				}				
				if(!(OutputXML.get(i).get(47) == null || "".equals(OutputXML.get(i).get(47))) ){
					DPD_150_in_last_3_months = OutputXML.get(i).get(47);
				}
				if(!(OutputXML.get(i).get(48) == null || "".equals(OutputXML.get(i).get(48))) ){
					DPD_150_in_last_6_months = OutputXML.get(i).get(48);
				}
				if(!(OutputXML.get(i).get(49) == null || "".equals(OutputXML.get(i).get(49))) ){
					DPD_150_in_last_9_months = OutputXML.get(i).get(49);
				}				
				if(!(OutputXML.get(i).get(50) == null || "".equals(OutputXML.get(i).get(50))) ){
					DPD_150_in_last_12_months = OutputXML.get(i).get(50);
				}
				if(!(OutputXML.get(i).get(51) == null || "".equals(OutputXML.get(i).get(51))) ){
					DPD_150_in_last_18_months = OutputXML.get(i).get(51);
				}
				if(!(OutputXML.get(i).get(52) == null || "".equals(OutputXML.get(i).get(52))) ){
					DPD_150_in_last_24_months = OutputXML.get(i).get(52);
				}				
				if(!(OutputXML.get(i).get(53) == null || "".equals(OutputXML.get(i).get(53))) ){
					DPD_180_in_last_3_months = OutputXML.get(i).get(53);
				}
				if(!(OutputXML.get(i).get(54) == null || "".equals(OutputXML.get(i).get(54))) ){
					DPD_180_in_last_6_months = OutputXML.get(i).get(54);
				}
				if(!(OutputXML.get(i).get(55) == null || "".equals(OutputXML.get(i).get(55))) ){
					DPD_180_in_last_9_months = OutputXML.get(i).get(55);
				}				
				if(!(OutputXML.get(i).get(56) == null || "".equals(OutputXML.get(i).get(56))) ){
					DPD_180_in_last_12_months = OutputXML.get(i).get(56);
				}
				if(!(OutputXML.get(i).get(57) == null || "".equals(OutputXML.get(i).get(57))) ){
					DPD_180_in_last_24_months = OutputXML.get(i).get(57);
				}
				if(!(OutputXML.get(i).get(60) == null || "".equals(OutputXML.get(i).get(60))) ){
					CardExpiryDate = OutputXML.get(i).get(60);
				}

				if(!(OutputXML.get(i).get(61) == null || "".equals(OutputXML.get(i).get(61))) ){
					Consider_For_Obligations = OutputXML.get(i).get(61);
					if ("false".equalsIgnoreCase(Consider_For_Obligations)){
						Consider_For_Obligations="N";
					}
					else {
						Consider_For_Obligations="Y";
					}
				}

				if(!(OutputXML.get(i).get(62) == null || "".equals(OutputXML.get(i).get(62))) ){
					phase = OutputXML.get(i).get(62);
					if (phase.startsWith("C")){
						phase="C";
					}
					else {
						phase="A";
					}
				}
				if(!(OutputXML.get(i).get(64) == null || "".equals(OutputXML.get(i).get(64))) ){
					writeoffStat = OutputXML.get(i).get(64);
				}
				if(!(OutputXML.get(i).get(65) == null || "".equals(OutputXML.get(i).get(65))) ){
					writeoffstatdt = OutputXML.get(i).get(65);
				}
				if(!(OutputXML.get(i).get(66) == null || "".equals(OutputXML.get(i).get(66))) ){
					lastrepmtdt = OutputXML.get(i).get(66);
				}
				if(!(OutputXML.get(i).get(67) == null || "".equals(OutputXML.get(i).get(67))) ){
					Limit_increase = OutputXML.get(i).get(67);
					if ("false".equalsIgnoreCase(Limit_increase)){
						Limit_increase="Y";
					}
					else{
						Limit_increase="N";
					}
				}
				if(!(OutputXML.get(i).get(68) == null || "".equals(OutputXML.get(i).get(68))) ){
					part_settlement_date = OutputXML.get(i).get(67);
					String abc=OutputXML.get(i).get(68);
					abc=abc.substring(0, 10)+"split"+abc.substring(10,abc.length() );
					String[] abcsa=abc.split("split");
					part_settlement_date = abcsa[0];
					part_settlement_amount = abcsa[1];
				}
				if(!(OutputXML.get(i).get(69) == null || "".equals(OutputXML.get(i).get(69))) ){
					SchemeCardProduct = OutputXML.get(i).get(69);
				}
				if(!(OutputXML.get(i).get(70) == null || "".equals(OutputXML.get(i).get(70))) ){
					General_Status = OutputXML.get(i).get(70);
				}
				if(!(OutputXML.get(i).get(71) == null || "".equals(OutputXML.get(i).get(71))) ){
					Internal_WriteOff_Check = OutputXML.get(i).get(71);
				}
				String sQueryCombinedLimit = "select Distinct(COMBINEDLIMIT_ELIGIBILITY) from ng_master_cardProduct where code='"+SchemeCardProduct+"'";
				List<List<String>> sQueryCombinedLimitXML = formObject.getDataFromDataSource(sQueryCombinedLimit);
				if (sQueryCombinedLimitXML!= null){
					Combined_Limit="1".equalsIgnoreCase(sQueryCombinedLimitXML.get(0).get(0))?"Y":"N";
				}
				String sQuerySecuredCard = "select count(*) from ng_master_cardProduct where code='"+SchemeCardProduct+"'  and subproduct='SEC'";
				List<List<String>> sQuerySecuredCardXML = formObject.getDataFromDataSource(sQuerySecuredCard);
				if (sQuerySecuredCardXML!= null){
					SecuredCard="0".equalsIgnoreCase(sQuerySecuredCardXML.get(0).get(0))?"N":"Y";
				}

				if(cifId!=null && !"".equalsIgnoreCase(cifId) && !"null".equalsIgnoreCase(cifId)){
					PersonalLoanS.mLogger.info("Inside if"+ "asdasdasd");
					add_xml_str = add_xml_str + "<InternalBureauIndividualProducts><applicant_id>"+cifId+"</applicant_id>";
					add_xml_str = add_xml_str + "<internal_bureau_individual_products_id>"+agreementId+"</internal_bureau_individual_products_id>";
					add_xml_str = add_xml_str + "<type_product>"+product_type+"</type_product>";
					if ("cards".equalsIgnoreCase(OutputXML.get(i).get(63))){					
						add_xml_str = add_xml_str + "<contract_type>CC</contract_type>";
					}
					if ("Loans".equalsIgnoreCase(OutputXML.get(i).get(63))){
						add_xml_str = add_xml_str + "<contract_type>PL</contract_type>";
					}	
					add_xml_str = add_xml_str + "<provider_no>"+"RAKBANK"+"</provider_no>";
					add_xml_str = add_xml_str + "<phase>"+phase+"</phase>";
					add_xml_str = add_xml_str + "<role_of_customer>Primary</role_of_customer>"; 

					add_xml_str = add_xml_str + "<start_date>"+loan_start_date+"</start_date>"; 
					add_xml_str = add_xml_str + "<close_date>"+loanmaturitydate+"</close_date>"; 
					add_xml_str = add_xml_str + "<date_last_updated>"+lastupdatedate+"</date_last_updated>"; 
					add_xml_str = add_xml_str + "<outstanding_balance>"+outstandingamt+"</outstanding_balance>"; 
					if ("Loans".equalsIgnoreCase(OutputXML.get(i).get(63))){
						add_xml_str = add_xml_str + "<total_amount>"+totalloanamount+"</total_amount>";
					}
					add_xml_str = add_xml_str + "<payments_amount>"+Emi+"</payments_amount>"; 
					add_xml_str = add_xml_str + "<method_of_payment>"+paymentmode+"</method_of_payment>"; 
					add_xml_str = add_xml_str + "<total_no_of_instalments>"+totalnoofinstalments+"</total_no_of_instalments>"; 
					add_xml_str = add_xml_str + "<no_of_remaining_instalments>"+remaininginstalments+"</no_of_remaining_instalments>"; 
					add_xml_str = add_xml_str + "<worst_status>"+writeoffStat+"</worst_status>"; 
					add_xml_str = add_xml_str + "<worst_status_date>"+writeoffstatdt+"</worst_status_date>"; 
					if ("cards".equalsIgnoreCase(OutputXML.get(i).get(63))){

						add_xml_str = add_xml_str + "<credit_limit>"+totalloanamount+"</credit_limit>"; 
					}
					add_xml_str = add_xml_str + "<overdue_amount>"+overdueamt+"</overdue_amount>"; 
					add_xml_str = add_xml_str + "<no_of_days_payment_delay>"+nofdayspmtdelay+"</no_of_days_payment_delay>"; 
					add_xml_str = add_xml_str + "<mob>"+monthsonbook+"</mob>"; 
					add_xml_str = add_xml_str + "<last_repayment_date>"+lastrepmtdt+"</last_repayment_date>"; 
					add_xml_str = add_xml_str + "<currently_current>"+currentlycurrent+"</currently_current>"; 
					add_xml_str = add_xml_str + "<current_utilization>"+currmaxutil+"</current_utilization>"; 
					add_xml_str = add_xml_str + "<dpd_30_last_6_mon>"+DPD_30_in_last_6_months+"</dpd_30_last_6_mon>"; 
					add_xml_str = add_xml_str + "<dpd_60p_in_last_18_mon>"+DPD_60_in_last_18_months+"</dpd_60p_in_last_18_mon>"; 


					add_xml_str = add_xml_str + "<card_product>"+SchemeCardProduct+"</card_product>"; 
					add_xml_str = add_xml_str + "<property_value>"+propertyvalue+"</property_value>"; 
					add_xml_str = add_xml_str + "<disbursal_date>"+loan_disbursal_date+"</disbursal_date>"; 
					add_xml_str = add_xml_str + "<marketing_code>"+marketingcode+"</marketing_code>"; 
					add_xml_str = add_xml_str + "<card_expiry_date>"+CardExpiryDate+"</card_expiry_date>"; 
					add_xml_str = add_xml_str + "<card_upgrade_indicator>"+Limit_increase+"</card_upgrade_indicator>"; 
					add_xml_str = add_xml_str + "<part_settlement_date>"+part_settlement_date+"</part_settlement_date>"; 
					add_xml_str = add_xml_str + "<part_settlement_amount>"+part_settlement_amount+"</part_settlement_amount>"; 
					add_xml_str = add_xml_str + "<part_settlement_reason>"+""+"</part_settlement_reason>"; 
					add_xml_str = add_xml_str + "<limit_expiry_date>"+""+"</limit_expiry_date>"; 
					add_xml_str = add_xml_str + "<no_of_primary_cards>"+CountXML.get(0).get(0)+"</no_of_primary_cards>"; 
					add_xml_str = add_xml_str + "<no_of_repayments_done>"+remaininginstalments+"</no_of_repayments_done>"; 
					add_xml_str = add_xml_str + "<card_segment>"+SchemeCardProduct+"</card_segment>"; 
					add_xml_str = add_xml_str + "<product_type>"+OutputXML.get(i).get(63)+"</product_type>"; 
					add_xml_str = add_xml_str + "<product_category>"+SchemeCardProduct+"</product_category>"; 
					add_xml_str = add_xml_str + "<combined_limit_flag>"+Combined_Limit+"</combined_limit_flag>"; 
					add_xml_str = add_xml_str + "<secured_card_flag>"+SecuredCard+"</secured_card_flag>"; 
					add_xml_str = add_xml_str + "<resch_tko_flag>"+Limit_increase+"</resch_tko_flag>"; 

					add_xml_str = add_xml_str + "<general_status>"+General_Status+"</general_status>"; 
					add_xml_str = add_xml_str + "<consider_for_obligation>"+Consider_For_Obligations+"</consider_for_obligation>"; 
					add_xml_str = add_xml_str + "<limit_increase>"+Limit_increase+"</limit_increase>"; 

					add_xml_str = add_xml_str + "<role>Primary</role>"; 
					add_xml_str = add_xml_str + "<limit>"+""+"</limit>"; 
					add_xml_str = add_xml_str + "<status>"+phase+"</status>";
					add_xml_str = add_xml_str + "<emi>"+Emi+"</emi>"; 
					add_xml_str = add_xml_str + "<os_amt>"+outstandingamt+"</os_amt>"; 

					add_xml_str = add_xml_str + "<dpd_30_in_last_3mon>"+DPD_30_in_last_3_months+"</dpd_30_in_last_3mon>"; 
					add_xml_str = add_xml_str + "<dpd_30_in_last_6mon>"+DPD_30_in_last_6_months+"</dpd_30_in_last_6mon>"; 
					add_xml_str = add_xml_str + "<dpd_30_in_last_9mon>"+DPD_30_in_last_9_months+"</dpd_30_in_last_9mon>"; 
					add_xml_str = add_xml_str + "<dpd_30_in_last_12mon>"+DPD_30_in_last_12_months+"</dpd_30_in_last_12mon>";

					add_xml_str = add_xml_str + "<dpd_30_in_last_18mon>"+DPD_30_in_last_18_months+"</dpd_30_in_last_18mon>"; 
					add_xml_str = add_xml_str + "<dpd_30_in_last_24mon>"+DPD_30_in_last_24_months+"</dpd_30_in_last_24mon>"; 
					add_xml_str = add_xml_str + "<dpd_60_in_last_3mon>"+DPD_60_in_last_3_months+"</dpd_60_in_last_3mon>"; 
					add_xml_str = add_xml_str + "<dpd_60_in_last_6mon>"+DPD_60_in_last_6_months+"</dpd_60_in_last_6mon>"; 
					add_xml_str = add_xml_str + "<dpd_60_in_last_9mon>"+DPD_60_in_last_9_months+"</dpd_60_in_last_9mon>"; 
					add_xml_str = add_xml_str + "<dpd_60_in_last_12mon>"+DPD_60_in_last_12_months+"</dpd_60_in_last_12mon>"; 
					add_xml_str = add_xml_str + "<dpd_60_in_last_18mon>"+DPD_60_in_last_18_months+"</dpd_60_in_last_18mon>"; 
					add_xml_str = add_xml_str + "<dpd_60_in_last_24mon>"+DPD_60_in_last_24_months+"</dpd_60_in_last_24mon>"; 
					add_xml_str = add_xml_str + "<dpd_90_in_last_3mon>"+DPD_90_in_last_3_months+"</dpd_90_in_last_3mon>"; 
					add_xml_str = add_xml_str + "<dpd_90_in_last_6mon>"+DPD_90_in_last_6_months+"</dpd_90_in_last_6mon>"; 
					add_xml_str = add_xml_str + "<dpd_90_in_last_9mon>"+DPD_90_in_last_9_months+"</dpd_90_in_last_9mon>"; 
					add_xml_str = add_xml_str + "<dpd_90_in_last_12mon>"+DPD_90_in_last_12_months+"</dpd_90_in_last_12mon>"; 
					add_xml_str = add_xml_str + "<dpd_90_in_last_18mon>"+DPD_90_in_last_18_months+"</dpd_90_in_last_18mon>"; 
					add_xml_str = add_xml_str + "<dpd_90_in_last_24mon>"+DPD_90_in_last_24_months+"</dpd_90_in_last_24mon>"; 
					add_xml_str = add_xml_str + "<dpd_120_in_last_3mon>"+DPD_120_in_last_3_months+"</dpd_120_in_last_3mon>"; 
					add_xml_str = add_xml_str + "<dpd_120_in_last_6mon>"+DPD_120_in_last_6_months+"</dpd_120_in_last_6mon>"; 
					add_xml_str = add_xml_str + "<dpd_120_in_last_9mon>"+DPD_120_in_last_9_months+"</dpd_120_in_last_9mon>"; 
					add_xml_str = add_xml_str + "<dpd_120_in_last_12mon>"+DPD_120_in_last_12_months+"</dpd_120_in_last_12mon>"; 
					add_xml_str = add_xml_str + "<dpd_120_in_last_18mon>"+DPD_120_in_last_18_months+"</dpd_120_in_last_18mon>"; 
					add_xml_str = add_xml_str + "<dpd_120_in_last_24mon>"+DPD_120_in_last_24_months+"</dpd_120_in_last_24mon>";

					add_xml_str = add_xml_str + "<dpd_150_in_last_3mon>"+DPD_150_in_last_3_months+"</dpd_150_in_last_3mon>"; 
					add_xml_str = add_xml_str + "<dpd_150_in_last_6mon>"+DPD_150_in_last_6_months+"</dpd_150_in_last_6mon>"; 
					add_xml_str = add_xml_str + "<dpd_150_in_last_9mon>"+DPD_150_in_last_9_months+"</dpd_150_in_last_9mon>"; 
					add_xml_str = add_xml_str + "<dpd_150_in_last_12mon>"+DPD_150_in_last_12_months+"</dpd_150_in_last_12mon>"; 
					add_xml_str = add_xml_str + "<dpd_150_in_last_18mon>"+DPD_150_in_last_18_months+"</dpd_150_in_last_18mon>"; 
					add_xml_str = add_xml_str + "<dpd_150_in_last_24mon>"+DPD_150_in_last_24_months+"</dpd_150_in_last_24mon>"; 
					add_xml_str = add_xml_str + "<dpd_180_in_last_3mon>"+DPD_180_in_last_3_months+"</dpd_180_in_last_3mon>"; 
					add_xml_str = add_xml_str + "<dpd_180_in_last_6mon>"+DPD_180_in_last_6_months+"</dpd_180_in_last_6mon>"; 
					add_xml_str = add_xml_str + "<dpd_180_in_last_9mon>"+DPD_180_in_last_9_months+"</dpd_180_in_last_9mon>"; 
					add_xml_str = add_xml_str + "<dpd_180_in_last_12mon>"+DPD_180_in_last_12_months+"</dpd_180_in_last_12mon>"; 
					add_xml_str = add_xml_str + "<dpd_180_in_last_18mon>"+""+"</dpd_180_in_last_18mon>"; 
					add_xml_str = add_xml_str + "<dpd_180_in_last_24mon>"+DPD_180_in_last_24_months+"</dpd_180_in_last_24mon>"; 
					add_xml_str = add_xml_str + "<last_temp_limit_exp>"+""+"</last_temp_limit_exp>"; 
					add_xml_str = add_xml_str + "<last_per_limit_exp>"+""+"</last_per_limit_exp>"; 
					add_xml_str = add_xml_str + "<security_cheque_amt>"+""+"</security_cheque_amt>"; 
					add_xml_str = add_xml_str + "<mol_salary_variance>"+mol_sal_var+"</mol_salary_variance>";
					if(Kompass!=null){
						if("true".equalsIgnoreCase(Kompass)){
							add_xml_str = add_xml_str + "<kompass>"+"Y"+"</kompass>";
						}
						else{
							add_xml_str = add_xml_str + "<kompass>"+"N"+"</kompass>";
						}
					}
					add_xml_str = add_xml_str + "<employer_type>"+EmployerType+"</employer_type>"; 


					if (totalnoofinstalments!=null && remaininginstalments!=null && !"".equals(totalnoofinstalments) &&  !"".equals(remaininginstalments)) {
						paid_installment= Integer.toString(Integer.parseInt(totalnoofinstalments) -Integer.parseInt(remaininginstalments));
						PersonalLoanS.mLogger.info("Inside paid_installment"+ "paid_installment"+paid_installment);

					}
					if ("Credit Card".equalsIgnoreCase(ReqProd)){

						add_xml_str = add_xml_str + "<no_of_paid_installment>"+paid_installment+"</no_of_paid_installment><company_flag>N</company_flag></InternalBureauIndividualProducts>";
					}
					else{
						add_xml_str = add_xml_str + "<no_of_paid_installment>"+paid_installment+"</no_of_paid_installment><write_off_amount>"+Internal_WriteOff_Check+"</write_off_amount><company_flag>N</company_flag><type_of_od>"+""+"</type_of_od><amt_paid_last6mnths>"+""+"</amt_paid_last6mnths></InternalBureauIndividualProducts>";

					}
				}	

			}
		}
		catch(Exception e){
			new PersonalLoanSCommonCode();
			PersonalLoanS.mLogger.info("RLOSCommon"+ "Internal liab tag Cration: ");
			PLCommon.printException(e);
		}
		PersonalLoanS.mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Generate XML for InternalBureauPipelineProducts    
	 
	***********************************************************************************  */

	public String InternalBureauPipelineProducts(){
		PersonalLoanS.mLogger.info("RLOSCommon java file"+ "inside InternalBureauPipelineProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "SELECT cifid,product_type,custroletype,lastupdatedate,totalamount,totalnoofinstalments,totalloanamount,agreementId FROM ng_RLOS_CUSTEXPOSE_LoanDetails  with (nolock) where Child_wi = '"+formObject.getWFWorkitemName()+"' and  LoanStat = 'Pipeline'";
		PersonalLoanS.mLogger.info("InternalBureauPipelineProducts sQuery"+sQuery+ "");
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		PersonalLoanS.mLogger.info("InternalBureauPipelineProducts list size"+OutputXML.size()+ "");

		for (int i = 0; i<OutputXML.size();i++){

			String cifId = "";
			String Product = "";
			String lastUpdateDate = ""; 
			String TotAmount = "";
			String TotNoOfInstlmnt = "";
			String TotLoanAmt = "";
			String agreementId = "";
			if(!(OutputXML.get(i).get(0) == null || "".equals(OutputXML.get(i).get(0))) ){
				cifId = OutputXML.get(i).get(0);
			}
			if(!(OutputXML.get(i).get(1) == null || "".equals(OutputXML.get(i).get(1))) ){
				Product = OutputXML.get(i).get(1);
			}
			
			if(!(OutputXML.get(i).get(3) == null || "".equals(OutputXML.get(i).get(3))) ){
				lastUpdateDate = OutputXML.get(i).get(3);
			}
			if(!(OutputXML.get(i).get(4) == null || "".equals(OutputXML.get(i).get(4))) ){
				TotAmount = OutputXML.get(i).get(4);
			}
			if(!(OutputXML.get(i).get(5) == null || "".equals(OutputXML.get(i).get(5))) ){
				TotNoOfInstlmnt = OutputXML.get(i).get(5);
			}
			if(!(OutputXML.get(i).get(6) == null || "".equals(OutputXML.get(i).get(6))) ){
				TotLoanAmt = OutputXML.get(i).get(6);
			}
			if(!(OutputXML.get(i).get(7) == null || "".equals(OutputXML.get(i).get(7)))) {
				agreementId = OutputXML.get(i).get(7);
			}
			if(cifId!=null && !"".equalsIgnoreCase(cifId) && !"null".equalsIgnoreCase(cifId)){
				add_xml_str = add_xml_str + "<InternalBureauPipelineProducts>";// to be populated later
				add_xml_str = add_xml_str + "<applicant_id>"+cifId+"</applicant_id>";
				add_xml_str = add_xml_str + "<internal_bureau_pipeline_products_id>"+agreementId+"</internal_bureau_pipeline_products_id>";// to be populated later
				add_xml_str = add_xml_str + "<ppl_provider_no>"+""+"</ppl_provider_no>";
				add_xml_str = add_xml_str + "<ppl_product>"+Product+"</ppl_product>";
				add_xml_str = add_xml_str + "<ppl_type_of_contract>"+""+"</ppl_type_of_contract>";
				add_xml_str = add_xml_str + "<ppl_phase>"+""+"</ppl_phase>"; // to be populated later

				add_xml_str = add_xml_str + "<ppl_role>"+Product+"</ppl_role>";
				add_xml_str = add_xml_str + "<ppl_date_of_last_update>"+lastUpdateDate+"</ppl_date_of_last_update>";
				add_xml_str = add_xml_str + "<ppl_total_amount>"+TotAmount+"</ppl_total_amount>";
				add_xml_str = add_xml_str + "<ppl_no_of_instalments>"+TotNoOfInstlmnt+"</ppl_no_of_instalments>";
				add_xml_str = add_xml_str + "<ppl_credit_limit>"+TotLoanAmt+"</ppl_credit_limit>";

				add_xml_str = add_xml_str + "<ppl_no_of_days_in_pipeline>"+""+"</ppl_no_of_days_in_pipeline><company_flag>N</company_flag></InternalBureauPipelineProducts>"; // to be populated later
			}

		}
		PersonalLoanS.mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Generate XML for ExternalBureauData
	 
	***********************************************************************************  */


	public String ExternalBureauData(){
		PersonalLoanS.mLogger.info("RLOSCommon java file"+ "inside ExternalBureauData : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "select  CifId,fullnm,TotalOutstanding,TotalOverdue,NoOfContracts,Total_Exposure,WorstCurrentPaymentDelay,Worst_PaymentDelay_Last24Months,Worst_Status_Last24Months,Nof_Records,NoOf_Cheque_Return_Last3,Nof_DDES_Return_Last3Months,Nof_Cheque_Return_Last6,DPD30_Last6Months,Internal_WriteOff_Check from NG_rlos_custexpose_Derived where Child_wi  = '"+formObject.getWFWorkitemName()+"' and Request_type= 'ExternalExposure'";
		PersonalLoanS.mLogger.info("ExternalBureauData sQuery"+sQuery+ "");
		String AecbHistQuery = "select isnull(max(AECBHistMonthCnt),0) as AECBHistMonthCnt from ( select MAX(AECBHistMonthCnt) as AECBHistMonthCnt  from ng_rlos_cust_extexpo_CardDetails where  wi_name  = '"+formObject.getWFWorkitemName()+"' union select Max(AECBHistMonthCnt) from ng_rlos_cust_extexpo_LoanDetails where Child_wi  = '"+formObject.getWFWorkitemName()+"') as ext_expo";
		String  add_xml_str ="";
		try{
			List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
			PersonalLoanS.mLogger.info("ExternalBureauData list size"+OutputXML.size()+ "");
			List<List<String>> AecbHistQueryData = formObject.getDataFromDataSource(AecbHistQuery);
			
			if ("0".equalsIgnoreCase(AecbHistQueryData.get(0).get(0))){


				String CifId = formObject.getNGValue("cmplx_Customer_CIFNO");
				String fullnm=formObject.getNGValue("cmplx_Customer_FIrstNAme")+" "+formObject.getNGValue("cmplx_Customer_LAstNAme");
				String TotalOutstanding="";
				String TotalOverdue="";
				String NoOfContracts="";
				String Total_Exposure="";
				String WorstCurrentPaymentDelay="";
				String Worst_PaymentDelay_Last24Months="";
				String Worst_Status_Last24Months="";
				String Nof_Records="";
				String NoOf_Cheque_Return_Last3="";
				String Nof_DDES_Return_Last3Months="";
				String Nof_Cheque_Return_Last6="";
				String DPD30_Last6Months="";
				add_xml_str = add_xml_str + "<ExternalBureau>"; 
				add_xml_str = add_xml_str + "<applicant_id>"+CifId+"</applicant_id>";

				add_xml_str = add_xml_str + "<full_name>"+fullnm+"</full_name>"; 
				add_xml_str = add_xml_str + "<total_out_bal>"+TotalOutstanding+"</total_out_bal>";

				add_xml_str = add_xml_str + "<total_overdue>"+TotalOverdue+"</total_overdue>";
				add_xml_str = add_xml_str + "<no_default_contract>"+NoOfContracts+"</no_default_contract>";
				add_xml_str = add_xml_str + "<total_exposure>"+Total_Exposure+"</total_exposure>";
				add_xml_str = add_xml_str + "<worst_curr_pay>"+WorstCurrentPaymentDelay+"</worst_curr_pay>";
				add_xml_str = add_xml_str + "<worst_curr_pay_24>"+Worst_PaymentDelay_Last24Months+"</worst_curr_pay_24>"; 
				add_xml_str = add_xml_str + "<worst_status_24>"+Worst_Status_Last24Months+"</worst_status_24>"; 


				add_xml_str = add_xml_str + "<no_of_rec>"+Nof_Records+"</no_of_rec>"; 
				add_xml_str = add_xml_str + "<cheque_return_3mon>"+NoOf_Cheque_Return_Last3+"</cheque_return_3mon>";
				add_xml_str = add_xml_str + "<dds_return_3mon>"+Nof_DDES_Return_Last3Months+"</dds_return_3mon>";
				add_xml_str = add_xml_str + "<cheque_return_6mon>"+Nof_Cheque_Return_Last6+"</cheque_return_6mon>";
				add_xml_str = add_xml_str + "<dds_return_6mon>"+DPD30_Last6Months+"</dds_return_6mon>";
				add_xml_str = add_xml_str + "<prod_external_writeoff_amount>"+""+"</prod_external_writeoff_amount>";

				add_xml_str = add_xml_str + "<no_months_aecb_history >"+AecbHistQueryData.get(0).get(0)+"</no_months_aecb_history >";

				add_xml_str = add_xml_str + "<company_flag>N</company_flag></ExternalBureau>";



				PersonalLoanS.mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "+ add_xml_str);
				return add_xml_str;
			}
			else{
				for (int i = 0; i<OutputXML.size();i++){

					String CifId = formObject.getNGValue("cmplx_Customer_CIFNO");
					String fullnm="";
					String TotalOutstanding="";
					String TotalOverdue="";
					String NoOfContracts="";
					String Total_Exposure="";
					String WorstCurrentPaymentDelay="";
					String Worst_PaymentDelay_Last24Months="";
					String Worst_Status_Last24Months="";
					String Nof_Records="";
					String NoOf_Cheque_Return_Last3="";
					String Nof_DDES_Return_Last3Months="";
					String Nof_Cheque_Return_Last6="";
					String DPD30_Last6Months="";
					if(!(OutputXML.get(i).get(1) == null || "".equals(OutputXML.get(i).get(1))) ){
						fullnm = OutputXML.get(i).get(1);
					}				
					if(!(OutputXML.get(i).get(2) == null || "".equals(OutputXML.get(i).get(2))) ){
						TotalOutstanding = OutputXML.get(i).get(2);

					}
					if(!(OutputXML.get(i).get(3) == null || "".equals(OutputXML.get(i).get(3))) ){
						TotalOverdue = OutputXML.get(i).get(3);
					}
					if(!(OutputXML.get(i).get(4) == null || "".equals(OutputXML.get(i).get(4))) ){
						NoOfContracts = OutputXML.get(i).get(4);
					}				
					if(!(OutputXML.get(i).get(5) == null || "".equals(OutputXML.get(i).get(5))) ){
						Total_Exposure = OutputXML.get(i).get(5);
					}
					if(OutputXML.get(i).get(6)!=null && !OutputXML.get(i).get(6).isEmpty() &&  !"".equals(OutputXML.get(i).get(6)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(6)) ){
						WorstCurrentPaymentDelay = OutputXML.get(i).get(6);
					}
					if(OutputXML.get(i).get(7)!=null && !OutputXML.get(i).get(7).isEmpty() &&  !"".equals(OutputXML.get(i).get(7)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(7)) ){
						Worst_PaymentDelay_Last24Months = OutputXML.get(i).get(7);
					}				
					if(OutputXML.get(i).get(8)!=null && !OutputXML.get(i).get(8).isEmpty() &&  !"".equals(OutputXML.get(i).get(8)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(8)) ){
						Worst_Status_Last24Months = OutputXML.get(i).get(8);
					}
					if(!(OutputXML.get(i).get(9) == null || "".equals(OutputXML.get(i).get(9))) ){
						Nof_Records = OutputXML.get(i).get(9);
					}
					if(!(OutputXML.get(i).get(10) == null || "".equals(OutputXML.get(i).get(10))) ){
						NoOf_Cheque_Return_Last3 = OutputXML.get(i).get(10);
					}				
					if(!(OutputXML.get(i).get(11) == null || "".equals(OutputXML.get(i).get(11))) ){
						Nof_DDES_Return_Last3Months = OutputXML.get(i).get(11);
					}
					if(OutputXML.get(i).get(12)!=null && !OutputXML.get(i).get(12).isEmpty() &&  !"".equals(OutputXML.get(i).get(12)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(12)) ){
						
						Nof_Cheque_Return_Last6 = OutputXML.get(i).get(12);
					}
					if(!(OutputXML.get(i).get(13) == null || "".equals(OutputXML.get(i).get(13))) ){
						DPD30_Last6Months = OutputXML.get(i).get(13);
					}

					add_xml_str = add_xml_str + "<ExternalBureau>"; 
					add_xml_str = add_xml_str + "<applicant_id>"+CifId+"</applicant_id>";
					add_xml_str = add_xml_str + "<full_name>"+fullnm+"</full_name>"; 
					add_xml_str = add_xml_str + "<total_out_bal>"+TotalOutstanding+"</total_out_bal>";

					add_xml_str = add_xml_str + "<total_overdue>"+TotalOverdue+"</total_overdue>";
					add_xml_str = add_xml_str + "<no_default_contract>"+NoOfContracts+"</no_default_contract>";
					add_xml_str = add_xml_str + "<total_exposure>"+Total_Exposure+"</total_exposure>";
					add_xml_str = add_xml_str + "<worst_curr_pay>"+WorstCurrentPaymentDelay+"</worst_curr_pay>";
					add_xml_str = add_xml_str + "<worst_curr_pay_24>"+Worst_PaymentDelay_Last24Months+"</worst_curr_pay_24>"; 
					add_xml_str = add_xml_str + "<worst_status_24>"+Worst_Status_Last24Months+"</worst_status_24>"; 


					add_xml_str = add_xml_str + "<no_of_rec>"+Nof_Records+"</no_of_rec>"; 
					add_xml_str = add_xml_str + "<cheque_return_3mon>"+NoOf_Cheque_Return_Last3+"</cheque_return_3mon>";
					add_xml_str = add_xml_str + "<dds_return_3mon>"+Nof_DDES_Return_Last3Months+"</dds_return_3mon>";
					add_xml_str = add_xml_str + "<cheque_return_6mon>"+Nof_Cheque_Return_Last6+"</cheque_return_6mon>";
					add_xml_str = add_xml_str + "<dds_return_6mon>"+DPD30_Last6Months+"</dds_return_6mon>";
					add_xml_str = add_xml_str + "<prod_external_writeoff_amount>"+""+"</prod_external_writeoff_amount>";

					add_xml_str = add_xml_str + "<no_months_aecb_history >"+AecbHistQueryData.get(0).get(0)+"</no_months_aecb_history >";

					add_xml_str = add_xml_str + "<company_flag>N</company_flag></ExternalBureau>";

				}
				PersonalLoanS.mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "+ add_xml_str);
				return add_xml_str;
			}
		}
		catch(Exception e)
		{
			new PersonalLoanSCommonCode();
			PersonalLoanS.mLogger.info("RLOSCommon"+ "Exception occurred in externalBureauData()");
			PLCommon.printException(e);
			return null;
		}
	}
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Generate XML for ExternalBouncedCheques
	 
	***********************************************************************************  */

	public String ExternalBouncedCheques(){
		PersonalLoanS.mLogger.info("RLOSCommon java file"+ "inside ExternalBouncedCheques : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "SELECT cifid,number,amount,reasoncode,returndate,providerno FROM ng_rlos_cust_extexpo_ChequeDetails  with (nolock) where child_wi = '"+formObject.getWFWorkitemName()+"' and Request_Type = 'ExternalExposure'";
		PersonalLoanS.mLogger.info("ExternalBouncedCheques sQuery"+sQuery+ "");
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		PersonalLoanS.mLogger.info("ExternalBouncedCheques list size"+OutputXML.size()+ "");

		for (int i = 0; i<OutputXML.size();i++){

			String CifId = formObject.getNGValue("cmplx_Customer_CIFNO");
			String chqNo = "";
			String Amount = "";
			String Reason = ""; 
			String returnDate = "";
			String providerNo = "";


			if(!(OutputXML.get(i).get(1) == null || "".equals(OutputXML.get(i).get(1))) ){
				chqNo = OutputXML.get(i).get(1);
			}
			if(!(OutputXML.get(i).get(2) == null || "".equals(OutputXML.get(i).get(2))) ){
				Amount = OutputXML.get(i).get(2);
			}
			if(!(OutputXML.get(i).get(3) == null || "".equals(OutputXML.get(i).get(3))) ){
				Reason = OutputXML.get(i).get(3);
			}
			if(!(OutputXML.get(i).get(4) == null || "".equals(OutputXML.get(i).get(4))) ){
				returnDate = OutputXML.get(i).get(4);
			}
			if(!(OutputXML.get(i).get(5) == null || "".equals(OutputXML.get(i).get(5))) ){
				providerNo = OutputXML.get(i).get(5);
			}


			add_xml_str = add_xml_str + "<ExternalBouncedCheques><applicant_id>"+CifId+"</applicant_id>";
			add_xml_str = add_xml_str + "<external_bounced_cheques_id>"+""+"</external_bounced_cheques_id>";
			add_xml_str = add_xml_str + "<bounced_cheque>"+""+"</bounced_cheque>";
			add_xml_str = add_xml_str + "<cheque_no>"+chqNo+"</cheque_no>";
			add_xml_str = add_xml_str + "<amount>"+Amount+"</amount>";
			add_xml_str = add_xml_str + "<reason>"+Reason+"</reason>";
			add_xml_str = add_xml_str + "<return_date>"+returnDate+"</return_date>"; // to be populated later
			add_xml_str = add_xml_str + "<provider_no>"+providerNo+"</provider_no><company_flag>N</company_flag></ExternalBouncedCheques>"; // to be populated later


		}
		PersonalLoanS.mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}

	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Generate XML for ExternalBureauIndividualProducts
	 
	***********************************************************************************  */

	public String ExternalBureauIndividualProducts(){
		PersonalLoanS.mLogger.info("RLOSCommon java file"+ "inside ExternalBureauIndividualProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//Query Changed on 1st Nov
		String sQuery = "select CifId,AgreementId,LoanType,ProviderNo,LoanStat,CustRoleType,LoanApprovedDate,LoanMaturityDate,OutstandingAmt,TotalAmt,PaymentsAmt,TotalNoOfInstalments,RemainingInstalments,WriteoffStat,WriteoffStatDt,CreditLimit,OverdueAmt,NofDaysPmtDelay,MonthsOnBook,lastrepmtdt,IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last18Months,AECBHistMonthCnt,DPD5_Last3Months,'' as qc_amt,'' as qc_emi,'' as Cac_indicator,Take_Over_Indicator,isnull(Consider_For_Obligations,'true') from ng_rlos_cust_extexpo_LoanDetails where child_wi= '"+formObject.getWFWorkitemName()+"'  and LoanStat != 'Pipeline' union select CifId,CardEmbossNum,CardType,ProviderNo,CardStatus,CustRoleType,StartDate,ClosedDate,CurrentBalance,'' as col6,PaymentsAmount,NoOfInstallments,'' as col5,WriteoffStat,WriteoffStatDt,CashLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,lastrepmtdt,IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last18Months,AECBHistMonthCnt,DPD5_Last3Months,QC_Amt,QC_EMI,CAC_Indicator,Take_Over_Indicator,isnull(Consider_For_Obligations,'true') from ng_rlos_cust_extexpo_CardDetails where child_wi  =  '"+formObject.getWFWorkitemName()+"' and cardstatus != 'Pipeline' ";
		//Query Changed on 1st Nov
		PersonalLoanS.mLogger.info("ExternalBureauIndividualProducts sQuery"+sQuery+ "");
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		PersonalLoanS.mLogger.info("ExternalBureauIndividualProducts list size"+OutputXML.size()+ "");
		String ReqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
		for (int i = 0; i<OutputXML.size();i++){

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
			String consider_for_obligation="";
			if(!(OutputXML.get(i).get(1) == null || "".equals(OutputXML.get(i).get(1))) ){
				AgreementId = OutputXML.get(i).get(1);
			}				
			if(!(OutputXML.get(i).get(2) == null || "".equals(OutputXML.get(i).get(2))) ){
				ContractType = OutputXML.get(i).get(2);
				try{
					String cardquery = "select code from ng_master_contract_type where description='"+ContractType+"'";
					PersonalLoanS.mLogger.info("ExternalBureauIndividualProducts sQuery"+sQuery+ "");
					List<List<String>> cardqueryXML = formObject.getDataFromDataSource(cardquery);
					ContractType=cardqueryXML.get(0).get(0);
					PersonalLoanS.mLogger.info("ExternalBureauIndividualProducts ContractType"+ContractType+ "ContractType");
				}
				catch(Exception e){
					PersonalLoanS.mLogger.info("ExternalBureauIndividualProducts ContractType Exception"+e+ "Exception");

					ContractType= OutputXML.get(i).get(2);
				}
			}
			if(!(OutputXML.get(i).get(3) == null || "".equals(OutputXML.get(i).get(3))) ){
				provider_no = OutputXML.get(i).get(3);
			}
			if(!(OutputXML.get(i).get(4) == null || "".equals(OutputXML.get(i).get(4))) ){
				phase = OutputXML.get(i).get(4);
				if (phase.startsWith("A")){
					phase="A";
				}
				else {
					phase="C";
				}
			}				
			if(!(OutputXML.get(i).get(5) == null || "".equals(OutputXML.get(i).get(5))) ){
				CustRoleType = OutputXML.get(i).get(5);
			}
			if(!(OutputXML.get(i).get(6) == null || "".equals(OutputXML.get(i).get(6))) ){
				start_date = OutputXML.get(i).get(6);
			}
			if(!(OutputXML.get(i).get(7) == null || "".equals(OutputXML.get(i).get(7)))) {
				close_date = OutputXML.get(i).get(7);
			}				
			if(!(OutputXML.get(i).get(8) == null || "".equals(OutputXML.get(i).get(8))) ){
				OutStanding_Balance = OutputXML.get(i).get(8);
			}
			if(!(OutputXML.get(i).get(9) == null || "".equals(OutputXML.get(i).get(9))) ){
				TotalAmt = OutputXML.get(i).get(9);
			}
			if(!(OutputXML.get(i).get(10) == null || "".equals(OutputXML.get(i).get(10))) ){
				PaymentsAmt = OutputXML.get(i).get(10);
			}
			if(!(OutputXML.get(i).get(11) == null || "".equals(OutputXML.get(i).get(11))) ){
				TotalNoOfInstalments = OutputXML.get(i).get(11);
			}
			if(!(OutputXML.get(i).get(12) == null || "".equals(OutputXML.get(i).get(12))) ){
				RemainingInstalments = OutputXML.get(i).get(12);
			}
			if(!(OutputXML.get(i).get(13) == null || "".equals(OutputXML.get(i).get(13))) ){
				WorstStatus = OutputXML.get(i).get(13);
			}
			if(!(OutputXML.get(i).get(14) == null || "".equals(OutputXML.get(i).get(14))) ){
				WorstStatusDate = OutputXML.get(i).get(14);
			}				
			if(!(OutputXML.get(i).get(15) == null || "".equals(OutputXML.get(i).get(15))) ){
				CreditLimit = OutputXML.get(i).get(15);
			}
			if(!(OutputXML.get(i).get(16) == null || "".equals(OutputXML.get(i).get(16))) ){
				OverdueAmt = OutputXML.get(i).get(16);
			}
			if(!(OutputXML.get(i).get(17) == null || "".equals(OutputXML.get(i).get(17))) ){
				NofDaysPmtDelay = OutputXML.get(i).get(17);
			}				
			if(!(OutputXML.get(i).get(18) == null || "".equals(OutputXML.get(i).get(18))) ){
				MonthsOnBook = OutputXML.get(i).get(18);
			}
			if(!(OutputXML.get(i).get(19) == null || "".equals(OutputXML.get(i).get(19))) ){
				last_repayment_date = OutputXML.get(i).get(19);
			}
			if(!(OutputXML.get(i).get(20) == null || "".equals(OutputXML.get(i).get(20))) ){
				currently_current = OutputXML.get(i).get(20);
			}
			if(!(OutputXML.get(i).get(21) == null || "".equals(OutputXML.get(i).get(21))) ){
				current_utilization = OutputXML.get(i).get(21);
			}
			if(!(OutputXML.get(i).get(22) == null || "".equals(OutputXML.get(i).get(22))) ){
				DPD30Last6Months = OutputXML.get(i).get(22);
			}
			if(!(OutputXML.get(i).get(23) == null || "".equals(OutputXML.get(i).get(23))) ){
				DPD60Last18Months = OutputXML.get(i).get(23);
			}
			if(!(OutputXML.get(i).get(24) == null || "".equals(OutputXML.get(i).get(24))) ){
				AECBHistMonthCnt = OutputXML.get(i).get(24);
			}				


			if(!(OutputXML.get(i).get(25) == null || "".equals(OutputXML.get(i).get(25))) ){
				delinquent_in_last_3months = OutputXML.get(i).get(25);
			}
			if(!(OutputXML.get(i).get(26) == null || "".equals(OutputXML.get(i).get(26))) ){
				QC_Amt = OutputXML.get(i).get(26);
			}
			if(!(OutputXML.get(i).get(27) == null || "".equals(OutputXML.get(i).get(27))) ){
				QC_emi = OutputXML.get(i).get(27);
			}
			if(!(OutputXML.get(i).get(28) == null || "".equals(OutputXML.get(i).get(28))) ){
				CAC_Indicator = OutputXML.get(i).get(28);
				if (CAC_Indicator != null && !("".equalsIgnoreCase(CAC_Indicator))){
					if ("true".equalsIgnoreCase(CAC_Indicator)){
						CAC_Indicator="Y";
					}
					else {
						CAC_Indicator="N";
					}
				}
			}
			String TakeOverIndicator="";
			if(!(OutputXML.get(i).get(29) == null || "".equals(OutputXML.get(i).get(29))) ){
				TakeOverIndicator = OutputXML.get(i).get(29);
				if (TakeOverIndicator != null && !("".equalsIgnoreCase(TakeOverIndicator))){
					if ("true".equalsIgnoreCase(TakeOverIndicator)){
						TakeOverIndicator="Y";
					}
					else {
						TakeOverIndicator="N";
					}
				}
			}
			if(!(OutputXML.get(i).get(30) == null || "".equals(OutputXML.get(i).get(30))) ){
				consider_for_obligation = OutputXML.get(i).get(30);
				if (consider_for_obligation != null && !("".equalsIgnoreCase(consider_for_obligation))){
					if ("true".equalsIgnoreCase(consider_for_obligation)){
						consider_for_obligation="Y";
					}
					else {
						consider_for_obligation="N";
					}
				}
			}

			add_xml_str = add_xml_str + "<ExternalBureauIndividualProducts><applicant_id>"+CifId+"</applicant_id>";
			add_xml_str = add_xml_str + "<external_bureau_individual_products_id>"+AgreementId+"</external_bureau_individual_products_id>";
			add_xml_str = add_xml_str + "<contract_type>"+ContractType+"</contract_type>";
			add_xml_str = add_xml_str + "<provider_no>"+provider_no+"</provider_no>";
			add_xml_str = add_xml_str + "<phase>"+phase+"</phase>";
			add_xml_str = add_xml_str + "<role_of_customer>"+CustRoleType+"</role_of_customer>";
			add_xml_str = add_xml_str + "<start_date>"+start_date+"</start_date>"; 

			add_xml_str = add_xml_str + "<close_date>"+close_date+"</close_date>";
			add_xml_str = add_xml_str + "<outstanding_balance>"+OutStanding_Balance+"</outstanding_balance>";
			add_xml_str = add_xml_str + "<total_amount>"+TotalAmt+"</total_amount>";
			add_xml_str = add_xml_str + "<payments_amount>"+PaymentsAmt+"</payments_amount>";
			add_xml_str = add_xml_str + "<total_no_of_instalments>"+TotalNoOfInstalments+"</total_no_of_instalments>";
			add_xml_str = add_xml_str + "<no_of_remaining_instalments>"+RemainingInstalments+"</no_of_remaining_instalments>";
			add_xml_str = add_xml_str + "<worst_status>"+WorstStatus+"</worst_status>";
			add_xml_str = add_xml_str + "<worst_status_date>"+WorstStatusDate+"</worst_status_date>";

			add_xml_str = add_xml_str + "<credit_limit>"+CreditLimit+"</credit_limit>";
			add_xml_str = add_xml_str + "<overdue_amount>"+OverdueAmt+"</overdue_amount>";
			add_xml_str = add_xml_str + "<no_of_days_payment_delay>"+NofDaysPmtDelay+"</no_of_days_payment_delay>";
			add_xml_str = add_xml_str + "<mob>"+MonthsOnBook+"</mob>";
			add_xml_str = add_xml_str + "<last_repayment_date>"+last_repayment_date+"</last_repayment_date>";
			if (currently_current != null && "1".equalsIgnoreCase(currently_current))
				add_xml_str = add_xml_str + "<currently_current>Y</currently_current>";
			else
			{
				add_xml_str = add_xml_str + "<currently_current>N</currently_current>";
			}
			add_xml_str = add_xml_str + "<current_utilization>"+current_utilization+"</current_utilization>";
			add_xml_str = add_xml_str + "<dpd_30_last_6_mon>"+DPD30Last6Months+"</dpd_30_last_6_mon>";

			add_xml_str = add_xml_str + "<dpd_60p_in_last_18_mon>"+DPD60Last18Months+"</dpd_60p_in_last_18_mon>";
			add_xml_str = add_xml_str + "<no_months_aecb_history>"+AECBHistMonthCnt+"</no_months_aecb_history>";
			add_xml_str = add_xml_str + "<delinquent_in_last_3months>"+delinquent_in_last_3months+"</delinquent_in_last_3months>";
			add_xml_str = add_xml_str + "<clean_funded>"+""+"</clean_funded>";
			add_xml_str = add_xml_str + "<cac_indicator>"+CAC_Indicator+"</cac_indicator>";
			add_xml_str = add_xml_str + "<qc_emi>"+QC_emi+"</qc_emi>";
			if ("Credit Card".equalsIgnoreCase(ReqProd)){
				add_xml_str = add_xml_str + "<qc_amount>"+QC_Amt+"</qc_amount><company_flag>N</company_flag><cac_bank_name></cac_bank_name></ExternalBureauIndividualProducts>";
			}
			else{
				add_xml_str = add_xml_str + "<qc_amount>"+QC_Amt+"</qc_amount><company_flag>N</company_flag><cac_bank_name></cac_bank_name><take_over_indicator>"+TakeOverIndicator+"</take_over_indicator><consider_for_obligation>"+consider_for_obligation+"</consider_for_obligation></ExternalBureauIndividualProducts>";
			}

		}
		PersonalLoanS.mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Generate XML for ExternalBureauManualAddIndividualProducts
	 
	***********************************************************************************  */

	public String ExternalBureauManualAddIndividualProducts(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PersonalLoanS.mLogger.info("ExternalBureauManualAddIndividualProducts sQuery"+ "");
		int Man_liab_row_count = formObject.getLVWRowCount("cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
		String applicant_id = formObject.getNGValue("cmplx_Customer_CIFNO");
		String  add_xml_str ="";
		Date date = new Date();
		String modifiedDate= new SimpleDateFormat("dd/MM/yyyy").format(date);
		PersonalLoanS.mLogger.info("ExternalBureauIndividualProducts list size"+Man_liab_row_count+ "");
		if (Man_liab_row_count !=0){
			for (int i = 0; i<Man_liab_row_count;i++){
				String Type_of_Contract = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 0); //0
				String Limit = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 1); //0
				String EMI = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 2); //0
				String cac_Indicator = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 5); //0
				String Qc_amt = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 6); //0
				String Qc_Emi = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 7); //0
				if ("true".equalsIgnoreCase(cac_Indicator)){
					cac_Indicator="Y";
				}
				else {
					cac_Indicator="N";
				}
				String consider_for_obligation ="true".equalsIgnoreCase(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 8))?"Y":"N"; //0
				
				String Utilization = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 11); //0
				String OutStanding = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 12); //0
				String mob = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 10);
				String delinquent_in_last_3months = "true".equalsIgnoreCase(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 13))?"1":"0";
				String dpd_30_last_6_mon = "true".equalsIgnoreCase(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 14))?"1":"0";
				String dpd_60p_in_last_18_mon = "true".equalsIgnoreCase(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 15))?"1":"0";
				add_xml_str = add_xml_str + "<ExternalBureauIndividualProducts><applicant_id>"+applicant_id+"</applicant_id>";
				add_xml_str = add_xml_str + "<external_bureau_individual_products_id></external_bureau_individual_products_id>";
				add_xml_str = add_xml_str + "<contract_type>"+Type_of_Contract+"</contract_type>";
				add_xml_str = add_xml_str + "<provider_no></provider_no>";
				add_xml_str = add_xml_str + "<phase>A</phase>";
				add_xml_str = add_xml_str + "<role_of_customer>Main</role_of_customer>";
				add_xml_str = add_xml_str + "<start_date>"+modifiedDate+"</start_date>"; 

				add_xml_str = add_xml_str + "<close_date></close_date>";
				add_xml_str = add_xml_str + "<outstanding_balance>"+OutStanding+"</outstanding_balance>";
				add_xml_str = add_xml_str + "<total_amount>"+Limit+"</total_amount>";
				add_xml_str = add_xml_str + "<payments_amount>"+EMI+"</payments_amount>";
				add_xml_str = add_xml_str + "<total_no_of_instalments></total_no_of_instalments>";
				add_xml_str = add_xml_str + "<no_of_remaining_instalments></no_of_remaining_instalments>";
				add_xml_str = add_xml_str + "<worst_status></worst_status>";
				add_xml_str = add_xml_str + "<worst_status_date></worst_status_date>";

				add_xml_str = add_xml_str + "<credit_limit>"+Limit+"</credit_limit>";
				add_xml_str = add_xml_str + "<overdue_amount></overdue_amount>";
				add_xml_str = add_xml_str + "<no_of_days_payment_delay></no_of_days_payment_delay>";
				add_xml_str = add_xml_str + "<mob>"+mob+"</mob>";
				add_xml_str = add_xml_str + "<last_repayment_date></last_repayment_date>";

				add_xml_str = add_xml_str + "<currently_current>N</currently_current>";

				add_xml_str = add_xml_str + "<current_utilization>"+Utilization+"</current_utilization>";
				add_xml_str = add_xml_str + "<dpd_30_last_6_mon>"+dpd_30_last_6_mon+"</dpd_30_last_6_mon>";

				add_xml_str = add_xml_str + "<dpd_60p_in_last_18_mon>"+dpd_60p_in_last_18_mon+"</dpd_60p_in_last_18_mon>";
				add_xml_str = add_xml_str + "<no_months_aecb_history></no_months_aecb_history>";
				add_xml_str = add_xml_str + "<delinquent_in_last_3months>"+delinquent_in_last_3months+"</delinquent_in_last_3months>";
				add_xml_str = add_xml_str + "<clean_funded>"+""+"</clean_funded>";
				add_xml_str = add_xml_str + "<cac_indicator>"+cac_Indicator+"</cac_indicator>";
				add_xml_str = add_xml_str + "<qc_emi>"+Qc_Emi+"</qc_emi>";
				add_xml_str = add_xml_str + "<qc_amount>"+Qc_amt+"</qc_amount><company_flag>N</company_flag><consider_for_obligation>"+consider_for_obligation+"</consider_for_obligation></ExternalBureauIndividualProducts>";

			}

		}
		PersonalLoanS.mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}

	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Generate XML for getInternalLiabDetails
	 
	***********************************************************************************  */

	public String  getInternalLiabDetails(){
		PersonalLoanS.mLogger.info("RLOSCommon java file"+ "inside getCustAddress_details : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "SELECT AcctType,CustRoleType,AcctId,AccountOpenDate,AcctStat,AcctSegment,AcctSubSegment,CreditGrade FROM ng_RLOS_CUSTEXPOSE_AcctDetails  with (nolock) where Child_Wi = '"+formObject.getWFWorkitemName()+"' and Request_Type = 'InternalExposure'";
		PersonalLoanS.mLogger.info("getInternalLiabDetails sQuery"+sQuery+ "");
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		PersonalLoanS.mLogger.info("getInternalLiabDetails list size"+OutputXML.size()+ "");

		for (int i = 0; i<OutputXML.size();i++){

			String accountType = "";
			String role = "";
			String accNumber = "";
			String acctOpenDate = ""; 
			String acctStatus = "";
			String acctSegment = "";
			String acctSubSegment = "";
			String acctCreditGrade = "";
			if(!(OutputXML.get(i).get(0) == null || "".equals(OutputXML.get(i).get(0))) ){
				accountType = OutputXML.get(i).get(0);
			}
			if(!(OutputXML.get(i).get(1) == null || "".equals(OutputXML.get(i).get(1))) ){
				role = OutputXML.get(i).get(1);
			}
			if(!(OutputXML.get(i).get(2) == null || "".equals(OutputXML.get(i).get(2))) ){
				accNumber = OutputXML.get(i).get(2);
			}
			if(!(OutputXML.get(i).get(3) == null || "".equals(OutputXML.get(i).get(3))) ){
				acctOpenDate = OutputXML.get(i).get(3);
			}
			if(!(OutputXML.get(i).get(4) == null || "".equals(OutputXML.get(i).get(4))) ){
				acctStatus = OutputXML.get(i).get(4);
			}
			if(!(OutputXML.get(i).get(5) == null || "".equals(OutputXML.get(i).get(5))) ){
				acctSegment = OutputXML.get(i).get(5);
			}
			if(!(OutputXML.get(i).get(6) == null || "".equals(OutputXML.get(i).get(6))) ){
				acctSubSegment = OutputXML.get(i).get(6);
			}
			if(!(OutputXML.get(i).get(7) == null || "".equals(OutputXML.get(i).get(7)))) {
				acctCreditGrade = OutputXML.get(i).get(7);
			}
			if(accNumber!=null && !"".equalsIgnoreCase(accNumber) && !"null".equalsIgnoreCase(accNumber)){
				add_xml_str = add_xml_str + "<AccountDetails><type_of_account>"+accountType+"</type_of_account>";
				add_xml_str = add_xml_str + "<role>"+role+"</role>";
				add_xml_str = add_xml_str + "<account_number>"+accNumber+"</account_number>";
				add_xml_str = add_xml_str + "<acct_open_date>"+acctOpenDate+"</acct_open_date>";
				add_xml_str = add_xml_str + "<acct_status>"+acctStatus+"</acct_status>";
				add_xml_str = add_xml_str + "<account_segment>"+acctSegment+"</account_segment>";
				add_xml_str = add_xml_str + "<account_sub_segment>"+acctSubSegment+"</account_sub_segment>";
				add_xml_str = add_xml_str + "<credit_grate_code>"+acctCreditGrade+"</credit_grate_code>";
				add_xml_str = add_xml_str + "<cust_type>"+role+"</cust_type></AccountDetails>";
			}
		}
		PersonalLoanS.mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Generate XML for ExternalBureauPipelineProducts
	 
	***********************************************************************************  */

	public String ExternalBureauPipelineProducts(){
		PersonalLoanS.mLogger.info("RLOSCommon java file"+ "inside ExternalBureauPipelineProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "select AgreementId,ProviderNo,LoanType,LoanDesc,CustRoleType,Datelastupdated,TotalAmt,TotalNoOfInstalments,'' as col1,NoOfDaysInPipeline,isnull(Consider_For_Obligations,'true') from ng_rlos_cust_extexpo_LoanDetails where child_wi  =  '"+formObject.getWFWorkitemName()+"' and LoanStat = 'Pipeline' union select CardEmbossNum,ProviderNo,CardTypeDesc,CardType,CustRoleType,LastUpdateDate,'' as col2,NoOfInstallments,TotalAmount,NoOfDaysInPipeLine,isnull(Consider_For_Obligations,'true')  from ng_rlos_cust_extexpo_CardDetails where child_wi  =  '"+formObject.getWFWorkitemName()+"' and cardstatus = 'Pipeline'";
		PersonalLoanS.mLogger.info("ExternalBureauPipelineProducts sQuery"+sQuery+ "");
		String  add_xml_str = "";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		PersonalLoanS.mLogger.info("ExternalBureauPipelineProducts list size"+OutputXML.size()+ "");
		String cifId=formObject.getNGValue("cmplx_Customer_CIFNO");

		for (int i = 0; i<OutputXML.size();i++){

			String agreementID = "";
			String ProviderNo="";
			String contractType = "";
			String productType = "";
			String role = ""; 
			String lastUpdateDate = "";
			String TotAmt = "";
			String noOfInstalmnt = "";
			String creditLimit = "";
			String noOfDayinPpl = "";
			String consider_for_obligation="";

			if(!(OutputXML.get(i).get(0) == null || "".equals(OutputXML.get(i).get(0))) ){
				agreementID = OutputXML.get(i).get(0);
			}
			if(!(OutputXML.get(i).get(1) == null || "".equals(OutputXML.get(i).get(1))) ){
				ProviderNo = OutputXML.get(i).get(1);
			}
			if(OutputXML.get(i).get(2)!=null && !OutputXML.get(i).get(2).isEmpty() &&  !"".equals(OutputXML.get(i).get(2)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(2)) ){
				contractType = OutputXML.get(i).get(2);
			}
			if(!(OutputXML.get(i).get(3) == null || "".equals(OutputXML.get(i).get(3))) ){
				productType = OutputXML.get(i).get(3);
			}
			if(!(OutputXML.get(i).get(4) == null || "".equals(OutputXML.get(i).get(4))) ){
				role = OutputXML.get(i).get(4);
			}
			if(OutputXML.get(i).get(5)!=null && !OutputXML.get(i).get(5).isEmpty() &&  !"".equals(OutputXML.get(i).get(5)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(5)) ){
				lastUpdateDate = OutputXML.get(i).get(5);
			}
			if(OutputXML.get(i).get(6)!=null && !OutputXML.get(i).get(6).isEmpty() &&  !"".equals(OutputXML.get(i).get(6)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(6))) {
				TotAmt = OutputXML.get(i).get(6);
			}
			if(!(OutputXML.get(i).get(7) == null || "".equals(OutputXML.get(i).get(7)))) {
				noOfInstalmnt = OutputXML.get(i).get(7);
			}
			if(!(OutputXML.get(i).get(8) == null || "".equals(OutputXML.get(i).get(8))) ){
				creditLimit = OutputXML.get(i).get(8);
			}
			if(OutputXML.get(i).get(9)!=null && !OutputXML.get(i).get(9).isEmpty() &&  !"".equals(OutputXML.get(i).get(9)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(9)) ){
				noOfDayinPpl = OutputXML.get(i).get(9);
			}
			if(!(OutputXML.get(i).get(10) == null || "".equals(OutputXML.get(i).get(10))) ){
				consider_for_obligation = OutputXML.get(i).get(10);
				if (consider_for_obligation != null && !("".equalsIgnoreCase(consider_for_obligation))){
					if ("true".equalsIgnoreCase(consider_for_obligation)){
						consider_for_obligation="Y";
					}
					else {
						consider_for_obligation="N";
					}
				}
			}

			add_xml_str = add_xml_str + "<ExternalBureauPipelineProducts><applicant_ID>"+cifId+"</applicant_ID>";
			add_xml_str = add_xml_str + "<external_bureau_pipeline_products_id>"+agreementID+"</external_bureau_pipeline_products_id>";
			add_xml_str = add_xml_str + "<ppl_provider_no>"+ProviderNo+"</ppl_provider_no>";
			add_xml_str = add_xml_str + "<ppl_type_of_contract>"+contractType+"</ppl_type_of_contract>";
			add_xml_str = add_xml_str + "<ppl_type_of_product>"+productType+"</ppl_type_of_product>";
			add_xml_str = add_xml_str + "<ppl_phase>"+"PIPELINE"+"</ppl_phase>";
			add_xml_str = add_xml_str + "<ppl_role>"+role+"</ppl_role>"; 

			add_xml_str = add_xml_str + "<ppl_date_of_last_update>"+lastUpdateDate+"</ppl_date_of_last_update>";
			add_xml_str = add_xml_str + "<ppl_total_amount>"+TotAmt+"</ppl_total_amount>";
			add_xml_str = add_xml_str + "<ppl_no_of_instalments>"+noOfInstalmnt+"</ppl_no_of_instalments>";
			add_xml_str = add_xml_str + "<ppl_credit_limit>"+creditLimit+"</ppl_credit_limit>";

			add_xml_str = add_xml_str + "<ppl_no_of_days_in_pipeline>"+noOfDayinPpl+"</ppl_no_of_days_in_pipeline><company_flag>N</company_flag><consider_for_obligation>"+consider_for_obligation+"</consider_for_obligation></ExternalBureauPipelineProducts>"; // to be populated later




		}
		PersonalLoanS.mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Generate XML for OECD Details
	 
	***********************************************************************************  */

	public String getCustOECD_details(String call_name){
		PersonalLoanS.mLogger.info("RLOSCommon java file"+ "inside getCustAddress_details : ");
		String  add_xml_str ="";
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			int add_row_count = formObject.getLVWRowCount("cmplx_OECD_cmplx_GR_OecdDetails");
			PersonalLoanS.mLogger.info("RLOSCommon java file"+ "inside getCustAddress_details add_row_count+ : "+add_row_count);
			if("CUSTOMER_UPDATE_REQ".equalsIgnoreCase(call_name)){
				for (int i = 0; i<add_row_count;i++){
					String City_of_Birth = formObject.getNGValue("cmplx_OECD_cmplx_GR_OecdDetails", i, 3); //0
					String Country_of_Birth=formObject.getNGValue("cmplx_OECD_cmplx_GR_OecdDetails", i, 2);//1
					String Undocumented_Flag=formObject.getNGValue("cmplx_OECD_cmplx_GR_OecdDetails", i, 0);//2
					String UndocumentedFlag_Reason=formObject.getNGValue("cmplx_OECD_cmplx_GR_OecdDetails", i, 1);//3
					add_xml_str = add_xml_str + "<OECDDet><CityOfBirth>"+City_of_Birth+"</CityOfBirth> ";
					add_xml_str = add_xml_str + "<CountryOfBirth>"+Country_of_Birth+"</CountryOfBirth>";
					add_xml_str = add_xml_str + "<CRSUnDocFlg>"+Undocumented_Flag+"</CRSUnDocFlg>";
			
					
					add_xml_str = add_xml_str + "<CRSUndocFlgReason>"+UndocumentedFlag_Reason+"</CRSUndocFlgReason></OECDDet>";

				}

			}
			PersonalLoanS.mLogger.info("RLOSCommon"+ "OECD tag creation "+ add_xml_str);
			return add_xml_str;
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("PLCommon getCustAddress_details method"+ "Exception Occure in generate Address XMl"+e.getMessage());
			PLCommon.printException(e);
			return add_xml_str;
		}

	}
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Generate Input XML for LIEN Details
	 
	***********************************************************************************  */

	public String getLien_details(String Call_name){
		PersonalLoanS.mLogger.info("RLOSCommon java file"+ "inside getLienDetails_details : ");
		String  lien_xml_str ="";

		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			int add_row_count = formObject.getLVWRowCount("cmplx_FinacleCore_liendet_grid");
			PersonalLoanS.mLogger.info("PL Common java file"+ "inside Lien details add_row_count+ : "+add_row_count);

			if (add_row_count>=0){
				String Lien_no = formObject.getNGValue("cmplx_FinacleCore_liendet_grid", 0, 1); //0
				String Lien_amount = formObject.getNGValue("cmplx_FinacleCore_liendet_grid", 0, 2); //0
				String Lien_maturity_date = formObject.getNGValue("cmplx_FinacleCore_liendet_grid", 0, 6); //0

				if("CARD_NOTIFICATION".equalsIgnoreCase(Call_name)){
					lien_xml_str = lien_xml_str+ "<LienDetails><LienNumber>"+Lien_no+"</LienNumber>";
					lien_xml_str = lien_xml_str+ "<LienCurrency>AED</LienCurrency><Amount>"+Lien_amount+"</Amount>";
					lien_xml_str = lien_xml_str+ "<MaturityDate>"+Lien_maturity_date+"</MaturityDate>";
					lien_xml_str = lien_xml_str+ "</LienDetails>";
				}
				else if("CARD_SERVICES_REQUEST".equalsIgnoreCase(Call_name)){
					lien_xml_str = lien_xml_str+ "<LienDetails><LienNumber>"+Lien_no+"</LienNumber>";
					lien_xml_str = lien_xml_str+ "<LienCurrency>AED</LienCurrency><LienAmount>"+Lien_amount+"</LienAmount>";
					lien_xml_str = lien_xml_str+ "<LienMaturityDt>"+Lien_maturity_date+"</LienMaturityDt>";
					lien_xml_str = lien_xml_str+ "</LienDetails>";
				}
			}
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("PL Common java file"+ "Exception occured in get lien details method : "+e.getMessage());
			PLCommon.printException(e);
		}

		return lien_xml_str;

	}
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Generate Input XML for Contact Details
	 
	***********************************************************************************  */

	public String getcontact_details(){
		PersonalLoanS.mLogger.info("RLOSCommon java file"+ "inside getContact_details : ");
		String  Contactdetails_xml_str ="";

		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			Contactdetails_xml_str = "<ContactDetails><PrefModeOfContact>Phone</PrefModeOfContact><PhnDet><PhoneType>MobileNumber1</PhoneType><PhnCountryCode>971</PhnCountryCode><CityCode></CityCode><PhnLocalCode>00971</PhnLocalCode><PhoneNumber>"+formObject.getNGValue("cmplx_Customer_MobNo")+"</PhoneNumber>";
			Contactdetails_xml_str = Contactdetails_xml_str+"<PhnPrefFlag>Y</PhnPrefFlag></PhnDet></ContactDetails><ContactDetails><PrefModeOfContact>Email</PrefModeOfContact><EmailDet><MailIdType>WORKEML</MailIdType><EmailID>"+formObject.getNGValue("AlternateContactDetails_EMAIL1_PRI")+"</EmailID><MailPrefFlag>Y</MailPrefFlag></EmailDet></ContactDetails><ContactDetails><PrefModeOfContact>Phone</PrefModeOfContact><PhnDet><PhoneType>HOMEPH2</PhoneType><PhnCountryCode>00971</PhnCountryCode>";
			Contactdetails_xml_str = Contactdetails_xml_str+"<CityCode></CityCode><PhnLocalCode>00971</PhnLocalCode><PhoneNumber>"+formObject.getNGValue("AlternateContactDetails_OFFICENO")+"</PhoneNumber><PhnPrefFlag>N</PhnPrefFlag></PhnDet></ContactDetails>";

		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("PL Common java file"+ "Exception occured in get lien details method : "+e.getMessage());
			PLCommon.printException(e);
		}

		return Contactdetails_xml_str;

	}

	//added 30/08/2017
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Generate Input XML for Address Details
	 
	***********************************************************************************  */

	public String getCustAddress_details(){
		PersonalLoanS.mLogger.info("RLOSCommon java file"+ "inside getCustAddress_details : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int add_row_count = formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
		PersonalLoanS.mLogger.info("RLOSCommon java file"+ "inside getCustAddress_details add_row_count+ : "+add_row_count);
		String  add_xml_str ="";
		for (int i = 0; i<add_row_count;i++){
			String Address_type = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0); //0
			String Po_Box=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 1);//1
			String flat_Villa=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 2);//2
			String Building_name=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 3);//3
			String street_name=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 4);//4
			String Landmard = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 5);//5
			String city=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 6);//6
			String Emirates=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 7);//7
			String country=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 8);//8
			String years_in_current_add=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 9);//9
			PersonalLoanS.mLogger.info("RLOSCommon java file"+ "inside getCustAddress_details add_row_count+ : "+years_in_current_add);
			int years=Integer.parseInt(years_in_current_add);
			String preferrd;
			//added
			String EffectiveFrom;
			String EffectiveTo;
			int DateEffectiveFromYears ;
			Calendar cal = Calendar.getInstance();
			int CurrentYear=cal.get(Calendar.YEAR);
			int CurrentMonth=cal.get(Calendar.MONTH)+1;
			int CurrentDate=cal.get(Calendar.DATE);
			PersonalLoanS.mLogger.info("RLOS value of CurrentDate"+""+CurrentYear+"-"+CurrentMonth+"-"+CurrentDate);
			EffectiveTo=CurrentYear+"-"+CurrentMonth+"-"+CurrentDate;
			PersonalLoanS.mLogger.info("RLOS value of CurrentDate EffectiveTo"+""+EffectiveTo);
			DateEffectiveFromYears=CurrentYear-years;
			EffectiveFrom=DateEffectiveFromYears+"-"+CurrentMonth+"-"+CurrentDate;
			PersonalLoanS.mLogger.info("RLOS value of EffectiveFromDate"+""+EffectiveFrom);

			//ended

			if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 10)))//10
				preferrd = "Y";
			else
				preferrd = "N";



			add_xml_str = add_xml_str + "<AddressDetails><AddressType>"+Address_type+"</AddressType><AddrPrefFlag>"+preferrd+"</AddrPrefFlag><PrefFormat>STRUCTURED_FORMAT</PrefFormat>";
			//added
			add_xml_str = add_xml_str + "<EffectiveFrom>"+EffectiveFrom+"</EffectiveFrom>";
			add_xml_str = add_xml_str + "<EffectiveTo>"+EffectiveTo+"</EffectiveTo>";
			//ended
			add_xml_str = add_xml_str + "<AddrLine1>"+flat_Villa+"</AddrLine1>";
			add_xml_str = add_xml_str + "<AddrLine2>"+Building_name+"</AddrLine2>";
			add_xml_str = add_xml_str + "<AddrLine3>"+street_name+"</AddrLine3>";
			add_xml_str = add_xml_str + "<AddrLine4>"+Landmard+"</AddrLine4>";
			add_xml_str = add_xml_str + "<City>"+city+"</City>";
			add_xml_str = add_xml_str + "<CountryCode>"+country+"</CountryCode>";
			add_xml_str = add_xml_str + "<State>"+Emirates+"</State>";
			add_xml_str = add_xml_str + "<POBox>"+Po_Box+"</POBox></AddressDetails>";
		}
		PersonalLoanS.mLogger.info("RLOSCommon"+ "Address tag Cration: "+ add_xml_str);
		return add_xml_str;
	}
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Generate Input XML for MQ Socket Utility
	 
	***********************************************************************************  */

	private static String getMQInputXML(String sessionID, String cabinetName, String wi_name, String ws_name, String userName, StringBuilder final_xml) {
		FormContext.getCurrentInstance().getFormConfig();

		StringBuilder strBuff = new StringBuilder();
		strBuff.append("<APMQPUTGET_Input>");
		strBuff.append("<SessionId>" + sessionID + "</SessionId>");
		strBuff.append("<EngineName>" + cabinetName + "</EngineName>");
		strBuff.append("<XMLHISTORY_TABLENAME>NG_PL_XMLLOG_HISTORY</XMLHISTORY_TABLENAME>");
		strBuff.append("<WI_NAME>" + wi_name + "</WI_NAME>");
		strBuff.append("<WS_NAME>" + ws_name + "</WS_NAME>");
		strBuff.append("<USER_NAME>" + userName + "</USER_NAME>");
		strBuff.append("<MQ_REQUEST_XML>");
		strBuff.append(final_xml);
		strBuff.append("</MQ_REQUEST_XML>");
		strBuff.append("</APMQPUTGET_Input>");
		PersonalLoanS.mLogger.info("inside getOutputXMLValues"+ "getMQInputXML"+ strBuff.toString());
		return strBuff.toString();
	}
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Generate Input XML for DECTECH
	 
	***********************************************************************************  */

	private Map<String, String> DECTECH_Custom(List<List<String>> OutputXML,FormReference formObject,String callName) {
		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();

		try{
			for (List<String> mylist : OutputXML) {
			
				for (int i = 0; i < 8; i++) {
					
					PersonalLoanS.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}

				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");

				if("Channel".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("RLOS COMMON"+" iNSIDE channelcode+ ");

					String ReqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);

					String xml_str = int_xml.get(parent_tag);
					xml_str =  "<"+tag_name+">"+("Personal Loan".equalsIgnoreCase(ReqProd)?"PL":"CC")
							+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding channelcode+ "+xml_str);
					int_xml.put(parent_tag, xml_str);

				}

				else if("emp_type".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("RLOS COMMON"+" iNSIDE channelcode+ ");

					String empttype=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6);
					if(empttype!=null){	
						if ("Salaried".equalsIgnoreCase(empttype)){
							empttype="S";
						}
						else if ("Salaried Pensioner".equalsIgnoreCase(empttype)){
							empttype="SP";
						}
						else {
							empttype="SE";
						}
					}
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str+ "<"+tag_name+">"+empttype+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding channelcode+ "+xml_str);
					int_xml.put(parent_tag, xml_str);

				}
				else if("world_check".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("RLOS COMMON"+" iNSIDE world_check+ ");

					String world_check=formObject.getNGValue("IS_WORLD_CHECK");
					PersonalLoanS.mLogger.info("RLOS COMMON"+" iNSIDE world_check+ "+formObject.getLVWRowCount("cmplx_WorldCheck_WorldCheck_Grid"));
					if (formObject.getLVWRowCount("cmplx_WorldCheck_WorldCheck_Grid")==0){
						world_check="Negative";
					}
					else {
						world_check="Positive";
					}


					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str+ "<"+tag_name+">"+world_check+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding world_check+ "+xml_str);
					int_xml.put(parent_tag, xml_str);

				}
				else if("current_emp_catogery".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("RLOS COMMON"+" iNSIDE current_emp_catogery+ ");

					String current_emp_catogery=formObject.getNGValue("cmplx_EmploymentDetails_CurrEmployer");
					PersonalLoanS.mLogger.info("RLOS COMMON"+" value of current_emp_catogery "+current_emp_catogery);
					String squerycurremp="select Description from NG_MASTER_EmployerCategory_PL where isActive='Y' and code='"+current_emp_catogery+"'";
					PersonalLoanS.mLogger.info("RLOS COMMON"+" query is "+squerycurremp);
					List<List<String>> squerycurrempXML=formObject.getDataFromDataSource(squerycurremp);
					PersonalLoanS.mLogger.info("RLOS COMMON"+" query is "+squerycurrempXML);
					if(!squerycurrempXML.isEmpty()){
						if (squerycurrempXML.get(0).get(0)!=null){
							PersonalLoanS.mLogger.info("RLOS COMMON"+" iNSIDE squerycurrempXML+ "+squerycurrempXML.get(0).get(0));
							current_emp_catogery=squerycurrempXML.get(0).get(0);
						}
					}

					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str+ "<"+tag_name+">"+current_emp_catogery+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding current_emp_catogery+ "+xml_str);
					int_xml.put(parent_tag, xml_str);

				}
				else if("prev_loan_dbr".equalsIgnoreCase(tag_name)||"prev_loan_tai".equalsIgnoreCase(tag_name)||
						"prev_loan_multiple".equalsIgnoreCase(tag_name)||"prev_loan_employer".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("RLOS COMMON"+" iNSIDE prev_loan_dbr+ ");
					String PreviousLoanDBR="";
					String PreviousLoanEmp="";
					String PreviousLoanMultiple="";
					String PreviousLoanTAI="";

					String squeryloan="select isNull(PreviousLoanDBR,0), isNull(PreviousLoanEmp,0), isNull(PreviousLoanMultiple,0), isNull(PreviousLoanTAI,0) from ng_RLOS_CUSTEXPOSE_LoanDetails where Request_Type='CollectionsSummary' and Limit_Increase='true'  and Child_wi= '"+formObject.getWFWorkitemName()+"'";
					List<List<String>> prevLoan=formObject.getDataFromDataSource(squeryloan);
					PersonalLoanS.mLogger.info("RLOS COMMON"+" iNSIDE prev_loan_dbr+ "+squeryloan);

					if (prevLoan!=null && !prevLoan.isEmpty()){
						PreviousLoanDBR=prevLoan.get(0).get(0);
						PreviousLoanEmp=prevLoan.get(0).get(1);
						PreviousLoanMultiple=prevLoan.get(0).get(2);
						PreviousLoanTAI=prevLoan.get(0).get(3);
					}


					String xml_str = int_xml.get(parent_tag);
					if("prev_loan_dbr".equalsIgnoreCase(tag_name)){
						xml_str = xml_str+ "<"+tag_name+">"+PreviousLoanDBR+"</"+ tag_name+">";
					}
					else if("prev_loan_tai".equalsIgnoreCase(tag_name)){
						xml_str = xml_str+ "<"+tag_name+">"+PreviousLoanTAI+"</"+ tag_name+">";
					}
					else if("prev_loan_multiple".equalsIgnoreCase(tag_name)){
						xml_str = xml_str+ "<"+tag_name+">"+PreviousLoanMultiple+"</"+ tag_name+">";
					}
					else if("prev_loan_employer".equalsIgnoreCase(tag_name)){
						xml_str = xml_str+ "<"+tag_name+">"+PreviousLoanEmp+"</"+ tag_name+">";
					}



					PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding world_check+ "+xml_str);
					int_xml.put(parent_tag, xml_str);

				}

				else if("no_of_cheque_bounce_int_3mon_Ind".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("RLOS COMMON"+" iNSIDE no_of_cheque_bounce_int_3mon_Ind+ ");
					String squerynoc="SELECT count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-3,GETDATE()) and returntype='ICCS' and Child_Wi='"+formObject.getWFWorkitemName()+"'";
					List<List<String>> NOC=formObject.getDataFromDataSource(squerynoc);
					if (NOC!=null && !NOC.isEmpty()){
						String xml_str =  int_xml.get(parent_tag);
						xml_str = xml_str+ "<"+tag_name+">"+NOC.get(0).get(0)
								+"</"+ tag_name+">";

						PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding internal_blacklist+ "+xml_str);
						int_xml.put(parent_tag, xml_str);

					}

				}
				else if("no_of_DDS_return_int_3mon_Ind".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("RLOS COMMON"+" iNSIDE no_of_cheque_bounce_int_3mon_Ind+ ");
					String squerynoc="SELECT count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-3,GETDATE()) and returntype='DDS' and Child_Wi='"+formObject.getWFWorkitemName()+"'";
					List<List<String>> NOC=formObject.getDataFromDataSource(squerynoc);
					if (NOC!=null && !NOC.isEmpty()){
						String xml_str =  int_xml.get(parent_tag);
						xml_str = xml_str+ "<"+tag_name+">"+NOC.get(0).get(0)
								+"</"+ tag_name+">";

						PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding internal_blacklist+ "+xml_str);
						int_xml.put(parent_tag, xml_str);

					}

				}
				else if("blacklist_cust_type".equalsIgnoreCase(tag_name)||"internal_blacklist".equalsIgnoreCase(tag_name)||
						"internal_blacklist_date".equalsIgnoreCase(tag_name)||"internal_blacklist_code".equalsIgnoreCase(tag_name)||
						"negative_cust_type".equalsIgnoreCase(tag_name)||"internal_negative_flag".equalsIgnoreCase(tag_name)||
						"internal_negative_date".equalsIgnoreCase(tag_name)||"internal_negative_code".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("RLOS COMMON"+" iNSIDE channelcode+ ");
					String ParentWI_Name = formObject.getNGValue("Parent_WIName");
					String squeryBlacklist="select BlacklistFlag,BlacklistDate,BlacklistReasonCode,NegatedFlag,NegatedDate,NegatedReasonCode from ng_rlos_cif_detail where cif_wi_name='"+ParentWI_Name+"' and cif_searchType = 'Internal'";
					List<List<String>> Blacklist=formObject.getDataFromDataSource(squeryBlacklist);
					String internal_blacklist =  "";
					String internal_blacklist_date =  "";
					String internal_blacklist_code =  "";
					String internal_negative_flag =  "";
					String internal_negative_date =  "";
					String internal_negative_code =  "";

					if (Blacklist!=null && !Blacklist.isEmpty()){		
						internal_blacklist =  Blacklist.get(0).get(0);
						internal_blacklist_date =  Blacklist.get(0).get(1);
						internal_blacklist_code =  Blacklist.get(0).get(2);
						internal_negative_flag =  Blacklist.get(0).get(3);
						internal_negative_date =  Blacklist.get(0).get(4);
						internal_negative_code =  Blacklist.get(0).get(5);
					}
					String xml_str =  int_xml.get(parent_tag);
					if("blacklist_cust_type".equalsIgnoreCase(tag_name) || "negative_cust_type".equalsIgnoreCase(tag_name)){
						xml_str = xml_str+ "<"+tag_name+">I</"+ tag_name+">";
					}
					else if("internal_blacklist".equalsIgnoreCase(tag_name)){
						xml_str = xml_str+ "<"+tag_name+">"+internal_blacklist+"</"+ tag_name+">";
					}
					else if("internal_blacklist_date".equalsIgnoreCase(tag_name)){
						xml_str = xml_str+ "<"+tag_name+">"+internal_blacklist_date+"</"+ tag_name+">";
					}
					else if("internal_blacklist_code".equalsIgnoreCase(tag_name)){
						xml_str = xml_str+ "<"+tag_name+">"+internal_blacklist_code+"</"+ tag_name+">";
					}
					
					else if("internal_negative_flag".equalsIgnoreCase(tag_name)){
						xml_str = xml_str+ "<"+tag_name+">"+internal_negative_flag+"</"+ tag_name+">";
					}
					else if("internal_negative_date".equalsIgnoreCase(tag_name)){
						xml_str = xml_str+ "<"+tag_name+">"+internal_negative_date+"</"+ tag_name+">";
					}
					else if("internal_negative_code".equalsIgnoreCase(tag_name)){
						xml_str = xml_str+ "<"+tag_name+">"+internal_negative_code+"</"+ tag_name+">";
					}
					PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding internal_blacklist+ "+xml_str);
					int_xml.put(parent_tag, xml_str);


				}

				else if("external_blacklist_flag".equalsIgnoreCase(tag_name)||"external_blacklist_date".equalsIgnoreCase(tag_name)||
						"external_blacklist_code".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("RLOS COMMON"+" iNSIDE channelcode+ ");
					String ParentWI_Name = formObject.getNGValue("Parent_WIName");
					String squeryBlacklist="select BlacklistFlag,BlacklistDate,BlacklistReasonCode from ng_rlos_cif_detail where (cif_wi_name='"+ParentWI_Name+"' or cif_wi_name='"+formObject.getWFWorkitemName()+"') and cif_searchType = 'External'";
					PersonalLoanS.mLogger.info("RLOS COMMON"+" iNSIDE channelcode+ "+squeryBlacklist);
					List<List<String>> Blacklist=formObject.getDataFromDataSource(squeryBlacklist);
					String External_blacklist_date =  "";
					String External_blacklist_code =  "";

					if (Blacklist!=null && !Blacklist.isEmpty()){		
						External_blacklist_date =  Blacklist.get(0).get(1);
						External_blacklist_code =  Blacklist.get(0).get(2);
					}
					String xml_str =  int_xml.get(parent_tag);
					if("external_blacklist_flag".equalsIgnoreCase(tag_name)){
						xml_str = xml_str+ "<"+tag_name+">I</"+ tag_name+">";
					}
					else if("external_blacklist_date".equalsIgnoreCase(tag_name)){
						xml_str = xml_str+ "<"+tag_name+">"+External_blacklist_date+"</"+ tag_name+">";
					}
					else if("external_blacklist_code".equalsIgnoreCase(tag_name)){
						xml_str = xml_str+ "<"+tag_name+">"+External_blacklist_code+"</"+ tag_name+">";
					}

					PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding internal_blacklist+ "+xml_str);
					int_xml.put(parent_tag, xml_str);


				}
				else if("ApplicationDetails".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("inside 1st if"+"inside DECTECH req1");

					PersonalLoanS.mLogger.info("inside 1st if"+"inside customer update req2");
					String xml_str = int_xml.get(parent_tag);
					PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding product+ "+xml_str);
					xml_str = xml_str + getProduct_details();
					PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding product+ "+xml_str);
					int_xml.put(parent_tag, xml_str);

				}
				else if("cust_name".equalsIgnoreCase(tag_name) ){
					if(int_xml.containsKey(parent_tag))
					{
						String first_name=formObject.getNGValue("cmplx_Customer_FIrstNAme");
						String middle_name=formObject.getNGValue("cmplx_Customer_MiddleName");
						String last_name=formObject.getNGValue("cmplx_Customer_LAstNAme");

						String full_name=first_name+" "+middle_name+""+last_name;

						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<"+tag_name+">"+full_name
								+"</"+ tag_name+">";

						PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding confirmedinjob+ "+xml_str);
						int_xml.put(parent_tag, xml_str);

					}		                            	
				}

				else if("ref_phone_no".equalsIgnoreCase(tag_name) ){
					if(int_xml.containsKey(parent_tag))
					{
						PersonalLoanS.mLogger.info("RLOS COMMON"+" INSIDE ref_phone_no+ ");
						int count=formObject.getLVWRowCount("cmplx_RefDetails_cmplx_Gr_ReferenceDetails");
						String ref_phone_no="";
						String ref_relationship="";
						PersonalLoanS.mLogger.info("RLOS COMMON"+" INSIDE ref_phone_no+ "+count);
						if (count != 0){
							ref_phone_no=formObject.getNGValue("cmplx_RefDetails_cmplx_Gr_ReferenceDetails",0,4);
							ref_relationship=formObject.getNGValue("cmplx_RefDetails_cmplx_Gr_ReferenceDetails",0,2);
							PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding ref_phone_no+ "+ref_phone_no);
							PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding ref_relationship+ "+ref_relationship);
						}


						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<"+tag_name+">"+ref_phone_no
								+"</"+ tag_name+"><ref_relationship>"+ref_relationship+"</ref_relationship>";

						PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding confirmedinjob+ "+xml_str);
						int_xml.put(parent_tag, xml_str);

					}		                            	
				}

				else if("confirmed_in_job".equalsIgnoreCase(tag_name)){
					if(int_xml.containsKey(parent_tag))
					{
						String confirmedinjob=formObject.getNGValue("cmplx_EmploymentDetails_JobConfirmed");

						if (confirmedinjob!=null){
							if ("true".equalsIgnoreCase(confirmedinjob)){
								confirmedinjob="Y";
							}
							else{
								confirmedinjob="N";
							}

							String xml_str = int_xml.get(parent_tag);
							xml_str = xml_str + "<"+tag_name+">"+confirmedinjob
									+"</"+ tag_name+">";

							PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding confirmedinjob+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}
					}		                            	
				}
				else if("included_pl_aloc".equalsIgnoreCase(tag_name)){
					if(int_xml.containsKey(parent_tag))
					{
						String included_pl_aloc=formObject.getNGValue("cmplx_EmploymentDetails_IncInPL");

						if (included_pl_aloc!=null){
							if ("true".equalsIgnoreCase(included_pl_aloc)){
								included_pl_aloc="Y";
							}
							else{
								included_pl_aloc="N";
							}

							String xml_str = int_xml.get(parent_tag);
							xml_str = xml_str + "<"+tag_name+">"+included_pl_aloc
									+"</"+ tag_name+">";

							PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding included_pl_aloc+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}
					}
				}
				else if("included_cc_aloc".equalsIgnoreCase(tag_name)){
					if(int_xml.containsKey(parent_tag))
					{
						String included_cc_aloc=formObject.getNGValue("cmplx_EmploymentDetails_IncInCC");

						if (included_cc_aloc!=null){
							if ("true".equalsIgnoreCase(included_cc_aloc)){
								included_cc_aloc="Y";
							}
							else{
								included_cc_aloc="N";
							}

							String xml_str = int_xml.get(parent_tag);
							xml_str = xml_str + "<"+tag_name+">"+included_cc_aloc
									+"</"+ tag_name+">";

							PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding cmplx_EmploymentDetails_IncInCC+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}	
					}
				}
				else if("vip_flag".equalsIgnoreCase(tag_name)){
					if(int_xml.containsKey(parent_tag))
					{
						String vip_flag=formObject.getNGValue("cmplx_Customer_VIPFlag");


						if ("true".equalsIgnoreCase(vip_flag)){
							vip_flag="Y";
						}
						else{
							vip_flag="N";
						}

						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<"+tag_name+">"+vip_flag
								+"</"+ tag_name+">";

						PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding cmplx_Customer_VIPFlag+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}		                            	
				}
				else if("standing_instruction".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("RLOS COMMON"+" iNSIDE standing_instruction+ ");
					String squerynoc="SELECT count(*) FROM ng_rlos_FinancialSummary_SiDtls WHERE Child_wi='"+formObject.getWFWorkitemName()+"'";
					List<List<String>> NOC=formObject.getDataFromDataSource(squerynoc);
					PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding cmplx_Customer_VIPFlag+ "+squerynoc);
					String standing_instruction;
					standing_instruction=NOC.get(0).get(0);
					if (NOC!=null && !NOC.isEmpty()){
						String xml_str =  int_xml.get(parent_tag);
						if ("0".equalsIgnoreCase(standing_instruction)){
							standing_instruction="N";
						}
						else{
							standing_instruction="Y";
						}

						xml_str = xml_str+ "<"+tag_name+">"+standing_instruction
								+"</"+ tag_name+">";

						PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding standing_instruction+ "+xml_str);
						int_xml.put(parent_tag, xml_str);

					}

				}
				else if("accomodation_provided".equalsIgnoreCase(tag_name)){
					if(int_xml.containsKey(parent_tag))
					{
						String accomodation_provided=formObject.getNGValue("cmplx_IncomeDetails_Accomodation");

						if (accomodation_provided!=null){
							if ("Yes".equalsIgnoreCase(accomodation_provided)){
								accomodation_provided="Y";
							}
							else{
								accomodation_provided="N";
							}

							String xml_str = int_xml.get(parent_tag);
							xml_str = xml_str + "<"+tag_name+">"+accomodation_provided
									+"</"+ tag_name+">";

							PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding confirmedinjob+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}	
					}
				}
				else if("AccountDetails".equalsIgnoreCase(tag_name)){
					if(int_xml.containsKey(parent_tag))
					{
						String xml_str = int_xml.get(parent_tag);
						PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding internal liability+ "+xml_str);
						xml_str = xml_str + getInternalLiabDetails();
						PersonalLoanS.mLogger.info("RLOS COMMON"+" after internal liability+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}

				}
				else if("InternalBureau".equalsIgnoreCase(tag_name) ){

					String xml_str = int_xml.get(parent_tag);
					PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding InternalBureauData+ "+xml_str);
					String temp = InternalBureauData();
					if(!"".equalsIgnoreCase(temp)){
						if (xml_str==null){
							PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding bhrabc"+xml_str);
							xml_str="";
						}
						xml_str =  xml_str+ temp;
						PersonalLoanS.mLogger.info("RLOS COMMON"+" after InternalBureauData+ "+xml_str);
						int_xml.get(parent_tag);
						int_xml.put(parent_tag, xml_str);
					}


				}
				else if("InternalBouncedCheques".equalsIgnoreCase(tag_name) ){

					String xml_str = int_xml.get(parent_tag);
					PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding InternalBouncedCheques+ "+xml_str);
					String temp = InternalBouncedCheques();
					if(!"".equalsIgnoreCase(temp)){
						if (xml_str==null){
							PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding bhrabc"+xml_str);
							xml_str="";
						}
						xml_str =  xml_str+ temp;
						PersonalLoanS.mLogger.info("RLOS COMMON"+" after InternalBouncedCheques+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}

				}
				else if("InternalBureauIndividualProducts".equalsIgnoreCase(tag_name)){

					String xml_str = int_xml.get(parent_tag);
					PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding InternalBureauIndividualProducts+ "+xml_str);
					String temp = InternalBureauIndividualProducts();
					if(!"".equalsIgnoreCase(temp)){
						if (xml_str==null){
							PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding bhrabc"+xml_str);
							xml_str="";
						}
						xml_str =  xml_str+ temp;
						PersonalLoanS.mLogger.info("RLOS COMMON"+" after InternalBureauIndividualProducts+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}

				}
				else if("InternalBureauPipelineProducts".equalsIgnoreCase(tag_name)){

					String xml_str = int_xml.get(parent_tag);
					PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding InternalBureauPipelineProducts+ "+xml_str);
					String temp = InternalBureauPipelineProducts();
					if(!"".equalsIgnoreCase(temp)){
						if (xml_str==null){
							PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding bhrabc"+xml_str);
							xml_str="";
						}
						xml_str =  xml_str+ temp;
						PersonalLoanS.mLogger.info("RLOS COMMON"+" after InternalBureauPipelineProducts+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}

				}
				else if("ExternalBureau".equalsIgnoreCase(tag_name) ){

					String xml_str = int_xml.get(parent_tag);
					PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding ExternalBureau+ "+xml_str);
					String temp = ExternalBureauData();
					if(!"".equalsIgnoreCase(temp)){
						if (xml_str==null){
							PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding bhrabc"+xml_str);
							xml_str="";
						}
						xml_str =  xml_str+ temp;
						PersonalLoanS.mLogger.info("RLOS COMMON"+" after ExternalBureau+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}

				}
				else if("ExternalBouncedCheques".equalsIgnoreCase(tag_name)){

					String xml_str = int_xml.get(parent_tag);
					PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding ExternalBouncedCheques+ "+xml_str);
					String temp = ExternalBouncedCheques();
					if(!"".equalsIgnoreCase(temp)){
						if (xml_str==null){
							PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding bhrabc"+xml_str);
							xml_str="";
						}
						xml_str =  xml_str+ temp;
						PersonalLoanS.mLogger.info("RLOS COMMON"+" after ExternalBouncedCheques+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}                    	
				}
				else if("ExternalBureauIndividualProducts".equalsIgnoreCase(tag_name)){

					String xml_str = int_xml.get(parent_tag);
					PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding ExternalBureauIndividualProducts+ "+xml_str);
					String temp =  ExternalBureauIndividualProducts();
					PersonalLoanS.mLogger.info("RLOS COMMON"+" value of temp to be adding temp+ "+temp);
					String Manual_add_Liab =  ExternalBureauManualAddIndividualProducts();

					if((!"".equalsIgnoreCase(temp)) || (!"".equalsIgnoreCase(Manual_add_Liab))){
						if (xml_str==null){
							PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding bhrabc"+xml_str);
							xml_str="";
						}
						xml_str =  xml_str + temp + Manual_add_Liab;
						PersonalLoanS.mLogger.info("RLOS COMMON"+" after ExternalBureauIndividualProducts+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}	                            	
				}
				else if("ExternalBureauPipelineProducts".equalsIgnoreCase(tag_name)){

					String xml_str = int_xml.get(parent_tag);
					PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding ExternalBureauPipelineProducts+ "+xml_str);
					String temp =  ExternalBureauPipelineProducts();
					if(!"".equalsIgnoreCase(temp)){
						if (xml_str==null){
							PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding bhrabc"+xml_str);
							xml_str="";
						}
						xml_str =  xml_str+ temp;
						PersonalLoanS.mLogger.info("RLOS COMMON"+" after ExternalBureauPipelineProducts+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}                        	
				}
				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
				}

			}
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + ");
			PLCommon.printException(e);
		}

		return int_xml;
	}
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Generate Input XML for NEW LOAN REQUEST
	 
	***********************************************************************************  */

	private Map<String, String> NEW_LOAN_REQ_Custom( List<List<String>> OutputXML,FormReference formObject,String callName) {
		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();

		try{
			for (List<String> mylist : OutputXML) {
				for (int i = 0; i < 8; i++) {
					PersonalLoanS.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}

				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");
				if("ApplicationID".equalsIgnoreCase(tag_name) ){
					PersonalLoanS.mLogger.info("inside 1st if"+"inside customer update req1");
					String xml_str = int_xml.get(parent_tag);
					xml_str = "<"+tag_name+">"+formObject.getWFWorkitemName().substring(5,14)+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("PL COMMON"+" after adding ApplicationID:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}
				else if("SourcingDate".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("inside 1st if"+"inside customer update req1");
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<SourcingDate>2015-04-15</SourcingDate>";

					PersonalLoanS.mLogger.info("PL COMMON"+" SourcingDate: "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}
				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
				}
			}
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + ");
			PLCommon.printException(e);
		}

		return int_xml;
	}
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Generate Input XML for CARD NOTIFICATION
	 
	***********************************************************************************  */

	private Map<String, String> CARD_NOTIFICATION_Custom( List<List<String>> OutputXML,FormReference formObject,String callName) {
		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();

		try{
			for (List<String> mylist : OutputXML) {
				for (int i = 0; i < 8; i++) {
					PersonalLoanS.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}

				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");
				if("ApplicationNumber".equalsIgnoreCase(tag_name) ){
					PersonalLoanS.mLogger.info("inside 1st if"+"inside customer update req1");
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str+"<"+tag_name+">"+formObject.getWFWorkitemName().substring(5,14)+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("PL COMMON"+" after adding ApplicationNumber:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}
				else if("ApplicationDate".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("inside 1st if"+"inside customer update req1");
					String xml_str = int_xml.get(parent_tag);
					xml_str = "<"+tag_name+">"+"2017-06-06"+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("PL COMMON"+" after adding ApplicationDate:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}
				else if("VIPFlag".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("inside 1st if"+"inside customer update req1");

					String vip_flag="N";
					if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_VIPFlag"))){
						vip_flag="Y";
					}	          	

					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<"+tag_name+">"+vip_flag+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding VIP flag+ "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}
				else if("PhnDet".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("inside 1st if"+"inside customer update req1");
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<PhnDet><PhoneType>OFFCPH1</PhoneType><PhnCountryCode>00971</PhnCountryCode><CityCode></CityCode><PhnLocalCode>00971</PhnLocalCode><PhoneNumber>"+formObject.getNGValue("cmplx_Customer_MobNo")+"</PhoneNumber><ExtensionNumber></ExtensionNumber><PhnPrefFlag>N</PhnPrefFlag></PhnDet>";

					PersonalLoanS.mLogger.info("PL COMMON"+" SourcingDate: "+xml_str);
					int_xml.put(parent_tag, xml_str);                                    
				}
				
				else if("DSAId".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("inside 1st if"+"inside customer update req1");
					String xml_str = int_xml.get(parent_tag);
					xml_str =xml_str+ "<"+tag_name+">"+"99D1243"+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("PL COMMON"+" after adding DSAId:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}
				else if("AddrDet".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("inside 1st if"+"inside customer update req1");
					if(int_xml.containsKey(parent_tag))
					{
						PersonalLoanS.mLogger.info("inside 1st if"+"inside customer update req2");
						String xml_str = int_xml.get(parent_tag);
						PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding address+ "+xml_str);
						xml_str = xml_str + getCustAddress_details(callName);
						PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding address+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}		                            	
				}
				else if("LienDetails".equalsIgnoreCase(tag_name) ){
					PersonalLoanS.mLogger.info("inside 1st if"+"inside customer update req1");
					if(int_xml.containsKey(parent_tag))
					{
						PersonalLoanS.mLogger.info("inside 1st if"+"inside customer update req2");
						String xml_str = int_xml.get(parent_tag);
						PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding address+ "+xml_str);
						xml_str = xml_str + getLien_details(callName);
						PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding address+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}		                            	
				}
				else if("ContactDetails".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("inside 1st if"+"inside customer update req1");
					if(int_xml.containsKey(parent_tag))
					{
						PersonalLoanS.mLogger.info("inside 1st if"+"inside customer update req2");
						String xml_str = int_xml.get(parent_tag);
						PersonalLoanS.mLogger.info("RLOS COMMON"+" before contact details+ "+xml_str);
						xml_str = xml_str + getcontact_details();
						PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding contact details+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}		                            	
				}
				else if("SalaryDate".equalsIgnoreCase(tag_name) ){
					PersonalLoanS.mLogger.info("inside 1st if"+"inside customer update req1");
					String xml_str = int_xml.get(parent_tag);
					Calendar now = Calendar.getInstance();
					String month;
					String day;
					if((now.get(Calendar.MONTH) - 1)<10){
						month = "0"+Integer.toString(now.get(Calendar.MONTH) - 1);
					}else{
						month =Integer.toString(now.get(Calendar.MONTH) - 1);
					}
					if(formObject.getNGValue("cmplx_IncomeDetails_SalaryDay").length()<2){
						day = "0" + formObject.getNGValue("cmplx_IncomeDetails_SalaryDay");
					}else{
						day = formObject.getNGValue("cmplx_IncomeDetails_SalaryDay");
					}


					String Current_date;

					Current_date = now.get(Calendar.YEAR)+"-"+month+"-"+day;
					xml_str = xml_str+"<"+tag_name+">"+Current_date+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("PL COMMON"+" after adding ApplicationID:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}
				else if("MonthlyCrTrnOvrAmt".equalsIgnoreCase(tag_name)){
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<"+tag_name+">"+"1000"
							+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else if("CardNumber".equalsIgnoreCase(tag_name)){
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<"+tag_name+">"+"5239266299926203"
							+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else if("CardProductType".equalsIgnoreCase(tag_name)){
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<"+tag_name+">"+"KALYAN-SEC"
							+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else if("DispatchMode".equalsIgnoreCase(tag_name)){
					String xml_str = int_xml.get(parent_tag);
					xml_str = "<"+tag_name+">"+"ByCourier"
							+"</"+ tag_name+">";	          	
					PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else if(("MinorFlag".equalsIgnoreCase(tag_name) && "PersonDetails".equalsIgnoreCase(parent_tag))|| "CARD_NOTIFICATION".equalsIgnoreCase(callName)){
					if(int_xml.containsKey(parent_tag))
					{
						int Age = Integer.parseInt(formObject.getNGValue("cmplx_Customer_age"));
						String age_flag = "N";
						if(Age<18)
							age_flag="Y";
						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<"+tag_name+">"+age_flag+"</"+ tag_name+">";

						PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}		                            	
				}
				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
				}
			}
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + ");
			PLCommon.printException(e);
		}

		return int_xml;
	}
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Generate Input XML for CARD SERVICES REQUEST
	 
	***********************************************************************************  */
	private Map<String, String> CARD_SERVICES_Custom( List<List<String>> OutputXML,FormReference formObject,String callName) {
		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();

		try{
			for (List<String> mylist : OutputXML) {
				for (int i = 0; i < 8; i++) {
					PersonalLoanS.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}

				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");
				if("LienDetails".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("inside 1st if"+"inside customer update req1");
					if(int_xml.containsKey(parent_tag))
					{
						PersonalLoanS.mLogger.info("inside 1st if"+"inside customer update req2");
						String xml_str = int_xml.get(parent_tag);
						PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding address+ "+xml_str);
						xml_str = xml_str + getLien_details(callName);
						PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding address+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}		                            	
				}
				else if("SalaryDate".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("inside 1st if"+"inside customer update req1");
					String xml_str = int_xml.get(parent_tag);
					Calendar now = Calendar.getInstance();
					String month = "";
					String day = "";
					if((now.get(Calendar.MONTH) - 1)<10){
						month = "0"+(now.get(Calendar.MONTH) - 1);
					}else{
						month = ""+Integer.toString((now.get(Calendar.MONTH) - 1));
					}
					if(formObject.getNGValue("cmplx_IncomeDetails_SalaryDay").length()<2){
						day = "0" + formObject.getNGValue("cmplx_IncomeDetails_SalaryDay");
					}else{
						day = formObject.getNGValue("cmplx_IncomeDetails_SalaryDay");
					}


					String Current_date="";

					Current_date = now.get(Calendar.YEAR)+"-"+month+"-"+day;
					xml_str = xml_str+"<"+tag_name+">"+Current_date+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("PL COMMON"+" after adding ApplicationID:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}
				else if("ProcessDate".equalsIgnoreCase(tag_name)){
					String xml_str = int_xml.get(parent_tag);
					SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
					xml_str = xml_str + "<"+tag_name+">"+sdf1.format(new Date())+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else if("ProcessedBy".equalsIgnoreCase(tag_name)){
					String xml_str = int_xml.get(parent_tag);

					xml_str = xml_str + "<"+tag_name+">"+formObject.getUserName()+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
				}
			}
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + ");
			PLCommon.printException(e);
		}

		return int_xml;
	}
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Generate Input XML for CHEQUE_BOOK_ELIGIBILITY
	 
	***********************************************************************************  */
	private Map<String, String> CHEQUE_BOOK_ELIGIBILITY_Custom( List<List<String>> OutputXML,FormReference formObject,String callName) {
		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();

		try{
			for (List<String> mylist : OutputXML) {
				for (int i = 0; i < 8; i++) {
					PersonalLoanS.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}

				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");
				if ("RecipientAddress".equalsIgnoreCase(tag_name)) {
					int add_len = formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
					String add_res_val = "";
					String xml_str = int_xml.get(parent_tag);
					if (add_len > 0) {
						for (int i = 0; i < add_len; i++) {
							PersonalLoanS.mLogger.info("selecting Emirates of residence: "+formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid"+i+ 0));
							if ("Home".equalsIgnoreCase(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0)))
							{
							formObject.setNGValue("cmplx_Customer_EmirateOfResidence",formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i, 6));
							add_res_val = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 1)+ " "+formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i, 2)+ formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i, 3)+ formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i, 4)+ formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i, 5)+ formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i, 6);
							}
						}
						xml_str = xml_str + "<" + tag_name + ">" + add_res_val + "</"+ tag_name + ">";

						PersonalLoanS.mLogger.info("CC Integration "+ " after adding res_flag+ "+ xml_str);
						int_xml.put(parent_tag, xml_str);
					}
				}
				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
				}
			}
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + ");
			PLCommon.printException(e);
		}

		return int_xml;
	}
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Generate Input XML for DB INPUT
	 
	***********************************************************************************  */
	private Map<String, String> GenDefault_Input_DB(Map<String, String> int_xml, Map<String, String> recordFileMap,FormReference formObject,String callName){

		String Call_name =  recordFileMap.get("Call_name");
		String form_control =  recordFileMap.get("form_control");
		String parent_tag =  recordFileMap.get("parent_tag_name");
		String tag_name =  recordFileMap.get("xmltag_name");
		String is_repetitive =  recordFileMap.get("is_repetitive");
		String default_val =  recordFileMap.get("default_val");
		String data_format12 =  recordFileMap.get("data_format");

		String form_control_val = "";
		java.util.Date startDate;

		if (parent_tag != null && !"".equalsIgnoreCase(parent_tag)) {

			PersonalLoanS.mLogger.info("inside 1st if"+"inside 1st if");
			if(int_xml.containsKey(parent_tag))
			{
				PersonalLoanS.mLogger.info("inside 1st if"+"inside 2nd if");
				String xml_str = int_xml.get(parent_tag);
				PersonalLoanS.mLogger.info("inside 1st if"+"inside 2nd if xml string"+xml_str);
				if("Y".equalsIgnoreCase(is_repetitive) && int_xml.containsKey(tag_name)){
					PersonalLoanS.mLogger.info("inside 1st if"+"inside 3rd if xml string");
					xml_str = int_xml.get(tag_name)+ "</"+tag_name+">" +"<"+ tag_name+">";
					PersonalLoanS.mLogger.info("inside 1st if"+"inside 3rd if xml string xml string"+xml_str);
					int_xml.remove(tag_name);
					int_xml.put(tag_name, xml_str);
					PersonalLoanS.mLogger.info("inside 1st if"+"inside 3rd if xml string xml string int_xml");
				}
				else{
					PersonalLoanS.mLogger.info("inside else of parent tag"+"value after adding "+ Call_name+": "+xml_str+" form_control name: "+form_control);
					PersonalLoanS.mLogger.info(""+"valuie of form control: "+formObject.getNGValue(form_control));
					if("".equalsIgnoreCase(form_control.trim()) && "".equalsIgnoreCase(default_val.trim())){
						PersonalLoanS.mLogger.info("inside if added by me"+"inside");
						xml_str = xml_str + "<"+tag_name+">"+"</"+ tag_name+">";
						PersonalLoanS.mLogger.info("added by xml"+"xml_str"+xml_str);
					}
					else if (!(formObject.getNGValue(form_control)==null || "".equalsIgnoreCase(formObject.getNGValue(form_control).trim())||  "null".equalsIgnoreCase(formObject.getNGValue(form_control))))
					{
						PersonalLoanS.mLogger.info("inside else of parent tag 1"+"form_control_val"+ form_control_val);
						if(fin_call_name.toUpperCase().contains(callName.toUpperCase())){
							form_control_val = formObject.getNGValue(form_control).toUpperCase();
						}
						else
							form_control_val = formObject.getNGValue(form_control);

						if(!"text".equalsIgnoreCase(data_format12)){
							String[] format_arr = data_format12.split(":");
							String format_name = format_arr[0];
							String format_type = format_arr[1];
							PersonalLoanS.mLogger.info(""+"format_name"+format_name);
							PersonalLoanS.mLogger.info(""+"format_type"+format_type);

							if("date".equalsIgnoreCase(format_name)){
								DateFormat df = new SimpleDateFormat("dd/MM/yyyy"); 
								DateFormat df_new = new SimpleDateFormat(format_type);

								try {
									startDate = df.parse(form_control_val);
									form_control_val = df_new.format(startDate);
									PersonalLoanS.mLogger.info("RLOSCommon#Create Input"+" date conversion: final Output: "+form_control_val+ " requested format: "+format_type);

								} catch (ParseException e) {
									PersonalLoanS.mLogger.info("RLOSCommon#Create Input"+" Error while format conversion: "+e.getMessage());
									PLCommon.printException(e);
								}
								catch (Exception e) {
									PersonalLoanS.mLogger.info("RLOSCommon#Create Input"+" Error while format conversion: "+e.getMessage());
									PLCommon.printException(e);
								}
							}
							else if("number".equalsIgnoreCase(format_name)){
								if(form_control_val.contains(",")){
									form_control_val = form_control_val.replace(",", "");
								}

							}
							//change here for other input format

						}
						PersonalLoanS.mLogger.info("inside else of parent tag"+"form_control_val"+ form_control_val);
						xml_str = xml_str + "<"+tag_name+">"+form_control_val
								+"</"+ tag_name+">";
						PersonalLoanS.mLogger.info("inside else of parent tag xml_str"+"xml_str"+ xml_str);
					}

					else if(default_val==null || "".equalsIgnoreCase(default_val.trim())){
						PersonalLoanS.mLogger.info("#RLOS Common GenerateXML IF part"+"no value found for tag name: "+ tag_name);
					}
					else{
						PersonalLoanS.mLogger.info("#RLOS Common GenerateXML inside set default value"+"");

						form_control_val = default_val;

						PersonalLoanS.mLogger.info("#RLOS Common GenerateXML inside set default value"+"form_control_val"+ form_control_val);
						xml_str = xml_str + "<"+tag_name+">"+form_control_val
								+"</"+ tag_name+">";
						PersonalLoanS.mLogger.info("#RLOS Common GenerateXML inside else of parent tag form_control_val xml_str1"+"xml_str"+ xml_str);

					}
					//code change for to remove docdect incase ref no is not present start	                                       
					if("DocumentRefNumber".equalsIgnoreCase(tag_name) && "Document".equalsIgnoreCase(parent_tag) && "".equalsIgnoreCase(form_control_val.trim())){
						if(xml_str.contains("</Document>")){
							xml_str = xml_str.substring(0, xml_str.lastIndexOf("</Document>"));
							int_xml.put(parent_tag, xml_str);
						}
						else
							int_xml.remove(parent_tag);
					}
					else if("DocRefNum".equalsIgnoreCase(tag_name) && "DocDetails".equalsIgnoreCase(parent_tag) && "".equalsIgnoreCase(form_control_val)){
						if(xml_str.contains("</DocDetails>")){
							xml_str = xml_str.substring(0, xml_str.lastIndexOf("</DocDetails>"));
							int_xml.put(parent_tag, xml_str);
						}
						else
							int_xml.remove(parent_tag);
					}
					else if("DocNo".equalsIgnoreCase(tag_name) && "DocDet".equalsIgnoreCase(parent_tag) && "".equalsIgnoreCase(form_control_val)){
						if(xml_str.contains("</DocDet>")){
							xml_str = xml_str.substring(0, xml_str.lastIndexOf("</DocDet>"));
							int_xml.put(parent_tag, xml_str);
						}
						else
							int_xml.remove(parent_tag);
					}
					

					else if("PhnLocalCode".equalsIgnoreCase(tag_name) && "PhnDetails".equalsIgnoreCase(parent_tag) && "".equalsIgnoreCase(form_control_val)){
						if(xml_str.contains("</PhnDetails>")){
							PersonalLoanS.mLogger.info("PL_common"+ "Inside PhnDetails condition to remove RVC tag </PhnDetails> contanied");
							xml_str = xml_str.substring(0, xml_str.lastIndexOf("</PhnDetails>"));
							int_xml.put(parent_tag, xml_str);
						}
						else{
							PersonalLoanS.mLogger.info("PL_common"+ "Inside PhnDetails condition to remove PhnDetails tag <PhnDetails> tag not contanied");
							int_xml.remove(parent_tag);
							Iterator<Map.Entry<String,String>> itr = int_xml.entrySet().iterator();
							PersonalLoanS.mLogger.info("itr of hashmap"+"itr"+itr);
							while (itr.hasNext())
							{
								Map.Entry<String, String> entry =  itr.next();
								PersonalLoanS.mLogger.info("entry of hashmap"+"entry"+entry+ " entry.getValue(): "+ entry.getValue());
								if(entry.getValue().contains("PhnDetails")){
									String key_val = entry.getValue();
									key_val = key_val.replace("<PhnDetails>", "");
									key_val = key_val.replace("</PhnDetails>", "");
									int_xml.put(entry.getKey(), key_val);
									PersonalLoanS.mLogger.info("PL_common"+"KEY: entry.getKey()" + " Updated value: "+key_val);
								
									PersonalLoanS.mLogger.info("PL_common "+"PhnDetails removed from parent key");
									break;
								}

							} 
						}
					}
					//for email for CIF update

					else if("Email".equalsIgnoreCase(tag_name) && "EmlDet".equalsIgnoreCase(parent_tag) && "".equalsIgnoreCase(form_control_val)){
						if(xml_str.contains("</EmlDet>")){
							PersonalLoanS.mLogger.info("PL_common"+ "Inside EmlDet condition to remove RVC tag </EmlDet> contanied");
							xml_str = xml_str.substring(0, xml_str.lastIndexOf("</EmlDet>"));
							int_xml.put(parent_tag, xml_str);
						}
						else{
							PersonalLoanS.mLogger.info("PL_common"+ "Inside EmlDet condition to remove PhnDetails tag <EmlDet> tag not contanied");
							int_xml.remove(parent_tag);
							Iterator<Map.Entry<String,String>> itr = int_xml.entrySet().iterator();
							PersonalLoanS.mLogger.info("itr of hashmap"+"itr"+itr);
							while (itr.hasNext())
							{
								Map.Entry<String, String> entry =  itr.next();
								PersonalLoanS.mLogger.info("entry of hashmap"+"entry"+entry+ " entry.getValue(): "+ entry.getValue());
								if(entry.getValue().contains("EmlDet")){
									String key_val = entry.getValue();
									key_val = key_val.replace("<EmlDet>", "");
									key_val = key_val.replace("</EmlDet>", "");
									int_xml.put(entry.getKey(), key_val);
									PersonalLoanS.mLogger.info("PL_common"+"KEY: entry.getKey()" + " Updated value: "+key_val);
								
									PersonalLoanS.mLogger.info("PL_common "+"EmlDet removed from parent key");
									break;
								}

							} 
						}
					}

					
					else if("IncomeAmount".equalsIgnoreCase(tag_name) && "OtherIncomeDetails".equalsIgnoreCase(parent_tag) && "".equalsIgnoreCase(form_control_val)){
						if(xml_str.contains("</OtherIncomeDetails>")){
							xml_str = xml_str.substring(0, xml_str.lastIndexOf("</OtherIncomeDetails>"));
							int_xml.put(parent_tag, xml_str);
						}
						else
							int_xml.remove(parent_tag);
					}
					else{
						int_xml.put(parent_tag, xml_str);
					}
					//code change for to remove docdect incase ref no is not present end
				
					PersonalLoanS.mLogger.info("else of generatexml"+"RLOSCommon"+"inside else"+xml_str);

				}

			}
			else{
				String new_xml_str ="";
				PersonalLoanS.mLogger.info("inside else of parent tag main 2"+"value after adding "+ Call_name+": "+new_xml_str+" form_control name: "+form_control);
				PersonalLoanS.mLogger.info(""+"valuie of form control: "+formObject.getNGValue(form_control));
				if (!(formObject.getNGValue(form_control)==null || "".equalsIgnoreCase(formObject.getNGValue(form_control).trim())||  "null".equalsIgnoreCase(formObject.getNGValue(form_control)))){
					PersonalLoanS.mLogger.info("inside else of parent tag 1"+"form_control_val"+ form_control_val);
					if(fin_call_name.toUpperCase().contains(callName.toUpperCase())){
						form_control_val = formObject.getNGValue(form_control).toUpperCase();
					}
					else
						form_control_val = formObject.getNGValue(form_control);
					if(!"text".equalsIgnoreCase(data_format12)){
						String[] format_arr = data_format12.split(":");
						String format_name = format_arr[0];
						String format_type = format_arr[1];
						if("date".equalsIgnoreCase(format_name)){
							DateFormat df = new SimpleDateFormat("MM/dd/yyyy"); 
							DateFormat df_new = new SimpleDateFormat(format_type);
						
							try {
								startDate = df.parse(form_control_val);
								form_control_val = df_new.format(startDate);
								PersonalLoanS.mLogger.info("RLOSCommon#Create Input"+" date conversion: final Output: "+form_control_val+ " requested format: "+format_type);

							} catch (ParseException e) {
								PersonalLoanS.mLogger.info("RLOSCommon#Create Input"+" Error while format conversion: "+e.getMessage());
								PLCommon.printException(e);
							}
							catch (Exception e) {
								PersonalLoanS.mLogger.info("RLOSCommon#Create Input"+" Error while format conversion: "+e.getMessage());
								PLCommon.printException(e);
							}
						}
						else if("number".equalsIgnoreCase(format_name)){
							if(form_control_val.contains(",")){
								form_control_val = form_control_val.replace(",", "");
							}

						}
						//change here for other input format

					}
					PersonalLoanS.mLogger.info("inside else of parent tag"+"form_control_val"+ form_control_val);
					new_xml_str = new_xml_str + "<"+tag_name+">"+form_control_val
							+"</"+ tag_name+">";
					PersonalLoanS.mLogger.info("inside else of parent tag xml_str"+"new_xml_str"+ new_xml_str);
				}

				else if(default_val==null || "".equalsIgnoreCase(default_val.trim())){
					if(int_xml.containsKey(parent_tag)|| "Y".equalsIgnoreCase(is_repetitive)){
						new_xml_str = new_xml_str + "<"+tag_name+">"+"</"+ tag_name+">";
					}
					PersonalLoanS.mLogger.info("#RLOS Common GenerateXML Inside Else Part"+"no value found for tag name: "+ tag_name);
				}
				else{
					PersonalLoanS.mLogger.info("#RLOS Common GenerateXML inside set default value"+"");
					form_control_val = default_val;
					PersonalLoanS.mLogger.info("#RLOS Common GenerateXML inside set default value"+"form_control_val"+ form_control_val);
					new_xml_str = new_xml_str + "<"+tag_name+">"+form_control_val
							+"</"+ tag_name+">";
					PersonalLoanS.mLogger.info("#RLOS Common GenerateXML inside else of parent tag form_control_val xml_str1"+"xml_str"+ new_xml_str);

				}
				int_xml.put(parent_tag, new_xml_str);
				PersonalLoanS.mLogger.info("else of generatexml"+"RLOSCommon"+"inside else"+new_xml_str);

			}

		}
		return int_xml;
	}
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Generate  XML for MQ CONNECTION RESPONSE
	 
	***********************************************************************************  */
	public String MQ_connection_response(StringBuilder finalXml) {

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
		int socketServerPort;
		try {


			cabinetName = FormContext.getCurrentInstance().getFormConfig().getConfigElement("EngineName");
			PersonalLoanS.mLogger.info("$$outputgGridtXML "+"cabinetName " + cabinetName);
			wi_name = FormContext.getCurrentInstance().getFormConfig().getConfigElement("ProcessInstanceId");
			ws_name = FormContext.getCurrentInstance().getFormConfig().getConfigElement("ActivityName");
			PersonalLoanS.mLogger.info("$$outputgGridtXML "+"ActivityName " + ws_name);
			sessionID = FormContext.getCurrentInstance().getFormConfig().getConfigElement("DMSSessionId");
			userName = FormContext.getCurrentInstance().getFormConfig().getConfigElement("UserName");
			PersonalLoanS.mLogger.info("$$outputgGridtXML "+ "userName "+ userName);
			PersonalLoanS.mLogger.info("$$outputgGridtXML "+ "sessionID "+ sessionID);

			String sMQuery = "SELECT SocketServerIP,SocketServerPort FROM NG_RLOS_MQ_TABLE with (nolock)";
			List<List<String>> outputMQXML = formObject.getDataFromDataSource(sMQuery);
			PersonalLoanS.mLogger.info("$$outputgGridtXML "+ "sMQuery " + sMQuery);
			if (!outputMQXML.isEmpty()) {
				PersonalLoanS.mLogger.info("$$outputgGridtXML "+ outputMQXML.get(0).get(0) + "+" + outputMQXML.get(0).get(1));
				socketServerIP = outputMQXML.get(0).get(0);
				PersonalLoanS.mLogger.info("$$outputgGridtXML "+ "socketServerIP " + socketServerIP);
				socketServerPort = Integer.parseInt(outputMQXML.get(0).get(1));
				PersonalLoanS.mLogger.info("$$outputgGridtXML "+ "socketServerPort " + socketServerPort);
				try{
				if (!("".equalsIgnoreCase(socketServerIP)  && socketServerPort==0)) {
					socket = new Socket(socketServerIP, socketServerPort);
					out = socket.getOutputStream();
					socketInputStream = socket.getInputStream();
					dout = new DataOutputStream(out);
					din = new DataInputStream(socketInputStream);
					mqOutputResponse = "";
					mqInputRequest = getMQInputXML(sessionID, cabinetName,wi_name, ws_name, userName, finalXml);
					PersonalLoanS.mLogger.info("$$outputgGridtXML "+"mqInputRequest " + mqInputRequest);

					if (mqInputRequest != null && mqInputRequest.length() > 0) {
						int outPut_len = mqInputRequest.getBytes("UTF-16LE").length;
					PersonalLoanS.mLogger.info("Final XML output len: "+outPut_len + "");
					mqInputRequest = outPut_len + "##8##;" + mqInputRequest;
					PersonalLoanS.mLogger.info("MqInputRequest"+"Input Request Bytes : "+ mqInputRequest.getBytes("UTF-16LE"));
					dout.write(mqInputRequest.getBytes("UTF-16LE"));
					dout.flush();
					}
					byte[] readBuffer = new byte[50000];
					int num = din.read(readBuffer);
					boolean wait_flag = true;
					int out_len = 0;

					if (num > 0) {
						while (wait_flag) {	
						PersonalLoanS.mLogger.info("MqOutputRequest"+ "num :"+ num);
						byte[] arrayBytes = new byte[num];
						System.arraycopy(readBuffer, 0, arrayBytes, 0, num);
						mqOutputResponse = mqOutputResponse+ new String(arrayBytes, "UTF-16LE");
						PersonalLoanS.mLogger.info("MqOutputRequest"+"inside loop output Response :\n"+ mqOutputResponse);
						if (mqOutputResponse.contains("##8##;")) {
							String[] mqOutputResponse_arr = mqOutputResponse.split("##8##;");
							mqOutputResponse = mqOutputResponse_arr[1];
							out_len = Integer.parseInt(mqOutputResponse_arr[0]);
							PersonalLoanS.mLogger.info("MqOutputRequest"+"First Output Response :\n"+ mqOutputResponse);
							PersonalLoanS.mLogger.info("MqOutputRequest"+"Output length :\n" + out_len);
						}	
						if (out_len <= mqOutputResponse.getBytes("UTF-16LE").length) {	
							wait_flag = false;
						}
						Thread.sleep(100);	
						num = din.read(readBuffer);
					}// Aman Code added for dectech to replace the &lt and// &gt start 13 sept 2017if (mqOutputResponse.contains("&lt;")) {	SKLogger_CC.writeLog("MqOutputRequest","inside for Dectech :\n"+ mqOutputResponse);	mqOutputResponse = mqOutputResponse.replaceAll("&lt;", "<");	SKLogger_CC.writeLog("MqOutputRequest","after replacing lt :\n"+ mqOutputResponse);	mqOutputResponse = mqOutputResponse.replaceAll("&gt;", ">");	SKLogger_CC.writeLog("MqOutputRequest","after replacing gt :\n"+ mqOutputResponse);}// Aman Code added for dectech to replace the &lt and// &gt end 13 sept 2017
					
						if(mqOutputResponse.contains("&lt;")){
							
							mqOutputResponse=mqOutputResponse.replaceAll("&lt;", "<");
							
							mqOutputResponse=mqOutputResponse.replaceAll("&gt;", ">");
							
						}
						PersonalLoanS.mLogger.info("MqOutputRequest"+"Final Output Response :\n" + mqOutputResponse);
					socket.close();return mqOutputResponse;
					}

				} else {
					PersonalLoanS.mLogger.info("SocketServerIp and SocketServerPort is not maintained ");
					PersonalLoanS.mLogger.info("SocketServerIp is not maintained "+	socketServerIP);
					PersonalLoanS.mLogger.info(" SocketServerPort is not maintained "+	socketServerPort);
					return "MQ details not maintained";
				}
				}
				finally{
					if(socket!=null)
					socket.close();
					}
			} else {
				PersonalLoanS.mLogger.info("SOcket details are not maintained in NG_RLOS_MQ_TABLE table"+"");
				return "MQ details not maintained";
			}
			return "";

		} catch (Exception e) {
			return "";
		}
	}
	public  String Clean_Xml(String InputXml){
		String Output_Xml="";
		PL_Integration_Output ReadXml = new PL_Integration_Output();
		try{
			Document doc = ReadXml.getDocument(InputXml);
			removeEmptyNodes(doc);
			DOMSource domSource = new DOMSource(doc);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
			Output_Xml = writer.toString().substring(38);
		}
		catch(Exception e){
			//empty catch
		}
		return Output_Xml;
	}
	public  void removeEmptyNodes(Node node) {
		NodeList list = node.getChildNodes();
		List<Node> nodesToRecursivelyCall = new LinkedList<Node>();
		for (int i = 0; i < list.getLength(); i++) {
			nodesToRecursivelyCall.add(list.item(i));
		}
		for(Node tempNode : nodesToRecursivelyCall) {
			removeEmptyNodes(tempNode);
		}
		boolean emptyElement = node.getNodeType() == Node.ELEMENT_NODE && node.getChildNodes().getLength() == 0;
		boolean emptyText = node.getNodeType() == Node.TEXT_NODE && node.getNodeValue().trim().isEmpty();
		boolean selectText = node.getNodeType() == Node.TEXT_NODE && (node.getNodeValue().trim().equalsIgnoreCase("--Select--")||node.getNodeValue().trim().equalsIgnoreCase("null"));
		if (emptyElement || emptyText || selectText) {
			if(!node.hasAttributes()) {
				node.getParentNode().removeChild(node);
			}
		}
	}

}
