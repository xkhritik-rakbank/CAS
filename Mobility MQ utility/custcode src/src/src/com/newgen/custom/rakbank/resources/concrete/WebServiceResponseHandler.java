/**--------------------------------------------------------------------------
 *        NEWGEN SOFTWARE TECHNOLOGIES LIMITED
 *    Group                     : AP2
 *    Product / Project         : NEMF 3.2/RAKBANK Implementation
 *    Module                    : Server Module
 *    File Name                 : WebServiceResponseHandler.java
 *    Author                    : Gaurav Sharma(gaurav-s)
 *    Date written (DD/MM/YYYY) : 25/03/2017
 *    Description               : This Class deals with parsing xmlResponse returned from service call
 * --------------------------------------------------------------------------
 *                   CHANGE HISTORY
 * ---------------------------------------------------------------------------
 * Date           Name      		Comment
 * ---------------------------------------------------------------------------
 * 
 * ---------------------------------------------------------------------------
 */
package com.newgen.custom.rakbank.resources.concrete;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.simple.JSONObject;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.newgen.custom.implementation.RakBankIntegrations;
import com.newgen.mcap.core.external.logging.concrete.LogMe;

public class WebServiceResponseHandler{

	public  JSONObject getOutputXMLValues(String outputXMLMsg, String callname,Connection conn) throws ParserConfigurationException
	{
		String sQuery = "";
		String tagValue = "";
		JSONObject toreturn=new JSONObject();
		try
		{ 
			//added mobility column name
			String col_name = "Mobility_Form_Control,Parent_Tag_Name,XmlTag_Name,InDirect_Mapping,Grid_Mapping,Grid_Table,grid_table_xml_tags";
			sQuery = "SELECT "+col_name+" FROM NG_RLOS_Integration_Field_Response where Call_Name ='"+callname+"'";
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"QUERY TO FIRE: "+sQuery);
			List<List<String>> outputTableXML=queryExecute(sQuery,conn);
			String[] col_name_arr = col_name.split(",");
			int n=outputTableXML.size();
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(outputXMLMsg)));
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
					String form_control = (String) responseFileMap.get("Mobility_Form_Control");
					String parent_tag = (String) responseFileMap.get("Parent_Tag_Name");
					String fielddbxml_tag = (String) responseFileMap.get("XmlTag_Name");
					String Grid_col_tag = (String) responseFileMap.get("grid_table_xml_tags");
					if (parent_tag!=null && !parent_tag.equalsIgnoreCase(""))
					{                   
						for (int i = 0; i < nl.getLength(); i++)
						{
							nl.item(i);
							if(nl.item(i).getNodeName().equalsIgnoreCase(fielddbxml_tag))
							{
								String indirectMapping = (String) responseFileMap.get("InDirect_Mapping");
								String gridMapping = (String) responseFileMap.get("Grid_Mapping");

								List<String> Grid_row = new ArrayList<String>();
								if(gridMapping.equalsIgnoreCase("Y"))
								{
									if(Grid_col_tag.contains(",")){
										String Grid_col_tag_arr[]  =Grid_col_tag.split(",");
										NodeList childnode  = nl.item(i).getChildNodes();
										for(int k = 0;k<Grid_col_tag_arr.length;k++){
											for (int child_node_len = 0 ;child_node_len< childnode.getLength();child_node_len++){
												if(childnode.item(child_node_len).getNodeName().equalsIgnoreCase(Grid_col_tag_arr[k])){
													Grid_row.add(childnode.item(child_node_len).getTextContent());
												}
											}
										}
										toreturn.put(form_control,Grid_row );
									}
									else{

									}
								}
								else if(indirectMapping.equalsIgnoreCase("Y")){
									String col_list = "xmltag_name,tag_value,indirect_tag_list,mobility_indirect_formfield_list,indirect_child_tag,mobility_form_control,indirect_val";
									sQuery = "SELECT "+col_list+" FROM NG_RLOS_Integration_Indirect_Response where Call_Name ='"+callname+"' and XmlTag_Name = '"+nl.item(i).getNodeName()+"'";
									LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"QUERY TO FIRE: "+sQuery);
									List<List<String>> outputindirect=queryExecute(sQuery,conn);
									LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, callname + "\n outputindirect : = "+ outputindirect.toString());
									String col_list_arr[] = col_list.split(",");
									Map<String, String> gridResponseMap = new HashMap<String, String>();
									for(List<String> mygridlist:outputindirect)
									{
										LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, callname + "\n mygridlist : = "+ mygridlist.toString());
										for(int x=0;x<col_list_arr.length;x++)
										{
											gridResponseMap.put(col_list_arr[x],mygridlist.get(x));
										}
										LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, callname + "\n gridResponseMap : = "+ gridResponseMap.toString());
										String xmltag_name = gridResponseMap.get("xmltag_name").toString();
										String tag_value = gridResponseMap.get("tag_value").toString().trim();
										String indirect_tag_list = gridResponseMap.get("indirect_tag_list").toString();
										String indirect_formfield_list = gridResponseMap.get("mobility_indirect_formfield_list").toString();
										String indirect_child_tag = gridResponseMap.get("indirect_child_tag").toString();
										String indirect_form_control = gridResponseMap.get("mobility_form_control").toString();//added mobility form control
										String indirect_val = gridResponseMap.get("indirect_val").toString();
										if(indirect_form_control!=null && !indirect_form_control.equalsIgnoreCase("")){
											if( nl.item(i).getTextContent().equalsIgnoreCase(tag_value)){
												toreturn.put(indirect_form_control, indirect_val);
											}
										}
										else{
											if(indirect_tag_list!=null ){
												NodeList childnode  = nl.item(i).getChildNodes();
												if(indirect_tag_list.contains(",")){
													String indirect_tag_list_arr[] = indirect_tag_list.split(",");
													String indirect_formfield_list_arr[] = indirect_formfield_list.split(",");
													if(indirect_tag_list_arr.length == indirect_formfield_list_arr.length){
														for (int k=0; k<indirect_tag_list_arr.length;k++){
															if(tag_value.equalsIgnoreCase(childnode.item(1).getTextContent()) && indirect_child_tag.equalsIgnoreCase(childnode.item(1).getNodeName())){
																for (int child_node_len = 2 ;child_node_len< childnode.getLength();child_node_len++){
																	if(childnode.item(child_node_len).getNodeName().equalsIgnoreCase(indirect_tag_list_arr[k])){
																		toreturn.put(indirect_formfield_list_arr[k], childnode.item(child_node_len).getTextContent());
																	}
																}
															}
														}
													}
													else{
													}
												}
												else{
													for (int child_node_len = 0 ;child_node_len< childnode.getLength();child_node_len++){
														if(tag_value.equalsIgnoreCase(childnode.item(child_node_len).getTextContent()) && indirect_child_tag.equalsIgnoreCase(childnode.item(child_node_len).getNodeName())){
															if(childnode.item(child_node_len).getNodeName().equalsIgnoreCase(indirect_tag_list)){
																toreturn.put(indirect_formfield_list, childnode.item(child_node_len).getTextContent());
															}
														}
													}
												}
											}
										}    
									}
								}
								if(indirectMapping.equalsIgnoreCase("N") && gridMapping.equalsIgnoreCase("N"))
								{   
									tagValue = getTagValue(outputXMLMsg,nl.item(i).getNodeName());
									toreturn.put(form_control, tagValue);
								}
							}
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			toreturn.put("Exception",this.getClass().getSimpleName()+".java " +e.toString());
			return toreturn;
		}
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"RETURNING FROM WEBSERVICERESPONSE HANDLER: "+toreturn.toString());
		return toreturn;
	}

	public static void main(String[] args){


		String out = "<?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>CUSTOMER_DETAILS</MsgFormat><MsgVersion>0000</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS151696524562682</MessageId><Extra1>REP||LAXMANRET.LAXMANRET</Extra1><Extra2>2018-01-26T03:14:06.262+04:00</Extra2></EE_EAI_HEADER><FetchCustomerDetailsRes><BankId>RAK</BankId><CIFDet><CIFID>2567137</CIFID><RetCorpFlag>C</RetCorpFlag><CifType>CF</CifType><CustomerStatus>ACTVE</CustomerStatus><CustomerSegment>SME</CustomerSegment><CustomerSubSeg>SME</CustomerSubSeg><AECBConsentHeld>Y</AECBConsentHeld><IsPremium>N</IsPremium><IsWMCustomer>N</IsWMCustomer><BlackListFlag>N</BlackListFlag><BlackListOvlFlag>N</BlackListOvlFlag><NegativeListOvlFlag>N</NegativeListOvlFlag><CreditGradeCode>R6</CreditGradeCode><PhnDet><PhnType>CELLPH1</PhnType><PhnPrefFlag>Y</PhnPrefFlag><PhnCountryCode>971</PhnCountryCode><PhnLocalCode>502567137</PhnLocalCode><PhoneNo>971502567137</PhoneNo></PhnDet><PhnDet><PhnType>FAXO1</PhnType><PhnPrefFlag>N</PhnPrefFlag><PhnCountryCode>971</PhnCountryCode><PhnLocalCode>92567137</PhnLocalCode><PhoneNo>97192567137</PhoneNo></PhnDet><PhnDet><PhnType>OFFCPH1</PhnType><PhnPrefFlag>N</PhnPrefFlag><PhnCountryCode>971</PhnCountryCode><PhnLocalCode>92567137</PhnLocalCode><PhoneNo>97192567137</PhoneNo></PhnDet><AddrDet><AddressType>OFFICE</AddressType><EffectiveFrom>2017-05-24</EffectiveFrom><EffectiveTo>2099-12-31</EffectiveTo><HoldMailFlag>N</HoldMailFlag><ReturnFlag>N</ReturnFlag><AddrPrefFlag>N</AddrPrefFlag><AddressLine1>12345</AddressLine1><State>DXB</State><City>DXB</City><Country>AE</Country></AddrDet><AddrDet><AddressType>Registered</AddressType><EffectiveFrom>2017-05-24</EffectiveFrom><EffectiveTo>2099-12-31</EffectiveTo><HoldMailFlag>N</HoldMailFlag><ReturnFlag>N</ReturnFlag><AddrPrefFlag>Y</AddrPrefFlag><AddressLine1>12345</AddressLine1><AddressLine2>PREMISE NAME FOR    2567137</AddressLine2><POBox>12345</POBox><State>RAK</State><City>RAK</City><Country>AE</Country></AddrDet><AddrDet><AddressType>Swift</AddressType><EffectiveFrom>2017-05-30</EffectiveFrom><EffectiveTo>2099-12-31</EffectiveTo><HoldMailFlag>N</HoldMailFlag></AddrDet><EmlDet><EmlType>HOMEEML</EmlType><EmlPrefFlag>Y</EmlPrefFlag><Email>1234567@RAKBANK.AE</Email></EmlDet><EmlDet><EmlType>WORKEML</EmlType><EmlPrefFlag>N</EmlPrefFlag><Email>123456@RAKBANK.AE</Email></EmlDet><DocDet><DocType>TDLIC</DocType><DocTypeDesc>TRADE LICENSE</DocTypeDesc><DocNo>RAKIA17IZ312136991</DocNo><DocIssDate>2016-12-06</DocIssDate><DocExpDate>2017-12-09</DocExpDate></DocDet><BuddyRMDetails><BuddyRMName>NATASHAH</BuddyRMName><BuddyRMPhone>971507241699</BuddyRMPhone></BuddyRMDetails><BackupRMDetails></BackupRMDetails><KYCDet><KYCHeld>Y</KYCHeld><KYCReviewDate>2018-05-24</KYCReviewDate></KYCDet><OECDDet><CRSUndocFlgReason>CIF UPDATE WITHIN UAE</CRSUndocFlgReason></OECDDet><CorpAddnlDet><CorpName>ACCOUNT NAME FOR     2567137XXX</CorpName><BusinessIncDate>2013-12-10</BusinessIncDate><OrgCountry>AE</OrgCountry><LegEnt>CF</LegEnt><GroupCreditGrading>R6</GroupCreditGrading></CorpAddnlDet></CIFDet></FetchCustomerDetailsRes></EE_EAI_MESSAGE>";
		Document doc = null;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(out)));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		NodeList nl = doc.getElementsByTagName("*");
		JSONObject toreturn=new JSONObject();
		for (int i = 0; i < nl.getLength(); i++)
		{
			nl.item(i);
			
			String col_list = "xmltag_name,tag_value,indirect_tag_list,mobility_indirect_formfield_list,indirect_child_tag,mobility_form_control,indirect_val";
			String s= "DocDet, TDLIC, DocNo,DocIssDate,DocExpDate, companydetails-tradelicenceno,companydetails-tradeexpiry, DocType, , ";
			List<String> indirect=new ArrayList<String>(Arrays.asList(s.split(",")));

			String s1= "DocDet, TDLIC, DocNo,DocExpDate, , DocType, , ";
			List<String> indirect1=new ArrayList<String>(Arrays.asList(s1.split(",")));

			List<List<String>> outputindirect=new LinkedList<List<String>>();
			outputindirect.add(indirect);
			outputindirect.add(indirect1);
			String col_list_arr[] = col_list.split(",");
			Map<String, String> gridResponseMap = new HashMap<String, String>();
			for(List<String> mygridlist:outputindirect)
			{
				for(int x=0;x<col_list_arr.length;x++)
				{
					gridResponseMap.put(col_list_arr[x],mygridlist.get(x));
				}
				String xmltag_name = gridResponseMap.get("xmltag_name").toString();
				String tag_value = gridResponseMap.get("tag_value").toString();
				String indirect_tag_list = gridResponseMap.get("indirect_tag_list").toString();
				String indirect_formfield_list = gridResponseMap.get("mobility_indirect_formfield_list").toString();
				String indirect_child_tag = gridResponseMap.get("indirect_child_tag").toString();
				String indirect_form_control = gridResponseMap.get("mobility_form_control").toString();//added mobility form control
				String indirect_val = gridResponseMap.get("indirect_val").toString();
				if(indirect_form_control!=null && !indirect_form_control.equalsIgnoreCase("")){
					if( nl.item(i).getTextContent().equalsIgnoreCase(tag_value.trim())){
						toreturn.put(indirect_form_control, indirect_val);
					}
				}
				else{
					if(indirect_tag_list!=null ){
						NodeList childnode  = nl.item(i).getChildNodes();
						if(indirect_tag_list.contains(",")){
							String indirect_tag_list_arr[] = indirect_tag_list.split(",");
							String indirect_formfield_list_arr[] = indirect_formfield_list.split(",");
							if(indirect_tag_list_arr.length == indirect_formfield_list_arr.length){
								for (int k=0; k<indirect_tag_list_arr.length;k++){
									if(tag_value.equalsIgnoreCase(childnode.item(1).getTextContent()) && indirect_child_tag.equalsIgnoreCase(childnode.item(1).getNodeName())){
										for (int child_node_len = 2 ;child_node_len< childnode.getLength();child_node_len++){
											if(childnode.item(child_node_len).getNodeName().equalsIgnoreCase(indirect_tag_list_arr[k])){
												toreturn.put(indirect_formfield_list_arr[k], childnode.item(child_node_len).getTextContent());
											}
										}
									}
								}
							}
							else{
							}
						}
						else{
							for (int child_node_len = 0 ;child_node_len< childnode.getLength();child_node_len++){
								if(tag_value.equalsIgnoreCase(childnode.item(child_node_len).getTextContent()) && indirect_child_tag.equalsIgnoreCase(childnode.item(child_node_len).getNodeName())){
									if(childnode.item(child_node_len).getNodeName().equalsIgnoreCase(indirect_tag_list)){
										toreturn.put(indirect_formfield_list, childnode.item(child_node_len).getTextContent());
									}
								}
							}
						}
					}
				}    
			}
		}
		
		new RakBankIntegrations().handleRepeatedTags(toreturn, "CUSTOMER_DETAILS", out);
	}
	
	public  String getTagValue(String xml, String tag) throws ParserConfigurationException, SAXException, IOException
	{  
		Document doc = getDocument(xml);
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
		return "";
	}

	public  Document getDocument(String xml) throws ParserConfigurationException, SAXException, IOException
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(new InputSource(new StringReader(xml)));
		return doc;
	}

	public List<List<String>>queryExecute(String querytofire,Connection con){
		RakBankIntegrations object=new RakBankIntegrations();
		List<List<String>> master_record_list=new LinkedList<List<String>>();
		ResultSet rs=null;
		PreparedStatement pstmt=null;
		try{
			if(con!=null){
				JSONObject toreturn=new JSONObject();
				System.out.println("connection Estbd");
				System.out.println(querytofire);
				pstmt = con.prepareStatement(querytofire); 		
				rs=pstmt.executeQuery();
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnsNumber = rsmd.getColumnCount();

				int i;
				if(rs!=null){
					while(rs.next()){
						i=1;
						List<String> templist=new LinkedList<String>();
						while(i<=columnsNumber){
							templist.add(rs.getString(i));	
							i++;
						}
						master_record_list.add(templist);
					}
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(rs!=null){
					rs.close();
				}
				if(pstmt!=null){
					pstmt.close();
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}

		}
		return master_record_list;
	}
}

