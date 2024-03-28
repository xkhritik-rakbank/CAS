package com.newgen.omniforms.user;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.newgen.custom.XMLParser;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.wfdesktop.xmlapi.WFCallBroker;

public class PL_Integration_Output implements Serializable{

	private static final long serialVersionUID = 1L;
	static Logger mLogger=PersonalLoanS.mLogger;
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to set INTEGRATION RESPONSE data on form
	 
	***********************************************************************************  */

	public void valueSetIntegration(String outputResponse)
	{
		mLogger.info("RLOSCommon valueSetCustomer"+ "Inside valueSetCustomer():");
		String outputXMLHead = "";
		String outputXMLMsg = "";
		String returnDesc = "";
		String returnCode = "";
		String response= "";
		String Operation_name="";
		String Call_type="";
		XMLParser objXMLParser = new XMLParser();
		try
		{
			if(outputResponse.indexOf("<EE_EAI_HEADER>")>-1)
			{
				outputXMLHead=outputResponse.substring(outputResponse.indexOf("<EE_EAI_HEADER>"),outputResponse.indexOf("</EE_EAI_HEADER>")+16);
				mLogger.info("RLOSCommon valueSetCustomer"+ "outputXMLHead");
			}
			objXMLParser.setInputXML(outputXMLHead);
			if(outputXMLHead.indexOf("<MsgFormat>")>-1)
			{
				response= outputXMLHead.substring(outputXMLHead.indexOf("<MsgFormat>")+11,outputXMLHead.indexOf("</MsgFormat>"));
				mLogger.info("$$response "+response);
			}
			if(outputXMLHead.indexOf("<ReturnDesc>")>-1)
			{
				returnDesc= outputXMLHead.substring(outputXMLHead.indexOf("<ReturnDesc>")+12,outputXMLHead.indexOf("</ReturnDesc>"));
				mLogger.info("$$returnDesc "+returnDesc);
			}
			if(outputXMLHead.indexOf("<ReturnCode>")>-1)
			{
				returnCode= outputXMLHead.substring(outputXMLHead.indexOf("<ReturnCode>")+12,outputXMLHead.indexOf("</ReturnCode>"));
				mLogger.info("$$returnCode "+returnCode);
			}

			if(outputResponse.indexOf("<MQ_RESPONSE_XML>")>-1)
			{
				outputXMLMsg=outputResponse.substring(outputResponse.indexOf("<MQ_RESPONSE_XML>")+17,outputResponse.indexOf("</MQ_RESPONSE_XML>"));
				
				mLogger.info("$$outputXMLMsg getOutputXMLValues"+"check inside getOutputXMLValues");
				
				getOutputXMLValues(outputXMLMsg,response,Operation_name);
				mLogger.info("$$outputXMLMsg "+"outputXMLMsg");
			}
			//ended by me
			// Changes done to set the value coming from dectech in the grid
			Call_type= outputResponse.substring(outputResponse.indexOf("<CallType>")+10,outputResponse.indexOf("</CallType>"));
			if	(Call_type!= null){
				if ("CA".equals(Call_type)){
					dectechfromeligbility(outputResponse);
				}
				if ("PM".equals(Call_type)){
					dectechfromeligbility(outputResponse);
				}
			}
			// Changes done to set the value coming from dectech in the grid end 
		}
		catch(Exception e)
		{            
			mLogger.info("Exception occured in valueSetCustomer method:  "+e.getMessage());
			mLogger.info("Exception occured in valueSetCustomer method: "+ e.getMessage());
			PLCommon.printException(e);
		}
	}
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to parse RESPONSE data
	 
	***********************************************************************************  */

