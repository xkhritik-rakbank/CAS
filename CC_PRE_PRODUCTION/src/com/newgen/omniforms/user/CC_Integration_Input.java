package com.newgen.omniforms.user;

import java.awt.print.Printable;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.newgen.custom.Common_Utils;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.Column;
import com.newgen.omniforms.component.ListView;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.util.Common;


public class CC_Integration_Input extends Common implements java.io.Serializable{
	/**cmplx_DEC_cmplx_gr_DeviaitonDetails 
	 * 
	 */
	private static final long serialVersionUID = 1L;
	CC_Common CC_Comn = new CC_Common();
	private String col_n = "call_type,Call_name,form_control,parent_tag_name,xmltag_name,is_repetitive,default_val,data_format";
	//Deepak 20 Dec PCSP-9 Card Notification & Card Creation call name added as they also need to be send in upper case.
	//Deepak 20 Dec PCSP-9 Card Notification & Card Creation call name removed again as they are getting failed.
	private String fin_call_name = "Customer_details, Customer_eligibility,new_customer_req,new_account_req,DEDUP_SUMMARY,CUSTOMER_UPDATE_REQ";
	private String capitalExceptionsTags = "EmploymentStatus,EmploymentType,AddressType";
//Designation_button7
	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : Generate XML           

	 ***********************************************************************************  */
	public String GenerateXML(String callName, String Operation_name) {

		CreditCard.mLogger.info("RLOSCommon"+ "Inside GenerateXML():");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		StringBuilder final_xml = new StringBuilder("");
		String header = "";
		String footer = "";
		String parentTagName = "";
 	String sQuery = null;

		//CreditCard.mLogger.info("$$outputgGridtXML "+ "before try");
		try {

			String sQuery_header = "SELECT Header,Footer,parenttagname FROM NG_Integration with (nolock) where Call_name='" + callName + "'";
			//CreditCard.mLogger.info("RLOSCommon"+ "sQuery" + sQuery_header);
			List<List<String>> OutputXML_header = formObject.getDataFromDataSource(sQuery_header);
			if (!OutputXML_header.isEmpty()) {
				//CreditCard.mLogger.info("RLOSCommon header: "+ OutputXML_header.get(0).get(0)+ " footer: "+ OutputXML_header.get(0).get(1)+ " parenttagname: " + OutputXML_header.get(0).get(2));
				header = OutputXML_header.get(0).get(0);
				footer = OutputXML_header.get(0).get(1);
				parentTagName = OutputXML_header.get(0).get(2);

				if (!("".equalsIgnoreCase(Operation_name) || Operation_name==null)) {
					CreditCard.mLogger.info("inside if of operation"+"operation: " + Operation_name+ "\n callName: "+ callName);
					sQuery = "SELECT "+ col_n+ " FROM NG_Integration_CC_field_Mapping with (nolock) where Call_name='"+ callName+ "' and active = 'Y' and Operation_name='"+ Operation_name + "' ORDER BY tag_seq ASC";

					} 
					else {
					CreditCard.mLogger.info("inside else of operation"+"operation" + Operation_name);
					sQuery = "SELECT "+ col_n+ " FROM NG_Integration_CC_field_Mapping with (nolock) where Call_name='"+ callName+ "' and active = 'Y' ORDER BY tag_seq ASC";
					}
				
					//CreditCard.mLogger.info("RLOSCommon"+ "sQuery " + sQuery);
					List<List<String>> DB_List=formObject.getDataFromDataSource(sQuery);//chnage to get data from DB directly
					//	CreditCard.mLogger.info("OutputXML"+ "OutputXML" + DB_List);
					
					if(!DB_List.isEmpty()){
					// CreditCard.mlogger.info("$$AKSHAY"+OutputXML.get(0).get(0)+OutputXML.get(0).get(1)+OutputXML.get(0).get(2)+OutputXML.get(0).get(3));
					/*CreditCard.mLogger.info("GenerateXML Integration field mapping table"+	DB_List.get(0).get(0) + DB_List.get(0).get(1)+ DB_List.get(0).get(2)+ DB_List.get(0).get(3)+ DB_List.get(0).get(4));
					CreditCard.mLogger.info("GenerateXML Integration field mapping table"+	DB_List.get(0).get(0) + DB_List.get(0).get(1)+ DB_List.get(0).get(2)+ DB_List.get(0).get(3));
					 */
					Map<String, String> int_xml = new LinkedHashMap<String, String>();
					
					if ("DEDUP_SUMMARY".equalsIgnoreCase(callName)) {
						int_xml = DEDUP_SUMMARY_Custom(DB_List,formObject,callName);
					} else if ("BLACKLIST_DETAILS".equalsIgnoreCase(callName)) {
						int_xml = Blacklist_Details_custom(DB_List,formObject,callName);
					}
					//added by akshay on 5/3/18 for drop 4- Placed here to cover single tag xml.
					else if("CIF_UNLOCK".equalsIgnoreCase(Operation_name) || "CIF_LOCK".equalsIgnoreCase(Operation_name) || "CIF_ENQUIRY".equalsIgnoreCase(Operation_name) || "CIF_verify".equalsIgnoreCase(Operation_name))
					{
						int_xml = CIFEnquiryLockUnlock(DB_List,formObject,callName);	
					}
					else if ("NEW_CUSTOMER_REQ".equalsIgnoreCase(callName)) {
						int_xml = NEW_CUSTOMER_Custom(DB_List,formObject,callName);
					} else if ("CUSTOMER_UPDATE_REQ".equalsIgnoreCase(callName)) {
						int_xml = CUSTOMER_UPDATE_Custom(DB_List,formObject,callName);
					} else if ("NEW_CARD_REQ".equalsIgnoreCase(callName)) {
						int_xml = NEW_CARD_Custom(DB_List,formObject,callName);
					} else if ("DECTECH".equalsIgnoreCase(callName)) {
						int_xml = DECTECH_Custom(DB_List,formObject,callName);
					} else if ("CHEQUE_BOOK_ELIGIBILITY".equalsIgnoreCase(callName)) {
						int_xml = CHEQUE_BOOK_ELIGIBILITY_Custom(DB_List,formObject,callName);
					} else if ("CARD_SERVICES_REQUEST".equalsIgnoreCase(callName)) {
						int_xml = CARD_SERVICES_REQUEST_Custom(DB_List,formObject,callName);
					}
					//additional parameter added by saurabh on 27th june
					else if ("CARD_NOTIFICATION".equalsIgnoreCase(callName)) {
						int_xml = CARD_NOTIFICATION_Custom(DB_List,formObject,callName,Operation_name);
					}
					else if ("NEW_CREDITCARD_REQ".equalsIgnoreCase(callName)) {
						int_xml = NEW_CREDITCARD_Custom(DB_List,formObject,callName);
					}
					
					
					else{
						int_xml = Non_Custom(DB_List,formObject,callName,Operation_name);
						//new method added for method in which nothing custom is required.
					}
					
					final_xml = final_xml.append("<").append(parentTagName).append(">");
					CreditCard.mLogger.info("RLOS"+ "Final XMLold--"+ final_xml);
					
					Iterator<Map.Entry<String, String>> itr = int_xml.entrySet().iterator();
					//CreditCard.mLogger.info("itr of hashmap"+ "itr" + itr);
					while (itr.hasNext()) {
						Map.Entry<String, String> entry = itr.next();
						CreditCard.mLogger.info("entry of hashmap"+ "entry"+ entry);
						if (final_xml.indexOf((entry.getKey())) > -1) {
							CreditCard.mLogger.info("RLOS"+ "itr_value: Key: "
									+ entry.getKey() + " Value: "
									+ entry.getValue());
							final_xml = final_xml.insert(final_xml.indexOf("<" + entry.getKey() + ">")
									+ entry.getKey().length() + 2, entry.getValue());
							CreditCard.mLogger.info("value of final xml"+"final_xml" + final_xml);
							itr.remove();
						}
					}
					final_xml = final_xml.append("</").append(parentTagName).append(">");
					//CreditCard.mLogger.info("CC_Common"+ "final_xml: "+ final_xml);
					final_xml = new StringBuilder( Clean_Xml(final_xml.toString(),callName));
					//CreditCard.mLogger.info("FInal XMLnew is: "+ final_xml.toString());
					final_xml.insert(0, header);
					final_xml.append(footer);
					//CreditCard.mLogger.info("FInal XMLnew with header: "+final_xml.toString());
					formObject.setNGValue("Is_" + callName, "Y");
					//CreditCard.mLogger.info("value of " + callName + " Flag: "+ formObject.getNGValue("Is_" + callName));
					Integration_fragName(callName);
					
					return MQ_connection_response(final_xml);
					
					//return dummyResponse(callName);
					
				} else {
					CreditCard.mLogger.info("Genrate XML: "+"Entry is not maintained in field mapping Master table for : "+ callName);
					return "Call not maintained";
				}

			} else {
				CreditCard.mLogger.info("Genrate XML: "+"Entry is not maintained in Master table for : "+ callName);
				return "Call not maintained";
			}
		}

		catch (Exception e) {
			CreditCard.mLogger.info("Exception ocurred: "+ e.getLocalizedMessage()+CC_Common.printException(e));
			CreditCard.mLogger.info("CC_Common"+ "$$final_xml: " + final_xml);
			CreditCard.mLogger.info("CC_Common"+
					"Exception occured in main thread: " + e.getMessage());
			return "0";
		}
	}

	public String dummyResponse(String callName )
	{
	//FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String response="";
		if(("CUSTOMER_DETAILS").equalsIgnoreCase(callName)){
			response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>CUSTOMER_DETAILS</MsgFormat><MsgVersion>0000</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153422713637417</MessageId><Extra1>REP||LAXMANRET.LAXMANRET</Extra1><Extra2>2018-08-14T10:12:17.561+04:00</Extra2></EE_EAI_HEADER><FetchCustomerDetailsRes><BankId>RAK</BankId><CIFDet><CIFID>2221180</CIFID><RetCorpFlag>R</RetCorpFlag><CifType>EB</CifType><CustomerStatus>ACTVE</CustomerStatus><CustomerSegment>PBD</CustomerSegment><CustomerSubSeg>PBN</CustomerSubSeg><AECBConsentHeld>Y</AECBConsentHeld><IsStaff>N</IsStaff><IsPremium>N</IsPremium><IsWMCustomer>N</IsWMCustomer><BlackListFlag>N</BlackListFlag><NegativeListOvlFlag>N</NegativeListOvlFlag><CreditGradeCode>P2</CreditGradeCode><PhnDet><PhnType>CELLPH1</PhnType><PhnPrefFlag>Y</PhnPrefFlag><PhnCountryCode>971</PhnCountryCode><PhnLocalCode>00971</PhnLocalCode><PhoneNo>97100971</PhoneNo></PhnDet><PhnDet><PhnType>HOMEPH1</PhnType><PhnPrefFlag>N</PhnPrefFlag><PhnCountryCode>971</PhnCountryCode><PhnLocalCode>00971</PhnLocalCode><PhoneNo>97100971</PhoneNo></PhnDet><PhnDet><PhnType>OFFCPH1</PhnType><PhnPrefFlag>N</PhnPrefFlag><PhnCountryCode>971</PhnCountryCode><PhnLocalCode>00971</PhnLocalCode><PhoneNo>97100971</PhoneNo></PhnDet><PhnDet><PhnType>OVHOMEPH</PhnType><PhnPrefFlag>N</PhnPrefFlag><PhnCountryCode>0091</PhnCountryCode><PhnLocalCode>982221180</PhnLocalCode><PhoneNo>0091982221180</PhoneNo></PhnDet><AddrDet><AddressType>Home</AddressType><EffectiveFrom>2014-06-27</EffectiveFrom><EffectiveTo>2018-06-27</EffectiveTo><HoldMailFlag>N</HoldMailFlag><ReturnFlag>N</ReturnFlag><AddrPrefFlag>N</AddrPrefFlag><AddressLine1>12345</AddressLine1><AddressLine2>PREMISE NAME FOR2221180</AddressLine2><AddressLine3>STREET NAME FOR   2221180</AddressLine3><AddressLine4>NA</AddressLine4><POBox>12345</POBox><State>UNZ</State><City>DXB</City><Country>IN</Country></AddrDet><AddrDet><AddressType>Mailing</AddressType><EffectiveFrom>2014-07-25</EffectiveFrom><EffectiveTo>2099-12-31</EffectiveTo><HoldMailFlag>N</HoldMailFlag><ReturnFlag>N</ReturnFlag><AddrPrefFlag>N</AddrPrefFlag><AddressLine1>12345</AddressLine1><AddressLine2>PREMISE NAME FOR2221180</AddressLine2><AddressLine3>STREET NAME FOR   2221180</AddressLine3><AddressLine4>NA</AddressLine4><POBox>12345</POBox><State>VIR</State><City>DXB</City><Country>AE</Country></AddrDet><AddrDet><AddressType>Swift</AddressType><EffectiveFrom>2016-05-30</EffectiveFrom><EffectiveTo>2099-12-31</EffectiveTo><AddrPrefFlag>N</AddrPrefFlag></AddrDet><AddrDet><AddressType>OFFICE</AddressType><EffectiveFrom>2014-08-13</EffectiveFrom><EffectiveTo>2099-12-31</EffectiveTo><HoldMailFlag>N</HoldMailFlag><ReturnFlag>N</ReturnFlag><AddrPrefFlag>Y</AddrPrefFlag><AddressLine1>12345</AddressLine1><AddressLine2>PREMISE NAME FOR2221180</AddressLine2><AddressLine3>STREET NAME FOR   2221180</AddressLine3><AddressLine4>NA</AddressLine4><POBox>12345</POBox><zipcode>30303</zipcode><State>VIR</State><City>DXB</City><Country>AE</Country></AddrDet><AddrDet><AddressType>RESIDENCE</AddressType><EffectiveFrom>2014-08-13</EffectiveFrom><EffectiveTo>2099-12-31</EffectiveTo><HoldMailFlag>N</HoldMailFlag><ReturnFlag>N</ReturnFlag><AddrPrefFlag>N</AddrPrefFlag><AddressLine1>12345</AddressLine1><AddressLine2>PREMISE NAME FOR   2221180</AddressLine2><AddressLine3>STREET NAME FOR2221180</AddressLine3><AddressLine4>LOCALITY NAME FOR2221180</AddressLine4><ResidenceType>R</ResidenceType><POBox>12345</POBox><State>JNE</State><City>DXB</City><Country>AE</Country></AddrDet><EmlDet><EmlType>ELML1</EmlType><EmlPrefFlag>N</EmlPrefFlag><Email>TEST11@RAKBANKTST.AE</Email></EmlDet><EmlDet><EmlType>ELML2</EmlType><EmlPrefFlag>N</EmlPrefFlag><Email>1234567891234567@RAKBANK.AE</Email></EmlDet><EmlDet><EmlType>HOMEEML</EmlType><EmlPrefFlag>Y</EmlPrefFlag><Email>1234567891234567@RAKBANK.AE</Email></EmlDet><EmlDet><EmlType>WORKEML</EmlType><EmlPrefFlag>N</EmlPrefFlag><Email>TEST11@RAKBANKTST.AE</Email></EmlDet><DocDet><DocType>PPT</DocType><DocTypeDesc>PASSPORT</DocTypeDesc><DocNo>AE2221180</DocNo><DocIssDate>2015-02-04</DocIssDate><DocExpDate>2025-12-31</DocExpDate></DocDet><DocDet><DocType>VISA</DocType><DocTypeDesc>VISA FILE NUMBER</DocTypeDesc><DocNo>20120142354787</DocNo><DocIssDate>2016-07-01</DocIssDate><DocExpDate>2020-07-16</DocExpDate></DocDet><DocDet><DocType>EMID</DocType><DocTypeDesc>EMIRATES ID</DocTypeDesc><DocNo>784198812221180</DocNo><DocIssDate>2016-07-01</DocIssDate><DocExpDate>2025-12-31</DocExpDate></DocDet><BuddyRMDetails><BuddyRMName>PERSONAL BANKER</BuddyRMName><BuddyRMPhone>8004048</BuddyRMPhone></BuddyRMDetails><BackupRMDetails></BackupRMDetails><RetAddnlDet><Title>MR.</Title><ShortName>F  L</ShortName><CustomerName>FIRST NAME FOR 2221180  LAST NAME FOR  2221180</CustomerName><FName>FIRST NAME FOR 2221180</FName><LName>LAST NAME FOR  2221180</LName><Gender>M</Gender><DOB>1988-01-01</DOB><MinorFlg>N</MinorFlg><MaritialStatus>S</MaritialStatus><MotherMaidenName>MOM</MotherMaidenName><Nationality>IN</Nationality><ResidentCountry>AE</ResidentCountry><CustType>EB</CustType><NoOfDepndant>3</NoOfDepndant><SalaryTranflag>N</SalaryTranflag><ResideSince>2014-07-24</ResideSince><CustomerNRIFlag>N</CustomerNRIFlag><EmpType>S</EmpType><EmployerCode>001</EmployerCode><EmployerName>MINISTRY OF DEFENCE</EmployerName><Department>ENGINEERING DEPT</Department><EmpStatus>2</EmpStatus><DOJ>2014-07-24</DOJ><Designation>03</Designation><CurrentJobPeriod>0</CurrentJobPeriod><GrossSalary>4E+4</GrossSalary><TotHouseholdInc>7.125E+3</TotHouseholdInc><InvIncome>0E+0</InvIncome><OthIncome>0E+0</OthIncome><Commissions>0E+0</Commissions><AssessedIncome>0E+0</AssessedIncome><HRA>0E+0</HRA><RentInc>0E+0</RentInc><MnthlyDispInc>7.125E+3</MnthlyDispInc><MnthlyHouseholdExp>0E+0</MnthlyHouseholdExp><AcctType>ODA</AcctType><AcctNum>8332221180901</AcctNum></RetAddnlDet><FatcaDet><USRelation>O</USRelation><TIN>NA</TIN><FatcaReason>NA</FatcaReason><DocumentsCollected>ID DOC!SELF-ATTEST FORM</DocumentsCollected></FatcaDet><KYCDet><KYCReviewDate>2020-12-31</KYCReviewDate><PEP>NPEP</PEP></KYCDet><OECDDet><CityOfBirth>BVT</CityOfBirth><CountryOfBirth>AD</CountryOfBirth><CRSUnDocFlg>Y</CRSUnDocFlg><CRSUndocFlgReason>CIF UPDATE WITHIN UAE</CRSUndocFlgReason><ReporCntryDet><CntryOfTaxRes>AE</CntryOfTaxRes><TINNumber>3433</TINNumber><MiscellaneousID>16986490</MiscellaneousID></ReporCntryDet><ReporCntryDet><CntryOfTaxRes>AE</CntryOfTaxRes><TINNumber>3433</TINNumber><NoTINReason>NA</NoTINReason><MiscellaneousID>16997075</MiscellaneousID></ReporCntryDet><ReporCntryDet><CntryOfTaxRes>AE</CntryOfTaxRes><TINNumber>234234</TINNumber><MiscellaneousID>16991110</MiscellaneousID></ReporCntryDet></OECDDet></CIFDet></FetchCustomerDetailsRes></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
	        
		}
		
		
		else if(("dectech").equalsIgnoreCase(callName)){
			response="<APMQPUTGET_Output><MQ_RESPONSE_XML>?<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Header><ServiceId>CallProcessManager</ServiceId><ServiceType>ProductEligibility</ServiceType><ServiceProviderId>DECTECH</ServiceProviderId><ServiceChannelId>CAS</ServiceChannelId><RequestID>CAS153302379542793</RequestID><TimeStampyyyymmddhhmmsss>2018-07-31T11:56:36.198</TimeStampyyyymmddhhmmsss><RequestLifeCycleStage>CallProcessManagerResponse</RequestLifeCycleStage><MessageStatus>Success</MessageStatus></soap:Header><soap:Body><CallProcessManagerResponse xmlns=\"http://tempuri.org/\"><CallProcessManagerResult><ProcessManagerResponse><Application><Channel>CC</Channel><CallType>PM</CallType><ApplicationNumber>CC-0030018313-process</ApplicationNumber><ReturnDateTime>20180731115636</ReturnDateTime><SystemErrorCode></SystemErrorCode><SystemErrorMessage></SystemErrorMessage><ReturnError RecordNumber=\"0\" /></Application><Instinct_Actions><Instinct_Action_Output_XML /></Instinct_Actions><PM_Results><Random_Number>437</Random_Number><DBR>0.00</DBR><Decision_Results name=\"MAC\"><PM_Decision_Results_Data><PM_Decision_Results>  <AppKey>CC-0030018313-processCC</AppKey>  <Decision_Objective>1</Decision_Objective>  <Decision_Sequence_Number>1</Decision_Sequence_Number>  <Last_Update_Date>2018-07-31T11:56:35.993+04:00</Last_Update_Date>  <Default_Decision>A</Default_Decision>  <Default_Reason>A999 - System Approve</Default_Reason>  <Default_Document>0</Default_Document>  <System_Decision>A</System_Decision>  <System_Document>0</System_Document>  <Decision_Node_Id>2.1</Decision_Node_Id>  <Decision_Test_Group>0</Decision_Test_Group></PM_Decision_Results></PM_Decision_Results_Data><PM_Reason_Codes_Data><PM_Reason_Codes>  <Decision_Objective>1</Decision_Objective>  <Decision_Sequence_Number>1</Decision_Sequence_Number>  <Sequence_Number>1</Sequence_Number>  <Reason_Decision>A</Reason_Decision>  <Reason_Code>A999</Reason_Code>  <Reason_Description>System Approve</Reason_Description>  <Criteria_Name />  <Letter_Code />  <Document />  <Letter_Reason /></PM_Reason_Codes></PM_Reason_Codes_Data></Decision_Results><Strategy_Results name=\"Eligibility\"><PM_Strategy_MaxLendingAmount_Data><PM_Strategy_MaxLendingAmount_Results>  <Policy_Number>1</Policy_Number>  <Adjustment_Number>0</Adjustment_Number>  <Max_Lending_Amount>100000.00</Max_Lending_Amount>  <Last_Update_Date>2018-07-31T11:56:36.04+04:00</Last_Update_Date>  <Entry_Name>Output TAI Multiplier 2 Max 100000</Entry_Name>  <Use_Requested_Limit_Flag>false</Use_Requested_Limit_Flag>  <Existing_Limit_Increase_Flag>false</Existing_Limit_Increase_Flag>  <Limit_Increase_Percent>0.00</Limit_Increase_Percent>  <Use_Characteristic_As_Limit_Flag>true</Use_Characteristic_As_Limit_Flag>  <Limit_Table_Id>1</Limit_Table_Id>  <Limit_Field_Number>22</Limit_Field_Number>  <Use_Characteristic_As_Limit_Multiplier>2.000</Use_Characteristic_As_Limit_Multiplier>  <Fixed_Amount_Flag>false</Fixed_Amount_Flag>  <Fixed_Amount>0</Fixed_Amount>  <Base_Percentage_Increase_Flag>false</Base_Percentage_Increase_Flag>  <Base_Percentage_Increase_Percent>0.00</Base_Percentage_Increase_Percent>  <Limit_Minimum>0</Limit_Minimum>  <Limit_Maximum>100000</Limit_Maximum>  <Limit_Rounding_Factor1>100</Limit_Rounding_Factor1>  <Limit_Rounding_Factor2>100</Limit_Rounding_Factor2>  <Limit_Rounding_Factor3>100</Limit_Rounding_Factor3>  <Limit_Rounding_Factor4>100</Limit_Rounding_Factor4>  <Limit_Rounding_Cutoff1>100000</Limit_Rounding_Cutoff1>  <Limit_Rounding_Cutoff2>200000</Limit_Rounding_Cutoff2>  <Limit_Rounding_Cutoff3>300000</Limit_Rounding_Cutoff3>  <Limit_Reason_Code />  <Max_Limit_Table_Id>0</Max_Limit_Table_Id>  <Max_Limit_Field_Number>0</Max_Limit_Field_Number>  <Max_Limit_Multiplier>0.00</Max_Limit_Multiplier>  <Use_Round_Up_Factor1_Flag>false</Use_Round_Up_Factor1_Flag>  <Use_Round_Up_Factor2_Flag>false</Use_Round_Up_Factor2_Flag>  <Use_Round_Up_Factor3_Flag>false</Use_Round_Up_Factor3_Flag>  <Use_Round_Up_Factor4_Flag>false</Use_Round_Up_Factor4_Flag>  <Strategy_Node_Id>1.2.1.3.1</Strategy_Node_Id>  <Strategy_Test_Group>0</Strategy_Test_Group>  <Tree_Error_Field_Label />  <Tree_Error_Field_Value /></PM_Strategy_MaxLendingAmount_Results></PM_Strategy_MaxLendingAmount_Data></Strategy_Results><Strategy_Results name=\"Interest Rate\"><PM_Strategy_PricingTerm_Data><PM_Strategy_PricingTerm_Results>  <Last_Update_Date>2018-07-31T11:56:36.057+04:00</Last_Update_Date>  <Entry_Name>Zero</Entry_Name>  <Interest_Rate>0.00</Interest_Rate>  <Max_Loan_Term>0</Max_Loan_Term>  <Strategy_Node_Id>2</Strategy_Node_Id>  <Strategy_Test_Group>0</Strategy_Test_Group></PM_Strategy_PricingTerm_Results></PM_Strategy_PricingTerm_Data></Strategy_Results><Scoring_Results name=\"Recalculation\"><PM_Scoring_Results_Data><PM_Scoring_Results>  <Scoring_Sequence_Number>1</Scoring_Sequence_Number>  <Scoring_Objective>2</Scoring_Objective>  <Last_Update_Date>2018-07-31T11:56:36.057+04:00</Last_Update_Date>  <Score>0.000000</Score>  <Score_Node_Id>1</Score_Node_Id>  <Scorecard_Id>Null Scorecard</Scorecard_Id>  <Grade>0</Grade>  <Score_Test_Group>0</Score_Test_Group></PM_Scoring_Results></PM_Scoring_Results_Data></Scoring_Results><Scoring_Results name=\"Application Score\"><PM_Scoring_Results_Data><PM_Scoring_Results>  <Scoring_Sequence_Number>1</Scoring_Sequence_Number>  <Scoring_Objective>1</Scoring_Objective>  <Last_Update_Date>2018-07-31T11:56:36.087+04:00</Last_Update_Date>  <Score>540.000000</Score>  <Score_Node_Id>2.1</Score_Node_Id>  <Scorecard_Id>CC App Scorecard</Scorecard_Id>  <Grade>0</Grade>  <Score_Test_Group>0</Score_Test_Group></PM_Scoring_Results></PM_Scoring_Results_Data></Scoring_Results><Decision_Results name=\"Eligibility Decision\"><PM_Decision_Results_Data><PM_Decision_Results>  <AppKey>CC-0030018313-processCC</AppKey>  <Decision_Objective>2</Decision_Objective>  <Decision_Sequence_Number>1</Decision_Sequence_Number>  <Last_Update_Date>2018-07-31T11:56:36.15+04:00</Last_Update_Date>  <Default_Decision>A</Default_Decision>  <Default_Reason>A999 - System Approve</Default_Reason>  <Default_Document>0</Default_Document>  <System_Decision>A</System_Decision>  <System_Document>0</System_Document>  <Decision_Node_Id>2.1</Decision_Node_Id>  <Decision_Test_Group>0</Decision_Test_Group></PM_Decision_Results></PM_Decision_Results_Data><PM_Reason_Codes_Data><PM_Reason_Codes>  <Decision_Objective>2</Decision_Objective>  <Decision_Sequence_Number>1</Decision_Sequence_Number>  <Sequence_Number>1</Sequence_Number>  <Reason_Decision>A</Reason_Decision>  <Reason_Code>A999</Reason_Code>  <Reason_Description>System Approve</Reason_Description>  <Criteria_Name />  <Letter_Code />  <Document />  <Letter_Reason /></PM_Reason_Codes></PM_Reason_Codes_Data></Decision_Results><Strategy_Results name=\"Additional Eligible Cards\"><PM_Strategy_Verification_Data><PM_Strategy_Verification_Results>  <Last_Update_Date>2018-07-31T11:56:36.18+04:00</Last_Update_Date>  <Entry_Name>Eligible Cards UAE</Entry_Name>  <Number_Of_Visits>0</Number_Of_Visits><Visits><Code>4</Code><Type_Of_Visit>KALYAN-UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>9</Code><Type_Of_Visit>MPL-UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>14</Code><Type_Of_Visit>MRBH GOLD UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>19</Code><Type_Of_Visit>MRBH PLTM UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>26</Code><Type_Of_Visit>MRBH WORLD UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>31</Code><Type_Of_Visit>MY RAK CARD-UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>36</Code><Type_Of_Visit>MY RAK ISLAMIC-UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>40</Code><Type_Of_Visit>REDCARD-UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>51</Code><Type_Of_Visit>TITANIUM-UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>55</Code><Type_Of_Visit>VCLASSIC-UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>60</Code><Type_Of_Visit>VGOLD-UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits><Visits><Code>65</Code><Type_Of_Visit>WORLD-UAE</Type_Of_Visit><Mandatory_Flag>False</Mandatory_Flag></Visits>  <Number_Of_Calls>0</Number_Of_Calls><Calls><Code></Code><Type_Of_Call></Type_Of_Call><Mandatory_Flag></Mandatory_Flag></Calls>  <Strategy_Node_Id>2.1.2.2.4.1</Strategy_Node_Id>  <Strategy_Test_Group>0</Strategy_Test_Group></PM_Strategy_Verification_Results></PM_Strategy_Verification_Data></Strategy_Results></PM_Results><PM_Outputs><Application><Output_Accommodation_Allowance>0.00</Output_Accommodation_Allowance><Output_Additional_Amount>36400.00</Output_Additional_Amount><Output_Affordable_EMI>43839.00</Output_Affordable_EMI><Output_CPV_Waiver>N</Output_CPV_Waiver><Output_Decision>A</Output_Decision><Output_Delegation_Authority>SR. MGR</Output_Delegation_Authority><Output_Eligible_Amount>100000.00</Output_Eligible_Amount><Output_Eligible_Amount_Path>Max Lending Amount Sub Process Type Segmentation CC [SAL]\\NOT Employee type SP or not confirmed in job with LOS LT 6\\LOS Segmentation [&amp;gt;= 24]\\No Months AECB History [0]\\Output TAI Segmentation [&amp;gt;= 35000]</Output_Eligible_Amount_Path><Output_Eligible_Cards>[{\"Credit_Limit\":48600.00,\"Flag\":\"E\"},{\"Card_Product\":\"MY RAK CARD-UAE\",\"Credit_Limit\":85000.00,\"Flag\":\"N\"}]</Output_Eligible_Cards><Output_Existing_DBR>3.6194</Output_Existing_DBR><Output_Final_Amount>85000.00</Output_Final_Amount><Output_Final_DBR>4.3896</Output_Final_DBR><Output_Interest_Rate>0.00</Output_Interest_Rate><Output_Net_Salary_DBR>6.3831</Output_Net_Salary_DBR><Output_Num_Eligible_Cards>8</Output_Num_Eligible_Cards><Output_Salary_Multiples>0.90</Output_Salary_Multiples><Output_TAI>94520.00</Output_TAI></Application></PM_Outputs></ProcessManagerResponse></CallProcessManagerResult></CallProcessManagerResponse></soap:Body></soap:Envelope></MQ_RESPONSE_XML></APMQPUTGET_Output>";
			//commented by deepak as the same is of no use.
			/*String test=formObject.getNGValue("FrameName");
			String previous_frame="EligibilityAndProductInformation,Internal_External_Liability";
*/
			
		}
		else if(("FINANCIAL_SUMMARY").equalsIgnoreCase(callName)){
			response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>FINANCIAL_SUMMARY</MsgFormat><MsgVersion>0000</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153415827022195</MessageId><Extra1>REP||LAXMANRET.LAXMANRET</Extra1><Extra2>2018-08-13T03:04:30.812+04:00</Extra2></EE_EAI_HEADER><FinancialSummaryRes><BankId>RAK</BankId><CIFID>0103147</CIFID><AcctId>0014103147002</AcctId><OperationType>AVGBALDET</OperationType><OperationDesc>AVERAGE BALANCE</OperationDesc><TxnSummary></TxnSummary><AvgBalanceDtls><Month>JUL</Month><AvgBalance>3163.93</AvgBalance></AvgBalanceDtls><AvgBalanceDtls><Month>JUN</Month><AvgBalance>3632.73</AvgBalance></AvgBalanceDtls><AvgBalanceDtls><Month>MAY</Month><AvgBalance>1374.64</AvgBalance></AvgBalanceDtls><AvgBalanceDtls><Month>APR</Month><AvgBalance>610.43</AvgBalance></AvgBalanceDtls><AvgBalanceDtls><Month>MAR</Month><AvgBalance>1183.5</AvgBalance></AvgBalanceDtls><AvgBalanceDtls><Month>FEB</Month><AvgBalance>1047.47</AvgBalance></AvgBalanceDtls></FinancialSummaryRes></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(("CUSTOMER_ELIGIBILITY").equalsIgnoreCase(callName)){
			response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>CUSTOMER_ELIGIBILITY</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153382588661616</MessageId><Extra1>REP||BPM.123</Extra1><Extra2>2018-08-09T06:44:51.296+04:00</Extra2></EE_EAI_HEADER><CustomerEligibilityResponse><BankId>RAK</BankId><CustomerDetails><SearchType>Internal</SearchType><CustId>2221180</CustId><PassportNum>AE2221180</PassportNum><BlacklistFlag>Y</BlacklistFlag><DuplicationFlag>N</DuplicationFlag><NegatedFlag>N</NegatedFlag><Products><ProductType>ACCOUNT ONLY</ProductType><NoOfProducts>1</NoOfProducts></Products><Products><ProductType>CAPS</ProductType><NoOfProducts>1</NoOfProducts></Products></CustomerDetails></CustomerEligibilityResponse></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(("CUSTOMER_EXPOSURE").equalsIgnoreCase(callName)){
			response="<MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>CUSTOMER_EXPOSURE</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153416576411240</MessageId><Extra1>REP||SHELL.JOHN</Extra1><Extra2>2018-08-13T05:09:25.164+04:00</Extra2></EE_EAI_HEADER><CustomerExposureResponse><RequestType>InternalExposure</RequestType><IsDirect>Y</IsDirect><CustInfo><CustId><CustIdType>CIF Id</CustIdType><CustIdValue>0232626</CustIdValue></CustId><FullNm></FullNm><BirthDt>1973-06-29</BirthDt><Nationality>INDIAN</Nationality><CustSegment>PERSONAL BANKING</CustSegment><CustSubSegment>PB - NORMAL</CustSubSegment><RMName>PERSONAL BANKER</RMName><CreditGrade>P2 - PERSONAL - ACCEPTABLE CREDIT</CreditGrade><ECRN>038275800</ECRN><BorrowingCustomer>N</BorrowingCustomer></CustInfo><ProductExposureDetails><LoanDetails><AgreementId>20424288</AgreementId><LoanStat>A</LoanStat><LoanType>PL</LoanType><LoanDesc></LoanDesc><TotalNoOfInstalments>48</TotalNoOfInstalments><KeyDt><KeyDtType>LoanApprovedDate</KeyDtType><KeyDtValue>2017-11-06</KeyDtValue></KeyDt><KeyDt><KeyDtType>LoanMaturityDate</KeyDtType><KeyDtValue>2021-11-15</KeyDtValue></KeyDt><CurCode>AED</CurCode><AmountDtls><AmtType>TotalLoanAmount</AmtType><Amt>241000</Amt></AmountDtls><AmountDtls><AmtType>TotalOutstandingAmt</AmtType><Amt>220687.84</Amt></AmountDtls><AmountDtls><AmtType>NextInstallmentAmt</AmtType><Amt>6018</Amt></AmountDtls></LoanDetails><CardDetails><CardEmbossNum>038275800</CardEmbossNum><CardStatus>NORM</CardStatus><CardType>TITANIUM-EXPAT</CardType><CustRoleType>Primary</CustRoleType><KeyDt><KeyDtType>ApplicationCreationDate</KeyDtType><KeyDtValue>2015-11-09</KeyDtValue></KeyDt><KeyDt><KeyDtType>ExpiryDate</KeyDtType><KeyDtValue>2025-01-31</KeyDtValue></KeyDt><CurCode></CurCode><AmountDtls><AmtType>CreditLimit</AmtType><Amt>39000</Amt></AmountDtls><AmountDtls><AmtType>OutstandingAmt</AmtType><Amt>-9256.23</Amt></AmountDtls><AmountDtls><AmtType>OverdueAmt</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>CurrMaxUtil</AmtType><Amt>23.73</Amt></AmountDtls></CardDetails><CardDetails><CardEmbossNum>038275801</CardEmbossNum><CardStatus>NORM</CardStatus><CardType>TITANIUM-EXPAT</CardType><CustRoleType>Secondary</CustRoleType><KeyDt><KeyDtType>ApplicationCreationDate</KeyDtType><KeyDtValue>2015-11-09</KeyDtValue></KeyDt><KeyDt><KeyDtType>ExpiryDate</KeyDtType><KeyDtValue>2020-01-31</KeyDtValue></KeyDt><CurCode></CurCode><AmountDtls><AmtType>CreditLimit</AmtType><Amt>2000</Amt></AmountDtls><AmountDtls><AmtType>OutstandingAmt</AmtType><Amt>0</Amt></AmountDtls><AmountDtls><AmtType>OverdueAmt</AmtType><Amt>0</Amt></AmountDtls></CardDetails><AcctDetails><AcctId>0002232626001</AcctId><IBANNumber>AE120400000002232626001</IBANNumber><AcctStat>ACTIVE</AcctStat><AcctCur>AED</AcctCur><AcctNm>GERLIE UAT RAMACHANDRAN</AcctNm><AcctType>CURRENT ACCOUNT</AcctType><AcctSegment>PBD</AcctSegment><AcctSubSegment>PBN</AcctSubSegment><CustRoleType>Main</CustRoleType><KeyDt><KeyDtType>AccountOpenDate</KeyDtType><KeyDtValue>2008-11-16</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitSactionDate</KeyDtType><KeyDtValue>2008-11-16</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitExpiryDate</KeyDtType><KeyDtValue>2012-06-08</KeyDtValue></KeyDt><KeyDt><KeyDtType>LimitStartDate</KeyDtType><KeyDtValue>2008-11-16</KeyDtValue></KeyDt><AmountDtls><AmtType>AvailableBalance</AmtType><Amt>847.30</Amt></AmountDtls><AmountDtls><AmtType>ClearBalanceAmount</AmtType><Amt>847.3</Amt></AmountDtls><AmountDtls><AmtType>LedgerBalance</AmtType><Amt>847.30</Amt></AmountDtls><AmountDtls><AmtType>EffectiveAvailableBalance</AmtType><Amt>847.30</Amt></AmountDtls><AmountDtls><AmtType>CumulativeDebitAmount</AmtType><Amt>1444110.31</Amt></AmountDtls><AmountDtls><AmtType>SanctionLimit</AmtType><Amt>0</Amt></AmountDtls><WriteoffStat>Y</WriteoffStat><WorstDelay24Months>P2</WorstDelay24Months><MonthsOnBook>117.00</MonthsOnBook><LastRepmtDt>AUG</LastRepmtDt><IsCurrent>Y</IsCurrent><ChargeOffFlag>N</ChargeOffFlag><DelinquencyInfo><BucketType>DaysPastDue</BucketType><BucketValue>0</BucketValue></DelinquencyInfo></AcctDetails></ProductExposureDetails></CustomerExposureResponse></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(("NEW_CUSTOMER_REQ").equalsIgnoreCase(callName)){
			response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>NEW_CUSTOMER_REQ</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RBDUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS15341561637020</MessageId><Extra1>REP||CAS.123</Extra1><Extra2>2018-08-13T02:29:26.540+04:00</Extra2></EE_EAI_HEADER><NewCustomerResponse><BankId>RAK</BankId><CIFId>2677277</CIFId><Desc>Retail Customer successfully created with CIFID 2677277</Desc><Entity>Retail Customer</Entity><Service>CIFRetailCustomerCreate</Service><Status>Success</Status></NewCustomerResponse></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";

			//commented by deepak as the same is of no use.
			/*			String test=formObject.getNGValue("FrameName");
			String previous_frame="DecisionHistoryContainer,CustomerDetails,";
*/
		
		}

		else if(("DEDUP_SUMMARY").equalsIgnoreCase(callName)){
			response="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>DEDUP_SUMMARY</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>TABUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153370821624852</MessageId><Extra1>REP||BPM.123</Extra1><Extra2>2018-08-08T10:03:39.184+04:00</Extra2></EE_EAI_HEADER><CustomerDuplicationListResponse><BankId>RAK</BankId><Customer><CIFID>2221180</CIFID><RetailCorpFlag>R</RetailCorpFlag><CustomerType>EB</CustomerType><EntityType>ALL</EntityType><StepsMatched>CIF ID</StepsMatched><PersonDetails><FirstName>FIRST NAME FOR 2221180</FirstName><LastName>LAST NAME FOR  2221180</LastName><ShortName>F  L</ShortName><FullName>FIRST NAME FOR 2221180  LAST NAME FOR  2221180</FullName><MaritalStatus>S</MaritalStatus><Nationality>IN</Nationality><DateOfBirth>1988-01-01</DateOfBirth></PersonDetails><Document><DocumentType>PPT</DocumentType><DocumentRefNumber>AE2221180</DocumentRefNumber></Document><Document><DocumentType>VISA</DocumentType><DocumentRefNumber>20120142354787</DocumentRefNumber></Document><Document><DocumentType>EMID</DocumentType><DocumentRefNumber>784198812221180</DocumentRefNumber></Document><ContactDetails><EmailAddress><MailIdType>ELML1</MailIdType><MailIdValue>TEST11@RAKBANKTST.AE</MailIdValue></EmailAddress><EmailAddress><MailIdType>ELML2</MailIdType><MailIdValue>1234567891234567@RAKBANK.AE</MailIdValue></EmailAddress><EmailAddress><MailIdType>HOMEEML</MailIdType><MailIdValue>1234567891234567@RAKBANK.AE</MailIdValue></EmailAddress><EmailAddress><MailIdType>WORKEML</MailIdType><MailIdValue>TEST11@RAKBANKTST.AE</MailIdValue></EmailAddress><PhoneFax><PhoneType>CELLPH1</PhoneType><PhoneValue>97100971</PhoneValue></PhoneFax><PhoneFax><PhoneType>HOMEPH1</PhoneType><PhoneValue>97100971</PhoneValue></PhoneFax><PhoneFax><PhoneType>OFFCPH1</PhoneType><PhoneValue>97100971</PhoneValue></PhoneFax><PhoneFax><PhoneType>OVHOMEPH</PhoneType><PhoneValue>0091982221180</PhoneValue></PhoneFax></ContactDetails><StatusInfo><StatusType>Blacklisted</StatusType><StatusFlag>N</StatusFlag></StatusInfo><StatusInfo><StatusType>Negativelisted</StatusType><StatusFlag>N</StatusFlag></StatusInfo><StatusInfo><StatusType>Suspended</StatusType><StatusFlag>N</StatusFlag></StatusInfo></Customer><Customer><CIFID>2221180</CIFID><RetailCorpFlag>R</RetailCorpFlag><CustomerType>EB</CustomerType><EntityType>ALL</EntityType><StepsMatched>NAME_PASSPORT_DOB,PASSPORT,VISA</StepsMatched><PersonDetails><FirstName>FIRST NAME FOR 2221180</FirstName><LastName>LAST NAME FOR  2221180</LastName><ShortName>F  L</ShortName><FullName>FIRST NAME FOR 2221180  LAST NAME FOR  2221180</FullName><MaritalStatus>S</MaritalStatus><Nationality>IN</Nationality><DateOfBirth>1988-01-01</DateOfBirth></PersonDetails><Document><DocumentType>PPT</DocumentType><DocumentRefNumber>AE2221180</DocumentRefNumber></Document><Document><DocumentType>VISA</DocumentType><DocumentRefNumber>20120142354787</DocumentRefNumber></Document><Document><DocumentType>EMID</DocumentType><DocumentRefNumber>784198812221180</DocumentRefNumber></Document><ContactDetails><EmailAddress><MailIdType>ELML1</MailIdType><MailIdValue>TEST11@RAKBANKTST.AE</MailIdValue></EmailAddress><EmailAddress><MailIdType>ELML2</MailIdType><MailIdValue>1234567891234567@RAKBANK.AE</MailIdValue></EmailAddress><EmailAddress><MailIdType>HOMEEML</MailIdType><MailIdValue>1234567891234567@RAKBANK.AE</MailIdValue></EmailAddress><EmailAddress><MailIdType>WORKEML</MailIdType><MailIdValue>TEST11@RAKBANKTST.AE</MailIdValue></EmailAddress><PhoneFax><PhoneType>CELLPH1</PhoneType><PhoneValue>97100971</PhoneValue></PhoneFax><PhoneFax><PhoneType>HOMEPH1</PhoneType><PhoneValue>97100971</PhoneValue></PhoneFax><PhoneFax><PhoneType>OFFCPH1</PhoneType><PhoneValue>97100971</PhoneValue></PhoneFax><PhoneFax><PhoneType>OVHOMEPH</PhoneType><PhoneValue>0091982221180</PhoneValue></PhoneFax></ContactDetails><StatusInfo><StatusType>Blacklisted</StatusType><StatusFlag>N</StatusFlag></StatusInfo><StatusInfo><StatusType>Negativelisted</StatusType><StatusFlag>N</StatusFlag></StatusInfo><StatusInfo><StatusType>Suspended</StatusType><StatusFlag>N</StatusFlag></StatusInfo></Customer></CustomerDuplicationListResponse></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
			
		
		
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
			response ="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>CARD_NOTIFICATION</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS154203934826971</MessageId><Extra1>REP||BPM.123</Extra1><Extra2>2018-11-12T08:15:49.057+04:00</Extra2></EE_EAI_HEADER></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		else if(("ENTITY_MAINTENANCE_REQ").equalsIgnoreCase(callName)){
			response ="<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>ENTITY_MAINTENANCE_REQ</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153286850527510</MessageId><Extra1>REP||RBD.123</Extra1><Extra2>2018-07-29T04:48:26.794+04:00</Extra2></EE_EAI_HEADER><UpdateCifAndAccStatusRes><OperDesc>AcctAct</OperDesc><SuccessFlag>S</SuccessFlag></UpdateCifAndAccStatusRes></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
	/*	commented by sagarika- condition already checked
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
/*commented by sagarika due to duplicacy
 else if(("DEDUP_SUMMARY").equalsIgnoreCase(callName)){
			response = "<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>DEDUP_SUMMARY</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>TABUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS153288278033261</MessageId><Extra1>REP||BPM.123</Extra1><Extra2>2018-07-29T08:46:49.468+04:00</Extra2></EE_EAI_HEADER><CustomerDuplicationListResponse><BankId>RAK</BankId><Customer><CIFID>2562401</CIFID><RetailCorpFlag>R</RetailCorpFlag><CustomerType>EB</CustomerType><EntityType>ALL</EntityType><StepsMatched>CIF ID</StepsMatched><PersonDetails><FirstName>FIRST NAME FOR  2562401</FirstName><LastName>LAST NAME FOR 2562401</LastName><ShortName>G U R</ShortName><FullName>FIRST NAME FOR  2562401  LAST NAME FOR 2562401</FullName><MaritalStatus>M</MaritalStatus><Nationality>IN</Nationality><DateOfBirth>1988-01-01</DateOfBirth></PersonDetails><Document><DocumentType>PPT</DocumentType><DocumentRefNumber>PK0002321</DocumentRefNumber></Document><Document><DocumentType>VISA</DocumentType><DocumentRefNumber>40120090004933</DocumentRefNumber></Document><Document><DocumentType>EMID</DocumentType><DocumentRefNumber>784199200002321</DocumentRefNumber></Document><ContactDetails><EmailAddress><MailIdType>ELML1</MailIdType><MailIdValue>1234567891234@RAKBANK.AE</MailIdValue></EmailAddress><EmailAddress><MailIdType>ELML2</MailIdType><MailIdValue>1234567891234@RAKBANK.AE</MailIdValue></EmailAddress><EmailAddress><MailIdType>HOMEEML</MailIdType><MailIdValue>1234567891234@RAKBANK.AE</MailIdValue></EmailAddress><EmailAddress><MailIdType>WORKEML</MailIdType><MailIdValue>1234567891234@RAKBANK.AE</MailIdValue></EmailAddress><PhoneFax><PhoneType>CELLPH1</PhoneType><PhoneValue>971522562401</PhoneValue></PhoneFax><PhoneFax><PhoneType>FAXO1</PhoneType><PhoneValue>00971672562401</PhoneValue></PhoneFax><PhoneFax><PhoneType>HOMEPH1</PhoneType><PhoneValue>971792562401</PhoneValue></PhoneFax><PhoneFax><PhoneType>OFFCPH1</PhoneType><PhoneValue>971672562401</PhoneValue></PhoneFax><PhoneFax><PhoneType>OVHOMEPH</PhoneType><PhoneValue>0091962562401</PhoneValue></PhoneFax></ContactDetails><StatusInfo><StatusType>Blacklisted</StatusType><StatusFlag>N</StatusFlag></StatusInfo><StatusInfo><StatusType>Negativelisted</StatusType><StatusFlag>N</StatusFlag></StatusInfo><StatusInfo><StatusType>Suspended</StatusType><StatusFlag>N</StatusFlag></StatusInfo></Customer><Customer><CIFID>2562401</CIFID><RetailCorpFlag>R</RetailCorpFlag><CustomerType>EB</CustomerType><EntityType>ALL</EntityType><StepsMatched>NAME,NAME_PASSPORT_DOB,PASSPORT,VISA</StepsMatched><PersonDetails><FirstName>FIRST NAME FOR  2562401</FirstName><LastName>LAST NAME FOR 2562401</LastName><ShortName>G U R</ShortName><FullName>FIRST NAME FOR  2562401  LAST NAME FOR 2562401</FullName><MaritalStatus>M</MaritalStatus><Nationality>IN</Nationality><DateOfBirth>1988-01-01</DateOfBirth></PersonDetails><Document><DocumentType>PPT</DocumentType><DocumentRefNumber>PK0002321</DocumentRefNumber></Document><Document><DocumentType>VISA</DocumentType><DocumentRefNumber>40120090004933</DocumentRefNumber></Document><Document><DocumentType>EMID</DocumentType><DocumentRefNumber>784199200002321</DocumentRefNumber></Document><ContactDetails><EmailAddress><MailIdType>ELML1</MailIdType><MailIdValue>1234567891234@RAKBANK.AE</MailIdValue></EmailAddress><EmailAddress><MailIdType>ELML2</MailIdType><MailIdValue>1234567891234@RAKBANK.AE</MailIdValue></EmailAddress><EmailAddress><MailIdType>HOMEEML</MailIdType><MailIdValue>1234567891234@RAKBANK.AE</MailIdValue></EmailAddress><EmailAddress><MailIdType>WORKEML</MailIdType><MailIdValue>1234567891234@RAKBANK.AE</MailIdValue></EmailAddress><PhoneFax><PhoneType>CELLPH1</PhoneType><PhoneValue>971522562401</PhoneValue></PhoneFax><PhoneFax><PhoneType>FAXO1</PhoneType><PhoneValue>00971672562401</PhoneValue></PhoneFax><PhoneFax><PhoneType>HOMEPH1</PhoneType><PhoneValue>971792562401</PhoneValue></PhoneFax><PhoneFax><PhoneType>OFFCPH1</PhoneType><PhoneValue>971672562401</PhoneValue></PhoneFax><PhoneFax><PhoneType>OVHOMEPH</PhoneType><PhoneValue>0091962562401</PhoneValue></PhoneFax></ContactDetails><StatusInfo><StatusType>Blacklisted</StatusType><StatusFlag>N</StatusFlag></StatusInfo><StatusInfo><StatusType>Negativelisted</StatusType><StatusFlag>N</StatusFlag></StatusInfo><StatusInfo><StatusType>Suspended</StatusType><StatusFlag>N</StatusFlag></StatusInfo></Customer></CustomerDuplicationListResponse></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
}*/
		else if(("NEW_LOAN_REQ").equalsIgnoreCase(callName)){
			response = "<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>NEW_LOAN_REQ</MsgFormat><MsgVersion>0</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS15328686886040</MessageId><Extra1>REP||SHELL.JOHN</Extra1><Extra2>2018-07-29T04:51:50.677+04:00</Extra2></EE_EAI_HEADER><ContractCreationRes><contractID>902CNP1182100003</contractID><StatusCode>H</StatusCode><Description>Contract Created Successfully</Description></ContractCreationRes></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}
		//Commented for sonar
		/*else if(("CUSTOMER_UPDATE_REQ").equalsIgnoreCase(callName)){
			response = "<APMQPUTGET_Output><MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>CUSTOMER_UPDATE_REQ</MsgFormat><MsgVersion>001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS15328684850094</MessageId><Extra1>REP||SHELL.dfgJOHN</Extra1><Extra2>2018-07-29T04:48:08.597+04:00</Extra2></EE_EAI_HEADER><CustomerDetailsUpdateRes><BankId>RAK</BankId><CIFId>2676881</CIFId><Status>S</Status><Message>SUCCESS</Message></CustomerDetailsUpdateRes></EE_EAI_MESSAGE></MQ_RESPONSE_XML></APMQPUTGET_Output>";
		}*/
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
        

		return response;        
	}
	
	public void Integration_fragName(String callName)
	{
		try{
			FormReference formObject=FormContext.getCurrentInstance().getFormReference();
			String Curr_framename=formObject.getNGValue("FrameName");
			String call_framename="";
			if(("CUSTOMER_DETAILS").equalsIgnoreCase(callName))
			{
				call_framename="CustomerDetails,EmploymentDetails,Address_Details_container,OECD,Alt_Contact_container,FATCA,KYC";
			}
			else if(("dectech").equalsIgnoreCase(callName)){
				call_framename="EligibilityAndProductInformation,Internal_External_Liability";
			}
			else if(("NEW_CUSTOMER_REQ").equalsIgnoreCase(callName)){
				call_framename="DecisionHistory,CustomerDetails";
			}
			else if(("DEDUP_SUMMARY").equalsIgnoreCase(callName)){
				call_framename="Part_Match,Finacle_CRM_CustomerInformation";
			}
			else if(("BLACKLIST_DETAILS").equalsIgnoreCase(callName)){
				call_framename="Part_Match,Finacle_CRM_CustomerInformation";
			}	
			else if("NEW_CUSTOMER_REQ".equalsIgnoreCase(callName) || "CUSTOMER_UPDATE_REQ".equalsIgnoreCase(callName) || "CIF_UNLOCK".equalsIgnoreCase(callName) || "CIF_LOCK".equalsIgnoreCase(callName) || "CIF_VERIFY".equalsIgnoreCase(callName) || "CIF_ENQUIRY".equalsIgnoreCase(callName)) {
				call_framename="DecisionHistory";
			}
			
			else if("CARD_NOTIFICATION".equalsIgnoreCase(callName) || "CARD_SERVICES_REQUEST".equalsIgnoreCase(callName) || "NEW_CREDITCARD_REQ".equalsIgnoreCase(callName)) {
				call_framename="CC_Creation,DecisionHistory";
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
			CreditCard.mLogger.info( "Exception occured in Integration_fragName: "+ e.getMessage());
		}
	}

	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : GMq Connection response           

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
			//CreditCard.mLogger.info("$$outputgGridtXML "+"cabinetName " + cabinetName);
			wi_name = FormContext.getCurrentInstance().getFormConfig().getConfigElement("ProcessInstanceId");
			ws_name = FormContext.getCurrentInstance().getFormConfig().getConfigElement("ActivityName");
			//CreditCard.mLogger.info("$$outputgGridtXML "+"ActivityName " + ws_name);
			sessionID = FormContext.getCurrentInstance().getFormConfig().getConfigElement("DMSSessionId");
			userName = FormContext.getCurrentInstance().getFormConfig().getConfigElement("UserName");
			//CreditCard.mLogger.info("$$outputgGridtXML "+ "userName "+ userName);
			//CreditCard.mLogger.info("$$outputgGridtXML "+ "sessionID "+ sessionID);

			String sMQuery = "SELECT SocketServerIP,SocketServerPort FROM NG_RLOS_MQ_TABLE with (nolock)";
			List<List<String>> outputMQXML = formObject.getDataFromDataSource(sMQuery);
			//CreditCard.mLogger.info("$$outputgGridtXML "+ "sMQuery " + sMQuery);
			if (!outputMQXML.isEmpty()) {
				//CreditCard.mLogger.info("$$outputgGridtXML "+ outputMQXML.get(0).get(0) + "," + outputMQXML.get(0).get(1));
				socketServerIP = outputMQXML.get(0).get(0);
				//CreditCard.mLogger.info("$$outputgGridtXML "+ "socketServerIP " + socketServerIP);
				socketServerPort = Integer.parseInt(outputMQXML.get(0).get(1));
				//CreditCard.mLogger.info("$$outputgGridtXML "+ "socketServerPort " + socketServerPort);
				if (!("".equalsIgnoreCase(socketServerIP) && socketServerIP == null && socketServerPort==0)) {
					socket = new Socket(socketServerIP, socketServerPort);
					//new Code added by Deepak to set connection timeout
					int connection_timeout=60;
						try{
							connection_timeout = Integer.parseInt(NGFUserResourceMgr_CreditCard.getGlobalVar("Integration_Connection_Timeout"));
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
					mqInputRequest = getMQInputXML(sessionID, cabinetName,wi_name, ws_name, userName, finalXml);
					//CreditCard.mLogger.info("$$outputgGridtXML "+"mqInputRequest " + mqInputRequest);

					if (mqInputRequest != null && mqInputRequest.length() > 0) {
						int outPut_len = mqInputRequest.getBytes("UTF-16LE").length;
						//CreditCard.mLogger.info("Final XML output len: "+outPut_len + "");
						mqInputRequest = outPut_len + "##8##;" + mqInputRequest;
						//CreditCard.mLogger.info("MqInputRequest"+"Input Request Bytes : "+ mqInputRequest.getBytes("UTF-16LE"));
						dout.write(mqInputRequest.getBytes("UTF-16LE"));dout.flush();
					}
					byte[] readBuffer = new byte[500];
					int num = din.read(readBuffer);
					if (num > 0) {

						byte[] arrayBytes = new byte[num];
						System.arraycopy(readBuffer, 0, arrayBytes, 0, num);
						mqOutputResponse = mqOutputResponse+ new String(arrayBytes, "UTF-16LE");
						CreditCard.mLogger.info("mqOutputResponse/message ID :  "+mqOutputResponse);
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
					CreditCard.mLogger.info("SocketServerIp and SocketServerPort is not maintained "+"");
					//CreditCard.mLogger.info("SocketServerIp is not maintained "+	socketServerIP);
					//CreditCard.mLogger.info(" SocketServerPort is not maintained "+	socketServerPort);
					return "MQ details not maintained";
				}
			} else {
				CreditCard.mLogger.info("SOcket details are not maintained in NG_RLOS_MQ_TABLE table"+"");
				return "MQ details not maintained";
			}
			
		} catch (Exception e) {
			CreditCard.mLogger.info("Exception Occured Mq_connection_CC"+e.getStackTrace());
			return "";
		}
		finally{
			try{
				if(out != null){
					
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
					if(!socket.isClosed()){
						socket.close();
					}
					socket=null;
				}
			}catch(Exception e)
			{
				//		RLOS.mLogger.info("Exception occurred while closing socket");
				CreditCard.mLogger.info("Exception Occured Mq_connection_CC"+e.getStackTrace());
				//printException(e);
			}
		}
	}
	
	public String getOutWtthMessageID(String message_ID){
		String outputxml="";
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String wi_name = formObject.getWFWorkitemName();
			String str_query = "select OUTPUT_XML from NG_CC_XMLLOG_HISTORY with (nolock) where MESSAGE_ID ='"+message_ID+"' and WI_NAME = '"+wi_name+"'";
			CreditCard.mLogger.info("inside getOutWtthMessageID str_query: "+ str_query);
			List<List<String>> result=formObject.getDataFromDataSource(str_query);
			//below code added by nikhil 18/10 for Connection timeout
			String Integration_timeOut=NGFUserResourceMgr_CreditCard.getGlobalVar("Inegration_Wait_Count");
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
			CreditCard.mLogger.info("getOutWtthMessageID" + outputxml);				
		}
		catch(Exception e){
			CreditCard.mLogger.info("Exception occurred in getOutWtthMessageID" + e.getMessage());
			CreditCard.mLogger.info("Exception occurred in getOutWtthMessageID" + e.getStackTrace());
			outputxml="Error";
		}
		return outputxml;
	}

	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : Internal Bureau Indiviual Product        

	 ***********************************************************************************  */
	public String InternalBureauIndividualProducts() {
		CreditCard.mLogger.info("RLOSCommon java file"+"inside InternalBureauIndividualProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//deepak 19 Aug 2019 query corrected based on PCAS-2634
		//String sQuery = "SELECT cifid,agreementid,loantype,loantype,custroletype,loan_start_date,loanmaturitydate,lastupdatedate ,totaloutstandingamt,totalloanamount,NextInstallmentAmt,paymentmode,totalnoofinstalments,remaininginstalments,totalloanamount,      overdueamt,nofdayspmtdelay,monthsonbook,currentlycurrentflg,currmaxutil,DPD_30_in_last_6_months,DPD_60_in_last_18_months,propertyvalue,loan_disbursal_date,marketingcode,DPD_30_in_last_3_months,DPD_30_in_last_6_months,DPD_30_in_last_9_months,DPD_30_in_last_12_months,DPD_30_in_last_18_months,DPD_30_in_last_24_months,DPD_60_in_last_3_months,DPD_60_in_last_6_months,DPD_60_in_last_9_months,DPD_60_in_last_12_months,DPD_60_in_last_18_months,DPD_60_in_last_24_months,DPD_90_in_last_3_months,DPD_90_in_last_6_months,DPD_90_in_last_9_months,DPD_90_in_last_12_months,DPD_90_in_last_18_months,DPD_90_in_last_24_months,DPD_120_in_last_3_months,DPD_120_in_last_6_months,DPD_120_in_last_9_months,DPD_120_in_last_12_months,DPD_120_in_last_18_months,DPD_120_in_last_24_months,DPD_150_in_last_3_months,DPD_150_in_last_6_months,DPD_150_in_last_9_months,DPD_150_in_last_12_months,DPD_150_in_last_18_months,DPD_150_in_last_24_months,DPD_180_in_last_3_months,DPD_180_in_last_6_months,DPD_180_in_last_9_months,DPD_180_in_last_12_months,DPD_180_in_last_24_months,'' as col1,isnull(Consider_For_Obligations,'true'),LoanStat,'LOANS',writeoffStat,writeoffstatdt,lastrepmtdt,limit_increase,PartSettlementDetails,SchemeCardProd as SchemeCardProduct,General_Status,Internal_WriteOff_Check,AmountPaidInLst6Mnths,InterestRate FROM ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) WHERE Child_wi = '"+formObject.getWFWorkitemName()+ "' and LoanStat  not in ('Pipeline','CAS-Pipeline') union select CifId,CardEmbossNum,CardType,CardType,CustRoleType,'' as col6,'' as col7,'' as col8,OutstandingAmt,CreditLimit,case when CardType like '%LOC%' then (select monthlyamount from ng_RLOS_CUSTEXPOSE_CardInstallmentDetails with (nolock) where child_wi ='"+formObject.getWFWorkitemName()+"' and CardCRNNumber=CardEmbossNum) else PaymentsAmount end,PaymentMode,'' as col13,case when SchemeCardProd like 'LOC%' then (select top 1 ISNULL((CAST(INSTALMENTpERIOD AS INT)-CAST(rEMAININGemi AS INT)),'') from ng_RLOS_CUSTEXPOSE_CardInstallmentDetails with (nolock) where child_wi = '"+formObject.getWFWorkitemName()+"') else ''end  as col14,CashLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,currentlycurrentflg,CurrMaxUtil,DPD_30_in_last_6_months,DPD_60_in_last_18_months,'' as col23,'' as col24,'' as col25,DPD_30_in_last_3_months,DPD_30_in_last_6_months,DPD_30_in_last_9_months,DPD_30_in_last_12_months,DPD_30_in_last_18_months,DPD_30_in_last_24_months,DPD_60_in_last_3_months,DPD_60_in_last_6_months,DPD_60_in_last_9_months,DPD_60_in_last_12_months,DPD_60_in_last_18_months,DPD_60_in_last_24_months,DPD_90_in_last_3_months,DPD_90_in_last_6_months,DPD_90_in_last_9_months,DPD_90_in_last_12_months,DPD_90_in_last_18_months,DPD_90_in_last_24_months,DPD_120_in_last_3_months,DPD_120_in_last_6_months,DPD_120_in_last_9_months,DPD_120_in_last_12_months,DPD_120_in_last_18_months,DPD_120_in_last_24_months,DPD_150_in_last_3_months,DPD_150_in_last_6_months,DPD_150_in_last_9_months,DPD_150_in_last_12_months,DPD_150_in_last_18_months,DPD_150_in_last_24_months,DPD_180_in_last_3_months,DPD_180_in_last_6_months,DPD_180_in_last_9_months,DPD_180_in_last_12_months,DPD_180_in_last_24_months,ExpiryDate,isnull(Consider_For_Obligations,'true'),CardStatus,'CARDS',writeoffStat,writeoffstatdt,'' as lastrepdate,limit_increase,'' as PartSettlementDetails,SchemeCardProd,General_Status,Internal_WriteOff_Check,AmountPaidInLst6Mnths,InterestRate FROM ng_RLOS_CUSTEXPOSE_CardDetails with (nolock) where      Child_wi = '"+ formObject.getWFWorkitemName()+ "' and Request_Type In ('InternalExposure','CollectionsSummary') union select CifId,AcctId,'OverDraft' as loantype,'OverDraft' as loantype,CustRoleType,LimitSactionDate as loan_start_date,LimitExpiryDate as loanmaturitydate,'' as lastupdateddate,ClearBalanceAmount,SanctionLimit,'','','','',SanctionLimit,OverdueAmount,DaysPastDue,MonthsOnBook,IsCurrent,CurUtilRate,DPD30Last6Months,DPD60Last18Months,'',AccountOpenDate,'','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','',LimitExpiryDate,isNull(Consider_For_Obligations,'true'),AcctStat,'OVERDRAFT',WriteoffStat,WriteoffStatDt,LastRepmtDt,'','','','','','',''  from ng_rlos_custexpose_acctdetails with (nolock) where Child_wi = '"+formObject.getWFWorkitemName()+"'  and ODType != ''";
		//Deepak 15Sept 2019 for PCAS-2788
		String sQuery = "SELECT cifid,agreementid,loantype,loantype,custroletype,loan_start_date,loanmaturitydate,lastupdatedate ,totaloutstandingamt,totalloanamount,NextInstallmentAmt,paymentmode,totalnoofinstalments,remaininginstalments,totalloanamount, overdueamt,nofdayspmtdelay,monthsonbook,currentlycurrentflg,currmaxutil,DPD_30_in_last_6_months,DPD_60_in_last_18_months,propertyvalue,loan_disbursal_date,marketingcode,DPD_30_in_last_3_months,DPD_30_in_last_6_months,DPD_30_in_last_9_months,DPD_30_in_last_12_months,DPD_30_in_last_18_months,DPD_30_in_last_24_months,DPD_60_in_last_3_months,DPD_60_in_last_6_months,DPD_60_in_last_9_months,DPD_60_in_last_12_months,DPD_60_in_last_18_months,DPD_60_in_last_24_months,DPD_90_in_last_3_months,DPD_90_in_last_6_months,DPD_90_in_last_9_months,DPD_90_in_last_12_months,DPD_90_in_last_18_months,DPD_90_in_last_24_months,DPD_120_in_last_3_months,DPD_120_in_last_6_months,DPD_120_in_last_9_months,DPD_120_in_last_12_months,DPD_120_in_last_18_months,DPD_120_in_last_24_months,DPD_150_in_last_3_months,DPD_150_in_last_6_months,DPD_150_in_last_9_months,DPD_150_in_last_12_months,DPD_150_in_last_18_months,DPD_150_in_last_24_months,DPD_180_in_last_3_months,DPD_180_in_last_6_months,DPD_180_in_last_9_months,DPD_180_in_last_12_months,DPD_180_in_last_24_months,'' as col1,isnull(Consider_For_Obligations,'true'),LoanStat,'LOANS',writeoffStat,writeoffstatdt,lastrepmtdt,limit_increase,PartSettlementDetails,SchemeCardProd as SchemeCardProduct,General_Status,Internal_WriteOff_Check,AmountPaidInLst6Mnths,InterestRate FROM ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) WHERE Child_wi = '"+formObject.getWFWorkitemName()+"' and LoanStat not in ('Pipeline','CAS-Pipeline') "+
		" union select CifId,CardEmbossNum,CardType,CardType,CustRoleType,'' as col6,'' as col7,'' as col8, OutstandingAmt,CreditLimit,case when SchemeCardProd like '%LOC%' then (select sum(cast(MonthlyAmount as float)) from ng_RLOS_CUSTEXPOSE_CardInstallmentDetails with (nolock) where child_wi ='"+formObject.getWFWorkitemName()+"'  and replace(CardNumber,'I','')=CardEmbossNum) else PaymentsAmount end,PaymentMode,'' as col13,case when SchemeCardProd like 'LOC%' then (select top 1 ISNULL((CAST(max(INSTALMENTpERIOD) AS INT)-CAST(min(rEMAININGemi) AS INT)),'') from ng_RLOS_CUSTEXPOSE_CardInstallmentDetails with (nolock) where child_wi = '"+formObject.getWFWorkitemName()+"' and replace(CardNumber,'I','')=CardEmbossNum) else ''end  as col14,CashLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,currentlycurrentflg,CurrMaxUtil,DPD_30_in_last_6_months,DPD_60_in_last_18_months,'' as col23,'' as col24,'' as col25,DPD_30_in_last_3_months,DPD_30_in_last_6_months,DPD_30_in_last_9_months,DPD_30_in_last_12_months,DPD_30_in_last_18_months,DPD_30_in_last_24_months,DPD_60_in_last_3_months,DPD_60_in_last_6_months,DPD_60_in_last_9_months,DPD_60_in_last_12_months,DPD_60_in_last_18_months,DPD_60_in_last_24_months,DPD_90_in_last_3_months,DPD_90_in_last_6_months,DPD_90_in_last_9_months,DPD_90_in_last_12_months,DPD_90_in_last_18_months,DPD_90_in_last_24_months,DPD_120_in_last_3_months,DPD_120_in_last_6_months,DPD_120_in_last_9_months,DPD_120_in_last_12_months,DPD_120_in_last_18_months,DPD_120_in_last_24_months,DPD_150_in_last_3_months,DPD_150_in_last_6_months,DPD_150_in_last_9_months,DPD_150_in_last_12_months,DPD_150_in_last_18_months,DPD_150_in_last_24_months,DPD_180_in_last_3_months,DPD_180_in_last_6_months,DPD_180_in_last_9_months,DPD_180_in_last_12_months,DPD_180_in_last_24_months,ExpiryDate,isnull(Consider_For_Obligations,'true'),CardStatus,'CARDS',writeoffStat,writeoffstatdt,'' as lastrepdate,limit_increase,'' as PartSettlementDetails,SchemeCardProd,General_Status,Internal_WriteOff_Check,AmountPaidInLst6Mnths,InterestRate FROM ng_RLOS_CUSTEXPOSE_CardDetails with (nolock) where Child_wi = '"+formObject.getWFWorkitemName()+"'  and Request_Type In ('InternalExposure','CollectionsSummary') "+
		" union select CifId,AcctId,'OverDraft' as loantype,'OverDraft' as loantype,CustRoleType,LimitSactionDate as loan_start_date,LimitExpiryDate as loanmaturitydate,'' as lastupdateddate,ClearBalanceAmount,SanctionLimit,'','','','',SanctionLimit,OverdueAmount,DaysPastDue,MonthsOnBook,IsCurrent,CurUtilRate,DPD30Last6Months,DPD60Last18Months,'',AccountOpenDate,'','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','',LimitExpiryDate,isNull(Consider_For_Obligations,'true'),AcctStat,'OVERDRAFT',WriteoffStat,WriteoffStatDt,LastRepmtDt,'','','','','','',''  from ng_rlos_custexpose_acctdetails with (nolock) where Child_wi = '"+formObject.getWFWorkitemName()+"'   and ODType != ''";
		//CreditCard.mLogger.info("InternalBureauIndividualProducts sQuery" + sQuery+ "");
		String CountQuery = "select count(*) from ng_RLOS_CUSTEXPOSE_CardDetails with (nolock) where Child_wi = '"+ formObject.getWFWorkitemName() + "' and cardStatus='A'";
		List<List<String>> CountXML = formObject.getDataFromDataSource(CountQuery);
		String add_xml_str = "";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		//CreditCard.mLogger.info("InternalBureauIndividualProducts list size"+ OutputXML.size()+ "");
		//CreditCard.mLogger.info("InternalBureauIndividualProducts list "+ OutputXML+ "");
		//String ReqProd = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 1);//commented by deepak as the same is of no use.
		String mol_sal_var = formObject.getNGValue("cmplx_MOL_molsalvar");

		try {
			for (int i = 0; i < OutputXML.size(); i++) {

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
				String Combined_Limit = "";
				String SecuredCard = "";
				String Internal_WriteOff_Check = "";
				String EmployerType = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 6);
				String Kompass = formObject.getNGValue("cmplx_EmploymentDetails_Kompass");
				CreditCard.mLogger.info("Inside for"+ "asdasdasd");
				String paid_installment = "";
				String LoanType = "";
				String amt_paid_last6mnths="";
				String interest_rate="";
				String custroletype="";//Deepak changes done to send Custrol type PCAS-2145 
				if (!(OutputXML.get(i).get(0) == null || OutputXML.get(i).get(0).equals(""))) {
					cifId = OutputXML.get(i).get(0).toString();
				}
				if (!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals(""))) {
					agreementId = OutputXML.get(i).get(1).toString();
				}
				if (!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2).equals(""))) {
					product_type = OutputXML.get(i).get(2).toString();
					LoanType = OutputXML.get(i).get(2);

					if ("Home In One".equalsIgnoreCase(product_type)) 
					{
						product_type = "HIO";
					} 
					else 
					{
						product_type = OutputXML.get(i).get(63).toString();
					}
				}
				if (!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals(""))) {
				}
				if (!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals(""))) {
					custroletype=OutputXML.get(i).get(4).toString();
				}
				if (!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5).equals(""))) {
					loan_start_date = OutputXML.get(i).get(5).toString();
				}
				if (OutputXML.get(i).get(6) != null && !OutputXML.get(i).get(6).isEmpty() && !OutputXML.get(i).get(6).equals("") && !OutputXML.get(i).get(6).equalsIgnoreCase("null")) {
					loanmaturitydate = OutputXML.get(i).get(6).toString();
				}
				if (OutputXML.get(i).get(7) != null && !OutputXML.get(i).get(7).isEmpty() && !OutputXML.get(i).get(7).equals("") && !OutputXML.get(i).get(7).equalsIgnoreCase("null")) {
					lastupdatedate = OutputXML.get(i).get(7).toString();
				}
				if (OutputXML.get(i).get(8) != null && !OutputXML.get(i).get(8).isEmpty() && !OutputXML.get(i).get(8).equals("") && !OutputXML.get(i).get(8).equalsIgnoreCase("null")) {
					outstandingamt = OutputXML.get(i).get(8).toString();
				}
				if (!(OutputXML.get(i).get(9) == null || OutputXML.get(i).get(9).equals(""))) {
					totalloanamount = OutputXML.get(i).get(9).toString();
				}
				if (!(OutputXML.get(i).get(10) == null || OutputXML.get(i).get(10).equals(""))) {
					Emi = OutputXML.get(i).get(10).toString();
				}
				if (!(OutputXML.get(i).get(11) == null || OutputXML.get(i).get(11).equals(""))) {
					paymentmode = OutputXML.get(i).get(11).toString();
				}
				if (OutputXML.get(i).get(12) != null && !OutputXML.get(i).get(12).isEmpty() && !OutputXML.get(i).get(12).equals("") && !OutputXML.get(i).get(12).equalsIgnoreCase("null")) {
					// SKLogger.writeLog("Inside for",
					// "asdasdasd"+OutputXML.get(i).get(12));
					totalnoofinstalments = OutputXML.get(i).get(12).toString();
				}
				if (!(OutputXML.get(i).get(13) == null || OutputXML.get(i).get(13).equals(""))) {
					remaininginstalments = OutputXML.get(i).get(13).toString();
				}
				if (!(OutputXML.get(i).get(14) == null || OutputXML.get(i).get(14).equals(""))) {
				}
				if (!(OutputXML.get(i).get(15) == null || OutputXML.get(i).get(15).equals(""))) {
					overdueamt = OutputXML.get(i).get(15).toString();
				}
				if (!(OutputXML.get(i).get(16) == null || OutputXML.get(i).get(16).equals(""))) {
					nofdayspmtdelay = OutputXML.get(i).get(16).toString();
				}
				if (!(OutputXML.get(i).get(17) == null || OutputXML.get(i).get(17).equals(""))) {
					monthsonbook = OutputXML.get(i).get(17).toString();
				}
				if (!(OutputXML.get(i).get(18) == null || OutputXML.get(i).get(18).equals(""))) {
					currentlycurrent = OutputXML.get(i).get(18).toString();
				}
				if (!(OutputXML.get(i).get(19) == null || OutputXML.get(i).get(19).equals(""))) {
					currmaxutil = OutputXML.get(i).get(19).toString();
				}
				if (!(OutputXML.get(i).get(20) == null || OutputXML.get(i).get(20).equals(""))) {
					DPD_30_in_last_6_months = OutputXML.get(i).get(20)	.toString();
				}
				if (!(OutputXML.get(i).get(21) == null || OutputXML.get(i).get(21).equals(""))) {
					DPD_60_in_last_18_months = OutputXML.get(i).get(21)	.toString();
				}
				if (!(OutputXML.get(i).get(22) == null || OutputXML.get(i).get(22).equals(""))) {
					propertyvalue = OutputXML.get(i).get(22).toString();
				}
				if (!(OutputXML.get(i).get(23) == null || OutputXML.get(i).get(23).equals(""))) {
					loan_disbursal_date = OutputXML.get(i).get(23).toString();
				}
				if (!(OutputXML.get(i).get(24) == null || OutputXML.get(i).get(24).equals(""))) {
					marketingcode = OutputXML.get(i).get(24).toString();
				}
				if (!(OutputXML.get(i).get(25) == null || OutputXML.get(i).get(25).equals(""))) {
					DPD_30_in_last_3_months = OutputXML.get(i).get(25)	.toString();
				}
				if (!(OutputXML.get(i).get(26) == null || OutputXML.get(i).get(26).equals(""))) {
					DPD_30_in_last_6_months = OutputXML.get(i).get(26)	.toString();
				}
				if (!(OutputXML.get(i).get(27) == null || OutputXML.get(i).get(27).equals(""))) {
					DPD_30_in_last_9_months = OutputXML.get(i).get(27)	.toString();
				}
				if (!(OutputXML.get(i).get(28) == null || OutputXML.get(i).get(28).equals(""))) {
					DPD_30_in_last_12_months = OutputXML.get(i).get(28)	.toString();
				}
				if (!(OutputXML.get(i).get(29) == null || OutputXML.get(i).get(29).equals(""))) {
					DPD_30_in_last_18_months = OutputXML.get(i).get(29)	.toString();
				}
				if (!(OutputXML.get(i).get(30) == null || OutputXML.get(i).get(30).equals(""))) {
					DPD_30_in_last_24_months = OutputXML.get(i).get(30)	.toString();
				}
				if (!(OutputXML.get(i).get(31) == null || OutputXML.get(i).get(31).equals(""))) {
					DPD_60_in_last_3_months = OutputXML.get(i).get(31)	.toString();
				}
				if (!(OutputXML.get(i).get(32) == null || OutputXML.get(i).get(32).equals(""))) {
					DPD_60_in_last_6_months = OutputXML.get(i).get(32)	.toString();
				}
				if (!(OutputXML.get(i).get(33) == null || OutputXML.get(i).get(33).equals(""))) {
					DPD_60_in_last_9_months = OutputXML.get(i).get(33)	.toString();
				}
				if (!(OutputXML.get(i).get(34) == null || OutputXML.get(i).get(34).equals(""))) {
					DPD_60_in_last_12_months = OutputXML.get(i).get(34)	.toString();
				}
				if (!(OutputXML.get(i).get(35) == null || OutputXML.get(i).get(35).equals(""))) {
					DPD_60_in_last_18_months = OutputXML.get(i).get(35)	.toString();
				}
				if (!(OutputXML.get(i).get(36) == null || OutputXML.get(i).get(36).equals(""))) {
					DPD_60_in_last_24_months = OutputXML.get(i).get(36)	.toString();
				}
				if (!(OutputXML.get(i).get(37) == null || OutputXML.get(i).get(37).equals(""))) {
					DPD_90_in_last_3_months = OutputXML.get(i).get(37)	.toString();
				}
				if (!(OutputXML.get(i).get(38) == null || OutputXML.get(i).get(38).equals(""))) {
					DPD_90_in_last_6_months = OutputXML.get(i).get(38)	.toString();
				}
				if (!(OutputXML.get(i).get(39) == null || OutputXML.get(i).get(39).equals(""))) {
					DPD_90_in_last_9_months = OutputXML.get(i).get(39)	.toString();
				}
				if (!(OutputXML.get(i).get(40) == null || OutputXML.get(i).get(40).equals(""))) {
					DPD_90_in_last_12_months = OutputXML.get(i).get(40)	.toString();
				}
				if (!(OutputXML.get(i).get(41) == null || OutputXML.get(i).get(41).equals(""))) {
					DPD_90_in_last_18_months = OutputXML.get(i).get(41)	.toString();
				}
				if (!(OutputXML.get(i).get(42) == null || OutputXML.get(i).get(42).equals(""))) {
					DPD_90_in_last_24_months = OutputXML.get(i).get(42)	.toString();
				}
				if (!(OutputXML.get(i).get(43) == null || OutputXML.get(i).get(43).equals(""))) {
					DPD_120_in_last_3_months = OutputXML.get(i).get(43)	.toString();
				}
				if (!(OutputXML.get(i).get(44) == null || OutputXML.get(i).get(44).equals(""))) {
					DPD_120_in_last_6_months = OutputXML.get(i).get(44)	.toString();
				}
				if (!(OutputXML.get(i).get(45) == null || OutputXML.get(i).get(45).equals(""))) {
					DPD_120_in_last_9_months = OutputXML.get(i).get(45)	.toString();
				}
				if (!(OutputXML.get(i).get(46) == null || OutputXML.get(i).get(46).equals(""))) {
					DPD_120_in_last_12_months = OutputXML.get(i).get(46)	.toString();
				}
				if (!(OutputXML.get(i).get(47) == null || OutputXML.get(i).get(47).equals(""))) {
					DPD_120_in_last_18_months = OutputXML.get(i).get(47)	.toString();
				}
				if (!(OutputXML.get(i).get(48) == null || OutputXML.get(i).get(48).equals(""))) {
					DPD_120_in_last_24_months = OutputXML.get(i).get(48)	.toString();
				}
				if (!(OutputXML.get(i).get(49) == null || OutputXML.get(i).get(49).equals(""))) {
					DPD_150_in_last_3_months = OutputXML.get(i).get(49)	.toString();
				}
				if (!(OutputXML.get(i).get(50) == null || OutputXML.get(i).get(50).equals(""))) {
					DPD_150_in_last_6_months = OutputXML.get(i).get(50)	.toString();
				}
				if (!(OutputXML.get(i).get(51) == null || OutputXML.get(i).get(51).equals(""))) {
					DPD_150_in_last_9_months = OutputXML.get(i).get(51)	.toString();
				}
				if (!(OutputXML.get(i).get(52) == null || OutputXML.get(i).get(52).equals(""))) {
					DPD_150_in_last_12_months = OutputXML.get(i).get(52)	.toString();
				}
				if (!(OutputXML.get(i).get(53) == null || OutputXML.get(i).get(53).equals(""))) {
					DPD_150_in_last_18_months = OutputXML.get(i).get(53)	.toString();
				}
				if (!(OutputXML.get(i).get(54) == null || OutputXML.get(i).get(54).equals(""))) {
					DPD_150_in_last_24_months = OutputXML.get(i).get(54)	.toString();
				}
				if (!(OutputXML.get(i).get(55) == null || OutputXML.get(i).get(55).equals(""))) {
					DPD_180_in_last_3_months = OutputXML.get(i).get(55)	.toString();
				}
				if (!(OutputXML.get(i).get(56) == null || OutputXML.get(i).get(56).equals(""))) {
					DPD_180_in_last_6_months = OutputXML.get(i).get(56)	.toString();
				}
				if (!(OutputXML.get(i).get(57) == null || OutputXML.get(i).get(57).equals(""))) {
					DPD_180_in_last_9_months = OutputXML.get(i).get(57)	.toString();
				}
				if (!(OutputXML.get(i).get(58) == null || OutputXML.get(i).get(58).equals(""))) {
					DPD_180_in_last_12_months = OutputXML.get(i).get(58)	.toString();
				}
				if (!(OutputXML.get(i).get(59) == null || OutputXML.get(i).get(59).equals(""))) {
					DPD_180_in_last_24_months = OutputXML.get(i).get(59)	.toString();
				}
				if (!(OutputXML.get(i).get(60) == null || OutputXML.get(i).get(60).equals(""))) {
					CardExpiryDate = OutputXML.get(i).get(60).toString();
				}

				if (!(OutputXML.get(i).get(61) == null || OutputXML.get(i).get(61).equals(""))) {
					Consider_For_Obligations = OutputXML.get(i).get(61)	.toString();
					if ("true".equalsIgnoreCase(Consider_For_Obligations)) 
					{
						Consider_For_Obligations = "Y";
					} 
					else 
					{
						Consider_For_Obligations = "N";
					}
				}

				if (!(OutputXML.get(i).get(62) == null || OutputXML.get(i).get(62).equals(""))) {
					phase = OutputXML.get(i).get(62).toString();
					if (phase.startsWith("C")) {phase = "C";
					} else {phase = "A";
					}
				}
				if (!(OutputXML.get(i).get(64) == null || OutputXML.get(i).get(64).equals(""))) {
					writeoffStat = OutputXML.get(i).get(64).toString();
				}
				if (!(OutputXML.get(i).get(65) == null || OutputXML.get(i).get(65).equals(""))) {
					writeoffstatdt = OutputXML.get(i).get(65).toString();
				}
				if (!(OutputXML.get(i).get(66) == null || OutputXML.get(i).get(66).equals(""))) {
					lastrepmtdt = OutputXML.get(i).get(66).toString();
				}
				if (!(OutputXML.get(i).get(67) == null || OutputXML.get(i).get(67).equals(""))) {
					Limit_increase = OutputXML.get(i).get(67).toString();
					if ("false".equalsIgnoreCase(Limit_increase)) 
					{
						Limit_increase = "N";
					} 
					else 
					{
						Limit_increase = "Y";
					}
				}
				if (!(OutputXML.get(i).get(68) == null || OutputXML.get(i).get(68).equals(""))) {
					// commented by aman 22-10-2017 to handdle part settelment
					// data
					// part_settlement_date =
					// OutputXML.get(i).get(67).toString();
					String abc = OutputXML.get(i).get(68).toString();
					abc = abc.substring(0, 10) + "split"+ abc.substring(10, abc.length());
					String abcsa[] = abc.split("split");
					part_settlement_date = abcsa[0];
					part_settlement_amount = abcsa[1];
				}
				if (!(OutputXML.get(i).get(69) == null || OutputXML.get(i).get(69).equals(""))) {
					SchemeCardProduct = OutputXML.get(i).get(69).toString();
				}
				if (!(OutputXML.get(i).get(70) == null || OutputXML.get(i).get(70).equals(""))) {
					General_Status = OutputXML.get(i).get(70).toString();
				}
				if (!(OutputXML.get(i).get(71) == null || OutputXML.get(i).get(71).equals(""))) {
					Internal_WriteOff_Check = OutputXML.get(i).get(71)	.toString();
				}
				//below tags added by saurabh on 11th July.
				if (!(OutputXML.get(i).get(72) == null || OutputXML.get(i).get(72).equals(""))) {
					amt_paid_last6mnths = OutputXML.get(i).get(72).toString();
				}
				if (!(OutputXML.get(i).get(73) == null || OutputXML.get(i).get(73).equals(""))) {
					interest_rate = OutputXML.get(i).get(73).toString();
				}


				String sQueryCombinedLimit = "select Distinct(COMBINEDLIMIT_ELIGIBILITY) from ng_master_cardProduct with (nolock) where code='" + SchemeCardProduct + "'";
				List<List<String>> sQueryCombinedLimitXML = formObject.getNGDataFromDataCache(sQueryCombinedLimit);
				CreditCard.mLogger.info("Deepak sQueryCombinedLimit: "+ sQueryCombinedLimit);
				CreditCard.mLogger.info("Deepak sQueryCombinedLimit result: "+ sQueryCombinedLimitXML);
				try{
					if(sQueryCombinedLimitXML!=null && sQueryCombinedLimitXML.size()>0 && sQueryCombinedLimitXML.get(0)!=null){
						Combined_Limit=sQueryCombinedLimitXML.get(0).get(0).equalsIgnoreCase("1")?"Y":"N";
					}
				}
				catch(Exception e){
					CreditCard.mLogger.info("Exception occured at sQueryCombinedLimit for"+sQueryCombinedLimit);

				}
				String sQuerySecuredCard = "select count(*) from ng_master_cardProduct with (nolock) where code='" + SchemeCardProduct + "'  and subproduct='SEC'";
				List<List<String>> sQuerySecuredCardXML = formObject.getNGDataFromDataCache(sQuerySecuredCard);
				try{
					if(sQuerySecuredCardXML!=null && sQuerySecuredCardXML.size()>0 && sQuerySecuredCardXML.get(0)!=null){
						SecuredCard=sQuerySecuredCardXML.get(0).get(0).equalsIgnoreCase("0")?"N":"Y";
					}	
				}
				catch(Exception e){
					CreditCard.mLogger.info("Exception occured at SecuredCard for"+SecuredCard);

				}

				if (cifId != null && !"".equalsIgnoreCase(cifId) && !"null".equalsIgnoreCase(cifId)) {
					CreditCard.mLogger.info("Inside if"+ "asdasdasd");
					add_xml_str = add_xml_str+ "<InternalBureauIndividualProducts><applicant_id>"+ cifId + "</applicant_id>";
					add_xml_str = add_xml_str+ "<internal_bureau_individual_products_id>"+ agreementId+ "</internal_bureau_individual_products_id>";
					add_xml_str = add_xml_str + "<type_product>" + product_type+ "</type_product>";
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
							//patch for PCAS-1310 (Nikhil)
							if ((SchemeCardProduct.toUpperCase()).contains("AMAL PERSONAL FINANCE")){
								add_xml_str = add_xml_str + "<contract_type>IPL</contract_type>";
							}
							else if ((SchemeCardProduct.toUpperCase()).contains("AMAL AUTO FINANCE")){
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
					add_xml_str = add_xml_str + "<provider_no>" + "RAKBANK"+ "</provider_no>";
					add_xml_str = add_xml_str + "<phase>" + phase + "</phase>";
					//Deepak changes done to send Custrol type PCAS-2145 
					add_xml_str = add_xml_str+ "<role_of_customer>"+custroletype+"</role_of_customer>";
					//add_xml_str = add_xml_str+ "<role_of_customer>Primary</role_of_customer>";
					
					

					add_xml_str = add_xml_str + "<start_date>"+ loan_start_date + "</start_date>";
					add_xml_str = add_xml_str + "<close_date>"+ loanmaturitydate + "</close_date>";
					add_xml_str = add_xml_str + "<date_last_updated>"+ lastupdatedate + "</date_last_updated>";
					add_xml_str = add_xml_str + "<outstanding_balance>"+ outstandingamt + "</outstanding_balance>";
					if ("Loans".equalsIgnoreCase(OutputXML.get(i).get(63))) {
						add_xml_str = add_xml_str + "<total_amount>"+ totalloanamount + "</total_amount>";
					}
					add_xml_str = add_xml_str + "<payments_amount>" + Emi+ "</payments_amount>";
					add_xml_str = add_xml_str + "<method_of_payment>"+ paymentmode + "</method_of_payment>";
					add_xml_str = add_xml_str + "<total_no_of_instalments>"+ totalnoofinstalments+ "</total_no_of_instalments>";
					add_xml_str = add_xml_str + "<no_of_remaining_instalments>"+ remaininginstalments+ "</no_of_remaining_instalments>";
					add_xml_str = add_xml_str + "<worst_status>" + writeoffStat+ "</worst_status>";
					add_xml_str = add_xml_str + "<worst_status_date>"+ writeoffstatdt + "</worst_status_date>";
					if ("cards".equalsIgnoreCase(OutputXML.get(i).get(63))||"OVERDRAFT".equalsIgnoreCase(OutputXML.get(i).get(63))){
						add_xml_str = add_xml_str + "<credit_limit>"+ totalloanamount + "</credit_limit>";
					}
					add_xml_str = add_xml_str + "<overdue_amount>" + overdueamt+ "</overdue_amount>";
					add_xml_str = add_xml_str + "<no_of_days_payment_delay>"+ nofdayspmtdelay + "</no_of_days_payment_delay>";
					add_xml_str = add_xml_str + "<mob>" + monthsonbook+ "</mob>";
					add_xml_str = add_xml_str + "<last_repayment_date>"+ lastrepmtdt + "</last_repayment_date>";
					add_xml_str = add_xml_str + "<currently_current>"+ currentlycurrent + "</currently_current>";
					//add_xml_str = add_xml_str + "<current_utilization>"+ currmaxutil + "</current_utilization>";
					add_xml_str = add_xml_str + "<dpd_30_last_6_mon>"+ DPD_30_in_last_6_months + "</dpd_30_last_6_mon>";
					add_xml_str = add_xml_str + "<dpd_60p_in_last_18_mon>"+ DPD_60_in_last_18_months+ "</dpd_60p_in_last_18_mon>";
					if ("cards".equalsIgnoreCase(OutputXML.get(i).get(63))) {
						add_xml_str = add_xml_str + "<card_product>"+ SchemeCardProduct + "</card_product>";
					}
					add_xml_str = add_xml_str + "<property_value>"+ propertyvalue + "</property_value>";
					add_xml_str = add_xml_str + "<disbursal_date>"+ loan_disbursal_date + "</disbursal_date>";
					add_xml_str = add_xml_str + "<marketing_code>"+ marketingcode + "</marketing_code>";
					add_xml_str = add_xml_str + "<card_expiry_date>"+ CardExpiryDate + "</card_expiry_date>";
					add_xml_str = add_xml_str + "<card_upgrade_indicator>"+ Limit_increase + "</card_upgrade_indicator>";
					add_xml_str = add_xml_str + "<part_settlement_date>"+ part_settlement_date + "</part_settlement_date>";
					add_xml_str = add_xml_str + "<part_settlement_amount>"+ part_settlement_amount+ "</part_settlement_amount>";
					add_xml_str = add_xml_str + "<part_settlement_reason>" + ""+ "</part_settlement_reason>";
					add_xml_str = add_xml_str + "<limit_expiry_date>" + ""+ "</limit_expiry_date>";
					add_xml_str = add_xml_str + "<no_of_primary_cards>"+ CountXML.get(0).get(0) + "</no_of_primary_cards>";
					add_xml_str = add_xml_str + "<no_of_repayments_done>"+ remaininginstalments + "</no_of_repayments_done>";
					add_xml_str = add_xml_str + "<card_segment>"+ SchemeCardProduct + "</card_segment>";
					add_xml_str = add_xml_str + "<product_type>"+ OutputXML.get(i).get(63) + "</product_type>";
					add_xml_str = add_xml_str + "<product_category>"+ SchemeCardProduct + "</product_category>";
					add_xml_str = add_xml_str + "<combined_limit_flag>"+ Combined_Limit + "</combined_limit_flag>";
					add_xml_str = add_xml_str + "<secured_card_flag>"+ SecuredCard + "</secured_card_flag>";
					add_xml_str = add_xml_str + "<resch_tko_flag>"+ Limit_increase + "</resch_tko_flag>";

					add_xml_str = add_xml_str + "<general_status>"+ General_Status + "</general_status>";
					add_xml_str = add_xml_str + "<consider_for_obligation>"+ Consider_For_Obligations+ "</consider_for_obligation>";
					add_xml_str = add_xml_str + "<limit_increase>"+ Limit_increase + "</limit_increase>";
					
					//Deepak changes done to send Custrol type PCAS-2145
					//add_xml_str = add_xml_str + "<role>Primary</role>";
					add_xml_str = add_xml_str + "<role>"+custroletype+"</role>";
					
					add_xml_str = add_xml_str + "<limit>" + "" + "</limit>";
					add_xml_str = add_xml_str + "<status>" + phase+ "</status>";
					add_xml_str = add_xml_str + "<emi>" + Emi + "</emi>";
					add_xml_str = add_xml_str + "<os_amt>" + outstandingamt+ "</os_amt>";

					add_xml_str = add_xml_str + "<dpd_30_in_last_3mon>"+ DPD_30_in_last_3_months+ "</dpd_30_in_last_3mon>";
					add_xml_str = add_xml_str + "<dpd_30_in_last_6mon>"+ DPD_30_in_last_6_months+ "</dpd_30_in_last_6mon>";
					add_xml_str = add_xml_str + "<dpd_30_in_last_9mon>"+ DPD_30_in_last_9_months+ "</dpd_30_in_last_9mon>";
					add_xml_str = add_xml_str + "<dpd_30_in_last_12mon>"+ DPD_30_in_last_12_months+ "</dpd_30_in_last_12mon>";

					add_xml_str = add_xml_str + "<dpd_30_in_last_18mon>"+ DPD_30_in_last_18_months+ "</dpd_30_in_last_18mon>";
					add_xml_str = add_xml_str + "<dpd_30_in_last_24mon>"+ DPD_30_in_last_24_months+ "</dpd_30_in_last_24mon>";
					add_xml_str = add_xml_str + "<dpd_60_in_last_3mon>"+ DPD_60_in_last_3_months+ "</dpd_60_in_last_3mon>";
					add_xml_str = add_xml_str + "<dpd_60_in_last_6mon>"+ DPD_60_in_last_6_months+ "</dpd_60_in_last_6mon>";
					add_xml_str = add_xml_str + "<dpd_60_in_last_9mon>"+ DPD_60_in_last_9_months+ "</dpd_60_in_last_9mon>";
					add_xml_str = add_xml_str + "<dpd_60_in_last_12mon>"+ DPD_60_in_last_12_months+ "</dpd_60_in_last_12mon>";
					//change done by nikhil PCAS-1275
					add_xml_str = add_xml_str + "<dpd_60p_in_last_12_mon>"+ DPD_60_in_last_12_months+ "</dpd_60p_in_last_12_mon>";
					add_xml_str = add_xml_str + "<dpd_60_in_last_18mon>"+ DPD_60_in_last_18_months+ "</dpd_60_in_last_18mon>";
					add_xml_str = add_xml_str + "<dpd_60_in_last_24mon>"+ DPD_60_in_last_24_months+ "</dpd_60_in_last_24mon>";
					add_xml_str = add_xml_str + "<dpd_90_in_last_3mon>"+ DPD_90_in_last_3_months+ "</dpd_90_in_last_3mon>";
					add_xml_str = add_xml_str + "<dpd_90_in_last_6mon>"+ DPD_90_in_last_6_months+ "</dpd_90_in_last_6mon>";
					add_xml_str = add_xml_str + "<dpd_90_in_last_9mon>"+ DPD_90_in_last_9_months+ "</dpd_90_in_last_9mon>";
					add_xml_str = add_xml_str + "<dpd_90_in_last_12mon>"+ DPD_90_in_last_12_months+ "</dpd_90_in_last_12mon>";
					add_xml_str = add_xml_str + "<dpd_90_in_last_18mon>"+ DPD_90_in_last_18_months+ "</dpd_90_in_last_18mon>";
					add_xml_str = add_xml_str + "<dpd_90_in_last_24mon>"+ DPD_90_in_last_24_months+ "</dpd_90_in_last_24mon>";
					add_xml_str = add_xml_str + "<dpd_120_in_last_3mon>"+ DPD_120_in_last_3_months+ "</dpd_120_in_last_3mon>";
					add_xml_str = add_xml_str + "<dpd_120_in_last_6mon>"+ DPD_120_in_last_6_months+ "</dpd_120_in_last_6mon>";
					add_xml_str = add_xml_str + "<dpd_120_in_last_9mon>"+ DPD_120_in_last_9_months+ "</dpd_120_in_last_9mon>";
					add_xml_str = add_xml_str + "<dpd_120_in_last_12mon>"+ DPD_120_in_last_12_months+ "</dpd_120_in_last_12mon>";
					add_xml_str = add_xml_str + "<dpd_120_in_last_18mon>"+ DPD_120_in_last_18_months+ "</dpd_120_in_last_18mon>";
					add_xml_str = add_xml_str + "<dpd_120_in_last_24mon>"+ DPD_120_in_last_24_months+ "</dpd_120_in_last_24mon>";

					add_xml_str = add_xml_str + "<dpd_150_in_last_3mon>"+ DPD_150_in_last_3_months+ "</dpd_150_in_last_3mon>";
					add_xml_str = add_xml_str + "<dpd_150_in_last_6mon>"+ DPD_150_in_last_6_months+ "</dpd_150_in_last_6mon>";
					add_xml_str = add_xml_str + "<dpd_150_in_last_9mon>"+ DPD_150_in_last_9_months+ "</dpd_150_in_last_9mon>";
					add_xml_str = add_xml_str + "<dpd_150_in_last_12mon>"+ DPD_150_in_last_12_months+ "</dpd_150_in_last_12mon>";
					add_xml_str = add_xml_str + "<dpd_150_in_last_18mon>"+ DPD_150_in_last_18_months+ "</dpd_150_in_last_18mon>";
					add_xml_str = add_xml_str + "<dpd_150_in_last_24mon>"+ DPD_150_in_last_24_months+ "</dpd_150_in_last_24mon>";
					add_xml_str = add_xml_str + "<dpd_180_in_last_3mon>"+ DPD_180_in_last_3_months+ "</dpd_180_in_last_3mon>";
					add_xml_str = add_xml_str + "<dpd_180_in_last_6mon>"+ DPD_180_in_last_6_months+ "</dpd_180_in_last_6mon>";
					add_xml_str = add_xml_str + "<dpd_180_in_last_9mon>"+ DPD_180_in_last_9_months+ "</dpd_180_in_last_9mon>";
					add_xml_str = add_xml_str + "<dpd_180_in_last_12mon>"+ DPD_180_in_last_12_months+ "</dpd_180_in_last_12mon>";
					add_xml_str = add_xml_str + "<dpd_180_in_last_18mon>" + ""+ "</dpd_180_in_last_18mon>";
					add_xml_str = add_xml_str + "<dpd_180_in_last_24mon>"+ DPD_180_in_last_24_months+ "</dpd_180_in_last_24mon>";
					add_xml_str = add_xml_str + "<last_temp_limit_exp>" + ""+ "</last_temp_limit_exp>";
					add_xml_str = add_xml_str + "<last_per_limit_exp>" + ""+ "</last_per_limit_exp>";
					add_xml_str = add_xml_str + "<security_cheque_amt>" + ""+ "</security_cheque_amt>";
					add_xml_str = add_xml_str + "<mol_salary_variance>"+ mol_sal_var + "</mol_salary_variance>";
					if (Kompass != null) 
					{
						if ("true".equalsIgnoreCase(Kompass))
						{	
							add_xml_str = add_xml_str + "<kompass>" + "Y"+ "</kompass>";
						} else 
						{	
							add_xml_str = add_xml_str + "<kompass>" + "N"+ "</kompass>";
						}
					}
					add_xml_str = add_xml_str + "<employer_type>"+ EmployerType + "</employer_type>";

					if (totalnoofinstalments != null	&& remaininginstalments != null	&& !totalnoofinstalments.equals("")	&& !remaininginstalments.equals("")) 
					{
						paid_installment = Integer.toString(Integer.parseInt(totalnoofinstalments)- Integer.parseInt(remaininginstalments));
						CreditCard.mLogger.info("Inside paid_installment"+"paid_installment" + paid_installment);

					}
					/*if (NGFUserResourceMgr_CreditCard.getGlobalVar("CC_CreditCard").equalsIgnoreCase(ReqProd)) 
					{
						add_xml_str = add_xml_str+ "<no_of_paid_installment>"+ paid_installment+ "</no_of_paid_installment><write_off_amount>"+ Internal_WriteOff_Check+ "</write_off_amount><company_flag>N</company_flag><amt_paid_last6mnths>"+ amt_paid_last6mnths+ "</amt_paid_last6mnths><interest_rate>"+interest_rate+"</interest_rate></InternalBureauIndividualProducts>";
					} 
					else 
					{*/
					add_xml_str = add_xml_str+ "<no_of_paid_installment>"+ paid_installment+ "</no_of_paid_installment><write_off_amount>"+ Internal_WriteOff_Check+ "</write_off_amount><company_flag>N</company_flag><type_of_od>"+ ""+ "</type_of_od><amt_paid_last6mnths>"+ amt_paid_last6mnths+ "</amt_paid_last6mnths><topup_indicator>"+Limit_increase+"</topup_indicator><interest_rate>"+interest_rate+"</interest_rate></InternalBureauIndividualProducts>";

					//}
				}

			}
		} catch (Exception e) {
			CreditCard.mLogger.info("RLOSCommon"+ "Internal liab tag Cration: " + CC_Common.printException(e));
		}
		CreditCard.mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "	+ add_xml_str);
		return add_xml_str;
}
	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : get Customer Adress Details       

	 ***********************************************************************************  */
	public String getCustAddress_details(String call_name){
		CreditCard.mLogger.info("RLOSCommon java file"+ " inside getCustAddress_details for: "+call_name);
		String  add_xml_str ="";
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			int add_row_count = formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
			CreditCard.mLogger.info("CC_Common java file"+ " inside getCustAddress_details add_row_count+ : "+add_row_count);

			for (int i = 0; i<add_row_count;i++){
				String Address_type = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0); //0
				String flat_Villa=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 1);//1
				String Building_name=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 2);//2
				String street_name=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 3);//3
				String Landmard=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 4);//4
				String city = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 5);//5
				String Emirates=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 6);//6
				String country=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 7);//7
				String Po_Box=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 8);//8
				String years_in_current_add=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 9);//9
				String Applicant_Name=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i,13);
				CreditCard.mLogger.info("aplicant name in address is: "+Applicant_Name.split("-")[1]);
				//CreditCard.mLogger.info("aplicant name in cc-creation is: "+formObject.getNGValue("CC_Creation_CustomerName"));
				String preferrd="";
				//deepak 24 june 2018 index in below if condition is changed from 11 to 10 for new card creation. 
				//if(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 11).equalsIgnoreCase("true"))//10
				if(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 10).equalsIgnoreCase("true"))//10
					preferrd = "Y";
				else
					preferrd = "N";


				//added here
				CreditCard.mLogger.info("RLOSCommon java file"+ " inside getCustAddress_details add_row_count+ : "+years_in_current_add);
				int years=0;

				//ended here
				//CreditCard.mLogger.info("PLCommon java file"+ " ADD Type: "+Address_type);
				/*if (Address_type.equalsIgnoreCase("HOME")){
						Address_type="Home Country";
					}*/
				//CreditCard.mLogger.info("PLCommon java file"+ " ADD Type after: "+Address_type);

				//Code change to added Effective from and to start
				String EffectiveFrom="";
				String EffectiveTo="";
				if (!"".equalsIgnoreCase(years_in_current_add)){
					years=(int)Float.parseFloat(years_in_current_add);

					SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
					Calendar cal = Calendar.getInstance();
					EffectiveTo=sdf1.format(cal.getTime());
					cal.add(Calendar.YEAR, -years);
					EffectiveFrom=sdf1.format(cal.getTime());
					CreditCard.mLogger.info("RLOS value of CurrentDate EffectiveTo"+" "+EffectiveTo);
					CreditCard.mLogger.info("RLOS value of EffectiveFromDate"+" "+EffectiveFrom);
				}
				//Code change to added Effective from and to End
				CreditCard.mLogger.info("RLOS value of prefered Add: Address Type: "+Address_type+" Address pref flag: "+formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 11));
				CreditCard.mLogger.info("RLOS Selected row is: "+formObject.getSelectedIndex("cmplx_DEC_MultipleApplicantsGrid"));
				String cust_type = formObject.getNGValue("cmplx_DEC_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_DEC_MultipleApplicantsGrid"),0);
				
				if (call_name.equalsIgnoreCase("NEW_CUSTOMER_REQ")){
					CreditCard.mLogger.info("Applicant_Name.split('-')[1].trim().replaceAll(' ',''): "+Applicant_Name.split("-")[1].trim().replaceAll(" ",""));
					CreditCard.mLogger.info("Multiple Applicants Grid:"+formObject.getNGValue("cmplx_DEC_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_DEC_MultipleApplicantsGrid"),1).trim().replaceAll(" ",""));
					//Deepak 20 Dec changes to pick correct add details in case both primary & supplement have same name.
					if("Primary".equalsIgnoreCase(cust_type)){
						if(Applicant_Name.split("-")[0].trim().replaceAll(" ","").equalsIgnoreCase("P")&& Applicant_Name.split("-")[1].trim().replaceAll(" ","").equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_DEC_MultipleApplicantsGrid"),1).trim().replaceAll(" ","")))
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
					else{
						if(Applicant_Name.split("-")[0].trim().replaceAll(" ","").equalsIgnoreCase("S") && Applicant_Name.split("-")[1].trim().replaceAll(" ","").equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_DEC_MultipleApplicantsGrid"),1).trim().replaceAll(" ","")) &&
								Applicant_Name.split("-")[2].trim().replaceAll(" ","").equalsIgnoreCase(formObject.getNGValue("cmplx_DEC_MultipleApplicantsGrid",formObject.getSelectedIndex("cmplx_DEC_MultipleApplicantsGrid"),2).trim().replaceAll(" ","")))
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

				}
				else if(NGFUserResourceMgr_CreditCard.getGlobalVar("Services").contains(formObject.getNGValue("Sub_Product"))
						|| Applicant_Name.split("-")[1].trim().replaceAll(" ","").equalsIgnoreCase(formObject.getNGValue("CC_Creation_CustomerName").trim().replaceAll(" ","")))
				{
					CreditCard.mLogger.info("RLOSCommon"+ "Applicant Type is Supplement and tab is disbursal!!!");

					if(call_name.equalsIgnoreCase("CUSTOMER_UPDATE_REQ")){
						add_xml_str = add_xml_str + "<AddrDet><AddressType>"+Address_type+"</AddressType>";
						//Code change to added Effective from and to start
						add_xml_str = add_xml_str + "<EffectiveFrom>"+EffectiveFrom+"</EffectiveFrom>";
						add_xml_str = add_xml_str + "<EffectiveTo>"+"2099-12-31"+"</EffectiveTo>";
						add_xml_str = add_xml_str + "<HoldMailFlag>N</HoldMailFlag>";
						add_xml_str = add_xml_str + "<ReturnFlag>N</ReturnFlag>";
						add_xml_str = add_xml_str + "<AddrPrefFlag>"+preferrd+"</AddrPrefFlag>";
						//Code change to added Effective from and to End

						add_xml_str = add_xml_str + "<AddrLine1>"+flat_Villa+"</AddrLine1>";
						add_xml_str = add_xml_str + "<AddrLine2>"+Building_name+"</AddrLine2>";
						add_xml_str = add_xml_str + "<AddrLine3>"+street_name+"</AddrLine3>";
						add_xml_str = add_xml_str + "<AddrLine4>"+Landmard+"</AddrLine4>";
						add_xml_str = add_xml_str + "<POBox>"+Po_Box+"</POBox>";
						add_xml_str = add_xml_str + "<State>"+Emirates+"</State>";
						add_xml_str = add_xml_str + "<City>"+city+"</City>";
						add_xml_str = add_xml_str + "<CountryCode>"+country+"</CountryCode></AddrDet>";
						//add_xml_str = add_xml_str + "<POBox>"+Po_Box+"</POBox></AddrDet>";
					}

					else if(call_name.equalsIgnoreCase("NEW_CREDITCARD_REQ")){
						//Changes done by Deepak for PCSP-7,PCSP-339,PCSP-340 
						//Changes overwritten for PCSP - 667
						if (Address_type.equalsIgnoreCase("Mailing")|| Address_type.equalsIgnoreCase("Home") ){

						}
						else{
							//if(preferrd.equalsIgnoreCase("N") && "AE".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_Nationality")))
							if(preferrd.equalsIgnoreCase("N"))
							{
								Address_type="Additional";
							}
							/*if(Address_type.equalsIgnoreCase("RESIDENCE")){
								Address_type="Residence";
							}*/
							/*else if(Address_type.equalsIgnoreCase("Home")){
								Address_type="Additional";
							}*/
							//Changes done by Deepak for PCSP-651
							if("0".equalsIgnoreCase(fetchAddressFlagVal())){
								add_xml_str=getCardCrationDummyAddress_details();
							}
							else{
								if(preferrd.equalsIgnoreCase("Y")){
									add_xml_str = add_xml_str + "<AddressDetails><AddrType>Statement</AddrType><UseExistingAddress>"+fetchAddressFlagVal()+"</UseExistingAddress><AddressLocation>STMT</AddressLocation><AddressReferenceLink>"+fetchAddressFlagVal()+"</AddressReferenceLink><IsPreferedAddr>"+preferrd+"</IsPreferedAddr>";
								}
								else{
									add_xml_str = add_xml_str + "<AddressDetails><AddrType>"+Address_type+"</AddrType><UseExistingAddress>"+fetchAddressFlagVal()+"</UseExistingAddress><AddressLocation>OTHER</AddressLocation><AddressReferenceLink>"+fetchAddressFlagVal()+"</AddressReferenceLink><IsPreferedAddr>"+preferrd+"</IsPreferedAddr>";
								}
								city = getCodeDesc("NG_MASTER_City","Description", city);
								Emirates = getCodeDesc("NG_MASTER_state","Description", Emirates);
								
								add_xml_str = add_xml_str + "<Addr1>"+flat_Villa+"</Addr1>";
								add_xml_str = add_xml_str + "<Addr2>"+Building_name+"</Addr2>";
								add_xml_str = add_xml_str + "<Addr3>"+street_name+" "+Landmard+"</Addr3>";
								add_xml_str = add_xml_str + "<Addr4>P.O.BOX "+Po_Box+"</Addr4>";
								//add_xml_str = add_xml_str + "<Addr5>"+city+"</Addr5>";
								add_xml_str = add_xml_str + "<PostalCode>"+Po_Box+"</PostalCode>";
								add_xml_str = add_xml_str + "<ZipCode></ZipCode>";
								add_xml_str = add_xml_str + "<City>"+city+"</City>";
								add_xml_str= add_xml_str + "<StateProv>"+Emirates+"</StateProv>";
								//change by saurabh on 28th Jan
								//add_xml_str = add_xml_str + "<StateProv>"+city+"</StateProv>";
								//add_xml_str = add_xml_str + "<County>"+city+"</County>";
								add_xml_str = add_xml_str + "<Country>784</Country>"+ "</AddressDetails>";
							}
							
						}
					}

					else if(call_name.equalsIgnoreCase("CARD_NOTIFICATION")){
						if (Address_type.equalsIgnoreCase("HOME")){
							Address_type="Home Country";
						}
						add_xml_str = add_xml_str + "<AddrDet><AddressType>"+Address_type+"</AddressType>";
						add_xml_str = add_xml_str + "<AddressLine1>"+flat_Villa+"</AddressLine1>";
						add_xml_str = add_xml_str + "<AddressLine2>"+Building_name+"</AddressLine2>";
						add_xml_str = add_xml_str + "<AddressLine3>"+street_name+"</AddressLine3>";
						add_xml_str = add_xml_str + "<AddressLine4>"+Landmard+"</AddressLine4>";
						add_xml_str = add_xml_str + "<City>"+city+"</City>";
						add_xml_str = add_xml_str + "<State>"+Emirates+"</State>";
						add_xml_str = add_xml_str + "<Country>"+country+"</Country>";
						if(Address_type.equalsIgnoreCase("Home Country")){
							add_xml_str = add_xml_str + "<ZipCode>"+Po_Box+"</ZipCode>";
						}
						add_xml_str = add_xml_str + "<POBox>"+Po_Box+"</POBox>";
						add_xml_str = add_xml_str + "<EffectiveFromDate>"+EffectiveFrom+"</EffectiveFromDate>";
						add_xml_str = add_xml_str + "<EffectiveToDate>"+EffectiveTo+"</EffectiveToDate>";
						add_xml_str = add_xml_str + "<NumberOfYears>"+years+"</NumberOfYears>";
						add_xml_str = add_xml_str + "<ResidenceType>R</ResidenceType>";
						add_xml_str = add_xml_str + "<AddrPrefFlag>"+preferrd+"</AddrPrefFlag>"
								+ "</AddrDet>";
						//add_xml_str = add_xml_str + "<POBox>"+Po_Box+"</POBox></AddrDet>";
					}
					//10/08/2017 added for the respective call
					else{
						add_xml_str = add_xml_str + "<AddressDetails><AddressType>"+Address_type+"</AddressType>";
						add_xml_str = add_xml_str + "<AddressLine1>"+flat_Villa+"</AddressLine1>";
						add_xml_str = add_xml_str + "<AddressLine2>"+Building_name+"</AddressLine2>";
						add_xml_str = add_xml_str + "<AddressLine3>"+street_name+"</AddressLine3>";
						add_xml_str = add_xml_str + "<City>"+city+"</City>";
						add_xml_str = add_xml_str + "<State>"+Emirates+"</State>";
						add_xml_str = add_xml_str + "<Country>"+country+"</Country>";
						add_xml_str = add_xml_str + "<POBox>"+Po_Box+"</POBox></AddressDetails>";
					}
				} 	
				/*else{
					add_xml_str = add_xml_str + "<AddressDetails><AddressType>"+Address_type+"</AddressType>";
					add_xml_str = add_xml_str + "<AddressLine1>"+flat_Villa+"</AddressLine1>";
					add_xml_str = add_xml_str + "<AddressLine2>"+Building_name+"</AddressLine2>";
					add_xml_str = add_xml_str + "<AddressLine3>"+street_name+"</AddressLine3>";
					add_xml_str = add_xml_str + "<City>"+city+"</City>";
					add_xml_str = add_xml_str + "<State>"+Emirates+"</State>";
					add_xml_str = add_xml_str + "<Country>"+country+"</Country>";
					add_xml_str = add_xml_str + "<POBox>"+Po_Box+"</POBox></AddressDetails>";
				}*/
			}
			//CreditCard.mLogger.info("RLOSCommon---->Address tag Cration: "+ add_xml_str);
			return add_xml_str;
		}
		catch(Exception e){
			CreditCard.mLogger.info("PLCommon getCustAddress_details method"+ " Exception Occure in generate Address XMl"+e.getMessage()+CC_Common.printException(e));
			return add_xml_str;
		}
	}


	public String getRVCDetails(){
		CreditCard.mLogger.info("RLOSCommon java file inside getRVCDetails : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String  add_xml_str ="";
		try{
			//<RVC><RVCPackage/><RVCStatus>No</RVCStatus><RVCSalesMode/><RVCRegDate/><RVCActivationDate/></RVC>
			if(formObject.isVisible("CC_Loan_Frame3")){
				if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_CC_Loan_VPSFlag"))){
					add_xml_str = add_xml_str + "<RVC><RVCPackage>"+formObject.getNGValue("cmplx_CC_Loan_VPSPAckage")+"</RVCPackage>";
					add_xml_str = add_xml_str + "<RVCStatus>Yes</RVCStatus>";
					add_xml_str = add_xml_str + "<RVCSalesMode>"+formObject.getNGValue("cmplx_CC_Loan_VPSSaleMode")+"</RVCSalesMode>";
					add_xml_str = add_xml_str + "<RVCRegDate>"+formObject.getNGValue("cmplx_CC_Loan_VPSRegDate")+"</RVCRegDate>";
					add_xml_str = add_xml_str + "<RVCActivationDate>"+formObject.getNGValue("cmplx_CC_Loan_VPSActDate")+"</RVCActivationDate></RVC>";
				}
			}
			return add_xml_str;
		}
		catch(Exception ex){
			/*new CC_Common();*/
			//Commented for sonar
			CreditCard.mLogger.info("Exception Occure in getRVCDetails()"+ex.getMessage()+CC_Common.printException(ex));
			return add_xml_str;
		}
	}

	public String getDDSDetails(){
		CreditCard.mLogger.info("RLOSCommon java file inside getDDSDetails : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String  add_xml_str ="";
		try{
			if(formObject.isVisible("CC_Loan_Frame4")){
				if("true".equalsIgnoreCase(formObject.getNGValue("cmplx_CC_Loan_DDSFlag"))){
					add_xml_str = add_xml_str + "<PaymentInstruction><TypeIndicator>DDS</TypeIndicator>";
					add_xml_str = add_xml_str + "<Mode>"+formObject.getNGValue("cmplx_CC_Loan_DDSMode")+"</Mode>";
					add_xml_str = add_xml_str + "<FlatAmount>"+formObject.getNGValue("cmplx_CC_Loan_DDSAmount")+"</FlatAmount>";
					add_xml_str = add_xml_str + "<Percentage>"+formObject.getNGValue("cmplx_CC_Loan_Percentage")+"</Percentage>";
					add_xml_str = add_xml_str + "<ExecutionDay>"+formObject.getNGValue("cmplx_CC_Loan_DDSExecDay")+"</ExecutionDay>";
					add_xml_str = add_xml_str + "<StartDate>"+formObject.getNGValue("cmplx_CC_Loan_DDSStartdate")+"</StartDate>";
					add_xml_str = add_xml_str + "<DebitBankName>"+formObject.getNGValue("cmplx_CC_Loan_DDSBankAName")+"</DebitBankName>";
					add_xml_str = add_xml_str + "<DebitAccountNum>"+formObject.getNGValue("cmplx_CC_Loan_IBan")+"</DebitAccountNum>";
					add_xml_str = add_xml_str + "<AccountType>"+formObject.getNGValue("cmplx_CC_Loan_AccType")+"</AccountType>";
					add_xml_str = add_xml_str + "<EntityNumber>"+formObject.getNGValue("cmplx_CC_Loan_DDSEntityNo")+"</EntityNumber></PaymentInstruction>";
				}
			}
			return add_xml_str;
		}
		catch(Exception ex){
		/*	new CC_Common();*/
		//Commented for sonar
			CreditCard.mLogger.info("Exception Occure in getDDSDetails()"+ex.getMessage()+CC_Common.printException(ex));
			return add_xml_str;
		}
	}
	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : Internal Bounced cheques       

	 ***********************************************************************************  */
	public String InternalBouncedCheques() {
		CreditCard.mLogger.info("RLOSCommon java file"+"inside InternalBouncedCheques : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "select CIFID,AcctId,returntype,returnNumber,returnAmount,retReasonCode,returnDate from ng_rlos_FinancialSummary_ReturnsDtls with (nolock) where Child_Wi = '"+ formObject.getWFWorkitemName() + "'";
		//CreditCard.mLogger.info("InternalBouncedCheques sQuery" + sQuery+ "");
		String add_xml_str = "";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		//CreditCard.mLogger.info("InternalBouncedCheques list size"+ OutputXML.size()+ "");

		for (int i = 0; i < OutputXML.size(); i++) {

			String applicantID = "";
			String chequeNo = "";
			String internal_bounced_cheques_id = "";
			String bouncedCheq = "";
			String amount = "";
			String reason = "";
			String returnDate = "";

			if (!(OutputXML.get(i).get(0) == null || OutputXML.get(i).get(0)
					.equals(""))) {
				applicantID = OutputXML.get(i).get(0).toString();
			}
			if (!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1)
					.equals(""))) {
				internal_bounced_cheques_id = OutputXML.get(i).get(1).toString();
			}
			if (!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2)
					.equals(""))) {
				bouncedCheq = OutputXML.get(i).get(2).toString();
			}
			if (!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3)
					.equals(""))) {
				chequeNo = OutputXML.get(i).get(3).toString();
			}
			if (!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4)
					.equals(""))) {
				amount = OutputXML.get(i).get(4).toString();
			}
			if (!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5)
					.equals(""))) {
				reason = OutputXML.get(i).get(5).toString();
			}
			if (!(OutputXML.get(i).get(6) == null || OutputXML.get(i).get(6)
					.equals(""))) {
				returnDate = OutputXML.get(i).get(6).toString();
			}

			if (applicantID != null && !"".equalsIgnoreCase(applicantID) && !"null".equalsIgnoreCase(applicantID)) 
			{
				add_xml_str = add_xml_str + "<InternalBouncedCheques><applicant_id>" + applicantID + "</applicant_id>";
				add_xml_str = add_xml_str + "<internal_bounced_cheques_id>" + internal_bounced_cheques_id + "</internal_bounced_cheques_id>";
				if ("ICCS".equalsIgnoreCase(bouncedCheq)) {
					add_xml_str = add_xml_str + "<bounced_cheque>" + "1"+ "</bounced_cheque>";
				}
				add_xml_str = add_xml_str + "<cheque_no>" + chequeNo + "</cheque_no>";
				add_xml_str = add_xml_str + "<amount>" + amount + "</amount>";
				add_xml_str = add_xml_str + "<reason>" + reason + "</reason>";
				add_xml_str = add_xml_str + "<return_date>" + returnDate + "</return_date>";
				add_xml_str = add_xml_str + "<provider_no>" + "RAKBANK" + "</provider_no>";
				if ("DDS".equalsIgnoreCase(bouncedCheq)) {
					add_xml_str = add_xml_str + "<bounced_cheque_dds>" + "1"+ "</bounced_cheque_dds>";
				}
				add_xml_str = add_xml_str + "<company_flag>N</company_flag></InternalBouncedCheques>";
			}

		}
		CreditCard.mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "
				+ add_xml_str);
		return add_xml_str;
	}
	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : Internal Bureau pipeline Products       

	 ***********************************************************************************  */
	public String InternalBureauPipelineProducts() {
		CreditCard.mLogger.info("RLOSCommon java file"+"inside InternalBureauPipelineProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "SELECT cifid,LoanType,custroletype,lastupdatedate,totalamount,totalnoofinstalments,totalloanamount,agreementId,NoOfDaysInPipeline,case when Consider_For_Obligations is null or Consider_For_Obligations='True' then 'Y' else 'N' end as Consider_For_Obligations,NextInstallmentAmt FROM ng_RLOS_CUSTEXPOSE_LoanDetails  with (nolock) where Wi_Name = '"
				+ formObject.getWFWorkitemName()
				+ "' and  LoanStat = 'CAS-Pipeline'";
		//CreditCard.mLogger.info("InternalBureauPipelineProducts sQuery" + sQuery+"");
		String add_xml_str = "";
		try{
			List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
			//CreditCard.mLogger.info("InternalBureauPipelineProducts list size"+ OutputXML.size()+ "");

			for (int i = 0; i < OutputXML.size(); i++) {

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
				if (!(OutputXML.get(i).get(0) == null || OutputXML.get(i).get(0).equals(""))) {
					cifId = OutputXML.get(i).get(0).toString();
				}
				if (!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1).equals(""))) {
					Product = OutputXML.get(i).get(1).toString();
				}
				if (!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2).equals(""))) {
				}
				if (!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3).equals(""))) {
					lastUpdateDate = OutputXML.get(i).get(3).toString();
				}
				if (!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4).equals(""))) {
					TotAmount = OutputXML.get(i).get(4).toString();
				}
				if (!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5).equals(""))) {
					TotNoOfInstlmnt = OutputXML.get(i).get(5).toString();
				}
				if (!(OutputXML.get(i).get(6) == null || OutputXML.get(i).get(6).equals(""))) {
					TotLoanAmt = OutputXML.get(i).get(6).toString();
				}
				if (!(OutputXML.get(i).get(7) == null || OutputXML.get(i).get(7).equals(""))) {
					agreementId = OutputXML.get(i).get(7).toString();
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
				if (cifId != null && !"".equalsIgnoreCase(cifId)&& !"null".equalsIgnoreCase(cifId)) 
				{
					add_xml_str = add_xml_str + "<InternalBureauPipelineProducts>";
					add_xml_str = add_xml_str + "<applicant_id>" + cifId + "</applicant_id>";
					add_xml_str = add_xml_str + "<internal_bureau_pipeline_products_id>" + agreementId + "</internal_bureau_pipeline_products_id>";// to be
					// populated
					// later
					add_xml_str = add_xml_str + "<ppl_provider_no>RAKBANK</ppl_provider_no>";
					//add_xml_str = add_xml_str + "<ppl_product>" + Product + "</ppl_product>";
					
					
					add_xml_str = add_xml_str + "<ppl_type_of_contract>" + Product + "</ppl_type_of_contract>";
					add_xml_str = add_xml_str + "<ppl_phase>PIPELINE</ppl_phase>"; // to
					// be
					// populated
					// later

					add_xml_str = add_xml_str + "<ppl_role>Primary</ppl_role>";
					add_xml_str = add_xml_str + "<ppl_date_of_last_update>" + lastUpdateDate + "</ppl_date_of_last_update>";
					add_xml_str = add_xml_str + "<ppl_total_amount>" + TotAmount + "</ppl_total_amount>";
					add_xml_str = add_xml_str + "<ppl_no_of_instalments>" + TotNoOfInstlmnt + "</ppl_no_of_instalments>";
					add_xml_str = add_xml_str + "<ppl_credit_limit>" + TotLoanAmt + "</ppl_credit_limit>";

					add_xml_str = add_xml_str + "<ppl_no_of_days_in_pipeline>"+NoOfDaysInPipeline+"</ppl_no_of_days_in_pipeline>";
					add_xml_str = add_xml_str + "<company_flag>N</company_flag>";
					add_xml_str = add_xml_str + "<ppl_consider_for_obligation>"+ConsiderForOblig+"</ppl_consider_for_obligation>";
					add_xml_str = add_xml_str + "<ppl_emi>"+EMI+"</ppl_emi>";
					add_xml_str = add_xml_str + "</InternalBureauPipelineProducts>"; // to be populated later
				}

			}
			CreditCard.mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "
					+ add_xml_str);
		}catch(Exception e){
			CreditCard.mLogger.info("RLOSCommon"+
					"Exception occurred in InternalBureauPipelineProducts()"+ e.getMessage() + "\n Error: "+ CC_Common.printException(e));
		}
		return add_xml_str;
	}
	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : external bureau data      

	 ***********************************************************************************  */
	public String ExternalBureauData() 
	{
		CreditCard.mLogger.info("RLOSCommon java file"+"inside ExternalBureauData : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "select  CifId,fullnm,TotalOutstanding,TotalOverdue,NoOfContracts,Total_Exposure,WorstCurrentPaymentDelay,Worst_PaymentDelay_Last24Months,Worst_Status_Last24Months,Nof_Records,NoOf_Cheque_Return_Last3,Nof_DDES_Return_Last3Months,Nof_Cheque_Return_Last6,DPD30_Last6Months,(select max(ExternalWriteOffCheck) ExternalWriteOffCheck from ((select convert(int,isNULL(ExternalWriteOffCheck,0)) ExternalWriteOffCheck  from ng_rlos_cust_extexpo_CardDetails with(nolock) where Child_wi  = '"+formObject.getWFWorkitemName()+"' and ProviderNo!='B01'  union select convert(int,isNULL(ExternalWriteOffCheck,0))ExternalWriteOffCheck    from ng_rlos_cust_extexpo_LoanDetails where Child_wi  = '"+formObject.getWFWorkitemName()+"' and ProviderNo!='B01')) as ExternalWriteOffCheck),(select count(*) from (select DisputeAlert from ng_rlos_cust_extexpo_LoanDetails with(nolock) where Child_wi = '"+formObject.getWFWorkitemName()+"' and DisputeAlert='1' union  select DisputeAlert from ng_rlos_cust_extexpo_CardDetails with(nolock) where Child_wi = '"+formObject.getWFWorkitemName()+"' and DisputeAlert='1') as tempTable)  from NG_rlos_custexpose_Derived with (nolock) where Child_wi  = '"+ formObject.getWFWorkitemName()+ "' and Request_type= 'ExternalExposure'";
		//CreditCard.mLogger.info("ExternalBureauData sQuery" + sQuery+ "");
		String Wi_Name=formObject.getWFWorkitemName();
		String AecbHistQuery = "select isnull(max(AECBHistMonthCnt),0) as AECBHistMonthCnt from ( select MAX(cast(isnull(AECBHistMonthCnt,'0') as int)) as AECBHistMonthCnt  from ng_rlos_cust_extexpo_CardDetails with (nolock) where  Child_wi  = '"+ Wi_Name + "' union select Max(cast(isnull(AECBHistMonthCnt,'0') as int)) as AECBHistMonthCnt from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Child_wi  = '"+ Wi_Name + "') as ext_expo";
		String add_xml_str = "";
		try {
			List<List<String>> OutputXML = formObject
					.getDataFromDataSource(sQuery);
			//CreditCard.mLogger.info("ExternalBureauData list size" + OutputXML.size()+ "");
			List<List<String>> AecbHistQueryData = formObject
					.getDataFromDataSource(AecbHistQuery);

			if (OutputXML.size()==0) 
			{
				String CifId = formObject.getNGValue("cmplx_Customer_CIFNO");
				String fullnm = formObject.getNGValue("cmplx_Customer_FIrstNAme") + " " + formObject.getNGValue("cmplx_Customer_LAstNAme");
				String TotalOutstanding = "";
				String TotalOverdue = "";
				String NoOfContracts = "";
				String Total_Exposure = "";
				String WorstCurrentPaymentDelay = "";
				String Worst_PaymentDelay_Last24Months = "";
				String Worst_Status_Last24Months = "";
				String Nof_Records = "";
				String NoOf_Cheque_Return_Last3 = "";
				String Nof_DDES_Return_Last3Months = "";
				String Nof_Cheque_Return_Last6 = "";
				String DPD30_Last6Months = "";
				add_xml_str = add_xml_str + "<ExternalBureau>";
				add_xml_str = add_xml_str + "<applicant_id>" + CifId + "</applicant_id>";

				add_xml_str = add_xml_str + "<full_name>" + fullnm + "</full_name>";
				add_xml_str = add_xml_str + "<total_out_bal>" + TotalOutstanding + "</total_out_bal>";

				add_xml_str = add_xml_str + "<total_overdue>" + TotalOverdue + "</total_overdue>";
				add_xml_str = add_xml_str + "<no_default_contract>" + NoOfContracts + "</no_default_contract>";
				add_xml_str = add_xml_str + "<total_exposure>" + Total_Exposure + "</total_exposure>";
				add_xml_str = add_xml_str + "<worst_curr_pay>" + WorstCurrentPaymentDelay + "</worst_curr_pay>";
				add_xml_str = add_xml_str + "<worst_curr_pay_24>" + Worst_PaymentDelay_Last24Months + "</worst_curr_pay_24>";
				add_xml_str = add_xml_str + "<worst_status_24>" + Worst_Status_Last24Months + "</worst_status_24>";

				add_xml_str = add_xml_str + "<no_of_rec>" + Nof_Records + "</no_of_rec>";
				add_xml_str = add_xml_str + "<cheque_return_3mon>" + NoOf_Cheque_Return_Last3 + "</cheque_return_3mon>";
				add_xml_str = add_xml_str + "<dds_return_3mon>" + Nof_DDES_Return_Last3Months + "</dds_return_3mon>";
				add_xml_str = add_xml_str + "<cheque_return_6mon>" + Nof_Cheque_Return_Last6 + "</cheque_return_6mon>";
				add_xml_str = add_xml_str + "<dds_return_6mon>" + DPD30_Last6Months + "</dds_return_6mon>";
				add_xml_str = add_xml_str + "<prod_external_writeoff_amount>" + "" + "</prod_external_writeoff_amount>";

				add_xml_str = add_xml_str + "<no_months_aecb_history >" + AecbHistQueryData.get(0).get(0) + "</no_months_aecb_history >";

				add_xml_str = add_xml_str + "<company_flag>N</company_flag></ExternalBureau>";

				//CreditCard.mLogger.info("RLOSCommon"+"Internal liab tag Cration: " + add_xml_str);
				return add_xml_str;
			} 
			else {
				for (int i = 0; i < OutputXML.size(); i++) {

					String CifId = formObject	.getNGValue("cmplx_Customer_CIFNO");
					String fullnm = "";
					String TotalOutstanding = "";
					String TotalOverdue = "";
					String NoOfContracts = "";
					String Total_Exposure = "";
					String WorstCurrentPaymentDelay = "";
					String Worst_PaymentDelay_Last24Months = "";
					String Worst_Status_Last24Months = "";
					String Nof_Records = "";
					String NoOf_Cheque_Return_Last3 = "";
					String Nof_DDES_Return_Last3Months = "";
					String Nof_Cheque_Return_Last6 = "";
					String DPD30_Last6Months = "";
					String ExternalWriteOffCheck = "";
					String dispute_alert="";
					if (!(OutputXML.get(i).get(1) == null || OutputXML.get(i)	.get(1).equals(""))) {fullnm = OutputXML.get(i).get(1).toString();
					}
					if (!(OutputXML.get(i).get(2) == null || OutputXML.get(i)	.get(2).equals(""))) {TotalOutstanding = OutputXML.get(i).get(2).toString();

					}
					if (!(OutputXML.get(i).get(3) == null || OutputXML.get(i)	.get(3).equals(""))) {
						TotalOverdue = OutputXML.get(i).get(3).toString();
					}
					if (!(OutputXML.get(i).get(4) == null || OutputXML.get(i)	.get(4).equals(""))) {NoOfContracts = OutputXML.get(i).get(4).toString();
					}
					if (!(OutputXML.get(i).get(5) == null || OutputXML.get(i)	.get(5).equals(""))) {Total_Exposure = OutputXML.get(i).get(5).toString();
					}
					if (OutputXML.get(i).get(6) != null	&& !OutputXML.get(i).get(6).isEmpty()	&& !OutputXML.get(i).get(6).equals("")	&& !OutputXML.get(i).get(6).equalsIgnoreCase("null")) {WorstCurrentPaymentDelay = OutputXML.get(i).get(6).toString();
					}
					if (OutputXML.get(i).get(7) != null	&& !OutputXML.get(i).get(7).isEmpty()	&& !OutputXML.get(i).get(7).equals("")	&& !OutputXML.get(i).get(7).equalsIgnoreCase("null")) {Worst_PaymentDelay_Last24Months = OutputXML.get(i).get(		7).toString();
					}
					if (OutputXML.get(i).get(8) != null	&& !OutputXML.get(i).get(8).isEmpty()	&& !OutputXML.get(i).get(8).equals("")	&& !OutputXML.get(i).get(8).equalsIgnoreCase("null")) {Worst_Status_Last24Months = OutputXML.get(i).get(8).toString();
					}
					if (!(OutputXML.get(i).get(9) == null || OutputXML.get(i)	.get(9).equals(""))) {Nof_Records = OutputXML.get(i).get(9).toString();
					}
					if (!(OutputXML.get(i).get(10) == null || OutputXML.get(i)	.get(10).equals(""))) {NoOf_Cheque_Return_Last3 = OutputXML.get(i).get(10).toString();
					}
					if (!(OutputXML.get(i).get(11) == null || OutputXML.get(i)	.get(11).equals(""))) {Nof_DDES_Return_Last3Months = OutputXML.get(i).get(11).toString();
					}
					if (OutputXML.get(i).get(12) != null	&& !OutputXML.get(i).get(12).isEmpty()	&& !OutputXML.get(i).get(12).equals("")	&& !OutputXML.get(i).get(12).equalsIgnoreCase("null")) {// SKLogger.writeLog("Inside for",// "asdasdasd"+OutputXML.get(i).get(12));Nof_Cheque_Return_Last6 = OutputXML.get(i).get(12).toString();
					}
					if (!(OutputXML.get(i).get(13) == null || OutputXML.get(i)	.get(13).equals(""))) {DPD30_Last6Months = OutputXML.get(i).get(13).toString();
					}
					if(!(OutputXML.get(i).get(14) == null || "".equalsIgnoreCase(OutputXML.get(i).get(14))) &&!"0".equalsIgnoreCase(OutputXML.get(i).get(14)) ){
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
					add_xml_str = add_xml_str + "<applicant_id>" + CifId+ "</applicant_id>";

					add_xml_str = add_xml_str + "<full_name>" + fullnm+ "</full_name>";
					add_xml_str = add_xml_str + "<total_out_bal>"+ TotalOutstanding + "</total_out_bal>";

					add_xml_str = add_xml_str + "<total_overdue>"+ TotalOverdue + "</total_overdue>";
					add_xml_str = add_xml_str + "<no_default_contract>"+ NoOfContracts + "</no_default_contract>";
					add_xml_str = add_xml_str + "<total_exposure>"+ Total_Exposure + "</total_exposure>";
					add_xml_str = add_xml_str + "<worst_curr_pay>"+ WorstCurrentPaymentDelay + "</worst_curr_pay>";
					add_xml_str = add_xml_str + "<worst_curr_pay_24>"+ Worst_PaymentDelay_Last24Months+ "</worst_curr_pay_24>";
					add_xml_str = add_xml_str + "<worst_status_24>"+ Worst_Status_Last24Months + "</worst_status_24>";

					add_xml_str = add_xml_str + "<no_of_rec>" + Nof_Records+ "</no_of_rec>";
					add_xml_str = add_xml_str + "<cheque_return_3mon>"+ NoOf_Cheque_Return_Last3+ "</cheque_return_3mon>";
					add_xml_str = add_xml_str + "<dds_return_3mon>"+ Nof_DDES_Return_Last3Months+ "</dds_return_3mon>";
					add_xml_str = add_xml_str + "<cheque_return_6mon>"+ Nof_Cheque_Return_Last6 + "</cheque_return_6mon>";
					add_xml_str = add_xml_str + "<dds_return_6mon>"+ DPD30_Last6Months + "</dds_return_6mon>";
					add_xml_str = add_xml_str+ "<prod_external_writeoff_amount>" +ExternalWriteOffCheck+ "</prod_external_writeoff_amount>";

					add_xml_str = add_xml_str + "<no_months_aecb_history >"+ AecbHistQueryData.get(0).get(0)+ "</no_months_aecb_history >";

					add_xml_str = add_xml_str + "<company_flag>N</company_flag>";
					add_xml_str = add_xml_str + "<dispute_alert>"+dispute_alert+"</dispute_alert></ExternalBureau>";

				}
				CreditCard.mLogger.info("RLOSCommon"+"Internal liab tag Cration: " + add_xml_str);
				return add_xml_str;
			}
		}

		catch (Exception e) {
			CreditCard.mLogger.info("RLOSCommon"+
					"Exception occurred in externalBureauData()"+ e.getMessage() + "\n Error: "+ CC_Common.printException(e));
			return null;
		}
	}
	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : External bounced cheques      

	 ***********************************************************************************  */
	public String ExternalBouncedCheques() {
		CreditCard.mLogger.info("RLOSCommon java file"+"inside ExternalBouncedCheques : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "SELECT cifid,number,amount,reasoncode,returndate,providerno FROM ng_rlos_cust_extexpo_ChequeDetails  with (nolock) where child_wi = '"
				+ formObject.getWFWorkitemName()
				+ "' and Request_Type = 'ExternalExposure'";
		//CreditCard.mLogger.info("ExternalBouncedCheques sQuery" + sQuery+ "");
		String add_xml_str = "";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		//CreditCard.mLogger.info("ExternalBouncedCheques list size"+ OutputXML.size()+ "");

		for (int i = 0; i < OutputXML.size(); i++) {

			String CifId = formObject.getNGValue("cmplx_Customer_CIFNO");
			String chqNo = "";
			String Amount = "";
			String Reason = "";
			String returnDate = "";
			String providerNo = "";

			if (!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1)
					.equals(""))) {
				chqNo = OutputXML.get(i).get(1).toString();
			}
			if (!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2)
					.equals(""))) {
				Amount = OutputXML.get(i).get(2).toString();
			}
			if (!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3)
					.equals(""))) {
				Reason = OutputXML.get(i).get(3).toString();
			}
			if (!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4)
					.equals(""))) {
				returnDate = OutputXML.get(i).get(4).toString();
			}
			if (!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5)
					.equals(""))) {
				providerNo = OutputXML.get(i).get(5).toString();
			}

			add_xml_str = add_xml_str + "<ExternalBouncedCheques><applicant_id>" + CifId + "</applicant_id>";
			add_xml_str = add_xml_str + "<external_bounced_cheques_id>" + "" + "</external_bounced_cheques_id>";
			add_xml_str = add_xml_str + "<bounced_cheque>" + "" + "</bounced_cheque>";
			add_xml_str = add_xml_str + "<cheque_no>" + chqNo + "</cheque_no>";
			add_xml_str = add_xml_str + "<amount>" + Amount + "</amount>";
			add_xml_str = add_xml_str + "<reason>" + Reason + "</reason>";
			add_xml_str = add_xml_str + "<return_date>" + returnDate + "</return_date>"; // to be populated later
			add_xml_str = add_xml_str + "<provider_no>" + providerNo + "</provider_no><company_flag>N</company_flag></ExternalBouncedCheques>"; // to
			// be
			// populated
			// later

		}
		CreditCard.mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "
				+ add_xml_str);
		return add_xml_str;
	}
	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : External Bureau Individual Products       

	 ***********************************************************************************  */
	public String ExternalBureauIndividualProducts() {
		CreditCard.mLogger.info("RLOSCommon java file"+"inside ExternalBureauIndividualProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String sQuery = "select CifId,AgreementId,LoanType,ProviderNo,LoanStat,CustRoleType,LoanApprovedDate,LoanMaturityDate,OutstandingAmt,TotalAmt,PaymentsAmt,TotalNoOfInstalments,RemainingInstalments,WriteoffStat,WriteoffStatDt,CreditLimit,OverdueAmt,NofDaysPmtDelay,MonthsOnBook,lastrepmtdt,IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last12Months,AECBHistMonthCnt,DPD5_Last3Months,'' as qc_Amnt,'' as Qc_emi,'' as Cac_indicator,Take_Over_Indicator,Consider_For_Obligations,case when IsDuplicate= '1' then 'Y' else 'N' end,avg_utilization,DPD5_Last12Months,DPD60Plus_Last12Months from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Child_wi= '"+ formObject.getWFWorkitemName()
		+ "'  and LoanStat != 'Pipeline'   union select CifId,CardEmbossNum,CardType,ProviderNo,CardStatus,CustRoleType,StartDate,ClosedDate,CurrentBalance,'' as col6,PaymentsAmount,NoOfInstallments,'' as col5,WriteoffStat,WriteoffStatDt,CashLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,lastrepmtdt,IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last12Months,AECBHistMonthCnt,DPD5_Last3Months,qc_amt,qc_emi,CAC_Indicator,Take_Over_Indicator,Consider_For_Obligations,case when IsDuplicate= '1' then 'Y' else 'N' end,avg_utilization,DPD5_Last12Months,DPD60Plus_Last12Months from ng_rlos_cust_extexpo_CardDetails with (nolock) where Child_wi  =  '"
		+ formObject.getWFWorkitemName()+ "' and cardstatus != 'Pipeline'   union select CifId,AcctId,AcctType,ProviderNo,AcctStat,CustRoleType,StartDate,ClosedDate,OutStandingBalance,TotalAmount,PaymentsAmount,'','',WriteoffStat,WriteoffStatDt,CreditLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,'',IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last12Months,AECBHistMonthCnt,DPD5_Last3Months,'','','','',isnull(Consider_For_Obligations,'true'),case when IsDuplicate= '1' then 'Y' else 'N' end,'',DPD5_Last12Months,DPD60Plus_Last12Months from ng_rlos_cust_extexpo_AccountDetails with (nolock)  where child_wi  =  '"+formObject.getWFWorkitemName()+"' union"
		+" select CifId,ServiceID,ServiceType,ProviderNo,ServiceStat,CustRoleType,SubscriptionDt,SvcExpDt,'','','','','',WriteoffStat,WriteoffStatDt,'',OverDueAmount,NofDaysPmtDelay,MonthsOnBook,'',IsCurrent,CurUtilRate,'',DPD30_Last6Months,AECBHistMonthCnt,DPD5_Last3Months,'','','','',isnull(Consider_For_Obligations,'true'),case when IsDuplicate= '1' then 'Y' else 'N' end,'',DPD5_Last12Months,DPD60Plus_Last12Months from ng_rlos_cust_extexpo_ServicesDetails with (nolock)  where ServiceStat='Active' and child_wi  =  '"+formObject.getWFWorkitemName()+"'";
		
		//Deepak code commited to include service details in Dectech call
		/*String sQuery = "select CifId,AgreementId,LoanType,ProviderNo,LoanStat,CustRoleType,LoanApprovedDate,LoanMaturityDate,OutstandingAmt,TotalAmt,PaymentsAmt,TotalNoOfInstalments,RemainingInstalments,WriteoffStat,WriteoffStatDt,CreditLimit,OverdueAmt,NofDaysPmtDelay,MonthsOnBook,lastrepmtdt,IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last12Months,AECBHistMonthCnt,DPD5_Last3Months,'' as qc_Amnt,'' as Qc_emi,'' as Cac_indicator,Take_Over_Indicator,Consider_For_Obligations,case when IsDuplicate= '1' then 'Y' else 'N' end,avg_utilization from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Child_wi= '"
				+ formObject.getWFWorkitemName()
				+ "'  and LoanStat != 'Pipeline'  and ProviderNo != 'B01' union select CifId,CardEmbossNum,CardType,ProviderNo,CardStatus,CustRoleType,StartDate,ClosedDate,CurrentBalance,'' as col6,PaymentsAmount,NoOfInstallments,'' as col5,WriteoffStat,WriteoffStatDt,CashLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,lastrepmtdt,IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last12Months,AECBHistMonthCnt,DPD5_Last3Months,qc_amt,qc_emi,CAC_Indicator,Take_Over_Indicator,Consider_For_Obligations,case when IsDuplicate= '1' then 'Y' else 'N' end,avg_utilization from ng_rlos_cust_extexpo_CardDetails with (nolock) where Child_wi  =  '"
				+ formObject.getWFWorkitemName()
				+ "' and cardstatus != 'Pipeline'  and ProviderNo != 'B01' union select CifId,AcctId,AcctType,ProviderNo,AcctStat,CustRoleType,StartDate,ClosedDate,OutStandingBalance,TotalAmount,PaymentsAmount,'','',WriteoffStat,WriteoffStatDt,CreditLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,'',IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last12Months,AECBHistMonthCnt,DPD5_Last3Months,'','','','',isnull(Consider_For_Obligations,'true'),case when IsDuplicate= '1' then 'Y' else 'N' end,'' from ng_rlos_cust_extexpo_AccountDetails with (nolock)  where child_wi  =  '"+formObject.getWFWorkitemName()+"' ";//Overdraft removed as per PCAS-2066
		*/
		//Deepak below query corrected for PCAS-2362 to send provider no insted of account no in Accounts as well. 
		/*String sQuery = "select CifId,AgreementId,LoanType,ProviderNo,LoanStat,CustRoleType,LoanApprovedDate,LoanMaturityDate,OutstandingAmt,TotalAmt,PaymentsAmt,TotalNoOfInstalments,RemainingInstalments,WriteoffStat,WriteoffStatDt,CreditLimit,OverdueAmt,NofDaysPmtDelay,MonthsOnBook,lastrepmtdt,IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last12Months,AECBHistMonthCnt,DPD5_Last3Months,'' as qc_Amnt,'' as Qc_emi,'' as Cac_indicator,Take_Over_Indicator,Consider_For_Obligations,case when IsDuplicate= '1' then 'Y' else 'N' end,avg_utilization from ng_rlos_cust_extexpo_LoanDetails with (nolock) where Child_wi= '"
				+ formObject.getWFWorkitemName()
				+ "'  and LoanStat != 'Pipeline'  and ProviderNo != 'B01' union select CifId,CardEmbossNum,CardType,ProviderNo,CardStatus,CustRoleType,StartDate,ClosedDate,CurrentBalance,'' as col6,PaymentsAmount,NoOfInstallments,'' as col5,WriteoffStat,WriteoffStatDt,CashLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,lastrepmtdt,IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last12Months,AECBHistMonthCnt,DPD5_Last3Months,qc_amt,qc_emi,CAC_Indicator,Take_Over_Indicator,Consider_For_Obligations,case when IsDuplicate= '1' then 'Y' else 'N' end,avg_utilization from ng_rlos_cust_extexpo_CardDetails with (nolock) where Child_wi  =  '"
				+ formObject.getWFWorkitemName()
				+ "' and cardstatus != 'Pipeline'  and ProviderNo != 'B01' union select CifId,AcctId,AcctType,AcctId,AcctStat,CustRoleType,StartDate,ClosedDate,OutStandingBalance,TotalAmount,PaymentsAmount,'','',WriteoffStat,WriteoffStatDt,CreditLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,'',IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last12Months,AECBHistMonthCnt,DPD5_Last3Months,'','','','',isnull(Consider_For_Obligations,'true'),case when IsDuplicate= '1' then 'Y' else 'N' end,'' from ng_rlos_cust_extexpo_AccountDetails with (nolock)  where child_wi  =  '"+formObject.getWFWorkitemName()+"' ";//Overdraft removed as per PCAS-2066
		*/
		//+ "' and cardstatus != 'Pipeline'  and ProviderNo != 'B01' union select CifId,AcctId,AcctType,AcctId,AcctStat,CustRoleType,StartDate,ClosedDate,OutStandingBalance,TotalAmount,PaymentsAmount,'','',WriteoffStat,WriteoffStatDt,CreditLimit,OverdueAmount,NofDaysPmtDelay,MonthsOnBook,'',IsCurrent,CurUtilRate,DPD30_Last6Months,DPD60_Last12Months,AECBHistMonthCnt,DPD5_Last3Months,'','','','',isnull(Consider_For_Obligations,'true'),case when IsDuplicate= '1' then 'Y' else 'N' end,'' from ng_rlos_cust_extexpo_AccountDetails with (nolock)  where AcctType='Overdraft' and child_wi  =  '"+formObject.getWFWorkitemName()+"' ";
		//CreditCard.mLogger.info("ExternalBureauIndividualProducts sQuery" + sQuery+ "");
		String add_xml_str = "";
		//added by aman for pcsp-111
				String CAC_BANK_NAME="";
				if ("Self Employed".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 6))){
					CAC_BANK_NAME=formObject.getNGValue("cmplx_Liability_New_CACBankName");
				}
				else{
					CAC_BANK_NAME=formObject.getNGValue("cmplx_EmploymentDetails_OtherBankCAC");
					}
				//added by aman for pcsp-111
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		//CreditCard.mLogger.info("ExternalBureauIndividualProducts list size"+ OutputXML.size()+ "");
		//String ReqProd = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 1);//commented by deepak as the same is of no use.
		for (int i = 0; i < OutputXML.size(); i++) {
//OtherBankCAC
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
			String consider_for_obligation = "";
			String Duplicate_flag="";
			String avg_utilization="";
			String DPD60plus_last12month="";
			String DPD5_last12month="";
			if (!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1)
					.equals(""))) {
				AgreementId = OutputXML.get(i).get(1).toString();
			}
			if (!(OutputXML.get(i).get(2) == null || OutputXML.get(i).get(2).equals(""))) {
				ContractType = OutputXML.get(i).get(2).toString();
				try {
					String cardquery = "select code from ng_master_contract_type with (nolock) where description='"+ ContractType + "'";
					//CreditCard.mLogger.info("ExternalBureauIndividualProducts sQuery"+ cardquery+ "");
					List<List<String>> cardqueryXML = formObject.getNGDataFromDataCache(cardquery);
					ContractType = cardqueryXML.get(0).get(0);
					//CreditCard.mLogger.info("ExternalBureauIndividualProducts ContractType"+ ContractType+ "ContractType");
				} catch (Exception e) {
					CreditCard.mLogger.info("ExternalBureauIndividualProducts ContractType Exception"+ e+ "Exception");

					ContractType = OutputXML.get(i).get(2).toString();
				}
			}
			if (!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3)
					.equals(""))) {
				provider_no = OutputXML.get(i).get(3).toString();
			}
			if (!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4)
					.equals(""))) {
				phase = OutputXML.get(i).get(4).toString();
				if (phase.startsWith("A")) {
					phase = "A";
				} else {
					phase = "C";
				}
			}
			if (!(OutputXML.get(i).get(5) == null || OutputXML.get(i).get(5)
					.equals(""))) {
				CustRoleType = OutputXML.get(i).get(5).toString();
				String sQueryCustRoleType = "select code from ng_master_role_of_customer with(nolock) where Description='"+CustRoleType+"'";
				CreditCard.mLogger.info("CustRoleType"+sQueryCustRoleType);
				List<List<String>> sQueryCustRoleTypeXML = formObject.getNGDataFromDataCache(sQueryCustRoleType);
				try{
					if(sQueryCustRoleTypeXML!=null && sQueryCustRoleTypeXML.size()>0 && sQueryCustRoleTypeXML.get(0)!=null){
						CustRoleType=sQueryCustRoleTypeXML.get(0).get(0);
					}
				}
				catch(Exception e){
					CreditCard.mLogger.info("Exception occured at sQueryCombinedLimit for"+sQueryCustRoleTypeXML);

				}	}
			if (!(OutputXML.get(i).get(6) == null || OutputXML.get(i).get(6)
					.equals(""))) {
				start_date = OutputXML.get(i).get(6).toString();
			}
			if (!(OutputXML.get(i).get(7) == null || OutputXML.get(i).get(7)
					.equals(""))) {
				close_date = OutputXML.get(i).get(7).toString();
			}
			if (!(OutputXML.get(i).get(8) == null || OutputXML.get(i).get(8)
					.equals(""))) {
				OutStanding_Balance = OutputXML.get(i).get(8).toString();
			}
			if (!(OutputXML.get(i).get(9) == null || OutputXML.get(i).get(9)
					.equals(""))) {
				TotalAmt = OutputXML.get(i).get(9).toString();
			}
			if (!(OutputXML.get(i).get(10) == null || OutputXML.get(i).get(10)
					.equals(""))) {
				PaymentsAmt = OutputXML.get(i).get(10).toString();
			}
			if (!(OutputXML.get(i).get(11) == null || OutputXML.get(i).get(11)
					.equals(""))) {
				TotalNoOfInstalments = OutputXML.get(i).get(11).toString();
			}
			if (!(OutputXML.get(i).get(12) == null || OutputXML.get(i).get(12)
					.equals(""))) {
				RemainingInstalments = OutputXML.get(i).get(12).toString();
			}
			if (!(OutputXML.get(i).get(13) == null || OutputXML.get(i).get(13)
					.equals(""))) {
				WorstStatus = OutputXML.get(i).get(13).toString();
			}
			if (!(OutputXML.get(i).get(14) == null || OutputXML.get(i).get(14)
					.equals(""))) {
				WorstStatusDate = OutputXML.get(i).get(14).toString();
			}
			if (!(OutputXML.get(i).get(15) == null || OutputXML.get(i).get(15)
					.equals(""))) {
				CreditLimit = OutputXML.get(i).get(15).toString();
			}
			if (!(OutputXML.get(i).get(16) == null || OutputXML.get(i).get(16)
					.equals(""))) {
				OverdueAmt = OutputXML.get(i).get(16).toString();
			}
			if (!(OutputXML.get(i).get(17) == null || OutputXML.get(i).get(17)
					.equals(""))) {
				NofDaysPmtDelay = OutputXML.get(i).get(17).toString();
			}
			if (!(OutputXML.get(i).get(18) == null || OutputXML.get(i).get(18)
					.equals(""))) {
				MonthsOnBook = OutputXML.get(i).get(18).toString();
			}
			if (!(OutputXML.get(i).get(19) == null || OutputXML.get(i).get(19)
					.equals(""))) {
				last_repayment_date = OutputXML.get(i).get(19).toString();
			}
			if (!(OutputXML.get(i).get(20) == null || OutputXML.get(i).get(20)
					.equals(""))) {
				currently_current = OutputXML.get(i).get(20).toString();
			}
			if (!(OutputXML.get(i).get(21) == null || OutputXML.get(i).get(21)
					.equals(""))) {
				current_utilization = OutputXML.get(i).get(21).toString();
			}
			if (!(OutputXML.get(i).get(22) == null || OutputXML.get(i).get(22)
					.equals(""))) {
				DPD30Last6Months = OutputXML.get(i).get(22).toString();
			}
			if (!(OutputXML.get(i).get(23) == null || OutputXML.get(i).get(23)
					.equals(""))) {
				DPD60Last12Months = OutputXML.get(i).get(23).toString();
			}
			if (!(OutputXML.get(i).get(24) == null || OutputXML.get(i).get(24)
					.equals(""))) {
				AECBHistMonthCnt = OutputXML.get(i).get(24).toString();
			}

			if (!(OutputXML.get(i).get(25) == null || OutputXML.get(i).get(25)
					.equals(""))) {
				delinquent_in_last_3months = OutputXML.get(i).get(25).toString();
			}
			if (!(OutputXML.get(i).get(26) == null || OutputXML.get(i).get(26)
					.equals(""))) {
				QC_Amt = OutputXML.get(i).get(26).toString();
			}
			if (!(OutputXML.get(i).get(27) == null || OutputXML.get(i).get(27)
					.equals(""))) {
				QC_emi = OutputXML.get(i).get(27).toString();
			}
			if (!(OutputXML.get(i).get(28) == null || OutputXML.get(i).get(28)
					.equals(""))) {
				CAC_Indicator = OutputXML.get(i).get(28).toString();
				if (CAC_Indicator != null && !("".equalsIgnoreCase(CAC_Indicator))) {
					if ("true".equalsIgnoreCase(CAC_Indicator)) 
					{
						CAC_Indicator = "Y";
					} 
					else 
					{
						CAC_Indicator = "N";
					}
				}
			}

			String TakeOverIndicator = "";
			if (!(OutputXML.get(i).get(29) == null || "".equals(OutputXML.get(i).get(29)))) {
				TakeOverIndicator = OutputXML.get(i).get(29).toString();
				if (TakeOverIndicator != null && !("".equalsIgnoreCase(TakeOverIndicator))) 
				{
					if ("true".equalsIgnoreCase(TakeOverIndicator)) 
					{
						TakeOverIndicator = "Y";
					} 
					else 
					{
						TakeOverIndicator = "N";
					}
				}
			}
			if (!(OutputXML.get(i).get(30) == null || "".equals(OutputXML.get(i).get(30))))
			{
				consider_for_obligation = OutputXML.get(i).get(30).toString();
				if (consider_for_obligation != null && !("".equalsIgnoreCase(consider_for_obligation))) {
					if ("true".equalsIgnoreCase(consider_for_obligation)) 
					{
						consider_for_obligation = "Y";
					} 
					else 
					{
						consider_for_obligation = "N";
					}
				}
			}
			if(!(OutputXML.get(i).get(31) == null || OutputXML.get(i).get(31).equals("")) ){
				Duplicate_flag = OutputXML.get(i).get(31).toString();
			}
			
			if(!(OutputXML.get(i).get(32) == null || OutputXML.get(i).get(32).equals("")) ){
				avg_utilization = OutputXML.get(i).get(32).toString();
			}
			if(!(OutputXML.get(i).get(33) == null || OutputXML.get(i).get(33).equals("")) ){
				DPD5_last12month = OutputXML.get(i).get(33).toString();
			}
			if(!(OutputXML.get(i).get(34) == null || OutputXML.get(i).get(34).equals("")) ){
				DPD60plus_last12month = OutputXML.get(i).get(34).toString();
			}

			add_xml_str = add_xml_str + "<ExternalBureauIndividualProducts><applicant_id>" + CifId + "</applicant_id>";
			add_xml_str = add_xml_str + "<external_bureau_individual_products_id>" + AgreementId + "</external_bureau_individual_products_id>";
			add_xml_str = add_xml_str + "<contract_type>" + ContractType + "</contract_type>";
			add_xml_str = add_xml_str + "<provider_no>" + provider_no + "</provider_no>";
			add_xml_str = add_xml_str + "<phase>" + phase + "</phase>";
			add_xml_str = add_xml_str + "<role_of_customer>" + CustRoleType + "</role_of_customer>";
			add_xml_str = add_xml_str + "<start_date>" + start_date + "</start_date>";

			add_xml_str = add_xml_str + "<close_date>" + close_date + "</close_date>";
			add_xml_str = add_xml_str + "<outstanding_balance>" + OutStanding_Balance + "</outstanding_balance>";
			add_xml_str = add_xml_str + "<total_amount>" + TotalAmt + "</total_amount>";
			add_xml_str = add_xml_str + "<payments_amount>" + PaymentsAmt + "</payments_amount>";
			add_xml_str = add_xml_str + "<total_no_of_instalments>" + TotalNoOfInstalments + "</total_no_of_instalments>";
			add_xml_str = add_xml_str + "<no_of_remaining_instalments>" + RemainingInstalments + "</no_of_remaining_instalments>";
			add_xml_str = add_xml_str + "<worst_status>" + WorstStatus + "</worst_status>";
			add_xml_str = add_xml_str + "<worst_status_date>" + WorstStatusDate + "</worst_status_date>";

			add_xml_str = add_xml_str + "<credit_limit>" + CreditLimit + "</credit_limit>";
			add_xml_str = add_xml_str + "<overdue_amount>" + OverdueAmt + "</overdue_amount>";
			add_xml_str = add_xml_str + "<no_of_days_payment_delay>" + NofDaysPmtDelay + "</no_of_days_payment_delay>";
			add_xml_str = add_xml_str + "<mob>" + MonthsOnBook + "</mob>";
			add_xml_str = add_xml_str + "<last_repayment_date>" + last_repayment_date + "</last_repayment_date>";
			if (currently_current != null && "1".equalsIgnoreCase(currently_current))
			{
				add_xml_str = add_xml_str + "<currently_current>Y</currently_current>";
			}
			else 
			{
				add_xml_str = add_xml_str + "<currently_current>N</currently_current>";
			}
		
			add_xml_str = add_xml_str + "<current_utilization>" + current_utilization + "</current_utilization>";
			add_xml_str = add_xml_str + "<dpd_30_last_6_mon>" + DPD30Last6Months + "</dpd_30_last_6_mon>";

			add_xml_str = add_xml_str + "<dpd_60p_in_last_12_mon>" + DPD60plus_last12month + "</dpd_60p_in_last_12_mon>";
			add_xml_str = add_xml_str + "<dpd_5_in_last_12_mon>" + DPD5_last12month + "</dpd_5_in_last_12_mon>";
			add_xml_str = add_xml_str + "<no_months_aecb_history>" + AECBHistMonthCnt + "</no_months_aecb_history>";
			add_xml_str = add_xml_str + "<delinquent_in_last_3months>" + delinquent_in_last_3months + "</delinquent_in_last_3months>";
			add_xml_str = add_xml_str + "<clean_funded>" + "" + "</clean_funded>";
			add_xml_str = add_xml_str + "<cac_indicator>" + CAC_Indicator + "</cac_indicator>";
			add_xml_str = add_xml_str + "<qc_emi>" + QC_emi + "</qc_emi>";
			add_xml_str = add_xml_str + "<qc_amount>" + QC_Amt + "</qc_amount><company_flag>N</company_flag><cac_bank_name>" + CAC_BANK_NAME+ "</cac_bank_name><take_over_indicator>" + TakeOverIndicator + "</take_over_indicator><consider_for_obligation>" + consider_for_obligation + "</consider_for_obligation><duplicate_flag>"+Duplicate_flag+"</duplicate_flag><avg_utilization>"+avg_utilization+"</avg_utilization></ExternalBureauIndividualProducts>";


		}
		//CreditCard.mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "	+ add_xml_str);
		return add_xml_str;
	}
	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         :External Bureau Manual Add Individual Products       

	 ***********************************************************************************  */
	public String ExternalBureauManualAddIndividualProducts() 
	{
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		CreditCard.mLogger.info("ExternalBureauMp.anualAddIndividualProducts sQuery"+ "");
		int Man_liab_row_count = formObject.getLVWRowCount("cmplx_Liability_New_cmplx_LiabilityAdditionGrid");
		String applicant_id = formObject.getNGValue("cmplx_Customer_CIFNO");
		String add_xml_str = "";
		//added by aman for pcsp-111
				String CAC_BANK_NAME="";
				if ("Self Employed".equalsIgnoreCase(formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 6))){
					CAC_BANK_NAME=formObject.getNGValue("cmplx_Liability_New_CACBankName");
				}
				else{
					CAC_BANK_NAME=formObject.getNGValue("cmplx_EmploymentDetails_OtherBankCAC");
					}
				//added by aman for pcsp-111
		Date currentDate=new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String modifiedDate= sdf.format(currentDate);
		String close_date= CC_Common.plusyear(modifiedDate,4,0,0);
		CreditCard.mLogger.info("ExternalBureauIndividualProducts list size"+ Man_liab_row_count+ "");
		if (Man_liab_row_count != 0) {
			for (int i = 0; i < Man_liab_row_count; i++) {
				String Type_of_Contract = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",i, 0); // 0
				String Limit = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",	i, 1); // 0
				String EMI = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",i, 2); // 0
				String Take_over_Indicator = ("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",i, 3)) ? "Y" : "N"); // 0
				String cac_Indicator = ("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",i, 5)) ? "Y" : "N"); // 0
				String Qc_amt = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",i, 6); // 0
				String Qc_Emi = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",i, 7); // 0
				String worst_status=formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",i, 17);
				String consider_for_obligation = ("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",i, 8)) ? "Y" : "N"); // 0
				String Application_type="L";
				// String MOB =
				// formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid",
				// i, 9); //0
				String Utilization = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i,11); // 0
				String OutStanding = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i,12); // 0
				String mob = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i,10);
				String avg_utilization = formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i, 20);
				String delinquent_in_last_3months = ("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i,13)) ? "1" : "0");
				String dpd_30_last_6_mon = ("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i,14)) ? "1" : "0");
				String dpd_60p_in_last_12_mon = ("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Liability_New_cmplx_LiabilityAdditionGrid", i,15)) ? "1" : "0");
				String query="select App_Type from NG_MASTER_contract_type with(nolock) where Code ='"+Type_of_Contract+"'";
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
					CreditCard.mLogger.info(Ex.getMessage());
				}
				add_xml_str = add_xml_str + "<ExternalBureauIndividualProducts><applicant_id>" + applicant_id + "</applicant_id>";
				add_xml_str = add_xml_str + "<external_bureau_individual_products_id></external_bureau_individual_products_id>";
				add_xml_str = add_xml_str + "<contract_type>" + Type_of_Contract + "</contract_type>";
				add_xml_str = add_xml_str + "<provider_no></provider_no>";
				add_xml_str = add_xml_str + "<phase>A</phase>";
				add_xml_str = add_xml_str + "<role_of_customer>A</role_of_customer>";
				add_xml_str = add_xml_str + "<start_date>" + modifiedDate + "</start_date>";

				add_xml_str = add_xml_str + "<close_date>"+close_date+"</close_date>";
				add_xml_str = add_xml_str + "<outstanding_balance>" + OutStanding + "</outstanding_balance>";
				if (Application_type.equalsIgnoreCase("L")){
					add_xml_str = add_xml_str + "<total_amount>"+Limit+"</total_amount>";
				}
				add_xml_str = add_xml_str + "<payments_amount>"+EMI+"</payments_amount>";
				add_xml_str = add_xml_str + "<total_no_of_instalments></total_no_of_instalments>";
				add_xml_str = add_xml_str + "<no_of_remaining_instalments></no_of_remaining_instalments>";
				add_xml_str = add_xml_str + "<worst_status>"+worst_status+"</worst_status>";
				add_xml_str = add_xml_str + "<worst_status_date></worst_status_date>";
				if (Application_type.equalsIgnoreCase("C")){
					add_xml_str = add_xml_str + "<credit_limit>"+Limit+"</credit_limit>";
				}
				add_xml_str = add_xml_str + "<overdue_amount></overdue_amount>";
				add_xml_str = add_xml_str + "<no_of_days_payment_delay></no_of_days_payment_delay>";
				add_xml_str = add_xml_str + "<mob>" + mob + "</mob>";
				add_xml_str = add_xml_str + "<last_repayment_date></last_repayment_date>";

				add_xml_str = add_xml_str + "<currently_current>N</currently_current>";

				add_xml_str = add_xml_str + "<current_utilization>" + Utilization + "</current_utilization>";
				add_xml_str = add_xml_str + "<dpd_30_last_6_mon>" + dpd_30_last_6_mon + "</dpd_30_last_6_mon>";

				add_xml_str = add_xml_str + "<dpd_60p_in_last_12_mon>" + dpd_60p_in_last_12_mon + "</dpd_60p_in_last_12_mon>";
				add_xml_str = add_xml_str + "<no_months_aecb_history></no_months_aecb_history>";
				add_xml_str = add_xml_str + "<delinquent_in_last_3months>" + delinquent_in_last_3months + "</delinquent_in_last_3months>";
				add_xml_str = add_xml_str + "<clean_funded>" + "" + "</clean_funded>";
				add_xml_str = add_xml_str + "<cac_indicator>" + cac_Indicator + "</cac_indicator>";
				add_xml_str = add_xml_str + "<qc_emi>" + Qc_Emi + "</qc_emi>";
				add_xml_str = add_xml_str + "<qc_amount>" + Qc_amt + "</qc_amount><cac_bank_name>" + CAC_BANK_NAME + "</cac_bank_name><take_over_indicator>" + Take_over_Indicator + "</take_over_indicator><company_flag>N</company_flag><consider_for_obligation>" + consider_for_obligation + "</consider_for_obligation><duplicate_flag>N</duplicate_flag><avg_utilization>"+avg_utilization+"</avg_utilization></ExternalBureauIndividualProducts>";

			}

		}
		CreditCard.mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "	+ add_xml_str);
		return add_xml_str;
	}
	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : External bureau Pipeline Products       

	 ***********************************************************************************  */
	public String ExternalBureauPipelineProducts() {
		CreditCard.mLogger.info("RLOSCommon java file"+"inside ExternalBureauPipelineProducts : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		//id changed by nikhil for PCSP-822
		String sQuery = "select AgreementId,ProviderNo,LoanType,LoanDesc,CustRoleType,Datelastupdated,TotalAmt,TotalNoOfInstalments,'' as col1,NoOfDaysInPipeline,isnull(Consider_For_Obligations,'true'),case when IsDuplicate= '1' then 'Y' else 'N' end from ng_rlos_cust_extexpo_LoanDetails with (nolock) where child_wi  =  '"
				+ formObject.getWFWorkitemName()
				+ "' and LoanStat = 'Pipeline' union select CardEmbossNum,ProviderNo,CardType,CardTypeDesc,CustRoleType,LastUpdateDate,'' as col2,NoOfInstallments,TotalAmount,NoOfDaysInPipeLine,isnull(Consider_For_Obligations,'true'),case when IsDuplicate= '1' then 'Y' else 'N' end  from ng_rlos_cust_extexpo_CardDetails with (nolock) where child_wi  =  '"
				+ formObject.getWFWorkitemName()
				+ "' and cardstatus = 'Pipeline'";
		//CreditCard.mLogger.info("ExternalBureauPipelineProducts sQuery" + sQuery+"");
		String add_xml_str = "";
		List<List<String>> OutputXML = formObject.getDataFromDataSource(sQuery);
		//CreditCard.mLogger.info("ExternalBureauPipelineProducts list size"+ OutputXML.size()+ "");
		String cifId = formObject.getNGValue("cmplx_Customer_CIFNO");

		for (int i = 0; i < OutputXML.size(); i++) {

			String agreementID = "";
			String ProviderNo = "";
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

			if (!(OutputXML.get(i).get(0) == null || OutputXML.get(i).get(0)
					.equals(""))) {
				agreementID = OutputXML.get(i).get(0).toString();
			}
			if (!(OutputXML.get(i).get(1) == null || OutputXML.get(i).get(1)
					.equals(""))) {
				ProviderNo = OutputXML.get(i).get(1).toString();
			}
			if (OutputXML.get(i).get(2) != null	&& !OutputXML.get(i).get(2).isEmpty() && !"".equals(OutputXML.get(i).get(2)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(2))) 
			{
				contractType = OutputXML.get(i).get(2).toString();
				//below code added by nikhil for PCSP-822
				try {
					String cardquery = "select code from ng_master_contract_type with (nolock) where description='"+ contractType + "'";
					//CreditCard.mLogger.info("ExternalBureauIndividualProducts sQuery"+ cardquery+ "");
					List<List<String>> cardqueryXML = formObject.getNGDataFromDataCache(cardquery);
					contractType = cardqueryXML.get(0).get(0);
					//CreditCard.mLogger.info("ExternalBureauIndividualProducts ContractType"+ ContractType+ "ContractType");
				} catch (Exception e) {
					CreditCard.mLogger.info("ExternalBureauIndividualProducts ContractType Exception"+ e+ "Exception");

					contractType = OutputXML.get(i).get(2).toString();
				}
			}
			if (!(OutputXML.get(i).get(3) == null || OutputXML.get(i).get(3)
					.equals(""))) {
				productType = OutputXML.get(i).get(3).toString();
			}
			
			//commented by nikhil for PCSP-822
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
			
			
			if (!(OutputXML.get(i).get(4) == null || OutputXML.get(i).get(4)
					.equals(""))) {
				role = OutputXML.get(i).get(4).toString();
				//added by nikhil for PCSP-822
				String sQueryCustRoleType = "select code from ng_master_role_of_customer with(nolock) where Description='"+role+"'";
				CreditCard.mLogger.info("CustRoleType"+sQueryCustRoleType);
				List<List<String>> sQueryCustRoleTypeXML = formObject.getNGDataFromDataCache(sQueryCustRoleType);
				try{
					if(sQueryCustRoleTypeXML!=null && sQueryCustRoleTypeXML.size()>0 && sQueryCustRoleTypeXML.get(0)!=null){
						role=sQueryCustRoleTypeXML.get(0).get(0);
					}
				}
				catch(Exception e){
					CreditCard.mLogger.info("Exception occured at sQueryCombinedLimit for"+sQueryCustRoleTypeXML);
					role = OutputXML.get(i).get(4).toString();
				}
			}
			if (OutputXML.get(i).get(5) != null	&& !OutputXML.get(i).get(5).isEmpty() && !"".equals(OutputXML.get(i).get(5)) && !"null".equalsIgnoreCase(OutputXML.get(i).get(5))) 
			{
				lastUpdateDate = OutputXML.get(i).get(5).toString();
			}
			if (OutputXML.get(i).get(6) != null	&& !OutputXML.get(i).get(6).isEmpty() && !OutputXML.get(i).get(6).equals("")	&& !"null".equalsIgnoreCase(OutputXML.get(i).get(6))) {
				TotAmt = OutputXML.get(i).get(6).toString();
			}
			if (!(OutputXML.get(i).get(7) == null || OutputXML.get(i).get(7).equals(""))) 
			{
				noOfInstalmnt = OutputXML.get(i).get(7).toString();
			}
			if (!(OutputXML.get(i).get(8) == null || OutputXML.get(i).get(8).equals(""))) 
			{
				creditLimit = OutputXML.get(i).get(8).toString();
			}
			if (OutputXML.get(i).get(9) != null	&& !OutputXML.get(i).get(9).isEmpty() && !OutputXML.get(i).get(9).equals("")&& !"null".equalsIgnoreCase(OutputXML.get(i).get(9))) 
			{
				noOfDayinPpl = OutputXML.get(i).get(9).toString();
			}
			if(!(OutputXML.get(i).get(10) == null || "".equals(OutputXML.get(i).get(10))) ){
				consider_for_obligation = OutputXML.get(i).get(10);
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
			add_xml_str = add_xml_str + "<ExternalBureauPipelineProducts><applicant_ID>" + cifId + "</applicant_ID>";
			add_xml_str = add_xml_str + "<external_bureau_pipeline_products_id>" + agreementID + "</external_bureau_pipeline_products_id>";
			add_xml_str = add_xml_str + "<ppl_provider_no>" + ProviderNo + "</ppl_provider_no>";
			add_xml_str = add_xml_str + "<ppl_type_of_contract>" + contractType + "</ppl_type_of_contract>";
			add_xml_str = add_xml_str + "<ppl_type_of_product>" + productType + "</ppl_type_of_product>";
			add_xml_str = add_xml_str + "<ppl_phase>" + "PIPELINE" + "</ppl_phase>";
			add_xml_str = add_xml_str + "<ppl_role>" + role + "</ppl_role>";

			add_xml_str = add_xml_str + "<ppl_date_of_last_update>" + lastUpdateDate + "</ppl_date_of_last_update>";
			add_xml_str = add_xml_str + "<ppl_total_amount>" + TotAmt + "</ppl_total_amount>";
			add_xml_str = add_xml_str + "<ppl_no_of_instalments>" + noOfInstalmnt + "</ppl_no_of_instalments>";
			add_xml_str = add_xml_str + "<ppl_credit_limit>" + creditLimit + "</ppl_credit_limit>";
			//changed by nikhil PCSP-822
			add_xml_str = add_xml_str + "<ppl_no_of_days_in_pipeline>" + noOfDayinPpl + "</ppl_no_of_days_in_pipeline><company_flag>N</company_flag><ppl_consider_for_obligation>"+consider_for_obligation+"</ppl_consider_for_obligation><ppl_duplicate_flag>"+ppl_Duplicate_flag+"</ppl_duplicate_flag></ExternalBureauPipelineProducts>"; // to
			// be
			// populated
			// later

		}
		CreditCard.mLogger.info("RLOSCommon"+ "Internal liab tag Cration: "	+ add_xml_str);
		return add_xml_str;
	}
	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : get MQ InputXML      

	 ***********************************************************************************  */
	private static String getMQInputXML(String sessionID, String cabinetName,
			String wi_name, String ws_name, String userName,
			StringBuilder final_xml) {
		FormContext.getCurrentInstance().getFormConfig();

		StringBuffer strBuff = new StringBuffer();
		strBuff.append("<APMQPUTGET_Input>");
		strBuff.append("<SessionId>" + sessionID + "</SessionId>");
		strBuff.append("<EngineName>" + cabinetName + "</EngineName>");
		strBuff.append("<XMLHISTORY_TABLENAME>NG_CC_XMLLOG_HISTORY</XMLHISTORY_TABLENAME>");
		strBuff.append("<WI_NAME>" + wi_name + "</WI_NAME>");
		strBuff.append("<WS_NAME>" + ws_name + "</WS_NAME>");
		strBuff.append("<USER_NAME>" + userName + "</USER_NAME>");
		strBuff.append("<MQ_REQUEST_XML>");
		strBuff.append(final_xml);
		strBuff.append("</MQ_REQUEST_XML>");
		strBuff.append("</APMQPUTGET_Input>");
		CreditCard.mLogger.info("inside getOutputXMLValues"+ "getMQInputXML"+ strBuff.toString());
		return strBuff.toString();
	}
	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : DEDUP SUMMARY CUSTOM       

	 ***********************************************************************************  */


	private Map<String, String> DEDUP_SUMMARY_Custom(List<List<String>> DB_List,FormReference formObject,String callName) {

		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();

		try{
			for (List<String> mylist : DB_List) {

				for (int i = 0; i < 8; i++) {

					//CreditCard.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");
				String Form_controlstr = recordFileMap.get("form_control");
				if ("AddressDetails".equalsIgnoreCase(tag_name)	&& int_xml.containsKey(parent_tag)) {
					String xml_str = int_xml.get(parent_tag);
					CreditCard.mLogger.info("RLOS COMMON"+ " before adding address+ " + xml_str);
					xml_str = xml_str + getCustAddress_details(callName);
					//CreditCard.mLogger.info("RLOS COMMON"+ " after adding address+ " + xml_str);
					int_xml.put(parent_tag, xml_str);
				} else if ("MaritalStatus".equalsIgnoreCase(tag_name)) {
					String marrital_code = formObject.getNGValue("cmplx_Customer_MAritalStatus").substring(0, 1);
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<" + tag_name + ">" + marrital_code + "</" + tag_name + ">";

					//CreditCard.mLogger.info("RLOS COMMON"+ " after adding Minor flag+ " + xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				//Deepak Changes done for PCAS-2635 
				else if("PhoneValue".equalsIgnoreCase(tag_name)	&& "PhoneFax".equalsIgnoreCase(parent_tag)){
					String xml_str = int_xml.get(parent_tag);
					String tag_val = formObject.getNGValue(Form_controlstr);
					tag_val=tag_val.replace("00971", "+00971()");
					xml_str = xml_str + "<" + tag_name + ">" + tag_val + "</" + tag_name + ">";
					int_xml.put(parent_tag, xml_str);
				}
				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
				}
			}
		}
		catch(Exception e){
			CreditCard.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + "+e.getMessage());
		}
		return int_xml;
	}


	/*          Function Header:

	 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 22/11/2017              
	Author                              : Deepak              
	Description                         : Function for Dedupe Summary call    

	 ***********************************************************************************  */

	private Map<String, String> Non_Custom(List<List<String>> DB_List,FormReference formObject,String callName,String operationName) {


		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();

		//FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try{
			for (List<String> mylist : DB_List) {
				// for(int i=0;i<col_n.length();i++)
				for (int i = 0; i < 8; i++) {
					// CreditCard.mLogger.info("rec: "+records.item(rec));
					CreditCard.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				String tag_name =  recordFileMap.get("xmltag_name");
				String parent_tag =  recordFileMap.get("parent_tag_name");

				if(operationName.equalsIgnoreCase("PartMatch_CIF") && tag_name.equalsIgnoreCase("RCIFID")){
					String xml_str = int_xml.get(parent_tag);
					//CreditCard.mLogger.info("RLOS COMMON"+ " before adding PartMatch_CIF+ " + xml_str);
					xml_str = xml_str +"<"+tag_name+">"+ formObject.getNGValue("cmplx_PartMatch_cmplx_Partmatch_grid",formObject.getSelectedIndex("cmplx_PartMatch_cmplx_Partmatch_grid"),0)+"</"+tag_name+">";
					//CreditCard.mLogger.info("RLOS COMMON"+ " after adding PartMatch_CIF+ " + xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
				}
			}
		}
		catch(Exception e){
			CreditCard.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + "+e.getMessage());

		}

		return int_xml;
	}


	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : Blacklist details Custom       

	 ***********************************************************************************  */
	private Map<String, String> Blacklist_Details_custom(List<List<String>> DB_List,FormReference formObject,String callName) {

		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();

		try{
			for (List<String> mylist : DB_List) {

				for (int i = 0; i < 8; i++) {

					//CreditCard.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");


				if ("AddressDetails".equalsIgnoreCase(tag_name)	&& int_xml.containsKey(parent_tag)) {
					String xml_str = int_xml.get(parent_tag);
					//CreditCard.mLogger.info("RLOS COMMON"+ " before adding address+ " + xml_str);
					xml_str = xml_str + getCustAddress_details(callName);
					//CreditCard.mLogger.info("RLOS COMMON"+ " after adding address+ " + xml_str);
					int_xml.put(parent_tag, xml_str);
				} else if ("MaritalStatus".equalsIgnoreCase(tag_name)) 
				{
					String marrital_code = formObject.getNGValue("cmplx_Customer_MAritalStatus").substring(0, 1);
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<" + tag_name + ">" + marrital_code + "</" + tag_name + ">";

					//CreditCard.mLogger.info("RLOS COMMON"+ " after adding Minor flag+ " + xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
				}
			}
		}
		catch(Exception e){
			CreditCard.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + "+e.getMessage());
		}
		return int_xml;
	}
	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : new customer custom       

	 ***********************************************************************************  */
	private Map<String, String> NEW_CUSTOMER_Custom(List<List<String>> DB_List,FormReference formObject,String callName) {

		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();
		//CC_Common common = new CC_Common();
		try{
			for (List<String> mylist : DB_List) {

				for (int i = 0; i < 8; i++) {

					//CreditCard.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");
				//CreditCard.mLogger.info("RLOS COMMON parent_tag+ " + parent_tag);
				//CreditCard.mLogger.info("RLOS COMMON tag_name+ " + tag_name);

				if( "SUPPLEMENT".equalsIgnoreCase(CC_Comn.MultipleAppGridSelectedRow("MultipleApp_AppType"))){	
					CreditCard.mLogger.info("Inside NEW_CUSTOMER_Custom()"+ "Value of Applicant tYpe is: " + CC_Comn.MultipleAppGridSelectedRow("MultipleApp_AppType"));

					if("PersonDetails".equalsIgnoreCase(tag_name)){
						String xml_str = int_xml.get(parent_tag);
						//CreditCard.mLogger.info("RLOS COMMON"+ " before adding guarantor PersonDetails+ " + xml_str);
						xml_str = xml_str + getSupplement_PersonDetails(formObject,callName);
						//CreditCard.mLogger.info("RLOS COMMON"+ " after adding PersonDetails " + xml_str);
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
						//CreditCard.mLogger.info("RLOS COMMON"+ " before adding guarantor doc details " + xml_str);
						xml_str = xml_str + getSupplement_DocDetails(formObject,callName);
						//CreditCard.mLogger.info("RLOS COMMON"+ " after adding docDetails+ " + xml_str);
						int_xml.put(parent_tag, xml_str);

					}
					else if("PhoneNumber".equalsIgnoreCase(tag_name)){
						String xml_str = int_xml.get(parent_tag);
						//CreditCard.mLogger.info("RLOS COMMON"+ " before adding guarantor PhoneDetails" + xml_str);
						String ph_no="";
						if( "SUPPLEMENT".equalsIgnoreCase(CC_Comn.MultipleAppGridSelectedRow("MultipleApp_AppType")) && formObject.getLVWRowCount("SupplementCardDetails_cmplx_supplementGrid")>0){ 
							for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_supplementGrid");i++){
								if(CC_Comn.MultipleAppGridSelectedRow("MultipleApp_AppPass").equals(formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,3))){
									ph_no=formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,6);
								}
							}
						}
						xml_str = xml_str +  "<"+tag_name+">"+ph_no
								+"</"+ tag_name+">";
						//CreditCard.mLogger.info("RLOS COMMON"+ " after adding guarantor PhoneDetails" + xml_str);
						int_xml.put(parent_tag, xml_str);
					}

					else if("MailIdValue".equalsIgnoreCase(tag_name)){

						String xml_str = int_xml.get(parent_tag);
						String email="";
						//CreditCard.mLogger.info("RLOS COMMON"+ " before adding guarantor mail details " + xml_str);
						if( "SUPPLEMENT".equalsIgnoreCase(CC_Comn.MultipleAppGridSelectedRow("MultipleApp_AppType")) && formObject.getLVWRowCount("SupplementCardDetails_cmplx_supplementGrid")>0){ 
							for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_supplementGrid");i++){
								if(CC_Comn.MultipleAppGridSelectedRow("MultipleApp_AppPass").equals(formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,3))){
									email=formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,21);
									break;
								}
							}
						}
						xml_str = xml_str +  "<"+tag_name+">"+email
								+"</"+ tag_name+">";
						//CreditCard.mLogger.info("RLOS COMMON"+ " after adding guarantor MailIdValue" + xml_str);
						int_xml.put(parent_tag, xml_str);
					}

					else if ("AddressDetails".equalsIgnoreCase(tag_name)	&& int_xml.containsKey(parent_tag)) {
						String xml_str = int_xml.get(parent_tag);
						//CreditCard.mLogger.info("RLOS COMMON"+ " before adding address+ " + xml_str);
						xml_str = xml_str + getCustAddress_details(callName);
						//CreditCard.mLogger.info("RLOS COMMON"+ " after adding address+ " + xml_str);
						int_xml.put(parent_tag, xml_str);
					}

					else if("EmploymentStatus".equalsIgnoreCase(tag_name)){
						String xml_str = int_xml.get(parent_tag);
						String status="";
						//CreditCard.mLogger.info("RLOS COMMON"+ " before adding guarantor EmploymentStatus " + xml_str);
						if( "SUPPLEMENT".equalsIgnoreCase(CC_Comn.MultipleAppGridSelectedRow("MultipleApp_AppType")) && formObject.getLVWRowCount("SupplementCardDetails_cmplx_supplementGrid")>0){ 
							for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_supplementGrid");i++){
								if(CC_Comn.MultipleAppGridSelectedRow("MultipleApp_AppPass").equals(formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,3))){
									status=formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,39);
									break;
								}
							}
						}
						xml_str = xml_str +  "<"+tag_name+">"+status
								+"</"+ tag_name+">";
						//CreditCard.mLogger.info("RLOS COMMON"+ " after adding guarantor EmploymentStatus" + xml_str);
						int_xml.put(parent_tag, xml_str);
					}

					else if("Occupation".equalsIgnoreCase(tag_name)){

						String xml_str = int_xml.get(parent_tag);
						String Occupation="";
						//CreditCard.mLogger.info("RLOS COMMON"+ " before adding guarantor Occupation " + xml_str);
						if( "SUPPLEMENT".equalsIgnoreCase(CC_Comn.MultipleAppGridSelectedRow("MultipleApp_AppType")) && formObject.getLVWRowCount("SupplementCardDetails_cmplx_supplementGrid")>0){ 
							for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_supplementGrid");i++){
								if(CC_Comn.MultipleAppGridSelectedRow("MultipleApp_AppPass").equals(formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,3))){
									Occupation=formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,31);
									break;
								}
							}
						}
						xml_str = xml_str +  "<"+tag_name+">"+Occupation
								+"</"+ tag_name+">";
						//CreditCard.mLogger.info("RLOS COMMON"+ " after adding guarantor EmploymentStatus" + xml_str);
						int_xml.put(parent_tag, xml_str);
					}
					//Deepak Changes done for minor supplement  
					else if("RelationshipDetails".equalsIgnoreCase(tag_name)){
						if(int_xml.containsKey(parent_tag))
						{
							float Age = Float.parseFloat(formObject.getNGValue("cmplx_Customer_age"));
							String cif_id = formObject.getNGValue("CIF_ID");
							String Contact_Name = formObject.getNGValue("CUSTOMERNAME");
							String Nation = formObject.getNGValue("cmplx_Customer_Nationality");
							if((Age<21 && !"AE".equalsIgnoreCase(Nation)) ||("AE".equalsIgnoreCase(Nation) && Age<18.06)){
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
						int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
					}
				}

				else{
					if ("AddressDetails".equalsIgnoreCase(tag_name)	&& int_xml.containsKey(parent_tag)) {
						String xml_str = int_xml.get(parent_tag);
						//CreditCard.mLogger.info("RLOS COMMON"+ " before adding address+ " + xml_str);
						xml_str = xml_str + getCustAddress_details(callName);
						//CreditCard.mLogger.info("RLOS COMMON"+ " after adding address+ " + xml_str);
						int_xml.put(parent_tag, xml_str);
					}
					//change by saurabh for defualt value points on 8th Feb 19.
					else if("PrimaryServiceCenter".equalsIgnoreCase(tag_name) || "PrimaryBranchId".equalsIgnoreCase(tag_name)){
						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<" + tag_name + ">" + formObject.getNGValue("SOLID") + "</" + tag_name + ">";
						int_xml.put(parent_tag, xml_str);
					}
					else if ("MinorFlag".equalsIgnoreCase(tag_name)&& "PersonDetails".equalsIgnoreCase(parent_tag)) {
						if (int_xml.containsKey(parent_tag)) {
							float Age = Float.parseFloat(formObject.getNGValue("cmplx_Customer_age"));
							String age_flag = "N";
							if (Age < 21)//changed form 18 to 21 by akshay on 3/4/18 for proc 7178
								age_flag = "Y";
							String xml_str = int_xml.get(parent_tag);
							xml_str = xml_str + "<" + tag_name + ">" + age_flag + "</" + tag_name + ">";

							//CreditCard.mLogger.info("RLOS COMMON"+" after adding Minor flag+ " + xml_str);
							int_xml.put(parent_tag, xml_str);
						}
					}
					else if ("NonResidentFlag".equalsIgnoreCase(tag_name)	&& "PersonDetails".equalsIgnoreCase(parent_tag)) 
					{
						if (int_xml.containsKey(parent_tag)) {
							String xml_str = int_xml.get(parent_tag);
							String res_flag = "N";

							if ("Resident".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_ResidentNonResident"))) {
								res_flag = "Y";
							}

							xml_str = xml_str + "<" + tag_name + ">" + res_flag + "</" + tag_name + ">";

							//CreditCard.mLogger.info("RLOS COMMON"+ " after adding res_flag+ " + xml_str);
							int_xml.put(parent_tag, xml_str);
						}
					}
					else{
						int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
					}
				}
			}	
		}
		catch(Exception e){
			CreditCard.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + "+e.getMessage());
			CC_Common.printException(e);
		}
		return int_xml;
	}
	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : Customer Update Custom       

	 ***********************************************************************************  */
	private Map<String, String> CUSTOMER_UPDATE_Custom(List<List<String>> DB_List,FormReference formObject,String callName) {
		CreditCard.mLogger.info("inside CUSTOMER_UPDATE_Custom");
		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();

		try{
			for (List<String> mylist : DB_List) {

				for (int i = 0; i < 8; i++) {

					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");
				CreditCard.mLogger.info("tag_name: "+tag_name);
				CreditCard.mLogger.info("parent_tag: "+parent_tag);

				String Call_name = (String) recordFileMap.get("Call_name");
				String form_control = (String) recordFileMap.get("form_control");


				if ("AddrDet".equalsIgnoreCase(tag_name)) {
					//CreditCard.mLogger.info("inside 1st if"+ "inside customer update req1");
					if (int_xml.containsKey(parent_tag)) {
						//CreditCard.mLogger.info("inside 1st if"+"inside customer update req2");
						String xml_str = int_xml.get(parent_tag);
						//CreditCard.mLogger.info("RLOS COMMON"+ " before adding address+ " + xml_str);
						xml_str = xml_str + getCustAddress_details(Call_name);
						//CreditCard.mLogger.info("RLOS COMMON"+ " after adding address+ " + xml_str);
						int_xml.put(parent_tag, xml_str);
					}
				}
				
				




				else if( "PRIMARY".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)) || NGFUserResourceMgr_CreditCard.getGlobalVar("Services").contains(formObject.getNGValue("Sub_Product")))
				{	
					 if ("PhnLocalCode".equalsIgnoreCase(tag_name)) {
						CreditCard.mLogger.info("inside PL Common generate xml"+"PhnLocalCode to substring"+form_control);
						String xml_str = int_xml.get(parent_tag);
						String phn_no = formObject.getNGValue(form_control);
						if ((!"".equalsIgnoreCase(phn_no)) && phn_no.indexOf("00971") > -1) 
						{
							phn_no = phn_no.substring(5);
						}

						xml_str = xml_str + "<" + tag_name + ">" + phn_no + "</" + tag_name + ">";

						CreditCard.mLogger.info("PL COMMON"+ " after adding ApplicationID:  " + xml_str);
						int_xml.put(parent_tag, xml_str);
					}

					/* if ("PhnLocalCode".equalsIgnoreCase(tag_name)) {
					//CreditCard.mLogger.info("inside PL Common generate xml"+"PhnLocalCode to substring");
					String xml_str = int_xml.get(parent_tag);
					String phn_no = formObject.getNGValue(form_control);
					if ((!"".equalsIgnoreCase(phn_no)) && phn_no.indexOf("00971") > -1) 
					{
						phn_no = phn_no.substring(5);
					}

					xml_str = xml_str + "<" + tag_name + ">" + phn_no + "</" + tag_name + ">";

					//CreditCard.mLogger.info("PL COMMON"+ " after adding ApplicationID:  " + xml_str);
					int_xml.put(parent_tag, xml_str);
				}*/

					 else if("FatcaDetails".equalsIgnoreCase(tag_name)){
						//CreditCard.mLogger.info("PL Common"+"inside fatca inside customer update req1");
						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + getFATCA_details(formObject);
						//CreditCard.mLogger.info("PL COMMON"+" after adding FatcaDetails: "+xml_str);
						int_xml.put(parent_tag, xml_str);                                    
					}


					else if("KYCDetails".equalsIgnoreCase(tag_name)){
						//CreditCard.mLogger.info("PL Common"+"inside KYCDetails");
						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + getKYC_details(formObject);
						//CreditCard.mLogger.info("PL COMMON"+" after adding KYCDetails: "+xml_str);
						int_xml.put(parent_tag, xml_str);                                    
					}

					else if ("OECDDet".equalsIgnoreCase(tag_name) && int_xml.containsKey(parent_tag)) {
						//CreditCard.mLogger.info("inside 1st if"+ "inside customer update req2123");
						String xml_str = int_xml.get(parent_tag);
						//CreditCard.mLogger.info("RLOS COMMON"+ " before adding OECD+ " + xml_str);
						xml_str = xml_str + getCustOECD_details(Call_name);
						//CreditCard.mLogger.info("RLOS COMMON"+ " after adding OeCD+ " + xml_str);
						int_xml.put(parent_tag, xml_str);
					}

					//added by akshay on 17/1/18 fpr proc 1595
					else if("AECBconsentHeld".equalsIgnoreCase(tag_name)){
						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<" + tag_name + ">" + ("true".equalsIgnoreCase(formObject.getNGValue(form_control))?"Y":"N") + "</" + tag_name + ">";

						//CreditCard.mLogger.info("RLOS COMMON"+ " after adding AECBconsentHeld flag+ " + xml_str);
						int_xml.put(parent_tag, xml_str);
					}
					else if("NonResidentInd".equalsIgnoreCase(tag_name)){
						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<" + tag_name + ">" + ("N".equalsIgnoreCase(formObject.getNGValue(form_control))?"Y":"N") + "</" + tag_name + ">";

						//CreditCard.mLogger.info("RLOS COMMON"+ " after adding AECBconsentHeld flag+ " + xml_str);
						int_xml.put(parent_tag, xml_str);
					}
					else{
						int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
					}
				}
				else if( "SUPPLEMENT".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)))
				{
					//CreditCard.mLogger.info("RLOS COMMON"+ "Customer type is supplement!!!");
					if("CIFId".equalsIgnoreCase(tag_name) ){
						//CreditCard.mLogger.info("inside 1st if inside customer update req");
						String xml_str = int_xml.get(parent_tag);
						xml_str =xml_str+ "<"+tag_name+">"+formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),3)+"</"+ tag_name+">";
						//CreditCard.mLogger.info("PL COMMON  after adding CIFId:  "+xml_str);
						int_xml.put(parent_tag, xml_str);	                            	
					}

					else if("PhoneNo".equalsIgnoreCase(tag_name)){//|| "PhnLocalCode".equalsIgnoreCase(tag_name)
						String xml_str = int_xml.get(parent_tag);
						String ph_no ="",phlocal="";
						if( formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12).equalsIgnoreCase("Supplement") && formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid")>0){ 
							for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid");i++){
								if(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13).equals(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,3))){
									ph_no=formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,6);
									break;
								}
							}
						} 

						if ("PhoneNo".equalsIgnoreCase(tag_name)){
							xml_str = xml_str + "<" + tag_name + ">" +ph_no + "</" + tag_name + ">";
						}

						else if(!"".equalsIgnoreCase(ph_no) && ph_no.indexOf("00971") > -1) 
						{
							phlocal = ph_no.substring(5);
							xml_str = xml_str + "<" + tag_name + ">" +phlocal + "</" + tag_name + ">";

						}
						//CreditCard.mLogger.info("RLOS COMMON"+ " after adding PhoneNo flag+ " + xml_str);
						int_xml.put(parent_tag, xml_str);
					}

					else if("Email".equalsIgnoreCase(tag_name)){

						String xml_str = int_xml.get(parent_tag);
						String email="";
						//CreditCard.mLogger.info("RLOS COMMON"+ " before adding supplement mail details " + xml_str);
						if( formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12).equalsIgnoreCase("Supplement") && formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid")>0){ 
							for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid");i++){
								if(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13).equals(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,3))){
									email=formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,21);
									break;
								}
							}
						}
						xml_str = xml_str +  "<"+tag_name+">"+email
								+"</"+ tag_name+">";
						//CreditCard.mLogger.info("RLOS COMMON"+ " after adding guarantor MailIdValue" + xml_str);
						int_xml.put(parent_tag, xml_str);
					}

					else if("DocDet".equalsIgnoreCase(tag_name)){
						String xml_str = int_xml.get(parent_tag);
						//CreditCard.mLogger.info("RLOS COMMON"+ " before adding guarantor doc details " + xml_str);
						xml_str = xml_str + getSupplement_DocDetails(formObject,callName);
						//CreditCard.mLogger.info("RLOS COMMON"+ " after adding docDetails+ " + xml_str);
						int_xml.put(parent_tag, xml_str);

					}

					else if("RtlAddnlDet".equalsIgnoreCase(tag_name)){
						String xml_str = int_xml.get(parent_tag);
						//CreditCard.mLogger.info("RLOS COMMON"+ " before adding guarantor doc details " + xml_str);
						xml_str = xml_str + getSupplement_PersonDetails(formObject,callName);
						//CreditCard.mLogger.info("RLOS COMMON"+ " after adding docDetails+ " + xml_str);
						int_xml.put(parent_tag, xml_str);

					}
					else if("NonResidentInd".equalsIgnoreCase(tag_name)){
						String xml_str = int_xml.get(parent_tag);
						String suppPassport = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13);
						for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_supplementGrid");i++){
							if(formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,3).equals(suppPassport)){
								xml_str = xml_str + "<" + tag_name + ">" + ("false".equalsIgnoreCase(formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,16))?"Y":"N") + "</" + tag_name + ">";
								break;
							}
						}

						int_xml.put(parent_tag, xml_str);
					}
					else{
						int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
					}
				}
			}
			/*for(Map.Entry<String,String> entry : int_xml.entrySet()){
				if(entry.getValue().contains(">NA<")){
					CreditCard.mLogger.info("CC_integration_Input NA wala tag: " + entry.getKey());
					String entryVal = entry.getValue();
					CreditCard.mLogger.info("CC_integration_Input NA wala tagvalue: " + entryVal);
					entryVal = entryVal.replaceAll(">NA<", "><");
					CreditCard.mLogger.info("CC_integration_Input NA wala new tagvalue: " + entryVal);
					int_xml.put(entry.getKey(), entryVal);
				}
			}*/
		}
		catch(Exception e){
			CreditCard.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + "+e.getMessage());
		}
		return int_xml;

	}
	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : new Card Custom       

	 ***********************************************************************************  */
	private Map<String, String> NEW_CARD_Custom(List<List<String>> DB_List,FormReference formObject,String callName) {

		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();

		try{
			for (List<String> mylist : DB_List) {

				for (int i = 0; i < 8; i++) {

					//CreditCard.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");
				if ("VIPFlag".equalsIgnoreCase(tag_name)) {
					String vip_flag = "N";
					if ("true".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_VIPFlag"))) {
						vip_flag = "Y";
					}
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<" + tag_name + ">" + vip_flag + "</" + tag_name + ">";

					//CreditCard.mLogger.info("RLOS COMMON"+ " after adding VIP flag+ " + xml_str);
					int_xml.put(parent_tag, xml_str);
				} else if ("ProcessingUserId".equalsIgnoreCase(tag_name)) {
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<" + tag_name + ">" + formObject.getUserName() + "</" + tag_name + ">";

					//CreditCard.mLogger.info("RLOS COMMON"+ " after adding Minor flag+ " + xml_str);
					int_xml.put(parent_tag, xml_str);
				} else if ("ProcessingDate".equalsIgnoreCase(tag_name)) {
					String xml_str = int_xml.get(parent_tag);
					SimpleDateFormat sdf1 = new SimpleDateFormat(
							"yyyy-MM-dd'T'HH:mm:ss.mmm+hh:mm");
					xml_str = xml_str + "<" + tag_name + ">" + sdf1.format(new Date()) + "</" + tag_name + ">";

					//CreditCard.mLogger.info("RLOS COMMON"+ " after adding Minor flag+ " + xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
				}
			}
		}
		catch(Exception e){
			CreditCard.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + "+e.getMessage());
		}
		return int_xml;
	}
	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 10/12/2017              
		Author                              : Saurabh Gupta              
		Description                         : CARD_SERVICES_REQUEST_Custom       

	 ***********************************************************************************  */
	private Map<String, String> CARD_SERVICES_REQUEST_Custom(List<List<String>> DB_List,FormReference formObject,String callName) {

		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();

		try{
			for (List<String> mylist : DB_List) {

				for (int i = 0; i < 8; i++) {

					//CreditCard.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");
				//CreditCard.mLogger.info("CC_Integration_Input"+" CARD SERVICE custom function parent_tag: "+parent_tag+" tag_name: "+tag_name);
				if(tag_name.equalsIgnoreCase("LimitChangeType")){
					String xml_str = int_xml.get(parent_tag);

					xml_str = xml_str + "<"+tag_name+">"+(formObject.getNGValue("cmplx_LimitInc_Permanant").equalsIgnoreCase("Permanent")?"P":"T")
							+"</"+ tag_name+">";

					//CreditCard.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}

				else if(tag_name.equalsIgnoreCase("ProcessedBy")){
					String xml_str = int_xml.get(parent_tag);

					xml_str = xml_str + "<"+tag_name+">"+formObject.getUserName()
							+"</"+ tag_name+">";

					//CreditCard.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else if(tag_name.equalsIgnoreCase("SecurityChequeDetails")){
					CreditCard.mLogger.info("inside Generate XML "+"inside SecurityChequeDetails");
					if(int_xml.containsKey(parent_tag))
					{						
						String xml_str = int_xml.get(parent_tag);
						//CreditCard.mLogger.info("RLOS COMMON"+" before adding cheque+ "+xml_str);
						xml_str = xml_str + getCheque_details("CARD_SERVICES_REQUEST");
						//CreditCard.mLogger.info("RLOS COMMON"+" after adding cheque+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}		                            	
				}

				else if(tag_name.equalsIgnoreCase("LienDetails")){
					CreditCard.mLogger.info("inside 1st if"+"inside customer update req1");
					if(int_xml.containsKey(parent_tag))
					{
						//CreditCard.mLogger.info("inside 1st if"+"inside customer update req2");
						String xml_str = int_xml.get(parent_tag);
						//CreditCard.mLogger.info("RLOS COMMON"+" before adding LienDetails+ "+xml_str);
						xml_str = xml_str + getLien_details("CARD_SERVICES_REQUEST");
						//CreditCard.mLogger.info("RLOS COMMON"+" after adding LienDetails+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}		                            	
				}
				else if(tag_name.equalsIgnoreCase("AppProcessingInfo")){
					CreditCard.mLogger.info("inside 1st if"+"inside Card Service <AppProcessingInfo>");                                              
					String xml_str = int_xml.get(parent_tag);
					//CreditCard.mLogger.info("RLOS COMMON"+" before adding AppProcessingInfo+ "+xml_str);
					xml_str = xml_str + new CC_Common().getDecisionHistory_details();
					//CreditCard.mLogger.info("RLOS COMMON"+" after adding AppProcessingInfo+ "+xml_str);
					int_xml.put(parent_tag, xml_str);

				}
				else if(tag_name.equalsIgnoreCase("OtherIncomeDetails")){
					try{
						CreditCard.mLogger.info("inside OtherIncomeDetails");
						String assessedIncome = formObject.getNGValue("cmplx_IncomeDetails_Other_Avg");
						String grossSal = formObject.getNGValue("cmplx_IncomeDetails_grossSal");
						String selfSal = formObject.getNGValue("cmplx_IncomeDetails_AvgBal");
						CreditCard.mLogger.info("inside OtherIncomeDetails assessedIncome: "+assessedIncome);
						CreditCard.mLogger.info("inside OtherIncomeDetails grossSal: "+grossSal);
						CreditCard.mLogger.info("inside OtherIncomeDetails selfSal: "+selfSal);

						String xml_str = int_xml.get(parent_tag);
						//CreditCard.mLogger.info("inside OtherIncomeDetails before tag addition  xml_str: "+xml_str);
						String empType = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6);
						CreditCard.mLogger.info("inside OtherIncomeDetails empType: "+empType);
						if("Salaried".equalsIgnoreCase(empType)){
							if(assessedIncome!=null && !"".equals(assessedIncome)){
								assessedIncome = String.valueOf(Float.parseFloat(assessedIncome.replace(",", "")));  
								xml_str = xml_str + "<OtherIncomeDetails><IncomeType>AssessedIncome</IncomeType><IncomeAmount>"+assessedIncome+"</IncomeAmount><IncomeSource>Salaried</IncomeSource></OtherIncomeDetails>";
							}
							if(grossSal!=null && !"".equals(grossSal)){
								grossSal = String.valueOf(Float.parseFloat(grossSal.replace(",", "")));
								xml_str = xml_str + "<OtherIncomeDetails><IncomeType>GrossIncomeForSalaried</IncomeType><IncomeAmount>"+grossSal+"</IncomeAmount><IncomeSource>Salaried</IncomeSource></OtherIncomeDetails>";
							}
						}
						else{
							if(selfSal!=null && !"".equals(selfSal)){
								selfSal = String.valueOf(Float.parseFloat(selfSal.replace(",", "")));
								xml_str = xml_str + "<OtherIncomeDetails><IncomeType>GrossIncomeForSelf</IncomeType><IncomeAmount>"+selfSal+"</IncomeAmount></OtherIncomeDetails>";
							}
						}
						int_xml.put(parent_tag, xml_str);
						//CreditCard.mLogger.info("inside OtherIncomeDetails after tag addition  xml_str: "+xml_str);
					}
					catch(Exception e){
						CreditCard.mLogger.info("CC_INtegration Exception in CARD services custom function OtherIncomeDetails tag:  "+CC_Common.printException(e));
					}
				}
				/*//change by saurabh on 12th Dec
				else if(tag_name.equalsIgnoreCase("SalaryDay")){
					CreditCard.mLogger.info("inside 1st if"+"inside customer update req1");
					String xml_str = int_xml.get(parent_tag);
					Calendar now = Calendar.getInstance();
					String month = "";
					String day = "";
					if((now.get(Calendar.MONTH) - 1)<10){
						month = "0"+(now.get(Calendar.MONTH) - 1);
					}else{
						month = ""+(now.get(Calendar.MONTH) - 1);
					}
					if(formObject.getNGValue("cmplx_IncomeDetails_SalaryDay").length()<2){
						day = "0" + formObject.getNGValue("cmplx_IncomeDetails_SalaryDay");
					}else{
						day = formObject.getNGValue("cmplx_IncomeDetails_SalaryDay");
					}


					String Current_date="";

					Current_date = now.get(Calendar.YEAR)+"-"+month+"-"+day;
					xml_str = xml_str+"<"+tag_name+">"+Current_date+"</"+ tag_name+">";

					CreditCard.mLogger.info("PL COMMON"+" after adding ApplicationID:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}*/

				//below code by saurabh on 15th Dec 17
				else if(tag_name.equalsIgnoreCase("FinancialServices") ){
					CreditCard.mLogger.info("inside Generate XML inside FinancialServices");
					if(int_xml.containsKey(parent_tag))
					{						
						String xml_str = int_xml.get(parent_tag);
						//CreditCard.mLogger.info("Cc_integration_input before adding financialservices+ "+xml_str);
						xml_str = xml_str + getFinancialServices_details(callName);
						//CreditCard.mLogger.info("Cc_integration_input after adding financialservices+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}		                            	
				}
				else{
					int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
				}
			}
		}
		catch(Exception e){
			CreditCard.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + "+e.getMessage());
			CreditCard.mLogger.info("CC_INtegration Exception in CARD services custom function:  "+CC_Common.printException(e));
		}
		return int_xml;
	}
	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 10/12/2017              
		Author                              : Saurabh Gupta              
		Description                         : CARD_SERVICES_REQUEST_Custom       

	 ***********************************************************************************  */
	private Map<String, String> CARD_NOTIFICATION_Custom(List<List<String>> DB_List,FormReference formObject,String callName,String Operation_name) {

		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();

		try{
			for (List<String> mylist : DB_List) {

				for (int i = 0; i < 8; i++) {

					//CreditCard.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");
				String form_control = recordFileMap.get("form_control");
				String Default_value = recordFileMap.get("default_val");
				//CreditCard.mLogger.info("CC_Integration_Input"+" CARD SERVICE custom function parent_tag: "+parent_tag+" tag_name: "+tag_name);
				


				if(tag_name.equalsIgnoreCase("ResidencyStatus")){
					CreditCard.mLogger.info("inside 1st if inside customer update req1");
					String xml_str = int_xml.get(parent_tag);
					String residencyStatus=((formObject.getNGValue("cmplx_Customer_RESIDENTNONRESIDENT").equals("Y") && "AE".equalsIgnoreCase(formObject.getNGValue("cmplx_Customer_Nationality")))?"GCC":"EXPAT");
					CreditCard.mLogger.info("residency status"+residencyStatus);
					xml_str = xml_str+"<"+tag_name+">"+residencyStatus+"</"+ tag_name+">";

					CreditCard.mLogger.info("CC COMMON  after adding ResidencyStatus:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}
				/*else if (tag_name.equalsIgnoreCase("DocumentType") && parent_tag.equalsIgnoreCase("DocumentDet") && Default_value.equalsIgnoreCase("EMID") && formObject.getNGValue("cmplx_Customer_EmiratesID").equals(""))
				{
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str+"<"+tag_name+">"+""+"</"+ tag_name+">";
					int_xml.put(parent_tag, "");
				}*/
				else if("ReferenceDetails".equalsIgnoreCase(tag_name)){
					CreditCard.mLogger.info("inside 1st if"+" inside ReferenceDetails for Card NOTIF");
					if(int_xml.containsKey(parent_tag))
					{
						String xml_str = int_xml.get(parent_tag);
						CreditCard.mLogger.info("RLOS COMMON"+" before adding ReferenceDetails+ "+xml_str);
						xml_str = xml_str + getReference_details();
						CreditCard.mLogger.info("RLOS COMMON"+" after adding ReferenceDetails+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}

				}
				else if(tag_name.equalsIgnoreCase("OtherIncomeDetails")){
					try{
						CreditCard.mLogger.info("inside OtherIncomeDetails");
						String assessedIncome = formObject.getNGValue("cmplx_EligibilityAndProductInfo_FinalTai");
						String grossSal = formObject.getNGValue("cmplx_IncomeDetails_grossSal");
						CreditCard.mLogger.info("inside OtherIncomeDetails assessedIncome: "+assessedIncome);
						CreditCard.mLogger.info("inside OtherIncomeDetails grossSal: "+grossSal);

						String xml_str = int_xml.get(parent_tag);
						//CreditCard.mLogger.info("inside OtherIncomeDetails before tag addition  xml_str: "+xml_str);
						String empType = formObject.getNGValue("EmploymentType");
						CreditCard.mLogger.info("inside OtherIncomeDetails empType: "+empType);

						if(assessedIncome!=null && !"".equals(assessedIncome)){
							assessedIncome = String.valueOf(Float.parseFloat(assessedIncome.replace(",", "")));  
							xml_str = xml_str + "<OtherIncomeDetails><IncomeType>AssessedIncome</IncomeType><IncomeAmount>"+assessedIncome+"</IncomeAmount><IncomeSource>Salaried</IncomeSource></OtherIncomeDetails>";
						}
						if(grossSal!=null && !"".equals(grossSal)){
							grossSal = String.valueOf(Float.parseFloat(grossSal.replace(",", "")));
							xml_str = xml_str + "<OtherIncomeDetails><IncomeType>GrossIncomeForSalaried</IncomeType><IncomeAmount>"+grossSal+"</IncomeAmount><IncomeSource>Salaried</IncomeSource></OtherIncomeDetails>";
						}

						int_xml.put(parent_tag, xml_str);
						//CreditCard.mLogger.info("inside OtherIncomeDetails after tag addition  xml_str: "+xml_str);
					}
					catch(Exception e){
						CreditCard.mLogger.info("CC_INtegration Exception in CARD services custom function OtherIncomeDetails tag:  "+CC_Common.printException(e));
					}
				}
				//Deepak Changes done on 24 Aud for PCAS-2516
				else if("CombinedCreditLimit".equalsIgnoreCase(tag_name) ){
					String listView = "cmplx_CCCreation_cmplx_CCCreationGrid";
					String selectproduct_value = formObject.getNGValue(listView,formObject.getSelectedIndex(listView),5);
					List<List<String>> prd_type = formObject.getNGDataFromDataCache("select top 1 ReqProduct From ng_master_cardproduct with(nolock) where CODE ='"+selectproduct_value+"'");
					String tag_val="";
					if(prd_type.size()>0 && "Islamic".equalsIgnoreCase(prd_type.get(0).get(0))){
						tag_val="0";
					}
					else{
						tag_val=formObject.getNGValue(form_control);
					}
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<"+tag_name+">"+tag_val+"</"+tag_name+">";
					int_xml.put(parent_tag, xml_str);
					
				}
				else if("AddrDet".equalsIgnoreCase(tag_name) || "AddressDetails".equalsIgnoreCase(tag_name)){
					CreditCard.mLogger.info("inside 1st if"+" inside customer update req1");
					if(int_xml.containsKey(parent_tag))
					{
						CreditCard.mLogger.info("inside 1st if"+" inside customer update req2");
						String xml_str = int_xml.get(parent_tag);
						CreditCard.mLogger.info("RLOS COMMON"+" before adding address+ "+xml_str);
						xml_str = xml_str + getCustAddress_details(callName);
						CreditCard.mLogger.info("RLOS COMMON"+" after adding address+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}		                            	
				}


				else if("MonthlyCrTrnOvrAmt".equalsIgnoreCase(tag_name)){
					CreditCard.mLogger.info("inside 1st if inside MonthlyCrTrnOvrAmt tag card notif");
					String xml_str = int_xml.get(parent_tag);
					String avgBal = "";
					try{
						String query = "select avg_credit_6month from NG_RLOS_FinacleCore with(nolock) where wi_name = '"+formObject.getWFWorkitemName()+"'";
						List<List<String>> result = formObject.getDataFromDataSource(query);
						if(result!=null && result.size()>0){
							avgBal = result.get(0).get(0);
						}
						xml_str = xml_str+"<"+tag_name+">"+avgBal+"</"+ tag_name+">";
					}
					catch(Exception ex){
						xml_str = xml_str+"<"+tag_name+">"+avgBal+"</"+ tag_name+">";
					}
					CreditCard.mLogger.info("CC COMMON  after adding MonthlyCrTrnOvrAmt:  "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else if(tag_name.equalsIgnoreCase("ApplicationNumber") || tag_name.equalsIgnoreCase("InstitutionID")){
					CreditCard.mLogger.info("inside 1st if inside customer update req1");
					String xml_str = int_xml.get(parent_tag);
					try{
						xml_str = xml_str+"<"+tag_name+">"+Integer.parseInt(formObject.getWFWorkitemName().split("-")[1])+"</"+ tag_name+">";
					}
					catch(Exception ex){
						xml_str = xml_str+"<"+tag_name+">"+formObject.getWFWorkitemName().split("-")[1]+"</"+ tag_name+">";
					}
					CreditCard.mLogger.info("CC COMMON  after adding ApplicationNumber:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}


				else if("InterestProfile".equalsIgnoreCase(tag_name)){
					String xml_str = int_xml.get(parent_tag);
					String cardProd = formObject.getNGValue("CC_Creation_Product");
					int CRNgridRowCount = formObject.getLVWRowCount("cmplx_CardDetails_cmplx_CardCRNDetails");
					String intProf = "";
					for(int i=0;i<CRNgridRowCount;i++){
						if(formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",i,0).equalsIgnoreCase(cardProd)){
							intProf = formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",i,4);
							break;
						}
					}
					xml_str = xml_str + "<"+tag_name+">"+intProf+"</"+ tag_name+">";

					CreditCard.mLogger.info("RLOS COMMON"+" after adding intProf+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else if("FeeProfile".equalsIgnoreCase(tag_name)){
					String xml_str = int_xml.get(parent_tag);
					String cardProd = formObject.getNGValue("CC_Creation_Product");
					int CRNgridRowCount = formObject.getLVWRowCount("cmplx_CardDetails_cmplx_CardCRNDetails");
					String feeProf = "";
					for(int i=0;i<CRNgridRowCount;i++){
						if(formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",i,0).equalsIgnoreCase(cardProd)){
							feeProf = formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",i,5);
							break;
						}
					}
					xml_str = xml_str + "<"+tag_name+">"+feeProf+"</"+ tag_name+">";

					CreditCard.mLogger.info("RLOS COMMON"+" after adding feeProf+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else if("TransactionFeeProfile".equalsIgnoreCase(tag_name)){
					String xml_str = int_xml.get(parent_tag);
					String cardProd = formObject.getNGValue("CC_Creation_Product");
					int CRNgridRowCount = formObject.getLVWRowCount("cmplx_CardDetails_cmplx_CardCRNDetails");
					String transProf = "";
					for(int i=0;i<CRNgridRowCount;i++){
						if(formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",i,0).equalsIgnoreCase(cardProd)){
							transProf = formObject.getNGValue("cmplx_CardDetails_cmplx_CardCRNDetails",i,3);
							break;
						}
					}
					xml_str = xml_str + "<"+tag_name+">"+transProf+"</"+ tag_name+">";

					CreditCard.mLogger.info("RLOS COMMON"+" after adding transProf+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else if(tag_name.equalsIgnoreCase("CardType")){
					//CreditCard.mLogger.info("inside getting cardtype from DB");
					String xml_str = int_xml.get(parent_tag);
					String cardProd = formObject.getNGValue("CC_Creation_Product");
					String Ctype ="";
					String Query="select distinct CARDTYPE from ng_master_cardProduct with(nolock) where CODE = '"+cardProd+"'";
					CreditCard.mLogger.info("Query for getting Card type from DB is: "+Query);
					List<List<String>> intsert_list = formObject.getNGDataFromDataCache(Query);
					CreditCard.mLogger.info("result from DB is: "+intsert_list);
					if(!intsert_list.isEmpty()){
						Ctype = intsert_list.get(0).get(0);
					}	
					xml_str = xml_str+"<"+tag_name+">"+Ctype+"</"+ tag_name+">";
					CreditCard.mLogger.info("RLOS COMMON after adding cardtype getting from DB "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else if(tag_name.equalsIgnoreCase("MarketingCode")){
					try{
						String xml_str = int_xml.get(parent_tag);
						String emptype = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6);
						String mktCode = "";
						if(emptype.equalsIgnoreCase("Self Employed")){
							int rowCount = formObject.getLVWRowCount("cmplx_CompanyDetails_cmplx_CompanyGrid");
							if(rowCount>0){
								for(int i=0;i<rowCount;i++){
									if("Business".equalsIgnoreCase(formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i,1))){
										mktCode = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",i,28);
										break;
									}
								}
							}

						}
						else{
							mktCode = formObject.getNGValue("cmplx_EmploymentDetails_marketcode");
						}
						xml_str = xml_str+"<"+tag_name+">"+mktCode+"</"+ tag_name+">";
						int_xml.put(parent_tag, xml_str);
					}catch(Exception ex){
						/*new CC_Common();*/
						//Commented for sonar
						CreditCard.mLogger.info("Exception in MarketingCode tag: "+CC_Common.printException(ex));
					}
				}
				else if(tag_name.equalsIgnoreCase("CustSegment")){
					//CreditCard.mLogger.info("inside getting cardtype from DB");
					try{
						String xml_str = int_xml.get(parent_tag);
						String emptype = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6);
						String tSegcode = "";
						if(emptype.equalsIgnoreCase("Self Employed")){
							int rowCount = formObject.getLVWRowCount("cmplx_CompanyDetails_cmplx_CompanyGrid");
							if(rowCount>0){
								for(int i=0;i<rowCount;i++){
									if("Business".equalsIgnoreCase(formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i,1 ))){
										tSegcode = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",i,22);
										break;
									}
								}
							}

						}
						else{
							tSegcode = formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode");
						}
						xml_str = xml_str+"<"+tag_name+">"+tSegcode+"</"+ tag_name+">";
						int_xml.put(parent_tag, xml_str);
					}catch(Exception ex){
						/*new CC_Common();*/
						//Commented for sonar
						CreditCard.mLogger.info("Exception in CustSegment tag: "+CC_Common.printException(ex));
					}

					//CreditCard.mLogger.info("RLOS COMMON after adding cardtype getting from DB "+xml_str);

				}



				else if(tag_name.equalsIgnoreCase("BlackCheckComp")){
					//CreditCard.mLogger.info("inside getting cardtype from DB");
					try{
						String xml_str = int_xml.get(parent_tag);
						boolean blacklistFlag = false;
						int rowCount = formObject.getLVWRowCount("cmplx_FinacleCRMCustInfo_FincustGrid");
						if(rowCount>0){
							for(int i=0;i<rowCount;i++){
								if("Y".equalsIgnoreCase(formObject.getNGValue("cmplx_FinacleCRMCustInfo_FincustGrid",i,7))){
									blacklistFlag = true;
									break;
								}
							}
						}
						if(blacklistFlag){
							xml_str = xml_str+"<"+tag_name+">"+"Y"+"</"+ tag_name+">";	
						}
						else{
							xml_str = xml_str+"<"+tag_name+">"+"N"+"</"+ tag_name+">";
						}

						int_xml.put(parent_tag, xml_str);
					}catch(Exception ex){
					/*	new CC_Common();*/
					//Commented for sonar
						CreditCard.mLogger.info("Exception in BlackCheckComp tag: "+CC_Common.printException(ex));
					}
				}

				else if(tag_name.equalsIgnoreCase("isDedupFound")){
					//CreditCard.mLogger.info("inside getting cardtype from DB");
					try{
						String xml_str = int_xml.get(parent_tag);

						int rowCount = formObject.getLVWRowCount("cmplx_PartMatch_cmplx_Partmatch_grid");
						if(rowCount>0){
							xml_str = xml_str+"<"+tag_name+">"+"Y"+"</"+ tag_name+">";
						}
						else{
							xml_str = xml_str+"<"+tag_name+">"+"N"+"</"+ tag_name+">";	
						}
						int_xml.put(parent_tag, xml_str);
					}catch(Exception ex){
					/*	new CC_Common();*/
					//Commented for sonar
						CreditCard.mLogger.info("Exception in isDedupFound tag: "+CC_Common.printException(ex));
					}
				}

				else if(tag_name.equalsIgnoreCase("ContactDetails")){
					//CreditCard.mLogger.info("inside 1st if"+"inside customer update req1");
					if(int_xml.containsKey(parent_tag))
					{
						//CreditCard.mLogger.info("inside 1st if"+"inside customer update req2");
						String xml_str = int_xml.get(parent_tag);
						CreditCard.mLogger.info("RLOS COMMON"+" before contact details+ "+xml_str);
						xml_str = xml_str + getcontact_details();
						CreditCard.mLogger.info("RLOS COMMON"+" after adding contact details+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}		                            	
				}

				else if(tag_name.equalsIgnoreCase("EmploymentDetails")){
					try{
						if(int_xml.containsKey(parent_tag))
						{
							//CreditCard.mLogger.info("inside 1st if"+"inside customer update req2");
							String xml_str = int_xml.get(parent_tag);
							//CreditCard.mLogger.info("RLOS COMMON"+" before contact details+ "+xml_str);
							String emptype = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6);
							CreditCard.mLogger.info("emptype: "+emptype);
							if(emptype.equalsIgnoreCase("Self Employed") && "Primary".equalsIgnoreCase(Operation_name)){
								CreditCard.mLogger.info("case 1:");
								xml_str = xml_str + getEmp_details(true,true);
							}
							else if(!emptype.equalsIgnoreCase("Self Employed") && "Primary".equalsIgnoreCase(Operation_name)){
								CreditCard.mLogger.info("case 2:");
								xml_str = xml_str + getEmp_details(false,true);
							}
							else if(emptype.equalsIgnoreCase("Self Employed") && "Supplement".equalsIgnoreCase(Operation_name)){
								CreditCard.mLogger.info("case 3:");
								xml_str = xml_str + getEmp_details(true,false);
							}
							else if(!emptype.equalsIgnoreCase("Self Employed") && "Supplement".equalsIgnoreCase(Operation_name)){
								CreditCard.mLogger.info("case 4:");
								xml_str = xml_str + getEmp_details(false,false);
							}
							CreditCard.mLogger.info("RLOS COMMON"+" after adding EmploymentDetails details+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}
					}catch(Exception ex){
					/*	new CC_Common();*/
					//Commented for sonar
						CreditCard.mLogger.info("Exception in EmploymentDetails aggregate tag: "+CC_Common.printException(ex));
					}
				}

				else if("ApplicationDate".equalsIgnoreCase(tag_name)){
					CreditCard.mLogger.info("inside 1st if inside card_notification req1");
					String xml_str = int_xml.get(parent_tag);
					Date d1=new Date();
					String Date="";

					SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
					Date=sf.format(d1);

					//d2.parse(sf.format(d1));
					xml_str =xml_str+ "<"+tag_name+">"+Date+"</"+ tag_name+">";

					CreditCard.mLogger.info("PL COMMON  after adding DSAId:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}
				//++below code added by nikhil for Self-Supp CR
				else if("ApplicationType".equalsIgnoreCase(tag_name)){
					String xml_str = int_xml.get(parent_tag);
					String Grid_Name="cmplx_CCCreation_cmplx_CCCreationGrid";
					int selectedindex=formObject.getSelectedIndex(Grid_Name);
					String App_type=formObject.getNGValue(Grid_Name,selectedindex,12);
					if(!App_type.equalsIgnoreCase("Primary"))
					{
						App_type="Secondary";
					}					
					xml_str =xml_str+ "<"+tag_name+">"+App_type+"</"+ tag_name+">";

					
					int_xml.put(parent_tag, xml_str);	                            	
				}
				//--above code added by nikhil for Self-Supp CR
				//tag added by saurabh on 8th Feb 19 for default value points.
				else if("DSAId".equalsIgnoreCase(tag_name)){
					try{
					CreditCard.mLogger.info("inside 1st DSAId card_notification req1");
					String xml_str = int_xml.get(parent_tag);
					
					String branchUserQuery = "select Introduce_By from NG_RLOS_EXTTABLE where WIname = (select parent_WIName from NG_CC_EXTTABLE where CC_Wi_Name='"+formObject.getWFWorkitemName()+"')";
					List<List<String>> result = formObject.getDataFromDataSource(branchUserQuery);
					if(null!=result && !result.isEmpty()){
						if(null!=result.get(0) && !result.get(0).isEmpty()){
							xml_str =xml_str+ "<"+tag_name+">"+result.get(0).get(0)+"</"+ tag_name+">";
						}
					}
					CreditCard.mLogger.info("PL COMMON  after adding DSAId:  "+xml_str);
					int_xml.put(parent_tag, xml_str);
					}catch(Exception ex){
						CreditCard.mLogger.info("inside exception in DSAId: "+new CC_Common().printException(ex));
					}
				}
				//Deepak - 28july 2019 code added for CardRefNumber
				else if("CardRefNumber".equalsIgnoreCase(tag_name) ){
					//CreditCard.mLogger.info("inside 1st if"+" inside CardRefNumber for Card NOTIF");
					String tagValue=getCardRefNumbervalue();
					String xml_str =  "<"+tag_name+">"+tagValue+"</"+ tag_name+">";
					int_xml.put(parent_tag, xml_str);
					//CreditCard.mLogger.info("inside CardRefNumber for Card NOTIF: CardRefNumber"+ xml_str);
				}
				else if("SubscriptionFlag".equalsIgnoreCase(tag_name) && "SubscriptionInfo".equalsIgnoreCase(parent_tag) ){
					CreditCard.mLogger.info("inside RefValue for cmplx_CardDetails_SMS_Output for Card Notification: "+ form_control);
					String xml_str = int_xml.get(parent_tag);
					String tagValue=(formObject.getNGValue(form_control).equalsIgnoreCase("true")?"Y":"N");
					 xml_str = xml_str+ "<"+tag_name+">"+tagValue+"</"+ tag_name+">";
					CreditCard.mLogger.info("inside RefValue for cmplx_CardDetails_SMS_Output for Card Notification xml_str: "+ xml_str);
					int_xml.put(parent_tag, xml_str);
					//CreditCard.mLogger.info("inside CardRefNumber for Card NOTIF: CardRefNumber"+ xml_str);
				}
				else if("preferredCharityorg".equalsIgnoreCase(tag_name) || "Donationamount".equalsIgnoreCase(tag_name)){
					try{
						//CreditCard.mLogger.info("inside preferredCharityorg if");
						String xml_str = int_xml.get(parent_tag);
						String card_prod = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),5);
						String squery="select top 1 ReqProduct from ng_master_cardProduct where CODE = '"+card_prod+"' and EmployerCategory = '"+formObject.getNGValue("EmploymentType")+"'";
						//CreditCard.mLogger.info("squery: "+squery);
						List<List<String>> typeOfCP = formObject.getDataFromDataSource(squery);
						//CreditCard.mLogger.info("squery output: "+typeOfCP);
						if(typeOfCP!=null && typeOfCP.size()>0 && typeOfCP.get(0)!=null && "Islamic".equalsIgnoreCase(typeOfCP.get(0).get(0))){
							//CreditCard.mLogger.info("inside preferredCharityorg Islamic"); 
							if("preferredCharityorg".equalsIgnoreCase(tag_name)){
								xml_str =  xml_str+ "<"+tag_name+">5</"+ tag_name+">";
							}
							else if("Donationamount".equalsIgnoreCase(tag_name)){
								xml_str =  xml_str+ "<"+tag_name+">0</"+ tag_name+">";
							}
							//CreditCard.mLogger.info("inside preferredCharityorg updated xml_str: "+xml_str);
						}
						int_xml.put(parent_tag, xml_str);
					}
					catch(Exception e){
						CreditCard.mLogger.info("Exception occured for preferredCharityorg or Donationamount "+ e.getMessage());
					}
					
				}
				//++below code added by nikhil for Self-Supp CR
				else if( "PRIMARY".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)) || ("SUPPLEMENT".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)) && formObject.getNGValue("cmplx_Customer_PAssportNo").equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13))  ))
				{
					if("RVCStatus".equalsIgnoreCase(tag_name)){
						String xml_str = int_xml.get(parent_tag);
						String RVC_status = (formObject.getNGValue(form_control).equalsIgnoreCase("true")?"Y":"N");;

						xml_str = xml_str + "<"+tag_name+">"+RVC_status+"</"+ tag_name+">";

						CreditCard.mLogger.info("RLOS COMMON"+" after adding RVCStatus+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}
					//changes by saurabh on 
					else if("DocId".equalsIgnoreCase(tag_name) && "cmplx_Customer_EmiratesID".equalsIgnoreCase(form_control)){
						String xml_str = int_xml.get(parent_tag);
						if(null!= formObject.getNGValue("cmplx_Customer_NEP") && !"--Select--".equals(formObject.getNGValue("cmplx_Customer_NEP")) && !"".equals(formObject.getNGValue("cmplx_Customer_NEP"))){
							form_control = "cmplx_Customer_EIDARegNo";
						}
						xml_str = xml_str + "<"+tag_name+">"+formObject.getNGValue(form_control)+"</"+ tag_name+">";
						CreditCard.mLogger.info("RLOS COMMON"+" after adding Emirates ID+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}
					
					else if("DispatchMode".equalsIgnoreCase(tag_name)){
						//String xml_str = int_xml.get(parent_tag);
						String Dispatch_details = formObject.getNGValue(form_control);
						String Branch_details="";
						if(Dispatch_details.equalsIgnoreCase("ByBranch")){
							Branch_details = "<BranchDetails><BranchCode>"+formObject.getNGValue("AlternateContactDetails_CustdomBranch")+"</BranchCode></BranchDetails>";
						}
						String xml_str =  "<"+tag_name+">"+Dispatch_details+"</"+ tag_name+">"+Branch_details;

						//CreditCard.mLogger.info("RLOS COMMON"+" after adding RVCStatus+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}
					else if("CustomerNameOnCard".equalsIgnoreCase(tag_name)){
						String xml_str = int_xml.get(parent_tag);
						String tagValue="";
						//CreditCard.mLogger.info("PL COMMON  before adding isNTB:  "+xml_str);
						if("PRIMARY".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)) )
						{
							tagValue=formObject.getNGValue("cmplx_CardDetails_cardEmbossing_name");
						}
						else 
						{
							tagValue=formObject.getNGValue("cmplx_CardDetails_Self_card_embossing");
						}
						xml_str = xml_str + "<"+tag_name+">"+tagValue+"</"+ tag_name+">";
						int_xml.put(parent_tag, xml_str);
						CreditCard.mLogger.info("after adding CustomerNameOnCard for primary:  "+xml_str);
					}
					
					else if("ProductInfoTags".equalsIgnoreCase(tag_name)){
						String xml_str = int_xml.get(parent_tag);
						String card_prod = formObject.getNGValue("CC_Creation_Product");
						String card_intrest_profile = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),11);
						CreditCard.mLogger.info("inside ProductInfoTags card_prod: "+ card_prod);
						//Deepak 25 dec Changes done for Arabia
						if(card_intrest_profile.contains("Arabia")){
							xml_str = xml_str+ "<ProductInfo><ProductName>AirArabia</ProductName><ProductId>"+formObject.getNGValue("AlternateContactDetails_AirArabiaIdentifier")+"</ProductId></ProductInfo>";
						}
						//Deepak changes done for kalyan card
						if(card_intrest_profile.contains("KALYAN")){
							String kalyan_no = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),14);
							CreditCard.mLogger.info("RLOS COMMON after adding kalyan_no: + "+kalyan_no);
							String LinkageType_refval = getLinkageType_refval(kalyan_no);
							xml_str = xml_str+ "<ProductInfo><ProductName>Kalyan</ProductName><ProductId>"+kalyan_no+"</ProductId><ProductRefInfo><RefType>LinkageType</RefType><RefValue>"+LinkageType_refval+"</RefValue></ProductRefInfo></ProductInfo>";
							
						}
						//elow code comented by nikhil for Card notification CR
						else if(!card_prod.contains("KALYAN")){
							List<List<String>> typeOfCP = formObject.getDataFromDataSource("select top 1 ReqProduct from ng_master_cardProduct where CODE = '"+card_prod+"' and Subproduct = '"+formObject.getNGValue("Sub_Product")+"' and EmployerCategory = '"+formObject.getNGValue("EmploymentType")+"'");
							if(typeOfCP!=null && typeOfCP.size()>0 && typeOfCP.get(0)!=null && "Islamic".equalsIgnoreCase(typeOfCP.get(0).get(0))){
							xml_str =  xml_str+ new Common_Utils(CreditCard.mLogger).murabhaTags(formObject.getWFWorkitemName(), "Murbaha",card_prod);
							}
						}

						else{
							xml_str =  xml_str+ new Common_Utils(CreditCard.mLogger).murabhaTags(formObject.getWFWorkitemName(), "Kalyan",card_prod);
						}
						CreditCard.mLogger.info("RLOS COMMON after adding ProductInfoTags+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}
					else if(tag_name.equalsIgnoreCase("IsNTB") || tag_name.equalsIgnoreCase("VIPFlag") || tag_name.equalsIgnoreCase("VIPFlg") || tag_name.equalsIgnoreCase("NonResidentFlag")||tag_name.equalsIgnoreCase("EStatementFlag") ){
						String xml_str = int_xml.get(parent_tag);
						String tagValue="";

						if(tag_name.equalsIgnoreCase("IsNTB")){
							tagValue=(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true")?"Y":"N");
						}
						/*else if((tag_name.equalsIgnoreCase("VIPFlag") || tag_name.equalsIgnoreCase("VIPFlg"))&&Call_name.equalsIgnoreCase("NEW_CREDITCARD_REQ")){
						tagValue=(formObject.getNGValue("cmplx_Customer_VIPFlag").equalsIgnoreCase("true")?"1":"0");
					}*/
						else if((tag_name.equalsIgnoreCase("VIPFlag") || tag_name.equalsIgnoreCase("VIPFlg"))&& !callName.equalsIgnoreCase("NEW_CREDITCARD_REQ")){
							tagValue=(formObject.getNGValue("cmplx_Customer_VIPFlag").equalsIgnoreCase("true")?"Y":"N");
						}
						else if(tag_name.equalsIgnoreCase("NonResidentFlag")){
							//change by saurabh for DROP-4
							tagValue=(formObject.getNGValue("cmplx_Customer_ResidentNonResident").equalsIgnoreCase("true")?"Y":"N");
						}	
						else if(tag_name.equalsIgnoreCase("EStatementFlag")){
							tagValue=(formObject.getNGValue("AlternateContactDetails_eStatementFlag").equalsIgnoreCase("Yes")?"Y":"N");
						}

						if(!tagValue.equals("")){
							xml_str = xml_str + "<"+tag_name+">"+tagValue+"</"+ tag_name+">";
						}


						//CreditCard.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}
					else if(tag_name.equalsIgnoreCase("SecurityChequeDetails") && !(new CC_Common().Check_Elite_Customer(formObject)) ){
						CreditCard.mLogger.info("inside Generate XML inside SecurityChequeDetails");
						if(int_xml.containsKey(parent_tag))
						{						
							String xml_str = int_xml.get(parent_tag);
							//CreditCard.mLogger.info("RLOS COMMON before adding cheque+ "+xml_str);
							xml_str = xml_str + getCheque_details(callName);
							CreditCard.mLogger.info("RLOS COMMON after adding cheque+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}		                            	
					}

					else if(tag_name.equalsIgnoreCase("LienDetails")){
						//CreditCard.mLogger.info("inside 1st if inside customer update req1");
						if(int_xml.containsKey(parent_tag))
						{
							//CreditCard.mLogger.info("inside 1st if"+"inside customer update req2");
							String xml_str = int_xml.get(parent_tag);
							//CreditCard.mLogger.info("RLOS COMMON"+" before adding LienDetails+ "+xml_str);
							xml_str = xml_str + getLien_details(callName);
							CreditCard.mLogger.info("RLOS COMMON after adding LienDetails+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}		                            	
					}

					else if(tag_name.equalsIgnoreCase("ResidenceSince"))
					{
						//CreditCard.mLogger.info("inside 1st if inside customer update req1");
						String xml_str = int_xml.get(parent_tag);

						xml_str = xml_str + "<"+tag_name+">"+calcResidenceSince(formObject)+"</"+ tag_name+">";//changed by akshay on 28/2/2018 for drop 4
						int_xml.put(parent_tag, xml_str);								
					}


					else if(tag_name.equalsIgnoreCase("SalaryDate")&& formObject.getNGValue("EmploymentType").contains("Salaried")){
						//CreditCard.mLogger.info("inside 1st if inside customer update req1");
						String xml_str = int_xml.get(parent_tag);
						Calendar now = Calendar.getInstance();
						String month = "";
						String day = "";


						if((now.get(Calendar.MONTH) + 1)<10){
							month = "0"+(now.get(Calendar.MONTH) + 1);
						}else{
							month = ""+(now.get(Calendar.MONTH) + 1);
						}
						if(formObject.getNGValue("cmplx_IncomeDetails_SalaryDay").length()<2){
							day = "0" + formObject.getNGValue("cmplx_IncomeDetails_SalaryDay");
						}else{
							day = formObject.getNGValue("cmplx_IncomeDetails_SalaryDay");
						}


						String Current_date="";

						Current_date = now.get(Calendar.YEAR)+"-"+month+"-"+day;
						xml_str = xml_str+"<"+tag_name+">"+Current_date+"</"+ tag_name+">";

						CreditCard.mLogger.info("PL COMMON  after adding SalaryDate:  "+xml_str);
						int_xml.put(parent_tag, xml_str);	                            	
					}
					//added by akshay on 20/3/18
					else if(tag_name.equalsIgnoreCase("ExecutionDay")){
						String xml_str = int_xml.get(parent_tag);
						String day = "";
						if(formObject.getNGValue("cmplx_CC_Loan_DDSExecDay")!=null && !"".equals(formObject.getNGValue("cmplx_CC_Loan_DDSExecDay"))){
							day = formObject.getNGValue("cmplx_CC_Loan_DDSExecDay").substring(0,2);
						}
						xml_str = xml_str+"<"+tag_name+">"+day+"</"+ tag_name+">";

						CreditCard.mLogger.info("PL COMMON  after adding ExecutionDay:  "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}

					else if(tag_name.equalsIgnoreCase("MinorFlag") ){
						if(int_xml.containsKey(parent_tag))
						{	//change from int to float by saurabh on 16th dec
							float Age = Float.parseFloat(formObject.getNGValue("cmplx_Customer_age"));
							String age_flag = "N";
							if(Age<21)
								age_flag="Y";
							String xml_str = int_xml.get(parent_tag);
							xml_str = xml_str + "<"+tag_name+">"+age_flag
									+"</"+ tag_name+">";

							//CreditCard.mLogger.info("RLOS COMMON  after adding Minor flag+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}		                            	
					}


					//below code by saurabh on 15th Dec 17
					else if(tag_name.equalsIgnoreCase("FinancialServices") ){
						CreditCard.mLogger.info("inside Generate XML inside FinancialServices");
						if(int_xml.containsKey(parent_tag))
						{						
							String xml_str = int_xml.get(parent_tag);
							//CreditCard.mLogger.info("Cc_integration_input before adding financialservices+ "+xml_str);
							xml_str = xml_str + getFinancialServices_details(callName);
							//CreditCard.mLogger.info("Cc_integration_input after adding financialservices+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}		                            	
					}
					else if(tag_name.equalsIgnoreCase("RVC") && parent_tag.equalsIgnoreCase("AddOnServices")){
						//CreditCard.mLogger.info("inside Generate XML inside RVC");
						if(int_xml.containsKey(parent_tag))
						{						
							String xml_str = int_xml.get(parent_tag);
							//CreditCard.mLogger.info("Cc_integration_input before adding RVC+ "+xml_str);
							xml_str = xml_str + getRVCDetails();
							//CreditCard.mLogger.info("Cc_integration_input after adding RVC+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}
					}
					else if(tag_name.equalsIgnoreCase("PaymentInstruction") && parent_tag.equalsIgnoreCase("AddOnServices")){
						//CreditCard.mLogger.info("inside Generate XML inside PaymentInstruction");
						if(int_xml.containsKey(parent_tag))
						{						
							String xml_str = int_xml.get(parent_tag);
							//CreditCard.mLogger.info("Cc_integration_input before adding PaymentInstruction+ "+xml_str);
							xml_str = xml_str + getDDSDetails();
							//CreditCard.mLogger.info("Cc_integration_input after adding PaymentInstruction+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}
					}
					else{
						int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
					}
				}
				else if( "SUPPLEMENT".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)))
				{

					if("CIFID".equalsIgnoreCase(tag_name) ){
						CreditCard.mLogger.info("inside 1st if inside customer update req");
						String xml_str = int_xml.get(parent_tag);
						xml_str =xml_str+ "<"+tag_name+">"+formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),3)+"</"+ tag_name+">";
						CreditCard.mLogger.info("PL COMMON  after adding CIFId:  "+xml_str);
						int_xml.put(parent_tag, xml_str);	                            	
					}
					else if("PersonDetails".equalsIgnoreCase(tag_name)){
						String xml_str = int_xml.get(parent_tag);
						CreditCard.mLogger.info("RLOS COMMON"+ " before adding guarantor PersonDetails+ " + xml_str);
						xml_str = xml_str + getSupplement_PersonDetails(formObject,callName);
						CreditCard.mLogger.info("RLOS COMMON"+ " after adding PersonDetails " + xml_str);
						int_xml.put(parent_tag, xml_str);

					}
					else if("CustomerNameOnCard".equalsIgnoreCase(tag_name)){
						String xml_str = int_xml.get(parent_tag);
						CreditCard.mLogger.info("RLOS COMMON"+ " before adding guarantor PersonDetails+ " + xml_str);
						xml_str =xml_str+ "<"+tag_name+">"+getSupplementaryTagValue(9)+"</"+ tag_name+">";
						CreditCard.mLogger.info("RLOS COMMON"+ " after adding PersonDetails " + xml_str);
						int_xml.put(parent_tag, xml_str);

					}

					else if("DocumentDet".equalsIgnoreCase(tag_name)){
						String xml_str = int_xml.get(parent_tag);
						CreditCard.mLogger.info("RLOS COMMON"+ " before adding supplement DocumentDet+ " + xml_str);
						xml_str = xml_str + getSupplement_DocDetails(formObject,callName);
						CreditCard.mLogger.info("RLOS COMMON"+ " after adding PersonDetails " + xml_str);
						int_xml.put(parent_tag, xml_str);

					}

					else if(tag_name.equalsIgnoreCase("EStatementFlag") ){
						String xml_str = int_xml.get(parent_tag);
						String tagValue="";
						if(tag_name.equalsIgnoreCase("EStatementFlag")){
							tagValue=(formObject.getNGValue("AlternateContactDetails_eStatementFlag").equalsIgnoreCase("Yes")?"Y":"N");
						}
						if(!tagValue.equals("")){
							xml_str = xml_str + "<"+tag_name+">"+tagValue+"</"+ tag_name+">";
						}
						//CreditCard.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}
					else{
						int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
					}
				}
			}
		}
		catch(Exception e){
			CreditCard.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + "+e.getMessage());
			CreditCard.mLogger.info("CC_INtegration Exception in CARD services custom function:  "+CC_Common.printException(e));
		}
		return int_xml;
	}
	//Deepak new method created for getCardRefNumbervalue
	private String getCardRefNumbervalue() {
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String selected_card_product = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),5);
			String selected_cust_product = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12);
			//CreditCard.mLogger.info(" In getCardRefNumbervalue, selected_card_product: "+selected_card_product);
			//CreditCard.mLogger.info(" In getCardRefNumbervalue, selected_cust_product: "+selected_cust_product);
			int add_row_count = formObject.getLVWRowCount("cmplx_CCCreation_cmplx_CCCreationGrid");
			String CRN_val="";
			if("SUPPLEMENT".equalsIgnoreCase(selected_cust_product)){
				for(int row_count = 0; row_count<add_row_count;row_count++){
					String card_product = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",row_count,5);
					String customer_type = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",row_count,12);
					//CreditCard.mLogger.info(" In getCardRefNumbervalue, card_product: "+card_product);
					//CreditCard.mLogger.info(" In getCardRefNumbervalue, customer_type: "+customer_type);
					
					if(selected_card_product.equalsIgnoreCase(card_product) && "Primary".equalsIgnoreCase(customer_type)){
						CRN_val = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",row_count,1);
						break;
					}
				}
			}
			else{
				CRN_val = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),1);
			}
			return CRN_val;
		}
		catch(Exception e){
			CreditCard.mLogger.info("CC Integration "+ " Exception occured in getCardRefNumbervalue + "+e.getMessage());
			return "";
		}
	}
//Deepak new method created for LinkageType_refval of kalyan card 707
	private String getLinkageType_refval(String kalyan_no) {
		
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String sQuery = "select count(REWARD_ACCOUNT_NUMBER) from CAPS_Kalyan_Ref_Details_Mig where REWARD_ACCOUNT_NUMBER ='"+kalyan_no+"'";
			List<List<String>> DB_List=formObject.getDataFromDataSource(sQuery);
			if( "0".equalsIgnoreCase(DB_List.get(0).get(0))){
				return "A";
			}
			else{
				return "T"; 
			}
		}
		catch(Exception e){
			CreditCard.mLogger.info("CC Integration "+ " Exception occured in getLinkageType_refval + "+e.getMessage());
			return "T";
		}
	}

	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 10/12/2017              
		Author                              : Saurabh Gupta              
		Description                         : CARD_SERVICES_REQUEST_Custom       

	 ***********************************************************************************  */
	private Map<String, String> NEW_CREDITCARD_Custom(List<List<String>> DB_List,FormReference formObject,String callName) {

		Map<String, String> int_xml = new LinkedHashMap<String, String>();
		Map<String, String> recordFileMap = new HashMap<String, String>();

		try{
			for (List<String> mylist : DB_List) {

				for (int i = 0; i < 8; i++) {

					//CreditCard.mLogger.info(""+ "column length values"+ col_n);
					String[] col_name = col_n.split(",");
					recordFileMap.put(col_name[i], mylist.get(i));
				}
				String parent_tag =  recordFileMap.get("parent_tag_name");
				String tag_name =  recordFileMap.get("xmltag_name");
				String form_control = recordFileMap.get("form_control");
				CreditCard.mLogger.info("card creation deep parent_tag:  "+parent_tag);
				CreditCard.mLogger.info("card creation deep tag_name:  "+tag_name);
				CreditCard.mLogger.info("card creation deep form_control:  "+form_control);
				CreditCard.mLogger.info("card creation deep int_xml:  "+int_xml);
				//CreditCard.mLogger.info("CC_Integration_Input"+" CARD SERVICE custom function parent_tag: "+parent_tag+" tag_name: "+tag_name);
				if("ReferenceDetails".equalsIgnoreCase(tag_name)){
					//CreditCard.mLogger.info("inside 1st if"+" inside ReferenceDetails for Card NOTIF");
					if(int_xml.containsKey(parent_tag))
					{
						String xml_str = int_xml.get(parent_tag);
						//CreditCard.mLogger.info("RLOS COMMON"+" before adding ReferenceDetails+ "+xml_str);
						xml_str = xml_str + getReference_details();
						//CreditCard.mLogger.info("RLOS COMMON"+" after adding ReferenceDetails+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}

				}
				//Changes done by Deepak for PCSP-651 Start
				else if("MaritalStatus".equalsIgnoreCase(tag_name) && "CustAndProductInfo".equalsIgnoreCase(parent_tag)){
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<"+tag_name+">"+ getCodeDesc("NG_MASTER_Maritalstatus", "prime_val",formObject.getNGValue(form_control))+"</"+tag_name+">";
					int_xml.put(parent_tag, xml_str);
				}
				else if("Gender".equalsIgnoreCase(tag_name) && "CustAndProductInfo".equalsIgnoreCase(parent_tag)){
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<"+tag_name+">"+ getCodeDesc("NG_MASTER_Gender", "prime_val",formObject.getNGValue(form_control))+"</"+tag_name+">";
					int_xml.put(parent_tag, xml_str);
				}
				else if(("Designation".equalsIgnoreCase(tag_name)||"Department".equalsIgnoreCase(tag_name)) && "CustAndProductInfo".equalsIgnoreCase(parent_tag)){
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<"+tag_name+">"+ getCodeDesc("NG_MASTER_Designation", "Description",formObject.getNGValue(form_control))+"</"+tag_name+">";
					int_xml.put(parent_tag, xml_str);
				}
				else if("PhoneFaxDtls".equalsIgnoreCase(tag_name) && "CustAndProductInfo".equalsIgnoreCase(parent_tag)){
					
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + getNewCardPhoneFaxDtls();
					int_xml.put(parent_tag, xml_str);
				}
				//Deepak Changes done on 24 Aud for PCAS-2516
				else if("MaxCreditLimit".equalsIgnoreCase(tag_name) ){
					String listView = "cmplx_CCCreation_cmplx_CCCreationGrid";
					String selectproduct_value = formObject.getNGValue(listView,formObject.getSelectedIndex(listView),5);
					List<List<String>> prd_type = formObject.getNGDataFromDataCache("select top 1 ReqProduct From ng_master_cardproduct with(nolock) where CODE ='"+selectproduct_value+"'");
					String tag_val="";
					if(prd_type.size()>0 && "Islamic".equalsIgnoreCase(prd_type.get(0).get(0))){
						tag_val="0";
					}
					else{
						tag_val=formObject.getNGValue(form_control);
					}
					
					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<"+tag_name+">"+tag_val+"</"+tag_name+">";
					int_xml.put(parent_tag, xml_str);
					
				}
				//Changes done by Deepak for PCSP-651 End
				//Deepak Code commented for CR PCSP-475 to send value from master 
				else if("ProfileSerNo".equalsIgnoreCase(tag_name) && "CardDetails".equalsIgnoreCase(parent_tag)){
					CreditCard.mLogger.info("inside profileserno for New Card ");
					if(int_xml.containsKey(parent_tag))
					{
						try{
							String xml_str = int_xml.get(parent_tag);
							List<List<String>> record = null;
							CreditCard.mLogger.info("RLOS COMMON before adding ProfileSerNo "+xml_str);
							if( "PRIMARY".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12))){
								 record = formObject.getNGDataFromDataCache("select top 1 MAST_PROF_PRIM from ng_master_cardProduct with(nolock) where CODE ='"+formObject.getNGValue("CC_Creation_Product")+"'");
							}
							else{
								 record = formObject.getNGDataFromDataCache("select top 1 MAST_PROF_SUPP_CLUND from ng_master_cardProduct with(nolock) where CODE ='"+formObject.getNGValue("CC_Creation_Product")+"'");
							}
							
							String result = record.get(0).get(0);
							xml_str = xml_str +"<"+tag_name+">"+result+"</"+tag_name+">";
							CreditCard.mLogger.info("RLOS COMMON after adding ProfileSerNo+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}catch(Exception ex){
						/*	new CC_Common();*/
						//Commented for sonar
							CreditCard.mLogger.info("Exception in aqdding profileserno "+ CC_Common.printException(ex));
						}
					}
				}

				else if("AddrDet".equalsIgnoreCase(tag_name) || "AddressDetails".equalsIgnoreCase(tag_name)){
					CreditCard.mLogger.info("inside AddrDet deep: ");
					if(int_xml.containsKey(parent_tag))
					{
						//CreditCard.mLogger.info("inside 1st if"+" inside customer update req2");
						String xml_str = int_xml.get(parent_tag);
						//CreditCard.mLogger.info("RLOS COMMON"+" before adding address+ "+xml_str);
						//Deepak Changes for card creation address change 
						String Cust_type = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12);
						String customer_ntb=fetchIsNTBval("Primary");
						CreditCard.mLogger.info("card creation AddrDet Cust_type:  "+xml_str);
						CreditCard.mLogger.info("card creation AddrDet customer_ntb: "+xml_str);
						if( "PRIMARY".equalsIgnoreCase(Cust_type) && "-1".equalsIgnoreCase(customer_ntb)){
								xml_str = xml_str + getCustAddress_details(callName);
						}
						else{
								xml_str = xml_str + getCardCrationDummyAddress_details(); 
						}
						
						//CreditCard.mLogger.info("RLOS COMMON"+" after adding address+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}		                            	
				}

				else if("ProcessDate".equalsIgnoreCase(tag_name) || "ApplicationDate".equalsIgnoreCase(tag_name)){
					//CreditCard.mLogger.info("inside 1st if inside NEW_CREDITCARD_REQ req1");
					String xml_str = int_xml.get(parent_tag);
					Date d1=new Date();
					String Date="";

					SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
					Date=sf.format(d1);

					//d2.parse(sf.format(d1));
					xml_str =xml_str+ "<"+tag_name+">"+Date+"</"+ tag_name+">";

					//CreditCard.mLogger.info("PL COMMON  after adding DSAId:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}

				else if("ProcessedBy".equalsIgnoreCase(tag_name) ){
					String xml_str = int_xml.get(parent_tag);

					xml_str = xml_str + "<"+tag_name+">"+formObject.getUserName()
							+"</"+ tag_name+">";

					//CreditCard.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}
				else if("ApplicationNumber".equalsIgnoreCase(tag_name) || "InstitutionID".equalsIgnoreCase(tag_name)){
					//CreditCard.mLogger.info("inside 1st if inside customer update req1");
					String xml_str = int_xml.get(parent_tag);
					//change by saurabh on 28th Jan
					String wiNum = (formObject.getWFWorkitemName()).split("-")[1];
					xml_str = xml_str+"<"+tag_name+">"+wiNum+"</"+ tag_name+">";

					//CreditCard.mLogger.info("CC COMMON  after adding ApplicationNumber:  "+xml_str);
					int_xml.put(parent_tag, xml_str);	                            	
				}

				else if("PeriodicCashLimit".equalsIgnoreCase(tag_name) || "StatementCycle".equalsIgnoreCase(tag_name)){
					//CreditCard.mLogger.info("inside else if for PeriodicCashLimit");
					if(int_xml.containsKey(parent_tag))
					{
						//Deepak changes done on 04July2018 below mapping changed from income details statement cycle to Demographics>Card Details>Statement Cycle   
						String statCycle = formObject.getNGValue("cmplx_CardDetails_Statement_cycle");
						if(statCycle!=null && !"".equalsIgnoreCase(statCycle) && !" ".equalsIgnoreCase(statCycle) && !"--Select--".equalsIgnoreCase(statCycle)){
							String result = "";
							String subStr = statCycle.substring(0, statCycle.indexOf("of each month"));
							for(int i=0;i<subStr.length();i++){
								int num = Character.getNumericValue(subStr.charAt(i));
								if(num>=0 && num<=9){
									result+=subStr.charAt(i);
								}
							}
							String xml_str = int_xml.get(parent_tag);
							//CreditCard.mLogger.info("RLOS COMMON before adding PeriodicCashLimit+ "+xml_str);
							if("StatementCycle".equalsIgnoreCase(tag_name)){
								if(result.length()==1)
									result="-20"+result;

								else if(result.length()==2)
									result="-2"+result;
							}
							xml_str = xml_str +"<"+tag_name+">"+result+"</"+tag_name+">";
							//CreditCard.mLogger.info("RLOS COMMON"+" after adding PeriodicCashLimit+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}
					}
				}
				//Deepak changes done on 5 jan 2019 for CR PCSP-254 - 2 new tags added in card creation newAccountFlag & newPersonFlag 
				else if("NewPersonalFlag".equalsIgnoreCase(tag_name)){
					CreditCard.mLogger.info("new changes for NewPersonalFlag flag");
					String xml_str = int_xml.get(parent_tag);
					xml_str=xml_str+"<"+tag_name+">"+fetchIsPersonalval()+"</"+tag_name+">";
					int_xml.put(parent_tag, xml_str);
				}
				
				else if(tag_name.equalsIgnoreCase("OrgName")||tag_name.equalsIgnoreCase("Department")||tag_name.equalsIgnoreCase("Designation")){
					CreditCard.mLogger.info("Before calling getEmploymentOrCompanyDetails()");
					String xml_str = int_xml.get(parent_tag);
					xml_str=xml_str+getEmploymentOrCompanyDetails(tag_name);
					int_xml.put(parent_tag, xml_str);

				}
				//++below code added by nikhil for Self-Supp CR
				else if("NewAccountFlag".equalsIgnoreCase(tag_name)){
					CreditCard.mLogger.info("new changes for NewAccountFlag flag");
					String xml_str = int_xml.get(parent_tag);
					String Val="";
					String listView = "cmplx_CCCreation_cmplx_CCCreationGrid";
					String Cust_type = formObject.getNGValue(listView,formObject.getSelectedIndex(listView),12);					
					if("PRIMARY".equalsIgnoreCase(Cust_type))
					{
						Val="-1";
					}
					else if("SUPPLEMENT".equalsIgnoreCase(Cust_type))
					{
						Val="0";
					}
					xml_str=xml_str+"<"+tag_name+">"+Val+"</"+tag_name+">";
					int_xml.put(parent_tag, xml_str);
				}
				//--above code added by nikhil for Self-Supp CR
				
				//Deepak 12 Aug 2019 changes for supplymentry case 
				else if("AuthorizationLimit".equalsIgnoreCase(tag_name)&& "CardDetails".equalsIgnoreCase(parent_tag)){
					CreditCard.mLogger.info("new changes for NewAccountFlag flag");
					String xml_str = int_xml.get(parent_tag);
					String Val="";
					String listView = "cmplx_CCCreation_cmplx_CCCreationGrid";
					String Cust_type = formObject.getNGValue(listView,formObject.getSelectedIndex(listView),12);					
					if("SUPPLEMENT".equalsIgnoreCase(Cust_type))
					{
						Val=formObject.getNGValue(listView,formObject.getSelectedIndex(listView),6);//card limit
					}
					xml_str=xml_str+"<"+tag_name+">"+Val+"</"+tag_name+">";
					int_xml.put(parent_tag, xml_str);
				}
				else if(tag_name.equalsIgnoreCase("FeeProfileSerNo")||tag_name.equalsIgnoreCase("TxnProfileSerNo")){
					//CreditCard.mLogger.info("inside 1st if"+" inside ReferenceDetails for Card NOTIF");
					if(int_xml.containsKey(parent_tag))
					{
						try{
							String xml_str = int_xml.get(parent_tag);
							//CreditCard.mLogger.info("RLOS COMMON"+" before adding ReferenceDetails+ "+xml_str);
							String intrest_val = "";
							String intrest_Query = "";
							String card_product ="";
							//below if else added by akshay on 5/3/18 for drop 4
							/*if("SUPPLEMENT".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)))
							{
								 for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid");i++){
									 if(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13).equals(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,3)))
										 {
											card_product = formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,30);
											break;
										 }
								 }
							}
							else{*/
							card_product = formObject.getNGValue("CC_Creation_Product");

							//}
							if(card_product==null|| card_product.equals("")){
								intrest_val="NA";
							} 
							else{
								if(tag_name.equalsIgnoreCase("InterestProfile")){
									intrest_Query="select code from NG_MASTER_Int_Profile with (nolock) where Product = '"+card_product+"'";
								}
								else if(tag_name.equalsIgnoreCase("FeeProfile")||tag_name.equalsIgnoreCase("FeeProfileSerNo")){
									intrest_Query="select code from NG_MASTER_Fee_Profile with (nolock) where Product = '"+card_product+"'";
								}
								else if(tag_name.equalsIgnoreCase("TransactionFeeProfile")||tag_name.equalsIgnoreCase("TxnProfileSerNo")){
									intrest_Query="select code from NG_MASTER_TransactionFee_Profile with (nolock) where Product = '"+card_product+"'";
								}
								List<List<String>> intsert_list = formObject.getDataFromDataSource(intrest_Query);
								if(!intsert_list.isEmpty()){
									intrest_val = intsert_list.get(0).get(0);
								}	
							}

							xml_str = xml_str+"<"+tag_name+">"+intrest_val+"</"+ tag_name+">";
							//CreditCard.mLogger.info("RLOS COMMON"+" after adding ReferenceDetails+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}
						catch(Exception e){
							CreditCard.mLogger.info("CC Common"+ "Exception occured while setting intrest data");
						}
					}

				}
				//if changed ro elsif for DROp4 by saurabh.
				//++below code added by nikhil for Self-Supp CR
				
				else if("PRIMARY".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)) || ("SUPPLEMENT".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)) && formObject.getNGValue("cmplx_Customer_PAssportNo").equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13))  ))
					//--above code added by nikhil for Self-Supp CR
				{
					//CreditCard.mLogger.info("inside PRIMARY & self supplemantary condition tag_name: " + tag_name+" parent_tag: "+parent_tag);
					//case added by saurabh on 28th Jan
					if(tag_name.equalsIgnoreCase("FullName")){
						String xml_str = int_xml.get(parent_tag);
						String fullName = formObject.getNGValue("cmplx_Customer_FirstNAme")+" "+formObject.getNGValue("cmplx_Customer_MiddleNAme")+" "+formObject.getNGValue("cmplx_Customer_LastNAme");
						xml_str = xml_str + "<"+tag_name+">"+fullName+"</"+ tag_name+">";

						//CreditCard.mLogger.info("RLOS COMMON"+" after adding BankName+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}
					//Deepak commented on 12 Aug 2019 as per sameera email
					/*else if(tag_name.equalsIgnoreCase("ApplicationType")){
						String xml_str = int_xml.get(parent_tag);
						String tag_value = "";
						if("PRIMARY".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12))){
							tag_value="Primary";
						}else{
							tag_value="secondary";
						}
						
						xml_str = xml_str + "<"+tag_name+">"+tag_value+"</"+ tag_name+">";

						//CreditCard.mLogger.info("RLOS COMMON"+" after adding BankName+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}*/
					else if("Extensions".equalsIgnoreCase(tag_name)){
						//CreditCard.mLogger.info("inside 1st if"+" inside ReferenceDetails for Card NOTIF");
						if(int_xml.containsKey(parent_tag))
						{
							String xml_str = int_xml.get(parent_tag);
							//CreditCard.mLogger.info("RLOS COMMON"+" before adding ReferenceDetails+ "+xml_str);
							xml_str = xml_str + getExtensions_details(true);
							//CreditCard.mLogger.info("RLOS COMMON"+" after adding ReferenceDetails+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}

					}
					else if(tag_name.equalsIgnoreCase("CIFId")){
						String xml_str = int_xml.get(parent_tag);
						xml_str = xml_str + "<"+tag_name+">C"+formObject.getNGValue(form_control)+"</"+ tag_name+">";

						//CreditCard.mLogger.info("RLOS COMMON"+" after adding BankName+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}
					else if("CardEmbossingName".equalsIgnoreCase(tag_name)){
						String xml_str = int_xml.get(parent_tag);
						String tagValue="";
						//CreditCard.mLogger.info("PL COMMON  before adding isNTB:  "+xml_str);
						if("PRIMARY".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)) )
						{
							tagValue=formObject.getNGValue("cmplx_CardDetails_cardEmbossing_name");
						}
						else 
						{
							tagValue=formObject.getNGValue("cmplx_CardDetails_Self_card_embossing");
						}
						
						//tagValue=(tagValue.equalsIgnoreCase("true")?"-1":"0");
						//if(!tagValue.equals("")){
						xml_str = xml_str + "<"+tag_name+">"+tagValue+"</"+ tag_name+">";
						//}
						int_xml.put(parent_tag, xml_str);
						//CreditCard.mLogger.info("PL COMMON  after adding isNTB:  "+xml_str);
					}
					else if(tag_name.equalsIgnoreCase("IsNTB") || tag_name.equalsIgnoreCase("VIPFlag") || tag_name.equalsIgnoreCase("VIPFlg") || tag_name.equalsIgnoreCase("NonResidentFlag")||tag_name.equalsIgnoreCase("EStatementFlag")){
						String xml_str = int_xml.get(parent_tag);
						String tagValue="";
						if(tag_name.equalsIgnoreCase("IsNTB")){
							tagValue = fetchIsNTBval("Primary");
							//tagValue=(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true")?"-1":"0");
						}
						/*else if(tag_name.equalsIgnoreCase("IsNTB")){
						tagValue=(formObject.getNGValue("cmplx_Customer_NTB").equalsIgnoreCase("true")?"Y":"N");
					}*/
						/*else if((tag_name.equalsIgnoreCase("VIPFlag") || tag_name.equalsIgnoreCase("VIPFlg"))&&Call_name.equalsIgnoreCase("NEW_CREDITCARD_REQ")){
						tagValue=(formObject.getNGValue("cmplx_Customer_VIPFlag").equalsIgnoreCase("true")?"1":"0");
					}*/
						else if(tag_name.equalsIgnoreCase("VIPFlag") || tag_name.equalsIgnoreCase("VIPFlg")){
							//change by saurabh on 28th Jan
							tagValue=(formObject.getNGValue("cmplx_Customer_VIPFlag").equalsIgnoreCase("true")?"1":"0");
						}
						else if(tag_name.equalsIgnoreCase("NonResidentFlag")){
							tagValue=(formObject.getNGValue("cmplx_Customer_ResidentNonResident").equalsIgnoreCase("Resident")?"Y":"N");
						}	
						else if(tag_name.equalsIgnoreCase("EStatementFlag")){
							tagValue=(formObject.getNGValue("AlternateContactDetails_eStatementFlag").equalsIgnoreCase("Yes")?"Y":"N");
						}
						if(!tagValue.equals("")){
							xml_str = xml_str + "<"+tag_name+">"+tagValue+"</"+ tag_name+">";
						}


						//CreditCard.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}
					else if("AcctId".equalsIgnoreCase(tag_name)&& "AccountDetails".equalsIgnoreCase(parent_tag)){
						//CreditCard.mLogger.info("inside NEW_CREDITCARD_REQ for AcctId");
						
							String xml_str = int_xml.get(parent_tag);
							String tagValue="";
							String listView = "cmplx_CCCreation_cmplx_CCCreationGrid";
							String selectproduct_value = formObject.getNGValue(listView,formObject.getSelectedIndex(listView),5);
							int rowcount = formObject.getLVWRowCount(listView);
							for(int i=0;i<rowcount;i++){
								String gridproduct_Value = formObject.getNGValue(listView,i,5);
								String gridCustType = formObject.getNGValue(listView,i,12);
								if(gridproduct_Value.equalsIgnoreCase(selectproduct_value) && "PRIMARY".equalsIgnoreCase(gridCustType)){
									tagValue = formObject.getNGValue(listView,i,1);
									break;
								}
							}
							xml_str = "<"+tag_name+">"+tagValue+"</"+ tag_name+">";
							//CreditCard.mLogger.info("inside NEW_CREDITCARD_REQ for AcctId: "+xml_str);
							int_xml.put(parent_tag, xml_str);
					}
// Commented by aman for PCSP-291
				/*	else if("MaritalStatus".equalsIgnoreCase(tag_name)){
						CreditCard.mLogger.info("inside 1st if"+"inside maritial NEW_CREDITCARD_REQ");
						String xml_str = int_xml.get(parent_tag);
						String marital="5";
						if(formObject.getNGValue("cmplx_Customer_MAritalStatus").equalsIgnoreCase("M")){
							marital="2";
						}
						else if(formObject.getNGValue("cmplx_Customer_MAritalStatus").equalsIgnoreCase("S")){
							marital="1";
						}
						else if(formObject.getNGValue("cmplx_Customer_MAritalStatus").equalsIgnoreCase("D")){
							marital="3";
						}
						else if(formObject.getNGValue("cmplx_Customer_MAritalStatus").equalsIgnoreCase("W")){
							marital="4";
						}
						xml_str = xml_str+"<"+tag_name+">"+marital+"</"+ tag_name+">";

						CreditCard.mLogger.info("PL COMMON"+" after adding maritia] NEW_CREDITCARD_REQ:  "+xml_str);
						int_xml.put(parent_tag, xml_str);	                            	
					}*/
					// Commented by aman for PCSP-291

					/*else if("StatementCycle".equalsIgnoreCase(tag_name)){
					String xml_str = int_xml.get(parent_tag);
					String statCycle=formObject.getNGValue("cmplx_IncomeDetails_StatementCycle");
					if(statCycle.length()==1)
						statCycle="-20"+statCycle;

					else if(statCycle.length()==2)
						statCycle="-2"+statCycle;

					xml_str = xml_str + "<"+tag_name+">"+statCycle+"</"+ tag_name+">";

					CreditCard.mLogger.info("RLOS COMMON"+" after adding Minor flag+ "+xml_str);
					int_xml.put(parent_tag, xml_str);
				}*/

					else if("BranchId".equalsIgnoreCase(tag_name) || "BankSortCode".equalsIgnoreCase(tag_name)){
						String xml_str = "";
						if(int_xml.containsKey(parent_tag)){
							xml_str = int_xml.get(parent_tag);
						}
						String CardDispatch=formObject.getNGValue("AlternateContactDetails_CardDisp");
						String tagValue="";

						if(CardDispatch!=null && CardDispatch.equalsIgnoreCase("ByCourier")){
							tagValue="998";}
						else{
							tagValue=formObject.getNGValue("AlternateContactDetails_CustdomBranch")!=null?formObject.getNGValue("AlternateContactDetails_CustdomBranch"):"";
						}
							xml_str = xml_str+"<"+tag_name+">"+tagValue+"</"+ tag_name+">";

							//CreditCard.mLogger.info("RLOS COMMON"+" after adding bhrabc id flag+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
					}
					// Commented by aman for PCSP-291

					/*else if("Gender".equalsIgnoreCase(tag_name) ){
						//CreditCard.mLogger.info("inside 1st if inside customer update req1");
						String xml_str = int_xml.get(parent_tag);
						xml_str =xml_str+ "<"+tag_name+">"+"1"+"</"+ tag_name+">";

						//CreditCard.mLogger.info("PL COMMON  after adding DSAId:  "+xml_str);
						int_xml.put(parent_tag, xml_str);	                            	
					}
*/
					// Commented by aman for PCSP-291


					else{
						int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
					}
				}
				//++below code added by nikhil for Self-Supp CR
				else if( "SUPPLEMENT".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)) && !formObject.getNGValue("cmplx_Customer_PAssportNo").equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13)) )
					//--above code added by nikhil for Self-Supp CR
				{
					//CreditCard.mLogger.info("inside supplement tag name: "+tag_name);
					if("CIFId".equalsIgnoreCase(tag_name) ){
						String xml_str="";
						//CreditCard.mLogger.info("inside 1st if inside customer update req");
						xml_str = int_xml.get(parent_tag);
						xml_str =xml_str+ "<"+tag_name+">C"+formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),3)+"</"+ tag_name+">";
						//CreditCard.mLogger.info("PL COMMON  after adding CIFId:  "+xml_str);
						int_xml.put(parent_tag, xml_str);	                            	

						//CreditCard.mLogger.info("PL COMMON  after adding CIFId:  "+xml_str);
						int_xml.put(parent_tag, xml_str);	                            	
					}
					else if(tag_name.equalsIgnoreCase("CardEmbossingName")){
						String xml_str = int_xml.get(parent_tag);
						String tagValue="";
						//CreditCard.mLogger.info("PL COMMON  before adding isNTB:  "+xml_str);
						tagValue = getSupplementaryTagValue(9);
						//tagValue=(tagValue.equalsIgnoreCase("true")?"-1":"0");
						//if(!tagValue.equals("")){
						xml_str = xml_str + "<"+tag_name+">"+tagValue+"</"+ tag_name+">";
						//}
						int_xml.put(parent_tag, xml_str);
						//CreditCard.mLogger.info("PL COMMON  after adding isNTB:  "+xml_str);
					}
					else if(tag_name.equalsIgnoreCase("IsNTB")){
						//CreditCard.mLogger.info("inside supplement tag name IsNTB: "+tag_name);
						String xml_str = int_xml.get(parent_tag);
						String tagValue="";
						//CreditCard.mLogger.info("PL COMMON  before adding isNTB:  "+xml_str);
						tagValue = fetchIsNTBval("Supplement");
						//tagValue=(tagValue.equalsIgnoreCase("true")?"-1":"0");
						//if(!tagValue.equals("")){
						xml_str = xml_str + "<"+tag_name+">"+tagValue+"</"+ tag_name+">";
						//}
						int_xml.put(parent_tag, xml_str);
						//CreditCard.mLogger.info("PL COMMON  after adding isNTB:  "+xml_str);
					}
					else if("AcctId".equalsIgnoreCase(tag_name)&& "AccountDetails".equalsIgnoreCase(parent_tag)){
						String xml_str = int_xml.get(parent_tag);
						String tagValue="";
						
						String listView = "cmplx_CCCreation_cmplx_CCCreationGrid";
						String selectproduct_value = formObject.getNGValue(listView,formObject.getSelectedIndex(listView),5);
						int rowcount = formObject.getLVWRowCount(listView);
						for(int i=0;i<rowcount;i++){
							String gridproduct_Value = formObject.getNGValue(listView,i,5);
							String gridCustType = formObject.getNGValue(listView,i,12);
							if(gridproduct_Value.equalsIgnoreCase(selectproduct_value) && "PRIMARY".equalsIgnoreCase(gridCustType)){
								tagValue = formObject.getNGValue(listView,i,1);
								break;
							}
						}
						xml_str = "<"+tag_name+">"+tagValue+"</"+ tag_name+">";
						int_xml.put(parent_tag, xml_str);
					}
					
					else if("Extensions".equalsIgnoreCase(tag_name)){
						//CreditCard.mLogger.info("inside 1st if"+" inside ReferenceDetails for Card NOTIF");
						if(int_xml.containsKey(parent_tag))
						{
							String xml_str = int_xml.get(parent_tag);
							//CreditCard.mLogger.info("RLOS COMMON"+" before adding ReferenceDetails+ "+xml_str);
							xml_str = xml_str + getExtensions_details(false);
							//CreditCard.mLogger.info("RLOS COMMON"+" after adding ReferenceDetails+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}

					}

					else if("TitleToEmailIdAggregateTag".equalsIgnoreCase(tag_name) ){
						String xml_str = int_xml.get(parent_tag);
						//CreditCard.mLogger.info("RLOS COMMON"+ " before adding guarantor PersonDetails+ " + xml_str);
						xml_str = xml_str + getSupplement_PersonDetails(formObject,callName);
						//CreditCard.mLogger.info("RLOS COMMON"+ " after adding PersonDetails " + xml_str);
						int_xml.put(parent_tag, xml_str);

					}

					else{
						//CreditCard.mLogger.info("inside supplement tag name else condition: "+tag_name);
						int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
					}
				}
			}
		}
		catch(Exception e){
			CreditCard.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + "+e.getMessage());
			CreditCard.mLogger.info("CC_INtegration Exception in CARD services custom function:  "+CC_Common.printException(e));
		}
		return int_xml;
	}

	private String getCardCrationDummyAddress_details() {
		// TODO Auto-generated method stub
		return "<AddressDetails>"
		+"<AddrType>Statement</AddrType>"
            +"<UseExistingAddress>1</UseExistingAddress>"
            +"<AddressReferenceLink>1</AddressReferenceLink>"
            +"</AddressDetails>"
         	+"<AddressDetails>"
            +"<AddrType>Additional</AddrType>"
            +"<UseExistingAddress>0</UseExistingAddress>"        
            +"<AddressReferenceLink>0</AddressReferenceLink>"
         +"</AddressDetails>";
	}

	public String getSupplementaryTagValue(int colIndex){
		try{
			String stat = "";
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String listViewName = "SupplementCardDetails_cmplx_supplementGrid";
			String passport = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13);
			String cardprod = formObject.getNGValue("CC_Creation_Product");

			for(int i=0;i<formObject.getLVWRowCount(listViewName);i++){
				String gridPassport = formObject.getNGValue(listViewName,i,3);
				String gridcardProd = formObject.getNGValue(listViewName,i,30);
				if(passport.equalsIgnoreCase(gridPassport) && cardprod.equalsIgnoreCase(gridcardProd)){
					stat = formObject.getNGValue(listViewName,i,colIndex);
					break;
				}
			}
			return stat;
		}catch(Exception ex){
			CreditCard.mLogger.info("Exception in getting Marital Status:  "+CC_Common.printException(ex));
			return "";
		}

	}
	//Deepak new method added for fetchAddressFlagVal
	public String fetchAddressFlagVal() {
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();	
			String AddressFlagVal = "-1";
			String listView = "cmplx_CCCreation_cmplx_CCCreationGrid";
			String Cust_type = formObject.getNGValue(listView,formObject.getSelectedIndex(listView),12);
			String selectPassport = formObject.getNGValue(listView,formObject.getSelectedIndex(listView),13);
			//++below code added by nikhil for Self-Supp CR
			if("SUPPLEMENT".equalsIgnoreCase(Cust_type) && formObject.getNGValue("cmplx_Customer_PAssportNo").equalsIgnoreCase(selectPassport))
			{
				Cust_type="PRIMARY";
			}
			//--above code added by nikhil for Self-Supp CR
			String selectCIF = formObject.getNGValue(listView,formObject.getSelectedIndex(listView),3);
			int rowcount = formObject.getLVWRowCount(listView);
			int createdCards = 0;
			for(int i=0;i<rowcount;i++){
				String gridPassprtValue = formObject.getNGValue(listView,i,13);
				String gridCifValue = formObject.getNGValue(listView,i,3);
				if(formObject.getNGValue(listView, i, 8).equals("Y") && gridPassprtValue.equalsIgnoreCase(selectPassport) && gridCifValue.equalsIgnoreCase(selectCIF)){
					createdCards++;
					break;
				}
			}
			if(createdCards>0){
				AddressFlagVal="0";
				return AddressFlagVal;
			}
			if(Cust_type.equalsIgnoreCase("Primary")){
				String query = "select count(distinct ECRN) from ng_RLOS_CUSTEXPOSE_CustInfo with (nolock) where Child_Wi = '"+formObject.getWFWorkitemName()+"' and CifId= '"+selectCIF+"' and ecrn !='' and ecrn is not null";
				List<List<String>> count = formObject.getNGDataFromDataCache(query);

				if(count!=null && count.size()>0 && count.get(0)!=null){
					if(Integer.parseInt(count.get(0).get(0))>0){
						AddressFlagVal="0";
					}
				}
			}
			CreditCard.mLogger.info("Inside fetchAddressFlagVal function before return is: "+AddressFlagVal);
			return AddressFlagVal;
		}catch(Exception ex){
			CreditCard.mLogger.info("CC_INtegration Exception in fetchAddressFlagVal) Function:  "+CC_Common.printException(ex));
			return "";
		}
	}
	//Deepak changes done on 5 jan 2019 for CR PCSP-254 - 2 new tags added in card creation newAccountFlag & newPersonFlag
	public String fetchIsNTBval(String applicantType) {
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();	
			String ntbVal = "-1";
			String listView = "cmplx_CCCreation_cmplx_CCCreationGrid";
			//CreditCard.mLogger.info("Inside fetchNTBVal function ");
			
			if(applicantType.equalsIgnoreCase("Primary")){
				//Deepak 20 Dec PCSP 142 Changes to send NTB customer as existing for second card if his first card got created successfully start 
				String selectPassport = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13);
				String selectCIF = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),3);//column no corrected by deepak
				int rowcount = formObject.getLVWRowCount(listView);
				int createdCards = 0;
				for(int i=0;i<rowcount;i++){
					String gridPassprtValue = formObject.getNGValue(listView,i,13);
					String gridCifValue = formObject.getNGValue(listView,i,3);//column no corrected by deepak
					if(formObject.getNGValue(listView, i, 8).equals("Y") && gridPassprtValue.equalsIgnoreCase(selectPassport) && gridCifValue.equalsIgnoreCase(selectCIF)){
						createdCards++;
						break;
					}
				}
				if(createdCards>0){
					ntbVal="0";
					return ntbVal;
				}
				//Deepak 20 Dec PCSP 142 Changes to send NTB customer as existing for second card if his first card got created successfully END
				String selectECRN = formObject.getNGValue("CC_Creation_ECRN");
				//CreditCard.mLogger.info("Inside fetchNTBVal function primary");
				//Deepak table name changed from ng_RLOS_CUSTEXPOSE_CardDetails to ng_RLOS_CUSTEXPOSE_CustInfo for closed card as well. 12 Aug 2019
				//String query = "select count(distinct ECRN) from ng_RLOS_CUSTEXPOSE_CustInfo with (nolock) where Child_Wi = '"+formObject.getWFWorkitemName()+"' and CifId= '"+selectCIF+"' and ecrn !='' and ecrn is not null";
				//Deepak changes done for PCAS-2931
				String query = "select count(ECRN) from (select distinct ECRN from ng_RLOS_CUSTEXPOSE_CustInfo with (nolock) where Child_Wi = '"+formObject.getWFWorkitemName()+"' and CifId= '"+selectCIF+"' and ecrn !='' and ecrn is not null union all select distinct ECRN from NG_RLOS_gr_CCCreation where ECRN='"+selectECRN+"' and CardCreated='Y') as ECRN_temp";
				CreditCard.mLogger.info("CC Integration query for ECRN "+query);
				List<List<String>> count = formObject.getDataFromDataSource(query);
				CreditCard.mLogger.info("CC Integration query for ECRN COUNT VALUE "+count);
				if(count!=null && count.size()>0 && count.get(0)!=null){
					if(Integer.parseInt(count.get(0).get(0))>0){
						ntbVal="0";
					}
				}
			}
			else if (applicantType.equalsIgnoreCase("Supplement")){
				ntbVal="0";
			}
			CreditCard.mLogger.info("Inside fetchNTBVal function before return is: "+ntbVal);
			return ntbVal;
		}catch(Exception ex){
			CreditCard.mLogger.info("CC_INtegration Exception in CARD services custom function:  "+CC_Common.printException(ex));
			return "";
		}
	}
	public String fetchIsPersonalval() {
		String IsPersonalflag_val = "-1";
		try{
			CreditCard.mLogger.info("Inside fetchIsPersonalval" );
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();
			String listView = "cmplx_CCCreation_cmplx_CCCreationGrid";
			String selectPassport = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13);
			String selectCIF = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),3);
			String App_type=formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12);
			CreditCard.mLogger.info("Inside fetchIsPersonalval selectPassport: "+selectPassport );
			CreditCard.mLogger.info("Inside fetchIsPersonalval selectCIF: "+selectCIF );
			
			int rowcount = formObject.getLVWRowCount(listView);
			int createdCards = 0;
			for(int i=0;i<rowcount;i++){
				String gridPassprtValue = formObject.getNGValue(listView,i,13);
				String gridCifValue = formObject.getNGValue(listView,i,3);
				CreditCard.mLogger.info("Inside fetchIsPersonalval gridPassprtValue: "+gridPassprtValue );
				CreditCard.mLogger.info("Inside fetchIsPersonalval gridCifValue: "+gridCifValue );
				
				if(formObject.getNGValue(listView, i, 8).equals("Y") && gridPassprtValue.equalsIgnoreCase(selectPassport) && gridCifValue.equalsIgnoreCase(selectCIF)){
					createdCards++;
					break;
				}
			}
			if(createdCards>0){
				return selectCIF;
			}
			
			
			
			//++below code added by nikhil for Self-Supp CR
			if("SUPPLEMENT".equalsIgnoreCase(App_type)) 
			{
				CreditCard.mLogger.info("Inside fetchIsPersonalval SUPPLEMENT condition : ");
				String LinkedProduct="";
				for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_supplementGrid");i++){
					if(selectPassport.equalsIgnoreCase(formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,3))){
						LinkedProduct = formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,41);
						CreditCard.mLogger.info("row cout: "+i+" LinkedProduct value: "+ LinkedProduct );
						break;
					}
				}
				if(LinkedProduct.contains("CAPS")){
					return selectCIF;
				}
				else{
					return "-1";
				}
				
			}
			
			//Deepak Query changed by deepak to include CIF ID. 04 Aug 2019 
			String query = "select count(distinct ECRN) from ng_RLOS_CUSTEXPOSE_CustInfo with (nolock) where Child_Wi = '"+formObject.getWFWorkitemName()+"' and  CifId= '"+selectCIF+"' and ecrn !='' and ecrn is not null";
			//CreditCard.mLogger.info("CC Integration query for ECRN "+query);
			List<List<String>> count = formObject.getNGDataFromDataCache(query);

			if(count!=null && count.size()>0 && count.get(0)!=null && Integer.parseInt(count.get(0).get(0))>0){
				return selectCIF;
			}
		}
		catch(Exception ex){
			CreditCard.mLogger.info("CC_INtegration Exception in fetchIsPersonalval function:  "+CC_Common.printException(ex));
		}
		return IsPersonalflag_val;
	}
	//Existing method commented for CR PCSP-478 - 2 new tags added in card creation newAccountFlag & newPersonFlag
	/*public String fetchIsNTBval_old(String applicantType) {
		try{
			FormReference formObject = FormContext.getCurrentInstance().getFormReference();	
			String ntbVal = "-1";
			String listView = "cmplx_CCCreation_cmplx_CCCreationGrid";
			//CreditCard.mLogger.info("Inside fetchNTBVal function ");
			
			//Deepak 20 Dec PCSP 142 Changes to send NTB customer as existing for second card if his first card got created successfully start 
			String selectPassport = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13);
			String selectCIF = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),2);
			int rowcount = formObject.getLVWRowCount(listView);
			int createdCards = 0;
			for(int i=0;i<rowcount;i++){
				String gridPassprtValue = formObject.getNGValue(listView,i,13);
				String gridCifValue = formObject.getNGValue(listView,i,2);
				if(formObject.getNGValue(listView, i, 7).equals("Y") && gridPassprtValue.equalsIgnoreCase(selectPassport) && gridCifValue.equalsIgnoreCase(selectCIF)){
					createdCards++;
					break;
				}
			}
			if(createdCards>0){
				ntbVal="0";
				return ntbVal;
			}
			//Deepak 20 Dec PCSP 142 Changes to send NTB customer as existing for second card if his first card got created successfully END
			
			if(applicantType.equalsIgnoreCase("Primary")){
				
				//CreditCard.mLogger.info("Inside fetchNTBVal function primary");
				String query = "select count(distinct ECRN) from ng_RLOS_CUSTEXPOSE_CardDetails with (nolock) where Child_Wi = '"+formObject.getWFWorkitemName()+"' and ecrn !='' and ecrn is not null";
				//CreditCard.mLogger.info("CC Integration query for ECRN "+query);
				List<List<String>> count = formObject.getNGDataFromDataCache(query);

				if(count!=null && count.size()>0 && count.get(0)!=null){
					if(Integer.parseInt(count.get(0).get(0))>0){
						ntbVal="0";
					}
					else{
						//int rowcount = formObject.getLVWRowCount(listView);
						 createdCards = 0;
						for(int i=0;i<rowcount;i++){
							if(formObject.getNGValue(listView, i, 8).equals("Y")){
								//CreditCard.mLogger.info("Value for Card already created for "+i+"row:"+formObject.getNGValue(listView, i, 8));
								createdCards++;
								break;
							}
						}
						if(createdCards>0){
							ntbVal="0";
						}
					}
				}
			}
			else if (applicantType.equalsIgnoreCase("Supplement")){
				//CreditCard.mLogger.info("Inside fetchNTBVal function supplement");
				String ntb = new CC_Common().checkSuppPrimaryNTB();
				//CreditCard.mLogger.info("Inside fetchNTBVal function supplement ntb value is: "+ntb);
				if("false".equalsIgnoreCase(ntb)){
					//this is ntb case.
					//Deepak 20 Dec PCSP 142 Changes below code moved to above & NTB true condition added.
					ntbVal="0";
				}
				else {
					//Deepak 20 Dec PCSP-254 changes done as in this condition supplimentry will be NTB & for NTB -1 flag need to send in condition.
					ntbVal = "-1";
				}
				//CreditCard.mLogger.info("Inside fetchNTBVal function supplement value is: "+ntbVal);
			}
			CreditCard.mLogger.info("Inside fetchNTBVal function before return is: "+ntbVal);
			return ntbVal;
		}catch(Exception ex){
			CreditCard.mLogger.info("CC_INtegration Exception in CARD services custom function:  "+CC_Common.printException(ex));
			return "";
		}
	}

*/

	/*          Function Header:

	 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 10/12/2017              
		Author                              : Saurabh Gupta              
		Description                         : get Contract details for Card Notification       

	 ***********************************************************************************  */
	public String getcontact_details(){
		CreditCard.mLogger.info("RLOSCommon java file inside getContact_details : ");
		FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		String  Contactdetails_xml_str ="";
		Contactdetails_xml_str = "<ContactDetails><PrefModeOfContact>Phone</PrefModeOfContact><PhnDet><PhoneType>MobileNumber1</PhoneType><PhnCountryCode>00971</PhnCountryCode><CityCode></CityCode><PhnLocalCode>00971</PhnLocalCode>";
		String App_type=formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12);
		String PassPort=formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13);
		//++below code added by nikhil for Self-Supp CR
		if("SUPPLEMENT".equalsIgnoreCase(App_type) && formObject.getNGValue("cmplx_Customer_PAssportNo").equals(PassPort)) 
		{
			App_type="PRIMARY";
		}
		//--above code added by nikhil for Self-Supp CR
		try{
			if("PRIMARY".equalsIgnoreCase(App_type))
			{
				Contactdetails_xml_str=Contactdetails_xml_str+"<PhoneNumber>"+formObject.getNGValue("cmplx_Customer_MobileNo")+"</PhoneNumber><PhnPrefFlag>Y</PhnPrefFlag></PhnDet></ContactDetails>";
				Contactdetails_xml_str = Contactdetails_xml_str+"<ContactDetails><PrefModeOfContact>Email</PrefModeOfContact><EmailDet><MailIdType>WORKEML</MailIdType><EmailID>"+formObject.getNGValue("AlternateContactDetails_Email1")+"</EmailID><MailPrefFlag>Y</MailPrefFlag></EmailDet></ContactDetails>";

				if(!formObject.getNGValue("AlternateContactDetails_ResidenceNo").equals(""))
					{
						Contactdetails_xml_str=Contactdetails_xml_str+"<ContactDetails><PrefModeOfContact>Phone</PrefModeOfContact><PhnDet><PhoneType>HOMEPH1</PhoneType><PhnCountryCode>00971</PhnCountryCode><CityCode></CityCode><PhnLocalCode>00971</PhnLocalCode><PhoneNumber>"+formObject.getNGValue("AlternateContactDetails_ResidenceNo")+"</PhoneNumber><PhnPrefFlag>N</PhnPrefFlag></PhnDet></ContactDetails>";
					}
					//added by akshay for procs 11485,11469,11488
					if(!formObject.getNGValue("AlternateContactDetails_OfficeNo").equals("")){
						Contactdetails_xml_str = Contactdetails_xml_str+"<ContactDetails><PrefModeOfContact>Phone</PrefModeOfContact><PhnDet><PhoneType>OFFCPH1</PhoneType><PhnCountryCode>00971</PhnCountryCode><CityCode></CityCode><PhnLocalCode>00971</PhnLocalCode><PhoneNumber>"+formObject.getNGValue("AlternateContactDetails_OfficeNo")+"</PhoneNumber><PhnPrefFlag>N</PhnPrefFlag></PhnDet></ContactDetails>";
					}

					if(!formObject.getNGValue("AlternateContactDetails_HomeCOuntryNo").equals("")){
						Contactdetails_xml_str = Contactdetails_xml_str+"<ContactDetails><PrefModeOfContact>Phone</PrefModeOfContact><PhnDet><PhoneType>OverseasPhone</PhoneType><PhnCountryCode>00971</PhnCountryCode><CityCode></CityCode><PhnLocalCode>00971</PhnLocalCode><PhoneNumber>"+formObject.getNGValue("AlternateContactDetails_HomeCOuntryNo")+"</PhoneNumber><PhnPrefFlag>N</PhnPrefFlag></PhnDet></ContactDetails>";
					}

					if(!formObject.getNGValue("AlternateContactDetails_Email2").equals(""))
					{
							Contactdetails_xml_str = Contactdetails_xml_str+"<ContactDetails><PrefModeOfContact>Email</PrefModeOfContact><EmailDet><MailIdType>HOMEEML</MailIdType><EmailID>"+formObject.getNGValue("AlternateContactDetails_Email2")+"</EmailID><MailPrefFlag>N</MailPrefFlag></EmailDet></ContactDetails>";
					}
				}

					else if( App_type.equalsIgnoreCase("Supplement") && formObject.getLVWRowCount("SupplementCardDetails_cmplx_supplementGrid")>0){ 
						for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_supplementGrid");i++){
							if(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13).equals(formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,3)))
							{
								Contactdetails_xml_str=Contactdetails_xml_str+"<PhoneNumber>"+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,6)+"</PhoneNumber>";
								Contactdetails_xml_str = Contactdetails_xml_str+"<PhnPrefFlag>Y</PhnPrefFlag></PhnDet></ContactDetails><ContactDetails><PrefModeOfContact>Email</PrefModeOfContact><EmailDet><MailIdType>WORKEML</MailIdType><EmailID>"+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,21)+"</EmailID><MailPrefFlag>Y</MailPrefFlag></EmailDet></ContactDetails><ContactDetails><PrefModeOfContact>Phone</PrefModeOfContact><PhnDet><PhoneType>HOMEPH2</PhoneType><PhnCountryCode>00971</PhnCountryCode>";
								Contactdetails_xml_str = Contactdetails_xml_str+"<CityCode></CityCode><PhnLocalCode>00971</PhnLocalCode><PhoneNumber>"+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,6)+"</PhoneNumber><PhnPrefFlag>N</PhnPrefFlag></PhnDet></ContactDetails>";
								break;
							}
						}
					}
					else{
						Contactdetails_xml_str="";
					}
				}
				catch(Exception e){
					Contactdetails_xml_str="";
					CreditCard.mLogger.info("PL Common java file Exception occured in get lien details method : "+CC_Common.printException(e));
				}

				return Contactdetails_xml_str;

			}

			public String getEmp_details(boolean selfEmpcase, boolean primaryApp){
				FormReference formObject = FormContext.getCurrentInstance().getFormReference();
				CreditCard.mLogger.info("RLOSCommon java file inside getEmp_details : ");
				String Salarydate=formObject.getNGValue("cmplx_IncomeDetails_SalaryDay");
				//Done for PCSP-101
				String Designation="";
				try
				{
					String sQuery = "select Description from ng_master_Designation with (nolock) where code='" +  formObject.getNGValue("cmplx_EmploymentDetails_Designation") + "'";

					//RLOS.mLogger.info("RLOS val change Value of dob is:"+sQuery);

					List<List<String>> recordList = formObject.getNGDataFromDataCache(sQuery);
					//RLOS.mLogger.info("RLOS val change Vasdfsdflue of dob is:"+recordList.get(0).get(0));
					if(recordList.get(0).get(0)!= null && recordList.get(0)!=null && !"".equalsIgnoreCase(recordList.get(0).get(0)) && recordList!=null)
					{
						Designation=recordList.get(0).get(0);
					}
				}
				catch(Exception e)
				{
					CreditCard.mLogger.info("In designation"+ e.getStackTrace());
				}
				//Done for PCSP-101
				String  Empdetails_xml_str ="";
				try{
					if(!selfEmpcase){
						Empdetails_xml_str +="<EmploymentDetails>";
						try{//Done by aman for CR 0712
							//Changes done by deepak as the tag seq was incorrect
							if(primaryApp){
								Empdetails_xml_str += "<EmploymentType>"+formObject.getNGValue("cmplx_EmploymentDetails_Emp_Type")+"</EmploymentType>";
							}
							else{
								Empdetails_xml_str += "<EmploymentType>"+getSuppCardnotifdata("EmploymentType")+"</EmploymentType>";
							}
							//Done by aman for CR 0712
							if(primaryApp)
								Empdetails_xml_str += "<EmployeeNumber>"+formObject.getNGValue("cmplx_EmploymentDetails_StaffID")+"</EmployeeNumber>";
							if(primaryApp)
								Empdetails_xml_str += "<EmployerId>"+formObject.getNGValue("cmplx_EmploymentDetails_EMpCode")+"</EmployerId>";
							if(primaryApp)
								Empdetails_xml_str += "<EmployerName>"+formObject.getNGValue("cmplx_EmploymentDetails_EmpName")+"</EmployerName>";
							//Empdetails_xml_str += "<EmploymentStatus>"+formObject.getNGValue("cmplx_EmploymentDetails_EmpStatus")+"</EmploymentStatus>"; commented until mapping received
							if(primaryApp){
								Empdetails_xml_str += "<Designation>"+Designation+"</Designation>";
							}
							else{
								Empdetails_xml_str += "<Designation>"+getSuppCardnotifdata("Designation")+"</Designation>";
							}
							if(primaryApp)
								Empdetails_xml_str += "<Department>"+formObject.getNGValue("cmplx_EmploymentDetails_Dept")+"</Department>";
							if(primaryApp){
								//changed by nikhil 05/01/2018 for Send detail to CAPS error
								Empdetails_xml_str += "<Occupation>"+formObject.getNGValue("cmplx_EmploymentDetails_Designation")+"</Occupation>";
							}
							else{
								Empdetails_xml_str += "<Occupation>"+getSuppCardnotifdata("Occupation")+"</Occupation>";
							}
							if(primaryApp)
								Empdetails_xml_str += "<EmploymentPeriod>"+getEmploymentPeriod()+"</EmploymentPeriod>";
							if(formObject.getNGValue("cmplx_EmploymentDetails_DOJ")!=null && !"".equals(formObject.getNGValue("cmplx_EmploymentDetails_DOJ")) && primaryApp){
								String doj = formObject.getNGValue("cmplx_EmploymentDetails_DOJ");
								if(doj.contains("/")){
									doj = doj.split("/")[2]+"-"+doj.split("/")[1]+"-"+doj.split("/")[0];
								}
								Empdetails_xml_str += "<DateOfJoining>"+doj+"</DateOfJoining>";
							}
							
							//Changes done by deepak as the tag seq was incorrect for CR 0712
							Empdetails_xml_str += "<OrganisationType>CurrentOrg</OrganisationType><SalaryTransferFlag>"+formObject.getNGValue("cmplx_IncomeDetails_SalaryXferToBank")+"</SalaryTransferFlag>";
							if(formObject.getNGValue("cmplx_IncomeDetails_SalaryDay")!=null && !"".equalsIgnoreCase(formObject.getNGValue("cmplx_IncomeDetails_SalaryDay"))){
								Calendar c = Calendar.getInstance();
								Empdetails_xml_str += "<SalaryDate>"+(c.get(Calendar.YEAR)+"-"+(String.valueOf(c.get(Calendar.MONTH)+1).length()==1?"0"+(c.get(Calendar.MONTH)+1):(c.get(Calendar.MONTH)+1))+"-"+(Salarydate.length()==1?"0"+formObject.getNGValue("cmplx_IncomeDetails_SalaryDay"):formObject.getNGValue("cmplx_IncomeDetails_SalaryDay")))+"</SalaryDate>";
							}
							Empdetails_xml_str +="<SalaryDetails>";
							try{
								//Deepak changes done for PCAS-2746//Deepak reverted back as per rachit 10 Nov 2019
								Empdetails_xml_str += "<GrossSalary>"+formObject.getNGValue("cmplx_IncomeDetails_grossSal").replace(",", "")+"</GrossSalary>";
								Empdetails_xml_str += "<Commission>"+formObject.getNGValue("cmplx_IncomeDetails_Commission_Avg").replace(",", "")+"</Commission>";
							}catch(Exception e){
								CreditCard.mLogger.info("CC Integ java file Exception occured in getEmp_details method : "+CC_Common.printException(e));
							}
							Empdetails_xml_str +="</SalaryDetails>";
						}catch(Exception e){
							CreditCard.mLogger.info("CC Integ java file Exception occured in getEmp_details method : "+CC_Common.printException(e));
						}
						Empdetails_xml_str +="</EmploymentDetails>";
						
						//Done by aman for CR 0712
						if (formObject.getNGValue("cmplx_EmploymentDetails_LOSPrevious")!=null &&  !"".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_LOSPrevious"))){
							Empdetails_xml_str +="<EmploymentDetails><EmploymentType>S</EmploymentType><OrganisationType>PreviousOrg</OrganisationType><JobDuration>"+formObject.getNGValue("cmplx_EmploymentDetails_LOSPrevious")+"</JobDuration></EmploymentDetails>";
						}
						//Done by aman for CR 0712
					}


					else{
						String designation = getDesignationAuthSign();
						Empdetails_xml_str +="<EmploymentDetails>";
						try{
							if(primaryApp){
								Empdetails_xml_str += "<EmploymentType>"+"Self employed"+"</EmploymentType>";
							}
							else{
								Empdetails_xml_str += "<EmploymentType>"+getSuppCardnotifdata("EmploymentType")+"</EmploymentType>";
							}

							if(primaryApp){
								Empdetails_xml_str += "<Designation>"+designation+"</Designation>";
							}
							else{
								Empdetails_xml_str += "<Designation>"+getSuppCardnotifdata("Designation")+"</Designation>";
							}

							if(primaryApp){
								Empdetails_xml_str += "<Occupation>"+designation+"</Occupation>";
							}
							else{
								Empdetails_xml_str += "<Occupation>"+getSuppCardnotifdata("Occupation")+"</Occupation>";
							}
							CreditCard.mLogger.info("before averageBalance tag:");
							Empdetails_xml_str += "<AverageBalance>"+getAvgBal()+"</AverageBalance>";//added by akshay for proc 11490
							CreditCard.mLogger.info("Empdetails_xml_str: +"+Empdetails_xml_str);
							//Empdetails_xml_str += "<BusinessInfo><MonthlyCrTrnOvrAmt>"+formObject.getNGValue("cmplx_IncomeDetails_CredTurnover")+"</MonthlyCrTrnOvrAmt></BusinessInfo>";//added by akshay for proc 11490
						}
						catch(Exception e)
						{
							CreditCard.mLogger.info("CC Integ java file Exception occured in getEmp_details method : "+CC_Common.printException(e));
						}
						Empdetails_xml_str +="</EmploymentDetails>";
					}

				}
				catch(Exception e){
					Empdetails_xml_str="";
					CreditCard.mLogger.info("PL Common java file Exception occured in getEmp_details method : "+CC_Common.printException(e));
				}
				CreditCard.mLogger.info("Empdetails_xml_str: +"+Empdetails_xml_str);
				return Empdetails_xml_str;
			}

			public String getAvgBal(){
				FormReference formObject = FormContext.getCurrentInstance().getFormReference();
				try{
					CreditCard.mLogger.info("inside 1st if inside AverageBalance tag card notif");
					String avgBal = "";
					String query = "select case when (select COUNT(Child_Wi) from ng_rlos_IGR_Eligibility_CardProduct with(nolock) where Child_Wi = '"+formObject.getWFWorkitemName()+"' and (Deviation_Code_Refer like '%RC044%' or Deviation_Code_Refer like '%RC045%'))>0 then total_avg_6  else total_avg_last1year  end from NG_RLOS_FinacleCore with(nolock) where wi_name = '"+formObject.getWFWorkitemName()+"'";
					List<List<String>> result = formObject.getDataFromDataSource(query);
					CreditCard.mLogger.info("averageBalance query:"+query);
					if(result!=null && result.size()>0){
						avgBal = result.get(0).get(0);
					}
					CreditCard.mLogger.info("averageBalance result:"+avgBal);
					return avgBal;

				}catch(Exception e){
					CreditCard.mLogger.info("PL Common java file Exception occured in getEmp_details method : "+CC_Common.printException(e));
					return "";
				}
			}

			public String getSuppCardnotifdata(String tag_name){
				FormReference formObject = FormContext.getCurrentInstance().getFormReference();
				if("EmploymentType".equalsIgnoreCase(tag_name) || "Designation".equalsIgnoreCase(tag_name) || "MarketingCode".equalsIgnoreCase(tag_name) || "Occupation".equalsIgnoreCase(tag_name)){
					String status="",Occupation="",marketCode="";
					//CreditCard.mLogger.info("RLOS COMMON"+ " before adding guarantor EmploymentType " + xml_str);
					for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid");i++){
						if(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13).equals(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,3))){
							status=formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,32);
							Occupation=formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,31);
							marketCode=formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,26);
							break;
						}
					}
					String Designation="";
					try
					{
						String sQuery = "select Description from ng_master_Designation with (nolock) where code='" +  Occupation + "'";

						//RLOS.mLogger.info("RLOS val change Value of dob is:"+sQuery);

						List<List<String>> recordList = formObject.getNGDataFromDataCache(sQuery);
						//RLOS.mLogger.info("RLOS val change Vasdfsdflue of dob is:"+recordList.get(0).get(0));
						if(recordList.get(0).get(0)!= null && recordList.get(0)!=null && !"".equalsIgnoreCase(recordList.get(0).get(0)))//Commented for Sonar
						{
							Designation=recordList.get(0).get(0);
						}
					}
					catch(Exception e)
					{
						CreditCard.mLogger.info("In designation"+ e.getStackTrace());
					}

					if("EmploymentType".equalsIgnoreCase(tag_name)){
						return status;
					}
					//below code done by nikhil for Send details to CAPs error 05/01/2018
					else if("Designation".equalsIgnoreCase(tag_name)){
						return Designation;
					}
					else if("Occupation".equalsIgnoreCase(tag_name)){
						return Occupation;
					}
					else if("MarketingCode".equalsIgnoreCase(tag_name)){
						return marketCode;
					}


				}
				return "";
			}

			public String getDesignationAuthSign(){
				String desig = null;
				try{
					FormReference formObject = FormContext.getCurrentInstance().getFormReference();
					String query = "select top 1 Designation from ng_rlos_gr_AuthSignDetails with(nolock) where ChildMapping = (select parentmapping_auth from NG_RLOS_GR_CompanyDetails with(nolock)  where applicantCategory='Business' and comp_winame = '"+formObject.getWFWorkitemName()+"') and AuthSignPassPortNo = (select PassportNo from ng_RLOS_Customer with(nolock) where wi_name = '"+formObject.getWFWorkitemName()+"')";
					List<List<String>> result = formObject.getDataFromDataSource(query);
					if(result!=null && result.size()>0 && result.get(0).size()>0){
						desig = result.get(0).get(0);
					}
				}catch(Exception e){
					desig="";
					CreditCard.mLogger.info("CC Integ java file Exception occured in getDesignationAuthSign method : "+CC_Common.printException(e));
				}
				return desig;
			}

			public String getEmploymentPeriod(){
				FormReference formObject = FormContext.getCurrentInstance().getFormReference();
				String LosCurr = formObject.getNGValue("cmplx_EmploymentDetails_LOS");
				String LosPrev = formObject.getNGValue("cmplx_EmploymentDetails_LOSPrevious");
				String yymm="";
				try{
					String l1 = LosCurr;
					String l2 = LosPrev;
					int y1;
					int y2;
					int m1;
					int m2;
					if(!"".equals(l1)){
						if(l1.contains(".")){
							y1 = Integer.parseInt(l1.split("\\.")[0]);
							m1 = Integer.parseInt(l1.split("\\.")[1]);
						}
						else{
							y1 = Integer.parseInt(l1);
							m1 = 0;
						}
					}
					else{
						y1=0;
						m1=0;
					}
					if(!"".equals(l2)){
						if(l2.contains(".")){
							y2 = Integer.parseInt(l2.split("\\.")[0]);
							m2 = Integer.parseInt(l2.split("\\.")[1]);
						}
						else{
							y2 = Integer.parseInt(l2);
							m2 = 0;
						}
					}
					else{
						y2=0;
						m2=0;
					}
					if(m1+m2>12){
						yymm = (y1+y2+1)+"."+(String.valueOf((12 - (m1+m2))).length()==1?"0"+String.valueOf((12 - (m1+m2))):String.valueOf(12 - (m1+m2)));
					}
					else{
						yymm = (y1+y2)+"."+(String.valueOf(m1+m2).length()==1?"0"+String.valueOf(m1+m2):String.valueOf(m1+m2));
					}

				}catch(Exception ex){
					yymm="";
					CreditCard.mLogger.info("Exception in getEmploymentPeriod : ");
					/*new CC_Common();*/
					//Commented for sonar
					CC_Common.printException(ex);
				}
				return yymm;
			}
			/*          Function Header:

			 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 10/12/2017              
		Author                              : Saurabh Gupta              
		Description                         : get cheque details for Card Notification       

			 ***********************************************************************************  */
			public String getCheque_details(String Call_name){
				CreditCard.mLogger.info("RLOSCommon java file inside getCheque_details : ");
				String  cheque_xml_str ="";

				try{
					FormReference formObject = FormContext.getCurrentInstance().getFormReference();
					int cheque_row_count = formObject.getLVWRowCount("cmplx_CardDetails_cmpmx_gr_cardDetails");
					CreditCard.mLogger.info("PL Common java file inside getCheque_details cheque_row_count+ : "+cheque_row_count);

					SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy");
					Date maxdate=sdf.parse(formObject.getNGValue("cmplx_CardDetails_cmpmx_gr_cardDetails", 0, 3));
					CreditCard.mLogger.info("PL Common inside getCheque_details maxDate in seconds : "+maxdate);
					int maxindex=0;
					for(int i=1;i<cheque_row_count;i++)
					{	
						if(!"".equalsIgnoreCase(formObject.getNGValue("cmplx_CardDetails_cmpmx_gr_cardDetails", i, 3))){
							String date = formObject.getNGValue("cmplx_CardDetails_cmpmx_gr_cardDetails", i, 3); //0
							Date curr_date=sdf.parse(date);
							CreditCard.mLogger.info("PL Common inside getCheque_details Grid date,curr_date is: "+date+" ,"+curr_date);

							if(curr_date.after(maxdate)){
								CreditCard.mLogger.info("PL Common inside getCheque_details Current Date is after max date");
								maxdate=curr_date;
								maxindex=i;
							}
						}
					}	

					String bankName = formObject.getNGValue("cmplx_CardDetails_cmpmx_gr_cardDetails", maxindex, 0); //0
					String cheqNo = formObject.getNGValue("cmplx_CardDetails_cmpmx_gr_cardDetails", maxindex, 1); //0
					String Amount = formObject.getNGValue("cmplx_CardDetails_cmpmx_gr_cardDetails", maxindex, 2); //0
					String chqDate = formObject.getNGValue("cmplx_CardDetails_cmpmx_gr_cardDetails", maxindex, 3); //0
					CreditCard.mLogger.info("chqate"+ chqDate);

					if(Call_name.equalsIgnoreCase("CARD_NOTIFICATION")&& !"".equalsIgnoreCase(cheqNo)){
						cheque_xml_str = cheque_xml_str+ "<SecurityChequeDetails><BankName>"+bankName+"</BankName>";
						cheque_xml_str = cheque_xml_str+ "<ChequeNo>"+cheqNo+"</ChequeNo>";
						cheque_xml_str = cheque_xml_str+ "<Amount>"+Amount+"</Amount>";
						cheque_xml_str = cheque_xml_str+ "<Date>"+new Common_Utils(CreditCard.mLogger).Convert_dateFormat(chqDate, "MM/dd/yyyy","yyyy-MM-dd")+"</Date></SecurityChequeDetails>";
					}
					else if(Call_name.equalsIgnoreCase("CARD_SERVICES_REQUEST") && !"".equalsIgnoreCase(cheqNo)){
						cheque_xml_str = cheque_xml_str+ "<SecurityChequeDetails><BankName>"+bankName+"</BankName>";
						cheque_xml_str = cheque_xml_str+ "<ChequeNo>"+cheqNo+"</ChequeNo>";
						cheque_xml_str = cheque_xml_str+ "<ChequeAmount>"+Amount+"</ChequeAmount>";
						cheque_xml_str = cheque_xml_str+ "<ChequeDate>"+new Common_Utils(CreditCard.mLogger).Convert_dateFormat(chqDate, "dd/MM/yyyy","yyyy-MM-dd")+"</ChequeDate></SecurityChequeDetails>";

					}

					CreditCard.mLogger.info("PL Common cheque_xml_str after adding cheque Details : "+cheque_xml_str);


					/*else{
				CreditCard.mLogger.info("CC Common cheque_xml_str NO record found in Grid");
			}*/

				}
				catch(Exception e){
					CreditCard.mLogger.info("PL Common java file Exception occured in get lien details method : "+CC_Common.printException(e));
				}

				return cheque_xml_str;

			}

			/*          Function Header:

			 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 15/12/2017              
		Author                              : Saurabh Gupta              
		Description                         : get Financial Services details for Card Notification       

			 ***********************************************************************************  */
			public String getFinancialServices_details(String Call_name){
				CreditCard.mLogger.info("CC_Integration_Input java file inside getFinancialServices_details : ");
				String  Financial_xml_str ="";
				try{
					FormReference formObject = FormContext.getCurrentInstance().getFormReference();
					String listViewname = "cmplx_CC_Loan_cmplx_btc";
					int rowCount = formObject.getLVWRowCount(listViewname);
					if(rowCount>0){
						UIComponent pComp =formObject.getComponent("cmplx_CC_Loan_cmplx_btc");
						int columns=0;
						List<String> gridColumnNames = new ArrayList<String>();
						List<String> transTypes =  new ArrayList<String>();
						if( pComp != null && pComp instanceof ListView )
						{			
							ListView objListView = ( ListView )pComp;
							columns  = objListView.getChildCount();
							for(int i=0;i<columns;i++){
								gridColumnNames.add(((Column)(pComp.getChildren().get(i))).getName());
							}
							for(int j=0;j<rowCount;j++){
								transTypes.add(formObject.getNGValue(listViewname, j, gridColumnNames.indexOf("Transaction Type")));
							}
						}

						int chosenRow = 0;//currently 0 for testing but has to be -1.
						String subProduct = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,2);
						String appType = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,4);
						if(subProduct.equalsIgnoreCase("IM") && appType.contains("NEW")){
							chosenRow = transTypes.indexOf("LOC");
						}
						else if(subProduct.equalsIgnoreCase("BTC")){
							chosenRow = transTypes.indexOf("BT");
						}
						if(chosenRow!=-1){
							String serviceType = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Transaction Type"));
							String amount = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Amount"));
							String cc_no = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("CreditCard No"));
							String benefName = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Beneficiary name"));
							String payMode = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Transfer Mode"));
							//String paymentPurpose = "PSC";//commented by deepak as the same is of no use.	
							String dispChanel = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Dispatch Channel"));
							String servMarkCode = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Marketing code"));
							String servAppCode = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Approval code"));
							String servSourceCode = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Source Code"));
							String nameOnCard = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Name on card"));
							String trfDate = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Date"));
							String tenor = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Tenor"));
							String appstatus = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Application Status"));

							//String servAppCode = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("Approval code"));
							//String bankName = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("bank Name"));
							String marketingCode = formObject.getNGValue("cmplx_EmploymentDetails_marketcode");

							String bankCode = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("bank Name"));
							formObject.setNGValue("bankName", bankCode);
							String bankName = formObject.getNGItemText("bankName", formObject.getSelectedIndex("bankName"));
							formObject.setNGValue("bankName", "");
							String branchName = formObject.getNGValue("AlternateContactDetails_CustdomBranch");
							String iban = formObject.getNGValue(listViewname, chosenRow, gridColumnNames.indexOf("IBAN"));
							String cardEmboss = cc_no;
							String custFullName = formObject.getNGValue("cmplx_Customer_Shortname");
							Financial_xml_str+="<FinancialServices>";
							if(Call_name.equalsIgnoreCase("CARD_NOTIFICATION")){
								if(checkValue(serviceType)){
									Financial_xml_str+="<ServiceType>"+serviceType+"</ServiceType>";
								}
								if(checkValue(amount)){
									Financial_xml_str+="<Amount>"+amount+"</Amount>";
								}
								if(checkValue(cc_no)){
									Financial_xml_str+="<CardNumber>"+cc_no+"</CardNumber>";
								}
								if(checkValue(benefName)){
									Financial_xml_str+="<BenificiaryName>"+benefName+"</BenificiaryName>";
								}
								if(checkValue(payMode)){
									Financial_xml_str+="<PaymentMode>"+payMode+"</PaymentMode>";
								}
								Financial_xml_str+="<PaymentPurpose>PSC</PaymentPurpose>";
								if(checkValue(dispChanel)){
									Financial_xml_str+="<DispatchChannel>"+dispChanel+"</DispatchChannel>";
								}
								if(checkValue(servMarkCode)){
									Financial_xml_str+="<ServiceMarketingCode>"+servMarkCode+"</ServiceMarketingCode>";
								}
								if(checkValue(servAppCode)){
									Financial_xml_str+="<ServiceApprovalCode>"+servAppCode+"</ServiceApprovalCode>";
								}
								if(checkValue(servSourceCode)){
									Financial_xml_str+="<ServiceSourceCode>"+servSourceCode+"</ServiceSourceCode>";
								}
								Financial_xml_str+="<OtherBankInfo>";
								if(checkValue(bankCode)){
									Financial_xml_str+="<BankCode>"+bankCode+"</BankCode>";
								}
								if(checkValue(iban)){
									Financial_xml_str+="<IBAN>"+iban+"</IBAN>";
								}
								if(checkValue(cardEmboss)){
									Financial_xml_str+="<CardEmbossNum>"+cardEmboss+"</CardEmbossNum>";
								}
								if(checkValue(custFullName)){
									Financial_xml_str+="<CustomerName>"+custFullName+"</CustomerName>";
								}
								Financial_xml_str+="</OtherBankInfo>";
							}
							else if(Call_name.equalsIgnoreCase("CARD_SERVICES_REQUEST")){
								if(checkValue(serviceType)){
									if(serviceType.equalsIgnoreCase("LOC")){
										serviceType = "LoanOnCard";
									}
									Financial_xml_str+="<RequestType>"+serviceType+"</RequestType>";
								}
								Financial_xml_str+="<ReferenceNumber>12345</ReferenceNumber>";
								if(checkValue(payMode)){
									Financial_xml_str+="<PaymentMode>"+payMode+"</PaymentMode>";
								}
								if(checkValue(bankCode)){
									Financial_xml_str+="<BankCode>"+bankCode+"</BankCode>";
								}
								if(checkValue(bankName)){
									Financial_xml_str+="<BankName>"+bankCode+"</BankName>";
								}
								if(checkValue(benefName)){
									Financial_xml_str+="<BenificiaryName>"+benefName+"</BenificiaryName>";
								}
								if(checkValue(cc_no)){
									Financial_xml_str+="<OtherBankCardNo>"+cc_no+"</OtherBankCardNo>";
								}
								if(checkValue(custFullName)){
									Financial_xml_str+="<CustomerName>"+custFullName+"</CustomerName>";
								}
								if(checkValue(nameOnCard)){
									Financial_xml_str+="<NameOnCard>"+nameOnCard+"</NameOnCard>";
								}
								if(checkValue(dispChanel)){
									Financial_xml_str+="<DispatchChannel>"+dispChanel+"</DispatchChannel>";
								}
								if(checkValue(branchName)){
									Financial_xml_str+="<BranchName>"+branchName+"</BranchName>";
								}
								if(checkValue(iban)){
									Financial_xml_str+="<IBAN>"+iban+"</IBAN>";
								}
								if(checkValue(amount)){
									Financial_xml_str+="<Amount>"+amount+"</Amount>";
								}
								if(checkValue(trfDate)){
									Financial_xml_str+="<TransferDate>"+new Common_Utils(CreditCard.mLogger).Convert_dateFormat(trfDate, "dd/MM/yyyy","yyyy-MM-dd")+"</TransferDate>";
								}
								if(checkValue(tenor)){
									Financial_xml_str+="<Tenor>"+tenor+"</Tenor>";
								}
								if(checkValue(marketingCode)){
									Financial_xml_str+="<MarketingCode>"+marketingCode+"</MarketingCode>";
								}
								if(checkValue(appstatus)){
									Financial_xml_str+="<ApplicationStatus>"+appstatus+"</ApplicationStatus>";
								}
								if(checkValue(servAppCode)){
									Financial_xml_str+="<ApprovalCode>"+servAppCode+"</ApprovalCode>";
								}
							}
							Financial_xml_str+="</FinancialServices>";
						}

					}
					return Financial_xml_str;
				}catch(Exception ex){
					CreditCard.mLogger.info("PL Common java file Exception occured in get lien details method : "+CC_Common.printException(ex));
					return "";
				}
			}

			public boolean checkValue(String ngValue){
				if(ngValue==null || "".equalsIgnoreCase(ngValue) || " ".equalsIgnoreCase(ngValue) || "--Select--".equalsIgnoreCase(ngValue)){
					return false;
				}
				return true;
			}


			/*          Function Header:

			 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 10/12/2017              
		Author                              : Saurabh Gupta              
		Description                         : Get Lien details for Card Notification       

			 ***********************************************************************************  */
			public String getLien_details(String Call_name){
				CreditCard.mLogger.info("RLOSCommon java file inside getLienDetails_details : ");
				String  lien_xml_str ="";

				try{
					FormReference formObject = FormContext.getCurrentInstance().getFormReference();
					int add_row_count = formObject.getLVWRowCount("cmplx_FinacleCore_liendet_grid");
					//CreditCard.mLogger.info("PL Common java file inside Lien details add_row_count+ : "+add_row_count);

					if (add_row_count>=0){

						String Lien_no = formObject.getNGValue("cmplx_FinacleCore_liendet_grid", 0, 1); //0
						String Lien_amount = formObject.getNGValue("cmplx_FinacleCore_liendet_grid", 0, 2); //0
						String Lien_maturity_date = formObject.getNGValue("cmplx_FinacleCore_liendet_grid", 0, 6); //0
						if(Call_name.equalsIgnoreCase("CARD_SERVICES_REQUEST")){
							lien_xml_str = lien_xml_str+ "<LienDetails><LienNumber>"+Lien_no+"</LienNumber>";
							lien_xml_str = lien_xml_str+ "<LienCurrency>AED</LienCurrency><LienAmount>"+Lien_amount+"</LienAmount>";
							if(!("".equals(Lien_maturity_date))){
								lien_xml_str = lien_xml_str+ "<LienMaturityDt>"+Lien_maturity_date+"</LienMaturityDt>";
							}
						}
						else if(Call_name.equalsIgnoreCase("CARD_NOTIFICATION")){

							lien_xml_str = lien_xml_str+ "<LienDetails><LienNumber>"+Lien_no+"</LienNumber>";
							lien_xml_str = lien_xml_str+ "<LienCurrency>AED</LienCurrency><Amount>"+Lien_amount+"</Amount>";
							if(!("".equals(Lien_maturity_date))){
								lien_xml_str = lien_xml_str+ "<MaturityDate>"+Lien_maturity_date+"</MaturityDate>";
							}
						}
						lien_xml_str = lien_xml_str+ "</LienDetails>";
					}
				}
				catch(Exception e){
					CreditCard.mLogger.info("PL Common java file Exception occured in get lien details method : "+CC_Common.printException(e));
				}

				return lien_xml_str;

			}
			/*          Function Header:

			 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : Dectech Custom    

			 ***********************************************************************************  */
			private Map<String, String> DECTECH_Custom(List<List<String>> DB_List,FormReference formObject,String callName) {

				Map<String, String> int_xml = new LinkedHashMap<String, String>();
				Map<String, String> recordFileMap = new HashMap<String, String>();

				try{
					for (List<String> mylist : DB_List) {

						for (int i = 0; i < 8; i++) {

							//CreditCard.mLogger.info(""+ "column length values"+ col_n);
							String[] col_name = col_n.split(",");
							recordFileMap.put(col_name[i], mylist.get(i));
						}
						String parent_tag =  recordFileMap.get("parent_tag_name");
						String tag_name =  recordFileMap.get("xmltag_name");
						String emp_type = "";
						int prod_count = formObject.getLVWRowCount("cmplx_Product_cmplx_ProductGrid");
						if (prod_count != 0) {
							emp_type = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",
									0, 6) == null ? "" : formObject.getNGValue(
											"cmplx_Product_cmplx_ProductGrid", 0, 6);
						}

						if ("Channel".equalsIgnoreCase(tag_name)) 
						{
							CreditCard.mLogger.info("RLOS COMMON"+ " iNSIDE channelcode+ ");

							String ReqProd = formObject.getNGValue(
									"cmplx_Product_cmplx_ProductGrid", 0, 1);

							String xml_str = int_xml.get(parent_tag);
							xml_str = "<" + tag_name + ">" + (NGFUserResourceMgr_CreditCard.getGlobalVar("CC_PersonalLoan").equalsIgnoreCase(ReqProd) ? "PL" : "CC") + "</" + tag_name + ">";

							//CreditCard.mLogger.info("RLOS COMMON"+ " after adding channelcode+ " + xml_str);
							int_xml.put(parent_tag, xml_str);

						}
						/*else if ("current_emp_catogery".equalsIgnoreCase(tag_name)) {
					//CreditCard.mLogger.info("RLOS COMMON"+ " iNSIDE channelcode+ ");

					String CurrentEmpCateg = formObject.getNGValue("cmplx_EmploymentDetails_CurrEmployer");


						if ("".equalsIgnoreCase(CurrentEmpCateg)||"--Select--".equalsIgnoreCase(CurrentEmpCateg)) {
							CurrentEmpCateg = "CN";

						}

					String xml_str = int_xml.get(parent_tag);
					xml_str = xml_str + "<" + tag_name + ">" + CurrentEmpCateg + "</" + tag_name + ">";

					//CreditCard.mLogger.info("RLOS COMMON"+ " after adding channelcode+ " + xml_str);
					int_xml.put(parent_tag, xml_str);

				}*/ 
						else if ("emp_type".equalsIgnoreCase(tag_name)) {
							//CreditCard.mLogger.info("RLOS COMMON"+ " iNSIDE channelcode+ ");

							String empttype = formObject.getNGValue(
									"cmplx_Product_cmplx_ProductGrid", 0, 6);
							if (empttype != null) {
								if ("Salaried".equalsIgnoreCase(empttype)) {
									empttype = "S";
								} else if ("Salaried Pensioner".equalsIgnoreCase(empttype)) {
									empttype = "SP";
								} 
								else if ("Pensioner".equalsIgnoreCase(empttype)){
									empttype="P";
								}
								else {
									empttype = "SE";
								}
							}
							String xml_str = int_xml.get(parent_tag);
							xml_str = xml_str + "<" + tag_name + ">" + empttype + "</" + tag_name + ">";

							//CreditCard.mLogger.info("RLOS COMMON"+ " after adding channelcode+ " + xml_str);
							int_xml.put(parent_tag, xml_str);

						} else if ("world_check".equalsIgnoreCase(tag_name)) {
							//CreditCard.mLogger.info("RLOS COMMON"+ " iNSIDE world_check+ ");

							String world_check = formObject.getNGValue("IS_WORLD_CHECK");
							//CreditCard.mLogger.info("RLOS COMMON"+" iNSIDE world_check+ "+ formObject.getLVWRowCount("cmplx_WorldCheck_WorldCheck_Grid"));
							
							//below code changed by nikhi for PCSP-35
							if (formObject.getLVWRowCount("cmplx_WorldCheck_WorldCheck_Grid") > 0) {
								//CHANGES DONE BY NIKHIL FOR PCAS-2779
								String flag_match_found="Negative";
								for (int i=0;i<formObject.getLVWRowCount("cmplx_WorldCheck_WorldCheck_Grid");i++)
								{
									if(formObject.getNGValue("cmplx_WorldCheck_WorldCheck_Grid",i,17).equalsIgnoreCase("true"))
									{
										flag_match_found="Positive";
									}
								}
								world_check = flag_match_found;
							} else {
								world_check = "Negative";
								
							}

							String xml_str = int_xml.get(parent_tag);
							xml_str = xml_str + "<" + tag_name + ">" + world_check + "</" + tag_name + ">";

							//CreditCard.mLogger.info("RLOS COMMON"+ " after adding world_check+ " + xml_str);
							int_xml.put(parent_tag, xml_str);

						}
						else if("fd_amount".equalsIgnoreCase(tag_name)){
							//CreditCard.mLogger.info("RLOS iNSIDE fd_amount+ ");
							String fd_amount=formObject.getNGValue("cmplx_Product_cmplx_ProductGrid", 0, 16);	
							String xml_str =  int_xml.get(parent_tag);
							xml_str = xml_str+ "<"+tag_name+">"+fd_amount
									+"</"+ tag_name+">";

							//CreditCard.mLogger.info("after adding fd_amount+ "+xml_str);
							int_xml.put(parent_tag, xml_str);
						}
						else if ("prev_loan_dbr".equalsIgnoreCase(tag_name)) {
							//CreditCard.mLogger.info("RLOS COMMON"+ " iNSIDE prev_loan_dbr+ ");
							String PreviousLoanDBR = "";
							String PreviousLoanEmp = "";
							String PreviousLoanMultiple = "";
							String PreviousLoanTAI = "";

							String squeryloan = "select PreviousLoanDBR,PreviousLoanEmp,PreviousLoanMultiple,PreviousLoanTAI from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock) where Request_Type='CollectionsSummary' and Limit_Increase='true' and Child_Wi= '" + formObject.getWFWorkitemName() + "'";
							List<List<String>> prevLoan = formObject.getNGDataFromDataCache(squeryloan);
							//CreditCard.mLogger.info("RLOS COMMON"+ " iNSIDE prev_loan_dbr+ " + squeryloan);

							if (prevLoan != null && prevLoan.size() > 0) {
								PreviousLoanDBR = prevLoan.get(0).get(0);
								PreviousLoanEmp = prevLoan.get(0).get(1);
								PreviousLoanMultiple = prevLoan.get(0).get(2);
								PreviousLoanTAI = prevLoan.get(0).get(3);
							}

							String xml_str = int_xml.get(parent_tag);
							xml_str = xml_str + "<" + tag_name + ">" + PreviousLoanDBR + "</" + tag_name + "><prev_loan_tai>" + PreviousLoanTAI + "</prev_loan_tai><prev_loan_multiple>" + PreviousLoanMultiple + "</prev_loan_multiple><prev_loan_amount></prev_loan_amount><prev_loan_employer>" + PreviousLoanEmp + "</prev_loan_employer>";

							//CreditCard.mLogger.info("RLOS COMMON"+ " after adding world_check+ " + xml_str);
							int_xml.put(parent_tag, xml_str);

						}

						else if ("no_of_cheque_bounce_int_3mon_Ind".equalsIgnoreCase(tag_name)) {
							CreditCard.mLogger.info("RLOS COMMON"+
									" iNSIDE no_of_cheque_bounce_int_3mon_Ind+ ");
							String squerynoc = "SELECT count(Child_Wi) FROM ng_rlos_FinancialSummary_ReturnsDtls with (nolock) WHERE CAST(returnDate AS datetime) >= DATEADD(month,-3,GETDATE()) and returntype='ICCS' and Child_Wi='" + formObject.getWFWorkitemName() + "'";
							List<List<String>> NOC = formObject.getDataFromDataSource(squerynoc);
							int count = 0;
							String xml_str = int_xml.get(parent_tag);
							if (NOC != null && NOC.size() > 0) {
								count += Integer.parseInt(NOC.get(0).get(0));
							}
							if(!formObject.isVisible("FinacleCore_Frame6")){
								String sQueryManualICCSadded = "select count(DDS_wi_name) from NG_RLOS_GR_DDSreturn where chqtype='ICCS' and chqretdate>=DATEADD(month,-3,GETDATE()) and DDS_wi_name ='" + formObject.getWFWorkitemName() + "'";
								List<List<String>> NOCManual = formObject.getNGDataFromDataCache(sQueryManualICCSadded);
								if (NOCManual != null && NOCManual.size() > 0) {
									count += Integer.parseInt(NOCManual.get(0).get(0));
								}
							}
							else{
								if(formObject.getLVWRowCount("cmplx_FinacleCore_DDSgrid")>0){
									for(int i=0;i<formObject.getLVWRowCount("cmplx_FinacleCore_DDSgrid");i++){
										if("ICCS".equalsIgnoreCase(formObject.getNGValue("cmplx_FinacleCore_DDSgrid",i,2))){
											count++;
										}
									}
								}
							}

							xml_str = xml_str + "<" + tag_name + ">" + count + "</" + tag_name + ">";

							//CreditCard.mLogger.info("RLOS COMMON"+" after adding no_of_cheque_bounce_int_3mon_Ind+ " + xml_str);
							int_xml.put(parent_tag, xml_str);


						} else if ("no_of_DDS_return_int_3mon_Ind".equalsIgnoreCase(tag_name)) {
							CreditCard.mLogger.info("RLOS COMMON"+
									" iNSIDE no_of_cheque_bounce_int_3mon_Ind+ ");
							String squerynoc = "SELECT count(Child_Wi) FROM ng_rlos_FinancialSummary_ReturnsDtls with (nolock) WHERE CAST(returnDate AS datetime) >= DATEADD(month,-3,GETDATE()) and returntype='DDS' and Child_Wi='" + formObject.getWFWorkitemName() + "'";
							List<List<String>> NOC = formObject
									.getNGDataFromDataCache(squerynoc);
							int count = 0;
							String xml_str = int_xml.get(parent_tag);
							if (NOC != null && NOC.size() > 0) {
								count += Integer.parseInt(NOC.get(0).get(0));
							}
							if(!formObject.isVisible("FinacleCore_Frame6")){
								String sQueryManualICCSadded = "select count(DDS_wi_name) from NG_RLOS_GR_DDSreturn where chqtype='DDS' and chqretdate>=DATEADD(month,-3,GETDATE()) and DDS_wi_name ='" + formObject.getWFWorkitemName() + "'";
								List<List<String>> NOCManual = formObject.getNGDataFromDataCache(sQueryManualICCSadded);
								if (NOCManual != null && NOCManual.size() > 0) {
									count += Integer.parseInt(NOCManual.get(0).get(0));
								}
							}
							else{
								if(formObject.getLVWRowCount("cmplx_FinacleCore_DDSgrid")>0){
									for(int i=0;i<formObject.getLVWRowCount("cmplx_FinacleCore_DDSgrid");i++){
										if("DDS".equalsIgnoreCase(formObject.getNGValue("cmplx_FinacleCore_DDSgrid",i,2))){
											count++;
										}
									}
								}
							}

							xml_str = xml_str + "<" + tag_name + ">" + count + "</" + tag_name + ">";

							//CreditCard.mLogger.info("RLOS COMMON"+" after adding no_of_DDS_return_int_3mon_Ind+ " + xml_str);
							int_xml.put(parent_tag, xml_str);

						} else if ("blacklist_cust_type".equalsIgnoreCase(tag_name)) {
							//CreditCard.mLogger.info("RLOS COMMON"+ " iNSIDE channelcode+ ");
							String ParentWI_Name = formObject.getNGValue("Parent_WIName");
							String squeryBlacklist = "select BlacklistFlag,BlacklistDate,BlacklistReasonCode,NegatedFlag,NegatedDate,NegatedReasonCode from ng_rlos_cif_detail with (nolock) where cif_wi_name='" + ParentWI_Name + "' and cif_searchType = 'Internal'";
							List<List<String>> Blacklist = formObject
									.getNGDataFromDataCache(squeryBlacklist);
							String internal_blacklist = "";
							String internal_blacklist_date = "";
							String internal_blacklist_code = "";
							String internal_negative_flag = "";
							String internal_negative_date = "";
							String internal_negative_code = "";

							if (Blacklist != null && Blacklist.size() > 0) {
								internal_blacklist = Blacklist.get(0).get(0);
								internal_blacklist_date = Blacklist.get(0).get(1);
								internal_blacklist_code = Blacklist.get(0).get(2);
								internal_negative_flag = Blacklist.get(0).get(3);
								internal_negative_date = Blacklist.get(0).get(4);
								internal_negative_code = Blacklist.get(0).get(5);
							}
							String xml_str = int_xml.get(parent_tag);
							xml_str = xml_str + "<" + tag_name + ">I</" + tag_name + "><internal_blacklist>" + internal_blacklist + "</internal_blacklist><internal_blacklist_date>" + internal_blacklist_date + "</internal_blacklist_date><internal_blacklist_code>" + internal_blacklist_code + "</internal_blacklist_code><negative_cust_type>I</negative_cust_type><internal_negative_flag>" + internal_negative_flag + "</internal_negative_flag><internal_negative_date>" + internal_negative_date + "</internal_negative_date><internal_negative_code>" + internal_negative_code + "</internal_negative_code>";

							CreditCard.mLogger.info("RLOS COMMON"+
									" after adding internal_blacklist+ " + xml_str);
							int_xml.put(parent_tag, xml_str);

						} else if ((("lob".equalsIgnoreCase(tag_name))
								|| ("target_segment_code".equalsIgnoreCase(tag_name))
								|| ("designation".equalsIgnoreCase(tag_name))
								|| ("emp_name".equalsIgnoreCase(tag_name))
								|| ("industry_sector".equalsIgnoreCase(tag_name))
								|| ("industry_macro".equalsIgnoreCase(tag_name))
								|| ("industry_micro".equalsIgnoreCase(tag_name))
								|| ("bvr".equalsIgnoreCase(tag_name))
								|| ("eff_date_estba".equalsIgnoreCase(tag_name))
								|| ("poa".equalsIgnoreCase(tag_name))
								|| ("tlc_issue_date".equalsIgnoreCase(tag_name))
								|| ("cc_employer_status".equalsIgnoreCase(tag_name))
								|| ("pl_employer_status".equalsIgnoreCase(tag_name)) || ("marketing_code".equalsIgnoreCase(tag_name))||("eff_lob".equalsIgnoreCase(tag_name))||("current_emp_catogery".equalsIgnoreCase(tag_name))||("head_offc_emirate".equalsIgnoreCase(tag_name)))&& "Self Employed".equalsIgnoreCase(emp_type)) {
							//CreditCard.mLogger.info("RLOSCommon java file"+	"inside getProduct_details : ");
							String xml_str = int_xml.get(parent_tag);
							int Comp_row_count = formObject
									.getLVWRowCount("cmplx_CompanyDetails_cmplx_CompanyGrid");
							String lob = "";
							String target_segment_code = "";
							String designation = "";
							String emp_name = "";
							String industry_sector = "";
							String eff_date_estba = "";
							String industry_marco = "";
							String industry_micro = "";
							String bvr = "";
							String cc_employer_status = "";
							String pl_employer_status = "";
							String poa = "";
							String marketing_code = "";
							String effLOB="";
							String current_emp_catogery="";
							String head_offc_emirate="";


							for (int i = 0; i < Comp_row_count; i++) {
								if (formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 2).equalsIgnoreCase("Secondary")){

									lob = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 15); // 0
									target_segment_code = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 22); // 0
									//designation = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 11); // 0
									emp_name = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 0); // 0
									// industry_sector =
									// formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",
									// i, 8); //0
									eff_date_estba = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 14); // 0
									industry_sector = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 8); // 0
									industry_marco = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 9); // 0
									industry_micro = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 10); // 0
									bvr = (formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 18).equalsIgnoreCase("true")?"Y":"N"); // 0
									cc_employer_status = (formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 34).equals("")?"CN":formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 34)); // 0
									pl_employer_status = (formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 35).equals("")?"CN":formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 35)); // 0
									poa = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 20); // 0
									marketing_code = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 28); // 0
									current_emp_catogery = (formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 33).equals("")?"CN":formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 33));
									effLOB = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 37); //0
									CreditCard.mLogger.info("CC integration"+	"effLOB: "+effLOB);
									head_offc_emirate = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 17); //0

								}
							}
							try
							{
								List<List<String>> auth_desig=formObject.getDataFromDataSource("select Designation from ng_rlos_gr_AuthSignDetails with(nolock) where sign_winame='"+formObject.getWFWorkitemName()+"'");
								//  no_of_partners;
								//	RLOS.mLogger.info("Partner count is "+partner_count);
								if(auth_desig!=null && !auth_desig.isEmpty()){
									designation=auth_desig.get(0).get(0);
								}
								else
								{
									designation="";
								}
							}
							catch(Exception ex)
							{
								CreditCard.mLogger.info(ex.getMessage());
							}

							if ("lob".equalsIgnoreCase(tag_name)) {
								xml_str = xml_str + "<" + tag_name + ">" + lob + "</" + tag_name + ">";
							} else if ("target_segment_code".equalsIgnoreCase(tag_name)) {
								xml_str = xml_str + "<" + tag_name + ">" + target_segment_code + "</" + tag_name + ">";
							} else if ("designation".equalsIgnoreCase(tag_name)) {
								xml_str = xml_str + "<" + tag_name + ">" + designation + "</" + tag_name + ">";
							} else if ("emp_name".equalsIgnoreCase(tag_name)) {
								xml_str = xml_str + "<" + tag_name + ">" + emp_name + "</" + tag_name + ">";
							} else if ("industry_sector".equalsIgnoreCase(tag_name)) {
								xml_str = xml_str + "<" + tag_name + ">" + industry_sector + "</" + tag_name + ">";
							} else if ("industry_macro".equalsIgnoreCase(tag_name)) {
								xml_str = xml_str + "<" + tag_name + ">" + industry_marco + "</" + tag_name + ">";
							} else if ("industry_micro".equalsIgnoreCase(tag_name)) {
								xml_str = xml_str + "<" + tag_name + ">" + industry_micro + "</" + tag_name + ">";
							} else if ("bvr".equalsIgnoreCase(tag_name)) {
								xml_str = xml_str + "<" + tag_name + ">" + bvr + "</" + tag_name + ">";
							} else if ("eff_date_estba".equalsIgnoreCase(tag_name)) {
								xml_str = xml_str + "<" + tag_name + ">" + eff_date_estba + "</" + tag_name + ">";
							} else if ("poa".equalsIgnoreCase(tag_name)) {
								xml_str = xml_str + "<" + tag_name + ">" + poa + "</" + tag_name + ">";
							} else if ("cc_employer_status".equalsIgnoreCase(tag_name)) {
								xml_str = xml_str + "<" + tag_name + ">" + cc_employer_status + "</" + tag_name + ">";
							} else if ("tlc_issue_date".equalsIgnoreCase(tag_name)) {
								xml_str = xml_str + "<" + tag_name + ">" + eff_date_estba + "</" + tag_name + ">";
							} else if ("pl_employer_status".equalsIgnoreCase(tag_name)) {
								xml_str = xml_str + "<" + tag_name + ">" + pl_employer_status + "</" + tag_name + ">";
							} else if ("marketing_code".equalsIgnoreCase(tag_name)) {
								xml_str = xml_str + "<" + tag_name + ">" + marketing_code + "</" + tag_name + ">";
							} else if("eff_lob".equalsIgnoreCase(tag_name)){
								xml_str = xml_str+ "<"+tag_name+">"+effLOB+"</"+ tag_name+">";
							} else if("current_emp_catogery".equalsIgnoreCase(tag_name)){
								xml_str = xml_str+ "<"+tag_name+">"+current_emp_catogery+"</"+ tag_name+">";
							}
							else if("head_offc_emirate".equalsIgnoreCase(tag_name)){
								xml_str = xml_str+ "<"+tag_name+">"+head_offc_emirate+"</"+ tag_name+">";
							}
							//CreditCard.mLogger.info("RLOS COMMON"+	" after adding cmplx_CompanyDetails_cmplx_CompanyGrid+ "+ xml_str);
							int_xml.put(parent_tag, xml_str);
						} else if (("auth_sig_sole_emp".equalsIgnoreCase(tag_name) || "shareholding_perc".equalsIgnoreCase(tag_name))&& "Self Employed".equalsIgnoreCase(emp_type)) {
							CreditCard.mLogger.info("RLOS COMMON"+ " iNSIDE channelcode+ ");
							String auth_sig_sole_emp = "";
							String shareholding_perc = "";
							int Authsign_row_count = formObject
									.getLVWRowCount("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails");
							if (Authsign_row_count != 0) {
								auth_sig_sole_emp = ("Yes".equalsIgnoreCase(formObject.getNGValue("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails",0, 10)) ? "Y" : "N"); // 0
								shareholding_perc = formObject.getNGValue("cmplx_AuthSignDetails_cmplx_GR_AuthorizedSignDetails",0, 9); // 0

							}
							String xml_str = int_xml.get(parent_tag);
							if ("auth_sig_sole_emp".equalsIgnoreCase(tag_name)) {
								xml_str = xml_str + "<" + tag_name + ">" + auth_sig_sole_emp + "</" + tag_name + ">";
							} else if ("shareholding_perc".equalsIgnoreCase(tag_name)) {
								xml_str = xml_str + "<" + tag_name + ">" + shareholding_perc + "</" + tag_name + ">";
							}

							//CreditCard.mLogger.info("RLOS COMMON"+	" after adding shareholding_perc+ " + xml_str);
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
						else if("no_of_partners".equalsIgnoreCase(tag_name)){
							CreditCard.mLogger.info(" iNSIDE world_check+ ");
							int no_of_partners=formObject.getLVWRowCount("cmplx_PartnerDetails_cmplx_partnerGrid");

							try{
								String parseXml=  formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", 1, 27);;
								InputStream is = new ByteArrayInputStream(parseXml.getBytes());
								DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
								DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
								Document doc = dBuilder.parse(is);
								doc.getDocumentElement().normalize();

								NodeList nList = doc.getElementsByTagName("ListItem");
								CreditCard.mLogger.info("nList length: "+ nList.getLength());
								no_of_partners=nList.getLength();
							}
							catch(Exception e){
								CreditCard.mLogger.info("Exception occured: "+e.getMessage());
							}

							String xml_str = int_xml.get(parent_tag);
							xml_str = xml_str+ "<"+tag_name+">"+no_of_partners+"</"+ tag_name+">";


							//CreditCard.mLogger.info(" after adding no_of_partners+ "+xml_str);
							int_xml.put(parent_tag, xml_str);


						}
						/*	else if ("avg_credit_turnover_6".equalsIgnoreCase(tag_name)	|| "avg_credit_turnover_3".equalsIgnoreCase(tag_name)	|| "avg_bal_3".equalsIgnoreCase(tag_name)|| "avg_bal_6".equalsIgnoreCase(tag_name)) {
						String avg_credit_turnover_6="";
						String avg_credit_turnover_3="";
						String avg_bal_3="";
						String avg_bal_6="";
						String squerycurremp="select total_avg_6,total_avg_last1year,avg_credit_3month,avg_credit_6month from NG_RLOS_FinacleCore with(nolock) where wi_name='"+formObject.getWFWorkitemName()+"'";
						List<List<String>> squerycurrempXML=formObject.getNGDataFromDataCache(squerycurremp);
						CreditCard.mLogger.info(" query is "+squerycurrempXML);

						if(!squerycurrempXML.isEmpty() && squerycurrempXML.get(0).get(0)!=null)
						{								
							//CreditCard.mLogger.info(" iNSIDE squerycurrempXML+ "+squerycurrempXML.get(0).get(0));
							avg_credit_turnover_6=squerycurrempXML.get(0).get(2);
							avg_credit_turnover_3=squerycurrempXML.get(0).get(3);
							avg_bal_3=squerycurrempXML.get(0).get(0);
							avg_bal_6=squerycurrempXML.get(0).get(1);
						}
						String xml_str = int_xml.get(parent_tag);
						if ("avg_credit_turnover_6".equalsIgnoreCase(tag_name)) {
							xml_str = xml_str + "<" + tag_name + ">"+avg_credit_turnover_6+"</" + tag_name + ">";
						} else if ("avg_credit_turnover_3".equalsIgnoreCase(tag_name)) {
							xml_str = xml_str + "<" + tag_name + ">" + avg_credit_turnover_3 + "</" + tag_name + ">";
						} else if ("avg_bal_3".equalsIgnoreCase(tag_name)) {
							xml_str = xml_str + "<" + tag_name + ">" + avg_bal_3 + "</" + tag_name + ">";
						}  else if ("avg_bal_6".equalsIgnoreCase(tag_name)) {
							xml_str = xml_str + "<" + tag_name + ">" + avg_bal_6 + "</" + tag_name + ">";
						}


						int_xml.put(parent_tag, xml_str);
					} */
						//start by sagarika
						else if(("avg_credit_turnover_6".equalsIgnoreCase(tag_name)||"avg_credit_turnover_3".equalsIgnoreCase(tag_name)||"avg_bal_3".equalsIgnoreCase(tag_name)||"avg_bal_6".equalsIgnoreCase(tag_name) )){
							//	RLOS.mLogger.info("@sagarika rlos");
							if(int_xml.containsKey(parent_tag))
							{
								if("PA".equalsIgnoreCase(formObject.getNGValue("Sub_Product"))){
									//	RLOS.mLogger.info("sagarika");

									double avg_credit_turnover6th=0;
									double avg_credit_turnover3rd=0;
									double avg_bal_3=0;
									double avg_bal_6=0;
									String avg_credit_turnover_freq=formObject.getNGValue("cmplx_IncomeDetails_AvgCredTurnoverFreq");
									String avg_bal_freq=formObject.getNGValue("cmplx_IncomeDetails_AvgBalFreq");
									String avg_credit_turnover=formObject.getNGValue("cmplx_IncomeDetails_AvgCredTurnover");
									String avg_bal=formObject.getNGValue("cmplx_IncomeDetails_AvgBal");

									//	RLOS.mLogger.info("avg_credit_turnover6th value"+avg_bal);
									String avg_bal_6_str="";
									String avg_bal_3_str="";
									String avg_credit_turnover3rd_str="";
									String avg_credit_turnover6th_str="";
									if (avg_credit_turnover!=(null)&&(!"".equalsIgnoreCase(avg_credit_turnover))){
										avg_credit_turnover6th=Double.parseDouble(avg_credit_turnover);
										//	RLOS.mLogger.info("avg_credit_turnover6th value"+avg_credit_turnover6th);

										if ("Annually".equalsIgnoreCase(avg_credit_turnover_freq)){
											avg_credit_turnover6th=avg_credit_turnover6th/2;
											avg_credit_turnover3rd=avg_credit_turnover6th/2;
										}
										else if ("Half Yearly".equalsIgnoreCase(avg_credit_turnover_freq)){

											avg_credit_turnover3rd=avg_credit_turnover6th/2;
										}
										else if ("Quarterly".equalsIgnoreCase(avg_credit_turnover_freq)){
											avg_credit_turnover6th=2*avg_credit_turnover6th;
											CreditCard.mLogger.info("avg_credit_turnover6th value"+avg_credit_turnover6th);
											avg_credit_turnover3rd=avg_credit_turnover6th/2;
											CreditCard.mLogger.info("avg_credit_turnover3rd value"+avg_credit_turnover3rd);
										}
										else if ("monthly".equalsIgnoreCase(avg_credit_turnover_freq)){
											avg_credit_turnover6th=6*avg_credit_turnover6th;
											avg_credit_turnover3rd=avg_credit_turnover6th/2;
										}
										//	RLOS.mLogger.info("avg_credit_turnover3rd value before"+avg_credit_turnover3rd);
										avg_credit_turnover3rd_str=String.format("%.0f", avg_credit_turnover3rd);
										avg_credit_turnover6th_str=String.format("%.0f", avg_credit_turnover6th);
										//	RLOS.mLogger.info("avg_credit_turnover3rd value after"+avg_credit_turnover3rd);
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
						}//end by sagarika


						else if("current_emp_catogery".equalsIgnoreCase(tag_name))
						{
							CreditCard.mLogger.info(" iNSIDE current_emp_catogery+ ");

							String current_emp_catogery=formObject.getNGValue("cmplx_EmploymentDetails_CurrEmployer");
							
							if ("".equalsIgnoreCase(current_emp_catogery)||"--Select--".equalsIgnoreCase(current_emp_catogery)) {
								current_emp_catogery = "CN";

							}

							String xml_str = int_xml.get(parent_tag);
							xml_str = xml_str+ "<"+tag_name+">"+current_emp_catogery+"</"+ tag_name+">";

							int_xml.put(parent_tag, xml_str);

						}
						//below code added by nikhi for PCSP-60
						else if("cc_employer_status".equalsIgnoreCase(tag_name))
						{
							CreditCard.mLogger.info(" iNSIDE current_emp_catogery+ ");

							String cc_employer_status=formObject.getNGValue("cmplx_EmploymentDetails_EmpStatusCC");
							//change by saurabh for PPG changes.23/4/19
							if ("".equalsIgnoreCase(cc_employer_status)||"--Select--".equalsIgnoreCase(cc_employer_status) || "UL".equalsIgnoreCase(cc_employer_status)) {
								cc_employer_status = "CN";

							}

							String xml_str = int_xml.get(parent_tag);
							xml_str = xml_str+ "<"+tag_name+">"+cc_employer_status+"</"+ tag_name+">";

							//CreditCard.mLogger.info(" after adding current_emp_catogery+ "+xml_str);
							int_xml.put(parent_tag, xml_str);

						}
						else if("pl_employer_status".equalsIgnoreCase(tag_name))
						{
							CreditCard.mLogger.info(" iNSIDE current_emp_catogery+ ");

							String pl_employer_status=formObject.getNGValue("cmplx_EmploymentDetails_EmpStatusPL");
						
							if ("".equalsIgnoreCase(pl_employer_status)||"--Select--".equalsIgnoreCase(pl_employer_status)) {
								pl_employer_status = "CN";

							}

							String xml_str = int_xml.get(parent_tag);
							xml_str = xml_str+ "<"+tag_name+">"+pl_employer_status+"</"+ tag_name+">";

							//CreditCard.mLogger.info(" after adding current_emp_catogery+ "+xml_str);
							int_xml.put(parent_tag, xml_str);

						}

						else if ("external_blacklist_flag".equalsIgnoreCase(tag_name)	|| "external_blacklist_date".equalsIgnoreCase(tag_name)	|| "external_blacklist_code".equalsIgnoreCase(tag_name)) {
							CreditCard.mLogger.info("RLOS COMMON"+ " iNSIDE channelcode+ ");
							String ParentWI_Name = formObject.getNGValue("Parent_WIName");
							String squeryBlacklist = "select BlacklistFlag,BlacklistDate,BlacklistReasonCode from ng_rlos_cif_detail with (nolock) where (cif_wi_name='" + ParentWI_Name + "' or cif_wi_name='" + formObject.getWFWorkitemName() + "') and cif_searchType = 'External'";
							CreditCard.mLogger.info("RLOS COMMON"+ " iNSIDE channelcode+ " + squeryBlacklist);
							List<List<String>> Blacklist = formObject
									.getNGDataFromDataCache(squeryBlacklist);
							String External_blacklist_date = "";
							String External_blacklist_code = "";

							if (Blacklist != null && Blacklist.size() > 0) {
								External_blacklist_date = Blacklist.get(0).get(1);
								External_blacklist_code = Blacklist.get(0).get(2);
							}
							String xml_str = int_xml.get(parent_tag);
							if ("external_blacklist_flag".equalsIgnoreCase(tag_name)) {
								xml_str = xml_str + "<" + tag_name + ">I</" + tag_name + ">";
							} else if ("external_blacklist_date".equalsIgnoreCase(tag_name)) {
								xml_str = xml_str + "<" + tag_name + ">" + External_blacklist_date + "</" + tag_name + ">";
							} else if ("external_blacklist_code".equalsIgnoreCase(tag_name)) {
								xml_str = xml_str + "<" + tag_name + ">" + External_blacklist_code + "</" + tag_name + ">";
							}

							//CreditCard.mLogger.info("RLOS COMMON"+" after adding internal_blacklist+ " + xml_str);
							int_xml.put(parent_tag, xml_str);

						} else if ("ApplicationDetails".equalsIgnoreCase(tag_name)) {
							CreditCard.mLogger.info("inside 1st if"+ "inside DECTECH req1");

							//CreditCard.mLogger.info("inside 1st if"+ "inside customer update req2");
							String xml_str = int_xml.get(parent_tag);
							//CreditCard.mLogger.info("RLOS COMMON"+ " before adding product+ " + xml_str);
							xml_str = xml_str + CC_Comn.getProduct_details();
							//CreditCard.mLogger.info("RLOS COMMON"+ " after adding product+ " + xml_str);
							int_xml.put(parent_tag, xml_str);

						} else if ("cust_name".equalsIgnoreCase(tag_name)) {
							if (int_xml.containsKey(parent_tag)) {
								String first_name = formObject.getNGValue("cmplx_Customer_FIrstNAme");
								String middle_name = formObject.getNGValue("cmplx_Customer_MiddleName");
								String last_name = formObject.getNGValue("cmplx_Customer_LAstNAme");

								String full_name = first_name + " " + middle_name + "" + last_name;

								String xml_str = int_xml.get(parent_tag);
								xml_str = xml_str + "<" + tag_name + ">" + full_name + "</" + tag_name + ">";

								//CreditCard.mLogger.info("RLOS COMMON"+" after adding confirmedinjob+ " + xml_str);
								int_xml.put(parent_tag, xml_str);

							}
						}

						else if ("ref_phone_no".equalsIgnoreCase(tag_name)) {
							if (int_xml.containsKey(parent_tag)) {
								//CreditCard.mLogger.info("RLOS COMMON"+ " INSIDE ref_phone_no+ ");
								int count = formObject.getLVWRowCount("cmplx_RefDetails_cmplx_Gr_ReferenceDetails");
								String ref_phone_no = "";
								String ref_relationship = "";
								//CreditCard.mLogger.info("RLOS COMMON"+ " INSIDE ref_phone_no+ " + count);
								if (count != 0) {
									ref_phone_no = formObject.getNGValue("cmplx_RefDetails_cmplx_Gr_ReferenceDetails", 0, 4);
									ref_relationship = formObject.getNGValue("cmplx_RefDetails_cmplx_Gr_ReferenceDetails", 0, 2);
									//CreditCard.mLogger.info("RLOS COMMON"+" after adding ref_phone_no+ " + ref_phone_no);
									//CreditCard.mLogger.info("RLOS COMMON"+" after adding ref_relationship+ "+ ref_relationship);
								}

								String xml_str = int_xml.get(parent_tag);
								xml_str = xml_str + "<" + tag_name + ">" + ref_phone_no + "</" + tag_name + "><ref_relationship>" + ref_relationship + "</ref_relationship>";

								//CreditCard.mLogger.info("RLOS COMMON"+" after adding confirmedinjob+ " + xml_str);
								int_xml.put(parent_tag, xml_str);

							}
						}

						else if ("confirmed_in_job".equalsIgnoreCase(tag_name)) {
							if (int_xml.containsKey(parent_tag)) {
								String confirmedinjob = formObject.getNGValue("cmplx_EmploymentDetails_JobConfirmed");

								if (confirmedinjob != null) {
									if ("true".equalsIgnoreCase(confirmedinjob)) 
									{
										confirmedinjob = "Y";
									} 
									else 
									{
										confirmedinjob = "N";
									}

									String xml_str = int_xml.get(parent_tag);
									xml_str = xml_str + "<" + tag_name + ">" + confirmedinjob+ "</" + tag_name + ">";

									//CreditCard.mLogger.info("RLOS COMMON"+" after adding confirmedinjob+ " + xml_str);
									int_xml.put(parent_tag, xml_str);
								}
							}
						} 
						else if ("included_pl_aloc".equalsIgnoreCase(tag_name)) {
							if (int_xml.containsKey(parent_tag)) {
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

									//CreditCard.mLogger.info("RLOS COMMON"+" after adding included_pl_aloc+ " + xml_str);
									int_xml.put(parent_tag, xml_str);
								}
							}
						} else if ("included_cc_aloc".equalsIgnoreCase(tag_name)) {
							if (int_xml.containsKey(parent_tag)) {
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

									//CreditCard.mLogger.info("RLOS COMMON"+" after adding cmplx_EmploymentDetails_IncInCC+ "+ xml_str);
									int_xml.put(parent_tag, xml_str);
								}
							}
						} else if ("vip_flag".equalsIgnoreCase(tag_name)) {
							if (int_xml.containsKey(parent_tag)) {
								String vip_flag = formObject.getNGValue("cmplx_Customer_VIPFlag");

								if ("true".equalsIgnoreCase(vip_flag)) {
									vip_flag = "Y";
								} else {
									vip_flag = "N";
								}

								String xml_str = int_xml.get(parent_tag);
								xml_str = xml_str + "<" + tag_name + ">" + vip_flag + "</" + tag_name + ">";

								//CreditCard.mLogger.info("RLOS COMMON"+" after adding cmplx_Customer_VIPFlag+ " + xml_str);
								int_xml.put(parent_tag, xml_str);
							}
						} else if ("standing_instruction".equalsIgnoreCase(tag_name)) {
							CreditCard.mLogger.info("RLOS COMMON"+
									" iNSIDE standing_instruction+ ");
							String squerynoc = "SELECT count(Child_wi) FROM ng_rlos_FinancialSummary_SiDtls with (nolock) WHERE Child_wi='" + formObject.getWFWorkitemName() + "'";
							List<List<String>> NOC = formObject
									.getNGDataFromDataCache(squerynoc);
							CreditCard.mLogger.info("RLOS COMMON"+
									" after adding cmplx_Customer_VIPFlag+ " + squerynoc);
							String standing_instruction = "";
							standing_instruction = NOC.get(0).get(0);
							if (NOC != null && NOC.size() > 0) {
								String xml_str = int_xml.get(parent_tag);
								if ("0".equalsIgnoreCase(standing_instruction)) {
									standing_instruction = "N";
								} else {
									standing_instruction = "Y";
								}

								xml_str = xml_str + "<" + tag_name + ">" + standing_instruction + "</" + tag_name + ">";

								//CreditCard.mLogger.info("RLOS COMMON"+" after adding standing_instruction+ " + xml_str);
								int_xml.put(parent_tag, xml_str);
							}
						}

						//copied from RLOS to here by akshay for proc 10324
						/*else if(("avg_credit_turnover_6".equalsIgnoreCase(tag_name)||"avg_credit_turnover_3".equalsIgnoreCase(tag_name)||"avg_bal_3".equalsIgnoreCase(tag_name)||"avg_bal_6".equalsIgnoreCase(tag_name) )){
					if(int_xml.containsKey(parent_tag))
					{
						if (NGFUserResourceMgr_CreditCard.getGlobalVar("CC_SelfEmployed").equalsIgnoreCase(emp_type)){
						double avg_credit_turnover6th=0;
						double avg_credit_turnover3rd=0;
						double avg_bal_3=0;
						double avg_bal_6=0;
						String avg_credit_turnover_freq=formObject.getNGValue("cmplx_IncomeDetails_AvgCredTurnoverFreq");
						String avg_bal_freq=formObject.getNGValue("cmplx_IncomeDetails_AvgBalFreq");
						String avg_credit_turnover=formObject.getNGValue("cmplx_IncomeDetails_AvgCredTurnover");
						String avg_bal=formObject.getNGValue("cmplx_IncomeDetails_AvgBal");

						String avg_bal_6_str="";
						String avg_bal_3_str="";
						String avg_credit_turnover3rd_str="";
						String avg_credit_turnover6th_str="";
						if (avg_credit_turnover!=(null)&&(!"".equalsIgnoreCase(avg_credit_turnover))){
							avg_credit_turnover6th=Double.parseDouble(avg_credit_turnover);
							CreditCard.mLogger.info("avg_credit_turnover6th value"+avg_credit_turnover6th);

							if ("Annually".equalsIgnoreCase(avg_credit_turnover_freq)){
								avg_credit_turnover6th=avg_credit_turnover6th/2;
								avg_credit_turnover3rd=avg_credit_turnover6th/2;
							}
							else if ("Half Yearly".equalsIgnoreCase(avg_credit_turnover_freq)){
								avg_credit_turnover3rd=avg_credit_turnover6th/2;
							}
							else if ("Quarterly".equalsIgnoreCase(avg_credit_turnover_freq)){
								avg_credit_turnover6th=2*avg_credit_turnover6th;
								CreditCard.mLogger.info("avg_credit_turnover6th value"+avg_credit_turnover6th);
								avg_credit_turnover3rd=avg_credit_turnover6th/2;
								CreditCard.mLogger.info("avg_credit_turnover3rd value"+avg_credit_turnover3rd);
							}
							else if ("monthly".equalsIgnoreCase(avg_credit_turnover_freq)){
								avg_credit_turnover6th=6*avg_credit_turnover6th;
								avg_credit_turnover3rd=avg_credit_turnover6th/2;
							}
							CreditCard.mLogger.info("avg_credit_turnover3rd value before"+avg_credit_turnover3rd);
							avg_credit_turnover3rd_str=String.format("%.0f", avg_credit_turnover3rd);
							avg_credit_turnover6th_str=String.format("%.0f", avg_credit_turnover6th);
							CreditCard.mLogger.info("avg_credit_turnover3rd value after"+avg_credit_turnover3rd);
						}
						if (avg_bal!=(null)&&(!"".equalsIgnoreCase(avg_bal))){
							//avg_bal_6=Double.parseDouble(avg_bal);
							CreditCard.mLogger.info("avg_credit_turnover6th value"+avg_credit_turnover6th);

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
						CreditCard.mLogger.info(" after adding cmplx_Customer_VIPFlag+ "+xml_str);
						int_xml.put(parent_tag, xml_str);
					}	
					}		
				}*/

						else if("borrowing_customer".equalsIgnoreCase(tag_name)){
							CreditCard.mLogger.info("RLOS iNSIDE borrowing_customer+ ");
							String squeryBorrow="select distinct(borrowingCustomer) from ng_RLOS_CUSTEXPOSE_CardDetails with (nolock) WHERE  Child_wi ='"+formObject.getWFWorkitemName()+"' union select distinct(borrowingCustomer) from ng_RLOS_CUSTEXPOSE_LoanDetails with (nolock)  WHERE Child_wi ='"+formObject.getWFWorkitemName()+"'";
							//CreditCard.mLogger.info("RLOS COMMONiNSIDE borrowing_customer query+ "+squeryBorrow);
							List<List<String>> borrowing_customer=formObject.getNGDataFromDataCache(squeryBorrow);
							if (borrowing_customer!=null && !borrowing_customer.isEmpty()){
								String xml_str =  int_xml.get(parent_tag);
								xml_str = xml_str+ "<"+tag_name+">"+borrowing_customer.get(0).get(0)
										+"</"+ tag_name+">";

								//CreditCard.mLogger.info("after adding borrowing_customer+ "+xml_str);
								int_xml.put(parent_tag, xml_str);
							}
						}
						else if("funding_pattern".equalsIgnoreCase(tag_name)){
							CreditCard.mLogger.info("RLOS iNSIDE FundingPattern+ ");
							String squeryfund="select distinct(FundingPattern) from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) WHERE  Child_wi ='"+formObject.getWFWorkitemName()+"'";
							//CreditCard.mLogger.info("RLOS COMMONiNSIDE FundingPattern query+ "+squeryfund);
							List<List<String>> funding_pattern=formObject.getNGDataFromDataCache(squeryfund);
							if (funding_pattern!=null && !funding_pattern.isEmpty()){
								String xml_str =  int_xml.get(parent_tag);
								xml_str = xml_str+ "<"+tag_name+">"+funding_pattern.get(0).get(0)
										+"</"+ tag_name+">";

								//CreditCard.mLogger.info("after adding funding_pattern+ "+xml_str);
								int_xml.put(parent_tag, xml_str);
							}
						}
						//added by nikhil  for Rachit CR
						else if("ins_value".equalsIgnoreCase(tag_name) || "prem_amnt".equalsIgnoreCase(tag_name) || "no_of_prem_paid".equalsIgnoreCase(tag_name) || "prem_type".equalsIgnoreCase(tag_name) ||"regular_payment".equalsIgnoreCase(tag_name) || "within_minwaiting_period".equalsIgnoreCase(tag_name) ){
							CreditCard.mLogger.info("ins_value");
							CreditCard.mLogger.info("ins_value::"+formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode"));
							String ins_value="",prem_amnt="",no_of_prem_paid="",prem_type="",regular_payment="",within_minwaiting_period="";
							
							if("LIFSUR".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")))
							{
								CreditCard.mLogger.info("ins_value life sur");
								ins_value=formObject.getNGValue("cmplx_EmploymentDetails_InsuranceValue");
								prem_amnt=formObject.getNGValue("cmplx_EmploymentDetails_PremAmt");
								no_of_prem_paid=formObject.getNGValue("cmplx_EmploymentDetails_PremPaid");
								prem_type=formObject.getNGValue("cmplx_EmploymentDetails_PremType") ;
								regular_payment=formObject.getNGValue("cmplx_EmploymentDetails_RegPayment").equalsIgnoreCase("true")?"Yes":"No";
								CreditCard.mLogger.info("lifersure");
								CreditCard.mLogger.info("lifersure"+regular_payment);
								within_minwaiting_period=formObject.getNGValue("cmplx_EmploymentDetails_MinimumWait").equalsIgnoreCase("true")?"Yes":"No";;
							}
							else if("MOTSUR".equalsIgnoreCase(formObject.getNGValue("cmplx_EmploymentDetails_targetSegCode")))
							{
								CreditCard.mLogger.info("ins_value motor sur");
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
						else if("nmf_flag".equalsIgnoreCase(tag_name)){
							String nmfQuery="";
							int Comp_row_count = formObject.getLVWRowCount("cmplx_CompanyDetails_cmplx_CompanyGrid");
							if(Comp_row_count!=0){	
								for (int i = 0; i<Comp_row_count;i++){
									if (formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 2).equalsIgnoreCase("Secondary")){
										String emp_name = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i, 0); //0
										nmfQuery="select count(comp_name) from NG_Master_NMF_not_listed_Comp  with (nolock) where comp_name='"+emp_name+"'";
									}
								}
							}
							else{
								nmfQuery="select count(comp_name) from NG_Master_NMF_not_listed_Comp with (nolock) where comp_name='"+formObject.getNGValue("cmplx_EmploymentDetails_EmpName")+"'";
							}
							CreditCard.mLogger.info("RLOS iNSIDE nmf_flag+ ");
							//CreditCard.mLogger.info("RLOS COMMONiNSIDE borrowing_customer query+ "+nmfQuery);
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

								//CreditCard.mLogger.info("after adding nmfQueryData+ "+xml_str);
								int_xml.put(parent_tag, xml_str);
							}
						}

						else if("aggregate_exposed".equalsIgnoreCase(tag_name)){
							CreditCard.mLogger.info(" Inside Aggregate ");
							if(int_xml.containsKey(parent_tag))
							{
								formObject.saveFragment("EligibilityAndProductInformation");
								//query changed by akshay on 4/6/18...card installment  and od sanction limit added
								//String aeQuery = "select (select isNull((Sum(convert(float,replace([TotalOutstandingAmt],'NA','0')))),0) as TotalOutstandingAmt from ng_RLOS_CUSTEXPOSE_loanDetails with (nolock) where Consider_For_Obligations='true' and LoanStat in ('A','ACTIVE') and child_wi ='"+formObject.getWFWorkitemName()+"' AND TotalOutstandingAmt not like '%-%') + (select isNull((Sum(convert(float,replace([TotalOutstandingAmt],'NA','0')))),0) as TotalOutstandingAmt from ng_RLOS_CUSTEXPOSE_loanDetails with (nolock) where Consider_For_Obligations='true' and LoanStat ='Pipeline' and child_wi ='"+formObject.getWFWorkitemName()+"' AND TotalOutstandingAmt not like '%-%') +(select isNull((Sum(convert(float,replace([OutstandingBalance],'NA','0')))),0)  from ng_RLOS_CUSTEXPOSE_CardinstallmentDetails installment join ng_RLOS_CUSTEXPOSE_CardDetails carddetails with (nolock) on carddetails.CardEmbossNum=installment.CardcrnNumber where  carddetails.Consider_For_Obligations='true' and carddetails.CardStatus in ('A','ACTIVE') and carddetails.child_wi ='"+formObject.getWFWorkitemName()+"' and SchemeCardProd like '%LOC%' AND OutstandingBalance not like '%-%')  +(select isNull((Sum(convert(float,replace([CreditLimit],'NA','0')))),0) as OutstandingAmt from (select max( liab.CreditLimit) as CreditLimit from ng_RLOS_CUSTEXPOSE_CardDetails liab, ng_master_cardProduct prod with (nolock) where  Consider_For_Obligations='true' and CardStatus in ('A','ACTIVE') and child_wi ='"+formObject.getWFWorkitemName()+"' and SchemeCardProd not like '%LOC%' AND CreditLimit not like '%-%' and liab.SchemeCardProd=prod.CODE group by case when prod.ReqProduct='Conventional' then prod.ReqProduct else liab.SchemeCardProd end) as TempTable)+( select isNull((Sum(convert(float,replace([final_limit],'NA','0')))),0) from ng_rlos_EligAndProdInfo with (nolock) where wi_name ='"+formObject.getWFWorkitemName()+"')+( select isNull((Sum(convert(float,replace([sanctionLimit],'NA','0')))),0) from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where child_wi ='"+formObject.getWFWorkitemName()+"' and ODDesc is not null and AcctStat in ('A','ACTIVE')) as aggregateExposure";
								//Deepak changes done on 16/07/2019 as per PCAS-1295
								String aeQuery = "select (select isNull((Sum(convert(float,replace([TotalOutstandingAmt],'NA','0')))),0) as TotalOutstandingAmt from ng_RLOS_CUSTEXPOSE_loanDetails with (nolock) where Consider_For_Obligations='true' and LoanStat in ('A','ACTIVE') and child_wi ='"+formObject.getWFWorkitemName()+"' AND TotalOutstandingAmt not like '%-%') + (select isNull((Sum(convert(float,replace([TotalOutstandingAmt],'NA','0')))),0) as TotalOutstandingAmt from ng_RLOS_CUSTEXPOSE_loanDetails with (nolock) where Consider_For_Obligations='true' and LoanStat ='Pipeline' and child_wi ='"+formObject.getWFWorkitemName()+"' AND TotalOutstandingAmt not like '%-%') +(select isNull((Sum(convert(float,replace([OutstandingBalance],'NA','0')))),0)  from ng_RLOS_CUSTEXPOSE_CardinstallmentDetails installment join ng_RLOS_CUSTEXPOSE_CardDetails carddetails with (nolock) on carddetails.CardEmbossNum=installment.CardcrnNumber where  carddetails.Consider_For_Obligations='true' and carddetails.CardStatus in ('A','ACTIVE') and carddetails.child_wi ='"+formObject.getWFWorkitemName()+"' and SchemeCardProd like '%LOC%' AND OutstandingBalance not like '%-%')  +(select isNull((Sum(convert(float,replace([CreditLimit],'NA','0')))),0) as OutstandingAmt from (select DISTINCT max( liab.CreditLimit) as CreditLimit from ng_RLOS_CUSTEXPOSE_CardDetails liab, ng_master_cardProduct prod with (nolock) where  Consider_For_Obligations='true' and CardStatus in ('A','ACTIVE') and child_wi ='"+formObject.getWFWorkitemName()+"' and SchemeCardProd not like '%LOC%' AND CreditLimit not like '%-%' and liab.SchemeCardProd=prod.CODE group by case when prod.ReqProduct='Conventional' then prod.ReqProduct else liab.SchemeCardProd end) as TempTable)+( select isNull((Sum(convert(float,replace([final_limit],'NA','0')))),0) from ng_rlos_EligAndProdInfo with (nolock) where wi_name ='"+formObject.getWFWorkitemName()+"')+( select isNull((Sum(convert(float,replace([sanctionLimit],'NA','0')))),0) from ng_RLOS_CUSTEXPOSE_AcctDetails with (nolock) where child_wi ='"+formObject.getWFWorkitemName()+"' and ODDesc is not null and AcctStat in ('A','ACTIVE')) as aggregateExposure";
								//CreditCard.mLogger.info(" Inside Aggregate "+aeQuery);
								List<List<String>> aggregate_exposed = formObject.getDataFromDataSource(aeQuery);

								String aggr_expo=aggregate_exposed.get(0).get(0);
								double aggreg=Double.parseDouble(aggr_expo);
								aggr_expo=String.format("%.2f", aggreg);

								formObject.setNGValue("cmplx_Liability_New_AggrExposure", aggr_expo,false);//changed by akshay on 25/9/17 as per point 2 of problem sheet
								String xml_str = int_xml.get(parent_tag);
								xml_str = xml_str + "<"+tag_name+">"+aggr_expo+"</"+ tag_name+">";

								//CreditCard.mLogger.info(" after adding aggregate_exposed+ "+xml_str);
								int_xml.put(parent_tag, xml_str);
							}                                        
						}

						else if ("accomodation_provided".equalsIgnoreCase(tag_name)) {
							if (int_xml.containsKey(parent_tag)) {
								String accomodation_provided = formObject.getNGValue("cmplx_IncomeDetails_Accomodation")!=null && "yes".equalsIgnoreCase(formObject.getNGValue("cmplx_IncomeDetails_Accomodation"))?"Y":"N";

								String xml_str = int_xml.get(parent_tag);
								xml_str = xml_str + "<" + tag_name + ">"+ accomodation_provided + "</" + tag_name + ">";

								//CreditCard.mLogger.info("RLOS COMMON"+" after adding confirmedinjob+ " + xml_str);
								int_xml.put(parent_tag, xml_str);

							}
						} else if ("AccountDetails".equalsIgnoreCase(tag_name)) {

							if (int_xml.containsKey(parent_tag)) {
								String xml_str = int_xml.get(parent_tag);
								//CreditCard.mLogger.info("RLOS COMMON"+" before adding internal liability+ " + xml_str);
								xml_str = xml_str + CC_Comn.getInternalLiabDetails();
								//CreditCard.mLogger.info("RLOS COMMON"+" after internal liability+ " + xml_str);
								int_xml.put(parent_tag, xml_str);
							}


						} else if ("InternalBureau".equalsIgnoreCase(tag_name)) {

							String xml_str = int_xml.get(parent_tag);
							//CreditCard.mLogger.info("RLOS COMMON"+" before adding InternalBureauData+ " + xml_str);
							String temp = CC_Comn.InternalBureauData();
							if (!"".equalsIgnoreCase(temp)) {
								if (xml_str == null) {
									//CreditCard.mLogger.info("RLOS COMMON"+ " before adding bhrabc"+ xml_str);
									xml_str = "";
								}
								xml_str = xml_str + temp;
								//CreditCard.mLogger.info("RLOS COMMON"+" after InternalBureauData+ " + xml_str);
								int_xml.get(parent_tag);
								int_xml.put(parent_tag, xml_str);
							}

						} else if ("InternalBouncedCheques".equalsIgnoreCase(tag_name)) {

							String xml_str = int_xml.get(parent_tag);
							//CreditCard.mLogger.info("RLOS COMMON"+" before adding InternalBouncedCheques+ " + xml_str);
							String temp = InternalBouncedCheques();
							if (!"".equalsIgnoreCase(temp)) {
								if (xml_str == null) {
									//CreditCard.mLogger.info("RLOS COMMON"+ " before adding bhrabc"+ xml_str);
									xml_str = "";
								}
								xml_str = xml_str + temp;
								//CreditCard.mLogger.info("RLOS COMMON"+" after InternalBouncedCheques+ " + xml_str);
								int_xml.put(parent_tag, xml_str);
							}

						} else if ("InternalBureauIndividualProducts".equalsIgnoreCase(tag_name)) {

							String xml_str = int_xml.get(parent_tag);
							//CreditCard.mLogger.info("RLOS COMMON"+" before adding InternalBureauIndividualProducts+ "+ xml_str);
							String temp = InternalBureauIndividualProducts();
							if (!"".equalsIgnoreCase(temp)) {
								if (xml_str == null) {
									//CreditCard.mLogger.info("RLOS COMMON"+ " before adding bhrabc"+ xml_str);
									xml_str = "";
								}
								xml_str = xml_str + temp;
								//CreditCard.mLogger.info("RLOS COMMON"+" after InternalBureauIndividualProducts+ " + xml_str);
								int_xml.put(parent_tag, xml_str);
							}

						} else if ("InternalBureauPipelineProducts".equalsIgnoreCase(tag_name)) {

							String xml_str = int_xml.get(parent_tag);
							//CreditCard.mLogger.info("RLOS COMMON"+" before adding InternalBureauPipelineProducts+ "+ xml_str);
							String temp = InternalBureauPipelineProducts();
							if (!"".equalsIgnoreCase(temp)) {
								if (xml_str == null) {
									//CreditCard.mLogger.info("RLOS COMMON"+ " before adding bhrabc"+ xml_str);
									xml_str = "";
								}
								xml_str = xml_str + temp;
								//CreditCard.mLogger.info("RLOS COMMON"+" after InternalBureauPipelineProducts+ " + xml_str);
								int_xml.put(parent_tag, xml_str);
							}

						} else if ("ExternalBureau".equalsIgnoreCase(tag_name)) {

							String xml_str = int_xml.get(parent_tag);
							CreditCard.mLogger.info("RLOS COMMON"+
									" before adding ExternalBureau+ " + xml_str);
							String temp = ExternalBureauData();
							if (!"".equalsIgnoreCase(temp)) {
								if (xml_str == null) {
									//CreditCard.mLogger.info("RLOS COMMON"+ " before adding bhrabc"+ xml_str);
									xml_str = "";
								}
								xml_str = xml_str + temp;
								//CreditCard.mLogger.info("RLOS COMMON"+ " after ExternalBureau+ " + xml_str);
								int_xml.put(parent_tag, xml_str);
							}

						} else if ("ExternalBouncedCheques".equalsIgnoreCase(tag_name)) {

							String xml_str = int_xml.get(parent_tag);
							//CreditCard.mLogger.info("RLOS COMMON"+" before adding ExternalBouncedCheques+ " + xml_str);
							String temp = ExternalBouncedCheques();
							if (!"".equalsIgnoreCase(temp)) {
								if (xml_str == null) {
									//CreditCard.mLogger.info("RLOS COMMON"+ " before adding bhrabc"+ xml_str);
									xml_str = "";
								}
								xml_str = xml_str + temp;
								//CreditCard.mLogger.info("RLOS COMMON"+" after ExternalBouncedCheques+ " + xml_str);
								int_xml.put(parent_tag, xml_str);
							}
						} else if ("ExternalBureauIndividualProducts".equalsIgnoreCase(tag_name)) {

							String xml_str = int_xml.get(parent_tag);
							String temp = ExternalBureauIndividualProducts();
							//CreditCard.mLogger.info("RLOS COMMON"+" value of temp to be adding temp+ " + temp);
							String Manual_add_Liab = ExternalBureauManualAddIndividualProducts();

							if ((!"".equalsIgnoreCase(temp))|| (!"".equalsIgnoreCase(Manual_add_Liab))) {
								if (xml_str == null) {
									//CreditCard.mLogger.info("RLOS COMMON"+ " before adding bhrabc"+ xml_str);
									xml_str = "";
								}
								xml_str = xml_str + temp + Manual_add_Liab;
								//CreditCard.mLogger.info("RLOS COMMON"+" after ExternalBureauIndividualProducts+ " + xml_str);
								int_xml.put(parent_tag, xml_str);
							}
						} else if ("ExternalBureauPipelineProducts".equalsIgnoreCase(tag_name)) {

							String xml_str = int_xml.get(parent_tag);
							CreditCard.mLogger.info("RLOS COMMON"+" before adding ExternalBureauPipelineProducts+ "+ xml_str);
							String temp = ExternalBureauPipelineProducts();
							if (!"".equalsIgnoreCase(temp)) {
								if (xml_str == null) {
									//CreditCard.mLogger.info("RLOS COMMON"+ " before adding bhrabc"+ xml_str);
									xml_str = "";
								}
								xml_str = xml_str + temp;
								//CreditCard.mLogger.info("RLOS COMMON"+" after ExternalBureauPipelineProducts+ " + xml_str);
								int_xml.put(parent_tag, xml_str);
							}
						}
						else{
							int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
						}
					}
				}
				catch(Exception e){
					CreditCard.mLogger.info("CC Integration "+ " Exception occured in dectech + "+e.getMessage());
					CreditCard.mLogger.info(CC_Common.printException(e));
				}
				return int_xml;
			}
			/*          Function Header:

			 **********************************************************************************

		         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


		Date Modified                       : 6/08/2017              
		Author                              : Disha              
		Description                         : Cheque book eligibility      

			 ***********************************************************************************  */
			private Map<String, String> CHEQUE_BOOK_ELIGIBILITY_Custom(List<List<String>> DB_List,FormReference formObject,String callName) {

				Map<String, String> int_xml = new LinkedHashMap<String, String>();
				Map<String, String> recordFileMap = new HashMap<String, String>();

				try{
					for (List<String> mylist : DB_List) {

						for (int i = 0; i < 8; i++) {

							//CreditCard.mLogger.info(""+ "column length values"+ col_n);
							String[] col_name = col_n.split(",");
							recordFileMap.put(col_name[i], mylist.get(i));
						}
						String parent_tag =  recordFileMap.get("parent_tag_name");
						String tag_name =  recordFileMap.get("xmltag_name");
						if ("RecipientAddress".equalsIgnoreCase(tag_name)) {
							int add_len = formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
							String add_res_val = "";
							String xml_str = int_xml.get(parent_tag);
							if (add_len > 0) {
								for (int i = 0; i < add_len; i++) {
									//CreditCard.mLogger.info("selecting Emirates of residence: "+formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i, 0));
									if ("Home".equalsIgnoreCase(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0))) {formObject.setNGValue("cmplx_Customer_EmirateOfResidence",formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i, 6));add_res_val = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 1)+ " "		+formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i, 2)+ formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i, 3)+ formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i, 4)+ formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i, 5)+ formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i, 6);
									}
								}
								xml_str = xml_str + "<" + tag_name + ">" + add_res_val + "</"+ tag_name + ">";

								//CreditCard.mLogger.info("RLOS COMMON"+ " after adding res_flag+ "+ xml_str);
								int_xml.put(parent_tag, xml_str);
							}
						}
						else{
							int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
						}
					}
				}
				catch(Exception e){
					CreditCard.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + "+e.getMessage());
					CreditCard.mLogger.info("CC_INtegration Exception in CARD services custom function:  "+CC_Common.printException(e));
				}
				return int_xml;
			}

			/*          Function Header:

			 **********************************************************************************

	         NEWGEN SOFTWARE TECHNOLOGIES LIMITED


	Date Modified                       : 6/08/2017              
	Author                              : Disha              
	Description                         : Function to Generate Input XML for DB INPUT

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
					//CreditCard.mLogger.info("inside 1st if"+"inside 1st if");
					if (int_xml.containsKey(parent_tag)) {
						CreditCard.mLogger.info("inside 1st if"+"inside 2nd if");
						String xml_str = int_xml.get(parent_tag);
						//CreditCard.mLogger.info("inside 1st if"+"inside 2nd if xml string"+ xml_str);
						if ("Y".equalsIgnoreCase(is_repetitive)&& int_xml.containsKey(tag_name)) {
							CreditCard.mLogger.info("inside 1st if"+"inside 3rd if xml string");
							xml_str = int_xml.get(tag_name) + "</"+ tag_name + ">" + "<"+ tag_name + ">";
							//CreditCard.mLogger.info("CC_Common"+"value after adding "+ Call_name + ": "+ xml_str);
							//CreditCard.mLogger.info("inside 1st if"+"inside 3rd if xml string xml string"+ xml_str);
							int_xml.remove(tag_name);
							int_xml.put(tag_name, xml_str);
							//CreditCard.mLogger.info("inside 1st if"+"inside 3rd if xml string xml string int_xml");
						} else {
							//CreditCard.mLogger.info("inside else of parent tag"+"value after adding "+ Call_name+ ": "+ xml_str+ " form_control name: "+ form_control);
							//CreditCard.mLogger.info(""+"valuie of form control: "+ formObject.getNGValue(form_control));
							if ("".equalsIgnoreCase(form_control.trim())&& "".equalsIgnoreCase(default_val.trim())) {
								//CreditCard.mLogger.info("inside 1st if"+"inside");
								xml_str = xml_str + "<" + tag_name+ ">" + "</" + tag_name+ ">";
								//CreditCard.mLogger.info("added by xml"+ "xml_str"+ xml_str);
							} else if (!(formObject.getNGValue(form_control) == null||  "null".equalsIgnoreCase(formObject.getNGValue(form_control)))) {//"".equalsIgnoreCase(formObject.getNGValue(form_control).trim()) || change by saurabh on 17th May 18
								//CreditCard.mLogger.info("inside else of parent tag 1"+"form_control_val"+ form_control_val);
								if (fin_call_name.toUpperCase().contains(callName.toUpperCase()) && !capitalExceptionsTags.toUpperCase().contains(tag_name.toUpperCase())) {
									form_control_val = formObject.getNGValue(form_control).toUpperCase();
								} else
									form_control_val = formObject.getNGValue(form_control);

								if (!"text".equalsIgnoreCase(data_format12)) {
									String[] format_arr = data_format12.split(":");
									String format_name = format_arr[0];
									String format_type = format_arr[1];
									//CreditCard.mLogger.info(""+"format_name"+ format_name);
									//CreditCard.mLogger.info(""+"format_type"+ format_type);

									if ("date".equalsIgnoreCase(format_name)) {
										DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
										DateFormat df_new = new SimpleDateFormat(format_type);

										try {
											if(!form_control_val.equals("")){
												startDate = df.parse(form_control_val);
												form_control_val = df_new.format(startDate);
												//CreditCard.mLogger.info("RLOSCommon#Create Input"+" date conversion: final Output: "+ form_control_val+ " requested format: "+ format_type);
											}
										} catch (ParseException e) {
											CreditCard.mLogger.info("RLOSCommon#Create Input"+" Error while format conversion: "+ e.getMessage());
											//e.printStackTrace();  commented because of SONAR
										} catch (Exception e) {
											CreditCard.mLogger.info("RLOSCommon#Create Input"+" Error while format conversion: "+ e.getMessage());
											//e.printStackTrace(); commented because of SONAR
										}
									} else if ("number".equalsIgnoreCase(format_name)) {
										if (form_control_val.contains(",")) {
											form_control_val = form_control_val.replace(",","");
										}

									}
								}
								//CreditCard.mLogger.info("inside else of parent tag"+"form_control_val"+ form_control_val);
								xml_str = xml_str + "<" + tag_name+ ">" + form_control_val+ "</" + tag_name + ">";
								//CreditCard.mLogger.info("inside else of parent tag xml_str"+"xml_str" + xml_str);
							}

							else if (default_val == null || "".equalsIgnoreCase(default_val.trim())) {
								CreditCard.mLogger.info("#RLOS Common GenerateXML IF part"+"no value found for tag name: "+ tag_name);
							} else {
								//CreditCard.mLogger.info("#RLOS Common GenerateXML inside set default value"+"");
								form_control_val = default_val;

								//CreditCard.mLogger.info("#RLOS Common GenerateXML inside set default value"+"form_control_val"+ form_control_val);
								xml_str = xml_str + "<" + tag_name+ ">" + form_control_val+ "</" + tag_name + ">";
								//CreditCard.mLogger.info("#RLOS Common GenerateXML inside else of parent tag form_control_val xml_str1"+"xml_str" + xml_str);

							}

							if ("DocumentRefNumber".equalsIgnoreCase(tag_name) && "Document".equalsIgnoreCase(parent_tag) && "".equalsIgnoreCase(form_control_val.trim())) {
								if (xml_str.contains("</Document>")) {
									xml_str = xml_str.substring(0,xml_str.lastIndexOf("</Document>"));
									int_xml.put(parent_tag,xml_str);
								} else
									int_xml.remove(parent_tag);
							} else if ("DocRefNum".equalsIgnoreCase(tag_name) && parent_tag.equalsIgnoreCase("DocDetails") && "".equalsIgnoreCase(form_control_val)) {
								if (xml_str.contains("</DocDetails>")) {
									xml_str = xml_str.substring(0,xml_str.lastIndexOf("</DocDetails>"));
									int_xml.put(parent_tag,xml_str);
								} else
									int_xml.remove(parent_tag);
							} else if ("PhnLocalCode".equalsIgnoreCase(tag_name) && "PhnDetails".equalsIgnoreCase(parent_tag) && "".equalsIgnoreCase(form_control_val)) {
								if (xml_str.contains("</PhnDetails>")) {
									xml_str = xml_str.substring(0,xml_str.lastIndexOf("</PhnDetails>"));
									int_xml.put(parent_tag,xml_str);
								} else
									int_xml.remove(parent_tag);
							} else if ("Email".equalsIgnoreCase(tag_name) && "EmlDet".equalsIgnoreCase(parent_tag) && "".equalsIgnoreCase(form_control_val)) {
								if (xml_str.contains("</EmlDet>")) {
									xml_str = xml_str.substring(0,xml_str.lastIndexOf("</EmlDet>"));
									int_xml.put(parent_tag,xml_str);
								} else
									int_xml.remove(parent_tag);
							}//Deepak 03Feb2019 Changes done for PCSP-369
							/* else if ("DocNo".equalsIgnoreCase(tag_name) && "DocDet".equalsIgnoreCase(parent_tag) && "".equalsIgnoreCase(form_control_val)) {
								if (xml_str.contains("</DocDet>")) {
									xml_str = xml_str.substring(0,xml_str.lastIndexOf("</DocDet>"));
									int_xml.put(parent_tag,xml_str);
								} else
									int_xml.remove(parent_tag);
							}*/else if ("IncomeAmount".equalsIgnoreCase(tag_name) && "OtherIncomeDetails".equalsIgnoreCase(parent_tag) && "".equalsIgnoreCase(form_control_val)) {
								if (xml_str.contains("</OtherIncomeDetails>")) {
									xml_str = xml_str.substring(0,xml_str.lastIndexOf("</OtherIncomeDetails>"));
									int_xml.put(parent_tag,xml_str);
								} else
									int_xml.remove(parent_tag);
							}
							else {
								int_xml.put(parent_tag, xml_str);
							}
							//CreditCard.mLogger.info("else of generatexml"+"RLOSCommon" + "inside else"+ xml_str);
						}

					} else {
						String new_xml_str = "";
						CreditCard.mLogger.info("inside else of parent tag main 2"+"value after adding " + Call_name+ ": " + new_xml_str+ " form_control name: "+ form_control);
						//CreditCard.mLogger.info(""+"valuie of form control: "+ formObject.getNGValue(form_control));
						if (!(formObject.getNGValue(form_control) == null|| "".equalsIgnoreCase(formObject.getNGValue(form_control).trim()) || "null".equalsIgnoreCase(formObject.getNGValue(form_control)))) {
							CreditCard.mLogger.info("inside else of parent tag 1"+"form_control_val"+ form_control_val);
							if (fin_call_name.toUpperCase().contains(callName.toUpperCase())) {
								form_control_val = formObject.getNGValue(form_control).toUpperCase();
							} else
								form_control_val = formObject.getNGValue(form_control);
							if (!"text".equalsIgnoreCase(data_format12)) {
								String[] format_arr = data_format12.split(":");
								String format_name = format_arr[0];
								String format_type = format_arr[1];
								if ("date".equalsIgnoreCase(format_name)) {
									DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
									DateFormat df_new = new SimpleDateFormat(format_type);
									// java.util.Date startDate;
									try {
										startDate = df.parse(form_control_val);
										form_control_val = df_new.format(startDate);
										CreditCard.mLogger.info("RLOSCommon#Create Input"+	" date conversion: final Output: "+ form_control_val+" requested format: "+ format_type);

									} catch (ParseException e) {
										CreditCard.mLogger.info("RLOSCommon#Create Input"+	" Error while format conversion: "+ e.getMessage());
										//e.printStackTrace(); //Commented for Sonar
									} catch (Exception e) {
										CreditCard.mLogger.info("RLOSCommon#Create Input"+	" Error while format conversion: "+ e.getMessage());
										//e.printStackTrace();//Commented for Sonar
									}
								}
								else if ("number".equalsIgnoreCase(format_name)) {
									if (form_control_val.contains(",")) {
										form_control_val = form_control_val.replace(",", "");
									}
								}
							}
							//CreditCard.mLogger.info("inside else of parent tag"+"form_control_val"+ form_control_val);
							new_xml_str = new_xml_str + "<"+ tag_name + ">"+ form_control_val + "</"+ tag_name + ">";
							//CreditCard.mLogger.info("inside else of parent tag xml_str"+"new_xml_str"+ new_xml_str);
						}

						else if (default_val == null || "".equalsIgnoreCase(default_val.trim())) {
							if (int_xml.containsKey(parent_tag) || "Y".equalsIgnoreCase(is_repetitive)) {
								new_xml_str = new_xml_str + "<"+ tag_name + ">" + "</"+ tag_name + ">";
							}
							CreditCard.mLogger.info("#RLOS Common GenerateXML Inside Else Part"+"no value found for tag name: "+ tag_name);
						} else {
							CreditCard.mLogger.info("#RLOS Common GenerateXML inside set default value"+"");
							form_control_val = default_val;
							//CreditCard.mLogger.info("#RLOS Common GenerateXML inside set default value"+"form_control_val"+ form_control_val);
							new_xml_str = new_xml_str + "<"+ tag_name + ">"+ form_control_val + "</"+ tag_name + ">";
							//CreditCard.mLogger.info("#RLOS Common GenerateXML inside else of parent tag form_control_val xml_str1"+"xml_str" + new_xml_str);
						}
						int_xml.put(parent_tag, new_xml_str);
						//CreditCard.mLogger.info("else of generatexml"+"RLOSCommon" + "inside else"+ new_xml_str);
					}
				}
				return int_xml;
			}
			public String getReference_details(){
				CreditCard.mLogger.info("RLOSCommon java file"+ "inside getCustAddress_details : ");
				String  ref_xml_str ="";
				try{
					FormReference formObject = FormContext.getCurrentInstance().getFormReference();
					int ref_row_count = formObject.getLVWRowCount("cmplx_RefDetails_cmplx_Gr_ReferenceDetails");
					CreditCard.mLogger.info("RLOSCommon java file"+ "inside getReference_details row_count+ : "+ref_row_count);

					for (int i = 0; i<ref_row_count;i++){
						String ref_name = formObject.getNGValue("cmplx_RefDetails_cmplx_Gr_ReferenceDetails", i, 0); //0
						String ref_phone=formObject.getNGValue("cmplx_RefDetails_cmplx_Gr_ReferenceDetails", i, 1);//1
						String ref_relation=formObject.getNGValue("cmplx_RefDetails_cmplx_Gr_ReferenceDetails", i, 2);//2
						ref_xml_str = ref_xml_str + "<ReferenceDetails><PersonName>"+ref_name+"</PersonName>";
						ref_xml_str = ref_xml_str + "<Relationship>"+ref_relation+"</Relationship>";
						ref_xml_str = ref_xml_str + "<PhnDet><PhoneType>OFFCPH1</PhoneType><PhnCountryCode>00971</PhnCountryCode><CityCode></CityCode><PhnLocalCode>00971</PhnLocalCode>";
						ref_xml_str = ref_xml_str +"<PhoneNumber>"+ref_phone+"</PhoneNumber><ExtensionNumber></ExtensionNumber><PhnPrefFlag>N</PhnPrefFlag></PhnDet></ReferenceDetails>";
					}
					//CreditCard.mLogger.info("RLOSCommon"+ "reference tag Cration: "+ ref_xml_str);
					return ref_xml_str;
				}
				catch(Exception e){
					CreditCard.mLogger.info("PLCommon getReference_details method"+ "Exception Occure in getReference_details"+CC_Common.printException(e));
					return ref_xml_str;
				}	
			}
			//below function identified by saurabh or extension fields CR
			public String getExtensions_details(boolean primary){
                CreditCard.mLogger.info("RLOSCommon java file inside getExtensions_details : ");
                String  ext_xml_str ="";
                try{
                    FormReference formObject = FormContext.getCurrentInstance().getFormReference();
                    String card_prod = formObject.getNGValue("CC_Creation_Product");
                    List<List<String>> typeOfCP = formObject.getDataFromDataSource("select top 1 ReqProduct from ng_master_cardProduct where CODE = '"+card_prod+"' and Subproduct = '"+formObject.getNGValue("Sub_Product")+"' and EmployerCategory = '"+formObject.getNGValue("EmploymentType")+"'");
                    Map<String,String> desctoFormCtrl = new LinkedHashMap<String, String>();
                    desctoFormCtrl.put("PS:Card:F152","C 10002 :AlternateContactDetails_CardDisp");
                    desctoFormCtrl.put("PS:Card:F153","C 10010 :CC_Creation_CRN");
                    desctoFormCtrl.put("P:Card:F154","C 10044:cmplx_CC_Loan_cmplx_btc");
                    desctoFormCtrl.put("P:Card:F155","C 10048:cmplx_CC_Loan_VPSPAckage");
                    desctoFormCtrl.put("P:Card:F156","C 10049:cmplx_CC_Loan_VPSStatus");
                    desctoFormCtrl.put("P:Card:F157","C 10052:cmplx_CardDetails_CompanyEmbossing_name");
                    //desctoFormCtrl.put("P:Account:F158","A 21010 :");
                    //desctoFormCtrl.put("P:Account:F159","A 21011 :Account Charity Percentage Amount    not to be passed");
                    //desctoFormCtrl.put("P:Account:F160","A 21012 :Account Charity Combination    From masters");
                    //desctoFormCtrl.put("P:Account:F161","A 21013 :Account Charity Organization ID    From masters");
                    desctoFormCtrl.put("PS:Account:F160:Kalyan","A 21017:cmplx_CardDetails_cmplx_CardCRNDetails");
                    desctoFormCtrl.put("PS:Account:F160:mrbh","A  707 :File_Generated_Date");
                    desctoFormCtrl.put("PS:Account:F161","A  705 :File_Id");
                    desctoFormCtrl.put("PS:Account:F163","A 21026:");
                    //desctoFormCtrl.put("PS:Account:F164","A 707 :Account Primary Agreement Start Date");
                    
                    //desctoFormCtrl.put("PS:Account:F166","A 777 :Used for Murabaha Card");
                    
                    if(primary){
                        desctoFormCtrl.put("PS:Account:F167","A 21073:AlternateContactDetails_eStatementFlag");
                    }
                    //Deepak changes done for Air Arabia in extension 5 feb -PCSP 653
                    String card_intrest_profile = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),11);
					if(card_intrest_profile.contains("Arabia")){
                        desctoFormCtrl.put("PS:Account:F168","A 21075:AlternateContactDetails_AirArabiaIdentifier");
					}
                    if(primary){
                        desctoFormCtrl.put("PS:Account:F169","A 10053:AlternateContactDetails_MobileNo1");
                        desctoFormCtrl.put("PS:Account:F170","A 10054:AlternateContactDetails_Email1");
                        desctoFormCtrl.put("PS:Account:F171","A 10058:cmplx_CardDetails_SMS_Output");
                    }
                    
 
                    /*desctoFormCtrl.put("PS:Account:F168","A 21075 :Air Arabia identifier    Air-Arabia");
            desctoFormCtrl.put("PS:Account:F169","A 10053 :Mobile Number");
            desctoFormCtrl.put("PS:Account:F170","A 10054 :Primary Email ID");
            desctoFormCtrl.put("PS:Account:F171","A 10058 :Send SMS Option    ");
                     */
                    //Deepak 3 feb changes done for extension fields. PCSP-653
                    int extcount=152;
                    for(Map.Entry<String, String> entry : desctoFormCtrl.entrySet()){
                    	 CreditCard.mLogger.info("Key :::"+entry.getKey());
                        if(entry.getKey().contains("F154")){
                            CreditCard.mLogger.info("inside  F154: ");
                            String lvName = entry.getValue().split(":")[1];
                            CreditCard.mLogger.info("inside  F154: "+lvName);
                            if(formObject.getLVWRowCount(lvName)>0){
                                for(int i=0;i<formObject.getLVWRowCount(lvName);i++){
                                    if("BT".equalsIgnoreCase(formObject.getNGValue(lvName,i,0))){
                                        ext_xml_str += addExtensionValue(entry.getKey(),entry.getValue(),"Y",extcount);
                                        extcount++;
                                        break;
                                    }
                                }
                                continue;
                            }
                        }
                        else if(entry.getKey().contains("F160")){
                        	
                            CreditCard.mLogger.info("Inside F160");
                            if(formObject.getNGValue("CC_Creation_Product").contains("KALYAN") && entry.getKey().contains("Kalyan")){
                                String passport = formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13);
                                String cardprod = formObject.getNGValue("CC_Creation_Product");
                                String lvName = entry.getValue().split(":")[1];
                                if(formObject.getLVWRowCount(lvName)>0){
                                    for(int i=0;i<formObject.getLVWRowCount(lvName);i++){
                                        if(formObject.getNGValue(lvName,i,0).equals(cardprod) && formObject.getNGValue(lvName,i,10).equals(passport)){
                                            ext_xml_str += addExtensionValue(entry.getKey(),entry.getValue(),formObject.getNGValue(lvName,i,8), extcount);
                                            extcount++;
                                            break;
                                        }
                                    }
                                }
                            }
                            else if(entry.getKey().contains("mrbh")) {
                            	try{
                            		if(typeOfCP!=null && typeOfCP.size()>0 && typeOfCP.get(0)!=null && "Islamic".equalsIgnoreCase(typeOfCP.get(0).get(0))){
                                        String query = "SELECT convert(date, Settlement_Date,103) as Settlement_Date FROM ng_rlos_Murabha_Warranty WHERE Murhabha_WIName='"+formObject.getWFWorkitemName()+"'";
                                        String  Settlement_Date = formObject.getDataFromDataSource(query).get(0).get(0);
                                        Settlement_Date = formatDateFromOnetoAnother(Settlement_Date,"yyyy-mm-dd","dd/mm/yyyy");
                                        ext_xml_str += addExtensionValue(entry.getKey(),entry.getValue(),Settlement_Date, extcount);
                                        extcount++;
                                    }
                            	}
                            	catch(Exception e){
                            		 CreditCard.mLogger.info("Exception in murhabah"+e.getMessage());
                            	}
                            	
                            }
                            continue;
                        }
                        
                        else if(entry.getKey().contains("F161")){
                        	if(typeOfCP!=null && typeOfCP.size()>0 && typeOfCP.get(0)!=null && "Islamic".equalsIgnoreCase(typeOfCP.get(0).get(0))){
                            String query = "SELECT File_Id FROM ng_rlos_Murabha_Warranty WHERE Murhabha_WIName='"+formObject.getWFWorkitemName()+"'";
                            ext_xml_str += addExtensionValue(entry.getKey(),entry.getValue(),formObject.getDataFromDataSource(query).get(0).get(0), extcount);
                            extcount++;
                            continue;
                        	}
                        }
                        
                        else if(entry.getKey().contains("F163")){
                            CreditCard.mLogger.info("Inside F163");
                            String emptype = formObject.getNGValue("cmplx_Product_cmplx_ProductGrid",0,6);
                            CreditCard.mLogger.info("Inside F163: "+emptype);
                            String mktCode = "";
                            if(emptype.equalsIgnoreCase("Self Employed")){
                                int rowCount = formObject.getLVWRowCount("cmplx_CompanyDetails_cmplx_CompanyGrid");
                                if(rowCount>0){
                                    for(int i=0;i<rowCount;i++){
                                        if("Business".equalsIgnoreCase(formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid", i,1))){
                                            mktCode = formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",i,28);
                                            ext_xml_str += addExtensionValue(entry.getKey(),entry.getValue(),mktCode,extcount);
                                            extcount++;
                                            break;
                                        }
                                    }
                                }
 
                            }
                            else{
                                mktCode = formObject.getNGValue("cmplx_EmploymentDetails_marketcode");
                                ext_xml_str += addExtensionValue(entry.getKey(),entry.getValue(),mktCode,extcount);
                                extcount++;
                            }
                        }
                        else{
                            String PorS = entry.getKey().split(":")[0];
                            String value = formObject.getNGValue(entry.getValue().split(":")[1]);
                            CreditCard.mLogger.info("Inside PorS: "+entry.getValue());
                            CreditCard.mLogger.info("Inside value: " +value);
                            if(value!=null && !"".equalsIgnoreCase(value)&& !"--Select--".equalsIgnoreCase(value)){
                                //    value = ("True".equalsIgnoreCase(value) || "Yes".equalsIgnoreCase(value))?"Y":"N";
                                if ("True".equalsIgnoreCase(value) || "Yes".equalsIgnoreCase(value)){
                                    value="Y";
                                }
                                else if("False".equalsIgnoreCase(value) || "No".equalsIgnoreCase(value)){
                                    value="N";
                                }
                                if("PS".equals(PorS) || ("P".equals(PorS) && primary)){
                                    ext_xml_str += addExtensionValue(entry.getKey(),entry.getValue(),value,extcount);
                                    extcount++;
                                }
                            }
                        }
                    }
 
                    return ext_xml_str;
                }
                catch(Exception e){
                    CreditCard.mLogger.info("PLCommon getExtensions_details method Exception Occure in getReference_details"+CC_Common.printException(e));
                    return ext_xml_str;
                }    
 
        }

			public String addExtensionValue(String mapKey,String mapValue, String value,int extcount_val){
				try{
					//below code added by nikhil for extension fields CR
/*					if(mapKey.contains("F160") || mapKey.contains("F161"))
					{
						return "<Extensions><Type>"+mapKey.split(":")[1]+"</Type><Number>"+extcount_val+"</Number><Value>"+mapValue.split(":")[0]+" "+value+"</Value></Extensions>";
					}
					else
					{*/
						return "<Extensions><Type>"+mapKey.split(":")[1]+"</Type><Number>F"+extcount_val+"</Number><Value>"+mapValue.split(":")[0]+ value+"</Value></Extensions>";
					//}
				}
				catch(Exception e){
					CreditCard.mLogger.info("Exception occured in addExtensionValue: "+CC_Common.printException(e));
					return"";
				}

			}

			public  String Clean_Xml(String InputXml,String Call_name){
				String Output_Xml="";

				try{
					//below change by saurabh on 18th Dec
					if(InputXml.indexOf("&")>-1){
						InputXml=InputXml.replaceAll("&", "ampr");
					}
					Document doc = CC_Common.getDocument(InputXml);
					removeEmptyNodes(doc,Call_name);
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
					//Changes done by deepak 25 Nov 2018 to remove DocumentDet tag in case it dosent contain DocId start.
					if("CARD_NOTIFICATION".equalsIgnoreCase(Call_name) && "DocumentDet".equalsIgnoreCase(tempNode.getNodeName())){
						boolean removecompletenode_flag = true;
						for (int tempnode_len=0;tempnode_len<tempNode.getChildNodes().getLength();tempnode_len++){
							//Changes done for case when Emirates ID is not present. PCSP-722
							if("DocId".equalsIgnoreCase(tempNode.getChildNodes().item(tempnode_len).getNodeName()) && 
									!"".equalsIgnoreCase(tempNode.getChildNodes().item(tempnode_len).getTextContent())){
								removecompletenode_flag = false;
								break;
							}
						}
						
						if(removecompletenode_flag){
							node.removeChild(tempNode);
						}
						else{
							removeEmptyNodes(tempNode,Call_name);
						}
					}//Deepak Changes done for PCSP-367 03Feb2018
					else if("DocDet".equalsIgnoreCase(tempNode.getNodeName())){
						boolean removecompletenode_flag = true;
						for (int tempnode_len=0;tempnode_len<tempNode.getChildNodes().getLength();tempnode_len++){
							//Changes done for case when Emirates ID is not present. PCSP-722
							if("DocNo".equalsIgnoreCase(tempNode.getChildNodes().item(tempnode_len).getNodeName()) && 
									!"".equalsIgnoreCase(tempNode.getChildNodes().item(tempnode_len).getTextContent())){
								removecompletenode_flag = false;
								break;
							}
						}
						
						if(removecompletenode_flag){
							node.removeChild(tempNode);
						}
						else{
							removeEmptyNodes(tempNode,Call_name);
						}
					}
					else{
						removeEmptyNodes(tempNode,Call_name);
					}
				}
				//Changes done by deepak 25 Nov 2018 to remove DocumentDet tag in case it dosent contain DocId start.
				
				boolean emptyElement = node.getNodeType() == Node.ELEMENT_NODE && node.getChildNodes().getLength() == 0;
				boolean emptyText = node.getNodeType() == Node.TEXT_NODE && node.getNodeValue().trim().isEmpty();
				boolean selectText = node.getNodeType() == Node.TEXT_NODE && (node.getNodeValue().trim().equalsIgnoreCase("--Select--")||node.getNodeValue().trim().equalsIgnoreCase("null"));
				//Changes done to incorporate blank tag removal for all calls Deepak 24june 2018
				if (emptyElement || emptyText || selectText) {
					if(!node.hasAttributes()) {
						node.getParentNode().removeChild(node);
					}
				}
				//Deepak 22 Dec 2018 Changes done to send data in MW call in upper case Start
				//commented as getting error for Card Creation.
				/*else{
					if(fin_call_name.toUpperCase().contains(Call_name.toUpperCase())
							&& node.getNodeValue()!=null
							&& !capitalExceptionsTags.toUpperCase().contains(node.getParentNode().getNodeName().toUpperCase())){
						node.setNodeValue(node.getNodeValue().toUpperCase());
					}
				}*/
				//Deepak 22 Dec 2018 Changes done to send data in MW call in upper case END
			}
			
			

			//below functions added by akshay for drop 4
			public String getSupplement_PersonDetails(FormReference formObject,String callName){
				String str="";
				try{
					Common_Utils common=new Common_Utils(CreditCard.mLogger);
					CreditCard.mLogger.info("Inside  getSupplement_PersonDetails() for callName: "+callName);
					if(callName.equalsIgnoreCase("NEW_CUSTOMER_REQ")){
						CreditCard.mLogger.info("CC_Comn.MultipleAppGridSelectedRow('MultipleApp_AppType')"+CC_Comn.MultipleAppGridSelectedRow("MultipleApp_AppType"));
						if( "SUPPLEMENT".equalsIgnoreCase(CC_Comn.MultipleAppGridSelectedRow("MultipleApp_AppType")) && formObject.getLVWRowCount("SupplementCardDetails_cmplx_supplementGrid")>0){ 
							for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_supplementGrid");i++){
								CreditCard.mLogger.info("Passport of multiple applicant:"+CC_Comn.MultipleAppGridSelectedRow("MultipleApp_AppPass"));
								if(CC_Comn.MultipleAppGridSelectedRow("MultipleApp_AppPass").equals(formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,3))){
									str=str+"<PersonDetails><TitlePrefix>"+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,14)+"</TitlePrefix>";
									str=str+"<FirstName>"+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,0)+"</FirstName>";
									str=str+"<LastName>"+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,2)+"</LastName>";
									str=str+"<ShortName>"+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,0).substring(0,1)+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,2).substring(0,1)+"</ShortName>";
									str=str+"<Gender>"+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,11)+"</Gender>";
									str=str+"<MotherMaidenName>"+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,12)+"</MotherMaidenName>";
									str=str+"<MinorFlag>"+(Float.parseFloat(common.getAge(formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,4)))<21?"Y":"N")+"</MinorFlag>";
									if(formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,16).equalsIgnoreCase("true")){
										str=str+"<NonResidentFlag>"+"Y"+"</NonResidentFlag>";
									}
									else{
										str=str+"<NonResidentFlag>"+"N"+"</NonResidentFlag>";
									}
									str=str+"<ResCountry>"+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,8)+"</ResCountry>";
									str=str+"<MaritalStatus>"+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,40)+"</MaritalStatus>";
									str=str+"<Nationality>"+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,5)+"</Nationality>";
									str=str+"<DateOfBirth>"+common.Convert_dateFormat(formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,4), "dd/MM/yyyy","yyyy-MM-dd")+"</DateOfBirth>";
									str=str+"</PersonDetails>";	
									break;
								}
							}
						}
					}
					else if(callName.equalsIgnoreCase("CUSTOMER_UPDATE_REQ")){
						if( "SUPPLEMENT".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)) && formObject.getLVWRowCount("SupplementCardDetails_cmplx_supplementGrid")>0){ 
							for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_supplementGrid");i++){
								if(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13).equals(formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,3))){
									str=str+"<RtlAddnlDet><MothersName>"+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,12)+"</MothersName>";
									str=str+"<MaritalStatus></MaritalStatus><EmployerCode></EmployerCode><EmploymentType></EmploymentType>";
									str=str+"<EmployeeStatus>"+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,32)+"</EmployeeStatus>";						
									str=str+"<Desig>"+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,31)+"</Desig>";	
									break;
								}
							}
						}
						str=str+getFATCA_details(formObject);
						str=str+getKYC_details(formObject);
						str=str+getCustOECD_details(callName);
						str=str+"</RtlAddnlDet>";	

					}

					else if(callName.equalsIgnoreCase("NEW_CREDITCARD_REQ")){
						if( "SUPPLEMENT".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)) && formObject.getLVWRowCount("SupplementCardDetails_cmplx_supplementGrid")>0){ 
							String phno="",email="";	
							for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_supplementGrid");i++){
								if(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13).equals(formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,3))){
									str=str+"<Title>"+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,14)+"</Title>";
									str=str+"<FirstName>"+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,0)+"</FirstName>";
									str=str+"<LastName>"+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,2)+"</LastName>";
									str=str+"<FullName>"+formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),4)+"</FullName>";
									str=str+"<MotherName>"+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,12)+"</MotherName>";
									str=str+"<DOB>"+common.Convert_dateFormat(formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,4), "dd/MM/yyyy","yyyy-MM-dd")+"</DOB>";
									if(formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,11).equals("M")){
										str=str+"<Gender>1</Gender>";
									}
									else{
										str=str+"<Gender>2</Gender>";
									}
									str=str+"<MaritalStatus>"+getMaritalstatusforSupplementary()+"</MaritalStatus>";
									str=str+"<Nationality>"+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,5)+"</Nationality>";
									str=str+"<Department>"+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,31)+"</Department>";						
									str=str+"<Designation>"+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,31)+"</Designation>";						
									str=str+"<ProfileSerNo>RAKH</ProfileSerNo>";
									str=str+"<DocumentDetails><DocType>PPT</DocType>";
									str=str+"<DocId>"+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,3)+"</DocId></DocumentDetails>";
									phno=formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,6);
									email=formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,21);
									break;
								}
							}
							//str=str+getCustAddress_details(callName);
							//Deepak Code change for Address change 
							str=str+getCardCrationDummyAddress_details();
							str=str+"<PhoneFaxDtls><Type>StmtTelephone1</Type>";
							str=str+"<Number>"+phno+"</Number></PhoneFaxDtls>";
							str=str+"<EmailDtls><Type>StmtEmail</Type>";
							str=str+"<EmailId>"+email+"</EmailId></EmailDtls>";
							str=str+"<EmailDtls><Type>AdditionalEmail</Type>";
							str=str+"<EmailId>"+email+"</EmailId></EmailDtls>";
						}
					}

					else if(callName.equalsIgnoreCase("CARD_NOTIFICATION")){
						if( "SUPPLEMENT".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)) && formObject.getLVWRowCount("SupplementCardDetails_cmplx_supplementGrid")>0){ 
							for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_supplementGrid");i++){
								if(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13).equals(formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,3))){
									str=str+"<PersonDetails><TitlePrefix>"+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,14)+"</TitlePrefix>";
									str=str+"<FirstName>"+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,0)+"</FirstName>";
									str=str+"<LastName>"+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,2)+"</LastName>";
									str=str+"<ShortName>"+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,0).substring(0,1)+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,2).substring(0,1)+"</ShortName>";
									str=str+"<FullName>"+formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),4)+"</FullName>";
									str=str+"<Gender>"+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,11)+"</Gender>";
									str=str+"<MotherMaidenName>"+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,12)+"</MotherMaidenName>";
									str=str+"<MinorFlag>"+(Float.parseFloat(common.getAge(formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,4)))<18?"Y":"N")+"</MinorFlag>";
									str=str+"<NonResidentFlag>"+(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,16).equalsIgnoreCase("true")?"Y":"N")+"</NonResidentFlag>";
									str=str+"<ResidencyStatus>EXPAT</ResidencyStatus>";
									str=str+"<MaritalStatus>"+formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,40)+"</MaritalStatus>";
									str=str+"<Nationality>"+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,5)+"</Nationality>";
									str=str+"<DateOfBirth>"+common.Convert_dateFormat(formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,4), "dd/MM/yyyy","yyyy-MM-dd")+"</DateOfBirth>";
									str=str+"<ResidenceSince>"+calcResidenceSince(formObject)+"</ResidenceSince>";
									str=str+"<ResCountry>"+formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,8)+"</ResCountry>";
									str=str+"<ECRNNum>"+formObject.getNGValue("CC_Creation_ECRN")+"</ECRNNum>";
									str=str+"<CombinedCreditLimit>"+formObject.getNGValue("CC_Creation_CombinedLimit")+"</CombinedCreditLimit>";
									str=str+"</PersonDetails>";	
									break;
								}
							}
						}
					}
					//CreditCard.mLogger.info("Inside  getGuarantor_PersonDetails()--->Final str is: "+str);
					return str;
				}catch(Exception e)
				{
					CreditCard.mLogger.info("Exception occurred inside getSupplement_PersonDetails for call name: "+callName); 
					CreditCard.mLogger.info(CC_Common.printException(e));
					return "";
				}

			}
			public String getMaritalstatusforSupplementary(){
				//CreditCard.mLogger.info("inside 1st if"+"inside maritial NEW_CREDITCARD_REQ");
				String maritalstat = getSupplementaryTagValue(40);
				String marital="5";
				if(maritalstat.equalsIgnoreCase("M")){
					marital="2";
				}
				else if(maritalstat.equalsIgnoreCase("S")){
					marital="1";
				}
				else if(maritalstat.equalsIgnoreCase("D")){
					marital="3";
				}
				else if(maritalstat.equalsIgnoreCase("W")){
					marital="4";
				}


				CreditCard.mLogger.info("PL COMMON"+" after adding maritia] NEW_CREDITCARD_REQ:  "+marital);
				return marital;

			}

			public String getSupplement_DocDetails(FormReference formObject,String callName){
				String str="";
				CreditCard.mLogger.info("Inside  getGuarantor_DocDetails()");

				if(callName.equalsIgnoreCase("NEW_CUSTOMER_REQ")){
					CreditCard.mLogger.info("call name is NEW_CUSTOMER_REQ..!!");
					if( "SUPPLEMENT".equalsIgnoreCase(new CC_Common().MultipleAppGridSelectedRow("MultipleApp_AppType")) && formObject.getLVWRowCount("SupplementCardDetails_cmplx_supplementGrid")>0){ 
						CreditCard.mLogger.info("Appplicant type is supplement!!");
						for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_supplementGrid");i++){
							if(new CC_Common().MultipleAppGridSelectedRow("MultipleApp_AppPass").equals(formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,3))){
								CreditCard.mLogger.info("Passport of supplement row "+i+" is equal to applicant passport!!");
								str=str+generateDocDetailsTag_NewCustomerReq(Arrays.asList("PPT",formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,36),formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,15),formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,5),formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",0,3)));
								str=str+generateDocDetailsTag_NewCustomerReq(Arrays.asList("VISA",formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,37),formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,23),"AE",formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,22)));
								str=str+generateDocDetailsTag_NewCustomerReq(Arrays.asList("EMID",formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,38),formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,18),formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,5),formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",0,7)));
								break;
							}
						}
					}
				}
				else if( formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12).equalsIgnoreCase("Supplement") && formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid")>0){ 
					for(int i=0;i<formObject.getLVWRowCount("SupplementCardDetails_cmplx_SupplementGrid");i++){
						if(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13).equals(formObject.getNGValue("SupplementCardDetails_cmplx_SupplementGrid",i,3))){

							if(callName.equalsIgnoreCase("CUSTOMER_UPDATE_REQ")){
								str=str+generateDocDetailsTag_CIFUpdate(Arrays.asList("PPT",formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,3),formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,36),formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,15)));
								str=str+generateDocDetailsTag_CIFUpdate(Arrays.asList("EMID",formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,7),formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,38),formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,18)));
								str=str+generateDocDetailsTag_CIFUpdate(Arrays.asList("VISA",formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,22),formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,37),formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,23)));
								break;
							}
							else  if(callName.equalsIgnoreCase("CARD_NOTIFICATION")){
								str=str+generateDocDetailsTag_CardNotif(Arrays.asList("EMID",formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,7),formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,38),formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,18)));
								str=str+generateDocDetailsTag_CardNotif(Arrays.asList("VISA",formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,22),formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,37),formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,23)));
								str=str+generateDocDetailsTag_CardNotif(Arrays.asList("PPT",formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,3),formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,36),formObject.getNGValue("SupplementCardDetails_cmplx_supplementGrid",i,15)));
								break;
							}

						}
					}
				}
				CreditCard.mLogger.info("Doc details XML is:"+str);
				return str;
			}


			public String generateDocDetailsTag_NewCustomerReq(List<String> DocValues){
				Common_Utils common=new Common_Utils(CreditCard.mLogger);
				if(!"".equalsIgnoreCase(DocValues.get(4))){
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
				else{
					return"";
				}

			}

			public String generateDocDetailsTag_CIFUpdate(List<String> DocValues){
				Common_Utils common=new Common_Utils(CreditCard.mLogger);
				if(!"".equals(DocValues.get(1))){
					return 
							"<DocDet><DocType>"+DocValues.get(0)+"</DocType>"
							+"<DocIsVerified>Y</DocIsVerified>"
							+"<DocNo>"+DocValues.get(1)+"</DocNo>"
							+"<DocIssDate>"+common.Convert_dateFormat(DocValues.get(2), "dd/MM/yyyy","yyyy-MM-dd")+"</DocIssDate>"
							+"<DocExpDate>"+common.Convert_dateFormat(DocValues.get(3), "dd/MM/yyyy","yyyy-MM-dd")+"</DocExpDate></DocDet>";
				}
				else{
					return"";
				}
			}

			public String generateDocDetailsTag_CardNotif(List<String> DocValues){
				Common_Utils common=new Common_Utils(CreditCard.mLogger);
				if(!"".equals(DocValues.get(1))){
					return 
							"<DocumentDet><DocumentType>"+DocValues.get(0)+"</DocumentType>"
							+"<DocId>"+DocValues.get(1)+"</DocId>"
							+"<DocExpiryFlag>Y</DocExpiryFlag>"
							+"<DocIssueDate>"+	common.Convert_dateFormat(DocValues.get(2), "dd/MM/yyyy","yyyy-MM-dd")+"</DocIssueDate>"
							+"<DocExpDt>"+common.Convert_dateFormat(DocValues.get(3), "dd/MM/yyyy","yyyy-MM-dd")+"</DocExpDt>"
							+"<DocCollected>Y</DocCollected>"
							+"<DocVerified>Y</DocVerified></DocumentDet>";
				}
				else{
					return"";
				}
			}
			public String getFATCA_details(FormReference formObject){
				String str="<FatcaDetails>";
				try{
					Common_Utils common=new Common_Utils(CreditCard.mLogger);
					for(int i=0;i<formObject.getLVWRowCount("cmplx_FATCA_cmplx_GR_FatcaDetails");i++){
						String applicantName=formObject.getNGValue("cmplx_FATCA_cmplx_GR_FatcaDetails",i,14);
						String USRelation=formObject.getNGValue("cmplx_FATCA_cmplx_GR_FatcaDetails",i, 0);
						String TIN=formObject.getNGValue("cmplx_FATCA_cmplx_GR_FatcaDetails",i, 2);
						String fatcaReason=formObject.getNGValue("cmplx_FATCA_cmplx_GR_FatcaDetails",i, 11);
						String signedDate=formObject.getNGValue("cmplx_FATCA_cmplx_GR_FatcaDetails",i, 4);
						String expiryDate=formObject.getNGValue("cmplx_FATCA_cmplx_GR_FatcaDetails",i, 5);
						
						String iddoc=formObject.getNGValue("cmplx_FATCA_cmplx_GR_FatcaDetails",i,6).equals("true")?"ID DOC":"";
						String w8form=formObject.getNGValue("cmplx_FATCA_cmplx_GR_FatcaDetails",i,7).equals("true")?"!W8":"";
						String w9form=formObject.getNGValue("cmplx_FATCA_cmplx_GR_FatcaDetails",i,8).equals("true")?"!W9":"";
						//String lossofnat=formObject.getNGValue("cmplx_FATCA_cmplx_FATCAGrid",i,10).equals("true")?"":"";
						String decforIndv=formObject.getNGValue("cmplx_FATCA_cmplx_GR_FatcaDetails",i,11).equals("true")?"!SELF-ATTEST FORM":"";

						String FatcaDocs=iddoc+w8form+w9form+decforIndv;
						
						if(fatcaReason!=null && !"".equals(fatcaReason)){
							fatcaReason.replaceAll(",", "!");
						}
						else{
							fatcaReason="";
						}
						if(signedDate!=null && !"".equals(signedDate)){
							signedDate = common.Convert_dateFormat(signedDate,"dd/MM/yyyy","yyyy-MM-dd");
						}
						else{
							signedDate="";
						}
						if(expiryDate!=null && !"".equals(expiryDate)){
							expiryDate = common.Convert_dateFormat(expiryDate,"dd/MM/yyyy","yyyy-MM-dd");
						}
						else{
							expiryDate="";
						}
						if(NGFUserResourceMgr_CreditCard.getGlobalVar("Services").contains(formObject.getNGValue("Sub_Product")) || applicantName.split("-")[1].trim().replaceAll(" ","").equalsIgnoreCase(formObject.getNGValue("CC_Creation_CustomerName").trim().replaceAll(" ","")))
						{	
							str=str+"<USRelation>"+(USRelation.equals("NA")?"":USRelation)+"</USRelation>";
							str=str+"<TIN>"+TIN+"</TIN>";
							str=str+"<FatcaReason>"+fatcaReason+"</FatcaReason>";
							str=str+"<DocumentsCollected>"+FatcaDocs+"</DocumentsCollected>";
							str=str+"<SignedDate>"+signedDate+"</SignedDate>";
							str=str+"<SignedExpiryDate>"+expiryDate+"</SignedExpiryDate>";

							break;
						}
					}
				}catch(Exception ex){
					CreditCard.mLogger.info("PLCommon getCustAddress_details method"+ "Exception Occure in generate Address XMl");
					CC_Common.printException(ex);
				}
				str=str+"</FatcaDetails>";
				return str;

			}

			public String getKYC_details(FormReference formObject)	{
				String str="<KYCDetails>";
				for(int i=0;i<formObject.getLVWRowCount("cmplx_KYC_cmplx_KYCGrid");i++){
					String applicantName=formObject.getNGValue("cmplx_KYC_cmplx_KYCGrid",i, 3);
					String KYCHeld=formObject.getNGValue("cmplx_KYC_cmplx_KYCGrid",i, 0);
					String PEP=formObject.getNGValue("cmplx_KYC_cmplx_KYCGrid",i,1);
					if(NGFUserResourceMgr_CreditCard.getGlobalVar("Services").contains(formObject.getNGValue("Sub_Product")) || applicantName.split("-")[1].trim().replaceAll(" ","").equalsIgnoreCase(formObject.getNGValue("CC_Creation_CustomerName").trim().replaceAll(" ","")))
					{
						str=str+"<KYCHeld>"+(KYCHeld.equals("NA")?"":KYCHeld)+"</KYCHeld>";
						str=str+"<PEP>"+(PEP.equals("NA")?"":PEP)+"</PEP>";				
						break;
					}
				}
				str=str+"</KYCDetails>";
				return str;

			}

			public String getCustOECD_details(String call_name){
				CreditCard.mLogger.info("RLOSCommon java file"+ "inside getCustOECD_details : ");
				String  xml_str ="";
				try{
					FormReference formObject = FormContext.getCurrentInstance().getFormReference();
					int add_row_count = formObject.getLVWRowCount("cmplx_OECD_cmplx_GR_OecdDetails");
					CreditCard.mLogger.info("RLOSCommon java file"+ "inside getCustAddress_details add_row_count+ : "+add_row_count);
					if("CUSTOMER_UPDATE_REQ".equalsIgnoreCase(call_name)){
						for (int i = 0; i<add_row_count;i++){
							String City_of_Birth = formObject.getNGValue("cmplx_OECD_cmplx_GR_OecdDetails", i, 3); //0
							String Country_of_Birth=formObject.getNGValue("cmplx_OECD_cmplx_GR_OecdDetails", i, 2);//1
							String Undocumented_Flag=formObject.getNGValue("cmplx_OECD_cmplx_GR_OecdDetails", i, 0);//2
							String UndocumentedFlag_Reason=formObject.getNGValue("cmplx_OECD_cmplx_GR_OecdDetails", i, 1);//3
							String applicantName=formObject.getNGValue("cmplx_OECD_cmplx_GR_OecdDetails", i, 8);//3
							String counTaxResid = formObject.getNGValue("cmplx_OECD_cmplx_GR_OecdDetails", i, 4);
							String tinNum = formObject.getNGValue("cmplx_OECD_cmplx_GR_OecdDetails", i, 5);
							String noTinRes = formObject.getNGValue("cmplx_OECD_cmplx_GR_OecdDetails", i, 6);

							if(NGFUserResourceMgr_CreditCard.getGlobalVar("Services").contains(formObject.getNGValue("Sub_Product")) || applicantName.split("-")[1].trim().replaceAll(" ","").equalsIgnoreCase(formObject.getNGValue("CC_Creation_CustomerName").trim().replaceAll(" ","")))
							{
								xml_str = xml_str + "<OECDDet><CityOfBirth>"+City_of_Birth+"</CityOfBirth> ";
								xml_str = xml_str + "<CountryOfBirth>"+Country_of_Birth+"</CountryOfBirth>";
								xml_str = xml_str + "<CRSUnDocFlg>"+Undocumented_Flag+"</CRSUnDocFlg>";
								xml_str = xml_str + "<CRSUndocFlgReason>"+(UndocumentedFlag_Reason.equals("NA")?"":UndocumentedFlag_Reason)+"</CRSUndocFlgReason>";
								xml_str = xml_str + "<ReporCntryDet><CntryOfTaxRes>"+counTaxResid+"</CntryOfTaxRes>";
								xml_str = xml_str + "<TINNumber>"+tinNum+"</TINNumber>";
								xml_str = xml_str + "<NoTINReason>"+noTinRes+"</NoTINReason></ReporCntryDet></OECDDet>";

								//below break added by saurabh for JIRA - 5129.
								break;
							}
						}

					}
					//CreditCard.mLogger.info("RLOSCommon"+ "OECD tag creation "+ xml_str);
					return xml_str;
				}
				catch(Exception e){
					CreditCard.mLogger.info("PLCommon getCustAddress_details method"+ "Exception Occure in generate Address XMl"+e.getMessage());
					CC_Common.printException(e);
					return xml_str;
				}

			}

			public String calcResidenceSince(FormReference formObject){
				int size=formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
				String residenceSince="",residenceSince_date="";
				try{
					for(int i=0;i<size;i++){
						if(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,13).split("-")[1].equals(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),4)))
						{
							if(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,0).equalsIgnoreCase("RESIDENCE")){
								residenceSince=formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid",i,9);
								CreditCard.mLogger.info("inside ResidenceSince Value of Years in RESD Address: "+residenceSince);
							}
						}
					}
					if(!"".equalsIgnoreCase(residenceSince)){
						SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
						Calendar cal = Calendar.getInstance();
						cal.add(Calendar.YEAR, (int) -Float.parseFloat(residenceSince));
						residenceSince_date=sdf1.format(cal.getTime());
						CreditCard.mLogger.info("inside ResidenceSince Value of residenceSince_date: "+residenceSince_date);
					}
				}
				catch(Exception e){
					CreditCard.mLogger.info("exception occurred inside calcResidenceSince() "+CC_Common.printException(e));
				}
				return residenceSince_date;
			}

			private Map<String, String> CIFEnquiryLockUnlock(List<List<String>> DB_List,FormReference formObject,String callName) {

				CreditCard.mLogger.info("Inside CIFEnquiryLockUnlock()------call name is: "+callName);
				Map<String, String> int_xml = new LinkedHashMap<String, String>();
				Map<String, String> recordFileMap = new HashMap<String, String>();

				try{
					for (List<String> mylist : DB_List) {
						// for(int i=0;i<col_n.length();i++)
						for (int i = 0; i < 8; i++) {
							// CreditCard.mLogger.info("rec: "+records.item(rec));
							CreditCard.mLogger.info(""+ "column length values"+ col_n);
							String[] col_name = col_n.split(",");
							recordFileMap.put(col_name[i], mylist.get(i));
						}
						String parent_tag =  recordFileMap.get("parent_tag_name");
						String tag_name =  recordFileMap.get("xmltag_name");

						if( "SUPPLEMENT".equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12)))
						{
							CreditCard.mLogger.info("inside supplement case");
							CreditCard.mLogger.info("inside supplement case tag name: "+tag_name);
							if("CIFId".equalsIgnoreCase(tag_name) || "CCIFId".equalsIgnoreCase(tag_name) ){
								CreditCard.mLogger.info("inside 1st if inside customer update req");
								String xml_str = int_xml.get(parent_tag);
								xml_str =xml_str+ "<"+tag_name+">"+formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),3)+"</"+ tag_name+">";
								//CreditCard.mLogger.info("PL COMMON  after adding CIFId:  "+xml_str);
								int_xml.put(parent_tag, xml_str);	                            	
							}
							else{	
								int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
							}
						}
						else{	
							int_xml = GenDefault_Input_DB(int_xml,recordFileMap,formObject,callName);
						}
					}
				}
				catch(Exception e){
					CreditCard.mLogger.info("CC Integration "+ " Exception occured in DEDUP_SUMMARY_Custom + "+e.getMessage());

				}

				return int_xml;
			}
			
			
			public String getEmploymentOrCompanyDetails(String tagName){
				try{
				FormReference formObject = FormContext.getCurrentInstance().getFormReference();
				String EmploymentType=formObject.getNGValue("EmploymentType");
				String xml="";
				CreditCard.mLogger.info("Inside getEmploymentOrCompanyDetails tag name ius:"+tagName);
				if(EmploymentType.equalsIgnoreCase("Self Employed")){
					if(tagName.equals("Department") || tagName.equals("Designation")){
						xml=xml+ "<"+tagName+">"+getDesignationAuthSign()+"</"+ tagName+">";
					}
					else if(tagName.equals("OrgName")){
						String compName=formObject.getNGValue("cmplx_CompanyDetails_cmplx_CompanyGrid",1,4);
						xml=xml+ "<"+tagName+">"+compName+"</"+ tagName+">";
					}
				}
				else{
					if(tagName.equals("Department") || tagName.equals("Designation")){
						xml=xml+ "<"+tagName+">"+formObject.getNGValue("cmplx_EmploymentDetails_Designation")+"</"+ tagName+">";
					}
					else if(tagName.equals("OrgName")){
						String empName=formObject.getNGValue("cmplx_EmploymentDetails_EmpName");
						xml=xml+ "<"+tagName+">"+empName+"</"+ tagName+">";
					}
				}
				CreditCard.mLogger.info("Inside getEmploymentOrCompanyDetails final xml is:"+xml);
				return xml;
			}
				catch(Exception e){
					CreditCard.mLogger.info("CC Integration "+ "getEmploymentOrCompanyDetails + "+e.getMessage());
					return "";
				}
			}
			public String getCodeDesc(String Master_Name,String Col_name,String Code){
				String desc = "";
				try{
					FormReference formObject = FormContext.getCurrentInstance().getFormReference();
					String query="select "+Col_name+" from "+Master_Name+" with (nolock) where code='"+Code+"' AND isActive='Y'";
					CreditCard.mLogger.info("query name :"+query);
					List<List<String>> result=formObject.getDataFromDataSource(query);
					if(!result.isEmpty()){
						if(null!=result.get(0).get(0) && !result.get(0).get(0).equals("")){
							desc = result.get(0).get(0);
						}
					}
				}
				catch(Exception e){
					CreditCard.mLogger.info("CC Integration exception in getCodeDesc: "+ e.getMessage());
				}
				return desc;
				
			}
			public String getNewCardPhoneFaxDtls(){
				String str_PhoneFaxDtls="";
				try{
					FormReference formObject = FormContext.getCurrentInstance().getFormReference();
					int add_row_count = formObject.getLVWRowCount("cmplx_AddressDetails_cmplx_AddressGrid");
					String pref_Address_type="";
					String customer_type =formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),12);
					//++below code added by nikhil for Self-Supp CR
					if("SUPPLEMENT".equalsIgnoreCase(customer_type) && formObject.getNGValue("cmplx_Customer_PAssportNo").equalsIgnoreCase(formObject.getNGValue("cmplx_CCCreation_cmplx_CCCreationGrid",formObject.getSelectedIndex("cmplx_CCCreation_cmplx_CCCreationGrid"),13)))
					{
						customer_type="PRIMARY";
					}
					//--above code added by nikhil for Self-Supp CR
					for (int i = 0; i<add_row_count;i++){
						if(formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 10).equalsIgnoreCase("true"))//10
							pref_Address_type = formObject.getNGValue("cmplx_AddressDetails_cmplx_AddressGrid", i, 0); //0
					}
					
					if("OFFICE".equalsIgnoreCase(pref_Address_type)){
						str_PhoneFaxDtls="<PhoneFaxDtls><Type>StmtTelephone1</Type><Number>office_number</Number></PhoneFaxDtls><PhoneFaxDtls><Type>AdditionalTelephone1</Type><Number>residence_number</Number></PhoneFaxDtls><PhoneFaxDtls><Type>AdditionalMobileNo</Type><Number>Primary_mobile_number</Number></PhoneFaxDtls>";
					}
					else if("RESIDENCE".equalsIgnoreCase(pref_Address_type)){
						str_PhoneFaxDtls="<PhoneFaxDtls><Type>StmtTelephone1</Type><Number>residence_number</Number></PhoneFaxDtls><PhoneFaxDtls><Type>StmtMobileNo</Type><Number>Primary_mobile_number</Number></PhoneFaxDtls><PhoneFaxDtls><Type>AdditionalTelephone1</Type><Number>office_number</Number></PhoneFaxDtls>";
					}
					if("PRIMARY".equalsIgnoreCase(customer_type)){
						str_PhoneFaxDtls = str_PhoneFaxDtls.replace("Primary_mobile_number", formObject.getNGValue("AlternateContactDetails_MobileNo1"));
					}
					else{
						str_PhoneFaxDtls = str_PhoneFaxDtls.replace("Primary_mobile_number", formObject.getNGValue("SupplementCardDetails_MobNo"));
					}
					
					str_PhoneFaxDtls = str_PhoneFaxDtls.replace("residence_number", formObject.getNGValue("AlternateContactDetails_ResidenceNo"));
					str_PhoneFaxDtls = str_PhoneFaxDtls.replace("office_number", formObject.getNGValue("AlternateContactDetails_OfficeNo"));
				}
				catch(Exception e){
					CreditCard.mLogger.info("CC Integration exception in getNewCardPhoneFaxDtls: "+ e.getMessage());
					str_PhoneFaxDtls="";
				}
				return str_PhoneFaxDtls;
			}
			
			public String formatDateFromOnetoAnother(String date,String givenformat,String resultformat) {
			    String result = "";
			    SimpleDateFormat givenDateformat;
			    SimpleDateFormat resultDateformat;
			    try {
			    	givenDateformat = new SimpleDateFormat(givenformat);
			    	resultDateformat = new SimpleDateFormat(resultformat);
			        result = resultDateformat.format(givenDateformat.parse(date));
			    }
			    catch(Exception e) {
			        e.printStackTrace();
			        return date;
			    }
			    finally {
			    	givenDateformat=null;
			    	resultDateformat=null;
			    }
			    return result;
			}
		}
