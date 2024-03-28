package com.newgen.omniforms.user;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.Socket;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;

public class RLOS_Integration_Input  implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public  String GenerateXML(String callName,String Operation_name)
	{
		RLOS.mLogger.info("Inside GenerateXML():");
		RLOSCommon rlosCommon=new RLOSCommon();
		
		StringBuilder final_xml= new StringBuilder("");
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
		Integer socketServerPort;
		String fin_call_name="Customer_details, Customer_eligibility,new_customer_req,new_account_req";
		RLOS.mLogger.info("before try");
		try
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			 String emp_type = formObject.getNGValue("empType");

			String sQuery_header = "SELECT Header,Footer,parenttagname FROM NG_Integration with (nolock) where Call_name='"+callName+"'";
			RLOS.mLogger.info("sQuery"+sQuery_header);
			List<List<String>> OutputXML_header=formObject.getDataFromDataSource(sQuery_header);
			if(!OutputXML_header.isEmpty()){
				RLOS.mLogger.info(OutputXML_header.get(0).get(0)+" footer: "+OutputXML_header.get(0).get(1)+" parenttagname: "+OutputXML_header.get(0).get(2));
				header = OutputXML_header.get(0).get(0);
				footer = OutputXML_header.get(0).get(1);
				parentTagName = OutputXML_header.get(0).get(2);
				String col_n = "call_type,Call_name,form_control,parent_tag_name,xmltag_name,is_repetitive,default_val,data_format";
				// String col_n = "call_type,Call_name,form_control,parent_tag_name,xmltag_name,is_repetitive,default_val";
				// String sQuery = "SELECT call_type, Call_name,form_control,parent_tag_name,xmltag_name,is_repetitive FROM NG_Integration_field_Mapping where Call_name='"+callName+"'ORDER BY tag_seq ASC" ;
				if(!("".equalsIgnoreCase(Operation_name) || Operation_name ==null))
				{   
					RLOS.mLogger.info("operation111"+Operation_name);
					RLOS.mLogger.info("callName111"+callName);
					sQuery = "SELECT "+col_n +" FROM NG_Integration_field_Mapping with (nolock) where Call_name='"+callName+"' and active = 'Y' and Operation_name='"+Operation_name+"' ORDER BY tag_seq ASC" ;
					RLOS.mLogger.info("sQuery "+sQuery);
				}
				else{
					RLOS.mLogger.info("operation"+Operation_name);
					sQuery = "SELECT "+col_n +" FROM NG_Integration_field_Mapping with (nolock) where Call_name='"+callName+"' and active = 'Y' ORDER BY tag_seq ASC" ;
					RLOS.mLogger.info("sQuery "+sQuery);
				}

				List<List<String>> OutputXML=formObject.getDataFromDataSource(sQuery);
				RLOS.mLogger.info("OutputXML"+OutputXML);
				if(!OutputXML.isEmpty()){
					//RLOS.mLogger.info(OutputXML.get(0).get(0)+OutputXML.get(0).get(1)+OutputXML.get(0).get(2)+OutputXML.get(0).get(3));
					RLOS.mLogger.info(OutputXML.get(0).get(0)+OutputXML.get(0).get(1)+OutputXML.get(0).get(2)+OutputXML.get(0).get(3)+OutputXML.get(0).get(4));
					RLOS.mLogger.info(OutputXML.get(0).get(0)+OutputXML.get(0).get(1)+OutputXML.get(0).get(2)+OutputXML.get(0).get(3));
					int n=OutputXML.size();
					RLOS.mLogger.info(n);


					if( n> 0)
					{
						RLOS.mLogger.info("column length"+col_n.length());
						Map<String, String> int_xml = new LinkedHashMap<String, String>();
						Map<String, String> recordFileMap = new HashMap<String, String>();

						for(List<String> mylist:OutputXML)
						{
							// for(int i=0;i<col_n.length();i++)
							for(int i=0;i<8;i++)
							{
								//RLOS.mLogger.info("rec: "+records.item(rec));
								RLOS.mLogger.info("column length values"+col_n);
								String[] col_name = col_n.split(",");
								recordFileMap.put(col_name[i],mylist.get(i));
							}
							recordFileMap.get("call_type");
							String Call_name =  recordFileMap.get("Call_name");
							String form_control =  recordFileMap.get("form_control");
							String parent_tag =  recordFileMap.get("parent_tag_name");
							String tag_name =  recordFileMap.get("xmltag_name");
							String is_repetitive =  recordFileMap.get("is_repetitive");
							String default_val =  recordFileMap.get("default_val");
							String data_format12 =  recordFileMap.get("data_format");
							RLOS.mLogger.info("tag_name : "+tag_name +" valuie of default_val: "+default_val+" Call_name: "+Call_name+" parent_tag"+ parent_tag);
							String form_control_val="";
							java.util.Date startDate;

							if("AddressDetails".equalsIgnoreCase(tag_name) && "NEW_CUSTOMER_REQ".equalsIgnoreCase(Call_name))
							{
								if(int_xml.containsKey(parent_tag))
								{
									String xml_str = int_xml.get(parent_tag);
									RLOS.mLogger.info(" before adding address+ "+xml_str);
									xml_str = xml_str + RLOS_Integration_Output.getCustAddress_details();
									RLOS.mLogger.info(" after adding address+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}		                            	
							}
							else if("CCIFID".equalsIgnoreCase(tag_name)){
								if(int_xml.containsKey(parent_tag))
								{
									String xml_str = int_xml.get(parent_tag);
									RLOS.mLogger.info(" before CIF+ "+xml_str);
									String Cif = formObject.getNGValue(form_control);
									if(Cif!=null && !"".equalsIgnoreCase(Cif) && Cif.length()!=7){
										Cif = "0"+Cif;
									}
									xml_str = xml_str + "<"+tag_name+">"+Cif+"</"+ tag_name+">";
									RLOS.mLogger.info(" after adding address+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}		                            	
							}
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

									RLOS.mLogger.info(" after adding Minor flag+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}		                            	
							}
							//added by saurabh on 23rd Aug 17.
							else if("AcRequired".equalsIgnoreCase(tag_name) && "NEW_ACCOUNT_REQ".equalsIgnoreCase(Call_name)){
                                RLOS.mLogger.info("inside New acc request for AcRequired");
                                String loantype = ("Conventional".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 0))?"KC":"AC");
                                String xml_str = int_xml.get(parent_tag);
                                xml_str = xml_str+"<"+tag_name+">"+loantype+"</"+ tag_name+">";
                               RLOS.mLogger.info("adding Host name in New Loan Req:  "+xml_str);
                                int_xml.put(parent_tag, xml_str);                                     
                          }
							//added by saurabh on 23rd Aug 17.
						

							// added for dectech call 
							else if("CallType".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name)){
								RLOS.mLogger.info(" iNSIDE CallType+ ");
								String CallType=formObject.getNGValue("DecCallFired");
								RLOS.mLogger.info(" CallType "+CallType);
									//String ReqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
									
									String xml_str = int_xml.get(parent_tag);
								if ("Eligibility".equalsIgnoreCase(CallType)){	
									xml_str = xml_str+ "<"+tag_name+">"+"PM"
									+"</"+ tag_name+">";
								}
								else if("CalculateDBR".equalsIgnoreCase(CallType)){
									xml_str = xml_str+ "<"+tag_name+">"+"CA"
									+"</"+ tag_name+">";
								}
								/*else{
									String alert_msg="Error Occured in CAS";
									throw new ValidatorException(new FacesMessage(alert_msg));
								}*/
									RLOS.mLogger.info(" after adding CallType+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
										                            	
							} 
							else if("Channel".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name)){
								RLOS.mLogger.info(" iNSIDE channelcode+ ");
								
									String ReqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
									
									String xml_str = int_xml.get(parent_tag);
									xml_str =  "<"+tag_name+">"+("Personal Loan".equalsIgnoreCase(ReqProd)?"PL":"CC")
									+"</"+ tag_name+">";

									RLOS.mLogger.info(" after adding channelcode+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
										                            	
							}
							
							else if("emp_type".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name)){
								RLOS.mLogger.info(" iNSIDE channelcode+ ");
								
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

									RLOS.mLogger.info(" after adding channelcode+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
										                            	
							}
							else if("world_check".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name)){
								RLOS.mLogger.info(" iNSIDE world_check+ ");
								
									String world_check=formObject.getNGValue("IS_WORLD_CHECK");
									RLOS.mLogger.info(" iNSIDE world_check+ "+formObject.getLVWRowCount("cmplx_WorldCheck_WorldCheck_Grid"));
									if (formObject.getLVWRowCount("cmplx_WorldCheck_WorldCheck_Grid")==0){
										world_check="Negative";
									}
									else {
										world_check="Positive";
									}
									
								
									String xml_str = int_xml.get(parent_tag);
									xml_str = xml_str+ "<"+tag_name+">"+world_check+"</"+ tag_name+">";

									RLOS.mLogger.info(" after adding world_check+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
										                            	
							}
							else if("current_emp_catogery".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name))
							{
								RLOS.mLogger.info(" iNSIDE current_emp_catogery+ ");

								String current_emp_catogery=formObject.getNGValue("cmplx_EmploymentDetails_CurrEmployer");
								RLOS.mLogger.info(" value of current_emp_catogery "+current_emp_catogery);
								String squerycurremp="select Description from NG_MASTER_EmployerCategory_PL where isActive='Y' and code='"+current_emp_catogery+"'";
								RLOS.mLogger.info(" query is "+squerycurremp);
								List<List<String>> squerycurrempXML=formObject.getDataFromDataSource(squerycurremp);
								RLOS.mLogger.info(" query is "+squerycurrempXML);
								if(!squerycurrempXML.isEmpty() && squerycurrempXML.get(0).get(0)!=null)
								{								
									RLOS.mLogger.info(" iNSIDE squerycurrempXML+ "+squerycurrempXML.get(0).get(0));
									current_emp_catogery=squerycurrempXML.get(0).get(0);								
								}

								String xml_str = int_xml.get(parent_tag);
								xml_str = xml_str+ "<"+tag_name+">"+current_emp_catogery+"</"+ tag_name+">";

								RLOS.mLogger.info(" after adding current_emp_catogery+ "+xml_str);
								int_xml.put(parent_tag, xml_str);

							}	
							else if(("prev_loan_dbr".equalsIgnoreCase(tag_name)||"prev_loan_tai".equalsIgnoreCase(tag_name)||"prev_loan_multiple".equalsIgnoreCase(tag_name)||"prev_loan_employer".equalsIgnoreCase(tag_name)) && "DECTECH".equalsIgnoreCase(Call_name))
							{
								RLOS.mLogger.info(" iNSIDE prev_loan_dbr+ ");
								String PreviousLoanDBR="";
								String PreviousLoanEmp="";
								String PreviousLoanMultiple="";
								String PreviousLoanTAI="";

								String squeryloan="select isNull(PreviousLoanDBR,0), isNull(PreviousLoanEmp,0), isNull(PreviousLoanMultiple,0), isNull(PreviousLoanTAI,0) from ng_RLOS_CUSTEXPOSE_LoanDetails where Limit_Increase='true'  and Wi_Name= '"+formObject.getWFWorkitemName()+"'";
								List<List<String>> prevLoan=formObject.getDataFromDataSource(squeryloan);
								RLOS.mLogger.info(" iNSIDE prev_loan_dbr+ "+squeryloan);
								
								if (prevLoan!=null && !prevLoan.isEmpty())
								{
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

									RLOS.mLogger.info(" after adding world_check+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
										                            	
							}
							
							else if("no_of_cheque_bounce_int_3mon_Ind".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name)){
								RLOS.mLogger.info(" iNSIDE no_of_cheque_bounce_int_3mon_Ind+ ");
								String squerynoc="SELECT count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-3,GETDATE()) and returntype='ICCS' and WI_Name='"+formObject.getWFWorkitemName()+"'";
								List<List<String>> NOC=formObject.getDataFromDataSource(squerynoc);
								if (NOC!=null && !NOC.isEmpty())
								{
									String xml_str =  int_xml.get(parent_tag);
									xml_str = xml_str+ "<"+tag_name+">"+NOC.get(0).get(0)
									+"</"+ tag_name+">";

									RLOS.mLogger.info(" after adding internal_blacklist+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
							
								}
								
							}
							else if("no_of_DDS_return_int_3mon_Ind".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name)){
								RLOS.mLogger.info(" iNSIDE no_of_cheque_bounce_int_3mon_Ind+ ");
								String squerynoc="SELECT count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-3,GETDATE()) and returntype='DDS' and WI_Name='"+formObject.getWFWorkitemName()+"'";
								List<List<String>> NOC=formObject.getDataFromDataSource(squerynoc);
								if (NOC!=null && !NOC.isEmpty())
								{
									String xml_str =  int_xml.get(parent_tag);
									xml_str = xml_str+ "<"+tag_name+">"+NOC.get(0).get(0)
									+"</"+ tag_name+">";

									RLOS.mLogger.info(" after adding internal_blacklist+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
							
								}

							}
							
							else if((("lob".equalsIgnoreCase(tag_name))||("target_segment_code".equalsIgnoreCase(tag_name))||("designation".equalsIgnoreCase(tag_name))||("emp_name".equalsIgnoreCase(tag_name))||("industry_sector".equalsIgnoreCase(tag_name))||("industry_marco".equalsIgnoreCase(tag_name))||("industry_micro".equalsIgnoreCase(tag_name))||("bvr".equalsIgnoreCase(tag_name))||("eff_date_estba".equalsIgnoreCase(tag_name))||("poa".equalsIgnoreCase(tag_name))||("tlc_issue_date".equalsIgnoreCase(tag_name))) && "DECTECH".equalsIgnoreCase(Call_name) && "Self Employed".equalsIgnoreCase(emp_type)){
								RLOS.mLogger.info("RLOSCommon java file inside getProduct_details : ");
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
								String tlc_issue_date="";
							if(Comp_row_count!=0){	
								for (int i = 0; i<Comp_row_count;i++){
									lob = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 15); //0
									target_segment_code = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 24); //0
									designation = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 11); //0
									emp_name = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 0); //0
									industry_sector = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 8); //0
									eff_date_estba = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 14); //0
									//industry_sector = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 5); //0
									industry_marco = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 9); //0
									industry_micro = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 10); //0
									bvr = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 19); //0
									poa = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 23); //0
									tlc_issue_date = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 26); //0
									
								
									}
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
								else if("bvr".equalsIgnoreCase(tag_name)){
									xml_str = xml_str+ "<"+tag_name+">"+bvr+"</"+ tag_name+">";
								}
								else if(eff_date_estba.equalsIgnoreCase(tag_name)){
									xml_str = xml_str+ "<"+tag_name+">"+eff_date_estba+"</"+ tag_name+">";
								}
								else if("poa".equalsIgnoreCase(tag_name)){
									xml_str = xml_str+ "<"+tag_name+">"+poa+"</"+ tag_name+">";
								}
								else if("tlc_issue_date".equalsIgnoreCase(tag_name)){
									xml_str = xml_str+ "<"+tag_name+">"+tlc_issue_date+"</"+ tag_name+">";
								}
								RLOS.mLogger.info(" after adding cmplx_CompanyDetails_cmplx_CompanyGrid+ "+xml_str);
								int_xml.put(parent_tag, xml_str);
							}
							
							else if((("blacklist_cust_type".equalsIgnoreCase(tag_name))||("internal_blacklist".equalsIgnoreCase(tag_name))||("internal_blacklist_date".equalsIgnoreCase(tag_name))||("internal_blacklist_code".equalsIgnoreCase(tag_name))||("negative_cust_type".equalsIgnoreCase(tag_name))||("internal_negative_flag".equalsIgnoreCase(tag_name))||("internal_negative_date".equalsIgnoreCase(tag_name))||("internal_negative_code".equalsIgnoreCase(tag_name))) && "DECTECH".equalsIgnoreCase(Call_name)){
								RLOS.mLogger.info(" iNSIDE channelcode+ ");
								String squeryBlacklist="select BlacklistFlag,BlacklistDate,BlacklistReasonCode,NegatedFlag,NegatedDate,NegatedReasonCode from ng_rlos_cif_detail where cif_wi_name='"+formObject.getWFWorkitemName()+"' and cif_searchType = 'Internal'";
								List<List<String>> Blacklist=formObject.getDataFromDataSource(squeryBlacklist);
								String internal_blacklist =  "";
								String internal_blacklist_date =  "";
								String internal_blacklist_code =  "";
								String internal_negative_flag =  "";
								String internal_negative_date =  "";
								String internal_negative_code =  "";
								
								if (Blacklist!=null && !Blacklist.isEmpty())
								{		
									internal_blacklist =  Blacklist.get(0).get(0);
									internal_blacklist_date =  Blacklist.get(0).get(1);
									internal_blacklist_code =  Blacklist.get(0).get(2);
									internal_negative_flag =  Blacklist.get(0).get(3);
									internal_negative_date =  Blacklist.get(0).get(4);
									internal_negative_code =  Blacklist.get(0).get(5);
									}
								String xml_str =  int_xml.get(parent_tag);

								if("blacklist_cust_type".equalsIgnoreCase(tag_name) || "negative_cust_type".equalsIgnoreCase(tag_name))
								{
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
								RLOS.mLogger.info(" after adding internal_blacklist+ "+xml_str);
								int_xml.put(parent_tag, xml_str);
						
										                            	
							}

							else if(("external_blacklist_flag".equalsIgnoreCase(tag_name)||"external_blacklist_date".equalsIgnoreCase(tag_name)||"external_blacklist_code".equalsIgnoreCase(tag_name)) && "DECTECH".equalsIgnoreCase(Call_name)){
								RLOS.mLogger.info(" iNSIDE channelcode+ ");
								String squeryBlacklist="select BlacklistFlag,BlacklistDate,BlacklistReasonCode from ng_rlos_cif_detail where cif_wi_name='"+formObject.getWFWorkitemName()+"' and cif_searchType = 'External'";
								List<List<String>> Blacklist=formObject.getDataFromDataSource(squeryBlacklist);
								String External_blacklist =  "";
								String External_blacklist_date =  "";
								String External_blacklist_code =  "";

								if (Blacklist!=null && !Blacklist.isEmpty())
								{		
									External_blacklist =  Blacklist.get(0).get(0);
									External_blacklist_date =  Blacklist.get(0).get(1);
									External_blacklist_code =  Blacklist.get(0).get(2);
								}
								String xml_str =  int_xml.get(parent_tag);
								if("external_blacklist_flag".equalsIgnoreCase(tag_name)){
									xml_str = xml_str+ "<"+tag_name+">"+External_blacklist+"</"+ tag_name+">";
								}
								else if("external_blacklist_date".equalsIgnoreCase(tag_name)){
									xml_str = xml_str+ "<"+tag_name+">"+External_blacklist_date+"</"+ tag_name+">";
								}
								else if("external_blacklist_code".equalsIgnoreCase(tag_name)){
									xml_str = xml_str+ "<"+tag_name+">"+External_blacklist_code+"</"+ tag_name+">";
								}

								RLOS.mLogger.info(" after adding internal_blacklist+ "+xml_str);
								int_xml.put(parent_tag, xml_str);


							}
							
							else if(("auth_sig_sole_emp".equalsIgnoreCase(tag_name)||"shareholding_perc".equalsIgnoreCase(tag_name)) && "DECTECH".equalsIgnoreCase(Call_name) && "Self Employed".equalsIgnoreCase(emp_type)){
								RLOS.mLogger.info(" iNSIDE channelcode+ ");
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
								

								RLOS.mLogger.info(" after adding shareholding_perc+ "+xml_str);
								int_xml.put(parent_tag, xml_str);


							}
							
							else if("ApplicationDetails".equalsIgnoreCase(tag_name) && ("DECTECH".equalsIgnoreCase(Call_name))){
								RLOS.mLogger.info("inside DECTECH req1");

								RLOS.mLogger.info("inside customer update req2");
								String xml_str = int_xml.get(parent_tag);
								RLOS.mLogger.info(" before adding product+ "+xml_str);
								xml_str = xml_str +RLOS_Integration_Output.getProduct_details();
								RLOS.mLogger.info(" after adding product+ "+xml_str);
								int_xml.put(parent_tag, xml_str);

							}

							


							
							/*else if(tag_name.equalsIgnoreCase("resident_flag") && Call_name.equalsIgnoreCase("DECTECH")){
								if(int_xml.containsKey(parent_tag))
								{
									String resident_flag=formObject.getNGValue("cmplx_Customer_ResidentNonResident");

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

									RLOS.mLogger.info(" after adding confirmedinjob+ "+xml_str);
									int_xml.put(parent_tag, xml_str);

								}		                            	
							}*/
							else if("cust_name".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name)){
								if(int_xml.containsKey(parent_tag))
								{
									String first_name=formObject.getNGValue("cmplx_Customer_FIrstNAme");
									String middle_name=formObject.getNGValue("cmplx_Customer_MiddleName");
									String last_name=formObject.getNGValue("cmplx_Customer_LAstNAme");

									String full_name=first_name+" "+middle_name+" "+last_name;

									String xml_str = int_xml.get(parent_tag);
									xml_str = xml_str + "<"+tag_name+">"+full_name
									+"</"+ tag_name+">";

									RLOS.mLogger.info(" after adding confirmedinjob+ "+xml_str);
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

										RLOS.mLogger.info(" after adding confirmedinjob+ "+xml_str);
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

										RLOS.mLogger.info(" after adding included_pl_aloc+ "+xml_str);
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

										RLOS.mLogger.info(" after adding cmplx_EmploymentDetails_IncInCC+ "+xml_str);
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

									RLOS.mLogger.info(" after adding cmplx_Customer_VIPFlag+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}		                            	
							}
							else if("standing_instruction".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name)){
								RLOS.mLogger.info(" iNSIDE standing_instruction+ ");
								String squerynoc="SELECT count(*) FROM ng_rlos_FinancialSummary_SiDtls WHERE WI_Name='"+formObject.getWFWorkitemName()+"'";
								List<List<String>> NOC=formObject.getDataFromDataSource(squerynoc);
								RLOS.mLogger.info(" after adding cmplx_Customer_VIPFlag+ "+squerynoc);
								RLOS.mLogger.info(" after adding cmplx_Customer_VIPFlag+ "+NOC);
								String standing_instruction="";
								standing_instruction=NOC.get(0).get(0);
								if (NOC!=null && !NOC.isEmpty())
								{
									String xml_str =  int_xml.get(parent_tag);
									if ("0".equalsIgnoreCase(standing_instruction)){
										standing_instruction="N";
									}
									else{
										standing_instruction="Y";
									}
									
									xml_str = xml_str+ "<"+tag_name+">"+standing_instruction
									+"</"+ tag_name+">";

									RLOS.mLogger.info(" after adding standing_instruction+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
							
								}
								
							}
							//Coding Done for avg_credit_turnover_6, avg_credit_turnover_3,avg_bal_3 and avg_bal_6 tag
							else if(("avg_credit_turnover_6".equalsIgnoreCase(tag_name)||"avg_credit_turnover_3".equalsIgnoreCase(tag_name)||"avg_bal_3".equalsIgnoreCase(tag_name)||"avg_bal_6".equalsIgnoreCase(tag_name) )&& "DECTECH".equalsIgnoreCase(Call_name)){
								if(int_xml.containsKey(parent_tag))
								{	
									double avg_credit_turnover6th=0;
									double avg_credit_turnover3rd=0;
									double avg_bal_3=0;
									double avg_bal_6=0;
									String avg_credit_turnover_freq=formObject.getNGValue("cmplx_IncomeDetails_AvgCredTurnoverFreq");
									String avg_bal_freq=formObject.getNGValue("cmplx_IncomeDetails_AvgBalFreq");
									String avg_credit_turnover=formObject.getNGValue("cmplx_IncomeDetails_AvgCredTurnover");
									String avg_bal=formObject.getNGValue("cmplx_IncomeDetails_AvgBal");
									if (avg_credit_turnover!=(null)&&(!"".equalsIgnoreCase(avg_credit_turnover))){
										avg_credit_turnover6th=Double.parseDouble(avg_credit_turnover);
										RLOS.mLogger.info("avg_credit_turnover6th value"+avg_credit_turnover6th);

										if ("Annually".equalsIgnoreCase(avg_credit_turnover_freq)){
											avg_credit_turnover6th=avg_credit_turnover6th/2;
											avg_credit_turnover3rd=avg_credit_turnover6th/2;
										}
										else if ("Half Yearly".equalsIgnoreCase(avg_credit_turnover_freq)){
											//avg_credit_turnover6th=avg_credit_turnover6th;
											avg_credit_turnover3rd=avg_credit_turnover6th/2;
										}
										else if ("Quaterly".equalsIgnoreCase(avg_credit_turnover_freq)){
											avg_credit_turnover6th=2*avg_credit_turnover6th;
											avg_credit_turnover3rd=avg_credit_turnover6th/2;
										}
										else if ("monthly".equalsIgnoreCase(avg_credit_turnover_freq)){
											avg_credit_turnover6th=6*avg_credit_turnover6th;
											avg_credit_turnover3rd=avg_credit_turnover6th/2;
										}
									}
									if (avg_bal!=(null)&&(!"".equalsIgnoreCase(avg_bal))){
										//avg_bal_6=Double.parseDouble(avg_bal);
										RLOS.mLogger.info("avg_credit_turnover6th value"+avg_credit_turnover6th);

										avg_bal_6=Double.parseDouble(avg_bal);
										if ("Annually".equalsIgnoreCase(avg_bal_freq)){
											avg_bal_6=avg_bal_6/2;
											avg_bal_3=avg_bal_6/2;
										}
										else if ("Half Yearly".equalsIgnoreCase(avg_bal_freq)){
											//avg_bal_6=avg_bal_6;
											avg_bal_3=avg_bal_6/2;
										}
										else if ("Quaterly".equalsIgnoreCase(avg_bal_freq)){
											avg_bal_6=2*avg_bal_6;
											avg_bal_3=avg_bal_6/2;
										}
										else if ("monthly".equalsIgnoreCase(avg_bal_freq)){
											avg_bal_6=6*avg_bal_6;
											avg_bal_3=avg_bal_6/2;
										}

									}	

									String xml_str = int_xml.get(parent_tag);

									if ("avg_credit_turnover_6".equalsIgnoreCase(tag_name)){
										xml_str = xml_str + "<"+tag_name+">"+avg_credit_turnover6th
										+"</"+ tag_name+">";
									}
									else if ("avg_credit_turnover_3".equalsIgnoreCase(tag_name)){
										xml_str = xml_str + "<"+tag_name+">"+avg_credit_turnover3rd
										+"</"+ tag_name+">";
									}
									else if ("avg_bal_3".equalsIgnoreCase(tag_name)){
										xml_str = xml_str + "<"+tag_name+">"+avg_bal_3
										+"</"+ tag_name+">";
									}
									else if ("avg_bal_6".equalsIgnoreCase(tag_name)){
										xml_str = xml_str + "<"+tag_name+">"+avg_bal_6
										+"</"+ tag_name+">";
									}
									RLOS.mLogger.info(" after adding cmplx_Customer_VIPFlag+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}		                            	
							}
							//Coding Done for avg_credit_turnover_6, avg_credit_turnover_3,avg_bal_3 and avg_bal_6 tag
							else if("aggregate_exposed".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name)){
								if(int_xml.containsKey(parent_tag))
								{
									String aeQuery = "select isnull(CreditLimit,0) as CreditLimit,'' as TotalOutstandingAmt,'' as TotalAmt, '' as TotalAmount,'' as TakeOverAmount FROM ng_RLOS_CUSTEXPOSE_CardDetails where Wi_Name='"+formObject.getWFWorkitemName()+"' union all select '',isnull(TotalOutstandingAmt,0),'','','' FROM ng_RLOS_CUSTEXPOSE_LoanDetails where Wi_Name='"+formObject.getWFWorkitemName()+"' union all select '','',isnull(TotalAmt,0),'','' FROM ng_rlos_cust_extexpo_LoanDetails where Wi_Name='"+formObject.getWFWorkitemName()+"' union all select '','','',isnull(TotalAmount,0),'' FROM ng_rlos_cust_extexpo_CardDetails where Wi_Name='"+formObject.getWFWorkitemName()+"' union all select '','','','',isnull(TakeOverAmount,0) FROM ng_rlos_gr_LiabilityAddition where LiabilityAddition_wiName='"+formObject.getWFWorkitemName()+"'";
									   
	                                    List<List<String>> aggregate_exposed = formObject.getDataFromDataSource(aeQuery);
	                                   
	                                    double CreditLimit;
	                                    double TotalOutstandingAmt;
	                                    double TotalAmount;
	                                    double TotalAmt;
	                                    double takeOverAmount=0.0f;//added by akshay on 25/9/17 as per point 2 of problem sheet
	                                    RLOS.mLogger.info("values");
	                                    CreditLimit=0.0f;
	                                    TotalOutstandingAmt=0.0f;
	                                    TotalAmount=0.0f;
	                                    TotalAmt=0.0f;
	                                    double Total;
	                                    RLOS.mLogger.info("values");
	                                    Total=0.0f;
	                                    if (aggregate_exposed!=null && !aggregate_exposed.isEmpty())
	                                    {        
	                                    for (int i = 0; i<aggregate_exposed.size();i++){
	                                        
	                                        
	                                        if(aggregate_exposed.get(i).get(0)!=null && !aggregate_exposed.get(i).get(0).isEmpty() &&  !"".equals(aggregate_exposed.get(i).get(0)) && !"null".equalsIgnoreCase(aggregate_exposed.get(i).get(0)) ){
	                                                CreditLimit = CreditLimit+  Float.parseFloat(aggregate_exposed.get(i).get(0));
	                                                RLOS.mLogger.info("CreditLimit list values"+CreditLimit);
	                                        }
	                                        if(aggregate_exposed.get(i).get(1)!=null && !aggregate_exposed.get(i).get(1).isEmpty() &&  !"".equals(aggregate_exposed.get(i).get(1)) && !"null".equalsIgnoreCase(aggregate_exposed.get(i).get(1)) ){
	                                                TotalOutstandingAmt =TotalOutstandingAmt + Float.parseFloat(aggregate_exposed.get(i).get(1));
	                                                RLOS.mLogger.info("TotalOutstandingAmt list values"+TotalOutstandingAmt);
	                                        }
	                                        if(aggregate_exposed.get(i).get(2)!=null && !aggregate_exposed.get(i).get(2).isEmpty() &&  !"".equals(aggregate_exposed.get(i).get(2)) && !"null".equalsIgnoreCase(aggregate_exposed.get(i).get(2)) ){
	                                                TotalAmount =TotalAmount+  Float.parseFloat(aggregate_exposed.get(i).get(2));
	                                                RLOS.mLogger.info("TotalAmount list values"+TotalAmount);
	                                        }
	                                        if(aggregate_exposed.get(i).get(3)!=null && !aggregate_exposed.get(i).get(3).isEmpty() &&  !"".equals(aggregate_exposed.get(i).get(3)) && !"null".equalsIgnoreCase(aggregate_exposed.get(i).get(3)) ){
	                                                TotalAmt = TotalAmt+ Float.parseFloat(aggregate_exposed.get(i).get(3));
	                                                RLOS.mLogger.info("TotalAmt list values"+TotalAmt);
	                                        }
	                                        //added by akshay on 25/9/17 as per point 2 of problem sheet
	                                        if(aggregate_exposed.get(i).get(4)!=null && !aggregate_exposed.get(i).get(4).isEmpty() &&  !"".equals(aggregate_exposed.get(i).get(4)) && !"null".equalsIgnoreCase(aggregate_exposed.get(i).get(4)) ){
	                                            takeOverAmount = takeOverAmount+ Float.parseFloat(aggregate_exposed.get(i).get(4));
	                                            RLOS.mLogger.info("TotalAmt list values"+TotalAmt);
	                                    }
	                                        //ended by akshay on 25/9/17 as per point 2 of problem sheet
	                                    }
	                                    }
	                                    Total=    CreditLimit+TotalOutstandingAmt+TotalAmount+TotalAmt+takeOverAmount;
	                                    BigDecimal Tot=BigDecimal.valueOf(CreditLimit+TotalOutstandingAmt+TotalAmount+TotalAmt);
	                                    RLOS.mLogger.info("values"+Total);
	                                    RLOS.mLogger.info("values"+Tot);
	                                    
	                                    String conv_String=rlosCommon.putComma(Tot.toString());//added by akshay on 25/9/17 as per point 3 of problem sheet
	                                    formObject.setNGValue("cmplx_Liability_New_AggrExposure", conv_String);//changed by akshay on 25/9/17 as per point 2 of problem sheet
	                                    String xml_str = int_xml.get(parent_tag);
	                                    xml_str = xml_str + "<"+tag_name+">"+Tot+"</"+ tag_name+">";
	 
	                                    RLOS.mLogger.info(" after adding aggregate_exposed+ "+xml_str);
	                                    int_xml.put(parent_tag, xml_str);
	                                }                                        
	                            }

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

									RLOS.mLogger.info(" after adding confirmedinjob+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}	
								}
							}
							else if("AccountDetails".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name)){
								String squeryLoan="select count(*) from ng_rlos_custexpose_loandetails where Wi_Name='"+formObject.getWFWorkitemName()+"'";
								List<List<String>> LoanCount=formObject.getDataFromDataSource(squeryLoan);
								String squeryCard="select count(*) from ng_rlos_custexpose_Carddetails where Wi_Name='"+formObject.getWFWorkitemName()+"'";
								List<List<String>> CardCount=formObject.getDataFromDataSource(squeryCard);
								if ((!"0".equalsIgnoreCase(LoanCount.get(0).get(0)))&&(!"0".equalsIgnoreCase(CardCount.get(0).get(0)))){
									RLOS.mLogger.info(" has internal liability+ ");	
								if(int_xml.containsKey(parent_tag))
								{
									String xml_str = int_xml.get(parent_tag);
									RLOS.mLogger.info(" before adding internal liability+ "+xml_str);
									xml_str = xml_str + rlosCommon.getInternalLiabDetails();
									RLOS.mLogger.info(" after internal liability+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}
								}
							}
							else if("InternalBureau".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name)){

								String xml_str = int_xml.get(parent_tag);
								RLOS.mLogger.info(" before adding InternalBureauData+ "+xml_str);
								String temp = InternalBureauData();
								if(!"".equalsIgnoreCase(temp)){
									xml_str =  temp;
									RLOS.mLogger.info(" after InternalBureauData+ "+xml_str);
									int_xml.get(parent_tag);
									int_xml.put(parent_tag, xml_str);
								}


							}
							else if("InternalBouncedCheques".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name)){

								String xml_str = int_xml.get(parent_tag);
								RLOS.mLogger.info(" before adding InternalBouncedCheques+ "+xml_str);
								String temp = InternalBouncedCheques();
								if(!"".equalsIgnoreCase(temp)){
									if (xml_str==null){
										RLOS.mLogger.info(" before adding bhrabc"+xml_str);
										xml_str="";
									}
									xml_str = xml_str + temp;
									RLOS.mLogger.info(" after InternalBouncedCheques+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}

							}
							else if("InternalBureauIndividualProducts".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name)){

								String xml_str = int_xml.get(parent_tag);
								RLOS.mLogger.info(" before adding InternalBureauIndividualProducts+ "+xml_str);
								String temp =InternalBureauIndividualProducts();
								if(!"".equalsIgnoreCase(temp)){
									if (xml_str==null){
										RLOS.mLogger.info(" before adding bhrabc"+xml_str);
										xml_str="";
									}
									xml_str = xml_str + temp;
									RLOS.mLogger.info(" after InternalBureauIndividualProducts+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}

							}
							else if("InternalBureauPipelineProducts".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name)){

								String xml_str = int_xml.get(parent_tag);
								RLOS.mLogger.info(" before adding InternalBureauPipelineProducts+ "+xml_str);
								String temp = InternalBureauPipelineProducts();
								if(!"".equalsIgnoreCase(temp)){
									if (xml_str==null){
										RLOS.mLogger.info(" before adding bhrabc"+xml_str);
										xml_str="";
									}
									xml_str = xml_str + temp;
									RLOS.mLogger.info(" after InternalBureauPipelineProducts+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}

							}
							else if("ExternalBureau".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name)){

								String xml_str = int_xml.get(parent_tag);
								RLOS.mLogger.info(" before adding ExternalBureau+ "+xml_str);
								String temp = ExternalBureauData();
								if(!"".equalsIgnoreCase(temp)){
									xml_str =  temp;
									RLOS.mLogger.info(" after ExternalBureau+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}

							}
							else if("ExternalBouncedCheques".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name)){

								String xml_str = int_xml.get(parent_tag);
								RLOS.mLogger.info(" before adding ExternalBouncedCheques+ "+xml_str);
								String temp = ExternalBouncedCheques();
								if(!"".equalsIgnoreCase(temp)){
									if (xml_str==null){
										RLOS.mLogger.info(" before adding bhrabc"+xml_str);
										xml_str="";
									}
									xml_str =  xml_str + temp;
									RLOS.mLogger.info(" after ExternalBouncedCheques+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}                    	
							}
							else if("ExternalBureauIndividualProducts".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name)){

								String xml_str = int_xml.get(parent_tag);
								RLOS.mLogger.info(" before adding ExternalBureauIndividualProducts+ "+xml_str);
								String temp =  ExternalBureauIndividualProducts();
								String Manual_add_Liab = ExternalBureauManualAddIndividualProducts();

								if((!"".equalsIgnoreCase(temp)) || (!"".equalsIgnoreCase(Manual_add_Liab))){
									if (xml_str==null){
										RLOS.mLogger.info(" before adding bhrabc"+xml_str);
										xml_str="";
									}
									xml_str =  xml_str + temp + Manual_add_Liab;
									RLOS.mLogger.info(" after ExternalBureauIndividualProducts+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}	                            	
							}
							else if("ExternalBureauPipelineProducts".equalsIgnoreCase(tag_name) && "DECTECH".equalsIgnoreCase(Call_name)){

								String xml_str = int_xml.get(parent_tag);
								RLOS.mLogger.info(" before adding ExternalBureauPipelineProducts+ "+xml_str);
								String temp =  ExternalBureauPipelineProducts();
								if(!"".equalsIgnoreCase(temp)){
									if (xml_str==null){
										RLOS.mLogger.info(" before adding bhrabc"+xml_str);
										xml_str="";
									}
									xml_str =  xml_str + temp;
									RLOS.mLogger.info(" after ExternalBureauPipelineProducts+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}                        	
							}
							
							else if("ServiceId".equalsIgnoreCase(tag_name) && "EID_Genuine".equalsIgnoreCase(Call_name) && "SecurityDataInfo".equalsIgnoreCase(parent_tag))
							{
								String ServiceId ="";
								Timestamp timestamp = new Timestamp(System.currentTimeMillis());
								ServiceId = Long.toString(timestamp.getTime());
								String xml_str = "<"+tag_name+">"+ServiceId.substring(1,ServiceId.length())
								+"</"+ tag_name+">";

								RLOS.mLogger.info(" after adding res_flag+ "+xml_str);
								int_xml.put(parent_tag, xml_str);
							}

							/*else if(tag_name.equalsIgnoreCase("NonResidentFlag") && Call_name.equalsIgnoreCase("NEW_CUSTOMER_REQ") && parent_tag.equalsIgnoreCase("PersonDetails")){
								if(int_xml.containsKey(parent_tag))
								{
									String xml_str = int_xml.get(parent_tag);
									String res_flag ="N";

									if(formObject.getNGValue("cmplx_Customer_ResidentNonResident").equalsIgnoreCase("Resident")){
										res_flag="Y";
									}

									xml_str = xml_str + "<"+tag_name+">"+res_flag
									+"</"+ tag_name+">";

									RLOS.mLogger.info(" after adding res_flag+ "+xml_str);
									int_xml.put(parent_tag, xml_str);
								}		                            	
							}*/
							else if (parent_tag!=null && !"".equalsIgnoreCase(parent_tag))
							{
								RLOS.mLogger.info("inside 1st if");
								if(int_xml.containsKey(parent_tag))
								{
									RLOS.mLogger.info("inside 2nd if");
									String xml_str = int_xml.get(parent_tag);
									RLOS.mLogger.info("inside 2nd if xml string"+xml_str);
									if("Y".equalsIgnoreCase(is_repetitive) && int_xml.containsKey(tag_name)){
										RLOS.mLogger.info("inside 3rd if xml string");
										xml_str = int_xml.get(tag_name)+ "</"+tag_name+">" +"<"+ tag_name+">";
										RLOS.mLogger.info("value after adding "+ Call_name+": "+xml_str);
										RLOS.mLogger.info("inside 3rd if xml string xml string"+xml_str);
										int_xml.remove(tag_name);
										int_xml.put(tag_name, xml_str);
										RLOS.mLogger.info("inside 3rd if xml string xml string int_xml");
									}
									else{
										RLOS.mLogger.info("value after adding "+ Call_name+": "+xml_str+" form_control name: "+form_control);
										RLOS.mLogger.info("valuie of form control: "+formObject.getNGValue(form_control));
										if("".equalsIgnoreCase(form_control) && "".equalsIgnoreCase(default_val)){
											
											xml_str = xml_str + "<"+tag_name+">"+"</"+ tag_name+">";
											RLOS.mLogger.info("xml_str"+xml_str);
										}
										else if (!(formObject.getNGValue(form_control)==null || "".equalsIgnoreCase(formObject.getNGValue(form_control))||  "null".equalsIgnoreCase(formObject.getNGValue(form_control))))
										{
											RLOS.mLogger.info("form_control_val"+ form_control_val);
											if(fin_call_name.toUpperCase().contains(callName.toUpperCase())){
												form_control_val = formObject.getNGValue(form_control).toUpperCase();
											}
											else
												form_control_val = formObject.getNGValue(form_control);

											//added by Akshay on 3/7/2017 to remove commas from numbers
											if(form_control_val.contains(","))
											{
												form_control_val = form_control_val.replaceAll(",","");
											}
											//ended by Akshay 3/7/2017 to remove commas from numbers

											if(!"text".equalsIgnoreCase(data_format12)){
												String[] format_arr = data_format12.split(":");
												String format_name = format_arr[0];
												String format_type = format_arr[1];
												RLOS.mLogger.info("format_name"+format_name);
												RLOS.mLogger.info("format_type"+format_type);

												if("date".equalsIgnoreCase(format_name)){
													DateFormat df = new SimpleDateFormat("dd/MM/yyyy"); 
													DateFormat df_new = new SimpleDateFormat(format_type);

													try {
														startDate = df.parse(form_control_val);
														form_control_val = df_new.format(startDate);
														RLOS.mLogger.info(" date conversion: final Output: "+form_control_val+ " requested format: "+format_type);

													} catch (ParseException e) {
														RLOS.mLogger.info(" Error while format conversion: "+e.getMessage());
														RLOS.logException(e);//pgarg
													}
													catch (Exception e) {
														RLOS.mLogger.info(" Error while format conversion: "+e.getMessage());
														RLOS.logException(e);//pgarg
													}
												}
												else if("number".equalsIgnoreCase(format_name) && form_control_val.contains(","))
												{
													form_control_val = form_control_val.replace(",", "");													
												}
												//change here for other input format

											}
											RLOS.mLogger.info("form_control_val"+ form_control_val);
											xml_str = xml_str + "<"+tag_name+">"+form_control_val
											+"</"+ tag_name+">";
											RLOS.mLogger.info("xml_str"+ xml_str);
										}

										else if(default_val==null || "".equalsIgnoreCase(default_val)){
											RLOS.mLogger.info("no value found for tag name: "+ tag_name);
										}
										
										//added by akshay for World Check at initiation
										else if("CUSTOMER_SEARCH_REQUEST".equalsIgnoreCase(Call_name)&&("PhoneValue".equalsIgnoreCase(tag_name) && "PhoneFax".equalsIgnoreCase(parent_tag) && "".equalsIgnoreCase(form_control_val))){
											if(xml_str.contains("</PhoneFax>")){
												xml_str = xml_str.substring(0, xml_str.lastIndexOf("</PhoneFax>"));
												int_xml.put(parent_tag, xml_str);
											}
											else
												int_xml.remove(parent_tag);
										}
										//ended by akshay for World Check at initiation

										
										else{
											
											if(fin_call_name.toUpperCase().contains(callName.toUpperCase())){
												form_control_val = default_val.toUpperCase();
											}
											else
												form_control_val = default_val;

											RLOS.mLogger.info("#RLOS Common GenerateXML inside set default value form_control_val"+ form_control_val);
											xml_str = xml_str + "<"+tag_name+">"+form_control_val
											+"</"+ tag_name+">";
											RLOS.mLogger.info("#RLOS Common GenerateXML inside else of parent tag form_control_val xml_str1 xml_str"+ xml_str);

										}
										//code change for to remove docdect incase ref no is not present start	                                       
										if("DocRefNum".equalsIgnoreCase(tag_name) && "DocDetails".equalsIgnoreCase(parent_tag) && "".equalsIgnoreCase(form_control_val)){
											if(xml_str.contains("</DocDetails>")){
												xml_str = xml_str.substring(0, xml_str.lastIndexOf("</DocDetails>"));
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
										RLOS.mLogger.info("RLOSCommon"+"inside else"+xml_str);

									}

								}
								else{
									String new_xml_str ="";
									RLOS.mLogger.info("value after adding "+ Call_name+": "+new_xml_str+" form_control name: "+form_control);
									RLOS.mLogger.info("valuie of form control: "+formObject.getNGValue(form_control));
									if (!(formObject.getNGValue(form_control)==null || "".equalsIgnoreCase(formObject.getNGValue(form_control))||  "null".equalsIgnoreCase(formObject.getNGValue(form_control)))){
										RLOS.mLogger.info("form_control_val"+ form_control_val);
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
													RLOS.mLogger.info(" date conversion: final Output: "+form_control_val+ " requested format: "+format_type);

												} catch (ParseException e) {
													RLOS.mLogger.info(" Error while format conversion: "+e.getMessage());
													RLOS.logException(e);//pgarg
												}
												catch (Exception e) {
													RLOS.mLogger.info(" Error while format conversion: "+e.getMessage());
													RLOS.logException(e);//pgarg
												}
											}
											else if("number".equalsIgnoreCase(format_name) && form_control_val.contains(","))
											{												
												form_control_val = form_control_val.replace(",", "");												
											}
											//change here for other input format

										}
										RLOS.mLogger.info("form_control_val"+ form_control_val);
										new_xml_str = new_xml_str + "<"+tag_name+">"+form_control_val
										+"</"+ tag_name+">";
										RLOS.mLogger.info("new_xml_str"+ new_xml_str);
									}

									else if(default_val==null || "".equalsIgnoreCase(default_val)){
										if(int_xml.containsKey(parent_tag)|| "Y".equalsIgnoreCase(is_repetitive)){
											new_xml_str = new_xml_str + "<"+tag_name+">"+"</"+ tag_name+">";
										}
										RLOS.mLogger.info("#RLOS Common GenerateXML Inside Else Part no value found for tag name: "+ tag_name);
									}
									else{
										
										if(fin_call_name.toUpperCase().contains(callName.toUpperCase())){
											form_control_val = default_val.toUpperCase();
										}
										else
											form_control_val = default_val;
										RLOS.mLogger.info("#RLOS Common GenerateXML inside set default value form_control_val"+ form_control_val);
										new_xml_str = new_xml_str + "<"+tag_name+">"+form_control_val
										+"</"+ tag_name+">";
										RLOS.mLogger.info("#RLOS Common GenerateXML inside else of parent tag form_control_val xml_str1 xml_str"+ new_xml_str);

									}
									int_xml.put(parent_tag, new_xml_str);
									RLOS.mLogger.info("else of generatexml RLOSCommon"+"inside else"+new_xml_str);

								}
							}

						}


						final_xml=final_xml.append("<").append(parentTagName).append(">");
						RLOS.mLogger.info("Final XMLold--"+final_xml);

						Iterator<Map.Entry<String,String>> itr = int_xml.entrySet().iterator();
						RLOS.mLogger.info("itr"+itr);
						while (itr.hasNext())
						{
							Map.Entry<String, String> entry =  itr.next();
							RLOS.mLogger.info("entry of hashmap entry"+entry);
							if(final_xml.indexOf((entry.getKey()))>-1){
								RLOS.mLogger.info("itr_value: Key: "+ entry.getKey()+" Value: "+entry.getValue());
								final_xml = final_xml.insert(final_xml.indexOf("<"+entry.getKey()+">")+entry.getKey().length()+2, entry.getValue());
								RLOS.mLogger.info("value of final xml "+final_xml);
								itr.remove();
							}

						}    
						final_xml=final_xml.append("</").append(parentTagName).append(">");

						RLOS.mLogger.info("FInal XMLnew is: "+ final_xml.toString());
						final_xml.insert(0, header);
						final_xml.append(footer);
						RLOS.mLogger.info("FInal XMLnew with header: "+ final_xml.toString());
						formObject.setNGValue("Is_"+callName,"Y");
						RLOS.mLogger.info("value of "+callName+" Flag: "+formObject.getNGValue("Is_"+callName));

						
						cabinetName = FormContext.getCurrentInstance().getFormConfig().getConfigElement("EngineName");
						RLOS.mLogger.info("cabinetName "+cabinetName);
						wi_name = FormContext.getCurrentInstance().getFormConfig().getConfigElement("ProcessInstanceId");
						ws_name = FormContext.getCurrentInstance().getFormConfig().getConfigElement("ActivityName");
						RLOS.mLogger.info("ActivityName "+ws_name);
						sessionID = FormContext.getCurrentInstance().getFormConfig().getConfigElement("DMSSessionId");
						userName = FormContext.getCurrentInstance().getFormConfig().getConfigElement("UserName");
						RLOS.mLogger.info("userName "+userName);
						RLOS.mLogger.info("sessionID "+sessionID);



						String sMQuery = "SELECT SocketServerIP,SocketServerPort FROM NG_RLOS_MQ_TABLE with (nolock)";
						List<List<String>> outputMQXML=formObject.getDataFromDataSource(sMQuery);
						RLOS.mLogger.info("sMQuery "+sMQuery);
						if(!outputMQXML.isEmpty()){
							RLOS.mLogger.info(outputMQXML.get(0).get(0)+","+outputMQXML.get(0).get(1));
							socketServerIP =  outputMQXML.get(0).get(0);
							RLOS.mLogger.info("socketServerIP "+socketServerIP);
							socketServerPort = Integer.parseInt(outputMQXML.get(0).get(1));
							RLOS.mLogger.info("socketServerPort "+socketServerPort);
							if(!("".equalsIgnoreCase(socketServerIP) && socketServerPort ==null && socketServerPort ==0))
							{
								socket = new Socket(socketServerIP, socketServerPort);
								out = socket.getOutputStream();
								socketInputStream = socket.getInputStream();
								dout = new DataOutputStream(out);
								din = new DataInputStream(socketInputStream);
								mqOutputResponse="";	     
								mqInputRequest= getMQInputXML(sessionID,cabinetName,wi_name,ws_name,userName,final_xml);
								RLOS.mLogger.info("mqInputRequest "+mqInputRequest);

								if (mqInputRequest != null && mqInputRequest.length() > 0) 
								{
									int outPut_len = mqInputRequest.getBytes("UTF-16LE").length;
									RLOS.mLogger.info("Final XML output len: "+outPut_len);
									mqInputRequest = outPut_len+"##8##;"+mqInputRequest;
									RLOS.mLogger.info("MqInputRequest Input Request Bytes : "+mqInputRequest.getBytes("UTF-16LE"));
									dout.write(mqInputRequest.getBytes("UTF-16LE"));
									dout.flush();                
								}
								byte[] readBuffer = new byte[50000];
								int num = din.read(readBuffer);
								boolean wait_flag = true;
								int out_len=0;

								if (num > 0) 
								{
									while(wait_flag)
									{
										RLOS.mLogger.info("num :"+num);
										byte[] arrayBytes = new byte[num];
										System.arraycopy(readBuffer, 0, arrayBytes, 0, num);
										mqOutputResponse = mqOutputResponse + new String(arrayBytes, "UTF-16LE");
										RLOS.mLogger.info("inside loop output Response :\n"+mqOutputResponse);
										if(mqOutputResponse.contains("##8##;")){
											String[]	mqOutputResponse_arr = mqOutputResponse.split("##8##;");
											mqOutputResponse = mqOutputResponse_arr[1];
											out_len = Integer.parseInt(mqOutputResponse_arr[0]);
											RLOS.mLogger.info("First Output Response :\n"+mqOutputResponse);
											RLOS.mLogger.info("Output length :\n"+out_len);
										}
										if(out_len <= mqOutputResponse.getBytes("UTF-16LE").length){
											wait_flag=false;
										}
										Thread.sleep(100);
										num = din.read(readBuffer);

									}
									if(mqOutputResponse.contains("&lt;"))
									{
                                        RLOS.mLogger.info("inside for Dectech :\n"+mqOutputResponse);
                                        mqOutputResponse=mqOutputResponse.replaceAll("&lt;", "<");
                                        RLOS.mLogger.info("after replacing lt :\n"+mqOutputResponse);
                                        mqOutputResponse=mqOutputResponse.replaceAll("&gt;", ">");
                                        RLOS.mLogger.info("after replacing gt :\n"+mqOutputResponse);
                                  }


									RLOS.mLogger.info("Final Output Response :\n"+mqOutputResponse);
									socket.close();
									return mqOutputResponse;
								}


							}
							else{
								RLOS.mLogger.info("SocketServerIp and SocketServerPort is not maintained ");
								RLOS.mLogger.info("SocketServerIp is not maintained "+socketServerIP);
								RLOS.mLogger.info(" SocketServerPort is not maintained "+socketServerPort.toString());
								return "MQ details not maintained";
							}
						}
						else{
							RLOS.mLogger.info("SOcket details are not maintained in NG_RLOS_MQ_TABLE table");
							return "MQ details not maintained";
						}
					}

				}
				else {
					RLOS.mLogger.info("Genrate XML: Entry is not maintained in field mapping Master table for : "+callName);
					return "Call not maintained";
				}


			}
			else{
				RLOS.mLogger.info("Genrate XML: Entry is not maintained in Master table for : "+callName);
				return "Call not maintained";
			}
			
			socket.close();
		}		
		catch(Exception e){
			RLOS.mLogger.info("Generate XML: Exception ocurred: "+e.getLocalizedMessage());
			RLOS.mLogger.info("$$final_xml: "+final_xml);
			RLOS.mLogger.info("Generate XML:Exception occured in main thread: "+ RLOSCommon.printException(e));
			return "0";
		}    
		finally 
		{
			try
			{
				if(socket!=null)
					socket.close();
			}
			catch(Exception e)
			{
				RLOS.mLogger.info("Generate XML: Exception ocurred: "+e.getLocalizedMessage());
				RLOS.mLogger.info("$$final_xml: "+final_xml);
				RLOS.mLogger.info("Generate XML:Exception occured in main thread: "+ RLOSCommon.printException(e));				
			}			
		}
		return "";
	}	

	public static String getMQInputXML(String sessionID, String cabinetName, String wi_name, String ws_name, String userName, StringBuilder final_xml) 
	{
		FormContext.getCurrentInstance().getFormConfig( );

		StringBuilder strBuff=new StringBuilder();
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
		RLOS.mLogger.info("getMQInputXML"+strBuff.toString());
		return strBuff.toString();
	}

	public static String getTagValue(String xml, String tag)  
	{   
		//RLOS.mLogger.info("Tag:"+tag+" XML:"+xml);
		try
		{
			Document doc = getDocument(xml);
			if(doc !=null)
			{
				NodeList nodeList = doc.getElementsByTagName(tag);
		
				int length = nodeList.getLength();
				//RLOS.mLogger.info("NodeList Length: " + length);
		
				if (length > 0) {
					Node node =  nodeList.item(0);
					//RLOS.mLogger.info("Node : " + node);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						NodeList childNodes = node.getChildNodes();
						String value = "";
						int count = childNodes.getLength();
						for (int i = 0; i < count; i++) {
							Node item = childNodes.item(i);
							if (item.getNodeType() == Node.TEXT_NODE) {
								value += item.getNodeValue();
							}
						}
						return value;
					} else if (node.getNodeType() == Node.TEXT_NODE) {
						return node.getNodeValue();
					}
		
				}
			}
		}
		catch(Exception ex)
		{
			RLOS.mLogger.info("Exception occured in getTagValue method");
			RLOS.logException(ex);
		}
		return "";
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
			RLOS.logException(ex);
		}
		finally {
			RLOS.mLogger.info("Inside finally block of getDocument method");
			
		}
		return doc;
	}  


	public String InternalBureauData(){
		RLOS.mLogger.info( "inside InternalBureauData : ");
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
			String sQuery = "SELECT OutstandingAmt,OverdueAmt,CreditLimit FROM ng_RLOS_CUSTEXPOSE_CardDetails WHERE Wi_Name = '"+formObject.getWFWorkitemName()+"' AND Request_Type = 'InternalExposure'  union SELECT   TotalOutstandingAmt ,OverdueAmt,TotalLoanAmount FROM ng_RLOS_CUSTEXPOSE_LoanDetails   with (nolock) WHERE Wi_Name = '"+formObject.getWFWorkitemName()+"'";
			List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);			
			
			RLOS.mLogger.info( "values");
			float TotOutstandingAmt;
			float TotOverdueAmt;
		
			RLOS.mLogger.info( "values");
			TotOutstandingAmt=0.0f;
			TotOverdueAmt=0.0f;
	
			RLOS.mLogger.info( "values");
			for(int i = 0; i<OutputXML.size();i++){
				RLOS.mLogger.info( "values"+OutputXML.get(i).get(1));
				if(OutputXML.get(i).get(0)!=null && !OutputXML.get(i).get(1).isEmpty() &&  !"".equals(OutputXML.get(i).get(1)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(1)) )
				{
					RLOS.mLogger.info( "values."+TotOutstandingAmt+"..");
					TotOutstandingAmt = TotOutstandingAmt + Float.parseFloat(OutputXML.get(i).get(1));
				}
				if(OutputXML.get(i).get(1)!=null && !OutputXML.get(i).get(1).isEmpty() && !"".equals(OutputXML.get(i).get(2)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(2)) )
				{
					RLOS.mLogger.info( "values."+TotOutstandingAmt+"..");
					TotOverdueAmt = TotOverdueAmt + Float.parseFloat(OutputXML.get(i).get(2));
				}
				
			}
			add_xml_str = add_xml_str + "<total_out_bal>"+TotOutstandingAmt+"</total_out_bal>";
			add_xml_str = add_xml_str + "<total_overdue>"+TotOverdueAmt+"</total_overdue>";
			String sQueryDerived = "select NoOfContracts,Total_Exposure,WorstCurrentPaymentDelay,Worst_PaymentDelay_Last24Months,Nof_Records from NG_RLOS_CUSTEXPOSE_Derived where Request_Type='CollectionsSummary' and Wi_Name='"+formObject.getWFWorkitemName()+"'" ;
			List<List<String>> OutputXMLDerived = formObject.getDataFromDataSource(sQueryDerived);
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
			
			add_xml_str = add_xml_str + "<no_default_contract>"+NoOfContracts+"</no_default_contract>";

			add_xml_str = add_xml_str + "<total_exposure>"+Total_Exposure+"</total_exposure>";
			add_xml_str = add_xml_str + "<worst_curr_pay>"+WorstCurrentPaymentDelay+"</worst_curr_pay>"; // to be populated later
			add_xml_str = add_xml_str + "<worst_curr_pay_24>"+Worst_PaymentDelay_Last24Months+"</worst_curr_pay_24>"; // to be populated later
			add_xml_str = add_xml_str + "<no_of_rec>"+Nof_Records+"</no_of_rec>"; 
			String sQuerycheque = "SELECT 'DDS 3 months',count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-3,GETDATE()) and returntype='DDS' and Wi_Name='"+formObject.getWFWorkitemName()+"' union SELECT 'DDS 6 months',count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-6,GETDATE()) and returntype='DDS' and Wi_Name='"+formObject.getWFWorkitemName()+"' union SELECT 'ICCS 3 months',count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-3,GETDATE()) and returntype='ICCS' and Wi_Name='"+formObject.getWFWorkitemName()+"' union SELECT 'ICCS 6 months',count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-6,GETDATE()) and returntype='ICCS' and Wi_Name='"+formObject.getWFWorkitemName()+"'" ;
			List<List<String>> OutputXMLcheque = formObject.getDataFromDataSource(sQuerycheque);


			add_xml_str = add_xml_str + "<cheque_return_3mon>"+OutputXMLcheque.get(2).get(1)+"</cheque_return_3mon>";
			add_xml_str = add_xml_str + "<dds_return_3mon>"+OutputXMLcheque.get(0).get(1)+"</dds_return_3mon>";
			add_xml_str = add_xml_str + "<cheque_return_6mon>"+OutputXMLcheque.get(3).get(1)+"</cheque_return_6mon>";
			add_xml_str = add_xml_str + "<dds_return_6mon>"+OutputXMLcheque.get(1).get(1)+"</dds_return_6mon>";
			add_xml_str = add_xml_str + "<internal_charge_off>"+"N"+"</internal_charge_off><company_flag>N</company_flag></InternalBureau>";// to be populated later



			RLOS.mLogger.info( "Internal liab tag Cration: "+ add_xml_str);
			return add_xml_str;
		}
		catch(Exception e)
		{
			RLOS.mLogger.info( "Exception occurred in InternalBureauData()"+e.getMessage()+RLOSCommon.printException(e));
			return "";
		}

	}

	
	public String InternalBouncedCheques(){
		RLOS.mLogger.info( "inside InternalBouncedCheques : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "select CIFID,AcctId,returntype,returnNumber,returnAmount,retReasonCode,returnDate from ng_rlos_FinancialSummary_ReturnsDtls with (nolock) where Wi_Name = '"+formObject.getWFWorkitemName()+"'";
		
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		
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


			if(!"".equalsIgnoreCase(applicantID)){
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
		RLOS.mLogger.info( "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}

	public String InternalBureauIndividualProducts(){
		RLOS.mLogger.info( "inside InternalBureauIndividualProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "SELECT cifid,agreementid,loantype,loantype,custroletype,loan_start_date,loanmaturitydate,lastupdatedate ,totaloutstandingamt,totalloanamount,NextInstallmentAmt,paymentmode,totalnoofinstalments,remaininginstalments,totalloanamount,	overdueamt,nofdayspmtdelay,monthsonbook,currentlycurrent,currmaxutil,DPD_30_in_last_6_months,DPD_60_in_last_18_months,propertyvalue,loan_disbursal_date,marketingcode,DPD_30_in_last_3_months,DPD_30_in_last_6_months,DPD_30_in_last_9_months,DPD_30_in_last_12_months,DPD_30_in_last_18_months,DPD_30_in_last_24_months,DPD_60_in_last_3_months,DPD_60_in_last_6_months,DPD_60_in_last_9_months,DPD_60_in_last_12_months,DPD_60_in_last_18_months,DPD_60_in_last_24_months,DPD_90_in_last_3_months,DPD_90_in_last_6_months,DPD_90_in_last_9_months,DPD_90_in_last_12_months,DPD_90_in_last_18_months,DPD_90_in_last_24_months,DPD_120_in_last_3_months,DPD_120_in_last_6_months,DPD_120_in_last_9_months,DPD_120_in_last_12_months,DPD_120_in_last_18_months,DPD_120_in_last_24_months,DPD_150_in_last_3_months,DPD_150_in_last_6_months,DPD_150_in_last_9_months,DPD_150_in_last_12_months,DPD_150_in_last_18_months,DPD_150_in_last_24_months,DPD_180_in_last_3_months,DPD_180_in_last_6_months,DPD_180_in_last_9_months,DPD_180_in_last_12_months,DPD_180_in_last_24_months,'' as col1,Consider_For_Obligations,LoanStat,'LOANS',writeoffStat,writeoffstatdt,lastrepmtdt,limit_increase,PartSettlementDetails,'' as SchemeCardProduct,General_Status FROM ng_RLOS_CUSTEXPOSE_LoanDetails WHERE Wi_Name = '"+formObject.getWFWorkitemName()+"' and LoanStat !='Pipeline' union select CifId,CardEmbossNum,CardType,CardType,CustRoleType,'' as col6,'' as col7,'' as col8,OutstandingAmt,CreditLimit,PaymentsAmount,PaymentMode,'' as col13,'' as col14,CashLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,'' as col19,CurrMaxUtil,DPD_30_in_last_6_months,DPD_60_in_last_18_months,'' as col23,'' as col24,'' as col25,DPD_30_in_last_3_months,DPD_30_in_last_6_months,DPD_30_in_last_9_months,DPD_30_in_last_12_months,DPD_30_in_last_18_months,DPD_30_in_last_24_months,DPD_60_in_last_3_months,DPD_60_in_last_6_months,DPD_60_in_last_9_months,DPD_60_in_last_12_months,DPD_60_in_last_18_months,DPD_60_in_last_24_months,DPD_90_in_last_3_months,DPD_90_in_last_6_months,DPD_90_in_last_9_months,DPD_90_in_last_12_months,DPD_90_in_last_18_months,DPD_90_in_last_24_months,DPD_120_in_last_3_months,DPD_120_in_last_6_months,DPD_120_in_last_9_months,DPD_120_in_last_12_months,DPD_120_in_last_18_months,DPD_120_in_last_24_months,DPD_150_in_last_3_months,DPD_150_in_last_6_months,DPD_150_in_last_9_months,DPD_150_in_last_12_months,DPD_150_in_last_18_months,DPD_150_in_last_24_months,DPD_180_in_last_3_months,DPD_180_in_last_6_months,DPD_180_in_last_9_months,DPD_180_in_last_12_months,DPD_180_in_last_24_months,ExpiryDate,Consider_For_Obligations,CardStatus,'CARDS',writeoffStat,writeoffstatdt,'' as lastrepdate,limit_increase,'' as PartSettlementDetails,SchemeCardProd,General_Status FROM ng_RLOS_CUSTEXPOSE_CardDetails where 	Wi_Name = '"+formObject.getWFWorkitemName()+"' and Request_Type In ('InternalExposure','CollectionsSummary')";
		
		String CountQuery ="select count(*) from ng_RLOS_CUSTEXPOSE_CardDetails where Wi_Name = '"+formObject.getWFWorkitemName()+"' and cardStatus='A'";
		List<List<String>> CountXML = formObject.getDataFromDataSource(CountQuery);
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		
		String ReqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
		
		try
		{
			for (int i = 0; i<OutputXML.size();i++)
			{

			String cifId = "";
			String agreementId = "";
			String product_type = "";
			String contractType = ""; 
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
				String EmployerType=formObject.getNGValue("EMploymentDetails_Combo5");
				String Kompass=formObject.getNGValue("cmplx_EmploymentDetails_Kompass");
				RLOS.mLogger.info( "asdasdasd");

				if(!(OutputXML.get(i).get(0) == null || "".equals(OutputXML.get(i).get(0))) ){
					cifId = OutputXML.get(i).get(0);
				}
				if(!(OutputXML.get(i).get(1) == null || "".equals(OutputXML.get(i).get(1))) ){
					agreementId = OutputXML.get(i).get(1);
				}				
				if(!(OutputXML.get(i).get(2) == null || "".equals(OutputXML.get(i).get(2))) ){
					product_type = OutputXML.get(i).get(2);
					if ("Home In One".equalsIgnoreCase(product_type))
					{
						product_type="HIO";
					}
					else
					{
						product_type = OutputXML.get(i).get(63);
					}
				}
				if(!(OutputXML.get(i).get(3) == null || "".equals(OutputXML.get(i).get(3))) ){
					contractType = OutputXML.get(i).get(2);
				}
				if(!(OutputXML.get(i).get(4) == null || "".equals(OutputXML.get(i).get(4))) ){
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
					//RLOS.mLogger.info( "asdasdasd"+OutputXML.get(i).get(12));
					totalnoofinstalments = OutputXML.get(i).get(12);
				}
				if(!(OutputXML.get(i).get(13) == null || "".equals(OutputXML.get(i).get(13))) ){
					remaininginstalments = OutputXML.get(i).get(13);
				}				
				if(!(OutputXML.get(i).get(14) == null || "".equals(OutputXML.get(i).get(14))) ){
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
				if(!(OutputXML.get(i).get(58) == null || "".equals(OutputXML.get(i).get(58))) ){
					CardExpiryDate = OutputXML.get(i).get(58);
				}

				if(!(OutputXML.get(i).get(61) == null || "".equals(OutputXML.get(i).get(61))) )
				{
					Consider_For_Obligations = OutputXML.get(i).get(61);
					if ("false".equalsIgnoreCase(Consider_For_Obligations))
					{
						Consider_For_Obligations="Y";
					}
					else {
						Consider_For_Obligations="N";
					}
				}

				if(!(OutputXML.get(i).get(62) == null || "".equals(OutputXML.get(i).get(62))) )
				{
					phase = OutputXML.get(i).get(62);
					if (phase.startsWith("C")){
						phase="C";
					}
					else {
						phase="A";
					}
				}
				if(!(OutputXML.get(i).get(63) == null || "".equals(OutputXML.get(i).get(63))) ){
					writeoffStat = OutputXML.get(i).get(63);
				}
				if(!(OutputXML.get(i).get(64) == null || "".equals(OutputXML.get(i).get(64))) ){
					writeoffstatdt = OutputXML.get(i).get(64);
				}
				if(!(OutputXML.get(i).get(65) == null || "".equals(OutputXML.get(i).get(65))) ){
					lastrepmtdt = OutputXML.get(i).get(65);
				}
				if(!(OutputXML.get(i).get(66) == null || "".equals(OutputXML.get(i).get(66))) ){
					Limit_increase = OutputXML.get(i).get(66);
					if ("false".equalsIgnoreCase(Limit_increase))
					{
						Limit_increase="Y";
					}
					else{
						Limit_increase="N";
					}
				}
				if(!(OutputXML.get(i).get(67) == null || "".equals(OutputXML.get(i).get(67))) )
				{
					part_settlement_date = OutputXML.get(i).get(67);
					String abc=OutputXML.get(i).get(67);
					if (abc != null && "".equalsIgnoreCase(abc))
					{
						abc=abc.substring(0, 10)+"split"+abc.substring(10,abc.length() );
						String abcsa[]=abc.split("split");
						part_settlement_date = abcsa[0];
						part_settlement_amount = abcsa[1];
					}
				}
				if(!(OutputXML.get(i).get(68) == null || "".equals(OutputXML.get(i).get(68))) ){
					SchemeCardProduct = OutputXML.get(i).get(68);
				}
				if(!(OutputXML.get(i).get(69) == null || "".equals(OutputXML.get(i).get(69))) ){
					General_Status = OutputXML.get(i).get(69);
				}
				if(cifId!=null && !"".equalsIgnoreCase(cifId) && !"null".equalsIgnoreCase(cifId)){
					RLOS.mLogger.info( "asdasdasd");
					add_xml_str = add_xml_str + "<InternalBureauIndividualProducts><applicant_id>"+cifId+"</applicant_id>";
					add_xml_str = add_xml_str + "<internal_bureau_individual_products_id>"+agreementId+"</internal_bureau_individual_products_id>";
					add_xml_str = add_xml_str + "<type_product>"+product_type+"</type_product>";
					if ("cards".equalsIgnoreCase(OutputXML.get(i).get(63))){					
						add_xml_str = add_xml_str + "<contract_type>CC</contract_type>";
					}
					if ("Loans".equalsIgnoreCase(OutputXML.get(i).get(63)))
					{
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


					add_xml_str = add_xml_str + "<card_product>"+contractType+"</card_product>"; 
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
					add_xml_str = add_xml_str + "<combined_limit_flag>"+""+"</combined_limit_flag>"; 
					add_xml_str = add_xml_str + "<secured_card_flag>"+""+"</secured_card_flag>"; 
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
					add_xml_str = add_xml_str + "<mol_salary_variance>"+""+"</mol_salary_variance>";
					if(Kompass!=null){
						if("true".equalsIgnoreCase(Kompass)){
							add_xml_str = add_xml_str + "<kompass>"+"Y"+"</kompass>";
						}
						else{
							add_xml_str = add_xml_str + "<kompass>"+"N"+"</kompass>";
						}
					}
					add_xml_str = add_xml_str + "<employer_type>"+EmployerType+"</employer_type>"; 


					if ("Credit Card".equalsIgnoreCase(ReqProd)){

						add_xml_str = add_xml_str + "<no_of_paid_installment>"+remaininginstalments+"</no_of_paid_installment><company_flag>N</company_flag></InternalBureauIndividualProducts>";
					}
					else{
						add_xml_str = add_xml_str + "<no_of_paid_installment>"+remaininginstalments+"</no_of_paid_installment><company_flag>N</company_flag><type_of_od>"+""+"</type_of_od><amt_paid_last6mnths>"+""+"</amt_paid_last6mnths></InternalBureauIndividualProducts>";

					}
				}	

			}
		}
		catch(Exception e){
			RLOS.mLogger.info( "Internal liab tag Cration: "+ RLOSCommon.printException(e));
		}
		RLOS.mLogger.info( "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}

	public String InternalBureauPipelineProducts(){
		RLOS.mLogger.info( "inside InternalBureauPipelineProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "SELECT cifid,product_type,custroletype,lastupdatedate,totalamount,totalnoofinstalments,totalloanamount,agreementId FROM ng_RLOS_CUSTEXPOSE_LoanDetails  with (nolock) where Wi_Name = '"+formObject.getWFWorkitemName()+"' and  LoanStat = 'Pipeline'";		
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		
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
			if(!(OutputXML.get(i).get(2) == null || "".equals(OutputXML.get(i).get(2))) ){
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
			if(!(OutputXML.get(i).get(7) == null || "".equals(OutputXML.get(i).get(7))) ){
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
		RLOS.mLogger.info( "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}

	public String ExternalBureauData(){
		RLOS.mLogger.info( "inside ExternalBureauData : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "select  CifId,fullnm,TotalOutstanding,TotalOverdue,NoOfContracts,Total_Exposure,WorstCurrentPaymentDelay,Worst_PaymentDelay_Last24Months,Worst_Status_Last24Months,Nof_Records,NoOf_Cheque_Return_Last3,Nof_DDES_Return_Last3Months,Nof_Cheque_Return_Last6,DPD30_Last6Months,Internal_WriteOff_Check from NG_rlos_custexpose_Derived where wi_name  = '"+formObject.getWFWorkitemName()+"' and Request_type= 'ExternalExposure'";
		
		String AecbHistQuery = "select isnull(max(AECBHistMonthCnt),0) as AECBHistMonthCnt from ( select MAX(AECBHistMonthCnt) as AECBHistMonthCnt  from ng_rlos_cust_extexpo_CardDetails where  wi_name  = '"+formObject.getWFWorkitemName()+"' union select Max(AECBHistMonthCnt) from ng_rlos_cust_extexpo_LoanDetails where wi_name  = '"+formObject.getWFWorkitemName()+"') as ext_expo";
		String  add_xml_str ="";
		try{
			List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
			
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
			if ("0".equalsIgnoreCase(AecbHistQueryData.get(0).get(0)))
			{
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

				RLOS.mLogger.info( "Inside If Internal liab tag Cration: "+ add_xml_str);
				return add_xml_str;
			}
			else
			{
				for (int i = 0; i<OutputXML.size();i++)
				{

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
						//RLOS.mLogger.info( "asdasdasd"+OutputXML.get(i).get(12));
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
				RLOS.mLogger.info( "Inside else Internal liab tag Cration: "+ add_xml_str);
				return add_xml_str;
			}

		}
		catch(Exception e)
		{
			RLOS.mLogger.info( "Exception occurred in externalBureauData()"+e.getMessage()+RLOSCommon.printException(e));
			return null;
		}
	}

	public String ExternalBouncedCheques(){
		RLOS.mLogger.info( "inside ExternalBouncedCheques : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "SELECT cifid,number,amount,reasoncode,returndate,providerno FROM ng_rlos_cust_extexpo_ChequeDetails  with (nolock) where Wi_Name = '"+formObject.getWFWorkitemName()+"' and Request_Type = 'ExternalExposure'";
		
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		
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
		RLOS.mLogger.info( "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}

	public String ExternalBureauIndividualProducts(){
		RLOS.mLogger.info( "inside ExternalBureauIndividualProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "select CifId,AgreementId,LoanType,ProviderNo,LoanStat,CustRoleType,LoanApprovedDate,LoanMaturityDate,OutstandingAmt,TotalAmt,PaymentsAmt,TotalNoOfInstalments,RemainingInstalments,WriteoffStat,WriteoffStatDt,CreditLimit,OverdueAmt,NofDaysPmtDelay,MonthsOnBook,lastrepmtdt,IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last18Months,AECBHistMonthCnt,DPD5_Last3Months,'' as qc_Amnt,'' as Qc_emi,'' as Cac_indicator from ng_rlos_cust_extexpo_LoanDetails where wi_name= '"+formObject.getWFWorkitemName()+"'  and LoanStat != 'Pipeline' union select CifId,CardEmbossNum,CardType,ProviderNo,CardStatus,CustRoleType,StartDate,ClosedDate,CurrentBalance,'' as col6,PaymentsAmount,NoOfInstallments,'' as col5,WriteoffStat,WriteoffStatDt,CashLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,lastrepmtdt,IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last18Months,AECBHistMonthCnt,DPD5_Last3Months,qc_amt,qc_emi,CAC_Indicator from ng_rlos_cust_extexpo_CardDetails where wi_name  =  '"+formObject.getWFWorkitemName()+"' and cardstatus != 'Pipeline' ";
		
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		
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


			if(!(OutputXML.get(i).get(1) == null || "".equals(OutputXML.get(i).get(1))) ){
				AgreementId = OutputXML.get(i).get(1);
			}				
			if(!(OutputXML.get(i).get(2) == null || "".equals(OutputXML.get(i).get(2))) ){
				ContractType = OutputXML.get(i).get(2);
				try{
				String cardquery = "select code from ng_master_contract_type where description='"+ContractType+"'";
				
				List<List<String>> cardqueryXML = formObject.getDataFromDataSource(cardquery);
				ContractType=cardqueryXML.get(0).get(0);
				RLOS.mLogger.info( "ContractType");
				}
				catch(Exception e){
					RLOS.mLogger.info( "Exception");
					RLOS.logException(e);
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
			if(!(OutputXML.get(i).get(7) == null || "".equals(OutputXML.get(i).get(7))) ){
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
			add_xml_str = add_xml_str + "<qc_emi>"+QC_Amt+"</qc_emi>";
			if ("Credit Card".equalsIgnoreCase(ReqProd)){
				add_xml_str = add_xml_str + "<qc_amount>"+QC_emi+"</qc_amount><company_flag>N</company_flag><cac_bank_name></cac_bank_name></ExternalBureauIndividualProducts>";
			}
			else{
				add_xml_str = add_xml_str + "<qc_amount>"+QC_emi+"</qc_amount><company_flag>N</company_flag><cac_bank_name></cac_bank_name><take_over_indicator></take_over_indicator></ExternalBureauIndividualProducts>";
			}

		}
		RLOS.mLogger.info( "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}
	public String ExternalBureauManualAddIndividualProducts(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		
		int Man_liab_row_count = formObject.getLVWRowCount("cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
		String applicant_id = formObject.getNGValue("cmplx_Customer_CIFNO");
		String  add_xml_str ="";
		Date date = new Date();
		String modifiedDate= new SimpleDateFormat("dd/MM/yyyy").format(date);
		
		if (Man_liab_row_count !=0){
			for (int i = 0; i<Man_liab_row_count;i++){
				String Type_of_Contract = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 0); //0
				String LimitEMI = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 1); //0
				
				
				String cac_Indicator = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 4); //0
				String Qc_amt = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 5); //0
				String Qc_Emi = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 6); //0
				if ("true".equalsIgnoreCase(cac_Indicator)){
					cac_Indicator="Y";
				}
				else {
					cac_Indicator="N";
				}
				
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
				add_xml_str = add_xml_str + "<total_amount>"+LimitEMI+"</total_amount>";
				add_xml_str = add_xml_str + "<payments_amount>"+LimitEMI+"</payments_amount>";
				add_xml_str = add_xml_str + "<total_no_of_instalments></total_no_of_instalments>";
				add_xml_str = add_xml_str + "<no_of_remaining_instalments></no_of_remaining_instalments>";
				add_xml_str = add_xml_str + "<worst_status></worst_status>";
				add_xml_str = add_xml_str + "<worst_status_date></worst_status_date>";

				add_xml_str = add_xml_str + "<credit_limit>"+LimitEMI+"</credit_limit>";
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
				add_xml_str = add_xml_str + "<qc_emi>"+Qc_amt+"</qc_emi>";
				add_xml_str = add_xml_str + "<qc_amount>"+Qc_Emi+"</qc_amount><company_flag>N</company_flag></ExternalBureauIndividualProducts>";

			}

		}
		RLOS.mLogger.info( "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}

	public String ExternalBureauPipelineProducts(){
		RLOS.mLogger.info( "inside ExternalBureauPipelineProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "select AgreementId,ProviderNo,LoanType,LoanDesc,CustRoleType,Datelastupdated,Total_Amount,TotalNoOfInstalments,'' as col1,NoOfDaysInPipeline from ng_rlos_cust_extexpo_LoanDetails where wi_name  =  '"+formObject.getWFWorkitemName()+"' and LoanStat = 'Pipeline' union select CardEmbossNum,ProviderNo,CardTypeDesc,CardType,CustRoleType,LastUpdateDate,'' as col2,NoOfInstallments,TotalAmount,NoOfDaysInPipeLine  from ng_rlos_cust_extexpo_CardDetails where wi_name  =  '"+formObject.getWFWorkitemName()+"' and cardstatus = 'Pipeline'";
		
		String  add_xml_str = "";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		
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
			if(OutputXML.get(i).get(6)!=null && !OutputXML.get(i).get(6).isEmpty() &&  !"".equals(OutputXML.get(i).get(6)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(6)) ){
						TotAmt = OutputXML.get(i).get(6);
			}
			if(!(OutputXML.get(i).get(7) == null || "".equals(OutputXML.get(i).get(7))) ){
				noOfInstalmnt = OutputXML.get(i).get(7);
			}
			if(!(OutputXML.get(i).get(8) == null || "".equals(OutputXML.get(i).get(8))) ){
				creditLimit = OutputXML.get(i).get(8);
			}
			if(OutputXML.get(i).get(9)!=null && !OutputXML.get(i).get(9).isEmpty() &&  !"".equals(OutputXML.get(i).get(9)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(9)) ){
					noOfDayinPpl = OutputXML.get(i).get(9);
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

				add_xml_str = add_xml_str + "<ppl_no_of_days_in_pipeline>"+noOfDayinPpl+"</ppl_no_of_days_in_pipeline><company_flag>N</company_flag></ExternalBureauPipelineProducts>"; // to be populated later

			


		}
		RLOS.mLogger.info( "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}
	
}
