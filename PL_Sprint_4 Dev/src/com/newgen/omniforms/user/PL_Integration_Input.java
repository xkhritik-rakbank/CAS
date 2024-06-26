
package com.newgen.omniforms.user;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.Socket;
import java.sql.Timestamp;
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
import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
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
	PLCommon PL_Common = new PLCommon();

	private String col_n = "call_type,Call_name,form_control,parent_tag_name,xmltag_name,is_repetitive,default_val,data_format";
	//Deepak 20 Dec PCSP-9 Card Notification & Card Creation call name added as they also need to be send in upper case.
	//Deepak 20 Dec PCSP-9 Card Notification & Card Creation call name removed again as they are getting failed.
	private String fin_call_name = "Customer_details, Customer_eligibility,NEW_CUSTOMER_REQ,new_account_req,DEDUP_SUMMARY,CUSTOMER_UPDATE_REQ";

	private String capitalExceptionsTags = "EmploymentStatus,EmploymentType,AddressType";;//added by shweta for PCSP-9
	PLCommon plcommonObj = new PLCommon();
	//Deepak Customer Update request added in fin call name string.  22Nov2017.
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description     	                   : Function to Generate XML for Integration calls     

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
						int_xml = CARD_NOTIFICATION_Custom(DB_List,formObject,callName,Operation_name);
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
					else if ("ENROLL_REWARDS".equalsIgnoreCase(callName)) {
						int_xml = ENROLL_REWARDS_Custom(DB_List,formObject,callName);
					}
					//added by akshay on 5/3/18 for drop 4xcvxc
					else if("CIF_UNLOCK".equalsIgnoreCase(Operation_name) || "CIF_LOCK".equalsIgnoreCase(Operation_name) || "CIF_VERIFY".equalsIgnoreCase(Operation_name) || "CIF_ENQUIRY".equalsIgnoreCase(Operation_name)
							|| "G_CIF_LOCK".equalsIgnoreCase(Operation_name) || "G_CIF_UNLOCK".equalsIgnoreCase(Operation_name))
					{
						int_xml = CIFEnquiryLockUnlock(DB_List,formObject,callName,Operation_name);	
					}
					//Added below by Tarang against drop 4 Takeover on 04/04/2018
					else if ("UPDATE_LOAN_DETAILS".equalsIgnoreCase(callName)) {
						int_xml = UPDATE_LOAN_DETAILS_Custom(DB_List,formObject,callName);
					}
					//Added above by Tarang against drop 4 Takeover on 04/04/2018
					//Added below by Shweta 
					else if ("RISK_SCORE_DETAILS".equalsIgnoreCase(callName)) {
						int_xml = RISK_SCORE_DETAILS_Custom(DB_List,formObject,callName);
					}
					else if ("COMPLIANCE_CHECK".equalsIgnoreCase(callName)) {
						int_xml = COMPLIANCE_CHECK_Custom(DB_List,formObject,callName,Operation_name);
					}
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
							//								PersonalLoanS.mLogger.info("RLOS"+"itr_value: Key: "+ entry.getKey()+" Value: "+entry.getValue());
							final_xml = final_xml.insert(final_xml.indexOf("<"+entry.getKey()+">")+entry.getKey().length()+2, entry.getValue());
							//							PersonalLoanS.mLogger.info("value of final xml"+"final_xml"+final_xml);
							itr.remove();
						}

					}    
					final_xml=final_xml.append("</").append(parentTagName).append(">");
					PersonalLoanS.mLogger.info("value of final_xml before clean xml: "+final_xml);
					final_xml = new StringBuilder( Clean_Xml(final_xml.toString(),callName));
					PersonalLoanS.mLogger.info("FInal XMLnew is: "+ final_xml);
					final_xml.insert(0, header);
					final_xml.append(footer);
					PersonalLoanS.mLogger.info("FInal XMLnew with header: "+ final_xml);
					formObject.setNGValue("Is_"+callName,"Y");
					PersonalLoanS.mLogger.info("value of "+callName+" Flag: "+formObject.getNGValue("Is_"+callName));
					Integration_fragName(callName);
					/*if(callName.equalsIgnoreCase("DEDUP_SUMMARY"))
						{
							return dummyResponse(callName);
						}
						else
						{*/
					//return MQ_connection_response(final_xml);
					//}
					return MQ_connection_response(final_xml);

					//return dummyResponse(callName);
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


	private Map<String, String> ENROLL_REWARDS_Custom(List<List<String>> DB_List, FormReference formObject,String callName) {

		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();

		try{
			String Applicant_type = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12);
			for (List<String> mylist : DB_List) {

				for (int i = 0; i < 8; i++) {

					//CreditCard.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");
				String form_control = recordFileMap.get("form_control");

				if(tag_name.equalsIgnoreCase("Number")){
					if("SUPPLEMENT".equalsIgnoreCase(Applicant_type)){
						String phno="";	
						for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_supplementGrid");i++){
							if(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13).equals(formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,3))){
								phno=formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,6);
								break;
							}
						}

						try{
							String xml_str = int_xml.get(parent_tag);
							//CreditCard.mLogger.info("RLOS COMMON"+" before contact details+ "+xml_str);
							xml_str = xml_str + "<"+tag_name+">"+ phno.substring(5,phno.length())+"</"+tag_name+">";
							//CreditCard.mLogger.info("RLOS COMMON"+" after adding contact details+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}
						catch(Exception e){
							PersonalLoanS.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + "+e.getMessage());
							PersonalLoanS.mLogger.info("CC_INtegration Exception in CARD services custom function:  ");

						}
					}
					else{
						if(int_xml.containsKey(parent_tag))
						{
							//CreditCard.mLogger.info("inside 1st if"+"inside customer update req2");
							try{
								String xml_str = int_xml.get(parent_tag);
								PersonalLoanS.mLogger.info("RLOS COMMON"+" before contact details+ "+xml_str);
								xml_str = xml_str + "<"+tag_name+">"+ formObject.getNGValue(form_control).substring(5,formObject.getNGValue(form_control).length())+"</"+tag_name+">";
								//CreditCard.mLogger.info("RLOS COMMON"+" after adding contact details+ "+xml_str);
								int_xml.put(parent_tag, xml_str);
							}
							catch(Exception e){
								PersonalLoanS.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + "+e.getMessage());
								PersonalLoanS.mLogger.info("CC_INtegration Exception in CARD services custom function:  ");
								PL_Common.printException(e);
							}
						}
					}                            	
				}
				else if(tag_name.equalsIgnoreCase("EmailAddrValue") && "SUPPLEMENT".equalsIgnoreCase(Applicant_type)){
					String EmailAddrValue="";

					for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_supplementGrid");i++){
						if(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13).equals(formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,3))){
							EmailAddrValue=formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,21);
							break;
						}
					}
					String xml_str = int_xml.get(parent_tag);
					//CreditCard.mLogger.info("RLOS COMMON"+" before contact details+ "+xml_str);
					xml_str = xml_str + "<"+tag_name+">"+ EmailAddrValue+"</"+tag_name+">";
					//CreditCard.mLogger.info("RLOS COMMON"+" after adding contact details+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else if(tag_name.equalsIgnoreCase("BirthDt") && !"SUPPLEMENT".equalsIgnoreCase(Applicant_type)){
					if(int_xml.containsKey(parent_tag))
					{
						try{
							String xml_str = int_xml.get(parent_tag);
							PersonalLoanS.mLogger.info("RLOS COMMON"+" before contact details+ "+xml_str);
							String dob_date =formObject.getNGValue(form_control);
							DateFormat for1 = new SimpleDateFormat("dd/MM/yyyy");
							DateFormat for2 = new SimpleDateFormat("ddMMMyyyy");
							Date con_dob_date = for1.parse(dob_date);
							//CreditCard.mLogger.info("RLOS COMMON"+" before contact details+ "+con_dob_date);
							xml_str = xml_str + "<"+tag_name+">"+ for2.format(con_dob_date)+"</"+tag_name+">";

							//xml_str = xml_str + "<"+tag_name+">"+ formObject.getNGValue(form_control).substring(5,15)+"</"+tag_name+">";
							//CreditCard.mLogger.info("RLOS COMMON"+" after adding contact details+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}
						catch(Exception e){
							PersonalLoanS.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + "+e.getMessage());
							PersonalLoanS.mLogger.info("CC_INtegration Exception in CARD services custom function:");
						}
					}
				}
				else if(tag_name.equalsIgnoreCase("MembershipNumber")){

					if(int_xml.containsKey(parent_tag))
					{
						try{
							String xml_str = int_xml.get(parent_tag);
							//CreditCard.mLogger.info("RLOS COMMON"+" before contact details+ "+xml_str);
							if("SUPPLEMENT".equalsIgnoreCase(Applicant_type)){
								String Enrolmentno="";	
								for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_supplementGrid");i++){
									if(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13).equals(formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,3))){
										Enrolmentno=getSkywardNo(formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,42));
										break;
									}
								}
								if(!"".equalsIgnoreCase(Enrolmentno)){
									xml_str = xml_str + "<"+tag_name+">"+ Enrolmentno+"</"+tag_name+">";
								}
							}
							else{
								if(!"".equalsIgnoreCase(formObject.getNGValue(form_control))){
									xml_str = xml_str + "<"+tag_name+">"+ getSkywardNo(formObject.getNGValue(form_control))+"</"+tag_name+">";
								}
							}
							//CreditCard.mLogger.info("RLOS COMMON"+" after adding contact details+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}
						catch(Exception e){
							PersonalLoanS.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + "+e.getMessage());
							PersonalLoanS.mLogger.info("CC_INtegration Exception in CARD services custom function:  ");
						}
					}

				}
				else if(tag_name.equalsIgnoreCase("PartnerProductCode")){
					if(int_xml.containsKey(parent_tag))
					{
						String xml_str = int_xml.get(parent_tag);
						//CreditCard.mLogger.info("RLOS COMMON"+" before contact details+ "+xml_str);
						String sQuery="";
						if("SUPPLEMENT".equalsIgnoreCase(Applicant_type)){
							sQuery="select SUPPL_CARD_PARTNER_ID from ng_master_ENROLL_REWARDS_PartnerCode where card_product='"+formObject.getNGValue("CC_Creation_Product")+"'";
						}
						else{
							sQuery="select PRIMARY_CARD_PARTNER_ID from ng_master_ENROLL_REWARDS_PartnerCode where card_product='"+formObject.getNGValue("CC_Creation_Product")+"'";
						}

						//CreditCard.mLogger.info("RLOS COMMON"+" before contact details+ "+sQuery);
						List<List<String>> DB_List1 = formObject.getDataFromDataSource(sQuery);
						PersonalLoanS.mLogger.info("RLOS COMMON"+" before contact details+ "+DB_List1);
						if(!DB_List1.isEmpty()){
							String ENROLL_REWARDS_PartnerCode=DB_List1.get(0).get(0);	
							xml_str = xml_str + "<"+tag_name+">"+ENROLL_REWARDS_PartnerCode+"</"+tag_name+">";
							int_xml.put(parent_tag, xml_str);
						}
					}
				}
				else if(tag_name.equalsIgnoreCase("TitlePrefix") && !"SUPPLEMENT".equalsIgnoreCase(Applicant_type)){
					String xml_str = int_xml.get(parent_tag);
					//CreditCard.mLogger.info("COMMON before TitlePrefix "+xml_str);
					String sQuery="select Enroll_RewardCode from ng_master_title where code='"+formObject.getNGValue(form_control)+"'";
					//CreditCard.mLogger.info("TitlePrefix sQuery: "+sQuery);
					List<List<String>> DB_List1 = formObject.getDataFromDataSource(sQuery);
					PersonalLoanS.mLogger.info("TitlePrefix sQuery result "+DB_List1);
					if(!DB_List1.isEmpty()){
						String ENROLL_REWARDS_PartnerCode=DB_List1.get(0).get(0);	
						xml_str = "<"+tag_name+">"+ENROLL_REWARDS_PartnerCode+"</"+tag_name+">";
						int_xml.put(parent_tag, xml_str);
					}
					else{
						xml_str = "<"+tag_name+">"+formObject.getNGValue(form_control)+"</"+tag_name+">";
						int_xml.put(parent_tag, xml_str);

					}
				}
				else if("PersonNm".equalsIgnoreCase(tag_name) && "SUPPLEMENT".equalsIgnoreCase(Applicant_type)){
					String xml_str = int_xml.get(parent_tag);
					xml_str = getSupplement_PersonDetails(formObject,callName);
					//CreditCard.mLogger.info("CC Integration post creating PersonNm tag CC Integration: "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else{
					//CreditCard.mLogger.info("inside supplement tag name else condition: "+tag_name);
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
				}

			}

		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + "+e.getMessage());
			PersonalLoanS.mLogger.info("CC_INtegration Exception in CARD services custom function:  ");
			PLCommon.printException(e);
		}
		return int_xml;
	}



	public String dummyResponse(String callName)
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String response="";

		if(("CUSTOMER_DETAILS").equalsIgnoreCase(callName)){
			response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>CUSTOMER_DETAILS</MsgFormat><MsgVersion>0000</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153422713637417</MessageId><Extra1>REP||LAXMANRET.LAXMANRET</Extra1><Extra2>2018-08-14T10:12:17.561+04:00</Extra2></EE_EAI_HEADER><FetchCustomerDetailsRes><BankId>RAK</BankId><CIFDet><CIFID>2221180</CIFID><RetCorpFlag>R</RetCorpFlag><CifType>EB</CifType><CustomerStatus>ACTVE</CustomerStatus><CustomerSegment>PBD</CustomerSegment><CustomerSubSeg>PBN</CustomerSubSeg><AECBConsentHeld>Y</AECBConsentHeld><IsStaff>N</IsStaff><IsPremium>N</IsPremium><IsWMCustomer>N</IsWMCustomer><BlackListFlag>N</BlackListFlag><NegativeListOvlFlag>N</NegativeListOvlFlag><CreditGradeCode>P2</CreditGradeCode><PhnDet><PhnType>CELLPH1</PhnType><PhnPrefFlag>Y</PhnPrefFlag><PhnCountryCode>971</PhnCountryCode><PhnLocalCode>00971</PhnLocalCode><PhoneNo>97100971</PhoneNo></PhnDet><PhnDet><PhnType>HOMEPH1</PhnType><PhnPrefFlag>N</PhnPrefFlag><PhnCountryCode>971</PhnCountryCode><PhnLocalCode>00971</PhnLocalCode><PhoneNo>97100971</PhoneNo></PhnDet><PhnDet><PhnType>OFFCPH1</PhnType><PhnPrefFlag>N</PhnPrefFlag><PhnCountryCode>971</PhnCountryCode><PhnLocalCode>00971</PhnLocalCode><PhoneNo>97100971</PhoneNo></PhnDet><PhnDet><PhnType>OVHOMEPH</PhnType><PhnPrefFlag>N</PhnPrefFlag><PhnCountryCode>0091</PhnCountryCode><PhnLocalCode>982221180</PhnLocalCode><PhoneNo>0091982221180</PhoneNo></PhnDet><AddrDet><AddressType>Home</AddressType><EffectiveFrom>2014-06-27</EffectiveFrom><EffectiveTo>2018-06-27</EffectiveTo><HoldMailFlag>N</HoldMailFlag><ReturnFlag>N</ReturnFlag><AddrPrefFlag>N</AddrPrefFlag><AddressLine1>12345</AddressLine1><AddressLine2>PREMISE NAME FOR2221180</AddressLine2><AddressLine3>STREET NAME FOR   2221180</AddressLine3><AddressLine4>NA</AddressLine4><POBox>12345</POBox><State>UNZ</State><City>DXB</City><Country>IN</Country></AddrDet><AddrDet><AddressType>Mailing</AddressType><EffectiveFrom>2014-07-25</EffectiveFrom><EffectiveTo>2099-12-31</EffectiveTo><HoldMailFlag>N</HoldMailFlag><ReturnFlag>N</ReturnFlag><AddrPrefFlag>N</AddrPrefFlag><AddressLine1>12345</AddressLine1><AddressLine2>PREMISE NAME FOR2221180</AddressLine2><AddressLine3>STREET NAME FOR   2221180</AddressLine3><AddressLine4>NA</AddressLine4><POBox>12345</POBox><State>VIR</State><City>DXB</City><Country>AE</Country></AddrDet><AddrDet><AddressType>Swift</AddressType><EffectiveFrom>2016-05-30</EffectiveFrom><EffectiveTo>2099-12-31</EffectiveTo><AddrPrefFlag>N</AddrPrefFlag></AddrDet><AddrDet><AddressType>OFFICE</AddressType><EffectiveFrom>2014-08-13</EffectiveFrom><EffectiveTo>2099-12-31</EffectiveTo><HoldMailFlag>N</HoldMailFlag><ReturnFlag>N</ReturnFlag><AddrPrefFlag>Y</AddrPrefFlag><AddressLine1>12345</AddressLine1><AddressLine2>PREMISE NAME FOR2221180</AddressLine2><AddressLine3>STREET NAME FOR   2221180</AddressLine3><AddressLine4>NA</AddressLine4><POBox>12345</POBox><zipcode>30303</zipcode><State>VIR</State><City>DXB</City><Country>AE</Country></AddrDet><AddrDet><AddressType>RESIDENCE</AddressType><EffectiveFrom>2014-08-13</EffectiveFrom><EffectiveTo>2099-12-31</EffectiveTo><HoldMailFlag>N</HoldMailFlag><ReturnFlag>N</ReturnFlag><AddrPrefFlag>N</AddrPrefFlag><AddressLine1>12345</AddressLine1><AddressLine2>PREMISE NAME FOR   2221180</AddressLine2><AddressLine3>STREET NAME FOR2221180</AddressLine3><AddressLine4>LOCALITY NAME FOR2221180</AddressLine4><ResidenceType>R</ResidenceType><POBox>12345</POBox><State>JNE</State><City>DXB</City><Country>AE</Country></AddrDet><EmlDet><EmlType>ELML1</EmlType><EmlPrefFlag>N</EmlPrefFlag><Email>TEST11@RAKBANKTST.AE</Email></EmlDet><EmlDet><EmlType>ELML2</EmlType><EmlPrefFlag>N</EmlPrefFlag><Email>1234567891234567@RAKBANK.AE</Email></EmlDet><EmlDet><EmlType>HOMEEML</EmlType><EmlPrefFlag>Y</EmlPrefFlag><Email>1234567891234567@RAKBANK.AE</Email></EmlDet><EmlDet><EmlType>WORKEML</EmlType><EmlPrefFlag>N</EmlPrefFlag><Email>TEST11@RAKBANKTST.AE</Email></EmlDet><DocDet><DocType>PPT</DocType><DocTypeDesc>PASSPORT</DocTypeDesc><DocNo>AE2221180</DocNo><DocIssDate>2015-02-04</DocIssDate><DocExpDate>2025-12-31</DocExpDate></DocDet><DocDet><DocType>VISA</DocType><DocTypeDesc>VISA FILE NUMBER</DocTypeDesc><DocNo>20120142354787</DocNo><DocIssDate>2016-07-01</DocIssDate><DocExpDate>2020-07-16</DocExpDate></DocDet><DocDet><DocType>EMID</DocType><DocTypeDesc>EMIRATES ID</DocTypeDesc><DocNo>784198812221180</DocNo><DocIssDate>2016-07-01</DocIssDate><DocExpDate>2025-12-31</DocExpDate></DocDet><BuddyRMDetails><BuddyRMName>PERSONAL BANKER</BuddyRMName><BuddyRMPhone>8004048</BuddyRMPhone></BuddyRMDetails><BackupRMDetails></BackupRMDetails><RetAddnlDet><Title>MR.</Title><ShortName>F  L</ShortName><CustomerName>FIRST NAME FOR 2221180  LAST NAME FOR  2221180</CustomerName><FName>FIRST NAME FOR 2221180</FName><LName>LAST NAME FOR  2221180</LName><Gender>M</Gender><DOB>1988-01-01</DOB><MinorFlg>N</MinorFlg><MaritialStatus>S</MaritialStatus><MotherMaidenName>MOM</MotherMaidenName><Nationality>IN</Nationality><ResidentCountry>AE</ResidentCountry><CustType>EB</CustType><NoOfDepndant>3</NoOfDepndant><SalaryTranflag>N</SalaryTranflag><ResideSince>2014-07-24</ResideSince><CustomerNRIFlag>N</CustomerNRIFlag><EmpType>S</EmpType><EmployerCode>001</EmployerCode><EmployerName>MINISTRY OF DEFENCE</EmployerName><Department>ENGINEERING DEPT</Department><EmpStatus>2</EmpStatus><DOJ>2014-07-24</DOJ><Designation>03</Designation><CurrentJobPeriod>0</CurrentJobPeriod><GrossSalary>4E+4</GrossSalary><TotHouseholdInc>7.125E+3</TotHouseholdInc><InvIncome>0E+0</InvIncome><OthIncome>0E+0</OthIncome><Commissions>0E+0</Commissions><AssessedIncome>0E+0</AssessedIncome><HRA>0E+0</HRA><RentInc>0E+0</RentInc><MnthlyDispInc>7.125E+3</MnthlyDispInc><MnthlyHouseholdExp>0E+0</MnthlyHouseholdExp><AcctType>ODA</AcctType><AcctNum>8332221180901</AcctNum></RetAddnlDet><FatcaDet><USRelation>O</USRelation><TIN>NA</TIN><FatcaReason>NA</FatcaReason><DocumentsCollected>ID DOC!SELF-ATTEST FORM</DocumentsCollected></FatcaDet><KYCDet><KYCReviewDate>2020-12-31</KYCReviewDate><PEP>NPEP</PEP></KYCDet><OECDDet><CityOfBirth>BVT</CityOfBirth><CountryOfBirth>AD</CountryOfBirth><CRSUnDocFlg>Y</CRSUnDocFlg><CRSUndocFlgReason>CIF UPDATE WITHIN UAE</CRSUndocFlgReason><ReporCntryDet><CntryOfTaxRes>AE</CntryOfTaxRes><TINNumber>3433</TINNumber><MiscellaneousID>16986490</MiscellaneousID></ReporCntryDet><ReporCntryDet><CntryOfTaxRes>AE</CntryOfTaxRes><TINNumber>3433</TINNumber><NoTINReason>NA</NoTINReason><MiscellaneousID>16997075</MiscellaneousID></ReporCntryDet><ReporCntryDet><CntryOfTaxRes>AE</CntryOfTaxRes><TINNumber>234234</TINNumber><MiscellaneousID>16991110</MiscellaneousID></ReporCntryDet></OECDDet></CIFDet></FetchCustomerDetailsRes></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";

		}
		else if (("COMPLIANCE_CHECK").equalsIgnoreCase(callName)){
			response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"  encoding=\"utf-8\"?> <EE_EAI_MESSAGE> <EE_EAI_HEADER> <MsgFormat>COMPLIANCE_CHECK</MsgFormat> <MsgVersion>0001</MsgVersion> <RequestorChannelId>CAS</RequestorChannelId> <RequestorUserId>RAKUSER</RequestorUserId> <RequestorLanguage>E</RequestorLanguage> <RequestorSecurityInfo>secure</RequestorSecurityInfo> <ReturnCode>000</ReturnCode> <ReturnDesc>Success</ReturnDesc> <MessageId>123123453</MessageId><Extra1>REP||SHELL.JOHN</Extra1> <Extra2>YYYY-MM-DDThh:mm:ss.mmm+hh:mm</Extra2> </EE_EAI_HEADER> <ComplianceCheckResponse> <SystemID>String</SystemID><FilterationDate>2018/09/11 22:17:56 </FilterationDate><StatusBehavior>1</StatusBehavior><StatusName>FALSE</StatusName><AlertDetails><![CDATA[Suspect(s) detected by OFAC-Agent:3SystemId: Associate: =============================Suspect detected #1OFAC ID:AS04979205MATCH: 0.00TAG: NAMMATCHINGTEXT: MANOJ KUMAR, RESULT: (0)NAME: KUMAR, MONOJ Synonyms: noneADDRESS: Synonyms: noneCITY: https://accuity.worldcompliance.com/signin.aspx?ent=158f588d-2b86-41e8-8432-5b7663dbf90aTYS: 1ISN: 0=============================   *** INTERNAL OFAC DETAILS ***HasSndRcvInLimited: 0|AS04979205|0.00|NAM|3|13|-1|-1|-1|-1|-1|-1|-1|-1||AS08151740|0.00|NAM|3|13|-1|-1|-1|-1|-1|-1|-1|-1||AS05752051|0.00|NAM|3|13|-1|-1|-1|-1|-1|-1|-1|-1|]]></AlertDetails></ComplianceCheckResponse> </EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(("dectech").equalsIgnoreCase(callName)){
			response="<APMQPUTGET_Output><MQ_RESPONSE_XML>?<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Header><ServiceId>CallProcessManager</ServiceId><ServiceType>ProductEligibility</ServiceType><ServiceProviderId>DECTECH</ServiceProviderId><ServiceChannelId>CAS</ServiceChannelId><RequestID>CAS153226201324026</RequestID><TimeStampyyyymmddhhmmsss>2018-07-22T16:20:13.874</TimeStampyyyymmddhhmmsss><RequestLifeCycleStage>CallProcessManagerResponse</RequestLifeCycleStage><MessageStatus>Success</MessageStatus></soap:Header><soap:Body><CallProcessManagerResponse xmlns=\"http://tempuri.org/\"><CallProcessManagerResult><ProcessManagerResponse><Application><Channel>PL</Channel><CallType>PM</CallType><ApplicationNumber>PL-0040008398-process</ApplicationNumber><ReturnDateTime>20180722162013</ReturnDateTime><SystemErrorCode></SystemErrorCode><SystemErrorMessage></SystemErrorMessage><ReturnError RecordNumber=\"0\" /></Application><Instinct_Actions><Instinct_Action_Output_XML /></Instinct_Actions><PM_Results><Random_Number>351</Random_Number><DBR>0.00</DBR><Decision_Results name=\"MAC\"><PM_Decision_Results_Data><PM_Decision_Results>  <AppKey>PL-0040008398-processPL</AppKey>  <Decision_Objective>4</Decision_Objective>  <Decision_Sequence_Number>1</Decision_Sequence_Number>  <Last_Update_Date>2018-07-22T16:20:13.623+04:00</Last_Update_Date>  <Default_Decision>A</Default_Decision>  <Default_Reason>A999 - System Approve</Default_Reason>  <Default_Document>0</Default_Document>  <System_Decision>R</System_Decision>  <System_Document>0</System_Document>  <Decision_Node_Id>2.2.1</Decision_Node_Id>  <Decision_Test_Group>0</Decision_Test_Group></PM_Decision_Results></PM_Decision_Results_Data><PM_Reason_Codes_Data><PM_Reason_Codes>  <Decision_Objective>4</Decision_Objective>  <Decision_Sequence_Number>1</Decision_Sequence_Number>  <Sequence_Number>1</Sequence_Number>  <Reason_Decision>R</Reason_Decision>  <Reason_Code>RC025</Reason_Code>  <Reason_Description>IB - Not Currently Current</Reason_Description>  <Criteria_Name>PL Internal Performance_001</Criteria_Name>  <Letter_Code />  <Document />  <Letter_Reason />  <Category>C - Internal Performance</Category></PM_Reason_Codes></PM_Reason_Codes_Data></Decision_Results><Strategy_Results name=\"Eligibility\"><PM_Strategy_MaxLendingAmount_Data><PM_Strategy_MaxLendingAmount_Results>  <Policy_Number>1</Policy_Number>  <Adjustment_Number>0</Adjustment_Number>  <Max_Lending_Amount>400000.00</Max_Lending_Amount>  <Last_Update_Date>2018-07-22T16:20:13.7+04:00</Last_Update_Date>  <Entry_Name>PL TAI x10 Unlimited</Entry_Name>  <Use_Requested_Limit_Flag>false</Use_Requested_Limit_Flag>  <Existing_Limit_Increase_Flag>false</Existing_Limit_Increase_Flag>  <Limit_Increase_Percent>0.00</Limit_Increase_Percent>  <Use_Characteristic_As_Limit_Flag>true</Use_Characteristic_As_Limit_Flag>  <Limit_Table_Id>1</Limit_Table_Id>  <Limit_Field_Number>22</Limit_Field_Number>  <Use_Characteristic_As_Limit_Multiplier>10.000</Use_Characteristic_As_Limit_Multiplier>  <Fixed_Amount_Flag>false</Fixed_Amount_Flag>  <Fixed_Amount>0</Fixed_Amount>  <Base_Percentage_Increase_Flag>false</Base_Percentage_Increase_Flag>  <Base_Percentage_Increase_Percent>0.00</Base_Percentage_Increase_Percent>  <Limit_Minimum>0</Limit_Minimum>  <Limit_Maximum>999999999</Limit_Maximum>  <Limit_Rounding_Factor1>1</Limit_Rounding_Factor1>  <Limit_Rounding_Factor2>1</Limit_Rounding_Factor2>  <Limit_Rounding_Factor3>1</Limit_Rounding_Factor3>  <Limit_Rounding_Factor4>1</Limit_Rounding_Factor4>  <Limit_Rounding_Cutoff1>100000</Limit_Rounding_Cutoff1>  <Limit_Rounding_Cutoff2>200000</Limit_Rounding_Cutoff2>  <Limit_Rounding_Cutoff3>300000</Limit_Rounding_Cutoff3>  <Limit_Reason_Code />  <Max_Limit_Table_Id>0</Max_Limit_Table_Id>  <Max_Limit_Field_Number>0</Max_Limit_Field_Number>  <Max_Limit_Multiplier>0.00</Max_Limit_Multiplier>  <Use_Round_Up_Factor1_Flag>false</Use_Round_Up_Factor1_Flag>  <Use_Round_Up_Factor2_Flag>false</Use_Round_Up_Factor2_Flag>  <Use_Round_Up_Factor3_Flag>false</Use_Round_Up_Factor3_Flag>  <Use_Round_Up_Factor4_Flag>false</Use_Round_Up_Factor4_Flag>  <Strategy_Node_Id>1.1.1.2.2.2</Strategy_Node_Id>  <Strategy_Test_Group>0</Strategy_Test_Group>  <Tree_Error_Field_Label />  <Tree_Error_Field_Value /></PM_Strategy_MaxLendingAmount_Results></PM_Strategy_MaxLendingAmount_Data></Strategy_Results><Strategy_Results name=\"Max Loan Amount\"><PM_Strategy_MaxLendingAmount_Data><PM_Strategy_MaxLendingAmount_Results>  <Policy_Number>1</Policy_Number>  <Adjustment_Number>0</Adjustment_Number>  <Max_Lending_Amount>0.00</Max_Lending_Amount>  <Last_Update_Date>2018-07-22T16:20:13.717+04:00</Last_Update_Date>  <Entry_Name>Zero</Entry_Name>  <Use_Requested_Limit_Flag>false</Use_Requested_Limit_Flag>  <Existing_Limit_Increase_Flag>false</Existing_Limit_Increase_Flag>  <Limit_Increase_Percent>0.00</Limit_Increase_Percent>  <Use_Characteristic_As_Limit_Flag>false</Use_Characteristic_As_Limit_Flag>  <Limit_Table_Id>0</Limit_Table_Id>  <Limit_Field_Number>0</Limit_Field_Number>  <Use_Characteristic_As_Limit_Multiplier>0.000</Use_Characteristic_As_Limit_Multiplier>  <Fixed_Amount_Flag>true</Fixed_Amount_Flag>  <Fixed_Amount>0</Fixed_Amount>  <Base_Percentage_Increase_Flag>false</Base_Percentage_Increase_Flag>  <Base_Percentage_Increase_Percent>0.00</Base_Percentage_Increase_Percent>  <Limit_Minimum>0</Limit_Minimum>  <Limit_Maximum>0</Limit_Maximum>  <Limit_Rounding_Factor1>100</Limit_Rounding_Factor1>  <Limit_Rounding_Factor2>100</Limit_Rounding_Factor2>  <Limit_Rounding_Factor3>100</Limit_Rounding_Factor3>  <Limit_Rounding_Factor4>100</Limit_Rounding_Factor4>  <Limit_Rounding_Cutoff1>100000</Limit_Rounding_Cutoff1>  <Limit_Rounding_Cutoff2>200000</Limit_Rounding_Cutoff2>  <Limit_Rounding_Cutoff3>300000</Limit_Rounding_Cutoff3>  <Limit_Reason_Code />  <Max_Limit_Table_Id>0</Max_Limit_Table_Id>  <Max_Limit_Field_Number>0</Max_Limit_Field_Number>  <Max_Limit_Multiplier>0.00</Max_Limit_Multiplier>  <Use_Round_Up_Factor1_Flag>false</Use_Round_Up_Factor1_Flag>  <Use_Round_Up_Factor2_Flag>false</Use_Round_Up_Factor2_Flag>  <Use_Round_Up_Factor3_Flag>false</Use_Round_Up_Factor3_Flag>  <Use_Round_Up_Factor4_Flag>false</Use_Round_Up_Factor4_Flag>  <Strategy_Node_Id>1</Strategy_Node_Id>  <Strategy_Test_Group>0</Strategy_Test_Group>  <Tree_Error_Field_Label />  <Tree_Error_Field_Value /></PM_Strategy_MaxLendingAmount_Results></PM_Strategy_MaxLendingAmount_Data></Strategy_Results><Strategy_Results name=\"Max Exposure\"><PM_Strategy_MaxLendingAmount_Data><PM_Strategy_MaxLendingAmount_Results>  <Policy_Number>1</Policy_Number>  <Adjustment_Number>0</Adjustment_Number>  <Max_Lending_Amount>0.00</Max_Lending_Amount>  <Last_Update_Date>2018-07-22T16:20:13.747+04:00</Last_Update_Date>  <Entry_Name>Zero</Entry_Name>  <Use_Requested_Limit_Flag>false</Use_Requested_Limit_Flag>  <Existing_Limit_Increase_Flag>false</Existing_Limit_Increase_Flag>  <Limit_Increase_Percent>0.00</Limit_Increase_Percent>  <Use_Characteristic_As_Limit_Flag>false</Use_Characteristic_As_Limit_Flag>  <Limit_Table_Id>0</Limit_Table_Id>  <Limit_Field_Number>0</Limit_Field_Number>  <Use_Characteristic_As_Limit_Multiplier>0.000</Use_Characteristic_As_Limit_Multiplier>  <Fixed_Amount_Flag>true</Fixed_Amount_Flag>  <Fixed_Amount>0</Fixed_Amount>  <Base_Percentage_Increase_Flag>false</Base_Percentage_Increase_Flag>  <Base_Percentage_Increase_Percent>0.00</Base_Percentage_Increase_Percent>  <Limit_Minimum>0</Limit_Minimum>  <Limit_Maximum>0</Limit_Maximum>  <Limit_Rounding_Factor1>100</Limit_Rounding_Factor1>  <Limit_Rounding_Factor2>100</Limit_Rounding_Factor2>  <Limit_Rounding_Factor3>100</Limit_Rounding_Factor3>  <Limit_Rounding_Factor4>100</Limit_Rounding_Factor4>  <Limit_Rounding_Cutoff1>100000</Limit_Rounding_Cutoff1>  <Limit_Rounding_Cutoff2>200000</Limit_Rounding_Cutoff2>  <Limit_Rounding_Cutoff3>300000</Limit_Rounding_Cutoff3>  <Limit_Reason_Code />  <Max_Limit_Table_Id>0</Max_Limit_Table_Id>  <Max_Limit_Field_Number>0</Max_Limit_Field_Number>  <Max_Limit_Multiplier>0.00</Max_Limit_Multiplier>  <Use_Round_Up_Factor1_Flag>false</Use_Round_Up_Factor1_Flag>  <Use_Round_Up_Factor2_Flag>false</Use_Round_Up_Factor2_Flag>  <Use_Round_Up_Factor3_Flag>false</Use_Round_Up_Factor3_Flag>  <Use_Round_Up_Factor4_Flag>false</Use_Round_Up_Factor4_Flag>  <Strategy_Node_Id>1</Strategy_Node_Id>  <Strategy_Test_Group>0</Strategy_Test_Group>  <Tree_Error_Field_Label />  <Tree_Error_Field_Value /></PM_Strategy_MaxLendingAmount_Results></PM_Strategy_MaxLendingAmount_Data></Strategy_Results><Strategy_Results name=\"Interest Rate\"><PM_Strategy_PricingTerm_Data><PM_Strategy_PricingTerm_Results>  <Last_Update_Date>2018-07-22T16:20:13.747+04:00</Last_Update_Date>  <Entry_Name>Fixed 6.49</Entry_Name>  <Interest_Rate>6.49</Interest_Rate>  <Max_Loan_Term>0</Max_Loan_Term>  <Strategy_Node_Id>2.1.2</Strategy_Node_Id>  <Strategy_Test_Group>0</Strategy_Test_Group></PM_Strategy_PricingTerm_Results></PM_Strategy_PricingTerm_Data></Strategy_Results><Scoring_Results name=\"Recalculation\"><PM_Scoring_Results_Data><PM_Scoring_Results>  <Scoring_Sequence_Number>1</Scoring_Sequence_Number>  <Scoring_Objective>4</Scoring_Objective>  <Last_Update_Date>2018-07-22T16:20:13.763+04:00</Last_Update_Date>  <Score>0.000000</Score>  <Score_Node_Id>1</Score_Node_Id>  <Scorecard_Id>Null Scorecard</Scorecard_Id>  <Grade>0</Grade>  <Score_Test_Group>0</Score_Test_Group></PM_Scoring_Results></PM_Scoring_Results_Data></Scoring_Results><Scoring_Results name=\"Application Score\"><PM_Scoring_Results_Data /></Scoring_Results><Decision_Results name=\"Eligibility Decision\"><PM_Decision_Results_Data><PM_Decision_Results>  <AppKey>PL-0040008398-processPL</AppKey>  <Decision_Objective>3</Decision_Objective>  <Decision_Sequence_Number>1</Decision_Sequence_Number>  <Last_Update_Date>2018-07-22T16:20:13.827+04:00</Last_Update_Date>  <Default_Decision>A</Default_Decision>  <Default_Reason>A999 - System Approve</Default_Reason>  <Default_Document>0</Default_Document>  <System_Decision>A</System_Decision>  <System_Document>0</System_Document>  <Decision_Node_Id>2.2.1</Decision_Node_Id>  <Decision_Test_Group>0</Decision_Test_Group></PM_Decision_Results></PM_Decision_Results_Data><PM_Reason_Codes_Data><PM_Reason_Codes>  <Decision_Objective>3</Decision_Objective>  <Decision_Sequence_Number>1</Decision_Sequence_Number>  <Sequence_Number>1</Sequence_Number>  <Reason_Decision>A</Reason_Decision>  <Reason_Code>A999</Reason_Code>  <Reason_Description>System Approve</Reason_Description>  <Criteria_Name />  <Letter_Code />  <Document />  <Letter_Reason /></PM_Reason_Codes></PM_Reason_Codes_Data></Decision_Results><Strategy_Results name=\"Additional Eligible Cards\"><PM_Strategy_Verification_Data><PM_Strategy_Verification_Results>  <Last_Update_Date>2018-07-22T16:20:13.84+04:00</Last_Update_Date>  <Entry_Name>Eligible Cards UAE</Entry_Name>  <Number_Of_Visits>0</Number_Of_Visits><Visits><Code>4</Code><Type_Of_Visit>KALYAN-UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>9</Code><Type_Of_Visit>MPL-UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>14</Code><Type_Of_Visit>MRBH GOLD UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>19</Code><Type_Of_Visit>MRBH PLTM UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>26</Code><Type_Of_Visit>MRBH WORLD UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>31</Code><Type_Of_Visit>MY RAK CARD-UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>36</Code><Type_Of_Visit>MY RAK ISLAMIC-UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>40</Code><Type_Of_Visit>REDCARD-UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>51</Code><Type_Of_Visit>TITANIUM-UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>55</Code><Type_Of_Visit>VCLASSIC-UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>60</Code><Type_Of_Visit>VGOLD-UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>65</Code><Type_Of_Visit>WORLD-UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits>  <Number_Of_Calls>0</Number_Of_Calls><Calls><Code></Code><Type_Of_Call></Type_Of_Call><Mandatory_Flag></Mandatory_Flag></Calls>  <Strategy_Node_Id>1.2.1</Strategy_Node_Id>  <Strategy_Test_Group>0</Strategy_Test_Group></PM_Strategy_Verification_Results></PM_Strategy_Verification_Data></Strategy_Results></PM_Results><PM_Outputs><Application><Output_Accommodation_Allowance>0.00</Output_Accommodation_Allowance><Output_Additional_Amount>100000.00</Output_Additional_Amount><Output_Affordable_EMI>20000.00</Output_Affordable_EMI><Output_CPV_Waiver>N</Output_CPV_Waiver><Output_Decision>R</Output_Decision><Output_Delegation_Authority>AM</Output_Delegation_Authority><Output_Eligible_Amount>400000.00</Output_Eligible_Amount><Output_Eligible_Amount_Path>PL Limit Matrix Sub Process Type [NAT-NEW/TOP/TKO]\\PL App Is Referred OR Cat B Declined\\PL Employer Is MOD\\NOT PL Is Pensioner\\PL Age Segmentation [Adult]\\PL Employer Contract Type Segmentation [Government]</Output_Eligible_Amount_Path><Output_Eligible_Cards>[{\"Card_Product\":\"TITANIUM-UAE\",\"Credit_Limit\":32500.00,\"Flag\":\"E\"},{\"Card_Product\":\"KALYAN-UAE\",\"Credit_Limit\":32500.00,\"Flag\":\"E\"},{\"Card_Product\":\"MPL-UAE\",\"Credit_Limit\":0.00,\"Flag\":\"N\"},{\"Card_Product\":\"MRBH GOLD UAE\",\"Credit_Limit\":0.00,\"Flag\":\"N\"},{\"Card_Product\":\"MRBH PLTM UAE\",\"Credit_Limit\":0.00,\"Flag\":\"N\"},{\"Card_Product\":\"MRBH WORLD UAE\",\"Credit_Limit\":0.00,\"Flag\":\"N\"},{\"Card_Product\":\"MY RAK CARD-UAE\",\"Credit_Limit\":0.00,\"Flag\":\"N\"},{\"Card_Product\":\"MY RAK ISLAMIC-UAE\",\"Credit_Limit\":0.00,\"Flag\":\"N\"},{\"Card_Product\":\"REDCARD-UAE\",\"Credit_Limit\":0.00,\"Flag\":\"N\"},{\"Card_Product\":\"VCLASSIC-UAE\",\"Credit_Limit\":0.00,\"Flag\":\"N\"},{\"Card_Product\":\"VGOLD-UAE\",\"Credit_Limit\":0.00,\"Flag\":\"N\"},{\"Card_Product\":\"WORLD-UAE\",\"Credit_Limit\":0.00,\"Flag\":\"N\"}]</Output_Eligible_Cards><Output_Existing_DBR>0.0000</Output_Existing_DBR><Output_Final_Amount>100000.00</Output_Final_Amount><Output_Final_DBR>5.9276</Output_Final_DBR><Output_Interest_Rate>6.49</Output_Interest_Rate><Output_Net_Salary_DBR>5.9276</Output_Net_Salary_DBR><Output_Num_Eligible_Cards>7</Output_Num_Eligible_Cards><Output_Salary_Multiples>2.50</Output_Salary_Multiples><Output_TAI>40000.00</Output_TAI></Application></PM_Outputs></ProcessManagerResponse></CallProcessManagerResult></CallProcessManagerResponse></soap:Body></soap:Envelope></MQ_RESPONSE_XML></APMQPUTGET_Output>";

		}

		else if(("CUSTOMER_ELIGIBILITY").equalsIgnoreCase(callName)){
			response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>CUSTOMER_ELIGIBILITY</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153382588661616</MessageId><Extra1>REP||BPM.123</Extra1><Extra2>2018-08-09T06:44:51.296+04:00</Extra2></EE_EAI_HEADER><CustomerEligibilityResponse><BankId>RAK</BankId><CustomerDetails><SearchType>Internal</SearchType><CustId>2221180</CustId><PassportNum>AE2221180</PassportNum><BlacklistFlag>Y</BlacklistFlag><DuplicationFlag>N</DuplicationFlag><NegatedFlag>N</NegatedFlag><Products><ProductType>ACCOUNT ONLY</ProductType><NoOfProducts>1</NoOfProducts></Products><Products><ProductType>CAPS</ProductType><NoOfProducts>1</NoOfProducts></Products></CustomerDetails></CustomerEligibilityResponse></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}

		else if(("NEW_CUSTOMER_REQ").equalsIgnoreCase(callName)){
			response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>NEW_CUSTOMER_REQ</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RBDUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS15341561637020</MessageId><Extra1>REP||CAS.123</Extra1><Extra2>2018-08-13T02:29:26.540+04:00</Extra2></EE_EAI_HEADER><NewCustomerResponse><BankId>RAK</BankId><CIFId>2677277</CIFId><Desc>Retail Customer successfully created with CIFID 2677277</Desc><Entity>Retail Customer</Entity><Service>CIFRetailCustomerCreate</Service><Status>Success</Status></NewCustomerResponse></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";

		}
		else if(("CUSTOMER_EXPOSURE").equalsIgnoreCase(callName)){
			response="<MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>CUSTOMER_EXPOSURE</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153416576411240</MessageId><Extra1>REP||SHELL.JOHN</Extra1><Extra2>2018-08-13T05:09:25.164+04:00</Extra2></EE_EAI_HEADER><CustomerExposureResponse><RequestType>InternalExposure</RequestType><IsDirect>Y</IsDirect><CustInfo><CustId><CustIdType>CIF Id</CustIdType><CustIdValue>0232626</CustIdValue></CustId><FullNm></FullNm><BirthDt>1973-06-29</BirthDt><Nationality>INDIAN</Nationality><CustSegment>PERSONAL BANKING</CustSegment><CustSubSegment>PB - NORMAL</CustSubSegment><RMName>PERSONAL BANKER</RMName><CreditGrade>P2 - PERSONAL - ACCEPTABLE CREDIT</CreditGrade><ECRN>038275800</ECRN><BorrowingCustomer>N</BorrowingCustomer></CustInfo><ProductExposureDetails><LoanDetails><AgreementId>20424288</AgreementId><LoanStat>A</LoanStat><LoanType>PL</LoanType><LoanDesc></LoanDesc><TotalNoOfInstalments>48</TotalNoOfInstalments><KeyDt><KeyDtType>LoanApprovedDate</KeyDtType><KeyDtValue>2017-11-06</KeyDtValue></KeyDt><KeyDt><KeyDtType>LoanMaturityDate</KeyDtType><KeyDtValue>2021-11-15</KeyDtValue></KeyDt><CurCode>AED</CurCode><AmountDtls><AmtType>TotalLoanAmount</AmtType><Amt>241000</Amt></AmountDtls><AmountDtls><AmtType>TotalOutstandingAmt</AmtType><Amt>220687.84</Amt></AmountDtls><AmountDtls><AmtType>NextInstallmentAmt</AmtType><Amt>6018</Amt></AmountDtls></LoanDetails><CardDetails><CardEmbossNum>038275800</CardEmbossNum><CardStatus>NORM</CardStatus><CardType>TITANIUM-EXPAT</CardType><CustRoleType>Primary</CustRoleType><KeyDt><KeyDtType>ApplicationCreationDate</KeyDtType><KeyDtValue>2015-11-09</KeyDtValue></KeyDt><KeyDt><KeyDtType>ExpiryDate</KeyDtType><KeyDtValue>2025-01-31</KeyDtValue></KeyDt><CurCode></CurCode><AmountDtls><AmtType>CreditLimit</AmtType><Amt>39000</Amt></AmountDtls><AmountDtls><AmtType>OutstandingAmt</AmtType><Amt>-9256.23</Amt></AmountDtls><AmountDtls><AmtType>OverdueAmt</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>CurrMaxUtil</AmtType><Amt>23.73</Amt></AmountDtls></CardDetails><CardDetails><CardEmbossNum>038275801</CardEmbossNum><CardStatus>NORM</CardStatus><CardType>TITANIUM-EXPAT</CardType><CustRoleType>Secondary</CustRoleType><KeyDt><KeyDtType>ApplicationCreationDate</KeyDtType><KeyDtValue>2015-11-09</KeyDtValue></KeyDt><KeyDt><KeyDtType>ExpiryDate</KeyDtType><KeyDtValue>2020-01-31</KeyDtValue></KeyDt><CurCode></CurCode><AmountDtls><AmtType>CreditLimit</AmtType><Amt>2000</Amt></AmountDtls><AmountDtls><AmtType>OutstandingAmt</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>OverdueAmt</AmtType><Amt>0</Amt></AmountDtls></CardDetails><AcctDetails><AcctId>0002232626001</AcctId><IBANNumber>AE120400000002232626001</IBANNumber><AcctStat>ACTIVE</AcctStat><AcctCur>AED</AcctCur><AcctNm>GERLIE UAT RAMACHANDRAN</AcctNm><AcctType>CURRENT ACCOUNT</AcctType><AcctSegment>PBD</AcctSegment><AcctSubSegment>PBN</AcctSubSegment><CustRoleType>Main</CustRoleType><KeyDt><KeyDtType>AccountOpenDate</KeyDtType><KeyDtValue>2008-11-16</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitSactionDate</KeyDtType><KeyDtValue>2008-11-16</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitExpiryDate</KeyDtType><KeyDtValue>2012-06-08</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitStartDate</KeyDtType><KeyDtValue>2008-11-16</KeyDtValue></KeyDt><AmountDtls><AmtType>AvailableBalance</AmtType><Amt>847.30</Amt></AmountDtls><AmountDtls><AmtType>ClearBalanceAmount</AmtType><Amt>847.3</Amt></AmountDtls><AmountDtls><AmtType>LedgerBalance</AmtType><Amt>847.30</Amt></AmountDtls><AmountDtls><AmtType>EffectiveAvailableBalance</AmtType><Amt>847.30</Amt></AmountDtls><AmountDtls><AmtType>CumulativeDebitAmount</AmtType><Amt>1444110.31</Amt></AmountDtls><AmountDtls><AmtType>SanctionLimit</AmtType><Amt>0</Amt></AmountDtls><WriteoffStat>Y</WriteoffStat><WorstDelay24Months>P2</WorstDelay24Months><MonthsOnBook>117.00</MonthsOnBook><LastRepmtDt>AUG</LastRepmtDt><IsCurrent>Y</IsCurrent><ChargeOffFlag>N</ChargeOffFlag><DelinquencyInfo><BucketType>DaysPastDue</BucketType><BucketValue>0</BucketValue></DelinquencyInfo></AcctDetails></ProductExposureDetails></CustomerExposureResponse></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}

		else if(("BLACKLIST_DETAILS").equalsIgnoreCase(callName)){
			response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>BLACKLIST_DETAILS</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153415691394885</MessageId><Extra1>REP||BPM.123</Extra1><Extra2>2018-08-13T02:41:54.681+04:00</Extra2></EE_EAI_HEADER><CustomerBlackListResponse><BankId>RAK</BankId><Customer><CIFID>0103147</CIFID><RetailCorpFlag>R</RetailCorpFlag><PersonDetails><FirstName>FIRST NAME FOR  0103147</FirstName><MiddleName>MIDDLE NAME FOR 0103147</MiddleName><LastName>LAST NAME FOR 0103147</LastName><DateOfBirth>1988-01-01</DateOfBirth></PersonDetails><CustomerStatus>ACTVE</CustomerStatus><CustDormStatus>N</CustDormStatus><Document><DocumentType>EMID</DocumentType><DocumentRefNumber>784200010103147</DocumentRefNumber></Document><Document><DocumentType>PPT</DocumentType><DocumentRefNumber>AE0103147</DocumentRefNumber><DocumentDescription>PASSPORT</DocumentDescription></Document><ContactDetails><PhoneFax><PhoneType>CELLPH1</PhoneType><PhoneValue>00971500103147</PhoneValue></PhoneFax><PhoneFax><PhoneType>FAXO1</PhoneType><PhoneValue>00971370103147</PhoneValue></PhoneFax><PhoneFax><PhoneType>HOMEPH1</PhoneType><PhoneValue>00971370103147</PhoneValue></PhoneFax><PhoneFax><PhoneType>OFFCPH1</PhoneType><PhoneValue>00971260103147</PhoneValue></PhoneFax></ContactDetails><StatusInfo><StatusType>Black List</StatusType><StatusFlag>N</StatusFlag><StatusOverAllFlag>N</StatusOverAllFlag><StatusList></StatusList></StatusInfo><StatusInfo><StatusType>Negative List</StatusType><StatusFlag>Y</StatusFlag><StatusNotes>4 CHEQUE RETURN</StatusNotes><StatusReason>4 CHEQUE RETURN</StatusReason><StatusOverAllFlag>Y</StatusOverAllFlag><StatusList><StatusDetails><ProductType>FINACLECORE</ProductType><ReferenceNumber>103147</ReferenceNumber><Unit>0103147</Unit><ReasonNotes>FOUR CHQS RETURNED AS PER EMAIL DATED 23/12/2008 AHHANADI 25/12/2008 10:33:43WALI 30/12/2008 12:34:01 [4 Cheque Return Negation Cancelled on 21-DEC-2014 in line with CB guideline of blacklist removal after 2 Years]</ReasonNotes><ReasonCodeDesc>MIGRATION DEFAULT</ReasonCodeDesc><ReasonCode>MIGR</ReasonCode><CreationDate>2008-12-25</CreationDate><CreatedBy>COPS</CreatedBy><Status>Y</Status></StatusDetails><StatusDetails><ProductType>FINACLECORE</ProductType><ReferenceNumber>0014103147001</ReferenceNumber><Unit>0103147</Unit><ReasonNotes>4 CHEQUE RETURN</ReasonNotes><ReasonCodeDesc>4 CHEQUE RETURN</ReasonCodeDesc><ReasonCode>CHQRT</ReasonCode><CreationDate>2014-07-01</CreationDate><CreatedBy>COPS</CreatedBy><Status>Y</Status></StatusDetails><StatusDetails><ProductType>FINACLECORE</ProductType><ReferenceNumber>0014103147001</ReferenceNumber><Unit>0103147</Unit><ReasonNotes>4 CHEQUE RETURN</ReasonNotes><ReasonCodeDesc>4 CHEQUE RETURN</ReasonCodeDesc><ReasonCode>CHQRT</ReasonCode><CreationDate>2014-07-01</CreationDate><CreatedBy>COPS</CreatedBy><Status>Y</Status></StatusDetails></StatusList></StatusInfo></Customer></CustomerBlackListResponse></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}		
		else if(("CARD_INSTALLMENT_DETAILS").equalsIgnoreCase(callName)){
			response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>CARD_INSTALLMENT_DETAILS</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>1</ReturnCode><ReturnDesc>PRMSD : This account does not exist in database</ReturnDesc><MessageId>CAS153415820216963</MessageId><Extra1>REP||BPM.123</Extra1><Extra2>2018-08-13T03:03:22.996+04:00</Extra2></EE_EAI_HEADER></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(("CUSTOMER_UPDATE_REQ").equalsIgnoreCase(callName)){
			response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>CUSTOMER_UPDATE_REQ</MsgFormat><MsgVersion>001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153416189541995</MessageId><Extra1>REP||SHELL.dfgJOHN</Extra1><Extra2>2018-08-13T04:04:58.969+04:00</Extra2></EE_EAI_HEADER><CustomerDetailsUpdateRes><BankId>RAK</BankId><CIFId>0103147</CIFId><Status>S</Status><Message>SUCCESS</Message></CustomerDetailsUpdateRes></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(("NEW_CREDITCARD_REQ").equalsIgnoreCase(callName)){
			response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>NEW_CREDITCARD_REQ</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>PRMSD : Success</ReturnDesc><MessageId>CAS153416190773281</MessageId><Extra1>REP||SHELL.JOHN</Extra1><Extra2>2018-08-13T04:05:16.776+04:00</Extra2></EE_EAI_HEADER></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(("CHEQUE_BOOK_ELIGIBILITY").equalsIgnoreCase(callName)){
			response ="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>CHEQUE_BOOK_ELIGIBILITY</MsgFormat><MsgVersion>0000</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153407822315923</MessageId><Extra1>REP||INFY.JOHN</Extra1><Extra2>2018-08-12T04:50:25.274+04:00</Extra2></EE_EAI_HEADER><ChequeBookRes><ReferenceId>1168819</ReferenceId></ChequeBookRes></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(("CARD_NOTIFICATION").equalsIgnoreCase(callName)){
			response ="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>CARD_NOTIFICATION</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153286447685654</MessageId><Extra1>REP||BPM.123</Extra1><Extra2>2018-07-29T03:41:18.186+04:00</Extra2></EE_EAI_HEADER></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(("ENTITY_MAINTENANCE_REQ").equalsIgnoreCase(callName)){
			response ="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>ENTITY_MAINTENANCE_REQ</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153286850527510</MessageId><Extra1>REP||RBD.123</Extra1><Extra2>2018-07-29T04:48:26.794+04:00</Extra2></EE_EAI_HEADER><UpdateCifAndAccStatusRes><OperDesc>AcctAct</OperDesc><SuccessFlag>S</SuccessFlag></UpdateCifAndAccStatusRes></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		/*commented by sagarika due to dupliacte condition
   	else if(("BLACKLIST_DETAILS").equalsIgnoreCase(callName)){
			response = "<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>BLACKLIST_DETAILS</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153286226647936</MessageId><Extra1>REP||BPM.123</Extra1><Extra2>2018-07-29T03:04:27.285+04:00</Extra2></EE_EAI_HEADER><CustomerBlackListResponse><BankId>RAK</BankId><Customer><CIFID>2676337</CIFID><RetailCorpFlag>R</RetailCorpFlag><PersonDetails><FirstName>TRISTAN</FirstName><LastName>GROVER</LastName><DateOfBirth>1985-01-01</DateOfBirth></PersonDetails><CustomerStatus>ACTVE</CustomerStatus><CustDormStatus>N</CustDormStatus><Document><DocumentType>PPT</DocumentType><DocumentRefNumber>A5623541</DocumentRefNumber></Document><Document><DocumentType>VISA</DocumentType><DocumentRefNumber>5162534162551</DocumentRefNumber></Document><Document><DocumentType>EMID</DocumentType><DocumentRefNumber>784198565314652</DocumentRefNumber></Document><ContactDetails><PhoneFax><PhoneType>CELLPH1</PhoneType><PhoneValue>97100971</PhoneValue></PhoneFax><PhoneFax><PhoneType>HOMEPH1</PhoneType><PhoneValue>97100971</PhoneValue></PhoneFax><PhoneFax><PhoneType>OFFCPH1</PhoneType><PhoneValue>97100971</PhoneValue></PhoneFax></ContactDetails><StatusInfo><StatusType>Black List</StatusType><StatusFlag>N</StatusFlag><StatusOverAllFlag>N</StatusOverAllFlag><StatusList></StatusList></StatusInfo><StatusInfo><StatusType>Negative List</StatusType><StatusFlag>N</StatusFlag><StatusOverAllFlag>N</StatusOverAllFlag><StatusList></StatusList></StatusInfo></Customer></CustomerBlackListResponse></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
 	}*/
		else if(("ACCOUNT_MAINTENANCE_REQ").equalsIgnoreCase(callName)){
			response = "<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>ACCOUNT_MAINTENANCE_REQ</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153286850693168</MessageId><Extra1>REP||SHELL.JOHN</Extra1><Extra2>2018-07-29T04:48:27.336+04:00</Extra2></EE_EAI_HEADER><modifyAccountDetailsRes><OperDesc>Account Name Modification</OperDesc></modifyAccountDetailsRes></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(("NEW_DEBITCARD_REQ").equalsIgnoreCase(callName)){
			response = "<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>NEW_DEBITCARD_REQ</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153286852165021</MessageId><Extra1>REP||SHELL.JOHN</Extra1><Extra2>2018-07-29T04:48:44.451+04:00</Extra2></EE_EAI_HEADER><DebitCardRequest><CustomerId>2676881</CustomerId><RequestId>Personal-705841</RequestId><SuccessFlag>S</SuccessFlag><ErrorCode>SUCCESS</ErrorCode><ErrorDesc>SUCCESS</ErrorDesc></DebitCardRequest></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";;
		}
		else if(("ACCOUNT_SUMMARY").equalsIgnoreCase(callName)){
			response = "<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>ACCOUNT_SUMMARY</MsgFormat><MsgVersion>0000</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153250382510523</MessageId><Extra1>REP||LAXMANRET.LAXMANRET</Extra1><Extra2>2018-07-25T11:30:25.620+04:00</Extra2></EE_EAI_HEADER><FetchIBSAccountListRes><CIFID>2221180</CIFID><BankId>RAK</BankId><StatusAsOf>2018-07-25T00:00:00.000+04:00</StatusAsOf><IsIslamic>I</IsIslamic><IBSAccountDetail><BranchId>902</BranchId><Acid>20331583</Acid><AcctType>LAA</AcctType><AccountCategory>ENP1</AccountCategory><CrnCode>AED</CrnCode><NicName>2221180</NicName><AccountName>KPNZ#UIPNBT#KPIO</AccountName><AcctBal>0</AcctBal><LoanAmt>71250</LoanAmt><JntAcctIndicator>N</JntAcctIndicator><AcctStatus>002</AcctStatus><AcctAccess>N</AcctAccess><AcctOpnDt>2015-09-20</AcctOpnDt><ProductId>AMAL PERSONAL FINANCE </ProductId><ModeOfOperation>SINGLY</ModeOfOperation></IBSAccountDetail><IBSAccountDetail><BranchId>902</BranchId><Acid>20359906</Acid><AcctType>LAA</AcctType><AccountCategory>ENP2</AccountCategory><CrnCode>AED</CrnCode><NicName>2221180</NicName><AccountName>KPNZ#UIPNBT#KPIO</AccountName><AcctBal>66633.9</AcctBal><LoanAmt>113000</LoanAmt><JntAcctIndicator>N</JntAcctIndicator><AcctStatus>001</AcctStatus><AcctAccess>N</AcctAccess><AcctOpnDt>2016-06-02</AcctOpnDt><ProductId>AMAL PERSONAL FINANCE </ProductId><ModeOfOperation>SINGLY</ModeOfOperation></IBSAccountDetail><IBSAccountDetail><BranchId>902</BranchId><Acid>40005178</Acid><AcctType>LAA</AcctType><AccountCategory>CNP1</AccountCategory><CrnCode>AED</CrnCode><NicName>2221180</NicName><AccountName>KPNZ#UIPNBT#KPIO</AccountName><AcctBal>200787.73</AcctBal><LoanAmt>200000</LoanAmt><JntAcctIndicator>N</JntAcctIndicator><AcctStatus>001</AcctStatus><AcctAccess>N</AcctAccess><AcctOpnDt>2018-07-02</AcctOpnDt><ProductId>AMAL PERSONAL FINANCE </ProductId><ModeOfOperation>SINGLY</ModeOfOperation></IBSAccountDetail></FetchIBSAccountListRes></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(("DEDUP_SUMMARY").equalsIgnoreCase(callName)){
			if(formObject.getNGValue("cmplx_Customer_CIFNO").equalsIgnoreCase("2676652"))
			{
				response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>DEDUP_SUMMARY</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>TABUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS154228758963687</MessageId><Extra1>REP||BPM.123</Extra1><Extra2>2018-11-15T05:13:12.147+04:00</Extra2></EE_EAI_HEADER><CustomerDuplicationListResponse><BankId>RAK</BankId><Customer><CIFID>2676652</CIFID><RetailCorpFlag>R</RetailCorpFlag><CustomerType>EB</CustomerType><EntityType>ALL</EntityType><StepsMatched>CIF ID</StepsMatched><PersonDetails><FirstName>GHAZANFAR</FirstName><MiddleName>UAT</MiddleName><LastName>RAJPUT</LastName><ShortName>F  L</ShortName><FullName>GHAZANFAR UAT RAJPUT</FullName><MaritalStatus>S</MaritalStatus><Nationality>CL</Nationality><DateOfBirth>1985-09-23</DateOfBirth></PersonDetails><Document><DocumentType>PPT</DocumentType><DocumentRefNumber>CL123456</DocumentRefNumber></Document><Document><DocumentType>VISA</DocumentType><DocumentRefNumber>20120142354787</DocumentRefNumber></Document><Document><DocumentType>EMID</DocumentType><DocumentRefNumber>784198520002322</DocumentRefNumber></Document><ContactDetails><EmailAddress><MailIdType>ELML1</MailIdType><MailIdValue>TEST11@RAKBANKTST.AE</MailIdValue></EmailAddress><EmailAddress><MailIdType>WORKEML</MailIdType><MailIdValue>1234567891234567@RAKBANK.AE</MailIdValue></EmailAddress><PhoneFax><PhoneType>CELLPH1</PhoneType><PhoneValue>97100971</PhoneValue></PhoneFax><PhoneFax><PhoneType>HOMEPH1</PhoneType><PhoneValue>97100971</PhoneValue></PhoneFax><PhoneFax><PhoneType>OFFCPH1</PhoneType><PhoneValue>97100971</PhoneValue></PhoneFax></ContactDetails><StatusInfo><StatusType>Blacklisted</StatusType><StatusFlag>N</StatusFlag></StatusInfo><StatusInfo><StatusType>Negativelisted</StatusType><StatusFlag>N</StatusFlag></StatusInfo><StatusInfo><StatusType>Suspended</StatusType><StatusFlag>N</StatusFlag></StatusInfo></Customer></CustomerDuplicationListResponse></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
			}
			else if(formObject.getNGValue("cmplx_Customer_CIFNO").equalsIgnoreCase("0109661")){
				response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>DEDUP_SUMMARY</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>TABUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS154228750821957</MessageId><Extra1>REP||BPM.123</Extra1><Extra2>2018-11-15T05:11:50.254+04:00</Extra2></EE_EAI_HEADER><CustomerDuplicationListResponse><BankId>RAK</BankId><Customer><CIFID>0109661</CIFID><RetailCorpFlag>R</RetailCorpFlag><CustomerType>EB</CustomerType><EntityType>ALL</EntityType><StepsMatched>CIF ID</StepsMatched><PersonDetails><FirstName>GHAZANFAR</FirstName><MiddleName>UAT</MiddleName><LastName>RAJPUT</LastName><ShortName>F  L</ShortName><FullName>GHAZANFAR UAT RAJPUT</FullName><MaritalStatus>S</MaritalStatus><Nationality>IN</Nationality><DateOfBirth>1985-09-23</DateOfBirth></PersonDetails><Document><DocumentType>PPT</DocumentType><DocumentRefNumber>AE0109661</DocumentRefNumber></Document><Document><DocumentType>VISA</DocumentType><DocumentRefNumber>20120142354787</DocumentRefNumber></Document><Document><DocumentType>EMID</DocumentType><DocumentRefNumber>784198520002322</DocumentRefNumber></Document><ContactDetails><EmailAddress><MailIdType>ELML1</MailIdType><MailIdValue>TEST11@RAKBANKTST.AE</MailIdValue></EmailAddress><EmailAddress><MailIdType>HOMEEML</MailIdType><MailIdValue>123456789.1234@RAKBANK.AE</MailIdValue></EmailAddress><EmailAddress><MailIdType>WORKEML</MailIdType><MailIdValue>1234567891234567@RAKBANK.AE</MailIdValue></EmailAddress><PhoneFax><PhoneType>CELLPH1</PhoneType><PhoneValue>97100971</PhoneValue></PhoneFax><PhoneFax><PhoneType>FAXO1</PhoneType><PhoneValue>00971420109661</PhoneValue></PhoneFax><PhoneFax><PhoneType>HOMEPH1</PhoneType><PhoneValue>97100971</PhoneValue></PhoneFax><PhoneFax><PhoneType>OFFCPH1</PhoneType><PhoneValue>97100971</PhoneValue></PhoneFax><PhoneFax><PhoneType>OVHOMEPH</PhoneType><PhoneValue>0091990109661</PhoneValue></PhoneFax></ContactDetails><StatusInfo><StatusType>Blacklisted</StatusType><StatusFlag>N</StatusFlag></StatusInfo><StatusInfo><StatusType>Negativelisted</StatusType><StatusFlag>N</StatusFlag></StatusInfo><StatusInfo><StatusType>Suspended</StatusType><StatusFlag>N</StatusFlag></StatusInfo></Customer></CustomerDuplicationListResponse></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
			}
			else if(formObject.getNGValue("cmplx_Customer_CIFNO").equalsIgnoreCase("0141133")){
				response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>DEDUP_SUMMARY</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>TABUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS154227420344446</MessageId><Extra1>REP||BPM.123</Extra1><Extra2>2018-11-15T01:30:05.941+04:00</Extra2></EE_EAI_HEADER><CustomerDuplicationListResponse><BankId>RAK</BankId><Customer><CIFID>0141133</CIFID><RetailCorpFlag>R</RetailCorpFlag><CustomerType>EB</CustomerType><EntityType>ALL</EntityType><StepsMatched>CIF ID</StepsMatched><PersonDetails><FirstName>GHAZANFAR</FirstName><MiddleName>UAT</MiddleName><LastName>RAJPUT</LastName><ShortName>GHAZANFAR</ShortName><FullName>GHAZANFAR UAT RAJPUT</FullName><MaritalStatus>M</MaritalStatus><Nationality>IN</Nationality><DateOfBirth>1985-09-23</DateOfBirth></PersonDetails><Document><DocumentType>PPT</DocumentType><DocumentRefNumber>AE0999999</DocumentRefNumber></Document><Document><DocumentType>VISA</DocumentType><DocumentRefNumber>20120087304986</DocumentRefNumber></Document><Document><DocumentType>EMID</DocumentType><DocumentRefNumber>784198520002322</DocumentRefNumber></Document><ContactDetails><EmailAddress><MailIdType>ELML1</MailIdType><MailIdValue>12345678@RAKBANK.AE</MailIdValue></EmailAddress><EmailAddress><MailIdType>ELML2</MailIdType><MailIdValue>1.234567@RAKBANK.AE</MailIdValue></EmailAddress><EmailAddress><MailIdType>HOMEEML</MailIdType><MailIdValue>1.234567@RAKBANK.AE</MailIdValue></EmailAddress><EmailAddress><MailIdType>WORKEML</MailIdType><MailIdValue>12345678@RAKBANK.AE</MailIdValue></EmailAddress><PhoneFax><PhoneType>CELLPH1</PhoneType><PhoneValue>00971500141133</PhoneValue></PhoneFax><PhoneFax><PhoneType>HOMEPH1</PhoneType><PhoneValue>00971690141133</PhoneValue></PhoneFax><PhoneFax><PhoneType>MIGR</PhoneType><PhoneValue></PhoneValue></PhoneFax><PhoneFax><PhoneType>OFFCPH1</PhoneType><PhoneValue>00971440141133</PhoneValue></PhoneFax><PhoneFax><PhoneType>OVHOMEPH</PhoneType><PhoneValue>0091980141133</PhoneValue></PhoneFax></ContactDetails><StatusInfo><StatusType>Blacklisted</StatusType><StatusFlag>N</StatusFlag></StatusInfo><StatusInfo><StatusType>Negativelisted</StatusType><StatusFlag>N</StatusFlag></StatusInfo><StatusInfo><StatusType>Suspended</StatusType><StatusFlag>N</StatusFlag></StatusInfo></Customer></CustomerDuplicationListResponse></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
			}
			else
			{			
				response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>DEDUP_SUMMARY</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>TABUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS154228756743199</MessageId><Extra1>REP||BPM.123</Extra1><Extra2>2018-11-15T05:12:50.918+04:00</Extra2></EE_EAI_HEADER><CustomerDuplicationListResponse><BankId>RAK</BankId><Customer><CIFID>2676917</CIFID><RetailCorpFlag>R</RetailCorpFlag><CustomerType>EB</CustomerType><EntityType>ALL</EntityType><StepsMatched>CIF ID</StepsMatched><PersonDetails><FirstName>GIRGIO</FirstName><MiddleName>UAT</MiddleName><LastName>RAJENDIRAN</LastName><ShortName>F  L</ShortName><FullName>GIRGIO UAT RAJENDIRAN</FullName><MaritalStatus>S</MaritalStatus><Nationality>AU</Nationality><DateOfBirth>1992-11-24</DateOfBirth></PersonDetails><Document><DocumentType>PPT</DocumentType><DocumentRefNumber>PK0002321</DocumentRefNumber></Document><Document><DocumentType>VISA</DocumentType><DocumentRefNumber>20120142354787</DocumentRefNumber></Document><Document><DocumentType>EMID</DocumentType><DocumentRefNumber>784199200002321</DocumentRefNumber></Document><ContactDetails><EmailAddress><MailIdType>ELML1</MailIdType><MailIdValue>TEST11@RAKBANKTST.AE</MailIdValue></EmailAddress><EmailAddress><MailIdType>WORKEML</MailIdType><MailIdValue>1234567891234567@RAKBANK.AE</MailIdValue></EmailAddress><PhoneFax><PhoneType>CELLPH1</PhoneType><PhoneValue>97100971</PhoneValue></PhoneFax><PhoneFax><PhoneType>HOMEPH1</PhoneType><PhoneValue>97100971</PhoneValue></PhoneFax><PhoneFax><PhoneType>OFFCPH1</PhoneType><PhoneValue>97100971</PhoneValue></PhoneFax></ContactDetails><StatusInfo><StatusType>Blacklisted</StatusType><StatusFlag>N</StatusFlag></StatusInfo><StatusInfo><StatusType>Negativelisted</StatusType><StatusFlag>N</StatusFlag></StatusInfo><StatusInfo><StatusType>Suspended</StatusType><StatusFlag>N</StatusFlag></StatusInfo></Customer></CustomerDuplicationListResponse></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
			}



		}
		else if(("NEW_LOAN_REQ").equalsIgnoreCase(callName)){
			response = "<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>NEW_LOAN_REQ</MsgFormat><MsgVersion>0</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS15328686886040</MessageId><Extra1>REP||SHELL.JOHN</Extra1><Extra2>2018-07-29T04:51:50.677+04:00</Extra2></EE_EAI_HEADER><ContractCreationRes><contractID>902CNP1182100003</contractID><StatusCode>H</StatusCode><Description>Contract Created Successfully</Description></ContractCreationRes></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";

		}
		else if(("CUSTOMER_UPDATE_REQ").equalsIgnoreCase(callName)){
			response = "<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>CUSTOMER_UPDATE_REQ</MsgFormat><MsgVersion>001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS15328684850094</MessageId><Extra1>REP||SHELL.dfgJOHN</Extra1><Extra2>2018-07-29T04:48:08.597+04:00</Extra2></EE_EAI_HEADER><CustomerDetailsUpdateRes><BankId>RAK</BankId><CIFId>2676881</CIFId><Status>S</Status><Message>SUCCESS</Message></CustomerDetailsUpdateRes></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(("CARD_SUMMARY").equalsIgnoreCase(callName)){
			response = "<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>CARD_SUMMARY</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>CASUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153241540837975</MessageId><Extra1>REP||BPM.123</Extra1><Extra2>2018-07-24T10:56:50.956+04:00</Extra2></EE_EAI_HEADER><CardSummaryResponse><BankId>RAK</BankId><CIFID>2221180</CIFID><CardCRNNumber>087749600</CardCRNNumber><IsCombinedCreditLimit>N</IsCombinedCreditLimit><CardDetails><CardNumber>5227856098400006</CardNumber><PrimaryCardHolderName>EMB_NAME_069534100</PrimaryCardHolderName><CardType>primary</CardType><CardProductType>AMAL-CLASSIC-I</CardProductType><CardStatus>NA</CardStatus><CardAsIsStatus>NORM</CardAsIsStatus><ExpiryDate>2020-01-31</ExpiryDate><LastStatementDate>2018-05-16</LastStatementDate><TotalAmtDue>-6278.94</TotalAmtDue><MinAmtDue>-303.09</MinAmtDue><TotalPayments>0</TotalPayments><LastPayment>300</LastPayment><CurrentMinAmtDue>-303.09</CurrentMinAmtDue><PymtDueDate>2018-06-10</PymtDueDate><TotalCreditLimit>6200</TotalCreditLimit><OverDueAmt>0</OverDueAmt><OutstandingBalance>-6278.94</OutstandingBalance><NextStatementDate>2018-06-16</NextStatementDate></CardDetails><CardDetails><CardNumber>1531007389202004</CardNumber><PrimaryCardHolderName>EMB_NAME_074587800</PrimaryCardHolderName><CardType>primary</CardType><CardProductType>LOC STANDARD</CardProductType><CardStatus>NA</CardStatus><CardAsIsStatus>CLSB</CardAsIsStatus><ExpiryDate>2020-01-31</ExpiryDate><LastStatementDate>1900-01-01</LastStatementDate><TotalAmtDue>0</TotalAmtDue><MinAmtDue>0</MinAmtDue><TotalPayments>0</TotalPayments><LastPayment>0</LastPayment><CurrentMinAmtDue>0</CurrentMinAmtDue><PymtDueDate>1900-01-01</PymtDueDate><TotalCreditLimit>18000</TotalCreditLimit><OverDueAmt>0</OverDueAmt><OutstandingBalance>0</OutstandingBalance><NextStatementDate>1900-02-01</NextStatementDate></CardDetails><CardDetails><CardNumber>1531004636412007</CardNumber><PrimaryCardHolderName>EMB_NAME_087749600</PrimaryCardHolderName><CardType>primary</CardType><CardProductType>LOC STANDARD</CardProductType><CardStatus>NA</CardStatus><CardAsIsStatus>NORM</CardAsIsStatus><ExpiryDate>2020-01-31</ExpiryDate><LastStatementDate>2018-05-16</LastStatementDate><TotalAmtDue>-582.05</TotalAmtDue><MinAmtDue>-582.05</MinAmtDue><TotalPayments>0</TotalPayments><LastPayment>600</LastPayment><CurrentMinAmtDue>-582.05</CurrentMinAmtDue><PymtDueDate>2018-06-10</PymtDueDate><TotalCreditLimit>18400</TotalCreditLimit><OverDueAmt>0</OverDueAmt><OutstandingBalance>-14972.22</OutstandingBalance><NextStatementDate>2018-06-16</NextStatementDate></CardDetails></CardSummaryResponse></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(("SIGNATURE_DETAILS").equalsIgnoreCase(callName)){
			response = "<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>SIGNATURE_DETAILS</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>SVS-101</ReturnCode><ReturnDesc>FIN : SVSInquiry: signautreMain: NO MATCHED RECORD FOUND FOR THE GIVEN ACCOUNT INFORMATIONSVS-101: N</ReturnDesc><MessageId>CAS152517834066848</MessageId><Extra1>REP||CAS.123</Extra1><Extra2>2018-05-01T04:39:03.426+04:00</Extra2></EE_EAI_HEADER></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(callName.indexOf("InternalExposure")>-1)
		{
			response = "<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>CUSTOMER_EXPOSURE</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153379503251654</MessageId><Extra1>REP||SHELL.JOHN</Extra1><Extra2>2018-08-09T10:10:35.712+04:00</Extra2></EE_EAI_HEADER><CustomerExposureResponse><RequestType>InternalExposure</RequestType><IsDirect>Y</IsDirect><CustInfo><CustId><CustIdType>CIF Id</CustIdType><CustIdValue>0536177</CustIdValue></CustId><FullNm></FullNm><BirthDt>1988-01-01</BirthDt><Nationality>INDIAN</Nationality><CustSegment>PERSONAL BANKING</CustSegment><CustSubSegment>PB - NORMAL</CustSubSegment><RMName>PERSONAL BANKER</RMName><CreditGrade>R6 - BUSINESS - SCORE 20-34</CreditGrade><ECRN>010790200</ECRN><BorrowingCustomer>N</BorrowingCustomer></CustInfo><ProductExposureDetails><CardDetails><CardEmbossNum>010790200</CardEmbossNum><CardStatus>STWO</CardStatus><CardType>VCLASSIC-EXPATR</CardType><CustRoleType>Primary</CustRoleType><KeyDt><KeyDtType>ApplicationCreationDate</KeyDtType><KeyDtValue>2012-06-09</KeyDtValue></KeyDt><KeyDt><KeyDtType>ExpiryDate</KeyDtType><KeyDtValue>2025-01-31</KeyDtValue></KeyDt><CurCode></CurCode><AmountDtls><AmtType>CreditLimit</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>OutstandingAmt</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>OverdueAmt</AmtType><Amt>0</Amt></AmountDtls></CardDetails><CardDetails><CardEmbossNum>010790300</CardEmbossNum><CardStatus>STWO</CardStatus><CardType>MSTANDARD-EXPATRIATE</CardType><CustRoleType>Primary</CustRoleType><KeyDt><KeyDtType>ApplicationCreationDate</KeyDtType><KeyDtValue>2012-06-09</KeyDtValue></KeyDt><KeyDt><KeyDtType>ExpiryDate</KeyDtType><KeyDtValue>2025-01-31</KeyDtValue></KeyDt><CurCode></CurCode><AmountDtls><AmtType>CreditLimit</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>OutstandingAmt</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>OverdueAmt</AmtType><Amt>0</Amt></AmountDtls></CardDetails><CardDetails><CardEmbossNum>075161000</CardEmbossNum><CardStatus>AUWO</CardStatus><CardType>SME-TITANIUM</CardType><CustRoleType>Primary</CustRoleType><KeyDt><KeyDtType>ApplicationCreationDate</KeyDtType><KeyDtValue>2015-05-29</KeyDtValue></KeyDt><KeyDt><KeyDtType>ExpiryDate</KeyDtType><KeyDtValue>2025-01-31</KeyDtValue></KeyDt><CurCode></CurCode><AmountDtls><AmtType>CreditLimit</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>OutstandingAmt</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>OverdueAmt</AmtType><Amt>-40184.49</Amt></AmountDtls></CardDetails><AcctDetails><AcctId>8032044263901</AcctId><IBANNumber>AE480400008032044263901</IBANNumber><AcctStat>ACTIVE</AcctStat><AcctCur>AED</AcctCur><AcctNm>FIRST NAME FOR 0536177  LAST NAME FOR 0536177</AcctNm><AcctType>AMAL BUSINESS FINANCE ACCOUNT</AcctType><AcctSegment>PBD</AcctSegment><AcctSubSegment>PSL</AcctSubSegment><CustRoleType>Auth Sign.</CustRoleType><KeyDt><KeyDtType>AccountOpenDate</KeyDtType><KeyDtValue>2013-02-25</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitSactionDate</KeyDtType><KeyDtValue>2013-02-25</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitExpiryDate</KeyDtType><KeyDtValue>2013-02-26</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitStartDate</KeyDtType><KeyDtValue>2013-02-25</KeyDtValue></KeyDt><AmountDtls><AmtType>AvailableBalance</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>ClearBalanceAmount</AmtType><Amt>-1824.32</Amt></AmountDtls><AmountDtls><AmtType>LedgerBalance</AmtType><Amt>-1824.32</Amt></AmountDtls><AmountDtls><AmtType>OverdueAmount</AmtType><Amt>-1824.32</Amt></AmountDtls><AmountDtls><AmtType>EffectiveAvailableBalance</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>CumulativeDebitAmount</AmtType><Amt>60443311.04</Amt></AmountDtls><AmountDtls><AmtType>ChrgOffBal</AmtType><Amt>1824.32</Amt></AmountDtls><AmountDtls><AmtType>SanctionLimit</AmtType><Amt>0</Amt></AmountDtls><WriteoffStat>N</WriteoffStat><WriteoffStatDt>2017-08-20</WriteoffStatDt><WorstDelay24Months>P6</WorstDelay24Months><WorstStatus24Months>DEFAULT</WorstStatus24Months><MonthsOnBook>66.00</MonthsOnBook><LastRepmtDt>AUG</LastRepmtDt><IsCurrent>N</IsCurrent><ChargeOffFlag>Y</ChargeOffFlag><DelinquencyInfo><BucketType>DaysPastDue</BucketType><BucketValue>29</BucketValue></DelinquencyInfo><LinkedCIFs><CIFId>2044263</CIFId><RelationType>Main Account Holder</RelationType></LinkedCIFs></AcctDetails><AcctDetails><AcctId>8032044263902</AcctId><IBANNumber>AE210400008032044263902</IBANNumber><AcctStat>ACTIVE</AcctStat><AcctCur>USD</AcctCur><AcctNm>FIRST NAME FOR 0536177  LAST NAME FOR 0536177</AcctNm><AcctType>AMAL BUSINESS FINANCE ACCOUNT</AcctType><AcctSegment>PBD</AcctSegment><AcctSubSegment>PSL</AcctSubSegment><CustRoleType>Auth Sign.</CustRoleType><KeyDt><KeyDtType>AccountOpenDate</KeyDtType><KeyDtValue>2013-02-25</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitSactionDate</KeyDtType><KeyDtValue>2013-02-25</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitExpiryDate</KeyDtType><KeyDtValue>2013-02-26</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitStartDate</KeyDtType><KeyDtValue>2013-02-25</KeyDtValue></KeyDt><AmountDtls><AmtType>AvailableBalance</AmtType><Amt>0.00</Amt></AmountDtls><AmountDtls><AmtType>ClearBalanceAmount</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>LedgerBalance</AmtType><Amt>0.00</Amt></AmountDtls><AmountDtls><AmtType>EffectiveAvailableBalance</AmtType><Amt>0.00</Amt></AmountDtls><AmountDtls><AmtType>CumulativeDebitAmount</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>SanctionLimit</AmtType><Amt>0</Amt></AmountDtls><WriteoffStat>Y</WriteoffStat><WorstDelay24Months>P2</WorstDelay24Months><MonthsOnBook>66.00</MonthsOnBook><LastRepmtDt>AUG</LastRepmtDt><IsCurrent>Y</IsCurrent><ChargeOffFlag>N</ChargeOffFlag><DelinquencyInfo><BucketType>DaysPastDue</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><LinkedCIFs><CIFId>2044263</CIFId><RelationType>Main Account Holder</RelationType></LinkedCIFs></AcctDetails><AcctDetails><AcctId>0030536177001</AcctId><IBANNumber>AE790400000030536177001</IBANNumber><AcctStat>ACTIVE</AcctStat><AcctCur>AED</AcctCur><AcctNm>FIRST NAME FOR 0536177  LAST NAME FOR 0536177</AcctNm><AcctType>CURRENT ACCOUNT</AcctType><AcctSegment>PBD</AcctSegment><AcctSubSegment>PBN</AcctSubSegment><CustRoleType>Main</CustRoleType><KeyDt><KeyDtType>AccountOpenDate</KeyDtType><KeyDtValue>2013-03-06</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitSactionDate</KeyDtType><KeyDtValue>2013-03-06</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitExpiryDate</KeyDtType><KeyDtValue>2013-03-07</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitStartDate</KeyDtType><KeyDtValue>2013-03-06</KeyDtValue></KeyDt><AmountDtls><AmtType>AvailableBalance</AmtType><Amt>0.00</Amt></AmountDtls><AmountDtls><AmtType>ClearBalanceAmount</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>LedgerBalance</AmtType><Amt>0.00</Amt></AmountDtls><AmountDtls><AmtType>EffectiveAvailableBalance</AmtType><Amt>0.00</Amt></AmountDtls><AmountDtls><AmtType>CumulativeDebitAmount</AmtType><Amt>10000</Amt></AmountDtls><AmountDtls><AmtType>SanctionLimit</AmtType><Amt>0</Amt></AmountDtls><WriteoffStat>Y</WriteoffStat><WorstDelay24Months>P2</WorstDelay24Months><MonthsOnBook>66.00</MonthsOnBook><LastRepmtDt>AUG</LastRepmtDt><IsCurrent>Y</IsCurrent><ChargeOffFlag>N</ChargeOffFlag><DelinquencyInfo><BucketType>DaysPastDue</BucketType><BucketValue>0</BucketValue></DelinquencyInfo></AcctDetails><AcctDetails><AcctId>0030536177998</AcctId><IBANNumber>AE290400000030536177998</IBANNumber><AcctStat>ACTIVE</AcctStat><AcctCur>AED</AcctCur><AcctNm>FIRST NAME FOR 0536177  LAST NAME FOR 0536177</AcctNm><AcctType>CHARGE RECEIVABLE</AcctType><AcctSegment>PBD</AcctSegment><AcctSubSegment>PBN</AcctSubSegment><CustRoleType>Main</CustRoleType><KeyDt><KeyDtType>AccountOpenDate</KeyDtType><KeyDtValue>2017-02-01</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitSactionDate</KeyDtType><KeyDtValue>2017-02-01</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitExpiryDate</KeyDtType><KeyDtValue>2099-01-01</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitStartDate</KeyDtType><KeyDtValue>2017-02-01</KeyDtValue></KeyDt><AmountDtls><AmtType>AvailableBalance</AmtType><Amt>99999999999599.00</Amt></AmountDtls><AmountDtls><AmtType>ClearBalanceAmount</AmtType><Amt>-400</Amt></AmountDtls><AmountDtls><AmtType>LedgerBalance</AmtType><Amt>-400.00</Amt></AmountDtls><AmountDtls><AmtType>EffectiveAvailableBalance</AmtType><Amt>99999999999599.00</Amt></AmountDtls><AmountDtls><AmtType>CumulativeDebitAmount</AmtType><Amt>400</Amt></AmountDtls><AmountDtls><AmtType>SanctionLimit</AmtType><Amt>99999999999999</Amt></AmountDtls><WriteoffStat>Y</WriteoffStat><WorstDelay24Months>P2</WorstDelay24Months><MonthsOnBook>19.00</MonthsOnBook><LastRepmtDt>AUG</LastRepmtDt><IsCurrent>Y</IsCurrent><ChargeOffFlag>N</ChargeOffFlag><DelinquencyInfo><BucketType>DaysPastDue</BucketType><BucketValue>0</BucketValue></DelinquencyInfo></AcctDetails><AcctDetails><AcctId>0003536177099</AcctId><IBANNumber></IBANNumber><AcctStat>CLOSED</AcctStat><AcctCur>AED</AcctCur><AcctNm>FIRST NAME FOR 0536177  LAST NAME FOR 0536177</AcctNm><AcctType>CUSTOMER SECURITY CHEQUE ACCOUNT</AcctType><AcctSegment>PBD</AcctSegment><AcctSubSegment>PBN</AcctSubSegment><CustRoleType>Main</CustRoleType><KeyDt><KeyDtType>AccountOpenDate</KeyDtType><KeyDtValue>2004-11-28</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitSactionDate</KeyDtType><KeyDtValue>2004-11-28</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitExpiryDate</KeyDtType><KeyDtValue>2012-06-08</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitStartDate</KeyDtType><KeyDtValue>2004-11-28</KeyDtValue></KeyDt><AmountDtls><AmtType>AvailableBalance</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>ClearBalanceAmount</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>LedgerBalance</AmtType><Amt>0.00</Amt></AmountDtls><AmountDtls><AmtType>EffectiveAvailableBalance</AmtType><Amt>0.00</Amt></AmountDtls><AmountDtls><AmtType>CumulativeDebitAmount</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>SanctionLimit</AmtType><Amt>0</Amt></AmountDtls><WriteoffStat>Y</WriteoffStat><WorstDelay24Months>P2</WorstDelay24Months><MonthsOnBook>104.00</MonthsOnBook><LastRepmtDt>JUL</LastRepmtDt><IsCurrent>Y</IsCurrent><ChargeOffFlag>N</ChargeOffFlag><DelinquencyInfo><BucketType>DaysPastDue</BucketType><BucketValue>0</BucketValue></DelinquencyInfo></AcctDetails></ProductExposureDetails></CustomerExposureResponse><CustomerExposureResponse><RequestType>InternalExposure</RequestType><IsDirect>N</IsDirect><CustInfo><CustId><CustIdType>CIF Id</CustIdType><CustIdValue>2044263</CustIdValue></CustId><FullNm></FullNm><BirthDt>2003-11-12</BirthDt><Nationality>UNITED ARAB EMIRATES</Nationality><CustSegment>PERSONAL BANKING</CustSegment><CreditGrade>W1-WRITE OFF ACCOUNT</CreditGrade></CustInfo><ProductExposureDetails><LoanDetails><AgreementId>0032044263001</AgreementId><LoanStat>ACTIVE</LoanStat><LoanType>SHORT TERM LOAN AGAINST INVOICE</LoanType><LoanDesc></LoanDesc><CustRoleType></CustRoleType><KeyDt><KeyDtType>LimitSactionDate</KeyDtType><KeyDtValue>2011-02-13</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitExpiryDate</KeyDtType><KeyDtValue>2099-02-13</KeyDtValue></KeyDt><CurCode>AED</CurCode><MonthsOnBook>37.00</MonthsOnBook><DelinquencyInfo><BucketType>DaysPastDue</BucketType><BucketValue>0</BucketValue></DelinquencyInfo></LoanDetails><LoanDetails><AgreementId>0032044263002</AgreementId><LoanStat>ACTIVE</LoanStat><LoanType>SHORT TERM LOAN AGAINST INVOICE PAST DUE</LoanType><LoanDesc></LoanDesc><CustRoleType></CustRoleType><KeyDt><KeyDtType>LimitSactionDate</KeyDtType><KeyDtValue>2011-02-13</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitExpiryDate</KeyDtType><KeyDtValue>2099-02-13</KeyDtValue></KeyDt><CurCode>AED</CurCode><AmountDtls><AmtType>OverdueAmt</AmtType><Amt>-110395.72</Amt></AmountDtls><WriteoffStat>ChargeOff</WriteoffStat><WriteoffStatDt>2016-07-26</WriteoffStatDt><MonthsOnBook>33.00</MonthsOnBook><DelinquencyInfo><BucketType>DaysPastDue</BucketType><BucketValue>0</BucketValue></DelinquencyInfo></LoanDetails><AcctDetails><AcctId>8032044263998</AcctId><IBANNumber>AE480400008032044263998</IBANNumber><AcctStat>CLOSED</AcctStat><AcctCur>AED</AcctCur><AcctNm>FIRST NAME FOR 0536177  LAST NAME FOR 0536177</AcctNm><AcctType>CHARGE RECEIVABLE</AcctType><AcctSegment>PBD</AcctSegment><AcctSubSegment>PSL</AcctSubSegment><CustRoleType>Main</CustRoleType><KeyDt><KeyDtType>AccountOpenDate</KeyDtType><KeyDtValue>2015-12-17</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitSactionDate</KeyDtType><KeyDtValue>2015-12-17</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitExpiryDate</KeyDtType><KeyDtValue>2099-01-01</KeyDtValue></KeyDt><AmountDtls><AmtType>AvailableBalance</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>ClearBalanceAmount</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>LedgerBalance</AmtType><Amt>0.00</Amt></AmountDtls><AmountDtls><AmtType>EffectiveAvailableBalance</AmtType><Amt>0</Amt></AmountDtls><WriteoffStat>Y</WriteoffStat><WorstDelay24Months>P2</WorstDelay24Months><MonthsOnBook>21.00</MonthsOnBook><LastRepmtDt>AUG</LastRepmtDt><IsCurrent>Y</IsCurrent><ChargeOffFlag>N</ChargeOffFlag><DelinquencyInfo><BucketType>DaysPastDue</BucketType><BucketValue>0</BucketValue></DelinquencyInfo></AcctDetails><AcctDetails><AcctId>8032044263999</AcctId><IBANNumber>AE210400008032044263999</IBANNumber><AcctStat>ACTIVE</AcctStat><AcctCur>AED</AcctCur><AcctNm>FIRST NAME FOR 0536177  LAST NAME FOR 0536177</AcctNm><AcctType>CHARGE RECEIVABLE</AcctType><AcctSegment>PBD</AcctSegment><AcctSubSegment>PSL</AcctSubSegment><CustRoleType>Main</CustRoleType><KeyDt><KeyDtType>AccountOpenDate</KeyDtType><KeyDtValue>2017-09-03</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitSactionDate</KeyDtType><KeyDtValue>2017-09-03</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitExpiryDate</KeyDtType><KeyDtValue>2099-01-01</KeyDtValue></KeyDt><AmountDtls><AmtType>AvailableBalance</AmtType><Amt>99999999997226.50</Amt></AmountDtls><AmountDtls><AmtType>ClearBalanceAmount</AmtType><Amt>-2772.5</Amt></AmountDtls><AmountDtls><AmtType>LedgerBalance</AmtType><Amt>-2772.50</Amt></AmountDtls><AmountDtls><AmtType>EffectiveAvailableBalance</AmtType><Amt>99999999997226.50</Amt></AmountDtls><WriteoffStat>Y</WriteoffStat><WorstDelay24Months>P2</WorstDelay24Months><MonthsOnBook>12.00</MonthsOnBook><LastRepmtDt>AUG</LastRepmtDt><IsCurrent>Y</IsCurrent><ChargeOffFlag>N</ChargeOffFlag><DelinquencyInfo><BucketType>DaysPastDue</BucketType><BucketValue>0</BucketValue></DelinquencyInfo></AcctDetails></ProductExposureDetails></CustomerExposureResponse></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";		                
		}

		else if(callName.indexOf("ExternalExposure")>-1)
		{
			response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>CUSTOMER_EXPOSURE</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153379503253258</MessageId><Extra1>REP||SHELL.JOHN</Extra1><Extra2>2018-08-09T10:10:36.002+04:00</Extra2></EE_EAI_HEADER><CustomerExposureResponse><RequestType>ExternalExposure</RequestType><ReferenceNumber>167054</ReferenceNumber><ReportUrl>'https://ant2a2aapps01.rakbanktst.ae:446/GetPdf.aspx?refno=ItkOnvCPKys%3d'</ReportUrl><IsDirect>N</IsDirect><CustInfo><FullNm>ACCENTURE PVT LTD</FullNm><Activity>0</Activity><TotalOutstanding>0</TotalOutstanding><TotalOverdue>0.00</TotalOverdue><NoOfContracts>0</NoOfContracts><CustInfoListDet><ReferenceNumber>167054</ReferenceNumber><InfoType>NameENInfo</InfoType><CustName>ACCENTURE PVT LTD</CustName><CustNameType>CompanyTradeName</CustNameType><ActualFlag>true</ActualFlag><ProviderNo>B01</ProviderNo><CreatedOn>2018-08-09</CreatedOn><DateOfUpdate>2018-08-09</DateOfUpdate></CustInfoListDet><CustInfoListDet><ReferenceNumber>167054</ReferenceNumber><InfoType>TradeLicenseHistorylst</InfoType><ActualFlag>true</ActualFlag><ProviderNo>B01</ProviderNo><RegistrationPlace>3</RegistrationPlace><LicenseNumber>123658585</LicenseNumber><CreatedOn>2018-08-09</CreatedOn><DateOfUpdate>2018-08-09</DateOfUpdate></CustInfoListDet><PhoneInfo><ReportedDate>2018-08-09</ReportedDate></PhoneInfo><InquiryInfo><ContractCategory>C</ContractCategory></InquiryInfo></CustInfo><ScoreInfo></ScoreInfo><RecordDestributions><RecordDestribution><ContractType>TotalSummary</ContractType><Contract_Role_Type></Contract_Role_Type><TotalNo>1</TotalNo><DataProvidersNo>1</DataProvidersNo><RequestNo>1</RequestNo><DeclinedNo>0</DeclinedNo><RejectedNo>0</RejectedNo><NotTakenUpNo>0</NotTakenUpNo><ActiveNo>0</ActiveNo><ClosedNo>0</ClosedNo></RecordDestribution><RecordDestribution><ContractType>Installments</ContractType><Contract_Role_Type>Holder</Contract_Role_Type><TotalNo>0</TotalNo><DataProvidersNo>0</DataProvidersNo><RequestNo>0</RequestNo><DeclinedNo>0</DeclinedNo><RejectedNo>0</RejectedNo><NotTakenUpNo>0</NotTakenUpNo><ActiveNo>0</ActiveNo><ClosedNo>0</ClosedNo></RecordDestribution><RecordDestribution><ContractType>NotInstallments</ContractType><Contract_Role_Type>Holder</Contract_Role_Type><TotalNo>0</TotalNo><DataProvidersNo>0</DataProvidersNo><RequestNo>0</RequestNo><DeclinedNo>0</DeclinedNo><RejectedNo>0</RejectedNo><NotTakenUpNo>0</NotTakenUpNo><ActiveNo>0</ActiveNo><ClosedNo>0</ClosedNo></RecordDestribution><RecordDestribution><ContractType>CreditCards</ContractType><Contract_Role_Type>Holder</Contract_Role_Type><TotalNo>1</TotalNo><DataProvidersNo>0</DataProvidersNo><RequestNo>1</RequestNo><DeclinedNo>0</DeclinedNo><RejectedNo>0</RejectedNo><NotTakenUpNo>0</NotTakenUpNo><ActiveNo>0</ActiveNo><ClosedNo>0</ClosedNo></RecordDestribution><RecordDestribution><ContractType>Services</ContractType><Contract_Role_Type>Holder</Contract_Role_Type><TotalNo>0</TotalNo><DataProvidersNo>0</DataProvidersNo><RequestNo>0</RequestNo><DeclinedNo>0</DeclinedNo><RejectedNo>0</RejectedNo><NotTakenUpNo>0</NotTakenUpNo><ActiveNo>0</ActiveNo><ClosedNo>0</ClosedNo></RecordDestribution></RecordDestributions><Derived><Total_Exposure>0</Total_Exposure><WorstCurrentPaymentDelay>0</WorstCurrentPaymentDelay><Worst_PaymentDelay_Last24Months>0</Worst_PaymentDelay_Last24Months><Nof_Records>0</Nof_Records><NoOf_Cheque_Return_Last3>0</NoOf_Cheque_Return_Last3><Nof_DDES_Return_Last3Months>0</Nof_DDES_Return_Last3Months><Nof_DDES_Return_Last6Months>0</Nof_DDES_Return_Last6Months><Nof_Cheque_Return_Last6>0</Nof_Cheque_Return_Last6><DPD30_Last6Months>0</DPD30_Last6Months><Nof_Enq_Last90Days>0</Nof_Enq_Last90Days><Nof_Enq_Last60Days>0</Nof_Enq_Last60Days><Nof_Enq_Last30Days>0</Nof_Enq_Last30Days><TotOverDue_GuarteContrct>0</TotOverDue_GuarteContrct></Derived><ProductExposureDetails><ChequeDetails><ChqType></ChqType><Number></Number><Amount></Amount><ReturnDate></ReturnDate><ProviderNo></ProviderNo><ReasonCode></ReasonCode><Severity></Severity></ChequeDetails></ProductExposureDetails></CustomerExposureResponse></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(callName.indexOf("CollectionsSummary")>-1)
		{
			response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>CUSTOMER_EXPOSURE</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153379505177990</MessageId><Extra1>REP||SHELL.JOHN</Extra1><Extra2>2018-08-09T10:10:52.790+04:00</Extra2></EE_EAI_HEADER><CustomerExposureResponse><RequestType>CollectionsSummary</RequestType><CustInfo><CustId><CustIdType>Primary</CustIdType><CustIdValue>0536177</CustIdValue></CustId><FullNm></FullNm><TotalOutstanding>97554.74</TotalOutstanding><TotalOverdue>0</TotalOverdue><NoOfContracts>0</NoOfContracts></CustInfo><Derived><Nof_Records>1</Nof_Records></Derived><ProductExposureDetails><CardDetails><CardEmbossNum>010790300</CardEmbossNum><CardStatus>C</CardStatus><CardType>CREDIT CARDS</CardType><KeyDt><KeyDtType>Card_approve_date</KeyDtType><KeyDtValue>2004-11-30</KeyDtValue></KeyDt><KeyDt><KeyDtType>CardsApplcnRecvdDate</KeyDtType><KeyDtValue>2004-11-30</KeyDtValue></KeyDt><CurCode>AED</CurCode><AmountDtls><AmtType>Outstanding_balance</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>Credit_limit</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>Overdue_amount</AmtType><Amt>0</Amt></AmountDtls><MonthsOnBook>164</MonthsOnBook><SchemeCardProd>MSTANDARD-EXPATRIATE</SchemeCardProd><CurrentlyCurrentFlg>Y</CurrentlyCurrentFlg><GeneralStatus>STWO</GeneralStatus><DelinquencyInfo><BucketType>DPD_30_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>Bucket</BucketType><BucketValue>0</BucketValue></DelinquencyInfo></CardDetails><CardDetails><CardEmbossNum>075161000</CardEmbossNum><CardStatus>A</CardStatus><CardType>CREDIT CARDS</CardType><KeyDt><KeyDtType>Card_approve_date</KeyDtType><KeyDtValue>2015-05-28</KeyDtValue></KeyDt><KeyDt><KeyDtType>CardsApplcnRecvdDate</KeyDtType><KeyDtValue>2015-05-28</KeyDtValue></KeyDt><CurCode>AED</CurCode><AmountDtls><AmtType>Outstanding_balance</AmtType><Amt>97554.74</Amt></AmountDtls><AmountDtls><AmtType>Credit_limit</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>Overdue_amount</AmtType><Amt>0</Amt></AmountDtls><MonthsOnBook>38</MonthsOnBook><LastRepmtDt>09-2016</LastRepmtDt><SchemeCardProd>SME-TITANIUM</SchemeCardProd><CurrentlyCurrentFlg>Y</CurrentlyCurrentFlg><GeneralStatus>AUWO</GeneralStatus><DelinquencyInfo><BucketType>DPD_30_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>Bucket</BucketType><BucketValue>0</BucketValue></DelinquencyInfo></CardDetails><CardDetails><CardEmbossNum>010790200</CardEmbossNum><CardStatus>C</CardStatus><CardType>CREDIT CARDS</CardType><KeyDt><KeyDtType>Card_approve_date</KeyDtType><KeyDtValue>2004-11-30</KeyDtValue></KeyDt><KeyDt><KeyDtType>CardsApplcnRecvdDate</KeyDtType><KeyDtValue>2004-11-30</KeyDtValue></KeyDt><CurCode>AED</CurCode><AmountDtls><AmtType>Outstanding_balance</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>Credit_limit</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>Overdue_amount</AmtType><Amt>0</Amt></AmountDtls><MonthsOnBook>164</MonthsOnBook><SchemeCardProd>VCLASSIC-EXPATR</SchemeCardProd><CurrentlyCurrentFlg>Y</CurrentlyCurrentFlg><GeneralStatus>STWO</GeneralStatus><DelinquencyInfo><BucketType>DPD_30_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>Bucket</BucketType><BucketValue>0</BucketValue></DelinquencyInfo></CardDetails></ProductExposureDetails></CustomerExposureResponse></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if (callName.indexOf("CARD_INSTALLMENT_DETAILS")>-1)
		{
			response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>CARD_INSTALLMENT_DETAILS</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>1</ReturnCode><ReturnDesc>PRMSD : This account does not exist in database</ReturnDesc><MessageId>CAS153379502338921</MessageId><Extra1>REP||BPM.123</Extra1><Extra2>2018-08-09T10:10:24.512+04:00</Extra2></EE_EAI_HEADER></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(callName.indexOf("SALDET")>-1)
		{
			response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>FINANCIAL_SUMMARY</MsgFormat><MsgVersion>0000</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>CINF212</ReturnCode><ReturnDesc>FIN : [CINF212]-NO SALARY RECORD FOUND</ReturnDesc><MessageId>CAS153382636971559</MessageId><Extra1>REP||LAXMANRET.LAXMANRET</Extra1><Extra2>2018-08-09T06:52:50.103+04:00</Extra2></EE_EAI_HEADER></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(callName.indexOf("RETURNDET")>-1)
		{
			response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>FINANCIAL_SUMMARY</MsgFormat><MsgVersion>0000</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>CINF728</ReturnCode><ReturnDesc>FIN : [CINF728]-NO CHEQUE OR DDS RETURN FOUND</ReturnDesc><MessageId>CAS153379510967534</MessageId><Extra1>REP||LAXMANRET.LAXMANRET</Extra1><Extra2>2018-08-09T10:11:50.233+04:00</Extra2></EE_EAI_HEADER></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(callName.indexOf("TRANSUM")>-1)
		{
			response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>FINANCIAL_SUMMARY</MsgFormat><MsgVersion>0000</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>CINF731</ReturnCode><ReturnDesc>FIN : [CINF731]-TRANSACTION SUMMARY NOT FOUND</ReturnDesc><MessageId>CAS153414285806563</MessageId><Extra1>REP||LAXMANRET.LAXMANRET</Extra1><Extra2>2018-08-13T10:47:38.472+04:00</Extra2></EE_EAI_HEADER></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(callName.indexOf("AVGBALDET")>-1)
		{
			response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>FINANCIAL_SUMMARY</MsgFormat><MsgVersion>0000</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153354273860916</MessageId><Extra1>REP||LAXMANRET.LAXMANRET</Extra1><Extra2>2018-08-06T12:05:39.106+04:00</Extra2></EE_EAI_HEADER><FinancialSummaryRes><BankId>RAK</BankId><CIFID>0181162</CIFID><AcctId>0033181162002</AcctId><OperationType>AVGBALDET</OperationType><OperationDesc>AVERAGE BALANCE</OperationDesc><TxnSummary></TxnSummary><AvgBalanceDtls><Month>JUN</Month><AvgBalance>0</AvgBalance></AvgBalanceDtls><AvgBalanceDtls><Month>MAY</Month><AvgBalance>0</AvgBalance></AvgBalanceDtls><AvgBalanceDtls><Month>APR</Month><AvgBalance>0</AvgBalance></AvgBalanceDtls><AvgBalanceDtls><Month>MAR</Month><AvgBalance>0</AvgBalance></AvgBalanceDtls><AvgBalanceDtls><Month>FEB</Month><AvgBalance>0</AvgBalance></AvgBalanceDtls><AvgBalanceDtls><Month>JAN</Month><AvgBalance>0</AvgBalance></AvgBalanceDtls></FinancialSummaryRes></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(callName.indexOf("LIENDET")>-1)
		{
			response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>FINANCIAL_SUMMARY</MsgFormat><MsgVersion>0000</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>CINF729</ReturnCode><ReturnDesc>FIN : [CINF729]-NO LIEN DETAILS FOUND</ReturnDesc><MessageId>CAS153405218506324</MessageId><Extra1>REP||LAXMANRET.LAXMANRET</Extra1><Extra2>2018-08-12T09:36:26.355+04:00</Extra2></EE_EAI_HEADER></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(callName.equalsIgnoreCase("CUSTOMER_SEARCH_REQUEST")){
			response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>CUSTOMER_SEARCH_REQUEST</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>9999</ReturnCode><ReturnDesc>CASMEX : No record found</ReturnDesc><MessageId>CAS153423574414232</MessageId><Extra1>REP</Extra1><Extra2>2018-08-14T12:35:45.145+04:00</Extra2></EE_EAI_HEADER></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}

		return response;        
	}

	public void Integration_fragName(String callName)
	{
		try{
			FormReference formObject=FormContext.getCurrentInstance().getFormReference();
			String Curr_framename=formObject.getNGValue("FrameName");
			String call_framename="";
			if(("CUSTOMER_DETAILS").equalsIgnoreCase(callName))
			{				
				call_framename="CustomerDetails,EmploymentDetails,Address_Details_container,OECD,Alt_Contact_container,FATCA,KYC";
			}
			else if(("dectech").equalsIgnoreCase(callName)){
				call_framename="EligibilityAndProductInformation,InternalExternalLiability";
			}
			else if(("NEW_CUSTOMER_REQ").equalsIgnoreCase(callName)){
				call_framename="DecisionHistory,CustomerDetails";
			}
			else if(("DEDUP_SUMMARY").equalsIgnoreCase(callName)){
				call_framename="Part_Match,FinacleCRM_CustInfo";
			}
			else if(("BLACKLIST_DETAILS").equalsIgnoreCase(callName)){
				call_framename="Part_Match,Finacle_CRM_Incidents";
			}	
			else if("NEW_CUSTOMER_REQ".equalsIgnoreCase(callName) || "CUSTOMER_UPDATE_REQ".equalsIgnoreCase(callName) || "CIF_UNLOCK".equalsIgnoreCase(callName) || "CIF_LOCK".equalsIgnoreCase(callName) || "CIF_VERIFY".equalsIgnoreCase(callName) || "CIF_ENQUIRY".equalsIgnoreCase(callName)) {
				call_framename="DecisionHistory";
			}
			else if("CHEQUE_BOOK_ELIGIBILITY".equalsIgnoreCase(callName) || "CUSTOMER_UPDATE_REQ".equalsIgnoreCase(callName)  ) {
				call_framename="Loan_Disbursal,DecisionHistory";
			}
			else if("CARD_NOTIFICATION".equalsIgnoreCase(callName) || "CARD_SERVICES_REQUEST".equalsIgnoreCase(callName) || "NEW_CREDITCARD_REQ".equalsIgnoreCase(callName)) {
				call_framename="CC_Creation,DecisionHistory";
			} else if ("RISK_SCORE_DETAILS".equalsIgnoreCase(callName)){
				call_framename="Risk_Rating";
			}



			if(!"".equalsIgnoreCase(call_framename)){
				String[] call_framename_arr = call_framename.split(",");
				for(int x = 0 ;x<call_framename_arr.length;x++){
					if(!Curr_framename.contains(call_framename_arr[x])){
						if("".equalsIgnoreCase(Curr_framename)){
							Curr_framename = call_framename_arr[x];
						}
						else{
							Curr_framename = Curr_framename+","+call_framename_arr[x];
						}
					}
				}
				formObject.setNGValue("FrameName", Curr_framename+",");
			}
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info( "Exception occured in Integration_fragName: "+ e.getMessage());
		}
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

					//PersonalLoanS.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");

				if ("AddressDetails".equalsIgnoreCase(tag_name) && int_xml.containsKey(parent_tag)) {
					String xml_str = int_xml.get(parent_tag);
					//PersonalLoanS.mLogger.info("RLOS COMMON"+ " before adding address+ " + xml_str);
					xml_str = xml_str + getCustAddress_details(callName);
					//PersonalLoanS.mLogger.info("RLOS COMMON"+ " after adding address+ " + xml_str);
					int_xml.put(parent_tag, xml_str);
				} 
				else if("MaritalStatus".equalsIgnoreCase(tag_name)){
					String marrital_code = formObject.getNGValue("cmplx_Customer_MAritalStatus").substring(0, 1);
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<"+tag_name+">"+marrital_code
							+"</"+ tag_name+">";

					//PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
				}
			}
		}
		catch(Exception e){
			//PersonalLoanS.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + ");
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
					//PersonalLoanS.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				//below code added by akshay on 4/2/18 for proc 7395
				String tag_name =  recordFileMap.get("xmltag_name");
				String parent_tag =  recordFileMap.get("parent_tag_name");

				if(Operation_name.equalsIgnoreCase("PartMatch_CIF") && tag_name.equalsIgnoreCase("RCIFID")){
					String xml_str = int_xml.get(parent_tag);
					//PersonalLoanS.mLogger.info("RLOS COMMON"+ " before adding PartMatch_CIF+ " + xml_str);
					xml_str = xml_str +"<"+tag_name+">"+ formObject.getNGValue("cmplx_PartMatch_cmplx_Partmatch_grid",formObject.getSelectedIndex("cmplx_PartMatch_cmplx_Partmatch_grid"),0)+"</"+tag_name+">";
					//PersonalLoanS.mLogger.info("RLOS COMMON"+ " after adding PartMatch_CIF+ " + xml_str);
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

					//PersonalLoanS.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");

				if ("AddressDetails".equalsIgnoreCase(tag_name)
						&& int_xml.containsKey(parent_tag)) {
					String xml_str = int_xml.get(parent_tag);
					//PersonalLoanS.mLogger.info("RLOS COMMON"+ " before adding address+ " + xml_str);
					xml_str = xml_str + getCustAddress_details(Call_name);
					PersonalLoanS.mLogger.info("RLOS COMMON"+ " after adding address+ " + xml_str);
					//int_xml.put(parent_tag, xml_str);
				} else if("MaritalStatus".equalsIgnoreCase(tag_name)){
					String marrital_code = formObject.getNGValue("cmplx_Customer_MAritalStatus").substring(0, 1);
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<"+tag_name+">"+marrital_code
							+"</"+ tag_name+">";

					//PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
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

					//PersonalLoanS.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");



				if("PRIMARY".equalsIgnoreCase(plcommonObj.MultipleAppGridSelectedRow("MultipleApp_AppType"))){	

					if ("AddressDetails".equalsIgnoreCase(tag_name)	&& int_xml.containsKey(parent_tag)) {
						String xml_str = int_xml.get(parent_tag);
						//PersonalLoanS.mLogger.info("RLOS COMMON"+ " before adding address+ " + xml_str);
						xml_str = xml_str + getCustAddress_details(Call_name);
						//PersonalLoanS.mLogger.info("RLOS COMMON"+ " after adding address+ " + xml_str);
						int_xml.put(parent_tag, xml_str);
					} 
					else if ("MinorFlag".equalsIgnoreCase(tag_name) && "PersonDetails".equalsIgnoreCase(parent_tag)) {
						if (int_xml.containsKey(parent_tag)) {
							float Age = Float.parseFloat(formObject.getNGValue("cmplx_Customer_age"));
							String age_flag = "N";
							if(plcommonObj.isCustomerMinor(formObject)){
								age_flag = "Y";
							}
							String xml_str = int_xml.get(parent_tag);
							xml_str = xml_str + "<" + tag_name + ">" + age_flag + "</" + tag_name + ">";

							//PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding Minor flag+ " + xml_str);
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

							//PersonalLoanS.mLogger.info("RLOS COMMON"+ " after adding res_flag+ " + xml_str);
							int_xml.put(parent_tag, xml_str);
						}
					}
					else if("MinorFlag".equalsIgnoreCase(tag_name)){
						if(int_xml.containsKey(parent_tag))
						{
							float Age = Float.parseFloat(formObject.getNGValue("cmplx_Customer_age"));
							String age_flag = "N";
							if(plcommonObj.isCustomerMinor(formObject)){
								age_flag = "Y";
							}
							String xml_str = int_xml.get(parent_tag);
							xml_str = xml_str + "<"+tag_name+">"+age_flag
									+"</"+ tag_name+">";

							PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+Age);
							int_xml.put(parent_tag, xml_str);
						}		                            	
					}

					else{
						int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,Call_name);
					}
				}

				else if( "SUPPLEMENT".equalsIgnoreCase(plcommonObj.MultipleAppGridSelectedRow("MultipleApp_AppType"))){	
					//PersonalLoanS.mLogger.info("shweta supplement"+ " before adding q_SOLID+ " + tag_name +" "+ formObject.getNGValue("q_SOLID"));

					if ("AddressDetails".equalsIgnoreCase(tag_name)	&& int_xml.containsKey(parent_tag)) {
						String xml_str = int_xml.get(parent_tag);
						//PersonalLoanS.mLogger.info("RLOS COMMON"+ " before adding address+ " + xml_str);
						xml_str = xml_str + getCustAddress_details(Call_name);
						//PersonalLoanS.mLogger.info("RLOS COMMON"+ " after adding address+ " + xml_str);
						int_xml.put(parent_tag, xml_str);
					} 
					//change by shweta for defualt value points on 8th Feb 19.
					else if("PrimaryServiceCenter".equalsIgnoreCase(tag_name) || "PrimaryBranchId".equalsIgnoreCase(tag_name)){
						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<" + tag_name + ">" + formObject.getNGValue("SOLID") + "</" + tag_name + ">";
						int_xml.put(parent_tag, xml_str);
					}

					else if("PersonDetails".equalsIgnoreCase(tag_name)){
						String xml_str = int_xml.get(parent_tag);
						//PersonalLoanS.mLogger.info("RLOS COMMON"+ " before adding guarantor PersonDetails+ " + xml_str);
						xml_str = xml_str + getSupplement_PersonDetails(formObject,Call_name);
						//PersonalLoanS.mLogger.info("RLOS COMMON"+ " after adding PersonDetails " + xml_str);
						int_xml.put(parent_tag, xml_str);

					}
					else if("DocDetails".equalsIgnoreCase(tag_name)){
						String xml_str = int_xml.get(parent_tag);
						//PersonalLoanS.mLogger.info("RLOS COMMON"+ " before adding guarantor doc details " + xml_str);
						xml_str = xml_str + getSupplement_DocDetails(formObject,Call_name);
						//PersonalLoanS.mLogger.info("RLOS COMMON"+ " after adding docDetails+ " + xml_str);
						int_xml.put(parent_tag, xml_str);

					}
					else if("PhoneNumber".equalsIgnoreCase(tag_name)){
						String xml_str = int_xml.get(parent_tag);
						String ph_no="";
						if( plcommonObj.MultipleAppGridSelectedRow("MultipleApp_AppType").equalsIgnoreCase("Supplement") && formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid")>0){ 
							for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid");i++){
								if(plcommonObj.MultipleAppGridSelectedRow("MultipleApp_AppPass").equals(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,3))){
									ph_no=formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,6);
								}
							}
						}
						xml_str = xml_str +  "<"+tag_name+">"+ph_no
								+"</"+ tag_name+">";
						//PersonalLoanS.mLogger.info("RLOS COMMON"+ " after adding guarantor PhoneDetails" + xml_str);
						int_xml.put(parent_tag, xml_str);
					}

					else if("MailIdValue".equalsIgnoreCase(tag_name)){

						String xml_str = int_xml.get(parent_tag);
						String email="";
						//PersonalLoanS.mLogger.info("RLOS COMMON"+ " before adding guarantor mail details " + xml_str);
						if( plcommonObj.MultipleAppGridSelectedRow("MultipleApp_AppType").equalsIgnoreCase("Supplement") && formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid")>0){ 
							for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid");i++){
								if(plcommonObj.MultipleAppGridSelectedRow("MultipleApp_AppPass").equals(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,3))){
									email=formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,21);
									break;
								}
							}
						}
						xml_str = xml_str +  "<"+tag_name+">"+email
								+"</"+ tag_name+">";
						//PersonalLoanS.mLogger.info("RLOS COMMON"+ " after adding guarantor MailIdValue" + xml_str);
						int_xml.put(parent_tag, xml_str);
					}


					else if("EmploymentStatus".equalsIgnoreCase(tag_name)){

						String xml_str = int_xml.get(parent_tag);
						String status="";
						//PersonalLoanS.mLogger.info("RLOS COMMON"+ " before adding guarantor EmploymentStatus " + xml_str);
						if( plcommonObj.MultipleAppGridSelectedRow("MultipleApp_AppType").equalsIgnoreCase("Supplement") && formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid")>0){ 
							for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid");i++){
								if(plcommonObj.MultipleAppGridSelectedRow("MultipleApp_AppPass").equals(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,3))){
									status=plcommonObj.getGridColumnValue("Supplement_Employment_Type", "SupplementCardDetails_cmplx_SupplementGrid", i);
									break;
								}
							}
						}
						xml_str = xml_str +  "<"+tag_name+">"+status
								+"</"+ tag_name+">";
						//PersonalLoanS.mLogger.info("RLOS COMMON"+ " after adding guarantor EmploymentStatus" + xml_str);
						int_xml.put(parent_tag, xml_str);
					}

					else if("Occupation".equalsIgnoreCase(tag_name)){

						String xml_str = int_xml.get(parent_tag);
						String Occupation="";
						//PersonalLoanS.mLogger.info("RLOS COMMON"+ " before adding guarantor Occupation " + xml_str);
						if( plcommonObj.MultipleAppGridSelectedRow("MultipleApp_AppType").equalsIgnoreCase("Supplement") && formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid")>0){ 
							for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid");i++){
								if(plcommonObj.MultipleAppGridSelectedRow("MultipleApp_AppPass").equals(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,3))){
									Occupation=formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,31);
									break;
								}
							}
						}
						xml_str = xml_str +  "<"+tag_name+">"+Occupation
								+"</"+ tag_name+">";
						//PersonalLoanS.mLogger.info("RLOS COMMON"+ " after adding guarantor EmploymentStatus" + xml_str);
						int_xml.put(parent_tag, xml_str);
					}

					else{
						int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,Call_name);
					}
				} else {//by shweta

					if ("AddressDetails".equalsIgnoreCase(tag_name)	&& int_xml.containsKey(parent_tag)) {
						String xml_str = int_xml.get(parent_tag);
						//CreditCard.mLogger.info("RLOS COMMON"+ " before adding address+ " + xml_str);
						xml_str = xml_str + getCustAddress_details(Call_name);
						//CreditCard.mLogger.info("RLOS COMMON"+ " after adding address+ " + xml_str);
						int_xml.put(parent_tag, xml_str);
					}
					//change by saurabh for defualt value points on 8th Feb 19.
					else if("PrimaryServiceCenter".equalsIgnoreCase(tag_name) || "PrimaryBranchId".equalsIgnoreCase(tag_name)){
						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<" + tag_name + ">" + formObject.getNGValue("q_SOLID") + "</" + tag_name + ">";
						int_xml.put(parent_tag, xml_str);
					}
					else if ("MinorFlag".equalsIgnoreCase(tag_name)&& "PersonDetails".equalsIgnoreCase(parent_tag)) {
						if (int_xml.containsKey(parent_tag)) {
							float Age = Float.parseFloat(formObject.getNGValue("cmplx_Customer_age"));
							String age_flag = "N";
							String Nation = formObject.getNGValue("cmplx_Customer_Nationality");
							if((Age<21 && !"AE".equalsIgnoreCase(Nation)) ||("AE".equalsIgnoreCase(Nation) && Age<18.06)){
								age_flag = "Y";
							}
							String xml_str = int_xml.get(parent_tag);
							xml_str = xml_str + "<" + tag_name + ">" + age_flag + "</" + tag_name + ">";

							//CreditCard.mLogger.info("RLOS COMMON"+" after adding Minor flag+ " + xml_str);
							int_xml.put(parent_tag, xml_str);
						}
					}
					else if("RelationshipDetails".equalsIgnoreCase(tag_name)){
						if(int_xml.containsKey(parent_tag))
						{
							float Age = Float.parseFloat(formObject.getNGValue("cmplx_Customer_age"));
							String cif_id = formObject.getNGValue("CIF_ID");
							String Contact_Name = formObject.getNGValue("CUSTOMERNAME");
							String Nation = formObject.getNGValue("cmplx_Customer_Nationality");
							if((Age<21 && !"AE".equalsIgnoreCase(Nation)) ||("AE".equalsIgnoreCase(Nation) && Age<18.06)){
								String xml_str = int_xml.get(parent_tag);

								xml_str = xml_str + "<"+tag_name+">"+"<ChildCIFID>"+cif_id+"</ChildCIFID>"+
										"<ChildEntity>CUSTOMER</ChildEntity><ChildEntityType>Retail</ChildEntityType>"+
										"<ContactName>"+Contact_Name+"</ContactName><Relationship>Guardian</Relationship>"
										+"</"+ tag_name+">";

								//RLOS.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
								int_xml.put(parent_tag, xml_str);
							}
						}			

					}



					else if ("NonResidentFlag".equalsIgnoreCase(tag_name)	&& "PersonDetails".equalsIgnoreCase(parent_tag)) 
					{
						if (int_xml.containsKey(parent_tag)) {
							String xml_str = int_xml.get(parent_tag);
							String res_flag = "N";

							if ("Resident".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_ResidentNonResident"))) {
								res_flag = "Y";
							}

							xml_str = xml_str + "<" + tag_name + ">" + res_flag + "</" + tag_name + ">";

							//CreditCard.mLogger.info("RLOS COMMON"+ " after adding res_flag+ " + xml_str);
							int_xml.put(parent_tag, xml_str);
						}
					}
					else{
						int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,Call_name);
					}

				}


			}
		}
		catch(Exception e){
			//PersonalLoanS.mLogger.info("CC Integration "+ " Exception occured in NEW_CUST_REQUEST + ");
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

					//PersonalLoanS.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				String form_control =  recordFileMap.get("form_control");
				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");
				PersonalLoanS.mLogger.info("Inside CUSTOMER_UPDATE_CUSTOM tag name"+tag_name);
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
				else if("ResidentSince".equalsIgnoreCase(tag_name)){
					if(int_xml.containsKey(parent_tag))
					{
						
						String xml_str = int_xml.get(parent_tag);
						try{
							Date currentDate=new Date();
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							String modifiedDate= sdf.format(currentDate);
							PersonalLoanS.mLogger.info("cmplx_Customer_yearsInUAE val: + "+formObject.getNGValue("cmplx_Customer_yearsInUAE"));
							double yearsInUAE = new Double(formObject.getNGValue("cmplx_Customer_yearsInUAE"));
							int year = (int)yearsInUAE;
							PersonalLoanS.mLogger.info("while adding year: "+year);
							int mon = (int)((yearsInUAE-year)*100);	
							PersonalLoanS.mLogger.info("while adding year: "+year);
							PersonalLoanS.mLogger.info("while adding mon: "+mon);
							String rest_date = PLCommon.plusyear(modifiedDate,-year,-mon,0);
							xml_str = xml_str + "<" + tag_name + ">" + rest_date + "</" + tag_name + ">";
							PersonalLoanS.mLogger.info("while adding ResidentSince+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}
						catch(Exception e){
							PersonalLoanS.mLogger.info("Exception inside ResidentSince "+e.getMessage());
						}
					}
				}
				else if("Desig".equalsIgnoreCase(tag_name)){
					String Designation=formObject.getNGValue(form_control);
					try{
						String sQuery = "select Description from ng_master_Designation with (nolock) where code='" +  formObject.getNGValue(form_control) + "'";

						//RLOS.mLogger.info("RLOS val change Value of dob is:"+sQuery);

						List<List<String>> recordList = formObject.getNGDataFromDataCache(sQuery);
						//RLOS.mLogger.info("RLOS val change Vasdfsdflue of dob is:"+recordList.get(0).get(0));
						if(recordList.get(0).get(0)!= null && recordList.get(0)!=null && !"".equalsIgnoreCase(recordList.get(0).get(0)) && recordList!=null)
						{
							Designation=recordList.get(0).get(0);
						}
					}
					catch(Exception e){}
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<" + tag_name + ">" + Designation + "</" + tag_name + ">";

					PersonalLoanS.mLogger.info("PL COMMON after adding Designation:  " + xml_str);
					int_xml.put(parent_tag, xml_str);
				}

				else if(plcommonObj.MultipleAppGridSelectedRow("MultipleApp_AppType")!=null && "PRIMARY".equalsIgnoreCase(plcommonObj.MultipleAppGridSelectedRow("MultipleApp_AppType")))
				{
					//Done by aman for CR 1212
					if ("PhnLocalCode".equalsIgnoreCase(tag_name)) {
						PersonalLoanS.mLogger.info("inside PL Common generate xml"+"PhnLocalCode to substring"+form_control);
						String xml_str = int_xml.get(parent_tag);
						String phn_no = formObject.getNGValue(form_control);
						if ((!"".equalsIgnoreCase(phn_no)) && phn_no.indexOf("00971") > -1) 
						{
							phn_no = phn_no.substring(5);
						}

						xml_str = xml_str + "<" + tag_name + ">" + phn_no + "</" + tag_name + ">";

						PersonalLoanS.mLogger.info("PL COMMON"+ " after adding ApplicationID:  " + xml_str);
						int_xml.put(parent_tag, xml_str);
					}

					//Done by aman for CR 1212
					else if("OECDDet".equalsIgnoreCase(tag_name) && "CUSTOMER_UPDATE_REQ".equalsIgnoreCase(Call_name)){
						PersonalLoanS.mLogger.info("PL Common"+"inside OECDDet inside customer update req1");
						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + getCustOECD_details(formObject,"PRIMARY");
						PersonalLoanS.mLogger.info("PL COMMON"+" after adding OECDDet: "+xml_str);
						int_xml.put(parent_tag, xml_str);                                    
					}

					else if("FatcaDetails".equalsIgnoreCase(tag_name)){
						PersonalLoanS.mLogger.info("PL Common"+"inside fatca inside customer update req1");
						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + getFATCA_details(formObject,"PRIMARY");
						PersonalLoanS.mLogger.info("PL COMMON"+" after adding FatcaDetails: "+xml_str);
						int_xml.put(parent_tag, xml_str);                                    
					}

					else if("KYCDetails".equalsIgnoreCase(tag_name)){
						PersonalLoanS.mLogger.info("PL Common"+"inside KYCDetails");
						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + getKYC_details(formObject,"PRIMARY");
						PersonalLoanS.mLogger.info("PL COMMON"+" after adding KYCDetails: "+xml_str);
						int_xml.put(parent_tag, xml_str);                                    
					}
					else if ("OECDDet".equalsIgnoreCase(tag_name) && int_xml.containsKey(parent_tag)) {
						//CreditCard.mLogger.info("inside 1st if"+ "inside customer update req2123");
						String xml_str = int_xml.get(parent_tag);
						//CreditCard.mLogger.info("RLOS COMMON"+ " before adding OECD+ " + xml_str);
						xml_str = xml_str + getCustOECD_details(formObject,Call_name);
						//CreditCard.mLogger.info("RLOS COMMON"+ " after adding OeCD+ " + xml_str);
						int_xml.put(parent_tag, xml_str);
					}

					/*else if ("PhnLocalCode".equalsIgnoreCase(tag_name)) {
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
					}*/
					else if("AECBconsentHeld".equalsIgnoreCase(tag_name)){
						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<" + tag_name + ">" + ("true".equalsIgnoreCase(formObject.getNGValue(form_control))?"Y":"N") + "</" + tag_name + ">";

						PersonalLoanS.mLogger.info("RLOS COMMON"+ " after adding AECBconsentHeld flag+ " + xml_str);
						int_xml.put(parent_tag, xml_str);
					}
					else if("NonResidentInd".equalsIgnoreCase(tag_name)){
						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<" + tag_name + ">" + ("N".equalsIgnoreCase(formObject.getNGValue(form_control))?"Y":"N") + "</" + tag_name + ">";

						//CreditCard.mLogger.info("RLOS COMMON"+ " after adding AECBconsentHeld flag+ " + xml_str);
						int_xml.put(parent_tag, xml_str);
					}
					else if("ShortName".equalsIgnoreCase(tag_name))
					{
						String xml_str = int_xml.get(parent_tag);
						String fullName = "";
						PersonalLoanS.mLogger.info("Inside ShortNAme");
						if(formObject.getNGValue("cmplx_Customer_MiddleNAme")!=null && !formObject.getNGValue("cmplx_Customer_MiddleNAme").isEmpty()){
							fullName = formObject.getNGValue("cmplx_Customer_FirstNAme")+" "+formObject.getNGValue("cmplx_Customer_MiddleNAme")+" "+formObject.getNGValue("cmplx_Customer_LastNAme");
						}
						else{
							fullName = formObject.getNGValue("cmplx_Customer_FirstNAme")+" "+formObject.getNGValue("cmplx_Customer_LastNAme");
						}
						String SN="";
						if(fullName.length()>=10){
							SN=fullName.substring(0,10);
						}else{
							SN=fullName;
						}						
						PersonalLoanS.mLogger.info("FN: "+fullName);
						PersonalLoanS.mLogger.info("SN: "+SN);
						xml_str = xml_str + "<" + tag_name + ">" + SN + "</" + tag_name + ">";
						int_xml.put(parent_tag, xml_str);
					}//By Alok for short name
					/*else if("ProductProcessor".equalsIgnoreCase(tag_name) ){
						PersonalLoanS.mLogger.info("PL_Integration_input"+ " Inside if product processor");
						String xml_str = int_xml.get(parent_tag);
						String ProductProcessor_val = "";
						PersonalLoanS.mLogger.info("PL_Integration_input"+ " is_cc_waiver_require require---"+formObject.getNGValue("is_cc_waiver_require"));
						if("Y".equalsIgnoreCase(formObject.getNGValue("is_cc_waiver_require"))){
							ProductProcessor_val = "FINACLECORE,RLS";
						}
//						else{
//							ProductProcessor_val = "CAPS,RLS,FINACLECORE";
//						}
						xml_str = xml_str + "<" + tag_name + ">" + ProductProcessor_val + "</" + tag_name + ">";
						int_xml.put(parent_tag, xml_str);
					}//Shivanshi
*/					else{
						int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,Call_name);
					}
				}
				else if(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)!=null && "SUPPLEMENT".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)))
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
					else if("CIFId".equalsIgnoreCase(tag_name) ){
						PersonalLoanS.mLogger.info("inside 1st if inside customer update req");
						String xml_str = int_xml.get(parent_tag);
						xml_str =xml_str+ "<"+tag_name+">"+formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),3)+"</"+ tag_name+">";
						PersonalLoanS.mLogger.info("PL COMMON  after adding CIFId:  "+xml_str);
						int_xml.put(parent_tag, xml_str);	                            	
					}
					else if("PhoneNo".equalsIgnoreCase(tag_name) ){//|| "PhnLocalCode".equalsIgnoreCase(tag_name)
						String xml_str = int_xml.get(parent_tag);
						String ph_no ="",phlocal="";
						if( formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12).equalsIgnoreCase("Supplement") && formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid")>0){ 
							for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid");i++){
								if(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13).equals(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,3))){
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
						if( formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12).equalsIgnoreCase("Supplement") && formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid")>0){ 
							for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid");i++){
								if(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13).equals(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,3))){
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
					else if("NonResidentInd".equalsIgnoreCase(tag_name)){
						String xml_str = int_xml.get(parent_tag);
						String suppPassport = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13);
						for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_supplementGrid");i++){
							if(formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,3).equals(suppPassport)){
								xml_str = xml_str + "<" + tag_name + ">" + ("false".equalsIgnoreCase(formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,16))?"Y":"N") + "</" + tag_name + ">";
								break;
							}
						}

						int_xml.put(parent_tag, xml_str);
					}
					else{	
						int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,Call_name);
					}
				}
				else if(plcommonObj.MultipleAppGridSelectedRow("MultipleApp_AppType")!=null && "GUARANTOR".equalsIgnoreCase(plcommonObj.MultipleAppGridSelectedRow("MultipleApp_AppType")))
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
					else if("CIFId".equalsIgnoreCase(tag_name) ){
						PersonalLoanS.mLogger.info("inside 1st if inside customer update req");
						String xml_str = int_xml.get(parent_tag);
						xml_str =xml_str+ "<"+tag_name+">"+plcommonObj.MultipleAppGridSelectedRow("MultipleApp_AppCIF")+"</"+ tag_name+">";
						PersonalLoanS.mLogger.info("PL COMMON  after adding CIFId:  "+xml_str);
						int_xml.put(parent_tag, xml_str);	                            	
					}
					else if("PhoneNo".equalsIgnoreCase(tag_name) ){//|| "PhnLocalCode".equalsIgnoreCase(tag_name)
						String xml_str = int_xml.get(parent_tag);
						String ph_no ="",phlocal="";
						if(plcommonObj.MultipleAppGridSelectedRow("MultipleApp_AppPass").equals(formObject.getNGValue("cmplx_Guarantror_GuarantorDet",0,5)))
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
						String Email = "";
						if(formObject.getLVWRowCount("cmplx_Guarantror_GuarantorDet")>0){
							Email = formObject.getNGValue("cmplx_Guarantror_GuarantorDet",0,23);
						}

						xml_str = xml_str +  "<"+tag_name+">"+Email+"</"+ tag_name+">";
						PersonalLoanS.mLogger.info("RLOS COMMON"+ " after adding guarantor MailIdValue" + xml_str);
						int_xml.put(parent_tag, xml_str);
					}
					else{
						int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,Call_name);
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

					//PersonalLoanS.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");
				if ("VIPFlg".equalsIgnoreCase(tag_name)) {//By shweta Corrected tag name
					String vip_flag = "N";
					if ("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_VIPFlag"))) {
						vip_flag = "Y";
						PersonalLoanS.mLogger.info("Rishabh check"+vip_flag + '1');
					}
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<" + tag_name + ">" + vip_flag + "</" + tag_name + ">";

					PersonalLoanS.mLogger.info("RLOS COMMON"+ " after adding VIP flag+ " + xml_str);
					int_xml.put(parent_tag, xml_str);
				} else if ("ProcessingUserId".equalsIgnoreCase(tag_name)) {
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<" + tag_name + ">" + formObject.getUserName() + "</" + tag_name + ">";

					PersonalLoanS.mLogger.info("RLOS COMMON"+ " after adding ProcessingUserId " + xml_str);
					int_xml.put(parent_tag, xml_str);
				} else if ("ProcessingDate".equalsIgnoreCase(tag_name)) {
					String xml_str = int_xml.get(parent_tag);
					SimpleDateFormat sdf1 = new SimpleDateFormat(
							"yyyy-MM-dd'T'HH:mm:ss.mmm+hh:mm");
					xml_str = xml_str + "<" + tag_name + ">" + sdf1.format(new Date()) + "</" + tag_name + ">";

					PersonalLoanS.mLogger.info("RLOS COMMON"+ " ProcessingDate " + xml_str);
					int_xml.put(parent_tag, xml_str);
				}

				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,Call_name);
				}
			}
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("CC Integration "+ " Exception occured in NEW_CARD_Custom + ");
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
				String res_type=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i,11);
				
				PersonalLoanS.mLogger.info("PLCommon java file"+ "ADD Type: "+Address_type);
				PersonalLoanS.mLogger.info("PLCommon java file"+ "Applicant type: "+Applicant_Name);
				String preferrd;
				if(NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 10)))
					preferrd = "Y";
				else
					preferrd = "N";

				//added here
				PersonalLoanS.mLogger.info("RLOSCommon java file"+ "inside getCustAddress_details add_row_count+ : "+years_in_current_add);
				int years=0;
				//ended here
				String EffectiveFrom="";
				String EffectiveTo="";
				if (!"".equalsIgnoreCase(years_in_current_add)){
					years=(int)Float.parseFloat(years_in_current_add);

					SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
					Calendar cal = Calendar.getInstance();
					EffectiveTo=sdf1.format(cal.getTime());
					cal.add(Calendar.YEAR, -years);
					EffectiveFrom=sdf1.format(cal.getTime());
					PersonalLoanS.mLogger.info("RLOS value of CurrentDate EffectiveTo"+" "+EffectiveTo);
					PersonalLoanS.mLogger.info("RLOS value of EffectiveFromDate"+" "+EffectiveFrom);
				}
				//Code change to added Effective from and to End
				PersonalLoanS.mLogger.info("RLOS value of prefered Add: Address Type: "+Address_type+" Address pref flag: "+formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid"+ i+ 11));
				//new condition added to handle address type for all finacle calls. 22Nov2017.
				/*if("HOME".equalsIgnoreCase(Address_type) && !fin_call_name.contains(call_name)) {
					Address_type="Home Country";
				}*/
				PersonalLoanS.mLogger.info("PLCommon java file"+ "ADD Applicant_Name after: "+Applicant_Name);
				PersonalLoanS.mLogger.info("PLCommon java file"+ "ADD asdasdasdasdasdasdasdasd after: "+formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),1));
				String cust_type = formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),0);

				if("NEW_CUSTOMER_REQ".equalsIgnoreCase(call_name))
				{	
					PersonalLoanS.mLogger.info("PLCommon java file"+ "InsideNEW_CUSTOMER_REQNEW_CUSTOMER_REQ "+Applicant_Name);

					//Deepak 20 Dec changes to pick correct add details in case both primary & supplement have same name.
					if("Primary".equalsIgnoreCase(cust_type)){
						if(Applicant_Name.split("-")[0].trim().replaceAll(" ","").equalsIgnoreCase("P")&& Applicant_Name.split("-")[1].trim().replaceAll(" ","").equals(formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),1).trim().replaceAll(" ","")))
						{
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
					}
					else{
						if(Applicant_Name.split("-")[0].trim().replaceAll(" ","").equalsIgnoreCase("S") && Applicant_Name.split("-")[1].trim().replaceAll(" ","").equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),1).trim().replaceAll(" ","")) &&
								Applicant_Name.split("-")[2].trim().replaceAll(" ","").equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),2).trim().replaceAll(" ","")))
						{
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
					}

				}


				else if(Applicant_Name.split("-")[1].trim().replaceAll(" ","").equalsIgnoreCase(formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),1).trim().replaceAll(" ","")))
				{
					if(call_name.equalsIgnoreCase("CUSTOMER_UPDATE_REQ")){
						PersonalLoanS.mLogger.info("PLCommon java file"+ "CUSTOMER_UPDATE_REQCUSTOMER_UPDATE_REQCUSTOMER_UPDATE_REQ "+Applicant_Name);

						add_xml_str = add_xml_str + "<AddrDet><AddressType>"+Address_type+"</AddressType>";
						//Code change to added Effective from and to start
						add_xml_str = add_xml_str + "<EffectiveFrom>"+EffectiveFrom+"</EffectiveFrom>";
						add_xml_str = add_xml_str + "<EffectiveTo>"+"2099-12-31"+"</EffectiveTo>";
						add_xml_str = add_xml_str + "<HoldMailFlag>N</HoldMailFlag>";
						add_xml_str = add_xml_str + "<ReturnFlag>N</ReturnFlag>";
						add_xml_str = add_xml_str + "<AddrPrefFlag>"+preferrd+"</AddrPrefFlag>";
						//Code change to added Effective from and to End

						add_xml_str = add_xml_str + "<AddrLine1>"+flat_Villa+"</AddrLine1>";
						add_xml_str = add_xml_str + "<AddrLine2>"+Building_name+"</AddrLine2>";
						add_xml_str = add_xml_str + "<AddrLine3>"+street_name+"</AddrLine3>";
						add_xml_str = add_xml_str + "<AddrLine4>"+Landmard+"</AddrLine4>";
						add_xml_str = add_xml_str + "<ResType>"+res_type+"</ResType>";
						add_xml_str = add_xml_str + "<POBox>"+Po_Box+"</POBox>";
						add_xml_str = add_xml_str + "<State>"+Emirates+"</State>";
						add_xml_str = add_xml_str + "<City>"+city+"</City>";
						add_xml_str = add_xml_str + "<CountryCode>"+country+"</CountryCode></AddrDet>";
					}
					else if(call_name.equalsIgnoreCase("NEW_CREDITCARD_REQ")){
						if (Address_type.equalsIgnoreCase("Mailing")|| Address_type.equalsIgnoreCase("Home")){

						}
						else{
							if(preferrd.equalsIgnoreCase("N"))
							{
								Address_type="Additional";
							}
							/*else if(Address_type.equalsIgnoreCase("Home") || "Home Country".equalsIgnoreCase(Address_type)){
									Address_type="Additional";
								}*/		
							if("0".equalsIgnoreCase(fetchAddressFlagVal())){
								add_xml_str=getCardCrationDummyAddress_details();
							}
							else{
								if(preferrd.equalsIgnoreCase("Y")){
									add_xml_str = add_xml_str + "<AddressDetails><AddrType>Statement</AddrType><UseExistingAddress>"+fetchAddressFlagVal()+"</UseExistingAddress><AddressLocation>STMT</AddressLocation><AddressReferenceLink>"+fetchAddressFlagVal()+"</AddressReferenceLink><IsPreferedAddr>"+preferrd+"</IsPreferedAddr>";
								}
								else{
									add_xml_str = add_xml_str + "<AddressDetails><AddrType>"+Address_type+"</AddrType><UseExistingAddress>"+fetchAddressFlagVal()+"</UseExistingAddress><AddressLocation>OTHER</AddressLocation><AddressReferenceLink>"+fetchAddressFlagVal()+"</AddressReferenceLink><IsPreferedAddr>"+preferrd+"</IsPreferedAddr>";
								}
								city = getCodeDesc("NG_MASTER_City","Description", city);
								Emirates = getCodeDesc("NG_MASTER_state","Description", Emirates);

								add_xml_str = add_xml_str + "<Addr1>"+flat_Villa+"</Addr1>";
								add_xml_str = add_xml_str + "<Addr2>"+Building_name+"</Addr2>";
								add_xml_str = add_xml_str + "<Addr3>"+street_name+" "+Landmard+"</Addr3>";
								add_xml_str = add_xml_str + "<Addr4>P.O.BOX "+Po_Box+"</Addr4>";
								//add_xml_str = add_xml_str + "<Addr5>"+city+"</Addr5>";
								add_xml_str = add_xml_str + "<PostalCode>"+Po_Box+"</PostalCode>";
								add_xml_str = add_xml_str + "<ZipCode></ZipCode>";
								add_xml_str = add_xml_str + "<City>"+city+"</City>";
								add_xml_str= add_xml_str + "<StateProv>"+Emirates+"</StateProv>";
								//change by saurabh on 28th Jan
								//add_xml_str = add_xml_str + "<StateProv>"+city+"</StateProv>";
								//add_xml_str = add_xml_str + "<County>"+city+"</County>";
								add_xml_str = add_xml_str + "<Country>784</Country>"+ "</AddressDetails>";
							}
						}
					}
					else if(call_name.equalsIgnoreCase("CARD_NOTIFICATION")){
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

				/*else{
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
				}*/
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

	//code changes by bandana start	
	public String getRVCDetails(){
		//PersonalLoanS.mLogger.info("RLOSCommon java file inside getRVCDetails : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String  add_xml_str ="";
		String listViewname="cmplx_CC_Loan_cmplx_rvc";
		try{
			//<RVC><RVCPackage/><RVCStatus>No</RVCStatus><RVCSalesMode/><RVCRegDate/><RVCActivationDate/></RVC>
			if(formObject.isVisible("CC_Loan_Frame3")){
				for(int chosenRow =0 ;chosenRow<formObject.getLVWRowCount(listViewname);chosenRow++ )
				{
					String Selected_card=formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid", formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"), 5);
					String Service_for_Card=formObject.getNGValue(listViewname, chosenRow, 7);
					if(!Selected_card.equals(Service_for_Card))
					{
						continue;
					}
					if("true".equalsIgnoreCase(formObject.getNGValue(listViewname, chosenRow, 0))){
						Common_Utils common=new Common_Utils(PersonalLoanS.mLogger);
						add_xml_str = add_xml_str + "<RVC><RVCPackage>"+formObject.getNGValue(listViewname, chosenRow, 0)+"</RVCPackage>";
						add_xml_str = add_xml_str + "<RVCStatus>Yes</RVCStatus>";
						add_xml_str = add_xml_str + "<RVCSalesMode>"+formObject.getNGValue(listViewname, chosenRow, 4)+"</RVCSalesMode>";
						add_xml_str = add_xml_str + "<RVCRegDate>"+common.Convert_dateFormat(formObject.getNGValue(listViewname, chosenRow, 6), "dd/MM/yyyy","yyyy-MM-dd")+"</RVCRegDate>";
						add_xml_str = add_xml_str + "<RVCActivationDate>"+formObject.getNGValue(listViewname, chosenRow, 5)+"</RVCActivationDate></RVC>";
					}
				}
			}
			return add_xml_str;
		}
		catch(Exception ex){
			/*new CC_Common();*/
			//Commented for sonar
			PersonalLoanS.mLogger.info("Exception Occure in getRVCDetails()"+ex.getMessage());
			PLCommon.printException(ex);
			return add_xml_str;
		}
	}

	public String getDDSDetails(){
		PersonalLoanS.mLogger.info("RLOSCommon java file inside getDDSDetails : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String  add_xml_str ="";
		String listViewname="cmplx_CC_Loan_cmplx_dds";
		try{
			if(formObject.isVisible("CC_Loan_Frame4")){
				for(int chosenRow =0 ;chosenRow<formObject.getLVWRowCount(listViewname);chosenRow++ )
				{
					String Selected_card=formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid", formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"), 5);
					String Service_for_Card=formObject.getNGValue(listViewname, chosenRow, 10);
					if(!Selected_card.equals(Service_for_Card))
					{
						continue;
					}
					if("true".equalsIgnoreCase(formObject.getNGValue(listViewname, chosenRow, 0))){
						Common_Utils common=new Common_Utils(PersonalLoanS.mLogger);
						add_xml_str = add_xml_str + "<PaymentInstruction><TypeIndicator>DDS</TypeIndicator>";
						add_xml_str = add_xml_str + "<Mode>"+formObject.getNGValue(listViewname, chosenRow, 1)+"</Mode>";
						add_xml_str = add_xml_str + "<FlatAmount>"+formObject.getNGValue(listViewname, chosenRow, 3)+"</FlatAmount>";
						add_xml_str = add_xml_str + "<Percentage>"+formObject.getNGValue(listViewname, chosenRow, 2)+"</Percentage>";
						add_xml_str = add_xml_str + "<ExecutionDay>"+formObject.getNGValue(listViewname, chosenRow, 4)+"</ExecutionDay>";
						add_xml_str = add_xml_str + "<StartDate>"+common.Convert_dateFormat(formObject.getNGValue(listViewname, chosenRow, 7), "dd/MM/yyyy","yyyy-MM-dd")+"</StartDate>";// Modified by Rajan for PCASP-2295
						add_xml_str = add_xml_str + "<DebitBankName>"+formObject.getNGValue(listViewname, chosenRow, 8)+"</DebitBankName>";
						add_xml_str = add_xml_str + "<DebitAccountNum>"+formObject.getNGValue(listViewname, chosenRow, 6)+"</DebitAccountNum>";
						add_xml_str = add_xml_str + "<AccountType>"+formObject.getNGValue(listViewname, chosenRow, 5)+"</AccountType>";
						add_xml_str = add_xml_str + "<EntityNumber>"+formObject.getNGValue(listViewname, chosenRow, 9)+"</EntityNumber></PaymentInstruction>";
					}
				}
			}
			return add_xml_str;
		}
		catch(Exception ex){
			/*	new CC_Common();*/
			//Commented for sonar
			PersonalLoanS.mLogger.info("Exception Occure in getDDSDetails()"+ex.getMessage());
			PLCommon.printException(ex);
			return add_xml_str;
		}
	}
	//code changes by bandana ends

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
		//PersonalLoanS.mLogger.info("RLOSCommon java file"+ "inside getCustAddress_details add_row_count+ : "+prod_row_count);
		//PersonalLoanS.mLogger.info("RLOSCommon java file"+ "inside getProduct_details : "+formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit"));
		String  prod_xml_str ="";
		String Manual_Dev=formObject.getNGValue("cmplx_Decision_Manual_Deviation");
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
			String newTenure = formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor");
			if(newTenure != null && newTenure!=""){
				tenure=newTenure;
			}
			String interestRate = formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate");
			if(interestRate == null){
				interestRate = "";
			}
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
				prod_xml_str = prod_xml_str + "<interest_rate>"+interestRate+"</interest_rate>";
				prod_xml_str = prod_xml_str + "<customer_type>"+(NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))?"NTB":"Existing")+"</customer_type>";
				if(Manual_Dev!=null){ 
					if("true".equalsIgnoreCase(Manual_Dev)){
						prod_xml_str = prod_xml_str + "<limit_expiry_date></limit_expiry_date><final_limit>"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_PLHidden")+"</final_limit><cc_bundle_limit>"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_EFCHidden")+"</cc_bundle_limit><emi>"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_EMI")+"</emi><manual_deviation>Y</manual_deviation><application_date>"+ApplicationDate+"</application_date></ApplicationDetails>";
					}
					else{
						prod_xml_str = prod_xml_str + "<limit_expiry_date></limit_expiry_date><final_limit>"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_PLHidden")+"</final_limit><cc_bundle_limit>"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_EFCHidden")+"</cc_bundle_limit><emi>"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_EMI")+"</emi><manual_deviation>N</manual_deviation><application_date>"+ApplicationDate+"</application_date></ApplicationDetails>";
					}   
				}
				else {
					prod_xml_str = prod_xml_str + "<limit_expiry_date></limit_expiry_date><final_limit>"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_PLHidden")+"</final_limit><cc_bundle_limit>"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_EFCHidden")+"</cc_bundle_limit><emi>"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_EMI")+"</emi><manual_deviation></manual_deviation><application_date>"+ApplicationDate+"</application_date></ApplicationDetails>";

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
		//PersonalLoanS.mLogger.info("RLOSCommon java file"+ "inside InternalBureauData : ");
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
			String sQuery = "SELECT isNull((Sum(Abs(convert(float,replace([OutstandingAmt],'NA','0'))))),0),isNull((Sum(Abs(convert(float,replace([OverdueAmt],'NA','0'))))),0),isNull((Sum(convert(float,replace([CreditLimit],'NA','0')))),0)FROM ng_RLOS_CUSTEXPOSE_CardDetails WHERE Child_Wi= '"+formObject.getWFWorkitemName()+"' AND Request_Type = 'InternalExposure'  union SELECT   isNull((Sum(Abs(convert(float,replace([TotalOutstandingAmt],'NA','0'))))),0),isNull((Sum(Abs(convert(float,replace([OverdueAmt],'NA','0'))))),0),isNull((Sum(convert(float,replace([TotalLoanAmount],'NA','0')))),0) FROM ng_RLOS_CUSTEXPOSE_LoanDetails   with (nolock) WHERE Child_wi = '"+formObject.getWFWorkitemName()+"'  and  LoanStat  not in ('Pipeline','CAS-Pipeline')";
			List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
			PersonalLoanS.mLogger.info("InternalBureauData list size"+OutputXML.size()+ ""+"this is squery"+sQuery);

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
			String sQueryDerived = "select NoOfContracts,Total_Exposure,WorstCurrentPaymentDelay,Worst_PaymentDelay_Last24Months,Nof_Records from NG_RLOS_CUSTEXPOSE_Derived with(nolock) where Request_Type='CollectionsSummary' and child_wi='"+formObject.getWFWorkitemName()+"'" ;
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
			if(!formObject.isVisible("FinacleCore_Frame6")){
				String sQuerycheque = "SELECT 'DDS 3 months',count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls with (nolock) WHERE CAST(returnDate AS datetime) >= DATEADD(month,-3,GETDATE()) and returntype='DDS' and child_wi='"+formObject.getWFWorkitemName()+"' union SELECT 'DDS 6 months',count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls with (nolock) WHERE CAST(returnDate AS datetime) >= DATEADD(month,-6,GETDATE()) and returntype='DDS' and child_wi='"+formObject.getWFWorkitemName()+"' union SELECT 'ICCS 3 months',count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls with (nolock) WHERE CAST(returnDate AS datetime) >= DATEADD(month,-3,GETDATE()) and returntype='ICCS' and child_wi='"+formObject.getWFWorkitemName()+"' union SELECT 'ICCS 6 months',count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls with (nolock) WHERE CAST(returnDate AS datetime) >= DATEADD(month,-6,GETDATE()) and returntype='ICCS' and child_wi='"+formObject.getWFWorkitemName()+"'" ;
				List<List<String>> OutputXMLcheque = formObject.getDataFromDataSource(sQuerycheque);

				PersonalLoanS.mLogger.info( "ICCS/DDS query: "+ sQuerycheque);
				add_xml_str = add_xml_str + "<cheque_return_3mon>"+OutputXMLcheque.get(2).get(1)+"</cheque_return_3mon>";
				add_xml_str = add_xml_str + "<dds_return_3mon>"+OutputXMLcheque.get(0).get(1)+"</dds_return_3mon>";
				add_xml_str = add_xml_str + "<cheque_return_6mon>"+OutputXMLcheque.get(3).get(1)+"</cheque_return_6mon>";
				add_xml_str = add_xml_str + "<dds_return_6mon>"+OutputXMLcheque.get(1).get(1)+"</dds_return_6mon>";
			}
			else{
				int ICCS_3 = 0;
				int ICCS_6 = 0;
				int DDS_3 = 0;
				int DDS_6 = 0;
				String lvName = "cmplx_FinacleCore_DDSgrid";
				if(formObject.getLVWRowCount(lvName)>0){
					for(int i=0;i<formObject.getLVWRowCount(lvName);i++){
						PersonalLoanS.mLogger.info( "ICCS/DDS[2] : "+ formObject.getNGValue(lvName,i,2));
						if("ICCS".equalsIgnoreCase(formObject.getNGValue(lvName,i,2)) && formObject.getNGValue(lvName, i, 14).equalsIgnoreCase("true")){
							//Deepak Change done for PCSP-146 (of the month is 3 then to pass if its more then three - 3 months 1 day then it should be 0 only start.
							if(monthsDiff(formObject.getNGValue(lvName,i,5),1)<3 ){
								ICCS_3++;
							}
							else if(monthsDiff(formObject.getNGValue(lvName,i,5),1)<6 && monthsDiff(formObject.getNGValue(lvName,i,5),1)>=3 ){
								ICCS_6++;
							}
						}
						//below code added by nikhil for PCSP-145
						else if("DDS".equalsIgnoreCase(formObject.getNGValue(lvName,i,2)) && formObject.getNGValue(lvName, i, 14).equalsIgnoreCase("true")){
							if(monthsDiff(formObject.getNGValue(lvName,i,5),1)<3){
								DDS_3++;
							}
							else if(monthsDiff(formObject.getNGValue(lvName,i,5),1)<6 && monthsDiff(formObject.getNGValue(lvName,i,5),1)>=3){
								DDS_6++;
							}
						}
					
					}
				}
				//PersonalLoanS.mLogger.info( "ICCS_3: "+ ICCS_3);
				//PersonalLoanS.mLogger.info( "ICCS_6: "+ ICCS_6);
				//PersonalLoanS.mLogger.info( "DDS_3: "+ DDS_3);
				//PersonalLoanS.mLogger.info( "DDS_6: "+ DDS_6);

				add_xml_str = add_xml_str + "<cheque_return_3mon>"+ICCS_3+"</cheque_return_3mon>";
				add_xml_str = add_xml_str + "<dds_return_3mon>"+DDS_3+"</dds_return_3mon>";
				add_xml_str = add_xml_str + "<cheque_return_6mon>"+ICCS_6+"</cheque_return_6mon>";
				add_xml_str = add_xml_str + "<dds_return_6mon>"+DDS_6+"</dds_return_6mon>";

			}			add_xml_str = add_xml_str + "<internal_charge_off>"+"N"+"</internal_charge_off><company_flag>N</company_flag></InternalBureau>";// to be populated later



			//PersonalLoanS.mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "+ add_xml_str);
			return add_xml_str;
		}
		catch(Exception e)
		{
			//Changes done for code optimization 25/07
			//new PersonalLoanSCommonCode();
			//Changes done for code optimization 25/07
			PersonalLoanS.mLogger.info("RLOSCommon"+ "Exception occurred in InternalBureauData()"+e.getMessage());
			PLCommon.printException(e);
			return "";
		}

	}

	public int monthsDiff(String diffDate,int dayToAdd){
		try{
			DateTime dt = new DateTime();
			DateTimeFormatter fp = DateTimeFormat.forPattern("dd/MM/yyyy");
			DateTime d2 = fp.parseDateTime(diffDate);
			Months m = Months.monthsBetween(d2, dt);
			return m.getMonths();
		}
		catch(Exception ex){
			PersonalLoanS.mLogger.info("Exception occured in monthsDiff(): ");
			PLCommon.printException(ex);
			return -1;
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
		//PersonalLoanS.mLogger.info("RLOSCommon java file"+ "inside InternalBouncedCheques : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "select CIFID,AcctId,returntype,returnNumber,returnAmount,retReasonCode,returnDate from ng_rlos_FinancialSummary_ReturnsDtls with (nolock) where Child_Wi = '"+formObject.getWFWorkitemName()+"'";
		//PersonalLoanS.mLogger.info("InternalBouncedCheques sQuery"+sQuery+ "");
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		//PersonalLoanS.mLogger.info("InternalBouncedCheques list size"+OutputXML.size()+ "");

		for (int i = 0; i<OutputXML.size();i++){

			String applicantID = "";
			String chequeNo = "";
			String internal_bounced_cheques_id = "";
			String bouncedCheq="";
			String amount = "";
			String reason = ""; 
			String returnDate = "";


			if(!(OutputXML.get(i).get(0) == null || "".equals(OutputXML.get(i).get(0))) ){
				applicantID = OutputXML.get(i).get(0).toString();
			}
			if(!(OutputXML.get(i).get(1) == null || "".equals(OutputXML.get(i).get(1))) ){
				internal_bounced_cheques_id = OutputXML.get(i).get(1).toString();
			}
			if(!(OutputXML.get(i).get(2) == null || "".equals(OutputXML.get(i).get(2))) ){
				bouncedCheq = OutputXML.get(i).get(2).toString();
			}
			if(!(OutputXML.get(i).get(3) == null || "".equals(OutputXML.get(i).get(3))) ){
				chequeNo = OutputXML.get(i).get(3).toString();
			}
			if(!(OutputXML.get(i).get(4) == null || "".equals(OutputXML.get(i).get(4))) ){
				amount = OutputXML.get(i).get(4).toString();
			}
			if(!(OutputXML.get(i).get(5) == null || "".equals(OutputXML.get(i).get(5))) ){
				reason = OutputXML.get(i).get(5).toString();
			}
			if(!(OutputXML.get(i).get(6) == null || "".equals(OutputXML.get(i).get(6))) ){
				returnDate = OutputXML.get(i).get(6).toString();
			}


			if(applicantID!=null && !"".equalsIgnoreCase(applicantID) && !"null".equalsIgnoreCase(applicantID))
			{
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
		//added by aastha for jira: PCAS-2145,1295 Sprint 4 Iteration 1
		PersonalLoanS.mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "
				+ add_xml_str);
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
		//PersonalLoanS.mLogger.info("RLOSCommon java file"+ "inside InternalBureauIndividualProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//Query Changed on 1st Nov
		String sQuery = "SELECT cifid,agreementid,loantype,loantype,custroletype,loan_start_date,loanmaturitydate,lastupdatedate ,totaloutstandingamt,totalloanamount,NextInstallmentAmt,paymentmode,totalnoofinstalments,remaininginstalments,totalloanamount, overdueamt,nofdayspmtdelay,monthsonbook,currentlycurrentflg,currmaxutil,DPD_30_in_last_6_months,DPD_60_in_last_18_months,propertyvalue,loan_disbursal_date,marketingcode,DPD_30_in_last_3_months,DPD_30_in_last_6_months,DPD_30_in_last_9_months,DPD_30_in_last_12_months,DPD_30_in_last_18_months,DPD_30_in_last_24_months,DPD_60_in_last_3_months,DPD_60_in_last_6_months,DPD_60_in_last_9_months,DPD_60_in_last_12_months,DPD_60_in_last_18_months,DPD_60_in_last_24_months,DPD_90_in_last_3_months,DPD_90_in_last_6_months,DPD_90_in_last_9_months,DPD_90_in_last_12_months,DPD_90_in_last_18_months,DPD_90_in_last_24_months,DPD_120_in_last_3_months,DPD_120_in_last_6_months,DPD_120_in_last_9_months,DPD_120_in_last_12_months,DPD_120_in_last_18_months,DPD_120_in_last_24_months,DPD_150_in_last_3_months,DPD_150_in_last_6_months,DPD_150_in_last_9_months,DPD_150_in_last_12_months,DPD_150_in_last_18_months,DPD_150_in_last_24_months,DPD_180_in_last_3_months,DPD_180_in_last_6_months,DPD_180_in_last_9_months,DPD_180_in_last_12_months,DPD_180_in_last_24_months,'' as col1,isnull(Consider_For_Obligations,'true'),LoanStat,'LOANS',writeoffStat,writeoffstatdt,lastrepmtdt,limit_increase,PartSettlementDetails,SchemeCardProd as SchemeCardProduct,General_Status,Internal_WriteOff_Check,AmountPaidInLst6Mnths,InterestRate,LoanApprovedDate FROM ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) WHERE Child_wi = '"+formObject.getWFWorkitemName()+"' and LoanStat not in ('Pipeline','CAS-Pipeline') union select CifId,CardEmbossNum,CardType,CardType,CustRoleType,'' as col6,'' as col7,'' as col8, OutstandingAmt,CreditLimit,case when SchemeCardProd like '%LOC%' then (select sum(cast(MonthlyAmount as float)) from ng_RLOS_CUSTEXPOSE_CardInstallmentDetails with (nolock) where child_wi ='"+formObject.getWFWorkitemName()+"'  and replace(CardNumber,'I','')=CardEmbossNum) else PaymentsAmount end,PaymentMode,'' as col13,case when SchemeCardProd like 'LOC%' then (select top 1 ISNULL((CAST(max(INSTALMENTpERIOD) AS INT)-CAST(min(rEMAININGemi) AS INT)),'') from ng_RLOS_CUSTEXPOSE_CardInstallmentDetails with (nolock) where child_wi = '"+formObject.getWFWorkitemName()+"' and replace(CardNumber,'I','')=CardEmbossNum) else ''end  as col14,CashLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,currentlycurrentflg,CurrMaxUtil,DPD_30_in_last_6_months,DPD_60_in_last_18_months,'' as col23,'' as col24,'' as col25,DPD_30_in_last_3_months,DPD_30_in_last_6_months,DPD_30_in_last_9_months,DPD_30_in_last_12_months,DPD_30_in_last_18_months,DPD_30_in_last_24_months,DPD_60_in_last_3_months,DPD_60_in_last_6_months,DPD_60_in_last_9_months,DPD_60_in_last_12_months,DPD_60_in_last_18_months,DPD_60_in_last_24_months,DPD_90_in_last_3_months,DPD_90_in_last_6_months,DPD_90_in_last_9_months,DPD_90_in_last_12_months,DPD_90_in_last_18_months,DPD_90_in_last_24_months,DPD_120_in_last_3_months,DPD_120_in_last_6_months,DPD_120_in_last_9_months,DPD_120_in_last_12_months,DPD_120_in_last_18_months,DPD_120_in_last_24_months,DPD_150_in_last_3_months,DPD_150_in_last_6_months,DPD_150_in_last_9_months,DPD_150_in_last_12_months,DPD_150_in_last_18_months,DPD_150_in_last_24_months,DPD_180_in_last_3_months,DPD_180_in_last_6_months,DPD_180_in_last_9_months,DPD_180_in_last_12_months,DPD_180_in_last_24_months,ExpiryDate,isnull(Consider_For_Obligations,'true'),CardStatus,'CARDS',writeoffStat,writeoffstatdt,'' as lastrepdate,limit_increase,'' as PartSettlementDetails,SchemeCardProd,General_Status,Internal_WriteOff_Check,AmountPaidInLst6Mnths,InterestRate,'' as LoanApprovedDate FROM ng_RLOS_CUSTEXPOSE_CardDetails with (nolock) where Child_wi = '"+formObject.getWFWorkitemName()+"'  and Request_Type In ('InternalExposure','CollectionsSummary') union select CifId,AcctId,'OverDraft' as loantype,'OverDraft' as loantype,CustRoleType,LimitSactionDate as loan_start_date,LimitExpiryDate as loanmaturitydate,'' as lastupdateddate,ClearBalanceAmount,SanctionLimit,'','','','',SanctionLimit,OverdueAmount,DaysPastDue,MonthsOnBook,IsCurrent,CurUtilRate,DPD30Last6Months,DPD60Last18Months,'',AccountOpenDate,'','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','',LimitExpiryDate,isNull(Consider_For_Obligations,'true'),AcctStat,'OVERDRAFT',WriteoffStat,WriteoffStatDt,LastRepmtDt,'','','','','','','','' as LoanApprovedDate  from ng_rlos_custexpose_acctdetails with (nolock) where Child_wi = '"+formObject.getWFWorkitemName()+"'   and ODType != ''";
		PersonalLoanS.mLogger.info("This is the query to test internalbureu"+sQuery);
		//Query Changed on 1st Nov
		//PersonalLoanS.mLogger.info("InternalBureauIndividualProducts sQuery"+sQuery+ "");
		String CountQuery ="select count(*) from ng_RLOS_CUSTEXPOSE_CardDetails with (nolock) where Child_wi = '"+formObject.getWFWorkitemName()+"' and cardStatus='A'";
		List<List<String>> CountXML = formObject.getDataFromDataSource(CountQuery);
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		//PersonalLoanS.mLogger.info("InternalBureauIndividualProducts list size"+OutputXML.size()+ "");
		//PersonalLoanS.mLogger.info("InternalBureauIndividualProducts list "+OutputXML+ "");
		String ReqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
		String mol_sal_var = "";
		//PersonalLoanS.mLogger.info("InternalBureauIndividualProducts outside cmplx_MOL_molsalvar "+formObject.getNGValue("cmplx_MOL_molsalvar")+ "");
		//Commented by aastha for Jira: in Sprint 4 batch 1
		if(formObject.getNGValue("cmplx_MOL_molsalvar")!= null){
			PersonalLoanS.mLogger.info("InternalBureauIndividualProducts  outside cmplx_MOL_molsalvar "+formObject.getNGValue("cmplx_MOL_molsalvar")+ "");
			mol_sal_var = formObject.getNGValue("cmplx_MOL_molsalvar");
		}

		//End commented by aastha
		try{
			for (int i = 0; i<OutputXML.size();i++){

				String cifId = "";
				String agreementId = "";
				String product_type = "";
				String loan_start_date = "";
				String loanmaturitydate = "";
				String loanapproveddate = "";
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
				String custroletype="";
				String Internal_WriteOff_Check = "";
				//added by aastha for Jira:PCAS 2145,1295 Sprint4 Iteration-1


				String EmployerType = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 5);
				//String EmployerType = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 6);
				//String EmployerType=formObject.getNGValue("cmplx_EmploymentDetails_employer_type");
				String Kompass=formObject.getNGValue("cmplx_EmploymentDetails_Kompass");
				String paid_installment="";
				//String Internal_WriteOff_Check="";
				String Type_of_OD="";
				String amt_paid_last6mnths="";
				String interest_rate="";
				//PersonalLoanS.mLogger.info("Inside for"+ "asdasdasd");
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
				}//addeby aastha for jira: PCAS 2145,1295 Sprint4, Iteration 1
				if (!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals(""))) {
				}
				if (!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals(""))) {
					custroletype=OutputXML.get(i).get(4).toString();
				}


				if(!(OutputXML.get(i).get(5) == null || "".equals(OutputXML.get(i).get(5))) ){
					loan_start_date = OutputXML.get(i).get(5);
				}
				if(OutputXML.get(i).get(6)!=null && !OutputXML.get(i).get(6).isEmpty() &&  !"".equals(OutputXML.get(i).get(6)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(6)) ){
					loanmaturitydate = OutputXML.get(i).get(6).toString();
				}
				if(OutputXML.get(i).get(7)!=null && !OutputXML.get(i).get(7).isEmpty() &&  !"".equals(OutputXML.get(i).get(7)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(7)) ){
					lastupdatedate = OutputXML.get(i).get(7).toString();
				}				
				if(OutputXML.get(i).get(8)!=null && !OutputXML.get(i).get(8).isEmpty() &&  !"".equals(OutputXML.get(i).get(8)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(8)) ){
					outstandingamt = OutputXML.get(i).get(8).toString();
				}
				if(!(OutputXML.get(i).get(9) == null || "".equals(OutputXML.get(i).get(9))) ){
					totalloanamount = OutputXML.get(i).get(9).toString();
				}
				if(!(OutputXML.get(i).get(10) == null || "".equals(OutputXML.get(i).get(10))) ){
					Emi = OutputXML.get(i).get(10).toString();
				}				
				if(!(OutputXML.get(i).get(11) == null || "".equals(OutputXML.get(i).get(11))) ){
					paymentmode = OutputXML.get(i).get(11).toString();
				}
				if(OutputXML.get(i).get(12)!=null && !OutputXML.get(i).get(12).isEmpty() &&  !"".equals(OutputXML.get(i).get(12)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(12)) ){

					totalnoofinstalments = OutputXML.get(i).get(12).toString();
				}
				if (!(OutputXML.get(i).get(13) == null || OutputXML.get(i).get(13).equals(""))) {
					remaininginstalments = OutputXML.get(i).get(13).toString();
				}
				PersonalLoanS.mLogger.info("testing remaininginstallment"+remaininginstalments);
				//added by aastha for jira: PCAS-2145,1295 Sprint 4, Iteration 1				
				if (!(OutputXML.get(i).get(14) == null || OutputXML.get(i).get(14).equals(""))) {
				}

				if(!(OutputXML.get(i).get(15) == null || "".equals(OutputXML.get(i).get(15))) ){
					overdueamt = OutputXML.get(i).get(15).toString();
				}
				if(!(OutputXML.get(i).get(16) == null || "".equals(OutputXML.get(i).get(16))) ){
					nofdayspmtdelay = OutputXML.get(i).get(16).toString();
				}				
				if(!(OutputXML.get(i).get(17) == null || "".equals(OutputXML.get(i).get(17))) ){
					monthsonbook = OutputXML.get(i).get(17).toString();
				}
				if(!(OutputXML.get(i).get(18) == null || "".equals(OutputXML.get(i).get(18))) ){
					currentlycurrent = OutputXML.get(i).get(18).toString();
				}
				if(!(OutputXML.get(i).get(19) == null || "".equals(OutputXML.get(i).get(19))) ){
					currmaxutil = OutputXML.get(i).get(19).toString();
				}				
				if(!(OutputXML.get(i).get(20) == null || "".equals(OutputXML.get(i).get(20))) ){
					DPD_30_in_last_6_months = OutputXML.get(i).get(20).toString();
				}
				if(!(OutputXML.get(i).get(21) == null || "".equals(OutputXML.get(i).get(21))) ){
					DPD_60_in_last_18_months = OutputXML.get(i).get(21).toString();
				}
				if(!(OutputXML.get(i).get(22) == null || "".equals(OutputXML.get(i).get(22))) ){
					propertyvalue = OutputXML.get(i).get(22).toString();
				}				
				if(!(OutputXML.get(i).get(23) == null || "".equals(OutputXML.get(i).get(23))) ){
					loan_disbursal_date = OutputXML.get(i).get(23).toString();
				}
				if(!(OutputXML.get(i).get(24) == null || "".equals(OutputXML.get(i).get(24))) ){
					marketingcode = OutputXML.get(i).get(24).toString();
				}
				if(!(OutputXML.get(i).get(25) == null || "".equals(OutputXML.get(i).get(25))) ){
					DPD_30_in_last_3_months = OutputXML.get(i).get(25).toString();
				}				
				if (!(OutputXML.get(i).get(26) == null || OutputXML.get(i).get(26).equals(""))) {
					DPD_30_in_last_6_months = OutputXML.get(i).get(26)	.toString();
				}
				if (!(OutputXML.get(i).get(27) == null || OutputXML.get(i).get(27).equals(""))) {
					DPD_30_in_last_9_months = OutputXML.get(i).get(27)	.toString();
				}
				if (!(OutputXML.get(i).get(28) == null || OutputXML.get(i).get(28).equals(""))) {
					DPD_30_in_last_12_months = OutputXML.get(i).get(28)	.toString();
				}
				if (!(OutputXML.get(i).get(29) == null || OutputXML.get(i).get(29).equals(""))) {
					DPD_30_in_last_18_months = OutputXML.get(i).get(29)	.toString();
				}
				if (!(OutputXML.get(i).get(30) == null || OutputXML.get(i).get(30).equals(""))) {
					DPD_30_in_last_24_months = OutputXML.get(i).get(30)	.toString();
				}
				if (!(OutputXML.get(i).get(31) == null || OutputXML.get(i).get(31).equals(""))) {
					DPD_60_in_last_3_months = OutputXML.get(i).get(31)	.toString();
				}
				if (!(OutputXML.get(i).get(32) == null || OutputXML.get(i).get(32).equals(""))) {
					DPD_60_in_last_6_months = OutputXML.get(i).get(32)	.toString();
				}
				if (!(OutputXML.get(i).get(33) == null || OutputXML.get(i).get(33).equals(""))) {
					DPD_60_in_last_9_months = OutputXML.get(i).get(33)	.toString();
				}
				if (!(OutputXML.get(i).get(34) == null || OutputXML.get(i).get(34).equals(""))) {
					DPD_60_in_last_12_months = OutputXML.get(i).get(34)	.toString();
				}
				if (!(OutputXML.get(i).get(35) == null || OutputXML.get(i).get(35).equals(""))) {
					DPD_60_in_last_18_months = OutputXML.get(i).get(35)	.toString();
				}
				if (!(OutputXML.get(i).get(36) == null || OutputXML.get(i).get(36).equals(""))) {
					DPD_60_in_last_24_months = OutputXML.get(i).get(36)	.toString();
				}
				if (!(OutputXML.get(i).get(37) == null || OutputXML.get(i).get(37).equals(""))) {
					DPD_90_in_last_3_months = OutputXML.get(i).get(37)	.toString();
				}
				if (!(OutputXML.get(i).get(38) == null || OutputXML.get(i).get(38).equals(""))) {
					DPD_90_in_last_6_months = OutputXML.get(i).get(38)	.toString();
				}
				if (!(OutputXML.get(i).get(39) == null || OutputXML.get(i).get(39).equals(""))) {
					DPD_90_in_last_9_months = OutputXML.get(i).get(39)	.toString();
				}
				if (!(OutputXML.get(i).get(40) == null || OutputXML.get(i).get(40).equals(""))) {
					DPD_90_in_last_12_months = OutputXML.get(i).get(40)	.toString();
				}
				if (!(OutputXML.get(i).get(41) == null || OutputXML.get(i).get(41).equals(""))) {
					DPD_90_in_last_18_months = OutputXML.get(i).get(41)	.toString();
				}
				if (!(OutputXML.get(i).get(42) == null || OutputXML.get(i).get(42).equals(""))) {
					DPD_90_in_last_24_months = OutputXML.get(i).get(42)	.toString();
				}
				if (!(OutputXML.get(i).get(43) == null || OutputXML.get(i).get(43).equals(""))) {
					DPD_120_in_last_3_months = OutputXML.get(i).get(43)	.toString();
				}
				if (!(OutputXML.get(i).get(44) == null || OutputXML.get(i).get(44).equals(""))) {
					DPD_120_in_last_6_months = OutputXML.get(i).get(44)	.toString();
				}
				if (!(OutputXML.get(i).get(45) == null || OutputXML.get(i).get(45).equals(""))) {
					DPD_120_in_last_9_months = OutputXML.get(i).get(45)	.toString();
				}
				if (!(OutputXML.get(i).get(46) == null || OutputXML.get(i).get(46).equals(""))) {
					DPD_120_in_last_12_months = OutputXML.get(i).get(46)	.toString();
				}
				if (!(OutputXML.get(i).get(47) == null || OutputXML.get(i).get(47).equals(""))) {
					DPD_120_in_last_18_months = OutputXML.get(i).get(47)	.toString();
				}
				if (!(OutputXML.get(i).get(48) == null || OutputXML.get(i).get(48).equals(""))) {
					DPD_120_in_last_24_months = OutputXML.get(i).get(48)	.toString();
				}
				if (!(OutputXML.get(i).get(49) == null || OutputXML.get(i).get(49).equals(""))) {
					DPD_150_in_last_3_months = OutputXML.get(i).get(49)	.toString();
				}
				if (!(OutputXML.get(i).get(50) == null || OutputXML.get(i).get(50).equals(""))) {
					DPD_150_in_last_6_months = OutputXML.get(i).get(50)	.toString();
				}
				if (!(OutputXML.get(i).get(51) == null || OutputXML.get(i).get(51).equals(""))) {
					DPD_150_in_last_9_months = OutputXML.get(i).get(51)	.toString();
				}
				if (!(OutputXML.get(i).get(52) == null || OutputXML.get(i).get(52).equals(""))) {
					DPD_150_in_last_12_months = OutputXML.get(i).get(52)	.toString();
				}
				if (!(OutputXML.get(i).get(53) == null || OutputXML.get(i).get(53).equals(""))) {
					DPD_150_in_last_18_months = OutputXML.get(i).get(53)	.toString();
				}
				if (!(OutputXML.get(i).get(54) == null || OutputXML.get(i).get(54).equals(""))) {
					DPD_150_in_last_24_months = OutputXML.get(i).get(54)	.toString();
				}
				if (!(OutputXML.get(i).get(55) == null || OutputXML.get(i).get(55).equals(""))) {
					DPD_180_in_last_3_months = OutputXML.get(i).get(55)	.toString();
				}
				if (!(OutputXML.get(i).get(56) == null || OutputXML.get(i).get(56).equals(""))) {
					DPD_180_in_last_6_months = OutputXML.get(i).get(56)	.toString();
				}
				if (!(OutputXML.get(i).get(57) == null || OutputXML.get(i).get(57).equals(""))) {
					DPD_180_in_last_9_months = OutputXML.get(i).get(57)	.toString();
				}
				if (!(OutputXML.get(i).get(58) == null || OutputXML.get(i).get(58).equals(""))) {
					DPD_180_in_last_12_months = OutputXML.get(i).get(58)	.toString();
				}
				if (!(OutputXML.get(i).get(59) == null || OutputXML.get(i).get(59).equals(""))) {
					DPD_180_in_last_24_months = OutputXML.get(i).get(59)	.toString();
				}
				if (!(OutputXML.get(i).get(60) == null || OutputXML.get(i).get(60).equals(""))) {
					CardExpiryDate = OutputXML.get(i).get(60).toString();
				}

				if (!(OutputXML.get(i).get(61) == null || OutputXML.get(i).get(61).equals(""))) {
					Consider_For_Obligations = OutputXML.get(i).get(61).toString();
					if ("true".equalsIgnoreCase(Consider_For_Obligations)) 
					{
						Consider_For_Obligations = "Y";
					} 
					else 
					{
						Consider_For_Obligations = "N";
					}
				}

				if (!(OutputXML.get(i).get(62) == null || OutputXML.get(i).get(62).equals(""))) {
					phase = OutputXML.get(i).get(62).toString();
					if (phase.startsWith("C")){
						phase="C";
					}
					else {
						phase="A";
					}
				}
				if(!(OutputXML.get(i).get(64) == null || "".equals(OutputXML.get(i).get(64))) ){
					writeoffStat = OutputXML.get(i).get(64).toString();
				}
				if(!(OutputXML.get(i).get(65) == null || "".equals(OutputXML.get(i).get(65))) ){
					writeoffstatdt = OutputXML.get(i).get(65).toString();
				}
				if(!(OutputXML.get(i).get(66) == null || "".equals(OutputXML.get(i).get(66))) ){
					lastrepmtdt = OutputXML.get(i).get(66).toString();
				}
				if(!(OutputXML.get(i).get(67) == null || "".equals(OutputXML.get(i).get(67))) ){
					Limit_increase = OutputXML.get(i).get(67).toString();
					if ("false".equalsIgnoreCase(Limit_increase)){
						Limit_increase="N";
					}
					else{
						Limit_increase="Y";
					}
				}
				if(!(OutputXML.get(i).get(68) == null || "".equals(OutputXML.get(i).get(68))) ){
					//Commented by aastha for Jira-PCAS 2145,1295 Sprint 4 Iteration 1
					//part_settlement_date = OutputXML.get(i).get(67);
					String abc=OutputXML.get(i).get(68);
					abc=abc.substring(0, 10)+"split"+abc.substring(10,abc.length() );
					String abcsa[] = abc.split("split");
					part_settlement_date = abcsa[0];
					part_settlement_amount = abcsa[1];
				}
				if(!(OutputXML.get(i).get(69) == null || "".equals(OutputXML.get(i).get(69))) ){
					SchemeCardProduct = OutputXML.get(i).get(69).toString();
				}
				if(!(OutputXML.get(i).get(70) == null || "".equals(OutputXML.get(i).get(70))) ){
					General_Status = OutputXML.get(i).get(70).toString();
				}
				if(!(OutputXML.get(i).get(71) == null || "".equals(OutputXML.get(i).get(71))) ){
					Internal_WriteOff_Check = OutputXML.get(i).get(71).toString();
				}
				if (!(OutputXML.get(i).get(72) == null || OutputXML.get(i).get(72).equals(""))) {
					amt_paid_last6mnths = OutputXML.get(i).get(72).toString();
				}
				//below tags added by saurabh on 11th July.
				if (!(OutputXML.get(i).get(73) == null || OutputXML.get(i).get(73).equals(""))) {
					interest_rate = OutputXML.get(i).get(73).toString();
				}
				if(!(OutputXML.get(i).get(74) == null || OutputXML.get(i).get(74).equals(""))) {
					loanapproveddate = OutputXML.get(i).get(74).toString();
				}//Added by rajan for PCASP-32


				String sQueryCombinedLimit = "select Distinct(COMBINEDLIMIT_ELIGIBILITY) from ng_master_cardProduct with(nolock) where code='"+SchemeCardProduct+"'";
				List<List<String>> sQueryCombinedLimitXML = formObject.getNGDataFromDataCache(sQueryCombinedLimit);
				try{
					if(sQueryCombinedLimitXML!=null && sQueryCombinedLimitXML.size()>0 && sQueryCombinedLimitXML.get(0)!=null){
						Combined_Limit=sQueryCombinedLimitXML.get(0).get(0).equalsIgnoreCase("1")?"Y":"N";
					}
				}
				catch(Exception e){
					PersonalLoanS.mLogger.info("Exception occured at sQueryCombinedLimit for"+sQueryCombinedLimit);

				}
				String sQuerySecuredCard = "select count(*) from ng_master_cardProduct with (nolock) where code='" + SchemeCardProduct + "'  and subproduct='SEC'";
				List<List<String>> sQuerySecuredCardXML = formObject.getNGDataFromDataCache(sQuerySecuredCard);
				try{
					if(sQuerySecuredCardXML!=null && sQuerySecuredCardXML.size()>0 && sQuerySecuredCardXML.get(0)!=null){
						SecuredCard=sQuerySecuredCardXML.get(0).get(0).equalsIgnoreCase("0")?"N":"Y";
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
							//added by aastha for Jira: PCAS-2145,1295 Sprint 4, Iteration 1
							if ((SchemeCardProduct.toUpperCase()).contains("AMAL PERSONAL FINANCE")){
								add_xml_str = add_xml_str + "<contract_type>IPL</contract_type>";
							}
							else if ((SchemeCardProduct.toUpperCase()).contains("AMAL AUTO FINANCE")){
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
					//added by aastha for Jira: PCAS-2145,1295 Sprint 4, Iteration 1
					add_xml_str = add_xml_str+ "<role_of_customer>"+custroletype+"</role_of_customer>";
					//Commented by aastha for Jira: PCAS-2145,1295 Sprint 4, Iteration 1
					//	add_xml_str = add_xml_str + "<role_of_customer>Primary</role_of_customer>"; 

					add_xml_str = add_xml_str + "<start_date>"+loan_start_date+"</start_date>"; 
					add_xml_str = add_xml_str + "<close_date>"+loanmaturitydate+"</close_date>"; 
					add_xml_str = add_xml_str + "<approved_date>"+ loanapproveddate + "</approved_date>";
					add_xml_str = add_xml_str + "<date_last_updated>"+ lastupdatedate + "</date_last_updated>";
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
					if ("cards".equalsIgnoreCase(OutputXML.get(i).get(63))||"OVERDRAFT".equalsIgnoreCase(OutputXML.get(i).get(63))){

						add_xml_str = add_xml_str + "<credit_limit>"+totalloanamount+"</credit_limit>"; 
					}
					add_xml_str = add_xml_str + "<overdue_amount>"+overdueamt+"</overdue_amount>"; 
					add_xml_str = add_xml_str + "<no_of_days_payment_delay>"+nofdayspmtdelay+"</no_of_days_payment_delay>"; 
					add_xml_str = add_xml_str + "<mob>"+monthsonbook+"</mob>"; 
					add_xml_str = add_xml_str + "<last_repayment_date>"+lastrepmtdt+"</last_repayment_date>"; 
					add_xml_str = add_xml_str + "<currently_current>"+currentlycurrent+"</currently_current>"; 
					//add_xml_str = add_xml_str + "<current_utilization>"+currmaxutil+"</current_utilization>"; 
					add_xml_str = add_xml_str + "<dpd_30_last_6_mon>"+DPD_30_in_last_6_months+"</dpd_30_last_6_mon>"; 
					add_xml_str = add_xml_str + "<dpd_60p_in_last_18_mon>"+DPD_60_in_last_18_months+"</dpd_60p_in_last_18_mon>"; 

					if ("cards".equalsIgnoreCase(OutputXML.get(i).get(63))){
						add_xml_str = add_xml_str + "<card_product>"+SchemeCardProduct+"</card_product>";
					}
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

					//Commented by aastha for Jira: PCAS-2145,1295 Sprint 4, Iteration 1
					//	add_xml_str = add_xml_str + "<role>Primary</role>"; 
					add_xml_str = add_xml_str + "<role>"+custroletype+"</role>";
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
					add_xml_str = add_xml_str + "<dpd_60p_in_last_12_mon>"+ DPD_60_in_last_12_months+ "</dpd_60p_in_last_12_mon>";
					add_xml_str = add_xml_str + "<dpd_60_in_last_18mon>"+ DPD_60_in_last_18_months+ "</dpd_60_in_last_18mon>";
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
					if (Kompass != null) 
					{
						if ("true".equalsIgnoreCase(Kompass))
						{	
							add_xml_str = add_xml_str + "<kompass>" + "Y"+ "</kompass>";
						} else 
						{	
							add_xml_str = add_xml_str + "<kompass>"+"N"+"</kompass>";
						}
					}
					add_xml_str = add_xml_str + "<employer_type>"+EmployerType+"</employer_type>"; 


					if (totalnoofinstalments != null	&& remaininginstalments != null	&& !totalnoofinstalments.equals("")	&& !remaininginstalments.equals("")) 
					{
						paid_installment= Integer.toString(Integer.parseInt(totalnoofinstalments) -Integer.parseInt(remaininginstalments));
						PersonalLoanS.mLogger.info("Inside paid_installment"+ "paid_installment"+paid_installment);

					}
					/*if ("Credit Card".equalsIgnoreCase(ReqProd)){

						add_xml_str = add_xml_str + "<no_of_paid_installment>"+paid_installment+"</no_of_paid_installment><company_flag>N</company_flag></InternalBureauIndividualProducts>";
					}
					else
					{*/
					add_xml_str = add_xml_str+ "<no_of_paid_installment>"+ paid_installment+ "</no_of_paid_installment><write_off_amount>"+ Internal_WriteOff_Check+ "</write_off_amount><company_flag>N</company_flag><type_of_od>"+ ""+ "</type_of_od><amt_paid_last6mnths>"+ amt_paid_last6mnths+ "</amt_paid_last6mnths><topup_indicator>"+Limit_increase+"</topup_indicator><interest_rate>"+interest_rate+"</interest_rate></InternalBureauIndividualProducts>";

					//}
				}	

			}
		}
		catch(Exception e){
			//Changes done for code optimization 25/07
			//new PersonalLoanSCommonCode();
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
		String sQuery = "SELECT cifid,LoanType,custroletype,lastupdatedate,totalamount,totalnoofinstalments,totalloanamount,agreementId,NoOfDaysInPipeline,case when Consider_For_Obligations is null or Consider_For_Obligations='True' then 'Y' else 'N' end as Consider_For_Obligations,NextInstallmentAmt FROM ng_RLOS_CUSTEXPOSE_LoanDetails  with (nolock) where Wi_Name = '"
				+ formObject.getWFWorkitemName()
				+ "' and  LoanStat = 'CAS-Pipeline'";
		PersonalLoanS.mLogger.info("InternalBureauPipelineProducts sQuery"+sQuery+ "");
		String  add_xml_str ="";
		try{
			List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
			//PersonalLoanS.mLogger.info("InternalBureauPipelineProducts list size"+OutputXML.size()+ "");

			for (int i = 0; i<OutputXML.size();i++){

				String cifId = "";
				String Product = "";
				String lastUpdateDate = ""; 
				String TotAmount = "";
				String TotNoOfInstlmnt = "";
				String TotLoanAmt = "";
				String agreementId = "";
				String NoOfDaysInPipeline="";
				String ConsiderForOblig = "";
				String EMI = "";
				if(!(OutputXML.get(i).get(0) == null || "".equals(OutputXML.get(i).get(0))) ){
					cifId = OutputXML.get(i).get(0).toString();
				}
				if(!(OutputXML.get(i).get(1) == null || "".equals(OutputXML.get(i).get(1))) ){
					Product = OutputXML.get(i).get(1).toString();
				}
				//added by aastha for Jira: PCAS-2145,1295 Sprint 4, Iteration 1
				if (!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2).equals(""))) {
				}
				//changes Done for Jira:PCAS-2145,1295 Sprint4, Iteration 1

				if(!(OutputXML.get(i).get(3) == null || "".equals(OutputXML.get(i).get(3))) ){
					lastUpdateDate = OutputXML.get(i).get(3).toString();
				}
				if(!(OutputXML.get(i).get(4) == null || "".equals(OutputXML.get(i).get(4))) ){
					TotAmount = OutputXML.get(i).get(4).toString();
				}
				if(!(OutputXML.get(i).get(5) == null || "".equals(OutputXML.get(i).get(5))) ){
					TotNoOfInstlmnt = OutputXML.get(i).get(5).toString();
				}
				if(!(OutputXML.get(i).get(6) == null || "".equals(OutputXML.get(i).get(6))) ){
					TotLoanAmt = OutputXML.get(i).get(6).toString();
				}
				if(!(OutputXML.get(i).get(7) == null || "".equals(OutputXML.get(i).get(7)))) {
					agreementId = OutputXML.get(i).get(7).toString();
				}
				if(!(OutputXML.get(i).get(8) == null || "".equalsIgnoreCase(OutputXML.get(i).get(8))) ){
					NoOfDaysInPipeline = OutputXML.get(i).get(8);
				}
				if(!(OutputXML.get(i).get(9) == null || "".equalsIgnoreCase(OutputXML.get(i).get(9))) ){
					ConsiderForOblig = OutputXML.get(i).get(9);
				}
				if(!(OutputXML.get(i).get(10) == null || "".equalsIgnoreCase(OutputXML.get(i).get(10))) ){
					EMI = OutputXML.get(i).get(10);
				}
				if(cifId!=null && !"".equalsIgnoreCase(cifId) && !"null".equalsIgnoreCase(cifId))
				{
					add_xml_str = add_xml_str + "<InternalBureauPipelineProducts>";
					add_xml_str = add_xml_str + "<applicant_id>"+cifId+"</applicant_id>";
					add_xml_str = add_xml_str + "<internal_bureau_pipeline_products_id>"+agreementId+"</internal_bureau_pipeline_products_id>";// to be 
					//populated 
					//later
					add_xml_str = add_xml_str + "<ppl_provider_no>RAKBANK</ppl_provider_no>";
					//add_xml_str = add_xml_str + "<ppl_product>"+Product+"</ppl_product>";
					add_xml_str = add_xml_str + "<ppl_type_of_contract>"+Product+"</ppl_type_of_contract>";
					add_xml_str = add_xml_str + "<ppl_phase>PIPELINE</ppl_phase>"; // to be populated later

					add_xml_str = add_xml_str + "<ppl_role>Primary</ppl_role>";
					add_xml_str = add_xml_str + "<ppl_date_of_last_update>"+lastUpdateDate+"</ppl_date_of_last_update>";
					add_xml_str = add_xml_str + "<ppl_total_amount>"+TotAmount+"</ppl_total_amount>";
					add_xml_str = add_xml_str + "<ppl_no_of_instalments>"+TotNoOfInstlmnt+"</ppl_no_of_instalments>";
					add_xml_str = add_xml_str + "<ppl_credit_limit>"+TotLoanAmt+"</ppl_credit_limit>";

					add_xml_str = add_xml_str + "<ppl_no_of_days_in_pipeline>"+NoOfDaysInPipeline+"</ppl_no_of_days_in_pipeline>";
					add_xml_str = add_xml_str + "<company_flag>N</company_flag>";
					add_xml_str = add_xml_str + "<ppl_consider_for_obligation>"+ConsiderForOblig+"</ppl_consider_for_obligation>";
					add_xml_str = add_xml_str + "<ppl_emi>"+EMI+"</ppl_emi>";
					add_xml_str = add_xml_str + "</InternalBureauPipelineProducts>"; // to be populated later
				}

			}
			PersonalLoanS.mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "	+ add_xml_str);
		}catch(Exception e){
			PersonalLoanS.mLogger.info("RLOSCommon"+ "Exception occurred in InternalBureauPipelineProducts()"+e.getMessage() + "\n Error: "  +  e.getMessage());
			PLCommon.printException(e);
		}
		return add_xml_str;
	}

	/*          Function Header:

	 **********************************************************************************
	 *

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Generate XML for ExternalBureauData

	 ***********************************************************************************  */


	public String ExternalBureauData()
	{
		PersonalLoanS.mLogger.info("RLOSCommon java file"+ "inside ExternalBureauData : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//			String sQuery = "select  CifId,fullnm,TotalOutstanding,TotalOverdue,NoOfContracts,Total_Exposure,WorstCurrentPaymentDelay,Worst_PaymentDelay_Last24Months,Worst_Status_Last24Months,Nof_Records,NoOf_Cheque_Return_Last3,Nof_DDES_Return_Last3Months,Nof_Cheque_Return_Last6,DPD30_Last6Months,(select max(ExternalWriteOffCheck) ExternalWriteOffCheck from ((select convert(int,isNULL(ExternalWriteOffCheck,0)) ExternalWriteOffCheck  from ng_rlos_cust_extexpo_CardDetails with(nolock) where Child_wi  = '"+formObject.getWFWorkitemName()+"' union select convert(int,isNULL(ExternalWriteOffCheck,0))ExternalWriteOffCheck    from ng_rlos_cust_extexpo_LoanDetails  where Child_wi  = '"+formObject.getWFWorkitemName()+"')) as ExternalWriteOffCheck),(select count(*) from (select DisputeAlert from ng_rlos_cust_extexpo_LoanDetails with(nolock) where Child_wi = '"+formObject.getWFWorkitemName()+"' and DisputeAlert='1' union  select DisputeAlert from ng_rlos_cust_extexpo_CardDetails with(nolock) where Child_wi = '"+formObject.getWFWorkitemName()+"' and DisputeAlert='1') as tempTable) from NG_rlos_custexpose_Derived with(nolock) where Child_wi  = '"+formObject.getWFWorkitemName()+"' and Request_type= 'ExternalExposure'";
		//changed by shweta
		/*String sQuery = "select CifId,AgreementId,LoanType,ProviderNo,LoanStat,CustRoleType,LoanApprovedDate,LoanMaturityDate,OutstandingAmt,TotalAmt,PaymentsAmt,TotalNoOfInstalments,RemainingInstalments,WriteoffStat,WriteoffStatDt,CreditLimit,OverdueAmt,NofDaysPmtDelay,MonthsOnBook,lastrepmtdt,IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last12Months,AECBHistMonthCnt,DPD5_Last3Months,'' as qc_Amnt,'' as Qc_emi,'' as Cac_indicator,Take_Over_Indicator,Consider_For_Obligations,case when IsDuplicate= '1' then 'Y' else 'N' end,avg_utilization,DPD5_Last12Months,DPD60Plus_Last12Months from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Child_wi= '"+ formObject.getWFWorkitemName()
			+ "'  and LoanStat != 'Pipeline'  and ProviderNo != 'B01' union select CifId,CardEmbossNum,CardType,ProviderNo,CardStatus,CustRoleType,StartDate,ClosedDate,CurrentBalance,'' as col6,PaymentsAmount,NoOfInstallments,'' as col5,WriteoffStat,WriteoffStatDt,CashLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,lastrepmtdt,IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last12Months,AECBHistMonthCnt,DPD5_Last3Months,qc_amt,qc_emi,CAC_Indicator,Take_Over_Indicator,Consider_For_Obligations,case when IsDuplicate= '1' then 'Y' else 'N' end,avg_utilization,DPD5_Last12Months,DPD60Plus_Last12Months from ng_rlos_cust_extexpo_CardDetails with (nolock) where Child_wi  =  '"
			+ formObject.getWFWorkitemName()+ "' and cardstatus != 'Pipeline'  and ProviderNo != 'B01' union select CifId,AcctId,AcctType,ProviderNo,AcctStat,CustRoleType,StartDate,ClosedDate,OutStandingBalance,TotalAmount,PaymentsAmount,'','',WriteoffStat,WriteoffStatDt,CreditLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,'',IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last12Months,AECBHistMonthCnt,DPD5_Last3Months,'','','','',isnull(Consider_For_Obligations,'true'),case when IsDuplicate= '1' then 'Y' else 'N' end,'',DPD5_Last12Months,DPD60Plus_Last12Months from ng_rlos_cust_extexpo_AccountDetails with (nolock)  where child_wi  =  '"+formObject.getWFWorkitemName()+"' union"
			+" select CifId,ServiceID,ServiceType,ProviderNo,ServiceStat,CustRoleType,SubscriptionDt,SvcExpDt,'','','','','',WriteoffStat,WriteoffStatDt,'',OverDueAmount,NofDaysPmtDelay,MonthsOnBook,'',IsCurrent,CurUtilRate,'',DPD30_Last6Months,AECBHistMonthCnt,DPD5_Last3Months,'','','','',isnull(Consider_For_Obligations,'true'),case when IsDuplicate= '1' then 'Y' else 'N' end,'',DPD5_Last12Months,DPD60Plus_Last12Months from ng_rlos_cust_extexpo_ServicesDetails with (nolock)  where ServiceStat='Active' and child_wi  =  '"+formObject.getWFWorkitemName()+"'";
		 */
		//below query is changed by shivang for PCASI-2875
		//String sQuery = "select  CifId,fullnm,TotalOutstanding,TotalOverdue,NoOfContracts,Total_Exposure,WorstCurrentPaymentDelay,Worst_PaymentDelay_Last24Months,Worst_Status_Last24Months,Nof_Records,NoOf_Cheque_Return_Last3,Nof_DDES_Return_Last3Months,Nof_Cheque_Return_Last6,DPD30_Last6Months,(select max(ExternalWriteOffCheck) ExternalWriteOffCheck from ((select convert(int,isNULL(ExternalWriteOffCheck,0)) ExternalWriteOffCheck  from ng_rlos_cust_extexpo_CardDetails with(nolock) where Child_wi  = '"+formObject.getWFWorkitemName()+"' and ProviderNo!='B01'  union select convert(int,isNULL(ExternalWriteOffCheck,0))ExternalWriteOffCheck    from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Child_wi  = '"+formObject.getWFWorkitemName()+"' and ProviderNo!='B01')) as ExternalWriteOffCheck),(select count(*) from (select DisputeAlert from ng_rlos_cust_extexpo_LoanDetails with(nolock) where Child_wi = '"+formObject.getWFWorkitemName()+"' and DisputeAlert='1' union  select DisputeAlert from ng_rlos_cust_extexpo_CardDetails with(nolock) where Child_wi = '"+formObject.getWFWorkitemName()+"' and DisputeAlert='1') as tempTable)  from NG_rlos_custexpose_Derived with (nolock) where Child_wi  = '"+ formObject.getWFWorkitemName()+ "' and Request_type= 'ExternalExposure'";
		String sQuery = "select  CifId,fullnm,TotalOutstanding,TotalOverdue,NoOfContracts,Total_Exposure,WorstCurrentPaymentDelay,Worst_PaymentDelay_Last24Months,Worst_Status_Last24Months,Nof_Records,NoOf_Cheque_Return_Last3,Nof_DDES_Return_Last3Months,Nof_Cheque_Return_Last6,DPD30_Last6Months,(select max(ExternalWriteOffCheck) ExternalWriteOffCheck from ((select convert(int,isNULL(ExternalWriteOffCheck,0)) ExternalWriteOffCheck  from ng_rlos_cust_extexpo_CardDetails with(nolock) where Child_wi  = '"+formObject.getWFWorkitemName()+"' and ProviderNo!='B01'  union all select convert(int,isNULL(ExternalWriteOffCheck,0))ExternalWriteOffCheck    from ng_rlos_cust_extexpo_LoanDetails where Child_wi  = '"+formObject.getWFWorkitemName()+"' and ProviderNo!='B01' union all select convert(int,isNULL(ExternalWriteOffCheck,0))ExternalWriteOffCheck    from ng_rlos_cust_extexpo_AccountDetails where Child_wi  = '"+formObject.getWFWorkitemName()+"' and ProviderNo!='B01')) as ExternalWriteOffCheck),(select count(*) from (select DisputeAlert from ng_rlos_cust_extexpo_LoanDetails with(nolock) where Child_wi = '"+formObject.getWFWorkitemName()+"' and DisputeAlert='1' union  select DisputeAlert from ng_rlos_cust_extexpo_CardDetails with(nolock) where Child_wi = '"+formObject.getWFWorkitemName()+"' and DisputeAlert='1') as tempTable)  from NG_rlos_custexpose_Derived with (nolock) where Child_wi  = '"+formObject.getWFWorkitemName()+"' and Request_type= 'ExternalExposure'";
		//commented by aastha for jira: PCAS-2145,1295 sprint 4 iteration 1
		PersonalLoanS.mLogger.info("ExternalBureauData sQuery"+sQuery+ "");
		//Comment End
		//added by aastha for Jira: PCAS-2145,1295 Sprint 4, Iteration 1
		//changed by shweta
		String Wi_Name=formObject.getWFWorkitemName();
		String AecbHistQuery = "select isnull(max(AECBHistMonthCnt),0) as AECBHistMonthCnt from ( select MAX(cast(isnull(AECBHistMonthCnt,'0') as int)) as AECBHistMonthCnt  from ng_rlos_cust_extexpo_CardDetails with (nolock) where  Child_wi  = '"+ Wi_Name + "' union select Max(cast(isnull(AECBHistMonthCnt,'0') as int)) as AECBHistMonthCnt from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Child_wi  = '"+ Wi_Name + "') as ext_expo";

		//End changes for Jira:PCAS-2145,1295 Sprint4, Iteration 1
		String  add_xml_str ="";
		try{
			List<List<String>> OutputXML = formObject.getNGDataFromDataCache(sQuery);
			//PersonalLoanS.mLogger.info("ExternalBureauData list size"+OutputXML.size()+ "");
			List<List<String>> AecbHistQueryData = formObject.getNGDataFromDataCache(AecbHistQuery);

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
				String aecb_score= formObject.getNGValue("cmplx_Liability_New_AECBScore");
				String range = formObject.getNGValue("cmplx_Liability_New_Range");
				String refNo = formObject.getNGValue("cmplx_Liability_New_ReferenceNo");
				PersonalLoanS.mLogger.info( "aecb_score :"+aecb_score+" range :: "+range+" refNo:: "+refNo);


				add_xml_str = add_xml_str + "<ExternalBureau>"; 
				add_xml_str = add_xml_str + "<applicant_id>"+CifId+"</applicant_id>";
				add_xml_str = add_xml_str + "<bureauone_ref_no>"+refNo+"</bureauone_ref_no>";
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
				add_xml_str = add_xml_str + "<aecb_score>"+aecb_score+"</aecb_score>";
				add_xml_str = add_xml_str + "<range>"+range+"</range>";
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
					String ExternalWriteOffCheck = "";
					String dispute_alert="";
					String aecb_score= formObject.getNGValue("cmplx_Liability_New_AECBScore");
					String range = formObject.getNGValue("cmplx_Liability_New_Range");
					String refNo = formObject.getNGValue("cmplx_Liability_New_ReferenceNo");
					if(!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals("")) ){
						fullnm = OutputXML.get(i).get(1).toString();
					}				
					if(!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2).equals("")) ){
						TotalOutstanding = OutputXML.get(i).get(2).toString();

					}
					if(!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals("")) ){
						TotalOverdue = OutputXML.get(i).get(3).toString();
					}
					if(!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals("")) ){
						NoOfContracts = OutputXML.get(i).get(4).toString();
					}				
					if(!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5).equals("")) ){
						Total_Exposure = OutputXML.get(i).get(5).toString();
					}
					if(OutputXML.get(i).get(6)!=null && !OutputXML.get(i).get(6).isEmpty() &&  !OutputXML.get(i).get(6).equals("") && !"null".equalsIgnoreCase(OutputXML.get(i).get(6)) ){
						WorstCurrentPaymentDelay = OutputXML.get(i).get(6).toString();
					}
					if(OutputXML.get(i).get(7)!=null && !OutputXML.get(i).get(7).isEmpty() &&  !OutputXML.get(i).get(7).equals("") && !"null".equalsIgnoreCase(OutputXML.get(i).get(7)) ){
						Worst_PaymentDelay_Last24Months = OutputXML.get(i).get(7).toString();
					}				
					if(OutputXML.get(i).get(8)!=null && !OutputXML.get(i).get(8).isEmpty() &&  !OutputXML.get(i).get(8).equals("") && !"null".equalsIgnoreCase(OutputXML.get(i).get(8)) ){
						Worst_Status_Last24Months = OutputXML.get(i).get(8).toString();
					}
					if(!(OutputXML.get(i).get(9) == null || OutputXML.get(i).get(9).equals("")) ){
						Nof_Records = OutputXML.get(i).get(9).toString();
					}
					if(!(OutputXML.get(i).get(10) == null || OutputXML.get(i).get(10).equals("")) ){
						NoOf_Cheque_Return_Last3 = OutputXML.get(i).get(10).toString();
					}				
					if(!(OutputXML.get(i).get(11) == null || OutputXML.get(i).get(11).equals("")) ){
						Nof_DDES_Return_Last3Months = OutputXML.get(i).get(11).toString();
					}
					if(OutputXML.get(i).get(12)!=null && !OutputXML.get(i).get(12).isEmpty() &&  !OutputXML.get(i).get(12).equals("") && !"null".equalsIgnoreCase(OutputXML.get(i).get(12)) ){
						//SKLogger.writeLog("Inside for", "asdasdasd"+OutputXML.get(i).get(12));
						Nof_Cheque_Return_Last6 = OutputXML.get(i).get(12).toString();
					}
					if(!(OutputXML.get(i).get(13) == null || OutputXML.get(i).get(13).equals("")) ){
						DPD30_Last6Months = OutputXML.get(i).get(13).toString();
					}
					if(!(OutputXML.get(i).get(14) == null || "".equalsIgnoreCase(OutputXML.get(i).get(14))) && !"0".equalsIgnoreCase(OutputXML.get(i).get(14)) ){
						ExternalWriteOffCheck = OutputXML.get(i).get(14);
					}
					if(!(OutputXML.get(i).get(15) == null || "".equalsIgnoreCase(OutputXML.get(i).get(15))) ){
						dispute_alert = OutputXML.get(i).get(15);
						if(Integer.parseInt(dispute_alert)>0){
							dispute_alert="Y";
						}
						else{
							dispute_alert="N";
						}
					}

					add_xml_str = add_xml_str + "<ExternalBureau>"; 
					add_xml_str = add_xml_str + "<applicant_id>"+CifId+"</applicant_id>";
					add_xml_str = add_xml_str + "<bureauone_ref_no>"+refNo+"</bureauone_ref_no>";
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
					add_xml_str = add_xml_str + "<aecb_score>"+aecb_score+"</aecb_score>";
					add_xml_str = add_xml_str + "<range>"+range+"</range>";
					add_xml_str = add_xml_str + "<company_flag>N</company_flag>";
					add_xml_str = add_xml_str + "<dispute_alert>"+dispute_alert+"</dispute_alert></ExternalBureau>";


				}
				PersonalLoanS.mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "+ add_xml_str);
				return add_xml_str;
			}
		}
		catch(Exception e){//new PersonalLoanSCommonCode();

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
		String sQuery = "SELECT cifid,number,amount,reasoncode,returndate,providerno FROM ng_rlos_cust_extexpo_ChequeDetails  with (nolock) where child_wi = '"
				+ formObject.getWFWorkitemName()
				+ "' and Request_Type = 'ExternalExposure'";
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
				chqNo = OutputXML.get(i).get(1).toString();
			}
			if(!(OutputXML.get(i).get(2) == null || "".equals(OutputXML.get(i).get(2))) ){
				Amount = OutputXML.get(i).get(2).toString();
			}
			if(!(OutputXML.get(i).get(3) == null || "".equals(OutputXML.get(i).get(3))) ){
				Reason = OutputXML.get(i).get(3).toString();
			}
			if(!(OutputXML.get(i).get(4) == null || "".equals(OutputXML.get(i).get(4))) ){
				returnDate = OutputXML.get(i).get(4).toString();
			}
			if(!(OutputXML.get(i).get(5) == null || "".equals(OutputXML.get(i).get(5))) ){
				providerNo = OutputXML.get(i).get(5).toString();
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
		//Query Changed by aastha for Jira: PCAS-2145,1295 Sprint 4, Iteration 1
		String sQuery = "select CifId,AgreementId,LoanType,ProviderNo,LoanStat,CustRoleType,LoanApprovedDate,LoanMaturityDate,OutstandingAmt,TotalAmt,PaymentsAmt,TotalNoOfInstalments,RemainingInstalments,WriteoffStat,WriteoffStatDt,CreditLimit,OverdueAmt,NofDaysPmtDelay,MonthsOnBook,lastrepmtdt,IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last12Months,AECBHistMonthCnt,DPD5_Last3Months,'' as qc_Amnt,'' as Qc_emi,'' as Cac_indicator,Take_Over_Indicator,Consider_For_Obligations,case when IsDuplicate= '1' then 'Y' else 'N' end,avg_utilization,DPD5_Last12Months,DPD60Plus_Last12Months,Settlement_Flag from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Child_wi= '"+ formObject.getWFWorkitemName()
		+ "'  and LoanStat != 'Pipeline'   union select CifId,CardEmbossNum,CardType,ProviderNo,CardStatus,CustRoleType,StartDate,ClosedDate,CurrentBalance,'' as col6,PaymentsAmount,NoOfInstallments,'' as col5,WriteoffStat,WriteoffStatDt,CashLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,lastrepmtdt,IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last12Months,AECBHistMonthCnt,DPD5_Last3Months,qc_amt,qc_emi,CAC_Indicator,Take_Over_Indicator,Consider_For_Obligations,case when IsDuplicate= '1' then 'Y' else 'N' end,avg_utilization,DPD5_Last12Months,DPD60Plus_Last12Months,Settlement_Flag from ng_rlos_cust_extexpo_CardDetails with (nolock) where Child_wi  =  '"
		+ formObject.getWFWorkitemName()+ "' and cardstatus != 'Pipeline'   union select CifId,AcctId,AcctType,ProviderNo,AcctStat,CustRoleType,StartDate,ClosedDate,OutStandingBalance,TotalAmount,PaymentsAmount,'','',WriteoffStat,WriteoffStatDt,CreditLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,'',IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last12Months,AECBHistMonthCnt,DPD5_Last3Months,'','','','',isnull(Consider_For_Obligations,'true'),case when IsDuplicate= '1' then 'Y' else 'N' end,'',DPD5_Last12Months,DPD60Plus_Last12Months,Settlement_Flag from ng_rlos_cust_extexpo_AccountDetails with (nolock)  where child_wi  =  '"+formObject.getWFWorkitemName()+"' union"
		+" select CifId,ServiceID,ServiceType,ProviderNo,ServiceStat,CustRoleType,SubscriptionDt,SvcExpDt,'','','','','',WriteoffStat,WriteoffStatDt,'',OverDueAmount,NofDaysPmtDelay,MonthsOnBook,'',IsCurrent,CurUtilRate,DPD30_Last6Months,'',AECBHistMonthCnt,DPD5_Last3Months,'','','','',isnull(Consider_For_Obligations,'true'),case when IsDuplicate= '1' then 'Y' else 'N' end,'',DPD5_Last12Months,DPD60Plus_Last12Months,Settlement_Flag from ng_rlos_cust_extexpo_ServicesDetails with (nolock)  where ServiceStat='Active' and child_wi  =  '"+formObject.getWFWorkitemName()+"'";

		//Query Changed Jira: PCAS-2145,1295 Sprint 4, Iteration 1
		
		PersonalLoanS.mLogger.info(" sQuery"+sQuery+ "");
		String  add_xml_str ="";
		//added by aastha Jira: PCAS-2145,1295 Sprint 4, Iteration 1

		String CAC_BANK_NAME="";
		if ("Self Employed".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 5))){
			CAC_BANK_NAME=formObject.getNGValue("cmplx_Liability_New_CACBankName");
		}
		else{
			CAC_BANK_NAME=formObject.getNGValue("cmplx_EmploymentDetails_OtherBankCAC");
		}
		//added by aastha for  Jira: PCAS-2145,1295 Sprint 4, Iteration 1

		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		//PersonalLoanS.mLogger.info("ExternalBureauIndividualProducts list size"+OutputXML.size()+ "");
		String ReqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);//Commented by aastha for jira-Pcas-2145,1295
		for (int i = 0; i<OutputXML.size();i++){
			//OtherBankCAC
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
			String DPD60Last12Months = "";
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
			String avg_utilization="";
			String DPD60plus_last12month="";
			String DPD5_last12month="";
			String settlement_falg="";
			if(!(OutputXML.get(i).get(1) == null || "".equals(OutputXML.get(i).get(1))) ){
				AgreementId = OutputXML.get(i).get(1).toString();
			}				
			if(!(OutputXML.get(i).get(2) == null || "".equals(OutputXML.get(i).get(2))) ){
				ContractType = OutputXML.get(i).get(2).toString();
				try{
					String cardquery = "select code from ng_master_contract_type with(nolock) where description='"+ContractType+"'";
					//	PersonalLoanS.mLogger.info("ExternalBureauIndividualProducts sQuery"+sQuery+ "");
					List<List<String>> cardqueryXML = formObject.getNGDataFromDataCache(cardquery);
					ContractType=cardqueryXML.get(0).get(0);
					//	PersonalLoanS.mLogger.info("ExternalBureauIndividualProducts ContractType"+ContractType+ "ContractType");
				}
				catch(Exception e){
					PersonalLoanS.mLogger.info("ExternalBureauIndividualProducts ContractType Exception"+e+ "Exception");

					ContractType= OutputXML.get(i).get(2).toString();
				}
			}
			if(!(OutputXML.get(i).get(3) == null || "".equals(OutputXML.get(i).get(3))) ){
				provider_no = OutputXML.get(i).get(3).toString();
			}
			if(!(OutputXML.get(i).get(4) == null || "".equals(OutputXML.get(i).get(4))) ){
				phase = OutputXML.get(i).get(4).toString();
				if (phase.startsWith("A")){
					phase="A";
				}
				else {
					phase="C";
				}
			}				
			if(!(OutputXML.get(i).get(5) == null || "".equals(OutputXML.get(i).get(5))) ){
				CustRoleType = OutputXML.get(i).get(5).toString();
				String sQueryCustRoleType = "select code from ng_master_role_of_customer with(nolock) where Description='"+CustRoleType+"'";
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
				start_date = OutputXML.get(i).get(6).toString();
			}
			if(!(OutputXML.get(i).get(7) == null || "".equals(OutputXML.get(i).get(7)))) {
				close_date = OutputXML.get(i).get(7).toString();
			}				
			if(!(OutputXML.get(i).get(8) == null || "".equals(OutputXML.get(i).get(8))) ){
				OutStanding_Balance = OutputXML.get(i).get(8).toString();
			}
			if(!(OutputXML.get(i).get(9) == null || "".equals(OutputXML.get(i).get(9))) ){
				TotalAmt = OutputXML.get(i).get(9).toString();
			}
			if(!(OutputXML.get(i).get(10) == null || "".equals(OutputXML.get(i).get(10))) ){
				PaymentsAmt = OutputXML.get(i).get(10).toString();
			}
			if(!(OutputXML.get(i).get(11) == null || "".equals(OutputXML.get(i).get(11))) ){
				TotalNoOfInstalments = OutputXML.get(i).get(11).toString();
			}
			if(!(OutputXML.get(i).get(12) == null || "".equals(OutputXML.get(i).get(12))) ){
				RemainingInstalments = OutputXML.get(i).get(12).toString();
			}
			if(!(OutputXML.get(i).get(13) == null || "".equals(OutputXML.get(i).get(13))) ){
				WorstStatus = OutputXML.get(i).get(13).toString();
			}
			if(!(OutputXML.get(i).get(14) == null || "".equals(OutputXML.get(i).get(14))) ){
				WorstStatusDate = OutputXML.get(i).get(14).toString();
			}				
			if(!(OutputXML.get(i).get(15) == null || "".equals(OutputXML.get(i).get(15))) ){
				CreditLimit = OutputXML.get(i).get(15).toString();
			}
			if(!(OutputXML.get(i).get(16) == null || "".equals(OutputXML.get(i).get(16))) ){
				OverdueAmt = OutputXML.get(i).get(16).toString();
			}
			if(!(OutputXML.get(i).get(17) == null || "".equals(OutputXML.get(i).get(17))) ){
				NofDaysPmtDelay = OutputXML.get(i).get(17).toString();
			}				
			if(!(OutputXML.get(i).get(18) == null || "".equals(OutputXML.get(i).get(18))) ){
				MonthsOnBook = OutputXML.get(i).get(18).toString();
			}
			if(!(OutputXML.get(i).get(19) == null || "".equals(OutputXML.get(i).get(19))) ){
				last_repayment_date = OutputXML.get(i).get(19).toString();
			}
			if(!(OutputXML.get(i).get(20) == null || "".equals(OutputXML.get(i).get(20))) ){
				currently_current = OutputXML.get(i).get(20).toString();
			}
			if(!(OutputXML.get(i).get(21) == null || "".equals(OutputXML.get(i).get(21))) ){
				current_utilization = OutputXML.get(i).get(21).toString();
			}
			if(!(OutputXML.get(i).get(22) == null || "".equals(OutputXML.get(i).get(22))) ){
				DPD30Last6Months = OutputXML.get(i).get(22).toString();
			}
			if(!(OutputXML.get(i).get(23) == null || "".equals(OutputXML.get(i).get(23))) ){
				DPD60Last12Months = OutputXML.get(i).get(23).toString();
			}
			if(!(OutputXML.get(i).get(24) == null || "".equals(OutputXML.get(i).get(24))) ){
				AECBHistMonthCnt = OutputXML.get(i).get(24).toString();
			}				


			if(!(OutputXML.get(i).get(25) == null || "".equals(OutputXML.get(i).get(25))) ){
				delinquent_in_last_3months = OutputXML.get(i).get(25).toString();
			}
			if(!(OutputXML.get(i).get(26) == null || "".equals(OutputXML.get(i).get(26))) ){
				QC_Amt = OutputXML.get(i).get(26).toString();
			}
			if(!(OutputXML.get(i).get(27) == null || "".equals(OutputXML.get(i).get(27))) ){
				QC_emi = OutputXML.get(i).get(27).toString();
			}
			if(!(OutputXML.get(i).get(28) == null || "".equals(OutputXML.get(i).get(28))) ){
				CAC_Indicator = OutputXML.get(i).get(28).toString();
				if (CAC_Indicator != null && !("".equalsIgnoreCase(CAC_Indicator))){
					if ("true".equalsIgnoreCase(CAC_Indicator))
					{
						CAC_Indicator="Y";
					}
					else
					{
						CAC_Indicator="N";
					}
				}
			}
			String TakeOverIndicator="";
			if(!(OutputXML.get(i).get(29) == null || "".equals(OutputXML.get(i).get(29))) ){
				TakeOverIndicator = OutputXML.get(i).get(29).toString();
				PersonalLoanS.mLogger.info("takeover indicator"+TakeOverIndicator);
				if (TakeOverIndicator != null && !("".equalsIgnoreCase(TakeOverIndicator)))
				{
					if ("true".equalsIgnoreCase(TakeOverIndicator))
					{
						TakeOverIndicator="Y";
					}
					else 
					{
						TakeOverIndicator="N";
					}
				}
			}
			if(!(OutputXML.get(i).get(30) == null || "".equals(OutputXML.get(i).get(30))) )
			{
				consider_for_obligation = OutputXML.get(i).get(30).toString();
				if (consider_for_obligation != null && !("".equalsIgnoreCase(consider_for_obligation))){
					if ("true".equalsIgnoreCase(consider_for_obligation))
					{
						consider_for_obligation="Y";
					}
					else 
					{
						consider_for_obligation="N";
					}
				}
			}
			if(!(OutputXML.get(i).get(31) == null || OutputXML.get(i).get(31).equals("")) ){
				Duplicate_flag = OutputXML.get(i).get(31).toString();
			}
			if(!(OutputXML.get(i).get(32) == null || OutputXML.get(i).get(32).equals("")) ){
				avg_utilization = OutputXML.get(i).get(32).toString();
			}
			if(!(OutputXML.get(i).get(33) == null || OutputXML.get(i).get(33).equals("")) ){
				DPD5_last12month = OutputXML.get(i).get(33).toString();
			}
			if(!(OutputXML.get(i).get(34) == null || OutputXML.get(i).get(34).equals("")) ){
				DPD60plus_last12month = OutputXML.get(i).get(34).toString();
			}

			if(!(OutputXML.get(i).get(35) == null || "".equals(OutputXML.get(i).get(35))) )
			{
				settlement_falg = OutputXML.get(i).get(35).toString();
				if (settlement_falg != null && !("".equalsIgnoreCase(settlement_falg))){
					if ("true".equalsIgnoreCase(settlement_falg))
					{
						settlement_falg="Y";
					}
					else 
					{
						settlement_falg="N";
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
			{
				add_xml_str = add_xml_str + "<currently_current>Y</currently_current>";
			}
			else
			{
				add_xml_str = add_xml_str + "<currently_current>N</currently_current>";
			}
			add_xml_str = add_xml_str + "<current_utilization>"+current_utilization+"</current_utilization>";
			add_xml_str = add_xml_str + "<dpd_30_last_6_mon>"+DPD30Last6Months+"</dpd_30_last_6_mon>";

			add_xml_str = add_xml_str + "<dpd_60p_in_last_12_mon>" + DPD60plus_last12month + "</dpd_60p_in_last_12_mon>";
			add_xml_str = add_xml_str + "<dpd_5_in_last_12_mon>" + DPD5_last12month + "</dpd_5_in_last_12_mon>";
			add_xml_str = add_xml_str + "<no_months_aecb_history>"+AECBHistMonthCnt+"</no_months_aecb_history>";
			add_xml_str = add_xml_str + "<delinquent_in_last_3months>"+delinquent_in_last_3months+"</delinquent_in_last_3months>";
			add_xml_str = add_xml_str + "<clean_funded>"+""+"</clean_funded>";
			add_xml_str = add_xml_str + "<cac_indicator>"+CAC_Indicator+"</cac_indicator>";
			add_xml_str = add_xml_str + "<qc_emi>"+QC_emi+"</qc_emi>";
			if ("Credit Card".equalsIgnoreCase(ReqProd)){
				add_xml_str = add_xml_str + "<qc_amount>"+QC_Amt+"</qc_amount><company_flag>N</company_flag><cac_bank_name>"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_takeoverBank")+"</cac_bank_name></ExternalBureauIndividualProducts>";
			}
			else{
				add_xml_str = add_xml_str + "<qc_amount>"+QC_Amt+"</qc_amount><company_flag>N</company_flag><cac_bank_name>"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_takeoverBank")+"</cac_bank_name><take_over_indicator>"+TakeOverIndicator+"</take_over_indicator>"; 
				add_xml_str = add_xml_str +	 "<settlement_flag>" + settlement_falg + "</settlement_flag><consider_for_obligation>"+consider_for_obligation+"</consider_for_obligation><duplicate_flag>"+Duplicate_flag+"</duplicate_flag><avg_utilization>"+avg_utilization+"</avg_utilization></ExternalBureauIndividualProducts>";
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

	public String ExternalBureauManualAddIndividualProducts()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PersonalLoanS.mLogger.info("ExternalBureauManualAddIndividualProducts sQuery"+ "");
		int Man_liab_row_count = formObject.getLVWRowCount("cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
		String applicant_id = formObject.getNGValue("cmplx_Customer_CIFNO");
		String  add_xml_str ="";
		//added by aastha for pcas-2145,1295
		String CAC_BANK_NAME="";
		if ("Self Employed".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 5))){
			CAC_BANK_NAME=formObject.getNGValue("cmplx_Liability_New_CACBankName");
		}
		else{
			CAC_BANK_NAME=formObject.getNGValue("cmplx_EmploymentDetails_OtherBankCAC");
		}
		//added by aastha for pcas-2145,1295
		Date currentDate=new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String modifiedDate= sdf.format(currentDate);
		String close_date=  PLCommon.plusyear(modifiedDate,4,0,0);

		PersonalLoanS.mLogger.info("ExternalBureauIndividualProducts list size"+Man_liab_row_count+ "");
		if (Man_liab_row_count !=0){
			for (int i = 0; i<Man_liab_row_count;i++){
				String Type_of_Contract = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 0); //0
				String Limit = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 1); //0
				String EMI = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 2); //0
				String Take_over_Indicator = ("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",i, 3)) ? "Y" : "N"); // 0
				String cac_Indicator = ("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",i, 5)) ? "Y" : "N"); // 0
				String Qc_amt = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 6); //0
				String Qc_Emi = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 7); //0
				String worst_status=formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 17);
				String settlement_flag = ("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",i, 21)) ? "Y" : "N"); // 0

				//added by aastha for for Jira: PCAS-2145,1295 Sprint 4, Iteration 1

				String consider_for_obligation = ("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",i, 8)) ? "Y" : "N"); // 0
				String Application_type="L";
				//changes Done for Jira:PCAS-2145,1295 Sprint4, Iteration 1
				if ("true".equalsIgnoreCase(cac_Indicator)){
					cac_Indicator="Y";
				}
				else {
					cac_Indicator="N";
				}
				consider_for_obligation =NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 8))?"Y":"N"; //0

				String Utilization = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 11); //0
				String OutStanding = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 12); //0
				String mob = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 10);
				String avg_utilization = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 20);
				String delinquent_in_last_3months =("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i,13)) ? "1" : "0");
				String dpd_30_last_6_mon =  ("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i,14)) ? "1" : "0");
				String dpd_60p_in_last_12_mon = ("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i,15)) ? "1" : "0");
				String query="select App_Type from NG_MASTER_contract_type with(nolock) where Code ='"+Type_of_Contract+"'";
				try
				{
					List<List<String>> App_type=formObject.getDataFromDataSource(query);
					if(App_type!=null && !App_type.isEmpty())
					{
						Application_type=App_type.get(0).get(0);
					}
				}
				catch(Exception Ex)
				{
					PersonalLoanS.mLogger.info(Ex.getMessage());
				}
				add_xml_str = add_xml_str + "<ExternalBureauIndividualProducts><applicant_id>"+applicant_id+"</applicant_id>";
				add_xml_str = add_xml_str + "<external_bureau_individual_products_id></external_bureau_individual_products_id>";
				add_xml_str = add_xml_str + "<contract_type>"+Type_of_Contract+"</contract_type>";
				add_xml_str = add_xml_str + "<provider_no></provider_no>";
				add_xml_str = add_xml_str + "<phase>A</phase>";
				add_xml_str = add_xml_str + "<role_of_customer>A</role_of_customer>";
				add_xml_str = add_xml_str + "<start_date>"+modifiedDate+"</start_date>"; 

				add_xml_str = add_xml_str + "<close_date>"+close_date+"</close_date>";
				add_xml_str = add_xml_str + "<outstanding_balance>"+OutStanding+"</outstanding_balance>";
				if (Application_type.equalsIgnoreCase("L"))
				{
					add_xml_str = add_xml_str + "<total_amount>"+Limit+"</total_amount>";
				}
				add_xml_str = add_xml_str + "<payments_amount>"+EMI+"</payments_amount>";
				add_xml_str = add_xml_str + "<total_no_of_instalments></total_no_of_instalments>";
				add_xml_str = add_xml_str + "<no_of_remaining_instalments></no_of_remaining_instalments>";
				add_xml_str = add_xml_str + "<worst_status>"+worst_status+"</worst_status>";
				add_xml_str = add_xml_str + "<worst_status_date></worst_status_date>";
				if (Application_type.equalsIgnoreCase("C") )
				{
					add_xml_str = add_xml_str + "<credit_limit>"+Limit+"</credit_limit>";
				}
				add_xml_str = add_xml_str + "<overdue_amount></overdue_amount>";
				add_xml_str = add_xml_str + "<no_of_days_payment_delay></no_of_days_payment_delay>";
				add_xml_str = add_xml_str + "<mob>"+mob+"</mob>";
				add_xml_str = add_xml_str + "<last_repayment_date></last_repayment_date>";

				add_xml_str = add_xml_str + "<currently_current>N</currently_current>";
				add_xml_str = add_xml_str + "<current_utilization>"+Utilization+"</current_utilization>";
				add_xml_str = add_xml_str + "<dpd_30_last_6_mon>"+dpd_30_last_6_mon+"</dpd_30_last_6_mon>";

				add_xml_str = add_xml_str + "<dpd_60p_in_last_12_mon>"+dpd_60p_in_last_12_mon+"</dpd_60p_in_last_12_mon>";
				add_xml_str = add_xml_str + "<no_months_aecb_history></no_months_aecb_history>";
				add_xml_str = add_xml_str + "<delinquent_in_last_3months>"+delinquent_in_last_3months+"</delinquent_in_last_3months>";
				add_xml_str = add_xml_str + "<clean_funded>"+""+"</clean_funded>";
				add_xml_str = add_xml_str + "<cac_indicator>"+cac_Indicator+"</cac_indicator>";
				add_xml_str = add_xml_str + "<qc_emi>"+Qc_Emi+"</qc_emi>";
				add_xml_str = add_xml_str + "<qc_amount>" + Qc_amt + "</qc_amount><company_flag>N</company_flag><cac_bank_name>" + CAC_BANK_NAME + "</cac_bank_name><take_over_indicator>" + Take_over_Indicator + "</take_over_indicator>";
				add_xml_str = add_xml_str + "<settlement_flag>" + settlement_flag + "</settlement_flag><consider_for_obligation>" + consider_for_obligation + "</consider_for_obligation><duplicate_flag>N</duplicate_flag><avg_utilization>"+avg_utilization+"</avg_utilization></ExternalBureauIndividualProducts>";

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
				add_xml_str = add_xml_str + "<credit_grate_code>"+acctCreditGrade.substring(0,2)+"</credit_grate_code>";//changed by akshay for proc 8795
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

		String sQuery = "select AgreementId,ProviderNo,LoanType,LoanDesc,CustRoleType,Datelastupdated,TotalAmt,TotalNoOfInstalments,'' as col1,NoOfDaysInPipeline,isnull(Consider_For_Obligations,'true'),case when IsDuplicate= '1' then 'Y' else 'N' end from ng_rlos_cust_extexpo_LoanDetails with (nolock) where child_wi  =  '"
				+formObject.getWFWorkitemName()
				+"' and LoanStat = 'Pipeline' union select CardEmbossNum,ProviderNo,CardType,CardTypeDesc,CustRoleType,LastUpdateDate,'' as col2,NoOfInstallments,TotalAmount,NoOfDaysInPipeLine,isnull(Consider_For_Obligations,'true'),case when IsDuplicate= '1' then 'Y' else 'N' end  from ng_rlos_cust_extexpo_CardDetails with (nolock) where child_wi  =  '"
				+formObject.getWFWorkitemName()
				+"' and cardstatus = 'Pipeline'";
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
				agreementID = OutputXML.get(i).get(0).toString();
			}
			if(!(OutputXML.get(i).get(1) == null || "".equals(OutputXML.get(i).get(1))) ){
				ProviderNo = OutputXML.get(i).get(1).toString();
			}
			if(OutputXML.get(i).get(2)!=null && !OutputXML.get(i).get(2).isEmpty() &&  !"".equals(OutputXML.get(i).get(2)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(2)) ){
				contractType = OutputXML.get(i).get(2).toString();
				//below code added by aastha for PCAS-2145/1295
				try {
					String cardquery = "select code from ng_master_contract_type with (nolock) where description='"+ contractType + "'";
					//CreditCard.mLogger.info("ExternalBureauIndividualProducts sQuery"+ cardquery+ "");
					List<List<String>> cardqueryXML = formObject.getNGDataFromDataCache(cardquery);
					contractType = cardqueryXML.get(0).get(0);
					//CreditCard.mLogger.info("ExternalBureauIndividualProducts ContractType"+ ContractType+ "ContractType");
				} catch (Exception e) {
					PersonalLoanS.mLogger.info("ExternalBureauIndividualProducts ContractType Exception"+ e+ "Exception");

					contractType = OutputXML.get(i).get(2).toString();
				}
			}
			//End change by aastha
			if(!(OutputXML.get(i).get(3) == null || "".equals(OutputXML.get(i).get(3))) ){
				productType = OutputXML.get(i).get(3).toString();
			}

			// Commented by aastha for PCAS- 2145,1295 
			/*			if (contractType.contains("CreditCard")){	

				contractType="CC";
			}
			else if (contractType.equalsIgnoreCase("MURABAHA")){
				if (productType.contains("AUTO")){
					contractType="IAL";
				}
				else{
					contractType="IPL";
				}
			}
			else if (contractType.equalsIgnoreCase("AUTO")){
				contractType="AL";
			}
			else if (contractType.equalsIgnoreCase("HOME")){
				contractType="ML";
			}
			else if (contractType.equalsIgnoreCase("IJARAH")){
				contractType="IML";
			}
			else if (contractType.equalsIgnoreCase("PERSONAL")){
				contractType="PL";
			}
			else if (contractType.equalsIgnoreCase("HOME IN ONE")){
				contractType="HIO";
			}
			else if ("OVERDRAFT".equalsIgnoreCase(contractType))
			{
				contractType= "OD";
			}
			else{
				contractType= "PL";

	}*/
			//Comment Ended
			//added by aastha for Jira: PCAS-2145,1295 Sprint 4, Iteration 1
			if (!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4)
					.equals(""))) {
				role = OutputXML.get(i).get(4).toString();
				//added by nikhil for PCSP-822
				String sQueryCustRoleType = "select code from ng_master_role_of_customer with(nolock) where Description='"+role+"'";
				PersonalLoanS.mLogger.info("CustRoleType"+sQueryCustRoleType);
				List<List<String>> sQueryCustRoleTypeXML = formObject.getNGDataFromDataCache(sQueryCustRoleType);
				try{
					if(sQueryCustRoleTypeXML!=null && sQueryCustRoleTypeXML.size()>0 && sQueryCustRoleTypeXML.get(0)!=null){
						role=sQueryCustRoleTypeXML.get(0).get(0);
					}
				}
				catch(Exception e){
					PersonalLoanS.mLogger.info("Exception occured at sQueryCombinedLimit for"+sQueryCustRoleTypeXML);
					//End Change by aastha
					//	if(!(OutputXML.get(i).get(4) == null || "".equals(OutputXML.get(i).get(4))) ){
					role = OutputXML.get(i).get(4);
				}
				if(OutputXML.get(i).get(5)!=null && !OutputXML.get(i).get(5).isEmpty() &&  !"".equals(OutputXML.get(i).get(5)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(5)) )
				{
					lastUpdateDate = OutputXML.get(i).get(5).toString();
				}
				if(OutputXML.get(i).get(6)!=null && !OutputXML.get(i).get(6).isEmpty() &&  !"".equals(OutputXML.get(i).get(6)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(6))) {
					TotAmt = OutputXML.get(i).get(6).toString();
				}
				if(!(OutputXML.get(i).get(7) == null || "".equals(OutputXML.get(i).get(7)))) {
					noOfInstalmnt = OutputXML.get(i).get(7).toString();
				}
				if(!(OutputXML.get(i).get(8) == null || "".equals(OutputXML.get(i).get(8))) ){
					creditLimit = OutputXML.get(i).get(8).toString();
				}
				if(OutputXML.get(i).get(9)!=null && !OutputXML.get(i).get(9).isEmpty() &&  !"".equals(OutputXML.get(i).get(9)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(9)) ){
					noOfDayinPpl = OutputXML.get(i).get(9).toString();
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

				add_xml_str = add_xml_str + "<ppl_no_of_days_in_pipeline>"+noOfDayinPpl+"</ppl_no_of_days_in_pipeline><company_flag>N</company_flag><ppl_consider_for_obligation>"+consider_for_obligation+"</ppl_consider_for_obligation><ppl_duplicate_flag>"+ppl_Duplicate_flag+"</ppl_duplicate_flag></ExternalBureauPipelineProducts>"; // to be populated later

			}
			PersonalLoanS.mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "+ add_xml_str);
		}
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
			else //if("GUARANTOR".equalsIgnoreCase(opName)  || ("PRIMARY".equalsIgnoreCase(opName) && "Disbursal".equalsIgnoreCase(formObject.getWFActivityName())))
			{
				multipleApplicantName=plcommonObj.MultipleAppGridSelectedRow("MultipleApp_AppName");
			}
			PersonalLoanS.mLogger.info("multipleApplicantName: "+multipleApplicantName);

			for (int i = 0; i<add_row_count;i++){
				String City_of_Birth = formObject.getNGValue("cmplx_OECD_cmplx_GR_OecdDetails", i, 3); //0
				String Country_of_Birth=formObject.getNGValue("cmplx_OECD_cmplx_GR_OecdDetails", i, 2);//1
				String Undocumented_Flag=formObject.getNGValue("cmplx_OECD_cmplx_GR_OecdDetails", i, 0);//2
				String UndocumentedFlag_Reason=formObject.getNGValue("cmplx_OECD_cmplx_GR_OecdDetails", i, 1);//3
				String applicantName=formObject.getNGValue("cmplx_OECD_cmplx_GR_OecdDetails", i, 8);//3
				String counTaxResid = formObject.getNGValue("cmplx_OECD_cmplx_GR_OecdDetails", i, 4);
				String tinNum = formObject.getNGValue("cmplx_OECD_cmplx_GR_OecdDetails", i, 5);
				String noTinRes = formObject.getNGValue("cmplx_OECD_cmplx_GR_OecdDetails", i, 6);
				if(applicantName.split("-")[1].equalsIgnoreCase(multipleApplicantName)){
					/*add_xml_str = add_xml_str + "<OECDDet><CityOfBirth>"+City_of_Birth+"</CityOfBirth> ";
					add_xml_str = add_xml_str + "<CountryOfBirth>"+Country_of_Birth+"</CountryOfBirth>";
					add_xml_str = add_xml_str + "<CRSUnDocFlg>"+Undocumented_Flag+"</CRSUnDocFlg>";
					add_xml_str = add_xml_str + "<CRSUndocFlgReason>"+UndocumentedFlag_Reason+"</CRSUndocFlgReason></OECDDet>";*/
					add_xml_str = add_xml_str + "<OECDDet><CityOfBirth>"+City_of_Birth+"</CityOfBirth> ";
					add_xml_str = add_xml_str + "<CountryOfBirth>"+Country_of_Birth+"</CountryOfBirth>";
					add_xml_str = add_xml_str + "<CRSUnDocFlg>"+Undocumented_Flag+"</CRSUnDocFlg>";
					add_xml_str = add_xml_str + "<CRSUndocFlgReason>"+(UndocumentedFlag_Reason.equals("NA")?"":UndocumentedFlag_Reason)+"</CRSUndocFlgReason>";
					
					if(applicantName.split("-")[0].equalsIgnoreCase("P")){
						for (int j = 0; j<add_row_count;j++){
							String countryTaxRes = formObject.getNGValue("cmplx_OECD_cmplx_GR_OecdDetails", j, 4);
							String tinNo = formObject.getNGValue("cmplx_OECD_cmplx_GR_OecdDetails", j, 5);
							String noTinReason = formObject.getNGValue("cmplx_OECD_cmplx_GR_OecdDetails", j, 6);
							add_xml_str = add_xml_str + "<ReporCntryDet>";
							add_xml_str = add_xml_str + "<CntryOfTaxRes>"+countryTaxRes+"</CntryOfTaxRes>";
							add_xml_str = add_xml_str + "<TINNumber>"+tinNo+"</TINNumber>";
							add_xml_str = add_xml_str + "<NoTINReason>"+noTinReason+"</NoTINReason></ReporCntryDet>";
						}
						add_xml_str = add_xml_str + "</OECDDet>";
					}else{
						add_xml_str = add_xml_str + "<ReporCntryDet><CntryOfTaxRes>"+counTaxResid+"</CntryOfTaxRes>";
						add_xml_str = add_xml_str + "<TINNumber>"+tinNum+"</TINNumber>";
						add_xml_str = add_xml_str + "<NoTINReason>"+noTinRes+"</NoTINReason></ReporCntryDet></OECDDet>";
					}

					//code by saurabh on 24th Mar 2018. error generated for CRSUndocfalgreason.
					if(add_xml_str.contains(">NA<")){
						add_xml_str.replaceAll(">NA<", "><");
					}
					//below break added by saurabh for JIRA - 5129.
					break;
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

			if (add_row_count>0){
				String Lien_no = formObject.getNGValue("cmplx_FinacleCore_liendet_grid", 0, 1); //0
				String Lien_amount = formObject.getNGValue("cmplx_FinacleCore_liendet_grid", 0, 2); //0
				String Lien_maturity_date = formObject.getNGValue("cmplx_FinacleCore_liendet_grid", 0, 6); //0

				if("CARD_NOTIFICATION".equalsIgnoreCase(Call_name)){
					lien_xml_str = lien_xml_str+ "<LienDetails><LienNumber>"+Lien_no+"</LienNumber>";
					lien_xml_str = lien_xml_str+ "<LienCurrency>AED</LienCurrency><Amount>"+Lien_amount+"</Amount>";
					//lien_xml_str = lien_xml_str+ "<LienMaturityDt>"+Lien_maturity_date+"</LienMaturityDt>";
					if(!("".equals(Lien_maturity_date))){
						lien_xml_str = lien_xml_str+ "<MaturityDate>"+Lien_maturity_date+"</MaturityDate>";
					}
					lien_xml_str = lien_xml_str+ "</LienDetails>";
				}
				else if("CARD_SERVICES_REQUEST".equalsIgnoreCase(Call_name)){
					lien_xml_str = lien_xml_str+ "<LienDetails><LienNumber>"+Lien_no+"</LienNumber>";
					lien_xml_str = lien_xml_str+ "<LienCurrency>AED</LienCurrency><LienAmount>"+Lien_amount+"</LienAmount>";
					//lien_xml_str = lien_xml_str+ "<LienMaturityDt>"+Lien_maturity_date+"</LienMaturityDt>";
					if(!("".equals(Lien_maturity_date))){
						lien_xml_str = lien_xml_str+ "<LienMaturityDt>"+Lien_maturity_date+"</LienMaturityDt>";
					}
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
			Contactdetails_xml_str=Contactdetails_xml_str+"<ContactDetails><PrefModeOfContact>Phone</PrefModeOfContact><PhnDet><PhoneType>MobileNumber1</PhoneType><PhnCountryCode>00971</PhnCountryCode><CityCode></CityCode><PhnLocalCode>00971</PhnLocalCode>";
			//Contactdetails_xml_str = Contactdetails_xml_str+"<ContactDetails><PrefModeOfContact>Email</PrefModeOfContact><EmailDet><MailIdType>WORKEML</MailIdType><EmailID>"+formObject.getNGValue("AlternateContactDetails_EMAIL1_PRI")+"</EmailID><MailPrefFlag>Y</MailPrefFlag></EmailDet></ContactDetails>";
			String App_type=formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12);
			String PassPort=formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13);
			//++below code added by nikhil for Self-Supp CR
			if("SUPPLEMENT".equalsIgnoreCase(App_type) && formObject.getNGValue("cmplx_Customer_PAssportNo").equals(PassPort)) 
			{
				App_type="PRIMARY";
			}
			//Deepak Changes done on 8 Jan 2019 for PCSP-312 Not to send country code and local code tag.
			if("PRIMARY".equalsIgnoreCase(App_type))
			{
				Contactdetails_xml_str=Contactdetails_xml_str+"<PhoneNumber>"+formObject.getNGValue("AlternateContactDetails_MobileNo1")+"</PhoneNumber><PhnPrefFlag>Y</PhnPrefFlag></PhnDet></ContactDetails>";
				Contactdetails_xml_str = Contactdetails_xml_str+"<ContactDetails><PrefModeOfContact>Email</PrefModeOfContact><EmailDet><MailIdType>WORKEML</MailIdType><EmailID>"+formObject.getNGValue("AlternateContactDetails_EMAIL1_PRI")+"</EmailID><MailPrefFlag>Y</MailPrefFlag></EmailDet></ContactDetails>";

				if(!formObject.getNGValue("AlternateContactDetails_RESIDENCENO").equals(""))
				{
					Contactdetails_xml_str=Contactdetails_xml_str+"<ContactDetails><PrefModeOfContact>Phone</PrefModeOfContact><PhnDet><PhoneType>HOMEPH1</PhoneType><PhnCountryCode>00971</PhnCountryCode><CityCode></CityCode><PhnLocalCode>00971</PhnLocalCode><PhoneNumber>"+formObject.getNGValue("AlternateContactDetails_RESIDENCENO")+"</PhoneNumber><PhnPrefFlag>N</PhnPrefFlag></PhnDet></ContactDetails>";
				}
				//added by akshay for procs 11485,11469,11488
				if(!formObject.getNGValue("AlternateContactDetails_OFFICENO").equals("")){
					Contactdetails_xml_str = Contactdetails_xml_str+"<ContactDetails><PrefModeOfContact>Phone</PrefModeOfContact><PhnDet><PhoneType>OFFCPH1</PhoneType><PhnCountryCode>00971</PhnCountryCode><CityCode></CityCode><PhnLocalCode>00971</PhnLocalCode><PhoneNumber>"+formObject.getNGValue("AlternateContactDetails_OFFICENO")+"</PhoneNumber><PhnPrefFlag>N</PhnPrefFlag></PhnDet></ContactDetails>";
				}

				if(!formObject.getNGValue("AlternateContactDetails_HOMECOUNTRYNO").equals("")){
					Contactdetails_xml_str = Contactdetails_xml_str+"<ContactDetails><PrefModeOfContact>Phone</PrefModeOfContact><PhnDet><PhoneType>OverseasPhone</PhoneType><PhnCountryCode>00971</PhnCountryCode><CityCode></CityCode><PhnLocalCode>00971</PhnLocalCode><PhoneNumber>"+formObject.getNGValue("AlternateContactDetails_HOMECOUNTRYNO")+"</PhoneNumber><PhnPrefFlag>N</PhnPrefFlag></PhnDet></ContactDetails>";
				}

				if(!formObject.getNGValue("AlternateContactDetails_EMAIL2_SEC").equals(""))
				{
					Contactdetails_xml_str = Contactdetails_xml_str+"<ContactDetails><PrefModeOfContact>Email</PrefModeOfContact><EmailDet><MailIdType>HOMEEML</MailIdType><EmailID>"+formObject.getNGValue("AlternateContactDetails_EMAIL2_SEC")+"</EmailID><MailPrefFlag>N</MailPrefFlag></EmailDet></ContactDetails>";
				}
			}
			else if( App_type.equalsIgnoreCase("Supplement") && formObject.getLVWRowCount("SupplementCardDetails_cmplx_supplementGrid")>0){ 
				for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_supplementGrid");i++){
					if(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13).equals(formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,3)))
					{
						Contactdetails_xml_str=Contactdetails_xml_str+"<PhoneNumber>"+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,6)+"</PhoneNumber>";
						//Contactdetails_xml_str = Contactdetails_xml_str+"<PhnPrefFlag>Y</PhnPrefFlag></PhnDet></ContactDetails><ContactDetails><PrefModeOfContact>Email</PrefModeOfContact><EmailDet><MailIdType>WORKEML</MailIdType><EmailID>"+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,21)+"</EmailID><MailPrefFlag>Y</MailPrefFlag></EmailDet></ContactDetails><ContactDetails><PrefModeOfContact>Phone</PrefModeOfContact><PhnDet><PhoneType>HOMEPH2</PhoneType><PhnCountryCode>00971</PhnCountryCode>";
						Contactdetails_xml_str = Contactdetails_xml_str+"<PhnPrefFlag>Y</PhnPrefFlag></PhnDet></ContactDetails><ContactDetails><PrefModeOfContact>Email</PrefModeOfContact><EmailDet><MailIdType>WORKEML</MailIdType><EmailID>"+formObject.getNGValue("AlternateContactDetails_EMAIL1_PRI")+"</EmailID><MailPrefFlag>Y</MailPrefFlag></EmailDet></ContactDetails><ContactDetails><PrefModeOfContact>Email</PrefModeOfContact><EmailDet><MailIdType>WORKEML2</MailIdType><EmailID>"+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,21)+"</EmailID><MailPrefFlag>N</MailPrefFlag></EmailDet></ContactDetails><ContactDetails><PrefModeOfContact>Phone</PrefModeOfContact><PhnDet><PhoneType>HOMEPH2</PhoneType><PhnCountryCode>00971</PhnCountryCode>";
						Contactdetails_xml_str = Contactdetails_xml_str+"<CityCode></CityCode><PhnLocalCode>00971</PhnLocalCode><PhoneNumber>"+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,6)+"</PhoneNumber><PhnPrefFlag>N</PhnPrefFlag></PhnDet></ContactDetails>";
						break;
					}
				}
			}
			else{
				Contactdetails_xml_str="";
			}
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("PL Common java file"+ "Exception occured in get lien details method : "+e.getMessage());
			PLCommon.printException(e);
		}

		return Contactdetails_xml_str;
	}
	public String getAcctIdforServices(String transType, String transMode, String acctNoforAT){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String acctNo="",acctType="",str_xml="";
		 acctNo=acctNoforAT;
		try{
			if(("CCC".equalsIgnoreCase(transType)||"SC".equalsIgnoreCase(transType)||"LOC".equalsIgnoreCase(transType))&&"A".equalsIgnoreCase(transMode)){
				//CreditCard.mLogger.info("Account No for Account Transfer "+acctNo);
				String query="select AcctType from ng_RLOS_CUSTEXPOSE_AcctDetails where Child_Wi='"+formObject.getWFWorkitemName()+"' and AcctId='"+acctNo+"'";
				List<List<String>> list = formObject.getDataFromDataSource(query);
				if(list.get(0).get(0)!=null){
					acctType= list.get(0).get(0);
				}
			}
			else{
				return "";
			}
			str_xml+="<AcctInfo><AcctId>"+acctNo+"</AcctId></AcctInfo>";
			//str_xml+="<AcctType>"+acctType+"</AcctType></AcctInfo>";		
		}catch(Exception ex){
			//PersonalLoanS.mLogger.info("PL Common java file Exception occured in get lien details method : "+PLCommon.printException(ex));
			return "";
		}
		return str_xml;
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

					//PersonalLoanS.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}

				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");
				//PersonalLoanS.mLogger.info("parent_tag: "+parent_tag);
				//PersonalLoanS.mLogger.info("tag_name: "+tag_name);
				if("Channel".equalsIgnoreCase(tag_name)){
					//PersonalLoanS.mLogger.info("RLOS COMMON"+" iNSIDE channelcode+ ");

					String ReqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);

					String xml_str = int_xml.get(parent_tag);
					xml_str =  "<"+tag_name+">"+("Personal Loan".equalsIgnoreCase(ReqProd)?"PL":"CC")
							+"</"+ tag_name+">";

					//PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding channelcode+ "+xml_str);
					int_xml.put(parent_tag, xml_str);

				}

				if("RejectedDetails".equalsIgnoreCase(tag_name)){

					if(int_xml.containsKey(parent_tag))
					{

						String xml_str = int_xml.get(parent_tag);
						//PersonalLoanS.mLogger.info(" before adding internal RejectedData+ "+xml_str);
						try{
							List<String> objInput = new ArrayList();
							List<Object> objOutput = new ArrayList();

							objInput.add("Text:" + formObject.getWFWorkitemName());
							objOutput.add("Text");
							//PersonalLoanS.mLogger.info(" input for procedure: Ng_Rlos_Dectech_Rejected_app"+objInput.toString());
							objOutput = formObject.getDataFromStoredProcedure("Ng_Rlos_Dectech_Rejected_app", objInput, objOutput);
							//PersonalLoanS.mLogger.info(" procedure executed objOutput"+objOutput.toString());

							xml_str = xml_str + getRejectedDetails();
							//PersonalLoanS.mLogger.info(" after internal RejectedData+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}
						catch(Exception e){
							//PersonalLoanS.mLogger.info(" Expection occured in RejectedData+ "+e.getMessage());
							new PLCommon().printException(e);
						}
					}


				}

				else if("emp_type".equalsIgnoreCase(tag_name)){
					//PersonalLoanS.mLogger.info("RLOS COMMON"+" iNSIDE channelcode+ ");

					String empttype=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5);
					if(empttype!=null){	
						if ("Salaried".equalsIgnoreCase(empttype)){
							empttype="S";
						}
						else if (NGFUserResourceMgr_PL.getGlobalVar("PL_SalariedPensioner").equalsIgnoreCase(empttype)){
							empttype="SP";
						}
						else if ("Pensioner".equalsIgnoreCase(empttype)){
							empttype="P";
						}
						else {
							empttype="SE";
						}
					}
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str+ "<"+tag_name+">"+empttype+"</"+ tag_name+">";

					//PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding channelcode+ "+xml_str);
					int_xml.put(parent_tag, xml_str);

				}
				else if("world_check".equalsIgnoreCase(tag_name)){
					//PersonalLoanS.mLogger.info("RLOS COMMON"+" iNSIDE world_check+ ");

					String world_check=formObject.getNGValue("IS_WORLD_CHECK");
					//PersonalLoanS.mLogger.info("RLOS COMMON"+" iNSIDE world_check+ "+formObject.getLVWRowCount("cmplx_WorldCheck_WorldCheck_Grid"));
					if (formObject.getLVWRowCount("cmplx_WorldCheck_WorldCheck_Grid") > 0) {
						//CHANGES DONE BY NIKHIL FOR PCAS-2779
						String flag_match_found="Negative";
						for (int i=0;i<formObject.getLVWRowCount("cmplx_WorldCheck_WorldCheck_Grid");i++)
						{
							if(formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,17).equalsIgnoreCase("true"))
							{
								flag_match_found="Positive";
							}
						}
						world_check = flag_match_found;
					} else {
						world_check = "Negative";

					}


					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str+ "<"+tag_name+">"+world_check+"</"+ tag_name+">";

					//PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding world_check+ "+xml_str);
					int_xml.put(parent_tag, xml_str);

				}
				else if("current_emp_catogery".equalsIgnoreCase(tag_name)){
					//PersonalLoanS.mLogger.info("RLOS COMMON"+" iNSIDE current_emp_catogery+ ");

					String current_emp_catogery=formObject.getNGValue("cmplx_EmploymentDetails_CurrEmployer");
					/*//PersonalLoanS.mLogger.info("RLOS COMMON"+" value of current_emp_catogery "+current_emp_catogery);
					String squerycurremp="select Description from NG_MASTER_EmployerCategory_PL where isActive='Y' and code='"+current_emp_catogery+"'";
					//PersonalLoanS.mLogger.info("RLOS COMMON"+" query is "+squerycurremp);
					List<List<String>> squerycurrempXML=formObject.getNGDataFromDataCache(squerycurremp);
					//PersonalLoanS.mLogger.info("RLOS COMMON"+" query is "+squerycurrempXML);
					if(!squerycurrempXML.isEmpty()){
						if (squerycurrempXML.get(0).get(0)!=null){
							//PersonalLoanS.mLogger.info("RLOS COMMON"+" iNSIDE squerycurrempXML+ "+squerycurrempXML.get(0).get(0));
							current_emp_catogery=squerycurrempXML.get(0).get(0);
						}
					}*/
					if ("".equalsIgnoreCase(current_emp_catogery)||"--Select--".equalsIgnoreCase(current_emp_catogery)) {
						current_emp_catogery = "CN";

					}
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str+ "<"+tag_name+">"+current_emp_catogery+"</"+ tag_name+">";

					//PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding current_emp_catogery+ "+xml_str);
					int_xml.put(parent_tag, xml_str);

				}
				else if("prev_loan_dbr".equalsIgnoreCase(tag_name)||"prev_loan_tai".equalsIgnoreCase(tag_name)||
						"prev_loan_multiple".equalsIgnoreCase(tag_name)||"prev_loan_employer".equalsIgnoreCase(tag_name)){
					//PersonalLoanS.mLogger.info("RLOS COMMON"+" iNSIDE prev_loan_dbr+ ");
					String PreviousLoanDBR="";
					String PreviousLoanEmp="";
					String PreviousLoanMultiple="";
					String PreviousLoanTAI="";

					String squeryloan="select isNull(PreviousLoanDBR,0), isNull(PreviousLoanEmp,0), isNull(PreviousLoanMultiple,0), isNull(PreviousLoanTAI,0) from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where Request_Type='CollectionsSummary' and Limit_Increase='true'  and Child_wi= '"+formObject.getWFWorkitemName()+"'";
					List<List<String>> prevLoan=formObject.getNGDataFromDataCache(squeryloan);
					//PersonalLoanS.mLogger.info("RLOS COMMON"+" iNSIDE prev_loan_dbr+ "+squeryloan);

					if (prevLoan!=null && !prevLoan.isEmpty()){
						PreviousLoanDBR=prevLoan.get(0).get(0);
						PreviousLoanEmp=prevLoan.get(0).get(1);
						PreviousLoanMultiple=prevLoan.get(0).get(2);
						PreviousLoanTAI=prevLoan.get(0).get(65);
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
				/*else if("ins_value".equalsIgnoreCase(tag_name) || "prem_amnt".equalsIgnoreCase(tag_name) || "no_of_prem_paid".equalsIgnoreCase(tag_name) || "prem_type".equalsIgnoreCase(tag_name) ||"regular_payment".equalsIgnoreCase(tag_name) || "within_minwaiting_period".equalsIgnoreCase(tag_name) ){
					//PersonalLoanS.mLogger.info("ins_value");
					//PersonalLoanS.mLogger.info("ins_value::"+formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode"));
					String ins_value="",prem_amnt="",no_of_prem_paid="",prem_type="",regular_payment="",within_minwaiting_period="";

					if("LIFESUR".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")))
					{
						//PersonalLoanS.mLogger.info("ins_value life sur");
						ins_value=formObject.getNGValue("cmplx_EmploymentDetails_InsuranceValue");
						prem_amnt=formObject.getNGValue("cmplx_EmploymentDetails_PremAmt");
						no_of_prem_paid=formObject.getNGValue("cmplx_EmploymentDetails_PremPaid");
						prem_type=formObject.getNGValue("cmplx_EmploymentDetails_PremType") ;
						regular_payment=formObject.getNGValue("cmplx_EmploymentDetails_RegPayment").equalsIgnoreCase("true")?"Yes":"No";
						//PersonalLoanS.mLogger.info("lifersure");
						//PersonalLoanS.mLogger.info("lifersure"+regular_payment);
						within_minwaiting_period=formObject.getNGValue("cmplx_EmploymentDetails_MinimumWait").equalsIgnoreCase("true")?"Yes":"No";;
					}
					else if("MOTSUR".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")))
					{
						//PersonalLoanS.mLogger.info("ins_value motor sur");
						ins_value=formObject.getNGValue("cmplx_EmploymentDetails_MotorInsurance");
					}

					String xml_str = int_xml.get(parent_tag);
					if ("ins_value".equalsIgnoreCase(tag_name)) {
						xml_str = xml_str + "<" + tag_name + ">"+ins_value+"</" + tag_name + ">";
					} else if ("prem_amnt".equalsIgnoreCase(tag_name)) {
						xml_str = xml_str + "<" + tag_name + ">" + prem_amnt + "</" + tag_name + ">";
					} else if ("no_of_prem_paid".equalsIgnoreCase(tag_name)) {
						xml_str = xml_str + "<" + tag_name + ">" + no_of_prem_paid + "</" + tag_name + ">";
					}  else if ("prem_type".equalsIgnoreCase(tag_name)) {
						xml_str = xml_str + "<" + tag_name + ">" + prem_type + "</" + tag_name + ">";
					}
					else if ("regular_payment".equalsIgnoreCase(tag_name)) {
						xml_str = xml_str + "<" + tag_name + ">" + regular_payment + "</" + tag_name + ">";
					}
					else if ("within_minwaiting_period".equalsIgnoreCase(tag_name)) {
						xml_str = xml_str + "<" + tag_name + ">" + within_minwaiting_period + "</" + tag_name + ">";
					}


					int_xml.put(parent_tag, xml_str);
				}*/
				else if("no_of_cheque_bounce_int_3mon_Ind".equalsIgnoreCase(tag_name)){
					//PersonalLoanS.mLogger.info("RLOS COMMON"+" iNSIDE no_of_cheque_bounce_int_3mon_Ind+ ");
					String squerynoc="SELECT count(Child_Wi) FROM ng_rlos_FinancialSummary_ReturnsDtls with(nolock) WHERE CAST(returnDate AS datetime) >= DATEADD(month,-3,GETDATE()) and returntype='ICCS' and Child_Wi='"+formObject.getWFWorkitemName()+"'";
					List<List<String>> NOC=formObject.getNGDataFromDataCache(squerynoc);
					int count = 0;
					String xml_str = int_xml.get(parent_tag);
					if (NOC != null && NOC.size() > 0) {
						count += Integer.parseInt(NOC.get(0).get(0));
					}
					if(!formObject.isVisible("FinacleCore_Frame6")){
						String sQueryManualICCSadded = "select count(DDS_wi_name) from NG_RLOS_GR_DDSreturn with(nolock) where chqtype='ICCS' and chqretdate>=DATEADD(month,-3,GETDATE()) and DDS_wi_name ='" + formObject.getWFWorkitemName() + "'";
						List<List<String>> NOCManual = formObject.getNGDataFromDataCache(sQueryManualICCSadded);
						if (NOCManual != null && NOCManual.size() > 0) {
							count += Integer.parseInt(NOCManual.get(0).get(0));
						}
					}
					else{
						if(formObject.getLVWRowCount("cmplx_FinacleCore_DDSgrid")>0){
							for(int i=0;i<formObject.getLVWRowCount("cmplx_FinacleCore_DDSgrid");i++){
								if("ICCS".equalsIgnoreCase(formObject.getNGValue("cmplx_FinacleCore_DDSgrid",i,1)) && monthsDiff(formObject.getNGValue("cmplx_FinacleCore_DDSgrid",i,4),1)<=3){
									count++;
								}
							}
						}
					}

					xml_str = xml_str + "<" + tag_name + ">" + count + "</" + tag_name + ">";

					//PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding no_of_cheque_bounce_int_3mon_Ind+ " + xml_str);
					int_xml.put(parent_tag, xml_str);

				}
				else if("no_of_DDS_return_int_3mon_Ind".equalsIgnoreCase(tag_name)){
					//PersonalLoanS.mLogger.info("RLOS COMMON"+" iNSIDE no_of_cheque_bounce_int_3mon_Ind+ ");
					String squerynoc="SELECT count(Child_Wi) FROM ng_rlos_FinancialSummary_ReturnsDtls with(nolock) WHERE CAST(returnDate AS datetime) >= DATEADD(month,-3,GETDATE()) and returntype='DDS' and Child_Wi='"+formObject.getWFWorkitemName()+"'";
					List<List<String>> NOC=formObject.getNGDataFromDataCache(squerynoc);
					int count = 0;
					String xml_str = int_xml.get(parent_tag);
					if (NOC != null && NOC.size() > 0) {
						count += Integer.parseInt(NOC.get(0).get(0));
					}
					if(!formObject.isVisible("FinacleCore_Frame6")){
						String sQueryManualICCSadded = "select count(DDS_wi_name) from NG_RLOS_GR_DDSreturn with(nolock) where chqtype='DDS' and chqretdate>=DATEADD(month,-3,GETDATE()) and DDS_wi_name ='" + formObject.getWFWorkitemName() + "'";
						List<List<String>> NOCManual = formObject.getNGDataFromDataCache(sQueryManualICCSadded);
						if (NOCManual != null && NOCManual.size() > 0) {
							count += Integer.parseInt(NOCManual.get(0).get(0));
						}
					}
					else{
						if(formObject.getLVWRowCount("cmplx_FinacleCore_DDSgrid")>0){
							for(int i=0;i<formObject.getLVWRowCount("cmplx_FinacleCore_DDSgrid");i++){//added by shweta
								if("DDS".equalsIgnoreCase(formObject.getNGValue("cmplx_FinacleCore_DDSgrid",i,1)) && monthsDiff(formObject.getNGValue("cmplx_FinacleCore_DDSgrid",i,4),1)<=3){
									count++;
								}
							}
						}
					}

					xml_str = xml_str + "<" + tag_name + ">" + count + "</" + tag_name + ">";

					//PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding no_of_DDS_return_int_3mon_Ind+ " + xml_str);
					int_xml.put(parent_tag, xml_str);

				}
				else if("blacklist_cust_type".equalsIgnoreCase(tag_name)||"internal_blacklist".equalsIgnoreCase(tag_name)||
						"internal_blacklist_date".equalsIgnoreCase(tag_name)||"internal_blacklist_code".equalsIgnoreCase(tag_name)||
						"negative_cust_type".equalsIgnoreCase(tag_name)||"internal_negative_flag".equalsIgnoreCase(tag_name)||
						"internal_negative_date".equalsIgnoreCase(tag_name)||"internal_negative_code".equalsIgnoreCase(tag_name)){
					//PersonalLoanS.mLogger.info("RLOS COMMON"+" iNSIDE channelcode+ ");
					String ParentWI_Name = formObject.getNGValue("Parent_WIName");
					String squeryBlacklist="select BlacklistFlag,BlacklistDate,BlacklistReasonCode,NegatedFlag,NegatedDate,NegatedReasonCode from ng_rlos_cif_detail with(nolock) where cif_wi_name='"+ParentWI_Name+"' and cif_searchType = 'Internal'";
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
					//PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding internal_blacklist+ "+xml_str);
					int_xml.put(parent_tag, xml_str);


				}
				else if("pl_employer_status".equalsIgnoreCase(tag_name))
				{
					PersonalLoanS.mLogger.info(" iNSIDE current_emp_catogery+ ");
					String pl_employer_status=formObject.getNGValue("cmplx_EmploymentDetails_EmpStatusPL");

					if ("".equalsIgnoreCase(pl_employer_status)||"--Select--".equalsIgnoreCase(pl_employer_status)) {
						pl_employer_status = "CN";
					}
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str+ "<"+tag_name+">"+pl_employer_status+"</"+ tag_name+">";
					int_xml.put(parent_tag, xml_str);
				}



				else if("external_blacklist_flag".equalsIgnoreCase(tag_name)||"external_blacklist_date".equalsIgnoreCase(tag_name)||
						"external_blacklist_code".equalsIgnoreCase(tag_name)){
					//PersonalLoanS.mLogger.info("RLOS COMMON"+" iNSIDE channelcode+ ");
					String ParentWI_Name = formObject.getNGValue("Parent_WIName");
					String squeryBlacklist="select BlacklistFlag,BlacklistDate,BlacklistReasonCode from ng_rlos_cif_detail with (nolock) where (cif_wi_name='"+ParentWI_Name+"' or cif_wi_name='"+formObject.getWFWorkitemName()+"') and cif_searchType = 'External'";
					//PersonalLoanS.mLogger.info("RLOS COMMON"+" iNSIDE channelcode+ "+squeryBlacklist);
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

					//PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding internal_blacklist+ "+xml_str);
					int_xml.put(parent_tag, xml_str);


				}
				else if("ApplicationDetails".equalsIgnoreCase(tag_name)){
					//PersonalLoanS.mLogger.info("inside 1st if"+"inside DECTECH req1");

					//PersonalLoanS.mLogger.info("inside 1st if"+"inside customer update req2");
					String xml_str = int_xml.get(parent_tag);
					//PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding product+ "+xml_str);
					xml_str = xml_str + getProduct_details();
					//PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding product+ "+xml_str);
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

						//PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding confirmedinjob+ "+xml_str);
						int_xml.put(parent_tag, xml_str);

					}		                            	
				}

				else if("ref_phone_no".equalsIgnoreCase(tag_name) ){
					if(int_xml.containsKey(parent_tag))
					{
						//PersonalLoanS.mLogger.info("RLOS COMMON"+" INSIDE ref_phone_no+ ");
						int count=formObject.getLVWRowCount("cmplx_ReferenceDetails_cmplx_ReferenceGrid");
						String ref_phone_no="";
						String ref_relationship="";
						//PersonalLoanS.mLogger.info("RLOS COMMON"+" INSIDE ref_phone_no+ "+count);
						if (count != 0){
							//ref_phone_no=formObject.getNGValue("cmplx_ReferenceDetails_cmplx_ReferenceGrid",0,4);
							ref_phone_no=formObject.getNGValue("cmplx_ReferenceDetails_cmplx_ReferenceGrid",0,2);//by shweta

							ref_relationship=formObject.getNGValue("cmplx_ReferenceDetails_cmplx_ReferenceGrid",0,3);
							//PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding ref_phone_no+ "+ref_phone_no);
							//PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding ref_relationship+ "+ref_relationship);
						}


						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<"+tag_name+">"+ref_phone_no
								+"</"+ tag_name+"><ref_relationship>"+ref_relationship+"</ref_relationship>";

						//PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding confirmedinjob+ "+xml_str);
						int_xml.put(parent_tag, xml_str);

					}		                            	
				}

				else if("confirmed_in_job".equalsIgnoreCase(tag_name)){
					if(int_xml.containsKey(parent_tag))
					{
						String confirmedinjob=formObject.getNGValue("cmplx_EmploymentDetails_JobConfirmed");

						if (confirmedinjob!=null){
							if ("true".equalsIgnoreCase(confirmedinjob) || "Yes".equalsIgnoreCase(confirmedinjob)){//PCASI-1047
								confirmedinjob="Y";
							}
							else{
								confirmedinjob="N";
							}

							String xml_str = int_xml.get(parent_tag);
							xml_str = xml_str + "<"+tag_name+">"+confirmedinjob
									+"</"+ tag_name+">";

							//PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding confirmedinjob+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}
					}		                            	
				}

				else if("borrowing_customer".equalsIgnoreCase(tag_name)){
					//PersonalLoanS.mLogger.info("RLOS iNSIDE borrowing_customer+ ");
					String squeryBorrow="select distinct(borrowingCustomer) from ng_RLOS_CUSTEXPOSE_CardDetails WHERE  child_wi ='"+formObject.getWFWorkitemName()+"' and cifid='"+formObject.getNGValue("cmplx_Customer_CIFNO")+"'  union select distinct(borrowingCustomer) from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock)  WHERE child_wi ='"+formObject.getWFWorkitemName()+"' and cifid='"+formObject.getNGValue("cmplx_Customer_CIFNO")+"'";

					//PersonalLoanS.mLogger.info("RLOS COMMONiNSIDE borrowing_customer query+ "+squeryBorrow);
					List<List<String>> borrowing_customer=formObject.getNGDataFromDataCache(squeryBorrow);
					if (borrowing_customer!=null && !borrowing_customer.isEmpty()){
						String xml_str =  int_xml.get(parent_tag);
						xml_str = xml_str+ "<"+tag_name+">"+borrowing_customer.get(0).get(0)
								+"</"+ tag_name+">";

						//PersonalLoanS.mLogger.info("after adding borrowing_customer+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}
				}

				else if("funding_pattern".equalsIgnoreCase(tag_name)){
					//PersonalLoanS.mLogger.info("RLOS iNSIDE FundingPattern+ ");
					String squeryfund="select distinct(FundingPattern) from ng_RLOS_CUSTEXPOSE_AcctDetails with(nolock) WHERE  Child_wi ='"+formObject.getWFWorkitemName()+"'";

					//PersonalLoanS.mLogger.info("RLOS COMMONiNSIDE FundingPattern query+ "+squeryfund);
					List<List<String>> funding_pattern=formObject.getNGDataFromDataCache(squeryfund);
					if (funding_pattern!=null && !funding_pattern.isEmpty()){
						String xml_str =  int_xml.get(parent_tag);
						xml_str = xml_str+ "<"+tag_name+">"+funding_pattern.get(0).get(0)
								+"</"+ tag_name+">";

						//PersonalLoanS.mLogger.info("after adding funding_pattern+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}
				}

				else if("primary_cif".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("RLOS iNSIDE FundingPattern+ ");
					String squeryfund="select CIF_ID as CustId from ng_RLOS_GR_FinacleCRMCustInfo with (nolock) where ChildMapping = (select ParentMapping from ng_RLOS_FinacleCRMCustInfo with (nolock) where wi_name = '"+formObject.getWFWorkitemName()+"') and Consider_For_Obligations = 'true'";
					//CreditCard.mLogger.info("RLOS COMMONiNSIDE FundingPattern query+ "+squeryfund);
					List<List<String>> allcifs=formObject.getNGDataFromDataCache(squeryfund);
					if (allcifs!=null && !allcifs.isEmpty()){
						String primar_cif="";
						for(List <String> cifs : allcifs)
						{
							if("".equalsIgnoreCase(primar_cif))
							{
								primar_cif+=cifs.get(0);
							}
							else
							{
								primar_cif+=","+cifs.get(0);
							}
						}
						String xml_str =  int_xml.get(parent_tag);
						xml_str = xml_str+ "<"+tag_name+">"+primar_cif
								+"</"+ tag_name+">";

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
								nmfQuery="select count(comp_name) from NG_Master_NMF_not_listed_Comp  with (nolock) where comp_name='"+emp_name+"'";
							}
						}
					}
					else{
						nmfQuery="select count(comp_name) from NG_Master_NMF_not_listed_Comp with (nolock) where comp_name='"+formObject.getNGValue("cmplx_EmploymentDetails_EmpName")+"'";
					}
					//PersonalLoanS.mLogger.info("RLOS iNSIDE nmf_flag+ ");
					//PersonalLoanS.mLogger.info("RLOS COMMONiNSIDE borrowing_customer query+ "+nmfQuery);
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

						//PersonalLoanS.mLogger.info("after adding nmfQueryData+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}
				}
				//Production change for sending CES flag to Dectech Dated 22Apr2021
				else if ("ces".equalsIgnoreCase(tag_name)) {
					if (int_xml.containsKey(parent_tag)) {
						String ces = formObject.getNGValue("cmplx_EmploymentDetails_CESflag").equalsIgnoreCase("true") ? "Y" : "N";
						if (ces != null) {
							String xml_str = int_xml.get(parent_tag);
							xml_str = xml_str + "<" + tag_name + ">" + ces+ "</" + tag_name + ">";
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

							//PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding included_pl_aloc+ "+xml_str);
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

							//PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding cmplx_EmploymentDetails_IncInCC+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}	
					}
				}
				else if("vip_flag".equalsIgnoreCase(tag_name)){
					if(int_xml.containsKey(parent_tag))
					{
						String vip_flag=formObject.getNGValue("cmplx_Customer_VIPFlag");
						//PersonalLoanS.mLogger.info("Rishabh check"+vip_flag + '2');


						if ("true".equalsIgnoreCase(vip_flag)){
							vip_flag="Y";
							//PersonalLoanS.mLogger.info("Rishabh check"+vip_flag + '3');
						}
						else{
							vip_flag="N";
							//PersonalLoanS.mLogger.info("Rishabh check"+vip_flag + '4');
						}

						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<"+tag_name+">"+	vip_flag	+"</"+ tag_name+">";
						//PersonalLoanS.mLogger.info("Rishabh check"+vip_flag + '5');

						//PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding cmplx_Customer_VIPFlag+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}		                            	
				}
				else if("standing_instruction".equalsIgnoreCase(tag_name)){
					//PersonalLoanS.mLogger.info("RLOS COMMON"+" iNSIDE standing_instruction+ ");
					String squerynoc="SELECT count(Child_wi) FROM ng_rlos_FinancialSummary_SiDtls with(nolock) WHERE Child_wi='"+formObject.getWFWorkitemName()+"'";
					List<List<String>> NOC=formObject.getNGDataFromDataCache(squerynoc);
					//PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding cmplx_Customer_VIPFlag+ "+squerynoc);
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

						//PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding standing_instruction+ "+xml_str);
						int_xml.put(parent_tag, xml_str);

					}

				}
				else if("aggregate_exposed".equalsIgnoreCase(tag_name)){
					//PersonalLoanS.mLogger.info(" Inside Aggregate ");
					if(int_xml.containsKey(parent_tag))
					{
						formObject.saveFragment("EligibilityAndProductInformation");
						//Considering negative query
						//String aeQuery = "select (select isNull((Sum(convert(float,replace([TotalOutstandingAmt],'NA','0')))),0) as TotalOutstandingAmt from ng_RLOS_CUSTEXPOSE_loanDetails with (nolock) where Consider_For_Obligations='true' and LoanStat in ('A','ACTIVE') and child_wi ='"+formObject.getWFWorkitemName()+"') + (select isNull((Sum(convert(float,replace([TotalOutstandingAmt],'NA','0')))),0) as TotalOutstandingAmt from ng_RLOS_CUSTEXPOSE_loanDetails with (nolock) where Consider_For_Obligations='true' and LoanStat ='Pipeline' and child_wi ='"+formObject.getWFWorkitemName()+"') +(select isNull((Sum(convert(float,replace([OutstandingAmt],'NA','0')))),0)  from ng_RLOS_CUSTEXPOSE_CardDetails with (nolock) where  Consider_For_Obligations='true' and CardStatus in ('A','ACTIVE') and child_wi  ='"+formObject.getWFWorkitemName()+"' and SchemeCardProd like '%LOC%') + (select isNull((Sum(convert(float,replace([CreditLimit],'NA','0')))),0) as OutstandingAmt from (select max( liab.CreditLimit) as CreditLimit from ng_RLOS_CUSTEXPOSE_CardDetails liab, ng_master_cardProduct prod with (nolock) where  Consider_For_Obligations='true' and CardStatus in ('A','ACTIVE') and child_wi ='"+formObject.getWFWorkitemName()+"' and SchemeCardProd not like '%LOC%' and liab.SchemeCardProd=prod.CODE group by case when prod.ReqProduct='Conventional' then prod.ReqProduct else liab.SchemeCardProd end) as TempTable)+( select isNull((Sum(convert(float,replace([final_limit],'NA','0')))),0) from ng_rlos_EligAndProdInfo with (nolock) where wi_name ='"+formObject.getWFWorkitemName()+"') as aggregateExposure";
						//Considering negative query

						String aeQuery = "select (select isNull((Sum(convert(float,replace([OutstandingBalance],'NA','0')))),0)  from ng_RLOS_CUSTEXPOSE_CardinstallmentDetails installment join ng_RLOS_CUSTEXPOSE_CardDetails carddetails with (nolock) on carddetails.CardEmbossNum=installment.CardcrnNumber where  carddetails.Consider_For_Obligations='true' and carddetails.CardStatus in ('A','ACTIVE') and carddetails.child_wi ='"+formObject.getWFWorkitemName()+"' and SchemeCardProd like '%LOC%' AND OutstandingBalance not like '%-%') +(select isNull((Sum(convert(float,replace([CreditLimit],'NA','0')))),0) as OutstandingAmt from (select DISTINCT max( liab.CreditLimit) as CreditLimit from ng_RLOS_CUSTEXPOSE_CardDetails liab, ng_master_cardProduct prod with (nolock) where  Consider_For_Obligations='true' and CardStatus in ('A','ACTIVE') and child_wi ='"+formObject.getWFWorkitemName()+"' and SchemeCardProd not like '%LOC%' AND CreditLimit not like '%-%' and liab.SchemeCardProd=prod.CODE group by case when prod.ReqProduct='Conventional' then prod.ReqProduct else liab.SchemeCardProd end) as TempTable)+( select isNull((Sum(convert(float,replace([final_limit],'NA','0')))),0) from ng_rlos_EligAndProdInfo with (nolock) where wi_name ='"+formObject.getWFWorkitemName()+"') + (select isNull((Sum(convert(float,replace([CreditLimit],'NA','0')))),0) as OutstandingAmt from ng_RLOS_CUSTEXPOSE_LoanDetails where (LoanType like '%PL%' or LoanType like '%PERSONAL%') and SchemeCardProd like '%P_XPAT_CASHLOAN%' and child_wi ='"+formObject.getWFWorkitemName()+"') as aggregateExposure";
						//PersonalLoanS.mLogger.info(" Inside Aggregate "+aeQuery);

						List<List<String>> aggregate_exposed = formObject.getDataFromDataSource(aeQuery);

						String aggr_expo=aggregate_exposed.get(0).get(0);
						double aggreg=Double.parseDouble(aggr_expo);
						aggr_expo=String.format("%.2f", aggreg);

						formObject.setNGValue("cmplx_Liability_New_AggrExposure", aggr_expo,false);//changed by akshay on 25/9/17 as per point 2 of problem sheet
						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<"+tag_name+">"+aggr_expo+"</"+ tag_name+">";

						//PersonalLoanS.mLogger.info(" after adding aggregate_exposed+ "+xml_str);
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

							//PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding confirmedinjob+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}	
					}
				}


				else if("AccountDetails".equalsIgnoreCase(tag_name)){
					if(int_xml.containsKey(parent_tag))
					{
						String xml_str = int_xml.get(parent_tag);
						//PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding internal liability+ "+xml_str);
						xml_str = xml_str + getInternalLiabDetails();
						//PersonalLoanS.mLogger.info("RLOS COMMON"+" after internal liability+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}

				}
				else if ("BlacklistDetails".equalsIgnoreCase(tag_name)) {

					if (int_xml.containsKey(parent_tag)) {
						String xml_str = int_xml.get(parent_tag);
						PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding BlacklistDetails+ " + xml_str);
						xml_str = xml_str + PL_Common.getBlacklistDetails("Blacklist");
						PersonalLoanS.mLogger.info("RLOS COMMON"+" after BlacklistDetails+ " + xml_str);
						int_xml.put(parent_tag, xml_str);
					}
				}
				else if ("NegatedDetails".equalsIgnoreCase(tag_name)) {

					if (int_xml.containsKey(parent_tag)) {
						String xml_str = int_xml.get(parent_tag);
						PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding NegatedDetails+ " + xml_str);
						xml_str = xml_str + PL_Common.getBlacklistDetails("Negated");
						PersonalLoanS.mLogger.info("RLOS COMMON"+" after NegatedDetails+ " + xml_str);
						int_xml.put(parent_tag, xml_str);
					}


				}

				else if("InternalBureau".equalsIgnoreCase(tag_name) ){

					String xml_str = int_xml.get(parent_tag);
					//PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding InternalBureauData+ "+xml_str);
					String temp = InternalBureauData();
					if(!"".equalsIgnoreCase(temp)){
						if (xml_str==null){
							//PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding bhrabc"+xml_str);
							xml_str="";
						}
						xml_str =  xml_str+ temp;
						//PersonalLoanS.mLogger.info("RLOS COMMON"+" after InternalBureauData+ "+xml_str);
						int_xml.get(parent_tag);
						int_xml.put(parent_tag, xml_str);
					}


				}
				else if("InternalBouncedCheques".equalsIgnoreCase(tag_name) ){

					String xml_str = int_xml.get(parent_tag);
					//PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding InternalBouncedCheques+ "+xml_str);
					String temp = InternalBouncedCheques();
					if(!"".equalsIgnoreCase(temp)){
						if (xml_str==null){
							//PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding bhrabc"+xml_str);
							xml_str="";
						}
						xml_str =  xml_str+ temp;
						//PersonalLoanS.mLogger.info("RLOS COMMON"+" after InternalBouncedCheques+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}

				}
				else if("InternalBureauIndividualProducts".equalsIgnoreCase(tag_name)){

					String xml_str = int_xml.get(parent_tag);
					//PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding InternalBureauIndividualProducts+ "+xml_str);
					String temp = InternalBureauIndividualProducts();
					if(!"".equalsIgnoreCase(temp)){
						if (xml_str==null){
							//PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding bhrabc"+xml_str);
							xml_str="";
						}
						xml_str =  xml_str+ temp;
						//PersonalLoanS.mLogger.info("RLOS COMMON"+" after InternalBureauIndividualProducts+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}

				}
				else if("InternalBureauPipelineProducts".equalsIgnoreCase(tag_name)){

					String xml_str = int_xml.get(parent_tag);
					//PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding InternalBureauPipelineProducts+ "+xml_str);
					String temp = InternalBureauPipelineProducts();
					if(!"".equalsIgnoreCase(temp)){
						if (xml_str==null){
							//PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding bhrabc"+xml_str);
							xml_str="";
						}
						xml_str =  xml_str+ temp;
						//PersonalLoanS.mLogger.info("RLOS COMMON"+" after InternalBureauPipelineProducts+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}

				}
				else if("ExternalBureau".equalsIgnoreCase(tag_name) ){

					String xml_str = int_xml.get(parent_tag);
					//PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding ExternalBureau+ "+xml_str);
					String temp = ExternalBureauData();
					if(!"".equalsIgnoreCase(temp)){
						if (xml_str==null){
							//PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding bhrabc"+xml_str);
							xml_str="";
						}
						xml_str =  xml_str+ temp;
						//PersonalLoanS.mLogger.info("RLOS COMMON"+" after ExternalBureau+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}

				}
				else if("ExternalBouncedCheques".equalsIgnoreCase(tag_name)){

					String xml_str = int_xml.get(parent_tag);
					//PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding ExternalBouncedCheques+ "+xml_str);
					String temp = ExternalBouncedCheques();
					if(!"".equalsIgnoreCase(temp)){
						if (xml_str==null){
							//PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding bhrabc"+xml_str);
							xml_str="";
						}
						xml_str =  xml_str+ temp;
						//PersonalLoanS.mLogger.info("RLOS COMMON"+" after ExternalBouncedCheques+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}                    	
				}
				else if("ExternalBureauIndividualProducts".equalsIgnoreCase(tag_name)){

					String xml_str = int_xml.get(parent_tag);
					//PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding ExternalBureauIndividualProducts+ "+xml_str);
					String temp =  ExternalBureauIndividualProducts();
					//PersonalLoanS.mLogger.info("RLOS COMMON"+" value of temp to be adding temp+ "+temp);
					String Manual_add_Liab =  ExternalBureauManualAddIndividualProducts();

					if((!"".equalsIgnoreCase(temp)) || (!"".equalsIgnoreCase(Manual_add_Liab))){
						if (xml_str==null){
							PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding bhrabc"+xml_str);
							xml_str="";
						}
						//PersonalLoanS.mLogger.info("MAnual and external liability"+Manual_add_Liab);
						//PersonalLoanS.mLogger.info("Manual liability"+Manual_add_Liab);

						xml_str =  xml_str + temp + Manual_add_Liab;
						//PersonalLoanS.mLogger.info("RLOS COMMON"+" after ExternalBureauIndividualProducts+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}	                            	
				}
				else if("ExternalBureauPipelineProducts".equalsIgnoreCase(tag_name)){

					String xml_str = int_xml.get(parent_tag);
					//PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding ExternalBureauPipelineProducts+ "+xml_str);
					String temp =  ExternalBureauPipelineProducts();
					if(!"".equalsIgnoreCase(temp)){
						if (xml_str==null){
							//PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding bhrabc"+xml_str);
							xml_str="";
						}
						xml_str =  xml_str+ temp;
						//PersonalLoanS.mLogger.info("RLOS COMMON"+" after ExternalBureauPipelineProducts+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}                        	
				}
				//added by aastha for CR: date:05-11-2019 Sprint 4, Iteration 3
				else if("Incomedetails".equalsIgnoreCase(tag_name)){
					String xml_str = int_xml.get(parent_tag);
					//PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding IncomeDetails+ "+xml_str);
					String consecutive_flag="";
					String salary_less_than_5000="";
					if(("cmplx_IncomeDetails_netSal1")!=null &&("cmplx_IncomeDetails_netSal2")!=null && ("cmplx_IncomeDetails_netSal3")!=null )
						consecutive_flag="Y";
					else
						consecutive_flag="N";	

					xml_str = xml_str + "<"+tag_name+">"+consecutive_flag+"</"+ tag_name+">";
					int_xml.put(parent_tag, xml_str);
					//Condition for average net salary of three months
					//PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding IncomeDetails: average net salary+ "+xml_str);
					if(Integer.parseInt("cmplx_IncomeDetails_AvgNetSal")<=5000)
						salary_less_than_5000="Y";

					else
						salary_less_than_5000="N";

					xml_str = xml_str + "<"+tag_name+">"+salary_less_than_5000+"</"+ tag_name+">";
					int_xml.put(parent_tag, xml_str);
					//PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding IncomeDetails: average net salary+ "+xml_str);

					//PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding IncomeDetails+ "+xml_str);

				}
				else if ("Utilization24months".equalsIgnoreCase(tag_name)) {

					String xml_str = int_xml.get(parent_tag);
					PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding ExternalBureauPipelineProducts+ "+ xml_str);
					String temp = getUtilization();
					if (!"".equalsIgnoreCase(temp)) {
						if (xml_str == null) {
							//CreditCard.mLogger.info("RLOS COMMON"+ " before adding bhrabc"+ xml_str);
							xml_str = "";
						}
						xml_str = xml_str + temp;
						//CreditCard.mLogger.info("RLOS COMMON"+" after ExternalBureauPipelineProducts+ " + xml_str);
						int_xml.put(parent_tag, xml_str);
					}
				}
				else if ("History_24months".equalsIgnoreCase(tag_name)) {

					String xml_str = int_xml.get(parent_tag);
					PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding ExternalBureauPipelineProducts+ "+ xml_str);
					String temp =getHistory();
					if (!"".equalsIgnoreCase(temp)) {
						if (xml_str == null) {
							//CreditCard.mLogger.info("RLOS COMMON"+ " before adding bhrabc"+ xml_str);
							xml_str = "";
						}
						xml_str = xml_str + temp;
						//CreditCard.mLogger.info("RLOS COMMON"+" after ExternalBureauPipelineProducts+ " + xml_str);
						int_xml.put(parent_tag, xml_str);
					}
				}
				//changes by aastha ends for sprint 4 iteration 3
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
	public String getSIDetails(){
		PersonalLoanS.mLogger.info("RLOSCommon java file inside getSIDetails : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String  add_xml_str ="";
		String listViewname="cmplx_CC_Loan_cmplx_si";
		try{
			if(formObject.isVisible("CC_Loan_Frame2")){
				for(int chosenRow =0 ;chosenRow<formObject.getLVWRowCount(listViewname);chosenRow++ )
				{
					String Selected_card=formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid", formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"), 5);
					String Service_for_Card=formObject.getNGValue(listViewname, chosenRow, 10);
					if(!Selected_card.equals(Service_for_Card))
					{
						continue;
					}
				
					Common_Utils common=new Common_Utils(PersonalLoanS.mLogger);
					add_xml_str = add_xml_str + "<PaymentInstruction><TypeIndicator>SI</TypeIndicator>";
					add_xml_str = add_xml_str + "<Mode>"+formObject.getNGValue(listViewname, chosenRow, 1)+"</Mode>";
					add_xml_str = add_xml_str + "<FlatAmount>"+formObject.getNGValue(listViewname, chosenRow, 4)+"</FlatAmount>";
					add_xml_str = add_xml_str + "<Percentage>"+formObject.getNGValue(listViewname, chosenRow, 3)+"</Percentage>";
					add_xml_str = add_xml_str + "<ExecutionDay>"+formObject.getNGValue(listViewname, chosenRow, 6)+"</ExecutionDay>";
					add_xml_str = add_xml_str + "<StartDate>"+common.Convert_dateFormat(formObject.getNGValue(listViewname, chosenRow, 5), "dd/MM/yyyy","yyyy-MM-dd")+"</StartDate>";// Modified by Rajan for PCASP-2295
					add_xml_str = add_xml_str + "<ExecuteOnDueDateFlag>"+formObject.getNGValue(listViewname, chosenRow, 2).substring(0, 1)+"</ExecuteOnDueDateFlag>";
					add_xml_str = add_xml_str + "<DebitAccountNum>"+formObject.getNGValue(listViewname, chosenRow, 0)+"</DebitAccountNum></PaymentInstruction>";
				
				}
			}
			return add_xml_str;
		}
		catch(Exception ex){
		/*	new CC_Common();*/
		//Commented for sonar
			//PersonalLoanS.mLogger.info("Exception Occure in getDDSDetails()"+ex.getMessage()+CC_Common.printException(ex));
			return add_xml_str;
		}
	}
	private Map<String, String> NEW_LOAN_REQ_Custom( List<List<String>> OutputXML,FormReference formObject,String callName) {
		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();

		try{
			for (List<String> mylist : OutputXML) {
				for (int i = 0; i < 8; i++) {
					//PersonalLoanS.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}

				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");
				if("ApplicationID".equalsIgnoreCase(tag_name) ){
					PersonalLoanS.mLogger.info("inside 1st if"+"inside customer update req1");
					String[] winum = formObject.getWFWorkitemName().split("-");
					String xml_str = int_xml.get(parent_tag);
					int wi_num_forLoan;
					try{
						wi_num_forLoan = Integer.parseInt(winum[1]);
						xml_str = "<"+tag_name+">"+wi_num_forLoan+"</"+ tag_name+">";
					}
					catch(Exception e){
						xml_str = "<"+tag_name+">"+winum[1]+"</"+ tag_name+">";
					}


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
	
	public String getPromotionDetails(){
		String promo = "<PromotionDetails><PromotionType>Health Insurance</PromotionType><PromotionValue>Y</PromotionValue></PromotionDetails>";
		return promo;
	}

	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Generate Input XML for CARD NOTIFICATION

	 ***********************************************************************************  */

	private Map<String, String> CARD_NOTIFICATION_Custom( List<List<String>> OutputXML,FormReference formObject,String callName,String Operation_name) {
		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();

		try{
			for (List<String> mylist : OutputXML) {
				for (int i = 0; i < 8; i++) {
					//PersonalLoanS.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}

				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");
				String form_control = recordFileMap.get("form_control");
				String Default_value = recordFileMap.get("default_val");
				PersonalLoanS.mLogger.info("CC_Integration_Input"+" CARD SERVICE custom function parent_tag: "+parent_tag+" tag_name: "+tag_name);

				if(tag_name.equalsIgnoreCase("ResidencyStatus")){
					PersonalLoanS.mLogger.info("inside 1st if inside customer update req1");
					String xml_str = int_xml.get(parent_tag);//by shweta 
					String residencyStatus=((formObject.getNGValue("cmplx_Customer_ResidentNonResident").equals("Y") && ",AE,BH,IQ,KW,OM,QA,SA".contains(","+formObject.getNGValue("cmplx_Customer_Nationality")))?"GCC":"EXPAT");
					xml_str = xml_str+"<"+tag_name+">"+residencyStatus+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("CC COMMON  after adding ResidencyStatus:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}

				else if("ReferenceDetails".equalsIgnoreCase(tag_name)){
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
				else if("CorpCIFID".equalsIgnoreCase(tag_name) && "BTC".equalsIgnoreCase(formObject.getNGValue("Sub_Product"))){
					String Comp_cif_query ="select ISnull(companyCIF,'') as companyCIF from NG_RLOS_GR_CompanyDetails where comp_winame = '"+formObject.getWFWorkitemName()+"' and applicantCategory = 'Business'";
					List<List<String>> result = formObject.getDataFromDataSource(Comp_cif_query);
					String Comp_cif="";
					if(result!=null && result.size()>0){
						Comp_cif = result.get(0).get(0);
					}
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<"+tag_name+">"+Comp_cif+"</"+tag_name+">";
					int_xml.put(parent_tag, xml_str);
				}
				else if(tag_name.equalsIgnoreCase("OtherIncomeDetails")){
					try{
						PersonalLoanS.mLogger.info("inside OtherIncomeDetails");
						String assessedIncome = formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalTai");
						String grossSal = formObject.getNGValue("cmplx_IncomeDetails_grossSal");
						PersonalLoanS.mLogger.info("inside OtherIncomeDetails assessedIncome: "+assessedIncome);
						PersonalLoanS.mLogger.info("inside OtherIncomeDetails grossSal: "+grossSal);

						String xml_str = int_xml.get(parent_tag);
						//CreditCard.mLogger.info("inside OtherIncomeDetails before tag addition  xml_str: "+xml_str);
						String empType = "Salaried";
						PersonalLoanS.mLogger.info("inside OtherIncomeDetails empType: "+empType);

						if(assessedIncome!=null && !"".equals(assessedIncome)){
							if(assessedIncome.contains(",")){
								assessedIncome=	assessedIncome.replace(",", "");
							}
							assessedIncome = String.valueOf(Float.parseFloat(assessedIncome));  
							xml_str = xml_str + "<OtherIncomeDetails><IncomeType>AssessedIncome</IncomeType><IncomeAmount>"+assessedIncome+"</IncomeAmount><IncomeSource>Salaried</IncomeSource></OtherIncomeDetails>";
						}
						PersonalLoanS.mLogger.info("inside OtherIncomeDetails AssessedIncome: "+xml_str);
						if(grossSal!=null && !"".equals(grossSal)){
							if(grossSal.contains(",")){
								grossSal=	assessedIncome.replace(",", "");
							}
							grossSal = String.valueOf(Float.parseFloat(grossSal));
							xml_str = xml_str + "<OtherIncomeDetails><IncomeType>GrossIncomeForSalaried</IncomeType><IncomeAmount>"+grossSal+"</IncomeAmount><IncomeSource>Salaried</IncomeSource></OtherIncomeDetails>";
						}
						PersonalLoanS.mLogger.info("inside OtherIncomeDetails GrossIncomeForSalaried: "+xml_str);
						int_xml.put(parent_tag, xml_str);
						//CreditCard.mLogger.info("inside OtherIncomeDetails after tag addition  xml_str: "+xml_str);
					}
					catch(Exception e){
						PersonalLoanS.mLogger.info("Exception occured inside OtherIncomeDetails creation");
						PersonalLoanS.mLogger.info("CC_INtegration Exception OtherIncomeDetails custom function OtherIncomeDetails tag:  "+e.getMessage());
					}
				}
				//Deepak Changes done on 24 Aud for PCAS-2516
				else if("CombinedCreditLimit".equalsIgnoreCase(tag_name) ){
					String listView = "cmplx_CCCreation_cmplx_CCCreationGrid";
					String selectproduct_value = formObject.getNGValue(listView,formObject.getSelectedIndex(listView),5);
					List<List<String>> prd_type = formObject.getNGDataFromDataCache("select top 1 ReqProduct From ng_master_cardproduct with(nolock) where CODE ='"+selectproduct_value+"'");
					String tag_val="";
					if( CombinedLimit_Eligibility(selectproduct_value)){
						tag_val="0";
					}
					else{
						tag_val=formObject.getNGValue(form_control);
					}
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<"+tag_name+">"+tag_val+"</"+tag_name+">";
					int_xml.put(parent_tag, xml_str);

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
				else if("AverageBalance".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("inside 1st if inside AverageBalance tag card notif");
					String xml_str = int_xml.get(parent_tag);
					String avgBal = "";
					try{
						String query = "select case when (select COUNT(Child_Wi) from ng_rlos_IGR_Eligibility_CardProduct with(nolock) where Child_Wi = '"+formObject.getWFWorkitemName()+"' and (Deviation_Code_Refer like '%RC044%' or Deviation_Code_Refer like '%RC045%'))>0 then total_avg_6  else total_avg_last1year  end from NG_RLOS_FinacleCore with(nolock) where wi_name = '"+formObject.getWFWorkitemName()+"'";


						List<List<String>> result = formObject.getDataFromDataSource(query);
						if(result!=null && result.size()>0){
							avgBal = result.get(0).get(0);
						}

						xml_str = xml_str+"<"+tag_name+">"+avgBal+"</"+ tag_name+">";
					}
					catch(Exception ex){
						xml_str = xml_str+"<"+tag_name+">"+avgBal+"</"+ tag_name+">";
					}
					PersonalLoanS.mLogger.info("CC COMMON  after adding ApplicationNumber:  "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}

				else if("MonthlyCrTrnOvrAmt".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("inside 1st if inside MonthlyCrTrnOvrAmt tag card notif");
					String xml_str = int_xml.get(parent_tag);
					String avgBal = "";
					try{
						String query = "select avg_credit_6month from NG_RLOS_FinacleCore with(nolock) where wi_name = '"+formObject.getWFWorkitemName()+"'";
						List<List<String>> result = formObject.getDataFromDataSource(query);
						if(result!=null && result.size()>0){
							avgBal = result.get(0).get(0);
						}
						xml_str = xml_str+"<"+tag_name+">"+avgBal+"</"+ tag_name+">";
					}
					catch(Exception ex){
						xml_str = xml_str+"<"+tag_name+">"+avgBal+"</"+ tag_name+">";
					}
					PersonalLoanS.mLogger.info("CC COMMON  after adding MonthlyCrTrnOvrAmt:  "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else if(tag_name.equalsIgnoreCase("ApplicationNumber") || tag_name.equalsIgnoreCase("InstitutionID")){
					PersonalLoanS.mLogger.info("inside 1st if inside customer update req1");
					String xml_str = int_xml.get(parent_tag);
					try{
						xml_str = xml_str+"<"+tag_name+">"+Integer.parseInt(formObject.getWFWorkitemName().split("-")[1])+"</"+ tag_name+">";
					}
					catch(Exception ex){
						xml_str = xml_str+"<"+tag_name+">"+formObject.getWFWorkitemName().split("-")[1]+"</"+ tag_name+">";
					}
					//CreditCard.mLogger.info("CC COMMON  after adding ApplicationNumber:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}
				else if("InterestProfile".equalsIgnoreCase(tag_name)){
					String xml_str = int_xml.get(parent_tag);
					String cardProd = formObject.getNGValue("CC_Creation_Product");
					int CRNgridRowCount = formObject.getLVWRowCount("cmplx_CardDetails_cmplx_CardCRNDetails");
					String intProf = "";
					for(int i=0;i<CRNgridRowCount;i++){
						if(formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",i,0).equalsIgnoreCase(cardProd)){
							intProf = formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",i,4);
							break;
						}
					}
					xml_str = xml_str + "<"+tag_name+">"+intProf+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding intProf+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else if("FeeProfile".equalsIgnoreCase(tag_name)){
					String xml_str = int_xml.get(parent_tag);
					String cardProd = formObject.getNGValue("CC_Creation_Product");
					int CRNgridRowCount = formObject.getLVWRowCount("cmplx_CardDetails_cmplx_CardCRNDetails");
					String feeProf = "";
					for(int i=0;i<CRNgridRowCount;i++){
						if(formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",i,0).equalsIgnoreCase(cardProd)){
							feeProf = formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",i,5);
							break;
						}
					}
					xml_str = xml_str + "<"+tag_name+">"+feeProf+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding feeProf+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else if("TransactionFeeProfile".equalsIgnoreCase(tag_name)){
					String xml_str = int_xml.get(parent_tag);
					String cardProd = formObject.getNGValue("CC_Creation_Product");
					int CRNgridRowCount = formObject.getLVWRowCount("cmplx_CardDetails_cmplx_CardCRNDetails");
					String transProf = "";
					for(int i=0;i<CRNgridRowCount;i++){
						if(formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",i,0).equalsIgnoreCase(cardProd)){
							transProf = formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",i,3);
							break;
						}
					}
					xml_str = xml_str + "<"+tag_name+">"+transProf+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding transProf+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else if(tag_name.equalsIgnoreCase("CardType")){
					//CreditCard.mLogger.info("inside getting cardtype from DB");
					String xml_str = int_xml.get(parent_tag);
					String cardProd = formObject.getNGValue("CC_Creation_Product");
					String Ctype ="";
					String Query="select distinct CARDTYPE from ng_master_cardProduct with(nolock) where CODE = '"+cardProd+"'";
					PersonalLoanS.mLogger.info("Query for getting Card type from DB is: "+Query);
					List<List<String>> intsert_list = formObject.getNGDataFromDataCache(Query);
					PersonalLoanS.mLogger.info("result from DB is: "+intsert_list);
					if(!intsert_list.isEmpty()){
						Ctype = intsert_list.get(0).get(0);
					}	
					xml_str = xml_str+"<"+tag_name+">"+Ctype+"</"+ tag_name+">";
					PersonalLoanS.mLogger.info("RLOS COMMON after adding cardtype getting from DB "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				/*else if(tag_name.equalsIgnoreCase("InterestProfile")||tag_name.equalsIgnoreCase("FeeProfile")||tag_name.equalsIgnoreCase("TransactionFeeProfile")){
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
									intrest_Query="select code from NG_MASTER_Int_Profile with(nolock) where Product = '"+card_product+"'";
								}
								else if(tag_name.equalsIgnoreCase("FeeProfile")||tag_name.equalsIgnoreCase("FeeProfileSerNo")){
									intrest_Query="select code from NG_MASTER_Fee_Profile with(nolock) where Product = '"+card_product+"'";
								}
								else if(tag_name.equalsIgnoreCase("TransactionFeeProfile")||tag_name.equalsIgnoreCase("TxnProfileSerNo")){
									intrest_Query="select code from NG_MASTER_TransactionFee_Profile with(nolock) where Product = '"+card_product+"'";
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
				}*/

				else if(tag_name.equalsIgnoreCase("MarketingCode")){
					try{
						String xml_str = int_xml.get(parent_tag);
						String emptype = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5);
						String mktCode = "";
						if(emptype.equalsIgnoreCase("Self Employed")){
							int rowCount = formObject.getLVWRowCount("cmplx_CompanyDetails_cmplx_CompanyGrid");
							if(rowCount>0){
								for(int i=0;i<rowCount;i++){
									if("Business".equalsIgnoreCase(formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i,1))){
										mktCode = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",i,28);
										break;
									}
								}
							}

						}
						else{
							mktCode = formObject.getNGValue("cmplx_CardDetails_MarketCode");
						}
						xml_str = xml_str+"<"+tag_name+">"+mktCode+"</"+ tag_name+">";
						int_xml.put(parent_tag, xml_str);
					}catch(Exception ex){
						PersonalLoanS.mLogger.info("Exception in MarketingCode tag: ");
						new PLCommon().printException(ex);
					}
				}

				else if(tag_name.equalsIgnoreCase("CustSegment")){
					//CreditCard.mLogger.info("inside getting cardtype from DB");
					try{
						String xml_str = int_xml.get(parent_tag);
						String emptype = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5);
						//Deepak Change for PCASI-3026
						String tSegcode = "PLBUN";
						/*if(emptype.equalsIgnoreCase("Self Employed")){
							int rowCount = formObject.getLVWRowCount("cmplx_CompanyDetails_cmplx_CompanyGrid");
							if(rowCount>0){
								for(int i=0;i<rowCount;i++){
									if("Business".equalsIgnoreCase(formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i,1 ))){
										tSegcode = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",i,22);
										break;
									}
								}
							}

						}
						else{
							tSegcode = formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode");
						}*/
						xml_str = xml_str+"<"+tag_name+">"+tSegcode+"</"+ tag_name+">";
						int_xml.put(parent_tag, xml_str);
					}catch(Exception ex){
						PersonalLoanS.mLogger.info("Exception in CustSegment tag: ");
						new PLCommon().printException(ex);
					}

					//CreditCard.mLogger.info("RLOS COMMON after adding cardtype getting from DB "+xml_str);

				}



				else if(tag_name.equalsIgnoreCase("BlackCheckComp")){
					//CreditCard.mLogger.info("inside getting cardtype from DB");
					try{
						String xml_str = int_xml.get(parent_tag);
						boolean blacklistFlag = false;
						int rowCount = formObject.getLVWRowCount("cmplx_FinacleCRMCustInfo_FincustGrid");
						if(rowCount>0){
							for(int i=0;i<rowCount;i++){
								if("Y".equalsIgnoreCase(formObject.getNGValue("cmplx_FinacleCRMCustInfo_FincustGrid",i,7))){
									blacklistFlag = true;
									break;
								}
							}
						}
						if(blacklistFlag){
							xml_str = xml_str+"<"+tag_name+">"+"Y"+"</"+ tag_name+">";	
						}
						else{
							xml_str = xml_str+"<"+tag_name+">"+"N"+"</"+ tag_name+">";
						}

						int_xml.put(parent_tag, xml_str);
					}catch(Exception ex){
						PersonalLoanS.mLogger.info("Exception in BlackCheckComp tag: ");
						new PLCommon().printException(ex);
					}
				}

				else if(tag_name.equalsIgnoreCase("isDedupFound")){
					//CreditCard.mLogger.info("inside getting cardtype from DB");
					try{
						String xml_str = int_xml.get(parent_tag);

						int rowCount = formObject.getLVWRowCount("cmplx_PartMatch_cmplx_Partmatch_grid");
						if(rowCount>0){
							xml_str = xml_str+"<"+tag_name+">"+"Y"+"</"+ tag_name+">";
						}
						else{
							xml_str = xml_str+"<"+tag_name+">"+"N"+"</"+ tag_name+">";	
						}
						int_xml.put(parent_tag, xml_str);
					}catch(Exception ex){
						PersonalLoanS.mLogger.info("Exception in isDedupFound tag: ");
						new PLCommon().printException(ex);
					}
				}
				//Added By Shivang for pcasp-2307,2308  
				else if("CustClassCode".equalsIgnoreCase(tag_name)){
					String emptype = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5);
					PersonalLoanS.mLogger.info("emptype: "+emptype);
					String xml_str;
					try{
						xml_str = int_xml.get(parent_tag);
						if("Self Employed".equalsIgnoreCase(emptype) && "Primary".equalsIgnoreCase(Operation_name)){						    
							xml_str += "<CustClassCode>"+formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",1,29)+"</CustClassCode>";																
						}
						else{
							xml_str += "<CustClassCode>"+formObject.getNGValue("cmplx_CardDetails_CustClassification")+"</CustClassCode>";
						}
						int_xml.put(parent_tag, xml_str);
						PersonalLoanS.mLogger.info("RLOS COMMON"+" CustClass Code:+ "+xml_str);
					}catch(Exception ex){
						PersonalLoanS.mLogger.info("PersonalLoan custclassification ::"+PLCommon.returnException(ex));
					}
				}
				//Added by shivang to send relationship under CardInfo in case of Self & Supplementary,PCASP-2501
				else if("Relationship".equalsIgnoreCase(tag_name) ){
					String xml_str;
					String relationship="";
					try{
						xml_str = int_xml.get(parent_tag);
						if(callName.equalsIgnoreCase("CARD_NOTIFICATION") && "Supplement".equalsIgnoreCase(Operation_name)){
							if( "SUPPLEMENT".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)) && formObject.getLVWRowCount("SupplementCardDetails_cmplx_supplementGrid")>0){ 
								for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_supplementGrid");i++){
									if(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13).equals(formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,3))){

										relationship= formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,13);
										break;
									}
								}
							}
						}
						else if(callName.equalsIgnoreCase("CARD_NOTIFICATION") && "PRIMARY".equalsIgnoreCase(Operation_name)){
							if("SUPPLEMENT".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12))){ 							
								if(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13).equals(formObject.getNGValue("cmplx_Customer_PAssportNo"))){									
									relationship= "SELF";
								}							
							}
						}
						xml_str += "<Relationship>"+relationship+"</Relationship>";
						int_xml.put(parent_tag, xml_str);
						//CreditCard.mLogger.info("RLOS COMMON"+" relationship :+ "+xml_str);
					}catch(Exception ex){
						PersonalLoanS.mLogger.info("Exception in CustClassCode tag: "+ex.getMessage());
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

				else if(tag_name.equalsIgnoreCase("EmploymentDetails")){
					try{
						if(int_xml.containsKey(parent_tag))
						{
							//CreditCard.mLogger.info("inside 1st if"+"inside customer update req2");
							String xml_str = int_xml.get(parent_tag);
							//CreditCard.mLogger.info("RLOS COMMON"+" before contact details+ "+xml_str);
							String emptype = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5);
							if(emptype.equalsIgnoreCase("Self Employed") && "Primary".equalsIgnoreCase(Operation_name)){
								PersonalLoanS.mLogger.info("case 1:");
								xml_str = xml_str + getEmp_details(true,true);
							}
							else if(!emptype.equalsIgnoreCase("Self Employed") && "Primary".equalsIgnoreCase(Operation_name)){
								PersonalLoanS.mLogger.info("case 2:");
								xml_str = xml_str + getEmp_details(false,true);
							}
							else if(emptype.equalsIgnoreCase("Self Employed") && "Supplement".equalsIgnoreCase(Operation_name)){
								PersonalLoanS.mLogger.info("case 3:");
								xml_str = xml_str + getEmp_details(true,false);
							}
							else if(!emptype.equalsIgnoreCase("Self Employed") && "Supplement".equalsIgnoreCase(Operation_name)){
								PersonalLoanS.mLogger.info("case 4:");
								xml_str = xml_str + getEmp_details(false,false);
							}

							//CreditCard.mLogger.info("RLOS COMMON"+" after adding contact details+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}
					}catch(Exception ex){
						PersonalLoanS.mLogger.info("Exception in isDedupFound tag: ");
						new PLCommon().printException(ex);
					}
				}
				//Added by shivang for PCASP-2357
				else if("BusinessInfo".equalsIgnoreCase(tag_name) && "CARD_NOTIFICATION".equalsIgnoreCase(callName) && "PRIMARY".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12))){
					try{
						if(int_xml.containsKey(parent_tag))
						{
							String xml_str = int_xml.get(parent_tag);
							xml_str = xml_str+ "<BusinessInfo><MonthlyCrTrnOvrAmt>"+formObject.getNGValue("cmplx_FinacleCore_avg_credit_3month")+"</MonthlyCrTrnOvrAmt></BusinessInfo>";
							int_xml.put(parent_tag, xml_str);
						}
					}catch(Exception ex){
						PersonalLoanS.mLogger.info("Exception in BusinessInfo aggregate tag: "+ex.getMessage());
					}					
				}
				else if("ApplicationDate".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("inside 1st if inside card_notification req1");
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

				else if(tag_name.equalsIgnoreCase("ApplicationNumber") || tag_name.equalsIgnoreCase("InstitutionID")){
					PersonalLoanS.mLogger.info("inside 1st if inside customer update req1");
					String xml_str = int_xml.get(parent_tag);
					try{
						xml_str = xml_str+"<"+tag_name+">"+Integer.parseInt(formObject.getWFWorkitemName().split("-")[1])+"</"+ tag_name+">";
					}
					catch(Exception ex){
						xml_str = xml_str+"<"+tag_name+">"+formObject.getWFWorkitemName().split("-")[1]+"</"+ tag_name+">";
					}
					PersonalLoanS.mLogger.info("CC COMMON  after adding ApplicationNumber:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}
				//++below code added by nikhil for Self-Supp CR
				else if("ApplicationType".equalsIgnoreCase(tag_name)){
					String xml_str = int_xml.get(parent_tag);
					String Grid_Name="cmplx_CCCreation_cmplx_CCCreationGrid";
					int selectedindex=formObject.getSelectedIndex(Grid_Name);
					String App_type=formObject.getNGValue(Grid_Name,selectedindex,12);
					if(!App_type.equalsIgnoreCase("Primary"))
					{
						App_type="Secondary";
					}					
					xml_str =xml_str+ "<"+tag_name+">"+App_type+"</"+ tag_name+">";


					int_xml.put(parent_tag, xml_str);	                            	
				}
				//--above code added by nikhil for Self-Supp CR

				//tag added by saurabh on 8th Feb 19 for default value points.
				else if("DSAId".equalsIgnoreCase(tag_name)){
					try{
						PersonalLoanS.mLogger.info("inside 1st DSAId card_notification req1");
						String xml_str = int_xml.get(parent_tag);

						String branchUserQuery = "select Introduce_By from NG_RLOS_EXTTABLE where WIname = (select parent_WIName from NG_PL_EXTTABLE where PL_Wi_Name='"+formObject.getWFWorkitemName()+"')";
						List<List<String>> result = formObject.getDataFromDataSource(branchUserQuery);
						if(null!=result && !result.isEmpty()){
							if(null!=result.get(0) && !result.get(0).isEmpty()){
								xml_str =xml_str+ "<"+tag_name+">"+result.get(0).get(0)+"</"+ tag_name+">";
							}
						}
						PersonalLoanS.mLogger.info("PL COMMON  after adding DSAId:  "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}catch(Exception ex){
						PersonalLoanS.mLogger.info("inside exception in DSAId: ");
						new PLCommon().printException(ex);
					}
				}
				//Deepak - 28july 2019 code added for CardRefNumber
				else if("CardRefNumber".equalsIgnoreCase(tag_name) ){
					//CreditCard.mLogger.info("inside 1st if"+" inside CardRefNumber for Card NOTIF");
					String tagValue=getCardRefNumbervalue();
					String xml_str =  "<"+tag_name+">"+tagValue+"</"+ tag_name+">";
					int_xml.put(parent_tag, xml_str);
					//CreditCard.mLogger.info("inside CardRefNumber for Card NOTIF: CardRefNumber"+ xml_str);
				}
				else if("SubscriptionFlag".equalsIgnoreCase(tag_name) && "SubscriptionInfo".equalsIgnoreCase(parent_tag) ){
					PersonalLoanS.mLogger.info("inside RefValue for cmplx_CardDetails_SMS_Output for Card Notification: "+ form_control);
					String xml_str = int_xml.get(parent_tag);
					String tagValue=(formObject.getNGValue(form_control).equalsIgnoreCase("true")?"Y":"N");
					xml_str = xml_str+ "<"+tag_name+">"+tagValue+"</"+ tag_name+">";
					//CreditCard.mLogger.info("inside RefValue for cmplx_CardDetails_SMS_Output for Card Notification xml_str: "+ xml_str);
					int_xml.put(parent_tag, xml_str);
					//CreditCard.mLogger.info("inside CardRefNumber for Card NOTIF: CardRefNumber"+ xml_str);
				}
				else if("preferredCharityorg".equalsIgnoreCase(tag_name) || "Donationamount".equalsIgnoreCase(tag_name)){
					try{
						//CreditCard.mLogger.info("inside preferredCharityorg if");
						String xml_str = int_xml.get(parent_tag);
						String card_prod = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),5);
						String squery="select top 1 ReqProduct from ng_master_cardProduct where CODE = '"+card_prod+"' and EmployerCategory = 'Salaried'";
						//CreditCard.mLogger.info("squery: "+squery);
						List<List<String>> typeOfCP = formObject.getDataFromDataSource(squery);
						//CreditCard.mLogger.info("squery output: "+typeOfCP);
						if(typeOfCP!=null && typeOfCP.size()>0 && typeOfCP.get(0)!=null && "Islamic".equalsIgnoreCase(typeOfCP.get(0).get(0))){
							//CreditCard.mLogger.info("inside preferredCharityorg Islamic"); 
							if("preferredCharityorg".equalsIgnoreCase(tag_name)){
								xml_str =  xml_str+ "<"+tag_name+">5</"+ tag_name+">";
							}
							else if("Donationamount".equalsIgnoreCase(tag_name)){
								xml_str =  xml_str+ "<"+tag_name+">0</"+ tag_name+">";
							}
							//CreditCard.mLogger.info("inside preferredCharityorg updated xml_str: "+xml_str);
						}
						int_xml.put(parent_tag, xml_str);
					}
					catch(Exception e){
						PersonalLoanS.mLogger.info("Exception occured for preferredCharityorg or Donationamount "+ e.getMessage());
					}

				}


				else if( "PRIMARY".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)))
				{
					if("RVCStatus".equalsIgnoreCase(tag_name)){
						String xml_str = int_xml.get(parent_tag);
						String RVC_status = (formObject.getNGValue(form_control).equalsIgnoreCase("true")?"Y":"N");;

						xml_str = xml_str + "<"+tag_name+">"+RVC_status+"</"+ tag_name+">";

						PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding RVCStatus+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}
					//changes by saurabh on 
					else if("DocId".equalsIgnoreCase(tag_name) && "cmplx_Customer_EmiratesID".equalsIgnoreCase(form_control)){
						String xml_str = int_xml.get(parent_tag);
						if(null!= formObject.getNGValue("cmplx_Customer_NEP") && !"--Select--".equals(formObject.getNGValue("cmplx_Customer_NEP")) && !"".equals(formObject.getNGValue("cmplx_Customer_NEP"))){
							form_control = "cmplx_Customer_EIDARegNo";
						}
						xml_str = xml_str + "<"+tag_name+">"+formObject.getNGValue(form_control)+"</"+ tag_name+">";
						PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding Emirates ID+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}
					else if("DispatchMode".equalsIgnoreCase(tag_name)){
						//String xml_str = int_xml.get(parent_tag);
						String Dispatch_details = formObject.getNGValue("AlternateContactDetails_carddispatch");
						String Branch_details="";
						if(Dispatch_details.equalsIgnoreCase("ByBranch")){
							Branch_details = "<BranchDetails><BranchCode>"+formObject.getNGValue("AlternateContactDetails_CustomerDomicileBranch")+"</BranchCode></BranchDetails>";
						}
						String xml_str =  "<"+tag_name+">"+getDispatchChannel(Dispatch_details)+"</"+ tag_name+">"+Branch_details;

						//String xml_str =  "<"+tag_name+">"+Dispatch_details+"</"+ tag_name+">"+Branch_details;

						PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding RVCStatus+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}
					else if("CustomerNameOnCard".equalsIgnoreCase(tag_name)){
						String xml_str = int_xml.get(parent_tag);
						String tagValue="";
						//CreditCard.mLogger.info("PL COMMON  before adding isNTB:  "+xml_str);
						if("PRIMARY".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)) )
						{
							tagValue=formObject.getNGValue("cmplx_CardDetails_cardemboss");//Wrong field ID
						}
						else 
						{
							tagValue=formObject.getNGValue("cmplx_CardDetails_Self_card_embossing");
						}
						xml_str = xml_str + "<"+tag_name+">"+tagValue+"</"+ tag_name+">";
						int_xml.put(parent_tag, xml_str);
						PersonalLoanS.mLogger.info("after adding CustomerNameOnCard for primary:  "+xml_str);
					}
					else if("ProductInfoTags".equalsIgnoreCase(tag_name)){
						String xml_str = int_xml.get(parent_tag);
						String card_prod = formObject.getNGValue("CC_Creation_Product");
						String card_intrest_profile = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),11);
						PersonalLoanS.mLogger.info("inside ProductInfoTags card_prod: "+ card_prod);
						//Deepak 25 dec Changes done for Arabia
						if(card_intrest_profile.contains("Arabia")){
							xml_str = xml_str+ "<ProductInfo><ProductName>AirArabia</ProductName><ProductId>"+formObject.getNGValue("AlternateContactDetails_AirArabiaIdentifier")+"</ProductId></ProductInfo>";
						}
						else if(card_intrest_profile.toUpperCase().contains("SKYWARDS")){
							String ProductInfo_str = getFalcon_prdInfo("Primary");
							xml_str = xml_str+ ProductInfo_str;
						}
						//Deepak changes done for kalyan card
						if(card_intrest_profile.contains("KALYAN")){
							String kalyan_no = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),14);
							PersonalLoanS.mLogger.info("RLOS COMMON after adding kalyan_no: + "+kalyan_no);
							String LinkageType_refval = getLinkageType_refval(kalyan_no);
							xml_str = xml_str+ "<ProductInfo><ProductName>Kalyan</ProductName><ProductId>"+kalyan_no+"</ProductId><ProductRefInfo><RefType>LinkageType</RefType><RefValue>"+LinkageType_refval+"</RefValue></ProductRefInfo></ProductInfo>";

						}
						//elow code comented by nikhil for Card notification CR
						else if(!card_prod.contains("KALYAN")){
							List<List<String>> typeOfCP = formObject.getDataFromDataSource("select top 1 ReqProduct from ng_master_cardProduct where CODE = '"+card_prod+"' and EmployerCategory = 'Salaried'");
							if(typeOfCP!=null && typeOfCP.size()>0 && typeOfCP.get(0)!=null && "Islamic".equalsIgnoreCase(typeOfCP.get(0).get(0))){
								xml_str =  xml_str+ new Common_Utils(PersonalLoanS.mLogger).murabhaTags(formObject.getWFWorkitemName(), "Murbaha",card_prod);
							}
						}

						else{
							xml_str =  xml_str+ new Common_Utils(PersonalLoanS.mLogger).murabhaTags(formObject.getWFWorkitemName(), "Kalyan",card_prod);
						}
						PersonalLoanS.mLogger.info("RLOS COMMON after adding ProductInfoTags+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}

					/*else if("ProductInfoTags".equalsIgnoreCase(tag_name)){
				String xml_str = int_xml.get(parent_tag);
				String card_prod = formObject.getNGValue("CC_Creation_Product");
				if(!card_prod.contains("KALYAN")){
					List<List<String>> typeOfCP = formObject.getDataFromDataSource("select top 1 ReqProduct from ng_master_cardProduct where CODE = '"+card_prod+"' and Subproduct = '"+formObject.getNGValue("Sub_Product")+"' and EmployerCategory = '"+formObject.getNGValue("EmploymentType")+"'");
					if(typeOfCP!=null && typeOfCP.size()>0 && typeOfCP.get(0)!=null && "Islamic".equalsIgnoreCase(typeOfCP.get(0).get(0))){
						xml_str =  xml_str+ new Common_Utils(PersonalLoanS.mLogger).murabhaTags(formObject.getWFWorkitemName(), "Murabha",card_prod);
					}
				}
				else{
					xml_str =  xml_str+ new Common_Utils(PersonalLoanS.mLogger).murabhaTags(formObject.getWFWorkitemName(), "Kalyan",card_prod);
				}
				PersonalLoanS.mLogger.info("RLOS COMMON after adding ProductInfoTags+ "+xml_str);
				int_xml.put(parent_tag, xml_str);
			}*/
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
							PersonalLoanS.mLogger.info("Rishabh Sarda"+tagValue + '6');
						}
						else if(tag_name.equalsIgnoreCase("NonResidentFlag")){
							//change by saurabh for DROP-4
							tagValue=(formObject.getNGValue("cmplx_Customer_ResidentNonResident").equalsIgnoreCase("true")?"Y":"N");
						}	
						else if(tag_name.equalsIgnoreCase("EStatementFlag")){
							tagValue=(formObject.getNGValue("AlternateContactDetails_ESTATEMENTFLAG").equalsIgnoreCase("Yes")?"Y":"N");
						}

						if(!tagValue.equals("")){
							xml_str = xml_str + "<"+tag_name+">"+tagValue+"</"+ tag_name+">";
						}


						PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}
					else if(tag_name.equalsIgnoreCase("SecurityChequeDetails") && !(new PLCommon().Check_Elite_Customer(formObject))){//by shweta
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


					else if(tag_name.equalsIgnoreCase("SalaryDate")&& formObject.getNGValue("EmploymentType").contains("Salaried") ){
						PersonalLoanS.mLogger.info("inside 1st if inside customer update req1");
						try{
							String xml_str = int_xml.get(parent_tag);
							Calendar now = Calendar.getInstance();
							String month = "";
							int sal_day=0;
							String day = formObject.getNGValue("cmplx_IncomeDetails_SalaryDay");
							//Changes done Deepak for salary day change
							try{
								sal_day = Integer.parseInt(day);
								int mon_lastday = now.getActualMaximum(Calendar.DAY_OF_MONTH);
								if (sal_day>mon_lastday){
									sal_day=mon_lastday;
								}
							}
							catch(Exception e){
								PersonalLoanS.mLogger.info("Exception occured in SalaryDate:  "+e.getMessage());
								sal_day=5;
							}
							if((now.get(Calendar.MONTH) + 1)<10){
								month = "0"+(now.get(Calendar.MONTH) + 1);
							}else{
								month = ""+(now.get(Calendar.MONTH) + 1);
							}
							if(sal_day<10){
								day = "0" + sal_day;
							}else{
								day = ""+sal_day;
							}


							String Current_date="";

							Current_date = now.get(Calendar.YEAR)+"-"+month+"-"+day;
							xml_str = xml_str+"<"+tag_name+">"+Current_date+"</"+ tag_name+">";

							PersonalLoanS.mLogger.info("PL COMMON  after adding ApplicationID:  "+xml_str);
							int_xml.put(parent_tag, xml_str);	     
						}catch(Exception ex){
							plcommonObj.printException(ex);
						}
					}

					//code changes by bandana starts
					//added by akshay on 20/3/18
					/*else if(tag_name.equalsIgnoreCase("ExecutionDay")){
					String xml_str = int_xml.get(parent_tag);
					String day = "";
					if(formObject.getNGValue("cmplx_CC_Loan_DDSExecDay")!=null && !"".equals(formObject.getNGValue("cmplx_CC_Loan_DDSExecDay"))){
						day = formObject.getNGValue("cmplx_CC_Loan_DDSExecDay").substring(0,2);
					}
					xml_str = xml_str+"<"+tag_name+">"+day+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("PL COMMON  after adding ExecutionDay:  "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}*/
					//code changes by bandana ends

					else if(tag_name.equalsIgnoreCase("MinorFlag") ){
						if(int_xml.containsKey(parent_tag))
						{	//change from int to float by saurabh on 16th dec
							float Age = Float.parseFloat(formObject.getNGValue("cmplx_Customer_age"));
							String age_flag = "N";
							if(Age<21)
								age_flag="Y";
							String xml_str = int_xml.get(parent_tag);
							xml_str = xml_str + "<"+tag_name+">"+age_flag
									+"</"+ tag_name+">";

							PersonalLoanS.mLogger.info("RLOS COMMON  after adding Minor flag+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}		                            	
					}
//by jahnavi for BT/CCC card notification call
					else if (tag_name.equalsIgnoreCase("AddOnServices") && parent_tag.equalsIgnoreCase("CardDetails"))
					{
				if(int_xml.containsKey(parent_tag))
				{						
					String xml_str = int_xml.get(parent_tag);
					//CreditCard.mLogger.info("Cc_integration_input before adding financialservices+ "+xml_str);
					xml_str = xml_str + "<AddOnServices>";
					xml_str = xml_str + getFinancialServices_details(callName);
					xml_str = xml_str + getRVCDetails();
					xml_str = xml_str + getDDSDetails();
					xml_str = xml_str + getSIDetails();//PCASP-2797
					xml_str = xml_str + getPromotionDetails();
					//CreditCard.mLogger.info("Cc_integration_input after adding financialservices+ "+xml_str);
					xml_str = xml_str + "</AddOnServices>";
					int_xml.put(parent_tag, xml_str);
				}
					}
					//below code by saurabh on 15th Dec 17
			/*		else if(tag_name.equalsIgnoreCase("FinancialServices") ){
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
					else if(tag_name.equalsIgnoreCase("RVC") && parent_tag.equalsIgnoreCase("AddOnServices")){
						//CreditCard.mLogger.info("inside Generate XML inside RVC");
						if(int_xml.containsKey(parent_tag))
						{						
							String xml_str = int_xml.get(parent_tag);
							//CreditCard.mLogger.info("Cc_integration_input before adding RVC+ "+xml_str);
							xml_str = xml_str + getRVCDetails();
							//CreditCard.mLogger.info("Cc_integration_input after adding RVC+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}
					}
					else if(tag_name.equalsIgnoreCase("PaymentInstruction") && parent_tag.equalsIgnoreCase("AddOnServices")){
						//CreditCard.mLogger.info("inside Generate XML inside PaymentInstruction");
						if(int_xml.containsKey(parent_tag))
						{						
							String xml_str = int_xml.get(parent_tag);
							//CreditCard.mLogger.info("Cc_integration_input before adding PaymentInstruction+ "+xml_str);
							xml_str = xml_str + getDDSDetails();
							//CreditCard.mLogger.info("Cc_integration_input after adding PaymentInstruction+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}
					}*/
					else{
						int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
					}
				}
				else if( "SUPPLEMENT".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)))
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
					else if("CustomerNameOnCard".equalsIgnoreCase(tag_name)){
						String xml_str = int_xml.get(parent_tag);
						PersonalLoanS.mLogger.info("RLOS COMMON"+ " before adding guarantor PersonDetails+ " + xml_str);
						xml_str =xml_str+ "<"+tag_name+">"+getSupplementaryTagValue(9)+"</"+ tag_name+">";
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
					//Added by shivang for Sending DispatchDetails in Supp. Prod issue
					else if("DispatchMode".equalsIgnoreCase(tag_name)){
						//String xml_str = int_xml.get(parent_tag);
						String Dispatch_details = formObject.getNGValue("AlternateContactDetails_CardDisp");
						String Branch_details="";
						if(Dispatch_details.equalsIgnoreCase("ByBranch")){
							Branch_details = "<BranchDetails><BranchCode>"+formObject.getNGValue("AlternateContactDetails_CustdomBranch")+"</BranchCode></BranchDetails>";
						}
						String xml_str =  "<"+tag_name+">"+getDispatchChannel(Dispatch_details)+"</"+ tag_name+">"+Branch_details;

						//CreditCard.mLogger.info("RLOS COMMON"+" after adding RVCStatus+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}
					else if(tag_name.equalsIgnoreCase("EStatementFlag") ){
						String xml_str = int_xml.get(parent_tag);
						String tagValue="";
						if(tag_name.equalsIgnoreCase("EStatementFlag")){
							tagValue=(formObject.getNGValue("AlternateContactDetails_ESTATEMENTFLAG").equalsIgnoreCase("Yes")?"Y":"N");
						}
						if(!tagValue.equals("")){
							xml_str = xml_str + "<"+tag_name+">"+tagValue+"</"+ tag_name+">";
						}
						//CreditCard.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}
					else if("CardProductType".equalsIgnoreCase(tag_name)){
						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<"+tag_name+">"+formObject.getNGValue(form_control)+"</"+ tag_name+">";
						String card_intrest_profile = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),11);
						if(card_intrest_profile.toUpperCase().contains("SKYWARDS")){
							String ProductInfo_str = getFalcon_prdInfo("SUPPLEMENT");
							xml_str = xml_str+ ProductInfo_str;
						}
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


	public String getEmp_details(boolean selfEmpcase, boolean primaryApp){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		PersonalLoanS.mLogger.info("RLOSCommon java file inside getEmp_details : ");
		//Done for PCSP-101
		String Designation="";
		try{
			String sQuery = "select Description from ng_master_Designation with (nolock) where code='" +  formObject.getNGValue("cmplx_EmploymentDetails_Designation") + "'";

			//RLOS.mLogger.info("RLOS val change Value of dob is:"+sQuery);

			List<List<String>> recordList = formObject.getNGDataFromDataCache(sQuery);
			//RLOS.mLogger.info("RLOS val change Vasdfsdflue of dob is:"+recordList.get(0).get(0));
			if(recordList.get(0).get(0)!= null && recordList.get(0)!=null && !"".equalsIgnoreCase(recordList.get(0).get(0)) && recordList!=null)
			{
				Designation=recordList.get(0).get(0);
			}
		}
		catch(Exception e){}
		//Done for PCSP-101
		String  Empdetails_xml_str ="";
		try{
			if(!selfEmpcase){
				Empdetails_xml_str +="<EmploymentDetails>";
				try{
					if(primaryApp){
						Empdetails_xml_str += "<EmploymentType>"+formObject.getNGValue("cmplx_EmploymentDetails_Emp_Type")+"</EmploymentType>";
					}
					else{
						Empdetails_xml_str += "<EmploymentType>"+getSuppCardnotifdata("EmploymentType")+"</EmploymentType>";
					}
					if(primaryApp){
						Empdetails_xml_str += "<EmployeeNumber>"+formObject.getNGValue("cmplx_EmploymentDetails_StaffID")+"</EmployeeNumber>";
						Empdetails_xml_str += "<EmployerId>"+formObject.getNGValue("cmplx_EmploymentDetails_EMpCode")+"</EmployerId>";
						Empdetails_xml_str += "<EmployerName>"+formObject.getNGValue("cmplx_EmploymentDetails_EmpName")+"</EmployerName>";
					}
					//Empdetails_xml_str += "<EmploymentStatus>"+formObject.getNGValue("cmplx_EmploymentDetails_EmpStatus")+"</EmploymentStatus>"; commented until mapping received
					if(primaryApp){
						Empdetails_xml_str += "<Designation>"+Designation+"</Designation>";
					}
					else{
						Empdetails_xml_str += "<Designation>"+getSuppCardnotifdata("Designation")+"</Designation>";
					}
					if(primaryApp){
						Empdetails_xml_str += "<Department>"+formObject.getNGValue("cmplx_EmploymentDetails_Dept")+"</Department>";
					}
					if(primaryApp){
						//changed by nikhil 05/01/2018 for Send detail to CAPS error
						Empdetails_xml_str += "<Occupation>"+formObject.getNGValue("cmplx_EmploymentDetails_Designation")+"</Occupation>";
					}
					else{
						Empdetails_xml_str += "<Occupation>"+getSuppCardnotifdata("Occupation")+"</Occupation>";
					}

					if(primaryApp){
						Empdetails_xml_str += "<EmploymentPeriod>"+getEmploymentPeriod()+"</EmploymentPeriod>";
					}
					if(formObject.getNGValue("cmplx_EmploymentDetails_DOJ")!=null && !"".equals(formObject.getNGValue("cmplx_EmploymentDetails_DOJ")) && primaryApp){
						String doj = formObject.getNGValue("cmplx_EmploymentDetails_DOJ");
						if(doj.contains("/")){
							doj = doj.split("/")[2]+"-"+doj.split("/")[1]+"-"+doj.split("/")[0];
						}
						Empdetails_xml_str += "<DateOfJoining>"+doj+"</DateOfJoining>";
					}
					//Changes done by deepak as the tag seq was incorrect for CR 0712
					Empdetails_xml_str += "<OrganisationType>CurrentOrg</OrganisationType><SalaryTransferFlag>"+formObject.getNGValue("cmplx_IncomeDetails_SalaryXferToBank")+"</SalaryTransferFlag>";
					if(formObject.getNGValue("cmplx_IncomeDetails_SalaryDay")!=null && !"".equalsIgnoreCase(formObject.getNGValue("cmplx_IncomeDetails_SalaryDay"))){
						Calendar c = Calendar.getInstance();
						try{

							Calendar now = Calendar.getInstance();
							String month = "";
							String day = "";
							if((now.get(Calendar.MONTH) + 1)<10){
								month = "0"+(now.get(Calendar.MONTH) + 1);
							}else{
								month = ""+(now.get(Calendar.MONTH) + 1);
							}
							if(Integer.parseInt(formObject.getNGValue("cmplx_IncomeDetails_SalaryDay"))<10){
								day = "0" + formObject.getNGValue("cmplx_IncomeDetails_SalaryDay");
							}else{
								day = formObject.getNGValue("cmplx_IncomeDetails_SalaryDay");
							}


							String Current_date="";

							Current_date = now.get(Calendar.YEAR)+"-"+month+"-"+day;
							Empdetails_xml_str += "<SalaryDate>"+Current_date+"</SalaryDate>";
						}catch(Exception ex){
							plcommonObj.printException(ex);
						}
					}

					Empdetails_xml_str +="<SalaryDetails>";
					try{
						Empdetails_xml_str += "<GrossSalary>"+formObject.getNGValue("cmplx_IncomeDetails_grossSal").replace(",", "")+"</GrossSalary>";
						Empdetails_xml_str += "<Commission>"+formObject.getNGValue("cmplx_IncomeDetails_Commission_Avg").replace(",", "")+"</Commission>";
					}catch(Exception e){
						PersonalLoanS.mLogger.info("Exception in Exception occured in getEmp_details method: ");
						PLCommon.printException(e);
					}
					Empdetails_xml_str +="</SalaryDetails>";
				}catch(Exception e){
					PersonalLoanS.mLogger.info("Exception in Exception occured in getEmp_details method: ");
					PLCommon.printException(e);
				}
				Empdetails_xml_str +="</EmploymentDetails>";
				//Done by aman for CR 0712
				if (formObject.getNGValue("cmplx_EmploymentDetails_LOSPrevious")!=null &&  !"".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_LOSPrevious"))){
					Empdetails_xml_str +="<EmploymentDetails><EmploymentType>S</EmploymentType><OrganisationType>PreviousOrg</OrganisationType><JobDuration>"+formObject.getNGValue("cmplx_EmploymentDetails_LOSPrevious")+"</JobDuration></EmploymentDetails>";
				}
			}
			//Done by aman for CR 0712
			else{
				String designation = getDesignationAuthSign();
				Empdetails_xml_str +="<EmploymentDetails>";
				try{
					if(primaryApp){
						Empdetails_xml_str += "<EmploymentType>"+"Self employed"+"</EmploymentType>";
					}
					else{
						Empdetails_xml_str += "<EmploymentType>"+getSuppCardnotifdata("EmploymentType")+"</EmploymentType>";
					}

					if(primaryApp){
						Empdetails_xml_str += "<Designation>"+designation+"</Designation>";
					}
					else{
						Empdetails_xml_str += "<Designation>"+getSuppCardnotifdata("Designation")+"</Designation>";
					}

					if(primaryApp){
						Empdetails_xml_str += "<Occupation>"+designation+"</Occupation>";
					}
					else{
						Empdetails_xml_str += "<Occupation>"+getSuppCardnotifdata("Occupation")+"</Designation>";
					}

				}catch(Exception ex){
					//CreditCard.mLogger.info("CC Integ java file Exception occured in getEmp_details method : "+CC_Common.printException(e));
					PersonalLoanS.mLogger.info("Exception in Exception occured in getEmp_details method: ");
					PLCommon.printException(ex);
				}
				Empdetails_xml_str +="</EmploymentDetails>";
			}

		}
		catch(Exception ex){
			Empdetails_xml_str="";
			PersonalLoanS.mLogger.info("Exception in getEmp_details function"+ex.getMessage());
			PLCommon.printException(ex);
		}
		return Empdetails_xml_str;
	}

	public String getSuppCardnotifdata(String tag_name){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		if("EmploymentType".equalsIgnoreCase(tag_name) || "Designation".equalsIgnoreCase(tag_name) || "MarketingCode".equalsIgnoreCase(tag_name) || "Occupation".equalsIgnoreCase(tag_name)){
			String status="",Occupation="",marketCode="";
			//CreditCard.mLogger.info("RLOS COMMON"+ " before adding guarantor EmploymentType " + xml_str);
			for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid");i++){
				if(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13).equals(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,3))){
					status=formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,32);
					Occupation=formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,31);
					marketCode=formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,26);
					break;
				}
			}
			String Designation="";
			try
			{
				String sQuery = "select Description from ng_master_Designation with (nolock) where code='" +  Occupation + "'";

				//RLOS.mLogger.info("RLOS val change Value of dob is:"+sQuery);

				List<List<String>> recordList = formObject.getNGDataFromDataCache(sQuery);
				//RLOS.mLogger.info("RLOS val change Vasdfsdflue of dob is:"+recordList.get(0).get(0));
				if(recordList.get(0).get(0)!= null && recordList.get(0)!=null && !"".equalsIgnoreCase(recordList.get(0).get(0)))//Commented for Sonar
				{
					Designation=recordList.get(0).get(0);
				}
			}
			catch(Exception e)
			{
				PersonalLoanS.mLogger.info("In designation"+ e.getStackTrace());
			}


			if("EmploymentType".equalsIgnoreCase(tag_name)){
				return status;
			}
			//below code done by nikhil for Send details to CAPs error 05/01/2018
			else if("Designation".equalsIgnoreCase(tag_name)){
				return Designation;
			}
			else if("Occupation".equalsIgnoreCase(tag_name)){
				return Occupation;
			}
			else if("MarketingCode".equalsIgnoreCase(tag_name)){
				return marketCode;
			}


		}
		return "";
	}

	public String getDesignationAuthSign(){
		String desig = null;
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String query = "select top 1 Designation from ng_rlos_gr_AuthSignDetails with(nolock) where ChildMapping = (select parentmapping_auth from NG_RLOS_GR_CompanyDetails with(nolock) where applicantCategory='Business' and comp_winame = '"+formObject.getWFWorkitemName()+"') and AuthSignPassPortNo = (select PassportNo from ng_RLOS_Customer with(nolock) where wi_name = '"+formObject.getWFWorkitemName()+"')";
			List<List<String>> result = formObject.getDataFromDataSource(query);
			if(result!=null && result.size()>0 && result.get(0).size()>0){
				desig = result.get(0).get(0);
			}
		}catch(Exception ex){
			desig="";
			PersonalLoanS.mLogger.info("Exception in getDesignationAuthSign function"+ex.getMessage());
			PLCommon.printException(ex);
		}
		return desig;
	}

	public String getEmploymentPeriod(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String LosCurr = formObject.getNGValue("cmplx_EmploymentDetails_LOS");
		String LosPrev = formObject.getNGValue("cmplx_EmploymentDetails_LOSPrevious");
		String yymm="";
		try{
			String l1 = LosCurr;
			String l2 = LosPrev;
			int y1;
			int y2;
			int m1;
			int m2;
			if(!"".equals(l1)){
				if(l1.contains(".")){
					y1 = Integer.parseInt(l1.split("\\.")[0]);
					m1 = Integer.parseInt(l1.split("\\.")[1]);
				}
				else{
					y1 = Integer.parseInt(l1);
					m1 = 0;
				}
			}
			else{
				y1=0;
				m1=0;
			}
			if(!"".equals(l2)){
				if(l2.contains(".")){
					y2 = Integer.parseInt(l2.split("\\.")[0]);
					m2 = Integer.parseInt(l2.split("\\.")[1]);
				}
				else{
					y2 = Integer.parseInt(l2);
					m2 = 0;
				}
			}
			else{
				y2=0;
				m2=0;
			}
			if(m1+m2>12){
				yymm = (y1+y2+1)+"."+(String.valueOf((12 - (m1+m2))).length()==1?"0"+String.valueOf((12 - (m1+m2))):String.valueOf(12 - (m1+m2)));
			}
			else{
				yymm = (y1+y2)+"."+(String.valueOf(m1+m2).length()==1?"0"+String.valueOf(m1+m2):String.valueOf(m1+m2));
			}

		}catch(Exception ex){
			yymm="";
			PersonalLoanS.mLogger.info("Exception in getEmploymentPeriod function"+ex.getMessage());
			PLCommon.printException(ex);
		}
		return yymm;
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


				for(int chosenRow =0 ;chosenRow<formObject.getLVWRowCount(listViewname);chosenRow++ )
				{
					String Selected_card=formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid", formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"), 5);
					String Service_for_Card=formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Card Product"));
					if(!Selected_card.equals(Service_for_Card))
					{
						continue;
					}
					String serviceType = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Transaction Type"));
					String amount = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Amount"));
					String Service_date = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Date"));
					String cc_no = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("CreditCard No"));
					String benefName = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Beneficiary name"));
					String payMode = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Transfer Mode"));
					//String paymentPurpose = "PSC";//commented by deepak as the same is of no use.	
					String paymentPurpose = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Payment Purpose"));
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
					String branchName = formObject.getNGValue("AlternateContactDetails_CustomerDomicileBranch");
					String iban = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("IBAN"));
					String cardEmboss = cc_no;
					String custFullName = formObject.getNGValue("cmplx_Customer_Shortname");
					String acctNo = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Account No for Swift"));
					String acctNoforAT = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Account No for Account Transfer"));
					
					Financial_xml_str+="<FinancialServices>";
					if(Call_name.equalsIgnoreCase("CARD_NOTIFICATION")){
						if(checkValue(serviceType)){
							Financial_xml_str+="<ServiceType>"+getTransactiontype(serviceType)+"</ServiceType>";
						}
						if(checkValue(amount)){
							Financial_xml_str+="<Amount>"+amount+"</Amount>";
						}
						if(checkValue(Service_date)){
							Service_date=Service_date.replaceAll("/", "-");
							Financial_xml_str+="<ServiceRequestedDate>"+Service_date+"</ServiceRequestedDate>";
						}
						if(checkValue(cc_no)){
							Financial_xml_str+="<CardNumber>"+cc_no+"</CardNumber>";
						}
						if(checkValue(benefName)){
							Financial_xml_str+="<BenificiaryName>"+benefName+"</BenificiaryName>";
						}
						if(checkValue(payMode)){
							Financial_xml_str+="<PaymentMode>"+getTrasferMode(payMode)+"</PaymentMode>";
						}
						if(checkValue(paymentPurpose)){
						Financial_xml_str+="<PaymentPurpose>"+paymentPurpose+"</PaymentPurpose>";
						}
						if(checkValue(tenor)){
							Financial_xml_str+="<PaymentPeriod>"+tenor+"</PaymentPeriod>";//Added for PCASP-2750
						}
						if(checkValue(dispChanel)){
							Financial_xml_str+="<DispatchChannel>"+getDispatchChannel(dispChanel)+"</DispatchChannel>";
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
						if(checkValue(acctNoforAT)){
							Financial_xml_str+= getAcctIdforServices(serviceType,payMode,acctNoforAT);
						}
						Financial_xml_str+="<OtherBankInfo>";
						if(checkValue(bankCode)){
							Financial_xml_str+="<BankCode>"+bankCode+"</BankCode>";
						}
						if(checkValue(iban)){
							Financial_xml_str+="<IBAN>"+iban+"</IBAN>";
						}
						if(checkValue(acctNo)){
							Financial_xml_str+="<AcctId>"+acctNo+"</AcctId>";
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

			SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy");
			Date maxdate=sdf.parse(formObject.getNGValue("cmplx_CardDetails_cmpmx_gr_cardDetails", 0, 3));
			PersonalLoanS.mLogger.info("PL Common inside getCheque_details maxDate in seconds : "+maxdate);
			int maxindex=0;
			for(int i=1;i<cheque_row_count;i++)
			{	/*			
				String date = formObject.getNGValue("cmplx_CardDetails_cmpmx_gr_cardDetails", i, 3); //0
				Date curr_date=sdf.parse(date);
				PersonalLoanS.mLogger.info("PL Common inside getCheque_details Grid date,curr_date is: "+date+" ,"+curr_date);

				if(curr_date.after(maxdate)){
					PersonalLoanS.mLogger.info("PL Common inside getCheque_details Current Date is after max date");
					maxdate=curr_date;
					maxindex=i;
				}*/

				if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_CardDetails_cmpmx_gr_cardDetails", i, 3))){
					String date = formObject.getNGValue("cmplx_CardDetails_cmpmx_gr_cardDetails", i, 3); //0
					Date curr_date=sdf.parse(date);
					PersonalLoanS.mLogger.info("PL Common inside getCheque_details Grid date,curr_date is: "+date+" ,"+curr_date);

					if(curr_date.after(maxdate)){
						PersonalLoanS.mLogger.info("PL Common inside getCheque_details Current Date is after max date");
						maxdate=curr_date;
						maxindex=i;
					}
				}
			}	

			String bankName = formObject.getNGValue("cmplx_CardDetails_cmpmx_gr_cardDetails", maxindex, 0); //0
			String cheqNo = formObject.getNGValue("cmplx_CardDetails_cmpmx_gr_cardDetails", maxindex, 1); //0
			String Amount = formObject.getNGValue("cmplx_CardDetails_cmpmx_gr_cardDetails", maxindex, 2); //0
			String chqDate = formObject.getNGValue("cmplx_CardDetails_cmpmx_gr_cardDetails", maxindex, 3); //0

			if(Call_name.equalsIgnoreCase("CARD_NOTIFICATION") && !"".equalsIgnoreCase(cheqNo)){
				cheque_xml_str = cheque_xml_str+ "<SecurityChequeDetails><BankName>"+bankName+"</BankName>";
				cheque_xml_str = cheque_xml_str+ "<ChequeNo>"+cheqNo+"</ChequeNo>";
				cheque_xml_str = cheque_xml_str+ "<Amount>"+Amount+"</Amount>";
				cheque_xml_str = cheque_xml_str+ "<Date>"+new Common_Utils(PersonalLoanS.mLogger).Convert_dateFormat(chqDate, "MM/dd/yyyy","yyyy-MM-dd")+"</Date></SecurityChequeDetails>";
			}
			else if(Call_name.equalsIgnoreCase("CARD_SERVICES_REQUEST") && !"".equalsIgnoreCase(cheqNo)){
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
					//PersonalLoanS.mLogger.info(""+ "column length values"+ col_n);
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

				else if(tag_name.equalsIgnoreCase("OtherIncomeDetails")){
					try{
						PersonalLoanS.mLogger.info("inside OtherIncomeDetails");
						String assessedIncome = formObject.getNGValue("cmplx_IncomeDetails_Other_Avg");
						String grossSal = formObject.getNGValue("cmplx_IncomeDetails_grossSal");
						String selfSal = formObject.getNGValue("cmplx_IncomeDetails_AvgBal");
						PersonalLoanS.mLogger.info("inside OtherIncomeDetails assessedIncome: "+assessedIncome);
						PersonalLoanS.mLogger.info("inside OtherIncomeDetails grossSal: "+grossSal);
						PersonalLoanS.mLogger.info("inside OtherIncomeDetails selfSal: "+selfSal);

						String xml_str = int_xml.get(parent_tag);
						PersonalLoanS.mLogger.info("inside OtherIncomeDetails before tag addition  xml_str: "+xml_str);
						String empType = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5);
						PersonalLoanS.mLogger.info("inside OtherIncomeDetails empType: "+empType);
						if("Salaried".equalsIgnoreCase(empType)){
							if(assessedIncome!=null && !"".equals(assessedIncome)){
								assessedIncome = String.valueOf(Float.parseFloat(assessedIncome.replace(",", ""))); 
								xml_str = xml_str + "<OtherIncomeDetails><IncomeType>AssessedIncome</IncomeType><IncomeAmount>"+assessedIncome+"</IncomeAmount><IncomeSource>Salaried</IncomeSource></OtherIncomeDetails>";
							}
							if(grossSal!=null && !"".equals(grossSal)){
								grossSal = String.valueOf(Float.parseFloat(grossSal.replace(",", "")));
								xml_str = xml_str + "<OtherIncomeDetails><IncomeType>GrossIncomeForSalaried</IncomeType><IncomeAmount>"+grossSal+"</IncomeAmount><IncomeSource>Salaried</IncomeSource></OtherIncomeDetails>";
							}
						}
						else{
							if(selfSal!=null && !"".equals(selfSal)){
								selfSal = String.valueOf(Float.parseFloat(selfSal.replace(",", "")));
								xml_str = xml_str + "<OtherIncomeDetails><IncomeType>GrossIncomeForSelf</IncomeType><IncomeAmount>"+selfSal+"</IncomeAmount></OtherIncomeDetails>";
							}
						}
						int_xml.put(parent_tag, xml_str);
						PersonalLoanS.mLogger.info("inside OtherIncomeDetails after tag addition  xml_str: "+xml_str);
					}
					catch(Exception e){
						PersonalLoanS.mLogger.info("CC_INtegration Exception in CARD services custom function OtherIncomeDetails tag:  ");
						new PLCommon().printException(e);
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

					//PersonalLoanS.mLogger.info(""+ "column length values"+ col_n);
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
							String card_product = "";
							if(card_product==null|| card_product.equals("")){
								intrest_val="NA";
							} 
							else{
								if(tag_name.equalsIgnoreCase("InterestProfile")){
									intrest_Query="select code from NG_MASTER_Int_Profile with(nolock) where Product = '"+card_product+"'";
								}
								else if(tag_name.equalsIgnoreCase("FeeProfile")||tag_name.equalsIgnoreCase("FeeProfileSerNo")){
									intrest_Query="select code from NG_MASTER_Fee_Profile with(nolock) where Product = '"+card_product+"'";
								}
								else if(tag_name.equalsIgnoreCase("TransactionFeeProfile")||tag_name.equalsIgnoreCase("TxnProfileSerNo")){
									intrest_Query="select code from NG_MASTER_TransactionFee_Profile with(nolock) where Product = '"+card_product+"'";
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
					//Changes done for code optimization 25/07
					if(tag_name.equalsIgnoreCase("IsNTB")){
						tagValue=(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true")?"Y":"N");
					}
					//Changes done for code optimization 25/07
					/*else if((tag_name.equalsIgnoreCase("VIPFlag") || tag_name.equalsIgnoreCase("VIPFlg"))&&Call_name.equalsIgnoreCase("NEW_PersonalLoanS_REQ")){
						tagValue=(formObject.getNGValue("cmplx_Customer_VIPFlag").equalsIgnoreCase("true")?"1":"0");
					}*/
					else if(tag_name.equalsIgnoreCase("VIPFlag") || tag_name.equalsIgnoreCase("VIPFlg")){
						tagValue=(formObject.getNGValue("cmplx_Customer_VIPFlag").equalsIgnoreCase("true")?"Y":"N");
						PersonalLoanS.mLogger.info("Rishabh Sarda"+tagValue + '7');
					}
					else if(tag_name.equalsIgnoreCase("NonResidentFlag")){
						tagValue=(formObject.getNGValue("cmplx_Customer_ResidentNonResident").equalsIgnoreCase("Resident")?"Y":"N");
					}	
					else if(tag_name.equalsIgnoreCase("EStatementFlag")){
						tagValue=(formObject.getNGValue("AlternateContactDetails_ESTATEMENTFLAG").equalsIgnoreCase("Yes")?"Y":"N");
					}

					if(!tagValue.equals("")){
						xml_str = xml_str + "<"+tag_name+">"+tagValue+"</"+ tag_name+">";
					}


					PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}

				/*else if("MaritalStatus".equalsIgnoreCase(tag_name)){
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
				}*/

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
					String CardDispatch=formObject.getNGValue("AlternateContactDetails_carddispatch");
					String tagValue="";

					if(CardDispatch!=null && CardDispatch.equalsIgnoreCase("ByCourier"))
						tagValue="998";
					else
						tagValue=formObject.getNGValue("AlternateContactDetails_CustomerDomicileBranch")!=null?formObject.getNGValue("AlternateContactDetails_CustomerDomicileBranch"):"";

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
				/*else if("Gender".equalsIgnoreCase(tag_name) ){
					PersonalLoanS.mLogger.info("inside 1st if inside customer update req1");
					String xml_str = int_xml.get(parent_tag);
					xml_str =xml_str+ "<"+tag_name+">"+"1"+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("PL COMMON  after adding DSAId:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}*/

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
	private String getFalcon_prdInfo(String applicant_type) {
		String prfInfo="";
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String card_prod = formObject.getNGValue("CC_Creation_Product");
			String card_intrest_profile = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),11);

			if("Primary".equalsIgnoreCase(applicant_type)){
				String Enrolmentno= formObject.getNGValue("AlternateContactDetails_EnrollRewardsIdentifier");

				prfInfo="<ProductName>FALCON</ProductName>"+
						"<ProductRefInfo>"+
						"	<RefType>SurrogateMode</RefType>"+
						"	<RefValue>AMS</RefValue>"+
						"</ProductRefInfo>"+
						" <ProductRefInfo>"+
						"   <RefType>MembershipNumber</RefType>"+
						"   <RefValue>"+Enrolmentno+"</RefValue>"+
						" </ProductRefInfo>"+
						" <ProductRefInfo>"+
						"   <RefType>EnrollToFalcon</RefType>"+
						"   <RefValue>Y</RefValue>"+
						"  </ProductRefInfo>";
			}
			else{
				String Enrolmentno="";
				for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_supplementGrid");i++){
					if(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13).equals(formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,3))){
						Enrolmentno=formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,42);
						break;
					}
				}

				prfInfo="<ProductName>FALCON</ProductName>"+
						"<ProductRefInfo>"+
						"	<RefType>SurrogateMode</RefType>"+
						"	<RefValue>AMS</RefValue>"+
						"</ProductRefInfo>"+
						" <ProductRefInfo>"+
						"   <RefType>MembershipNumber</RefType>"+
						"   <RefValue>"+Enrolmentno+"</RefValue>"+
						" </ProductRefInfo>"+
						" <ProductRefInfo>"+
						"   <RefType>EnrollToFalcon</RefType>"+
						"   <RefValue>Y</RefValue>"+
						"  </ProductRefInfo>";
			}
			//elow code comented by nikhil for Card notification CR
			if(card_prod.contains("EKTMC")||card_prod.contains("EKWEC") && "S".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg"))){
				String member = formObject.getNGValue("cmplx_EmploymentDetails_tierno");
				if(!"".equalsIgnoreCase(member)){
					prfInfo = prfInfo+ "<ProductRefInfo><RefType>MembershipOption</RefType><RefValue>"+member+"</RefValue></ProductRefInfo>";
				}
			}

		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("CC Integration "+ " Exception occured in getLinkageType_refval + "+e.getMessage());

		}
		return ("<ProductInfo>"+prfInfo+"</ProductInfo>");
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

					//PersonalLoanS.mLogger.info(""+ "column length values"+ col_n);
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
				else if("MaritalStatus".equalsIgnoreCase(tag_name) && "CustAndProductInfo".equalsIgnoreCase(parent_tag)){
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<"+tag_name+">"+ getCodeDesc("NG_MASTER_Maritalstatus", "prime_val",formObject.getNGValue(form_control))+"</"+tag_name+">";
					int_xml.put(parent_tag, xml_str);
				}
				else if("Gender".equalsIgnoreCase(tag_name) && "CustAndProductInfo".equalsIgnoreCase(parent_tag)){
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<"+tag_name+">"+ getCodeDesc("NG_MASTER_Gender", "prime_val",formObject.getNGValue(form_control))+"</"+tag_name+">";
					int_xml.put(parent_tag, xml_str);
				}
				else if(("Designation".equalsIgnoreCase(tag_name)||"Department".equalsIgnoreCase(tag_name)) && "CustAndProductInfo".equalsIgnoreCase(parent_tag)){
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<"+tag_name+">"+ getCodeDesc("NG_MASTER_Designation", "Description",formObject.getNGValue(form_control))+"</"+tag_name+">";
					int_xml.put(parent_tag, xml_str);
				}
				else if("PhoneFaxDtls".equalsIgnoreCase(tag_name) && "CustAndProductInfo".equalsIgnoreCase(parent_tag)){

					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + getNewCardPhoneFaxDtls();
					int_xml.put(parent_tag, xml_str);
				}
				else if("ProfileSerNo".equalsIgnoreCase(tag_name) && "CardDetails".equalsIgnoreCase(parent_tag)){
					PersonalLoanS.mLogger.info("inside profileserno for New Card ");
					if(int_xml.containsKey(parent_tag))
					{
						try{
							String xml_str = int_xml.get(parent_tag);
							List<List<String>> record = null;
							PersonalLoanS.mLogger.info("RLOS COMMON before adding ProfileSerNo "+xml_str);
							if( "PRIMARY".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12))){
								record = formObject.getNGDataFromDataCache("select top 1 MAST_PROF_PRIM from ng_master_cardProduct with(nolock) where CODE ='"+formObject.getNGValue("CC_Creation_Product")+"'");
							}
							else{
								record = formObject.getNGDataFromDataCache("select top 1 MAST_PROF_SUPP_CLUND from ng_master_cardProduct with(nolock) where CODE ='"+formObject.getNGValue("CC_Creation_Product")+"'");
							}

							String result = record.get(0).get(0);
							xml_str = xml_str +"<"+tag_name+">"+result+"</"+tag_name+">";
							PersonalLoanS.mLogger.info("RLOS COMMON after adding ProfileSerNo+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}catch(Exception ex){
							PersonalLoanS.mLogger.info("Exception in aqdding profileserno ");
							new PLCommon().printException(ex);
						}
					}

				}

				else if("AddrDet".equalsIgnoreCase(tag_name) || "AddressDetails".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("inside 1st if"+" inside customer update req1");
					if(int_xml.containsKey(parent_tag))
					{

						PersonalLoanS.mLogger.info("inside 1st if"+" inside customer update req2");
						String xml_str = int_xml.get(parent_tag);
						String Cust_type = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12);
						String customer_ntb=fetchIsNTBval("Primary");
						PersonalLoanS.mLogger.info("RLOS COMMON"+" before adding address+ "+xml_str);
						//xml_str = xml_str + getCustAddress_details(callName);
						if( "PRIMARY".equalsIgnoreCase(Cust_type) && "-1".equalsIgnoreCase(customer_ntb)){
							xml_str = xml_str + getCustAddress_details(callName);
						}
						else{
							xml_str = xml_str + getCardCrationDummyAddress_details(); 
						}

						PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding address+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}		                            	
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
					//change by saurabh on 28th Jan
					String wiNum = (formObject.getWFWorkitemName()).split("-")[1];
					xml_str = xml_str+"<"+tag_name+">"+wiNum+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("CC COMMON  after adding ApplicationNumber:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}

				else if("PeriodicCashLimit".equalsIgnoreCase(tag_name) || "StatementCycle".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("inside else if for PeriodicCashLimit");
					if(int_xml.containsKey(parent_tag))
					{
						//Deepak changes done on 04July2018 below mapping changed from income details statement cycle to Demographics>Card Details>Statement Cycle
						String statCycle = formObject.getNGValue("cmplx_CardDetails_statcycle");
						if(statCycle!=null && !"".equalsIgnoreCase(statCycle) && !" ".equalsIgnoreCase(statCycle) && !"--Select--".equalsIgnoreCase(statCycle)){
							String result = "";
							String subStr = statCycle.substring(0, statCycle.indexOf("of each month"));
							for(int i=0;i<subStr.length();i++){
								int num = Character.getNumericValue(subStr.charAt(i));
								if(num>=0 && num<=9){
									result+=subStr.charAt(i);
								}
							}
							String xml_str = int_xml.get(parent_tag);
							//Deepak changes done on 04July2018 new login for statement cycle as ne mapping sheet
							if("StatementCycle".equalsIgnoreCase(tag_name)){
								if(result.length()==1)
									result="-20"+result;

								else if(result.length()==2)
									result="-2"+result;
							}

							PersonalLoanS.mLogger.info("RLOS COMMON before adding PeriodicCashLimit+ "+xml_str);
							xml_str = xml_str +"<"+tag_name+">"+result+"</"+tag_name+">";
							PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding PeriodicCashLimit+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}
					}
				}
				else if("NewPersonalFlag".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("new changes for NewPersonalFlag flag");
					String xml_str = int_xml.get(parent_tag);
					xml_str=xml_str+"<"+tag_name+">"+fetchIsPersonalval()+"</"+tag_name+">";
					int_xml.put(parent_tag, xml_str);
				}

				else if(tag_name.equalsIgnoreCase("OrgName")||tag_name.equalsIgnoreCase("Department")||tag_name.equalsIgnoreCase("Designation")){
					PersonalLoanS.mLogger.info("Before calling getEmploymentOrCompanyDetails()");
					String xml_str = int_xml.get(parent_tag);
					xml_str=xml_str+getEmploymentOrCompanyDetails(tag_name);
					int_xml.put(parent_tag, xml_str);

				}
				else if("NewAccountFlag".equalsIgnoreCase(tag_name)){
					PersonalLoanS.mLogger.info("new changes for NewAccountFlag flag");
					String xml_str = int_xml.get(parent_tag);
					String Val="";
					String listView = "cmplx_CCCreation_cmplx_CCCreationGrid";
					String Cust_type = formObject.getNGValue(listView,formObject.getSelectedIndex(listView),12);					
					if("PRIMARY".equalsIgnoreCase(Cust_type))
					{
						Val="-1";
					}
					else if("SUPPLEMENT".equalsIgnoreCase(Cust_type))
					{
						Val="0";
					}
					xml_str=xml_str+"<"+tag_name+">"+Val+"</"+tag_name+">";
					int_xml.put(parent_tag, xml_str);
				}

				else if("AuthorizationLimit".equalsIgnoreCase(tag_name)&& "CardDetails".equalsIgnoreCase(parent_tag)){
					PersonalLoanS.mLogger.info("new changes for NewAccountFlag flag");
					String xml_str = int_xml.get(parent_tag);
					String Val="";
					String listView = "cmplx_CCCreation_cmplx_CCCreationGrid";
					String Cust_type = formObject.getNGValue(listView,formObject.getSelectedIndex(listView),12);					
					if("SUPPLEMENT".equalsIgnoreCase(Cust_type))
					{
						Val=formObject.getNGValue(listView,formObject.getSelectedIndex(listView),6);//card limit
					}
					xml_str=xml_str+"<"+tag_name+">"+Val+"</"+tag_name+">";
					int_xml.put(parent_tag, xml_str);
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
							String card_product ="";
							//below if else added by akshay on 5/3/18 for drop 4
							/*if("SUPPLEMENT".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)))
							{
								 for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid");i++){
									 if(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13).equals(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,3)))
										 {
											card_product = formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,30);
											break;
										 }
								 }
							}
							else{*/
							card_product = formObject.getNGValue("CC_Creation_Product");

							//}
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
								List<List<String>> intsert_list = formObject.getDataFromDataSource(intrest_Query);
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
				//if changed ro elsif for DROp4 by saurabh.
				else if("PRIMARY".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)) || ("SUPPLEMENT".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)) && formObject.getNGValue("cmplx_Customer_PAssportNo").equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13))  ))
				{


					//case added by saurabh on 28th Jan
					if(tag_name.equalsIgnoreCase("FullName")){
						String xml_str = int_xml.get(parent_tag);
						String fullName = formObject.getNGValue("cmplx_Customer_FirstNAme")+" "+formObject.getNGValue("cmplx_Customer_MiddleNAme")+" "+formObject.getNGValue("cmplx_Customer_LastNAme");
						xml_str = xml_str + "<"+tag_name+">"+fullName+"</"+ tag_name+">";

						PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding BankName+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}
					else if(tag_name.equalsIgnoreCase("CIFId")){
						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<"+tag_name+">C"+formObject.getNGValue(form_control)+"</"+ tag_name+">";

						PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding BankName+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}
					else if("Extensions".equalsIgnoreCase(tag_name)){
						//CreditCard.mLogger.info("inside 1st if"+" inside ReferenceDetails for Card NOTIF");
						if(int_xml.containsKey(parent_tag))
						{
							String xml_str = int_xml.get(parent_tag);
							//CreditCard.mLogger.info("RLOS COMMON"+" before adding ReferenceDetails+ "+xml_str);
							xml_str = xml_str + getExtensions_details(true);
							//CreditCard.mLogger.info("RLOS COMMON"+" after adding ReferenceDetails+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}

					}
					else if(tag_name.equalsIgnoreCase("IsNTB") || tag_name.equalsIgnoreCase("VIPFlag") || tag_name.equalsIgnoreCase("VIPFlg") || tag_name.equalsIgnoreCase("NonResidentFlag")||tag_name.equalsIgnoreCase("EStatementFlag")){
						String xml_str = int_xml.get(parent_tag);
						String tagValue="";
						if(tag_name.equalsIgnoreCase("IsNTB")){
							tagValue = new PLCommon().fetchIsNTBval("Primary");
							//tagValue=(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true")?"-1":"0");
						}
						/*else if(tag_name.equalsIgnoreCase("IsNTB")){
						tagValue=(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true")?"Y":"N");
					}*/
						/*else if((tag_name.equalsIgnoreCase("VIPFlag") || tag_name.equalsIgnoreCase("VIPFlg"))&&Call_name.equalsIgnoreCase("NEW_CREDITCARD_REQ")){
						tagValue=(formObject.getNGValue("cmplx_Customer_VIPFlag").equalsIgnoreCase("true")?"1":"0");
					}*/
						else if(tag_name.equalsIgnoreCase("VIPFlag") || tag_name.equalsIgnoreCase("VIPFlg")){
							//change by saurabh on 28th Jan
							tagValue=(formObject.getNGValue("cmplx_Customer_VIPFlag").equalsIgnoreCase("true")?"1":"0");
							PersonalLoanS.mLogger.info("Rishabh Sarda"+tagValue + '1');
						}
						else if(tag_name.equalsIgnoreCase("NonResidentFlag")){
							tagValue=(formObject.getNGValue("cmplx_Customer_ResidentNonResident").equalsIgnoreCase("Resident")?"Y":"N");
						}	
						else if(tag_name.equalsIgnoreCase("EStatementFlag")){
							tagValue=(formObject.getNGValue("AlternateContactDetails_ESTATEMENTFLAG").equalsIgnoreCase("Yes")?"Y":"N");
						}

						if(!tagValue.equals("")){
							xml_str = xml_str + "<"+tag_name+">"+tagValue+"</"+ tag_name+">";
						}


						PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}
					else if("CardEmbossingName".equalsIgnoreCase(tag_name)){
						String xml_str = int_xml.get(parent_tag);
						String tagValue="";
						//CreditCard.mLogger.info("PL COMMON  before adding isNTB:  "+xml_str);
						if("PRIMARY".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)) )
						{
							tagValue=formObject.getNGValue("cmplx_CardDetails_cardemboss");//wrong field ID
						}
						else 
						{
							tagValue=formObject.getNGValue("cmplx_CardDetails_Self_card_embossing");
						}

						//tagValue=(tagValue.equalsIgnoreCase("true")?"-1":"0");
						//if(!tagValue.equals("")){
						xml_str = xml_str + "<"+tag_name+">"+tagValue+"</"+ tag_name+">";
						//}
						int_xml.put(parent_tag, xml_str);
						//CreditCard.mLogger.info("PL COMMON  after adding isNTB:  "+xml_str);
					}

					else if("AcctId".equalsIgnoreCase(tag_name)&& "AccountDetails".equalsIgnoreCase(parent_tag)){
						//CreditCard.mLogger.info("inside NEW_CREDITCARD_REQ for AcctId");

						String xml_str = int_xml.get(parent_tag);
						String tagValue="";
						String listView = "cmplx_CCCreation_cmplx_CCCreationGrid";
						String selectproduct_value = formObject.getNGValue(listView,formObject.getSelectedIndex(listView),5);
						int rowcount = formObject.getLVWRowCount(listView);
						for(int i=0;i<rowcount;i++){
							String gridproduct_Value = formObject.getNGValue(listView,i,5);
							String gridCustType = formObject.getNGValue(listView,i,12);
							if(gridproduct_Value.equalsIgnoreCase(selectproduct_value) && "PRIMARY".equalsIgnoreCase(gridCustType)){
								tagValue = formObject.getNGValue(listView,i,1);
								break;
							}
						}
						xml_str = "<"+tag_name+">"+tagValue+"</"+ tag_name+">";
						//CreditCard.mLogger.info("inside NEW_CREDITCARD_REQ for AcctId: "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}
					// Commented by aman for PCSP-291
					/*else if("MaritalStatus".equalsIgnoreCase(tag_name)){
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
				}*/

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
						String CardDispatch=formObject.getNGValue("AlternateContactDetails_carddispatch");
						String tagValue="";

						if(CardDispatch!=null && CardDispatch.equalsIgnoreCase("ByCourier"))
							tagValue="998";
						else
							tagValue=formObject.getNGValue("AlternateContactDetails_CustomerDomicileBranch")!=null?formObject.getNGValue("AlternateContactDetails_CustomerDomicileBranch"):"";

							xml_str = xml_str+"<"+tag_name+">"+tagValue+"</"+ tag_name+">";

							PersonalLoanS.mLogger.info("RLOS COMMON"+" after adding bhrabc id flag+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
					}

					/*else if("Gender".equalsIgnoreCase(tag_name) ){
					PersonalLoanS.mLogger.info("inside 1st if inside customer update req1");
					String xml_str = int_xml.get(parent_tag);
					xml_str =xml_str+ "<"+tag_name+">"+"1"+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("PL COMMON  after adding DSAId:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}*/


					else{
						int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
					}
				}


				else if( "SUPPLEMENT".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)) && !formObject.getNGValue("cmplx_Customer_PAssportNo").equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13)) )
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
					else if(tag_name.equalsIgnoreCase("CardEmbossingName")){
						String xml_str = int_xml.get(parent_tag);
						String tagValue="";
						//CreditCard.mLogger.info("PL COMMON  before adding isNTB:  "+xml_str);
						tagValue = getSupplementaryTagValue(9);
						//tagValue=(tagValue.equalsIgnoreCase("true")?"-1":"0");
						//if(!tagValue.equals("")){
						xml_str = xml_str + "<"+tag_name+">"+tagValue+"</"+ tag_name+">";
						//}
						int_xml.put(parent_tag, xml_str);
						//CreditCard.mLogger.info("PL COMMON  after adding isNTB:  "+xml_str);
					}
					else if(tag_name.equalsIgnoreCase("IsNTB")){
						//CreditCard.mLogger.info("inside supplement tag name IsNTB: "+tag_name);
						String xml_str = int_xml.get(parent_tag);
						String tagValue="";
						//CreditCard.mLogger.info("PL COMMON  before adding isNTB:  "+xml_str);
						tagValue = fetchIsNTBval("Supplement");
						//tagValue=(tagValue.equalsIgnoreCase("true")?"-1":"0");
						//if(!tagValue.equals("")){
						xml_str = xml_str + "<"+tag_name+">"+tagValue+"</"+ tag_name+">";
						//}
						int_xml.put(parent_tag, xml_str);
						//CreditCard.mLogger.info("PL COMMON  after adding isNTB:  "+xml_str);
					}
					else if("AcctId".equalsIgnoreCase(tag_name)&& "AccountDetails".equalsIgnoreCase(parent_tag)){
						String xml_str = int_xml.get(parent_tag);
						String tagValue="";

						String listView = "cmplx_CCCreation_cmplx_CCCreationGrid";
						String selectproduct_value = formObject.getNGValue(listView,formObject.getSelectedIndex(listView),5);
						int rowcount = formObject.getLVWRowCount(listView);
						for(int i=0;i<rowcount;i++){
							String gridproduct_Value = formObject.getNGValue(listView,i,5);
							String gridCustType = formObject.getNGValue(listView,i,12);
							if(gridproduct_Value.equalsIgnoreCase(selectproduct_value) && "PRIMARY".equalsIgnoreCase(gridCustType)){
								tagValue = formObject.getNGValue(listView,i,1);
								break;
							}
						}
						xml_str = "<"+tag_name+">"+tagValue+"</"+ tag_name+">";
						int_xml.put(parent_tag, xml_str);
					}


					else if("Extensions".equalsIgnoreCase(tag_name)){
						//CreditCard.mLogger.info("inside 1st if"+" inside ReferenceDetails for Card NOTIF");
						if(int_xml.containsKey(parent_tag))
						{
							String xml_str = int_xml.get(parent_tag);
							//CreditCard.mLogger.info("RLOS COMMON"+" before adding ReferenceDetails+ "+xml_str);
							xml_str = xml_str + getExtensions_details(false);
							//CreditCard.mLogger.info("RLOS COMMON"+" after adding ReferenceDetails+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}

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

	//Deepak changes done on 5 jan 2019 for CR PCSP-254 - 2 new tags added in card creation newAccountFlag & newPersonFlag
	public String fetchIsNTBval(String applicantType) {
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();	
			String ntbVal = "-1";
			String listView = "cmplx_CCCreation_cmplx_CCCreationGrid";
			//CreditCard.mLogger.info("Inside fetchNTBVal function ");

			if(applicantType.equalsIgnoreCase("Primary")){
				//Deepak 20 Dec PCSP 142 Changes to send NTB customer as existing for second card if his first card got created successfully start 
				String selectPassport = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13);
				String selectCIF = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),3);//column no corrected by deepak
				int rowcount = formObject.getLVWRowCount(listView);
				int createdCards = 0;
				for(int i=0;i<rowcount;i++){
					String gridPassprtValue = formObject.getNGValue(listView,i,13);
					String gridCifValue = formObject.getNGValue(listView,i,3);//column no corrected by deepak
					if(formObject.getNGValue(listView, i, 8).equals("Y") && gridPassprtValue.equalsIgnoreCase(selectPassport) && gridCifValue.equalsIgnoreCase(selectCIF)){
						createdCards++;
						break;
					}
				}
				if(createdCards>0){
					ntbVal="0";
					return ntbVal;
				}
				//Deepak 20 Dec PCSP 142 Changes to send NTB customer as existing for second card if his first card got created successfully END
				String selectECRN = formObject.getNGValue("CC_Creation_ECRN");
				//CreditCard.mLogger.info("Inside fetchNTBVal function primary");
				//Deepak table name changed from ng_RLOS_CUSTEXPOSE_CardDetails to ng_RLOS_CUSTEXPOSE_CustInfo for closed card as well. 12 Aug 2019
				//String query = "select count(distinct ECRN) from ng_RLOS_CUSTEXPOSE_CustInfo with (nolock) where Child_Wi = '"+formObject.getWFWorkitemName()+"' and CifId= '"+selectCIF+"' and ecrn !='' and ecrn is not null";
				//Deepak changes done for PCAS-2931
				String query = "select count(ECRN) from (select distinct ECRN from ng_RLOS_CUSTEXPOSE_CustInfo with (nolock) where Child_Wi = '"+formObject.getWFWorkitemName()+"' and CifId= '"+selectCIF+"' and ecrn !='' and ecrn is not null and ecrn not in (select ECRN from ng_cas_rejected_table where CIF= '"+selectCIF+"' and CRN=ECRN)  union all select distinct ECRN from NG_RLOS_gr_CCCreation where ECRN='"+selectECRN+"' and CardCreated='Y') as ECRN_temp";
				PersonalLoanS.mLogger.info("CC Integration query for ECRN "+query);
				List<List<String>> count = formObject.getDataFromDataSource(query);
				PersonalLoanS.mLogger.info("CC Integration query for ECRN COUNT VALUE "+count);
				if(count!=null && count.size()>0 && count.get(0)!=null){
					if(Integer.parseInt(count.get(0).get(0))>0){
						ntbVal="0";
					}
				}
			}
			else if (applicantType.equalsIgnoreCase("Supplement")){
				ntbVal="0";
			}
			PersonalLoanS.mLogger.info("Inside fetchNTBVal function before return is: "+ntbVal);
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
					//PersonalLoanS.mLogger.info(""+ "column length values"+ col_n);
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
							if (NGFUserResourceMgr_PL.getGlobalVar("PL_true").equalsIgnoreCase(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 10)))
							{
								formObject.setNGValue("cmplx_Customer_EmirateOfResidence",formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i, 6));
								add_res_val = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 1)+ " "+formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i, 2)+ formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i, 3)+ formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i, 4)+ formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i, 5)+ formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i, 6);
							}
						}
						xml_str = xml_str + "<" + tag_name + ">" + add_res_val + "</"+ tag_name + ">";

						//PersonalLoanS.mLogger.info("CC Integration "+ " after adding res_flag+ "+ xml_str);
						int_xml.put(parent_tag, xml_str);
					}
				}
				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
				}
			}
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("CC Integration "+ "CHEQUE_BOOK_ELIGIBILITY_Custom + ");
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
					//PersonalLoanS.mLogger.info("inside 1st if"+"inside 3rd if xml string xml string int_xml");
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
						//PersonalLoanS.mLogger.info("Search tags:: ShortNAme"+tag_name);
						if(fin_call_name.toUpperCase().contains(callName.toUpperCase()) && !capitalExceptionsTags.toUpperCase().contains(tag_name.toUpperCase())){
							form_control_val = formObject.getNGValue(form_control).toUpperCase();
						}
						else
							form_control_val = formObject.getNGValue(form_control);

						if(!"text".equalsIgnoreCase(data_format12)){
							String[] format_arr = data_format12.split(":");
							String format_name = format_arr[0];
							String format_type = format_arr[1];
							//PersonalLoanS.mLogger.info(""+"format_name"+format_name);
							//PersonalLoanS.mLogger.info(""+"format_type"+format_type);

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
						//PersonalLoanS.mLogger.info("inside else of parent tag xml_str"+"xml_str"+ xml_str);
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
						//PersonalLoanS.mLogger.info("#RLOS Common GenerateXML inside else of parent tag form_control_val xml_str1"+"xml_str"+ xml_str);

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
					/*else if("DocNo".equalsIgnoreCase(tag_name) && "DocDet".equalsIgnoreCase(parent_tag) && "".equalsIgnoreCase(form_control_val)){
						if(xml_str.contains("</DocDet>")){
							xml_str = xml_str.substring(0, xml_str.lastIndexOf("</DocDet>"));
							int_xml.put(parent_tag, xml_str);
						}
						else
							int_xml.remove(parent_tag);
					}*/


					else if("PhnLocalCode".equalsIgnoreCase(tag_name) && "PhnDetails".equalsIgnoreCase(parent_tag) && "".equalsIgnoreCase(form_control_val)){
						if(xml_str.contains("</PhnDetails>")){
							//PersonalLoanS.mLogger.info("PL_common"+ "Inside PhnDetails condition to remove RVC tag </PhnDetails> contanied");
							xml_str = xml_str.substring(0, xml_str.lastIndexOf("</PhnDetails>"));
							int_xml.put(parent_tag, xml_str);
						}
						else{
							//PersonalLoanS.mLogger.info("PL_common"+ "Inside PhnDetails condition to remove PhnDetails tag <PhnDetails> tag not contanied");
							int_xml.remove(parent_tag);
							Iterator<Map.Entry<String,String>> itr = int_xml.entrySet().iterator();
							PersonalLoanS.mLogger.info("itr of hashmap"+"itr"+itr);
							while (itr.hasNext())
							{
								Map.Entry<String, String> entry =  itr.next();
								//PersonalLoanS.mLogger.info("entry of hashmap"+"entry"+entry+ " entry.getValue(): "+ entry.getValue());
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
								//PersonalLoanS.mLogger.info("entry of hashmap"+"entry"+entry+ " entry.getValue(): "+ entry.getValue());
								if(entry.getValue().contains("EmlDet")){
									String key_val = entry.getValue();
									key_val = key_val.replace("<EmlDet>", "");
									key_val = key_val.replace("</EmlDet>", "");
									int_xml.put(entry.getKey(), key_val);
									//PersonalLoanS.mLogger.info("PL_common"+"KEY: entry.getKey()" + " Updated value: "+key_val);

									//PersonalLoanS.mLogger.info("PL_common "+"EmlDet removed from parent key");
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
								if(!form_control_val.equals("")){
									startDate = df.parse(form_control_val);
									form_control_val = df_new.format(startDate);
									PersonalLoanS.mLogger.info("RLOSCommon#Create Input"+" date conversion: final Output: "+form_control_val+ " requested format: "+format_type);
								}
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
					//PersonalLoanS.mLogger.info("inside else of parent tag xml_str"+"new_xml_str"+ new_xml_str);
				}

				else if(default_val==null || "".equalsIgnoreCase(default_val.trim())){
					if(int_xml.containsKey(parent_tag)|| "Y".equalsIgnoreCase(is_repetitive)){
						new_xml_str = new_xml_str + "<"+tag_name+">"+"</"+ tag_name+">";
					}
					//PersonalLoanS.mLogger.info("#RLOS Common GenerateXML Inside Else Part"+"no value found for tag name: "+ tag_name);
				}
				else{
					//PersonalLoanS.mLogger.info("#RLOS Common GenerateXML inside set default value"+"");
					form_control_val = default_val;
					//PersonalLoanS.mLogger.info("#RLOS Common GenerateXML inside set default value"+"form_control_val"+ form_control_val);
					new_xml_str = new_xml_str + "<"+tag_name+">"+form_control_val
							+"</"+ tag_name+">";
					//PersonalLoanS.mLogger.info("#RLOS Common GenerateXML inside else of parent tag form_control_val xml_str1"+"xml_str"+ new_xml_str);

				}
				int_xml.put(parent_tag, new_xml_str);
				//PersonalLoanS.mLogger.info("else of generatexml"+"RLOSCommon"+"inside else"+new_xml_str);

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
				if(finalXml.toString().contains("<MsgFormat>COMPLIANCE_CHECK</MsgFormat>")){
					socketServerPort = 5556;
				}
				else{
					socketServerPort = Integer.parseInt(outputMQXML.get(0).get(1));
				}
				//socketServerPort = Integer.parseInt(outputMQXML.get(0).get(1));
				PersonalLoanS.mLogger.info("$$outputgGridtXML "+ "socketServerPort " + socketServerPort);
				try{
					if (!("".equalsIgnoreCase(socketServerIP)  && socketServerPort==0)) {
						socket = new Socket(socketServerIP, socketServerPort);
						//new Code added by Deepak to set connection timeout
						int connection_timeout=60;
						try{
							connection_timeout = Integer.parseInt(NGFUserResourceMgr_PL.getGlobalVar("Integration_Connection_Timeout"));
						}
						catch(Exception e){
							connection_timeout=60;
						}

						socket.setSoTimeout(connection_timeout*1000);
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
						byte[] readBuffer = new byte[500];
						int num = din.read(readBuffer);
						if (num > 0) {

							byte[] arrayBytes = new byte[num];
							System.arraycopy(readBuffer, 0, arrayBytes, 0, num);
							mqOutputResponse = mqOutputResponse+ new String(arrayBytes, "UTF-16LE");
							PersonalLoanS.mLogger.info("mqOutputResponse/message ID :  "+mqOutputResponse);
							if(!"".equalsIgnoreCase(mqOutputResponse)){
								mqOutputResponse = getOutWtthMessageID(mqOutputResponse);
							}
							if(mqOutputResponse.contains("&lt;")){
								mqOutputResponse=mqOutputResponse.replaceAll("&lt;", "<");
								mqOutputResponse=mqOutputResponse.replaceAll("&gt;", ">");
							}
						}
						socket.close();
						return mqOutputResponse;

					} else {
						PersonalLoanS.mLogger.info("SocketServerIp and SocketServerPort is not maintained ");
						PersonalLoanS.mLogger.info("SocketServerIp is not maintained "+	socketServerIP);
						PersonalLoanS.mLogger.info(" SocketServerPort is not maintained "+	socketServerPort);
						return "MQ details not maintained";
					}
				}
				finally{
					if(out != null){

						out.close();
						out=null;
					}
					if(socketInputStream != null){

						socketInputStream.close();
						socketInputStream=null;
					}
					if(dout != null){

						dout.close();
						dout=null;
					}
					if(din != null){

						din.close();
						din=null;
					}
					if(socket != null){

						socket.close();
						socket=null;
					}
				}
			} else {
				PersonalLoanS.mLogger.info("SOcket details are not maintained in NG_RLOS_MQ_TABLE table"+"");
				return "MQ details not maintained";
			}
		} catch (Exception e) {
			PLCommon.printException(e);
			return "";
		}
	}
	public String getOutWtthMessageID(String message_ID){
		String outputxml="";
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String wi_name = formObject.getWFWorkitemName();
			String str_query = "select OUTPUT_XML from NG_PL_XMLLOG_HISTORY with (nolock) where MESSAGE_ID ='"+message_ID+"' and WI_NAME = '"+wi_name+"'";
			PersonalLoanS.mLogger.info("inside getOutWtthMessageID str_query: "+ str_query);
			List<List<String>> result=formObject.getDataFromDataSource(str_query);
			//below code added by nikhil 18/10 for Connection timeout
			String Integration_timeOut=NGFUserResourceMgr_PL.getGlobalVar("Inegration_Wait_Count");
			int Loop_wait_count=10;
			try
			{
				Loop_wait_count=Integer.parseInt(Integration_timeOut);
			}
			catch(Exception ex)
			{
				Loop_wait_count=10;
			}

			for(int Loop_count=0;Loop_count<Loop_wait_count;Loop_count++){
				if(result.size()>0){
					outputxml = result.get(0).get(0);
					break;
				}
				else{
					Thread.sleep(1000);
					result=formObject.getDataFromDataSource(str_query);
				}
			}

			if("".equalsIgnoreCase(outputxml)){
				outputxml="Error";
			}

		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("Exception occurred in getOutWtthMessageID" + e.getMessage());
			PersonalLoanS.mLogger.info("Exception occurred in getOutWtthMessageID" + e.getStackTrace());
			outputxml="Error";
		}
		return outputxml;
	}
	public String getReference_details(){
		PersonalLoanS.mLogger.info("RLOSCommon java file"+ "inside getReference_details : ");
		String  ref_xml_str ="";
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			int ref_row_count = formObject.getLVWRowCount("cmplx_ReferenceDetails_cmplx_ReferenceGrid");
			PersonalLoanS.mLogger.info("RLOSCommon java file"+ "inside getReference_details row_count+ : "+ref_row_count);

			for (int i = 0; i<ref_row_count;i++){
				String ref_name = formObject.getNGValue("cmplx_ReferenceDetails_cmplx_ReferenceGrid", i, 0); //0
				String ref_phone=formObject.getNGValue("cmplx_ReferenceDetails_cmplx_ReferenceGrid", i, 1);//1
				String ref_relation=formObject.getNGValue("cmplx_ReferenceDetails_cmplx_ReferenceGrid", i, 3);//2//by shweta
				ref_xml_str = ref_xml_str + "<ReferenceDetails><PersonName>"+ref_name+"</PersonName>";
				ref_xml_str = ref_xml_str + "<Relationship>"+ref_relation+"</Relationship>";
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

	public String getExtensions_details(boolean primary){
		PersonalLoanS.mLogger.info("RLOSCommon java file inside getExtensions_details : ");
		String  ext_xml_str ="";
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String card_prod = formObject.getNGValue("CC_Creation_Product");
			List<List<String>> typeOfCP = formObject.getDataFromDataSource("select top 1 ReqProduct from ng_master_cardProduct where CODE = '"+card_prod+"' and EmployerCategory = 'Salaried'");
			Map<String,String> desctoFormCtrl = new LinkedHashMap<String, String>();
			desctoFormCtrl.put("PS:Card:F152","C 10002 :AlternateContactDetails_carddispatch");
			desctoFormCtrl.put("PS:Card:F153","C 10010 :CC_Creation_CRN");
			desctoFormCtrl.put("P:Card:F154","C 10044:cmplx_CC_Loan_cmplx_btc");
			desctoFormCtrl.put("P:Card:F155","C 10048:cmplx_CC_Loan_VPSPAckage");
			desctoFormCtrl.put("P:Card:F156","C 10049:cmplx_CC_Loan_VPSStatus");
			desctoFormCtrl.put("P:Card:F157","C 10052:cmplx_CardDetails_compemboss");
			//desctoFormCtrl.put("P:Account:F158","A 21010 :");
			//desctoFormCtrl.put("P:Account:F159","A 21011 :Account Charity Percentage Amount    not to be passed");
			//desctoFormCtrl.put("P:Account:F160","A 21012 :Account Charity Combination    From masters");
			//desctoFormCtrl.put("P:Account:F161","A 21013 :Account Charity Organization ID    From masters");
			desctoFormCtrl.put("PS:Account:F160:Kalyan","A 21017:cmplx_CardDetails_cmplx_CardCRNDetails");
			desctoFormCtrl.put("PS:Account:F160:mrbh","A  707 :File_Generated_Date");
			desctoFormCtrl.put("PS:Account:F161","A  705 :File_Id");
			desctoFormCtrl.put("PS:Account:F163","A 21026:");
			//desctoFormCtrl.put("PS:Account:F164","A 707 :Account Primary Agreement Start Date");

			//desctoFormCtrl.put("PS:Account:F166","A 777 :Used for Murabaha Card");

			if(primary){
				desctoFormCtrl.put("PS:Account:F167","A 21073:AlternateContactDetails_ESTATEMENTFLAG");
			}
			//Deepak changes done for Air Arabia in extension 5 feb -PCSP 653
			String card_intrest_profile = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),11);
			if(card_intrest_profile.contains("Arabia")){
				desctoFormCtrl.put("PS:Account:F168","A 21075:AlternateContactDetails_AirArabiaIdentifier");
			}
			else if(card_intrest_profile.toUpperCase().contains("SKYWARDS")){
				desctoFormCtrl.put("PS:Card:F168","C 10101:AlternateContactDetails_EnrollRewardsIdentifier");
				desctoFormCtrl.put("PS:Card:F169","C 10102:#");
				desctoFormCtrl.put("PS:Card:F170","C 10103:Y");
			}
			if(primary){
				desctoFormCtrl.put("PS:Account:F169","A 10053:AlternateContactDetails_MobileNo1");
				desctoFormCtrl.put("PS:Account:F170","A 10054:AlternateContactDetails_EMAIL1_PRI");
				desctoFormCtrl.put("PS:Account:F171","A 10058:cmplx_CardDetails_smsopt");
				desctoFormCtrl.put("PS:Account:F164","A 21001:N");
			}

			//Deepak 3 feb changes done for extension fields. PCSP-653
			int extcount=152;
			for(Map.Entry<String, String> entry : desctoFormCtrl.entrySet()){
				if(extcount==165){extcount=173;}//Deepak Change to skip extension from 165 to 172
				PersonalLoanS.mLogger.info("Key :::"+entry.getKey());
				if(entry.getKey().contains("F154")){
					PersonalLoanS.mLogger.info("inside  F154: ");
					String lvName = entry.getValue().split(":")[1];
					PersonalLoanS.mLogger.info("inside  F154: "+lvName);
					if(formObject.getLVWRowCount(lvName)>0){
						for(int i=0;i<formObject.getLVWRowCount(lvName);i++){
							if("BT".equalsIgnoreCase(formObject.getNGValue(lvName,i,0))){
								ext_xml_str += addExtensionValue(entry.getKey(),entry.getValue(),"Y",extcount);
								extcount++;
								break;
							}
						}
						continue;
					}
				}
				else if(entry.getKey().contains("F164")){
					if(primary){
						String CardShield_val="N";
						String cardprod = formObject.getNGValue("CC_Creation_Product");
						String CardShieldQuery = "select CardShield from ng_rlos_IGR_Eligibility_CardLimit where Child_Wi='"+formObject.getWFWorkitemName()+"' and Card_Product = '"+cardprod+"'";
						//CreditCard.mLogger.info("CardShieldQuery: "+ CardShieldQuery);
						CardShield_val = formObject.getDataFromDataSource(CardShieldQuery).get(0).get(0);
						if( CardShield_val!=null && "false".equalsIgnoreCase(CardShield_val)){
							CardShield_val="N";
						}
						else if("true".equalsIgnoreCase(CardShield_val)){
							CardShield_val="Y";
						}
						else{
							CardShield_val="N";
						}

						ext_xml_str += addExtensionValue(entry.getKey(),entry.getValue(),CardShield_val,extcount);
					}
					extcount++;
				}
				else if(entry.getKey().contains("F160")){

					PersonalLoanS.mLogger.info("Inside F160");
					if(formObject.getNGValue("CC_Creation_Product").contains("KALYAN") && entry.getKey().contains("Kalyan")){
						String passport = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13);
						String cardprod = formObject.getNGValue("CC_Creation_Product");
						String lvName = entry.getValue().split(":")[1];
						if(formObject.getLVWRowCount(lvName)>0){
							for(int i=0;i<formObject.getLVWRowCount(lvName);i++){
								if(formObject.getNGValue(lvName,i,0).equals(cardprod) && formObject.getNGValue(lvName,i,10).equals(passport)){
									ext_xml_str += addExtensionValue(entry.getKey(),entry.getValue(),formObject.getNGValue(lvName,i,8), extcount);
									extcount++;
									break;
								}
							}
						}
					}
					else if(entry.getKey().contains("mrbh")) {
						try{
							if(typeOfCP!=null && typeOfCP.size()>0 && typeOfCP.get(0)!=null && "Islamic".equalsIgnoreCase(typeOfCP.get(0).get(0))){
								String query = "SELECT convert(date, Settlement_Date,103) as Settlement_Date FROM ng_rlos_Murabha_Warranty WHERE Murhabha_WIName='"+formObject.getWFWorkitemName()+"'";
								String  Settlement_Date = formObject.getDataFromDataSource(query).get(0).get(0);
								Settlement_Date = formatDateFromOnetoAnother(Settlement_Date,"yyyy-mm-dd","dd/mm/yyyy");
								ext_xml_str += addExtensionValue(entry.getKey(),entry.getValue(),Settlement_Date, extcount);
								extcount++;
							}
						}
						catch(Exception e){
							PersonalLoanS.mLogger.info("Exception in murhabah"+e.getMessage());
						}

					}
					continue;
				}

				else if(entry.getKey().contains("F161")){
					if(typeOfCP!=null && typeOfCP.size()>0 && typeOfCP.get(0)!=null && "Islamic".equalsIgnoreCase(typeOfCP.get(0).get(0))){
						String query = "SELECT File_Id FROM ng_rlos_Murabha_Warranty WHERE Murhabha_WIName='"+formObject.getWFWorkitemName()+"'";
						ext_xml_str += addExtensionValue(entry.getKey(),entry.getValue(),formObject.getDataFromDataSource(query).get(0).get(0), extcount);
						extcount++;
						continue;
					}
				}

				else if(entry.getKey().contains("F163")){
					String tarsegcode="PLBUN";
					ext_xml_str += addExtensionValue(entry.getKey(),entry.getValue(),tarsegcode,extcount);
					extcount++;
				
					//commented for PCASI-3026
					/*
					PersonalLoanS.mLogger.info("Inside F163");
					String emptype = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5);
					PersonalLoanS.mLogger.info("Inside F163: "+emptype);
					//String mktCode = "";
					String tarsegcode="";
					if(emptype.equalsIgnoreCase("Self Employed")){
						int rowCount = formObject.getLVWRowCount("cmplx_CompanyDetails_cmplx_CompanyGrid");
						if(rowCount>0){
							for(int i=0;i<rowCount;i++){
								if("Business".equalsIgnoreCase(formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i,1))){
									if(formObject.getNGValue("cmplx_Customer_CustomerSubSeg").equalsIgnoreCase("PAM")){
										tarsegcode="RELT";
									}
									else{
										tarsegcode = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",i,22);
									}
									//mktCode = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",i,28);
									ext_xml_str += addExtensionValue(entry.getKey(),entry.getValue(),tarsegcode,extcount);
									extcount++;
									break;
								}
							}
						}

					}
					else{
						if(formObject.getNGValue("cmplx_Customer_CustomerSubSeg").equalsIgnoreCase("PAM")){
							tarsegcode="RELT";
						}
						else{
							tarsegcode = formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode");
						}
						//mktCode = formObject.getNGValue("cmplx_EmploymentDetails_marketcode");
						ext_xml_str += addExtensionValue(entry.getKey(),entry.getValue(),tarsegcode,extcount);
						extcount++;
					}
				*/}
				else{
					String PorS = entry.getKey().split(":")[0];
					String value = formObject.getNGValue(entry.getValue().split(":")[1]);
					if(card_intrest_profile.toUpperCase().contains("SKYWARDS") && entry.getKey().contains("F169") ){
						value="";
					}
					else if(card_intrest_profile.toUpperCase().contains("SKYWARDS") && entry.getKey().contains("F170")){
						value="Y";
					}
					else if(card_intrest_profile.toUpperCase().contains("SKYWARDS") && entry.getKey().contains("F168") && !primary){
						for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_supplementGrid");i++){
							if(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13).equals(formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,3))){
								value=formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,42);
								break;
							}
						}
					}
					PersonalLoanS.mLogger.info("Inside PorS: "+entry.getValue());
					PersonalLoanS.mLogger.info("Inside value: " +value);
					if(value!=null && !"".equalsIgnoreCase(value)&& !"--Select--".equalsIgnoreCase(value)){
						//    value = ("True".equalsIgnoreCase(value) || "Yes".equalsIgnoreCase(value))?"Y":"N";
						if ("True".equalsIgnoreCase(value) || "Yes".equalsIgnoreCase(value)){
							value="Y";
						}
						else if("False".equalsIgnoreCase(value) || "No".equalsIgnoreCase(value)){
							value="N";
						}
						if("PS".equals(PorS) || ("P".equals(PorS) && primary)){
							ext_xml_str += addExtensionValue(entry.getKey(),entry.getValue(),value,extcount);
							extcount++;
						}
					}
				}
			}

			return ext_xml_str;
		}

		catch(Exception e){
			PersonalLoanS.mLogger.info("PLCommon getExtensions_details method"+ "Exception Occure in getReference_details");
			PLCommon.printException(e);
			return ext_xml_str;
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
			//Changes done by deepak 25 Nov 2018 to remove DocumentDet tag in case it dosent contain DocId start.
			if("CARD_NOTIFICATION".equalsIgnoreCase(Call_name) && "DocumentDet".equalsIgnoreCase(tempNode.getNodeName())){
				boolean removecompletenode_flag = true;
				for (int tempnode_len=0;tempnode_len<tempNode.getChildNodes().getLength();tempnode_len++){
					//Changes done for case when Emirates ID is not present. PCSP-722
					if("DocId".equalsIgnoreCase(tempNode.getChildNodes().item(tempnode_len).getNodeName()) && 
							!"".equalsIgnoreCase(tempNode.getChildNodes().item(tempnode_len).getTextContent())){
						removecompletenode_flag = false;
						break;
					}
				}

				if(removecompletenode_flag){
					node.removeChild(tempNode);
				}
				else{
					removeEmptyNodes(tempNode,Call_name);
				}
			}//Deepak Changes done for PCSP-367 03Feb2018
			else if("DocDet".equalsIgnoreCase(tempNode.getNodeName())){
				boolean removecompletenode_flag = true;
				for (int tempnode_len=0;tempnode_len<tempNode.getChildNodes().getLength();tempnode_len++){
					//Changes done for case when Emirates ID is not present. PCSP-722
					if("DocNo".equalsIgnoreCase(tempNode.getChildNodes().item(tempnode_len).getNodeName()) && 
							!"".equalsIgnoreCase(tempNode.getChildNodes().item(tempnode_len).getTextContent())){
						removecompletenode_flag = false;
						break;
					}
				}

				if(removecompletenode_flag){
					node.removeChild(tempNode);
				}
				else{
					removeEmptyNodes(tempNode,Call_name);
				}
			}
			else {
				removeEmptyNodes(tempNode,Call_name);
			}
		}
		//Changes done by deepak 25 Nov 2018 to remove DocumentDet tag in case it dosent contain DocId start.

		boolean emptyElement = node.getNodeType() == Node.ELEMENT_NODE && node.getChildNodes().getLength() == 0;
		boolean emptyText = node.getNodeType() == Node.TEXT_NODE && node.getNodeValue().trim().isEmpty();
		boolean selectText = node.getNodeType() == Node.TEXT_NODE && (node.getNodeValue().trim().equalsIgnoreCase("--Select--")||node.getNodeValue().trim().equalsIgnoreCase("null"));

		//Changes done to incorporate blank tag removal for all calls Deepak 24june 2018
		if (emptyElement || emptyText || selectText) {
			if(!node.hasAttributes()) {
				node.getParentNode().removeChild(node);
			}
		}

		/*//changes done to remove empty tags in Dectech only.
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
		}*/
	}


	//below functions added by akshay for drop 4
	public String getSupplement_PersonDetails(FormReference formObject,String callName){
		String str="";
		String Supplistviewname = "SupplementCardDetails_cmplx_SupplementGrid";

		try{
			Common_Utils common=new Common_Utils(PersonalLoanS.mLogger);
			PersonalLoanS.mLogger.info("Inside  getSupplement_PersonDetails() for callName: "+callName);
			PersonalLoanS.mLogger.info("MultipleApp_AppType: "+plcommonObj.MultipleAppGridSelectedRow("MultipleApp_AppType"));
			if(callName.equalsIgnoreCase("NEW_CUSTOMER_REQ")){
				if( "SUPPLEMENT".equalsIgnoreCase(plcommonObj.MultipleAppGridSelectedRow("MultipleApp_AppType")) && formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid")>0){ 
					for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid");i++){
						PersonalLoanS.mLogger.info("inside passport: "+plcommonObj.MultipleAppGridSelectedRow("MultipleApp_AppPass"));
						PersonalLoanS.mLogger.info("grid value: "+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,3));
						if(plcommonObj.MultipleAppGridSelectedRow("MultipleApp_AppPass").equals(plcommonObj.getGridColumnValue("Supplement_Passport_No",Supplistviewname,i))){
							PersonalLoanS.mLogger.info("inside passport match: ");
							str=str+"<PersonDetails><TitlePrefix>"+plcommonObj.getGridColumnValue("Supplement_Title",Supplistviewname,i)+"</TitlePrefix>";
							str=str+"<FirstName>"+plcommonObj.getGridColumnValue("Supplement_First_Name",Supplistviewname,i)+"</FirstName>";
							str=str+"<LastName>"+plcommonObj.getGridColumnValue("Supplement_Last_Name",Supplistviewname,i)+"</LastName>";
							str=str+"<ShortName>"+plcommonObj.getGridColumnValue("Supplement_First_Name",Supplistviewname,i).substring(0,1)+plcommonObj.getGridColumnValue("Supplement_First_Name",Supplistviewname,i).substring(0,1)+"</ShortName>";
							str=str+"<Gender>"+(plcommonObj.getGridColumnValue("Supplement_Gender",Supplistviewname,i).equalsIgnoreCase("Male")?"M":"F")+"</Gender>";
							str=str+"<MotherMaidenName>"+plcommonObj.getGridColumnValue("Supplement_Mother_Name",Supplistviewname,i)+"</MotherMaidenName>";
							//str=str+"<MinorFlag>"+(plcommonObj.getAge(plcommonObj.getGridColumnValue("Supplement_Date_Of_Birth",Supplistviewname,i))<21?"Y":"N")+"</MinorFlag>";
							str=str+"<MinorFlag>"+(Float.parseFloat(common.getAge(plcommonObj.getGridColumnValue("Supplement_Date_Of_Birth",Supplistviewname,i)))<21?"Y":"N")+"</MinorFlag>";

							str=str+"<NonResidentFlag>"+(plcommonObj.getGridColumnValue("Supplement_Non_Resident",Supplistviewname,i).equalsIgnoreCase("true")?"Y":"N")+"</NonResidentFlag>";
							str=str+"<ResCountry>"+plcommonObj.getGridColumnValue("Supplement_Resident_Country",Supplistviewname,i)+"</ResCountry>";
							str=str+"<MaritalStatus>"+plcommonObj.getGridColumnValue("Supplement_Marital_Status",Supplistviewname,i)+"</MaritalStatus>";
							str=str+"<Nationality>"+plcommonObj.getGridColumnValue("Supplement_Nationality",Supplistviewname,i)+"</Nationality>";
							str=str+"<DateOfBirth>"+common.Convert_dateFormat(plcommonObj.getGridColumnValue("Supplement_Date_Of_Birth",Supplistviewname,i), "dd/MM/yyyy","yyyy-MM-dd")+"</DateOfBirth>";
							str=str+"</PersonDetails>";								
							PersonalLoanS.mLogger.info("inside passport match str is: "+str);
							break;
						}
					}
				}
			}
			else if(callName.equalsIgnoreCase("CUSTOMER_UPDATE_REQ")){

				if(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)!=null && "SUPPLEMENT".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)))
				{ 
					for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid");i++){
						if(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13).equals(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,3))){
							str=str+"<RtlAddnlDet><MothersName>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,12)+"</MothersName>";
							str=str+"<MaritalStatus>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,37)+"</MaritalStatus>";
							str=str+"<EmploymentType>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,40)+"</EmploymentType>";
							str=str+"<EmployeeStatus>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,32)+"</EmployeeStatus>";						
							str=str+"<Desig>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,31)+"</Desig>";						
							break;
						}
					}
					str=str+getFATCA_details(formObject,"SUPPLEMENT");
					str=str+getKYC_details(formObject,"SUPPLEMENT");
					str=str+getCustOECD_details(formObject,"SUPPLEMENT");
					str=str+"</RtlAddnlDet>";	
				}
				else if(plcommonObj.MultipleAppGridSelectedRow("MultipleApp_AppType")!=null && "GUARANTOR".equalsIgnoreCase(plcommonObj.MultipleAppGridSelectedRow("MultipleApp_AppType")) && plcommonObj.MultipleAppGridSelectedRow("MultipleApp_AppPass").equals(formObject.getNGValue("cmplx_Guarantror_GuarantorDet",0,5)))
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
				if( "SUPPLEMENT".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)) && formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid")>0){ 
					String phno="",email="";	
					for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid");i++){
						if(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13).equals(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,3))){
							str=str+"<Title>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,14)+"</Title>";
							str=str+"<FirstName>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,0)+"</FirstName>";
							str=str+"<LastName>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,2)+"</LastName>";
							str=str+"<FullName>"+formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),4)+"</FullName>";
							str=str+"<MotherName>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,12)+"</MotherName>";
							str=str+"<DOB>"+common.Convert_dateFormat(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,4), "dd/MM/yyyy","yyyy-MM-dd")+"</DOB>";
							//str=str+"<Gender>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,11)+"</Gender>";
							if(formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,11).equals("M")){
								str=str+"<Gender>1</Gender>";
							}
							else{
								str=str+"<Gender>2</Gender>";
							}
							str=str+"<MaritalStatus>"+getMaritalstatusforSupplementary()+"</MaritalStatus>";
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
					//str=str+getCustAddress_details(callName);	
					//Deepak Code change for Address change 
					str=str+getCardCrationDummyAddress_details();
					str=str+"<PhoneFaxDtls><Type>StmtTelephone1</Type>";
					str=str+"<Number>"+phno+"</Number></PhoneFaxDtls>";
					str=str+"<EmailDtls><Type>StmtEmail</Type>";
					str=str+"<EmailId>"+email+"</EmailId></EmailDtls>";
					str=str+"<EmailDtls><Type>AdditionalEmail</Type>";
					str=str+"<EmailId>"+email+"</EmailId></EmailDtls>";
				}
			}

			else if(callName.equalsIgnoreCase("CARD_NOTIFICATION")){
				if( "SUPPLEMENT".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)) && formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid")>0){ 
					for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid");i++){
						if(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13).equals(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,3))){
							str=str+"<PersonDetails><TitlePrefix>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,14)+"</TitlePrefix>";
							str=str+"<FirstName>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,0)+"</FirstName>";
							str=str+"<LastName>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,2)+"</LastName>";
							str=str+"<ShortName>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,0).substring(0,1)+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,2).substring(0,1)+"</ShortName>";
							str=str+"<FullName>"+formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),4)+"</FullName>";
							str=str+"<Gender>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,11)+"</Gender>";
							str=str+"<MotherMaidenName>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,12)+"</MotherMaidenName>";
							//str=str+"<MinorFlag>"+(plcommonObj.getAge(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,4))<21?"Y":"N")+"</MinorFlag>";
							str=str+"<MinorFlag>"+(Float.parseFloat(common.getAge(formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,4)))<21?"Y":"N")+"</MinorFlag>";
							str=str+"<NonResidentFlag>"+(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,16).equalsIgnoreCase("true")?"Y":"N")+"</NonResidentFlag>";
							str=str+"<ResidencyStatus>EXPAT</ResidencyStatus>";
							str=str+"<MaritalStatus>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,40)+"</MaritalStatus>";
							str=str+"<Nationality>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,5)+"</Nationality>";
							str=str+"<DateOfBirth>"+common.Convert_dateFormat(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,4), "dd/MM/yyyy","yyyy-MM-dd")+"</DateOfBirth>";
							str=str+"<ResidenceSince>"+calcResidenceSince(formObject)+"</ResidenceSince>";
							str=str+"<ResCountry>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,8)+"</ResCountry>";
							str=str+"<ECRNNum>"+formObject.getNGValue("CC_Creation_ECRN")+"</ECRNNum>";
							str=str+"<CombinedCreditLimit>"+formObject.getNGValue("CC_Creation_CombinedLimit")+"</CombinedCreditLimit>";
							str=str+"</PersonDetails>";
							break;
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
		String Supplistviewname="";
		PersonalLoanS.mLogger.info("Inside  getGuarantor_DocDetails()");
		if(callName.equalsIgnoreCase("NEW_CUSTOMER_REQ")){
			if( "SUPPLEMENT".equalsIgnoreCase(plcommonObj.MultipleAppGridSelectedRow("MultipleApp_AppType")) && formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid")>0){
				Supplistviewname = "SupplementCardDetails_cmplx_SupplementGrid";
				for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid");i++){
					if(plcommonObj.MultipleAppGridSelectedRow("MultipleApp_AppPass").equals(plcommonObj.getGridColumnValue("Supplement_Passport_No",Supplistviewname,i))){
						str=str+generateDocDetailsTag_NewCustomerReq(Arrays.asList("PPT",plcommonObj.getGridColumnValue("Supplement_Passport_Issue_Date",Supplistviewname,i),plcommonObj.getGridColumnValue("Supplement_Passport_Expiry",Supplistviewname,i),plcommonObj.getGridColumnValue("Supplement_Nationality",Supplistviewname,i),plcommonObj.getGridColumnValue("Supplement_Passport_No",Supplistviewname,i)));
						str=str+generateDocDetailsTag_NewCustomerReq(Arrays.asList("VISA",plcommonObj.getGridColumnValue("Supplement_Visa_Issue_Date",Supplistviewname,i),plcommonObj.getGridColumnValue("Supplement_Visa_Expiry",Supplistviewname,i),"AE",plcommonObj.getGridColumnValue("Supplement_Visa_No",Supplistviewname,i)));
						str=str+generateDocDetailsTag_NewCustomerReq(Arrays.asList("EMID",plcommonObj.getGridColumnValue("Supplement_EID_Issue_Date",Supplistviewname,i),plcommonObj.getGridColumnValue("Supplement_EID_Expiry",Supplistviewname,i),plcommonObj.getGridColumnValue("Supplement_Nationality",Supplistviewname,i),plcommonObj.getGridColumnValue("Supplement_Emirate_ID",Supplistviewname,i)));
					}
				}
			}
		}
		else if(callName.equalsIgnoreCase("CUSTOMER_UPDATE_REQ")){
			if(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)!=null && "SUPPLEMENT".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)))
			{ 
				for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid");i++){
					if(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13).equals(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,3))){
						str=str+generateDocDetailsTag_CIFUpdate(Arrays.asList("PPT",formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,3),formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,36),formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,15)));
						str=str+generateDocDetailsTag_CIFUpdate(Arrays.asList("EMID",formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,7),formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,34),formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,18)));
						str=str+generateDocDetailsTag_CIFUpdate(Arrays.asList("VISA",formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,22),formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,35),formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,23)));
						break;
					}
				}
			}
			else if("GUARANTOR".equalsIgnoreCase(plcommonObj.MultipleAppGridSelectedRow("MultipleApp_AppType")) && plcommonObj.MultipleAppGridSelectedRow("MultipleApp_AppPass").equals(formObject.getNGValue("cmplx_Guarantror_GuarantorDet",0,5)))
			{
				str=str+generateDocDetailsTag_CIFUpdate(Arrays.asList("PPT",formObject.getNGValue("cmplx_Guarantror_GuarantorDet",0,5),formObject.getNGValue("cmplx_Guarantror_GuarantorDet",0,19),formObject.getNGValue("cmplx_Guarantror_GuarantorDet",0,16)));
				str=str+generateDocDetailsTag_CIFUpdate(Arrays.asList("VISA",formObject.getNGValue("cmplx_Guarantror_GuarantorDet",0,17),formObject.getNGValue("cmplx_Guarantror_GuarantorDet",0,20),formObject.getNGValue("cmplx_Guarantror_GuarantorDet",0,18)));
				str=str+generateDocDetailsTag_CIFUpdate(Arrays.asList("EMID",formObject.getNGValue("cmplx_Guarantror_GuarantorDet",0,2),formObject.getNGValue("cmplx_Guarantror_GuarantorDet",0,13),formObject.getNGValue("cmplx_Guarantror_GuarantorDet",0,14)));
			}
		}

		else  if(callName.equalsIgnoreCase("CARD_NOTIFICATION")){
			if("SUPPLEMENT".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)) && formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid")>0)
			{ 
				for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid");i++){
					if(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13).equals(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,3))){		 	
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
		else //if("GUARANTOR".equalsIgnoreCase(opName)  || ("PRIMARY".equalsIgnoreCase(opName) && "Disbursal".equalsIgnoreCase(formObject.getWFActivityName())))---commented by akshay on 25/4/18
		{
			multipleApplicantName=formObject.getNGValue("cmplx_Decision_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_Decision_MultipleApplicantsGrid"),1);
		}
		PersonalLoanS.mLogger.info("getFATCA_details multipleApplicantName: "+multipleApplicantName);

		try{
			Common_Utils common=new Common_Utils(PersonalLoanS.mLogger);
			String lvName = "cmplx_FATCA_cmplx_FATCAGrid";
			for(int i=0;i<formObject.getLVWRowCount(lvName);i++){
				String applicantName=formObject.getNGValue(lvName,i,13);
				String USRelation=formObject.getNGValue(lvName,i, 0);
				String TIN=formObject.getNGValue(lvName,i, 2);
				String fatcaReason=formObject.getNGValue(lvName,i, 10);
				String signedDate=formObject.getNGValue(lvName,i, 4);
				String expiryDate=formObject.getNGValue(lvName,i, 5);

				String iddoc=formObject.getNGValue("cmplx_FATCA_cmplx_FATCAGrid",i,6).equals("true")?"ID DOC":"";
				String w8form=formObject.getNGValue("cmplx_FATCA_cmplx_FATCAGrid",i,7).equals("true")?"!W8":"";
				String w9form=formObject.getNGValue("cmplx_FATCA_cmplx_FATCAGrid",i,8).equals("true")?"!W9":"";
				//String lossofnat=formObject.getNGValue("cmplx_FATCA_cmplx_FATCAGrid",i,10).equals("true")?"":"";
				String decforIndv=formObject.getNGValue("cmplx_FATCA_cmplx_FATCAGrid",i,11).equals("true")?"!SELF-ATTEST FORM":"";

				String FatcaDocs=iddoc+w8form+w9form+decforIndv;

				//changes by saurabh on 3rd Aug 18.
				if(fatcaReason!=null && !"".equals(fatcaReason)){
					//String query="select code+',' as code from ng_master_fatcaReasons where Description in('"+fatcaReason.replaceAll(",", "','")+"') for xml path('') ";
					//List<List<String>> result = formObject.getDataFromDataSource(query);
					//PersonalLoanS.mLogger.info("query for FATCA formed is: "+query);
					//fatcaReason=result.get(0).get(0);
					if(fatcaReason.endsWith(",")){
						fatcaReason=fatcaReason.substring(0,fatcaReason.length()-1);
					}
					fatcaReason=fatcaReason.replaceAll(",", "!");
				}
				else{
					fatcaReason="";
				}
				if(signedDate!=null && !"".equals(signedDate)){
					signedDate = common.Convert_dateFormat(signedDate,"dd/MM/yyyy","yyyy-MM-dd");
				}
				else{
					signedDate="";
				}
				if(expiryDate!=null && !"".equals(expiryDate)){
					expiryDate = common.Convert_dateFormat(expiryDate,"dd/MM/yyyy","yyyy-MM-dd");
				}
				else{
					expiryDate="";
				}
				if(applicantName.split("-")[1].equalsIgnoreCase(multipleApplicantName))
				{	
					str=str+"<USRelation>"+(USRelation.equals("NA")?"":USRelation)+"</USRelation>";
					str=str+"<TIN>"+TIN+"</TIN>";
					str=str+"<FatcaReason>"+fatcaReason+"</FatcaReason>";
					str=str+"<DocumentsCollected>"+FatcaDocs+"</DocumentsCollected>";
					str=str+"<SignedDate>"+signedDate+"</SignedDate>";
					str=str+"<SignedExpiryDate>"+expiryDate+"</SignedExpiryDate>";

					break;
				}
				PersonalLoanS.mLogger.info("getFATCA_details str formed is: "+str);
			}
		}catch(Exception ex){
			PersonalLoanS.mLogger.info("PLCommon getCustAddress_details method"+ "Exception Occure in generate Address XMl");
			plcommonObj.printException(ex);
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
		else //if("GUARANTOR".equalsIgnoreCase(opName)  || ("PRIMARY".equalsIgnoreCase(opName) && "Disbursal".equalsIgnoreCase(formObject.getWFActivityName())))
		{
			multipleApplicantName=plcommonObj.MultipleAppGridSelectedRow("MultipleApp_AppName");
		}
		PersonalLoanS.mLogger.info("getKYC_details multipleApplicantName: "+multipleApplicantName);

		for(int i=0;i<formObject.getLVWRowCount("cmplx_KYC_cmplx_KYCGrid");i++){
			String applicantName=formObject.getNGValue("cmplx_KYC_cmplx_KYCGrid",i, 3);
			String KYCHeld=formObject.getNGValue("cmplx_KYC_cmplx_KYCGrid",i, 0);
			String PEP=formObject.getNGValue("cmplx_KYC_cmplx_KYCGrid",i,1);
			if(applicantName.split("-")[1].equalsIgnoreCase(multipleApplicantName)){
				str=str+"<KYCHeld>"+(KYCHeld.equals("NA")?"":KYCHeld)+"</KYCHeld>";
				str=str+"<PEP>"+(PEP.equals("NA")?"":PEP)+"</PEP>";
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
			cal.add(Calendar.YEAR,(int) -Float.parseFloat(residenceSince));
			residenceSince_date=sdf1.format(cal.getTime());
			PersonalLoanS.mLogger.info("inside ResidenceSince Value of residenceSince_date: "+residenceSince_date);
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("exception occurred inside calcResidenceSince()");
			PLCommon.printException(e);
		}
		return residenceSince_date;
	}

	private Map<String, String> CIFEnquiryLockUnlock(List<List<String>> DB_List,FormReference formObject,String callName, String Operation_name) 
	{

		PersonalLoanS.mLogger.info("Inside CIFEnquiryLockUnlock()------call name is: "+callName);
		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();

		try{
			for (List<String> mylist : DB_List) {
				// for(int i=0;i<col_n.length();i++)
				for (int i = 0; i < 8; i++) {
					// PersonalLoanS.mLogger.info("rec: "+records.item(rec));
					//PersonalLoanS.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");
				// operation name chck and tag name 
				/*if(("CIFId".equalsIgnoreCase(tag_name) || "CCIFId".equalsIgnoreCase(tag_name)) && ("G_CIF_LOCK".equalsIgnoreCase(Operation_name) || "G_CIF_UNLOCK".equalsIgnoreCase(Operation_name)))
				{
					PersonalLoanS.mLogger.info("inside 1st if inside customer update req by hritik");
					String xml_str = int_xml.get(parent_tag);
					xml_str =xml_str+ "<"+tag_name+">"+formObject.getNGValue("cmplx_Customer_guarcif")+"</"+ tag_name+">";
					//PersonalLoanS.mLogger.info("PL COMMON  after adding CIFId:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	
					PersonalLoanS.mLogger.info("inside CCIFID: CIFid="+formObject.getNGValue("cmplx_Guarantror_GuarantorDet",formObject.getSelectedIndex("cmplx_Guarantror_GuarantorDet"),1));
				} //hritik 3353 */
				if( "SUPPLEMENT".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)))
				{
					PersonalLoanS.mLogger.info("inside supplement case");
					PersonalLoanS.mLogger.info("inside supplement case tag name: "+tag_name);
					if("CIFId".equalsIgnoreCase(tag_name) || "CCIFId".equalsIgnoreCase(tag_name) ){
						PersonalLoanS.mLogger.info("inside 1st if inside customer update req");
						String xml_str = int_xml.get(parent_tag);
						xml_str =xml_str+ "<"+tag_name+">"+formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),3)+"</"+ tag_name+">";
						//PersonalLoanS.mLogger.info("PL COMMON  after adding CIFId:  "+xml_str);
						int_xml.put(parent_tag, xml_str);	                            	
					}
					else{	
						int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
					}
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
					//PersonalLoanS.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}

				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");
				if("DSAId".equalsIgnoreCase(tag_name)){
					String xml_str = int_xml.get(parent_tag);
					PersonalLoanS.mLogger.info("PL COMMON"+ " before adding UpdateLoanDetails DSAId " + xml_str);
					xml_str = xml_str + "<"+tag_name+">"+formObject.getNGValue("DSA_Name")+"</"+ tag_name+">";
					PersonalLoanS.mLogger.info("PL COMMON"+ " after adding UpdateLoanDetails DSAId+ " + xml_str);
				}
				else if("ApplicationID".equalsIgnoreCase(tag_name) ){
					PersonalLoanS.mLogger.info("inside 1st if"+"inside update loan details1");
					String[] winum = formObject.getWFWorkitemName().split("-");
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<"+tag_name+">"+winum[1].substring(2)+"</"+ tag_name+">";

					PersonalLoanS.mLogger.info("PL COMMON"+" after adding ApplicationID:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}
				else if("DocDetails".equalsIgnoreCase(tag_name)){
					String xml_str = int_xml.get(parent_tag);
					PersonalLoanS.mLogger.info("PL COMMON"+ " before adding UpdateLoanDetails doc details " + xml_str);
					xml_str = xml_str + getUpdLoanDetails_IncomingDocDetails(formObject,callName);
					PersonalLoanS.mLogger.info("PL COMMON"+ " after adding UpdateLoanDetails docDetails+ " + xml_str);
					int_xml.put(parent_tag, xml_str);
					/*PersonalLoanS.mLogger.info("PL COMMON"+ " before Liability certificate grid 1+ " + xml_str);
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
					int_xml.put(parent_tag, xml_str);*/
				}
				else if("AdditionalAttributeDtls".equalsIgnoreCase(tag_name)){//AdditionalAttributeDtls
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
			PersonalLoanS.mLogger.info("CC Integration "+ " Exception occured in UPDATE_LOAN_DETAILS_Custom + ");
			new PLCommon().printException(e);
		}
		return int_xml;
	}
	//by shweta
	private Map<String, String> RISK_SCORE_DETAILS_Custom(List<List<String>> DB_List,FormReference formObject,String callName) {
		PersonalLoanS.mLogger.info("Inside RISK_SCORE_DETAILS_Custom()------call name is: "+callName);
		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();
		try{
			for (List<String> mylist : DB_List) {
				for (int i = 0; i < 8; i++) {
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}

				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");
				String form_control = recordFileMap.get("form_control");

				if(tag_name.equalsIgnoreCase("RequestInfo")){		
					String ifNTB = formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true")?"-1":"0";
					String xml_str="";
					//if(ifNTB.equalsIgnoreCase("-1")) {
						String referNo =formObject.getWFWorkitemName();
						xml_str = xml_str+ "<"+tag_name+">"+ "<RequestType>Reference Id</RequestType>";
						xml_str = xml_str + "<RequestValue>"+referNo+"</RequestValue>"+"</"+ tag_name+">";

						
						//Reference Id
					//}else {
						String CifId = formObject.getNGValue("cmplx_Customer_CIFNO");
						xml_str = xml_str+ "<"+tag_name+">"+ "<RequestType>CIF Id</RequestType>";
						xml_str = xml_str + "<RequestValue>"+CifId+"</RequestValue>"+"</"+ tag_name+">";					
						//int_xml.put(parent_tag, xml_str);
					//}
					int_xml.put(parent_tag, xml_str);
					PersonalLoanS.mLogger.info("int_xml" + int_xml);
				}
				else if (tag_name.equalsIgnoreCase("DSAId")){
					try{
						String xml_str = int_xml.get(parent_tag);
						String branchUserQuery = "select Introduce_By from NG_RLOS_EXTTABLE where WIname = (select parent_WIName from NG_PL_EXTTABLE where PL_Wi_Name='"+formObject.getWFWorkitemName()+"')";
						List<List<String>> result = formObject.getDataFromDataSource(branchUserQuery);
						if(result!=null && !result.isEmpty()){
							if(null!=result.get(0) && !result.get(0).isEmpty()){
								xml_str =xml_str+ "<"+tag_name+">"+result.get(0).get(0)+"</"+ tag_name+">";
							}
						}
						int_xml.put(parent_tag, xml_str);
						PersonalLoanS.mLogger.info("int_xml" + int_xml);

					} catch(Exception ex){
						PersonalLoanS.mLogger.info("inside exception in DSAId: ");
						new PLCommon().printException(ex);
					}	

				}  else if (tag_name.equalsIgnoreCase("IsPoliticallyExposed")){
					String strPEP="N";
					if (formObject.getNGValue("cmplx_KYC_PEP")!=null){
						strPEP=formObject.getNGValue("cmplx_KYC_PEP");
						strPEP=strPEP.equals("NA")?"N":"Y";			
					}	
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<"+tag_name+">"+strPEP+"</"+ tag_name+">";
					int_xml.put(parent_tag, xml_str);
					PersonalLoanS.mLogger.info("int_xml" + int_xml);

				}  else if (tag_name.equalsIgnoreCase("CustomerName")){
					String custFullName=formObject.getNGValue("cmplx_Customer_FIrstNAme");
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<"+tag_name+">"+custFullName+"</"+ tag_name+">";
					int_xml.put(parent_tag, xml_str);
					PersonalLoanS.mLogger.info("int_xml" + int_xml);


				} 
				else if (tag_name.equalsIgnoreCase("CustomerCategory")){
					String customerCat="Resident Individual";
					/*String custCat=formObject.getNGValue("cmplx_Customer_CustomerCategory");
					// select  description
					String sQuery="select description from NG_MASTER_CustomerCategory with (nolock)  WHERE Code="+custCat ;
					List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
					if(!OutputXML.isEmpty()){
						customerCat = OutputXML.get(0).get(0);
					}*/
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<"+tag_name+">"+customerCat+"</"+ tag_name+">";
					int_xml.put(parent_tag, xml_str);
					PersonalLoanS.mLogger.info("int_xml" + int_xml);
				} 
				//Change for nationality
				else if (tag_name.equalsIgnoreCase("Nationality")){
					try{
						String xml_str = int_xml.get(parent_tag);
						String country_name = formObject.getNGValue(form_control);
						String nationalityQuery = "select Description from NG_MASTER_Country with(nolock) where Code = '"+country_name+"'";
						PersonalLoanS.mLogger.info("nationality"+nationalityQuery);
						//nationalityQuery=nationalityQuery.substring(0,nationalityQuery.lastIndexOf("-"));
						List<List<String>> result = formObject.getDataFromDataSource(nationalityQuery);
						PersonalLoanS.mLogger.info("int_xml_result" + result);
						if(result!=null && !result.isEmpty()){
							if(null!=result.get(0) && !result.get(0).isEmpty()){
								String s=result.get(0).get(0);
								xml_str =xml_str+ "<"+tag_name+">"+s.substring(0,s.lastIndexOf("-"))+"</"+ tag_name+">";
								PersonalLoanS.mLogger.info("nationality check final "+xml_str+s);
							}
						
						}
						
						/*if (formObject.getNGValue("cmplx_Customer_Third_Nationaity_Applicable").equalsIgnoreCase("Yes")){
							nationalityQuery = "select Description from NG_MASTER_Country with(nolock) where Code = '"+formObject.getNGValue("cmplx_Customer_Third_Nationaity")+"'";
							PersonalLoanS.mLogger.info("nationality"+nationalityQuery);
							//nationalityQuery=nationalityQuery.substring(0,nationalityQuery.lastIndexOf("-"));
							List<List<String>> result1 = formObject.getDataFromDataSource(nationalityQuery);
							PersonalLoanS.mLogger.info("int_xml_result1" + result1);
							if(result1!=null && !result1.isEmpty()){
								if(null!=result1.get(0) && !result1.get(0).isEmpty()){
									String s=result1.get(0).get(0);
									xml_str =xml_str+ "<"+tag_name+">"+s.substring(0,s.lastIndexOf("-"))+"</"+ tag_name+">";
									PersonalLoanS.mLogger.info("nationality check final "+xml_str+s);
								}
							
							}
							
							
						}*///By Alok Tiwari for Third Nationality
						
						int_xml.put(parent_tag, xml_str);
						PersonalLoanS.mLogger.info("int_xml" + int_xml);


					} catch(Exception ex){
						PersonalLoanS.mLogger.info("inside exception in DSAId: ");
						new PLCommon().printException(ex);
					}	
				}
				else if("EmploymentType".equalsIgnoreCase(tag_name)){
					try{
						String xml_str = int_xml.get(parent_tag);
						String emp_val = formObject.getNGValue("cmplx_EmploymentDetails_Emp_Type");
						if ("S".equalsIgnoreCase(emp_val)){
							emp_val="salaried";
						}
						String EmploymentTypeQuery = "select top 1 Description from ng_MASTER_EmploymentType with(nolock) where Code = '"+emp_val+"'";
						PersonalLoanS.mLogger.info("EmploymentType: "+EmploymentTypeQuery);
						//nationalityQuery=nationalityQuery.substring(0,nationalityQuery.lastIndexOf("-"));
						List<List<String>> result = formObject.getDataFromDataSource(EmploymentTypeQuery);
						
						if(result!=null && !result.isEmpty()){
							if(null!=result.get(0) && !result.get(0).isEmpty()){
								String tag_val=result.get(0).get(0);
								xml_str =xml_str+ "<"+tag_name+">"+tag_val+"</"+ tag_name+">";
								PersonalLoanS.mLogger.info("EmploymentType check final "+xml_str);
							}
						}
						int_xml.put(parent_tag, xml_str);
						PersonalLoanS.mLogger.info("int_xml" + int_xml);


					} catch(Exception ex){
						PersonalLoanS.mLogger.info("inside exception in EmploymentType: ");
						new PLCommon().printException(ex);
					}	
				}

				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
				}

			}
		} catch(Exception e){
			PersonalLoanS.mLogger.info("CC Integration "+ " Exception occured in RISK_SCORE_DETAILS_Custom + ");
			new PLCommon().printException(e);
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
			PersonalLoanS.mLogger.info( "value of Loan_Attribute_Value for GridName : "+GridName+" is : "+Loan_Attribute_Value);
			if(Loan_Attribute_Value!=null && !"".equals(Loan_Attribute_Value)){
				String Doc_Name = "";
				if(Loan_Attribute_Value.indexOf("_")>-1){
					Doc_Name=Loan_Attribute_Value.substring(0,Loan_Attribute_Value.lastIndexOf("_"));
				}
				else if(Loan_Attribute_Value.indexOf(" ")>-1){
					Doc_Name=Loan_Attribute_Value.substring(0,Loan_Attribute_Value.lastIndexOf(" "));	
				}
				String sQuery = "";
				if(!"NO_LIABILITY_CERTIFICATE".equalsIgnoreCase(Loan_Attribute_Value)){
					sQuery = "select code from NG_MASTER_LoanAttributeMis with (nolock) where description like '"+Doc_Name+"%'";
				}
				else{
					String status = formObject.getNGValue("cmplx_PostDisbursal_cpmlx_gr_NLC", i, 6);
					sQuery = "select code from NG_MASTER_LoanAttributeMis with (nolock) where description = 'NO_LIABILITY_CERTIFICATE-"+status+"'";
				}
				PersonalLoanS.mLogger.info( "value of sQuery is : "+sQuery);
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
		}
		PersonalLoanS.mLogger.info( "DocDetails tag Cration: "+ add_xml_str);
		return add_xml_str;
	}


	public static String getUpdLoanDetails_IncomingDocDetails(FormReference formObject,String callName){
		PersonalLoanS.mLogger.info( "inside getUpdLoanDetails_details : ");
		try{
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

					Doc_Type=repObj.getValue(j, "cmplx_DocName_DocName");//
					Doc_Expected_Date=repObj.getValue(j, "cmplx_DocName_Deferred_Until");
					String StatusValue=repObj.getValue(j,"cmplx_DocName_Doc_Status")==null?"":repObj.getValue(j,"cmplx_DocName_Doc_Status");

					if ((Doc_Type.contains("NO_LIABILITY_CERTIFICATE") || Doc_Type.contains("LIABILITY_CERTIFICATE") || Doc_Type.contains("Salary_Transfer_letter")) && ("Received".equalsIgnoreCase(StatusValue) ||"Deferred".equalsIgnoreCase(StatusValue) )){
						Doc_Expiry_Date=repObj.getValue(j, "cmplx_DocName_ExpiryDate");
						Doc_Received_Date=(new Date()).toString();
						//if(!"".equalsIgnoreCase(Bank_Name) || !Bank_Name.isEmpty() || !"null".equalsIgnoreCase(Bank_Name))
						//{
						Remarks=repObj.getValue(j, "cmplx_DocName_Remarks");
						//}	

						String Doc_Name = Doc_Type.substring(0, Doc_Type.lastIndexOf("_"));	
						//String Doc_Name[]=Doc_Type.split("_");
						String sQuery = "select code from NG_MASTER_DocType with (nolock) where description like '"+Doc_Name+"%'";
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
						add_xml_str = add_xml_str + "<DocIssueDate></DocIssueDate>";//"+common.Convert_dateFormat(Doc_Issue_Date, "dd/MM/yyyy","yyyy-MM-dd")+"
						add_xml_str = add_xml_str + "<DocExpiryDate></DocExpiryDate>";//"+common.Convert_dateFormat(Doc_Expiry_Date, "dd/MM/yyyy","yyyy-MM-dd")+"
						if("Received".equalsIgnoreCase(StatusValue)){
							add_xml_str = add_xml_str + "<DocReceivedDate>"+getLatestDate(Doc_Type)+"</DocReceivedDate>";//"<DocReceivedDate>"+common.Convert_dateFormat(Doc_Received_Date, "dd/MM/yyyy","yyyy-MM-dd")+"</DocReceivedDate>";
						}
						if("Deferred".equalsIgnoreCase(StatusValue)){
							add_xml_str = add_xml_str + "<DocExpectedDate>"+common.Convert_dateFormat(Doc_Expected_Date, "dd/MM/yyyy","yyyy-MM-dd")+"</DocExpectedDate>";
						}
						add_xml_str = add_xml_str + "<CountryOfIssue>"+country+"</CountryOfIssue>";
						add_xml_str = add_xml_str + "<PlaceOfIssue>"+city+"</PlaceOfIssue>";
						add_xml_str = add_xml_str + "<DocLocation>"+Doc_Location+"</DocLocation>";

						add_xml_str = add_xml_str + "<DocRefNum>46</DocRefNum>";//temporary.

						add_xml_str = add_xml_str + "<DocumentExpiryFlag>"+Document_Expiry_Flag+"</DocumentExpiryFlag>";
						if(Doc_Type.contains("NO_LIABILITY_CERTIFICATE") || Doc_Type.contains("LIABILITY_CERTIFICATE")){
							add_xml_str = add_xml_str + "<Remarks>"+getFormattedRemarks(Doc_Type)+" "+Remarks+"</Remarks>";//<Remarks>"+Remarks+"</Remarks></AddressDetails>";
						}
						else{
							add_xml_str = add_xml_str + "<Remarks>"+Remarks+"</Remarks>";	
						}
						add_xml_str = add_xml_str + "</DocDetails>";
					}
				}
				PersonalLoanS.mLogger.info( "DocDetails tag Cration: "+ add_xml_str);
			}
			return add_xml_str;
		}catch(Exception e){
			PersonalLoanS.mLogger.info("Pl Integration Exception occured in getUpdLoanDetails_IncomingDocDetails + "+e.getMessage());
			new PLCommon().printException(e);
			return "";
		}
	}

	public static String getLatestDate(String Doc_Type){
		try{
			Common_Utils common=new Common_Utils(PersonalLoanS.mLogger);
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			if(formObject.isVisible("PostDisbursal_Frame1")){
				if("NO_LIABILITY_CERTIFICATE".equalsIgnoreCase(Doc_Type)){
					if(formObject.isVisible("PostDisbursal_Frame4")){
						if(formObject.getLVWRowCount("cmplx_PostDisbursal_cpmlx_gr_NLC")>0){
							String maxDate = "";
							for(int i=0;i<formObject.getLVWRowCount("cmplx_PostDisbursal_cpmlx_gr_NLC");i++){
								String tempDate = formObject.getNGValue("cmplx_PostDisbursal_cpmlx_gr_NLC",i,0); 
								if("".equalsIgnoreCase(maxDate)){
									maxDate = tempDate;
									continue;
								}
								else{
									if(new Date().parse(maxDate)<new Date().parse(tempDate)){
										maxDate = tempDate;
										continue;
									}
									else{
										continue;
									}
								}
							}
							return common.Convert_dateFormat(maxDate, "dd/MM/yyyy","yyyy-MM-dd");
						}
						else{
							return "";
						}
					}
					else{
						return "";
					}
				}
				else if("LIABILITY_CERTIFICATE".equalsIgnoreCase(Doc_Type)){
					if(formObject.isVisible("PostDisbursal_Frame2")){
						if(formObject.getLVWRowCount("cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate")>0){
							String maxDate = "";
							for(int i=0;i<formObject.getLVWRowCount("cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate");i++){
								String tempDate = formObject.getNGValue("cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate",i,0); 
								if("".equalsIgnoreCase(maxDate)){
									maxDate = tempDate;
									continue;
								}
								else{
									if(new Date().parse(maxDate)<new Date().parse(tempDate)){
										maxDate = tempDate;
										continue;
									}
									else{
										continue;
									}
								}
							}
							return common.Convert_dateFormat(maxDate, "dd/MM/yyyy","yyyy-MM-dd");
						}
						else{
							return "";
						}
					}
					else{
						return "";
					}
				}
				else if("Salary_Transfer_letter".equalsIgnoreCase(Doc_Type)){
					if(formObject.isVisible("PostDisbursal_Frame5")){
						String recDt = formObject.getNGValue("cmplx_PostDisbursal_STLReceivedDate");
						if(recDt!=null && "".equals(recDt)){
							return common.Convert_dateFormat(recDt, "dd/MM/yyyy","yyyy-MM-dd");
						}
						else{
							return "";
						}
					}
					else{
						return "";
					}
				}
			}
			return "";


		}catch(Exception ex){
			PersonalLoanS.mLogger.info("Pl Integration Exception occured in getDocReceivedDate + "+ex.getMessage());
			return "";
		}

	}

	public static String getFormattedRemarks(String DocType){
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String str = getLatestDate(DocType);
			if(!str.equals("")){
				String listViewName = "";
				if(DocType.equals("LIABILITY_CERTIFICATE")){
					listViewName = "cmplx_PostDisbursal_cmplx_gr_LiabilityCertificate";
				}
				else if(DocType.equals("NO_LIABILITY_CERTIFICATE")){
					listViewName = "cmplx_PostDisbursal_cpmlx_gr_NLC";
				}
				for(int i=0;i<formObject.getLVWRowCount(listViewName);i++){
					if(formObject.getNGValue(listViewName,i,0).equals(str)){
						return formObject.getNGValue(listViewName,i,1);
					}
				}
				return "";
			}
			return "";
		}
		catch(Exception ex){
			PersonalLoanS.mLogger.info("Pl Integration Exception occured in getDocReceivedDate + "+ex.getMessage());
			return "";
		}
	}

	public String  getRejectedDetails(){
		PersonalLoanS.mLogger.info( "inside getCustAddress_details : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "SELECT Rejected_cust,Rejected_Date,Rejected_reason,Rejected_product,Rejected_app_id FROM Ng_Rlos_Dectech_Rejected_App_Data  with (nolock) where Wi_Name = '"+formObject.getWFWorkitemName()+"'";
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		for (int i = 0; i<OutputXML.size();i++){
			String Rejected_cust = "";
			String Rejected_Date = "";
			String Rejected_reason = "";
			String Rejected_product = ""; 
			String Rejected_app_id = "";
			if(!(OutputXML.get(i).get(0) == null || "".equalsIgnoreCase(OutputXML.get(i).get(0))) ){
				Rejected_cust = OutputXML.get(i).get(0);
			}
			if(!(OutputXML.get(i).get(1) == null || "".equalsIgnoreCase(OutputXML.get(i).get(1))) ){
				Rejected_Date = OutputXML.get(i).get(1);
			}
			if(!(OutputXML.get(i).get(2) == null || "".equalsIgnoreCase(OutputXML.get(i).get(2))) ){
				Rejected_reason = OutputXML.get(i).get(2);
			}
			if(!(OutputXML.get(i).get(3) == null || "".equalsIgnoreCase(OutputXML.get(i).get(3))) ){
				Rejected_product = OutputXML.get(i).get(3);
			}
			if(!(OutputXML.get(i).get(4) == null || "".equalsIgnoreCase(OutputXML.get(i).get(4))) ){
				Rejected_app_id = OutputXML.get(i).get(4);
			}
			add_xml_str = add_xml_str + "<RejectedDetails><rejected_cust>"+Rejected_cust+"</rejected_cust>";
			add_xml_str = add_xml_str + "<rejected_date>"+Rejected_Date+"</rejected_date>";
			add_xml_str = add_xml_str + "<rejected_reason>"+Rejected_reason+"</rejected_reason>";
			add_xml_str = add_xml_str + "<rejected_product>"+Rejected_product+"</rejected_product>";
			add_xml_str = add_xml_str + "<rejected_app_id>"+Rejected_app_id+"</rejected_app_id></RejectedDetails>";
		}
		PersonalLoanS.mLogger.info( "Internal liab tag Cration: "+ add_xml_str);
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

	//Deepak new method added for fetchAddressFlagVal
	public String fetchAddressFlagVal() {
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();	
			String AddressFlagVal = "-1";
			String listView = "cmplx_CCCreation_cmplx_CCCreationGrid";
			String Cust_type = formObject.getNGValue(listView,formObject.getSelectedIndex(listView),12);
			String selectPassport = formObject.getNGValue(listView,formObject.getSelectedIndex(listView),13);
			//++below code added by nikhil for Self-Supp CR
			if("SUPPLEMENT".equalsIgnoreCase(Cust_type) && formObject.getNGValue("cmplx_Customer_PAssportNo").equalsIgnoreCase(selectPassport))
			{
				Cust_type="PRIMARY";
			}
			//--above code added by nikhil for Self-Supp CR
			String selectCIF = formObject.getNGValue(listView,formObject.getSelectedIndex(listView),3);
			int rowcount = formObject.getLVWRowCount(listView);
			int createdCards = 0;
			for(int i=0;i<rowcount;i++){
				String gridPassprtValue = formObject.getNGValue(listView,i,13);
				String gridCifValue = formObject.getNGValue(listView,i,3);
				if(formObject.getNGValue(listView, i, 8).equals("Y") && gridPassprtValue.equalsIgnoreCase(selectPassport) && gridCifValue.equalsIgnoreCase(selectCIF)){
					createdCards++;
					break;
				}
			}
			if(createdCards>0){
				AddressFlagVal="0";
				return AddressFlagVal;
			}
			if(Cust_type.equalsIgnoreCase("Primary")){
				String query = "select count(distinct ECRN) from ng_RLOS_CUSTEXPOSE_CustInfo with (nolock) where Child_Wi = '"+formObject.getWFWorkitemName()+"' and CifId= '"+selectCIF+"' and ecrn !='' and ecrn is not null and ecrn not in (select ECRN from ng_cas_rejected_table where CIF= '"+selectCIF+"' and CRN=ECRN)";
				List<List<String>> count = formObject.getNGDataFromDataCache(query);

				if(count!=null && count.size()>0 && count.get(0)!=null){
					if(Integer.parseInt(count.get(0).get(0))>0){
						AddressFlagVal="0";
					}
				}
			}
			PersonalLoanS.mLogger.info("Inside fetchAddressFlagVal function before return is: "+AddressFlagVal);
			return AddressFlagVal;
		}catch(Exception ex){
			PersonalLoanS.mLogger.info("CC_INtegration Exception in fetchAddressFlagVal) Function:  ");
			PLCommon.printException(ex);
			return "";
		}


	}

	public String getCodeDesc(String Master_Name,String Col_name,String Code){
		String desc = "";
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String query="select "+Col_name+" from "+Master_Name+" with (nolock) where code='"+Code+"' AND isActive='Y'";
			PersonalLoanS.mLogger.info("query name :"+query);
			List<List<String>> result=formObject.getDataFromDataSource(query);
			if(!result.isEmpty()){
				if(null!=result.get(0).get(0) && !result.get(0).get(0).equals("")){
					desc = result.get(0).get(0);
				}
			}
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("CC Integration exception in getCodeDesc: "+ e.getMessage());
		}
		return desc;

	}

	private String getCardCrationDummyAddress_details() {
		// TODO Auto-generated method stub
		return "<AddressDetails>"
		+"<AddrType>Statement</AddrType>"
		+"<UseExistingAddress>1</UseExistingAddress>"
		+"<AddressReferenceLink>1</AddressReferenceLink>"
		+"</AddressDetails>"
		+"<AddressDetails>"
		+"<AddrType>Additional</AddrType>"
		+"<UseExistingAddress>0</UseExistingAddress>"        
		+"<AddressReferenceLink>0</AddressReferenceLink>"
		+"</AddressDetails>";
	}

	public String getSupplementaryTagValue(int colIndex){
		try{
			String stat = "";
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String listViewName = "SupplementCardDetails_cmplx_supplementGrid";
			String passport = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13);
			String cardprod = formObject.getNGValue("CC_Creation_Product");

			for(int i=0;i<formObject.getLVWRowCount(listViewName);i++){
				String gridPassport = formObject.getNGValue(listViewName,i,3);
				String gridcardProd = formObject.getNGValue(listViewName,i,30);
				if(passport.equalsIgnoreCase(gridPassport) && cardprod.equalsIgnoreCase(gridcardProd)){
					stat = formObject.getNGValue(listViewName,i,colIndex);
					break;
				}
			}
			return stat;
		}catch(Exception ex){
			PersonalLoanS.mLogger.info("Exception in getting Marital Status:  ");
			PLCommon.printException(ex);
			return "";
		}

	}

	public String getMaritalstatusforSupplementary(){
		//CreditCard.mLogger.info("inside 1st if"+"inside maritial NEW_CREDITCARD_REQ");
		String maritalstat = getSupplementaryTagValue(40);
		String marital="5";
		if(maritalstat.equalsIgnoreCase("M")){
			marital="2";
		}
		else if(maritalstat.equalsIgnoreCase("S")){
			marital="1";
		}
		else if(maritalstat.equalsIgnoreCase("D")){
			marital="3";
		}
		else if(maritalstat.equalsIgnoreCase("W")){
			marital="4";
		}


		PersonalLoanS.mLogger.info("PL COMMON"+" after adding maritia] NEW_CREDITCARD_REQ:  "+marital);
		return marital;

	}

	//Deepak new method created for getCardRefNumbervalue
	private String getCardRefNumbervalue() {
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String selected_card_product = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),5);
			String selected_cust_product = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12);
			//CreditCard.mLogger.info(" In getCardRefNumbervalue, selected_card_product: "+selected_card_product);
			//CreditCard.mLogger.info(" In getCardRefNumbervalue, selected_cust_product: "+selected_cust_product);
			int add_row_count = formObject.getLVWRowCount("cmplx_CCCreation_cmplx_CCCreationGrid");
			String CRN_val="";
			if("SUPPLEMENT".equalsIgnoreCase(selected_cust_product)){
				for(int row_count = 0; row_count<add_row_count;row_count++){
					String card_product = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",row_count,5);
					String customer_type = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",row_count,12);
					//CreditCard.mLogger.info(" In getCardRefNumbervalue, card_product: "+card_product);
					//CreditCard.mLogger.info(" In getCardRefNumbervalue, customer_type: "+customer_type);

					if(selected_card_product.equalsIgnoreCase(card_product) && "Primary".equalsIgnoreCase(customer_type)){
						CRN_val = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",row_count,1);
						break;
					}
				}
			}
			else{
				CRN_val = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),1);
			}
			return CRN_val;
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("CC Integration "+ " Exception occured in getCardRefNumbervalue + "+e.getMessage());
			return "";
		}
	}

	//Deepak new method created for LinkageType_refval of kalyan card 707
	private String getLinkageType_refval(String kalyan_no) {

		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String sQuery = "select count(REWARD_ACCOUNT_NUMBER) from CAPS_Kalyan_Ref_Details_Mig where REWARD_ACCOUNT_NUMBER ='"+kalyan_no+"'";
			List<List<String>> DB_List=formObject.getDataFromDataSource(sQuery);
			if( "0".equalsIgnoreCase(DB_List.get(0).get(0))){
				return "A";
			}
			else{
				return "T"; 
			}
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("CC Integration "+ " Exception occured in getLinkageType_refval + "+e.getMessage());
			return "T";
		}
	}

	public String addExtensionValue(String mapKey,String mapValue, String value,int extcount_val){
		try{
			//below code added by nikhil for extension fields CR
			/*					if(mapKey.contains("F160") || mapKey.contains("F161"))
				{
					return "<Extensions><Type>"+mapKey.split(":")[1]+"</Type><Number>"+extcount_val+"</Number><Value>"+mapValue.split(":")[0]+" "+value+"</Value></Extensions>";
				}
				else
				{*/
			return "<Extensions><Type>"+mapKey.split(":")[1]+"</Type><Number>F"+extcount_val+"</Number><Value>"+mapValue.split(":")[0]+ value+"</Value></Extensions>";
			//}
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("Exception occured in addExtensionValue: ");
			PLCommon.printException(e);
			return"";
		}

	}

	public String formatDateFromOnetoAnother(String date,String givenformat,String resultformat) {
		String result = "";
		SimpleDateFormat givenDateformat;
		SimpleDateFormat resultDateformat;
		try {
			givenDateformat = new SimpleDateFormat(givenformat);
			resultDateformat = new SimpleDateFormat(resultformat);
			result = resultDateformat.format(givenDateformat.parse(date));
		}
		catch(Exception e) {
			e.printStackTrace();
			return date;
		}
		finally {
			givenDateformat=null;
			resultDateformat=null;
		}
		return result;
	}

	public  String getSkywardNo(String skywardno){
		String updatedSkyNO=skywardno;
		try{
			if(!"".equalsIgnoreCase(updatedSkyNO)){
				int n= skywardno.length()-9;
				if(skywardno.startsWith("EK")){
					updatedSkyNO=skywardno.replace("EK", "00");
				}
				else if(skywardno.length()>9){
					updatedSkyNO = "00" +skywardno.substring(n, skywardno.length());
				}
				else{
					updatedSkyNO = "00"+skywardno;
				}
			}     
		}
		catch(Exception ex){
			PersonalLoanS.mLogger.info("getSkywardNo Exception in getSkywardNo custom function:  "+ex.getMessage());
		}
		return updatedSkyNO;

	}
	//added by shweta
	private Map<String, String> COMPLIANCE_CHECK_Custom(List<List<String>> DB_List,FormReference formObject,String callName,String Operation_name) {
		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();

		//FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try{
			for (List<String> mylist : DB_List) {
				// for(int i=0;i<col_n.length();i++)
				for (int i = 0; i < 8; i++) {
					// SKLogger_CC.writeLog("rec: "+records.item(rec));
					//		RLOS.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");
				if("ReferenceNo".equalsIgnoreCase(tag_name) )
				{
					PersonalLoanS.mLogger.info("Compliacnce check WINumber");
										
					String ReferenceNo = PersonalLoanSCommonCode.ReferenceNo;
					PersonalLoanS.mLogger.info("Compliacnce check WINumber"+ ReferenceNo);
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str+  "<"+tag_name+">"+ReferenceNo
							+"</"+ tag_name+">";
					int_xml.put(parent_tag, xml_str);
					PersonalLoanS.mLogger.info("Compliacnce check xml_str: "+ xml_str);
				}
				else if ("Primary".equalsIgnoreCase(Operation_name)){
					if("EntityName".equalsIgnoreCase(tag_name) )
					{
						String LastName =formObject.getNGValue("cmplx_Customer_LAstNAme");
						String MiddleName=formObject.getNGValue("cmplx_Customer_MiddleName");
						String FirstName=formObject.getNGValue("cmplx_Customer_FIrstNAme");
						String full_name=LastName+", "+FirstName+" "+MiddleName;
						//String recordType=getCustomerType();
						/*if(recordType.equalsIgnoreCase("C"))
									full_name=formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",1,4);*/
						String xml_str = int_xml.get(parent_tag);
						xml_str =xml_str+ "<"+tag_name+">"+full_name
								+"</"+ tag_name+">";
						int_xml.put(parent_tag, xml_str);
					}
					else if("Gender".equalsIgnoreCase(tag_name))
					{
						String desc = "";
						String gender =formObject.getNGValue("cmplx_Customer_gender");
						if(gender !=null && gender.equalsIgnoreCase("M")){
							desc="Male";
						} else if( gender !=null && gender.equalsIgnoreCase("F")){
							desc="Female";
						} else{
							desc="NOT AVAILABLE";
						}
						String xml_str = int_xml.get(parent_tag);
						xml_str =xml_str+ "<"+tag_name+">"+desc
								+"</"+ tag_name+">";
						int_xml.put(parent_tag, xml_str);
					}
					else if("PassportExpiryDate".equalsIgnoreCase(tag_name) )
					{
						String PassportExpiry=formObject.getNGValue("cmplx_Customer_PassPortExpiry");//8
						PassportExpiry=new Common_Utils(PersonalLoanS.mLogger).Convert_dateFormat(PassportExpiry, "dd/mm/yyyy", "dd-mm-yyyy");
						String xml_str = int_xml.get(parent_tag);
						xml_str =xml_str+ "<"+tag_name+">"+PassportExpiry
								+"</"+ tag_name+">";
						int_xml.put(parent_tag, xml_str);
					}
					else if("DateOfBirthOrIncorporation".equalsIgnoreCase(tag_name) )
					{
						String DOB="";
						DOB=formObject.getNGValue("cmplx_Customer_DOb");
						DOB=new Common_Utils(PersonalLoanS.mLogger).Convert_dateFormat(DOB, "dd/mm/yyyy", "dd-mm-yyyy");
						String xml_str = int_xml.get(parent_tag);
						xml_str =xml_str+ "<"+tag_name+">"+DOB
								+"</"+ tag_name+">";
						int_xml.put(parent_tag, xml_str);
					}
					else{
						int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
					}
				}  else if ("SUPPLEMENT".equalsIgnoreCase(Operation_name)){
					if("EntityName".equalsIgnoreCase(tag_name) )
					{
						String LastName="";
						String MiddleName="";
						String FirstName="";
						String full_name="";							
						LastName =formObject.getNGValue("SupplementCardDetails_FirstName");
						MiddleName=formObject.getNGValue("SupplementCardDetails_MiddleName");
						FirstName=formObject.getNGValue("SupplementCardDetails_FirstName");
						full_name=LastName+", "+FirstName+" "+MiddleName;

						String xml_str = int_xml.get(parent_tag);
						xml_str =xml_str+ "<"+tag_name+">"+full_name
								+"</"+ tag_name+">";
						int_xml.put(parent_tag, xml_str);
					}
					else if("Gender".equalsIgnoreCase(tag_name) )
					{
						String desc = "";
						String gender="";
						gender =formObject.getNGValue("SupplementCardDetails_Gender");
						if(gender !=null && gender.equalsIgnoreCase("M")){
							desc="Male";
						} else if( gender !=null && gender.equalsIgnoreCase("F")){
							desc="Female";
						} else{
							desc="NOT AVAILABLE";
						}
						String xml_str = int_xml.get(parent_tag);
						xml_str =xml_str+ "<"+tag_name+">"+desc
								+"</"+ tag_name+">";
						int_xml.put(parent_tag, xml_str);
					}
					else if("PassportExpiryDate".equalsIgnoreCase(tag_name) )
					{
						String PassportExpiry="";
						PassportExpiry =formObject.getNGValue("SupplementCardDetails_PassportExpiry");
						PassportExpiry=new Common_Utils(PersonalLoanS.mLogger).Convert_dateFormat(PassportExpiry, "dd/mm/yyyy", "dd-mm-yyyy");
						String xml_str = int_xml.get(parent_tag);
						xml_str =xml_str+ "<"+tag_name+">"+PassportExpiry
								+"</"+ tag_name+">";
						int_xml.put(parent_tag, xml_str);
					}
					else if("DateOfBirthOrIncorporation".equalsIgnoreCase(tag_name) )
					{
						String DOB="";
						DOB=formObject.getNGValue("SupplementCardDetails_DOB");

						DOB=new Common_Utils(PersonalLoanS.mLogger).Convert_dateFormat(DOB, "dd/mm/yyyy", "dd-mm-yyyy");
						String xml_str = int_xml.get(parent_tag);
						xml_str =xml_str+ "<"+tag_name+">"+DOB
								+"</"+ tag_name+">";
						int_xml.put(parent_tag, xml_str);
					} 
					else{
						int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
					}
				}	
			}


		}catch(Exception e){
			//RLOS.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + ");
			PLCommon.printException(e);
		}

		return int_xml;
	}

	public static String getCustomerType(){
		//RLOS.mLogger.info( "inside getCustAddress_details : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String recordType="I";
		String empttype=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,5);
		if(empttype!=null){	
			if (empttype.contains(NGFUserResourceMgr_PL.getGlobalVar("PL_SelfEmployed"))){
				recordType="C";
			}				
			else {
				recordType="I";
			}	
		}
		return recordType;
	}

	public String getDispatchChannel(String dispatchChannel){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		@SuppressWarnings("unused")
		String channel=dispatchChannel;
		if(formObject.getLVWRowCount("cmplx_CC_Loan_cmplx_btc")>0){
			String query="select Description from NG_MASTER_DispatchChannel where Code='"+dispatchChannel+"' and IsActive='Y'";
			List<List<String>> list = formObject.getDataFromDataSource(query);
			if(list.get(0).get(0)!=null){
				channel= list.get(0).get(0);
			}
		}
		return channel;
	}

	public String getTrasferMode(String transferMode){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		@SuppressWarnings("unused")
		String paymentMode=transferMode;
		String query="select Description from ng_master_TransferMode where Code='"+transferMode+"' and IsActive='Y'";
		List<List<String>> list = formObject.getDataFromDataSource(query);
		if(list.get(0).get(0)!=null){
			paymentMode= list.get(0).get(0);
		}
		return paymentMode;
	}

	public String getTransactiontype(String transtype)
	{
		if(transtype.equalsIgnoreCase("BT")){
			return "BalanceTransfer";
		}
		else if(transtype.equalsIgnoreCase("CCC")){
			return "CreditCardCheque";
		}
		else if(transtype.equalsIgnoreCase("SC")){
			return "SmartCash";
		}
		else if(transtype.equalsIgnoreCase("LOC")){
			return "LoanOnCard";
		}
		else{
			return transtype;
		}
	}


	public static String getHistory()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//Deepak for PCASI-2690
		String Query ="select CardEmbossNum,replace(history,'Key','monthyear') as History from ng_rlos_cust_extexpo_CardDetails where child_wi='"+formObject.getWFWorkitemName()+"' and (History is not null or History!='') union all select AgreementId,replace(history,'Key','monthyear') from ng_rlos_cust_extexpo_LoanDetails where child_wi='"+formObject.getWFWorkitemName()+"' and (History is not null or History!='')";

		String add_xml_str = "";
		try
		{
			List<List<String>> OutputXML = formObject.getDataFromDataSource(Query);
			for (int i = 0; i < OutputXML.size(); i++) {
				String agreementID = "";
				String HistoryTag="";

				if (!(OutputXML.get(i).get(0) == null || OutputXML.get(i).get(0)
						.equals(""))) {
					agreementID = OutputXML.get(i).get(0).toString();
				}
				if (!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1)
						.equals(""))) {
					HistoryTag = OutputXML.get(i).get(1).toString();
				}
				// below Changes Done by shivang for PCASI-2690
				HistoryTag= HistoryTag.replaceAll("<History>","<History><CB_application_id>"+agreementID+"</CB_application_id>");
				if(!"".equals(HistoryTag) && HistoryTag.startsWith("<"))
				{
					//add_xml_str = add_xml_str + "<History_24months><CB_application_id>"+agreementID+"</CB_application_id>"+HistoryTag+"</History_24months>";
					add_xml_str = add_xml_str + "<History_24months>"+HistoryTag+"</History_24months>";
				}
			}
		}
		catch(Exception ex)
		{PersonalLoanS.mLogger.info("Exception in get History");

		}
		return add_xml_str;
	}
	public static String getUtilization()
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String Query ="select CardEmbossNum,Utilizations24Months from ng_rlos_cust_extexpo_CardDetails where child_wi='"+formObject.getWFWorkitemName()+"' and (History is not null or History!='') union all select AgreementId,Utilizations24Months from ng_rlos_cust_extexpo_LoanDetails where child_wi='"+formObject.getWFWorkitemName()+"' and (History is not null or History!='')";
		String add_xml_str = "";
		try
		{
			List<List<String>> OutputXML = formObject.getDataFromDataSource(Query);
			for (int i = 0; i < OutputXML.size(); i++) {
				String agreementID = "";
				String UtilizationTag="";

				if (!(OutputXML.get(i).get(0) == null || OutputXML.get(i).get(0)
						.equals(""))) {
					agreementID = OutputXML.get(i).get(0).toString();
				}
				if (!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1)
						.equals(""))) {
					UtilizationTag = OutputXML.get(i).get(1).toString();
				}
				UtilizationTag= UtilizationTag.replaceAll("Utilizations24Months","Month_Utilization");
				UtilizationTag= UtilizationTag.replaceAll("<Month_Utilization>","<Month_Utilization><CB_application_id>"+agreementID+"</CB_application_id>");
				if(!"".equals(UtilizationTag) && UtilizationTag.startsWith("<"))
				{
					//Changes Done by shivang for PCASI-2690
					//add_xml_str = add_xml_str + "<Utilization24months><CB_application_id>"+agreementID+"</CB_application_id>"+UtilizationTag+"</Utilization24months>";
					add_xml_str = add_xml_str + "<Utilization24months>"+UtilizationTag+"</Utilization24months>";
				}
			}
		}
		catch(Exception ex)
		{	
			PersonalLoanS.mLogger.info("Exception in get History");		
		}
		return add_xml_str;
	}
	public String getNewCardPhoneFaxDtls(){
		String str_PhoneFaxDtls="";
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			int add_row_count = formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");//cmplx_AddressDetails_cmplx_AddressGrid
			String pref_Address_type="";//cmplx_CCCreation_cmplx_CCCreationGrid
			String customer_type =formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12);
			//++below code added by nikhil for Self-Supp CR//cmplx_Customer_PAssportNo
			if("SUPPLEMENT".equalsIgnoreCase(customer_type) && formObject.getNGValue("cmplx_Customer_PAssportNo").equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13)))
			{
				customer_type="PRIMARY";
			}
			//--above code added by nikhil for Self-Supp CR
			for (int i = 0; i<add_row_count;i++){
				if(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 10).equalsIgnoreCase("true"))//10
					pref_Address_type = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0); //0
			}

			if("OFFICE".equalsIgnoreCase(pref_Address_type)){
				str_PhoneFaxDtls="<PhoneFaxDtls><Type>StmtTelephone1</Type><Number>office_number</Number></PhoneFaxDtls><PhoneFaxDtls><Type>AdditionalTelephone1</Type><Number>residence_number</Number></PhoneFaxDtls><PhoneFaxDtls><Type>AdditionalMobileNo</Type><Number>Primary_mobile_number</Number></PhoneFaxDtls>";
			}
			else if("RESIDENCE".equalsIgnoreCase(pref_Address_type)){
				str_PhoneFaxDtls="<PhoneFaxDtls><Type>StmtTelephone1</Type><Number>residence_number</Number></PhoneFaxDtls><PhoneFaxDtls><Type>StmtMobileNo</Type><Number>Primary_mobile_number</Number></PhoneFaxDtls><PhoneFaxDtls><Type>AdditionalTelephone1</Type><Number>office_number</Number></PhoneFaxDtls>";
			}
			if("PRIMARY".equalsIgnoreCase(customer_type)){
				str_PhoneFaxDtls = str_PhoneFaxDtls.replace("Primary_mobile_number", formObject.getNGValue("AlternateContactDetails_MobileNo1"));
			}
			else{
				str_PhoneFaxDtls = str_PhoneFaxDtls.replace("Primary_mobile_number", formObject.getNGValue("SupplementCardDetails_MobNo"));
			}

			str_PhoneFaxDtls = str_PhoneFaxDtls.replace("residence_number", formObject.getNGValue("AlternateContactDetails_RESIDENCENO"));
			str_PhoneFaxDtls = str_PhoneFaxDtls.replace("office_number", formObject.getNGValue("AlternateContactDetails_OFFICENO"));
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("CC Integration exception in getNewCardPhoneFaxDtls: "+ e.getMessage());
			str_PhoneFaxDtls="";
		}
		return str_PhoneFaxDtls;
	}
	public String fetchIsPersonalval() {
		String IsPersonalflag_val = "-1";
		try{
			PersonalLoanS.mLogger.info("Inside fetchIsPersonalval" );
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String listView = "cmplx_CCCreation_cmplx_CCCreationGrid";
			String selectPassport = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13);
			String selectCIF = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),3);
			String App_type=formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12);
			PersonalLoanS.mLogger.info("Inside fetchIsPersonalval selectPassport: "+selectPassport );
			PersonalLoanS.mLogger.info("Inside fetchIsPersonalval selectCIF: "+selectCIF );

			int rowcount = formObject.getLVWRowCount(listView);
			int createdCards = 0;
			for(int i=0;i<rowcount;i++){
				String gridPassprtValue = formObject.getNGValue(listView,i,13);
				String gridCifValue = formObject.getNGValue(listView,i,3);
				PersonalLoanS.mLogger.info("Inside fetchIsPersonalval gridPassprtValue: "+gridPassprtValue );
				PersonalLoanS.mLogger.info("Inside fetchIsPersonalval gridCifValue: "+gridCifValue );

				if(formObject.getNGValue(listView, i, 8).equals("Y") && gridPassprtValue.equalsIgnoreCase(selectPassport) && gridCifValue.equalsIgnoreCase(selectCIF)){
					createdCards++;
					break;
				}
			}
			if(createdCards>0){
				return selectCIF;
			}



			//++below code added by nikhil for Self-Supp CR
			if("SUPPLEMENT".equalsIgnoreCase(App_type)) 
			{
				PersonalLoanS.mLogger.info("Inside fetchIsPersonalval SUPPLEMENT condition : ");
				String LinkedProduct="";
				for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_supplementGrid");i++){
					if(selectPassport.equalsIgnoreCase(formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,3))){
						LinkedProduct = formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,41);
						PersonalLoanS.mLogger.info("row cout: "+i+" LinkedProduct value: "+ LinkedProduct );
						break;
					}
				}
				if(LinkedProduct.contains("CAPS")){
					return selectCIF;
				}
				else{
					return "-1";
				}

			}

			//Deepak Query changed by deepak to include CIF ID. 04 Aug 2019 
			//below changes done by deepak for Prod bug
			String query = "select count(distinct ECRN) from ng_RLOS_CUSTEXPOSE_CustInfo with (nolock) where Child_Wi = '"+formObject.getWFWorkitemName()+"' and  CifId= '"+selectCIF+"' and ecrn !='' and ecrn is not null and ecrn not in (select ECRN from ng_cas_rejected_table where CIF= '"+selectCIF+"' and CRN=ECRN)";
			//String query = "select count(distinct ECRN) from ng_RLOS_CUSTEXPOSE_CustInfo with (nolock) where Child_Wi = '"+formObject.getWFWorkitemName()+"' and  CifId= '"+selectCIF+"' and ecrn !='' and ecrn is not null";
			//CreditCard.mLogger.info("CC Integration query for ECRN "+query);
			List<List<String>> count = formObject.getNGDataFromDataCache(query);

			if(count!=null && count.size()>0 && count.get(0)!=null && Integer.parseInt(count.get(0).get(0))>0){
				return selectCIF;
			}
		}
		catch(Exception ex){
			PersonalLoanS.mLogger.info("CC_INtegration Exception in fetchIsPersonalval function:  "+(ex.getMessage()));
		}
		return IsPersonalflag_val;
	}
	public String getEmploymentOrCompanyDetails(String tagName){
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String EmploymentType=formObject.getNGValue("EmploymentType");
			String xml="";
			PersonalLoanS.mLogger.info("Inside getEmploymentOrCompanyDetails tag name ius:"+tagName);
			if("Self Employed".equalsIgnoreCase(EmploymentType)){
				if(tagName.equals("Department") || tagName.equals("Designation")){
					xml=xml+ "<"+tagName+">"+getDesignationAuthSign()+"</"+ tagName+">";
				}
				else if(tagName.equals("OrgName")){
					String compName=formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",1,4);
					xml=xml+ "<"+tagName+">"+compName+"</"+ tagName+">";
				}
			}
			else{
				if(tagName.equals("Department") || tagName.equals("Designation")){
					xml=xml+ "<"+tagName+">"+formObject.getNGValue("cmplx_EmploymentDetails_Designation")+"</"+ tagName+">";
				}
				else if(tagName.equals("OrgName")){
					String empName=formObject.getNGValue("cmplx_EmploymentDetails_EmpName");
					xml=xml+ "<"+tagName+">"+empName+"</"+ tagName+">";
				}
			}
			PersonalLoanS.mLogger.info("Inside getEmploymentOrCompanyDetails final xml is:"+xml);
			return xml;
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("CC Integration "+ "getEmploymentOrCompanyDetails + "+e.getMessage());
			return "";
		}
	}
	public boolean CombinedLimit_Eligibility(String card_product){
		boolean result_val = false;
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String Query ="select top 1 COMBINEDLIMIT_ELIGIBILITY from ng_master_cardproduct with(nolock) where CODE = '"+card_product+"'";
			List<List<String>> OutputXML = formObject.getNGDataFromDataCache(Query);
			//CreditCard.mLogger.info("Query to check CombinedLimit_Eligibility: "+ Query+ " result: "+OutputXML);
			 if(OutputXML!=null && !OutputXML.isEmpty()){
				 if("0".equalsIgnoreCase(OutputXML.get(0).get(0))){
					 result_val = true;
				 }						 
			 }
		}
		catch(Exception e){
			PersonalLoanS.mLogger.info("Exception in while retriving info regarding CombinedLimit_Eligibility" + e.getMessage());
		}
		return result_val;
	}

}