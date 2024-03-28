
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.newgen.custom.Common_Utils;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.Column;
import com.newgen.omniforms.component.IRepeater;
import com.newgen.omniforms.component.ListView;
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
			List<List<String>> DB_header=formObject.getDataFromDataSource(sQuery_header);
			PersonalLoanS.mLogger.info("Inside ELigibiltyAndProductInfo_ButtonAman2"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
			
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

				//temp change by saurabh on 7th Feb.
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
						else if ("NEW_PersonalLoanS_REQ".equalsIgnoreCase(callName)) {
							int_xml = NEW_PersonalLoanS_Custom(DB_List,formObject,callName);
						}
						else if ("NEW_CREDITCARD_REQ".equalsIgnoreCase(callName)) {
							int_xml = NEW_CREDITCARD_Custom(DB_List,formObject,callName);
						}
	//added by akshay on 5/3/18 for drop 4
					else if("CIF_UNLOCK".equalsIgnoreCase(Operation_name) || "CIF_LOCK".equalsIgnoreCase(Operation_name) || "CIF_VERIFY".equalsIgnoreCase(Operation_name) || "CIF_ENQUIRY".equalsIgnoreCase(Operation_name))
					{
						int_xml = CIFEnquiryLockUnlock(DB_List,formObject,callName);	
					}
					//Added below by Tarang against drop 4 Takeover on 04/04/2018
					else if ("UPDATE_LOAN_DETAILS".equalsIgnoreCase(callName)) {
						int_xml = UPDATE_LOAN_DETAILS_Custom(DB_List,formObject,callName);
					}
					//Added above by Tarang against drop 4 Takeover on 04/04/2018
						else{
							int_xml = Non_Custom(DB_List,formObject,callName,Operation_name);
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
						final_xml = new StringBuilder( Clean_Xml(final_xml.toString(),callName));
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

	private Map<String, String> Non_Custom(List<List<String>> DB_List,FormReference formObject,String callName,String Operation_name) {


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
				//below code added by akshay on 4/2/18 for proc 7395
				String tag_name =  recordFileMap.get("xmltag_name");
				String parent_tag =  recordFileMap.get("parent_tag_name");

				if(Operation_name.equalsIgnoreCase("PartMatch_CIF") && tag_name.equalsIgnoreCase("RCIFID")){
					String xml_str = int_xml.get(parent_tag);
					PersonalLoanS.mLogger.info("RLOS COMMON"+ " before adding PartMatch_CIF+ " + xml_str);
					xml_str = xml_str +"<"+tag_name+">"+ formObject.getNGValue("cmplx_PartMatch_cmplx_Partmatch_grid",formObject.getSelectedIndex("cmplx_PartMatch_cmplx_Partmatch_grid"),0)+"</"+tag_name+">";
					PersonalLoanS.mLogger.info("RLOS COMMON"+ " after adding PartMatch_CIF+ " + xml_str);
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

				

				if("PRIMARY".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),0))){	

					if ("AddressDetails".equalsIgnoreCase(tag_name)	&& int_xml.containsKey(parent_tag)) {
						String xml_str = int_xml.get(parent_tag);
						PersonalLoanS.mLogger.info("RLOS COMMON"+ " before adding address+ " + xml_str);
						xml_str = xml_str + getCustAddress_details(Call_name);
						PersonalLoanS.mLogger.info("RLOS COMMON"+ " after adding address+ " + xml_str);
						int_xml.put(parent_tag, xml_str);
					} 
					else if ("MinorFlag".equalsIgnoreCase(tag_name) && "PersonDetails".equalsIgnoreCase(parent_tag)) {
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

						if (NGFUserResourceMgr_PL.getGlobalVar("PL_Resident").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_ResidentNonResident"))) {
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

				else if( "SUPPLEMENT".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),0))){	
					if ("AddressDetails".equalsIgnoreCase(tag_name)	&& int_xml.containsKey(parent_tag)) {
						String xml_str = int_xml.get(parent_tag);
						PersonalLoanS.mLogger.info("RLOS COMMON"+ " before adding address+ " + xml_str);
						xml_str = xml_str + getCustAddress_details(Call_name);
						PersonalLoanS.mLogger.info("RLOS COMMON"+ " after adding address+ " + xml_str);
						int_xml.put(parent_tag, xml_str);
					} 
					
					else if("PersonDetails".equalsIgnoreCase(tag_name)){
						String xml_str = int_xml.get(parent_tag);
						PersonalLoanS.mLogger.info("RLOS COMMON"+ " before adding guarantor PersonDetails+ " + xml_str);
						xml_str = xml_str + getSupplement_PersonDetails(formObject,Call_name);
						PersonalLoanS.mLogger.info("RLOS COMMON"+ " after adding PersonDetails " + xml_str);
						int_xml.put(parent_tag, xml_str);

					}
					else if("DocDetails".equalsIgnoreCase(tag_name)){
						String xml_str = int_xml.get(parent_tag);
						PersonalLoanS.mLogger.info("RLOS COMMON"+ " before adding guarantor doc details " + xml_str);
						xml_str = xml_str + getSupplement_DocDetails(formObject,Call_name);
						PersonalLoanS.mLogger.info("RLOS COMMON"+ " after adding docDetails+ " + xml_str);
						int_xml.put(parent_tag, xml_str);

					}
					else if("PhoneNumber".equalsIgnoreCase(tag_name)){
						String xml_str = int_xml.get(parent_tag);
						String ph_no="";
						if( formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),0).equalsIgnoreCase("Supplement") && formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid")>0){ 
							for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid");i++){
								if(formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"), 2).equals(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,3))){
									ph_no=formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,6);
								}
							}
						}
						xml_str = xml_str +  "<"+tag_name+">"+ph_no
								+"</"+ tag_name+">";
						PersonalLoanS.mLogger.info("RLOS COMMON"+ " after adding guarantor PhoneDetails" + xml_str);
						int_xml.put(parent_tag, xml_str);
					}

					else if("MailIdValue".equalsIgnoreCase(tag_name)){

						String xml_str = int_xml.get(parent_tag);
						String email="";
						PersonalLoanS.mLogger.info("RLOS COMMON"+ " before adding guarantor mail details " + xml_str);
						if( formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),0).equalsIgnoreCase("Supplement") && formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid")>0){ 
							for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid");i++){
								if(formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"), 2).equals(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,3))){
									email=formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,21);
									break;
								}
							}
						}
						xml_str = xml_str +  "<"+tag_name+">"+email
								+"</"+ tag_name+">";
						PersonalLoanS.mLogger.info("RLOS COMMON"+ " after adding guarantor MailIdValue" + xml_str);
						int_xml.put(parent_tag, xml_str);
					}


					else if("EmploymentStatus".equalsIgnoreCase(tag_name)){

						String xml_str = int_xml.get(parent_tag);
						String status="";
						PersonalLoanS.mLogger.info("RLOS COMMON"+ " before adding guarantor EmploymentStatus " + xml_str);
						if( formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),0).equalsIgnoreCase("Supplement") && formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid")>0){ 
							for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid");i++){
								if(formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"), 2).equals(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,3))){
									status=formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,32);
									break;
								}
							}
						}
						xml_str = xml_str +  "<"+tag_name+">"+status
								+"</"+ tag_name+">";
						PersonalLoanS.mLogger.info("RLOS COMMON"+ " after adding guarantor EmploymentStatus" + xml_str);
						int_xml.put(parent_tag, xml_str);
					}

					else if("Occupation".equalsIgnoreCase(tag_name)){

						String xml_str = int_xml.get(parent_tag);
						String Occupation="";
						PersonalLoanS.mLogger.info("RLOS COMMON"+ " before adding guarantor Occupation " + xml_str);
						if( formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),0).equalsIgnoreCase("Supplement") && formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid")>0){ 
							for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid");i++){
								if(formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"), 2).equals(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,3))){
									Occupation=formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,31);
									break;
								}
							}
						}
						xml_str = xml_str +  "<"+tag_name+">"+Occupation
								+"</"+ tag_name+">";
						PersonalLoanS.mLogger.info("RLOS COMMON"+ " after adding guarantor EmploymentStatus" + xml_str);
						int_xml.put(parent_tag, xml_str);
					}

					else{
						int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,Call_name);
					}
				}


			}
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("CC Integration "+ " Exception occured in NEW_CUST_REQUEST + ");
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

				 if("AddrDet".equalsIgnoreCase(tag_name)){
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

				if(formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),0)!=null && "PRIMARY".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),0)))
				{
					if("OECDDet".equalsIgnoreCase(tag_name) && "CUSTOMER_UPDATE_REQ".equalsIgnoreCase(Call_name)){
						PersonalLoanS.mLogger.info("PL Common"+"inside OECDDet inside customer update req1");
						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + getCustOECD_details(formObject,"PRIMARY");
						PersonalLoanS.mLogger.info("PL COMMON"+" after adding OECDDet: "+xml_str);
						int_xml.put(parent_tag, xml_str);                                    
					} 
					
					else if ("PhnLocalCode".equalsIgnoreCase(tag_name)) {
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
					}
					else{
						int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,Call_name);
					}
				}
				else
				{
					if("DocDet".equalsIgnoreCase(tag_name)){
						String xml_str = int_xml.get(parent_tag);
						PersonalLoanS.mLogger.info("RLOS COMMON"+ " before adding guarantor doc details " + xml_str);
						xml_str = xml_str + getSupplement_DocDetails(formObject,Call_name);
						PersonalLoanS.mLogger.info("RLOS COMMON"+ " after adding docDetails+ " + xml_str);
						int_xml.put(parent_tag, xml_str);

					}

					else if("RtlAddnlDet".equalsIgnoreCase(tag_name)){
						String xml_str = int_xml.get(parent_tag);
						PersonalLoanS.mLogger.info("RLOS COMMON"+ " before adding RtlAddnlDet details " + xml_str);
						xml_str = xml_str + getSupplement_PersonDetails(formObject,Call_name);
						PersonalLoanS.mLogger.info("RLOS COMMON"+ " after adding RtlAddnlDet+ " + xml_str);
						int_xml.put(parent_tag, xml_str);

					}

					if(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13)!=null && "SUPPLEMENT".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13)))
					{
						if("CCIFID".equalsIgnoreCase(tag_name) ){
							PersonalLoanS.mLogger.info("inside 1st if inside customer update req");
							String xml_str = int_xml.get(parent_tag);
							xml_str =xml_str+ "<"+tag_name+">"+formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),3)+"</"+ tag_name+">";
							PersonalLoanS.mLogger.info("PL COMMON  after adding CIFId:  "+xml_str);
							int_xml.put(parent_tag, xml_str);	                            	
						}
						else if("PhoneNo".equalsIgnoreCase(tag_name) || "PhnLocalCode".equalsIgnoreCase(tag_name)){
							String xml_str = int_xml.get(parent_tag);
							String ph_no ="",phlocal="";
							if( formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13).equalsIgnoreCase("Supplement") && formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid")>0){ 
								for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid");i++){
									if(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),14).equals(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,3))){
										ph_no=formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,6);
										break;
									}
								}
							} 
							if ("PhoneNo".equalsIgnoreCase(tag_name)){
								xml_str = xml_str + "<" + tag_name + ">" +ph_no + "</" + tag_name + ">";
							}
							else if(!"".equalsIgnoreCase(ph_no) && ph_no.indexOf("00971") > -1) 
							{
								phlocal = ph_no.substring(5);
								xml_str = xml_str + "<" + tag_name + ">" +phlocal + "</" + tag_name + ">";
							}
							PersonalLoanS.mLogger.info("RLOS COMMON"+ " after adding PhoneNo flag+ " + xml_str);
							int_xml.put(parent_tag, xml_str);
						}
						else if("Email".equalsIgnoreCase(tag_name)){

							String xml_str = int_xml.get(parent_tag);
							String email="";
							PersonalLoanS.mLogger.info("RLOS COMMON"+ " before adding supplement mail details " + xml_str);
							if( formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13).equalsIgnoreCase("Supplement") && formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid")>0){ 
								for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid");i++){
									if(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),14).equals(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,3))){
										email=formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,21);
										break;
									}
								}
							}
							xml_str = xml_str +  "<"+tag_name+">"+email
									+"</"+ tag_name+">";
							PersonalLoanS.mLogger.info("RLOS COMMON"+ " after adding guarantor MailIdValue" + xml_str);
							int_xml.put(parent_tag, xml_str);
						}
						else{	
							int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,Call_name);
						}
					}
					else if(formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),0)!=null && "GUARANTOR".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),0)))
					{
						if("CCIFID".equalsIgnoreCase(tag_name) ){
							PersonalLoanS.mLogger.info("inside 1st if inside customer update req");
							String xml_str = int_xml.get(parent_tag);
							xml_str =xml_str+ "<"+tag_name+">"+formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),3)+"</"+ tag_name+">";
							PersonalLoanS.mLogger.info("PL COMMON  after adding CIFId:  "+xml_str);
							int_xml.put(parent_tag, xml_str);	                            	
						}
						else if("PhoneNo".equalsIgnoreCase(tag_name) || "PhnLocalCode".equalsIgnoreCase(tag_name)){
							String xml_str = int_xml.get(parent_tag);
							String ph_no ="",phlocal="";
							if(formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),2).equals(formObject.getNGValue("cmplx_Guarantror_GuarantorDet",0,5)))
							{ 
								ph_no=formObject.getNGValue("cmplx_Guarantror_GuarantorDet",0,10);								
								
							} 
							if ("PhoneNo".equalsIgnoreCase(tag_name)){
								xml_str = xml_str + "<" + tag_name + ">" +ph_no + "</" + tag_name + ">";
							}
							else if(!"".equalsIgnoreCase(ph_no) && ph_no.indexOf("00971") > -1) 
							{
								phlocal = ph_no.substring(5);
								xml_str = xml_str + "<" + tag_name + ">" +phlocal + "</" + tag_name + ">";
							}
							PersonalLoanS.mLogger.info("RLOS COMMON"+ " after adding PhoneNo flag+ " + xml_str);
							int_xml.put(parent_tag, xml_str);
						}
						
						else if("Email".equalsIgnoreCase(tag_name)){

							String xml_str = int_xml.get(parent_tag);
							xml_str = xml_str +  "<"+tag_name+">"
									+"</"+ tag_name+">";
							PersonalLoanS.mLogger.info("RLOS COMMON"+ " after adding guarantor MailIdValue" + xml_str);
							int_xml.put(parent_tag, xml_str);
						}
						else{
							int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,Call_name);
						}
					}

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
				String Applicant_Name=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i,13);

				PersonalLoanS.mLogger.info("PLCommon java file"+ "ADD Type: "+Address_type);
				PersonalLoanS.mLogger.info("PLCommon java file"+ "Applicant type: "+Applicant_Name);
				String preferrd;
				if(NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 11)))
					preferrd = "Y";
				else
					preferrd = "N";

				//added here
				PersonalLoanS.mLogger.info("RLOSCommon java file"+ "inside getCustAddress_details add_row_count+ : "+years_in_current_add);
				int years=Integer.parseInt(years_in_current_add);
				//ended here
				if (!"".equalsIgnoreCase(years_in_current_add)){
					years=(int)Float.parseFloat(years_in_current_add);
				}
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
				//new condition added to handle address type for all finacle calls. 22Nov2017.
				if("HOME".equalsIgnoreCase(Address_type) && !fin_call_name.contains(call_name)) {
					Address_type="Home Country";
				}
				PersonalLoanS.mLogger.info("PLCommon java file"+ "ADD Applicant_Name after: "+Applicant_Name);
				PersonalLoanS.mLogger.info("PLCommon java file"+ "ADD asdasdasdasdasdasdasdasd after: "+formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),1));


				if("NEW_CUSTOMER_REQ".equalsIgnoreCase(call_name) && Applicant_Name.split("-")[1].equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),1)))
				{	PersonalLoanS.mLogger.info("PLCommon java file"+ "InsideNEW_CUSTOMER_REQNEW_CUSTOMER_REQ "+Applicant_Name);
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


				else if("CUSTOMER_UPDATE_REQ".equalsIgnoreCase(call_name)){
					if(Applicant_Name.split("-")[1].equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),1)) || Applicant_Name.split("-")[1].equals(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),4)))
					{PersonalLoanS.mLogger.info("PLCommon java file"+ "CUSTOMER_UPDATE_REQCUSTOMER_UPDATE_REQCUSTOMER_UPDATE_REQ "+Applicant_Name);
					
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
				}

				else if(Applicant_Name.split("-")[1].equals(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),4)))
				{
					/*if("CARD_NOTIFICATION".equalsIgnoreCase(call_name)){
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

					}*/
					  if(call_name.equalsIgnoreCase("CARD_NOTIFICATION")){
							if (Address_type.equalsIgnoreCase("HOME")){
								Address_type="Home Country";
							}
							add_xml_str = add_xml_str + "<AddrDet><AddressType>"+Address_type+"</AddressType>";
							add_xml_str = add_xml_str + "<AddressLine1>"+flat_Villa+"</AddressLine1>";
							add_xml_str = add_xml_str + "<AddressLine2>"+Building_name+"</AddressLine2>";
							add_xml_str = add_xml_str + "<AddressLine3>"+street_name+"</AddressLine3>";
							add_xml_str = add_xml_str + "<AddressLine4>"+Landmard+"</AddressLine4>";
							add_xml_str = add_xml_str + "<City>"+city+"</City>";
							add_xml_str = add_xml_str + "<State>"+Emirates+"</State>";
							add_xml_str = add_xml_str + "<Country>"+country+"</Country>";
							if(Address_type.equalsIgnoreCase("Home Country")){
								add_xml_str = add_xml_str + "<ZipCode>"+Po_Box+"</ZipCode>";
							}
							add_xml_str = add_xml_str + "<POBox>"+Po_Box+"</POBox>";
							add_xml_str = add_xml_str + "<EffectiveFromDate>"+EffectiveFrom+"</EffectiveFromDate>";
							add_xml_str = add_xml_str + "<EffectiveToDate>"+EffectiveTo+"</EffectiveToDate>";
							add_xml_str = add_xml_str + "<NumberOfYears>"+years+"</NumberOfYears>";
							add_xml_str = add_xml_str + "<ResidenceType>R</ResidenceType>";
							add_xml_str = add_xml_str + "<AddrPrefFlag>"+preferrd+"</AddrPrefFlag>"
							+ "</AddrDet>";
							//add_xml_str = add_xml_str + "<POBox>"+Po_Box+"</POBox></AddrDet>";
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
				}
				
				else{
					PersonalLoanS.mLogger.info("getCustAddressDetails final else--->"+ "Customer entry not in CC creation Grid");
					add_xml_str = add_xml_str + "<AddressDetails><AddressType>"+Address_type+"</AddressType>";
					add_xml_str = add_xml_str + "<AddressLine1>"+flat_Villa+"</AddressLine1>";
					add_xml_str = add_xml_str + "<AddressLine2>"+Building_name+"</AddressLine2>";
					add_xml_str = add_xml_str + "<AddressLine3>"+street_name+"</AddressLine3>";
					add_xml_str = add_xml_str + "<City>"+city+"</City>";
					add_xml_str = add_xml_str + "<State>"+Emirates+"</State>";
					add_xml_str = add_xml_str + "<Country>"+country+"</Country>";
					add_xml_str = add_xml_str + "<POBox>"+Po_Box+"</POBox></AddressDetails>";
					//change by saurabh on 1st Feb as acc to TRS only 1 address aggregate to pass for Address.
					if("DEDUP_SUMMARY".equalsIgnoreCase(call_name)){
						break;
					}
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
		PersonalLoanS.mLogger.info("RLOSCommon java file"+ "inside getProduct_details : "+formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
		String  prod_xml_str ="";
		String Manual_Dev=formObject.getNGValue("cmplx_DEC_Manual_Deviation");
		String ApplicationDate="";
		for (int i = 0; i<prod_row_count;i++){
			PersonalLoanS.mLogger.info("PL_SKLogger java file"+ "inside getProduct_details add_row_count+ : "+prod_row_count);

			String prod_type = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 0); //0
			String priority = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 8);
			String reqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 1);//1
			String subProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2);//2
			String reqLimit=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 3);//3
			reqLimit=reqLimit.replaceAll(",", "");
			String appType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 4);//4
			//String cardProd = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 5);//5
			String scheme=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 7);//6
			String tenure=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 6);//7
			// String limitExpiry=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 12);//8
			//String ApplicationDate=formObject.getNGValue("CreatedDate");//Tarang to be removed on friday(1/19/2018)
			String ApplicationDateQuery="select Introduction_date from ng_pl_exttable with (nolock) where pl_wi_name='"+formObject.getWFWorkitemName()+"'";
			List<List<String>> ApplicationDateXML = formObject.getNGDataFromDataCache(ApplicationDateQuery);
			try{
			if (!ApplicationDateXML.isEmpty())
			{
				ApplicationDate=ApplicationDateXML.get(0).get(0);
				PersonalLoanS.mLogger.info( "Excep occur in app date  "+ApplicationDate);
				ApplicationDate=ApplicationDate.substring(0, 10)+"T"+ApplicationDate.substring(11, 19);
			}
			}
			catch(Exception e){PersonalLoanS.mLogger.info( "Excep occur in app date  ");
			new PLCommon().printException(e);}
			
			
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
				prod_xml_str = prod_xml_str + "<requested_card_product></requested_card_product>";
				prod_xml_str = prod_xml_str + "<application_type>"+appType+"</application_type>";
				prod_xml_str = prod_xml_str + "<scheme>"+scheme+"</scheme>";
				prod_xml_str = prod_xml_str + "<tenure>"+tenure+"</tenure>";
				prod_xml_str = prod_xml_str + "<customer_type>"+(NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))?"NTB":"Existing")+"</customer_type>";
				if(Manual_Dev!=null){ 
					if("true".equalsIgnoreCase(Manual_Dev)){
						prod_xml_str = prod_xml_str + "<limit_expiry_date></limit_expiry_date><final_limit>"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit")+"</final_limit><emi>"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_EMI")+"</emi><manual_deviation>Y</manual_deviation><application_date>"+ApplicationDate+"</application_date></ApplicationDetails>";
					}
					else{
						prod_xml_str = prod_xml_str + "<limit_expiry_date></limit_expiry_date><final_limit>"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit")+"</final_limit><emi>"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_EMI")+"</emi><manual_deviation>N</manual_deviation><application_date>"+ApplicationDate+"</application_date></ApplicationDetails>";
					}   
				}
				else {
					prod_xml_str = prod_xml_str + "<limit_expiry_date></limit_expiry_date><final_limit>"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit")+"</final_limit><emi>"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_EMI")+"</emi><manual_deviation></manual_deviation><application_date>"+ApplicationDate+"</application_date></ApplicationDetails>";

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
			String sQuery = "SELECT isNull((Sum(convert(float,replace([OutstandingAmt],'NA','0')))),0),isNull((Sum(convert(float,replace([OverdueAmt],'NA','0')))),0),isNull((Sum(convert(float,replace([CreditLimit],'NA','0')))),0)FROM ng_RLOS_CUSTEXPOSE_CardDetails WHERE Child_Wi= '"+formObject.getWFWorkitemName()+"' AND Request_Type = 'InternalExposure'  union SELECT   isNull((Sum(convert(float,replace([TotalOutstandingAmt],'NA','0')))),0),isNull((Sum(convert(float,replace([OverdueAmt],'NA','0')))),0),isNull((Sum(convert(float,replace([TotalLoanAmount],'NA','0')))),0) FROM ng_RLOS_CUSTEXPOSE_LoanDetails   with (nolock) WHERE Child_wi = '"+formObject.getWFWorkitemName()+"'  and  LoanStat  not in ('Pipeline','CAS-Pipeline')";
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
				//PersonalLoanS.mLogger.info("insidefor list "+i+ "values"+OutputXML.get(i).get(1));
				if(OutputXML.get(i).get(0)!=null && !OutputXML.get(i).get(0).isEmpty() &&  !"".equals(OutputXML.get(i).get(0)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(0)) ){
					PersonalLoanS.mLogger.info("Totaloutstanding"+i+ "values."+TotOutstandingAmt+"..");
					PersonalLoanS.mLogger.info("Float.parseFloat(OutputXML.get(i).get(0)"+Float.parseFloat(OutputXML.get(i).get(0)));
					TotOutstandingAmt = TotOutstandingAmt + Float.parseFloat(OutputXML.get(i).get(0));
					PersonalLoanS.mLogger.info("Totaloutstanding after:"+TotOutstandingAmt);
				}
				if(OutputXML.get(i).get(1)!=null && !OutputXML.get(i).get(1).isEmpty() && !"".equals(OutputXML.get(i).get(1)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(1)) ){
					PersonalLoanS.mLogger.info("TotOverdueAmt"+i+ "values."+TotOutstandingAmt+"..");
					PersonalLoanS.mLogger.info("Float.parseFloat(OutputXML.get(i).get(1)"+Float.parseFloat(OutputXML.get(i).get(1)));
					TotOverdueAmt = TotOverdueAmt + Float.parseFloat(OutputXML.get(i).get(1));
					PersonalLoanS.mLogger.info("TotOverdueAmt after:"+TotOverdueAmt);

				}

			}
			String TotOutstandingAmtSt=String.format("%.0f", TotOutstandingAmt);
			String TotOverdueAmtSt=String.format("%.0f", TotOverdueAmt);
			
			add_xml_str = add_xml_str + "<total_out_bal>"+TotOutstandingAmtSt+"</total_out_bal>";
			add_xml_str = add_xml_str + "<total_overdue>"+TotOverdueAmtSt+"</total_overdue>";
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
		String sQuery = "SELECT cifid,agreementid,loantype,loantype,custroletype,loan_start_date,loanmaturitydate,lastupdatedate ,totaloutstandingamt,totalloanamount,NextInstallmentAmt,paymentmode,totalnoofinstalments,remaininginstalments,totalloanamount,	overdueamt,nofdayspmtdelay,monthsonbook,currentlycurrentflg,currmaxutil,DPD_30_in_last_6_months,DPD_60_in_last_18_months,propertyvalue,loan_disbursal_date,marketingcode,DPD_30_in_last_3_months,DPD_30_in_last_6_months,DPD_30_in_last_9_months,DPD_30_in_last_12_months,DPD_30_in_last_18_months,DPD_30_in_last_24_months,DPD_60_in_last_3_months,DPD_60_in_last_6_months,DPD_60_in_last_9_months,DPD_60_in_last_12_months,DPD_60_in_last_18_months,DPD_60_in_last_24_months,DPD_90_in_last_3_months,DPD_90_in_last_6_months,DPD_90_in_last_9_months,DPD_90_in_last_12_months,DPD_90_in_last_18_months,DPD_90_in_last_24_months,DPD_120_in_last_3_months,DPD_120_in_last_6_months,DPD_120_in_last_9_months,DPD_120_in_last_12_months,DPD_120_in_last_18_months,DPD_120_in_last_24_months,DPD_150_in_last_3_months,DPD_150_in_last_6_months,DPD_150_in_last_9_months,DPD_150_in_last_12_months,DPD_150_in_last_18_months,DPD_150_in_last_24_months,DPD_180_in_last_3_months,DPD_180_in_last_6_months,DPD_180_in_last_9_months,DPD_180_in_last_12_months,DPD_180_in_last_24_months,'' as col1,isnull(Consider_For_Obligations,'true'),LoanStat,'LOANS',writeoffStat,writeoffstatdt,lastrepmtdt,limit_increase,PartSettlementDetails,SchemeCardProd as SchemeCardProduct,General_Status,Internal_WriteOff_Check,'' FROM ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) WHERE Child_wi = '"+formObject.getWFWorkitemName()+"' and LoanStat  not in ('Pipeline','CAS-Pipeline') union select CifId,CardEmbossNum,CardType,CardType,CustRoleType,'' as col6,'' as col7,'' as col8,OutstandingAmt,CreditLimit,case when CardType like '%LOC%' then (select monthlyamount from ng_RLOS_CUSTEXPOSE_CardInstallmentDetails where child_wi ='"+formObject.getWFWorkitemName()+"' and CardCRNNumber=CardEmbossNum) else PaymentsAmount end,PaymentMode,'' as col13,'' as col14,CashLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,'' as col19,CurrMaxUtil,DPD_30_in_last_6_months,DPD_60_in_last_18_months,'' as col23,'' as col24,'' as col25,DPD_30_in_last_3_months,DPD_30_in_last_6_months,DPD_30_in_last_9_months,DPD_30_in_last_12_months,DPD_30_in_last_18_months,DPD_30_in_last_24_months,DPD_60_in_last_3_months,DPD_60_in_last_6_months,DPD_60_in_last_9_months,DPD_60_in_last_12_months,DPD_60_in_last_18_months,DPD_60_in_last_24_months,DPD_90_in_last_3_months,DPD_90_in_last_6_months,DPD_90_in_last_9_months,DPD_90_in_last_12_months,DPD_90_in_last_18_months,DPD_90_in_last_24_months,DPD_120_in_last_3_months,DPD_120_in_last_6_months,DPD_120_in_last_9_months,DPD_120_in_last_12_months,DPD_120_in_last_18_months,DPD_120_in_last_24_months,DPD_150_in_last_3_months,DPD_150_in_last_6_months,DPD_150_in_last_9_months,DPD_150_in_last_12_months,DPD_150_in_last_18_months,DPD_150_in_last_24_months,DPD_180_in_last_3_months,DPD_180_in_last_6_months,DPD_180_in_last_9_months,DPD_180_in_last_12_months,DPD_180_in_last_24_months,ExpiryDate,isnull(Consider_For_Obligations,'true'),CardStatus,'CARDS',writeoffStat,writeoffstatdt,'' as lastrepdate,limit_increase,'' as PartSettlementDetails,SchemeCardProd,General_Status,Internal_WriteOff_Check,'' FROM ng_RLOS_CUSTEXPOSE_CardDetails with (nolock) where 	Child_wi = '"+formObject.getWFWorkitemName()+"' and Request_Type In ('InternalExposure','CollectionsSummary') union select CifId,AcctId,'OverDraft' as loantype,'OverDraft' as loantype,CustRoleType,LimitSactionDate as loan_start_date,LimitExpiryDate as loanmaturitydate,'' as lastupdateddate,ClearBalanceAmount,SanctionLimit,'','','','',SanctionLimit,OverdueAmount,DaysPastDue,MonthsOnBook,IsCurrent,CurUtilRate,DPD30Last6Months,DPD60Last18Months,'',AccountOpenDate,'','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','',LimitExpiryDate,isNull(Consider_For_Obligations,'true'),AcctStat,'OVERDRAFT',WriteoffStat,WriteoffStatDt,LastRepmtDt,'','','','','',odtype  from ng_rlos_custexpose_acctdetails where Child_wi = '"+formObject.getWFWorkitemName()+"'  and ODType != '' ";
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
				String EmployerType=formObject.getNGValue("cmplx_EmploymentDetails_employer_type");
				String Kompass=formObject.getNGValue("cmplx_EmploymentDetails_Kompass");
				String paid_installment="";
				String Internal_WriteOff_Check="";
				String Type_of_OD="";
				PersonalLoanS.mLogger.info("Inside for"+ "asdasdasd");
				String LoanType="";

				if(!(OutputXML.get(i).get(0) == null || "".equals(OutputXML.get(i).get(0))) ){
					cifId = OutputXML.get(i).get(0);
				}
				if(!(OutputXML.get(i).get(1) == null || "".equals(OutputXML.get(i).get(1))) ){
					agreementId = OutputXML.get(i).get(1);
				}				
				if(!(OutputXML.get(i).get(2) == null || "".equals(OutputXML.get(i).get(2))) ){
					product_type = OutputXML.get(i).get(2);
					LoanType = OutputXML.get(i).get(2);
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
						Limit_increase="N";
					}
					else{
						Limit_increase="Y";
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
				if(!(OutputXML.get(i).get(72) == null || "".equals(OutputXML.get(i).get(72))) ){
					Type_of_OD = OutputXML.get(i).get(72);
				}
				String sQueryCombinedLimit = "select Distinct(COMBINEDLIMIT_ELIGIBILITY) from ng_master_cardProduct where code='"+SchemeCardProduct+"'";
				List<List<String>> sQueryCombinedLimitXML = formObject.getNGDataFromDataCache(sQueryCombinedLimit);
				try{
					if(sQueryCombinedLimitXML!=null && sQueryCombinedLimitXML.size()>0 && sQueryCombinedLimitXML.get(0)!=null){
							Combined_Limit="1".equalsIgnoreCase(sQueryCombinedLimitXML.get(0).get(0))?"Y":"N";
					}
				}
				catch(Exception e){
					PersonalLoanS.mLogger.info("Exception occured at sQueryCombinedLimit for"+sQueryCombinedLimit);

				}
				String sQuerySecuredCard = "select count(*) from ng_master_cardProduct where code='"+SchemeCardProduct+"'  and subproduct='SEC'";
				List<List<String>> sQuerySecuredCardXML = formObject.getNGDataFromDataCache(sQuerySecuredCard);
				try{
					if(sQuerySecuredCardXML!=null && sQuerySecuredCardXML.size()>0 && sQuerySecuredCardXML.get(0)!=null){
							SecuredCard="0".equalsIgnoreCase(sQuerySecuredCardXML.get(0).get(0))?"N":"Y";
				}
				}
				catch(Exception e){
					PersonalLoanS.mLogger.info("Exception occured at sQueryCombinedLimit for"+sQueryCombinedLimit);

				}
				if(cifId!=null && !"".equalsIgnoreCase(cifId) && !"null".equalsIgnoreCase(cifId)){
					PersonalLoanS.mLogger.info("Inside if"+ "asdasdasd");
					add_xml_str = add_xml_str + "<InternalBureauIndividualProducts><applicant_id>"+cifId+"</applicant_id>";
					add_xml_str = add_xml_str + "<internal_bureau_individual_products_id>"+agreementId+"</internal_bureau_individual_products_id>";
					add_xml_str = add_xml_str + "<type_product>"+product_type+"</type_product>";
					if ("cards".equalsIgnoreCase(OutputXML.get(i).get(63))){	
						if (SchemeCardProduct.startsWith("LOC")){
							add_xml_str = add_xml_str + "<contract_type>IM</contract_type>";
						}
						else{
						add_xml_str = add_xml_str + "<contract_type>CC</contract_type>";
						}
					}
					else if ("Loans".equalsIgnoreCase(OutputXML.get(i).get(63)))
					{
						if (LoanType.equalsIgnoreCase("MURABAHA")){
							if (SchemeCardProduct.contains("AMAL PERSONAL FINANCE")){
								add_xml_str = add_xml_str + "<contract_type>IPL</contract_type>";
							}
							else if (SchemeCardProduct.contains("AMAL AUTO FINANCE")){
								add_xml_str = add_xml_str + "<contract_type>IAL</contract_type>";
							}
						}
						else if (LoanType.equalsIgnoreCase("AUTO")){
							add_xml_str = add_xml_str + "<contract_type>AL</contract_type>";
						}
						else if (LoanType.equalsIgnoreCase("HOME")){
							add_xml_str = add_xml_str + "<contract_type>ML</contract_type>";
						}
						else if (LoanType.equalsIgnoreCase("IJARAH")){
							add_xml_str = add_xml_str + "<contract_type>IML</contract_type>";
						}
						else if (LoanType.equalsIgnoreCase("PERSONAL")){
							add_xml_str = add_xml_str + "<contract_type>PL</contract_type>";
						}
						else if (LoanType.equalsIgnoreCase("HOME IN ONE")){
							add_xml_str = add_xml_str + "<contract_type>HIO</contract_type>";
						}
						else{
							add_xml_str = add_xml_str + "<contract_type>TRADE</contract_type>";
						}
					}	
					else if ("OVERDRAFT".equalsIgnoreCase(OutputXML.get(i).get(63)))
					{
						add_xml_str = add_xml_str + "<contract_type>OD</contract_type>";
						
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
						add_xml_str = add_xml_str + "<no_of_paid_installment>"+paid_installment+"</no_of_paid_installment><write_off_amount>"+Internal_WriteOff_Check+"</write_off_amount><company_flag>N</company_flag><type_of_od>"+Type_of_OD+"</type_of_od><amt_paid_last6mnths>"+""+"</amt_paid_last6mnths></InternalBureauIndividualProducts>";

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
		String sQuery = "SELECT cifid,loantype,custroletype,lastupdatedate,totalamount,totalnoofinstalments,totalloanamount,agreementId,NoOfDaysInPipeline FROM ng_RLOS_CUSTEXPOSE_LoanDetails  with (nolock) where Child_wi = '"+formObject.getWFWorkitemName()+"' and  LoanStat = 'Pipeline'";
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
			String NoOfDaysInPipeline="";
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
			if(!(OutputXML.get(i).get(8) == null || "".equalsIgnoreCase(OutputXML.get(i).get(8))) ){
				NoOfDaysInPipeline = OutputXML.get(i).get(8);
			}
			if(cifId!=null && !"".equalsIgnoreCase(cifId) && !"null".equalsIgnoreCase(cifId)){
				add_xml_str = add_xml_str + "<InternalBureauPipelineProducts>";// to be populated later
				add_xml_str = add_xml_str + "<applicant_id>"+cifId+"</applicant_id>";
				add_xml_str = add_xml_str + "<internal_bureau_pipeline_products_id>"+agreementId+"</internal_bureau_pipeline_products_id>";// to be populated later
				add_xml_str = add_xml_str + "<ppl_provider_no>RAKBANK</ppl_provider_no>";
				add_xml_str = add_xml_str + "<ppl_product>"+Product+"</ppl_product>";
				add_xml_str = add_xml_str + "<ppl_type_of_contract>"+""+"</ppl_type_of_contract>";
				add_xml_str = add_xml_str + "<ppl_phase>PIPELINE</ppl_phase>"; // to be populated later

				add_xml_str = add_xml_str + "<ppl_role>Primary</ppl_role>";
				add_xml_str = add_xml_str + "<ppl_date_of_last_update>"+lastUpdateDate+"</ppl_date_of_last_update>";
				add_xml_str = add_xml_str + "<ppl_total_amount>"+TotAmount+"</ppl_total_amount>";
				add_xml_str = add_xml_str + "<ppl_no_of_instalments>"+TotNoOfInstlmnt+"</ppl_no_of_instalments>";
				add_xml_str = add_xml_str + "<ppl_credit_limit>"+TotLoanAmt+"</ppl_credit_limit>";

				add_xml_str = add_xml_str + "<ppl_no_of_days_in_pipeline>"+NoOfDaysInPipeline+"</ppl_no_of_days_in_pipeline><company_flag>N</company_flag></InternalBureauPipelineProducts>"; // to be populated later
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
		String sQuery = "select  CifId,fullnm,TotalOutstanding,TotalOverdue,NoOfContracts,Total_Exposure,WorstCurrentPaymentDelay,Worst_PaymentDelay_Last24Months,Worst_Status_Last24Months,Nof_Records,NoOf_Cheque_Return_Last3,Nof_DDES_Return_Last3Months,Nof_Cheque_Return_Last6,DPD30_Last6Months,(select max(ExternalWriteOffCheck) ExternalWriteOffCheck from ((select convert(int,isNULL(ExternalWriteOffCheck,0)) ExternalWriteOffCheck  from ng_rlos_cust_extexpo_CardDetails where Child_wi  = '"+formObject.getWFWorkitemName()+"' union select convert(int,isNULL(ExternalWriteOffCheck,0))ExternalWriteOffCheck    from ng_rlos_cust_extexpo_LoanDetails where Child_wi  = '"+formObject.getWFWorkitemName()+"')) as ExternalWriteOffCheck) from NG_rlos_custexpose_Derived where Child_wi  = '"+formObject.getWFWorkitemName()+"' and Request_type= 'ExternalExposure'";
		PersonalLoanS.mLogger.info("ExternalBureauData sQuery"+sQuery+ "");
		String AecbHistQuery = "select isnull(max(AECBHistMonthCnt),0) as AECBHistMonthCnt from ( select MAX(AECBHistMonthCnt) as AECBHistMonthCnt  from ng_rlos_cust_extexpo_CardDetails where  wi_name  = '"+formObject.getWFWorkitemName()+"' union select Max(AECBHistMonthCnt) from ng_rlos_cust_extexpo_LoanDetails where Child_wi  = '"+formObject.getWFWorkitemName()+"') as ext_expo";
		String  add_xml_str ="";
		try{
			List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
			PersonalLoanS.mLogger.info("ExternalBureauData list size"+OutputXML.size()+ "");
			List<List<String>> AecbHistQueryData = formObject.getDataFromDataSource(AecbHistQuery);
			
			if (OutputXML.size()==0){


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
					String ExternalWriteOffCheck="";
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
					if(!(OutputXML.get(i).get(14) == null || "".equalsIgnoreCase(OutputXML.get(i).get(14))) && !"0".equalsIgnoreCase(OutputXML.get(i).get(14)) ){
						ExternalWriteOffCheck = OutputXML.get(i).get(14);
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
					add_xml_str = add_xml_str + "<prod_external_writeoff_amount>"+ExternalWriteOffCheck+"</prod_external_writeoff_amount>";

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
		String sQuery = "select CifId,AgreementId,LoanType,ProviderNo,LoanStat,CustRoleType,LoanApprovedDate,LoanMaturityDate,OutstandingAmt,TotalAmt,PaymentsAmt,TotalNoOfInstalments,RemainingInstalments,WriteoffStat,WriteoffStatDt,CreditLimit,OverdueAmt,NofDaysPmtDelay,MonthsOnBook,lastrepmtdt,IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last18Months,AECBHistMonthCnt,DPD5_Last3Months,'' as qc_amt,'' as qc_emi,'' as Cac_indicator,Take_Over_Indicator,isnull(Consider_For_Obligations,'true'),case when IsDuplicate= '1' then 'Y' else 'N' end from ng_rlos_cust_extexpo_LoanDetails where child_wi= '"+formObject.getWFWorkitemName()+"'  and LoanStat != 'Pipeline' union select CifId,CardEmbossNum,CardType,ProviderNo,CardStatus,CustRoleType,StartDate,ClosedDate,CurrentBalance,'' as col6,PaymentsAmount,NoOfInstallments,'' as col5,WriteoffStat,WriteoffStatDt,CashLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,lastrepmtdt,IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last18Months,AECBHistMonthCnt,DPD5_Last3Months,QC_Amt,QC_EMI,isNull(CAC_Indicator,'false'),Take_Over_Indicator,isnull(Consider_For_Obligations,'true'),case when IsDuplicate= '1' then 'Y' else 'N' end from ng_rlos_cust_extexpo_CardDetails where child_wi  =  '"+formObject.getWFWorkitemName()+"' and cardstatus != 'Pipeline' union select CifId,AcctId,AcctType,AcctId,AcctStat,CustRoleType,StartDate,ClosedDate,OutStandingBalance,TotalAmount,PaymentsAmount,'','',WriteoffStat,WriteoffStatDt,CreditLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,'',IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last18Months,AECBHistMonthCnt,DPD5_Last3Months,'','','','',isnull(Consider_For_Obligations,'true'),case when IsDuplicate= '1' then 'Y' else 'N' end from ng_rlos_cust_extexpo_AccountDetails where AcctType='Overdraft' and child_wi  =  '"+formObject.getWFWorkitemName()+"' ";
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
			String Duplicate_flag="";
			if(!(OutputXML.get(i).get(1) == null || "".equals(OutputXML.get(i).get(1))) ){
				AgreementId = OutputXML.get(i).get(1);
			}				
			if(!(OutputXML.get(i).get(2) == null || "".equals(OutputXML.get(i).get(2))) ){
				ContractType = OutputXML.get(i).get(2);
				try{
					String cardquery = "select code from ng_master_contract_type where description='"+ContractType+"'";
					PersonalLoanS.mLogger.info("ExternalBureauIndividualProducts sQuery"+sQuery+ "");
					List<List<String>> cardqueryXML = formObject.getNGDataFromDataCache(cardquery);
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
				String sQueryCustRoleType = "select code from ng_master_role_of_customer where Description='"+CustRoleType+"'";
				PersonalLoanS.mLogger.info("CustRoleType"+sQueryCustRoleType);
				List<List<String>> sQueryCustRoleTypeXML = formObject.getNGDataFromDataCache(sQueryCustRoleType);
				try{
					if(sQueryCustRoleTypeXML!=null && sQueryCustRoleTypeXML.size()>0 && sQueryCustRoleTypeXML.get(0)!=null){
						CustRoleType=sQueryCustRoleTypeXML.get(0).get(0);
					}
				}
				catch(Exception e){
					PersonalLoanS.mLogger.info("Exception occured at sQueryCombinedLimit for"+sQueryCustRoleTypeXML);

				}
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
			if(!(OutputXML.get(i).get(31) == null || OutputXML.get(i).get(31).equals("")) ){
				Duplicate_flag = OutputXML.get(i).get(31).toString();
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
				add_xml_str = add_xml_str + "<qc_amount>"+QC_Amt+"</qc_amount><company_flag>N</company_flag><cac_bank_name></cac_bank_name><take_over_indicator>"+TakeOverIndicator+"</take_over_indicator><consider_for_obligation>"+consider_for_obligation+"</consider_for_obligation><duplicate_flag>"+Duplicate_flag+"</duplicate_flag></ExternalBureauIndividualProducts>";
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
		Date currentDate=new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String modifiedDate= sdf.format(currentDate);
		String close_date= new PLCommon().plusyear(modifiedDate,4,0,0);
	
		PersonalLoanS.mLogger.info("ExternalBureauIndividualProducts list size"+Man_liab_row_count+ "");
		if (Man_liab_row_count !=0){
			for (int i = 0; i<Man_liab_row_count;i++){
				String Type_of_Contract = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 0); //0
				String Limit = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 1); //0
				String EMI = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 2); //0
				String cac_Indicator = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 5); //0
				String Qc_amt = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 6); //0
				String Qc_Emi = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 7); //0
				String worst_ststus=formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 17);
				if ("true".equalsIgnoreCase(cac_Indicator)){
					cac_Indicator="Y";
				}
				else {
					cac_Indicator="N";
				}
				String consider_for_obligation =NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 8))?"Y":"N"; //0
				
				String Utilization = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 11); //0
				String OutStanding = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 12); //0
				String mob = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 10);
				String delinquent_in_last_3months = NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 13))?"1":"0";
				String dpd_30_last_6_mon = NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 14))?"1":"0";
				String dpd_60p_in_last_18_mon = NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 15))?"1":"0";
				add_xml_str = add_xml_str + "<ExternalBureauIndividualProducts><applicant_id>"+applicant_id+"</applicant_id>";
				add_xml_str = add_xml_str + "<external_bureau_individual_products_id></external_bureau_individual_products_id>";
				add_xml_str = add_xml_str + "<contract_type>"+Type_of_Contract+"</contract_type>";
				add_xml_str = add_xml_str + "<provider_no></provider_no>";
				add_xml_str = add_xml_str + "<phase>A</phase>";
				add_xml_str = add_xml_str + "<role_of_customer>A</role_of_customer>";
				add_xml_str = add_xml_str + "<start_date>"+modifiedDate+"</start_date>"; 

				add_xml_str = add_xml_str + "<close_date>"+close_date+"</close_date>";
				add_xml_str = add_xml_str + "<outstanding_balance>"+OutStanding+"</outstanding_balance>";
				if (!Type_of_Contract.equalsIgnoreCase("01")){
					add_xml_str = add_xml_str + "<total_amount>"+Limit+"</total_amount>";
					}
					add_xml_str = add_xml_str + "<payments_amount>"+EMI+"</payments_amount>";
					add_xml_str = add_xml_str + "<total_no_of_instalments></total_no_of_instalments>";
					add_xml_str = add_xml_str + "<no_of_remaining_instalments></no_of_remaining_instalments>";
					add_xml_str = add_xml_str + "<worst_status>"+worst_ststus+"</worst_status>";
					add_xml_str = add_xml_str + "<worst_status_date></worst_status_date>";
					if (Type_of_Contract.equalsIgnoreCase("01")){
					add_xml_str = add_xml_str + "<credit_limit>"+Limit+"</credit_limit>";
					}add_xml_str = add_xml_str + "<overdue_amount></overdue_amount>";
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
				add_xml_str = add_xml_str + "<qc_amount>"+Qc_amt+"</qc_amount><company_flag>N</company_flag><consider_for_obligation>"+consider_for_obligation+"</consider_for_obligation><duplicate_flag>N</duplicate_flag></ExternalBureauIndividualProducts>";

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
		String sQuery = "select AgreementId,ProviderNo,LoanType,LoanDesc,CustRoleType,Datelastupdated,TotalAmt,TotalNoOfInstalments,'' as col1,NoOfDaysInPipeline,isnull(Consider_For_Obligations,'true'),case when IsDuplicate= '1' then 'Y' else 'N' end from ng_rlos_cust_extexpo_LoanDetails where child_wi  =  '"+formObject.getWFWorkitemName()+"' and LoanStat = 'Pipeline' union select CardEmbossNum,ProviderNo,CardTypeDesc,CardType,CustRoleType,LastUpdateDate,'' as col2,NoOfInstallments,TotalAmount,NoOfDaysInPipeLine,isnull(Consider_For_Obligations,'true'),case when IsDuplicate= '1' then 'Y' else 'N' end  from ng_rlos_cust_extexpo_CardDetails where child_wi  =  '"+formObject.getWFWorkitemName()+"' and cardstatus = 'Pipeline'";
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
			String ppl_Duplicate_flag="";
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
			if(OutputXML.get(i).get(11)!=null && !OutputXML.get(i).get(11).isEmpty() &&  !"".equalsIgnoreCase(OutputXML.get(i).get(11)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(11)) ){
				ppl_Duplicate_flag = OutputXML.get(i).get(11);
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

			add_xml_str = add_xml_str + "<ppl_no_of_days_in_pipeline>"+noOfDayinPpl+"</ppl_no_of_days_in_pipeline><company_flag>N</company_flag><consider_for_obligation>"+consider_for_obligation+"</consider_for_obligation><ppl_duplicate_flag>"+ppl_Duplicate_flag+"</ppl_duplicate_flag></ExternalBureauPipelineProducts>"; // to be populated later




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

	public String getCustOECD_details(FormReference formObject,String opName){
		PersonalLoanS.mLogger.info("RLOSCommon java file"+ "inside getCustOECD_details : ");
		String  add_xml_str ="";
		try{
			int add_row_count = formObject.getLVWRowCount("cmplx_OECD_cmplx_GR_OecdDetails");
			PersonalLoanS.mLogger.info("RLOSCommon java file"+ "inside getCustAddress_details add_row_count+ : "+add_row_count);
			String multipleApplicantName="";
			if( "SUPPLEMENT".equalsIgnoreCase(opName) || ("PRIMARY".equalsIgnoreCase(opName) && "CC_Disbursal".equalsIgnoreCase(formObject.getWFActivityName())))
			{
				multipleApplicantName=formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),4);
			}
			else if("GUARANTOR".equalsIgnoreCase(opName)  || ("PRIMARY".equalsIgnoreCase(opName) && "Disbursal".equalsIgnoreCase(formObject.getWFActivityName())))
			{
				multipleApplicantName=formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),1);
			}
			PersonalLoanS.mLogger.info("multipleApplicantName: "+multipleApplicantName);
	
			for (int i = 0; i<add_row_count;i++){
				String City_of_Birth = formObject.getNGValue("cmplx_OECD_cmplx_GR_OecdDetails", i, 3); //0
				String Country_of_Birth=formObject.getNGValue("cmplx_OECD_cmplx_GR_OecdDetails", i, 2);//1
				String Undocumented_Flag=formObject.getNGValue("cmplx_OECD_cmplx_GR_OecdDetails", i, 0);//2
				String UndocumentedFlag_Reason=formObject.getNGValue("cmplx_OECD_cmplx_GR_OecdDetails", i, 1);//3
				String applicantName=formObject.getNGValue("cmplx_OECD_cmplx_GR_OecdDetails", i, 8);//3
				
				if(applicantName.split("-")[1].equalsIgnoreCase(multipleApplicantName)){
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

			if(NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 10)))//10
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

					String empttype=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5);
					if(empttype!=null){	
						if ("Salaried".equalsIgnoreCase(empttype)){
							empttype="S";
						}
						else if (NGFUserResourceMgr_PL.getGlobalVar("PL_SalariedPensioner").equalsIgnoreCase(empttype)){
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
					List<List<String>> squerycurrempXML=formObject.getNGDataFromDataCache(squerycurremp);
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

					String squeryloan="select isNull(PreviousLoanDBR,0), isNull(PreviousLoanEmp,0), isNull(PreviousLoanMultiple,0), isNull(PreviousLoanTAI,0) from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where Request_Type='CollectionsSummary' and Limit_Increase='true'  and Child_wi= '"+formObject.getWFWorkitemName()+"'";
					List<List<String>> prevLoan=formObject.getNGDataFromDataCache(squeryloan);
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
					List<List<String>> NOC=formObject.getNGDataFromDataCache(squerynoc);
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
					List<List<String>> NOC=formObject.getNGDataFromDataCache(squerynoc);
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
					List<List<String>> Blacklist=formObject.getNGDataFromDataCache(squeryBlacklist);
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
					List<List<String>> Blacklist=formObject.getNGDataFromDataCache(squeryBlacklist);
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
				
				else if("borrowing_customer".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("RLOS iNSIDE borrowing_customer+ ");
					String squeryBorrow="select distinct(borrowingCustomer) from ng_RLOS_CUSTEXPOSE_CardDetails WHERE  child_wi ='"+formObject.getWFWorkitemName()+"' union select distinct(borrowingCustomer) from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock)  WHERE child_wi ='"+formObject.getWFWorkitemName()+"'";
					PersonalLoanS.mLogger.info("RLOS COMMONiNSIDE borrowing_customer query+ "+squeryBorrow);
					List<List<String>> borrowing_customer=formObject.getNGDataFromDataCache(squeryBorrow);
					if (borrowing_customer!=null && !borrowing_customer.isEmpty()){
						String xml_str =  int_xml.get(parent_tag);
						xml_str = xml_str+ "<"+tag_name+">"+borrowing_customer.get(0).get(0)
						+"</"+ tag_name+">";

						PersonalLoanS.mLogger.info("after adding borrowing_customer+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}
				}
				
				else if("funding_pattern".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("RLOS iNSIDE FundingPattern+ ");
					String squeryfund="select distinct(FundingPattern) from ng_RLOS_CUSTEXPOSE_AcctDetails WHERE  Child_wi ='"+formObject.getWFWorkitemName()+"'";
					PersonalLoanS.mLogger.info("RLOS COMMONiNSIDE FundingPattern query+ "+squeryfund);
					List<List<String>> funding_pattern=formObject.getNGDataFromDataCache(squeryfund);
					if (funding_pattern!=null && !funding_pattern.isEmpty()){
						String xml_str =  int_xml.get(parent_tag);
						xml_str = xml_str+ "<"+tag_name+">"+funding_pattern.get(0).get(0)
						+"</"+ tag_name+">";

						PersonalLoanS.mLogger.info("after adding funding_pattern+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}
				}
				
				else if("nmf_flag".equalsIgnoreCase(tag_name)){
					String nmfQuery="";
					int Comp_row_count = formObject.getLVWRowCount("cmplx_CompanyDetails_cmplx_CompanyGrid");
					if(Comp_row_count!=0){	
						for (int i = 0; i<Comp_row_count;i++){
							if (formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 2).equalsIgnoreCase("Secondary")){
								String emp_name = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 0); //0
								nmfQuery="select count(*) from NG_Master_NMF_not_listed_Comp  with (nolock) where comp_name='"+emp_name+"'";
							}
							}
						}
					else{
						nmfQuery="select count(*) from NG_Master_NMF_not_listed_Comp with (nolock) where comp_name='"+formObject.getNGValue("cmplx_EmploymentDetails_EmpName")+"'";
						}
					PersonalLoanS.mLogger.info("RLOS iNSIDE nmf_flag+ ");
					PersonalLoanS.mLogger.info("RLOS COMMONiNSIDE borrowing_customer query+ "+nmfQuery);
					List<List<String>> nmfQueryData=formObject.getNGDataFromDataCache(nmfQuery);
					if (nmfQueryData!=null && !nmfQueryData.isEmpty()){
						
						String NMFflag="";
						if (nmfQueryData.get(0).get(0).equalsIgnoreCase("0")){
							NMFflag="N";
						}
						else {
							NMFflag="Y";
						}
		                String xml_str =  int_xml.get(parent_tag);
						xml_str = xml_str+ "<"+tag_name+">"+NMFflag+"</"+ tag_name+">";

						PersonalLoanS.mLogger.info("after adding nmfQueryData+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
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
					List<List<String>> NOC=formObject.getNGDataFromDataCache(squerynoc);
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
				else if("aggregate_exposed".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info(" Inside Aggregate ");
					if(int_xml.containsKey(parent_tag))
					{
						formObject.saveFragment("EligibilityAndProductInformation");
						String aeQuery = " select (select isNull((Sum(convert(float,replace([TotalOutstandingAmt],'NA','0')))),0) as TotalOutstandingAmt from ng_RLOS_CUSTEXPOSE_loanDetails with (nolock) where Consider_For_Obligations='true' and LoanStat in ('A','ACTIVE') and child_wi ='"+formObject.getWFWorkitemName()+"') + (select isNull((Sum(convert(float,replace([TotalOutstandingAmt],'NA','0')))),0) as TotalOutstandingAmt from ng_RLOS_CUSTEXPOSE_loanDetails with (nolock) where Consider_For_Obligations='true' and LoanStat ='Pipeline' and child_wi ='"+formObject.getWFWorkitemName()+"') +(select isNull((Sum(convert(float,replace([OutstandingAmt],'NA','0')))),0)  from ng_RLOS_CUSTEXPOSE_CardDetails with (nolock) where  Consider_For_Obligations='true' and CardStatus in ('A','ACTIVE') and child_wi  ='"+formObject.getWFWorkitemName()+"' and SchemeCardProd like '%LOC%') + (select isNull((Sum(convert(float,replace([CreditLimit],'NA','0')))),0) as OutstandingAmt from ng_RLOS_CUSTEXPOSE_CardDetails with (nolock) where  Consider_For_Obligations='true' and CardStatus in ('A','ACTIVE') and child_wi  ='"+formObject.getWFWorkitemName()+"' and SchemeCardProd not like '%LOC%')+( select isNull((Sum(convert(float,replace([final_limit],'NA','0')))),0) from ng_rlos_EligAndProdInfo with (nolock) where wi_name ='"+formObject.getWFWorkitemName()+"') as aggregateExposure";
						PersonalLoanS.mLogger.info(" Inside Aggregate "+aeQuery);

						List<List<String>> aggregate_exposed = formObject.getNGDataFromDataCache(aeQuery);


					
						String aggr_expo=aggregate_exposed.get(0).get(0);
						double aggreg=Double.parseDouble(aggr_expo);
						aggr_expo=String.format("%.2f", aggreg);


						formObject.setNGValue("cmplx_Liability_New_AggrExposure", aggr_expo);//changed by akshay on 25/9/17 as per point 2 of problem sheet
						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<"+tag_name+">"+aggr_expo+"</"+ tag_name+">";

						PersonalLoanS.mLogger.info(" after adding aggregate_exposed+ "+xml_str);
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
					String[] winum = formObject.getWFWorkitemName().split("-");
					String xml_str = int_xml.get(parent_tag);
					xml_str = "<"+tag_name+">"+winum[1]+"</"+ tag_name+">";

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
				String form_control = recordFileMap.get("form_control");
				PersonalLoanS.mLogger.info("CC_Integration_Input"+" CARD SERVICE custom function parent_tag: "+parent_tag+" tag_name: "+tag_name);
				if("ReferenceDetails".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("inside 1st if"+" inside ReferenceDetails for Card NOTIF");
					if(int_xml.containsKey(parent_tag))
					{
						String xml_str = int_xml.get(parent_tag);
						PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding ReferenceDetails+ "+xml_str);
						xml_str = xml_str + getReference_details();
						PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding ReferenceDetails+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}
					
				}
				else if("AddrDet".equalsIgnoreCase(tag_name) || "AddressDetails".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("inside 1st if"+" inside customer update req1");
					if(int_xml.containsKey(parent_tag))
					{
						PersonalLoanS.mLogger.info("inside 1st if"+" inside customer update req2");
						String xml_str = int_xml.get(parent_tag);
						PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding address+ "+xml_str);
						xml_str = xml_str + getCustAddress_details(callName);
						PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding address+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}		                            	
				}
				else if(tag_name.equalsIgnoreCase("InterestProfile")||tag_name.equalsIgnoreCase("FeeProfile")||tag_name.equalsIgnoreCase("TransactionFeeProfile")){
					PersonalLoanS.mLogger.info("inside 1st if inside ReferenceDetails for Card NOTIF");
					if(int_xml.containsKey(parent_tag))
					{
						try{
							String xml_str = int_xml.get(parent_tag);
							PersonalLoanS.mLogger.info("RLOS COMMON before adding ReferenceDetails+ "+xml_str);
							String intrest_val = "";
							String intrest_Query = "";
							String card_product = formObject.getNGValue("CC_Creation_Product");
							if(card_product==null|| card_product.equals("")){
								intrest_val="NA";
							} 
							else{
								if(tag_name.equalsIgnoreCase("InterestProfile")){
									intrest_Query="select code from NG_MASTER_Int_Profile where Product = '"+card_product+"'";
								}
								else if(tag_name.equalsIgnoreCase("FeeProfile")||tag_name.equalsIgnoreCase("FeeProfileSerNo")){
									intrest_Query="select code from NG_MASTER_Fee_Profile where Product = '"+card_product+"'";
								}
								else if(tag_name.equalsIgnoreCase("TransactionFeeProfile")||tag_name.equalsIgnoreCase("TxnProfileSerNo")){
									intrest_Query="select code from NG_MASTER_TransactionFee_Profile where Product = '"+card_product+"'";
								}
								List<List<String>> intsert_list = formObject.getNGDataFromDataCache(intrest_Query);
								if(!intsert_list.isEmpty()){
									intrest_val = intsert_list.get(0).get(0);
								}	
							}

							xml_str = xml_str+"<"+tag_name+">"+intrest_val+"</"+ tag_name+">";
							PersonalLoanS.mLogger.info("RLOS COMMON after adding ReferenceDetails+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}
						catch(Exception e){
							PersonalLoanS.mLogger.info("CC Common Exception occured while setting intrest data");
						}
					}		                            	
				}
				else if(tag_name.equalsIgnoreCase("ContactDetails")){
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
				
				else if(tag_name.equalsIgnoreCase("ApplicationNumber") || tag_name.equalsIgnoreCase("InstitutionID")){
					PersonalLoanS.mLogger.info("inside 1st if inside customer update req1");
					String xml_str = int_xml.get(parent_tag);

					xml_str = xml_str+"<"+tag_name+">"+formObject.getWFWorkitemName().substring(5,14)+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("CC COMMON  after adding ApplicationNumber:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}


				 if( "PRIMARY".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13)))
				 {
				 if("RVCStatus".equalsIgnoreCase(tag_name)){
					String xml_str = int_xml.get(parent_tag);
					String RVC_status = (formObject.getNGValue(form_control).equalsIgnoreCase("true")?"Y":"N");;
					
					xml_str = xml_str + "<"+tag_name+">"+RVC_status+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding RVCStatus+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
			else if("DispatchMode".equalsIgnoreCase(tag_name)){
				//String xml_str = int_xml.get(parent_tag);
				String Dispatch_details = formObject.getNGValue(form_control);
				String Branch_details="";
				if(Dispatch_details.equalsIgnoreCase("ByBranch")){
					Branch_details = "<BranchDetails><BranchCode>"+formObject.getNGValue("AlternateContactDetails_CustdomBranch")+"</BranchCode></BranchDetails>";
				}
				String xml_str =  "<"+tag_name+">"+Dispatch_details+"</"+ tag_name+">"+Branch_details;

				PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding RVCStatus+ "+xml_str);
				int_xml.put(parent_tag, xml_str);
			}
				else if(tag_name.equalsIgnoreCase("IsNTB") || tag_name.equalsIgnoreCase("VIPFlag") || tag_name.equalsIgnoreCase("VIPFlg") || tag_name.equalsIgnoreCase("NonResidentFlag")||tag_name.equalsIgnoreCase("EStatementFlag") ){
					String xml_str = int_xml.get(parent_tag);
					String tagValue="";
					
					if(tag_name.equalsIgnoreCase("IsNTB")){
						tagValue=(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true")?"Y":"N");
					}
					/*else if((tag_name.equalsIgnoreCase("VIPFlag") || tag_name.equalsIgnoreCase("VIPFlg"))&&Call_name.equalsIgnoreCase("NEW_CREDITCARD_REQ")){
						tagValue=(formObject.getNGValue("cmplx_Customer_VIPFlag").equalsIgnoreCase("true")?"1":"0");
					}*/
					else if((tag_name.equalsIgnoreCase("VIPFlag") || tag_name.equalsIgnoreCase("VIPFlg"))&& !callName.equalsIgnoreCase("NEW_CREDITCARD_REQ")){
						tagValue=(formObject.getNGValue("cmplx_Customer_VIPFlag").equalsIgnoreCase("true")?"Y":"N");
					}
					else if(tag_name.equalsIgnoreCase("NonResidentFlag")){
						tagValue=(formObject.getNGValue("cmplx_Customer_ResidentNonResident").equalsIgnoreCase("Resident")?"Y":"N");
					}	
					else if(tag_name.equalsIgnoreCase("EStatementFlag")){
						tagValue=(formObject.getNGValue("AlternateContactDetails_eStatementFlag").equalsIgnoreCase("Yes")?"Y":"N");
					}
					
					if(!tagValue.equals("")){
						xml_str = xml_str + "<"+tag_name+">"+tagValue+"</"+ tag_name+">";
					}
					
					
					PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else if(tag_name.equalsIgnoreCase("SecurityChequeDetails") ){
					PersonalLoanS.mLogger.info("inside Generate XML inside SecurityChequeDetails");
					if(int_xml.containsKey(parent_tag))
					{						
						String xml_str = int_xml.get(parent_tag);
						PersonalLoanS.mLogger.info("RLOS COMMON before adding cheque+ "+xml_str);
						xml_str = xml_str + getCheque_details(callName);
						PersonalLoanS.mLogger.info("RLOS COMMON after adding cheque+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}		                            	
				}
				
				else if(tag_name.equalsIgnoreCase("LienDetails")){
					PersonalLoanS.mLogger.info("inside 1st if inside customer update req1");
					if(int_xml.containsKey(parent_tag))
					{
						PersonalLoanS.mLogger.info("inside 1st if"+"inside customer update req2");
						String xml_str = int_xml.get(parent_tag);
						PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding LienDetails+ "+xml_str);
						xml_str = xml_str + getLien_details(callName);
						PersonalLoanS.mLogger.info("RLOS COMMON after adding LienDetails+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}		                            	
				}
				
				else if(tag_name.equalsIgnoreCase("ResidenceSince"))
				{
					PersonalLoanS.mLogger.info("inside 1st if inside customer update req1");
					String xml_str = int_xml.get(parent_tag);
					int size=formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
					/*ring residenceSince="0";
					for(int i=0;i<size;i++){
						if(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,0).equalsIgnoreCase("RESIDENCE")){
							residenceSince=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,9);
							PersonalLoanS.mLogger.info("inside ResidenceSince Value of Years in RESD Address: "+residenceSince);
						}
					}
					SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.YEAR, -Integer.parseInt(residenceSince));
					String residenceSince_date=sdf1.format(cal.getTime());
					PersonalLoanS.mLogger.info("inside ResidenceSince Value of residenceSince_date: "+residenceSince_date);*/
					xml_str = xml_str + "<"+tag_name+">"+calcResidenceSince(formObject)+"</"+ tag_name+">";
					int_xml.put(parent_tag, xml_str);								
				}
				
				
				else if(tag_name.equalsIgnoreCase("SalaryDate") ){
					PersonalLoanS.mLogger.info("inside 1st if inside customer update req1");
					String xml_str = int_xml.get(parent_tag);
					Calendar now = Calendar.getInstance();
					String month = "";
					String day = "";
					if((now.get(Calendar.MONTH) + 1)<10){
						month = "0"+(now.get(Calendar.MONTH) + 1);
					}else{
						month = ""+(now.get(Calendar.MONTH) + 1);
					}
					if(formObject.getNGValue("cmplx_IncomeDetails_SalaryDay").length()<2){
						day = "0" + formObject.getNGValue("cmplx_IncomeDetails_SalaryDay");
					}else{
						day = formObject.getNGValue("cmplx_IncomeDetails_SalaryDay");
					}


					String Current_date="";

					Current_date = now.get(Calendar.YEAR)+"-"+month+"-"+day;
					xml_str = xml_str+"<"+tag_name+">"+Current_date+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("PL COMMON  after adding ApplicationID:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}
				else if(tag_name.equalsIgnoreCase("MinorFlag") ){
					if(int_xml.containsKey(parent_tag))
					{	//change from int to float by saurabh on 16th dec
						float Age = Float.parseFloat(formObject.getNGValue("cmplx_Customer_age"));
						String age_flag = "N";
						if(Age<18)
							age_flag="Y";
						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<"+tag_name+">"+age_flag
						+"</"+ tag_name+">";

						PersonalLoanS.mLogger.info("RLOS COMMON  after adding Minor flag+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}		                            	
				}
				
				
				//below code by saurabh on 15th Dec 17
				else if(tag_name.equalsIgnoreCase("FinancialServices") ){
					PersonalLoanS.mLogger.info("inside Generate XML inside FinancialServices");
					if(int_xml.containsKey(parent_tag))
					{						
						String xml_str = int_xml.get(parent_tag);
						PersonalLoanS.mLogger.info("Cc_integration_input before adding financialservices+ "+xml_str);
						xml_str = xml_str + getFinancialServices_details(callName);
						PersonalLoanS.mLogger.info("Cc_integration_input after adding financialservices+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}		                            	
				}
				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
				}
			}
				 else if( "SUPPLEMENT".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13)))
				 {
					
					 if("CIFID".equalsIgnoreCase(tag_name) ){
						 PersonalLoanS.mLogger.info("inside 1st if inside customer update req");
							String xml_str = int_xml.get(parent_tag);
								xml_str =xml_str+ "<"+tag_name+">"+formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),3)+"</"+ tag_name+">";
								PersonalLoanS.mLogger.info("PL COMMON  after adding CIFId:  "+xml_str);
							int_xml.put(parent_tag, xml_str);	                            	
						}
					 else if("PersonDetails".equalsIgnoreCase(tag_name)){
							String xml_str = int_xml.get(parent_tag);
							PersonalLoanS.mLogger.info("RLOS COMMON"+ " before adding supplement PersonDetails+ " + xml_str);
							xml_str = xml_str + getSupplement_PersonDetails(formObject,callName);
							PersonalLoanS.mLogger.info("RLOS COMMON"+ " after adding PersonDetails " + xml_str);
							int_xml.put(parent_tag, xml_str);
						
						}
					 
					 else if("DocumentDet".equalsIgnoreCase(tag_name)){
							String xml_str = int_xml.get(parent_tag);
							PersonalLoanS.mLogger.info("RLOS COMMON"+ " before adding supplement DocumentDet+ " + xml_str);
							xml_str = xml_str + getSupplement_DocDetails(formObject,callName);
							PersonalLoanS.mLogger.info("RLOS COMMON"+ " after adding PersonDetails " + xml_str);
							int_xml.put(parent_tag, xml_str);
						
						}
					 
					 else if("EmploymentType".equalsIgnoreCase(tag_name) || "Designation".equalsIgnoreCase(tag_name) || "MarketingCode".equalsIgnoreCase(tag_name)){
							
							String xml_str = int_xml.get(parent_tag);
							String status="",Occupation="",marketCode="";
							PersonalLoanS.mLogger.info("RLOS COMMON"+ " before adding guarantor EmploymentType " + xml_str);
								 for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid");i++){
									 if(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),14).equals(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,3))){
										 	status=formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,32);
										 	Occupation=formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,31);
										 	marketCode=formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,26);
										 	break;
									}
								}
							
								if("EmploymentType".equalsIgnoreCase(tag_name)){
										xml_str = xml_str +  "<"+tag_name+">"+status+"</"+ tag_name+">";
								}
								else if("Designation".equalsIgnoreCase(tag_name)){
									xml_str = xml_str +  "<"+tag_name+">"+Occupation+"</"+ tag_name+">";
								}
								else if("MarketingCode".equalsIgnoreCase(tag_name)){
									xml_str = xml_str +  "<"+tag_name+">"+marketCode+"</"+ tag_name+">";
								}
								PersonalLoanS.mLogger.info("RLOS COMMON"+ " after adding supplement" + tag_name+" :"+xml_str);
							int_xml.put(parent_tag, xml_str);
						}
						
						else{
							int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
						}
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


		Date Modified                       : 15/12/2017              
		Author                              : Saurabh Gupta              
		Description                         : get Financial Services details for Card Notification       

	 ***********************************************************************************  */
	public String getFinancialServices_details(String Call_name){
		PersonalLoanS.mLogger.info("CC_Integration_Input java file inside getFinancialServices_details : ");
		String  Financial_xml_str ="";
		try{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String listViewname = "cmplx_CC_Loan_cmplx_btc";
		int rowCount = formObject.getLVWRowCount(listViewname);
		if(rowCount>0){
			UIComponent pComp =formObject.getComponent("cmplx_CC_Loan_cmplx_btc");
			int columns=0;
			List<String> gridColumnNames = new ArrayList<String>();
			List<String> transTypes =  new ArrayList<String>();
			if( pComp != null && pComp instanceof ListView )
			{			
				ListView objListView = ( ListView )pComp;
				columns  = objListView.getChildCount();
				for(int i=0;i<columns;i++){
					gridColumnNames.add(((Column)(pComp.getChildren().get(i))).getName());
				}
				for(int j=0;j<rowCount;j++){
					transTypes.add(formObject.getNGValue(listViewname, j, gridColumnNames.indexOf("Transaction Type")));
				}
			}
			
			int chosenRow = 0;//currently 0 for testing but has to be -1.
			String subProduct = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2);
			String appType = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,4);
			if(subProduct.equalsIgnoreCase("IM") && appType.contains("NEW")){
				chosenRow = transTypes.indexOf("LOC");
			}
			else if(subProduct.equalsIgnoreCase("BTC")){
				chosenRow = transTypes.indexOf("BT");
			}
			if(chosenRow!=-1){
				String serviceType = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Transaction Type"));
				String amount = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Amount"));
				String cc_no = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("CreditCard No"));
				String benefName = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Beneficiary name"));
				String payMode = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Transfer Mode"));
				//String paymentPurpose = "PSC";
				String dispChanel = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Dispatch Channel"));
				String servMarkCode = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Marketing code"));
				String servAppCode = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Approval code"));
				String servSourceCode = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Source Code"));
				String nameOnCard = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Name on card"));
				String trfDate = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Date"));
				String tenor = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Tenor"));
				String appstatus = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Application Status"));
				
				//String servAppCode = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Approval code"));
				//String bankName = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("bank Name"));
				String marketingCode = formObject.getNGValue("cmplx_EmploymentDetails_marketcode");
				
				String bankCode = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("bank Name"));
				formObject.setNGValue("bankName", bankCode);
				String bankName = formObject.getNGItemText("bankName", formObject.getSelectedIndex("bankName"));
				formObject.setNGValue("bankName", "");
				String branchName = formObject.getNGValue("AlternateContactDetails_CustdomBranch");
				String iban = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("IBAN"));
				String cardEmboss = cc_no;
				String custFullName = formObject.getNGValue("cmplx_Customer_Shortname");
				Financial_xml_str+="<FinancialServices>";
				if(Call_name.equalsIgnoreCase("CARD_NOTIFICATION")){
					if(checkValue(serviceType)){
						Financial_xml_str+="<ServiceType>"+serviceType+"</ServiceType>";
					}
					if(checkValue(amount)){
						Financial_xml_str+="<Amount>"+amount+"</Amount>";
					}
					if(checkValue(cc_no)){
						Financial_xml_str+="<CardNumber>"+cc_no+"</CardNumber>";
					}
					if(checkValue(benefName)){
						Financial_xml_str+="<BenificiaryName>"+benefName+"</BenificiaryName>";
					}
					if(checkValue(payMode)){
						Financial_xml_str+="<PaymentMode>"+payMode+"</PaymentMode>";
					}
					Financial_xml_str+="<PaymentPurpose>PSC</PaymentPurpose>";
					if(checkValue(dispChanel)){
						Financial_xml_str+="<DispatchChannel>"+dispChanel+"</DispatchChannel>";
					}
					if(checkValue(servMarkCode)){
						Financial_xml_str+="<ServiceMarketingCode>"+servMarkCode+"</ServiceMarketingCode>";
					}
					if(checkValue(servAppCode)){
						Financial_xml_str+="<ServiceApprovalCode>"+servAppCode+"</ServiceApprovalCode>";
					}
					if(checkValue(servSourceCode)){
						Financial_xml_str+="<ServiceSourceCode>"+servSourceCode+"</ServiceSourceCode>";
					}
					Financial_xml_str+="<OtherBankInfo>";
					if(checkValue(bankCode)){
						Financial_xml_str+="<BankCode>"+bankCode+"</BankCode>";
					}
					if(checkValue(iban)){
						Financial_xml_str+="<IBAN>"+iban+"</IBAN>";
					}
					if(checkValue(cardEmboss)){
						Financial_xml_str+="<CardEmbossNum>"+cardEmboss+"</CardEmbossNum>";
					}
					if(checkValue(custFullName)){
						Financial_xml_str+="<CustomerName>"+custFullName+"</CustomerName>";
					}
					Financial_xml_str+="</OtherBankInfo>";
				}
				else if(Call_name.equalsIgnoreCase("CARD_SERVICES_REQUEST")){
					if(checkValue(serviceType)){
						if(serviceType.equalsIgnoreCase("LOC")){
							serviceType = "LoanOnCard";
						}
						Financial_xml_str+="<RequestType>"+serviceType+"</RequestType>";
					}
					Financial_xml_str+="<ReferenceNumber>12345</ReferenceNumber>";
					if(checkValue(payMode)){
						Financial_xml_str+="<PaymentMode>"+payMode+"</PaymentMode>";
					}
					if(checkValue(bankCode)){
						Financial_xml_str+="<BankCode>"+bankCode+"</BankCode>";
					}
					if(checkValue(bankName)){
						Financial_xml_str+="<BankName>"+bankCode+"</BankName>";
					}
					if(checkValue(benefName)){
						Financial_xml_str+="<BenificiaryName>"+benefName+"</BenificiaryName>";
					}
					if(checkValue(cc_no)){
						Financial_xml_str+="<OtherBankCardNo>"+cc_no+"</OtherBankCardNo>";
					}
					if(checkValue(custFullName)){
						Financial_xml_str+="<CustomerName>"+custFullName+"</CustomerName>";
					}
					if(checkValue(nameOnCard)){
						Financial_xml_str+="<NameOnCard>"+nameOnCard+"</NameOnCard>";
					}
					if(checkValue(dispChanel)){
						Financial_xml_str+="<DispatchChannel>"+dispChanel+"</DispatchChannel>";
					}
					if(checkValue(branchName)){
						Financial_xml_str+="<BranchName>"+branchName+"</BranchName>";
					}
					if(checkValue(iban)){
						Financial_xml_str+="<IBAN>"+iban+"</IBAN>";
					}
					if(checkValue(amount)){
						Financial_xml_str+="<Amount>"+amount+"</Amount>";
					}
					if(checkValue(trfDate)){
						Financial_xml_str+="<TransferDate>"+new Common_Utils(PersonalLoanS.mLogger).Convert_dateFormat(trfDate, "dd/MM/yyyy","yyyy-MM-dd")+"</TransferDate>";
					}
					if(checkValue(tenor)){
						Financial_xml_str+="<Tenor>"+tenor+"</Tenor>";
					}
					if(checkValue(marketingCode)){
						Financial_xml_str+="<MarketingCode>"+marketingCode+"</MarketingCode>";
					}
					if(checkValue(appstatus)){
						Financial_xml_str+="<ApplicationStatus>"+appstatus+"</ApplicationStatus>";
					}
					if(checkValue(servAppCode)){
						Financial_xml_str+="<ApprovalCode>"+servAppCode+"</ApprovalCode>";
					}
				}
				Financial_xml_str+="</FinancialServices>";
			}
			
		}
		return Financial_xml_str;
		}catch(Exception ex){
			PersonalLoanS.mLogger.info("PL Common java file Exception occured in get lien details method : ");
			PLCommon.printException(ex);
			return "";
		}
	}
	
	public boolean checkValue(String ngValue){
		if(ngValue==null || "".equalsIgnoreCase(ngValue) || " ".equalsIgnoreCase(ngValue) || "--Select--".equalsIgnoreCase(ngValue)){
			return false;
		}
		return true;
	}

	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 10/12/2017              
		Author                              : Saurabh Gupta              
		Description                         : get cheque details for Card Notification       

	 ***********************************************************************************  */
	public String getCheque_details(String Call_name){
		PersonalLoanS.mLogger.info("RLOSCommon java file inside getCheque_details : ");
		String  cheque_xml_str ="";

		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			int cheque_row_count = formObject.getLVWRowCount("cmplx_CardDetails_cmpmx_gr_cardDetails");
			PersonalLoanS.mLogger.info("PL Common java file inside getCheque_details cheque_row_count+ : "+cheque_row_count);
			
			SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
			Date maxdate=sdf.parse(formObject.getNGValue("cmplx_CardDetails_cmpmx_gr_cardDetails", 0, 3));
			PersonalLoanS.mLogger.info("PL Common inside getCheque_details maxDate in seconds : "+maxdate);
			int maxindex=0;
			for(int i=1;i<cheque_row_count;i++)
			{				
				String date = formObject.getNGValue("cmplx_CardDetails_cmpmx_gr_cardDetails", i, 3); //0
				Date curr_date=sdf.parse(date);
				PersonalLoanS.mLogger.info("PL Common inside getCheque_details Grid date,curr_date is: "+date+" ,"+curr_date);

				if(curr_date.after(maxdate)){
					PersonalLoanS.mLogger.info("PL Common inside getCheque_details Current Date is after max date");
					maxdate=curr_date;
					maxindex=i;
				}
			}	
			
				String bankName = formObject.getNGValue("cmplx_CardDetails_cmpmx_gr_cardDetails", maxindex, 0); //0
				String cheqNo = formObject.getNGValue("cmplx_CardDetails_cmpmx_gr_cardDetails", maxindex, 1); //0
				String Amount = formObject.getNGValue("cmplx_CardDetails_cmpmx_gr_cardDetails", maxindex, 2); //0
				String chqDate = formObject.getNGValue("cmplx_CardDetails_cmpmx_gr_cardDetails", maxindex, 3); //0

				if(Call_name.equalsIgnoreCase("CARD_NOTIFICATION")){
				cheque_xml_str = cheque_xml_str+ "<SecurityChequeDetails><BankName>"+bankName+"</BankName>";
				cheque_xml_str = cheque_xml_str+ "<ChequeNo>"+cheqNo+"</ChequeNo>";
				cheque_xml_str = cheque_xml_str+ "<Amount>"+Amount+"</Amount>";
				cheque_xml_str = cheque_xml_str+ "<Date>"+new Common_Utils(PersonalLoanS.mLogger).Convert_dateFormat(chqDate, "dd/MM/yyyy","yyyy-MM-dd")+"</Date></SecurityChequeDetails>";
		}
		else if(Call_name.equalsIgnoreCase("CARD_SERVICES_REQUEST")){
			cheque_xml_str = cheque_xml_str+ "<SecurityChequeDetails><BankName>"+bankName+"</BankName>";
			cheque_xml_str = cheque_xml_str+ "<ChequeNo>"+cheqNo+"</ChequeNo>";
			cheque_xml_str = cheque_xml_str+ "<ChequeAmount>"+Amount+"</ChequeAmount>";
			cheque_xml_str = cheque_xml_str+ "<ChequeDate>"+new Common_Utils(PersonalLoanS.mLogger).Convert_dateFormat(chqDate, "dd/MM/yyyy","yyyy-MM-dd")+"</ChequeDate></SecurityChequeDetails>";
	
		}
				
				PersonalLoanS.mLogger.info("PL Common cheque_xml_str after adding cheque Details : "+cheque_xml_str);

			
			/*else{
				CreditCard.mLogger.info("CC Common cheque_xml_str NO record found in Grid");
			}*/
				
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("PL Common java file Exception occured in get lien details method : "+e.getMessage());
		}

		return cheque_xml_str;

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
					if((now.get(Calendar.MONTH) + 1)<10){
						month = "0"+(now.get(Calendar.MONTH) + 1);
					}else{
						month = ""+Integer.toString((now.get(Calendar.MONTH) + 1));
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


		Date Modified                       : 10/12/2017              
		Author                              : Saurabh Gupta              
		Description                         : CARD_SERVICES_REQUEST_Custom       

	 ***********************************************************************************  */
	private Map<String, String> NEW_PersonalLoanS_Custom(List<List<String>> DB_List,FormReference formObject,String callName) {

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
				String form_control = recordFileMap.get("form_control");
				
				PersonalLoanS.mLogger.info("CC_Integration_Input"+" CARD SERVICE custom function parent_tag: "+parent_tag+" tag_name: "+tag_name);
				if("ReferenceDetails".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("inside 1st if"+" inside ReferenceDetails for Card NOTIF");
					if(int_xml.containsKey(parent_tag))
					{
						String xml_str = int_xml.get(parent_tag);
						PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding ReferenceDetails+ "+xml_str);
						xml_str = xml_str + getReference_details();
						PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding ReferenceDetails+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}
					
				}
				else if("AddrDet".equalsIgnoreCase(tag_name) || "AddressDetails".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("inside 1st if"+" inside customer update req1");
					if(int_xml.containsKey(parent_tag))
					{
						PersonalLoanS.mLogger.info("inside 1st if"+" inside customer update req2");
						String xml_str = int_xml.get(parent_tag);
						PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding address+ "+xml_str);
						xml_str = xml_str + getCustAddress_details(callName);
						PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding address+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}		                            	
				}
				else if(tag_name.equalsIgnoreCase("FeeProfileSerNo")||tag_name.equalsIgnoreCase("TxnProfileSerNo")){
					PersonalLoanS.mLogger.info("inside 1st if"+" inside ReferenceDetails for Card NOTIF");
					if(int_xml.containsKey(parent_tag))
					{
						try{
							String xml_str = int_xml.get(parent_tag);
							PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding ReferenceDetails+ "+xml_str);
							String intrest_val = "";
							String intrest_Query = "";
							String card_product = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5);
							if(card_product==null|| card_product.equals("")){
								intrest_val="NA";
							} 
							else{
								if(tag_name.equalsIgnoreCase("InterestProfile")){
									intrest_Query="select code from NG_MASTER_Int_Profile where Product = '"+card_product+"'";
								}
								else if(tag_name.equalsIgnoreCase("FeeProfile")||tag_name.equalsIgnoreCase("FeeProfileSerNo")){
									intrest_Query="select code from NG_MASTER_Fee_Profile where Product = '"+card_product+"'";
								}
								else if(tag_name.equalsIgnoreCase("TransactionFeeProfile")||tag_name.equalsIgnoreCase("TxnProfileSerNo")){
									intrest_Query="select code from NG_MASTER_TransactionFee_Profile where Product = '"+card_product+"'";
								}
								List<List<String>> intsert_list = formObject.getNGDataFromDataCache(intrest_Query);
								if(!intsert_list.isEmpty()){
									intrest_val = intsert_list.get(0).get(0);
								}	
							}

							xml_str = xml_str+"<"+tag_name+">"+intrest_val+"</"+ tag_name+">";
							PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding ReferenceDetails+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}
						catch(Exception e){
							PersonalLoanS.mLogger.info("CC Common"+ "Exception occured while setting intrest data");
						}
					}		                            	
				}
				else if(tag_name.equalsIgnoreCase("CIFId")){
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<"+tag_name+">C"+formObject.getNGValue(form_control)+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding BankName+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else if(tag_name.equalsIgnoreCase("IsNTB") || tag_name.equalsIgnoreCase("VIPFlag") || tag_name.equalsIgnoreCase("VIPFlg") || tag_name.equalsIgnoreCase("NonResidentFlag")||tag_name.equalsIgnoreCase("EStatementFlag")){
					String xml_str = int_xml.get(parent_tag);
					String tagValue="";
					if(tag_name.equalsIgnoreCase("IsNTB")){
						tagValue=(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true")?"-1":"0");
					}
					else if(tag_name.equalsIgnoreCase("IsNTB")){
						tagValue=(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true")?"Y":"N");
					}
					/*else if((tag_name.equalsIgnoreCase("VIPFlag") || tag_name.equalsIgnoreCase("VIPFlg"))&&Call_name.equalsIgnoreCase("NEW_PersonalLoanS_REQ")){
						tagValue=(formObject.getNGValue("cmplx_Customer_VIPFlag").equalsIgnoreCase("true")?"1":"0");
					}*/
					else if(tag_name.equalsIgnoreCase("VIPFlag") || tag_name.equalsIgnoreCase("VIPFlg")){
						tagValue=(formObject.getNGValue("cmplx_Customer_VIPFlag").equalsIgnoreCase("true")?"Y":"N");
					}
					else if(tag_name.equalsIgnoreCase("NonResidentFlag")){
						tagValue=(formObject.getNGValue("cmplx_Customer_ResidentNonResident").equalsIgnoreCase("Resident")?"Y":"N");
					}	
					else if(tag_name.equalsIgnoreCase("EStatementFlag")){
						tagValue=(formObject.getNGValue("AlternateContactDetails_eStatementFlag").equalsIgnoreCase("Yes")?"Y":"N");
					}
					
					if(!tagValue.equals("")){
						xml_str = xml_str + "<"+tag_name+">"+tagValue+"</"+ tag_name+">";
					}
					
					
					PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				
				else if("MaritalStatus".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("inside 1st if"+"inside maritial NEW_PersonalLoanS_REQ");
					String xml_str = int_xml.get(parent_tag);
					String marital="5";
					if(formObject.getNGValue("cmplx_Customer_MAritalStatus").equalsIgnoreCase("M")){
						marital="2";
					}
					else if(formObject.getNGValue("cmplx_Customer_MAritalStatus").equalsIgnoreCase("S")){
						marital="1";
					}
					else if(formObject.getNGValue("cmplx_Customer_MAritalStatus").equalsIgnoreCase("D")){
						marital="3";
					}
					else if(formObject.getNGValue("cmplx_Customer_MAritalStatus").equalsIgnoreCase("W")){
						marital="4";
					}
					xml_str = xml_str+"<"+tag_name+">"+marital+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("PL COMMON"+" after adding maritia] NEW_PersonalLoanS_REQ:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}
				
				else if("StatementCycle".equalsIgnoreCase(tag_name)){
					String xml_str = int_xml.get(parent_tag);
					String statCycle=formObject.getNGValue("cmplx_IncomeDetails_StatementCycle");
					if(statCycle.length()==1)
						statCycle="-20"+statCycle;
					
					else if(statCycle.length()==2)
						statCycle="-2"+statCycle;

					xml_str = xml_str + "<"+tag_name+">"+statCycle+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				
				else if("BranchId".equalsIgnoreCase(tag_name) || "BankSortCode".equalsIgnoreCase(tag_name)){
					String xml_str = "";
					if(int_xml.containsKey(parent_tag)){
						xml_str = int_xml.get(parent_tag);
					}
					String CardDispatch=formObject.getNGValue("AlternateContactDetails_CardDisp");
					String tagValue="";
					
					if(CardDispatch!=null && CardDispatch.equalsIgnoreCase("ByCourier"))
						tagValue="998";
					else
						tagValue=formObject.getNGValue("AlternateContactDetails_CustdomBranch")!=null?formObject.getNGValue("AlternateContactDetails_CustdomBranch"):"";

					xml_str = xml_str+"<"+tag_name+">"+tagValue+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding bhrabc id flag+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				
				else if("ProcessedBy".equalsIgnoreCase(tag_name) ){
					String xml_str = int_xml.get(parent_tag);

					xml_str = xml_str + "<"+tag_name+">"+formObject.getUserName()
					+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else if("ApplicationNumber".equalsIgnoreCase(tag_name) || "InstitutionID".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("inside 1st if inside customer update req1");
					String xml_str = int_xml.get(parent_tag);
					
						xml_str = xml_str+"<"+tag_name+">"+formObject.getWFWorkitemName().substring(11,21)+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("CC COMMON  after adding ApplicationNumber:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}
				else if("Gender".equalsIgnoreCase(tag_name) ){
					PersonalLoanS.mLogger.info("inside 1st if inside customer update req1");
					String xml_str = int_xml.get(parent_tag);
					xml_str =xml_str+ "<"+tag_name+">"+"1"+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("PL COMMON  after adding DSAId:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}
			
				else if("ProcessDate".equalsIgnoreCase(tag_name) || "ApplicationDate".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("inside 1st if inside NEW_PersonalLoanS_REQ req1");
					String xml_str = int_xml.get(parent_tag);
					Date d1=new Date();
					String Date="";

					SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
					Date=sf.format(d1);

					//d2.parse(sf.format(d1));
					xml_str =xml_str+ "<"+tag_name+">"+Date+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("PL COMMON  after adding DSAId:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}

				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
				}
			}
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + "+e.getMessage());
			PLCommon.printException(e);
		}
		return int_xml;
	}
	
	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 10/12/2017              
		Author                              : Saurabh Gupta              
		Description                         : CARD_SERVICES_REQUEST_Custom       

	 ***********************************************************************************  */
	private Map<String, String> NEW_CREDITCARD_Custom(List<List<String>> DB_List,FormReference formObject,String callName) {

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
				String form_control = recordFileMap.get("form_control");
				
				PersonalLoanS.mLogger.info("CC_Integration_Input"+" CARD SERVICE custom function parent_tag: "+parent_tag+" tag_name: "+tag_name);
				if("ReferenceDetails".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("inside 1st if"+" inside ReferenceDetails for Card NOTIF");
					if(int_xml.containsKey(parent_tag))
					{
						String xml_str = int_xml.get(parent_tag);
						PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding ReferenceDetails+ "+xml_str);
						xml_str = xml_str + getReference_details();
						PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding ReferenceDetails+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}

				}
				else if("AddrDet".equalsIgnoreCase(tag_name) || "AddressDetails".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("inside 1st if"+" inside customer update req1");
					if(int_xml.containsKey(parent_tag))
					{
						PersonalLoanS.mLogger.info("inside 1st if"+" inside customer update req2");
						String xml_str = int_xml.get(parent_tag);
						PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding address+ "+xml_str);
						xml_str = xml_str + getCustAddress_details(callName);
						PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding address+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}		                            	
				}
				else if(tag_name.equalsIgnoreCase("FeeProfileSerNo")||tag_name.equalsIgnoreCase("TxnProfileSerNo")){
					PersonalLoanS.mLogger.info("inside 1st if"+" inside ReferenceDetails for Card NOTIF");
					if(int_xml.containsKey(parent_tag))
					{
						try{
							String xml_str = int_xml.get(parent_tag);
							PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding ReferenceDetails+ "+xml_str);
							String intrest_val = "";
							String intrest_Query = "";
							String card_product = formObject.getNGValue("CC_Creation_Product");
							if(card_product==null|| card_product.equals("")){
								intrest_val="NA";
							} 
							else{
								if(tag_name.equalsIgnoreCase("InterestProfile")){
									intrest_Query="select code from NG_MASTER_Int_Profile with (nolock) where Product = '"+card_product+"'";
								}
								else if(tag_name.equalsIgnoreCase("FeeProfile")||tag_name.equalsIgnoreCase("FeeProfileSerNo")){
									intrest_Query="select code from NG_MASTER_Fee_Profile with (nolock) where Product = '"+card_product+"'";
								}
								else if(tag_name.equalsIgnoreCase("TransactionFeeProfile")||tag_name.equalsIgnoreCase("TxnProfileSerNo")){
									intrest_Query="select code from NG_MASTER_TransactionFee_Profile with (nolock) where Product = '"+card_product+"'";
								}
								List<List<String>> intsert_list = formObject.getNGDataFromDataCache(intrest_Query);
								if(!intsert_list.isEmpty()){
									intrest_val = intsert_list.get(0).get(0);
								}	
							}

							xml_str = xml_str+"<"+tag_name+">"+intrest_val+"</"+ tag_name+">";
							PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding ReferenceDetails+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}
						catch(Exception e){
							PersonalLoanS.mLogger.info("CC Common"+ "Exception occured while setting intrest data");
						}
					}		                            	
				}
				
				else if("ProcessedBy".equalsIgnoreCase(tag_name) ){
					String xml_str = int_xml.get(parent_tag);

					xml_str = xml_str + "<"+tag_name+">"+formObject.getUserName()
					+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else if("ApplicationNumber".equalsIgnoreCase(tag_name) || "InstitutionID".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("inside 1st if inside customer update req1");
					String xml_str = int_xml.get(parent_tag);

					xml_str = xml_str+"<"+tag_name+">"+formObject.getWFWorkitemName().substring(11,21)+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("CC COMMON  after adding ApplicationNumber:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}
				
				else if("ProcessDate".equalsIgnoreCase(tag_name) || "ApplicationDate".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("inside 1st if inside NEW_CREDITCARD_REQ req1");
					String xml_str = int_xml.get(parent_tag);
					Date d1=new Date();
					String Date="";

					SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
					Date=sf.format(d1);

					//d2.parse(sf.format(d1));
					xml_str =xml_str+ "<"+tag_name+">"+Date+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("PL COMMON  after adding DSAId:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}

				 if( "PRIMARY".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13)))
				 {
				 if(tag_name.equalsIgnoreCase("CIFId")){
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<"+tag_name+">C"+formObject.getNGValue(form_control)+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding BankName+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else if(tag_name.equalsIgnoreCase("IsNTB") || tag_name.equalsIgnoreCase("VIPFlag") || tag_name.equalsIgnoreCase("VIPFlg") || tag_name.equalsIgnoreCase("NonResidentFlag")||tag_name.equalsIgnoreCase("EStatementFlag")){
					String xml_str = int_xml.get(parent_tag);
					String tagValue="";
					if(tag_name.equalsIgnoreCase("IsNTB")){
						tagValue=(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true")?"-1":"0");
					}
					else if(tag_name.equalsIgnoreCase("IsNTB")){
						tagValue=(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true")?"Y":"N");
					}
					/*else if((tag_name.equalsIgnoreCase("VIPFlag") || tag_name.equalsIgnoreCase("VIPFlg"))&&Call_name.equalsIgnoreCase("NEW_CREDITCARD_REQ")){
						tagValue=(formObject.getNGValue("cmplx_Customer_VIPFlag").equalsIgnoreCase("true")?"1":"0");
					}*/
					else if(tag_name.equalsIgnoreCase("VIPFlag") || tag_name.equalsIgnoreCase("VIPFlg")){
						tagValue=(formObject.getNGValue("cmplx_Customer_VIPFlag").equalsIgnoreCase("true")?"Y":"N");
					}
					else if(tag_name.equalsIgnoreCase("NonResidentFlag")){
						tagValue=(formObject.getNGValue("cmplx_Customer_ResidentNonResident").equalsIgnoreCase("Resident")?"Y":"N");
					}	
					else if(tag_name.equalsIgnoreCase("EStatementFlag")){
						tagValue=(formObject.getNGValue("AlternateContactDetails_eStatementFlag").equalsIgnoreCase("Yes")?"Y":"N");
					}
					
					if(!tagValue.equals("")){
						xml_str = xml_str + "<"+tag_name+">"+tagValue+"</"+ tag_name+">";
					}
					
					
					PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				
				else if("MaritalStatus".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("inside 1st if"+"inside maritial NEW_CREDITCARD_REQ");
					String xml_str = int_xml.get(parent_tag);
					String marital="5";
					if(formObject.getNGValue("cmplx_Customer_MAritalStatus").equalsIgnoreCase("M")){
						marital="2";
					}
					else if(formObject.getNGValue("cmplx_Customer_MAritalStatus").equalsIgnoreCase("S")){
						marital="1";
					}
					else if(formObject.getNGValue("cmplx_Customer_MAritalStatus").equalsIgnoreCase("D")){
						marital="3";
					}
					else if(formObject.getNGValue("cmplx_Customer_MAritalStatus").equalsIgnoreCase("W")){
						marital="4";
					}
					xml_str = xml_str+"<"+tag_name+">"+marital+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("PL COMMON"+" after adding maritia] NEW_CREDITCARD_REQ:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}

				/*else if("StatementCycle".equalsIgnoreCase(tag_name)){
					String xml_str = int_xml.get(parent_tag);
					String statCycle=formObject.getNGValue("cmplx_IncomeDetails_StatementCycle");
					if(statCycle.length()==1)
						statCycle="-20"+statCycle;
					
					else if(statCycle.length()==2)
						statCycle="-2"+statCycle;

					xml_str = xml_str + "<"+tag_name+">"+statCycle+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}*/

				else if("BranchId".equalsIgnoreCase(tag_name) || "BankSortCode".equalsIgnoreCase(tag_name)){
					String xml_str = "";
					if(int_xml.containsKey(parent_tag)){
						xml_str = int_xml.get(parent_tag);
					}
					String CardDispatch=formObject.getNGValue("AlternateContactDetails_CardDisp");
					String tagValue="";
					
					if(CardDispatch!=null && CardDispatch.equalsIgnoreCase("ByCourier"))
						tagValue="998";
					else
						tagValue=formObject.getNGValue("AlternateContactDetails_CustdomBranch")!=null?formObject.getNGValue("AlternateContactDetails_CustdomBranch"):"";

					xml_str = xml_str+"<"+tag_name+">"+tagValue+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding bhrabc id flag+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				
				else if("Gender".equalsIgnoreCase(tag_name) ){
					PersonalLoanS.mLogger.info("inside 1st if inside customer update req1");
					String xml_str = int_xml.get(parent_tag);
					xml_str =xml_str+ "<"+tag_name+">"+"1"+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("PL COMMON  after adding DSAId:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}

				
				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
				}
			}
				 
			else if( "SUPPLEMENT".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13)))
			{
					
				if("CIFId".equalsIgnoreCase(tag_name) ){
					String xml_str="";
					PersonalLoanS.mLogger.info("inside 1st if inside customer update req");
						 xml_str = int_xml.get(parent_tag);
							xml_str =xml_str+ "<"+tag_name+">"+formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),3)+"</"+ tag_name+">";
							PersonalLoanS.mLogger.info("PL COMMON  after adding CIFId:  "+xml_str);
						int_xml.put(parent_tag, xml_str);	                            	
					
						PersonalLoanS.mLogger.info("PL COMMON  after adding CIFId:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}
				
				else if("TitleToEmailIdAggregateTag".equalsIgnoreCase(tag_name) ){
					String xml_str = int_xml.get(parent_tag);
					PersonalLoanS.mLogger.info("RLOS COMMON"+ " before adding guarantor PersonDetails+ " + xml_str);
					xml_str = xml_str + getSupplement_PersonDetails(formObject,callName);
					PersonalLoanS.mLogger.info("RLOS COMMON"+ " after adding PersonDetails " + xml_str);
					int_xml.put(parent_tag, xml_str);
				
				}
				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
				}
			}
			}
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + "+e.getMessage());
			PLCommon.printException(e);
		}
		return int_xml;
	}
	public String fetchIsNTBval() {
		try{
		String ntbVal = "-1";
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		if(!formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true")){
			String query = "select count(distinct ECRN) from ng_RLOS_CUSTEXPOSE_CardDetails with (nolock) where Child_Wi = '"+formObject.getWFWorkitemName()+"' and ecrn !='' and ecrn is not null";
			PersonalLoanS.mLogger.info("CC Integration query for ECRN "+query);
			List<List<String>> count = formObject.getNGDataFromDataCache(query);
			if(count!=null && count.size()>0 && count.get(0)!=null){
				if(Integer.parseInt(count.get(0).get(0))>0){
					ntbVal="0";
				}
			}
			return ntbVal;
		}
		return ntbVal;
		}catch(Exception ex){
			PersonalLoanS.mLogger.info("CC_INtegration Exception in CARD services custom function:  ");
			PLCommon.printException(ex);
			return "";
		}
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
							if (NGFUserResourceMgr_PL.getGlobalVar("PL_Home").equalsIgnoreCase(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0)))
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
			List<List<String>> outputMQXML = formObject.getNGDataFromDataCache(sMQuery);
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
			PLCommon.printException(e);
			return "";
		}
	}
	public String getReference_details(){
		PersonalLoanS.mLogger.info("RLOSCommon java file"+ "inside getCustAddress_details : ");
		String  ref_xml_str ="";
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			int ref_row_count = formObject.getLVWRowCount("cmplx_RefDetails_cmplx_Gr_ReferenceDetails");
			PersonalLoanS.mLogger.info("RLOSCommon java file"+ "inside getReference_details row_count+ : "+ref_row_count);

			for (int i = 0; i<ref_row_count;i++){
				String ref_name = formObject.getNGValue("cmplx_RefDetails_cmplx_Gr_ReferenceDetails", i, 0); //0
				String ref_phone=formObject.getNGValue("cmplx_RefDetails_cmplx_Gr_ReferenceDetails", i, 1);//1
				//String ref_relation=formObject.getNGValue("cmplx_RefDetails_cmplx_Gr_ReferenceDetails", i, 2);//2
				ref_xml_str = ref_xml_str + "<ReferenceDetails><PersonName>"+ref_name+"</PersonName>";
				ref_xml_str = ref_xml_str + "<PhnDet><PhoneType>OFFCPH1</PhoneType><PhnCountryCode>00971</PhnCountryCode><CityCode></CityCode><PhnLocalCode>00971</PhnLocalCode>";
				ref_xml_str = ref_xml_str +"<PhoneNumber>"+ref_phone+"</PhoneNumber><ExtensionNumber></ExtensionNumber><PhnPrefFlag>N</PhnPrefFlag></PhnDet></ReferenceDetails>";
				}
			PersonalLoanS.mLogger.info("RLOSCommon"+ "reference tag Cration: "+ ref_xml_str);
			return ref_xml_str;
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("PLCommon getReference_details method"+ "Exception Occure in getReference_details");
			PLCommon.printException(e);
			return ref_xml_str;
		}	
	}	
	public  String Clean_Xml(String InputXml,String Call_name){
		String Output_Xml="";
		PL_Integration_Output ReadXml = new PL_Integration_Output();
		try{
			//below change by saurabh on 18th Dec
			if(InputXml.indexOf("&")>-1){
				InputXml=InputXml.replaceAll("&", "ampr");
			}
			Document doc = ReadXml.getDocument(InputXml);
			removeEmptyNodes(doc,Call_name);
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
	public  void removeEmptyNodes(Node node,String Call_name) {
		NodeList list = node.getChildNodes();
		List<Node> nodesToRecursivelyCall = new LinkedList<Node>();
		PersonalLoanS.mLogger.info("Node for removeEmptyNodes function is: "+ node);
		for (int i = 0; i < list.getLength(); i++) {
			nodesToRecursivelyCall.add(list.item(i));
		}
		for(Node tempNode : nodesToRecursivelyCall) {
			removeEmptyNodes(tempNode,Call_name);
		}
		boolean emptyElement = node.getNodeType() == Node.ELEMENT_NODE && node.getChildNodes().getLength() == 0;
		boolean emptyText = node.getNodeType() == Node.TEXT_NODE && node.getNodeValue().trim().isEmpty();
		boolean selectText = node.getNodeType() == Node.TEXT_NODE && (node.getNodeValue().trim().equalsIgnoreCase("--Select--")||node.getNodeValue().trim().equalsIgnoreCase("null"));
		//changes done to remove empty tags in Dectech only.
		if ("DECTECH".equalsIgnoreCase(Call_name)){
			PersonalLoanS.mLogger.info("inside if condition for: "+ node);
			if(emptyElement || emptyText || selectText) {
				if(!node.hasAttributes()) {
					node.getParentNode().removeChild(node);
				}
			}
		}
		else if ( selectText) {
			PersonalLoanS.mLogger.info("inside else if condition for: "+ node);
			if(!node.hasAttributes()) {
				node.getParentNode().removeChild(node);
			}
		}
	}


	//below functions added by akshay for drop 4
	public String getSupplement_PersonDetails(FormReference formObject,String callName){
		String str="";
		try{
			Common_Utils common=new Common_Utils(PersonalLoanS.mLogger);
			PersonalLoanS.mLogger.info("Inside  getSupplement_PersonDetails() for callName: "+callName);
			if(callName.equalsIgnoreCase("NEW_CUSTOMER_REQ")){
				if( "SUPPLEMENT".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),0)) && formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid")>0){ 
					for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid");i++){
						if(formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"), 2).equals(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,3))){
							str=str+"<PersonDetails><TitlePrefix>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,14)+"</TitlePrefix>";
							str=str+"<FirstName>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,0)+"</FirstName>";
							str=str+"<LastName>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,2)+"</LastName>";
							str=str+"<ShortName>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,0).substring(0,1)+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,2).substring(0,1)+"</ShortName>";
							str=str+"<Gender>"+(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,11).equalsIgnoreCase("Male")?"M":"F")+"</Gender>";
							str=str+"<MotherMaidenName>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,12)+"</MotherMaidenName>";
							str=str+"<MinorFlag>"+(Float.parseFloat(common.getAge(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,4)))<18?"Y":"N")+"</MinorFlag>";
							str=str+"<NonResidentFlag>"+(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,16).equalsIgnoreCase("true")?"Y":"N")+"</NonResidentFlag>";
							str=str+"<ResCountry>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,8)+"</ResCountry>";
							str=str+"<MaritalStatus>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,37)+"</MaritalStatus>";
							str=str+"<Nationality>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,5)+"</Nationality>";
							str=str+"<DateOfBirth>"+common.Convert_dateFormat(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,4), "dd/MM/yyyy","yyyy-MM-dd")+"</DateOfBirth>";
							str=str+"</PersonDetails>";	
						}
					}
				}
			}
			else if(callName.equalsIgnoreCase("CUSTOMER_UPDATE_REQ")){
				
					if(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13)!=null && "SUPPLEMENT".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13)))
					{ 
						for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid");i++){
							if(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),14).equals(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,3))){
								str=str+"<RtlAddnlDet><MothersName>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,12)+"</MothersName>";
								str=str+"<MaritalStatus>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,37)+"</MaritalStatus>";
								str=str+"<EmploymentType>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,40)+"</EmploymentType>";
								str=str+"<EmployeeStatus>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,32)+"</EmployeeStatus>";						
								str=str+"<Desig>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,31)+"</Desig>";						
							}
						}
						str=str+getFATCA_details(formObject,"SUPPLEMENT");
						str=str+getKYC_details(formObject,"SUPPLEMENT");
						str=str+getCustOECD_details(formObject,"SUPPLEMENT");
						str=str+"</RtlAddnlDet>";	
					}
					else if(formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),0)!=null && "GUARANTOR".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),0)) && formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"), 2).equals(formObject.getNGValue("cmplx_Guarantror_GuarantorDet",0,5)))
					{
						str=str+"<RtlAddnlDet><MothersName>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",0,25)+"</MothersName>";
						str=str+"<MaritalStatus>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",0,21)+"</MaritalStatus>";
						str=str+"<EmploymentType>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",0,22)+"</EmploymentType>";
						str=str+"<EmployeeStatus>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",0,26)+"</EmployeeStatus>";						
						str=str+"<Desig>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",0,24)+"</Desig>";						
					
						str=str+getFATCA_details(formObject,"GUARANTOR");
						str=str+getKYC_details(formObject,"GUARANTOR");
						str=str+getCustOECD_details(formObject,"GUARANTOR");
						str=str+"</RtlAddnlDet>";	
					}
			}

			else if(callName.equalsIgnoreCase("NEW_CREDITCARD_REQ")){
				if( "SUPPLEMENT".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13)) && formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid")>0){ 
					String phno="",email="";	
					for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid");i++){
						if(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),14).equals(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,3))){
							str=str+"<Title>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,14)+"</Title>";
							str=str+"<FirstName>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,0)+"</FirstName>";
							str=str+"<LastName>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,2)+"</LastName>";
							str=str+"<FullName>"+formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),4)+"</FullName>";
							str=str+"<MotherName>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,12)+"</MotherName>";
							str=str+"<DOB>"+common.Convert_dateFormat(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,4), "dd/MM/yyyy","yyyy-MM-dd")+"</DOB>";
							str=str+"<Gender>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,11)+"</Gender>";
							str=str+"<MaritalStatus>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,37)+"</MaritalStatus>";
							str=str+"<Nationality>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,5)+"</Nationality>";
							str=str+"<Department>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,31)+"</Department>";						
							str=str+"<Designation>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,31)+"</Designation>";						
							str=str+"<ProfileSerNo>RAKH</ProfileSerNo>";
							str=str+"<DocumentDetails><DocType>PPT</DocType>";
							str=str+"<DocId>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,3)+"</DocId></DocumentDetails>";
							phno=formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,6);
							email=formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,21);
							break;
						}
					}
					str=str+getCustAddress_details(callName);	
					str=str+"<PhoneFaxDtls><Type>StmtTelephone1</Type>";
					str=str+"<Number>"+phno+"</Number></PhoneFaxDtls>";
					str=str+"<EmailDtls><Type>StmtEmail</Type>";
					str=str+"<EmailId>"+email+"</EmailId></EmailDtls>";
					str=str+"<EmailDtls><Type>AdditionalEmail</Type>";
					str=str+"<EmailId></EmailId></EmailDtls>";
				}
			}

			else if(callName.equalsIgnoreCase("CARD_NOTIFICATION")){
				if( "SUPPLEMENT".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13)) && formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid")>0){ 
					for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid");i++){
						if(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),14).equals(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,3))){
							str=str+"<PersonDetails><TitlePrefix>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,14)+"</TitlePrefix>";
							str=str+"<FirstName>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,0)+"</FirstName>";
							str=str+"<LastName>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,2)+"</LastName>";
							str=str+"<ShortName>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,0).substring(0,1)+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,2).substring(0,1)+"</ShortName>";
							str=str+"<FullName>"+formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),4)+"</FullName>";
							str=str+"<Gender>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,11)+"</Gender>";
							str=str+"<MotherMaidenName>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,12)+"</MotherMaidenName>";
							str=str+"<MinorFlag>"+(Float.parseFloat(common.getAge(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,4)))<18?"Y":"N")+"</MinorFlag>";
							str=str+"<NonResidentFlag>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,16)+"</NonResidentFlag>";
							str=str+"<MaritalStatus>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,37)+"</MaritalStatus>";
							str=str+"<Nationality>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,5)+"</Nationality>";
							str=str+"<DateOfBirth>"+common.Convert_dateFormat(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,4), "dd/MM/yyyy","yyyy-MM-dd")+"</DateOfBirth>";
							str=str+"<ResidenceSince>"+calcResidenceSince(formObject)+"</ResidenceSince>";
							str=str+"<ResCountry>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,8)+"</ResCountry>";
							str=str+"<ECRNNum>"+formObject.getNGValue("CC_Creation_ECRN")+"</ECRNNum>";
							str=str+"</PersonDetails>";	
						}
					}
				}
			}
			PersonalLoanS.mLogger.info("Inside  getGuarantor_PersonDetails()--->Final str is: "+str);
		}catch(Exception e)
		{
			PersonalLoanS.mLogger.info("Exception occurred inside getSupplement_PersonDetails for call name: "+callName); 
			PLCommon.printException(e);
		}
		return str;	
	}

	public String getSupplement_DocDetails(FormReference formObject,String callName){
		String str="";
		PersonalLoanS.mLogger.info("Inside  getGuarantor_DocDetails()");
		if(callName.equalsIgnoreCase("NEW_CUSTOMER_REQ")){
			if( "SUPPLEMENT".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),0)) && formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid")>0){ 
				for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid");i++){
					if(formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"), 2).equals(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,3))){
						str=str+generateDocDetailsTag_NewCustomerReq(Arrays.asList("PPT",formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,36),formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,15),formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,5),formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",0,3)));
						str=str+generateDocDetailsTag_NewCustomerReq(Arrays.asList("VISA",formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,35),formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,23),"AE",formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,22)));
						str=str+generateDocDetailsTag_NewCustomerReq(Arrays.asList("EMID",formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,34),formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,18),formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,5),formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",0,7)));
					}
				}
			}
		}
		else if(callName.equalsIgnoreCase("CUSTOMER_UPDATE_REQ")){
			if(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13)!=null && "SUPPLEMENT".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13)))
			{ 
				for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid");i++){
					if(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),14).equals(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,3))){
						str=str+generateDocDetailsTag_CIFUpdate(Arrays.asList("PPT",formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,3),formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,36),formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,15)));
						str=str+generateDocDetailsTag_CIFUpdate(Arrays.asList("EMID",formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,7),formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,34),formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,18)));
						str=str+generateDocDetailsTag_CIFUpdate(Arrays.asList("VISA",formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,22),formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,35),formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,23)));
						break;
					}
				}
			}
			else if("GUARANTOR".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),0)) && formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"), 2).equals(formObject.getNGValue("cmplx_Guarantror_GuarantorDet",0,5)))
			{
				str=str+generateDocDetailsTag_CIFUpdate(Arrays.asList("PPT",formObject.getNGValue("cmplx_Guarantror_GuarantorDet",0,5),formObject.getNGValue("cmplx_Guarantror_GuarantorDet",0,19),formObject.getNGValue("cmplx_Guarantror_GuarantorDet",0,16)));
				str=str+generateDocDetailsTag_CIFUpdate(Arrays.asList("VISA",formObject.getNGValue("cmplx_Guarantror_GuarantorDet",0,17),formObject.getNGValue("cmplx_Guarantror_GuarantorDet",0,20),formObject.getNGValue("cmplx_Guarantror_GuarantorDet",0,18)));
				str=str+generateDocDetailsTag_CIFUpdate(Arrays.asList("EMID",formObject.getNGValue("cmplx_Guarantror_GuarantorDet",0,2),formObject.getNGValue("cmplx_Guarantror_GuarantorDet",0,13),formObject.getNGValue("cmplx_Guarantror_GuarantorDet",0,14)));
			}
		}

		else  if(callName.equalsIgnoreCase("CARD_NOTIFICATION")){
			if("SUPPLEMENT".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13)) && formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid")>0)
			{ 
				for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid");i++){
					if(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),14).equals(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,3))){		 	
						str=str+generateDocDetailsTag_CardNotif(Arrays.asList("EMID",formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,7),formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,34),formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,18)));
						str=str+generateDocDetailsTag_CardNotif(Arrays.asList("VISA",formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,22),formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,35),formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,23)));
						str=str+generateDocDetailsTag_CardNotif(Arrays.asList("PPT",formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,3),formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,36),formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,15)));
						break;
					}
				}
			}
		}
		return str;
	}


	public String generateDocDetailsTag_NewCustomerReq(List<String> DocValues){
		Common_Utils common=new Common_Utils(PersonalLoanS.mLogger);
		return 
				"<DocDetails><DocType>"+DocValues.get(0)+"</DocType>"
				+"<DocCode>"+DocValues.get(0)+"</DocCode>"
				+"<DocIssueDate>"+	common.Convert_dateFormat(DocValues.get(1), "dd/MM/yyyy","yyyy-MM-dd")+"</DocIssueDate>"
				+"<DocExpiryDate>"+common.Convert_dateFormat(DocValues.get(2), "dd/MM/yyyy","yyyy-MM-dd")+"</DocExpiryDate>"
				+"<ParentDocCode>Retail</ParentDocCode>"
				+"<CountryOfIssue>"+DocValues.get(3)+"</CountryOfIssue>"
				+"<PlaceOfIssue>DXB</PlaceOfIssue>"
				+"<DocRefNum>"+DocValues.get(4)+"</DocRefNum></DocDetails>";
	}

	public String generateDocDetailsTag_CIFUpdate(List<String> DocValues){
		Common_Utils common=new Common_Utils(PersonalLoanS.mLogger);
		return 
				"<DocDet><DocType>"+DocValues.get(0)+"</DocType>"
				+"<DocIsVerified>Y</DocIsVerified>"
				+"<DocNo>"+DocValues.get(1)+"</DocNo>"
				+"<DocIssDate>"+common.Convert_dateFormat(DocValues.get(2), "dd/MM/yyyy","yyyy-MM-dd")+"</DocIssDate>"
				+"<DocExpDate>"+common.Convert_dateFormat(DocValues.get(3), "dd/MM/yyyy","yyyy-MM-dd")+"</DocExpDate></DocDet>";

	}

	public String generateDocDetailsTag_CardNotif(List<String> DocValues){
		Common_Utils common=new Common_Utils(PersonalLoanS.mLogger);
		return 
				"<DocumentDet><DocumentType>"+DocValues.get(0)+"</DocumentType>"
				+"<DocId>"+DocValues.get(1)+"</DocId>"
				+"<DocExpiryFlag>Y</DocExpiryFlag>"
				+"<DocIssueDate>"+	common.Convert_dateFormat(DocValues.get(2), "dd/MM/yyyy","yyyy-MM-dd")+"</DocIssueDate>"
				+"<DocExpDt>"+common.Convert_dateFormat(DocValues.get(3), "dd/MM/yyyy","yyyy-MM-dd")+"</DocExpDt>"
				+"<DocCollected>Y</DocCollected>"
				+"<DocVerified>Y</DocVerified></DocumentDet>";

	}

	public String getFATCA_details(FormReference formObject,String opName)	{
		String str="<FatcaDetails>";
		String multipleApplicantName="";
		PersonalLoanS.mLogger.info("inside getFATCA_details for  "+opName);

		if( "SUPPLEMENT".equalsIgnoreCase(opName) || ("PRIMARY".equalsIgnoreCase(opName) && "CC_Disbursal".equalsIgnoreCase(formObject.getWFActivityName())))
			{
			multipleApplicantName=formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),4);
			}
		else if("GUARANTOR".equalsIgnoreCase(opName)  || ("PRIMARY".equalsIgnoreCase(opName) && "Disbursal".equalsIgnoreCase(formObject.getWFActivityName())))
			{
			multipleApplicantName=formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),1);
			}
		PersonalLoanS.mLogger.info("getFATCA_details multipleApplicantName: "+multipleApplicantName);

			for(int i=0;i<formObject.getLVWRowCount("cmplx_FATCA_cmplx_FATCAGrid");i++){
			String applicantName=formObject.getNGValue("cmplx_FATCA_cmplx_FATCAGrid",i, 0);
			String USRelation=formObject.getNGValue("cmplx_FATCA_cmplx_FATCAGrid",i, 1);
			String TIN=formObject.getNGValue("cmplx_FATCA_cmplx_FATCAGrid",i, 3);
			if(applicantName.split("-")[1].equalsIgnoreCase(multipleApplicantName)){
				str=str+"<USRelation>"+USRelation+"</USRelation>";
				str=str+"<TIN>"+TIN+"</TIN>";
				break;
			}
		}
		str=str+"</FatcaDetails>";
		return str;

	}

	public String getKYC_details(FormReference formObject,String opName)	{
		PersonalLoanS.mLogger.info("inside getKYC_details for  "+opName);

		String str="<KYCDetails>";
		String multipleApplicantName="";
		if( "SUPPLEMENT".equalsIgnoreCase(opName) || ("PRIMARY".equalsIgnoreCase(opName) && "CC_Disbursal".equalsIgnoreCase(formObject.getWFActivityName())))
		{
		multipleApplicantName=formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),4);
		}
		else if("GUARANTOR".equalsIgnoreCase(opName)  || ("PRIMARY".equalsIgnoreCase(opName) && "Disbursal".equalsIgnoreCase(formObject.getWFActivityName())))
		{
		multipleApplicantName=formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),1);
		}
		PersonalLoanS.mLogger.info("getKYC_details multipleApplicantName: "+multipleApplicantName);

		for(int i=0;i<formObject.getLVWRowCount("cmplx_KYC_cmplx_KYCGrid");i++){
			String applicantName=formObject.getNGValue("cmplx_KYC_cmplx_KYCGrid",i, 0);
			String KYCHeld=formObject.getNGValue("cmplx_KYC_cmplx_KYCGrid",i, 1);
			String PEP=formObject.getNGValue("cmplx_KYC_cmplx_KYCGrid",i,2);
			if(applicantName.split("-")[1].equalsIgnoreCase(multipleApplicantName)){
				str=str+"<KYCHeld>"+KYCHeld+"</KYCHeld>";
				str=str+"<PEP>"+PEP+"</PEP>";
				break;
			}
		}
		str=str+"</KYCDetails>";
		return str;

	}

	public String calcResidenceSince(FormReference formObject){
		int size=formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
		String residenceSince="",residenceSince_date="";
		try{
			for(int i=0;i<size;i++){
				if(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,13).split("-")[1].equals(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),4)))
				{
					if(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,0).equalsIgnoreCase("RESIDENCE")){
						residenceSince=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,9);
						PersonalLoanS.mLogger.info("inside ResidenceSince Value of Years in RESD Address: "+residenceSince);
					}
				}
			}
			SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.YEAR, -Integer.parseInt(residenceSince));
			residenceSince_date=sdf1.format(cal.getTime());
			PersonalLoanS.mLogger.info("inside ResidenceSince Value of residenceSince_date: "+residenceSince_date);
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("exception occurred inside calcResidenceSince()");
			PLCommon.printException(e);
		}
		return residenceSince_date;
	}
	
	private Map<String, String> CIFEnquiryLockUnlock(List<List<String>> DB_List,FormReference formObject,String callName) {

		PersonalLoanS.mLogger.info("Inside CIFEnquiryLockUnlock()------call name is: "+callName);
		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();

		try{
			for (List<String> mylist : DB_List) {
				// for(int i=0;i<col_n.length();i++)
				for (int i = 0; i < 8; i++) {
					// CreditCard.mLogger.info("rec: "+records.item(rec));
					PersonalLoanS.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");

				if("CCIFID".equalsIgnoreCase(tag_name) || "CIFId".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("inside 1st if inside customer update req");
					String xml_str = int_xml.get(parent_tag);
					if(formObject.isVisible("") && "SUPPLEMENT".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13)))
					{
						xml_str =xml_str+ "<"+tag_name+">"+formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),3)+"</"+ tag_name+">";
					}
					else if("GUARANTOR".equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),0)))
					{	
						xml_str =xml_str+ "<"+tag_name+">"+formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),3)+"</"+ tag_name+">";
					}
					else{	
						int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
					}
					PersonalLoanS.mLogger.info("PL COMMON  after adding CIFId:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}
				else{	
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
				}
			}
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + "+e.getMessage());

		}

		return int_xml;
	}
	//added below by Tarang against drop 4 TakeOver on 04/04/2018
	private Map<String, String> UPDATE_LOAN_DETAILS_Custom(List<List<String>> DB_List,FormReference formObject,String callName) {

		PersonalLoanS.mLogger.info("Inside UPDATE_LOAN_DETAILS()------call name is: "+callName);
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
				if("DSAId".equalsIgnoreCase(tag_name)){
					String xml_str = int_xml.get(parent_tag);
					PersonalLoanS.mLogger.info("PL COMMON"+ " before adding UpdateLoanDetails DSAId " + xml_str);
					xml_str = "<"+tag_name+">"+formObject.getNGValue("DSA_Name")+"</"+ tag_name+">";
					PersonalLoanS.mLogger.info("PL COMMON"+ " after adding UpdateLoanDetails DSAId+ " + xml_str);
				}
				else if("ApplicationID".equalsIgnoreCase(tag_name) ){
					PersonalLoanS.mLogger.info("inside 1st if"+"inside update loan details1");
					String[] winum = formObject.getWFWorkitemName().split("-");
					String xml_str = int_xml.get(parent_tag);
					xml_str = "<"+tag_name+">"+winum[1].substring(2)+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("PL COMMON"+" after adding ApplicationID:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}
				else if("DocDetails".equalsIgnoreCase(tag_name)){
					String xml_str = int_xml.get(parent_tag);
					PersonalLoanS.mLogger.info("PL COMMON"+ " before adding UpdateLoanDetails doc details " + xml_str);
					xml_str = xml_str + getUpdLoanDetails_IncomingDocDetails(formObject,callName);
					PersonalLoanS.mLogger.info("PL COMMON"+ " after adding UpdateLoanDetails docDetails+ " + xml_str);
					int_xml.put(parent_tag, xml_str);
					PersonalLoanS.mLogger.info("PL COMMON"+ " before Liability certificate grid 1+ " + xml_str);
					xml_str= xml_str + getUpdLoanDetails_GridDocDetails(formObject,callName,"cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate");
					int_xml.put(parent_tag, xml_str);
					PersonalLoanS.mLogger.info("PL COMMON"+ " before Manager's cheque grid 1+ " + xml_str);
					xml_str= xml_str + getUpdLoanDetails_GridDocDetails(formObject,callName,"cmplx_PostDisbursal_cmplx_gr_ManagersCheque");
					int_xml.put(parent_tag, xml_str);
					PersonalLoanS.mLogger.info("PL COMMON"+ " before No Liability certificate grid 1+ " + xml_str);
					xml_str= xml_str + getUpdLoanDetails_GridDocDetails(formObject,callName,"cmplx_PostDisbursal_cpmlx_gr_NLC");
					int_xml.put(parent_tag, xml_str);
					PersonalLoanS.mLogger.info("PL COMMON"+ " before Bank Guarantee grid 1+ " + xml_str);
					xml_str= xml_str + getUpdLoanDetails_GridDocDetails(formObject,callName,"cmplx_PostDisbursal_cmplx_gr_BankGuarantee");
					int_xml.put(parent_tag, xml_str);
				}
				else if("AdditionalAttributeDtls".equalsIgnoreCase(tag_name)){
					String xml_str = int_xml.get(parent_tag);
					PersonalLoanS.mLogger.info("PL COMMON"+ " before adding UpdateLoanDetails AdditionalAttributeDtls details " + xml_str);
					xml_str= xml_str + getUpdLoanDetails_AdditionalAttributeDtlsDetails(formObject,callName,"cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate");
					int_xml.put(parent_tag, xml_str);
					PersonalLoanS.mLogger.info("PL COMMON"+ " before Manager's cheque grid 1+ " + xml_str);
					xml_str= xml_str + getUpdLoanDetails_AdditionalAttributeDtlsDetails(formObject,callName,"cmplx_PostDisbursal_cmplx_gr_ManagersCheque");
					int_xml.put(parent_tag, xml_str);
					PersonalLoanS.mLogger.info("PL COMMON"+ " before No Liability certificate grid 1+ " + xml_str);
					xml_str= xml_str + getUpdLoanDetails_AdditionalAttributeDtlsDetails(formObject,callName,"cmplx_PostDisbursal_cpmlx_gr_NLC");
					int_xml.put(parent_tag, xml_str);
					PersonalLoanS.mLogger.info("PL COMMON"+ " before Bank Guarantee grid 1+ " + xml_str);
					xml_str= xml_str + getUpdLoanDetails_AdditionalAttributeDtlsDetails(formObject,callName,"cmplx_PostDisbursal_cmplx_gr_BankGuarantee");
					int_xml.put(parent_tag, xml_str);
				}
				else{	
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
				}
			}
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + "+e.getMessage());
		}
		return int_xml;
	}
	
	
	public static String getUpdLoanDetails_AdditionalAttributeDtlsDetails(FormReference formObject,String callName,String GridName){
		PersonalLoanS.mLogger.info( "inside getUpdLoanDetails_AdditionalAttributeDtlsDetails : ");
		String add_xml_str="";
		int row_count = formObject.getLVWRowCount(GridName);
		PersonalLoanS.mLogger.info("PLIntegration_Input java file"+ "inside getUpdLoanDetails_AdditionalAttributeDtlsDetails add_row_count+ : "+row_count);

		for (int i = 0; i<row_count;i++){
			PersonalLoanS.mLogger.info( "inside add_row_count+ : "+formObject.getNGValue("cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate",formObject.getSelectedIndex("cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate"),0));
			
			
			String Loan_Attribute_Type = ""; 
			String Loan_Attribute_Value="";
			String Remark="";
			
			Loan_Attribute_Value=formObject.getNGValue(GridName, i, 8);
			String Doc_Name[]=Loan_Attribute_Value.split("_");
			String sQuery = "select code from NG_MASTER_LoanAttributeMis with (nolock) where description ='"+Doc_Name[1]+"'";
			List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
			PersonalLoanS.mLogger.info( "value is "+OutputXML.get(0).get(0));
			if(!OutputXML.isEmpty()){
				Loan_Attribute_Type = OutputXML.get(0).get(0);
			}
	
			if("TAKE1".equalsIgnoreCase(Loan_Attribute_Type)){
				Remark="Released to "+formObject.getNGValue("PostDisbursal_MCQ_Released_To")+ " - " + Remark;
			}
			else if(Loan_Attribute_Type.contains("NLC")){
				Remark="25%Released " +formObject.getNGValue("PostDisbursal_25Released")+" and Full Fund Released -"+formObject.getNGValue("PostDisbursal_FullFundReleased")+ " - " +Remark;
			}
			
			add_xml_str = add_xml_str + "<AdditionalAttributeDtls><LoanAttributeType>"+Loan_Attribute_Type+"</LoanAttributeType>";
			add_xml_str = add_xml_str + "<LoanAttributeValue>"+Loan_Attribute_Value+"</LoanAttributeValue>";
			add_xml_str = add_xml_str + "<Remarks>"+Remark+"</Remarks></AdditionalAttributeDtls>";
		}
	PersonalLoanS.mLogger.info( "DocDetails tag Cration: "+ add_xml_str);
	return add_xml_str;
	}
	
	
	public static String getUpdLoanDetails_IncomingDocDetails(FormReference formObject,String callName){
		PersonalLoanS.mLogger.info( "inside getUpdLoanDetails_details : ");
		String add_xml_str="";
		IRepeater repObj;
		repObj = formObject.getRepeaterControl("IncomingDoc_Frame2");
		int rowRowcount=repObj.getRepeaterRowCount();
		PersonalLoanS.mLogger.info("RLOS Initiation"+ "sQuery for document name is: rowRowcount" +  rowRowcount);
		if (repObj.getRepeaterRowCount() != 0) {

			for(int j = 0; j < rowRowcount; j++){				
				String Doc_Code = ""; 
				String Doc_Issue_Date="";
				String Doc_Type="";
				String Doc_Expiry_Date="";
				String Doc_Received_Date="";
				String Doc_Expected_Date = "";
				String city="";
				String country="";
				String Doc_Location="AE";
				String Document_Expiry_Flag="N";
				String Remarks="";
				Common_Utils common=new Common_Utils(PersonalLoanS.mLogger);
				
				Doc_Type=repObj.getValue(j, "cmplx_DocName_DocName");
				String StatusValue=repObj.getValue(j,"cmplx_DocName_Doc_Status")==null?"":repObj.getValue(j,"cmplx_DocName_Doc_Status");
				
				if ((Doc_Type.contains("NO_LIABILITY_CERTIFICATE") || Doc_Type.contains("BANK_GUARANTEE") || Doc_Type.contains("MANAGERS_CHEQUE") || Doc_Type.contains("LIABILITY_CERTIFICATE") || Doc_Type.contains("Salary_Transfer_letter")) && "Received".equalsIgnoreCase(StatusValue)){
					Doc_Expiry_Date=repObj.getValue(j, "cmplx_DocName_ExpiryDate");
					Doc_Received_Date=(new Date()).toString();
					//if(!"".equalsIgnoreCase(Bank_Name) || !Bank_Name.isEmpty() || !"null".equalsIgnoreCase(Bank_Name))
					//{
						Remarks=repObj.getValue(j, "cmplx_DocName_Remarks");
					//}	
					
					String Doc_Name[]=Doc_Type.split("_");
					String sQuery = "select code from NG_MASTER_DocType with (nolock) where description like '"+Doc_Name[1]+"%'";
		    		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		    		PersonalLoanS.mLogger.info( "value is "+OutputXML.get(0).get(0));
		    		if(!OutputXML.isEmpty()){
		    			Doc_Code = OutputXML.get(0).get(0);
		    		}
			    		
		    		try{
		    			DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
		    			if(df.parse(Doc_Expiry_Date).compareTo(new Date())<0){
		    				Document_Expiry_Flag="Y";
		    			}
		    		}
		    		catch(ParseException e){
		    			PersonalLoanS.mLogger.info("PL Integration "+ " Exception occured in Parsing date in getUpdLoanDetails_DocDetails + "+e.getMessage());
		    		}
		
							
					add_xml_str = add_xml_str + "<DocDetails><DocType>"+Doc_Type+"</DocType>";
					add_xml_str = add_xml_str + "<DocCode>"+Doc_Code+"</DocCode>";
					add_xml_str = add_xml_str + "<DocIssueDate>"+common.Convert_dateFormat(Doc_Issue_Date, "dd/MM/yyyy","yyyy-MM-dd")+"</DocIssueDate>";
					add_xml_str = add_xml_str + "<DocExpiryDate>"+common.Convert_dateFormat(Doc_Expiry_Date, "dd/MM/yyyy","yyyy-MM-dd")+"</DocExpiryDate>";
					add_xml_str = add_xml_str + "<DocReceivedDate>"+common.Convert_dateFormat(Doc_Received_Date, "dd/MM/yyyy","yyyy-MM-dd")+"</DocReceivedDate>";
					add_xml_str = add_xml_str + "<DocExpectedDate>"+common.Convert_dateFormat(Doc_Expected_Date, "dd/MM/yyyy","yyyy-MM-dd")+"</DocExpectedDate>";
					add_xml_str = add_xml_str + "<CountryOfIssue>"+country+"</CountryOfIssue>";
					add_xml_str = add_xml_str + "<PlaceOfIssue>"+city+"</PlaceOfIssue>";
					add_xml_str = add_xml_str + "<DocLocation>"+Doc_Location+"</DocLocation>";
					add_xml_str = add_xml_str + "<DocRefNum>"+Doc_Code+"</DocRefNum>";
					add_xml_str = add_xml_str + "<DocumentExpiryFlag>"+Document_Expiry_Flag+"</DocumentExpiryFlag>";
					add_xml_str = add_xml_str + "<Remarks>"+Remarks+"</Remarks></AddressDetails>";
				}
			}
		PersonalLoanS.mLogger.info( "DocDetails tag Cration: "+ add_xml_str);
		}
		return add_xml_str;
	}
	
	
	public static String getUpdLoanDetails_GridDocDetails(FormReference formObject,String callName,String GridName){
		PersonalLoanS.mLogger.info( "inside getUpdLoanDetails_GridDocDetails : ");
		String add_xml_str="";
		int row_count = formObject.getLVWRowCount(GridName);
		PersonalLoanS.mLogger.info("PLIntegration_Input java file"+ "inside getUpdLoanDetails_GridDocDetails add_row_count+ : "+row_count);

		for (int i = 0; i<row_count;i++){			
			String Doc_Code = ""; 
			String Doc_Issue_Date="";
			String Doc_Type="";
			String Doc_Expiry_Date="";
			String Doc_Received_Date="";
			String Doc_Expected_Date = "";
			String city="";
			String country="";
			String Doc_Location="AE";
			String Document_Expiry_Flag="N";
			String Remarks="";
			String Bank_Name="";
			Common_Utils common=new Common_Utils(PersonalLoanS.mLogger);
			PersonalLoanS.mLogger.info( "DocDetails tag Cration: "+ add_xml_str);
			
			Doc_Issue_Date=formObject.getNGValue("", i, 3);
			Doc_Type=formObject.getNGValue("", i, 8);
			Doc_Expiry_Date=formObject.getNGValue("", i, 4);
			Doc_Received_Date=formObject.getNGValue("", i, 0);
			Remarks=formObject.getNGValue("", i, 5);
			Bank_Name=formObject.getNGValue("", i, 1);
			
			if(!"".equalsIgnoreCase(Bank_Name) || !Bank_Name.isEmpty() || !"null".equalsIgnoreCase(Bank_Name))
			{
				Remarks=Bank_Name+ " - "+Remarks.substring(0, 177);
			}	
			
			String Doc_Name[]=Doc_Type.split("_");
			String sQuery = "select code from NG_MASTER_DocType with (nolock) where description like '"+Doc_Name[1]+"%'";
    		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
    		PersonalLoanS.mLogger.info( "value is "+OutputXML.get(0).get(0));
    		if(!OutputXML.isEmpty()){
    			Doc_Code = OutputXML.get(0).get(0);
    		}
	    		
    		try{
    			DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
    			if(df.parse(Doc_Expiry_Date).compareTo(new Date())<0){
    				Document_Expiry_Flag="Y";
    			}
    		}
    		catch(ParseException e){
    			PersonalLoanS.mLogger.info("PL Integration "+ " Exception occured in Parsing date in getUpdLoanDetails_DocDetails + "+e.getMessage());
    		}
    		
			add_xml_str = add_xml_str + "<DocDetails><DocType>"+Doc_Type+"</DocType>";
			add_xml_str = add_xml_str + "<DocCode>"+Doc_Code+"</DocCode>";
			add_xml_str = add_xml_str + "<DocIssueDate>"+common.Convert_dateFormat(Doc_Issue_Date, "dd/MM/yyyy","yyyy-MM-dd")+"</DocIssueDate>";
			add_xml_str = add_xml_str + "<DocExpiryDate>"+common.Convert_dateFormat(Doc_Expiry_Date, "dd/MM/yyyy","yyyy-MM-dd")+"</DocExpiryDate>";
			add_xml_str = add_xml_str + "<DocReceivedDate>"+common.Convert_dateFormat(Doc_Received_Date, "dd/MM/yyyy","yyyy-MM-dd")+"</DocReceivedDate>";
			add_xml_str = add_xml_str + "<DocExpectedDate>"+common.Convert_dateFormat(Doc_Expected_Date, "dd/MM/yyyy","yyyy-MM-dd")+"</DocExpectedDate>";
			add_xml_str = add_xml_str + "<CountryOfIssue>"+country+"</CountryOfIssue>";
			add_xml_str = add_xml_str + "<PlaceOfIssue>"+city+"</PlaceOfIssue>";
			add_xml_str = add_xml_str + "<DocLocation>"+Doc_Location+"</DocLocation>";
			add_xml_str = add_xml_str + "<DocRefNum>"+Doc_Code+"</DocRefNum>";
			add_xml_str = add_xml_str + "<DocumentExpiryFlag>"+Document_Expiry_Flag+"</DocumentExpiryFlag>";
			add_xml_str = add_xml_str + "<Remarks>"+Remarks+"</Remarks></AddressDetails>";
		}
		return add_xml_str;
	}
	//added above by Tarang against drop 4 TakeOver on 04/04/2018


}
