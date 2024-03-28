package com.newgen.custom.implementation;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.Map.Entry;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.codec.binary.Base64;
import org.json.XML;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ISPack.CPISDocumentTxn;
import ISPack.ISUtil.JPDBRecoverDocData;
import ISPack.ISUtil.JPISException;
import ISPack.ISUtil.JPISIsIndex;
import Jdts.Client.JtsConnection;

import com.newgen.dmsapi.DMSCallBroker;
import com.newgen.dmsapi.DMSInputXml;
import com.newgen.dmsapi.DMSXmlResponse;
import com.newgen.essentials.backendprocessing.interfaces.SubmissionManager;
import com.newgen.essentials.templates.defaulttemplate.entities.concrete.Attachment;
import com.newgen.essentials.templates.defaulttemplate.entities.concrete.BusinessUseCase;
import com.newgen.essentials.templates.defaulttemplate.entities.concrete.Form;
import com.newgen.essentials.templates.defaulttemplate.entities.concrete.Section;
import com.newgen.mcap.core.external.apiengine.concrete.APICallContext;
import com.newgen.mcap.core.external.basic.entities.concrete.Device;
import com.newgen.mcap.core.external.basic.entities.concrete.Organization;
import com.newgen.mcap.core.external.basic.entities.concrete.User;
import com.newgen.mcap.core.external.configuration.entities.concrete.Configuration;
import com.newgen.mcap.core.external.logging.concrete.LogMe;
import com.newgen.mcap.core.external.resources.abstracted.Resource;
import com.newgen.mcap.core.external.resources.concrete.StreamedResource;
import com.newgen.mcap.core.external.utils.EncryptionUtils;
import com.newgen.mcap.core.external.utils.GenericUtils;
import com.newgen.mcap.core.external.utils.HibernateUtil;
import com.newgen.mcap.core.external.utils.MobileCaptureConstants;
import com.newgen.mcap.core.external.utils.ThreadUtils;
import com.newgen.niplj.NIPLJ;
import com.newgen.niplj.fileformat.Tif6;
import com.newgen.niplj.generic.NGIMException;
import com.newgen.omni.jts.cmgr.XMLParser;
import com.newgen.omni.wf.util.app.NGEjbClient;
import com.newgen.wfdesktop.xmlapi.WFCallBroker;
import com.newgen.wfdesktop.xmlapi.WFInputXml;
public class RakOFSubmission implements SubmissionManager {
	

	class SMSDocument {
		private String path; // absolute-path
		private String docType;
		private String imageIndex;
		private String volumeId;
		private int noOfPages;
		private long size;
		private String extenstion;
		private String comment;
	}

	private APICallContext apiCallContext;
	private Configuration configuration;

	@Override
	public APICallContext getAPICallContext() throws Exception {
		return apiCallContext;
	}

	@Override
	public void setAPICallContext(APICallContext apiCallContext)
	throws Exception {
		this.apiCallContext = apiCallContext;
	}

	@Override
	public Configuration getConfiguration() throws Exception {
		return configuration;
	}

	@Override
	public void setConfiguration(Configuration configuration) throws Exception {
		this.configuration = configuration;
	}

	@Override
	public String getSolutionVersion() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAbstractionName() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getIdentifier() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StreamedResource submitResource(BusinessUseCase buc)
	throws Exception { 
		try {
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "[submitResource initiated]"+ new Date().toString());
			StreamedResource toReturn = new StreamedResource();
			User user = (User) ThreadUtils.getThreadVariable("user");
			if (user == null || user.getAuthenticationToken() == null) {
				throw new Exception("cannot submit without user");
			}
			buc.setUser(user);

			// read server configurations
			String tempDir = null;
			String jtsAddress = null;
			String jtsPort = null;
			String appServerType = null;
			String cabinetName = null;
			String volumeIndex = null;
			Node ofConfigNode = null;
			try {
				String xPath = null;
				xPath = "./*/OFMapping[@bucName='" + buc.getBucName() + "']";

				// xPath =
				// "configuratioNode/childConfigurationItems[configNodeName='OFMapping' and attributes[attributeName='bucName' and attributeValue='"
				// + buc.getBucName() + "']]";
				ofConfigNode = configuration.searchObjectGraph(xPath,
						configuration.getConfigurationXmlText()).item(0);

				// xPath =
				// "childConfigurationItems[configNodeName='Server']";
				xPath = "./Server";
				// ConfigurationNode serverInfoNode = (ConfigurationNode)
				// ofConfigNode
				// .searchObjectGraph(xPath).get(0);

				Node serverInfoNode = configuration.searchObjectGraph(xPath,
						(Element) ofConfigNode).item(0);
				// temp-directory
				// xPath = "childConfigurationItems[configNodeName='Key'"
				// +
				// " and attributes[attributeName='name' and attributeValue='tempDir']]"
				// + "/textWithinNode";

				xPath = "./Key[@name = 'tempDir']";
				tempDir = configuration
				.searchObjectGraph(xPath, (Element) serverInfoNode)
				.item(0).getTextContent().trim();
				// tempDir = (String)
				// serverInfoNode.searchObjectGraph(xPath).get(0);
				if (!tempDir.endsWith(File.separator)) {
					tempDir += File.separator;
				}
				// server-address
				xPath = "./Key[@name = 'serverAddress']";
				jtsAddress = configuration
				.searchObjectGraph(xPath, (Element) serverInfoNode)
				.item(0).getTextContent().trim();

				// xPath = "childConfigurationItems[configNodeName='Key'"
				// +
				// " and attributes[attributeName='name' and attributeValue='serverAddress']]"
				// + "/textWithinNode";
				// jtsAddress = (String)
				// serverInfoNode.searchObjectGraph(xPath)
				// .get(0);
				// server-port
				xPath = "./Key[@name = 'serverPort']";
				jtsPort = configuration
				.searchObjectGraph(xPath, (Element) serverInfoNode)
				.item(0).getTextContent().trim();
				// xPath = "childConfigurationItems[configNodeName='Key'"
				// +
				// " and attributes[attributeName='name' and attributeValue='serverPort']]"
				// + "/textWithinNode";
				// jtsPort = (String)
				// serverInfoNode.searchObjectGraph(xPath).get(0);
				// cabinet-name
				xPath = "./Key[@name = 'serverType']";
				appServerType = configuration
				.searchObjectGraph(xPath, (Element) serverInfoNode)
				.item(0).getTextContent().trim();
				xPath = "./Key[@name = 'cabinetName']";
				cabinetName = configuration
				.searchObjectGraph(xPath, (Element) serverInfoNode)
				.item(0).getTextContent().trim();
				// xPath = "childConfigurationItems[configNodeName='Key'"
				// +
				// " and attributes[attributeName='name' and attributeValue='cabinetName']]"
				// + "/textWithinNode";
				// cabinetName = (String)
				// serverInfoNode.searchObjectGraph(xPath).get(
				// 0);
				// volume-index
				xPath = "./Key[@name = 'volumeIndex']";
				volumeIndex = configuration
				.searchObjectGraph(xPath, (Element) serverInfoNode)
				.item(0).getTextContent().trim();
				// xPath = "childConfigurationItems[configNodeName='Key'"
				// +
				// " and attributes[attributeName='name' and attributeValue='volumeIndex']]"
				// + "/textWithinNode";
				// volumeIndex = (String)
				// serverInfoNode.searchObjectGraph(xPath).get(
				// 0);
				if (tempDir == null || jtsAddress == null || jtsPort == null
						|| cabinetName == null || volumeIndex == null) {
					throw new Exception(
					"null entries while reading server configuartion");
				}
			} catch (Exception e) {
				e.printStackTrace();
				LogMe.logMe(LogMe.LOG_LEVEL_ERROR, e);
				throw new Exception("error in reading server configuration");
			}

			// read process configurations
			String processDefId = null;
			// String processName = null;
			// String processVersion = null;
			// String initiateFromActivityId = null;
			String initiateAlso = null;
			// String allowDuplicacy = null;
			String activityId = null;
			String activityType = null;
			try {
				String xPath = null;
				xPath = "./Process";
				Node processInfoNode = configuration.searchObjectGraph(xPath,
						(Element) ofConfigNode).item(0);
				// xPath =
				// "childConfigurationItems[configNodeName='Process']";
				// ConfigurationNode processInfoNode = (ConfigurationNode)
				// ofConfigNode
				// .searchObjectGraph(xPath).get(0);
				// process-def-id
				xPath = "./Key[@name = 'processDefId']";
				processDefId = configuration
				.searchObjectGraph(xPath, (Element) processInfoNode)
				.item(0).getTextContent().trim();

				xPath = "./Key[@name = 'activityId']";
				activityId = configuration
				.searchObjectGraph(xPath, (Element) processInfoNode)
				.item(0).getTextContent().trim();

				xPath = "./Key[@name = 'activityType']";
				activityType = configuration
				.searchObjectGraph(xPath, (Element) processInfoNode)
				.item(0).getTextContent().trim();

				// xPath = "childConfigurationItems[configNodeName='Key'"
				// +
				// " and attributes[attributeName='name' and attributeValue='processDefId']]"
				// + "/textWithinNode";
				// processDefId = (String)
				// processInfoNode.searchObjectGraph(xPath)
				// .get(0);
				if (!tempDir.endsWith(File.separator)) {
					tempDir += File.separator;
				}
				// initiate-Also
				xPath = "./Key[@name = 'initiateAlso']";
				initiateAlso = configuration
				.searchObjectGraph(xPath, (Element) processInfoNode)
				.item(0).getTextContent().trim();


				if (processDefId == null || initiateAlso == null) {
					throw new Exception(
					"null entries while reading process configuartion");
				}
			} catch (Exception e) {
				e.printStackTrace();
				LogMe.logMe(LogMe.LOG_LEVEL_ERROR, e);
				throw new Exception("error in reading process configuration");
			}

			// process output-model
			Map<String, String> attributesMapFromApk = new HashMap<String, String>();
			List<String> attachmentsToProcess = new ArrayList<String>();
			if (buc.getForms() != null) {
				for (Form form : buc.getForms()) {
					String outputModel = form.getOutputModel();
					if (outputModel == null || outputModel.trim().isEmpty()) {
						throw new Exception("no input");
					}
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "outputModel=>"
							+ outputModel);
					JSONParser jParser = new JSONParser();
					JSONObject jObject = (JSONObject) jParser.parse(outputModel);
					populateFromJsonObject(jObject, attributesMapFromApk,
							attachmentsToProcess);
				}
			} else {
				throw new Exception("no forms to process");
			}

			// read external table mapping (externalTableFieldName,
			// apkFieldname)
			Map<String, String> externalTableFieldToApkFieldMap = new HashMap<String, String>();
			Map<String, Map<String, String>> apkFieldToComplexTableFieldMap = new HashMap<String, Map<String,String>>();
			Map<String, Map<String, String>> apkFieldToGridTableFieldMap = new HashMap<String, Map<String,String>>();
			Map<String,Map<String,Map<String,String>>> gridtableMap= new HashMap<String,Map<String,Map<String,String>>>();
			
			//gaurav-s change 15012018
			
			// added by abhishek for company details
			Map<String,Map<String,Map<String,Map<String,String>>>> NestedgridtableMap= new HashMap<String,Map<String,Map<String,Map<String,String>>>>();
			Map<String,Map<String, Map<String, String>>> apkFieldToNestedGridTableFieldMap = new HashMap<String,Map<String, Map<String,String>>>();
			
			//HashMap<String,HashMap<String, HashMap<String, String>>> parentChildNestedGridMap=new HashMap<String, HashMap<String,HashMap<String,String>>>();
			try {

				String xPath = null;
				xPath = "./ExternalTableMapping";
				Node extTableMappingNode = configuration.searchObjectGraph(xPath, (Element) ofConfigNode).item(0);

				// get fields
				xPath = "./Field";
				NodeList fieldNodes = configuration.searchObjectGraph(xPath, (Element) extTableMappingNode);

				String xPathForExternalTableFieldName = "attributes[attributeName='columnname']/attributeValue";
				String xPathForApkFieldName = "attributes[attributeName='apkfieldname']/attributeValue";
				Node fieldNode = null;
				for (int j = 0; j < fieldNodes.getLength(); j++) {
					fieldNode = fieldNodes.item(j);
					String externalTableFieldName = ((Element) fieldNode).getAttribute("columnname");
					String apkFieldName = ((Element) fieldNode).getAttribute("apkfieldname");
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "extFields=>" + externalTableFieldName + ":" + apkFieldName);
					externalTableFieldToApkFieldMap.put(externalTableFieldName, apkFieldName);
				}

				xPath = "./ComplexTableMapping";
				Node complexTableMappingNode = configuration.searchObjectGraph(xPath, (Element) ofConfigNode).item(0);

				xPath = "./Field";
				NodeList complexFieldNodes = configuration.searchObjectGraph(xPath, (Element) complexTableMappingNode);
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "complexFieldNodes=>" + complexFieldNodes + "complexTableMappingNode" + complexTableMappingNode);
				
				Node complexFieldNode = null;
				for (int x = 0; x < complexFieldNodes.getLength(); x++) {
					Map<String, String> complexFieldsMap = new HashMap<String, String>();
					Node complexFieldChildNode = null;
					complexFieldNode = complexFieldNodes.item(x);
					String complexFieldName = ((Element) complexFieldNode).getAttribute("name");
					xPath = "./ChildItem";
					NodeList complexFieldChildNodes = configuration.searchObjectGraph(xPath, (Element) complexFieldNode);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "complexFieldChildNodes=>" + complexFieldChildNodes + "complexTableMappingNode" + complexTableMappingNode);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "complexFieldNode=>" + complexFieldNode + "complexTableMappingNode" + complexTableMappingNode);
					
					for (int y = 0; y < complexFieldChildNodes.getLength(); y++) {
						complexFieldChildNode = complexFieldChildNodes.item(y);
						String complexFieldChildNodeName = ((Element) complexFieldChildNode).getAttribute("columnname");
						String complexFieldChildApkFieldName = ((Element) complexFieldChildNode).getAttribute("apkfieldname");
						complexFieldsMap.put(complexFieldChildNodeName, complexFieldChildApkFieldName);
					}
					apkFieldToComplexTableFieldMap.put(complexFieldName, complexFieldsMap);
				}


				//gaurav-s changes
				xPath = "./GridTableMapping";
				Node gridTableMappingNode = configuration.searchObjectGraph(xPath, (Element) ofConfigNode).item(0);
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "gridTableMappingNode value : "+ gridTableMappingNode+" ");

				xPath = "./Field";
				NodeList gridfieldnodes = configuration.searchObjectGraph(xPath, (Element) gridTableMappingNode);
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "gridfieldnodes value : "+ gridfieldnodes+" ");
				
				for(int k = 0; k < gridfieldnodes.getLength(); k++)
				{
					Map<String,Map<String,String>> gridname_to_apk_column_mapping= new HashMap<String,Map<String,String>>(); 
					Node gridNode=null;
					Node gridfieldNode=gridfieldnodes.item(k);
					String queuevariable= ((Element) gridfieldNode).getAttribute("name");
					//if(!"q_CompanyDetails".equalsIgnoreCase(queuevariable)){
						xPath = "./Grid";
						NodeList gridnodes = configuration.searchObjectGraph(xPath, (Element) gridfieldNode);
						Node gridChildNode = null;
						gridNode = gridnodes.item(0);
						String gridname = ((Element) gridNode).getAttribute("name");
						
						xPath = "./ChildItem";
						NodeList gridChildNodes = configuration.searchObjectGraph(xPath, (Element) gridNode);
						Map<String,String> apktocolumngridtableMap= new HashMap<String,String>(); 
						for (int y = 0; y < gridChildNodes.getLength(); y++) {
							gridChildNode = gridChildNodes.item(y);
							String gridFieldChildNodeName = ((Element) gridChildNode).getAttribute("columnname");
							String gridChildApkFieldName = ((Element) gridChildNode).getAttribute("apkfieldname");
							apktocolumngridtableMap.put(gridFieldChildNodeName, gridChildApkFieldName);
						}
						gridname_to_apk_column_mapping.put(gridname, apktocolumngridtableMap);
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "\nqueuevariable value  "+ queuevariable+" ");
				//	}
					//added by abhishek for company details
					/*else{
						xPath = "./Grid";
						NodeList gridnodes = configuration.searchObjectGraph(xPath, (Element) gridfieldNode);
						Node gridChildNode = null;
						gridNode = gridnodes.item(0);
						String gridname = ((Element) gridNode).getAttribute("name");
						if("cmplx_GR_AuthorizedSignDetails".equalsIgnoreCase(gridname) || "cmplx_partnerGrid".equalsIgnoreCase(gridname)){
							xPath = "./ChildItem";
							NodeList gridChildNodes = configuration.searchObjectGraph(xPath, (Element) gridNode);
							Map<String,String> apktocolumngridtableMap= new HashMap<String,String>(); 
							for (int y = 0; y < gridChildNodes.getLength(); y++) {
								gridChildNode = gridChildNodes.item(y);
								String gridFieldChildNodeName = ((Element) gridChildNode).getAttribute("columnname");
								String gridChildApkFieldName = ((Element) gridChildNode).getAttribute("apkfieldname");
								apktocolumngridtableMap.put(gridFieldChildNodeName, gridChildApkFieldName);
							}
							gridname_to_apk_column_mapping.put(gridname, apktocolumngridtableMap);
							apkFieldToNestedGridTableFieldMap.put("cmplx_CompanyGrid", gridname_to_apk_column_mapping);
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "\nqueuevariable value  "+ queuevariable+" ");
						}else{
							xPath = "./ChildItem";
							NodeList gridChildNodes = configuration.searchObjectGraph(xPath, (Element) gridNode);
							Map<String,String> apktocolumngridtableMap= new HashMap<String,String>(); 
							for (int y = 0; y < gridChildNodes.getLength(); y++) {
								gridChildNode = gridChildNodes.item(y);
								String gridFieldChildNodeName = ((Element) gridChildNode).getAttribute("columnname");
								String gridChildApkFieldName = ((Element) gridChildNode).getAttribute("apkfieldname");
								apktocolumngridtableMap.put(gridFieldChildNodeName, gridChildApkFieldName);
							}
							gridname_to_apk_column_mapping.put(gridname, apktocolumngridtableMap);
							apkFieldToNestedGridTableFieldMap.put("cmplx_CompanyGrid", gridname_to_apk_column_mapping);
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "\nqueuevariable value  "+ queuevariable+" ");
						}
						
					}
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "gridname_to_apk_column_mapping value  "+ gridname_to_apk_column_mapping+" ");
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "apkFieldToNestedGridTableFieldMap value  "+ apkFieldToNestedGridTableFieldMap+" ");*/
					/*if(queuevariable.equalsIgnoreCase("q_AddressDetails")){
						// added by aman/Abhishek for deleting address details grid rows on save 
					  String xmlToDel=ExecuteQuery_APdelete("ng_RLOS_GR_AddressDetails", "Wi_Name = '"+buc.getProcessId().toString()+ "'", cabinetName, user.getAuthenticationToken());
                   	  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "xmlToDel: "+ xmlToDel);
                   	  String Deloutputxml = WFCallBroker.execute(xmlToDel, jtsAddress, Integer.parseInt(jtsPort), 0);
                   	  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Deloutputxml: "+ Deloutputxml);
                   	
					}*/
					 if(queuevariable.equals("border_RAKBANK_lia") || queuevariable.equals("border_EXTERNAL_lia") || queuevariable.equals("border_EXTERNAL_lia_loan") || queuevariable.equals("border_RAKBANK_lia_loan") || queuevariable.equals("requestedproductsclonecard") || queuevariable.equals("fundingAccountTableDiv") ||queuevariable.trim().equalsIgnoreCase("DBRLoanGridclonecard"))
					 {				 
						String tableName="";
						String sWhere="";
						String tablenametodelete = "";
						String wherefordelete="";
						boolean flagforloancard=false;
						boolean flagforpersonalloan=false;
						for (Entry<String, Map<String, String>> gridentry : gridname_to_apk_column_mapping.entrySet()) {
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Getting  GRID: "+ gridentry+" ");
							JSONObject attributesjsonFromApk=new JSONObject(attributesMapFromApk);
							JSONArray grid_table_array=(JSONArray)new JSONParser().parse(attributesjsonFromApk.get(gridentry.getKey()).toString());
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Found Grid Values : "+ grid_table_array+" In Output Model");
							for(int record_counter=0;record_counter<grid_table_array.size();record_counter++){
								String ColName="";
								String Values="";
								JSONObject record=(JSONObject)grid_table_array.get(record_counter);
								for (Entry<String, String> subentries : gridentry.getValue().entrySet()) 
								{
									LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Preparing XML for   GRID: "+ gridentry+" ");
									LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Getting : "+ subentries.getValue()+" In Output Model");
									ColName = ColName+subentries.getKey()+",";
									if(record.get(subentries.getValue())==null||record.get(subentries.getValue()).toString().equalsIgnoreCase("undefined")){
										Values = Values+"'',";
									}
									else{
										Values = Values+"'"+record.get(subentries.getValue()).toString()+"',";
									}
								}
								LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "ColName : "+ ColName);
								LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Values : "+ Values);
								if(queuevariable.equals("border_RAKBANK_lia")){
									//if((record.get("Product Type").toString().contains("Credit") ||record.get("Product Type").toString().contains("Card"))){
									tableName="ng_RLOS_CUSTEXPOSE_CardDetails";
									sWhere="Wi_Name = '"+record.get("wiName").toString()+"' and CardEmbossNum = '"+record.get("Agreement Id/Crnno/OD no./HIO/Trade").toString()+"'";
								}else if (queuevariable.equals("border_RAKBANK_lia_loan")){
									tableName="ng_RLOS_CUSTEXPOSE_LoanDetails";
									sWhere="Wi_Name = '"+record.get("wiName").toString()+"' and AgreementId = '"+record.get("Agreement Id/Crnno/OD no./HIO/Trade").toString()+"'";
								}
								//}
								else if(queuevariable.equals("border_EXTERNAL_lia")){
									//if((record.get("Type of contract").toString().contains("Credit") ||record.get("Type of contract").toString().contains("Card"))){
									tableName="ng_rlos_cust_extexpo_CardDetails";
									sWhere="Wi_Name = '"+record.get("wiName").toString()+"' and CardEmbossNum = '"+record.get("Provider Number").toString()+"'";
								}
								else if(queuevariable.equals("border_EXTERNAL_lia_loan")){
									tableName="ng_rlos_cust_extexpo_loanDetails";
									sWhere="Wi_Name = '"+record.get("wiName").toString()+"' and AgreementId = '"+record.get("Provider Number").toString()+"'";
								}
								 //gaurav-s added q variable on 16032018
                                else if(queuevariable.equals("requestedproductsclonecard")){
                                	LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "flagforloancard: "+ flagforloancard);
                                	if(!flagforloancard){
                                	  String xmlToDel=ExecuteQuery_APdelete("ng_rlos_IGR_Eligibility_CardLimit", "Wi_Name = '"+record.get("wiName").toString()+"'", cabinetName, user.getAuthenticationToken());
                                	  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "xmlToDel: "+ xmlToDel);
                                	  String Deloutputxml = WFCallBroker.execute(xmlToDel, jtsAddress, Integer.parseInt(jtsPort), 0);
                                	  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Deloutputxml: "+ Deloutputxml);
                                	  flagforloancard=true;
                                	}
                                	  String xmltoinsert= ExecuteQuery_APInsert("ng_rlos_IGR_Eligibility_CardLimit",
                                     		 ColName.substring(0,ColName.length()-1),
                                     		 Values.substring(0,Values.length()-1), 
                                     		 cabinetName,
                                     		 user.getAuthenticationToken());
                                	  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "xmltoinsert: "+ xmltoinsert);
                                	  String insertoutxml = WFCallBroker.execute(xmltoinsert, jtsAddress, Integer.parseInt(jtsPort), 0);
                                      LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "insertoutxml: "+ insertoutxml);
                                	//commented for Eligible Limit bug
