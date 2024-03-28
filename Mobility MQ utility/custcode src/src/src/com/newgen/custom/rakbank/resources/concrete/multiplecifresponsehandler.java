package com.newgen.custom.rakbank.resources.concrete;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.newgen.mcap.core.external.logging.concrete.LogMe;

public class multiplecifresponsehandler {
	//	public static void main(String args[]){
	//		
	//		JSONObject object=new JSONObject();
	//		multiplecifresponsehandler test=new multiplecifresponsehandler();
	//		test.parse_cif_eligibility("<MQ_RESPONSE_XML> <EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"> 	<EE_EAI_HEADER> 		<MsgFormat>CUSTOMER_ELIGIBILITY</MsgFormat> 		<MsgVersion>0001</MsgVersion> 		<RequestorChannelId>CAS</RequestorChannelId> 		<RequestorUserId>RAKUSER</RequestorUserId> 		<RequestorLanguage>E</RequestorLanguage> 		<RequestorSecurityInfo>secure</RequestorSecurityInfo> 		<ReturnCode>0000</ReturnCode> 		<ReturnDesc>Successful</ReturnDesc> 		<MessageId>[B@3f329dcb</MessageId> 		<Extra1>REP||BPM.123</Extra1> 		<Extra2>2017-04-21T05:22:55.601+04:00</Extra2> 	</EE_EAI_HEADER> 	<CustomerEligibilityResponse> 		<BankId>RAK</BankId> 		<CustomerDetails> 			<SearchType>Internal</SearchType> 			<CustId>0213223</CustId> 			<PassportNum>PPT78945612</PassportNum> 			<BlacklistFlag>Y</BlacklistFlag> 			<DuplicationFlag>N</DuplicationFlag> 			<NegatedFlag>N</NegatedFlag> 			<Products> 				<ProductType>ACCOUNT ONLY</ProductType> 				<NoOfProducts>1</NoOfProducts> 			</Products> 			<Products> 				<ProductType>CAPS</ProductType> 				<NoOfProducts>1</NoOfProducts> 			</Products> 			<Products> 				<ProductType>RLS</ProductType> 				<NoOfProducts>1</NoOfProducts> 			</Products> 		</CustomerDetails> 		<CustomerDetails> 			<SearchType>Internal</SearchType> 			<CustId>2527255</CustId> 			<PassportNum>PPT78945912</PassportNum> 			<BlacklistFlag>Y</BlacklistFlag> 			<DuplicationFlag>N</DuplicationFlag> 			<NegatedFlag>N</NegatedFlag> 			<Products> 				<ProductType>ACCOUNT ONLY</ProductType> 				<NoOfProducts>1</NoOfProducts> 			</Products> 		</CustomerDetails> 	</CustomerEligibilityResponse> </EE_EAI_MESSAGE></MQ_RESPONSE_XML>",object);
	//  System.out.println(object.toString());	
	//  }
	public void parse_cif_eligibility(String output,JSONObject masterobject){
		//LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"INSIDE : parse_cif_eligibility");
		try{
			output=output.substring(output.indexOf("<MQ_RESPONSE_XML>")+17,output.indexOf("</MQ_RESPONSE_XML>"));
			Map<String, HashMap<String, String> > Cus_details = new HashMap<String, HashMap<String, String>>();
			String passport_list = "";
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(output)));
			NodeList nl = doc.getElementsByTagName("*");

