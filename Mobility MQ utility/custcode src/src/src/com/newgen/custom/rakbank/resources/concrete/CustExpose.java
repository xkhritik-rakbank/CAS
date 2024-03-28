package com.newgen.custom.rakbank.resources.concrete;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

import com.newgen.dmsapi.DMSCallBroker;
import com.newgen.mcap.core.external.logging.concrete.LogMe;
import com.newgen.omni.wf.util.app.NGEjbClient;
import com.newgen.omni.wf.util.excp.NGException;

public class CustExpose {
	public String CustExpose_datasave(String result,String wi_name, String prod,String subprod,String cifId,String is_mobility, String sessionId,
			String row_count,String CardNumber,String cust_type,String CabinetName,String WrapperIP,String WrapperPort,String AppServerType ){
		
		String cabinetName = CabinetName;
		String wrapperIP = WrapperIP;
		String wrapperPort = WrapperPort;
		String appServerType = AppServerType;
		
		
	/*	WriteLog("inside custexpose_output.jsp for "+"result: "+result);

		try {
			String prop_file_loc=System.getProperty("user.dir") + File.separatorChar+"CustomConfig"+File.separatorChar+"ServerConfig.properties";
		    File file = new File(prop_file_loc);
	        FileInputStream fileInput = new FileInputStream(file);
	        Properties properties = new Properties();
	        properties.load(fileInput);
	        fileInput.close();
			cabinetName = "cas";
			wrapperIP = "10.15.11.94";
			wrapperPort = "3333";
			appServerType = "WebSphere";
	    } 
		catch(Exception e){
			return("false"+"@#"+row_count);
		}*/
		
		String outputXMLMsg = "";
		String flagOp="";
		try
		{		
			if(result.indexOf("<MQ_RESPONSE_XML>")>-1)
			{
				outputXMLMsg=result.substring(result.indexOf("<MQ_RESPONSE_XML>")+17,result.indexOf("</MQ_RESPONSE_XML>"));
				cifId =cifId.trim();
				flagOp = getOutputXMLValues(outputXMLMsg,appServerType,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,prod,subprod,cifId,CardNumber,cust_type);
			}
		}
		catch(Exception e)
		{     
			System.out.println("Exception occured in custexpose_output: "+ e.getMessage());
			e.printStackTrace();
		}
		return(flagOp+"@#"+row_count);
	} 
	