//                                    tableName="ng_rlos_IGR_Eligibility_CardLimit";
//                                    //Card_Product    Eligible_Limit
//                                    //Card_Product = 'Card Product' and Eligible_Limit = 'Eligible Limit'
//                                    sWhere="Wi_Name = '"+record.get("wiName").toString()+"' and Card_Product = '"+record.get("Card Product").toString()+"'"+" and Eligible_Limit = '"+record.get("Eligible Limit").toString()+"'";
                                }
                                //gaurav-s added q variable on 16032018
                                else if(queuevariable.equals("fundingAccountTableDiv")){
                                	
                                    tableName="ng_RLOS_CUSTEXPOSE_AcctDetails";
                                    //AcctId = 'Account Number'
                                    sWhere="Wi_Name = '"+record.get("wiName").toString()+"' and AcctId = '"+record.get("Account Number").toString()+"'";
                                }
                                else if(queuevariable.trim().equalsIgnoreCase("DBRLoanGridclonecard")){
                                   // tableName="ng_rlos_IGR_Eligibility_PersonalLoan";
                                    //AcctId = 'Account Number'
                                LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "flagforpersonalloan: "+ flagforpersonalloan);
                                if(!flagforpersonalloan){
                                  String xmlToDel=ExecuteQuery_APdelete("ng_rlos_IGR_Eligibility_PersonalLoan", "wi_name = '"+record.get("wiName").toString()+"'", cabinetName, user.getAuthenticationToken());
                                  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "xmlToDel: "+ xmlToDel);
                                  String Deloutputxml = WFCallBroker.execute(xmlToDel, jtsAddress, Integer.parseInt(jtsPort), 0);
                                  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Deloutputxml: "+ Deloutputxml);
                                  flagforpersonalloan=true;
                                	}
                                String insertQuery="INSERT INTO ng_rlos_IGR_Eligibility_PersonalLoan " +
                                		"("+ColName.substring(0,ColName.length()-1)+") " +
                                				" VALUES ("+ Values.substring(0,Values.length()-1)+")";
                                ExecuteDbqueryReturn(insertQuery);
//                                  String xmltoinsert= ExecuteQuery_APInsert("ng_rlos_IGR_Eligibility_PersonalLoan",
//                                		 ColName.substring(0,ColName.length()-1),
//                                		 Values.substring(0,Values.length()-1), 
//                                		 cabinetName,
//                                		 user.getAuthenticationToken());
//                                  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "xmltoinsert: "+ xmltoinsert);
//                                 String insertoutxml = WFCallBroker.execute(xmltoinsert, jtsAddress, Integer.parseInt(jtsPort), 0);
//                                 LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "insertoutxml: "+ insertoutxml);
                                
                                  
                                  //commented for final limit bug
//                                  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "APUpdate port: "+ jtsPort); 
//                                  if(record.get("Decision").toString().trim().equalsIgnoreCase("Decline"))
//                                    sWhere="wi_name = '"+record.get("wiName").toString()+"' AND Decision = '"+record.get("Decision").toString()+"' AND DeclinedReasonCode = '"+record.get("Declined Reason and Code").toString()+"'";
//                                    else
//                                    	sWhere="wi_name = '"+record.get("wiName").toString()+"' AND Decision = '"+record.get("Decision").toString()+"' AND ReferReasoncode = '"+record.get("Refer Reason and Code").toString()+"'";
                                }
								//}
								String strOutputXml = "";
								//LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "NEW JAR!!");
								if(!(queuevariable.trim().equalsIgnoreCase("DBRLoanGridclonecard") || queuevariable.equals("requestedproductsclonecard") )){
									LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "APUpdate tablename: "+ tableName);
									LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "APUpdate sWhere: "+ sWhere);

									String strInputXML=ExecuteQuery_APUpdate(tableName, ColName.substring(0,ColName.length()-1), Values.substring(0,Values.length()-1), sWhere, "cas",user.getAuthenticationToken());
									
									
									LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "APUpdate port: "+ jtsPort);
									 strOutputXml = WFCallBroker.execute(strInputXML, jtsAddress, Integer.parseInt(jtsPort), 0);
									 LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"strOutputXml is:"+strOutputXml);
								}
								
								//String strOutputXml = makeCall(jtsAddress,Short.valueOf(jtsPort), appServerType, strInputXML);
								
								
								if(queuevariable.equals("requestedproductsclonecard")){
									tableName="NG_RLOS_EXTTABLE";
                                    //Card_Product    Eligible_Limit
                                    //Card_Product = 'Card Product' and Eligible_Limit = 'Eligible Limit'
                                    sWhere="WIname = '"+record.get("wiName").toString()+"'" ;
                                   String waiver =  record.get("Waiver").toString();
                                   String decision = "";
                                   if("true".equalsIgnoreCase(waiver)){
                                	   decision = "'Y'";
                                   }
                                   if("false".equalsIgnoreCase(waiver)){
                                	   decision = "'N'";
                                   }
                                  String strinputtXml=ExecuteQuery_APUpdate(tableName,"is_cc_waiver_require", decision, sWhere, "cas",user.getAuthenticationToken());
                                   strOutputXml = WFCallBroker.execute(strinputtXml, jtsAddress, Integer.parseInt(jtsPort), 0);
								}
								LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"strOutputXml is:"+strOutputXml);
								continue;
							}
						}
					}
					 
					
					gridtableMap.put(queuevariable, gridname_to_apk_column_mapping);
				}

				//gaurav-s changes END
				
				//gaurav-s changes 15012018
				
