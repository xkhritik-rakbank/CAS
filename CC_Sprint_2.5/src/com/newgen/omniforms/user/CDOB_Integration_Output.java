

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

import javax.faces.component.UIComponent;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.newgen.custom.Common_Utils;
import com.newgen.custom.XMLParser;
import com.newgen.omni.wf.util.app.NGEjbClient;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.Column;
import com.newgen.omniforms.component.ListView;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.util.Common;

//import com.newgen.wfdesktop.xmlapi.WFCallBroker;

public class CDOB_Integration_Output extends CDOB_Common implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static CDOB_Common CC_Comn = new CDOB_Common();
	static NGEjbClient ngejbclient = null;
	
	
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : value set customer         

	 ***********************************************************************************  */
	public void valueSetCustomer(String outputResponse ,String operationName)
	{
		DigitalOnBoarding.mLogger.info("RLOSCommon valueSetCustomer"+ "Inside valueSetCustomer():");
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
				DigitalOnBoarding.mLogger.info("RLOSCommon valueSetCustomer"+ "outputXMLHead");
			}
			objXMLParser.setInputXML(outputXMLHead);
			if(outputXMLHead.indexOf("<MsgFormat>")>-1)
			{
				response= outputXMLHead.substring(outputXMLHead.indexOf("<MsgFormat>")+11,outputXMLHead.indexOf("</MsgFormat>"));
				DigitalOnBoarding.mLogger.info("$$response "+response);
			}
			if(outputXMLHead.indexOf("<ReturnDesc>")>-1)
			{
				returnDesc= outputXMLHead.substring(outputXMLHead.indexOf("<ReturnDesc>")+12,outputXMLHead.indexOf("</ReturnDesc>"));
				DigitalOnBoarding.mLogger.info("$$returnDesc "+returnDesc);
			}
			if(outputXMLHead.indexOf("<ReturnCode>")>-1)
			{
				returnCode= outputXMLHead.substring(outputXMLHead.indexOf("<ReturnCode>")+12,outputXMLHead.indexOf("</ReturnCode>"));
				DigitalOnBoarding.mLogger.info("$$returnCode "+returnCode);
			}

			if(outputResponse.indexOf("<MQ_RESPONSE_XML>")>-1)
			{
				outputXMLMsg=outputResponse.substring(outputResponse.indexOf("<MQ_RESPONSE_XML>")+17,outputResponse.indexOf("</MQ_RESPONSE_XML>"));
				//CreditCard.mLogger.info("$$outputXMLMsg "+outputXMLMsg);
				DigitalOnBoarding.mLogger.info("$$outputXMLMsg getOutputXMLValues"+"check inside getOutputXMLValues");
				//getOutputXMLValues(outputXMLMsg,response);
				getOutputXMLValues(outputXMLMsg,response,operationName);
				DigitalOnBoarding.mLogger.info("$$outputXMLMsg "+"outputXMLMsg");
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
			DigitalOnBoarding.mLogger.info("Exception occured in valueSetCustomer method:  "+e.getMessage());
			
		}
	}
	
	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : getoutput xml values       

	 ***********************************************************************************  */
	/*public static void getOutputXMLValues(String outputXMLMsg, String response,String Operation_name) throws ParserConfigurationException
	{
		String sQuery = "";
		String tagValue = "";
		try
		{   
			CreditCard.mLogger.info("inside getOutputXMLValues"+"inside outputxml");
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			Common_Utils common=new Common_Utils(CreditCard.mLogger);
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
														else{
															CreditCard.mLogger.info("Matching column  is found in call tag");
															UIComponent pComp =formObject.getComponent(form_control);
															if( pComp != null && pComp instanceof ListView )
															{			
																//ListView objListView = ( ListView )pComp;
																if(!((Column)(pComp.getChildren().get(k))).getFormat().equals("")){
																	CreditCard.mLogger.info(form_control+"-->"+((Column)(pComp.getChildren().get(k))).getName()+" is of date type");
																	CreditCard.mLogger.info("After converting date format-->value: "+common.Convert_dateFormat(childnode.item(child_node_len).getTextContent(), "yyyy-MM-dd", "dd/MM/yyyy"));
																	Grid_row.add(common.Convert_dateFormat(childnode.item(child_node_len).getTextContent(), "yyyy-MM-dd", "dd/MM/yyyy"));																
																}
																else{
																	
																	Grid_row.add(childnode.item(child_node_len).getTextContent());
																}
															}
														}													flaga="Y";
													break;
												}                                            
											}

											if("N".equalsIgnoreCase(flaga) ){
												CreditCard.mLogger.info("value of flaga in if of for loop "+flaga);
												CreditCard.mLogger.info("Grid_col_tag_arr[k]: "+Grid_col_tag_arr[k]);
												UIComponent pComp =formObject.getComponent(form_control);
												if( pComp != null && pComp instanceof ListView )
												{			
													//ListView objListView = ( ListView )pComp;
													if(!((Column)(pComp.getChildren().get(k))).getFormat().equals("")){//date type column
														CreditCard.mLogger.info(form_control+"-->"+((Column)(pComp.getChildren().get(k))).getName()+" is of date type");
														Grid_row.add("");
													}
													else{//text type column
														Grid_row.add("NA");
													}
												}
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
										else{
											formObject.addItemFromList(form_control, Grid_row);

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
	}----commented by akshay on 14/6/18*/
	
	
	public static void getOutputXMLValues(String outputXMLMsg, String response,String Operation_name) throws ParserConfigurationException
	{
		String sQuery = "";
		String tagValue = "";
		try
		{   
			DigitalOnBoarding.mLogger.info("inside getOutputXMLValues"+"inside outputxml");
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			Common_Utils common=new Common_Utils(DigitalOnBoarding.mLogger);
			String col_name = "Form_Control,Parent_Tag_Name,XmlTag_Name,InDirect_Mapping,Grid_Mapping,Grid_Table,grid_table_xml_tags";

			//  sQuery = "SELECT "+col_name+" FROM NG_PL_Integration_Field_Response where Call_Name ='"+response+"'";
			//code added here
			if(!"".equalsIgnoreCase(Operation_name) && Operation_name!=null){   
				DigitalOnBoarding.mLogger.info("inside if of operation"+"operation111"+Operation_name);
				DigitalOnBoarding.mLogger.info("inside if of operation"+"callName111"+col_name);
				sQuery = "SELECT "+col_name+" FROM NG_CDOB_Integration_Field_Response with (nolock) where Call_Name ='"+response+"' and Operation_name='"+Operation_name+"'" ;
				
			}
			//Changes done by Deepak to not execute this method if no response-mapping is available.   
			else if(!"".equalsIgnoreCase(response)){
				sQuery = "SELECT "+col_name+" FROM NG_CDOB_Integration_Field_Response with (nolock) where Call_Name ='"+response+"'";
			}
			
			if(!"".equalsIgnoreCase(sQuery)){
				DigitalOnBoarding.mLogger.info("RLOSCommon"+ "sQuery "+sQuery);
				//ended here
				List<List<String>> outputTableXML=formObject.getDataFromDataSource(sQuery);


				String[] col_name_arr = col_name.split(",");

				DigitalOnBoarding.mLogger.info("$$outputTableXML"+outputTableXML.get(0).get(0)+outputTableXML.get(0).get(1)+outputTableXML.get(0).get(2)+outputTableXML.get(0).get(3));
				int n=outputTableXML.size();
				DigitalOnBoarding.mLogger.info("outputTableXML size: " + n+"");
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
				Document doc = dbf.newDocumentBuilder().parse(new InputSource(new StringReader(outputXMLMsg)));
				DigitalOnBoarding.mLogger.info("name is doc : "+ doc+"");
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
						String grid_table_flag = responseFileMap.get("Grid_Table");
						String Grid_col_tag = (String) responseFileMap.get("grid_table_xml_tags");
						String GridTags="AddrDet,OECDDet,KYCDet,FATCADet,CorpRelDet";
						//boolean Grid_dateColumn_found;
						DigitalOnBoarding.mLogger.info("Grid_col_tag"+Grid_col_tag);
						if(GridTags.contains(fielddbxml_tag)){
							DigitalOnBoarding.mLogger.info("Before clearing"+form_control+" grid");
							if(Operation_name.equalsIgnoreCase("Primary_CIF")){
								String firstName = getTagValue(outputXMLMsg, "FName");
								String lastName = getTagValue(outputXMLMsg, "LName");
								DigitalOnBoarding.mLogger.info("firstname is:"+firstName);
								DigitalOnBoarding.mLogger.info("lastName is:"+lastName);
								formObject.clear(form_control);
							}
							else{
								//for Supp and Guar
								CDOB_Common.clearSelectiveRows(Operation_name,form_control);
							}
							
							
						}
						String Fatcadocs="";
						if (parent_tag!=null && !"".equalsIgnoreCase(parent_tag))
						{                    
							for (int i = 0; i < nl.getLength(); i++)
							{
								nl.item(i);
								if(nl.item(i).getNodeName().equalsIgnoreCase(fielddbxml_tag))
								{
									DigitalOnBoarding.mLogger.info("RLOS Common# getOutputXMLValues()"+" fielddbxml_tag: "+fielddbxml_tag);
									String indirectMapping = (String) responseFileMap.get("InDirect_Mapping");
									String gridMapping = (String) responseFileMap.get("Grid_Mapping");

									if("Y".equalsIgnoreCase(gridMapping))
									{
										DigitalOnBoarding.mLogger.info("Grid_col_tag_arr"+"inside indirect mapping");
										if(Grid_col_tag.contains(",")){
											String Grid_col_tag_arr[]  =Grid_col_tag.split(",");
											//String grid_detail_str = nl.item(i).getNodeValue();
											NodeList childnode  = nl.item(i).getChildNodes();
											DigitalOnBoarding.mLogger.info("Grid_col_tag_arr"+"Grid_col_tag_arr: "+Grid_col_tag);   
											DigitalOnBoarding.mLogger.info("childnode"+"childnode"+childnode); 
											List<String> Grid_row = new ArrayList<String>(); 
											Grid_row.clear();

											String flaga="N";
											for(int k = 0;k<Grid_col_tag_arr.length;k++){

												for (int child_node_len = 0 ;child_node_len< childnode.getLength();child_node_len++){
													
													if("DocumentsCollected".equalsIgnoreCase(childnode.item(child_node_len).getNodeName())){
														DigitalOnBoarding.mLogger.info("Inside FATCA DocCollected tag "+childnode.item(child_node_len).getTextContent());
														Fatcadocs=childnode.item(child_node_len).getTextContent();
														//String[] docslist=docs.split("!");
														DigitalOnBoarding.mLogger.info("Inside FATCA DocCollected tag--->docslist: "+Fatcadocs);
													}
													
													if(childnode.item(child_node_len).getNodeName().equalsIgnoreCase(Grid_col_tag_arr[k])){
														DigitalOnBoarding.mLogger.info("child_node_len "+"getTextContent: "+childnode.item(child_node_len).getNodeName());
														DigitalOnBoarding.mLogger.info("child_node_len "+"getTextContent: "+childnode.item(child_node_len).getTextContent());
														if("AddrPrefFlag".equalsIgnoreCase(childnode.item(child_node_len).getNodeName()))
														{
															if("Y".equalsIgnoreCase(childnode.item(child_node_len).getTextContent()))
																Grid_row.add("true");

															else if("N".equalsIgnoreCase(childnode.item(child_node_len).getTextContent()))
																Grid_row.add("false");

														}	
														
														else if("FatcaReason".equalsIgnoreCase(childnode.item(child_node_len).getNodeName())){
															Grid_row.add(childnode.item(child_node_len).getTextContent().replaceAll("!",","));
														}
														
															else{
																DigitalOnBoarding.mLogger.info("Matching column  is found in call tag");
																UIComponent pComp =formObject.getComponent(form_control);
															if(grid_table_flag.equals("Y")){
																Grid_row.add(childnode.item(child_node_len).getTextContent());
															}
															else{
																if( pComp != null && pComp instanceof ListView )
																{			
																	//ListView objListView = ( ListView )pComp;
																	if(!((Column)(pComp.getChildren().get(k))).getFormat().equals("")){
																		DigitalOnBoarding.mLogger.info(form_control+"-->"+((Column)(pComp.getChildren().get(k))).getName()+" is of date type");
																		DigitalOnBoarding.mLogger.info("After converting date format-->value: "+common.Convert_dateFormat(childnode.item(child_node_len).getTextContent(), "yyyy-MM-dd", "dd/MM/yyyy"));
																		Grid_row.add(common.Convert_dateFormat(childnode.item(child_node_len).getTextContent(), "yyyy-MM-dd", "dd/MM/yyyy"));																
																	}
																	else{
																		
																		Grid_row.add(childnode.item(child_node_len).getTextContent());
																	}
															}
																}
															}													flaga="Y";
														break;
													}                                            
												}

												if("N".equalsIgnoreCase(flaga) ){
													DigitalOnBoarding.mLogger.info("value of flaga in if of for loop "+flaga);
													DigitalOnBoarding.mLogger.info("Grid_col_tag_arr[k]: "+Grid_col_tag_arr[k]);
													UIComponent pComp =formObject.getComponent(form_control);
													
													//below clause added by akshay for proc 11281
													if(Grid_col_tag_arr[k].equals("SELF-ATTEST FORM") || Grid_col_tag_arr[k].equals("ID DOC") || Grid_col_tag_arr[k].equals("W8")  || Grid_col_tag_arr[k].equals("W9")){
														DigitalOnBoarding.mLogger.info("DB column name is equal to fatca doc code");
														if(Fatcadocs.contains(Grid_col_tag_arr[k])){
															Grid_row.add("true");
														}
														else{
															Grid_row.add("false");
														}
													}
													
													else if(grid_table_flag.equals("Y")){
														Grid_row.add("NA");
													}
													
													else{
													if( pComp != null && pComp instanceof ListView )
													{			
														//ListView objListView = ( ListView )pComp;
														if(!((Column)(pComp.getChildren().get(k))).getFormat().equals("")){//date type column
															DigitalOnBoarding.mLogger.info(form_control+"-->"+((Column)(pComp.getChildren().get(k))).getName()+" is of date type");
															Grid_row.add("");
														}
														else{//text type column
															Grid_row.add("NA");
														}
													}
												}
											}
												flaga="N";

											}
											//Grid_row.add("true");
											Grid_row.add(formObject.getWFWorkitemName());
											String firstName = getTagValue(outputXMLMsg, "FName");
											String lastName = getTagValue(outputXMLMsg, "LName");
											DigitalOnBoarding.mLogger.info(form_control+"-->firstname is:"+firstName);
											DigitalOnBoarding.mLogger.info(form_control+"-->lastName is:"+lastName);
											if(Operation_name.equalsIgnoreCase("Primary_CIF")){
												Grid_row.add("P-"+firstName+" "+lastName);
											}
											else if(Operation_name.equalsIgnoreCase("Supplementary_Card_Details")){
												Grid_row.add("S-"+firstName+" "+lastName+"-"+formObject.getNGValue("SupplementCardDetails_passportNo"));
											}
											
											if("Y".equals(grid_table_flag)){
												String existingVal = formObject.getNGValue(form_control);
												if(existingVal!=null){
													//to handle commas in data start
													String str_newval = "";
													for (int Grid_row_num=0;Grid_row_num<Grid_row.size();Grid_row_num++){
														if (Grid_row_num==0){
															str_newval =Grid_row.get(Grid_row_num);
														}
														else{
															str_newval = str_newval + "~cas_sep~" + Grid_row.get(Grid_row_num);
														}															
													}
													//to handle commas in data start

													DigitalOnBoarding.mLogger.info( "New grid data to be added in Grid: "+str_newval );
													if(!"".equalsIgnoreCase(existingVal)){
														formObject.setNGValue(form_control,existingVal+"#"+str_newval);	
													}
													else{
														formObject.setNGValue(form_control,str_newval);
													}
													}
												}
											//Grid_col_tag_arr[k]
											//code to add row in grid. and pass Grid_row in that.

											else if("AddrDet".equalsIgnoreCase(fielddbxml_tag)){ 
												if(!"swift".equalsIgnoreCase(Grid_row.get(0))){	
													if("N".equals(grid_table_flag)){
												formObject.addItemFromList("cmplx_AddressDetails_cmplx_AddressGrid", Grid_row);
													}
													/*else if("Y".equals(grid_table_flag)){
														String existingVal = formObject.getNGValue(form_control);
														if(existingVal!=null){
															//to handle commas in data start
															String str_newval = "";
															for (int Grid_row_num=0;Grid_row_num<Grid_row.size();Grid_row_num++){
																if (Grid_row_num==0){
																	str_newval =Grid_row.get(Grid_row_num);
																}
																else{
																	str_newval = str_newval + "~cas_sep~" + Grid_row.get(Grid_row_num);
																}															
															}
															//to handle commas in data start

															CreditCard.mLogger.info( "New grid data to be added in Grid: "+str_newval );
															if(!"".equalsIgnoreCase(existingVal)){
																formObject.setNGValue(form_control,existingVal+"#"+str_newval);	
															}
															else{
																formObject.setNGValue(form_control,str_newval);
															}

														}
													}*/
												}
										}
										
											else if("OECDDet".equalsIgnoreCase(fielddbxml_tag)){ 
												/*if("Y".equals(grid_table_flag)){
													String existingVal = formObject.getNGValue(form_control);
													if(existingVal!=null){
														if(!"".equalsIgnoreCase(existingVal)){
															formObject.setNGValue(form_control,existingVal+"#"+Grid_row);	
														}
														else{
															formObject.setNGValue(form_control,Grid_row);
														}
													}
												}
												else{*/
												DigitalOnBoarding.mLogger.info("fieldxml_tag is:"+fielddbxml_tag+" form_control is:"+form_control);
												DigitalOnBoarding.mLogger.info("Grid row is:"+Grid_row);
												formObject.addItemFromList(form_control, Grid_row);
												//}
												//rowAddedinOECD =true;
											}
											else if("KYCDet".equalsIgnoreCase(fielddbxml_tag)){ 
												/*if("Y".equals(grid_table_flag)){
													String existingVal = formObject.getNGValue(form_control);
													if(existingVal!=null){
														if(!"".equalsIgnoreCase(existingVal)){
															formObject.setNGValue(form_control,existingVal+"#"+Grid_row);	
														}
														else{
															formObject.setNGValue(form_control,Grid_row);
														}
													}
												}
												else{*/
													DigitalOnBoarding.mLogger.info("fieldxml_tag is:"+fielddbxml_tag+" form_control is:"+form_control);
													DigitalOnBoarding.mLogger.info("Grid row is:"+Grid_row);
													formObject.addItemFromList(form_control, Grid_row);	
												//}
												
											}
											else if("FATCADet".equalsIgnoreCase(fielddbxml_tag)){ 
												/*if("Y".equals(grid_table_flag)){
													String existingVal = formObject.getNGValue(form_control);
													if(existingVal!=null){
														if(!"".equalsIgnoreCase(existingVal)){
															formObject.setNGValue(form_control,existingVal+"#"+Grid_row);	
														}
														else{
															formObject.setNGValue(form_control,Grid_row);
														}
													}
												}
												else{*/
												DigitalOnBoarding.mLogger.info("fieldxml_tag is:"+fielddbxml_tag+" form_control is:"+form_control);
												DigitalOnBoarding.mLogger.info("Grid row is:"+Grid_row);
												formObject.addItemFromList(form_control, Grid_row);
												//}
											}
											
											else { 
												formObject.addItemFromList(form_control, Grid_row);
											}
										}
										else{
											DigitalOnBoarding.mLogger.info("RLOS Common# getOutputXMLValues()"+ "Grid mapping is Y for this tag and grid_table_xml_tags column is blank tag name: "+ fielddbxml_tag);
										}

									}
									else if("Y".equalsIgnoreCase(indirectMapping)){
										DigitalOnBoarding.mLogger.info("Grid_col_tag_arr"+"inside indirect mapping");
										String col_list = "xmltag_name,tag_value,indirect_tag_list,indirect_formfield_list,indirect_child_tag,form_control,indirect_val,IS_Master,Master_Name";
										//code added
										if(!("".equalsIgnoreCase(Operation_name) || Operation_name!=null)){   
											DigitalOnBoarding.mLogger.info("inside if of operation"+"operation111"+Operation_name);
											DigitalOnBoarding.mLogger.info("inside if of operation"+"callName111"+col_name);
											sQuery = "SELECT "+col_list+" FROM NG_CDOB_Integration_Indirect_Response with (nolock) where Call_Name ='"+response+"' and XmlTag_Name = '"+nl.item(i).getNodeName()+"' and Operation_name='"+Operation_name+"'" ;
											DigitalOnBoarding.mLogger.info("RLOSCommon"+ "sQuery "+sQuery);
										}
										
										else{
											sQuery = "SELECT "+col_list+" FROM NG_CDOB_Integration_Indirect_Response with (nolock) where Call_Name ='"+response+"' and XmlTag_Name = '"+nl.item(i).getNodeName()+"'";
											DigitalOnBoarding.mLogger.info("#RLOS Common Inside indirectMapping "+ "query: "+sQuery);
										}
										List<List<String>> outputindirect=formObject.getNGDataFromDataCache(sQuery);
										DigitalOnBoarding.mLogger.info("#RLOS Common Inside indirectMapping test tanshu "+ "1");
										String col_list_arr[] = col_list.split(",");
										Map<String, String> gridResponseMap = new HashMap<String, String>();
										for(List<String> mygridlist:outputindirect)
										{
											DigitalOnBoarding.mLogger.info("#RLOS Common Inside indirectMapping test tanshu "+ "inside list loop");

											for(int x=0;x<col_list_arr.length;x++)
											{
												gridResponseMap.put(col_list_arr[x],mygridlist.get(x));
												DigitalOnBoarding.mLogger.info("#RLOS Common Inside indirectMapping "+ "inside put map"+x);
											}
											String xmltag_name = gridResponseMap.get("xmltag_name").toString();
											String tag_value = gridResponseMap.get("tag_value").toString();
											String indirect_tag_list = gridResponseMap.get("indirect_tag_list").toString();
											String indirect_formfield_list = gridResponseMap.get("indirect_formfield_list").toString();
											String indirect_child_tag = gridResponseMap.get("indirect_child_tag").toString();
											String indirect_form_control = gridResponseMap.get("form_control").toString();
											DigitalOnBoarding.mLogger.info("indirect_form_control in string"+indirect_form_control );
											String indirect_val = gridResponseMap.get("indirect_val").toString();
											String IS_Master = gridResponseMap.get("IS_Master").toString();
											DigitalOnBoarding.mLogger.info("#RLOS Common Inside indirectMapping "+ "IS_Master"+IS_Master);
											String Master_Name = gridResponseMap.get("Master_Name").toString();
											DigitalOnBoarding.mLogger.info("#RLOS Common Inside indirectMapping "+ "Master_Name"+Master_Name);
											DigitalOnBoarding.mLogger.info("#RLOS Common Inside indirectMapping "+ "all details fetched");
											if("Y".equalsIgnoreCase(IS_Master)){
												String code = nl.item(i).getTextContent();
												DigitalOnBoarding.mLogger.info("#RLOS Common Inside indirectMapping code:"+code);
												sQuery= "Select description from "+Master_Name+" with(nolock) where Code='"+code+"'";
												DigitalOnBoarding.mLogger.info("#RLOS Common Inside indirectMapping code:"+"Query to select master value: "+  sQuery);
												List<List<String>> query=formObject.getNGDataFromDataCache(sQuery);
												String value=query.get(0).get(0);
												DigitalOnBoarding.mLogger.info("#query.get(0).get(0)"+value );
												DigitalOnBoarding.mLogger.info("#RLOS Common Inside indirectMapping code:"+"Query to select master value: "+  sQuery);
												formObject.setNGValue(indirect_form_control,value);
												DigitalOnBoarding.mLogger.info("indirect_form_control value"+formObject.getNGValue(indirect_form_control));
											}

											else if(indirect_form_control!=null && !"".equalsIgnoreCase(indirect_form_control)){
												if( tag_value.equalsIgnoreCase(nl.item(i).getTextContent())){
													DigitalOnBoarding.mLogger.info("RLOS common: getOutputXMLValues"+"Indirect value for "+xmltag_name+" Indirect control name: "+indirect_form_control+" final value: "+indirect_val+" tag_value: "+tag_value);
													formObject.setNGValue(indirect_form_control,indirect_val,false);
												}
												// CreditCard.mLogger.info("form Control: "+ indirect_form_control + "indirect val: "+ indirect_val);

											}

											else{
												DigitalOnBoarding.mLogger.info("Grid_col_tag_arr"+"inside indirect mapping part2 else");
												if(indirect_tag_list!=null ){
													NodeList childnode  = nl.item(i).getChildNodes();
													DigitalOnBoarding.mLogger.info("childnode"+"childnode"+childnode);
													if(indirect_tag_list.contains(",")){
														DigitalOnBoarding.mLogger.info("#RLOS common indirect field values"+"inside indirect mapping part2 indirect_tag_list with ,");
														String indirect_tag_list_arr[] = indirect_tag_list.split(",");
														String indirect_formfield_list_arr[] = indirect_formfield_list.split(",");
														if(indirect_tag_list_arr.length == indirect_formfield_list_arr.length){
															for (int k=0; k<indirect_tag_list_arr.length;k++){
																DigitalOnBoarding.mLogger.info("#RLOS Common inside child node 1 "+ "node name : "+childnode.item(0).getNodeName() +" node data: "+childnode.item(1).getTextContent());
																if(tag_value.equalsIgnoreCase(childnode.item(0).getTextContent()) && indirect_child_tag.equalsIgnoreCase(childnode.item(0).getNodeName())){
																	//Ref. 1006
																	for (int child_node_len = 1 ;child_node_len< childnode.getLength();child_node_len++){
																		//Ref. 1006 end.
																		DigitalOnBoarding.mLogger.info("#RLOS common: "+ "child node IF: Child node value: "+ child_node_len+" name: "+ childnode.item(child_node_len).getNodeName()+" child node value: "+ childnode.item(child_node_len).getTextContent());

																		if(childnode.item(child_node_len).getNodeName().equalsIgnoreCase(indirect_tag_list_arr[k])){

																			DigitalOnBoarding.mLogger.info("child_node_len"+"getTextContent"+childnode.item(child_node_len).getNodeName());
																			DigitalOnBoarding.mLogger.info("child_node_len"+"getTextContent"+childnode.item(child_node_len).getTextContent());
																			DigitalOnBoarding.mLogger.info("RLOS common: getOutputXMLValues"+"");
																			DigitalOnBoarding.mLogger.info(""+indirect_formfield_list_arr[k]+" :"+childnode.item(child_node_len).getTextContent());
																			formObject.setNGValue(indirect_formfield_list_arr[k],childnode.item(child_node_len).getTextContent(),false);
																		}
																	}

																}
															}
														}
														else{
															DigitalOnBoarding.mLogger.info("RLOS common: getOutputXMLValues"+"Error as for :"+xmltag_name+" indirect_formfield_list and indirect_tag_list dosent match");
														} 
													}
													else{
														DigitalOnBoarding.mLogger.info("#RLOS common indirect field values"+"inside indirect mapping part2 indirect_tag_list without ,");
														for (int child_node_len = 0 ;child_node_len< childnode.getLength();child_node_len++){
															if(tag_value.equalsIgnoreCase(childnode.item(child_node_len).getTextContent()) && indirect_child_tag.equalsIgnoreCase(childnode.item(child_node_len).getNodeName())){
																if(childnode.item(child_node_len).getNodeName().equalsIgnoreCase(indirect_tag_list)){
																	DigitalOnBoarding.mLogger.info("child_node_len"+"getTextContent"+childnode.item(child_node_len).getNodeName());
																	DigitalOnBoarding.mLogger.info("child_node_len"+"getTextContent"+childnode.item(child_node_len).getTextContent());
																	DigitalOnBoarding.mLogger.info("RLOS common: getOutputXMLValues"+"");
																	DigitalOnBoarding.mLogger.info(""+indirect_formfield_list+" :"+childnode.item(child_node_len).getTextContent());
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
										DigitalOnBoarding.mLogger.info("check14 " +"check");
										tagValue = CDOB_Common.getTagValue(outputXMLMsg,nl.item(i).getNodeName());
										DigitalOnBoarding.mLogger.info("Node value "+"tagValue:"+tagValue);
										DigitalOnBoarding.mLogger.info("Node form_control "+"form_control:"+ form_control);

										DigitalOnBoarding.mLogger.info("$$tagValue NN  "+tagValue);
										DigitalOnBoarding.mLogger.info("$$form_control  NN "+form_control);
										formObject.setNGValue(form_control,tagValue,false);
									}
								}
							}
						}
						//till for loop
					}
				}
			}

		}
		catch(Exception e)
		{
			DigitalOnBoarding.mLogger.info("Exception occured in getOutputXMLValues:  "+printException(e));

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
			DigitalOnBoarding.mLogger.info("Inside Dectech from Eligibility");
			ngejbclient = NGEjbClient.getSharedInstance();
			if(outputResponse.indexOf("<PM_Reason_Codes>")>-1 && outputResponse.indexOf("<PM_Outputs>")>-1 ){
				DigitalOnBoarding.mLogger.info("inside if PM_codes");
				//Below code added by nikhil for DOM parsing Of dectech
				String Application_output = outputResponse.substring(outputResponse.lastIndexOf("<Application>"),outputResponse.lastIndexOf("</Application>")+14);
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
				DocumentBuilder builder = factory.newDocumentBuilder();
				InputSource is = new InputSource(new StringReader(Application_output));
				Document doc = builder.parse(is);
				doc.getDocumentElement().normalize();

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
				String Category="";
				int a=0,b=0,c=0;
				FormReference formObject = FormContext.getCurrentInstance().getFormReference();
				String strOutputXml = "";
				String sGeneralData = formObject.getWFGeneralData();
				DigitalOnBoarding.mLogger.info("$$outputXMLMsg "+"inside outpute get sGeneralData"+sGeneralData);
				double cac_calc_limit=0.0;
				String cac_calc_limit_str = null;
				String cabinetName = sGeneralData.substring(sGeneralData.indexOf("<EngineName>")+12,sGeneralData.indexOf("</EngineName>")); 
				String sessionId = sGeneralData.substring(sGeneralData.indexOf("<DMSSessionId>")+14,sGeneralData.indexOf("</DMSSessionId>"));
				String jtsIp = sGeneralData.substring(sGeneralData.indexOf("<JTSIP>")+7,sGeneralData.indexOf("</JTSIP>")); ;
				String jtsPort = sGeneralData.substring(sGeneralData.indexOf("<JTSPORT>")+9,sGeneralData.indexOf("</JTSPORT>")); ;
				String Output_Affordable_EMI="";
				String combined_limit="";
				String Output_CPV_Waiver="";
				//Setting the value in ELIGANDPROD info
				if (doc.getElementsByTagName("Output_TAI").getLength()>0){
					Output_TAI = doc.getElementsByTagName("Output_TAI").item(0).getTextContent();
					if (Output_TAI!=null){
						try{
							formObject.setNGValue("cmplx_Liability_New_TAI", Output_TAI,false);
							formObject.setNGValue("cmplx_EligibilityAndProductInfo_FinalTAI", Output_TAI,false);

						}
						catch (Exception e){
							DigitalOnBoarding.mLogger.info("Dectech error"+ "Exception:"+e.getMessage());
						}


					}
					DigitalOnBoarding.mLogger.info("$$outputXMLMsg "+"inside outpute get Output_TAI"+Output_TAI);
				}

				boolean DecFragVis=formObject.isVisible("DecisionHistory_Frame1");
				DigitalOnBoarding.mLogger.info("$$outputXMLMsg "+"inside outpute get Output_TAI"+DecFragVis);
				if (DecFragVis==false){
					//formObject.fetchFragment("DecisionHistory", "DecisionHistory", "q_DecisionHistory");
					new CDOB_Common().expandDecision();
					formObject.fetchFragment("DecisionHistory", "DecisionHistory", "q_DecisionHistory");
					new CDOB_Common().loadInDecGrid();


				}

				if (doc.getElementsByTagName("Output_Delegation_Authority").getLength()>0){
					Output_Delegation_Authority = doc.getElementsByTagName("Output_Delegation_Authority").item(0).getTextContent();
					DigitalOnBoarding.mLogger.info("$$outputXMLMsg "+"inside outpute get Output_Existing_DBR"+Output_Eligible_Amount);
					formObject.setNGValue("cmplx_DEC_HighDeligatinAuth", Output_Delegation_Authority,false);
				}
				//Added by aman for PROC-2535
				if (doc.getElementsByTagName("Output_Accommodation_Allowance").getLength()>0){
					Output_Accommodation_Allowance = doc.getElementsByTagName("Output_Accommodation_Allowance").item(0).getTextContent();
					DigitalOnBoarding.mLogger.info("$$outputXMLMsg "+"inside outpute get Output_Accommodation_Allowance"+Output_Accommodation_Allowance);
					formObject.setNGValue("cmplx_IncomeDetails_CompanyAcc", Output_Accommodation_Allowance,false);
				}
				//Added by aman for PROC-2535
				if (doc.getElementsByTagName("Output_Decision").getLength()>0){
					Output_Decision = doc.getElementsByTagName("Output_Decision").item(0).getTextContent();
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
							formObject.setNGValue("cmplx_DEC_DectechDecision", Output_Decision,false);
							formObject.setNGValue("Dectech_Decision", Output_Decision,false);
						}
						catch (Exception e){
							DigitalOnBoarding.mLogger.info("Dectech error"+ "Exception:"+e.getMessage());
						}
					}
					DigitalOnBoarding.mLogger.info("$$outputXMLMsg "+"inside outpute get Output_TAI"+Output_TAI);
				}

				if (doc.getElementsByTagName("Output_Eligible_Cards").getLength()>0){
					try{
						String Output_Eligible_Cards = doc.getElementsByTagName("Output_Eligible_Cards").item(0).getTextContent();
						Output_Eligible_Cards=Output_Eligible_Cards.substring(2, Output_Eligible_Cards.length()-2);
						//below code added b y akshay on 5/6/18 for proc 9941
						String queryForExistingCards="Select Card_product,final_limit from ng_rlos_IGR_Eligibility_CardLimit with(nolock) where child_wi='"+formObject.getWFWorkitemName()+"' and Cardproductselect='true'";
						DigitalOnBoarding.mLogger.info( "queryForExistingCards:"+queryForExistingCards);
						List<List<String>> SelectedCards=formObject.getDataFromDataSource(queryForExistingCards);
						DigitalOnBoarding.mLogger.info( "SelectedCards:"+SelectedCards);
						Map<String,String> SelectedCardsMap= new HashMap<String, String>();
						Map<String,String> CardShieldMap= new HashMap<String, String>();
						for(int i=0;i<SelectedCards.size();i++){
							SelectedCardsMap.put(SelectedCards.get(i).get(0), SelectedCards.get(i).get(1));
							if("true".equalsIgnoreCase(SelectedCards.get(i).get(2))){
								CardShieldMap.put(SelectedCards.get(i).get(0), SelectedCards.get(i).get(2));
							}
						}
						DigitalOnBoarding.mLogger.info("SelectedCardsMap"+SelectedCardsMap);
						DigitalOnBoarding.mLogger.info("SelectedCardsMap"+CardShieldMap);
						//ended by akshay
						String strInputXML =CDOB_Common.ExecuteQuery_APdelete("ng_rlos_IGR_Eligibility_CardLimit","child_wi ='" + formObject.getWFWorkitemName() + "'",cabinetName,sessionId);
						//strOutputXml = WFCallBroker.execute(strInputXML,jtsIp,Integer.parseInt(jtsPort),1);
						strOutputXml = ngejbclient.makeCall(jtsIp, jtsPort, "WebSphere", strInputXML);
						//String SelectedCards = "";
						//String CRNGrid="cmplx_CardDetails_cmplx_CardCRNDetails";
						DigitalOnBoarding.mLogger.info("Output_Eligible_Cards"+ "after removing the starting and Trailing:"+Output_Eligible_Cards);
						String[] Output_Eligible_Cards_Arr=Output_Eligible_Cards.split("\\},\\{");
						//added By Tarang as per drop 4 point 22 started on 19/02/2018
						if(Output_Eligible_Cards_Arr.length>0)
						{
							/*if(formObject.getLVWRowCount(CRNGrid)>0){
								for(int i=0;i<formObject.getLVWRowCount(CRNGrid);i++){
									if(formObject.getNGValue(CRNGrid,i,CC_Comn.returnGridColumnIndex(CRNGrid,NGFUserResourceMgr_CreditCard.getGlobalVar("CRN_ApplicantType"))).equalsIgnoreCase("Primary"))
									{
										SelectedCards+=formObject.getNGValue(CRNGrid,i,CC_Comn.returnGridColumnIndex(CRNGrid,NGFUserResourceMgr_CreditCard.getGlobalVar("CRN_CardProduct")));
									}
								}
							}*/

							for (int i=0; i<Output_Eligible_Cards_Arr.length;i++){
								DigitalOnBoarding.mLogger.info("Output_Eligible_Cards_Arr[i]:"+Output_Eligible_Cards_Arr[i]);
								String[] Output_Eligible_Cards_Array=Output_Eligible_Cards_Arr[i].split(",");
								DigitalOnBoarding.mLogger.info("Output_Eligible_Cards"+ "Output_Eligible_Cards_Array:"+Output_Eligible_Cards_Array);
								DigitalOnBoarding.mLogger.info("Output_Eligible_Cards_Arr.length():"+Output_Eligible_Cards_Arr.length);
								if(Output_Eligible_Cards_Array.length==3){
									String[] Card_Prod=Output_Eligible_Cards_Array[0].split(":");
									String[] Limit=Output_Eligible_Cards_Array[1].split(":");
									String[] flag=Output_Eligible_Cards_Array[2].split(":");
									String Card_Product= Card_Prod[1].substring(1,Card_Prod[1].length()-1);
									String LIMIT= Limit[1];
									String FLAG= flag[1].substring(1,flag[1].length()-1);
									String selectedCardProduct="false";
									String CardShieldProduct="false";
									String final_limit="";
									String combined_limitquery="select COMBINEDLIMIT_ELIGIBILITY from ng_master_cardProduct with (nolock)  where CODE = '"+Card_Product+"'" ;
									DigitalOnBoarding.mLogger.info( "combined_limitquery:"+combined_limitquery);
									List<List<String>> combined_limitqueryXML = formObject.getNGDataFromDataCache(combined_limitquery);
									if (!combined_limitqueryXML.isEmpty()){
										combined_limit= combined_limitqueryXML.get(0).get(0);
									}	
									if(SelectedCardsMap.size()>0 && SelectedCardsMap.containsKey(Card_Product)){
										selectedCardProduct="true";
										final_limit=SelectedCardsMap.get(Card_Product);
									}
									if(CardShieldMap.size()>0 && CardShieldMap.containsKey(Card_Product)){
										CardShieldProduct="true";
									}
									DigitalOnBoarding.mLogger.info("Output_Eligible_Cards"+ "Card_Prod:"+Card_Prod[1]);
									DigitalOnBoarding.mLogger.info("Output_Eligible_Cards"+ "Limit:"+Limit[1]);
									DigitalOnBoarding.mLogger.info("Output_Eligible_Cards"+ "flag:"+flag[1]);
									DigitalOnBoarding.mLogger.info("Output_Eligible_Cards"+ "selectedCardProduct:"+selectedCardProduct);
									DigitalOnBoarding.mLogger.info("Output_Eligible_Cards"+ "final_limit:"+final_limit);
									DigitalOnBoarding.mLogger.info("Output_Eligible_Cards"+ "CardShieldProduct: "+CardShieldProduct);
									
									//String query="INSERT INTO ng_rlos_IGR_Eligibility_CardLimit VALUES ('"+Card_Product+"','"+LIMIT+"','','"+ formObject.getWFWorkitemName() +"','"+ FLAG +"')";
									String query="INSERT INTO ng_rlos_IGR_Eligibility_CardLimit(Card_product,Eligible_limit,wi_name,child_wi,flag,combined_limit,Cardproductselect,final_limit,CardShield) VALUES ('"+Card_Product+"','"+LIMIT+"','"+formObject.getNGValue("parent_WIName")+"','"  + formObject.getWFWorkitemName() +"','"+ FLAG +"','"+ combined_limit +"','"+ selectedCardProduct+"','"+ final_limit+"','"+ CardShieldProduct+"')";
									DigitalOnBoarding.mLogger.info("Output_Eligible_Cards"+ "QUERY:"+query);
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
		    					   strInputXML =CC_Comn.ExecuteQuery_APdelete("ng_rlos_IGR_Eligibility_CardProduct","WI_NAME ='" + formObject.getWFWorkitemName() + "'",cabinetName,sessionId);
		    					  strOutputXml = WFCallBroker.execute(strInputXML,jtsIp,Integer.parseInt(jtsPort),1);
		    					   query="INSERT INTO ng_rlos_IGR_Eligibility_CardLimit("+values[0]+") VALUES ('"+values[1]+"')";
		    					  CreditCard.mLogger.info("Output_Eligible_Cards"+ "QUERY:"+query);
		    			    	  formObject.saveDataIntoDataSource(query);
		    				  }*/
								}
							}
						}
						else{
							formObject.setNGValue("is_cc_waiver_require", "Y",false);
							
						}
						//ended By Tarang as per drop 4 point 22 started on 19/02/2018
					}
					catch(Exception e){
						DigitalOnBoarding.mLogger.info("RLOSCommon"+ "Exception occurred in elig dectech"+CC_Comn.printException(e));

					}
				}	
				if (doc.getElementsByTagName("Output_Final_DBR").getLength()>0){

					Output_Final_DBR = doc.getElementsByTagName("Output_Final_DBR").item(0).getTextContent();
					if (Output_Final_DBR!=null){
						formObject.setNGValue("cmplx_EligibilityAndProductInfo_FinalDBR", Output_Final_DBR,false);
					}
					DigitalOnBoarding.mLogger.info("$$outputXMLMsg "+"inside outpute get Output_Final_DBR"+Output_Final_DBR);
				}
				if (doc.getElementsByTagName("Output_Interest_Rate").getLength()>0){
					Output_Interest_Rate = doc.getElementsByTagName("Output_Interest_Rate").item(0).getTextContent();
					if (Output_Interest_Rate!=null){
						formObject.setNGValue("cmplx_EligibilityAndProductInfo_InterestRate", Output_Interest_Rate,false);
					}
					DigitalOnBoarding.mLogger.info("$$outputXMLMsg "+"inside outpute get Output_Interest_Rate"+Output_Interest_Rate);

				}
				//Setting the value in ELIGANDPROD info
				//Setting the value in lIABILITY info
				if (doc.getElementsByTagName("Output_Existing_DBR").getLength()>0){
					Output_Existing_DBR = doc.getElementsByTagName("Output_Existing_DBR").item(0).getTextContent();
					if (Output_Existing_DBR!=null){
						formObject.setNGValue("cmplx_Liability_New_DBR", Output_Existing_DBR,false);
					}
					DigitalOnBoarding.mLogger.info("$$outputXMLMsg "+"inside outpute get Output_Existing_DBR"+Output_Existing_DBR);

				}
				if (doc.getElementsByTagName("Output_Affordable_EMI").getLength()>0){
					Output_Affordable_EMI = doc.getElementsByTagName("Output_Affordable_EMI").item(0).getTextContent();
					DigitalOnBoarding.mLogger.info("$$outputXMLMsg "+"inside outpute get Output_Affordable_EMI"+Output_Affordable_EMI);

				}
				try{
					double tenor=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor"));
					double RateofInt=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate")==null||formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate").equalsIgnoreCase("")?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate"));
					double out_aff_emi=Double.parseDouble(Output_Affordable_EMI);
					cac_calc_limit=Math.round(Cas_Limit(out_aff_emi,RateofInt,tenor));
					DigitalOnBoarding.mLogger.info("$$outputXMLMsg "+"inside outpute get sGeneralData"+tenor+" "+RateofInt+" "+out_aff_emi+" "+cac_calc_limit);
				}
				catch(Exception e){
					DigitalOnBoarding.mLogger.info("$$Exception occured in card elig of decesion: "+e.getMessage());
				}

				if (doc.getElementsByTagName("Output_Net_Salary_DBR").getLength()>0){
					Output_Net_Salary_DBR = doc.getElementsByTagName("Output_Net_Salary_DBR").item(0).getTextContent();
					if (Output_Net_Salary_DBR!=null){
						formObject.setNGValue("cmplx_Liability_New_DBRNet", Output_Net_Salary_DBR,false);
					}
					DigitalOnBoarding.mLogger.info("$$outputXMLMsg "+"inside outpute get Output_Net_Salary_DBR"+Output_Net_Salary_DBR);

				}
				//Setting the value in lIABILITY info
				//Setting the value in creditCard iFrame
//sagarika//sagarika for CPV decision NOT apllicable if cpv wavier is Y 
				if (doc.getElementsByTagName("Output_CPV_Waiver").getLength()>0){
//sagarika 
					Output_CPV_Waiver = doc.getElementsByTagName("Output_CPV_Waiver").item(0).getTextContent();
					if (Output_CPV_Waiver!=null){
						if(Output_CPV_Waiver.equalsIgnoreCase("Y"))
						{	
						DigitalOnBoarding.mLogger.info("Output_CPV_Waiver"+Output_CPV_Waiver);
						formObject.setNGValue("CPV_WAVIER",Output_CPV_Waiver);
						formObject.setNGValue("CPV_WAIVER_DECTECH",Output_CPV_Waiver);
						formObject.setNGValue("cmplx_CustDetailVerification_Decision","Not Applicable");
						//String is_cpv_wav="Y";
						DigitalOnBoarding.mLogger.info("CPV_WAVIER"+formObject.getNGValue("CPV_WAVIER"));
					}
						else
						{
							formObject.setNGValue("CPV_WAVIER",Output_CPV_Waiver);
							formObject.setNGValue("CPV_WAIVER_DECTECH",Output_CPV_Waiver);
							formObject.setNGValue("CPV_REQUIRED","Y");
							DigitalOnBoarding.mLogger.info("CPV_WAVIER"+formObject.getNGValue("CPV_WAVIER"));	
						}
						
					DigitalOnBoarding.mLogger.info("test 111 waiver flag "+formObject.getNGValue("CPV_WAIVER_DECTECH"));
					DigitalOnBoarding.mLogger.info("$$outputXMLMsg "+"inside outpute get Output_Final_DBR"+Output_Final_DBR);
				}
				}

				if (doc.getElementsByTagName("output_accomodation").getLength()>0){
					output_accomodation = doc.getElementsByTagName("output_accomodation").item(0).getTextContent();
					DigitalOnBoarding.mLogger.info("$$outputXMLMsg "+"inside outpute get Output_Existing_DBR"+output_accomodation);
					if (output_accomodation!=null){
						formObject.setNGValue("cmplx_IncomeDetails_CompanyAcc", output_accomodation,false);
					}
				}
				if (outputResponse.contains("Grade")){
					Grade = outputResponse.substring(outputResponse.indexOf("<Grade>")+7,outputResponse.indexOf("</Grade>")); ;
					if (Grade!=null){
						//commented by saurabh on 29th May wrt JIRA-10051. Confirmed with Srinidhi this field not to populate in Decision history and on CAM.
						//formObject.setNGValue("cmplx_DEC_ScoreGrade", Grade);
					}
					DigitalOnBoarding.mLogger.info("$$outputXMLMsg "+"inside outpute get Grade"+Grade);

				}
				if (doc.getElementsByTagName("Output_Eligible_Amount").getLength()>0){
					Output_Eligible_Amount = doc.getElementsByTagName("Output_Eligible_Amount").item(0).getTextContent();
					DigitalOnBoarding.mLogger.info("$$outputXMLMsg "+"inside outpute get Output_Existing_DBR"+Output_Eligible_Amount);

				}
				//Setting the value in creditCard iFrame


				//String strInputXML = ExecuteQuery_APUpdate("ng_rlos_IGR_Eligibility_CardProduct","Product,Card_Product,Eligible_Card,Decision,Declined_Reason,wi_name","'Credit Card','"+subProd+"','"+Output_Eligible_Amount+"','Declined','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+formObject.getWFWorkitemName()+"'","WI_NAME ='" + formObject.getWFWorkitemName() + "'",cabinetName,sessionId);
				//CreditCard.mLogger.info("$$Value set for DECTECH->>"+"UpdateinputXML is:"+UpdateinputXML);
				String strInputXML =CDOB_Common.ExecuteQuery_APdelete("ng_rlos_IGR_Eligibility_CardProduct","Child_Wi ='" + formObject.getWFWorkitemName() + "'",cabinetName,sessionId);

				//strOutputXml = WFCallBroker.execute(strInputXML,jtsIp,Integer.parseInt(jtsPort),1);
				strOutputXml = ngejbclient.makeCall(jtsIp, jtsPort, "WebSphere", strInputXML);
				DigitalOnBoarding.mLogger.info("$$Value set for DECTECH->>"+"strOutputXml is:"+strOutputXml);

				String mainCode=strOutputXml.substring(strOutputXml.indexOf("<MainCode>")+10,strOutputXml.indexOf("</MainCode>"));
				String rowUpdated=strOutputXml.substring(strOutputXml.indexOf("<Output>")+8,strOutputXml.indexOf("</Output>"));
				DigitalOnBoarding.mLogger.info("$$Value set for DECTECH->>"+"mainCode,rowUpdated is: "+mainCode+", "+rowUpdated);

				outputResponse = outputResponse.substring(outputResponse.indexOf("<ProcessManagerResponse>")+24, outputResponse.indexOf("</ProcessManagerResponse>"));
				outputResponse = "<?xml version=\"1.0\"?><Response>" + outputResponse;
				outputResponse = outputResponse+"</Response>";
				DigitalOnBoarding.mLogger.info("$$outputResponse "+"inside outpute get outputResponse"+outputResponse);
				DocumentBuilderFactory factory_1 = DocumentBuilderFactory.newInstance();
				factory_1.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
				DocumentBuilder builder_1 = factory_1.newDocumentBuilder();
				InputSource is_1 = new InputSource(new StringReader(outputResponse));

				Document doc_1 = builder_1.parse(is_1);
				doc_1.getDocumentElement().normalize();

				DigitalOnBoarding.mLogger.info("Root element :" +doc.getDocumentElement().getNodeName());

				NodeList nList = doc_1.getElementsByTagName("PM_Reason_Codes");
				//added by sagarika for Deviation CR
				int Approve_counter=0,Refer_decline_counter=0;
				for (int temp = 0; temp < nList.getLength(); temp++) {
					String Reason_Decision="";
					Node nNode = nList.item(temp);


					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						//CreditCard.mLogger.info("Student roll no : " + eElement.getAttribute("rollno"));
						DigitalOnBoarding.mLogger.info("$$outputResponse "+"inside outpute get Decision_Objective"+ eElement.getElementsByTagName("Decision_Objective").item(0).getTextContent());
						DigitalOnBoarding.mLogger.info("$$outputResponse "+"inside outpute get Decision_Sequence_Number"+eElement.getElementsByTagName("Decision_Sequence_Number").item(0).getTextContent());
						DigitalOnBoarding.mLogger.info("$$outputResponse "+"inside outpute get Sequence_Number"+eElement.getElementsByTagName("Sequence_Number").item(0).getTextContent());

						DigitalOnBoarding.mLogger.info("$$outputResponse "+"inside outpute get Reason_Description"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent());
						String subProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2);//2


						ReasonCode= eElement.getElementsByTagName("Reason_Code").item(0).getTextContent();
						Reason_Decision = eElement.getElementsByTagName("Reason_Decision").item(0).getTextContent() ;
						//added by nikhil 18-06-2018
						if (eElement.getElementsByTagName("Category").getLength()!=0){

							Category = eElement.getElementsByTagName("Category").item(0).getTextContent() ;
							DigitalOnBoarding.mLogger.info("DectechCategory  Categoryis"+Category);

							if (Category.contains("A -")){
								a++;
							}
							else if (Category.contains("B -")){
								b++;
							}
							else{
								c++;
							}
						}
						//added by akshay on 12/4/18 for proc 6975
						String Reason_Description=eElement.getElementsByTagName("Reason_Description").item(0).getTextContent() ;
						DigitalOnBoarding.mLogger.info("Reason_Description before replacing &gt;---->"+Reason_Description);
						Reason_Description=Reason_Description.replaceAll("&gt;", ">");
						Reason_Description=Reason_Description.replaceAll("&lt;", "<");
						DigitalOnBoarding.mLogger.info("Reason_Description after replacing &gt;---->"+Reason_Description);

						try{
							if(temp==0){

								DeviationCode=ReasonCode+"-"+Reason_Description;
							}
							else
							{
								if(!DeviationCode.contains(ReasonCode))
								{
									DeviationCode=DeviationCode+","+ReasonCode+"-"+Reason_Description;
								}
							}
						}
						catch(Exception e){

						}DigitalOnBoarding.mLogger.info("$$outputResponse "+"Value of Reason_Decision"+Reason_Decision);
						if (temp==0){
							if ("D".equalsIgnoreCase(Reason_Decision)){
								Refer_decline_counter++;
								squery="Insert into ng_rlos_IGR_Eligibility_CardProduct (S_No,Product,Card_Product,Eligible_Card,Decision,Declined_Reason,Delegation_Authorithy,Score_grade,child_wi,CACCalculatedLimit) values('"+temp+"','Credit Card','"+subProd+"','"+Output_Eligible_Amount+"','Declined','"+Reason_Description+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"','"+cac_calc_limit+"')";
							}
							if ("R".equalsIgnoreCase(Reason_Decision)){
								Refer_decline_counter++;
								squery="Insert into ng_rlos_IGR_Eligibility_CardProduct (S_No,Product,Card_Product,Eligible_Card,Decision,Deviation_Code_Refer,Delegation_Authorithy,Score_grade,child_wi,CACCalculatedLimit) values('"+temp+"','Credit Card','"+subProd+"','"+Output_Eligible_Amount+"','Refer','"+Reason_Description+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"','"+cac_calc_limit+"')";
							}
							if("A".equalsIgnoreCase(Reason_Decision))
							{
								if(Refer_decline_counter<=0)
								{
									squery="Insert into ng_rlos_IGR_Eligibility_CardProduct (S_no,Product,Card_Product,Eligible_Card,Decision,Deviation_Code_Refer,Delegation_Authorithy,Score_grade,child_wi,CACCalculatedLimit,category) values('"+temp+"','Credit Card','"+subProd+"','"+Output_Eligible_Amount+"','Approve','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"','"+cac_calc_limit_str+"','"+Category+"')";
									Approve_counter++;
								}
								else
								{
									squery="";
								}
							}
						}
						else{
							if ("D".equalsIgnoreCase(Reason_Decision)){
								Refer_decline_counter++;//sagarika for change of deviation logic
								squery="Insert into ng_rlos_IGR_Eligibility_CardProduct (S_No,Product,Card_Product,Eligible_Card,Decision,Declined_Reason,Delegation_Authorithy,Score_grade,child_wi,CACCalculatedLimit) values('"+temp+"','Credit Card','"+subProd+"','"+Output_Eligible_Amount+"','Declined','"+Reason_Description+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"','')";
							}
							if ("R".equalsIgnoreCase(Reason_Decision)){
								Refer_decline_counter++;
								squery="Insert into ng_rlos_IGR_Eligibility_CardProduct (S_No,Product,Card_Product,Eligible_Card,Decision,Deviation_Code_Refer,Delegation_Authorithy,Score_grade,child_wi,CACCalculatedLimit) values('"+temp+"','Credit Card','"+subProd+"','"+Output_Eligible_Amount+"','Refer','"+Reason_Description+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"','')";
							}
							if ("A".equalsIgnoreCase(Reason_Decision))
							{
								if(Refer_decline_counter<=0)//sagarika for change of deviation logic
								{
									squery="Insert into ng_rlos_IGR_Eligibility_CardProduct (S_No,Product,Card_Product,Eligible_Card,Decision,Deviation_Code_Refer,Delegation_Authorithy,Score_grade,child_wi,CACCalculatedLimit) values('"+temp+"','Credit Card','"+subProd+"','"+Output_Eligible_Amount+"','Approve','"+Reason_Description+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"','"+cac_calc_limit+"')";
									Approve_counter++;
								}
								else{//duplicate entries 
									squery="";
								}
							}
						}
						DigitalOnBoarding.mLogger.info("$$outputResponse "+"Squery is"+squery);
						if(!"".equalsIgnoreCase(squery)){
							formObject.saveDataIntoDataSource(squery);
						}
						

					}
					if(temp==(nList.getLength()-1))
					{//sagarika for change of deviation logic
						if(Approve_counter>0 && Refer_decline_counter>0)
						{
							String Query="Delete from ng_rlos_IGR_Eligibility_CardProduct where Decision='Approve' AND Child_Wi='"+formObject.getWFWorkitemName()+"'";
							formObject.saveDataIntoDataSource(Query);

						}
					}
				}
				if(DeviationCode.contains(",A999-System Approve") || DeviationCode.contains("A999-System Approve,"))
				{
					DeviationCode=DeviationCode.replaceAll(",A999-System Approve", "");
					DeviationCode=DeviationCode.replaceAll("A999-System Approve,", "");
				}
				formObject.setNGValue("cmplx_DEC_DeviationCode", DeviationCode); 
				if (a!=0){
					formObject.setNGValue("DectechCategory", "A",false);
				}
				else if ((b !=0)&&(a ==0)){
					formObject.setNGValue("DectechCategory", "B",false);
				}
				else{
					formObject.setNGValue("DectechCategory", "C",false);
				}
				if ("A".equalsIgnoreCase(formObject.getNGValue("DectechCategory"))){
					formObject.setLocked("cmplx_DEC_Decision", true);
					formObject.setNGValue("cmplx_DEC_Decision", "Reject",false);
					formObject.setLocked("DecisionHistory_dec_reason_code", false);
					//below code added by nikhil for PCSP-498
					formObject.setLocked("cmplx_DEC_ReferTo",false);	
				}
				//below code added by nikhil for PCSP-58
				else
				{
					if(!"--Select--".equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_Decision"))){
						formObject.setNGValue("cmplx_DEC_Decision","--Select--");
					}
					formObject.setLocked("cmplx_DEC_Decision", false);
					formObject.setLocked("DecisionHistory_dec_reason_code", true);
				}
			}
		}
		catch(Exception e){
			DigitalOnBoarding.mLogger.info("Dectech error"+ "Exception:"+e.getMessage());

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
			DigitalOnBoarding.mLogger.info("CC_Common"+"final_rate_new 1ST pmt11 : " + pmt);
		}
		catch(Exception e){
			pmt=0;
		}
		return pmt;
		
	}

}
