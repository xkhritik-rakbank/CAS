//----------------------------------------------------------------------------------------------------
//		NEWGEN SOFTWARE TECHNOLOGIES LIMITED
//Group						: AP2
//Product / Project			: RLOS
//Module					: CAS
//File Name					: RLOS_IntegrationInput.java
//Author					: Deepak Kumar
//Date written (DD/MM/YYYY)	: 06/08/2017
//Description				: 
//----------------------------------------------------------------------------------------------------


package com.newgen.omniforms.user;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.newgen.custom.Common_Utils;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.io.NGFReader;

public class RLOS_IntegrationInput extends RLOSCommon implements Serializable
{


	private static final long serialVersionUID = 1L;
	private String col_n = "call_type,Call_name,form_control,parent_tag_name,xmltag_name,is_repetitive,default_val,data_format";
	private String fin_call_name = "Customer_details, Customer_eligibility,new_customer_req,new_account_req,DEDUP_SUMMARY";
	private String capitalExceptionsTags = "EmploymentStatus";
	Common_Utils common=new Common_Utils(RLOS.mLogger);

	public String GenerateXML(String callName,String Operation_name)
	{
		StringBuilder final_xml= new StringBuilder("");
		String header ="";
		String footer = "";
		String parentTagName="";
		String sQuery=null;
		//RLOS.mLogger.info("before try");
		try
		{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			RLOS.mLogger.info("Inside GenerateXML:: callName: "+callName+" wi_name: "+ formObject.getWFWorkitemName()+ " user name: " +formObject.getUserName());
			String sQuery_header = "SELECT Header,Footer,parenttagname FROM NG_Integration with (nolock) where Call_name='"+callName+"'";
			//RLOS.mLogger.info("sQuery"+sQuery_header);
			List<List<String>> OutputXML_header=formObject.getDataFromDataSource(sQuery_header);
			if(!OutputXML_header.isEmpty()){
				//RLOS.mLogger.info(OutputXML_header.get(0).get(0)+" footer: "+OutputXML_header.get(0).get(1)+" parenttagname: "+OutputXML_header.get(0).get(2));
				header = OutputXML_header.get(0).get(0);
				footer = OutputXML_header.get(0).get(1);
				parentTagName = OutputXML_header.get(0).get(2);
				String col_n = "call_type,Call_name,form_control,parent_tag_name,xmltag_name,is_repetitive,default_val,data_format";

				if(!("".equalsIgnoreCase(Operation_name) || Operation_name ==null))
				{   
					//RLOS.mLogger.info("operation111"+Operation_name);
					//RLOS.mLogger.info("callName111"+callName);
					sQuery = "SELECT "+col_n +" FROM NG_Integration_field_Mapping with (nolock) where Call_name='"+callName+"' and active = 'Y' and Operation_name='"+Operation_name+"' ORDER BY tag_seq ASC" ;
					//RLOS.mLogger.info("sQuery "+sQuery);
				}
				else{
					//RLOS.mLogger.info("operation"+Operation_name);
					sQuery = "SELECT "+col_n +" FROM NG_Integration_field_Mapping with (nolock) where Call_name='"+callName+"' and active = 'Y' ORDER BY tag_seq ASC" ;
					//RLOS.mLogger.info("sQuery "+sQuery);
				}

				List<List<String>> DB_List=formObject.getNGDataFromDataCache(sQuery);
				//RLOS.mLogger.info("OutputXML"+DB_List);
				if(!DB_List.isEmpty()){

					//RLOS.mLogger.info(DB_List.get(0).get(0)+DB_List.get(0).get(1)+DB_List.get(0).get(2)+DB_List.get(0).get(3)+DB_List.get(0).get(4));
					//RLOS.mLogger.info(DB_List.get(0).get(0)+DB_List.get(0).get(1)+DB_List.get(0).get(2)+DB_List.get(0).get(3));


					//RLOS.mLogger.info(""+"column length"+col_n.length());
					Map<String, String> int_xml = new LinkedHashMap<String, String>();

					if ("DEDUP_SUMMARY".equalsIgnoreCase(callName)) {
						int_xml = DEDUP_SUMMARY_Custom(DB_List,formObject,callName);
					} else if ("NEW_CUSTOMER_REQ".equalsIgnoreCase(callName)) {
						int_xml = NEW_CUSTOMER_Custom(DB_List,formObject,callName);
					}  else if ("DECTECH".equalsIgnoreCase(callName)) {
						int_xml = DECTECH_Custom(DB_List,formObject,callName);
					} else if ("NEW_ACCOUNT_REQ".equalsIgnoreCase(callName)) {
						int_xml = NEW_ACCOUNT_Custom(DB_List,formObject,callName);
					} else if ("EID_Genuine".equalsIgnoreCase(callName)) {
						int_xml = EID_Genuine_Custom(DB_List,formObject,callName);
					}
					else{
						int_xml = GenDefault(DB_List,formObject,callName);
					}
					final_xml=final_xml.append("<").append(parentTagName).append(">");
					//RLOS.mLogger.info("RLOS"+"Final XMLold--"+final_xml);

					Iterator<Map.Entry<String,String>> itr = int_xml.entrySet().iterator();
					//RLOS.mLogger.info("itr of hashmap"+"itr"+itr);
					while (itr.hasNext())
					{
						Map.Entry<String, String> entry =  itr.next();
						//RLOS.mLogger.info("entry of hashmap"+"entry"+entry);
						if(final_xml.indexOf(entry.getKey())>-1){
							//RLOS.mLogger.info("RLOS"+"itr_value: Key: "+ entry.getKey()+" Value: "+entry.getValue());
							//prateek change 04-12-2017 : replace & with and to prevent parsing error
							final_xml = final_xml.insert(final_xml.indexOf("<"+entry.getKey()+">")+entry.getKey().length()+2, entry.getValue().replace("&", "and"));
							//RLOS.mLogger.info("value of final xml"+"final_xml"+final_xml);
							itr.remove();
						}
					}
					final_xml=final_xml.append("</").append(parentTagName).append(">");
					RLOS.mLogger.info("Inside GenerateXML xml creation finish:: callName: "+callName+" wi_name: "+ formObject.getWFWorkitemName()+ " user name: " +formObject.getUserName());
					final_xml = new StringBuilder( Clean_Xml(final_xml.toString(),callName));
					RLOS.mLogger.info("Inside GenerateXML xml clean finish:: callName: "+callName+" wi_name: "+ formObject.getWFWorkitemName()+ " user name: " +formObject.getUserName());
					//RLOS.mLogger.info("FInal XMLnew is: "+ final_xml);
					final_xml.insert(0, header);
					final_xml.append(footer);
					RLOS.mLogger.info("FInal XMLnew with header: "+ final_xml);
					formObject.setNGValue("Is_"+callName,"Y");
					//RLOS.mLogger.info("value of "+callName+" Flag: "+formObject.getNGValue("Is_"+callName));

					
					Integration_fragName(callName);
					RLOS.mLogger.info("before Mq response ");
					return MQ_connection_response(final_xml);
					//return dummyResponse(callName);
				}
				else {
					//RLOS.mLogger.info("Genrate XML: Entry is not maintained in field mapping Master table for : "+callName);
					return "Call not maintained";
				}
			}
			else{
				//RLOS.mLogger.info("Genrate XML: Entry is not maintained in Master table for : "+callName);
				return "Call not maintained";
			}
		}		
		catch(Exception e){
			//RLOS.mLogger.info("Generate XML: Exception ocurred: "+e.getLocalizedMessage());
			//RLOS.mLogger.info("$$final_xml: "+final_xml);
			//RLOS.mLogger.info("Generate XML:Exception occured in main thread: "+ printException(e));
			return "0";
		}
	}