	public void getOutputXMLValues(String outputXMLMsg, String response,String Operation_name) throws ParserConfigurationException
	{
		String sQuery = "";
		String tagValue = "";
		try
		{   
			mLogger.info("inside getOutputXMLValues"+"inside outputxml");
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String col_name = "Form_Control,Parent_Tag_Name,XmlTag_Name,InDirect_Mapping,Grid_Mapping,Grid_Table,grid_table_xml_tags";

			
			//code added here
			if(!("".equalsIgnoreCase(Operation_name) || Operation_name.equalsIgnoreCase(null))){   
				mLogger.info("inside if of operation"+"operation111"+Operation_name);
				mLogger.info("inside if of operation"+"callName111"+col_name);
				sQuery = "SELECT "+col_name+" FROM NG_PL_Integration_Field_Response where Call_Name ='"+response+"' and Operation_name='"+Operation_name+"'" ;
				mLogger.info("RLOSCommon"+ "sQuery "+sQuery);
			}
			else{
				sQuery = "SELECT "+col_name+" FROM NG_PL_Integration_Field_Response where Call_Name ='"+response+"'";
			}

			//ended here
			List<List<String>> outputTableXML=formObject.getNGDataFromDataCache(sQuery);


			String[] col_name_arr = col_name.split(",");

			mLogger.info("$$outputTableXML"+outputTableXML.get(0).get(0)+outputTableXML.get(0).get(1)+outputTableXML.get(0).get(2)+outputTableXML.get(0).get(3));
			int n=outputTableXML.size();
			mLogger.info("outputTableXML size: " + n+"");

			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(outputXMLMsg)));
			mLogger.info("name is doc : "+ doc+"");
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
					String form_control = responseFileMap.get("Form_Control");
					String parent_tag = responseFileMap.get("Parent_Tag_Name");
					String fielddbxml_tag =  responseFileMap.get("XmlTag_Name");
					String Grid_col_tag =  responseFileMap.get("grid_table_xml_tags");
					mLogger.info(" Grid_col_tag"+"Grid_col_tag"+Grid_col_tag);
					if (parent_tag!=null && !"".equalsIgnoreCase(parent_tag))
					{                    
						for (int i = 0; i < nl.getLength(); i++)
						{
							if(nl.item(i).getNodeName().equalsIgnoreCase(fielddbxml_tag))
							{
								mLogger.info("RLOS Common# getOutputXMLValues()"+" fielddbxml_tag: "+fielddbxml_tag);
								String indirectMapping =  responseFileMap.get("InDirect_Mapping");
								String gridMapping = responseFileMap.get("Grid_Mapping");

								if("Y".equalsIgnoreCase(gridMapping))
								{
									mLogger.info("Grid_col_tag_arr"+"inside indirect mapping");
									if(Grid_col_tag.contains(",")){
										String[] Grid_col_tag_arr  =Grid_col_tag.split(",");
										
										NodeList childnode  = nl.item(i).getChildNodes();
										mLogger.info("Grid_col_tag_arr"+"Grid_col_tag_arr: "+Grid_col_tag);   
										mLogger.info("childnode"+"childnode"+childnode); 
										List<String> Grid_row = new ArrayList<String>(); 
										Grid_row.clear();

										String flaga="N";
										for(int k = 0;k<Grid_col_tag_arr.length;k++){

											for (int child_node_len = 0 ;child_node_len< childnode.getLength();child_node_len++){
												if(childnode.item(child_node_len).getNodeName().equalsIgnoreCase(Grid_col_tag_arr[k])){
													mLogger.info("child_node_len "+"getTextContent: "+childnode.item(child_node_len).getNodeName());
													mLogger.info("child_node_len "+"getTextContent: "+childnode.item(child_node_len).getTextContent());
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
												mLogger.info("child_node_len "+"Grid_col_tag_arr[k]: "+Grid_col_tag_arr[k]);
												
												Grid_row.add("NA");

											}
											flaga="N";

										}
										
										Grid_row.add(formObject.getWFWorkitemName());
										//Grid_col_tag_arr[k]
										//code to add row in grid. and pass Grid_row in that.
										mLogger.info("RLOS Common# getOutputXMLValues()"+ "$$AKSHAY$$List to be added in address grid: "+ Grid_row.toString());

										if("AddrDet".equalsIgnoreCase(fielddbxml_tag)){ 
											formObject.addItemFromList("cmplx_AddressDetails_cmplx_AddressGrid", Grid_row);
											formObject.RaiseEvent("WFSave");
											mLogger.info("of Part match grid"+"after WFS Save");
										}
										mLogger.info("RLOS Common# getOutputXMLValues()"+ "$$AKSHAY$$List to be added in Lien details grid: "+ Grid_row.toString());

										mLogger.info("PL Common# getOutputXMLValues()"+ "$$AKSHAY$$List to be added : in PartMatch Details"+ Grid_row.toString());
										if("PersonDetails".equalsIgnoreCase(fielddbxml_tag)){ 
											formObject.addItemFromList("cmplx_PartMatch_cmplx_Partmatch_grid", Grid_row);
											formObject.RaiseEvent("WFSave");
											mLogger.info("of Part match grid"+"after WFS Save");
											
										}
									}
									else{
										mLogger.info("RLOS Common# getOutputXMLValues()"+ "Grid mapping is Y for this tag and grid_table_xml_tags column is blank tag name: "+ fielddbxml_tag);
									}

								}
								else if("Y".equalsIgnoreCase(indirectMapping)){
									mLogger.info("Grid_col_tag_arr"+"inside indirect mapping");
									String col_list = "xmltag_name,tag_value,indirect_tag_list,indirect_formfield_list,indirect_child_tag,form_control,indirect_val,IS_Master,Master_Name";
									//code added
									if(!("".equalsIgnoreCase(Operation_name) || Operation_name.equalsIgnoreCase(null))){   
										mLogger.info("inside if of operation"+"operation111"+Operation_name);
										mLogger.info("inside if of operation"+"callName111"+col_name);
										sQuery = "SELECT "+col_list+" FROM NG_PL_Integration_Indirect_Response where Call_Name ='"+response+"' and XmlTag_Name = '"+nl.item(i).getNodeName()+"' and Operation_name='"+Operation_name+"'" ;
										mLogger.info("RLOSCommon"+ "sQuery "+sQuery);
									}

									else{
										sQuery = "SELECT "+col_list+" FROM NG_PL_Integration_Indirect_Response where Call_Name ='"+response+"' and XmlTag_Name = '"+nl.item(i).getNodeName()+"'";
										mLogger.info("#RLOS Common Inside indirectMapping "+ "query: "+sQuery);
									}
									List<List<String>> outputindirect=formObject.getNGDataFromDataCache(sQuery);
									mLogger.info("#RLOS Common Inside indirectMapping test tanshu "+ "1");
									String[] col_list_arr = col_list.split(",");
									Map<String, String> gridResponseMap = new HashMap<String, String>();
									for(List<String> mygridlist:outputindirect)
									{
										mLogger.info("#RLOS Common Inside indirectMapping test tanshu "+ "inside list loop");

										for(int x=0;x<col_list_arr.length;x++)
										{
											gridResponseMap.put(col_list_arr[x],mygridlist.get(x));
											mLogger.info("#RLOS Common Inside indirectMapping "+ "inside put map"+x);
										}
										String xmltag_name = gridResponseMap.get("xmltag_name");
										String tag_value = gridResponseMap.get("tag_value");
										String indirect_tag_list = gridResponseMap.get("indirect_tag_list");
										String indirect_formfield_list = gridResponseMap.get("indirect_formfield_list");
										String indirect_child_tag = gridResponseMap.get("indirect_child_tag");
										String indirect_form_control = gridResponseMap.get("form_control");
										mLogger.info("indirect_form_control in string"+indirect_form_control );
										String indirect_val = gridResponseMap.get("indirect_val");
										String IS_Master = gridResponseMap.get("IS_Master");
										mLogger.info("#RLOS Common Inside indirectMapping "+ "IS_Master"+IS_Master);
										String Master_Name = gridResponseMap.get("Master_Name");
										mLogger.info("#RLOS Common Inside indirectMapping "+ "Master_Name"+Master_Name);
										mLogger.info("#RLOS Common Inside indirectMapping "+ "all details fetched");
										if("Y".equalsIgnoreCase(IS_Master)){
											String code = nl.item(i).getTextContent();
											mLogger.info("#RLOS Common Inside indirectMapping code:"+code);
											sQuery= "Select description from "+Master_Name+" where Code='"+code+"'";
											mLogger.info("#RLOS Common Inside indirectMapping code:"+"Query to select master value: "+  sQuery);
											List<List<String>> query=formObject.getNGDataFromDataCache(sQuery);
											String value=query.get(0).get(0);
											mLogger.info("#query.get(0).get(0)"+value );
											mLogger.info("#RLOS Common Inside indirectMapping code:"+"Query to select master value: "+  sQuery);
											formObject.setNGValue(indirect_form_control,value,false);
											mLogger.info("indirect_form_control value"+formObject.getNGValue(indirect_form_control));
										}

										else if(indirect_form_control!=null && !"".equalsIgnoreCase(indirect_form_control)){
											if( nl.item(i).getTextContent().equalsIgnoreCase(tag_value)){
												mLogger.info("RLOS common: getOutputXMLValues"+"Indirect value for "+xmltag_name+" Indirect control name: "+indirect_form_control+" final value: "+indirect_val+" tag_value: "+tag_value);
												formObject.setNGValue(indirect_form_control,indirect_val,false);
											}


										}

										else{
											mLogger.info("Grid_col_tag_arr"+"inside indirect mapping part2 else");
											if(indirect_tag_list!=null ){
												NodeList childnode  = nl.item(i).getChildNodes();
												mLogger.info("childnode"+"childnode"+childnode);
												if(indirect_tag_list.contains(",")){
													mLogger.info("#RLOS common indirect field values"+"inside indirect mapping part2 indirect_tag_list with +");
													String[] indirect_tag_list_arr = indirect_tag_list.split(",");
													String[] indirect_formfield_list_arr = indirect_formfield_list.split(",");
													if(indirect_tag_list_arr.length == indirect_formfield_list_arr.length){
														for (int k=0; k<indirect_tag_list_arr.length;k++){
															mLogger.info("#RLOS Common inside child node 1 "+ "node name : "+childnode.item(0).getNodeName() +" node data: "+childnode.item(1).getTextContent());
															if(tag_value.equalsIgnoreCase(childnode.item(1).getTextContent()) && indirect_child_tag.equalsIgnoreCase(childnode.item(1).getNodeName())){

																//code changes to parse indirect output tag from 1st index rather than 2
																for (int child_node_len = 1 ;child_node_len< childnode.getLength();child_node_len++){

																	mLogger.info("#RLOS common: "+ "child node IF: Child node value: "+ child_node_len+" name: "+ childnode.item(child_node_len).getNodeName()+" child node value: "+ childnode.item(child_node_len).getTextContent());

																	if(childnode.item(child_node_len).getNodeName().equalsIgnoreCase(indirect_tag_list_arr[k])){

																		mLogger.info("child_node_len"+"getTextContent"+childnode.item(child_node_len).getNodeName());
																		mLogger.info("child_node_len"+"getTextContent"+childnode.item(child_node_len).getTextContent());
																		mLogger.info("RLOS common: getOutputXMLValues"+"");
																		mLogger.info(""+indirect_formfield_list_arr[k]+" :"+childnode.item(child_node_len).getTextContent());
																		formObject.setNGValue(indirect_formfield_list_arr[k],childnode.item(child_node_len).getTextContent(),false);
																	}
																}

															}
														}
													}
													else{
														mLogger.info("RLOS common: getOutputXMLValues"+"Error as for :"+xmltag_name+" indirect_formfield_list and indirect_tag_list dosent match");
													} 
												}
												else{
													mLogger.info("#RLOS common indirect field values"+"inside indirect mapping part2 indirect_tag_list without +");
													for (int child_node_len = 0 ;child_node_len< childnode.getLength();child_node_len++){
														if(tag_value.equalsIgnoreCase(childnode.item(child_node_len).getTextContent()) && indirect_child_tag.equalsIgnoreCase(childnode.item(child_node_len).getNodeName())){
															if(childnode.item(child_node_len).getNodeName().equalsIgnoreCase(indirect_tag_list)){
																mLogger.info("child_node_len"+"getTextContent"+childnode.item(child_node_len).getNodeName());
																mLogger.info("child_node_len"+"getTextContent"+childnode.item(child_node_len).getTextContent());
																mLogger.info("RLOS common: getOutputXMLValues"+"");
																mLogger.info(""+indirect_formfield_list+" :"+childnode.item(child_node_len).getTextContent());
																formObject.setNGValue(indirect_formfield_list,childnode.item(child_node_len).getTextContent(),false);
															}
														}

													}
												}


											}
										}     
									}



								}
								if("N".equalsIgnoreCase(indirectMapping) && "N".equalsIgnoreCase(gridMapping))
								{    
									mLogger.info("check14 " +"check");
									tagValue =getTagValue(outputXMLMsg,nl.item(i).getNodeName());
									mLogger.info("Node value "+"tagValue:"+tagValue);
									mLogger.info("Node form_control "+"form_control:"+ form_control);

									mLogger.info("$$tagValue NN  "+tagValue);
									mLogger.info("$$form_control  NN "+form_control);
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
			mLogger.info("Exception occured in getOutputXMLValues:  "+e.getMessage());
			PLCommon.printException(e);

		}
	}

	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to get XML tag value
	 
	***********************************************************************************  */
	public  String getTagValue(String xml, String tag)
	{   
		try{
			Document doc = getDocument(xml);
			if(doc!=null){
				NodeList nodeList = doc.getElementsByTagName(tag);

				int length = nodeList.getLength();


				if (length > 0) {
					Node node =  nodeList.item(0);

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
		catch(Exception e){
			mLogger.info("Exception occurred in getTagValue()");
			PLCommon.printException(e);
		}
		return "";
	}

	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to return Document Builder Factory Object
	 
	***********************************************************************************  */
	public Document getDocument(String xml)
	{
		try{
			// Step 1: create a DocumentBuilderFactory
			DocumentBuilderFactory dbf =
					DocumentBuilderFactory.newInstance();

			// Step 2: create a DocumentBuilder
			DocumentBuilder db = dbf.newDocumentBuilder();

			// Step 3: parse the input file to get a Document object
			return db.parse(new InputSource(new StringReader(xml)));
			 
		}
		catch(Exception e){
			mLogger.info("Exception occurred in getTagValue()");
			PLCommon.printException(e);
			return null;
		}

	} 
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Parse Dedupe Summary
	 
	***********************************************************************************  */

	public static  void parseDedupe_summary(String outxml){

		outxml=outxml.substring(outxml.indexOf("<MQ_RESPONSE_XML>")+17,outxml.indexOf("</MQ_RESPONSE_XML>"));


		String tagName= "Customer";		
		String subTagName= "Document,EmailAddress,PhoneFax,StatusInfo";
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int dedupe_row_count = formObject.getLVWRowCount("cmplx_PartMatch_cmplx_Partmatch_grid");
		if(dedupe_row_count>0){
			for(int i=0;i<dedupe_row_count;i++)
			{
				try {
					formObject.removeItem("cmplx_PartMatch_cmplx_Partmatch_grid", dedupe_row_count);
				} catch (Exception e) {
					mLogger.info("PL Common parseDedupe_summary method"+ "Exception occured while removing data from Grid"+e.getMessage());
					PLCommon.printException(e);
				}
			}

		}

		String [] valueArr;
		Map<String, String> tagValuesMap;		 
		tagValuesMap=getTagData_dedupeSummary(outxml,tagName,subTagName);
		Map<String, String> map = tagValuesMap;
		for (Map.Entry<String, String> entry : map.entrySet())
		{
			valueArr=entry.getValue().split("~");
			String[] col_name = valueArr[0].split(":,#:");
			String[] col_val = valueArr[1].split(":,#:");
			String cif_id = "NA";
			String CIF_Status= "NA";
			String First_name= "NA";
			String Last_name= "NA";
			String Full_name= "NA";
			String Passport_no= "NA";
			String old_passport_no= "NA";
			String visa_no= "NA";
			String mobile_no1= "NA";
			String mobile_no2= "NA";
			String dOB= "NA";
			String Eid= "NA";
			String DL= "NA";
			String nationality= "NA";
			String company_name= "NA";
			String TL_no= "NA";
			List<String> Grid_row = new ArrayList<String>();
			for(int i =0; i<col_name.length;i++){
				if("CIFID".equalsIgnoreCase(col_name[i]))
					cif_id = col_val[i];
				else if("Suspended".equalsIgnoreCase(col_name[i])){
					CIF_Status = col_val[i];

					if("Y".equalsIgnoreCase(CIF_Status))
						CIF_Status="N";
					else
						CIF_Status="Y";
				}
				else if("FirstName".equalsIgnoreCase(col_name[i]))
					First_name = col_val[i];
				else if("LastName".equalsIgnoreCase(col_name[i]))
					Last_name = col_val[i];
				else if("FullName".equalsIgnoreCase(col_name[i]))
					Full_name = col_val[i];
				else if("PPT".equalsIgnoreCase(col_name[i]))
					Passport_no = col_val[i];
				else if("OPPT".equalsIgnoreCase(col_name[i]))
					old_passport_no = col_val[i];
				else if("VISA".equalsIgnoreCase(col_name[i]))
					visa_no = col_val[i];
				else if("CELLPH1".equalsIgnoreCase(col_name[i]))
					mobile_no1 = col_val[i];
				else if("HOMEPH1".equalsIgnoreCase(col_name[i]))
					mobile_no2 = col_val[i];
				else if("DateOfBirth".equalsIgnoreCase(col_name[i])){
					dOB = "";
					if (col_val[i]!=null && !"".equalsIgnoreCase(col_val[i])){
						SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
						SimpleDateFormat sdf2=new SimpleDateFormat("dd/MM/yyyy");
						try{
							dOB = sdf2.format(sdf1.parse(col_val[i]));
							mLogger.info("PLCommon "+ "Dedupe Summary Date chabge: "+dOB);
						}
						catch(Exception e){
							mLogger.info("PL Common"+ "Exception while parsing DOB in Dedupe summary"+e.getMessage());
							PLCommon.printException(e);
							dOB="";
						}
					}
					else{
						dOB="";
					}
				}
				else if("EMID".equalsIgnoreCase(col_name[i]))
					Eid = col_val[i];
				else if("DRILV".equalsIgnoreCase(col_name[i]))
					DL = col_val[i];
				else if("Nationality".equalsIgnoreCase(col_name[i]))
					nationality = col_val[i];

				else if("TDLIC".equalsIgnoreCase(col_name[i]))
					TL_no = col_val[i];
			}
			Grid_row.add(cif_id);
			Grid_row.add(CIF_Status);
			Grid_row.add(First_name);
			Grid_row.add(Last_name);
			Grid_row.add(Full_name);
			Grid_row.add(Passport_no);
			Grid_row.add(old_passport_no);
			Grid_row.add(visa_no);
			Grid_row.add(mobile_no1);
			Grid_row.add(mobile_no2);

			Grid_row.add(dOB);		// disha P2 - 26 - dob missing in part match grid
			Grid_row.add(Eid);
			Grid_row.add(DL);
			Grid_row.add(nationality);
			Grid_row.add(company_name);
			Grid_row.add(TL_no);
			Grid_row.add(formObject.getWFWorkitemName());
			mLogger.info("PL common parse dedupe summary"+ Grid_row.toString());
			formObject.addItemFromList("cmplx_PartMatch_cmplx_Partmatch_grid", Grid_row);
		}

	}
	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to get Dedupe Summary TAG DATA
	 
	***********************************************************************************  */

	public static Map<String, String> getTagData_dedupeSummary(String parseXml,String tagName,String sub_tag){

		Map<String, String> tagValuesMap= new LinkedHashMap<String, String>(); 
		try {
			mLogger.info("getTagDataParent_deep jsp: parseXml: "+parseXml);
			mLogger.info("getTagDataParent_deep jsp: tagName: "+tagName);
			mLogger.info("getTagDataParent_deep jsp: subTagName: "+sub_tag);
			
			InputStream is = new ByteArrayInputStream(parseXml.getBytes());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();

			NodeList nList_loan = doc.getElementsByTagName(tagName);
			for(int i = 0 ; i<nList_loan.getLength();i++){
				String col_name = "";
				String col_val ="";
				NodeList ch_nodeList = nList_loan.item(i).getChildNodes();
				String id = ch_nodeList.item(0).getTextContent();
				for(int ch_len = 0 ;ch_len< ch_nodeList.getLength(); ch_len++){
					if(sub_tag.contains(ch_nodeList.item(ch_len).getNodeName())){
						NodeList sub_ch_nodeList =  ch_nodeList.item(ch_len).getChildNodes();
						if("".equalsIgnoreCase(col_name)){
							col_name = sub_ch_nodeList.item(0).getTextContent();
							col_val = sub_ch_nodeList.item(1).getTextContent();
						}
						else{
							col_name = col_name+":,#:"+sub_ch_nodeList.item(0).getTextContent();
							col_val = col_val+":,#:"+sub_ch_nodeList.item(1).getTextContent();
						}

					}
					else if("PersonDetails".equalsIgnoreCase(ch_nodeList.item(ch_len).getNodeName())){
						NodeList sub_ch_nodeList =  ch_nodeList.item(ch_len).getChildNodes();
						for (int k =0;k<sub_ch_nodeList.getLength();k++){
							col_name = col_name+":,#:"+sub_ch_nodeList.item(k).getNodeName();
							col_val = col_val+":,#:"+sub_ch_nodeList.item(k).getTextContent()+"";
						}
					}
					else if("ContactDetails".equalsIgnoreCase(ch_nodeList.item(ch_len).getNodeName())){
						NodeList sub_ch_nodeList =  ch_nodeList.item(ch_len).getChildNodes();
						for (int k =0;k<sub_ch_nodeList.getLength();k++){
							NodeList contact_sub_ch_nodeList =  sub_ch_nodeList.item(k).getChildNodes();
							for (int con_ch_len=0; con_ch_len<contact_sub_ch_nodeList.getLength();con_ch_len++){
								col_name = col_name+":,#:"+contact_sub_ch_nodeList.item(0).getTextContent();
								col_val = col_val+":,#:"+contact_sub_ch_nodeList.item(1).getTextContent();
							}
						}
					} 
					else{
						if("".equalsIgnoreCase(col_name)){
							col_name = ch_nodeList.item(ch_len).getNodeName();
							col_val = ch_nodeList.item(ch_len).getTextContent();
						}
						else{
							col_name = col_name+":,#:"+ch_nodeList.item(ch_len).getNodeName();
							col_val = col_val+":,#:"+ch_nodeList.item(ch_len).getTextContent();
						}

					}

				}
				mLogger.info("insert/update for id: "+id);
				mLogger.info("insert/update cal_name: "+col_name);
				mLogger.info("insert/update col_val: "+col_val);
				if(!"".equalsIgnoreCase(col_name))
					tagValuesMap.put(id, col_name+"~"+col_val);	
			}

		} catch (Exception e) {

			PLCommon.printException(e);
			mLogger.info("Exception occured in getTagDataParent method:  "+e.getMessage());
		}
		return tagValuesMap;
	}

	
	/*          Function Header:
	 
	**********************************************************************************
	 
	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED
	 
	 
	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to set DECTECH data in grid
	 
	***********************************************************************************  */

	//Setting the value in the grid for DECTECH
	public static  void dectechfromeligbility(String outputResponse){
		try{
			if(outputResponse.indexOf("<PM_Reason_Codes>")>-1 && outputResponse.indexOf("<PM_Outputs>")>-1 ){
				String squery="";
				String Output_TAI;
				String Output_Final_DBR;
				String Output_Decision;
				String Output_Existing_DBR ;
				String Output_Eligible_Amount="";
				String Output_Delegation_Authority="";
				String Grade;
				String Output_Interest_Rate;
				String Output_Net_Salary_DBR;
				String Output_Affordable_EMI="";
				double cac_calc_limit=0.0;
				String ReasonCode;
				String DeviationCode="";
				String Output_Accommodation_Allowance;
				FormReference formObject = FormContext.getCurrentInstance().getFormReference();
				String strOutputXml ;
				String sGeneralData = formObject.getWFGeneralData();
				mLogger.info("$$outputXMLMsg "+"inside outpute get sGeneralData"+sGeneralData);
				String output_accomodation;
				String cabinetName = sGeneralData.substring(sGeneralData.indexOf("<EngineName>")+12,sGeneralData.indexOf("</EngineName>")); 
				String sessionId = sGeneralData.substring(sGeneralData.indexOf("<DMSSessionId>")+14,sGeneralData.indexOf("</DMSSessionId>"));
				String jtsIp = sGeneralData.substring(sGeneralData.indexOf("<JTSIP>")+7,sGeneralData.indexOf("</JTSIP>"));
				String jtsPort = sGeneralData.substring(sGeneralData.indexOf("<JTSPORT>")+9,sGeneralData.indexOf("</JTSPORT>"));

				//Setting the value in ELIGANDPROD info
				if (outputResponse.contains("Output_TAI")){
					Output_TAI = outputResponse.substring(outputResponse.indexOf("<Output_TAI>")+12,outputResponse.indexOf("</Output_TAI>"));
					if (Output_TAI!=null){
						try{
							formObject.setNGValue("cmplx_Liability_New_TAI", Output_TAI);
							formObject.setNGValue("cmplx_EligibilityAndProductInfo_FinalTAI", Output_TAI);

						}
						catch (Exception e){
							mLogger.info("Dectech error"+ "Exception:"+e.getMessage());
						}


					}
					mLogger.info("$$outputXMLMsg "+"inside outpute get Output_TAI"+Output_TAI);
				}
				if (outputResponse.contains("Output_Delegation_Authority")){
					Output_Delegation_Authority = outputResponse.substring(outputResponse.indexOf("<Output_Delegation_Authority>")+29,outputResponse.indexOf("</Output_Delegation_Authority>"));
					mLogger.info("$$outputXMLMsg "+"inside outpute get Output_Delegation_Authority"+Output_Delegation_Authority);

				}
				boolean DecFragVis=formObject.isVisible("DecisionHistory_Frame1");
				mLogger.info("$$outputXMLMsg "+"inside outpute get Output_TAI"+DecFragVis);
				if (!DecFragVis){
					formObject.fetchFragment("DecisionHistory", "DecisionHistory", "q_DecisionHistory");
					formObject.setNGValue("cmplx_Decision_Highest_delegauth", Output_Delegation_Authority);
				}
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
							formObject.setNGValue("cmplx_Decision_Dectech_decsion", Output_Decision);
							formObject.setNGValue("cmplx_Decision_Highest_delegauth", Output_Delegation_Authority);

						}
						catch (Exception e){
							mLogger.info("Dectech error"+ "Exception:"+e.getMessage());
							PLCommon.printException(e);

						}


					}
					mLogger.info("$$outputXMLMsg "+"inside outpute get Output_Decision"+Output_Decision);
				}

				if (outputResponse.contains("output_accomodation")){
					output_accomodation = outputResponse.substring(outputResponse.indexOf("<output_accomodation>")+20,outputResponse.indexOf("</output_accomodation>"));
					mLogger.info("$$outputXMLMsg "+"inside outpute get Output_Existing_DBR"+Output_Eligible_Amount);
					if (output_accomodation!=null){
						formObject.setNGValue("cmplx_IncomeDetails_compaccAmt", output_accomodation);
					}
				}
				//Added by aman for PROC-2535
				if (outputResponse.contains("Output_Accommodation_Allowance")){
					Output_Accommodation_Allowance = outputResponse.substring(outputResponse.indexOf("<Output_Accommodation_Allowance>")+32,outputResponse.indexOf("</Output_Accommodation_Allowance>"));
					mLogger.info("$$outputXMLMsg "+"inside outpute get Output_Accommodation_Allowance"+Output_Accommodation_Allowance);
					formObject.setNGValue("cmplx_IncomeDetails_compaccAmt", Output_Accommodation_Allowance);
				}
				//Added by aman for PROC-2535

				if (outputResponse.contains("Output_Eligible_Cards")){
					try{
						String Output_Eligible_Cards = outputResponse.substring(outputResponse.indexOf("<Output_Eligible_Cards>")+23,outputResponse.indexOf("</Output_Eligible_Cards>"));
						Output_Eligible_Cards=Output_Eligible_Cards.substring(2, Output_Eligible_Cards.length()-2);
						String strInputXML =ExecuteQuery_APdelete("ng_rlos_IGR_Eligibility_CardLimit","WI_NAME ='" + formObject.getWFWorkitemName() + "'",cabinetName,sessionId);
						strOutputXml = WFCallBroker.execute(strInputXML,jtsIp,Integer.parseInt(jtsPort),1);

						mLogger.info("Output_Eligible_Cards"+ "after removing the starting and Trailing:"+Output_Eligible_Cards);
						String[] Output_Eligible_Cards_Arr=Output_Eligible_Cards.split("\\},\\{");
						for (int i=0; i<Output_Eligible_Cards_Arr.length;i++){
							String[] Output_Eligible_Cards_Array=Output_Eligible_Cards_Arr[i].split(",");
							mLogger.info("Output_Eligible_Cards"+ "Output_Eligible_Cards_Array:"+Output_Eligible_Cards_Array);
							String[] Card_Prod=Output_Eligible_Cards_Array[0].split(":");
							String[] Limit=Output_Eligible_Cards_Array[1].split(":");
							String[] flag=Output_Eligible_Cards_Array[2].split(":");
							String Card_Product= Card_Prod[1].substring(1,Card_Prod[1].length()-1);
							String LIMIT= Limit[1];
							String FLAG= flag[1].substring(1,flag[1].length()-1);

							mLogger.info("Output_Eligible_Cards"+ "Card_Prod:"+Card_Prod[1]);
							mLogger.info("Output_Eligible_Cards"+ "Limit:"+Limit[1]);
							mLogger.info("Output_Eligible_Cards"+ "flag:"+flag[1]);
							String query="INSERT INTO ng_rlos_IGR_Eligibility_CardLimit VALUES ('"+Card_Product+"','"+LIMIT+"','','"+ formObject.getWFWorkitemName() +"','"+ FLAG +"')";
							mLogger.info("Output_Eligible_Cards"+ "QUERY:"+query);
							formObject.saveDataIntoDataSource(query);

						}
					}
					catch(Exception e){
						mLogger.info("RLOSCommon"+ "Exception occurred in elig dectech");
						PLCommon.printException(e);

					}
				}	
				if (outputResponse.contains("Output_Final_DBR")){

					Output_Final_DBR = outputResponse.substring(outputResponse.indexOf("<Output_Final_DBR>")+18,outputResponse.indexOf("</Output_Final_DBR>"));
					if (Output_Final_DBR!=null){
						formObject.setNGValue("cmplx_EligibilityAndProductInfo_FinalDBR", Output_Final_DBR);
					}
					mLogger.info("$$outputXMLMsg "+"inside outpute get Output_Final_DBR"+Output_Final_DBR);
				}
				if (outputResponse.contains("Output_Interest_Rate")){
					Output_Interest_Rate = outputResponse.substring(outputResponse.indexOf("<Output_Interest_Rate>")+22,outputResponse.indexOf("</Output_Interest_Rate>"));
					if (Output_Interest_Rate!=null){
						formObject.setNGValue("cmplx_EligibilityAndProductInfo_InterestRate", Output_Interest_Rate);
					}
					mLogger.info("$$outputXMLMsg "+"inside outpute get Output_Interest_Rate"+Output_Interest_Rate);

				}
				//Setting the value in ELIGANDPROD info
				//Setting the value in lIABILITY info
				if (outputResponse.contains("Output_Existing_DBR")){
					Output_Existing_DBR = outputResponse.substring(outputResponse.indexOf("<Output_Existing_DBR>")+21,outputResponse.indexOf("</Output_Existing_DBR>"));
					if (Output_Existing_DBR!=null){
						formObject.setNGValue("cmplx_Liability_New_DBR", Output_Existing_DBR);
					}
					mLogger.info("$$outputXMLMsg "+"inside outpute get Output_Existing_DBR"+Output_Existing_DBR);

				}
				if (outputResponse.contains("Output_Affordable_EMI")){
					Output_Affordable_EMI = outputResponse.substring(outputResponse.indexOf("<Output_Affordable_EMI>")+23,outputResponse.indexOf("</Output_Affordable_EMI>"));
					mLogger.info("$$outputXMLMsg "+"inside outpute get Output_Affordable_EMI"+Output_Affordable_EMI);

				}
				try{
					double tenor=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor")==null||"".equalsIgnoreCase(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor"))?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor"));
					double RateofInt=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate")==null||"".equalsIgnoreCase(formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate"))?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate"));
					double out_aff_emi=Double.parseDouble(Output_Affordable_EMI);
					cac_calc_limit=Math.round(Cas_Limit(out_aff_emi,RateofInt,tenor));
					mLogger.info("$$outputXMLMsg "+"inside outpute get sGeneralData"+tenor+" "+RateofInt+" "+out_aff_emi+" "+cac_calc_limit);
				}
				catch(Exception e){
					PLCommon.printException(e);

				}

				if (outputResponse.contains("Output_Net_Salary_DBR")){
					Output_Net_Salary_DBR = outputResponse.substring(outputResponse.indexOf("<Output_Net_Salary_DBR>")+23,outputResponse.indexOf("</Output_Net_Salary_DBR>"));
					if (Output_Net_Salary_DBR!=null){
						formObject.setNGValue("cmplx_Liability_New_DBRNet", Output_Net_Salary_DBR);
					}
					mLogger.info("$$outputXMLMsg "+"inside outpute get Output_Net_Salary_DBR"+Output_Net_Salary_DBR);

				}
				//Setting the value in lIABILITY info
				//Setting the value in creditCard iFrame

				if (outputResponse.contains("Grade")){
					Grade = outputResponse.substring(outputResponse.indexOf("<Grade>")+7,outputResponse.indexOf("</Grade>"));
					if (Grade!=null){
						formObject.setNGValue("cmplx_Decision_score_grade", Grade);
					}
					mLogger.info("$$outputXMLMsg "+"inside outpute get Grade"+Grade);

				}
				if (outputResponse.contains("Output_Eligible_Amount")){
					Output_Eligible_Amount = outputResponse.substring(outputResponse.indexOf("<Output_Eligible_Amount>")+24,outputResponse.indexOf("</Output_Eligible_Amount>"));
					mLogger.info("$$outputXMLMsg "+"inside outpute get Output_Existing_DBR"+Output_Eligible_Amount);

				}
				//Setting the value in creditCard iFrame




				String strInputXML =ExecuteQuery_APdelete("ng_rlos_IGR_Eligibility_PersonalLoan","Child_WI ='" + formObject.getWFWorkitemName() + "'",cabinetName,sessionId);

				strOutputXml = WFCallBroker.execute(strInputXML,jtsIp,Integer.parseInt(jtsPort),1);
				mLogger.info("$$Value set for DECTECH->>"+"strOutputXml is:"+strOutputXml);

				String mainCode=strOutputXml.substring(strOutputXml.indexOf("<MainCode>")+10,strOutputXml.indexOf("</MainCode>"));
				String rowUpdated=strOutputXml.substring(strOutputXml.indexOf("<Output>")+8,strOutputXml.indexOf("</Output>"));
				mLogger.info("$$Value set for DECTECH->>"+"mainCode+rowUpdated is: "+mainCode+"+ "+rowUpdated);

				outputResponse = outputResponse.substring(outputResponse.indexOf("<ProcessManagerResponse>")+24, outputResponse.indexOf("</ProcessManagerResponse>"));
				outputResponse = "<?xml version=\"1.0\"?><Response>" + outputResponse;
				outputResponse = outputResponse+"</Response>";
				mLogger.info("$$outputResponse "+"inside outpute get outputResponse"+outputResponse);
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				InputSource is = new InputSource(new StringReader(outputResponse));

				Document doc = builder.parse(is);
				doc.getDocumentElement().normalize();

				mLogger.info("Root element :" +doc.getDocumentElement().getNodeName());

				NodeList nList = doc.getElementsByTagName("PM_Reason_Codes");
				mLogger.info("----------------------------");

				for (int temp = 0; temp < nList.getLength(); temp++) {
					String Reason_Decision;
					Node nNode = nList.item(temp);
					mLogger.info("\nCurrent Element :" + nNode.getNodeName());

					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;

						mLogger.info("$$outputResponse "+"inside outpute get Decision_Objective"+ eElement.getElementsByTagName("Decision_Objective").item(0).getTextContent());
						mLogger.info("$$outputResponse "+"inside outpute get Decision_Sequence_Number"+eElement.getElementsByTagName("Decision_Sequence_Number").item(0).getTextContent());
						mLogger.info("$$outputResponse "+"inside outpute get Sequence_Number"+eElement.getElementsByTagName("Sequence_Number").item(0).getTextContent());

						mLogger.info("$$outputResponse "+"inside outpute get Reason_Description"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent());
						String subProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 2);//2
						String appType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,4);

						String subprodquery="Select Description from ng_MASTER_SubProduct_PL where code='"+subProd+"'";
						String apptypequery="Select Description from ng_master_applicationtype where code='"+appType+"'";
						List<List<String>> subprodqueryXML = formObject.getDataFromDataSource(subprodquery);
						List<List<String>> apptypequeryXML = formObject.getDataFromDataSource(apptypequery);
						ReasonCode= eElement.getElementsByTagName("Reason_Code").item(0).getTextContent();
						Reason_Decision = eElement.getElementsByTagName("Reason_Decision").item(0).getTextContent() ;
						try{
							if(temp==0){

								DeviationCode=ReasonCode;}
							else{
								DeviationCode=DeviationCode+","+ReasonCode;}
						}
						catch(Exception e){
							PLCommon.printException(e);
						}
						mLogger.info("$$outputResponse "+"Value of Reason_Decision"+Reason_Decision);
						if (temp==0){
							if ("D".equalsIgnoreCase(Reason_Decision)){
								squery="Insert into ng_rlos_IGR_Eligibility_PersonalLoan (S_no,RequestedLoan,RequestedAppType,EligibleExposure,Decision,DeclinedReasonCode,DelegationAuthorithy,child_wi,CACCalculatedLimit) values('"+temp+"','"+subprodqueryXML.get(0).get(0)+"','"+apptypequeryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Declined','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+formObject.getWFWorkitemName()+"','"+cac_calc_limit+"')";
							}
							else if ("R".equalsIgnoreCase(Reason_Decision)){
								squery="Insert into ng_rlos_IGR_Eligibility_PersonalLoan (S_no,RequestedLoan,RequestedAppType,EligibleExposure,Decision,ReferReasoncode,DelegationAuthorithy,child_wi,CACCalculatedLimit) values('"+temp+"','"+subprodqueryXML.get(0).get(0)+"','"+apptypequeryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Refer','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+formObject.getWFWorkitemName()+"','"+cac_calc_limit+"')";
							}
							else if ("A".equalsIgnoreCase(Reason_Decision)){
								squery="Insert into ng_rlos_IGR_Eligibility_PersonalLoan (S_no,RequestedLoan,RequestedAppType,EligibleExposure,Decision,ReferReasoncode,DelegationAuthorithy,child_wi,CACCalculatedLimit) values('"+temp+"','"+subprodqueryXML.get(0).get(0)+"','"+apptypequeryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Approve','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+formObject.getWFWorkitemName()+"','"+cac_calc_limit+"')";
							}
						}
						else{
							if ("D".equalsIgnoreCase(Reason_Decision)){
								squery="Insert into ng_rlos_IGR_Eligibility_PersonalLoan (S_no,RequestedLoan,RequestedAppType,EligibleExposure,Decision,DeclinedReasonCode,DelegationAuthorithy,child_wi,CACCalculatedLimit) values('"+temp+"','"+subprodqueryXML.get(0).get(0)+"','"+apptypequeryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Declined','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+formObject.getWFWorkitemName()+"','')";
							}
							else if ("R".equalsIgnoreCase(Reason_Decision)){
								squery="Insert into ng_rlos_IGR_Eligibility_PersonalLoan (S_no,RequestedLoan,RequestedAppType,EligibleExposure,Decision,ReferReasoncode,DelegationAuthorithy,child_wi,CACCalculatedLimit) values('"+temp+"','"+subprodqueryXML.get(0).get(0)+"','"+apptypequeryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Refer','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+formObject.getWFWorkitemName()+"','')";
							}
							else if ("A".equalsIgnoreCase(Reason_Decision)){
								squery="Insert into ng_rlos_IGR_Eligibility_PersonalLoan (S_no,RequestedLoan,RequestedAppType,EligibleExposure,Decision,ReferReasoncode,DelegationAuthorithy,child_wi,CACCalculatedLimit) values('"+temp+"','"+subprodqueryXML.get(0).get(0)+"','"+apptypequeryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Approve','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+formObject.getWFWorkitemName()+"','')";
							}
						}



						mLogger.info("$$outputResponse "+"Squery is"+squery);
						formObject.saveDataIntoDataSource(squery);

					}
				}
				formObject.setNGValue("cmplx_Decision_Deviationcode", DeviationCode); 
			}
		}
		catch(Exception e){
			mLogger.info("Dectech error"+ "Exception:"+e.getMessage());
			PLCommon.printException(e);

		}

	}

	//Setting the value in the grid for DECTECH
	public static String ExecuteQuery_APdelete(String tableName,String sWhere, String cabinetName, String sessionId)
	{
		return "<?xml version=\"1.0\"?>"+
				"<APDelete_Input><Option>APDelete</Option>"+
				"<TableName>"+tableName+"</TableName>"+
				"<WhereClause>"+sWhere+"</WhereClause>"+
				"<EngineName>"+cabinetName+"</EngineName>"+
				"<SessionId>"+sessionId+"</SessionId>"+
				"</APDelete_Input>";
		
	}

	public static  double Cas_Limit(double aff_emi,double rate,double tenureMonths)
	{
		double pmt;
		try{
			double new_rate = (rate/100)/12;
			pmt = (aff_emi)*(1-Math.pow(1+new_rate,-tenureMonths))/new_rate;
			mLogger.info("final_rate_new 1ST pmt11 : " + pmt);
		}
		catch(Exception e){
			PLCommon.printException(e);
			pmt=0;
		}
		return pmt;

	}

}