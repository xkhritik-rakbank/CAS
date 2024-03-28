package com.newgen.omniforms.user;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
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


public class CC_Integration_Input {
	CC_Common CC_Comn = new CC_Common();
	public String GenerateXML(String callName,String Operation_name)
	{

		CreditCard.mLogger.info( "Inside GenerateXML():");
		
		StringBuffer final_xml= new StringBuffer("");
		String header ="";
		String footer = "";
		String parentTagName="";
		Socket socket = null;
		OutputStream out = null;
		InputStream socketInputStream = null;
		DataOutputStream dout = null;
		DataInputStream din = null;
		String mqOutputResponse=null;
		String mqInputRequest=null;
		String cabinetName=null;
		String wi_name=null;
		String ws_name=null;
		String sessionID=null;
		String userName=null;
		String socketServerIP;
		String sQuery=null;

		int socketServerPort;
		String fin_call_name="Customer_details, Customer_eligibility,new_customer_req,new_account_req,DEDUP_SUMMARY";
		CreditCard.mLogger.info("before try");
		try
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String emp_type ="";
			int prod_count=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
			if (prod_count!=0){
				 emp_type = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 6)==null?"":formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 6);
			}
			String sQuery_header = "SELECT Header,Footer,parenttagname FROM NG_Integration with (nolock) where Call_name='"+callName+"'";
			CreditCard.mLogger.info( "sQuery"+sQuery_header);
			List<List<String>> OutputXML_header=formObject.getDataFromDataSource(sQuery_header);
			if(!OutputXML_header.isEmpty()){
				CreditCard.mLogger.info(OutputXML_header.get(0).get(0)+" footer: "+OutputXML_header.get(0).get(1)+" parenttagname: "+OutputXML_header.get(0).get(2));
				header = OutputXML_header.get(0).get(0);
				footer = OutputXML_header.get(0).get(1);
				parentTagName = OutputXML_header.get(0).get(2);
				String col_n = "call_type,Call_name,form_control,parent_tag_name,xmltag_name,is_repetitive,default_val,data_format";
				// String col_n = "call_type,Call_name,form_control,parent_tag_name,xmltag_name,is_repetitive,default_val";
				// String sQuery = "SELECT call_type, Call_name,form_control,parent_tag_name,xmltag_name,is_repetitive FROM NG_Integration_PL_field_Mapping where Call_name='"+callName+"'ORDER BY tag_seq ASC" ;
				if(!("".equalsIgnoreCase(Operation_name) || Operation_name.equalsIgnoreCase(null))){   
					CreditCard.mLogger.info("operation111"+Operation_name);
					CreditCard.mLogger.info("callName111"+callName);
					sQuery = "SELECT "+col_n +" FROM NG_Integration_CC_field_Mapping with (nolock) where Call_name='"+callName+"' and active = 'Y' and Operation_name='"+Operation_name+"' ORDER BY tag_seq ASC" ;
					CreditCard.mLogger.info( "sQuery "+sQuery);
				}
				else{
					CreditCard.mLogger.info("operation"+Operation_name);
					sQuery = "SELECT "+col_n +" FROM NG_Integration_CC_field_Mapping with (nolock) where Call_name='"+callName+"' and active = 'Y' ORDER BY tag_seq ASC" ;
					CreditCard.mLogger.info( "sQuery "+sQuery);
				}

				List<List<String>> OutputXML=formObject.getDataFromDataSource(sQuery);
				CreditCard.mLogger.info("OutputXML"+OutputXML);
				if(!OutputXML.isEmpty()){
					//CreditCard.mLogger.info(OutputXML.get(0).get(0)+OutputXML.get(0).get(1)+OutputXML.get(0).get(2)+OutputXML.get(0).get(3));
					CreditCard.mLogger.info(OutputXML.get(0).get(0)+OutputXML.get(0).get(1)+OutputXML.get(0).get(2)+OutputXML.get(0).get(3)+OutputXML.get(0).get(4));
					CreditCard.mLogger.info(OutputXML.get(0).get(0)+OutputXML.get(0).get(1)+OutputXML.get(0).get(2)+OutputXML.get(0).get(3));
					int n=OutputXML.size();
					CreditCard.mLogger.info("row count:"+n);


					if( n> 0)
					{

						CreditCard.mLogger.info("column length"+col_n.length());
						Map<String, String> int_xml = new LinkedHashMap<String, String>();
						Map<String, String> recordFileMap = new HashMap<String, String>();

						for(List<String> mylist:OutputXML)
						{
							// for(int i=0;i<col_n.length();i++)
							for(int i=0;i<8;i++)
							{
								//CreditCard.mLogger.info("rec: "+records.item(rec));
								CreditCard.mLogger.info("column length values"+col_n);
								String[] col_name = col_n.split(",");
								recordFileMap.put(col_name[i],mylist.get(i));
							}
							recordFileMap.get("call_type");
							String Call_name = (String) recordFileMap.get("Call_name");
							String form_control = (String) recordFileMap.get("form_control");
							String parent_tag = (String) recordFileMap.get("parent_tag_name");
							String tag_name = (String) recordFileMap.get("xmltag_name");
							String is_repetitive = (String) recordFileMap.get("is_repetitive");
							String default_val = (String) recordFileMap.get("default_val");
							String data_format12 = (String) recordFileMap.get("data_format");
							CreditCard.mLogger.info("tag_name : "+tag_name +" valuie of default_val: "+default_val+" Call_name: "+Call_name+" parent_tag"+ parent_tag);
							String form_control_val="";
							java.util.Date startDate;

							if("AddressDetails".equalsIgnoreCase(tag_name) &&( "DEDUP_SUMMARY".equalsIgnoreCase(Call_name)||"BLACKLIST_DETAILS".equalsIgnoreCase(Call_name) ||"NEW_CUSTOMER_REQ".equalsIgnoreCase(Call_name))){
								if(int_xml.containsKey(parent_tag))
								{
									String xml_str = int_xml.get(parent_tag);
									CreditCard.mLogger.info(" before adding address+ "+xml_str);
									xml_str = new StringBuffer().append(xml_str).append(getCustAddress_details()).toString(); //xml_str + getCustAddress_details();
									CreditCard.mLogger.info(" after adding address+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}		                            	
							}

							//added 30/08/2017
							else if("OECDDet".equalsIgnoreCase(tag_name) && "CUSTOMER_UPDATE_REQ".equalsIgnoreCase(Call_name)){
								CreditCard.mLogger.info("inside customer update req1234");
								if(int_xml.containsKey(parent_tag))
								{
									CreditCard.mLogger.info("inside customer update req2123");
									String xml_str = int_xml.get(parent_tag);
									CreditCard.mLogger.info(" before adding OECD+ "+xml_str);
									xml_str = xml_str + CC_Comn.getCustOECD_details(Call_name);
									CreditCard.mLogger.info(" after adding OeCD+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}		                            	
							}
							//ended 30/08/2017
							else if("PhnLocalCode".equalsIgnoreCase(tag_name) && "CUSTOMER_UPDATE_REQ".equalsIgnoreCase(Call_name)){
								CreditCard.mLogger.info("PhnLocalCode to substring");
								String xml_str = int_xml.get(parent_tag);
								String phn_no = formObject.getNGValue(form_control);
								if((!"".equalsIgnoreCase(phn_no)) && phn_no.indexOf("00971")>-1){
									phn_no = phn_no.substring(5);
								}

								xml_str = xml_str+"<"+tag_name+">"+phn_no+"</"+ tag_name+">";

								CreditCard.mLogger.info(" after adding ApplicationID:  "+xml_str);
								int_xml.put(parent_tag, xml_str);	                            	
							}
							//code change for Customer update to add address tag.
							else if("AddrDet".equalsIgnoreCase(tag_name) && "CUSTOMER_UPDATE_REQ".equalsIgnoreCase(Call_name)){
								CreditCard.mLogger.info("inside customer update req1");
								if(int_xml.containsKey(parent_tag))
								{
									CreditCard.mLogger.info("inside customer update req2");
									String xml_str = int_xml.get(parent_tag);
									CreditCard.mLogger.info(" before adding address+ "+xml_str);
									xml_str = xml_str + CC_Comn.getCustAddress_details(Call_name);
									CreditCard.mLogger.info(" after adding address+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}	                            	
							}
							//code change for Customer update to add address tag.
							else if("MaritalStatus".equalsIgnoreCase(tag_name) && ("DEDUP_SUMMARY".equalsIgnoreCase(Call_name)||"BLACKLIST_DETAILS".equalsIgnoreCase(Call_name))){
								String marrital_code = formObject.getNGValue("cmplx_Customer_MAritalStatus").substring(0, 1);
								String xml_str = int_xml.get(parent_tag);
								xml_str = xml_str + "<"+tag_name+">"+marrital_code
								+"</"+ tag_name+">";

								CreditCard.mLogger.info(" after adding Minor flag+ "+xml_str);
								int_xml.put(parent_tag, xml_str);
							}
							else if("VIPFlg".equalsIgnoreCase(tag_name) && "NEW_CARD_REQ".equalsIgnoreCase(Call_name)){
								String vip_flag="N";
								if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_VIPFlag"))){
									vip_flag="Y";
								}
								String xml_str = int_xml.get(parent_tag);
								xml_str = xml_str + "<"+tag_name+">"+vip_flag
								+"</"+ tag_name+">";

								CreditCard.mLogger.info(" after adding VIP flag+ "+xml_str);
								int_xml.put(parent_tag, xml_str);
							}
							else if("ProcessingUserId".equalsIgnoreCase(tag_name) && "NEW_CARD_REQ".equalsIgnoreCase(Call_name)){
								String xml_str = int_xml.get(parent_tag);
								xml_str = xml_str + "<"+tag_name+">"+formObject.getUserName()
								+"</"+ tag_name+">";

								CreditCard.mLogger.info(" after adding Minor flag+ "+xml_str);
								int_xml.put(parent_tag, xml_str);
							}
							else if("ProcessingDate".equalsIgnoreCase(tag_name) && "NEW_CARD_REQ".equalsIgnoreCase(Call_name)){
								String xml_str = int_xml.get(parent_tag);
								SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.mmm+hh:mm");
								xml_str = xml_str + "<"+tag_name+">"+sdf1.format(new Date())
								+"</"+ tag_name+">";

								CreditCard.mLogger.info(" after adding Minor flag+ "+xml_str);
								int_xml.put(parent_tag, xml_str);
							}
							//FOR dectech

							else if("Channel".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name)){
								CreditCard.mLogger.info(" iNSIDE channelcode+ ");

								String ReqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);

								String xml_str="<"+tag_name+">"+("Personal Loan".equalsIgnoreCase(ReqProd)?"PL":"CC")+"</"+ tag_name+">"; //String xml_str = int_xml.get(parent_tag);
								/*xml_str =  "<"+tag_name+">"+(ReqProd.equalsIgnoreCase("Personal Loan")?"PL":"CC")
								+"</"+ tag_name+">";*/

								CreditCard.mLogger.info(" after adding channelcode+ "+xml_str);
								int_xml.put(parent_tag, xml_str);

							}

							else if("emp_type".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name)){
								CreditCard.mLogger.info(" iNSIDE channelcode+ ");

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

								CreditCard.mLogger.info(" after adding channelcode+ "+xml_str);
								int_xml.put(parent_tag, xml_str);

							}
							else if("world_check".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name)){
								CreditCard.mLogger.info(" iNSIDE world_check+ ");

								String world_check; //String world_check=formObject.getNGValue("IS_WORLD_CHECK");
								CreditCard.mLogger.info(" iNSIDE world_check+ "+formObject.getLVWRowCount("cmplx_WorldCheck_WorldCheck_Grid"));
								if (formObject.getLVWRowCount("cmplx_WorldCheck_WorldCheck_Grid")==0){
									world_check="Negative";
								}
								else {
									world_check="Positive";
								}


								String xml_str = int_xml.get(parent_tag);
								xml_str = xml_str+ "<"+tag_name+">"+world_check+"</"+ tag_name+">";

								CreditCard.mLogger.info(" after adding world_check+ "+xml_str);
								int_xml.put(parent_tag, xml_str);

							}

							else if("prev_loan_dbr".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name)){
								CreditCard.mLogger.info(" iNSIDE prev_loan_dbr+ ");
								String PreviousLoanDBR="";
								String PreviousLoanEmp="";
								String PreviousLoanMultiple="";
								String PreviousLoanTAI="";

								String squeryloan="select PreviousLoanDBR,PreviousLoanEmp,PreviousLoanMultiple,PreviousLoanTAI from ng_RLOS_CUSTEXPOSE_LoanDetails where Request_Type='CollectionsSummary' and Limit_Increase='true' and Child_Wi= '"+formObject.getWFWorkitemName()+"'";
								List<List<String>> prevLoan=formObject.getDataFromDataSource(squeryloan);
								CreditCard.mLogger.info(" iNSIDE prev_loan_dbr+ "+squeryloan);

								if (!prevLoan.isEmpty()) {
									PreviousLoanDBR=prevLoan.get(0).get(0);
									PreviousLoanEmp=prevLoan.get(0).get(1);
									PreviousLoanMultiple=prevLoan.get(0).get(2);
									PreviousLoanTAI=prevLoan.get(0).get(3);
								}


								String xml_str = int_xml.get(parent_tag);
								xml_str = xml_str+ "<"+tag_name+">"+PreviousLoanDBR+"</"+ tag_name+"><prev_loan_tai>"+PreviousLoanTAI+"</prev_loan_tai><prev_loan_multiple>"+PreviousLoanMultiple+"</prev_loan_multiple><prev_loan_amount></prev_loan_amount><prev_loan_employer>"+PreviousLoanEmp+"</prev_loan_employer>";

								CreditCard.mLogger.info(" after adding world_check+ "+xml_str);
								int_xml.put(parent_tag, xml_str);

							}

							else if("no_of_cheque_bounce_int_3mon_Ind".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name)){
								CreditCard.mLogger.info(" iNSIDE no_of_cheque_bounce_int_3mon_Ind+ ");
								String squerynoc="SELECT count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-3,GETDATE()) and returntype='ICCS' and Child_Wi='"+formObject.getWFWorkitemName()+"'";
								List<List<String>> NOC=formObject.getDataFromDataSource(squerynoc);
								if (!NOC.isEmpty()){
									String xml_str =  int_xml.get(parent_tag);
									xml_str = xml_str+ "<"+tag_name+">"+NOC.get(0).get(0)
									+"</"+ tag_name+">";

									CreditCard.mLogger.info(" after adding internal_blacklist+ "+xml_str);
									int_xml.put(parent_tag, xml_str);

								}

							}
							else if("no_of_DDS_return_int_3mon_Ind".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name)){
								CreditCard.mLogger.info(" iNSIDE no_of_cheque_bounce_int_3mon_Ind+ ");
								String squerynoc="SELECT count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-3,GETDATE()) and returntype='DDS' and Child_Wi='"+formObject.getWFWorkitemName()+"'";
								List<List<String>> NOC=formObject.getDataFromDataSource(squerynoc);
								if (!NOC.isEmpty()){
									String xml_str =  int_xml.get(parent_tag);
									xml_str = xml_str+ "<"+tag_name+">"+NOC.get(0).get(0)
									+"</"+ tag_name+">";

									CreditCard.mLogger.info(" after adding internal_blacklist+ "+xml_str);
									int_xml.put(parent_tag, xml_str);

								}

							}
							else if("blacklist_cust_type".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name)){
								CreditCard.mLogger.info(" iNSIDE channelcode+ ");
								String ParentWI_Name = formObject.getNGValue("Parent_WIName");
								String squeryBlacklist="select BlacklistFlag,BlacklistDate,BlacklistReasonCode,NegatedFlag,NegatedDate,NegatedReasonCode from ng_rlos_cif_detail where cif_wi_name='"+ParentWI_Name+"' and cif_searchType = 'Internal'";
								List<List<String>> Blacklist=formObject.getDataFromDataSource(squeryBlacklist);
								String internal_blacklist =  "";
								String internal_blacklist_date =  "";
								String internal_blacklist_code =  "";
								String internal_negative_flag =  "";
								String internal_negative_date =  "";
								String internal_negative_code =  "";

								if (!Blacklist.isEmpty()){		
									internal_blacklist =  Blacklist.get(0).get(0);
									internal_blacklist_date =  Blacklist.get(0).get(1);
									internal_blacklist_code =  Blacklist.get(0).get(2);
									internal_negative_flag =  Blacklist.get(0).get(3);
									internal_negative_date =  Blacklist.get(0).get(4);
									internal_negative_code =  Blacklist.get(0).get(5);
								}
								String xml_str =  int_xml.get(parent_tag);
								xml_str = xml_str+ "<"+tag_name+">I</"+ tag_name+"><internal_blacklist>"+internal_blacklist+"</internal_blacklist><internal_blacklist_date>"+internal_blacklist_date+"</internal_blacklist_date><internal_blacklist_code>"+internal_blacklist_code+"</internal_blacklist_code><negative_cust_type>I</negative_cust_type><internal_negative_flag>"+internal_negative_flag+"</internal_negative_flag><internal_negative_date>"+internal_negative_date+"</internal_negative_date><internal_negative_code>"+internal_negative_code+"</internal_negative_code>";

								CreditCard.mLogger.info(" after adding internal_blacklist+ "+xml_str);
								int_xml.put(parent_tag, xml_str);


							}

							/*
							 * else if(tag_name.equalsIgnoreCase("LienDetails") && (Call_name.equalsIgnoreCase("CARD_NOTIFICATION")||Call_name.equalsIgnoreCase("CARD_SERVICES_REQUEST"))){
								PL_SKLogger.writeLog("inside 1st if","inside customer update req1");
								if(int_xml.containsKey(parent_tag))
								{
									PL_SKLogger.writeLog("inside 1st if","inside customer update req2");
									String xml_str = int_xml.get(parent_tag);
									PL_SKLogger.writeLog("RLOS COMMON"," before adding address+ "+xml_str);
									xml_str = xml_str + getLien_details(Call_name);
									PL_SKLogger.writeLog("RLOS COMMON"," after adding address+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}		                            	
							}
							 * */
							else if((("lob".equalsIgnoreCase(tag_name))||("target_segment_code".equalsIgnoreCase(tag_name))||("designation".equalsIgnoreCase(tag_name))||("emp_name".equalsIgnoreCase(tag_name))||("industry_sector".equalsIgnoreCase(tag_name))||("industry_marco".equalsIgnoreCase(tag_name))||("industry_micro".equalsIgnoreCase(tag_name))||("bvr".equalsIgnoreCase(tag_name))||("eff_date_estba".equalsIgnoreCase(tag_name))||("poa".equalsIgnoreCase(tag_name))||("tlc_issue_date".equalsIgnoreCase(tag_name))||("cc_employer_status".equalsIgnoreCase(tag_name))||("pl_employer_status".equalsIgnoreCase(tag_name))||("marketing_code".equalsIgnoreCase(tag_name))) && "DECTECH".equalsIgnoreCase(Call_name) && "Self Employed".equalsIgnoreCase(emp_type)){
								CreditCard.mLogger.info( "inside getProduct_details : ");
								String xml_str =  int_xml.get(parent_tag);
								int Comp_row_count = formObject.getLVWRowCount("cmplx_CompanyDetails_cmplx_CompanyGrid");
								String lob="";
								String target_segment_code="";
								String designation="";
								String emp_name="";
								String industry_sector="";
								String eff_date_estba="";
								String industry_marco="";
								String industry_micro="";
								String bvr="";
								String poa="";
								String marketing_code="";
								
								for (int i = 0; i<Comp_row_count;i++){
									lob = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 15); //0
									target_segment_code = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 25); //0
									designation = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 11); //0
									emp_name = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 0); //0
								//	industry_sector = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 8); //0
									eff_date_estba = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 14); //0
									industry_sector = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 8); //0
									industry_marco = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 9); //0
									industry_micro = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 10); //0
									bvr = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 19); //0
									poa = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 20); //0
									marketing_code = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 27); //0
									
								
								}
								if("lob".equalsIgnoreCase(tag_name)){
									xml_str = xml_str+ "<"+tag_name+">"+lob+"</"+ tag_name+">";
								}
								else if("target_segment_code".equalsIgnoreCase(tag_name)){
									xml_str = xml_str+ "<"+tag_name+">"+target_segment_code+"</"+ tag_name+">";
								}
								else if("designation".equalsIgnoreCase(tag_name)){
									xml_str = xml_str+ "<"+tag_name+">"+designation+"</"+ tag_name+">";
								}
								else if("emp_name".equalsIgnoreCase(tag_name)){
									xml_str = xml_str+ "<"+tag_name+">"+emp_name+"</"+ tag_name+">";
								}
								else if("industry_sector".equalsIgnoreCase(tag_name)){
									xml_str = xml_str+ "<"+tag_name+">"+industry_sector+"</"+ tag_name+">";
								}
								else if("industry_marco".equalsIgnoreCase(tag_name)){
									xml_str = xml_str+ "<"+tag_name+">"+industry_marco+"</"+ tag_name+">";
								}
								else if("industry_micro".equalsIgnoreCase(tag_name)){
									xml_str = xml_str+ "<"+tag_name+">"+industry_micro+"</"+ tag_name+">";
								}
								else if("bvr".equalsIgnoreCase(tag_name) || "cc_employer_status".equalsIgnoreCase(tag_name)){
									xml_str = xml_str+ "<"+tag_name+">"+bvr+"</"+ tag_name+">";
								}
								else if("eff_date_estba".equalsIgnoreCase(tag_name) || "tlc_issue_date".equalsIgnoreCase(tag_name)){
									xml_str = xml_str+ "<"+tag_name+">"+eff_date_estba+"</"+ tag_name+">";
								}
								else if("poa".equalsIgnoreCase(tag_name) || "pl_employer_status".equalsIgnoreCase(tag_name)){
									xml_str = xml_str+ "<"+tag_name+">"+poa+"</"+ tag_name+">";
								}
								/*else if(tag_name.equalsIgnoreCase("cc_employer_status")){
									xml_str = xml_str+ "<"+tag_name+">"+bvr+"</"+ tag_name+">";
								}
								else if(tag_name.equalsIgnoreCase("tlc_issue_date")){
									xml_str = xml_str+ "<"+tag_name+">"+eff_date_estba+"</"+ tag_name+">";
								}
								else if(tag_name.equalsIgnoreCase("pl_employer_status")){
									xml_str = xml_str+ "<"+tag_name+">"+poa+"</"+ tag_name+">";
								}*/
								else if("marketing_code".equalsIgnoreCase(tag_name)){
									xml_str = xml_str+ "<"+tag_name+">"+marketing_code+"</"+ tag_name+">";
								}
								CreditCard.mLogger.info(" after adding cmplx_CompanyDetails_cmplx_CompanyGrid+ "+xml_str);
								int_xml.put(parent_tag, xml_str);
							}
							else if(("auth_sig_sole_emp".equalsIgnoreCase(tag_name)||"shareholding_perc".equalsIgnoreCase(tag_name)) && "DECTECH".equalsIgnoreCase(Call_name) && "Self Employed".equalsIgnoreCase(emp_type)){
								CreditCard.mLogger.info(" iNSIDE channelcode+ ");
								String auth_sig_sole_emp =  "";
								String shareholding_perc =  "";
								int Authsign_row_count = formObject.getLVWRowCount("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails");
								if (Authsign_row_count !=0){
									auth_sig_sole_emp =("Yes".equalsIgnoreCase(formObject.getNGValue("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails", 0, 10))?"Y":"N"); //0
									shareholding_perc = formObject.getNGValue("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails", 0, 9); //0
									
									
								}
								String xml_str =  int_xml.get(parent_tag);
								if("auth_sig_sole_emp".equalsIgnoreCase(tag_name)){
									xml_str = xml_str+ "<"+tag_name+">"+auth_sig_sole_emp+"</"+ tag_name+">";
								}
								else if("shareholding_perc".equalsIgnoreCase(tag_name)){
									xml_str = xml_str+ "<"+tag_name+">"+shareholding_perc+"</"+ tag_name+">";
								}
								

								CreditCard.mLogger.info(" after adding shareholding_perc+ "+xml_str);
								int_xml.put(parent_tag, xml_str);


							}
							else if(("external_blacklist_flag".equalsIgnoreCase(tag_name)||"external_blacklist_date".equalsIgnoreCase(tag_name)||"external_blacklist_code".equalsIgnoreCase(tag_name)) && "DECTECH".equalsIgnoreCase(Call_name)){
								CreditCard.mLogger.info(" iNSIDE channelcode+ ");
							String ParentWI_Name = formObject.getNGValue("Parent_WIName");
							String squeryBlacklist="select BlacklistFlag,BlacklistDate,BlacklistReasonCode from ng_rlos_cif_detail where (cif_wi_name='"+ParentWI_Name+"' or cif_wi_name='"+formObject.getWFWorkitemName()+"') and cif_searchType = 'External'";
							CreditCard.mLogger.info(" iNSIDE channelcode+ "+squeryBlacklist);
							List<List<String>> Blacklist=formObject.getDataFromDataSource(squeryBlacklist);
							String External_blacklist_date =  "";
							String External_blacklist_code =  "";
								
							if (!Blacklist.isEmpty()){		
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
							
							CreditCard.mLogger.info(" after adding internal_blacklist+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						
									                            	
						}
							else if("ApplicationDetails".equalsIgnoreCase(tag_name) && ("DECTECH".equalsIgnoreCase(Call_name))){
								CreditCard.mLogger.info("inside DECTECH req1");

								CreditCard.mLogger.info("inside customer update req2");
								String xml_str = int_xml.get(parent_tag);
								CreditCard.mLogger.info(" before adding product+ "+xml_str);
								xml_str = xml_str + CC_Comn.getProduct_details();
								CreditCard.mLogger.info(" after adding product+ "+xml_str);
								int_xml.put(parent_tag, xml_str);

							}
							//code commented for age calculation as the generic method is used - 28-sept-2017 start
							/*
						      else if(tag_name.equalsIgnoreCase("age") && Call_name.equalsIgnoreCase("DECTECH")){
						    	  CreditCard.mLogger.info(" Inside age + ");
									if(int_xml.containsKey(parent_tag))
									{
										//by aman for getting difference between 2 dates in YY.MM format.
										String age = getYearsDifference(formObject,"cmplx_Customer_DOb");		
										String xml_str = int_xml.get(parent_tag);
										xml_str = xml_str + "<"+tag_name+">"+age
										+"</"+ tag_name+">";

										CreditCard.mLogger.info(" after adding age+ "+xml_str);
										int_xml.put(parent_tag, xml_str);
									}		                            	
								}

							else if(tag_name.equalsIgnoreCase("LOS") && Call_name.equalsIgnoreCase("DECTECH")){
								if(int_xml.containsKey(parent_tag))
								{
									String LOS=formObject.getNGValue("cmplx_EmploymentDetails_LOS");
									//CreditCard.mLogger.info(" LOS length+ "+LOS.length());
									CreditCard.mLogger.info(" value of LOS "+LOS);
								try{	
								if (LOS!=null){
										CreditCard.mLogger.info(" after adding los+ "+LOS);
									if (LOS.length()!=0){
										if (LOS.length()==1){
											LOS="0"+LOS+".00";
										}
										else{
											LOS=LOS+".00";
										}
									}

									String xml_str = int_xml.get(parent_tag);
									xml_str = xml_str + "<"+tag_name+">"+LOS
									+"</"+ tag_name+">";

									CreditCard.mLogger.info(" after adding los+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}
								}	
								catch (Exception e){
									CreditCard.mLogger.info(" after adding los+ "+printException(e));


								}
							}
							}*/
							//code commented for age calculation as the generic method is used - 28-sept-2017 End
							/*else if(tag_name.equalsIgnoreCase("resident_flag") && Call_name.equalsIgnoreCase("DECTECH")){
								if(int_xml.containsKey(parent_tag))
								{
									String resident_flag=formObject.getNGValue("cmplx_Customer_RESIDENTNONRESIDENT");

									String xml_str = int_xml.get(parent_tag);
									if (resident_flag!=null){
										if (resident_flag.equalsIgnoreCase("Resident")){
											resident_flag="Y";
										}
										else{
											resident_flag="N";
										}
									}
									xml_str = xml_str + "<"+tag_name+">"+resident_flag
									+"</"+ tag_name+">";

									CreditCard.mLogger.info(" after adding cmplx_Customer_RESIDENTNONRESIDENT+ "+xml_str);
									int_xml.put(parent_tag, xml_str);

								}		                            	
							}*/
							else if("cust_name".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name)){
								if(int_xml.containsKey(parent_tag))
								{
									String first_name=formObject.getNGValue("cmplx_Customer_FIrstNAme");
									String middle_name=formObject.getNGValue("cmplx_Customer_MiddleName");
									String last_name=formObject.getNGValue("cmplx_Customer_LAstNAme");

									String full_name=first_name+" "+middle_name+""+last_name;

									String xml_str = int_xml.get(parent_tag);
									xml_str = xml_str + "<"+tag_name+">"+full_name
									+"</"+ tag_name+">";

									CreditCard.mLogger.info(" after adding confirmedinjob+ "+xml_str);
									int_xml.put(parent_tag, xml_str);

								}		                            	
							}

							else if("ref_phone_no".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name)){
								if(int_xml.containsKey(parent_tag))
								{
									CreditCard.mLogger.info(" INSIDE ref_phone_no+ ");
									int count=formObject.getLVWRowCount("cmplx_RefDetails_cmplx_Gr_ReferenceDetails");
									String ref_phone_no="";
									String ref_relationship="";
									CreditCard.mLogger.info(" INSIDE ref_phone_no+ "+count);
									if (count != 0){
										ref_phone_no=formObject.getNGValue("cmplx_RefDetails_cmplx_Gr_ReferenceDetails",0,4);
										ref_relationship=formObject.getNGValue("cmplx_RefDetails_cmplx_Gr_ReferenceDetails",0,2);
										CreditCard.mLogger.info(" after adding ref_phone_no+ "+ref_phone_no);
										CreditCard.mLogger.info(" after adding ref_relationship+ "+ref_relationship);
									}


									String xml_str = int_xml.get(parent_tag);
									xml_str = xml_str + "<"+tag_name+">"+ref_phone_no
									+"</"+ tag_name+"><ref_relationship>"+ref_relationship+"</ref_relationship>";

									CreditCard.mLogger.info(" after adding confirmedinjob+ "+xml_str);
									int_xml.put(parent_tag, xml_str);

								}		                            	
							}

							else if("confirmed_in_job".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name)){
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

										CreditCard.mLogger.info(" after adding confirmedinjob+ "+xml_str);
										int_xml.put(parent_tag, xml_str);
									}
								}		                            	
							}
							else if("included_pl_aloc".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name)){
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

										CreditCard.mLogger.info(" after adding included_pl_aloc+ "+xml_str);
										int_xml.put(parent_tag, xml_str);
									}
								}
							}
							else if("included_cc_aloc".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name)){
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

										CreditCard.mLogger.info(" after adding cmplx_EmploymentDetails_IncInCC+ "+xml_str);
										int_xml.put(parent_tag, xml_str);
									}	
								}
							}
							else if("vip_flag".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name)){
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

									CreditCard.mLogger.info(" after adding cmplx_Customer_VIPFlag+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}		                            	
							}
							else if("standing_instruction".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name)){
								CreditCard.mLogger.info(" iNSIDE standing_instruction+ ");
								String squerynoc="SELECT count(*) FROM ng_rlos_FinancialSummary_SiDtls WHERE Child_wi='"+formObject.getWFWorkitemName()+"'";
								List<List<String>> NOC=formObject.getDataFromDataSource(squerynoc);
								CreditCard.mLogger.info(" after adding cmplx_Customer_VIPFlag+ "+squerynoc);
								String standing_instruction=NOC.get(0).get(0);  //String standing_instruction="";
								//standing_instruction=NOC.get(0).get(0);
								if (!NOC.isEmpty()){
									String xml_str =  int_xml.get(parent_tag);
									if ("0".equalsIgnoreCase(standing_instruction)){
										standing_instruction="N";
									}
									else{
										standing_instruction="Y";
									}

									xml_str = xml_str+ "<"+tag_name+">"+standing_instruction
									+"</"+ tag_name+">";

									CreditCard.mLogger.info(" after adding standing_instruction+ "+xml_str);
									int_xml.put(parent_tag, xml_str);

								}

							}
							/*	else if(tag_name.equalsIgnoreCase("avg_credit_turnover_6") && Call_name.equalsIgnoreCase("DECTECH")){
								if(int_xml.containsKey(parent_tag))
								{	
									int avg_credit_turnover6th=0;
									String avg_credit_turnover_freq=formObject.getNGValue("cmplx_IncomeDetails_AvgCredTurnoverFreq");
									String avg_credit_turnover6=formObject.getNGValue("cmplx_IncomeDetails_AvgCredTurnover");
									if ((!avg_credit_turnover6.equalsIgnoreCase(null))&&(!avg_credit_turnover6.equalsIgnoreCase(""))){
									 avg_credit_turnover6th=Integer.parseInt(avg_credit_turnover6);
									}
									//String avg_credit_turnover3="";
									String avg_bal_freq=formObject.getNGValue("cmplx_IncomeDetails_AvgBalFreq");
									String avg_bal6=formObject.getNGValue("cmplx_IncomeDetails_AvgBal");
									int avg_bal6th=Integer.parseInt(avg_bal6);
									if (avg_credit_turnover_freq.equalsIgnoreCase("Annually")){
										avg_credit_turnover6th=avg_credit_turnover6th/2;
									}
									else if (avg_credit_turnover_freq.equalsIgnoreCase("Monthly")){
										avg_credit_turnover6th=6*avg_credit_turnover6th;
									}
									else if (avg_credit_turnover_freq.equalsIgnoreCase("Quarterly")){
										avg_credit_turnover6th=2*avg_credit_turnover6th;
									}
									if (avg_bal_freq.equalsIgnoreCase("Annually")){
										avg_bal6th=avg_bal6th/2;
									}
									else if (avg_bal_freq.equalsIgnoreCase("Monthly")){
										avg_bal6th=6*avg_bal6th;
									}
									else if (avg_bal_freq.equalsIgnoreCase("Quarterly")){
										avg_bal6th=2*avg_bal6th;
									}	

									String xml_str = int_xml.get(parent_tag);
									xml_str = xml_str + "<"+tag_name+">"+avg_credit_turnover6th
									+"</"+ tag_name+"><avg_credit_turnover_3>"+avg_credit_turnover6th/2+"</avg_credit_turnover_3><avg_bal_3>"+avg_bal6th+"</avg_bal_3><avg_bal_6>"+avg_bal6th/2+"</avg_bal_6>";

									CreditCard.mLogger.info(" after adding cmplx_Customer_VIPFlag+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}		                            	
							}
							 */		/*	else if(tag_name.equalsIgnoreCase("aggregate_exposed") && Call_name.equalsIgnoreCase("DECTECH")){
								if(int_xml.containsKey(parent_tag))
								{
									String aeQuery = "SELECT A.CreditLimit, B.TotalOutstandingAmt,C.TotalAmount,D.TotalAmt FROM ng_RLOS_CUSTEXPOSE_CardDetails a FULL OUTER JOIN ng_RLOS_CUSTEXPOSE_LoanDetails B ON A.Wi_Name=B.Wi_Name FULL OUTER JOIN ng_rlos_cust_extexpo_CardDetails C ON C.Wi_Name=A.Wi_Name FULL OUTER JOIN ng_rlos_cust_extexpo_LoanDetails D ON D.Wi_Name=A.Wi_Name WHERE A.Wi_Name = '"+formObject.getWFWorkitemName()+"'";
									CreditCard.mLogger.info("aggregate_exposed sQuery"+aeQuery+ "");
									List<List<String>> aggregate_exposed = formObject.getDataFromDataSource(aeQuery);
									CreditCard.mLogger.info("aggregate_exposed list size"+aggregate_exposed.size()+ "");
									float CreditLimit;
									float TotalOutstandingAmt;
									float TotalAmount;
									float TotalAmt;
									CreditCard.mLogger.info("outsidefor list "+ "values");
									CreditLimit=0.0f;
									TotalOutstandingAmt=0.0f;
									TotalAmount=0.0f;
									TotalAmt=0.0f;
									float Total;
									CreditCard.mLogger.info("outsidefor list "+ "values");
									Total=0.0f;

									for (int i = 0; i<aggregate_exposed.size();i++){


										if(aggregate_exposed.get(i).get(0)!=null && !aggregate_exposed.get(i).get(0).isEmpty() &&  !aggregate_exposed.get(i).get(0).equals("") && !aggregate_exposed.get(i).get(0).equalsIgnoreCase("null") ){
												CreditLimit = CreditLimit+  Float.parseFloat(aggregate_exposed.get(i).get(0));
										}
										if(aggregate_exposed.get(i).get(1)!=null && !aggregate_exposed.get(i).get(1).isEmpty() &&  !aggregate_exposed.get(i).get(1).equals("") && !aggregate_exposed.get(i).get(1).equalsIgnoreCase("null") ){
												TotalOutstandingAmt =TotalOutstandingAmt + Float.parseFloat(aggregate_exposed.get(i).get(1));
										}
										if(aggregate_exposed.get(i).get(2)!=null && !aggregate_exposed.get(i).get(2).isEmpty() &&  !aggregate_exposed.get(i).get(2).equals("") && !aggregate_exposed.get(i).get(2).equalsIgnoreCase("null") ){
												TotalAmount =TotalAmount+  Float.parseFloat(aggregate_exposed.get(i).get(2));
										}
										if(aggregate_exposed.get(i).get(3)!=null && !aggregate_exposed.get(i).get(3).isEmpty() &&  !aggregate_exposed.get(i).get(3).equals("") && !aggregate_exposed.get(i).get(3).equalsIgnoreCase("null") ){
												TotalAmt = TotalAmt+ Float.parseFloat(aggregate_exposed.get(i).get(3));
										}

									}
									Total=	CreditLimit+TotalOutstandingAmt+TotalAmount+TotalAmt;

									String xml_str = int_xml.get(parent_tag);
									xml_str = xml_str + "<"+tag_name+">"+Total
									+"</"+ tag_name+">";

									CreditCard.mLogger.info(" after adding aggregate_exposed+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}		                            	
							}
							  */			
							else if("accomodation_provided".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name)){
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

										CreditCard.mLogger.info(" after adding confirmedinjob+ "+xml_str);
										int_xml.put(parent_tag, xml_str);
									}	
								}
							}
							else if("AccountDetails".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name)){
								if(int_xml.containsKey(parent_tag))
								{
									String xml_str = int_xml.get(parent_tag);
									CreditCard.mLogger.info(" before adding internal liability+ "+xml_str);
									xml_str = xml_str + CC_Comn.getInternalLiabDetails();
									CreditCard.mLogger.info(" after internal liability+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}

							}
							else if("InternalBureau".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name)){

								String xml_str = int_xml.get(parent_tag);
								CreditCard.mLogger.info(" before adding InternalBureauData+ "+xml_str);
								String temp = CC_Comn.InternalBureauData();
								if(!"".equalsIgnoreCase(temp)){
									if (xml_str==null){
										CreditCard.mLogger.info(" before adding bhrabc"+xml_str);
										xml_str="";
									}
									xml_str =  xml_str+ temp;
									CreditCard.mLogger.info(" after InternalBureauData+ "+xml_str);
									int_xml.get(parent_tag);
									int_xml.put(parent_tag, xml_str);
								}


							}
							else if("InternalBouncedCheques".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name)){

								String xml_str = int_xml.get(parent_tag);
								CreditCard.mLogger.info(" before adding InternalBouncedCheques+ "+xml_str);
								String temp = InternalBouncedCheques();
								if(!"".equalsIgnoreCase(temp)){
									if (xml_str==null){
										CreditCard.mLogger.info(" before adding bhrabc"+xml_str);
										xml_str="";
									}
									xml_str =  xml_str+ temp;
									CreditCard.mLogger.info(" after InternalBouncedCheques+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}

							}
							else if("InternalBureauIndividualProducts".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name)){

								String xml_str = int_xml.get(parent_tag);
								CreditCard.mLogger.info(" before adding InternalBureauIndividualProducts+ "+xml_str);
								String temp = InternalBureauIndividualProducts();
								if(!"".equalsIgnoreCase(temp)){
									if (xml_str==null){
										CreditCard.mLogger.info(" before adding bhrabc"+xml_str);
										xml_str="";
									}
									xml_str =  xml_str+ temp;
									CreditCard.mLogger.info(" after InternalBureauIndividualProducts+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}

							}
							else if("InternalBureauPipelineProducts".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name)){

								String xml_str = int_xml.get(parent_tag);
								CreditCard.mLogger.info(" before adding InternalBureauPipelineProducts+ "+xml_str);
								String temp = InternalBureauPipelineProducts();
								if(!"".equalsIgnoreCase(temp)){
									if (xml_str==null){
										CreditCard.mLogger.info(" before adding bhrabc"+xml_str);
										xml_str="";
									}
									xml_str =  xml_str+ temp;
									CreditCard.mLogger.info(" after InternalBureauPipelineProducts+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}

							}
							else if("ExternalBureau".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name)){

								String xml_str = int_xml.get(parent_tag);
								CreditCard.mLogger.info(" before adding ExternalBureau+ "+xml_str);
								String temp = ExternalBureauData();
								if(!"".equalsIgnoreCase(temp)){
									if (xml_str==null){
										CreditCard.mLogger.info(" before adding bhrabc"+xml_str);
										xml_str="";
									}
									xml_str =  xml_str+ temp;
									CreditCard.mLogger.info(" after ExternalBureau+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}

							}
							else if("ExternalBouncedCheques".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name)){

								String xml_str = int_xml.get(parent_tag);
								CreditCard.mLogger.info(" before adding ExternalBouncedCheques+ "+xml_str);
								String temp = ExternalBouncedCheques();
								if(!"".equalsIgnoreCase(temp)){
									if (xml_str==null){
										CreditCard.mLogger.info(" before adding bhrabc"+xml_str);
										xml_str="";
									}
									xml_str =  xml_str+ temp;
									CreditCard.mLogger.info(" after ExternalBouncedCheques+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}                    	
							}
							else if("ExternalBureauIndividualProducts".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name)){

								String xml_str = int_xml.get(parent_tag);
								String temp =  ExternalBureauIndividualProducts();
								CreditCard.mLogger.info(" value of temp to be adding temp+ "+temp);
								String Manual_add_Liab =  ExternalBureauManualAddIndividualProducts();

								if((!"".equalsIgnoreCase(temp)) || (!"".equalsIgnoreCase(Manual_add_Liab))){
									if (xml_str==null){
										CreditCard.mLogger.info(" before adding bhrabc"+xml_str);
										xml_str="";
									}
									xml_str =  xml_str+ temp + Manual_add_Liab;
									CreditCard.mLogger.info(" after ExternalBureauIndividualProducts+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}	                            	
							}
							else if("ExternalBureauPipelineProducts".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name)){

								String xml_str = int_xml.get(parent_tag);
								CreditCard.mLogger.info(" before adding ExternalBureauPipelineProducts+ "+xml_str);
								String temp =  ExternalBureauPipelineProducts();
								if(!"".equalsIgnoreCase(temp)){
									if (xml_str==null){
										CreditCard.mLogger.info(" before adding bhrabc"+xml_str);
										xml_str="";
									}
									xml_str =  xml_str+ temp;
									CreditCard.mLogger.info(" after ExternalBureauPipelineProducts+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}                        	
							}
							//for Dectech


							else if("MinorFlag".equalsIgnoreCase(tag_name) && "NEW_CUSTOMER_REQ".equalsIgnoreCase(Call_name) && "PersonDetails".equalsIgnoreCase(parent_tag)){
								if(int_xml.containsKey(parent_tag))
								{
									float Age = Float.parseFloat(formObject.getNGValue("cmplx_Customer_age"));
									String age_flag = "N";
									if(Age<18)
										age_flag="Y";
									String xml_str = int_xml.get(parent_tag);
									xml_str = xml_str + "<"+tag_name+">"+age_flag
									+"</"+ tag_name+">";

									CreditCard.mLogger.info(" after adding Minor flag+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}		                            	
							}
							else if("NonResidentFlag".equalsIgnoreCase(tag_name) && "NEW_CUSTOMER_REQ".equalsIgnoreCase(Call_name) && "PersonDetails".equalsIgnoreCase(parent_tag)){
								if(int_xml.containsKey(parent_tag))
								{
									String xml_str = int_xml.get(parent_tag);
									String res_flag ="N";

									if("Resident".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_ResidentNonResident"))){
										res_flag="Y";
									}

									xml_str = xml_str + "<"+tag_name+">"+res_flag
									+"</"+ tag_name+">";

									CreditCard.mLogger.info(" after adding res_flag+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}		                            	
							}
							else if("RecipientAddress".equalsIgnoreCase(tag_name) && "CHEQUE_BOOK_ELIGIBILITY".equalsIgnoreCase(Call_name) ){
								int add_len=formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
								String add_res_val ="";
								String xml_str = int_xml.get(parent_tag);
								if(add_len>0){
									for(int i=0;i<add_len;i++){
										CreditCard.mLogger.info(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0));
										if("Home".equalsIgnoreCase(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0))){
											formObject.setNGValue("cmplx_Customer_EmirateOfResidence",formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 6));
											add_res_val = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 1)+" "+ formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 2)+ formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 3)+ formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 4)+ formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 5)+ formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 6);
										}
									}
									xml_str = xml_str + "<"+tag_name+">"+add_res_val
									+"</"+ tag_name+">";

									CreditCard.mLogger.info(" after adding res_flag+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}
							}

							else if (parent_tag!=null && !parent_tag.equalsIgnoreCase(""))
							{
								CreditCard.mLogger.info("inside 1st if");
								if(int_xml.containsKey(parent_tag))
								{
									CreditCard.mLogger.info("inside 2nd if");
									String xml_str = int_xml.get(parent_tag);
									CreditCard.mLogger.info("inside 2nd if xml string"+xml_str);
									if("Y".equalsIgnoreCase(is_repetitive) && int_xml.containsKey(tag_name)){
										CreditCard.mLogger.info("inside 3rd if xml string");
										xml_str = int_xml.get(tag_name)+ "</"+tag_name+">" +"<"+ tag_name+">";
										CreditCard.mLogger.info("value after adding "+ Call_name+": "+xml_str);
										CreditCard.mLogger.info("inside 3rd if xml string xml string"+xml_str);
										int_xml.remove(tag_name);
										int_xml.put(tag_name, xml_str);
										CreditCard.mLogger.info("inside 3rd if xml string xml string int_xml");
									}
									else{
										CreditCard.mLogger.info("value after adding "+ Call_name+": "+xml_str+" form_control name: "+form_control);
										CreditCard.mLogger.info("valuie of form control: "+formObject.getNGValue(form_control));
										if("".equalsIgnoreCase(form_control.trim()) && "".equalsIgnoreCase(default_val.trim())){
											CreditCard.mLogger.info("inside if added by me"+"inside");
											xml_str = xml_str + "<"+tag_name+">"+"</"+ tag_name+">";
											CreditCard.mLogger.info("added by xml"+"xml_str"+xml_str);
										}
										else if (!(formObject.getNGValue(form_control)==null || "".equalsIgnoreCase(formObject.getNGValue(form_control).trim())||  "null".equalsIgnoreCase(formObject.getNGValue(form_control))))
										{
											CreditCard.mLogger.info("inside else of parent tag 1"+"form_control_val"+ form_control_val);
											if(fin_call_name.toUpperCase().contains(callName.toUpperCase())){
												form_control_val = formObject.getNGValue(form_control).toUpperCase();
											}
											else
												form_control_val = formObject.getNGValue(form_control);

											if(!"text".equalsIgnoreCase(data_format12)){
												String[] format_arr = data_format12.split(":");
												String format_name = format_arr[0];
												String format_type = format_arr[1];
												CreditCard.mLogger.info("format_name"+format_name);
												CreditCard.mLogger.info("format_type"+format_type);

												if("date".equalsIgnoreCase(format_name)){
													DateFormat df = new SimpleDateFormat("dd/MM/yyyy"); 
													DateFormat df_new = new SimpleDateFormat(format_type);

													try {
														startDate = df.parse(form_control_val);
														form_control_val = df_new.format(startDate);
														CreditCard.mLogger.info("RLOSCommon#Create Input"+" date conversion: final Output: "+form_control_val+ " requested format: "+format_type);

													} catch (ParseException e) {
														CreditCard.mLogger.info("RLOSCommon#Create Input"+" Error while format conversion: "+e.getMessage());
														CreditCard.logException(e); //e.printStackTrace();
													}
													catch (Exception e) {
														CreditCard.mLogger.info("RLOSCommon#Create Input"+" Error while format conversion: "+e.getMessage());
														CreditCard.logException(e); //e.printStackTrace();
													}
												}
												else if("number".equalsIgnoreCase(format_name) && form_control_val.contains(",")){
													
														form_control_val = form_control_val.replace(",", "");
													

												}
												//change here for other input format

											}
											CreditCard.mLogger.info("form_control_val"+ form_control_val);
											xml_str = xml_str + "<"+tag_name+">"+form_control_val
											+"</"+ tag_name+">";
											CreditCard.mLogger.info("xml_str"+ xml_str);
										}

										else if(default_val==null || "".equalsIgnoreCase(default_val.trim())){
											CreditCard.mLogger.info("no value found for tag name: "+ tag_name);
										}
										else{
											CreditCard.mLogger.info("#RLOS Common GenerateXML inside set default value");
											form_control_val = default_val;

											CreditCard.mLogger.info("form_control_val"+ form_control_val);
											xml_str = xml_str + "<"+tag_name+">"+form_control_val
											+"</"+ tag_name+">";
											CreditCard.mLogger.info("#RLOS Common GenerateXML inside else of parent tag form_control_val xml_str1"+"xml_str"+ xml_str);

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
										else if("PhnLocalCode".equalsIgnoreCase(tag_name) && "PhnDetails".equalsIgnoreCase(parent_tag) && "".equalsIgnoreCase(form_control_val)){
											if(xml_str.contains("</PhnDetails>")){
												xml_str = xml_str.substring(0, xml_str.lastIndexOf("</PhnDetails>"));
												int_xml.put(parent_tag, xml_str);
											}
											else
												int_xml.remove(parent_tag);
										}
										else if("Email".equalsIgnoreCase(tag_name) && "EmlDet".equalsIgnoreCase(parent_tag) && "".equalsIgnoreCase(form_control_val)){
											if(xml_str.contains("</EmlDet>")){
												xml_str = xml_str.substring(0, xml_str.lastIndexOf("</EmlDet>"));
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
										else{
											int_xml.put(parent_tag, xml_str);
										}
										//code change for to remove docdect incase ref no is not present end
										//int_xml.put(parent_tag, xml_str);
										CreditCard.mLogger.info("RLOSCommon"+"inside else"+xml_str);

									}

								}
								else{
									String new_xml_str ="";
									CreditCard.mLogger.info("value after adding "+ Call_name+": "+new_xml_str+" form_control name: "+form_control);
									CreditCard.mLogger.info("valuie of form control: "+formObject.getNGValue(form_control));
									if (!(formObject.getNGValue(form_control)==null || "".equalsIgnoreCase(formObject.getNGValue(form_control).trim())||  "null".equalsIgnoreCase(formObject.getNGValue(form_control)))){
										CreditCard.mLogger.info("form_control_val"+ form_control_val);
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
												// java.util.Date startDate;
												try {
													startDate = df.parse(form_control_val);
													form_control_val = df_new.format(startDate);
													CreditCard.mLogger.info(" date conversion: final Output: "+form_control_val+ " requested format: "+format_type);

												} catch (ParseException e) {
													CreditCard.mLogger.info(" Error while format conversion: "+e.getMessage());
													CreditCard.logException(e); //e.printStackTrace();
												}
												catch (Exception e) {
													CreditCard.mLogger.info(" Error while format conversion: "+e.getMessage());
													CreditCard.logException(e); //e.printStackTrace();
												}

											}
											//Changes Done to accept data format as number start
											else if("number".equalsIgnoreCase(format_name) && form_control_val.contains(",")){
												
													form_control_val = form_control_val.replace(",", "");
												
											}
											//Changes Done to accept data format as number END	
											//change here for other input format

										}
										CreditCard.mLogger.info("form_control_val"+ form_control_val);
										new_xml_str = new_xml_str + "<"+tag_name+">"+form_control_val
										+"</"+ tag_name+">";
										CreditCard.mLogger.info("new_xml_str"+ new_xml_str);
									}

									else if(default_val==null || "".equalsIgnoreCase(default_val.trim())){
										if(int_xml.containsKey(parent_tag)|| "Y".equalsIgnoreCase(is_repetitive)){
											new_xml_str = new_xml_str + "<"+tag_name+">"+"</"+ tag_name+">";
										}
										CreditCard.mLogger.info("no value found for tag name: "+ tag_name);
									}
									else{
										CreditCard.mLogger.info("");
										form_control_val = default_val;
										CreditCard.mLogger.info("form_control_val"+ form_control_val);
										new_xml_str = new_xml_str + "<"+tag_name+">"+form_control_val
										+"</"+ tag_name+">";
										CreditCard.mLogger.info("#RLOS Common GenerateXML inside else of parent tag form_control_val xml_str1"+"xml_str"+ new_xml_str);

									}
									int_xml.put(parent_tag, new_xml_str);
									CreditCard.mLogger.info("RLOSCommon"+"inside else"+new_xml_str);

								}
							}

						}


						final_xml=final_xml.append("<").append(parentTagName).append(">");
						CreditCard.mLogger.info("Final XMLold--"+final_xml);

						Iterator<Map.Entry<String,String>> itr = int_xml.entrySet().iterator();
						CreditCard.mLogger.info("itr"+itr);
						while (itr.hasNext())
						{
							Map.Entry<String, String> entry =  itr.next();
							CreditCard.mLogger.info("entry"+entry);
							if(final_xml.indexOf((entry.getKey()))>-1){
								CreditCard.mLogger.info("itr_value: Key: "+ entry.getKey()+" Value: "+entry.getValue());
								final_xml = final_xml.insert(final_xml.indexOf("<"+entry.getKey()+">")+entry.getKey().length()+2, entry.getValue());
								CreditCard.mLogger.info("final_xml"+final_xml);
								itr.remove();
							}

						}    
						final_xml=final_xml.append("</").append(parentTagName).append(">");
						CreditCard.mLogger.info("final_xml: "+final_xml);
						CreditCard.mLogger.info( final_xml.toString());
						final_xml.insert(0, header);
						final_xml.append(footer);
						CreditCard.mLogger.info( final_xml.toString());
						formObject.setNGValue("Is_"+callName,"Y");
						CreditCard.mLogger.info(formObject.getNGValue("Is_"+callName));

						
						
						cabinetName = FormContext.getCurrentInstance().getFormConfig().getConfigElement("EngineName");
						CreditCard.mLogger.info("cabinetName "+cabinetName);
						wi_name = FormContext.getCurrentInstance().getFormConfig().getConfigElement("ProcessInstanceId");
						ws_name = FormContext.getCurrentInstance().getFormConfig().getConfigElement("ActivityName");
						CreditCard.mLogger.info("ActivityName "+ws_name);
						sessionID = FormContext.getCurrentInstance().getFormConfig().getConfigElement("DMSSessionId");
						userName = FormContext.getCurrentInstance().getFormConfig().getConfigElement("UserName");
						CreditCard.mLogger.info("userName "+userName);
						CreditCard.mLogger.info("sessionID "+sessionID);



						String sMQuery = "SELECT SocketServerIP,SocketServerPort FROM NG_RLOS_MQ_TABLE with (nolock)";
						List<List<String>> outputMQXML=formObject.getDataFromDataSource(sMQuery);
						CreditCard.mLogger.info("sMQuery "+sMQuery);
						if(!outputMQXML.isEmpty()){
							CreditCard.mLogger.info(outputMQXML.get(0).get(0)+","+outputMQXML.get(0).get(1));
							socketServerIP =  outputMQXML.get(0).get(0);
							CreditCard.mLogger.info("socketServerIP "+socketServerIP);
							socketServerPort = Integer.parseInt(outputMQXML.get(0).get(1));
							CreditCard.mLogger.info("socketServerPort "+socketServerPort);
							if(!("".equalsIgnoreCase(socketServerIP) && socketServerIP==null && socketServerPort==0))
							{
								socket = new Socket(socketServerIP, socketServerPort);
								out = socket.getOutputStream();
								socketInputStream = socket.getInputStream();
								dout = new DataOutputStream(out);
								din = new DataInputStream(socketInputStream);
								mqOutputResponse="";	     
								mqInputRequest= getMQInputXML(sessionID,cabinetName,wi_name,ws_name,userName,final_xml);
								CreditCard.mLogger.info("mqInputRequest "+mqInputRequest);

								if (mqInputRequest != null && mqInputRequest.length() > 0) 
								{
									int outPut_len = mqInputRequest.getBytes("UTF-16LE").length;
									CreditCard.mLogger.info(Integer.toString(outPut_len)+"");
									mqInputRequest = outPut_len+"##8##;"+mqInputRequest;
									CreditCard.mLogger.info("Input Request Bytes : "+mqInputRequest.getBytes("UTF-16LE"));
									dout.write(mqInputRequest.getBytes("UTF-16LE"));
									dout.flush();                
								}
								byte[] readBuffer = new byte[50000];
								int num = din.read(readBuffer);
								boolean wait_flag = true;
								int out_len=0;

								if (num > 0) 
								{
									while(wait_flag){
										CreditCard.mLogger.info("num :"+num);
										byte[] arrayBytes = new byte[num];
										System.arraycopy(readBuffer, 0, arrayBytes, 0, num);
										mqOutputResponse = mqOutputResponse + new String(arrayBytes, "UTF-16LE");
										CreditCard.mLogger.info("inside loop output Response :\n"+mqOutputResponse);
										if(mqOutputResponse.contains("##8##;")){
											String[]	mqOutputResponse_arr = mqOutputResponse.split("##8##;");
											mqOutputResponse = mqOutputResponse_arr[1];
											out_len = Integer.parseInt(mqOutputResponse_arr[0]);
											CreditCard.mLogger.info("First Output Response :\n"+mqOutputResponse);
											CreditCard.mLogger.info("Output length :\n"+out_len);
										}
										if(out_len <= mqOutputResponse.getBytes("UTF-16LE").length){
											wait_flag=false;
										}
										Thread.sleep(100);
										num = din.read(readBuffer);

									}
									// Aman Code added for dectech to replace the &lt and &gt start 13 sept 2017
									if(mqOutputResponse.contains("&lt;")){
										CreditCard.mLogger.info("inside for Dectech :\n"+mqOutputResponse);
										mqOutputResponse=mqOutputResponse.replaceAll("&lt;", "<");
										CreditCard.mLogger.info("after replacing lt :\n"+mqOutputResponse);
										mqOutputResponse=mqOutputResponse.replaceAll("&gt;", ">");
										CreditCard.mLogger.info("after replacing gt :\n"+mqOutputResponse);
									}
									// Aman Code added for dectech to replace the &lt and &gt end 13 sept 2017

									CreditCard.mLogger.info("Final Output Response :\n"+mqOutputResponse);
									socket.close();
									return mqOutputResponse;
								}


							}
							else{
								CreditCard.mLogger.info("");
								CreditCard.mLogger.info(socketServerIP);
								CreditCard.mLogger.info(socketServerPort);
								return "MQ details not maintained";
							}
						}
						else{
							CreditCard.mLogger.info("SOcket details are not maintained in NG_RLOS_MQ_TABLE table"+"");
							return "MQ details not maintained";
						}
					}

				}
				else {
					CreditCard.mLogger.info("Genrate XML: "+"Entry is not maintained in field mapping Master table for : "+callName);
					return "Call not maintained";
				}


			}
			else{
				CreditCard.mLogger.info("Genrate XML: "+"Entry is not maintained in Master table for : "+callName);
				return "Call not maintained";
			}
		}
		catch (UnknownHostException e) 
		{        
			CreditCard.logException(e); //e.printStackTrace();
			return "0";
		}
		catch(Exception e){
			CreditCard.mLogger.info("Exception ocurred: "+e.getLocalizedMessage());
			CreditCard.mLogger.info("$$final_xml: "+final_xml);
			CreditCard.mLogger.info("Exception occured in main thread: "+ e.getMessage());
			return "0";
		}  
		finally
		{
			try
			{
			socket.close();
			}
			catch(Exception e)
			{
				CreditCard.logException(e);
			}
		}
		return "";
	
	}
	private String InternalBureauIndividualProducts() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getCustAddress_details(){
		CreditCard.mLogger.info( "inside getCustAddress_details : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int add_row_count = formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
		CreditCard.mLogger.info( "inside getCustAddress_details add_row_count+ : "+add_row_count);
		String  add_xml_str ="";
		for (int i = 0; i<add_row_count;i++){
			String Address_type = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0); //0
			String Po_Box=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 8);//1
			String flat_Villa=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 1);//2
			String Building_name=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 2);//3
			String street_name=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 3);//4
			String Landmard = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 4);//5
			String city=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 5);//6
			String Emirates=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 6);//7
			String country=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 7);//8
			String preferrd="";
			if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 10)))//10
				preferrd = "Y";
			else
				preferrd = "N";

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
		CreditCard.mLogger.info( "Address tag Cration: "+ add_xml_str);
		return add_xml_str;
	}
	public String InternalBouncedCheques(){
		CreditCard.mLogger.info( "inside InternalBouncedCheques : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "select CIFID,AcctId,returntype,returnNumber,returnAmount,retReasonCode,returnDate from ng_rlos_FinancialSummary_ReturnsDtls with (nolock) where Child_Wi = '"+formObject.getWFWorkitemName()+"'";
		CreditCard.mLogger.info("InternalBouncedCheques sQuery"+sQuery+ "");
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		CreditCard.mLogger.info("InternalBouncedCheques list size"+OutputXML.size()+"");

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
		CreditCard.mLogger.info( "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}
	
	public String InternalBureauPipelineProducts(){
		CreditCard.mLogger.info( "inside InternalBureauPipelineProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "SELECT cifid,product_type,custroletype,lastupdatedate,totalamount,totalnoofinstalments,totalloanamount,agreementId FROM ng_RLOS_CUSTEXPOSE_LoanDetails  with (nolock) where Child_wi = '"+formObject.getWFWorkitemName()+"' and  LoanStat = 'Pipeline'";
		CreditCard.mLogger.info("InternalBureauPipelineProducts sQuery"+sQuery+ "");
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		CreditCard.mLogger.info("InternalBureauPipelineProducts list size"+OutputXML.size()+ "");

		for (int i = 0; i<OutputXML.size();i++){

			String cifId = "";
			String Product = "";
			String lastUpdateDate = ""; 
			String TotAmount = "";
			String TotNoOfInstlmnt = "";
			String TotLoanAmt = "";
			String agreementId = "";

			if(!(OutputXML.get(i).get(0) == null || OutputXML.get(i).get(0).equals("")) )
			{
				cifId = OutputXML.get(i).get(0);
			}
			if(!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals("")) )
			{
				Product = OutputXML.get(i).get(1);
			}
			
			if(!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals("")) )
			{
				lastUpdateDate = OutputXML.get(i).get(3);
			}
			if(!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals("")) )
			{
				TotAmount = OutputXML.get(i).get(4);
			}
			if(!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5).equals("")) )
			{
				TotNoOfInstlmnt = OutputXML.get(i).get(5);
			}
			if(!(OutputXML.get(i).get(6) == null || OutputXML.get(i).get(6).equals("")) )
			{
				TotLoanAmt = OutputXML.get(i).get(6);
			}
			if(!(OutputXML.get(i).get(7) == null || OutputXML.get(i).get(7).equals("")) )
			{
				agreementId = OutputXML.get(i).get(7);
			}

			if(cifId!=null && !"".equalsIgnoreCase(cifId) && !"null".equalsIgnoreCase(cifId))
			{
				add_xml_str = add_xml_str + "<InternalBureauPipelineProducts>";
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
		CreditCard.mLogger.info( "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}
	public String ExternalBureauData()
	{
		CreditCard.mLogger.info( "inside ExternalBureauData : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "select  CifId,fullnm,TotalOutstanding,TotalOverdue,NoOfContracts,Total_Exposure,WorstCurrentPaymentDelay,Worst_PaymentDelay_Last24Months,Worst_Status_Last24Months,Nof_Records,NoOf_Cheque_Return_Last3,Nof_DDES_Return_Last3Months,Nof_Cheque_Return_Last6,DPD30_Last6Months,Internal_WriteOff_Check from NG_rlos_custexpose_Derived where Child_wi  = '"+formObject.getWFWorkitemName()+"' and Request_type= 'ExternalExposure'";
		CreditCard.mLogger.info("ExternalBureauData sQuery"+sQuery+ "");
		String AecbHistQuery = "select isnull(max(AECBHistMonthCnt),0) as AECBHistMonthCnt from ( select MAX(AECBHistMonthCnt) as AECBHistMonthCnt  from ng_rlos_cust_extexpo_CardDetails where  Child_wi  = '"+formObject.getWFWorkitemName()+"' union select Max(AECBHistMonthCnt) from ng_rlos_cust_extexpo_LoanDetails where Child_wi  = '"+formObject.getWFWorkitemName()+"') as ext_expo";
		String  add_xml_str ="";
		try{
			List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
			CreditCard.mLogger.info("ExternalBureauData list size"+OutputXML.size()+ "");
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



				CreditCard.mLogger.info( "Internal liab tag Cration: "+ add_xml_str);
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
					if(OutputXML.get(i).get(6)!=null && !OutputXML.get(i).get(6).isEmpty() &&  !OutputXML.get(i).get(6).equals("") && !"null".equalsIgnoreCase(OutputXML.get(i).get(6)) ){
						WorstCurrentPaymentDelay = OutputXML.get(i).get(6);
					}
					if(OutputXML.get(i).get(7)!=null && !OutputXML.get(i).get(7).isEmpty() &&  !OutputXML.get(i).get(7).equals("") && !"null".equalsIgnoreCase(OutputXML.get(i).get(7)) ){
						Worst_PaymentDelay_Last24Months = OutputXML.get(i).get(7);
					}				
					if(OutputXML.get(i).get(8)!=null && !OutputXML.get(i).get(8).isEmpty() &&  !OutputXML.get(i).get(8).equals("") && !"null".equalsIgnoreCase(OutputXML.get(i).get(8)) ){
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
					if(OutputXML.get(i).get(12)!=null && !OutputXML.get(i).get(12).isEmpty() &&  !OutputXML.get(i).get(12).equals("") && !"null".equalsIgnoreCase(OutputXML.get(i).get(12)) ){
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
				CreditCard.mLogger.info( "Internal liab tag Cration: "+ add_xml_str);
				return add_xml_str;
			}

		}

		catch(Exception e)
		{
			CreditCard.mLogger.info( "Exception occurred in externalBureauData()"+e.getMessage()+"\n Error: "+CC_Common.printException(e));
			return null;
		}
	}
	public String ExternalBouncedCheques(){
		CreditCard.mLogger.info( "inside ExternalBouncedCheques : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "SELECT cifid,number,amount,reasoncode,returndate,providerno FROM ng_rlos_cust_extexpo_ChequeDetails  with (nolock) where child_wi = '"+formObject.getWFWorkitemName()+"' and Request_Type = 'ExternalExposure'";
		CreditCard.mLogger.info("ExternalBouncedCheques sQuery"+sQuery+ "");
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		CreditCard.mLogger.info("ExternalBouncedCheques list size"+OutputXML.size()+ "");

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
		CreditCard.mLogger.info( "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}
	public String ExternalBureauIndividualProducts(){
		CreditCard.mLogger.info( "inside ExternalBureauIndividualProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "select CifId,AgreementId,LoanType,ProviderNo,LoanStat,CustRoleType,LoanApprovedDate,LoanMaturityDate,OutstandingAmt,TotalAmt,PaymentsAmt,TotalNoOfInstalments,RemainingInstalments,WriteoffStat,WriteoffStatDt,CreditLimit,OverdueAmt,NofDaysPmtDelay,MonthsOnBook,lastrepmtdt,IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last18Months,AECBHistMonthCnt,DPD5_Last3Months,'' as qc_Amnt,'' as Qc_emi,'' as Cac_indicator,Take_Over_Indicator,Consider_For_Obligations from ng_rlos_cust_extexpo_LoanDetails where Child_wi= '"+formObject.getWFWorkitemName()+"'  and LoanStat != 'Pipeline' union select CifId,CardEmbossNum,CardType,ProviderNo,CardStatus,CustRoleType,StartDate,ClosedDate,CurrentBalance,'' as col6,PaymentsAmount,NoOfInstallments,'' as col5,WriteoffStat,WriteoffStatDt,CashLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,lastrepmtdt,IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last18Months,AECBHistMonthCnt,DPD5_Last3Months,qc_amt,qc_emi,CAC_Indicator,Take_Over_Indicator,Consider_For_Obligations from ng_rlos_cust_extexpo_CardDetails where Child_wi  =  '"+formObject.getWFWorkitemName()+"' and cardstatus != 'Pipeline' ";
		CreditCard.mLogger.info("ExternalBureauIndividualProducts sQuery"+sQuery+ "");
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		CreditCard.mLogger.info("ExternalBureauIndividualProducts list size"+OutputXML.size()+ "");
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
					CreditCard.mLogger.info("ExternalBureauIndividualProducts sQuery"+cardquery+ "");
					List<List<String>> cardqueryXML = formObject.getDataFromDataSource(cardquery);
					ContractType=cardqueryXML.get(0).get(0);
					CreditCard.mLogger.info("ExternalBureauIndividualProducts ContractType"+ContractType+ "ContractType");
				}
				catch(Exception e){
					CreditCard.mLogger.info("ExternalBureauIndividualProducts ContractType Exception"+e+ "Exception");

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
			if(!(OutputXML.get(i).get(29) == null || OutputXML.get(i).get(29).equals("")) ){
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
			if(!(OutputXML.get(i).get(30) == null || OutputXML.get(i).get(30).equals("")) ){
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
				add_xml_str = add_xml_str + "<qc_amount>"+QC_Amt+"</qc_amount><company_flag>N</company_flag><cac_bank_name>"+formObject.getNGValue("cmplx_EmploymentDetails_tenancycntrctemirate")+"</cac_bank_name><take_over_indicator>"+TakeOverIndicator+"</take_over_indicator><consider_for_obligation>"+consider_for_obligation+"</consider_for_obligation></ExternalBureauIndividualProducts>";
			}
			else{
				add_xml_str = add_xml_str + "<qc_amount>"+QC_Amt+"</qc_amount><company_flag>N</company_flag><cac_bank_name>"+formObject.getNGValue("cmplx_EmploymentDetails_tenancycntrctemirate")+"</cac_bank_name><take_over_indicator>"+TakeOverIndicator+"</take_over_indicator></ExternalBureauIndividualProducts>";
			}

		}
		CreditCard.mLogger.info( "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}
	public String ExternalBureauManualAddIndividualProducts(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		CreditCard.mLogger.info("ExternalBureauManualAddIndividualProducts sQuery"+ "");
		int Man_liab_row_count = formObject.getLVWRowCount("cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
		String applicant_id = formObject.getNGValue("cmplx_Customer_CIFNO");
		String  add_xml_str ="";
		Date date = new Date();
		String modifiedDate= new SimpleDateFormat("dd/MM/yyyy").format(date);
		CreditCard.mLogger.info("ExternalBureauIndividualProducts list size"+Man_liab_row_count+ "");
		if (Man_liab_row_count !=0){
			for (int i = 0; i<Man_liab_row_count;i++){
				String Type_of_Contract = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 0); //0
				String Limit = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 1); //0
				String EMI = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 2); //0
				String Take_over_Indicator = ("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 3))?"Y":"N"); //0
				String cac_Indicator = ("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 5))?"Y":"N"); //0
				String Qc_amt = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 6); //0
				String Qc_Emi = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 7); //0
				if ("true".equalsIgnoreCase(cac_Indicator)){
					cac_Indicator="Y";
				}
				else {
					cac_Indicator="N";
				}
				String consider_for_obligation = ("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 8))?"Y":"N"); //0
				//String MOB = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 9); //0
				String Utilization = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 11); //0
				String OutStanding = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 12); //0
				String mob = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 10);
				String delinquent_in_last_3months = ("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 13))?"1":"0");
				String dpd_30_last_6_mon = ("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 14))?"1":"0");
				String dpd_60p_in_last_18_mon = ("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 15))?"1":"0");
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
				add_xml_str = add_xml_str + "<payments_amount>"+Limit+"</payments_amount>";
				add_xml_str = add_xml_str + "<total_no_of_instalments></total_no_of_instalments>";
				add_xml_str = add_xml_str + "<no_of_remaining_instalments></no_of_remaining_instalments>";
				add_xml_str = add_xml_str + "<worst_status></worst_status>";
				add_xml_str = add_xml_str + "<worst_status_date></worst_status_date>";

				add_xml_str = add_xml_str + "<credit_limit>"+EMI+"</credit_limit>";
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
				add_xml_str = add_xml_str + "<qc_amount>"+Qc_amt+"</qc_amount><cac_bank_name>"+formObject.getNGValue("cmplx_EmploymentDetails_tenancycntrctemirate")+"</cac_bank_name><take_over_indicator>"+Take_over_Indicator+"</take_over_indicator><company_flag>N</company_flag><consider_for_obligation>"+consider_for_obligation+"</consider_for_obligation></ExternalBureauIndividualProducts>";

			}

		}
		CreditCard.mLogger.info( "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}

	public String ExternalBureauPipelineProducts(){
		CreditCard.mLogger.info( "inside ExternalBureauPipelineProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "select AgreementId,ProviderNo,LoanType,LoanDesc,CustRoleType,Datelastupdated,Total_Amount,TotalNoOfInstalments,'' as col1,NoOfDaysInPipeline from ng_rlos_cust_extexpo_LoanDetails where child_wi  =  '"+formObject.getWFWorkitemName()+"' and LoanStat = 'Pipeline' union select CardEmbossNum,ProviderNo,CardTypeDesc,CardType,CustRoleType,LastUpdateDate,'' as col2,NoOfInstallments,TotalAmount,NoOfDaysInPipeLine  from ng_rlos_cust_extexpo_CardDetails where child_wi  =  '"+formObject.getWFWorkitemName()+"' and cardstatus = 'Pipeline'";
		CreditCard.mLogger.info("ExternalBureauPipelineProducts sQuery"+sQuery+ "");
		String  add_xml_str = "";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		CreditCard.mLogger.info("ExternalBureauPipelineProducts list size"+OutputXML.size()+ "");
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
			if(!(OutputXML.get(i).get(0) == null || OutputXML.get(i).get(0).equals("")) ){
				agreementID = OutputXML.get(i).get(0).toString();
			}
			if(!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals("")) ){
				ProviderNo = OutputXML.get(i).get(1).toString();
			}
			if(OutputXML.get(i).get(2)!=null && !OutputXML.get(i).get(2).isEmpty() &&  !OutputXML.get(i).get(2).equals("") && !"null".equalsIgnoreCase(OutputXML.get(i).get(2)) ){
				contractType = OutputXML.get(i).get(2).toString();
			}
			if(!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals("")) ){
				productType = OutputXML.get(i).get(3).toString();
			}
			if(!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals("")) ){
				role = OutputXML.get(i).get(4).toString();
			}
			if(OutputXML.get(i).get(5)!=null && !OutputXML.get(i).get(5).isEmpty() &&  !OutputXML.get(i).get(5).equals("") && !"null".equalsIgnoreCase(OutputXML.get(i).get(5)) ){
				lastUpdateDate = OutputXML.get(i).get(5).toString();
			}
			if(OutputXML.get(i).get(6)!=null && !OutputXML.get(i).get(6).isEmpty() &&  !OutputXML.get(i).get(6).equals("") && !"null".equalsIgnoreCase(OutputXML.get(i).get(6)) ){
				TotAmt = OutputXML.get(i).get(6).toString();
			}
			if(!(OutputXML.get(i).get(7) == null || OutputXML.get(i).get(7).equals("")) ){
				noOfInstalmnt = OutputXML.get(i).get(7).toString();
			}
			if(!(OutputXML.get(i).get(8) == null || OutputXML.get(i).get(8).equals("")) ){
				creditLimit = OutputXML.get(i).get(8).toString();
			}
			if(OutputXML.get(i).get(9)!=null && !OutputXML.get(i).get(9).isEmpty() &&  !OutputXML.get(i).get(9).equals("") && !"null".equalsIgnoreCase(OutputXML.get(i).get(9)) ){
				noOfDayinPpl = OutputXML.get(i).get(9).toString();
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

			add_xml_str = add_xml_str + "<ppl_no_of_days_in_pipeline>"+noOfDayinPpl+"</ppl_no_of_days_in_pipeline><company_flag>N</company_flag><consider_for_obligation></consider_for_obligation></ExternalBureauPipelineProducts>"; // to be populated later




		}
		CreditCard.mLogger.info( "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}
	private static String getMQInputXML(String sessionID, String cabinetName, String wi_name, String ws_name, String userName, StringBuffer final_xml) 
	{
		FormContext.getCurrentInstance().getFormConfig( );

		StringBuffer strBuff=new StringBuffer();
		strBuff.append("<APMQPUTGET_Input>");
		strBuff.append("<SessionId>"+sessionID+"</SessionId>");
		strBuff.append("<EngineName>"+cabinetName+"</EngineName>");
		strBuff.append("<XMLHISTORY_TABLENAME>NG_CC_XMLLOG_HISTORY</XMLHISTORY_TABLENAME>");
		strBuff.append("<WI_NAME>"+wi_name+"</WI_NAME>");
		strBuff.append("<WS_NAME>"+ws_name+"</WS_NAME>");
		strBuff.append("<USER_NAME>"+userName+"</USER_NAME>");
		strBuff.append("<MQ_REQUEST_XML>");
		strBuff.append(final_xml);		
		strBuff.append("</MQ_REQUEST_XML>");
		strBuff.append("</APMQPUTGET_Input>");	
		CreditCard.mLogger.info("inside getOutputXMLValues"+"getMQInputXML"+strBuff.toString());
		return strBuff.toString();
	}
	
	
	
	
}