//				HashMap<String, HashMap<String, String>> NestedGridMap=new HashMap<String, HashMap<String,String>>();
//				
//				xPath = "./NestedGridMapping";  
//				Node nestedgridTableMappingNode = configuration.searchObjectGraph(xPath, (Element) ofConfigNode).item(0);
//
//				xPath = "./NestedGrid";
//				NodeList nestedgridnodes = configuration.searchObjectGraph(xPath, (Element) nestedgridTableMappingNode);
//				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG," nestedgridnodes Length:"+ nestedgridnodes.getLength());
//				for(int nestedgridentry = 0; nestedgridentry < nestedgridnodes.getLength(); nestedgridentry++){
//					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"Inside Loop  1");
//					Node nestedgridfieldNode=nestedgridnodes.item(nestedgridentry);
//					String nestedgridfieldNodename= ((Element) nestedgridfieldNode).getAttribute("name");
//					String parentgridname=((Element) nestedgridfieldNode).getAttribute("parentGrid");
//					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"parentgridname is:"+parentgridname);
//					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"nestedgridfieldNodename is:"+nestedgridfieldNodename);
//					xPath = "./ChildItem";
//					NodeList childNodes = configuration.searchObjectGraph(xPath, (Element) nestedgridfieldNode);
//					HashMap<String, String> nestedGridXmlIdMap=new HashMap<String, String>();
//					for(int childentry=0;childentry < childNodes.getLength();childentry++){
//						//xPath = "./ChildItem";
//						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"Inside Loop  2");
//						Node childnode=childNodes.item(childentry);
//						String xmltag= ((Element) childnode).getAttribute("columnname");
//						String apkId= ((Element) childnode).getAttribute("apkfieldname");
//						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"xmltag is:"+xmltag);
//						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"apkId is:"+apkId);
//						nestedGridXmlIdMap.put(xmltag, apkId);
//					}
//					NestedGridMap.put(nestedgridfieldNodename, nestedGridXmlIdMap);
//					parentChildNestedGridMap.put(parentgridname, NestedGridMap);
//				}

				//gaurav-s changes END

			} catch (Exception e) {
				e.printStackTrace();
				LogMe.logMe(LogMe.LOG_LEVEL_ERROR, e);
				throw new Exception("error in reading server configuration");
			}

			// process field-value-map
			Map<String, String> attributesMapToUpload = new HashMap<String, String>();
			for (Entry<String, String> entry : externalTableFieldToApkFieldMap
					.entrySet()) {
				String externalTableField = entry.getKey();
				String apkField = entry.getValue();
				String fieldValue = null;
				if (attributesMapFromApk.containsKey(apkField)) {
					fieldValue = attributesMapFromApk.get(apkField);
				} else {
					throw new Exception(
							"no apk-field mapping found for external field : "
							+ externalTableField);
				}
				attributesMapToUpload.put(externalTableField, fieldValue);
			}

			// process complex fields map
			Map<String, Map<String, String>> complexAttributesMapToUpload = new HashMap<String, Map<String,String>>();
			for (Entry<String, Map<String, String>> entry : apkFieldToComplexTableFieldMap
					.entrySet()) {
				Map<String, String> complexFieldsToUpload = new HashMap<String, String>();
				String complexTableField = entry.getKey();
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "complexTableField for company details => " + complexTableField);
				Map<String, String> complexFieldValueMap = entry.getValue();
				String complexFieldValue = null;
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "complexFieldValueMap for company details => " + complexFieldValueMap);
				if (complexFieldValueMap.size() > 0)
					for (Entry<String, String> fieldEntry : complexFieldValueMap.entrySet())  {
						String complexField = fieldEntry.getKey();
						String apkField = fieldEntry.getValue();
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"COMPLEX==> "+complexField+" APK ==>"+apkField);
						if (attributesMapFromApk.containsKey(apkField)) {
							complexFieldValue = attributesMapFromApk.get(apkField);
						} else {
							throw new Exception(
									"no apk-field mapping found for complex field : "
									+ complexField);
						}
						complexFieldsToUpload.put(complexField, complexFieldValue);
					}
				complexAttributesMapToUpload.put(complexTableField, complexFieldsToUpload);
			}

			// read custom table mapping (customTableFieldName,
			// apkFieldname) :
			// Gaurav Berry
			// Map<String, String> customTableFieldToApkFieldMap = new
			// HashMap<String, String>();
			// try {
			// String xPath = null;
			// xPath =
			// "childConfigurationItems[configNodeName='CustomTableMapping']";
			// ConfigurationNode extTableMappingNode = (ConfigurationNode)
			// ofConfigNode
			// .searchObjectGraph(xPath).get(0);
			// // get fields
			// xPath = "childConfigurationItems[configNodeName='Field']";
			// @SuppressWarnings("unchecked")
			// List<ConfigurationNode> fieldNodes =
			// (List<ConfigurationNode>) extTableMappingNode
			// .searchObjectGraph(xPath);
			// String xPathForCustomTableFieldName =
			// "attributes[attributeName='columnname']/attributeValue";
			// String xPathForApkFieldName =
			// "attributes[attributeName='apkfieldname']/attributeValue";
			// for (ConfigurationNode fieldNode : fieldNodes) {
			// String customTableFieldName = (String) fieldNode
			// .searchObjectGraph(xPathForCustomTableFieldName).get(0);
			// String apkFieldName = (String) fieldNode.searchObjectGraph(
			// xPathForApkFieldName).get(0);
			// LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "extFields=>"
			// + customTableFieldName + ":" + apkFieldName);
			// customTableFieldToApkFieldMap.put(customTableFieldName,
			// apkFieldName);
			// }
			// } catch (Exception e) {
			// e.printStackTrace();
			// LogMe.logMe(LogMe.LOG_LEVEL_ERROR, e);
			// throw new Exception("error in reading server configuration");
			// }

			// process Custom Table field-value-map : Gaurav Berry
			// Map<String, String> attributesMapToCustomTable = new
			// HashMap<String, String>();
			// for (Entry<String, String> entry :
			// customTableFieldToApkFieldMap
			// .entrySet()) {
			// String customTableField = entry.getKey();
			// String apkField = entry.getValue();
			// String fieldValue = null;
			// if (attributesMapFromApk.containsKey(apkField)) {
			// fieldValue = attributesMapFromApk.get(apkField);
			// } else {
			// throw new Exception(
			// "no apk-field mapping found for custom field : "
			// + customTableField);
			// }
			// attributesMapToCustomTable.put(customTableField, fieldValue);
			// }

			// process attachments
			if (buc.getApplicantId() == null
					|| buc.getApplicantId().trim().isEmpty()) {
				throw new Exception("no applicant-id specified");
			}
			// map containing set of file per doc-type(as in OF)
			Map<String, Set<String>> documentMap = new HashMap<String, Set<String>>();
			String xPath = null;
			String dataMode = null;
			List<SMSDocument> smsDocuments;

			Organization organization = (Organization) ThreadUtils
			.getThreadVariable("organization");
			Configuration configuration = organization.getConfiguration();
			xPath = "./*/*/DataMode";
			dataMode = configuration
			.searchObjectGraph(xPath,
					configuration.getConfigurationXmlText()).item(0)
					.getTextContent().trim();

			if (dataMode.equalsIgnoreCase(MobileCaptureConstants.Data_Mode)) {
				for (Form form : buc.getForms()) {
					if (form.getFormName() == null
							|| form.getFormName().trim().isEmpty()) {
						throw new Exception("no form name specified");
					}
					if (form.getSections() != null) {
						for (Section section : form.getSections()) {
							if (section.getSectionName() == null
									|| section.getSectionName().trim()
									.isEmpty()) {
								throw new Exception("no section name specified");
							}
							if (section.getAttachments() != null) {
								for (Attachment attachment : section
										.getAttachments()) {
									processAttachmentsonFileSystem(attachment,
											attachmentsToProcess, documentMap);
								}
							}
						}
					}
					if (form.getAttachments() != null) {
						for (Attachment attachment : form.getAttachments()) {
							processAttachmentsonFileSystem(attachment,
									attachmentsToProcess, documentMap);
						}
					}
				}
				if (buc.getAttachments() != null) {
					for (Attachment attachment : buc.getAttachments()) {
						processAttachmentsonFileSystem(attachment,
								attachmentsToProcess, documentMap);
					}
				}
				smsDocuments = processDocumentsAsPerRequirementsOnFileSystem(documentMap);

			} else {

				for (Form form : buc.getForms()) {
					if (form.getFormName() == null
							|| form.getFormName().trim().isEmpty()) {
						throw new Exception("no form name specified");
					}
					if (form.getSections() != null) {
						for (Section section : form.getSections()) {
							if (section.getSectionName() == null
									|| section.getSectionName().trim()
									.isEmpty()) {
								throw new Exception("no section name specified");
							}
							if (section.getAttachments() != null) {
								for (Attachment attachment : section
										.getAttachments()) {
									processAttachments(
											attachment,
											attachmentsToProcess,
											documentMap,
											tempDir + buc.getApplicantId()
											+ File.separator
											+ form.getFormName()
											+ File.separator
											+ section.getSectionName()
											+ File.separator);
								}
							}
						}
					}
					if (form.getAttachments() != null) {
						for (Attachment attachment : form.getAttachments()) {
							processAttachments(
									attachment,
									attachmentsToProcess,
									documentMap,
									tempDir + buc.getApplicantId()
									+ File.separator
									+ form.getFormName()
									+ File.separator);
						}
					}
				}
				if (buc.getAttachments() != null) {
					for (Attachment attachment : buc.getAttachments()) {
						processAttachments(attachment, attachmentsToProcess,
								documentMap, tempDir + buc.getApplicantId()
								+ File.separator);
					}
				}
				smsDocuments = processDocumentsAsPerRequirements(documentMap);

			}

			// process documents
			// List<SMSDocument> smsDocuments =
			// processDocumentsAsPerRequirements(documentMap);
			 
			// gaurav-s 04042018
			HashMap<String,String> docIndexMap=new HashMap<String,String>();
			
			for (SMSDocument smsDocument : smsDocuments) {
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "uploading to SMS : "
						+ smsDocument.path);
				String imageIndexAndVolumeId = addAttachmentToSMS(jtsAddress,
						Short.valueOf(jtsPort), Short.valueOf(volumeIndex),
						cabinetName, smsDocument.path, "");
				String[] imageIndexAndVolumeIdArray = imageIndexAndVolumeId
				.split("#");
				//gaurav-s added 04042018
				docIndexMap.put(smsDocument.docType,imageIndexAndVolumeIdArray[0]);
				
				smsDocument.imageIndex = imageIndexAndVolumeIdArray[0];
				smsDocument.volumeId = imageIndexAndVolumeIdArray[1];
				String imageIndex = imageIndexAndVolumeId
				+ smsDocument.extenstion + "#" + smsDocument.docType
				+ "#" + smsDocument.size + "#" + smsDocument.noOfPages
				+ "#";
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "uploaded to SMS : "
						+ imageIndex + "(" + smsDocument.path + ")");
			}

			// get session-id for upload
			String sessionId = user.getAuthenticationToken();// getWfSessionId(user.getUserName(),
			// user.getPassword(),
			String processId = buc.getProcessId();
			JSONObject jsonObject = new JSONObject();
			// cabinetName, serverAddress, serverPort, appServerType);
			// : Gaurav Berry
			if (processId == null || processId.trim().isEmpty()) {
				try {
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "[submitResource -uploadWorkItem initiated]"+ new Date().toString());
					
					processId = uploadWorkItem(sessionId, cabinetName,
							jtsAddress, jtsPort, appServerType, processDefId, initiateAlso,
							attributesMapToUpload,complexAttributesMapToUpload, smsDocuments,gridtableMap, attributesMapFromApk,NestedgridtableMap);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "[submitResource -uploadWorkItem completed]"+ new Date().toString());
					
					if (processId != null && !processId.trim().isEmpty()) {
						buc.setProcessId(processId);
						buc.setStatusRemark("submitted");
						buc.saveOrUpdate();
						jsonObject.put("status", "submitted");
						jsonObject.put("processId", processId);
						jsonObject.put("applicantId", buc.getApplicantId());
						jsonObject.put("bucName", buc.getBucName());
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				}
			} 
			else {
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "[submitResource -getWorkItemIdForProcessInstance initiated]"+ new Date().toString());
				
				String workItemId = getWorkItemIdForProcessInstance(
						cabinetName, sessionId, processId, processDefId,
						jtsAddress, jtsPort, appServerType);
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "[submitResource -getWorkItemIdForProcessInstance completed]"+ new Date().toString());
				
				if (acquireLockOnWorkItem(cabinetName, sessionId, processId,
						workItemId, jtsAddress, jtsPort, appServerType)) {
					try {
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "[submitResource -updateWorkItemAttributes initiated]"+ new Date().toString());
						
						String value = updateWorkItemAttributes(cabinetName, sessionId,
								processId, workItemId, jtsAddress, jtsPort,
								appServerType, attributesMapToUpload,complexAttributesMapToUpload,gridtableMap,attributesMapFromApk,processDefId,activityId,activityType,NestedgridtableMap,docIndexMap);
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "[submitResource -updateWorkItemAttributes completed]"+ new Date().toString());
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "[VALUE FROM UPDATEWORKITEM CALL]"+value);
						
						//update authsign GAURAV-S 07-01-2019
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Going for companyDetails update: ");
						String childMapping = ExecuteDbqueryReturn("UPDATE NG_RLOS_GR_CompanyDetails set parentmapping_auth = (SELECT max(parentmapping_auth + 1) AS parentmapping_auth FROM NG_RLOS_GR_CompanyDetails WITH (NOLOCK))  OUTPUT Inserted.parentmapping_auth where comp_winame like '"+processId+"'");
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Child Mapping to Insert");
						String colName = "AuthSignCIFNo,AuthSignName,AuthSignNationality,AuthSignDOB,AuthSignVisaNo,AuthSignVisaExpiry,AuthSignStatus,AuthSignPassPortNo,AuthSignPassportExpiry,AuthSignShareholding,AuthSignSoleEmp,sign_winame,insertionOrderId,Designation,DesignationAsPerVisa,ChildMapping";
						Map<String,Map<String,String>> gridToColApkMap = gridtableMap.get("q_AuthSignDetails");
						Map<String,String>colApkMap = gridToColApkMap.get("cmplx_GR_AuthorizedSignDetails");
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Column to Apk Map" + colApkMap);
						List<String> list = new ArrayList<String>(Arrays.asList(colName.split(" , ")));

						JSONObject attributesjsonFromApk=new JSONObject(attributesMapFromApk);
						
						if(null!=attributesjsonFromApk.get("cmplx_GR_AuthorizedSignDetails")){	
						JSONArray authListArray=(JSONArray)new JSONParser().parse(attributesjsonFromApk.get("cmplx_GR_AuthorizedSignDetails").toString());
						for(int record_counter=0;record_counter<authListArray.size();record_counter++){
							JSONObject record=(JSONObject)authListArray.get(record_counter);
							
							for(String col : colApkMap.keySet()){
								if(col.equalsIgnoreCase("ChildMapping")){
									value = "'"+childMapping+"',";
								}
								value = "'"+record.get(gridToColApkMap.get(col))+"',";
								
								
							}
							value =  value.replaceAll(",$", "");
							String xmltoinsert= ExecuteQuery_APInsert("ng_rlos_gr_AuthSignDetails",
			                		 colName,
			                		 value, 
			                		 cabinetName,
			                		 user.getAuthenticationToken());
							 LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "xmltoinsert: "+ xmltoinsert);
				           	  String insertoutxml = WFCallBroker.execute(xmltoinsert, jtsAddress, Integer.parseInt(jtsPort), 0);
				                 LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "insertoutxml: "+ insertoutxml);
				                 LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "[submitResource -ExecuteQuery_APInsert completed]"+ new Date().toString());
				 				
						}
						}
						else{
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "[cmplx_GR_AuthorizedSignDetails not in output modal]"+ new Date().toString());
						}
						
						//	END	
						
						
						
						String processFolderIndex = null;
						if (smsDocuments.size() > 0) {
							processFolderIndex = getFolderIndexForProcessFolder(
									cabinetName, sessionId, processId,
									jtsAddress, jtsPort, appServerType);
						}
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "[submitResource -updateWorkItemDocument initiated]"+ new Date().toString());
						
						for (SMSDocument document : smsDocuments) {
							updateWorkItemDocument(cabinetName, sessionId,
									processId, processFolderIndex, jtsAddress,
									jtsPort, appServerType, document);
						}
						
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "[submitResource -updateWorkItemDocument completed]"+ new Date().toString());
						
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "value status =>"+ value);
						String[] result = value.split("\\|\\|\\|");
						String message = result[0];
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "message =>"+ message);
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "insertionId =>"+ result[1]);
						if (message != null && !message.trim().isEmpty() && message.equalsIgnoreCase("success")) {
							buc.setStatusRemark("submitted");
							buc.saveOrUpdate();
							jsonObject.put("status", "submitted");
							jsonObject.put("processId", processId);
							jsonObject.put("applicantId", buc.getApplicantId());
							jsonObject.put("bucName", buc.getBucName());
							jsonObject.put("insertionId", result[1]);
						}
					} catch (Exception ex) {
						LogMe.logMe(LogMe.LOG_LEVEL_ERROR, ex);
						throw ex;

					} catch (Throwable t) {
						LogMe.logMe(LogMe.LOG_LEVEL_ERROR, t);
						throw t;
					}
				}
			}
			
			//Below added by Tarang on 01/05/2019
			
			/*String ColName="BlacklistDate,BlacklistFlag,BlacklistReasonCode,cif_DuplicationFlag,cif_SearchType,cif_wi_name,CustId,is_primary_cif,NegatedDate,NegatedFlag,NegatedReasonCode,no_of_product,PassportNo";
			//String Values="'"+curr_entry.get("BlacklistDate")+"','"+curr_entry.get("BlacklistFlag")+"','"+curr_entry.get("BlacklistReasonCode")+"','"+curr_entry.get("DuplicationFlag")+"','"+curr_entry.get("SearchType")+"','','"+curr_entry.get("CustId")+"','','"+curr_entry.get("NegatedDate")+"','"+curr_entry.get("NegatedFlag")+"','"+curr_entry.get("NegatedReasonCode")+"','"+curr_entry.get("Products")+"','"+curr_entry.get("PassportNum")+"'"; LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Values: "+ Values);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "ColName: "+ ColName);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "[submitResource -ExecuteQuery_APInsert initiated for Blacklist entries]");
				
			String xmltoinsert= ExecuteQuery_APInsert("ng_rlos_cif_detail",
            		 ColName,
            		 Values, 
            		 cabinetName,
            		 user.getAuthenticationToken());
           	  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "xmltoinsert: "+ xmltoinsert);
           	  String insertoutxml = WFCallBroker.execute(xmltoinsert, jtsAddress, Integer.parseInt(jtsPort), 0);
			  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "insertoutxml: "+ insertoutxml);
			  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "[submitResource -ExecuteQuery_APInsert completed]");*/
			  //Above added by Tarang by 01/05/2019
			/* kush bharadwaj
			 * 28-06-2018
			 * AP_Insert for decision_history
			 * */
			
			JSONObject model=new JSONObject();
			String decision = "";
			String remarks="";
            if (buc.getForms() != null) {
                for (Form form : buc.getForms()) {
                    String outputModel = form.getOutputModel();
                    if (outputModel == null || outputModel.trim().isEmpty()) {
                        throw new Exception("no input");
                    }
                    LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "custom outputModel for =>" + outputModel);
                    JSONParser jParser = new JSONParser();
                    model = (JSONObject) jParser.parse(outputModel);

                }
            } 

            JSONObject json = (JSONObject) model.get("decisiontab");
            LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "JSON Result for testing on 01/05 =>" + json);
            decision=json.get("decision_type").toString();
            remarks=json.get("decision_remarks").toString();
						
			if(processId!=null && (decision.equalsIgnoreCase("Submit") || decision.equalsIgnoreCase("Reject"))){
				String Designation = "";
				
				String ColName="dateLastChanged,userName,workstepName,remarks,dec_wi_name,entry_date ,Decision";
				String Values="GETDATE(),'"+user.getUserName()+"','mobility','"+remarks+"','"+processId+"',GETDATE(),'"+decision+"'";
				 LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Values: "+ Values);
				 LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "ColName: "+ ColName);
				 LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "[submitResource -ExecuteQuery_APInsert initiated]"+ new Date().toString());
					
				String xmltoinsert= ExecuteQuery_APInsert("ng_rlos_gr_decision",
                		 ColName,
                		 Values, 
                		 cabinetName,
                		 user.getAuthenticationToken());
           	  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "xmltoinsert: "+ xmltoinsert);
           	  String insertoutxml = WFCallBroker.execute(xmltoinsert, jtsAddress, Integer.parseInt(jtsPort), 0);
                 LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "insertoutxml: "+ insertoutxml);
                 LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "[submitResource -ExecuteQuery_APInsert completed]"+ new Date().toString());
 				
                // added by abhishek on 1/11/2018 for Updating initiator Designation in ext table at submission start
                String squerydesig = "select designation from NG_MASTER_SourceCode where userid ='" + user.getUserName() + "'";
 				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "user designation is " + squerydesig);
 				List < List < String >> squerydesigXML = ExecuteSelectQueryGetList(squerydesig);
 				if (!squerydesigXML.isEmpty()) {
 					if (squerydesigXML.get(0).get(0) != null) {
 						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "INSIDE squerydesigXML+ " + squerydesigXML.get(0).get(0));
 						Designation = squerydesigXML.get(0).get(0);
 					}
 				}
 				if(!"".equals(Designation)){
 					String sWhere="WIname = '"+processId+"'";
 	 				String strInputXML=ExecuteQuery_APUpdate("NG_RLOS_EXTTABLE", "InitiatorDesig","'"+ Designation+"'",sWhere , cabinetName,user.getAuthenticationToken());
 	 				String  strOutputXml = WFCallBroker.execute(strInputXML, jtsAddress, Integer.parseInt(jtsPort), 0);
 					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"strOutputXml for InitiatorDesig flag update is:"+strOutputXml);
 				}
 				
	            // added by abhishek on 1/11/2018 for Updating initiator Designation in ext table at submission end
				
			}	
			

			toReturn.setStream(new String(jsonObject.toString()).getBytes());
			// Work Item Submitted Confirmation Only
			// toReturn.setStream(new String("submitted").getBytes());

			// for process ID Responce
			// toReturn.setStream(new
			// String("submitted, "+processId).getBytes());
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "[submitResource completed]"+ new Date().toString());
			
			return toReturn;
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			// sw.toString();
			LogMe.logMe(LogMe.LOG_LEVEL_ERROR,
					"EXCEPTION OCCOURED while submitting->>" + sw);
			String message = ex.getMessage();

			JSONObject exceptionMessage = new JSONObject();
			exceptionMessage.put("exceptionMessage", message);
			exceptionMessage.put("bucId", buc.getApplicantId());
			exceptionMessage.put("bucName", buc.getBucName());

			throw new Exception(exceptionMessage.toString());
		} catch (Throwable t) {
			// TODO: handle exception
			LogMe.logMe(LogMe.LOG_LEVEL_ERROR,
					"EXCEPTION OCCOURED while submitting->> t" + t);
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			t.printStackTrace(pw);
			// sw.toString();
			LogMe.logMe(LogMe.LOG_LEVEL_ERROR,
					"EXCEPTION OCCOURED while submitting->>" + sw);
			String message = t.getMessage();

			JSONObject exceptionMessage = new JSONObject();
			exceptionMessage.put("exceptionMessage", message);
			exceptionMessage.put("bucId", buc.getApplicantId());
			exceptionMessage.put("bucName", buc.getBucName());

			throw new Exception(exceptionMessage.toString());
		}
	}
	
	/**		
	 * 		08/10/2018
	 * 		documents parallel upload
	 * */
	
	public StreamedResource submitAttachment(Attachment attachment) throws Exception
	  {
	    StreamedResource toReturn = null;
	    Long referenceAttachmentId = attachment.getId();
	    toReturn = new StreamedResource();
	    org.json.simple.JSONObject jsonObject = new org.json.simple.JSONObject();
	    jsonObject.put("status", "submitted");
	    jsonObject.put("referenceAttachmentId", referenceAttachmentId);
	    toReturn.setStream(new String(jsonObject.toString()).getBytes());
	    return toReturn;
	  }
	
	/**		END		*/

	
	
	
	
	

	private String ExecuteQuery_APUpdate(String tableName, String colName, String values, String sWhere, String cabinetName, String sessionId)
	{
		String sInputXML = "<?xml version=\"1.0\"?>"+
		"<APUpdate_Input><Option>APUpdate</Option>"+
		"<TableName>"+tableName+"</TableName>"+
		"<ColName>"+colName+"</ColName>"+
		"<Values>"+values+"</Values>"+
		"<WhereClause>"+sWhere+"</WhereClause>"+
		"<EngineName>"+cabinetName+"</EngineName>"+
		"<SessionId>"+sessionId+"</SessionId>"+
		"</APUpdate_Input>";
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "sInputXML" + sInputXML);
		return sInputXML;           

	}

	private void processAttachments(Attachment attachment,
			List<String> attachmentsToProcess,
			Map<String, Set<String>> documentMap, String localDirectoryToWrite)
	throws Exception {
		Set<String> attachmentLocalPaths = null;
		if (attachmentsToProcess.contains(attachment.getAttachmentName())) {
			String attachmentLocalPath = writeAttachmentToLocalDirectory(
					attachment, localDirectoryToWrite);
			attachmentLocalPaths = documentMap.get(attachment
					.getAttachmentType());
			if (attachmentLocalPaths == null) {
				attachmentLocalPaths = new HashSet<String>();
			}
			attachmentLocalPaths.add(attachmentLocalPath);
			documentMap.put(attachment.getAttachmentType(),
					attachmentLocalPaths);
		}
	}

	private void processAttachmentsonFileSystem(Attachment attachment,
			List<String> attachmentsToProcess,
			Map<String, Set<String>> documentMap) throws Exception {
		Set<String> attachmentLocalPaths = null;
		if (attachmentsToProcess.contains(attachment.getAttachmentName())) {
			String attachmentLocalPath = attachment.getBase64EncodedData();
			attachmentLocalPaths = documentMap.get(attachment
					.getAttachmentType());
			if (attachmentLocalPaths == null) {
				attachmentLocalPaths = new HashSet<String>();
			}
			attachmentLocalPaths.add(attachmentLocalPath);
			documentMap.put(attachment.getAttachmentType(),
					attachmentLocalPaths);
		}
	}

	@Override
	public List<Resource> getPermittedInstances(User user,
			Map<String, String> searchCriteria) throws Exception {
		return null;
	}

	@Override
	public void lockInstancesAtBackEnd(List<Resource> resources)
	throws Exception {

	}
	private String uploadWorkItem(String sessionId, String cabinetName,
			String jtsAddress, String jtsPort, String appServerType,
			String processDefId, String initiateAlso,
			Map<String, String> attributesMap, Map<String, Map<String, String>> complexAttributes,  List<SMSDocument> documents,Map<String,Map<String,Map<String,String>>> gridtableMap, Map<String, String> attributesMapFromApk,Map <String,Map<String,Map<String, Map<String, String>>>> NestedgridtableMap)
	throws Exception {
		StringBuilder wfUploadWorkItemInputXML = new StringBuilder();
		wfUploadWorkItemInputXML.append("<?xml version=\"1.0\"?>");
		wfUploadWorkItemInputXML.append("<WFUploadWorkItem_Input>");
		wfUploadWorkItemInputXML.append("<Option>WFUploadWorkItem</Option>");
		wfUploadWorkItemInputXML.append("<EngineName>" + cabinetName
				+ "</EngineName>");
		wfUploadWorkItemInputXML.append("<SessionId>" + sessionId
				+ "</SessionId>");
		wfUploadWorkItemInputXML.append("<ProcessDefId>" + processDefId
				+ "</ProcessDefId>");
		wfUploadWorkItemInputXML.append("<DataDefName></DataDefName>");
		wfUploadWorkItemInputXML.append("<UserDefVarFlag>Y</UserDefVarFlag>");
		wfUploadWorkItemInputXML.append("<Fields></Fields>");
		wfUploadWorkItemInputXML.append("<InitiateAlso>" + initiateAlso
				+ "</InitiateAlso>");
		wfUploadWorkItemInputXML.append("<Attributes>");
		for (Entry<String, String> entry : attributesMap.entrySet()) {
			//			wfUploadWorkItemInputXML.append(entry.getKey() + (char) 21
			//					+ entry.getValue() + (char) 25);
			wfUploadWorkItemInputXML.append("<" + entry.getKey() + ">");
			wfUploadWorkItemInputXML.append(entry.getValue());
			wfUploadWorkItemInputXML.append("</" + entry.getKey() + ">");
		}
		for (Entry<String, Map<String, String>> entry : complexAttributes.entrySet()) {
			//gaurav-s to skip tag from cmplx parent tag
			if(!entry.getKey().trim().equals("q_cif_detail")){
				wfUploadWorkItemInputXML.append("<" + entry.getKey() + ">");
			}
			for (Entry<String, String> subentries : entry.getValue().entrySet()) {
				wfUploadWorkItemInputXML.append("<" + subentries.getKey() + ">");
				wfUploadWorkItemInputXML.append( subentries.getValue());
				wfUploadWorkItemInputXML.append("</" + subentries.getKey() + ">");
			}
			//gaurav-s changes
			if(gridtableMap.get(entry.getKey())!=null){
				Map<String, Map<String, String>> grid_to_apk_column_map=gridtableMap.get(entry.getKey());
				for (Entry<String, Map<String, String>> gridentry : grid_to_apk_column_map.entrySet()) {
					JSONObject attributesjsonFromApk=new JSONObject(attributesMapFromApk);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "attributesjsonFromApk : "	+ attributesjsonFromApk +"  "+ gridentry.getKey());
					JSONArray grid_table_array=(JSONArray)new JSONParser().parse(attributesjsonFromApk.get(gridentry.getKey()).toString());
					for(int record_counter=0;record_counter<grid_table_array.size();record_counter++){
						JSONObject record=(JSONObject)grid_table_array.get(record_counter);
						wfUploadWorkItemInputXML.append("<" + gridentry.getKey() + ">");
						for (Entry<String, String> subentries : gridentry.getValue().entrySet()) 
						{
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Getting : "
									+ subentries.getValue()+" In Output Model");
							wfUploadWorkItemInputXML.append("<" + subentries.getKey() + ">");
							if(record.get(subentries.getValue())==null||record.get(subentries.getValue()).toString().equalsIgnoreCase("undefined")){
								wfUploadWorkItemInputXML.append("");

							}
							else{
								wfUploadWorkItemInputXML.append(record.get(subentries.getValue()) );
							}
							wfUploadWorkItemInputXML.append("</" + subentries.getKey() + ">");
						}
						//for nested map gaurav-s 15012018
//						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "parentChildNestedGridMap : " + parentChildNestedGridMap);
//						if(parentChildNestedGridMap.containsKey(gridentry.getKey().trim())){
//							HashMap<String, HashMap<String,String>> currententry=parentChildNestedGridMap.get(gridentry.getKey().trim());
//							Set<String> nestedgrid  = currententry.keySet();
//							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "nestedgrid : " + nestedgrid);
//							for(String nestedkey : nestedgrid){
//								LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "nestedkey : " + nestedkey);
//							wfUploadWorkItemInputXML.append("<"+ nestedkey+">");
//							HashMap<String, String> xmlId=new HashMap<String, String>();
//							Set<String> childset=xmlId.keySet();
//							JSONArray currentNestedGridArray =(JSONArray)record.get(nestedkey);
//							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "currentNestedGridArray : " + currentNestedGridArray);
//							for(int x = 0;x<currentNestedGridArray.size();x++){
//								JSONObject nestedgridentry=(JSONObject)currentNestedGridArray.get(x);
//								LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "nestedgridentry : " + nestedgridentry);
//								for(String nestedchild : childset){
//									LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "nestedchild tag  : " + nestedchild);
//									wfUploadWorkItemInputXML.append("<"+ nestedchild+">");
//									//cmplx_GR_AuthorizedSignDetails
//									LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "nestedchild value in op modal : " + xmlId.get(nestedgridentry.get(nestedchild)));
//									xmlId.get(nestedgridentry.get(nestedchild));
//									wfUploadWorkItemInputXML.append("</"+ nestedchild+">");	
//									
//								}
//							}
//							
//							wfUploadWorkItemInputXML.append("</"+ nestedkey+">");
//							}
//							
//							
//						}
						//for nested map gaurav-s 15012018
						wfUploadWorkItemInputXML.append("</" + gridentry.getKey() + ">");
					}
				}
			}
			//gaurav-s changes END
			//gaurav-s to skip tag from cmplx parent tag
			if(!entry.getKey().trim().equals("q_cif_detail")){
				wfUploadWorkItemInputXML.append("</" + entry.getKey() + ">");
			}

		}
		wfUploadWorkItemInputXML.append("</Attributes>");

		wfUploadWorkItemInputXML.append("<Documents>");
		for (SMSDocument document : documents) {
			wfUploadWorkItemInputXML.append(document.docType);
			wfUploadWorkItemInputXML.append((char) 21);
			wfUploadWorkItemInputXML.append(document.imageIndex);
			wfUploadWorkItemInputXML.append("#");
			wfUploadWorkItemInputXML.append(document.volumeId);
			wfUploadWorkItemInputXML.append((char) 21);
			wfUploadWorkItemInputXML.append(document.noOfPages);
			wfUploadWorkItemInputXML.append((char) 21);
			wfUploadWorkItemInputXML.append(document.size);
			wfUploadWorkItemInputXML.append((char) 21);
			wfUploadWorkItemInputXML.append(document.extenstion);
			wfUploadWorkItemInputXML.append((char) 21);
			wfUploadWorkItemInputXML.append("I");
			wfUploadWorkItemInputXML.append((char) 21);
			wfUploadWorkItemInputXML.append(document.docType);
			wfUploadWorkItemInputXML.append((char) 25);
		}
		wfUploadWorkItemInputXML.append("</Documents>");
		wfUploadWorkItemInputXML.append("</WFUploadWorkItem_Input>");

		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "inputXML to upload workitem : "
				+ wfUploadWorkItemInputXML.toString());
		//		String wfUploadWorkItemOutputXML = makeCall(jtsAddress,
		//				Short.valueOf(jtsPort), appServerType,
		//				wfUploadWorkItemInputXML.toString());
		//gaurav-s changes to With Wrapper Call
		String wfUploadWorkItemOutputXML=makeCall(jtsAddress, Short.parseShort(jtsPort), wfUploadWorkItemInputXML.toString());
		if (wfUploadWorkItemOutputXML == null
				|| wfUploadWorkItemOutputXML.trim().isEmpty()) {
			throw new Exception(
			"no outputXML generated while uploading workitem");
		}
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "outputXML to upload workitem : "
				+ wfUploadWorkItemOutputXML);
		XMLParser xmlParser = new XMLParser();
		xmlParser.setInputXML(wfUploadWorkItemOutputXML);
		if (xmlParser.getValueOf("MainCode").trim().equals("0")) {
			String processId = xmlParser.getValueOf("ProcessInstanceId");
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "processId =>" + processId);
			return processId;
		} else {
			throw new Exception(xmlParser.getValueOf("Error"));
		}

	}

	/*public String xmlToJson(String response) {
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Inside xmlToJson");
		JSONObject returnObj = new JSONObject();
		try {
			org.json.JSONObject xmlJSONObj = XML.toJSONObject(response);
			String jsonPrettyPrintString = xmlJSONObj.toString();
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Response xmlToJson" +jsonPrettyPrintString);
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject) parser.parse(jsonPrettyPrintString);

			JSONObject results = (JSONObject) jsonObject.get("WFSetAttributes_Output");   
			JSONObject results1 = (JSONObject) results.get("InsertionOrderIdValues");   
			JSONArray getArray = (JSONArray) results1.get("InsertionOrderIdValue");
			
			
			for (int i = 0; i < getArray.size(); i++) {
	            JSONObject objects = (JSONObject) getArray.get(i);
	            LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "JSON HashID=" +objects.get("HashId")+" InsertionOrderId="+ objects.get("InsertionOrderId"));
	            returnObj.put(objects.get("HashId"), objects.get("InsertionOrderId"));
	        }
		} catch (Exception e) {
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Exception xmlToJson" +e.getMessage());
			e.printStackTrace();
		}
		return returnObj.toString();
	}*/

	public static String xmlToJson(String response) {
		//LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Inside xmlToJson");
		JSONObject returnObj = new JSONObject();
		try {
			org.json.JSONObject xmlJSONObj = XML.toJSONObject(response);
			String jsonPrettyPrintString = xmlJSONObj.toString();
			System.out.println("Response xmlToJson" +jsonPrettyPrintString);
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject) parser.parse(jsonPrettyPrintString);

			JSONObject results = (JSONObject) jsonObject.get("WFSetAttributes_Output");   
			JSONObject results1 = (JSONObject) results.get("InsertionOrderIdValues");  
			/*
			 * Author			:	Sumit Balyan
			 * Date				:	23-05-2018
			 * Description	:	Single/Multiple InsertionOrderIdValue issue
			 * 
			 */
			
			/*Start*/
			if (results1.get("InsertionOrderIdValue") instanceof JSONObject) {
			    JSONObject objects = (JSONObject) results1.get("InsertionOrderIdValue");
			    LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "JSON HashID=" +objects.get("HashId")+" InsertionOrderId="+ objects.get("InsertionOrderId"));
	            returnObj.put( objects.get("HashId"), objects.get("InsertionOrderId"));
			} else {
			    JSONArray getArray = (JSONArray) results1.get("InsertionOrderIdValue");
				
				
				for (int i = 0; i < getArray.size(); i++) {
		            JSONObject objects = (JSONObject) getArray.get(i);
		            LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "JSON HashID=" +objects.get("HashId")+" InsertionOrderId="+ objects.get("InsertionOrderId"));
		            returnObj.put( objects.get("HashId"), objects.get("InsertionOrderId"));
		        }
			}
			
			/*End*/
			
		} catch (Exception e) {
			System.out.println("Exception xmlToJson" +e.getMessage());
			e.printStackTrace();
		}
		System.out.println("xmlToJson Output:: " +returnObj.toString());
		return returnObj.toString();
	}
	private String wfCompleteWorkItem(String cabinetName, String sessionID,
			String jtsAddress, String jtsPort,String appServerType, String processInstanceID,
			String workitemId) throws Exception {
		StringBuilder wfCompleteWorkItemInputXML = new StringBuilder();
		wfCompleteWorkItemInputXML.append("<?xml version=\"1.0\"?>");
		wfCompleteWorkItemInputXML.append("<WMCompleteWorkItem_Input>");
		wfCompleteWorkItemInputXML
		.append("<Option>WMCompleteWorkItem</Option>");
		wfCompleteWorkItemInputXML.append("<EngineName>" + cabinetName
				+ "</EngineName>");
		wfCompleteWorkItemInputXML.append("<SessionId>" + sessionID
				+ "</SessionId>");
		wfCompleteWorkItemInputXML.append("<ProcessInstanceId>"
				+ processInstanceID + "</ProcessInstanceId>");
		wfCompleteWorkItemInputXML.append("<WorkitemId>" + workitemId
				+ "</WorkitemId>");
		wfCompleteWorkItemInputXML.append("<AuditStatus>" + ""
				+ "</AuditStatus>");
		wfCompleteWorkItemInputXML.append("</WMCompleteWorkItem_Input>");

		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "inputXML to custom table : "
				+ wfCompleteWorkItemInputXML.toString());
		//		String wfCompleteWorkItemOutputXML = makeCall(jtsAddress,
		//				Short.valueOf(jtsPort), appServerType, wfCompleteWorkItemInputXML.toString());
		String wfCompleteWorkItemOutputXML=makeCall(jtsAddress, Short.parseShort(jtsPort), wfCompleteWorkItemInputXML.toString());
		if (wfCompleteWorkItemOutputXML == null
				|| wfCompleteWorkItemOutputXML.trim().isEmpty()) {
			throw new Exception("no outputXML generated while custom table");
		}
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "outputXML of custom table : "
				+ wfCompleteWorkItemOutputXML);
		XMLParser xmlParser = new XMLParser();
		xmlParser.setInputXML(wfCompleteWorkItemOutputXML);
		if (xmlParser.getValueOf("MainCode").trim().equals("0")) {
			return "success";
		} else {
			throw new Exception(xmlParser.getValueOf("Error"));
		}
	}

	private String wfInsertInput(String sessionId, String cabinetName,
			String jtsAddress, String jtsPort, String appServerType, String sTableName,
			String sColumnName, String sColumnValue) throws Exception {
		StringBuilder wfInsertInputXML = new StringBuilder();
		wfInsertInputXML.append("<?xml version=\"1.0\"?>");
		wfInsertInputXML.append("<WFInsert_Input>");
		wfInsertInputXML.append("<Option>WFInsert_new</Option>");
		wfInsertInputXML.append("<EngineName>" + cabinetName + "</EngineName>");
		wfInsertInputXML.append("<SessionId>" + sessionId + "</SessionId>");
		wfInsertInputXML.append("<TableName>" + sTableName + "</TableName>");
		wfInsertInputXML.append("<ColName>" + sColumnName + "</ColName>");
		wfInsertInputXML.append("<Values>" + sColumnValue + "</Values>");
		wfInsertInputXML.append("</WFInsert_Input>");

		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "inputXML to custom table : "
				+ wfInsertInputXML.toString());
		String wfInsertOutputXML = makeCall(jtsAddress, Short.valueOf(jtsPort),
				appServerType, wfInsertInputXML.toString());
		if (wfInsertOutputXML == null || wfInsertOutputXML.trim().isEmpty()) {
			throw new Exception("no outputXML generated while custom table");
		}
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "outputXML of custom table : "
				+ wfInsertOutputXML);
		XMLParser xmlParser = new XMLParser();
		xmlParser.setInputXML(wfInsertOutputXML);
		if (xmlParser.getValueOf("MainCode").trim().equals("0")) {
			return "success";
		} else {
			throw new Exception(xmlParser.getValueOf("Error"));
		}
	}

	//with wrapper
	private String makeCall(String jtsAddress, short jtsPort, String
			inputXML)
	throws Exception {
		String output = null;
		try {
			int debug = 0; // (0|1)
			output = DMSCallBroker
			.execute(inputXML, jtsAddress, jtsPort, debug);
		} catch (Exception e) {
			e.printStackTrace();
			LogMe.logMe(LogMe.LOG_LEVEL_ERROR, "error=> " + e);
			throw new Exception("OF setup incomplete");
		} catch (Error e) {
			e.printStackTrace();
			LogMe.logMe(LogMe.LOG_LEVEL_ERROR, "error=> " + e);
			throw new Exception("OF setup incomplete");
		}
		return output;
	}

	// without wrapper
	public String makeCall(String jtsAddress, short jtsPort, String appServerType, String inputXML)
	throws Exception {
		String output = null;
		try {
			int debug = 0; // (0|1)
			//			output = DMSCallBroker.execute(inputXML, jtsAddress, jtsPort,
			//					"8859_1", debug, true);
			output = NGEjbClient.getSharedInstance().makeCall(jtsAddress, String.valueOf(jtsPort),appServerType, inputXML);
		} catch (Exception e) {
			e.printStackTrace();
			LogMe.logMe(LogMe.LOG_LEVEL_ERROR, "error=> " + e);
			throw new Exception("OF setup incomplete");
		} catch (Error e) {
			e.printStackTrace();
			LogMe.logMe(LogMe.LOG_LEVEL_ERROR, "error=> " + e);
			throw new Exception("OF setup incomplete");
		}
		return output;
	}

	private String addAttachmentToSMS(String jtsAddress, short jtsPort,
			short volumeId, String cabinetName, String filePath,
			String userIndex) throws Exception {
		JtsConnection jtsConnection = null;
		JPDBRecoverDocData docDBData = new JPDBRecoverDocData();
		JPISIsIndex newIsIndex = new JPISIsIndex();
		// add document to ImageServer (SMS)
		try {
			CPISDocumentTxn.AddDocument_MT(jtsConnection, jtsAddress, jtsPort,
					cabinetName, volumeId, filePath, docDBData, userIndex,
					newIsIndex);
		} catch (JPISException e) {
			e.printStackTrace();
			LogMe.logMe(LogMe.LOG_LEVEL_ERROR, "JPISException=> " + e);
			throw new Exception("error in adding to SMS");
		} catch (Exception e) {
			e.printStackTrace();
			LogMe.logMe(LogMe.LOG_LEVEL_ERROR, "error=> " + e);
			throw new Exception("error in adding to SMS");
		}
		String imageServerIndex = newIsIndex.m_nDocIndex + "#"
		+ newIsIndex.m_sVolumeId + "#";
		if (imageServerIndex.indexOf('#') < 1 && imageServerIndex.length() < 3) {
			throw new Exception("can't submit to SMS =>" + filePath);
		} else {
			return imageServerIndex;
		}
	}

	/*
	 * private String writeAttachmentToLocalDirectory(Attachment attachment,
	 * String localDirectory) throws Exception { String fileName =
	 * attachment.getAttachmentName(); String base64String = null; String
	 * fileExtension = null; byte[] dataStream = null; try { fileExtension =
	 * attachment.getAttachmentFormat().substring( new
	 * String("image/").length()); } catch (Exception e) { e.printStackTrace();
	 * throw new Exception("wrong image format"); } // write image to
	 * local-file-system LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Attachment-name::"
	 * + attachment.getAttachmentName()); base64String =
	 * attachment.getBase64EncodedData(); base64String =
	 * base64String.replaceAll(" ", "+"); dataStream =
	 * Base64.decodeBase64(base64String);
	 * 
	 * GenericUtils.writeImageToFile(localDirectory, fileName, fileExtension,
	 * dataStream); String filePath = localDirectory + fileName + "." +
	 * fileExtension; LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
	 * "file written locally =>" + filePath); return filePath; }
	 */

	private String writeAttachmentToLocalDirectory(Attachment attachment,
			String localDirectory) throws Exception {

		String fileName = attachment.getAttachmentName();
		String base64String = null;
		String fileExtension = null;
		byte[] dataStream = null;
		try {
			fileExtension = attachment.getAttachmentFormat().substring(
					new String("image/").length());
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("wrong image format");
		}

		// write image to local-file-system
		// -- DEBUG-to-INFO
		String imageBase64EncodedData = "";
		// String imageBase64EncodedDataBytearray = null;
		if (attachment.getIsAttachmentEncrypted() == true) {
			String encryptionAlgorithm = attachment.getEncryptionType();
			if (encryptionAlgorithm != null
					&& encryptionAlgorithm.toLowerCase().equals("des")) {
				String base64DecodedEncryptedBase64Data = new String(
						attachment.getBase64EncodedData());
				System.out.println(base64DecodedEncryptedBase64Data);
				base64DecodedEncryptedBase64Data = base64DecodedEncryptedBase64Data
				.replaceAll(" ", "+");

				byte[] decodedBytesOfEncryptedBase64Data = Base64
				.decodeBase64(base64DecodedEncryptedBase64Data
						.getBytes());
				String encryptedBase64DataStringData = new String(
						decodedBytesOfEncryptedBase64Data, "UTF8");

				imageBase64EncodedData = EncryptionUtils
				.decrypt(encryptedBase64DataStringData);

				base64DecodedEncryptedBase64Data = null;
				decodedBytesOfEncryptedBase64Data = null;
				encryptedBase64DataStringData = null;
				// imageBase64EncodedData = new
				// String(imageBase64EncodedDataBytearray);
			}
		} else {
			imageBase64EncodedData = attachment.getBase64EncodedData();
			imageBase64EncodedData = imageBase64EncodedData
			.replaceAll(" ", "+");
		}
		LogMe.logMe(LogMe.LOG_LEVEL_INFO,
				"Attachment-name::" + attachment.getAttachmentName());
		// base64String = attachment.getBase64EncodedData();
		dataStream = Base64.decodeBase64(imageBase64EncodedData);

		GenericUtils.writeImageToFile(localDirectory, fileName, fileExtension,
				dataStream);
		String filePath = localDirectory + fileName + "." + fileExtension;
		// -- DEBUG-to-INFO
		LogMe.logMe(LogMe.LOG_LEVEL_INFO, "file written locally =>" + filePath);
		return filePath;
	}

	public void populateFromJsonObject(JSONObject jObject,
			Map<String, String> valueMap, List<String> attachmentList) {
		Iterator<?> iter = jObject.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Object> entry = (Entry<String, Object>) iter.next();
			Object obj = entry.getValue();
			if (entry.getKey().equalsIgnoreCase("attachments")) {
				JSONArray attachments = (JSONArray) obj;
				for (Iterator<JSONObject> iterator = attachments.iterator(); iterator
				.hasNext();) {
					Object object = (Object) iterator.next();
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "attachmentToProcess=>"
							+ String.valueOf(object));
					attachmentList.add(String.valueOf(object));
				}
				continue;
			}
			if (obj instanceof JSONObject) {
				populateFromJsonObject((JSONObject) obj, valueMap,
						attachmentList);
			} else if (obj instanceof JSONArray) {
				populateFromJsonArray((JSONArray) obj, valueMap, attachmentList);
			} else {
				valueMap.put(entry.getKey(), String.valueOf(entry.getValue()));
			}
		}
	}

	public void populateFromJsonArray(JSONArray jArray,
			Map<String, String> valueMap, List<String> attachmentList) {
		Iterator<?> iter = jArray.listIterator();
		while (iter.hasNext()) {
			Object obj = iter.next();
			if (obj instanceof JSONObject) {
				populateFromJsonObject((JSONObject) obj, valueMap,
						attachmentList);
			} else if (obj instanceof JSONArray) {
				System.out.println("<===JSONArray>" + obj);
			} else {
				System.out.println("<===>" + obj.getClass()
						+ String.valueOf(obj));
			}
		}
	}

	private Boolean acquireLockOnWorkItem(String cabinetName, String sessionId,
			String processId, String workItemId, String jtsAddress,
			String jtsPort, String appServerType) throws Exception {
		StringBuilder wmGetWorkItemInputXml = new StringBuilder();
		wmGetWorkItemInputXml.append("<?xml version=\"1.0\"?>");
		wmGetWorkItemInputXml.append("<WMGetWorkItem_Input>");
		wmGetWorkItemInputXml.append("<Option>WMGetWorkItem</Option>");
		wmGetWorkItemInputXml.append("<EngineName>" + cabinetName
				+ "</EngineName>");
		wmGetWorkItemInputXml
		.append("<SessionId>" + sessionId + "</SessionId>");
		wmGetWorkItemInputXml.append("<ProcessInstanceId>" + processId
				+ "</ProcessInstanceId>");
		wmGetWorkItemInputXml.append("<WorkItemId>" + workItemId
				+ "</WorkItemId>");
		wmGetWorkItemInputXml.append("</WMGetWorkItem_Input>");

		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
				"inputXML for acquiring lock on workitem : "
				+ wmGetWorkItemInputXml.toString());
		//		String wmGetWorkItemOutputXml = makeCall(jtsAddress,
		//				Short.valueOf(jtsPort), appServerType, wmGetWorkItemInputXml.toString());
		String wmGetWorkItemOutputXml=makeCall(jtsAddress, Short.parseShort(jtsPort), wmGetWorkItemInputXml.toString());
		if (wmGetWorkItemOutputXml == null
				|| wmGetWorkItemOutputXml.trim().isEmpty()) {
			throw new Exception(
					"no outputXML generated while acuiring lock on process : "
					+ processId);
		}
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
				"outputXML for acquiring lock on workitem : "
				+ wmGetWorkItemOutputXml);
		XMLParser xmlParser = new XMLParser();
		xmlParser.setInputXML(wmGetWorkItemOutputXml);
		if (xmlParser.getValueOf("MainCode").trim().equals("0")) {
			return true;
		} else {
			throw new Exception(xmlParser.getValueOf("Error"));
		}
	}

	private void releaseLockOnWorkItem(String cabinetName, String sessionId,
			String processId, String workItemId, String jtsAddress,
			String jtsPort, String appServerType) throws Exception {
		StringBuilder wmUnlockWorkItemInputXml = new StringBuilder();
		wmUnlockWorkItemInputXml.append("<?xml version=\"1.0\"?>");
		wmUnlockWorkItemInputXml.append("<WMUnlockWorkItem_Input>");
		wmUnlockWorkItemInputXml.append("<Option>WMUnlockWorkItem</Option>");
		wmUnlockWorkItemInputXml.append("<EngineName>" + cabinetName
				+ "</EngineName>");
		wmUnlockWorkItemInputXml.append("<SessionId>" + sessionId
				+ "</SessionId>");
		wmUnlockWorkItemInputXml.append("<ProcessInstanceId>" + processId
				+ "</ProcessInstanceId>");
		wmUnlockWorkItemInputXml.append("<WorkItemId>" + workItemId
				+ "</WorkItemId>");
		wmUnlockWorkItemInputXml.append("</WMUnlockWorkItem_Input>");

		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
				"inputXML for releasing lock on workitem : "
				+ wmUnlockWorkItemInputXml.toString());
		String wmUnlockWorkItemOutputXml = makeCall(jtsAddress,
				Short.valueOf(jtsPort), appServerType, wmUnlockWorkItemInputXml.toString());
		if (wmUnlockWorkItemOutputXml == null
				|| wmUnlockWorkItemOutputXml.trim().isEmpty()) {
			throw new Exception(
					"no outputXML generated while releasing lock on process : "
					+ processId);
		}
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
				"outputXML for releasing lock on workitem : "
				+ wmUnlockWorkItemOutputXml);
		XMLParser xmlParser = new XMLParser();
		xmlParser.setInputXML(wmUnlockWorkItemOutputXml);
		if (xmlParser.getValueOf("MainCode").trim().equals("0")) {
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "released lock for process : "
					+ processId);
			return;
		} else {
			throw new Exception(xmlParser.getValueOf("Error"));
		}
	}

	private String getWorkItemIdForProcessInstance(String cabinetName,
			String sessionId, String processId, String processDefId,
			String jtsAddress, String jtsPort, String appServerType) throws Exception {
		StringBuilder wmSearchWorkItemsInputXml = new StringBuilder();
		wmSearchWorkItemsInputXml.append("<?xml version=\"1.0\"?>");
		wmSearchWorkItemsInputXml.append("<WMSearchWorkItems_Input>");
		wmSearchWorkItemsInputXml.append("<Option>WMSearchWorkItems</Option>");
		wmSearchWorkItemsInputXml.append("<EngineName>" + cabinetName
				+ "</EngineName>");
		wmSearchWorkItemsInputXml.append("<SessionId>" + sessionId
				+ "</SessionId>");
		wmSearchWorkItemsInputXml.append("<Filter>");
		wmSearchWorkItemsInputXml.append("<ProcessInstanceName>" + processId
				+ "</ProcessInstanceName>");
		wmSearchWorkItemsInputXml.append("<ProcessDefinitionID>" + processDefId
				+ "</ProcessDefinitionID>");
		wmSearchWorkItemsInputXml.append("</Filter>");
		wmSearchWorkItemsInputXml.append("</WMSearchWorkItems_Input>");

		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "inputXML for getting workItemId : "
				+ wmSearchWorkItemsInputXml.toString());
		//		String wmSearchWorkItemsOutputXml = makeCall(jtsAddress,
		//				Short.valueOf(jtsPort), appServerType, wmSearchWorkItemsInputXml.toString());
		//gaurav-s changes With Wrapper 
		String wmSearchWorkItemsOutputXml=makeCall(jtsAddress, Short.parseShort(jtsPort), wmSearchWorkItemsInputXml.toString());
		if (wmSearchWorkItemsOutputXml == null
				|| wmSearchWorkItemsOutputXml.trim().isEmpty()) {
			throw new Exception(
					"no outputXML generated while getting workItemId on process : "
					+ processId);
		}
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
				"outputXML for getting workItemId : "
				+ wmSearchWorkItemsOutputXml);
		XMLParser xmlParser = new XMLParser();
		xmlParser.setInputXML(wmSearchWorkItemsOutputXml);
		if (xmlParser.getValueOf("MainCode").trim().equals("0")) {
			String workItemId = xmlParser.getValueOf("WorkItemId");
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "workItemId for process("
					+ processId + ") : " + workItemId);
			return workItemId;
		} else {
			throw new Exception(xmlParser.getValueOf("Error"));
		}
	}

	// Gaurav Rohira

	public String getStatusRemarksForCorrespondingWorkItem(String cabinetName,
			String sessionId, String processId, String jtsAddress,
			String jtsPort, String appServerType, String workItem) throws Exception {
		String remarkList = null;
		String sQuery = "SELECT REJECTION_CATEGORY,REJECTION_COMMENTS FROM ng_ao_rejection_details WHERE wi_name='"
			+ workItem + "' and Rejection_Locked<>'C'  ";
		int noOfColumns = 2;
		StringBuilder wmGetStatusRemarksInputXml = new StringBuilder();
		wmGetStatusRemarksInputXml.append("<?xml version=\"1.0\"?>");
		wmGetStatusRemarksInputXml.append("<WMTestSelect_Input>");
		wmGetStatusRemarksInputXml.append("<Option>WFSelectTest_new</Option>");
		wmGetStatusRemarksInputXml.append("<sQry>" + sQuery + "</sQry>");
		wmGetStatusRemarksInputXml.append("<EngineName>" + cabinetName
				+ "</EngineName>");
		wmGetStatusRemarksInputXml.append("<NoOfCols>" + noOfColumns
				+ "</NoOfCols>");
		wmGetStatusRemarksInputXml.append("<SessionId>" + sessionId
				+ "</SessionId>");
		wmGetStatusRemarksInputXml.append("</WMTestSelect_Input>");

		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
				"inputXML for getting status remarks : "
				+ wmGetStatusRemarksInputXml.toString());
		String wmGetStatusRemarksOutputXml = makeCall(jtsAddress,
				Short.valueOf(jtsPort), appServerType, wmGetStatusRemarksInputXml.toString());
		if (wmGetStatusRemarksOutputXml == null
				|| wmGetStatusRemarksOutputXml.trim().isEmpty()) {
			throw new Exception(
					"no outputXML generated while getting status remarks on process : "
					+ processId);
		}
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
				"outputXML for getting status remarks : "
				+ wmGetStatusRemarksOutputXml);
		XMLParser xmlParser = new XMLParser();
		xmlParser.setInputXML(wmGetStatusRemarksOutputXml);
		if (xmlParser.getValueOf("MainCode").trim().equals("0")) {
			// String statusRemarks = xmlParser.getValueOf("Result").toString();
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "fetching result tags:");
			int size = xmlParser.getNoOfFields("Result");
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "fetching result tags:" + size);
			for (int i = 0; i < size; i++) {
				String statusRemarks = xmlParser.getNextValueOf("Result");
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "statusRemarks for process("
						+ processId + ") : " + statusRemarks);
				if (remarkList != null) {
					remarkList = remarkList + "$" + statusRemarks;
				} else {
					remarkList = statusRemarks;
				}
			}
			// LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "statusRemarks for process(" +
			// processId + ") : " + statusRemarks);
			return remarkList;
		} else {
			throw new Exception(xmlParser.getValueOf("Error"));
		}
	}

	private String updateWorkItemAttributes(String cabinetName, String sessionId,
			String processId, String workItemId, String jtsAddress,
			String jtsPort, String appServerType, Map<String, String> attributesMap, Map<String, Map<String, String>> complexAttributes,Map<String,Map<String,Map<String,String>>> gridtableMap,Map<String, String>  attributesMapFromApk,String processDefId,String activityId,String activityType, Map<String,Map<String,Map<String,Map<String, String>>>> NestedgridtableMap,HashMap<String,String> docIndexMap) throws Exception {
		if (attributesMap == null) {
			throw new Exception("empty data value map");
		}
		StringBuilder wfSetAttributesInputXml = new StringBuilder();
		wfSetAttributesInputXml.append("<?xml version=\"1.0\"?>");
		wfSetAttributesInputXml.append("<WFSetAttributes_Input>");
		wfSetAttributesInputXml
		.append("<Option>WMAssignWorkItemAttributes</Option>");
		wfSetAttributesInputXml.append("<EngineName>" + cabinetName
				+ "</EngineName>");
		wfSetAttributesInputXml.append("<SessionId>" + sessionId
				+ "</SessionId>");
		wfSetAttributesInputXml.append("<ProcessInstanceId>" + processId
				+ "</ProcessInstanceId>");
		wfSetAttributesInputXml.append("<WorkItemId>" + workItemId
				+ "</WorkItemId>");
		wfSetAttributesInputXml.append("<UserDefVarFlag>Y</UserDefVarFlag>");
		//gaurav-s changed added config attributes
		wfSetAttributesInputXml.append("<ActivityId>"+activityId+"</ActivityId>");
		wfSetAttributesInputXml.append("<ProcessDefId>"+processDefId+"</ProcessDefId>");
		wfSetAttributesInputXml.append("<LastModifiedTime></LastModifiedTime>");
		wfSetAttributesInputXml.append("<ActivityType>"+activityType+"</ActivityType>");
		if(complexAttributes.get("q_DecisionHistory")!=null){
			HashMap<String, String> columnapkmap=(HashMap<String, String>)(complexAttributes.get("q_DecisionHistory"));
			if(!columnapkmap.get("Decision").toString().trim().toLowerCase().contains("select")&&!"".equalsIgnoreCase(columnapkmap.get("Decision").toString().trim())){
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "decision status =>"	+ columnapkmap.get("Decision").toString().trim());
				wfSetAttributesInputXml.append("<complete>I</complete>");
			}
			//wfSetAttributesInputXml.append("<ActivityId>1</ActivityId><ProcessDefId>6</ProcessDefId><LastModifiedTime></LastModifiedTime><ActivityType>1</ActivityType>");
		}
		String xmlforIncomingdocuments="";
		wfSetAttributesInputXml.append("<Attributes>");
		for (Entry<String, String> entry : attributesMap.entrySet()) {
			wfSetAttributesInputXml.append("<");
			wfSetAttributesInputXml.append(entry.getKey());
			wfSetAttributesInputXml.append(">");
			wfSetAttributesInputXml.append(entry.getValue());
			wfSetAttributesInputXml.append("</");
			wfSetAttributesInputXml.append(entry.getKey());
			wfSetAttributesInputXml.append(">");
		}
		//		for (Entry<String, String> entry : attributesMap.entrySet()) {
		////			wfSetAttributesInputXml.append(entry.getKey() + (char) 21
		////					+ entry.getValue() + (char) 25);
		//			wfSetAttributesInputXml.append("<" + entry.getKey() + ">");
		//			wfSetAttributesInputXml.append(entry.getValue());
		//			wfSetAttributesInputXml.append("</" + entry.getKey() + ">");
		//		}
		
		for (Entry<String, Map<String, String>> entry : complexAttributes.entrySet()) {
			if(!entry.getKey().trim().equals("q_cif_detail")){
				wfSetAttributesInputXml.append("<" + entry.getKey() + ">");
			}
			

			for (Entry<String, String> subentries : entry.getValue().entrySet()) {
				wfSetAttributesInputXml.append("<" + subentries.getKey() + ">");
				if(null==subentries.getValue()||subentries.getValue().toString().equalsIgnoreCase("null")||subentries.getValue().toString().equalsIgnoreCase("--Select--")||subentries.getValue().toString().equalsIgnoreCase("undefined")){
					wfSetAttributesInputXml.append("");

				}
				else{
				wfSetAttributesInputXml.append( subentries.getValue());}
				wfSetAttributesInputXml.append("</" + subentries.getKey() + ">");
			}
			//gaurav-s changes
			if(gridtableMap.get(entry.getKey())!=null){
				Map<String, Map<String, String>> grid_to_apk_column_map=gridtableMap.get(entry.getKey());
				for (Entry<String, Map<String, String>> gridentry : grid_to_apk_column_map.entrySet()) {
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Getting  GRID: "
							+ gridentry+" ");
					JSONObject attributesjsonFromApk=new JSONObject(attributesMapFromApk);
					JSONArray grid_table_array=(JSONArray)new JSONParser().parse(attributesjsonFromApk.get(gridentry.getKey()).toString());
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Found Grid Values : "
							+ grid_table_array+" In Output Model");
					//gaurav-s added 04042018 added this code to get xml for incoming doc  --  put jsonarray in any one of cmplx like we do for grid
					if(null!=attributesjsonFromApk.get("documentList")){	
					JSONArray doc_list_array=(JSONArray)new JSONParser().parse(attributesjsonFromApk.get("documentList").toString());
					xmlforIncomingdocuments=generatexmlforIncomingdocuments(doc_list_array,docIndexMap);
					}
						
					
					
					for(int record_counter=0;record_counter<grid_table_array.size();record_counter++){
						
						
						JSONObject record=(JSONObject)grid_table_array.get(record_counter);
						wfSetAttributesInputXml.append("<" + gridentry.getKey() + ">");
						for (Entry<String, String> subentries : gridentry.getValue().entrySet()) 
						{
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Preparing XML for   GRID: "
									+ gridentry+" ");
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Getting : "
									+ subentries.getValue()+" In Output Model");
							wfSetAttributesInputXml.append("<" + subentries.getKey() + ">");
							if(null==record.get(subentries.getValue())||record.get(subentries.getValue()).toString().equalsIgnoreCase("null")||record.get(subentries.getValue()).toString().equalsIgnoreCase("--Select--")||record.get(subentries.getValue()).toString().equalsIgnoreCase("undefined")){
								wfSetAttributesInputXml.append("");

							}
							else{
								if(subentries.getValue().equalsIgnoreCase("wiName")){
									wfSetAttributesInputXml.append(processId);	
								}
								else{
									wfSetAttributesInputXml.append(record.get(subentries.getValue()) );
								}
							}
							wfSetAttributesInputXml.append("</" + subentries.getKey() + ">");
						}
						//for nested map gaurav-s 15012018
						
