package com.newgen.omniforms.user;


import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.wfdesktop.xmlapi.WFCallBroker;

public class RLOS_Integration_Output implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static void valueSetCustomer(String outputResponse, String operationName)
	{
		RLOS.mLogger.info( "Inside valueSetCustomer():"+outputResponse);
		String outputXMLHead = "";
		String outputXMLMsg = "";
		String returnDesc = "";
		String returnCode = "";
		String response= "";
		String Call_type="";


		//XMLParser objXMLParser = new XMLParser();
		try
		{
			RLOS.mLogger.info( "Inside try");
			if(outputResponse.indexOf("<EE_EAI_HEADER>")>-1)
			{
				outputXMLHead=outputResponse.substring(outputResponse.indexOf("<EE_EAI_HEADER>"),outputResponse.indexOf("</EE_EAI_HEADER>")+16);
				RLOS.mLogger.info( "outputXMLHead");
			}
			//objXMLParser.setInputXML(outputXMLHead);
			if(outputXMLHead.indexOf("<MsgFormat>")>-1)
			{
				response= outputXMLHead.substring(outputXMLHead.indexOf("<MsgFormat>")+11,outputXMLHead.indexOf("</MsgFormat>"));
				RLOS.mLogger.info(response);
			}
			if(outputXMLHead.indexOf("<ReturnDesc>")>-1)
			{
				returnDesc= outputXMLHead.substring(outputXMLHead.indexOf("<ReturnDesc>")+12,outputXMLHead.indexOf("</ReturnDesc>"));
				RLOS.mLogger.info(returnDesc);
			}
			if(outputXMLHead.indexOf("<ReturnCode>")>-1)
			{
				returnCode= outputXMLHead.substring(outputXMLHead.indexOf("<ReturnCode>")+12,outputXMLHead.indexOf("</ReturnCode>"));
				RLOS.mLogger.info(returnCode);
			}

			if(outputResponse.indexOf("<MQ_RESPONSE_XML>")>-1)
			{
				outputXMLMsg=outputResponse.substring(outputResponse.indexOf("<MQ_RESPONSE_XML>")+17,outputResponse.indexOf("</MQ_RESPONSE_XML>"));
				//RLOS.mLogger.info(outputXMLMsg);
				RLOS.mLogger.info("check inside getOutputXMLValues");
				//getOutputXMLValues(outputXMLMsg,response);
				getOutputXMLValues(outputXMLMsg,response,operationName);
				RLOS.mLogger.info("outputXMLMsg");
			}
			if(outputResponse.indexOf("<CallType>")>-1){
				Call_type= outputResponse.substring(outputResponse.indexOf("<CallType>")+10,outputResponse.indexOf("</CallType>"));
				if	(Call_type!= null){
					if ("CA".equals(Call_type)){
						dectechfromliability(outputResponse);
					}
					if ("PM".equals(Call_type)){
						dectechfromeligbility(outputResponse);
					}
				}

			}
			
			//ended by me
		}
		catch(Exception e)
		{			
			RLOS.mLogger.info("Exception occured in valueSetCustomer method: "+ e.getMessage());
			RLOS.logException(e);
		}
	}

	public static void dectechfromliability(String outputResponse){
		
		String Output_TAI;
		String Output_Existing_DBR ;
		String Output_Net_Salary_DBR;
		
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//Setting the value in ELIGANDPROD info
		try{
			if (outputResponse.contains("Output_TAI")){
			Output_TAI = outputResponse.substring(outputResponse.indexOf("<Output_TAI>")+12,outputResponse.indexOf("</Output_TAI>"));
	    	if (Output_TAI!=null){
	    		formObject.setNGValue("cmplx_Liability_New_TAI", Output_TAI);
	    	}
	    	RLOS.mLogger.info("inside outpute get Output_TAI"+Output_TAI);
			}

			//FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			if (outputResponse.contains("Output_Existing_DBR")){
			 Output_Existing_DBR = outputResponse.substring(outputResponse.indexOf("<Output_Existing_DBR>")+21,outputResponse.indexOf("</Output_Existing_DBR>"));
			 if (Output_Existing_DBR!=null){
		    		formObject.setNGValue("cmplx_Liability_New_DBR", Output_Existing_DBR);
		    	}
			 RLOS.mLogger.info("inside outpute get Output_Existing_DBR"+Output_Existing_DBR);
			
			}
			
			
			if (outputResponse.contains("Output_Net_Salary_DBR")){
				Output_Net_Salary_DBR = outputResponse.substring(outputResponse.indexOf("<Output_Net_Salary_DBR>")+23,outputResponse.indexOf("</Output_Net_Salary_DBR>"));
				 if (Output_Net_Salary_DBR!=null){
			    		formObject.setNGValue("cmplx_Liability_New_DBRNet", Output_Net_Salary_DBR);
			    	}
				RLOS.mLogger.info("inside outpute get Output_Net_Salary_DBR"+Output_Net_Salary_DBR);
				
				}
		}
		catch(Exception e){
			RLOS.mLogger.info( "Exception:"+e.getMessage());
			RLOS.logException(e);
    		
		}
	}
	
	public static void dectechfromeligbility(String outputResponse){
		try{
			if(outputResponse.indexOf("<PM_Reason_Codes>")>-1 && outputResponse.indexOf("<PM_Outputs>")>-1 ){
				String squery="";
				String Output_TAI;
				String Output_Final_DBR;
				String Output_Existing_DBR;
				String Output_Eligible_Amount="";
				String Output_Affordable_EMI="";
				String Output_Delegation_Authority="";
				String Grade="";
				String Output_Interest_Rate;
				String Output_Net_Salary_DBR;
				double cac_calc_limit=0.0;

				FormReference formObject = FormContext.getCurrentInstance().getFormReference();
				String strOutputXml ;
				String sGeneralData = formObject.getWFGeneralData();
				RLOS.mLogger.info("inside outpute get sGeneralData"+sGeneralData);

				String cabinetName = sGeneralData.substring(sGeneralData.indexOf("<EngineName>")+12,sGeneralData.indexOf("</EngineName>")); 
				String sessionId = sGeneralData.substring(sGeneralData.indexOf("<DMSSessionId>")+14,sGeneralData.indexOf("</DMSSessionId>"));
				String jtsIp = sGeneralData.substring(sGeneralData.indexOf("<JTSIP>")+7,sGeneralData.indexOf("</JTSIP>"));
				String jtsPort = sGeneralData.substring(sGeneralData.indexOf("<JTSPORT>")+9,sGeneralData.indexOf("</JTSPORT>"));
				String ReqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
				String subProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2);
				String appType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,4);
				//Setting the value in ELIGANDPROD info
				if (outputResponse.contains("Output_TAI")){
					Output_TAI = outputResponse.substring(outputResponse.indexOf("<Output_TAI>")+12,outputResponse.indexOf("</Output_TAI>"));
					if (Output_TAI!=null)
					{
						formObject.setNGValue("cmplx_Liability_New_TAI", Output_TAI);
						formObject.setNGValue("cmplx_EligibilityAndProductInfo_FinalTAI", Output_TAI);
					}
					RLOS.mLogger.info("inside outpute get Output_TAI"+Output_TAI);
				}
				if (outputResponse.contains("Output_Eligible_Cards"))
				{
					try
					{
						String Output_Eligible_Cards = outputResponse.substring(outputResponse.indexOf("<Output_Eligible_Cards>")+23,outputResponse.indexOf("</Output_Eligible_Cards>"));
						Output_Eligible_Cards=Output_Eligible_Cards.substring(2, Output_Eligible_Cards.length()-2);
						String strInputXML =ExecuteQuery_APdelete("ng_rlos_IGR_Eligibility_CardLimit","WI_NAME ='" + formObject.getWFWorkitemName() + "'",cabinetName,sessionId);
						strOutputXml = WFCallBroker.execute(strInputXML,jtsIp,Integer.parseInt(jtsPort),1);
						
						if("".equals(strOutputXml))
							RLOS.mLogger.info("strOutputXml is empty...");

						RLOS.mLogger.info( "after removing the starting and Trailing:"+Output_Eligible_Cards);
						String[] Output_Eligible_Cards_Arr=Output_Eligible_Cards.split("\\},\\{");
						for (int i=0; i<Output_Eligible_Cards_Arr.length;i++){
							String[] Output_Eligible_Cards_Array=Output_Eligible_Cards_Arr[i].split(",");
							RLOS.mLogger.info( "Output_Eligible_Cards_Array:"+Output_Eligible_Cards_Array);
							String[] Card_Prod=Output_Eligible_Cards_Array[0].split(":");
							String[] Limit=Output_Eligible_Cards_Array[1].split(":");
							String[] flag=Output_Eligible_Cards_Array[2].split(":");
							String Card_Product= Card_Prod[1].substring(1,Card_Prod[1].length()-1);
							String LIMIT= Limit[1];
							String FLAG= flag[1].substring(1,flag[1].length()-1);
							String combined_limitquery="select COMBINEDLIMIT_ELIGIBILITY from ng_master_cardProduct where CODE = '"+Card_Product+"' and Subproduct ='"+subProd+"'" ;
							RLOS.mLogger.info( "combined_limitquery:"+combined_limitquery);
							List<List<String>> combined_limitqueryXML = formObject.getDataFromDataSource(combined_limitquery);
							String combined_limit= combined_limitqueryXML.get(0).get(0);
							RLOS.mLogger.info( "Card_Prod:"+Card_Prod[1]);
							RLOS.mLogger.info( "Limit:"+Limit[1]);
							RLOS.mLogger.info( "flag:"+flag[1]);
							String query="INSERT INTO ng_rlos_IGR_Eligibility_CardLimit(Card_product,Eligible_limit,wi_name,flag,combined_limit) VALUES ('"+Card_Product+"','"+LIMIT+"','"+ formObject.getWFWorkitemName() +"','"+ FLAG +"','"+ combined_limit +"')";
							RLOS.mLogger.info( "QUERY:"+query);
							formObject.saveDataIntoDataSource(query);

							/* for (int j=0; j<Output_Eligible_Cards_Array.length;j++){
	    					 String[] values=Output_Eligible_Cards_Array[j].split(":");
	    					 RLOS.mLogger.info( "values:"+values);
	    					  if(values[0].contains("\"")){
	    						  values[0]=values[0].substring(1, values[0].length()-1);
	    				    	}
	    					  if(values[1].contains("\"")){
	    						  values[1]=values[1].substring(1, values[1].length()-1);
	    				    	}
	    					  String strInputXML =ExecuteQuery_APdelete("ng_rlos_IGR_Eligibility_CardProduct","WI_NAME ='" + formObject.getWFWorkitemName() + "'",cabinetName,sessionId);
	    					  strOutputXml = WFCallBroker.execute(strInputXML,jtsIp,Integer.parseInt(jtsPort),1);
	    					  String query="INSERT INTO ng_rlos_IGR_Eligibility_CardLimit("+values[0]+") VALUES ('"+values[1]+"')";
	    					  RLOS.mLogger.info( "QUERY:"+query);
	    			    	  formObject.saveDataIntoDataSource(query);
	    				  }*/
						}
					}
					catch(Exception e){
						RLOS.mLogger.info( "Exception occurred in elig dectech"+RLOSCommon.printException(e));
						RLOS.logException(e);

					}
				}	
				if (outputResponse.contains("Output_Final_DBR")){

					Output_Final_DBR = outputResponse.substring(outputResponse.indexOf("<Output_Final_DBR>")+18,outputResponse.indexOf("</Output_Final_DBR>"));
					if (Output_Final_DBR!=null){
						formObject.setNGValue("cmplx_EligibilityAndProductInfo_FinalDBR", Output_Final_DBR);
					}
					RLOS.mLogger.info("inside outpute get Output_Final_DBR"+Output_Final_DBR);
				}
				if (outputResponse.contains("Output_Interest_Rate")){
					Output_Interest_Rate = outputResponse.substring(outputResponse.indexOf("<Output_Interest_Rate>")+22,outputResponse.indexOf("</Output_Interest_Rate>"));
					if (Output_Interest_Rate!=null){
						formObject.setNGValue("cmplx_EligibilityAndProductInfo_InterestRate", Output_Interest_Rate);
					}
					RLOS.mLogger.info("inside outpute get Output_Interest_Rate"+Output_Interest_Rate);

				}
				//Setting the value in ELIGANDPROD info
				//Setting the value in lIABILITY info
				if (outputResponse.contains("Output_Existing_DBR")){
					Output_Existing_DBR = outputResponse.substring(outputResponse.indexOf("<Output_Existing_DBR>")+21,outputResponse.indexOf("</Output_Existing_DBR>"));
					if (Output_Existing_DBR!=null){
						formObject.setNGValue("cmplx_Liability_New_DBR", Output_Existing_DBR);
					}
					RLOS.mLogger.info("inside outpute get Output_Existing_DBR"+Output_Existing_DBR);

				}
				if (outputResponse.contains("Output_Affordable_EMI")){
					Output_Affordable_EMI = outputResponse.substring(outputResponse.indexOf("<Output_Affordable_EMI>")+23,outputResponse.indexOf("</Output_Affordable_EMI>"));
					RLOS.mLogger.info("inside outpute get Output_Affordable_EMI"+Output_Affordable_EMI);

				}
				try{
				double tenor=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor")==null||"".equalsIgnoreCase(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor"))?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor"));
				double RateofInt=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate")==null||"".equalsIgnoreCase(formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate"))?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate"));
				double out_aff_emi=Double.parseDouble(Output_Affordable_EMI);
				cac_calc_limit=Math.round(RLOSCommon.Cas_Limit(out_aff_emi,RateofInt,tenor));
				
				}
				catch(Exception e)
				{
					RLOS.logException(e);
				}

				if (outputResponse.contains("Output_Net_Salary_DBR")){
					Output_Net_Salary_DBR = outputResponse.substring(outputResponse.indexOf("<Output_Net_Salary_DBR>")+23,outputResponse.indexOf("</Output_Net_Salary_DBR>"));
					if (Output_Net_Salary_DBR!=null){
						formObject.setNGValue("cmplx_Liability_New_DBRNet", Output_Net_Salary_DBR);
					}
					RLOS.mLogger.info("inside outpute get Output_Net_Salary_DBR"+Output_Net_Salary_DBR);

				}
			//Setting the value in lIABILITY info
			//Setting the value in creditCard iFrame
			if (outputResponse.contains("Output_Delegation_Authority")){
				Output_Delegation_Authority = outputResponse.substring(outputResponse.indexOf("<Output_Delegation_Authority>")+29,outputResponse.indexOf("</Output_Delegation_Authority>"));
				RLOS.mLogger.info("inside outpute get Output_Existing_DBR"+Output_Eligible_Amount);
				
				}
			if (outputResponse.contains("Grade")){
				Grade = outputResponse.substring(outputResponse.indexOf("<Grade>")+7,outputResponse.indexOf("</Grade>"));
				RLOS.mLogger.info("inside outpute get Grade"+Grade);
				
				}
			if (outputResponse.contains("Output_Eligible_Amount")){
				Output_Eligible_Amount = outputResponse.substring(outputResponse.indexOf("<Output_Eligible_Amount>")+24,outputResponse.indexOf("</Output_Eligible_Amount>"));
				RLOS.mLogger.info("inside outpute get Output_Existing_DBR"+Output_Eligible_Amount);
				
				}
			//Setting the value in creditCard iFrame

				
				//String strInputXML = ExecuteQuery_APUpdate("ng_rlos_IGR_Eligibility_CardProduct","Product,Card_Product,Eligible_Card,Decision,Declined_Reason,wi_name","'Credit Card','"+subProd+"','"+Output_Eligible_Amount+"','Declined','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+formObject.getWFWorkitemName()+"'","WI_NAME ='" + formObject.getWFWorkitemName() + "'",cabinetName,sessionId);
				//RLOS.mLogger.info("UpdateinputXML is:"+UpdateinputXML);
				if ("Credit Card".equalsIgnoreCase(ReqProd)){
					String strInputXML =ExecuteQuery_APdelete("ng_rlos_IGR_Eligibility_CardProduct","WI_NAME ='" + formObject.getWFWorkitemName() + "'",cabinetName,sessionId);

					strOutputXml = WFCallBroker.execute(strInputXML,jtsIp,Integer.parseInt(jtsPort),1);
					RLOS.mLogger.info("strOutputXml is:"+strOutputXml);

					String mainCode=strOutputXml.substring(strOutputXml.indexOf("<MainCode>")+10,strOutputXml.indexOf("</MainCode>"));
					String rowUpdated=strOutputXml.substring(strOutputXml.indexOf("<Output>")+8,strOutputXml.indexOf("</Output>"));
					RLOS.mLogger.info("mainCode,rowUpdated is: "+mainCode+", "+rowUpdated);
				}
				else {
					String strInputXML =ExecuteQuery_APdelete("ng_rlos_IGR_Eligibility_PersonalLoan","WI_NAME ='" + formObject.getWFWorkitemName() + "'",cabinetName,sessionId);

					strOutputXml = WFCallBroker.execute(strInputXML,jtsIp,Integer.parseInt(jtsPort),1);
					RLOS.mLogger.info("strOutputXml is:"+strOutputXml);

					String mainCode=strOutputXml.substring(strOutputXml.indexOf("<MainCode>")+10,strOutputXml.indexOf("</MainCode>"));
					String rowUpdated=strOutputXml.substring(strOutputXml.indexOf("<Output>")+8,strOutputXml.indexOf("</Output>"));
					RLOS.mLogger.info("mainCode,rowUpdated is: "+mainCode+", "+rowUpdated);


				}
				/*
				outputResponse = outputResponse.substring(outputResponse.indexOf("<ProcessManagerResponse>")+24, outputResponse.indexOf("</ProcessManagerResponse>"));
				outputResponse = "<?xml version=\"1.0\"?><Response>" + outputResponse;
				outputResponse = outputResponse+"</Response>";
				*/
				String outResponseData = "<?xml version=\"1.0\"?><Response>" + 
										outputResponse.substring(outputResponse.indexOf("<ProcessManagerResponse>")+24, outputResponse.indexOf("</ProcessManagerResponse>"))+
										"</Response>";
				
				RLOS.mLogger.info("inside outpute get outputResponse"+outResponseData);
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				InputSource is = new InputSource(new StringReader(outResponseData));

				Document doc = builder.parse(is);
				doc.getDocumentElement().normalize();

				RLOS.mLogger.info(doc.getDocumentElement().getNodeName());

				NodeList nList = doc.getElementsByTagName("PM_Reason_Codes");
				RLOS.mLogger.info("----------------------------");

				for (int List_len_num = 0; List_len_num < nList.getLength(); List_len_num++) {
					String Reason_Decision;
					Node nNode = nList.item(List_len_num);
					RLOS.mLogger.info("\nCurrent Element :" + nNode.getNodeName());

					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						//RLOS.mLogger.info("Student roll no : " + eElement.getAttribute("rollno"));
						RLOS.mLogger.info("inside outpute get Decision_Objective"+ eElement.getElementsByTagName("Decision_Objective").item(0).getTextContent());
						RLOS.mLogger.info("inside outpute get Decision_Sequence_Number"+eElement.getElementsByTagName("Decision_Sequence_Number").item(0).getTextContent());
						RLOS.mLogger.info("inside outpute get Sequence_Number"+eElement.getElementsByTagName("Sequence_Number").item(0).getTextContent());

						RLOS.mLogger.info("inside outpute get Reason_Description"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent());

						String subprodquery="Select Description from ng_MASTER_SubProduct_PL where code='"+subProd+"'";
						String subprodCCquery="Select Description from ng_MASTER_SubProduct_CC where code='"+subProd+"'";
						String apptypequery="Select Description from ng_master_applicationtype where code='"+appType+"'";
						List<List<String>> subprodqueryXML = formObject.getDataFromDataSource(subprodquery);
						List<List<String>> apptypequeryXML = formObject.getDataFromDataSource(apptypequery);
						List<List<String>> subprodCCqueryXML = formObject.getDataFromDataSource(subprodCCquery);
						Reason_Decision = eElement.getElementsByTagName("Reason_Decision").item(0).getTextContent() ;
						RLOS.mLogger.info("Value of Reason_Decision"+Reason_Decision);
						if (List_len_num==0){
							if ("Credit Card".equalsIgnoreCase(ReqProd)){
								if ("D".equalsIgnoreCase(Reason_Decision)){
								squery="Insert into ng_rlos_IGR_Eligibility_CardProduct (S_no,Product,Card_Product,Eligible_Card,Decision,Declined_Reason,Delegation_Authorithy,Score_grade,wi_name,CACCalculatedLimit) values('"+List_len_num+"','Credit Card','"+subprodCCqueryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Declined','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"','"+cac_calc_limit+"')";
								}
								else if ("R".equalsIgnoreCase(Reason_Decision)){
									squery="Insert into ng_rlos_IGR_Eligibility_CardProduct (S_no,Product,Card_Product,Eligible_Card,Decision,Deviation_Code_Refer,Delegation_Authorithy,Score_grade,wi_name,CACCalculatedLimit) values('"+List_len_num+"','Credit Card','"+subprodCCqueryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Refer','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"','"+cac_calc_limit+"')";
								}
								else if ("A".equalsIgnoreCase(Reason_Decision)){
									squery="Insert into ng_rlos_IGR_Eligibility_CardProduct (S_no,Product,Card_Product,Eligible_Card,Decision,Deviation_Code_Refer,Delegation_Authorithy,Score_grade,wi_name,CACCalculatedLimit) values('"+List_len_num+"','Credit Card','"+subprodCCqueryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Approve','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"','"+cac_calc_limit+"')";
								}
							}
							else {
								if ("D".equalsIgnoreCase(Reason_Decision)){
									squery="Insert into ng_rlos_IGR_Eligibility_PersonalLoan (S_no,RequestedLoan,RequestedAppType,EligibleExposure,Decision,DeclinedReasonCode,DelegationAuthorithy,wi_name,CACCalculatedLimit) values('"+List_len_num+"','"+subprodqueryXML.get(0).get(0)+"','"+apptypequeryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Declined','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+formObject.getWFWorkitemName()+"','"+cac_calc_limit+"')";
								}
								else if ("R".equalsIgnoreCase(Reason_Decision)){
									squery="Insert into ng_rlos_IGR_Eligibility_PersonalLoan (S_no,RequestedLoan,RequestedAppType,EligibleExposure,Decision,ReferReasoncode,DelegationAuthorithy,wi_name,CACCalculatedLimit) values('"+List_len_num+"','"+subprodqueryXML.get(0).get(0)+"','"+apptypequeryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Refer','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+formObject.getWFWorkitemName()+"','"+cac_calc_limit+"')";
								}
								else if ("A".equalsIgnoreCase(Reason_Decision)){
									squery="Insert into ng_rlos_IGR_Eligibility_PersonalLoan (S_no,RequestedLoan,RequestedAppType,EligibleExposure,Decision,ReferReasoncode,DelegationAuthorithy,wi_name,CACCalculatedLimit) values('"+List_len_num+"','"+subprodqueryXML.get(0).get(0)+"','"+apptypequeryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Approve','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+formObject.getWFWorkitemName()+"','"+cac_calc_limit+"')";
								}
							}
						}
						else{
							if ("Credit Card".equalsIgnoreCase(ReqProd)){
								if ("D".equalsIgnoreCase(Reason_Decision)){
								squery="Insert into ng_rlos_IGR_Eligibility_CardProduct (S_no,Product,Card_Product,Eligible_Card,Decision,Declined_Reason,Delegation_Authorithy,Score_grade,wi_name,CACCalculatedLimit) values('"+List_len_num+"','Credit Card','"+subprodCCqueryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Declined','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"','')";
								}
								else if ("R".equalsIgnoreCase(Reason_Decision)){
									squery="Insert into ng_rlos_IGR_Eligibility_CardProduct (S_no,Product,Card_Product,Eligible_Card,Decision,Deviation_Code_Refer,Delegation_Authorithy,Score_grade,wi_name,CACCalculatedLimit) values('"+List_len_num+"','Credit Card','"+subprodCCqueryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Refer','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"','')";
								}
								else if ("A".equalsIgnoreCase(Reason_Decision)){
									squery="Insert into ng_rlos_IGR_Eligibility_CardProduct (S_no,Product,Card_Product,Eligible_Card,Decision,Deviation_Code_Refer,Delegation_Authorithy,Score_grade,wi_name,CACCalculatedLimit) values('"+List_len_num+"','Credit Card','"+subprodCCqueryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Approve','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"','')";
								}
							}
							else {
								if ("D".equalsIgnoreCase(Reason_Decision)){
									squery="Insert into ng_rlos_IGR_Eligibility_PersonalLoan (S_no,RequestedLoan,RequestedAppType,EligibleExposure,Decision,DeclinedReasonCode,DelegationAuthorithy,wi_name,CACCalculatedLimit) values('"+List_len_num+"','"+subprodqueryXML.get(0).get(0)+"','"+apptypequeryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Declined','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+formObject.getWFWorkitemName()+"','')";
								}
								else if ("R".equalsIgnoreCase(Reason_Decision)){
									squery="Insert into ng_rlos_IGR_Eligibility_PersonalLoan (S_no,RequestedLoan,RequestedAppType,EligibleExposure,Decision,ReferReasoncode,DelegationAuthorithy,wi_name,CACCalculatedLimit) values('"+List_len_num+"','"+subprodqueryXML.get(0).get(0)+"','"+apptypequeryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Refer','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+formObject.getWFWorkitemName()+"','')";
								}
								else if ("A".equalsIgnoreCase(Reason_Decision)){
									squery="Insert into ng_rlos_IGR_Eligibility_PersonalLoan (S_no,RequestedLoan,RequestedAppType,EligibleExposure,Decision,ReferReasoncode,DelegationAuthorithy,wi_name,CACCalculatedLimit) values('"+List_len_num+"','"+subprodqueryXML.get(0).get(0)+"','"+apptypequeryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Approve','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+formObject.getWFWorkitemName()+"','')";
								}
							}
						}
						

						RLOS.mLogger.info("Squery is"+squery);
						formObject.saveDataIntoDataSource(squery);

					}
				}
			}
		}
		catch(Exception e){
			RLOS.mLogger.info( "Exception:"+e.getMessage());
			RLOS.logException(e);
		}
		
	}
	
	public static String getProduct_details(){
		RLOS.mLogger.info( "inside getProduct_details : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int prod_row_count = formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		RLOS.mLogger.info( "inside getCustAddress_details add_row_count+ : "+prod_row_count);
		String FinalLimitQuery="select isnull(Final_limit,0) from ng_rlos_IGR_Eligibility_CardProduct where wi_name='"+formObject.getWFWorkitemName()+"'";
		List<List<String>> FinalLimitXML = formObject.getDataFromDataSource(FinalLimitQuery);
		//changed by aman shukla on 18th Sept.
		String FinalLimitPLQuery="select isnull(FinalLoanAmount,0) from ng_rlos_IGR_Eligibility_PersonalLoan where wi_name='"+formObject.getWFWorkitemName()+"'";
		List<List<String>> FinalLimitPLXML = formObject.getDataFromDataSource(FinalLimitPLQuery);
		
		
		
		String finalLimit="";
		if (!FinalLimitXML.isEmpty())
		{
			finalLimit=FinalLimitXML.get(0).get(0);
			formObject.setNGValue("cmplx_EligibilityAndProductInfo_FinalLimit", finalLimit);
		}
		else if (!FinalLimitPLXML.isEmpty())
		{
			finalLimit=FinalLimitPLXML.get(0).get(0);
			formObject.setNGValue("cmplx_EligibilityAndProductInfo_FinalLimit", finalLimit);
		}

		String  prod_xml_str ="";

		for (int i = 0; i<prod_row_count;i++){
			String prod_type = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 0); //0
			String reqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 1);//1
			String subProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2);//2
			String reqLimit=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 3);//3
			reqLimit=reqLimit.replaceAll(",", "");
			String appType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 4);//4
			String cardProd = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 5);//5
			String scheme=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 8);//6
			String tenure=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 7);//7
			String limitExpiry=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 12);//8
			String ApplicationDate=formObject.getNGValue("Introduction_date");
			String AppCateg=formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg");
			RLOS.mLogger.info( "inside getCustAddress_details ApplicationDate+ : "+ApplicationDate);
			ApplicationDate=ApplicationDate.substring(0, 10);
			limitExpiry=RLOSCommon.Convert_dateFormat(limitExpiry, "dd/MM/yyyy", "yyyy-MM-dd");
			String EMI;
			EMI=formObject.getNGValue("cmplx_EligibilityAndProductInfo_EMI");
			if(EMI == null){
				EMI=""; 
			}
			if(AppCateg == null){
				AppCateg=""; 
			}
			// ApplicationDate=Convert_dateFormat(ApplicationDate, "dd/MM/yyyy", "yyyy-MM-dd");

			prod_xml_str = prod_xml_str + "<ApplicationDetails><product_type>"+("Conventional".equalsIgnoreCase(prod_type)?"CON":"ISL")+"</product_type>";
			prod_xml_str = prod_xml_str + "<app_category>"+AppCateg+"</app_category>";
			prod_xml_str = prod_xml_str + "<requested_product>"+("Personal Loan".equalsIgnoreCase(reqProd)?"PL":"CC")+"</requested_product>";
			prod_xml_str = prod_xml_str + "<requested_limit>"+reqLimit+"</requested_limit>";
			prod_xml_str = prod_xml_str + "<sub_product>"+subProd+"</sub_product>";
			prod_xml_str = prod_xml_str + "<requested_card_product>"+cardProd+"</requested_card_product>";
			prod_xml_str = prod_xml_str + "<application_type>"+appType+"</application_type>";
			prod_xml_str = prod_xml_str + "<scheme>"+scheme+"</scheme>";
			prod_xml_str = prod_xml_str + "<tenure>"+tenure+"</tenure>";
			prod_xml_str = prod_xml_str + "<customer_type>"+("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))?"NTB":"Existing")+"</customer_type>";
			if ("Credit Card".equalsIgnoreCase(reqProd)){
				prod_xml_str = prod_xml_str + "<limit_Expiry_Date>"+limitExpiry+"</limit_Expiry_Date><final_limit>"+finalLimit+"</final_limit><emi>"+EMI+"</emi><manual_deviation>N</manual_deviation></ApplicationDetails>";
			}
			else{
				prod_xml_str = prod_xml_str + "<limit_Expiry_Date>"+limitExpiry+"</limit_Expiry_Date><final_limit>"+finalLimit+"</final_limit><emi>"+EMI+"</emi><manual_deviation>N</manual_deviation><application_date>"+ApplicationDate+"</application_date></ApplicationDetails>";
			}

		}
		RLOS.mLogger.info( "Address tag Cration: "+ prod_xml_str);
		return prod_xml_str;
	}

	public static String getCustAddress_details(){
		RLOS.mLogger.info( "inside getCustAddress_details : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int add_row_count = formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
		RLOS.mLogger.info( "inside getCustAddress_details add_row_count+ : "+add_row_count);
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
			String preferrd;
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
		RLOS.mLogger.info( "Address tag Cration: "+ add_xml_str);
		return add_xml_str;
	}
	
	
	public static void getOutputXMLValues(String outputXMLMsg, String response,String Operation_name) throws ParserConfigurationException
	{
		String sQuery = "";
		String tagValue = "";
		String EffectFrom="";
		String EffectTo ="";
		try
		{   
			RLOS.mLogger.info("inside outputxml");
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String col_name = "Form_Control,Parent_Tag_Name,XmlTag_Name,InDirect_Mapping,Grid_Mapping,Grid_Table,grid_table_xml_tags";

			//  sQuery = "SELECT "+col_name+" FROM NG_RLOS_Integration_Field_Response where Call_Name ='"+response+"'";
			//code added here
			if(!"".equalsIgnoreCase(Operation_name)){   
				RLOS.mLogger.info("operation111"+Operation_name);
				RLOS.mLogger.info("callName111"+col_name);
				sQuery = "SELECT "+col_name+" FROM NG_RLOS_Integration_Field_Response where Call_Name ='"+response+"' and Operation_name='"+Operation_name+"'" ;
				RLOS.mLogger.info( "sQuery "+sQuery);
			}
			else{
				sQuery = "SELECT "+col_name+" FROM NG_RLOS_Integration_Field_Response where Call_Name ='"+response+"'";
			}

			//ended here
			List<List<String>> outputTableXML=formObject.getDataFromDataSource(sQuery);


			String[] col_name_arr = col_name.split(",");

			RLOS.mLogger.info(outputTableXML.get(0).get(0)+outputTableXML.get(0).get(1)+outputTableXML.get(0).get(2)+outputTableXML.get(0).get(3));
			int n=outputTableXML.size();			

			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(outputXMLMsg)));
			RLOS.mLogger.info( doc+"");
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
					String form_control =  responseFileMap.get("Form_Control");
					String parent_tag =  responseFileMap.get("Parent_Tag_Name");
					String fielddbxml_tag = responseFileMap.get("XmlTag_Name");
					String Grid_col_tag =  responseFileMap.get("grid_table_xml_tags");
					RLOS.mLogger.info("Grid_col_tag"+Grid_col_tag);

					
					if (parent_tag!=null && !"".equalsIgnoreCase(parent_tag))
					{                    
						for (int i = 0; i < nl.getLength(); i++)
						{
							nl.item(i);
							if(nl.item(i).getNodeName().equalsIgnoreCase(fielddbxml_tag))
							{
								RLOS.mLogger.info(" fielddbxml_tag: "+fielddbxml_tag);
								String indirectMapping =  responseFileMap.get("InDirect_Mapping");
								String gridMapping =  responseFileMap.get("Grid_Mapping");

								if("Y".equalsIgnoreCase(gridMapping))
								{
									RLOS.mLogger.info("inside indirect mapping");
									if(Grid_col_tag.contains(",")){
										String Grid_col_tag_arr[]  =Grid_col_tag.split(",");
										//String grid_detail_str = nl.item(i).getNodeValue();
										NodeList childnode  = nl.item(i).getChildNodes();
										RLOS.mLogger.info("Grid_col_tag_arr: "+Grid_col_tag);   
										RLOS.mLogger.info("childnode"+childnode); 
										List<String> Grid_row = new ArrayList<String>(); 
										Grid_row.clear();

										String flaga="N";
										String flgYrs="N";
										for(int k = 0;k<Grid_col_tag_arr.length;k++){

											for (int child_node_len = 0 ;child_node_len< childnode.getLength();child_node_len++){
												if("EffectiveFrom".equalsIgnoreCase(childnode.item(child_node_len).getNodeName()))
													EffectFrom= childnode.item(child_node_len).getTextContent();



												if("EffectiveTo".equalsIgnoreCase(childnode.item(child_node_len).getNodeName()))
													EffectTo= childnode.item(child_node_len).getTextContent();

												if("ReporCntryDet".equalsIgnoreCase(childnode.item(child_node_len).getNodeName())){
													RLOS.mLogger.info("inside ReporCntryDet for OECD");
													NodeList ReporCntry_childnode  = childnode.item(child_node_len).getChildNodes();
													RLOS.mLogger.info("ReporCntry_childnode: "+ReporCntry_childnode.toString());
													for (int ReporCntry_childnode_len = 0; ReporCntry_childnode_len<ReporCntry_childnode.getLength();ReporCntry_childnode_len++){
														if(ReporCntry_childnode.item(ReporCntry_childnode_len).getNodeName().equalsIgnoreCase(Grid_col_tag_arr[k])){
															Grid_row.add(ReporCntry_childnode.item(ReporCntry_childnode_len).getTextContent());
															flaga="Y";
															break;
														}
														else {
															flaga="N";
														}
													}													
												}
												
												if(childnode.item(child_node_len).getNodeName().equalsIgnoreCase(Grid_col_tag_arr[k])){
													RLOS.mLogger.info("getTextContent: "+childnode.item(child_node_len).getNodeName());
													RLOS.mLogger.info("getTextContent: "+childnode.item(child_node_len).getTextContent());
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
											//need to see the years from product grid and add the grid
											if ("Years".equalsIgnoreCase(Grid_col_tag_arr[k]) && "N".equalsIgnoreCase(flgYrs))
											{												
												RLOS.mLogger.info("Inside Years tag:");
												int Diff=0;
												if(EffectFrom!=null && !"".equalsIgnoreCase(EffectFrom))
												{												
													int year = Calendar.getInstance().get(Calendar.YEAR);
													//d2=(Date) sdf.parse(EffectTo);
													RLOS.mLogger.info(EffectFrom+"  EffectTo: "+EffectTo);
													String[] year1 = EffectFrom.split("-");
													Diff= year-Integer.parseInt(year1[0]);
												}
												else
												{
													RLOS.mLogger.info("Exception as Effective from or Effective is not received");
												}

												flgYrs="Y";
												flaga="Y";
												RLOS.mLogger.info("Inside Years tag: "+Diff);     
												Grid_row.add(Integer.toString(Diff));

											}
											if("N".equalsIgnoreCase(flaga) ){
												RLOS.mLogger.info("value of flaga in if of for loop "+flaga);
												RLOS.mLogger.info("Grid_col_tag_arr[k]: "+Grid_col_tag_arr[k]);
												// RLOS.mLogger.info("getTextContent: "+childnode.item(child_node_len).getTextContent());
												Grid_row.add("NA");

											}
											flaga="N";
											flgYrs="N";

										}
										//Grid_row.add("true");
										Grid_row.add(formObject.getWFWorkitemName());
										//Grid_col_tag_arr[k]
										//code to add row in grid. and pass Grid_row in that.
										RLOS.mLogger.info( "$$AKSHAY$$List to be added in address grid: "+ Grid_row.toString());

										if("AddrDet".equalsIgnoreCase(fielddbxml_tag)){ 
											if(!"swift".equalsIgnoreCase(Grid_row.get(0)))
											{
												//added By Akshay on 28/6/17 for removing swift
												formObject.addItemFromList("cmplx_AddressDetails_cmplx_AddressGrid", Grid_row);
											}
										}	
									      else if("OECDDet".equalsIgnoreCase(fielddbxml_tag)){ 
                                              RLOS.mLogger.info("fieldxml_tag is:"+fielddbxml_tag+" form_control is:"+form_control);
                                              RLOS.mLogger.info("Grid row is:"+Grid_row);
                                              formObject.addItemFromList(form_control, Grid_row);
                                        }
										
										// added	 by yash on 10/7/2017 for getting values in worldcheck grid 
											else if("CustomerDetails".equalsIgnoreCase(fielddbxml_tag))
											{
												RLOS.mLogger.info("Form Control: "+form_control+" Grid data to be added for World check: "+ Grid_row);
												formObject.addItemFromList(form_control,Grid_row);
											}
											//ended 10/07/2017 for worldcheck grid


									}
									else{
										RLOS.mLogger.info( "Grid mapping is Y for this tag and grid_table_xml_tags column is blank tag name: "+ fielddbxml_tag);
									}

								}
								else if("Y".equalsIgnoreCase(indirectMapping)){
									RLOS.mLogger.info("inside indirect mapping");
									String col_list = "xmltag_name,tag_value,indirect_tag_list,indirect_formfield_list,indirect_child_tag,form_control,indirect_val,IS_Master,Master_Name";
									//code added
									if(!"".equalsIgnoreCase(Operation_name)){   
										RLOS.mLogger.info("operation111"+Operation_name);
										RLOS.mLogger.info("callName111"+col_name);
										sQuery = "SELECT "+col_list+" FROM NG_RLOS_Integration_Indirect_Response where Call_Name ='"+response+"' and XmlTag_Name = '"+nl.item(i).getNodeName()+"' and Operation_name='"+Operation_name+"'" ;
										RLOS.mLogger.info( "sQuery "+sQuery);
									}

									else{
										sQuery = "SELECT "+col_list+" FROM NG_RLOS_Integration_Indirect_Response where Call_Name ='"+response+"' and XmlTag_Name = '"+nl.item(i).getNodeName()+"'";
										RLOS.mLogger.info( "query: "+sQuery);
									}
									List<List<String>> outputindirect=formObject.getDataFromDataSource(sQuery);
									RLOS.mLogger.info( "1");
									String col_list_arr[] = col_list.split(",");
									Map<String, String> gridResponseMap = new HashMap<String, String>();
									for(List<String> mygridlist:outputindirect)
									{
										RLOS.mLogger.info( "inside list loop");

										for(int x=0;x<col_list_arr.length;x++)
										{
											gridResponseMap.put(col_list_arr[x],mygridlist.get(x));
											RLOS.mLogger.info( "inside put map"+x);
										}
										String xmltag_name = gridResponseMap.get("xmltag_name");
										String tag_value = gridResponseMap.get("tag_value");
										String indirect_tag_list = gridResponseMap.get("indirect_tag_list");
										String indirect_formfield_list = gridResponseMap.get("indirect_formfield_list");
										String indirect_child_tag = gridResponseMap.get("indirect_child_tag");
										String indirect_form_control = gridResponseMap.get("form_control");
										String indirect_val = gridResponseMap.get("indirect_val");
										String IS_Master = gridResponseMap.get("IS_Master");
										RLOS.mLogger.info( "IS_Master"+IS_Master);
										String Master_Name = gridResponseMap.get("Master_Name");
										RLOS.mLogger.info( "Master_Name"+Master_Name);
										RLOS.mLogger.info( "all details fetched");
										if("Y".equalsIgnoreCase(IS_Master)){
											String code = nl.item(i).getTextContent();
											RLOS.mLogger.info(code);
											sQuery= "Select description from "+Master_Name+" where Code='"+code+"'";
											RLOS.mLogger.info("Query to select master value: "+  sQuery);
											List<List<String>> query=formObject.getDataFromDataSource(sQuery);
											String value=query.get(0).get(0);
											RLOS.mLogger.info(value);
											RLOS.mLogger.info("Query to select master value: "+  sQuery);
											formObject.setNGValue(indirect_form_control,value,false);
											RLOS.mLogger.info(formObject.getNGValue(indirect_form_control));
										}

										else if(!"".equalsIgnoreCase(indirect_form_control))
										{
											if( nl.item(i).getTextContent().equalsIgnoreCase(tag_value)){
												RLOS.mLogger.info("Indirect value for "+xmltag_name+" Indirect control name: "+indirect_form_control+" final value: "+indirect_val+" tag_value: "+tag_value);
												formObject.setNGValue(indirect_form_control,indirect_val,false);
											}
											//RLOS.mLogger.info("form Control: "+ indirect_form_control + "indirect val: "+ indirect_val);

										}

										else{
											RLOS.mLogger.info("inside indirect mapping part2 else");
											if(indirect_tag_list!=null ){
												NodeList childnode  = nl.item(i).getChildNodes();
												RLOS.mLogger.info("childnode"+childnode);
												if(indirect_tag_list.contains(",")){
													RLOS.mLogger.info("inside indirect mapping part2 indirect_tag_list with ,");
													String indirect_tag_list_arr[] = indirect_tag_list.split(",");
													String indirect_formfield_list_arr[] = indirect_formfield_list.split(",");
													if(indirect_tag_list_arr.length == indirect_formfield_list_arr.length){
														for (int k=0; k<indirect_tag_list_arr.length;k++){
															RLOS.mLogger.info( "node name : "+childnode.item(0).getNodeName() +" node data: "+childnode.item(0).getTextContent());
															if(tag_value.equalsIgnoreCase(childnode.item(0).getTextContent()) && indirect_child_tag.equalsIgnoreCase(childnode.item(0).getNodeName())){
																for (int child_node_len = 1 ;child_node_len< childnode.getLength();child_node_len++){
																	RLOS.mLogger.info( "child node IF: Child node value: "+ child_node_len+" name: "+ childnode.item(child_node_len).getNodeName()+" child node value: "+ childnode.item(child_node_len).getTextContent());

																	if(childnode.item(child_node_len).getNodeName().equalsIgnoreCase(indirect_tag_list_arr[k])){

																		RLOS.mLogger.info("getTextContent"+childnode.item(child_node_len).getNodeName());
																		RLOS.mLogger.info("getTextContent"+childnode.item(child_node_len).getTextContent());
																		
																		RLOS.mLogger.info(" :"+childnode.item(child_node_len).getTextContent());
																		formObject.setNGValue(indirect_formfield_list_arr[k],childnode.item(child_node_len).getTextContent(),false);
																	}
																}

															}
														}
													}
													else{
														RLOS.mLogger.info("Error as for :"+xmltag_name+" indirect_formfield_list and indirect_tag_list dosent match");
													} 
												}
												else{
													RLOS.mLogger.info("inside indirect mapping part2 indirect_tag_list without ,");
													RLOS.mLogger.info( "tag_value: "+ tag_value+" indirect_child_tag: "+indirect_child_tag );
													RLOS.mLogger.info( "textcontent: "+ childnode.item(0).getTextContent()+" NodeName: "+childnode.item(0).getNodeName() );
													//changed for data populating in the Contact Details Frame
													if(tag_value.equalsIgnoreCase(childnode.item(0).getTextContent()) && indirect_child_tag.equalsIgnoreCase(childnode.item(0).getNodeName())){
														for (int child_node_len = 0 ;child_node_len< childnode.getLength();child_node_len++){
															RLOS.mLogger.info( "child node IF: Child node value: "+ child_node_len+" name: "+ childnode.item(child_node_len).getNodeName()+" child node value: "+ childnode.item(child_node_len).getTextContent());

															if(childnode.item(child_node_len).getNodeName().equalsIgnoreCase(indirect_tag_list)){

																RLOS.mLogger.info("getTextContent"+childnode.item(child_node_len).getNodeName());
																RLOS.mLogger.info("getTextContent"+childnode.item(child_node_len).getTextContent());
																
																RLOS.mLogger.info(" :"+childnode.item(child_node_len).getTextContent());
																formObject.setNGValue(indirect_formfield_list,childnode.item(child_node_len).getTextContent(),false);
															}
														}
													}
													//changed for data populating in the Contact Details Frame
												}


											}
										}     
									}
									//List<List<String>> outputIndirectXML=formObject.getNGDataFromDataCache(sQuery);
									//RLOS.mLogger.info("$$outputIndirectXML "+outputIndirectXML.get(0).get(0)+outputIndirectXML.get(0).get(1)+outputIndirectXML.get(0).get(2));

								}
								if("N".equalsIgnoreCase(indirectMapping) && "N".equalsIgnoreCase(gridMapping))
								{    
									RLOS.mLogger.info("check");
									tagValue =RLOS_Integration_Input.getTagValue(outputXMLMsg,nl.item(i).getNodeName());
									RLOS.mLogger.info("tagValue:"+tagValue);
									RLOS.mLogger.info("form_control:"+ form_control);

									RLOS.mLogger.info(tagValue);
									RLOS.mLogger.info(form_control);
									if("cmplx_FATCA_selectedreason".equalsIgnoreCase(form_control))
									{
										formObject.addItem(form_control,tagValue);
									}
									else{
										formObject.setNGValue(form_control,tagValue,false);
									}

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
			RLOS.mLogger.info(e.getMessage());
			RLOS.logException(e);
		}
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
	public static String ExecuteQuery_APInsert(String tableName,String columnName,String strValues, String cabinetName, String sessionId)
	{
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

}
