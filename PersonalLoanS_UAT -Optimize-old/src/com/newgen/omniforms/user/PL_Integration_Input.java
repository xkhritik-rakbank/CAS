
package com.newgen.omniforms.user;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;

public class PL_Integration_Input implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	static Logger mLogger=PersonalLoanS.mLogger;
	private String col_n = "call_type,Call_name,form_control,parent_tag_name,xmltag_name,is_repetitive,default_val,data_format";
	private String fin_call_name = "Customer_details, Customer_eligibility,new_customer_req,new_account_req,DEDUP_SUMMARY";
	public String GenerateXML(String callName,String Operation_name)
	{
		mLogger.info("RLOSCommon"+ "Inside GenerateXML():");

		StringBuilder final_xml= new StringBuilder("");
		String header ="";
		String footer = "";
		String parentTagName="";
		String sQuery=null;
		mLogger.info("$$outputgGridtXML "+"before try");
		try
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String sQuery_header = "SELECT Header,Footer,parenttagname FROM NG_Integration with (nolock) where Call_name='"+callName+"'";
			mLogger.info("RLOSCommon"+ "sQuery"+sQuery_header);
			List<List<String>> DB_header=formObject.getNGDataFromDataCache(sQuery_header);
			if(!DB_header.isEmpty()){
				mLogger.info("RLOSCommon header: "+DB_header.get(0).get(0)+" footer: "+DB_header.get(0).get(1)+" parenttagname: "+DB_header.get(0).get(2));
				header = DB_header.get(0).get(0);
				footer = DB_header.get(0).get(1);
				parentTagName = DB_header.get(0).get(2);

				// String col_n = "call_type,Call_name,form_control,parent_tag_name,xmltag_name,is_repetitive,default_val";
				// String sQuery = "SELECT call_type, Call_name,form_control,parent_tag_name,xmltag_name,is_repetitive FROM NG_Integration_PL_field_Mapping where Call_name='"+callName+"'ORDER BY tag_seq ASC" ;
				if(!(Operation_name.equalsIgnoreCase("") || Operation_name.equalsIgnoreCase(null))){   
					mLogger.info("inside if of operation"+"operation111"+Operation_name);
					mLogger.info("inside if of operation"+"callName111"+callName);
					sQuery = "SELECT "+col_n +" FROM NG_Integration_PL_field_Mapping with (nolock) where Call_name='"+callName+"' and active = 'Y' and Operation_name='"+Operation_name+"' ORDER BY tag_seq ASC" ;
					mLogger.info("RLOSCommon"+ "sQuery "+sQuery);
				}
				else{
					mLogger.info("inside else of operation"+"operation"+Operation_name);
					sQuery = "SELECT "+col_n +" FROM NG_Integration_PL_field_Mapping with (nolock) where Call_name='"+callName+"' and active = 'Y' ORDER BY tag_seq ASC" ;
					mLogger.info("RLOSCommon"+ "sQuery "+sQuery);
				}

				//List<List<String>> OutputXML=formObject.getNGDataFromDataCache(sQuery);
				List<List<String>> DB_List=formObject.getDataFromDataSource(sQuery);//chnage to get data from DB directly
				mLogger.info("OutputXML"+"OutputXML"+DB_List);
				if(!DB_List.isEmpty()){
					//SKLogger.writeLog("$$AKSHAY",OutputXML.get(0).get(0)+OutputXML.get(0).get(1)+OutputXML.get(0).get(2)+OutputXML.get(0).get(3));
					mLogger.info("GenerateXML Integration field mapping table"+DB_List.get(0).get(0)+DB_List.get(0).get(1)+DB_List.get(0).get(2)+DB_List.get(0).get(3)+DB_List.get(0).get(4));
					mLogger.info("GenerateXML Integration field mapping table"+DB_List.get(0).get(0)+DB_List.get(0).get(1)+DB_List.get(0).get(2)+DB_List.get(0).get(3));
					
						mLogger.info(""+"column length"+col_n.length());
						Map<String, String> int_xml = new LinkedHashMap<String, String>();

						if (callName.equalsIgnoreCase("DEDUP_SUMMARY")) {
							int_xml = DEDUP_SUMMARY_Custom(DB_List,formObject,callName);
						} else if (callName.equalsIgnoreCase("BLACKLIST_DETAILS")) {
							int_xml = Blacklist_Details_custom(DB_List,formObject,callName);
						} else if (callName.equalsIgnoreCase("NEW_CUSTOMER_REQ")) {
							int_xml = NEW_CUSTOMER_Custom(DB_List,formObject,callName);
						} else if (callName.equalsIgnoreCase("CUSTOMER_UPDATE_REQ")) {
							int_xml = CUSTOMER_UPDATE_Custom(DB_List,formObject,callName);
						} else if (callName.equalsIgnoreCase("NEW_CARD_REQ")) {
							int_xml = NEW_CARD_Custom(DB_List,formObject,callName);
						} else if (callName.equalsIgnoreCase("DECTECH")) {
							int_xml = DECTECH_Custom(DB_List,formObject,callName);
						} else if (callName.equalsIgnoreCase("CHEQUE_BOOK_ELIGIBILITY")) {
							int_xml = CHEQUE_BOOK_ELIGIBILITY_Custom(DB_List,formObject,callName);
						} else if (callName.equalsIgnoreCase("NEW_LOAN_REQ")) {
							int_xml = NEW_LOAN_REQ_Custom(DB_List,formObject,callName);
						}
						else if (callName.equalsIgnoreCase("CARD_NOTIFICATION")) {
							int_xml = CARD_NOTIFICATION_Custom(DB_List,formObject,callName);
						}
						else if (callName.equalsIgnoreCase("CARD_SERVICES_REQUEST")) {
							int_xml = CARD_SERVICES_Custom(DB_List,formObject,callName);
						}

						final_xml=final_xml.append("<").append(parentTagName).append(">");
						mLogger.info("RLOS"+"Final XMLold--"+final_xml);

						Iterator<Map.Entry<String,String>> itr = int_xml.entrySet().iterator();
						mLogger.info("itr of hashmap"+"itr"+itr);
						while (itr.hasNext())
						{
							Map.Entry<String, String> entry =  itr.next();
							mLogger.info("entry of hashmap"+"entry"+entry);
							if(final_xml.indexOf(entry.getKey())>-1){
								mLogger.info("RLOS"+"itr_value: Key: "+ entry.getKey()+" Value: "+entry.getValue());
								final_xml = final_xml.insert(final_xml.indexOf("<"+entry.getKey()+">")+entry.getKey().length()+2, entry.getValue());
								mLogger.info("value of final xml"+"final_xml"+final_xml);
								itr.remove();
							}

						}    
						final_xml=final_xml.append("</").append(parentTagName).append(">");
						mLogger.info("FInal XMLnew is: "+ final_xml);
						final_xml.insert(0, header);
						final_xml.append(footer);
						mLogger.info("FInal XMLnew with header: "+ final_xml);
						formObject.setNGValue("Is_"+callName,"Y");
						mLogger.info("value of "+callName+" Flag: "+formObject.getNGValue("Is_"+callName));

						return MQ_connection_response(final_xml);
					}

				}
				else {
					mLogger.info("Genrate XML: "+"Entry is not maintained in field mapping Master table for : "+callName);
					return "Call not maintained";
				}
		}

		catch(Exception e){
			mLogger.info("Exception ocurred: "+e.getLocalizedMessage());
			PLCommon.printException(e);
			return "0";
		}    
		return "";

	}

	private Map<String, String> DEDUP_SUMMARY_Custom(List<List<String>> DB_List,FormReference formObject,String callName) {


		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();

		//FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try{
			for (List<String> mylist : DB_List) {
				// for(int i=0;i<col_n.length();i++)
				for (int i = 0; i < 8; i++) {
					// SKLogger_CC.writeLog("rec: "+records.item(rec));
					mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");

				if (tag_name.equalsIgnoreCase("AddressDetails") && int_xml.containsKey(parent_tag)) {
					String xml_str = int_xml.get(parent_tag);
					mLogger.info("RLOS COMMON"+ " before adding address+ " + xml_str);
					xml_str = xml_str + getCustAddress_details(callName);
					mLogger.info("RLOS COMMON"+ " after adding address+ " + xml_str);
					int_xml.put(parent_tag, xml_str);
				} 
				else if(tag_name.equalsIgnoreCase("MaritalStatus")){
					String marrital_code = formObject.getNGValue("cmplx_Customer_MAritalStatus").substring(0, 1);
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<"+tag_name+">"+marrital_code
							+"</"+ tag_name+">";

					mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
				}
			}
		}
		catch(Exception e){
			mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + ");
			PLCommon.printException(e);
		}

		return int_xml;
	}

	private Map<String, String> Blacklist_Details_custom(List<List<String>> DB_List,FormReference formObject, String Call_name) {

		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();

		//FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try{
			for (List<String> mylist : DB_List) {
				// for(int i=0;i<col_n.length();i++)
				for (int i = 0; i < 8; i++) {
					// SKLogger_CC.writeLog("rec: "+records.item(rec));
					mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");

				if (tag_name.equalsIgnoreCase("AddressDetails")
						&& int_xml.containsKey(parent_tag)) {
					String xml_str = int_xml.get(parent_tag);
					mLogger.info("RLOS COMMON"+ " before adding address+ " + xml_str);
					xml_str = xml_str + getCustAddress_details(Call_name);
					mLogger.info("RLOS COMMON"+ " after adding address+ " + xml_str);
					int_xml.put(parent_tag, xml_str);
				} else if(tag_name.equalsIgnoreCase("MaritalStatus")){
					String marrital_code = formObject.getNGValue("cmplx_Customer_MAritalStatus").substring(0, 1);
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<"+tag_name+">"+marrital_code
							+"</"+ tag_name+">";

					mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,Call_name);
				}
			}
		}
		catch(Exception e){
			mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + ");
			PLCommon.printException(e);
		}

		return int_xml;
	}

	private Map<String, String> NEW_CUSTOMER_Custom(List<List<String>> DB_List,FormReference formObject, String Call_name) {

		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();
		try{
			for (List<String> mylist : DB_List) {
				// for(int i=0;i<col_n.length();i++)
				for (int i = 0; i < 8; i++) {
					// SKLogger_CC.writeLog("rec: "+records.item(rec));
					mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");

				if (tag_name.equalsIgnoreCase("AddressDetails")	&& int_xml.containsKey(parent_tag)) {
					String xml_str = int_xml.get(parent_tag);
					mLogger.info("RLOS COMMON"+ " before adding address+ " + xml_str);
					xml_str = xml_str + getCustAddress_details(Call_name);
					mLogger.info("RLOS COMMON"+ " after adding address+ " + xml_str);
					int_xml.put(parent_tag, xml_str);
				} else if (tag_name.equalsIgnoreCase("MinorFlag") && parent_tag.equalsIgnoreCase("PersonDetails")) {
					if (int_xml.containsKey(parent_tag)) {
						float Age = Float.parseFloat(formObject.getNGValue("cmplx_Customer_age"));
						String age_flag = "N";
						if (Age < 18)
							age_flag = "Y";
						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<" + tag_name + ">" + age_flag + "</" + tag_name + ">";

						mLogger.info("RLOS COMMON"+" after adding Minor flag+ " + xml_str);
						int_xml.put(parent_tag, xml_str);
					}
				} else if (tag_name.equalsIgnoreCase("NonResidentFlag") && parent_tag.equalsIgnoreCase("PersonDetails")) {
					if (int_xml.containsKey(parent_tag)) {
						String xml_str = int_xml.get(parent_tag);
						String res_flag = "N";

						if (formObject.getNGValue("cmplx_Customer_ResidentNonResident").equalsIgnoreCase("Resident")) {
							res_flag = "Y";
						}

						xml_str = xml_str + "<" + tag_name + ">" + res_flag + "</" + tag_name + ">";

						mLogger.info("RLOS COMMON"+ " after adding res_flag+ " + xml_str);
						int_xml.put(parent_tag, xml_str);
					}
				}
				else if(tag_name.equalsIgnoreCase("MinorFlag")){
					if(int_xml.containsKey(parent_tag))
					{
						int Age = Integer.parseInt(formObject.getNGValue("cmplx_Customer_age"));
						String age_flag = "N";
						if(Age<18)
							age_flag="Y";
						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<"+tag_name+">"+age_flag
								+"</"+ tag_name+">";

						mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}		                            	
				}
				
				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,Call_name);
				}
			}
		}
		catch(Exception e){
			mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + ");
			PLCommon.printException(e);
		}
		return int_xml;
	}

	private Map<String, String> CUSTOMER_UPDATE_Custom(List<List<String>> DB_List,FormReference formObject, String Call_name) {
		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();
		try{
			for (List<String> mylist : DB_List) {
				// for(int i=0;i<col_n.length();i++)
				for (int i = 0; i < 8; i++) {
					// SKLogger_CC.writeLog("rec: "+records.item(rec));
					mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				String form_control =  recordFileMap.get("form_control");
				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");
				if(tag_name.equalsIgnoreCase("OECDDet") && Call_name.equalsIgnoreCase("CUSTOMER_UPDATE_REQ")){
					mLogger.info("PL Common"+"inside OECDDet inside customer update req1");
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + getCustOECD_details(Call_name);
					mLogger.info("PL COMMON"+" after adding OECDDet: "+xml_str);
					int_xml.put(parent_tag, xml_str);                                    
				} else if (tag_name.equalsIgnoreCase("PhnLocalCode")) {
					mLogger.info("inside PL Common generate xml"+
							"PhnLocalCode to substring");
					String xml_str = int_xml.get(parent_tag);
					String phn_no = formObject.getNGValue(form_control);
					if ((!phn_no.equalsIgnoreCase("")) && phn_no.indexOf("00971") > -1) {
						phn_no = phn_no.substring(5);
					}

					xml_str = xml_str + "<" + tag_name + ">" + phn_no + "</" + tag_name + ">";

					mLogger.info("PL COMMON"+ " after adding ApplicationID:  " + xml_str);
					int_xml.put(parent_tag, xml_str);
				} else if(tag_name.equalsIgnoreCase("AddrDet")){
					mLogger.info("inside 1st if"+"inside customer update req1");
					if(int_xml.containsKey(parent_tag))
					{
						mLogger.info("inside 1st if"+"inside customer update req2");
						String xml_str = int_xml.get(parent_tag);
						mLogger.info("RLOS COMMON"+" before adding address+ "+xml_str);
						xml_str = xml_str + getCustAddress_details(Call_name);
						mLogger.info("RLOS COMMON"+" after adding address+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}		                            	
				}
				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,Call_name);
				}
			}
		}
		catch(Exception e){
			mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + ");
			PLCommon.printException(e);
		}
		return int_xml;

	}

	private Map<String, String> NEW_CARD_Custom(List<List<String>> DB_List,FormReference formObject, String Call_name) {

		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();
		try{
			for (List<String> mylist : DB_List) {
				// for(int i=0;i<col_n.length();i++)
				for (int i = 0; i < 8; i++) {
					// SKLogger_CC.writeLog("rec: "+records.item(rec));
					mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");
				if (tag_name.equalsIgnoreCase("VIPFlg")) {
					String vip_flag = "N";
					if (formObject.getNGValue("cmplx_Customer_VIPFlag")
							.equalsIgnoreCase("true")) {
						vip_flag = "Y";
					}
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<" + tag_name + ">" + vip_flag + "</" + tag_name + ">";

					mLogger.info("RLOS COMMON"+ " after adding VIP flag+ " + xml_str);
					int_xml.put(parent_tag, xml_str);
				} else if (tag_name.equalsIgnoreCase("ProcessingUserId")) {
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<" + tag_name + ">" + formObject.getUserName() + "</" + tag_name + ">";

					mLogger.info("RLOS COMMON"+ " after adding Minor flag+ " + xml_str);
					int_xml.put(parent_tag, xml_str);
				} else if (tag_name.equalsIgnoreCase("ProcessingDate")) {
					String xml_str = int_xml.get(parent_tag);
					SimpleDateFormat sdf1 = new SimpleDateFormat(
							"yyyy-MM-dd'T'HH:mm:ss.mmm+hh:mm");
					xml_str = xml_str + "<" + tag_name + ">" + sdf1.format(new Date()) + "</" + tag_name + ">";

					mLogger.info("RLOS COMMON"+ " after adding Minor flag+ " + xml_str);
					int_xml.put(parent_tag, xml_str);
				}
			
				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,Call_name);
				}
			}
		}
		catch(Exception e){
			mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + ");
			PLCommon.printException(e);
		}
		return int_xml;
	}



	public String getCustAddress_details(String call_name){
		mLogger.info("RLOSCommon java file"+ "inside getCustAddress_details : ");
		String  add_xml_str ="";
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			int add_row_count = formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
			mLogger.info("RLOSCommon java file"+ "inside getCustAddress_details add_row_count+ : "+add_row_count);

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
				mLogger.info("PLCommon java file"+ "ADD Type: "+Address_type);
				if (Address_type.equalsIgnoreCase("HOME")){
					Address_type="Home Country";
				}
				mLogger.info("PLCommon java file"+ "ADD Type after: "+Address_type);
				//added here
				mLogger.info("RLOSCommon java file"+ "inside getCustAddress_details add_row_count+ : "+years_in_current_add);
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
				mLogger.info("RLOS value of CurrentDate EffectiveTo"+""+EffectiveTo);
				mLogger.info("RLOS value of EffectiveFromDate"+""+EffectiveFrom);
				//Code change to added Effective from and to End
				mLogger.info("RLOS value of prefered Add: Address Type: "+Address_type+" Address pref flag: "+formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid"+ i+ 11));
				if(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 11).equalsIgnoreCase("true"))//10
					preferrd = "Y";
				else
					preferrd = "N";


				if(call_name.equalsIgnoreCase("CUSTOMER_UPDATE_REQ")){
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
					//add_xml_str = add_xml_str + "<POBox>"+Po_Box+"</POBox></AddrDet>";
				}
				else if(call_name.equalsIgnoreCase("CARD_NOTIFICATION")){
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
					//add_xml_str = add_xml_str + "<POBox>"+Po_Box+"</POBox></AddrDet>";
				}
				else if (call_name.equalsIgnoreCase("NEW_CUSTOMER_REQ")){
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
				else if(call_name.equalsIgnoreCase("NEW_CREDITCARD_REQ")){
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
			mLogger.info("RLOSCommon"+ "Address tag Cration: "+ add_xml_str);
			return add_xml_str;
		}
		catch(Exception e){
			mLogger.info("PLCommon getCustAddress_details method"+ "Exception Occure in generate Address XMl"+e.getMessage());
			PLCommon.printException(e);
			return add_xml_str;
		}

	}	

	public String getProduct_details(){
		mLogger.info("RLOSCommon java file"+ "inside getProduct_details : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int prod_row_count = formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		mLogger.info("RLOSCommon java file"+ "inside getCustAddress_details add_row_count+ : "+prod_row_count);
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
			mLogger.info("PL_SKLogger java file"+ "inside getProduct_details add_row_count+ : "+prod_row_count);

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
			mLogger.info("PL_SKLogger java file"+ "inside ApplicationDate ApplicationDate+ : "+ApplicationDate);

			// limitExpiry=Convert_dateFormat(limitExpiry, "dd/MM/yyyy", "yyyy-MM-dd");
			String EMI;
			EMI=formObject.getNGValue("cmplx_EligibilityAndProductInfo_EMI");
			if(EMI == null){
				EMI=""; 
			}

			if (priority.equalsIgnoreCase("Primary")){
				prod_xml_str = prod_xml_str + "<ApplicationDetails><product_type>"+(prod_type.equalsIgnoreCase("Conventional")?"CON":"ISL")+"</product_type>";
				prod_xml_str = prod_xml_str + "<app_category>"+formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg")+"</app_category>";
				prod_xml_str = prod_xml_str + "<requested_product>"+(reqProd.equalsIgnoreCase("Personal Loan")?"PL":"CC")+"</requested_product>";
				prod_xml_str = prod_xml_str + "<requested_limit>"+reqLimit+"</requested_limit>";
				prod_xml_str = prod_xml_str + "<sub_product>"+subProd+"</sub_product>";
				prod_xml_str = prod_xml_str + "<requested_card_product>"+cardProd+"</requested_card_product>";
				prod_xml_str = prod_xml_str + "<application_type>"+appType+"</application_type>";
				prod_xml_str = prod_xml_str + "<scheme>"+scheme+"</scheme>";
				prod_xml_str = prod_xml_str + "<tenure>"+tenure+"</tenure>";
				prod_xml_str = prod_xml_str + "<customer_type>"+(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true")?"NTB":"Existing")+"</customer_type>";
				if(Manual_Dev!=null){ 
					if(Manual_Dev.equalsIgnoreCase("true")){
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
		mLogger.info("RLOSCommon"+ "Address tag Cration: "+ prod_xml_str);
		return prod_xml_str;
	}


	public String InternalBureauData(){
		mLogger.info("RLOSCommon java file"+ "inside InternalBureauData : ");
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
			mLogger.info("InternalBureauData list size"+OutputXML.size()+ "");
			//SKLogger.writeLog("InternalBureauData list "+OutputXML, "");
			mLogger.info("obefor list "+ "values");
			float TotOutstandingAmt;
			float TotOverdueAmt;

			mLogger.info("outsidefor list "+ "values");
			TotOutstandingAmt=0.0f;
			TotOverdueAmt=0.0f;

			mLogger.info("outsidefor2 list "+ "values");
			for(int i = 0; i<OutputXML.size();i++){
				mLogger.info("insidefor list "+i+ "values"+OutputXML.get(i).get(1));
				if(OutputXML.get(i).get(0)!=null && !OutputXML.get(i).get(1).isEmpty() &&  !OutputXML.get(i).get(1).equals("") && !OutputXML.get(i).get(1).equalsIgnoreCase("null") ){
					mLogger.info("Totaloutstanding"+i+ "values."+TotOutstandingAmt+"..");
					TotOutstandingAmt = TotOutstandingAmt + Float.parseFloat(OutputXML.get(i).get(0));
				}
				if(OutputXML.get(i).get(1)!=null && !OutputXML.get(i).get(1).isEmpty() && !OutputXML.get(i).get(2).equals("") && !OutputXML.get(i).get(2).equalsIgnoreCase("null") ){
					mLogger.info("TotOverdueAmt"+i+ "values."+TotOutstandingAmt+"..");
					TotOverdueAmt = TotOverdueAmt + Float.parseFloat(OutputXML.get(i).get(1));
				}

			}
			add_xml_str = add_xml_str + "<total_out_bal>"+TotOutstandingAmt+"</total_out_bal>";
			add_xml_str = add_xml_str + "<total_overdue>"+TotOverdueAmt+"</total_overdue>";
			String sQueryDerived = "select NoOfContracts,Total_Exposure,WorstCurrentPaymentDelay,Worst_PaymentDelay_Last24Months,Nof_Records from NG_RLOS_CUSTEXPOSE_Derived where Request_Type='CollectionsSummary' and child_wi='"+formObject.getWFWorkitemName()+"'" ;
			List<List<String>> OutputXMLDerived = formObject.getDataFromDataSource(sQueryDerived);
			if(OutputXMLDerived!=null && !OutputXMLDerived.isEmpty() && OutputXMLDerived.get(0)!=null){
				if(!(OutputXMLDerived.get(0).get(0)==null || OutputXMLDerived.get(0).get(0).equals("")) ){
					NoOfContracts= OutputXMLDerived.get(0).get(0);
				}
				if(!(OutputXMLDerived.get(0).get(1)==null || OutputXMLDerived.get(0).get(1).equals("")) ){
					Total_Exposure= OutputXMLDerived.get(0).get(1);
				}
				if(!(OutputXMLDerived.get(0).get(2)==null || OutputXMLDerived.get(0).get(2).equals("")) ){
					WorstCurrentPaymentDelay= OutputXMLDerived.get(0).get(2);
				}
				if(!(OutputXMLDerived.get(0).get(3)==null || OutputXMLDerived.get(0).get(3).equals("")) ){
					Worst_PaymentDelay_Last24Months= OutputXMLDerived.get(0).get(3);
				}
				if(!(OutputXMLDerived.get(0).get(4)==null || OutputXMLDerived.get(0).get(4).equals("")) ){
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



			mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "+ add_xml_str);
			return add_xml_str;
		}
		catch(Exception e)
		{
			new PersonalLoanSCommonCode();
			mLogger.info("RLOSCommon"+ "Exception occurred in InternalBureauData()"+e.getMessage());
			PLCommon.printException(e);
			return "";
		}

	}

	public String InternalBouncedCheques(){
		mLogger.info("RLOSCommon java file"+ "inside InternalBouncedCheques : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "select CIFID,AcctId,returntype,returnNumber,returnAmount,retReasonCode,returnDate from ng_rlos_FinancialSummary_ReturnsDtls with (nolock) where Child_Wi = '"+formObject.getWFWorkitemName()+"'";
		mLogger.info("InternalBouncedCheques sQuery"+sQuery+ "");
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		mLogger.info("InternalBouncedCheques list size"+OutputXML.size()+ "");

		for (int i = 0; i<OutputXML.size();i++){

			String applicantID = "";
			String chequeNo = "";
			String internal_bounced_cheques_id = "";
			String bouncedCheq="";
			String amount = "";
			String reason = ""; 
			String returnDate = "";


			if(!(OutputXML.get(i).get(0) == null || OutputXML.get(i).get(0).equals("")) ){
				applicantID = OutputXML.get(i).get(0);
			}
			if(!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals("")) ){
				internal_bounced_cheques_id = OutputXML.get(i).get(1);
			}
			if(!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2).equals("")) ){
				bouncedCheq = OutputXML.get(i).get(2);
			}
			if(!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals("")) ){
				chequeNo = OutputXML.get(i).get(3);
			}
			if(!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals("")) ){
				amount = OutputXML.get(i).get(4);
			}
			if(!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5).equals("")) ){
				reason = OutputXML.get(i).get(5);
			}
			if(!(OutputXML.get(i).get(6) == null || OutputXML.get(i).get(6).equals("")) ){
				returnDate = OutputXML.get(i).get(6);
			}


			if(applicantID!=null && !applicantID.equalsIgnoreCase("") && !applicantID.equalsIgnoreCase("null")){
				add_xml_str = add_xml_str + "<InternalBouncedCheques><applicant_id>"+applicantID+"</applicant_id>";
				add_xml_str = add_xml_str + "<internal_bounced_cheques_id>"+internal_bounced_cheques_id+"</internal_bounced_cheques_id>";
				if (bouncedCheq.equalsIgnoreCase("ICCS")){
					add_xml_str = add_xml_str + "<bounced_cheque>"+"1"+"</bounced_cheque>";
				}
				add_xml_str = add_xml_str + "<cheque_no>"+chequeNo+"</cheque_no>";
				add_xml_str = add_xml_str + "<amount>"+amount+"</amount>";
				add_xml_str = add_xml_str + "<reason>"+reason+"</reason>";
				add_xml_str = add_xml_str + "<return_date>"+returnDate+"</return_date>"; 
				add_xml_str = add_xml_str + "<provider_no>"+"RAKBANK"+"</provider_no>";
				if (bouncedCheq.equalsIgnoreCase("DDS")){
					add_xml_str = add_xml_str + "<bounced_cheque_dds>"+"1"+"</bounced_cheque_dds>"; 
				}
				add_xml_str=  add_xml_str+"<company_flag>N</company_flag></InternalBouncedCheques>";
			}

		}
		mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}


	public String InternalBureauIndividualProducts(){
		mLogger.info("RLOSCommon java file"+ "inside InternalBureauIndividualProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "SELECT cifid,agreementid,loantype,loantype,custroletype,loan_start_date,loanmaturitydate,lastupdatedate ,totaloutstandingamt,totalloanamount,NextInstallmentAmt,paymentmode,totalnoofinstalments,remaininginstalments,totalloanamount,	overdueamt,nofdayspmtdelay,monthsonbook,currentlycurrentflg,currmaxutil,DPD_30_in_last_6_months,DPD_60_in_last_18_months,propertyvalue,loan_disbursal_date,marketingcode,DPD_30_in_last_3_months,DPD_30_in_last_6_months,DPD_30_in_last_9_months,DPD_30_in_last_12_months,DPD_30_in_last_18_months,DPD_30_in_last_24_months,DPD_60_in_last_3_months,DPD_60_in_last_6_months,DPD_60_in_last_9_months,DPD_60_in_last_12_months,DPD_60_in_last_18_months,DPD_60_in_last_24_months,DPD_90_in_last_3_months,DPD_90_in_last_6_months,DPD_90_in_last_9_months,DPD_90_in_last_12_months,DPD_90_in_last_18_months,DPD_90_in_last_24_months,DPD_120_in_last_3_months,DPD_120_in_last_6_months,DPD_120_in_last_9_months,DPD_120_in_last_12_months,DPD_120_in_last_18_months,DPD_120_in_last_24_months,DPD_150_in_last_3_months,DPD_150_in_last_6_months,DPD_150_in_last_9_months,DPD_150_in_last_12_months,DPD_150_in_last_18_months,DPD_150_in_last_24_months,DPD_180_in_last_3_months,DPD_180_in_last_6_months,DPD_180_in_last_9_months,DPD_180_in_last_12_months,DPD_180_in_last_24_months,'' as col1,,isnull(Consider_For_Obligations,'true'),LoanStat,'LOANS',writeoffStat,writeoffstatdt,lastrepmtdt,limit_increase,PartSettlementDetails,'' as SchemeCardProduct,General_Status,Internal_WriteOff_Check FROM ng_RLOS_CUSTEXPOSE_LoanDetails WHERE Child_wi = '"+formObject.getWFWorkitemName()+"' and LoanStat !='Pipeline' union select CifId,CardEmbossNum,CardType,CardType,CustRoleType,'' as col6,'' as col7,'' as col8,OutstandingAmt,CreditLimit,PaymentsAmount,PaymentMode,'' as col13,'' as col14,CashLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,'' as col19,CurrMaxUtil,DPD_30_in_last_6_months,DPD_60_in_last_18_months,'' as col23,'' as col24,'' as col25,DPD_30_in_last_3_months,DPD_30_in_last_6_months,DPD_30_in_last_9_months,DPD_30_in_last_12_months,DPD_30_in_last_18_months,DPD_30_in_last_24_months,DPD_60_in_last_3_months,DPD_60_in_last_6_months,DPD_60_in_last_9_months,DPD_60_in_last_12_months,DPD_60_in_last_18_months,DPD_60_in_last_24_months,DPD_90_in_last_3_months,DPD_90_in_last_6_months,DPD_90_in_last_9_months,DPD_90_in_last_12_months,DPD_90_in_last_18_months,DPD_90_in_last_24_months,DPD_120_in_last_3_months,DPD_120_in_last_6_months,DPD_120_in_last_9_months,DPD_120_in_last_12_months,DPD_120_in_last_18_months,DPD_120_in_last_24_months,DPD_150_in_last_3_months,DPD_150_in_last_6_months,DPD_150_in_last_9_months,DPD_150_in_last_12_months,DPD_150_in_last_18_months,DPD_150_in_last_24_months,DPD_180_in_last_3_months,DPD_180_in_last_6_months,DPD_180_in_last_9_months,DPD_180_in_last_12_months,DPD_180_in_last_24_months,ExpiryDate,,isnull(Consider_For_Obligations,'true'),CardStatus,'CARDS',writeoffStat,writeoffstatdt,'' as lastrepdate,limit_increase,'' as PartSettlementDetails,SchemeCardProd,General_Status,Internal_WriteOff_Check FROM ng_RLOS_CUSTEXPOSE_CardDetails where 	Child_wi = '"+formObject.getWFWorkitemName()+"' and Request_Type In ('InternalExposure','CollectionsSummary')";
		mLogger.info("InternalBureauIndividualProducts sQuery"+sQuery+ "");
		String CountQuery ="select count(*) from ng_RLOS_CUSTEXPOSE_CardDetails where Child_wi = '"+formObject.getWFWorkitemName()+"' and cardStatus='A'";
		List<List<String>> CountXML = formObject.getDataFromDataSource(CountQuery);
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		mLogger.info("InternalBureauIndividualProducts list size"+OutputXML.size()+ "");
		mLogger.info("InternalBureauIndividualProducts list "+OutputXML+ "");
		String ReqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
		String mol_sal_var = "";
		mLogger.info("InternalBureauIndividualProducts outside cmplx_MOL_molsalvar "+formObject.getNGValue("cmplx_MOL_molsalvar")+ "");
		if(formObject.getNGValue("cmplx_MOL_molsalvar")!= null){
			mLogger.info("InternalBureauIndividualProducts  outside cmplx_MOL_molsalvar "+formObject.getNGValue("cmplx_MOL_molsalvar")+ "");
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
				mLogger.info("Inside for"+ "asdasdasd");


				if(!(OutputXML.get(i).get(0) == null || OutputXML.get(i).get(0).equals("")) ){
					cifId = OutputXML.get(i).get(0);
				}
				if(!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals("")) ){
					agreementId = OutputXML.get(i).get(1);
				}				
				if(!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2).equals("")) ){
					product_type = OutputXML.get(i).get(2);
					if (product_type.equalsIgnoreCase("Home In One")){
						product_type="HIO";
					}
					else{
						product_type = OutputXML.get(i).get(63);
					}
				}
						
				if(!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5).equals("")) ){
					loan_start_date = OutputXML.get(i).get(5);
				}
				if(OutputXML.get(i).get(6)!=null && !OutputXML.get(i).get(6).isEmpty() &&  !OutputXML.get(i).get(6).equals("") && !OutputXML.get(i).get(6).equalsIgnoreCase("null") ){
					loanmaturitydate = OutputXML.get(i).get(6);
				}
				if(OutputXML.get(i).get(7)!=null && !OutputXML.get(i).get(7).isEmpty() &&  !OutputXML.get(i).get(7).equals("") && !OutputXML.get(i).get(7).equalsIgnoreCase("null") ){
					lastupdatedate = OutputXML.get(i).get(7);
				}				
				if(OutputXML.get(i).get(8)!=null && !OutputXML.get(i).get(8).isEmpty() &&  !OutputXML.get(i).get(8).equals("") && !OutputXML.get(i).get(8).equalsIgnoreCase("null") ){
					outstandingamt = OutputXML.get(i).get(8);
				}
				if(!(OutputXML.get(i).get(9) == null || OutputXML.get(i).get(9).equals("")) ){
					totalloanamount = OutputXML.get(i).get(9);
				}
				if(!(OutputXML.get(i).get(10) == null || OutputXML.get(i).get(10).equals("")) ){
					Emi = OutputXML.get(i).get(10);
				}				
				if(!(OutputXML.get(i).get(11) == null || OutputXML.get(i).get(11).equals("")) ){
					paymentmode = OutputXML.get(i).get(11);
				}
				if(OutputXML.get(i).get(12)!=null && !OutputXML.get(i).get(12).isEmpty() &&  !OutputXML.get(i).get(12).equals("") && !OutputXML.get(i).get(12).equalsIgnoreCase("null") ){
					//SKLogger.writeLog("Inside for", "asdasdasd"+OutputXML.get(i).get(12));
					totalnoofinstalments = OutputXML.get(i).get(12);
				}
				if(!(OutputXML.get(i).get(13) == null || OutputXML.get(i).get(13).equals("")) ){
					remaininginstalments = OutputXML.get(i).get(13);
				}				
				
				if(!(OutputXML.get(i).get(15) == null || OutputXML.get(i).get(15).equals("")) ){
					overdueamt = OutputXML.get(i).get(15);
				}
				if(!(OutputXML.get(i).get(16) == null || OutputXML.get(i).get(16).equals("")) ){
					nofdayspmtdelay = OutputXML.get(i).get(16);
				}				
				if(!(OutputXML.get(i).get(17) == null || OutputXML.get(i).get(17).equals("")) ){
					monthsonbook = OutputXML.get(i).get(17);
				}
				if(!(OutputXML.get(i).get(18) == null || OutputXML.get(i).get(18).equals("")) ){
					currentlycurrent = OutputXML.get(i).get(18);
				}
				if(!(OutputXML.get(i).get(19) == null || OutputXML.get(i).get(19).equals("")) ){
					currmaxutil = OutputXML.get(i).get(19);
				}				
				if(!(OutputXML.get(i).get(20) == null || OutputXML.get(i).get(20).equals("")) ){
					DPD_30_in_last_6_months = OutputXML.get(i).get(20);
				}
				if(!(OutputXML.get(i).get(21) == null || OutputXML.get(i).get(21).equals("")) ){
					DPD_60_in_last_18_months = OutputXML.get(i).get(21);
				}
				if(!(OutputXML.get(i).get(22) == null || OutputXML.get(i).get(22).equals("")) ){
					propertyvalue = OutputXML.get(i).get(22);
				}				
				if(!(OutputXML.get(i).get(23) == null || OutputXML.get(i).get(23).equals("")) ){
					loan_disbursal_date = OutputXML.get(i).get(23);
				}
				if(!(OutputXML.get(i).get(24) == null || OutputXML.get(i).get(24).equals("")) ){
					marketingcode = OutputXML.get(i).get(24);
				}
				if(!(OutputXML.get(i).get(25) == null || OutputXML.get(i).get(25).equals("")) ){
					DPD_30_in_last_3_months = OutputXML.get(i).get(25);
				}				
				if(!(OutputXML.get(i).get(26) == null || OutputXML.get(i).get(26).equals("")) ){
					DPD_30_in_last_9_months	 = OutputXML.get(i).get(26);
				}
				if(!(OutputXML.get(i).get(27) == null || OutputXML.get(i).get(27).equals("")) ){
					DPD_30_in_last_12_months = OutputXML.get(i).get(27);
				}
				if(!(OutputXML.get(i).get(28) == null || OutputXML.get(i).get(28).equals("")) ){
					DPD_30_in_last_18_months = OutputXML.get(i).get(28);
				}				
				if(!(OutputXML.get(i).get(29) == null || OutputXML.get(i).get(29).equals("")) ){
					DPD_30_in_last_24_months = OutputXML.get(i).get(29);
				}
				if(!(OutputXML.get(i).get(30) == null || OutputXML.get(i).get(30).equals("")) ){
					DPD_60_in_last_3_months = OutputXML.get(i).get(30);
				}
				if(!(OutputXML.get(i).get(31) == null || OutputXML.get(i).get(31).equals("")) ){
					DPD_60_in_last_6_months = OutputXML.get(i).get(31);
				}				
				if(!(OutputXML.get(i).get(32) == null || OutputXML.get(i).get(32).equals("")) ){
					DPD_60_in_last_9_months = OutputXML.get(i).get(32);
				}
				if(!(OutputXML.get(i).get(33) == null || OutputXML.get(i).get(33).equals("")) ){
					DPD_60_in_last_12_months = OutputXML.get(i).get(33);
				}
				if(!(OutputXML.get(i).get(34) == null || OutputXML.get(i).get(34).equals("")) ){
					DPD_60_in_last_24_months = OutputXML.get(i).get(34);
				}				
				if(!(OutputXML.get(i).get(35) == null || OutputXML.get(i).get(35).equals("")) ){
					DPD_90_in_last_3_months = OutputXML.get(i).get(35);
				}
				if(!(OutputXML.get(i).get(36) == null || OutputXML.get(i).get(36).equals("")) ){
					DPD_90_in_last_6_months = OutputXML.get(i).get(36);
				}
				if(!(OutputXML.get(i).get(37) == null || OutputXML.get(i).get(37).equals("")) ){
					DPD_90_in_last_9_months = OutputXML.get(i).get(37);
				}				
				if(!(OutputXML.get(i).get(38) == null || OutputXML.get(i).get(38).equals("")) ){
					DPD_90_in_last_12_months = OutputXML.get(i).get(38);
				}
				if(!(OutputXML.get(i).get(39) == null || OutputXML.get(i).get(39).equals("")) ){
					DPD_90_in_last_18_months = OutputXML.get(i).get(39);
				}
				if(!(OutputXML.get(i).get(40) == null || OutputXML.get(i).get(40).equals("")) ){
					DPD_90_in_last_24_months = OutputXML.get(i).get(40);
				}				
				if(!(OutputXML.get(i).get(41) == null || OutputXML.get(i).get(41).equals("")) ){
					DPD_120_in_last_3_months = OutputXML.get(i).get(41);
				}
				if(!(OutputXML.get(i).get(42) == null || OutputXML.get(i).get(42).equals("")) ){
					DPD_120_in_last_6_months = OutputXML.get(i).get(42);
				}
				if(!(OutputXML.get(i).get(43) == null || OutputXML.get(i).get(43).equals("")) ){
					DPD_120_in_last_9_months = OutputXML.get(i).get(43);
				}				
				if(!(OutputXML.get(i).get(44) == null || OutputXML.get(i).get(44).equals("")) ){
					DPD_120_in_last_12_months = OutputXML.get(i).get(44);
				}
				if(!(OutputXML.get(i).get(45) == null || OutputXML.get(i).get(45).equals("")) ){
					DPD_120_in_last_18_months = OutputXML.get(i).get(45);
				}
				if(!(OutputXML.get(i).get(46) == null || OutputXML.get(i).get(46).equals("")) ){
					DPD_120_in_last_24_months = OutputXML.get(i).get(46);
				}				
				if(!(OutputXML.get(i).get(47) == null || OutputXML.get(i).get(47).equals("")) ){
					DPD_150_in_last_3_months = OutputXML.get(i).get(47);
				}
				if(!(OutputXML.get(i).get(48) == null || OutputXML.get(i).get(48).equals("")) ){
					DPD_150_in_last_6_months = OutputXML.get(i).get(48);
				}
				if(!(OutputXML.get(i).get(49) == null || OutputXML.get(i).get(49).equals("")) ){
					DPD_150_in_last_9_months = OutputXML.get(i).get(49);
				}				
				if(!(OutputXML.get(i).get(50) == null || OutputXML.get(i).get(50).equals("")) ){
					DPD_150_in_last_12_months = OutputXML.get(i).get(50);
				}
				if(!(OutputXML.get(i).get(51) == null || OutputXML.get(i).get(51).equals("")) ){
					DPD_150_in_last_18_months = OutputXML.get(i).get(51);
				}
				if(!(OutputXML.get(i).get(52) == null || OutputXML.get(i).get(52).equals("")) ){
					DPD_150_in_last_24_months = OutputXML.get(i).get(52);
				}				
				if(!(OutputXML.get(i).get(53) == null || OutputXML.get(i).get(53).equals("")) ){
					DPD_180_in_last_3_months = OutputXML.get(i).get(53);
				}
				if(!(OutputXML.get(i).get(54) == null || OutputXML.get(i).get(54).equals("")) ){
					DPD_180_in_last_6_months = OutputXML.get(i).get(54);
				}
				if(!(OutputXML.get(i).get(55) == null || OutputXML.get(i).get(55).equals("")) ){
					DPD_180_in_last_9_months = OutputXML.get(i).get(55);
				}				
				if(!(OutputXML.get(i).get(56) == null || OutputXML.get(i).get(56).equals("")) ){
					DPD_180_in_last_12_months = OutputXML.get(i).get(56);
				}
				if(!(OutputXML.get(i).get(57) == null || OutputXML.get(i).get(57).equals("")) ){
					DPD_180_in_last_24_months = OutputXML.get(i).get(57);
				}
				if(!(OutputXML.get(i).get(60) == null || OutputXML.get(i).get(60).equals("")) ){
					CardExpiryDate = OutputXML.get(i).get(60);
				}

				if(!(OutputXML.get(i).get(61) == null || OutputXML.get(i).get(61).equals("")) ){
					Consider_For_Obligations = OutputXML.get(i).get(61);
					if (Consider_For_Obligations.equalsIgnoreCase("false")){
						Consider_For_Obligations="N";
					}
					else {
						Consider_For_Obligations="Y";
					}
				}

				if(!(OutputXML.get(i).get(62) == null || OutputXML.get(i).get(62).equals("")) ){
					phase = OutputXML.get(i).get(62);
					if (phase.startsWith("C")){
						phase="C";
					}
					else {
						phase="A";
					}
				}
				if(!(OutputXML.get(i).get(64) == null || OutputXML.get(i).get(64).equals("")) ){
					writeoffStat = OutputXML.get(i).get(64);
				}
				if(!(OutputXML.get(i).get(65) == null || OutputXML.get(i).get(65).equals("")) ){
					writeoffstatdt = OutputXML.get(i).get(65);
				}
				if(!(OutputXML.get(i).get(66) == null || OutputXML.get(i).get(66).equals("")) ){
					lastrepmtdt = OutputXML.get(i).get(66);
				}
				if(!(OutputXML.get(i).get(67) == null || OutputXML.get(i).get(67).equals("")) ){
					Limit_increase = OutputXML.get(i).get(67);
					if (Limit_increase.equalsIgnoreCase("false")){
						Limit_increase="Y";
					}
					else{
						Limit_increase="N";
					}
				}
				if(!(OutputXML.get(i).get(68) == null || OutputXML.get(i).get(68).equals("")) ){
					part_settlement_date = OutputXML.get(i).get(67);
					String abc=OutputXML.get(i).get(68);
					abc=abc.substring(0, 10)+"split"+abc.substring(10,abc.length() );
					String[] abcsa=abc.split("split");
					part_settlement_date = abcsa[0];
					part_settlement_amount = abcsa[1];
				}
				if(!(OutputXML.get(i).get(69) == null || OutputXML.get(i).get(69).equals("")) ){
					SchemeCardProduct = OutputXML.get(i).get(69);
				}
				if(!(OutputXML.get(i).get(70) == null || OutputXML.get(i).get(70).equals("")) ){
					General_Status = OutputXML.get(i).get(70);
				}
				if(!(OutputXML.get(i).get(71) == null || OutputXML.get(i).get(71).equals("")) ){
					Internal_WriteOff_Check = OutputXML.get(i).get(71);
				}
				String sQueryCombinedLimit = "select Distinct(COMBINEDLIMIT_ELIGIBILITY) from ng_master_cardProduct where code='"+SchemeCardProduct+"'";
				List<List<String>> sQueryCombinedLimitXML = formObject.getDataFromDataSource(sQueryCombinedLimit);
				if (sQueryCombinedLimitXML!= null){
					Combined_Limit=sQueryCombinedLimitXML.get(0).get(0).equalsIgnoreCase("1")?"Y":"N";
				}
				String sQuerySecuredCard = "select count(*) from ng_master_cardProduct where code='"+SchemeCardProduct+"'  and subproduct='SEC'";
				List<List<String>> sQuerySecuredCardXML = formObject.getDataFromDataSource(sQuerySecuredCard);
				if (sQuerySecuredCardXML!= null){
					SecuredCard=sQuerySecuredCardXML.get(0).get(0).equalsIgnoreCase("0")?"N":"Y";
				}

				if(cifId!=null && !cifId.equalsIgnoreCase("") && !cifId.equalsIgnoreCase("null")){
					mLogger.info("Inside if"+ "asdasdasd");
					add_xml_str = add_xml_str + "<InternalBureauIndividualProducts><applicant_id>"+cifId+"</applicant_id>";
					add_xml_str = add_xml_str + "<internal_bureau_individual_products_id>"+agreementId+"</internal_bureau_individual_products_id>";
					add_xml_str = add_xml_str + "<type_product>"+product_type+"</type_product>";
					if (OutputXML.get(i).get(63).equalsIgnoreCase("cards")){					
						add_xml_str = add_xml_str + "<contract_type>CC</contract_type>";
					}
					if (OutputXML.get(i).get(63).equalsIgnoreCase("Loans")){
						add_xml_str = add_xml_str + "<contract_type>PL</contract_type>";
					}	
					add_xml_str = add_xml_str + "<provider_no>"+"RAKBANK"+"</provider_no>";
					add_xml_str = add_xml_str + "<phase>"+phase+"</phase>";
					add_xml_str = add_xml_str + "<role_of_customer>Primary</role_of_customer>"; 

					add_xml_str = add_xml_str + "<start_date>"+loan_start_date+"</start_date>"; 
					add_xml_str = add_xml_str + "<close_date>"+loanmaturitydate+"</close_date>"; 
					add_xml_str = add_xml_str + "<date_last_updated>"+lastupdatedate+"</date_last_updated>"; 
					add_xml_str = add_xml_str + "<outstanding_balance>"+outstandingamt+"</outstanding_balance>"; 
					if (OutputXML.get(i).get(63).equalsIgnoreCase("Loans")){
						add_xml_str = add_xml_str + "<total_amount>"+totalloanamount+"</total_amount>";
					}
					add_xml_str = add_xml_str + "<payments_amount>"+Emi+"</payments_amount>"; 
					add_xml_str = add_xml_str + "<method_of_payment>"+paymentmode+"</method_of_payment>"; 
					add_xml_str = add_xml_str + "<total_no_of_instalments>"+totalnoofinstalments+"</total_no_of_instalments>"; 
					add_xml_str = add_xml_str + "<no_of_remaining_instalments>"+remaininginstalments+"</no_of_remaining_instalments>"; 
					add_xml_str = add_xml_str + "<worst_status>"+writeoffStat+"</worst_status>"; 
					add_xml_str = add_xml_str + "<worst_status_date>"+writeoffstatdt+"</worst_status_date>"; 
					if (OutputXML.get(i).get(63).equalsIgnoreCase("cards")){

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
						if(Kompass.equalsIgnoreCase("true")){
							add_xml_str = add_xml_str + "<kompass>"+"Y"+"</kompass>";
						}
						else{
							add_xml_str = add_xml_str + "<kompass>"+"N"+"</kompass>";
						}
					}
					add_xml_str = add_xml_str + "<employer_type>"+EmployerType+"</employer_type>"; 


					if (totalnoofinstalments!=null && remaininginstalments!=null && !totalnoofinstalments.equals("") &&  !remaininginstalments.equals("")) {
						paid_installment= Integer.toString(Integer.parseInt(totalnoofinstalments) -Integer.parseInt(remaininginstalments));
						mLogger.info("Inside paid_installment"+ "paid_installment"+paid_installment);

					}
					if (ReqProd.equalsIgnoreCase("Credit Card")){

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
			mLogger.info("RLOSCommon"+ "Internal liab tag Cration: ");
			PLCommon.printException(e);
		}
		mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}

	public String InternalBureauPipelineProducts(){
		mLogger.info("RLOSCommon java file"+ "inside InternalBureauPipelineProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "SELECT cifid,product_type,custroletype,lastupdatedate,totalamount,totalnoofinstalments,totalloanamount,agreementId FROM ng_RLOS_CUSTEXPOSE_LoanDetails  with (nolock) where Child_wi = '"+formObject.getWFWorkitemName()+"' and  LoanStat = 'Pipeline'";
		mLogger.info("InternalBureauPipelineProducts sQuery"+sQuery+ "");
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		mLogger.info("InternalBureauPipelineProducts list size"+OutputXML.size()+ "");

		for (int i = 0; i<OutputXML.size();i++){

			String cifId = "";
			String Product = "";
			String lastUpdateDate = ""; 
			String TotAmount = "";
			String TotNoOfInstlmnt = "";
			String TotLoanAmt = "";
			String agreementId = "";
			if(!(OutputXML.get(i).get(0) == null || OutputXML.get(i).get(0).equals("")) ){
				cifId = OutputXML.get(i).get(0);
			}
			if(!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals("")) ){
				Product = OutputXML.get(i).get(1);
			}
			
			if(!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals("")) ){
				lastUpdateDate = OutputXML.get(i).get(3);
			}
			if(!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals("")) ){
				TotAmount = OutputXML.get(i).get(4);
			}
			if(!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5).equals("")) ){
				TotNoOfInstlmnt = OutputXML.get(i).get(5);
			}
			if(!(OutputXML.get(i).get(6) == null || OutputXML.get(i).get(6).equals("")) ){
				TotLoanAmt = OutputXML.get(i).get(6);
			}
			if(!(OutputXML.get(i).get(7) == null || OutputXML.get(i).get(7).equals("")) ){
				agreementId = OutputXML.get(i).get(7);
			}
			if(cifId!=null && !cifId.equalsIgnoreCase("") && !cifId.equalsIgnoreCase("null")){
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
		mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}


	public String ExternalBureauData(){
		mLogger.info("RLOSCommon java file"+ "inside ExternalBureauData : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "select  CifId,fullnm,TotalOutstanding,TotalOverdue,NoOfContracts,Total_Exposure,WorstCurrentPaymentDelay,Worst_PaymentDelay_Last24Months,Worst_Status_Last24Months,Nof_Records,NoOf_Cheque_Return_Last3,Nof_DDES_Return_Last3Months,Nof_Cheque_Return_Last6,DPD30_Last6Months,Internal_WriteOff_Check from NG_rlos_custexpose_Derived where Child_wi  = '"+formObject.getWFWorkitemName()+"' and Request_type= 'ExternalExposure'";
		mLogger.info("ExternalBureauData sQuery"+sQuery+ "");
		String AecbHistQuery = "select isnull(max(AECBHistMonthCnt),0) as AECBHistMonthCnt from ( select MAX(AECBHistMonthCnt) as AECBHistMonthCnt  from ng_rlos_cust_extexpo_CardDetails where  wi_name  = '"+formObject.getWFWorkitemName()+"' union select Max(AECBHistMonthCnt) from ng_rlos_cust_extexpo_LoanDetails where Child_wi  = '"+formObject.getWFWorkitemName()+"') as ext_expo";
		String  add_xml_str ="";
		try{
			List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
			mLogger.info("ExternalBureauData list size"+OutputXML.size()+ "");
			List<List<String>> AecbHistQueryData = formObject.getDataFromDataSource(AecbHistQuery);
			/*
		float TotOutstandingAmt = 0.0f;
		float TotOverdueAmt = 0.0f;
		float TotalExposure = 0.0f;
		for(int i = 0; i<OutputXML.size();i++){
			if(OutputXML.get(i).get(1)!=null && !OutputXML.get(i).get(1).isEmpty() &&  !OutputXML.get(i).get(1).equals("") && !OutputXML.get(i).get(1).equalsIgnoreCase("null") ){
					TotOutstandingAmt = TotOutstandingAmt + Float.parseFloat(OutputXML.get(i).get(1));
			}
			if(OutputXML.get(i).get(2)!=null && !OutputXML.get(i).get(2).isEmpty() &&  !OutputXML.get(i).get(2).equals("") && !OutputXML.get(i).get(2).equalsIgnoreCase("null") ){
					TotOverdueAmt = TotOverdueAmt + Float.parseFloat(OutputXML.get(i).get(2));
			}
			if(OutputXML.get(i).get(3)!=null && !OutputXML.get(i).get(3).isEmpty() &&  !OutputXML.get(i).get(3).equals("") && !OutputXML.get(i).get(3).equalsIgnoreCase("null") ){
					TotalExposure = TotalExposure + Float.parseFloat(OutputXML.get(i).get(3));
			}
		}*/
			if (AecbHistQueryData.get(0).get(0).equalsIgnoreCase("0")){


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



				mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "+ add_xml_str);
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
					if(!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals("")) ){
						fullnm = OutputXML.get(i).get(1);
					}				
					if(!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2).equals("")) ){
						TotalOutstanding = OutputXML.get(i).get(2);

					}
					if(!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals("")) ){
						TotalOverdue = OutputXML.get(i).get(3);
					}
					if(!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals("")) ){
						NoOfContracts = OutputXML.get(i).get(4);
					}				
					if(!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5).equals("")) ){
						Total_Exposure = OutputXML.get(i).get(5);
					}
					if(OutputXML.get(i).get(6)!=null && !OutputXML.get(i).get(6).isEmpty() &&  !OutputXML.get(i).get(6).equals("") && !OutputXML.get(i).get(6).equalsIgnoreCase("null") ){
						WorstCurrentPaymentDelay = OutputXML.get(i).get(6);
					}
					if(OutputXML.get(i).get(7)!=null && !OutputXML.get(i).get(7).isEmpty() &&  !OutputXML.get(i).get(7).equals("") && !OutputXML.get(i).get(7).equalsIgnoreCase("null") ){
						Worst_PaymentDelay_Last24Months = OutputXML.get(i).get(7);
					}				
					if(OutputXML.get(i).get(8)!=null && !OutputXML.get(i).get(8).isEmpty() &&  !OutputXML.get(i).get(8).equals("") && !OutputXML.get(i).get(8).equalsIgnoreCase("null") ){
						Worst_Status_Last24Months = OutputXML.get(i).get(8);
					}
					if(!(OutputXML.get(i).get(9) == null || OutputXML.get(i).get(9).equals("")) ){
						Nof_Records = OutputXML.get(i).get(9);
					}
					if(!(OutputXML.get(i).get(10) == null || OutputXML.get(i).get(10).equals("")) ){
						NoOf_Cheque_Return_Last3 = OutputXML.get(i).get(10);
					}				
					if(!(OutputXML.get(i).get(11) == null || OutputXML.get(i).get(11).equals("")) ){
						Nof_DDES_Return_Last3Months = OutputXML.get(i).get(11);
					}
					if(OutputXML.get(i).get(12)!=null && !OutputXML.get(i).get(12).isEmpty() &&  !OutputXML.get(i).get(12).equals("") && !OutputXML.get(i).get(12).equalsIgnoreCase("null") ){
						//SKLogger.writeLog("Inside for", "asdasdasd"+OutputXML.get(i).get(12));
						Nof_Cheque_Return_Last6 = OutputXML.get(i).get(12);
					}
					if(!(OutputXML.get(i).get(13) == null || OutputXML.get(i).get(13).equals("")) ){
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
				mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "+ add_xml_str);
				return add_xml_str;
			}
		}
		catch(Exception e)
		{
			new PersonalLoanSCommonCode();
			mLogger.info("RLOSCommon"+ "Exception occurred in externalBureauData()");
			PLCommon.printException(e);
			return null;
		}
	}

	public String ExternalBouncedCheques(){
		mLogger.info("RLOSCommon java file"+ "inside ExternalBouncedCheques : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "SELECT cifid,number,amount,reasoncode,returndate,providerno FROM ng_rlos_cust_extexpo_ChequeDetails  with (nolock) where child_wi = '"+formObject.getWFWorkitemName()+"' and Request_Type = 'ExternalExposure'";
		mLogger.info("ExternalBouncedCheques sQuery"+sQuery+ "");
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		mLogger.info("ExternalBouncedCheques list size"+OutputXML.size()+ "");

		for (int i = 0; i<OutputXML.size();i++){

			String CifId = formObject.getNGValue("cmplx_Customer_CIFNO");
			String chqNo = "";
			String Amount = "";
			String Reason = ""; 
			String returnDate = "";
			String providerNo = "";


			if(!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals("")) ){
				chqNo = OutputXML.get(i).get(1);
			}
			if(!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2).equals("")) ){
				Amount = OutputXML.get(i).get(2);
			}
			if(!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals("")) ){
				Reason = OutputXML.get(i).get(3);
			}
			if(!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals("")) ){
				returnDate = OutputXML.get(i).get(4);
			}
			if(!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5).equals("")) ){
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
		mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}


	public String ExternalBureauIndividualProducts(){
		mLogger.info("RLOSCommon java file"+ "inside ExternalBureauIndividualProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "select CifId,AgreementId,LoanType,ProviderNo,LoanStat,CustRoleType,LoanApprovedDate,LoanMaturityDate,OutstandingAmt,TotalAmt,PaymentsAmt,TotalNoOfInstalments,RemainingInstalments,WriteoffStat,WriteoffStatDt,CreditLimit,OverdueAmt,NofDaysPmtDelay,MonthsOnBook,lastrepmtdt,IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last18Months,AECBHistMonthCnt,DPD5_Last3Months,'' as qc_amt,'' as qc_emi,'' as Cac_indicator,Take_Over_Indicator,,isnull(Consider_For_Obligations,'true') from ng_rlos_cust_extexpo_LoanDetails where child_wi= '"+formObject.getWFWorkitemName()+"'  and LoanStat != 'Pipeline' union select CifId,CardEmbossNum,CardType,ProviderNo,CardStatus,CustRoleType,StartDate,ClosedDate,CurrentBalance,'' as col6,PaymentsAmount,NoOfInstallments,'' as col5,WriteoffStat,WriteoffStatDt,CashLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,lastrepmtdt,IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last18Months,AECBHistMonthCnt,DPD5_Last3Months,QC_Amt,QC_EMI,CAC_Indicator,Take_Over_Indicator,,isnull(Consider_For_Obligations,'true') from ng_rlos_cust_extexpo_CardDetails where child_wi  =  '"+formObject.getWFWorkitemName()+"' and cardstatus != 'Pipeline' ";
		mLogger.info("ExternalBureauIndividualProducts sQuery"+sQuery+ "");
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		mLogger.info("ExternalBureauIndividualProducts list size"+OutputXML.size()+ "");
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
			if(!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals("")) ){
				AgreementId = OutputXML.get(i).get(1);
			}				
			if(!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2).equals("")) ){
				ContractType = OutputXML.get(i).get(2);
				try{
					String cardquery = "select code from ng_master_contract_type where description='"+ContractType+"'";
					mLogger.info("ExternalBureauIndividualProducts sQuery"+sQuery+ "");
					List<List<String>> cardqueryXML = formObject.getDataFromDataSource(cardquery);
					ContractType=cardqueryXML.get(0).get(0);
					mLogger.info("ExternalBureauIndividualProducts ContractType"+ContractType+ "ContractType");
				}
				catch(Exception e){
					mLogger.info("ExternalBureauIndividualProducts ContractType Exception"+e+ "Exception");

					ContractType= OutputXML.get(i).get(2);
				}
			}
			if(!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals("")) ){
				provider_no = OutputXML.get(i).get(3);
			}
			if(!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals("")) ){
				phase = OutputXML.get(i).get(4);
				if (phase.startsWith("A")){
					phase="A";
				}
				else {
					phase="C";
				}
			}				
			if(!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5).equals("")) ){
				CustRoleType = OutputXML.get(i).get(5);
			}
			if(!(OutputXML.get(i).get(6) == null || OutputXML.get(i).get(6).equals("")) ){
				start_date = OutputXML.get(i).get(6);
			}
			if(!(OutputXML.get(i).get(7) == null || OutputXML.get(i).get(7).equals("")) ){
				close_date = OutputXML.get(i).get(7);
			}				
			if(!(OutputXML.get(i).get(8) == null || OutputXML.get(i).get(8).equals("")) ){
				OutStanding_Balance = OutputXML.get(i).get(8);
			}
			if(!(OutputXML.get(i).get(9) == null || OutputXML.get(i).get(9).equals("")) ){
				TotalAmt = OutputXML.get(i).get(9);
			}
			if(!(OutputXML.get(i).get(10) == null || OutputXML.get(i).get(10).equals("")) ){
				PaymentsAmt = OutputXML.get(i).get(10);
			}
			if(!(OutputXML.get(i).get(11) == null || OutputXML.get(i).get(11).equals("")) ){
				TotalNoOfInstalments = OutputXML.get(i).get(11);
			}
			if(!(OutputXML.get(i).get(12) == null || OutputXML.get(i).get(12).equals("")) ){
				RemainingInstalments = OutputXML.get(i).get(12);
			}
			if(!(OutputXML.get(i).get(13) == null || OutputXML.get(i).get(13).equals("")) ){
				WorstStatus = OutputXML.get(i).get(13);
			}
			if(!(OutputXML.get(i).get(14) == null || OutputXML.get(i).get(14).equals("")) ){
				WorstStatusDate = OutputXML.get(i).get(14);
			}				
			if(!(OutputXML.get(i).get(15) == null || OutputXML.get(i).get(15).equals("")) ){
				CreditLimit = OutputXML.get(i).get(15);
			}
			if(!(OutputXML.get(i).get(16) == null || OutputXML.get(i).get(16).equals("")) ){
				OverdueAmt = OutputXML.get(i).get(16);
			}
			if(!(OutputXML.get(i).get(17) == null || OutputXML.get(i).get(17).equals("")) ){
				NofDaysPmtDelay = OutputXML.get(i).get(17);
			}				
			if(!(OutputXML.get(i).get(18) == null || OutputXML.get(i).get(18).equals("")) ){
				MonthsOnBook = OutputXML.get(i).get(18);
			}
			if(!(OutputXML.get(i).get(19) == null || OutputXML.get(i).get(19).equals("")) ){
				last_repayment_date = OutputXML.get(i).get(19);
			}
			if(!(OutputXML.get(i).get(20) == null || OutputXML.get(i).get(20).equals("")) ){
				currently_current = OutputXML.get(i).get(20);
			}
			if(!(OutputXML.get(i).get(21) == null || OutputXML.get(i).get(21).equals("")) ){
				current_utilization = OutputXML.get(i).get(21);
			}
			if(!(OutputXML.get(i).get(22) == null || OutputXML.get(i).get(22).equals("")) ){
				DPD30Last6Months = OutputXML.get(i).get(22);
			}
			if(!(OutputXML.get(i).get(23) == null || OutputXML.get(i).get(23).equals("")) ){
				DPD60Last18Months = OutputXML.get(i).get(23);
			}
			if(!(OutputXML.get(i).get(24) == null || OutputXML.get(i).get(24).equals("")) ){
				AECBHistMonthCnt = OutputXML.get(i).get(24);
			}				


			if(!(OutputXML.get(i).get(25) == null || OutputXML.get(i).get(25).equals("")) ){
				delinquent_in_last_3months = OutputXML.get(i).get(25);
			}
			if(!(OutputXML.get(i).get(26) == null || OutputXML.get(i).get(26).equals("")) ){
				QC_Amt = OutputXML.get(i).get(26);
			}
			if(!(OutputXML.get(i).get(27) == null || OutputXML.get(i).get(27).equals("")) ){
				QC_emi = OutputXML.get(i).get(27);
			}
			if(!(OutputXML.get(i).get(28) == null || OutputXML.get(i).get(28).equals("")) ){
				CAC_Indicator = OutputXML.get(i).get(28);
				if (CAC_Indicator != null && !(CAC_Indicator.equalsIgnoreCase(""))){
					if (CAC_Indicator.equalsIgnoreCase("true")){
						CAC_Indicator="Y";
					}
					else {
						CAC_Indicator="N";
					}
				}
			}
			String TakeOverIndicator="";
			if(!(OutputXML.get(i).get(29) == null || OutputXML.get(i).get(29).equals("")) ){
				TakeOverIndicator = OutputXML.get(i).get(29);
				if (TakeOverIndicator != null && !(TakeOverIndicator.equalsIgnoreCase(""))){
					if (TakeOverIndicator.equalsIgnoreCase("true")){
						TakeOverIndicator="Y";
					}
					else {
						TakeOverIndicator="N";
					}
				}
			}
			if(!(OutputXML.get(i).get(30) == null || OutputXML.get(i).get(30).equals("")) ){
				consider_for_obligation = OutputXML.get(i).get(30);
				if (consider_for_obligation != null && !(consider_for_obligation.equalsIgnoreCase(""))){
					if (consider_for_obligation.equalsIgnoreCase("true")){
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
			if (currently_current != null && currently_current.equalsIgnoreCase("1"))
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
			if (ReqProd.equalsIgnoreCase("Credit Card")){
				add_xml_str = add_xml_str + "<qc_amount>"+QC_Amt+"</qc_amount><company_flag>N</company_flag><cac_bank_name></cac_bank_name></ExternalBureauIndividualProducts>";
			}
			else{
				add_xml_str = add_xml_str + "<qc_amount>"+QC_Amt+"</qc_amount><company_flag>N</company_flag><cac_bank_name></cac_bank_name><take_over_indicator>"+TakeOverIndicator+"</take_over_indicator><consider_for_obligation>"+consider_for_obligation+"</consider_for_obligation></ExternalBureauIndividualProducts>";
			}

		}
		mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}

	public String ExternalBureauManualAddIndividualProducts(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		mLogger.info("ExternalBureauManualAddIndividualProducts sQuery"+ "");
		int Man_liab_row_count = formObject.getLVWRowCount("cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
		String applicant_id = formObject.getNGValue("cmplx_Customer_CIFNO");
		String  add_xml_str ="";
		Date date = new Date();
		String modifiedDate= new SimpleDateFormat("dd/MM/yyyy").format(date);
		mLogger.info("ExternalBureauIndividualProducts list size"+Man_liab_row_count+ "");
		if (Man_liab_row_count !=0){
			for (int i = 0; i<Man_liab_row_count;i++){
				String Type_of_Contract = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 0); //0
				String Limit = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 1); //0
				String EMI = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 2); //0
				String cac_Indicator = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 5); //0
				String Qc_amt = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 6); //0
				String Qc_Emi = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 7); //0
				if (cac_Indicator.equalsIgnoreCase("true")){
					cac_Indicator="Y";
				}
				else {
					cac_Indicator="N";
				}
				String consider_for_obligation =formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 8).equalsIgnoreCase("true")?"Y":"N"; //0
				//String MOB = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 9); //0
				String Utilization = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 11); //0
				String OutStanding = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 12); //0
				String mob = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 10);
				String delinquent_in_last_3months = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 13).equalsIgnoreCase("true")?"1":"0";
				String dpd_30_last_6_mon = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 14).equalsIgnoreCase("true")?"1":"0";
				String dpd_60p_in_last_18_mon = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 15).equalsIgnoreCase("true")?"1":"0";
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
		mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}



	public String  getInternalLiabDetails(){
		mLogger.info("RLOSCommon java file"+ "inside getCustAddress_details : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "SELECT AcctType,CustRoleType,AcctId,AccountOpenDate,AcctStat,AcctSegment,AcctSubSegment,CreditGrade FROM ng_RLOS_CUSTEXPOSE_AcctDetails  with (nolock) where Child_Wi = '"+formObject.getWFWorkitemName()+"' and Request_Type = 'InternalExposure'";
		mLogger.info("getInternalLiabDetails sQuery"+sQuery+ "");
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		mLogger.info("getInternalLiabDetails list size"+OutputXML.size()+ "");

		for (int i = 0; i<OutputXML.size();i++){

			String accountType = "";
			String role = "";
			String accNumber = "";
			String acctOpenDate = ""; 
			String acctStatus = "";
			String acctSegment = "";
			String acctSubSegment = "";
			String acctCreditGrade = "";
			if(!(OutputXML.get(i).get(0) == null || OutputXML.get(i).get(0).equals("")) ){
				accountType = OutputXML.get(i).get(0);
			}
			if(!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals("")) ){
				role = OutputXML.get(i).get(1);
			}
			if(!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2).equals("")) ){
				accNumber = OutputXML.get(i).get(2);
			}
			if(!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals("")) ){
				acctOpenDate = OutputXML.get(i).get(3);
			}
			if(!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals("")) ){
				acctStatus = OutputXML.get(i).get(4);
			}
			if(!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5).equals("")) ){
				acctSegment = OutputXML.get(i).get(5);
			}
			if(!(OutputXML.get(i).get(6) == null || OutputXML.get(i).get(6).equals("")) ){
				acctSubSegment = OutputXML.get(i).get(6);
			}
			if(!(OutputXML.get(i).get(7) == null || OutputXML.get(i).get(7).equals("")) ){
				acctCreditGrade = OutputXML.get(i).get(7);
			}
			if(accNumber!=null && !accNumber.equalsIgnoreCase("") && !accNumber.equalsIgnoreCase("null")){
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
		mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}


	public String ExternalBureauPipelineProducts(){
		mLogger.info("RLOSCommon java file"+ "inside ExternalBureauPipelineProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "select AgreementId,ProviderNo,LoanType,LoanDesc,CustRoleType,Datelastupdated,TotalAmt,TotalNoOfInstalments,'' as col1,NoOfDaysInPipeline,isnull(Consider_For_Obligations,'true') from ng_rlos_cust_extexpo_LoanDetails where child_wi  =  '"+formObject.getWFWorkitemName()+"' and LoanStat = 'Pipeline' union select CardEmbossNum,ProviderNo,CardTypeDesc,CardType,CustRoleType,LastUpdateDate,'' as col2,NoOfInstallments,TotalAmount,NoOfDaysInPipeLine,isnull(Consider_For_Obligations,'true')  from ng_rlos_cust_extexpo_CardDetails where child_wi  =  '"+formObject.getWFWorkitemName()+"' and cardstatus = 'Pipeline'";
		mLogger.info("ExternalBureauPipelineProducts sQuery"+sQuery+ "");
		String  add_xml_str = "";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		mLogger.info("ExternalBureauPipelineProducts list size"+OutputXML.size()+ "");
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

			if(!(OutputXML.get(i).get(0) == null || OutputXML.get(i).get(0).equals("")) ){
				agreementID = OutputXML.get(i).get(0);
			}
			if(!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals("")) ){
				ProviderNo = OutputXML.get(i).get(1);
			}
			if(OutputXML.get(i).get(2)!=null && !OutputXML.get(i).get(2).isEmpty() &&  !OutputXML.get(i).get(2).equals("") && !OutputXML.get(i).get(2).equalsIgnoreCase("null") ){
				contractType = OutputXML.get(i).get(2);
			}
			if(!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals("")) ){
				productType = OutputXML.get(i).get(3);
			}
			if(!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals("")) ){
				role = OutputXML.get(i).get(4);
			}
			if(OutputXML.get(i).get(5)!=null && !OutputXML.get(i).get(5).isEmpty() &&  !OutputXML.get(i).get(5).equals("") && !OutputXML.get(i).get(5).equalsIgnoreCase("null") ){
				lastUpdateDate = OutputXML.get(i).get(5);
			}
			if(OutputXML.get(i).get(6)!=null && !OutputXML.get(i).get(6).isEmpty() &&  !OutputXML.get(i).get(6).equals("") && !OutputXML.get(i).get(6).equalsIgnoreCase("null") ){
				TotAmt = OutputXML.get(i).get(6);
			}
			if(!(OutputXML.get(i).get(7) == null || OutputXML.get(i).get(7).equals("")) ){
				noOfInstalmnt = OutputXML.get(i).get(7);
			}
			if(!(OutputXML.get(i).get(8) == null || OutputXML.get(i).get(8).equals("")) ){
				creditLimit = OutputXML.get(i).get(8);
			}
			if(OutputXML.get(i).get(9)!=null && !OutputXML.get(i).get(9).isEmpty() &&  !OutputXML.get(i).get(9).equals("") && !OutputXML.get(i).get(9).equalsIgnoreCase("null") ){
				noOfDayinPpl = OutputXML.get(i).get(9);
			}
			if(!(OutputXML.get(i).get(10) == null || OutputXML.get(i).get(10).equals("")) ){
				consider_for_obligation = OutputXML.get(i).get(10);
				if (consider_for_obligation != null && !(consider_for_obligation.equalsIgnoreCase(""))){
					if (consider_for_obligation.equalsIgnoreCase("true")){
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
		mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}

	public String getCustOECD_details(String call_name){
		mLogger.info("RLOSCommon java file"+ "inside getCustAddress_details : ");
		String  add_xml_str ="";
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			int add_row_count = formObject.getLVWRowCount("cmplx_OECD_cmplx_GR_OecdDetails");
			mLogger.info("RLOSCommon java file"+ "inside getCustAddress_details add_row_count+ : "+add_row_count);
			if(call_name.equalsIgnoreCase("CUSTOMER_UPDATE_REQ")){
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
			mLogger.info("RLOSCommon"+ "OECD tag creation "+ add_xml_str);
			return add_xml_str;
		}
		catch(Exception e){
			mLogger.info("PLCommon getCustAddress_details method"+ "Exception Occure in generate Address XMl"+e.getMessage());
			PLCommon.printException(e);
			return add_xml_str;
		}

	}

	public String getLien_details(String Call_name){
		mLogger.info("RLOSCommon java file"+ "inside getLienDetails_details : ");
		String  lien_xml_str ="";

		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			int add_row_count = formObject.getLVWRowCount("cmplx_FinacleCore_liendet_grid");
			mLogger.info("PL Common java file"+ "inside Lien details add_row_count+ : "+add_row_count);

			if (add_row_count>=0){
				String Lien_no = formObject.getNGValue("cmplx_FinacleCore_liendet_grid", 0, 1); //0
				String Lien_amount = formObject.getNGValue("cmplx_FinacleCore_liendet_grid", 0, 2); //0
				String Lien_maturity_date = formObject.getNGValue("cmplx_FinacleCore_liendet_grid", 0, 6); //0

				if(Call_name.equalsIgnoreCase("CARD_NOTIFICATION")){
					lien_xml_str = lien_xml_str+ "<LienDetails><LienNumber>"+Lien_no+"</LienNumber>";
					lien_xml_str = lien_xml_str+ "<LienCurrency>AED</LienCurrency><Amount>"+Lien_amount+"</Amount>";
					lien_xml_str = lien_xml_str+ "<MaturityDate>"+Lien_maturity_date+"</MaturityDate>";
					lien_xml_str = lien_xml_str+ "</LienDetails>";
				}
				else if(Call_name.equalsIgnoreCase("CARD_SERVICES_REQUEST")){
					lien_xml_str = lien_xml_str+ "<LienDetails><LienNumber>"+Lien_no+"</LienNumber>";
					lien_xml_str = lien_xml_str+ "<LienCurrency>AED</LienCurrency><LienAmount>"+Lien_amount+"</LienAmount>";
					lien_xml_str = lien_xml_str+ "<LienMaturityDt>"+Lien_maturity_date+"</LienMaturityDt>";
					lien_xml_str = lien_xml_str+ "</LienDetails>";
				}
			}
		}
		catch(Exception e){
			mLogger.info("PL Common java file"+ "Exception occured in get lien details method : "+e.getMessage());
			PLCommon.printException(e);
		}

		return lien_xml_str;

	}

	public String getcontact_details(){
		mLogger.info("RLOSCommon java file"+ "inside getContact_details : ");
		String  Contactdetails_xml_str ="";

		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			Contactdetails_xml_str = "<ContactDetails><PrefModeOfContact>Phone</PrefModeOfContact><PhnDet><PhoneType>MobileNumber1</PhoneType><PhnCountryCode>971</PhnCountryCode><CityCode></CityCode><PhnLocalCode>00971</PhnLocalCode><PhoneNumber>"+formObject.getNGValue("cmplx_Customer_MobNo")+"</PhoneNumber>";
			Contactdetails_xml_str = Contactdetails_xml_str+"<PhnPrefFlag>Y</PhnPrefFlag></PhnDet></ContactDetails><ContactDetails><PrefModeOfContact>Email</PrefModeOfContact><EmailDet><MailIdType>WORKEML</MailIdType><EmailID>"+formObject.getNGValue("AlternateContactDetails_EMAIL1_PRI")+"</EmailID><MailPrefFlag>Y</MailPrefFlag></EmailDet></ContactDetails><ContactDetails><PrefModeOfContact>Phone</PrefModeOfContact><PhnDet><PhoneType>HOMEPH2</PhoneType><PhnCountryCode>00971</PhnCountryCode>";
			Contactdetails_xml_str = Contactdetails_xml_str+"<CityCode></CityCode><PhnLocalCode>00971</PhnLocalCode><PhoneNumber>"+formObject.getNGValue("AlternateContactDetails_OFFICENO")+"</PhoneNumber><PhnPrefFlag>N</PhnPrefFlag></PhnDet></ContactDetails>";

		}
		catch(Exception e){
			mLogger.info("PL Common java file"+ "Exception occured in get lien details method : "+e.getMessage());
			PLCommon.printException(e);
		}

		return Contactdetails_xml_str;

	}

	//added 30/08/2017

	public String getCustAddress_details(){
		mLogger.info("RLOSCommon java file"+ "inside getCustAddress_details : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int add_row_count = formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
		mLogger.info("RLOSCommon java file"+ "inside getCustAddress_details add_row_count+ : "+add_row_count);
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
			mLogger.info("RLOSCommon java file"+ "inside getCustAddress_details add_row_count+ : "+years_in_current_add);
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
			mLogger.info("RLOS value of CurrentDate"+""+CurrentYear+"-"+CurrentMonth+"-"+CurrentDate);
			EffectiveTo=CurrentYear+"-"+CurrentMonth+"-"+CurrentDate;
			mLogger.info("RLOS value of CurrentDate EffectiveTo"+""+EffectiveTo);
			DateEffectiveFromYears=CurrentYear-years;
			EffectiveFrom=DateEffectiveFromYears+"-"+CurrentMonth+"-"+CurrentDate;
			mLogger.info("RLOS value of EffectiveFromDate"+""+EffectiveFrom);

			//ended

			if(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 10).equalsIgnoreCase("true"))//10
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
		mLogger.info("RLOSCommon"+ "Address tag Cration: "+ add_xml_str);
		return add_xml_str;
	}

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
		mLogger.info("inside getOutputXMLValues"+ "getMQInputXML"+ strBuff.toString());
		return strBuff.toString();
	}

	private Map<String, String> DECTECH_Custom(List<List<String>> OutputXML,FormReference formObject,String callName) {
		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();

		try{
			for (List<String> mylist : OutputXML) {
				// for(int i=0;i<col_n.length();i++)
				for (int i = 0; i < 8; i++) {
					// SKLogger_CC.writeLog("rec: "+records.item(rec));
					mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}

				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");

				if(tag_name.equalsIgnoreCase("Channel")){
					mLogger.info("RLOS COMMON"+" iNSIDE channelcode+ ");

					String ReqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);

					String xml_str = int_xml.get(parent_tag);
					xml_str =  "<"+tag_name+">"+(ReqProd.equalsIgnoreCase("Personal Loan")?"PL":"CC")
							+"</"+ tag_name+">";

					mLogger.info("RLOS COMMON"+" after adding channelcode+ "+xml_str);
					int_xml.put(parent_tag, xml_str);

				}

				else if(tag_name.equalsIgnoreCase("emp_type")){
					mLogger.info("RLOS COMMON"+" iNSIDE channelcode+ ");

					String empttype=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6);
					if(empttype!=null){	
						if (empttype.equalsIgnoreCase("Salaried")){
							empttype="S";
						}
						else if (empttype.equalsIgnoreCase("Salaried Pensioner")){
							empttype="SP";
						}
						else {
							empttype="SE";
						}
					}
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str+ "<"+tag_name+">"+empttype+"</"+ tag_name+">";

					mLogger.info("RLOS COMMON"+" after adding channelcode+ "+xml_str);
					int_xml.put(parent_tag, xml_str);

				}
				else if(tag_name.equalsIgnoreCase("world_check")){
					mLogger.info("RLOS COMMON"+" iNSIDE world_check+ ");

					String world_check=formObject.getNGValue("IS_WORLD_CHECK");
					mLogger.info("RLOS COMMON"+" iNSIDE world_check+ "+formObject.getLVWRowCount("cmplx_WorldCheck_WorldCheck_Grid"));
					if (formObject.getLVWRowCount("cmplx_WorldCheck_WorldCheck_Grid")==0){
						world_check="Negative";
					}
					else {
						world_check="Positive";
					}


					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str+ "<"+tag_name+">"+world_check+"</"+ tag_name+">";

					mLogger.info("RLOS COMMON"+" after adding world_check+ "+xml_str);
					int_xml.put(parent_tag, xml_str);

				}
				else if(tag_name.equalsIgnoreCase("current_emp_catogery")){
					mLogger.info("RLOS COMMON"+" iNSIDE current_emp_catogery+ ");

					String current_emp_catogery=formObject.getNGValue("cmplx_EmploymentDetails_CurrEmployer");
					mLogger.info("RLOS COMMON"+" value of current_emp_catogery "+current_emp_catogery);
					String squerycurremp="select Description from NG_MASTER_EmployerCategory_PL where isActive='Y' and code='"+current_emp_catogery+"'";
					mLogger.info("RLOS COMMON"+" query is "+squerycurremp);
					List<List<String>> squerycurrempXML=formObject.getDataFromDataSource(squerycurremp);
					mLogger.info("RLOS COMMON"+" query is "+squerycurrempXML);
					if(!squerycurrempXML.isEmpty()){
						if (squerycurrempXML.get(0).get(0)!=null){
							mLogger.info("RLOS COMMON"+" iNSIDE squerycurrempXML+ "+squerycurrempXML.get(0).get(0));
							current_emp_catogery=squerycurrempXML.get(0).get(0);
						}
					}

					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str+ "<"+tag_name+">"+current_emp_catogery+"</"+ tag_name+">";

					mLogger.info("RLOS COMMON"+" after adding current_emp_catogery+ "+xml_str);
					int_xml.put(parent_tag, xml_str);

				}
				else if(tag_name.equalsIgnoreCase("prev_loan_dbr")||tag_name.equalsIgnoreCase("prev_loan_tai")||
						tag_name.equalsIgnoreCase("prev_loan_multiple")||tag_name.equalsIgnoreCase("prev_loan_employer")){
					mLogger.info("RLOS COMMON"+" iNSIDE prev_loan_dbr+ ");
					String PreviousLoanDBR="";
					String PreviousLoanEmp="";
					String PreviousLoanMultiple="";
					String PreviousLoanTAI="";

					String squeryloan="select isNull(PreviousLoanDBR,0), isNull(PreviousLoanEmp,0), isNull(PreviousLoanMultiple,0), isNull(PreviousLoanTAI,0) from ng_RLOS_CUSTEXPOSE_LoanDetails where Request_Type='CollectionsSummary' and Limit_Increase='true'  and Child_wi= '"+formObject.getWFWorkitemName()+"'";
					List<List<String>> prevLoan=formObject.getDataFromDataSource(squeryloan);
					mLogger.info("RLOS COMMON"+" iNSIDE prev_loan_dbr+ "+squeryloan);

					if (prevLoan!=null && !prevLoan.isEmpty()){
						PreviousLoanDBR=prevLoan.get(0).get(0);
						PreviousLoanEmp=prevLoan.get(0).get(1);
						PreviousLoanMultiple=prevLoan.get(0).get(2);
						PreviousLoanTAI=prevLoan.get(0).get(3);
					}


					String xml_str = int_xml.get(parent_tag);
					if(tag_name.equalsIgnoreCase("prev_loan_dbr")){
						xml_str = xml_str+ "<"+tag_name+">"+PreviousLoanDBR+"</"+ tag_name+">";
					}
					else if(tag_name.equalsIgnoreCase("prev_loan_tai")){
						xml_str = xml_str+ "<"+tag_name+">"+PreviousLoanTAI+"</"+ tag_name+">";
					}
					else if(tag_name.equalsIgnoreCase("prev_loan_multiple")){
						xml_str = xml_str+ "<"+tag_name+">"+PreviousLoanMultiple+"</"+ tag_name+">";
					}
					else if(tag_name.equalsIgnoreCase("prev_loan_employer")){
						xml_str = xml_str+ "<"+tag_name+">"+PreviousLoanEmp+"</"+ tag_name+">";
					}



					mLogger.info("RLOS COMMON"+" after adding world_check+ "+xml_str);
					int_xml.put(parent_tag, xml_str);

				}

				else if(tag_name.equalsIgnoreCase("no_of_cheque_bounce_int_3mon_Ind")){
					mLogger.info("RLOS COMMON"+" iNSIDE no_of_cheque_bounce_int_3mon_Ind+ ");
					String squerynoc="SELECT count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-3,GETDATE()) and returntype='ICCS' and Child_Wi='"+formObject.getWFWorkitemName()+"'";
					List<List<String>> NOC=formObject.getDataFromDataSource(squerynoc);
					if (NOC!=null && !NOC.isEmpty()){
						String xml_str =  int_xml.get(parent_tag);
						xml_str = xml_str+ "<"+tag_name+">"+NOC.get(0).get(0)
								+"</"+ tag_name+">";

						mLogger.info("RLOS COMMON"+" after adding internal_blacklist+ "+xml_str);
						int_xml.put(parent_tag, xml_str);

					}

				}
				else if(tag_name.equalsIgnoreCase("no_of_DDS_return_int_3mon_Ind")){
					mLogger.info("RLOS COMMON"+" iNSIDE no_of_cheque_bounce_int_3mon_Ind+ ");
					String squerynoc="SELECT count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-3,GETDATE()) and returntype='DDS' and Child_Wi='"+formObject.getWFWorkitemName()+"'";
					List<List<String>> NOC=formObject.getDataFromDataSource(squerynoc);
					if (NOC!=null && !NOC.isEmpty()){
						String xml_str =  int_xml.get(parent_tag);
						xml_str = xml_str+ "<"+tag_name+">"+NOC.get(0).get(0)
								+"</"+ tag_name+">";

						mLogger.info("RLOS COMMON"+" after adding internal_blacklist+ "+xml_str);
						int_xml.put(parent_tag, xml_str);

					}

				}
				else if(tag_name.equalsIgnoreCase("blacklist_cust_type")||tag_name.equalsIgnoreCase("internal_blacklist")||
						tag_name.equalsIgnoreCase("internal_blacklist_date")||tag_name.equalsIgnoreCase("internal_blacklist_code")||
						tag_name.equalsIgnoreCase("negative_cust_type")||tag_name.equalsIgnoreCase("internal_negative_flag")||
						tag_name.equalsIgnoreCase("internal_negative_date")||tag_name.equalsIgnoreCase("internal_negative_code")){
					mLogger.info("RLOS COMMON"+" iNSIDE channelcode+ ");
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
					if(tag_name.equalsIgnoreCase("blacklist_cust_type") || tag_name.equalsIgnoreCase("negative_cust_type")){
						xml_str = xml_str+ "<"+tag_name+">I</"+ tag_name+">";
					}
					else if(tag_name.equalsIgnoreCase("internal_blacklist")){
						xml_str = xml_str+ "<"+tag_name+">"+internal_blacklist+"</"+ tag_name+">";
					}
					else if(tag_name.equalsIgnoreCase("internal_blacklist_date")){
						xml_str = xml_str+ "<"+tag_name+">"+internal_blacklist_date+"</"+ tag_name+">";
					}
					else if(tag_name.equalsIgnoreCase("internal_blacklist_code")){
						xml_str = xml_str+ "<"+tag_name+">"+internal_blacklist_code+"</"+ tag_name+">";
					}
					
					else if(tag_name.equalsIgnoreCase("internal_negative_flag")){
						xml_str = xml_str+ "<"+tag_name+">"+internal_negative_flag+"</"+ tag_name+">";
					}
					else if(tag_name.equalsIgnoreCase("internal_negative_date")){
						xml_str = xml_str+ "<"+tag_name+">"+internal_negative_date+"</"+ tag_name+">";
					}
					else if(tag_name.equalsIgnoreCase("internal_negative_code")){
						xml_str = xml_str+ "<"+tag_name+">"+internal_negative_code+"</"+ tag_name+">";
					}
					mLogger.info("RLOS COMMON"+" after adding internal_blacklist+ "+xml_str);
					int_xml.put(parent_tag, xml_str);


				}

				else if(tag_name.equalsIgnoreCase("external_blacklist_flag")||tag_name.equalsIgnoreCase("external_blacklist_date")||
						tag_name.equalsIgnoreCase("external_blacklist_code")){
					mLogger.info("RLOS COMMON"+" iNSIDE channelcode+ ");
					String ParentWI_Name = formObject.getNGValue("Parent_WIName");
					String squeryBlacklist="select BlacklistFlag,BlacklistDate,BlacklistReasonCode from ng_rlos_cif_detail where (cif_wi_name='"+ParentWI_Name+"' or cif_wi_name='"+formObject.getWFWorkitemName()+"') and cif_searchType = 'External'";
					mLogger.info("RLOS COMMON"+" iNSIDE channelcode+ "+squeryBlacklist);
					List<List<String>> Blacklist=formObject.getDataFromDataSource(squeryBlacklist);
					String External_blacklist_date =  "";
					String External_blacklist_code =  "";

					if (Blacklist!=null && !Blacklist.isEmpty()){		
						External_blacklist_date =  Blacklist.get(0).get(1);
						External_blacklist_code =  Blacklist.get(0).get(2);
					}
					String xml_str =  int_xml.get(parent_tag);
					if(tag_name.equalsIgnoreCase("external_blacklist_flag")){
						xml_str = xml_str+ "<"+tag_name+">I</"+ tag_name+">";
					}
					else if(tag_name.equalsIgnoreCase("external_blacklist_date")){
						xml_str = xml_str+ "<"+tag_name+">"+External_blacklist_date+"</"+ tag_name+">";
					}
					else if(tag_name.equalsIgnoreCase("external_blacklist_code")){
						xml_str = xml_str+ "<"+tag_name+">"+External_blacklist_code+"</"+ tag_name+">";
					}

					mLogger.info("RLOS COMMON"+" after adding internal_blacklist+ "+xml_str);
					int_xml.put(parent_tag, xml_str);


				}
				else if(tag_name.equalsIgnoreCase("ApplicationDetails")){
					mLogger.info("inside 1st if"+"inside DECTECH req1");

					mLogger.info("inside 1st if"+"inside customer update req2");
					String xml_str = int_xml.get(parent_tag);
					mLogger.info("RLOS COMMON"+" before adding product+ "+xml_str);
					xml_str = xml_str + getProduct_details();
					mLogger.info("RLOS COMMON"+" after adding product+ "+xml_str);
					int_xml.put(parent_tag, xml_str);

				}
				else if(tag_name.equalsIgnoreCase("cust_name") ){
					if(int_xml.containsKey(parent_tag))
					{
						String first_name=formObject.getNGValue("cmplx_Customer_FIrstNAme");
						String middle_name=formObject.getNGValue("cmplx_Customer_MiddleName");
						String last_name=formObject.getNGValue("cmplx_Customer_LAstNAme");

						String full_name=first_name+" "+middle_name+""+last_name;

						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<"+tag_name+">"+full_name
								+"</"+ tag_name+">";

						mLogger.info("RLOS COMMON"+" after adding confirmedinjob+ "+xml_str);
						int_xml.put(parent_tag, xml_str);

					}		                            	
				}

				else if(tag_name.equalsIgnoreCase("ref_phone_no") ){
					if(int_xml.containsKey(parent_tag))
					{
						mLogger.info("RLOS COMMON"+" INSIDE ref_phone_no+ ");
						int count=formObject.getLVWRowCount("cmplx_RefDetails_cmplx_Gr_ReferenceDetails");
						String ref_phone_no="";
						String ref_relationship="";
						mLogger.info("RLOS COMMON"+" INSIDE ref_phone_no+ "+count);
						if (count != 0){
							ref_phone_no=formObject.getNGValue("cmplx_RefDetails_cmplx_Gr_ReferenceDetails",0,4);
							ref_relationship=formObject.getNGValue("cmplx_RefDetails_cmplx_Gr_ReferenceDetails",0,2);
							mLogger.info("RLOS COMMON"+" after adding ref_phone_no+ "+ref_phone_no);
							mLogger.info("RLOS COMMON"+" after adding ref_relationship+ "+ref_relationship);
						}


						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<"+tag_name+">"+ref_phone_no
								+"</"+ tag_name+"><ref_relationship>"+ref_relationship+"</ref_relationship>";

						mLogger.info("RLOS COMMON"+" after adding confirmedinjob+ "+xml_str);
						int_xml.put(parent_tag, xml_str);

					}		                            	
				}

				else if(tag_name.equalsIgnoreCase("confirmed_in_job")){
					if(int_xml.containsKey(parent_tag))
					{
						String confirmedinjob=formObject.getNGValue("cmplx_EmploymentDetails_JobConfirmed");

						if (confirmedinjob!=null){
							if (confirmedinjob.equalsIgnoreCase("true")){
								confirmedinjob="Y";
							}
							else{
								confirmedinjob="N";
							}

							String xml_str = int_xml.get(parent_tag);
							xml_str = xml_str + "<"+tag_name+">"+confirmedinjob
									+"</"+ tag_name+">";

							mLogger.info("RLOS COMMON"+" after adding confirmedinjob+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}
					}		                            	
				}
				else if(tag_name.equalsIgnoreCase("included_pl_aloc")){
					if(int_xml.containsKey(parent_tag))
					{
						String included_pl_aloc=formObject.getNGValue("cmplx_EmploymentDetails_IncInPL");

						if (included_pl_aloc!=null){
							if (included_pl_aloc.equalsIgnoreCase("true")){
								included_pl_aloc="Y";
							}
							else{
								included_pl_aloc="N";
							}

							String xml_str = int_xml.get(parent_tag);
							xml_str = xml_str + "<"+tag_name+">"+included_pl_aloc
									+"</"+ tag_name+">";

							mLogger.info("RLOS COMMON"+" after adding included_pl_aloc+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}
					}
				}
				else if(tag_name.equalsIgnoreCase("included_cc_aloc")){
					if(int_xml.containsKey(parent_tag))
					{
						String included_cc_aloc=formObject.getNGValue("cmplx_EmploymentDetails_IncInCC");

						if (included_cc_aloc!=null){
							if (included_cc_aloc.equalsIgnoreCase("true")){
								included_cc_aloc="Y";
							}
							else{
								included_cc_aloc="N";
							}

							String xml_str = int_xml.get(parent_tag);
							xml_str = xml_str + "<"+tag_name+">"+included_cc_aloc
									+"</"+ tag_name+">";

							mLogger.info("RLOS COMMON"+" after adding cmplx_EmploymentDetails_IncInCC+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}	
					}
				}
				else if(tag_name.equalsIgnoreCase("vip_flag")){
					if(int_xml.containsKey(parent_tag))
					{
						String vip_flag=formObject.getNGValue("cmplx_Customer_VIPFlag");


						if (vip_flag.equalsIgnoreCase("true")){
							vip_flag="Y";
						}
						else{
							vip_flag="N";
						}

						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<"+tag_name+">"+vip_flag
								+"</"+ tag_name+">";

						mLogger.info("RLOS COMMON"+" after adding cmplx_Customer_VIPFlag+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}		                            	
				}
				else if(tag_name.equalsIgnoreCase("standing_instruction")){
					mLogger.info("RLOS COMMON"+" iNSIDE standing_instruction+ ");
					String squerynoc="SELECT count(*) FROM ng_rlos_FinancialSummary_SiDtls WHERE Child_wi='"+formObject.getWFWorkitemName()+"'";
					List<List<String>> NOC=formObject.getDataFromDataSource(squerynoc);
					mLogger.info("RLOS COMMON"+" after adding cmplx_Customer_VIPFlag+ "+squerynoc);
					String standing_instruction;
					standing_instruction=NOC.get(0).get(0);
					if (NOC!=null && !NOC.isEmpty()){
						String xml_str =  int_xml.get(parent_tag);
						if (standing_instruction.equalsIgnoreCase("0")){
							standing_instruction="N";
						}
						else{
							standing_instruction="Y";
						}

						xml_str = xml_str+ "<"+tag_name+">"+standing_instruction
								+"</"+ tag_name+">";

						mLogger.info("RLOS COMMON"+" after adding standing_instruction+ "+xml_str);
						int_xml.put(parent_tag, xml_str);

					}

				}
				else if(tag_name.equalsIgnoreCase("accomodation_provided")){
					if(int_xml.containsKey(parent_tag))
					{
						String accomodation_provided=formObject.getNGValue("cmplx_IncomeDetails_Accomodation");

						if (accomodation_provided!=null){
							if (accomodation_provided.equalsIgnoreCase("Yes")){
								accomodation_provided="Y";
							}
							else{
								accomodation_provided="N";
							}

							String xml_str = int_xml.get(parent_tag);
							xml_str = xml_str + "<"+tag_name+">"+accomodation_provided
									+"</"+ tag_name+">";

							mLogger.info("RLOS COMMON"+" after adding confirmedinjob+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}	
					}
				}
				else if(tag_name.equalsIgnoreCase("AccountDetails")){
					if(int_xml.containsKey(parent_tag))
					{
						String xml_str = int_xml.get(parent_tag);
						mLogger.info("RLOS COMMON"+" before adding internal liability+ "+xml_str);
						xml_str = xml_str + getInternalLiabDetails();
						mLogger.info("RLOS COMMON"+" after internal liability+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}

				}
				else if(tag_name.equalsIgnoreCase("InternalBureau") ){

					String xml_str = int_xml.get(parent_tag);
					mLogger.info("RLOS COMMON"+" before adding InternalBureauData+ "+xml_str);
					String temp = InternalBureauData();
					if(!temp.equalsIgnoreCase("")){
						if (xml_str==null){
							mLogger.info("RLOS COMMON"+" before adding bhrabc"+xml_str);
							xml_str="";
						}
						xml_str =  xml_str+ temp;
						mLogger.info("RLOS COMMON"+" after InternalBureauData+ "+xml_str);
						int_xml.get(parent_tag);
						int_xml.put(parent_tag, xml_str);
					}


				}
				else if(tag_name.equalsIgnoreCase("InternalBouncedCheques") ){

					String xml_str = int_xml.get(parent_tag);
					mLogger.info("RLOS COMMON"+" before adding InternalBouncedCheques+ "+xml_str);
					String temp = InternalBouncedCheques();
					if(!temp.equalsIgnoreCase("")){
						if (xml_str==null){
							mLogger.info("RLOS COMMON"+" before adding bhrabc"+xml_str);
							xml_str="";
						}
						xml_str =  xml_str+ temp;
						mLogger.info("RLOS COMMON"+" after InternalBouncedCheques+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}

				}
				else if(tag_name.equalsIgnoreCase("InternalBureauIndividualProducts")){

					String xml_str = int_xml.get(parent_tag);
					mLogger.info("RLOS COMMON"+" before adding InternalBureauIndividualProducts+ "+xml_str);
					String temp = InternalBureauIndividualProducts();
					if(!temp.equalsIgnoreCase("")){
						if (xml_str==null){
							mLogger.info("RLOS COMMON"+" before adding bhrabc"+xml_str);
							xml_str="";
						}
						xml_str =  xml_str+ temp;
						mLogger.info("RLOS COMMON"+" after InternalBureauIndividualProducts+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}

				}
				else if(tag_name.equalsIgnoreCase("InternalBureauPipelineProducts")){

					String xml_str = int_xml.get(parent_tag);
					mLogger.info("RLOS COMMON"+" before adding InternalBureauPipelineProducts+ "+xml_str);
					String temp = InternalBureauPipelineProducts();
					if(!temp.equalsIgnoreCase("")){
						if (xml_str==null){
							mLogger.info("RLOS COMMON"+" before adding bhrabc"+xml_str);
							xml_str="";
						}
						xml_str =  xml_str+ temp;
						mLogger.info("RLOS COMMON"+" after InternalBureauPipelineProducts+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}

				}
				else if(tag_name.equalsIgnoreCase("ExternalBureau") ){

					String xml_str = int_xml.get(parent_tag);
					mLogger.info("RLOS COMMON"+" before adding ExternalBureau+ "+xml_str);
					String temp = ExternalBureauData();
					if(!temp.equalsIgnoreCase("")){
						if (xml_str==null){
							mLogger.info("RLOS COMMON"+" before adding bhrabc"+xml_str);
							xml_str="";
						}
						xml_str =  xml_str+ temp;
						mLogger.info("RLOS COMMON"+" after ExternalBureau+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}

				}
				else if(tag_name.equalsIgnoreCase("ExternalBouncedCheques")){

					String xml_str = int_xml.get(parent_tag);
					mLogger.info("RLOS COMMON"+" before adding ExternalBouncedCheques+ "+xml_str);
					String temp = ExternalBouncedCheques();
					if(!temp.equalsIgnoreCase("")){
						if (xml_str==null){
							mLogger.info("RLOS COMMON"+" before adding bhrabc"+xml_str);
							xml_str="";
						}
						xml_str =  xml_str+ temp;
						mLogger.info("RLOS COMMON"+" after ExternalBouncedCheques+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}                    	
				}
				else if(tag_name.equalsIgnoreCase("ExternalBureauIndividualProducts")){

					String xml_str = int_xml.get(parent_tag);
					mLogger.info("RLOS COMMON"+" before adding ExternalBureauIndividualProducts+ "+xml_str);
					String temp =  ExternalBureauIndividualProducts();
					mLogger.info("RLOS COMMON"+" value of temp to be adding temp+ "+temp);
					String Manual_add_Liab =  ExternalBureauManualAddIndividualProducts();

					if((!temp.equalsIgnoreCase("")) || (!Manual_add_Liab.equalsIgnoreCase(""))){
						if (xml_str==null){
							mLogger.info("RLOS COMMON"+" before adding bhrabc"+xml_str);
							xml_str="";
						}
						xml_str =  xml_str + temp + Manual_add_Liab;
						mLogger.info("RLOS COMMON"+" after ExternalBureauIndividualProducts+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}	                            	
				}
				else if(tag_name.equalsIgnoreCase("ExternalBureauPipelineProducts")){

					String xml_str = int_xml.get(parent_tag);
					mLogger.info("RLOS COMMON"+" before adding ExternalBureauPipelineProducts+ "+xml_str);
					String temp =  ExternalBureauPipelineProducts();
					if(!temp.equalsIgnoreCase("")){
						if (xml_str==null){
							mLogger.info("RLOS COMMON"+" before adding bhrabc"+xml_str);
							xml_str="";
						}
						xml_str =  xml_str+ temp;
						mLogger.info("RLOS COMMON"+" after ExternalBureauPipelineProducts+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}                        	
				}
				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
				}

			}
		}
		catch(Exception e){
			mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + ");
			PLCommon.printException(e);
		}

		return int_xml;
	}

	private Map<String, String> NEW_LOAN_REQ_Custom( List<List<String>> OutputXML,FormReference formObject,String callName) {
		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();

		try{
			for (List<String> mylist : OutputXML) {
				for (int i = 0; i < 8; i++) {
					mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}

				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");
				if(tag_name.equalsIgnoreCase("ApplicationID") ){
					mLogger.info("inside 1st if"+"inside customer update req1");
					String xml_str = int_xml.get(parent_tag);
					xml_str = "<"+tag_name+">"+formObject.getWFWorkitemName().substring(5,14)+"</"+ tag_name+">";

					mLogger.info("PL COMMON"+" after adding ApplicationID:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}
				else if(tag_name.equalsIgnoreCase("SourcingDate")){
					mLogger.info("inside 1st if"+"inside customer update req1");
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<SourcingDate>2015-04-15</SourcingDate>";

					mLogger.info("PL COMMON"+" SourcingDate: "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}
				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
				}
			}
		}
		catch(Exception e){
			mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + ");
			PLCommon.printException(e);
		}

		return int_xml;
	}

	private Map<String, String> CARD_NOTIFICATION_Custom( List<List<String>> OutputXML,FormReference formObject,String callName) {
		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();

		try{
			for (List<String> mylist : OutputXML) {
				for (int i = 0; i < 8; i++) {
					mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}

				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");
				if(tag_name.equalsIgnoreCase("ApplicationNumber") ){
					mLogger.info("inside 1st if"+"inside customer update req1");
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str+"<"+tag_name+">"+formObject.getWFWorkitemName().substring(5,14)+"</"+ tag_name+">";

					mLogger.info("PL COMMON"+" after adding ApplicationNumber:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}
				else if(tag_name.equalsIgnoreCase("ApplicationDate")){
					mLogger.info("inside 1st if"+"inside customer update req1");
					String xml_str = int_xml.get(parent_tag);
					xml_str = "<"+tag_name+">"+"2017-06-06"+"</"+ tag_name+">";

					mLogger.info("PL COMMON"+" after adding ApplicationDate:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}
				else if(tag_name.equalsIgnoreCase("VIPFlag")){
					mLogger.info("inside 1st if"+"inside customer update req1");

					String vip_flag="N";
					if(formObject.getNGValue("cmplx_Customer_VIPFlag").equalsIgnoreCase("true")){
						vip_flag="Y";
					}	          	

					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<"+tag_name+">"+vip_flag+"</"+ tag_name+">";

					mLogger.info("RLOS COMMON"+" after adding VIP flag+ "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}
				else if(tag_name.equalsIgnoreCase("PhnDet")){
					mLogger.info("inside 1st if"+"inside customer update req1");
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<PhnDet><PhoneType>OFFCPH1</PhoneType><PhnCountryCode>00971</PhnCountryCode><CityCode></CityCode><PhnLocalCode>00971</PhnLocalCode><PhoneNumber>"+formObject.getNGValue("cmplx_Customer_MobNo")+"</PhoneNumber><ExtensionNumber></ExtensionNumber><PhnPrefFlag>N</PhnPrefFlag></PhnDet>";

					mLogger.info("PL COMMON"+" SourcingDate: "+xml_str);
					int_xml.put(parent_tag, xml_str);                                    
				}
				
				else if(tag_name.equalsIgnoreCase("DSAId")){
					mLogger.info("inside 1st if"+"inside customer update req1");
					String xml_str = int_xml.get(parent_tag);
					xml_str =xml_str+ "<"+tag_name+">"+"99D1243"+"</"+ tag_name+">";

					mLogger.info("PL COMMON"+" after adding DSAId:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}
				else if(tag_name.equalsIgnoreCase("AddrDet")){
					mLogger.info("inside 1st if"+"inside customer update req1");
					if(int_xml.containsKey(parent_tag))
					{
						mLogger.info("inside 1st if"+"inside customer update req2");
						String xml_str = int_xml.get(parent_tag);
						mLogger.info("RLOS COMMON"+" before adding address+ "+xml_str);
						xml_str = xml_str + getCustAddress_details(callName);
						mLogger.info("RLOS COMMON"+" after adding address+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}		                            	
				}
				else if(tag_name.equalsIgnoreCase("LienDetails") ){
					mLogger.info("inside 1st if"+"inside customer update req1");
					if(int_xml.containsKey(parent_tag))
					{
						mLogger.info("inside 1st if"+"inside customer update req2");
						String xml_str = int_xml.get(parent_tag);
						mLogger.info("RLOS COMMON"+" before adding address+ "+xml_str);
						xml_str = xml_str + getLien_details(callName);
						mLogger.info("RLOS COMMON"+" after adding address+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}		                            	
				}
				else if(tag_name.equalsIgnoreCase("ContactDetails")){
					mLogger.info("inside 1st if"+"inside customer update req1");
					if(int_xml.containsKey(parent_tag))
					{
						mLogger.info("inside 1st if"+"inside customer update req2");
						String xml_str = int_xml.get(parent_tag);
						mLogger.info("RLOS COMMON"+" before contact details+ "+xml_str);
						xml_str = xml_str + getcontact_details();
						mLogger.info("RLOS COMMON"+" after adding contact details+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}		                            	
				}
				else if(tag_name.equalsIgnoreCase("SalaryDate") ){
					mLogger.info("inside 1st if"+"inside customer update req1");
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

					mLogger.info("PL COMMON"+" after adding ApplicationID:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}
				else if(tag_name.equalsIgnoreCase("MonthlyCrTrnOvrAmt")){
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<"+tag_name+">"+"1000"
							+"</"+ tag_name+">";

					mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else if(tag_name.equalsIgnoreCase("CardNumber")){
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<"+tag_name+">"+"5239266299926203"
							+"</"+ tag_name+">";

					mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else if(tag_name.equalsIgnoreCase("CardProductType")){
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<"+tag_name+">"+"KALYAN-SEC"
							+"</"+ tag_name+">";

					mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else if(tag_name.equalsIgnoreCase("DispatchMode")){
					String xml_str = int_xml.get(parent_tag);
					xml_str = "<"+tag_name+">"+"ByCourier"
							+"</"+ tag_name+">";	          	
					mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else if((tag_name.equalsIgnoreCase("MinorFlag") && parent_tag.equalsIgnoreCase("PersonDetails"))|| callName.equalsIgnoreCase("CARD_NOTIFICATION")){
					if(int_xml.containsKey(parent_tag))
					{
						int Age = Integer.parseInt(formObject.getNGValue("cmplx_Customer_age"));
						String age_flag = "N";
						if(Age<18)
							age_flag="Y";
						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<"+tag_name+">"+age_flag+"</"+ tag_name+">";

						mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}		                            	
				}
				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
				}
			}
		}
		catch(Exception e){
			mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + ");
			PLCommon.printException(e);
		}

		return int_xml;
	}
	private Map<String, String> CARD_SERVICES_Custom( List<List<String>> OutputXML,FormReference formObject,String callName) {
		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();

		try{
			for (List<String> mylist : OutputXML) {
				for (int i = 0; i < 8; i++) {
					mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}

				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");
				if(tag_name.equalsIgnoreCase("LienDetails")){
					mLogger.info("inside 1st if"+"inside customer update req1");
					if(int_xml.containsKey(parent_tag))
					{
						mLogger.info("inside 1st if"+"inside customer update req2");
						String xml_str = int_xml.get(parent_tag);
						mLogger.info("RLOS COMMON"+" before adding address+ "+xml_str);
						xml_str = xml_str + getLien_details(callName);
						mLogger.info("RLOS COMMON"+" after adding address+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}		                            	
				}
				else if(tag_name.equalsIgnoreCase("SalaryDate")){
					mLogger.info("inside 1st if"+"inside customer update req1");
					String xml_str = int_xml.get(parent_tag);
					Calendar now = Calendar.getInstance();
					String month = "";
					String day = "";
					if((now.get(Calendar.MONTH) - 1)<10){
						month = "0"+(now.get(Calendar.MONTH) - 1);
					}else{
						month = ""+(now.get(Calendar.MONTH) - 1);
					}
					if(formObject.getNGValue("cmplx_IncomeDetails_SalaryDay").length()<2){
						day = "0" + formObject.getNGValue("cmplx_IncomeDetails_SalaryDay");
					}else{
						day = formObject.getNGValue("cmplx_IncomeDetails_SalaryDay");
					}


					String Current_date="";

					Current_date = now.get(Calendar.YEAR)+"-"+month+"-"+day;
					xml_str = xml_str+"<"+tag_name+">"+Current_date+"</"+ tag_name+">";

					mLogger.info("PL COMMON"+" after adding ApplicationID:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}
				else if(tag_name.equalsIgnoreCase("ProcessDate")){
					String xml_str = int_xml.get(parent_tag);
					SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
					xml_str = xml_str + "<"+tag_name+">"+sdf1.format(new Date())+"</"+ tag_name+">";

					mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else if(tag_name.equalsIgnoreCase("ProcessedBy")){
					String xml_str = int_xml.get(parent_tag);

					xml_str = xml_str + "<"+tag_name+">"+formObject.getUserName()+"</"+ tag_name+">";

					mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
				}
			}
		}
		catch(Exception e){
			mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + ");
			PLCommon.printException(e);
		}

		return int_xml;
	}
	private Map<String, String> CHEQUE_BOOK_ELIGIBILITY_Custom( List<List<String>> OutputXML,FormReference formObject,String callName) {
		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();

		try{
			for (List<String> mylist : OutputXML) {
				for (int i = 0; i < 8; i++) {
					mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}

				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");
				if (tag_name.equalsIgnoreCase("RecipientAddress")) {
					int add_len = formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
					String add_res_val = "";
					String xml_str = int_xml.get(parent_tag);
					if (add_len > 0) {
						for (int i = 0; i < add_len; i++) {
							mLogger.info("selecting Emirates of residence: "+formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid"+i+ 0));
							if (formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0).equalsIgnoreCase("Home"))
							{
							formObject.setNGValue("cmplx_Customer_EmirateOfResidence",formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i, 6));
							add_res_val = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 1)+ " "+formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i, 2)+ formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i, 3)+ formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i, 4)+ formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i, 5)+ formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i, 6);
							}
						}
						xml_str = xml_str + "<" + tag_name + ">" + add_res_val + "</"+ tag_name + ">";

						mLogger.info("CC Integration "+ " after adding res_flag+ "+ xml_str);
						int_xml.put(parent_tag, xml_str);
					}
				}
				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
				}
			}
		}
		catch(Exception e){
			mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + ");
			PLCommon.printException(e);
		}

		return int_xml;
	}
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

		if (parent_tag != null && !parent_tag.equalsIgnoreCase("")) {

			mLogger.info("inside 1st if"+"inside 1st if");
			if(int_xml.containsKey(parent_tag))
			{
				mLogger.info("inside 1st if"+"inside 2nd if");
				String xml_str = int_xml.get(parent_tag);
				mLogger.info("inside 1st if"+"inside 2nd if xml string"+xml_str);
				if(is_repetitive.equalsIgnoreCase("Y") && int_xml.containsKey(tag_name)){
					mLogger.info("inside 1st if"+"inside 3rd if xml string");
					xml_str = int_xml.get(tag_name)+ "</"+tag_name+">" +"<"+ tag_name+">";
					mLogger.info("inside 1st if"+"inside 3rd if xml string xml string"+xml_str);
					int_xml.remove(tag_name);
					int_xml.put(tag_name, xml_str);
					mLogger.info("inside 1st if"+"inside 3rd if xml string xml string int_xml");
				}
				else{
					mLogger.info("inside else of parent tag"+"value after adding "+ Call_name+": "+xml_str+" form_control name: "+form_control);
					mLogger.info(""+"valuie of form control: "+formObject.getNGValue(form_control));
					if(form_control.trim().equalsIgnoreCase("") && default_val.trim().equalsIgnoreCase("")){
						mLogger.info("inside if added by me"+"inside");
						xml_str = xml_str + "<"+tag_name+">"+"</"+ tag_name+">";
						mLogger.info("added by xml"+"xml_str"+xml_str);
					}
					else if (!(formObject.getNGValue(form_control)==null || formObject.getNGValue(form_control).trim().equalsIgnoreCase("")||  formObject.getNGValue(form_control).equalsIgnoreCase("null")))
					{
						mLogger.info("inside else of parent tag 1"+"form_control_val"+ form_control_val);
						if(fin_call_name.toUpperCase().contains(callName.toUpperCase())){
							form_control_val = formObject.getNGValue(form_control).toUpperCase();
						}
						else
							form_control_val = formObject.getNGValue(form_control);

						if(!data_format12.equalsIgnoreCase("text")){
							String[] format_arr = data_format12.split(":");
							String format_name = format_arr[0];
							String format_type = format_arr[1];
							mLogger.info(""+"format_name"+format_name);
							mLogger.info(""+"format_type"+format_type);

							if(format_name.equalsIgnoreCase("date")){
								DateFormat df = new SimpleDateFormat("dd/MM/yyyy"); 
								DateFormat df_new = new SimpleDateFormat(format_type);

								try {
									startDate = df.parse(form_control_val);
									form_control_val = df_new.format(startDate);
									mLogger.info("RLOSCommon#Create Input"+" date conversion: final Output: "+form_control_val+ " requested format: "+format_type);

								} catch (ParseException e) {
									mLogger.info("RLOSCommon#Create Input"+" Error while format conversion: "+e.getMessage());
									PLCommon.printException(e);
								}
								catch (Exception e) {
									mLogger.info("RLOSCommon#Create Input"+" Error while format conversion: "+e.getMessage());
									PLCommon.printException(e);
								}
							}
							else if(format_name.equalsIgnoreCase("number")){
								if(form_control_val.contains(",")){
									form_control_val = form_control_val.replace(",", "");
								}

							}
							//change here for other input format

						}
						mLogger.info("inside else of parent tag"+"form_control_val"+ form_control_val);
						xml_str = xml_str + "<"+tag_name+">"+form_control_val
								+"</"+ tag_name+">";
						mLogger.info("inside else of parent tag xml_str"+"xml_str"+ xml_str);
					}

					else if(default_val==null || default_val.trim().equalsIgnoreCase("")){
						mLogger.info("#RLOS Common GenerateXML IF part"+"no value found for tag name: "+ tag_name);
					}
					else{
						mLogger.info("#RLOS Common GenerateXML inside set default value"+"");

						form_control_val = default_val;

						mLogger.info("#RLOS Common GenerateXML inside set default value"+"form_control_val"+ form_control_val);
						xml_str = xml_str + "<"+tag_name+">"+form_control_val
								+"</"+ tag_name+">";
						mLogger.info("#RLOS Common GenerateXML inside else of parent tag form_control_val xml_str1"+"xml_str"+ xml_str);

					}
					//code change for to remove docdect incase ref no is not present start	                                       
					if(tag_name.equalsIgnoreCase("DocumentRefNumber") && parent_tag.equalsIgnoreCase("Document") && form_control_val.trim().equalsIgnoreCase("")){
						if(xml_str.contains("</Document>")){
							xml_str = xml_str.substring(0, xml_str.lastIndexOf("</Document>"));
							int_xml.put(parent_tag, xml_str);
						}
						else
							int_xml.remove(parent_tag);
					}
					else if(tag_name.equalsIgnoreCase("DocRefNum") && parent_tag.equalsIgnoreCase("DocDetails") && form_control_val.equalsIgnoreCase("")){
						if(xml_str.contains("</DocDetails>")){
							xml_str = xml_str.substring(0, xml_str.lastIndexOf("</DocDetails>"));
							int_xml.put(parent_tag, xml_str);
						}
						else
							int_xml.remove(parent_tag);
					}
					else if(tag_name.equalsIgnoreCase("DocNo") && parent_tag.equalsIgnoreCase("DocDet") && form_control_val.equalsIgnoreCase("")){
						if(xml_str.contains("</DocDet>")){
							xml_str = xml_str.substring(0, xml_str.lastIndexOf("</DocDet>"));
							int_xml.put(parent_tag, xml_str);
						}
						else
							int_xml.remove(parent_tag);
					}
					/*else if(tag_name.equalsIgnoreCase("PhnLocalCode") && parent_tag.equalsIgnoreCase("PhnDetails") && form_control_val.equalsIgnoreCase("")){
						if(xml_str.contains("</PhnDetails>")){
							xml_str = xml_str.substring(0, xml_str.lastIndexOf("</PhnDetails>"));
							int_xml.put(parent_tag, xml_str);
						}
						else
							int_xml.remove(parent_tag);
					}*/

					else if(tag_name.equalsIgnoreCase("PhnLocalCode") && parent_tag.equalsIgnoreCase("PhnDetails") && form_control_val.equalsIgnoreCase("")){
						if(xml_str.contains("</PhnDetails>")){
							mLogger.info("PL_common"+ "Inside PhnDetails condition to remove RVC tag </PhnDetails> contanied");
							xml_str = xml_str.substring(0, xml_str.lastIndexOf("</PhnDetails>"));
							int_xml.put(parent_tag, xml_str);
						}
						else{
							mLogger.info("PL_common"+ "Inside PhnDetails condition to remove PhnDetails tag <PhnDetails> tag not contanied");
							int_xml.remove(parent_tag);
							Iterator<Map.Entry<String,String>> itr = int_xml.entrySet().iterator();
							mLogger.info("itr of hashmap"+"itr"+itr);
							while (itr.hasNext())
							{
								Map.Entry<String, String> entry =  itr.next();
								mLogger.info("entry of hashmap"+"entry"+entry+ " entry.getValue(): "+ entry.getValue());
								if(entry.getValue().contains("PhnDetails")){
									String key_val = entry.getValue();
									key_val = key_val.replace("<PhnDetails>", "");
									key_val = key_val.replace("</PhnDetails>", "");
									int_xml.put(entry.getKey(), key_val);
									mLogger.info("PL_common"+"KEY: entry.getKey()" + " Updated value: "+key_val);
									//final_xml = final_xml.insert(final_xml.indexOf("<"+entry.getKey()+">")+entry.getKey().length()+2, entry.getValue());
									mLogger.info("PL_common "+"PhnDetails removed from parent key");
									break;
								}

							} 
						}
					}
					//for email for CIF update

					else if(tag_name.equalsIgnoreCase("Email") && parent_tag.equalsIgnoreCase("EmlDet") && form_control_val.equalsIgnoreCase("")){
						if(xml_str.contains("</EmlDet>")){
							mLogger.info("PL_common"+ "Inside EmlDet condition to remove RVC tag </EmlDet> contanied");
							xml_str = xml_str.substring(0, xml_str.lastIndexOf("</EmlDet>"));
							int_xml.put(parent_tag, xml_str);
						}
						else{
							mLogger.info("PL_common"+ "Inside EmlDet condition to remove PhnDetails tag <EmlDet> tag not contanied");
							int_xml.remove(parent_tag);
							Iterator<Map.Entry<String,String>> itr = int_xml.entrySet().iterator();
							mLogger.info("itr of hashmap"+"itr"+itr);
							while (itr.hasNext())
							{
								Map.Entry<String, String> entry =  itr.next();
								mLogger.info("entry of hashmap"+"entry"+entry+ " entry.getValue(): "+ entry.getValue());
								if(entry.getValue().contains("EmlDet")){
									String key_val = entry.getValue();
									key_val = key_val.replace("<EmlDet>", "");
									key_val = key_val.replace("</EmlDet>", "");
									int_xml.put(entry.getKey(), key_val);
									mLogger.info("PL_common"+"KEY: entry.getKey()" + " Updated value: "+key_val);
									//final_xml = final_xml.insert(final_xml.indexOf("<"+entry.getKey()+">")+entry.getKey().length()+2, entry.getValue());
									mLogger.info("PL_common "+"EmlDet removed from parent key");
									break;
								}

							} 
						}
					}

					
					else if(tag_name.equalsIgnoreCase("IncomeAmount") && parent_tag.equalsIgnoreCase("OtherIncomeDetails") && form_control_val.equalsIgnoreCase("")){
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
					//int_xml.put(parent_tag, xml_str);
					mLogger.info("else of generatexml"+"RLOSCommon"+"inside else"+xml_str);

				}

			}
			else{
				String new_xml_str ="";
				mLogger.info("inside else of parent tag main 2"+"value after adding "+ Call_name+": "+new_xml_str+" form_control name: "+form_control);
				mLogger.info(""+"valuie of form control: "+formObject.getNGValue(form_control));
				if (!(formObject.getNGValue(form_control)==null || formObject.getNGValue(form_control).trim().equalsIgnoreCase("")||  formObject.getNGValue(form_control).equalsIgnoreCase("null"))){
					mLogger.info("inside else of parent tag 1"+"form_control_val"+ form_control_val);
					if(fin_call_name.toUpperCase().contains(callName.toUpperCase())){
						form_control_val = formObject.getNGValue(form_control).toUpperCase();
					}
					else
						form_control_val = formObject.getNGValue(form_control);
					if(!data_format12.equalsIgnoreCase("text")){
						String[] format_arr = data_format12.split(":");
						String format_name = format_arr[0];
						String format_type = format_arr[1];
						if(format_name.equalsIgnoreCase("date")){
							DateFormat df = new SimpleDateFormat("MM/dd/yyyy"); 
							DateFormat df_new = new SimpleDateFormat(format_type);
							// java.util.Date startDate;
							try {
								startDate = df.parse(form_control_val);
								form_control_val = df_new.format(startDate);
								mLogger.info("RLOSCommon#Create Input"+" date conversion: final Output: "+form_control_val+ " requested format: "+format_type);

							} catch (ParseException e) {
								mLogger.info("RLOSCommon#Create Input"+" Error while format conversion: "+e.getMessage());
								PLCommon.printException(e);
							}
							catch (Exception e) {
								mLogger.info("RLOSCommon#Create Input"+" Error while format conversion: "+e.getMessage());
								PLCommon.printException(e);
							}
						}
						else if(format_name.equalsIgnoreCase("number")){
							if(form_control_val.contains(",")){
								form_control_val = form_control_val.replace(",", "");
							}

						}
						//change here for other input format

					}
					mLogger.info("inside else of parent tag"+"form_control_val"+ form_control_val);
					new_xml_str = new_xml_str + "<"+tag_name+">"+form_control_val
							+"</"+ tag_name+">";
					mLogger.info("inside else of parent tag xml_str"+"new_xml_str"+ new_xml_str);
				}

				else if(default_val==null || default_val.trim().equalsIgnoreCase("")){
					if(int_xml.containsKey(parent_tag)|| is_repetitive.equalsIgnoreCase("Y")){
						new_xml_str = new_xml_str + "<"+tag_name+">"+"</"+ tag_name+">";
					}
					mLogger.info("#RLOS Common GenerateXML Inside Else Part"+"no value found for tag name: "+ tag_name);
				}
				else{
					mLogger.info("#RLOS Common GenerateXML inside set default value"+"");
					form_control_val = default_val;
					mLogger.info("#RLOS Common GenerateXML inside set default value"+"form_control_val"+ form_control_val);
					new_xml_str = new_xml_str + "<"+tag_name+">"+form_control_val
							+"</"+ tag_name+">";
					mLogger.info("#RLOS Common GenerateXML inside else of parent tag form_control_val xml_str1"+"xml_str"+ new_xml_str);

				}
				int_xml.put(parent_tag, new_xml_str);
				mLogger.info("else of generatexml"+"RLOSCommon"+"inside else"+new_xml_str);

			}

		}
		return int_xml;
	}
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
			mLogger.info("$$outputgGridtXML "+"cabinetName " + cabinetName);
			wi_name = FormContext.getCurrentInstance().getFormConfig().getConfigElement("ProcessInstanceId");
			ws_name = FormContext.getCurrentInstance().getFormConfig().getConfigElement("ActivityName");
			mLogger.info("$$outputgGridtXML "+"ActivityName " + ws_name);
			sessionID = FormContext.getCurrentInstance().getFormConfig().getConfigElement("DMSSessionId");
			userName = FormContext.getCurrentInstance().getFormConfig().getConfigElement("UserName");
			mLogger.info("$$outputgGridtXML "+ "userName "+ userName);
			mLogger.info("$$outputgGridtXML "+ "sessionID "+ sessionID);

			String sMQuery = "SELECT SocketServerIP,SocketServerPort FROM NG_RLOS_MQ_TABLE with (nolock)";
			List<List<String>> outputMQXML = formObject.getDataFromDataSource(sMQuery);
			mLogger.info("$$outputgGridtXML "+ "sMQuery " + sMQuery);
			if (!outputMQXML.isEmpty()) {
				mLogger.info("$$outputgGridtXML "+ outputMQXML.get(0).get(0) + "+" + outputMQXML.get(0).get(1));
				socketServerIP = outputMQXML.get(0).get(0);
				mLogger.info("$$outputgGridtXML "+ "socketServerIP " + socketServerIP);
				socketServerPort = Integer.parseInt(outputMQXML.get(0).get(1));
				mLogger.info("$$outputgGridtXML "+ "socketServerPort " + socketServerPort);
				if (!(socketServerIP.equalsIgnoreCase("")  && socketServerPort==0)) {
					socket = new Socket(socketServerIP, socketServerPort);
					out = socket.getOutputStream();
					socketInputStream = socket.getInputStream();
					dout = new DataOutputStream(out);
					din = new DataInputStream(socketInputStream);
					mqOutputResponse = "";
					mqInputRequest = getMQInputXML(sessionID, cabinetName,wi_name, ws_name, userName, finalXml);
					mLogger.info("$$outputgGridtXML "+"mqInputRequest " + mqInputRequest);

					if (mqInputRequest != null && mqInputRequest.length() > 0) {
						int outPut_len = mqInputRequest.getBytes("UTF-16LE").length;
					mLogger.info("Final XML output len: "+outPut_len + "");
					mqInputRequest = outPut_len + "##8##;" + mqInputRequest;
					mLogger.info("MqInputRequest"+"Input Request Bytes : "+ mqInputRequest.getBytes("UTF-16LE"));
					dout.write(mqInputRequest.getBytes("UTF-16LE"));
					dout.flush();
					}
					byte[] readBuffer = new byte[50000];
					int num = din.read(readBuffer);
					boolean wait_flag = true;
					int out_len = 0;

					if (num > 0) {
						while (wait_flag) {	
						mLogger.info("MqOutputRequest"+ "num :"+ num);
						byte[] arrayBytes = new byte[num];
						System.arraycopy(readBuffer, 0, arrayBytes, 0, num);
						mqOutputResponse = mqOutputResponse+ new String(arrayBytes, "UTF-16LE");
						mLogger.info("MqOutputRequest"+"inside loop output Response :\n"+ mqOutputResponse);
						if (mqOutputResponse.contains("##8##;")) {
							String[] mqOutputResponse_arr = mqOutputResponse.split("##8##;");
							mqOutputResponse = mqOutputResponse_arr[1];
							out_len = Integer.parseInt(mqOutputResponse_arr[0]);
							mLogger.info("MqOutputRequest"+"First Output Response :\n"+ mqOutputResponse);
							mLogger.info("MqOutputRequest"+"Output length :\n" + out_len);
						}	
						if (out_len <= mqOutputResponse.getBytes("UTF-16LE").length) {	
							wait_flag = false;
						}
						Thread.sleep(100);	num = din.read(readBuffer);
					}// Aman Code added for dectech to replace the &lt and// &gt start 13 sept 2017if (mqOutputResponse.contains("&lt;")) {	SKLogger_CC.writeLog("MqOutputRequest","inside for Dectech :\n"+ mqOutputResponse);	mqOutputResponse = mqOutputResponse.replaceAll("&lt;", "<");	SKLogger_CC.writeLog("MqOutputRequest","after replacing lt :\n"+ mqOutputResponse);	mqOutputResponse = mqOutputResponse.replaceAll("&gt;", ">");	SKLogger_CC.writeLog("MqOutputRequest","after replacing gt :\n"+ mqOutputResponse);}// Aman Code added for dectech to replace the &lt and// &gt end 13 sept 2017
					mLogger.info("MqOutputRequest"+"Final Output Response :\n" + mqOutputResponse);
					socket.close();return mqOutputResponse;
					}

				} else {
					mLogger.info("SocketServerIp and SocketServerPort is not maintained ");
					mLogger.info("SocketServerIp is not maintained "+	socketServerIP);
					mLogger.info(" SocketServerPort is not maintained "+	socketServerPort);
					return "MQ details not maintained";
				}
			} else {
				mLogger.info("SOcket details are not maintained in NG_RLOS_MQ_TABLE table"+"");
				return "MQ details not maintained";
			}
			return "";

		} catch (Exception e) {
			return "";
		}
		finally{
			try{
				socket.close();
			}catch(Exception e)
			{
				mLogger.info("Exception occurred while closing socket");
				PLCommon.printException(e);
			}
		}
	}


}