//						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "parentChildNestedGridMap : " + parentChildNestedGridMap);
//						if(parentChildNestedGridMap.containsKey(gridentry.getKey().trim())){
//							
//							HashMap<String, HashMap<String,String>> currententry=parentChildNestedGridMap.get(gridentry.getKey().trim());
//							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "currententry : " + currententry);
//							Set<String> nestedgrid  = currententry.keySet();
//							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "nestedgrid : " + nestedgrid);
//							for(String nestedkey : nestedgrid){
//								wfSetAttributesInputXml.append("<"+ nestedkey+">");
//								//JSONParser parser=new JSONParser();
//								HashMap<String,String> xmlIdmap=currententry.get(nestedkey);
//						//	HashMap<String, String> xmlId=new HashMap<String, String>();
//							Set<String> childset=xmlIdmap.keySet();
//							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "childset : " + childset);
//							JSONArray currentNestedGridArray=null;
//							if((null!=(JSONArray)record.get(nestedkey))){
//								LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Getting"+ nestedkey+" in   : " + record);
//								 currentNestedGridArray =(JSONArray)(record.get(nestedkey));	
//							}else{
//								LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Getting"+ nestedkey+" in   : " + record);
//								LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, nestedkey+" Not Found in op modal : ");
//								break;
//							}
//							
//							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "currentNestedGridArray : " + currentNestedGridArray);
//							for(int x = 0;x<currentNestedGridArray.size();x++){
//								JSONObject nestedgridentry=(JSONObject)currentNestedGridArray.get(x);
//								
//								for(String nestedchild : childset){
//									LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "nestedchild tag  : " + nestedchild);
//									wfSetAttributesInputXml.append("<"+ nestedchild+">");
//									//cmplx_GR_AuthorizedSignDetails
//									LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "value in op modal nestedchild tag  : " +nestedgridentry.get(nestedchild));
//								nestedgridentry.get(nestedchild);
//									wfSetAttributesInputXml.append("</"+ nestedchild+">");	
//									
//								}
//							}
//							
//							wfSetAttributesInputXml.append("</"+ nestedkey+">");
//							}
//							
//							
//						}
						//for nested map gaurav-s 15012018
						
						wfSetAttributesInputXml.append("</" + gridentry.getKey() + ">");
					}
				}
			}
			//gaurav-s changes END
			if(!entry.getKey().trim().equals("q_cif_detail")){
				wfSetAttributesInputXml.append("</" + entry.getKey() + ">");
			}
		}
		//gauarv-s changes 04042018 for generating xml 
		wfSetAttributesInputXml.append(xmlforIncomingdocuments);
		wfSetAttributesInputXml.append("</Attributes>");
		wfSetAttributesInputXml.append("</WFSetAttributes_Input>");

		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"inputXML for updating workItem attributes: "
				+ wfSetAttributesInputXml.toString());
		//		String wfSetAttributesOutputXml = makeCall(jtsAddress,
		//				Short.valueOf(jtsPort), appServerType, wfSetAttributesInputXml.toString()
		//				.replaceAll("&", "&amp;"));

		String wfSetAttributesOutputXml=makeCall(jtsAddress, Short.parseShort(jtsPort), wfSetAttributesInputXml.toString()
				.replaceAll("&", "&amp;"));
		if (wfSetAttributesOutputXml == null
				|| wfSetAttributesOutputXml.trim().isEmpty()) {
			throw new Exception(
					"no outputXML generated while getting workItemId on process : "
					+ processId);
		}
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"outputXML for updating workItem attributes: " + wfSetAttributesOutputXml);
		XMLParser xmlParser = new XMLParser();
		xmlParser.setInputXML(wfSetAttributesOutputXml);
		if (xmlParser.getValueOf("MainCode").trim().equals("0")) {
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
					"attributes updated for process : " + processId);
			return "success|||" + xmlToJson(wfSetAttributesOutputXml);
		} else {
			throw new Exception(xmlParser.getValueOf("Subject"));
		}
	}

	// Custom table call: Gaurav Berry
	private void updateCustomTableAttributes(String cabinetName,
			String sessionId, String tableName, String jtsAddress,
			String jtsPort, String appServerType, Map<String, String> attributesMap) throws Exception {
		if (attributesMap == null) {
			throw new Exception("empty data value map");
		}

		StringBuilder wfSetAttributesInputXml = new StringBuilder();
		wfSetAttributesInputXml.append("<?xml version=\"1.0\"?>");
		wfSetAttributesInputXml.append("<WFInsert_Input>");
		wfSetAttributesInputXml.append("<Option>WFInsert_new</Option>");
		wfSetAttributesInputXml.append("<EngineName>" + cabinetName
				+ "</EngineName>");
		wfSetAttributesInputXml.append("<SessionId>" + sessionId
				+ "</SessionId>");
		wfSetAttributesInputXml.append("<TableName>" + tableName
				+ "</TableName>");
		wfSetAttributesInputXml.append("<ColName>");
		StringBuilder colName = new StringBuilder();
		for (Entry<String, String> entry : attributesMap.entrySet()) {

			colName.append(entry.getKey());
			colName.append(",");
		}
		wfSetAttributesInputXml.append(colName.substring(0,
				colName.length() - 1));
		wfSetAttributesInputXml.append("</ColName>");
		wfSetAttributesInputXml.append("<Values>");

		StringBuilder values = new StringBuilder();
		String sValue = ""; // vipin
		/*
		 * for (Entry<String, String> entry : attributesMap.entrySet()) {
		 * values.append(entry.getValue()); values.append(","); }
		 */

		for (Entry<String, String> entry : attributesMap.entrySet()) {

			sValue = entry.getValue();
			try {
				Integer.parseInt(sValue);
				values.append(sValue);
			} catch (Exception e) {
				values.append("'" + sValue + "'");
			}

			values.append(",");

		}

		wfSetAttributesInputXml
		.append(values.substring(0, values.length() - 1));

		wfSetAttributesInputXml.append("</Values>");
		wfSetAttributesInputXml.append("</WFInsert_Input>");

		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
				"inputXML for updating workItem attributes: "
				+ wfSetAttributesInputXml.toString());
		String wfSetAttributesOutputXml = makeCall(jtsAddress,
				Short.valueOf(jtsPort), appServerType, wfSetAttributesInputXml.toString());
		if (wfSetAttributesOutputXml == null
				|| wfSetAttributesOutputXml.trim().isEmpty()) {
			throw new Exception(
					"no outputXML generated while getting workItemId on tableName : "
					+ tableName);
		}
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
				"outputXML for updating workItem attributes: "
				+ wfSetAttributesOutputXml);
		XMLParser xmlParser = new XMLParser();
		xmlParser.setInputXML(wfSetAttributesOutputXml);
		if (xmlParser.getValueOf("MainCode").trim().equals("0")) {
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
					"attributes updated for tableName : " + tableName);
		} else {
			throw new Exception(xmlParser.getValueOf("Subject"));
		}
	}

	// Custom table call: Jitendra Jeet Singh
	private void updateCustomTableAttributesToUpdateforReupload(
			String cabinetName, String sessionId, String tableName,
			String jtsAddress, String jtsPort, String appServerType,
			Map<String, String> attributesMap, String processId)
	throws Exception {
		if (attributesMap == null) {
			throw new Exception("empty data value map");
		}

		StringBuilder wfSetAttributesInputXml = new StringBuilder();
		wfSetAttributesInputXml.append("<?xml version=\"1.0\"?>");
		wfSetAttributesInputXml.append("<WFUpdate_Input>");
		wfSetAttributesInputXml.append("<Option>WFUpdate_new</Option>");
		wfSetAttributesInputXml.append("<EngineName>" + cabinetName
				+ "</EngineName>");
		wfSetAttributesInputXml.append("<SessionId>" + sessionId
				+ "</SessionId>");
		wfSetAttributesInputXml.append("<TableName>" + tableName
				+ "</TableName>");
		wfSetAttributesInputXml.append("<ColName>");
		StringBuilder colName = new StringBuilder();
		for (Entry<String, String> entry : attributesMap.entrySet()) {

			colName.append(entry.getKey());
			colName.append(",");
		}
		wfSetAttributesInputXml.append(colName.substring(0,
				colName.length() - 1));
		wfSetAttributesInputXml.append("</ColName>");
		wfSetAttributesInputXml.append("<Values>");

		StringBuilder values = new StringBuilder();
		String sValue = ""; // vipin
		/*
		 * for (Entry<String, String> entry : attributesMap.entrySet()) {
		 * values.append(entry.getValue()); values.append(","); }
		 */

		for (Entry<String, String> entry : attributesMap.entrySet()) {

			sValue = entry.getValue();
			try {
				Integer.parseInt(sValue);
				values.append(sValue);
			} catch (Exception e) {
				values.append("'" + sValue + "'");
			}

			values.append(",");

		}

		wfSetAttributesInputXml
		.append(values.substring(0, values.length() - 1));

		wfSetAttributesInputXml.append("</Values>");
		wfSetAttributesInputXml.append("<WhereClause>");
		wfSetAttributesInputXml.append("WI_NAME = '" + processId + "'");
		wfSetAttributesInputXml.append("</WhereClause>");

		wfSetAttributesInputXml.append("</WFUpdate_Input>");

		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
				"inputXML for updating workItem attributes for Reupload: "
				+ wfSetAttributesInputXml.toString());
		String wfSetAttributesOutputXml = makeCall(jtsAddress,
				Short.valueOf(jtsPort), appServerType, wfSetAttributesInputXml.toString());
		if (wfSetAttributesOutputXml == null
				|| wfSetAttributesOutputXml.trim().isEmpty()) {
			throw new Exception(
					"no outputXML generated while getting workItemId on tableName : "
					+ tableName);
		}
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
				"outputXML for updating workItem attributes on Reupload: "
				+ wfSetAttributesOutputXml);
		XMLParser xmlParser = new XMLParser();
		xmlParser.setInputXML(wfSetAttributesOutputXml);
		if (xmlParser.getValueOf("MainCode").trim().equals("0")) {
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
					"attributes updated for tableName : " + tableName);
		} else {
			throw new Exception(xmlParser.getValueOf("Subject"));
		}
	}

	private void updateWorkItemDocument(String cabinetName, String sessionId,
			String processId, String processFolderIndex, String jtsAddress,
			String jtsPort, String appServerType, SMSDocument document) throws Exception {
		DMSInputXml xmlBuilder = new DMSInputXml();
		XMLParser xmlParser = new XMLParser();
		String versionFlag = "Y"; // document-version-ing is required
		String documentType = document.docType;
		String inputXmlToMapDocument = xmlBuilder.getAddDocumentXml(
				cabinetName, sessionId, "", processFolderIndex,
				String.valueOf(document.noOfPages), "I", documentType, "", "",
				versionFlag, "I", String.valueOf(document.size), "",
				document.extenstion, document.imageIndex + "#"
				+ document.volumeId + "#", "", "",
				"document re-uploaded", "", "", "", "", "", "");

		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "inputXML to update document : "
				+ inputXmlToMapDocument);
		//		String outputXmlToMapDocument = makeCall(jtsAddress,
		//				Short.valueOf(jtsPort), appServerType, inputXmlToMapDocument);
		String outputXmlToMapDocument=makeCall(jtsAddress, Short.parseShort(jtsPort), inputXmlToMapDocument);
		if (outputXmlToMapDocument == null
				|| outputXmlToMapDocument.trim().isEmpty()) {
			throw new Exception(
					"no outputXML generated while re-mapping document : "
					+ documentType);
		}
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "inputXML to update document : "
				+ outputXmlToMapDocument);
		xmlParser.setInputXML(outputXmlToMapDocument);
		String status = xmlParser.getValueOf("Status");
		if (status == null || status.trim().isEmpty()) {
			throw new Exception(xmlParser.getValueOf("Error"));
		}
		String documentIndex = xmlParser.getValueOf("DocumentIndex");
		if (documentIndex == null || documentIndex.trim().isEmpty()) {
			throw new Exception("couldn't get document index for : "
					+ documentType);
		}
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "document(" + documentType
				+ ") re-uploaded with " + documentIndex + " for process : "
				+ processId);
	}

	private String getFolderIndexForProcessFolder(String cabinetName,
			String sessionId, String folderName, String jtsAddress,
			String jtsPort, String appServerType) throws Exception {
		// generate XML
		StringBuffer inputXMLString = null;
		try {
			DMSInputXml dmsInputXml = new DMSInputXml();
			inputXMLString = new StringBuffer();
			inputXMLString.append("<?xml version=\"1.0\"?>");
			inputXMLString.append("<NGOSearchFolder_Input>");
			inputXMLString.append("<Option>NGOSearchFolder</Option>");
			inputXMLString.append("<CabinetName>" + cabinetName
					+ "</CabinetName>");
			inputXMLString.append("<UserDBId>" + sessionId + "</UserDBId>");
			inputXMLString.append("<LookInFolder>0</LookInFolder>");
			inputXMLString.append("<IncludeSubFolder>Y</IncludeSubFolder>");
			inputXMLString.append("<Name>" + folderName + "</Name>");
			inputXMLString.append("<StartFrom>1</StartFrom>");
			inputXMLString.append("<NoOfRecordsToFetch>1</NoOfRecordsToFetch>");
			inputXMLString.append("</NGOSearchFolder_Input>");
		} catch (Exception e) {
			e.printStackTrace();
			LogMe.logMe(LogMe.LOG_LEVEL_ERROR, "error=> " + e);
			throw new Exception("error in input xml generation");
		}

		// make call
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "inputXML to search folder => "
				+ inputXMLString);
		//		String outputXMLString = makeCall(jtsAddress, Short.valueOf(jtsPort), appServerType,
		//				inputXMLString.toString());
		String outputXMLString=makeCall(jtsAddress, Short.parseShort(jtsPort), inputXMLString.toString());
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "outputXML to search folder => "
				+ outputXMLString);
		DMSXmlResponse dmsXmlResponse = new DMSXmlResponse(outputXMLString);
		String status = dmsXmlResponse.getVal("Status");
		if (status != null && status.equals("0")) {
			return dmsXmlResponse.getVal("FolderIndex");
		} else {
			throw new Exception(dmsXmlResponse.getVal("Error"));
		}
	}

	private List<SMSDocument> processDocumentsAsPerRequirements(
			Map<String, Set<String>> documentMap) throws Exception {
		String targetFile = null;
		String fileExtension = null;
		List<SMSDocument> smsDocuments = new ArrayList<RakOFSubmission.SMSDocument>();
		for (Entry<String, Set<String>> entry : documentMap.entrySet()) {
			String documentType = entry.getKey();
			Set<String> documents = entry.getValue();
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Document path values size>>>"
					+ documents.size());
			int numberOfTiffDocuments = 0;
			for (String sourceFile : documents) {
				LogMe.logMe(LogMe.LOG_LEVEL_INFO, "SourceFile>>" + sourceFile);
				targetFile = sourceFile;
				int status[] = new int[1]; // input for niplj
				try {
					fileExtension = sourceFile.substring(sourceFile
							.lastIndexOf('.') + 1);
				} catch (Exception e) {
					LogMe.logMe(LogMe.LOG_LEVEL_ERROR, e);
					throw new Exception("can't get file extenstion for : "
							+ sourceFile);
				}
				if (fileExtension.equalsIgnoreCase("jpeg")
						|| fileExtension.equalsIgnoreCase("jpg")) {
					targetFile = targetFile.replace(fileExtension, "tiff");
					try {
						NIPLJ.convertJPEGIntoTif6(sourceFile, targetFile,
								status);
						numberOfTiffDocuments++; // after successful conversion,
						// increment no. of
						// tiff-documents
					} catch (NGIMException e) {
						LogMe.logMe(LogMe.LOG_LEVEL_ERROR, e);
						throw new Exception(
								"error when converting jpeg into tiff format : "
								+ sourceFile);
					} catch (Throwable t) {
						LogMe.logMe(LogMe.LOG_LEVEL_ERROR, t);
						throw new Exception(
								"error when converting jpeg into tiff format : "
								+ sourceFile);
					}
				} else if (fileExtension.equalsIgnoreCase("tiff")
						|| fileExtension.equalsIgnoreCase("tif")) {
					numberOfTiffDocuments++;
				} else {
					throw new Exception("no handling for document : "
							+ sourceFile);
				}
			}
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "numberOfTiffDocuments>>>"
					+ numberOfTiffDocuments);
			if (numberOfTiffDocuments > 1) {
				if (numberOfTiffDocuments == documents.size()) {
					String parentDir = targetFile.substring(0,
							targetFile.lastIndexOf(File.separator) + 1);
					targetFile = parentDir + documentType + "_CON" + ".tiff";
					try {
						String[] images = documents
						.toArray(new String[documents.size()]);
						LogMe.logMe(
								LogMe.LOG_LEVEL_DEBUG,
								"images to concatenate>>>"
								+ Arrays.toString(images));
						Tif6.concatenateTif(images, targetFile);
					} catch (Throwable e) {
						LogMe.logMe(LogMe.LOG_LEVEL_ERROR, e);
						throw new Exception("concat error");
					}
				} else {
					throw new Exception("not all images are in tiff format");
				}
			}
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "targetFile>>>" + targetFile);
			SMSDocument smsDocument = new SMSDocument();
			smsDocument.path = targetFile;
			smsDocument.docType = documentType;
			smsDocument.noOfPages = documents.size();
			smsDocument.size = new File(targetFile).length();
			smsDocument.comment = "document re-uploaded";
			try {
				smsDocument.extenstion = targetFile.substring(targetFile
						.lastIndexOf('.') + 1);
			} catch (Exception e) {
				LogMe.logMe(LogMe.LOG_LEVEL_ERROR, e);
				throw new Exception("can't get file extenstion for : "
						+ targetFile);
			}
			smsDocuments.add(smsDocument);
		}
		return smsDocuments;
	}

	private List<SMSDocument> processDocumentsAsPerRequirementsOnFileSystem(
			Map<String, Set<String>> documentMap) throws Exception {
		String targetFile = null;
		String fileExtension = null;
		List<SMSDocument> smsDocuments = new ArrayList<RakOFSubmission.SMSDocument>();
		for (Entry<String, Set<String>> entry : documentMap.entrySet()) {
			String documentType = entry.getKey();
			Set<String> documents = entry.getValue();
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Document path values size>>>"
					+ documents.size());
			int numberOfTiffDocuments = 0;
			for (String sourceFile : documents) {
				targetFile = sourceFile;
				int status[] = new int[1]; // input for niplj
				try {
					fileExtension = sourceFile.substring(sourceFile
							.lastIndexOf('.') + 1);
				} catch (Exception e) {
					LogMe.logMe(LogMe.LOG_LEVEL_ERROR, e);
					throw new Exception("can't get file extenstion for : "
							+ sourceFile);
				}
				if (fileExtension.equalsIgnoreCase("jpeg")
						|| fileExtension.equalsIgnoreCase("jpg")) {
					targetFile = targetFile.replace(fileExtension, "tiff");
					try {
						NIPLJ.convertJPEGIntoTif6(sourceFile, targetFile,
								status);

						numberOfTiffDocuments++; // after successful conversion,
						// increment no. of
						// tiff-documents
					} catch (NGIMException e) {
						LogMe.logMe(LogMe.LOG_LEVEL_ERROR, e);
						throw new Exception(
								"error when converting jpeg into tiff format : "
								+ sourceFile);
					} catch (Throwable t) {
						LogMe.logMe(LogMe.LOG_LEVEL_ERROR, t);
						throw new Exception(
								"error when converting jpeg into tiff format : "
								+ sourceFile);
					}
				} else if (fileExtension.equalsIgnoreCase("tiff")
						|| fileExtension.equalsIgnoreCase("tif")) {
					numberOfTiffDocuments++;
				} else {
					throw new Exception("no handling for document : "
							+ sourceFile);
				}
			}
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "numberOfTiffDocuments>>>"
					+ numberOfTiffDocuments);
			if (numberOfTiffDocuments > 1) {
				if (numberOfTiffDocuments == documents.size()) {
					String parentDir = targetFile.substring(0,
							targetFile.lastIndexOf(File.separator) + 1);
					targetFile = parentDir + documentType + "_CON" + ".tiff";
					try {
						String[] images = documents
						.toArray(new String[documents.size()]);
						LogMe.logMe(
								LogMe.LOG_LEVEL_DEBUG,
								"images to concatenate>>>"
								+ Arrays.toString(images));
						Tif6.concatenateTif(images, targetFile);
					} catch (Throwable e) {
						LogMe.logMe(LogMe.LOG_LEVEL_ERROR, e);
						throw new Exception("concat error");
					}
				} else {
					throw new Exception("not all images are in tiff format");
				}
			}
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "targetFile>>>" + targetFile);
			SMSDocument smsDocument = new SMSDocument();
			smsDocument.path = targetFile;
			smsDocument.docType = documentType;
			smsDocument.noOfPages = documents.size();
			smsDocument.size = new File(targetFile).length();
			try {
				smsDocument.extenstion = targetFile.substring(targetFile
						.lastIndexOf('.') + 1);
			} catch (Exception e) {
				LogMe.logMe(LogMe.LOG_LEVEL_ERROR, e);
				throw new Exception("can't get file extenstion for : "
						+ targetFile);
			}
			smsDocuments.add(smsDocument);
		}
		return smsDocuments;
	}

	@Override
	public List<Resource> getInstances(BusinessUseCase buc) throws Exception {
		List<Resource> toReturn = new ArrayList<Resource>();
		User user = (User) ThreadUtils.getThreadVariable("user");
		if (user == null || user.getAuthenticationToken() == null) {
			throw new Exception("cannot fetch without user");
		}
		String sessionId = user.getAuthenticationToken();
		String userName = user.getUserName();
		// read server configurations
		String jtsAddress = null;
		String jtsPort = null;
		String appServerType = null;
		String cabinetName = null;
		String processDefId = null;
		Node ofConfigNode = null;
		try {
			String xPath = null;
			// Gaurav Rohira

			// xPath =
			// "configuratioNode/childConfigurationItems[configNodeName='OFMapping' and attributes[attributeName='bucName' and attributeValue='"
			// + bucModel.getBucName() + "']]";
			// ofConfigNode = (ConfigurationNode)
			// configuration.searchObjectGraph(
			// xPath).get(0);

			xPath = "./*/OFMapping[@bucName='" + buc.getBucName() + "']";

			ofConfigNode = configuration.searchObjectGraph(xPath,
					configuration.getConfigurationXmlText()).item(0);

			xPath = "./Server";

			Node serverInfoNode = configuration.searchObjectGraph(xPath,
					(Element) ofConfigNode).item(0);

			xPath = "./Key[@name = 'serverAddress']";
			jtsAddress = configuration
			.searchObjectGraph(xPath, (Element) serverInfoNode).item(0)
			.getTextContent().trim();

			xPath = "./Key[@name = 'serverPort']";
			jtsPort = configuration
			.searchObjectGraph(xPath, (Element) serverInfoNode).item(0)
			.getTextContent().trim();

			xPath = "./Key[@name = 'serverType']";
			appServerType = "";

			xPath = "./Key[@name = 'cabinetName']";
			cabinetName = configuration
			.searchObjectGraph(xPath, (Element) serverInfoNode).item(0)
			.getTextContent().trim();

			// server-address
			// xPath = "childConfigurationItems[configNodeName='Key'"
			// +
			// " and attributes[attributeName='name' and attributeValue='serverAddress']]"
			// + "/textWithinNode";
			// jtsAddress = (String) serverInfoNode.searchObjectGraph(xPath)
			// .get(0);
			// // server-port
			// xPath = "childConfigurationItems[configNodeName='Key'"
			// +
			// " and attributes[attributeName='name' and attributeValue='serverPort']]"
			// + "/textWithinNode";
			// jtsPort = (String)
			// serverInfoNode.searchObjectGraph(xPath).get(0);
			// // cabinet-name
			// xPath = "childConfigurationItems[configNodeName='Key'"
			// +
			// " and attributes[attributeName='name' and attributeValue='cabinetName']]"
			// + "/textWithinNode";
			// cabinetName = (String)
			// serverInfoNode.searchObjectGraph(xPath).get(
			// 0);
			xPath = "./Process";
			Node processInfoNode = configuration.searchObjectGraph(xPath,
					(Element) ofConfigNode).item(0);

			xPath = "./Key[@name = 'processDefId']";
			processDefId = configuration
			.searchObjectGraph(xPath, (Element) processInfoNode)
			.item(0).getTextContent().trim();


			// xPath = "childConfigurationItems[configNodeName='Process']";
			// ConfigurationNode processInfoNode = (ConfigurationNode)
			// ofConfigNode
			// .searchObjectGraph(xPath).get(0);
			// // process-def-id
			// xPath = "childConfigurationItems[configNodeName='Key'"
			// +
			// " and attributes[attributeName='name' and attributeValue='processDefId']]"
			// + "/textWithinNode";
			// processDefId = (String) processInfoNode.searchObjectGraph(xPath)
			// .get(0);
			if (jtsAddress == null || jtsPort == null || cabinetName == null
					|| processDefId == null) {
				throw new Exception(
				"null entries while reading server configuartion");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogMe.logMe(LogMe.LOG_LEVEL_ERROR, e);
			throw new Exception("error in reading server configuration");
		}
		if (buc.getForms() == null) {
			throw new Exception("no model to populate");
		}
		// read external table mapping (externalTableFieldName, apkFieldname)
		Map<String, String> apkFieldToExternalTableFieldMap = new HashMap<String, String>();
		try {
			String xPath = null;
			xPath = "./ExternalTableMapping";
			Node extTableMappingNode = configuration.searchObjectGraph(xPath,
					(Element) ofConfigNode).item(0);

			xPath = "./Field";
			NodeList fieldNodes = configuration.searchObjectGraph(xPath,
					(Element) extTableMappingNode);
			Node fieldNode = null;

			for (int j = 0; j < fieldNodes.getLength(); j++) {
				fieldNode = fieldNodes.item(j);
				String externalTableFieldName = ((Element) fieldNode)
				.getAttribute("columnname");
				String apkFieldName = ((Element) fieldNode)
				.getAttribute("apkfieldname");
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "apkFields=>" + apkFieldName
						+ ":" + externalTableFieldName);
				apkFieldToExternalTableFieldMap.put(apkFieldName,
						externalTableFieldName);
			}

			// ConfigurationNode extTableMappingNode = (ConfigurationNode)
			// ofConfigNode
			// .searchObjectGraph(xPath).get(0);
			// get fields
			// xPath = "childConfigurationItems[configNodeName='Field']";
			// @SuppressWarnings("unchecked")
			// List<ConfigurationNode> fieldNodes = (List<ConfigurationNode>)
			// extTableMappingNode
			// .searchObjectGraph(xPath);
			// String xPathForExternalTableFieldName =
			// "attributes[attributeName='columnname']/attributeValue";
			// String xPathForApkFieldName =
			// "attributes[attributeName='apkfieldname']/attributeValue";
			// for (ConfigurationNode fieldNode : fieldNodes) {
			// String externalTableFieldName = (String) fieldNode
			// .searchObjectGraph(xPathForExternalTableFieldName).get(
			// 0);
			// String apkFieldName = (String) fieldNode.searchObjectGraph(
			// xPathForApkFieldName).get(0);
			// LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "apkFields=>" + apkFieldName
			// + ":" + externalTableFieldName);
			// apkFieldToExternalTableFieldMap.put(apkFieldName,
			// externalTableFieldName);
			// }
		} catch (Exception e) {
			e.printStackTrace();
			LogMe.logMe(LogMe.LOG_LEVEL_ERROR, e);
			throw new Exception("error in reading server configuration");
		}

		// fetch no. of buc-instances here
		// File dir = new File("D:\\Random\\Sample_Configuration");
		// File[] bucs = dir.listFiles(new FilenameFilter() {
		// @Override
		// public boolean accept(File dir, String name) {
		// return name.startsWith("outputmodel");
		// }
		// });

		// jitendra - workitems on Zapin_Hold are assigend to users queue before
		// fetching all workItem assigend.
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "userName ::" + userName);
		// updateWFInstrumentTable(userName, cabinetName, sessionId, jtsAddress,
		// jtsPort);

		Set<String> workItemIds = getAssignedInstance(cabinetName, sessionId,
				userName, processDefId, jtsAddress, jtsPort, appServerType);
		if (workItemIds.size() == 0) {
			toReturn = new ArrayList<Resource>();
		} else {
			for (String workItemId : workItemIds) {

				/*
				 * For Duplicate Fetch case from CRM Vipin gupta 19/october/2015
				 * Code starts
				 */

				BusinessUseCase businessUseCase = new BusinessUseCase();
				businessUseCase.setProcessId(workItemId);

				List<BusinessUseCase> searchedBusinessUseCases = (List<BusinessUseCase>) HibernateUtil
				.search(businessUseCase);
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Size of bucs::"
						+ searchedBusinessUseCases.size());

				/*
				 * For Duplicate Fetch case from CRM Vipin gupta 19/october/2015
				 * Code Ends
				 */
				if (searchedBusinessUseCases.size() > 1) { // for Duplicate CRM
					// case
					// vipin
					throw new Exception("Duplicate businessusecases found");
				}

				// prepare data-value map here

				if (searchedBusinessUseCases.size() == 0) { // // for Duplicate
					// CRM
					// case vipin
					Map<String, String> extfieldValueMap = new HashMap<String, String>();
					Map<String, String> apkFieldvalueMap = new HashMap<String, String>();
					// Properties properties = new Properties();
					// properties.load(new FileInputStream(buc));
					extfieldValueMap = getAssignedInstanceAttributes(
							workItemId, cabinetName, sessionId, processDefId,
							jtsAddress, jtsPort, appServerType);
					for (Entry<String, String> entry : apkFieldToExternalTableFieldMap
							.entrySet()) {
						apkFieldvalueMap.put(entry.getKey(),
								extfieldValueMap.get(entry.getValue()));
						LogMe.logMe(
								LogMe.LOG_LEVEL_DEBUG,
								"ApkFieldValueMap Key::"
								+ entry.getKey()
								+ " Value::"
								+ extfieldValueMap.get(entry.getValue())); // sonia
					}

					Long creationTimeStamp = System.currentTimeMillis();
					BusinessUseCase assignedBUC = new BusinessUseCase();
					assignedBUC.setBucName(buc.getBucName());
					assignedBUC.setProcessId(workItemId);
					String appId = null;
					// String bucCode = null;
					Set<Device> devices = user.getDevices();
					if (devices.size() > 0) {
						appId = devices.iterator().next().getAppId();
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
								"Assigned buc appid>>" + appId);
					}

					// if
					// (bucModel.getBucName().equalsIgnoreCase("SavingsAccountOpening"))
					// {
					// bucCode = "SA";
					// }
					//
					// else if
					// (bucModel.getBucName().equalsIgnoreCase("CurrentAccountOpening"))
					// {
					// bucCode = "CA";
					// }

					assignedBUC.setApplicantId(appId + "_" + userName + "_"
							+ String.valueOf(System.currentTimeMillis()));
					// reference-app requirement
					Date creationTime = new Date(creationTimeStamp);
					SimpleDateFormat sdf = new SimpleDateFormat(
					"E MMM d yyyy HH:mm:ss");
					sdf.setTimeZone(TimeZone.getTimeZone("IST"));
					String creationTimeInString = sdf.format(creationTime);
					assignedBUC.setCreationTime(creationTimeInString
							+ " GMT+0530 (India Standard Time)");
					assignedBUC.setLastAccessTime(creationTimeInString
							+ " GMT+0530 (India Standard Time)");
					assignedBUC.setSolutionVersion(buc.getSolutionVersion());
					// reference-app requirement - ends
					assignedBUC.setForms(new HashSet<Form>());
					for (Form formModel : buc.getForms()) {
						Form form = new Form();
						form.setFormName(formModel.getFormName());
						String outputModel = new String(
								formModel.getOutputModel());
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "OutputModel::"
								+ outputModel);// sonia
						if (!outputModel.trim().isEmpty()) {
							JSONParser jsonParser = new JSONParser();
							JSONObject jsonObject = (JSONObject) jsonParser
							.parse(outputModel);
							populateToJsonObject(jsonObject, apkFieldvalueMap);
							form.setOutputModel(jsonObject.toJSONString());
							form.saveOrUpdate();
							assignedBUC.getForms().add(form);
						} else {
							throw new Exception("empty output-model");
						}
					}
					toReturn.add(assignedBUC);
				}
			}
		}
		return toReturn;
	}

	private void populateToJsonObject(JSONObject jObject,
			Map<String, String> valueMap) {
		for (Object jEntry : jObject.entrySet()) {
			Entry<String, Object> entry = (Entry<String, Object>) jEntry;
			Object obj = entry.getValue();
			if (entry.getKey().equalsIgnoreCase("attachments")) {
			}
			if (obj instanceof JSONObject) {
				populateToJsonObject((JSONObject) obj, valueMap);
			} else if (obj instanceof JSONArray) {
				populateToJsonArray((JSONArray) obj, valueMap);
			} else {
				jObject.put(entry.getKey(), valueMap.get(entry.getKey()));
			}
		}
	}

	private void populateToJsonArray(JSONArray jArray,
			Map<String, String> valueMap) {
		Iterator<?> iter = jArray.listIterator();
		while (iter.hasNext()) {
			Object obj = iter.next();
			if (obj instanceof JSONObject) {
				populateToJsonObject((JSONObject) obj, valueMap);
			} else if (obj instanceof JSONArray) {
				System.out.println("<===JSONArray>" + obj);
			} else {
				System.out.println("<===>" + obj.getClass()
						+ String.valueOf(obj));
			}
		}
	}

	public Set<String> getAssignedInstance(String engineName, String sessionId,
			String username, String processDefId, String jtsAddress,
			String jtsPort, String appServerType) throws Exception {
		Set<String> workItemsList = new HashSet<String>();
		StringBuilder wmSearchWorkItemsInputXml = new StringBuilder();
		wmSearchWorkItemsInputXml.append("<?xml version=\"1.0\"?>");
		wmSearchWorkItemsInputXml.append("<WMSearchWorkItems_Input>");
		wmSearchWorkItemsInputXml.append("<Option>WMSearchWorkItems</Option>");
		wmSearchWorkItemsInputXml.append("<EngineName>" + engineName
				+ "</EngineName>");
		wmSearchWorkItemsInputXml.append("<SessionID>" + sessionId
				+ "</SessionID>");
		wmSearchWorkItemsInputXml.append("<Filter><AssignedToUser>" + username
				+ "</AssignedToUser>");
		wmSearchWorkItemsInputXml.append("<ProcessDefinitionID>" + processDefId
				+ "</ProcessDefinitionID></Filter>");
		wmSearchWorkItemsInputXml.append("<DataFlag>Y</DataFlag>");
		wmSearchWorkItemsInputXml.append("</WMSearchWorkItems_Input> ");

		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "inputXML for getting workItemId : "
				+ wmSearchWorkItemsInputXml.toString());
		String wmSearchWorkItemsOutputXml = makeCall(jtsAddress,
				Short.valueOf(jtsPort), appServerType, wmSearchWorkItemsInputXml.toString());
		if (wmSearchWorkItemsOutputXml == null
				|| wmSearchWorkItemsOutputXml.trim().isEmpty()) {
			throw new Exception(
					"no outputXML generated while fetching assigned cases for : "
					+ username);
		}
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
				"outputXML for fetching assigned cases for " + username + ": "
				+ wmSearchWorkItemsOutputXml);
		XMLParser xmlParser = new XMLParser();
		xmlParser.setInputXML(wmSearchWorkItemsOutputXml);
		if (xmlParser.getValueOf("MainCode").trim().equals("0")) {
			int size = Integer.parseInt(xmlParser.getValueOf("RetrievedCount"));
			for (int i = 0; i < size; i++) {
				String workItemId = xmlParser.getNextValueOf("RegistrationNo");
				workItemsList.add(workItemId);
			}
			// LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "workItemId for process(" +
			// processId + ") : " + workItemId);
			return workItemsList;
		} else if (xmlParser.getValueOf("MainCode").trim().equals("18")) {
			return new HashSet<String>();
		} else {
			throw new Exception(xmlParser.getValueOf("Error"));
		}
	}

	public Map<String, String> getAssignedInstanceAttributes(String workItemId,
			String engineName, String sessionId, String processDefinitionId,
			String jtsAddress, String jtsPort, String appServerType) throws NumberFormatException,
			Exception {
		Map<String, String> attributesMap = new HashMap<String, String>();
		// for (Iterator<String> itrWorkItem =
		// workItems.iterator();itrWorkItem.hasNext();){
		// String workItemId = itrWorkItem.next();
		StringBuilder wmFetchAttributesInputXml = new StringBuilder();
		wmFetchAttributesInputXml.append("<?xml version=\"1.0\"?>");
		wmFetchAttributesInputXml.append("<WMFetchWorkItemAttributes_Input>");
		wmFetchAttributesInputXml
		.append("<Option>WMFetchWorkItemAttributes</Option>");
		wmFetchAttributesInputXml.append("<EngineName>" + engineName
				+ "</EngineName>");
		wmFetchAttributesInputXml.append("<SessionId>" + sessionId
				+ "</SessionId>");
		wmFetchAttributesInputXml.append("<ProcessInstanceId>" + workItemId
				+ "</ProcessInstanceId>");
		wmFetchAttributesInputXml.append("<WorkItemId>1</WorkItemId>");
		wmFetchAttributesInputXml.append("<ActivityId>1</ActivityId>");
		wmFetchAttributesInputXml.append("<ProcessDefinitionId>"
				+ processDefinitionId + "</ProcessDefinitionId>");
		wmFetchAttributesInputXml.append("<BatchInfo></BatchInfo>");
		wmFetchAttributesInputXml.append("</WMFetchWorkItemAttributes_Input>");
		String wmFetchAttributesOutputXml = makeCall(jtsAddress,
				Short.valueOf(jtsPort), appServerType, wmFetchAttributesInputXml.toString());
		if (wmFetchAttributesOutputXml == null
				|| wmFetchAttributesOutputXml.isEmpty()) {
			throw new Exception(
					"no outputXML generated while fetching attributes for : "
					+ workItemId);
		}
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
				"outputXML for fetching attributes for " + workItemId + ": "
				+ wmFetchAttributesOutputXml);
		XMLParser xmlParser = new XMLParser();
		xmlParser.setInputXML(wmFetchAttributesOutputXml);
		if (xmlParser.getValueOf("MainCode").trim().equals("0")) {
			int size = xmlParser.getNoOfFields("Attribute");
			for (int i = 0; i < size; i++) {
				String attrXml = xmlParser.getNextValueOf("Attribute");
				XMLParser attrXmlParser = new XMLParser();
				attrXmlParser.setInputXML(attrXml);
				String attrName = attrXmlParser.getValueOf("Name");
				String attrVal = attrXmlParser.getValueOf("Value");
				if (attrVal != null) {
					attributesMap.put(attrName, attrVal);
				}
			}
		} else {
			throw new Exception(xmlParser.getValueOf("Error"));
		}
		// }?
		return attributesMap;
	}

	/*
	 * By Jitendra 02122015 Workitem on workstep = Zapin_Hold will be assigned
	 * to logged in user by selecting the workItem initiated by user logged in.
	 * updateCallForWFInstrument() method updates the records so the workitem is
	 * available in user's Myqueue.
	 */
	// public void updateWFInstrumentTable(String userName, String cabinetName,
	// String sessionId, String jtsAddress, String jtsPort)
	// throws Exception {
	//
	// String sQuery =
	// "SELECT R.WI_NAME,U.USERINDEX,R.RM_CODE  FROM NG_AO_CORPCON_ROUTE_TABLE R,WFINSTRUMENTTABLE I,PDBUSER U WHERE "
	// + " UPPER(U.USERNAME) = UPPER(R.RM_CODE) AND "
	// + " I.VAR_REC_1=R.ITEMINDEX AND "
	// + " I.ACTIVITYNAME= 'Zapin_Hold' AND "
	// + " UPPER(R.RM_CODE)=UPPER('"
	// + userName + "')";
	//
	// int noOfColumns = 3;
	// StringBuilder wmUserIndexInputXml = new StringBuilder();
	// wmUserIndexInputXml.append("<?xml version=\"1.0\"?>");
	// wmUserIndexInputXml.append("<WMTestSelect_Input>");
	// wmUserIndexInputXml.append("<Option>WFSelectTest_new</Option>");
	// wmUserIndexInputXml.append("<sQry>" + sQuery + "</sQry>");
	// wmUserIndexInputXml.append("<EngineName>" + cabinetName
	// + "</EngineName>");
	// wmUserIndexInputXml.append("<NoOfCols>" + noOfColumns + "</NoOfCols>");
	// wmUserIndexInputXml.append("<SessionId>" + sessionId + "</SessionId>");
	// wmUserIndexInputXml.append("</WMTestSelect_Input>");
	//
	// LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "inputXML for getting RM Code :"
	// + userName + ">>" + wmUserIndexInputXml.toString());
	// String wfGetUserIndexOutputXml = makeCall(jtsAddress,
	// Short.valueOf(jtsPort), wmUserIndexInputXml.toString());
	// if (wfGetUserIndexOutputXml == null
	// || wfGetUserIndexOutputXml.trim().isEmpty()) {
	// throw new Exception(
	// "no outputXML generated while getting USERINDEX on userName : "
	// + userName);
	// }
	// LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
	// "outputXML for updating workItem attributes: "
	// + wfGetUserIndexOutputXml);
	// XMLParser xmlParser = new XMLParser();
	// xmlParser.setInputXML(wfGetUserIndexOutputXml);
	// String UserIndexRMCode = "";
	// if (xmlParser.getValueOf("MainCode").trim().equals("0")) {
	// LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "fetching result tags:");
	// int size = xmlParser.getNoOfFields("Result");
	// LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "fetching result tags:" + size);
	// for (int i = 0; i < size; i++) {
	// UserIndexRMCode = xmlParser.getNextValueOf("Result");
	// if (!UserIndexRMCode.isEmpty()) {
	// String WI_NAME = UserIndexRMCode.split("\\|")[1];
	// String USERINDEX = UserIndexRMCode.split("\\|")[2];
	// String RM_CODE = UserIndexRMCode.split("\\|")[3];
	//
	// LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "WI_NAME:: " + WI_NAME
	// + " and RMCode:: " + RM_CODE + " USERINDEX ::"
	// + USERINDEX);
	// updateCallForWFInstrument(WI_NAME, USERINDEX, RM_CODE,
	// cabinetName, sessionId, jtsAddress, jtsPort);
	// } else {
	// LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "userName:: " + userName
	// + " and WMTestSelect_Input Result:: "
	// + UserIndexRMCode);
	// }
	// }
	// } else {
	// throw new Exception(xmlParser.getValueOf("Error"));
	// }
	//
	// }

	// public void updateCallForWFInstrument(String workItemId, String
	// USERINDEX,
	// String RM_CODE, String cabinetName, String sessionId,
	// String jtsAddress, String jtsPort) throws Exception {
	// if ((!workItemId.isEmpty()) && (!USERINDEX.isEmpty())
	// && (!RM_CODE.isEmpty())) {
	// String sTable = "WFINSTRUMENTTABLE";
	// String sCols =
	// "ASSIGNMENTTYPE,Q_USERID,ASSIGNEDUSER,WORKITEMSTATE,STATENAME,QUEUENAME,QUEUETYPE,NOTIFYSTATUS";
	// String sValues = "'X','" + USERINDEX + "','" + RM_CODE
	// + "',1,'NOTSTARTED','" + RM_CODE
	// + "'||'''s MyQueue','U','Y'";
	// String sWhereClause = "PROCESSINSTANCEID ='" + workItemId + "'";
	// String update_InXML = "<?xml version=\"1.0\"?>\n"
	// + "<WFUpdate_Input>\n" + "<Option>WFUpdate_new</Option>\n"
	// + "<EngineName>" + cabinetName + "</EngineName>\n"
	// + "<SessionId>" + sessionId + "</SessionId>\n"
	// + "<TableName>" + sTable + "</TableName>\n" + "<ColName>"
	// + sCols + "</ColName>\n" + "<Values>" + sValues
	// + "</Values>\n" + "<WhereClause>" + sWhereClause
	// + "</WhereClause>\n" + "</WFUpdate_Input>";
	// LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
	// "inputXML to update WFInstrument : " + update_InXML);
	// String outputXmlToUpdateWFInstrument = makeCall(jtsAddress,
	// Short.valueOf(jtsPort), update_InXML);
	// if (outputXmlToUpdateWFInstrument == null
	// || outputXmlToUpdateWFInstrument.trim().isEmpty()) {
	// throw new Exception("no outputXML generated For UserIndex : "
	// + USERINDEX);
	// }
	// LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
	// "OutputXML to update document : "
	// + outputXmlToUpdateWFInstrument);
	// XMLParser xmlParser = new XMLParser();
	// xmlParser.setInputXML(outputXmlToUpdateWFInstrument);
	// String status = xmlParser.getValueOf("MainCode");
	// if (status == null || status.trim().isEmpty()) {
	// throw new Exception(xmlParser.getValueOf("Error"));
	// }
	//
	// LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
	// "WFInstrumentTable Update with WI_Name of processId :: "
	// + workItemId + "\nstatus ::" + status);
	// } else {
	// throw new Exception("No Result for WI_NAME : " + workItemId);
	// }
	//
	// }

	// public String select_SOURCE_SYSTEM_NAMEForCRM(String cabinetName,String
	// sessionId,String customTableName,String jtsAddress,String jtsPort,String
	// processId) throws Exception
	// {
	// String CRMCaseResult = "";
	// String sQuery =
	// "SELECT WS_NAME,SOURCE_SYSTEM_NAME  FROM NG_AO_CORPCON_ROUTE_TABLE  WHERE WI_NAME = '"+processId+"'";
	//
	// int noOfColumns = 2;
	// StringBuilder wmUserIndexInputXml = new StringBuilder();
	// wmUserIndexInputXml.append("<?xml version=\"1.0\"?>");
	// wmUserIndexInputXml.append("<WMTestSelect_Input>");
	// wmUserIndexInputXml.append("<Option>WFSelectTest_new</Option>");
	// wmUserIndexInputXml.append("<sQry>" + sQuery + "</sQry>");
	// wmUserIndexInputXml.append("<EngineName>" + cabinetName
	// + "</EngineName>");
	// wmUserIndexInputXml.append("<NoOfCols>" + noOfColumns + "</NoOfCols>");
	// wmUserIndexInputXml.append("<SessionId>" + sessionId + "</SessionId>");
	// wmUserIndexInputXml.append("</WMTestSelect_Input>");
	//
	// LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
	// "inputXML for getting WS_NAME,SOURCE_SYSTEM_NAME for processid :"
	// + processId + ">>" + wmUserIndexInputXml.toString());
	// String wfGetUserIndexOutputXml =
	// makeCall(jtsAddress,Short.valueOf(jtsPort),
	// wmUserIndexInputXml.toString());
	// if (wfGetUserIndexOutputXml == null
	// || wfGetUserIndexOutputXml.trim().isEmpty()) {
	// throw new
	// Exception("no outputXML generated for getting WS_NAME,SOURCE_SYSTEM_NAME for processid :"
	// + processId);
	// }
	// LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
	// "outputXML for for getting WS_NAME,SOURCE_SYSTEM_NAME for processid :"
	// + wfGetUserIndexOutputXml);
	// XMLParser xmlParser = new XMLParser();
	// xmlParser.setInputXML(wfGetUserIndexOutputXml);
	//
	// if (xmlParser.getValueOf("MainCode").trim().equals("0")) {
	// LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "fetching result tags:");
	// int size = xmlParser.getNoOfFields("Result");
	// LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "fetching result tags:" + size);
	// for (int i = 0; i < size; i++) {
	// CRMCaseResult = xmlParser.getNextValueOf("Result");
	// if (!CRMCaseResult.isEmpty()) {
	// String WS_NAME = CRMCaseResult.split("\\|")[1];
	// String SOURCE_SYSTEM_NAME = CRMCaseResult.split("\\|")[2];
	// LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "WS_NAME:: " +
	// WS_NAME+" SOURCE_SYSTEM_NAME:: " +SOURCE_SYSTEM_NAME);
	//
	// if(SOURCE_SYSTEM_NAME.isEmpty() || !SOURCE_SYSTEM_NAME.equals("CRM"))
	// {
	// CRMCaseResult="UPDATE";
	// }
	// else if(SOURCE_SYSTEM_NAME.equals("CRM") && WS_NAME.equals("Zapin_Hold"))
	// {
	// CRMCaseResult="INSERT";
	// }
	// else if(SOURCE_SYSTEM_NAME.equals("CRM") &&
	// WS_NAME.equals("Zapin_Rejected"))
	// {
	// CRMCaseResult="UPDATE";
	// }
	// } else {
	// LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "No Result for processId:: " +
	// processId);
	// }
	// }
	//
	// } else {
	// throw new Exception(xmlParser.getValueOf("Error"));
	// }
	// return CRMCaseResult;
	// }
	

