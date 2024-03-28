package com.newgen.custom.rakbank.resources.concrete;

import java.util.HashMap;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.newgen.omni.jts.cmgr.XMLParser;

public class eligiblitymultipletaghandler {
	public void handleresponse(String result){
		JSONArray array=new JSONArray();
		XMLParser parser=new XMLParser();
		parser.setInputXML(result);
		HashMap<String,HashMap<String,String>> parentchildmap=new HashMap<String,HashMap<String,String>>();
		 HashMap<String,String> xmlapkmap=new HashMap<String,String>();
		 xmlapkmap.put("PhnType", "PhnType");
		 xmlapkmap.put("PhoneNo", "PhoneNo");
		 parentchildmap.put("PhnDet", xmlapkmap);
		Set<String> parentset=parentchildmap.keySet();
		for(String parent : parentset){
			
			int count=parser.getNoOfFields(parent);
			Set<String> xmltags=parentchildmap.get(parent).keySet();
			String value="";
			XMLParser newxmlpareser=new XMLParser();
			for(int i=1;i<=count;i++){
				JSONObject tempobject=new JSONObject();
				if(i == 1){
					value=parser.getNextValueOf(parent);
					newxmlpareser.setInputXML(value);
					for(String xmltag : xmltags ){
						tempobject.put(parentchildmap.get(parent).get(xmltag),newxmlpareser.getValueOf(xmltag));
					}
					
				}
				else {
					value=parser.getNextValueOf(parent);
					newxmlpareser.setInputXML(value);
					for(String xmltag : xmltags ){
						tempobject.put(parentchildmap.get(parent).get(xmltag),newxmlpareser.getValueOf(xmltag));
					}

			}
			array.add(tempobject);
			}
		}
		System.out.println(array.toString());
	}
	public static void main(String args[]){
		String test="<APMQPUTGET_Output> <MQ_RESPONSE_XML> <?xml version=\"1.0\"?> <EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"> <EE_EAI_HEADER> <MsgFormat>CUSTOMER_DETAILS</MsgFormat> <MsgVersion>0000</MsgVersion> <RequestorChannelId>CAS</RequestorChannelId> <RequestorUserId>RAKUSER</RequestorUserId> <RequestorLanguage>E</RequestorLanguage> <RequestorSecurityInfo>secure</RequestorSecurityInfo> <ReturnCode>0000</ReturnCode> <ReturnDesc>Successful</ReturnDesc> <MessageId>cas1497247658245</MessageId> <Extra1>REP||LAXMANRET.LAXMANRET</Extra1> <Extra2>2017-06-12T10:07:40.044+04:00</Extra2> </EE_EAI_HEADER> <FetchCustomerDetailsRes> <BankId>RAK</BankId> <CIFDet> <CIFID>0213223</CIFID> <RetCorpFlag>R</RetCorpFlag> <CifType>EJ</CifType> <CustomerStatus>ACTVE</CustomerStatus> <CustomerSegment>PBD</CustomerSegment> <CustomerSubSeg>PBN</CustomerSubSeg> <AECBConsentHeld>Y</AECBConsentHeld> <IsStaff>Y</IsStaff> <IsPremium>N</IsPremium> <IsWMCustomer>N</IsWMCustomer> <BlackListFlag>N</BlackListFlag> <NegativeListOvlFlag>N</NegativeListOvlFlag> <CreditGradeCode>P2</CreditGradeCode> <GCDNo>20082642</GCDNo> <PhnDet> <PhnType>CELLPH1</PhnType> <PhnPrefFlag>Y</PhnPrefFlag> <PhnCountryCode>00971</PhnCountryCode> <PhnLocalCode>560213223</PhnLocalCode> <PhoneNo>00971560213223</PhoneNo> </PhnDet> <PhnDet> <PhnType>FAXO1</PhnType> <PhnPrefFlag>N</PhnPrefFlag> <PhnCountryCode>00971</PhnCountryCode> <PhnLocalCode>420213223</PhnLocalCode> <PhoneNo>00971420213223</PhoneNo> </PhnDet> <PhnDet> <PhnType>OFFCPH1</PhnType> <PhnPrefFlag>N</PhnPrefFlag> <PhnCountryCode>00971</PhnCountryCode> <PhnCityCode>432</PhnCityCode> <PhnLocalCode>420326407</PhnLocalCode> <PhoneNo>00971432420326407</PhoneNo> </PhnDet> <PhnDet> <PhnType>OVHOMEPH</PhnType> <PhnPrefFlag>N</PhnPrefFlag> <PhnCountryCode>91</PhnCountryCode> <PhnLocalCode>620213223</PhnLocalCode> <PhoneNo>91620213223</PhoneNo> </PhnDet> <AddrDet> <AddressType>RESIDENCE</AddressType> <EffectiveFrom>2008-05-15</EffectiveFrom> <EffectiveTo>2099-12-31</EffectiveTo> <HoldMailFlag>N</HoldMailFlag> <ReturnFlag>N</ReturnFlag> <AddrPrefFlag>N</AddrPrefFlag> <AddressLine1>12345</AddressLine1> <AddressLine2>PREMISE NAME FOR     0213223</AddressLine2> <AddressLine3>STREET NAME FOR     0213223</AddressLine3> <ResidenceType>R</ResidenceType> <City>DXB</City> <Country>AE</Country> </AddrDet> <AddrDet> <AddressType>OFFICE</AddressType> <EffectiveFrom>2008-05-15</EffectiveFrom> <EffectiveTo>2099-12-31</EffectiveTo> <HoldMailFlag>N</HoldMailFlag> <ReturnFlag>N</ReturnFlag> <AddrPrefFlag>N</AddrPrefFlag> <AddressLine1>125411111111115</AddressLine1> <AddressLine2>PREMISE NAME FOR 0326407</AddressLine2> <AddressLine3>STREET NAME FOR 0326407</AddressLine3> <AddressLine4>Addr line 4</AddressLine4> <ResidenceType>Rent</ResidenceType> <POBox>12345</POBox> <City>DXB</City> <Country>AE</Country> </AddrDet> <AddrDet> <AddressType>Mailing</AddressType> <EffectiveFrom>2008-05-15</EffectiveFrom> <EffectiveTo>2099-12-31</EffectiveTo> <HoldMailFlag>N</HoldMailFlag> <ReturnFlag>N</ReturnFlag> <AddrPrefFlag>N</AddrPrefFlag> <AddressLine1>12345</AddressLine1> <AddressLine2>PREMISE NAME FOR      0213223X</AddressLine2> <AddressLine3>STREET NAME FOR ,  0213223</AddressLine3> <ResidenceType>R</ResidenceType> <POBox>12345</POBox> <City>DXB</City> <Country>AE</Country> </AddrDet> <AddrDet> <AddressType>Swift</AddressType> <EffectiveFrom>2017-04-03</EffectiveFrom> <EffectiveTo>2099-12-31</EffectiveTo> </AddrDet> <EmlDet> <EmlType>ELML1</EmlType> <EmlPrefFlag>Y</EmlPrefFlag> <Email>test@gmail.com</Email> </EmlDet> <EmlDet> <EmlType>ELML2</EmlType> <EmlPrefFlag>N</EmlPrefFlag> <Email>1234567891@RAKBANK.AE</Email> </EmlDet> <EmlDet> <EmlType>HOMEEML</EmlType> <EmlPrefFlag>N</EmlPrefFlag> <Email>deepthi.n@rakbanktst.ae</Email> </EmlDet> <EmlDet> <EmlType>WORKEML</EmlType> <EmlPrefFlag>N</EmlPrefFlag> <Email>12345_67891@RAKBANK.AE</Email> </EmlDet> <DocDet> <DocType>PPT</DocType> <DocTypeDesc>PASSPORT</DocTypeDesc> <DocIsVerified>Y</DocIsVerified> <DocNo>PPT78945612</DocNo> <DocExpDate>2020-12-29</DocExpDate> </DocDet> <DocDet> <DocType>DRILV</DocType> <DocTypeDesc>DRIVING LICENSE</DocTypeDesc> <DocIsVerified>Y</DocIsVerified> <DocNo>1441392</DocNo> <DocExpDate>2019-06-03</DocExpDate> </DocDet> <DocDet> <DocType>VISA</DocType> <DocTypeDesc>VISA FILE NUMBER</DocTypeDesc> <DocIsVerified>Y</DocIsVerified> <DocNo>73610003</DocNo> <DocExpDate>2020-12-29</DocExpDate> </DocDet> <DocDet> <DocType>EMID</DocType> <DocTypeDesc>Emirates Id</DocTypeDesc> <DocNo>784198546081476</DocNo> <DocExpDate>2016-12-29</DocExpDate> </DocDet> <BuddyRMDetails> <BuddyRMName>PERSONAL BANKER</BuddyRMName> <BuddyRMPhone>8004048</BuddyRMPhone> </BuddyRMDetails> <BackupRMDetails> </BackupRMDetails> <RetAddnlDet> <Title>MR.</Title> <ShortName>FIRST NAME</ShortName> <CustomerName>FIRST NAME FOR 0213223 VIMALA</CustomerName> <FName>FIRST NAME FOR 0213223</FName> <LName>VIMALA</LName> <Gender>M</Gender> <DOB>1989-02-03</DOB> <MinorFlg>N</MinorFlg> <MaritialStatus>M</MaritialStatus> <MotherMaidenName>July</MotherMaidenName> <Nationality>IN</Nationality> <ResidentCountry>AE</ResidentCountry> <CustType>EJ</CustType> <NoOfDepndant>2</NoOfDepndant> <SalaryTranflag>Y</SalaryTranflag> <ResideSince>2008-05-14</ResideSince> <CustomerNRIFlag>N</CustomerNRIFlag> <EmpType>S</EmpType> <EmployerCode>721567</EmployerCode> <EmployerName>DHONI LLC.</EmployerName> <Department>SME</Department> <EmpStatus>A</EmpStatus> <DOJ>2012-05-10</DOJ> <Designation>CEO</Designation> <GrossSalary>0E+0</GrossSalary> <TotHouseholdInc>0E+0</TotHouseholdInc> <InvIncome>0E+0</InvIncome> <OthIncome>0E+0</OthIncome> <Commissions>0E+0</Commissions> <AssessedIncome>0E+0</AssessedIncome> <HRA>0E+0</HRA> <RentInc>0E+0</RentInc> <MnthlyDispInc>0E+0</MnthlyDispInc> <MnthlyHouseholdExp>0E+0</MnthlyHouseholdExp> <AcctType>ODA</AcctType> <AcctNum>0018213223001</AcctNum> </RetAddnlDet> <FatcaDet> <DocumentsCollected>ID DOC</DocumentsCollected> </FatcaDet> <KYCDet> <KYCHeld>N</KYCHeld> <PEP>NPEP</PEP> </KYCDet> <OECDDet> <CityOfBirth>DXB</CityOfBirth> <CountryOfBirth>AE</CountryOfBirth> <ReporCntryDet> <CntryOfTaxRes>AE</CntryOfTaxRes> <NoTINReason>C-NO TIN REQD</NoTINReason> <MiscellaneousID>13445428</MiscellaneousID> </ReporCntryDet> </OECDDet> </CIFDet> <DealInfo> </DealInfo> </FetchCustomerDetailsRes> </EE_EAI_MESSAGE> </MQ_RESPONSE_XML> </APMQPUTGET_Output>";
		eligiblitymultipletaghandler handler=new eligiblitymultipletaghandler();
		handler.handleresponse(test);
	}
}