	public String dummyResponse(String callName)
	{
		FormReference formObject=FormContext.getCurrentInstance().getFormReference();
		String prod="";
		int n=formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		if(n>0){
			prod=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
		}

		String response="";
		if(("CUSTOMER_DETAILS").equalsIgnoreCase(callName))
		{
			RLOS.mLogger.info(" Inside Customer_Details" +formObject.getNGValue("FrameName"));

			response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>CUSTOMER_DETAILS</MsgFormat><MsgVersion>0000</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS156724117904852</MessageId><Extra1>REP||LAXMANRET.LAXMANRET</Extra1><Extra2>2019-08-31T12:46:19.630+04:00</Extra2></EE_EAI_HEADER><FetchCustomerDetailsRes><BankId>RAK</BankId><CIFDet><CIFID>0508331</CIFID><RetCorpFlag>R</RetCorpFlag><CifType>EB</CifType><CustomerStatus>ACTVE</CustomerStatus><CustomerSegment>PBD</CustomerSegment><CustomerSubSeg>PBN</CustomerSubSeg><AECBConsentHeld>Y</AECBConsentHeld><IsStaff>N</IsStaff><IsPremium>N</IsPremium><IsWMCustomer>N</IsWMCustomer><BlackListFlag>N</BlackListFlag><NegativeListOvlFlag>N</NegativeListOvlFlag><CreditGradeCode>P2</CreditGradeCode><GCDNo>20318311</GCDNo><PhnDet><PhnType>CELLPH1</PhnType><PhnPrefFlag>Y</PhnPrefFlag><PhnCountryCode>00971</PhnCountryCode><PhnLocalCode>508101695</PhnLocalCode><PhoneNo>00971508101695</PhoneNo></PhnDet><PhnDet><PhnType>FAXO1</PhnType><PhnPrefFlag>N</PhnPrefFlag><PhnCountryCode>00971</PhnCountryCode><PhnLocalCode>720508331</PhnLocalCode><PhoneNo>00971720508331</PhoneNo></PhnDet><PhnDet><PhnType>HOMEPH1</PhnType><PhnPrefFlag>N</PhnPrefFlag><PhnCountryCode>00971</PhnCountryCode><PhnLocalCode>720508331</PhnLocalCode><PhoneNo>00971720508331</PhoneNo></PhnDet><PhnDet><PhnType>OFFCPH1</PhnType><PhnPrefFlag>N</PhnPrefFlag><PhnCountryCode>00971</PhnCountryCode><PhnLocalCode>720508331</PhnLocalCode><PhoneNo>00971720508331</PhoneNo></PhnDet><PhnDet><PhnType>OVHOMEPH</PhnType><PhnPrefFlag>N</PhnPrefFlag><PhnCountryCode>91</PhnCountryCode><PhnLocalCode>470508331</PhnLocalCode><PhoneNo>91470508331</PhoneNo></PhnDet><AddrDet><AddressType>RESIDENCE</AddressType><EffectiveFrom>2014-06-11</EffectiveFrom><EffectiveTo>2099-12-31</EffectiveTo><HoldMailFlag>N</HoldMailFlag><ReturnFlag>N</ReturnFlag><AddrPrefFlag>N</AddrPrefFlag><AddressLine1>12345</AddressLine1><AddressLine3>STREET NAME FOR         0508331</AddressLine3><AddressLine4>LOCALITY NAME FOR        0508331</AddressLine4><ResidenceType>R</ResidenceType><POBox>12345</POBox><City>RAK</City><Country>AE</Country></AddrDet><AddrDet><AddressType>OFFICE</AddressType><EffectiveFrom>2004-07-21</EffectiveFrom><EffectiveTo>2099-12-31</EffectiveTo><HoldMailFlag>N</HoldMailFlag><ReturnFlag>N</ReturnFlag><AddrPrefFlag>Y</AddrPrefFlag><AddressLine1>12345</AddressLine1><AddressLine2>PREMISE NAME FOR     0508331</AddressLine2><AddressLine3>STREET NAME FOR       0508331</AddressLine3><ResidenceType>R</ResidenceType><POBox>12345</POBox><City>RAK</City><Country>AE</Country></AddrDet><AddrDet><AddressType>Swift</AddressType><EffectiveFrom>2019-07-11</EffectiveFrom><EffectiveTo>2099-12-31</EffectiveTo><AddrPrefFlag>N</AddrPrefFlag></AddrDet><AddrDet><AddressType>Mailing</AddressType><EffectiveFrom>2004-07-21</EffectiveFrom><EffectiveTo>2099-12-31</EffectiveTo><HoldMailFlag>N</HoldMailFlag><ReturnFlag>N</ReturnFlag><AddrPrefFlag>N</AddrPrefFlag><AddressLine1>12345</AddressLine1><AddressLine2>PREMISE NAME FOR     0508331</AddressLine2><AddressLine3>STREET NAME FOR       0508331</AddressLine3><ResidenceType>R</ResidenceType><POBox>12345</POBox><City>RAK</City><Country>AE</Country></AddrDet><EmlDet><EmlType>ELML1</EmlType><EmlPrefFlag>Y</EmlPrefFlag><Email>123456789@RAKBANK.AE</Email></EmlDet><EmlDet><EmlType>ELML2</EmlType><EmlPrefFlag>N</EmlPrefFlag><Email>12345678912345@RAKBANK.AE</Email></EmlDet><EmlDet><EmlType>HOMEEML</EmlType><EmlPrefFlag>N</EmlPrefFlag><Email>12345678912345@RAKBANK.AE</Email></EmlDet><EmlDet><EmlType>WORKEML</EmlType><EmlPrefFlag>N</EmlPrefFlag><Email>123456789@RAKBANK.AE</Email></EmlDet><DocDet><DocType>EMID</DocType><DocTypeDesc>Emirates Id</DocTypeDesc><DocNo>784198508101695</DocNo><DocExpDate>2019-02-26</DocExpDate></DocDet><DocDet><DocType>PPT</DocType><DocTypeDesc>PASSPORT</DocTypeDesc><DocIsVerified>Y</DocIsVerified><DocNo>IN8101695</DocNo><DocIssDate>2014-06-25</DocIssDate><DocExpDate>2025-12-31</DocExpDate></DocDet><DocDet><DocType>VISA</DocType><DocTypeDesc>VISA FILE NUMBER</DocTypeDesc><DocIsVerified>Y</DocIsVerified><DocNo>601201720005395</DocNo><DocIssDate>2017-02-27</DocIssDate><DocExpDate>2019-02-26</DocExpDate></DocDet><BuddyRMDetails><BuddyRMName>PERSONAL BANKER</BuddyRMName><BuddyRMPhone>8004048</BuddyRMPhone></BuddyRMDetails><BackupRMDetails></BackupRMDetails><RetAddnlDet><Title>MR.</Title><ShortName>DAVIN MEND</ShortName><CustomerName>DAVIN MENDENHALL</CustomerName><FName>DAVIN</FName><LName>MENDENHALL</LName><Gender>M</Gender><DOB>1985-10-08</DOB><MinorFlg>N</MinorFlg><MaritialStatus>M</MaritialStatus><MotherMaidenName>MOM</MotherMaidenName><Nationality>IN</Nationality><ResidentCountry>AE</ResidentCountry><CustType>EB</CustType><NoOfDepndant>1</NoOfDepndant><SalaryTranflag>Y</SalaryTranflag><ResideSince>1999-01-01</ResideSince><CustomerNRIFlag>N</CustomerNRIFlag><EmpType>S</EmpType><EmployerCode>583683</EmployerCode><EmployerName>EMPLOYER NAME FOR      &amp;   583683</EmployerName><EmpStatus>2</EmpStatus><DOJ>2017-02-27</DOJ><Designation>LABOURER SUPERVISOR</Designation><CurrentJobPeriod>16</CurrentJobPeriod><GrossSalary>5.545E+3</GrossSalary><TotHouseholdInc>5.545E+3</TotHouseholdInc><InvIncome>0E+0</InvIncome><OthIncome>0E+0</OthIncome><Commissions>0E+0</Commissions><AssessedIncome>0E+0</AssessedIncome><HRA>0E+0</HRA><RentInc>0E+0</RentInc><MnthlyDispInc>5.545E+3</MnthlyDispInc><MnthlyHouseholdExp>0E+0</MnthlyHouseholdExp><AcctType>ODA</AcctType><AcctNum>0002508331001</AcctNum></RetAddnlDet><FatcaDet><USRelation>O</USRelation><DocumentsCollected>ID DOC!SELF-ATTEST FORM</DocumentsCollected></FatcaDet><KYCDet><KYCHeld>N</KYCHeld><KYCReviewDate>2020-07-30</KYCReviewDate><PEP>NPEP</PEP></KYCDet><OECDDet><CityOfBirth>KERALA</CityOfBirth><CountryOfBirth>IN</CountryOfBirth><CRSUnDocFlg>Y</CRSUnDocFlg><CRSUndocFlgReason>CIF UPDATE WITHIN UAE</CRSUndocFlgReason></OECDDet></CIFDet></FetchCustomerDetailsRes></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(("dectech").equalsIgnoreCase(callName)){
			RLOS.mLogger.info(" Inside detech" +formObject.getNGValue("FrameName"));

			if("Credit Card".equalsIgnoreCase(prod))
				response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Header><ServiceId>CallProcessManager</ServiceId><ServiceType>ProductEligibility</ServiceType><ServiceProviderId>DECTECH</ServiceProviderId><ServiceChannelId>CAS</ServiceChannelId><RequestID>CAS153302379542793</RequestID><TimeStampyyyymmddhhmmsss>2018-07-31T11:56:36.198</TimeStampyyyymmddhhmmsss><RequestLifeCycleStage>CallProcessManagerResponse</RequestLifeCycleStage><MessageStatus>Success</MessageStatus></soap:Header><soap:Body><CallProcessManagerResponse xmlns=\"http://tempuri.org/\"><CallProcessManagerResult><ProcessManagerResponse><Application><Channel>CC</Channel><CallType>PM</CallType><ApplicationNumber>CC-0030018313-process</ApplicationNumber><ReturnDateTime>20180731115636</ReturnDateTime><SystemErrorCode></SystemErrorCode><SystemErrorMessage></SystemErrorMessage><ReturnError RecordNumber=\"0\" /></Application><Instinct_Actions><Instinct_Action_Output_XML /></Instinct_Actions><PM_Results><Random_Number>437</Random_Number><DBR>0.00</DBR><Decision_Results name=\"MAC\"><PM_Decision_Results_Data><PM_Decision_Results>  <AppKey>CC-0030018313-processCC</AppKey>  <Decision_Objective>1</Decision_Objective>  <Decision_Sequence_Number>1</Decision_Sequence_Number>  <Last_Update_Date>2018-07-31T11:56:35.993+04:00</Last_Update_Date>  <Default_Decision>A</Default_Decision>  <Default_Reason>A999 - System Approve</Default_Reason>  <Default_Document>0</Default_Document>  <System_Decision>A</System_Decision>  <System_Document>0</System_Document>  <Decision_Node_Id>2.1</Decision_Node_Id>  <Decision_Test_Group>0</Decision_Test_Group></PM_Decision_Results></PM_Decision_Results_Data><PM_Reason_Codes_Data><PM_Reason_Codes>  <Decision_Objective>1</Decision_Objective>  <Decision_Sequence_Number>1</Decision_Sequence_Number>  <Sequence_Number>1</Sequence_Number>  <Reason_Decision>A</Reason_Decision>  <Reason_Code>A999</Reason_Code>  <Reason_Description>System Approve</Reason_Description>  <Criteria_Name />  <Letter_Code />  <Document />  <Letter_Reason /></PM_Reason_Codes></PM_Reason_Codes_Data></Decision_Results><Strategy_Results name=\"Eligibility\"><PM_Strategy_MaxLendingAmount_Data><PM_Strategy_MaxLendingAmount_Results>  <Policy_Number>1</Policy_Number>  <Adjustment_Number>0</Adjustment_Number>  <Max_Lending_Amount>100000.00</Max_Lending_Amount>  <Last_Update_Date>2018-07-31T11:56:36.04+04:00</Last_Update_Date>  <Entry_Name>Output TAI Multiplier 2 Max 100000</Entry_Name>  <Use_Requested_Limit_Flag>false</Use_Requested_Limit_Flag>  <Existing_Limit_Increase_Flag>false</Existing_Limit_Increase_Flag>  <Limit_Increase_Percent>0.00</Limit_Increase_Percent>  <Use_Characteristic_As_Limit_Flag>true</Use_Characteristic_As_Limit_Flag>  <Limit_Table_Id>1</Limit_Table_Id>  <Limit_Field_Number>22</Limit_Field_Number>  <Use_Characteristic_As_Limit_Multiplier>2.000</Use_Characteristic_As_Limit_Multiplier>  <Fixed_Amount_Flag>false</Fixed_Amount_Flag>  <Fixed_Amount>0</Fixed_Amount>  <Base_Percentage_Increase_Flag>false</Base_Percentage_Increase_Flag>  <Base_Percentage_Increase_Percent>0.00</Base_Percentage_Increase_Percent>  <Limit_Minimum>0</Limit_Minimum>  <Limit_Maximum>100000</Limit_Maximum>  <Limit_Rounding_Factor1>100</Limit_Rounding_Factor1>  <Limit_Rounding_Factor2>100</Limit_Rounding_Factor2>  <Limit_Rounding_Factor3>100</Limit_Rounding_Factor3>  <Limit_Rounding_Factor4>100</Limit_Rounding_Factor4>  <Limit_Rounding_Cutoff1>100000</Limit_Rounding_Cutoff1>  <Limit_Rounding_Cutoff2>200000</Limit_Rounding_Cutoff2>  <Limit_Rounding_Cutoff3>300000</Limit_Rounding_Cutoff3>  <Limit_Reason_Code />  <Max_Limit_Table_Id>0</Max_Limit_Table_Id>  <Max_Limit_Field_Number>0</Max_Limit_Field_Number>  <Max_Limit_Multiplier>0.00</Max_Limit_Multiplier>  <Use_Round_Up_Factor1_Flag>false</Use_Round_Up_Factor1_Flag>  <Use_Round_Up_Factor2_Flag>false</Use_Round_Up_Factor2_Flag>  <Use_Round_Up_Factor3_Flag>false</Use_Round_Up_Factor3_Flag>  <Use_Round_Up_Factor4_Flag>false</Use_Round_Up_Factor4_Flag>  <Strategy_Node_Id>1.2.1.3.1</Strategy_Node_Id>  <Strategy_Test_Group>0</Strategy_Test_Group>  <Tree_Error_Field_Label />  <Tree_Error_Field_Value /></PM_Strategy_MaxLendingAmount_Results></PM_Strategy_MaxLendingAmount_Data></Strategy_Results><Strategy_Results name=\"Interest Rate\"><PM_Strategy_PricingTerm_Data><PM_Strategy_PricingTerm_Results>  <Last_Update_Date>2018-07-31T11:56:36.057+04:00</Last_Update_Date>  <Entry_Name>Zero</Entry_Name>  <Interest_Rate>0.00</Interest_Rate>  <Max_Loan_Term>0</Max_Loan_Term>  <Strategy_Node_Id>2</Strategy_Node_Id>  <Strategy_Test_Group>0</Strategy_Test_Group></PM_Strategy_PricingTerm_Results></PM_Strategy_PricingTerm_Data></Strategy_Results><Scoring_Results name=\"Recalculation\"><PM_Scoring_Results_Data><PM_Scoring_Results>  <Scoring_Sequence_Number>1</Scoring_Sequence_Number>  <Scoring_Objective>2</Scoring_Objective>  <Last_Update_Date>2018-07-31T11:56:36.057+04:00</Last_Update_Date>  <Score>0.000000</Score>  <Score_Node_Id>1</Score_Node_Id>  <Scorecard_Id>Null Scorecard</Scorecard_Id>  <Grade>0</Grade>  <Score_Test_Group>0</Score_Test_Group></PM_Scoring_Results></PM_Scoring_Results_Data></Scoring_Results><Scoring_Results name=\"Application Score\"><PM_Scoring_Results_Data><PM_Scoring_Results>  <Scoring_Sequence_Number>1</Scoring_Sequence_Number>  <Scoring_Objective>1</Scoring_Objective>  <Last_Update_Date>2018-07-31T11:56:36.087+04:00</Last_Update_Date>  <Score>540.000000</Score>  <Score_Node_Id>2.1</Score_Node_Id>  <Scorecard_Id>CC App Scorecard</Scorecard_Id>  <Grade>0</Grade>  <Score_Test_Group>0</Score_Test_Group></PM_Scoring_Results></PM_Scoring_Results_Data></Scoring_Results><Decision_Results name=\"Eligibility Decision\"><PM_Decision_Results_Data><PM_Decision_Results>  <AppKey>CC-0030018313-processCC</AppKey>  <Decision_Objective>2</Decision_Objective>  <Decision_Sequence_Number>1</Decision_Sequence_Number>  <Last_Update_Date>2018-07-31T11:56:36.15+04:00</Last_Update_Date>  <Default_Decision>A</Default_Decision>  <Default_Reason>A999 - System Approve</Default_Reason>  <Default_Document>0</Default_Document>  <System_Decision>A</System_Decision>  <System_Document>0</System_Document>  <Decision_Node_Id>2.1</Decision_Node_Id>  <Decision_Test_Group>0</Decision_Test_Group></PM_Decision_Results></PM_Decision_Results_Data><PM_Reason_Codes_Data><PM_Reason_Codes>  <Decision_Objective>2</Decision_Objective>  <Decision_Sequence_Number>1</Decision_Sequence_Number>  <Sequence_Number>1</Sequence_Number>  <Reason_Decision>A</Reason_Decision>  <Reason_Code>A999</Reason_Code>  <Reason_Description>System Approve</Reason_Description>  <Criteria_Name />  <Letter_Code />  <Document />  <Letter_Reason /></PM_Reason_Codes></PM_Reason_Codes_Data></Decision_Results><Strategy_Results name=\"Additional Eligible Cards\"><PM_Strategy_Verification_Data><PM_Strategy_Verification_Results>  <Last_Update_Date>2018-07-31T11:56:36.18+04:00</Last_Update_Date>  <Entry_Name>Eligible Cards UAE</Entry_Name>  <Number_Of_Visits>0</Number_Of_Visits><Visits><Code>4</Code><Type_Of_Visit>KALYAN-UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>9</Code><Type_Of_Visit>MPL-UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>14</Code><Type_Of_Visit>MRBH GOLD UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>19</Code><Type_Of_Visit>MRBH PLTM UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>26</Code><Type_Of_Visit>MRBH WORLD UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>31</Code><Type_Of_Visit>MY RAK CARD-UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>36</Code><Type_Of_Visit>MY RAK ISLAMIC-UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>40</Code><Type_Of_Visit>REDCARD-UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>51</Code><Type_Of_Visit>TITANIUM-UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>55</Code><Type_Of_Visit>VCLASSIC-UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>60</Code><Type_Of_Visit>VGOLD-UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>65</Code><Type_Of_Visit>WORLD-UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits>  <Number_Of_Calls>0</Number_Of_Calls><Calls><Code></Code><Type_Of_Call></Type_Of_Call><Mandatory_Flag></Mandatory_Flag></Calls>  <Strategy_Node_Id>2.1.2.2.4.1</Strategy_Node_Id>  <Strategy_Test_Group>0</Strategy_Test_Group></PM_Strategy_Verification_Results></PM_Strategy_Verification_Data></Strategy_Results></PM_Results><PM_Outputs><Application><Output_Accommodation_Allowance>0.00</Output_Accommodation_Allowance><Output_Additional_Amount>36400.00</Output_Additional_Amount><Output_Affordable_EMI>43839.00</Output_Affordable_EMI><Output_CPV_Waiver>N</Output_CPV_Waiver><Output_Decision>A</Output_Decision><Output_Delegation_Authority>SR. MGR</Output_Delegation_Authority><Output_Eligible_Amount>100000.00</Output_Eligible_Amount><Output_Eligible_Amount_Path>Max Lending Amount Sub Process Type Segmentation CC [SAL]\\NOT Employee type SP or not confirmed in job with LOS LT 6\\LOS Segmentation [&amp;gt;= 24]\\No Months AECB History [0]\\Output TAI Segmentation [&amp;gt;= 35000]</Output_Eligible_Amount_Path><Output_Eligible_Cards>[{\"Credit_Limit\":48600.00,\"Flag\":\"E\"},{\"Card_Product\":\"MY RAK CARD-UAE\",\"Credit_Limit\":85000.00,\"Flag\":\"N\"}]</Output_Eligible_Cards><Output_Existing_DBR>3.6194</Output_Existing_DBR><Output_Final_Amount>85000.00</Output_Final_Amount><Output_Final_DBR>4.3896</Output_Final_DBR><Output_Interest_Rate>0.00</Output_Interest_Rate><Output_Net_Salary_DBR>6.3831</Output_Net_Salary_DBR><Output_Num_Eligible_Cards>8</Output_Num_Eligible_Cards><Output_Salary_Multiples>0.90</Output_Salary_Multiples><Output_TAI>94520.00</Output_TAI></Application></PM_Outputs></ProcessManagerResponse></CallProcessManagerResult></CallProcessManagerResponse></soap:Body></soap:Envelope></MQ_RESPONSE_XML></APMQPUTGET_Output>";
			else
				response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Header><ServiceId>CallProcessManager</ServiceId><ServiceType>ProductEligibility</ServiceType><ServiceProviderId>DECTECH</ServiceProviderId><ServiceChannelId>CAS</ServiceChannelId><RequestID>CAS153226201324026</RequestID><TimeStampyyyymmddhhmmsss>2018-07-22T16:20:13.874</TimeStampyyyymmddhhmmsss><RequestLifeCycleStage>CallProcessManagerResponse</RequestLifeCycleStage><MessageStatus>Success</MessageStatus></soap:Header><soap:Body><CallProcessManagerResponse xmlns=\"http://tempuri.org/\"><CallProcessManagerResult><ProcessManagerResponse><Application><Channel>PL</Channel><CallType>PM</CallType><ApplicationNumber>PL-0040008398-process</ApplicationNumber><ReturnDateTime>20180722162013</ReturnDateTime><SystemErrorCode></SystemErrorCode><SystemErrorMessage></SystemErrorMessage><ReturnError RecordNumber=\"0\" /></Application><Instinct_Actions><Instinct_Action_Output_XML /></Instinct_Actions><PM_Results><Random_Number>351</Random_Number><DBR>0.00</DBR><Decision_Results name=\"MAC\"><PM_Decision_Results_Data><PM_Decision_Results>  <AppKey>PL-0040008398-processPL</AppKey>  <Decision_Objective>4</Decision_Objective>  <Decision_Sequence_Number>1</Decision_Sequence_Number>  <Last_Update_Date>2018-07-22T16:20:13.623+04:00</Last_Update_Date>  <Default_Decision>A</Default_Decision>  <Default_Reason>A999 - System Approve</Default_Reason>  <Default_Document>0</Default_Document>  <System_Decision>R</System_Decision>  <System_Document>0</System_Document>  <Decision_Node_Id>2.2.1</Decision_Node_Id>  <Decision_Test_Group>0</Decision_Test_Group></PM_Decision_Results></PM_Decision_Results_Data><PM_Reason_Codes_Data><PM_Reason_Codes>  <Decision_Objective>4</Decision_Objective>  <Decision_Sequence_Number>1</Decision_Sequence_Number>  <Sequence_Number>1</Sequence_Number>  <Reason_Decision>R</Reason_Decision>  <Reason_Code>RC025</Reason_Code>  <Reason_Description>IB - Not Currently Current</Reason_Description>  <Criteria_Name>PL Internal Performance_001</Criteria_Name>  <Letter_Code />  <Document />  <Letter_Reason />  <Category>C - Internal Performance</Category></PM_Reason_Codes></PM_Reason_Codes_Data></Decision_Results><Strategy_Results name=\"Eligibility\"><PM_Strategy_MaxLendingAmount_Data><PM_Strategy_MaxLendingAmount_Results>  <Policy_Number>1</Policy_Number>  <Adjustment_Number>0</Adjustment_Number>  <Max_Lending_Amount>400000.00</Max_Lending_Amount>  <Last_Update_Date>2018-07-22T16:20:13.7+04:00</Last_Update_Date>  <Entry_Name>PL TAI x10 Unlimited</Entry_Name>  <Use_Requested_Limit_Flag>false</Use_Requested_Limit_Flag>  <Existing_Limit_Increase_Flag>false</Existing_Limit_Increase_Flag>  <Limit_Increase_Percent>0.00</Limit_Increase_Percent>  <Use_Characteristic_As_Limit_Flag>true</Use_Characteristic_As_Limit_Flag>  <Limit_Table_Id>1</Limit_Table_Id>  <Limit_Field_Number>22</Limit_Field_Number>  <Use_Characteristic_As_Limit_Multiplier>10.000</Use_Characteristic_As_Limit_Multiplier>  <Fixed_Amount_Flag>false</Fixed_Amount_Flag>  <Fixed_Amount>0</Fixed_Amount>  <Base_Percentage_Increase_Flag>false</Base_Percentage_Increase_Flag>  <Base_Percentage_Increase_Percent>0.00</Base_Percentage_Increase_Percent>  <Limit_Minimum>0</Limit_Minimum>  <Limit_Maximum>999999999</Limit_Maximum>  <Limit_Rounding_Factor1>1</Limit_Rounding_Factor1>  <Limit_Rounding_Factor2>1</Limit_Rounding_Factor2>  <Limit_Rounding_Factor3>1</Limit_Rounding_Factor3>  <Limit_Rounding_Factor4>1</Limit_Rounding_Factor4>  <Limit_Rounding_Cutoff1>100000</Limit_Rounding_Cutoff1>  <Limit_Rounding_Cutoff2>200000</Limit_Rounding_Cutoff2>  <Limit_Rounding_Cutoff3>300000</Limit_Rounding_Cutoff3>  <Limit_Reason_Code />  <Max_Limit_Table_Id>0</Max_Limit_Table_Id>  <Max_Limit_Field_Number>0</Max_Limit_Field_Number>  <Max_Limit_Multiplier>0.00</Max_Limit_Multiplier>  <Use_Round_Up_Factor1_Flag>false</Use_Round_Up_Factor1_Flag>  <Use_Round_Up_Factor2_Flag>false</Use_Round_Up_Factor2_Flag>  <Use_Round_Up_Factor3_Flag>false</Use_Round_Up_Factor3_Flag>  <Use_Round_Up_Factor4_Flag>false</Use_Round_Up_Factor4_Flag>  <Strategy_Node_Id>1.1.1.2.2.2</Strategy_Node_Id>  <Strategy_Test_Group>0</Strategy_Test_Group>  <Tree_Error_Field_Label />  <Tree_Error_Field_Value /></PM_Strategy_MaxLendingAmount_Results></PM_Strategy_MaxLendingAmount_Data></Strategy_Results><Strategy_Results name=\"Max Loan Amount\"><PM_Strategy_MaxLendingAmount_Data><PM_Strategy_MaxLendingAmount_Results>  <Policy_Number>1</Policy_Number>  <Adjustment_Number>0</Adjustment_Number>  <Max_Lending_Amount>0.00</Max_Lending_Amount>  <Last_Update_Date>2018-07-22T16:20:13.717+04:00</Last_Update_Date>  <Entry_Name>Zero</Entry_Name>  <Use_Requested_Limit_Flag>false</Use_Requested_Limit_Flag>  <Existing_Limit_Increase_Flag>false</Existing_Limit_Increase_Flag>  <Limit_Increase_Percent>0.00</Limit_Increase_Percent>  <Use_Characteristic_As_Limit_Flag>false</Use_Characteristic_As_Limit_Flag>  <Limit_Table_Id>0</Limit_Table_Id>  <Limit_Field_Number>0</Limit_Field_Number>  <Use_Characteristic_As_Limit_Multiplier>0.000</Use_Characteristic_As_Limit_Multiplier>  <Fixed_Amount_Flag>true</Fixed_Amount_Flag>  <Fixed_Amount>0</Fixed_Amount>  <Base_Percentage_Increase_Flag>false</Base_Percentage_Increase_Flag>  <Base_Percentage_Increase_Percent>0.00</Base_Percentage_Increase_Percent>  <Limit_Minimum>0</Limit_Minimum>  <Limit_Maximum>0</Limit_Maximum>  <Limit_Rounding_Factor1>100</Limit_Rounding_Factor1>  <Limit_Rounding_Factor2>100</Limit_Rounding_Factor2>  <Limit_Rounding_Factor3>100</Limit_Rounding_Factor3>  <Limit_Rounding_Factor4>100</Limit_Rounding_Factor4>  <Limit_Rounding_Cutoff1>100000</Limit_Rounding_Cutoff1>  <Limit_Rounding_Cutoff2>200000</Limit_Rounding_Cutoff2>  <Limit_Rounding_Cutoff3>300000</Limit_Rounding_Cutoff3>  <Limit_Reason_Code />  <Max_Limit_Table_Id>0</Max_Limit_Table_Id>  <Max_Limit_Field_Number>0</Max_Limit_Field_Number>  <Max_Limit_Multiplier>0.00</Max_Limit_Multiplier>  <Use_Round_Up_Factor1_Flag>false</Use_Round_Up_Factor1_Flag>  <Use_Round_Up_Factor2_Flag>false</Use_Round_Up_Factor2_Flag>  <Use_Round_Up_Factor3_Flag>false</Use_Round_Up_Factor3_Flag>  <Use_Round_Up_Factor4_Flag>false</Use_Round_Up_Factor4_Flag>  <Strategy_Node_Id>1</Strategy_Node_Id>  <Strategy_Test_Group>0</Strategy_Test_Group>  <Tree_Error_Field_Label />  <Tree_Error_Field_Value /></PM_Strategy_MaxLendingAmount_Results></PM_Strategy_MaxLendingAmount_Data></Strategy_Results><Strategy_Results name=\"Max Exposure\"><PM_Strategy_MaxLendingAmount_Data><PM_Strategy_MaxLendingAmount_Results>  <Policy_Number>1</Policy_Number>  <Adjustment_Number>0</Adjustment_Number>  <Max_Lending_Amount>0.00</Max_Lending_Amount>  <Last_Update_Date>2018-07-22T16:20:13.747+04:00</Last_Update_Date>  <Entry_Name>Zero</Entry_Name>  <Use_Requested_Limit_Flag>false</Use_Requested_Limit_Flag>  <Existing_Limit_Increase_Flag>false</Existing_Limit_Increase_Flag>  <Limit_Increase_Percent>0.00</Limit_Increase_Percent>  <Use_Characteristic_As_Limit_Flag>false</Use_Characteristic_As_Limit_Flag>  <Limit_Table_Id>0</Limit_Table_Id>  <Limit_Field_Number>0</Limit_Field_Number>  <Use_Characteristic_As_Limit_Multiplier>0.000</Use_Characteristic_As_Limit_Multiplier>  <Fixed_Amount_Flag>true</Fixed_Amount_Flag>  <Fixed_Amount>0</Fixed_Amount>  <Base_Percentage_Increase_Flag>false</Base_Percentage_Increase_Flag>  <Base_Percentage_Increase_Percent>0.00</Base_Percentage_Increase_Percent>  <Limit_Minimum>0</Limit_Minimum>  <Limit_Maximum>0</Limit_Maximum>  <Limit_Rounding_Factor1>100</Limit_Rounding_Factor1>  <Limit_Rounding_Factor2>100</Limit_Rounding_Factor2>  <Limit_Rounding_Factor3>100</Limit_Rounding_Factor3>  <Limit_Rounding_Factor4>100</Limit_Rounding_Factor4>  <Limit_Rounding_Cutoff1>100000</Limit_Rounding_Cutoff1>  <Limit_Rounding_Cutoff2>200000</Limit_Rounding_Cutoff2>  <Limit_Rounding_Cutoff3>300000</Limit_Rounding_Cutoff3>  <Limit_Reason_Code />  <Max_Limit_Table_Id>0</Max_Limit_Table_Id>  <Max_Limit_Field_Number>0</Max_Limit_Field_Number>  <Max_Limit_Multiplier>0.00</Max_Limit_Multiplier>  <Use_Round_Up_Factor1_Flag>false</Use_Round_Up_Factor1_Flag>  <Use_Round_Up_Factor2_Flag>false</Use_Round_Up_Factor2_Flag>  <Use_Round_Up_Factor3_Flag>false</Use_Round_Up_Factor3_Flag>  <Use_Round_Up_Factor4_Flag>false</Use_Round_Up_Factor4_Flag>  <Strategy_Node_Id>1</Strategy_Node_Id>  <Strategy_Test_Group>0</Strategy_Test_Group>  <Tree_Error_Field_Label />  <Tree_Error_Field_Value /></PM_Strategy_MaxLendingAmount_Results></PM_Strategy_MaxLendingAmount_Data></Strategy_Results><Strategy_Results name=\"Interest Rate\"><PM_Strategy_PricingTerm_Data><PM_Strategy_PricingTerm_Results>  <Last_Update_Date>2018-07-22T16:20:13.747+04:00</Last_Update_Date>  <Entry_Name>Fixed 6.49</Entry_Name>  <Interest_Rate>6.49</Interest_Rate>  <Max_Loan_Term>0</Max_Loan_Term>  <Strategy_Node_Id>2.1.2</Strategy_Node_Id>  <Strategy_Test_Group>0</Strategy_Test_Group></PM_Strategy_PricingTerm_Results></PM_Strategy_PricingTerm_Data></Strategy_Results><Scoring_Results name=\"Recalculation\"><PM_Scoring_Results_Data><PM_Scoring_Results>  <Scoring_Sequence_Number>1</Scoring_Sequence_Number>  <Scoring_Objective>4</Scoring_Objective>  <Last_Update_Date>2018-07-22T16:20:13.763+04:00</Last_Update_Date>  <Score>0.000000</Score>  <Score_Node_Id>1</Score_Node_Id>  <Scorecard_Id>Null Scorecard</Scorecard_Id>  <Grade>0</Grade>  <Score_Test_Group>0</Score_Test_Group></PM_Scoring_Results></PM_Scoring_Results_Data></Scoring_Results><Scoring_Results name=\"Application Score\"><PM_Scoring_Results_Data /></Scoring_Results><Decision_Results name=\"Eligibility Decision\"><PM_Decision_Results_Data><PM_Decision_Results>  <AppKey>PL-0040008398-processPL</AppKey>  <Decision_Objective>3</Decision_Objective>  <Decision_Sequence_Number>1</Decision_Sequence_Number>  <Last_Update_Date>2018-07-22T16:20:13.827+04:00</Last_Update_Date>  <Default_Decision>A</Default_Decision>  <Default_Reason>A999 - System Approve</Default_Reason>  <Default_Document>0</Default_Document>  <System_Decision>A</System_Decision>  <System_Document>0</System_Document>  <Decision_Node_Id>2.2.1</Decision_Node_Id>  <Decision_Test_Group>0</Decision_Test_Group></PM_Decision_Results></PM_Decision_Results_Data><PM_Reason_Codes_Data><PM_Reason_Codes>  <Decision_Objective>3</Decision_Objective>  <Decision_Sequence_Number>1</Decision_Sequence_Number>  <Sequence_Number>1</Sequence_Number>  <Reason_Decision>A</Reason_Decision>  <Reason_Code>A999</Reason_Code>  <Reason_Description>System Approve</Reason_Description>  <Criteria_Name />  <Letter_Code />  <Document />  <Letter_Reason /></PM_Reason_Codes></PM_Reason_Codes_Data></Decision_Results><Strategy_Results name=\"Additional Eligible Cards\"><PM_Strategy_Verification_Data><PM_Strategy_Verification_Results>  <Last_Update_Date>2018-07-22T16:20:13.84+04:00</Last_Update_Date>  <Entry_Name>Eligible Cards UAE</Entry_Name>  <Number_Of_Visits>0</Number_Of_Visits><Visits><Code>4</Code><Type_Of_Visit>KALYAN-UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>9</Code><Type_Of_Visit>MPL-UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>14</Code><Type_Of_Visit>MRBH GOLD UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>19</Code><Type_Of_Visit>MRBH PLTM UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>26</Code><Type_Of_Visit>MRBH WORLD UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>31</Code><Type_Of_Visit>MY RAK CARD-UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>36</Code><Type_Of_Visit>MY RAK ISLAMIC-UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>40</Code><Type_Of_Visit>REDCARD-UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>51</Code><Type_Of_Visit>TITANIUM-UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>55</Code><Type_Of_Visit>VCLASSIC-UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>60</Code><Type_Of_Visit>VGOLD-UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>65</Code><Type_Of_Visit>WORLD-UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits>  <Number_Of_Calls>0</Number_Of_Calls><Calls><Code></Code><Type_Of_Call></Type_Of_Call><Mandatory_Flag></Mandatory_Flag></Calls>  <Strategy_Node_Id>1.2.1</Strategy_Node_Id>  <Strategy_Test_Group>0</Strategy_Test_Group></PM_Strategy_Verification_Results></PM_Strategy_Verification_Data></Strategy_Results></PM_Results><PM_Outputs><Application><Output_Accommodation_Allowance>0.00</Output_Accommodation_Allowance><Output_Additional_Amount>100000.00</Output_Additional_Amount><Output_Affordable_EMI>20000.00</Output_Affordable_EMI><Output_CPV_Waiver>N</Output_CPV_Waiver><Output_Decision>R</Output_Decision><Output_Delegation_Authority>AM</Output_Delegation_Authority><Output_Eligible_Amount>400000.00</Output_Eligible_Amount><Output_Eligible_Amount_Path>PL Limit Matrix Sub Process Type [NAT-NEW/TOP/TKO]\\PL App Is Referred OR Cat B Declined\\PL Employer Is MOD\\NOT PL Is Pensioner\\PL Age Segmentation [Adult]\\PL Employer Contract Type Segmentation [Government]</Output_Eligible_Amount_Path><Output_Eligible_Cards>[{\"Card_Product\":\"TITANIUM-UAE\",\"Credit_Limit\":32500.00,\"Flag\":\"E\"},{\"Card_Product\":\"KALYAN-UAE\",\"Credit_Limit\":32500.00,\"Flag\":\"E\"},{\"Card_Product\":\"MPL-UAE\",\"Credit_Limit\":0.00,\"Flag\":\"N\"},{\"Card_Product\":\"MRBH GOLD UAE\",\"Credit_Limit\":0.00,\"Flag\":\"N\"},{\"Card_Product\":\"MRBH PLTM UAE\",\"Credit_Limit\":0.00,\"Flag\":\"N\"},{\"Card_Product\":\"MRBH WORLD UAE\",\"Credit_Limit\":0.00,\"Flag\":\"N\"},{\"Card_Product\":\"MY RAK CARD-UAE\",\"Credit_Limit\":0.00,\"Flag\":\"N\"},{\"Card_Product\":\"MY RAK ISLAMIC-UAE\",\"Credit_Limit\":0.00,\"Flag\":\"N\"},{\"Card_Product\":\"REDCARD-UAE\",\"Credit_Limit\":0.00,\"Flag\":\"N\"},{\"Card_Product\":\"VCLASSIC-UAE\",\"Credit_Limit\":0.00,\"Flag\":\"N\"},{\"Card_Product\":\"VGOLD-UAE\",\"Credit_Limit\":0.00,\"Flag\":\"N\"},{\"Card_Product\":\"WORLD-UAE\",\"Credit_Limit\":0.00,\"Flag\":\"N\"}]</Output_Eligible_Cards><Output_Existing_DBR>0.0000</Output_Existing_DBR><Output_Final_Amount>100000.00</Output_Final_Amount><Output_Final_DBR>5.9276</Output_Final_DBR><Output_Interest_Rate>6.49</Output_Interest_Rate><Output_Net_Salary_DBR>5.9276</Output_Net_Salary_DBR><Output_Num_Eligible_Cards>7</Output_Num_Eligible_Cards><Output_Salary_Multiples>2.50</Output_Salary_Multiples><Output_TAI>40000.00</Output_TAI></Application></PM_Outputs></ProcessManagerResponse></CallProcessManagerResult></CallProcessManagerResponse></soap:Body></soap:Envelope></MQ_RESPONSE_XML></APMQPUTGET_Output>";

		}
		else if(("FINANCIAL_SUMMARY").equalsIgnoreCase(callName)){
			response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>FINANCIAL_SUMMARY</MsgFormat><MsgVersion>0000</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153415827022195</MessageId><Extra1>REP||LAXMANRET.LAXMANRET</Extra1><Extra2>2018-08-13T03:04:30.812+04:00</Extra2></EE_EAI_HEADER><FinancialSummaryRes><BankId>RAK</BankId><CIFID>0103147</CIFID><AcctId>0014103147002</AcctId><OperationType>AVGBALDET</OperationType><OperationDesc>AVERAGE BALANCE</OperationDesc><TxnSummary></TxnSummary><AvgBalanceDtls><Month>JUL</Month><AvgBalance>3163.93</AvgBalance></AvgBalanceDtls><AvgBalanceDtls><Month>JUN</Month><AvgBalance>3632.73</AvgBalance></AvgBalanceDtls><AvgBalanceDtls><Month>MAY</Month><AvgBalance>1374.64</AvgBalance></AvgBalanceDtls><AvgBalanceDtls><Month>APR</Month><AvgBalance>610.43</AvgBalance></AvgBalanceDtls><AvgBalanceDtls><Month>MAR</Month><AvgBalance>1183.5</AvgBalance></AvgBalanceDtls><AvgBalanceDtls><Month>FEB</Month><AvgBalance>1047.47</AvgBalance></AvgBalanceDtls></FinancialSummaryRes></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(("CUSTOMER_ELIGIBILITY").equalsIgnoreCase(callName)){
			response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>CUSTOMER_ELIGIBILITY</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS156724117698583</MessageId><Extra1>REP||BPM.123</Extra1><Extra2>2019-08-31T12:46:18.077+04:00</Extra2></EE_EAI_HEADER><CustomerEligibilityResponse><BankId>RAK</BankId><CustomerDetails><SearchType>Internal</SearchType><CustId>0508331</CustId><PassportNum>IN8101695</PassportNum><BlacklistFlag>Y</BlacklistFlag><DuplicationFlag>N</DuplicationFlag><NegatedFlag>N</NegatedFlag><Products><ProductType>ACCOUNT ONLY</ProductType><NoOfProducts>1</NoOfProducts></Products><Products><ProductType>CAPS</ProductType><NoOfProducts>1</NoOfProducts></Products></CustomerDetails></CustomerEligibilityResponse></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(("CUSTOMER_EXPOSURE").equalsIgnoreCase(callName)){
			response="<MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>CUSTOMER_EXPOSURE</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153416576411240</MessageId><Extra1>REP||SHELL.JOHN</Extra1><Extra2>2018-08-13T05:09:25.164+04:00</Extra2></EE_EAI_HEADER><CustomerExposureResponse><RequestType>InternalExposure</RequestType><IsDirect>Y</IsDirect><CustInfo><CustId><CustIdType>CIF Id</CustIdType><CustIdValue>0232626</CustIdValue></CustId><FullNm></FullNm><BirthDt>1973-06-29</BirthDt><Nationality>INDIAN</Nationality><CustSegment>PERSONAL BANKING</CustSegment><CustSubSegment>PB - NORMAL</CustSubSegment><RMName>PERSONAL BANKER</RMName><CreditGrade>P2 - PERSONAL - ACCEPTABLE CREDIT</CreditGrade><ECRN>038275800</ECRN><BorrowingCustomer>N</BorrowingCustomer></CustInfo><ProductExposureDetails><LoanDetails><AgreementId>20424288</AgreementId><LoanStat>A</LoanStat><LoanType>PL</LoanType><LoanDesc></LoanDesc><TotalNoOfInstalments>48</TotalNoOfInstalments><KeyDt><KeyDtType>LoanApprovedDate</KeyDtType><KeyDtValue>2017-11-06</KeyDtValue></KeyDt><KeyDt><KeyDtType>LoanMaturityDate</KeyDtType><KeyDtValue>2021-11-15</KeyDtValue></KeyDt><CurCode>AED</CurCode><AmountDtls><AmtType>TotalLoanAmount</AmtType><Amt>241000</Amt></AmountDtls><AmountDtls><AmtType>TotalOutstandingAmt</AmtType><Amt>220687.84</Amt></AmountDtls><AmountDtls><AmtType>NextInstallmentAmt</AmtType><Amt>6018</Amt></AmountDtls></LoanDetails><CardDetails><CardEmbossNum>038275800</CardEmbossNum><CardStatus>NORM</CardStatus><CardType>TITANIUM-EXPAT</CardType><CustRoleType>Primary</CustRoleType><KeyDt><KeyDtType>ApplicationCreationDate</KeyDtType><KeyDtValue>2015-11-09</KeyDtValue></KeyDt><KeyDt><KeyDtType>ExpiryDate</KeyDtType><KeyDtValue>2025-01-31</KeyDtValue></KeyDt><CurCode></CurCode><AmountDtls><AmtType>CreditLimit</AmtType><Amt>39000</Amt></AmountDtls><AmountDtls><AmtType>OutstandingAmt</AmtType><Amt>-9256.23</Amt></AmountDtls><AmountDtls><AmtType>OverdueAmt</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>CurrMaxUtil</AmtType><Amt>23.73</Amt></AmountDtls></CardDetails><CardDetails><CardEmbossNum>038275801</CardEmbossNum><CardStatus>NORM</CardStatus><CardType>TITANIUM-EXPAT</CardType><CustRoleType>Secondary</CustRoleType><KeyDt><KeyDtType>ApplicationCreationDate</KeyDtType><KeyDtValue>2015-11-09</KeyDtValue></KeyDt><KeyDt><KeyDtType>ExpiryDate</KeyDtType><KeyDtValue>2020-01-31</KeyDtValue></KeyDt><CurCode></CurCode><AmountDtls><AmtType>CreditLimit</AmtType><Amt>2000</Amt></AmountDtls><AmountDtls><AmtType>OutstandingAmt</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>OverdueAmt</AmtType><Amt>0</Amt></AmountDtls></CardDetails><AcctDetails><AcctId>0002232626001</AcctId><IBANNumber>AE120400000002232626001</IBANNumber><AcctStat>ACTIVE</AcctStat><AcctCur>AED</AcctCur><AcctNm>GERLIE UAT RAMACHANDRAN</AcctNm><AcctType>CURRENT ACCOUNT</AcctType><AcctSegment>PBD</AcctSegment><AcctSubSegment>PBN</AcctSubSegment><CustRoleType>Main</CustRoleType><KeyDt><KeyDtType>AccountOpenDate</KeyDtType><KeyDtValue>2008-11-16</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitSactionDate</KeyDtType><KeyDtValue>2008-11-16</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitExpiryDate</KeyDtType><KeyDtValue>2012-06-08</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitStartDate</KeyDtType><KeyDtValue>2008-11-16</KeyDtValue></KeyDt><AmountDtls><AmtType>AvailableBalance</AmtType><Amt>847.30</Amt></AmountDtls><AmountDtls><AmtType>ClearBalanceAmount</AmtType><Amt>847.3</Amt></AmountDtls><AmountDtls><AmtType>LedgerBalance</AmtType><Amt>847.30</Amt></AmountDtls><AmountDtls><AmtType>EffectiveAvailableBalance</AmtType><Amt>847.30</Amt></AmountDtls><AmountDtls><AmtType>CumulativeDebitAmount</AmtType><Amt>1444110.31</Amt></AmountDtls><AmountDtls><AmtType>SanctionLimit</AmtType><Amt>0</Amt></AmountDtls><WriteoffStat>Y</WriteoffStat><WorstDelay24Months>P2</WorstDelay24Months><MonthsOnBook>117.00</MonthsOnBook><LastRepmtDt>AUG</LastRepmtDt><IsCurrent>Y</IsCurrent><ChargeOffFlag>N</ChargeOffFlag><DelinquencyInfo><BucketType>DaysPastDue</BucketType><BucketValue>0</BucketValue></DelinquencyInfo></AcctDetails></ProductExposureDetails></CustomerExposureResponse></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(("NEW_CUSTOMER_REQ").equalsIgnoreCase(callName)){
			response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>NEW_CUSTOMER_REQ</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RBDUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS15341561637020</MessageId><Extra1>REP||CAS.123</Extra1><Extra2>2018-08-13T02:29:26.540+04:00</Extra2></EE_EAI_HEADER><NewCustomerResponse><BankId>RAK</BankId><CIFId>2677277</CIFId><Desc>Retail Customer successfully created with CIFID 2677277</Desc><Entity>Retail Customer</Entity><Service>CIFRetailCustomerCreate</Service><Status>Success</Status></NewCustomerResponse></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(("DEDUP_SUMMARY").equalsIgnoreCase(callName)){
			response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>DEDUP_SUMMARY</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>TABUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153415690250761</MessageId><Extra1>REP||BPM.123</Extra1><Extra2>2018-08-13T02:41:44.691+04:00</Extra2></EE_EAI_HEADER><CustomerDuplicationListResponse><BankId>RAK</BankId><Customer><CIFID>0103147</CIFID><RetailCorpFlag>R</RetailCorpFlag><CustomerType>EB</CustomerType><EntityType>ALL</EntityType><StepsMatched>CIF ID</StepsMatched><PersonDetails><FirstName>FIRST NAME FOR  0103147</FirstName><MiddleName>MIDDLE NAME FOR 0103147</MiddleName><LastName>LAST NAME FOR 0103147</LastName><ShortName>ST 0103147</ShortName><FullName>FIRST NAME FOR  0103147 MIDDLE NAME FOR 0103147 LAST NAME FOR 0103147</FullName><MaritalStatus>M</MaritalStatus><Nationality>AE</Nationality><DateOfBirth>1988-01-01</DateOfBirth></PersonDetails><Document><DocumentType>PPT</DocumentType><DocumentRefNumber>AE0103147</DocumentRefNumber></Document><Document><DocumentType>EMID</DocumentType><DocumentRefNumber>784200010103147</DocumentRefNumber></Document><ContactDetails><EmailAddress><MailIdType>HOMEEML</MailIdType><MailIdValue>12345@RAKBANK.AE</MailIdValue></EmailAddress><PhoneFax><PhoneType>CELLPH1</PhoneType><PhoneValue>00971500103147</PhoneValue></PhoneFax><PhoneFax><PhoneType>FAXO1</PhoneType><PhoneValue>00971370103147</PhoneValue></PhoneFax><PhoneFax><PhoneType>HOMEPH1</PhoneType><PhoneValue>00971370103147</PhoneValue></PhoneFax><PhoneFax><PhoneType>OFFCPH1</PhoneType><PhoneValue>00971260103147</PhoneValue></PhoneFax></ContactDetails><StatusInfo><StatusType>Blacklisted</StatusType><StatusFlag>N</StatusFlag></StatusInfo><StatusInfo><StatusType>Negativelisted</StatusType><StatusFlag>Y</StatusFlag><ReasonCode>CHQRT</ReasonCode><ReasonNotes>4 CHEQUE RETURN</ReasonNotes><ReasonStatus>Y</ReasonStatus><CreationDate>2014-07-01</CreationDate><CreatedBy>COPS</CreatedBy></StatusInfo><StatusInfo><StatusType>Suspended</StatusType><StatusFlag>N</StatusFlag></StatusInfo></Customer><Customer><CIFID>0103147</CIFID><RetailCorpFlag>R</RetailCorpFlag><CustomerType>EB</CustomerType><EntityType>ALL</EntityType><StepsMatched>NAME_PASSPORT_DOB,PASSPORT</StepsMatched><PersonDetails><FirstName>FIRST NAME FOR  0103147</FirstName><MiddleName>MIDDLE NAME FOR 0103147</MiddleName><LastName>LAST NAME FOR 0103147</LastName><ShortName>ST 0103147</ShortName><FullName>FIRST NAME FOR  0103147 MIDDLE NAME FOR 0103147 LAST NAME FOR 0103147</FullName><MaritalStatus>M</MaritalStatus><Nationality>AE</Nationality><DateOfBirth>1988-01-01</DateOfBirth></PersonDetails><Document><DocumentType>PPT</DocumentType><DocumentRefNumber>AE0103147</DocumentRefNumber></Document><Document><DocumentType>EMID</DocumentType><DocumentRefNumber>784200010103147</DocumentRefNumber></Document><ContactDetails><EmailAddress><MailIdType>HOMEEML</MailIdType><MailIdValue>12345@RAKBANK.AE</MailIdValue></EmailAddress><PhoneFax><PhoneType>CELLPH1</PhoneType><PhoneValue>00971500103147</PhoneValue></PhoneFax><PhoneFax><PhoneType>FAXO1</PhoneType><PhoneValue>00971370103147</PhoneValue></PhoneFax><PhoneFax><PhoneType>HOMEPH1</PhoneType><PhoneValue>00971370103147</PhoneValue></PhoneFax><PhoneFax><PhoneType>OFFCPH1</PhoneType><PhoneValue>00971260103147</PhoneValue></PhoneFax></ContactDetails><StatusInfo><StatusType>Blacklisted</StatusType><StatusFlag>N</StatusFlag></StatusInfo><StatusInfo><StatusType>Negativelisted</StatusType><StatusFlag>Y</StatusFlag><ReasonCode>CHQRT</ReasonCode><ReasonNotes>4 CHEQUE RETURN</ReasonNotes><ReasonStatus>Y</ReasonStatus><CreationDate>2014-07-01</CreationDate><CreatedBy>COPS</CreatedBy></StatusInfo><StatusInfo><StatusType>Suspended</StatusType><StatusFlag>N</StatusFlag></StatusInfo></Customer></CustomerDuplicationListResponse></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(("BLACKLIST_DETAILS").equalsIgnoreCase(callName)){
			response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>BLACKLIST_DETAILS</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153415691394885</MessageId><Extra1>REP||BPM.123</Extra1><Extra2>2018-08-13T02:41:54.681+04:00</Extra2></EE_EAI_HEADER><CustomerBlackListResponse><BankId>RAK</BankId><Customer><CIFID>0103147</CIFID><RetailCorpFlag>R</RetailCorpFlag><PersonDetails><FirstName>FIRST NAME FOR  0103147</FirstName><MiddleName>MIDDLE NAME FOR 0103147</MiddleName><LastName>LAST NAME FOR 0103147</LastName><DateOfBirth>1988-01-01</DateOfBirth></PersonDetails><CustomerStatus>ACTVE</CustomerStatus><CustDormStatus>N</CustDormStatus><Document><DocumentType>EMID</DocumentType><DocumentRefNumber>784200010103147</DocumentRefNumber></Document><Document><DocumentType>PPT</DocumentType><DocumentRefNumber>AE0103147</DocumentRefNumber><DocumentDescription>PASSPORT</DocumentDescription></Document><ContactDetails><PhoneFax><PhoneType>CELLPH1</PhoneType><PhoneValue>00971500103147</PhoneValue></PhoneFax><PhoneFax><PhoneType>FAXO1</PhoneType><PhoneValue>00971370103147</PhoneValue></PhoneFax><PhoneFax><PhoneType>HOMEPH1</PhoneType><PhoneValue>00971370103147</PhoneValue></PhoneFax><PhoneFax><PhoneType>OFFCPH1</PhoneType><PhoneValue>00971260103147</PhoneValue></PhoneFax></ContactDetails><StatusInfo><StatusType>Black List</StatusType><StatusFlag>N</StatusFlag><StatusOverAllFlag>N</StatusOverAllFlag><StatusList></StatusList></StatusInfo><StatusInfo><StatusType>Negative List</StatusType><StatusFlag>Y</StatusFlag><StatusNotes>4 CHEQUE RETURN</StatusNotes><StatusReason>4 CHEQUE RETURN</StatusReason><StatusOverAllFlag>Y</StatusOverAllFlag><StatusList><StatusDetails><ProductType>FINACLECORE</ProductType><ReferenceNumber>103147</ReferenceNumber><Unit>0103147</Unit><ReasonNotes>FOUR CHQS RETURNED AS PER EMAIL DATED 23/12/2008 AHHANADI 25/12/2008 10:33:43WALI 30/12/2008 12:34:01 [4 Cheque Return Negation Cancelled on 21-DEC-2014 in line with CB guideline of blacklist removal after 2 Years]</ReasonNotes><ReasonCodeDesc>MIGRATION DEFAULT</ReasonCodeDesc><ReasonCode>MIGR</ReasonCode><CreationDate>2008-12-25</CreationDate><CreatedBy>COPS</CreatedBy><Status>Y</Status></StatusDetails><StatusDetails><ProductType>FINACLECORE</ProductType><ReferenceNumber>0014103147001</ReferenceNumber><Unit>0103147</Unit><ReasonNotes>4 CHEQUE RETURN</ReasonNotes><ReasonCodeDesc>4 CHEQUE RETURN</ReasonCodeDesc><ReasonCode>CHQRT</ReasonCode><CreationDate>2014-07-01</CreationDate><CreatedBy>COPS</CreatedBy><Status>Y</Status></StatusDetails><StatusDetails><ProductType>FINACLECORE</ProductType><ReferenceNumber>0014103147001</ReferenceNumber><Unit>0103147</Unit><ReasonNotes>4 CHEQUE RETURN</ReasonNotes><ReasonCodeDesc>4 CHEQUE RETURN</ReasonCodeDesc><ReasonCode>CHQRT</ReasonCode><CreationDate>2014-07-01</CreationDate><CreatedBy>COPS</CreatedBy><Status>Y</Status></StatusDetails></StatusList></StatusInfo></Customer></CustomerBlackListResponse></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}		else if(("CARD_INSTALLMENT_DETAILS").equalsIgnoreCase(callName)){
			response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>CARD_INSTALLMENT_DETAILS</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>1</ReturnCode><ReturnDesc>PRMSD : This account does not exist in database</ReturnDesc><MessageId>CAS153415820216963</MessageId><Extra1>REP||BPM.123</Extra1><Extra2>2018-08-13T03:03:22.996+04:00</Extra2></EE_EAI_HEADER></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(("CUSTOMER_UPDATE_REQ").equalsIgnoreCase(callName)){
			response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>CUSTOMER_UPDATE_REQ</MsgFormat><MsgVersion>001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153416189541995</MessageId><Extra1>REP||SHELL.dfgJOHN</Extra1><Extra2>2018-08-13T04:04:58.969+04:00</Extra2></EE_EAI_HEADER><CustomerDetailsUpdateRes><BankId>RAK</BankId><CIFId>0103147</CIFId><Status>S</Status><Message>SUCCESS</Message></CustomerDetailsUpdateRes></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(("NEW_CREDITCARD_REQ").equalsIgnoreCase(callName)){
			response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>NEW_CREDITCARD_REQ</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>PRMSD : Success</ReturnDesc><MessageId>CAS153416190773281</MessageId><Extra1>REP||SHELL.JOHN</Extra1><Extra2>2018-08-13T04:05:16.776+04:00</Extra2></EE_EAI_HEADER></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(("CHEQUE_BOOK_ELIGIBILITY").equalsIgnoreCase(callName)){
			response ="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>CHEQUE_BOOK_ELIGIBILITY</MsgFormat><MsgVersion>0000</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153407822315923</MessageId><Extra1>REP||INFY.JOHN</Extra1><Extra2>2018-08-12T04:50:25.274+04:00</Extra2></EE_EAI_HEADER><ChequeBookRes><ReferenceId>1168819</ReferenceId></ChequeBookRes></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(("CARD_NOTIFICATION").equalsIgnoreCase(callName)){
			response ="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>CARD_NOTIFICATION</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153286447685654</MessageId><Extra1>REP||BPM.123</Extra1><Extra2>2018-07-29T03:41:18.186+04:00</Extra2></EE_EAI_HEADER></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(("ENTITY_MAINTENANCE_REQ").equalsIgnoreCase(callName)){
			response ="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>ENTITY_MAINTENANCE_REQ</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153286850527510</MessageId><Extra1>REP||RBD.123</Extra1><Extra2>2018-07-29T04:48:26.794+04:00</Extra2></EE_EAI_HEADER><UpdateCifAndAccStatusRes><OperDesc>AcctAct</OperDesc><SuccessFlag>S</SuccessFlag></UpdateCifAndAccStatusRes></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		/*
		 commented by sagarika-duplicate condition
		 else if(("BLACKLIST_DETAILS").equalsIgnoreCase(callName)){
			response = "<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>BLACKLIST_DETAILS</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153286226647936</MessageId><Extra1>REP||BPM.123</Extra1><Extra2>2018-07-29T03:04:27.285+04:00</Extra2></EE_EAI_HEADER><CustomerBlackListResponse><BankId>RAK</BankId><Customer><CIFID>2676337</CIFID><RetailCorpFlag>R</RetailCorpFlag><PersonDetails><FirstName>TRISTAN</FirstName><LastName>GROVER</LastName><DateOfBirth>1985-01-01</DateOfBirth></PersonDetails><CustomerStatus>ACTVE</CustomerStatus><CustDormStatus>N</CustDormStatus><Document><DocumentType>PPT</DocumentType><DocumentRefNumber>A5623541</DocumentRefNumber></Document><Document><DocumentType>VISA</DocumentType><DocumentRefNumber>5162534162551</DocumentRefNumber></Document><Document><DocumentType>EMID</DocumentType><DocumentRefNumber>784198565314652</DocumentRefNumber></Document><ContactDetails><PhoneFax><PhoneType>CELLPH1</PhoneType><PhoneValue>97100971</PhoneValue></PhoneFax><PhoneFax><PhoneType>HOMEPH1</PhoneType><PhoneValue>97100971</PhoneValue></PhoneFax><PhoneFax><PhoneType>OFFCPH1</PhoneType><PhoneValue>97100971</PhoneValue></PhoneFax></ContactDetails><StatusInfo><StatusType>Black List</StatusType><StatusFlag>N</StatusFlag><StatusOverAllFlag>N</StatusOverAllFlag><StatusList></StatusList></StatusInfo><StatusInfo><StatusType>Negative List</StatusType><StatusFlag>N</StatusFlag><StatusOverAllFlag>N</StatusOverAllFlag><StatusList></StatusList></StatusInfo></Customer></CustomerBlackListResponse></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";

		}*/
		else if(("ACCOUNT_MAINTENANCE_REQ").equalsIgnoreCase(callName)){
			response = "<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>ACCOUNT_MAINTENANCE_REQ</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153286850693168</MessageId><Extra1>REP||SHELL.JOHN</Extra1><Extra2>2018-07-29T04:48:27.336+04:00</Extra2></EE_EAI_HEADER><modifyAccountDetailsRes><OperDesc>Account Name Modification</OperDesc></modifyAccountDetailsRes></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(("NEW_DEBITCARD_REQ").equalsIgnoreCase(callName)){
			response = "<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>NEW_DEBITCARD_REQ</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153286852165021</MessageId><Extra1>REP||SHELL.JOHN</Extra1><Extra2>2018-07-29T04:48:44.451+04:00</Extra2></EE_EAI_HEADER><DebitCardRequest><CustomerId>2676881</CustomerId><RequestId>Personal-705841</RequestId><SuccessFlag>S</SuccessFlag><ErrorCode>SUCCESS</ErrorCode><ErrorDesc>SUCCESS</ErrorDesc></DebitCardRequest></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";;
		}
		else if(("ACCOUNT_SUMMARY").equalsIgnoreCase(callName)){
			response = "<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>ACCOUNT_SUMMARY</MsgFormat><MsgVersion>0000</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153250382510523</MessageId><Extra1>REP||LAXMANRET.LAXMANRET</Extra1><Extra2>2018-07-25T11:30:25.620+04:00</Extra2></EE_EAI_HEADER><FetchIBSAccountListRes><CIFID>2221180</CIFID><BankId>RAK</BankId><StatusAsOf>2018-07-25T00:00:00.000+04:00</StatusAsOf><IsIslamic>I</IsIslamic><IBSAccountDetail><BranchId>902</BranchId><Acid>20331583</Acid><AcctType>LAA</AcctType><AccountCategory>ENP1</AccountCategory><CrnCode>AED</CrnCode><NicName>2221180</NicName><AccountName>KPNZ#UIPNBT#KPIO</AccountName><AcctBal>0</AcctBal><LoanAmt>71250</LoanAmt><JntAcctIndicator>N</JntAcctIndicator><AcctStatus>002</AcctStatus><AcctAccess>N</AcctAccess><AcctOpnDt>2015-09-20</AcctOpnDt><ProductId>AMAL PERSONAL FINANCE </ProductId><ModeOfOperation>SINGLY</ModeOfOperation></IBSAccountDetail><IBSAccountDetail><BranchId>902</BranchId><Acid>20359906</Acid><AcctType>LAA</AcctType><AccountCategory>ENP2</AccountCategory><CrnCode>AED</CrnCode><NicName>2221180</NicName><AccountName>KPNZ#UIPNBT#KPIO</AccountName><AcctBal>66633.9</AcctBal><LoanAmt>113000</LoanAmt><JntAcctIndicator>N</JntAcctIndicator><AcctStatus>001</AcctStatus><AcctAccess>N</AcctAccess><AcctOpnDt>2016-06-02</AcctOpnDt><ProductId>AMAL PERSONAL FINANCE </ProductId><ModeOfOperation>SINGLY</ModeOfOperation></IBSAccountDetail><IBSAccountDetail><BranchId>902</BranchId><Acid>40005178</Acid><AcctType>LAA</AcctType><AccountCategory>CNP1</AccountCategory><CrnCode>AED</CrnCode><NicName>2221180</NicName><AccountName>KPNZ#UIPNBT#KPIO</AccountName><AcctBal>200787.73</AcctBal><LoanAmt>200000</LoanAmt><JntAcctIndicator>N</JntAcctIndicator><AcctStatus>001</AcctStatus><AcctAccess>N</AcctAccess><AcctOpnDt>2018-07-02</AcctOpnDt><ProductId>AMAL PERSONAL FINANCE </ProductId><ModeOfOperation>SINGLY</ModeOfOperation></IBSAccountDetail></FetchIBSAccountListRes></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		/*commented by sagarika-duplicate condition
		 * else if(("DEDUP_SUMMARY").equalsIgnoreCase(callName)){
			response = "<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>DEDUP_SUMMARY</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>TABUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153288278033261</MessageId><Extra1>REP||BPM.123</Extra1><Extra2>2018-07-29T08:46:49.468+04:00</Extra2></EE_EAI_HEADER><CustomerDuplicationListResponse><BankId>RAK</BankId><Customer><CIFID>2562401</CIFID><RetailCorpFlag>R</RetailCorpFlag><CustomerType>EB</CustomerType><EntityType>ALL</EntityType><StepsMatched>CIF ID</StepsMatched><PersonDetails><FirstName>FIRST NAME FOR  2562401</FirstName><LastName>LAST NAME FOR 2562401</LastName><ShortName>G U R</ShortName><FullName>FIRST NAME FOR  2562401  LAST NAME FOR 2562401</FullName><MaritalStatus>M</MaritalStatus><Nationality>IN</Nationality><DateOfBirth>1988-01-01</DateOfBirth></PersonDetails><Document><DocumentType>PPT</DocumentType><DocumentRefNumber>PK0002321</DocumentRefNumber></Document><Document><DocumentType>VISA</DocumentType><DocumentRefNumber>40120090004933</DocumentRefNumber></Document><Document><DocumentType>EMID</DocumentType><DocumentRefNumber>784199200002321</DocumentRefNumber></Document><ContactDetails><EmailAddress><MailIdType>ELML1</MailIdType><MailIdValue>1234567891234@RAKBANK.AE</MailIdValue></EmailAddress><EmailAddress><MailIdType>ELML2</MailIdType><MailIdValue>1234567891234@RAKBANK.AE</MailIdValue></EmailAddress><EmailAddress><MailIdType>HOMEEML</MailIdType><MailIdValue>1234567891234@RAKBANK.AE</MailIdValue></EmailAddress><EmailAddress><MailIdType>WORKEML</MailIdType><MailIdValue>1234567891234@RAKBANK.AE</MailIdValue></EmailAddress><PhoneFax><PhoneType>CELLPH1</PhoneType><PhoneValue>971522562401</PhoneValue></PhoneFax><PhoneFax><PhoneType>FAXO1</PhoneType><PhoneValue>00971672562401</PhoneValue></PhoneFax><PhoneFax><PhoneType>HOMEPH1</PhoneType><PhoneValue>971792562401</PhoneValue></PhoneFax><PhoneFax><PhoneType>OFFCPH1</PhoneType><PhoneValue>971672562401</PhoneValue></PhoneFax><PhoneFax><PhoneType>OVHOMEPH</PhoneType><PhoneValue>0091962562401</PhoneValue></PhoneFax></ContactDetails><StatusInfo><StatusType>Blacklisted</StatusType><StatusFlag>N</StatusFlag></StatusInfo><StatusInfo><StatusType>Negativelisted</StatusType><StatusFlag>N</StatusFlag></StatusInfo><StatusInfo><StatusType>Suspended</StatusType><StatusFlag>N</StatusFlag></StatusInfo></Customer><Customer><CIFID>2562401</CIFID><RetailCorpFlag>R</RetailCorpFlag><CustomerType>EB</CustomerType><EntityType>ALL</EntityType><StepsMatched>NAME,NAME_PASSPORT_DOB,PASSPORT,VISA</StepsMatched><PersonDetails><FirstName>FIRST NAME FOR  2562401</FirstName><LastName>LAST NAME FOR 2562401</LastName><ShortName>G U R</ShortName><FullName>FIRST NAME FOR  2562401  LAST NAME FOR 2562401</FullName><MaritalStatus>M</MaritalStatus><Nationality>IN</Nationality><DateOfBirth>1988-01-01</DateOfBirth></PersonDetails><Document><DocumentType>PPT</DocumentType><DocumentRefNumber>PK0002321</DocumentRefNumber></Document><Document><DocumentType>VISA</DocumentType><DocumentRefNumber>40120090004933</DocumentRefNumber></Document><Document><DocumentType>EMID</DocumentType><DocumentRefNumber>784199200002321</DocumentRefNumber></Document><ContactDetails><EmailAddress><MailIdType>ELML1</MailIdType><MailIdValue>1234567891234@RAKBANK.AE</MailIdValue></EmailAddress><EmailAddress><MailIdType>ELML2</MailIdType><MailIdValue>1234567891234@RAKBANK.AE</MailIdValue></EmailAddress><EmailAddress><MailIdType>HOMEEML</MailIdType><MailIdValue>1234567891234@RAKBANK.AE</MailIdValue></EmailAddress><EmailAddress><MailIdType>WORKEML</MailIdType><MailIdValue>1234567891234@RAKBANK.AE</MailIdValue></EmailAddress><PhoneFax><PhoneType>CELLPH1</PhoneType><PhoneValue>971522562401</PhoneValue></PhoneFax><PhoneFax><PhoneType>FAXO1</PhoneType><PhoneValue>00971672562401</PhoneValue></PhoneFax><PhoneFax><PhoneType>HOMEPH1</PhoneType><PhoneValue>971792562401</PhoneValue></PhoneFax><PhoneFax><PhoneType>OFFCPH1</PhoneType><PhoneValue>971672562401</PhoneValue></PhoneFax><PhoneFax><PhoneType>OVHOMEPH</PhoneType><PhoneValue>0091962562401</PhoneValue></PhoneFax></ContactDetails><StatusInfo><StatusType>Blacklisted</StatusType><StatusFlag>N</StatusFlag></StatusInfo><StatusInfo><StatusType>Negativelisted</StatusType><StatusFlag>N</StatusFlag></StatusInfo><StatusInfo><StatusType>Suspended</StatusType><StatusFlag>N</StatusFlag></StatusInfo></Customer></CustomerDuplicationListResponse></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";

		}*/
		else if(("NEW_LOAN_REQ").equalsIgnoreCase(callName)){
			response = "<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>NEW_LOAN_REQ</MsgFormat><MsgVersion>0</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS15328686886040</MessageId><Extra1>REP||SHELL.JOHN</Extra1><Extra2>2018-07-29T04:51:50.677+04:00</Extra2></EE_EAI_HEADER><ContractCreationRes><contractID>902CNP1182100003</contractID><StatusCode>H</StatusCode><Description>Contract Created Successfully</Description></ContractCreationRes></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		/*else if(("CUSTOMER_UPDATE_REQ").equalsIgnoreCase(callName)){
			response = "<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>CUSTOMER_UPDATE_REQ</MsgFormat><MsgVersion>001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS15328684850094</MessageId><Extra1>REP||SHELL.dfgJOHN</Extra1><Extra2>2018-07-29T04:48:08.597+04:00</Extra2></EE_EAI_HEADER><CustomerDetailsUpdateRes><BankId>RAK</BankId><CIFId>2676881</CIFId><Status>S</Status><Message>SUCCESS</Message></CustomerDetailsUpdateRes></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}*/// Commented for Sonar
		else if(("CARD_SUMMARY").equalsIgnoreCase(callName)){
			response = "<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>CARD_SUMMARY</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>CASUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153241540837975</MessageId><Extra1>REP||BPM.123</Extra1><Extra2>2018-07-24T10:56:50.956+04:00</Extra2></EE_EAI_HEADER><CardSummaryResponse><BankId>RAK</BankId><CIFID>2221180</CIFID><CardCRNNumber>087749600</CardCRNNumber><IsCombinedCreditLimit>N</IsCombinedCreditLimit><CardDetails><CardNumber>5227856098400006</CardNumber><PrimaryCardHolderName>EMB_NAME_069534100</PrimaryCardHolderName><CardType>primary</CardType><CardProductType>AMAL-CLASSIC-I</CardProductType><CardStatus>NA</CardStatus><CardAsIsStatus>NORM</CardAsIsStatus><ExpiryDate>2020-01-31</ExpiryDate><LastStatementDate>2018-05-16</LastStatementDate><TotalAmtDue>-6278.94</TotalAmtDue><MinAmtDue>-303.09</MinAmtDue><TotalPayments>0</TotalPayments><LastPayment>300</LastPayment><CurrentMinAmtDue>-303.09</CurrentMinAmtDue><PymtDueDate>2018-06-10</PymtDueDate><TotalCreditLimit>6200</TotalCreditLimit><OverDueAmt>0</OverDueAmt><OutstandingBalance>-6278.94</OutstandingBalance><NextStatementDate>2018-06-16</NextStatementDate></CardDetails><CardDetails><CardNumber>1531007389202004</CardNumber><PrimaryCardHolderName>EMB_NAME_074587800</PrimaryCardHolderName><CardType>primary</CardType><CardProductType>LOC STANDARD</CardProductType><CardStatus>NA</CardStatus><CardAsIsStatus>CLSB</CardAsIsStatus><ExpiryDate>2020-01-31</ExpiryDate><LastStatementDate>1900-01-01</LastStatementDate><TotalAmtDue>0</TotalAmtDue><MinAmtDue>0</MinAmtDue><TotalPayments>0</TotalPayments><LastPayment>0</LastPayment><CurrentMinAmtDue>0</CurrentMinAmtDue><PymtDueDate>1900-01-01</PymtDueDate><TotalCreditLimit>18000</TotalCreditLimit><OverDueAmt>0</OverDueAmt><OutstandingBalance>0</OutstandingBalance><NextStatementDate>1900-02-01</NextStatementDate></CardDetails><CardDetails><CardNumber>1531004636412007</CardNumber><PrimaryCardHolderName>EMB_NAME_087749600</PrimaryCardHolderName><CardType>primary</CardType><CardProductType>LOC STANDARD</CardProductType><CardStatus>NA</CardStatus><CardAsIsStatus>NORM</CardAsIsStatus><ExpiryDate>2020-01-31</ExpiryDate><LastStatementDate>2018-05-16</LastStatementDate><TotalAmtDue>-582.05</TotalAmtDue><MinAmtDue>-582.05</MinAmtDue><TotalPayments>0</TotalPayments><LastPayment>600</LastPayment><CurrentMinAmtDue>-582.05</CurrentMinAmtDue><PymtDueDate>2018-06-10</PymtDueDate><TotalCreditLimit>18400</TotalCreditLimit><OverDueAmt>0</OverDueAmt><OutstandingBalance>-14972.22</OutstandingBalance><NextStatementDate>2018-06-16</NextStatementDate></CardDetails></CardSummaryResponse></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(("SIGNATURE_DETAILS").equalsIgnoreCase(callName)){
			response = "<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>SIGNATURE_DETAILS</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>SVS-101</ReturnCode><ReturnDesc>FIN : SVSInquiry: signautreMain: NO MATCHED RECORD FOUND FOR THE GIVEN ACCOUNT INFORMATIONSVS-101: N</ReturnDesc><MessageId>CAS152517834066848</MessageId><Extra1>REP||CAS.123</Extra1><Extra2>2018-05-01T04:39:03.426+04:00</Extra2></EE_EAI_HEADER></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(callName.indexOf("InternalExposure")>-1)
		{
			response = "<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>CUSTOMER_EXPOSURE</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153379503251654</MessageId><Extra1>REP||SHELL.JOHN</Extra1><Extra2>2018-08-09T10:10:35.712+04:00</Extra2></EE_EAI_HEADER><CustomerExposureResponse><RequestType>InternalExposure</RequestType><IsDirect>Y</IsDirect><CustInfo><CustId><CustIdType>CIF Id</CustIdType><CustIdValue>0536177</CustIdValue></CustId><FullNm></FullNm><BirthDt>1988-01-01</BirthDt><Nationality>INDIAN</Nationality><CustSegment>PERSONAL BANKING</CustSegment><CustSubSegment>PB - NORMAL</CustSubSegment><RMName>PERSONAL BANKER</RMName><CreditGrade>R6 - BUSINESS - SCORE 20-34</CreditGrade><ECRN>010790200</ECRN><BorrowingCustomer>N</BorrowingCustomer></CustInfo><ProductExposureDetails><CardDetails><CardEmbossNum>010790200</CardEmbossNum><CardStatus>STWO</CardStatus><CardType>VCLASSIC-EXPATR</CardType><CustRoleType>Primary</CustRoleType><KeyDt><KeyDtType>ApplicationCreationDate</KeyDtType><KeyDtValue>2012-06-09</KeyDtValue></KeyDt><KeyDt><KeyDtType>ExpiryDate</KeyDtType><KeyDtValue>2025-01-31</KeyDtValue></KeyDt><CurCode></CurCode><AmountDtls><AmtType>CreditLimit</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>OutstandingAmt</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>OverdueAmt</AmtType><Amt>0</Amt></AmountDtls></CardDetails><CardDetails><CardEmbossNum>010790300</CardEmbossNum><CardStatus>STWO</CardStatus><CardType>MSTANDARD-EXPATRIATE</CardType><CustRoleType>Primary</CustRoleType><KeyDt><KeyDtType>ApplicationCreationDate</KeyDtType><KeyDtValue>2012-06-09</KeyDtValue></KeyDt><KeyDt><KeyDtType>ExpiryDate</KeyDtType><KeyDtValue>2025-01-31</KeyDtValue></KeyDt><CurCode></CurCode><AmountDtls><AmtType>CreditLimit</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>OutstandingAmt</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>OverdueAmt</AmtType><Amt>0</Amt></AmountDtls></CardDetails><CardDetails><CardEmbossNum>075161000</CardEmbossNum><CardStatus>AUWO</CardStatus><CardType>SME-TITANIUM</CardType><CustRoleType>Primary</CustRoleType><KeyDt><KeyDtType>ApplicationCreationDate</KeyDtType><KeyDtValue>2015-05-29</KeyDtValue></KeyDt><KeyDt><KeyDtType>ExpiryDate</KeyDtType><KeyDtValue>2025-01-31</KeyDtValue></KeyDt><CurCode></CurCode><AmountDtls><AmtType>CreditLimit</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>OutstandingAmt</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>OverdueAmt</AmtType><Amt>-40184.49</Amt></AmountDtls></CardDetails><AcctDetails><AcctId>8032044263901</AcctId><IBANNumber>AE480400008032044263901</IBANNumber><AcctStat>ACTIVE</AcctStat><AcctCur>AED</AcctCur><AcctNm>FIRST NAME FOR 0536177  LAST NAME FOR 0536177</AcctNm><AcctType>AMAL BUSINESS FINANCE ACCOUNT</AcctType><AcctSegment>PBD</AcctSegment><AcctSubSegment>PSL</AcctSubSegment><CustRoleType>Auth Sign.</CustRoleType><KeyDt><KeyDtType>AccountOpenDate</KeyDtType><KeyDtValue>2013-02-25</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitSactionDate</KeyDtType><KeyDtValue>2013-02-25</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitExpiryDate</KeyDtType><KeyDtValue>2013-02-26</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitStartDate</KeyDtType><KeyDtValue>2013-02-25</KeyDtValue></KeyDt><AmountDtls><AmtType>AvailableBalance</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>ClearBalanceAmount</AmtType><Amt>-1824.32</Amt></AmountDtls><AmountDtls><AmtType>LedgerBalance</AmtType><Amt>-1824.32</Amt></AmountDtls><AmountDtls><AmtType>OverdueAmount</AmtType><Amt>-1824.32</Amt></AmountDtls><AmountDtls><AmtType>EffectiveAvailableBalance</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>CumulativeDebitAmount</AmtType><Amt>60443311.04</Amt></AmountDtls><AmountDtls><AmtType>ChrgOffBal</AmtType><Amt>1824.32</Amt></AmountDtls><AmountDtls><AmtType>SanctionLimit</AmtType><Amt>0</Amt></AmountDtls><WriteoffStat>N</WriteoffStat><WriteoffStatDt>2017-08-20</WriteoffStatDt><WorstDelay24Months>P6</WorstDelay24Months><WorstStatus24Months>DEFAULT</WorstStatus24Months><MonthsOnBook>66.00</MonthsOnBook><LastRepmtDt>AUG</LastRepmtDt><IsCurrent>N</IsCurrent><ChargeOffFlag>Y</ChargeOffFlag><DelinquencyInfo><BucketType>DaysPastDue</BucketType><BucketValue>29</BucketValue></DelinquencyInfo><LinkedCIFs><CIFId>2044263</CIFId><RelationType>Main Account Holder</RelationType></LinkedCIFs></AcctDetails><AcctDetails><AcctId>8032044263902</AcctId><IBANNumber>AE210400008032044263902</IBANNumber><AcctStat>ACTIVE</AcctStat><AcctCur>USD</AcctCur><AcctNm>FIRST NAME FOR 0536177  LAST NAME FOR 0536177</AcctNm><AcctType>AMAL BUSINESS FINANCE ACCOUNT</AcctType><AcctSegment>PBD</AcctSegment><AcctSubSegment>PSL</AcctSubSegment><CustRoleType>Auth Sign.</CustRoleType><KeyDt><KeyDtType>AccountOpenDate</KeyDtType><KeyDtValue>2013-02-25</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitSactionDate</KeyDtType><KeyDtValue>2013-02-25</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitExpiryDate</KeyDtType><KeyDtValue>2013-02-26</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitStartDate</KeyDtType><KeyDtValue>2013-02-25</KeyDtValue></KeyDt><AmountDtls><AmtType>AvailableBalance</AmtType><Amt>0.00</Amt></AmountDtls><AmountDtls><AmtType>ClearBalanceAmount</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>LedgerBalance</AmtType><Amt>0.00</Amt></AmountDtls><AmountDtls><AmtType>EffectiveAvailableBalance</AmtType><Amt>0.00</Amt></AmountDtls><AmountDtls><AmtType>CumulativeDebitAmount</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>SanctionLimit</AmtType><Amt>0</Amt></AmountDtls><WriteoffStat>Y</WriteoffStat><WorstDelay24Months>P2</WorstDelay24Months><MonthsOnBook>66.00</MonthsOnBook><LastRepmtDt>AUG</LastRepmtDt><IsCurrent>Y</IsCurrent><ChargeOffFlag>N</ChargeOffFlag><DelinquencyInfo><BucketType>DaysPastDue</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><LinkedCIFs><CIFId>2044263</CIFId><RelationType>Main Account Holder</RelationType></LinkedCIFs></AcctDetails><AcctDetails><AcctId>0030536177001</AcctId><IBANNumber>AE790400000030536177001</IBANNumber><AcctStat>ACTIVE</AcctStat><AcctCur>AED</AcctCur><AcctNm>FIRST NAME FOR 0536177  LAST NAME FOR 0536177</AcctNm><AcctType>CURRENT ACCOUNT</AcctType><AcctSegment>PBD</AcctSegment><AcctSubSegment>PBN</AcctSubSegment><CustRoleType>Main</CustRoleType><KeyDt><KeyDtType>AccountOpenDate</KeyDtType><KeyDtValue>2013-03-06</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitSactionDate</KeyDtType><KeyDtValue>2013-03-06</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitExpiryDate</KeyDtType><KeyDtValue>2013-03-07</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitStartDate</KeyDtType><KeyDtValue>2013-03-06</KeyDtValue></KeyDt><AmountDtls><AmtType>AvailableBalance</AmtType><Amt>0.00</Amt></AmountDtls><AmountDtls><AmtType>ClearBalanceAmount</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>LedgerBalance</AmtType><Amt>0.00</Amt></AmountDtls><AmountDtls><AmtType>EffectiveAvailableBalance</AmtType><Amt>0.00</Amt></AmountDtls><AmountDtls><AmtType>CumulativeDebitAmount</AmtType><Amt>10000</Amt></AmountDtls><AmountDtls><AmtType>SanctionLimit</AmtType><Amt>0</Amt></AmountDtls><WriteoffStat>Y</WriteoffStat><WorstDelay24Months>P2</WorstDelay24Months><MonthsOnBook>66.00</MonthsOnBook><LastRepmtDt>AUG</LastRepmtDt><IsCurrent>Y</IsCurrent><ChargeOffFlag>N</ChargeOffFlag><DelinquencyInfo><BucketType>DaysPastDue</BucketType><BucketValue>0</BucketValue></DelinquencyInfo></AcctDetails><AcctDetails><AcctId>0030536177998</AcctId><IBANNumber>AE290400000030536177998</IBANNumber><AcctStat>ACTIVE</AcctStat><AcctCur>AED</AcctCur><AcctNm>FIRST NAME FOR 0536177  LAST NAME FOR 0536177</AcctNm><AcctType>CHARGE RECEIVABLE</AcctType><AcctSegment>PBD</AcctSegment><AcctSubSegment>PBN</AcctSubSegment><CustRoleType>Main</CustRoleType><KeyDt><KeyDtType>AccountOpenDate</KeyDtType><KeyDtValue>2017-02-01</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitSactionDate</KeyDtType><KeyDtValue>2017-02-01</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitExpiryDate</KeyDtType><KeyDtValue>2099-01-01</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitStartDate</KeyDtType><KeyDtValue>2017-02-01</KeyDtValue></KeyDt><AmountDtls><AmtType>AvailableBalance</AmtType><Amt>99999999999599.00</Amt></AmountDtls><AmountDtls><AmtType>ClearBalanceAmount</AmtType><Amt>-400</Amt></AmountDtls><AmountDtls><AmtType>LedgerBalance</AmtType><Amt>-400.00</Amt></AmountDtls><AmountDtls><AmtType>EffectiveAvailableBalance</AmtType><Amt>99999999999599.00</Amt></AmountDtls><AmountDtls><AmtType>CumulativeDebitAmount</AmtType><Amt>400</Amt></AmountDtls><AmountDtls><AmtType>SanctionLimit</AmtType><Amt>99999999999999</Amt></AmountDtls><WriteoffStat>Y</WriteoffStat><WorstDelay24Months>P2</WorstDelay24Months><MonthsOnBook>19.00</MonthsOnBook><LastRepmtDt>AUG</LastRepmtDt><IsCurrent>Y</IsCurrent><ChargeOffFlag>N</ChargeOffFlag><DelinquencyInfo><BucketType>DaysPastDue</BucketType><BucketValue>0</BucketValue></DelinquencyInfo></AcctDetails><AcctDetails><AcctId>0003536177099</AcctId><IBANNumber></IBANNumber><AcctStat>CLOSED</AcctStat><AcctCur>AED</AcctCur><AcctNm>FIRST NAME FOR 0536177  LAST NAME FOR 0536177</AcctNm><AcctType>CUSTOMER SECURITY CHEQUE ACCOUNT</AcctType><AcctSegment>PBD</AcctSegment><AcctSubSegment>PBN</AcctSubSegment><CustRoleType>Main</CustRoleType><KeyDt><KeyDtType>AccountOpenDate</KeyDtType><KeyDtValue>2004-11-28</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitSactionDate</KeyDtType><KeyDtValue>2004-11-28</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitExpiryDate</KeyDtType><KeyDtValue>2012-06-08</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitStartDate</KeyDtType><KeyDtValue>2004-11-28</KeyDtValue></KeyDt><AmountDtls><AmtType>AvailableBalance</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>ClearBalanceAmount</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>LedgerBalance</AmtType><Amt>0.00</Amt></AmountDtls><AmountDtls><AmtType>EffectiveAvailableBalance</AmtType><Amt>0.00</Amt></AmountDtls><AmountDtls><AmtType>CumulativeDebitAmount</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>SanctionLimit</AmtType><Amt>0</Amt></AmountDtls><WriteoffStat>Y</WriteoffStat><WorstDelay24Months>P2</WorstDelay24Months><MonthsOnBook>104.00</MonthsOnBook><LastRepmtDt>JUL</LastRepmtDt><IsCurrent>Y</IsCurrent><ChargeOffFlag>N</ChargeOffFlag><DelinquencyInfo><BucketType>DaysPastDue</BucketType><BucketValue>0</BucketValue></DelinquencyInfo></AcctDetails></ProductExposureDetails></CustomerExposureResponse><CustomerExposureResponse><RequestType>InternalExposure</RequestType><IsDirect>N</IsDirect><CustInfo><CustId><CustIdType>CIF Id</CustIdType><CustIdValue>2044263</CustIdValue></CustId><FullNm></FullNm><BirthDt>2003-11-12</BirthDt><Nationality>UNITED ARAB EMIRATES</Nationality><CustSegment>PERSONAL BANKING</CustSegment><CreditGrade>W1-WRITE OFF ACCOUNT</CreditGrade></CustInfo><ProductExposureDetails><LoanDetails><AgreementId>0032044263001</AgreementId><LoanStat>ACTIVE</LoanStat><LoanType>SHORT TERM LOAN AGAINST INVOICE</LoanType><LoanDesc></LoanDesc><CustRoleType></CustRoleType><KeyDt><KeyDtType>LimitSactionDate</KeyDtType><KeyDtValue>2011-02-13</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitExpiryDate</KeyDtType><KeyDtValue>2099-02-13</KeyDtValue></KeyDt><CurCode>AED</CurCode><MonthsOnBook>37.00</MonthsOnBook><DelinquencyInfo><BucketType>DaysPastDue</BucketType><BucketValue>0</BucketValue></DelinquencyInfo></LoanDetails><LoanDetails><AgreementId>0032044263002</AgreementId><LoanStat>ACTIVE</LoanStat><LoanType>SHORT TERM LOAN AGAINST INVOICE PAST DUE</LoanType><LoanDesc></LoanDesc><CustRoleType></CustRoleType><KeyDt><KeyDtType>LimitSactionDate</KeyDtType><KeyDtValue>2011-02-13</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitExpiryDate</KeyDtType><KeyDtValue>2099-02-13</KeyDtValue></KeyDt><CurCode>AED</CurCode><AmountDtls><AmtType>OverdueAmt</AmtType><Amt>-110395.72</Amt></AmountDtls><WriteoffStat>ChargeOff</WriteoffStat><WriteoffStatDt>2016-07-26</WriteoffStatDt><MonthsOnBook>33.00</MonthsOnBook><DelinquencyInfo><BucketType>DaysPastDue</BucketType><BucketValue>0</BucketValue></DelinquencyInfo></LoanDetails><AcctDetails><AcctId>8032044263998</AcctId><IBANNumber>AE480400008032044263998</IBANNumber><AcctStat>CLOSED</AcctStat><AcctCur>AED</AcctCur><AcctNm>FIRST NAME FOR 0536177  LAST NAME FOR 0536177</AcctNm><AcctType>CHARGE RECEIVABLE</AcctType><AcctSegment>PBD</AcctSegment><AcctSubSegment>PSL</AcctSubSegment><CustRoleType>Main</CustRoleType><KeyDt><KeyDtType>AccountOpenDate</KeyDtType><KeyDtValue>2015-12-17</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitSactionDate</KeyDtType><KeyDtValue>2015-12-17</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitExpiryDate</KeyDtType><KeyDtValue>2099-01-01</KeyDtValue></KeyDt><AmountDtls><AmtType>AvailableBalance</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>ClearBalanceAmount</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>LedgerBalance</AmtType><Amt>0.00</Amt></AmountDtls><AmountDtls><AmtType>EffectiveAvailableBalance</AmtType><Amt>0</Amt></AmountDtls><WriteoffStat>Y</WriteoffStat><WorstDelay24Months>P2</WorstDelay24Months><MonthsOnBook>21.00</MonthsOnBook><LastRepmtDt>AUG</LastRepmtDt><IsCurrent>Y</IsCurrent><ChargeOffFlag>N</ChargeOffFlag><DelinquencyInfo><BucketType>DaysPastDue</BucketType><BucketValue>0</BucketValue></DelinquencyInfo></AcctDetails><AcctDetails><AcctId>8032044263999</AcctId><IBANNumber>AE210400008032044263999</IBANNumber><AcctStat>ACTIVE</AcctStat><AcctCur>AED</AcctCur><AcctNm>FIRST NAME FOR 0536177  LAST NAME FOR 0536177</AcctNm><AcctType>CHARGE RECEIVABLE</AcctType><AcctSegment>PBD</AcctSegment><AcctSubSegment>PSL</AcctSubSegment><CustRoleType>Main</CustRoleType><KeyDt><KeyDtType>AccountOpenDate</KeyDtType><KeyDtValue>2017-09-03</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitSactionDate</KeyDtType><KeyDtValue>2017-09-03</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitExpiryDate</KeyDtType><KeyDtValue>2099-01-01</KeyDtValue></KeyDt><AmountDtls><AmtType>AvailableBalance</AmtType><Amt>99999999997226.50</Amt></AmountDtls><AmountDtls><AmtType>ClearBalanceAmount</AmtType><Amt>-2772.5</Amt></AmountDtls><AmountDtls><AmtType>LedgerBalance</AmtType><Amt>-2772.50</Amt></AmountDtls><AmountDtls><AmtType>EffectiveAvailableBalance</AmtType><Amt>99999999997226.50</Amt></AmountDtls><WriteoffStat>Y</WriteoffStat><WorstDelay24Months>P2</WorstDelay24Months><MonthsOnBook>12.00</MonthsOnBook><LastRepmtDt>AUG</LastRepmtDt><IsCurrent>Y</IsCurrent><ChargeOffFlag>N</ChargeOffFlag><DelinquencyInfo><BucketType>DaysPastDue</BucketType><BucketValue>0</BucketValue></DelinquencyInfo></AcctDetails></ProductExposureDetails></CustomerExposureResponse></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";		
		}

		else if(callName.indexOf("ExternalExposure")>-1)
		{
			response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>CUSTOMER_EXPOSURE</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153379503253258</MessageId><Extra1>REP||SHELL.JOHN</Extra1><Extra2>2018-08-09T10:10:36.002+04:00</Extra2></EE_EAI_HEADER><CustomerExposureResponse><RequestType>ExternalExposure</RequestType><ReferenceNumber>167054</ReferenceNumber><ReportUrl>https://ant2a2aapps01.rakbanktst.ae:446/GetPdf.aspx?refno=Ysk2f%2fhPUcQ%3d</ReportUrl><IsDirect>N</IsDirect><CustInfo><FullNm>ACCENTURE PVT LTD</FullNm><Activity>0</Activity><TotalOutstanding>0</TotalOutstanding><TotalOverdue>0.00</TotalOverdue><NoOfContracts>0</NoOfContracts><CustInfoListDet><ReferenceNumber>167054</ReferenceNumber><InfoType>NameENInfo</InfoType><CustName>ACCENTURE PVT LTD</CustName><CustNameType>CompanyTradeName</CustNameType><ActualFlag>true</ActualFlag><ProviderNo>B01</ProviderNo><CreatedOn>2018-08-09</CreatedOn><DateOfUpdate>2018-08-09</DateOfUpdate></CustInfoListDet><CustInfoListDet><ReferenceNumber>167054</ReferenceNumber><InfoType>TradeLicenseHistorylst</InfoType><ActualFlag>true</ActualFlag><ProviderNo>B01</ProviderNo><RegistrationPlace>3</RegistrationPlace><LicenseNumber>123658585</LicenseNumber><CreatedOn>2018-08-09</CreatedOn><DateOfUpdate>2018-08-09</DateOfUpdate></CustInfoListDet><PhoneInfo><ReportedDate>2018-08-09</ReportedDate></PhoneInfo><InquiryInfo><ContractCategory>C</ContractCategory></InquiryInfo></CustInfo><ScoreInfo></ScoreInfo><RecordDestributions><RecordDestribution><ContractType>TotalSummary</ContractType><Contract_Role_Type></Contract_Role_Type><TotalNo>1</TotalNo><DataProvidersNo>1</DataProvidersNo><RequestNo>1</RequestNo><DeclinedNo>0</DeclinedNo><RejectedNo>0</RejectedNo><NotTakenUpNo>0</NotTakenUpNo><ActiveNo>0</ActiveNo><ClosedNo>0</ClosedNo></RecordDestribution><RecordDestribution><ContractType>Installments</ContractType><Contract_Role_Type>Holder</Contract_Role_Type><TotalNo>0</TotalNo><DataProvidersNo>0</DataProvidersNo><RequestNo>0</RequestNo><DeclinedNo>0</DeclinedNo><RejectedNo>0</RejectedNo><NotTakenUpNo>0</NotTakenUpNo><ActiveNo>0</ActiveNo><ClosedNo>0</ClosedNo></RecordDestribution><RecordDestribution><ContractType>NotInstallments</ContractType><Contract_Role_Type>Holder</Contract_Role_Type><TotalNo>0</TotalNo><DataProvidersNo>0</DataProvidersNo><RequestNo>0</RequestNo><DeclinedNo>0</DeclinedNo><RejectedNo>0</RejectedNo><NotTakenUpNo>0</NotTakenUpNo><ActiveNo>0</ActiveNo><ClosedNo>0</ClosedNo></RecordDestribution><RecordDestribution><ContractType>CreditCards</ContractType><Contract_Role_Type>Holder</Contract_Role_Type><TotalNo>1</TotalNo><DataProvidersNo>0</DataProvidersNo><RequestNo>1</RequestNo><DeclinedNo>0</DeclinedNo><RejectedNo>0</RejectedNo><NotTakenUpNo>0</NotTakenUpNo><ActiveNo>0</ActiveNo><ClosedNo>0</ClosedNo></RecordDestribution><RecordDestribution><ContractType>Services</ContractType><Contract_Role_Type>Holder</Contract_Role_Type><TotalNo>0</TotalNo><DataProvidersNo>0</DataProvidersNo><RequestNo>0</RequestNo><DeclinedNo>0</DeclinedNo><RejectedNo>0</RejectedNo><NotTakenUpNo>0</NotTakenUpNo><ActiveNo>0</ActiveNo><ClosedNo>0</ClosedNo></RecordDestribution></RecordDestributions><Derived><Total_Exposure>0</Total_Exposure><WorstCurrentPaymentDelay>0</WorstCurrentPaymentDelay><Worst_PaymentDelay_Last24Months>0</Worst_PaymentDelay_Last24Months><Nof_Records>0</Nof_Records><NoOf_Cheque_Return_Last3>0</NoOf_Cheque_Return_Last3><Nof_DDES_Return_Last3Months>0</Nof_DDES_Return_Last3Months><Nof_DDES_Return_Last6Months>0</Nof_DDES_Return_Last6Months><Nof_Cheque_Return_Last6>0</Nof_Cheque_Return_Last6><DPD30_Last6Months>0</DPD30_Last6Months><Nof_Enq_Last90Days>0</Nof_Enq_Last90Days><Nof_Enq_Last60Days>0</Nof_Enq_Last60Days><Nof_Enq_Last30Days>0</Nof_Enq_Last30Days><TotOverDue_GuarteContrct>0</TotOverDue_GuarteContrct></Derived><ProductExposureDetails><ChequeDetails><ChqType></ChqType><Number></Number><Amount></Amount><ReturnDate></ReturnDate><ProviderNo></ProviderNo><ReasonCode></ReasonCode><Severity></Severity></ChequeDetails></ProductExposureDetails></CustomerExposureResponse></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(callName.indexOf("CollectionsSummary")>-1)
		{
			response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>CUSTOMER_EXPOSURE</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153379505177990</MessageId><Extra1>REP||SHELL.JOHN</Extra1><Extra2>2018-08-09T10:10:52.790+04:00</Extra2></EE_EAI_HEADER><CustomerExposureResponse><RequestType>CollectionsSummary</RequestType><CustInfo><CustId><CustIdType>Primary</CustIdType><CustIdValue>0536177</CustIdValue></CustId><FullNm></FullNm><TotalOutstanding>97554.74</TotalOutstanding><TotalOverdue>0</TotalOverdue><NoOfContracts>0</NoOfContracts></CustInfo><Derived><Nof_Records>1</Nof_Records></Derived><ProductExposureDetails><CardDetails><CardEmbossNum>010790300</CardEmbossNum><CardStatus>C</CardStatus><CardType>CREDIT CARDS</CardType><KeyDt><KeyDtType>Card_approve_date</KeyDtType><KeyDtValue>2004-11-30</KeyDtValue></KeyDt><KeyDt><KeyDtType>CardsApplcnRecvdDate</KeyDtType><KeyDtValue>2004-11-30</KeyDtValue></KeyDt><CurCode>AED</CurCode><AmountDtls><AmtType>Outstanding_balance</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>Credit_limit</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>Overdue_amount</AmtType><Amt>0</Amt></AmountDtls><MonthsOnBook>164</MonthsOnBook><SchemeCardProd>MSTANDARD-EXPATRIATE</SchemeCardProd><CurrentlyCurrentFlg>Y</CurrentlyCurrentFlg><GeneralStatus>STWO</GeneralStatus><DelinquencyInfo><BucketType>DPD_30_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>Bucket</BucketType><BucketValue>0</BucketValue></DelinquencyInfo></CardDetails><CardDetails><CardEmbossNum>075161000</CardEmbossNum><CardStatus>A</CardStatus><CardType>CREDIT CARDS</CardType><KeyDt><KeyDtType>Card_approve_date</KeyDtType><KeyDtValue>2015-05-28</KeyDtValue></KeyDt><KeyDt><KeyDtType>CardsApplcnRecvdDate</KeyDtType><KeyDtValue>2015-05-28</KeyDtValue></KeyDt><CurCode>AED</CurCode><AmountDtls><AmtType>Outstanding_balance</AmtType><Amt>97554.74</Amt></AmountDtls><AmountDtls><AmtType>Credit_limit</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>Overdue_amount</AmtType><Amt>0</Amt></AmountDtls><MonthsOnBook>38</MonthsOnBook><LastRepmtDt>09-2016</LastRepmtDt><SchemeCardProd>SME-TITANIUM</SchemeCardProd><CurrentlyCurrentFlg>Y</CurrentlyCurrentFlg><GeneralStatus>AUWO</GeneralStatus><DelinquencyInfo><BucketType>DPD_30_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>Bucket</BucketType><BucketValue>0</BucketValue></DelinquencyInfo></CardDetails><CardDetails><CardEmbossNum>010790200</CardEmbossNum><CardStatus>C</CardStatus><CardType>CREDIT CARDS</CardType><KeyDt><KeyDtType>Card_approve_date</KeyDtType><KeyDtValue>2004-11-30</KeyDtValue></KeyDt><KeyDt><KeyDtType>CardsApplcnRecvdDate</KeyDtType><KeyDtValue>2004-11-30</KeyDtValue></KeyDt><CurCode>AED</CurCode><AmountDtls><AmtType>Outstanding_balance</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>Credit_limit</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>Overdue_amount</AmtType><Amt>0</Amt></AmountDtls><MonthsOnBook>164</MonthsOnBook><SchemeCardProd>VCLASSIC-EXPATR</SchemeCardProd><CurrentlyCurrentFlg>Y</CurrentlyCurrentFlg><GeneralStatus>STWO</GeneralStatus><DelinquencyInfo><BucketType>DPD_30_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_30_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_60_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_90_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_120_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_150_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_3_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_6_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_9_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_12_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_18_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>DPD_180_in_last_24_months</BucketType><BucketValue>0</BucketValue></DelinquencyInfo><DelinquencyInfo><BucketType>Bucket</BucketType><BucketValue>0</BucketValue></DelinquencyInfo></CardDetails></ProductExposureDetails></CustomerExposureResponse></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if (callName.indexOf("CARD_INSTALLMENT_DETAILS")>-1)
		{
			response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>CARD_INSTALLMENT_DETAILS</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>1</ReturnCode><ReturnDesc>PRMSD : This account does not exist in database</ReturnDesc><MessageId>CAS153379502338921</MessageId><Extra1>REP||BPM.123</Extra1><Extra2>2018-08-09T10:10:24.512+04:00</Extra2></EE_EAI_HEADER></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(callName.indexOf("SALDET")>-1)
		{
			response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>FINANCIAL_SUMMARY</MsgFormat><MsgVersion>0000</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>CINF212</ReturnCode><ReturnDesc>FIN : [CINF212]-NO SALARY RECORD FOUND</ReturnDesc><MessageId>CAS153382636971559</MessageId><Extra1>REP||LAXMANRET.LAXMANRET</Extra1><Extra2>2018-08-09T06:52:50.103+04:00</Extra2></EE_EAI_HEADER></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(callName.indexOf("RETURNDET")>-1)
		{
			response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>FINANCIAL_SUMMARY</MsgFormat><MsgVersion>0000</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>CINF728</ReturnCode><ReturnDesc>FIN : [CINF728]-NO CHEQUE OR DDS RETURN FOUND</ReturnDesc><MessageId>CAS153379510967534</MessageId><Extra1>REP||LAXMANRET.LAXMANRET</Extra1><Extra2>2018-08-09T10:11:50.233+04:00</Extra2></EE_EAI_HEADER></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(callName.indexOf("TRANSUM")>-1)
		{
			response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>FINANCIAL_SUMMARY</MsgFormat><MsgVersion>0000</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>CINF731</ReturnCode><ReturnDesc>FIN : [CINF731]-TRANSACTION SUMMARY NOT FOUND</ReturnDesc><MessageId>CAS153414285806563</MessageId><Extra1>REP||LAXMANRET.LAXMANRET</Extra1><Extra2>2018-08-13T10:47:38.472+04:00</Extra2></EE_EAI_HEADER></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(callName.indexOf("AVGBALDET")>-1)
		{
			response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>FINANCIAL_SUMMARY</MsgFormat><MsgVersion>0000</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153354273860916</MessageId><Extra1>REP||LAXMANRET.LAXMANRET</Extra1><Extra2>2018-08-06T12:05:39.106+04:00</Extra2></EE_EAI_HEADER><FinancialSummaryRes><BankId>RAK</BankId><CIFID>0181162</CIFID><AcctId>0033181162002</AcctId><OperationType>AVGBALDET</OperationType><OperationDesc>AVERAGE BALANCE</OperationDesc><TxnSummary></TxnSummary><AvgBalanceDtls><Month>JUN</Month><AvgBalance>0</AvgBalance></AvgBalanceDtls><AvgBalanceDtls><Month>MAY</Month><AvgBalance>0</AvgBalance></AvgBalanceDtls><AvgBalanceDtls><Month>APR</Month><AvgBalance>0</AvgBalance></AvgBalanceDtls><AvgBalanceDtls><Month>MAR</Month><AvgBalance>0</AvgBalance></AvgBalanceDtls><AvgBalanceDtls><Month>FEB</Month><AvgBalance>0</AvgBalance></AvgBalanceDtls><AvgBalanceDtls><Month>JAN</Month><AvgBalance>0</AvgBalance></AvgBalanceDtls></FinancialSummaryRes></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(callName.indexOf("LIENDET")>-1)
		{
			response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>FINANCIAL_SUMMARY</MsgFormat><MsgVersion>0000</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>CINF729</ReturnCode><ReturnDesc>FIN : [CINF729]-NO LIEN DETAILS FOUND</ReturnDesc><MessageId>CAS153405218506324</MessageId><Extra1>REP||LAXMANRET.LAXMANRET</Extra1><Extra2>2018-08-12T09:36:26.355+04:00</Extra2></EE_EAI_HEADER></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(callName.equalsIgnoreCase("CUSTOMER_SEARCH_REQUEST")){
			response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>CUSTOMER_SEARCH_REQUEST</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>9999</ReturnCode><ReturnDesc>CASMEX : No record found</ReturnDesc><MessageId>CAS153423574414232</MessageId><Extra1>REP</Extra1><Extra2>2018-08-14T12:35:45.145+04:00</Extra2></EE_EAI_HEADER></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(callName.equalsIgnoreCase("NEW_ACCOUNT_REQ")){
			response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>NEW_ACCOUNT_REQ</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS152042331228897</MessageId><Extra1>REP||SHELL.JOHN</Extra1><Extra2>2018-03-07T03:48:34.222+04:00</Extra2></EE_EAI_HEADER><AcccountResponse><BankId>RAK</BankId><BranchId>002</BranchId><CustomerId>2573591</CustomerId><NewAcid>0022573591001</NewAcid><IBANNumber>AE330400000022573591001</IBANNumber><FirstName>RENUKA</FirstName><MidName>ASHOK</MidName><LastName>GULBARGA</LastName><DocType>PPT</DocType><DocRefNum>3524545445</DocRefNum><MotherMaidenName>MNBVNB</MotherMaidenName><AcctCurr>AED</AcctCurr><AcctStat>F</AcctStat><AcctSchemeCode>ACNP1</AcctSchemeCode><AcctSchemeType>ODA</AcctSchemeType></AcccountResponse></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";

		}

		return response;
	}
	public void Integration_fragName(String callName)
	{
		try{
			FormReference formObject=FormContext.getCurrentInstance().getFormReference();
			String Curr_framename=formObject.getNGValue("FrameName");
			RLOS.mLogger.info("FrameName Before DBR"+Curr_framename);
			String call_framename="";
			if(("CUSTOMER_DETAILS").equalsIgnoreCase(callName))
			{
				call_framename="CustomerDetails,EmploymentDetails,Address_Details_container,OECD_container,Alt_Contact_container,FATCA_container,KYC_container";
			}
			else if(("dectech").equalsIgnoreCase(callName)){
				call_framename="EligibilityAndProductInformation,Liability_container";
			}
			else if(("NEW_CUSTOMER_REQ").equalsIgnoreCase(callName)){
				call_framename="DecisionHistoryContainer,CustomerDetails";
			}
			else if(("DEDUP_SUMMARY").equalsIgnoreCase(callName)){
				call_framename="Part_Match";
			}
			else if(("BLACKLIST_DETAILS").equalsIgnoreCase(callName)){
				call_framename="Finacle_CRM_Incidents";
			}	
			else if(callName.equalsIgnoreCase("NEW_ACCOUNT_REQ")){
				call_framename="DecisionHistoryContainer";
			}

			if(!"".equalsIgnoreCase(call_framename)){
				String[] call_framename_arr = call_framename.split(",");
				for(int x = 0 ;x<call_framename_arr.length;x++){
					if(!Curr_framename.contains(call_framename_arr[x])){
						if("".equalsIgnoreCase(Curr_framename)){
							Curr_framename = call_framename_arr[x];
						}
						else{
							Curr_framename = Curr_framename+","+call_framename_arr[x];
						}
					}
				}
				formObject.setNGValue("FrameName", Curr_framename+",");
			}
		}
		catch(Exception e){
			RLOS.mLogger.info( "Exception occured in Integration_fragName: "+ e.getMessage());
		}
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
		//RLOS.mLogger.info("getMQInputXML"+strBuff.toString());
		return strBuff.toString();
	}

	//modified by akshay on 27/2/18 for drop 4
	public static String getCustAddress_details(){
		//RLOS.mLogger.info( "inside getCustAddress_details : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int add_row_count = formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
		//RLOS.mLogger.info( "inside getCustAddress_details add_row_count+ : "+add_row_count);
		String  add_xml_str ="";
		for (int i = 0; i<add_row_count;i++){
			//RLOS.mLogger.info( "inside cmplx_DecisionHistory_MultipleApplicantsGrid add_row_count+ : "+formObject.getNGValue("cmplx_DecisionHistory_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_DecisionHistory_MultipleApplicantsGrid"),0));

			String Address_type = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0); //0
			String Po_Box=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 1);//1
			String flat_Villa=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 2);//2
			String Building_name=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 3);//3
			String street_name=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 4);//4
			String Landmard = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 5);//5
			String city=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 6);//6
			String Emirates=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 7);//7
			String country=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 8);//8
			String Applicant_type=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i,13);
			//	RLOS.mLogger.info( "inside Applicant_type add_row_count+ : "+Applicant_type);

			String preferrd;
			if(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_true").equalsIgnoreCase(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 10)))//10
				preferrd = "Y";
			else
				preferrd = "N";

			if(Applicant_type.contains("P-") && formObject.getNGValue("cmplx_DecisionHistory_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_DecisionHistory_MultipleApplicantsGrid"),0).equalsIgnoreCase("PRIMARY")){
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
			else if(Applicant_type.contains("G-") && formObject.getNGValue("cmplx_DecisionHistory_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_DecisionHistory_MultipleApplicantsGrid"),0).equalsIgnoreCase("Guarantor"))
			{
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
		}
		//RLOS.mLogger.info( "Address tag Cration: "+ add_xml_str);
		return add_xml_str;
	}
	public String  getRejectedDetails(){
		// RLOS.mLogger.info( "inside getCustAddress_details : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "SELECT Rejected_cust,Rejected_Date,Rejected_reason,Rejected_product,Rejected_app_id FROM Ng_Rlos_Dectech_Rejected_App_Data  with (nolock) where Wi_Name = '"+formObject.getWFWorkitemName()+"'";
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		for (int i = 0; i<OutputXML.size();i++){
			String Rejected_cust = "";
			String Rejected_Date = "";
			String Rejected_reason = "";
			String Rejected_product = ""; 
			String Rejected_app_id = "";
			if(!(OutputXML.get(i).get(0) == null || "".equalsIgnoreCase(OutputXML.get(i).get(0))) ){
				Rejected_cust = OutputXML.get(i).get(0);
			}
			if(!(OutputXML.get(i).get(1) == null || "".equalsIgnoreCase(OutputXML.get(i).get(1))) ){
				Rejected_Date = OutputXML.get(i).get(1);
			}
			if(!(OutputXML.get(i).get(2) == null || "".equalsIgnoreCase(OutputXML.get(i).get(2))) ){
				Rejected_reason = OutputXML.get(i).get(2);
			}
			if(!(OutputXML.get(i).get(3) == null || "".equalsIgnoreCase(OutputXML.get(i).get(3))) ){
				Rejected_product = OutputXML.get(i).get(3);
			}
			if(!(OutputXML.get(i).get(4) == null || "".equalsIgnoreCase(OutputXML.get(i).get(4))) ){
				Rejected_app_id = OutputXML.get(i).get(4);
			}
			add_xml_str = add_xml_str + "<RejectedDetails><rejected_cust>"+Rejected_cust+"</rejected_cust>";
			add_xml_str = add_xml_str + "<rejected_date>"+Rejected_Date+"</rejected_date>";
			add_xml_str = add_xml_str + "<rejected_reason>"+Rejected_reason+"</rejected_reason>";
			add_xml_str = add_xml_str + "<rejected_product>"+Rejected_product+"</rejected_product>";
			add_xml_str = add_xml_str + "<rejected_app_id>"+Rejected_app_id+"</rejected_app_id></RejectedDetails>";
		}
		//RLOS.mLogger.info( "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}

	// added for dectech call on 28july2017
	public String  getInternalLiabDetails(){
		//RLOS.mLogger.info( "inside getCustAddress_details : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "SELECT AcctType,CustRoleType,AcctId,AccountOpenDate,AcctStat,AcctSegment,AcctSubSegment,CreditGrade FROM ng_RLOS_CUSTEXPOSE_AcctDetails  with (nolock) where Wi_Name = '"+formObject.getWFWorkitemName()+"' and Request_Type = 'InternalExposure'";


		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);



		for (int i = 0; i<OutputXML.size();i++){

			String accountType = "";
			String role = "";
			String accNumber = "";
			String acctOpenDate = ""; 
			String acctStatus = "";
			String acctSegment = "";
			String acctSubSegment = "";
			String acctCreditGrade = "";
			if(!(OutputXML.get(i).get(0) == null || "".equalsIgnoreCase(OutputXML.get(i).get(0))) ){
				accountType = OutputXML.get(i).get(0);
			}
			if(!(OutputXML.get(i).get(1) == null || "".equalsIgnoreCase(OutputXML.get(i).get(1))) ){
				role = OutputXML.get(i).get(1);
			}
			if(!(OutputXML.get(i).get(2) == null || "".equalsIgnoreCase(OutputXML.get(i).get(2))) ){
				accNumber = OutputXML.get(i).get(2);
			}
			if(!(OutputXML.get(i).get(3) == null || "".equalsIgnoreCase(OutputXML.get(i).get(3))) ){
				acctOpenDate = OutputXML.get(i).get(3);
			}
			if(!(OutputXML.get(i).get(4) == null || "".equalsIgnoreCase(OutputXML.get(i).get(4))) ){
				acctStatus = OutputXML.get(i).get(4);
			}
			if(!(OutputXML.get(i).get(5) == null || "".equalsIgnoreCase(OutputXML.get(i).get(5))) ){
				acctSegment = OutputXML.get(i).get(5);
			}
			if(!(OutputXML.get(i).get(6) == null || "".equalsIgnoreCase(OutputXML.get(i).get(6))) ){
				acctSubSegment = OutputXML.get(i).get(6);
			}
			if(!(OutputXML.get(i).get(7) == null || "".equalsIgnoreCase(OutputXML.get(i).get(7))) ){
				acctCreditGrade = OutputXML.get(i).get(7);
			}
			if(!"".equalsIgnoreCase(accNumber) && accNumber!=null && !"null".equalsIgnoreCase(accNumber))
			{

				add_xml_str = add_xml_str + "<AccountDetails><type_of_account>"+accountType+"</type_of_account>";
				add_xml_str = add_xml_str + "<role>"+role+"</role>";
				add_xml_str = add_xml_str + "<account_number>"+accNumber+"</account_number>";
				add_xml_str = add_xml_str + "<acct_open_date>"+acctOpenDate+"</acct_open_date>";
				add_xml_str = add_xml_str + "<acct_status>"+acctStatus+"</acct_status>";
				add_xml_str = add_xml_str + "<account_segment>"+acctSegment+"</account_segment>";
				add_xml_str = add_xml_str + "<account_sub_segment>"+acctSubSegment+"</account_sub_segment>";
				add_xml_str = add_xml_str + "<credit_grate_code>"+acctCreditGrade.substring(0,2)+"</credit_grate_code>";//changed by akshay for proc 8795
				add_xml_str = add_xml_str + "<cust_type>"+role+"</cust_type></AccountDetails>";
			}
		}
		//RLOS.mLogger.info( "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}

	public String InternalBureauData(){
		//RLOS.mLogger.info( "inside InternalBureauData : ");
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
			String sQuery = "SELECT isNull((Sum(Abs(convert(float,replace([OutstandingAmt],'NA','0'))))),0),isNull((Sum(Abs(convert(float,replace([OverdueAmt],'NA','0'))))),0) FROM ng_RLOS_CUSTEXPOSE_CardDetails WHERE wi_name= '"+formObject.getWFWorkitemName()+"' AND Request_Type = 'InternalExposure'  union SELECT   isNull((Sum(Abs(convert(float,replace([TotalOutstandingAmt],'NA','0'))))),0),isNull((Sum(Abs(convert(float,replace([OverdueAmt],'NA','0'))))),0) FROM ng_RLOS_CUSTEXPOSE_LoanDetails   with (nolock) WHERE wi_name = '"+formObject.getWFWorkitemName()+"'";
			List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);


			//RLOS.mLogger.info( "values");
			double TotOutstandingAmt;
			double TotOverdueAmt;


			//RLOS.mLogger.info( "values");
			TotOutstandingAmt=0.0f;
			TotOverdueAmt=0.0f;


			//RLOS.mLogger.info( "values");
			for(int i = 0; i<OutputXML.size();i++){
				//RLOS.mLogger.info( "values"+OutputXML.get(i).get(1));
				if(OutputXML.get(i).get(0)!=null && !OutputXML.get(i).get(0).isEmpty() &&  !"".equalsIgnoreCase(OutputXML.get(i).get(0)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(0)) )
				{
					//RLOS.mLogger.info( "values."+TotOutstandingAmt+"..");
					TotOutstandingAmt = TotOutstandingAmt + Double.parseDouble(OutputXML.get(i).get(0));
				}
				if(OutputXML.get(i).get(1)!=null && !OutputXML.get(i).get(1).isEmpty() && !"".equalsIgnoreCase(OutputXML.get(i).get(1)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(1)) )
				{
					//RLOS.mLogger.info( "values."+TotOutstandingAmt+"..");
					TotOverdueAmt = TotOverdueAmt + Double.parseDouble(OutputXML.get(i).get(1));
				}

			}
			String TotOutstandingAmtSt=String.format("%.0f", TotOutstandingAmt);
			String TotOverdueAmtSt=String.format("%.0f", TotOverdueAmt);
			add_xml_str = add_xml_str + "<total_out_bal>"+TotOutstandingAmtSt+"</total_out_bal>";
			add_xml_str = add_xml_str + "<total_overdue>"+TotOverdueAmtSt+"</total_overdue>";
			String sQueryDerived = "select NoOfContracts,Total_Exposure,WorstCurrentPaymentDelay,Worst_PaymentDelay_Last24Months,Nof_Records from NG_RLOS_CUSTEXPOSE_Derived where Request_Type='CollectionsSummary' and Wi_Name='"+formObject.getWFWorkitemName()+"'" ;
			List<List<String>> OutputXMLDerived = formObject.getDataFromDataSource(sQueryDerived);
			//RLOS.mLogger.info("sQueryDerived sQueryDerived "+sQueryDerived);
			if(OutputXMLDerived!=null && OutputXMLDerived.size()>0 && OutputXMLDerived.get(0)!=null){
				if(!(OutputXMLDerived.get(0).get(0)==null || "".equalsIgnoreCase(OutputXMLDerived.get(0).get(0))) ){
					NoOfContracts= OutputXMLDerived.get(0).get(0);
				}
				if(!(OutputXMLDerived.get(0).get(1)==null || "".equalsIgnoreCase(OutputXMLDerived.get(0).get(1))) ){
					Total_Exposure= OutputXMLDerived.get(0).get(1);
				}
				if(!(OutputXMLDerived.get(0).get(2)==null || "".equalsIgnoreCase(OutputXMLDerived.get(0).get(2))) ){
					WorstCurrentPaymentDelay= OutputXMLDerived.get(0).get(2);
				}
				if(!(OutputXMLDerived.get(0).get(3)==null || "".equalsIgnoreCase(OutputXMLDerived.get(0).get(3))) ){
					Worst_PaymentDelay_Last24Months= OutputXMLDerived.get(0).get(3);
				}
				if(!(OutputXMLDerived.get(0).get(4)==null || "".equalsIgnoreCase(OutputXMLDerived.get(0).get(4))) ){
					Nof_Records= OutputXMLDerived.get(0).get(4);
				}
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



			//RLOS.mLogger.info( "Internal liab tag Cration: "+ add_xml_str);
			return add_xml_str;
		}
		catch(Exception e)
		{
			RLOS.mLogger.info( "Exception occurred in InternalBureauData()"+e.getMessage()+printException(e));
			return "";
		}

	}


	public String InternalBouncedCheques(){
		//RLOS.mLogger.info( "inside InternalBouncedCheques : ");
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


			if(!(OutputXML.get(i).get(0) == null || "".equalsIgnoreCase(OutputXML.get(i).get(0))) ){
				applicantID = OutputXML.get(i).get(0);
			}
			if(!(OutputXML.get(i).get(1) == null || "".equalsIgnoreCase(OutputXML.get(i).get(1))) ){
				internal_bounced_cheques_id = OutputXML.get(i).get(1);
			}
			if(!(OutputXML.get(i).get(2) == null || "".equalsIgnoreCase(OutputXML.get(i).get(2))) ){
				bouncedCheq = OutputXML.get(i).get(2);
			}
			if(!(OutputXML.get(i).get(3) == null || "".equalsIgnoreCase(OutputXML.get(i).get(3))) ){
				chequeNo = OutputXML.get(i).get(3);
			}
			if(!(OutputXML.get(i).get(4) == null || "".equalsIgnoreCase(OutputXML.get(i).get(4))) ){
				amount = OutputXML.get(i).get(4);
			}
			if(!(OutputXML.get(i).get(5) == null || "".equalsIgnoreCase(OutputXML.get(i).get(5))) ){
				reason = OutputXML.get(i).get(5);
			}
			if(!(OutputXML.get(i).get(6) == null || "".equalsIgnoreCase(OutputXML.get(i).get(6))) ){
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
		//RLOS.mLogger.info( "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}

	public String InternalBureauIndividualProducts(){
		RLOS.mLogger.info( "inside InternalBureauIndividualProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//Query Changed on 1st Nov
		String sQuery = "SELECT cifid,agreementid,loantype,loantype,custroletype,loan_start_date,loanmaturitydate,lastupdatedate ,totaloutstandingamt,totalloanamount,NextInstallmentAmt,paymentmode,totalnoofinstalments,remaininginstalments,totalloanamount,      overdueamt,nofdayspmtdelay,monthsonbook,currentlycurrentflg,currmaxutil,DPD_30_in_last_6_months,DPD_60_in_last_18_months,propertyvalue,loan_disbursal_date,marketingcode,DPD_30_in_last_3_months,DPD_30_in_last_6_months,DPD_30_in_last_9_months,DPD_30_in_last_12_months,DPD_30_in_last_18_months,DPD_30_in_last_24_months,DPD_60_in_last_3_months,DPD_60_in_last_6_months,DPD_60_in_last_9_months,DPD_60_in_last_12_months,DPD_60_in_last_18_months,DPD_60_in_last_24_months,DPD_90_in_last_3_months,DPD_90_in_last_6_months,DPD_90_in_last_9_months,DPD_90_in_last_12_months,DPD_90_in_last_18_months,DPD_90_in_last_24_months,DPD_120_in_last_3_months,DPD_120_in_last_6_months,DPD_120_in_last_9_months,DPD_120_in_last_12_months,DPD_120_in_last_18_months,DPD_120_in_last_24_months,DPD_150_in_last_3_months,DPD_150_in_last_6_months,DPD_150_in_last_9_months,DPD_150_in_last_12_months,DPD_150_in_last_18_months,DPD_150_in_last_24_months,DPD_180_in_last_3_months,DPD_180_in_last_6_months,DPD_180_in_last_9_months,DPD_180_in_last_12_months,DPD_180_in_last_24_months,'' as col1,isnull(Consider_For_Obligations,'true'),LoanStat,'LOANS',writeoffStat,writeoffstatdt,lastrepmtdt,limit_increase,PartSettlementDetails,SchemeCardProd as SchemeCardProduct,General_Status,Internal_WriteOff_Check,AmountPaidInLst6Mnths,InterestRate FROM ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) WHERE Wi_Name = '"+formObject.getWFWorkitemName()+"' and LoanStat not in ('Pipeline','CAS-Pipeline') union select CifId,CardEmbossNum,CardType,CardType,CustRoleType,'' as col6,'' as col7,'' as col8,OutstandingAmt,CreditLimit,case when SchemeCardProd like '%LOC%' then (select top 1 monthlyamount from ng_RLOS_CUSTEXPOSE_CardInstallmentDetails with (nolock) where Wi_Name ='"+formObject.getWFWorkitemName()+"'  and replace(CardNumber,'I','')=CardEmbossNum) else PaymentsAmount end,PaymentMode,'' as col13,case when SchemeCardProd like 'LOC%' then (select top 1 ISNULL((CAST(INSTALMENTpERIOD AS INT)-CAST(rEMAININGemi AS INT)),'') from ng_RLOS_CUSTEXPOSE_CardInstallmentDetails with (nolock) where Wi_Name = '"+formObject.getWFWorkitemName()+"' ) else ''end  as col14,CashLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,currentlycurrentflg,CurrMaxUtil,DPD_30_in_last_6_months,DPD_60_in_last_18_months,'' as col23,'' as col24,'' as col25,DPD_30_in_last_3_months,DPD_30_in_last_6_months,DPD_30_in_last_9_months,DPD_30_in_last_12_months,DPD_30_in_last_18_months,DPD_30_in_last_24_months,DPD_60_in_last_3_months,DPD_60_in_last_6_months,DPD_60_in_last_9_months,DPD_60_in_last_12_months,DPD_60_in_last_18_months,DPD_60_in_last_24_months,DPD_90_in_last_3_months,DPD_90_in_last_6_months,DPD_90_in_last_9_months,DPD_90_in_last_12_months,DPD_90_in_last_18_months,DPD_90_in_last_24_months,DPD_120_in_last_3_months,DPD_120_in_last_6_months,DPD_120_in_last_9_months,DPD_120_in_last_12_months,DPD_120_in_last_18_months,DPD_120_in_last_24_months,DPD_150_in_last_3_months,DPD_150_in_last_6_months,DPD_150_in_last_9_months,DPD_150_in_last_12_months,DPD_150_in_last_18_months,DPD_150_in_last_24_months,DPD_180_in_last_3_months,DPD_180_in_last_6_months,DPD_180_in_last_9_months,DPD_180_in_last_12_months,DPD_180_in_last_24_months,ExpiryDate,isnull(Consider_For_Obligations,'true'),CardStatus,'CARDS',writeoffStat,writeoffstatdt,'' as lastrepdate,limit_increase,'' as PartSettlementDetails,SchemeCardProd,General_Status,Internal_WriteOff_Check,AmountPaidInLst6Mnths,InterestRate FROM ng_RLOS_CUSTEXPOSE_CardDetails with (nolock) where Wi_Name = '"+formObject.getWFWorkitemName()+"'  and Request_Type In ('InternalExposure','CollectionsSummary') union select CifId,AcctId,'OverDraft' as loantype,'OverDraft' as loantype,CustRoleType,LimitSactionDate as loan_start_date,LimitExpiryDate as loanmaturitydate,'' as lastupdateddate,ClearBalanceAmount,SanctionLimit,'','','','',SanctionLimit,OverdueAmount,DaysPastDue,MonthsOnBook,IsCurrent,CurUtilRate,DPD30Last6Months,DPD60Last18Months,'',AccountOpenDate,'','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','',LimitExpiryDate,isNull(Consider_For_Obligations,'true'),AcctStat,'OVERDRAFT',WriteoffStat,WriteoffStatDt,LastRepmtDt,'','','','','','',''  from ng_rlos_custexpose_acctdetails with (nolock) where Wi_Name = '"+formObject.getWFWorkitemName()+"'   and ODType != ''";//Query Changed sagarika
		RLOS.mLogger.info( "InternalBureauIndividualProducts sQuery sQuery sQuery: "+ sQuery);

		String CountQuery ="select count(*) from ng_RLOS_CUSTEXPOSE_CardDetails with (nolock)  where Wi_Name = '"+formObject.getWFWorkitemName()+"' and cardStatus='A'";
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
				String Internal_WriteOff_Check="";
				String Combined_Limit = "";
				String SecuredCard = "";
				String EmployerType=formObject.getNGValue("cmplx_EmploymentDetails_Emp_Type");
				String Kompass=formObject.getNGValue("cmplx_EmploymentDetails_Kompass");
				String paid_installment="";
				String Type_of_OD="";
				String LoanType="";
				String amt_paid_last6mnths="";
				String interest_rate="";
				//RLOS.mLogger.info( "asdasdasd");

				if(!(OutputXML.get(i).get(0) == null || "".equalsIgnoreCase(OutputXML.get(i).get(0))) ){
					cifId = OutputXML.get(i).get(0);
				}
				if(!(OutputXML.get(i).get(1) == null || "".equalsIgnoreCase(OutputXML.get(i).get(1))) ){
					agreementId = OutputXML.get(i).get(1);
				}				
				if(!(OutputXML.get(i).get(2) == null || "".equalsIgnoreCase(OutputXML.get(i).get(2))) ){
					product_type = OutputXML.get(i).get(2);
					LoanType = OutputXML.get(i).get(2);
					if ("Home In One".equalsIgnoreCase(product_type))
					{
						product_type="HIO";
					}
					else
					{
						product_type = OutputXML.get(i).get(63);
					}
				}
				if(!(OutputXML.get(i).get(3) == null || "".equalsIgnoreCase(OutputXML.get(i).get(3))) ){
				}
				if(!(OutputXML.get(i).get(4) == null || "".equalsIgnoreCase(OutputXML.get(i).get(4))) ){
				}				
				if(!(OutputXML.get(i).get(5) == null || "".equalsIgnoreCase(OutputXML.get(i).get(5))) ){
					loan_start_date = OutputXML.get(i).get(5);
				}
				if(OutputXML.get(i).get(6)!=null && !OutputXML.get(i).get(6).isEmpty() &&  !"".equalsIgnoreCase(OutputXML.get(i).get(6)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(6)) ){
					loanmaturitydate = OutputXML.get(i).get(6);
				}
				if(OutputXML.get(i).get(7)!=null && !OutputXML.get(i).get(7).isEmpty() &&  !"".equalsIgnoreCase(OutputXML.get(i).get(7)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(7)) ){
					lastupdatedate = OutputXML.get(i).get(7);
				}				
				if(OutputXML.get(i).get(8)!=null && !OutputXML.get(i).get(8).isEmpty() &&  !"".equalsIgnoreCase(OutputXML.get(i).get(8)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(8)) ){
					outstandingamt = OutputXML.get(i).get(8);
				}
				if(!(OutputXML.get(i).get(9) == null || "".equalsIgnoreCase(OutputXML.get(i).get(9))) ){
					totalloanamount = OutputXML.get(i).get(9);
				}
				if(!(OutputXML.get(i).get(10) == null || "".equalsIgnoreCase(OutputXML.get(i).get(10))) ){
					Emi = OutputXML.get(i).get(10);
				}				
				if(!(OutputXML.get(i).get(11) == null || "".equalsIgnoreCase(OutputXML.get(i).get(11))) ){
					paymentmode = OutputXML.get(i).get(11);
				}
				if(OutputXML.get(i).get(12)!=null && !OutputXML.get(i).get(12).isEmpty() &&  !"".equalsIgnoreCase(OutputXML.get(i).get(12)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(12)) ){
					//RLOS.mLogger.info( "asdasdasd"+OutputXML.get(i).get(12));
					totalnoofinstalments = OutputXML.get(i).get(12);
				}
				if(!(OutputXML.get(i).get(13) == null || "".equalsIgnoreCase(OutputXML.get(i).get(13))) ){
					remaininginstalments = OutputXML.get(i).get(13);
				}				
				if(!(OutputXML.get(i).get(14) == null || "".equalsIgnoreCase(OutputXML.get(i).get(14))) ){
				}
				if(!(OutputXML.get(i).get(15) == null || "".equalsIgnoreCase(OutputXML.get(i).get(15))) ){
					overdueamt = OutputXML.get(i).get(15);
				}
				if(!(OutputXML.get(i).get(16) == null || "".equalsIgnoreCase(OutputXML.get(i).get(16))) ){
					nofdayspmtdelay = OutputXML.get(i).get(16);
				}				
				if(!(OutputXML.get(i).get(17) == null || "".equalsIgnoreCase(OutputXML.get(i).get(17))) ){
					monthsonbook = OutputXML.get(i).get(17);
				}
				if(!(OutputXML.get(i).get(18) == null || "".equalsIgnoreCase(OutputXML.get(i).get(18))) ){
					currentlycurrent = OutputXML.get(i).get(18);
				}
				if(!(OutputXML.get(i).get(19) == null || "".equalsIgnoreCase(OutputXML.get(i).get(19))) ){
					currmaxutil = OutputXML.get(i).get(19);
				}				
				if(!(OutputXML.get(i).get(20) == null || "".equalsIgnoreCase(OutputXML.get(i).get(20))) ){
					DPD_30_in_last_6_months = OutputXML.get(i).get(20);
				}
				if(!(OutputXML.get(i).get(21) == null || "".equalsIgnoreCase(OutputXML.get(i).get(21))) ){
					DPD_60_in_last_18_months = OutputXML.get(i).get(21);
				}
				if(!(OutputXML.get(i).get(22) == null || "".equalsIgnoreCase(OutputXML.get(i).get(22))) ){
					propertyvalue = OutputXML.get(i).get(22);
				}				
				if(!(OutputXML.get(i).get(23) == null || "".equalsIgnoreCase(OutputXML.get(i).get(23))) ){
					loan_disbursal_date = OutputXML.get(i).get(23);
				}
				if(!(OutputXML.get(i).get(24) == null || "".equalsIgnoreCase(OutputXML.get(i).get(24))) ){
					marketingcode = OutputXML.get(i).get(24);
				}
				if(!(OutputXML.get(i).get(25) == null || "".equalsIgnoreCase(OutputXML.get(i).get(25))) ){
					DPD_30_in_last_3_months = OutputXML.get(i).get(25);
				}
				if(!(OutputXML.get(i).get(26) == null || "".equalsIgnoreCase(OutputXML.get(i).get(26))) ){
					DPD_30_in_last_6_months	 = OutputXML.get(i).get(26);
				}
				if(!(OutputXML.get(i).get(27) == null || "".equalsIgnoreCase(OutputXML.get(i).get(27))) ){
					DPD_30_in_last_9_months	 = OutputXML.get(i).get(27);
				}
				if(!(OutputXML.get(i).get(28) == null || "".equalsIgnoreCase(OutputXML.get(i).get(28))) ){
					DPD_30_in_last_12_months = OutputXML.get(i).get(28);
				}
				if(!(OutputXML.get(i).get(29) == null || "".equalsIgnoreCase(OutputXML.get(i).get(29))) ){
					DPD_30_in_last_18_months = OutputXML.get(i).get(29);
				}				
				if(!(OutputXML.get(i).get(30) == null || "".equalsIgnoreCase(OutputXML.get(i).get(30))) ){
					DPD_30_in_last_24_months = OutputXML.get(i).get(30);
				}
				if(!(OutputXML.get(i).get(31) == null || "".equalsIgnoreCase(OutputXML.get(i).get(31))) ){
					DPD_60_in_last_3_months = OutputXML.get(i).get(31);
				}
				if(!(OutputXML.get(i).get(32) == null || "".equalsIgnoreCase(OutputXML.get(i).get(32))) ){
					DPD_60_in_last_6_months = OutputXML.get(i).get(32);
				}				
				if(!(OutputXML.get(i).get(33) == null || "".equalsIgnoreCase(OutputXML.get(i).get(33))) ){
					DPD_60_in_last_9_months = OutputXML.get(i).get(33);
				}
				if(!(OutputXML.get(i).get(34) == null || "".equalsIgnoreCase(OutputXML.get(i).get(34))) ){
					DPD_60_in_last_12_months = OutputXML.get(i).get(34);
				}
				if(!(OutputXML.get(i).get(35) == null || "".equalsIgnoreCase(OutputXML.get(i).get(35))) ){
					DPD_60_in_last_18_months = OutputXML.get(i).get(35);
				}
				if(!(OutputXML.get(i).get(36) == null || "".equalsIgnoreCase(OutputXML.get(i).get(36))) ){
					DPD_60_in_last_24_months = OutputXML.get(i).get(36);
				}				
				if(!(OutputXML.get(i).get(37) == null || "".equalsIgnoreCase(OutputXML.get(i).get(37))) ){
					DPD_90_in_last_3_months = OutputXML.get(i).get(37);
				}
				if(!(OutputXML.get(i).get(38) == null || "".equalsIgnoreCase(OutputXML.get(i).get(38))) ){
					DPD_90_in_last_6_months = OutputXML.get(i).get(38);
				}
				if(!(OutputXML.get(i).get(39) == null || "".equalsIgnoreCase(OutputXML.get(i).get(39))) ){
					DPD_90_in_last_9_months = OutputXML.get(i).get(39);
				}				
				if(!(OutputXML.get(i).get(40) == null || "".equalsIgnoreCase(OutputXML.get(i).get(40))) ){
					DPD_90_in_last_12_months = OutputXML.get(i).get(40);
				}
				if(!(OutputXML.get(i).get(41) == null || "".equalsIgnoreCase(OutputXML.get(i).get(41))) ){
					DPD_90_in_last_18_months = OutputXML.get(i).get(41);
				}
				if(!(OutputXML.get(i).get(42) == null || "".equalsIgnoreCase(OutputXML.get(i).get(42))) ){
					DPD_90_in_last_24_months = OutputXML.get(i).get(42);
				}				
				if(!(OutputXML.get(i).get(43) == null || "".equalsIgnoreCase(OutputXML.get(i).get(43))) ){
					DPD_120_in_last_3_months = OutputXML.get(i).get(43);
				}
				if(!(OutputXML.get(i).get(44) == null || "".equalsIgnoreCase(OutputXML.get(i).get(44))) ){
					DPD_120_in_last_6_months = OutputXML.get(i).get(44);
				}
				if(!(OutputXML.get(i).get(45) == null || "".equalsIgnoreCase(OutputXML.get(i).get(45))) ){
					DPD_120_in_last_9_months = OutputXML.get(i).get(45);
				}				
				if(!(OutputXML.get(i).get(46) == null || "".equalsIgnoreCase(OutputXML.get(i).get(46))) ){
					DPD_120_in_last_12_months = OutputXML.get(i).get(46);
				}
				if(!(OutputXML.get(i).get(47) == null || "".equalsIgnoreCase(OutputXML.get(i).get(47))) ){
					DPD_120_in_last_18_months = OutputXML.get(i).get(47);
				}
				if(!(OutputXML.get(i).get(48) == null || "".equalsIgnoreCase(OutputXML.get(i).get(48))) ){
					DPD_120_in_last_24_months = OutputXML.get(i).get(48);
				}				
				if(!(OutputXML.get(i).get(49) == null || "".equalsIgnoreCase(OutputXML.get(i).get(49))) ){
					DPD_150_in_last_3_months = OutputXML.get(i).get(49);
				}
				if(!(OutputXML.get(i).get(50) == null || "".equalsIgnoreCase(OutputXML.get(i).get(50))) ){
					DPD_150_in_last_6_months = OutputXML.get(i).get(50);
				}
				if(!(OutputXML.get(i).get(51) == null || "".equalsIgnoreCase(OutputXML.get(i).get(51))) ){
					DPD_150_in_last_9_months = OutputXML.get(i).get(51);
				}				
				if(!(OutputXML.get(i).get(52) == null || "".equalsIgnoreCase(OutputXML.get(i).get(52))) ){
					DPD_150_in_last_12_months = OutputXML.get(i).get(52);
				}
				if(!(OutputXML.get(i).get(53) == null || "".equalsIgnoreCase(OutputXML.get(i).get(53))) ){
					DPD_150_in_last_18_months = OutputXML.get(i).get(53);
				}
				if(!(OutputXML.get(i).get(54) == null || "".equalsIgnoreCase(OutputXML.get(i).get(54))) ){
					DPD_150_in_last_24_months = OutputXML.get(i).get(54);
				}				
				if(!(OutputXML.get(i).get(55) == null || "".equalsIgnoreCase(OutputXML.get(i).get(55))) ){
					DPD_180_in_last_3_months = OutputXML.get(i).get(55);
				}
				if(!(OutputXML.get(i).get(56) == null || "".equalsIgnoreCase(OutputXML.get(i).get(56))) ){
					DPD_180_in_last_6_months = OutputXML.get(i).get(56);
				}
				if(!(OutputXML.get(i).get(57) == null || "".equalsIgnoreCase(OutputXML.get(i).get(57))) ){
					DPD_180_in_last_9_months = OutputXML.get(i).get(57);
				}				
				if(!(OutputXML.get(i).get(58) == null || "".equalsIgnoreCase(OutputXML.get(i).get(58))) ){
					DPD_180_in_last_12_months = OutputXML.get(i).get(58);
				}
				if(!(OutputXML.get(i).get(59) == null || "".equalsIgnoreCase(OutputXML.get(i).get(59))) ){
					DPD_180_in_last_24_months = OutputXML.get(i).get(59);
				}
				if(!(OutputXML.get(i).get(60) == null || "".equalsIgnoreCase(OutputXML.get(i).get(60))) ){
					CardExpiryDate = OutputXML.get(i).get(60);
				}

				if(!(OutputXML.get(i).get(61) == null || "".equalsIgnoreCase(OutputXML.get(i).get(61))) )
				{
					Consider_For_Obligations = OutputXML.get(i).get(61);
					//Prateek Change 05-12-2017 : In case of false, value set was Y , changed to N
					if ("false".equalsIgnoreCase(Consider_For_Obligations))
					{
						Consider_For_Obligations="N";
					}
					else {
						Consider_For_Obligations="Y";
					}
				}

				if(!(OutputXML.get(i).get(62) == null || "".equalsIgnoreCase(OutputXML.get(i).get(62))) )
				{
					phase = OutputXML.get(i).get(62);
					if (phase.startsWith("C")){
						phase="C";
					}
					else {
						phase="A";
					}
				}
				if(!(OutputXML.get(i).get(64) == null || "".equalsIgnoreCase(OutputXML.get(i).get(64))) ){
					writeoffStat = OutputXML.get(i).get(64);
				}
				if(!(OutputXML.get(i).get(65) == null || "".equalsIgnoreCase(OutputXML.get(i).get(65))) ){
					writeoffstatdt = OutputXML.get(i).get(65);
				}
				if(!(OutputXML.get(i).get(66) == null || "".equalsIgnoreCase(OutputXML.get(i).get(66))) ){
					lastrepmtdt = OutputXML.get(i).get(66);
				}
				if(!(OutputXML.get(i).get(67) == null || "".equalsIgnoreCase(OutputXML.get(i).get(67))) ){
					Limit_increase = OutputXML.get(i).get(67);
					if ("false".equalsIgnoreCase(Limit_increase))
					{
						Limit_increase="N";
					}
					else{
						Limit_increase="Y";
					}
				}
				if(!(OutputXML.get(i).get(68) == null || "".equalsIgnoreCase(OutputXML.get(i).get(68))) )
				{
					//part_settlement_date = OutputXML.get(i).get(67);
					String abc=OutputXML.get(i).get(68);
					if (abc != null && "".equalsIgnoreCase(abc))
					{
						abc=abc.substring(0, 10)+"split"+abc.substring(10,abc.length() );
						String abcsa[]=abc.split("split");
						part_settlement_date = abcsa[0];
						part_settlement_amount = abcsa[1];
					}
				}
				if(!(OutputXML.get(i).get(69) == null || "".equalsIgnoreCase(OutputXML.get(i).get(69))) ){
					SchemeCardProduct = OutputXML.get(i).get(69);
				}
				if(!(OutputXML.get(i).get(70) == null || "".equalsIgnoreCase(OutputXML.get(i).get(70))) ){
					General_Status = OutputXML.get(i).get(70);
				}
				if(!(OutputXML.get(i).get(71) == null || "".equalsIgnoreCase(OutputXML.get(i).get(71))) ){
					Internal_WriteOff_Check = OutputXML.get(i).get(71).toString();

				}
				/*if(!(OutputXML.get(i).get(72) == null || "".equalsIgnoreCase(OutputXML.get(i).get(72))) ){
					Type_of_OD = OutputXML.get(i).get(72).toString();

				}*/
				//below tags added by saurabh on 11th July.
				if (!(OutputXML.get(i).get(72) == null || OutputXML.get(i).get(72).equals(""))) {
					amt_paid_last6mnths = OutputXML.get(i).get(72).toString();
				}
				if (!(OutputXML.get(i).get(73) == null || OutputXML.get(i).get(73).equals(""))) {
					interest_rate = OutputXML.get(i).get(73).toString();
				}
				String sQueryCombinedLimit = "select Distinct(COMBINEDLIMIT_ELIGIBILITY) from ng_master_cardProduct where code='"+SchemeCardProduct+"'";
				//RLOS.mLogger.info("sQueryCombinedLimit"+sQueryCombinedLimit);
				List<List<String>> sQueryCombinedLimitXML = formObject.getDataFromDataSource(sQueryCombinedLimit);
				try{
					if(sQueryCombinedLimitXML!=null && sQueryCombinedLimitXML.size()>0 && sQueryCombinedLimitXML.get(0)!=null){
						Combined_Limit=sQueryCombinedLimitXML.get(0).get(0).equalsIgnoreCase("1")?"Y":"N";
					}
				}
				catch(Exception e){
					RLOS.mLogger.info("Exception occured at sQueryCombinedLimit for"+sQueryCombinedLimit);

				}
				String sQuerySecuredCard = "select count(*) from ng_master_cardProduct where code='"+SchemeCardProduct+"'  and subproduct='SEC'";
				//RLOS.mLogger.info( "sQueryCombinedLimit"+sQueryCombinedLimit);
				List<List<String>> sQuerySecuredCardXML = formObject.getDataFromDataSource(sQuerySecuredCard);
				try{
					if(sQuerySecuredCardXML!=null && sQuerySecuredCardXML.size()>0 && sQuerySecuredCardXML.get(0)!=null){
						SecuredCard=sQuerySecuredCardXML.get(0).get(0).equalsIgnoreCase("0")?"N":"Y";
					}	
				}
				catch(Exception e){
					RLOS.mLogger.info("Exception occured at SecuredCard for"+SecuredCard);

				}
				if(cifId!=null && !"".equalsIgnoreCase(cifId) && !"null".equalsIgnoreCase(cifId)){
					//RLOS.mLogger.info( "asdasdasd");
					add_xml_str = add_xml_str + "<InternalBureauIndividualProducts><applicant_id>"+cifId+"</applicant_id>";
					add_xml_str = add_xml_str + "<internal_bureau_individual_products_id>"+agreementId+"</internal_bureau_individual_products_id>";
					add_xml_str = add_xml_str + "<type_product>"+product_type+"</type_product>";
					//RLOS.mLogger.info( "InternalBureauIndividualProducts OutputXML sQuery sQuery sQuery: "+ OutputXML.get(i).get(63));
					//RLOS.mLogger.info( "LoanType sQuery sQuery sQuery: "+ LoanType);

					if ("cards".equalsIgnoreCase(OutputXML.get(i).get(63))){	
						if (SchemeCardProduct.startsWith("LOC")){
							add_xml_str = add_xml_str + "<contract_type>IM</contract_type>";
						}
						else{
							add_xml_str = add_xml_str + "<contract_type>CC</contract_type>";
						}
					}
					else if ("Loans".equalsIgnoreCase(OutputXML.get(i).get(63)))
					{
						if (LoanType.equalsIgnoreCase("MURABAHA")){
							//RLOS.mLogger.info( "SchemeCardProduct sQuery sQuery sQuery: "+ SchemeCardProduct);
							if (SchemeCardProduct.equalsIgnoreCase("AMAL PERSONAL FINANCE")){
								add_xml_str = add_xml_str + "<contract_type>IPL</contract_type>";
							}
							else if (SchemeCardProduct.equalsIgnoreCase("AMAL AUTO FINANCE")){
								add_xml_str = add_xml_str + "<contract_type>IAL</contract_type>";
							}
						}
						else if (LoanType.equalsIgnoreCase("AUTO")){
							add_xml_str = add_xml_str + "<contract_type>AL</contract_type>";
						}
						else if (LoanType.equalsIgnoreCase("HOME")){
							add_xml_str = add_xml_str + "<contract_type>ML</contract_type>";
						}
						else if (LoanType.equalsIgnoreCase("IJARAH")){
							add_xml_str = add_xml_str + "<contract_type>IML</contract_type>";
						}
						else if (LoanType.equalsIgnoreCase("PERSONAL")){
							add_xml_str = add_xml_str + "<contract_type>PL</contract_type>";
						}
						else if (LoanType.equalsIgnoreCase("HOME IN ONE")){
							add_xml_str = add_xml_str + "<contract_type>HIO</contract_type>";
						}
						else{
							add_xml_str = add_xml_str + "<contract_type>TRADE</contract_type>";
						}
					}	
					else if ("OVERDRAFT".equalsIgnoreCase(OutputXML.get(i).get(63)))
					{
						add_xml_str = add_xml_str + "<contract_type>OD</contract_type>";

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
					if ("cards".equalsIgnoreCase(OutputXML.get(i).get(63))||"OVERDRAFT".equalsIgnoreCase(OutputXML.get(i).get(63))){

						add_xml_str = add_xml_str + "<credit_limit>"+totalloanamount+"</credit_limit>"; 
					}
					add_xml_str = add_xml_str + "<overdue_amount>"+overdueamt+"</overdue_amount>"; 
					add_xml_str = add_xml_str + "<no_of_days_payment_delay>"+nofdayspmtdelay+"</no_of_days_payment_delay>"; 
					add_xml_str = add_xml_str + "<mob>"+monthsonbook+"</mob>"; 
					add_xml_str = add_xml_str + "<last_repayment_date>"+lastrepmtdt+"</last_repayment_date>"; 
					add_xml_str = add_xml_str + "<currently_current>"+currentlycurrent+"</currently_current>"; 
					//add_xml_str = add_xml_str + "<current_utilization>"+currmaxutil+"</current_utilization>"; 
					add_xml_str = add_xml_str + "<dpd_30_last_6_mon>"+DPD_30_in_last_6_months+"</dpd_30_last_6_mon>"; 
					add_xml_str = add_xml_str + "<dpd_60p_in_last_18_mon>"+DPD_60_in_last_18_months+"</dpd_60p_in_last_18_mon>"; 

					if ("cards".equalsIgnoreCase(OutputXML.get(i).get(63))){
						add_xml_str = add_xml_str + "<card_product>"+SchemeCardProduct+"</card_product>"; 
					}
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
					add_xml_str = add_xml_str + "<combined_limit_flag>"+Combined_Limit+"</combined_limit_flag>"; 
					add_xml_str = add_xml_str + "<secured_card_flag>"+SecuredCard+"</secured_card_flag>"; 
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

					if (totalnoofinstalments!=null && remaininginstalments!=null && !"".equalsIgnoreCase(totalnoofinstalments) &&  !"".equalsIgnoreCase(remaininginstalments)) {
						paid_installment= Integer.toString(Integer.parseInt(totalnoofinstalments) -Integer.parseInt(remaininginstalments));
						//RLOS.mLogger.info("Inside paid_installment paid_installment"+paid_installment);

					}
					/*if (NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard").equalsIgnoreCase(ReqProd)){

						add_xml_str = add_xml_str + "<no_of_paid_installment>"+paid_installment+"</no_of_paid_installment><write_off_amount>"+Internal_WriteOff_Check+"</write_off_amount><company_flag>N</company_flag></InternalBureauIndividualProducts>";
					}
					else{*/
					add_xml_str = add_xml_str + "<no_of_paid_installment>"+paid_installment+"</no_of_paid_installment><write_off_amount>"+Internal_WriteOff_Check+"</write_off_amount><company_flag>N</company_flag><type_of_od>"+Type_of_OD+"</type_of_od><amt_paid_last6mnths>"+ amt_paid_last6mnths+ "</amt_paid_last6mnths><topup_indicator>"+Limit_increase+"</topup_indicator><interest_rate>"+interest_rate+"</interest_rate></InternalBureauIndividualProducts>";


				}	

			}
		}
		catch(Exception e){
			RLOS.mLogger.info( "Internal liab tag Cration: "+ printException(e));
		}
		RLOS.mLogger.info( "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}

	public String InternalBureauPipelineProducts(){
		//RLOS.mLogger.info( "inside InternalBureauPipelineProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String  add_xml_str ="";
		String sQuery = "SELECT cifid,loantype,custroletype,lastupdatedate,totalamount,totalnoofinstalments,totalloanamount,agreementId,NoOfDaysInPipeline,case when Consider_For_Obligations is null or Consider_For_Obligations='True' then 'Y' else 'N' end as Consider_For_Obligations,NextInstallmentAmt FROM ng_RLOS_CUSTEXPOSE_LoanDetails  with (nolock) where Wi_Name = '"+formObject.getWFWorkitemName()+"' and (LoanStat = 'Pipeline' or loanstat='CAS-pipeline' or loanStat='al-pipeline')";//query changed by akshay on 29/5/18 for proc 10063
		try{

			List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);

			for (int i = 0; i<OutputXML.size();i++){

				String cifId = "";
				String Product = "";
				String lastUpdateDate = ""; 
				String TotAmount = "";
				String TotNoOfInstlmnt = "";
				String TotLoanAmt = "";
				String agreementId = "";
				String NoOfDaysInPipeline="";
				String ConsiderForOblig = "";
				String EMI = "";

				if(!(OutputXML.get(i).get(0) == null || "".equalsIgnoreCase(OutputXML.get(i).get(0))) ){
					cifId = OutputXML.get(i).get(0);
				}
				if(!(OutputXML.get(i).get(1) == null || "".equalsIgnoreCase(OutputXML.get(i).get(1))) ){
					Product = OutputXML.get(i).get(1);
				}
				if(!(OutputXML.get(i).get(2) == null || "".equalsIgnoreCase(OutputXML.get(i).get(2))) ){
				}
				if(!(OutputXML.get(i).get(3) == null || "".equalsIgnoreCase(OutputXML.get(i).get(3))) ){
					lastUpdateDate = OutputXML.get(i).get(3);
				}
				if(!(OutputXML.get(i).get(4) == null || "".equalsIgnoreCase(OutputXML.get(i).get(4))) ){
					TotAmount = OutputXML.get(i).get(4);
				}
				if(!(OutputXML.get(i).get(5) == null || "".equalsIgnoreCase(OutputXML.get(i).get(5))) ){
					TotNoOfInstlmnt = OutputXML.get(i).get(5);
				}
				if(!(OutputXML.get(i).get(6) == null || "".equalsIgnoreCase(OutputXML.get(i).get(6))) ){
					TotLoanAmt = OutputXML.get(i).get(6);
				}
				if(!(OutputXML.get(i).get(7) == null || "".equalsIgnoreCase(OutputXML.get(i).get(7))) ){
					agreementId = OutputXML.get(i).get(7);
				}
				if(!(OutputXML.get(i).get(8) == null || "".equalsIgnoreCase(OutputXML.get(i).get(8))) ){
					NoOfDaysInPipeline = OutputXML.get(i).get(8);
				}
				if(!(OutputXML.get(i).get(9) == null || "".equalsIgnoreCase(OutputXML.get(i).get(9))) ){
					ConsiderForOblig = OutputXML.get(i).get(9);
				}
				if(!(OutputXML.get(i).get(10) == null || "".equalsIgnoreCase(OutputXML.get(i).get(10))) ){
					EMI = OutputXML.get(i).get(10);
				}
				if(cifId!=null && !"".equalsIgnoreCase(cifId) && !"null".equalsIgnoreCase(cifId))
				{
					add_xml_str = add_xml_str + "<InternalBureauPipelineProducts>";
					add_xml_str = add_xml_str + "<applicant_id>"+cifId+"</applicant_id>";
					add_xml_str = add_xml_str + "<internal_bureau_pipeline_products_id>"+agreementId+"</internal_bureau_pipeline_products_id>";// to be populated later
					add_xml_str = add_xml_str + "<ppl_provider_no>RAKBANK</ppl_provider_no>";
					//add_xml_str = add_xml_str + "<ppl_product>"+Product+"</ppl_product>";
					add_xml_str = add_xml_str + "<ppl_type_of_contract>"+Product+"</ppl_type_of_contract>";
					add_xml_str = add_xml_str + "<ppl_phase>PIPELINE</ppl_phase>"; // to be populated later

					add_xml_str = add_xml_str + "<ppl_role>Primary</ppl_role>";
					add_xml_str = add_xml_str + "<ppl_date_of_last_update>"+lastUpdateDate+"</ppl_date_of_last_update>";
					add_xml_str = add_xml_str + "<ppl_total_amount>"+TotAmount+"</ppl_total_amount>";
					add_xml_str = add_xml_str + "<ppl_no_of_instalments>"+TotNoOfInstlmnt+"</ppl_no_of_instalments>";
					add_xml_str = add_xml_str + "<ppl_credit_limit>"+TotLoanAmt+"</ppl_credit_limit>";

					add_xml_str = add_xml_str + "<ppl_no_of_days_in_pipeline>"+NoOfDaysInPipeline+"</ppl_no_of_days_in_pipeline>";
					add_xml_str = add_xml_str + "<company_flag>N</company_flag>";
					add_xml_str = add_xml_str + "<ppl_consider_for_obligation>"+ConsiderForOblig+"</ppl_consider_for_obligation>";
					add_xml_str = add_xml_str + "<ppl_emi>"+EMI+"</ppl_emi>";
					add_xml_str = add_xml_str + "</InternalBureauPipelineProducts>"; // to be populated later
				}
				//RLOS.mLogger.info( "Internal liab tag Cration: "+ add_xml_str);
			}
		}catch(Exception e){
			RLOS.mLogger.info( "Exception occurred in InternalBureauPipelineProducts()"+printException(e));

		}

		return add_xml_str;
	}

	public String ExternalBureauData(){
		//RLOS.mLogger.info( "inside ExternalBureauData : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//Deepak 31 Dec 2018 Change done as space was not there from 
		String sQuery = "select CifId,fullnm,TotalOutstanding,TotalOverdue,NoOfContracts,Total_Exposure,WorstCurrentPaymentDelay,Worst_PaymentDelay_Last24Months,Worst_Status_Last24Months,Nof_Records,NoOf_Cheque_Return_Last3,Nof_DDES_Return_Last3Months,Nof_Cheque_Return_Last6,DPD30_Last6Months,(select max(ExternalWriteOffCheck) ExternalWriteOffCheck from ((select convert(int,isNULL(ExternalWriteOffCheck,0)) ExternalWriteOffCheck  from ng_rlos_cust_extexpo_CardDetails with(nolock) where Wi_name  = '"+formObject.getWFWorkitemName()+"' union select convert(int,isNULL(ExternalWriteOffCheck,0))ExternalWriteOffCheck from ng_rlos_cust_extexpo_LoanDetails with(nolock) where wi_name  = '"+formObject.getWFWorkitemName()+"')) as ExternalWriteOffCheck),(select count(*) from (select DisputeAlert from ng_rlos_cust_extexpo_LoanDetails with(nolock) where Wi_Name = '"+formObject.getWFWorkitemName()+"' and DisputeAlert='1' union  select DisputeAlert from ng_rlos_cust_extexpo_CardDetails with(nolock) where Wi_Name = '"+formObject.getWFWorkitemName()+"' and DisputeAlert='1') as tempTable) from NG_rlos_custexpose_Derived with(nolock) where wi_name  = '"+formObject.getWFWorkitemName()+"' and Request_type= 'ExternalExposure'";
		String Wi_Name=formObject.getWFWorkitemName();
		String AecbHistQuery = "select isnull(max(AECBHistMonthCnt),0) as AECBHistMonthCnt from ( select MAX(cast(isnull(AECBHistMonthCnt,'0') as int)) as AECBHistMonthCnt  from ng_rlos_cust_extexpo_CardDetails with (nolock) where  wi_name  = '"+ Wi_Name + "' union select Max(cast(isnull(AECBHistMonthCnt,'0') as int)) as AECBHistMonthCnt from ng_rlos_cust_extexpo_LoanDetails with (nolock) where wi_name  = '"+ Wi_Name + "') as ext_expo";
		String  add_xml_str ="";
		try{
			List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);

			List<List<String>> AecbHistQueryData = formObject.getDataFromDataSource(AecbHistQuery);
			//RLOS.mLogger.info( "inside ExternalBureauData : Value of AECBHistMonthCount: "+AecbHistQueryData.get(0).get(0));

			if (OutputXML.size()==0)
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
				String aecb_score= formObject.getNGValue("cmplx_Liability_New_AECBScore");
				String range = formObject.getNGValue("cmplx_Liability_New_Range");
				String refNo = formObject.getNGValue("cmplx_Liability_New_ReferenceNo");
				RLOS.mLogger.info( "aecb_score :"+aecb_score+" range :: "+range+" refNo:: "+refNo);
				
				
				add_xml_str = add_xml_str + "<ExternalBureau>"; 
				add_xml_str = add_xml_str + "<applicant_id>"+CifId+"</applicant_id>";
				add_xml_str = add_xml_str + "<bureauone_ref_no>"+refNo+"</bureauone_ref_no>";
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
				//changes done by shivang for 2.1 
				add_xml_str = add_xml_str + "<aecb_score>"+aecb_score+"</aecb_score>";
				add_xml_str = add_xml_str + "<range>"+range+"</range>";
				add_xml_str = add_xml_str + "<company_flag>N</company_flag></ExternalBureau>";



				//RLOS.mLogger.info( "Inside If Internal liab tag Cration: "+ add_xml_str);
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
					String ExternalWriteOffCheck="";
					String dispute_alert="";
					String aecb_score= formObject.getNGValue("cmplx_Liability_New_AECBScore");
					String range = formObject.getNGValue("cmplx_Liability_New_Range");
					String refNo = formObject.getNGValue("cmplx_Liability_New_ReferenceNo");
					RLOS.mLogger.info( "aecb_score :"+aecb_score+" range :: "+range+" refNo:: "+refNo);

					if(!(OutputXML.get(i).get(1) == null || "".equalsIgnoreCase(OutputXML.get(i).get(1))) ){
						fullnm = OutputXML.get(i).get(1);
					}				
					if(!(OutputXML.get(i).get(2) == null || "".equalsIgnoreCase(OutputXML.get(i).get(2))) ){
						TotalOutstanding = OutputXML.get(i).get(2);

					}
					if(!(OutputXML.get(i).get(3) == null || "".equalsIgnoreCase(OutputXML.get(i).get(3))) ){
						TotalOverdue = OutputXML.get(i).get(3);
					}
					if(!(OutputXML.get(i).get(4) == null || "".equalsIgnoreCase(OutputXML.get(i).get(4))) ){
						NoOfContracts = OutputXML.get(i).get(4);
					}				
					if(!(OutputXML.get(i).get(5) == null || "".equalsIgnoreCase(OutputXML.get(i).get(5))) ){
						Total_Exposure = OutputXML.get(i).get(5);
					}
					if(OutputXML.get(i).get(6)!=null && !OutputXML.get(i).get(6).isEmpty() &&  !"".equalsIgnoreCase(OutputXML.get(i).get(6)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(6)) ){
						WorstCurrentPaymentDelay = OutputXML.get(i).get(6);
					}
					if(OutputXML.get(i).get(7)!=null && !OutputXML.get(i).get(7).isEmpty() &&  !"".equalsIgnoreCase(OutputXML.get(i).get(7)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(7)) ){
						Worst_PaymentDelay_Last24Months = OutputXML.get(i).get(7);
					}				
					if(OutputXML.get(i).get(8)!=null && !OutputXML.get(i).get(8).isEmpty() &&  !"".equalsIgnoreCase(OutputXML.get(i).get(8)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(8)) ){
						Worst_Status_Last24Months = OutputXML.get(i).get(8);
					}
					if(!(OutputXML.get(i).get(9) == null || "".equalsIgnoreCase(OutputXML.get(i).get(9))) ){
						Nof_Records = OutputXML.get(i).get(9);
					}
					if(!(OutputXML.get(i).get(10) == null || "".equalsIgnoreCase(OutputXML.get(i).get(10))) ){
						NoOf_Cheque_Return_Last3 = OutputXML.get(i).get(10);
					}				
					if(!(OutputXML.get(i).get(11) == null || "".equalsIgnoreCase(OutputXML.get(i).get(11))) ){
						Nof_DDES_Return_Last3Months = OutputXML.get(i).get(11);
					}
					if(OutputXML.get(i).get(12)!=null && !OutputXML.get(i).get(12).isEmpty() &&  !"".equalsIgnoreCase(OutputXML.get(i).get(12)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(12)) ){
						//RLOS.mLogger.info( "asdasdasd"+OutputXML.get(i).get(12));
						Nof_Cheque_Return_Last6 = OutputXML.get(i).get(12);
					}
					if(!(OutputXML.get(i).get(13) == null || "".equalsIgnoreCase(OutputXML.get(i).get(13))) ){
						DPD30_Last6Months = OutputXML.get(i).get(13);
					}
					//RLOS.mLogger.info( "asdaasdasdsdasd"+OutputXML.get(i).get(14));

					if(!(OutputXML.get(i).get(14) == null || "".equalsIgnoreCase(OutputXML.get(i).get(14))) && !"0".equalsIgnoreCase(OutputXML.get(i).get(14)) ){
						//RLOS.mLogger.info( "asdasdasd"+OutputXML.get(i).get(14));
						ExternalWriteOffCheck = OutputXML.get(i).get(14);
					}
					if(!(OutputXML.get(i).get(15) == null || "".equalsIgnoreCase(OutputXML.get(i).get(15))) ){
						dispute_alert = OutputXML.get(i).get(15);
						if(Integer.parseInt(dispute_alert)>0){
							dispute_alert="Y";
						}
						else{
							dispute_alert="N";
						}
					}


					add_xml_str = add_xml_str + "<ExternalBureau>"; 
					add_xml_str = add_xml_str + "<applicant_id>"+CifId+"</applicant_id>";
					add_xml_str = add_xml_str + "<bureauone_ref_no>"+refNo+"</bureauone_ref_no>";
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
					add_xml_str = add_xml_str + "<prod_external_writeoff_amount>"+ExternalWriteOffCheck+"</prod_external_writeoff_amount>";

					add_xml_str = add_xml_str + "<no_months_aecb_history >"+AecbHistQueryData.get(0).get(0)+"</no_months_aecb_history >";
					add_xml_str = add_xml_str + "<aecb_score>"+aecb_score+"</aecb_score>";
					add_xml_str = add_xml_str + "<range>"+range+"</range>";
					add_xml_str = add_xml_str + "<company_flag>N</company_flag>";
					add_xml_str = add_xml_str + "<dispute_alert>"+dispute_alert+"</dispute_alert></ExternalBureau>";

				}
			}
			//RLOS.mLogger.info( "Inside else Internal liab tag Cration: "+ add_xml_str);
			return add_xml_str;


		}

		catch(Exception e)
		{
			RLOS.mLogger.info( "Exception occurred in externalBureauData()"+e.getMessage()+printException(e));
			return null;
		}
	}

	public String ExternalBouncedCheques(){
		//RLOS.mLogger.info( "inside ExternalBouncedCheques : ");
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


			if(!(OutputXML.get(i).get(1) == null || "".equalsIgnoreCase(OutputXML.get(i).get(1))) ){
				chqNo = OutputXML.get(i).get(1);
			}
			if(!(OutputXML.get(i).get(2) == null || "".equalsIgnoreCase(OutputXML.get(i).get(2))) ){
				Amount = OutputXML.get(i).get(2);
			}
			if(!(OutputXML.get(i).get(3) == null || "".equalsIgnoreCase(OutputXML.get(i).get(3))) ){
				Reason = OutputXML.get(i).get(3);
			}
			if(!(OutputXML.get(i).get(4) == null || "".equalsIgnoreCase(OutputXML.get(i).get(4))) ){
				returnDate = OutputXML.get(i).get(4);
			}
			if(!(OutputXML.get(i).get(5) == null || "".equalsIgnoreCase(OutputXML.get(i).get(5))) ){
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
		//RLOS.mLogger.info( "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}

	public String ExternalBureauIndividualProducts(){
		//RLOS.mLogger.info("RLOSCommon java fileinside ExternalBureauIndividualProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//Query Changed on 1st Nov
		//String sQuery = "select CifId,ServiceID,ServiceType,ServiceId,ServiceStat,CustRoleType,SubscriptionDt,SvcExpDt,'','','','','',WriteoffStat,WriteoffStatDt,'',OverdueAmount,NofDaysPmtDelay,MonthsOnBook,'',IsCurrent,CurUtilRate,DPD30_Last6Months,'',AECBHistMonthCnt,DPD5_Last3Months,'','','','',isnull(Consider_For_Obligations,'true'),case when IsDuplicate= '1' then 'Y' else 'N' end,'' from ng_rlos_cust_extexpo_ServicesDetails where wi_name  =  '"+formObject.getWFWorkitemName()+"' ";//sagarika
		//Corrected by Deepak.
		String sQuery = "select CifId,AgreementId,LoanType,ProviderNo,LoanStat,CustRoleType,LoanApprovedDate,LoanMaturityDate,OutstandingAmt,TotalAmt,PaymentsAmt,TotalNoOfInstalments,RemainingInstalments,WriteoffStat,WriteoffStatDt,CreditLimit,OverdueAmt,NofDaysPmtDelay,MonthsOnBook,lastrepmtdt,IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last12Months,AECBHistMonthCnt,DPD5_Last3Months,'' as qc_Amnt,'' as Qc_emi,'' as Cac_indicator,Take_Over_Indicator,ISNULL(Consider_For_Obligations,'true'),case when IsDuplicate= '1' then 'Y' else 'N' end,avg_utilization from ng_rlos_cust_extexpo_LoanDetails with (nolock) where wi_name= '"+ formObject.getWFWorkitemName()
				+ "'  and LoanStat != 'Pipeline'  and ProviderNo != 'B01' union select CifId,CardEmbossNum,CardType,ProviderNo,CardStatus,CustRoleType,StartDate,ClosedDate,CurrentBalance,'' as col6,PaymentsAmount,NoOfInstallments,'' as col5,WriteoffStat,WriteoffStatDt,CashLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,lastrepmtdt,IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last12Months,AECBHistMonthCnt,DPD5_Last3Months,qc_amt,qc_emi,CAC_Indicator,Take_Over_Indicator,ISNULL(Consider_For_Obligations,'true'),case when IsDuplicate= '1' then 'Y' else 'N' end,avg_utilization from ng_rlos_cust_extexpo_CardDetails with (nolock) where wi_name  =  '"
				+ formObject.getWFWorkitemName()+ "' and cardstatus != 'Pipeline'  and ProviderNo != 'B01' union select CifId,AcctId,AcctType,ProviderNo,AcctStat,CustRoleType,StartDate,ClosedDate,OutStandingBalance,TotalAmount,PaymentsAmount,'','',WriteoffStat,WriteoffStatDt,CreditLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,'',IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last12Months,AECBHistMonthCnt,DPD5_Last3Months,'','','','',isnull(Consider_For_Obligations,'true'),case when IsDuplicate= '1' then 'Y' else 'N' end,'' from ng_rlos_cust_extexpo_AccountDetails with (nolock)  where wi_name  =  '"+formObject.getWFWorkitemName()+"' union"
				+" select CifId,ServiceID,ServiceType,ProviderNo,ServiceStat,CustRoleType,SubscriptionDt,SvcExpDt,'','','','','',WriteoffStat,WriteoffStatDt,'',OverDueAmount,NofDaysPmtDelay,MonthsOnBook,'',IsCurrent,CurUtilRate,'',DPD30_Last6Months,AECBHistMonthCnt,DPD5_Last3Months,'','','','',isnull(Consider_For_Obligations,'true'),case when IsDuplicate= '1' then 'Y' else 'N' end,'' from ng_rlos_cust_extexpo_ServicesDetails with (nolock)  where ServiceStat='Active' and wi_name  =  '"+formObject.getWFWorkitemName()+"'";
		//Query Changed on 1st Nov

		//RLOS.mLogger.info("ExternalBureauIndividualProducts sQuery"+sQuery);
		String  add_xml_str ="";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		//RLOS.mLogger.info("ExternalBureauIndividualProducts list size"+OutputXML.size());
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
			String DPD60Last12Months = "";
			String AECBHistMonthCnt = "";
			String DPD30Last6Months = "";
			String currently_current = "";
			String current_utilization = "";
			String delinquent_in_last_3months = "";
			String QC_Amt = "";
			String QC_emi = "";
			String CAC_Indicator = "";
			String consider_for_obligation="";
			String Duplicate_flag="";
			String avg_utilization="";

			if(!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals("")) ){
				AgreementId = OutputXML.get(i).get(1).toString();
			}				
			if(!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2).equals("")) ){
				ContractType = OutputXML.get(i).get(2).toString();
				try{
					String cardquery = "select code from ng_master_contract_type where description='"+ContractType+"'";
					//RLOS.mLogger.info("ExternalBureauIndividualProducts sQuery"+sQuery);
					List<List<String>> cardqueryXML = formObject.getDataFromDataSource(cardquery);
					ContractType=cardqueryXML.get(0).get(0);
					//RLOS.mLogger.info("ExternalBureauIndividualProducts ContractType"+ContractType);
				}
				catch(Exception e){
					RLOS.mLogger.info("ExternalBureauIndividualProducts ContractType Exception"+e);

					ContractType= OutputXML.get(i).get(2).toString();
				}
			}
			if(!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals("")) ){
				provider_no = OutputXML.get(i).get(3).toString();
			}
			if(!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals("")) ){
				phase = OutputXML.get(i).get(4).toString();
				if (phase.startsWith("A")){
					phase="A";
				}
				else {
					phase="C";
				}
			}				
			if(!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5).equals("")) ){
				CustRoleType = OutputXML.get(i).get(5).toString();
			}
			if(!(OutputXML.get(i).get(6) == null || OutputXML.get(i).get(6).equals("")) ){
				start_date = OutputXML.get(i).get(6).toString();
			}
			if(!(OutputXML.get(i).get(7) == null || OutputXML.get(i).get(7).equals("")) ){
				close_date = OutputXML.get(i).get(7).toString();
			}				
			if(!(OutputXML.get(i).get(8) == null || OutputXML.get(i).get(8).equals("")) ){
				OutStanding_Balance = OutputXML.get(i).get(8).toString();
			}
			if(!(OutputXML.get(i).get(9) == null || OutputXML.get(i).get(9).equals("")) ){
				TotalAmt = OutputXML.get(i).get(9).toString();
			}
			if(!(OutputXML.get(i).get(10) == null || OutputXML.get(i).get(10).equals("")) ){
				PaymentsAmt = OutputXML.get(i).get(10).toString();
			}
			if(!(OutputXML.get(i).get(11) == null || OutputXML.get(i).get(11).equals("")) ){
				TotalNoOfInstalments = OutputXML.get(i).get(11).toString();
			}
			if(!(OutputXML.get(i).get(12) == null || OutputXML.get(i).get(12).equals("")) ){
				RemainingInstalments = OutputXML.get(i).get(12).toString();
			}
			if(!(OutputXML.get(i).get(13) == null || OutputXML.get(i).get(13).equals("")) ){
				WorstStatus = OutputXML.get(i).get(13).toString();
			}
			if(!(OutputXML.get(i).get(14) == null || OutputXML.get(i).get(14).equals("")) ){
				WorstStatusDate = OutputXML.get(i).get(14).toString();
			}				
			if(!(OutputXML.get(i).get(15) == null || OutputXML.get(i).get(15).equals("")) ){
				CreditLimit = OutputXML.get(i).get(15).toString();
			}
			if(!(OutputXML.get(i).get(16) == null || OutputXML.get(i).get(16).equals("")) ){
				OverdueAmt = OutputXML.get(i).get(16).toString();
			}
			if(!(OutputXML.get(i).get(17) == null || OutputXML.get(i).get(17).equals("")) ){
				NofDaysPmtDelay = OutputXML.get(i).get(17).toString();
			}				
			if(!(OutputXML.get(i).get(18) == null || OutputXML.get(i).get(18).equals("")) ){
				MonthsOnBook = OutputXML.get(i).get(18).toString();
			}
			if(!(OutputXML.get(i).get(19) == null || OutputXML.get(i).get(19).equals("")) ){
				last_repayment_date = OutputXML.get(i).get(19).toString();
			}
			if(!(OutputXML.get(i).get(20) == null || OutputXML.get(i).get(20).equals("")) ){
				currently_current = OutputXML.get(i).get(20).toString();
			}
			if(!(OutputXML.get(i).get(21) == null || OutputXML.get(i).get(21).equals("")) ){
				current_utilization = OutputXML.get(i).get(21).toString();
			}
			if(!(OutputXML.get(i).get(22) == null || OutputXML.get(i).get(22).equals("")) ){
				DPD30Last6Months = OutputXML.get(i).get(22).toString();
			}
			if(!(OutputXML.get(i).get(23) == null || OutputXML.get(i).get(23).equals("")) ){
				DPD60Last12Months = OutputXML.get(i).get(23).toString();
			}
			if(!(OutputXML.get(i).get(24) == null || OutputXML.get(i).get(24).equals("")) ){
				AECBHistMonthCnt = OutputXML.get(i).get(24).toString();
			}				


			if(!(OutputXML.get(i).get(25) == null || OutputXML.get(i).get(25).equals("")) ){
				delinquent_in_last_3months = OutputXML.get(i).get(25).toString();
			}
			if(!(OutputXML.get(i).get(26) == null || OutputXML.get(i).get(26).equals("")) ){
				QC_Amt = OutputXML.get(i).get(26).toString();
			}
			if(!(OutputXML.get(i).get(27) == null || OutputXML.get(i).get(27).equals("")) ){
				QC_emi = OutputXML.get(i).get(27).toString();
			}
			if(!(OutputXML.get(i).get(28) == null || OutputXML.get(i).get(28).equals("")) ){
				CAC_Indicator = OutputXML.get(i).get(28).toString();
				//RLOS.mLogger.info("value of CAC_Indicator"+CAC_Indicator);

				if (CAC_Indicator != null && !(CAC_Indicator.equalsIgnoreCase(""))){
					if (CAC_Indicator.equalsIgnoreCase("true")){

						CAC_Indicator="Y";
						//RLOS.mLogger.info("value of CAC_Indicator"+CAC_Indicator);

					}
					else {
						CAC_Indicator="N";

						//RLOS.mLogger.info("value of CAC_Indicator"+CAC_Indicator);
					}
				}
			}
			String TakeOverIndicator="";
			if(!(OutputXML.get(i).get(29) == null || OutputXML.get(i).get(29).equals("")) ){
				TakeOverIndicator = OutputXML.get(i).get(29).toString();
				if (TakeOverIndicator != null && !(TakeOverIndicator.equalsIgnoreCase(""))){
					if (TakeOverIndicator.equalsIgnoreCase("true")){
						TakeOverIndicator="Y";
					}
					else {
						TakeOverIndicator="N";
					}
				}
			}
			if(!(OutputXML.get(i).get(30) == null || OutputXML.get(i).get(30).equals("")) ){
				consider_for_obligation = OutputXML.get(i).get(30).toString();
				//RLOS.mLogger.info("consider_for_obligationconsider_for_obligationconsider_for_obligation"+consider_for_obligation);
				if (consider_for_obligation != null && !(consider_for_obligation.equalsIgnoreCase(""))){
					if (consider_for_obligation.equalsIgnoreCase("true")){
						consider_for_obligation="Y";
						//	RLOS.mLogger.info("consider_for_obligationconsider_for_obligationconsider_for_obligation"+consider_for_obligation);

					}
					else {
						consider_for_obligation="N";
						//	RLOS.mLogger.info("consider_for_obligationconsider_for_obligationconsider_for_obligation"+consider_for_obligation);

					}
				}
			}
			if(!(OutputXML.get(i).get(31) == null || OutputXML.get(i).get(31).equals("")) ){
				Duplicate_flag = OutputXML.get(i).get(31).toString();
			}

			if(!(OutputXML.get(i).get(32) == null || OutputXML.get(i).get(32).equals("")) ){
				avg_utilization = OutputXML.get(i).get(32).toString();
			}

			String sQueryCustRoleType = "select code from ng_master_role_of_customer where Description='"+CustRoleType+"'";
			//RLOS.mLogger.info("CustRoleType"+sQueryCustRoleType);
			List<List<String>> sQueryCustRoleTypeXML = formObject.getNGDataFromDataCache(sQueryCustRoleType);
			try{
				if(sQueryCustRoleTypeXML!=null && sQueryCustRoleTypeXML.size()>0 && sQueryCustRoleTypeXML.get(0)!=null){
					CustRoleType=sQueryCustRoleTypeXML.get(0).get(0);
				}
			}
			catch(Exception e){
				RLOS.mLogger.info("Exception occured at sQueryCombinedLimit for"+sQueryCustRoleTypeXML);

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
			if (currently_current != null && currently_current.equalsIgnoreCase("1"))
				add_xml_str = add_xml_str + "<currently_current>Y</currently_current>";
			else
			{
				add_xml_str = add_xml_str + "<currently_current>N</currently_current>";
			}
			//add_xml_str = add_xml_str + "<current_utilization>"+current_utilization+"</current_utilization>";
			add_xml_str = add_xml_str + "<dpd_30_last_6_mon>"+DPD30Last6Months+"</dpd_30_last_6_mon>";

			add_xml_str = add_xml_str + "<dpd_60p_in_last_12_mon>"+DPD60Last12Months+"</dpd_60p_in_last_12_mon>";
			add_xml_str = add_xml_str + "<no_months_aecb_history>"+AECBHistMonthCnt+"</no_months_aecb_history>";
			add_xml_str = add_xml_str + "<delinquent_in_last_3months>"+delinquent_in_last_3months+"</delinquent_in_last_3months>";
			add_xml_str = add_xml_str + "<clean_funded>"+""+"</clean_funded>";
			add_xml_str = add_xml_str + "<cac_indicator>"+CAC_Indicator+"</cac_indicator>";
			add_xml_str = add_xml_str + "<qc_emi>"+QC_emi+"</qc_emi>";
			if (ReqProd.equalsIgnoreCase(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_CreditCard"))){
				add_xml_str = add_xml_str + "<qc_amount>"+QC_Amt+"</qc_amount><company_flag>N</company_flag><cac_bank_name>"+formObject.getNGValue("cmplx_Liability_New_CACBankName")+"</cac_bank_name><consider_for_obligation>"+consider_for_obligation+"</consider_for_obligation><duplicate_flag>"+Duplicate_flag+"</duplicate_flag><avg_utilization>"+avg_utilization+"</avg_utilization></ExternalBureauIndividualProducts>";
			}
			else{
				add_xml_str = add_xml_str + "<qc_amount>"+QC_Amt+"</qc_amount><company_flag>N</company_flag><cac_bank_name>"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_takeoverBank")+"</cac_bank_name><take_over_indicator>"+TakeOverIndicator+"</take_over_indicator><consider_for_obligation>"+consider_for_obligation+"</consider_for_obligation><duplicate_flag>"+Duplicate_flag+"</duplicate_flag></ExternalBureauIndividualProducts>";
			}

		}
		//RLOS.mLogger.info("RLOSCommon Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}
	public String ExternalBureauManualAddIndividualProducts(){
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();

		int Man_liab_row_count = formObject.getLVWRowCount("cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
		String applicant_id = formObject.getNGValue("cmplx_Customer_CIFNO");
		String  add_xml_str ="";
		/*Date date = new Date();
		String modifiedDate= new SimpleDateFormat("yyyy-MM-dd").format(date);
		DateTime dt = new DateTime();
		DateTimeFormatter fp = DateTimeFormat.forPattern("yyyy-MM-dd");
		DateTime cd = dt.parse(modifiedDate,fp).plusYears(4); 
		String close_date = cd.getYear()+"-"+(cd.getMonthOfYear()<10?"0"+cd.getMonthOfYear():cd.getMonthOfYear())+"-"+(cd.getDayOfMonth()<10?"0"+cd.getDayOfMonth():cd.getDayOfMonth());
		 */

		Date currentDate=new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String modifiedDate= sdf.format(currentDate);
		String close_date= plusyear(modifiedDate,4,0,0,"yyyy-MM-dd");

		if (Man_liab_row_count !=0){
			for (int i = 0; i<Man_liab_row_count;i++){
				String Type_of_Contract = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 0); //0 //Changes Done by aman to send the value in Y and N format
				String Limit = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 1); //0
				String EMI = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 2); //0
				String Take_over_Indicator = (formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 3).equalsIgnoreCase("true")?"Y":"N"); //0
				String cac_Indicator = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 5); //0
				String Qc_amt = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 6); //0
				String Qc_Emi = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 7); //0
				String worst_status = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 17); //0
				String Application_type="L";
				if ("true".equalsIgnoreCase(cac_Indicator)){
					cac_Indicator="Y";
				}
				else {
					cac_Indicator="N";
				}
				String consider_for_obligation = (formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 8).equalsIgnoreCase("true")?"Y":"N"); //0
				//String MOB = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 9); //0
				String Utilization = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 11); //0
				String OutStanding = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 12); //0
				String mob = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 10);
				String avg_utilization = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 20);
				String delinquent_in_last_3months = (NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 13))?"1":"0");
				String dpd_30_last_6_mon = (NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 14))?"1":"0");
				String dpd_60p_in_last_12_mon = (NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 15))?"1":"0");
				String query="select App_Type from NG_MASTER_contract_type where Code ='"+Type_of_Contract+"'";
				try
				{
					List<List<String>> App_type=formObject.getDataFromDataSource(query);
					if(App_type!=null && !App_type.isEmpty())
					{
						Application_type=App_type.get(0).get(0);
					}
				}
				catch(Exception Ex)
				{
					RLOS.mLogger.info(Ex.getMessage());
				}
				add_xml_str = add_xml_str + "<ExternalBureauIndividualProducts><applicant_id>"+applicant_id+"</applicant_id>";
				add_xml_str = add_xml_str + "<external_bureau_individual_products_id></external_bureau_individual_products_id>";
				add_xml_str = add_xml_str + "<contract_type>"+Type_of_Contract+"</contract_type>";
				add_xml_str = add_xml_str + "<provider_no></provider_no>";
				add_xml_str = add_xml_str + "<phase>A</phase>";
				add_xml_str = add_xml_str + "<role_of_customer>A</role_of_customer>";
				add_xml_str = add_xml_str + "<start_date>"+modifiedDate+"</start_date>"; 

				add_xml_str = add_xml_str + "<close_date>"+close_date+"</close_date>";
				add_xml_str = add_xml_str + "<outstanding_balance>"+OutStanding+"</outstanding_balance>";
				if (Application_type.equalsIgnoreCase("L")){
					add_xml_str = add_xml_str + "<total_amount>"+Limit+"</total_amount>";
				}
				add_xml_str = add_xml_str + "<payments_amount>"+EMI+"</payments_amount>";
				add_xml_str = add_xml_str + "<total_no_of_instalments></total_no_of_instalments>";
				add_xml_str = add_xml_str + "<no_of_remaining_instalments></no_of_remaining_instalments>";
				add_xml_str = add_xml_str + "<worst_status>"+worst_status+"</worst_status>";
				add_xml_str = add_xml_str + "<worst_status_date></worst_status_date>";
				if (Application_type.equalsIgnoreCase("C") ){
					add_xml_str = add_xml_str + "<credit_limit>"+Limit+"</credit_limit>";
				}	
				add_xml_str = add_xml_str + "<overdue_amount></overdue_amount>";
				add_xml_str = add_xml_str + "<no_of_days_payment_delay></no_of_days_payment_delay>";
				add_xml_str = add_xml_str + "<mob>"+mob+"</mob>";
				add_xml_str = add_xml_str + "<last_repayment_date></last_repayment_date>";

				add_xml_str = add_xml_str + "<currently_current>N</currently_current>";

				add_xml_str = add_xml_str + "<current_utilization>"+Utilization+"</current_utilization>";
				add_xml_str = add_xml_str + "<dpd_30_last_6_mon>"+dpd_30_last_6_mon+"</dpd_30_last_6_mon>";

				add_xml_str = add_xml_str + "<dpd_60p_in_last_12_mon>"+dpd_60p_in_last_12_mon+"</dpd_60p_in_last_12_mon>";
				add_xml_str = add_xml_str + "<no_months_aecb_history></no_months_aecb_history>";
				add_xml_str = add_xml_str + "<delinquent_in_last_3months>"+delinquent_in_last_3months+"</delinquent_in_last_3months>";
				add_xml_str = add_xml_str + "<clean_funded>"+""+"</clean_funded>";
				add_xml_str = add_xml_str + "<cac_indicator>"+cac_Indicator+"</cac_indicator>";
				add_xml_str = add_xml_str + "<qc_emi>"+Qc_Emi+"</qc_emi>";
				add_xml_str = add_xml_str + "<qc_amount>"+Qc_amt+"</qc_amount><company_flag>N</company_flag><cac_bank_name>"+formObject.getNGValue("cmplx_Liability_New_CACBankName")+"</cac_bank_name><take_over_indicator>"+Take_over_Indicator+"</take_over_indicator><consider_for_obligation>"+consider_for_obligation+"</consider_for_obligation><duplicate_flag>N</duplicate_flag><avg_utilization>"+avg_utilization+"</avg_utilization></ExternalBureauIndividualProducts>";

			}

		}
		//RLOS.mLogger.info( "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}


	public String ExternalBureauPipelineProducts(){
		//RLOS.mLogger.info( "inside ExternalBureauPipelineProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "select AgreementId,ProviderNo,LoanType,LoanDesc,CustRoleType,Datelastupdated,TotalAmt,TotalNoOfInstalments,'' as col1,NoOfDaysInPipeline,isnull(Consider_For_Obligations,'true'),case when IsDuplicate= '1' then 'Y' else 'N' end from ng_rlos_cust_extexpo_LoanDetails with (nolock) where wi_name  =  '"+formObject.getWFWorkitemName()+"' and LoanStat = 'Pipeline' union select CardEmbossNum,ProviderNo,CardType,CardTypeDesc,CustRoleType,LastUpdateDate,'' as col2,NoOfInstallments,TotalAmount,NoOfDaysInPipeLine,isnull(Consider_For_Obligations,'true'),case when IsDuplicate= '1' then 'Y' else 'N' end  from ng_rlos_cust_extexpo_CardDetails with (nolock) where wi_name  =  '"+formObject.getWFWorkitemName()+"' and cardstatus = 'Pipeline'";

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
			String consider_for_obligation="";
			String ppl_Duplicate_flag="";
			if(!(OutputXML.get(i).get(0) == null || "".equalsIgnoreCase(OutputXML.get(i).get(0))) ){
				agreementID = OutputXML.get(i).get(0);
			}
			if(!(OutputXML.get(i).get(1) == null || "".equalsIgnoreCase(OutputXML.get(i).get(1))) ){
				ProviderNo = OutputXML.get(i).get(1);
			}
			if(OutputXML.get(i).get(2)!=null && !OutputXML.get(i).get(2).isEmpty() &&  !"".equalsIgnoreCase(OutputXML.get(i).get(2)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(2)) ){
				contractType = OutputXML.get(i).get(2);
				//below code added by nikhil for PCSP-822
				try {
					String cardquery = "select code from ng_master_contract_type with (nolock) where description='"+ contractType + "'";
					//CreditCard.mLogger.info("ExternalBureauIndividualProducts sQuery"+ cardquery+ "");
					List<List<String>> cardqueryXML = formObject.getNGDataFromDataCache(cardquery);
					contractType = cardqueryXML.get(0).get(0);
					//CreditCard.mLogger.info("ExternalBureauIndividualProducts ContractType"+ ContractType+ "ContractType");
				} catch (Exception e) {
					RLOS.mLogger.info("ExternalBureauIndividualProducts ContractType Exception"+ e+ "Exception");

					contractType = OutputXML.get(i).get(2).toString();
				}
			}
			if(!(OutputXML.get(i).get(3) == null || "".equalsIgnoreCase(OutputXML.get(i).get(3))) ){
				productType = OutputXML.get(i).get(3);
			}
			//below code added by nikhil for PCSP-822
			/*if (contractType.contains("CreditCard")){	

				contractType="CC";
			}
			else if (contractType.equalsIgnoreCase("MURABAHA")){
				if (productType.contains("AUTO")){
					contractType="IAL";
				}
				else{
					contractType="IPL";
				}
			}
			else if (contractType.equalsIgnoreCase("AUTO")){
				contractType="AL";
			}
			else if (contractType.equalsIgnoreCase("HOME")){
				contractType="ML";
			}
			else if (contractType.equalsIgnoreCase("IJARAH")){
				contractType="IML";
			}
			else if (contractType.equalsIgnoreCase("PERSONAL")){
				contractType="PL";
			}
			else if (contractType.equalsIgnoreCase("HOME IN ONE")){
				contractType="HIO";
			}
			else if ("OVERDRAFT".equalsIgnoreCase(contractType))
			{
				contractType= "OD";
			}
			else{
				contractType= "PL";
			}*/

			if(!(OutputXML.get(i).get(4) == null || "".equalsIgnoreCase(OutputXML.get(i).get(4))) ){
				role = OutputXML.get(i).get(4);
				//added by nikhil for PCSP-822
				String sQueryCustRoleType = "select code from ng_master_role_of_customer with(nolock) where Description='"+role+"'";
				RLOS.mLogger.info("CustRoleType"+sQueryCustRoleType);
				List<List<String>> sQueryCustRoleTypeXML = formObject.getNGDataFromDataCache(sQueryCustRoleType);
				try{
					if(sQueryCustRoleTypeXML!=null && sQueryCustRoleTypeXML.size()>0 && sQueryCustRoleTypeXML.get(0)!=null){
						role=sQueryCustRoleTypeXML.get(0).get(0);
					}
				}
				catch(Exception e){
					RLOS.mLogger.info("Exception occured at sQueryCombinedLimit for"+sQueryCustRoleTypeXML);
					role = OutputXML.get(i).get(4).toString();
				}
			}
			if(OutputXML.get(i).get(5)!=null && !OutputXML.get(i).get(5).isEmpty() &&  !"".equalsIgnoreCase(OutputXML.get(i).get(5)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(5)) ){
				lastUpdateDate = OutputXML.get(i).get(5);
			}
			if(OutputXML.get(i).get(6)!=null && !OutputXML.get(i).get(6).isEmpty() &&  !"".equalsIgnoreCase(OutputXML.get(i).get(6)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(6)) ){
				TotAmt = OutputXML.get(i).get(6);
			}
			if(!(OutputXML.get(i).get(7) == null || "".equalsIgnoreCase(OutputXML.get(i).get(7))) ){
				noOfInstalmnt = OutputXML.get(i).get(7);
			}
			if(!(OutputXML.get(i).get(8) == null || "".equalsIgnoreCase(OutputXML.get(i).get(8))) ){
				creditLimit = OutputXML.get(i).get(8);
			}
			if(OutputXML.get(i).get(9)!=null && !OutputXML.get(i).get(9).isEmpty() &&  !"".equalsIgnoreCase(OutputXML.get(i).get(9)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(9)) ){
				noOfDayinPpl = OutputXML.get(i).get(9);
			}
			if(!(OutputXML.get(i).get(10) == null || "".equalsIgnoreCase(OutputXML.get(i).get(10))) ){
				consider_for_obligation = OutputXML.get(i).get(10).toString();
				if (consider_for_obligation != null && !("".equalsIgnoreCase(consider_for_obligation))){
					if ("true".equalsIgnoreCase(consider_for_obligation)){
						consider_for_obligation="Y";
					}
					else {
						consider_for_obligation="N";
					}
				}
			}
			if(OutputXML.get(i).get(11)!=null && !OutputXML.get(i).get(11).isEmpty() &&  !"".equalsIgnoreCase(OutputXML.get(i).get(11)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(11)) ){
				ppl_Duplicate_flag = OutputXML.get(i).get(11);
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
			//Deepak consider for obligation corrected on 20Oct
			add_xml_str = add_xml_str + "<ppl_no_of_days_in_pipeline>"+noOfDayinPpl+"</ppl_no_of_days_in_pipeline><company_flag>N</company_flag><ppl_consider_for_obligation>"+consider_for_obligation+"</ppl_consider_for_obligation><ppl_duplicate_flag>"+ppl_Duplicate_flag+"</ppl_duplicate_flag></ExternalBureauPipelineProducts>"; // to be populated later




		}
		//RLOS.mLogger.info( "Internal liab tag Cration: "+ add_xml_str);
		return add_xml_str;
	}

	public String getProduct_details(){
		//RLOS.mLogger.info( "inside getProduct_details : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		int prod_row_count = formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
		//RLOS.mLogger.info( "inside getCustAddress_details add_row_count+ : "+prod_row_count);
		/*String FinalLimitQuery="select isnull(Final_limit,0) from ng_rlos_IGR_Eligibility_CardProduct where wi_name='"+formObject.getWFWorkitemName()+"'";
		List<List<String>> FinalLimitXML = formObject.getNGDataFromDataCache(FinalLimitQuery);
		//changed by aman shukla on 18th Sept.
		String FinalLimitPLQuery="select isnull(FinalLoanAmount,0) from ng_rlos_IGR_Eligibility_PersonalLoan where wi_name='"+formObject.getWFWorkitemName()+"'";
		List<List<String>> FinalLimitPLXML = formObject.getNGDataFromDataCache(FinalLimitPLQuery);



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
		 */
		//subProd
		String  prod_xml_str ="";

		for (int i = 0; i<prod_row_count;i++){
			String prod_type = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 0); //0
			String reqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 1);//1
			String subProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 2);//2
			String reqLimit=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 3).replaceAll(",", "");//3
			reqLimit=reqLimit.replaceAll(",", "");
			String appType=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 4);//4

			String cardProd = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 5);//5
			String scheme=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 8);//6
			String tenure=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 7);//7
			String limitExpiry=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", i, 18);//8
			String ApplicationDate=formObject.getNGValue("Introduction_date");
			String AppCateg=formObject.getNGValue("cmplx_EmploymentDetails_ApplicationCateg");
			String interestRate = formObject.getNGValue("cmplx_EligibilityAndProductInfo_InterestRate");
			if(interestRate == null){
				interestRate = "";
			}
			if ("Self Employed".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 6))){
				//RLOS.mLogger.info( "inside cmplx_Product_cmplx_ProductGrid add_row_count+ : "+prod_row_count);
				int Comp_row_count = formObject.getLVWRowCount("cmplx_CompanyDetails_cmplx_CompanyGrid");
				//RLOS.mLogger.info( "inside cmplx_Product_cmplx_ProductGrid Comp_row_count+ : "+Comp_row_count);

				for (int j = 0; j<Comp_row_count;j++){
					if (formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", j, 2).equalsIgnoreCase("Secondary")){
						AppCateg = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", j, 24); //0
						//		RLOS.mLogger.info( "inside cmplx_Product_cmplx_ProductGrid Comp_row_count+ : "+AppCateg);

					}
				}

			}
			//RLOS.mLogger.info( "inside getCustAddress_details ApplicationDate+ : "+ApplicationDate);
			ApplicationDate=ApplicationDate.substring(0, 10)+"T"+ApplicationDate.substring(11, 19);
			//limitExpiry=Convert_dateFormat(limitExpiry, "dd/MM/yyyy", "yyyy-MM-dd");
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
			prod_xml_str = prod_xml_str + "<requested_product>"+(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_PersonalLoan").equalsIgnoreCase(reqProd)?"PL":"CC")+"</requested_product>";
			prod_xml_str = prod_xml_str + "<requested_limit>"+reqLimit+"</requested_limit>";
			prod_xml_str = prod_xml_str + "<sub_product>"+subProd+"</sub_product>";
			prod_xml_str = prod_xml_str + "<requested_card_product>"+cardProd+"</requested_card_product>";
			prod_xml_str = prod_xml_str + "<application_type>"+appType+"</application_type>";
			prod_xml_str = prod_xml_str + "<scheme>"+scheme+"</scheme>";
			prod_xml_str = prod_xml_str + "<tenure>"+(tenure.equals("NA")?"":tenure)+"</tenure>";
			prod_xml_str = prod_xml_str + "<interest_rate>"+interestRate+"</interest_rate>";
			prod_xml_str = prod_xml_str + "<customer_type>"+(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_true").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_NTB"))?"NTB":"Existing")+"</customer_type>";
			prod_xml_str = prod_xml_str + "<limit_expiry_date>"+limitExpiry+"</limit_expiry_date><final_limit>"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalLimit")+"</final_limit><cc_bundle_limit>"+formObject.getNGValue("cmplx_EligibilityAndProductInfo_EFCHidden")+"</cc_bundle_limit><emi>"+EMI+"</emi><manual_deviation>N</manual_deviation><application_date>"+ApplicationDate+"</application_date></ApplicationDetails>";


		}
		//RLOS.mLogger.info( "Address tag Cration: "+ prod_xml_str);
		return prod_xml_str;
	}
	/*  Function Header:

	 **********************************************************************************

		 NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified   : 6/08/2017  
		Author  : Deepak  
		Description : Function for Dedupe Summary call

	 ***********************************************************************************  */

	private Map<String, String> DEDUP_SUMMARY_Custom(List<List<String>> DB_List,FormReference formObject,String callName) {


		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();

		//FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try{
			for (List<String> mylist : DB_List) {
				// for(int i=0;i<col_n.length();i++)
				for (int i = 0; i < 8; i++) {
					// SKLogger_CC.writeLog("rec: "+records.item(rec));
					//RLOS.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");

				if ("AddressDetails".equalsIgnoreCase(tag_name) && int_xml.containsKey(parent_tag)) {
					String xml_str = int_xml.get(parent_tag);
					//RLOS.mLogger.info("RLOS COMMON"+ " before adding address+ " + xml_str);
					xml_str = xml_str + getCustAddress_details();
					//RLOS.mLogger.info("RLOS COMMON"+ " after adding address+ " + xml_str);
					int_xml.put(parent_tag, xml_str);
				} 
				else if("MaritalStatus".equalsIgnoreCase(tag_name)){
					String marrital_code = formObject.getNGValue("cmplx_Customer_MAritalStatus").substring(0, 1);
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<"+tag_name+">"+marrital_code
					+"</"+ tag_name+">";

					//RLOS.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
				}
			}
		}
		catch(Exception e){
			RLOS.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + ");
			printException(e);
		}

		return int_xml;
	}
	/*  Function Header:

	 **********************************************************************************

		 NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified   : 6/08/2017  
		Author  : Disha  
		Description : Function to Generate Input XML for DB INPUT

	 ***********************************************************************************  */
	private Map<String, String> GenDefault_Input_DB(Map<String, String> int_xml, Map<String, String> recordFileMap,FormReference formObject,String callName){

		String Call_name =  recordFileMap.get("Call_name");
		String form_control =  recordFileMap.get("form_control");
		String parent_tag =  recordFileMap.get("parent_tag_name");
		String tag_name =  recordFileMap.get("xmltag_name");
		String is_repetitive =  recordFileMap.get("is_repetitive");
		String default_val =  recordFileMap.get("default_val");
		String data_format12 =  recordFileMap.get("data_format");

		String form_control_val = "";
		java.util.Date startDate;

		if (parent_tag != null && !"".equalsIgnoreCase(parent_tag)) {

			//RLOS.mLogger.info("inside 1st if"+"inside 1st if");
			if(int_xml.containsKey(parent_tag))
			{
				RLOS.mLogger.info("inside 1st if"+"inside 2nd if");
				String xml_str = int_xml.get(parent_tag);
				RLOS.mLogger.info("inside 1st if"+"inside 2nd if xml string"+xml_str);
				if("Y".equalsIgnoreCase(is_repetitive) && int_xml.containsKey(tag_name)){
						RLOS.mLogger.info("inside 1st if"+"inside 3rd if xml string");
					xml_str = int_xml.get(tag_name)+ "</"+tag_name+">" +"<"+ tag_name+">";
						RLOS.mLogger.info("inside 1st if"+"inside 3rd if xml string xml string"+xml_str);
					int_xml.remove(tag_name);
					int_xml.put(tag_name, xml_str);
					RLOS.mLogger.info("inside 1st if"+"inside 3rd if xml string xml string int_xml");
				}
				else{
					RLOS.mLogger.info("inside else of parent tag"+"value after adding "+ Call_name+": "+xml_str+" form_control name: "+form_control);
					RLOS.mLogger.info(""+"valuie of form control: "+formObject.getNGValue(form_control));
					if("".equalsIgnoreCase(form_control.trim()) && "".equalsIgnoreCase(default_val.trim())){
						RLOS.mLogger.info("inside if added by me"+"inside");
						xml_str = xml_str + "<"+tag_name+">"+"</"+ tag_name+">";
							RLOS.mLogger.info("added by xml"+"xml_str"+xml_str);
					}
					else if (!(formObject.getNGValue(form_control)==null || "".equalsIgnoreCase(formObject.getNGValue(form_control).trim())||  "null".equalsIgnoreCase(formObject.getNGValue(form_control))))
					{
							RLOS.mLogger.info("inside else of parent tag 1"+"form_control_val"+ form_control_val);
						if(fin_call_name.toUpperCase().contains(callName.toUpperCase()) && !capitalExceptionsTags.toUpperCase().contains(tag_name.toUpperCase())){
							form_control_val = formObject.getNGValue(form_control).toUpperCase();
						}
						else
							form_control_val = formObject.getNGValue(form_control);

						if(!"text".equalsIgnoreCase(data_format12)){
							String[] format_arr = data_format12.split(":");
							String format_name = format_arr[0];
							String format_type = format_arr[1];
								RLOS.mLogger.info(""+"format_name"+format_name);
								RLOS.mLogger.info(""+"format_type"+format_type);

							if("date".equalsIgnoreCase(format_name)){
								DateFormat df = new SimpleDateFormat("dd/MM/yyyy"); 
								DateFormat df_new = new SimpleDateFormat(format_type);

								try {
									startDate = df.parse(form_control_val);
									form_control_val = df_new.format(startDate);
											RLOS.mLogger.info("RLOSCommon#Create Input"+" date conversion: final Output: "+form_control_val+ " requested format: "+format_type);

								} catch (ParseException e) {
									RLOS.mLogger.info("RLOSCommon#Create Input"+" Error while format conversion: "+e.getMessage());
									printException(e);
								}
								catch (Exception e) {
									RLOS.mLogger.info("RLOSCommon#Create Input"+" Error while format conversion: "+e.getMessage());
									printException(e);
								}
							}
							else if("number".equalsIgnoreCase(format_name)){
								if(form_control_val.contains(",")){
									form_control_val = form_control_val.replace(",", "");
								}

							}
							//change here for other input format

						}
						//RLOS.mLogger.info("inside else of parent tag"+"form_control_val"+ form_control_val);
						xml_str = xml_str + "<"+tag_name+">"+form_control_val
						+"</"+ tag_name+">";
						//RLOS.mLogger.info("inside else of parent tag xml_str"+"xml_str"+ xml_str);
					}

					else if(default_val==null || "".equalsIgnoreCase(default_val.trim())){
						//RLOS.mLogger.info("#RLOS Common GenerateXML IF part"+"no value found for tag name: "+ tag_name);
					}
					else{
						//	RLOS.mLogger.info("#RLOS Common GenerateXML inside set default value"+"");

						form_control_val = default_val;

						//	RLOS.mLogger.info("#RLOS Common GenerateXML inside set default value"+"form_control_val"+ form_control_val);
						xml_str = xml_str + "<"+tag_name+">"+form_control_val
						+"</"+ tag_name+">";
						//	RLOS.mLogger.info("#RLOS Common GenerateXML inside else of parent tag form_control_val xml_str1"+"xml_str"+ xml_str);

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
					else if("DocNo".equalsIgnoreCase(tag_name) && "DocDet".equalsIgnoreCase(parent_tag) && "".equalsIgnoreCase(form_control_val)){
						if(xml_str.contains("</DocDet>")){
							xml_str = xml_str.substring(0, xml_str.lastIndexOf("</DocDet>"));
							int_xml.put(parent_tag, xml_str);
						}
						else
							int_xml.remove(parent_tag);
					}

					else if("CUSTOMER_SEARCH_REQUEST".equalsIgnoreCase(Call_name)&&("PhoneValue".equalsIgnoreCase(tag_name) && "PhoneFax".equalsIgnoreCase(parent_tag) && "".equalsIgnoreCase(form_control_val))){
						if(xml_str.contains("</PhoneFax>")){
							xml_str = xml_str.substring(0, xml_str.lastIndexOf("</PhoneFax>"));
							int_xml.put(parent_tag, xml_str);
						}
						else
							int_xml.remove(parent_tag);
					}
					else if("PhnLocalCode".equalsIgnoreCase(tag_name) && "PhnDetails".equalsIgnoreCase(parent_tag) && "".equalsIgnoreCase(form_control_val)){
						if(xml_str.contains("</PhnDetails>")){
							//	RLOS.mLogger.info("PL_common"+ "Inside PhnDetails condition to remove RVC tag </PhnDetails> contanied");
							xml_str = xml_str.substring(0, xml_str.lastIndexOf("</PhnDetails>"));
							int_xml.put(parent_tag, xml_str);
						}
						else{
							//RLOS.mLogger.info("PL_common"+ "Inside PhnDetails condition to remove PhnDetails tag <PhnDetails> tag not contanied");
							int_xml.remove(parent_tag);
							Iterator<Map.Entry<String,String>> itr = int_xml.entrySet().iterator();
							//	RLOS.mLogger.info("itr of hashmap"+"itr"+itr);
							while (itr.hasNext())
							{
								Map.Entry<String, String> entry =  itr.next();
								//RLOS.mLogger.info("entry of hashmap"+"entry"+entry+ " entry.getValue(): "+ entry.getValue());
								if(entry.getValue().contains("PhnDetails")){
									String key_val = entry.getValue();
									key_val = key_val.replace("<PhnDetails>", "");
									key_val = key_val.replace("</PhnDetails>", "");
									int_xml.put(entry.getKey(), key_val);
									//	RLOS.mLogger.info("PL_common"+"KEY: entry.getKey()" + " Updated value: "+key_val);
									//final_xml = final_xml.insert(final_xml.indexOf("<"+entry.getKey()+">")+entry.getKey().length()+2, entry.getValue());
									//		RLOS.mLogger.info("PL_common "+"PhnDetails removed from parent key");
									break;
								}

							} 
						}
					}
					//for email for CIF update

					else if("Email".equalsIgnoreCase(tag_name) && "EmlDet".equalsIgnoreCase(parent_tag) && "".equalsIgnoreCase(form_control_val)){
						if(xml_str.contains("</EmlDet>")){
							//	RLOS.mLogger.info("PL_common"+ "Inside EmlDet condition to remove RVC tag </EmlDet> contanied");
							xml_str = xml_str.substring(0, xml_str.lastIndexOf("</EmlDet>"));
							int_xml.put(parent_tag, xml_str);
						}
						else{
							//RLOS.mLogger.info("PL_common"+ "Inside EmlDet condition to remove PhnDetails tag <EmlDet> tag not contanied");
							int_xml.remove(parent_tag);
							Iterator<Map.Entry<String,String>> itr = int_xml.entrySet().iterator();
							//RLOS.mLogger.info("itr of hashmap"+"itr"+itr);
							while (itr.hasNext())
							{
								Map.Entry<String, String> entry =  itr.next();
								//	RLOS.mLogger.info("entry of hashmap"+"entry"+entry+ " entry.getValue(): "+ entry.getValue());
								if(entry.getValue().contains("EmlDet")){
									String key_val = entry.getValue();
									key_val = key_val.replace("<EmlDet>", "");
									key_val = key_val.replace("</EmlDet>", "");
									int_xml.put(entry.getKey(), key_val);
									//	RLOS.mLogger.info("PL_common"+"KEY: entry.getKey()" + " Updated value: "+key_val);
									//final_xml = final_xml.insert(final_xml.indexOf("<"+entry.getKey()+">")+entry.getKey().length()+2, entry.getValue());
									//	RLOS.mLogger.info("PL_common "+"EmlDet removed from parent key");
									break;
								}

							} 
						}
					}


					else if("IncomeAmount".equalsIgnoreCase(tag_name) && "OtherIncomeDetails".equalsIgnoreCase(parent_tag) && "".equalsIgnoreCase(form_control_val)){
						if(xml_str.contains("</OtherIncomeDetails>")){
							xml_str = xml_str.substring(0, xml_str.lastIndexOf("</OtherIncomeDetails>"));
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
					//RLOS.mLogger.info("else of generatexml"+"RLOSCommon"+"inside else"+xml_str);

				}

			}
			else{
				String new_xml_str ="";
				//RLOS.mLogger.info("inside else of parent tag main 2"+"value after adding "+ Call_name+": "+new_xml_str+" form_control name: "+form_control);
				//RLOS.mLogger.info(""+"valuie of form control: "+formObject.getNGValue(form_control));
				if (!(formObject.getNGValue(form_control)==null || "".equalsIgnoreCase(formObject.getNGValue(form_control).trim())||  "null".equalsIgnoreCase(formObject.getNGValue(form_control)))){
					//	RLOS.mLogger.info("inside else of parent tag 1"+"form_control_val"+ form_control_val);
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
								if(!form_control_val.equals("")){
									startDate = df.parse(form_control_val);
									form_control_val = df_new.format(startDate);
									//RLOS.mLogger.info("RLOSCommon#Create Input"+" date conversion: final Output: "+form_control_val+ " requested format: "+format_type);
								}
							} catch (ParseException e) {
								//RLOS.mLogger.info("RLOSCommon#Create Input"+" Error while format conversion: "+e.getMessage());
								printException(e);
							}
							catch (Exception e) {
								//RLOS.mLogger.info("RLOSCommon#Create Input"+" Error while format conversion: "+e.getMessage());
								printException(e);
							}
						}
						else if("number".equalsIgnoreCase(format_name)){
							if(form_control_val.contains(",")){
								form_control_val = form_control_val.replace(",", "");
							}

						}
						//change here for other input format

					}
					//RLOS.mLogger.info("inside else of parent tag"+"form_control_val"+ form_control_val);
					new_xml_str = new_xml_str + "<"+tag_name+">"+form_control_val
					+"</"+ tag_name+">";
					//RLOS.mLogger.info("inside else of parent tag xml_str"+"new_xml_str"+ new_xml_str);
				}

				else if(default_val==null || "".equalsIgnoreCase(default_val.trim())){
					if(int_xml.containsKey(parent_tag)|| "Y".equalsIgnoreCase(is_repetitive)){
						new_xml_str = new_xml_str + "<"+tag_name+">"+"</"+ tag_name+">";
					}
					//RLOS.mLogger.info("#RLOS Common GenerateXML Inside Else Part"+"no value found for tag name: "+ tag_name);
				}
				else{
					//	RLOS.mLogger.info("#RLOS Common GenerateXML inside set default value"+"");
					form_control_val = default_val;
					//	RLOS.mLogger.info("#RLOS Common GenerateXML inside set default value"+"form_control_val"+ form_control_val);
					new_xml_str = new_xml_str + "<"+tag_name+">"+form_control_val
					+"</"+ tag_name+">";
					//RLOS.mLogger.info("#RLOS Common GenerateXML inside else of parent tag form_control_val xml_str1"+"xml_str"+ new_xml_str);

				}
				int_xml.put(parent_tag, new_xml_str);
				//RLOS.mLogger.info("else of generatexml"+"RLOSCommon"+"inside else"+new_xml_str);

			}

		}
		//RLOS.mLogger.info("else of generatexml"+"RLOSCommon"+"inside else"+int_xml);
		return int_xml;
	}
	/*  Function Header:

	 **********************************************************************************

		 NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified   : 6/08/2017  
		Author  : Deepak  
		Description : Function for New Customer Call  

	 ***********************************************************************************  */

	private Map<String, String> NEW_CUSTOMER_Custom(List<List<String>> DB_List,FormReference formObject, String Call_name) {

		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();
		try{
			for (List<String> mylist : DB_List) {
				// for(int i=0;i<col_n.length();i++)
				for (int i = 0; i < 8; i++) {
					// SKLogger_CC.writeLog("rec: "+records.item(rec));
					//RLOS.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");
				
				
				if("PRIMARY".equalsIgnoreCase(formObject.getNGValue("cmplx_DecisionHistory_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_DecisionHistory_MultipleApplicantsGrid"),0))){	

					if ("AddressDetails".equalsIgnoreCase(tag_name)	&& int_xml.containsKey(parent_tag)) {
						String xml_str = int_xml.get(parent_tag);
						//RLOS.mLogger.info("RLOS COMMON"+ " before adding address+ " + xml_str);
						xml_str = xml_str + getCustAddress_details();
						//RLOS.mLogger.info("RLOS COMMON"+ " after adding address+ " + xml_str);
						int_xml.put(parent_tag, xml_str);
					}
					//change by saurabh for defualt value points on 8th Feb 19.
					else if("PrimaryServiceCenter".equalsIgnoreCase(tag_name) || "PrimaryBranchId".equalsIgnoreCase(tag_name)){
						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<" + tag_name + ">" + formObject.getNGValue("SOLID") + "</" + tag_name + ">";
						int_xml.put(parent_tag, xml_str);
					}

					else if ("MinorFlag".equalsIgnoreCase(tag_name) && "PersonDetails".equalsIgnoreCase(parent_tag)) {
						if (int_xml.containsKey(parent_tag)) {
							float Age = Float.parseFloat(formObject.getNGValue("cmplx_Customer_age"));
							String age_flag = "N";
							if (Age < 21)//changed from 18 to 21 by akshay on 17/4/18
								age_flag = "Y";
							String xml_str = int_xml.get(parent_tag);
							xml_str = xml_str + "<" + tag_name + ">" + age_flag + "</" + tag_name + ">";

							//	RLOS.mLogger.info("RLOS COMMON"+" after adding Minor flag+ " + xml_str);
							int_xml.put(parent_tag, xml_str);
						}
					} else if ("NonResidentFlag".equalsIgnoreCase(tag_name) && "PersonDetails".equalsIgnoreCase(parent_tag)) {
						if (int_xml.containsKey(parent_tag)) {
							String xml_str = int_xml.get(parent_tag);
							String res_flag = "N";

							if (NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Resident").equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_ResidentNonResident"))) {
								res_flag = "Y";
							}

							xml_str = xml_str + "<" + tag_name + ">" + res_flag + "</" + tag_name + ">";

							//	RLOS.mLogger.info("RLOS COMMON"+ " after adding res_flag+ " + xml_str);
							int_xml.put(parent_tag, xml_str);
						}
					}
					else if("MinorFlag".equalsIgnoreCase(tag_name)){
						if(int_xml.containsKey(parent_tag))
						{
							int Age = Integer.parseInt(formObject.getNGValue("cmplx_Customer_age"));
							String age_flag = "N";
							if(Age<21)
								age_flag="Y";
							String xml_str = int_xml.get(parent_tag);
							xml_str = xml_str + "<"+tag_name+">"+age_flag
							+"</"+ tag_name+">";

							//	RLOS.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}			
					}

					else if("RelationshipDetails".equalsIgnoreCase(tag_name)){
						if(int_xml.containsKey(parent_tag))
						{
							float Age = Float.parseFloat(formObject.getNGValue("cmplx_Customer_age"));
							String cif_id = "";
							String Contact_Name = "";
							if(Age<21){
								for(int i=0;i<formObject.getLVWRowCount("cmplx_DecisionHistory_MultipleApplicantsGrid");i++){
									if("Guarantor".equalsIgnoreCase(formObject.getNGValue("cmplx_DecisionHistory_MultipleApplicantsGrid",i,0))){
										cif_id=formObject.getNGValue("cmplx_DecisionHistory_MultipleApplicantsGrid",i,3);
										Contact_Name=formObject.getNGValue("cmplx_DecisionHistory_MultipleApplicantsGrid",i,1);
									}
								}
								String xml_str = int_xml.get(parent_tag);

								xml_str = xml_str + "<"+tag_name+">"+"<ChildCIFID>"+cif_id+"</ChildCIFID>"+
								"<ChildEntity>CUSTOMER</ChildEntity><ChildEntityType>Retail</ChildEntityType>"+
								"<ContactName>"+Contact_Name+"</ContactName><Relationship>Guardian</Relationship>"
								+"</"+ tag_name+">";

								//RLOS.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
								int_xml.put(parent_tag, xml_str);
							}
						}			

					}

					else{
						int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,Call_name);
					}
				}

				else if("GUARANTOR".equalsIgnoreCase(formObject.getNGValue("cmplx_DecisionHistory_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_DecisionHistory_MultipleApplicantsGrid"),0))){	
					if("PersonDetails".equalsIgnoreCase(tag_name)){
						String xml_str = int_xml.get(parent_tag);
						//	RLOS.mLogger.info("RLOS COMMON"+ " before adding guarantor PersonDetails+ " + xml_str);
						xml_str = xml_str + getGuarantor_PersonDetails(formObject);
						//	RLOS.mLogger.info("RLOS COMMON"+ " after adding PersonDetails " + xml_str);
						int_xml.put(parent_tag, xml_str);

					}
					//change by saurabh for defualt value points on 8th Feb 19.
					else if("PrimaryServiceCenter".equalsIgnoreCase(tag_name) || "PrimaryBranchId".equalsIgnoreCase(tag_name)){
						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<" + tag_name + ">" + formObject.getNGValue("SOLID") + "</" + tag_name + ">";
						int_xml.put(parent_tag, xml_str);
					}
					else if("DocDetails".equalsIgnoreCase(tag_name)){
						String xml_str = int_xml.get(parent_tag);
						//	RLOS.mLogger.info("RLOS COMMON"+ " before adding guarantor doc details " + xml_str);
						xml_str = xml_str + getGuarantor_DocDetails(formObject);
						//	RLOS.mLogger.info("RLOS COMMON"+ " after adding docDetails+ " + xml_str);
						int_xml.put(parent_tag, xml_str);

					}
					else if("PhoneNumber".equalsIgnoreCase(tag_name)){
						String xml_str = int_xml.get(parent_tag);
						String ph_no="";
						ph_no=formObject.getNGValue("cmplx_GuarantorDetails_cmplx_GuarantorGrid",0,returnGridColumnIndex("cmplx_GuarantorDetails_cmplx_GuarantorGrid",NGFUserResourceMgr_RLOS.getGlobalVar("Guarantor_MobileNo")));

						xml_str = xml_str +  "<"+tag_name+">"+ph_no
						+"</"+ tag_name+">";
						//	RLOS.mLogger.info("RLOS COMMON"+ " after adding guarantor PhoneDetails" + xml_str);
						int_xml.put(parent_tag, xml_str);
					}

					else if("MailIdValue".equalsIgnoreCase(tag_name)){

						String xml_str = int_xml.get(parent_tag);
						String email=formObject.getNGValue("cmplx_GuarantorDetails_cmplx_GuarantorGrid",0,returnGridColumnIndex("cmplx_GuarantorDetails_cmplx_GuarantorGrid",NGFUserResourceMgr_RLOS.getGlobalVar("Guarantor_Email")));
						//	RLOS.mLogger.info("RLOS COMMON"+ " before adding guarantor mail details " + xml_str);

						xml_str = xml_str +  "<"+tag_name+">"+email
						+"</"+ tag_name+">";
						//	RLOS.mLogger.info("RLOS COMMON"+ " after adding guarantor MailIdValue" + xml_str);
						int_xml.put(parent_tag, xml_str);
					}

					else if ("AddressDetails".equalsIgnoreCase(tag_name)	&& int_xml.containsKey(parent_tag)) {
						String xml_str = int_xml.get(parent_tag);
						//	RLOS.mLogger.info("RLOS COMMON"+ " before adding address+ " + xml_str);
						xml_str = xml_str + getCustAddress_details();
						//	RLOS.mLogger.info("RLOS COMMON"+ " after adding address+ " + xml_str);
						int_xml.put(parent_tag, xml_str);
					}

					else if("EmploymentStatus".equalsIgnoreCase(tag_name)){

						String xml_str = int_xml.get(parent_tag);
						//added by aman for Drop4 on 4th April
						String status=formObject.getNGValue("cmplx_GuarantorDetails_cmplx_GuarantorGrid",0,returnGridColumnIndex("cmplx_GuarantorDetails_cmplx_GuarantorGrid",NGFUserResourceMgr_RLOS.getGlobalVar("Guarantor_EmploymentType"))); 
						//added by aman for Drop4 on 4th April
						xml_str = xml_str +  "<"+tag_name+">"+status
						+"</"+ tag_name+">";
						//	RLOS.mLogger.info("RLOS COMMON"+ " after adding guarantor EmploymentStatus" + xml_str);
						int_xml.put(parent_tag, xml_str);
					}

					else if("Occupation".equalsIgnoreCase(tag_name)){

						String xml_str = int_xml.get(parent_tag);
						//added by aman for Drop4 on 4th April
						String Occupation=formObject.getNGValue("cmplx_GuarantorDetails_cmplx_GuarantorGrid",0,returnGridColumnIndex("cmplx_GuarantorDetails_cmplx_GuarantorGrid",NGFUserResourceMgr_RLOS.getGlobalVar("Guarantor_Designation")));
						//added by aman for Drop4 on 4th April
						xml_str = xml_str +  "<"+tag_name+">"+Occupation
						+"</"+ tag_name+">";
						//	RLOS.mLogger.info("RLOS COMMON"+ " after adding guarantor EmploymentStatus" + xml_str);
						int_xml.put(parent_tag, xml_str);
					}




					else{
						int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,Call_name);
					}
				}	
			}
		}
		catch(Exception e){
			RLOS.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + "+printException(e));

		}
		//RLOS.mLogger.info("RLOS COMMON"+ " final XML of NEW_CUSTOMER_Custom: " + int_xml);
		return int_xml;
	}
	/*  Function Header:

	 **********************************************************************************

		 NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified   : 6/08/2017  
		Author  : Deepak  
		Description : Function for New Customer Call  

	 ***********************************************************************************  */

	private Map<String, String> NEW_ACCOUNT_Custom(List<List<String>> DB_List,FormReference formObject, String Call_name) {

		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();
		try{
			for (List<String> mylist : DB_List) {
				// for(int i=0;i<col_n.length();i++)
				for (int i = 0; i < 8; i++) {
					// SKLogger_CC.writeLog("rec: "+records.item(rec));
					//	RLOS.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");

				if("AcRequired".equalsIgnoreCase(tag_name)){

					//RLOS.mLogger.info("inside New acc request for AcRequired");
					String loantype = (NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Conventional").equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 0))?"KC":"AC");
					String xml_str = int_xml.get(parent_tag);
					float Age = Float.parseFloat(formObject.getNGValue("cmplx_Customer_age"));
					if(Age<21){
						loantype="KS";
					}

					xml_str = xml_str+"<"+tag_name+">"+loantype+"</"+ tag_name+">";
					//RLOS.mLogger.info("adding Host name in New Loan Req:  "+xml_str);
					int_xml.put(parent_tag, xml_str); 
				}


				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,Call_name);
				}
			}
		}
		catch(Exception e){
			RLOS.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom"+printException(e));
		}
		//RLOS.mLogger.info("RLOS COMMON"+ " final XML of NEW_ACCOUNT_Custom: " + int_xml);
		return int_xml;
	}
	/*  Function Header:

	 **********************************************************************************

		 NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified   : 6/08/2017  
		Author  : Disha  
		Description : Function to Generate Input XML for DECTECH

	 ***********************************************************************************  */

	private Map<String, String> DECTECH_Custom(List<List<String>> OutputXML,FormReference formObject,String callName) {
		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();
		Common_Utils common=new Common_Utils(RLOS.mLogger);
		try{
			for (List<String> mylist : OutputXML) {
				// for(int i=0;i<col_n.length();i++)
				for (int i = 0; i < 8; i++) {
					// SKLogger_CC.writeLog("rec: "+records.item(rec));
					//RLOS.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}

				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");
				//String emp_type = formObject.getNGValue("empType");
				String emp_type = formObject.getNGValue("EmploymentType");

/*				if("age".equalsIgnoreCase(tag_name)){
					RLOS.mLogger.info("inside age aggregate");
					String xml_str = int_xml.get(parent_tag);
					String age=common.getAge_Dectech(formObject.getNGValue("cmplx_Customer_DOb"));
					RLOS.mLogger.info("Age is: "+age);
					xml_str = xml_str+ "<"+tag_name+">"+age+"</"+ tag_name+">";
					int_xml.put(parent_tag, xml_str);	
				}
*/
				if("CallType".equalsIgnoreCase(tag_name)){

					//RLOS.mLogger.info(" iNSIDE CallType+ ");
					String CallType=formObject.getNGValue("DecCallFired");
					//RLOS.mLogger.info(" CallType "+CallType);
					String xml_str = int_xml.get(parent_tag);

					if ("Eligibility".equalsIgnoreCase(CallType)){	

						xml_str = xml_str+ "<"+tag_name+">"+"PM"+"</"+ tag_name+">";
					}
					else if("CalculateDBR".equalsIgnoreCase(CallType)){
						xml_str = xml_str+ "<"+tag_name+">"+"CA"
						+"</"+ tag_name+">";
					}
					//RLOS.mLogger.info(" after adding CallType+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				} 

				else if("Channel".equalsIgnoreCase(tag_name)){
					//RLOS.mLogger.info(" iNSIDE channelcode+ ");
					String ReqProd=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,1);
					String xml_str = int_xml.get(parent_tag);
					xml_str =  "<"+tag_name+">"+(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_PersonalLoan").equalsIgnoreCase(ReqProd)?"PL":"CC")
					+"</"+ tag_name+">";
					//RLOS.mLogger.info(" after adding channelcode+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}

				else if("emp_type".equalsIgnoreCase(tag_name)){
					//	RLOS.mLogger.info(" iNSIDE channelcode+ ");
					String empttype=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6);
					if(empttype!=null){	
						if ("Salaried".equalsIgnoreCase(empttype)){
							empttype="S";
						}
						else if (NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Salariedpensioner").equalsIgnoreCase(empttype)){
							empttype="SP";
						}
						else if ("Pensioner".equalsIgnoreCase(empttype)){
							empttype="P";
						}
						else {
							empttype="SE";
						}
					}
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str+ "<"+tag_name+">"+empttype+"</"+ tag_name+">";

					//	RLOS.mLogger.info(" after adding channelcode+ "+xml_str);
					int_xml.put(parent_tag, xml_str);					
				}
				else if("world_check".equalsIgnoreCase(tag_name)){
					//	RLOS.mLogger.info(" iNSIDE world_check+ ");

					String world_check=formObject.getNGValue("IS_WORLD_CHECK");
					//	RLOS.mLogger.info(" iNSIDE world_check+ "+formObject.getLVWRowCount("cmplx_WorldCheck_WorldCheck_Grid"));
					
					if (("YES").equals(world_check)){
						world_check="Negative";
					}
					else if("Y".equals(world_check)) 
					{
						world_check="Positive";
					}


					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str+ "<"+tag_name+">"+world_check+"</"+ tag_name+">";


					//	RLOS.mLogger.info(" after adding world_check+ "+xml_str);
					int_xml.put(parent_tag, xml_str);


				}

				else if("no_of_partners".equalsIgnoreCase(tag_name)){
					//	RLOS.mLogger.info(" iNSIDE world_check+ ");
					//modified by akshay for proc 6654
					try{
						List<List<String>> partner_count=formObject.getDataFromDataSource("Select count(*) from NG_RLOS_GR_PartnerDetails where part_winame='"+formObject.getWFWorkitemName()+"'");
						String  no_of_partners;
						//	RLOS.mLogger.info("Partner count is "+partner_count);
						if(partner_count!=null && !partner_count.isEmpty()){
							no_of_partners=partner_count.get(0).get(0);
						}
						else{
							no_of_partners="0";
						}
						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str+ "<"+tag_name+">"+no_of_partners+"</"+ tag_name+">";
						//	RLOS.mLogger.info(" after adding no_of_partners+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}
					catch(Exception e){
						printException(e);
					}

				}

				else if("current_emp_catogery".equalsIgnoreCase(tag_name))
				{
					//	RLOS.mLogger.info(" iNSIDE current_emp_catogery+ ");

					String current_emp_catogery=formObject.getNGValue("cmplx_EmploymentDetails_CurrEmployer");
					
					if (("".equalsIgnoreCase(current_emp_catogery)||"--Select--".equalsIgnoreCase(current_emp_catogery) ) && !"Y".equalsIgnoreCase(formObject.getNGValue("Is_Principal_Approval"))) {
						current_emp_catogery = "CN";

					}

					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str+ "<"+tag_name+">"+current_emp_catogery+"</"+ tag_name+">";

					//	RLOS.mLogger.info(" after adding current_emp_catogery+ "+xml_str);
					int_xml.put(parent_tag, xml_str);

				}
				//below code added by nikhi for PCSP-60
				else if("cc_employer_status".equalsIgnoreCase(tag_name))
				{
					RLOS.mLogger.info(" iNSIDE current_emp_catogery+ ");

					String cc_employer_status=formObject.getNGValue("cmplx_EmploymentDetails_EmpStatusCC");
				//change by saurabh for PPG changes.23/4/19
					if (("".equalsIgnoreCase(cc_employer_status)||"--Select--".equalsIgnoreCase(cc_employer_status) ||"UL".equalsIgnoreCase(cc_employer_status))  && !"Y".equalsIgnoreCase(formObject.getNGValue("Is_Principal_Approval"))) {
						cc_employer_status = "CN";

					}

					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str+ "<"+tag_name+">"+cc_employer_status+"</"+ tag_name+">";

					//CreditCard.mLogger.info(" after adding current_emp_catogery+ "+xml_str);
					int_xml.put(parent_tag, xml_str);

				}
				else if("pl_employer_status".equalsIgnoreCase(tag_name))
				{
					RLOS.mLogger.info(" iNSIDE current_emp_catogery+ ");

					String pl_employer_status=formObject.getNGValue("cmplx_EmploymentDetails_EmpStatusPL");
				
					if (("".equalsIgnoreCase(pl_employer_status)||"--Select--".equalsIgnoreCase(pl_employer_status) )  && !"Y".equalsIgnoreCase(formObject.getNGValue("Is_Principal_Approval")) )
				{
						pl_employer_status = "CN";

					}

					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str+ "<"+tag_name+">"+pl_employer_status+"</"+ tag_name+">";

					//CreditCard.mLogger.info(" after adding current_emp_catogery+ "+xml_str);
					int_xml.put(parent_tag, xml_str);

				}
				else if(("prev_loan_dbr".equalsIgnoreCase(tag_name)||"prev_loan_tai".equalsIgnoreCase(tag_name)||"prev_loan_multiple".equalsIgnoreCase(tag_name)||"prev_loan_employer".equalsIgnoreCase(tag_name)))
				{
					//	RLOS.mLogger.info(" iNSIDE prev_loan_dbr+ ");
					String PreviousLoanDBR="";
					String PreviousLoanEmp="";
					String PreviousLoanMultiple="";
					String PreviousLoanTAI="";

					String squeryloan="select isNull(PreviousLoanDBR,0), isNull(PreviousLoanEmp,0), isNull(PreviousLoanMultiple,0), isNull(PreviousLoanTAI,0) from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where Limit_Increase='true'  and Wi_Name= '"+formObject.getWFWorkitemName()+"'";
					List<List<String>> prevLoan=formObject.getNGDataFromDataCache(squeryloan);
					//RLOS.mLogger.info(" iNSIDE prev_loan_dbr+ "+squeryloan);

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



					//	RLOS.mLogger.info(" after adding world_check+ "+xml_str);
					int_xml.put(parent_tag, xml_str);


				}
				else if("no_of_cheque_bounce_int_3mon_Ind".equalsIgnoreCase(tag_name)){
					//RLOS.mLogger.info(" iNSIDE no_of_cheque_bounce_int_3mon_Ind+ ");
					String squerynoc="SELECT count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-3,GETDATE()) and returntype='ICCS' and WI_Name='"+formObject.getWFWorkitemName()+"'";
					List<List<String>> NOC=formObject.getNGDataFromDataCache(squerynoc);
					if (NOC!=null && !NOC.isEmpty())
					{
						String xml_str =  int_xml.get(parent_tag);
						xml_str = xml_str+ "<"+tag_name+">"+NOC.get(0).get(0)
						+"</"+ tag_name+">";

						//RLOS.mLogger.info(" after adding internal_blacklist+ "+xml_str);
						int_xml.put(parent_tag, xml_str);


					}


				}
				else if("no_of_DDS_return_int_3mon_Ind".equalsIgnoreCase(tag_name)){
					//RLOS.mLogger.info(" iNSIDE no_of_cheque_bounce_int_3mon_Ind+ ");
					String squerynoc="SELECT count(*) FROM ng_rlos_FinancialSummary_ReturnsDtls WHERE CAST(returnDate AS datetime) >= DATEADD(month,-3,GETDATE()) and returntype='DDS' and WI_Name='"+formObject.getWFWorkitemName()+"'";
					List<List<String>> NOC=formObject.getNGDataFromDataCache(squerynoc);
					if (NOC!=null && !NOC.isEmpty())
					{
						String xml_str =  int_xml.get(parent_tag);
						xml_str = xml_str+ "<"+tag_name+">"+NOC.get(0).get(0)
						+"</"+ tag_name+">";

						//	RLOS.mLogger.info(" after adding internal_blacklist+ "+xml_str);
						int_xml.put(parent_tag, xml_str);

					}

				}

				else if("borrowing_customer".equalsIgnoreCase(tag_name)){
					//	RLOS.mLogger.info("RLOS iNSIDE borrowing_customer+ ");
					String squeryBorrow="select distinct(borrowingCustomer) from ng_RLOS_CUSTEXPOSE_CardDetails  with (nolock) WHERE  Wi_Name ='"+formObject.getWFWorkitemName()+"' union select distinct(borrowingCustomer) from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock)  WHERE Wi_Name ='"+formObject.getWFWorkitemName()+"'";
					//	RLOS.mLogger.info("RLOS COMMONiNSIDE borrowing_customer query+ "+squeryBorrow);
					List<List<String>> borrowing_customer=formObject.getNGDataFromDataCache(squeryBorrow);
					if (borrowing_customer!=null && !borrowing_customer.isEmpty()){
						String xml_str =  int_xml.get(parent_tag);
						xml_str = xml_str+ "<"+tag_name+">"+borrowing_customer.get(0).get(0)
						+"</"+ tag_name+">";

						//	RLOS.mLogger.info("after adding borrowing_customer+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}
				}

				else if("funding_pattern".equalsIgnoreCase(tag_name)){
					//	RLOS.mLogger.info("RLOS iNSIDE FundingPattern+ ");
					String squeryfund="select distinct(FundingPattern) from ng_RLOS_CUSTEXPOSE_AcctDetails  with (nolock) WHERE  Wi_Name ='"+formObject.getWFWorkitemName()+"'";
					//	RLOS.mLogger.info("RLOS COMMONiNSIDE FundingPattern query+ "+squeryfund);
					List<List<String>> funding_pattern=formObject.getNGDataFromDataCache(squeryfund);
					if (funding_pattern!=null && !funding_pattern.isEmpty()){
						String xml_str =  int_xml.get(parent_tag);
						xml_str = xml_str+ "<"+tag_name+">"+funding_pattern.get(0).get(0)
						+"</"+ tag_name+">";

						//	RLOS.mLogger.info("after adding funding_pattern+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}
				}
				//rachit change CR
				//added by nikhil  for Rachit CR
				else if("ins_value".equalsIgnoreCase(tag_name) || "prem_amnt".equalsIgnoreCase(tag_name) || "no_of_prem_paid".equalsIgnoreCase(tag_name) || "prem_type".equalsIgnoreCase(tag_name) ||"regular_payment".equalsIgnoreCase(tag_name) || "within_minwaiting_period".equalsIgnoreCase(tag_name) ){
					RLOS.mLogger.info("ins_value");
					String ins_value="",prem_amnt="",no_of_prem_paid="",prem_type="",regular_payment="",within_minwaiting_period="";
					
					if("LIFSUR".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")))
					{
						ins_value=formObject.getNGValue("cmplx_EmploymentDetails_lifeInsurance");
						prem_amnt=formObject.getNGValue("cmplx_EmploymentDetails_PremAmt");
						no_of_prem_paid=formObject.getNGValue("cmplx_EmploymentDetails_NoPremPaid");
						prem_type=formObject.getNGValue("cmplx_EmploymentDetails_PremiumType") ;
						regular_payment=formObject.getNGValue("cmplx_EmploymentDetails_RegPay").equalsIgnoreCase("true")?"Yes":"No";
						within_minwaiting_period=formObject.getNGValue("cmplx_EmploymentDetails_Withinminwait").equalsIgnoreCase("true")?"Yes":"No";;
					}
					else if("MOTSUR".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")))
					{
						ins_value=formObject.getNGValue("cmplx_EmploymentDetails_MotorInsurance");
					}
					
					String xml_str = int_xml.get(parent_tag);
					if ("ins_value".equalsIgnoreCase(tag_name)) {
						xml_str = xml_str + "<" + tag_name + ">"+ins_value+"</" + tag_name + ">";
					} else if ("prem_amnt".equalsIgnoreCase(tag_name)) {
						xml_str = xml_str + "<" + tag_name + ">" + prem_amnt + "</" + tag_name + ">";
					} else if ("no_of_prem_paid".equalsIgnoreCase(tag_name)) {
						xml_str = xml_str + "<" + tag_name + ">" + no_of_prem_paid + "</" + tag_name + ">";
					}  else if ("prem_type".equalsIgnoreCase(tag_name)) {
						xml_str = xml_str + "<" + tag_name + ">" + prem_type + "</" + tag_name + ">";
					}
					else if ("regular_payment".equalsIgnoreCase(tag_name)) {
						xml_str = xml_str + "<" + tag_name + ">" + regular_payment + "</" + tag_name + ">";
					}
					else if ("within_minwaiting_period".equalsIgnoreCase(tag_name)) {
						xml_str = xml_str + "<" + tag_name + ">" + within_minwaiting_period + "</" + tag_name + ">";
					}


					int_xml.put(parent_tag, xml_str);
				}
				//end
				//end

				else if("nmf_flag".equalsIgnoreCase(tag_name)){
					String nmfQuery="";
					int Comp_row_count = formObject.getLVWRowCount("cmplx_CompanyDetails_cmplx_CompanyGrid");
					//RLOS.mLogger.info("Company details row xcount: "+Comp_row_count);
					if(Comp_row_count>0){	
						for (int i = 0; i<Comp_row_count;i++){
							if (formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 2).equalsIgnoreCase("Secondary")){
								String emp_name = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 0); //0
								nmfQuery="select count(*) from NG_Master_NMF_not_listed_Comp  with (nolock) where comp_name='"+emp_name+"'";
							}
							else{
								nmfQuery="select count(*) from NG_Master_NMF_not_listed_Comp with (nolock) where comp_name='"+formObject.getNGValue("cmplx_EmploymentDetails_EmpName")+"'";
							}
						}
					}
					else{
						nmfQuery="select count(*) from NG_Master_NMF_not_listed_Comp with (nolock) where comp_name='"+formObject.getNGValue("cmplx_EmploymentDetails_EmpName")+"'";
					}
					//RLOS.mLogger.info("RLOS iNSIDE nmf_flag+ ");
					//RLOS.mLogger.info("RLOS COMMONiNSIDE borrowing_customer query+ "+nmfQuery);
					List<List<String>> nmfQueryData=formObject.getNGDataFromDataCache(nmfQuery);
					if (nmfQueryData!=null && !nmfQueryData.isEmpty()){
						String NMFflag="";
						if (nmfQueryData.get(0).get(0).equalsIgnoreCase("0")){
							NMFflag="N";
						}
						else {
							NMFflag="Y";
						}
						String xml_str =  int_xml.get(parent_tag);
						xml_str = xml_str+ "<"+tag_name+">"+NMFflag+"</"+ tag_name+">";

						//	RLOS.mLogger.info("after adding nmfQueryData+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}
				}

				else if("fd_amount".equalsIgnoreCase(tag_name)){
					//RLOS.mLogger.info("RLOS iNSIDE fd_amount+ ");
					String fd_amount=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 16);	
					String xml_str =  int_xml.get(parent_tag);
					xml_str = xml_str+ "<"+tag_name+">"+fd_amount
					+"</"+ tag_name+">";

					//	RLOS.mLogger.info("after adding fd_amount+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}

				else if("no_bank_other_statement_provided".equalsIgnoreCase(tag_name)){
					//	RLOS.mLogger.info("RLOS iNSIDE no_bank_other_statement_provided+ ");
					String ProdInfo=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 6);	
					String no_bank_other_statement_provided="";
					if ("Self Employed".equalsIgnoreCase(ProdInfo)){
						no_bank_other_statement_provided=formObject.getNGValue("cmplx_IncomeDetails_NoOfMonthsOtherbankStat2");
					}
					else {
						no_bank_other_statement_provided=formObject.getNGValue("cmplx_IncomeDetails_NoOfMonthsOtherbankStat");

					}
					String xml_str =  int_xml.get(parent_tag);
					xml_str = xml_str+ "<"+tag_name+">"+no_bank_other_statement_provided
					+"</"+ tag_name+">";

					//		RLOS.mLogger.info("after adding fd_amount+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}


				else if((("lob".equalsIgnoreCase(tag_name))||("target_segment_code".equalsIgnoreCase(tag_name))||("designation".equalsIgnoreCase(tag_name))||("emp_name".equalsIgnoreCase(tag_name))||("industry_sector".equalsIgnoreCase(tag_name))||("eff_lob".equalsIgnoreCase(tag_name))||("industry_macro".equalsIgnoreCase(tag_name))||("industry_micro".equalsIgnoreCase(tag_name))||("bvr".equalsIgnoreCase(tag_name))||("eff_date_estba".equalsIgnoreCase(tag_name))||("poa".equalsIgnoreCase(tag_name))||("tlc_issue_date".equalsIgnoreCase(tag_name))||("head_offc_emirate".equalsIgnoreCase(tag_name))) && NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SelfEmployed").equalsIgnoreCase(emp_type)){
					//		RLOS.mLogger.info("RLOSCommon java file inside target_segment_code : ");
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
					String head_offc_emirate="";
					String effLOB="";
					String tlc_issue_date="";
					if(Comp_row_count!=0){	
						for (int i = 0; i<Comp_row_count;i++){
							if (formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 2).equalsIgnoreCase("Secondary")){
								lob = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 15); //0
								target_segment_code = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 25); //0
								//designation = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 11); //0
								emp_name = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 0); //0
								industry_sector = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 8); //0
								eff_date_estba = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 14); //0
								//industry_sector = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 5); //0
								industry_marco = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 9); //0
								industry_micro = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 10); //0
								bvr = (NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_true").equalsIgnoreCase(formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 19))?"Y":"N"); //0
								poa = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 23); //0
								tlc_issue_date = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 27); //0
								effLOB = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 16); //0
								head_offc_emirate = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 18); //0----17 is EOW

							}

						}
						try
						{
							List<List<String>> auth_desig=formObject.getDataFromDataSource("select Designation from ng_rlos_gr_AuthSignDetails where sign_winame='"+formObject.getWFWorkitemName()+"'");
							//  no_of_partners;
							//	RLOS.mLogger.info("Partner count is "+partner_count);
							if(auth_desig!=null && !auth_desig.isEmpty()){
								designation=auth_desig.get(1).get(0);
							}
							else{
								designation="";
							}
						}
						catch(Exception ex)
						{
							RLOS.mLogger.info(ex.getMessage());
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
					else if("industry_macro".equalsIgnoreCase(tag_name)){
						xml_str = xml_str+ "<"+tag_name+">"+industry_marco+"</"+ tag_name+">";
					}
					else if("industry_micro".equalsIgnoreCase(tag_name)){
						xml_str = xml_str+ "<"+tag_name+">"+industry_micro+"</"+ tag_name+">";
					}
					else if("bvr".equalsIgnoreCase(tag_name)){
						xml_str = xml_str+ "<"+tag_name+">"+bvr+"</"+ tag_name+">";
					}
					else if("eff_date_estba".equalsIgnoreCase(tag_name)){
						xml_str = xml_str+ "<"+tag_name+">"+eff_date_estba+"</"+ tag_name+">";
					}
					else if("poa".equalsIgnoreCase(tag_name)){
						xml_str = xml_str+ "<"+tag_name+">"+poa+"</"+ tag_name+">";
					}
					else if("tlc_issue_date".equalsIgnoreCase(tag_name)){
						xml_str = xml_str+ "<"+tag_name+">"+tlc_issue_date+"</"+ tag_name+">";
					}
					else if("eff_lob".equalsIgnoreCase(tag_name)){
						xml_str = xml_str+ "<"+tag_name+">"+effLOB+"</"+ tag_name+">";
					}
					else if("head_offc_emirate".equalsIgnoreCase(tag_name)){
						xml_str = xml_str+ "<"+tag_name+">"+head_offc_emirate+"</"+ tag_name+">";
					}

					//	RLOS.mLogger.info(" after adding cmplx_CompanyDetails_cmplx_CompanyGrid+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}

				else if("lob".equalsIgnoreCase(tag_name)&& 
						!"Self Employed".equalsIgnoreCase(emp_type)){
					//CreditCard.mLogger.info("RLOS iNSIDE fd_amount+ ");
					String lob=formObject.getNGValue("cmplx_EmploymentDetails_LengthOfBusiness");	
					String xml_str =  int_xml.get(parent_tag);
					xml_str = xml_str+ "<"+tag_name+">"+lob
					+"</"+ tag_name+">";

					//CreditCard.mLogger.info("after adding fd_amount+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else if((("blacklist_cust_type".equalsIgnoreCase(tag_name))||("internal_blacklist".equalsIgnoreCase(tag_name))||("internal_blacklist_date".equalsIgnoreCase(tag_name))||("internal_blacklist_code".equalsIgnoreCase(tag_name))||("negative_cust_type".equalsIgnoreCase(tag_name))||("internal_negative_flag".equalsIgnoreCase(tag_name))||("internal_negative_date".equalsIgnoreCase(tag_name))||("internal_negative_code".equalsIgnoreCase(tag_name)))){
					//		RLOS.mLogger.info(" iNSIDE channelcode+ ");
					String squeryBlacklist="select BlacklistFlag,BlacklistDate,BlacklistReasonCode,NegatedFlag,NegatedDate,NegatedReasonCode from ng_rlos_cif_detail  with (nolock) where cif_wi_name='"+formObject.getWFWorkitemName()+"' and cif_searchType = 'Internal'";
					List<List<String>> Blacklist=formObject.getNGDataFromDataCache(squeryBlacklist);
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
					else if("negative_cust_type".equalsIgnoreCase(tag_name)){
						xml_str = xml_str+ "<"+tag_name+">I</"+ tag_name+">";
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
					//		RLOS.mLogger.info(" after adding internal_blacklist+ "+xml_str);
					int_xml.put(parent_tag, xml_str);




				}

				else if(("external_blacklist_flag".equalsIgnoreCase(tag_name)||"external_blacklist_date".equalsIgnoreCase(tag_name)||"external_blacklist_code".equalsIgnoreCase(tag_name))){
					//		RLOS.mLogger.info(" iNSIDE channelcode+ ");
					String squeryBlacklist="select BlacklistFlag,BlacklistDate,BlacklistReasonCode from ng_rlos_cif_detail with (nolock)  where cif_wi_name='"+formObject.getWFWorkitemName()+"' and cif_searchType = 'External'";
					List<List<String>> Blacklist=formObject.getNGDataFromDataCache(squeryBlacklist);
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

					//	RLOS.mLogger.info(" after adding internal_blacklist+ "+xml_str);
					int_xml.put(parent_tag, xml_str);


				}

				else if(("auth_sig_sole_emp".equalsIgnoreCase(tag_name)||"shareholding_perc".equalsIgnoreCase(tag_name))&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SelfEmployed").equalsIgnoreCase(emp_type)){
					//	RLOS.mLogger.info(" iNSIDE channelcode+ ");
					String auth_sig_sole_emp =  "";
					String shareholding_perc =  "";
					int Authsign_row_count = formObject.getLVWRowCount("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails");

					if (Authsign_row_count !=0){
						auth_sig_sole_emp =(NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_Yes").equalsIgnoreCase(formObject.getNGValue("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails", 0, 10))?"Y":"N"); //0
						shareholding_perc = formObject.getNGValue("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails", 0, 9); //0


					}
					String xml_str =  int_xml.get(parent_tag);
					if("auth_sig_sole_emp".equalsIgnoreCase(tag_name)){
						xml_str = xml_str+ "<"+tag_name+">"+auth_sig_sole_emp+"</"+ tag_name+">";
					}
					else if("shareholding_perc".equalsIgnoreCase(tag_name)){

						xml_str = xml_str+ "<"+tag_name+">"+shareholding_perc+"</"+ tag_name+">";
					}


					//		RLOS.mLogger.info(" after adding shareholding_perc+ "+xml_str);
					int_xml.put(parent_tag, xml_str);


				}

				else if("ApplicationDetails".equalsIgnoreCase(tag_name)){

					//	RLOS.mLogger.info("inside DECTECH req1");

					//	RLOS.mLogger.info("inside customer update req2");
					String xml_str = int_xml.get(parent_tag);
					//	RLOS.mLogger.info(" before adding product+ "+xml_str);
					xml_str = xml_str + getProduct_details();
					//	RLOS.mLogger.info(" after adding product+ "+xml_str);
					int_xml.put(parent_tag, xml_str);

				}

				else if("cust_name".equalsIgnoreCase(tag_name)){
					if(int_xml.containsKey(parent_tag))
					{
						String first_name=formObject.getNGValue("cmplx_Customer_FIrstNAme");
						String middle_name=formObject.getNGValue("cmplx_Customer_MiddleName");
						String last_name=formObject.getNGValue("cmplx_Customer_LAstNAme");
						String full_name=first_name+" "+middle_name+" "+last_name;
						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<"+tag_name+">"+full_name +"</"+ tag_name+">";
						//		RLOS.mLogger.info(" after adding confirmedinjob+ "+xml_str);
						int_xml.put(parent_tag, xml_str);

					}			
				}
				else if("confirmed_in_job".equalsIgnoreCase(tag_name)){
					if(int_xml.containsKey(parent_tag))
					{
						String confirmedinjob=formObject.getNGValue("cmplx_EmploymentDetails_JobConfirmed")!=null&&formObject.getNGValue("cmplx_EmploymentDetails_JobConfirmed").equalsIgnoreCase("true")?"Y":"N";;
						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<"+tag_name+">"+confirmedinjob
						+"</"+ tag_name+">";

						//	RLOS.mLogger.info(" after adding confirmedinjob+ "+xml_str);
						int_xml.put(parent_tag, xml_str);

					}			
				}
				else if("included_pl_aloc".equalsIgnoreCase(tag_name)){
					if(int_xml.containsKey(parent_tag))
					{

						String included_pl_aloc = formObject.getNGValue("cmplx_EmploymentDetails_IncInPL");

						if (included_pl_aloc != null) {
							if ("true".equalsIgnoreCase(included_pl_aloc)) 
							{
								included_pl_aloc = "Y";
							} 
							else 
							{
								included_pl_aloc = "N";
							}

							String xml_str = int_xml.get(parent_tag);
							xml_str = xml_str + "<" + tag_name + ">" + included_pl_aloc+ "</" + tag_name + ">";

							//		RLOS.mLogger.info("RLOS COMMON"+" after adding included_pl_aloc+ " + xml_str);
							int_xml.put(parent_tag, xml_str);
						}

					}
				}
				else if("included_cc_aloc".equalsIgnoreCase(tag_name)){
					if(int_xml.containsKey(parent_tag))
					{
						String included_cc_aloc = formObject.getNGValue("cmplx_EmploymentDetails_IncInCC");

						if (included_cc_aloc != null) {
							if ("true".equalsIgnoreCase(included_cc_aloc)) 
							{
								included_cc_aloc = "Y";
							} else 
							{
								included_cc_aloc = "N";
							}

							String xml_str = int_xml.get(parent_tag);
							xml_str = xml_str + "<" + tag_name + ">" + included_cc_aloc+ "</" + tag_name + ">";

							//		RLOS.mLogger.info("RLOS COMMON"+" after adding cmplx_EmploymentDetails_IncInCC+ "+ xml_str);
							int_xml.put(parent_tag, xml_str);
						}}
				}
				else if("vip_flag".equalsIgnoreCase(tag_name)){

					if(int_xml.containsKey(parent_tag))
					{
						String vip_flag=formObject.getNGValue("cmplx_Customer_VIPFlag")!=null&&formObject.getNGValue("cmplx_Customer_VIPFlag").equalsIgnoreCase("true")?"Y":"N";
						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<"+tag_name+">"+vip_flag
						+"</"+ tag_name+">";

						//	RLOS.mLogger.info(" after adding cmplx_Customer_VIPFlag+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}			
				}
				else if("standing_instruction".equalsIgnoreCase(tag_name)){

					//	RLOS.mLogger.info(" iNSIDE standing_instruction+ ");
					String squerynoc="SELECT count(*) FROM ng_rlos_FinancialSummary_SiDtls WHERE WI_Name='"+formObject.getWFWorkitemName()+"'";
					List<List<String>> NOC=formObject.getNGDataFromDataCache(squerynoc);
					//	RLOS.mLogger.info(" after adding cmplx_Customer_VIPFlag+ "+squerynoc);
					//	RLOS.mLogger.info(" after adding cmplx_Customer_VIPFlag+ "+NOC);
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
						xml_str = xml_str+ "<"+tag_name+">"+standing_instruction +"</"+ tag_name+">";

						//		RLOS.mLogger.info(" after adding standing_instruction+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}


				}
				//Coding Done for avg_credit_turnover_6, avg_credit_turnover_3,avg_bal_3 and avg_bal_6 tag
				
				//modified by sagarika 
				else if(("avg_credit_turnover_6".equalsIgnoreCase(tag_name)||"avg_credit_turnover_3".equalsIgnoreCase(tag_name)||"avg_bal_3".equalsIgnoreCase(tag_name)||"avg_bal_6".equalsIgnoreCase(tag_name) )){
					RLOS.mLogger.info("@sagarika rlos");
					if(int_xml.containsKey(parent_tag))
					{
						//if (NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SelfEmployed").equalsIgnoreCase(emp_type)){
						if("PA".equalsIgnoreCase(formObject.getNGValue("Subproduct_productGrid")) || "Self Employed".equalsIgnoreCase(emp_type)){
						RLOS.mLogger.info("sagarika");
							double avg_credit_turnover6th=0;
							double avg_credit_turnover3rd=0;
							double avg_bal_3=0;
							double avg_bal_6=0;
							String avg_credit_turnover_freq=formObject.getNGValue("cmplx_IncomeDetails_AvgCredTurnoverFreq");
							String avg_bal_freq=formObject.getNGValue("cmplx_IncomeDetails_AvgBalFreq");
							String avg_credit_turnover=formObject.getNGValue("cmplx_IncomeDetails_AvgCredTurnover");
							String avg_bal=formObject.getNGValue("cmplx_IncomeDetails_AvgBal");
							//RLOS.mLogger.info("avg_credit_turnover6th value"+avg_credit_turnover_freq);
							//RLOS.mLogger.info("avg_credit_turnover6th value"+avg_bal_freq);
							//RLOS.mLogger.info("avg_credit_turnover6th value"+avg_credit_turnover);
							//RLOS.mLogger.info("avg_credit_turnover6th value"+avg_bal);
							String avg_bal_6_str="";
							String avg_bal_3_str="";
							String avg_credit_turnover3rd_str="";
							String avg_credit_turnover6th_str="";
							if (avg_credit_turnover!=(null)&&(!"".equalsIgnoreCase(avg_credit_turnover))){
								avg_credit_turnover6th=Double.parseDouble(avg_credit_turnover);
								//		RLOS.mLogger.info("avg_credit_turnover6th value"+avg_credit_turnover6th);

								if ("Annually".equalsIgnoreCase(avg_credit_turnover_freq)){
									avg_credit_turnover6th=avg_credit_turnover6th/2;
									avg_credit_turnover3rd=avg_credit_turnover6th/2;
								}
								else if ("Half Yearly".equalsIgnoreCase(avg_credit_turnover_freq)){

									avg_credit_turnover3rd=avg_credit_turnover6th/2;
								}
								else if ("Quarterly".equalsIgnoreCase(avg_credit_turnover_freq)){
									avg_credit_turnover6th=2*avg_credit_turnover6th;
									//		RLOS.mLogger.info("avg_credit_turnover6th value"+avg_credit_turnover6th);
									avg_credit_turnover3rd=avg_credit_turnover6th/2;
									//		RLOS.mLogger.info("avg_credit_turnover3rd value"+avg_credit_turnover3rd);
								}
								else if ("monthly".equalsIgnoreCase(avg_credit_turnover_freq)){
									avg_credit_turnover6th=6*avg_credit_turnover6th;
									avg_credit_turnover3rd=avg_credit_turnover6th/2;
								}
								//		RLOS.mLogger.info("avg_credit_turnover3rd value before"+avg_credit_turnover3rd);
								avg_credit_turnover3rd_str=String.format("%.0f", avg_credit_turnover3rd);
								avg_credit_turnover6th_str=String.format("%.0f", avg_credit_turnover6th);
								//		RLOS.mLogger.info("avg_credit_turnover3rd value after"+avg_credit_turnover3rd);
							}
							if (avg_bal!=(null)&&(!"".equalsIgnoreCase(avg_bal))){
								//avg_bal_6=Double.parseDouble(avg_bal);
								//		RLOS.mLogger.info("avg_credit_turnover6th value"+avg_credit_turnover6th);

								avg_bal_6=Double.parseDouble(avg_bal);
								if ("Annually".equalsIgnoreCase(avg_bal_freq)){

									avg_bal_6=avg_bal_6/2;
									avg_bal_3=avg_bal_6/2;
								}
								else if ("Half Yearly".equalsIgnoreCase(avg_bal_freq)){

									avg_bal_3=avg_bal_6/2;
								}
								else if ("Quarterly".equalsIgnoreCase(avg_bal_freq)){

									avg_bal_6=2*avg_bal_6;
									avg_bal_3=avg_bal_6/2;
								}
								else if ("monthly".equalsIgnoreCase(avg_bal_freq)){

									avg_bal_6=6*avg_bal_6;
									avg_bal_3=avg_bal_6/2;

								}
								avg_bal_6_str=String.format("%.0f", avg_bal_6);
								avg_bal_3_str=String.format("%.0f", avg_bal_3);



							}	

							String xml_str = int_xml.get(parent_tag);

							if ("avg_credit_turnover_6".equalsIgnoreCase(tag_name)){
								xml_str = xml_str + "<"+tag_name+">"+avg_credit_turnover6th_str
								+"</"+ tag_name+">";
							}
							else if ("avg_credit_turnover_3".equalsIgnoreCase(tag_name)){
								xml_str = xml_str + "<"+tag_name+">"+avg_credit_turnover3rd_str
								+"</"+ tag_name+">";
							}
							else if ("avg_bal_3".equalsIgnoreCase(tag_name)){

								xml_str = xml_str + "<"+tag_name+">"+avg_bal_3_str
								+"</"+ tag_name+">";
							}
							else if ("avg_bal_6".equalsIgnoreCase(tag_name)){

								xml_str = xml_str + "<"+tag_name+">"+avg_bal_6_str
								+"</"+ tag_name+">";
							}
							//	RLOS.mLogger.info(" after adding cmplx_Customer_VIPFlag+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}	
					}		
				}
				//Coding Done for avg_credit_turnover_6, avg_credit_turnover_3,avg_bal_3 and avg_bal_6 tag
				/*else if("aggregate_exposed".equalsIgnoreCase(tag_name)){

					if(int_xml.containsKey(parent_tag))
					{
						String aeQuery = "select isnull(CreditLimit,0) as CreditLimit,'' as TotalOutstandingAmt,'' as TotalAmt, '' as TotalAmount,'' as TakeOverAmount FROM ng_RLOS_CUSTEXPOSE_CardDetails where Wi_Name='"+formObject.getWFWorkitemName()+"' union all select '',isnull(TotalOutstandingAmt,0),'','','' FROM ng_RLOS_CUSTEXPOSE_LoanDetails where Wi_Name='"+formObject.getWFWorkitemName()+"' union all select '','',isnull(TotalAmt,0),'','' FROM ng_rlos_cust_extexpo_LoanDetails where Wi_Name='"+formObject.getWFWorkitemName()+"' union all select '','','',isnull(TotalAmount,0),'' FROM ng_rlos_cust_extexpo_CardDetails where Wi_Name='"+formObject.getWFWorkitemName()+"' union all select '','','','',isnull(TakeOverAmount,0) FROM ng_rlos_gr_LiabilityAddition where LiabilityAddition_wiName='"+formObject.getWFWorkitemName()+"'";


						List<List<String>> aggregate_exposed = formObject.getNGDataFromDataCache(aeQuery);


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
						Total=CreditLimit+TotalOutstandingAmt+TotalAmount+TotalAmt+takeOverAmount;
						BigDecimal Tot= BigDecimal.valueOf(CreditLimit+TotalOutstandingAmt+TotalAmount+TotalAmt);
						RLOS.mLogger.info("values"+Total);
						RLOS.mLogger.info("values"+Tot);



						String conv_String=putComma(Tot.toString());//added by akshay on 25/9/17 as per point 3 of problem sheet
						String aggr_expo=conv_String.replace(",", ""); // DOne for temp purpose by aman
						RLOS.mLogger.info("aggr_expo values"+aggr_expo);
						formObject.setNGValue("cmplx_Liability_New_AggrExposure", conv_String);//changed by akshay on 25/9/17 as per point 2 of problem sheet
						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<"+tag_name+">"+aggr_expo+"</"+ tag_name+">";

						RLOS.mLogger.info(" after adding aggregate_exposed+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}
				}
				 */

				else if("aggregate_exposed".equalsIgnoreCase(tag_name)){
					//		RLOS.mLogger.info(" Inside Aggregate ");
					if(int_xml.containsKey(parent_tag))
					{
						//Tarang 24 Dec, New method added to handle invalid session in fragment save event.
						Custom_fragmentSave("EligibilityAndProductInformation");
						String aeQuery = "select (select isNull((Sum(convert(float,replace([TotalOutstandingAmt],'NA','0')))),0) as TotalOutstandingAmt from ng_RLOS_CUSTEXPOSE_loanDetails with (nolock) where Consider_For_Obligations='true' and LoanStat in ('A','ACTIVE') AND TotalOutstandingAmt not like '%-%' and wi_name ='"+formObject.getWFWorkitemName()+"')+(select isNull((Sum(convert(float,replace([TotalOutstandingAmt],'NA','0')))),0) as TotalOutstandingAmt from ng_RLOS_CUSTEXPOSE_loanDetails with (nolock) where Consider_For_Obligations='true' and LoanStat ='Pipeline' AND TotalOutstandingAmt not like '%-%' and wi_name ='"+formObject.getWFWorkitemName()+"')+(select isNull((Sum(convert(float,replace([OutstandingBalance],'NA','0')))),0)  from ng_RLOS_CUSTEXPOSE_CardinstallmentDetails installment join ng_RLOS_CUSTEXPOSE_CardDetails carddetails with (nolock) on carddetails.CardEmbossNum=installment.CardcrnNumber where  carddetails.Consider_For_Obligations='true' and carddetails.CardStatus in ('A','ACTIVE') and carddetails.wi_name  ='"+formObject.getWFWorkitemName()+"' and SchemeCardProd like '%LOC%' AND OutstandingBalance not like '%-%')+(select isNull((Sum(convert(float,replace([CreditLimit],'NA','0')))),0) as OutstandingAmt from (select max( liab.CreditLimit) as CreditLimit from ng_RLOS_CUSTEXPOSE_CardDetails liab, ng_master_cardProduct prod with (nolock) where  Consider_For_Obligations='true' and CardStatus in ('A','ACTIVE') and wi_name ='"+formObject.getWFWorkitemName()+"' and SchemeCardProd not like '%LOC%' AND CreditLimit not like '%-%' and liab.SchemeCardProd=prod.CODE group by case when prod.ReqProduct='Conventional' then prod.ReqProduct else liab.SchemeCardProd end) as TempTable)+( select isNull((Sum(convert(float,replace([final_limit],'NA','0')))),0) from ng_rlos_EligAndProdInfo with (nolock) where Wi_Name ='"+formObject.getWFWorkitemName()+"')+( select isNull((Sum(convert(float,replace([sanctionLimit],'NA','0')))),0) from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where Wi_Name ='"+formObject.getWFWorkitemName()+"' and ODDesc is not null and AcctStat in ('A','ACTIVE')) as aggregateExposure";


						//		RLOS.mLogger.info(" Inside Aggregate "+aeQuery);

						List<List<String>> aggregate_exposed = formObject.getDataFromDataSource(aeQuery);//changed by akshay for proc 7711

						String aggr_expo=aggregate_exposed.get(0).get(0);
						double aggreg=Double.parseDouble(aggr_expo);
						aggr_expo=String.format("%.2f", aggreg);

						formObject.setNGValue("cmplx_Liability_New_AggrExposure", aggr_expo);//changed by akshay on 25/9/17 as per point 2 of problem sheet
						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<"+tag_name+">"+aggr_expo+"</"+ tag_name+">";

						//		RLOS.mLogger.info(" after adding aggregate_exposed+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}
				}

				else if("accomodation_provided".equalsIgnoreCase(tag_name)){

					if(int_xml.containsKey(parent_tag))
					{
						String accomodation_provided=formObject.getNGValue("cmplx_IncomeDetails_Accomodation")!=null&&formObject.getNGValue("cmplx_IncomeDetails_Accomodation").equalsIgnoreCase("Yes")?"Y":"N";;
						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<"+tag_name+">"+accomodation_provided
						+"</"+ tag_name+">";
						//		RLOS.mLogger.info(" after adding confirmedinjob+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}
				}
				else if("RejectedDetails".equalsIgnoreCase(tag_name)){

					if(int_xml.containsKey(parent_tag))
					{

						String xml_str = int_xml.get(parent_tag);
						// RLOS.mLogger.info(" before adding internal RejectedDetails+ "+xml_str);
						try{
							List<String> objInput = new ArrayList();
							List<Object> objOutput = new ArrayList();

							objInput.add("Text:" + formObject.getWFWorkitemName());
							objOutput.add("Text");
							//   RLOS.mLogger.info(" input for procedure: Ng_Rlos_Dectech_Rejected_app"+objInput.toString());
							objOutput = formObject.getDataFromStoredProcedure("Ng_Rlos_Dectech_Rejected_app", objInput, objOutput);
							//RLOS.mLogger.info(" procedure executed objOutput"+objOutput.toString());

							xml_str = xml_str + getRejectedDetails();
							//  RLOS.mLogger.info(" after internal RejectedDetails+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}
						catch(Exception e){
							RLOS.mLogger.info(" Expection occured in RejectedDetails+ "+e.getMessage());
							printException(e);
						}
					}


				}
				else if("AccountDetails".equalsIgnoreCase(tag_name)){

					if(int_xml.containsKey(parent_tag))
					{

						String xml_str = int_xml.get(parent_tag);
						//	RLOS.mLogger.info(" before adding internal liability+ "+xml_str);
						xml_str = xml_str + getInternalLiabDetails();
						//	RLOS.mLogger.info(" after internal liability+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}


				}
				else if("InternalBureau".equalsIgnoreCase(tag_name)){


					String xml_str = int_xml.get(parent_tag);
					//RLOS.mLogger.info(" before adding InternalBureauData+ "+xml_str);
					String temp = InternalBureauData();
					if(!"".equalsIgnoreCase(temp)){

						xml_str =  temp;
						//	RLOS.mLogger.info(" after InternalBureauData+ "+xml_str);
						int_xml.get(parent_tag);
						int_xml.put(parent_tag, xml_str);
					}
				}
				else if("InternalBouncedCheques".equalsIgnoreCase(tag_name)){
					String xml_str = int_xml.get(parent_tag);
					//	RLOS.mLogger.info(" before adding InternalBouncedCheques+ "+xml_str);
					String temp = InternalBouncedCheques();
					if(!"".equalsIgnoreCase(temp)){
						if (xml_str==null){
							//RLOS.mLogger.info(" before adding bhrabc"+xml_str);
							xml_str="";
						}
						xml_str = xml_str + temp;
						//	RLOS.mLogger.info(" after InternalBouncedCheques+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}

				}
				else if("InternalBureauIndividualProducts".equalsIgnoreCase(tag_name)){
					String xml_str = int_xml.get(parent_tag);
					//	RLOS.mLogger.info(" before adding InternalBureauIndividualProducts+ "+xml_str);
					String temp = InternalBureauIndividualProducts();
					if(!"".equalsIgnoreCase(temp)){

						if (xml_str==null){
							//		RLOS.mLogger.info(" before adding bhrabc"+xml_str);
							xml_str="";
						}
						xml_str = xml_str + temp;
						//	RLOS.mLogger.info(" after InternalBureauIndividualProducts+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}

				}
				else if("InternalBureauPipelineProducts".equalsIgnoreCase(tag_name)){
					String xml_str = int_xml.get(parent_tag);
					//	RLOS.mLogger.info(" before adding InternalBureauPipelineProducts+ "+xml_str);
					String temp = InternalBureauPipelineProducts();
					if(!"".equalsIgnoreCase(temp)){

						if (xml_str==null){
							//		RLOS.mLogger.info(" before adding bhrabc"+xml_str);
							xml_str="";
						}
						xml_str = xml_str + temp;
						//	RLOS.mLogger.info(" after InternalBureauPipelineProducts+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}

				}
				else if("ExternalBureau".equalsIgnoreCase(tag_name)){
					String xml_str = int_xml.get(parent_tag);
					//	RLOS.mLogger.info(" before adding ExternalBureau+ "+xml_str);
					String temp = ExternalBureauData();
					if(!"".equalsIgnoreCase(temp)){

						xml_str =  temp;
						//		RLOS.mLogger.info(" after ExternalBureau+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}

				}
				else if("ExternalBouncedCheques".equalsIgnoreCase(tag_name)){
					String xml_str = int_xml.get(parent_tag);
					//	RLOS.mLogger.info(" before adding ExternalBouncedCheques+ "+xml_str);
					String temp = ExternalBouncedCheques();
					if(!"".equalsIgnoreCase(temp)){

						if (xml_str==null){
							//			RLOS.mLogger.info(" before adding bhrabc"+xml_str);
							xml_str="";
						}
						xml_str =  xml_str + temp;
						//		RLOS.mLogger.info(" after ExternalBouncedCheques+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}	
				}
				else if("ExternalBureauIndividualProducts".equalsIgnoreCase(tag_name)){
					String xml_str = int_xml.get(parent_tag);
					//	RLOS.mLogger.info(" before adding ExternalBureauIndividualProducts+ "+xml_str);
					String temp =  ExternalBureauIndividualProducts();
					String Manual_add_Liab = ExternalBureauManualAddIndividualProducts();

					if((!"".equalsIgnoreCase(temp)) || (!"".equalsIgnoreCase(Manual_add_Liab))){

						if (xml_str==null){
							//		RLOS.mLogger.info(" before adding bhrabc"+xml_str);
							xml_str="";
						}
						xml_str =  xml_str + temp + Manual_add_Liab;
						//		RLOS.mLogger.info(" after ExternalBureauIndividualProducts+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}		
				}
				else if("ExternalBureauPipelineProducts".equalsIgnoreCase(tag_name)){
					String xml_str = int_xml.get(parent_tag);
					//	RLOS.mLogger.info(" before adding ExternalBureauPipelineProducts+ "+xml_str);
					String temp =  ExternalBureauPipelineProducts();
					if(!"".equalsIgnoreCase(temp)){

						if (xml_str==null){
							//		RLOS.mLogger.info(" before adding bhrabc"+xml_str);
							xml_str="";
						}
						xml_str =  xml_str + temp;
						//		RLOS.mLogger.info(" after ExternalBureauPipelineProducts+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}	
				}
				else if(("gross_salary".equalsIgnoreCase(tag_name))&& NGFUserResourceMgr_RLOS.getGlobalVar("RLOS_SelfEmployed").equalsIgnoreCase(emp_type)){
					//	RLOS.mLogger.info(" iNSIDE channelcode+ ");
					String xml_str = int_xml.get(parent_tag);

					xml_str = xml_str+ "<"+tag_name+">"+""+"</"+ tag_name+">";



					//RLOS.mLogger.info(" after adding shareholding_perc+ "+xml_str);
					int_xml.put(parent_tag, xml_str);


				}


				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
				}
			}
		}
		catch(Exception e){
			//RLOS.mLogger.info("CC Integration "+ " Exception occured in DECTECH_Custom + "+printException(e));
			printException(e);
		}
		//RLOS.mLogger.info("CC Integration "+ " fianl XML: + "+int_xml);
		return int_xml;
	}
	/*  Function Header:

	 **********************************************************************************

		 NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified   : 6/08/2017  
		Author  : Deepak  
		Description : Function for Dedupe Summary call

	 ***********************************************************************************  */

	private Map<String, String> GenDefault(List<List<String>> DB_List,FormReference formObject,String callName) {


		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();

		//FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try{
			for (List<String> mylist : DB_List) {
				// for(int i=0;i<col_n.length();i++)
				for (int i = 0; i < 8; i++) {
					// SKLogger_CC.writeLog("rec: "+records.item(rec));
					//	RLOS.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);

			}
		}
		catch(Exception e){
			//RLOS.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + ");
			printException(e);
		}

		return int_xml;
	}
	/*  Function Header:

	 **********************************************************************************

		 NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified   : 6/08/2017  
		Author  : Deepak  
		Description : Function for Dedupe Summary call

	 ***********************************************************************************  */

	private Map<String, String> EID_Genuine_Custom(List<List<String>> DB_List,FormReference formObject,String callName) {


		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();

		//FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try{
			for (List<String> mylist : DB_List) {
				// for(int i=0;i<col_n.length();i++)
				for (int i = 0; i < 8; i++) {
					// SKLogger_CC.writeLog("rec: "+records.item(rec));
					//		RLOS.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");


				if("ServiceId".equalsIgnoreCase(tag_name) && "SecurityDataInfo".equalsIgnoreCase(parent_tag))
				{
					String ServiceId ="";
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					ServiceId = Long.toString(timestamp.getTime());
					String xml_str = "<"+tag_name+">"+ServiceId.substring(1,ServiceId.length())
					+"</"+ tag_name+">";

					//		RLOS.mLogger.info(" after adding res_flag+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
				}
			}
		}
		catch(Exception e){
			//RLOS.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + ");
			printException(e);
		}

		return int_xml;
	}
	/*  Function Header:

	 **********************************************************************************

		 NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified   : 6/08/2017  
		Author  : Disha  
		Description : Function to Generate  XML for MQ CONNECTION RESPONSE

	 ***********************************************************************************  */
	public String MQ_connection_response(StringBuilder finalXml) {

		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		Socket socket = null;
		OutputStream out = null;
		InputStream socketInputStream = null;
		DataOutputStream dout = null;
		DataInputStream din = null;
		String mqOutputResponse = null;
		String mqInputRequest = null;
		String cabinetName = null;
		String wi_name = null;
		String ws_name = null;
		String sessionID = null;
		String userName = null;
		String socketServerIP;
		int socketServerPort;
		try {


			cabinetName = FormContext.getCurrentInstance().getFormConfig().getConfigElement("EngineName");
			//	RLOS.mLogger.info("$$outputgGridtXML "+"cabinetName " + cabinetName);
			wi_name = FormContext.getCurrentInstance().getFormConfig().getConfigElement("ProcessInstanceId");
			ws_name = FormContext.getCurrentInstance().getFormConfig().getConfigElement("ActivityName");
			//	RLOS.mLogger.info("$$outputgGridtXML "+"ActivityName " + ws_name);
			sessionID = FormContext.getCurrentInstance().getFormConfig().getConfigElement("DMSSessionId");
			userName = FormContext.getCurrentInstance().getFormConfig().getConfigElement("UserName");
			//		RLOS.mLogger.info("$$outputgGridtXML "+ "userName "+ userName);
			//		RLOS.mLogger.info("$$outputgGridtXML "+ "sessionID "+ sessionID);

			String sMQuery = "SELECT SocketServerIP,SocketServerPort FROM NG_RLOS_MQ_TABLE with (nolock)";
			List<List<String>> outputMQXML = formObject.getDataFromDataSource(sMQuery);
			RLOS.mLogger.info("before Mq query response "+outputMQXML);
			if (!outputMQXML.isEmpty()) {
				mqInputRequest = getMQInputXML(sessionID, cabinetName,wi_name, ws_name, userName, finalXml);
				socketServerIP = outputMQXML.get(0).get(0);
				socketServerPort = 5555;
				if (!("".equalsIgnoreCase(socketServerIP)  && socketServerPort==0)) {
					socket = new Socket(socketServerIP, socketServerPort);
					int connection_timeout=60;
					try{
						connection_timeout = Integer.parseInt(NGFUserResourceMgr_RLOS.getGlobalVar("Integration_Connection_Timeout"));
					}
					catch(Exception e){
						connection_timeout=60;
					}

					socket.setSoTimeout(connection_timeout*1000);
					out = socket.getOutputStream();
					socketInputStream = socket.getInputStream();
					dout = new DataOutputStream(out);
					din = new DataInputStream(socketInputStream);
					mqOutputResponse = "";
					if (mqInputRequest != null && mqInputRequest.length() > 0) {
						int outPut_len = mqInputRequest.getBytes("UTF-16LE").length;
						mqInputRequest = outPut_len + "##8##;" + mqInputRequest;
						dout.write(mqInputRequest.getBytes("UTF-16LE"));
						dout.flush();
					}
					byte[] readBuffer = new byte[500];
					int num = din.read(readBuffer);
					if (num > 0) {

						byte[] arrayBytes = new byte[num];
						System.arraycopy(readBuffer, 0, arrayBytes, 0, num);
						mqOutputResponse = mqOutputResponse+ new String(arrayBytes, "UTF-16LE");
						//RLOS.mLogger.info("mqOutputResponse/message ID :  "+mqOutputResponse);
						if(!"".equalsIgnoreCase(mqOutputResponse)){
							mqOutputResponse = getOutWtthMessageID(mqOutputResponse);
						}
						if(mqOutputResponse.contains("&lt;")){
							mqOutputResponse=mqOutputResponse.replaceAll("&lt;", "<");
							mqOutputResponse=mqOutputResponse.replaceAll("&gt;", ">");
						}
					}
					socket.close();
					return mqOutputResponse;

				} else {
					return "MQ details not maintained";
				}
			} else {
				//		RLOS.mLogger.info("SOcket details are not maintained in NG_RLOS_MQ_TABLE table"+"");
				return "MQ details not maintained";
			}


		}
		catch(SocketTimeoutException e){
			RLOS.mLogger.info("Exception occurred SocketTimeoutException: "+e.getMessage());
			//e.printStackTrace();
			//RLOS.mLogger.info(e.getStackTrace());
			return "";
		}
		catch (Exception e) {
			RLOS.mLogger.info("Exception occurred while closing socket: "+e.getMessage());
			return "";
		}
		finally{
			try{
				if(out != null){
					//RLOS.mLogger.info("Inside out=null out.close");
					out.close();
					out=null;
				}
				if(socketInputStream != null){

					socketInputStream.close();
					socketInputStream=null;
				}
				if(dout != null){

					dout.close();
					dout=null;
				}
				if(din != null){

					din.close();
					din=null;
				}
				if(socket != null){
					socket.close();
					socket=null;
				}
			}catch(Exception e)
			{
				//		RLOS.mLogger.info("Exception occurred while closing socket");
				printException(e);
			}
		}
	}

	public String getOutWtthMessageID(String message_ID){
		String outputxml="";
		//RLOS.mLogger.info("Inside getOutWtthMessageID for  "+message_ID);
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String wi_name = formObject.getWFWorkitemName();
			String str_query = "select OUTPUT_XML from NG_RLOS_XMLLOG_HISTORY with (nolock) where MESSAGE_ID ='"+message_ID+"' and WI_NAME = '"+wi_name+"'";
			//RLOS.mLogger.info("Inside getOutWtthMessageID for str_query: "+str_query);
			List<List<String>> result=formObject.getDataFromDataSource(str_query);
			//RLOS.mLogger.info("out put for message_ID "+ message_ID+ " : "+result.toString());
			//below code added by nikhil 18/10 for Connection timeout
			String Integration_timeOut=NGFUserResourceMgr_RLOS.getGlobalVar("Inegration_Wait_Count");
			int Loop_wait_count=10;
			try
			{
				Loop_wait_count=Integer.parseInt(Integration_timeOut);
			}
			catch(Exception ex)
			{
				Loop_wait_count=10;
			}
			for(int Loop_count=0;Loop_count<Loop_wait_count;Loop_count++){
				if(result.size()>0){
					outputxml = result.get(0).get(0);
					//RLOS.mLogger.info("out put for message_ID "+ message_ID+ " : "+result.toString());
					break;
				}
				else{
					Thread.sleep(1000);
					result=formObject.getDataFromDataSource(str_query);
				}
			}

			if("".equalsIgnoreCase(outputxml)){
				outputxml="Error";
			}

		}
		catch(Exception e){
			//RLOS.mLogger.info("Exception occurred in getOutWtthMessageID" + e.getMessage());
			//RLOS.mLogger.info("Exception occurred in getOutWtthMessageID" + e.getStackTrace());
			outputxml="Error";
		}
		return outputxml;
	}

	public  String Clean_Xml(String InputXml,String Call_name){
		String Output_Xml="";
		try{
			Document doc = getDocument(InputXml);
			removeEmptyNodes(doc, Call_name);
			DOMSource domSource = new DOMSource(doc);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
			Output_Xml = writer.toString().substring(38);
		}
		catch(Exception e){

		}
		return Output_Xml;
	}
	public  void removeEmptyNodes(Node node,String Call_name) {
		NodeList list = node.getChildNodes();
		List<Node> nodesToRecursivelyCall = new LinkedList<Node>();
		for (int i = 0; i < list.getLength(); i++) {
			nodesToRecursivelyCall.add(list.item(i));
		}
		for(Node tempNode : nodesToRecursivelyCall) {
			removeEmptyNodes(tempNode, Call_name);
		}
		boolean emptyElement = node.getNodeType() == Node.ELEMENT_NODE && node.getChildNodes().getLength() == 0;
		boolean emptyText = node.getNodeType() == Node.TEXT_NODE && node.getNodeValue().trim().isEmpty();
		boolean selectText = node.getNodeType() == Node.TEXT_NODE && (node.getNodeValue().trim().equalsIgnoreCase("--Select--")||node.getNodeValue().trim().equalsIgnoreCase("null"));
		//Changes done to incorporate blank tag removal for all calls Deepak 24june 2018
		if(emptyElement || emptyText || selectText) {
			if(!node.hasAttributes()) {
				node.getParentNode().removeChild(node);
			}
		}
		/*//changes done to remove empty tags in Dectech only.
		if ("DECTECH".equalsIgnoreCase(Call_name)){
			if(emptyElement || emptyText || selectText) {
				if(!node.hasAttributes()) {
					node.getParentNode().removeChild(node);
				}
			}
		}
		else if (emptyElement || selectText) {
			if(!node.hasAttributes()) {
				node.getParentNode().removeChild(node);
			}
		}*/
		//  return node;
	}
	public String getGuarantor_PersonDetails(FormReference formObject){
		try{
			String str="";
			Common_Utils common=new Common_Utils(RLOS.mLogger);
			//RLOS.mLogger.info("Inside  getGuarantor_PersonDetails()");
			String GuarantorGrid="cmplx_GuarantorDetails_cmplx_GuarantorGrid";
			String age=formObject.getNGValue(GuarantorGrid,0,returnGridColumnIndex(GuarantorGrid,NGFUserResourceMgr_RLOS.getGlobalVar("Guarantor_Age")));
			String MinorFlag="";
			if (Float.parseFloat(age)>21){
				MinorFlag="N";
			}
			else{
				MinorFlag="Y";
			}
			if(formObject.getNGValue("cmplx_DecisionHistory_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_DecisionHistory_MultipleApplicantsGrid"),0).equalsIgnoreCase("GUARANTOR") && formObject.getNGValue("cmplx_DecisionHistory_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_DecisionHistory_MultipleApplicantsGrid"), 2).equals(formObject.getNGValue(GuarantorGrid,0,returnGridColumnIndex(GuarantorGrid,NGFUserResourceMgr_RLOS.getGlobalVar("Guarantor_PassportNo")))))
			{
				str=str+"<PersonDetails><TitlePrefix>"+formObject.getNGValue(GuarantorGrid,0,returnGridColumnIndex(GuarantorGrid,NGFUserResourceMgr_RLOS.getGlobalVar("Guarantor_Title")))+"</TitlePrefix>";
				str=str+"<FirstName>"+formObject.getNGValue(GuarantorGrid,0,returnGridColumnIndex(GuarantorGrid,NGFUserResourceMgr_RLOS.getGlobalVar("Guarantor_FirstName")))+"</FirstName>";
				str=str+"<LastName>"+formObject.getNGValue(GuarantorGrid,0,returnGridColumnIndex(GuarantorGrid,NGFUserResourceMgr_RLOS.getGlobalVar("Guarantor_LastName")))+"</LastName>";
				str=str+"<ShortName>"+formObject.getNGValue(GuarantorGrid,0,returnGridColumnIndex(GuarantorGrid,NGFUserResourceMgr_RLOS.getGlobalVar("Guarantor_FirstName"))).substring(0,1)+formObject.getNGValue(GuarantorGrid,0,returnGridColumnIndex(GuarantorGrid,NGFUserResourceMgr_RLOS.getGlobalVar("Guarantor_LastName")))+"</ShortName>";
				str=str+"<Gender>"+formObject.getNGValue(GuarantorGrid,0,returnGridColumnIndex(GuarantorGrid,NGFUserResourceMgr_RLOS.getGlobalVar("Guarantor_Gender")))+"</Gender>";
				str=str+"<MotherMaidenName>"+formObject.getNGValue(GuarantorGrid,0,returnGridColumnIndex(GuarantorGrid,NGFUserResourceMgr_RLOS.getGlobalVar("Guarantor_MothersName")))+"</MotherMaidenName>";
				str=str+"<MinorFlag>"+MinorFlag+"</MinorFlag>";
				str=str+"<NonResidentFlag>N</NonResidentFlag>";
				str=str+"<ResCountry></ResCountry>";
				str=str+"<MaritalStatus>"+formObject.getNGValue(GuarantorGrid,0,returnGridColumnIndex(GuarantorGrid,NGFUserResourceMgr_RLOS.getGlobalVar("Guarantor_MaritalStatus")))+"</MaritalStatus>";
				str=str+"<Nationality>"+formObject.getNGValue(GuarantorGrid,0,returnGridColumnIndex(GuarantorGrid,NGFUserResourceMgr_RLOS.getGlobalVar("Guarantor_Nationality")))+"</Nationality>";
				str=str+"<DateOfBirth>"+common.Convert_dateFormat(formObject.getNGValue(GuarantorGrid,0,returnGridColumnIndex(GuarantorGrid,NGFUserResourceMgr_RLOS.getGlobalVar("Guarantor_DOB"))), "dd/MM/yyyy","yyyy-MM-dd")+"</DateOfBirth>";
				str=str+"</PersonDetails>";
			}
			//RLOS.mLogger.info("Inside  getGuarantor_PersonDetails()--->Final str is: "+str);
			return str;
		}
		catch(Exception e){
			//RLOS.mLogger.info("Inside  getGuarantor_PersonDetails()--exception occurred ");
			printException(e);
			return "";
		}
	}

	public String getGuarantor_DocDetails(FormReference formObject){
		try{
			String str="";//testing SVn
			//RLOS.mLogger.info("Inside  getGuarantor_DocDetails()");
			String GuarantorGrid="cmplx_GuarantorDetails_cmplx_GuarantorGrid";
			if(formObject.getNGValue("cmplx_DecisionHistory_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_DecisionHistory_MultipleApplicantsGrid"),0).equalsIgnoreCase("GUARANTOR") && formObject.getNGValue("cmplx_DecisionHistory_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_DecisionHistory_MultipleApplicantsGrid"), 2).equals(formObject.getNGValue(GuarantorGrid,0,returnGridColumnIndex(GuarantorGrid,NGFUserResourceMgr_RLOS.getGlobalVar("Guarantor_PassportNo")))))
			{
				if(!"".equals(formObject.getNGValue(GuarantorGrid,0,returnGridColumnIndex(GuarantorGrid,NGFUserResourceMgr_RLOS.getGlobalVar("Guarantor_PassportNo"))))){
					str=str+generateDocDetailsTag(Arrays.asList("PPT",formObject.getNGValue(GuarantorGrid,0,returnGridColumnIndex(GuarantorGrid,NGFUserResourceMgr_RLOS.getGlobalVar("Guarantor_PassportIssue"))),formObject.getNGValue(GuarantorGrid,0,returnGridColumnIndex(GuarantorGrid,NGFUserResourceMgr_RLOS.getGlobalVar("Guarantor_passpExpiry"))),formObject.getNGValue(GuarantorGrid,0,returnGridColumnIndex(GuarantorGrid,NGFUserResourceMgr_RLOS.getGlobalVar("Guarantor_Nationality"))),formObject.getNGValue(GuarantorGrid,0,returnGridColumnIndex(GuarantorGrid,NGFUserResourceMgr_RLOS.getGlobalVar("Guarantor_PassportNo")))));
				}
				if(!"".equals(formObject.getNGValue(GuarantorGrid,0,returnGridColumnIndex(GuarantorGrid,NGFUserResourceMgr_RLOS.getGlobalVar("Guarantor_visaNo"))))){
					str=str+generateDocDetailsTag(Arrays.asList("VISA",formObject.getNGValue(GuarantorGrid,0,returnGridColumnIndex(GuarantorGrid,NGFUserResourceMgr_RLOS.getGlobalVar("Guarantor_VisaIssue"))),formObject.getNGValue(GuarantorGrid,0,returnGridColumnIndex(GuarantorGrid,NGFUserResourceMgr_RLOS.getGlobalVar("Guarantor_VisaExpiry"))),"AE",formObject.getNGValue(GuarantorGrid,0,returnGridColumnIndex(GuarantorGrid,NGFUserResourceMgr_RLOS.getGlobalVar("Guarantor_visaNo")))));
				}
				if(!"".equals(formObject.getNGValue(GuarantorGrid,0,returnGridColumnIndex(GuarantorGrid,NGFUserResourceMgr_RLOS.getGlobalVar("Guarantor_emiratesid"))))){
					str=str+generateDocDetailsTag(Arrays.asList("EMID",formObject.getNGValue(GuarantorGrid,0,returnGridColumnIndex(GuarantorGrid,NGFUserResourceMgr_RLOS.getGlobalVar("Guarantor_IDIssueDate"))),formObject.getNGValue(GuarantorGrid,0,returnGridColumnIndex(GuarantorGrid,NGFUserResourceMgr_RLOS.getGlobalVar("Guarantor_EIDExpiry"))),formObject.getNGValue(GuarantorGrid,0,returnGridColumnIndex(GuarantorGrid,NGFUserResourceMgr_RLOS.getGlobalVar("Guarantor_Nationality"))),formObject.getNGValue(GuarantorGrid,0,returnGridColumnIndex(GuarantorGrid,NGFUserResourceMgr_RLOS.getGlobalVar("Guarantor_emiratesid")))));
				}
			}

			return str;
		}
		catch(Exception e){
			//RLOS.mLogger.info("Inside  getGuarantor_DocDetails()--exception occurred!!!");
			printException(e);
			return "";
		}
	}


	public String generateDocDetailsTag(List<String> DocValues){
		Common_Utils common=new Common_Utils(RLOS.mLogger);
		return 
		"<DocDetails><DocType>"+DocValues.get(0)+"</DocType>"
		+"<DocCode>"+DocValues.get(0)+"</DocCode>"
		+"<DocIssueDate>"+	common.Convert_dateFormat(DocValues.get(1), "dd/MM/yyyy","yyyy-MM-dd")+"</DocIssueDate>"
		+"<DocExpiryDate>"+common.Convert_dateFormat(DocValues.get(2), "dd/MM/yyyy","yyyy-MM-dd")+"</DocExpiryDate>"
		+"<ParentDocCode>Retail</ParentDocCode>"
		+"<CountryOfIssue>"+DocValues.get(3)+"</CountryOfIssue>"
		+"<PlaceOfIssue>DXB</PlaceOfIssue>"
		+"<DocRefNum>"+DocValues.get(4)+"</DocRefNum></DocDetails>";
	}

}