public  static String generatexmlforIncomingdocuments(JSONArray array,HashMap<String, String>docIndexMap){
	String toreturn = "";
	String currdocindex = "";
	String expdate="";
	String deffer="";
	String docstatus="";
	//documentList":[{"hashId":"128321522835595133","insertId":"0","docName":"AECBconsentform","mandatory":"Y"}
//	for(int i  = 0; i<array.size() ; i++){
//		JSONObject tempobj=(JSONObject)array.get(i);
//		if(null!=docIndexMap.get(tempobj.get("docName"))){
//			currdocindex=	"<DocIndex>"+docIndexMap.get(tempobj.get("docName").toString().trim())+"</DocIndex>";	
//		}
//		else
//			currdocindex = "<DocIndex></DocIndex>";
//		if(null!=tempobj.get("exp")){
//			expdate=tempobj.get("exp").toString().trim();
//		}
//		if(null!=tempobj.get("deffer")){
//			deffer=tempobj.get("deffer").toString();
//		}
//		if(null!=tempobj.get("docstatus")){
//			docstatus=	tempobj.get("docstatus").toString();
//		}
//		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, " \n tempobj Xml  : "
//				+ tempobj+" ");
//		toreturn =toreturn+"<q_IncomingDoc Unbounded=\"Y\">"+
//			"<HashId>"+tempobj.get("hashId")+"</HashId>"+
//			"<InsertionOrderId>"+tempobj.get("insertId")+"</InsertionOrderId>"+
//			"<DocName>"+tempobj.get("docName")+"</DocName>"+
//			"<ExpiryDate>"+tempobj.get("exp")+"</ExpiryDate>"+
//			"<Mandatory>"+tempobj.get("mandatory")+"</Mandatory>"+
//			"<DocStatus>"+docstatus+"</DocStatus>"+
//			"<Remarks>"+tempobj.get("remark")+"</Remarks>"+
//			currdocindex+
//			"<DeferredUntil>"+deffer+"</DeferredUntil>"+
//			"</q_IncomingDoc>";
//		
//	}
	
	for(int i  = 0; i<array.size() ; i++){
		JSONObject tempobj=(JSONObject)array.get(i);
		if(null!=docIndexMap.get(tempobj.get("docName"))){
			currdocindex=	"<DocIndex>"+docIndexMap.get(tempobj.get("docName").toString().trim())+"</DocIndex>";	
		}
		else
			currdocindex = "<DocIndex></DocIndex>";
		if(null!=tempobj.get("exp")){
			expdate=tempobj.get("exp").toString().trim();
		}
		if(null!=tempobj.get("deffer")){
			deffer=tempobj.get("deffer").toString();
		}
		if(null!=tempobj.get("docstatus")){
			docstatus=	tempobj.get("docstatus").toString();
		}
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, " \n tempobj Xml  : "
				+ tempobj+" ");
		if(!docstatus.equalsIgnoreCase("Select") && !docstatus.equalsIgnoreCase("")){
			toreturn =toreturn+"<IncomingDocGrid Unbounded=\"Y\">"+
			"<HashId>"+tempobj.get("hashId")+"</HashId>"+
			"<InsertionOrderId>"+tempobj.get("insertId")+"</InsertionOrderId>"+
			"<Doctype>"+tempobj.get("docType")+"</Doctype>"+
			"<DocName>"+tempobj.get("docName")+"</DocName>"+
			"<ExpiryDate>"+tempobj.get("exp")+"</ExpiryDate>"+
			"<Mandatory>"+tempobj.get("mandatory")+"</Mandatory>"+
			"<status>"+docstatus+"</status>"+
			"<Remarks>"+tempobj.get("remark")+"</Remarks>"+
			currdocindex+
			"<DeferredUntilDate>"+deffer+"</DeferredUntilDate>"+
			"<IncomingDoc_Winame>"+tempobj.get("wiName")+"</IncomingDoc_Winame>"+
			"<DocIndex />"+
			"</IncomingDocGrid>";
		}
		
	}
	
	toreturn = "<q_incomingDocNew><MandatoryDocument></MandatoryDocument><NonMandatoryDoc></NonMandatoryDoc>" +toreturn+ "</q_incomingDocNew>";

	LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, " \n q_IncomingDoc Xml  : "
			+ toreturn+" ");
	return toreturn;
}