			for (int nodelen=0; nodelen<nl.getLength();nodelen++){
				Map<String, String> cif_details = new HashMap<String, String>();
				nl.item(nodelen);
				if(nl.item(nodelen).getNodeName().equalsIgnoreCase("CustomerDetails"))
				{
					int no_of_product = 0;
					NodeList childnode  = nl.item(nodelen).getChildNodes();
					for (int childnodelen= 0;childnodelen<childnode.getLength();childnodelen++){
						String tag_name = childnode.item(childnodelen).getNodeName();
						String tag_value=childnode.item(childnodelen).getTextContent();
						if(tag_name!=null && tag_value!=null){
							if(tag_name.equalsIgnoreCase("Products")){
								++no_of_product;
								cif_details.put(tag_name, no_of_product+"");
							}else{
								if(tag_name.equalsIgnoreCase("PassportNum"))
									passport_list = tag_value+ ","+passport_list;
								cif_details.put(tag_name, tag_value);
							}

						}
						else{
						}

					}
					Cus_details.put("cust_detail_"+nodelen+1, (HashMap<String, String>) cif_details) ;
//					Cus_details.put(cif_details.get("CustId"), (HashMap<String, String>) cif_details) ;
				}
			}
			String Prim_cif = primary_cif_identify(Cus_details);
			save_cif_data(Cus_details,Prim_cif,masterobject);
			if(!Prim_cif.isEmpty()){
				//  SKLogger.writeLog("parse_cif_eligibility Primary CIF: ",Prim_cif+"");
				Map<String, String> prim_entry = new HashMap<String, String>();
				prim_entry = Cus_details.get(Prim_cif);
				String primary_pass = prim_entry.get("PassportNum");
				passport_list = passport_list.replace(primary_pass, "");
				set_nonprimaryPassport(Prim_cif,passport_list,masterobject);
			}


		}
		catch(Exception e){
			//LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"INSIDE  Exception: parse_cif_eligibility");
			e.printStackTrace();
		}

	}
	public void save_cif_data(Map<String, HashMap<String, String>> cusDetails ,String prim_cif,JSONObject masterobject){
		//LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"INSIDE : save_cif_data"); 
		try{
			Map<String, String> curr_entry = new HashMap<String, String>();
			Iterator<Map.Entry<String, HashMap<String, String>>> itr = cusDetails.entrySet().iterator();
			List<String> ciflist=new LinkedList<String>();
			//gaurav-s added new jaon array to hold non primary's details 04102017
			JSONArray cifArray=new JSONArray();
			
			while(itr.hasNext()){
				JSONObject tempobj=new JSONObject();
				Map.Entry<String, HashMap<String, String>> entry =  itr.next();
				curr_entry = entry.getValue();
				tempobj.put("SearchType",curr_entry.get("SearchType"));
				tempobj.put("CustId",curr_entry.get("CustId"));
				tempobj.put("PassportNum",curr_entry.get("PassportNum"));
				tempobj.put("BlacklistFlag",curr_entry.get("BlacklistFlag"));
				tempobj.put("BlacklistDate",curr_entry.get("BlacklistDate"));
				tempobj.put("BlacklistReasonCode",curr_entry.get("BlacklistReasonCode"));
				tempobj.put("NegatedReasonCode",curr_entry.get("NegatedReasonCode"));
				tempobj.put("DuplicationFlag",curr_entry.get("DuplicationFlag"));
				tempobj.put("NegatedDate",curr_entry.get("NegatedDate"));
				tempobj.put("NegatedFlag",curr_entry.get("NegatedFlag"));
				tempobj.put("Products",curr_entry.get("Products"));
				
				tempobj.put("prim_flag","N");
				if(curr_entry.get("CustId")!=null && curr_entry.get("CustId").equalsIgnoreCase(prim_cif)){
					tempobj.put("prim_flag","Y");
					masterobject.put("primary_cif", tempobj);
				}
				
					cifArray.add(tempobj);
					ciflist.add(curr_entry.get("CustId"));
					//tempobj.put("prim_flag","N");

				curr_entry=null;
			}
			masterobject.put("ciflist", ciflist);
			//gaurav-s added new jaon array to hold non primary's details 04102017
			masterobject.put("cifArray", cifArray);
		}
		catch(Exception e){
			//LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"INSIDE  Exception: save_cif_data");
			e.printStackTrace();
		}


	}
	public String primary_cif_identify(Map<String, HashMap<String, String>> cusDetails )
	{
		//LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"INSIDE : primary_cif_identify"); 
		String primary_cif ="";
		try{
			Map<String, String> prim_entry = new HashMap<String, String>();
			Map<String, String> curr_entry = new HashMap<String, String>();


			Iterator<Map.Entry<String, HashMap<String, String>>> itr = cusDetails.entrySet().iterator();
			while(itr.hasNext()){
				Map.Entry<String, HashMap<String, String>> entry =  itr.next();

				curr_entry = entry.getValue();
				if(curr_entry.get("SearchType").equalsIgnoreCase("Internal"))
				{
					if(primary_cif.isEmpty()&& curr_entry.containsKey("Products")){
						primary_cif =entry.getKey();
					}
					else if(curr_entry.containsKey("Products"))
					{
						prim_entry = cusDetails.get(primary_cif+"");
						int prim_entry_prod_no = Integer.parseInt(prim_entry.get("Products"));
						int curr_entry_prod_no = Integer.parseInt(curr_entry.get("Products"));

						if(curr_entry_prod_no>prim_entry_prod_no){
							primary_cif = entry.getKey();
						}
						else if(curr_entry_prod_no==prim_entry_prod_no)
						{
							int prim_cif_no = Integer.parseInt(prim_entry.get("CustId"));
							int curr_cif_no = Integer.parseInt(curr_entry.get("CustId"));
							if(curr_cif_no>prim_cif_no)
								primary_cif = curr_entry.get("CustId");

						}

					}
				}

			}

		}
		catch(Exception e){
			//LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"INSIDE  Exception: primary_cif_identify");
			e.printStackTrace();
		}

		return String.valueOf(primary_cif);
	}
	//  public String primary_cif_identify(Map<String, HashMap<String, String>> cusDetails )
	//  {
	//        String primary_cif = "";
	//        try{
	//              Map<String, String> prim_entry = new HashMap<String, String>();
	//              Map<String, String> curr_entry = new HashMap<String, String>();
	//              
	//              
	//                          Iterator<Map.Entry<String, HashMap<String, String>>> itr = cusDetails.entrySet().iterator();
	//                          while(itr.hasNext()){
	//                                Map.Entry<String, HashMap<String, String>> entry =  itr.next();
	//                                
	//                                curr_entry = entry.getValue();
	//                                if(curr_entry.get("SearchType").equalsIgnoreCase("Internal"))
	//                                {
	//                                      if(primary_cif.isEmpty() && curr_entry.containsKey("Products")){
	//                                      primary_cif = entry.getKey();
	//                                      }
	//                                      else if(curr_entry.containsKey("Products"))
	//                                      {
	//                                            prim_entry = cusDetails.get(primary_cif+"");
	//                                            int prim_entry_prod_no = Integer.parseInt(prim_entry.get("Products"));
	//                                            int curr_entry_prod_no = Integer.parseInt(curr_entry.get("Products"));
	//                                      
	//                                            if(curr_entry_prod_no>prim_entry_prod_no){
	//                                                  primary_cif = entry.getKey();
	//                                            }
	//                                            else if(curr_entry_prod_no==prim_entry_prod_no)
	//                                            {
	//                                                 
	//                                                  if(Integer.parseInt(curr_entry.get("CustId"))>Integer.parseInt(prim_entry.get("CustId")))
	//                                                        primary_cif = curr_entry.get("CustId");
	//                                            
	//                                            }
	//                                      
	//                                      }
	//                                }
	//                                
	//                          }
	//        
	//        }
	//        catch(Exception e){
	//             // SKLogger.writeLog("Exception occured while parsing Customer Eligibility : ", e.getMessage());
	//        }
	//              
	//        return primary_cif;
	//  }
	public void set_nonprimaryPassport(String cif_id, String pass_list,JSONObject masterobject){
		//LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"INSIDE : set_nonprimaryPassport");
		JSONObject passportobj=new JSONObject();
		try{
			//   formObject.setNGValue("cmplx_Customer_CIFNO",cif_id);
			if(pass_list.contains(",")){
				String[] pass_list_arr = pass_list.split(",");
				for(int i= 0; i<pass_list_arr.length && i<4 ; i++){
					passportobj.put("pass"+i, pass_list_arr[i]);
					//   formObject.setNGValue("cmplx_Customer_Passport"+(i+2),pass_list_arr[i]);
				}
			}
			masterobject.put("passport_details", passportobj);
			System.out.println(passportobj.toString());     
		}
		catch(Exception e){
			//LogMe.logMe(LogMe.LOG_LEVEL_DEBUG,"INSIDE  Exception: set_nonprimaryPassport");
			e.printStackTrace();
		}

	}
	