	public static String getOutputXMLValues(String parseXml,String appServerType,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name, String prod, String subprod, String cifId,String CardNumber,String cust_type)
	{
		String outputXMLHead = "";
        String outputXMLMsg = "";
        String returnCode = "";
        String returnType="";
		String result_str = "";
		String MsgFormat="";
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Inside getOutputXMLValues for:: "+parseXml);
		try
		{
			if(parseXml.indexOf("<EE_EAI_HEADER>")>-1)
			{
				outputXMLHead=parseXml.substring(parseXml.indexOf("<EE_EAI_HEADER>"),parseXml.indexOf("</EE_EAI_HEADER>")+16);
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "RLOSCommon valueSetCustomer"+ outputXMLHead);
			}
			if(outputXMLHead.indexOf("<ReturnCode>")>-1)
			{
				returnCode= outputXMLHead.substring(outputXMLHead.indexOf("<ReturnCode>")+12,outputXMLHead.indexOf("</ReturnCode>"));
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "$$returnCode "+returnCode);
			}
			if(parseXml.indexOf("<RequestType>")>-1)
			{
				returnType= parseXml.substring(parseXml.indexOf("<RequestType>")+13,parseXml.indexOf("</RequestType>"));
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "$$returnType "+returnType);
				//Added By Prabhakar
				if(!"0000".equalsIgnoreCase(returnCode))
				{	
					String errorQuery="SELECT isnull((SELECT alert FROM ng_MASTER_INTEGRATION_ERROR_CODE with (nolock) WHERE  error_code='"+returnCode+"'),(SELECT alert FROM ng_MASTER_INTEGRATION_ERROR_CODE with (nolock) WHERE error_code='DEFAULT')) As Alert";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "@@@@@@@@@@@@@@  "+errorQuery);
					String strInputXml = ExecuteQuery_APSelect(errorQuery,cabinetName,sessionId);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "@@@@@@@@@@@@@@  "+strInputXml);
					String strOutputXml  = makeCall(wrapperIP, Short.valueOf(wrapperPort), strInputXml);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "@@@@@@@@@@@@@@  "+strOutputXml);
					result_str=strOutputXml.substring(strOutputXml.indexOf("<Alert>")+"</Alert>".length()-1,strOutputXml.indexOf("</Alert>"));
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "@@@@@@@@@@@@@@  "+result_str); 
					return result_str;
				}
				else if(returnType.equalsIgnoreCase("InternalExposure"))
				{
					result_str=parseInternalExposure(returnType,parseXml,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,appServerType,prod,subprod,cifId,cust_type);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "cifId jsp: getOutputXMLValuesresult:InternalExpFlag "+result_str);
				}
				else if(returnType.equalsIgnoreCase("ExternalExposure"))
				{
					result_str=parseExternalExposure(returnType,parseXml,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,appServerType,prod,subprod,cifId,cust_type);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "cifId jsp: getOutputXMLValuesresult:ExternalExpFlag "+result_str);
				}
				else if(returnType.equalsIgnoreCase("CollectionsSummary"))
				{
					result_str=parseCollectionSummary(returnType,parseXml,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,appServerType,prod,subprod,cifId,cust_type);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "cifId jsp: getOutputXMLValuesresult:Collections "+result_str);
				}
			}
			
			//added
			else if(parseXml.indexOf("<MsgFormat>")>-1)
			{
				returnType= parseXml.substring(parseXml.indexOf("<MsgFormat>")+11,parseXml.indexOf("</MsgFormat>"));
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "$$MsgFormat "+returnType);
				//Added By Prabhakar
				if(!"0000".equalsIgnoreCase(returnCode))
				{	
				
					String errorQuery="SELECT isnull((SELECT alert FROM ng_MASTER_INTEGRATION_ERROR_CODE with(nolock) WHERE  error_code='"+returnCode+"'),(SELECT alert FROM ng_MASTER_INTEGRATION_ERROR_CODE with(nolock) WHERE error_code='DEFAULT')) As Alert";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "@@@@@@@@@@@@@@  "+errorQuery);
					String strInputXml = ExecuteQuery_APSelect(errorQuery,cabinetName,sessionId);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "@@@@@@@@@@@@@@  "+strInputXml);
					String strOutputXml = makeCall(wrapperIP, Short.valueOf(wrapperPort), strInputXml);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "@@@@@@@@@@@@@@  "+strOutputXml);
					result_str=strOutputXml.substring(strOutputXml.indexOf("<Alert>")+"</Alert>".length()-1,strOutputXml.indexOf("</Alert>"));
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "@@@@@@@@@@@@@@  "+result_str); 
					return result_str;
				}
				if(returnType.equalsIgnoreCase("CARD_INSTALLMENT_DETAILS"))
				{
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "cifId jsp: getOutputXMLValuesresult:CardInstallmentDetailsFlag inside card installment123");
					result_str=parseCardInstallmentsDetails(returnType,parseXml,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,appServerType,prod,subprod,cifId,CardNumber);
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "cifId jsp: getOutputXMLValuesresult:CardInstallmentDetailsFlag "+result_str);
				}
			}
			//ended
			
			else if(parseXml.indexOf("<OperationType>")>-1)
			{
				returnType= parseXml.substring(parseXml.indexOf("<OperationType>")+15,parseXml.indexOf("</OperationType>"));
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "$$returnTypereturnTypereturnTypereturnType "+returnType);
				//Added By Prabhakar
			if(!"0000".equalsIgnoreCase(returnCode))
				{	
					String errorQuery="SELECT isnull((SELECT alert FROM ng_MASTER_INTEGRATION_ERROR_CODE with(nolock) WHERE  error_code='"+returnCode+"'),(SELECT alert FROM ng_MASTER_INTEGRATION_ERROR_CODE with(nolock) WHERE error_code='DEFAULT')) As Alert";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "@@@@@@@@@@@@@@  "+errorQuery);
					String strInputXml = ExecuteQuery_APSelect(errorQuery,cabinetName,sessionId);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "@@@@@@@@@@@@@@  "+strInputXml);
					String strOutputXml = makeCall(wrapperIP, Short.valueOf(wrapperPort), strInputXml);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "@@@@@@@@@@@@@@  "+strOutputXml);
					result_str=strOutputXml.substring(strOutputXml.indexOf("<Alert>")+"</Alert>".length()-1,strOutputXml.indexOf("</Alert>"));
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "@@@@@@@@@@@@@@  "+result_str); 
					return result_str;
				}
				
				else if(returnType.equalsIgnoreCase("AVGBALDET"))
				{
					result_str=parseAVGBALDET(returnType,parseXml,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,appServerType,cifId);
				}
				else if(returnType.equalsIgnoreCase("RETURNDET"))
				{
					result_str=parseRETURNDET(returnType,parseXml,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,appServerType,cifId);
				}
				else if(returnType.equalsIgnoreCase("LIENDET"))
				{
					result_str=parseLIENDET(returnType,parseXml,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,appServerType,cifId);
				}
				else if(returnType.equalsIgnoreCase("SIDET"))
				{
					result_str=parseSIDET(returnType,parseXml,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,appServerType,cifId);
				}
				else if(returnType.equalsIgnoreCase("SALDET"))
				{
					result_str=parseSALDET(returnType,parseXml,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,appServerType,cifId);
				}
				
				else if(returnType.equalsIgnoreCase("TRANSUM"))
				{
					result_str=parseTRANSUM(returnType,parseXml,wrapperIP,wrapperPort,sessionId,cabinetName,wi_name,appServerType,cifId);
				}
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "$$result_str "+result_str);
			}
			
				returnType= parseXml.substring(parseXml.indexOf("<MsgFormat>")+11,parseXml.indexOf("</MsgFormat>"));
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "$$returnType result_strresult_strresult_str"+returnType);
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "$$MsgFormat "+returnType);
				if(returnType.equalsIgnoreCase("FINANCIAL_SUMMARY") &&(result_str.equalsIgnoreCase(""))){
					result_str=returnCode;
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "$$result_str result_strresult_strresult_str"+result_str);
				}
				if(returnType.equalsIgnoreCase("CUSTOMER_EXPOSURE") &&(result_str.equalsIgnoreCase(""))){
					result_str=returnCode;
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "$$result_str result_strresult_strresult_str"+result_str);
				}
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "$$result_str saurabh"+result_str);
			
			
		}
		catch(Exception e)
        {     
			System.out.println("Exception occured in getOutputXMLValues method: "+ e.getMessage());
			e.printStackTrace();
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Exception occured in getOutputXMLValues method:  "+e.getMessage());
			result_str ="Failure";
        }
		
		return (result_str);
	}

	public static String parseInternalExposure(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String prod, String subprod, String cifId,String cust_type)
	{
		String flag1="";
		
		String tagName="";
		String subTagName="";
		String sTableName="";
		String sParentTagName="";
		String result="";
		//Deepak code commented method changed with new subtag_single param 23jan2018
		String subtag_single="";
		InputStream is = new ByteArrayInputStream(parseXml.getBytes());
	    LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Inside parseInternalExposure: ");
	    try{
	    	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();
		
			NodeList nList_loan = doc.getElementsByTagName("CustomerExposureResponse");
			
			 
			for(int i = 0 ; i<nList_loan.getLength();i++){
				Node node  = nList_loan.item(i);
				Document newXmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
				 DOMImplementationLS abc  = (DOMImplementationLS) newXmlDocument.getImplementation();
				 LSSerializer lsSerializer = abc.createLSSerializer();
				
				Element root = newXmlDocument.createElement("root");
				 newXmlDocument.appendChild(root);
				root.appendChild(newXmlDocument.importNode(node, true));
				 String n_parseXml = lsSerializer.writeToString(newXmlDocument);
				 n_parseXml = n_parseXml.substring(n_parseXml.indexOf("<root>")+6,n_parseXml.indexOf("</root>"));
				 cifId =  (n_parseXml.contains("<CustIdValue>")) ? n_parseXml.substring(n_parseXml.indexOf("<CustIdValue>")+"</CustIdValue>".length()-1,n_parseXml.indexOf("</CustIdValue>")):cifId;
				 tagName="LoanDetails"; 
					subTagName = "KeyDt,AmountDtls,DelinquencyInfo";
					sTableName="ng_RLOS_CUSTEXPOSE_LoanDetails";
					subtag_single="";
					flag1=commonParseProduct(n_parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,cust_type,subtag_single);
					
					if(flag1.equalsIgnoreCase("true")){
					
						tagName="CardDetails";
						subTagName = "KeyDt,AmountDtls,DelinquencyInfo";
						sTableName="ng_RLOS_CUSTEXPOSE_CardDetails";
						subtag_single="";
						flag1=commonParseProduct(n_parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,cust_type,subtag_single);
						
						if(flag1.equalsIgnoreCase("true")){
							tagName="AcctDetails";
							subTagName = "KeyDt,AmountDtls,DelinquencyInfo";
							sTableName="ng_RLOS_CUSTEXPOSE_AcctDetails";
							subtag_single="ODDetails";
							flag1=commonParseProduct(n_parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,cust_type,subtag_single);
							
								if(flag1.equalsIgnoreCase("true")){
								tagName="Derived";
								subTagName = "";
								sTableName="NG_rlos_custexpose_Derived";
								subtag_single="";
								flag1=commonParseProduct(n_parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,cust_type,subtag_single);
										if(flag1.equalsIgnoreCase("true")){
											tagName="RecordDestribution";
											subTagName = "";
											sTableName="NG_RLOS_CUSTEXPOSE_RecordDestribution";
											subtag_single="";
											flag1=commonParseProduct(n_parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,cust_type,subtag_single);
											}
										else{
											flag1="false";
											}
								}
								else{
									flag1="false";
								}
						}
						else{
							flag1="false";
						}
					}
					else
					{
						flag1="false";
					}
					
				}
	    }
	    catch(Exception e){
	    	
	    }
			finally
			{
				try{
			    		if(is!=null)
			    		{
			    		is.close();
			    		is=null;
			    		}
			    	}
			    	catch(Exception e){
			    		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Exception occured in is close:  "+e.getMessage());
			    	}
			}
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "End parseInternalExposure: ");	
	  	return flag1;
	}
	public static String parseExternalExposure(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String prod, String subprod, String cifId,String cust_type)
	{
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Inside parseExternalExposure: ");
		String ReturnCode="";
		
		if(parseXml.indexOf("<ReturnCode>")>-1)
		{
			ReturnCode= parseXml.substring(parseXml.indexOf("<ReturnCode>")+12,parseXml.indexOf("</ReturnCode>"));
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "$$return Code "+ReturnCode);
		}
		
		if(ReturnCode.equalsIgnoreCase("B003"))
		{
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "AECB:No record found!!");
			return "B003";
		}
		
		String flag1="";
		
		String tagName="";
		String subTagName="";
		String sTableName="";
		String sParentTagName="";
		String result="";
		//Deepak code commented method changed with new subtag_single param 23jan2018
		String subtag_single="";
		
		tagName="ChequeDetails"; 
		subTagName = "";
		sTableName="ng_rlos_cust_extexpo_ChequeDetails";
		flag1 = commonParseProduct(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,cust_type,subtag_single);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "cifId jsp: parseExternalExposure: updated or inserted"+flag1);
		
		
		if(flag1.equalsIgnoreCase("true")){
			tagName="LoanDetails"; 
			subTagName = "KeyDt,AmountDtls";
			sTableName="ng_rlos_cust_extexpo_LoanDetails";
			flag1 = commonParseProduct(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,cust_type,subtag_single);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "cifId jsp: parseExternalExposure: updated or inserted1"+flag1);
			
			if(flag1.equalsIgnoreCase("true")){
				tagName="CardDetails"; 
				subTagName = "KeyDt,AmountDtls,DelinquencyInfo";
				sTableName="ng_rlos_cust_extexpo_CardDetails";
				flag1 = commonParseProduct(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,cust_type,subtag_single);	
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "cifId jsp: parseExternalExposure: updated or inserted2"+flag1);
				
					if(flag1.equalsIgnoreCase("true")){
					tagName="Derived"; 
					subTagName = "";
					sTableName="NG_rlos_custexpose_Derived";
					flag1 = commonParseProduct(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,cust_type,subtag_single);	
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "cifId jsp: parseExternalExposure: updated or NG_rlos_custexpose_Derived"+flag1);
								if(flag1.equalsIgnoreCase("true")){
								tagName="RecordDestribution";
								subTagName = "";
								sTableName="NG_RLOS_CUSTEXPOSE_RecordDestribution";
								flag1=commonParseProduct(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,cust_type,subtag_single);
									if(flag1.equalsIgnoreCase("true")){
										tagName="AcctDetails";
										subTagName = "KeyDt,AmountDtls";
										sTableName="ng_rlos_cust_extexpo_AccountDetails";
										flag1=commonParseProduct(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,cust_type,subtag_single);
										}
									else{
										flag1="false";
										}
								}
							else{
								flag1="false";
								}
					}
					else{
						flag1 ="false";
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "cifId jsp: parseExternalExposure: updated or NG_rlos_custexpose_Derived"+flag1);
						}
			}
			else{
				flag1 ="false";
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "cifId jsp: parseExternalExposure: updated or insertedfalse"+flag1);
			}
		
			
		}
		else{
			flag1 ="false";
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "cifId jsp: parseExternalExposure: updated or insertedfalse1"+flag1);
		}
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "cifId jsp: parseExternalExposure: updated or inserted final value"+flag1);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "END parseExternalExposure: ");
	return flag1;
	}
	
	public static String parseCollectionSummary(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String prod, String subprod, String cifId,String cust_type)
	{
		String tagName="";
		String subTagName="";
		String sTableName="";
		String sParentTagName="";
		String result="";
		String flag1="";
		//Deepak code commented method changed with new subtag_single param 23jan2018
		String subtag_single="";
		
		
		tagName="LoanDetails"; 
		subTagName = "KeyDt,AmountDtls,DelinquencyInfo";
		sTableName="ng_RLOS_CUSTEXPOSE_LoanDetails";
		flag1=commonParseProduct_collection(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,subtag_single,cust_type);
		
		if(flag1.equalsIgnoreCase("true")){
		tagName="CardDetails";
		subTagName = "KeyDt,AmountDtls,DelinquencyInfo";
		sTableName="ng_RLOS_CUSTEXPOSE_CardDetails";
		flag1=commonParseProduct_collection(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,subtag_single,cust_type);
					if(flag1.equalsIgnoreCase("true")){
					tagName="Derived";
					subTagName = "";
					sTableName="NG_rlos_custexpose_Derived";
					flag1=commonParseProduct(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,prod,subprod,cifId,cust_type,subtag_single);
					
					}
					else{
						flag1="false";
					}
		}
		else{
			flag1="false";
		}
		return flag1;
	}
	//added
		public static String parseCardInstallmentsDetails(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String prod, String subprod, String cifId,String CardNumber)
	{
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "wrapperIP jsp: parseCardInstallmentsDetails: "+wrapperIP);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "wrapperPort jsp: parseCardInstallmentsDetails: "+wrapperPort);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "sessionId jsp: parseCardInstallmentsDetails: "+sessionId);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "cabinetName jsp: parseCardInstallmentsDetails: "+cabinetName);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "wi_name jsp: parseCardInstallmentsDetails: "+wi_name);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "appServerType jsp: parseCardInstallmentsDetails: "+appServerType);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "parseXml jsp: parseCardInstallmentsDetails: "+parseXml);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "returnType jsp: parseCardInstallmentsDetails: "+returnType);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "cifId jsp: parseCardInstallmentsDetails: "+cifId);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "cifId jsp: parseCardInstallmentsDetails:CardNumber "+CardNumber);
		
		String tagName="";
		String subTagName="";
		String sTableName="";
		String sParentTagName="";
		String result="";
		String flag1="";
		//Deepak code commented method changed with new subtag_single param 23jan2018
		String subtag_single="";
		
		
		tagName="TransactionDetailsRec"; 
		subTagName = "";
		sTableName="ng_RLOS_CUSTEXPOSE_CardInstallmentDetails";
		flag1=commonParseFinance_CardInstallment(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,CardNumber,subtag_single);
		
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "wrapperIP jsp: CardInstallmentDetailsResponse: "+flag1);
		
	
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "wrapperIP jsp: CardInstallmentDetailsResponse final value: "+flag1);
		return flag1;
	}
	//ended
	public static String parseTRANSUM(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String cifId)
	{
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "wrapperIP jsp: parseTRANSUM: "+wrapperIP);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "wrapperPort jsp: parseTRANSUM: "+wrapperPort);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "sessionId jsp: parseTRANSUM: "+sessionId);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "cabinetName jsp: parseTRANSUM: "+cabinetName);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "wi_name jsp: parseTRANSUM: "+wi_name);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "appServerType jsp: parseTRANSUM: "+appServerType);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "parseXml jsp: parseTRANSUM: "+parseXml);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "returnType jsp: parseTRANSUM: "+returnType);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "cifId jsp: parseTRANSUM: "+cifId);
		
		String tagName="";
		String subTagName="";
		String sTableName="";
		String sParentTagName="";
		String flag1="";
		//Deepak code commented method changed with new subtag_single param 23jan2018
		String subtag_single="";
		
		tagName= "TxnSummaryDtls";		
		subTagName= "";
		sTableName="ng_rlos_FinancialSummary_TxnSummary";
		//commonParseFinance(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName);
		flag1=commonParseFinance(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,subtag_single);
		if(flag1.equalsIgnoreCase("true")){
			flag1="true";
		}
		else{
			flag1="false";
		}
	return flag1;
	}
	public static String parseAVGBALDET(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String cifId)
	{
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "wrapperIP jsp: parseAVGBALDET: "+wrapperIP);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "wrapperPort jsp: parseAVGBALDET: "+wrapperPort);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "sessionId jsp: parseAVGBALDET: "+sessionId);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "cabinetName jsp: parseAVGBALDET: "+cabinetName);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "wi_name jsp: parseAVGBALDET: "+wi_name);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "appServerType jsp: parseAVGBALDET: "+appServerType);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "parseXml jsp: parseAVGBALDET: "+parseXml);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "returnType jsp: parseAVGBALDET: "+returnType);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "cifId jsp: parseAVGBALDET: "+cifId);
		
		String flag1="";
		String tagName="";
		String subTagName="";
		String sTableName="";
		String sParentTagName="";
		//Deepak code commented method changed with new subtag_single param 23jan2018
		String subtag_single="";
		
		tagName= "FinancialSummaryRes";		
		subTagName= "AvgBalanceDtls";
		sTableName="ng_rlos_FinancialSummary_AvgBalanceDtls";
		//commonParseFinance(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName);
		flag1=commonParseFinance(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,subtag_single);
		if(flag1.equalsIgnoreCase("true")){
			flag1="true";
		}
		else{
			flag1="false";
		}
	return flag1;
	
	}
	public static String parseRETURNDET(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String cifId)
	{
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "wrapperIP jsp: parseRETURNDET: "+wrapperIP);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "wrapperPort jsp: parseRETURNDET: "+wrapperPort);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "sessionId jsp: parseRETURNDET: "+sessionId);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "cabinetName jsp: parseRETURNDET: "+cabinetName);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "wi_name jsp: parseRETURNDET: "+wi_name);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "appServerType jsp: parseRETURNDET: "+appServerType);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "parseXml jsp: parseRETURNDET: "+parseXml);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "returnType jsp: parseRETURNDET: "+returnType);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "cifId jsp: parseRETURNDET: "+cifId);
		
		String tagName="";
		String subTagName="";
		String sTableName="";
		String sParentTagName="";
		String flag1="";
		//Deepak code commented method changed with new subtag_single param 23jan2018
		String subtag_single="";
		
		tagName= "ReturnsDtls";		
		subTagName= "";
		sTableName="ng_rlos_FinancialSummary_ReturnsDtls";
		//commonParseFinance(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName);
		flag1=commonParseFinance(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,subtag_single);
		if(flag1.equalsIgnoreCase("true")){
			flag1="true";
		}
		else{
			flag1="false";
		}
	return flag1;
	
	}
	public static String parseLIENDET(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String cifId)
	{
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "wrapperIP jsp: parseLIENDET: "+wrapperIP);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "wrapperPort jsp: parseLIENDET: "+wrapperPort);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "sessionId jsp: parseLIENDET: "+sessionId);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "cabinetName jsp: parseLIENDET: "+cabinetName);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "wi_name jsp: parseLIENDET: "+wi_name);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "appServerType jsp: parseLIENDET: "+appServerType);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "parseXml jsp: parseLIENDET: "+parseXml);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "returnType jsp: parseLIENDET: "+returnType);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "cifId jsp: parseLIENDET: "+cifId);
		
		String tagName="";
		String subTagName="";
		String sTableName="";
		String sParentTagName="";
		String flag1="";
		//Deepak code commented method changed with new subtag_single param 23jan2018
		String subtag_single="";
		tagName= "LienDetails";		
		subTagName= "";
		sTableName="ng_rlos_FinancialSummary_LienDetails";
		//commonParseFinance(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName);
		flag1=commonParseFinance(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,subtag_single);

		if(flag1.equalsIgnoreCase("true")){
			flag1="true";
		}
		else{
			flag1="false";
		}
	return flag1;
		
	}
	public static String parseSIDET(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String cifId)
	{
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "wrapperIP jsp: parseSIDET: "+wrapperIP);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "wrapperPort jsp: parseSIDET: "+wrapperPort);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "sessionId jsp: parseSIDET: "+sessionId);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "cabinetName jsp: parseSIDET: "+cabinetName);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "wi_name jsp: parseSIDET: "+wi_name);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "appServerType jsp: parseSIDET: "+appServerType);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "parseXml jsp: parseSIDET: "+parseXml);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "returnType jsp: parseSIDET: "+returnType);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "cifId jsp: parseSIDET: "+cifId);
		
		String tagName="";
		String subTagName="";
		String sTableName="";
		String sParentTagName="";
		String flag1="";
		//Deepak code commented method changed with new subtag_single param 23jan2018
		String subtag_single="";
		
		tagName= "SIDetails";		
		subTagName= "";
		sTableName="ng_rlos_FinancialSummary_SiDtls";
		//commonParseFinance(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName);
		flag1=commonParseFinance(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,subtag_single);
		if(flag1.equalsIgnoreCase("true")){
				flag1="true";
			}
			else{
				flag1="false";
			}
	return flag1;
	
	}
	public static String parseSALDET(String returnType,String parseXml,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String wi_name,String appServerType, String cifId)
	{
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "wrapperIP jsp: parseSALDET: "+wrapperIP);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "wrapperPort jsp: parseSALDET: "+wrapperPort);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "sessionId jsp: parseSALDET: "+sessionId);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "cabinetName jsp: parseSALDET: "+cabinetName);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "wi_name jsp: parseSALDET: "+wi_name);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "appServerType jsp: parseSALDET: "+appServerType);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "parseXml jsp: parseSALDET: "+parseXml);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "returnType jsp: parseSALDET: "+returnType);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "cifId jsp: parseSALDET: "+cifId);
		
		String tagName="";
		String subTagName="";
		String sTableName="";
		String sParentTagName="";
		String flag1="";
		//Deepak code commented method changed with new subtag_single param 23jan2018
		String subtag_single="";
		
		tagName= "SalDetails";		
		subTagName= "";
		sTableName="ng_rlos_FinancialSummary_SalTxnDetails";
		//commonParseFinance(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName);
		flag1=commonParseFinance(parseXml,tagName,wi_name,returnType,sTableName,wrapperIP,wrapperPort,sessionId,cabinetName,appServerType,subTagName,subtag_single);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "return flag1 jsp: parseSALDET: "+flag1);
		return flag1;
	}
	
	
	
	public static String commonParse(String parseXml,String tagName,String wi_name,String returnType,String sTableName,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String appServerType, String subTagName, String cifId)
	{
		String retVal = "";
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "commonParse jsp: inside: ");
		String [] valueArr= null;
		String strInputXml="";
		String strOutputXml="";
		String columnName = "";
		String columnValues = "";
		String tagNameU = "";
		String subTagNameU = "";
		String subTagNameU_2 = "";
		String mainCode = "";
		String sWhere = "";
		String row_updated = "";
		
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "tagName jsp: commonParse: "+tagName);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "subTagName jsp: commonParse: "+subTagName);
		
		
		 Map<Integer, String> tagValuesMap= new LinkedHashMap<Integer, String>();		 
		 tagValuesMap=getTagDataParent(parseXml,tagName,subTagName);
		
		 Map<Integer, String> map = tagValuesMap;
	  String colValue="";
		  for (Map.Entry<Integer, String> entry : map.entrySet())
		  {
			  valueArr=entry.getValue().split("~");
			  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "tag values" + entry.getValue());
			  
			  //columnValues = valueArr[1].spilt(",");
			 // columnValues=columnValues+",'"+getCellData(SheetName1, rCnt, cCnt)+"'";
			  colValue = "'"+valueArr[1].replaceAll("[,]", "','")+"'";
			  columnName = valueArr[0]+",Wi_Name,Request_Type,CifId";
			  columnValues = colValue+",'"+wi_name+"','"+returnType+"','"+cifId+"'";
			   
			  
			   
			  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "columnName commonParse" + columnName);
			  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "columnValues commonParse" + columnValues);
			  String[] columnValues_arr = columnValues.split(",");
			  if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_CardDetails")){
				   sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND CardEmbossNum = "+columnValues_arr[0]+" AND Liability_type='Individual_CIF'";
			  }
			  else if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_LoanDetails")){
				  sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+columnValues_arr[0]+" AND Liability_type='Individual_CIF'";
			  }
				  else{
			   sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND Liability_type='Individual_CIF'";
			  }
			  strInputXml =	ExecuteQuery_APUpdate(sTableName,columnName,columnValues,sWhere,cabinetName,sessionId);
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "strInputXml update " + strInputXml);
				  try 
					{
						strOutputXml = makeCall(wrapperIP, Short.valueOf(wrapperPort), strInputXml);
						
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: strOutputXml update: "+strOutputXml);
					} 
					catch (NGException e) 
					{
					   e.printStackTrace();
					} 
					catch (Exception ex) 
					{
					   ex.printStackTrace();
					}
					
					tagNameU = "APUpdate_Output";
					subTagNameU = "MainCode";
					subTagNameU_2 = "Output";
					mainCode = getTagValue(strOutputXml,tagNameU,subTagNameU);
					row_updated = getTagValue(strOutputXml,tagNameU,subTagNameU_2);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagValue select mainCode --> "+mainCode);
					if(!mainCode.equalsIgnoreCase("0") || row_updated.equalsIgnoreCase("0"))
					{
					  strInputXml =	ExecuteQuery_APInsert(sTableName,columnName,columnValues,cabinetName,sessionId);
					  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "strInputXml" + strInputXml);
					  try 
						{
							strOutputXml = makeCall(wrapperIP, Short.valueOf(wrapperPort), strInputXml);
							
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: strOutputXml: "+strOutputXml);
							mainCode = getTagValue(strOutputXml,"APInsert_Output",subTagNameU);
							if(!mainCode.equalsIgnoreCase("0"))
							{
								retVal = "false";
								LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: commonParse true for insert: "+retVal);
							}
							else
							{
								retVal = "true";
								LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: commonParse false for insert: "+retVal);
							}
						} 
						catch (NGException e) 
						{
						   e.printStackTrace();
						} 
						catch (Exception ex) 
						{
						   ex.printStackTrace();
						}
				}
			  	else
				{
					retVal = "true";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: commonParse true for update: "+retVal);
				}
		  }
		   LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: commonParse true for final return value: "+retVal);
		  return retVal;
		 
	}
	
	//added 12/07/2017
	
		public static String commonParseFinance_CardInstallment(String parseXml,String tagName,String wi_name,String returnType,String sTableName,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String appServerType, String subTagName,String CardNumber,String subtag_single)
	{
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "commonParseFinance jsp: inside: ");
		String retVal = "";
		String [] valueArr= null;
		String strInputXml="";
		String strOutputXml="";
		String columnName = "";
		String columnValues = "";
		String tagNameU = "";
		String subTagNameU = "";
		String subTagNameU_2 = "";
		String mainCode = "";
		String sWhere = "";
		String row_updated = "";
		String txnNum="";
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "tagName jsp: commonParseFinance: "+tagName);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "subTagName jsp: commonParseFinance: "+subTagName);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "sTableName jsp: commonParseFinance: "+sTableName);
		
		
		if((returnType.equalsIgnoreCase("CARD_INSTALLMENT_DETAILS")&& parseXml.contains("TransactionDetailsRec")))
		{
			
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "returnType jsp: commonParseFinance: "+returnType);
		 Map<String, String> tagValuesMap= new LinkedHashMap<String, String>();		 
		 tagValuesMap=getTagDataParent_deep(parseXml,tagName,subTagName,subtag_single);
		
		 Map<String , String> map = tagValuesMap;
		String colValue="";
		  for (Map.Entry<String, String> entry : map.entrySet())
		  {
			  valueArr=entry.getValue().split("~");
			  for(int i=0;i<valueArr.length;i++)
			  {
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "tag values:12345 " +valueArr[i]);
			}
			  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "tag values: " + entry.getValue());
			   LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "Key values: " + entry.getKey());
			   
			   colValue = "'"+valueArr[1].replaceAll("[,]", "','")+"'";
			  
			  
			  //added
			   if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_CardInstallmentDetails")){
				  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "Inside commonParseFinance for ng_RLOS_CUSTEXPOSE_CardInstallmentDetails");
				  String header_info = getTagDataParent_cardInstallment_header(parseXml,"CardInstallmentDetailsResponse","CIFID,CardCRNNumber,CardSerialNumber,OTBAmount,TotalExposureAmount,TotalRepaymentAmount,InstallmentAccountStatus");
				  
				  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Inside commonParseFinance for ng_RLOS_CUSTEXPOSE_CardInstallmentDetails header info: "+ header_info);
				  String [] header_info_arr = header_info.split(":");
				  
				  columnName = valueArr[0]+",Wi_Name,Request_Type,"+header_info_arr[0];
				  columnValues = valueArr[1]+",'"+wi_name+"','CARD_INSTALLMENT_DETAILS',"+header_info_arr[1];
				  String columnName_arr[] = columnName.split(",");
				  String columnValues_arr[] = columnValues.split(",");
					columnValues="";
				  for(int i=0;i<columnName_arr.length;i++)
				  {
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Inside Card Installment for loop to remove I:"+columnName_arr[i]);
					if(columnName_arr[i].equalsIgnoreCase("CardNumber"))
					{
						columnValues_arr[i]=columnValues_arr[i].replace("I","");
					}
					if(i==0){
						columnValues=columnValues_arr[i];
					}
					else{
						columnValues=columnValues+","+columnValues_arr[i];
					}
				  }
				  
				  txnNum = columnValues_arr[Arrays.asList(columnName_arr).indexOf("TxnSerialNum")];
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Inside Cardinstallment: columnName after merging:"+columnValues);

				 // sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND TxnSerialNum = '"+entry.getKey()+"' ";
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND TxnSerialNum = "+txnNum+"";
				
				   LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "sWhere of cardinstallmentDetails"+sWhere);
				}
			  //ended
			 
			  else{
				columnName = valueArr[0]+",Wi_Name,Request_Type";
				columnValues = colValue+",'"+wi_name+"','"+returnType+"'";  
			  }
			  
			  
			  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "columnName commonParse123" + columnName);
			  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "columnValues commonParse456" + columnValues);
			  
			 strInputXml =	ExecuteQuery_APUpdate(sTableName,columnName,columnValues,sWhere,cabinetName,sessionId);
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "strInputXml update for finance " + strInputXml);
				  try 
					{
						strOutputXml = makeCall(wrapperIP, Short.valueOf(wrapperPort), strInputXml);
						
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: strOutputXml update:123 "+strOutputXml);
					} 
					catch (NGException e) 
					{
					   e.printStackTrace();
					} 
					catch (Exception ex) 
					{
					   ex.printStackTrace();
					}
					
					tagNameU = "APUpdate_Output";
					subTagNameU = "MainCode";
					subTagNameU_2 = "Output";
					mainCode = getTagValue(strOutputXml,tagNameU,subTagNameU);
					row_updated = getTagValue(strOutputXml,tagNameU,subTagNameU_2);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagValue select mainCode123 --> "+mainCode);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagValue select mainCode123 --> "+row_updated);
					if(!mainCode.equalsIgnoreCase("0") || row_updated.equalsIgnoreCase("0"))
					{
						  strInputXml =	ExecuteQuery_APInsert(sTableName,columnName,columnValues,cabinetName,sessionId);
						  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "strInputXml123Installment insert Query:" + strInputXml);
						  try 
							{
								strOutputXml = makeCall(wrapperIP, Short.valueOf(wrapperPort), strInputXml);
								
								LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: strOutputXml:1234 "+strOutputXml);
								LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: strOutputXml:mainCode value "+mainCode);
								mainCode = getTagValue(strOutputXml,"APInsert_Output",subTagNameU);
								LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: strOutputXml:mainCode value1234 "+mainCode);
								if(!mainCode.equalsIgnoreCase("0"))
								{
									retVal = "false";
									LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: commonparseproduct:false "+retVal);
								}
								else
								{
									retVal = "true";
									LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: commonparseproduct:true "+retVal);
								}
							} 
							catch (NGException e) 
							{
							   e.printStackTrace();
							} 
							catch (Exception ex) 
							{
							   ex.printStackTrace();
							}
					}
					else
					{
						retVal = "true";
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: commonparseproductapupdate:true "+retVal);
					}
		  }
			
		}
		else{
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "returnType jsp: commonParseFinance Empty tag : "+returnType+" Wi_Name: "+wi_name);
		}
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: final value for financial summary "+retVal);
			return retVal;
	}
	
	
	//ended 12/07/2017
		public static String commonParseFinance(String parseXml,String tagName,String wi_name,String returnType,String sTableName,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String appServerType, String subTagName,String subtag_single)
	{
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "commonParseFinance jsp: inside: ");
		String retVal = "";
		String [] valueArr= null;
		String strInputXml="";
		String strOutputXml="";
		String columnName = "";
		String columnValues = "";
		String tagNameU = "";
		String subTagNameU = "";
		String subTagNameU_2 = "";
		String mainCode = "";
		String sWhere = "";
		String row_updated = "";
		String id="";
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "tagName jsp: commonParseFinance: "+tagName);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "subTagName jsp: commonParseFinance: "+subTagName);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "sTableName jsp: commonParseFinance: "+sTableName);
		
		
		if((returnType.equalsIgnoreCase("RETURNDET")&& parseXml.contains("ReturnsDtls"))||(returnType.equalsIgnoreCase("AVGBALDET")&& parseXml.contains("AcctId"))||(returnType.equalsIgnoreCase("LIENDET")&& parseXml.contains("LienDetails"))||(returnType.equalsIgnoreCase("SIDET")&& parseXml.contains("SIDetails"))||(returnType.equalsIgnoreCase("TRANSUM")&& parseXml.contains("TxnSummary"))||(returnType.equalsIgnoreCase("SALDET")&& parseXml.contains("SalDetails")))
		{
			
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "returnType jsp: commonParseFinance: "+returnType);
		 Map<String, String> tagValuesMap= new LinkedHashMap<String, String>();		 
		 tagValuesMap=getTagDataParent_deep(parseXml,tagName,subTagName, subtag_single);
		
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Inside commonParseFinance-->tagValuesMap of column and values: "+tagValuesMap);

		 Map<String , String> map = tagValuesMap;
		String colValue="";
		  for (Map.Entry<String, String> entry : map.entrySet())
		  {
			  valueArr=entry.getValue().split("~");
			  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "tag values:1234 " +valueArr);
			  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "tag values: " + entry.getValue());
			  
			   colValue = "'"+valueArr[1].replaceAll("[,]", "','")+"'";
			  if(returnType.equalsIgnoreCase("AVGBALDET")&& valueArr[0].contains("AcctId")){
				  String columnName_arr[] = valueArr[0].split(",");
				  String columnValues_arr[] = valueArr[1].split(",");
				  id = columnValues_arr[Arrays.asList(columnName_arr).indexOf("AcctId")];
			  }
			  
			  if(sTableName.equalsIgnoreCase("ng_rlos_FinancialSummary_AvgBalanceDtls")){
				  columnName = valueArr[0]+",Wi_Name";
				  columnValues = valueArr[1]+",'"+wi_name+"'";
				  sWhere="Wi_Name='"+wi_name+"' AND OperationType='"+returnType+"' AND AcctId = "+id;
			  }
			  else if(sTableName.equalsIgnoreCase("ng_rlos_FinancialSummary_ReturnsDtls")){
				  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "Inside commonParseFinance for ng_rlos_FinancialSummary_ReturnsDtls");
				  String header_info = getTagDataParent_financ_header(parseXml,"FinancialSummaryRes","CIFID,AcctId,OperationType");
				  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "Inside commonParseFinance for ng_rlos_FinancialSummary_ReturnsDtls header info: "+ header_info);
				  String [] header_info_arr = header_info.split(":");
				  columnName = valueArr[0]+",Wi_Name,"+header_info_arr[0];
				  columnValues = valueArr[1]+",'"+wi_name+"',"+header_info_arr[1];
				  String columnName_arr[] = columnName.split(",");
				  String columnValues_arr[] = columnValues.split(",");
				   if(returnType.equalsIgnoreCase("RETURNDET") && valueArr[0].contains("ReturnNumber")){
					id = columnValues_arr[Arrays.asList(columnName_arr).indexOf("ReturnNumber")];
					sWhere="Wi_Name='"+wi_name+"' AND OperationType='"+returnType+"' AND ReturnNumber = "+id;
				  }else{
					  id = columnValues_arr[Arrays.asList(columnName_arr).indexOf("AcctId")];
					  sWhere="Wi_Name='"+wi_name+"' AND OperationType='"+returnType+"' AND AcctId = "+id;
				  }
				  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "returndetails sWhere="+sWhere);
				}
			  else if(sTableName.equalsIgnoreCase("ng_rlos_FinancialSummary_TxnSummary")){
				  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "Inside commonParseFinance for ng_rlos_FinancialSummary_TxnSummary");
				  String header_info = getTagDataParent_financ_header(parseXml,"FinancialSummaryRes","CIFID,AcctId,OperationType");
				  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "Inside commonParseFinance for ng_rlos_FinancialSummary_TxnSummary header info: "+ header_info);
				  String [] header_info_arr = header_info.split(":");
				  String AvgCrTurnOver=(parseXml.contains("<AvgCrTurnOver>")) ? parseXml.substring(parseXml.indexOf("<AvgCrTurnOver>")+"</AvgCrTurnOver>".length()-1,parseXml.indexOf("</AvgCrTurnOver>")):"";
				  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "Inside commonParseFinance for ng_rlos_FinancialSummary_TxnSummary AvgCrTurnOver info: "+ AvgCrTurnOver);
				  columnName = valueArr[0]+",Wi_Name,AvgCrTurnOver,"+header_info_arr[0];
				  columnValues = valueArr[1]+",'"+wi_name+"','"+AvgCrTurnOver+"',"+header_info_arr[1];
				  String columnName_arr[] = columnName.split(",");
				  String columnValues_arr[] = columnValues.split(",");
				  id = columnValues_arr[Arrays.asList(columnName_arr).indexOf("AcctId")];
				  String Month = columnValues_arr[Arrays.asList(columnName_arr).indexOf("Month")];
				  sWhere="Wi_Name='"+wi_name+"' AND OperationType='"+returnType+"' AND AcctId = "+id+" and Month = "+Month+"";
			  }
			  else if(sTableName.equalsIgnoreCase("ng_rlos_FinancialSummary_LienDetails")){
				    LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "Inside commonParseFinance for ng_rlos_FinancialSummary_LienDetails");
				  String header_info = getTagDataParent_financ_header(parseXml,"FinancialSummaryRes","CIFID,AcctId,OperationType");
				  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "Inside commonParseFinance for ng_rlos_FinancialSummary_LienDetails header info: "+ header_info);
				  String [] header_info_arr = header_info.split(":");
				  String columnName_arr[] = header_info_arr[0].split(",");
				  String columnValues_arr[] = header_info_arr[1].split(",");
				  id = columnValues_arr[Arrays.asList(columnName_arr).indexOf("AcctId")];
				  String leinId=columnValues_arr[Arrays.asList(columnName_arr).indexOf("LienId")];
				  columnName = valueArr[0]+",Wi_Name,"+header_info_arr[0];
				  columnValues = valueArr[1]+",'"+wi_name+"',"+header_info_arr[1];
				  sWhere="Wi_Name='"+wi_name+"' AND OperationType='"+returnType+"' AND AcctId = "+id+" and LienId = "+leinId;
				}
			  else if(sTableName.equalsIgnoreCase("ng_rlos_FinancialSummary_SalTxnDetails")){
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "Inside commonParseFinance for ng_rlos_FinancialSummary_SalTxnDetails");
				  String header_info = getTagDataParent_financ_header(parseXml,"FinancialSummaryRes","CifId,AcctId,OperationType");
				  String [] header_info_arr = header_info.split(":");
				  columnName = valueArr[0]+",Wi_Name,"+header_info_arr[0];
				  columnValues = valueArr[1]+",'"+wi_name+"',"+header_info_arr[1];
				   String columnName_arr[] = columnName.split(",");
				  String columnValues_arr[] = columnValues.split(",");
				  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "Inside commonParseFinance for ng_rlos_FinancialSummary_SalTxnDetails--->columnName:"+columnName);
				  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "Inside commonParseFinance for ng_rlos_FinancialSummary_SalTxnDetails--->columnValues:"+columnValues);

				  id = columnValues_arr[Arrays.asList(columnName_arr).indexOf("AcctId")];
				  String SalCreditDate = columnValues_arr[Arrays.asList(columnName_arr).indexOf("SalCreditDate")];
				  sWhere="Wi_Name='"+wi_name+"' AND OperationType='"+returnType+"' AND AcctId = "+id+" and SalCreditDate = "+SalCreditDate+"";
			  }
			  else if(sTableName.equalsIgnoreCase("ng_rlos_FinancialSummary_SiDtls")){
					  String header_info = getTagDataParent_financ_header(parseXml,"FinancialSummaryRes","CifId,AcctId,OperationType");
					  String [] header_info_arr = header_info.split(":");
					  String columnName_arr[] = header_info_arr[0].split(",");
					  String columnValues_arr[] = header_info_arr[1].split(",");
					  id = columnValues_arr[Arrays.asList(columnName_arr).indexOf("AcctId")];
					  columnName = valueArr[0]+",Wi_Name,"+header_info_arr[0];
					  columnValues = valueArr[1]+",'"+wi_name+"',"+header_info_arr[1];
					  sWhere="Wi_Name='"+wi_name+"' AND OperationType='"+returnType+"' AND AcctId = "+id;
					  strInputXml =	ExecuteQuery_APdelete(sTableName,sWhere,cabinetName,sessionId);
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "strInputXml delete returndtls " + strInputXml);
					  try 
						{
							strOutputXml = makeCall(wrapperIP, Short.valueOf(wrapperPort), strInputXml);
							
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: strOutputXml delete returndtls: "+strOutputXml);
						} 
						catch (NGException e) 
						{
						   e.printStackTrace();
						} 
						catch (Exception ex) 
						{
						   ex.printStackTrace();
						}
			  }
			  else{
				columnName = valueArr[0]+",Wi_Name,Request_Type";
				columnValues = colValue+",'"+wi_name+"','"+returnType+"'";  
			  }
			  
			  
			  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "columnName commonParse" + columnName);
			  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "columnValues commonParse" + columnValues);
			  
			 strInputXml =	ExecuteQuery_APUpdate(sTableName,columnName,columnValues,sWhere,cabinetName,sessionId);
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "strInputXml update " + strInputXml);
				  try 
					{
						strOutputXml = makeCall(wrapperIP, Short.valueOf(wrapperPort), strInputXml);
						
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: strOutputXml update: "+strOutputXml);
					} 
					catch (NGException e) 
					{
					   e.printStackTrace();
					} 
					catch (Exception ex) 
					{
					   ex.printStackTrace();
					}
					
					tagNameU = "APUpdate_Output";
					subTagNameU = "MainCode";
					subTagNameU_2 = "Output";
					mainCode = getTagValue(strOutputXml,tagNameU,subTagNameU);
					row_updated = getTagValue(strOutputXml,tagNameU,subTagNameU_2);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagValue select mainCode --> "+mainCode);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagValue select mainCode --> "+row_updated);
					if(!mainCode.equalsIgnoreCase("0") || row_updated.equalsIgnoreCase("0"))
					{
						  strInputXml =	ExecuteQuery_APInsert(sTableName,columnName,columnValues,cabinetName,sessionId);
						  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "strInputXml" + strInputXml);
						  try 
							{
								strOutputXml = makeCall(wrapperIP, Short.valueOf(wrapperPort), strInputXml);
								
								LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: strOutputXml: "+strOutputXml);
								mainCode = getTagValue(strOutputXml,"APInsert_Output",subTagNameU);
								if(!mainCode.equalsIgnoreCase("0"))
								{
									retVal = "false";
										LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: ApINsertfalse for financial summary: "+retVal);
								}
								else
								{
									retVal = "true";
										LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: ApINserttrue for financial summary: "+retVal);
								}
							} 
							catch (NGException e) 
							{
							   e.printStackTrace();
							} 
							catch (Exception ex) 
							{
							   ex.printStackTrace();
							}
					}
					else
					{
						retVal = "true";
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: commonparseproductapupdate:true "+retVal);
					}
		  }
		}
		else{
		retVal = "true";
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "returnType jsp: commonParseFinance Empty tag : "+returnType+" Wi_Name: "+wi_name);
		}
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: final value for financial summary "+retVal);
		return retVal;
	}
	
	
	public static String commonParseProduct(String parseXml,String tagName,String wi_name,String returnType,String sTableName,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String appServerType, String subTagName,String prod,String subprod, String cifId,String cust_type,String subtag_single)
	{
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "inside commonParseProduct for cif: "+cifId);
		String retVal = "";
		if(!parseXml.contains(tagName)){
			return "true";
		}
		else
		{ 
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "commonParse jsp: inside: ");
			String [] valueArr= null;
			String strInputXml="";
			String strOutputXml="";
			String columnName = "";
			String columnValues = "";
			String tagNameU = "";
			String subTagNameU = "";
			String subTagNameU_2 = "";
			String mainCode = "";
			String sWhere = "";
			String row_updated = "";
			String ReportUrl = "";
			String FullNm = "";
			String TotalOutstanding = "";
			String TotalOverdue = "";
			String NoOfContracts = "";
			String ECRN = "";
			String BorrowingCustomer = "";
			String 	sQry="";
			String selectdata="";
			String companyUpdateQuery="";
			String companiestobeUpdated = "";
			boolean stopIndividualToInsert = false;
			
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "tagName jsp: commonParse: "+tagName);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "subTagName jsp: commonParse: "+subTagName);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "parsexml jsp: commonParse: "+parseXml);
			ReportUrl=(parseXml.contains("<ReportUrl>")) ? parseXml.substring(parseXml.indexOf("<ReportUrl>")+"</ReportUrl>".length()-1,parseXml.indexOf("</ReportUrl>")):"";
			FullNm=(parseXml.contains("<FullNm>")) ? parseXml.substring(parseXml.indexOf("<FullNm>")+"</FullNm>".length()-1,parseXml.indexOf("</FullNm>")):"";
			TotalOutstanding=(parseXml.contains("<TotalOutstanding>")) ? parseXml.substring(parseXml.indexOf("<TotalOutstanding>")+"</TotalOutstanding>".length()-1,parseXml.indexOf("</TotalOutstanding>")):"";
			TotalOverdue=(parseXml.contains("<TotalOverdue>")) ? parseXml.substring(parseXml.indexOf("<TotalOverdue>")+"</TotalOverdue>".length()-1,parseXml.indexOf("</TotalOverdue>")):"";
			NoOfContracts=(parseXml.contains("<NoOfContracts>")) ? parseXml.substring(parseXml.indexOf("<NoOfContracts>")+"</NoOfContracts>".length()-1,parseXml.indexOf("</NoOfContracts>")):"";
			ECRN=(parseXml.contains("<ECRN>")) ? parseXml.substring(parseXml.indexOf("<ECRN>")+"</ECRN>".length()-1,parseXml.indexOf("</ECRN>")):"";
			BorrowingCustomer=(parseXml.contains("<BorrowingCustomer>")) ? parseXml.substring(parseXml.indexOf("<BorrowingCustomer>")+"</BorrowingCustomer>".length()-1,parseXml.indexOf("</BorrowingCustomer>")):"";
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "ReportUrl jsp: ReportUrl: "+ReportUrl);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Cifid jsp: ReportUrl: "+cifId);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "FullNm jsp: ReportUrl: "+FullNm);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "TotalOutstanding jsp: TotalOutstanding: "+TotalOutstanding);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "TotalOverdue jsp: TotalOverdue: "+TotalOverdue);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "NoOfContracts jsp: NoOfContracts: "+NoOfContracts);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "ECRN jsp: ECRN: "+ECRN);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "BorrowingCustomer jsp: BorrowingCustomer: "+BorrowingCustomer);
			
			 Map<String, String> tagValuesMap= new LinkedHashMap<String, String>();		 
			 tagValuesMap=getTagDataParent_deep(parseXml,tagName,subTagName,subtag_single);
			
			 Map<String, String> map = tagValuesMap;
				String colValue="";
			  for (Map.Entry<String, String> entry : map.entrySet())
			  {
				  valueArr=entry.getValue().split("~");
				  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "tag values" + entry.getValue());
				 
					colValue = "'"+valueArr[1].replaceAll("[,]", "','")+"'";
					columnName = valueArr[0]+",Wi_Name,Request_Type,Product_Type,CardType,CifId";
					columnValues = valueArr[1]+",'"+wi_name+"','"+returnType+"','"+prod+"','"+subprod+"','"+cifId+"'";
				  
				   
				   
				  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "columnName commonParse" + columnName);
				  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "columnValues commonParse" + columnValues);
				  if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_CardDetails")){
					  columnName = valueArr[0]+",Wi_Name,Request_Type,CifId,Liability_type,BorrowingCustomer,ECRN";
					  columnValues = valueArr[1]+",'"+wi_name+"','"+returnType+"','"+cifId+"','"+cust_type+"','"+BorrowingCustomer+"','"+ECRN+"'";
					   sWhere="Wi_Name='"+wi_name+"' AND CardEmbossNum = '"+entry.getKey()+"' And Liability_type ='"+cust_type+"'";
					   
					   //change by saurabh on 2nd Feb for cust_type
					   sQry="Select count(Wi_Name) as selectdata from "+sTableName+" with(nolock) where Wi_Name='"+wi_name+"' And CardEmbossNum = '"+entry.getKey()+"' And Liability_type ='Individual_CIF' ";
						 LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "sQry sQry" + sQry);
						if(cust_type.equalsIgnoreCase("Individual_CIF")) {
							companyUpdateQuery="Select count(Wi_Name) as selectdata from "+sTableName+" with(nolock) where Wi_Name='"+wi_name+"' And CardEmbossNum = '"+entry.getKey()+"' And Liability_type ='Corporate_CIF'";
						}
				  }
				  else if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_LoanDetails")){
					  columnName = valueArr[0]+",Wi_Name,Request_Type,Product_Type,CardType,CifId,Liability_type,BorrowingCustomer,ECRN";
					  columnValues = valueArr[1]+",'"+wi_name+"','"+returnType+"','"+prod+"','"+subprod+"','"+cifId+"','"+cust_type+"','"+BorrowingCustomer+"','"+ECRN+"'";
					  columnName =columnName.replace("OutStandingAmt","TotalOutStandingAmt");
					  sWhere="Wi_Name='"+wi_name+"' AND AgreementId = '"+entry.getKey()+"' And Liability_type ='"+cust_type+"'";
					  //change by saurabh on 2nd Feb for cust_type
					    sQry="Select count(Wi_Name) as selectdata from "+sTableName+" with(nolock) where Wi_Name='"+wi_name+"' And  AgreementId = '"+entry.getKey()+"' And Liability_type ='Individual_CIF'";
						if(cust_type.equalsIgnoreCase("Individual_CIF")) {
							companyUpdateQuery="Select count(Wi_Name) as selectdata from "+sTableName+" with(nolock) where Wi_Name='"+wi_name+"' And AgreementId = '"+entry.getKey()+"' And Liability_type ='Corporate_CIF'";
						}
				  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "sQry  loan sQry" + sQry);
				  }
				  else if(sTableName.equalsIgnoreCase("ng_rlos_cust_extexpo_ChequeDetails")){
						  columnName = valueArr[0]+",Wi_Name,Request_Type,CifId";
						  columnValues = valueArr[1]+",'"+wi_name+"','"+returnType+"','"+cifId+"'";
						  sWhere="Wi_Name='"+wi_name+"' AND ChqType = '"+entry.getKey()+"'";
						   LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "columnName entry.getKey()" + entry.getKey());
				  }
				  else if(sTableName.equalsIgnoreCase("ng_rlos_cust_extexpo_LoanDetails")){
					   columnName = valueArr[0]+",Wi_Name,Request_Type,Product_Type,CardType,CifId,ReportURL,Liability_type";
					  columnValues = valueArr[1]+",'"+wi_name+"','"+returnType+"','"+prod+"','"+subprod+"','"+cifId+"','"+ReportUrl+"','"+cust_type+"'";
					  columnName =columnName.replace("OutStanding Balance","OutStanding_Balance");
					  columnName =columnName.replace("LastUpdateDate","datelastupdated");
					  columnName =columnName.replace("Total Amount","Total_Amount");
					  columnName =columnName.replace("Payments Amount","Payments_Amount");
					  columnName =columnName.replace("Overdue Amount","Overdue_Amount");
					  sWhere="Wi_Name='"+wi_name+"' AND AgreementId = '"+entry.getKey()+"'";
				  }
				  else if(sTableName.equalsIgnoreCase("ng_rlos_cust_extexpo_CardDetails")){
					  columnName = valueArr[0]+",Wi_Name,Request_Type,Product_Type,sub_product_type,CifId,Liability_type";
					  columnValues = valueArr[1]+",'"+wi_name+"','"+returnType+"','"+prod+"','"+subprod+"','"+cifId+"','"+cust_type+"'";
					  sWhere="Wi_Name='"+wi_name+"' AND CardEmbossNum = '"+entry.getKey()+"'";
				  }
				  else if(sTableName.equalsIgnoreCase("NG_rlos_custexpose_Derived")){
						  columnName = valueArr[0]+",Wi_Name,Request_Type,CifId,FullNm,TotalOutstanding,TotalOverdue,NoOfContracts,ReportURL";
						  columnValues = valueArr[1]+",'"+wi_name+"','"+returnType+"','"+cifId+"','"+FullNm+"','"+TotalOutstanding+"','"+TotalOverdue+"','"+NoOfContracts+"','"+ReportUrl+"'";
						  sWhere="Wi_Name='"+wi_name+"' AND Request_Type = '"+returnType+"' and CifId='"+cifId+"'";
				  }
				  //Changes Done to save data in NG_RLOS_CUSTEXPOSE_RecordDestribution table on 14th sept by Aman
				   else if(sTableName.equalsIgnoreCase("NG_RLOS_CUSTEXPOSE_RecordDestribution")){
						  columnName = valueArr[0]+",Wi_Name,Request_Type,CifId";
						  columnValues = valueArr[1]+",'"+wi_name+"','"+returnType+"','"+cifId+"'";
						  sWhere="Wi_Name='"+wi_name+"' AND ContractType = '"+entry.getKey()+"'";
				  }
				  //Changes Done to save data in NG_RLOS_CUSTEXPOSE_RecordDestribution table on 14th sept by Aman
				   else if(sTableName.equalsIgnoreCase("ng_rlos_cust_extexpo_AccountDetails")){
						  columnName = valueArr[0]+",Wi_Name,Request_Type,CifId";
						  columnValues = valueArr[1]+",'"+wi_name+"','"+returnType+"','"+cifId+"'";
						  sWhere="Wi_Name='"+wi_name+"' AND AcctId = '"+entry.getKey()+"'";
				  }
				  //below changes Done to save AccountType in ng_RLOS_CUSTEXPOSE_AcctDetails table on 29th Dec by Disha
				  else if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_AcctDetails")){
					 String CreditGrade = (parseXml.contains("<CreditGrade>")) ? parseXml.substring(parseXml.indexOf("<CreditGrade>")+"</CreditGrade>".length()-1,parseXml.indexOf("</CreditGrade>")):"";
					 //String ODType = (parseXml.contains("<ODType>")) ? parseXml.substring(parseXml.indexOf("<ODType>")+"</ODType>".length()-1,parseXml.indexOf("</ODType>")):"";
					 //String ODDesc = (parseXml.contains("<ODDesc>")) ? parseXml.substring(parseXml.indexOf("<ODDesc>")+"</ODDesc>".length()-1,parseXml.indexOf("</ODDesc>")):"";
						  columnName = valueArr[0]+",Wi_Name,Request_Type,CifId,CreditGrade,Account_Type";
						  columnValues = valueArr[1]+",'"+wi_name+"','"+returnType+"','"+cifId+"','"+CreditGrade+"','"+cust_type+"'";
						  sWhere="Wi_Name='"+wi_name+"' AND AcctId = '"+entry.getKey()+"' AND Account_Type = '"+cust_type+"'";
						  //change by saurabh on 24th Feb for skipping account save for Employer cif.
					sQry="Select count(EMPLOYER_CODE) as selectdata from NG_RLOS_ALOC_OFFLINE_DATA with(nolock) where CIF_ID ='"+cifId+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "sQry  loan sQry" + sQry);	  
				  }
				  //above changes Done to save AccountType in ng_RLOS_CUSTEXPOSE_AcctDetails table on 29th Dec by Disha
				else{
				   sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"'";
				  }
				  
				  strInputXml =	ExecuteQuery_APUpdate(sTableName,columnName,columnValues,sWhere,cabinetName,sessionId);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "strInputXml update " + strInputXml);
					  try 
						{
							strOutputXml = makeCall(wrapperIP, Short.valueOf(wrapperPort), strInputXml);
							
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: strOutputXml update: "+strOutputXml);
						} 
						catch (NGException e) 
						{
						   e.printStackTrace();
						} 
						catch (Exception ex) 
						{
						   ex.printStackTrace();
						}
						
						tagNameU = "APUpdate_Output";
						subTagNameU = "MainCode";
						subTagNameU_2 = "Output";
						mainCode = getTagValue(strOutputXml,tagNameU,subTagNameU);
						row_updated = getTagValue(strOutputXml,tagNameU,subTagNameU_2);
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagValue select mainCode for update query for cif"+cifId+"--> "+mainCode);
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagValue select rowUpdated for update query for cif"+cifId+" --> "+row_updated);
						if(!mainCode.equalsIgnoreCase("0") || row_updated.equalsIgnoreCase("0"))
						{
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "sQry sQry sQry inside for insert--> "+sQry);
						if (!sQry.equalsIgnoreCase("")){
							strInputXml =	ExecuteQuery_APSelect(sQry,cabinetName,sessionId);
							try 
							{
								strOutputXml = makeCall(wrapperIP, Short.valueOf(wrapperPort), strInputXml);
								LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: strOutputXml ExecuteQuery_APSelect: "+strOutputXml);
							} 
							catch (NGException e) 
							{
								e.printStackTrace();
							} 
							catch (Exception ex) 
							{
								ex.printStackTrace();
							}
							
						
							
							mainCode = (strOutputXml.contains("<MainCode>")) ? strOutputXml.substring(strOutputXml.indexOf("<MainCode>")+"</MainCode>".length()-1,strOutputXml.indexOf("</MainCode>")):"";
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagValue select mainCode --> "+mainCode);
						
							selectdata=(strOutputXml.contains("<selectdata>")) ? strOutputXml.substring(strOutputXml.indexOf("<selectdata>")+"</selectdata>".length()-1,strOutputXml.indexOf("</selectdata>")):"";
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagValue select selectdata --> "+selectdata);
							}
							
							
							if (!companyUpdateQuery.equalsIgnoreCase("")){
							strInputXml =	ExecuteQuery_APSelect(companyUpdateQuery,cabinetName,sessionId);
							try 
							{
								strOutputXml = makeCall(wrapperIP, Short.valueOf(wrapperPort), strInputXml);
								LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: strOutputXml ExecuteQuery_APSelect: "+strOutputXml);
							} 
							catch (NGException e) 
							{
								e.printStackTrace();
							} 
							catch (Exception ex) 
							{
								ex.printStackTrace();
							}
							
						
							
							mainCode = (strOutputXml.contains("<MainCode>")) ? strOutputXml.substring(strOutputXml.indexOf("<MainCode>")+"</MainCode>".length()-1,strOutputXml.indexOf("</MainCode>")):"";
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagValue select mainCode --> "+mainCode);
						
							companiestobeUpdated=(strOutputXml.contains("<selectdata>")) ? strOutputXml.substring(strOutputXml.indexOf("<selectdata>")+"</selectdata>".length()-1,strOutputXml.indexOf("</selectdata>")):"";
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagValue select companiestobeUpdated --> "+companiestobeUpdated);
							
							if(Integer.parseInt(companiestobeUpdated)>0){
								sWhere="Wi_Name='"+wi_name+"' AND CardEmbossNum = '"+entry.getKey()+"' And Liability_type ='Corporate_CIF'";
								strInputXml =	ExecuteQuery_APUpdate(sTableName,columnName,columnValues,sWhere,cabinetName,sessionId);
								LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "strInputXml update " + strInputXml);
							  try 
								{
									strOutputXml = makeCall(wrapperIP, Short.valueOf(wrapperPort), strInputXml);
									
									LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: strOutputXml update: "+strOutputXml);
								} 
								catch (NGException e) 
								{
								   e.printStackTrace();
								} 
								catch (Exception ex) 
								{
								   ex.printStackTrace();
								}
								
								tagNameU = "APUpdate_Output";
								subTagNameU = "MainCode";
								subTagNameU_2 = "Output";
								mainCode = getTagValue(strOutputXml,tagNameU,subTagNameU);
								//row_updated = getTagValue(strOutputXml,tagNameU,subTagNameU_2);
								//LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagValue select mainCode for update query for cif"+cifId+"--> "+mainCode);
								//LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagValue select rowUpdated for company for update query for cif"+cifId+" --> "+row_updated);
								stopIndividualToInsert = true;
							}
							
							
							}
							
							
							
							if(sQry.equalsIgnoreCase("") ||(mainCode.equalsIgnoreCase("0") && selectdata.equalsIgnoreCase("0") && !stopIndividualToInsert)){
						
							  strInputXml =	ExecuteQuery_APInsert(sTableName,columnName,columnValues,cabinetName,sessionId);
							  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "strInputXml" + strInputXml);
							  try 
								{
									strOutputXml = makeCall(wrapperIP, Short.valueOf(wrapperPort), strInputXml);
									
									LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: strOutputXml: "+strOutputXml);
									mainCode = getTagValue(strOutputXml,"APInsert_Output",subTagNameU);
									if(!mainCode.equalsIgnoreCase("0"))
									{
										retVal = "false";
										LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: commonparseproduct:false "+retVal);
									}
									else
									{
										retVal = "true";
										LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: commonparseproduct:true "+retVal);
									}
								} 
								catch (NGException e) 
								{
								   e.printStackTrace();
								} 
								catch (Exception ex) 
								{
								   ex.printStackTrace();
								}
						  }
						  //change by saurabh for company if its not able to overwrite data of individual but on frontend it should be success.change on 2nd feb
						  else{
							retVal = "true";
						  }
						  }
						else
						{
							retVal = "true";
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: commonparseproductapupdate:true "+retVal);
						}
			  }
			  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: finalValue: "+retVal);
			  return retVal;
		}
	}
	
	public static String commonParseProduct_collection(String parseXml,String tagName,String wi_name,String returnType,String sTableName,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String appServerType, String subTagName,String prod,String subprod, String cifId,String subtag_single,String cust_type)
	{
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Inside commonParseProduct_collection for CifId: "+cifId);
		if(!parseXml.contains(tagName)){
			return "true";
		}
		else
		{ 
		
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "commonParse jsp: inside: ");
			String retVal = "";
			String [] valueArr= null;
			String strInputXml="";
			String strOutputXml="";
			String columnName = "";
			String columnValues = "";
			String tagNameU = "";
			String subTagNameU = "";
			String subTagNameU_2 = "";
			String mainCode = "";
			String sWhere = "";
			String row_updated = "";
			String 	sQry="";
			String selectdata="";
			String companyUpdateQuery="";
			String companiestobeUpdated = "";
			boolean stopIndividualToInsert = false;
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "tagName jsp: commonParse: "+tagName);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "subTagName jsp: commonParse: "+subTagName);
			
			
			 Map<String, String> tagValuesMap= new LinkedHashMap<String, String>();		 
			 tagValuesMap=getTagDataParent_deep(parseXml,tagName,subTagName, subtag_single);
			
			 Map<String, String> map = tagValuesMap;
		  String colValue="";
			  for (Map.Entry<String, String> entry : map.entrySet())
			  {
				  valueArr=entry.getValue().split("~");
				  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "tag values" + entry.getValue());
				  
				  
				  colValue = "'"+valueArr[1].replaceAll("[,]", "','")+"'";
				  columnName = valueArr[0]+",Wi_Name,Request_Type,Product_Type,CardType,CifId";
				  columnValues = valueArr[1]+",'"+wi_name+"','"+returnType+"','"+prod+"','"+subprod+"','"+cifId+"'";
				  
				   
				   
				  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "columnName commonParse" + columnName);
				  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "columnValues commonParse" + columnValues);
				  if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_CardDetails")){
					  
					  columnName = valueArr[0]+",Request_Type,CifId,Wi_Name,Liability_type";
					  columnName = columnName.replaceAll("Card_approve_date","ApplicationCreationDate");
					  columnName = columnName.replaceAll("Outstanding_balance","OutstandingAmt");
					  columnName = columnName.replaceAll("Credit_limit","CreditLimit");
					  columnName = columnName.replaceAll("Overdue_amount","OverdueAmt");
					  columnName = columnName.replaceAll("GeneralStatus","General_Status");
					  columnValues = valueArr[1]+",'"+returnType+"','"+cifId+"','"+wi_name+"','"+cust_type+"'";
					   sWhere="Wi_Name='"+wi_name+"' AND CardEmbossNum = '"+entry.getKey()+"' And Liability_type ='"+cust_type+"'";
					   //change by saurabh below cust_type on 2nd Feb
			     	sQry="Select count(Wi_Name) as selectdata from "+sTableName+" with(nolock) where Wi_Name='"+wi_name+"' And CardEmbossNum = '"+entry.getKey()+"' And Liability_type ='Individual_CIF'";
					  if(cust_type.equalsIgnoreCase("Individual_CIF")) {
							companyUpdateQuery="Select count(Wi_Name) as selectdata from "+sTableName+" with(nolock) where Wi_Name='"+wi_name+"' And CardEmbossNum = '"+entry.getKey()+"' And Liability_type ='Corporate_CIF'";
						}
				  }
				  else if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_LoanDetails")){
					  columnName = valueArr[0]+",Wi_Name,Request_Type,Product_Type,CardType,CifId,Liability_type";
					  columnValues = valueArr[1]+",'"+wi_name+"','"+returnType+"','"+prod+"','"+subprod+"','"+cifId+"','"+cust_type+"'";
					  columnName = columnName.replaceAll("OutstandingAmt","TotalOutstandingAmt");
					  columnName = columnName.replaceAll("GeneralStatus","General_Status");
					  columnName = columnName.replaceAll("Loan_close_date","LoanMaturityDate");
					  sWhere="Wi_Name='"+wi_name+"' AND AgreementId = '"+entry.getKey()+"' And Liability_type ='"+cust_type+"'";
					  //change by saurabh on 2nd Feb cust_type  
					  sQry="Select count(Wi_Name) as selectdata from "+sTableName+" with(nolock) where Wi_Name='"+wi_name+"' And AgreementId = '"+entry.getKey()+"' And Liability_type ='Individual_CIF'";
					  if(cust_type.equalsIgnoreCase("Individual_CIF")) {
							companyUpdateQuery="Select count(Wi_Name) as selectdata from "+sTableName+" with(nolock) where Wi_Name='"+wi_name+"' And AgreementId = '"+entry.getKey()+"' And Liability_type ='Corporate_CIF'";
						}

				  }
			  //Changes Done by aman to save the data of collection summary
		/*			  else if(sTableName.equalsIgnoreCase("NG_rlos_custexpose_Derived")){
						  columnName = valueArr[0]+",Wi_Name,Request_Type,CifId,FullNm,TotalOutstanding,TotalOverdue,NoOfContracts";
						  columnValues = valueArr[1]+",'"+wi_name+"','"+returnType+"','"+cifId+"','"+FullNm+"','"+TotalOutstanding+"','"+TotalOverdue+"','"+NoOfContracts+"'";
						  sWhere="Wi_Name='"+wi_name+"' AND Total_Exposure = '"+entry.getKey()+"'";
				  }
		*/		  
				    //Changes Done by aman to save the data of collection summary
				  else if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_AcctDetails")){
					 String CreditGrade = (parseXml.contains("<CreditGrade>")) ? parseXml.substring(parseXml.indexOf("<CreditGrade>")+"</CreditGrade>".length()-1,parseXml.indexOf("</CreditGrade>")):"";
						  columnName = valueArr[0]+",Wi_Name,Request_Type,CifId,CreditGrade";
						  columnValues = valueArr[1]+",'"+wi_name+"','"+returnType+"','"+cifId+"','"+CreditGrade+"'";
						  sWhere="Wi_Name='"+wi_name+"' AND AcctId = '"+entry.getKey()+"'";
						  sQry="Select count(Wi_Name) as selectdata from "+sTableName+" with(nolock) where Wi_Name='"+wi_name+"' And AcctId = '"+entry.getKey()+"' And Account_Type ='Individual_CIF'";
				  }
				else{
				   sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"'";
				  }
				  
				  strInputXml =	ExecuteQuery_APUpdate(sTableName,columnName,columnValues,sWhere,cabinetName,sessionId);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "strInputXml update " + strInputXml);
					  try 
						{
							strOutputXml = makeCall(wrapperIP, Short.valueOf(wrapperPort), strInputXml);
							
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: strOutputXml update: "+strOutputXml);
						} 
						catch (NGException e) 
						{
						   e.printStackTrace();
						} 
						catch (Exception ex) 
						{
						   ex.printStackTrace();
						}
						
						tagNameU = "APUpdate_Output";
						subTagNameU = "MainCode";
						subTagNameU_2 = "Output";
						mainCode = getTagValue(strOutputXml,tagNameU,subTagNameU);
						row_updated = getTagValue(strOutputXml,tagNameU,subTagNameU_2);
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagValue select mainCode --> "+mainCode);
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagValue select mainCode --> "+row_updated);
						if(!mainCode.equalsIgnoreCase("0") || row_updated.equalsIgnoreCase("0"))
						{
						
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "sQry sQry sQry --> "+sQry);
						if (!sQry.equalsIgnoreCase("")){
							strInputXml =	ExecuteQuery_APSelect(sQry,cabinetName,sessionId);
							try 
							{
								strOutputXml = makeCall(wrapperIP, Short.valueOf(wrapperPort), strInputXml);
								LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: strOutputXml ExecuteQuery_APSelect: "+strOutputXml);
							} 
							catch (NGException e) 
							{
								e.printStackTrace();
							} 
							catch (Exception ex) 
							{
								ex.printStackTrace();
							}
							
						
							
							mainCode = (strOutputXml.contains("<MainCode>")) ? strOutputXml.substring(strOutputXml.indexOf("<MainCode>")+"</MainCode>".length()-1,strOutputXml.indexOf("</MainCode>")):"";
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagValue select mainCode --> "+mainCode);
						
							selectdata=(strOutputXml.contains("<selectdata>")) ? strOutputXml.substring(strOutputXml.indexOf("<selectdata>")+"</selectdata>".length()-1,strOutputXml.indexOf("</selectdata>")):"";
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagValue select selectdata --> "+selectdata);
							}
							
							if (!companyUpdateQuery.equalsIgnoreCase("")){
							strInputXml =	ExecuteQuery_APSelect(companyUpdateQuery,cabinetName,sessionId);
							try 
							{
								strOutputXml = makeCall(wrapperIP, Short.valueOf(wrapperPort), strInputXml);
								LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: strOutputXml ExecuteQuery_APSelect: "+strOutputXml);
							} 
							catch (NGException e) 
							{
								e.printStackTrace();
							} 
							catch (Exception ex) 
							{
								ex.printStackTrace();
							}
							
						
							
							mainCode = (strOutputXml.contains("<MainCode>")) ? strOutputXml.substring(strOutputXml.indexOf("<MainCode>")+"</MainCode>".length()-1,strOutputXml.indexOf("</MainCode>")):"";
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagValue select mainCode --> "+mainCode);
						
							companiestobeUpdated=(strOutputXml.contains("<selectdata>")) ? strOutputXml.substring(strOutputXml.indexOf("<selectdata>")+"</selectdata>".length()-1,strOutputXml.indexOf("</selectdata>")):"";
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagValue select companiestobeUpdated --> "+companiestobeUpdated);
							
							if(Integer.parseInt(companiestobeUpdated)>0){
								if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_CardDetails")){
								sWhere="Wi_Name='"+wi_name+"' AND CardEmbossNum = '"+entry.getKey()+"' And Liability_type ='Corporate_CIF'";
								}
								else if(sTableName.equalsIgnoreCase("ng_RLOS_CUSTEXPOSE_LoanDetails")){
								sWhere="Wi_Name='"+wi_name+"' AND AgreementId = '"+entry.getKey()+"' And Liability_type ='Corporate_CIF'";
								}
								strInputXml =	ExecuteQuery_APUpdate(sTableName,columnName,columnValues,sWhere,cabinetName,sessionId);
								LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "strInputXml update " + strInputXml);
							  try 
								{
									strOutputXml = makeCall(wrapperIP, Short.valueOf(wrapperPort), strInputXml);
									
									LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: strOutputXml update: "+strOutputXml);
								} 
								catch (NGException e) 
								{
								   e.printStackTrace();
								} 
								catch (Exception ex) 
								{
								   ex.printStackTrace();
								}
								
								tagNameU = "APUpdate_Output";
								subTagNameU = "MainCode";
								subTagNameU_2 = "Output";
								mainCode = getTagValue(strOutputXml,tagNameU,subTagNameU);
								//row_updated = getTagValue(strOutputXml,tagNameU,subTagNameU_2);
								//LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagValue select mainCode for update query for cif"+cifId+"--> "+mainCode);
								//LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagValue select rowUpdated for company for update query for cif"+cifId+" --> "+row_updated);
								stopIndividualToInsert = true;
							}
							
							
							}
							
							if(sQry.equalsIgnoreCase("") || (mainCode.equalsIgnoreCase("0") && selectdata.equalsIgnoreCase("0") && !stopIndividualToInsert)){
						
							  strInputXml =	ExecuteQuery_APInsert(sTableName,columnName,columnValues,cabinetName,sessionId);
							  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "strInputXml" + strInputXml);
							  try 
								{
									strOutputXml = makeCall(wrapperIP, Short.valueOf(wrapperPort), strInputXml);
									
									LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: strOutputXml: "+strOutputXml);
									mainCode = getTagValue(strOutputXml,"APInsert_Output",subTagNameU);
									if(!mainCode.equalsIgnoreCase("0"))
									{
										retVal = "false";
											LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: ApINsertfalse for collection summary: "+retVal);
									}
									else
									{
										retVal = "true";
											LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: ApINserttrue for collection summary: "+retVal);
									}
								} 
								catch (NGException e) 
								{
								   e.printStackTrace();
								} 
								catch (Exception ex) 
								{
								   ex.printStackTrace();
								}
						  }
						  //change by saurabh for company call if its not able to overwrite individual data but call was successful so at frontend it should be successfull. Change on 2nd feb.
						  else{
							retVal = "true";
						  }
						 } 
					else
						{
							retVal = "true";
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: ApUpdatetrue for collection summary: "+retVal);
						}
			  }
			   LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: final value for collection summary: "+retVal);
			   return retVal;
		}  
	}
	
	public static String commonParseParent(String parseXml,String tagName,String wi_name,String returnType,String sTableName,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String appServerType, String sParentTagName, String subTagName, String cifId)
	{
		String retVal="";
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "commonParseParent jsp: inside: ");
		String [] valueArr= null;
		String strInputXml="";
		String strOutputXml="";
		String columnName = "";
		String columnValues = "";
		String sWhere = "";
		String tagNameU = "";
		String subTagNameU = "";
		String subTagNameU_2 = "";
		String mainCode = "";
		String row_updated = "";
		
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "tagName jsp: commonParseParent: "+tagName);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "subTagName jsp: commonParseParent: "+subTagName);
		
		 Map<Integer, String> tagValuesMap= new LinkedHashMap<Integer, String>();
		 tagValuesMap=getTagDataParent(parseXml,tagName,subTagName);
		
		 Map<Integer, String> map = tagValuesMap;
	  String colValue ="";
		  for (Map.Entry<Integer, String> entry : map.entrySet())
		  {
			  valueArr=entry.getValue().split("~");
			  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "tag values" + entry.getValue());
			  
			  colValue = "'"+valueArr[1].replaceAll("[,]", "','")+"'";
			  
			  columnName = valueArr[0]+","+sParentTagName+",Wi_Name,Request_Type,CifId";
			  columnValues = colValue+",'"+valueArr[2]+"','"+wi_name+"','"+returnType+"','"+cifId+"'";
			  
			  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "columnName commonParseParent" + columnName);
			  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "columnValues commonParseParent" + columnValues);
			  sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"'";
			  
			  strInputXml =	ExecuteQuery_APUpdate(sTableName,columnName,columnValues,sWhere,cabinetName,sessionId);
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "strInputXml update " + strInputXml);
				  try 
					{
						strOutputXml = makeCall(wrapperIP, Short.valueOf(wrapperPort), strInputXml);
						
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: strOutputXml update: "+strOutputXml);
					} 
					catch (NGException e) 
					{
					   e.printStackTrace();
					} 
					catch (Exception ex) 
					{
					   ex.printStackTrace();
					}
					
					tagNameU = "APUpdate_Output";
					subTagNameU = "MainCode";
					subTagNameU_2 = "Output";
					mainCode = getTagValue(strOutputXml,tagNameU,subTagNameU);
					row_updated = getTagValue(strOutputXml,tagNameU,subTagNameU_2);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagValue select mainCode --> "+mainCode);
					if(!mainCode.equalsIgnoreCase("0") || row_updated.equalsIgnoreCase("0"))
					{	
					  strInputXml =	ExecuteQuery_APInsert(sTableName,columnName,columnValues,cabinetName,sessionId);
					  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "strInputXml" + strInputXml);
					  try 
						{
							strOutputXml = makeCall(wrapperIP, Short.valueOf(wrapperPort), strInputXml);
							
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: strOutputXml: "+strOutputXml);
							mainCode = getTagValue(strOutputXml,"APInsert_Output",subTagNameU);
							if(!mainCode.equalsIgnoreCase("0"))
							{
								retVal = "false";
								LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: commonparseparentinsert:false "+retVal);
							}					
							else
							{
								retVal = "true";
								LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: commonparseparentinsert:true "+retVal);
							}
						} 
						catch (NGException e) 
						{
						   e.printStackTrace();
						} 
						catch (Exception ex) 
						{
						   ex.printStackTrace();
						}
					}
					else
					{
						retVal = "true";
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: commonparseparentupdate:true "+retVal);
					}
		  }
		   LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: final value of commonParseParent "+retVal);
		  return retVal;
		 
	}
	
	public static Map<Integer, String> getTagDataParent(String parseXml,String tagName,String subTagName){
	  
	  Map<Integer, String> tagValuesMap= new LinkedHashMap<Integer, String>(); 
	   InputStream is = new ByteArrayInputStream(parseXml.getBytes());
	  try {
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagDataParent jsp: parseXml: "+parseXml);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagDataParent jsp: tagName: "+tagName);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagDataParent jsp: subTagName: "+subTagName);
		    //InputStream is = new FileInputStream(parseXml);
			
		    LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagDataParent jsp: strOutputXml: "+is);
		  	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();
			
			NodeList nList = doc.getElementsByTagName(tagName);
			
			String[] values =subTagName.split(",");
			String value="";
			String subTagDerivedvalue="";
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					Node uNode=eElement.getParentNode();
					
					for(int j=0;j<values.length;j++){
						if(eElement.getElementsByTagName(values[j]).item(0) !=null){
							value=value+","+eElement.getElementsByTagName(values[j]).item(0).getTextContent();
							subTagDerivedvalue=subTagDerivedvalue+","+values[j];
						}
						
					}
					value=value.substring(1,value.length());
					subTagDerivedvalue=subTagDerivedvalue.substring(1,subTagDerivedvalue.length());
					
					Node nNode_c = doc.getElementsByTagName(uNode.getNodeName()).item(temp);
					Element eElement_agg = (Element) nNode_c;
					String id_val = "";
					if(uNode.getNodeName().equalsIgnoreCase("LoanDetails")){
						id_val = eElement_agg.getElementsByTagName("AgreementId").item(0).getTextContent();
					}
					else if(uNode.getNodeName().equalsIgnoreCase("CardDetails")){
						id_val = eElement_agg.getElementsByTagName("CardEmbossNum").item(0).getTextContent();
					}
					else if(uNode.getNodeName().equalsIgnoreCase("AcctDetails")){
						id_val = eElement_agg.getElementsByTagName("AcctId").item(0).getTextContent();
					}
					else{
						id_val="";
					}
					
					tagValuesMap.put(temp+1, subTagDerivedvalue+"~"+value+"~"+uNode.getNodeName()+"~"+id_val);
					value="";
					subTagDerivedvalue="";
				}
			}
			
		    } catch (Exception e) {
		    	System.out.println("Exception occured in getTagDataParent method: "+ e.getMessage());
				e.printStackTrace();
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Exception occured in getTagDataParent method:  "+e.getMessage());
		    }
				finally
			{
				try{
			    		if(is!=null)
			    		{
			    		is.close();
			    		is=null;
			    		}
			    	}
			    	catch(Exception e){
			    		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Exception occured in is close:  "+e.getMessage());
			    	}
			}
		    return tagValuesMap;
  }
  
  public static void commonParseParentKey(String parseXml,String tagName,String wi_name,String returnType,String sTableName,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String appServerType, String sParentTagName, String subTagName, String cifId)
	{
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "commonParseParent jsp: inside: ");
		String [] valueArr= null;
		String sWhere = "";
		String columnName ="";
		String colValue ="";
		String keyDtType = "";
		String [] colArr= null;
		String detailType= null;
		String id_val = "";
		 
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "tagName jsp: commonParseParent: "+tagName);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "subTagName jsp: commonParseParent: "+subTagName);
		
		 Map<Integer, String> tagValuesMap= new LinkedHashMap<Integer, String>();
		 tagValuesMap=getTagDataParent(parseXml,tagName,subTagName);
		
		 Map<Integer, String> map = tagValuesMap;
	 
		  for (Map.Entry<Integer, String> entry : map.entrySet())
		  {
			  valueArr=entry.getValue().split("~");
			  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "tag values" + entry.getValue());
			  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2]" + valueArr[2]);
			  
			  detailType = valueArr[2];
			  id_val = valueArr[3];
			  
			 colArr = valueArr[1].split(",");
			 LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colArr[0]" + colArr[0]);
			 keyDtType = colArr[0];
			
			if(detailType.equalsIgnoreCase("LoanDetails"))
			{
				sTableName="ng_RLOS_CUSTEXPOSE_LoanDetails";
				if(returnType.equalsIgnoreCase("InternalExposure"))
				{
				  if(keyDtType.equalsIgnoreCase("LoanApprovedDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];					
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("LoanMaturityDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				}
				if(returnType.equalsIgnoreCase("ExternalExposure"))
				{
				  if(keyDtType.equalsIgnoreCase("LoanApprovedDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("LoanMaturityDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				    if(keyDtType.equalsIgnoreCase("MaxOverdueAmountDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				}
				if(returnType.equalsIgnoreCase("CollectionsSummary"))
				{
				  if(keyDtType.equalsIgnoreCase("LoanApprovedDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("LoanMaturityDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  
				}
			 }
			if(detailType.equalsIgnoreCase("CardDetails"))
			{
				sTableName="ng_RLOS_CUSTEXPOSE_CardDetails";
				if(returnType.equalsIgnoreCase("InternalExposure"))
				{
				  if(keyDtType.equalsIgnoreCase("ApplicationCreationDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND CardEmbossNum = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("ExpiryDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND CardEmbossNum = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				}
				if(returnType.equalsIgnoreCase("ExternalExposure"))
				{
				  if(keyDtType.equalsIgnoreCase("MaxOverDueAmountDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND CardEmbossNum = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				}
			 }
			if(detailType.equalsIgnoreCase("AcctDetails"))
			{
				sTableName="ng_RLOS_CUSTEXPOSE_AcctDetails";
				if(returnType.equalsIgnoreCase("InternalExposure"))
				{
				  if(keyDtType.equalsIgnoreCase("AccountOpenDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AcctId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("LimitSactionDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AcctId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("LimitExpiryDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AcctId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				}
				if(returnType.equalsIgnoreCase("ExternalExposure"))
				{
				  if(keyDtType.equalsIgnoreCase("StartDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AcctId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("ClosedDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AcctId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("MaxOverdueAmtDate"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AcctId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				}
			 }
			 if(detailType.equalsIgnoreCase("ServicesDetails"))
			{
				sTableName="NG_RLOS_CUSTEXPOSE_ServicesDetails";
				if(returnType.equalsIgnoreCase("ExternalExposure"))
				{
				  if(keyDtType.equalsIgnoreCase("SubscriptionDt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("SvcExpDt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
					
				  }
				}
				
			 }		  			  
			  
		  }
	}
	
	public static void commonParseParentAmt(String parseXml,String tagName,String wi_name,String returnType,String sTableName,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String appServerType, String sParentTagName, String subTagName, String cifId)
	{
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "commonParseParent jsp: inside: ");
		String [] valueArr= null;
		String sWhere = "";
		String columnName ="";
		String colValue ="";
		String keyDtType = "";
		String [] colArr= null;
		String detailType= null;
		String id_val = ""; 
		 
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "tagName jsp: commonParseParent: "+tagName);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "subTagName jsp: commonParseParent: "+subTagName);
		
		 Map<Integer, String> tagValuesMap= new LinkedHashMap<Integer, String>();
		 tagValuesMap=getTagDataParent(parseXml,tagName,subTagName);
		
		 Map<Integer, String> map = tagValuesMap;
	 
		  for (Map.Entry<Integer, String> entry : map.entrySet())
		  {
			  valueArr=entry.getValue().split("~");
			  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "tag values" + entry.getValue());
			  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2]" + valueArr[2]);
			  
			  detailType = valueArr[2];
			  id_val = valueArr[3];

			 colArr = valueArr[1].split(",");
			 LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colArr[0]" + colArr[0]);
			 keyDtType = colArr[0];
			
			if(detailType.equalsIgnoreCase("LoanDetails"))
			{
				sTableName="ng_RLOS_CUSTEXPOSE_LoanDetails";
				if(returnType.equalsIgnoreCase("InternalExposure"))
				{
				  if(keyDtType.equalsIgnoreCase("TotalLoanAmount"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];					
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("LoanDisbursementAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("TotalOutstandingAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("OverdueAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("MortgagesValue"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("NextInstallmentAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				}
				if(returnType.equalsIgnoreCase("ExternalExposure"))
				{
				  if(keyDtType.equalsIgnoreCase("TotalAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("OutstandingAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				    if(keyDtType.equalsIgnoreCase("PaymentsAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				     if(keyDtType.equalsIgnoreCase("OverdueAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				     if(keyDtType.equalsIgnoreCase("MaximumOvrdueAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				}
				if(returnType.equalsIgnoreCase("CollectionsSummary"))
				{
				  if(keyDtType.equalsIgnoreCase("OutstandingAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("ProductAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("NextInstallmentAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("OverdueAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AgreementId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  
				}
			 }
			if(detailType.equalsIgnoreCase("CardDetails"))
			{
				sTableName="ng_RLOS_CUSTEXPOSE_CardDetails";
				if(returnType.equalsIgnoreCase("InternalExposure"))
				{
				  if(keyDtType.equalsIgnoreCase("CreditLimit"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND CardEmbossNum = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("OutstandingAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND CardEmbossNum = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("OverdueAmt"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND CardEmbossNum = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("CurrMaxUtil"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND CardEmbossNum = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				}
				if(returnType.equalsIgnoreCase("ExternalExposure"))
				{
				  if(keyDtType.equalsIgnoreCase("CurrentBalance"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND CardEmbossNum = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("CashLimit"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND CardEmbossNum = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("OverdueAmount"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND CardEmbossNum = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("MaxOverdueAmount"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND CardEmbossNum = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				}
			 }
			if(detailType.equalsIgnoreCase("AcctDetails"))
			{
				sTableName="ng_RLOS_CUSTEXPOSE_AcctDetails";
				if(returnType.equalsIgnoreCase("InternalExposure"))
				{
				  if(keyDtType.equalsIgnoreCase("AvailableBalance"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AcctId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("ClearBalanceAmount"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AcctId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("LedgerBalance"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AcctId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("LoanDisbursementAmount"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AcctId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("OverdueAmount"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AcctId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("EffectiveAvailableBalance"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AcctId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("CumulativeDebitAmount"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AcctId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				   if(keyDtType.equalsIgnoreCase("CurOutstandingAmount"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AcctId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				}
				if(returnType.equalsIgnoreCase("ExternalExposure"))
				{
				  if(keyDtType.equalsIgnoreCase("CashLimit"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AcctId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("OverdueAmount"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AcctId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("MaxOverdueAmount"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"' AND AcctId = "+id_val;
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				}
			 }
			 if(detailType.equalsIgnoreCase("ServicesDetails"))
			{
				sTableName="NG_RLOS_CUSTEXPOSE_ServicesDetails";
				if(returnType.equalsIgnoreCase("ExternalExposure"))
				{
				  if(keyDtType.equalsIgnoreCase("OverDue"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				 
				}
				
			 }		  			  
			  
		  }
	}
	
	public static void commonParseParentDelinquency(String parseXml,String tagName,String wi_name,String returnType,String sTableName,String wrapperIP,String wrapperPort,String sessionId,String cabinetName,String appServerType, String sParentTagName, String subTagName, String cifId)
	{
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "commonParseParent jsp: inside: ");
		String [] valueArr= null;
		String sWhere = "";
		String columnName ="";
		String colValue ="";
		String keyDtType = "";
		String [] colArr= null;
		String detailType= null;
		 
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "tagName jsp: commonParseParent: "+tagName);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "subTagName jsp: commonParseParent: "+subTagName);
		
		 Map<Integer, String> tagValuesMap= new LinkedHashMap<Integer, String>();
		 tagValuesMap=getTagDataParent(parseXml,tagName,subTagName);
		
		 Map<Integer, String> map = tagValuesMap;
	 
		  for (Map.Entry<Integer, String> entry : map.entrySet())
		  {
			  valueArr=entry.getValue().split("~");
			  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "tag values" + entry.getValue());
			  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2]" + valueArr[2]);
			  
			  detailType = valueArr[2];
			  
			 colArr = valueArr[1].split(",");
			 LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colArr[0]" + colArr[0]);
			 keyDtType = colArr[0];
			
			if(detailType.equalsIgnoreCase("CardDetails"))
			{
				sTableName="ng_RLOS_CUSTEXPOSE_CardDetails";
				if(returnType.equalsIgnoreCase("ExternalExposure"))
				{
				  if(keyDtType.equalsIgnoreCase("DPD30Last6Months"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("DPD60Last18Months"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				 
				}
			 }
			if(detailType.equalsIgnoreCase("AcctDetails"))
			{
				sTableName="ng_RLOS_CUSTEXPOSE_AcctDetails";
				if(returnType.equalsIgnoreCase("InternalExposure"))
				{
				  if(keyDtType.equalsIgnoreCase("DaysPastDue"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				 
				}
				if(returnType.equalsIgnoreCase("ExternalExposure"))
				{
				  if(keyDtType.equalsIgnoreCase("DPD30Last6Months"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				  if(keyDtType.equalsIgnoreCase("DPD60Last18Months"))
				  {
					sWhere="Wi_Name='"+wi_name+"' AND Request_Type='"+returnType+"'";
					columnName = colArr[0];
					colValue = "'"+colArr[1]+"'";
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "valueArr[2] sWhere" + sWhere);
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "colValue[2]" + colValue);
					updateQuery(sTableName,columnName,colValue,sWhere,cabinetName,sessionId,returnType,wrapperIP, wrapperPort, appServerType,cifId,wi_name);
				  }
				
				}
			 }					  			  
			  
		  }
	}
  
  public static String ExecuteQuery_APInsert(String tableName,String columnName,String strValues, String cabinetName, String sessionId)
    {
	//changes done by saurabh on 30th aug. removed cabinet name. Temporary change to stop inserting into db.
    String sInputXML = "<?xml version=\"1.0\"?>" +"\n"+
            "<APInsert_Input>" +"\n"+
			"<Option>APInsert</Option>" +"\n"+
			"<TableName>" + tableName + "</TableName>" +"\n"+
			"<ColName>" + columnName + "</ColName>" +"\n"+
			"<Values>" + strValues + "</Values>" +"\n"+
			"<EngineName>" + cabinetName + "</EngineName>" +"\n"+
			"<SessionId>" + sessionId + "</SessionId>" +"\n"+
            "</APInsert_Input>";
    return sInputXML;	
}

 public static String ExecuteQuery_APdelete(String tableName,String sWhere, String cabinetName, String sessionId)
    {
  String sInputXML = "<?xml version=\"1.0\"?>"+
					"<APDelete_Input><Option>APDelete</Option>"+
								"<TableName>"+tableName+"</TableName>"+
								"<WhereClause>"+sWhere+"</WhereClause>"+
								"<EngineName>"+cabinetName+"</EngineName>"+
								"<SessionId>"+sessionId+"</SessionId>"+
							"</APDelete_Input>";
    return sInputXML;	
}
public static String ExecuteQuery_APUpdate(String tableName,String columnName,String strValues,String sWhere, String cabinetName, String sessionId)
    {
   String sInputXML = "<?xml version=\"1.0\"?>"+
								"<APUpdate_Input><Option>APUpdate</Option>"+
								"<TableName>"+tableName+"</TableName>"+
								"<ColName>"+columnName+"</ColName>"+
								"<Values>"+strValues+"</Values>"+
								"<WhereClause>"+sWhere+"</WhereClause>"+
								"<EngineName>"+cabinetName+"</EngineName>"+
								"<SessionId>"+sessionId+"</SessionId>"+
							"</APUpdate_Input>";
    return sInputXML;	
}

public static String ExecuteQuery_APSelect(String sQry, String cabinetName, String sessionId)
    {
   String sInputXML =  "<?xml version=\"1.0\"?>"+
					   "<APSelectWithColumnNames_Input>"+
					   "<Option>APSelectWithColumnNames</Option>"+
					   "<Query>" + sQry + "</Query>"+
					   "<EngineName>" + cabinetName + "</EngineName>"+
					   "<SessionId>" + sessionId+ "</SessionId>"+
					   "</APSelectWithColumnNames_Input>";
   
  
								
    return sInputXML;	
}



 public static String getTagValue(String parseXml,String tagName,String subTagName)
	{
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagValue jsp: inside: ");
		String [] valueArr= null;
		String mainCodeValue = "";
		
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "tagName jsp: getTagValue: "+tagName);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "subTagName jsp: getTagValue: "+subTagName);
		
		
		 Map<Integer, String> tagValuesMap= new LinkedHashMap<Integer, String>();		 
		 tagValuesMap=getTagDataParent(parseXml,tagName,subTagName);
		
		 Map<Integer, String> map = tagValuesMap;
		String colValue="";
		  for (Map.Entry<Integer, String> entry : map.entrySet())
		  {
			  valueArr=entry.getValue().split("~");
			  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "tag values" + entry.getValue());
			  mainCodeValue = valueArr[1];	
			  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "mainCodeValue" + mainCodeValue);
				
		  }
		  return mainCodeValue;
	}
	
	public static void updateQuery(String sTableName,String columnName,String colValue,String sWhere,String cabinetName,String sessionId,String returnType,String wrapperIP,String wrapperPort, String appServerType,String cifId,String wi_name)
  {
	String strInputXml="";
	String strOutputXml="";
	String mainCode="";
	String tagNameU="";
	String subTagNameU="";
	String subTagNameU_2="";
	String columnValues="";
	String row_updated="";
	
	strInputXml =	ExecuteQuery_APUpdate(sTableName,columnName,colValue,sWhere,cabinetName,sessionId);
	LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "strInputXml update " + strInputXml);
	  try 
		{
			strOutputXml = makeCall(wrapperIP, Short.valueOf(wrapperPort), strInputXml);
			
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: strOutputXml update: "+strOutputXml);
		} 
		catch (NGException e) 
		{
		   e.printStackTrace();
		} 
		catch (Exception ex) 
		{
		   ex.printStackTrace();
		}
		
		tagNameU = "APUpdate_Output";
		subTagNameU = "MainCode";
		subTagNameU_2 = "Output";
		mainCode = getTagValue(strOutputXml,tagNameU,subTagNameU);
		row_updated = getTagValue(strOutputXml,tagNameU,subTagNameU_2);
		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagValue select mainCode --> "+mainCode);
		if(!mainCode.equalsIgnoreCase("0") || row_updated.equalsIgnoreCase("0"))
		{
			//colValue = "'"+valueArr[1].replaceAll("[,]", "','")+"'";
			  
			  columnName = columnName+",Wi_Name,Request_Type,CifId";
			  columnValues = colValue+",'"+wi_name+"','"+returnType+"','"+cifId+"'";
			   
			 strInputXml =	ExecuteQuery_APInsert(sTableName,columnName,columnValues,cabinetName,sessionId);
			  LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,  "strInputXml insert " + strInputXml);
			  try 
				{
					strOutputXml = makeCall(wrapperIP, Short.valueOf(wrapperPort), strInputXml);
					
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "CustExpose_Output jsp: strOutputXml insert: "+strOutputXml);
				} 
				catch (NGException e) 
				{
				   e.printStackTrace();
				} 
				catch (Exception ex) 
				{
				   ex.printStackTrace();
				}
		
		}
  }
	//Deepak code commented method changed with new subtag_single param 23jan2018
	public static Map<String, String> getTagDataParent_deep(String parseXml,String tagName,String sub_tag,String subtag_single){
		  Map<String, String> tagValuesMap= new LinkedHashMap<String, String>(); 
		  InputStream is = new ByteArrayInputStream(parseXml.getBytes());
		  try {
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagDataParent_deep jsp: parseXml: "+parseXml);
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagDataParent_deep jsp: tagName: "+tagName);
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagDataParent_deep jsp: subTagName: "+sub_tag);
				String Operationtype="";
				String id="";
				String tag_notused = "BankId,OperationDesc,TxnSummary,#text";
				if(parseXml.indexOf("<OperationType>")>-1)
					{
						Operationtype= parseXml.substring(parseXml.indexOf("<OperationType>")+15,parseXml.indexOf("</OperationType>"));
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "$$Operationtype "+Operationtype);
					}
				 
			   	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(is);
				doc.getDocumentElement().normalize();
				NodeList nList_loan = doc.getElementsByTagName(tagName);
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagDataParent_deep jsp: nList_loan: "+nList_loan);
				for(int i = 0 ; i<nList_loan.getLength();i++){
					String col_name = "";
					String col_val ="";
					NodeList ch_nodeList = nList_loan.item(i).getChildNodes();
					//LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagDataParent_deep jsp: ch_nodeList: "+ch_nodeList);
					if (Operationtype.equalsIgnoreCase("SALDET")){
						id = ch_nodeList.item(1).getTextContent();
					}
					else if (Operationtype.equalsIgnoreCase("RETURNDET")){
							int id_num = 0;
							for(int ch_len = 0 ;ch_len< ch_nodeList.getLength(); ch_len++){
								if(ch_nodeList.item(ch_len).getNodeName().equalsIgnoreCase("ReturnNumber")){
									id_num = ch_len;
								}
							}
							id = ch_nodeList.item(id_num).getTextContent();
						}
					else {
						id = ch_nodeList.item(0).getTextContent();
					}
						for(int ch_len = 0 ;ch_len< ch_nodeList.getLength(); ch_len++){
							if(sub_tag.contains(ch_nodeList.item(ch_len).getNodeName())){
								NodeList sub_ch_nodeList =  ch_nodeList.item(ch_len).getChildNodes();
								if(!sub_ch_nodeList.item(0).getTextContent().equalsIgnoreCase("#text")){
									if(col_name.equalsIgnoreCase("")){
										col_name = sub_ch_nodeList.item(0).getTextContent();
										col_val = "'"+sub_ch_nodeList.item(1).getTextContent()+"'";
									}
									else if(!col_name.contains(sub_ch_nodeList.item(0).getTextContent())){
										col_name = col_name+","+sub_ch_nodeList.item(0).getTextContent();
										col_val = col_val+",'"+sub_ch_nodeList.item(1).getTextContent()+"'";
									}
								}
							}
							else if(tag_notused.contains(ch_nodeList.item(ch_len).getNodeName())){
								LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "this tag not to be passed: "+ch_nodeList.item(ch_len).getNodeName());
							}
							else if(subtag_single.contains(ch_nodeList.item(ch_len).getNodeName())){
								NodeList sub_ch_nodeList =  ch_nodeList.item(ch_len).getChildNodes();
								if(!sub_ch_nodeList.item(0).getTextContent().equalsIgnoreCase("#text")){
									for(int sub_chd_len=0;sub_chd_len<sub_ch_nodeList.getLength();sub_chd_len++){
										if(col_name.equalsIgnoreCase("")){
											col_name = sub_ch_nodeList.item(sub_chd_len).getNodeName();
											col_val = "'"+sub_ch_nodeList.item(sub_chd_len).getTextContent()+"'";
										}
										else if(!col_name.contains(sub_ch_nodeList.item(0).getTextContent())){
													col_name = col_name+","+sub_ch_nodeList.item(sub_chd_len).getNodeName();
													col_val = col_val+",'"+sub_ch_nodeList.item(sub_chd_len).getTextContent()+"'";
										}
									}
								}
							}
							else{
								if(col_name.equalsIgnoreCase("")){
									//LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "inside else if");
									col_name = ch_nodeList.item(ch_len).getNodeName();
									col_val = "'"+ch_nodeList.item(ch_len).getTextContent()+"'";
								}
								else if(!col_name.contains(ch_nodeList.item(ch_len).getNodeName())){
								LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "inside else else if"+ch_nodeList.item(ch_len).getNodeName());
								LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "inside else else if"+ch_nodeList.item(ch_len).getTextContent());
												col_name = col_name+","+ch_nodeList.item(ch_len).getNodeName();
												LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "inside else col_name"+col_name);
												col_val = col_val+",'"+ch_nodeList.item(ch_len).getTextContent()+"'";
												LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "inside else col_name"+col_val);
											}
							}
						}
						if(!col_name.equalsIgnoreCase(""))
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "inside else col_name "+col_name+" id "+id+" col_val "+col_val);
							tagValuesMap.put(id, col_name+"~"+col_val);	
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "tagValuesMap inside loop"+tagValuesMap);
				}
			    } catch (Exception e) {
					System.out.println("Exception occured in getTagDataParent_deep method: "+ e.getMessage());
					e.printStackTrace();
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Exception occured in getTagDataParent method:  "+e.getMessage());
			    }
					finally
			{
				try{
			    		if(is!=null)
			    		{
			    		is.close();
			    		is=null;
			    		}
			    	}
			    	catch(Exception e){
			    		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Exception occured in is close:  "+e.getMessage());
			    	}
			}
			    return tagValuesMap;
	  }
	//Deepak code commented method changed with new subtag_single param 23jan2018
  	/*public static Map<String, String> getTagDataParent_deep(String parseXml,String tagName,String sub_tag){
	  
	  Map<String, String> tagValuesMap= new LinkedHashMap<String, String>(); 
	  InputStream is = new ByteArrayInputStream(parseXml.getBytes());
	  try {
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagDataParent_deep jsp: parseXml: "+parseXml);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagDataParent_deep jsp: tagName: "+tagName);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagDataParent_deep jsp: subTagName: "+sub_tag);
			String Operationtype="";
			String id="";
			String tag_notused = "BankId,OperationDesc,TxnSummary,#text";
			if(parseXml.indexOf("<OperationType>")>-1)
				{
					Operationtype= parseXml.substring(parseXml.indexOf("<OperationType>")+15,parseXml.indexOf("</OperationType>"));
					LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "$$Operationtype "+Operationtype);
				}
			 
		   	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();
		
			NodeList nList_loan = doc.getElementsByTagName(tagName);
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagDataParent_deep jsp: nList_loan: "+nList_loan);
			for(int i = 0 ; i<nList_loan.getLength();i++){
				String col_name = "";
				String col_val ="";
				NodeList ch_nodeList = nList_loan.item(i).getChildNodes();
				//LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagDataParent_deep jsp: ch_nodeList: "+ch_nodeList);
				if (Operationtype.equalsIgnoreCase("SALDET")){
					id = ch_nodeList.item(1).getTextContent();
				}
				else if (Operationtype.equalsIgnoreCase("RETURNDET")){
						int id_num = 0;
						for(int ch_len = 0 ;ch_len< ch_nodeList.getLength(); ch_len++){
							if(ch_nodeList.item(ch_len).getNodeName().equalsIgnoreCase("ReturnNumber")){
								id_num = ch_len;
							}
						}
						id = ch_nodeList.item(id_num).getTextContent();
					}
				else {
					id = ch_nodeList.item(0).getTextContent();
				}
				//LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagDataParent_deep jsp: id: "+id);
					for(int ch_len = 0 ;ch_len< ch_nodeList.getLength(); ch_len++){
					//LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagDataParent_deep jsp: sub_tag: "+sub_tag);
					//LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagDataParent_deep jsp: ch_nodeList.item(ch_len).getNodeName(): "+ch_nodeList.item(ch_len).getNodeName());
					//LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagDataParent_deep jsp: ch_nodeList.item(ch_len).getNodevalues(): "+ch_nodeList.item(ch_len).getTextContent());
						if(sub_tag.contains(ch_nodeList.item(ch_len).getNodeName())){
							//LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "inside if");
							NodeList sub_ch_nodeList =  ch_nodeList.item(ch_len).getChildNodes();
							if(!sub_ch_nodeList.item(0).getTextContent().equalsIgnoreCase("#text")){
								//LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "inside if if");
								if(col_name.equalsIgnoreCase("")){
								//LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "inside if if if");
								col_name = sub_ch_nodeList.item(0).getTextContent();
								col_val = "'"+sub_ch_nodeList.item(1).getTextContent()+"'";
								}
								else if(!col_name.contains(sub_ch_nodeList.item(0).getTextContent())){
								//LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "inside if if else if");
											col_name = col_name+","+sub_ch_nodeList.item(0).getTextContent();
											col_val = col_val+",'"+sub_ch_nodeList.item(1).getTextContent()+"'";
								}
							}
							
						}
						else if(tag_notused.contains(ch_nodeList.item(ch_len).getNodeName())){
							
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "this tag not to be passed: "+ch_nodeList.item(ch_len).getNodeName());
						}
						else{
							//LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "inside else"+col_name);
							//LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "inside else col val"+col_val);
							
							if(col_name.equalsIgnoreCase("")){
								//LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "inside else if");
								col_name = ch_nodeList.item(ch_len).getNodeName();
								col_val = "'"+ch_nodeList.item(ch_len).getTextContent()+"'";
							}
							else if(!col_name.contains(ch_nodeList.item(ch_len).getNodeName())){
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "inside else else if"+ch_nodeList.item(ch_len).getNodeName());
							LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "inside else else if"+ch_nodeList.item(ch_len).getTextContent());
											col_name = col_name+","+ch_nodeList.item(ch_len).getNodeName();
											LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "inside else col_name"+col_name);
											col_val = col_val+",'"+ch_nodeList.item(ch_len).getTextContent()+"'";
											LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "inside else col_name"+col_val);
										}
							
						}
						
					}
					if(!col_name.equalsIgnoreCase(""))
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "inside else col_name "+col_name+" id "+id+" col_val "+col_val);
						tagValuesMap.put(id, col_name+"~"+col_val);	
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "tagValuesMap inside loop"+tagValuesMap);
						
			}
			
		    } catch (Exception e) {
		    	System.out.println("Exception occured in getTagDataParent_deep method: "+ e.getMessage());
				e.printStackTrace();
			LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Exception occured in getTagDataParent method:  "+e.getMessage());
		    }
				finally
			{
				try{
			    		if(is!=null)
			    		{
			    		is.close();
			    		is=null;
			    		}
			    	}
			    	catch(Exception e){
			    		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Exception occured in is close:  "+e.getMessage());
			    	}
			}
		    return tagValuesMap;
  }*/
  
 public static String getTagDataParent_financ_header(String parseXml,String tagName,String sub_tag){
		String col_name = "";
		String col_val ="";
		InputStream is = new ByteArrayInputStream(parseXml.getBytes());
		  try {
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagDataParent_financ_header jsp: parseXml: "+parseXml);
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagDataParent_financ_header jsp: tagName: "+tagName);
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagDataParent_financ_header jsp: subTagName: "+sub_tag);
								
			    //InputStream is = new FileInputStream(parseXml);
				 
			    LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagDataParent_financ_header jsp: strOutputXml: "+is);
			  	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(is);
				doc.getDocumentElement().normalize();
			
				NodeList nList_loan = doc.getElementsByTagName(tagName);
				for(int i = 0 ; i<nList_loan.getLength();i++){
					
					NodeList ch_nodeList = nList_loan.item(i).getChildNodes();
					String id = ch_nodeList.item(0).getTextContent();
						for(int ch_len = 0 ;ch_len< ch_nodeList.getLength(); ch_len++){
							if(sub_tag.toUpperCase().contains(ch_nodeList.item(ch_len).getNodeName().toUpperCase())){
								if(col_name.equalsIgnoreCase("")){
									col_name = ch_nodeList.item(ch_len).getNodeName();
									col_val = "'"+ch_nodeList.item(ch_len).getTextContent()+"'";
								}
								else{
									col_name = col_name+","+ch_nodeList.item(ch_len).getNodeName();
									col_val = col_val+",'"+ch_nodeList.item(ch_len).getTextContent()+"'";
								}
							}							
						}
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "insert/update getTagDataParent_financ_header for id: "+id);
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "insert/update getTagDataParent_financ_header cal_name: "+col_name);
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "insert/update getTagDataParent_financ_header col_val: "+col_val);
							
				}
				
			    } catch (Exception e) {
			    	
				e.printStackTrace();
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Exception occured in getTagDataParent_financ_header method:  "+e.getMessage());
			    }
				finally
			{
				try{
			    		if(is!=null)
			    		{
			    		is.close();
			    		is=null;
			    		}
			    	}
			    	catch(Exception e){
			    		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Exception occured in is close:  "+e.getMessage());
			    	}
			}
			    return col_name+":"+col_val;
	}
	public static String getTagDataParent_cardInstallment_header(String parseXml,String tagName,String sub_tag){
		String col_name = "";
		String col_val ="";
		InputStream is = new ByteArrayInputStream(parseXml.getBytes());
		  try {
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagDataParent_cardInstallment_header jsp: parseXml: "+parseXml);
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagDataParent_cardInstallment_header jsp: tagName: "+tagName);
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagDataParent_cardInstallment_header jsp: subTagName: "+sub_tag);
								
			    //InputStream is = new FileInputStream(parseXml);
				 
			    LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "getTagDataParent_cardInstallment_header jsp: strOutputXml: "+is);
			  	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(is);
				doc.getDocumentElement().normalize();
			
				NodeList nList_loan = doc.getElementsByTagName(tagName);
				for(int i = 0 ; i<nList_loan.getLength();i++){
					
					NodeList ch_nodeList = nList_loan.item(i).getChildNodes();
					String id = ch_nodeList.item(0).getTextContent();
						for(int ch_len = 0 ;ch_len< ch_nodeList.getLength(); ch_len++){
							if(sub_tag.toUpperCase().contains(ch_nodeList.item(ch_len).getNodeName().toUpperCase())){
								if(col_name.equalsIgnoreCase("")){
									col_name = ch_nodeList.item(ch_len).getNodeName();
									col_val = "'"+ch_nodeList.item(ch_len).getTextContent()+"'";
								}
								else{
									col_name = col_name+","+ch_nodeList.item(ch_len).getNodeName();
									col_val = col_val+",'"+ch_nodeList.item(ch_len).getTextContent()+"'";
								}
							}							
						}
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "insert/update getTagDataParent_cardInstallment_header for id: "+id);
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "insert/update getTagDataParent_cardInstallment_header cal_name: "+col_name);
						LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "insert/update getTagDataParent_cardInstallment_header col_val: "+col_val);
							
				}
				
			    } catch (Exception e) {
			    	System.out.println("Exception occured in getTagDataParent_cardInstallment_header method: "+ e.getMessage());
					e.printStackTrace();
				LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Exception occured in getTagDataParent_cardInstallment_header method:  "+e.getMessage());
			    }
					finally
			{
				try{
			    		if(is!=null)
			    		{
			    		is.close();
			    		is=null;
			    		}
			    	}
			    	catch(Exception e){
			    		LogMe.logMe(LogMe.LOG_LEVEL_DEBUG, "Exception occured in is close:  "+e.getMessage());
			    	}
			}
			    return col_name+":"+col_val;
	}
	
	//with wrapper
	private static String makeCall(String jtsAddress, short jtsPort, String inputXML)
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

	
}