public static String ExecuteQuery_APdelete(String tableName, String sWhere,
		String cabinetName, String sessionId) {
	String sInputXML = "<?xml version=\"1.0\"?>" + "<APDelete_Input><Option>APDelete</Option>" + "<TableName>" + tableName + "</TableName>" + "<WhereClause>" + sWhere + "</WhereClause>" + "<EngineName>" + cabinetName + "</EngineName>" + "<SessionId>" + sessionId + "</SessionId>" + "</APDelete_Input>";
	LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "APdelete INputXml is:" + sInputXML);
	return sInputXML;
}

public static String ExecuteQuery_APInsert(String tableName,String columnName,String strValues,String cabinetName,String sessionId)
{
    WFInputXml wfInputXml = new WFInputXml();

    wfInputXml.appendStartCallName("APInsert", "Input");
    wfInputXml.appendTagAndValue("TableName",tableName);
    wfInputXml.appendTagAndValue("ColName",columnName);
    wfInputXml.appendTagAndValue("Values",strValues);
    wfInputXml.appendTagAndValue("EngineName",cabinetName);
    wfInputXml.appendTagAndValue("SessionId",sessionId);
    wfInputXml.appendEndCallName("APInsert","Input");
    return wfInputXml.toString();
}

// added by abhishek on 1st Nov 2018



