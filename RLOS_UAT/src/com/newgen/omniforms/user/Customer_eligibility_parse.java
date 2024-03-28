package com.newgen.omniforms.user;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.skutil.SKLogger;

public class Customer_eligibility_parse {

	/**
	 * @param args
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
		// TODO Auto-generated method stub
		
		String output = "<MQ_RESPONSE_XML><EE_EAI_HEADER><MsgFormat>CUSTOMER_ELIGIBILITY</MsgFormat><MsgVersion>0001</MsgVersion><RequestorChannelId>CAS</RequestorChannelId><RequestorUserId>RAKUSER</RequestorUserId><RequestorLanguage>E</RequestorLanguage><RequestorSecurityInfo>secure</RequestorSecurityInfo><ReturnCode>0000</ReturnCode><ReturnDesc>Successful</ReturnDesc><MessageId>[B@2e679401</MessageId><Extra1>REP||BPM.123</Extra1><Extra2>2017-04-01T03:44:51.678+04:00</Extra2></EE_EAI_HEADER><CustomerEligibilityResponse><BankId>RAK</BankId><CustomerDetails><SearchType>Internal</SearchType><CustId>2527255</CustId><PassportNum>PPT78945612</PassportNum><BlacklistFlag>Y</BlacklistFlag><DuplicationFlag>N</DuplicationFlag><NegatedFlag>N</NegatedFlag><Products><ProductType>ACCOUNT ONLY</ProductType><NoOfProducts>1</NoOfProducts></Products></CustomerDetails><CustomerDetails><SearchType>Internal</SearchType><CustId>2527256</CustId><PassportNum>PPT78945612</PassportNum><BlacklistFlag>Y</BlacklistFlag><DuplicationFlag>N</DuplicationFlag><NegatedFlag>N</NegatedFlag><Products><ProductType>ACCOUNT ONLY</ProductType><NoOfProducts>1</NoOfProducts></Products></CustomerDetails></CustomerEligibilityResponse></MQ_RESPONSE_XML>";
		try{
			Map<String, HashMap<String, String> > Cus_details = new HashMap<String, HashMap<String, String>>();
			String passport_list = "";
			//Map<String, String> cif_details = new HashMap<String, String>();
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(output)));
	        SKLogger.writeLog("name is doc : ", doc+"");
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
                			 System.out.println("tag name and tag value can not be null:: tag name: "+tag_name+ " tag value: " +tag_value);
                		 }
                		
                	 }
                	 Cus_details.put(cif_details.get("CustId"), (HashMap<String, String>) cif_details) ;
                }
	        }
	        int Prim_cif = primary_cif_identify(Cus_details);
	        if(Prim_cif!=0){
	        	Map<String, String> prim_entry = new HashMap<String, String>();
	        	prim_entry = Cus_details.get(Prim_cif+"");
	        	String primary_pass = prim_entry.get("PassportNum");
	        	passport_list = passport_list.replace(primary_pass, "");
	        	set_nonprimaryPassport(passport_list);
	        }
	        	
	        System.out.println("Prim_cif: "+ Prim_cif);
	        
		}
		catch(Exception e){
			System.out.println("Exception occured while parsing Customer Eligibility : "+ e.getMessage());
		}
	}
	public static int primary_cif_identify(Map<String, HashMap<String, String>> cusDetails )
	{
		Map<String, String> prim_entry = new HashMap<String, String>();
		Map<String, String> curr_entry = new HashMap<String, String>();
		int primary_cif = 0;
		
				Iterator<Map.Entry<String, HashMap<String, String>>> itr = cusDetails.entrySet().iterator();
				while(itr.hasNext()){
					Map.Entry<String, HashMap<String, String>> entry =  itr.next();
					
					curr_entry = entry.getValue();
					if(curr_entry.get("SearchType").equalsIgnoreCase("Internal"))
					{
						if(primary_cif==0 && curr_entry.containsKey("Products")){
						primary_cif = Integer.parseInt(entry.getKey());
						}
						else if(curr_entry.containsKey("Products"))
						{
							prim_entry = cusDetails.get(primary_cif+"");
							int prim_entry_prod_no = Integer.parseInt(prim_entry.get("Products"));
							int curr_entry_prod_no = Integer.parseInt(curr_entry.get("Products"));
						
							if(curr_entry_prod_no>prim_entry_prod_no){
								primary_cif = Integer.parseInt(entry.getKey());
							}
							else if(curr_entry_prod_no==prim_entry_prod_no)
							{
								int prim_cif_no = Integer.parseInt(prim_entry.get("CustId"));
								int curr_cif_no = Integer.parseInt(curr_entry.get("CustId"));
								if(curr_cif_no>prim_cif_no)
									primary_cif = curr_cif_no;
							
							}
						
						}
					}
					
				}
		
		return primary_cif;
	}
	public static void set_nonprimaryPassport(String pass_list){
		 FormReference formObject = FormContext.getCurrentInstance().getFormReference();
		try{
			if(pass_list.contains(",")){
				String[] pass_list_arr = pass_list.split(",");
				for(int i= 0; i<pass_list_arr.length && i<4 ; i++){
					formObject.setNGValue("cmplx_Customer_Passport"+(i+2),pass_list_arr[i]);
				}
			}
			
		}
		catch(Exception e){
			System.out.println("Exception occured while seting non primary CIF: "+ e.getMessage());
		}
		
	}

}