//	public static void main(String args[]){
//		
//		String inputxml="<MQ_RESPONSE_XML><?xml version=\"1.0\"?><EE_EAI_MESSAGE xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><EE_EAI_HEADER><MsgFormat>CUSTOMER_ELIGIBILITY</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>CAS150962059883037</MessageId><Extra1>REP||BPM.123</Extra1><Extra2>2017-11-02T03:03:22.846+04:00</Extra2></EE_EAI_HEADER><CustomerEligibilityResponse><BankId>RAK</BankId><CustomerDetails><SearchType>Internal</SearchType><BlacklistFlag>Y</BlacklistFlag><DuplicationFlag>Y</DuplicationFlag><NegatedFlag>N</NegatedFlag></CustomerDetails><CustomerDetails><SearchType>External</SearchType><CustId>12235</CustId><BlacklistFlag>Y</BlacklistFlag><BlacklistReasonCode>MIGR</BlacklistReasonCode><BlacklistDate>2012-06-07</BlacklistDate></CustomerDetails></CustomerEligibilityResponse></EE_EAI_MESSAGE></MQ_RESPONSE_XML>";
//		 multiplecifresponsehandler handler=new multiplecifresponsehandler();
//		 JSONObject obj=new JSONObject();
//		 handler.parse_cif_eligibility(inputxml,obj);
//		 System.out.println(obj);
 //	}

}