public String ExecuteDbqueryReturn(String query) throws Exception {
	// TODO Auto-generated method stub
	LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "query : "+query);
	LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Inside ExecuteDbquery Block");
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Connection connection = null;
	String toreturn = null;
	try {
		connection = nemfDbconnection();
		if (connection != null) {
			PreparedStatement statement = connection.prepareStatement(
					query, Statement.RETURN_GENERATED_KEYS);
			int countrec=statement.executeUpdate();
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "countrec : "+countrec);
			ResultSet generatedKeys = statement.getGeneratedKeys();
			generatedKeys.next();
			toreturn = String.valueOf(generatedKeys.getLong(1));
		}
	} catch (Exception e) {
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
				"ExecuteDbqueryReturn Method EXCEPTION BLOCK : " + e.getMessage());
		e.printStackTrace();
	} finally {
		try {
			if (pstmt != null) {
				pstmt.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (connection != null) {
				connection.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	return toreturn;
}
public Connection nemfDbconnection() {
	String xPath = "";
	String dburl = "";
	String dbdriver = "";
	String source = "";
	String datasource = "";
	Node DbMapping = null;
	InitialContext localInitialContext = null;
	DataSource localDataSource = null;
	PreparedStatement pstmt = null;
	Connection connection = null;
	LogMe
	.logMe(LogMe.LOG_LEVEL_DEBUG,
			"Inside Get OF DB Connection method");
	try {
		xPath = "./*/DbMapping";
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, xPath + "\n");
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, configuration.searchObjectGraph(
				xPath, configuration.getConfigurationXmlText()) + "\n");
		DbMapping = configuration.searchObjectGraph(xPath,
				configuration.getConfigurationXmlText()).item(0);
		xPath = "./Key[@name = 'SOURCE']";
		source = configuration
				.searchObjectGraph(xPath, (Element) DbMapping).item(0)
				.getTextContent().trim();
		if (source.equalsIgnoreCase("DATASOURCE")) {
			xPath = "./Key[@name = 'DATASOURCE']";
			datasource = configuration.searchObjectGraph(xPath,
					(Element) DbMapping).item(0).getTextContent().trim();
		//	LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, xPath + "\n" + datasource);
		} else if (source.equalsIgnoreCase("DBCONNECTION")) {
			xPath = "./Key[@name = 'DBURL']";
			dburl = configuration.searchObjectGraph(xPath,
					(Element) DbMapping).item(0).getTextContent().trim();
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "xPath=" + xPath);
		//	LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "dburl=" + dburl);
			xPath = "./Key[@name = 'DBDRIVER']";
			dbdriver = configuration.searchObjectGraph(xPath,
					(Element) DbMapping).item(0).getTextContent().trim();
		//	LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, xPath + "\n" + dburl + "\n" + dbdriver);
		}
		if (source.equalsIgnoreCase("DBCONNECTION")) {
			Class.forName(dbdriver).newInstance();
			connection = DriverManager.getConnection(dburl);
		} else if (source.equalsIgnoreCase("DATASOURCE")) {
			localInitialContext = new InitialContext();
			localDataSource = (DataSource) localInitialContext
					.lookup(datasource);
			connection = localDataSource.getConnection();
		}

		if (connection != null) {
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
					"Connection Estb. with NEMF Database");
		} else {
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
					"Error while Opening Connection with OF Db");
		}
	} catch (Exception e) {
		e.printStackTrace();
		return null;
	}
	return connection;
}

