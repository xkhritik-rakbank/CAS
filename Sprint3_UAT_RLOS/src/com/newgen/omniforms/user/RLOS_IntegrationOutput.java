package com.newgen.omniforms.user;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
//----------------------------------------------------------------------------------------------------
//NEWGEN SOFTWARE TECHNOLOGIES LIMITED
//Group						: AP2
//Product / Project			: RLOS
//Module					: CAS
//File Name					: RLOS_IntegrationOutput.java
//Author					: Deepak Kumar
//Date written (DD/MM/YYYY)	: 06/08/2017
//Description				: 
//----------------------------------------------------------------------------------------------------

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.newgen.custom.Common_Utils;
import com.newgen.omni.wf.util.app.NGEjbClient;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.Column;
import com.newgen.omniforms.component.ComboBox;
import com.newgen.omniforms.component.ListView;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.user.RLOS;
import com.newgen.omniforms.user.RLOSCommon;
import com.newgen.wfdesktop.xmlapi.WFCallBroker;

public class RLOS_IntegrationOutput extends RLOSCommon implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static NGEjbClient ngejbclient = null;


	public static void valueSetIntegration(String outputResponse, String operationName)
	{
		//RLOS.mLogger.info( "Inside valueSetCustomer():"+outputResponse);
		String outputXMLHead = "";
		String outputXMLMsg = "";
		String returnDesc = "";
		String returnCode = "";
		String response= "";
		String Call_type="";
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();


		try
		{

			if(outputResponse.indexOf("<EE_EAI_HEADER>")>-1)
			{
				outputXMLHead=outputResponse.substring(outputResponse.indexOf("<EE_EAI_HEADER>"),outputResponse.indexOf("</EE_EAI_HEADER>")+16);
			}

			if(outputXMLHead.indexOf("<MsgFormat>")>-1)
			{
				response= outputXMLHead.substring(outputXMLHead.indexOf("<MsgFormat>")+11,outputXMLHead.indexOf("</MsgFormat>"));
			}
			if(outputXMLHead.indexOf("<ReturnDesc>")>-1)
			{
				returnDesc= outputXMLHead.substring(outputXMLHead.indexOf("<ReturnDesc>")+12,outputXMLHead.indexOf("</ReturnDesc>"));
			}
			if(outputXMLHead.indexOf("<ReturnCode>")>-1)
			{
				returnCode= outputXMLHead.substring(outputXMLHead.indexOf("<ReturnCode>")+12,outputXMLHead.indexOf("</ReturnCode>"));
			}

			if(outputResponse.indexOf("<MQ_RESPONSE_XML>")>-1)
			{
				outputXMLMsg=outputResponse.substring(outputResponse.indexOf("<MQ_RESPONSE_XML>")+17,outputResponse.indexOf("</MQ_RESPONSE_XML>"));

				RLOS.mLogger.info("Inside valueSetIntegration before value set :: wi_name: "+ formObject.getWFWorkitemName()+ " user name: " +formObject.getUserName());
				RLOS.mLogger.info("OUTPUT XML:"+outputXMLMsg);
				getOutputXMLValues(outputXMLMsg,response,operationName);
				RLOS.mLogger.info("Inside valueSetIntegration after value set:: wi_name: "+ formObject.getWFWorkitemName()+ " user name: " +formObject.getUserName());
			}
			if(outputResponse.indexOf("<CallType>")>-1){
				Call_type= outputResponse.substring(outputResponse.indexOf("<CallType>")+10,outputResponse.indexOf("</CallType>"));
				if	(Call_type!= null){
					RLOS.mLogger.info("Inside valueSetIntegration before value set :: wi_name: "+ formObject.getWFWorkitemName()+ " user name: " +formObject.getUserName());
					if ("CA".equalsIgnoreCase(Call_type)){
						dectechfromliability(outputResponse);
					}
					else if ("PM".equalsIgnoreCase(Call_type)){
						dectechfromeligbility(outputResponse);
					}
					RLOS.mLogger.info("Inside valueSetIntegration before value set :: wi_name: "+ formObject.getWFWorkitemName()+ " user name: " +formObject.getUserName());
				}

			}
		}
		catch(Exception e)
		{			
			//RLOS.mLogger.info("Exception occured in valueSetCustomer method: "+ e.getMessage());
			RLOS.logException(e);
		}
	}


	public static void dectechfromliability(String outputResponse){

		
		/*String Output_TAI;
		String Output_Existing_DBR ;
		String Output_Net_Salary_DBR;*/
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try{
			String Application_output = outputResponse.substring(outputResponse.lastIndexOf("<Application>"),outputResponse.lastIndexOf("</Application>")+14);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(Application_output));

            Document doc = builder.parse(is);
            doc.getDocumentElement().normalize();
            //doc.getElementsByTagName("Application").item(0).getTextContent();
		//Setting the value in ELIGANDPROD info
	
				if (doc.getElementsByTagName("Output_TAI").getLength()!=0){					
					formObject.setNGValue("cmplx_Liability_New_TAI",doc.getElementsByTagName("Output_TAI").item(0).getTextContent()) ;
				}
				if (doc.getElementsByTagName("Output_Existing_DBR").getLength()!=0){					
					formObject.setNGValue("cmplx_Liability_New_DBR",doc.getElementsByTagName("Output_Existing_DBR").item(0).getTextContent()) ;
				}
				if (doc.getElementsByTagName("Output_Net_Salary_DBR").getLength()!=0){					
					formObject.setNGValue("cmplx_Liability_New_DBRNet",doc.getElementsByTagName("Output_Net_Salary_DBR").item(0).getTextContent()) ;
				}
			
		
			/*if (outputResponse.contains("Output_TAI")){
				Output_TAI = outputResponse.substring(outputResponse.indexOf("<Output_TAI>")+12,outputResponse.indexOf("</Output_TAI>"));
				if (Output_TAI!=null){
					formObject.setNGValue("cmplx_Liability_New_TAI", Output_TAI);
				}

				//RLOS.mLogger.info("inside outpute get Output_TAI"+Output_TAI);
			}

			//FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			if (outputResponse.contains("Output_Existing_DBR")){
				Output_Existing_DBR = outputResponse.substring(outputResponse.indexOf("<Output_Existing_DBR>")+21,outputResponse.indexOf("</Output_Existing_DBR>"));
				if (Output_Existing_DBR!=null){
					Output_Existing_DBR=Output_Existing_DBR.substring(0, Output_Existing_DBR.indexOf(".")+3);
					formObject.setNGValue("cmplx_Liability_New_DBR", Output_Existing_DBR);
				}

				//RLOS.mLogger.info("inside outpute get Output_Existing_DBR"+Output_Existing_DBR);

			}
			if (outputResponse.contains("Output_Net_Salary_DBR")){
				Output_Net_Salary_DBR = outputResponse.substring(outputResponse.indexOf("<Output_Net_Salary_DBR>")+23,outputResponse.indexOf("</Output_Net_Salary_DBR>"));
				if (Output_Net_Salary_DBR!=null){
					Output_Net_Salary_DBR=Output_Net_Salary_DBR.substring(0, Output_Net_Salary_DBR.indexOf(".")+3);
					formObject.setNGValue("cmplx_Liability_New_DBRNet", Output_Net_Salary_DBR);
				}
				//RLOS.mLogger.info("inside outpute get Output_Net_Salary_DBR"+Output_Net_Salary_DBR);
			}*/
		}
		catch(Exception e){
			//RLOS.mLogger.info( "Exception:"+e.getMessage());
			RLOS.logException(e);

		}
	}


	public static void dectechfromeligbility(String outputResponse){
		try{
			ngejbclient = NGEjbClient.getSharedInstance();
			if(outputResponse.indexOf("<PM_Reason_Codes>")>-1 && outputResponse.indexOf("<PM_Outputs>")>-1 ){
				
				
				String Application_output = outputResponse.substring(outputResponse.lastIndexOf("<Application>"),outputResponse.lastIndexOf("</Application>")+14);
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                InputSource is = new InputSource(new StringReader(Application_output));

                Document doc = builder.parse(is);
                doc.getDocumentElement().normalize();
              //  doc.getElementsByTagName("Application").item(0).getTextContent();

				
				String squery="";
				String Output_TAI;
				String Output_Final_DBR;
				String Output_Existing_DBR;
				String Output_Eligible_Amount="";
				String Output_Affordable_EMI="";
				String Output_Delegation_Authority="";
				String Output_CPV_Waiver="";
				String Grade="";
				String Output_Interest_Rate;
				String Output_Net_Salary_DBR;
				double cac_calc_limit=0.0;
				String cac_calc_limit_str = null;
				String combined_limit="";
				FormReference formObject = FormContext.getCurrentInstance().getFormReference();
				String strOutputXml ;
				String sGeneralData = formObject.getWFGeneralData();
				//RLOS.mLogger.info("inside outpute get sGeneralData"+sGeneralData);
				int a=0;
				int b=0;
				int c=0;
				
				String cabinetName = sGeneralData.substring(sGeneralData.indexOf("<EngineName>")+12,sGeneralData.indexOf("</EngineName>")); 
				String sessionId = sGeneralData.substring(sGeneralData.indexOf("<DMSSessionId>")+14,sGeneralData.indexOf("</DMSSessionId>"));
				String jtsIp = sGeneralData.substring(sGeneralData.indexOf("<JTSIP>")+7,sGeneralData.indexOf("</JTSIP>"));
				String jtsPort = sGeneralData.substring(sGeneralData.indexOf("<JTSPORT>")+9,sGeneralData.indexOf("</JTSPORT>"));
				String ReqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
				String subProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2);
				String appType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,4);
				//Setting the value in ELIGANDPROD info
				if (doc.getElementsByTagName("Output_TAI").getLength()>0){
					Output_TAI = doc.getElementsByTagName("Output_TAI").item(0).getTextContent();
					if (Output_TAI!=null)
					{
						try{
							formObject.setNGValue("cmplx_Liability_New_TAI", Output_TAI);
							formObject.setNGValue("cmplx_EligibilityAndProductInfo_FinalTAI", Output_TAI);

						}
						catch (Exception e){
							RLOS.mLogger.info("Dectech error Exception:"+e.getMessage());
						}


					}
					//RLOS.mLogger.info("inside outpute get Output_TAI"+Output_TAI);
				}
				if (doc.getElementsByTagName("Output_Eligible_Cards").getLength()>0)
				{
					try
					{

						String Output_Eligible_Cards = doc.getElementsByTagName("Output_Eligible_Cards").item(0).getTextContent();
						Output_Eligible_Cards=Output_Eligible_Cards.substring(2, Output_Eligible_Cards.length()-2);
						String strInputXML =ExecuteQuery_APdelete("ng_rlos_IGR_Eligibility_CardLimit","WI_NAME ='" + formObject.getWFWorkitemName() + "'",cabinetName,sessionId);
						//strOutputXml = WFCallBroker.execute(strInputXML,jtsIp,Integer.parseInt(jtsPort),1);
						strOutputXml =ngejbclient.makeCall(jtsIp, jtsPort, "WebSphere", strInputXML);
						//RLOS.mLogger.info( "after removing the starting and Trailing:"+Output_Eligible_Cards);
						String[] Output_Eligible_Cards_Arr=Output_Eligible_Cards.split("\\},\\{");
						//added By Tarang as per drop 4 point 22 started on 19/02/2018
						if(Output_Eligible_Cards_Arr.length>0)
						{
							for (int i=0; i<Output_Eligible_Cards_Arr.length;i++){
								String[] Output_Eligible_Cards_Array=Output_Eligible_Cards_Arr[i].split(",");
								int arrKiLength = Output_Eligible_Cards_Array.length;
								//RLOS.mLogger.info( "Output_Eligible_Cards_Array:"+Output_Eligible_Cards_Array.length);
								if(arrKiLength>2){
								String[] Card_Prod=Output_Eligible_Cards_Array[0].split(":");
								String[] Limit=Output_Eligible_Cards_Array[1].split(":");
								String[] flag=Output_Eligible_Cards_Array[2].split(":");
								String Card_Product= Card_Prod[1].substring(1,Card_Prod[1].length()-1);
								String LIMIT= Limit[1];
								String FLAG= flag[1].substring(1,flag[1].length()-1);
								String combined_limitquery="select COMBINEDLIMIT_ELIGIBILITY from ng_master_cardProduct with (nolock)  where CODE = '"+Card_Product+"'" ;
								//RLOS.mLogger.info( "combined_limitquery:"+combined_limitquery);
								List<List<String>> combined_limitqueryXML = formObject.getNGDataFromDataCache(combined_limitquery);
								if (!combined_limitqueryXML.isEmpty()){
									combined_limit= combined_limitqueryXML.get(0).get(0);
								}	
//								RLOS.mLogger.info( "Card_Prod:"+Card_Prod[1]);
//								RLOS.mLogger.info( "Limit:"+Limit[1]);
//								RLOS.mLogger.info( "flag:"+flag[1]);
								String query="INSERT INTO ng_rlos_IGR_Eligibility_CardLimit(Card_product,Eligible_limit,wi_name,flag,combined_limit) VALUES ('"+Card_Product+"','"+LIMIT+"','"+ formObject.getWFWorkitemName() +"','"+ FLAG +"','"+ combined_limit +"')";
					//			RLOS.mLogger.info( "QUERY:"+query);
								formObject.saveDataIntoDataSource(query);
								//formObject.setNGValue("is_cc_waiver_require", "N");//added by akshay on 9/1/18
								}
							}
						}
						else{
							formObject.setNGValue("is_cc_waiver_require", "Y");
						}
						//ended By Tarang as per drop 4 point 22 started on 19/02/2018
					}
					catch(Exception e){
						//RLOS.mLogger.info( "Exception occurred in elig dectech"+printException(e));
						RLOS.logException(e);

					}
				}	
				if (doc.getElementsByTagName("Output_Final_DBR").getLength()>0){

					Output_Final_DBR = doc.getElementsByTagName("Output_Final_DBR").item(0).getTextContent();
					if (Output_Final_DBR!=null){
						Output_Final_DBR=Output_Final_DBR.substring(0, Output_Final_DBR.indexOf(".")+3);
						
						formObject.setNGValue("cmplx_EligibilityAndProductInfo_FinalDBR", Output_Final_DBR);
					}
					//RLOS.mLogger.info("inside outpute get Output_Final_DBR"+Output_Final_DBR);
				}
				if (doc.getElementsByTagName("Output_Interest_Rate").getLength()>0){
					Output_Interest_Rate = outputResponse.substring(outputResponse.indexOf("Output_Interest_Rate")+21,outputResponse.indexOf("</Output_Interest_Rate>"));
					if (Output_Interest_Rate!=null){
						// hritik 13.4.22
						formObject.setNGValue("cmplx_EligibilityAndProductInfo_InterestRate", Output_Interest_Rate);
						// if subprod = IM then set promoional and final rate and if final is empty then only final rate 
						// else 
						// cmplx_EligibilityAndProductInfo_InterestRate
						// cmplx_EligibilityAndProductInfo_promo_interest_rate
						String final_rate = formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalInterestRate");
						if(("IM".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2))))
						{
							if(final_rate.isEmpty() || final_rate=="")
							{
								formObject.setNGValue("cmplx_EligibilityAndProductInfo_FinalInterestRate",Output_Interest_Rate);
							}
							formObject.setNGValue("cmplx_EligibilityAndProductInfo_promo_interest_rate",Output_Interest_Rate);
						}
						else
						{
							formObject.setNGValue("cmplx_EligibilityAndProductInfo_FinalInterestRate", Output_Interest_Rate); 	
						}
						 // formObject.setNGValue("cmplx_EligibilityAndProductInfo_FinalInterestRate", Output_Interest_Rate); commented by hritik 13.4.22
						//added by akshay o 31/1/18 for proc 4300
						if(formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalInterestRate")!= null && formObject.getNGValue("cmplx_EligibilityAndProductInfo_BAseRate")!= null && formObject.getNGValue("cmplx_EligibilityAndProductInfo_ProdPrefRate") != null && !formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalInterestRate").equalsIgnoreCase("") && !formObject.getNGValue("cmplx_EligibilityAndProductInfo_BAseRate").equalsIgnoreCase("") && !formObject.getNGValue("cmplx_EligibilityAndProductInfo_ProdPrefRate").equalsIgnoreCase("")){
							formObject.setNGValue("cmplx_EligibilityAndProductInfo_MArginRate", Float.parseFloat(formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalInterestRate"))-Float.parseFloat(formObject.getNGValue("cmplx_EligibilityAndProductInfo_BAseRate"))-Float.parseFloat(formObject.getNGValue("cmplx_EligibilityAndProductInfo_ProdPrefRate")));
						}
					}
					//RLOS.mLogger.info("inside outpute get Output_Interest_Rate"+Output_Interest_Rate);

				}
				if (doc.getElementsByTagName("Output_CPV_Waiver").getLength()>0){
					Output_CPV_Waiver = doc.getElementsByTagName("Output_CPV_Waiver").item(0).getTextContent();
					int framestate1=formObject.getNGFrameState("DecisionHistoryContainer");
					if(framestate1 == 0){
						//RLOS.mLogger.info("RLOS count of current account not NTB Incomedetails");
					}
					else {new RLOSCommon().expandDecision();}
					if (Output_CPV_Waiver!=null){
						if(Output_CPV_Waiver.equalsIgnoreCase("N")){
							formObject.setNGValue("cmplx_DecisionHistory_VerificationRequired", "Y");
						}
						else if(Output_CPV_Waiver.equalsIgnoreCase("Y")){
							formObject.setNGValue("cmplx_DecisionHistory_VerificationRequired", "N");	
						}
						//Deepak Changes for PCAS-3033
						formObject.saveFragment("DecisionHistoryContainer");

					}
					//RLOS.mLogger.info("inside outpute get Output_Interest_Rate"+Output_CPV_Waiver);

				}
				//Setting the value in ELIGANDPROD info
				//Setting the value in lIABILITY info
				if (doc.getElementsByTagName("Output_Existing_DBR").getLength()>0){
					Output_Existing_DBR = doc.getElementsByTagName("Output_Existing_DBR").item(0).getTextContent();
					if (Output_Existing_DBR!=null){
						Output_Existing_DBR=Output_Existing_DBR.substring(0, Output_Existing_DBR.indexOf(".")+3);
						
						formObject.setNGValue("cmplx_Liability_New_DBR", Output_Existing_DBR);
					}
					//RLOS.mLogger.info("inside outpute get Output_Existing_DBR"+Output_Existing_DBR);

				}
				if (doc.getElementsByTagName("Output_Affordable_EMI").getLength()>0){
					Output_Affordable_EMI = doc.getElementsByTagName("Output_Affordable_EMI").item(0).getTextContent();
					formObject.setNGValue("cmplx_EligibilityAndProductInfo_OutAffEmi", Output_Affordable_EMI);
					//RLOS.mLogger.info("inside outpute get Output_Affordable_EMI"+Output_Affordable_EMI);

				}
				try{
					double tenor=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor")==null||"".equalsIgnoreCase(formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor"))?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_Tenor"));
					double RateofInt=Double.parseDouble(formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate")==null||"".equalsIgnoreCase(formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate"))?"0":formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate"));
					double out_aff_emi=Double.parseDouble(Output_Affordable_EMI);

					cac_calc_limit=Math.round(Cas_Limit(out_aff_emi,RateofInt,tenor));
					cac_calc_limit_str = String.format("%.0f", cac_calc_limit);
				}
				catch(Exception e)
				{
					RLOS.logException(e);

				}

				if (doc.getElementsByTagName("Output_Net_Salary_DBR").getLength()>0){
					Output_Net_Salary_DBR = doc.getElementsByTagName("Output_Net_Salary_DBR").item(0).getTextContent();
					if (Output_Net_Salary_DBR!=null){
						Output_Net_Salary_DBR=Output_Net_Salary_DBR.substring(0, Output_Net_Salary_DBR.indexOf(".")+3);
						
						formObject.setNGValue("cmplx_Liability_New_DBRNet", Output_Net_Salary_DBR);
					}
					//RLOS.mLogger.info("inside outpute get Output_Net_Salary_DBR"+Output_Net_Salary_DBR);

				}
				//Setting the value in lIABILITY info
				//Setting the value in creditCard iFrame
				if (doc.getElementsByTagName("Output_Delegation_Authority").getLength()>0){
					Output_Delegation_Authority = doc.getElementsByTagName("Output_Delegation_Authority").item(0).getTextContent();
					//RLOS.mLogger.info("inside outpute get Output_Existing_DBR"+Output_Eligible_Amount);


				}
				if (outputResponse.contains("Grade")){
					Grade = outputResponse.substring(outputResponse.indexOf("<Grade>")+7,outputResponse.indexOf("</Grade>"));
					//RLOS.mLogger.info("inside outpute get Grade"+Grade);


				}
				if (doc.getElementsByTagName("Output_Eligible_Amount").getLength()>0){
					Output_Eligible_Amount = doc.getElementsByTagName("Output_Eligible_Amount").item(0).getTextContent();
					//RLOS.mLogger.info("inside outpute get Output_Existing_DBR"+Output_Eligible_Amount);


				}
				//Setting the value in creditCard iFrame



				//String strInputXML = ExecuteQuery_APUpdate("ng_rlos_IGR_Eligibility_CardProduct","Product,Card_Product,Eligible_Card,Decision,Declined_Reason,wi_name","'Credit Card','"+subProd+"','"+Output_Eligible_Amount+"','Declined','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+formObject.getWFWorkitemName()+"'","WI_NAME ='" + formObject.getWFWorkitemName() + "'",cabinetName,sessionId);
				//RLOS.mLogger.info("UpdateinputXML is:"+UpdateinputXML);
				if (NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard").equalsIgnoreCase(ReqProd)){

					String strInputXML =ExecuteQuery_APdelete("ng_rlos_IGR_Eligibility_CardProduct","WI_NAME ='" + formObject.getWFWorkitemName() + "'",cabinetName,sessionId);

					//strOutputXml = WFCallBroker.execute(strInputXML,jtsIp,Integer.parseInt(jtsPort),1);
					strOutputXml = ngejbclient.makeCall(jtsIp, jtsPort, "WebSphere", strInputXML);
					//RLOS.mLogger.info("strOutputXml is:"+strOutputXml);

					String mainCode=strOutputXml.substring(strOutputXml.indexOf("<MainCode>")+10,strOutputXml.indexOf("</MainCode>"));
					String rowUpdated=strOutputXml.substring(strOutputXml.indexOf("<Output>")+8,strOutputXml.indexOf("</Output>"));
					//RLOS.mLogger.info("mainCode,rowUpdated is: "+mainCode+", "+rowUpdated);
				}
				else {
					String strInputXML =ExecuteQuery_APdelete("ng_rlos_IGR_Eligibility_PersonalLoan","WI_NAME ='" + formObject.getWFWorkitemName() + "'",cabinetName,sessionId);
					//RLOS.mLogger.info("RLSO Integration output:  strInputXML is:"+strInputXML);
					strOutputXml = ngejbclient.makeCall(jtsIp, jtsPort, "WebSphere", strInputXML);
					//RLOS.mLogger.info("RLSO Integration output: strOutputXml is:"+strOutputXml);

					String mainCode=strOutputXml.substring(strOutputXml.indexOf("<MainCode>")+10,strOutputXml.indexOf("</MainCode>"));
					String rowUpdated=strOutputXml.substring(strOutputXml.indexOf("<Output>")+8,strOutputXml.indexOf("</Output>"));
					//RLOS.mLogger.info("mainCode,rowUpdated is: "+mainCode+", "+rowUpdated);


				}
				/*
				outputResponse = outputResponse.substring(outputResponse.indexOf("<ProcessManagerResponse>")+24, outputResponse.indexOf("</ProcessManagerResponse>"));
				outputResponse = "<?xml version=\"1.0\"?><Response>" + outputResponse;
				outputResponse = outputResponse+"</Response>";
				 */
				String outResponseData = "<?xml version=\"1.0\"?><Response>" + 
				outputResponse.substring(outputResponse.indexOf("<ProcessManagerResponse>")+24, outputResponse.indexOf("</ProcessManagerResponse>"))+
				"</Response>";

				//RLOS.mLogger.info("inside outpute get outputResponse"+outResponseData);
				DocumentBuilderFactory factory_1 = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder_1 = factory_1.newDocumentBuilder();
				InputSource is_1 = new InputSource(new StringReader(outResponseData));

				Document doc_1 = builder_1.parse(is_1);
				doc_1.getDocumentElement().normalize();

				//RLOS.mLogger.info(doc.getDocumentElement().getNodeName());

				NodeList nList = doc_1.getElementsByTagName("PM_Reason_Codes");
				//RLOS.mLogger.info("----------------------------");

				int Approve_counter=0,Refer_decline_counter=0;
				for (int List_len_num = 0; List_len_num < nList.getLength(); List_len_num++) {
					String Reason_Decision;
					String Category="";
					Node nNode = nList.item(List_len_num);
					//RLOS.mLogger.info("\nCurrent Element :" + nNode.getNodeName());

					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						//RLOS.mLogger.info("Student roll no : " + eElement.getAttribute("rollno"));
//						RLOS.mLogger.info("inside outpute get Decision_Objective"+ eElement.getElementsByTagName("Decision_Objective").item(0).getTextContent());
//						RLOS.mLogger.info("inside outpute get Decision_Sequence_Number"+eElement.getElementsByTagName("Decision_Sequence_Number").item(0).getTextContent());
//						RLOS.mLogger.info("inside outpute get Sequence_Number"+eElement.getElementsByTagName("Sequence_Number").item(0).getTextContent());
//
//						RLOS.mLogger.info("inside outpute get Reason_Description"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent());
//ng_master_Subproduct_CC
						String subprodquery="Select Description from ng_MASTER_SubProduct_PL with(nolock) where code='"+subProd+"'";
						String subprodCCquery="Select Description from ng_MASTER_SubProduct_CC with(nolock) where code='"+subProd+"'";
						String apptypequery="Select Description from ng_master_applicationtype with(nolock) where code='"+appType+"' and subprodflag='"+subProd+"'";
						List<List<String>> subprodqueryXML = formObject.getNGDataFromDataCache(subprodquery);
						List<List<String>> apptypequeryXML = formObject.getNGDataFromDataCache(apptypequery);
						List<List<String>> subprodCCqueryXML = formObject.getNGDataFromDataCache(subprodCCquery);
						Reason_Decision = eElement.getElementsByTagName("Reason_Decision").item(0).getTextContent() ;
						//Changes Done to eliminate Null pointer exception
						if (eElement.getElementsByTagName("Category").getLength()!=0){
							
							Category = eElement.getElementsByTagName("Category").item(0).getTextContent() ;
							//RLOS.mLogger.info("DectechCategory  Categoryis"+Category);
							
							if (Category.contains("A -")){
								a++;
									}
							else if (Category.contains("B -")){
								b++;
									}
							else{
								c++;
									}
//							RLOS.mLogger.info("DectechCategory  a is"+a);
//							RLOS.mLogger.info("DectechCategory  b is"+b);
//							RLOS.mLogger.info("DectechCategory  C is"+c);
//							
							
							
							
						}
						//Changes Done to eliminate Null pointer exception

						//RLOS.mLogger.info("Value of Reason_Decision"+Reason_Decision);
						if (List_len_num==0){//sagarika for change of deviation logic
							if (NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard").equalsIgnoreCase(ReqProd)){

								if ("D".equalsIgnoreCase(Reason_Decision)){
									Refer_decline_counter++;
									squery="Insert into ng_rlos_IGR_Eligibility_CardProduct (S_no,Product,Card_Product,Eligible_Card,Decision,Declined_Reason,Delegation_Authorithy,Score_grade,wi_name,CACCalculatedLimit,category) values('"+List_len_num+"','Credit Card','"+subprodCCqueryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Declined','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"','"+cac_calc_limit_str+"','"+Category+"')";
								}
								else if ("R".equalsIgnoreCase(Reason_Decision)){
									Refer_decline_counter++;
									squery="Insert into ng_rlos_IGR_Eligibility_CardProduct (S_no,Product,Card_Product,Eligible_Card,Decision,Deviation_Code_Refer,Delegation_Authorithy,Score_grade,wi_name,CACCalculatedLimit,category) values('"+List_len_num+"','Credit Card','"+subprodCCqueryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Refer','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"','"+cac_calc_limit_str+"','"+Category+"')";
								}
								else if ("A".equalsIgnoreCase(Reason_Decision)){
									if(Refer_decline_counter<=0)
									{
									squery="Insert into ng_rlos_IGR_Eligibility_CardProduct (S_no,Product,Card_Product,Eligible_Card,Decision,Deviation_Code_Refer,Delegation_Authorithy,Score_grade,wi_name,CACCalculatedLimit,category) values('"+List_len_num+"','Credit Card','"+subprodCCqueryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Approve','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"','"+cac_calc_limit_str+"','"+Category+"')";
									Approve_counter++;
									}
									else{
										squery="";
									}
								
								}
							}
							else {
								if ("D".equalsIgnoreCase(Reason_Decision)){
									squery="Insert into ng_rlos_IGR_Eligibility_PersonalLoan (S_no,RequestedLoan,RequestedAppType,EligibleExposure,Decision,DeclinedReasonCode,DelegationAuthorithy,ScoreGrade,wi_name,CACCalculatedLimit,category) values('"+List_len_num+"','"+subprodqueryXML.get(0).get(0)+"','"+apptypequeryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Declined','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"','"+cac_calc_limit_str+"','"+Category+"')";
								}
								else if ("R".equalsIgnoreCase(Reason_Decision)){
									squery="Insert into ng_rlos_IGR_Eligibility_PersonalLoan (S_no,RequestedLoan,RequestedAppType,EligibleExposure,Decision,ReferReasoncode,DelegationAuthorithy,ScoreGrade,wi_name,CACCalculatedLimit,category) values('"+List_len_num+"','"+subprodqueryXML.get(0).get(0)+"','"+apptypequeryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Refer','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"','"+cac_calc_limit_str+"','"+Category+"')";
								}
								else if ("A".equalsIgnoreCase(Reason_Decision)){
									if(Refer_decline_counter<=0)
									{
										squery="Insert into ng_rlos_IGR_Eligibility_PersonalLoan (S_no,RequestedLoan,RequestedAppType,EligibleExposure,Decision,ReferReasoncode,DelegationAuthorithy,ScoreGrade,wi_name,CACCalculatedLimit,category) values('"+List_len_num+"','"+subprodqueryXML.get(0).get(0)+"','"+apptypequeryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Approve','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"','"+cac_calc_limit_str+"','"+Category+"')";
										Approve_counter++;
									} else{
										squery="";
									}
									}
							}
						}
						else{
							if (NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard").equalsIgnoreCase(ReqProd)){

								if ("D".equalsIgnoreCase(Reason_Decision)){//sagarika for change of deviation logic
									Refer_decline_counter++;
									squery="Insert into ng_rlos_IGR_Eligibility_CardProduct (S_no,Product,Card_Product,Eligible_Card,Decision,Declined_Reason,Delegation_Authorithy,Score_grade,wi_name,CACCalculatedLimit,category) values('"+List_len_num+"','Credit Card','"+subprodCCqueryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Declined','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"','','"+Category+"')";
								}
								else if ("R".equalsIgnoreCase(Reason_Decision)){
									Refer_decline_counter++;
									squery="Insert into ng_rlos_IGR_Eligibility_CardProduct (S_no,Product,Card_Product,Eligible_Card,Decision,Deviation_Code_Refer,Delegation_Authorithy,Score_grade,wi_name,CACCalculatedLimit,category) values('"+List_len_num+"','Credit Card','"+subprodCCqueryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Refer','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"','','"+Category+"')";
								}
								else if ("A".equalsIgnoreCase(Reason_Decision)){
									if(Refer_decline_counter<=0)
									{
									squery="Insert into ng_rlos_IGR_Eligibility_CardProduct (S_no,Product,Card_Product,Eligible_Card,Decision,Deviation_Code_Refer,Delegation_Authorithy,Score_grade,wi_name,CACCalculatedLimit,category) values('"+List_len_num+"','Credit Card','"+subprodCCqueryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Approve','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"','','"+Category+"')";
									Approve_counter++;
									}
									else{
										squery="";
									}
								}
							}
							else {
								if ("D".equalsIgnoreCase(Reason_Decision)){
									Refer_decline_counter++;
									squery="Insert into ng_rlos_IGR_Eligibility_PersonalLoan (S_no,RequestedLoan,RequestedAppType,EligibleExposure,Decision,DeclinedReasonCode,DelegationAuthorithy,ScoreGrade,wi_name,CACCalculatedLimit,category) values('"+List_len_num+"','"+subprodqueryXML.get(0).get(0)+"','"+apptypequeryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Declined','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"','"+cac_calc_limit_str+"','"+Category+"')";//by shweta to save scoregrade for pl
								}
								else if ("R".equalsIgnoreCase(Reason_Decision)){
									Refer_decline_counter++;
									squery="Insert into ng_rlos_IGR_Eligibility_PersonalLoan (S_no,RequestedLoan,RequestedAppType,EligibleExposure,Decision,ReferReasoncode,DelegationAuthorithy,ScoreGrade,wi_name,CACCalculatedLimit,category) values('"+List_len_num+"','"+subprodqueryXML.get(0).get(0)+"','"+apptypequeryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Refer','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"','"+cac_calc_limit_str+"','"+Category+"')";
								}
								else if ("A".equalsIgnoreCase(Reason_Decision)){
									if(Refer_decline_counter<=0)
									{
									squery="Insert into ng_rlos_IGR_Eligibility_PersonalLoan (S_no,RequestedLoan,RequestedAppType,EligibleExposure,Decision,ReferReasoncode,DelegationAuthorithy,ScoreGrade,wi_name,CACCalculatedLimit,category) values('"+List_len_num+"','"+subprodqueryXML.get(0).get(0)+"','"+apptypequeryXML.get(0).get(0)+"','"+Output_Eligible_Amount+"','Approve','"+eElement.getElementsByTagName("Reason_Description").item(0).getTextContent()+"-"+eElement.getElementsByTagName("Reason_Code").item(0).getTextContent()+"','"+Output_Delegation_Authority+"','"+Grade+"','"+formObject.getWFWorkitemName()+"','"+cac_calc_limit_str+"','"+Category+"')";
									Approve_counter++;
									}
									else{
										squery="";
									}
								}
							}
						}

						//RLOS.mLogger.info("Squery is"+squery);
						if(!"".equalsIgnoreCase(squery)){
							formObject.saveDataIntoDataSource(squery);//for decision entry
						}

					}
					//sagarika for change of deviation logic
					if(List_len_num==(nList.getLength()-1))
					{
					if(Approve_counter>0 && Refer_decline_counter>0 )
					{	if (NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard").equalsIgnoreCase(ReqProd)){
							String Query="Delete from ng_rlos_IGR_Eligibility_CardProduct where Decision='Approve' AND wi_name='"+formObject.getWFWorkitemName()+"'";
							formObject.saveDataIntoDataSource(Query);
						}else {
							String Query="Delete from ng_rlos_IGR_Eligibility_PersonalLoan where Decision='Approve' AND wi_name='"+formObject.getWFWorkitemName()+"'";
							formObject.saveDataIntoDataSource(Query);
						}
					}
					}
				}
				/*if (NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard").equalsIgnoreCase(ReqProd)){
					String DectechCategA="Select count(*) from ng_rlos_IGR_Eligibility_CardProduct where wi_name='"+formObject.getWFWorkitemName()+"' and category like '%A -%'";
					String DectechCategB="Select count(*) from ng_rlos_IGR_Eligibility_CardProduct where wi_name='"+formObject.getWFWorkitemName()+"' and category like '%B -%'";
					List<List<String>> DectechCategAXML = formObject.getDataFromDataSource(DectechCategA);
					List<List<String>> DectechCategBXML = formObject.getDataFromDataSource(DectechCategB);
					RLOS.mLogger.info("DectechCategAXML is"+DectechCategAXML);
					RLOS.mLogger.info("DectechCategBXML is"+DectechCategBXML);
					RLOS.mLogger.info("AmanAMANANMANANMANAMAAN"+DectechCategBXML);
					if (!DectechCategAXML.get(0).get(0).equalsIgnoreCase("0")){
						formObject.setNGValue("DectechCategory", "A");
					}
					else if (!DectechCategBXML.get(0).get(0).equalsIgnoreCase("0")){
						formObject.setNGValue("DectechCategory", "B");
					}
					else
					{
						formObject.setNGValue("DectechCategory", "C");
					}
				}
				else {
					String DectechCategA="Select count(*) from ng_rlos_IGR_Eligibility_PersonalLoan where wi_name='"+formObject.getWFWorkitemName()+"' and category like '%A -%'";
					String DectechCategB="Select count(*) from ng_rlos_IGR_Eligibility_PersonalLoan where wi_name='"+formObject.getWFWorkitemName()+"' and category like '%B -%'";
					List<List<String>> DectechCategAXML = formObject.getNGDataFromDataCache(DectechCategA);
					List<List<String>> DectechCategBXML = formObject.getNGDataFromDataCache(DectechCategB);
					RLOS.mLogger.info("DectechCategAXML is"+DectechCategA);
					RLOS.mLogger.info("DectechCategBXML is"+DectechCategB);
					
					RLOS.mLogger.info("DectechCategAXML is"+DectechCategAXML);
					RLOS.mLogger.info("DectechCategBXML is"+DectechCategBXML);
					RLOS.mLogger.info("DectechCategAXML is"+DectechCategAXML.get(0).get(0));
					RLOS.mLogger.info("DectechCategBXML is"+DectechCategBXML.get(0).get(0));
					
					if (!DectechCategAXML.get(0).get(0).equalsIgnoreCase("0")){
						RLOS.mLogger.info("DectechCategory  A is"+formObject.getNGValue("DectechCategory"));
						
						formObject.setNGValue("DectechCategory", "A");
						RLOS.mLogger.info("DectechCategory is"+formObject.getNGValue("DectechCategory"));
						
					}
					else if (!DectechCategBXML.get(0).get(0).equalsIgnoreCase("0")){
						RLOS.mLogger.info("DectechCategory  B is"+formObject.getNGValue("DectechCategory"));
						
						formObject.setNGValue("DectechCategory", "B");
						RLOS.mLogger.info("DectechCategory  B is"+formObject.getNGValue("DectechCategory"));
						
					}
					else
					{
						RLOS.mLogger.info("DectechCategory  C is"+formObject.getNGValue("DectechCategory"));
						
						formObject.setNGValue("DectechCategory", "C");
						RLOS.mLogger.info("DectechCategory  C is"+formObject.getNGValue("DectechCategory"));
						
					}
				}*/
				if (a!=0){
					formObject.setNGValue("DectechCategory", "A");
				}
				else if ((b !=0)&&(a ==0)){
					formObject.setNGValue("DectechCategory", "B");
				}
				else{
					formObject.setNGValue("DectechCategory", "C");
				}
				if ("A".equalsIgnoreCase(formObject.getNGValue("DectechCategory"))){
					formObject.setLocked("cmplx_DecisionHistory_Decision", true);
					formObject.setNGValue("cmplx_DecisionHistory_Decision", "Reject");
					formObject.setLocked("DecisionHistory_DecisionReasonCode", false);
					
				}
				else
				{
					formObject.setLocked("cmplx_DecisionHistory_Decision", false);
					formObject.setNGValue("cmplx_DecisionHistory_Decision", "");
					
					formObject.setLocked("DecisionHistory_DecisionReasonCode", true);
					
					
				}	

			}
		}
		catch(Exception e){
			//RLOS.mLogger.info( "Exception:"+e.getMessage());
			RLOS.logException(e);

		}


	}
	

	public static void getOutputXMLValues(String outputXMLMsg, String response,String Operation_name) throws ParserConfigurationException
	{
		String sQuery = "";
		String tagValue = "";
		String EffectFrom="";
		String EffectTo ="";
		//boolean rowAddedinAddress = false;
		//boolean rowAddedinOECD = false;
		try
		{   
		//	
			//RLOS.mLogger.info("inside outputxml");
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			Common_Utils common=new Common_Utils(RLOS.mLogger);
			String col_name = "Form_Control,Parent_Tag_Name,XmlTag_Name,InDirect_Mapping,Grid_Mapping,Grid_Table,grid_table_xml_tags";
			//  sQuery = "SELECT "+col_name+" FROM NG_RLOS_Integration_Field_Response where Call_Name ='"+response+"'";
			//code added here
			if(!"".equalsIgnoreCase(Operation_name)){   
//				RLOS.mLogger.info("operation111"+Operation_name);
//				RLOS.mLogger.info("callName111"+col_name);
				sQuery = "SELECT "+col_name+" FROM NG_RLOS_Integration_Field_Response with(nolock) where Call_Name ='"+response+"' and Operation_name='"+Operation_name+"' order by ID" ;
				//RLOS.mLogger.info( "sQuery "+sQuery);
			}
			else{
				sQuery = "SELECT "+col_name+" FROM NG_RLOS_Integration_Field_Response where Call_Name ='"+response+"' order by ID";
			}

			//ended here
			String Fatcadocs="";//FatcaReason="";
			//List<List<String>> fatcaOutput;
			List<List<String>> outputTableXML=formObject.getDataFromDataSource(sQuery);
			String[] col_name_arr = col_name.split(",");
			//			RLOS.mLogger.info(outputTableXML.get(0).get(0)+outputTableXML.get(0).get(1)+outputTableXML.get(0).get(2)+outputTableXML.get(0).get(3));
			int n=outputTableXML.size();			
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(outputXMLMsg)));
			RLOS.mLogger.info( doc+"");
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
					String form_control =  responseFileMap.get("Form_Control");
					String parent_tag =  responseFileMap.get("Parent_Tag_Name");
					String fielddbxml_tag = responseFileMap.get("XmlTag_Name");
					String grid_table_flag = responseFileMap.get("Grid_Table");
					String Grid_col_tag =  responseFileMap.get("grid_table_xml_tags");
					String GridTags="AddrDet,OECDDet,KYCDet,FatcaDet,CorpRelDet";
					//boolean Grid_dateColumn_found;
					//RLOS.mLogger.info("Grid_col_tag"+Grid_col_tag);
					if(GridTags.contains(fielddbxml_tag)){
					//	RLOS.mLogger.info("Before clearing"+form_control+" grid");
						if(Operation_name.equalsIgnoreCase("Primary_CIF")){
							String firstName = getTagValue(outputXMLMsg, "FName");
							String lastName = getTagValue(outputXMLMsg, "LName");
//							RLOS.mLogger.info("firstname is:"+firstName);
//							RLOS.mLogger.info("lastName is:"+lastName);
							formObject.clear(form_control);
						}
						else{
							//for Supp and Guar
							clearSelectiveRows(Operation_name,form_control);
						}
						
						
					}
					if (parent_tag!=null && !"".equalsIgnoreCase(parent_tag))
					{                    
						for (int i = 0; i < nl.getLength(); i++)
						{
							nl.item(i);
							if(nl.item(i).getNodeName().equalsIgnoreCase(fielddbxml_tag))
							{
							//	RLOS.mLogger.info(" fielddbxml_tag: "+fielddbxml_tag);
								String indirectMapping =  responseFileMap.get("InDirect_Mapping");
								String gridMapping =  responseFileMap.get("Grid_Mapping");

								if("Y".equalsIgnoreCase(gridMapping))
								{
									//RLOS.mLogger.info("inside grid mapping");
									if(Grid_col_tag.contains(",")){
										String Grid_col_tag_arr[]  =Grid_col_tag.split(",");
										//String grid_detail_str = nl.item(i).getNodeValue();
										NodeList childnode  = nl.item(i).getChildNodes();
//										RLOS.mLogger.info("Grid_col_tag_arr: "+Grid_col_tag);   
//										RLOS.mLogger.info("childnode"+childnode); 
										List<String> Grid_row = new ArrayList<String>(); 
										Grid_row.clear();
										//change by saurabh on 4th Jan for clearing the previous rows in grid.
										//commented by akshay on 12/5/18
										/*if(("AddrDet".equalsIgnoreCase(fielddbxml_tag) && !rowAddedinAddress) || ("OECDDet".equalsIgnoreCase(fielddbxml_tag) && !rowAddedinOECD)){
											RLOS.mLogger.info("i is 0 and formControl is: "+form_control);
											RLOS.mLogger.info("rows in grid before: "+formObject.getLVWRowCount(form_control));
											formObject.clear(form_control);
											RLOS.mLogger.info("rows in grid after: "+formObject.getLVWRowCount(form_control));
										}*/
										String flaga="N";
										String flgYrs="N";
										for(int k = 0;k<Grid_col_tag_arr.length;k++){
											boolean repEnc = false;
											//RLOS.mLogger.info("inside Grid_col_tag_arr loop iteraition is: "+k+" and value is: "+Grid_col_tag_arr[k]);
											for (int child_node_len = 0 ;child_node_len< childnode.getLength();child_node_len++){
										//		RLOS.mLogger.info("inside child_node_len loop iteraition is: "+child_node_len+" and value is: "+childnode.item(child_node_len).getNodeName());
												if("EffectiveFrom".equalsIgnoreCase(childnode.item(child_node_len).getNodeName()))
													EffectFrom= childnode.item(child_node_len).getTextContent();
												
												if("EffectiveTo".equalsIgnoreCase(childnode.item(child_node_len).getNodeName()))
													EffectTo= childnode.item(child_node_len).getTextContent();
												
												if("DocumentsCollected".equalsIgnoreCase(childnode.item(child_node_len).getNodeName())){
													//RLOS.mLogger.info("Inside FATCA DocCollected tag "+childnode.item(child_node_len).getTextContent());
													Fatcadocs=childnode.item(child_node_len).getTextContent();
													//String[] docslist=docs.split("!");
													//RLOS.mLogger.info("Inside FATCA DocCollected tag--->docslist: "+Fatcadocs);
												}
												
												/*if("FatcaReason".equalsIgnoreCase(childnode.item(child_node_len).getNodeName())){
													RLOS.mLogger.info("Inside FATCA DocCollected tag "+childnode.item(child_node_len).getTextContent());
													
													String[] FatcaReasonList=childnode.item(child_node_len).getTextContent().split("!");
													for(int counter=0;i<FatcaReasonList.length;counter++){
														String query="select description from ng_master_fatcaReasons where code='"+FatcaReasonList[counter]+"'";
														RLOS.mLogger.info("Inside FATCA FatcaReason tag--->query "+query);
														fatcaOutput=formObject.getNGDataFromDataCache(query);
														RLOS.mLogger.info("Inside FATCA FatcaReason tag--->fatcaOutput "+fatcaOutput);
														if(fatcaOutput!=null && !fatcaOutput.isEmpty()){
															if(counter==0){
																FatcaReason=fatcaOutput.get(0).get(0);
														}
														else{
															FatcaReason+=fatcaOutput.get(0).get(0);	
														}
													}
													RLOS.mLogger.info("Inside FATCA FatcaReason tag--->FatcaReason: "+Fatcadocs);
												}
												}*/
												
												if("ReporCntryDet".equalsIgnoreCase(childnode.item(child_node_len).getNodeName())  && !repEnc){//&& repCntryDetAdd<repCounter
													repEnc = true;
											//		RLOS.mLogger.info("inside ReporCntryDet for OECD");
													NodeList ReporCntry_childnode  = childnode.item(child_node_len).getChildNodes();
//													RLOS.mLogger.info("ReporCntry_childnode: "+ReporCntry_childnode.item(0).getNodeName());
//													RLOS.mLogger.info("ReporCntry_childnode length: "+ReporCntry_childnode.getLength());
													for (int ReporCntry_childnode_len = 0; ReporCntry_childnode_len<ReporCntry_childnode.getLength();ReporCntry_childnode_len++){
														//RLOS.mLogger.info("tag name: "+ReporCntry_childnode.item(ReporCntry_childnode_len).getNodeName());
														if(ReporCntry_childnode.item(ReporCntry_childnode_len).getNodeName().equalsIgnoreCase(Grid_col_tag_arr[k])){
//															RLOS.mLogger.info("Match found for tag: "+ReporCntry_childnode.item(ReporCntry_childnode_len).getNodeName());
//															RLOS.mLogger.info("Value of matched tag: "+ReporCntry_childnode.item(ReporCntry_childnode_len).getTextContent());
															Grid_row.add(ReporCntry_childnode.item(ReporCntry_childnode_len).getTextContent());
													//		RLOS.mLogger.info("Grid_row: "+Grid_row);

															flaga="Y";
															
															break;
														}
														else {
															flaga="N";
														}
													}
													
												}
												

												if(childnode.item(child_node_len).getNodeName().equalsIgnoreCase(Grid_col_tag_arr[k])){
//													RLOS.mLogger.info("Nodename "+childnode.item(child_node_len).getNodeName());
//													RLOS.mLogger.info("getTextContent: "+childnode.item(child_node_len).getTextContent());
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
													//	RLOS.mLogger.info("Matching column  is found in call tag");
														UIComponent pComp =formObject.getComponent(form_control);
														
														
														if(grid_table_flag.equals("Y")){
															Grid_row.add(childnode.item(child_node_len).getTextContent());
														}
														else{
														if( pComp != null && pComp instanceof ListView )
														{			
															
															if(!((Column)(pComp.getChildren().get(k))).getFormat().equals("")){
																//RLOS.mLogger.info(form_control+"-->"+((Column)(pComp.getChildren().get(k))).getName()+" is of date type");
														//RLOS.mLogger.info("After converting date format-->value: "+common.Convert_dateFormat(childnode.item(child_node_len).getTextContent(), "yyyy-MM-dd", "dd/MM/yyyy"));
																Grid_row.add(common.Convert_dateFormat(childnode.item(child_node_len).getTextContent(), "yyyy-MM-dd", "dd/MM/yyyy"));																
															}
															else{
																
																Grid_row.add(childnode.item(child_node_len).getTextContent());
															}
														}
													  }
													}
													flaga="Y";
													break;
												}                                            
											}
											
											
											//need to see the years from product grid and add the grid
											if ("Years".equalsIgnoreCase(Grid_col_tag_arr[k]) && "N".equalsIgnoreCase(flgYrs))
											{												


											//	RLOS.mLogger.info("Inside Years tag:");
												int Diff=0;
												if(EffectFrom!=null && !"".equalsIgnoreCase(EffectFrom))
												{												
													int year = Calendar.getInstance().get(Calendar.YEAR);
													//d2=(Date) sdf.parse(EffectTo);
												//	RLOS.mLogger.info(EffectFrom+"  EffectTo: "+EffectTo);
													String[] year1 = EffectFrom.split("-");
													Diff= year-Integer.parseInt(year1[0]);
													Grid_row.add(String.valueOf(Diff));
												}
												else
												{

													//RLOS.mLogger.info("Exception as Effective from or Effective is not received");
													Grid_row.add("");
												}

												flgYrs="Y";
												flaga="Y";
												//RLOS.mLogger.info("Inside Years tag: "+Diff);     
											//	Grid_row.add(Integer.toString(Diff));		
												}
											
											if("N".equalsIgnoreCase(flaga) ){
//												RLOS.mLogger.info("value of flaga in if of for loop "+flaga);
//												RLOS.mLogger.info("Grid_col_tag_arr[k]: "+Grid_col_tag_arr[k]);
												UIComponent pComp =formObject.getComponent(form_control);
												
												
												//below clause added by akshay for proc 11281
												if(Grid_col_tag_arr[k].equals("SELF-ATTEST FORM") || Grid_col_tag_arr[k].equals("ID DOC") || Grid_col_tag_arr[k].equals("W8")  || Grid_col_tag_arr[k].equals("W9")){
													//RLOS.mLogger.info("DB column name is equal to fatca doc code");
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
														//RLOS.mLogger.info(form_control+"-->"+((Column)(pComp.getChildren().get(k))).getName()+" is of date type");
														Grid_row.add("");
													}
													
													/*else if(newComp!=null && newComp instanceof ComboBox){
														Grid_row.add("");
													}---temporary added by akshay on 6/6/18 for inserting blank values in combo boxes instead of NA*/
													
													else{//text type column
														Grid_row.add("NA");
													}
													
													
												}
											
												
											}
										}
											flaga="N";
											flgYrs="N";

										}
										//Grid_row.add("true");
										Grid_row.add(formObject.getWFWorkitemName());
										String firstName = getTagValue(outputXMLMsg, "FName");
										String lastName = getTagValue(outputXMLMsg, "LName");
//										RLOS.mLogger.info("firstname is:"+firstName);
//										RLOS.mLogger.info("lastName is:"+lastName);
										if(Operation_name.equalsIgnoreCase("Primary_CIF")){
											Grid_row.add("P-"+firstName+" "+lastName);
										}
										else if(Operation_name.equalsIgnoreCase("Supplementary_Card_Details")){
											Grid_row.add("S-"+firstName+" "+lastName+"-"+formObject.getNGValue("passportNo"));
										}
										else if(Operation_name.equalsIgnoreCase("Guarantor_CIF")){
											Grid_row.add("G-"+firstName+" "+lastName);
										}
										else if(Operation_name.equalsIgnoreCase("Guarantor_CIF")){
											Grid_row.add("G-"+firstName+" "+lastName);
										}
										else if(Operation_name.equalsIgnoreCase("Corporation_CIF")){
											Grid_row.add("");
											Grid_row.add("");
										}
										//Grid_col_tag_arr[k]
										//code to add row in grid. and pass Grid_row in that.
										//RLOS.mLogger.info( "List to be added in "+form_control+" grid: "+ Grid_row.toString());

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

												//RLOS.mLogger.info( "New grid data to be added in Grid: "+str_newval );
												if(!"".equalsIgnoreCase(existingVal)){
													formObject.setNGValue(form_control,existingVal+"#"+str_newval);	
												}
												else{
													formObject.setNGValue(form_control,str_newval);
												}
												}
											}
										
										else if("AddrDet".equalsIgnoreCase(fielddbxml_tag)){ 
											if(!"swift".equalsIgnoreCase(Grid_row.get(0)))	
											{
												if("N".equals(grid_table_flag)){
												//added By Akshay on 28/6/17 for removing swift
												formObject.addItemFromList("cmplx_AddressDetails_cmplx_AddressGrid", Grid_row);
												//rowAddedinAddress =true;
												}
												
											}
										}	
										

										// added	 by yash on 10/7/2017 for getting values in worldcheck grid 
										else if("CustomerDetails".equalsIgnoreCase(fielddbxml_tag))
										{
											
								//			RLOS.mLogger.info("Form Control: "+form_control+" Grid data to be added for World check: "+ Grid_row);
											formObject.addItemFromList(form_control,Grid_row);
										}
										else { 
//											RLOS.mLogger.info("fieldxml_tag is:"+fielddbxml_tag+" form_control is:"+form_control);
//											RLOS.mLogger.info("Grid row is:"+Grid_row);
											formObject.addItemFromList(form_control, Grid_row);
										}

										//ended 10/07/2017 for worldcheck grid


									}
									else{
									//	RLOS.mLogger.info( "Grid mapping is Y for this tag and grid_table_xml_tags column is blank tag name: "+ fielddbxml_tag);
									}

								}
								else if("Y".equalsIgnoreCase(indirectMapping)){
							//		RLOS.mLogger.info("inside indirect mapping");
									String col_list = "xmltag_name,tag_value,indirect_tag_list,indirect_formfield_list,indirect_child_tag,form_control,indirect_val,IS_Master,Master_Name";
									//code added
									if(!("".equalsIgnoreCase(Operation_name) || Operation_name.equalsIgnoreCase(null))){   
//										RLOS.mLogger.info("operation111"+Operation_name);
//										RLOS.mLogger.info("callName111"+col_name);
										sQuery = "SELECT "+col_list+" FROM NG_RLOS_Integration_Indirect_Response with(nolock) where Call_Name ='"+response+"' and XmlTag_Name = '"+nl.item(i).getNodeName()+"' and Operation_name='"+Operation_name+"'" ;
								//		RLOS.mLogger.info( "sQuery "+sQuery);
									}

									else{
										sQuery = "SELECT "+col_list+" FROM NG_RLOS_Integration_Indirect_Response with(nolock) where Call_Name ='"+response+"' and XmlTag_Name = '"+nl.item(i).getNodeName()+"'";
								//		RLOS.mLogger.info( "query: "+sQuery);
									}
									List<List<String>> outputindirect=formObject.getNGDataFromDataCache(sQuery);
							//		RLOS.mLogger.info( "1");

									String col_list_arr[] = col_list.split(",");
									Map<String, String> gridResponseMap = new HashMap<String, String>();
									for(List<String> mygridlist:outputindirect)
									{
								//		RLOS.mLogger.info( "inside list loop");

										for(int x=0;x<col_list_arr.length;x++)
										{
											gridResponseMap.put(col_list_arr[x],mygridlist.get(x));
								//			RLOS.mLogger.info( "inside put map"+x);
										}
										String xmltag_name = gridResponseMap.get("xmltag_name");
										String tag_value = gridResponseMap.get("tag_value");
										String indirect_tag_list = gridResponseMap.get("indirect_tag_list");
										String indirect_formfield_list = gridResponseMap.get("indirect_formfield_list");
										String indirect_child_tag = gridResponseMap.get("indirect_child_tag");
										String indirect_form_control = gridResponseMap.get("form_control");
										String indirect_val = gridResponseMap.get("indirect_val");
										String IS_Master = gridResponseMap.get("IS_Master");
								//		RLOS.mLogger.info( "IS_Master"+IS_Master);
										String Master_Name = gridResponseMap.get("Master_Name");
								//		RLOS.mLogger.info( "Master_Name"+Master_Name);
								//		RLOS.mLogger.info( "all details fetched");
										if("Y".equalsIgnoreCase(IS_Master)){

											String code = nl.item(i).getTextContent();
								//			RLOS.mLogger.info(code);

											sQuery= "Select description from "+Master_Name+" with(nolock) where Code='"+code+"'";
								//			RLOS.mLogger.info("Query to select master value: "+  sQuery);
											List<List<String>> query=formObject.getNGDataFromDataCache(sQuery);
											String value=query.get(0).get(0);
											RLOS.mLogger.info(value);

									//		RLOS.mLogger.info("Query to select master value: "+  sQuery);
											formObject.setNGValue(indirect_form_control,value,false);
									//		RLOS.mLogger.info(formObject.getNGValue(indirect_form_control));
										}

										else if(indirect_form_control!=null && !"".equalsIgnoreCase(indirect_form_control))
										{
											if( nl.item(i).getTextContent().equalsIgnoreCase(tag_value)){
								//				RLOS.mLogger.info("Indirect value for "+xmltag_name+" Indirect control name: "+indirect_form_control+" final value: "+indirect_val+" tag_value: "+tag_value);
												formObject.setNGValue(indirect_form_control,indirect_val,false);
											}
											//RLOS.mLogger.info("form Control: "+ indirect_form_control + "indirect val: "+ indirect_val);

										}

										else{
										//	RLOS.mLogger.info("inside indirect mapping part2 else");
											if(indirect_tag_list!=null ){
												NodeList childnode  = nl.item(i).getChildNodes();
										//		RLOS.mLogger.info("childnode"+childnode);
												if(indirect_tag_list.contains(",")){
											//		RLOS.mLogger.info("inside indirect mapping part2 indirect_tag_list with ,");
													String indirect_tag_list_arr[] = indirect_tag_list.split(",");
													String indirect_formfield_list_arr[] = indirect_formfield_list.split(",");
													if(indirect_tag_list_arr.length == indirect_formfield_list_arr.length){
														for (int k=0; k<indirect_tag_list_arr.length;k++){
												//			RLOS.mLogger.info( "node name : "+childnode.item(0).getNodeName() +" node data: "+childnode.item(0).getTextContent());
															if(tag_value.equalsIgnoreCase(childnode.item(0).getTextContent()) && indirect_child_tag.equalsIgnoreCase(childnode.item(0).getNodeName())){
																for (int child_node_len = 1 ;child_node_len< childnode.getLength();child_node_len++){
												//					RLOS.mLogger.info( "child node IF: Child node value: "+ child_node_len+" name: "+ childnode.item(child_node_len).getNodeName()+" child node value: "+ childnode.item(child_node_len).getTextContent());

																	if(childnode.item(child_node_len).getNodeName().equalsIgnoreCase(indirect_tag_list_arr[k])){

//																		RLOS.mLogger.info("getTextContent"+childnode.item(child_node_len).getNodeName());
//																		RLOS.mLogger.info("getTextContent"+childnode.item(child_node_len).getTextContent());


															//			RLOS.mLogger.info(" :"+childnode.item(child_node_len).getTextContent());
																		formObject.setNGValue(indirect_formfield_list_arr[k],childnode.item(child_node_len).getTextContent(),false);
																	}
																}
															}
														}
													}
													else{
														//RLOS.mLogger.info("Error as for :"+xmltag_name+" indirect_formfield_list and indirect_tag_list dosent match");
													} 
												}
												else{
//													RLOS.mLogger.info("inside indirect mapping part2 indirect_tag_list without ,");
//													RLOS.mLogger.info( "tag_value: "+ tag_value+" indirect_child_tag: "+indirect_child_tag );
//													RLOS.mLogger.info( "textcontent: "+ childnode.item(0).getTextContent()+" NodeName: "+childnode.item(0).getNodeName() );
//													//changed for data populating in the Contact Details Frame
													if(tag_value.equalsIgnoreCase(childnode.item(0).getTextContent()) && indirect_child_tag.equalsIgnoreCase(childnode.item(0).getNodeName())){
														for (int child_node_len = 0 ;child_node_len< childnode.getLength();child_node_len++){
															//RLOS.mLogger.info( "child node IF: Child node value: "+ child_node_len+" name: "+ childnode.item(child_node_len).getNodeName()+" child node value: "+ childnode.item(child_node_len).getTextContent());
//
															if(childnode.item(child_node_len).getNodeName().equalsIgnoreCase(indirect_tag_list)){
//																RLOS.mLogger.info("getTextContent"+childnode.item(child_node_len).getNodeName());
//																RLOS.mLogger.info("getTextContent"+childnode.item(child_node_len).getTextContent());
//																RLOS.mLogger.info(" :"+childnode.item(child_node_len).getTextContent());
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
								//	RLOS.mLogger.info("check");

									tagValue = getTagValue(outputXMLMsg,nl.item(i).getNodeName());
//									RLOS.mLogger.info("tagValue:"+tagValue);
//									RLOS.mLogger.info("form_control:"+ form_control);
//
//									RLOS.mLogger.info(tagValue);
//
//									RLOS.mLogger.info(form_control);
									/*if("cmplx_FATCA_selectedreason".equalsIgnoreCase(form_control))
									{
										formObject.addItem(form_control,tagValue);
									}
									else{*/
									if("IsPremium".equalsIgnoreCase(fielddbxml_tag) && "Corporation_CIF".equalsIgnoreCase(Operation_name)){
										if("B".equalsIgnoreCase(tagValue)){
											tagValue = "true";													
										}
										else{
											tagValue = "false";
										}
									}
										formObject.setNGValue(form_control,tagValue,false);
									//}

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
			//RLOS.mLogger.info(e.getMessage());
			RLOS.logException(e);

		}
	}

}
