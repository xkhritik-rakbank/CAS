

/*------------------------------------------------------------------------------------------------------

                     NEWGEN SOFTWARE TECHNOLOGIES LIMITED

Group                                                             : Application -Projects

Project/Product                                                   : Rakbank  

Application                                                       : Credit Card

Module                                                            : Integration Output

File Name                                                         : CC_Integration_Output

Author                                                            : Disha 

Date (DD/MM/YYYY)                                                 : 

Description                                                       : 

-------------------------------------------------------------------------------------------------------

CHANGE HISTORY

-------------------------------------------------------------------------------------------------------

Problem No/CR No   Change Date   Changed By    Change Description

------------------------------------------------------------------------------------------------------*/






package com.newgen.omniforms.user;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.newgen.custom.XMLParser;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.util.Common;

import com.newgen.wfdesktop.xmlapi.WFCallBroker;

public class CC_Integration_Output extends Common implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static CC_Common CC_Comn = new CC_Common();
	
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : value set customer         

	 ***********************************************************************************  */
	public void valueSetCustomer(String outputResponse ,String operationName)
	{
		CreditCard.mLogger.info("RLOSCommon valueSetCustomer"+ "Inside valueSetCustomer():");
		String outputXMLHead = "";
		String outputXMLMsg = "";
		String returnDesc = "";
		String returnCode = "";
		String response= "";
		//String Operation_name="";
		String Call_type="";
		XMLParser objXMLParser = new XMLParser();
		try
		{
			if(outputResponse.indexOf("<EE_EAI_HEADER>")>-1)
			{
				outputXMLHead=outputResponse.substring(outputResponse.indexOf("<EE_EAI_HEADER>"),outputResponse.indexOf("</EE_EAI_HEADER>")+16);
				CreditCard.mLogger.info("RLOSCommon valueSetCustomer"+ "outputXMLHead");
			}
			objXMLParser.setInputXML(outputXMLHead);
			if(outputXMLHead.indexOf("<MsgFormat>")>-1)
			{
				response= outputXMLHead.substring(outputXMLHead.indexOf("<MsgFormat>")+11,outputXMLHead.indexOf("</MsgFormat>"));
				CreditCard.mLogger.info("$$response "+response);
			}
			if(outputXMLHead.indexOf("<ReturnDesc>")>-1)
			{
				returnDesc= outputXMLHead.substring(outputXMLHead.indexOf("<ReturnDesc>")+12,outputXMLHead.indexOf("</ReturnDesc>"));
				CreditCard.mLogger.info("$$returnDesc "+returnDesc);
			}
			if(outputXMLHead.indexOf("<ReturnCode>")>-1)
			{
				returnCode= outputXMLHead.substring(outputXMLHead.indexOf("<ReturnCode>")+12,outputXMLHead.indexOf("</ReturnCode>"));
				CreditCard.mLogger.info("$$returnCode "+returnCode);
			}

			if(outputResponse.indexOf("<MQ_RESPONSE_XML>")>-1)
			{
				outputXMLMsg=outputResponse.substring(outputResponse.indexOf("<MQ_RESPONSE_XML>")+17,outputResponse.indexOf("</MQ_RESPONSE_XML>"));
				//CreditCard.mLogger.info("$$outputXMLMsg "+outputXMLMsg);
				CreditCard.mLogger.info("$$outputXMLMsg getOutputXMLValues"+"check inside getOutputXMLValues");
				//getOutputXMLValues(outputXMLMsg,response);
				getOutputXMLValues(outputXMLMsg,response,operationName);
				CreditCard.mLogger.info("$$outputXMLMsg "+"outputXMLMsg");
			}
			//ended by me
			if(outputResponse.indexOf("<CallType>")>-1){
				Call_type= outputResponse.substring(outputResponse.indexOf("<CallType>")+10,outputResponse.indexOf("</CallType>"));
				if	(Call_type!= null){
					if ("CA".equalsIgnoreCase(Call_type)){
						//dectechfromliability(outputResponse);
					}
					if ("PM".equalsIgnoreCase(Call_type)){
						dectechfromeligbility(outputResponse);
					}
				}

			}
		}
		catch(Exception e)
		{            
			CreditCard.mLogger.info("Exception occured in valueSetCustomer method:  "+e.getMessage());
			
		}
	}
	
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : getoutput xml values       

	 ***********************************************************************************  */
	public static void getOutputXMLValues(String outputXMLMsg, String response,String Operation_name) throws ParserConfigurationException
	{
		String sQuery = "";
		String tagValue = "";
		try
		{   
			CreditCard.mLogger.info("inside getOutputXMLValues"+"inside outputxml");
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String col_name = "Form_Control,Parent_Tag_Name,XmlTag_Name,InDirect_Mapping,Grid_Mapping,Grid_Table,grid_table_xml_tags";

			//  sQuery = "SELECT "+col_name+" FROM NG_PL_Integration_Field_Response where Call_Name ='"+response+"'";
			//code added here
			if(!("".equalsIgnoreCase(Operation_name) || Operation_name!=null)){   
				CreditCard.mLogger.info("inside if of operation"+"operation111"+Operation_name);
				CreditCard.mLogger.info("inside if of operation"+"callName111"+col_name);
				sQuery = "SELECT "+col_name+" FROM NG_CC_Integration_Field_Response with (nolock) where Call_Name ='"+response+"' and Operation_name='"+Operation_name+"'" ;
				CreditCard.mLogger.info("RLOSCommon"+ "sQuery "+sQuery);
			}
			else{
				sQuery = "SELECT "+col_name+" FROM NG_CC_Integration_Field_Response with (nolock) where Call_Name ='"+response+"'";
			}

			//ended here
			List<List<String>> outputTableXML=formObject.getNGDataFromDataCache(sQuery);


			String[] col_name_arr = col_name.split(",");

			CreditCard.mLogger.info("$$outputTableXML"+outputTableXML.get(0).get(0)+outputTableXML.get(0).get(1)+outputTableXML.get(0).get(2)+outputTableXML.get(0).get(3));
			int n=outputTableXML.size();
			CreditCard.mLogger.info("outputTableXML size: " + n+"");

			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(outputXMLMsg)));
			CreditCard.mLogger.info("name is doc : "+ doc+"");
			NodeList nl = doc.getElementsByTagName("*");

			if( n> 0)
			{
				//new LinkedHashMap<String, String>();
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
					CreditCard.mLogger.info(" Grid_col_tag"+"Grid_col_tag"+Grid_col_tag);
					if (parent_tag!=null && !"".equalsIgnoreCase(parent_tag))
					{                    
						for (int i = 0; i < nl.getLength(); i++)
						{
							nl.item(i);
							if(nl.item(i).getNodeName().equalsIgnoreCase(fielddbxml_tag))
							{
								CreditCard.mLogger.info("RLOS Common# getOutputXMLValues()"+" fielddbxml_tag: "+fielddbxml_tag);
								String indirectMapping = (String) responseFileMap.get("InDirect_Mapping");
								String gridMapping = (String) responseFileMap.get("Grid_Mapping");

								if("Y".equalsIgnoreCase(gridMapping))
								{
									CreditCard.mLogger.info("Grid_col_tag_arr"+"inside indirect mapping");
									if(Grid_col_tag.contains(",")){
										String Grid_col_tag_arr[]  =Grid_col_tag.split(",");
										//String grid_detail_str = nl.item(i).getNodeValue();
										NodeList childnode  = nl.item(i).getChildNodes();
										CreditCard.mLogger.info("Grid_col_tag_arr"+"Grid_col_tag_arr: "+Grid_col_tag);   
										CreditCard.mLogger.info("childnode"+"childnode"+childnode); 
										List<String> Grid_row = new ArrayList<String>(); 
										Grid_row.clear();

										String flaga="N";
										for(int k = 0;k<Grid_col_tag_arr.length;k++){

											for (int child_node_len = 0 ;child_node_len< childnode.getLength();child_node_len++){
												if(childnode.item(child_node_len).getNodeName().equalsIgnoreCase(Grid_col_tag_arr[k])){
													CreditCard.mLogger.info("child_node_len "+"getTextContent: "+childnode.item(child_node_len).getNodeName());
													CreditCard.mLogger.info("child_node_len "+"getTextContent: "+childnode.item(child_node_len).getTextContent());
													if("AddrPrefFlag".equalsIgnoreCase(childnode.item(child_node_len).getNodeName()))
													{
														if("Y".equalsIgnoreCase(childnode.item(child_node_len).getTextContent()))
															Grid_row.add("true");

														else if("N".equalsIgnoreCase(childnode.item(child_node_len).getTextContent()))
															Grid_row.add("false");

													}
													else
														Grid_row.add(childnode.item(child_node_len).getTextContent());
													flaga="Y";
													break;
												}                                            
											}

											if("N".equalsIgnoreCase(flaga) ){
												CreditCard.mLogger.info("child_node_len "+"Grid_col_tag_arr[k]: "+Grid_col_tag_arr[k]);
												// CreditCard.mLogger.info("child_node_len "+"getTextContent: "+childnode.item(child_node_len).getTextContent());
												Grid_row.add("NA");

											}
											flaga="N";

										}
										//Grid_row.add("true");
										Grid_row.add(formObject.getWFWorkitemName());
										//Grid_col_tag_arr[k]
										//code to add row in grid. and pass Grid_row in that.
										CreditCard.mLogger.info("RLOS Common# getOutputXMLValues()"+ "$$AKSHAY$$List to be added in address grid: "+ Grid_row.toString());

										if("AddrDet".equalsIgnoreCase(fielddbxml_tag)){ 
											formObject.addItemFromList("cmplx_AddressDetails_cmplx_AddressGrid", Grid_row);


										}
									}
									else{
										CreditCard.mLogger.info("RLOS Common# getOutputXMLValues()"+ "Grid mapping is Y for this tag and grid_table_xml_tags column is blank tag name: "+ fielddbxml_tag);
									}

								}
								else if("Y".equalsIgnoreCase(indirectMapping)){
									CreditCard.mLogger.info("Grid_col_tag_arr"+"inside indirect mapping");
									String col_list = "xmltag_name,tag_value,indirect_tag_list,indirect_formfield_list,indirect_child_tag,form_control,indirect_val,IS_Master,Master_Name";
									//code added
									if(!("".equalsIgnoreCase(Operation_name) || Operation_name!=null)){   
										CreditCard.mLogger.info("inside if of operation"+"operation111"+Operation_name);
										CreditCard.mLogger.info("inside if of operation"+"callName111"+col_name);
										sQuery = "SELECT "+col_list+" FROM NG_CC_Integration_Indirect_Response with (nolock) where Call_Name ='"+response+"' and XmlTag_Name = '"+nl.item(i).getNodeName()+"' and Operation_name='"+Operation_name+"'" ;
										CreditCard.mLogger.info("RLOSCommon"+ "sQuery "+sQuery);
									}

									else{
										sQuery = "SELECT "+col_list+" FROM NG_CC_Integration_Indirect_Response with (nolock) where Call_Name ='"+response+"' and XmlTag_Name = '"+nl.item(i).getNodeName()+"'";
										CreditCard.mLogger.info("#RLOS Common Inside indirectMapping "+ "query: "+sQuery);
									}
									List<List<String>> outputindirect=formObject.getNGDataFromDataCache(sQuery);
									CreditCard.mLogger.info("#RLOS Common Inside indirectMapping test tanshu "+ "1");
									String col_list_arr[] = col_list.split(",");
									Map<String, String> gridResponseMap = new HashMap<String, String>();
									for(List<String> mygridlist:outputindirect)
									{
										CreditCard.mLogger.info("#RLOS Common Inside indirectMapping test tanshu "+ "inside list loop");

										for(int x=0;x<col_list_arr.length;x++)
										{
											gridResponseMap.put(col_list_arr[x],mygridlist.get(x));
											CreditCard.mLogger.info("#RLOS Common Inside indirectMapping "+ "inside put map"+x);
										}
										String xmltag_name = gridResponseMap.get("xmltag_name").toString();
										String tag_value = gridResponseMap.get("tag_value").toString();
										String indirect_tag_list = gridResponseMap.get("indirect_tag_list").toString();
										String indirect_formfield_list = gridResponseMap.get("indirect_formfield_list").toString();
										String indirect_child_tag = gridResponseMap.get("indirect_child_tag").toString();
										String indirect_form_control = gridResponseMap.get("form_control").toString();
										CreditCard.mLogger.info("indirect_form_control in string"+indirect_form_control );
										String indirect_val = gridResponseMap.get("indirect_val").toString();
										String IS_Master = gridResponseMap.get("IS_Master").toString();
										CreditCard.mLogger.info("#RLOS Common Inside indirectMapping "+ "IS_Master"+IS_Master);
										String Master_Name = gridResponseMap.get("Master_Name").toString();
										CreditCard.mLogger.info("#RLOS Common Inside indirectMapping "+ "Master_Name"+Master_Name);
										CreditCard.mLogger.info("#RLOS Common Inside indirectMapping "+ "all details fetched");
										if("Y".equalsIgnoreCase(IS_Master)){
											String code = nl.item(i).getTextContent();
											CreditCard.mLogger.info("#RLOS Common Inside indirectMapping code:"+code);
											sQuery= "Select description from "+Master_Name+" where Code='"+code+"'";
											CreditCard.mLogger.info("#RLOS Common Inside indirectMapping code:"+"Query to select master value: "+  sQuery);
											List<List<String>> query=formObject.getNGDataFromDataCache(sQuery);
											String value=query.get(0).get(0);
											CreditCard.mLogger.info("#query.get(0).get(0)"+value );
											CreditCard.mLogger.info("#RLOS Common Inside indirectMapping code:"+"Query to select master value: "+  sQuery);
											formObject.setNGValue(indirect_form_control,value);
											CreditCard.mLogger.info("indirect_form_control value"+formObject.getNGValue(indirect_form_control));
										}

										else if(indirect_form_control!=null && !"".equalsIgnoreCase(indirect_form_control)){
											if( tag_value.equalsIgnoreCase(nl.item(i).getTextContent())){
												CreditCard.mLogger.info("RLOS common: getOutputXMLValues"+"Indirect value for "+xmltag_name+" Indirect control name: "+indirect_form_control+" final value: "+indirect_val+" tag_value: "+tag_value);
												formObject.setNGValue(indirect_form_control,indirect_val,false);
											}
											// CreditCard.mLogger.info("form Control: "+ indirect_form_control + "indirect val: "+ indirect_val);

										}

										else{
											CreditCard.mLogger.info("Grid_col_tag_arr"+"inside indirect mapping part2 else");
											if(indirect_tag_list!=null ){
												NodeList childnode  = nl.item(i).getChildNodes();
												CreditCard.mLogger.info("childnode"+"childnode"+childnode);
												if(indirect_tag_list.contains(",")){
													CreditCard.mLogger.info("#RLOS common indirect field values"+"inside indirect mapping part2 indirect_tag_list with ,");
													String indirect_tag_list_arr[] = indirect_tag_list.split(",");
													String indirect_formfield_list_arr[] = indirect_formfield_list.split(",");
													if(indirect_tag_list_arr.length == indirect_formfield_list_arr.length){
														for (int k=0; k<indirect_tag_list_arr.length;k++){
															CreditCard.mLogger.info("#RLOS Common inside child node 1 "+ "node name : "+childnode.item(0).getNodeName() +" node data: "+childnode.item(1).getTextContent());
															if(tag_value.equalsIgnoreCase(childnode.item(0).getTextContent()) && indirect_child_tag.equalsIgnoreCase(childnode.item(0).getNodeName())){
																//Ref. 1006
																for (int child_node_len = 1 ;child_node_len< childnode.getLength();child_node_len++){
																	//Ref. 1006 end.
																	CreditCard.mLogger.info("#RLOS common: "+ "child node IF: Child node value: "+ child_node_len+" name: "+ childnode.item(child_node_len).getNodeName()+" child node value: "+ childnode.item(child_node_len).getTextContent());

																	if(childnode.item(child_node_len).getNodeName().equalsIgnoreCase(indirect_tag_list_arr[k])){

																		CreditCard.mLogger.info("child_node_len"+"getTextContent"+childnode.item(child_node_len).getNodeName());
																		CreditCard.mLogger.info("child_node_len"+"getTextContent"+childnode.item(child_node_len).getTextContent());
																		CreditCard.mLogger.info("RLOS common: getOutputXMLValues"+"");
																		CreditCard.mLogger.info(""+indirect_formfield_list_arr[k]+" :"+childnode.item(child_node_len).getTextContent());
																		formObject.setNGValue(indirect_formfield_list_arr[k],childnode.item(child_node_len).getTextContent(),false);
																	}
																}

															}
														}
													}
													else{
														CreditCard.mLogger.info("RLOS common: getOutputXMLValues"+"Error as for :"+xmltag_name+" indirect_formfield_list and indirect_tag_list dosent match");
													} 
												}
												else{
													CreditCard.mLogger.info("#RLOS common indirect field values"+"inside indirect mapping part2 indirect_tag_list without ,");
													for (int child_node_len = 0 ;child_node_len< childnode.getLength();child_node_len++){
														if(tag_value.equalsIgnoreCase(childnode.item(child_node_len).getTextContent()) && indirect_child_tag.equalsIgnoreCase(childnode.item(child_node_len).getNodeName())){
															if(childnode.item(child_node_len).getNodeName().equalsIgnoreCase(indirect_tag_list)){
																CreditCard.mLogger.info("child_node_len"+"getTextContent"+childnode.item(child_node_len).getNodeName());
																CreditCard.mLogger.info("child_node_len"+"getTextContent"+childnode.item(child_node_len).getTextContent());
																CreditCard.mLogger.info("RLOS common: getOutputXMLValues"+"");
																CreditCard.mLogger.info(""+indirect_formfield_list+" :"+childnode.item(child_node_len).getTextContent());
																formObject.setNGValue(indirect_formfield_list,childnode.item(child_node_len).getTextContent(),false);
															}
														}

													}
												}


											}
										}     
									}
									//List<List<String>> outputIndirectXML=formObject.getNGDataFromDataCache(sQuery);
									//CreditCard.mLogger.info("$$outputIndirectXML "+outputIndirectXML.get(0).get(0)+outputIndirectXML.get(0).get(1)+outputIndirectXML.get(0).get(2));

								}
								if("N".equalsIgnoreCase(indirectMapping) && "N".equalsIgnoreCase(gridMapping))
								{    
									CreditCard.mLogger.info("check14 " +"check");
									tagValue = CC_Common.getTagValue(outputXMLMsg,nl.item(i).getNodeName());
									CreditCard.mLogger.info("Node value "+"tagValue:"+tagValue);
									CreditCard.mLogger.info("Node form_control "+"form_control:"+ form_control);

									CreditCard.mLogger.info("$$tagValue NN  "+tagValue);
									CreditCard.mLogger.info("$$form_control  NN "+form_control);
									formObject.setNGValue(form_control,tagValue,false);
								}
							}
						}
					}
					//till for loop

				}
			}
		}
		catch(Exception e)
		{
			CreditCard.mLogger.info("Exception occured in getOutputXMLValues:  "+e.getMessage());

		}
	}
	
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : dectech from eligibility          

	 ***********************************************************************************  */
	public static void dectechfromeligbility(String outputResponse){
		try{
			if(outputResponse.indexOf("<PM_Reason_Codes>")>-1 && outputResponse.indexOf("<PM_Outputs>")>-1 ){
				String squery="";
				String Output_TAI="";
				String Output_Decision="";
				String Output_Final_DBR="";
				String Output_Existing_DBR  ="";
				String Output_Eligible_Amount="";
				String Output_Delegation_Authority="";
				String output_accomodation="";
				String Grade="";
				String Output_Interest_Rate="";
				String Output_Net_Salary_DBR="";
				String ReasonCode="";
				String DeviationCode="";
				String Output_Accommodation_Allowance="";
				FormReference formObject = FormContext.getCurrentInstance().getFormReference();
				String strOutputXml = "";
				String sGeneralData = formObject.getWFGeneralData();
				CreditCard.mLogger.info("$$outputXMLMsg "+"inside outpute get sGeneralData"+sGeneralData);
				double cac_calc_limit=0.0;
				String cac_calc_limit_str = null;
				String cabinetName = sGeneralData.substring(sGeneralData.indexOf("<EngineName>")+12,sGeneralData.indexOf("</EngineName>")); 
				String sessionId = sGeneralData.substring(sGeneralData.indexOf("<DMSSessionId>")+14,sGeneralData.indexOf("</DMSSessionId>"));
				String jtsIp = sGeneralData.substring(sGeneralData.indexOf("<JTSIP>")+7,sGeneralData.indexOf("</JTSIP>"));
				String jtsPort = sGeneralData.substring(sGeneralData.indexOf("<JTSPORT>")+9,sGeneralData.indexOf("</JTSPORT>"));
				String Output_Affordable_EMI="";
				//Setting the value in ELIGANDPROD info
				if (outputResponse.contains("Output_TAI")){
					Output_TAI = outputResponse.substring(outputResponse.indexOf("<Output_TAI>")+12,outputResponse.indexOf("</Output_TAI>"));
					if (Output_TAI!=null){
						try{
							formObject.setNGValue("cmplx_Liability_New_TAI", Output_TAI);
							formObject.setNGValue("cmplx_EligibilityAndProductInfo_FinalTAI", Output_TAI);

						}
						catch (Exception e){
							CreditCard.mLogger.info("Dectech error"+ "Exception:"+e.getMessage());
						}


					}
					CreditCard.mLogger.info("$$outputXMLMsg "+"inside outpute get Output_TAI"+Output_TAI);
				}
				
				boolean DecFragVis=formObject.isVisible("DecisionHistory_Frame1");
				CreditCard.mLogger.info("$$outputXMLMsg "+"inside outpute get Output_TAI"+DecFragVis);
				if (DecFragVis==false){
					formObject.fetchFragment("DecisionHistory", "DecisionHistory", "q_DecisionHistory");
					
					new CC_Common().Decision_cadanalyst1();
					
				}
				
				if (outputResponse.contains("Output_Delegation_Authority")){
					Output_Delegation_Authority = outputResponse.substring(outputResponse.indexOf("<Output_Delegation_Authority>")+29,outputResponse.indexOf("</Output_Delegation_Authority>"));
					CreditCard.mLogger.info("$$outputXMLMsg "+"inside outpute get Output_Existing_DBR"+Output_Eligible_Amount);
					formObject.setNGValue("cmplx_DEC_HighDeligatinAuth", Output_Delegation_Authority);
				}
				//Added by aman for PROC-2535
				if (outputResponse.contains("Output_Accommodation_Allowance")){
					Output_Accommodation_Allowance = outputResponse.substring(outputResponse.indexOf("<Output_Accommodation_Allowance>")+32,outputResponse.indexOf("</Output_Accommodation_Allowance>"));
					CreditCard.mLogger.info("$$outputXMLMsg "+"inside outpute get Output_Accommodation_Allowance"+Output_Accommodation_Allowance);
					formObject.setNGValue("cmplx_IncomeDetails_CompanyAcc", Output_Accommodation_Allowance);
				}
				//Added by aman for PROC-2535
				if (outputResponse.contains("Output_Decision")){
					Output_Decision = outputResponse.substring(outputResponse.indexOf("<Output_Decision>")+17,outputResponse.indexOf("</Output_Decision>"));
					if (Output_Decision!=null){
						try{
							if ("D".equalsIgnoreCase(Output_Decision)){
								Output_Decision="Declined";
							}	
							else if ("A".equalsIgnoreCase(Output_Decision)){
								Output_Decision="Approve";
							}	
							else if ("R".equalsIgnoreCase(Output_Decision)){
								Output_Decision="Refer";
							}	
							formObject.setNGValue("cmplx_DEC_DectechDecision", Output_Decision);


						}
						catch (Exception e){
							CreditCard.mLogger.info("Dectech error"+ "Exception:"+e.getMessage());
						}


					}
					CreditCard.mLogger.info("$$outputXMLMsg "+"inside outpute get Output_TAI"+Output_TAI);
				}
				
				if (outputResponse.contains("Output_Eligible_Cards")){
					try{
						String Output_Eligible_Cards = outputResponse.substring(outputResponse.indexOf("<Output_Eligible_Cards>")+23,outputResponse.indexOf("</Output_Eligible_Cards>"));
						Output_Eligible_Cards=Output_Eligible_Cards.substring(2, Output_Eligible_Cards.length()-2);
						String strInputXML =CC_Common.ExecuteQuery_APdelete("ng_rlos_IGR_Eligibility_CardLimit","WI_NAME ='" + formObject.getWFWorkitemName() + "'",cabinetName,sessionId);
						strOutputXml = WFCallBroker.execute(strInputXML,jtsIp,Integer.parseInt(jtsPort),1);

						CreditCard.mLogger.info("Output_Eligible_Cards"+ "after removing the starting and Trailing:"+Output_Eligible_Cards);
						String[] Output_Eligible_Cards_Arr=Output_Eligible_Cards.split("\\},\\{");
						for (int i=0; i<Output_Eligible_Cards_Arr.length;i++){
							String[] Output_Eligible_Cards_Array=Output_Eligible_Cards_Arr[i].split(",");
							CreditCard.mLogger.info("Output_Eligible_Cards"+ "Output_Eligible_Cards_Array:"+Output_Eligible_Cards_Array);
							String[] Card_Prod=Output_Eligible_Cards_Array[0].split(":");
							String[] Limit=Output_Eligible_Cards_Array[1].split(":");
							String[] flag=Output_Eligible_Cards_Array[2].split(":");
							String Card_Product= Card_Prod[1].substring(1,Card_Prod[1].length()-1);
							String LIMIT= Limit[1];
							String FLAG= flag[1].substring(1,flag[1].length()-1);

							CreditCard.mLogger.info("Output_Eligible_Cards"+ "Card_Prod:"+Card_Prod[1]);
							CreditCard.mLogger.info("Output_Eligible_Cards"+ "Limit:"+Limit[1]);
							CreditCard.mLogger.info("Output_Eligible_Cards"+ "flag:"+flag[1]);
							String query="INSERT INTO ng_rlos_IGR_Eligibility_CardLimit VALUES ('"+Card_Product+"','"+LIMIT+"','','"+ formObject.getWFWorkitemName() +"','"+ FLAG +"')";
							CreditCard.mLogger.info("Output_Eligible_Cards"+ "QUERY:"+query);
							formObject.saveDataIntoDataSource(query);

							/* for (int j=0; j<Output_Eligible_Cards_Array.length;j++){
	    					 String[] values=Output_Eligible_Cards_Array[j].split(":");
	    					 CreditCard.mLogger.info("Output_Eligible_Cards"+ "values:"+values);
	    					  if(values[0].contains("\"")){
	    						  values[0]=values[0].substring(1, values[0].length()-1);
	    				    	}
	    					  if(values[1].contains("\"")){
	    						  values[1]=values[1].substring(1, values[1].length()-1);
	    				    	}
	    					  String strInputXML =ExecuteQuery_APdelete("ng_rlos_IGR_Eligibility_CardProduct","WI_NAME ='" + formObject.getWFWorkitemName() + "'",cabinetName,sessionId);
	    					  strOutputXml = WFCallBroker.execute(strInputXML,jtsIp,Integer.parseInt(jtsPort),1);
	    					  String query="INSERT INTO ng_rlos_IGR_Eligibility_CardLimit("+values[0]+") VALUES ('"+values[1]+"')";
	    					  CreditCard.mLogger.info("Output_Eligible_Cards"+ "QUERY:"+query);
	    			    	  formObject.saveDataIntoDataSource(query);
	    				  }*/
						}
					}
					catch(Exception e){
						CreditCard.mLogger.info("RLOSCommon"+ "Exception occurred in elig dectech"+ CC_Common.printException(e));

					}
				}	
				if (outputResponse.contains("Output_Final_DBR")){

					Output_Final_DBR = outputResponse.substring(outputResponse.indexOf("<Output_Final_DBR>")+18,outputResponse.indexOf("</Output_Final_DBR>"));
					if (Output_Final_DBR!=null){
						formObject.setNGValue("cmplx_EligibilityAndProductInfo_FinalDBR", Output_Final_DBR);
					}
					CreditCard.mLogger.info("$$outputXMLMsg "+"inside outpute get Output_Final_DBR"+Output_Final_DBR);
				}
				if (outputResponse.contains("Output_Interest_Rate")){
					Output_Interest_Rate = outputResponse.substring(outputResponse.indexOf("<Output_Interest_Rate>")+22,outputResponse.indexOf("</Output_Interest_Rate>"));
					if (Output_Interest_Rate!=null){
						formObject.setNGValue("cmplx_EligibilityAndProductInfo_InterestRate", Output_Interest_Rate);
					}
					CreditCard.mLogger.info("$$outputXMLMsg "+"inside outpute get Output_Interest_Rate"+Output_Interest_Rate);

				}
				//Setting the value in ELIGANDPROD info
				//Setting the value in lIABILITY info
				if (outputResponse.contains("Output_Existing_DBR")){
					Output_Existing_DBR = outputResponse.substring(outputResponse.indexOf("<Output_Existing_DBR>")+21,outputResponse.indexOf("</Output_Existing_DBR>"));
					if (Output_Existing_DBR!=null){
						formObject.setNGValue("cmplx_Liability_New_DBR", Output_Existing_DBR);
					}
					CreditCard.mLogger.info("$$outputXMLMsg "+"inside outpute get Output_Existing_DBR"+Output_Existing_DBR);

				}
				if (outputResponse.contains("Output_Affordable_EMI")){
					Output_Affordable_EMI = outputResponse.substring(outputResponse.indexOf("<Output_Affordable_EMI>")+23,outputResponse.indexOf("</Output_Affordable_EMI>"));
					CreditCard.mLogger.info("$$outputXMLMsg "+"inside outpute get Output_Affordable_EMI"+Output_Affordable_EMI);

				}
				try{
					double tenor=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor"));
					double RateofInt=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate"));
					double out_aff_emi=Double.parseDouble(Output_Affordable_EMI);
					cac_calc_limit=Math.round(Cas_Limit(out_aff_emi,RateofInt,tenor));
					cac_calc_limit_str = String.format("%.0f", cac_calc_limit);
					CreditCard.mLogger.info("$$outputXMLMsg "+"inside outpute get sGeneralData"+tenor+" "+RateofInt+" "+out_aff_emi+" "+cac_calc_limit);
					}
					catch(Exception e){}

				if (outputResponse.contains("Output_Net_Salary_DBR")){
					Output_Net_Salary_DBR = outputResponse.substring(outputResponse.indexOf("<Output_Net_Salary_DBR>")+23,outputResponse.indexOf("</Output_Net_Salary_DBR>"));
					if (Output_Net_Salary_DBR!=null){
						formObject.setNGValue("cmplx_Liability_New_DBRNet", Output_Net_Salary_DBR);
					}
					CreditCard.mLogger.info("$$outputXMLMsg "+"inside outpute get Output_Net_Salary_DBR"+Output_Net_Salary_DBR);

				}
				//Setting the value in lIABILITY info
				//Setting the value in creditCard iFrame
				
				
				if (outputResponse.contains("output_accomodation")){
					output_accomodation = outputResponse.substring(outputResponse.indexOf("<output_accomodation>")+20,outputResponse.indexOf("</output_accomodation>"));
					CreditCard.mLogger.info("$$outputXMLMsg "+"inside outpute get Output_Existing_DBR"+output_accomodation);
					if (output_accomodation!=null){
						formObject.setNGValue("cmplx_IncomeDetails_CompanyAcc", output_accomodation);
					}
				}
				if (outputResponse.contains("Grade")){
					Grade = outputResponse.substring(outputResponse.indexOf("<Grade>")+7,outputResponse.indexOf("</Grade>"));
					if (Grade!=null){
			    		formObject.setNGValue("cmplx_DEC_ScoreGrade", Grade);
			    	}
					CreditCard.mLogger.info("$$outputXMLMsg "+"inside outpute get Grade"+Grade);

				}
				if (outputResponse.contains("Output_Eligible_Amount")){
					Output_Eligible_Amount = outputResponse.substring(outputResponse.indexOf("<Output_Eligible_Amount>")+24,outputResponse.indexOf("</Output_Eligible_Amount>"));
					CreditCard.mLogger.info("$$outputXMLMsg "+"inside outpute get Output_Existing_DBR"+Output_Eligible_Amount);

				}
				//Setting the value in creditCard iFrame


				//String strInputXML = ExecuteQuery_APUpdate("ng_rlos_IGR_Eligibility_CardProduct","Product,Card_Product,Eligible_Card,Decision,Declined_Reason,wi_name","'Credit Card','"+subProd+"','"+Output_Eligible_Amount+"','Declined','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+formObject.getWFWorkitemName()+"'","WI_NAME ='" + formObject.getWFWorkitemName() + "'",cabinetName,sessionId);
				//CreditCard.mLogger.info("$$Value set for DECTECH->>"+"UpdateinputXML is:"+UpdateinputXML);
				String strInputXML =CC_Common.ExecuteQuery_APdelete("ng_rlos_IGR_Eligibility_CardProduct","Child_Wi ='" + formObject.getWFWorkitemName() + "'",cabinetName,sessionId);

				strOutputXml = WFCallBroker.execute(strInputXML,jtsIp,Integer.parseInt(jtsPort),1);
				CreditCard.mLogger.info("$$Value set for DECTECH->>"+"strOutputXml is:"+strOutputXml);

				String mainCode=strOutputXml.substring(strOutputXml.indexOf("<MainCode>")+10,strOutputXml.indexOf("</MainCode>"));
				String rowUpdated=strOutputXml.substring(strOutputXml.indexOf("<Output>")+8,strOutputXml.indexOf("</Output>"));
				CreditCard.mLogger.info("$$Value set for DECTECH->>"+"mainCode,rowUpdated is: "+mainCode+", "+rowUpdated);

				outputResponse = outputResponse.substring(outputResponse.indexOf("<ProcessManagerResponse>")+24, outputResponse.indexOf("</ProcessManagerResponse>"));
				outputResponse = "<?xml version=\"1.0\"?><Response>" + outputResponse;
				outputResponse = outputResponse+"</Response>";
				CreditCard.mLogger.info("$$outputResponse "+"inside outpute get outputResponse"+outputResponse);
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				InputSource is = new InputSource(new StringReader(outputResponse));

				Document doc = builder.parse(is);
				doc.getDocumentElement().normalize();

				CreditCard.mLogger.info("Root element :" +doc.getDocumentElement().getNodeName());

				NodeList nList = doc.getElementsByTagName("PM_Reason_Codes");
				

				for (int temp = 0; temp < nList.getLength(); temp++) {
					String Reason_Decision="";
					Node nNode = nList.item(temp);
					

					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						//CreditCard.mLogger.info("Student roll no : " + eElement.getAttribute("rollno"));
						CreditCard.mLogger.info("$$outputResponse "+"inside outpute get Decision_Objective"+ eElement.getElementsByTagName("Decision_Objective").item(0).getTextContent());
						CreditCard.mLogger.info("$$outputResponse "+"inside outpute get Decision_Sequence_Number"+eElement.getElementsByTagName("Decision_Sequence_Number").item(0).getTextContent());
						CreditCard.mLogger.info("$$outputResponse "+"inside outpute get Sequence_Number"+eElement.getElementsByTagName("Sequence_Number").item(0).getTextContent());

						CreditCard.mLogger.info("$$outputResponse "+"inside outpute get Reason_Description"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent());
						String subProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2);//2
						//change by saurabh on 21st Dec
						new CC_Common().LoadPickList("subProd", "select  '--Select--' as description,'' as code union select convert(varchar,description),code from ng_master_Subproduct_CC with (nolock) order by code");
						formObject.setNGValue("subProd", subProd);
						subProd = formObject.getNGSelectedItemText("subProd");
						formObject.setNGValue("subProd", "");
						ReasonCode= eElement.getElementsByTagName("Reason_Code").item(0).getTextContent();
						Reason_Decision = eElement.getElementsByTagName("Reason_Decision").item(0).getTextContent() ;
						try{
							if(temp==0){
								
								DeviationCode=ReasonCode;}
							else{
								DeviationCode=DeviationCode+","+ReasonCode;}
							}
						catch(Exception e){
							
						}CreditCard.mLogger.info("$$outputResponse "+"Value of Reason_Decision"+Reason_Decision);
						if (temp==0){
							if ("D".equalsIgnoreCase(Reason_Decision)){
								squery="Insert into ng_rlos_IGR_Eligibility_CardProduct (S_No,Product,Card_Product,Eligible_Card,Decision,Declined_Reason,Delegation_Authorithy,Score_grade,child_wi,CACCalculatedLimit) values('"+temp+"','Credit Card','"+subProd+"','"+Output_Eligible_Amount+"','Declined','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"','"+cac_calc_limit_str+"')";
							}
							if ("R".equalsIgnoreCase(Reason_Decision)){
								squery="Insert into ng_rlos_IGR_Eligibility_CardProduct (S_No,Product,Card_Product,Eligible_Card,Decision,Deviation_Code_Refer,Delegation_Authorithy,Score_grade,child_wi,CACCalculatedLimit) values('"+temp+"','Credit Card','"+subProd+"','"+Output_Eligible_Amount+"','Refer','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"','"+cac_calc_limit_str+"')";
							}
							if ("A".equalsIgnoreCase(Reason_Decision)){
								squery="Insert into ng_rlos_IGR_Eligibility_CardProduct (S_No,Product,Card_Product,Eligible_Card,Decision,Deviation_Code_Refer,Delegation_Authorithy,Score_grade,child_wi,CACCalculatedLimit) values('"+temp+"','Credit Card','"+subProd+"','"+Output_Eligible_Amount+"','Approve','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"','"+cac_calc_limit_str+"')";
							}
						}
						else{
							if ("D".equalsIgnoreCase(Reason_Decision)){
								squery="Insert into ng_rlos_IGR_Eligibility_CardProduct (S_No,Product,Card_Product,Eligible_Card,Decision,Declined_Reason,Delegation_Authorithy,Score_grade,child_wi,CACCalculatedLimit) values('"+temp+"','Credit Card','"+subProd+"','"+Output_Eligible_Amount+"','Declined','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"','')";
							}
							if ("R".equalsIgnoreCase(Reason_Decision)){
								squery="Insert into ng_rlos_IGR_Eligibility_CardProduct (S_No,Product,Card_Product,Eligible_Card,Decision,Deviation_Code_Refer,Delegation_Authorithy,Score_grade,child_wi,CACCalculatedLimit) values('"+temp+"','Credit Card','"+subProd+"','"+Output_Eligible_Amount+"','Refer','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"','')";
							}
							if ("A".equalsIgnoreCase(Reason_Decision)){
								squery="Insert into ng_rlos_IGR_Eligibility_CardProduct (S_No,Product,Card_Product,Eligible_Card,Decision,Deviation_Code_Refer,Delegation_Authorithy,Score_grade,child_wi,CACCalculatedLimit) values('"+temp+"','Credit Card','"+subProd+"','"+Output_Eligible_Amount+"','Approve','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"','')";
							}
						}
						CreditCard.mLogger.info("$$outputResponse "+"Squery is"+squery);
						formObject.saveDataIntoDataSource(squery);

					}
				} formObject.setNGValue("cmplx_DEC_DeviationCode", DeviationCode); 
			}
		}
		catch(Exception e){
			CreditCard.mLogger.info("Dectech error"+ "Exception:"+e.getMessage());

		}

	}
	
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : CAS Limit          

	 ***********************************************************************************  */
	public static double Cas_Limit(double aff_emi,double rate,double tenureMonths)
	{
		double pmt;
		try{
			double new_rate = (rate/100)/12;
			 pmt = (aff_emi)*(1-Math.pow(1+new_rate,-tenureMonths))/new_rate;
			CreditCard.mLogger.info("CC_Common"+"final_rate_new 1ST pmt11 : " + pmt);
		}
		catch(Exception e){
			pmt=0;
		}
		return pmt;
		
	}

}