public  List < List < String >> ExecuteSelectQueryGetList(String query)
throws Exception {

LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
	"Inside ExecuteSelectQueryGetList Method");
List < List < String >> result = new ArrayList < List < String >> ();
PreparedStatement pstmt = null;
ResultSet rs = null;
Connection conn = null;
conn = nemfDbconnection();
try {
if (conn != null) {
	String Sql = query;
	LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "QUERY TO HIT : " + query);
	pstmt = conn.prepareStatement(query);
	rs = pstmt.executeQuery();
	int numcols = rs.getMetaData().getColumnCount();

	while (rs.next()) {
		List < String > row = new ArrayList < String > (numcols);
		int i = 1;
		while (i <= numcols) { // don't skip the last column, use <=
			row.add(rs.getString(i++));
		}
		result.add(row); // add it to the result
	}
	LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
			"ExecuteSelectQueryGetList Method Result: " + result);
} else {
	LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
			"ERROR WHILE OPENING CONNECTION ");
}
} catch (Exception e) {
LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,
		"ExecuteSelectQueryGetList Method EXCEPTION BLOCK : " + e.getMessage());

//e.printStackTrace();
} finally {
try {
	if (pstmt != null) {
		pstmt.close();
	}
	if (rs != null) {
		rs.close();
	}
	if (conn != null) {
		conn.close();
	}
} catch (Exception e) {
	LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"ExecuteSelectQueryGetList Method EXCEPTION BLOCK : " + e);
	//e.printStackTrace();
}
}
return result;
}
}

